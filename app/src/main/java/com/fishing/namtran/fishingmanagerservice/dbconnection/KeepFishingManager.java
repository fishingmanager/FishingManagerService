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
        long keepFishingId = -1;

        if(!checkKeepFishingExisted(mCustomerId)) {
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
            keepFishingId = db.insert(KeepFishing.Properties.TABLE_NAME, null, values);
        }

        //close connection
        db.close();

        return keepFishingId;
    }

    public boolean checkKeepFishingExisted(long custId)
    {
        InitializeDatabase mDbHelper = new InitializeDatabase(context);
        db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                KeepFishing.Properties._ID,
                KeepFishing.Properties.CUSTOMER_ID,
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = KeepFishing.Properties.CUSTOMER_ID + " = ?";
        String[] selectionArgs = { Long.toString(custId) };

        Cursor cursor = db.query(
                KeepFishing.Properties.TABLE_NAME,              // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if(cursor.moveToNext()) {
            if(custId == cursor.getLong(cursor.getColumnIndexOrThrow(KeepFishing.Properties.CUSTOMER_ID))) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }

    public void updateKeepFishingEntry(String mCustomerId, String mKeepHours, String mNoKeepHours, String mKeepFish, String mTakeFish, String mTotalFish, String mFeeDoFish, String mNote) {
        InitializeDatabase mDbHelper = new InitializeDatabase(context);
        db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(KeepFishing.Properties.KEEP_HOURS, mKeepHours);
        values.put(KeepFishing.Properties.NO_KEEP_HOURS, mNoKeepHours);
        values.put(KeepFishing.Properties.KEEP_FISH, mKeepFish);
        values.put(KeepFishing.Properties.TAKE_FISH, mTakeFish);
        values.put(KeepFishing.Properties.TOTAL_FISH, mTotalFish);

        if(mFeeDoFish != null) {
            values.put(KeepFishing.Properties.FEE_DO_FISH, mFeeDoFish);
        }
        values.put(KeepFishing.Properties.NOTE, mNote);

        // Which row to update, based on the title
        String selection = KeepFishing.Properties.CUSTOMER_ID + " = ?";
        String[] selectionArgs = { mCustomerId };

        int count = db.update(
                KeepFishing.Properties.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        //close connection
        db.close();
        mDbHelper.close();
    }

    public long createLogsKeepFishingEntry(String mCustomerId, String mKeepFish, String mTakeFish, String mTotalFish, String mFeeDoFish, String mBuyFish, String mTotalMoney, String mStatus, String mNote, String datetime) {

        InitializeDatabase mDbHelper = new InitializeDatabase(context);
        db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(LogsKeepFishing.Properties.CUSTOMER_ID, mCustomerId);
        values.put(LogsKeepFishing.Properties.KEEP_FISH, mKeepFish);
        values.put(LogsKeepFishing.Properties.TAKE_FISH, mTakeFish);
        values.put(LogsKeepFishing.Properties.TOTAL_FISH, mTotalFish);
        values.put(LogsKeepFishing.Properties.FEE_DO_FISH, mFeeDoFish);
        values.put(LogsKeepFishing.Properties.BUY_FISH, mBuyFish);
        values.put(LogsKeepFishing.Properties.TOTAL_MONEY_BUY_FISH, mTotalMoney);
        values.put(LogsKeepFishing.Properties.STATUS, mStatus);
        values.put(LogsKeepFishing.Properties.DATE_TIME, datetime);
        values.put(LogsKeepFishing.Properties.NOTE, mNote);

        // Insert the new row, returning the primary key value of the new row
        long logKeepFishingId = db.insert(LogsKeepFishing.Properties.TABLE_NAME, null, values);

        //close connection
        db.close();
        mDbHelper.close();

        return logKeepFishingId;
    }

    public Cursor getLogsKeepFishing(String custId)
    {
        InitializeDatabase mDbHelper = new InitializeDatabase(context);
        db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                LogsKeepFishing.Properties._ID,
                LogsKeepFishing.Properties.CUSTOMER_ID,
                LogsKeepFishing.Properties.KEEP_FISH,
                LogsKeepFishing.Properties.TAKE_FISH,
                LogsKeepFishing.Properties.FEE_DO_FISH,
                LogsKeepFishing.Properties.TOTAL_FISH,
                LogsKeepFishing.Properties.BUY_FISH,
                LogsKeepFishing.Properties.TOTAL_MONEY_BUY_FISH,
                LogsKeepFishing.Properties.STATUS,
                LogsKeepFishing.Properties.DATE_TIME,
                LogsKeepFishing.Properties.NOTE,
        };

        String selection = LogsKeepFishing.Properties.CUSTOMER_ID + " = ? LIMIT 0,5";
        String[] selectionArgs = { custId };

        return db.query(
                LogsKeepFishing.Properties.TABLE_NAME,              // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
    }
}
