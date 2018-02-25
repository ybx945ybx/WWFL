package com.haitao.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 在这里创建所需要的各种表，并进行表的版本更新。
 * 
 * @author 
 */
public class DbHelper extends SQLiteOpenHelper {
    // 数据库名
    public static final String DATABASE_NAME = "55haitao.db";
    public static int DATABASE_VERSION = 5;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private void createTable(SQLiteDatabase db) {
        StringBuffer sbSearch = new StringBuffer();
        sbSearch.append("CREATE TABLE IF NOT EXISTS ");
        sbSearch.append(DBConstant.SEARCH_TABLE);
        sbSearch.append("(");
        sbSearch.append(DBConstant.ID + " integer primary key,");
        sbSearch.append(DBConstant.SEARCH_NAME + " TEXT NULL,");
        sbSearch.append(DBConstant.UPDATE_TIME + " TEXT NULL");
        sbSearch.append(")");
        db.execSQL(sbSearch.toString());
        StringBuffer sbProvince = new StringBuffer();
        sbProvince.append("CREATE TABLE IF NOT EXISTS ");
        sbProvince.append(DBConstant.PROVINCE_TABLE);
        sbProvince.append("(");
        sbProvince.append(DBConstant.ID + " integer primary key,");
        sbProvince.append(DBConstant.PROVINCE_ID + " TEXT NULL,");
        sbProvince.append(DBConstant.PROVINCE_NAME + " TEXT NULL,");
        sbProvince.append(DBConstant.UPDATE_TIME + " TEXT NULL");
        sbProvince.append(")");
        db.execSQL(sbProvince.toString());
        StringBuffer sbCity = new StringBuffer();
        sbCity.append("CREATE TABLE IF NOT EXISTS ");
        sbCity.append(DBConstant.CITY_TABLE);
        sbCity.append("(");
        sbCity.append(DBConstant.ID + " integer primary key,");
        sbCity.append(DBConstant.CITY_ID + " TEXT NULL,");
        sbCity.append(DBConstant.CITY_NAME + " TEXT NULL,");
        sbCity.append(DBConstant.CITY_PID + " TEXT NULL,");
        sbCity.append(DBConstant.UPDATE_TIME + " TEXT NULL");
        sbCity.append(")");
        db.execSQL(sbCity.toString());
        //分类
        StringBuffer sbCategory = new StringBuffer();
        sbCategory.append("CREATE TABLE IF NOT EXISTS ");
        sbCategory.append(DBConstant.CATEGORY_TABLE);
        sbCategory.append("(");
        sbCategory.append(DBConstant.ID + " integer primary key,");
        sbCategory.append(DBConstant.CATEGORY_ID + " TEXT NULL,");
        sbCategory.append(DBConstant.CATEGORY_TEXT + " TEXT NULL,");
        sbCategory.append(DBConstant.CATEGORY_BRANCH + " TEXT NULL,");
        sbCategory.append(DBConstant.CATEGORY_TYPE + " TEXT NULL,");
        sbCategory.append(DBConstant.CATEGORY_PIC + " TEXT NULL,");
        sbCategory.append(DBConstant.UPDATE_TIME + " TEXT NULL");
        sbCategory.append(")");
        db.execSQL(sbCategory.toString());
        //商家
        StringBuffer sbStore = new StringBuffer();
        sbStore.append("CREATE TABLE IF NOT EXISTS ");
        sbStore.append(DBConstant.STORE_TABLE);
        sbStore.append("(");
        sbStore.append(DBConstant.ID + " integer primary key,");
        sbStore.append(DBConstant.STORE_ID + " TEXT NULL,");
        sbStore.append(DBConstant.STORE_NAME + " TEXT NULL,");
        sbStore.append(DBConstant.STORE_COUNTRY_ID + " TEXT NULL,");
        sbStore.append(DBConstant.STORE_COUNTRY_NAME + " TEXT NULL,");
        sbStore.append(DBConstant.STORE_COUNTRY_PIC + " TEXT NULL,");
        sbStore.append(DBConstant.STORE_CASHBACK + " TEXT NULL,");
        sbStore.append(DBConstant.STORE_CASHBACK_VIEW + " TEXT NULL,");
        sbStore.append(DBConstant.STORE_IS_ALIPAY + " TEXT NULL,");
        sbStore.append(DBConstant.STORE_IS_TRANSPORTS + " TEXT NULL,");
        sbStore.append(DBConstant.STORE_IS_DIRECT_MAIL + " TEXT NULL,");
        sbStore.append(DBConstant.STORE_IS_SUPER_REBATE + " TEXT NULL,");
        sbStore.append(DBConstant.STORE_CHAR + " TEXT NULL,");
        sbStore.append(DBConstant.STORE_CATEGORY + " TEXT NULL,");
        sbStore.append(DBConstant.UPDATE_TIME + " TEXT NULL");
        sbStore.append(")");
        db.execSQL(sbStore.toString());
        //版块
        StringBuffer sbSection = new StringBuffer();
        sbSection.append("CREATE TABLE IF NOT EXISTS ");
        sbSection.append(DBConstant.SECTION_TABLE);
        sbSection.append("(");
        sbSection.append(DBConstant.ID + " integer primary key,");
        sbSection.append(DBConstant.SECTION_ID + " TEXT NULL,");
        sbSection.append(DBConstant.SECTION_P_ID + " TEXT NULL,");
        sbSection.append(DBConstant.SECTION_NAME + " TEXT NULL,");
        sbSection.append(DBConstant.SECTION_POSTS + " TEXT NULL,");
        sbSection.append(DBConstant.SECTION_THREADS + " TEXT NULL,");
        sbSection.append(DBConstant.SECTION_TODAYPOSTS + " TEXT NULL,");
        sbSection.append(DBConstant.SECTION_ICON + " TEXT NULL,");
        sbSection.append(DBConstant.UPDATE_TIME + " TEXT NULL");
        sbSection.append(")");
        db.execSQL(sbSection.toString());
        //转运公司
        StringBuffer sbTransfer = new StringBuffer();
        sbTransfer.append("CREATE TABLE IF NOT EXISTS ");
        sbTransfer.append(DBConstant.TRANSFER_TABLE);
        sbTransfer.append("(");
        sbTransfer.append(DBConstant.ID + " integer primary key,");
        sbTransfer.append(DBConstant.TRANSFER_ID + " TEXT NULL,");
        sbTransfer.append(DBConstant.TRANSFER_NAME + " TEXT NULL,");
        sbTransfer.append(DBConstant.UPDATE_TIME + " TEXT NULL");
        sbTransfer.append(")");
        db.execSQL(sbTransfer.toString());
        StringBuffer sbTransport = new StringBuffer();
        sbTransport.append("CREATE TABLE IF NOT EXISTS ");
        sbTransport.append(DBConstant.TRANSPORT_TABLE);
        sbTransport.append("(");
        sbTransport.append(DBConstant.ID + " integer primary key,");
        sbTransport.append(DBConstant.TRANSPORT_ID + " TEXT NULL,");
        sbTransport.append(DBConstant.TRANSPORT_NAME + " TEXT NULL,");
        sbTransport.append(DBConstant.TRANSPORT_LOGO + " TEXT NULL,");
        sbTransport.append(DBConstant.TRANSPORT_FORUMID + " TEXT NULL,");
        sbTransport.append(DBConstant.TRANSPORT_COLLECT_COUNT + " TEXT NULL,");
        sbTransport.append(DBConstant.TRANSPORT_THREAD_COUNT + " TEXT NULL,");
        sbTransport.append(DBConstant.TRANSPORT_STAR_NUM + " TEXT NULL,");
        sbTransport.append(DBConstant.TRANSPORT_CHAR + " TEXT NULL,");
        sbTransport.append(DBConstant.TRANSPORT_COUNTRY_ID + " TEXT NULL,");
        sbTransport.append(DBConstant.UPDATE_TIME + " TEXT NULL");
        sbTransport.append(")");
        db.execSQL(sbTransport.toString());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	if(newVersion > oldVersion){
//            db.execSQL("drop table if exists " + DBConstant.SEARCH_TABLE);
        }
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

}