package models.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import myannotation.EscapeHtmlSerializer;

/**
 *
 */
@Entity
@Table(name = "v1_service_fee_discount_group")
public class ServiceFeeDiscountGroup extends Model {
    /**
     * 折扣优先 10部门折扣优先 20用户折扣优先
     */
    public static final int DISCOUNT_FIX = 10;
    public static final int DISCOUNT_RATIO = 20;

    public static final int STATUS_NOT_START = 10;
    public static final int STATUS_EFFECT = 20;
    public static final int STATUS_EXPIRE = 30;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "org_id")
    public long orgId;

    @Column(name = "station_list")
    public String stationList;

    @Column(name = "name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String name;

    @Column(name = "note")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String note;
    @Column(name = "discount_type")
    public int discountType;

    @Column(name = "status")
    public int status;

    @Column(name = "fix_service_fee")
    public int fixServiceFee;//

    @Column(name = "begin_time")
    public long beginTime;

    @Column(name = "end_time")
    public long endTime;

    @Column(name = "discount")
    public int discount;

    @Column(name = "members")
    public long members;

    @Column(name = "stations")
    public int stations;

    @Column(name = "sort")
    public int sort;

    @Column(name = "update_time")
    public long updateTime;

    @Column(name = "create_time")
    public long createdTime;

    public static Finder<Long, ServiceFeeDiscountGroup> find = new Finder<>(ServiceFeeDiscountGroup.class);

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

    public String getStationList() {
        return stationList;
    }

    public void setStationList(String stationList) {
        this.stationList = stationList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getDiscountType() {
        return discountType;
    }

    public void setDiscountType(int discountType) {
        this.discountType = discountType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFixServiceFee() {
        return fixServiceFee;
    }

    public void setFixServiceFee(int fixServiceFee) {
        this.fixServiceFee = fixServiceFee;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public long getMembers() {
        return members;
    }

    public void setMembers(long members) {
        this.members = members;
    }

    public int getStations() {
        return stations;
    }

    public void setStations(int stations) {
        this.stations = stations;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
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
}
