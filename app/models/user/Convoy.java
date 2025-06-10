package models.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import myannotation.EscapeHtmlSerializer;

/**
 * 车队管理
 * Created by win7 on 2016/6/7.
 */
@Entity
@Table(name = "v1_convoy")
public class Convoy extends Model {
    private static final long serialVersionUID = 8880942309217592353L;

    /**
     * 用户的状态：正常
     */
    public static final int STATUS_NORMAL = 1;
    /**
     * 用户的状态：被锁定
     */
    public static final int STATUS_LOCK = 2;


    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "org_id")
    public long orgId;

    @Column(name = "org_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String orgName;

    @Column(name = "status")
    public int status;//用户状态 1正常2锁定

    @Column(name = "name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String name;

    @Column(name = "convoy_type")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String convoyType;

    @Column(name = "is_auth")
    public boolean isAuth;

    @Column(name = "license_no")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String licenseNo;

    @Column(name = "card_no")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String cardNo;

    @Column(name = "admin_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String adminName;
    @Column(name = "admin_phone_number")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String adminPhoneNumber;


    @Column(name = "left_balance")
    public long leftBalance;

    @Column(name = "share_group_balance")
    public boolean shareGroupBalance;

    @Column(name = "electric_price_discount")
    public int electricPriceDiscount;

    @Column(name = "occupy_price_discount")
    public int occupyPriceDiscount;

    @Column(name = "filter")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String filter;

    @Column(name = "update_time")
    public long updateTime;//创建时间

    @Column(name = "create_time")
    public long createTime;//创建时间


    public static Finder<Long, Convoy> find = new Finder<>(Convoy.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConvoyType() {
        return convoyType;
    }

    public void setConvoyType(String convoyType) {
        this.convoyType = convoyType;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminPhoneNumber() {
        return adminPhoneNumber;
    }

    public void setAdminPhoneNumber(String adminPhoneNumber) {
        this.adminPhoneNumber = adminPhoneNumber;
    }

    public long getLeftBalance() {
        return leftBalance;
    }

    public void setLeftBalance(long leftBalance) {
        this.leftBalance = leftBalance;
    }

    public boolean isShareGroupBalance() {
        return shareGroupBalance;
    }

    public void setShareGroupBalance(boolean shareGroupBalance) {
        this.shareGroupBalance = shareGroupBalance;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
