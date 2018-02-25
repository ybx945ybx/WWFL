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

import io.swagger.client.model.ForumBoardModel;
import io.swagger.client.model.ForumSubBoardModel;
import io.swagger.client.model.TopicModel;
import io.swagger.client.model.TopicModelTags;
import java.util.*;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class ForumIndexShowOrdersModelData  {
  
  @SerializedName("board_info")
  private ForumBoardModel boardInfo = null;
  @SerializedName("sub_boards")
  private List<ForumSubBoardModel> subBoards = null;
  @SerializedName("tags")
  private List<TopicModelTags> tags = null;
  @SerializedName("topics")
  private List<TopicModel> topics = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public ForumBoardModel getBoardInfo() {
    return boardInfo;
  }
  public void setBoardInfo(ForumBoardModel boardInfo) {
    this.boardInfo = boardInfo;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<ForumSubBoardModel> getSubBoards() {
    return subBoards;
  }
  public void setSubBoards(List<ForumSubBoardModel> subBoards) {
    this.subBoards = subBoards;
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
   **/
  @ApiModelProperty(value = "")
  public List<TopicModel> getTopics() {
    return topics;
  }
  public void setTopics(List<TopicModel> topics) {
    this.topics = topics;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ForumIndexShowOrdersModelData forumIndexShowOrdersModelData = (ForumIndexShowOrdersModelData) o;
    return (boardInfo == null ? forumIndexShowOrdersModelData.boardInfo == null : boardInfo.equals(forumIndexShowOrdersModelData.boardInfo)) &&
        (subBoards == null ? forumIndexShowOrdersModelData.subBoards == null : subBoards.equals(forumIndexShowOrdersModelData.subBoards)) &&
        (tags == null ? forumIndexShowOrdersModelData.tags == null : tags.equals(forumIndexShowOrdersModelData.tags)) &&
        (topics == null ? forumIndexShowOrdersModelData.topics == null : topics.equals(forumIndexShowOrdersModelData.topics));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (boardInfo == null ? 0: boardInfo.hashCode());
    result = 31 * result + (subBoards == null ? 0: subBoards.hashCode());
    result = 31 * result + (tags == null ? 0: tags.hashCode());
    result = 31 * result + (topics == null ? 0: topics.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ForumIndexShowOrdersModelData {\n");
    
    sb.append("  boardInfo: ").append(boardInfo).append("\n");
    sb.append("  subBoards: ").append(subBoards).append("\n");
    sb.append("  tags: ").append(tags).append("\n");
    sb.append("  topics: ").append(topics).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
