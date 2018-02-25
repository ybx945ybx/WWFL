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
public class In24hDealsListModelDataCategories  {
  
  @SerializedName("category_id")
  private String categoryId = null;
  @SerializedName("category_name")
  private String categoryName = null;
  @SerializedName("is_checked")
  private String isChecked = null;

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
   * 是否被选中  0：未选中 1：选中
   **/
  @ApiModelProperty(value = "是否被选中  0：未选中 1：选中")
  public String getIsChecked() {
    return isChecked;
  }
  public void setIsChecked(String isChecked) {
    this.isChecked = isChecked;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    In24hDealsListModelDataCategories in24hDealsListModelDataCategories = (In24hDealsListModelDataCategories) o;
    return (categoryId == null ? in24hDealsListModelDataCategories.categoryId == null : categoryId.equals(in24hDealsListModelDataCategories.categoryId)) &&
        (categoryName == null ? in24hDealsListModelDataCategories.categoryName == null : categoryName.equals(in24hDealsListModelDataCategories.categoryName)) &&
        (isChecked == null ? in24hDealsListModelDataCategories.isChecked == null : isChecked.equals(in24hDealsListModelDataCategories.isChecked));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (categoryId == null ? 0: categoryId.hashCode());
    result = 31 * result + (categoryName == null ? 0: categoryName.hashCode());
    result = 31 * result + (isChecked == null ? 0: isChecked.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class In24hDealsListModelDataCategories {\n");
    
    sb.append("  categoryId: ").append(categoryId).append("\n");
    sb.append("  categoryName: ").append(categoryName).append("\n");
    sb.append("  isChecked: ").append(isChecked).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}