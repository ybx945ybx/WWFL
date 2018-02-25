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
public class SingedInInfoModelDataCalendar  {
  
  @SerializedName("date")
  private String date = null;
  @SerializedName("is_signed_in")
  private String isSignedIn = null;

  /**
   * 日期的阿拉伯数字 1~31
   **/
  @ApiModelProperty(value = "日期的阿拉伯数字 1~31")
  public String getDate() {
    return date;
  }
  public void setDate(String date) {
    this.date = date;
  }

  /**
   * 当日是否已签到
   **/
  @ApiModelProperty(value = "当日是否已签到")
  public String getIsSignedIn() {
    return isSignedIn;
  }
  public void setIsSignedIn(String isSignedIn) {
    this.isSignedIn = isSignedIn;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SingedInInfoModelDataCalendar singedInInfoModelDataCalendar = (SingedInInfoModelDataCalendar) o;
    return (date == null ? singedInInfoModelDataCalendar.date == null : date.equals(singedInInfoModelDataCalendar.date)) &&
        (isSignedIn == null ? singedInInfoModelDataCalendar.isSignedIn == null : isSignedIn.equals(singedInInfoModelDataCalendar.isSignedIn));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (date == null ? 0: date.hashCode());
    result = 31 * result + (isSignedIn == null ? 0: isSignedIn.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class SingedInInfoModelDataCalendar {\n");
    
    sb.append("  date: ").append(date).append("\n");
    sb.append("  isSignedIn: ").append(isSignedIn).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
