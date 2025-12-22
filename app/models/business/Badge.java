package models.business;

import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.DbComment;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "v1_badge")
@DbComment("徽章表")
public class Badge extends Model {

    @Column(name = "org_id")
    @DbComment("机构ID")
    public long orgId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @DbComment("唯一标识")
    public long id;

    @Column(name = "badge_id")
    @DbComment("所属徽章类型")
    public int badgeId;

    @Column(name = "badge_name")
    @DbComment("徽章名称")
    public String badgeName;

    @Column(name = "badge_image")
    @DbComment("徽章图片")
    public String badgeImage;

    @Column(name = "description")
    @DbComment("描述")
    public String description;

    @Column(name = "active")
    @DbComment("是否启用")
    public boolean active;

    @Column(name = "create_time")
    @DbComment("创建时间")
    public long createTime;

    public static Finder<Long, Badge> find = new Finder<>(Badge.class);
}
