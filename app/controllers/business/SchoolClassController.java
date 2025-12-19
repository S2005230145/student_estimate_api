package controllers.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import controllers.BaseSecurityController;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.admin.ShopAdmin;
import models.business.ClassTeacherRelation;
import models.business.SchoolClass;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import utils.ValidationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class SchoolClassController extends BaseSecurityController {

    /**
     * @api {GET} /v2/p/school_class_list/   01列表-班级信息
     * @apiName listSchoolClass
     * @apiGroup SCHOOL-CLASS-CONTROLLER
     * @apiParam {int} page 页码
     * @apiParam {String} filter 搜索栏()
     * @apiSuccess (Success 200) {long} orgId 机构ID
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {String} className 班级名称
     * @apiSuccess (Success 200) {int} grade 年级
     * @apiSuccess (Success 200) {long} headTeacherId 班主任ID
     * @apiSuccess (Success 200) {ShopAdmin} headTeacher
     * @apiSuccess (Success 200) {int} studentNum 人数
     * @apiSuccess (Success 200) {double} academicScore 学业得分总分
     * @apiSuccess (Success 200) {double} specialtyScore 特长得分总分
     * @apiSuccess (Success 200) {double} routineScore 常规得分
     * @apiSuccess (Success 200) {double} homeVisitScore 家访得分
     * @apiSuccess (Success 200) {double} totalScore 总分
     * @apiSuccess (Success 200) {boolean} disqualified 一票否决
     * @apiSuccess (Success 200) {double} deductionScore 扣分
     * @apiSuccess (Success 200) {String} honorTitle 荣誉称号
     * @apiSuccess (Success 200) {long} createTime 创建时间
     * @apiSuccess (Success 200) {ShopAdmin} teachers
     */
    public CompletionStage<Result> listSchoolClass(Http.Request request, int page, String filter, int status) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ExpressionList<SchoolClass> expressionList = SchoolClass.find.query().where().eq("org_id", adminMember.getOrgId());
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
            result.put("list", Json.toJson(list));
            return ok(result);
        });

    }

    /**
     * @api {GET} /v2/p/school_class/:id/  02详情-SchoolClass班级信息
     * @apiName getSchoolClass
     * @apiGroup SCHOOL-CLASS-CONTROLLER
     * @apiParam {long} id id
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {long} orgId 机构ID
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {String} className 班级名称
     * @apiSuccess (Success 200) {int} grade 年级
     * @apiSuccess (Success 200) {long} headTeacherId 班主任ID
     * @apiSuccess (Success 200) {ShopAdmin} headTeacher
     * @apiSuccess (Success 200) {int} studentNum 人数
     * @apiSuccess (Success 200) {double} academicScore 学业得分总分
     * @apiSuccess (Success 200) {double} specialtyScore 特长得分总分
     * @apiSuccess (Success 200) {double} routineScore 常规得分
     * @apiSuccess (Success 200) {double} homeVisitScore 家访得分
     * @apiSuccess (Success 200) {double} totalScore 总分
     * @apiSuccess (Success 200) {boolean} disqualified 一票否决
     * @apiSuccess (Success 200) {double} deductionScore 扣分
     * @apiSuccess (Success 200) {String} honorTitle 荣誉称号
     * @apiSuccess (Success 200) {long} createTime 创建时间
     * @apiSuccess (Success 200) {ShopAdmin} teachers
     */
    public CompletionStage<Result> getSchoolClass(Http.Request request, long id) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            SchoolClass schoolClass = SchoolClass.find.byId(id);
            if (null == schoolClass) return okCustomJson(CODE40001, "数据不存在");
            //sass数据校验  
            if (schoolClass.orgId > adminMember.getOrgId()) return okCustomJson(CODE40001, "数据不存在");
            ObjectNode result = (ObjectNode) Json.toJson(schoolClass);
            result.put(CODE, CODE200);
            return ok(result);
        });

    }

    /**
     * @api {POST} /v2/p/school_class/new/   01添加-SchoolClass班级信息
     * @apiName addSchoolClass
     * @apiDescription 描述
     * @apiGroup SCHOOL-CLASS-CONTROLLER
     * @apiParam {long} orgId 机构ID
     * @apiParam {long} id 唯一标识
     * @apiParam {String} className 班级名称
     * @apiParam {ShopAdmin} headTeacher
     * @apiParam {int} studentNum 人数
     * @apiParam {double} academicScore 学业得分总分
     * @apiParam {double} specialtyScore 特长得分总分
     * @apiParam {double} routineScore 常规得分
     * @apiParam {double} homeVisitScore 家访得分
     * @apiParam {double} totalScore 总分
     * @apiParam {boolean} disqualified 一票否决
     * @apiParam {double} deductionScore 扣分
     * @apiParam {String} honorTitle 荣誉称号
     * @apiParam {long} createTime 创建时间
     * @apiParam {ShopAdmin} teachers
     * @apiSuccess (Success 200){int} code 200
     */

    public CompletionStage<Result> addSchoolClass(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((admin) -> {
            if (null == admin) return unauth403();
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");
            SchoolClass schoolClass = Json.fromJson(jsonNode, SchoolClass.class);
            //解析SchoolClass的className,拆分出年级grade和班级class,比如说一年级一班 ，用contains
            String className = schoolClass.getClassName();
            if (className.contains("一年级")) schoolClass.grade = 1;
            if (className.contains("二年级")) schoolClass.grade =2 ;
            if (className.contains("三年级")) schoolClass.grade = 3;
            if (className.contains("四年级")) schoolClass.grade = 4;
            if (className.contains("五年级")) schoolClass.grade = 5;
            if (className.contains("六年级")) schoolClass.grade = 6;
            if (className.contains("一班")) schoolClass.classId = 1;
            if (className.contains("二班")) schoolClass.classId = 2;
            if (className.contains("三班")) schoolClass.classId = 3;
            if (className.contains("四班")) schoolClass.classId = 4;
// 数据sass化
            schoolClass.setOrgId(admin.getOrgId());
            long currentTimeBySecond = dateUtils.getCurrentTimeByMilliSecond();
            schoolClass.setCreateTime(currentTimeBySecond);
            schoolClass.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/school_class/:id/  04更新-SchoolClass班级信息
     * @apiName updateSchoolClass
     * @apiGroup SCHOOL-CLASS-CONTROLLER
     * @apiParam {long} orgId 机构ID
     * @apiParam {long} id 唯一标识
     * @apiParam {String} className 班级名称
     * @apiParam {int} grade 年级
     * @apiParam {long} headTeacherId 班主任ID
     * @apiParam {ShopAdmin} headTeacher
     * @apiParam {int} studentNum 人数
     * @apiParam {double} academicScore 学业得分总分
     * @apiParam {double} specialtyScore 特长得分总分
     * @apiParam {double} routineScore 常规得分
     * @apiParam {double} homeVisitScore 家访得分
     * @apiParam {double} totalScore 总分
     * @apiParam {boolean} disqualified 一票否决
     * @apiParam {double} deductionScore 扣分
     * @apiParam {String} honorTitle 荣誉称号
     * @apiParam {long} createTime 创建时间
     * @apiParam {ShopAdmin} teachers
     * @apiSuccess (Success 200){int} code 200
     */
    public CompletionStage<Result> updateSchoolClass(Http.Request request, long id) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            SchoolClass originalSchoolClass = SchoolClass.find.byId(id);
            SchoolClass newSchoolClass = Json.fromJson(jsonNode, SchoolClass.class);
            if (null == originalSchoolClass) return okCustomJson(CODE40001, "数据不存在");
            //sass数据校验  
            if (originalSchoolClass.orgId != adminMember.getOrgId()) return okCustomJson(CODE40001, "数据不存在");
            if (!ValidationUtil.isEmpty(newSchoolClass.className))
                originalSchoolClass.setClassName(newSchoolClass.className);
            if (newSchoolClass.grade > 0) originalSchoolClass.setGrade(newSchoolClass.grade);
            if (newSchoolClass.headTeacherId > 0) originalSchoolClass.setHeadTeacherId(newSchoolClass.headTeacherId);
            //字段<< headTeacher >>非基础类型,请自行编写更新语句
            if (newSchoolClass.studentNum > 0) originalSchoolClass.setStudentNum(newSchoolClass.studentNum);
            if (newSchoolClass.academicScore > 0) originalSchoolClass.setAcademicScore(newSchoolClass.academicScore);
            if (newSchoolClass.specialtyScore > 0) originalSchoolClass.setSpecialtyScore(newSchoolClass.specialtyScore);
            if (newSchoolClass.routineScore > 0) originalSchoolClass.setRoutineScore(newSchoolClass.routineScore);
            if (newSchoolClass.homeVisitScore > 0) originalSchoolClass.setHomeVisitScore(newSchoolClass.homeVisitScore);
            if (newSchoolClass.totalScore > 0) originalSchoolClass.setTotalScore(newSchoolClass.totalScore);
            if (newSchoolClass.disqualified != originalSchoolClass.disqualified)
                originalSchoolClass.setDisqualified(newSchoolClass.disqualified);
            if (newSchoolClass.deductionScore > 0) originalSchoolClass.setDeductionScore(newSchoolClass.deductionScore);
            if (!ValidationUtil.isEmpty(newSchoolClass.honorTitle))
                originalSchoolClass.setHonorTitle(newSchoolClass.honorTitle);
            //字段<< teachers >>非基础类型,请自行编写更新语句

            originalSchoolClass.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/school_class/   05删除-班级信息
     * @apiName deleteSchoolClass
     * @apiGroup SCHOOL-CLASS-CONTROLLER
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
            //sass数据校验  
            if (deleteModel.orgId != adminMember.getOrgId()) return okCustomJson(CODE40001, "数据不存在");
            deleteModel.delete();
            return okJSON200();
        });
    }


    /**
     * @api {POST} /v2/p/school_class/:id/set_head_teacher/   06设置班主任
     * @apiName setHeadTeacher
     * @apiGroup SCHOOL-CLASS-CONTROLLER
     * @apiParam {long} id 班级ID
     * @apiParam {String} subject 任教科目（可选）
     * @apiParam {long} teacherId 教师ID
     * @apiSuccess (Success 200){int} 200 成功
     */
    public CompletionStage<Result> setHeadTeacher(Http.Request request, long id) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");

            try {
                long teacherId = jsonNode.findPath("teacherId").asLong();
                String subject = jsonNode.findPath("subject").asText();

                // 参数验证
                if (teacherId <= 0) return okCustomJson(CODE40001, "教师ID不能为空");

                // 验证班级是否存在
                SchoolClass schoolClass = SchoolClass.find.byId(id);
                if (schoolClass == null) return okCustomJson(CODE40001, "班级不存在");

                // 验证教师是否存在
                ShopAdmin teacher = ShopAdmin.find.byId(teacherId);
                if (teacher == null) return okCustomJson(CODE40001, "教师不存在");

                // 设置班主任
                boolean success = setClassHeadTeacher(id, teacherId, subject);
                if (!success) return okCustomJson(CODE40001, "设置班主任失败");

                return okJSON200();

            } catch (Exception e) {
                return okCustomJson(CODE40001, "设置班主任失败：" + e.getMessage());
            }
        });
    }

    /**
     * @api {POST} /v2/p/school_class/:id/add_teacher/   07添加科任教师
     * @apiName addClassTeacher
     * @apiGroup SCHOOL-CLASS-CONTROLLER
     * @apiParam {long} id 班级ID
     * @apiParam {long} teacherId 教师ID
     * @apiParam {String} subject 任教科目
     * @apiParam {int} teachingHours 周课时数（可选）
     * @apiParam {String} responsibility 职责描述（可选）
     * @apiSuccess (Success 200){int} 200 成功
     */
    public CompletionStage<Result> addClassTeacher(Http.Request request, long id) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");

            try {
                long teacherId = jsonNode.findPath("teacherId").asLong();
                String subject = jsonNode.findPath("subject").asText();
                int teachingHours = jsonNode.findPath("teachingHours").asInt();
                String responsibility = jsonNode.findPath("responsibility").asText();

                // 参数验证
                if (teacherId <= 0) return okCustomJson(CODE40001, "教师ID不能为空");
                if (ValidationUtil.isEmpty(subject)) return okCustomJson(CODE40001, "任教科目不能为空");

                // 验证班级是否存在
                SchoolClass schoolClass = SchoolClass.find.byId(id);
                if (schoolClass == null) return okCustomJson(CODE40001, "班级不存在");
                if (ValidationUtil.isEmpty(subject)) return okCustomJson(CODE40001, "任教科目不能为空");

                // 验证教师是否存在
                ShopAdmin teacher = ShopAdmin.find.byId(teacherId);
                if (teacher == null) return okCustomJson(CODE40001, "教师不存在");

                // 检查是否已经是该班级的教师
                boolean isAlreadyTeacher = ClassTeacherRelation.isTeacherInClass(teacherId, id);
                if (isAlreadyTeacher) return okCustomJson(CODE40001, "该教师已经是本班教师");

                // 添加科任教师
                addTeacherToClass(id, teacherId, subject, teachingHours, responsibility, false);

                return okJSON200();

            } catch (Exception e) {
                return okCustomJson(CODE40001, "添加教师失败：" + e.getMessage());
            }
        });
    }

    /**
     * @api {POST} /v2/p/school_class/:id/remove_teacher/   08移除班级教师
     * @apiName removeClassTeacher
     * @apiGroup SCHOOL-CLASS-CONTROLLER
     * @apiParam {long} id 班级ID
     * @apiParam {long} teacherId 教师ID
     * @apiSuccess (Success 200){int} 200 成功
     */
    public CompletionStage<Result> removeClassTeacher(Http.Request request, long id) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");

            try {
                long teacherId = jsonNode.findPath("teacherId").asLong();

                // 参数验证
                if (teacherId <= 0) return okCustomJson(CODE40001, "教师ID不能为空");

                // 验证班级是否存在
                SchoolClass schoolClass = SchoolClass.find.byId(id);
                if (schoolClass == null) return okCustomJson(CODE40001, "班级不存在");

                // 移除教师
                removeTeacherFromClass(id, teacherId);
                return okJSON200();

            } catch (Exception e) {
                return okCustomJson(CODE40001, "移除教师失败：" + e.getMessage());
            }
        });
    }

    /**
     * @api {GET} /v2/p/school_class/:id/teachers/   09获取班级教师列表
     * @apiName getClassTeachers
     * @apiGroup SCHOOL-CLASS-CONTROLLER
     * @apiParam {long} id 班级ID
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {Array} teachers 教师列表
     * @apiSuccess (Success 200) {Object} headTeacher 班主任信息
     */
    public CompletionStage<Result> getClassTeachers(Http.Request request, long id) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();

            try {
                // 验证班级是否存在
                SchoolClass schoolClass = SchoolClass.find.byId(id);
                if (schoolClass == null) return okCustomJson(CODE40001, "班级不存在");

                // 获取班级所有教师关系
                List<ClassTeacherRelation> teacherRelations = ClassTeacherRelation.findByClassId(id);
                List<ObjectNode> teacherList = new ArrayList<>();
                ObjectNode headTeacher = null;

                for (ClassTeacherRelation relation : teacherRelations) {
                    ShopAdmin teacher = ShopAdmin.find.byId(relation.getTeacherId());
                    if (teacher != null) {
                        ObjectNode teacherNode = Json.newObject();
                        teacherNode.put("relationId", relation.getId());
                        teacherNode.put("teacherId", teacher.getId());
                        teacherNode.put("teacherName", teacher.getRealName());
                        teacherNode.put("phone", teacher.getPhoneNumber());
                        teacherNode.put("subject", relation.getSubject());
                        teacherNode.put("isHeadTeacher", relation.isHeadTeacher());
                        teacherNode.put("createTime", relation.getCreateTime());

                        if (relation.isHeadTeacher()) {
                            headTeacher = teacherNode;
                        } else {
                            teacherList.add(teacherNode);
                        }
                    }
                }

                ObjectNode result = Json.newObject();
                result.put(CODE, CODE200);
                if (headTeacher != null) {
                    result.set("headTeacher", headTeacher);
                }
                result.set("subjectTeachers", Json.toJson(teacherList));
                return ok(result);

            } catch (Exception e) {
                return okCustomJson(CODE40001, "获取教师列表失败：" + e.getMessage());
            }
        });
    }

    /**
     * @api {GET} /v2/p/school_class/:id/head_teacher/   10获取班主任信息
     * @apiName getHeadTeacher
     * @apiGroup SCHOOL-CLASS-CONTROLLER
     * @apiParam {long} id 班级ID
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {Object} headTeacher 班主任信息
     */
    public CompletionStage<Result> getHeadTeacher(Http.Request request, long id) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();

            try {
                // 验证班级是否存在
                SchoolClass schoolClass = SchoolClass.find.byId(id);
                if (schoolClass == null) return okCustomJson(CODE40001, "班级不存在");

                // 获取班主任关系
                ClassTeacherRelation headTeacherRelation = ClassTeacherRelation.findHeadTeacherByClassId(id);
                if (headTeacherRelation == null) {
                    return okCustomJson(CODE40001, "该班级暂无班主任");
                }

                ShopAdmin headTeacher = ShopAdmin.find.byId(headTeacherRelation.getTeacherId());
                if (headTeacher == null) {
                    return okCustomJson(CODE40001, "班主任信息不存在");
                }

                ObjectNode result = Json.newObject();
                result.put(CODE, CODE200);
                result.put("teacherId", headTeacher.getId());
                result.put("teacherName", headTeacher.getRealName());
                result.put("phone", headTeacher.getPhoneNumber());
                result.put("subject", headTeacherRelation.getSubject());
                result.put("createTime", headTeacherRelation.getCreateTime());

                return ok(result);

            } catch (Exception e) {
                return okCustomJson(CODE40001, "获取班主任信息失败：" + e.getMessage());
            }
        });
    }
    /**
     * 设置班主任
     */
    private boolean setClassHeadTeacher(long classId, long teacherId, String subject) {
        try {
            // 先取消现有的班主任
            ClassTeacherRelation existingHeadTeacher = ClassTeacherRelation.findHeadTeacherByClassId(classId);
            if (existingHeadTeacher != null) {
                existingHeadTeacher.setHeadTeacher(false);
                existingHeadTeacher.setUpdateTime(dateUtils.getCurrentTimeByMilliSecond());
                existingHeadTeacher.update();
            }

            // 检查该教师是否已经是科任教师
            ClassTeacherRelation existingRelation = ClassTeacherRelation.find.query()
                    .where()
                    .eq("class_id", classId)
                    .eq("teacher_id", teacherId)
                    .findOne();

            if (existingRelation != null) {
                // 如果是科任教师，升级为班主任
                existingRelation.setHeadTeacher(true);
                if (!ValidationUtil.isEmpty(subject)) {
                    existingRelation.setSubject(subject);
                }
                existingRelation.setUpdateTime(dateUtils.getCurrentTimeByMilliSecond());
                existingRelation.update();
            } else {
                // 创建新的班主任关系
                addTeacherToClass(classId, teacherId,
                        ValidationUtil.isEmpty(subject) ? "班主任" : subject,
                        0, "班主任", true);
            }

            // 更新班级的班主任ID
            SchoolClass schoolClass = SchoolClass.find.byId(classId);
            if (schoolClass != null) {
                schoolClass.setHeadTeacherId(teacherId);
                schoolClass.update();
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 添加教师到班级
     */
    private void addTeacherToClass(long classId, long teacherId, String subject,
                                   int teachingHours, String responsibility, boolean isHeadTeacher) {
        ClassTeacherRelation relation = new ClassTeacherRelation();
        relation.setClassId(classId);
        relation.setTeacherId(teacherId);
        relation.setSubject(subject);
        relation.setHeadTeacher(isHeadTeacher);
        relation.setCreateTime(dateUtils.getCurrentTimeByMilliSecond());
        relation.setUpdateTime(dateUtils.getCurrentTimeByMilliSecond());
        relation.save();

    }

    /**
     * 移除班级教师
     */
    private void removeTeacherFromClass(long classId, long teacherId) {
        ClassTeacherRelation relation = ClassTeacherRelation.find.query()
                .where()
                .eq("class_id", classId)
                .eq("teacher_id", teacherId)
                .findOne();

        if (relation != null) {
            // 如果是班主任，需要清除班级的班主任ID
            if (relation.isHeadTeacher()) {
                SchoolClass schoolClass = SchoolClass.find.byId(classId);
                if (schoolClass != null) {
                    schoolClass.setHeadTeacherId(0L);
                    schoolClass.update();
                }
            }


            relation.delete();


        }
    }

    /**
     * 更新教师信息
     */
    private void updateTeacherInfo(long classId, long teacherId, String subject) {
        ClassTeacherRelation relation = ClassTeacherRelation.find.query()
                .where()
                .eq("class_id", classId)
                .eq("teacher_id", teacherId)
                .findOne();

        if (relation != null) {
            if (!ValidationUtil.isEmpty(subject)) {
                relation.setSubject(subject);
            }
            relation.setUpdateTime(dateUtils.getCurrentTimeByMilliSecond());
            relation.update();
        }
    }


    /**
     * 获取当前学年
     */
    private String getCurrentAcademicYear() {
        // 根据业务逻辑实现，这里返回示例
        return "2024-2025";
    }

    /**
     * 获取当前学期
     */
    private int getCurrentSemester() {
        // 根据业务逻辑实现，这里返回示例
        return 1; // 1-上学期, 2-下学期
    }
}

