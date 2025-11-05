package controllers.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import controllers.BaseSecurityController;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.business.Student;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import utils.ValidationUtil;

import java.util.List;
import java.util.concurrent.CompletionStage;

public class StudentController extends BaseSecurityController {

    /**
     * @api {GET} /v2/p/student_list/   01列表-学生信息
     * @apiName listStudent
     * @apiGroup STUDENT-MANAGER
     * @apiParam {int} page 页码
     * @apiParam {String} filter 搜索栏()
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {String} studentNumber 学号
     * @apiSuccess (Success 200) {String} name 学生姓名
     * @apiSuccess (Success 200) {long} classId 班级ID
     * @apiSuccess (Success 200) {int} grade 年级
     * @apiSuccess (Success 200) {int} evaluationScheme 评价方案
     * @apiSuccess (Success 200) {double} classAverageScore 班级平均分
     * @apiSuccess (Success 200) {double} academicScore 学业得分
     * @apiSuccess (Success 200) {double} specialtyScore 特长得分
     * @apiSuccess (Success 200) {double} habitScore 习惯得分
     * @apiSuccess (Success 200) {double} totalScore 总分
     * @apiSuccess (Success 200) {String} badges 获得徽章
     * @apiSuccess (Success 200) {long} createTime 创建时间
     * @apiSuccess (Success 200) {long} updateTime 更新时间
     */
    public CompletionStage<Result> listStudent(Http.Request request, int page, String filter, int status) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ExpressionList<Student> expressionList = Student.find.query().where();
            if (status > 0) expressionList.eq("status", status);
            if (!ValidationUtil.isEmpty(filter)) expressionList
                    .or()
                    .icontains("filter", filter)
                    .endOr();               //编写其他条件  
            //编写其他条件
            //编写其他条件
            //编写其他条件

            ObjectNode result = Json.newObject();
            List<Student> list;
            if (page == 0) list = expressionList.findList();
            else {
                PagedList<Student> pagedList = expressionList
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
     * @api {GET} /v2/p/student/:id/  02详情-Student学生信息
     * @apiName getStudent
     * @apiGroup STUDENT-MANAGER
     * @apiParam {long} id id
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {String} studentNumber 学号
     * @apiSuccess (Success 200) {String} name 学生姓名
     * @apiSuccess (Success 200) {long} classId 班级ID
     * @apiSuccess (Success 200) {int} grade 年级
     * @apiSuccess (Success 200) {int} evaluationScheme 评价方案
     * @apiSuccess (Success 200) {double} classAverageScore 班级平均分
     * @apiSuccess (Success 200) {double} academicScore 学业得分
     * @apiSuccess (Success 200) {double} specialtyScore 特长得分
     * @apiSuccess (Success 200) {double} habitScore 习惯得分
     * @apiSuccess (Success 200) {double} totalScore 总分
     * @apiSuccess (Success 200) {String} badges 获得徽章
     * @apiSuccess (Success 200) {long} createTime 创建时间
     * @apiSuccess (Success 200) {long} updateTime 更新时间
     */
    public CompletionStage<Result> getStudent(Http.Request request, long id) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            Student student = Student.find.byId(id);
            if (null == student) return okCustomJson(CODE40001, "数据不存在");
            ObjectNode result = (ObjectNode) Json.toJson(student);
            result.put(CODE, CODE200);
            return ok(result);
        });

    }

    /**
     * @api {POST} /v2/p/student/new/   01添加-Student学生信息
     * @apiName addStudent
     * @apiDescription 描述
     * @apiGroup STUDENT-MANAGER
     * @apiParam {long} id 唯一标识
     * @apiParam {String} studentNumber 学号
     * @apiParam {String} name 学生姓名
     * @apiParam {long} classId 班级ID
     * @apiParam {int} grade 年级
     * @apiParam {int} evaluationScheme 评价方案
     * @apiParam {double} classAverageScore 班级平均分
     * @apiParam {double} academicScore 学业得分
     * @apiParam {double} specialtyScore 特长得分
     * @apiParam {double} habitScore 习惯得分
     * @apiParam {double} totalScore 总分
     * @apiParam {String} badges 获得徽章
     * @apiParam {long} createTime 创建时间
     * @apiParam {long} updateTime 更新时间
     * @apiSuccess (Success 200){int} code 200
     */

    public CompletionStage<Result> addStudent(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((admin) -> {
            if (null == admin) return unauth403();
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");
            Student student = Json.fromJson(jsonNode, Student.class);

            long currentTimeBySecond = dateUtils.getCurrentTimeByMilliSecond();
            student.setCreateTime(currentTimeBySecond);
            student.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/student/:id/  04更新-Student学生信息
     * @apiName updateStudent
     * @apiGroup STUDENT-MANAGER
     * @apiParam {long} id 唯一标识
     * @apiParam {String} studentNumber 学号
     * @apiParam {String} name 学生姓名
     * @apiParam {long} classId 班级ID
     * @apiParam {int} grade 年级
     * @apiParam {int} evaluationScheme 评价方案
     * @apiParam {double} classAverageScore 班级平均分
     * @apiParam {double} academicScore 学业得分
     * @apiParam {double} specialtyScore 特长得分
     * @apiParam {double} habitScore 习惯得分
     * @apiParam {double} totalScore 总分
     * @apiParam {String} badges 获得徽章
     * @apiParam {long} createTime 创建时间
     * @apiParam {long} updateTime 更新时间
     * @apiSuccess (Success 200){int} code 200
     */
    public CompletionStage<Result> updateStudent(Http.Request request, long id) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            Student originalStudent = Student.find.byId(id);
            Student newStudent = Json.fromJson(jsonNode, Student.class);
            if (null == originalStudent) return okCustomJson(CODE40001, "数据不存在");
            if (!ValidationUtil.isEmpty(newStudent.studentNumber))
                originalStudent.setStudentNumber(newStudent.studentNumber);
            if (!ValidationUtil.isEmpty(newStudent.name)) originalStudent.setName(newStudent.name);
            if (newStudent.classId > 0) originalStudent.setClassId(newStudent.classId);
            if (newStudent.grade > 0) originalStudent.setGrade(newStudent.grade);

            if (newStudent.evaluationScheme > 0) {
                if (newStudent.evaluationScheme==Student.SCHEME_B) {
                    if (!newStudent.isOverAverage()) {
                        return okCustomJson(CODE40001, "学业成绩未达到班级平均分，不能选择方案B");
                    }
                }
                originalStudent.setEvaluationScheme(newStudent.evaluationScheme);
            }

            if (newStudent.classAverageScore > 0) originalStudent.setClassAverageScore(newStudent.classAverageScore);
            if (newStudent.academicScore > 0) originalStudent.setAcademicScore(newStudent.academicScore);
            if (newStudent.specialtyScore > 0) originalStudent.setSpecialtyScore(newStudent.specialtyScore);
            if (newStudent.habitScore > 0) originalStudent.setHabitScore(newStudent.habitScore);
            if (newStudent.totalScore > 0) originalStudent.setTotalScore(newStudent.totalScore);
            if (!ValidationUtil.isEmpty(newStudent.badges)) originalStudent.setBadges(newStudent.badges);
            if (newStudent.updateTime > 0) originalStudent.setUpdateTime(newStudent.updateTime);
            long currentTimeBySecond = dateUtils.getCurrentTimeByMilliSecond();
            originalStudent.setUpdateTime(currentTimeBySecond);
            originalStudent.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/student/   05删除-学生信息
     * @apiName deleteStudent
     * @apiGroup STUDENT-MANAGER
     * @apiParam {long} id id
     * @apiParam {String} operation del时删除
     * @apiSuccess (Success 200){int} 200 成功
     */
    public CompletionStage<Result> deleteStudent(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            long id = jsonNode.findPath("id").asLong();
            String operation = jsonNode.findPath("operation").asText();
            if (!"del".equals(operation)) return okCustomJson(CODE40001, "操作错误");
            Student deleteModel = Student.find.byId(id);
            if (null == deleteModel) return okCustomJson(CODE40001, "数据不存在");
            deleteModel.delete();
            return okJSON200();
        });
    }


}
