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
@Table(name = "v1_academic_record")
@Translation("学业成绩记录")
public class AcademicRecord extends Model {

    public static final int EXAM_MIDTERM = 0; // 期中考试
    public static final int EXAM_FINAL = 1; // 期末考试
    public static final double PASS_SCORE = 60.0; // 及格分数
    public static final int TOP_RANKING = 50; // 前50名
    public static final double BASE_SCORE = 20.0; // 保底分
    public static final double EXCELLENT_SCORE = 40.0; // 优秀分
    public static final double PROGRESS_SCORE = 30.0; // 进步分
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Translation("唯一标识")
    public long id;
    
    @Column(name = "student_id")
    @Translation("学生ID")
    public long studentId;
    
    @Column(name = "exam_type")
    @Translation("考试类型") // 0-期中, 1-期末
    public int examType;
    
    @Column(name = "chinese_score")
    @Translation("语文成绩")
    public double chineseScore;
    
    @Column(name = "math_score")
    @Translation("数学成绩")
    public double mathScore;
    
    @Column(name = "english_score")
    @Translation("英语成绩")
    public double englishScore;
    
    @Column(name = "average_score")
    @Translation("平均分")
    public double averageScore;
    
    @Column(name = "grade_ranking")
    @Translation("年级排名")
    public int gradeRanking;
    
    @Column(name = "class_ranking")
    @Translation("班级排名")
    public int classRanking;
    
    @Column(name = "progress_ranking")
    @Translation("进步排名")
    public int progressRanking;
    
    @Column(name = "calculated_score")
    @Translation("计算得分")
    public double calculatedScore;
    
    @Column(name = "badge_awarded")
    @Translation("授予徽章") // 星辰徽章/星火徽章
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String badgeAwarded;
    
    @Column(name = "exam_date")
    @Translation("考试时间")
    public long examDate;
    
    @Column(name = "create_time")
    @Translation("创建时间")
    public long createTime;

    public static Finder<Long, AcademicRecord> find = new Finder<>(AcademicRecord.class);
}