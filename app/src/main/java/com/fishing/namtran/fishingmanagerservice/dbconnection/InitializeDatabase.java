package com.fishing.namtran.fishingmanagerservice.dbconnection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nam.tran on 10/18/2017.
 */

public class InitializeDatabase extends SQLiteOpenHelper {

    private static final String SQL_CREATE_SETTINGS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + Settings.Properties.TABLE_NAME + " (" +
                    Settings.Properties._ID + " INTEGER PRIMARY KEY," +
                    Settings.Properties.PACKAGE_FISHING + " INTEGER," +
                    Settings.Properties.PRICE_FISHING + " INTEGER," +
                    Settings.Properties.PRICE_FEED_TYPE + " INTEGER); ";

    private static final String SQL_CREATE_USERS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + User.Properties.TABLE_NAME + " (" +
                    User.Properties._ID + " INTEGER PRIMARY KEY," +
                    User.Properties.ROLE + " INTEGER," +
                    User.Properties.EMAIL + " TEXT," +
                    User.Properties.PASSWORD + " TEXT); ";

    private static final String SQL_CREATE_FISHINGS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + Fishings.Properties.TABLE_NAME + " (" +
                    Fishings.Properties._ID + " INTEGER PRIMARY KEY," +
                    Fishings.Properties.CUSTOMER_ID + " INTEGER," +
                    Fishings.Properties.DATE_IN + " DATETIME," +
                    Fishings.Properties.DATE_OUT + " DATETIME," +
                    Fishings.Properties.FEED_TYPE + " INTEGER," +
                    Fishings.Properties.NOTE + " TEXT); ";

    private static final String SQL_CREATE_KEEP_FISHINGS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + KeepFishing.Properties.TABLE_NAME + " (" +
                    KeepFishing.Properties._ID + " INTEGER PRIMARY KEY," +
                    KeepFishing.Properties.CUSTOMER_ID + " INTEGER," +
                    KeepFishing.Properties.KEEP_HOURS + " INTEGER," +
                    KeepFishing.Properties.NO_KEEP_HOURS + " INTEGER," +
                    KeepFishing.Properties.KEEP_FISH + " INTEGER," +
                    KeepFishing.Properties.TAKE_FISH + " INTEGER," +
                    KeepFishing.Properties.TOTAL_FISH + " INTEGER," +
                    KeepFishing.Properties.NOTE + " TEXT); ";

    private static final String SQL_CREATE_CUSTOMERS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + Customers.Properties.TABLE_NAME + " (" +
                    Customers.Properties._ID + " INTEGER PRIMARY KEY," +
                    Customers.Properties.FULLNAME + " TEXT," +
                    Customers.Properties.MOBILE + " TEXT); ";

    /*---------------- Insert sample records -----------------------*/

    private static final String SQL_CREATE_USERS_RECORDS =
            "INSERT INTO " + User.Properties.TABLE_NAME + " (" +
                    User.Properties._ID + "," +
                    User.Properties.ROLE + "," +
                    User.Properties.EMAIL + "," +
                    User.Properties.PASSWORD + ") VALUES ( 1, 0, 'nam@gmail.com', '12341234'); ";

    private static final String SQL_CREATE_SETTINGS_RECORDS =
            "INSERT INTO " + Settings.Properties.TABLE_NAME + " (" +
                    Settings.Properties._ID + "," +
                    Settings.Properties.PACKAGE_FISHING + "," +
                    Settings.Properties.PRICE_FISHING + "," +
                    Settings.Properties.PRICE_FEED_TYPE + ") VALUES ( 1, 4, 200000, 15000 ); ";

    private static final String SQL_CREATE_CUSTOMERS_RECORDS =
            "INSERT INTO " + Customers.Properties.TABLE_NAME + " (" +
                    Customers.Properties._ID + "," +
                    Customers.Properties.FULLNAME + "," +
                    Customers.Properties.MOBILE + ") VALUES ( 1, 'Anh Nam', '0909686767' ); ";

    private static final String SQL_CREATE_FISHINGS_RECORDS =
            "INSERT INTO " + Fishings.Properties.TABLE_NAME + " (" +
                    Fishings.Properties._ID + "," +
                    Fishings.Properties.CUSTOMER_ID + "," +
                    Fishings.Properties.DATE_IN + "," +
                    Fishings.Properties.DATE_OUT + "," +
                    Fishings.Properties.FEED_TYPE + "," +
                    Fishings.Properties.NOTE + ") VALUES ( 1, 1, '2017/11/06 08:00:59', '2017/11/06 10:00:59', '15001', 'Khong co' ); ";

    private static final String SQL_CREATE_KEEP_FISHINGS_RECORDS =
            "INSERT INTO " + KeepFishing.Properties.TABLE_NAME + " (" +
                    KeepFishing.Properties._ID + "," +
                    KeepFishing.Properties.CUSTOMER_ID + "," +
                    KeepFishing.Properties.KEEP_HOURS + "," +
                    KeepFishing.Properties.NO_KEEP_HOURS + "," +
                    KeepFishing.Properties.KEEP_FISH + "," +
                    KeepFishing.Properties.TAKE_FISH + "," +
                    KeepFishing.Properties.TOTAL_FISH + "," +
                    KeepFishing.Properties.NOTE + ") VALUES ( 1, 1, 0, 0, 0, 0, 0, 'Moi bat dau cau' ); ";

    /*---------------- Insert sample records -----------------------*/

    private static final String SQL_DELETE_USERS_TABLE =
            "DROP TABLE IF EXISTS " + User.Properties.TABLE_NAME;

    private static final String SQL_DELETE_SETTINGS_TABLE =
            "DROP TABLE IF EXISTS " + Settings.Properties.TABLE_NAME;

    public InitializeDatabase(Context context) {
        super(context, DbConfig.DATABASE_NAME, null, DbConfig.DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_SETTINGS_TABLE);
        db.execSQL(SQL_CREATE_USERS_TABLE);
        db.execSQL(SQL_CREATE_FISHINGS_TABLE);
        db.execSQL(SQL_CREATE_KEEP_FISHINGS_TABLE);
        db.execSQL(SQL_CREATE_CUSTOMERS_TABLE);

        db.execSQL(SQL_CREATE_USERS_RECORDS);
        db.execSQL(SQL_CREATE_SETTINGS_RECORDS);
        db.execSQL(SQL_CREATE_CUSTOMERS_RECORDS);
        db.execSQL(SQL_CREATE_FISHINGS_RECORDS);
        db.execSQL(SQL_CREATE_KEEP_FISHINGS_RECORDS);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_USERS_TABLE + SQL_DELETE_SETTINGS_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
