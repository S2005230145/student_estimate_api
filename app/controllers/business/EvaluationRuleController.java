package controllers.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import controllers.BaseSecurityController;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.business.Badge;
import models.business.EvaluationRule;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import utils.ValidationUtil;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class EvaluationRuleController extends BaseSecurityController {

    /**
     * @api {GET} /v2/p/evaluation_rule_list/   01列表-评价规则配置
     * @apiName listEvaluationRule
     * @apiGroup EVALUATION-RULE-CONTROLLER
     * @apiParam {int} page 页码
     * @apiParam {String} filter 搜索栏()
     * @apiParam {String} name 指标名称
     * @apiParam {double} scoreBasic 类型基础分
     * @apiParam {double} scoreMax 类型上限分
     * @apiSuccess (Success 200) {long} orgId 机构ID
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {String} badgeType 徽章类型
     * @apiSuccess (Success 200) {String} name 指标名称
     * @apiSuccess (Success 200) {String} description 描述
     * @apiSuccess (Success 200) {boolean} active 是否启用
     * @apiSuccess (Success 200) {long} createTime 创建时间
     */
    public CompletionStage<Result> listEvaluationRule(Http.Request request, int page, String filter, int status) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ExpressionList<EvaluationRule> expressionList = EvaluationRule.find.query().where().eq("org_id", adminMember.getOrgId());
            if (status > 0) expressionList.eq("status", status);
            if (!ValidationUtil.isEmpty(filter)) {
                expressionList.or()
                        .icontains("name", filter)
                        .icontains("description", filter)
                        .endOr();
            }

            ObjectNode result = Json.newObject();
            List<EvaluationRule> list;
            if (page == 0) list = expressionList.findList();
            else {
                PagedList<EvaluationRule> pagedList = expressionList
                        .order().desc("id")
                        .setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_20)
                        .setMaxRows(BusinessConstant.PAGE_SIZE_20)
                        .findPagedList();
                list = pagedList.getList();
                result.put("pages", pagedList.getTotalPageCount());
                result.put("hasNest", pagedList.hasNext());
            }
            
            // 为每个 EvaluationRule 查询关联的 Badge 列表（一对多）
            for (EvaluationRule rule : list) {
                // badge_id 对应 EvaluationRule 的 id
                List<Badge> badges = Badge.find.query().where()
                        .eq("org_id", adminMember.getOrgId())
                        .eq("badge_id", rule.id)
                        .findList();
                rule.badges = badges;
            }

            // 构建返回结果，包含 Badge 列表
            com.fasterxml.jackson.databind.node.ArrayNode listNode = Json.newArray();
            for (EvaluationRule rule : list) {
                ObjectNode ruleNode = (ObjectNode) Json.toJson(rule);
                ruleNode.set("badges", Json.toJson(rule.badges));
                listNode.add(ruleNode);
            }
            
            result.put(CODE, CODE200);
            result.set("list", listNode);
            return ok(result);
        });

    }

    /**
     * @api {GET} /v2/p/evaluation_rule/:id/  02详情-EvaluationRule评价规则配置
     * @apiName getEvaluationRule
     * @apiGroup EVALUATION-RULE-CONTROLLER
     * @apiParam {long} id id
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {long} orgId 机构ID
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiParam {double} scoreBasic 类型基础分
     * @apiParam {double} scoreMax 类型上限分
     * @apiSuccess (Success 200) {String} badgeType 徽章类型
     * @apiSuccess (Success 200) {String} description 描述
     * @apiSuccess (Success 200) {boolean} active 是否启用
     * @apiSuccess (Success 200) {long} createTime 创建时间
     */
    public CompletionStage<Result> getEvaluationRule(Http.Request request, long id) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            EvaluationRule evaluationRule = EvaluationRule.find.byId(id);
            if (null == evaluationRule) return okCustomJson(CODE40001, "数据不存在");
            //sass数据校验  
            if (evaluationRule.orgId != adminMember.getOrgId()) return okCustomJson(CODE40001, "数据不存在");
            
            // 查询关联的 Badge 列表（一对多）
            // badge_id 对应 EvaluationRule 的 id
            List<Badge> badges = Badge.find.query().where()
                    .eq("org_id", adminMember.getOrgId())
                    .eq("badge_id", (int) evaluationRule.id)
                    .findList();
            evaluationRule.badges = badges;
            
            ObjectNode result = (ObjectNode) Json.toJson(evaluationRule);
            result.set("badges", Json.toJson(badges));
            result.put(CODE, CODE200);
            return ok(result);
        });

    }

    /**
     * @api {POST} /v2/p/evaluation_rule/new/   01添加-EvaluationRule评价规则配置
     * @apiName addEvaluationRule
     * @apiDescription 描述
     * @apiGroup EVALUATION-RULE-CONTROLLER
     * @apiParam {long} orgId 机构ID
     * @apiParam {long} id 唯一标识
     * @apiParam {double} scoreBasic 类型基础分
     * @apiParam {double} scoreMax 类型上限分
     * @apiSuccess (Success 200) {String} badgeType 徽章类型
     * @apiSuccess (Success 200) {String} description 描述
     * @apiSuccess (Success 200) {boolean} active 是否启用
     * @apiSuccess (Success 200) {long} createTime 创建时间
     * @apiSuccess (Success 200){int} code 200
     */

    public CompletionStage<Result> addEvaluationRule(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((admin) -> {
            if (null == admin) return unauth403();
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");
            EvaluationRule evaluationRule = Json.fromJson(jsonNode, EvaluationRule.class);
// 数据sass化
            evaluationRule.setOrgId(admin.getOrgId());
            long currentTimeBySecond = dateUtils.getCurrentTimeByMilliSecond();
            evaluationRule.setCreateTime(currentTimeBySecond);
            evaluationRule.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/evaluation_rule/:id/  04更新-EvaluationRule评价规则配置
     * @apiName updateEvaluationRule
     * @apiGroup EVALUATION-RULE-CONTROLLER
     * @apiParam {long} orgId 机构ID
     * @apiParam {long} id 唯一标识

     * @apiParam {double} scoreBasic 类型基础分
     * @apiParam {double} scoreMax 类型上限分
     * @apiSuccess (Success 200) {String} badgeType 徽章类型
     * @apiSuccess (Success 200) {String} description 描述
     * @apiSuccess (Success 200) {boolean} active 是否启用
     * @apiSuccess (Success 200) {long} createTime 创建时间
     * @apiSuccess (Success 200){int} code 200
     */
    public CompletionStage<Result> updateEvaluationRule(Http.Request request, long id) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            EvaluationRule originalEvaluationRule = EvaluationRule.find.byId(id);
            EvaluationRule newEvaluationRule = Json.fromJson(jsonNode, EvaluationRule.class);
            if (null == originalEvaluationRule) return okCustomJson(CODE40001, "数据不存在");
            //sass数据校验  
            if (originalEvaluationRule.orgId != adminMember.getOrgId()) return okCustomJson(CODE40001, "数据不存在");
            if (newEvaluationRule.scoreBasic > 0) originalEvaluationRule.setScoreBasic(newEvaluationRule.scoreBasic);
            if (newEvaluationRule.scoreMax > 0) originalEvaluationRule.setScoreMax(newEvaluationRule.scoreMax);
            if (!ValidationUtil.isEmpty(newEvaluationRule.badgeType))
                originalEvaluationRule.setBadgeType(newEvaluationRule.badgeType);
            if (!ValidationUtil.isEmpty(newEvaluationRule.description))
                originalEvaluationRule.setDescription(newEvaluationRule.description);
            if (newEvaluationRule.active != originalEvaluationRule.active)
                originalEvaluationRule.setActive(newEvaluationRule.active);

            originalEvaluationRule.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/evaluation_rule/   05删除-评价规则配置
     * @apiName deleteEvaluationRule
     * @apiGroup EVALUATION-RULE-CONTROLLER
     * @apiParam {long} id id
     * @apiParam {String} operation del时删除
     * @apiSuccess (Success 200){int} 200 成功
     */
    public CompletionStage<Result> deleteEvaluationRule(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            long id = jsonNode.findPath("id").asLong();
            String operation = jsonNode.findPath("operation").asText();
            if (!"del".equals(operation)) return okCustomJson(CODE40001, "操作错误");
            EvaluationRule deleteModel = EvaluationRule.find.byId(id);
            if (null == deleteModel) return okCustomJson(CODE40001, "数据不存在");
            //sass数据校验  
            if (deleteModel.orgId != adminMember.getOrgId()) return okCustomJson(CODE40001, "数据不存在");
            deleteModel.delete();
            return okJSON200();
        });
    }

}
