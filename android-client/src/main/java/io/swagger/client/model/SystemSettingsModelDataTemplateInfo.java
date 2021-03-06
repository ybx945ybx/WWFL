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

import io.swagger.client.model.LinkWidgetModel;
import java.util.*;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class SystemSettingsModelDataTemplateInfo  {
  
  @SerializedName("key")
  private String key = null;
  @SerializedName("name")
  private String name = null;
  @SerializedName("style_version")
  private String styleVersion = null;
  @SerializedName("widgets")
  private List<LinkWidgetModel> widgets = null;

  /**
   * 模板标识符 default:APP默认模板 其它键名：活动模板
   **/
  @ApiModelProperty(value = "模板标识符 default:APP默认模板 其它键名：活动模板")
  public String getKey() {
    return key;
  }
  public void setKey(String key) {
    this.key = key;
  }

  /**
   * 模板名称
   **/
  @ApiModelProperty(value = "模板名称")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  /**
   * 风格版本号
   **/
  @ApiModelProperty(value = "风格版本号")
  public String getStyleVersion() {
    return styleVersion;
  }
  public void setStyleVersion(String styleVersion) {
    this.styleVersion = styleVersion;
  }

  /**
   * 模板全局控件 - rb_float_icon:APP右下角图标
   **/
  @ApiModelProperty(value = "模板全局控件 - rb_float_icon:APP右下角图标")
  public List<LinkWidgetModel> getWidgets() {
    return widgets;
  }
  public void setWidgets(List<LinkWidgetModel> widgets) {
    this.widgets = widgets;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SystemSettingsModelDataTemplateInfo systemSettingsModelDataTemplateInfo = (SystemSettingsModelDataTemplateInfo) o;
    return (key == null ? systemSettingsModelDataTemplateInfo.key == null : key.equals(systemSettingsModelDataTemplateInfo.key)) &&
        (name == null ? systemSettingsModelDataTemplateInfo.name == null : name.equals(systemSettingsModelDataTemplateInfo.name)) &&
        (styleVersion == null ? systemSettingsModelDataTemplateInfo.styleVersion == null : styleVersion.equals(systemSettingsModelDataTemplateInfo.styleVersion)) &&
        (widgets == null ? systemSettingsModelDataTemplateInfo.widgets == null : widgets.equals(systemSettingsModelDataTemplateInfo.widgets));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (key == null ? 0: key.hashCode());
    result = 31 * result + (name == null ? 0: name.hashCode());
    result = 31 * result + (styleVersion == null ? 0: styleVersion.hashCode());
    result = 31 * result + (widgets == null ? 0: widgets.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class SystemSettingsModelDataTemplateInfo {\n");
    
    sb.append("  key: ").append(key).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  styleVersion: ").append(styleVersion).append("\n");
    sb.append("  widgets: ").append(widgets).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
