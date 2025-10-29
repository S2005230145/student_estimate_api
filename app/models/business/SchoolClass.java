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
@Table(name = "v1_school_class")
@Translation("班级信息")
public class SchoolClass  extends Model {

    public static final double ACADEMIC_WEIGHT = 0.3; // 学业权重30%
    public static final double SPECIALTY_WEIGHT = 0.2; // 特长权重20%
    public static final double ROUTINE_WEIGHT = 0.3; // 常规权重30%
    public static final double HOME_VISIT_WEIGHT = 0.2; // 家访权重20%
    public static final double SATISFACTION_THRESHOLD = 70.0; // 满意率阈值70%
    public static final double TOTAL_MAX_SCORE = 100.0; // 班级总分满分
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Translation("唯一标识")
    public long id;
    
    @Column(name = "class_name")
    @Translation("班级名称")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String className;
    
    @Column(name = "grade")
    @Translation("年级")
    public int grade;
    
    @Column(name = "head_teacher_id")
    @Translation("班主任ID")
    public long headTeacherId;
    
    @Column(name = "academic_score")
    @Translation("学业得分")
    public double academicScore;
    
    @Column(name = "specialty_score")
    @Translation("特长得分")
    public double specialtyScore;
    
    @Column(name = "routine_score")
    @Translation("常规得分")
    public double routineScore;
    
    @Column(name = "home_visit_score")
    @Translation("家访得分")
    public double homeVisitScore;
    
    @Column(name = "total_score")
    @Translation("总分")
    public double totalScore;
    
    @Column(name = "disqualified")
    @Translation("一票否决")
    public boolean disqualified;
    
    @Column(name = "honor_title")
    @Translation("荣誉称号") // 星河班级/星辰班级
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String honorTitle;
    
    @Column(name = "create_time")
    @Translation("创建时间")
    public long createTime;

    public static Finder<Long, SchoolClass> find = new Finder<>(SchoolClass.class);
}