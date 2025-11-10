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
    public static final double SATISFACTION_THRESHOLD = 70.0; // 满意率阈值70%
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
}