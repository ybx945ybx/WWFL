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
public class CreditCardModel  {
  
  @SerializedName("realname")
  private String realname = null;
  @SerializedName("card_number")
  private String cardNumber = null;
  @SerializedName("expiration_timestamp")
  private String expirationTimestamp = null;

  /**
   * 真实姓名
   **/
  @ApiModelProperty(value = "真实姓名")
  public String getRealname() {
    return realname;
  }
  public void setRealname(String realname) {
    this.realname = realname;
  }

  /**
   * 信用卡号
   **/
  @ApiModelProperty(value = "信用卡号")
  public String getCardNumber() {
    return cardNumber;
  }
  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
  }

  /**
   * 到期时间(时间戳)
   **/
  @ApiModelProperty(value = "到期时间(时间戳)")
  public String getExpirationTimestamp() {
    return expirationTimestamp;
  }
  public void setExpirationTimestamp(String expirationTimestamp) {
    this.expirationTimestamp = expirationTimestamp;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreditCardModel creditCardModel = (CreditCardModel) o;
    return (realname == null ? creditCardModel.realname == null : realname.equals(creditCardModel.realname)) &&
        (cardNumber == null ? creditCardModel.cardNumber == null : cardNumber.equals(creditCardModel.cardNumber)) &&
        (expirationTimestamp == null ? creditCardModel.expirationTimestamp == null : expirationTimestamp.equals(creditCardModel.expirationTimestamp));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (realname == null ? 0: realname.hashCode());
    result = 31 * result + (cardNumber == null ? 0: cardNumber.hashCode());
    result = 31 * result + (expirationTimestamp == null ? 0: expirationTimestamp.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreditCardModel {\n");
    
    sb.append("  realname: ").append(realname).append("\n");
    sb.append("  cardNumber: ").append(cardNumber).append("\n");
    sb.append("  expirationTimestamp: ").append(expirationTimestamp).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
