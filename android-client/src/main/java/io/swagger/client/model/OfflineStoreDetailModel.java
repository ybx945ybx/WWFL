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
public class OfflineStoreDetailModel  {
  
  @SerializedName("store_id")
  private String storeId = null;
  @SerializedName("store_name")
  private String storeName = null;
  @SerializedName("store_logo")
  private String storeLogo = null;
  @SerializedName("country_abbr")
  private String countryAbbr = null;
  @SerializedName("country_flag_pic")
  private String countryFlagPic = null;
  @SerializedName("rebate_view")
  private String rebateView = null;
  @SerializedName("has_rebate")
  private String hasRebate = null;
  @SerializedName("has_related_deals")
  private String hasRelatedDeals = null;
  @SerializedName("category_id")
  private String categoryId = null;
  @SerializedName("category_name")
  private String categoryName = null;
  @SerializedName("address")
  private String address = null;
  @SerializedName("website")
  private String website = null;
  @SerializedName("business_hours")
  private String businessHours = null;
  @SerializedName("rebate_influence_view")
  private String rebateInfluenceView = null;
  @SerializedName("collections_count_view")
  private String collectionsCountView = null;
  @SerializedName("store_description")
  private String storeDescription = null;
  @SerializedName("has_offline_store")
  private String hasOfflineStore = null;

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
   * 商家LOGO地址
   **/
  @ApiModelProperty(value = "商家LOGO地址")
  public String getStoreLogo() {
    return storeLogo;
  }
  public void setStoreLogo(String storeLogo) {
    this.storeLogo = storeLogo;
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
   * 国旗图片地址
   **/
  @ApiModelProperty(value = "国旗图片地址")
  public String getCountryFlagPic() {
    return countryFlagPic;
  }
  public void setCountryFlagPic(String countryFlagPic) {
    this.countryFlagPic = countryFlagPic;
  }

  /**
   * 返利信息（文字说明）
   **/
  @ApiModelProperty(value = "返利信息（文字说明）")
  public String getRebateView() {
    return rebateView;
  }
  public void setRebateView(String rebateView) {
    this.rebateView = rebateView;
  }

  /**
   * 是否有返利 1是 0否
   **/
  @ApiModelProperty(value = "是否有返利 1是 0否")
  public String getHasRebate() {
    return hasRebate;
  }
  public void setHasRebate(String hasRebate) {
    this.hasRebate = hasRebate;
  }

  /**
   * 是否有相关优惠 1是 0否
   **/
  @ApiModelProperty(value = "是否有相关优惠 1是 0否")
  public String getHasRelatedDeals() {
    return hasRelatedDeals;
  }
  public void setHasRelatedDeals(String hasRelatedDeals) {
    this.hasRelatedDeals = hasRelatedDeals;
  }

  /**
   * 分类ID
   **/
  @ApiModelProperty(value = "分类ID")
  public String getCategoryId() {
    return categoryId;
  }
  public void setCategoryId(String categoryId) {
    this.categoryId = categoryId;
  }

  /**
   * 分类名称
   **/
  @ApiModelProperty(value = "分类名称")
  public String getCategoryName() {
    return categoryName;
  }
  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  /**
   * 总部地址
   **/
  @ApiModelProperty(value = "总部地址")
  public String getAddress() {
    return address;
  }
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * 官网地址
   **/
  @ApiModelProperty(value = "官网地址")
  public String getWebsite() {
    return website;
  }
  public void setWebsite(String website) {
    this.website = website;
  }

  /**
   * 营业时间(文字说明)
   **/
  @ApiModelProperty(value = "营业时间(文字说明)")
  public String getBusinessHours() {
    return businessHours;
  }
  public void setBusinessHours(String businessHours) {
    this.businessHours = businessHours;
  }

  /**
   * 获得返利的用户数（文字说明）
   **/
  @ApiModelProperty(value = "获得返利的用户数（文字说明）")
  public String getRebateInfluenceView() {
    return rebateInfluenceView;
  }
  public void setRebateInfluenceView(String rebateInfluenceView) {
    this.rebateInfluenceView = rebateInfluenceView;
  }

  /**
   * 收藏的用户数（文字说明）
   **/
  @ApiModelProperty(value = "收藏的用户数（文字说明）")
  public String getCollectionsCountView() {
    return collectionsCountView;
  }
  public void setCollectionsCountView(String collectionsCountView) {
    this.collectionsCountView = collectionsCountView;
  }

  /**
   * 返利商家介绍
   **/
  @ApiModelProperty(value = "返利商家介绍")
  public String getStoreDescription() {
    return storeDescription;
  }
  public void setStoreDescription(String storeDescription) {
    this.storeDescription = storeDescription;
  }

  /**
   * 是否有线下门店
   **/
  @ApiModelProperty(value = "是否有线下门店")
  public String getHasOfflineStore() {
    return hasOfflineStore;
  }
  public void setHasOfflineStore(String hasOfflineStore) {
    this.hasOfflineStore = hasOfflineStore;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OfflineStoreDetailModel offlineStoreDetailModel = (OfflineStoreDetailModel) o;
    return (storeId == null ? offlineStoreDetailModel.storeId == null : storeId.equals(offlineStoreDetailModel.storeId)) &&
        (storeName == null ? offlineStoreDetailModel.storeName == null : storeName.equals(offlineStoreDetailModel.storeName)) &&
        (storeLogo == null ? offlineStoreDetailModel.storeLogo == null : storeLogo.equals(offlineStoreDetailModel.storeLogo)) &&
        (countryAbbr == null ? offlineStoreDetailModel.countryAbbr == null : countryAbbr.equals(offlineStoreDetailModel.countryAbbr)) &&
        (countryFlagPic == null ? offlineStoreDetailModel.countryFlagPic == null : countryFlagPic.equals(offlineStoreDetailModel.countryFlagPic)) &&
        (rebateView == null ? offlineStoreDetailModel.rebateView == null : rebateView.equals(offlineStoreDetailModel.rebateView)) &&
        (hasRebate == null ? offlineStoreDetailModel.hasRebate == null : hasRebate.equals(offlineStoreDetailModel.hasRebate)) &&
        (hasRelatedDeals == null ? offlineStoreDetailModel.hasRelatedDeals == null : hasRelatedDeals.equals(offlineStoreDetailModel.hasRelatedDeals)) &&
        (categoryId == null ? offlineStoreDetailModel.categoryId == null : categoryId.equals(offlineStoreDetailModel.categoryId)) &&
        (categoryName == null ? offlineStoreDetailModel.categoryName == null : categoryName.equals(offlineStoreDetailModel.categoryName)) &&
        (address == null ? offlineStoreDetailModel.address == null : address.equals(offlineStoreDetailModel.address)) &&
        (website == null ? offlineStoreDetailModel.website == null : website.equals(offlineStoreDetailModel.website)) &&
        (businessHours == null ? offlineStoreDetailModel.businessHours == null : businessHours.equals(offlineStoreDetailModel.businessHours)) &&
        (rebateInfluenceView == null ? offlineStoreDetailModel.rebateInfluenceView == null : rebateInfluenceView.equals(offlineStoreDetailModel.rebateInfluenceView)) &&
        (collectionsCountView == null ? offlineStoreDetailModel.collectionsCountView == null : collectionsCountView.equals(offlineStoreDetailModel.collectionsCountView)) &&
        (storeDescription == null ? offlineStoreDetailModel.storeDescription == null : storeDescription.equals(offlineStoreDetailModel.storeDescription)) &&
        (hasOfflineStore == null ? offlineStoreDetailModel.hasOfflineStore == null : hasOfflineStore.equals(offlineStoreDetailModel.hasOfflineStore));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (storeId == null ? 0: storeId.hashCode());
    result = 31 * result + (storeName == null ? 0: storeName.hashCode());
    result = 31 * result + (storeLogo == null ? 0: storeLogo.hashCode());
    result = 31 * result + (countryAbbr == null ? 0: countryAbbr.hashCode());
    result = 31 * result + (countryFlagPic == null ? 0: countryFlagPic.hashCode());
    result = 31 * result + (rebateView == null ? 0: rebateView.hashCode());
    result = 31 * result + (hasRebate == null ? 0: hasRebate.hashCode());
    result = 31 * result + (hasRelatedDeals == null ? 0: hasRelatedDeals.hashCode());
    result = 31 * result + (categoryId == null ? 0: categoryId.hashCode());
    result = 31 * result + (categoryName == null ? 0: categoryName.hashCode());
    result = 31 * result + (address == null ? 0: address.hashCode());
    result = 31 * result + (website == null ? 0: website.hashCode());
    result = 31 * result + (businessHours == null ? 0: businessHours.hashCode());
    result = 31 * result + (rebateInfluenceView == null ? 0: rebateInfluenceView.hashCode());
    result = 31 * result + (collectionsCountView == null ? 0: collectionsCountView.hashCode());
    result = 31 * result + (storeDescription == null ? 0: storeDescription.hashCode());
    result = 31 * result + (hasOfflineStore == null ? 0: hasOfflineStore.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class OfflineStoreDetailModel {\n");
    
    sb.append("  storeId: ").append(storeId).append("\n");
    sb.append("  storeName: ").append(storeName).append("\n");
    sb.append("  storeLogo: ").append(storeLogo).append("\n");
    sb.append("  countryAbbr: ").append(countryAbbr).append("\n");
    sb.append("  countryFlagPic: ").append(countryFlagPic).append("\n");
    sb.append("  rebateView: ").append(rebateView).append("\n");
    sb.append("  hasRebate: ").append(hasRebate).append("\n");
    sb.append("  hasRelatedDeals: ").append(hasRelatedDeals).append("\n");
    sb.append("  categoryId: ").append(categoryId).append("\n");
    sb.append("  categoryName: ").append(categoryName).append("\n");
    sb.append("  address: ").append(address).append("\n");
    sb.append("  website: ").append(website).append("\n");
    sb.append("  businessHours: ").append(businessHours).append("\n");
    sb.append("  rebateInfluenceView: ").append(rebateInfluenceView).append("\n");
    sb.append("  collectionsCountView: ").append(collectionsCountView).append("\n");
    sb.append("  storeDescription: ").append(storeDescription).append("\n");
    sb.append("  hasOfflineStore: ").append(hasOfflineStore).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
