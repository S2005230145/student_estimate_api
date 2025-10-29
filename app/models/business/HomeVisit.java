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
@Table(name = "v1_home_visit")
@Translation("家访工作记录")
public class HomeVisit  extends Model {
    public static final int VISIT_NORMAL = 0; // 普访
    public static final int VISIT_KEY = 1; // 重点家访
    public static final int BASE_SCORE_VALUE = 10; // 基础分10分
    public static final int CASE_BONUS_MAX = 5; // 案例加分上限5分
    public static final int VIDEO_BONUS_MAX = 5; // 视频加分上限5分
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Translation("唯一标识")
    public long id;
    
    @Column(name = "teacher_id")
    @Translation("教师ID")
    public long teacherId;
    
    @Column(name = "class_id")
    @Translation("班级ID")
    public long classId;
    
    @Column(name = "student_id")
    @Translation("学生ID")
    public long studentId;
    
    @Column(name = "visit_type")
    @Translation("家访类型") // 0-普访,1-重点家访
    public int visitType;
    
    @Column(name = "record_content")
    @Translation("记录内容")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String recordContent;
    
    @Column(name = "case_study")
    @Translation("优秀案例")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String caseStudy;
    
    @Column(name = "video_evidence")
    @Translation("视频证据")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String videoEvidence;
    
    @Column(name = "base_score")
    @Translation("基础分")
    public int baseScore;
    
    @Column(name = "bonus_score")
    @Translation("加分")
    public int bonusScore;
    
    @Column(name = "total_score")
    @Translation("总分")
    public int totalScore;
    
    @Column(name = "status")
    @Translation("审核状态")
    public int status;
    
    @Column(name = "visit_time")
    @Translation("家访时间")
    public long visitTime;
    
    @Column(name = "create_time")
    @Translation("创建时间")
    public long createTime;

    public static Finder<Long, HomeVisit> find = new Finder<>(HomeVisit.class);
}