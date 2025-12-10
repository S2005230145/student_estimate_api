package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.cache.AsyncCacheApi;
import play.cache.NamedCache;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import utils.BizUtils;
import utils.CacheUtils;
import utils.DateUtils;

import javax.inject.Inject;

/**
 * BaseController 所有的业务逻辑必须都继续自该controller以便方便管理
 *
 * @link BaseSecurityController
 */
//@With(CorsAction.class)
public class BaseController extends Controller {
    @Inject
    protected DateUtils dateUtils;
    @Inject
    protected BizUtils businessUtils;
    @Inject
    protected CacheUtils cacheUtils;

    @Inject
    @NamedCache("redis")
    protected play.cache.redis.AsyncCacheApi redis;

    @Inject
    protected MessagesApi messagesApi;


    public static final String CODE = "code";
    public static final int CODE200 = 200;
    public static final int CODE403 = 403;
    public static final int CODE404 = 404;
    public static final int CODE500 = 500;
    public static final int CODE40001 = 40001;
    public static final int CODE40002 = 40002;
    public static final int CODE40003 = 40003;
    public static final int CODE40004 = 40004;
    public static final int CODE40005 = 40005;
    public static final int CODE40006 = 40006;
    public static final int CODE40007 = 40007;
    public static final int CODE40008 = 40008;
    public static final int CODE40009 = 40009;
    public static final int CODE40010 = 40010;

    public static final String FILE_DIR_LOCATION="/home/ubuntu/web/student_file/";
//    public static final String FILE_DIR_LOCATION="D:/tmp/";
    public static final String DOWNLOAD_LOCATION = "/download/";

    /**
     * 未授权
     *
     * @return
     */
    public Result unauth403() {
        ObjectNode node = Json.newObject();
        node.put("code", 403);
        node.put("reason", "未授权");
        return ok(node);
    }

    public Result unauth403(Http.Request request) {
        Messages messages = messagesApi.preferred(request);
        String translation = messages.at("base.not.auth");
        ObjectNode node = Json.newObject();
        node.put("code", 403);
        node.put("reason", translation);
        return ok(node);
    }

    public Result unauth503() {
        ObjectNode node = Json.newObject();
        node.put("code", 503);
        node.put("reason", "请先完成认证");
        return ok(node);
    }

    public Result unauth503(Http.Request request) {
        Messages messages = messagesApi.preferred(request);
        String translation = messages.at("base.not.auth");
        ObjectNode node = Json.newObject();
        node.put("code", 503);
        node.put("reason", translation);
        return ok(node);
    }

    /**
     * 返回200
     *
     * @return
     */
    public Result okJSON200() {
        ObjectNode node = Json.newObject();
        node.put("code", 200);
        return ok(node);
    }

    public Result okCustomJson(int code, String reason) {
        ObjectNode node = Json.newObject();
        node.put("code", code);
        node.put("reason", reason);
        return ok(node);
    }


    public Result okCustomJson(Http.Request request, int code, String key) {
        Messages messages = messagesApi.preferred(request);
        String translation = messages.at(key);
        ObjectNode node = Json.newObject();
        node.put("code", code);
        node.put("reason", translation);
        return ok(node);
    }

    /**
     * 自定义返回json对象的节点
     *
     * @param code
     * @param reason
     * @return
     */
    public ObjectNode customObjectNode(int code, String reason) {
        ObjectNode node = Json.newObject();
        node.put("code", code);
        node.put("reason", reason);
        node.put("message", reason);
        return node;
    }

    public Result index() {
        return okJSON200();
    }
}
