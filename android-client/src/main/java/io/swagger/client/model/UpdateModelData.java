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
 * 更新日志模型
 **/
@ApiModel(description = "更新日志模型")
public class UpdateModelData  {
  
  @SerializedName("low_ver_desc")
  private String lowVerDesc = null;
  @SerializedName("low_ver_num")
  private String lowVerNum = null;
  @SerializedName("now_ver_desc")
  private String nowVerDesc = null;
  @SerializedName("now_ver_num")
  private String nowVerNum = null;
  @SerializedName("new_change")
  private String newChange = null;
  @SerializedName("download_url")
  private String downloadUrl = null;

  /**
   * 最低版本名称
   **/
  @ApiModelProperty(value = "最低版本名称")
  public String getLowVerDesc() {
    return lowVerDesc;
  }
  public void setLowVerDesc(String lowVerDesc) {
    this.lowVerDesc = lowVerDesc;
  }

  /**
   * 最低版本号 整型
   **/
  @ApiModelProperty(value = "最低版本号 整型")
  public String getLowVerNum() {
    return lowVerNum;
  }
  public void setLowVerNum(String lowVerNum) {
    this.lowVerNum = lowVerNum;
  }

  /**
   * 当前更新版本名称
   **/
  @ApiModelProperty(value = "当前更新版本名称")
  public String getNowVerDesc() {
    return nowVerDesc;
  }
  public void setNowVerDesc(String nowVerDesc) {
    this.nowVerDesc = nowVerDesc;
  }

  /**
   * 当前更新版本号 整型
   **/
  @ApiModelProperty(value = "当前更新版本号 整型")
  public String getNowVerNum() {
    return nowVerNum;
  }
  public void setNowVerNum(String nowVerNum) {
    this.nowVerNum = nowVerNum;
  }

  /**
   * 更新日志内容
   **/
  @ApiModelProperty(value = "更新日志内容")
  public String getNewChange() {
    return newChange;
  }
  public void setNewChange(String newChange) {
    this.newChange = newChange;
  }

  /**
   * 新版本APP包下载地址
   **/
  @ApiModelProperty(value = "新版本APP包下载地址")
  public String getDownloadUrl() {
    return downloadUrl;
  }
  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateModelData updateModelData = (UpdateModelData) o;
    return (lowVerDesc == null ? updateModelData.lowVerDesc == null : lowVerDesc.equals(updateModelData.lowVerDesc)) &&
        (lowVerNum == null ? updateModelData.lowVerNum == null : lowVerNum.equals(updateModelData.lowVerNum)) &&
        (nowVerDesc == null ? updateModelData.nowVerDesc == null : nowVerDesc.equals(updateModelData.nowVerDesc)) &&
        (nowVerNum == null ? updateModelData.nowVerNum == null : nowVerNum.equals(updateModelData.nowVerNum)) &&
        (newChange == null ? updateModelData.newChange == null : newChange.equals(updateModelData.newChange)) &&
        (downloadUrl == null ? updateModelData.downloadUrl == null : downloadUrl.equals(updateModelData.downloadUrl));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (lowVerDesc == null ? 0: lowVerDesc.hashCode());
    result = 31 * result + (lowVerNum == null ? 0: lowVerNum.hashCode());
    result = 31 * result + (nowVerDesc == null ? 0: nowVerDesc.hashCode());
    result = 31 * result + (nowVerNum == null ? 0: nowVerNum.hashCode());
    result = 31 * result + (newChange == null ? 0: newChange.hashCode());
    result = 31 * result + (downloadUrl == null ? 0: downloadUrl.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateModelData {\n");
    
    sb.append("  lowVerDesc: ").append(lowVerDesc).append("\n");
    sb.append("  lowVerNum: ").append(lowVerNum).append("\n");
    sb.append("  nowVerDesc: ").append(nowVerDesc).append("\n");
    sb.append("  nowVerNum: ").append(nowVerNum).append("\n");
    sb.append("  newChange: ").append(newChange).append("\n");
    sb.append("  downloadUrl: ").append(downloadUrl).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
