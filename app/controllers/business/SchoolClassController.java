package controllers.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import controllers.BaseSecurityController;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.business.SchoolClass;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import utils.ValidationUtil;

import java.util.List;
import java.util.concurrent.CompletionStage;

public class SchoolClassController extends BaseSecurityController {

    /**
     * @api {GET} /v2/p/school_class_list/   01列表-班级信息
     * @apiName listSchoolClass
     * @apiGroup SCHOOL-CLASS-MANAGER
     * @apiParam {int} page 页码
     * @apiParam {String} filter 搜索栏()
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {String} className 班级名称
     * @apiSuccess (Success 200) {int} grade 年级
     * @apiSuccess (Success 200) {long} headTeacherId 班主任ID
     * @apiSuccess (Success 200) {double} academicScore 学业得分
     * @apiSuccess (Success 200) {double} specialtyScore 特长得分
     * @apiSuccess (Success 200) {double} routineScore 常规得分
     * @apiSuccess (Success 200) {double} homeVisitScore 家访得分
     * @apiSuccess (Success 200) {double} totalScore 总分
     * @apiSuccess (Success 200) {boolean} disqualified 一票否决
     * @apiSuccess (Success 200) {String} honorTitle 荣誉称号
     * @apiSuccess (Success 200) {long} createTime 创建时间
     */
    public CompletionStage<Result> listSchoolClass(Http.Request request, int page, String filter, int status) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ExpressionList<SchoolClass> expressionList = SchoolClass.find.query().where();
            if (status > 0) expressionList.eq("status", status);
            if (!ValidationUtil.isEmpty(filter)) expressionList
                    .or()
                    .icontains("filter", filter)
                    .endOr();               //编写其他条件  
            //编写其他条件
            //编写其他条件
            //编写其他条件

            ObjectNode result = Json.newObject();
            List<SchoolClass> list;
            if (page == 0) list = expressionList.findList();
            else {
                PagedList<SchoolClass> pagedList = expressionList
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
     * @api {GET} /v2/p/school_class/:id/  02详情-SchoolClass班级信息
     * @apiName getSchoolClass
     * @apiGroup SCHOOL-CLASS-MANAGER
     * @apiParam {long} id id
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {String} className 班级名称
     * @apiSuccess (Success 200) {int} grade 年级
     * @apiSuccess (Success 200) {long} headTeacherId 班主任ID
     * @apiSuccess (Success 200) {double} academicScore 学业得分
     * @apiSuccess (Success 200) {double} specialtyScore 特长得分
     * @apiSuccess (Success 200) {double} routineScore 常规得分
     * @apiSuccess (Success 200) {double} homeVisitScore 家访得分
     * @apiSuccess (Success 200) {double} totalScore 总分
     * @apiSuccess (Success 200) {boolean} disqualified 一票否决
     * @apiSuccess (Success 200) {String} honorTitle 荣誉称号
     * @apiSuccess (Success 200) {long} createTime 创建时间
     */
    public CompletionStage<Result> getSchoolClass(Http.Request request, long id) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            SchoolClass schoolClass = SchoolClass.find.byId(id);
            if (null == schoolClass) return okCustomJson(CODE40001, "数据不存在");
            ObjectNode result = (ObjectNode) Json.toJson(schoolClass);
            result.put(CODE, CODE200);
            return ok(result);
        });

    }

    /**
     * @api {POST} /v2/p/school_class/new/   01添加-SchoolClass班级信息
     * @apiName addSchoolClass
     * @apiDescription 描述
     * @apiGroup SCHOOL-CLASS-MANAGER
     * @apiParam {long} id 唯一标识
     * @apiParam {String} className 班级名称
     * @apiParam {int} grade 年级
     * @apiParam {long} headTeacherId 班主任ID
     * @apiParam {double} academicScore 学业得分
     * @apiParam {double} specialtyScore 特长得分
     * @apiParam {double} routineScore 常规得分
     * @apiParam {double} homeVisitScore 家访得分
     * @apiParam {double} totalScore 总分
     * @apiParam {boolean} disqualified 一票否决
     * @apiParam {String} honorTitle 荣誉称号
     * @apiParam {long} createTime 创建时间
     * @apiSuccess (Success 200){int} code 200
     */

    public CompletionStage<Result> addSchoolClass(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((admin) -> {
            if (null == admin) return unauth403();
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");
            SchoolClass schoolClass = Json.fromJson(jsonNode, SchoolClass.class);

            long currentTimeBySecond = dateUtils.getCurrentTimeByMilliSecond();
            schoolClass.setCreateTime(currentTimeBySecond);
            schoolClass.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/school_class/:id/  04更新-SchoolClass班级信息
     * @apiName updateSchoolClass
     * @apiGroup SCHOOL-CLASS-MANAGER
     * @apiParam {long} id 唯一标识
     * @apiParam {String} className 班级名称
     * @apiParam {int} grade 年级
     * @apiParam {long} headTeacherId 班主任ID
     * @apiParam {double} academicScore 学业得分
     * @apiParam {double} specialtyScore 特长得分
     * @apiParam {double} routineScore 常规得分
     * @apiParam {double} homeVisitScore 家访得分
     * @apiParam {double} totalScore 总分
     * @apiParam {boolean} disqualified 一票否决
     * @apiParam {String} honorTitle 荣誉称号
     * @apiParam {String} teachersJson 科任教师
     * @apiParam {long} createTime 创建时间
     * @apiSuccess (Success 200){int} code 200
     */
    public CompletionStage<Result> updateSchoolClass(Http.Request request, long id) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            SchoolClass originalSchoolClass = SchoolClass.find.byId(id);
            SchoolClass newSchoolClass = Json.fromJson(jsonNode, SchoolClass.class);
            if (null == originalSchoolClass) return okCustomJson(CODE40001, "数据不存在");
            if (!ValidationUtil.isEmpty(newSchoolClass.className))
                originalSchoolClass.setClassName(newSchoolClass.className);
            if (newSchoolClass.grade > 0) originalSchoolClass.setGrade(newSchoolClass.grade);
            if (newSchoolClass.headTeacherId > 0) originalSchoolClass.setHeadTeacherId(newSchoolClass.headTeacherId);
            if (newSchoolClass.academicScore > 0) originalSchoolClass.setAcademicScore(newSchoolClass.academicScore);
            if (newSchoolClass.specialtyScore > 0) originalSchoolClass.setSpecialtyScore(newSchoolClass.specialtyScore);
            if (newSchoolClass.routineScore > 0) originalSchoolClass.setRoutineScore(newSchoolClass.routineScore);
            if (newSchoolClass.homeVisitScore > 0) originalSchoolClass.setHomeVisitScore(newSchoolClass.homeVisitScore);
            if (newSchoolClass.totalScore > 0) originalSchoolClass.setTotalScore(newSchoolClass.totalScore);
            if (newSchoolClass.disqualified != originalSchoolClass.disqualified)
                originalSchoolClass.setDisqualified(newSchoolClass.disqualified);
            if (!ValidationUtil.isEmpty(newSchoolClass.honorTitle))
                originalSchoolClass.setHonorTitle(newSchoolClass.honorTitle);
            if (!ValidationUtil.isEmpty(newSchoolClass.teacherIds))
                originalSchoolClass.setTeacherIds(newSchoolClass.teacherIds);

            originalSchoolClass.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/school_class/   05删除-班级信息
     * @apiName deleteSchoolClass
     * @apiGroup SCHOOL-CLASS-MANAGER
     * @apiParam {long} id id
     * @apiParam {String} operation del时删除
     * @apiSuccess (Success 200){int} 200 成功
     */
    public CompletionStage<Result> deleteSchoolClass(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            long id = jsonNode.findPath("id").asLong();
            String operation = jsonNode.findPath("operation").asText();
            if (!"del".equals(operation)) return okCustomJson(CODE40001, "操作错误");
            SchoolClass deleteModel = SchoolClass.find.byId(id);
            if (null == deleteModel) return okCustomJson(CODE40001, "数据不存在");
            deleteModel.delete();
            return okJSON200();
        });
    }
}
