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
public class AddPostSuccessModelData  {
  
  @SerializedName("post_id")
  private String postId = null;

  /**
   * 
   **/
  @ApiModelProperty(value = "")
  public String getPostId() {
    return postId;
  }
  public void setPostId(String postId) {
    this.postId = postId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AddPostSuccessModelData addPostSuccessModelData = (AddPostSuccessModelData) o;
    return (postId == null ? addPostSuccessModelData.postId == null : postId.equals(addPostSuccessModelData.postId));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (postId == null ? 0: postId.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class AddPostSuccessModelData {\n");
    
    sb.append("  postId: ").append(postId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
