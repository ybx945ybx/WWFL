/**
 * 五五海淘返利APP新版接口
 * 更新日志<br> 相对于上一build的变更： <br/> APP5.5接口 
 *
 * OpenAPI spec version: 1.4 build20170919-2
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

import io.swagger.client.model.AppMenuWidgetModel;
import java.util.*;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class AppMenuModel  {
  
  @SerializedName("menu_key")
  private String menuKey = null;
  @SerializedName("widgets")
  private List<AppMenuWidgetModel> widgets = null;

  /**
   * 唯一标识
   **/
  @ApiModelProperty(value = "唯一标识")
  public String getMenuKey() {
    return menuKey;
  }
  public void setMenuKey(String menuKey) {
    this.menuKey = menuKey;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<AppMenuWidgetModel> getWidgets() {
    return widgets;
  }
  public void setWidgets(List<AppMenuWidgetModel> widgets) {
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
    AppMenuModel appMenuModel = (AppMenuModel) o;
    return (menuKey == null ? appMenuModel.menuKey == null : menuKey.equals(appMenuModel.menuKey)) &&
        (widgets == null ? appMenuModel.widgets == null : widgets.equals(appMenuModel.widgets));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (menuKey == null ? 0: menuKey.hashCode());
    result = 31 * result + (widgets == null ? 0: widgets.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class AppMenuModel {\n");
    
    sb.append("  menuKey: ").append(menuKey).append("\n");
    sb.append("  widgets: ").append(widgets).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
