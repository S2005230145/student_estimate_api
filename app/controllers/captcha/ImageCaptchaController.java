package controllers.captcha;

import cloud.tianai.captcha.generator.common.model.dto.ImageCaptchaInfo;
import cloud.tianai.captcha.validator.ImageCaptchaValidator;
import cloud.tianai.captcha.validator.common.model.dto.ImageCaptchaTrack;
import cloud.tianai.captcha.validator.impl.BasicCaptchaTrackValidator;
import cloud.tianai.captcha.validator.impl.SimpleImageCaptchaValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.BaseController;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import utils.CaptchaUtil;
import utils.IdGenerator;
import utils.ValidationUtil;

import javax.inject.Inject;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * @author chenjp
 * @version 1.0
 * @date 31/12/22 10.13
 */
public class ImageCaptchaController extends BaseController {
    @Inject
    CaptchaUtil captchaUtil;
    /*
                  生成滑块验证码图片, 可选项
                  SLIDER (滑块验证码)
                  ROTATE (旋转验证码)
                  CONCAT (滑动还原验证码)
                  WORD_IMAGE_CLICK (文字点选验证码)

                  更多验证码支持 详见 cloud.tianai.captcha.common.constant.CaptchaTypeConstant
           */

    /**
     * @api {GET} /v2/s/captcha/gen/noauth/ 01获取验证码图片
     * @apiName getCaptcha
     * @apiGroup Captcha
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){long} id 本次验证ID，此ID做为下一步请求验证的提交数据
     * @apiSuccess (Success 200){Object} data 对象，包含如下字段
     * @apiSuccess (Success 200){string} backgroundImage 背景图片,base64图片格式
     * @apiSuccess (Success 200){int} backgroundImageHeight 背景图片高度
     * @apiSuccess (Success 200){int} backgroundImageWidth 背景图片宽度
     * @apiSuccess (Success 200){string} sliderImage 滑块图
     * @apiSuccess (Success 200){int} sliderImageHeight 滑块图高度
     * @apiSuccess (Success 200){int} sliderImageWidth 滑块图宽度
     */
    public CompletionStage<Result> getCaptcha(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
            ImageCaptchaInfo imageCaptchaInfo = captchaUtil.generateCaptchaImage();
            ObjectNode node = Json.newObject();
            node.put(CODE, CODE200);
            String id = IdGenerator.createUUid();
            node.put("id", id);
            ObjectNode captcha = Json.newObject();
            captcha.put("backgroundImage", imageCaptchaInfo.getBackgroundImage());
            captcha.put("backgroundImageHeight", imageCaptchaInfo.getBgImageHeight());
            captcha.put("backgroundImageWidth", imageCaptchaInfo.getBgImageWidth());
            captcha.put("sliderImage", imageCaptchaInfo.getSliderImage());
            captcha.put("sliderImageHeight", imageCaptchaInfo.getSliderImageHeight());
            captcha.put("sliderImageWidth", imageCaptchaInfo.getSliderImageWidth());
            captcha.set("data", Json.newObject());
            node.set("captcha", captcha);
            ImageCaptchaValidator imageCaptchaValidator = new BasicCaptchaTrackValidator();
            // 这个map数据应该存到缓存中，校验的时候需要用到该数据
            Map<String, Object> map = imageCaptchaValidator.generateImageCaptchaValidData(imageCaptchaInfo);
            redis.set(id + ":check", map, 5 * 60);
            return ok(node);
        });
    }

    /**
     * @api {POST} /v2/s/captcha/check/noauth/ 02请求验证
     * @apiName validate
     * @apiGroup Captcha
     * @apiParam {long} id 验证ID
     * @apiParam {Object} data 对象，用base64位进行加密后形成字符串提交,包含如下字段，从示意中js 复制出来的，供参考.他们网站上是用FORM提交 ，我们改成JSON提交
     * bgImageWidth: bgImgWidth,
     * bgImageHeight: $(".content").height(),
     * sliderImageWidth: -1,
     * sliderImageHeight: -1,
     * startSlidingTime: startSlidingTime,
     * endSlidingTime: entSlidingTime,
     * trackList: trackArr
     * @apiSuccess (Success 200){int} code 200
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> validate(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode requestNode = request.body().asJson();
            if (null == requestNode) return okCustomJson(request,CODE40001, "base.json.error");
            String id = requestNode.findPath("id").asText();
            if (ValidationUtil.isEmpty(id)) return okCustomJson(request,CODE40001, "captcha.id.error");
            String data = requestNode.findPath("data").asText();
            if (ValidationUtil.isEmpty(data)) return okCustomJson(request,CODE40001, "captcha.data.error");
            JsonNode dataNode = Json.parse(Base64.getDecoder().decode(data));
            ImageCaptchaTrack imageCaptchaTrack = Json.fromJson(dataNode, ImageCaptchaTrack.class);
            if (null == imageCaptchaTrack) return okCustomJson(request,CODE40001, "captcha.data.format.error");
            ImageCaptchaValidator imageCaptchaValidator = new SimpleImageCaptchaValidator();
            Optional<Map<String, Object>> optional = redis.sync().get(id + ":check");
            if (!optional.isPresent()) return okCustomJson(request,CODE40001, "captcha.data.check.error");
            Map<String, Object> map = optional.get();
            if (null == map) return okCustomJson(request,CODE40001, "captcha.data.check.error");
            // 用户传来的行为轨迹和进行校验
            // - imageCaptchaTrack为前端传来的滑动轨迹数据
            // - map 为生成验证码时缓存的map数据
            boolean check = imageCaptchaValidator.valid(imageCaptchaTrack, map);
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.put("check", check);
            redis.set(id, check, 5 * 60);
//            // 如果只想校验用户是否滑到指定凹槽即可，也可以使用
//            // - 参数1 用户传来的百分比数据
//            // - 参数2 生成滑块是真实的百分比数据
//            Float percentage = null;
//            check = imageCaptchaValidator.checkPercentage(0.2f, percentage);
            return ok(result);
        });
    }


}
