/**
 * 五五海淘返利APP新版接口
 * 更新日志<br> 相对于上一build的变更： <br/>Nu 调整搜索模型 
 *
 * OpenAPI spec version: 1.8 build20180202-2
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.swagger.client.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "")
public class DealExtraModel implements Serializable {
  
  @SerializedName("deal_id")
  private String dealId = null;
  @SerializedName("title")
  private String title = null;
  @SerializedName("price_view")
  private String priceView = null;
  @SerializedName("discount_view")
  private String discountView = null;
  @SerializedName("has_rebate")
  private String hasRebate = null;
  @SerializedName("rebate_view")
  private String rebateView = null;
  @SerializedName("original_price")
  private String originalPrice = null;
  @SerializedName("now_price")
  private String nowPrice = null;
  @SerializedName("currency_symbol")
  private String currencySymbol = null;
  @SerializedName("currency_abbr")
  private String currencyAbbr = null;
  @SerializedName("deal_pic")
  private String dealPic = null;
  @SerializedName("store_id")
  private String storeId = null;
  @SerializedName("store_name")
  private String storeName = null;
  @SerializedName("store_logo")
  private String storeLogo = null;
  @SerializedName("rebate_instruction")
  private String rebateInstruction = null;
  @SerializedName("praise_count")
  private String praiseCount = null;
  @SerializedName("comment_count")
  private String commentCount = null;
  @SerializedName("collection_count")
  private String collectionCount = null;
  @SerializedName("buy_count")
  private String buyCount = null;
  @SerializedName("country_id")
  private String countryId = null;
  @SerializedName("country_name")
  private String countryName = null;
  @SerializedName("country_abbr")
  private String countryAbbr = null;
  @SerializedName("country_flag_pic")
  private String countryFlagPic = null;
  @SerializedName("is_praised")
  private String isPraised = null;
  @SerializedName("is_collected")
  private String isCollected = null;
  @SerializedName("alipay_supported")
  private String alipaySupported = null;
  @SerializedName("direct_post_supported")
  private String directPostSupported = null;
  @SerializedName("cn_web_supported")
  private String cnWebSupported = null;
  @SerializedName("deal_url")
  private String dealUrl = null;
  @SerializedName("publish_time")
  private String publishTime = null;
  @SerializedName("recommended_deals")
  private List<DealModel> recommendedDeals = null;
  @SerializedName("jump_url")
  private String jumpUrl = null;
  @SerializedName("store_info_url")
  private String storeInfoUrl = null;
  @SerializedName("coupons")
  private List<CouponModel> coupons = null;
  @SerializedName("left_time")
  private String leftTime = null;
  @SerializedName("share_title")
  private String shareTitle = null;
  @SerializedName("share_content")
  private String shareContent = null;
  @SerializedName("share_content_weibo")
  private String shareContentWeibo = null;
  @SerializedName("share_url")
  private String shareUrl = null;
  @SerializedName("share_pic")
  private String sharePic = null;

  /**
   * 优惠ID
   **/
  @ApiModelProperty(value = "优惠ID")
  public String getDealId() {
    return dealId;
  }
  public void setDealId(String dealId) {
    this.dealId = dealId;
  }

  /**
   * 优惠标题
   **/
  @ApiModelProperty(value = "优惠标题")
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * 显示价格
   **/
  @ApiModelProperty(value = "显示价格")
  public String getPriceView() {
    return priceView;
  }
  public void setPriceView(String priceView) {
    this.priceView = priceView;
  }

  /**
   * 显示折扣
   **/
  @ApiModelProperty(value = "显示折扣")
  public String getDiscountView() {
    return discountView;
  }
  public void setDiscountView(String discountView) {
    this.discountView = discountView;
  }

  /**
   * 是否有返利
   **/
  @ApiModelProperty(value = "是否有返利")
  public String getHasRebate() {
    return hasRebate;
  }
  public void setHasRebate(String hasRebate) {
    this.hasRebate = hasRebate;
  }

  /**
   * 显示的返利信息
   **/
  @ApiModelProperty(value = "显示的返利信息")
  public String getRebateView() {
    return rebateView;
  }
  public void setRebateView(String rebateView) {
    this.rebateView = rebateView;
  }

  /**
   * 原价
   **/
  @ApiModelProperty(value = "原价")
  public String getOriginalPrice() {
    return originalPrice;
  }
  public void setOriginalPrice(String originalPrice) {
    this.originalPrice = originalPrice;
  }

  /**
   * 现价
   **/
  @ApiModelProperty(value = "现价")
  public String getNowPrice() {
    return nowPrice;
  }
  public void setNowPrice(String nowPrice) {
    this.nowPrice = nowPrice;
  }

  /**
   * 货币符号
   **/
  @ApiModelProperty(value = "货币符号")
  public String getCurrencySymbol() {
    return currencySymbol;
  }
  public void setCurrencySymbol(String currencySymbol) {
    this.currencySymbol = currencySymbol;
  }

  /**
   * 货币缩写
   **/
  @ApiModelProperty(value = "货币缩写")
  public String getCurrencyAbbr() {
    return currencyAbbr;
  }
  public void setCurrencyAbbr(String currencyAbbr) {
    this.currencyAbbr = currencyAbbr;
  }

  /**
   * 优惠图片
   **/
  @ApiModelProperty(value = "优惠图片")
  public String getDealPic() {
    return dealPic;
  }
  public void setDealPic(String dealPic) {
    this.dealPic = dealPic;
  }

  /**
   * 商家ID
   **/
  @ApiModelProperty(value = "商家ID")
  public String getStoreId() {
    return storeId;
  }
  public void setStoreId(String storeId) {
    this.storeId = storeId;
  }

  /**
   * 商家名称
   **/
  @ApiModelProperty(value = "商家名称")
  public String getStoreName() {
    return storeName;
  }
  public void setStoreName(String storeName) {
    this.storeName = storeName;
  }

  /**
   * 商家logo
   **/
  @ApiModelProperty(value = "商家logo")
  public String getStoreLogo() {
    return storeLogo;
  }
  public void setStoreLogo(String storeLogo) {
    this.storeLogo = storeLogo;
  }

  /**
   * 返利说明
   **/
  @ApiModelProperty(value = "返利说明")
  public String getRebateInstruction() {
    return rebateInstruction;
  }
  public void setRebateInstruction(String rebateInstruction) {
    this.rebateInstruction = rebateInstruction;
  }

  /**
   * 点赞数
   **/
  @ApiModelProperty(value = "点赞数")
  public String getPraiseCount() {
    return praiseCount;
  }
  public void setPraiseCount(String praiseCount) {
    this.praiseCount = praiseCount;
  }

  /**
   * 评论数
   **/
  @ApiModelProperty(value = "评论数")
  public String getCommentCount() {
    return commentCount;
  }
  public void setCommentCount(String commentCount) {
    this.commentCount = commentCount;
  }

  /**
   * 收藏数
   **/
  @ApiModelProperty(value = "收藏数")
  public String getCollectionCount() {
    return collectionCount;
  }
  public void setCollectionCount(String collectionCount) {
    this.collectionCount = collectionCount;
  }

  /**
   * 购买数
   **/
  @ApiModelProperty(value = "购买数")
  public String getBuyCount() {
    return buyCount;
  }
  public void setBuyCount(String buyCount) {
    this.buyCount = buyCount;
  }

  /**
   * 国家ID
   **/
  @ApiModelProperty(value = "国家ID")
  public String getCountryId() {
    return countryId;
  }
  public void setCountryId(String countryId) {
    this.countryId = countryId;
  }

  /**
   * 国家名称
   **/
  @ApiModelProperty(value = "国家名称")
  public String getCountryName() {
    return countryName;
  }
  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }

  /**
   * 国家缩写
   **/
  @ApiModelProperty(value = "国家缩写")
  public String getCountryAbbr() {
    return countryAbbr;
  }
  public void setCountryAbbr(String countryAbbr) {
    this.countryAbbr = countryAbbr;
  }

  /**
   * 国旗图片
   **/
  @ApiModelProperty(value = "国旗图片")
  public String getCountryFlagPic() {
    return countryFlagPic;
  }
  public void setCountryFlagPic(String countryFlagPic) {
    this.countryFlagPic = countryFlagPic;
  }

  /**
   * 是否已赞
   **/
  @ApiModelProperty(value = "是否已赞")
  public String getIsPraised() {
    return isPraised;
  }
  public void setIsPraised(String isPraised) {
    this.isPraised = isPraised;
  }

  /**
   * 是否已收藏
   **/
  @ApiModelProperty(value = "是否已收藏")
  public String getIsCollected() {
    return isCollected;
  }
  public void setIsCollected(String isCollected) {
    this.isCollected = isCollected;
  }

  /**
   * 是否支持支付宝
   **/
  @ApiModelProperty(value = "是否支持支付宝")
  public String getAlipaySupported() {
    return alipaySupported;
  }
  public void setAlipaySupported(String alipaySupported) {
    this.alipaySupported = alipaySupported;
  }

  /**
   * 是否支持直邮中国
   **/
  @ApiModelProperty(value = "是否支持直邮中国")
  public String getDirectPostSupported() {
    return directPostSupported;
  }
  public void setDirectPostSupported(String directPostSupported) {
    this.directPostSupported = directPostSupported;
  }

  /**
   * 是否支持中文页面
   **/
  @ApiModelProperty(value = "是否支持中文页面")
  public String getCnWebSupported() {
    return cnWebSupported;
  }
  public void setCnWebSupported(String cnWebSupported) {
    this.cnWebSupported = cnWebSupported;
  }

  /**
   * 优惠详情地址
   **/
  @ApiModelProperty(value = "优惠详情地址")
  public String getDealUrl() {
    return dealUrl;
  }
  public void setDealUrl(String dealUrl) {
    this.dealUrl = dealUrl;
  }

  /**
   * 发布时间
   **/
  @ApiModelProperty(value = "发布时间")
  public String getPublishTime() {
    return publishTime;
  }
  public void setPublishTime(String publishTime) {
    this.publishTime = publishTime;
  }

  /**
   * 推荐优惠
   **/
  @ApiModelProperty(value = "推荐优惠")
  public List<DealModel> getRecommendedDeals() {
    return recommendedDeals;
  }
  public void setRecommendedDeals(List<DealModel> recommendedDeals) {
    this.recommendedDeals = recommendedDeals;
  }

  /**
   * 优惠跳转链接
   **/
  @ApiModelProperty(value = "优惠跳转链接")
  public String getJumpUrl() {
    return jumpUrl;
  }
  public void setJumpUrl(String jumpUrl) {
    this.jumpUrl = jumpUrl;
  }

  /**
   * 商家信息地址
   **/
  @ApiModelProperty(value = "商家信息地址")
  public String getStoreInfoUrl() {
    return storeInfoUrl;
  }
  public void setStoreInfoUrl(String storeInfoUrl) {
    this.storeInfoUrl = storeInfoUrl;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<CouponModel> getCoupons() {
    return coupons;
  }
  public void setCoupons(List<CouponModel> coupons) {
    this.coupons = coupons;
  }

  /**
   * 优惠剩余时间说明文字，过期显示为空
   **/
  @ApiModelProperty(value = "优惠剩余时间说明文字，过期显示为空")
  public String getLeftTime() {
    return leftTime;
  }
  public void setLeftTime(String leftTime) {
    this.leftTime = leftTime;
  }

  /**
   * 分享内容
   **/
  @ApiModelProperty(value = "分享内容")
  public String getShareTitle() {
    return shareTitle;
  }
  public void setShareTitle(String shareTitle) {
    this.shareTitle = shareTitle;
  }

  /**
   * 分享内容正文
   **/
  @ApiModelProperty(value = "分享内容正文")
  public String getShareContent() {
    return shareContent;
  }
  public void setShareContent(String shareContent) {
    this.shareContent = shareContent;
  }

  /**
   * 分享到微博的正文
   **/
  @ApiModelProperty(value = "分享到微博的正文")
  public String getShareContentWeibo() {
    return shareContentWeibo;
  }
  public void setShareContentWeibo(String shareContentWeibo) {
    this.shareContentWeibo = shareContentWeibo;
  }

  /**
   * 分享内容链接
   **/
  @ApiModelProperty(value = "分享内容链接")
  public String getShareUrl() {
    return shareUrl;
  }
  public void setShareUrl(String shareUrl) {
    this.shareUrl = shareUrl;
  }

  /**
   * 分享内容封面图片
   **/
  @ApiModelProperty(value = "分享内容封面图片")
  public String getSharePic() {
    return sharePic;
  }
  public void setSharePic(String sharePic) {
    this.sharePic = sharePic;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DealExtraModel dealExtraModel = (DealExtraModel) o;
    return (dealId == null ? dealExtraModel.dealId == null : dealId.equals(dealExtraModel.dealId)) &&
        (title == null ? dealExtraModel.title == null : title.equals(dealExtraModel.title)) &&
        (priceView == null ? dealExtraModel.priceView == null : priceView.equals(dealExtraModel.priceView)) &&
        (discountView == null ? dealExtraModel.discountView == null : discountView.equals(dealExtraModel.discountView)) &&
        (hasRebate == null ? dealExtraModel.hasRebate == null : hasRebate.equals(dealExtraModel.hasRebate)) &&
        (rebateView == null ? dealExtraModel.rebateView == null : rebateView.equals(dealExtraModel.rebateView)) &&
        (originalPrice == null ? dealExtraModel.originalPrice == null : originalPrice.equals(dealExtraModel.originalPrice)) &&
        (nowPrice == null ? dealExtraModel.nowPrice == null : nowPrice.equals(dealExtraModel.nowPrice)) &&
        (currencySymbol == null ? dealExtraModel.currencySymbol == null : currencySymbol.equals(dealExtraModel.currencySymbol)) &&
        (currencyAbbr == null ? dealExtraModel.currencyAbbr == null : currencyAbbr.equals(dealExtraModel.currencyAbbr)) &&
        (dealPic == null ? dealExtraModel.dealPic == null : dealPic.equals(dealExtraModel.dealPic)) &&
        (storeId == null ? dealExtraModel.storeId == null : storeId.equals(dealExtraModel.storeId)) &&
        (storeName == null ? dealExtraModel.storeName == null : storeName.equals(dealExtraModel.storeName)) &&
        (storeLogo == null ? dealExtraModel.storeLogo == null : storeLogo.equals(dealExtraModel.storeLogo)) &&
        (rebateInstruction == null ? dealExtraModel.rebateInstruction == null : rebateInstruction.equals(dealExtraModel.rebateInstruction)) &&
        (praiseCount == null ? dealExtraModel.praiseCount == null : praiseCount.equals(dealExtraModel.praiseCount)) &&
        (commentCount == null ? dealExtraModel.commentCount == null : commentCount.equals(dealExtraModel.commentCount)) &&
        (collectionCount == null ? dealExtraModel.collectionCount == null : collectionCount.equals(dealExtraModel.collectionCount)) &&
        (buyCount == null ? dealExtraModel.buyCount == null : buyCount.equals(dealExtraModel.buyCount)) &&
        (countryId == null ? dealExtraModel.countryId == null : countryId.equals(dealExtraModel.countryId)) &&
        (countryName == null ? dealExtraModel.countryName == null : countryName.equals(dealExtraModel.countryName)) &&
        (countryAbbr == null ? dealExtraModel.countryAbbr == null : countryAbbr.equals(dealExtraModel.countryAbbr)) &&
        (countryFlagPic == null ? dealExtraModel.countryFlagPic == null : countryFlagPic.equals(dealExtraModel.countryFlagPic)) &&
        (isPraised == null ? dealExtraModel.isPraised == null : isPraised.equals(dealExtraModel.isPraised)) &&
        (isCollected == null ? dealExtraModel.isCollected == null : isCollected.equals(dealExtraModel.isCollected)) &&
        (alipaySupported == null ? dealExtraModel.alipaySupported == null : alipaySupported.equals(dealExtraModel.alipaySupported)) &&
        (directPostSupported == null ? dealExtraModel.directPostSupported == null : directPostSupported.equals(dealExtraModel.directPostSupported)) &&
        (cnWebSupported == null ? dealExtraModel.cnWebSupported == null : cnWebSupported.equals(dealExtraModel.cnWebSupported)) &&
        (dealUrl == null ? dealExtraModel.dealUrl == null : dealUrl.equals(dealExtraModel.dealUrl)) &&
        (publishTime == null ? dealExtraModel.publishTime == null : publishTime.equals(dealExtraModel.publishTime)) &&
        (recommendedDeals == null ? dealExtraModel.recommendedDeals == null : recommendedDeals.equals(dealExtraModel.recommendedDeals)) &&
        (jumpUrl == null ? dealExtraModel.jumpUrl == null : jumpUrl.equals(dealExtraModel.jumpUrl)) &&
        (storeInfoUrl == null ? dealExtraModel.storeInfoUrl == null : storeInfoUrl.equals(dealExtraModel.storeInfoUrl)) &&
        (coupons == null ? dealExtraModel.coupons == null : coupons.equals(dealExtraModel.coupons)) &&
        (leftTime == null ? dealExtraModel.leftTime == null : leftTime.equals(dealExtraModel.leftTime)) &&
        (shareTitle == null ? dealExtraModel.shareTitle == null : shareTitle.equals(dealExtraModel.shareTitle)) &&
        (shareContent == null ? dealExtraModel.shareContent == null : shareContent.equals(dealExtraModel.shareContent)) &&
        (shareContentWeibo == null ? dealExtraModel.shareContentWeibo == null : shareContentWeibo.equals(dealExtraModel.shareContentWeibo)) &&
        (shareUrl == null ? dealExtraModel.shareUrl == null : shareUrl.equals(dealExtraModel.shareUrl)) &&
        (sharePic == null ? dealExtraModel.sharePic == null : sharePic.equals(dealExtraModel.sharePic));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (dealId == null ? 0: dealId.hashCode());
    result = 31 * result + (title == null ? 0: title.hashCode());
    result = 31 * result + (priceView == null ? 0: priceView.hashCode());
    result = 31 * result + (discountView == null ? 0: discountView.hashCode());
    result = 31 * result + (hasRebate == null ? 0: hasRebate.hashCode());
    result = 31 * result + (rebateView == null ? 0: rebateView.hashCode());
    result = 31 * result + (originalPrice == null ? 0: originalPrice.hashCode());
    result = 31 * result + (nowPrice == null ? 0: nowPrice.hashCode());
    result = 31 * result + (currencySymbol == null ? 0: currencySymbol.hashCode());
    result = 31 * result + (currencyAbbr == null ? 0: currencyAbbr.hashCode());
    result = 31 * result + (dealPic == null ? 0: dealPic.hashCode());
    result = 31 * result + (storeId == null ? 0: storeId.hashCode());
    result = 31 * result + (storeName == null ? 0: storeName.hashCode());
    result = 31 * result + (storeLogo == null ? 0: storeLogo.hashCode());
    result = 31 * result + (rebateInstruction == null ? 0: rebateInstruction.hashCode());
    result = 31 * result + (praiseCount == null ? 0: praiseCount.hashCode());
    result = 31 * result + (commentCount == null ? 0: commentCount.hashCode());
    result = 31 * result + (collectionCount == null ? 0: collectionCount.hashCode());
    result = 31 * result + (buyCount == null ? 0: buyCount.hashCode());
    result = 31 * result + (countryId == null ? 0: countryId.hashCode());
    result = 31 * result + (countryName == null ? 0: countryName.hashCode());
    result = 31 * result + (countryAbbr == null ? 0: countryAbbr.hashCode());
    result = 31 * result + (countryFlagPic == null ? 0: countryFlagPic.hashCode());
    result = 31 * result + (isPraised == null ? 0: isPraised.hashCode());
    result = 31 * result + (isCollected == null ? 0: isCollected.hashCode());
    result = 31 * result + (alipaySupported == null ? 0: alipaySupported.hashCode());
    result = 31 * result + (directPostSupported == null ? 0: directPostSupported.hashCode());
    result = 31 * result + (cnWebSupported == null ? 0: cnWebSupported.hashCode());
    result = 31 * result + (dealUrl == null ? 0: dealUrl.hashCode());
    result = 31 * result + (publishTime == null ? 0: publishTime.hashCode());
    result = 31 * result + (recommendedDeals == null ? 0: recommendedDeals.hashCode());
    result = 31 * result + (jumpUrl == null ? 0: jumpUrl.hashCode());
    result = 31 * result + (storeInfoUrl == null ? 0: storeInfoUrl.hashCode());
    result = 31 * result + (coupons == null ? 0: coupons.hashCode());
    result = 31 * result + (leftTime == null ? 0: leftTime.hashCode());
    result = 31 * result + (shareTitle == null ? 0: shareTitle.hashCode());
    result = 31 * result + (shareContent == null ? 0: shareContent.hashCode());
    result = 31 * result + (shareContentWeibo == null ? 0: shareContentWeibo.hashCode());
    result = 31 * result + (shareUrl == null ? 0: shareUrl.hashCode());
    result = 31 * result + (sharePic == null ? 0: sharePic.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class DealExtraModel {\n");
    
    sb.append("  dealId: ").append(dealId).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  priceView: ").append(priceView).append("\n");
    sb.append("  discountView: ").append(discountView).append("\n");
    sb.append("  hasRebate: ").append(hasRebate).append("\n");
    sb.append("  rebateView: ").append(rebateView).append("\n");
    sb.append("  originalPrice: ").append(originalPrice).append("\n");
    sb.append("  nowPrice: ").append(nowPrice).append("\n");
    sb.append("  currencySymbol: ").append(currencySymbol).append("\n");
    sb.append("  currencyAbbr: ").append(currencyAbbr).append("\n");
    sb.append("  dealPic: ").append(dealPic).append("\n");
    sb.append("  storeId: ").append(storeId).append("\n");
    sb.append("  storeName: ").append(storeName).append("\n");
    sb.append("  storeLogo: ").append(storeLogo).append("\n");
    sb.append("  rebateInstruction: ").append(rebateInstruction).append("\n");
    sb.append("  praiseCount: ").append(praiseCount).append("\n");
    sb.append("  commentCount: ").append(commentCount).append("\n");
    sb.append("  collectionCount: ").append(collectionCount).append("\n");
    sb.append("  buyCount: ").append(buyCount).append("\n");
    sb.append("  countryId: ").append(countryId).append("\n");
    sb.append("  countryName: ").append(countryName).append("\n");
    sb.append("  countryAbbr: ").append(countryAbbr).append("\n");
    sb.append("  countryFlagPic: ").append(countryFlagPic).append("\n");
    sb.append("  isPraised: ").append(isPraised).append("\n");
    sb.append("  isCollected: ").append(isCollected).append("\n");
    sb.append("  alipaySupported: ").append(alipaySupported).append("\n");
    sb.append("  directPostSupported: ").append(directPostSupported).append("\n");
    sb.append("  cnWebSupported: ").append(cnWebSupported).append("\n");
    sb.append("  dealUrl: ").append(dealUrl).append("\n");
    sb.append("  publishTime: ").append(publishTime).append("\n");
    sb.append("  recommendedDeals: ").append(recommendedDeals).append("\n");
    sb.append("  jumpUrl: ").append(jumpUrl).append("\n");
    sb.append("  storeInfoUrl: ").append(storeInfoUrl).append("\n");
    sb.append("  coupons: ").append(coupons).append("\n");
    sb.append("  leftTime: ").append(leftTime).append("\n");
    sb.append("  shareTitle: ").append(shareTitle).append("\n");
    sb.append("  shareContent: ").append(shareContent).append("\n");
    sb.append("  shareContentWeibo: ").append(shareContentWeibo).append("\n");
    sb.append("  shareUrl: ").append(shareUrl).append("\n");
    sb.append("  sharePic: ").append(sharePic).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
