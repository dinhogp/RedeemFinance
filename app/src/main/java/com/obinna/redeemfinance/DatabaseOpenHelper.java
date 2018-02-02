package com.obinna.redeemfinance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by hp 14 on 1/11/2017.
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseOpenHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "churchtable.db";
    public static final String TABLE_NAME = "income";
    private SQLiteDatabase db;
    //private String type;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //, entry_date datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
    // id integer primary key not null,
    private static final String TABLE_CREATE = "create table income(income_type text not null,profitname text not null,date text NOT NULL,input_type text not null);";

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TABLE_CREATE);
        this.db = db;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_NAME);
        this.onCreate(db);

    }

    public Cursor getinformation(SQLiteDatabase db,String fromDate,String toDate) {

        Cursor cursor;
        //String query = "select * from income" ;
        String[] projections = {"income_type", "profitname", "date", "input_type"};

        cursor = db.query(TABLE_NAME, projections, "(date >= ? and date <= ? )", new String[]{fromDate,toDate}, null, null, "date ASC");
//        cursor=db.rawQuery(select,  new String[]{fromDate,toDate});


        return cursor;
    }



    public void addInformation(String income_type, String profitname, String date, String type, SQLiteDatabase db) {


        ContentValues contentValues = new ContentValues();
        contentValues.put("income_type", income_type);
        contentValues.put("profitname", profitname);
        contentValues.put("date", date);
        contentValues.put("input_type",type);
        long numRowsInserted = db.insert(TABLE_NAME, null, contentValues);

        Log.d(TAG, "Number of rows: " + numRowsInserted);

    }

    public void debitInformation(String income_type, String profitname, String date, String type, SQLiteDatabase db) {

        //type="-";
        ContentValues contentValues = new ContentValues();
        contentValues.put("income_type", income_type);
        contentValues.put("profitname", profitname);
        contentValues.put("date", date);
        contentValues.put("input_type", type);
        long numRowsInserted = db.insert(TABLE_NAME, null, contentValues);

        Log.d(TAG, "Number of rows: " + numRowsInserted);

    }
    public void clearTable(SQLiteDatabase db){

        db.delete(TABLE_NAME,null,null);

    }

  /*  //Add a new row to the database
    public void addProfit(Profits profit){
        ContentValues values = new ContentValues();
        String query = "select * from profits";

        Cursor cursor = db.rawQuery(query , null);
        int count = cursor.getCount();
        values.put(COLUMN_ID, count);
        values.put(COLUMN_PROFITNAME,profit.get_profitname());
        db=this.getWritableDatabase();

        long insertId= db.insert(TABLE_NAME, null, values);
        Log.i(TAG, "added name id:" + insertId);
        db.close();

    }*/

    ///print out database as a string

    public String databaseToString() {
        String dbString = " ";
        SQLiteDatabase db = getWritableDatabase();
        String query = "select * from " + TABLE_NAME;

        //Cursor point to a location in result
        Cursor c = db.rawQuery(query, null);
        //Move to first row in your results
        c.moveToFirst();

        while (c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("profitname")) != null) {
                dbString += c.getString(c.getColumnIndex("profitname"));
                dbString += "\n";
            }
        }
        db.close();
        return dbString;
    }

}

