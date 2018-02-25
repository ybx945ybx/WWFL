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

import io.swagger.client.model.DealIndexModelDataTodayTalkTopic;
import io.swagger.client.model.DealModel;
import io.swagger.client.model.DealsWithAdsModel;
import io.swagger.client.model.LinkWidgetModel;
import io.swagger.client.model.SlidePicModel;
import java.util.*;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class DealIndexModelData  {
  
  @SerializedName("slide_pics")
  private List<SlidePicModel> slidePics = null;
  @SerializedName("cross_bar_pics")
  private List<SlidePicModel> crossBarPics = null;
  @SerializedName("top_deals")
  private List<DealModel> topDeals = null;
  @SerializedName("specials")
  private List<SlidePicModel> specials = null;
  @SerializedName("entries")
  private List<LinkWidgetModel> entries = null;
  @SerializedName("today_talk_topic")
  private DealIndexModelDataTodayTalkTopic todayTalkTopic = null;
  @SerializedName("newest_deals")
  private DealsWithAdsModel newestDeals = null;

  /**
   * 首页轮播
   **/
  @ApiModelProperty(value = "首页轮播")
  public List<SlidePicModel> getSlidePics() {
    return slidePics;
  }
  public void setSlidePics(List<SlidePicModel> slidePics) {
    this.slidePics = slidePics;
  }

  /**
   * 首页横幅
   **/
  @ApiModelProperty(value = "首页横幅")
  public List<SlidePicModel> getCrossBarPics() {
    return crossBarPics;
  }
  public void setCrossBarPics(List<SlidePicModel> crossBarPics) {
    this.crossBarPics = crossBarPics;
  }

  /**
   * 置顶优惠
   **/
  @ApiModelProperty(value = "置顶优惠")
  public List<DealModel> getTopDeals() {
    return topDeals;
  }
  public void setTopDeals(List<DealModel> topDeals) {
    this.topDeals = topDeals;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<SlidePicModel> getSpecials() {
    return specials;
  }
  public void setSpecials(List<SlidePicModel> specials) {
    this.specials = specials;
  }

  /**
   * 首页快捷入口
   **/
  @ApiModelProperty(value = "首页快捷入口")
  public List<LinkWidgetModel> getEntries() {
    return entries;
  }
  public void setEntries(List<LinkWidgetModel> entries) {
    this.entries = entries;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public DealIndexModelDataTodayTalkTopic getTodayTalkTopic() {
    return todayTalkTopic;
  }
  public void setTodayTalkTopic(DealIndexModelDataTodayTalkTopic todayTalkTopic) {
    this.todayTalkTopic = todayTalkTopic;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public DealsWithAdsModel getNewestDeals() {
    return newestDeals;
  }
  public void setNewestDeals(DealsWithAdsModel newestDeals) {
    this.newestDeals = newestDeals;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DealIndexModelData dealIndexModelData = (DealIndexModelData) o;
    return (slidePics == null ? dealIndexModelData.slidePics == null : slidePics.equals(dealIndexModelData.slidePics)) &&
        (crossBarPics == null ? dealIndexModelData.crossBarPics == null : crossBarPics.equals(dealIndexModelData.crossBarPics)) &&
        (topDeals == null ? dealIndexModelData.topDeals == null : topDeals.equals(dealIndexModelData.topDeals)) &&
        (specials == null ? dealIndexModelData.specials == null : specials.equals(dealIndexModelData.specials)) &&
        (entries == null ? dealIndexModelData.entries == null : entries.equals(dealIndexModelData.entries)) &&
        (todayTalkTopic == null ? dealIndexModelData.todayTalkTopic == null : todayTalkTopic.equals(dealIndexModelData.todayTalkTopic)) &&
        (newestDeals == null ? dealIndexModelData.newestDeals == null : newestDeals.equals(dealIndexModelData.newestDeals));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (slidePics == null ? 0: slidePics.hashCode());
    result = 31 * result + (crossBarPics == null ? 0: crossBarPics.hashCode());
    result = 31 * result + (topDeals == null ? 0: topDeals.hashCode());
    result = 31 * result + (specials == null ? 0: specials.hashCode());
    result = 31 * result + (entries == null ? 0: entries.hashCode());
    result = 31 * result + (todayTalkTopic == null ? 0: todayTalkTopic.hashCode());
    result = 31 * result + (newestDeals == null ? 0: newestDeals.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class DealIndexModelData {\n");
    
    sb.append("  slidePics: ").append(slidePics).append("\n");
    sb.append("  crossBarPics: ").append(crossBarPics).append("\n");
    sb.append("  topDeals: ").append(topDeals).append("\n");
    sb.append("  specials: ").append(specials).append("\n");
    sb.append("  entries: ").append(entries).append("\n");
    sb.append("  todayTalkTopic: ").append(todayTalkTopic).append("\n");
    sb.append("  newestDeals: ").append(newestDeals).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}