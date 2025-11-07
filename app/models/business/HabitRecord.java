package models.business;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.DbComment;
import jakarta.persistence.*;
import lombok.Data;
import myannotation.EscapeHtmlAuthoritySerializer;
import myannotation.Translation;

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

    public static final double TEACHER_SCORE_RANGE = 1.0; // 教师评分范围0.5-1分
    public static final double HEAD_TEACHER_SCORE_RANGE = 2.0; // 班主任评分范围1-2分
    public static final double PARENT_SCORE_RANGE = 1.0; // 家长评分范围0.5-1分
    
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
}