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

import io.swagger.client.model.ForumIndexBoardModel;
import io.swagger.client.model.SlidePicModel;
import io.swagger.client.model.TopicBriefModel;
import io.swagger.client.model.TopicModel;
import java.util.*;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class ForumIndexHotTopicsModelData  {
  
  @SerializedName("slide_pics")
  private List<SlidePicModel> slidePics = null;
  @SerializedName("boards")
  private List<ForumIndexBoardModel> boards = null;
  @SerializedName("top_topics")
  private List<TopicBriefModel> topTopics = null;
  @SerializedName("talk_topics")
  private List<TopicBriefModel> talkTopics = null;
  @SerializedName("topics")
  private List<TopicModel> topics = null;

  /**
   * 图片轮播
   **/
  @ApiModelProperty(value = "图片轮播")
  public List<SlidePicModel> getSlidePics() {
    return slidePics;
  }
  public void setSlidePics(List<SlidePicModel> slidePics) {
    this.slidePics = slidePics;
  }

  /**
   * 版块
   **/
  @ApiModelProperty(value = "版块")
  public List<ForumIndexBoardModel> getBoards() {
    return boards;
  }
  public void setBoards(List<ForumIndexBoardModel> boards) {
    this.boards = boards;
  }

  /**
   * 置顶帖
   **/
  @ApiModelProperty(value = "置顶帖")
  public List<TopicBriefModel> getTopTopics() {
    return topTopics;
  }
  public void setTopTopics(List<TopicBriefModel> topTopics) {
    this.topTopics = topTopics;
  }

  /**
   * 话题帖
   **/
  @ApiModelProperty(value = "话题帖")
  public List<TopicBriefModel> getTalkTopics() {
    return talkTopics;
  }
  public void setTalkTopics(List<TopicBriefModel> talkTopics) {
    this.talkTopics = talkTopics;
  }

  /**
   * 热帖
   **/
  @ApiModelProperty(value = "热帖")
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
    ForumIndexHotTopicsModelData forumIndexHotTopicsModelData = (ForumIndexHotTopicsModelData) o;
    return (slidePics == null ? forumIndexHotTopicsModelData.slidePics == null : slidePics.equals(forumIndexHotTopicsModelData.slidePics)) &&
        (boards == null ? forumIndexHotTopicsModelData.boards == null : boards.equals(forumIndexHotTopicsModelData.boards)) &&
        (topTopics == null ? forumIndexHotTopicsModelData.topTopics == null : topTopics.equals(forumIndexHotTopicsModelData.topTopics)) &&
        (talkTopics == null ? forumIndexHotTopicsModelData.talkTopics == null : talkTopics.equals(forumIndexHotTopicsModelData.talkTopics)) &&
        (topics == null ? forumIndexHotTopicsModelData.topics == null : topics.equals(forumIndexHotTopicsModelData.topics));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (slidePics == null ? 0: slidePics.hashCode());
    result = 31 * result + (boards == null ? 0: boards.hashCode());
    result = 31 * result + (topTopics == null ? 0: topTopics.hashCode());
    result = 31 * result + (talkTopics == null ? 0: talkTopics.hashCode());
    result = 31 * result + (topics == null ? 0: topics.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ForumIndexHotTopicsModelData {\n");
    
    sb.append("  slidePics: ").append(slidePics).append("\n");
    sb.append("  boards: ").append(boards).append("\n");
    sb.append("  topTopics: ").append(topTopics).append("\n");
    sb.append("  talkTopics: ").append(talkTopics).append("\n");
    sb.append("  topics: ").append(topics).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
