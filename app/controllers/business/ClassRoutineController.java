package controllers.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import controllers.BaseSecurityController;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.business.ClassRoutine;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import utils.ValidationUtil;

import java.util.List;
import java.util.concurrent.CompletionStage;

public class ClassRoutineController extends BaseSecurityController {

    /**
     * @api {GET} /v2/p/class_routine_list/   01列表-班级常规评比
     * @apiName listClassRoutine
     * @apiGroup CLASS-ROUTINE-CONTROLLER
     * @apiParam {int} page 页码
     * @apiParam {String} filter 搜索栏()
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {long} classId 班级ID
     * @apiSuccess (Success 200) {int} weekNumber 周次
     * @apiSuccess (Success 200) {int} month 月份
     * @apiSuccess (Success 200) {double} hygieneScore 卫生得分
     * @apiSuccess (Success 200) {double} disciplineScore 纪律得分
     * @apiSuccess (Success 200) {double} exerciseScore 两操得分
     * @apiSuccess (Success 200) {double} mannerScore 文明礼仪得分
     * @apiSuccess (Success 200) {double} readingScore 晨诵午读得分
     * @apiSuccess (Success 200) {double} totalScore 周总分
     * @apiSuccess (Success 200) {long} recordTime 记录时间
     * @apiSuccess (Success 200) {long} createTime 创建时间
     */
    public CompletionStage<Result> listClassRoutine(Http.Request request, int page, String filter, int status) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ExpressionList<ClassRoutine> expressionList = ClassRoutine.find.query().where();
            if (status > 0) expressionList.eq("status", status);
            if (!ValidationUtil.isEmpty(filter)) expressionList
                    .or()
                    .icontains("filter", filter)
                    .endOr();               //编写其他条件  
            //编写其他条件
            //编写其他条件
            //编写其他条件

            ObjectNode result = Json.newObject();
            List<ClassRoutine> list;
            if (page == 0) list = expressionList.findList();
            else {
                PagedList<ClassRoutine> pagedList = expressionList
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
     * @api {GET} /v2/p/class_routine/:id/  02详情-ClassRoutine班级常规评比
     * @apiName getClassRoutine
     * @apiGroup CLASS-ROUTINE-CONTROLLER
     * @apiParam {long} id id
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {long} classId 班级ID
     * @apiSuccess (Success 200) {int} weekNumber 周次
     * @apiSuccess (Success 200) {int} month 月份
     * @apiSuccess (Success 200) {double} hygieneScore 卫生得分
     * @apiSuccess (Success 200) {double} disciplineScore 纪律得分
     * @apiSuccess (Success 200) {double} exerciseScore 两操得分
     * @apiSuccess (Success 200) {double} mannerScore 文明礼仪得分
     * @apiSuccess (Success 200) {double} readingScore 晨诵午读得分
     * @apiSuccess (Success 200) {double} totalScore 周总分
     * @apiSuccess (Success 200) {long} recordTime 记录时间
     * @apiSuccess (Success 200) {long} createTime 创建时间
     */
    public CompletionStage<Result> getClassRoutine(Http.Request request, long id) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ClassRoutine classRoutine = ClassRoutine.find.byId(id);
            if (null == classRoutine) return okCustomJson(CODE40001, "数据不存在");
            ObjectNode result = (ObjectNode) Json.toJson(classRoutine);
            result.put(CODE, CODE200);
            return ok(result);
        });

    }

    /**
     * @api {POST} /v2/p/class_routine/new/   01添加-ClassRoutine班级常规评比
     * @apiName addClassRoutine
     * @apiDescription 描述
     * @apiGroup CLASS-ROUTINE-CONTROLLER
     * @apiParam {long} id 唯一标识
     * @apiParam {long} classId 班级ID
     * @apiParam {int} weekNumber 周次
     * @apiParam {int} month 月份
     * @apiParam {double} hygieneScore 卫生得分
     * @apiParam {double} disciplineScore 纪律得分
     * @apiParam {double} exerciseScore 两操得分
     * @apiParam {double} mannerScore 文明礼仪得分
     * @apiParam {double} readingScore 晨诵午读得分
     * @apiParam {double} totalScore 周总分
     * @apiParam {long} recordTime 记录时间
     * @apiParam {long} createTime 创建时间
     * @apiSuccess (Success 200){int} code 200
     */

    public CompletionStage<Result> addClassRoutine(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((admin) -> {
            if (null == admin) return unauth403();
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");
            ClassRoutine classRoutine = Json.fromJson(jsonNode, ClassRoutine.class);
            long currentTimeBySecond = dateUtils.getCurrentTimeByMilliSecond();
            classRoutine.setCreateTime(currentTimeBySecond);
            double totalScore = classRoutine.calcTotalScore();
            classRoutine.setTotalScore(totalScore);
            classRoutine.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/class_routine/:id/  04更新-ClassRoutine班级常规评比
     * @apiName updateClassRoutine
     * @apiGroup CLASS-ROUTINE-CONTROLLER
     * @apiParam {long} id 唯一标识
     * @apiParam {long} classId 班级ID
     * @apiParam {int} weekNumber 周次
     * @apiParam {int} month 月份
     * @apiParam {double} hygieneScore 卫生得分
     * @apiParam {double} disciplineScore 纪律得分
     * @apiParam {double} exerciseScore 两操得分
     * @apiParam {double} mannerScore 文明礼仪得分
     * @apiParam {double} readingScore 晨诵午读得分
     * @apiParam {double} totalScore 周总分
     * @apiParam {long} recordTime 记录时间
     * @apiParam {long} createTime 创建时间
     * @apiSuccess (Success 200){int} code 200
     */
    public CompletionStage<Result> updateClassRoutine(Http.Request request, long id) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ClassRoutine originalClassRoutine = ClassRoutine.find.byId(id);
            ClassRoutine newClassRoutine = Json.fromJson(jsonNode, ClassRoutine.class);
            if (null == originalClassRoutine) return okCustomJson(CODE40001, "数据不存在");
            if (newClassRoutine.classId > 0) originalClassRoutine.setClassId(newClassRoutine.classId);
            if (newClassRoutine.weekNumber > 0) originalClassRoutine.setWeekNumber(newClassRoutine.weekNumber);
            if (newClassRoutine.month > 0) originalClassRoutine.setMonth(newClassRoutine.month);
            if (newClassRoutine.hygieneScore > 0) originalClassRoutine.setHygieneScore(newClassRoutine.hygieneScore);
            if (newClassRoutine.disciplineScore > 0)
                originalClassRoutine.setDisciplineScore(newClassRoutine.disciplineScore);
            if (newClassRoutine.exerciseScore > 0) originalClassRoutine.setExerciseScore(newClassRoutine.exerciseScore);
            if (newClassRoutine.mannerScore > 0) originalClassRoutine.setMannerScore(newClassRoutine.mannerScore);
            if (newClassRoutine.readingScore > 0) originalClassRoutine.setReadingScore(newClassRoutine.readingScore);
//            if (newClassRoutine.totalScore > 0) originalClassRoutine.setTotalScore(newClassRoutine.totalScore);
            if (newClassRoutine.recordTime > 0) originalClassRoutine.setRecordTime(newClassRoutine.recordTime);

            double totalScore = originalClassRoutine.calcTotalScore();
            originalClassRoutine.setTotalScore(totalScore);
            originalClassRoutine.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/class_routine/   05删除-班级常规评比
     * @apiName deleteClassRoutine
     * @apiGroup CLASS-ROUTINE-CONTROLLER
     * @apiParam {long} id id
     * @apiParam {String} operation del时删除
     * @apiSuccess (Success 200){int} 200 成功
     */
    public CompletionStage<Result> deleteClassRoutine(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            long id = jsonNode.findPath("id").asLong();
            String operation = jsonNode.findPath("operation").asText();
            if (!"del".equals(operation)) return okCustomJson(CODE40001, "操作错误");
            ClassRoutine deleteModel = ClassRoutine.find.byId(id);
            if (null == deleteModel) return okCustomJson(CODE40001, "数据不存在");
            deleteModel.delete();
            return okJSON200();
        });
    }
}
