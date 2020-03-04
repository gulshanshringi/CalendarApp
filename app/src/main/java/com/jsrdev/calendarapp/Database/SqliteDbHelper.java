package com.jsrdev.calendarapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SqliteDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Calendar.db";
    private static final String TABLE_NAME = "events_table";
    public static final String ID = "ID";
    private static final String TITLE = "TITLE";
    private static final String LOCATION = "LOCATION";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String START_DATE = "START_DATE";
    private static final String END_DATE = "END_DATE";
 /*   private static final String COL_1 = "Calendar.db";
    private static final String COL_1 = "Calendar.db";
    private static final String COL_1 = "Calendar.db";
*/


    public SqliteDbHelper( Context context) {
        super(context, DATABASE_NAME, null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+ TABLE_NAME  +"("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + TITLE +" TEXT NOT NULL , "
                + LOCATION + " TEXT , "
                + DESCRIPTION + " TEXT , "
                + START_DATE +" TEXT NOT NULL  ,"
                + END_DATE +" TEXT NOT NULL );");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public boolean insertData(String title , String location , String description , String startDate, String endDate){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE , title);
        contentValues.put(LOCATION,location);
        contentValues.put(DESCRIPTION,description);
        contentValues.put(START_DATE,startDate);
        contentValues.put(END_DATE,endDate);

        long result = db.insert(TABLE_NAME,null,contentValues);

        if (result == -1){
            return false;
        }else{
            return true;
        }

    }

    public Cursor showAllData(String condition , String[] selArgs){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "select * from "+ TABLE_NAME +" "+ condition;

        Cursor cursor = db.rawQuery(query, selArgs);
        return cursor;
    }
}
