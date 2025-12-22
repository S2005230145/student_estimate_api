package controllers.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import controllers.BaseSecurityController;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.admin.ShopAdmin;
import models.business.ClassTeacherRelation;
import models.business.ParentStudentRelation;
import models.business.SchoolClass;
import models.business.Student;
import models.excel.StudentImportExcel;
import org.apache.commons.io.FilenameUtils;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.ebean.Transactional;
import play.libs.Files;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import utils.EncodeUtils;
import utils.ValidationUtil;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class StudentController extends BaseSecurityController {
    @Inject
    FormFactory formFactory;
    @Inject
    EncodeUtils encodeUtils;
    /**
     * @api {POST} /v2/p/student_list/   01列表-学生
     * @apiName listStudent
     * @apiGroup STUDENT-CONTROLLER
     * @apiParam {int} page 页码
     * @apiParam {String} studentName 学生姓名
     * @apiSuccess (Success 200) {long} orgId 机构ID
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
    public CompletionStage<Result> listStudent(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();

            int page = jsonNode.get("page").asInt();

            // 修复：检查studentName节点是否存在
            String studentName = jsonNode.has("studentName") && !jsonNode.get("studentName").isNull()
                    ? jsonNode.get("studentName").asText()
                    : null;

            ExpressionList<Student> expressionList = Student.find.query().where().eq("org_id", adminMember.getOrgId());
            //if (status > 0) expressionList.eq("status", status);
            //编写其他条件
            //编写其他条件
            if(!ValidationUtil.isEmpty(studentName)) expressionList
                    .or()
                    .icontains("name", studentName)
                    .endOr();


            //编写其他条件
            //编写其他条件
            if (!adminMember.getRules().contains("教导处")){
                if (adminMember.getRules().contains("科任教师")||adminMember.getRules().contains("班主任")) {
                    List<ClassTeacherRelation> classTeacherRelations = ClassTeacherRelation.findByTeacherId(adminMember.getId());
                    List<Long> classIds = classTeacherRelations.stream().map(ClassTeacherRelation::getClassId).toList();
                    expressionList.in("classId", classIds);
                }
            }
            if (adminMember.getRules().contains("家长")){
                List<ParentStudentRelation> parentStudentRelations = ParentStudentRelation.findByParentId(adminMember.getId());
                List<Long> studentIds = parentStudentRelations.stream().map(ParentStudentRelation::getStudentId).toList();
                expressionList.in("id",studentIds);
            }

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
     * @api {GET} /v2/p/student/:id/  02详情-Student学生
     * @apiName getStudent
     * @apiGroup STUDENT-CONTROLLER
     * @apiParam {long} id id
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {long} orgId 机构ID
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
            //sass数据校验
            if (student.orgId != adminMember.getOrgId()) return okCustomJson(CODE40001, "数据不存在");
            ObjectNode result = (ObjectNode) Json.toJson(student);
            result.put(CODE, CODE200);
            return ok(result);
        });

    }

    /**
     * @api {POST} /v2/p/student/new/   01添加-Student学生
     * @apiName addStudent
     * @apiDescription 描述
     * @apiGroup STUDENT-CONTROLLER
     * @apiParam {long} orgId 机构ID
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
// 数据sass化
            student.setOrgId(0);
            long currentTimeBySecond = dateUtils.getCurrentTimeByMilliSecond();
            student.setCreateTime(currentTimeBySecond);
            student.setUpdateTime(currentTimeBySecond);
            student.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/student/:id/  04更新-Student学生
     * @apiName updateStudent
     * @apiGroup STUDENT-CONTROLLER
     * @apiParam {long} orgId 机构ID
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
            //sass数据校验
            if (originalStudent.orgId != adminMember.getOrgId()) return okCustomJson(CODE40001, "数据不存在");
            if (!ValidationUtil.isEmpty(newStudent.studentNumber))
                originalStudent.setStudentNumber(newStudent.studentNumber);
            if (!ValidationUtil.isEmpty(newStudent.name)) originalStudent.setName(newStudent.name);
            if (newStudent.classId > 0) originalStudent.setClassId(newStudent.classId);
            if (newStudent.grade > 0) originalStudent.setGrade(newStudent.grade);
            if (newStudent.evaluationScheme > 0) {
                if (newStudent.evaluationScheme==Student.SCHEME_B) {
//                    if (!newStudent.isOverAverage()) {
//                        return okCustomJson(CODE40001, "学业成绩未达到班级平均分，不能选择方案B");
//                    }
                    if(!newStudent.isPass()){
                        return okCustomJson(CODE40001, "学业成绩未达到及格分20分，不能选择方案B");
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
     * @api {POST} /v2/p/student/   05删除-学生
     * @apiName deleteStudent
     * @apiGroup STUDENT-CONTROLLER
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
            //sass数据校验
            if (deleteModel.orgId > adminMember.getOrgId()) return okCustomJson(CODE40001, "数据不存在");
            deleteModel.delete();
            return okJSON200();
        });
    }
    /**
     * @api {POST} /v2/p/student_excel/   06导入学生文件(按班的)
     * @apiName studentImport
     * @apiGroup STUDENT-CONTROLLER
     * @apiParam {file} file 学生文件
     * @apiParam {long} classId 班级ID
     * @apiSuccess (Success 200){int} 200 成功
     */
    public CompletionStage<Result> studentImport(Http.Request request,Long classId) {

            Http.MultipartFormData<Files.TemporaryFile> body = request.body().asMultipartFormData();
            Http.MultipartFormData.FilePart<Files.TemporaryFile> filePart = body.getFile("file");

            // 获取班级ID参数
            DynamicForm form = formFactory.form().bindFromRequest(request);
            //long classId = Long.parseLong(form.get("classId"));

            if (filePart == null) {
                return CompletableFuture.completedFuture(okCustomJson(CODE40001, "文件不能为空"));
            }

        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync(adminMember -> {
            if (adminMember == null) return unauth403();

            if(adminMember.orgId < 1){
                throw new RuntimeException("机构ID不能为空");
            }

                Files.TemporaryFile file = filePart.getRef();
                String fileName = filePart.getFilename();
                String targetFileName = UUID.randomUUID() + "." + FilenameUtils.getExtension(fileName);
                String destPath = FILE_DIR_LOCATION + targetFileName;

                // 确保目录存在
                new File(FILE_DIR_LOCATION).mkdirs();

                file.copyTo(Paths.get(destPath), true);
                File destFile = new File(destPath);

                try (InputStream inputStream = new FileInputStream(destFile)) {
                    // 读取Excel文件
                    List<StudentImportExcel> list = StudentImportExcel.importFromExcel(inputStream);

                    // 使用事务，导入过程中任意一步出错则整体回滚
                    try (io.ebean.Transaction txn = io.ebean.DB.beginTransaction()) {
                        // 数据验证
                        StudentImportExcel.validateData(list, classId);

                        // 转换为实体并保存
                        StudentImportExcel.toEntity(list, classId, adminMember.getOrgId());

                        // 一切成功后提交事务
                        txn.commit();
                    }

                    return okJSON200();
                } catch (Exception e) {
                    // 发生异常时事务未提交，Ebean 会自动回滚
                    return okCustomJson(CODE40001, "导入失败：" + e.getMessage());
                }
            });
    }

    /**
     * @api {GET} /v2/p/student_excel_template/ 07导出学生导入模板
     * @apiName exportStudentTemplate
     * @apiGroup STUDENT-CONTROLLER
     * @apiSuccess (Success 200){file} Excel文件 导入模板文件
     */
    public CompletionStage<Result> exportStudentTemplate(Http.Request request) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync(adminMember -> {
            if (adminMember == null) return unauth403();

            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                // 导出空模板
                StudentImportExcel.exportToExcel(outputStream, new ArrayList<>());
                byte[] bytes = outputStream.toByteArray();

                return ok(bytes)
                        .withHeader("Content-Disposition", "attachment; filename=student_import_template.xlsx")
                        .as("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            } catch (Exception e) {
                return okCustomJson(CODE40001, "导出模板失败：" + e.getMessage());
            }
        });
    }

    /**
     * @api {POST} /v2/p/student_parent/   08创建学生家长关系
     * @apiName createStudentParent
     * @apiGroup STUDENT-CONTROLLER
     * @apiParam {long} studentId 学生ID
     * @apiParam {String} parentName 家长姓名
     * @apiParam {String} parentPhone 家长手机号
     * @apiParam {String} relationship 关系类型（父亲/母亲/爷爷/奶奶等）
     * @apiSuccess (Success 200){int} 200 成功
     */
    public CompletionStage<Result> createStudentParent(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");

            try {
                long studentId = jsonNode.findPath("studentId").asLong();
                String parentName = jsonNode.findPath("parentName").asText();
                String parentPhone = jsonNode.findPath("parentPhone").asText();
                String relationship = jsonNode.findPath("relationship").asText();

                // 参数验证
                if (studentId <= 0) return okCustomJson(CODE40001, "学生ID不能为空");
                if (ValidationUtil.isEmpty(parentName)) return okCustomJson(CODE40001, "家长姓名不能为空");
                if (ValidationUtil.isEmpty(parentPhone)) return okCustomJson(CODE40001, "家长手机号不能为空");
                if (ValidationUtil.isEmpty(relationship)) return okCustomJson(CODE40001, "关系类型不能为空");

                // 验证学生是否存在
                Student student = Student.find.byId(studentId);
                if (student == null) return okCustomJson(CODE40001, "学生不存在");

                // 验证手机号格式
                if (!parentPhone.matches("^1[3-9]\\d{9}$")) {
                    return okCustomJson(CODE40001, "手机号格式不正确");
                }

                // 查找或创建家长账号
                ShopAdmin parent = findOrCreateParent(parentName, parentPhone, relationship, student.getName());
                if (parent == null) {
                    return okCustomJson(CODE40001, "家长账号创建失败");
                }

                // 创建家长学生关系
                ParentStudentRelation.addRelation(parent.getId(), studentId, relationship,adminMember.orgId);

                return okJSON200();

            } catch (Exception e) {
                return okCustomJson(CODE40001, "创建家长关系失败：" + e.getMessage());
            }
        });
    }

    /**
     * @api {GET} /v2/p/student/:id/parents/   09获取学生家长列表
     * @apiName getStudentParents
     * @apiGroup STUDENT-CONTROLLER
     * @apiParam {long} id 学生ID
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {Array} parents 家长列表
     */
    public CompletionStage<Result> getStudentParents(Http.Request request, long id) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();

            try {
                // 验证学生是否存在
                Student student = Student.find.byId(id);
                if (student == null) return okCustomJson(CODE40001, "学生不存在");

                // 获取家长关系列表
                List<ParentStudentRelation> parentRelations = ParentStudentRelation.findByStudentId(id);
                List<ObjectNode> parentList = new ArrayList<>();

                for (ParentStudentRelation relation : parentRelations) {
                    ShopAdmin parent = ShopAdmin.find.byId(relation.getParentId());
                    if (parent != null) {
                        ObjectNode parentNode = Json.newObject();
                        parentNode.put("id", parent.getId());
                        parentNode.put("name", parent.getRealName());
                        parentNode.put("phone", parent.getPhoneNumber());
                        parentNode.put("relationship", relation.getRelationship());
                        parentNode.put("createTime", relation.getCreateTime());
                        parentList.add(parentNode);
                    }
                }

                ObjectNode result = Json.newObject();
                result.put(CODE, CODE200);
                result.set("parents", Json.toJson(parentList));
                return ok(result);

            } catch (Exception e) {
                return okCustomJson(CODE40001, "获取家长列表失败：" + e.getMessage());
            }
        });
    }

    /**
     * @api {POST} /v2/p/student_parent/:id/   10删除学生家长关系
     * @apiName deleteStudentParent
     * @apiGroup STUDENT-CONTROLLER
     * @apiParam {long} id 家长关系ID
     * @apiParam {String} operation del时删除
     * @apiSuccess (Success 200){int} 200 成功
     */
    public CompletionStage<Result> deleteStudentParent(Http.Request request, long id) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();

            try {
                String operation = jsonNode.findPath("operation").asText();
                if (!"del".equals(operation)) return okCustomJson(CODE40001, "操作错误");

                ParentStudentRelation relation = ParentStudentRelation.find.byId(id);
                if (relation == null) return okCustomJson(CODE40001, "家长关系不存在");

                relation.delete();
                return okJSON200();

            } catch (Exception e) {
                return okCustomJson(CODE40001, "删除家长关系失败：" + e.getMessage());
            }
        });
    }

    /**
     * @api {POST} /v2/p/student_batch_assign/   11批量分配学生到班级
     * @apiName batchAssignStudents
     * @apiGroup STUDENT-CONTROLLER
     * @apiParam {Array} studentIds 学生ID数组
     * @apiParam {long} classId 目标班级ID
     * @apiSuccess (Success 200){int} 200 成功
     */
    public CompletionStage<Result> batchAssignStudents(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");

            try {
                JsonNode studentIdsNode = jsonNode.findPath("studentIds");
                long classId = jsonNode.findPath("classId").asLong();

                // 参数验证
                if (studentIdsNode == null || !studentIdsNode.isArray() || studentIdsNode.size() == 0) {
                    return okCustomJson(CODE40001, "学生ID列表不能为空");
                }
                if (classId <= 0) return okCustomJson(CODE40001, "班级ID不能为空");

                // 验证班级是否存在
                SchoolClass targetClass = SchoolClass.find.byId(classId);
                if (targetClass == null) return okCustomJson(CODE40001, "目标班级不存在");

                List<Long> studentIds = new ArrayList<>();
                for (JsonNode idNode : studentIdsNode) {
                    studentIds.add(idNode.asLong());
                }

                // 批量分配学生到班级
                int successCount = batchAssignStudentsToClass(studentIds, classId);

                ObjectNode result = Json.newObject();
                result.put(CODE, CODE200);
                result.put("message", "成功分配 " + successCount + " 个学生到班级");
                result.put("successCount", successCount);
                result.put("totalCount", studentIds.size());
                return ok(result);

            } catch (Exception e) {
                return okCustomJson(CODE40001, "批量分配失败：" + e.getMessage());
            }
        });
    }


    /**
     * @api {GET} /v2/p/student_list_class_currentUser/   12列表-当前用户的所在班级的学生列表
     * @apiName listStudent
     * @apiGroup STUDENT-CONTROLLER
     * @apiParam {int} page 页码
     * @apiSuccess (Success 200) {long} orgId 机构ID
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
    public CompletionStage<Result> listStudentClassCurrentUser (Http.Request request, long classId, int status) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ExpressionList<Student> expressionList = Student.find.query().where().eq("org_id", adminMember.getOrgId());
            if (status > 0) expressionList.eq("status", status);
            SchoolClass schoolClass = SchoolClass.find.byId(classId);

            return null ;
        });

    }


    /**
     * @api {POST} /v2/p/student_excel_school/   13导入学生文件(全校)
     * @apiName studentImport
     * @apiGroup STUDENT-CONTROLLER
     * @apiParam {file} file 学生文件
     * @apiSuccess (Success 200){int} 200 成功
     */
    @Transactional
    public CompletionStage<Result> studentImportSchool(Http.Request request) {

            Http.MultipartFormData<Files.TemporaryFile> body = request.body().asMultipartFormData();
            Http.MultipartFormData.FilePart<Files.TemporaryFile> filePart = body.getFile("file");

            // 获取班级ID参数
            DynamicForm form = formFactory.form().bindFromRequest(request);
            //long classId = Long.parseLong(form.get("classId"));

            if (filePart == null) {
                return CompletableFuture.completedFuture(okCustomJson(CODE40001, "文件不能为空"));
            }

            return businessUtils.getUserIdByAuthToken(request).thenApplyAsync(adminMember -> {
                if (adminMember == null) return unauth403();

                if(adminMember.orgId < 1){
                    throw new RuntimeException("机构ID不能为空");
                }


                Files.TemporaryFile file = filePart.getRef();
                String fileName = filePart.getFilename();
                String targetFileName = UUID.randomUUID() + "." + FilenameUtils.getExtension(fileName);
                String destPath = FILE_DIR_LOCATION + targetFileName;

                // 确保目录存在
                new File(FILE_DIR_LOCATION).mkdirs();

                file.copyTo(Paths.get(destPath), true);
                File destFile = new File(destPath);

                try (InputStream inputStream = new FileInputStream(destFile)) {
                    // 读取Excel文件
                    List<StudentImportExcel> list = StudentImportExcel.importFromExcel(inputStream);

                    // 使用显式的事务管理
                    try (io.ebean.Transaction txn = io.ebean.DB.beginTransaction()) {
                        try {
                            // 数据验证
                            StudentImportExcel.validateData(list);

                            // 转换为实体并保存
                            StudentImportExcel.toEntity(list,adminMember.orgId);

                            // 提交事务
                            txn.commit();
                        } catch (Exception e) {
                            // 回滚事务
                            txn.rollback();
                            throw e;
                        }
                    }
                    return okJSON200();
                } catch (Exception e) {
                    return okCustomJson(CODE40001, "导入失败：" + e.getMessage());
                }
            });
    }


    /**
     * 查找或创建家长账号
     */
    private  ShopAdmin findOrCreateParent(String name, String phone, String relationship, String studentName) {
        if (name == null || name.trim().isEmpty() || phone == null || phone.trim().isEmpty()) {
            return null;
        }

        name = name.trim();
        phone = phone.trim();

        // 验证手机号格式
        if (!ValidationUtil.isValidPassword(phone)) {
            throw new RuntimeException("家长身份： " + name + " 的手机号格式不正确: " + phone);
        }

        // 查找现有家长（按手机号查找，确保唯一性）
        ShopAdmin parent = ShopAdmin.find.query()
                .where()
                .eq("phone_number", phone)
                .findOne();

        if (parent != null) {
//            // 如果找到现有家长，更新姓名（如果姓名不同）
            if (!generateParentRealName(studentName, relationship).equals(parent.getRealName())) {
                parent.setUserName(name);
                parent.update();
            }
            return parent;
        }

        // 创建新家长账号
        parent = new ShopAdmin();
        parent.setPhoneNumber(phone);
        parent.setUserName(phone); // 用户名设为手机号
        parent.setRealName(generateParentRealName(studentName, relationship)); // 真实姓名设为"学生名_关系"
        parent.setRules("家长");
        parent.setPassword(generateDefaultPassword());
        parent.save();

        return parent;
    }

    /**
     * 生成默认密码
     */
    private  String generateDefaultPassword() {
        return encodeUtils.getMd5WithSalt("123456");
    }

    /**
     * 生成家长真实姓名（学生名_关系）
     */
    private static String generateParentRealName(String studentName, String relationship) {
        return studentName + "-" + relationship;
    }

    /**
     * 批量分配学生到班级
     */
    private int batchAssignStudentsToClass(List<Long> studentIds, long classId) {
        int successCount = 0;
        long currentTime = dateUtils.getCurrentTimeByMilliSecond();

        for (Long studentId : studentIds) {
            try {
                Student student = Student.find.byId(studentId);
                if (student != null) {
                    student.setClassId(classId);
                    student.setUpdateTime(currentTime);
                    student.update();
                    successCount++;
                }
            } catch (Exception e) {
                // 记录错误但继续处理其他学生
                System.err.println("分配学生失败 ID=" + studentId + ": " + e.getMessage());
            }
        }

        return successCount;
    }

}
