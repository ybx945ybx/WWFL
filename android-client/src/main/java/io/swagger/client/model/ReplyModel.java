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

import io.swagger.client.model.ReplyModelQuotation;
import java.util.*;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class ReplyModel  {
  
  @SerializedName("tid")
  private String tid = null;
  @SerializedName("pid")
  private String pid = null;
  @SerializedName("author_id")
  private String authorId = null;
  @SerializedName("author_name")
  private String authorName = null;
  @SerializedName("author_avatar")
  private String authorAvatar = null;
  @SerializedName("content")
  private String content = null;
  @SerializedName("floor_num")
  private String floorNum = null;
  @SerializedName("praise_count")
  private String praiseCount = null;
  @SerializedName("post_time")
  private String postTime = null;
  @SerializedName("pics")
  private List<String> pics = null;
  @SerializedName("is_praised")
  private String isPraised = null;
  @SerializedName("is_targeted")
  private String isTargeted = null;
  @SerializedName("device")
  private String device = null;
  @SerializedName("quotation")
  private ReplyModelQuotation quotation = null;

  /**
   * 帖子ID
   **/
  @ApiModelProperty(value = "帖子ID")
  public String getTid() {
    return tid;
  }
  public void setTid(String tid) {
    this.tid = tid;
  }

  /**
   * 本回复ID
   **/
  @ApiModelProperty(value = "本回复ID")
  public String getPid() {
    return pid;
  }
  public void setPid(String pid) {
    this.pid = pid;
  }

  /**
   * 作者ID
   **/
  @ApiModelProperty(value = "作者ID")
  public String getAuthorId() {
    return authorId;
  }
  public void setAuthorId(String authorId) {
    this.authorId = authorId;
  }

  /**
   * 作者名字
   **/
  @ApiModelProperty(value = "作者名字")
  public String getAuthorName() {
    return authorName;
  }
  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }

  /**
   * 作者头像(url)
   **/
  @ApiModelProperty(value = "作者头像(url)")
  public String getAuthorAvatar() {
    return authorAvatar;
  }
  public void setAuthorAvatar(String authorAvatar) {
    this.authorAvatar = authorAvatar;
  }

  /**
   * 回复内容
   **/
  @ApiModelProperty(value = "回复内容")
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * 楼层编号
   **/
  @ApiModelProperty(value = "楼层编号")
  public String getFloorNum() {
    return floorNum;
  }
  public void setFloorNum(String floorNum) {
    this.floorNum = floorNum;
  }

  /**
   * 点赞数
   **/
  @ApiModelProperty(value = "点赞数")
  public String getPraiseCount() {
    return praiseCount;
  }
  public void setPraiseCount(String praiseCount) {
    this.praiseCount = praiseCount;
  }

  /**
   * 回复时间
   **/
  @ApiModelProperty(value = "回复时间")
  public String getPostTime() {
    return postTime;
  }
  public void setPostTime(String postTime) {
    this.postTime = postTime;
  }

  /**
   * 帖子图片
   **/
  @ApiModelProperty(value = "帖子图片")
  public List<String> getPics() {
    return pics;
  }
  public void setPics(List<String> pics) {
    this.pics = pics;
  }

  /**
   * 是否已赞
   **/
  @ApiModelProperty(value = "是否已赞")
  public String getIsPraised() {
    return isPraised;
  }
  public void setIsPraised(String isPraised) {
    this.isPraised = isPraised;
  }

  /**
   * 是否回复的是某一楼层
   **/
  @ApiModelProperty(value = "是否回复的是某一楼层")
  public String getIsTargeted() {
    return isTargeted;
  }
  public void setIsTargeted(String isTargeted) {
    this.isTargeted = isTargeted;
  }

  /**
   * 用户设备名称
   **/
  @ApiModelProperty(value = "用户设备名称")
  public String getDevice() {
    return device;
  }
  public void setDevice(String device) {
    this.device = device;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public ReplyModelQuotation getQuotation() {
    return quotation;
  }
  public void setQuotation(ReplyModelQuotation quotation) {
    this.quotation = quotation;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReplyModel replyModel = (ReplyModel) o;
    return (tid == null ? replyModel.tid == null : tid.equals(replyModel.tid)) &&
        (pid == null ? replyModel.pid == null : pid.equals(replyModel.pid)) &&
        (authorId == null ? replyModel.authorId == null : authorId.equals(replyModel.authorId)) &&
        (authorName == null ? replyModel.authorName == null : authorName.equals(replyModel.authorName)) &&
        (authorAvatar == null ? replyModel.authorAvatar == null : authorAvatar.equals(replyModel.authorAvatar)) &&
        (content == null ? replyModel.content == null : content.equals(replyModel.content)) &&
        (floorNum == null ? replyModel.floorNum == null : floorNum.equals(replyModel.floorNum)) &&
        (praiseCount == null ? replyModel.praiseCount == null : praiseCount.equals(replyModel.praiseCount)) &&
        (postTime == null ? replyModel.postTime == null : postTime.equals(replyModel.postTime)) &&
        (pics == null ? replyModel.pics == null : pics.equals(replyModel.pics)) &&
        (isPraised == null ? replyModel.isPraised == null : isPraised.equals(replyModel.isPraised)) &&
        (isTargeted == null ? replyModel.isTargeted == null : isTargeted.equals(replyModel.isTargeted)) &&
        (device == null ? replyModel.device == null : device.equals(replyModel.device)) &&
        (quotation == null ? replyModel.quotation == null : quotation.equals(replyModel.quotation));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (tid == null ? 0: tid.hashCode());
    result = 31 * result + (pid == null ? 0: pid.hashCode());
    result = 31 * result + (authorId == null ? 0: authorId.hashCode());
    result = 31 * result + (authorName == null ? 0: authorName.hashCode());
    result = 31 * result + (authorAvatar == null ? 0: authorAvatar.hashCode());
    result = 31 * result + (content == null ? 0: content.hashCode());
    result = 31 * result + (floorNum == null ? 0: floorNum.hashCode());
    result = 31 * result + (praiseCount == null ? 0: praiseCount.hashCode());
    result = 31 * result + (postTime == null ? 0: postTime.hashCode());
    result = 31 * result + (pics == null ? 0: pics.hashCode());
    result = 31 * result + (isPraised == null ? 0: isPraised.hashCode());
    result = 31 * result + (isTargeted == null ? 0: isTargeted.hashCode());
    result = 31 * result + (device == null ? 0: device.hashCode());
    result = 31 * result + (quotation == null ? 0: quotation.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReplyModel {\n");
    
    sb.append("  tid: ").append(tid).append("\n");
    sb.append("  pid: ").append(pid).append("\n");
    sb.append("  authorId: ").append(authorId).append("\n");
    sb.append("  authorName: ").append(authorName).append("\n");
    sb.append("  authorAvatar: ").append(authorAvatar).append("\n");
    sb.append("  content: ").append(content).append("\n");
    sb.append("  floorNum: ").append(floorNum).append("\n");
    sb.append("  praiseCount: ").append(praiseCount).append("\n");
    sb.append("  postTime: ").append(postTime).append("\n");
    sb.append("  pics: ").append(pics).append("\n");
    sb.append("  isPraised: ").append(isPraised).append("\n");
    sb.append("  isTargeted: ").append(isTargeted).append("\n");
    sb.append("  device: ").append(device).append("\n");
    sb.append("  quotation: ").append(quotation).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
