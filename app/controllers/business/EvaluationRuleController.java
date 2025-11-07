package controllers.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import controllers.BaseSecurityController;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.business.EvaluationRule;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import utils.ValidationUtil;

import java.util.List;
import java.util.concurrent.CompletionStage;

public class EvaluationRuleController extends BaseSecurityController {

    /**
     * @api {GET} /v2/p/evaluation_rule_list/   01列表-评价规则配置
     * @apiName listEvaluationRule
     * @apiGroup EVALUATION-RULE-MANAGER
     * @apiParam {int} page 页码
     * @apiParam {String} filter 搜索栏()
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {String} ruleType 规则类型
     * @apiSuccess (Success 200) {String} condition 条件
     * @apiSuccess (Success 200) {double} score 得分
     * @apiSuccess (Success 200) {String} badgeType 徽章类型
     * @apiSuccess (Success 200) {String} description 描述
     * @apiSuccess (Success 200) {boolean} active 是否启用
     * @apiSuccess (Success 200) {long} createTime 创建时间
     */
    public CompletionStage<Result> listEvaluationRule(Http.Request request, int page, String filter, int status) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ExpressionList<EvaluationRule> expressionList = EvaluationRule.find.query().where();
            if (status > 0) expressionList.eq("status", status);
            if (!ValidationUtil.isEmpty(filter)) expressionList
                    .or()
                    .icontains("filter", filter)
                    .endOr();               //编写其他条件  
            //编写其他条件
            //编写其他条件
            //编写其他条件

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
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(list));
            return ok(result);

        });

    }

    /**
     * @api {GET} /v2/p/evaluation_rule/:id/  02详情-EvaluationRule评价规则配置
     * @apiName getEvaluationRule
     * @apiGroup EVALUATION-RULE-MANAGER
     * @apiParam {long} id id
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {String} ruleType 规则类型
     * @apiSuccess (Success 200) {String} condition 条件
     * @apiSuccess (Success 200) {double} score 得分
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
            ObjectNode result = (ObjectNode) Json.toJson(evaluationRule);
            result.put(CODE, CODE200);
            return ok(result);
        });

    }

    /**
     * @api {POST} /v2/p/evaluation_rule/new/   01添加-EvaluationRule评价规则配置
     * @apiName addEvaluationRule
     * @apiDescription 描述
     * @apiGroup EVALUATION-RULE-MANAGER
     * @apiParam {long} id 唯一标识
     * @apiParam {String} ruleType 规则类型
     * @apiParam {String} condition 条件
     * @apiParam {double} score 得分
     * @apiParam {String} badgeType 徽章类型
     * @apiParam {String} description 描述
     * @apiParam {boolean} active 是否启用
     * @apiParam {long} createTime 创建时间
     * @apiSuccess (Success 200){int} code 200
     */

    public CompletionStage<Result> addEvaluationRule(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((admin) -> {
            if (null == admin) return unauth403();
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");
            EvaluationRule evaluationRule = Json.fromJson(jsonNode, EvaluationRule.class);

            long currentTimeBySecond = dateUtils.getCurrentTimeByMilliSecond();
            evaluationRule.setCreateTime(currentTimeBySecond);
            evaluationRule.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/evaluation_rule/:id/  04更新-EvaluationRule评价规则配置
     * @apiName updateEvaluationRule
     * @apiGroup EVALUATION-RULE-MANAGER
     * @apiParam {long} id 唯一标识
     * @apiParam {String} ruleType 规则类型
     * @apiParam {String} condition 条件
     * @apiParam {double} score 得分
     * @apiParam {String} badgeType 徽章类型
     * @apiParam {String} description 描述
     * @apiParam {boolean} active 是否启用
     * @apiParam {long} createTime 创建时间
     * @apiSuccess (Success 200){int} code 200
     */
    public CompletionStage<Result> updateEvaluationRule(Http.Request request, long id) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            EvaluationRule originalEvaluationRule = EvaluationRule.find.byId(id);
            EvaluationRule newEvaluationRule = Json.fromJson(jsonNode, EvaluationRule.class);
            if (null == originalEvaluationRule) return okCustomJson(CODE40001, "数据不存在");
            if (!ValidationUtil.isEmpty(newEvaluationRule.ruleType))
                originalEvaluationRule.setRuleType(newEvaluationRule.ruleType);
            if (!ValidationUtil.isEmpty(newEvaluationRule.conditions))
                originalEvaluationRule.setConditions(newEvaluationRule.conditions);
            if (newEvaluationRule.score > 0) originalEvaluationRule.setScore(newEvaluationRule.score);
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
     * @apiGroup EVALUATION-RULE-MANAGER
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
            deleteModel.delete();
            return okJSON200();
        });
    }
}
