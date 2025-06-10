package controllers.basic;

import controllers.BaseController;
import play.mvc.Http;
import play.mvc.Result;
import utils.ValidationUtil;

import java.util.Optional;

import static constants.RedisKeyConstant.ADMIN_KEY_MEMBER_ID_AUTH_TOKEN_PREFIX;

/**
 * 管理员注销控制器
 */
public class LogoutController extends BaseController {
    /**
     * @api {POST} /v2/s/logout/ 03注销
     * @apiName logout
     * @apiGroup Admin-Authority
     * @apiSuccess (Success 200){int} code 200 注销成功.
     * @apiSuccess (Error 40003) {int} code 40003 未提供token
     */
    public Result logout(Http.Request request) {
        String idToken = businessUtils.getUIDFromRequest(request);
        if (!ValidationUtil.isEmpty(idToken)) {
            redis.remove(idToken);
            return okJSON200();
        } else return okCustomJson(request,CODE40003, "member.error");
    }

}
