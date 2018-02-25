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


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "")
public class SlidePicModel extends DealSlidePicBaseModel implements Serializable {
  
  @SerializedName("id")
  private String id = null;
  @SerializedName("title")
  private String title = null;
  @SerializedName("pic")
  private String pic = null;
  @SerializedName("is_commercial")
  private String isCommercial = null;
  @SerializedName("type")
  private String type = null;
  @SerializedName("link_data")
  private String linkData = null;

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
   * 图片地址
   **/
  @ApiModelProperty(value = "图片地址")
  public String getPic() {
    return pic;
  }
  public void setPic(String pic) {
    this.pic = pic;
  }

  /**
   * 是否是商业广告 - 0：否 1：是
   **/
  @ApiModelProperty(value = "是否是商业广告 - 0：否 1：是")
  public String getIsCommercial() {
    return isCommercial;
  }
  public void setIsCommercial(String isCommercial) {
    this.isCommercial = isCommercial;
  }

  /**
   * d：优惠跳转, s：商家跳转, b：帖子详情, w：H5, sp：保留的老版本类型, l：邀请好友-未登录时跳转注册, trans：转运, section：版块, user：用户详情, tag：标签详情, exchange：兑换详情, trial：试用详情, invite：邀请好友, appstore：应用商店, outapp：外部浏览器打开，sign:每日签到, store_list：返利商家列表，trans_list:转运商家列表，dealDaily_list：优惠日报列表
   **/
  @ApiModelProperty(value = "d：优惠跳转, s：商家跳转, b：帖子详情, w：H5, sp：保留的老版本类型, l：邀请好友-未登录时跳转注册, trans：转运, section：版块, user：用户详情, tag：标签详情, exchange：兑换详情, trial：试用详情, invite：邀请好友, appstore：应用商店, outapp：外部浏览器打开，sign:每日签到, store_list：返利商家列表，trans_list:转运商家列表，dealDaily_list：优惠日报列表")
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }

  /**
   * 链接内容
   **/
  @ApiModelProperty(value = "链接内容")
  public String getLinkData() {
    return linkData;
  }
  public void setLinkData(String linkData) {
    this.linkData = linkData;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SlidePicModel slidePicModel = (SlidePicModel) o;
    return (id == null ? slidePicModel.id == null : id.equals(slidePicModel.id)) &&
        (title == null ? slidePicModel.title == null : title.equals(slidePicModel.title)) &&
        (pic == null ? slidePicModel.pic == null : pic.equals(slidePicModel.pic)) &&
        (isCommercial == null ? slidePicModel.isCommercial == null : isCommercial.equals(slidePicModel.isCommercial)) &&
        (type == null ? slidePicModel.type == null : type.equals(slidePicModel.type)) &&
        (linkData == null ? slidePicModel.linkData == null : linkData.equals(slidePicModel.linkData));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (id == null ? 0: id.hashCode());
    result = 31 * result + (title == null ? 0: title.hashCode());
    result = 31 * result + (pic == null ? 0: pic.hashCode());
    result = 31 * result + (isCommercial == null ? 0: isCommercial.hashCode());
    result = 31 * result + (type == null ? 0: type.hashCode());
    result = 31 * result + (linkData == null ? 0: linkData.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class SlidePicModel {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  pic: ").append(pic).append("\n");
    sb.append("  isCommercial: ").append(isCommercial).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  linkData: ").append(linkData).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}