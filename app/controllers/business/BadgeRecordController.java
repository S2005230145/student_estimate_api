package controllers.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import controllers.BaseSecurityController;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.business.BadgeRecord;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import utils.ValidationUtil;

import java.util.List;
import java.util.concurrent.CompletionStage;

public class BadgeRecordController extends BaseSecurityController {

    /**
     * @api {GET} /v2/p/badge_record_list/   01列表-徽章授予记录
     * @apiName listBadgeRecord
     * @apiGroup 徽章授予记录模块
     * @apiParam {int} page 页码
     * @apiParam {String} filter 搜索栏()
     * @apiSuccess (Success 200) {long} orgId 机构ID
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {long} studentId 学生ID
     * @apiSuccess (Success 200) {String} badgeType 徽章类型
     * @apiSuccess (Success 200) {String} awardReason 授予原因
     * @apiSuccess (Success 200) {long} awardTime 授予时间
     * @apiSuccess (Success 200) {String} awardPeriod 授予周期
     * @apiSuccess (Success 200) {long} createTime 创建时间
     */
    public CompletionStage<Result> listBadgeRecord(Http.Request request, int page, String filter, int status) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ExpressionList<BadgeRecord> expressionList = BadgeRecord.find.query().where().eq("org_id", adminMember.getOrgId());
            if (status > 0) expressionList.eq("status", status);
            if (!ValidationUtil.isEmpty(filter)) expressionList
                    .or()
                    .icontains("filter", filter)
                    .endOr();               //编写其他条件
            //编写其他条件
            //编写其他条件
            //编写其他条件

            ObjectNode result = Json.newObject();
            List<BadgeRecord> list;
            if (page == 0) list = expressionList.findList();
            else {
                PagedList<BadgeRecord> pagedList = expressionList
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
     * @api {GET} /v2/p/badge_record/:id/  02详情-BadgeRecord徽章授予记录
     * @apiName getBadgeRecord
     * @apiGroup 徽章授予记录模块
     * @apiParam {long} id id
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {long} orgId 机构ID
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {long} studentId 学生ID
     * @apiSuccess (Success 200) {String} badgeType 徽章类型
     * @apiSuccess (Success 200) {String} awardReason 授予原因
     * @apiSuccess (Success 200) {long} awardTime 授予时间
     * @apiSuccess (Success 200) {String} awardPeriod 授予周期
     * @apiSuccess (Success 200) {long} createTime 创建时间
     */
    public CompletionStage<Result> getBadgeRecord(Http.Request request, long id) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            BadgeRecord badgeRecord = BadgeRecord.find.byId(id);
            if (null == badgeRecord) return okCustomJson(CODE40001, "数据不存在");
            //sass数据校验
            if (badgeRecord.orgId != adminMember.getOrgId()) return okCustomJson(CODE40001, "数据不存在");
            ObjectNode result = (ObjectNode) Json.toJson(badgeRecord);
            result.put(CODE, CODE200);
            return ok(result);
        });

    }

    /**
     * @api {POST} /v2/p/badge_record/new/   03添加-BadgeRecord徽章授予记录
     * @apiName addBadgeRecord
     * @apiDescription 描述
     * @apiGroup 徽章授予记录模块
     * @apiParam {long} orgId 机构ID
     * @apiParam {long} id 唯一标识
     * @apiParam {long} studentId 学生ID
     * @apiParam {String} badgeType 徽章类型
     * @apiParam {String} awardReason 授予原因
     * @apiParam {long} awardTime 授予时间
     * @apiParam {String} awardPeriod 授予周期
     * @apiParam {long} createTime 创建时间
     * @apiSuccess (Success 200){int} code 200
     */

    public CompletionStage<Result> addBadgeRecord(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((admin) -> {
            if (null == admin) return unauth403();
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");
            BadgeRecord badgeRecord = Json.fromJson(jsonNode, BadgeRecord.class);
// 数据sass化
            badgeRecord.setOrgId(admin.getOrgId());
            long currentTimeBySecond = dateUtils.getCurrentTimeByMilliSecond();
            badgeRecord.setCreateTime(currentTimeBySecond);
            badgeRecord.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/badge_record/:id/  04更新-BadgeRecord徽章授予记录
     * @apiName updateBadgeRecord
     * @apiGroup 徽章授予记录模块
     * @apiParam {long} orgId 机构ID
     * @apiParam {long} id 唯一标识
     * @apiParam {long} studentId 学生ID
     * @apiParam {String} badgeType 徽章类型
     * @apiParam {String} awardReason 授予原因
     * @apiParam {long} awardTime 授予时间
     * @apiParam {String} awardPeriod 授予周期
     * @apiParam {long} createTime 创建时间
     * @apiSuccess (Success 200){int} code 200
     */
    public CompletionStage<Result> updateBadgeRecord(Http.Request request, long id) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            BadgeRecord originalBadgeRecord = BadgeRecord.find.byId(id);
            BadgeRecord newBadgeRecord = Json.fromJson(jsonNode, BadgeRecord.class);
            if (null == originalBadgeRecord) return okCustomJson(CODE40001, "数据不存在");
            //sass数据校验  
            if (originalBadgeRecord.orgId != adminMember.getOrgId()) return okCustomJson(CODE40001, "数据不存在");
            if (newBadgeRecord.studentId > 0) originalBadgeRecord.setStudentId(newBadgeRecord.studentId);
            if (!ValidationUtil.isEmpty(newBadgeRecord.badgeType))
                originalBadgeRecord.setBadgeType(newBadgeRecord.badgeType);
            if (!ValidationUtil.isEmpty(newBadgeRecord.awardReason))
                originalBadgeRecord.setAwardReason(newBadgeRecord.awardReason);
            if (newBadgeRecord.awardTime > 0) originalBadgeRecord.setAwardTime(newBadgeRecord.awardTime);
            if (!ValidationUtil.isEmpty(newBadgeRecord.awardPeriod))
                originalBadgeRecord.setAwardPeriod(newBadgeRecord.awardPeriod);

            originalBadgeRecord.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/badge_record/   05删除-徽章授予记录
     * @apiName deleteBadgeRecord
     * @apiGroup 徽章授予记录模块
     * @apiParam {long} id id
     * @apiParam {String} operation del时删除
     * @apiSuccess (Success 200){int} 200 成功
     */
    public CompletionStage<Result> deleteBadgeRecord(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            long id = jsonNode.findPath("id").asLong();
            String operation = jsonNode.findPath("operation").asText();
            if (!"del".equals(operation)) return okCustomJson(CODE40001, "操作错误");
            BadgeRecord deleteModel = BadgeRecord.find.byId(id);
            if (null == deleteModel) return okCustomJson(CODE40001, "数据不存在");
            //sass数据校验  
            if (deleteModel.orgId != adminMember.getOrgId()) return okCustomJson(CODE40001, "数据不存在");
            deleteModel.delete();
            return okJSON200();
        });
    }
}
