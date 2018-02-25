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
public class OfflineStoreAddressListModelLists  {
  
  @SerializedName("id")
  private String id = null;
  @SerializedName("store_id")
  private String storeId = null;
  @SerializedName("country_id")
  private String countryId = null;
  @SerializedName("province_id")
  private String provinceId = null;
  @SerializedName("province")
  private String province = null;
  @SerializedName("city_id")
  private String cityId = null;
  @SerializedName("city")
  private String city = null;
  @SerializedName("address")
  private String address = null;
  @SerializedName("longitude")
  private String longitude = null;
  @SerializedName("latitude")
  private String latitude = null;

  /**
   * 地址ID
   **/
  @ApiModelProperty(value = "地址ID")
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
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
  public String getProvince() {
    return province;
  }
  public void setProvince(String province) {
    this.province = province;
  }

  /**
   * 城市ID
   **/
  @ApiModelProperty(value = "城市ID")
  public String getCityId() {
    return cityId;
  }
  public void setCityId(String cityId) {
    this.cityId = cityId;
  }

  /**
   * 城市名称
   **/
  @ApiModelProperty(value = "城市名称")
  public String getCity() {
    return city;
  }
  public void setCity(String city) {
    this.city = city;
  }

  /**
   * 详细地址
   **/
  @ApiModelProperty(value = "详细地址")
  public String getAddress() {
    return address;
  }
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * 经度
   **/
  @ApiModelProperty(value = "经度")
  public String getLongitude() {
    return longitude;
  }
  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  /**
   * 纬度
   **/
  @ApiModelProperty(value = "纬度")
  public String getLatitude() {
    return latitude;
  }
  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OfflineStoreAddressListModelLists offlineStoreAddressListModelLists = (OfflineStoreAddressListModelLists) o;
    return (id == null ? offlineStoreAddressListModelLists.id == null : id.equals(offlineStoreAddressListModelLists.id)) &&
        (storeId == null ? offlineStoreAddressListModelLists.storeId == null : storeId.equals(offlineStoreAddressListModelLists.storeId)) &&
        (countryId == null ? offlineStoreAddressListModelLists.countryId == null : countryId.equals(offlineStoreAddressListModelLists.countryId)) &&
        (provinceId == null ? offlineStoreAddressListModelLists.provinceId == null : provinceId.equals(offlineStoreAddressListModelLists.provinceId)) &&
        (province == null ? offlineStoreAddressListModelLists.province == null : province.equals(offlineStoreAddressListModelLists.province)) &&
        (cityId == null ? offlineStoreAddressListModelLists.cityId == null : cityId.equals(offlineStoreAddressListModelLists.cityId)) &&
        (city == null ? offlineStoreAddressListModelLists.city == null : city.equals(offlineStoreAddressListModelLists.city)) &&
        (address == null ? offlineStoreAddressListModelLists.address == null : address.equals(offlineStoreAddressListModelLists.address)) &&
        (longitude == null ? offlineStoreAddressListModelLists.longitude == null : longitude.equals(offlineStoreAddressListModelLists.longitude)) &&
        (latitude == null ? offlineStoreAddressListModelLists.latitude == null : latitude.equals(offlineStoreAddressListModelLists.latitude));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (id == null ? 0: id.hashCode());
    result = 31 * result + (storeId == null ? 0: storeId.hashCode());
    result = 31 * result + (countryId == null ? 0: countryId.hashCode());
    result = 31 * result + (provinceId == null ? 0: provinceId.hashCode());
    result = 31 * result + (province == null ? 0: province.hashCode());
    result = 31 * result + (cityId == null ? 0: cityId.hashCode());
    result = 31 * result + (city == null ? 0: city.hashCode());
    result = 31 * result + (address == null ? 0: address.hashCode());
    result = 31 * result + (longitude == null ? 0: longitude.hashCode());
    result = 31 * result + (latitude == null ? 0: latitude.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class OfflineStoreAddressListModelLists {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  storeId: ").append(storeId).append("\n");
    sb.append("  countryId: ").append(countryId).append("\n");
    sb.append("  provinceId: ").append(provinceId).append("\n");
    sb.append("  province: ").append(province).append("\n");
    sb.append("  cityId: ").append(cityId).append("\n");
    sb.append("  city: ").append(city).append("\n");
    sb.append("  address: ").append(address).append("\n");
    sb.append("  longitude: ").append(longitude).append("\n");
    sb.append("  latitude: ").append(latitude).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
