package com.fishing.namtran.fishingmanagerservice.dbconnection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

        CustomerDbHelper mDbHelper = new CustomerDbHelper(context);
        db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Customer.Properties.FULLNAME, mFullName);
        values.put(Customer.Properties.MOBILE, mMobile);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(Customer.Properties.TABLE_NAME, null, values);

        //close connection
        db.close();
        mDbHelper.close();

        return newRowId;
    }

    public void updateCustomer(String mFullName, String mMobile) {
        CustomerDbHelper mDbHelper = new CustomerDbHelper(context);
        db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Customer.Properties.FULLNAME, mFullName);

        // Which row to update, based on the title
        String selection = Customer.Properties.MOBILE + " LIKE ?";
        String[] selectionArgs = { mMobile };

        int count = db.update(
                Customer.Properties.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        //close connection
        db.close();
        mDbHelper.close();
    }

}

