package utils;

import constants.BusinessConstant;
import constants.RedisKeyConstant;
import models.system.ParamConfig;
import play.Logger;
import play.cache.AsyncCacheApi;
import play.cache.SyncCacheApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

import static constants.RedisKeyConstant.*;
import static constants.RedisKeyConstant.GET_SHOP_TABLE_JSON_CACHE;

/**
 * cache utils
 */
@Singleton
public class CacheUtils {
    Logger.ALogger logger = Logger.of(CacheUtils.class);

    @Inject
    SyncCacheApi cache;

    @Inject
    AsyncCacheApi asyncCacheApi;

    @Inject
    EncodeUtils encodeUtils;

    /**
     * 获取用户表的cache key
     *
     * @param id
     * @return
     */
    public String getMemberKey(long id) {
        return RedisKeyConstant.KEY_MEMBER_ID + id;
    }

    public String getMemberKey(String id) {
        return RedisKeyConstant.KEY_MEMBER_ID + id;
    }

    public String getMemberKey(int deviceType, String token) {
        return RedisKeyConstant.KEY_MEMBER_ID + deviceType + ":" + token;
    }

    /**
     * 获取手机号短信限制缓存key
     *
     * @param phoneNumber
     * @return
     */
    public String getSmsPhoneNumberLimitKey(String phoneNumber) {
        return SMS_PHONENUMBER_LIMIT_PREFIX_KEY + phoneNumber;
    }


    public String getSMSLastVerifyCodeKey(String phoneNumber) {
        return BusinessConstant.KEY_LAST_VERIFICATION_CODE_PREFIX_KEY + ":"  + phoneNumber;
    }

    public String getAllLevelKeySet() {
        return ALL_LEVELS_KEY_SET;
    }

    public String getEachLevelKey(int level) {
        return LEVEL_KEY_PREFIX + level;
    }

    public String getAllScoreConfigKeySet() {
        return ALL_SCORE_CONFIGS_KEY_SET;
    }

    public String getEachScoreConfigKey(int type) {
        return SCORE_CONFIG_KEY_PREFIX + type;
    }

    public String getSoldAmountCacheKey(long merchantId) {
        return RedisKeyConstant.MERCHANTS_SOLD_AMOUNT_CACHE_KEY + merchantId;
    }

    /**
     * 商品分类列表集合
     *
     * @return
     */
    public String getCategoryPrefix(long categoryId) {
        return RedisKeyConstant.MERCHANTS_CATEGORIES_EACH_PREFIX + categoryId;
    }

    public String getWineCategoryPrefix(long categoryId) {
        return RedisKeyConstant.WINE_CATEGORIES_EACH_PREFIX + categoryId;
    }

    /**
     * 兑换分类缓存列表
     *
     * @return
     */
    public String getCategoryEachListKey(String filter) {
        return RedisKeyConstant.MERCHANTS_CATEGORIES_LIST_CACHE_KEY_PREFIX + filter;
    }

    /**
     * 品牌集合
     *
     * @return
     */
    public String getBrandKeySet() {
        return RedisKeyConstant.BRAND_KEY_SET;
    }

    /**
     * 单个品牌的KEY
     *
     * @param brandId
     * @return
     */
    public String getBrandKey(long brandId) {
        return RedisKeyConstant.BRAND_EACH_KEY + brandId;
    }

    /**
     * 首页商品图缓存
     *
     * @return
     */
    public String homepageBrandJsonCache() {
        return HOMEPAGE_BRAND_JSON_CACHE;
    }

    public String homepageOnArrivalJsonCache() {
        return ON_NEW_ARRIVAL_JSON_CACHE;
    }

    public String homepageOnPromotionJsonCache() {
        return ON_PROMOTION_JSON_CACHE;
    }

    public String getMailFeeCacheKeyList() {
        return RedisKeyConstant.MAIL_FEE_CACHE_KEYSET;
    }

    public String getMailFeeKey(String regionCode) {
        return RedisKeyConstant.MAIL_FEE_CACHE_KEY_PREFIX + regionCode;
    }


    public String getCouponConfigCacheKeyset() {
        return RedisKeyConstant.COUPON_CONFIG_KEYSET;
    }

    public String getCouponConfigCacheKey(long id) {
        return RedisKeyConstant.COUPON_CONFIG_KEY_PREFIX + id;
    }

    public String getParamConfigCacheKeyset() {
        return RedisKeyConstant.PARAM_CONFIG_KEYSET;
    }

//    public String getParamConfigCacheKey() {
//        return RedisKeyConstant.PARAM_CONFIG_KEY_PREFIX;
//    }

    public String getMerchantCacheKey(long id) {
        return RedisKeyConstant.MERCHANT_CACHE_KEY_PREFIX + id;
    }

    public String getProductJsonCacheKey(long id) {
        return RedisKeyConstant.MERCHANT_JSON_CACHE_KEY_PREFIX + id;
    }



    public String getMemberTokenKey(long orgId, long uid) {
        return RedisKeyConstant.KEY_SHOP_MEMBER_TOKEN_KEY + ":" + orgId + ":" + uid;
    }



    /**
     * 获取组缓存的key
     *
     * @param groupId
     * @return
     */
    public String getGroupActionKey(int groupId) {
        return ADMIN_GROUP_ACTION_KEYSET_PREFIX + groupId;
    }
  
    public String getShopCategoryJsonCache(long shopId) {
        return RedisKeyConstant.SHOP_PRODUCT_CATEGORIES_LIST_CACHE_KEY_PREFIX;
    }

    public String getActivityCategoryJsonCache(long shopId) {
        return RedisKeyConstant.ACTIVITY_CATEGORIES_LIST_CACHE_KEY_PREFIX + shopId;
    }

    public void updateParamConfigCache() {
        logger.info("updateParamConfigCache");
        List<ParamConfig> list = ParamConfig.find.query().where().orderBy().desc("id").findList();
        list.forEach((config) -> {
            if (config.isEncrypt) {
                asyncCacheApi.set(config.key, encodeUtils.decrypt(config.value));
            } else asyncCacheApi.set(config.key, config.value);
        });
    }
    public void deleteArticleCache() {
        String key = getCarouselJsonCache();
        asyncCacheApi.remove(key);
        asyncCacheApi.remove(RedisKeyConstant.KEY_HOME_PAGE_BOTTOM_INFO_LINKS);
        asyncCacheApi.remove(RedisKeyConstant.KEY_HOME_PAGE_INFO_LINKS);
        asyncCacheApi.remove("KEY_CAROUSEL_PREFIX:0");
        asyncCacheApi.remove("KEY_CAROUSEL_PREFIX:1");
        asyncCacheApi.remove("KEY_CAROUSEL_PREFIX:2");
        asyncCacheApi.remove("KEY_CAROUSEL_PREFIX:3");
        asyncCacheApi.remove("KEY_CAROUSEL_PREFIX:4");
        asyncCacheApi.remove("KEY_CAROUSEL_PREFIX:5");
    }

    public String getMixOptionCacheSet() {
        return RedisKeyConstant.MIX_OPTION_KEY_SET;
    }

    public String getProductClassifyCacheSet() {
        return RedisKeyConstant.PRODUCT_CLASSIFY_KEY_SET;
    }

    public String getClassifyJsonCache(String classifyCode) {
        return RedisKeyConstant.PRODUCT_CLASSIFY_JSON_CACHE + classifyCode;
    }

    public String getClassifyJsonCache(long classifyId, int page) {
        return RedisKeyConstant.PRODUCT_CLASSIFY_BY_ID_JSON_CACHE + classifyId + ":" + page;
    }

    public String getClassifyListJsonCache() {
        return RedisKeyConstant.PRODUCT_CLASSIFY_LIST_JSON_CACHE;
    }

    public String getArticleCategoryKey(String cateName) {
        return RedisKeyConstant.ARTICLE_CATEGORY_KEY_PREFIX + cateName;
    }

    public String getArticleCategoryKey(long categoryId) {
        return RedisKeyConstant.ARTICLE_CATEGORY_KEY_BY_ID_PREFIX + categoryId;
    }

    public String getProductsJsonCache() {
        return PRODUCT_JSON_CACHE;
    }

    public String getProductsByTagJsonCache(String tag) {
        return PRODUCT_BY_TAG_JSON_CACHE + tag;
    }

    public String getShopsByTagJsonCache(String tag) {
        return SHOPS_BY_TAG_JSON_CACHE + tag;
    }

    public String getSpecialTopicJsonCache() {
        return SPECIAL_TOPIC_JSON_CACHE;
    }

    public String getBrandJsonCache() {
        return BRAND_JSON_CACHE;
    }

    public String getFlashsaleJsonCache() {
        return FLASH_SALE_JSON_CACHE;
    }

    public String getFlashsaleCacheByProductId(long productId) {
        return FLASH_SALE_CACHE_BY_PRODUCT_ID + productId;
    }



    public String getProductTabJsonCache(long orgId) {
        return PRODUCT_TAB_JSON_CACHE + orgId;
    }

    public String getFavorCache(long favorId) {
        return PRODUCT_FAVOR_CACHE + favorId;
    }

    public String defaultRecommendProductList() {
        return DEFAULT_RECOMMEND_PRODUCT;
    }

    public String getRecommendProductList(long productId) {
        return RECOMMEND_PRODUCT_KEY + productId;
    }

    public String getRelateProductList(long productId) {
        return RELATE_PRODUCT_KEY + productId;
    }

    public String getWineKey(long id) {
        return RedisKeyConstant.KEY_WINE_ID + id;
    }

    public String getProductKeywordsJsonCache() {
        return PRODUCT_SEARCH_KEYWORDS_JSON_CACHE;
    }

    public String getSearchKeywordsJsonCache() {
        return SEARCH_KEYWORDS_JSON_CACHE;
    }

    public String getMemberPlatformTokenKey(long uid) {
        return RedisKeyConstant.KEY_MEMBER_PLATFORM_TOKEN_KEY + uid;
    }

    public String getPrefetchJsonCache() {
        return PREFETCH_JSON_CACHE;
    }

    public String getCarouselJsonCache() {
        return RedisKeyConstant.CAROUSEL_JSON_CACHE;
    }

    public String getFlashsaleTodayJsonCache(int page) {
        return FLASH_SALE_TODAY_JSON_CACHE + page;
    }

    public String getShopListJsonCache(int page) {
        return SHOP_LIST_JSON_CACHE + page;
    }

    public String getTopShopListJsonCache() {
        return TOP_SHOP_LIST_JSON_CACHE;
    }

    public String getMembershipJsonCache() {
        return RedisKeyConstant.KEY_MEMBER_SHIP_JSON_CACHE_KEY;
    }

    public String getProductTabDetail(long tabId) {
        return RedisKeyConstant.PRODUCT_TAB_DETAIL_JSON_CACHE + tabId;
    }

    public String getGrouponProductsJsonCache(int page) {
        return GROUPON_PRODUCT_JSON_CACHE + page;
    }

    public String getParamConfigCacheKey() {
        return RedisKeyConstant.PARAM_CONFIG_KEY_PREFIX;
    }

    public String getShopConfigCacheKey() {
        return RedisKeyConstant.PARAM_SHOP_CONFIG_KEY_PREFIX;
    }

    public String getProductCacheKey(long id) {
        return RedisKeyConstant.PRODUCT_CACHE_KEY + id;
    }


    public String getPosProductJsonCache(long orgId) {
        return POS_PRODUCT_JSON_CACHE + ":" + orgId;
    }

    public String getPosMemberLevelCache() {
        return POS_MEMBER_LEVEL_JSON_CACHE;
    }

    public String getShopTableCache(long shopId) {
        return GET_SHOP_TABLE_JSON_CACHE + shopId;
    }
}
