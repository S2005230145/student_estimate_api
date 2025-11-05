package models.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import models.business.AcademicRecord;
import models.business.Student;
import myannotation.Translation;
import utils.DateUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Data
@Translation("考试成绩导入Excel")
public class AcademicRecordExcel {

    @ExcelProperty(value = "学号", index = 0)
    @Translation("学号")
    public String studentNumber;

    @ExcelProperty(value = "学生姓名", index = 1)
    @Translation("学生姓名")
    public String studentName;

    @ExcelProperty(value = "班级", index = 2)
    @Translation("班级")
    public String className;

    @ExcelProperty(value = "考试类型", index = 3)
    @Translation("考试类型") // 期中/期末
    public String examType;

    @ExcelProperty(value = "考试时间", index = 4)
    @Translation("考试时间") // 格式：2024-01-15
    public String examDate;

    @ExcelProperty(value = "语文成绩", index = 5)
    @Translation("语文成绩")
    public Double chineseScore;

    @ExcelProperty(value = "数学成绩", index = 6)
    @Translation("数学成绩")
    public Double mathScore;

    @ExcelProperty(value = "英语成绩", index = 7)
    @Translation("英语成绩")
    public Double englishScore;

    @ExcelIgnore
    @Translation("平均分")
    public Double averageScore;

    @ExcelIgnore
    @Translation("计算得分")
    public Double calculatedScore;

    @ExcelIgnore
    @Translation("学生ID")
    public Long studentId;

    @ExcelIgnore
    private static DateUtils dateUtils = new DateUtils();

    /**
     * 导入Excel数据并转换为AcademicRecord实体
     */
    public static List<AcademicRecord> importFromExcel(InputStream inputStream) {
        List<AcademicRecordExcel> excelList = EasyExcel.read(inputStream)
                .head(AcademicRecordExcel.class)
                .sheet()
                .headRowNumber(1) // 跳过标题行
                .doReadSync();

        if (excelList == null || excelList.isEmpty()) {
            throw new RuntimeException("导入数据为空");
        }

        List<AcademicRecord> records = new ArrayList<>();
        for (AcademicRecordExcel excel : excelList) {
            AcademicRecord record = convertToEntity(excel);
            records.add(record);
        }

        return records;
    }

    /**
     * 将Excel数据转换为实体类
     */
    private static AcademicRecord convertToEntity(AcademicRecordExcel excel) {
        // 验证数据
        if (!validateData(excel)) {
            throw new RuntimeException("数据验证失败: " + excel.getStudentNumber());
        }

        // 查找学生
        Student student = Student.find.query()
                .where()
                .eq("student_number", excel.getStudentNumber())
                .findOne();

        if (student == null) {
            throw new RuntimeException("学号不存在: " + excel.getStudentNumber());
        }

        // 计算平均分
        double average ;
        if (student.isHighGrade()) {
            average = (excel.getChineseScore() + excel.getMathScore() + excel.getEnglishScore()) / 3.0;
        }else{
            average = (excel.getChineseScore() + excel.getMathScore() ) / 2.0;
        }

        // 转换考试类型
        int examType = "期中".equals(excel.getExamType()) ? AcademicRecord.EXAM_MIDTERM : AcademicRecord.EXAM_FINAL;

        // 转换考试时间
        long examDate = dateUtils.convertStringToUnixStamp(excel.getExamDate());

        // 检查是否已存在相同学生、相同考试类型、相同考试时间的记录
        AcademicRecord record = AcademicRecord.find.query()
                .where()
                .eq("student_id", student.id)
                .eq("exam_type", examType)
                .eq("exam_date", examDate)
                .findOne();

        if (record == null) {
            // 不存在记录，创建新记录
            record = new AcademicRecord();
            record.createTime = System.currentTimeMillis();
        } else {
            // 存在记录，更新记录
            record.updateTime = System.currentTimeMillis();
        }

        // 更新/设置记录数据
        record.studentId = student.id;
        record.examType = examType;
        record.chineseScore = excel.getChineseScore();
        record.mathScore = excel.getMathScore();
        record.englishScore = excel.getEnglishScore();
        record.averageScore = average;
        record.calculatedScore = calculateAcademicScore(average, record.examType);
        record.examDate = examDate;
        return record;
    }

    /**
     * 数据验证
     */
    private static boolean validateData(AcademicRecordExcel excel) {
        if (excel.getStudentNumber() == null || excel.getStudentNumber().trim().isEmpty()) {
            throw new RuntimeException("学号不能为空");
        }
        if (excel.getChineseScore() == null || excel.getChineseScore() < 0 || excel.getChineseScore() > 100) {
            throw new RuntimeException("语文成绩必须在0-100之间");
        }
        if (excel.getMathScore() == null || excel.getMathScore() < 0 || excel.getMathScore() > 100) {
            throw new RuntimeException("数学成绩必须在0-100之间");
        }
        if (excel.getEnglishScore() == null || excel.getEnglishScore() < 0 || excel.getEnglishScore() > 100) {
            throw new RuntimeException("英语成绩必须在0-100之间");
        }
        if (excel.getExamType() == null || (!"期中".equals(excel.getExamType()) && !"期末".equals(excel.getExamType()))) {
            throw new RuntimeException("考试类型必须是'期中'或'期末'");
        }
        return true;
    }

    /**
     * 计算学业得分
     */
    private static double calculateAcademicScore(double averageScore, int examType) {
        if (averageScore >= AcademicRecord.PASS_SCORE) {
            return AcademicRecord.BASE_SCORE; // 及格得20分
        }
        if (averageScore >= 85) {
            return AcademicRecord.EXCELLENT_SCORE; // 优秀得40分
        }
        return 0.0; // 不及格得0分
    }

    /**
     * 批量保存成绩记录
     */
    public static void batchSave(List<AcademicRecord> records) {
        if (records != null && !records.isEmpty()) {
            for (AcademicRecord record : records) {
                record.save();
            }
        }
    }

    /**
     * 导出成绩数据到Excel
     */
    public static void exportToExcel(OutputStream outputStream, List<AcademicRecord> records) {
        List<AcademicRecordExcel> excelList = toExcelList(records);
        EasyExcel.write(outputStream, AcademicRecordExcel.class)
                .sheet("考试成绩")
                .doWrite(excelList);
    }

    /**
     * 将实体列表转换为Excel列表
     */
    public static List<AcademicRecordExcel> toExcelList(List<AcademicRecord> records) {
        if (records == null || records.isEmpty()) {
            throw new RuntimeException("数据为空");
        }

        List<AcademicRecordExcel> excelList = new ArrayList<>();
        for (AcademicRecord record : records) {
            AcademicRecordExcel excel = new AcademicRecordExcel();
            Student student = Student.find.byId(record.studentId);

            if (student != null) {
                excel.studentNumber = student.studentNumber;
                excel.studentName = student.name;
            }

            excel.examType = record.examType == AcademicRecord.EXAM_MIDTERM ? "期中" : "期末";
            excel.examDate = dateUtils.formatToYMD(record.examDate);
            excel.chineseScore = record.chineseScore;
            excel.mathScore = record.mathScore;
            excel.englishScore = record.englishScore;
            excel.averageScore = record.averageScore;
            excel.calculatedScore = record.calculatedScore;

            excelList.add(excel);
        }

        return excelList;
    }

    /**
     * 获取Excel表头
     */
    public static String[] getExcelHeaders() {
        Field[] fields = AcademicRecordExcel.class.getDeclaredFields();
        return Arrays.stream(fields)
                .filter(f -> f.isAnnotationPresent(ExcelProperty.class))
                .sorted(Comparator.comparingInt(f -> f.getAnnotation(ExcelProperty.class).index()))
                .map(f -> {
                    ExcelProperty prop = f.getAnnotation(ExcelProperty.class);
                    return prop.value().length > 0 ? prop.value()[0] : f.getName();
                })
                .toArray(String[]::new);
    }

    /**
     * 简单的导入方法（一键导入）
     */
    public static String simpleImport(InputStream inputStream) {
        try {
            List<AcademicRecord> records = importFromExcel(inputStream);
            batchSave(records);
            return String.format("导入成功：共%d条记录", records.size());
        } catch (Exception e) {
            return "导入失败：" + e.getMessage();
        }
    }
}