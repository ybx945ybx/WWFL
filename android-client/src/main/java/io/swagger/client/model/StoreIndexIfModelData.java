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

import io.swagger.client.model.EnteredStoreModel;
import io.swagger.client.model.SlidePicModel;
import io.swagger.client.model.StoreIndexIfModelDataAreasBriefs;
import io.swagger.client.model.StoreIndexIfModelDataSuperRebate;
import io.swagger.client.model.StoreWithDealsModel;
import java.util.*;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class StoreIndexIfModelData  {
  
  @SerializedName("super_rebate")
  private StoreIndexIfModelDataSuperRebate superRebate = null;
  @SerializedName("favorite_stores_count")
  private String favoriteStoresCount = null;
  @SerializedName("cross_bar_pics")
  private List<SlidePicModel> crossBarPics = null;
  @SerializedName("areas_briefs")
  private List<StoreIndexIfModelDataAreasBriefs> areasBriefs = null;
  @SerializedName("double_rebate_stores")
  private List<EnteredStoreModel> doubleRebateStores = null;
  @SerializedName("stores_with_deals")
  private List<StoreWithDealsModel> storesWithDeals = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public StoreIndexIfModelDataSuperRebate getSuperRebate() {
    return superRebate;
  }
  public void setSuperRebate(StoreIndexIfModelDataSuperRebate superRebate) {
    this.superRebate = superRebate;
  }

  /**
   * 收藏的商家的数目（数值）
   **/
  @ApiModelProperty(value = "收藏的商家的数目（数值）")
  public String getFavoriteStoresCount() {
    return favoriteStoresCount;
  }
  public void setFavoriteStoresCount(String favoriteStoresCount) {
    this.favoriteStoresCount = favoriteStoresCount;
  }

  /**
   * 横幅广告
   **/
  @ApiModelProperty(value = "横幅广告")
  public List<SlidePicModel> getCrossBarPics() {
    return crossBarPics;
  }
  public void setCrossBarPics(List<SlidePicModel> crossBarPics) {
    this.crossBarPics = crossBarPics;
  }

  /**
   * 国家/地区简要信息
   **/
  @ApiModelProperty(value = "国家/地区简要信息")
  public List<StoreIndexIfModelDataAreasBriefs> getAreasBriefs() {
    return areasBriefs;
  }
  public void setAreasBriefs(List<StoreIndexIfModelDataAreasBriefs> areasBriefs) {
    this.areasBriefs = areasBriefs;
  }

  /**
   * 加倍返利
   **/
  @ApiModelProperty(value = "加倍返利")
  public List<EnteredStoreModel> getDoubleRebateStores() {
    return doubleRebateStores;
  }
  public void setDoubleRebateStores(List<EnteredStoreModel> doubleRebateStores) {
    this.doubleRebateStores = doubleRebateStores;
  }

  /**
   * 商家及其置顶优惠
   **/
  @ApiModelProperty(value = "商家及其置顶优惠")
  public List<StoreWithDealsModel> getStoresWithDeals() {
    return storesWithDeals;
  }
  public void setStoresWithDeals(List<StoreWithDealsModel> storesWithDeals) {
    this.storesWithDeals = storesWithDeals;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StoreIndexIfModelData storeIndexIfModelData = (StoreIndexIfModelData) o;
    return (superRebate == null ? storeIndexIfModelData.superRebate == null : superRebate.equals(storeIndexIfModelData.superRebate)) &&
        (favoriteStoresCount == null ? storeIndexIfModelData.favoriteStoresCount == null : favoriteStoresCount.equals(storeIndexIfModelData.favoriteStoresCount)) &&
        (crossBarPics == null ? storeIndexIfModelData.crossBarPics == null : crossBarPics.equals(storeIndexIfModelData.crossBarPics)) &&
        (areasBriefs == null ? storeIndexIfModelData.areasBriefs == null : areasBriefs.equals(storeIndexIfModelData.areasBriefs)) &&
        (doubleRebateStores == null ? storeIndexIfModelData.doubleRebateStores == null : doubleRebateStores.equals(storeIndexIfModelData.doubleRebateStores)) &&
        (storesWithDeals == null ? storeIndexIfModelData.storesWithDeals == null : storesWithDeals.equals(storeIndexIfModelData.storesWithDeals));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (superRebate == null ? 0: superRebate.hashCode());
    result = 31 * result + (favoriteStoresCount == null ? 0: favoriteStoresCount.hashCode());
    result = 31 * result + (crossBarPics == null ? 0: crossBarPics.hashCode());
    result = 31 * result + (areasBriefs == null ? 0: areasBriefs.hashCode());
    result = 31 * result + (doubleRebateStores == null ? 0: doubleRebateStores.hashCode());
    result = 31 * result + (storesWithDeals == null ? 0: storesWithDeals.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class StoreIndexIfModelData {\n");
    
    sb.append("  superRebate: ").append(superRebate).append("\n");
    sb.append("  favoriteStoresCount: ").append(favoriteStoresCount).append("\n");
    sb.append("  crossBarPics: ").append(crossBarPics).append("\n");
    sb.append("  areasBriefs: ").append(areasBriefs).append("\n");
    sb.append("  doubleRebateStores: ").append(doubleRebateStores).append("\n");
    sb.append("  storesWithDeals: ").append(storesWithDeals).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}