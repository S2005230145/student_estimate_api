package models.business;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.DbComment;
import jakarta.persistence.*;
import lombok.Data;
import models.admin.ShopAdmin;
import myannotation.EscapeHtmlAuthoritySerializer;

import javax.inject.Inject;
import java.util.List;

@Data
@Entity
@Table(name = "v1_school_class")
@DbComment("班级信息")
public class SchoolClass  extends Model {

    public static final double ACADEMIC_WEIGHT = 0.3; // 学业权重30%
    public static final double SPECIALTY_WEIGHT = 0.2; // 特长权重20%
    public static final double ROUTINE_WEIGHT = 0.3; // 常规权重30%
    public static final double HOME_VISIT_WEIGHT = 0.2; // 家访权重20%

    // 学业评分等级
    public static final double ACADEMIC_EXCELLENT = 30.0; // 优
    public static final double ACADEMIC_GOOD = 24.0;      // 良
    public static final double ACADEMIC_PASS = 20.0;      // 合格
    public static final double ACADEMIC_FAIL = 0.0;       // 不合格

    public static final double TOTAL_MAX_SCORE = 100.0; // 班级总分满分

    @Column(name = "org_id")
    @DbComment("机构ID")
    public long orgId;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @DbComment("唯一标识")
    public long id;
    
    @Column(name = "class_name")
    @DbComment("班级名称")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String className;
    
    @Column(name = "grade")
    @DbComment("年级")
    public int grade;
    
    @Column(name = "head_teacher_id")
    @DbComment("班主任ID")
    public long headTeacherId;

    @Transient
    private ShopAdmin headTeacher;

    @Column(name = "student_num")
    @DbComment("人数")
    public int studentNum;
    
    @Column(name = "academic_score")
    @DbComment("学业得分总分")
    public double academicScore;
    
    @Column(name = "specialty_score")
    @DbComment("特长得分总分")
    public double specialtyScore;
    
    @Column(name = "routine_score")
    @DbComment("常规得分")
    public double routineScore;
    
    @Column(name = "home_visit_score")
    @DbComment("家访得分")
    public double homeVisitScore;
    
    @Column(name = "total_score")
    @DbComment("总分")
    public double totalScore;
    
    @Column(name = "disqualified")
    @DbComment("一票否决")
    public boolean disqualified;

    @Column(name = "deduction_score")
    @DbComment("扣分")
    public double deductionScore;

    @Column(name = "honor_title")
    @DbComment("荣誉称号") // 星河班级/星辰班级
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String honorTitle;

    @Column(name = "create_time")
    @DbComment("创建时间")
    public long createTime;

    @Transient
    private List<ShopAdmin> teachers;


    public static Finder<Long, SchoolClass> find = new Finder<>(SchoolClass.class);

    public boolean isHighGrade() {
        return this.grade >= 3;
    }


    /**
     * 计算班级总分
     */
    public double calculateTotalScore() {
        // 按照公式计算总分
        double calculatedTotal = getAcademicScore()/getStudentNum()* ACADEMIC_WEIGHT + getSpecialtyScore()/getStudentNum() * SPECIALTY_WEIGHT +
                getRoutineScore() * ROUTINE_WEIGHT + getHomeVisitScore() * HOME_VISIT_WEIGHT - getDeductionScore();

        this.setTotalScore(calculatedTotal);

        return this.getTotalScore();
    }


    /**
     * 计算学生数
     */
    public void calcStudentNum() {
        int count = Student.find.query().where().eq("class_id", this.getId()).findCount();
        this.setStudentNum(count);
        this.update();
    }


//    ----------学业分计算----------------

    /**
     * 重新计算班级学业总分（基于所有学生学业成绩）
     * @return 计算后的学业总分
     */
    public double recalcAcademicTotalScore() {
        List<Student> students = Student.find.query()
                .where()
                .eq("class_id", this.getId())
                .findList();

        if (students == null || students.isEmpty()) {
            this.setAcademicScore(0);
            return 0;
        }

        double totalAcademicScore = 0;
        int validCount = 0;

        for (Student student : students) {
            if (student.getAcademicScore() >= 0) {
                totalAcademicScore += student.getAcademicScore();
                validCount++;
            }
        }

        if (validCount == 0) {
            this.setAcademicScore(0);
            return 0;
        }

        this.setAcademicScore(totalAcademicScore);
        return this.getAcademicScore();
    }

    /**
     * 重新计算班级学业总分并保存
     * @return 更新后的学业总分
     */
    public double recalcAndSaveAcademicTotalScore() {
        recalcAcademicTotalScore();
        this.update();
        return this.getAcademicScore();
    }

    /**
     * 重新计算指定班级的学业总分
     * @param classId 班级ID
     * @return 计算后的学业总分
     */
    public static double recalcAcademicTotalScoreById(long classId) {
        SchoolClass schoolClass = find.byId(classId);
        if (schoolClass == null) {
            return 0;
        }
        return schoolClass.recalcAndSaveAcademicTotalScore();
    }

    /**
     * 批量重新计算多个班级的学业总分
     * @param classIds 班级ID列表
     * @return 成功计算的班级数量
     */
    public static int  batchRecalcAcademicScores(List<Long> classIds) {
        int successCount = 0;
        for (Long classId : classIds) {
            try {
                recalcAcademicTotalScoreById(classId);
                successCount++;
            } catch (Exception e) {
                System.err.println("计算班级 " + classId + " 学业总分失败: " + e.getMessage());
            }
        }
        return successCount;
    }


    /**
     * 获取学业得分排名（在年级中的排名）
     */
    public int getAcademicRankInGrade() {
        List<SchoolClass> sameGradeClasses = find.query()
                .where()
                .eq("grade", this.getGrade())
                .orderBy("academic_score desc")
                .findList();

        for (int i = 0; i < sameGradeClasses.size(); i++) {
            if (sameGradeClasses.get(i).getId() == this.getId()) {
                return i + 1;
            }
        }
        return -1;
    }

//    -------------特长分计算----------------

    /**
     * 重新计算班级特长总分（基于所有学生特长成绩）
     * @return 计算后的特长总分
     */
    public double recalcSpecialtyTotalScore() {
        List<Student> students = Student.find.query()
                .where()
                .eq("class_id", this.getId())
                .findList();

        if (students == null || students.isEmpty()) {
            this.setSpecialtyScore(0);
            return 0;
        }

        double totalSpecialtyScore = 0;
        int validCount = 0;

        for (Student student : students) {
            if (student.getSpecialtyScore() >= 0) {
                totalSpecialtyScore += student.getSpecialtyScore();
                validCount++;
            }
        }

        if (validCount == 0) {
            this.setSpecialtyScore(0);
            return 0;
        }

        this.setSpecialtyScore(totalSpecialtyScore);
        return this.getSpecialtyScore();
    }

    /**
     * 重新计算班级特长总分并保存
     * @return 更新后的特长总分
     */
    public double recalcAndSaveSpecialtyTotalScore() {
        recalcSpecialtyTotalScore();
        this.update();
        return this.getSpecialtyScore();
    }

    /**
     * 重新计算指定班级的特长总分
     * @param classId 班级ID
     * @return 计算后的特长总分
     */
    public static double recalcSpecialtyTotalScoreById(long classId) {
        SchoolClass schoolClass = find.byId(classId);
        if (schoolClass == null) {
            return 0;
        }
        return schoolClass.recalcAndSaveSpecialtyTotalScore();
    }


    /**
     * 批量重新计算多个班级的特长总分
     * @param classIds 班级ID列表
     * @return 成功计算的班级数量
     */
    public static int batchRecalcSpecialtyScores(List<Long> classIds) {
        int successCount = 0;
        for (Long classId : classIds) {
            try {
                recalcSpecialtyTotalScoreById(classId);
                successCount++;
            } catch (Exception e) {
                System.err.println("计算班级 " + classId + " 特长总分失败: " + e.getMessage());
            }
        }
        return successCount;
    }



    /**
     * 获取特长得分排名（在年级中的排名）
     */
    public int getSpecialtyRankInGrade() {
        List<SchoolClass> sameGradeClasses = find.query()
                .where()
                .eq("grade", this.getGrade())
                .orderBy("specialty_score desc")
                .findList();

        for (int i = 0; i < sameGradeClasses.size(); i++) {
            if (sameGradeClasses.get(i).getId() == this.getId()) {
                return i + 1;
            }
        }
        return -1;
    }


}