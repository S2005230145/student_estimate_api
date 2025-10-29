package controllers.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import controllers.BaseSecurityController;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.business.SpecialtyAward;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import utils.ValidationUtil;

import java.util.List;
import java.util.concurrent.CompletionStage;

public class SpecialtyAwardController extends BaseSecurityController {

    /**
     * @api {GET} /v2/p/specialty_award_list/   01列表-特长获奖记录
     * @apiName listSpecialtyAward
     * @apiGroup SPECIALTY-AWARD-MANAGER
     * @apiParam {int} page 页码
     * @apiParam {String} filter 搜索栏()
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {long} studentId 学生ID
     * @apiSuccess (Success 200) {int} awardLevel 奖项级别
     * @apiSuccess (Success 200) {int} awardGrade 奖项等级
     * @apiSuccess (Success 200) {String} competitionName 竞赛名称
     * @apiSuccess (Success 200) {String} category 比赛类别
     * @apiSuccess (Success 200) {double} awardScore 奖项得分
     * @apiSuccess (Success 200) {int} status 审核状态
     * @apiSuccess (Success 200) {String} certificateImage 证书图片
     * @apiSuccess (Success 200) {String} badgeAwarded 授予徽章
     * @apiSuccess (Success 200) {long} createTime 创建时间
     */
    public CompletionStage<Result> listSpecialtyAward(Http.Request request, int page, String filter, int status) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ExpressionList<SpecialtyAward> expressionList = SpecialtyAward.find.query().where();
            if (status > 0) expressionList.eq("status", status);
            if (!ValidationUtil.isEmpty(filter)) expressionList
                    .or()
                    .icontains("filter", filter)
                    .endOr();               //编写其他条件  
            //编写其他条件
            //编写其他条件
            //编写其他条件

            ObjectNode result = Json.newObject();
            List<SpecialtyAward> list;
            if (page == 0) list = expressionList.findList();
            else {
                PagedList<SpecialtyAward> pagedList = expressionList
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
     * @api {GET} /v2/p/specialty_award/:id/  02详情-SpecialtyAward特长获奖记录
     * @apiName getSpecialtyAward
     * @apiGroup SPECIALTY-AWARD-MANAGER
     * @apiParam {long} id id
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {long} studentId 学生ID
     * @apiSuccess (Success 200) {int} awardLevel 奖项级别
     * @apiSuccess (Success 200) {int} awardGrade 奖项等级
     * @apiSuccess (Success 200) {String} competitionName 竞赛名称
     * @apiSuccess (Success 200) {String} category 比赛类别
     * @apiSuccess (Success 200) {double} awardScore 奖项得分
     * @apiSuccess (Success 200) {int} status 审核状态
     * @apiSuccess (Success 200) {String} certificateImage 证书图片
     * @apiSuccess (Success 200) {String} badgeAwarded 授予徽章
     * @apiSuccess (Success 200) {long} createTime 创建时间
     */
    public CompletionStage<Result> getSpecialtyAward(Http.Request request, long id) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            SpecialtyAward specialtyAward = SpecialtyAward.find.byId(id);
            if (null == specialtyAward) return okCustomJson(CODE40001, "数据不存在");
            ObjectNode result = (ObjectNode) Json.toJson(specialtyAward);
            result.put(CODE, CODE200);
            return ok(result);
        });

    }

    /**
     * @api {POST} /v2/p/specialty_award/new/   01添加-SpecialtyAward特长获奖记录
     * @apiName addSpecialtyAward
     * @apiDescription 描述
     * @apiGroup SPECIALTY-AWARD-MANAGER
     * @apiParam {long} id 唯一标识
     * @apiParam {long} studentId 学生ID
     * @apiParam {int} awardLevel 奖项级别
     * @apiParam {int} awardGrade 奖项等级
     * @apiParam {String} competitionName 竞赛名称
     * @apiParam {String} category 比赛类别
     * @apiParam {double} awardScore 奖项得分
     * @apiParam {int} status 审核状态
     * @apiParam {String} certificateImage 证书图片
     * @apiParam {String} badgeAwarded 授予徽章
     * @apiParam {long} createTime 创建时间
     * @apiSuccess (Success 200){int} code 200
     */

    public CompletionStage<Result> addSpecialtyAward(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((admin) -> {
            if (null == admin) return unauth403();
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");
            SpecialtyAward specialtyAward = Json.fromJson(jsonNode, SpecialtyAward.class);

            long currentTimeBySecond = dateUtils.getCurrentTimeByMilliSecond();
            specialtyAward.setCreateTime(currentTimeBySecond);
            specialtyAward.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/specialty_award/:id/  04更新-SpecialtyAward特长获奖记录
     * @apiName updateSpecialtyAward
     * @apiGroup SPECIALTY-AWARD-MANAGER
     * @apiParam {long} id 唯一标识
     * @apiParam {long} studentId 学生ID
     * @apiParam {int} awardLevel 奖项级别
     * @apiParam {int} awardGrade 奖项等级
     * @apiParam {String} competitionName 竞赛名称
     * @apiParam {String} category 比赛类别
     * @apiParam {double} awardScore 奖项得分
     * @apiParam {int} status 审核状态
     * @apiParam {String} certificateImage 证书图片
     * @apiParam {String} badgeAwarded 授予徽章
     * @apiParam {long} createTime 创建时间
     * @apiSuccess (Success 200){int} code 200
     */
    public CompletionStage<Result> updateSpecialtyAward(Http.Request request, long id) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            SpecialtyAward originalSpecialtyAward = SpecialtyAward.find.byId(id);
            SpecialtyAward newSpecialtyAward = Json.fromJson(jsonNode, SpecialtyAward.class);
            if (null == originalSpecialtyAward) return okCustomJson(CODE40001, "数据不存在");
            if (newSpecialtyAward.studentId > 0) originalSpecialtyAward.setStudentId(newSpecialtyAward.studentId);
            if (newSpecialtyAward.awardLevel > 0) originalSpecialtyAward.setAwardLevel(newSpecialtyAward.awardLevel);
            if (newSpecialtyAward.awardGrade > 0) originalSpecialtyAward.setAwardGrade(newSpecialtyAward.awardGrade);
            if (!ValidationUtil.isEmpty(newSpecialtyAward.competitionName))
                originalSpecialtyAward.setCompetitionName(newSpecialtyAward.competitionName);
            if (!ValidationUtil.isEmpty(newSpecialtyAward.category))
                originalSpecialtyAward.setCategory(newSpecialtyAward.category);
            if (newSpecialtyAward.awardScore > 0) originalSpecialtyAward.setAwardScore(newSpecialtyAward.awardScore);
            if (newSpecialtyAward.status > 0) originalSpecialtyAward.setStatus(newSpecialtyAward.status);
            if (!ValidationUtil.isEmpty(newSpecialtyAward.certificateImage))
                originalSpecialtyAward.setCertificateImage(newSpecialtyAward.certificateImage);
            if (!ValidationUtil.isEmpty(newSpecialtyAward.badgeAwarded))
                originalSpecialtyAward.setBadgeAwarded(newSpecialtyAward.badgeAwarded);

            originalSpecialtyAward.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/specialty_award/   05删除-特长获奖记录
     * @apiName deleteSpecialtyAward
     * @apiGroup SPECIALTY-AWARD-MANAGER
     * @apiParam {long} id id
     * @apiParam {String} operation del时删除
     * @apiSuccess (Success 200){int} 200 成功
     */
    public CompletionStage<Result> deleteSpecialtyAward(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            long id = jsonNode.findPath("id").asLong();
            String operation = jsonNode.findPath("operation").asText();
            if (!"del".equals(operation)) return okCustomJson(CODE40001, "操作错误");
            SpecialtyAward deleteModel = SpecialtyAward.find.byId(id);
            if (null == deleteModel) return okCustomJson(CODE40001, "数据不存在");
            deleteModel.delete();
            return okJSON200();
        });
    }
}
