package controllers.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import controllers.BaseSecurityController;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.business.ParentStudentRelation;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import utils.ValidationUtil;

import java.util.List;
import java.util.concurrent.CompletionStage;

public class ParentStudentRelationController extends BaseSecurityController {

    /**
     * @api {GET} /v2/p/parent_student_relation_list/   01列表-家长学生关系
     * @apiName listParentStudentRelation
     * @apiGroup PARENT-STUDENT-RELATION-CONTROLLER
     * @apiParam {int} page 页码
     * @apiParam {String} filter 搜索栏()
     * @apiSuccess (Success 200) {long} id
     * @apiSuccess (Success 200) {long} parentId
     * @apiSuccess (Success 200) {long} studentId
     * @apiSuccess (Success 200) {String} relationship
     * @apiSuccess (Success 200) {long} createTime
     * @apiSuccess (Success 200) {long} updateTime
     */
    public CompletionStage<Result> listParentStudentRelation(Http.Request request, int page, String filter, int status) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ExpressionList<ParentStudentRelation> expressionList = ParentStudentRelation.find.query().where();
            if (status > 0) expressionList.eq("status", status);
            if (!ValidationUtil.isEmpty(filter)) expressionList
                    .or()
                    .icontains("filter", filter)
                    .endOr();               //编写其他条件  
            //编写其他条件
            //编写其他条件
            //编写其他条件

            ObjectNode result = Json.newObject();
            List<ParentStudentRelation> list;
            if (page == 0) list = expressionList.findList();
            else {
                PagedList<ParentStudentRelation> pagedList = expressionList
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
     * @api {GET} /v2/p/parent_student_relation/:id/  02详情-ParentStudentRelation家长学生关系
     * @apiName getParentStudentRelation
     * @apiGroup PARENT-STUDENT-RELATION-CONTROLLER
     * @apiParam {long} id id
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {long} id
     * @apiSuccess (Success 200) {long} parentId
     * @apiSuccess (Success 200) {long} studentId
     * @apiSuccess (Success 200) {String} relationship
     * @apiSuccess (Success 200) {long} createTime
     * @apiSuccess (Success 200) {long} updateTime
     */
    public CompletionStage<Result> getParentStudentRelation(Http.Request request, long id) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ParentStudentRelation parentStudentRelation = ParentStudentRelation.find.byId(id);
            if (null == parentStudentRelation) return okCustomJson(CODE40001, "数据不存在");
            ObjectNode result = (ObjectNode) Json.toJson(parentStudentRelation);
            result.put(CODE, CODE200);
            return ok(result);
        });

    }

    /**
     * @api {POST} /v2/p/parent_student_relation/new/   01添加-ParentStudentRelation家长学生关系
     * @apiName addParentStudentRelation
     * @apiDescription 描述
     * @apiGroup PARENT-STUDENT-RELATION-CONTROLLER
     * @apiParam {long} id
     * @apiParam {long} parentId
     * @apiParam {long} studentId
     * @apiParam {String} relationship
     * @apiParam {long} createTime
     * @apiParam {long} updateTime
     * @apiSuccess (Success 200){int} code 200
     */

    public CompletionStage<Result> addParentStudentRelation(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((admin) -> {
            if (null == admin) return unauth403();
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");
            ParentStudentRelation parentStudentRelation = Json.fromJson(jsonNode, ParentStudentRelation.class);

            long currentTimeBySecond = dateUtils.getCurrentTimeByMilliSecond();
            parentStudentRelation.setCreateTime(currentTimeBySecond);
            parentStudentRelation.setUpdateTime(currentTimeBySecond);
            parentStudentRelation.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/parent_student_relation/:id/  04更新-ParentStudentRelation家长学生关系
     * @apiName updateParentStudentRelation
     * @apiGroup PARENT-STUDENT-RELATION-CONTROLLER
     * @apiParam {long} id
     * @apiParam {long} parentId
     * @apiParam {long} studentId
     * @apiParam {String} relationship
     * @apiParam {long} createTime
     * @apiParam {long} updateTime
     * @apiSuccess (Success 200){int} code 200
     */
    public CompletionStage<Result> updateParentStudentRelation(Http.Request request, long id) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ParentStudentRelation originalParentStudentRelation = ParentStudentRelation.find.byId(id);
            ParentStudentRelation newParentStudentRelation = Json.fromJson(jsonNode, ParentStudentRelation.class);
            if (null == originalParentStudentRelation) return okCustomJson(CODE40001, "数据不存在");
            if (newParentStudentRelation.parentId > 0)
                originalParentStudentRelation.setParentId(newParentStudentRelation.parentId);
            if (newParentStudentRelation.studentId > 0)
                originalParentStudentRelation.setStudentId(newParentStudentRelation.studentId);
            if (!ValidationUtil.isEmpty(newParentStudentRelation.relationship))
                originalParentStudentRelation.setRelationship(newParentStudentRelation.relationship);
            if (newParentStudentRelation.updateTime > 0)
                originalParentStudentRelation.setUpdateTime(newParentStudentRelation.updateTime);
            long currentTimeBySecond = dateUtils.getCurrentTimeByMilliSecond();
            originalParentStudentRelation.setUpdateTime(currentTimeBySecond);
            originalParentStudentRelation.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/parent_student_relation/   05删除-家长学生关系
     * @apiName deleteParentStudentRelation
     * @apiGroup PARENT-STUDENT-RELATION-CONTROLLER
     * @apiParam {long} id id
     * @apiParam {String} operation del时删除
     * @apiSuccess (Success 200){int} 200 成功
     */
    public CompletionStage<Result> deleteParentStudentRelation(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            long id = jsonNode.findPath("id").asLong();
            String operation = jsonNode.findPath("operation").asText();
            if (!"del".equals(operation)) return okCustomJson(CODE40001, "操作错误");
            ParentStudentRelation deleteModel = ParentStudentRelation.find.byId(id);
            if (null == deleteModel) return okCustomJson(CODE40001, "数据不存在");
            deleteModel.delete();
            return okJSON200();
        });
    }
}
