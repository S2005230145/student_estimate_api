package controllers.basic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.BaseController;
import io.ebean.ExpressionList;
import models.system.ParamConfig;
import play.Logger;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import utils.ValidationUtil;

import java.util.List;
import java.util.concurrent.CompletionStage;

public class SystemController extends BaseController {

    Logger.ALogger logger = Logger.of(SystemController.class);

    /**
     * @api {GET} /v2/s/param_config/?page=&key= 01获取配置列表
     * @apiName listParamConfig
     * @apiGroup SHOP-PARAM-CONFIG
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} pages 页数
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200){int} id 配置id
     * @apiSuccess (Success 200){String} key key
     * @apiSuccess (Success 200){String} value 值
     * @apiSuccess (Success 200){String} note 中文备注
     * @apiSuccess (Success 40001) {int} code 40001 参数错误
     * @apiSuccess (Success 40002) {int} code 40002 配置不存在
     * @apiSuccess (Success 40003) {int} code 40003 该配置的KEY已存在
     */
    public CompletionStage<Result> listParamConfig(Http.Request request, String key) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((admin) -> {
            if (null == admin || admin.orgId < 1) return unauth503(request);
            ExpressionList<ParamConfig> expressionList = ParamConfig.find.query().where()
                    .eq("orgId", admin.orgId)
                    .eq("enable", true);
            if (!ValidationUtil.isEmpty(key)) expressionList.icontains("key", key);
            List<ParamConfig> list = expressionList.orderBy().desc("id").orderBy().asc("key")
                    .findList();
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(list));
            return ok(result);
        });
    }

    /**
     * @api {GET} /v2/s/param_config/param_config/:configId/ 02获取配置详情
     * @apiName getParamConfig
     * @apiGroup SHOP-PARAM-CONFIG
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200){int} id 配置id
     * @apiSuccess (Success 200){String} key key
     * @apiSuccess (Success 200){String} value 值
     * @apiSuccess (Success 200){String} note 中文备注
     * @apiSuccess (Success 40001) {int} code 40001 参数错误
     * @apiSuccess (Success 40002) {int} code 40002 配置不存在
     * @apiSuccess (Success 40003) {int} code 40003 该配置的KEY已存在
     */
    public CompletionStage<Result> getParamConfig(Http.Request request, long configId) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((admin) -> {
            if (null == admin || admin.orgId < 1) return unauth503(request);
            if (configId < 1) return okCustomJson(request, CODE40001, "base.argument.error");
            ParamConfig config = ParamConfig.find.query().where()
                    .eq("id", configId)
                    .eq("orgId", admin.orgId)
                    .eq("enable", true)
                    .setMaxRows(1)
                    .findOne();
            if (null == config) return okCustomJson(request, CODE40002, "base.config.not.found");
            ObjectNode result = (ObjectNode) Json.toJson(config);
            result.put(CODE, CODE200);
            return ok(result);
        });
    }

    /**
     * @api {POST} /v2/s/param_config/new/ 03增加配置
     * @apiName addParamConfig
     * @apiGroup ADMIN-CONFIG
     * @apiParam {String} key key
     * @apiParam {String} value 值
     * @apiParam {String} note 中文备注
     * @apiSuccess (Success 40001) {int} code 40001 参数错误
     * @apiSuccess (Success 40002) {int} code 40002 配置不存在
     * @apiSuccess (Success 40003) {int} code 40003 该配置的KEY已存在
     * @apiSuccess (Success 200) {int} code 200 请求成功
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public CompletionStage<Result> addParamConfig(Http.Request request) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((admin) -> {
            if (null == admin || admin.orgId < 1) return unauth503(request);
            JsonNode requestNode = request.body().asJson();
            if (null == requestNode) return okCustomJson(CODE40001, "参数错误");
            ParamConfig param = Json.fromJson(requestNode, ParamConfig.class);
            if (null == param) return okCustomJson(CODE40001, "参数错误");
            if (ValidationUtil.isEmpty(param.key) || ValidationUtil.isEmpty(param.value) || ValidationUtil.isEmpty(param.note))
                return okCustomJson(CODE40001, "参数错误");
            ParamConfig config = ParamConfig.find.query().where()
                    .eq("orgId", admin.orgId)
                    .eq("key", param.key).setMaxRows(1).findOne();
            if (null != config) return okCustomJson(CODE40003, "该配置的KEY已存在");
            param.setOrgId(admin.orgId);
            param.setOrgName(admin.orgName);
            param.setUpdateTime(dateUtils.getCurrentTimeByMilliSecond());
            param.setEnable(true);
            param.save();
            businessUtils.addOperationLog(request, admin, "增加系统参数配置：" + param.toString());
            updateParamConfigCache();
            return okJSON200();
        });
    }

    private void updateParamConfigCache() {
        cacheUtils.updateParamConfigCache();
    }

    /**
     * @api {POST} /v2/s/param_config/:id/ 03更新配置value值
     * @apiName getParamConfig
     * @apiGroup SHOP-PARAM-CONFIG
     * @apiSuccess (Success 200){int} id 配置id
     * @apiSuccess (Success 200){String} value 值
     * @apiSuccess (Success 40001) {int} code 40001 参数错误
     * @apiSuccess (Success 40002) {int} code 40002 配置不存在
     * @apiSuccess (Success 40003) {int} code 40003 该配置的KEY已存在
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> updateParamConfig(Http.Request request, long configId) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((admin) -> {
            if (null == admin || admin.orgId < 1) return unauth503(request);
            if (null == jsonNode) return okCustomJson(request, CODE40001, "base.argument.error");
            String value = jsonNode.findPath("value").asText();
            ParamConfig param = Json.fromJson(jsonNode, ParamConfig.class);
            if (null == param) return okCustomJson(request, CODE40001, "base.argument.error");
            ParamConfig paramConfig = ParamConfig.find.byId(configId);
            if (null == paramConfig || paramConfig.orgId != admin.orgId)
                return okCustomJson(request, CODE40001, "paramConfig.not.exist");
            if (!ValidationUtil.isEmpty(param.key)) {
                ParamConfig existConfig = ParamConfig.find.query().where()
                        .eq("orgId", admin.orgId)
                        .eq("key", param.key)
                        .ne("id", configId)
                        .setMaxRows(1).findOne();
                if (null != existConfig) return okCustomJson(request, CODE40001, "paramConfig.key.exist");
                paramConfig.setKey(param.key);
            }
            if (!ValidationUtil.isEmpty(param.value)) paramConfig.setValue(param.value);
            if (jsonNode.has("contentType")) paramConfig.setContentType(param.contentType);
            if (!ValidationUtil.isEmpty(param.note)) paramConfig.setNote(param.note);
            paramConfig.setUpdateTime(dateUtils.getCurrentTimeByMilliSecond());
            paramConfig.setValue(value);
            paramConfig.save();
            updateParamConfigCache();
            return okJSON200();
        });
    }
}
