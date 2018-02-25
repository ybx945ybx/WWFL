/**
 * 五五海淘返利APP新版接口
 * 更新日志<br> 相对于上一build的变更： <br/> 商家详情接口增加store_info_url
 * <p>
 * OpenAPI spec version: 1.7 build20171207-7
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
public class EnteredStoreModelStoreList extends BaseStoreListObject {

    @SerializedName("store_id")
    private String storeId              = null;
    @SerializedName("store_name")
    private String storeName            = null;
    @SerializedName("category_name")
    private String categoryName         = null;
    @SerializedName("store_logo")
    private String storeLogo            = null;
    @SerializedName("country_flag_pic")
    private String countryFlagPic       = null;
    @SerializedName("rebate_view")
    private String rebateView           = null;
    @SerializedName("rebate_influence_view")
    private String rebateInfluenceView  = null;
    @SerializedName("collections_count_view")
    private String collectionsCountView = null;
    @SerializedName("alipay_supported")
    private String alipaySupported      = null;
    @SerializedName("direct_post_supported")
    private String directPostSupported  = null;
    @SerializedName("cn_web_supported")
    private String cnWebSupported       = null;

    /**
     * 商家ID
     **/
    @ApiModelProperty(value = "商家ID")
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
     * 商家类别名称
     **/
    @ApiModelProperty(value = "商家类别名称")
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * 商家LOGO
     **/
    @ApiModelProperty(value = "商家LOGO")
    public String getStoreLogo() {
        return storeLogo;
    }

    public void setStoreLogo(String storeLogo) {
        this.storeLogo = storeLogo;
    }

    /**
     * 国旗图片地址
     **/
    @ApiModelProperty(value = "国旗图片地址")
    public String getCountryFlagPic() {
        return countryFlagPic;
    }

    public void setCountryFlagPic(String countryFlagPic) {
        this.countryFlagPic = countryFlagPic;
    }

    /**
     * 返利信息（文字说明）
     **/
    @ApiModelProperty(value = "返利信息（文字说明）")
    public String getRebateView() {
        return rebateView;
    }

    public void setRebateView(String rebateView) {
        this.rebateView = rebateView;
    }

    /**
     * 获得返利的用户数（文字说明）
     **/
    @ApiModelProperty(value = "获得返利的用户数（文字说明）")
    public String getRebateInfluenceView() {
        return rebateInfluenceView;
    }

    public void setRebateInfluenceView(String rebateInfluenceView) {
        this.rebateInfluenceView = rebateInfluenceView;
    }

    /**
     * 收藏的用户数（文字说明）
     **/
    @ApiModelProperty(value = "收藏的用户数（文字说明）")
    public String getCollectionsCountView() {
        return collectionsCountView;
    }

    public void setCollectionsCountView(String collectionsCountView) {
        this.collectionsCountView = collectionsCountView;
    }

    /**
     * 是否支持支付宝
     **/
    @ApiModelProperty(value = "是否支持支付宝")
    public String getAlipaySupported() {
        return alipaySupported;
    }

    public void setAlipaySupported(String alipaySupported) {
        this.alipaySupported = alipaySupported;
    }

    /**
     * 是否支持直邮中国
     **/
    @ApiModelProperty(value = "是否支持直邮中国")
    public String getDirectPostSupported() {
        return directPostSupported;
    }

    public void setDirectPostSupported(String directPostSupported) {
        this.directPostSupported = directPostSupported;
    }

    /**
     * 是否支持中文页面
     **/
    @ApiModelProperty(value = "是否支持中文页面")
    public String getCnWebSupported() {
        return cnWebSupported;
    }

    public void setCnWebSupported(String cnWebSupported) {
        this.cnWebSupported = cnWebSupported;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EnteredStoreModelStoreList enteredStoreModel = (EnteredStoreModelStoreList) o;
        return (storeId == null ? enteredStoreModel.storeId == null : storeId.equals(enteredStoreModel.storeId)) &&
                (storeName == null ? enteredStoreModel.storeName == null : storeName.equals(enteredStoreModel.storeName)) &&
                (categoryName == null ? enteredStoreModel.categoryName == null : categoryName.equals(enteredStoreModel.categoryName)) &&
                (storeLogo == null ? enteredStoreModel.storeLogo == null : storeLogo.equals(enteredStoreModel.storeLogo)) &&
                (countryFlagPic == null ? enteredStoreModel.countryFlagPic == null : countryFlagPic.equals(enteredStoreModel.countryFlagPic)) &&
                (rebateView == null ? enteredStoreModel.rebateView == null : rebateView.equals(enteredStoreModel.rebateView)) &&
                (rebateInfluenceView == null ? enteredStoreModel.rebateInfluenceView == null : rebateInfluenceView.equals(enteredStoreModel.rebateInfluenceView)) &&
                (collectionsCountView == null ? enteredStoreModel.collectionsCountView == null : collectionsCountView.equals(enteredStoreModel.collectionsCountView)) &&
                (alipaySupported == null ? enteredStoreModel.alipaySupported == null : alipaySupported.equals(enteredStoreModel.alipaySupported)) &&
                (directPostSupported == null ? enteredStoreModel.directPostSupported == null : directPostSupported.equals(enteredStoreModel.directPostSupported)) &&
                (cnWebSupported == null ? enteredStoreModel.cnWebSupported == null : cnWebSupported.equals(enteredStoreModel.cnWebSupported));
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (storeId == null ? 0 : storeId.hashCode());
        result = 31 * result + (storeName == null ? 0 : storeName.hashCode());
        result = 31 * result + (categoryName == null ? 0 : categoryName.hashCode());
        result = 31 * result + (storeLogo == null ? 0 : storeLogo.hashCode());
        result = 31 * result + (countryFlagPic == null ? 0 : countryFlagPic.hashCode());
        result = 31 * result + (rebateView == null ? 0 : rebateView.hashCode());
        result = 31 * result + (rebateInfluenceView == null ? 0 : rebateInfluenceView.hashCode());
        result = 31 * result + (collectionsCountView == null ? 0 : collectionsCountView.hashCode());
        result = 31 * result + (alipaySupported == null ? 0 : alipaySupported.hashCode());
        result = 31 * result + (directPostSupported == null ? 0 : directPostSupported.hashCode());
        result = 31 * result + (cnWebSupported == null ? 0 : cnWebSupported.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class EnteredStoreModelStoreList {\n");

        sb.append("  storeId: ").append(storeId).append("\n");
        sb.append("  storeName: ").append(storeName).append("\n");
        sb.append("  categoryName: ").append(categoryName).append("\n");
        sb.append("  storeLogo: ").append(storeLogo).append("\n");
        sb.append("  countryFlagPic: ").append(countryFlagPic).append("\n");
        sb.append("  rebateView: ").append(rebateView).append("\n");
        sb.append("  rebateInfluenceView: ").append(rebateInfluenceView).append("\n");
        sb.append("  collectionsCountView: ").append(collectionsCountView).append("\n");
        sb.append("  alipaySupported: ").append(alipaySupported).append("\n");
        sb.append("  directPostSupported: ").append(directPostSupported).append("\n");
        sb.append("  cnWebSupported: ").append(cnWebSupported).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
