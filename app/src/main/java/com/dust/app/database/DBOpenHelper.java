package com.dust.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBOpenHelper extends SQLiteOpenHelper {

    public DBOpenHelper(@Nullable Context context) {
        super(context, "dust.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user_status(id INTEGER PRIMARY KEY AUTOINCREMENT,user_id INTEGER,user_lon REAL,user_lat REAL,user_alt REAL,time TEXT,user_status INTEGER,has_been_published TEXT DEFAULT 'false')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
