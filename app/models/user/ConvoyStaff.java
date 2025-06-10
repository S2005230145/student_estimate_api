package models.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import myannotation.EscapeHtmlSerializer;

/**
 * 员工
 * Created by win7 on 2016/6/7.
 */
@Entity
@Table(name = "v1_convoy_staff")
public class ConvoyStaff extends Model {
    private static final long serialVersionUID = 8880942309217592353L;
    public static final int MEMBER_TYPE_NORMAL = 1;
    public static final int MEMBER_TYPE_ANONYMOUS = 2;
    /**
     * 用户的状态：正常
     */
    public static final int MEMBER_STATUS_NORMAL = 1;
    /**
     * 用户的状态：被锁定
     */
    public static final int MEMBER_STATUS_LOCK = 2;


    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;


    @Column(name = "status")
    public int status;//用户状态 1正常2锁定

    @Column(name = "real_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String realName;//真实姓名

    @Column(name = "phone_number")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String phoneNumber;//手机号

    @Column(name = "card_no")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String cardNo;
    @Column(name = "convoy_id")
    public long convoyId;
    @Column(name = "convoy_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String convoyName;

    @Column(name = "dept_id")
    public long deptId;
    @Column(name = "dept_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String deptName;

    @Column(name = "monthly_balance")
    public long monthlyBalance;
    @Column(name = "max_charge_count")
    public long maxChargeCount;

    @Column(name = "filter")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String filter;

    @Column(name = "update_time")
    public long updateTime;//创建时间

    @Column(name = "create_time")
    public long createTime;//创建时间


    public static Finder<Long, ConvoyStaff> find = new Finder<>(ConvoyStaff.class);

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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public long getConvoyId() {
        return convoyId;
    }

    public void setConvoyId(long convoyId) {
        this.convoyId = convoyId;
    }

    public String getConvoyName() {
        return convoyName;
    }

    public void setConvoyName(String convoyName) {
        this.convoyName = convoyName;
    }

    public long getMonthlyBalance() {
        return monthlyBalance;
    }

    public void setMonthlyBalance(long monthlyBalance) {
        this.monthlyBalance = monthlyBalance;
    }

    public long getMaxChargeCount() {
        return maxChargeCount;
    }

    public void setMaxChargeCount(long maxChargeCount) {
        this.maxChargeCount = maxChargeCount;
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


    public long getDeptId() {
        return deptId;
    }

    public void setDeptId(long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
