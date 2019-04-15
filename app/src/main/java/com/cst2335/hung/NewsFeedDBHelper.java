package com.cst2335.hung;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NewsFeedDBHelper extends SQLiteOpenHelper {


        public static final String DATABASE_NAME = "NewsFeedDB"; //Db name
        public static final int VERSION_NUM = 1;

        public static final String TABLE_NAME = "saved_news_feed"; //table name

        public static final String COL_ID = "_id"; //column 0
        public static final String COL_TITLE = "news_title"; //column 1


        public NewsFeedDBHelper(Context ctx){
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
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.i("Database downgrade", " Old version:" + oldVersion + " newVersion:"+newVersion);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

    /**
     * get all data from table using raw query
     * @return
     */
    public Cursor getAllData(){
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("select news_title from "+ TABLE_NAME, null);
            return cursor;
}


}
