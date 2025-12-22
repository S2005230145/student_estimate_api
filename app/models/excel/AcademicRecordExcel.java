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
import java.util.ArrayList;
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
    @Translation("考试类型")
    public String examType;

    @ExcelProperty(value = "考试时间", index = 4)
    @Translation("考试时间")
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
    private static DateUtils dateUtils = new DateUtils();


    /**
     * 导入
     */
    public static List<AcademicRecord> importFromExcel(InputStream inputStream) {
        List<AcademicRecordExcel> excelList = EasyExcel.read(inputStream)
                .head(AcademicRecordExcel.class)
                .sheet()
                .headRowNumber(1)
                .doReadSync();

        if (excelList == null || excelList.isEmpty()) {
            throw new RuntimeException("导入数据为空");
        }

        List<AcademicRecord> records = new ArrayList<>();
        for (AcademicRecordExcel excel : excelList) {
            records.add(convertToEntity(excel));
        }
        return records;
    }

    /**
     * 导出
     */
    public static void exportToExcel(OutputStream outputStream, List<AcademicRecord> records) {
        List<AcademicRecordExcel> excelList = toExcelList(records);
        EasyExcel.write(outputStream, AcademicRecordExcel.class)
                .sheet("考试成绩")
                .doWrite(excelList);
    }

    /**
     * entity list -> Excel list
     */
    private static List<AcademicRecordExcel> toExcelList(List<AcademicRecord> records) {
        List<AcademicRecordExcel> excelList = new ArrayList<>();
        for (AcademicRecord record : records) {
            AcademicRecordExcel excel = new AcademicRecordExcel();
            Student student = Student.find.byId(record.studentId);

            if (student != null) {
                excel.studentNumber = student.studentNumber;
                excel.studentName = student.name;
                //excel.className = student.setClassInfo;
                //student.setClassInfo(excel.className);
            }

            excel.examType = record.examType == AcademicRecord.EXAM_MIDTERM ? "期中" : "期末";
            excel.examDate = dateUtils.formatToYMD(record.examDate);
            excel.chineseScore = record.chineseScore;
            excel.mathScore = record.mathScore;
            excel.englishScore = record.englishScore;

            excelList.add(excel);
        }
        return excelList;
    }

    /**
     * excel -> entity
     */
    private static AcademicRecord convertToEntity(AcademicRecordExcel excel) {
        validateData(excel);
        Student student = findOrCreateStudent(excel);
        AcademicRecord record = findOrCreateRecord(excel, student);
        updateRecordData(record, excel, student);
        return record;
    }

    /**
     * 数据验证
     */
    private static void validateData(AcademicRecordExcel excel) {
        if (excel.getStudentNumber() == null || excel.getStudentNumber().trim().isEmpty()) {
            throw new RuntimeException("学号不能为空");
        }
        if (excel.getChineseScore() == null || excel.getChineseScore() < 0 || excel.getChineseScore() > 100) {
            throw new RuntimeException("语文成绩必须在0-100之间");
        }
        if (excel.getMathScore() == null || excel.getMathScore() < 0 || excel.getMathScore() > 100) {
            throw new RuntimeException("数学成绩必须在0-100之间");
        }
//        if (excel.getEnglishScore() == null || excel.getEnglishScore() < 0 || excel.getEnglishScore() > 100) {
//            throw new RuntimeException("英语成绩必须在0-100之间");
//        }

        // 先解析年级
        int grade = 0;
        String className = excel.getClassName();
        if (className != null) {
            if (className.contains("一年级")) grade = 1;
            else if (className.contains("二年级")) grade = 2;
            else if (className.contains("三年级")) grade = 3;
            else if (className.contains("四年级")) grade = 4;
            else if (className.contains("五年级")) grade = 5;
            else if (className.contains("六年级")) grade = 6;
        }

        // 三年级及以上需要英语成绩
        if (grade >= 3) {
            Double englishScore = excel.getEnglishScore();
            if (englishScore == null || englishScore < 0 || englishScore > 100) {
                throw new RuntimeException("英语成绩必须在0-100之间");
            }
        }



        if (excel.getExamType() == null || (!"期中".equals(excel.getExamType()) && !"期末".equals(excel.getExamType()))) {
            throw new RuntimeException("考试类型必须是'期中'或'期末'");
        }
    }

    /**
     * 查找或创建学生
     */
    private static Student findOrCreateStudent(AcademicRecordExcel excel) {
        Student student = Student.find.query()
                .where()
                .eq("student_number", excel.getStudentNumber())
                .setMaxRows(1)
                .findOne();

        if (student == null) {
            throw new RuntimeException("学生不存在");
//            student = createStudent(excel);
//            student.save();
        }
        return student;
    }

    /**
     * 创建学生
     */
    private static Student createStudent(AcademicRecordExcel excel) {
        Student student = new Student();
        student.studentNumber = excel.getStudentNumber();
        student.name = excel.getStudentName();
        parseClassInfo(student, excel.getClassName());
        student.evaluationScheme = Student.SCHEME_A;
        student.createTime = System.currentTimeMillis();
        student.updateTime = System.currentTimeMillis();
        return student;
    }

    /**
     * 解析班级信息
     */
    private static void parseClassInfo(Student student, String className) {
        if (className.contains("一年级")) student.grade = 1;
        else if (className.contains("二年级")) student.grade = 2;
        else if (className.contains("三年级")) student.grade = 3;
        else if (className.contains("四年级")) student.grade = 4;
        else if (className.contains("五年级")) student.grade = 5;
        else if (className.contains("六年级")) student.grade = 6;
        else throw new RuntimeException("无法识别的年级: " + className);

        if (className.contains("一班")) student.classId = 1L;
        else if (className.contains("二班")) student.classId = 2L;
        else if (className.contains("三班")) student.classId = 3L;
        else if (className.contains("四班")) student.classId = 4L;
        else if (className.contains("五班")) student.classId = 5L;
        else if (className.contains("六班")) student.classId = 6L;
        else student.classId = 1L;
    }


    /**
     * 查找或创建成绩记录
     */
    private static AcademicRecord findOrCreateRecord(AcademicRecordExcel excel, Student student) {
        int examType = "期中".equals(excel.getExamType()) ? AcademicRecord.EXAM_MIDTERM : AcademicRecord.EXAM_FINAL;
        long examDate = dateUtils.convertStringToUnixStamp(excel.getExamDate());

        AcademicRecord record = AcademicRecord.find.query()
                .where()
                .eq("student_id", student.id)
                .eq("exam_type", examType)
                .eq("exam_date", examDate)
                .findOne();

        if (record == null) {
            record = new AcademicRecord();
            record.createTime = System.currentTimeMillis();
        }
        record.updateTime = System.currentTimeMillis();
        return record;
    }

    /**
     * 更新记录数据 (基本数据保存，计算排名、徽章、学业分在另外的方法
     */
    private static void updateRecordData(AcademicRecord record, AcademicRecordExcel excel, Student student) {
        record.studentId = student.id;
        record.examType = "期中".equals(excel.getExamType()) ? AcademicRecord.EXAM_MIDTERM : AcademicRecord.EXAM_FINAL;
        record.examDate = dateUtils.convertStringToUnixStamp(excel.getExamDate());
        record.chineseScore = excel.getChineseScore();
        record.mathScore = excel.getMathScore();

        // 英语成绩可能为 null，这里做安全处理，空则按 0 分处理
        Double englishScoreWrapper = excel.getEnglishScore();
        double englishScore = englishScoreWrapper == null ? 0.0 : englishScoreWrapper;
        record.englishScore = englishScore;

        // 计算平均分：如果英语成绩不为 null 且是高年级，则计算三科平均分；否则计算两科平均分
        boolean hasEnglishScore = englishScoreWrapper != null;
        boolean isHighGrade = student.isHighGrade() && hasEnglishScore;

        record.averageScore = Math.round(
                (isHighGrade ?
                        (excel.getChineseScore() + excel.getMathScore() + englishScore) / 3.0 :
                        (excel.getChineseScore() + excel.getMathScore()) / 2.0
                ) * 100.0
        ) / 100.0;
    }
}