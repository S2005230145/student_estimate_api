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
     * @apiGroup 评价规则配置模块
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
     * @apiSuccessExample {json} 响应示例:
     * {
     *     "pages": 1,
     *     "hasNest": false,
     *     "code": 200,
     *     "list": [
     *         {
     *             "orgId": 1,
     *             "id": 5,
     *             "name": "生活素养",
     *             "scoreBasic": 3.0,
     *             "scoreMax": 7.0,
     *             "badgeType": "劳",
     *             "description": "",
     *             "active": true,
     *             "createTime": 1766463839123,
     *             "badges": [
     *                 {
     *                     "orgId": 1,
     *                     "id": 35,
     *                     "badgeId": 5,
     *                     "badgeName": "自理能力",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766465112834
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 36,
     *                     "badgeId": 5,
     *                     "badgeName": "物品归位",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766465120764
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 37,
     *                     "badgeId": 5,
     *                     "badgeName": "家务分担",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766465128379
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 38,
     *                     "badgeId": 5,
     *                     "badgeName": "值日负责",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766465138269
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 39,
     *                     "badgeId": 5,
     *                     "badgeName": "生命教育",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766465147973
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 40,
     *                     "badgeId": 5,
     *                     "badgeName": "合作意识",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766465156953
     *                 }
     *             ]
     *         },
     *         {
     *             "orgId": 1,
     *             "id": 4,
     *             "name": "生活素养",
     *             "scoreBasic": 3.0,
     *             "scoreMax": 7.0,
     *             "badgeType": "美",
     *             "description": "",
     *             "active": true,
     *             "createTime": 1766463812306,
     *             "badges": [
     *                 {
     *                     "orgId": 1,
     *                     "id": 32,
     *                     "badgeId": 4,
     *                     "badgeName": "经典诵读",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766465068581
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 33,
     *                     "badgeId": 4,
     *                     "badgeName": "艺术培养",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766465082651
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 34,
     *                     "badgeId": 4,
     *                     "badgeName": "舞台展示",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766465089492
     *                 }
     *             ]
     *         },
     *         {
     *             "orgId": 1,
     *             "id": 3,
     *             "name": "生活素养",
     *             "scoreBasic": 3.0,
     *             "scoreMax": 7.0,
     *             "badgeType": "体",
     *             "description": "",
     *             "active": true,
     *             "createTime": 1766463785608,
     *             "badges": [
     *                 {
     *                     "orgId": 1,
     *                     "id": 26,
     *                     "badgeId": 3,
     *                     "badgeName": "规律作息",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766465002690
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 27,
     *                     "badgeId": 3,
     *                     "badgeName": "用眼卫生",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766465016336
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 28,
     *                     "badgeId": 3,
     *                     "badgeName": "个人卫生",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766465026984
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 29,
     *                     "badgeId": 3,
     *                     "badgeName": "日常锻炼",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766465033092
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 30,
     *                     "badgeId": 3,
     *                     "badgeName": "饮食健康",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766465041255
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 31,
     *                     "badgeId": 3,
     *                     "badgeName": "运动安全",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766465050019
     *                 }
     *             ]
     *         },
     *         {
     *             "orgId": 1,
     *             "id": 2,
     *             "name": "生活素养",
     *             "scoreBasic": 3.0,
     *             "scoreMax": 9.0,
     *             "badgeType": "智",
     *             "description": "",
     *             "active": true,
     *             "createTime": 1766463764929,
     *             "badges": [
     *                 {
     *                     "orgId": 1,
     *                     "id": 15,
     *                     "badgeId": 2,
     *                     "badgeName": "课堂专注",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766464810944
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 16,
     *                     "badgeId": 2,
     *                     "badgeName": "规范书写",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766464818350
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 17,
     *                     "badgeId": 2,
     *                     "badgeName": "积极发言",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766464828244
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 18,
     *                     "badgeId": 2,
     *                     "badgeName": "作业规范",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766464836113
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 19,
     *                     "badgeId": 2,
     *                     "badgeName": "电子管理",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766464865563
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 20,
     *                     "badgeId": 2,
     *                     "badgeName": "每日阅读",
     *                     "badgeImage": null,
     *                     "description": "坚持每天自主读20-30分钟课外书，读完能把内容讲出来；高年级学生可以尝试写读后感，制作思维导图等。（校长作业）",
     *                     "active": true,
     *                     "createTime": 1766464899469
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 21,
     *                     "badgeId": 2,
     *                     "badgeName": "物品整理",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766464922457
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 22,
     *                     "badgeId": 2,
     *                     "badgeName": "时间管理",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766464958219
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 23,
     *                     "badgeId": 2,
     *                     "badgeName": "问题解决",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766464969957
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 24,
     *                     "badgeId": 2,
     *                     "badgeName": "复习习惯",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766464976107
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 25,
     *                     "badgeId": 2,
     *                     "badgeName": "考试规划",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766464982989
     *                 }
     *             ]
     *         },
     *         {
     *             "orgId": 1,
     *             "id": 1,
     *             "name": "生活素养",
     *             "scoreBasic": 3.0,
     *             "scoreMax": 12.0,
     *             "badgeType": "德",
     *             "description": "",
     *             "active": true,
     *             "createTime": 1766461919467,
     *             "badges": [
     *                 {
     *                     "orgId": 1,
     *                     "id": 1,
     *                     "badgeId": 1,
     *                     "badgeName": "升旗礼仪",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766464656430
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 2,
     *                     "badgeId": 1,
     *                     "badgeName": "见面礼仪",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766464664323
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 3,
     *                     "badgeId": 1,
     *                     "badgeName": "交往礼仪",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766464673365
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 4,
     *                     "badgeId": 1,
     *                     "badgeName": "交谈礼仪",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766464681526
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 5,
     *                     "badgeId": 1,
     *                     "badgeName": "倾听礼仪",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766464697282
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 6,
     *                     "badgeId": 1,
     *                     "badgeName": "会客礼仪",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766464703948
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 7,
     *                     "badgeId": 1,
     *                     "badgeName": "访客礼仪",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766464712176
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 8,
     *                     "badgeId": 1,
     *                     "badgeName": "外出礼仪",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766464718170
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 9,
     *                     "badgeId": 1,
     *                     "badgeName": "行走礼仪",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766464729539
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 10,
     *                     "badgeId": 1,
     *                     "badgeName": "仪表礼仪",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766464739948
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 11,
     *                     "badgeId": 1,
     *                     "badgeName": "就餐礼仪",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766464753823
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 12,
     *                     "badgeId": 1,
     *                     "badgeName": "公共礼仪",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766464761472
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 13,
     *                     "badgeId": 1,
     *                     "badgeName": "责任担当",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766464769958
     *                 },
     *                 {
     *                     "orgId": 1,
     *                     "id": 14,
     *                     "badgeId": 1,
     *                     "badgeName": "情绪管理",
     *                     "badgeImage": null,
     *                     "description": "",
     *                     "active": true,
     *                     "createTime": 1766464783716
     *                 }
     *             ]
     *         }
     *     ]
     * }
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
     * @apiGroup 评价规则配置模块
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
     * @apiGroup 评价规则配置模块
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
     * @apiGroup 评价规则配置模块
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
     * @apiGroup 评价规则配置模块
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
