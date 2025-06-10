package controllers.enterprise;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.BaseSecurityController;
import models.admin.ShopAdmin;
import models.enterprise.EnterpriseInfo;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import utils.ValidationUtil;

import java.util.concurrent.CompletionStage;

public class EnterpriseInfoController extends BaseSecurityController {

    /**
     * @api {GET} /v2/s/enterprise_info/  01详情-企业信息
     * @apiName getEnterpriseInfo
     * @apiGroup ENTERPRISE-INFO-MANAGER
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {long} id 企业ID
     * @apiSuccess (Success 200) {long} orgId 机构ID
     * @apiSuccess (Success 200) {long} adminId 管理员ID
     * @apiSuccess (Success 200) {String} adminAccount 管理员账号
     * @apiSuccess (Success 200) {String} adminName 管理员名称
     * @apiSuccess (Success 200) {String} name 企业名称
     * @apiSuccess (Success 200) {String} legalPerson 法人
     * @apiSuccess (Success 200) {String} enterpriseCode 企业编码
     * @apiSuccess (Success 200) {String} orgCode 组织机构代码
     * @apiSuccess (Success 200) {int} companyType 企业类型
     * @apiSuccess (Success 200) {String} address 企业地址
     * @apiSuccess (Success 200) {String} platformLogo 平台企业LOGO
     * @apiSuccess (Success 200) {String} appLogo APP LOGO
     * @apiSuccess (Success 200) {String} businessLicense 营业执照
     * @apiSuccess (Success 200) {String} accountList 账户信息 json
     * @apiSuccess (Success 200) {long} updateTime 更新时间
     * @apiSuccess (Success 200) {long} createdTime 创建时间
     */
    public CompletionStage<Result> getEnterpriseInfo(Http.Request request) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403(request);
            EnterpriseInfo enterpriseInfo =  EnterpriseInfo.find.query().where().eq("orgId", adminMember.getOrgId())
                    .orderBy().asc("id")
                    .setMaxRows(1).findOne();
            if (null == enterpriseInfo) {
                enterpriseInfo = new EnterpriseInfo();
                enterpriseInfo.setOrgId(adminMember.getOrgId());
                enterpriseInfo.setName(adminMember.getOrgName());
                long currentTime = dateUtils.getCurrentTimeByMilliSecond();
                enterpriseInfo.setCreatedTime(currentTime);
                enterpriseInfo.setUpdateTime(currentTime);
                enterpriseInfo.save();
            }
            ObjectNode result = (ObjectNode) Json.toJson(enterpriseInfo);
            result.put(CODE, CODE200);
            return ok(result);
        });

    }

    /**
     * @api {POST} /v2/s/enterprise_info/:id/  02更新-企业信息
     * @apiName updateEnterpriseInfo
     * @apiGroup ENTERPRISE-INFO-MANAGER
     * @apiParam {long} id 企业ID
     * @apiParam {long} orgId 机构ID
     * @apiParam {long} adminId 管理员ID
     * @apiParam {String} name 企业名称
     * @apiParam {String} legalPerson 法人
     * @apiParam {String} enterpriseCode 企业编码
     * @apiParam {String} orgCode 组织机构代码
     * @apiParam {int} companyType 企业类型
     * @apiParam {String} address 企业地址
     * @apiParam {String} platformLogo 平台企业LOGO
     * @apiParam {String} appLogo APP LOGO
     * @apiParam {String} businessLicense 营业执照
     * @apiParam {String} accountList 账户信息 json
     * @apiSuccess (Success 200){int} code 200
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> updateEnterpriseInfo(Http.Request request, long id) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403(request);
            EnterpriseInfo originalEnterpriseInfo = EnterpriseInfo.find.byId(id);
            EnterpriseInfo newEnterpriseInfo = Json.fromJson(jsonNode, EnterpriseInfo.class);
            if (null == originalEnterpriseInfo) return okCustomJson(request, CODE40001, "base.date.not.exist");
            if (newEnterpriseInfo.adminId > 0) {
                ShopAdmin shopAdmin = ShopAdmin.find.byId(newEnterpriseInfo.adminId);
                if (shopAdmin == null) return okCustomJson(request, CODE40001, "shopAdmin.not.exist");
                originalEnterpriseInfo.setAdminId(shopAdmin.id);
                originalEnterpriseInfo.setAdminAccount(shopAdmin.userName);
                originalEnterpriseInfo.setAdminName(shopAdmin.realName);
            }
            if (!ValidationUtil.isEmpty(newEnterpriseInfo.name)) originalEnterpriseInfo.setName(newEnterpriseInfo.name);
            if (!ValidationUtil.isEmpty(newEnterpriseInfo.legalPerson))
                originalEnterpriseInfo.setLegalPerson(newEnterpriseInfo.legalPerson);
            if (!ValidationUtil.isEmpty(newEnterpriseInfo.enterpriseCode)) originalEnterpriseInfo.setEnterpriseCode(newEnterpriseInfo.enterpriseCode);
            if (!ValidationUtil.isEmpty(newEnterpriseInfo.orgCode))
                originalEnterpriseInfo.setOrgCode(newEnterpriseInfo.orgCode);
            if (newEnterpriseInfo.companyType > 0) originalEnterpriseInfo.setCompanyType(newEnterpriseInfo.companyType);
            if (!ValidationUtil.isEmpty(newEnterpriseInfo.address))
                originalEnterpriseInfo.setAddress(newEnterpriseInfo.address);
            if (!ValidationUtil.isEmpty(newEnterpriseInfo.platformLogo))
                originalEnterpriseInfo.setPlatformLogo(newEnterpriseInfo.platformLogo);
            if (!ValidationUtil.isEmpty(newEnterpriseInfo.appLogo))
                originalEnterpriseInfo.setAppLogo(newEnterpriseInfo.appLogo);
            if (!ValidationUtil.isEmpty(newEnterpriseInfo.businessLicense))
                originalEnterpriseInfo.setBusinessLicense(newEnterpriseInfo.businessLicense);
            if (!ValidationUtil.isEmpty(newEnterpriseInfo.accountList))
                originalEnterpriseInfo.setAccountList(newEnterpriseInfo.accountList);
            long currentTimeBySecond = dateUtils.getCurrentTimeByMilliSecond();
            originalEnterpriseInfo.setUpdateTime(currentTimeBySecond);
            originalEnterpriseInfo.save();
            return okJSON200();
        });
    }

}
