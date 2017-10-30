package com.fishing.namtran.fishingmanagerservice.dbconnection;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import static com.fishing.namtran.fishingmanagerservice.dbconnection.FeedReaderUser.*;

/**
 * Created by Nam Tran on 10/30/2017.
 */

public class User {

    public void createUser(Context context, String mEmail, String mPassword, String role) {
        //
        FeedReaderDbHelper mDbHelper;
        mDbHelper = new FeedReaderDbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedUser.EMAIL, mEmail);
        values.put(FeedUser.PASSWORD, mPassword);
        values.put(FeedUser.ROLE, role);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(FeedUser.TABLE_NAME, null, values);

        //close connection
        db.close();
    }
}
//https://stackoverflow.com/questions/19194576/how-do-i-view-the-sqlite-database-on-an-android-device
//https://www.androidhive.info/2012/08/android-session-management-using-shared-preferences/
