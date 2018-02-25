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
public class InviteIfModelData  {
  
  @SerializedName("intro_topic_id")
  private String introTopicId = null;
  @SerializedName("reward_count_view")
  private String rewardCountView = null;
  @SerializedName("sub_reward_count_view")
  private String subRewardCountView = null;
  @SerializedName("invite_code")
  private String inviteCode = null;
  @SerializedName("invite_reward_view")
  private String inviteRewardView = null;
  @SerializedName("invite_description")
  private String inviteDescription = null;
  @SerializedName("invited_users_count")
  private String invitedUsersCount = null;
  @SerializedName("share_title")
  private String shareTitle = null;
  @SerializedName("share_content")
  private String shareContent = null;
  @SerializedName("share_content_weibo")
  private String shareContentWeibo = null;
  @SerializedName("share_url")
  private String shareUrl = null;
  @SerializedName("share_pic")
  private String sharePic = null;

  /**
   * 推广介绍帖ID
   **/
  @ApiModelProperty(value = "推广介绍帖ID")
  public String getIntroTopicId() {
    return introTopicId;
  }
  public void setIntroTopicId(String introTopicId) {
    this.introTopicId = introTopicId;
  }

  /**
   * 推广奖励金额（文字说明）
   **/
  @ApiModelProperty(value = "推广奖励金额（文字说明）")
  public String getRewardCountView() {
    return rewardCountView;
  }
  public void setRewardCountView(String rewardCountView) {
    this.rewardCountView = rewardCountView;
  }

  /**
   * 下级用户推广奖励金额（文字说明）
   **/
  @ApiModelProperty(value = "下级用户推广奖励金额（文字说明）")
  public String getSubRewardCountView() {
    return subRewardCountView;
  }
  public void setSubRewardCountView(String subRewardCountView) {
    this.subRewardCountView = subRewardCountView;
  }

  /**
   * 邀请码
   **/
  @ApiModelProperty(value = "邀请码")
  public String getInviteCode() {
    return inviteCode;
  }
  public void setInviteCode(String inviteCode) {
    this.inviteCode = inviteCode;
  }

  /**
   * 每邀请一个用户获得多少奖励（文字说明 自带币种符号）
   **/
  @ApiModelProperty(value = "每邀请一个用户获得多少奖励（文字说明 自带币种符号）")
  public String getInviteRewardView() {
    return inviteRewardView;
  }
  public void setInviteRewardView(String inviteRewardView) {
    this.inviteRewardView = inviteRewardView;
  }

  /**
   * 邀请奖励说明
   **/
  @ApiModelProperty(value = "邀请奖励说明")
  public String getInviteDescription() {
    return inviteDescription;
  }
  public void setInviteDescription(String inviteDescription) {
    this.inviteDescription = inviteDescription;
  }

  /**
   * 已成功邀请人数
   **/
  @ApiModelProperty(value = "已成功邀请人数")
  public String getInvitedUsersCount() {
    return invitedUsersCount;
  }
  public void setInvitedUsersCount(String invitedUsersCount) {
    this.invitedUsersCount = invitedUsersCount;
  }

  /**
   * 分享内容
   **/
  @ApiModelProperty(value = "分享内容")
  public String getShareTitle() {
    return shareTitle;
  }
  public void setShareTitle(String shareTitle) {
    this.shareTitle = shareTitle;
  }

  /**
   * 分享内容正文
   **/
  @ApiModelProperty(value = "分享内容正文")
  public String getShareContent() {
    return shareContent;
  }
  public void setShareContent(String shareContent) {
    this.shareContent = shareContent;
  }

  /**
   * 分享到微博的正文
   **/
  @ApiModelProperty(value = "分享到微博的正文")
  public String getShareContentWeibo() {
    return shareContentWeibo;
  }
  public void setShareContentWeibo(String shareContentWeibo) {
    this.shareContentWeibo = shareContentWeibo;
  }

  /**
   * 分享内容链接
   **/
  @ApiModelProperty(value = "分享内容链接")
  public String getShareUrl() {
    return shareUrl;
  }
  public void setShareUrl(String shareUrl) {
    this.shareUrl = shareUrl;
  }

  /**
   * 分享内容封面图片
   **/
  @ApiModelProperty(value = "分享内容封面图片")
  public String getSharePic() {
    return sharePic;
  }
  public void setSharePic(String sharePic) {
    this.sharePic = sharePic;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InviteIfModelData inviteIfModelData = (InviteIfModelData) o;
    return (introTopicId == null ? inviteIfModelData.introTopicId == null : introTopicId.equals(inviteIfModelData.introTopicId)) &&
        (rewardCountView == null ? inviteIfModelData.rewardCountView == null : rewardCountView.equals(inviteIfModelData.rewardCountView)) &&
        (subRewardCountView == null ? inviteIfModelData.subRewardCountView == null : subRewardCountView.equals(inviteIfModelData.subRewardCountView)) &&
        (inviteCode == null ? inviteIfModelData.inviteCode == null : inviteCode.equals(inviteIfModelData.inviteCode)) &&
        (inviteRewardView == null ? inviteIfModelData.inviteRewardView == null : inviteRewardView.equals(inviteIfModelData.inviteRewardView)) &&
        (inviteDescription == null ? inviteIfModelData.inviteDescription == null : inviteDescription.equals(inviteIfModelData.inviteDescription)) &&
        (invitedUsersCount == null ? inviteIfModelData.invitedUsersCount == null : invitedUsersCount.equals(inviteIfModelData.invitedUsersCount)) &&
        (shareTitle == null ? inviteIfModelData.shareTitle == null : shareTitle.equals(inviteIfModelData.shareTitle)) &&
        (shareContent == null ? inviteIfModelData.shareContent == null : shareContent.equals(inviteIfModelData.shareContent)) &&
        (shareContentWeibo == null ? inviteIfModelData.shareContentWeibo == null : shareContentWeibo.equals(inviteIfModelData.shareContentWeibo)) &&
        (shareUrl == null ? inviteIfModelData.shareUrl == null : shareUrl.equals(inviteIfModelData.shareUrl)) &&
        (sharePic == null ? inviteIfModelData.sharePic == null : sharePic.equals(inviteIfModelData.sharePic));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (introTopicId == null ? 0: introTopicId.hashCode());
    result = 31 * result + (rewardCountView == null ? 0: rewardCountView.hashCode());
    result = 31 * result + (subRewardCountView == null ? 0: subRewardCountView.hashCode());
    result = 31 * result + (inviteCode == null ? 0: inviteCode.hashCode());
    result = 31 * result + (inviteRewardView == null ? 0: inviteRewardView.hashCode());
    result = 31 * result + (inviteDescription == null ? 0: inviteDescription.hashCode());
    result = 31 * result + (invitedUsersCount == null ? 0: invitedUsersCount.hashCode());
    result = 31 * result + (shareTitle == null ? 0: shareTitle.hashCode());
    result = 31 * result + (shareContent == null ? 0: shareContent.hashCode());
    result = 31 * result + (shareContentWeibo == null ? 0: shareContentWeibo.hashCode());
    result = 31 * result + (shareUrl == null ? 0: shareUrl.hashCode());
    result = 31 * result + (sharePic == null ? 0: sharePic.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class InviteIfModelData {\n");
    
    sb.append("  introTopicId: ").append(introTopicId).append("\n");
    sb.append("  rewardCountView: ").append(rewardCountView).append("\n");
    sb.append("  subRewardCountView: ").append(subRewardCountView).append("\n");
    sb.append("  inviteCode: ").append(inviteCode).append("\n");
    sb.append("  inviteRewardView: ").append(inviteRewardView).append("\n");
    sb.append("  inviteDescription: ").append(inviteDescription).append("\n");
    sb.append("  invitedUsersCount: ").append(invitedUsersCount).append("\n");
    sb.append("  shareTitle: ").append(shareTitle).append("\n");
    sb.append("  shareContent: ").append(shareContent).append("\n");
    sb.append("  shareContentWeibo: ").append(shareContentWeibo).append("\n");
    sb.append("  shareUrl: ").append(shareUrl).append("\n");
    sb.append("  sharePic: ").append(sharePic).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
