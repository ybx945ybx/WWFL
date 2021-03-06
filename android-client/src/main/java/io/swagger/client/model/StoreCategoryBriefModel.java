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
public class StoreCategoryBriefModel  {
  
  @SerializedName("cate_id")
  private String cateId = null;
  @SerializedName("cate_name")
  private String cateName = null;

  /**
   * 商家分类ID
   **/
  @ApiModelProperty(value = "商家分类ID")
  public String getCateId() {
    return cateId;
  }
  public void setCateId(String cateId) {
    this.cateId = cateId;
  }

  /**
   * 商家分类名称
   **/
  @ApiModelProperty(value = "商家分类名称")
  public String getCateName() {
    return cateName;
  }
  public void setCateName(String cateName) {
    this.cateName = cateName;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StoreCategoryBriefModel storeCategoryBriefModel = (StoreCategoryBriefModel) o;
    return (cateId == null ? storeCategoryBriefModel.cateId == null : cateId.equals(storeCategoryBriefModel.cateId)) &&
        (cateName == null ? storeCategoryBriefModel.cateName == null : cateName.equals(storeCategoryBriefModel.cateName));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (cateId == null ? 0: cateId.hashCode());
    result = 31 * result + (cateName == null ? 0: cateName.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class StoreCategoryBriefModel {\n");
    
    sb.append("  cateId: ").append(cateId).append("\n");
    sb.append("  cateName: ").append(cateName).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
