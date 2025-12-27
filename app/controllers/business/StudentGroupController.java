package controllers.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import controllers.BaseSecurityController;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.business.StudentGroup;
import models.business.Student;
import models.business.SchoolClass;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import utils.ValidationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class StudentGroupController extends BaseSecurityController {

    /**
     * @api {POST} /v2/p/student_group_list/   01列表-学生分组
     * @apiName listStudentGroup
     * @apiGroup 学生分组模块
     * @apiParam {int} page 页码  //0-全查  1-分页
     * @apiParam {String} groupName 分组名称
     * @apiParam {long} classId 班级ID
     * @apiParam {long} studentId 学生ID
     * @apiParamExample  {json} 请求示例:
     * {
     *     "page":1,
     *     "groupName":"第一组",
     *     "classId":1
     * }
     * @apiSuccess (Success 200) {long} orgId 机构ID
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {long} studentId 学生ID
     * @apiSuccess (Success 200) {long} classId 班级ID
     * @apiSuccess (Success 200) {String} groupName 分组名称
     * @apiSuccess (Success 200) {long} createTime 创建时间
     * @apiSuccess (Success 200) {long} updateTime 更新时间
     * @apiSuccess (Success 200) {int} status 状态
     * @apiSuccessExample {json} 响应示例:
     * {
     *     "pages": 10,
     *     "hasNest": true,
     *     "code": 200,
     *     "list": [
     *         {
     *             "orgId": 1,
     *             "id": 1,
     *             "studentId": 100,
     *             "classId": 47,
     *             "groupName": "第一组",
     *             "createTime": 1766649180731,
     *             "updateTime": 1766649180731,
     *             "status": 1
     *         }
     *     ]
     * }
     */
    public CompletionStage<Result> listStudentGroup(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();

            int page = jsonNode.get("page").asInt();
            String groupName = jsonNode.has("groupName") && !jsonNode.get("groupName").isNull()
                    ? jsonNode.get("groupName").asText()
                    : null;
            long classId = jsonNode.has("classId") && !jsonNode.get("classId").isNull()
                    ? jsonNode.get("classId").asLong()
                    : 0;
            long studentId = jsonNode.has("studentId") && !jsonNode.get("studentId").isNull()
                    ? jsonNode.get("studentId").asLong()
                    : 0;

            ExpressionList<StudentGroup> expressionList = StudentGroup.find.query().where()
                    .eq("org_id", adminMember.getOrgId());

            if (!ValidationUtil.isEmpty(groupName)) {
                expressionList.icontains("group_name", groupName);
            }
            if (classId > 0) {
                expressionList.eq("class_id", classId);
            }
            if (studentId > 0) {
                expressionList.eq("student_id", studentId);
            }

            ObjectNode result = Json.newObject();
            List<StudentGroup> list;
            if (page == 0) {
                list = expressionList.findList();
            } else {
                PagedList<StudentGroup> pagedList = expressionList
                        .order().desc("id")
                        .setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_20)
                        .setMaxRows(BusinessConstant.PAGE_SIZE_20)
                        .findPagedList();
                list = pagedList.getList();
                result.put("pages", pagedList.getTotalPageCount());
                result.put("hasNest", pagedList.hasNext());
            }
            
            // 填充学生和班级信息
            List<ObjectNode> resultList = new ArrayList<>();
            for (StudentGroup group : list) {
                ObjectNode groupNode = (ObjectNode) Json.toJson(group);
                // 查询学生信息
                if (group.studentId > 0) {
                    Student student = Student.find.byId(group.studentId);
                    if (student != null) {
                        groupNode.put("studentName", student.name);
                    }
                }
                // 查询班级信息
                if (group.classId > 0) {
                    SchoolClass schoolClass = SchoolClass.find.byId(group.classId);
                    if (schoolClass != null) {
                        groupNode.put("className", schoolClass.className);
                    }
                }
                resultList.add(groupNode);
            }
            
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(resultList));
            return ok(result);
        });
    }

    /**
     * @api {GET} /v2/p/student_group/:id/  02详情-学生分组
     * @apiName getStudentGroup
     * @apiGroup 学生分组模块
     * @apiParam {long} id id
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {long} orgId 机构ID
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {long} studentId 学生ID
     * @apiSuccess (Success 200) {long} classId 班级ID
     * @apiSuccess (Success 200) {String} groupName 分组名称
     * @apiSuccess (Success 200) {long} createTime 创建时间
     * @apiSuccess (Success 200) {long} updateTime 更新时间
     * @apiSuccess (Success 200) {int} status 状态
     */
    public CompletionStage<Result> getStudentGroup(Http.Request request, long id) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            StudentGroup studentGroup = StudentGroup.find.byId(id);
            if (null == studentGroup) return okCustomJson(CODE40001, "数据不存在");
            //sass数据校验
            if (studentGroup.orgId != adminMember.getOrgId()) return okCustomJson(CODE40001, "数据不存在");
            ObjectNode result = (ObjectNode) Json.toJson(studentGroup);
            result.put(CODE, CODE200);
            return ok(result);
        });
    }

    /**
     * @api {POST} /v2/p/student_group/new/   03添加-学生分组
     * @apiName addStudentGroup
     * @apiDescription 添加学生分组
     * @apiGroup 学生分组模块
     * @apiParam {long} studentId 学生ID
     * @apiParam {long} classId 班级ID
     * @apiParam {String} groupName 分组名称
     * @apiParam {int} status 状态
     * @apiSuccess (Success 200){int} code 200
     */
    public CompletionStage<Result> addStudentGroup(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((admin) -> {
            if (null == admin) return unauth403();
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");
            StudentGroup studentGroup = Json.fromJson(jsonNode, StudentGroup.class);
            // 数据sass化
            studentGroup.setOrgId(admin.getOrgId());
            long currentTimeBySecond = dateUtils.getCurrentTimeByMilliSecond();
            studentGroup.setCreateTime(currentTimeBySecond);
            studentGroup.setUpdateTime(currentTimeBySecond);
            if (studentGroup.getStatus() == 0) {
                studentGroup.setStatus(1);
            }
            studentGroup.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/student_group/:id/  04更新-学生分组
     * @apiName updateStudentGroup
     * @apiGroup 学生分组模块
     * @apiParam {long} id 唯一标识
     * @apiParam {long} studentId 学生ID
     * @apiParam {long} classId 班级ID
     * @apiParam {String} groupName 分组名称
     * @apiParam {int} status 状态
     * @apiSuccess (Success 200){int} code 200
     */
    public CompletionStage<Result> updateStudentGroup(Http.Request request, long id) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            StudentGroup originalStudentGroup = StudentGroup.find.byId(id);
            StudentGroup newStudentGroup = Json.fromJson(jsonNode, StudentGroup.class);
            if (null == originalStudentGroup) return okCustomJson(CODE40001, "数据不存在");
            //sass数据校验
            if (originalStudentGroup.orgId != adminMember.getOrgId()) return okCustomJson(CODE40001, "数据不存在");
            if (newStudentGroup.studentId > 0) originalStudentGroup.setStudentId(newStudentGroup.studentId);
            if (newStudentGroup.classId > 0) originalStudentGroup.setClassId(newStudentGroup.classId);
            if (!ValidationUtil.isEmpty(newStudentGroup.groupName))
                originalStudentGroup.setGroupName(newStudentGroup.groupName);
            if (newStudentGroup.status >= 0) originalStudentGroup.setStatus(newStudentGroup.status);
            long currentTimeBySecond = dateUtils.getCurrentTimeByMilliSecond();
            originalStudentGroup.setUpdateTime(currentTimeBySecond);
            originalStudentGroup.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/student_group/   05删除-学生分组
     * @apiName deleteStudentGroup
     * @apiGroup 学生分组模块
     * @apiParam {long} id id
     * @apiParam {String} operation del时删除
     * @apiSuccess (Success 200){int} 200 成功
     */
    public CompletionStage<Result> deleteStudentGroup(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            long id = jsonNode.findPath("id").asLong();
            String operation = jsonNode.findPath("operation").asText();
            if (!"del".equals(operation)) return okCustomJson(CODE40001, "操作错误");
            StudentGroup deleteModel = StudentGroup.find.byId(id);
            if (null == deleteModel) return okCustomJson(CODE40001, "数据不存在");
            //sass数据校验
            if (deleteModel.orgId != adminMember.getOrgId()) return okCustomJson(CODE40001, "数据不存在");
            deleteModel.delete();
            return okJSON200();
        });
    }
}

