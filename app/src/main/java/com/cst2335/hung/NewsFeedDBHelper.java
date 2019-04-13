package com.cst2335.hung;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NewsFeedDBHelper extends SQLiteOpenHelper {


        public static final String DATABASE_NAME = "NewsFeedDB";
        public static final int VERSION_NUM = 1;
        // the column names in database
        public static final String TABLE_NAME = "saved_news_feed";
        public static final String COL_ID = "_id";
        public static final String COL_TITLE = "news_title";


        public NewsFeedDBHelper(Activity ctx){
            //The factory parameter should be null, unless you know a lot about Database Memory management
            super(ctx, DATABASE_NAME, null, VERSION_NUM );
        }

        // it will run only if the database file doesn't exist yet
        public void onCreate(SQLiteDatabase db)
        {
            //Make sure you put spaces between SQL statements and Java strings:
            String sql = "CREATE TABLE " + TABLE_NAME + "( "
                    + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COL_TITLE + " TEXT)";
            Log.i("Database onCreate:", "Query:" + sql);
            db.execSQL(sql);
            // log messages
            Log.i("Database onCreate:", "Created.");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.i("Database upgrade:", " Old version:" + oldVersion + " newVersion:"+newVersion);

            //Delete the old table:
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

            //Create a new table:
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.i("Database downgrade", " Old version:" + oldVersion + " newVersion:"+newVersion);

            //Delete the old table:
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

            //Create a new table:
            onCreate(db);
        }



}