package utils;

import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import models.admin.ShopAdmin;
import models.system.AdminConfig;
import models.user.Member;
import models.user.MemberBalance;
import play.Logger;
import play.cache.NamedCache;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.mvc.Http;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static constants.BusinessConstant.*;
import static constants.RedisKeyConstant.PARAM_KEY_GROUPON_REQUIRE_ORDERS;

@Singleton
public class BizUtils {

    Logger.ALogger logger = Logger.of(BizUtils.class);
    public static final int TOKEN_EXPIRE_TIME = 2592000;

    @Inject
    CacheUtils cacheUtils;

    @Inject
    EncodeUtils encodeUtils;

    @Inject
    IPUtil ipUtil;
 

    @Inject
    @NamedCache("redis")
    protected play.cache.AsyncCacheApi redis;
    @Inject
    ConfigUtils configUtils;

    @Inject
    protected MessagesApi messagesApi;

    public static DecimalFormat DF = new DecimalFormat("0.0");

    public String getAuthTokenFromRequest(Http.Request request) {
        Optional<String> authTokenHeaderValues = request.getHeaders().get(KEY_AUTH_TOKEN);
        if (authTokenHeaderValues.isPresent()) {
            return authTokenHeaderValues.get();
        }
        return "";
    }

    public ShopAdmin getUserIdByAuthToken2(Http.Request request) {
        String authToken = getUIDFromRequest(request);
        if (ValidationUtil.isEmpty(authToken)) return null;
        Optional<ShopAdmin> optional = redis.sync().get(authToken);
        if (optional.isPresent()) {
            ShopAdmin member = optional.get();
            return member;
        }
        return null;
    }

    public CompletionStage<ShopAdmin> getUserIdByAuthToken(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> getUserIdByAuthToken2(request));
    }


    public String getUIDFromRequest(Http.Request request) {
        Optional<String> authTokenHeaderValues = request.getHeaders().get(KEY_AUTH_TOKEN_UID);
        if (authTokenHeaderValues.isPresent()) {
            String authToken = authTokenHeaderValues.get();
            return authToken;
        }
        return "";
    }

    public String getRequestIP(Http.Request request) {
        String ip = null;
        try {
            String remoteAddr = request.remoteAddress();
            String forwarded = request.getHeaders().get("X-Forwarded-For").get();
            String realIp = request.getHeaders().get(BusinessConstant.X_REAL_IP_HEADER).get();
            if (forwarded != null) {
                ip = forwarded.split(",")[0];
            }
            if (ValidationUtil.isEmpty(ip)) {
                ip = realIp;
            }
            if (ValidationUtil.isEmpty(ip)) {
                ip = remoteAddr;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return ip == null ? "" : escapeHtml(ip);
    }

    public boolean checkVcode(String vcode) {
        Optional<Boolean> optional = redis.sync().get(vcode);
        if (optional.isPresent()) {
            boolean result = optional.get();
            return result;
        }
        return false;
    }

    public boolean checkVcode(String accountName, String vcode) {
        if (ValidationUtil.isPhoneNumber(accountName)) {
            String key = cacheUtils.getSMSLastVerifyCodeKey(accountName);
            Optional<String> optional = redis.sync().get(key);
            if (optional.isPresent()) {
                String correctVcode = optional.get();
                if (!ValidationUtil.isEmpty(correctVcode)) {
                    if (ValidationUtil.isVcodeCorrect(vcode) && ValidationUtil.isVcodeCorrect(correctVcode) && vcode.equals(correctVcode))
                        return true;
                }
            }
        } else return false;
        return false;
    }


    /**
     * 转义html脚本
     *
     * @param value
     * @return
     */
    public String escapeHtml(String value) {
        if (ValidationUtil.isEmpty(value)) return "";
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("\\(", "（").replaceAll("\\)", "）");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("script", "");
        value = value.replaceAll("select", "");
        value = value.replaceAll("insert", "");
        value = value.replaceAll("update", "");
        value = value.replaceAll("delete", "");
        value = value.replaceAll("%", "\\%");
        value = value.replaceAll("union", "");
        value = value.replaceAll("load_file", "");
        value = value.replaceAll("outfile", "");
        return value;
    }

    public boolean setLock(String id, String operationType) {
        String key = operationType + ":" + id;
        try {
            Optional<String> optional = redis.sync().get(key);
            if (optional.isPresent()) return false;
            redis.sync().set(key, key, 5);
            return true;
        } catch (Exception e) {
            logger.error("getLock:" + e.getMessage());
            redis.remove(key);
        }
        return true;
    }

    /**
     * 解锁
     *
     * @param uid
     * @param operationType
     */
    public void unLock(String uid, String operationType) {
        redis.remove(operationType + ":" + uid);
    }
 

    public String getUserName(Member member) {
        String userName = "";
        if (null != member) {
            userName = member.realName;
            if (ValidationUtil.isEmpty(userName)) userName = member.nickName;
        }
        return userName;
    }


    public void push(ObjectNode node) {

    }
    public int getGrouponRequireOrders() {
        String value = configUtils.getPlatformConfigValue(PARAM_KEY_GROUPON_REQUIRE_ORDERS);
        if (!ValidationUtil.isEmpty(value)) {
            return Integer.parseInt(value);
        }
        return GROUPON_REQUIRE_ORDERS;
    }

    public boolean checkBalanceEnough(long uid, long totalAmount) {
        MemberBalance cashBalance = MemberBalance.find.query().where().eq("uid", uid)
                .eq("itemId", BusinessItem.CASH).setMaxRows(1).findOne();
        if (null != cashBalance) {
            if (cashBalance.leftBalance >= totalAmount) return true;
        }
        return false;
    }


    public String limit20(String value) {
        if (ValidationUtil.isEmpty(value)) return "";
        if (value.length() > 20) return value.substring(0, 17) + "...";
        return value;
    }

    public String limit10(String value) {
        if (ValidationUtil.isEmpty(value)) return "";
        if (value.length() > 10) return value.substring(0, 7) + "...";
        return value;
    }
 


    public static BufferedImage desaturate(BufferedImage source) {
        ColorConvertOp colorConvert =
                new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        colorConvert.filter(source, source);
        return source;
    }


    public String convertScene(ObjectNode node) {
        String temp = Json.stringify(node);
        return temp.replaceAll("\\{", "(")
                .replaceAll("\\}", ")")
                .replaceAll("\"", "'");
    }


    public void setSkuStockCache(long skuId, long stock) {
//        String key = cacheUtils.getSkuStockCache(skuId);
//        asyncCacheApi.set(key, stock, 24 * 3600);
    }


    public String getMemberName(ShopAdmin member) {
        String name = "";
        if (null != member) {
            name = member.realName;
            if (ValidationUtil.isEmpty(name)) name = member.realName;
        }
        return name;
    }

    public String getMemberName(Member member) {
        String name = "";
        if (null != member) {
            name = member.phoneNumber;
            if (ValidationUtil.isEmpty(name)) name = member.realName;
            if (ValidationUtil.isEmpty(name)) name = member.nickName;
        }
        return name;
    }

    public String getDomain() {
        return configUtils.getPlatformConfigValue(PARAM_KEY_DEFAULT_HOME_PAGE_URL);
    }

    public void deleteVcodeCache(String accountName) {
        String key = cacheUtils.getSMSLastVerifyCodeKey(accountName);
        if (!ValidationUtil.isEmpty(key)) redis.remove(key);
    }
 
    public void addOperationLog(Http.Request request, ShopAdmin admin, String note) {
        String ip = getRequestIP(request);
        String place = "";
        if (!ValidationUtil.isEmpty(ip)) place = ipUtil.getCityByIp(ip);
        request.getHeaders()
                .adding("adminId", admin.id + "")
                .adding("adminName", admin.realName)
                .adding("ip", ip)
                .adding("place", place)
                .adding("note", note);
    }

    public boolean uptoErrorLimit(Http.Request request, String key, int max) {
        Optional<Integer> accessCountOptional = redis.sync().get(key);
        int accessCount = 0;
        if (accessCountOptional.isPresent()) {
            accessCount = accessCountOptional.get();
        }
        if (accessCount <= 0) {
            redis.set(key, 1, BusinessConstant.KEY_EXPIRE_TIME_2M);
        } else {
            int accessCountInt = accessCount + 1;
            if (accessCountInt > max) return true;
            redis.set(key, accessCountInt, BusinessConstant.KEY_EXPIRE_TIME_2M);
        }
        return false;
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
 
 
    public long calcGiveAmount(long charge) {
        long fiveMulti = charge / 50000;
        long twoMulti = (charge - fiveMulti * 50000) / 20000;
        long give = fiveMulti * 10000 + twoMulti * 2000;
        return give;
    }

    public String getWepaySubKeySerialNo(long orgId) {
        return getPlatformConfigValue(PARAM_KEY_WE_PAY_KEY_SERIAL_NO);
    }
 
    public boolean upToIPLimit(Http.Request request, String key, int max) {
        String ip = getRequestIP(request);
        if (!ValidationUtil.isEmpty(ip)) {
            String accessCount = redis.sync().getOrElseUpdate(key + ip, () -> "");
            if (ValidationUtil.isEmpty(accessCount)) {
                redis.set(key + ip, "1", BusinessConstant.KEY_EXPIRE_TIME_2M);
            } else {
                int accessCountInt = Integer.parseInt(accessCount) + 1;
                if (accessCountInt > max) return true;
                redis.set(key + ip, String.valueOf(accessCountInt), BusinessConstant.KEY_EXPIRE_TIME_2M);
            }
        }
        return false;
    }

 
    public long getOrgIdFromHeader(Http.Request request) {
        Optional<String> orgIdOptional = request.getHeaders().get("orgId");
        if (orgIdOptional.isPresent()) {
            String orgIdStr = orgIdOptional.get();
            if (!ValidationUtil.isEmpty(orgIdStr)) {
                long orgId = Long.parseLong(orgIdStr);
                return orgId;
            }
        }
        return 0;
    }
 

    public String getMessageValue(Http.Request request, String key) {
        Messages messages = messagesApi.preferred(request);
        String translation = messages.at(key);
        return translation != null ? translation : "";
    }
    
    public String getAlinYunAccessId() {
        return configUtils.getPlatformConfigValue(PARAM_KEY_ALI_YUN_ACCESS_ID);
    }
    
    public String getAliYunSecretKey() {
        return configUtils.getPlatformConfigValue(PARAM_KEY_ALI_YUN_SECRET_KEY);
    }
    
    
}
