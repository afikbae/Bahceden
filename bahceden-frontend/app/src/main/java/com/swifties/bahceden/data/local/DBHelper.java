package com.swifties.bahceden.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bahceden.db";
    private static final int DATABASE_VERSION = 2;  // Increased version number
    private static final String TABLE_USER = "users";
    private static final String TABLE_SEARCH_HISTORY = "search_history";

    // columns for users table
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_EMAIL = "EMAIL";
    public static final String COLUMN_PASSWORD = "PASSWORD";

    // columns for search history table
    public static final String COLUMN_HISTORY_ID = "ID";
    public static final String COLUMN_SEARCH_QUERY = "SEARCH_QUERY";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USER + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_EMAIL + " TEXT, " + COLUMN_PASSWORD + " TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SEARCH_HISTORY + " (" + COLUMN_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_SEARCH_QUERY + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCH_HISTORY);
        onCreate(db);
    }

    public boolean insertUser(String email, String password) {
        deleteAllUsers();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USER, null, contentValues);
        return result != -1;
    }

    public String[] getUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_USER, null);

        if (res.moveToFirst()) {
            return new String[]{ res.getString(1), res.getString(2)};
        } else {
            return null;
        }
    }

    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_USER);
    }


    public boolean insertSearchHistory(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SEARCH_QUERY, query);

        long result = db.insert(TABLE_SEARCH_HISTORY, null, contentValues);

        // Debugging
//        if (result != -1) {
//            Log.d("DBHelper", "Data inserted successfully");
//        } else {
//            Log.d("DBHelper", "Failed to insert data");
//        }

        return result != -1;
    }

    public ArrayList<String> getSearchHistory() {
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<String> searchHistoryList;
        Cursor res = null;
        try {
            res = db.rawQuery("select * from " + TABLE_SEARCH_HISTORY, null);

            searchHistoryList = new ArrayList<>();
            while (res.moveToNext()) {
                searchHistoryList.add(res.getString(1));
            }
        } finally {
            if (res != null) {
                res.close();
            }
        }

        // Debugging
        //Log.d("DBHelper", "Retrieved search history: " + searchHistoryList.toString());

        return searchHistoryList;
    }

    public void deleteAllSearchHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_SEARCH_HISTORY);
    }

    public boolean insertSearch(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, item);

        long result = db.insert(TABLE_SEARCH_HISTORY, null, contentValues);

        //if date as inserted incorrectly it will return -1
        return result != -1;
    }
}
