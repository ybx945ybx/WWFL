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

import io.swagger.client.model.TopicModelTags;
import java.util.*;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class TopicModel  {
  
  @SerializedName("tid")
  private String tid = null;
  @SerializedName("board_id")
  private String boardId = null;
  @SerializedName("board_name")
  private String boardName = null;
  @SerializedName("title")
  private String title = null;
  @SerializedName("post_time")
  private String postTime = null;
  @SerializedName("author_name")
  private String authorName = null;
  @SerializedName("author_uid")
  private String authorUid = null;
  @SerializedName("view_count")
  private String viewCount = null;
  @SerializedName("reply_count")
  private String replyCount = null;
  @SerializedName("category_name")
  private String categoryName = null;
  @SerializedName("praise_count")
  private String praiseCount = null;
  @SerializedName("collection_count")
  private String collectionCount = null;
  @SerializedName("is_praised")
  private String isPraised = null;
  @SerializedName("is_hot")
  private String isHot = null;
  @SerializedName("is_best")
  private String isBest = null;
  @SerializedName("is_recommended")
  private String isRecommended = null;
  @SerializedName("avatar")
  private String avatar = null;
  @SerializedName("pics")
  private List<String> pics = null;
  @SerializedName("tags")
  private List<TopicModelTags> tags = null;
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
   * 版块ID
   **/
  @ApiModelProperty(value = "版块ID")
  public String getBoardId() {
    return boardId;
  }
  public void setBoardId(String boardId) {
    this.boardId = boardId;
  }

  /**
   * 版块名称
   **/
  @ApiModelProperty(value = "版块名称")
  public String getBoardName() {
    return boardName;
  }
  public void setBoardName(String boardName) {
    this.boardName = boardName;
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
   * 发布时间
   **/
  @ApiModelProperty(value = "发布时间")
  public String getPostTime() {
    return postTime;
  }
  public void setPostTime(String postTime) {
    this.postTime = postTime;
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
   * 作者ID
   **/
  @ApiModelProperty(value = "作者ID")
  public String getAuthorUid() {
    return authorUid;
  }
  public void setAuthorUid(String authorUid) {
    this.authorUid = authorUid;
  }

  /**
   * 查看次数
   **/
  @ApiModelProperty(value = "查看次数")
  public String getViewCount() {
    return viewCount;
  }
  public void setViewCount(String viewCount) {
    this.viewCount = viewCount;
  }

  /**
   * 回复次数
   **/
  @ApiModelProperty(value = "回复次数")
  public String getReplyCount() {
    return replyCount;
  }
  public void setReplyCount(String replyCount) {
    this.replyCount = replyCount;
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
   * 收藏数
   **/
  @ApiModelProperty(value = "收藏数")
  public String getCollectionCount() {
    return collectionCount;
  }
  public void setCollectionCount(String collectionCount) {
    this.collectionCount = collectionCount;
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
   * 是否热帖
   **/
  @ApiModelProperty(value = "是否热帖")
  public String getIsHot() {
    return isHot;
  }
  public void setIsHot(String isHot) {
    this.isHot = isHot;
  }

  /**
   * 是否是精华帖 - 0：非精华帖 1~3：精华1~3级
   **/
  @ApiModelProperty(value = "是否是精华帖 - 0：非精华帖 1~3：精华1~3级")
  public String getIsBest() {
    return isBest;
  }
  public void setIsBest(String isBest) {
    this.isBest = isBest;
  }

  /**
   * 是否是推荐帖
   **/
  @ApiModelProperty(value = "是否是推荐帖")
  public String getIsRecommended() {
    return isRecommended;
  }
  public void setIsRecommended(String isRecommended) {
    this.isRecommended = isRecommended;
  }

  /**
   * 头像地址
   **/
  @ApiModelProperty(value = "头像地址")
  public String getAvatar() {
    return avatar;
  }
  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  /**
   * 帖子包含的图片的地址，用于主题显示
   **/
  @ApiModelProperty(value = "帖子包含的图片的地址，用于主题显示")
  public List<String> getPics() {
    return pics;
  }
  public void setPics(List<String> pics) {
    this.pics = pics;
  }

  /**
   * 相关标签
   **/
  @ApiModelProperty(value = "相关标签")
  public List<TopicModelTags> getTags() {
    return tags;
  }
  public void setTags(List<TopicModelTags> tags) {
    this.tags = tags;
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
    TopicModel topicModel = (TopicModel) o;
    return (tid == null ? topicModel.tid == null : tid.equals(topicModel.tid)) &&
        (boardId == null ? topicModel.boardId == null : boardId.equals(topicModel.boardId)) &&
        (boardName == null ? topicModel.boardName == null : boardName.equals(topicModel.boardName)) &&
        (title == null ? topicModel.title == null : title.equals(topicModel.title)) &&
        (postTime == null ? topicModel.postTime == null : postTime.equals(topicModel.postTime)) &&
        (authorName == null ? topicModel.authorName == null : authorName.equals(topicModel.authorName)) &&
        (authorUid == null ? topicModel.authorUid == null : authorUid.equals(topicModel.authorUid)) &&
        (viewCount == null ? topicModel.viewCount == null : viewCount.equals(topicModel.viewCount)) &&
        (replyCount == null ? topicModel.replyCount == null : replyCount.equals(topicModel.replyCount)) &&
        (categoryName == null ? topicModel.categoryName == null : categoryName.equals(topicModel.categoryName)) &&
        (praiseCount == null ? topicModel.praiseCount == null : praiseCount.equals(topicModel.praiseCount)) &&
        (collectionCount == null ? topicModel.collectionCount == null : collectionCount.equals(topicModel.collectionCount)) &&
        (isPraised == null ? topicModel.isPraised == null : isPraised.equals(topicModel.isPraised)) &&
        (isHot == null ? topicModel.isHot == null : isHot.equals(topicModel.isHot)) &&
        (isBest == null ? topicModel.isBest == null : isBest.equals(topicModel.isBest)) &&
        (isRecommended == null ? topicModel.isRecommended == null : isRecommended.equals(topicModel.isRecommended)) &&
        (avatar == null ? topicModel.avatar == null : avatar.equals(topicModel.avatar)) &&
        (pics == null ? topicModel.pics == null : pics.equals(topicModel.pics)) &&
        (tags == null ? topicModel.tags == null : tags.equals(topicModel.tags)) &&
        (shareTitle == null ? topicModel.shareTitle == null : shareTitle.equals(topicModel.shareTitle)) &&
        (shareContent == null ? topicModel.shareContent == null : shareContent.equals(topicModel.shareContent)) &&
        (shareContentWeibo == null ? topicModel.shareContentWeibo == null : shareContentWeibo.equals(topicModel.shareContentWeibo)) &&
        (shareUrl == null ? topicModel.shareUrl == null : shareUrl.equals(topicModel.shareUrl)) &&
        (sharePic == null ? topicModel.sharePic == null : sharePic.equals(topicModel.sharePic));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (tid == null ? 0: tid.hashCode());
    result = 31 * result + (boardId == null ? 0: boardId.hashCode());
    result = 31 * result + (boardName == null ? 0: boardName.hashCode());
    result = 31 * result + (title == null ? 0: title.hashCode());
    result = 31 * result + (postTime == null ? 0: postTime.hashCode());
    result = 31 * result + (authorName == null ? 0: authorName.hashCode());
    result = 31 * result + (authorUid == null ? 0: authorUid.hashCode());
    result = 31 * result + (viewCount == null ? 0: viewCount.hashCode());
    result = 31 * result + (replyCount == null ? 0: replyCount.hashCode());
    result = 31 * result + (categoryName == null ? 0: categoryName.hashCode());
    result = 31 * result + (praiseCount == null ? 0: praiseCount.hashCode());
    result = 31 * result + (collectionCount == null ? 0: collectionCount.hashCode());
    result = 31 * result + (isPraised == null ? 0: isPraised.hashCode());
    result = 31 * result + (isHot == null ? 0: isHot.hashCode());
    result = 31 * result + (isBest == null ? 0: isBest.hashCode());
    result = 31 * result + (isRecommended == null ? 0: isRecommended.hashCode());
    result = 31 * result + (avatar == null ? 0: avatar.hashCode());
    result = 31 * result + (pics == null ? 0: pics.hashCode());
    result = 31 * result + (tags == null ? 0: tags.hashCode());
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
    sb.append("class TopicModel {\n");
    
    sb.append("  tid: ").append(tid).append("\n");
    sb.append("  boardId: ").append(boardId).append("\n");
    sb.append("  boardName: ").append(boardName).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  postTime: ").append(postTime).append("\n");
    sb.append("  authorName: ").append(authorName).append("\n");
    sb.append("  authorUid: ").append(authorUid).append("\n");
    sb.append("  viewCount: ").append(viewCount).append("\n");
    sb.append("  replyCount: ").append(replyCount).append("\n");
    sb.append("  categoryName: ").append(categoryName).append("\n");
    sb.append("  praiseCount: ").append(praiseCount).append("\n");
    sb.append("  collectionCount: ").append(collectionCount).append("\n");
    sb.append("  isPraised: ").append(isPraised).append("\n");
    sb.append("  isHot: ").append(isHot).append("\n");
    sb.append("  isBest: ").append(isBest).append("\n");
    sb.append("  isRecommended: ").append(isRecommended).append("\n");
    sb.append("  avatar: ").append(avatar).append("\n");
    sb.append("  pics: ").append(pics).append("\n");
    sb.append("  tags: ").append(tags).append("\n");
    sb.append("  shareTitle: ").append(shareTitle).append("\n");
    sb.append("  shareContent: ").append(shareContent).append("\n");
    sb.append("  shareContentWeibo: ").append(shareContentWeibo).append("\n");
    sb.append("  shareUrl: ").append(shareUrl).append("\n");
    sb.append("  sharePic: ").append(sharePic).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}