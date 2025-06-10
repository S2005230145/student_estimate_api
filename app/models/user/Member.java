package models.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;
import play.data.validation.Constraints;

import jakarta.persistence.*;

/**
 * 用户类
 * Created by win7 on 2016/6/7.
 */
@Entity
@Table(name = "v1_member")
public class Member extends Model {
    private static final long serialVersionUID = 8880942309217592333L;
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

    public static final int MEMBER_STATUS_DELETE = -10;
    //1普通会员，2高级会员，3钻石会员，4至尊会员
    public static final int LEVEL_0 = 0;
    public static final int LEVEL_1 = 1;
    public static final int LEVEL_2 = 2;
    public static final int LEVEL_3 = 3;
    public static final int LEVEL_4 = 4;

    public static final int USER_TYPE_PUBLIC = 1;//public user
    public static final int USER_TYPE_STAFF = 10;//员工
    public static final int USER_TYPE_COOPERATOR = 20;//合作商

    public static final int RECEIVER_STATUS_TO_ADD = 10;
    public static final int RECEIVER_ADDED = 20;

    public static final int DEALER_TYPE_SALESMAN = 1;//营业员
    public static final int DEALER_TYPE_MANAGER = 20;//管理员
    public static final int DEALER_TYPE_BOSS = 30;//BOSS


    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Constraints.Required
    @Constraints.MinLength(6)
    @Constraints.MaxLength(30)
    @com.fasterxml.jackson.annotation.JsonIgnore
    @Column(name = "login_password")
    public String loginPassword;//登录密码

    @Column(name = "pay_password")
    @com.fasterxml.jackson.annotation.JsonIgnore
    public String payPassword;//支付密码

    @Column(name = "status")
    public int status;//用户状态 1正常2锁定

    @Column(name = "real_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String realName;//真实姓名

    @Column(name = "nick_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String nickName;//昵称

    @Column(name = "phone_number")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String phoneNumber;//手机号
    @Column(name = "car_no")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String carNo;

    @Column(name = "contact_number")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String contactNumber;

    @Column(name = "create_time")
    public long createdTime;//创建时间

    @Column(name = "dealer_id")
    public long dealerId;

    @Column(name = "second_dealer_id")
    public long secondDealerId;

    @Column(name = "dealer_type")
    public long dealerType;

    @Column(name = "level")
    public int level;
    @Column(name = "level_name")
    public String levelName;

    @Column(name = "station_id")
    public long stationId;

    @Column(name = "station_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String stationName;

    @Column(name = "org_id")
    public long orgId;

    @Column(name = "org_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String orgName;

    @Column(name = "avatar")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String avatar;//头像

    @Column(name = "user_type")
    public int userType;

    @Column(name = "logical_number")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String logicalNumber;

    @Column(name = "physical_number")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String physicalNumber;
    @Column(name = "card_password")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String cardPassword;

    @Column(name = "vin")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String vin;

    @Column(name = "birthday")
    public long birthday;

    @Column(name = "birthday_month")
    public int birthdayMonth;

    @Column(name = "birthday_day")
    public int birthdayDay;
    @Column(name = "barcode_img_url")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String barcodeImgUrl;

    @Column(name = "open_id")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String openId;

    @Column(name = "session_key")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String sessionKey;

    @Column(name = "union_id")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String unionId;

    @Column(name = "id_card_no")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String idCardNo;//身份证号码
    @Column(name = "filter")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String filter;
    @Column(name = "note")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String note;

    @Column(name = "user_note")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String userNote;

    @Column(name = "dealer_level")
    public long dealerLevel;

    @Column(name = "sex")
    public int sex;

    @Column(name = "receiver_status")
    public int receiverStatus;

    @Column(name = "login_count")
    public int loginCount;

    @Column(name = "group_id")
    public long groupId;

    @Column(name = "group_name")
    public String groupName;

    @Column(name = "dept_id")
    public long deptId;

    @Column(name = "update_time")
    public long updateTime;

    @Transient
    public long leftBalance;

    @Transient
    public long score;

    @Transient
    public String dealerName;

    @Transient
    public double totalOrderMoney;


    public static Finder<Long, Member> find = new Finder<>(Member.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getDealerId() {
        return dealerId;
    }

    public void setDealerId(long dealerId) {
        this.dealerId = dealerId;
    }

    public long getDealerType() {
        return dealerType;
    }

    public void setDealerType(long dealerType) {
        this.dealerType = dealerType;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getStationId() {
        return stationId;
    }

    public void setStationId(long stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public long getLeftBalance() {
        return leftBalance;
    }

    public void setLeftBalance(long leftBalance) {
        this.leftBalance = leftBalance;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public double getTotalOrderMoney() {
        return totalOrderMoney;
    }

    public void setTotalOrderMoney(double totalOrderMoney) {
        this.totalOrderMoney = totalOrderMoney;
    }

    public String getLogicalNumber() {
        return logicalNumber;
    }

    public void setLogicalNumber(String logicalNumber) {
        this.logicalNumber = logicalNumber;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public int getBirthdayMonth() {
        return birthdayMonth;
    }

    public void setBirthdayMonth(int birthdayMonth) {
        this.birthdayMonth = birthdayMonth;
    }

    public int getBirthdayDay() {
        return birthdayDay;
    }

    public void setBirthdayDay(int birthdayDay) {
        this.birthdayDay = birthdayDay;
    }

    public String getBarcodeImgUrl() {
        return barcodeImgUrl;
    }

    public void setBarcodeImgUrl(String barcodeImgUrl) {
        this.barcodeImgUrl = barcodeImgUrl;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getUserNote() {
        return userNote;
    }

    public void setUserNote(String userNote) {
        this.userNote = userNote;
    }

    public long getDealerLevel() {
        return dealerLevel;
    }

    public void setDealerLevel(long dealerLevel) {
        this.dealerLevel = dealerLevel;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public long getSecondDealerId() {
        return secondDealerId;
    }

    public void setSecondDealerId(long secondDealerId) {
        this.secondDealerId = secondDealerId;
    }

    public String getPhysicalNumber() {
        return physicalNumber;
    }

    public void setPhysicalNumber(String physicalNumber) {
        this.physicalNumber = physicalNumber;
    }

    public int getReceiverStatus() {
        return receiverStatus;
    }

    public void setReceiverStatus(int receiverStatus) {
        this.receiverStatus = receiverStatus;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getCardPassword() {
        return cardPassword;
    }

    public void setCardPassword(String cardPassword) {
        this.cardPassword = cardPassword;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getDeptId() {
        return deptId;
    }

    public void setDeptId(long deptId) {
        this.deptId = deptId;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
