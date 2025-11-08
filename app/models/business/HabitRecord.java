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

    public static final double BASE_SCORE = 20.0; // 基础分
    public static final double MAX_SCORE = 40.0; // 最高分
    public static final double MIN_SCORE = 0.0; // 最低分

    public static final double TEACHER_SCORE_MAX = 1.0; // 教师评分范围0.5-1分
    public static final double TEACHER_SCORE_MIN = 0.5;
    public static final double HEAD_TEACHER_SCORE_MAX = 2.0; // 班主任评分范围1-2分
    public static final double HEAD_TEACHER_SCORE_MIN = 1.0; // 班主任评分范围1-2分
    public static final double PARENT_SCORE_MAX = 1.0; // 家长评分范围0.5-1分
    public static final double PARENT_SCORE_MIN = 0.5; // 家长评分范围0.5-1分

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
        student.update();
    }
}