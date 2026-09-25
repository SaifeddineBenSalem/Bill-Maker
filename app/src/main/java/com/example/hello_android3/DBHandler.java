package com.example.hello_android3;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {
        public static final String DB_NAME = "appMobile.db";
        public static final int DB_VERSION = 10;

        public DBHandler(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE IF NOT EXISTS " + UserColumns.TABLE_NAME + " ("
                    + UserColumns.ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + UserColumns.FIRST_NAME + " TEXT, "
                    + UserColumns.LAST_NAME + " TEXT, "
                    + UserColumns.DATE_OF_BIRTH + " TEXT, "
                    + UserColumns.COUNTRY + " TEXT, "
                    + UserColumns.PHONE_NUMBER + " TEXT, "
                    + UserColumns.SECURITY_QUESTION + " TEXT, "
                    + UserColumns.SECURITY_ANSWER + " TEXT, "
                    + UserColumns.PROFILE_PHOTO + " TEXT, "
                    + UserColumns.EMAIL + " TEXT, "
                    + UserColumns.ADMIN + " TINYINT DEFAULT 0, "
                    + UserColumns.FIRST_LOGIN + " TINYINT DEFAULT 1, "
                    + UserColumns.BANNED + " TINYINT DEFAULT 0, "
                    + UserColumns.PASSWORD + " TEXT)";
            db.execSQL(query);
            String query1 = "CREATE TABLE IF NOT EXISTS " + RequestColumns.TABLE_NAME + " ("
                    + RequestColumns.ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + RequestColumns.NAME + " TEXT, "
                    + RequestColumns.DESCRIPTION + " TEXT, " // Added space after "DESCRIPTION"
                    + RequestColumns.COUNTRY + " TEXT, "
                    + RequestColumns.PHOTO + " TEXT, " // Added space after "PHOTO"
                    + RequestColumns.TYPE + " TEXT, " // Added space after "TYPE"
                    + RequestColumns.POSTER + " INTEGER, " // Added space after "POSTER"
                    + RequestColumns.ACTION_BY + " INTEGER, " // Added space after "ACTION_BY"
                    + RequestColumns.POSTING_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
            db.execSQL(query1);
        }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       if (oldVersion < 10) {
           db.execSQL("ALTER TABLE " + UserColumns.TABLE_NAME + " ADD COLUMN " + UserColumns.FIRST_LOGIN + " INTEGER DEFAULT 1");
       }
    }
}