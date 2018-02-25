/**
 * 五五海淘返利APP新版接口
 * 更新日志<br> 相对于上一build的变更： <br/> APP5.4接口 
 *
 * OpenAPI spec version: 1.3 build20170808-6
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

import io.swagger.client.model.ShoppedStoresListIfModelData;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class ShoppedStoresListIfModel  {
  
  @SerializedName("code")
  private String code = null;
  @SerializedName("msg")
  private String msg = null;
  @SerializedName("data")
  private ShoppedStoresListIfModelData data = null;

  /**
   * 成功返回0
   **/
  @ApiModelProperty(value = "成功返回0")
  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * 成功信息
   **/
  @ApiModelProperty(value = "成功信息")
  public String getMsg() {
    return msg;
  }
  public void setMsg(String msg) {
    this.msg = msg;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public ShoppedStoresListIfModelData getData() {
    return data;
  }
  public void setData(ShoppedStoresListIfModelData data) {
    this.data = data;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShoppedStoresListIfModel shoppedStoresListIfModel = (ShoppedStoresListIfModel) o;
    return (code == null ? shoppedStoresListIfModel.code == null : code.equals(shoppedStoresListIfModel.code)) &&
        (msg == null ? shoppedStoresListIfModel.msg == null : msg.equals(shoppedStoresListIfModel.msg)) &&
        (data == null ? shoppedStoresListIfModel.data == null : data.equals(shoppedStoresListIfModel.data));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (code == null ? 0: code.hashCode());
    result = 31 * result + (msg == null ? 0: msg.hashCode());
    result = 31 * result + (data == null ? 0: data.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShoppedStoresListIfModel {\n");
    
    sb.append("  code: ").append(code).append("\n");
    sb.append("  msg: ").append(msg).append("\n");
    sb.append("  data: ").append(data).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
