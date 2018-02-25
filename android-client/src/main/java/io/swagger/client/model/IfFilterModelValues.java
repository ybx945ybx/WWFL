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
public class IfFilterModelValues  {
  
  @SerializedName("is_all")
  private String isAll = null;
  @SerializedName("value")
  private String value = null;
  @SerializedName("text")
  private String text = null;

  /**
   * 是否是条件“全部”(取消该ID名的过滤条件) 0：否 1：是
   **/
  @ApiModelProperty(value = "是否是条件“全部”(取消该ID名的过滤条件) 0：否 1：是")
  public String getIsAll() {
    return isAll;
  }
  public void setIsAll(String isAll) {
    this.isAll = isAll;
  }

  /**
   * 参数值
   **/
  @ApiModelProperty(value = "参数值")
  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * 参数的显示元素的文案
   **/
  @ApiModelProperty(value = "参数的显示元素的文案")
  public String getText() {
    return text;
  }
  public void setText(String text) {
    this.text = text;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IfFilterModelValues ifFilterModelValues = (IfFilterModelValues) o;
    return (isAll == null ? ifFilterModelValues.isAll == null : isAll.equals(ifFilterModelValues.isAll)) &&
        (value == null ? ifFilterModelValues.value == null : value.equals(ifFilterModelValues.value)) &&
        (text == null ? ifFilterModelValues.text == null : text.equals(ifFilterModelValues.text));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (isAll == null ? 0: isAll.hashCode());
    result = 31 * result + (value == null ? 0: value.hashCode());
    result = 31 * result + (text == null ? 0: text.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class IfFilterModelValues {\n");
    
    sb.append("  isAll: ").append(isAll).append("\n");
    sb.append("  value: ").append(value).append("\n");
    sb.append("  text: ").append(text).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
