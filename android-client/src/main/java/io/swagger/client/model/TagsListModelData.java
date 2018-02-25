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

import io.swagger.client.model.TagModel;
import java.util.*;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class TagsListModelData  {
  
  @SerializedName("page_num")
  private String pageNum = null;
  @SerializedName("page_size")
  private String pageSize = null;
  @SerializedName("page_length")
  private String pageLength = null;
  @SerializedName("has_more")
  private String hasMore = null;
  @SerializedName("rows")
  private List<TagModel> rows = null;

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
  public List<TagModel> getRows() {
    return rows;
  }
  public void setRows(List<TagModel> rows) {
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
    TagsListModelData tagsListModelData = (TagsListModelData) o;
    return (pageNum == null ? tagsListModelData.pageNum == null : pageNum.equals(tagsListModelData.pageNum)) &&
        (pageSize == null ? tagsListModelData.pageSize == null : pageSize.equals(tagsListModelData.pageSize)) &&
        (pageLength == null ? tagsListModelData.pageLength == null : pageLength.equals(tagsListModelData.pageLength)) &&
        (hasMore == null ? tagsListModelData.hasMore == null : hasMore.equals(tagsListModelData.hasMore)) &&
        (rows == null ? tagsListModelData.rows == null : rows.equals(tagsListModelData.rows));
  }

  @Override
  public int hashCode() {
    int result = 17;
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
    sb.append("class TagsListModelData {\n");
    
    sb.append("  pageNum: ").append(pageNum).append("\n");
    sb.append("  pageSize: ").append(pageSize).append("\n");
    sb.append("  pageLength: ").append(pageLength).append("\n");
    sb.append("  hasMore: ").append(hasMore).append("\n");
    sb.append("  rows: ").append(rows).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}