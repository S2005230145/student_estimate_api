package models.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import myannotation.EscapeHtmlSerializer;

/**
 * 电卡
 */
@Entity
@Table(name = "v1_vin_apply")
public class VinApply extends Model {
    private static final long serialVersionUID = 8880942309217592953L;
    public static final int STATUS_APPLYING = 10;
    public static final int STATUS_APPROVE = 20;
    public static final int STATUS_DENY = -10;


    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "org_id")
    public long orgId;

    @Column(name = "status")
    public int status;

    @Column(name = "vin")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String vin;

    @Column(name = "id_card_front")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String idCardFront;
    @Column(name = "id_card_back")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String idCardBack;
    @Column(name = "driver_license_card")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String driverLicenseCard;
    @Column(name = "uid")
    public long userId;

    @Column(name = "user_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String userName;
    @Column(name = "operator_uid")
    public long operatorUid;

    @Column(name = "operator_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String operatorName;

    @Column(name = "reason")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String reason;

    @Column(name = "update_time")
    public long updateTime;//创建时间

    @Column(name = "create_time")
    public long createTime;//创建时间


    public static Finder<Long, VinApply> find = new Finder<>(VinApply.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getIdCardFront() {
        return idCardFront;
    }

    public void setIdCardFront(String idCardFront) {
        this.idCardFront = idCardFront;
    }

    public String getIdCardBack() {
        return idCardBack;
    }

    public void setIdCardBack(String idCardBack) {
        this.idCardBack = idCardBack;
    }

    public String getDriverLicenseCard() {
        return driverLicenseCard;
    }

    public void setDriverLicenseCard(String driverLicenseCard) {
        this.driverLicenseCard = driverLicenseCard;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getOperatorUid() {
        return operatorUid;
    }

    public void setOperatorUid(long operatorUid) {
        this.operatorUid = operatorUid;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
}
