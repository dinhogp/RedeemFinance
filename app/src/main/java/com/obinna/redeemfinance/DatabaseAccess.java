package com.obinna.redeemfinance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by hp 14 on 1/26/2017.
 */

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    //Add a new row to the database
    public void insertProfit(Profits profit) {

        ContentValues values = new ContentValues();

        values.put("profit_name", profit.getprofitname());
        values.put("income_type", profit.getincome_type());


        database.insert("income", null, values);

    }

/*
    public List<Profits> getchurchtable() {
        List<Profits> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM income", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Profits profit = new Profits();
            profit.setincome_type(cursor.getString(1));
            profit.setprofitname(cursor.getString(2));
            //String profit;
            //profit=cursor.getString(0) + "   " + cursor.getString(1);
            list.add(profit);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
*/
}
