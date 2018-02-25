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
public class TopicModelTags  {
  
  @SerializedName("tag_id")
  private String tagId = null;
  @SerializedName("tag_name")
  private String tagName = null;
  @SerializedName("is_hot")
  private String isHot = null;
  @SerializedName("pic")
  private String pic = null;

  /**
   * 标签ID
   **/
  @ApiModelProperty(value = "标签ID")
  public String getTagId() {
    return tagId;
  }
  public void setTagId(String tagId) {
    this.tagId = tagId;
  }

  /**
   * 标签名称
   **/
  @ApiModelProperty(value = "标签名称")
  public String getTagName() {
    return tagName;
  }
  public void setTagName(String tagName) {
    this.tagName = tagName;
  }

  /**
   * 是否热门
   **/
  @ApiModelProperty(value = "是否热门")
  public String getIsHot() {
    return isHot;
  }
  public void setIsHot(String isHot) {
    this.isHot = isHot;
  }

  /**
   * 标签图片
   **/
  @ApiModelProperty(value = "标签图片")
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
    TopicModelTags topicModelTags = (TopicModelTags) o;
    return (tagId == null ? topicModelTags.tagId == null : tagId.equals(topicModelTags.tagId)) &&
        (tagName == null ? topicModelTags.tagName == null : tagName.equals(topicModelTags.tagName)) &&
        (isHot == null ? topicModelTags.isHot == null : isHot.equals(topicModelTags.isHot)) &&
        (pic == null ? topicModelTags.pic == null : pic.equals(topicModelTags.pic));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (tagId == null ? 0: tagId.hashCode());
    result = 31 * result + (tagName == null ? 0: tagName.hashCode());
    result = 31 * result + (isHot == null ? 0: isHot.hashCode());
    result = 31 * result + (pic == null ? 0: pic.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class TopicModelTags {\n");
    
    sb.append("  tagId: ").append(tagId).append("\n");
    sb.append("  tagName: ").append(tagName).append("\n");
    sb.append("  isHot: ").append(isHot).append("\n");
    sb.append("  pic: ").append(pic).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
