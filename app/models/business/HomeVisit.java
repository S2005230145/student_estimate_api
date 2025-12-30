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
@Table(name = "v1_home_visit")
@DbComment("家访工作记录")
public class HomeVisit  extends Model {
    public static final int VISIT_NORMAL = 0; // 普访
    public static final int VISIT_KEY = 1; // 重点家访
    public static final int BASE_SCORE_VALUE = 10; // 基础分10分
    public static final int CASE_BONUS_MAX = 5; // 案例加分上限5分
    public static final int VIDEO_BONUS_MAX = 5; // 视频加分上限5分
    public static final int STATUS_NEED_REVIEW = 0; // 待评价
    public static final int STATUS_APPROVED = 10; // 已评价

    @Column(name = "org_id")
    @DbComment("机构ID")
    public long orgId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @DbComment("唯一标识")
    public long id;
    
    @Column(name = "teacher_id")
    @DbComment("教师ID")
    public long teacherId;
    
    @Column(name = "class_id")
    @DbComment("班级ID")
    public long classId;
    
    @Column(name = "student_id")
    @DbComment("学生ID")
    public long studentId;
    
    @Column(name = "visit_type")
    @DbComment("家访类型") // 0-普访,1-重点家访
    public int visitType;
    
    @Column(name = "record_content")
    @DbComment("记录内容")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String recordContent;
    
    @Column(name = "case_study")
    @DbComment("优秀案例")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String caseStudy;

    @Column(name = "case_level")
    @DbComment("案例评价等级")
    public String caseLevel;
    
    @Column(name = "video_evidence")
    @DbComment("视频证据")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String videoEvidence;

    @Column(name = "video_level")
    @DbComment("视频评价等级")
    public String videoLevel;

    @Column(name = "base_score")
    @DbComment("基础分")
        public int baseScore;
    
    @Column(name = "bonus_score")
    @DbComment("加分")
    public int bonusScore;
    
    @Column(name = "total_score")
    @DbComment("总分")
    public int totalScore;
    
    @Column(name = "status")
    @DbComment("状态")// 0-待审核,1-通过,2-拒绝
    public int status;

    @Column(name = "visit_time")
    @DbComment("家访时间")
    public long visitTime;
    
    @Column(name = "create_time")
    @DbComment("创建时间")
    public long createTime;

    @Transient
    public String teacherName;

    @Transient
    public String className;

    @Transient
    public String studentNumber;

    @Transient
    public String studentName;

    public static Finder<Long, HomeVisit> find = new Finder<>(HomeVisit.class);

    public void calcBonusScore() {
        int caseScore = 0;
        int videoScore = 0;
        switch (this.caseLevel) {
            case "优秀":
                caseScore = 5;
                break;
            case "良好":
                caseScore = 3;
                break;
            case "合格":
                caseScore = 2;
                break;
            case "不合格":
                break;
        }
        switch (this.videoLevel) {
            case "优秀":
                videoScore = 5;
                break;
            case "良好":
                videoScore = 3;
                break;
            case "合格":
                videoScore = 2;
                break;
            case "不合格":
                break;
        }
        this.setBonusScore(caseScore + videoScore);
    }

    public void calcTotalScore() {
        this.setTotalScore(this.baseScore + this.bonusScore);
    }
}