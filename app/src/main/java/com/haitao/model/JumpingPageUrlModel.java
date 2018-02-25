package com.haitao.model;

/**
 * 跳转页url
 *
 * @author 陶声
 * @since 2017-08-22
 */

public class JumpingPageUrlModel {
    public String store_id;
    public String store_name;
    public String store_description;
    public String store_logo;
    public String rebate_view;
    public String rebate_instruction;
    public String card_supported;
    public String alipay_supported;
    public String paypal_supported;
    public String direct_post_supported;
    public String transshipping_supported;
    public String mobile_has_rebate;
    public String bounded_accessing;
    public String jump_url;
    public String jump_delay;

    @Override
    public String toString() {
        return "{" +
                "store_id='" + store_id + '\'' +
                ", store_name='" + store_name + '\'' +
                ", store_description='" + store_description + '\'' +
                ", store_logo='" + store_logo + '\'' +
                ", rebate_view='" + rebate_view + '\'' +
                ", rebate_instruction='" + rebate_instruction + '\'' +
                ", card_supported='" + card_supported + '\'' +
                ", alipay_supported='" + alipay_supported + '\'' +
                ", paypal_supported='" + paypal_supported + '\'' +
                ", direct_post_supported='" + direct_post_supported + '\'' +
                ", transshipping_supported='" + transshipping_supported + '\'' +
                ", mobile_has_rebate='" + mobile_has_rebate + '\'' +
                ", bounded_accessing='" + bounded_accessing + '\'' +
                ", jump_url='" + jump_url + '\'' +
                ", jump_delay='" + jump_delay + '\'' +
                '}';
    }
}
