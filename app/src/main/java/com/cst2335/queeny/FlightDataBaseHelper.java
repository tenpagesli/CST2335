package com.cst2335.queeny;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FlightDataBaseHelper extends SQLiteOpenHelper {
    /** database */
    private SQLiteDatabase database;
    /** database table name */
    public static final String DATABASE_NAME = "FlightDB";
    /** database version number*/
    public static final int VERSION_NUM = 1;
    /** database table name*/
    public static final String TABLE_NAME = "flight_table";
    /** database table column names */
    public static final String COL_ID = "id";
    public static final String COL_FLIGHT_NO = "flight_no";
    public static final String COL_LATITUDE= "latitude";
    public static final String COL_LONGITUDE = "longitude";
    public static final String COL_HORIZONTAL = "horizontal";
    public static final String COL_ISGROUND = "isground";
    public static final String COL_VERTICAL = "vertical";
    public static final String COL_ALTITUDE = "altitude";
    public static final String COL_STATUS = "status";

    public FlightDataBaseHelper(Activity ctx){
        super(ctx, DATABASE_NAME,null,VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COL_FLIGHT_NO + " TEXT, "
                    + COL_LATITUDE + " TEXT, "
                    + COL_LONGITUDE + " TEXT, "
                    + COL_HORIZONTAL + " TEXT, "
                    + COL_ISGROUND + " TEXT, "
                    + COL_VERTICAL + " TEXT, "
                    + COL_ALTITUDE + " TEXT, "
                    + COL_STATUS + " TEXT) "
                    + "");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i("Database upgrade", "old version: " + oldVersion + "  newVersion" + newVersion);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database downgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }
}