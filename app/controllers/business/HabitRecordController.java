package controllers.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import controllers.BaseSecurityController;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.business.HabitRecord;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import utils.ValidationUtil;

import java.util.List;
import java.util.concurrent.CompletionStage;

public class HabitRecordController extends BaseSecurityController {

    /**
     * @api {GET} /v2/p/habit_record_list/   01列表-习惯评价记录
     * @apiName listHabitRecord
     * @apiGroup HABIT-RECORD-MANAGER
     * @apiParam {int} page 页码
     * @apiParam {String} filter 搜索栏()
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {long} studentId 学生ID
     * @apiSuccess (Success 200) {int} habitType 习惯类型
     * @apiSuccess (Success 200) {String} evaluatorType 评价者类型
     * @apiSuccess (Success 200) {long} evaluatorId 评价者ID
     * @apiSuccess (Success 200) {double} scoreChange 分数变化
     * @apiSuccess (Success 200) {String} description 行为描述
     * @apiSuccess (Success 200) {String} evidenceImage 证据图片
     * @apiSuccess (Success 200) {long} recordTime 记录时间
     * @apiSuccess (Success 200) {long} createTime 创建时间
     */
    public CompletionStage<Result> listHabitRecord(Http.Request request, int page, String filter, int status) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ExpressionList<HabitRecord> expressionList = HabitRecord.find.query().where();
            if (status > 0) expressionList.eq("status", status);
            if (!ValidationUtil.isEmpty(filter)) expressionList
                    .or()
                    .icontains("filter", filter)
                    .endOr();               //编写其他条件  
            //编写其他条件
            //编写其他条件
            //编写其他条件

            ObjectNode result = Json.newObject();
            List<HabitRecord> list;
            if (page == 0) list = expressionList.findList();
            else {
                PagedList<HabitRecord> pagedList = expressionList
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
     * @api {GET} /v2/p/habit_record/:id/  02详情-HabitRecord习惯评价记录
     * @apiName getHabitRecord
     * @apiGroup HABIT-RECORD-MANAGER
     * @apiParam {long} id id
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {long} studentId 学生ID
     * @apiSuccess (Success 200) {int} habitType 习惯类型
     * @apiSuccess (Success 200) {String} evaluatorType 评价者类型
     * @apiSuccess (Success 200) {long} evaluatorId 评价者ID
     * @apiSuccess (Success 200) {double} scoreChange 分数变化
     * @apiSuccess (Success 200) {String} description 行为描述
     * @apiSuccess (Success 200) {String} evidenceImage 证据图片
     * @apiSuccess (Success 200) {long} recordTime 记录时间
     * @apiSuccess (Success 200) {long} createTime 创建时间
     */
    public CompletionStage<Result> getHabitRecord(Http.Request request, long id) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            HabitRecord habitRecord = HabitRecord.find.byId(id);
            if (null == habitRecord) return okCustomJson(CODE40001, "数据不存在");
            ObjectNode result = (ObjectNode) Json.toJson(habitRecord);
            result.put(CODE, CODE200);
            return ok(result);
        });

    }

    /**
     * @api {POST} /v2/p/habit_record/new/   01添加-HabitRecord习惯评价记录
     * @apiName addHabitRecord
     * @apiDescription 描述
     * @apiGroup HABIT-RECORD-MANAGER
     * @apiParam {long} id 唯一标识
     * @apiParam {long} studentId 学生ID
     * @apiParam {int} habitType 习惯类型
     * @apiParam {String} evaluatorType 评价者类型
     * @apiParam {long} evaluatorId 评价者ID
     * @apiParam {double} scoreChange 分数变化
     * @apiParam {String} description 行为描述
     * @apiParam {String} evidenceImage 证据图片
     * @apiParam {long} recordTime 记录时间
     * @apiParam {long} createTime 创建时间
     * @apiSuccess (Success 200){int} code 200
     */

    public CompletionStage<Result> addHabitRecord(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((admin) -> {
            if (null == admin) return unauth403();
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");
            HabitRecord habitRecord = Json.fromJson(jsonNode, HabitRecord.class);

            long currentTimeBySecond = dateUtils.getCurrentTimeByMilliSecond();
            habitRecord.setCreateTime(currentTimeBySecond);
            habitRecord.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/habit_record/:id/  04更新-HabitRecord习惯评价记录
     * @apiName updateHabitRecord
     * @apiGroup HABIT-RECORD-MANAGER
     * @apiParam {long} id 唯一标识
     * @apiParam {long} studentId 学生ID
     * @apiParam {int} habitType 习惯类型
     * @apiParam {String} evaluatorType 评价者类型
     * @apiParam {long} evaluatorId 评价者ID
     * @apiParam {double} scoreChange 分数变化
     * @apiParam {String} description 行为描述
     * @apiParam {String} evidenceImage 证据图片
     * @apiParam {long} recordTime 记录时间
     * @apiParam {long} createTime 创建时间
     * @apiSuccess (Success 200){int} code 200
     */
    public CompletionStage<Result> updateHabitRecord(Http.Request request, long id) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            HabitRecord originalHabitRecord = HabitRecord.find.byId(id);
            HabitRecord newHabitRecord = Json.fromJson(jsonNode, HabitRecord.class);
            if (null == originalHabitRecord) return okCustomJson(CODE40001, "数据不存在");
            if (newHabitRecord.studentId > 0) originalHabitRecord.setStudentId(newHabitRecord.studentId);
            if (newHabitRecord.habitType > 0) originalHabitRecord.setHabitType(newHabitRecord.habitType);
            if (!ValidationUtil.isEmpty(newHabitRecord.evaluatorType))
                originalHabitRecord.setEvaluatorType(newHabitRecord.evaluatorType);
            if (newHabitRecord.evaluatorId > 0) originalHabitRecord.setEvaluatorId(newHabitRecord.evaluatorId);
            if (newHabitRecord.scoreChange > 0) originalHabitRecord.setScoreChange(newHabitRecord.scoreChange);
            if (!ValidationUtil.isEmpty(newHabitRecord.description))
                originalHabitRecord.setDescription(newHabitRecord.description);
            if (!ValidationUtil.isEmpty(newHabitRecord.evidenceImage))
                originalHabitRecord.setEvidenceImage(newHabitRecord.evidenceImage);
            if (newHabitRecord.recordTime > 0) originalHabitRecord.setRecordTime(newHabitRecord.recordTime);

            originalHabitRecord.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/habit_record/   05删除-习惯评价记录
     * @apiName deleteHabitRecord
     * @apiGroup HABIT-RECORD-MANAGER
     * @apiParam {long} id id
     * @apiParam {String} operation del时删除
     * @apiSuccess (Success 200){int} 200 成功
     */
    public CompletionStage<Result> deleteHabitRecord(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            long id = jsonNode.findPath("id").asLong();
            String operation = jsonNode.findPath("operation").asText();
            if (!"del".equals(operation)) return okCustomJson(CODE40001, "操作错误");
            HabitRecord deleteModel = HabitRecord.find.byId(id);
            if (null == deleteModel) return okCustomJson(CODE40001, "数据不存在");
            deleteModel.delete();
            return okJSON200();
        });
    }
}
