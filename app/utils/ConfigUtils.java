package utils;

import models.system.AdminConfig;
import models.system.ParamConfig;
import play.cache.NamedCache;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

import static constants.BusinessConstant.*;
import static constants.RedisKeyConstant.STABLE_ACCESS_TOKEN;

@Singleton
public class ConfigUtils {
    @Inject
    @NamedCache("redis")
    protected play.cache.redis.AsyncCacheApi redis;
    @Inject
    EncodeUtils encodeUtils;

    public String getShopConfigValue(String key, long orgId) {
        String value = "";
        String finalKey = key + ":" + orgId;
        Optional<Object> accountOptional = redis.sync().get(finalKey);
        if (accountOptional.isPresent()) {
            value = (String) accountOptional.get();
            if (!ValidationUtil.isEmpty(value)) return value;
        }
        if (ValidationUtil.isEmpty(value)) {
            ParamConfig config = ParamConfig.find.query().where()
                    .eq("key", key)
                    .eq("orgId", orgId)
                    .orderBy().asc("id")
                    .setMaxRows(1).findOne();
            if (null != config && !ValidationUtil.isEmpty(config.value)) {
                if (config.isEncrypt) {
                    value = encodeUtils.decrypt(config.value);
                } else value = config.value;
                redis.set(finalKey, value, 10);
            }
        }
        return value;
    }


    public String getPlatformConfigValue(String key) {
        String value = "";
        Optional<Object> accountOptional = redis.sync().get(key);
        if (accountOptional.isPresent()) {
            value = (String) accountOptional.get();
            if (!ValidationUtil.isEmpty(value)) return value;
        }
        if (ValidationUtil.isEmpty(value)) {
            AdminConfig config = AdminConfig.find.query().where()
                    .eq("key", key)
                    .orderBy().asc("id")
                    .setMaxRows(1).findOne();
            if (null != config && !ValidationUtil.isEmpty(config.value)) {
                if (config.isEncrypt) {
                    value = encodeUtils.decrypt(config.value);
                } else value = config.value;
                redis.set(key, value);
            }
        }
        return value;
    }


    public String getDomain() {
        return getPlatformConfigValue(PARAM_KEY_DEFAULT_HOME_PAGE_URL);
    }

    public String getWechatMpAppId(long orgId) {
        return getShopConfigValue(PARAM_KEY_WECHAT_MP_APP_ID, orgId);
    }

    public String getWechatMpSecretCode(long orgId) {
        return getShopConfigValue(PARAM_KEY_WECHAT_MP_SECRET_CODE, orgId);
    }

    public String getWechatMiniAppId(long orgId) {
        return getShopConfigValue(PARAM_KEY_WECHAT_MINI_APP_ID, orgId);
    }

    public String getWechatMiniAppSecretCode() {
        return getPlatformConfigValue(PARAM_KEY_WECHAT_MINI_APP_SECRET_CODE);
    }


    public String getWechatMchId() {
        return getPlatformConfigValue(PARAM_KEY_WECHAT_MCH_ID);
    }

    public String getWechatMchAppSecretCode() {
        return getPlatformConfigValue(PARAM_KEY_WECHAT_MCH_API_SECURITY_CODE);
    }


    public String getWepaySpAppId() {
        return getPlatformConfigValue(PARAM_KEY_WE_PAY_SP_APP_ID);
    }

    public String getWepaySpMchId() {
        return getPlatformConfigValue(PARAM_KEY_WE_PAY_SP_MCH_ID);
    }

    public String getWepaySubMchId() {
        return getPlatformConfigValue(PARAM_KEY_WE_PAY_SP_MCH_ID);
    }

    public String getWepaySubKeySerialNo() {
        return getPlatformConfigValue(PARAM_KEY_WE_PAY_KEY_SERIAL_NO);
    }

    public String getWepayAPIV3Key() {
        return getPlatformConfigValue(PARAM_KEY_WE_PAY_API_V3_KEY);
    }

    public String getWepayPrivateKey() {
        return getPlatformConfigValue(PARAM_KEY_WE_PAY_PRIVATE_KEY);
    }

    public String getCOSSecretId() {
        return getPlatformConfigValue(PARAM_KEY_COS_SECRET_ID);
    }

    public String getCOSSecretKey() {
        return getPlatformConfigValue(PARAM_KEY_COS_SECRET_KEY);
    }


    public String getAlinYunAccessId() {
        return getPlatformConfigValue(PARAM_KEY_ALI_YUN_ACCESS_ID);
    }

    public String getAliYunSecretKey() {
        return getPlatformConfigValue(PARAM_KEY_ALI_YUN_SECRET_KEY);
    }

    public String getFeiePrinterkey() {
        return getPlatformConfigValue(PARAM_KEY_FEIE_PRINTER_KEY);
    }
    public String getAccessToken(long orgId) {
        Optional<String> optional = redis.sync().get(STABLE_ACCESS_TOKEN + orgId);
        if (optional.isPresent()) return optional.get();
        return "";
    }

}
