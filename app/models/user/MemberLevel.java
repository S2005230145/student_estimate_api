package models.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import myannotation.EscapeHtmlSerializer;

/**
 * 用户等级类
 */
@Entity
@Table(name = "v1_member_level")
public class MemberLevel extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(name = "need_score")
    public long needScore;//该等级所需积分

    @Column(name = "level")
    public int level;//用户等级

    @Column(name = "level_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String levelName;//用户等级名字


    @Column(name = "sketch")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String sketch;//简述

    @Column(name = "order_discount")
    public int orderDiscount;
    @Column(name = "coupon")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String couponGift;//赠送优惠卷 集合 "[{"couponId":22,"couponName":"满100-20券""count":1},{"couponId":33,"couponName":"青岛啤酒减2元券""count":4}]"

    @Column(name = "mail_free")
    public int mailFree;//邮费减免

    @Column(name = "update_time")
    public long updateTime;//更新时间

    @Column(name = "create_time")
    public long createdTime;//创建时间

    @Column(name = "org_id")
    public long orgId;

    public static Finder<Long, MemberLevel> find = new Finder<>(MemberLevel.class);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getNeedScore() {
        return needScore;
    }

    public void setNeedScore(long needScore) {
        this.needScore = needScore;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getSketch() {
        return sketch;
    }

    public void setSketch(String sketch) {
        this.sketch = sketch;
    }

    public int getOrderDiscount() {
        return orderDiscount;
    }

    public void setOrderDiscount(int orderDiscount) {
        this.orderDiscount = orderDiscount;
    }

    public String getCouponGift() {
        return couponGift;
    }

    public void setCouponGift(String couponGift) {
        this.couponGift = couponGift;
    }

    public int getMailFree() {
        return mailFree;
    }

    public void setMailFree(int mailFree) {
        this.mailFree = mailFree;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }
}
