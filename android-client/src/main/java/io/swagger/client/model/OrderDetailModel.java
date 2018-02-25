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
public class OrderDetailModel  {
  
  @SerializedName("order_id")
  private String orderId = null;
  @SerializedName("order_number")
  private String orderNumber = null;
  @SerializedName("store_id")
  private String storeId = null;
  @SerializedName("store_name")
  private String storeName = null;
  @SerializedName("cost_view")
  private String costView = null;
  @SerializedName("rebate_view")
  private String rebateView = null;
  @SerializedName("status")
  private String status = null;
  @SerializedName("status_view")
  private String statusView = null;
  @SerializedName("comment")
  private String comment = null;
  @SerializedName("pics")
  private List<String> pics = null;
  @SerializedName("order_time")
  private String orderTime = null;
  @SerializedName("rebate_time")
  private String rebateTime = null;

  /**
   * 订单ID
   **/
  @ApiModelProperty(value = "订单ID")
  public String getOrderId() {
    return orderId;
  }
  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  /**
   * 订单编号
   **/
  @ApiModelProperty(value = "订单编号")
  public String getOrderNumber() {
    return orderNumber;
  }
  public void setOrderNumber(String orderNumber) {
    this.orderNumber = orderNumber;
  }

  /**
   * 商家名称
   **/
  @ApiModelProperty(value = "商家名称")
  public String getStoreId() {
    return storeId;
  }
  public void setStoreId(String storeId) {
    this.storeId = storeId;
  }

  /**
   * 商家名称
   **/
  @ApiModelProperty(value = "商家名称")
  public String getStoreName() {
    return storeName;
  }
  public void setStoreName(String storeName) {
    this.storeName = storeName;
  }

  /**
   * 消费金额（文字描述）
   **/
  @ApiModelProperty(value = "消费金额（文字描述）")
  public String getCostView() {
    return costView;
  }
  public void setCostView(String costView) {
    this.costView = costView;
  }

  /**
   * 返利金额（文字描述）
   **/
  @ApiModelProperty(value = "返利金额（文字描述）")
  public String getRebateView() {
    return rebateView;
  }
  public void setRebateView(String rebateView) {
    this.rebateView = rebateView;
  }

  /**
   * 订单状态 - 1：已生效 2：待生效 3：无效订单
   **/
  @ApiModelProperty(value = "订单状态 - 1：已生效 2：待生效 3：无效订单")
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * 订单状态文字描述
   **/
  @ApiModelProperty(value = "订单状态文字描述")
  public String getStatusView() {
    return statusView;
  }
  public void setStatusView(String statusView) {
    this.statusView = statusView;
  }

  /**
   * 订单备注信息
   **/
  @ApiModelProperty(value = "订单备注信息")
  public String getComment() {
    return comment;
  }
  public void setComment(String comment) {
    this.comment = comment;
  }

  /**
   * 订单图片
   **/
  @ApiModelProperty(value = "订单图片")
  public List<String> getPics() {
    return pics;
  }
  public void setPics(List<String> pics) {
    this.pics = pics;
  }

  /**
   * 下单时间
   **/
  @ApiModelProperty(value = "下单时间")
  public String getOrderTime() {
    return orderTime;
  }
  public void setOrderTime(String orderTime) {
    this.orderTime = orderTime;
  }

  /**
   * 返利时间
   **/
  @ApiModelProperty(value = "返利时间")
  public String getRebateTime() {
    return rebateTime;
  }
  public void setRebateTime(String rebateTime) {
    this.rebateTime = rebateTime;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderDetailModel orderDetailModel = (OrderDetailModel) o;
    return (orderId == null ? orderDetailModel.orderId == null : orderId.equals(orderDetailModel.orderId)) &&
        (orderNumber == null ? orderDetailModel.orderNumber == null : orderNumber.equals(orderDetailModel.orderNumber)) &&
        (storeId == null ? orderDetailModel.storeId == null : storeId.equals(orderDetailModel.storeId)) &&
        (storeName == null ? orderDetailModel.storeName == null : storeName.equals(orderDetailModel.storeName)) &&
        (costView == null ? orderDetailModel.costView == null : costView.equals(orderDetailModel.costView)) &&
        (rebateView == null ? orderDetailModel.rebateView == null : rebateView.equals(orderDetailModel.rebateView)) &&
        (status == null ? orderDetailModel.status == null : status.equals(orderDetailModel.status)) &&
        (statusView == null ? orderDetailModel.statusView == null : statusView.equals(orderDetailModel.statusView)) &&
        (comment == null ? orderDetailModel.comment == null : comment.equals(orderDetailModel.comment)) &&
        (pics == null ? orderDetailModel.pics == null : pics.equals(orderDetailModel.pics)) &&
        (orderTime == null ? orderDetailModel.orderTime == null : orderTime.equals(orderDetailModel.orderTime)) &&
        (rebateTime == null ? orderDetailModel.rebateTime == null : rebateTime.equals(orderDetailModel.rebateTime));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (orderId == null ? 0: orderId.hashCode());
    result = 31 * result + (orderNumber == null ? 0: orderNumber.hashCode());
    result = 31 * result + (storeId == null ? 0: storeId.hashCode());
    result = 31 * result + (storeName == null ? 0: storeName.hashCode());
    result = 31 * result + (costView == null ? 0: costView.hashCode());
    result = 31 * result + (rebateView == null ? 0: rebateView.hashCode());
    result = 31 * result + (status == null ? 0: status.hashCode());
    result = 31 * result + (statusView == null ? 0: statusView.hashCode());
    result = 31 * result + (comment == null ? 0: comment.hashCode());
    result = 31 * result + (pics == null ? 0: pics.hashCode());
    result = 31 * result + (orderTime == null ? 0: orderTime.hashCode());
    result = 31 * result + (rebateTime == null ? 0: rebateTime.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderDetailModel {\n");
    
    sb.append("  orderId: ").append(orderId).append("\n");
    sb.append("  orderNumber: ").append(orderNumber).append("\n");
    sb.append("  storeId: ").append(storeId).append("\n");
    sb.append("  storeName: ").append(storeName).append("\n");
    sb.append("  costView: ").append(costView).append("\n");
    sb.append("  rebateView: ").append(rebateView).append("\n");
    sb.append("  status: ").append(status).append("\n");
    sb.append("  statusView: ").append(statusView).append("\n");
    sb.append("  comment: ").append(comment).append("\n");
    sb.append("  pics: ").append(pics).append("\n");
    sb.append("  orderTime: ").append(orderTime).append("\n");
    sb.append("  rebateTime: ").append(rebateTime).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
