package models.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.Transaction;
import io.ebean.annotation.DbComment;
import jakarta.persistence.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import myannotation.EscapeHtmlAuthoritySerializer;

import java.time.*;
import java.util.List;

import static constants.BusinessConstant.SPECIALTY_SCORE_MATRIX;
import static models.business.SchoolClass.ACADEMIC_PASS;
import static models.business.Student.HABIT_PASS;

@Slf4j
@Data
@Entity
@Table(name = "v1_specialty_award")
@DbComment("特长获奖记录")
public class SpecialtyAward extends Model {

    public static final int LEVEL_NATIONAL = 0; // 国家级
    public static final int LEVEL_PROVINCIAL = 1; // 省级
    public static final int LEVEL_CITY = 2; // 市级
    public static final int LEVEL_COUNTY = 3; // 县区级
    public static final int LEVEL_SCHOOL = 4; // 校级

    public static final int GRADE_FIRST = 0; // 一等奖
    public static final int GRADE_SECOND = 1; // 二等奖
    public static final int GRADE_THIRD = 2; // 三等奖
    public static final int GRADE_EXCELLENCE = 3; // 优秀奖
    public static final int GRADE_COLLECTIVE = 4; // 集体奖

    public static final int STATUS_PENDING = 0; // 待审核
    public static final int STATUS_APPROVED = 1; // 通过
    public static final int STATUS_REJECTED = 2; // 拒绝

    @Column(name = "org_id")
    @DbComment("机构ID")
    public long orgId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @DbComment("唯一标识")
    public long id;

    @Column(name = "student_id")
    @DbComment("学生ID")
    public long studentId;

    @Transient
    public Student student;

    @Column(name = "award_level")
    @DbComment("奖项级别") // 0-国家级,1-省级,2-市级,3-县区级,4-校级
    public int awardLevel;

    @Column(name = "award_grade")
    @DbComment("奖项等级") // 0-一等奖,1-二等奖,2-三等奖,3-优秀奖,4-集体奖
    public int awardGrade;

    @Column(name = "competition_name")
    @DbComment("竞赛名称")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String competitionName;

    @Column(name = "category")
    @DbComment("比赛类别")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String category;

    @Column(name = "award_score")
    @DbComment("奖项得分")
    public double awardScore;

    @Column(name = "status")
    @DbComment("审核状态") // 0-待审核,1-通过,2-拒绝
    public int status;

    @Column(name = "certificate_image")
    @DbComment("证书图片")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String certificateImage;

    @Column(name = "badge_awarded")
    @DbComment("授予徽章") // 星辰徽章/星河徽章
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String badgeAwarded;

    @Column(name = "award_date")
    @DbComment("获奖时间")
    public long awardDate;

    @Column(name = "create_time")
    @DbComment("创建时间")
    public long createTime;

    @Column(name = "update_time")
    @DbComment("更新时间")
    public long updateTime;

    @Column(name = "month_end_time")
    @DbComment("审核月结束时间")
    public long monthEndTime;

    @Transient
    public String studentName;


    public static Finder<Long, SpecialtyAward> find = new Finder<>(SpecialtyAward.class);



    /**
     * 单个奖项的完整处理：计算得分和徽章
     */
    public void processSingleAward() {
        // 1. 计算得分
        calculateAwardScore();

        // 2. 计算徽章
        calculateBadge();

        // 3. 保存记录
        this.update();

        // 4. 同步到学生表
        syncStudentSpecialtyScore(this.studentId);
    }

    /**
     * 计算单个奖项的得分
     */
    public double calculateAwardScore() {
        // 只有审核通过的记录才计算得分
        if (this.status != STATUS_APPROVED) {
            return 0.0;
        }

        // 检查级别和等级是否在有效范围内
        if (this.awardLevel < 0 || this.awardLevel >= SPECIALTY_SCORE_MATRIX.length ||
                this.awardGrade < 0 || this.awardGrade >= SPECIALTY_SCORE_MATRIX[0].length) {
            return 0.0;
        }

        // 根据矩阵计算得分
        double score = SPECIALTY_SCORE_MATRIX[this.awardLevel][this.awardGrade];

        // 根据学生评价方案调整上限
        if (this.student != null) {
            if (this.student.evaluationScheme == Student.SCHEME_A) {
                // 方案A：特长得分最高20分
                score = Math.min(score, Student.SPECIALTY_MAX_SCORE_A);
            } else {
                // 方案B：特长得分最高40分
                score = Math.min(score, Student.SPECIALTY_MAX_SCORE_B);
            }
        }
        this.setAwardScore(score);
        return score;
    }


    /**
     * 计算学生的特长总分（所有审核通过的奖项得分总和）
     */
    public static double calculateStudentTotalSpecialtyScore(Long studentId) {
        List<SpecialtyAward> awards = find.query()
                .where()
                .eq("student_id", studentId)
                .eq("status", STATUS_APPROVED)
                .findList();

        double totalScore = 0.0;
        for (SpecialtyAward award : awards) {
            totalScore += award.calculateAwardScore();
        }

        // 根据学生评价方案调整上限
        Student student = Student.find.byId(studentId);
        if (student != null) {
            if (student.evaluationScheme == Student.SCHEME_A) {
                totalScore = Math.min(totalScore, Student.SPECIALTY_MAX_SCORE_A);
            } else {
                totalScore = Math.min(totalScore, Student.SPECIALTY_MAX_SCORE_B);
            }
        }

        return totalScore;
    }


    /**
     * 同步学生的特长得分到学生表
     */
    public static void syncStudentSpecialtyScore(Long studentId) {
        double totalScore = calculateStudentTotalSpecialtyScore(studentId);
        Student student = Student.find.byId(studentId);

        if (student != null) {
            student.setSpecialtyScore(totalScore);
            student.update();
        }
    }



    /**
     * 计算徽章授予
     */
    public void calculateBadge() {
        if (this.status != STATUS_APPROVED) {
            this.badgeAwarded = null;
            return;
        }

        // 获取学生信息
        Student student = Student.find.byId(this.studentId);
        if (student == null) {
            this.badgeAwarded = null;
            return;
        }

        // 检查学业评定是否合格（学业得分不低于20分）
        boolean isAcademicQualified = student.academicScore >= ACADEMIC_PASS;

        // 检查习惯评定是否良好（习惯得分不低于30分）
        boolean isHabitGood = student.habitScore >= HABIT_PASS;

        // 只有学业合格且习惯良好才能获得徽章
        if (isAcademicQualified && isHabitGood) {
            if (this.awardLevel <= LEVEL_COUNTY) {
                // 市级以上获得星河徽章
                this.badgeAwarded = "敏行徽章";
            } else if ((this.awardLevel <= LEVEL_SCHOOL && student.getRewardRankGrade() > 0 && student.getRewardRankGrade() <= 50)) {
                // 校级获得星辰徽章
                this.badgeAwarded = "力行徽章";
            } else if(student.getRewardRankSchool() > 0 && student.getRewardRankSchool() <= 50){
                this.badgeAwarded = "敏行徽章";
            }
        } else {
            this.badgeAwarded = null;
        }
    }


    /**
     * 验证奖项数据有效性
     */
    public boolean validate() {
        if (this.awardLevel < LEVEL_NATIONAL || this.awardLevel > LEVEL_SCHOOL) {
            return false;
        }
        if (this.awardGrade < GRADE_FIRST || this.awardGrade > GRADE_COLLECTIVE) {
            return false;
        }
        if (this.status < STATUS_PENDING || this.status > STATUS_REJECTED) {
            return false;
        }
        return true;
    }


    /**
     * 计算月份截至时间
     */
    public void calculateEndMonthNew(){
        // 验证 createTime 是否有效
        if (this.createTime <= 0) {
            throw new IllegalArgumentException("createTime 不能为 0 或负数");
        }

        // MySQL DATETIME 范围: 1000-01-01 到 9999-12-31
        // 对应的时间戳范围
        long minValidTime = LocalDate.of(1000, 1, 1).atStartOfDay()
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long maxValidTime = LocalDate.of(9999, 12, 31).atStartOfDay()
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        if (this.createTime < minValidTime || this.createTime > maxValidTime) {
            throw new IllegalArgumentException("createTime 超出 MySQL 日期范围 (1000-9999年): " +
                    Instant.ofEpochMilli(this.createTime)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate());
        }

        try {
            LocalDate date = Instant.ofEpochMilli(this.createTime)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            YearMonth yearMonth = YearMonth.from(date);
            LocalDate lastDay = yearMonth.atEndOfMonth();

            // 计算月末时间戳
            long endOfMonthTime = lastDay.atTime(23, 59, 59, 999_999_999)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli();

            // 验证计算结果是否在有效范围内
            if (endOfMonthTime < minValidTime || endOfMonthTime > maxValidTime) {
                throw new IllegalArgumentException("计算出的 monthEndTime 超出 MySQL 日期范围: " +
                        Instant.ofEpochMilli(endOfMonthTime)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate());
            }

            this.monthEndTime = endOfMonthTime;
        } catch (Exception e) {
            throw new RuntimeException("计算 monthEndTime 时出错 - createTime: " + this.createTime +
                    ", 错误: " + e.getMessage(), e);
        }
    }


}