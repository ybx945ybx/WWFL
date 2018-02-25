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

import io.swagger.client.model.DealModel;
import io.swagger.client.model.In24hDealsListModelDataCategories;
import java.util.*;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class In24hDealsListModelData  {
  
  @SerializedName("almost_expire_checked")
  private String almostExpireChecked = null;
  @SerializedName("transshipping_supported_checked")
  private String transshippingSupportedChecked = null;
  @SerializedName("alipay_supported_checked")
  private String alipaySupportedChecked = null;
  @SerializedName("categories")
  private List<In24hDealsListModelDataCategories> categories = null;
  @SerializedName("page_num")
  private String pageNum = null;
  @SerializedName("page_size")
  private String pageSize = null;
  @SerializedName("page_length")
  private String pageLength = null;
  @SerializedName("has_more")
  private String hasMore = null;
  @SerializedName("rows")
  private List<DealModel> rows = null;

  /**
   * 是否选中了即将过期 0：未选中 1：选中
   **/
  @ApiModelProperty(value = "是否选中了即将过期 0：未选中 1：选中")
  public String getAlmostExpireChecked() {
    return almostExpireChecked;
  }
  public void setAlmostExpireChecked(String almostExpireChecked) {
    this.almostExpireChecked = almostExpireChecked;
  }

  /**
   * 是否选中了支持转运 0：未选中 1：选中
   **/
  @ApiModelProperty(value = "是否选中了支持转运 0：未选中 1：选中")
  public String getTransshippingSupportedChecked() {
    return transshippingSupportedChecked;
  }
  public void setTransshippingSupportedChecked(String transshippingSupportedChecked) {
    this.transshippingSupportedChecked = transshippingSupportedChecked;
  }

  /**
   * 是否选中了支持支付宝 0：未选中 1：选中
   **/
  @ApiModelProperty(value = "是否选中了支持支付宝 0：未选中 1：选中")
  public String getAlipaySupportedChecked() {
    return alipaySupportedChecked;
  }
  public void setAlipaySupportedChecked(String alipaySupportedChecked) {
    this.alipaySupportedChecked = alipaySupportedChecked;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<In24hDealsListModelDataCategories> getCategories() {
    return categories;
  }
  public void setCategories(List<In24hDealsListModelDataCategories> categories) {
    this.categories = categories;
  }

  /**
   * 页码
   **/
  @ApiModelProperty(value = "页码")
  public String getPageNum() {
    return pageNum;
  }
  public void setPageNum(String pageNum) {
    this.pageNum = pageNum;
  }

  /**
   * 每页显示的行数
   **/
  @ApiModelProperty(value = "每页显示的行数")
  public String getPageSize() {
    return pageSize;
  }
  public void setPageSize(String pageSize) {
    this.pageSize = pageSize;
  }

  /**
   * 每页实际有多少行数据
   **/
  @ApiModelProperty(value = "每页实际有多少行数据")
  public String getPageLength() {
    return pageLength;
  }
  public void setPageLength(String pageLength) {
    this.pageLength = pageLength;
  }

  /**
   * 是否有更多数据
   **/
  @ApiModelProperty(value = "是否有更多数据")
  public String getHasMore() {
    return hasMore;
  }
  public void setHasMore(String hasMore) {
    this.hasMore = hasMore;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<DealModel> getRows() {
    return rows;
  }
  public void setRows(List<DealModel> rows) {
    this.rows = rows;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    In24hDealsListModelData in24hDealsListModelData = (In24hDealsListModelData) o;
    return (almostExpireChecked == null ? in24hDealsListModelData.almostExpireChecked == null : almostExpireChecked.equals(in24hDealsListModelData.almostExpireChecked)) &&
        (transshippingSupportedChecked == null ? in24hDealsListModelData.transshippingSupportedChecked == null : transshippingSupportedChecked.equals(in24hDealsListModelData.transshippingSupportedChecked)) &&
        (alipaySupportedChecked == null ? in24hDealsListModelData.alipaySupportedChecked == null : alipaySupportedChecked.equals(in24hDealsListModelData.alipaySupportedChecked)) &&
        (categories == null ? in24hDealsListModelData.categories == null : categories.equals(in24hDealsListModelData.categories)) &&
        (pageNum == null ? in24hDealsListModelData.pageNum == null : pageNum.equals(in24hDealsListModelData.pageNum)) &&
        (pageSize == null ? in24hDealsListModelData.pageSize == null : pageSize.equals(in24hDealsListModelData.pageSize)) &&
        (pageLength == null ? in24hDealsListModelData.pageLength == null : pageLength.equals(in24hDealsListModelData.pageLength)) &&
        (hasMore == null ? in24hDealsListModelData.hasMore == null : hasMore.equals(in24hDealsListModelData.hasMore)) &&
        (rows == null ? in24hDealsListModelData.rows == null : rows.equals(in24hDealsListModelData.rows));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (almostExpireChecked == null ? 0: almostExpireChecked.hashCode());
    result = 31 * result + (transshippingSupportedChecked == null ? 0: transshippingSupportedChecked.hashCode());
    result = 31 * result + (alipaySupportedChecked == null ? 0: alipaySupportedChecked.hashCode());
    result = 31 * result + (categories == null ? 0: categories.hashCode());
    result = 31 * result + (pageNum == null ? 0: pageNum.hashCode());
    result = 31 * result + (pageSize == null ? 0: pageSize.hashCode());
    result = 31 * result + (pageLength == null ? 0: pageLength.hashCode());
    result = 31 * result + (hasMore == null ? 0: hasMore.hashCode());
    result = 31 * result + (rows == null ? 0: rows.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class In24hDealsListModelData {\n");
    
    sb.append("  almostExpireChecked: ").append(almostExpireChecked).append("\n");
    sb.append("  transshippingSupportedChecked: ").append(transshippingSupportedChecked).append("\n");
    sb.append("  alipaySupportedChecked: ").append(alipaySupportedChecked).append("\n");
    sb.append("  categories: ").append(categories).append("\n");
    sb.append("  pageNum: ").append(pageNum).append("\n");
    sb.append("  pageSize: ").append(pageSize).append("\n");
    sb.append("  pageLength: ").append(pageLength).append("\n");
    sb.append("  hasMore: ").append(hasMore).append("\n");
    sb.append("  rows: ").append(rows).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
