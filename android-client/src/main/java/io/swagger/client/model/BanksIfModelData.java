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
 * 银行信息
 **/
@ApiModel(description = "银行信息")
public class BanksIfModelData  {
  
  @SerializedName("bank_id")
  private String bankId = null;
  @SerializedName("bank_name")
  private String bankName = null;

  /**
   * 银行ID
   **/
  @ApiModelProperty(value = "银行ID")
  public String getBankId() {
    return bankId;
  }
  public void setBankId(String bankId) {
    this.bankId = bankId;
  }

  /**
   * 银行名称
   **/
  @ApiModelProperty(value = "银行名称")
  public String getBankName() {
    return bankName;
  }
  public void setBankName(String bankName) {
    this.bankName = bankName;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BanksIfModelData banksIfModelData = (BanksIfModelData) o;
    return (bankId == null ? banksIfModelData.bankId == null : bankId.equals(banksIfModelData.bankId)) &&
        (bankName == null ? banksIfModelData.bankName == null : bankName.equals(banksIfModelData.bankName));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (bankId == null ? 0: bankId.hashCode());
    result = 31 * result + (bankName == null ? 0: bankName.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class BanksIfModelData {\n");
    
    sb.append("  bankId: ").append(bankId).append("\n");
    sb.append("  bankName: ").append(bankName).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
