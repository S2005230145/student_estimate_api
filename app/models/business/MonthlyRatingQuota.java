package models.business;

import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.DbComment;
import jakarta.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "v1_monthly_rating_quota")
@DbComment("每月评分额度表")
public class MonthlyRatingQuota extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long id;

    @Column(name = "org_id")
    @DbComment("机构ID")
    public long orgId;

    @Column(name = "class_id")
    @DbComment("班级ID")
    public long classId;

    @Column(name = "evaluator_id")
    @DbComment("教师ID/评价者ID")
    public long evaluatorId;

    @Column(name = "role_type")
    @DbComment("身份：head/basic/other/parent/adm")
    public String roleType;

    @Column(name = "month_key")
    @DbComment("月份yyyy-MM")
    public String monthKey;

    @Column(name = "rating_amount")
    @DbComment("当月已用额度")
    public double ratingAmount;

    @Column(name = "cap_value")
    @DbComment("当月上限冗余存储")
    public double capValue;

    @Column(name = "create_time")
    @DbComment("创建时间")
    public long createTime;

    @Column(name = "update_time")
    @DbComment("更新时间")
    public long updateTime;

    public static Finder<Long, MonthlyRatingQuota> find = new Finder<>(MonthlyRatingQuota.class);
}
