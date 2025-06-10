package controllers.basic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import controllers.BaseSecurityController;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.log.Suggestion;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import utils.BizUtils;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * 建议管理
 */
public class SuggestionManager extends BaseSecurityController {
    @Inject
    BizUtils bizUtils;
    Logger.ALogger logger = Logger.of(SuggestionManager.class);

    /**
     * @api {GET} /v2/s/suggestion/?page=&status= 01建议列表
     * @apiName listSuggestion
     * @apiGroup SUGGESTION
     * @apiParam {int} status 1未处理 2需要跟进 3已处理
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){int} pages 分页
     * @apiSuccess (Success 200){JsonArray} list 列表
     * @apiSuccess (Success 200){int} status 1未联系 2已联系 3需要进一步联系 4已加盟
     * @apiSuccess (Success 200){string} name 用户名字
     * @apiSuccess (Success 200){string} content 内容
     * @apiSuccess (Success 200){string} createTime 提交时间
     */
    public CompletionStage<Result> listSuggestion(Http.Request request, int page, int status) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((shopAdmin) -> {
            if (null == shopAdmin || shopAdmin.orgId < 1) return unauth503(request);
            ExpressionList<Suggestion> expressionList = Suggestion.find.query().where().eq("orgId", shopAdmin.orgId);
            if (status > 0) expressionList.eq("status", status);
            PagedList<Suggestion> pagedList = expressionList.orderBy().desc("id")
                    .setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_20)
                    .setMaxRows(BusinessConstant.PAGE_SIZE_20)
                    .findPagedList();
            List<Suggestion> list = pagedList.getList();
            int pages = pagedList.getTotalPageCount();
            ObjectNode node = Json.newObject();
            node.put(CODE, CODE200);
            node.put("pages", pages);
            node.set("list", Json.toJson(list));
            return ok(node);
        });
    }


    /**
     * @api {POST} /v2/s/suggestion/ 02修改建议状态
     * @apiName setSuggestionStatus
     * @apiGroup SUGGESTION
     * @apiParam {long} id  记录ID
     * @apiParam {int} status 状态
     * @apiParam {String} note 备注
     * @apiSuccess (Success 200){int} code 200
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> setSuggestionStatus(Http.Request request) {
        JsonNode requestNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((shopAdmin) -> {
            if (null == shopAdmin || shopAdmin.orgId < 1) return unauth503(request);
            if (null == request) return okCustomJson(request,CODE40001, "base.argument.error");
            long id = requestNode.findPath("id").asLong();
            int status = requestNode.findPath("status").asInt();
            String note = requestNode.findPath("note").asText();
            Suggestion log = Suggestion.find.byId(id);
            if (null == log) return okCustomJson(request,CODE40001, "base.record.not.exist");
            log.setStatus(status);
            log.setNote(note);
            log.save();
            String prefixTrackSuggestionState = bizUtils.getMessageValue(request, "prefix.track.suggestion.state");
            businessUtils.addOperationLog(request, shopAdmin, prefixTrackSuggestionState + log.toString());
            return okJSON200();
        });
    }


}
