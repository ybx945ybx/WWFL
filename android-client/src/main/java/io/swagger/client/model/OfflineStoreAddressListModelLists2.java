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

import io.swagger.client.model.OfflineStoreAddressListModelLists1;
import java.util.*;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class OfflineStoreAddressListModelLists2  {
  
  @SerializedName("province_id")
  private String provinceId = null;
  @SerializedName("province_name")
  private String provinceName = null;
  @SerializedName("location_address_count")
  private String locationAddressCount = null;
  @SerializedName("lists")
  private List<OfflineStoreAddressListModelLists1> lists = null;

  /**
   * 省份/州ID
   **/
  @ApiModelProperty(value = "省份/州ID")
  public String getProvinceId() {
    return provinceId;
  }
  public void setProvinceId(String provinceId) {
    this.provinceId = provinceId;
  }

  /**
   * 省份/州名称
   **/
  @ApiModelProperty(value = "省份/州名称")
  public String getProvinceName() {
    return provinceName;
  }
  public void setProvinceName(String provinceName) {
    this.provinceName = provinceName;
  }

  /**
   * 该地区门店地址个数
   **/
  @ApiModelProperty(value = "该地区门店地址个数")
  public String getLocationAddressCount() {
    return locationAddressCount;
  }
  public void setLocationAddressCount(String locationAddressCount) {
    this.locationAddressCount = locationAddressCount;
  }

  /**
   * 城市列表
   **/
  @ApiModelProperty(value = "城市列表")
  public List<OfflineStoreAddressListModelLists1> getLists() {
    return lists;
  }
  public void setLists(List<OfflineStoreAddressListModelLists1> lists) {
    this.lists = lists;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OfflineStoreAddressListModelLists2 offlineStoreAddressListModelLists2 = (OfflineStoreAddressListModelLists2) o;
    return (provinceId == null ? offlineStoreAddressListModelLists2.provinceId == null : provinceId.equals(offlineStoreAddressListModelLists2.provinceId)) &&
        (provinceName == null ? offlineStoreAddressListModelLists2.provinceName == null : provinceName.equals(offlineStoreAddressListModelLists2.provinceName)) &&
        (locationAddressCount == null ? offlineStoreAddressListModelLists2.locationAddressCount == null : locationAddressCount.equals(offlineStoreAddressListModelLists2.locationAddressCount)) &&
        (lists == null ? offlineStoreAddressListModelLists2.lists == null : lists.equals(offlineStoreAddressListModelLists2.lists));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (provinceId == null ? 0: provinceId.hashCode());
    result = 31 * result + (provinceName == null ? 0: provinceName.hashCode());
    result = 31 * result + (locationAddressCount == null ? 0: locationAddressCount.hashCode());
    result = 31 * result + (lists == null ? 0: lists.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class OfflineStoreAddressListModelLists2 {\n");
    
    sb.append("  provinceId: ").append(provinceId).append("\n");
    sb.append("  provinceName: ").append(provinceName).append("\n");
    sb.append("  locationAddressCount: ").append(locationAddressCount).append("\n");
    sb.append("  lists: ").append(lists).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}