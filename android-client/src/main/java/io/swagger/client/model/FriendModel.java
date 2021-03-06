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
public class FriendModel  {
  
  @SerializedName("friend_uid")
  private String friendUid = null;
  @SerializedName("friend_name")
  private String friendName = null;
  @SerializedName("friend_avatar")
  private String friendAvatar = null;
  @SerializedName("friend_sex")
  private String friendSex = null;
  @SerializedName("friend_group_name")
  private String friendGroupName = null;
  @SerializedName("friend_level")
  private String friendLevel = null;

  /**
   * 好友UID
   **/
  @ApiModelProperty(value = "好友UID")
  public String getFriendUid() {
    return friendUid;
  }
  public void setFriendUid(String friendUid) {
    this.friendUid = friendUid;
  }

  /**
   * 好友昵称
   **/
  @ApiModelProperty(value = "好友昵称")
  public String getFriendName() {
    return friendName;
  }
  public void setFriendName(String friendName) {
    this.friendName = friendName;
  }

  /**
   * 好友头像
   **/
  @ApiModelProperty(value = "好友头像")
  public String getFriendAvatar() {
    return friendAvatar;
  }
  public void setFriendAvatar(String friendAvatar) {
    this.friendAvatar = friendAvatar;
  }

  /**
   * 好友性别 - 0：保密，不显示 1：男 2：女
   **/
  @ApiModelProperty(value = "好友性别 - 0：保密，不显示 1：男 2：女")
  public String getFriendSex() {
    return friendSex;
  }
  public void setFriendSex(String friendSex) {
    this.friendSex = friendSex;
  }

  /**
   * 好友用户组（头衔）
   **/
  @ApiModelProperty(value = "好友用户组（头衔）")
  public String getFriendGroupName() {
    return friendGroupName;
  }
  public void setFriendGroupName(String friendGroupName) {
    this.friendGroupName = friendGroupName;
  }

  /**
   * 好友级别（数值）
   **/
  @ApiModelProperty(value = "好友级别（数值）")
  public String getFriendLevel() {
    return friendLevel;
  }
  public void setFriendLevel(String friendLevel) {
    this.friendLevel = friendLevel;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FriendModel friendModel = (FriendModel) o;
    return (friendUid == null ? friendModel.friendUid == null : friendUid.equals(friendModel.friendUid)) &&
        (friendName == null ? friendModel.friendName == null : friendName.equals(friendModel.friendName)) &&
        (friendAvatar == null ? friendModel.friendAvatar == null : friendAvatar.equals(friendModel.friendAvatar)) &&
        (friendSex == null ? friendModel.friendSex == null : friendSex.equals(friendModel.friendSex)) &&
        (friendGroupName == null ? friendModel.friendGroupName == null : friendGroupName.equals(friendModel.friendGroupName)) &&
        (friendLevel == null ? friendModel.friendLevel == null : friendLevel.equals(friendModel.friendLevel));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (friendUid == null ? 0: friendUid.hashCode());
    result = 31 * result + (friendName == null ? 0: friendName.hashCode());
    result = 31 * result + (friendAvatar == null ? 0: friendAvatar.hashCode());
    result = 31 * result + (friendSex == null ? 0: friendSex.hashCode());
    result = 31 * result + (friendGroupName == null ? 0: friendGroupName.hashCode());
    result = 31 * result + (friendLevel == null ? 0: friendLevel.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class FriendModel {\n");
    
    sb.append("  friendUid: ").append(friendUid).append("\n");
    sb.append("  friendName: ").append(friendName).append("\n");
    sb.append("  friendAvatar: ").append(friendAvatar).append("\n");
    sb.append("  friendSex: ").append(friendSex).append("\n");
    sb.append("  friendGroupName: ").append(friendGroupName).append("\n");
    sb.append("  friendLevel: ").append(friendLevel).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
