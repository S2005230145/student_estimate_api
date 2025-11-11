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
    public static final double ACADEMIC_PASS = 18.0;      // 合格
    public static final double ACADEMIC_FAIL = 0.0;       // 不合格

    public static final double TOTAL_MAX_SCORE = 100.0; // 班级总分满分
    
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
    
    @Column(name = "academic_score")
    @DbComment("学业得分")
    public double academicScore;
    
    @Column(name = "specialty_score")
    @DbComment("特长得分")
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
     * 检查其他一票否决条件
     */
    private boolean checkOtherDisqualificationConditions() {
        // 这里可以添加平台测试、体育达标率等检查逻辑
        return false;
    }

    // ========== 学业得分计算方法 ==========

    /**
     * 计算学业得分（严格按照图片规则）
     */
    public double calculateAcademicScore( double chineseScore, double mathScore, Double englishScore) {

        // 计算学科平均分
        double subjectAverage;
        if (!this.isHighGrade()) {
            // 低年级（1-2年级）：只计算语文数学
            subjectAverage = (chineseScore + mathScore) / 2;
        } else {
            // 高年级（3年级以上）：计算语数英
            if (englishScore == null) {
                throw new IllegalArgumentException("高年级必须提供英语成绩");
            }
            subjectAverage = (chineseScore + mathScore + englishScore) / 3;
        }

        // 最终学业得分 = 基础分 × 学科平均分比例 × 30%
        double calculatedScore = baseScore * (subjectAverage / 100) * ACADEMIC_WEIGHT;
        setAcademicScore(calculatedScore);
        return getAcademicScore();
    }



    /**
     * 简化学业得分计算（如果已经有综合评分）
     */
    public double calculateAcademicScore(String academicLevel) {
        setAcademicScore(baseScore * ACADEMIC_WEIGHT);
        return getAcademicScore();
    }

    // ========== 特长得分计算方法 ==========

    /**
     * 计算特长得分（严格按照图片规则）
     */
    public double calculateSpecialtyScore(List<Student> students) {
        if (students == null || students.isEmpty()) {
            setSpecialtyScore(0);
            return 0;
        }

        // 计算所有学生特长平均分
        double totalSpecialty = 0;
        int validCount = 0;

        for (Student student : students) {
            if (student.getSpecialtyScore() >= 0) {
                totalSpecialty += student.getSpecialtyScore();
                validCount++;
            }
        }

        if (validCount == 0) {
            setSpecialtyScore(0);
            return 0;
        }

        double averageSpecialty = totalSpecialty / validCount;

        // 按20%折算，并封顶20分
        double calculatedScore = Math.min(averageSpecialty * SPECIALTY_WEIGHT, 20.0);
        setSpecialtyScore(calculatedScore);
        return getSpecialtyScore();
    }

    // ========== 班级常规评比得分设置方法 ==========

    /**
     * 设置班级常规评比得分（由德育处评定）
     */
    public double setRoutineScore(double routineScore) {
        // 常规评比得分直接由德育处输入，按30%折算
        setRoutineScore(routineScore * ROUTINE_WEIGHT);
        return getRoutineScore();
    }

    // ========== 家访工作评价得分设置方法 ==========

    /**
     * 设置家访工作评价得分
     */
    public double setHomeVisitScore(double homeVisitScore) {
        setHomeVisitScore(homeVisitScore * HOME_VISIT_WEIGHT);
        return getHomeVisitScore();
    }

    // ========== 扣分设置方法 ==========

    /**
     * 设置扣分
     */
    public void setDeductionScore(double deductionScore) {
        setDeductionScore(Math.max(0, deductionScore)); // 扣分不能为负数
    }

    // ========== 总分计算方法 ==========

    /**
     * 计算班级总分（严格按照图片公式）
     */
    public double calculateTotalScore() {
        // 先检查一票否决
        if (checkDisqualification()) {
            setTotalScore(0);
            setHonorTitle("不合格班级");
            return 0;
        }

        // 按照图片公式计算总分
        double calculatedTotal = getAcademicScore() + getSpecialtyScore() +
                getRoutineScore() + getHomeVisitScore() - getDeductionScore();

        // 确保总分不为负数
        setTotalScore(Math.max(0, calculatedTotal));

        // 更新荣誉称号
        updateHonorTitle();

        return getTotalScore();
    }

    /**
     * 综合计算所有得分
     */
    public SchoolClass calculateAllScores(String academicLevel, double chineseScore, double mathScore,
                                          Double englishScore, List<Student> students,
                                          double routineScore, double homeVisitScore, double deductionScore) {
        // 设置各项得分
        calculateAcademicScore(academicLevel, chineseScore, mathScore, englishScore);
        calculateSpecialtyScore(students);
        setRoutineScore(routineScore);
        setHomeVisitScore(homeVisitScore);
        setDeductionScore(deductionScore);

        // 计算总分
        calculateTotalScore();

        return this;
    }

    // ========== 荣誉称号计算方法 ==========

    /**
     * 更新荣誉称号
     */
    public String updateHonorTitle() {
        if (isDisqualified()) {
            setHonorTitle("不合格班级");
            return getHonorTitle();
        }

        double total = getTotalScore();
        if (total >= 90) {
            setHonorTitle("星河班级");
        } else if (total >= 80) {
            setHonorTitle("星辰班级");
        } else if (total >= 70) {
            setHonorTitle("优秀班级");
        } else if (total >= 60) {
            setHonorTitle("合格班级");
        } else {
            setHonorTitle("待改进班级");
        }

        return getHonorTitle();
    }

    // ========== 批量操作方法 ==========

    /**
     * 批量计算班级得分
     */
    public static void batchCalculateClassScores() {
        List<SchoolClass> classes = find.all();
        for (SchoolClass schoolClass : classes) {
            try {
                // 获取班级学生
                List<Student> students = Student.find.query()
                        .where()
                        .eq("class_id", schoolClass.getId())
                        .findList();

                // 这里需要根据实际情况获取其他评分数据
                // 暂时使用默认值，实际使用时需要从相关表获取
                schoolClass.calculateAcademicScore("良", 85, 80, 75.0);
                schoolClass.calculateSpecialtyScore(students);
                schoolClass.setRoutineScore(85);
                schoolClass.setHomeVisitScore(80);
                schoolClass.setDeductionScore(0);

                schoolClass.calculateTotalScore();
                schoolClass.setUpdateTime(System.currentTimeMillis());
                schoolClass.update();

            } catch (Exception e) {
                System.err.println("计算班级 " + schoolClass.getId() + " 得分失败: " + e.getMessage());
            }
        }
    }

    // ========== 工具方法 ==========

    /**
     * 获取得分详情
     */
    public String getScoreDetails() {
        if (isDisqualified()) {
            return String.format("一票否决：%s，总分为0", getDisqualifyReason());
        }

        return String.format("学业:%.1f(等级:%s) 特长:%.1f 常规:%.1f 家访:%.1f 扣分:%.1f 总分:%.1f",
                getAcademicScore(), getAcademicLevel(), getSpecialtyScore(), getRoutineScore(),
                getHomeVisitScore(), getDeductionScore(), getTotalScore());
    }

    /**
     * 获取计算公式
     */
    public String getCalculationFormula() {
        return String.format("总分 = %.1f×30%% + %.1f×20%% + %.1f×30%% + %.1f×20%% - %.1f = %.1f",
                getAcademicScore() / ACADEMIC_WEIGHT, getSpecialtyScore() / SPECIALTY_WEIGHT,
                getRoutineScore() / ROUTINE_WEIGHT, getHomeVisitScore() / HOME_VISIT_WEIGHT,
                getDeductionScore(), getTotalScore());
    }

    /**
     * 重置所有得分
     */
    public void resetScores() {
        setAcademicLevel("未评分");
        setAcademicScore(0);
        setSpecialtyScore(0);
        setRoutineScore(0);
        setHomeVisitScore(0);
        setDeductionScore(0);
        setTotalScore(0);
        setDisqualified(false);
        setDisqualifyReason(null);
        setHonorTitle("未评分");
        setUpdateTime(System.currentTimeMillis());
    }

    /**
     * 设置一票否决条件
     */
    public void setDisqualificationConditions(boolean teacherViolation, boolean safetyAccident,
                                              boolean disobeyAssignment, double satisfactionRate,
                                              boolean parentComplaint) {
        setTeacherViolation(teacherViolation);
        setSafetyAccident(safetyAccident);
        setDisobeyAssignment(disobeyAssignment);
        setSatisfactionRate(satisfactionRate);
        setParentComplaint(parentComplaint);

        // 自动检查是否触发一票否决
        checkDisqualification();
    }

    // ========== 业务逻辑方法 ==========

    /**
     * 检查是否可以选择方案B（学业成绩达到班级平均分）
     */
    public boolean canChooseSchemeB() {
        return getAcademicScore() >= 18 && !isDisqualified(); // 合格以上且未被一票否决
    }

    /**
     * 检查是否达到满意率
     */
    public boolean isSatisfactionAchieved() {
        return getSatisfactionRate() >= SATISFACTION_THRESHOLD && !isDisqualified();
    }

    /**
     * 获取评分等级
     */
    public String getScoreGrade() {
        if (isDisqualified()) return "E";
        double total = getTotalScore();
        if (total >= 90) return "A";
        if (total >= 80) return "B";
        if (total >= 70) return "C";
        if (total >= 60) return "D";
        return "E";
    }

    /**
     * 检查是否需要改进
     */
    public boolean needsImprovement() {
        return getTotalScore() < 60 && !isDisqualified();
    }
}


}