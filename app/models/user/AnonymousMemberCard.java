package models.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import myannotation.EscapeHtmlSerializer;

/**
 * 用户类
 * Created by win7 on 2016/6/7.
 */
@Entity
@Table(name = "v1_anonymous_member_card")
public class AnonymousMemberCard extends Model {

    public static final int STATUS_ACTIVATED = 1;
    public static final int STATUS_CONVERTED = 2;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "card_no")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String cardNo;

    @Column(name = "status")
    public int status;

    @Column(name = "balance")
    public long balance;

    @Column(name = "real_pay")
    public long realPay;

    @Column(name = "give")
    public long give;

    @Column(name = "create_time")
    public long createTime;

    @Column(name = "operator_id")
    public long operatorId;

    @Column(name = "operator_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String operatorName;

    @Column(name = "bind_uid")
    public long bindUid;

    @Column(name = "bind_user_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String bindUserName;

    public static Finder<Long, AnonymousMemberCard> find = new Finder<>(AnonymousMemberCard.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getRealPay() {
        return realPay;
    }

    public void setRealPay(long realPay) {
        this.realPay = realPay;
    }

    public long getGive() {
        return give;
    }

    public void setGive(long give) {
        this.give = give;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public long getBindUid() {
        return bindUid;
    }

    public void setBindUid(long bindUid) {
        this.bindUid = bindUid;
    }

    public String getBindUserName() {
        return bindUserName;
    }

    public void setBindUserName(String bindUserName) {
        this.bindUserName = bindUserName;
    }
}
