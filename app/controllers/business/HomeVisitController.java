package controllers.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import controllers.BaseSecurityController;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.business.HomeVisit;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import utils.ValidationUtil;

import java.util.List;
import java.util.concurrent.CompletionStage;

public class HomeVisitController extends BaseSecurityController {

    /**
     * @api {GET} /v2/p/home_visit_list/   01列表-家访工作记录
     * @apiName listHomeVisit
     * @apiGroup HOME-VISIT-MANAGER
     * @apiParam {int} page 页码
     * @apiParam {String} filter 搜索栏()
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {long} teacherId 教师ID
     * @apiSuccess (Success 200) {long} classId 班级ID
     * @apiSuccess (Success 200) {long} studentId 学生ID
     * @apiSuccess (Success 200) {int} visitType 家访类型
     * @apiSuccess (Success 200) {String} recordContent 记录内容
     * @apiSuccess (Success 200) {String} caseStudy 优秀案例
     * @apiSuccess (Success 200) {String} videoEvidence 视频证据
     * @apiSuccess (Success 200) {int} baseScore 基础分
     * @apiSuccess (Success 200) {int} bonusScore 加分
     * @apiSuccess (Success 200) {int} totalScore 总分
     * @apiSuccess (Success 200) {int} status 审核状态
     * @apiSuccess (Success 200) {long} visitTime 家访时间
     * @apiSuccess (Success 200) {long} createTime 创建时间
     */
    public CompletionStage<Result> listHomeVisit(Http.Request request, int page, String filter, int status) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ExpressionList<HomeVisit> expressionList = HomeVisit.find.query().where();
            if (status > 0) expressionList.eq("status", status);
            if (!ValidationUtil.isEmpty(filter)) expressionList
                    .or()
                    .icontains("filter", filter)
                    .endOr();               //编写其他条件  
            //编写其他条件
            //编写其他条件
            //编写其他条件

            ObjectNode result = Json.newObject();
            List<HomeVisit> list;
            if (page == 0) list = expressionList.findList();
            else {
                PagedList<HomeVisit> pagedList = expressionList
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
     * @api {GET} /v2/p/home_visit/:id/  02详情-HomeVisit家访工作记录
     * @apiName getHomeVisit
     * @apiGroup HOME-VISIT-MANAGER
     * @apiParam {long} id id
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {long} teacherId 教师ID
     * @apiSuccess (Success 200) {long} classId 班级ID
     * @apiSuccess (Success 200) {long} studentId 学生ID
     * @apiSuccess (Success 200) {int} visitType 家访类型
     * @apiSuccess (Success 200) {String} recordContent 记录内容
     * @apiSuccess (Success 200) {String} caseStudy 优秀案例
     * @apiSuccess (Success 200) {String} videoEvidence 视频证据
     * @apiSuccess (Success 200) {int} baseScore 基础分
     * @apiSuccess (Success 200) {int} bonusScore 加分
     * @apiSuccess (Success 200) {int} totalScore 总分
     * @apiSuccess (Success 200) {int} status 审核状态
     * @apiSuccess (Success 200) {long} visitTime 家访时间
     * @apiSuccess (Success 200) {long} createTime 创建时间
     */
    public CompletionStage<Result> getHomeVisit(Http.Request request, long id) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            HomeVisit homeVisit = HomeVisit.find.byId(id);
            if (null == homeVisit) return okCustomJson(CODE40001, "数据不存在");
            ObjectNode result = (ObjectNode) Json.toJson(homeVisit);
            result.put(CODE, CODE200);
            return ok(result);
        });

    }

    /**
     * @api {POST} /v2/p/home_visit/new/   01添加-HomeVisit家访工作记录
     * @apiName addHomeVisit
     * @apiDescription 描述
     * @apiGroup HOME-VISIT-MANAGER
     * @apiParam {long} id 唯一标识
     * @apiParam {long} teacherId 教师ID
     * @apiParam {long} classId 班级ID
     * @apiParam {long} studentId 学生ID
     * @apiParam {int} visitType 家访类型
     * @apiParam {String} recordContent 记录内容
     * @apiParam {String} caseStudy 优秀案例
     * @apiParam {String} videoEvidence 视频证据
     * @apiParam {int} baseScore 基础分
     * @apiParam {int} bonusScore 加分
     * @apiParam {int} totalScore 总分
     * @apiParam {int} status 审核状态
     * @apiParam {long} visitTime 家访时间
     * @apiParam {long} createTime 创建时间
     * @apiSuccess (Success 200){int} code 200
     */

    public CompletionStage<Result> addHomeVisit(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((admin) -> {
            if (null == admin) return unauth403();
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");
            HomeVisit homeVisit = Json.fromJson(jsonNode, HomeVisit.class);

            long currentTimeBySecond = dateUtils.getCurrentTimeByMilliSecond();
            homeVisit.setCreateTime(currentTimeBySecond);
            homeVisit.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/home_visit/:id/  04更新-HomeVisit家访工作记录
     * @apiName updateHomeVisit
     * @apiGroup HOME-VISIT-MANAGER
     * @apiParam {long} id 唯一标识
     * @apiParam {long} teacherId 教师ID
     * @apiParam {long} classId 班级ID
     * @apiParam {long} studentId 学生ID
     * @apiParam {int} visitType 家访类型
     * @apiParam {String} recordContent 记录内容
     * @apiParam {String} caseStudy 优秀案例
     * @apiParam {String} videoEvidence 视频证据
     * @apiParam {int} baseScore 基础分
     * @apiParam {int} bonusScore 加分
     * @apiParam {int} totalScore 总分
     * @apiParam {int} status 审核状态
     * @apiParam {long} visitTime 家访时间
     * @apiParam {long} createTime 创建时间
     * @apiSuccess (Success 200){int} code 200
     */
    public CompletionStage<Result> updateHomeVisit(Http.Request request, long id) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            HomeVisit originalHomeVisit = HomeVisit.find.byId(id);
            HomeVisit newHomeVisit = Json.fromJson(jsonNode, HomeVisit.class);
            if (null == originalHomeVisit) return okCustomJson(CODE40001, "数据不存在");
            if (newHomeVisit.teacherId > 0) originalHomeVisit.setTeacherId(newHomeVisit.teacherId);
            if (newHomeVisit.classId > 0) originalHomeVisit.setClassId(newHomeVisit.classId);
            if (newHomeVisit.studentId > 0) originalHomeVisit.setStudentId(newHomeVisit.studentId);
            if (newHomeVisit.visitType > 0) originalHomeVisit.setVisitType(newHomeVisit.visitType);
            if (!ValidationUtil.isEmpty(newHomeVisit.recordContent))
                originalHomeVisit.setRecordContent(newHomeVisit.recordContent);
            if (!ValidationUtil.isEmpty(newHomeVisit.caseStudy)) originalHomeVisit.setCaseStudy(newHomeVisit.caseStudy);
            if (!ValidationUtil.isEmpty(newHomeVisit.videoEvidence))
                originalHomeVisit.setVideoEvidence(newHomeVisit.videoEvidence);
            if (newHomeVisit.baseScore > 0) originalHomeVisit.setBaseScore(newHomeVisit.baseScore);
            if (newHomeVisit.bonusScore > 0) originalHomeVisit.setBonusScore(newHomeVisit.bonusScore);
            if (newHomeVisit.totalScore > 0) originalHomeVisit.setTotalScore(newHomeVisit.totalScore);
            if (newHomeVisit.status > 0) originalHomeVisit.setStatus(newHomeVisit.status);
            if (newHomeVisit.visitTime > 0) originalHomeVisit.setVisitTime(newHomeVisit.visitTime);

            originalHomeVisit.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/home_visit/   05删除-家访工作记录
     * @apiName deleteHomeVisit
     * @apiGroup HOME-VISIT-MANAGER
     * @apiParam {long} id id
     * @apiParam {String} operation del时删除
     * @apiSuccess (Success 200){int} 200 成功
     */
    public CompletionStage<Result> deleteHomeVisit(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            long id = jsonNode.findPath("id").asLong();
            String operation = jsonNode.findPath("operation").asText();
            if (!"del".equals(operation)) return okCustomJson(CODE40001, "操作错误");
            HomeVisit deleteModel = HomeVisit.find.byId(id);
            if (null == deleteModel) return okCustomJson(CODE40001, "数据不存在");
            deleteModel.delete();
            return okJSON200();
        });
    }
}
