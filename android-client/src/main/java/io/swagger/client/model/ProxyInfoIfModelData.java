/**
 * 五五海淘返利APP新版接口
 * 更新日志<br> 相对于上一build的变更： <br/> APP5.4接口
 * <p>
 * OpenAPI spec version: 1.3 build20170814-1
 * <p>
 * <p>
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
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
public class ProxyInfoIfModelData {

    @SerializedName("ip_addr")
    private String ipAddr = null;
    @SerializedName("port")
    private String port   = null;

    /**
     * IP地址
     **/
    @ApiModelProperty(value = "IP地址")
    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    /**
     * 端口
     **/
    @ApiModelProperty(value = "端口")
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProxyInfoIfModelData proxyInfoIfModelData = (ProxyInfoIfModelData) o;
        return (ipAddr == null ? proxyInfoIfModelData.ipAddr == null : ipAddr.equals(proxyInfoIfModelData.ipAddr)) &&
                (port == null ? proxyInfoIfModelData.port == null : port.equals(proxyInfoIfModelData.port));
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (ipAddr == null ? 0 : ipAddr.hashCode());
        result = 31 * result + (port == null ? 0 : port.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ProxyInfoIfModelData {\n");

        sb.append("  ipAddr: ").append(ipAddr).append("\n");
        sb.append("  port: ").append(port).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
