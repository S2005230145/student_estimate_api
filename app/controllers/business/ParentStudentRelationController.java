package controllers.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import controllers.BaseSecurityController;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.admin.ShopAdmin;
import models.business.*;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import utils.EncodeUtils;
import utils.ValidationUtil;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static models.business.HabitRecord.MOUTH_PARENT_MAX_SCORE;

public class ParentStudentRelationController extends BaseSecurityController {
    @Inject
    EncodeUtils encodeUtils;

    /**
     * @api {GET} /v2/p/parent_student_relation_list/   01列表-家长学生关系表
     * @apiName listParentStudentRelation
     * @apiGroup 学生-家长模块
     * @apiParam {int} page 页码
     * @apiParam {String} filter 搜索栏()
     * @apiSuccess (Success 200) {long} orgId 机构ID
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {long} parentId 家长ID
     * @apiSuccess (Success 200) {long} studentId 学生ID
     * @apiSuccess (Success 200) {String} relationship 关系类型
     * @apiSuccess (Success 200) {long} createTime 创建时间
     * @apiSuccess (Success 200) {long} updateTime 更新时间
     */
    public CompletionStage<Result> listParentStudentRelation(Http.Request request, int page, String filter, int status) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ExpressionList<ParentStudentRelation> expressionList = ParentStudentRelation.find.query().where().eq("org_id", adminMember.getOrgId());
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
     * @api {GET} /v2/p/parent_student_relation/:id/  02详情-ParentStudentRelation家长学生关系表
     * @apiName getParentStudentRelation
     * @apiGroup 学生-家长模块
     * @apiParam {long} id id
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {long} orgId 机构ID
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {long} parentId 家长ID
     * @apiSuccess (Success 200) {long} studentId 学生ID
     * @apiSuccess (Success 200) {String} relationship 关系类型
     * @apiSuccess (Success 200) {long} createTime 创建时间
     * @apiSuccess (Success 200) {long} updateTime 更新时间
     */
    public CompletionStage<Result> getParentStudentRelation(Http.Request request, long id) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ParentStudentRelation parentStudentRelation = ParentStudentRelation.find.byId(id);
            if (null == parentStudentRelation) return okCustomJson(CODE40001, "数据不存在");
            //sass数据校验  
            if (parentStudentRelation.orgId != adminMember.getOrgId()) return okCustomJson(CODE40001, "数据不存在");
            ObjectNode result = (ObjectNode) Json.toJson(parentStudentRelation);
            result.put(CODE, CODE200);
            return ok(result);
        });

    }

    /**
     * @api {POST} /v2/p/parent_student_relation/new/   03添加-ParentStudentRelation家长学生关系表
     * @apiName addParentStudentRelation
     * @apiDescription 描述
     * @apiGroup 学生-家长模块
     * @apiParam {long} studentId 学生Id
     * @apiParam {String} relationship 关系类型（如：爸爸、妈妈等）
     * @apiParam {String} parentPhone 家长手机号码
     * @apiSuccess (Success 200){int} code 200
     */

    public CompletionStage<Result> addParentStudentRelation(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((admin) -> {
            if (null == admin) return unauth403();
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");
            
            try {
                long studentId = jsonNode.findPath("studentId").asLong();
                String relationship = jsonNode.findPath("relationship").asText();
                String parentPhone = jsonNode.findPath("parentPhone").asText(null);
                
                // 参数验证
                if (studentId <= 0) return okCustomJson(CODE40001, "学生ID不能为空");
                if (ValidationUtil.isEmpty(relationship)) return okCustomJson(CODE40001, "关系类型不能为空");
                if (!isValidParentInfo(parentPhone)) return okCustomJson(CODE40001, "家长手机号不能为空");
                
                // 验证学生是否存在
                Student student = Student.find.byId(studentId);
                if (student == null) return okCustomJson(CODE40001, "学生不存在");
                
                // 根据手机号查找或创建家长账号（完全按照导入逻辑）
                ShopAdmin parent = findOrCreateParent(
                        relationship,  // name参数在导入逻辑中实际未使用，但保留以保持一致性
                        parentPhone,
                        relationship,
                        student.getName(),
                        admin.getOrgId()
                );
                
                if (parent == null) {
                    return okCustomJson(CODE40001, "家长账号创建失败");
                }
                
                // 创建家长学生关系（使用导入逻辑中的方法）
                ParentStudentRelation.addRelation(parent.getId(), studentId, relationship, admin.getOrgId());
                
                return okJSON200();
            } catch (Exception e) {
                return okCustomJson(CODE40001, "创建家长关系失败：" + e.getMessage());
            }
        });
    }
    
    /**
     * 查找或创建家长账号（完全按照导入逻辑实现）
     */
    private ShopAdmin findOrCreateParent(String name, String phone, String relationship, String studentName, long orgId) {
        if (name == null || name.trim().isEmpty() || phone == null || phone.trim().isEmpty()) {
            return null;
        }
        
        //name = name.trim();  // 导入逻辑中name参数未使用，但保留
        phone = phone.trim();
        
        // 查找现有家长（按手机号查找，确保唯一性）
        ShopAdmin parent = ShopAdmin.find.query()
                .where()
                .eq("phone_number", phone)
                .findOne();
        
        if (parent != null) {
            // 如果找到现有家长，更新相关信息（完全按照导入逻辑）
            if (!generateParentRealName(studentName, relationship).equals(parent.getRealName())) {
                if (parent.getRules() != null && parent.getRules().equals("家长")) {
                    parent.setUserName(phone);
                    parent.setRealName(generateParentRealName(studentName, relationship));
                    parent.setOrgId(orgId);
                    parent.update();
                } else if (parent.getRules() != null && parent.getRules().contains("科任教师") && !parent.getRules().contains("家长")) {
                    parent.setUserName(phone);
                    //parent.setRealName(generateParentRealName(studentName, relationship));  // 导入逻辑中注释掉了
                    parent.setOrgId(orgId);
                    parent.setRules(parent.getRules() + ",家长");
                    parent.update();
                }
            }
            return parent;
        }
        
        // 创建新家长账号
        parent = new ShopAdmin();
        parent.setPhoneNumber(phone);
        parent.setUserName(phone); // 用户名设为手机号
        parent.setRealName(generateParentRealName(studentName, relationship)); // 真实姓名设为"学生名-关系"
        parent.setRules("家长");
        parent.setPassword(generateDefaultPassword());
        parent.setOrgId(orgId);
        parent.setStatus(1);
        parent.save();
        return parent;
    }
    
    /**
     * 生成默认密码
     */
    private String generateDefaultPassword() {
        return encodeUtils.getMd5WithSalt("123456");
    }
    
    /**
     * 生成家长真实姓名（学生名-关系）
     */
    private static String generateParentRealName(String studentName, String relationship) {
        return studentName + "-" + relationship;
    }
    
    /**
     * 验证家长信息是否有效（按照导入逻辑）
     */
    private static boolean isValidParentInfo(String phone) {
        return phone != null && !phone.trim().isEmpty();
    }

    /**
     * @api {POST} /v2/p/parent_student_relation/:id/  04更新-ParentStudentRelation家长学生关系表
     * @apiName updateParentStudentRelation
     * @apiGroup 学生-家长模块
     * @apiParam {long} orgId 机构ID
     * @apiParam {long} id 唯一标识
     * @apiParam {long} parentId 家长ID
     * @apiParam {long} studentId 学生ID
     * @apiParam {String} relationship 关系类型
     * @apiParam {long} createTime 创建时间
     * @apiParam {long} updateTime 更新时间
     * @apiSuccess (Success 200){int} code 200
     */
    public CompletionStage<Result> updateParentStudentRelation(Http.Request request, long id) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ParentStudentRelation originalParentStudentRelation = ParentStudentRelation.find.byId(id);
            ParentStudentRelation newParentStudentRelation = Json.fromJson(jsonNode, ParentStudentRelation.class);
            if (null == originalParentStudentRelation) return okCustomJson(CODE40001, "数据不存在");
            //sass数据校验  
            if (originalParentStudentRelation.orgId != adminMember.getOrgId())
                return okCustomJson(CODE40001, "数据不存在");
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
     * @api {POST} /v2/p/parent_student_relation/   05删除-家长学生关系，以及家长账号
     * @apiName deleteParentStudentRelation
     * @apiGroup 学生-家长模块
     * @apiParam {long} id 关系ID
     * @apiParam {String} operation del时删除
     * @apiSuccess (Success 200){int} 200 成功
     */
    public CompletionStage<Result> deleteParentStudentRelation(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");
            
            try {
                long id = jsonNode.findPath("id").asLong();
                String operation = jsonNode.findPath("operation").asText();
                
                // 参数验证
                if (id <= 0) return okCustomJson(CODE40001, "关系ID不能为空");
                if (!"del".equals(operation)) return okCustomJson(CODE40001, "操作错误");
                
                // 查找关系记录
                ParentStudentRelation deleteModel = ParentStudentRelation.find.byId(id);
                if (null == deleteModel) return okCustomJson(CODE40001, "数据不存在");
                
                // SaaS数据校验
                if (deleteModel.orgId != adminMember.getOrgId()) {
                    return okCustomJson(CODE40001, "数据不存在");
                }
                
                // 保存家长ID，用于后续处理
                long parentId = deleteModel.getParentId();
                
                // 删除关系记录
                deleteModel.delete();
                
                // 检查该家长是否还有其他学生关系
                List<ParentStudentRelation> remainingRelations = ParentStudentRelation.findByParentId(parentId);
                
                // 如果没有其他关系，处理家长账号
                if (remainingRelations == null || remainingRelations.isEmpty()) {
                    handleParentAccountAfterRelationDelete(parentId);
                }
                
                return okJSON200();
            } catch (Exception e) {
                return okCustomJson(CODE40001, "删除家长关系失败：" + e.getMessage());
            }
        });
    }
    
    /**
     * 删除关系后处理家长账号（结合添加逻辑的反向操作）
     * 添加时：如果角色是"科任教师"，会追加",家长"
     * 删除时：如果角色包含"家长"，移除"家长"部分；如果只有"家长"角色，删除账号
     */
    private void handleParentAccountAfterRelationDelete(long parentId) {
        ShopAdmin parent = ShopAdmin.find.byId(parentId);
        if (parent == null) {
            return;
        }
        
        String rules = parent.getRules();
        if (rules == null || rules.trim().isEmpty()) {
            // 如果没有角色，删除账号
            parent.delete();
            return;
        }
        
        rules = rules.trim();
        
        // 如果角色是"家长"，删除账号
        if (rules.equals("家长")) {
            parent.delete();
            return;
        }
        
        // 如果角色包含"家长"，移除"家长"角色（对应添加时的追加逻辑）
        if (rules.contains("家长")) {
            String newRules = rules;
            
            // 移除",家长"（对应添加时的追加格式）
            if (newRules.contains(",家长")) {
                newRules = newRules.replace(",家长", "");
            } 
            // 移除"家长,"（如果"家长"在前面）
            else if (newRules.contains("家长,")) {
                newRules = newRules.replace("家长,", "");
            }
            
            // 清理多余的空格和逗号
            newRules = newRules.trim();
            // 移除开头的逗号
            while (newRules.startsWith(",")) {
                newRules = newRules.substring(1).trim();
            }
            // 移除结尾的逗号
            while (newRules.endsWith(",")) {
                newRules = newRules.substring(0, newRules.length() - 1).trim();
            }
            
            // 如果移除后没有角色了，删除账号
            if (newRules.isEmpty()) {
                parent.delete();
            } else {
                // 更新角色（移除"家长"后保留其他角色，如"科任教师"）
                parent.setRules(newRules);
                parent.update();
            }
        }
    }

    /**
     * @api {POST} /v2/p/parent_student/   06 获取当前家长的孩子信息
     * @apiName parentStudentList
     * @apiGroup 学生-家长模块
     * @apiSuccess (Success 200){int} 200 成功
     * @apiSuccessExample {json} 响应示例:
     * {
     *     "code": 200,
     *     "list": [
     *         {
     *             "orgId": 1,
     *             "id": 1,
     *             "studentNumber": "20250101",
     *             "name": "王欣瑶",
     *             "classId": 1,
     *             "grade": 1,
     *             "classHg": 1,
     *             "evaluationScheme": 0,
     *             "classAverageScore": 0.0,
     *             "academicScore": 0.0,
     *             "specialtyScore": 0.0,
     *             "habitScore": 16.0,
     *             "points": 16.0,
     *             "totalScore": 0.0,
     *             "badges": null,
     *             "rewardRankGrade": 0,
     *             "rewardRankSchool": 0,
     *             "createTime": 1766727983372,
     *             "updateTime": 1766734215190,
     *             "className": "一年级一班",
     *             "pass": false,
     *             "overAverage": false,
     *             "highGrade": false
     *         }
     *     ]
     * }
     */
    public CompletionStage<Result> parentStudentList(Http.Request request) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync(adminMember -> {
            if(adminMember == null) return unauth403();
            //获取该家长的孩子信息
            List<Long> parentStudentIds  = ParentStudentRelation
                    .findByParentId(adminMember.id)
                    .stream()
                    .map(ParentStudentRelation::getStudentId)
                    .toList();

            List<Student> students = Student.find.query()
                    .where()
                    .in("id", parentStudentIds)
                    .findList();

            //在students中加入className这个字段,但是student没有className这个字段，返回一个新的list
            List<Student> studentsWithClassName = students.stream()
                    .peek(student -> {
                        SchoolClass schoolClass = SchoolClass.find.byId(student.classId);
                        student.setClassName(schoolClass != null ? schoolClass.getClassName() : null);
                    }).toList();

            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(studentsWithClassName));
            return ok(result);
        });
    }

    /**
     * @api {POST} /v2/p/parent_student/habit_record/   06 获取当前家长的孩子所有习惯记录
     * @apiName parentStudentList
     * @apiGroup 学生-家长模块
     * @apiSuccess (Success 200){int} 200 成功
     * @apiSuccessExample {json} 响应示例:
     * {
     *     "code": 200,
     *     "list": [
     *         {
     *             "orgId": 1,
     *             "id": 1,
     *             "studentId": 1,
     *             "habitType": 1,
     *             "evaluatorType": "teacher",
     *             "evaluatorId": 5,
     *             "scoreChange": 1.0,
     *             "description": "666666",
     *             "evidenceImage": "",
     *             "recordTime": 1766734204254,
     *             "createTime": 1766734215134,
     *             "monthEndTime": 1767196799999,
     *             "status": 0,
     *             "studentName": "王欣瑶"
     *         }
     *     ]
     * }
     */
    public CompletionStage<Result> parentStudentHabitList(Http.Request request) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync(adminMember -> {
            if(adminMember == null) return unauth403();
            //获取该家长的孩子信息
            List<Long> parentStudentIds  = ParentStudentRelation
                    .findByParentId(adminMember.id)
                    .stream()
                    .map(ParentStudentRelation::getStudentId)
                    .toList();

            List<Student> students = Student.find.query()
                    .where()
                    .in("id", parentStudentIds)
                    .findList();

            List<HabitRecord> habitRecords = HabitRecord.find.query()
                    .where()
                    .in("studentId", parentStudentIds)
                    .findList();

            List<HabitRecord> habitRecordsWithStudentName = habitRecords.stream()
                    .peek(habitRecord -> {
                        Student student = students.stream()
                                .filter(s -> s.id == habitRecord.studentId)
                                .findFirst()
                                .orElse(null);
                        if(student != null) habitRecord.setStudentName(student.name);
                        else habitRecord.setStudentName(null);
                    }).toList();

            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(habitRecordsWithStudentName));
            return ok(result);
        });
    }

    /**
     * @api {POST} /v2/p/parent_student/award_record/   06 获取当前家长的孩子所有奖项记录
     * @apiName parentStudentList
     * @apiGroup 学生-家长模块
     * @apiSuccess (Success 200){int} 200 成功
     * @apiSuccessExample {json} 响应示例:
     * {
     *     "code": 200,
     *     "list": [
     *         {
     *             "orgId": 1,
     *             "id": 1,
     *             "studentId": 1,
     *             "student": null,
     *             "awardLevel": 0,
     *             "awardGrade": 0,
     *             "competitionName": "四百米跑步",
     *             "category": "个人",
     *             "awardScore": 20.0,
     *             "status": 0,   // 0 待审核 1 审核通过 2 审核未通过
     *             "certificateImage": "",
     *             "badgeAwarded": "",
     *             "awardDate": 1764518400000,
     *             "createTime": 1766735064543,
     *             "updateTime": 1766735064543,
     *             "studentName": "王欣瑶"
     *         }
     *     ]
     * }
     */
    public CompletionStage<Result> parentStudentAwardList(Http.Request request) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync(adminMember -> {
            if(adminMember == null) return unauth403();
            //获取该家长的孩子信息
            List<Long> parentStudentIds  = ParentStudentRelation
                    .findByParentId(adminMember.id)
                    .stream()
                    .map(ParentStudentRelation::getStudentId)
                    .toList();

            List<Student> students = Student.find.query()
                    .where()
                    .in("id", parentStudentIds)
                    .findList();

            List<SpecialtyAward> specialtyAwards = SpecialtyAward.find.query()
                    .where()
                    .in("studentId", parentStudentIds)
                    .findList();

            List<SpecialtyAward> specialtyAwardsWithStudentName = specialtyAwards.stream()
                    .peek(specialtyAward -> {
                        Student student = students.stream()
                                .filter(s -> s.id == specialtyAward.studentId)
                                .findFirst()
                                .orElse(null);
                        specialtyAward.studentName = student != null ? student.name : null;
                    }).toList();

            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(specialtyAwardsWithStudentName));
            return ok(result);
        });
    }

}
