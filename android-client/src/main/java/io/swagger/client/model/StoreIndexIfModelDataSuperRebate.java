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
public class StoreIndexIfModelDataSuperRebate  {
  
  @SerializedName("id")
  private String id = null;
  @SerializedName("title")
  private String title = null;
  @SerializedName("rebate_view")
  private String rebateView = null;
  @SerializedName("pic")
  private String pic = null;

  /**
   * id
   **/
  @ApiModelProperty(value = "id")
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  /**
   * 标题
   **/
  @ApiModelProperty(value = "标题")
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * 返利额度（文字说明）
   **/
  @ApiModelProperty(value = "返利额度（文字说明）")
  public String getRebateView() {
    return rebateView;
  }
  public void setRebateView(String rebateView) {
    this.rebateView = rebateView;
  }

  /**
   * 图片地址
   **/
  @ApiModelProperty(value = "图片地址")
  public String getPic() {
    return pic;
  }
  public void setPic(String pic) {
    this.pic = pic;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StoreIndexIfModelDataSuperRebate storeIndexIfModelDataSuperRebate = (StoreIndexIfModelDataSuperRebate) o;
    return (id == null ? storeIndexIfModelDataSuperRebate.id == null : id.equals(storeIndexIfModelDataSuperRebate.id)) &&
        (title == null ? storeIndexIfModelDataSuperRebate.title == null : title.equals(storeIndexIfModelDataSuperRebate.title)) &&
        (rebateView == null ? storeIndexIfModelDataSuperRebate.rebateView == null : rebateView.equals(storeIndexIfModelDataSuperRebate.rebateView)) &&
        (pic == null ? storeIndexIfModelDataSuperRebate.pic == null : pic.equals(storeIndexIfModelDataSuperRebate.pic));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (id == null ? 0: id.hashCode());
    result = 31 * result + (title == null ? 0: title.hashCode());
    result = 31 * result + (rebateView == null ? 0: rebateView.hashCode());
    result = 31 * result + (pic == null ? 0: pic.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class StoreIndexIfModelDataSuperRebate {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  rebateView: ").append(rebateView).append("\n");
    sb.append("  pic: ").append(pic).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
