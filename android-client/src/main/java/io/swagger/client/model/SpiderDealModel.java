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

import io.swagger.client.model.SpiderDealModelPropertiesList;
import io.swagger.client.model.SpiderDealModelSkuList;
import java.util.*;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class SpiderDealModel  {
  
  @SerializedName("deal_original_url")
  private String dealOriginalUrl = null;
  @SerializedName("deal_id")
  private String dealId = null;
  @SerializedName("store_id")
  private String storeId = null;
  @SerializedName("store_name")
  private String storeName = null;
  @SerializedName("deal_pic")
  private String dealPic = null;
  @SerializedName("deal_title")
  private String dealTitle = null;
  @SerializedName("price_symbol")
  private String priceSymbol = null;
  @SerializedName("sku_list")
  private List<SpiderDealModelSkuList> skuList = null;
  @SerializedName("properties_list")
  private List<SpiderDealModelPropertiesList> propertiesList = null;

  /**
   * 优惠原始链接
   **/
  @ApiModelProperty(value = "优惠原始链接")
  public String getDealOriginalUrl() {
    return dealOriginalUrl;
  }
  public void setDealOriginalUrl(String dealOriginalUrl) {
    this.dealOriginalUrl = dealOriginalUrl;
  }

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
   * 优惠封面图片
   **/
  @ApiModelProperty(value = "优惠封面图片")
  public String getDealPic() {
    return dealPic;
  }
  public void setDealPic(String dealPic) {
    this.dealPic = dealPic;
  }

  /**
   * 优惠标题
   **/
  @ApiModelProperty(value = "优惠标题")
  public String getDealTitle() {
    return dealTitle;
  }
  public void setDealTitle(String dealTitle) {
    this.dealTitle = dealTitle;
  }

  /**
   * 货币符号
   **/
  @ApiModelProperty(value = "货币符号")
  public String getPriceSymbol() {
    return priceSymbol;
  }
  public void setPriceSymbol(String priceSymbol) {
    this.priceSymbol = priceSymbol;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<SpiderDealModelSkuList> getSkuList() {
    return skuList;
  }
  public void setSkuList(List<SpiderDealModelSkuList> skuList) {
    this.skuList = skuList;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<SpiderDealModelPropertiesList> getPropertiesList() {
    return propertiesList;
  }
  public void setPropertiesList(List<SpiderDealModelPropertiesList> propertiesList) {
    this.propertiesList = propertiesList;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SpiderDealModel spiderDealModel = (SpiderDealModel) o;
    return (dealOriginalUrl == null ? spiderDealModel.dealOriginalUrl == null : dealOriginalUrl.equals(spiderDealModel.dealOriginalUrl)) &&
        (dealId == null ? spiderDealModel.dealId == null : dealId.equals(spiderDealModel.dealId)) &&
        (storeId == null ? spiderDealModel.storeId == null : storeId.equals(spiderDealModel.storeId)) &&
        (storeName == null ? spiderDealModel.storeName == null : storeName.equals(spiderDealModel.storeName)) &&
        (dealPic == null ? spiderDealModel.dealPic == null : dealPic.equals(spiderDealModel.dealPic)) &&
        (dealTitle == null ? spiderDealModel.dealTitle == null : dealTitle.equals(spiderDealModel.dealTitle)) &&
        (priceSymbol == null ? spiderDealModel.priceSymbol == null : priceSymbol.equals(spiderDealModel.priceSymbol)) &&
        (skuList == null ? spiderDealModel.skuList == null : skuList.equals(spiderDealModel.skuList)) &&
        (propertiesList == null ? spiderDealModel.propertiesList == null : propertiesList.equals(spiderDealModel.propertiesList));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (dealOriginalUrl == null ? 0: dealOriginalUrl.hashCode());
    result = 31 * result + (dealId == null ? 0: dealId.hashCode());
    result = 31 * result + (storeId == null ? 0: storeId.hashCode());
    result = 31 * result + (storeName == null ? 0: storeName.hashCode());
    result = 31 * result + (dealPic == null ? 0: dealPic.hashCode());
    result = 31 * result + (dealTitle == null ? 0: dealTitle.hashCode());
    result = 31 * result + (priceSymbol == null ? 0: priceSymbol.hashCode());
    result = 31 * result + (skuList == null ? 0: skuList.hashCode());
    result = 31 * result + (propertiesList == null ? 0: propertiesList.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class SpiderDealModel {\n");
    
    sb.append("  dealOriginalUrl: ").append(dealOriginalUrl).append("\n");
    sb.append("  dealId: ").append(dealId).append("\n");
    sb.append("  storeId: ").append(storeId).append("\n");
    sb.append("  storeName: ").append(storeName).append("\n");
    sb.append("  dealPic: ").append(dealPic).append("\n");
    sb.append("  dealTitle: ").append(dealTitle).append("\n");
    sb.append("  priceSymbol: ").append(priceSymbol).append("\n");
    sb.append("  skuList: ").append(skuList).append("\n");
    sb.append("  propertiesList: ").append(propertiesList).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
