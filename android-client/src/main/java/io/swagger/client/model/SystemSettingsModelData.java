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

import io.swagger.client.model.SystemSettingsModelDataActivity;
import io.swagger.client.model.SystemSettingsModelDataTemplateInfo;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class SystemSettingsModelData  {
  
  @SerializedName("verify_type")
  private String verifyType = null;
  @SerializedName("register_reward_amount")
  private String registerRewardAmount = null;
  @SerializedName("activity")
  private SystemSettingsModelDataActivity activity = null;
  @SerializedName("template_info")
  private SystemSettingsModelDataTemplateInfo templateInfo = null;

  /**
   * 验证码类型
   **/
  @ApiModelProperty(value = "验证码类型")
  public String getVerifyType() {
    return verifyType;
  }
  public void setVerifyType(String verifyType) {
    this.verifyType = verifyType;
  }

  /**
   * 注册赠送金额（数值 单位美元）
   **/
  @ApiModelProperty(value = "注册赠送金额（数值 单位美元）")
  public String getRegisterRewardAmount() {
    return registerRewardAmount;
  }
  public void setRegisterRewardAmount(String registerRewardAmount) {
    this.registerRewardAmount = registerRewardAmount;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public SystemSettingsModelDataActivity getActivity() {
    return activity;
  }
  public void setActivity(SystemSettingsModelDataActivity activity) {
    this.activity = activity;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public SystemSettingsModelDataTemplateInfo getTemplateInfo() {
    return templateInfo;
  }
  public void setTemplateInfo(SystemSettingsModelDataTemplateInfo templateInfo) {
    this.templateInfo = templateInfo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SystemSettingsModelData systemSettingsModelData = (SystemSettingsModelData) o;
    return (verifyType == null ? systemSettingsModelData.verifyType == null : verifyType.equals(systemSettingsModelData.verifyType)) &&
        (registerRewardAmount == null ? systemSettingsModelData.registerRewardAmount == null : registerRewardAmount.equals(systemSettingsModelData.registerRewardAmount)) &&
        (activity == null ? systemSettingsModelData.activity == null : activity.equals(systemSettingsModelData.activity)) &&
        (templateInfo == null ? systemSettingsModelData.templateInfo == null : templateInfo.equals(systemSettingsModelData.templateInfo));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (verifyType == null ? 0: verifyType.hashCode());
    result = 31 * result + (registerRewardAmount == null ? 0: registerRewardAmount.hashCode());
    result = 31 * result + (activity == null ? 0: activity.hashCode());
    result = 31 * result + (templateInfo == null ? 0: templateInfo.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class SystemSettingsModelData {\n");
    
    sb.append("  verifyType: ").append(verifyType).append("\n");
    sb.append("  registerRewardAmount: ").append(registerRewardAmount).append("\n");
    sb.append("  activity: ").append(activity).append("\n");
    sb.append("  templateInfo: ").append(templateInfo).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}