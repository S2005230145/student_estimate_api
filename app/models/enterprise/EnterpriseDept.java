package models.enterprise;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import myannotation.EscapeHtmlSerializer;

/**
 * 企业部门
 */
@Entity
@Table(name = "v1_enterprise_dept")
public class EnterpriseDept extends Model {
    /**
     * 折扣优先 10部门折扣优先 20用户折扣优先
     */
    public static final int DISCOUNT_PRIORITY_DEPT = 10;
    public static final int DISCOUNT_PRIORITY_USER = 20;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "org_id")
    public long orgId;

    @Column(name = "enterprise_id")
    public long enterpriseId;

    @Column(name = "parent_id")
    public long parentId; //父类目ID=0时，代表的是一级的类目

    @Column(name = "name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String name;

    @Column(name = "discount_type")
    public int discountType;

    @Column(name = "fix_service_fee")
    public int fixServiceFee;

    @Column(name = "discount")
    public int discount;

    @Column(name = "is_shown")
    public int show;

    @Column(name = "sort")
    public int sort;

    @Column(name = "update_time")
    public long updateTime;

    @Column(name = "create_time")
    public long createdTime;

    public static Finder<Long, EnterpriseDept> find = new Finder<>(EnterpriseDept.class);

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

    public long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDiscountType() {
        return discountType;
    }

    public void setDiscountType(int discountType) {
        this.discountType = discountType;
    }

    public int getFixServiceFee() {
        return fixServiceFee;
    }

    public void setFixServiceFee(int fixServiceFee) {
        this.fixServiceFee = fixServiceFee;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getShow() {
        return show;
    }

    public void setShow(int show) {
        this.show = show;
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
