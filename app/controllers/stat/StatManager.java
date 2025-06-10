package controllers.stat;

import controllers.BaseSecurityController;
import play.mvc.Http;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;


/**
 * 统计管理
 */
public class StatManager extends BaseSecurityController {
 


    /**
     * @api {GET} /v2/s/home_page_stat/ 01首页统计
     * @apiName homepageStat
     * @apiGroup SHOP-STAT
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {Object} todayOverview todayOverview
     * @apiSuccess (Success 200) {long} todayRegCount 今日注册人数
     * @apiSuccess (Success 200) {long} todayOrders 今日订单数
     * @apiSuccess (Success 200) {long} todayTotalMoney 今日订单总额
     * @apiSuccess (Success 200) {long} todayRealPay 今日订单实付
     * @apiSuccess (Success 200) {long} todayDiscount 今日订单优惠金额
     * @apiSuccess (Success 200) {long} todayTotalRefund 今日订单退款金额
     * @apiSuccess (Success 200) {double} todayTotalDegree 今日充电总度数
     * @apiSuccess (Success 200) {long} todayDuration 今日充电总时长
     * @apiSuccess (Success 200) {long} todayTotalElectricFee 今日充电电费总额
     * @apiSuccess (Success 200) {long} todayTotalServiceFee 今日充电服务费总额
     * @apiSuccess (Success 200) {long} todayTotalOccupyFee 今日充电占桩费总额
     * @apiSuccess (Success 200) {long} todayDeposit 今日充值总额
     * @apiSuccess (Success 200) {long} monthRegCount 本月注册人数
     * @apiSuccess (Success 200) {long} monthOrders 本月订单数
     * @apiSuccess (Success 200) {long} monthTotalMoney 本月订单总额
     * @apiSuccess (Success 200) {long} monthRealPay 本月订单实付
     * @apiSuccess (Success 200) {long} monthDiscount 本月订单优惠金额
     * @apiSuccess (Success 200) {long} monthTotalRefund 本月订单退款金额
     * @apiSuccess (Success 200) {double} monthTotalDegree 本月充电总度数
     * @apiSuccess (Success 200) {long} monthDuration 本月充电总时长
     * @apiSuccess (Success 200) {long} monthTotalElectricFee 本月充电电费总额
     * @apiSuccess (Success 200) {long} monthTotalServiceFee 本月充电服务费总额
     * @apiSuccess (Success 200) {long} monthTotalOccupyFee 本月充电占桩费总额
     * @apiSuccess (Success 200) {long} monthDeposit 本月充值总额
     * @apiSuccess (Success 200) {JsonArray} list 最新30条统计数据
     */
    public CompletionStage<Result> homepageStat(Http.Request request) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((member) -> {
            if (null == member || member.orgId < 1) return unauth503(request);
            return okJSON200();
        });
    }
}
