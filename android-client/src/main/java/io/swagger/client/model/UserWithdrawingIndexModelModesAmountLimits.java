/**
 * 五五海淘返利APP新版接口
 * 更新日志<br> 相对于上一build的变更： <br/> 调整搜索模型 
 *
 * OpenAPI spec version: 1.8 build20180130-2
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
public class UserWithdrawingIndexModelModesAmountLimits  {
  
  @SerializedName("mode_key")
  private String modeKey = null;
  @SerializedName("limit")
  private String limit = null;

  /**
   * 提现方式键名
   **/
  @ApiModelProperty(value = "提现方式键名")
  public String getModeKey() {
    return modeKey;
  }
  public void setModeKey(String modeKey) {
    this.modeKey = modeKey;
  }

  /**
   * 提现金额上限
   **/
  @ApiModelProperty(value = "提现金额上限")
  public String getLimit() {
    return limit;
  }
  public void setLimit(String limit) {
    this.limit = limit;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserWithdrawingIndexModelModesAmountLimits userWithdrawingIndexModelModesAmountLimits = (UserWithdrawingIndexModelModesAmountLimits) o;
    return (modeKey == null ? userWithdrawingIndexModelModesAmountLimits.modeKey == null : modeKey.equals(userWithdrawingIndexModelModesAmountLimits.modeKey)) &&
        (limit == null ? userWithdrawingIndexModelModesAmountLimits.limit == null : limit.equals(userWithdrawingIndexModelModesAmountLimits.limit));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (modeKey == null ? 0: modeKey.hashCode());
    result = 31 * result + (limit == null ? 0: limit.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserWithdrawingIndexModelModesAmountLimits {\n");
    
    sb.append("  modeKey: ").append(modeKey).append("\n");
    sb.append("  limit: ").append(limit).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
