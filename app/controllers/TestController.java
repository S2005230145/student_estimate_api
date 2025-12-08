package controllers;

import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public class TestController extends BaseSecurityController{

    /**
     * @api {GET} /v2/s/test/   测试接口
     * @apiName test
     * @apiGroup A
     * @apiSuccess (Success 200) {String} msg 测试成功
     */
    public Result test(){
        return ok("接口测试成功");
    }
}
