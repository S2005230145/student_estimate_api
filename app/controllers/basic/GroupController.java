package controllers.basic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.BaseSecurityController;
import models.admin.Group;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import utils.BizUtils;
import utils.ValidationUtil;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Created by win7 on 2016/8/2.
 */
public class GroupController extends BaseSecurityController {
    @Inject
    BizUtils bizUtils;

    /**
     * @api {GET} /v2/s/groups/:groupId/ 01权限组详情
     * @apiName getGroup
     * @apiGroup Admin-GROUP
     * @apiSuccess {string} groupName 组名
     * @apiSuccess {string} remark 组－备注
     * @apiSuccess {long} createdTime 创建时间
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Error 40001) {int} code 40001 参数错误
     * @apiSuccess (Error 40002) {int} code 40002 该组不存在
     */
    public CompletionStage<Result> getGroup(Http.Request request,int groupId) {
        return CompletableFuture.supplyAsync(() -> {
            if (groupId < 1) return okCustomJson(request,CODE40001, "base.argument.error");
            Group group = Group.find.byId(groupId);
            if (null == group) return okCustomJson(request,CODE40002, "action.group.not.exist");
            ObjectNode node = (ObjectNode) Json.toJson(group);
            node.put("code", 200);
            return ok(node);
        });
    }

    /**
     * @api {GET} /v2/s/groups/ 02分组列表
     * @apiName listGroups
     * @apiGroup Admin-GROUP
     * @apiSuccess {json} list
     * @apiSuccess {string} groupName 组名
     * @apiSuccess {string} groupRemark 组－备注
     * @apiSuccess {boolean} isAdmin
     * @apiSuccess {long} createdTimeForShow 创建时间
     * @apiSuccess (Success 200) {int} code 200 请求成功
     */
    public CompletionStage<Result> listGroups() {
        return CompletableFuture.supplyAsync(() -> {
            List<Group> list = Group.find.all();
            ObjectNode node = Json.newObject();
            node.put("code", 200);
            node.set("list", Json.toJson(list));
            return ok(node);
        });
    }

    /**
     * @api {POST} /v2/s/group/new/ 03添加分组
     * @apiName addGroup
     * @apiGroup Admin-GROUP
     * @apiParam {string} groupName 组名
     * @apiParam {string} description 组－备注
     * @apiParam {boolean} isAdmin
     * @apiSuccess (Success 40001) {int} code 40001 参数错误
     * @apiSuccess (Success 40002) {int} code 40002 该组已存在
     * @apiSuccess (Success 200) {int} code 200 请求成功
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> addGroup(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((shopAdmin) -> {
            if (null == shopAdmin || shopAdmin.orgId < 1) return unauth503(request);
            Group group = Json.fromJson(jsonNode, Group.class);
            if (ValidationUtil.isEmpty(group.groupName) ||
                    ValidationUtil.isEmpty(group.description)) return okCustomJson(request,CODE40001, "base.argument.error");
            Group existGroup = Group.find.query().where().eq("groupName", group.groupName).findOne();
            if (null != existGroup) return okCustomJson(request,CODE40002, "group.exist");
            group.setCreatedTime(dateUtils.getCurrentTimeByMilliSecond());
            group.save();

            businessUtils.addOperationLog(request, shopAdmin, "添加分组：" + group.toString());
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/s/group/:id/ 04修改分组
     * @apiName updateGroup
     * @apiGroup Admin-GROUP
     * @apiParam {string} groupName 组名
     * @apiParam {string} description 组－备注
     * @apiParam {boolean} isAdmin
     * @apiSuccess (Success 40001) {int} code 40001 参数错误
     * @apiSuccess (Success 40001) {int} code 40002 分组不存在
     * @apiSuccess (Success 200) {int} code 200 请求成功
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> updateGroup(Http.Request request, int id) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((shopAdmin) -> {
            if (null == shopAdmin || shopAdmin.orgId < 1) return unauth503(request);
            if (id < 1) return okCustomJson(request,CODE40001, "base.argument.error");
            Group group = Group.find.byId(id);
            if (null == group) return okCustomJson(request,CODE40002, "action.group.not.exist");
            String prefixUpdateGroupBeforeWork = bizUtils.getMessageValue(request, "prefix.update.group.before.work");
            businessUtils.addOperationLog(request, shopAdmin, prefixUpdateGroupBeforeWork + group.toString() + prefixUpdateGroupBeforeWork + jsonNode.toString());

            boolean changed = false;
            String groupName = jsonNode.findPath("groupName").asText();
            if (!ValidationUtil.isEmpty(groupName) && !groupName.equals(group.groupName)) {
                changed = true;
                group.setGroupName(groupName);
            }
            String description = jsonNode.findPath("description").asText();
            if (!ValidationUtil.isEmpty(description) && !description.equals(group.description)) {
                changed = true;
                group.setDescription(description);
            }
            if (jsonNode.has("isAdmin")) {
                changed = true;
                group.setAdmin(jsonNode.findPath("isAdmin").asBoolean());
            }
            if (changed) group.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/s/group/ 05删除分组
     * @apiName delGroup
     * @apiGroup Admin-GROUP
     * @apiParam {int} id 组的id
     * @apiParam {String} operation 操作,"del"为删除
     * @apiSuccess (Success 40001) {int} code 40001 参数错误
     * @apiSuccess (Success 40001) {int} code 40002 分组不存在
     * @apiSuccess (Success 200) {int} code 200 请求成功
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> delGroup(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((shopAdmin) -> {
            if (null == shopAdmin || shopAdmin.orgId < 1) return unauth503(request);
            String operation = jsonNode.findPath("operation").asText();
            if (ValidationUtil.isEmpty(operation) || !operation.equals("del"))
                return okCustomJson(request,CODE40001, "base.argument.error");
            int id = jsonNode.findPath("id").asInt();
            if (id < 1) return okCustomJson(request,CODE40001, "base.argument.error");
            Group group = Group.find.byId(id);
            if (null == group) return okCustomJson(request,CODE40002, "action.group.not.exist");
            String prefixDelGroup = bizUtils.getMessageValue(request, "prefix.del.group");
            businessUtils.addOperationLog(request, shopAdmin, prefixDelGroup + group.toString());
            group.delete();
            return okJSON200();
        });
    }

}
