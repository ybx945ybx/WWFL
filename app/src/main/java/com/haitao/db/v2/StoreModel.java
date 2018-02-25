package com.haitao.db.v2;


import com.alibaba.fastjson.JSON;
import com.haitao.db.DBConstant;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by apple on 16/9/28.
 */
public class StoreModel extends DataSupport {

    @Column(unique = true, defaultValue = "unknown")
    private long id;

    @Column(unique = true, defaultValue = "unknown")
    private String name;

    private String store_id;

    private String country_id;

    private String country_name;

    private String country_pic;

    private String cashback;

    private String cashback_view;

    private String is_alipay;

    private String is_transports;

    private String is_direct_mail;

    private String is_super_rebate;

    private String character;

    private String category;


    public String getCashback() {
        return cashback;
    }

    public void setCashback(String cashback) {
        this.cashback = cashback;
    }

    public String getCashback_view() {
        return cashback_view;
    }

    public void setCashback_view(String cashback_view) {
        this.cashback_view = cashback_view;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCountry_pic() {
        return country_pic;
    }

    public void setCountry_pic(String country_pic) {
        this.country_pic = country_pic;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
        setStore_id(String.valueOf(id));
    }

    public String getIs_alipay() {
        return is_alipay;
    }

    public void setIs_alipay(String is_alipay) {
        this.is_alipay = is_alipay;
    }

    public String getIs_direct_mail() {
        return is_direct_mail;
    }

    public void setIs_direct_mail(String is_direct_mail) {
        this.is_direct_mail = is_direct_mail;
    }

    public String getIs_super_rebate() {
        return is_super_rebate;
    }

    public void setIs_super_rebate(String is_super_rebate) {
        this.is_super_rebate = is_super_rebate;
    }

    public String getIs_transports() {
        return is_transports;
    }

    public void setIs_transports(String is_transports) {
        this.is_transports = is_transports;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }
}
