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
@Table(name = "v1_vin_card")
public class VinCard extends Model {
    private static final long serialVersionUID = 8880942309217592953L;
    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_LOCK = 2;


    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "org_id")
    public long orgId;

    @Column(name = "status")
    public int status;//用户状态 1正常2锁定

    @Column(name = "vin")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String vin;

    @Column(name = "charger_uid")
    public long chargerUid;

    @Column(name = "charger_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String chargerName;

    @Column(name = "user_id")
    public long userId;

    @Column(name = "user_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String userName;

    @Column(name = "accept_station_list")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String acceptStationList;

    @Column(name = "filter")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String filter;

    @Column(name = "update_time")
    public long updateTime;//创建时间

    @Column(name = "create_time")
    public long createTime;//创建时间


    public static Finder<Long, VinCard> find = new Finder<>(VinCard.class);

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

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
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

    public long getChargerUid() {
        return chargerUid;
    }

    public void setChargerUid(long chargerUid) {
        this.chargerUid = chargerUid;
    }

    public String getChargerName() {
        return chargerName;
    }

    public void setChargerName(String chargerName) {
        this.chargerName = chargerName;
    }

    public long getUserId() {
        return userId;
    }

    public String getAcceptStationList() {
        return acceptStationList;
    }

    public void setAcceptStationList(String acceptStationList) {
        this.acceptStationList = acceptStationList;
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
