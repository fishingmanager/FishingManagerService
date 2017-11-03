package com.fishing.namtran.fishingmanagerservice.dbconnection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nam.tran on 10/18/2017.
 */

public class CustomerDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + Customer.Properties.TABLE_NAME + " (" +
                    Customer.Properties._ID + " INTEGER PRIMARY KEY," +
                    Customer.Properties.FULLNAME + " TEXT," +
                    Customer.Properties.MOBILE + " TEXT," +
                    Customer.Properties.EMAIL + " TEXT," +
                    Customer.Properties.GENDER + " INTEGER," +
                    Customer.Properties.AGE + " INTEGER," +
                    Customer.Properties.ADDRESS + " TEXT," +
                    Customer.Properties.NOTE + " TEXT)";

    private static final String SQL_CREATE_RECORDS =
            "INSERT INTO " + User.Properties.TABLE_NAME + " (" +
                    Customer.Properties._ID + "," +
                    Customer.Properties.FULLNAME + "," +
                    Customer.Properties.MOBILE + "," +
                    Customer.Properties.EMAIL + "," +
                    Customer.Properties.GENDER + "," +
                    Customer.Properties.AGE + "," +
                    Customer.Properties.ADDRESS + "," +
                    Customer.Properties.NOTE + ") VALUES ( 1, 'Tran Xuan Nam', '0909686767', 'hienmong2002@gmail.com', 1, 36, '003 Pham Van Bach, Go Vap, HCM', 'Nguoi lap trinh android')";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + User.Properties.TABLE_NAME;

    public CustomerDbHelper(Context context) {
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
