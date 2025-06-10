package controllers.basic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.BaseSecurityController;
import models.admin.ShopAdmin;
import models.admin.Group;
import models.admin.GroupUser;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import utils.BizUtils;
import utils.ValidationUtil;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


/**
 * 组里用户控制器
 */
public class GroupUserController extends BaseSecurityController {
    @Inject
    BizUtils bizUtils;
    /**
     * @api {POST} /v2/s/group_user/new/ 01成员加入组
     * @apiName addGroupUser
     * @apiGroup Admin-GROUP-USER
     * @apiParam {int} groupId 组id
     * @apiParam {long} memberId 成员id
     * @apiSuccess (Success 40001) {int} code 40001 参数错误
     * @apiSuccess (Success 40002) {int} code 40002 该成员已属于该组
     * @apiSuccess (Success 200) {int} code 200 请求成功
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> addGroupUser(Http.Request request) {
        JsonNode node = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((shopAdmin) -> {
            if (null == shopAdmin || shopAdmin.orgId < 1) return unauth503(request);
            int groupId = node.findPath("groupId").asInt();
            long memberId = node.findPath("memberId").asLong();
            if (groupId < 1 || memberId < 1) return okCustomJson(request,CODE40001, "base.argument.error");
            Group group = Group.find.byId(groupId);
            if (null == group) return okCustomJson(request,CODE40001, "action.group.not.exist");
            ShopAdmin member = ShopAdmin.find.byId(memberId);
            if (null == member) return okCustomJson(request,CODE40001, "shopAdmin.not.exist");
            List<GroupUser> groupUsers = GroupUser.find.query().where().eq("groupId", groupId).eq("memberId", memberId).findList();
            if (groupUsers.size() > 0) return okCustomJson(request,CODE40002, "group.member.exist");
            GroupUser newGroupUser = new GroupUser();
            newGroupUser.setGroupId(groupId);
            newGroupUser.setGroupName(group.groupName);
            newGroupUser.setMemberId(memberId);
            newGroupUser.setRealName(member.realName);
            newGroupUser.setCreateTime(dateUtils.getCurrentTimeByMilliSecond());
            newGroupUser.save();
            String prefixMemberJoinGroup = bizUtils.getMessageValue(request, "prefix.member.join.group");
            businessUtils.addOperationLog(request, shopAdmin, prefixMemberJoinGroup + newGroupUser.toString());
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/s/group_user/ 02成员移出组
     * @apiName delGroupUser
     * @apiGroup Admin-GROUP-USER
     * @apiParam {int} groupId 组id
     * @apiParam {int} memberId 成员id
     * @apiParam {String} operation 操作,"del"为删除
     * @apiSuccess (Success 40001) {int} code 40001 参数错误
     * @apiSuccess (Success 40001) {int} code 40002 分组不存在
     * @apiSuccess (Success 200) {int} code 200 请求成功
     */
    public CompletionStage<Result> delGroupUser(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((shopAdmin) -> {
            if (null == shopAdmin || shopAdmin.orgId < 1) return unauth503(request);
            String operation = jsonNode.findPath("operation").asText();
            if (ValidationUtil.isEmpty(operation) || !operation.equals("del")) return okCustomJson(request,CODE40001, "参数错误");
            int groupId = jsonNode.findPath("groupId").asInt();
            int memberId = jsonNode.findPath("memberId").asInt();
            if (groupId < 1 || memberId < 1) return okCustomJson(request,CODE40001, "base.argument.error");
            GroupUser groupUser = GroupUser.find.query().where().eq("groupId", groupId).eq("memberId", memberId).findOne();
            if (null == groupUser) return okCustomJson(request,CODE40002, "group.not.have.group");
            String prefixDelMember = bizUtils.getMessageValue(request, "prefix.del.member");
            businessUtils.addOperationLog(request, shopAdmin, prefixDelMember + groupUser.toString());
            groupUser.delete();
            return okJSON200();
        });
    }

    /**
     * @api {GET} /v2/s/group_user/:groupId/ 03获取指定组的成员列表
     * @apiName listUsersByGroupId
     * @apiGroup Admin-GROUP-USER
     * @apiSuccess {jsonArray} list
     * @apiSuccess {int} groupId 组的id
     * @apiSuccess {string} groupName 组名
     * @apiSuccess {int} memberId 组员的id
     * @apiSuccess {string} realName 组员的名字
     * @apiSuccess (Success 200) {int} code 200 请求成功
     */
    public CompletionStage<Result> listUsersByGroupId(Http.Request request,int groupId) {
        return CompletableFuture.supplyAsync(() -> {
            if (groupId < 1) return okCustomJson(request,CODE40001, "base.argument.error");
            List<GroupUser> users = GroupUser.find.query().where().eq("groupId", groupId).findList();
            ObjectNode node = Json.newObject();
            node.put(CODE, CODE200);
            node.set("list", Json.toJson(users));
            return ok(node);
        });
    }
}
