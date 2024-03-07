package com.example.protocolapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.protocolapp.constants.Constants;
import com.example.protocolapp.model.User;

public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(@Nullable Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //create table on database
        db.execSQL(Constants.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                // Upgrade logic from version 1 to 2 (keep your existing code if any)
                // ...
                // Fall through to include logic for the next version
            case 2:
                // Upgrade logic from version 2 to 3
                db.execSQL("ALTER TABLE " + Constants.TABLE_NAME + " ADD COLUMN new_column_name column_type");
                // If you have other upgrade logic, add it here
                break;
            default:
                throw new IllegalStateException("onUpgrade() with unknown oldVersion " + oldVersion);
        }
    }


    // Insert Function to insert data in database
    public long insertContact(String email, String password) {

        //get writable database to write data on db
        SQLiteDatabase db = this.getWritableDatabase();

        // create ContentValue class object to save data
        ContentValues contentValues = new ContentValues();

        contentValues.put(Constants.USER_EMAIL, email);
        contentValues.put(Constants.USER_PASSWORD, password);
        long id = db.insert(Constants.TABLE_NAME, null, contentValues);

        db.close();

        return id;

    }

    public String findByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                "*"
        };

        String selection = Constants.USER_EMAIL+ " = ?";
        String[] selectionArgs = { String.valueOf(email) };

        Cursor cursor = db.query(
                Constants.TABLE_NAME,   // The table to query
                projection,                   // The columns to return
                selection,                    // The columns for the WHERE clause
                selectionArgs,                // The values for the WHERE clause
                null,                         // Don't group the rows
                null,                         // Don't filter by row groups
                null                          // The sort order
        );

        if (cursor != null && cursor.moveToFirst()) {
            String emailOBJ = cursor.getString(cursor.getColumnIndexOrThrow(Constants.USER_EMAIL));
            cursor.close();
            db.close();
            return emailOBJ;
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return null;
    }

}
