package models.business;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.DB;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.DbComment;
import jakarta.persistence.*;
import lombok.Data;
import myannotation.EscapeHtmlAuthoritySerializer;
import myannotation.Translation;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "v1_habit_record")
@DbComment("习惯评价记录")
public class HabitRecord  extends Model {
    public static final int HABIT_STUDY = 0; // 学习习惯
    public static final int HABIT_LIFE = 1; // 生活习惯
    public static final int HABIT_LABOR = 2; // 劳动习惯
    public static final int HABIT_BEHAVIOR = 3; // 行为习惯

    public static final double BASE_SCORE = 15.0; // 基础分
    public static final double MAX_SCORE = 40.0; // 最高分
    public static final double MIN_SCORE = 0.0; // 最低分

    public static final double TEACHER_SCORE_MAX = 1.0; // 教师评分范围0.5-1分
    public static final double TEACHER_SCORE_MIN = 0.5;
    public static final double HEAD_TEACHER_SCORE_MAX = 2.0; // 班主任评分范围1-2分
    public static final double HEAD_TEACHER_SCORE_MIN = 1.0; // 班主任评分范围1-2分
    public static final double PARENT_SCORE_MAX = 1.0; // 家长评分范围0.5-1分
    public static final double PARENT_SCORE_MIN = 0.5; // 家长评分范围0.5-1分

    public static final double MOUTH_PARENT_MAX_SCORE = 10.0; //家长习惯月封顶
    public static final double MOUTH_HEADER_TEACHER_MAX_SCORE = 300.0; //班主任习惯月封顶
    public static final double MOUTH_BASIC_TEACHER_MAX_SCORE = 200.0; // 基础学科老师（语数英）习惯月封顶
    public static final double MOUTH_OTHER_TEACHER_PARENT_MAX_SCORE = 50.0; //艺体学科习惯月封顶

    //未结算
    public static final int STATUS_UNSETTLED = 0;
    //已结算
    public static final int STATUS_SETTLED = 1;

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
    
    @Column(name = "habit_type")
    @DbComment("习惯类型") // 0-学习习惯,1-生活习惯,2-劳动习惯,3-行为习惯
    public int habitType;
    
    @Column(name = "evaluator_type")
    @DbComment("评价者类型") // teacher,head_teacher,parent
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String evaluatorType;
    
    @Column(name = "evaluator_id")
    @DbComment("评价者ID")
    public long evaluatorId;
    
    @Column(name = "score_change")
    @DbComment("分数变化")
    public double scoreChange;
    
    @Column(name = "description")
    @DbComment("行为描述")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String description;
    
    @Column(name = "evidence_image")
    @DbComment("证据图片")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String evidenceImage;
    
    @Column(name = "record_time")
    @DbComment("记录时间")
    public long recordTime;
    
    @Column(name = "create_time")
    @DbComment("创建时间")
    public long createTime;

    @Column(name = "month_end_time")
    @DbComment("对应月末时间")
    public Long monthEndTime;

    @Column(name = "status")
    @DbComment("状态")
    public Integer status;

    public static Finder<Long, HabitRecord> find = new Finder<>(HabitRecord.class);


    /**
     * 数据验证（根据评价者类型）
     */
    public void validate(String evaluatorRole) {
        List<String> errors = new ArrayList<>();

        // 验证学生ID
        if (this.studentId <= 0) {
            errors.add("学生ID不能为空");
        }

        // 验证习惯类型
        if (this.habitType < HABIT_STUDY || this.habitType > HABIT_BEHAVIOR) {
            errors.add("习惯类型无效");
        }

        // 验证分数变化范围
        switch (evaluatorRole) {
            case "科任教师" -> {
                if (this.scoreChange < TEACHER_SCORE_MIN || this.scoreChange > TEACHER_SCORE_MAX) {
                    errors.add("科任教师评分范围应为" + TEACHER_SCORE_MIN + "~" + TEACHER_SCORE_MAX + "分");
                }
                this.evaluatorType = "teacher";
            }
            case "班主任" -> {
                if (this.scoreChange < HEAD_TEACHER_SCORE_MIN || this.scoreChange > HEAD_TEACHER_SCORE_MAX) {
                    errors.add("班主任评分范围应为" + HEAD_TEACHER_SCORE_MIN + "~" + HEAD_TEACHER_SCORE_MAX + "分");
                }
                this.evaluatorType = "head_teacher";
            }
            case "家长" -> {
                if (this.scoreChange < PARENT_SCORE_MIN || this.scoreChange > PARENT_SCORE_MAX) {
                    errors.add("家长评分范围应为" + PARENT_SCORE_MIN + "~" + PARENT_SCORE_MAX + "分");
                }
                this.evaluatorType = "parent";
            }
            default -> errors.add("未知的评价者类型");
        }

        // 验证描述不能为空
        if (this.description == null || this.description.trim().isEmpty()) {
            errors.add("行为描述不能为空");
        }

        // 验证记录时间
        if (this.recordTime <= 0) {
            errors.add("记录时间无效");
        }

        if (!errors.isEmpty()) {
            throw new RuntimeException(String.join("; ", errors));
        }
    }

    /**
     * 计算学生习惯得分
     */
    public void calculateStudentHabitScore() {
        // 只有科任教师和班主任的记录才计算得分
        if (!"teacher".equals(this.evaluatorType) && !"head_teacher".equals(this.evaluatorType)) {
            return;
        }

        // 获取学生当前的习惯得分
        Student student = Student.find.byId(this.studentId);
        if (student == null) {
            throw new RuntimeException("学生不存在，ID: " + this.studentId);
        }

        // 计算新的习惯得分
        double newHabitScore = student.habitScore + this.scoreChange;

        // 确保得分在合理范围内
        newHabitScore = Math.max(MIN_SCORE, Math.min(newHabitScore, MAX_SCORE));

        // 更新学生习惯得分
        student.setHabitScore(newHabitScore);
        student.update();
    }

    /**
     * 重新计算单个学生的习惯得分
     */
    public static void recalculateStudentHabitScore(Long studentId) {
        // 获取该学生所有教师和班主任的评价记录
        List<HabitRecord> records = find.query()
                .where()
                .eq("student_id", studentId)
                .in("evaluator_type", "teacher", "head_teacher")
                .findList();

        // 重新计算习惯得分
        calculateStudentTotalHabitScore(studentId, records);

    }


    /**
     * 计算学生总习惯得分
     */
    private static void calculateStudentTotalHabitScore(Long studentId, List<HabitRecord> records) {
        Student student = Student.find.byId(studentId);
        if (student == null) {
            return;
        }

        // 计算总得分变化（只计算教师和班主任的记录）
        double totalScoreChange = records.stream()
                .mapToDouble(r -> r.scoreChange)
                .sum();

        // 计算新的习惯得分
        double newHabitScore = BASE_SCORE + totalScoreChange;

        // 确保得分在合理范围内
        newHabitScore = Math.max(MIN_SCORE, Math.min(newHabitScore, MAX_SCORE));

        // 更新学生习惯得分
        student.setHabitScore(newHabitScore);
        SchoolClass.recalcSpecialtyTotalScoreById(student.classId);
        student.update();
    }

    /**
     * 数据验证
     * 每月当前评级人所在的这个班级是否有评价额度
     */
    public void validate(Long evaluatorId,Long classId,Double scoreChange) {
        List<String> errors = new ArrayList<>();

        MonthlyRatingQuota quota = MonthlyRatingQuota.find.query()
                .where()
                .eq("teacher_id", evaluatorId)
                .eq("class_id", classId)
                .findOne();

        //scoreChange 取绝对值
        scoreChange = Math.abs(scoreChange);
        if (quota == null) {
            errors.add("当前评价人所在的班级没有分配评价额度");
        }

        if (quota != null && quota.ratingAmount < scoreChange) {
            errors.add("当前评价人所在的班级的评价额度不足");
        }

        if (!errors.isEmpty()) {
            throw new RuntimeException(String.join("; ", errors));
        }

    }

    /**
     * 计算月份截至时间
     */
    public void calculateEndMonth(){
        long ct = this.createTime;
        LocalDate date = Instant.ofEpochMilli(ct)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        YearMonth yearMonth = YearMonth.from(date);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        this.monthEndTime= lastDay.atTime(23, 59, 59, 999_999_999)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();;
    }

}