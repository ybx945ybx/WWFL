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
public class DealPublicityIfModelData  {
  
  @SerializedName("commission_rate_view")
  private String commissionRateView = null;
  @SerializedName("commission_view")
  private String commissionView = null;
  @SerializedName("cover")
  private String cover = null;
  @SerializedName("title")
  private String title = null;
  @SerializedName("price_view")
  private String priceView = null;
  @SerializedName("track_link")
  private String trackLink = null;

  /**
   * 佣金比例（文字说明）
   **/
  @ApiModelProperty(value = "佣金比例（文字说明）")
  public String getCommissionRateView() {
    return commissionRateView;
  }
  public void setCommissionRateView(String commissionRateView) {
    this.commissionRateView = commissionRateView;
  }

  /**
   * 佣金数额（文字说明）
   **/
  @ApiModelProperty(value = "佣金数额（文字说明）")
  public String getCommissionView() {
    return commissionView;
  }
  public void setCommissionView(String commissionView) {
    this.commissionView = commissionView;
  }

  /**
   * 封面图片
   **/
  @ApiModelProperty(value = "封面图片")
  public String getCover() {
    return cover;
  }
  public void setCover(String cover) {
    this.cover = cover;
  }

  /**
   * 标题
   **/
  @ApiModelProperty(value = "标题")
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * 价格（文字说明）
   **/
  @ApiModelProperty(value = "价格（文字说明）")
  public String getPriceView() {
    return priceView;
  }
  public void setPriceView(String priceView) {
    this.priceView = priceView;
  }

  /**
   * 优惠跟踪链接
   **/
  @ApiModelProperty(value = "优惠跟踪链接")
  public String getTrackLink() {
    return trackLink;
  }
  public void setTrackLink(String trackLink) {
    this.trackLink = trackLink;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DealPublicityIfModelData dealPublicityIfModelData = (DealPublicityIfModelData) o;
    return (commissionRateView == null ? dealPublicityIfModelData.commissionRateView == null : commissionRateView.equals(dealPublicityIfModelData.commissionRateView)) &&
        (commissionView == null ? dealPublicityIfModelData.commissionView == null : commissionView.equals(dealPublicityIfModelData.commissionView)) &&
        (cover == null ? dealPublicityIfModelData.cover == null : cover.equals(dealPublicityIfModelData.cover)) &&
        (title == null ? dealPublicityIfModelData.title == null : title.equals(dealPublicityIfModelData.title)) &&
        (priceView == null ? dealPublicityIfModelData.priceView == null : priceView.equals(dealPublicityIfModelData.priceView)) &&
        (trackLink == null ? dealPublicityIfModelData.trackLink == null : trackLink.equals(dealPublicityIfModelData.trackLink));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (commissionRateView == null ? 0: commissionRateView.hashCode());
    result = 31 * result + (commissionView == null ? 0: commissionView.hashCode());
    result = 31 * result + (cover == null ? 0: cover.hashCode());
    result = 31 * result + (title == null ? 0: title.hashCode());
    result = 31 * result + (priceView == null ? 0: priceView.hashCode());
    result = 31 * result + (trackLink == null ? 0: trackLink.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class DealPublicityIfModelData {\n");
    
    sb.append("  commissionRateView: ").append(commissionRateView).append("\n");
    sb.append("  commissionView: ").append(commissionView).append("\n");
    sb.append("  cover: ").append(cover).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  priceView: ").append(priceView).append("\n");
    sb.append("  trackLink: ").append(trackLink).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
