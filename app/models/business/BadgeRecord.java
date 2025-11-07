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
@Table(name = "v1_badge_record")
@DbComment("徽章授予记录")
public class BadgeRecord extends Model {
    public static final String BADGE_GALAXY = "星河徽章"; // 优秀
    public static final String BADGE_STAR = "星辰徽章"; // 进步
    public static final String BADGE_SPARK = "星火徽章"; // 学业进步
    public static final String BADGE_TRUTH = "求真徽章"; // 习惯优秀

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @DbComment("唯一标识")
    public long id;

    @Column(name = "student_id")
    @DbComment("学生ID")
    public long studentId;

    @Column(name = "badge_type")
    @DbComment("徽章类型") // 星辰徽章/星河徽章/星火徽章/求真徽章
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String badgeType;

    @Column(name = "award_reason")
    @DbComment("授予原因")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String awardReason;

    @Column(name = "award_time")
    @DbComment("授予时间")
    public long awardTime;

    @Column(name = "award_period")
    @DbComment("授予周期") // 期中/期末/月度
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String awardPeriod;

    @Column(name = "create_time")
    @DbComment("创建时间")
    public long createTime;

    public static Finder<Long, BadgeRecord> find = new Finder<>(BadgeRecord.class);

}