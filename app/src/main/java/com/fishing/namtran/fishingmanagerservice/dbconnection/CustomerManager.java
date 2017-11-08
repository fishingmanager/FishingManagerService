package com.fishing.namtran.fishingmanagerservice.dbconnection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

/**
 * Created by Nam Tran on 10/30/2017.
 */

public class CustomerManager {

    private SQLiteDatabase db;
    private Context context;

    public CustomerManager(Context context)
    {
        this.context = context;
    }

    public long createCustomer(String mFullName, String mMobile) {

        InitializeDatabase mDbHelper = new InitializeDatabase(context);
        db = mDbHelper.getWritableDatabase();
        long newRowId = checkCustomerExisted(mMobile);

        if( newRowId < 0) {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(Customers.Properties.FULLNAME, mFullName);
            values.put(Customers.Properties.MOBILE, mMobile);

            // Insert the new row, returning the primary key value of the new row
            newRowId = db.insert(Customers.Properties.TABLE_NAME, null, values);
        }

        //close connection
        db.close();
        mDbHelper.close();

        return newRowId;
    }

    public long checkCustomerExisted(String mobile)
    {
        InitializeDatabase mDbHelper = new InitializeDatabase(context);
        db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                Customers.Properties._ID,
                Customers.Properties.FULLNAME,
                Customers.Properties.MOBILE,
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = Customers.Properties.MOBILE + " = ?";
        String[] selectionArgs = { mobile };

        Cursor cursor = db.query(
                Customers.Properties.TABLE_NAME,              // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if(cursor.moveToNext()) {
            if(mobile == cursor.getString(cursor.getColumnIndexOrThrow(Customers.Properties.MOBILE))) {
                long custId = cursor.getLong(cursor.getColumnIndexOrThrow(Customers.Properties._ID));
                cursor.close();
                return custId;
            }
        }
        cursor.close();
        return -1;
    }

    public void updateCustomer(String mFullName, String mMobile) {
        InitializeDatabase mDbHelper = new InitializeDatabase(context);
        db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Customers.Properties.FULLNAME, mFullName);

        // Which row to update, based on the title
        String selection = Customers.Properties.MOBILE + " LIKE ?";
        String[] selectionArgs = { mMobile };

        int count = db.update(
                Customers.Properties.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        //close connection
        db.close();
        mDbHelper.close();
    }

}

