package controllers.business;

import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import controllers.BaseSecurityController;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.business.ClassConfig;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import utils.BizUtils;
import utils.CacheUtils;
import utils.DateUtils;
import utils.ValidationUtil;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class ClassConfigController extends BaseSecurityController {

    @Inject
    protected DateUtils dateUtils;
    @Inject
    protected BizUtils businessUtils;
    @Inject
    protected CacheUtils cacheUtils;


    /**
     * @api {GET} /v2/p/class_config/   01列表-班级配置信息
     * @apiName listSchoolClass
     * @apiGroup CLASS-CONFIG
     * @apiParam {int} page 页码
     * @apiParam {String} filter 搜索栏()
     * @apiSusccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {String} className 班级名称
     * @apiSuccess (Success 200) {long} orgId 机构ID
     */

    public CompletionStage<Result> listClassConfig(Http.Request request, int page, String filter, int status) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync(adminMember -> {
            if (null == adminMember) return unauth403();
            ExpressionList<ClassConfig> expressionList = ClassConfig.find.query().where().eq("org_id", adminMember.getOrgId());
            if (status > 0) expressionList.eq("status", status);
            if (!ValidationUtil.isEmpty(filter)) expressionList
                    .or()
                    .icontains("filter", filter)
                    .endOr();               //编写其他条件
            //编写其他条件
            //编写其他条件
            //编写其他条件
            ObjectNode result = Json.newObject();
            List<ClassConfig> list;
            if (page == 0) list = expressionList.findList();
            else {
                PagedList<ClassConfig> pagedList = expressionList
                        .order().desc("id")
                        .setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_20)
                        .setMaxRows(BusinessConstant.PAGE_SIZE_20)
                        .findPagedList();
                list = pagedList.getList();
                result.put("pages", pagedList.getTotalPageCount());
                result.put("hasNest", pagedList.hasNext());
            }
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(list));
            return ok(result);
        });
    }

}
