package utils;

import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import cloud.tianai.captcha.generator.ImageCaptchaGenerator;
import cloud.tianai.captcha.generator.ImageTransform;
import cloud.tianai.captcha.generator.common.model.dto.ImageCaptchaInfo;
import cloud.tianai.captcha.generator.impl.MultiImageCaptchaGenerator;
import cloud.tianai.captcha.generator.impl.transform.Base64ImageTransform;
import cloud.tianai.captcha.resource.ImageCaptchaResourceManager;
import cloud.tianai.captcha.resource.impl.DefaultImageCaptchaResourceManager;

import javax.inject.Singleton;

@Singleton
public class CaptchaUtil {
    ImageCaptchaResourceManager imageCaptchaResourceManager = new DefaultImageCaptchaResourceManager();
    ImageTransform imageTransform = new Base64ImageTransform();
    ImageCaptchaGenerator imageCaptchaGenerator = new MultiImageCaptchaGenerator(imageCaptchaResourceManager, imageTransform).init(true);
    public ImageCaptchaInfo generateCaptchaImage() {
        ImageCaptchaInfo imageCaptchaInfo = imageCaptchaGenerator.generateCaptchaImage(CaptchaTypeConstant.WORD_IMAGE_CLICK);
        return imageCaptchaInfo;
    }
}
