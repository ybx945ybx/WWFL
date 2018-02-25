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


import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "")
public class WithdrawingRecordDetailModel  {
  
  @SerializedName("record_id")
  private String recordId = null;
  @SerializedName("mode_id")
  private String modeId = null;
  @SerializedName("mode_name")
  private String modeName = null;
  @SerializedName("mode_icon")
  private String modeIcon = null;
  @SerializedName("mode_view")
  private String modeView = null;
  @SerializedName("status")
  private String status = null;
  @SerializedName("status_view")
  private String statusView = null;
  @SerializedName("amount")
  private String amount = null;
  @SerializedName("amount_view")
  private String amountView = null;
  @SerializedName("account")
  private String account = null;
  @SerializedName("bank_name")
  private String bankName = null;
  @SerializedName("realname")
  private String realname = null;
  @SerializedName("comment")
  private String comment = null;
  @SerializedName("withdrawing_time")
  private String withdrawingTime = null;

  /**
   * 提现记录ID
   **/
  @ApiModelProperty(value = "提现记录ID")
  public String getRecordId() {
    return recordId;
  }
  public void setRecordId(String recordId) {
    this.recordId = recordId;
  }

  /**
   * 提现方式ID
   **/
  @ApiModelProperty(value = "提现方式ID")
  public String getModeId() {
    return modeId;
  }
  public void setModeId(String modeId) {
    this.modeId = modeId;
  }

  /**
   * 提现方式名称
   **/
  @ApiModelProperty(value = "提现方式名称")
  public String getModeName() {
    return modeName;
  }
  public void setModeName(String modeName) {
    this.modeName = modeName;
  }

  /**
   * 提现方式图标
   **/
  @ApiModelProperty(value = "提现方式图标")
  public String getModeIcon() {
    return modeIcon;
  }
  public void setModeIcon(String modeIcon) {
    this.modeIcon = modeIcon;
  }

  /**
   * 提现方式文案
   **/
  @ApiModelProperty(value = "提现方式文案")
  public String getModeView() {
    return modeView;
  }
  public void setModeView(String modeView) {
    this.modeView = modeView;
  }

  /**
   * 状态 - 1:审核中 2:待付款 3:提现成功 4:审核未通过 5:提现被驳回 6:提现失败
   **/
  @ApiModelProperty(value = "状态 - 1:审核中 2:待付款 3:提现成功 4:审核未通过 5:提现被驳回 6:提现失败")
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * 状态(文字说明)
   **/
  @ApiModelProperty(value = "状态(文字说明)")
  public String getStatusView() {
    return statusView;
  }
  public void setStatusView(String statusView) {
    this.statusView = statusView;
  }

  /**
   * 提现金额
   **/
  @ApiModelProperty(value = "提现金额")
  public String getAmount() {
    return amount;
  }
  public void setAmount(String amount) {
    this.amount = amount;
  }

  /**
   * 提现金额(文字描述)
   **/
  @ApiModelProperty(value = "提现金额(文字描述)")
  public String getAmountView() {
    return amountView;
  }
  public void setAmountView(String amountView) {
    this.amountView = amountView;
  }

  /**
   * 提现账户
   **/
  @ApiModelProperty(value = "提现账户")
  public String getAccount() {
    return account;
  }
  public void setAccount(String account) {
    this.account = account;
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

  /**
   * 提现账户姓名
   **/
  @ApiModelProperty(value = "提现账户姓名")
  public String getRealname() {
    return realname;
  }
  public void setRealname(String realname) {
    this.realname = realname;
  }

  /**
   * 备注
   **/
  @ApiModelProperty(value = "备注")
  public String getComment() {
    return comment;
  }
  public void setComment(String comment) {
    this.comment = comment;
  }

  /**
   * 提现时间
   **/
  @ApiModelProperty(value = "提现时间")
  public String getWithdrawingTime() {
    return withdrawingTime;
  }
  public void setWithdrawingTime(String withdrawingTime) {
    this.withdrawingTime = withdrawingTime;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WithdrawingRecordDetailModel withdrawingRecordDetailModel = (WithdrawingRecordDetailModel) o;
    return (recordId == null ? withdrawingRecordDetailModel.recordId == null : recordId.equals(withdrawingRecordDetailModel.recordId)) &&
        (modeId == null ? withdrawingRecordDetailModel.modeId == null : modeId.equals(withdrawingRecordDetailModel.modeId)) &&
        (modeName == null ? withdrawingRecordDetailModel.modeName == null : modeName.equals(withdrawingRecordDetailModel.modeName)) &&
        (modeIcon == null ? withdrawingRecordDetailModel.modeIcon == null : modeIcon.equals(withdrawingRecordDetailModel.modeIcon)) &&
        (modeView == null ? withdrawingRecordDetailModel.modeView == null : modeView.equals(withdrawingRecordDetailModel.modeView)) &&
        (status == null ? withdrawingRecordDetailModel.status == null : status.equals(withdrawingRecordDetailModel.status)) &&
        (statusView == null ? withdrawingRecordDetailModel.statusView == null : statusView.equals(withdrawingRecordDetailModel.statusView)) &&
        (amount == null ? withdrawingRecordDetailModel.amount == null : amount.equals(withdrawingRecordDetailModel.amount)) &&
        (amountView == null ? withdrawingRecordDetailModel.amountView == null : amountView.equals(withdrawingRecordDetailModel.amountView)) &&
        (account == null ? withdrawingRecordDetailModel.account == null : account.equals(withdrawingRecordDetailModel.account)) &&
        (bankName == null ? withdrawingRecordDetailModel.bankName == null : bankName.equals(withdrawingRecordDetailModel.bankName)) &&
        (realname == null ? withdrawingRecordDetailModel.realname == null : realname.equals(withdrawingRecordDetailModel.realname)) &&
        (comment == null ? withdrawingRecordDetailModel.comment == null : comment.equals(withdrawingRecordDetailModel.comment)) &&
        (withdrawingTime == null ? withdrawingRecordDetailModel.withdrawingTime == null : withdrawingTime.equals(withdrawingRecordDetailModel.withdrawingTime));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (recordId == null ? 0: recordId.hashCode());
    result = 31 * result + (modeId == null ? 0: modeId.hashCode());
    result = 31 * result + (modeName == null ? 0: modeName.hashCode());
    result = 31 * result + (modeIcon == null ? 0: modeIcon.hashCode());
    result = 31 * result + (modeView == null ? 0: modeView.hashCode());
    result = 31 * result + (status == null ? 0: status.hashCode());
    result = 31 * result + (statusView == null ? 0: statusView.hashCode());
    result = 31 * result + (amount == null ? 0: amount.hashCode());
    result = 31 * result + (amountView == null ? 0: amountView.hashCode());
    result = 31 * result + (account == null ? 0: account.hashCode());
    result = 31 * result + (bankName == null ? 0: bankName.hashCode());
    result = 31 * result + (realname == null ? 0: realname.hashCode());
    result = 31 * result + (comment == null ? 0: comment.hashCode());
    result = 31 * result + (withdrawingTime == null ? 0: withdrawingTime.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class WithdrawingRecordDetailModel {\n");
    
    sb.append("  recordId: ").append(recordId).append("\n");
    sb.append("  modeId: ").append(modeId).append("\n");
    sb.append("  modeName: ").append(modeName).append("\n");
    sb.append("  modeIcon: ").append(modeIcon).append("\n");
    sb.append("  modeView: ").append(modeView).append("\n");
    sb.append("  status: ").append(status).append("\n");
    sb.append("  statusView: ").append(statusView).append("\n");
    sb.append("  amount: ").append(amount).append("\n");
    sb.append("  amountView: ").append(amountView).append("\n");
    sb.append("  account: ").append(account).append("\n");
    sb.append("  bankName: ").append(bankName).append("\n");
    sb.append("  realname: ").append(realname).append("\n");
    sb.append("  comment: ").append(comment).append("\n");
    sb.append("  withdrawingTime: ").append(withdrawingTime).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}