package handlers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.Logger;
import play.cache.NamedCache;
import play.libs.Json;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import utils.BizUtils;
import utils.EncodeUtils;
import utils.ValidationUtil;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static play.mvc.Results.ok;

/**
 * Created by win7 on 2016/7/1.
 */
public class ApiCheckActionCreator implements play.http.ActionCreator {

    @Inject
    BizUtils bizUtils;
    @Inject
    EncodeUtils encodeUtils;

    @Inject
    @NamedCache("redis")
    protected play.cache.redis.AsyncCacheApi redis;

    Logger.ALogger logger = Logger.of(ApiCheckActionCreator.class);
    public static final String API_NONE_KEY = "API_NONE_KEY:";

    @Override
    public Action createAction(Http.Request request, Method actionMethod) {
        return new Action.Simple() {
            @Override
            public CompletionStage<Result> call(Http.Request req) {
                String uri = req.uri();
                if (uri.contains("noauth") || uri.contains("/v2/ocpp/")) {
                    return delegate.call(req);
                } else {
                    if (!isValidRequest(req)) return CompletableFuture.supplyAsync(() -> onUnauthorized(req));
                }
                return delegate.call(req);
            }
        };
    }

    public boolean isValidRequest(Http.Request request) {
        String uri = request.uri();
        String authToken = bizUtils.getUIDFromRequest(request);
        if (ValidationUtil.isEmpty(authToken)) return false;
        //todo 这里需要做限频
        if (uri.contains("/v2/")) {
            String timeStamp = "";
            String md5 = "";
            String nonce = "";
            Optional<String> tOptional = request.getHeaders().get("t");
            if (tOptional.isPresent()) {
                timeStamp = tOptional.get();
            }

            Optional<String> sOptional = request.getHeaders().get("s");
            if (sOptional.isPresent()) {
                md5 = sOptional.get();
            }
            Optional<String> nonceOptional = request.getHeaders().get("nonce");
            if (nonceOptional.isPresent()) {
                nonce = nonceOptional.get();
            }
            if (ValidationUtil.isEmpty(nonce) || ValidationUtil.isEmpty(md5) || ValidationUtil.isEmpty(timeStamp))
                return false;
            long timeStampLong = Long.parseLong(timeStamp);
            long currentTime = System.currentTimeMillis() / 1000;
            if (currentTime > timeStampLong + 30) return false;
//            if (nonce.length() != 18) return Optional.empty();
            String key = API_NONE_KEY + nonce;
            try {
                Optional<Object> nonceKeyOptional = redis.sync().get(key);
                if (nonceKeyOptional.isPresent()) return false;
                String salt = authToken + EncodeUtils.API_SALT + timeStamp + nonce;
                String md5FirstTime = encodeUtils.getMd5(salt);
                String md5SecondTime = encodeUtils.getMd5(authToken + md5FirstTime);
                if (md5.equals(md5SecondTime)) {
                    redis.set(key, key, 24 * 3600);
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return false;
        }
        return true;
    }

    public Result onUnauthorized(Http.Request req) {
        ObjectNode node = Json.newObject();
        node.put("code", -1);
        node.put("reason", bizUtils.getMessageValue(req, "base.apiCheck.error"));
        return ok(node);
    }
}
