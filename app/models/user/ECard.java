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
@Table(name = "v1_ecard")
public class ECard extends Model {
    private static final long serialVersionUID = 8880942309217592953L;

    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_ANONYMOUS = 2;

    public static final int TARGET_STAFF = 10;
    public static final int TARGET_CONVOY = 20;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;


    @Column(name = "status")
    public int status;//用户状态 1正常2锁定

    @Column(name = "card_no")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String cardNo;//真实姓名

    @Column(name = "card_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String cardName;//手机号

    @Column(name = "card_type")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String cardType;

    @Column(name = "left_balance")
    public long leftBalance;
    @Column(name = "target_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String targetName;
    @Column(name = "target_id")
    public long targetId;
    @Column(name = "target_type")
    public int targetType;

    @Column(name = "staff_phone_number")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String staffPhoneNumber;

    @Column(name = "filter")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String filter;

    @Column(name = "update_time")
    public long updateTime;//创建时间

    @Column(name = "create_time")
    public long createTime;//创建时间


    public static Finder<Long, ECard> find = new Finder<>(ECard.class);

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

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public long getLeftBalance() {
        return leftBalance;
    }

    public void setLeftBalance(long leftBalance) {
        this.leftBalance = leftBalance;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public int getTargetType() {
        return targetType;
    }

    public void setTargetType(int targetType) {
        this.targetType = targetType;
    }

    public String getStaffPhoneNumber() {
        return staffPhoneNumber;
    }

    public void setStaffPhoneNumber(String staffPhoneNumber) {
        this.staffPhoneNumber = staffPhoneNumber;
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
}
