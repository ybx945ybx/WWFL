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
 * 回复的帖子
 **/
@ApiModel(description = "回复的帖子")
public class MyReplyModelSource  {
  
  @SerializedName("author_id")
  private String authorId = null;
  @SerializedName("author_name")
  private String authorName = null;
  @SerializedName("tid")
  private String tid = null;
  @SerializedName("title")
  private String title = null;

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
   * 回复的帖子ID
   **/
  @ApiModelProperty(value = "回复的帖子ID")
  public String getTid() {
    return tid;
  }
  public void setTid(String tid) {
    this.tid = tid;
  }

  /**
   * 主题标题
   **/
  @ApiModelProperty(value = "主题标题")
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MyReplyModelSource myReplyModelSource = (MyReplyModelSource) o;
    return (authorId == null ? myReplyModelSource.authorId == null : authorId.equals(myReplyModelSource.authorId)) &&
        (authorName == null ? myReplyModelSource.authorName == null : authorName.equals(myReplyModelSource.authorName)) &&
        (tid == null ? myReplyModelSource.tid == null : tid.equals(myReplyModelSource.tid)) &&
        (title == null ? myReplyModelSource.title == null : title.equals(myReplyModelSource.title));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (authorId == null ? 0: authorId.hashCode());
    result = 31 * result + (authorName == null ? 0: authorName.hashCode());
    result = 31 * result + (tid == null ? 0: tid.hashCode());
    result = 31 * result + (title == null ? 0: title.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class MyReplyModelSource {\n");
    
    sb.append("  authorId: ").append(authorId).append("\n");
    sb.append("  authorName: ").append(authorName).append("\n");
    sb.append("  tid: ").append(tid).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
