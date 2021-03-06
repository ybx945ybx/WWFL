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
public class CollectionStoreModel  {
  
  @SerializedName("category_id")
  private String categoryId = null;
  @SerializedName("category_name")
  private String categoryName = null;
  @SerializedName("store_id")
  private String storeId = null;
  @SerializedName("store_name")
  private String storeName = null;
  @SerializedName("country_flag_pic")
  private String countryFlagPic = null;
  @SerializedName("store_logo")
  private String storeLogo = null;
  @SerializedName("orders_count_view")
  private String ordersCountView = null;
  @SerializedName("collection_count_view")
  private String collectionCountView = null;
  @SerializedName("user_has_rebate")
  private String userHasRebate = null;
  @SerializedName("rebate_view")
  private String rebateView = null;

  /**
   * 商家类别ID
   **/
  @ApiModelProperty(value = "商家类别ID")
  public String getCategoryId() {
    return categoryId;
  }
  public void setCategoryId(String categoryId) {
    this.categoryId = categoryId;
  }

  /**
   * 商家类别名称
   **/
  @ApiModelProperty(value = "商家类别名称")
  public String getCategoryName() {
    return categoryName;
  }
  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
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
   * 商家LOGO
   **/
  @ApiModelProperty(value = "商家LOGO")
  public String getStoreLogo() {
    return storeLogo;
  }
  public void setStoreLogo(String storeLogo) {
    this.storeLogo = storeLogo;
  }

  /**
   * 成功下单数(文字说明)
   **/
  @ApiModelProperty(value = "成功下单数(文字说明)")
  public String getOrdersCountView() {
    return ordersCountView;
  }
  public void setOrdersCountView(String ordersCountView) {
    this.ordersCountView = ordersCountView;
  }

  /**
   * 收藏数(文字说明)
   **/
  @ApiModelProperty(value = "收藏数(文字说明)")
  public String getCollectionCountView() {
    return collectionCountView;
  }
  public void setCollectionCountView(String collectionCountView) {
    this.collectionCountView = collectionCountView;
  }

  /**
   * 普通用户是否有返利
   **/
  @ApiModelProperty(value = "普通用户是否有返利")
  public String getUserHasRebate() {
    return userHasRebate;
  }
  public void setUserHasRebate(String userHasRebate) {
    this.userHasRebate = userHasRebate;
  }

  /**
   * 返利比例(文字说明)
   **/
  @ApiModelProperty(value = "返利比例(文字说明)")
  public String getRebateView() {
    return rebateView;
  }
  public void setRebateView(String rebateView) {
    this.rebateView = rebateView;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CollectionStoreModel collectionStoreModel = (CollectionStoreModel) o;
    return (categoryId == null ? collectionStoreModel.categoryId == null : categoryId.equals(collectionStoreModel.categoryId)) &&
        (categoryName == null ? collectionStoreModel.categoryName == null : categoryName.equals(collectionStoreModel.categoryName)) &&
        (storeId == null ? collectionStoreModel.storeId == null : storeId.equals(collectionStoreModel.storeId)) &&
        (storeName == null ? collectionStoreModel.storeName == null : storeName.equals(collectionStoreModel.storeName)) &&
        (countryFlagPic == null ? collectionStoreModel.countryFlagPic == null : countryFlagPic.equals(collectionStoreModel.countryFlagPic)) &&
        (storeLogo == null ? collectionStoreModel.storeLogo == null : storeLogo.equals(collectionStoreModel.storeLogo)) &&
        (ordersCountView == null ? collectionStoreModel.ordersCountView == null : ordersCountView.equals(collectionStoreModel.ordersCountView)) &&
        (collectionCountView == null ? collectionStoreModel.collectionCountView == null : collectionCountView.equals(collectionStoreModel.collectionCountView)) &&
        (userHasRebate == null ? collectionStoreModel.userHasRebate == null : userHasRebate.equals(collectionStoreModel.userHasRebate)) &&
        (rebateView == null ? collectionStoreModel.rebateView == null : rebateView.equals(collectionStoreModel.rebateView));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (categoryId == null ? 0: categoryId.hashCode());
    result = 31 * result + (categoryName == null ? 0: categoryName.hashCode());
    result = 31 * result + (storeId == null ? 0: storeId.hashCode());
    result = 31 * result + (storeName == null ? 0: storeName.hashCode());
    result = 31 * result + (countryFlagPic == null ? 0: countryFlagPic.hashCode());
    result = 31 * result + (storeLogo == null ? 0: storeLogo.hashCode());
    result = 31 * result + (ordersCountView == null ? 0: ordersCountView.hashCode());
    result = 31 * result + (collectionCountView == null ? 0: collectionCountView.hashCode());
    result = 31 * result + (userHasRebate == null ? 0: userHasRebate.hashCode());
    result = 31 * result + (rebateView == null ? 0: rebateView.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class CollectionStoreModel {\n");
    
    sb.append("  categoryId: ").append(categoryId).append("\n");
    sb.append("  categoryName: ").append(categoryName).append("\n");
    sb.append("  storeId: ").append(storeId).append("\n");
    sb.append("  storeName: ").append(storeName).append("\n");
    sb.append("  countryFlagPic: ").append(countryFlagPic).append("\n");
    sb.append("  storeLogo: ").append(storeLogo).append("\n");
    sb.append("  ordersCountView: ").append(ordersCountView).append("\n");
    sb.append("  collectionCountView: ").append(collectionCountView).append("\n");
    sb.append("  userHasRebate: ").append(userHasRebate).append("\n");
    sb.append("  rebateView: ").append(rebateView).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
