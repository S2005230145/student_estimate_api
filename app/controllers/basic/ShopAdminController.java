package controllers.basic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.BaseSecurityController;
import io.ebean.DB;
import models.admin.Group;
import models.admin.GroupUser;
import models.admin.ShopAdmin;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import utils.EncodeUtils;
import utils.Pinyin4j;
import utils.ValidationUtil;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static constants.RedisKeyConstant.KEY_LOGIN_MAX_ERROR_TIMES;

/**
 * 成员管理器
 */
public class ShopAdminController extends BaseSecurityController {
    @Inject
    EncodeUtils encodeUtils;

    @Inject
    Pinyin4j pinyin4j;

    /**
     * @api {GET} /v2/s/admin_members/ 01查看管理员列表
     * @apiName listShopMembers
     * @apiGroup SHOP-ADMIN
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess {json} list
     * @apiSuccess {int} id 用户id
     * @apiSuccess {string} userName 用户名
     * @apiSuccess {string} realName 真名
     * @apiSuccess {String} avatar 头像
     * @apiSuccess {String} phoneNumber 手机号码
     * @apiSuccess {boolean} isAdmin 是否是管理员
     * @apiSuccess {String} shopName 归属店铺
     * @apiSuccess {String} orgName 机构名
     * @apiSuccess {int} status 状态 1正常 2锁定
     * @apiSuccess {String} lastLoginTime 最后登录时间
     * @apiSuccess {String} lastLoginIP 最后登录ip
     */
    public CompletionStage<Result> listShopMembers(Http.Request request,long orgId) {
        return CompletableFuture.supplyAsync(() -> {
            ShopAdmin member = businessUtils.getUserIdByAuthToken2(request);
            if (null == member) return unauth403(request);
            List<ShopAdmin> list = ShopAdmin.find.query().where()
                    .le("orgId", orgId!=0? orgId: member.orgId)
                    .orderBy().asc("id").findList();
            list.parallelStream().forEach((each) -> {
                List<GroupUser> groupUserList = GroupUser.find.query().where().eq("memberId", each.id)
                        .orderBy().asc("id")
                        .findList();
                each.groupUserList.addAll(groupUserList);
            });
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(list));
            return ok(result);
        });

    }

    /**
     * @api {GET} /v2/s/admin_members/:memberId/ 02查看管理员详情
     * @apiName getAdminMember
     * @apiGroup SHOP-ADMIN
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess {int} id 用户id
     * @apiSuccess {string} userName 用户名
     * @apiSuccess {string} realName 真名
     * @apiSuccess {String} avatar 头像
     * @apiSuccess {String} phoneNumber 手机号码
     * @apiSuccess {boolean} isAdmin 是否是管理员
     * @apiSuccess {String} shopName 归属店铺
     * @apiSuccess {int} status 状态 1正常 2锁定
     * @apiSuccess {String} lastLoginTime 最后登录时间
     * @apiSuccess {String} lastLoginIP 最后登录ip
     * @apiSuccess (Error 40001) {int} code 40001 参数错误
     * @apiSuccess (Error 40002) {int} code 40002 该管理员不存在
     */
    public CompletionStage<Result> getAdminMember(Http.Request request, long memberId) {
        return CompletableFuture.supplyAsync(() -> {
            ShopAdmin admin = businessUtils.getUserIdByAuthToken2(request);
            if (null == admin) return unauth403(request);
            if (memberId < 1) return okCustomJson(request,CODE40001, "base.argument.error");
            ShopAdmin member = ShopAdmin.find.query().where()
                    .eq("orgId", admin.orgId)
                    .eq("id", memberId)
                    .setMaxRows(1)
                    .findOne();
            if (null == member) return okCustomJson(request,CODE40002, "shopAdmin.not.exist");
            ObjectNode result = (ObjectNode) Json.toJson(member);
            result.put("code", 200);
            return ok(result);
        });
    }


    /**
     * @api {POST}  03添加成员
     * @apiName addAdminMember
     * @apiGroup SHOP-ADMIN
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiParam {string} userName 用户名
     * @apiParam {string} phoneNumber 手机号码
     * @apiParam {string} realName 真名
     * @apiParam {string} password 密码6-20
     * @apiParam {string} [avatar] 头像地址
     * @apiParam {string} [rules] 角色
     * @apiParam {boolean} isAdmin 是否是管理员
     * @apiSuccess (Error 40001) {int} code 40001 参数错误
     * @apiSuccess (Error 40002) {int} code 40002 该管理员已存在
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public CompletionStage<Result> addAdminMember(Http.Request request) {
        JsonNode node = request.body().asJson();
        String ip = request.remoteAddress();
        return CompletableFuture.supplyAsync(() -> {
            ShopAdmin admin = businessUtils.getUserIdByAuthToken2(request);
            if (null == admin) return unauth403(request);
            if (!admin.isAdmin) {
                //输出该条数据
                System.out.println(admin.isAdmin);
                return okCustomJson(request,CODE40001, "shopAdmin.not.auth.add");
            }
            ShopAdmin member = Json.fromJson(node, ShopAdmin.class);
            String password = node.findPath("password").asText();
            member.setPassword(password);
            if (ValidationUtil.isEmpty(member.realName)) return okCustomJson(request,CODE40001, "shopAdmin.name.error");
            if (!ValidationUtil.isValidPassword(member.password)) return okCustomJson(request,CODE40001, "shopAdmin.password.error");
            if (!ValidationUtil.isPhoneNumber(member.userName)) return okCustomJson(request,CODE40001, "shopAdmin.phoneNumber.error");
            List<ShopAdmin> existMembers = ShopAdmin.find.query().where()
                    .eq("userName", member.userName)
                    .findList();
            if (existMembers.size() > 0) return okCustomJson(request,CODE40002, "shopAdmin.phoneNumber.exist");
            long currentTime = dateUtils.getCurrentTimeByMilliSecond();
            String avatar = node.findPath("avatar").asText();
            JsonNode rulesNode = node.findPath("rules");
            String rules = null;
            if (rulesNode.isArray()) {
                // 如果是数组，转换为逗号分隔的字符串
                ArrayNode rulesArray = (ArrayNode) rulesNode;
                List<String> rulesList = new ArrayList<>();
                for (JsonNode ruleNode : rulesArray) {
                    if (!ruleNode.isNull() && !ruleNode.asText().isEmpty()) {
                        rulesList.add(ruleNode.asText());
                    }
                }
                rules = String.join(",", rulesList);
            } else if (!rulesNode.isNull() && !rulesNode.asText().isEmpty()) {
                // 如果是字符串，直接使用
                rules = rulesNode.asText();
            }
            if (!ValidationUtil.isEmpty(rules)) member.setRules(rules);
            if (ValidationUtil.isEmpty(member.rules)) return okCustomJson(request, CODE40001, "角色不能为空");
            if (!ValidationUtil.isEmpty(avatar)) member.setAvatar(avatar);
            member.setPhoneNumber(member.phoneNumber);
            member.setStatus(ShopAdmin.STATUS_NORMAL);
            member.setPassword(encodeUtils.getMd5WithSalt(member.password));
            member.setCreatedTime(currentTime);
            member.setLastLoginIP(ip);
            member.setShopId(admin.shopId);
            member.setShopName(admin.shopName);
            
            // 通过rules查找对应的group，比较最大orgId
            long maxOrgId = admin.orgId; // 默认使用admin的orgId
            if (!ValidationUtil.isEmpty(rules)) {
                // 解析rules（可能是逗号分隔的字符串）
                List<String> ruleNames = Arrays.stream(rules.split(","))
                        .map(String::trim)
                        .filter(name -> !ValidationUtil.isEmpty(name))
                        .collect(Collectors.toList());
                
                // 批量查询所有匹配的Group
                List<Group> matchedGroups = new ArrayList<>();
                if (!ruleNames.isEmpty()) {
                    matchedGroups = Group.find.query()
                            .where()
                            .in("name", ruleNames)
                            .findList();
                }
                
                // 找到最大的orgId
                if (!matchedGroups.isEmpty()) {
                    maxOrgId = matchedGroups.stream()
                            .mapToLong(g -> g.orgId)
                            .max()
                            .orElse(admin.orgId);
                }
            }
            
            member.setOrgId(maxOrgId);
            // 根据maxOrgId查找对应的orgName（如果需要）
            // 这里假设orgName需要从其他地方获取，暂时使用admin的orgName
            member.setOrgName(admin.orgName);
            member.setLastLoginTime(currentTime);
            member.setPinyinAbbr(pinyin4j.toPinYinUppercase(member.realName));

            member.save();
            businessUtils.addOperationLog(request, admin, "添加成员：" + member.toString());
            return okJSON200();
        });

    }

    /**
     * @api {POST} /v2/s/admin_member/:id/ 04修改成员
     * @apiName updateAdminMember
     * @apiGroup SHOP-ADMIN
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiParam {string} [userName] 用户名
     * @apiParam {string} [realName] 真名
     * @apiParam {string} [phoneNumber] 电话号码
     * @apiParam {string} [password] 新密码6-20
     * @apiParam {string} [avatar] 头像地址
     * @apiSuccess (Success 40001) {int} code 40001 参数错误
     * @apiSuccess (Success 40001) {int} code 40002 该管理员不存在
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public CompletionStage<Result> updateAdminMember(Http.Request request, long id) {
        JsonNode node = request.body().asJson();
        return CompletableFuture.supplyAsync(() -> {
            ShopAdmin admin = businessUtils.getUserIdByAuthToken2(request);
            if (null == admin) return unauth403(request);
            ShopAdmin updateMember = Json.fromJson(node, ShopAdmin.class);
            if (null == updateMember) return okCustomJson(request,CODE40001, "base.argument.error");
            ShopAdmin existMember = ShopAdmin.find.query().where()
                    .eq("id", id)
                    .eq("orgId", admin.orgId)
                    .findOne();
            if (null == existMember) return okCustomJson(request,CODE40002, "shopAdmin.not.exist");
            if (!ValidationUtil.isEmpty(updateMember.phoneNumber) && !updateMember.phoneNumber.equals(existMember.phoneNumber)) {
                System.out.println("改了手机号码");
                List<ShopAdmin> existMembers = ShopAdmin.find.query().where()
                        .ne("id", id)
                        .eq("phoneNumber", updateMember.phoneNumber)
                        .findList();
                if (existMembers.size() > 0) return okCustomJson(request,CODE40002, "shopAdmin.phoneNumber.exist");
                existMember.setPhoneNumber(updateMember.phoneNumber);
            }
            if (!ValidationUtil.isEmpty(updateMember.realName) && !updateMember.realName.equals(existMember.realName)) {
                System.out.println("改了名字");
                existMember.setRealName(updateMember.realName);
                existMember.setPinyinAbbr(pinyin4j.toPinYinUppercase(existMember.realName));
            }
            String avatar = node.findPath("avatar").asText();
            if (!ValidationUtil.isEmpty(avatar)) {
                System.out.println("改了头像");
                existMember.setAvatar(avatar);
            }

            JsonNode rulesNode = node.findPath("rules");
            String rules = null;
            if (rulesNode.isArray()) {
                // 如果是数组，转换为逗号分隔的字符串
                ArrayNode rulesArray = (ArrayNode) rulesNode;
                List<String> rulesList = new ArrayList<>();
                for (JsonNode ruleNode : rulesArray) {
                    if (!ruleNode.isNull() && !ruleNode.asText().isEmpty()) {
                        rulesList.add(ruleNode.asText());
                    }
                }
                rules = String.join(",", rulesList);
            } else if (!rulesNode.isNull() && !rulesNode.asText().isEmpty()) {
                // 如果是字符串，直接使用
                rules = rulesNode.asText();
            }

            if (!ValidationUtil.isEmpty(rules)) {
                existMember.setRules(rules);
            }else {
                return okCustomJson(CODE40001, "角色不能为空");
            }
            if (updateMember.shopId > 0 && updateMember.shopId != existMember.shopId) {
                existMember.setShopId(updateMember.shopId);
            }
            if (admin.isAdmin) {
                System.out.println("是管理员");
                int status = node.findPath("status").asInt();
                if (status > 0) existMember.setStatus(status);
                if (node.hasNonNull("isAdmin")) {
                    existMember.setAdmin(node.findPath("isAdmin").asBoolean());
                }
            }
            existMember.save();
            saveGroup(node, existMember);
            businessUtils.addOperationLog(request, admin, "添加成员：" + existMember.toString());
            return okJSON200();
        });
    }

    private void saveGroup(JsonNode node, ShopAdmin existMember) {
        if (node.has("groupIdList")) {
            ArrayNode list = (ArrayNode) node.findPath("groupIdList");
            if (null != list && list.size() > 0) {
                List<GroupUser> groupUsers = new ArrayList<>();
                if (list.size() > 0) {
                    long currentTime = dateUtils.getCurrentTimeByMilliSecond();
                    list.forEach((each) -> {
                        GroupUser groupUser = new GroupUser();
                        Group group = Group.find.byId(each.asInt());
                        if (null != group) {
                            groupUser.setGroupId(group.id);
                            groupUser.setGroupName(group.groupName);
                            groupUser.setMemberId(existMember.id);
                            groupUser.setRealName(existMember.realName);
                            groupUser.setCreateTime(currentTime);
                            groupUsers.add(groupUser);
                        }
                    });
                    if (list.size() > 0) {
                        //删除旧的
                        List<GroupUser> oldGroupUser = GroupUser.find.query().where()
                                .eq("memberId", existMember.id).findList();
                        if (oldGroupUser.size() > 0) DB.deleteAll(oldGroupUser);
                        DB.saveAll(groupUsers);
                    }
                }
            }
        }
    }

    /**
     * @api {POST} /v2/s/admin_member/ 05删除成员
     * @apiName delAdminMember
     * @apiGroup SHOP-ADMIN
     * @apiParam {int} id 管理员id
     * @apiParam {String} operation 操作,"del"为删除
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 40001) {int} code 40001 参数错误
     * @apiSuccess (Success 40002) {int} code 40002 该管理员不存在
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public CompletionStage<Result> delAdminMember(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(() -> {
            ShopAdmin admin = businessUtils.getUserIdByAuthToken2(request);
            if (null == admin) return unauth403(request);
            if (!admin.isAdmin) return okCustomJson(request,CODE40001, "shopAdmin.not.auth.del");
            String operation = jsonNode.findPath("operation").asText();
            if (ValidationUtil.isEmpty(operation) || !operation.equals("del"))
                return okCustomJson(request,CODE40001, "base.argument.error");
            long id = jsonNode.findPath("id").asLong();
            if (id < 1) return okCustomJson(request,CODE40001, "base.argument.error");
            ShopAdmin member = ShopAdmin.find.byId(id);
            if (null == member || member.orgId != admin.orgId) return okCustomJson(request,CODE40002, "shopAdmin.not.exist");
            member.delete();
            List<GroupUser> list = GroupUser.find.query().where().eq("memberId", id).findList();
            if (list.size() > 0) DB.deleteAll(list);
            return okJSON200();
        });
    }


    /**
     * @api {POST} /v2/s/admin_members/status/ 06锁定/解锁成员
     * @apiName lockMember
     * @apiGroup SHOP-ADMIN
     * @apiParam {long} memberId 用户ID
     * @apiParam {int} status 1正常，2锁定
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Error 40001){int} code 40001 用户不存在
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public CompletionStage<Result> setAdminMemberStatus(Http.Request request) {
        JsonNode requestNode = request.body().asJson();
        long memberId = requestNode.findPath("memberId").asLong();
        int status = requestNode.findPath("status").asInt();
        return CompletableFuture.supplyAsync(() -> {
            ShopAdmin admin = businessUtils.getUserIdByAuthToken2(request);
            if (null == admin) return unauth403(request);
            if (!admin.isAdmin) return okCustomJson(request,CODE40001, "shopAdmin.not.auth.del");
            if (memberId < 1) return okCustomJson(request,CODE40001, "base.argument.error");
            if (status != ShopAdmin.STATUS_NORMAL && status != ShopAdmin.STATUS_LOCK)
                return okCustomJson(request,CODE40001, "base.argument.error");
            ShopAdmin member = ShopAdmin.find.byId(memberId);
            if (null == member || member.orgId != admin.orgId) return okCustomJson(request,CODE40001, "user.member.empty");
            member.setStatus(status);
            redis.remove(KEY_LOGIN_MAX_ERROR_TIMES + member.id);
            member.save();
            return okJSON200();
        });
    }


    /**
     * @api {GET} /v2/s/admin_member/info/ 07查看自己详情信息
     * @apiName getAdminMemberInfo
     * @apiGroup Admin-Member
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess {int} id 用户id
     * @apiSuccess {string} name 用户名
     * @apiSuccess {string} avatar avatar
     * @apiSuccess {int} groupId 所在分组id
     * @apiSuccess {long} shopId 机构ID
     * @apiSuccess {String} avatar 头像
     * @apiSuccess (Error 40001) {int} code 40001 参数错误
     * @apiSuccess (Error 40002) {int} code 40002 该管理员不存在
     */
    public CompletionStage<Result> getAdminMemberInfo(Http.Request request) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((shopAdmin) -> {
            if (null == shopAdmin || shopAdmin.orgId < 1) return unauth503(request);
            ShopAdmin member = ShopAdmin.find.byId(shopAdmin.id);
            if (null == member) return unauth403(request);
            ObjectNode result = (ObjectNode) Json.toJson(member);
            result.put("code", 200);
            result.put("id", member.id);
            result.put("name", member.realName);
            result.put("avatar", member.avatar);
            result.put("shopName", member.shopName);
            result.put("shopId", member.shopId);
            result.put("introduction", "");

            List<Integer> groupIdList = new ArrayList<>();
            List<GroupUser> groupUserList = GroupUser.find.query().where()
                    .eq("memberId", shopAdmin.id)
                    .orderBy().asc("id")
                    .findList();
            List<String> roleList = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            groupUserList.parallelStream().forEach((each) -> {
                        roleList.add(each.groupName);
                        sb.append(each.groupName).append(" ");
                        groupIdList.add(each.groupId);
                    }
            );
            boolean isAdmin = false;
            for (GroupUser groupUser : groupUserList) {
                Group group = Group.find.byId(groupUser.groupId);
                if (null != group) {
                    isAdmin = group.isAdmin;
                    if (isAdmin) break;
                }
            }
            result.put("isAdmin", isAdmin);
            result.put("groupName", sb.toString());
            result.set("roles", Json.toJson(roleList));
            result.set("groupIdList", Json.toJson(groupIdList));
            result.put("lastLoginTime", dateUtils.formatToYMDHMSBySecond(member.lastLoginTime));
            result.put("createdTime", dateUtils.formatToYMDHMSBySecond(member.createdTime));
            return ok(result);
        });
    }

    /**
     * @api {POST} /v2/s/bind_member_to_group/ 08批量绑定用户到角色组
     * @apiName bindMemberToGroup
     * @apiGroup Admin-Member
     * @apiParam {long} uid 用户ID
     * @apiParam {JsonArray} list groupId的数组
     * @apiSuccess (Success 200){int} code 200
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public CompletionStage<Result> bindMemberToGroup(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((shopAdmin) -> {
            if (null == shopAdmin || shopAdmin.orgId < 1) return unauth503(request);
            if (null == jsonNode) return okCustomJson(request,CODE40001, "base.argument.error");
            long uid = jsonNode.findPath("uid").asLong();
            ShopAdmin member = ShopAdmin.find.byId(uid);
            if (null == member) return okCustomJson(request,CODE40001, "user.id.error");

            ArrayNode list = (ArrayNode) jsonNode.findPath("list");
            if (null != list && list.size() > 0) {
                List<GroupUser> groupUsers = new ArrayList<>();
                if (list.size() > 0) {
                    long currentTime = dateUtils.getCurrentTimeByMilliSecond();
                    list.forEach((node) -> {
                        GroupUser groupUser = new GroupUser();
                        Group group = Group.find.byId(node.asInt());
                        if (null != group) {
                            groupUser.setGroupId(group.id);
                            groupUser.setGroupName(group.groupName);
                            groupUser.setMemberId(uid);
                            groupUser.setRealName(member.realName);
                            groupUser.setCreateTime(currentTime);
                            groupUsers.add(groupUser);
                        }
                    });
                    if (list.size() > 0) {
                        //删除旧的
                        List<GroupUser> oldGroupUser = GroupUser.find.query().where()
                                .eq("memberId", member.id).findList();
                        if (oldGroupUser.size() > 0) DB.deleteAll(oldGroupUser);
                        DB.saveAll(groupUsers);
                    }
                }
            }
            businessUtils.addOperationLog(request, shopAdmin, "批量修改用户角色：" + jsonNode.toString());
            return okJSON200();
        });
    }

    /**
     * @api {GET} /v2/s/user_groups/?memberId= 09用户所属分组
     * @apiName listUserGroups
     * @apiGroup Admin-Member
     * @apiSuccess {json} list
     * @apiSuccess (Success 200) {int} code 200 请求成功
     */
    public CompletionStage<Result> listUserGroups(Http.Request request, long memberId) {
        return CompletableFuture.supplyAsync(() -> {
            List<GroupUser> list = GroupUser.find.query().where().eq("memberId", memberId).findList();
            ObjectNode node = Json.newObject();
            node.put("code", 200);
            node.set("list", Json.toJson(list));
            return ok(node);
        });
    }
}
