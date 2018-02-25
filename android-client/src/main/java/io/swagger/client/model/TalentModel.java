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


/**
 * 达人
 **/
@ApiModel(description = "达人")
public class TalentModel  {
  
  @SerializedName("uid")
  private String uid = null;
  @SerializedName("username")
  private String username = null;
  @SerializedName("title")
  private String title = null;
  @SerializedName("avatar")
  private String avatar = null;
  @SerializedName("category")
  private String category = null;
  @SerializedName("interview_tid")
  private String interviewTid = null;
  @SerializedName("pic")
  private String pic = null;
  @SerializedName("posts_count")
  private String postsCount = null;
  @SerializedName("topics_count")
  private String topicsCount = null;
  @SerializedName("digest_topics_count")
  private String digestTopicsCount = null;
  @SerializedName("gold")
  private String gold = null;

  /**
   * 用户ID
   **/
  @ApiModelProperty(value = "用户ID")
  public String getUid() {
    return uid;
  }
  public void setUid(String uid) {
    this.uid = uid;
  }

  /**
   * 用户名
   **/
  @ApiModelProperty(value = "用户名")
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * 达人头衔
   **/
  @ApiModelProperty(value = "达人头衔")
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * 头像
   **/
  @ApiModelProperty(value = "头像")
  public String getAvatar() {
    return avatar;
  }
  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  /**
   * 达人类别
   **/
  @ApiModelProperty(value = "达人类别")
  public String getCategory() {
    return category;
  }
  public void setCategory(String category) {
    this.category = category;
  }

  /**
   * 采访帖ID
   **/
  @ApiModelProperty(value = "采访帖ID")
  public String getInterviewTid() {
    return interviewTid;
  }
  public void setInterviewTid(String interviewTid) {
    this.interviewTid = interviewTid;
  }

  /**
   * 达人封面图片
   **/
  @ApiModelProperty(value = "达人封面图片")
  public String getPic() {
    return pic;
  }
  public void setPic(String pic) {
    this.pic = pic;
  }

  /**
   * 回复数
   **/
  @ApiModelProperty(value = "回复数")
  public String getPostsCount() {
    return postsCount;
  }
  public void setPostsCount(String postsCount) {
    this.postsCount = postsCount;
  }

  /**
   * 主题数
   **/
  @ApiModelProperty(value = "主题数")
  public String getTopicsCount() {
    return topicsCount;
  }
  public void setTopicsCount(String topicsCount) {
    this.topicsCount = topicsCount;
  }

  /**
   * 精华主题数
   **/
  @ApiModelProperty(value = "精华主题数")
  public String getDigestTopicsCount() {
    return digestTopicsCount;
  }
  public void setDigestTopicsCount(String digestTopicsCount) {
    this.digestTopicsCount = digestTopicsCount;
  }

  /**
   * 金币数
   **/
  @ApiModelProperty(value = "金币数")
  public String getGold() {
    return gold;
  }
  public void setGold(String gold) {
    this.gold = gold;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TalentModel talentModel = (TalentModel) o;
    return (uid == null ? talentModel.uid == null : uid.equals(talentModel.uid)) &&
        (username == null ? talentModel.username == null : username.equals(talentModel.username)) &&
        (title == null ? talentModel.title == null : title.equals(talentModel.title)) &&
        (avatar == null ? talentModel.avatar == null : avatar.equals(talentModel.avatar)) &&
        (category == null ? talentModel.category == null : category.equals(talentModel.category)) &&
        (interviewTid == null ? talentModel.interviewTid == null : interviewTid.equals(talentModel.interviewTid)) &&
        (pic == null ? talentModel.pic == null : pic.equals(talentModel.pic)) &&
        (postsCount == null ? talentModel.postsCount == null : postsCount.equals(talentModel.postsCount)) &&
        (topicsCount == null ? talentModel.topicsCount == null : topicsCount.equals(talentModel.topicsCount)) &&
        (digestTopicsCount == null ? talentModel.digestTopicsCount == null : digestTopicsCount.equals(talentModel.digestTopicsCount)) &&
        (gold == null ? talentModel.gold == null : gold.equals(talentModel.gold));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (uid == null ? 0: uid.hashCode());
    result = 31 * result + (username == null ? 0: username.hashCode());
    result = 31 * result + (title == null ? 0: title.hashCode());
    result = 31 * result + (avatar == null ? 0: avatar.hashCode());
    result = 31 * result + (category == null ? 0: category.hashCode());
    result = 31 * result + (interviewTid == null ? 0: interviewTid.hashCode());
    result = 31 * result + (pic == null ? 0: pic.hashCode());
    result = 31 * result + (postsCount == null ? 0: postsCount.hashCode());
    result = 31 * result + (topicsCount == null ? 0: topicsCount.hashCode());
    result = 31 * result + (digestTopicsCount == null ? 0: digestTopicsCount.hashCode());
    result = 31 * result + (gold == null ? 0: gold.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class TalentModel {\n");
    
    sb.append("  uid: ").append(uid).append("\n");
    sb.append("  username: ").append(username).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  avatar: ").append(avatar).append("\n");
    sb.append("  category: ").append(category).append("\n");
    sb.append("  interviewTid: ").append(interviewTid).append("\n");
    sb.append("  pic: ").append(pic).append("\n");
    sb.append("  postsCount: ").append(postsCount).append("\n");
    sb.append("  topicsCount: ").append(topicsCount).append("\n");
    sb.append("  digestTopicsCount: ").append(digestTopicsCount).append("\n");
    sb.append("  gold: ").append(gold).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
