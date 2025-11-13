package controllers.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import controllers.BaseSecurityController;
import io.ebean.DB;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import io.ebean.annotation.Transactional;
import models.business.AcademicRecord;
import models.business.Student;
import models.excel.AcademicRecordExcel;
import org.apache.commons.io.FilenameUtils;
import play.libs.Files;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import utils.ValidationUtil;

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

public class AcademicRecordController extends BaseSecurityController {

    /**
     * @api {GET} /v2/p/academic_record_list/   01列表-学业成绩记录
     * @apiName listAcademicRecord
     * @apiGroup ACADEMIC-RECORD-CONTROLLER
     * @apiParam {int} page 页码
     * @apiParam {String} filter 搜索栏()
     * @apiSuccess (Success 200) {int} EXAM_MIDTERM
     * @apiSuccess (Success 200) {int} EXAM_FINAL
     * @apiSuccess (Success 200) {double} PASS_SCORE
     * @apiSuccess (Success 200) {int} TOP_RANKING
     * @apiSuccess (Success 200) {double} BASE_SCORE
     * @apiSuccess (Success 200) {double} EXCELLENT_SCORE
     * @apiSuccess (Success 200) {double} PROGRESS_SCORE
     * @apiSuccess (Success 200) {long} orgId 机构ID
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {long} studentId 学生ID
     * @apiSuccess (Success 200) {int} examType 考试类型
     * @apiSuccess (Success 200) {double} chineseScore 语文成绩
     * @apiSuccess (Success 200) {double} mathScore 数学成绩
     * @apiSuccess (Success 200) {double} englishScore 英语成绩
     * @apiSuccess (Success 200) {double} averageScore 平均分
     * @apiSuccess (Success 200) {int} gradeRanking 年级排名
     * @apiSuccess (Success 200) {int} classRanking 班级排名
     * @apiSuccess (Success 200) {int} progressAmount 进步名次
     * @apiSuccess (Success 200) {int} progressRanking 进步排名
     * @apiSuccess (Success 200) {double} calculatedScore 计算得分
     * @apiSuccess (Success 200) {String} badgeAwarded 授予徽章
     * @apiSuccess (Success 200) {long} examDate 考试时间
     * @apiSuccess (Success 200) {long} createTime 创建时间
     * @apiSuccess (Success 200) {long} updateTime 更新时间
     */
    public CompletionStage<Result> listAcademicRecord(Http.Request request, int page, String filter, int status) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ExpressionList<AcademicRecord> expressionList = AcademicRecord.find.query().where().eq("org_id", adminMember.getOrgId());
            if (status > 0) expressionList.eq("status", status);
            if (!ValidationUtil.isEmpty(filter)) expressionList
                    .or()
                    .icontains("filter", filter)
                    .endOr();               //编写其他条件  
            //编写其他条件
            //编写其他条件
            //编写其他条件

            ObjectNode result = Json.newObject();
            List<AcademicRecord> list;
            if (page == 0) list = expressionList.findList();
            else {
                PagedList<AcademicRecord> pagedList = expressionList
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
     * @api {GET} /v2/p/academic_record/:id/  02详情-AcademicRecord学业成绩记录
     * @apiName getAcademicRecord
     * @apiGroup ACADEMIC-RECORD-CONTROLLER
     * @apiParam {long} id id
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {int} EXAM_MIDTERM
     * @apiSuccess (Success 200) {int} EXAM_FINAL
     * @apiSuccess (Success 200) {double} PASS_SCORE
     * @apiSuccess (Success 200) {int} TOP_RANKING
     * @apiSuccess (Success 200) {double} BASE_SCORE
     * @apiSuccess (Success 200) {double} EXCELLENT_SCORE
     * @apiSuccess (Success 200) {double} PROGRESS_SCORE
     * @apiSuccess (Success 200) {long} orgId 机构ID
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {long} studentId 学生ID
     * @apiSuccess (Success 200) {int} examType 考试类型
     * @apiSuccess (Success 200) {double} chineseScore 语文成绩
     * @apiSuccess (Success 200) {double} mathScore 数学成绩
     * @apiSuccess (Success 200) {double} englishScore 英语成绩
     * @apiSuccess (Success 200) {double} averageScore 平均分
     * @apiSuccess (Success 200) {int} gradeRanking 年级排名
     * @apiSuccess (Success 200) {int} classRanking 班级排名
     * @apiSuccess (Success 200) {int} progressAmount 进步名次
     * @apiSuccess (Success 200) {int} progressRanking 进步排名
     * @apiSuccess (Success 200) {double} calculatedScore 计算得分
     * @apiSuccess (Success 200) {String} badgeAwarded 授予徽章
     * @apiSuccess (Success 200) {long} examDate 考试时间
     * @apiSuccess (Success 200) {long} createTime 创建时间
     * @apiSuccess (Success 200) {long} updateTime 更新时间
     */
    public CompletionStage<Result> getAcademicRecord(Http.Request request, long id) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            AcademicRecord academicRecord = AcademicRecord.find.byId(id);
            if (null == academicRecord) return okCustomJson(CODE40001, "数据不存在");
            //sass数据校验  
            if (academicRecord.orgId != adminMember.getOrgId()) return okCustomJson(CODE40001, "数据不存在");
            ObjectNode result = (ObjectNode) Json.toJson(academicRecord);
            result.put(CODE, CODE200);
            return ok(result);
        });

    }

    /**
     * @api {POST} /v2/p/academic_record/new/   01添加-AcademicRecord学业成绩记录
     * @apiName addAcademicRecord
     * @apiDescription 描述
     * @apiGroup ACADEMIC-RECORD-CONTROLLER
     * @apiParam {int} EXAM_MIDTERM
     * @apiParam {int} EXAM_FINAL
     * @apiParam {double} PASS_SCORE
     * @apiParam {int} TOP_RANKING
     * @apiParam {double} BASE_SCORE
     * @apiParam {double} EXCELLENT_SCORE
     * @apiParam {double} PROGRESS_SCORE
     * @apiParam {long} orgId 机构ID
     * @apiParam {long} id 唯一标识
     * @apiParam {long} studentId 学生ID
     * @apiParam {int} examType 考试类型
     * @apiParam {double} chineseScore 语文成绩
     * @apiParam {double} mathScore 数学成绩
     * @apiParam {double} englishScore 英语成绩
     * @apiParam {double} averageScore 平均分
     * @apiParam {int} gradeRanking 年级排名
     * @apiParam {int} classRanking 班级排名
     * @apiParam {int} progressAmount 进步名次
     * @apiParam {int} progressRanking 进步排名
     * @apiParam {double} calculatedScore 计算得分
     * @apiParam {String} badgeAwarded 授予徽章
     * @apiParam {long} examDate 考试时间
     * @apiParam {long} createTime 创建时间
     * @apiParam {long} updateTime 更新时间
     * @apiSuccess (Success 200){int} code 200
     */

    public CompletionStage<Result> addAcademicRecord(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((admin) -> {
            if (null == admin) return unauth403();
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");
            AcademicRecord academicRecord = Json.fromJson(jsonNode, AcademicRecord.class);
// 数据sass化
            academicRecord.setOrgId(admin.getOrgId());
            long currentTimeBySecond = dateUtils.getCurrentTimeByMilliSecond();
            academicRecord.setCreateTime(currentTimeBySecond);
            academicRecord.setUpdateTime(currentTimeBySecond);
            academicRecord.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/academic_record/:id/  04更新-AcademicRecord学业成绩记录
     * @apiName updateAcademicRecord
     * @apiGroup ACADEMIC-RECORD-CONTROLLER
     * @apiParam {int} EXAM_MIDTERM
     * @apiParam {int} EXAM_FINAL
     * @apiParam {double} PASS_SCORE
     * @apiParam {int} TOP_RANKING
     * @apiParam {double} BASE_SCORE
     * @apiParam {double} EXCELLENT_SCORE
     * @apiParam {double} PROGRESS_SCORE
     * @apiParam {long} orgId 机构ID
     * @apiParam {long} id 唯一标识
     * @apiParam {long} studentId 学生ID
     * @apiParam {int} examType 考试类型
     * @apiParam {double} chineseScore 语文成绩
     * @apiParam {double} mathScore 数学成绩
     * @apiParam {double} englishScore 英语成绩
     * @apiParam {double} averageScore 平均分
     * @apiParam {int} gradeRanking 年级排名
     * @apiParam {int} classRanking 班级排名
     * @apiParam {int} progressAmount 进步名次
     * @apiParam {int} progressRanking 进步排名
     * @apiParam {double} calculatedScore 计算得分
     * @apiParam {String} badgeAwarded 授予徽章
     * @apiParam {long} examDate 考试时间
     * @apiParam {long} createTime 创建时间
     * @apiParam {long} updateTime 更新时间
     * @apiSuccess (Success 200){int} code 200
     */
    public CompletionStage<Result> updateAcademicRecord(Http.Request request, long id) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            AcademicRecord originalAcademicRecord = AcademicRecord.find.byId(id);
            AcademicRecord newAcademicRecord = Json.fromJson(jsonNode, AcademicRecord.class);
            if (null == originalAcademicRecord) return okCustomJson(CODE40001, "数据不存在");
            //sass数据校验  
            if (originalAcademicRecord.orgId != adminMember.getOrgId()) return okCustomJson(CODE40001, "数据不存在");
            if (newAcademicRecord.studentId > 0) originalAcademicRecord.setStudentId(newAcademicRecord.studentId);
            if (newAcademicRecord.examType > 0) originalAcademicRecord.setExamType(newAcademicRecord.examType);
            if (newAcademicRecord.chineseScore > 0)
                originalAcademicRecord.setChineseScore(newAcademicRecord.chineseScore);
            if (newAcademicRecord.mathScore > 0) originalAcademicRecord.setMathScore(newAcademicRecord.mathScore);
            if (newAcademicRecord.englishScore > 0)
                originalAcademicRecord.setEnglishScore(newAcademicRecord.englishScore);
            if (newAcademicRecord.averageScore > 0)
                originalAcademicRecord.setAverageScore(newAcademicRecord.averageScore);
            if (newAcademicRecord.gradeRanking > 0)
                originalAcademicRecord.setGradeRanking(newAcademicRecord.gradeRanking);
            if (newAcademicRecord.classRanking > 0)
                originalAcademicRecord.setClassRanking(newAcademicRecord.classRanking);
            if (newAcademicRecord.progressAmount > 0)
                originalAcademicRecord.setProgressAmount(newAcademicRecord.progressAmount);
            if (newAcademicRecord.progressRanking > 0)
                originalAcademicRecord.setProgressRanking(newAcademicRecord.progressRanking);
            if (newAcademicRecord.calculatedScore > 0)
                originalAcademicRecord.setCalculatedScore(newAcademicRecord.calculatedScore);
            if (!ValidationUtil.isEmpty(newAcademicRecord.badgeAwarded))
                originalAcademicRecord.setBadgeAwarded(newAcademicRecord.badgeAwarded);
            if (newAcademicRecord.examDate > 0) originalAcademicRecord.setExamDate(newAcademicRecord.examDate);
            if (newAcademicRecord.updateTime > 0) originalAcademicRecord.setUpdateTime(newAcademicRecord.updateTime);
            long currentTimeBySecond = dateUtils.getCurrentTimeByMilliSecond();
            originalAcademicRecord.setUpdateTime(currentTimeBySecond);
            originalAcademicRecord.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/academic_record/   05删除-学业成绩记录
     * @apiName deleteAcademicRecord
     * @apiGroup ACADEMIC-RECORD-CONTROLLER
     * @apiParam {long} id id
     * @apiParam {String} operation del时删除
     * @apiSuccess (Success 200){int} 200 成功
     */
    public CompletionStage<Result> deleteAcademicRecord(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            long id = jsonNode.findPath("id").asLong();
            String operation = jsonNode.findPath("operation").asText();
            if (!"del".equals(operation)) return okCustomJson(CODE40001, "操作错误");
            AcademicRecord deleteModel = AcademicRecord.find.byId(id);
            if (null == deleteModel) return okCustomJson(CODE40001, "数据不存在");
            //sass数据校验  
            if (deleteModel.orgId != adminMember.getOrgId()) return okCustomJson(CODE40001, "数据不存在");
            deleteModel.delete();
            return okJSON200();
        });
    }
    /**
     * @api {POST} /v2/p/academic_record_excel/   06导入学生成绩文件
     * @apiName academicRecordImport
     * @apiGroup ACADEMIC-RECORD-CONTROLLER
     * @apiParam {file} file 成绩文件
     * @apiSuccess (Success 200){int} 200 成功
     */
    @Transactional
    public CompletionStage<Result> academicRecordImport(Http.Request request) {
        Http.MultipartFormData<Files.TemporaryFile> body = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<Files.TemporaryFile> filePart = body.getFile("file");
        if (filePart == null) {
            return CompletableFuture.completedFuture(okCustomJson(CODE40001, "文件不能为空"));
        }

        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync(adminMember -> {
            if (adminMember == null) return unauth403();

            Files.TemporaryFile file = filePart.getRef();
            String fileName = filePart.getFilename();
            String targetFileName = UUID.randomUUID() + "." + FilenameUtils.getExtension(fileName);
            String destPath = FILE_DIR_LOCATION + targetFileName;
            file.copyTo(Paths.get(destPath), true);
            File destFile = new File(destPath);

            try (InputStream inputStream = new FileInputStream(destFile)) {
                // 读取文件
                List<AcademicRecord> list = AcademicRecordExcel.importFromExcel(inputStream);
                // 计算排名和徽章和学业分
                List<AcademicRecord> academicRecords = AcademicRecord.batchCalcAllRankingsAndBadgesAndStudyScore(list);

                DB.saveAll(academicRecords);
                return okJSON200();
            } catch (Exception e) {
                return okCustomJson(CODE40001, "导入失败：" + e.getMessage());
            }
        });
    }

    /**
     * @api {GET} /v2/p/academic_record_excel_template/ 07导出学业成绩导入模板
     * @apiName exportAcTemplate
     * @apiGroup PERSONNEL-CONTROLLER
     * @apiSuccess (Success 200){file} Excel文件 导入模板文件
     */
    public CompletionStage<Result> exportAcTemplate(Http.Request request) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync(adminMember -> {
            if (adminMember == null) return unauth403();
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                AcademicRecordExcel.exportToExcel(outputStream, new ArrayList<>());
                byte[] bytes = outputStream.toByteArray();

                return ok(bytes)
                        .withHeader("Content-Disposition", "attachment; filename=personnel_import_template.xlsx")
                        .as("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            } catch (Exception e) {
                return okCustomJson(CODE40001, "导出模板失败：" + e.getMessage());
            }
        });
    }

    /**
     * @api {GET} /v2/p/test/ 07导出学业成绩导入模板
     * @apiName exportAcTemplate
     * @apiGroup PERSONNEL-CONTROLLER
     * @apiSuccess (Success 200){file} Excel文件 导入模板文件
     */
    public CompletionStage<Result> test(Http.Request request) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync(adminMember -> {
            if (adminMember == null) return unauth403();
            Student student = Student.find.all().get(0);
            student.setBadges("星辰徽章,星火徽章");
            student.save();
            return okJSON200();
        });
    }
}
