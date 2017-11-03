package com.fishing.namtran.fishingmanagerservice.dbconnection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Nam Tran on 10/30/2017.
 */

public class FishingManager {

    private SQLiteDatabase db;
    private Context context;

    public FishingManager(Context context) {
        this.context = context;
    }

    public void createFishingEntry(String mCustomerId, String mDateIn, String mDateOut, String mFeedType, String mKeepHours, String mNoKeepHours, String mKeepFish, String mTakeFish, String mTotalFish, String mTotalMoney, String mNote) {

        FishingDbHelper mDbHelper = new FishingDbHelper(context);
        db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Fishing.Properties.CUSTOMER_ID, mCustomerId);
        values.put(Fishing.Properties.DATE_IN, mDateIn);
        values.put(Fishing.Properties.DATE_OUT, mDateOut);
        values.put(Fishing.Properties.FEED_TYPE, mFeedType);
        values.put(Fishing.Properties.KEEP_HOURS, mKeepHours);
        values.put(Fishing.Properties.NO_KEEP_HOURS, mNoKeepHours);
        values.put(Fishing.Properties.KEEP_FISH, mKeepFish);
        values.put(Fishing.Properties.TAKE_FISH, mTakeFish);
        values.put(Fishing.Properties.TOTAL_FISH, mTotalFish);
        values.put(Fishing.Properties.TOTAL_MONEY, mTotalMoney);
        values.put(Fishing.Properties.NOTE, mNote);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(Fishing.Properties.TABLE_NAME, null, values);

        //close connection
        db.close();
    }

    public Cursor getFishingEntries() {
        FishingDbHelper mDbHelper = new FishingDbHelper(context);
        db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                Fishing.Properties.CUSTOMER_ID,
                Fishing.Properties.DATE_IN,
                Fishing.Properties.DATE_OUT,
                Fishing.Properties.FEED_TYPE,
                Fishing.Properties.KEEP_HOURS,
                Fishing.Properties.NO_KEEP_HOURS,
                Fishing.Properties.KEEP_FISH,
                Fishing.Properties.TAKE_FISH,
                Fishing.Properties.TOTAL_FISH,
                Fishing.Properties.TOTAL_MONEY,
                Fishing.Properties.NOTE,
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                Fishing.Properties._ID + " ASC";

        Cursor cursor = db.query(
                Fishing.Properties.TABLE_NAME,              // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        cursor.close();
        return cursor;
    }

    public Cursor getFishingEntry(String mCustomerId) {
        FishingDbHelper mDbHelper = new FishingDbHelper(context);
        db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                Fishing.Properties.CUSTOMER_ID,
                Fishing.Properties.DATE_IN,
                Fishing.Properties.DATE_OUT,
                Fishing.Properties.FEED_TYPE,
                Fishing.Properties.KEEP_HOURS,
                Fishing.Properties.NO_KEEP_HOURS,
                Fishing.Properties.KEEP_FISH,
                Fishing.Properties.TAKE_FISH,
                Fishing.Properties.TOTAL_FISH,
                Fishing.Properties.TOTAL_MONEY,
                Fishing.Properties.NOTE,
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = Fishing.Properties.CUSTOMER_ID + " = ?";
        String[] selectionArgs = { mCustomerId };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                Fishing.Properties._ID + " ASC";

        Cursor cursor = db.query(
                Fishing.Properties.TABLE_NAME,              // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        cursor.close();
        return cursor;
    }
}
