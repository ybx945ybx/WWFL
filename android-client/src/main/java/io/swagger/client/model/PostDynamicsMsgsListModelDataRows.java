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

import io.swagger.client.model.PostDynamicsMsgsListModelDataQuotation;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class PostDynamicsMsgsListModelDataRows  {
  
  @SerializedName("type")
  private String type = null;
  @SerializedName("sender_uid")
  private String senderUid = null;
  @SerializedName("sender_name")
  private String senderName = null;
  @SerializedName("sender_avatar")
  private String senderAvatar = null;
  @SerializedName("tid")
  private String tid = null;
  @SerializedName("pid")
  private String pid = null;
  @SerializedName("floor_num")
  private String floorNum = null;
  @SerializedName("content")
  private String content = null;
  @SerializedName("quotation")
  private PostDynamicsMsgsListModelDataQuotation quotation = null;
  @SerializedName("sent_time")
  private String sentTime = null;

  /**
   * 类型 - 1：主题帖被回复 2：普通帖被回复
   **/
  @ApiModelProperty(value = "类型 - 1：主题帖被回复 2：普通帖被回复")
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }

  /**
   * 发送者ID
   **/
  @ApiModelProperty(value = "发送者ID")
  public String getSenderUid() {
    return senderUid;
  }
  public void setSenderUid(String senderUid) {
    this.senderUid = senderUid;
  }

  /**
   * 发送者名字
   **/
  @ApiModelProperty(value = "发送者名字")
  public String getSenderName() {
    return senderName;
  }
  public void setSenderName(String senderName) {
    this.senderName = senderName;
  }

  /**
   * 发送者头像
   **/
  @ApiModelProperty(value = "发送者头像")
  public String getSenderAvatar() {
    return senderAvatar;
  }
  public void setSenderAvatar(String senderAvatar) {
    this.senderAvatar = senderAvatar;
  }

  /**
   * 主题ID
   **/
  @ApiModelProperty(value = "主题ID")
  public String getTid() {
    return tid;
  }
  public void setTid(String tid) {
    this.tid = tid;
  }

  /**
   * 回复ID
   **/
  @ApiModelProperty(value = "回复ID")
  public String getPid() {
    return pid;
  }
  public void setPid(String pid) {
    this.pid = pid;
  }

  /**
   * 回复楼层编号
   **/
  @ApiModelProperty(value = "回复楼层编号")
  public String getFloorNum() {
    return floorNum;
  }
  public void setFloorNum(String floorNum) {
    this.floorNum = floorNum;
  }

  /**
   * 消息内容
   **/
  @ApiModelProperty(value = "消息内容")
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public PostDynamicsMsgsListModelDataQuotation getQuotation() {
    return quotation;
  }
  public void setQuotation(PostDynamicsMsgsListModelDataQuotation quotation) {
    this.quotation = quotation;
  }

  /**
   * 发送时间
   **/
  @ApiModelProperty(value = "发送时间")
  public String getSentTime() {
    return sentTime;
  }
  public void setSentTime(String sentTime) {
    this.sentTime = sentTime;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostDynamicsMsgsListModelDataRows postDynamicsMsgsListModelDataRows = (PostDynamicsMsgsListModelDataRows) o;
    return (type == null ? postDynamicsMsgsListModelDataRows.type == null : type.equals(postDynamicsMsgsListModelDataRows.type)) &&
        (senderUid == null ? postDynamicsMsgsListModelDataRows.senderUid == null : senderUid.equals(postDynamicsMsgsListModelDataRows.senderUid)) &&
        (senderName == null ? postDynamicsMsgsListModelDataRows.senderName == null : senderName.equals(postDynamicsMsgsListModelDataRows.senderName)) &&
        (senderAvatar == null ? postDynamicsMsgsListModelDataRows.senderAvatar == null : senderAvatar.equals(postDynamicsMsgsListModelDataRows.senderAvatar)) &&
        (tid == null ? postDynamicsMsgsListModelDataRows.tid == null : tid.equals(postDynamicsMsgsListModelDataRows.tid)) &&
        (pid == null ? postDynamicsMsgsListModelDataRows.pid == null : pid.equals(postDynamicsMsgsListModelDataRows.pid)) &&
        (floorNum == null ? postDynamicsMsgsListModelDataRows.floorNum == null : floorNum.equals(postDynamicsMsgsListModelDataRows.floorNum)) &&
        (content == null ? postDynamicsMsgsListModelDataRows.content == null : content.equals(postDynamicsMsgsListModelDataRows.content)) &&
        (quotation == null ? postDynamicsMsgsListModelDataRows.quotation == null : quotation.equals(postDynamicsMsgsListModelDataRows.quotation)) &&
        (sentTime == null ? postDynamicsMsgsListModelDataRows.sentTime == null : sentTime.equals(postDynamicsMsgsListModelDataRows.sentTime));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (type == null ? 0: type.hashCode());
    result = 31 * result + (senderUid == null ? 0: senderUid.hashCode());
    result = 31 * result + (senderName == null ? 0: senderName.hashCode());
    result = 31 * result + (senderAvatar == null ? 0: senderAvatar.hashCode());
    result = 31 * result + (tid == null ? 0: tid.hashCode());
    result = 31 * result + (pid == null ? 0: pid.hashCode());
    result = 31 * result + (floorNum == null ? 0: floorNum.hashCode());
    result = 31 * result + (content == null ? 0: content.hashCode());
    result = 31 * result + (quotation == null ? 0: quotation.hashCode());
    result = 31 * result + (sentTime == null ? 0: sentTime.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class PostDynamicsMsgsListModelDataRows {\n");
    
    sb.append("  type: ").append(type).append("\n");
    sb.append("  senderUid: ").append(senderUid).append("\n");
    sb.append("  senderName: ").append(senderName).append("\n");
    sb.append("  senderAvatar: ").append(senderAvatar).append("\n");
    sb.append("  tid: ").append(tid).append("\n");
    sb.append("  pid: ").append(pid).append("\n");
    sb.append("  floorNum: ").append(floorNum).append("\n");
    sb.append("  content: ").append(content).append("\n");
    sb.append("  quotation: ").append(quotation).append("\n");
    sb.append("  sentTime: ").append(sentTime).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
