/**
 * 五五海淘返利APP新版接口
 * 更新日志<br> 相对于上一build的变更： <br/> APP旧接口迭代 
 *
 * OpenAPI spec version: 1.6 build20171129-3
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

import io.swagger.client.model.StoreBriefModel;
import java.util.*;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class StoreIndexIfModelDataStoresSections  {
  
  @SerializedName("letter")
  private String letter = null;
  @SerializedName("stores")
  private List<StoreBriefModel> stores = null;

  /**
   * 索引字母
   **/
  @ApiModelProperty(value = "索引字母")
  public String getLetter() {
    return letter;
  }
  public void setLetter(String letter) {
    this.letter = letter;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<StoreBriefModel> getStores() {
    return stores;
  }
  public void setStores(List<StoreBriefModel> stores) {
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
    StoreIndexIfModelDataStoresSections storeIndexIfModelDataStoresSections = (StoreIndexIfModelDataStoresSections) o;
    return (letter == null ? storeIndexIfModelDataStoresSections.letter == null : letter.equals(storeIndexIfModelDataStoresSections.letter)) &&
        (stores == null ? storeIndexIfModelDataStoresSections.stores == null : stores.equals(storeIndexIfModelDataStoresSections.stores));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (letter == null ? 0: letter.hashCode());
    result = 31 * result + (stores == null ? 0: stores.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class StoreIndexIfModelDataStoresSections {\n");
    
    sb.append("  letter: ").append(letter).append("\n");
    sb.append("  stores: ").append(stores).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
