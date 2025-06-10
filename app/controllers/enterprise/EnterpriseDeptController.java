package controllers.enterprise;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import controllers.BaseSecurityController;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.enterprise.EnterpriseDept;
import models.enterprise.EnterpriseInfo;
import models.user.Member;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import utils.ValidationUtil;

import java.util.List;
import java.util.concurrent.CompletionStage;

public class EnterpriseDeptController extends BaseSecurityController {


    /**
     * @api {POST} /v2/s/enterprise_dept/new/   01添加-企业部门
     * @apiName addEnterpriseDept
     * @apiGroup ENTERPRISE-DEPT-MANAGER
     * @apiParam {long} parentId 父ID
     * @apiParam {String} name 部门名称
     * @apiParam {int} discountPriority 折扣优先 10部门折扣优先 20用户折扣优先
     * @apiParam {int} electricityFee 电费
     * @apiParam {int} serviceFee 服务费
     * @apiSuccess (Success 200){int} code 200
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> addEnterpriseDept(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((admin) -> {
            if (null == admin) return unauth403(request);
            if (null == jsonNode) return okCustomJson(request, CODE40001, "base.argument.error");
            EnterpriseDept enterpriseDept = Json.fromJson(jsonNode, EnterpriseDept.class);
            if (enterpriseDept == null) return okCustomJson(request, CODE40001, "base.argument.error");
            if (enterpriseDept.parentId > 0) {
                EnterpriseDept parent = EnterpriseDept.find.byId(enterpriseDept.parentId);
                if (parent == null) return okCustomJson(request, CODE40001, "base.argument.error");
            }
            EnterpriseInfo enterpriseInfo = EnterpriseInfo.find.query().where().eq("orgId", admin.getOrgId())
                    .orderBy().asc("id")
                    .setMaxRows(1).findOne();
            long currentTime = dateUtils.getCurrentTimeByMilliSecond();
            if (null == enterpriseInfo) {
                enterpriseInfo = new EnterpriseInfo();
                enterpriseInfo.setOrgId(admin.getOrgId());
                enterpriseInfo.setName(admin.getOrgName());
                enterpriseInfo.setCreatedTime(currentTime);
                enterpriseInfo.setUpdateTime(currentTime);
                enterpriseInfo.save();
            }
            enterpriseDept.setEnterpriseId(enterpriseInfo.id);
            enterpriseDept.setOrgId(enterpriseInfo.orgId);
            enterpriseDept.setCreatedTime(currentTime);
            enterpriseDept.setUpdateTime(currentTime);
            enterpriseDept.save();
            return okJSON200();
        });
    }

    /**
     * @api {GET} /v2/s/enterprise_dept/:id/  02详情-企业部门
     * @apiName getEnterpriseDept
     * @apiGroup ENTERPRISE-DEPT-MANAGER
     * @apiParam {long} id id
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {long} id 部门ID
     * @apiSuccess (Success 200) {long} orgId 机构ID
     * @apiSuccess (Success 200) {long} enterpriseId 公司ID
     * @apiSuccess (Success 200) {long} parentId 父ID
     * @apiSuccess (Success 200) {String} name 部门名称
     * @apiSuccess (Success 200) {int} discountPriority 折扣优先 10部门折扣优先 20用户折扣优先
     * @apiSuccess (Success 200) {int} electricityFee 电费
     * @apiSuccess (Success 200) {int} serviceFee 服务费
     * @apiSuccess (Success 200) {long} updateTime 更新时间
     * @apiSuccess (Success 200) {long} createdTime 创建时间
     */
    public CompletionStage<Result> getEnterpriseDept(Http.Request request, long id) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403(request);
            EnterpriseDept enterpriseDept = EnterpriseDept.find.byId(id);
            if (null == enterpriseDept || enterpriseDept.orgId != adminMember.orgId)
                return okCustomJson(request, CODE40001, "base.date.not.exist");
            ObjectNode result = (ObjectNode) Json.toJson(enterpriseDept);
            result.put(CODE, CODE200);
            return ok(result);
        });

    }

    /**
     * @api {POST} /v2/s/enterprise_dept/:id/  04更新-企业部门
     * @apiName updateEnterpriseDept
     * @apiGroup ENTERPRISE-DEPT-MANAGER
     * @apiParam {long} id 部门ID
     * @apiParam {long} parentId 父ID
     * @apiParam {String} name 部门名称
     * @apiParam {int} discountPriority 折扣优先 10部门折扣优先 20用户折扣优先
     * @apiParam {int} electricityFee 电费
     * @apiParam {int} serviceFee 服务费
     * @apiSuccess (Success 200){int} code 200
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> updateEnterpriseDept(Http.Request request, long id) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403(request);
            EnterpriseDept originalEnterpriseDept = EnterpriseDept.find.byId(id);
            EnterpriseDept newEnterpriseDept = Json.fromJson(jsonNode, EnterpriseDept.class);
            if (null == originalEnterpriseDept) return okCustomJson(request, CODE40001, "base.date.not.exist");
            if (newEnterpriseDept.parentId > 0) {
                EnterpriseDept parent = EnterpriseDept.find.byId(newEnterpriseDept.parentId);
                if (parent == null) return okCustomJson(request, CODE40001, "base.argument.error");
                originalEnterpriseDept.setParentId(newEnterpriseDept.parentId);
            }
            if (!ValidationUtil.isEmpty(newEnterpriseDept.name)) originalEnterpriseDept.setName(newEnterpriseDept.name);
            if (originalEnterpriseDept.discountType > 0)
                originalEnterpriseDept.setDiscountType(newEnterpriseDept.discountType);
            if (jsonNode.has("fixServiceFee"))
                originalEnterpriseDept.setFixServiceFee(newEnterpriseDept.fixServiceFee);
            if (newEnterpriseDept.discount > 0)
                originalEnterpriseDept.setDiscount(newEnterpriseDept.discount);

            if (jsonNode.has("show")) originalEnterpriseDept.setShow(newEnterpriseDept.show);
            if (jsonNode.has("sort")) originalEnterpriseDept.setSort(newEnterpriseDept.sort);
            long currentTimeBySecond = dateUtils.getCurrentTimeByMilliSecond();
            originalEnterpriseDept.setUpdateTime(currentTimeBySecond);
            originalEnterpriseDept.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/s/enterprise_dept/   05删除-企业部门
     * @apiName deleteEnterpriseDept
     * @apiGroup ENTERPRISE-DEPT-MANAGER
     * @apiParam {long} id id
     * @apiParam {String} operation del时删除
     * @apiSuccess (Success 200){int} 200 成功
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> deleteEnterpriseDept(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403(request);
            long id = jsonNode.findPath("id").asLong();
            String operation = jsonNode.findPath("operation").asText();
            if (!"del".equals(operation)) return okCustomJson(request, CODE40001, "base.operation.error");
            EnterpriseDept deleteModel = EnterpriseDept.find.byId(id);
            if (null == deleteModel) return okCustomJson(request, CODE40001, "base.date.not.exist");
            deleteModel.delete();
            return okJSON200();
        });
    }

    /**
     * @api {GET} /v2/s/enterprise_dept_list/   05列表-企业部门
     * @apiName listEnterpriseDept
     * @apiGroup ENTERPRISE-DEPT-MANAGER
     * @apiParam {int} page 页码
     * @apiSuccess (Success 200) {Object} list 列表
     * @apiSuccess (Success 200) {long} list.id 部门ID
     * @apiSuccess (Success 200) {long} list.orgId 机构ID
     * @apiSuccess (Success 200) {long} list.enterpriseId 公司ID
     * @apiSuccess (Success 200) {long} list.parentId 父ID
     * @apiSuccess (Success 200) {String} list.name 部门名称
     * @apiSuccess (Success 200) {int} list.discountPriority 折扣优先 10部门折扣优先 20用户折扣优先
     * @apiSuccess (Success 200) {int} list.electricityFee 电费
     * @apiSuccess (Success 200) {int} list.serviceFee 服务费
     * @apiSuccess (Success 200) {long} list.updateTime 更新时间
     * @apiSuccess (Success 200) {long} list.createdTime 创建时间
     */
    public CompletionStage<Result> listEnterpriseDept(Http.Request request, int page) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403(request);
            ExpressionList<EnterpriseDept> expressionList = EnterpriseDept.find.query().where();

            ObjectNode result = Json.newObject();
            List<EnterpriseDept> list;
            if (page == 0) list = expressionList.findList();
            else {
                PagedList<EnterpriseDept> pagedList = expressionList
                        .order().desc("id")
                        .setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_20)
                        .setMaxRows(BusinessConstant.PAGE_SIZE_20)
                        .findPagedList();
                list = pagedList.getList();
                result.put("pages", pagedList.getTotalPageCount());
                result.put("hasNest", pagedList.hasNext());
            }
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(list));
            return ok(result);
        });
    }

    /**
     * @api {GET} /v2/s/enterprise_dept_staff_list/:id/   07列表-企业部门员工
     * @apiName listEnterpriseDeptStaff
     * @apiGroup ENTERPRISE-DEPT-MANAGER
     * @apiParam {int} page 页码
     * @apiSuccess (Success 200) {Object} list 列表
     * @apiSuccess (Success 200) {int} list.status 状态　１正常　２禁用
     * @apiSuccess (Success 200) {String} list.realName 员工名字
     * @apiSuccess (Success 200) {String} list.phoneNumber 员工帐号(手机号)
     * @apiSuccess (Success 200) {long} list.deptId 部门部门ID
     * @apiSuccess (Success 200) {String} list.deptName 部门名称
     * @apiSuccess (Success 200) {String} list.cardNo 卡号
     * @apiSuccess (Success 200) {long} list.monthlyBalance 每月自动充值金额
     * @apiSuccess (Success 200) {long} list.maxChargeCount 最多充值次数
     * @apiSuccess (Success 200) {long} list.updateTime 更新时间
     * @apiSuccess (Success 200) {long} list.createTime 创建时间
     */
    public CompletionStage<Result> listEnterpriseDeptStaff(Http.Request request, long id, int page) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403(request);
            if (id < 1) return okCustomJson(request, CODE40001, "base.argument.error");
            EnterpriseDept dept = EnterpriseDept.find.byId(id);
            if (null == dept) return okCustomJson(request, CODE40001, "base.date.not.exist");
            ExpressionList<Member> expressionList = Member.find.query().where().eq("deptId", id);
            ObjectNode result = Json.newObject();
            List<Member> list;
            if (page == 0) list = expressionList.findList();
            else {
                PagedList<Member> pagedList = expressionList
                        .order().desc("id")
                        .setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_20)
                        .setMaxRows(BusinessConstant.PAGE_SIZE_20)
                        .findPagedList();
                list = pagedList.getList();
                result.put("pages", pagedList.getTotalPageCount());
                result.put("hasNest", pagedList.hasNext());
            }
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(list));
            return ok(result);
        });
    }

    /**
     * @api {GET} /v2/s/enterprise_dept_staff_no_bind_list/   08列表-未绑定企业部门员工
     * @apiName listNoBindEnterpriseDeptStaff
     * @apiGroup ENTERPRISE-DEPT-MANAGER
     * @apiParam {int} page 页码
     * @apiSuccess (Success 200) {Object} list 列表
     * @apiSuccess (Success 200) {int} list.status 状态　１正常　２禁用
     * @apiSuccess (Success 200) {String} list.realName 员工名字
     * @apiSuccess (Success 200) {String} list.phoneNumber 员工帐号(手机号)
     * @apiSuccess (Success 200) {long} list.deptId 部门部门ID
     * @apiSuccess (Success 200) {String} list.deptName 部门名称
     * @apiSuccess (Success 200) {String} list.cardNo 卡号
     * @apiSuccess (Success 200) {long} list.monthlyBalance 每月自动充值金额
     * @apiSuccess (Success 200) {long} list.maxChargeCount 最多充值次数
     * @apiSuccess (Success 200) {long} list.updateTime 更新时间
     * @apiSuccess (Success 200) {long} list.createTime 创建时间
     */
    public CompletionStage<Result> listNoBindEnterpriseDeptStaff(Http.Request request, int page) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403(request);
            ExpressionList<Member> expressionList = Member.find.query().where().eq("deptId", 0);
            ObjectNode result = Json.newObject();
            List<Member> list;
            if (page == 0) list = expressionList.findList();
            else {
                PagedList<Member> pagedList = expressionList
                        .order().desc("id")
                        .setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_20)
                        .setMaxRows(BusinessConstant.PAGE_SIZE_20)
                        .findPagedList();
                list = pagedList.getList();
                result.put("pages", pagedList.getTotalPageCount());
                result.put("hasNest", pagedList.hasNext());
            }
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(list));
            return ok(result);
        });
    }

    /**
     * @api {POST} /v2/s/enterprise_dept_batch_bind_staff/   09批量绑定-企业部门员工
     * @apiName bindEnterpriseDeptStaff
     * @apiGroup ENTERPRISE-DEPT-MANAGER
     * @apiParam {long} id 部门ID
     * @apiParam {Array} list 员工ID 列表
     * @apiSuccess (Success 200){int} 200 成功
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> bindEnterpriseDeptStaff(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403(request);
            JsonNode list = jsonNode.findPath("list");
            if (list == null || !list.isArray() || list.size() < 1)
                return okCustomJson(request, CODE40001, "base.argument.error");
            long id = jsonNode.findPath("id").asLong();
            if (id < 1) return okCustomJson(request, CODE40001, "base.argument.error");
            EnterpriseDept dept = EnterpriseDept.find.byId(id);
            if (null == dept) return okCustomJson(request, CODE40001, "base.date.not.exist");
            for (JsonNode node : list) {
                long staffId = node.asLong();
                if (staffId < 1) return okCustomJson(request, CODE40001, "base.argument.error");
                Member staff = Member.find.byId(staffId);
                if (null == staff) return okCustomJson(request, CODE40001, "base.date.not.exist");
                if (staff.getDeptId() != 0) return okCustomJson(request, CODE40001, "department.exist");
                staff.setDeptId(dept.id);
                staff.save();
            }
            return okJSON200();
        });
    }

    /**
     * @api {GET} /v2/s/enterprise_dept_batch_unbind_staff/   10批量解绑-企业部门员工
     * @apiName unbindEnterpriseDeptStaff
     * @apiGroup ENTERPRISE-DEPT-MANAGER
     * @apiParam {Array} list 员工ID 列表
     * @apiSuccess (Success 200){int} 200 成功
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> unbindEnterpriseDeptStaff(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403(request);
            JsonNode list = jsonNode.findPath("list");
            if (list == null || !list.isArray() || list.size() < 1)
                return okCustomJson(request, CODE40001, "base.argument.error");
            for (JsonNode node : list) {
                long staffId = node.asLong();
                if (staffId < 1) return okCustomJson(request, CODE40001, "base.argument.error");
                Member staff = Member.find.byId(staffId);
                if (null == staff) return okCustomJson(request, CODE40001, "base.date.not.exist");
                staff.setDeptId(0);
                staff.save();
            }
            return okJSON200();
        });
    }
}
