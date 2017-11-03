package com.fishing.namtran.fishingmanagerservice.dbconnection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nam.tran on 10/18/2017.
 */

public class FishingDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + Fishing.Properties.TABLE_NAME + " (" +
                    Fishing.Properties._ID + " LONG PRIMARY KEY," +
                    Fishing.Properties.CUSTOMER_ID + " INTEGER," +
                    Fishing.Properties.DATE_IN + " DATETIME," +
                    Fishing.Properties.DATE_OUT + " DATETIME," +
                    Fishing.Properties.FEED_TYPE + " INTEGER," +
                    Fishing.Properties.KEEP_HOURS + " INTEGER," +
                    Fishing.Properties.NO_KEEP_HOURS + " INTEGER," +
                    Fishing.Properties.KEEP_FISH + " INTEGER," +
                    Fishing.Properties.TAKE_FISH + " INTEGER," +
                    Fishing.Properties.TOTAL_FISH + " INTEGER," +
                    Fishing.Properties.TOTAL_MONEY + " INTEGER," +
                    Fishing.Properties.NOTE + " TEXT)";

    private static final String SQL_CREATE_RECORDS =
            "INSERT INTO " + Fishing.Properties.TABLE_NAME + " (" +
                    Fishing.Properties._ID + "," +
                    Fishing.Properties.CUSTOMER_ID + "," +
                    Fishing.Properties.DATE_IN + "," +
                    Fishing.Properties.DATE_OUT + "," +
                    Fishing.Properties.FEED_TYPE + "," +
                    Fishing.Properties.KEEP_HOURS + "," +
                    Fishing.Properties.NO_KEEP_HOURS + "," +
                    Fishing.Properties.KEEP_FISH + "," +
                    Fishing.Properties.TAKE_FISH + "," +
                    Fishing.Properties.TOTAL_FISH + "," +
                    Fishing.Properties.TOTAL_MONEY + "," +
                    Fishing.Properties.NOTE + ") VALUES ( 1, 1, '2017/11/03 08:25:59', '2017/11/03 08:25:59', 2, 2, 1, 4, 2, 2, 300000, 'Con no ca')";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + User.Properties.TABLE_NAME;

    public FishingDbHelper(Context context) {
        super(context, DbConfig.DATABASE_NAME, null, DbConfig.DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_RECORDS);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
