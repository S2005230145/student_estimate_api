package security;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.Logger;
import play.cache.NamedCache;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import utils.BizUtils;
import utils.EncodeUtils;
import utils.ValidationUtil;

import javax.inject.Inject;
import java.util.Optional;

public class ApiSecured extends Security.Authenticator {

    @Inject
    EncodeUtils encodeUtils;
    @Inject
    BizUtils businessUtils;

    @Inject
    @NamedCache("redis")
    protected play.cache.AsyncCacheApi redis;

    Logger.ALogger logger = Logger.of(ApiSecured.class);
    public static final String API_NONE_KEY = "API_NONE_KEY:";

    /**
     * api认证接口
     *
     * @return
     */
    @Override
    public Optional<String> getUsername(Http.Request request) {
        String uri = request.uri();
        String authToken = "";
        String uidToken = businessUtils.getUIDFromRequest(request);
        if (!ValidationUtil.isEmpty(uidToken)) {
            Optional<Object> tokenOptional = redis.sync().get(uidToken);
            if (tokenOptional.isPresent()) {
                authToken = (String) tokenOptional.get();
            }
        }
        if (uri.contains("/v1/")) {
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
                return Optional.empty();
            long timeStampLong = Long.parseLong(timeStamp);
            long currentTime = System.currentTimeMillis() / 1000;
            if (currentTime > timeStampLong + 30) return Optional.empty();
//            if (nonce.length() != 18) return Optional.empty();
            String key = API_NONE_KEY + nonce;
            Optional<Object> nonceKeyOptional = redis.sync().get(key);
            if (nonceKeyOptional.isPresent()) return Optional.empty();
            String salt = authToken + EncodeUtils.API_SALT + timeStamp + nonce;
            String md5FirstTime = encodeUtils.getMd5(salt);
            String md5SecondTime = encodeUtils.getMd5(authToken + md5FirstTime);
            if (!md5.equals(md5SecondTime)) return Optional.empty();
            redis.set(key, key, 24 * 3600);
        }
        return Optional.of("YES");
    }


    @Override
    public Result onUnauthorized(Http.Request req) {
        ObjectNode node = Json.newObject();
        node.put("code", -1);
        node.put("reason", businessUtils.getMessageValue(req,"base.apiCheck.error"));
        return ok(node);
    }

}
