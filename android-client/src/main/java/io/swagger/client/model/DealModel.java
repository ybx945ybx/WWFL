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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "")
public class DealModel extends DealSlidePicBaseModel implements Serializable {
  
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
  @SerializedName("praise_count")
  private String praiseCount = null;
  @SerializedName("comment_count")
  private String commentCount = null;
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
  @SerializedName("publish_time")
  private String publishTime = null;
  @SerializedName("left_time")
  private String leftTime = null;

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
   * 优惠剩余时间说明文字，过期显示为空
   **/
  @ApiModelProperty(value = "优惠剩余时间说明文字，过期显示为空")
  public String getLeftTime() {
    return leftTime;
  }
  public void setLeftTime(String leftTime) {
    this.leftTime = leftTime;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DealModel dealModel = (DealModel) o;
    return (dealId == null ? dealModel.dealId == null : dealId.equals(dealModel.dealId)) &&
        (title == null ? dealModel.title == null : title.equals(dealModel.title)) &&
        (priceView == null ? dealModel.priceView == null : priceView.equals(dealModel.priceView)) &&
        (discountView == null ? dealModel.discountView == null : discountView.equals(dealModel.discountView)) &&
        (hasRebate == null ? dealModel.hasRebate == null : hasRebate.equals(dealModel.hasRebate)) &&
        (rebateView == null ? dealModel.rebateView == null : rebateView.equals(dealModel.rebateView)) &&
        (originalPrice == null ? dealModel.originalPrice == null : originalPrice.equals(dealModel.originalPrice)) &&
        (nowPrice == null ? dealModel.nowPrice == null : nowPrice.equals(dealModel.nowPrice)) &&
        (currencySymbol == null ? dealModel.currencySymbol == null : currencySymbol.equals(dealModel.currencySymbol)) &&
        (currencyAbbr == null ? dealModel.currencyAbbr == null : currencyAbbr.equals(dealModel.currencyAbbr)) &&
        (dealPic == null ? dealModel.dealPic == null : dealPic.equals(dealModel.dealPic)) &&
        (storeId == null ? dealModel.storeId == null : storeId.equals(dealModel.storeId)) &&
        (storeName == null ? dealModel.storeName == null : storeName.equals(dealModel.storeName)) &&
        (praiseCount == null ? dealModel.praiseCount == null : praiseCount.equals(dealModel.praiseCount)) &&
        (commentCount == null ? dealModel.commentCount == null : commentCount.equals(dealModel.commentCount)) &&
        (countryId == null ? dealModel.countryId == null : countryId.equals(dealModel.countryId)) &&
        (countryName == null ? dealModel.countryName == null : countryName.equals(dealModel.countryName)) &&
        (countryAbbr == null ? dealModel.countryAbbr == null : countryAbbr.equals(dealModel.countryAbbr)) &&
        (countryFlagPic == null ? dealModel.countryFlagPic == null : countryFlagPic.equals(dealModel.countryFlagPic)) &&
        (isPraised == null ? dealModel.isPraised == null : isPraised.equals(dealModel.isPraised)) &&
        (publishTime == null ? dealModel.publishTime == null : publishTime.equals(dealModel.publishTime)) &&
        (leftTime == null ? dealModel.leftTime == null : leftTime.equals(dealModel.leftTime));
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
    result = 31 * result + (praiseCount == null ? 0: praiseCount.hashCode());
    result = 31 * result + (commentCount == null ? 0: commentCount.hashCode());
    result = 31 * result + (countryId == null ? 0: countryId.hashCode());
    result = 31 * result + (countryName == null ? 0: countryName.hashCode());
    result = 31 * result + (countryAbbr == null ? 0: countryAbbr.hashCode());
    result = 31 * result + (countryFlagPic == null ? 0: countryFlagPic.hashCode());
    result = 31 * result + (isPraised == null ? 0: isPraised.hashCode());
    result = 31 * result + (publishTime == null ? 0: publishTime.hashCode());
    result = 31 * result + (leftTime == null ? 0: leftTime.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class DealModel {\n");
    
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
    sb.append("  praiseCount: ").append(praiseCount).append("\n");
    sb.append("  commentCount: ").append(commentCount).append("\n");
    sb.append("  countryId: ").append(countryId).append("\n");
    sb.append("  countryName: ").append(countryName).append("\n");
    sb.append("  countryAbbr: ").append(countryAbbr).append("\n");
    sb.append("  countryFlagPic: ").append(countryFlagPic).append("\n");
    sb.append("  isPraised: ").append(isPraised).append("\n");
    sb.append("  publishTime: ").append(publishTime).append("\n");
    sb.append("  leftTime: ").append(leftTime).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}