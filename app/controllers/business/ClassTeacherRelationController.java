package controllers.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import controllers.BaseSecurityController;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.business.ClassTeacherRelation;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import utils.ValidationUtil;

import java.util.List;
import java.util.concurrent.CompletionStage;

public class ClassTeacherRelationController extends BaseSecurityController {

    /**
     * @api {GET} /v2/p/class_teacher_relation_list/   01列表-班级教师关系
     * @apiName listClassTeacherRelation
     * @apiGroup CLASS-TEACHER-RELATION-MANAGER
     * @apiParam {int} page 页码
     * @apiParam {String} filter 搜索栏()
     * @apiSuccess (Success 200) {long} id
     * @apiSuccess (Success 200) {long} classId
     * @apiSuccess (Success 200) {long} teacherId
     * @apiSuccess (Success 200) {String} subject
     * @apiSuccess (Success 200) {boolean} isHeadTeacher
     * @apiSuccess (Success 200) {long} createTime
     * @apiSuccess (Success 200) {long} updateTime
     */
    public CompletionStage<Result> listClassTeacherRelation(Http.Request request, int page, String filter, int status) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ExpressionList<ClassTeacherRelation> expressionList = ClassTeacherRelation.find.query().where();
            if (status > 0) expressionList.eq("status", status);
            if (!ValidationUtil.isEmpty(filter)) expressionList
                    .or()
                    .icontains("filter", filter)
                    .endOr();               //编写其他条件  
            //编写其他条件
            //编写其他条件
            //编写其他条件

            ObjectNode result = Json.newObject();
            List<ClassTeacherRelation> list;
            if (page == 0) list = expressionList.findList();
            else {
                PagedList<ClassTeacherRelation> pagedList = expressionList
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
     * @api {GET} /v2/p/class_teacher_relation/:id/  02详情-ClassTeacherRelation班级教师关系
     * @apiName getClassTeacherRelation
     * @apiGroup CLASS-TEACHER-RELATION-MANAGER
     * @apiParam {long} id id
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {long} id
     * @apiSuccess (Success 200) {long} classId
     * @apiSuccess (Success 200) {long} teacherId
     * @apiSuccess (Success 200) {String} subject
     * @apiSuccess (Success 200) {boolean} isHeadTeacher
     * @apiSuccess (Success 200) {long} createTime
     * @apiSuccess (Success 200) {long} updateTime
     */
    public CompletionStage<Result> getClassTeacherRelation(Http.Request request, long id) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ClassTeacherRelation classTeacherRelation = ClassTeacherRelation.find.byId(id);
            if (null == classTeacherRelation) return okCustomJson(CODE40001, "数据不存在");
            ObjectNode result = (ObjectNode) Json.toJson(classTeacherRelation);
            result.put(CODE, CODE200);
            return ok(result);
        });

    }

    /**
     * @api {POST} /v2/p/class_teacher_relation/new/   01添加-ClassTeacherRelation班级教师关系
     * @apiName addClassTeacherRelation
     * @apiDescription 描述
     * @apiGroup CLASS-TEACHER-RELATION-MANAGER
     * @apiParam {long} id
     * @apiParam {long} classId
     * @apiParam {long} teacherId
     * @apiParam {String} subject
     * @apiParam {boolean} isHeadTeacher
     * @apiParam {long} createTime
     * @apiParam {long} updateTime
     * @apiSuccess (Success 200){int} code 200
     */

    public CompletionStage<Result> addClassTeacherRelation(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((admin) -> {
            if (null == admin) return unauth403();
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");
            ClassTeacherRelation classTeacherRelation = Json.fromJson(jsonNode, ClassTeacherRelation.class);

            long currentTimeBySecond = dateUtils.getCurrentTimeByMilliSecond();
            classTeacherRelation.setCreateTime(currentTimeBySecond);
            classTeacherRelation.setUpdateTime(currentTimeBySecond);
            classTeacherRelation.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/class_teacher_relation/:id/  04更新-ClassTeacherRelation班级教师关系
     * @apiName updateClassTeacherRelation
     * @apiGroup CLASS-TEACHER-RELATION-MANAGER
     * @apiParam {long} id
     * @apiParam {long} classId
     * @apiParam {long} teacherId
     * @apiParam {String} subject
     * @apiParam {boolean} isHeadTeacher
     * @apiParam {long} createTime
     * @apiParam {long} updateTime
     * @apiSuccess (Success 200){int} code 200
     */
    public CompletionStage<Result> updateClassTeacherRelation(Http.Request request, long id) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ClassTeacherRelation originalClassTeacherRelation = ClassTeacherRelation.find.byId(id);
            ClassTeacherRelation newClassTeacherRelation = Json.fromJson(jsonNode, ClassTeacherRelation.class);
            if (null == originalClassTeacherRelation) return okCustomJson(CODE40001, "数据不存在");
            if (newClassTeacherRelation.classId > 0)
                originalClassTeacherRelation.setClassId(newClassTeacherRelation.classId);
            if (newClassTeacherRelation.teacherId > 0)
                originalClassTeacherRelation.setTeacherId(newClassTeacherRelation.teacherId);
            if (!ValidationUtil.isEmpty(newClassTeacherRelation.subject))
                originalClassTeacherRelation.setSubject(newClassTeacherRelation.subject);
            if (newClassTeacherRelation.isHeadTeacher != originalClassTeacherRelation.isHeadTeacher)
                originalClassTeacherRelation.setHeadTeacher(newClassTeacherRelation.isHeadTeacher);
            if (newClassTeacherRelation.updateTime > 0)
                originalClassTeacherRelation.setUpdateTime(newClassTeacherRelation.updateTime);
            long currentTimeBySecond = dateUtils.getCurrentTimeByMilliSecond();
            originalClassTeacherRelation.setUpdateTime(currentTimeBySecond);
            originalClassTeacherRelation.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/class_teacher_relation/   05删除-班级教师关系
     * @apiName deleteClassTeacherRelation
     * @apiGroup CLASS-TEACHER-RELATION-MANAGER
     * @apiParam {long} id id
     * @apiParam {String} operation del时删除
     * @apiSuccess (Success 200){int} 200 成功
     */
    public CompletionStage<Result> deleteClassTeacherRelation(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            long id = jsonNode.findPath("id").asLong();
            String operation = jsonNode.findPath("operation").asText();
            if (!"del".equals(operation)) return okCustomJson(CODE40001, "操作错误");
            ClassTeacherRelation deleteModel = ClassTeacherRelation.find.byId(id);
            if (null == deleteModel) return okCustomJson(CODE40001, "数据不存在");
            deleteModel.delete();
            return okJSON200();
        });
    }
}
