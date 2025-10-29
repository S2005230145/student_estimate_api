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
@Table(name = "v1_badge_record")
@Translation("徽章授予记录")
public class BadgeRecord extends Model {
    public static final String BADGE_GALAXY = "星河徽章"; // 优秀
    public static final String BADGE_STAR = "星辰徽章"; // 进步
    public static final String BADGE_SPARK = "星火徽章"; // 学业进步
    public static final String BADGE_TRUTH = "求真徽章"; // 习惯优秀

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Translation("唯一标识")
    public long id;

    @Column(name = "student_id")
    @Translation("学生ID")
    public long studentId;

    @Column(name = "badge_type")
    @Translation("徽章类型") // 星辰徽章/星河徽章/星火徽章/求真徽章
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String badgeType;

    @Column(name = "award_reason")
    @Translation("授予原因")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String awardReason;

    @Column(name = "award_time")
    @Translation("授予时间")
    public long awardTime;

    @Column(name = "award_period")
    @Translation("授予周期") // 期中/期末/月度
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String awardPeriod;

    @Column(name = "create_time")
    @Translation("创建时间")
    public long createTime;

    public static Finder<Long, BadgeRecord> find = new Finder<>(BadgeRecord.class);

}