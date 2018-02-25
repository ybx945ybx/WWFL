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

import io.swagger.client.model.CouponModel;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class SquiredModelLists  {
  
  @SerializedName("title")
  private String title = null;
  @SerializedName("sub_title")
  private String subTitle = null;
  @SerializedName("img")
  private String img = null;
  @SerializedName("link")
  private String link = null;
  @SerializedName("source_link")
  private String sourceLink = null;
  @SerializedName("currency")
  private String currency = null;
  @SerializedName("cost_price")
  private String costPrice = null;
  @SerializedName("now_price")
  private String nowPrice = null;
  @SerializedName("price_view")
  private String priceView = null;
  @SerializedName("hand_price")
  private String handPrice = null;
  @SerializedName("coupon")
  private CouponModel coupon = null;

  /**
   * 九宫格
   **/
  @ApiModelProperty(value = "九宫格")
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * 九宫格副标题
   **/
  @ApiModelProperty(value = "九宫格副标题")
  public String getSubTitle() {
    return subTitle;
  }
  public void setSubTitle(String subTitle) {
    this.subTitle = subTitle;
  }

  /**
   * 图片地址
   **/
  @ApiModelProperty(value = "图片地址")
  public String getImg() {
    return img;
  }
  public void setImg(String img) {
    this.img = img;
  }

  /**
   * 跳转链接
   **/
  @ApiModelProperty(value = "跳转链接")
  public String getLink() {
    return link;
  }
  public void setLink(String link) {
    this.link = link;
  }

  /**
   * 跳转原始链接
   **/
  @ApiModelProperty(value = "跳转原始链接")
  public String getSourceLink() {
    return sourceLink;
  }
  public void setSourceLink(String sourceLink) {
    this.sourceLink = sourceLink;
  }

  /**
   * 货币符号
   **/
  @ApiModelProperty(value = "货币符号")
  public String getCurrency() {
    return currency;
  }
  public void setCurrency(String currency) {
    this.currency = currency;
  }

  /**
   * 原价
   **/
  @ApiModelProperty(value = "原价")
  public String getCostPrice() {
    return costPrice;
  }
  public void setCostPrice(String costPrice) {
    this.costPrice = costPrice;
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
   * 用于显示的价格
   **/
  @ApiModelProperty(value = "用于显示的价格")
  public String getPriceView() {
    return priceView;
  }
  public void setPriceView(String priceView) {
    this.priceView = priceView;
  }

  /**
   * 到手价(约xxx)
   **/
  @ApiModelProperty(value = "到手价(约xxx)")
  public String getHandPrice() {
    return handPrice;
  }
  public void setHandPrice(String handPrice) {
    this.handPrice = handPrice;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public CouponModel getCoupon() {
    return coupon;
  }
  public void setCoupon(CouponModel coupon) {
    this.coupon = coupon;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SquiredModelLists squiredModelLists = (SquiredModelLists) o;
    return (title == null ? squiredModelLists.title == null : title.equals(squiredModelLists.title)) &&
        (subTitle == null ? squiredModelLists.subTitle == null : subTitle.equals(squiredModelLists.subTitle)) &&
        (img == null ? squiredModelLists.img == null : img.equals(squiredModelLists.img)) &&
        (link == null ? squiredModelLists.link == null : link.equals(squiredModelLists.link)) &&
        (sourceLink == null ? squiredModelLists.sourceLink == null : sourceLink.equals(squiredModelLists.sourceLink)) &&
        (currency == null ? squiredModelLists.currency == null : currency.equals(squiredModelLists.currency)) &&
        (costPrice == null ? squiredModelLists.costPrice == null : costPrice.equals(squiredModelLists.costPrice)) &&
        (nowPrice == null ? squiredModelLists.nowPrice == null : nowPrice.equals(squiredModelLists.nowPrice)) &&
        (priceView == null ? squiredModelLists.priceView == null : priceView.equals(squiredModelLists.priceView)) &&
        (handPrice == null ? squiredModelLists.handPrice == null : handPrice.equals(squiredModelLists.handPrice)) &&
        (coupon == null ? squiredModelLists.coupon == null : coupon.equals(squiredModelLists.coupon));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (title == null ? 0: title.hashCode());
    result = 31 * result + (subTitle == null ? 0: subTitle.hashCode());
    result = 31 * result + (img == null ? 0: img.hashCode());
    result = 31 * result + (link == null ? 0: link.hashCode());
    result = 31 * result + (sourceLink == null ? 0: sourceLink.hashCode());
    result = 31 * result + (currency == null ? 0: currency.hashCode());
    result = 31 * result + (costPrice == null ? 0: costPrice.hashCode());
    result = 31 * result + (nowPrice == null ? 0: nowPrice.hashCode());
    result = 31 * result + (priceView == null ? 0: priceView.hashCode());
    result = 31 * result + (handPrice == null ? 0: handPrice.hashCode());
    result = 31 * result + (coupon == null ? 0: coupon.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class SquiredModelLists {\n");
    
    sb.append("  title: ").append(title).append("\n");
    sb.append("  subTitle: ").append(subTitle).append("\n");
    sb.append("  img: ").append(img).append("\n");
    sb.append("  link: ").append(link).append("\n");
    sb.append("  sourceLink: ").append(sourceLink).append("\n");
    sb.append("  currency: ").append(currency).append("\n");
    sb.append("  costPrice: ").append(costPrice).append("\n");
    sb.append("  nowPrice: ").append(nowPrice).append("\n");
    sb.append("  priceView: ").append(priceView).append("\n");
    sb.append("  handPrice: ").append(handPrice).append("\n");
    sb.append("  coupon: ").append(coupon).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
