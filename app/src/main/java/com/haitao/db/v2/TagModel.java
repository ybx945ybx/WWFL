package com.haitao.db.v2;


import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by apple on 16/9/28.
 */
public class TagModel extends DataSupport {

    @Column(unique = true, defaultValue = "unknown")
    private long id;

    @Column(unique = true, defaultValue = "unknown")
    private String name;


}
