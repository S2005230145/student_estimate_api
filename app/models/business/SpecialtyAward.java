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
@Table(name = "v1_specialty_award")
@Translation("特长获奖记录")
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
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Translation("唯一标识")
    public long id;
    
    @Column(name = "student_id")
    @Translation("学生ID")
    public long studentId;
    
    @Column(name = "award_level")
    @Translation("奖项级别") // 0-国家级,1-省级,2-市级,3-县区级,4-校级
    public int awardLevel;
    
    @Column(name = "award_grade")
    @Translation("奖项等级") // 0-一等奖,1-二等奖,2-三等奖,3-优秀奖,4-集体奖
    public int awardGrade;
    
    @Column(name = "competition_name")
    @Translation("竞赛名称")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String competitionName;
    
    @Column(name = "category")
    @Translation("比赛类别")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String category;
    
    @Column(name = "award_score")
    @Translation("奖项得分")
    public double awardScore;
    
    @Column(name = "status")
    @Translation("审核状态") // 0-待审核,1-通过,2-拒绝
    public int status;
    
    @Column(name = "certificate_image")
    @Translation("证书图片")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String certificateImage;
    
    @Column(name = "badge_awarded")
    @Translation("授予徽章") // 星辰徽章/星河徽章
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String badgeAwarded;
    
    @Column(name = "create_time")
    @Translation("创建时间")
    public long createTime;

    public static Finder<Long, SpecialtyAward> find = new Finder<>(SpecialtyAward.class);
}