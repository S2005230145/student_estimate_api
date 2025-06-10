package models.enterprise;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import myannotation.EscapeHtmlSerializer;

/**
 * 企业信息
 */
@Entity
@Table(name = "v1_enterprise_info")
public class EnterpriseInfo extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "org_id")
    public long orgId;

    @Column(name = "admin_id")
    public long adminId;
    @Column(name = "admin_account")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String adminAccount;

    @Column(name = "admin_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String adminName;

    @Column(name = "name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String name;

    @Column(name = "legal_person")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String legalPerson;

    @Column(name = "code")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String enterpriseCode;

    @Column(name = "org_code")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String orgCode;

    @Column(name = "company_type")
    public int companyType;

    @Column(name = "address")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String address;

    @Column(name = "platform_logo")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String platformLogo;

    @Column(name = "app_logo")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String appLogo;

    @Column(name = "business_license")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String businessLicense;

    @Column(name = "account_list")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String accountList;

    @Column(name = "update_time")
    public long updateTime;

    @Column(name = "create_time")
    public long createdTime;

    public static Finder<Long, EnterpriseInfo> find = new Finder<>(EnterpriseInfo.class);

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

    public long getAdminId() {
        return adminId;
    }

    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }

    public String getAdminAccount() {
        return adminAccount;
    }

    public void setAdminAccount(String adminAccount) {
        this.adminAccount = adminAccount;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public String getEnterpriseCode() {
        return enterpriseCode;
    }

    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public int getCompanyType() {
        return companyType;
    }

    public void setCompanyType(int companyType) {
        this.companyType = companyType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlatformLogo() {
        return platformLogo;
    }

    public void setPlatformLogo(String platformLogo) {
        this.platformLogo = platformLogo;
    }

    public String getAppLogo() {
        return appLogo;
    }

    public void setAppLogo(String appLogo) {
        this.appLogo = appLogo;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getAccountList() {
        return accountList;
    }

    public void setAccountList(String accountList) {
        this.accountList = accountList;
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
