/**
 * 五五海淘返利APP新版接口
 * 更新日志<br> 相对于上一build的变更： <br/> APP5.4接口 
 *
 * OpenAPI spec version: 1.3 build20170802-1
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

import io.swagger.client.model.ShoppedStoresSectionsListIfModelDataStores;
import java.util.*;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class ShoppedStoresSectionsListIfModelDataSections  {
  
  @SerializedName("section_name")
  private String sectionName = null;
  @SerializedName("stores")
  private List<ShoppedStoresSectionsListIfModelDataStores> stores = null;

  /**
   * 区块名称
   **/
  @ApiModelProperty(value = "区块名称")
  public String getSectionName() {
    return sectionName;
  }
  public void setSectionName(String sectionName) {
    this.sectionName = sectionName;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<ShoppedStoresSectionsListIfModelDataStores> getStores() {
    return stores;
  }
  public void setStores(List<ShoppedStoresSectionsListIfModelDataStores> stores) {
    this.stores = stores;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShoppedStoresSectionsListIfModelDataSections shoppedStoresSectionsListIfModelDataSections = (ShoppedStoresSectionsListIfModelDataSections) o;
    return (sectionName == null ? shoppedStoresSectionsListIfModelDataSections.sectionName == null : sectionName.equals(shoppedStoresSectionsListIfModelDataSections.sectionName)) &&
        (stores == null ? shoppedStoresSectionsListIfModelDataSections.stores == null : stores.equals(shoppedStoresSectionsListIfModelDataSections.stores));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (sectionName == null ? 0: sectionName.hashCode());
    result = 31 * result + (stores == null ? 0: stores.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShoppedStoresSectionsListIfModelDataSections {\n");
    
    sb.append("  sectionName: ").append(sectionName).append("\n");
    sb.append("  stores: ").append(stores).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}