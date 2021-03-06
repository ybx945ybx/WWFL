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

import java.util.*;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class StaticsIfModelData  {
  
  @SerializedName("type")
  private String type = null;
  @SerializedName("id")
  private String id = null;
  @SerializedName("files")
  private List<String> files = null;
  @SerializedName("play_time")
  private String playTime = null;

  /**
   * 静态资源类型 - 1：图片 2：文件 3：逐帧动画
   **/
  @ApiModelProperty(value = "静态资源类型 - 1：图片 2：文件 3：逐帧动画")
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }

  /**
   * 唯一标识 用于区分资源 - refreshing_animate:下拉刷新图片 store_activity_label_pic:商家LOGO活动角标
   **/
  @ApiModelProperty(value = "唯一标识 用于区分资源 - refreshing_animate:下拉刷新图片 store_activity_label_pic:商家LOGO活动角标")
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<String> getFiles() {
    return files;
  }
  public void setFiles(List<String> files) {
    this.files = files;
  }

  /**
   * 逐帧动画播放时间 单位毫秒
   **/
  @ApiModelProperty(value = "逐帧动画播放时间 单位毫秒")
  public String getPlayTime() {
    return playTime;
  }
  public void setPlayTime(String playTime) {
    this.playTime = playTime;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StaticsIfModelData staticsIfModelData = (StaticsIfModelData) o;
    return (type == null ? staticsIfModelData.type == null : type.equals(staticsIfModelData.type)) &&
        (id == null ? staticsIfModelData.id == null : id.equals(staticsIfModelData.id)) &&
        (files == null ? staticsIfModelData.files == null : files.equals(staticsIfModelData.files)) &&
        (playTime == null ? staticsIfModelData.playTime == null : playTime.equals(staticsIfModelData.playTime));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (type == null ? 0: type.hashCode());
    result = 31 * result + (id == null ? 0: id.hashCode());
    result = 31 * result + (files == null ? 0: files.hashCode());
    result = 31 * result + (playTime == null ? 0: playTime.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class StaticsIfModelData {\n");
    
    sb.append("  type: ").append(type).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  files: ").append(files).append("\n");
    sb.append("  playTime: ").append(playTime).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
