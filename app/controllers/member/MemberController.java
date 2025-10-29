package controllers.member;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import controllers.BaseSecurityController;
import io.ebean.Expr;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.admin.ShopAdmin;
import models.user.Member;
import myannotation.EscapeHtmlSerializer;
import play.Logger;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import utils.*;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static constants.BusinessConstant.*;

/**
 * 用户管理
 */
public class MemberController extends BaseSecurityController {
    @Inject
    BizUtils bizUtils;
    Logger.ALogger logger = Logger.of(MemberController.class);

    @Inject
    EscapeHtmlSerializer escapeHtmlSerializer;

    /**
     * @api {POST} /v2/s/members/?page=&uid=&filter= 01获取用户列表
     * @apiName listMembers
     * @apiGroup ADMIN_MEMBER
     * @apiParam {long} uid uid
     * @apiParam {long} orgId orgId
     * @apiParam {long} shopId shopId
     * @apiParam {int} page page
     * @apiParam {String} filter realName/nickName/phoneNumber/dealerCode
     * @apiParam {int} status 0all 1normal 2pause
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){int} pages 分页
     * @apiSuccess (Success 200){JsonArray} list 用户列表
     * @apiSuccess (Success 200){long} id 用户ID
     * @apiSuccess (Success 200){int} status 用户状态1正常2锁定
     * @apiSuccess (Success 200){string} realName 实名
     * @apiSuccess (Success 200){string} nickName 昵称
     * @apiSuccess (Success 200){string} phoneNumber 手机号
     * @apiSuccess (Success 200){string} description 备注
     * @apiSuccess (Success 200){string} agentCode 代理编号
     * @apiSuccess (Success 200){string} updateTime 更新时间
     * @apiSuccess (Success 200){string} createdTime 创建时间
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> listMembers(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode requestNode = request.body().asJson();
            if (null == requestNode) return okCustomJson(request, CODE40001, "base.argument.error");
            int page = requestNode.findPath("page").asInt();
            int status = requestNode.findPath("status").asInt();
            int hasDealer = requestNode.findPath("hasDealer").asInt();
            long uid = requestNode.findPath("uid").asLong();
            long dealerId = requestNode.findPath("dealerId").asLong();
            long orgId = requestNode.findPath("orgId").asLong();

            String filter = requestNode.findPath("filter").asText();
            ShopAdmin admin = businessUtils.getUserIdByAuthToken2(request);
            if (null == admin) return unauth403(request);


            ExpressionList<Member> expressionList = Member.find.query().where();
            //.eq("orgId", admin.orgId);
            if (uid > 0) expressionList.eq("id", uid);
            if (status > 0) expressionList.eq("status", status);
            if (dealerId > 0) expressionList.eq("dealerId", dealerId);
            if (orgId > 0) expressionList.eq("orgId", orgId);
            if (hasDealer > 0) {
                if (hasDealer == 1) expressionList.gt("dealerId", 0);
                else expressionList.eq("dealerId", 0);
            }
            if (!ValidationUtil.isEmpty(filter)) {
                String orFilter = escapeHtmlSerializer.escapeHtml(filter);
                orFilter = "%" + orFilter + "%";
                expressionList.or(
                        Expr.or(Expr.ilike("realName", orFilter), Expr.like("phoneNumber", orFilter)),
                        Expr.or(Expr.ilike("nickName", orFilter), Expr.ilike("stationName", orFilter))
                );
            }
            int members = expressionList.findCount();
            PagedList<Member> pagedList = expressionList.orderBy().desc("id")
                    .setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_20)
                    .setMaxRows(BusinessConstant.PAGE_SIZE_20)
                    .findPagedList();
            List<Member> list = pagedList.getList();

            Set<Long> set = new HashSet<>();
            list.forEach((member) -> set.add(member.id));

            int pages = pagedList.getTotalPageCount();
            ObjectNode node = Json.newObject();
            node.put(CODE, CODE200);
            node.put("pages", pages);
            node.put("totalMembers", members);
            node.set("list", Json.toJson(list));
            String execQueryUserList = bizUtils.getMessageValue(request, "exec.query.user.list");
            businessUtils.addOperationLog(request, admin, execQueryUserList);
            return ok(node);
        });
    }

    /**
     * @api {GET} /v2/s/members/:memberId/ 02获取用户详情
     * @apiName getUser
     * @apiGroup ADMIN_MEMBER
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){int} pages 分页
     * @apiSuccess (Success 200){JsonArray} list 用户列表
     * @apiSuccess (Success 200){long} id 用户ID
     * @apiSuccess (Success 200){int} status 用户状态1正常2锁定
     * @apiSuccess (Success 200){string} realName 实名
     * @apiSuccess (Success 200){string} nickName 昵称
     * @apiSuccess (Success 200){string} phoneNumber 手机号
     * @apiSuccess (Success 200){string} description 备注
     * @apiSuccess (Success 200){long} birthday 生日
     * @apiSuccess (Success 200){String} idCardNo 身份证号
     * @apiSuccess (Success 200){String} licenseNo 营业执照
     * @apiSuccess (Success 200){String} licenseImgUrl 营业执照图片地址
     * @apiSuccess (Success 200){string} agentCode 代理编号
     * @apiSuccess (Success 200){string} idCardNo 身份证号码
     * @apiSuccess (Success 200){string} licenseNo 营业执照
     * @apiSuccess (Success 200){int} gender 0：未知、1：男、2：女
     * @apiSuccess (Success 200){String} city 城市
     * @apiSuccess (Success 200){String} province 省份
     * @apiSuccess (Success 200){String} country 国家
     * @apiSuccess (Success 200){String} shopName 店铺
     * @apiSuccess (Success 200){String} contactPhoneNumber 联系电话
     * @apiSuccess (Success 200){String} contactAddress 联系地址
     * @apiSuccess (Success 200){String} businessItems 经营类目
     * @apiSuccess (Success 200){String} images 图片，多张，以逗号隔开
     * @apiSuccess (Success 200){string} createdTime 创建时间
     */
    public CompletionStage<Result> getMember(Http.Request request, long uid) {
        return CompletableFuture.supplyAsync(() -> {
            ShopAdmin admin = businessUtils.getUserIdByAuthToken2(request);
            if (null == admin) return unauth403(request);
            Member member = Member.find.byId(uid);
            if (null == member) return okCustomJson(request, CODE40001, "user.member.empty");
            ObjectNode result = (ObjectNode) Json.toJson(member);
            result.put(CODE, CODE200);
            businessUtils.addOperationLog(request, admin, "查看用户详情，uid:" + member.id);
            return ok(result);
        });
    }

    /**
     * @api {POST} /v2/s/members/status/ 03锁定/解锁用户
     * @apiName setMemberStatus
     * @apiGroup ADMIN_MEMBER
     * @apiParam {long} memberId 用户ID
     * @apiParam {int} status 1正常，2锁定
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Error 40001){int} code 40001 用户不存在
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public CompletionStage<Result> setMemberStatus(Http.Request request) {
        JsonNode requestNode = request.body().asJson();
        long memberId = requestNode.findPath("memberId").asLong();
        int status = requestNode.findPath("status").asInt();
        return CompletableFuture.supplyAsync(() -> {
            ShopAdmin admin = businessUtils.getUserIdByAuthToken2(request);
            if (null == admin) return unauth403(request);
            if (memberId < 1) return okCustomJson(request, CODE40001, "base.argument.error");
            if (status != Member.MEMBER_STATUS_LOCK && status != Member.MEMBER_STATUS_NORMAL)
                return okCustomJson(request, CODE40001, "base.argument.error");
            Member member = Member.find.byId(memberId);
            if (null == member) return okCustomJson(request, CODE40001, "user.member.empty");
            member.setStatus(status);
            member.save();
            //将用户的缓存清掉
            deleteMemberLoginStatus(member);
            String prefixLockOrUnlockUser = bizUtils.getMessageValue(request, "prefix.lock.or.unlock.user.uid");
            businessUtils.addOperationLog(request, admin, prefixLockOrUnlockUser + member.id);
            return okJSON200();
        });
    }

    private void deleteMemberLoginStatus(Member member) {
        String tokenKey = cacheUtils.getMemberTokenKey(member.orgId, member.id);
        redis.remove(tokenKey);
    }


    /**
     * @api {POST} /v2/s/members/:uid/ 13修改用户信息
     * @apiName updateMember
     * @apiGroup ADMIN_MEMBER
     * @apiParam {long} uid 用户ID
     * @apiParam {int} [status] 1正常，2锁定
     * @apiParam {int} [dealerType] 1正常，2锁定
     * @apiParam {string} [realName] 真实姓名
     * @apiParam {string} [nickName] 昵称
     * @apiParam {string} [phoneNumber] 手机号
     * @apiParam {string} [physicalNumber] 物理卡号
     * @apiParam {string} [logicalNumber] 逻辑卡号
     * @apiParam {string} [cardPassword] 卡密
     * @apiParam {string} [vin] 车架号
     * @apiParam {string} [carNo] 车牌号
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Error 40001){int} code 40001 用户不存在
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public CompletionStage<Result> updateMember(Http.Request request, long uid) {
        JsonNode requestNode = request.body().asJson();
        return CompletableFuture.supplyAsync(() -> {
            ShopAdmin admin = businessUtils.getUserIdByAuthToken2(request);
            if (null == admin) return unauth403(request);
            int status = requestNode.findPath("status").asInt(0);
            int dealerType = requestNode.findPath("dealerType").asInt(0);
            int userType = requestNode.findPath("userType").asInt(0);
            String realName = requestNode.findPath("realName").asText();
            String nickName = requestNode.findPath("nickName").asText();
            String phoneNumber = requestNode.findPath("phoneNumber").asText();
            String physicalNumber = requestNode.findPath("physicalNumber").asText();
            String logicalNumber = requestNode.findPath("logicalNumber").asText();
            String cardPassword = requestNode.findPath("cardPassword").asText();
            String carNo = requestNode.findPath("carNo").asText();
            String vin = requestNode.findPath("vin").asText();
            if (uid < 1) return okCustomJson(request, CODE40001, "base.argument.error");
            Member member = Member.find.byId(uid);
            if (null == member || member.orgId != admin.orgId)
                return okCustomJson(request, CODE40001, "user.member.empty");
            if (status != 0 && (status == Member.MEMBER_STATUS_LOCK || status == Member.MEMBER_STATUS_NORMAL)) {
                member.setStatus(status);
            }
            if (dealerType > 0) member.setDealerType(dealerType);
            if (userType > 0) member.setUserType(userType);
            if (!ValidationUtil.isEmpty(realName)) member.setRealName(realName);
            if (!ValidationUtil.isEmpty(nickName)) member.setNickName(nickName);
            if (!ValidationUtil.isEmpty(phoneNumber)) member.setPhoneNumber(phoneNumber);
            if (!ValidationUtil.isEmpty(carNo)) member.setCarNo(carNo);
            if (requestNode.has("physicalNumber")) {
                if (ValidationUtil.isEmpty(physicalNumber)) member.setPhysicalNumber("");
                else {
                    Member exist = Member.find.query().where()
                            .eq("orgId", admin.orgId)
                            .eq("physicalNumber", physicalNumber)
                            .ne("id", member.id)
                            .setMaxRows(1)
                            .findOne();
                    if (null != exist) {
                        return okCustomJson(request, CODE40001, "user.physicalNumber.exist");
                    }
                    member.setPhysicalNumber(physicalNumber);
//                    member.setPhysicalNumber(bizUtils.prependZero(physicalNumber, 16));
                }
            }
            if (requestNode.has("logicalNumber")) {
                if (ValidationUtil.isEmpty(logicalNumber)) member.setLogicalNumber("");
                else {
                    Member exist = Member.find.query().where()
                            .eq("orgId", admin.orgId)
                            .eq("logicalNumber", logicalNumber)
                            .ne("id", member.id)
                            .setMaxRows(1)
                            .findOne();
                    if (null != exist) {
                        return okCustomJson(request, CODE40001, "user.logicalNumber.exist");
                    }
                    member.setLogicalNumber(logicalNumber);
//                    member.setLogicalNumber(bizUtils.prependZero(logicalNumber, 16));
                }
            }
            if (requestNode.has("cardPassword")) {
                if (ValidationUtil.isEmpty(cardPassword)) member.setCardPassword("");
                else member.setCardPassword(MD5.MD5Encode(cardPassword));
            }
            if (requestNode.has("vin")) {
                member.setVin(vin);
//                if (vin.length() > 0) {
//                    member.setVin(bizUtils.prependZero(vin, 34));
//                }
            }
            member.save();
            //将用户的缓存清掉
            deleteMemberLoginStatus(member);
            String prefixLockOrUnlockUser = bizUtils.getMessageValue(request, "prefix.lock.or.unlock.user.uid");
            businessUtils.addOperationLog(request, admin, prefixLockOrUnlockUser + member.id);
            return okJSON200();
        });
    }


    /**
     * @api {POST} /v2/s/dealer_customers/ 14业务员归属客户列表
     * @apiName listCustomers
     * @apiGroup ADMIN_MEMBER
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){int} pages 分页
     * @apiSuccess (Success 200){JsonArray} list 用户列表
     * @apiSuccess (Success 200){double} totalOrderMoney totalOrderMoney
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public CompletionStage<Result> listCustomers(Http.Request request) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((operator) -> {
            if (null == operator) return unauth503(request);
            JsonNode requestNode = request.body().asJson();
            if (null == requestNode) return okCustomJson(request, CODE40001, "base.argument.error");
            int page = requestNode.findPath("page").asInt();
            long dealerId = requestNode.findPath("dealerId").asLong();
            ExpressionList<Member> expressionList = Member.find.query().where()
                    .eq("orgId", operator.orgId)
                    .eq("dealerId", dealerId);
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            if (page == 1) {
                long totalInvites = expressionList.findCount();
                result.put("totalInvites", totalInvites);
                int members = Member.find.query().where()
                        .eq("orgId", operator.orgId)
                        .eq("dealerId", dealerId)
                        .findCount();
                result.put("totalMembers", members);
            }
            PagedList<Member> pagedList = expressionList
                    .orderBy().desc("id")
                    .setFirstRow((page - 1) * PAGE_SIZE_10)
                    .setMaxRows(PAGE_SIZE_10)
                    .findPagedList();
            List<Member> list = pagedList.getList();
            int pages = pagedList.getTotalPageCount();
            result.put("pages", pages);
            result.put("hasNext", pagedList.hasNext());
            result.set("list", Json.toJson(list));
            return ok(result);
        });
    }

}
