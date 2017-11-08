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

    public long createFishingEntry(long mCustomerId, String mDateIn, int mFeedType, String mNote) { // String mKeepHours, String mNoKeepHours, String mKeepFish, String mTakeFish, String mTotalFish, String mTotalMoney, String mNote

        InitializeDatabase mDbHelper = new InitializeDatabase(context);
        db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Fishings.Properties.CUSTOMER_ID, mCustomerId);
        values.put(Fishings.Properties.DATE_IN, mDateIn);
        values.put(Fishings.Properties.FEED_TYPE, mFeedType);
        values.put(Fishings.Properties.NOTE, mNote);

        // Insert the new row, returning the primary key value of the new row
        long rowId = db.insert(Fishings.Properties.TABLE_NAME, null, values);

        //close connection
        db.close();

        return rowId;
    }

    public Cursor getFishingAllEntries() {
        InitializeDatabase mDbHelper = new InitializeDatabase(context);
        db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                Fishings.Properties.CUSTOMER_ID,
                Fishings.Properties.DATE_IN,
                Fishings.Properties.DATE_OUT,
                Fishings.Properties.NOTE,
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                Fishings.Properties._ID + " ASC";

        Cursor cursor = db.query(
                Fishings.Properties.TABLE_NAME,              // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        return cursor;
    }

    public Cursor getFishingEntry(String mCustomerId) {
        InitializeDatabase mDbHelper = new InitializeDatabase(context);
        db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                Fishings.Properties.CUSTOMER_ID,
                Fishings.Properties.DATE_IN,
                Fishings.Properties.DATE_OUT,
                Fishings.Properties.NOTE,
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = Fishings.Properties.CUSTOMER_ID + " = ?";
        String[] selectionArgs = { mCustomerId };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                Fishings.Properties._ID + " ASC";

        Cursor cursor = db.query(
                Fishings.Properties.TABLE_NAME,              // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        return cursor;
    }

    public Cursor getFishingEntries() {
        InitializeDatabase mDbHelper = new InitializeDatabase(context);
        db = mDbHelper.getReadableDatabase();

        String query = "SELECT fishing." + Fishings.Properties.DATE_IN + ", fishing." + Fishings.Properties.DATE_OUT + ", fishing." + Fishings.Properties.FEED_TYPE + ", fishing." + Fishings.Properties.NOTE
                                + ", customer." + Customers.Properties._ID + ", customer." + Customers.Properties.FULLNAME + ", customer." + Customers.Properties.MOBILE
                                + ", keepfishing." + KeepFishing.Properties.KEEP_HOURS + ", keepfishing." + KeepFishing.Properties.NO_KEEP_HOURS + ", keepfishing." + KeepFishing.Properties.KEEP_FISH
                                + ", keepfishing." + KeepFishing.Properties.TAKE_FISH + ", keepfishing." + KeepFishing.Properties.TOTAL_FISH +
                        " FROM " +  Fishings.Properties.TABLE_NAME + " fishing, " + Customers.Properties.TABLE_NAME + " customer, " + KeepFishing.Properties.TABLE_NAME + " keepfishing" +
                        " WHERE " + "customer." + Customers.Properties._ID + " = " + "fishing." + Fishings.Properties.CUSTOMER_ID + " AND " + "fishing." + Fishings.Properties.CUSTOMER_ID + " = " + "keepfishing." + KeepFishing.Properties.CUSTOMER_ID;

        return db.rawQuery(query, null);
    }
}
