package controllers.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import controllers.BaseSecurityController;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.business.Badge;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import utils.ValidationUtil;

import java.util.List;
import java.util.concurrent.CompletionStage;

public class BadgeController extends BaseSecurityController {

    /**
     * @api {GET} /v2/p/badge_list/   01列表-徽章配置
     * @apiName listBadge
     * @apiGroup 徽章配置模块
     * @apiParam {int} page 页码
     * @apiParam {String} filter 搜索栏
     * @apiParam {int} active 状态筛选
     * @apiSuccess (Success 200) {long} orgId 机构ID
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {int} badgeId 所属徽章类型
     * @apiSuccess (Success 200) {String} badgeName 徽章名称
     * @apiSuccess (Success 200) {String} description 描述
     * @apiSuccess (Success 200) {boolean} active 是否启用
     * @apiSuccess (Success 200) {long} createTime 创建时间
     */
    public CompletionStage<Result> listBadge(Http.Request request, int page, String filter, int active) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ExpressionList<Badge> expressionList = Badge.find.query().where().eq("org_id", adminMember.getOrgId());
            if (active > 0) expressionList.eq("active", active);
            if (!ValidationUtil.isEmpty(filter)) {
                expressionList.or()
                        .icontains("badge_name", filter)
                        .icontains("description", filter)
                        .endOr();
            }

            ObjectNode result = Json.newObject();
            List<Badge> list;
            if (page == 0) list = expressionList.findList();
            else {
                PagedList<Badge> pagedList = expressionList
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

    /**
     * @api {GET} /v2/p/badge/:id/  02详情-徽章配置
     * @apiName getBadge
     * @apiGroup 徽章配置模块
     * @apiParam {long} id id
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {long} orgId 机构ID
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {int} badgeId 所属徽章类型
     * @apiSuccess (Success 200) {String} badgeName 徽章名称
     * @apiSuccess (Success 200) {String} description 描述
     * @apiSuccess (Success 200) {boolean} active 是否启用
     * @apiSuccess (Success 200) {long} createTime 创建时间
     */
    public CompletionStage<Result> getBadge(Http.Request request, long id) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            Badge badge = Badge.find.byId(id);
            if (null == badge) return okCustomJson(CODE40001, "数据不存在");
            //sass数据校验  
            if (badge.orgId != adminMember.getOrgId()) return okCustomJson(CODE40001, "数据不存在");
            ObjectNode result = (ObjectNode) Json.toJson(badge);
            result.put(CODE, CODE200);
            return ok(result);
        });

    }

    /**
     * @api {POST} /v2/p/badge/new/   01添加-徽章配置
     * @apiName addBadge
     * @apiDescription 添加徽章配置
     * @apiGroup 徽章配置模块
     * @apiParam {long} orgId 机构ID
     * @apiParam {int} badgeId 所属徽章类型
     * @apiParam {String} badgeName 徽章名称
     * @apiParam {String} description 描述
     * @apiParam {boolean} active 是否启用
     * @apiSuccess (Success 200){int} code 200
     */
    public CompletionStage<Result> addBadge(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((admin) -> {
            if (null == admin) return unauth403();
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");
            Badge badge = Json.fromJson(jsonNode, Badge.class);
            // 数据sass化
            badge.setOrgId(admin.getOrgId());
            long currentTimeBySecond = dateUtils.getCurrentTimeByMilliSecond();
            badge.setCreateTime(currentTimeBySecond);
            badge.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/badge/:id/  04更新-徽章配置
     * @apiName updateBadge
     * @apiGroup 徽章配置模块
     * @apiParam {long} id 唯一标识
     * @apiParam {int} badgeId 所属徽章类型
     * @apiParam {String} badgeName 徽章名称
     * @apiParam {String} description 描述
     * @apiParam {boolean} active 是否启用
     * @apiSuccess (Success 200){int} code 200
     */
    public CompletionStage<Result> updateBadge(Http.Request request, long id) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            Badge originalBadge = Badge.find.byId(id);
            Badge newBadge = Json.fromJson(jsonNode, Badge.class);
            if (null == originalBadge) return okCustomJson(CODE40001, "数据不存在");
            //sass数据校验  
            if (originalBadge.orgId != adminMember.getOrgId()) return okCustomJson(CODE40001, "数据不存在");
            
            if (newBadge.badgeId > 0) originalBadge.setBadgeId(newBadge.badgeId);
            if (!ValidationUtil.isEmpty(newBadge.badgeName))
                originalBadge.setBadgeName(newBadge.badgeName);
            if(!ValidationUtil.isEmpty(newBadge.badgeImage)){
                originalBadge.setBadgeImage(newBadge.badgeImage);
            }
            if (!ValidationUtil.isEmpty(newBadge.description))
                originalBadge.setDescription(newBadge.description);
            if (newBadge.active != originalBadge.active)
                originalBadge.setActive(newBadge.active);

            originalBadge.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/badge/   05删除-徽章配置
     * @apiName deleteBadge
     * @apiGroup 徽章配置模块
     * @apiParam {long} id id
     * @apiParam {String} operation del时删除
     * @apiSuccess (Success 200){int} 200 成功
     */
    public CompletionStage<Result> deleteBadge(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            long id = jsonNode.findPath("id").asLong();
            String operation = jsonNode.findPath("operation").asText();
            if (!"del".equals(operation)) return okCustomJson(CODE40001, "操作错误");
            Badge deleteModel = Badge.find.byId(id);
            if (null == deleteModel) return okCustomJson(CODE40001, "数据不存在");
            //sass数据校验  
            if (deleteModel.orgId != adminMember.getOrgId()) return okCustomJson(CODE40001, "数据不存在");
            deleteModel.delete();
            return okJSON200();
        });
    }
}

