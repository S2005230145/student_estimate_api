package security;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.admin.ShopAdmin;
import models.user.Member;
import play.Logger;
import play.cache.AsyncCacheApi;
import play.cache.NamedCache;
import play.cache.SyncCacheApi;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import utils.*;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Secured extends Security.Authenticator {

    Logger.ALogger logger = Logger.of(Secured.class);
    @Inject
    BizUtils businessUtils;
    @Inject
    @NamedCache("redis")
    protected play.cache.redis.AsyncCacheApi redis;

    /**
     * 认证接口，目前以token作为凭据，如果有在缓存中说明是合法用户，如果不在缓存中说明是非法用户
     *
     * @return
     */
    @Override
    public Optional<String> getUsername(Http.Request request) {
        String authToken = businessUtils.getUIDFromRequest(request);
        if (ValidationUtil.isEmpty(authToken)) return Optional.empty();
        try {
            Optional<ShopAdmin> optional = redis.sync().get(authToken);
            if (optional.isPresent()) {
                ShopAdmin member = optional.get();
                if (null != member) return Optional.of(member.id + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public Result onUnauthorized(Http.Request req) {
        ObjectNode node = Json.newObject();
        node.put("code", 403);
        node.put("reason", businessUtils.getMessageValue(req,"base.please.login"));
        return ok(node);
    }

}
