package controllers.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import controllers.BaseSecurityController;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.admin.ShopAdmin;
import models.business.ClassGroup;
import models.business.SchoolClass;
import models.business.Student;
import models.business.StudentGroup;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import utils.ValidationUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class ClassGroupController extends BaseSecurityController {

    /**
     * @api {POST} /v2/p/class_group_list/   01列表-该教师在此班级的分组配置
     * @apiName listClassGroup
     * @apiGroup 班级分组模块
     * @apiParam {int} page 页码
     * @apiParam {String} classId 班级ID（可选）
     * @apiSuccess (Success 200) {Long} id 分组ID
     * @apiSuccess (Success 200) {Long} orgId 机构ID
     * @apiSuccess (Success 200) {Long} classId 班级ID
     * @apiSuccess (Success 200) {Long} teacherId 老师ID
     * @apiSuccess (Success 200) {String} groupName 分组名称
     * @apiSuccess (Success 200) {Long} createTime 创建时间
     * @apiSuccess (Success 200) {Long} updateTime 更新时间
     * @apiSuccessExample {json} 响应示例:
     * {
     *     "pages": 1,
     *     "hasNest": false,
     *     "code": 200,
     *     "list": [
     *         {
     *             "id": 8,
     *             "orgId": 1,
     *             "classId": 1,
     *             "teacherId": 5,
     *             "groupName": "0101K组",
     *             "createTime": 1767063985555,
     *             "updateTime": 1767063985555,
     *             "className": "一年级一班",
     *             "studentGroups": [
     *                 {
     *                     "id": 36,
     *                     "groupId": 8,
     *                     "studentId": 35,
     *                     "createTime": 1767064055026,
     *                     "updateTime": 1767064055026,
     *                     "studentName": "邹锦浩",
     *                     "studentNumber": "20250135"
     *                 },
     *                 {
     *                     "id": 37,
     *                     "groupId": 8,
     *                     "studentId": 37,
     *                     "createTime": 1767064055026,
     *                     "updateTime": 1767064055026,
     *                     "studentName": "ZHENGBENJAMIN",
     *                     "studentNumber": "20250137"
     *                 },
     *                 {
     *                     "id": 38,
     *                     "groupId": 8,
     *                     "studentId": 38,
     *                     "createTime": 1767064055026,
     *                     "updateTime": 1767064055026,
     *                     "studentName": "翁嘉盛",
     *                     "studentNumber": "20250138"
     *                 },
     *                 {
     *                     "id": 39,
     *                     "groupId": 8,
     *                     "studentId": 39,
     *                     "createTime": 1767064055026,
     *                     "updateTime": 1767064055026,
     *                     "studentName": "CHENLIMATEO",
     *                     "studentNumber": "20250139"
     *                 }
     *             ]
     *         },
     *         {
     *             "id": 7,
     *             "orgId": 1,
     *             "classId": 1,
     *             "teacherId": 5,
     *             "groupName": "0101J组",
     *             "createTime": 1767063972705,
     *             "updateTime": 1767063972705,
     *             "className": "一年级一班",
     *             "studentGroups": [
     *                 {
     *                     "id": 31,
     *                     "groupId": 7,
     *                     "studentId": 32,
     *                     "createTime": 1767064048443,
     *                     "updateTime": 1767064048443,
     *                     "studentName": "林梓昱",
     *                     "studentNumber": "20250132"
     *                 },
     *                 {
     *                     "id": 32,
     *                     "groupId": 7,
     *                     "studentId": 33,
     *                     "createTime": 1767064048443,
     *                     "updateTime": 1767064048443,
     *                     "studentName": "姚凯彬",
     *                     "studentNumber": "20250133"
     *                 },
     *                 {
     *                     "id": 33,
     *                     "groupId": 7,
     *                     "studentId": 34,
     *                     "createTime": 1767064048443,
     *                     "updateTime": 1767064048443,
     *                     "studentName": "李诗琪",
     *                     "studentNumber": "20250134"
     *                 },
     *                 {
     *                     "id": 34,
     *                     "groupId": 7,
     *                     "studentId": 36,
     *                     "createTime": 1767064048443,
     *                     "updateTime": 1767064048443,
     *                     "studentName": "YANEVELYNANGELICA",
     *                     "studentNumber": "20250136"
     *                 },
     *                 {
     *                     "id": 35,
     *                     "groupId": 7,
     *                     "studentId": 31,
     *                     "createTime": 1767064048443,
     *                     "updateTime": 1767064048443,
     *                     "studentName": "黄以安",
     *                     "studentNumber": "20250131"
     *                 }
     *             ]
     *         },
     *         {
     *             "id": 6,
     *             "orgId": 1,
     *             "classId": 1,
     *             "teacherId": 5,
     *             "groupName": "0101F组",
     *             "createTime": 1767063962227,
     *             "updateTime": 1767063962227,
     *             "className": "一年级一班",
     *             "studentGroups": [
     *                 {
     *                     "id": 26,
     *                     "groupId": 6,
     *                     "studentId": 26,
     *                     "createTime": 1767064039140,
     *                     "updateTime": 1767064039140,
     *                     "studentName": "陈悦纳",
     *                     "studentNumber": "20250126"
     *                 },
     *                 {
     *                     "id": 27,
     *                     "groupId": 6,
     *                     "studentId": 27,
     *                     "createTime": 1767064039140,
     *                     "updateTime": 1767064039140,
     *                     "studentName": "庄沐恩",
     *                     "studentNumber": "20250127"
     *                 },
     *                 {
     *                     "id": 28,
     *                     "groupId": 6,
     *                     "studentId": 28,
     *                     "createTime": 1767064039140,
     *                     "updateTime": 1767064039140,
     *                     "studentName": "詹蓁蓁",
     *                     "studentNumber": "20250128"
     *                 },
     *                 {
     *                     "id": 29,
     *                     "groupId": 6,
     *                     "studentId": 29,
     *                     "createTime": 1767064039140,
     *                     "updateTime": 1767064039140,
     *                     "studentName": "陈炫宇",
     *                     "studentNumber": "20250129"
     *                 },
     *                 {
     *                     "id": 30,
     *                     "groupId": 6,
     *                     "studentId": 30,
     *                     "createTime": 1767064039140,
     *                     "updateTime": 1767064039140,
     *                     "studentName": "许梓轩",
     *                     "studentNumber": "20250130"
     *                 }
     *             ]
     *         },
     *         {
     *             "id": 5,
     *             "orgId": 1,
     *             "classId": 1,
     *             "teacherId": 5,
     *             "groupName": "0101E组",
     *             "createTime": 1767063954363,
     *             "updateTime": 1767063954363,
     *             "className": "一年级一班",
     *             "studentGroups": [
     *                 {
     *                     "id": 21,
     *                     "groupId": 5,
     *                     "studentId": 21,
     *                     "createTime": 1767064032222,
     *                     "updateTime": 1767064032222,
     *                     "studentName": "林恩多",
     *                     "studentNumber": "20250121"
     *                 },
     *                 {
     *                     "id": 22,
     *                     "groupId": 5,
     *                     "studentId": 22,
     *                     "createTime": 1767064032222,
     *                     "updateTime": 1767064032222,
     *                     "studentName": "黄昱翎",
     *                     "studentNumber": "20250122"
     *                 },
     *                 {
     *                     "id": 23,
     *                     "groupId": 5,
     *                     "studentId": 23,
     *                     "createTime": 1767064032222,
     *                     "updateTime": 1767064032222,
     *                     "studentName": "薛逸菲",
     *                     "studentNumber": "20250123"
     *                 },
     *                 {
     *                     "id": 24,
     *                     "groupId": 5,
     *                     "studentId": 24,
     *                     "createTime": 1767064032222,
     *                     "updateTime": 1767064032222,
     *                     "studentName": "庄景皓",
     *                     "studentNumber": "20250124"
     *                 },
     *                 {
     *                     "id": 25,
     *                     "groupId": 5,
     *                     "studentId": 25,
     *                     "createTime": 1767064032222,
     *                     "updateTime": 1767064032222,
     *                     "studentName": "林璟怡",
     *                     "studentNumber": "20250125"
     *                 }
     *             ]
     *         },
     *         {
     *             "id": 4,
     *             "orgId": 1,
     *             "classId": 1,
     *             "teacherId": 5,
     *             "groupName": "0101D组",
     *             "createTime": 1767063945907,
     *             "updateTime": 1767063945907,
     *             "className": "一年级一班",
     *             "studentGroups": [
     *                 {
     *                     "id": 16,
     *                     "groupId": 4,
     *                     "studentId": 16,
     *                     "createTime": 1767064024795,
     *                     "updateTime": 1767064024795,
     *                     "studentName": "余天鑫",
     *                     "studentNumber": "20250116"
     *                 },
     *                 {
     *                     "id": 17,
     *                     "groupId": 4,
     *                     "studentId": 17,
     *                     "createTime": 1767064024795,
     *                     "updateTime": 1767064024795,
     *                     "studentName": "何宥辰",
     *                     "studentNumber": "20250117"
     *                 },
     *                 {
     *                     "id": 18,
     *                     "groupId": 4,
     *                     "studentId": 18,
     *                     "createTime": 1767064024795,
     *                     "updateTime": 1767064024795,
     *                     "studentName": "林昊宸",
     *                     "studentNumber": "20250118"
     *                 },
     *                 {
     *                     "id": 19,
     *                     "groupId": 4,
     *                     "studentId": 19,
     *                     "createTime": 1767064024795,
     *                     "updateTime": 1767064024795,
     *                     "studentName": "严凌宇",
     *                     "studentNumber": "20250119"
     *                 },
     *                 {
     *                     "id": 20,
     *                     "groupId": 4,
     *                     "studentId": 20,
     *                     "createTime": 1767064024795,
     *                     "updateTime": 1767064024795,
     *                     "studentName": "杨欣妍",
     *                     "studentNumber": "20250120"
     *                 }
     *             ]
     *         },
     *         {
     *             "id": 3,
     *             "orgId": 1,
     *             "classId": 1,
     *             "teacherId": 5,
     *             "groupName": "0101C组",
     *             "createTime": 1767063937576,
     *             "updateTime": 1767063937576,
     *             "className": "一年级一班",
     *             "studentGroups": [
     *                 {
     *                     "id": 11,
     *                     "groupId": 3,
     *                     "studentId": 11,
     *                     "createTime": 1767064016362,
     *                     "updateTime": 1767064016362,
     *                     "studentName": "洪煜飞",
     *                     "studentNumber": "20250111"
     *                 },
     *                 {
     *                     "id": 12,
     *                     "groupId": 3,
     *                     "studentId": 12,
     *                     "createTime": 1767064016362,
     *                     "updateTime": 1767064016362,
     *                     "studentName": "林睿豪",
     *                     "studentNumber": "20250112"
     *                 },
     *                 {
     *                     "id": 13,
     *                     "groupId": 3,
     *                     "studentId": 13,
     *                     "createTime": 1767064016362,
     *                     "updateTime": 1767064016362,
     *                     "studentName": "胡佳恩",
     *                     "studentNumber": "20250113"
     *                 },
     *                 {
     *                     "id": 14,
     *                     "groupId": 3,
     *                     "studentId": 14,
     *                     "createTime": 1767064016362,
     *                     "updateTime": 1767064016362,
     *                     "studentName": "何欣钥",
     *                     "studentNumber": "20250114"
     *                 },
     *                 {
     *                     "id": 15,
     *                     "groupId": 3,
     *                     "studentId": 15,
     *                     "createTime": 1767064016362,
     *                     "updateTime": 1767064016362,
     *                     "studentName": "林哲锐",
     *                     "studentNumber": "20250115"
     *                 }
     *             ]
     *         },
     *         {
     *             "id": 2,
     *             "orgId": 1,
     *             "classId": 1,
     *             "teacherId": 5,
     *             "groupName": "0101B组",
     *             "createTime": 1767063928081,
     *             "updateTime": 1767063928081,
     *             "className": "一年级一班",
     *             "studentGroups": [
     *                 {
     *                     "id": 6,
     *                     "groupId": 2,
     *                     "studentId": 6,
     *                     "createTime": 1767064006979,
     *                     "updateTime": 1767064006979,
     *                     "studentName": "庄鹏博",
     *                     "studentNumber": "20250106"
     *                 },
     *                 {
     *                     "id": 7,
     *                     "groupId": 2,
     *                     "studentId": 7,
     *                     "createTime": 1767064006979,
     *                     "updateTime": 1767064006979,
     *                     "studentName": "林嘉韵",
     *                     "studentNumber": "20250107"
     *                 },
     *                 {
     *                     "id": 8,
     *                     "groupId": 2,
     *                     "studentId": 8,
     *                     "createTime": 1767064006979,
     *                     "updateTime": 1767064006979,
     *                     "studentName": "何安桐",
     *                     "studentNumber": "20250108"
     *                 },
     *                 {
     *                     "id": 9,
     *                     "groupId": 2,
     *                     "studentId": 9,
     *                     "createTime": 1767064006979,
     *                     "updateTime": 1767064006979,
     *                     "studentName": "周芯宥",
     *                     "studentNumber": "20250109"
     *                 },
     *                 {
     *                     "id": 10,
     *                     "groupId": 2,
     *                     "studentId": 10,
     *                     "createTime": 1767064006979,
     *                     "updateTime": 1767064006979,
     *                     "studentName": "余亦航",
     *                     "studentNumber": "20250110"
     *                 }
     *             ]
     *         },
     *         {
     *             "id": 1,
     *             "orgId": 1,
     *             "classId": 1,
     *             "teacherId": 5,
     *             "groupName": "0101A组",
     *             "createTime": 1767063912039,
     *             "updateTime": 1767063912039,
     *             "className": "一年级一班",
     *             "studentGroups": [
     *                 {
     *                     "id": 1,
     *                     "groupId": 1,
     *                     "studentId": 1,
     *                     "createTime": 1767063996010,
     *                     "updateTime": 1767063996010,
     *                     "studentName": "王欣瑶",
     *                     "studentNumber": "20250101"
     *                 },
     *                 {
     *                     "id": 2,
     *                     "groupId": 1,
     *                     "studentId": 2,
     *                     "createTime": 1767063996010,
     *                     "updateTime": 1767063996010,
     *                     "studentName": "邓安妮",
     *                     "studentNumber": "20250102"
     *                 },
     *                 {
     *                     "id": 3,
     *                     "groupId": 1,
     *                     "studentId": 3,
     *                     "createTime": 1767063996010,
     *                     "updateTime": 1767063996010,
     *                     "studentName": "翁煜宸",
     *                     "studentNumber": "20250103"
     *                 },
     *                 {
     *                     "id": 4,
     *                     "groupId": 1,
     *                     "studentId": 4,
     *                     "createTime": 1767063996010,
     *                     "updateTime": 1767063996010,
     *                     "studentName": "魏博辉",
     *                     "studentNumber": "20250104"
     *                 },
     *                 {
     *                     "id": 5,
     *                     "groupId": 1,
     *                     "studentId": 5,
     *                     "createTime": 1767063996010,
     *                     "updateTime": 1767063996010,
     *                     "studentName": "郑嘉诺",
     *                     "studentNumber": "20250105"
     *                 }
     *             ]
     *         }
     *     ]
     * }
     */
    public CompletionStage<Result> listClassGroup(Http.Request request) {
       JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ExpressionList<ClassGroup> expressionList = ClassGroup.find.query().where()
                    .eq("org_id", adminMember.getOrgId())
                    .eq("teacher_id", adminMember.getId());
            Integer page = jsonNode.get("page") != null ? jsonNode.get("page").asInt() : null;
            Long classId = jsonNode.get("classId") != null ? jsonNode.get("classId").asLong() : null;

            if (classId != null && classId > 0) {
                expressionList.eq("class_id", classId);
            }

            ObjectNode result = Json.newObject();
            List<ClassGroup> list;
            if (page == 0) {
                list = expressionList.findList();
            } else {
                PagedList<ClassGroup> pagedList = expressionList
                        .order().desc("create_time")
                        .setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_20)
                        .setMaxRows(BusinessConstant.PAGE_SIZE_20)
                        .findPagedList();
                list = pagedList.getList();
                result.put("pages", pagedList.getTotalPageCount());
                result.put("hasNest", pagedList.hasNext());
            }

            list = list.stream().peek(classGroup -> {
                List<StudentGroup> studentGroups = StudentGroup.find.query().where().eq("group_id", classGroup.getId()).findList();
                studentGroups.stream().peek(studentGroup -> {
                    Student student = Student.find.byId(studentGroup.getStudentId());
                    if (student != null) {
                        studentGroup.setStudentName(student.getName());
                        studentGroup.setStudentNumber(student.getStudentNumber());
                    }
                }).toList();
                String className = SchoolClass.find.byId(classGroup.getClassId())!= null ? SchoolClass.find.byId(classGroup.getClassId()).getClassName() : null;
                classGroup.setStudentGroups(studentGroups);
                classGroup.setClassName(className);
            }).collect(Collectors.toList());

            result.put(CODE, CODE200);
            result.set("list", Json.toJson(list));
            return ok(result);
        });
    }

    /**
     * @api {GET} /v2/p/class_group/:id/  02详情-班级分组
     * @apiName getClassGroup
     * @apiGroup 班级分组模块
     * @apiParam {Long} id 分组ID
     * @apiSuccess (Success 200) {Long} id 分组ID
     * @apiSuccess (Success 200) {Long} orgId 机构ID
     * @apiSuccess (Success 200) {Long} classId 班级ID
     * @apiSuccess (Success 200) {Long} teacherId 老师ID
     * @apiSuccess (Success 200) {String} groupName 分组名称
     * @apiSuccess (Success 200) {Long} createTime 创建时间
     * @apiSuccess (Success 200) {Long} updateTime 更新时间
     */
    public CompletionStage<Result> getClassGroup(Http.Request request, Long id) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ClassGroup classGroup = ClassGroup.find.byId(String.valueOf(id));
            if (null == classGroup) return okCustomJson(CODE40001, "数据不存在");
            // SaaS数据校验
            if (!classGroup.getOrgId().equals(adminMember.getOrgId())) {
                return okCustomJson(CODE40001, "数据不存在");
            }
            ObjectNode result = (ObjectNode) Json.toJson(classGroup);
            result.put(CODE, CODE200);
            return ok(result);
        });
    }

    /**
     * @api {POST} /v2/p/class_group/new/   03添加-班级分组
     * @apiName addClassGroup
     * @apiGroup 班级分组模块
     * @apiParam {Long} classId 班级ID
     * @apiParam {Long} teacherId 老师ID
     * @apiParam {String} groupName 分组名称
     * @apiSuccess (Success 200) {int} code 200
     */
    public CompletionStage<Result> addClassGroup(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((admin) -> {
            if (null == admin) return unauth403();
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");
            
            Long classId = jsonNode.findPath("classId").asLong(0);
            String groupName = jsonNode.findPath("groupName").asText(null);
            
            // 参数验证
            if (classId <= 0) return okCustomJson(CODE40001, "班级ID不能为空");
            if (ValidationUtil.isEmpty(groupName)) return okCustomJson(CODE40001, "分组名称不能为空");
            
            ClassGroup classGroup = new ClassGroup();
            classGroup.setOrgId(admin.getOrgId());
            classGroup.setClassId(classId);
            classGroup.setTeacherId(admin.id);
            classGroup.setGroupName(groupName);
            
            long currentTimeBySecond = dateUtils.getCurrentTimeByMilliSecond();
            classGroup.setCreateTime(currentTimeBySecond);
            classGroup.setUpdateTime(currentTimeBySecond);
            classGroup.save();
            
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/class_group/:id/  04更新-班级分组
     * @apiName updateClassGroup
     * @apiGroup 班级分组模块
     * @apiParam {Long} id 分组ID
     * @apiParam {Long} teacherId 老师ID（可选）
     * @apiParam {String} groupName 分组名称（可选）
     * @apiSuccess (Success 200) {int} code 200
     */
    public CompletionStage<Result> updateClassGroup(Http.Request request, Long id) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");
            
            ClassGroup originalClassGroup = ClassGroup.find.byId(String.valueOf(id));
            if (null == originalClassGroup) return okCustomJson(CODE40001, "数据不存在");
            // SaaS数据校验
            if (!originalClassGroup.getOrgId().equals(adminMember.getOrgId())) {
                return okCustomJson(CODE40001, "数据不存在");
            }
            
            Long teacherId = jsonNode.findPath("teacherId").asLong(0);
            String groupName = jsonNode.findPath("groupName").asText(null);
            
            if (teacherId > 0) {
                originalClassGroup.setTeacherId(teacherId);
            }
            if (!ValidationUtil.isEmpty(groupName)) {
                originalClassGroup.setGroupName(groupName);
            }
            
            long currentTimeBySecond = dateUtils.getCurrentTimeByMilliSecond();
            originalClassGroup.setUpdateTime(currentTimeBySecond);
            originalClassGroup.save();
            
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/class_group/   05删除-班级分组
     * @apiName deleteClassGroup
     * @apiGroup 班级分组模块
     * @apiParam {Long} id 分组ID
     * @apiParam {String} operation del时删除
     * @apiSuccess (Success 200) {int} 200 成功
     */
    public CompletionStage<Result> deleteClassGroup(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");
            
            Long id = jsonNode.findPath("id").asLong(0);
            String operation = jsonNode.findPath("operation").asText(null);
            
            if (id <= 0) return okCustomJson(CODE40001, "分组ID不能为空");
            if (!"del".equals(operation)) return okCustomJson(CODE40001, "操作错误");
            
            ClassGroup deleteModel = ClassGroup.find.byId(String.valueOf(id));
            if (null == deleteModel) return okCustomJson(CODE40001, "数据不存在");
            // SaaS数据校验
            if (!deleteModel.getOrgId().equals(adminMember.getOrgId())) {
                return okCustomJson(CODE40001, "数据不存在");
            }
            
            // 删除分组时，同时删除该分组下的所有学生关系
            List<StudentGroup> studentGroups = StudentGroup.find.query()
                    .where()
                    .eq("group_id", id)
                    .findList();
            for (StudentGroup studentGroup : studentGroups) {
                studentGroup.delete();
            }
            
            deleteModel.delete();
            return okJSON200();
        });
    }

    /**
     * @api {GET} /v2/p/class_group/students/:id/   06获取分组中的学生列表
     * @apiName getGroupStudents
     * @apiGroup 班级分组模块
     * @apiParam {Long} id 分组ID
     * @apiParam {String} filter 过滤条件（可选，支持按姓名或学号过滤）
     * @apiSuccess (Success 200) {int} code 200
     * @apiSuccess (Success 200) {Array} list 学生列表
     */
    public CompletionStage<Result> getGroupStudents(Http.Request request, Long id) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            
            ClassGroup classGroup = ClassGroup.find.byId(String.valueOf(id));
            if (null == classGroup) return okCustomJson(CODE40001, "分组不存在");
            // SaaS数据校验
            if (!classGroup.getOrgId().equals(adminMember.getOrgId())) {
                return okCustomJson(CODE40001, "数据不存在");
            }
            
            // 获取分组中的所有学生ID
            List<StudentGroup> studentGroups = StudentGroup.find.query()
                    .where()
                    .eq("group_id", id)
                    .findList();
            
            List<Long> studentIds = studentGroups.stream()
                    .map(StudentGroup::getStudentId)
                    .collect(Collectors.toList());
            
            if (studentIds.isEmpty()) {
                ObjectNode result = Json.newObject();
                result.put(CODE, CODE200);
                result.set("list", Json.toJson(new ArrayList<>()));
                return ok(result);
            }
            
            // 查询学生信息
            ExpressionList<Student> expressionList = Student.find.query()
                    .where()
                    .in("id", studentIds);
            
            // 获取过滤参数
            String filter = request.getQueryString("filter");
            if (!ValidationUtil.isEmpty(filter)) {
                expressionList.or()
                    .icontains("name", filter)
                    .icontains("student_number", filter)
                    .endOr();
            }
            
            List<Student> students = expressionList.findList();
            
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(students));
            return ok(result);
        });
    }

    /**
     * @api {POST} /v2/p/class_group/manage_students/:id/   07同步分组中的学生列表
     * @apiName manageGroupStudents
     * @apiDescription 根据传入的学生ID列表，自动同步分组中的学生：新增之前没有的学生，删除传入列表中缺少的学生。支持一对多关系。
     * @apiGroup 班级分组模块
     * @apiParam {Long} id 分组ID（路径参数）
     * @apiParam {Array} studentIds 学生ID数组，例如：[1, 2, 3, 4, 5]。系统会根据此列表自动判断哪些需要添加，哪些需要删除
     * @apiSuccess (Success 200) {int} code 200
     * @apiSuccess (Success 200) {int} addedCount 新增的学生数量
     * @apiSuccess (Success 200) {int} removedCount 删除的学生数量
     * @apiSuccess (Success 200) {int} invalidCount 无效的学生数量（学生不存在或不属于该班级）
     * @apiSuccessExample {json} 请求示例:
     * {
     *   "studentIds": [1, 2, 3, 4, 5]
     * }
     * @apiSuccessExample {json} 响应示例:
     * {
     *   "code": 200,
     *   "addedCount": 2,
     *   "removedCount": 1,
     *   "invalidCount": 0
     * }
     */
    public CompletionStage<Result> manageGroupStudents(Http.Request request, Long id) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");
            
            // 验证分组是否存在
            ClassGroup classGroup = ClassGroup.find.byId(String.valueOf(id));
            if (null == classGroup) return okCustomJson(CODE40001, "分组不存在");
            // SaaS数据校验
            if (!classGroup.getOrgId().equals(adminMember.getOrgId())) {
                return okCustomJson(CODE40001, "数据不存在");
            }
            
            // 获取学生ID列表
            JsonNode studentIdsNode = jsonNode.findPath("studentIds");
            if (studentIdsNode == null || !studentIdsNode.isArray()) {
                return okCustomJson(CODE40001, "学生ID列表格式错误");
            }
            
            // 提取所有学生ID（允许空列表，表示清空分组）
            Set<Long> targetStudentIds = new HashSet<>();
            for (JsonNode studentIdNode : studentIdsNode) {
                Long studentId = studentIdNode.asLong(0);
                if (studentId > 0) {
                    targetStudentIds.add(studentId);
                }
            }
            
            // 获取当前分组中已有的学生ID列表
            List<StudentGroup> existingStudentGroups = StudentGroup.find.query()
                    .where()
                    .eq("group_id", id)
                    .findList();
            
            Set<Long> existingStudentIds = existingStudentGroups.stream()
                    .map(StudentGroup::getStudentId)
                    .collect(Collectors.toSet());
            
            // 计算需要添加的学生ID（在目标列表中但不在现有列表中）
            Set<Long> toAdd = new HashSet<>(targetStudentIds);
            toAdd.removeAll(existingStudentIds);
            
            // 计算需要删除的学生ID（在现有列表中但不在目标列表中）
            Set<Long> toRemove = new HashSet<>(existingStudentIds);
            toRemove.removeAll(targetStudentIds);
            
            // 批量查询需要添加的学生是否存在，并验证是否属于该分组所在的班级
            List<Student> studentsToAdd = new ArrayList<>();
            if (!toAdd.isEmpty()) {
                studentsToAdd = Student.find.query()
                        .where()
                        .in("id", new ArrayList<>(toAdd))
                        .eq("org_id", adminMember.getOrgId())
                        .findList();
            }
            
            // 获取有效学生ID集合（存在且属于该班级）
            Long groupClassId = classGroup.getClassId();
            Set<Long> validStudentIds = studentsToAdd.stream()
                    .filter(student -> student.classId == groupClassId)
                    .map(Student::getId)
                    .collect(Collectors.toSet());
            
            // 统计信息
            int addedCount = 0;
            int removedCount = 0;
            int invalidCount = 0;
            
            long currentTimeBySecond = dateUtils.getCurrentTimeByMilliSecond();
            
            // 批量添加新的学生分组关系
            List<StudentGroup> newStudentGroups = new ArrayList<>();
            for (Long studentId : toAdd) {
                // 检查学生是否有效（存在且属于该班级）
                if (!validStudentIds.contains(studentId)) {
                    invalidCount++;
                    continue;
                }
                
                // 创建新的学生分组关系
                StudentGroup studentGroup = new StudentGroup();
                studentGroup.setGroupId(id);
                studentGroup.setStudentId(studentId);
                studentGroup.setCreateTime(currentTimeBySecond);
                studentGroup.setUpdateTime(currentTimeBySecond);
                newStudentGroups.add(studentGroup);
                addedCount++;
            }
            
            // 批量保存新增的关系
            if (!newStudentGroups.isEmpty()) {
                for (StudentGroup studentGroup : newStudentGroups) {
                    studentGroup.save();
                }
            }
            
            // 批量删除需要移除的学生分组关系
            if (!toRemove.isEmpty()) {
                List<StudentGroup> studentGroupsToRemove = existingStudentGroups.stream()
                        .filter(sg -> toRemove.contains(sg.getStudentId()))
                        .collect(Collectors.toList());
                
                for (StudentGroup studentGroup : studentGroupsToRemove) {
                    studentGroup.delete();
                    removedCount++;
                }
            }
            
            // 返回统计信息
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.put("addedCount", addedCount);
            result.put("removedCount", removedCount);
            result.put("invalidCount", invalidCount);
            return ok(result);
        });
    }

    /**
     * @api {GET} /v2/p/class_group/ungrouped_students/:classId/   10获取班级中未分组的学生列表
     * @apiName getUngroupedStudents
     * @apiGroup 班级分组模块
     * @apiParam {Long} classId 班级ID
     * @apiParam {String} filter 过滤条件（可选，支持按姓名或学号过滤）
     * @apiSuccess (Success 200) {int} code 200
     * @apiSuccess (Success 200) {Array} list 未分组的学生列表
     */
    public CompletionStage<Result> getUngroupedStudents(Http.Request request, Long classId) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            
            // 验证班级是否存在
            SchoolClass schoolClass = SchoolClass.find.byId(classId);
            if (null == schoolClass) return okCustomJson(CODE40001, "班级不存在");
            // SaaS数据校验
            if (schoolClass.getOrgId() != adminMember.getOrgId()) {
                return okCustomJson(CODE40001, "数据不存在");
            }
            
            // 获取该班级的所有分组
            List<ClassGroup> classGroups = ClassGroup.find.query()
                    .where()
                    .eq("class_id", classId)
                    .eq("teacher_id", adminMember.getId())
                    .findList();
            
            // 获取所有分组中的学生ID
            Set<Long> groupedStudentIds = new HashSet<>();
            for (ClassGroup classGroup : classGroups) {
                List<StudentGroup> studentGroups = StudentGroup.find.query()
                        .where()
                        .eq("group_id", classGroup.getId())
                        .findList();
                for (StudentGroup studentGroup : studentGroups) {
                    groupedStudentIds.add(studentGroup.getStudentId());
                }
            }
            
            // 查询该班级的所有学生
            ExpressionList<Student> expressionList = Student.find.query()
                    .where()
                    .eq("class_id", classId)
                    .eq("org_id", adminMember.getOrgId());
            
            // 获取过滤参数
            String filter = request.getQueryString("filter");
            if (!ValidationUtil.isEmpty(filter)) {
                expressionList.or()
                    .icontains("name", filter)
                    .icontains("student_number", filter)
                    .endOr();
            }
            
            List<Student> allStudents = expressionList.findList();
            
            // 过滤出未分组的学生
            List<Student> ungroupedStudents = allStudents.stream()
                    .filter(student -> !groupedStudentIds.contains(student.getId()))
                    .collect(Collectors.toList());
            
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(ungroupedStudents));
            return ok(result);
        });
    }
}

