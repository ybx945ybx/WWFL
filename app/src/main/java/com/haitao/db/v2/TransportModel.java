package com.haitao.db.v2;


import com.haitao.db.DBConstant;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by apple on 16/9/28.
 */
public class TransportModel extends DataSupport {

    @Column(unique = true, defaultValue = "unknown")
    private long id;

    @Column(unique = true, defaultValue = "unknown")
    private String name;

    private String transport_id;

    private String forumid;

    private String collection_count;

    private String logo;

    private String thread_count;

    private String start_number;

    private String character;

    private String country_id;


    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getCollection_count() {
        return collection_count;
    }

    public void setCollection_count(String collection_count) {
        this.collection_count = collection_count;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getForumid() {
        return forumid;
    }

    public void setForumid(String forumid) {
        this.forumid = forumid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
        setTransport_id(String.valueOf(id));
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart_number() {
        return start_number;
    }

    public void setStart_number(String start_number) {
        this.start_number = start_number;
    }

    public String getThread_count() {
        return thread_count;
    }

    public void setThread_count(String thread_count) {
        this.thread_count = thread_count;
    }

    public String getTransport_id() {
        return transport_id;
    }

    public void setTransport_id(String transport_id) {
        this.transport_id = transport_id;
    }
}
