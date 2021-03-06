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


import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class DealBriefModel  {
  
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DealBriefModel dealBriefModel = (DealBriefModel) o;
    return (dealId == null ? dealBriefModel.dealId == null : dealId.equals(dealBriefModel.dealId)) &&
        (title == null ? dealBriefModel.title == null : title.equals(dealBriefModel.title)) &&
        (priceView == null ? dealBriefModel.priceView == null : priceView.equals(dealBriefModel.priceView)) &&
        (discountView == null ? dealBriefModel.discountView == null : discountView.equals(dealBriefModel.discountView)) &&
        (hasRebate == null ? dealBriefModel.hasRebate == null : hasRebate.equals(dealBriefModel.hasRebate)) &&
        (rebateView == null ? dealBriefModel.rebateView == null : rebateView.equals(dealBriefModel.rebateView)) &&
        (originalPrice == null ? dealBriefModel.originalPrice == null : originalPrice.equals(dealBriefModel.originalPrice)) &&
        (nowPrice == null ? dealBriefModel.nowPrice == null : nowPrice.equals(dealBriefModel.nowPrice)) &&
        (currencySymbol == null ? dealBriefModel.currencySymbol == null : currencySymbol.equals(dealBriefModel.currencySymbol)) &&
        (currencyAbbr == null ? dealBriefModel.currencyAbbr == null : currencyAbbr.equals(dealBriefModel.currencyAbbr)) &&
        (dealPic == null ? dealBriefModel.dealPic == null : dealPic.equals(dealBriefModel.dealPic)) &&
        (storeId == null ? dealBriefModel.storeId == null : storeId.equals(dealBriefModel.storeId)) &&
        (storeName == null ? dealBriefModel.storeName == null : storeName.equals(dealBriefModel.storeName));
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
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class DealBriefModel {\n");
    
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
    sb.append("}\n");
    return sb.toString();
  }
}
