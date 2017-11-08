package com.fishing.namtran.fishingmanagerservice.dbconnection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Nam Tran on 10/30/2017.
 */

public class KeepFishingManager {

    private SQLiteDatabase db;
    private Context context;

    public KeepFishingManager(Context context) {
        this.context = context;
    }

    public long createKeepFishingEntry(long mCustomerId, int mKeepHours, int mNoKeepHours, int mKeepFish, int mTakeFish, int mTotalFish, String mNote) {

        InitializeDatabase mDbHelper = new InitializeDatabase(context);
        db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(KeepFishing.Properties.CUSTOMER_ID, mCustomerId);
        values.put(KeepFishing.Properties.KEEP_HOURS, mKeepHours);
        values.put(KeepFishing.Properties.NO_KEEP_HOURS, mNoKeepHours);
        values.put(KeepFishing.Properties.KEEP_FISH, mKeepFish);
        values.put(KeepFishing.Properties.TAKE_FISH, mTakeFish);
        values.put(KeepFishing.Properties.TOTAL_FISH, mTotalFish);
        values.put(KeepFishing.Properties.NOTE, mNote);

        // Insert the new row, returning the primary key value of the new row
        long rowId = db.insert(KeepFishing.Properties.TABLE_NAME, null, values);

        //close connection
        db.close();

        return rowId;
    }
}
