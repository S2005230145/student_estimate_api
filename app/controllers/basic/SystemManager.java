package controllers.basic;

import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import controllers.BaseSecurityController;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.log.OperationLog;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import utils.ValidationUtil;

import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * 统计管理
 */
public class SystemManager extends BaseSecurityController {


    /**
     * @api {GET} /v2/s/operation_logs/?page=&key= 01操作日志
     * @apiName listOperationLog
     * @apiGroup ADMIN-System
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} pages 页数
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200){int} id id
     * @apiSuccess (Success 200){String} adminId 管理员ID
     * @apiSuccess (Success 200){String} adminName 管理员名字
     * @apiSuccess (Success 200){String} ip 操作时IP
     * @apiSuccess (Success 200){String} place ip地址
     * @apiSuccess (Success 200){String} note 操作说明
     * @apiSuccess (Success 200){String} createTime 操作时间
     */
    public CompletionStage<Result> listOperationLog(Http.Request request, int page, String adminName, long adminId) {
        if (page < 1) page = 1;
        final int queryPage = page - 1;
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((shopAdmin) -> {
            if (null == shopAdmin || shopAdmin.orgId < 1) return unauth503(request);
            ExpressionList<OperationLog> expressionList = OperationLog.find.query().where().eq("orgId", shopAdmin.orgId);
            if (!ValidationUtil.isEmpty(adminName)) expressionList.icontains("adminName", adminName);
            if (adminId > 0) expressionList.eq("adminId", adminId);
            PagedList<OperationLog> pagedList = expressionList.orderBy().desc("id")
                    .setFirstRow(queryPage * BusinessConstant.PAGE_SIZE_20)
                    .setMaxRows(BusinessConstant.PAGE_SIZE_20)
                    .findPagedList();
            List<OperationLog> list = pagedList.getList();
            int pages = pagedList.getTotalPageCount();
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.put("pages", pages);
            result.set("list", Json.toJson(list));
            return ok(result);
        });
    }
}
