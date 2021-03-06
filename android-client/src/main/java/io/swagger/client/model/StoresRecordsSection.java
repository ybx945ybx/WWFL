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

import io.swagger.client.model.VisitedStoreRecordModel;
import java.util.*;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class StoresRecordsSection  {
  
  @SerializedName("section_title")
  private String sectionTitle = null;
  @SerializedName("stores_records")
  private List<VisitedStoreRecordModel> storesRecords = null;

  /**
   * 区块名称
   **/
  @ApiModelProperty(value = "区块名称")
  public String getSectionTitle() {
    return sectionTitle;
  }
  public void setSectionTitle(String sectionTitle) {
    this.sectionTitle = sectionTitle;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<VisitedStoreRecordModel> getStoresRecords() {
    return storesRecords;
  }
  public void setStoresRecords(List<VisitedStoreRecordModel> storesRecords) {
    this.storesRecords = storesRecords;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StoresRecordsSection storesRecordsSection = (StoresRecordsSection) o;
    return (sectionTitle == null ? storesRecordsSection.sectionTitle == null : sectionTitle.equals(storesRecordsSection.sectionTitle)) &&
        (storesRecords == null ? storesRecordsSection.storesRecords == null : storesRecords.equals(storesRecordsSection.storesRecords));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (sectionTitle == null ? 0: sectionTitle.hashCode());
    result = 31 * result + (storesRecords == null ? 0: storesRecords.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class StoresRecordsSection {\n");
    
    sb.append("  sectionTitle: ").append(sectionTitle).append("\n");
    sb.append("  storesRecords: ").append(storesRecords).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
