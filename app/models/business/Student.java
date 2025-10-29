package models.business;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import lombok.Data;
import myannotation.EscapeHtmlAuthoritySerializer;
import myannotation.Translation;

@Data
@Entity
@Table(name = "v1_student")
@Translation("学生信息")
public class Student extends Model {

    public static final int SCHEME_A = 0; // 方案A
    public static final int SCHEME_B = 1; // 方案B
    public static final double ACADEMIC_MAX_SCORE_A = 40.0; // 方案A学业满分
    public static final double ACADEMIC_MAX_SCORE_B = 20.0; // 方案B学业满分
    public static final double SPECIALTY_MAX_SCORE_A = 20.0; // 方案A特长满分
    public static final double SPECIALTY_MAX_SCORE_B = 40.0; // 方案B特长满分
    public static final double HABIT_MAX_SCORE = 40.0; // 习惯满分
    public static final double TOTAL_MAX_SCORE = 100.0; // 总分满分

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Translation("唯一标识")
    public long id;

    @Column(name = "student_number")
    @Translation("学号")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String studentNumber;

    @Column(name = "name")
    @Translation("学生姓名")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String name;

    @Column(name = "class_id")
    @Translation("班级ID")
    public long classId;

    @Column(name = "grade")
    @Translation("年级")
    public int grade;

    @Column(name = "evaluation_scheme")
    @Translation("评价方案") // 0-方案A, 1-方案B
    public int evaluationScheme;

    @Column(name = "class_average_score")
    @Translation("班级平均分")
    public double classAverageScore;

    @Column(name = "academic_score")
    @Translation("学业得分")
    public double academicScore;

    @Column(name = "specialty_score")
    @Translation("特长得分")
    public double specialtyScore;

    @Column(name = "habit_score")
    @Translation("习惯得分")
    public double habitScore;

    @Column(name = "total_score")
    @Translation("总分")
    public double totalScore;

    @Column(name = "badges")
    @Translation("获得徽章")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String badges;

    @Column(name = "create_time")
    @Translation("创建时间")
    public long createTime;

    @Column(name = "update_time")
    @Translation("更新时间")
    public long updateTime;

    public static Finder<Long, Student> find = new Finder<>(Student.class);

}