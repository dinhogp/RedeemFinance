package com.obinna.redeemfinance;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by hp 14 on 1/9/2017.
 */

public class Menu_display extends Activity {

    SQLiteDatabase sqLiteDatabase;
    DatabaseOpenHelper helper;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

    }
    public void onProfitClick(View v) {
        if (v.getId() == R.id.Bprofit) {

            startActivity(new Intent(Menu_display.this,Profit_Menu.class));
        }
    }
    public void onCalculatorClick(View v) {
        if (v.getId() == R.id.Bcalculator) {

            startActivity(new Intent(Menu_display.this,Record_Menu.class));

        }
    }
    public void onDebitClick(View v) {
        if (v.getId() == R.id.Bdebit) {

            startActivity(new Intent(Menu_display.this,Debit_Menu.class));
        }
    }
    public void onCreditRecord(View v) {
        if (v.getId() == R.id.Bcreditrecord) {

            startActivity(new Intent(Menu_display.this,Credit_Record.class));
        }
    }
    public void ondebitrecClick(View v) {
        if (v.getId() == R.id.Bdebitrecord) {

            startActivity(new Intent(Menu_display.this,Debit_Record.class));
        }
    }
    public void onfullClick(View v) {
        if (v.getId() == R.id.Bfullrecord) {

            startActivity(new Intent(Menu_display.this,Full_Record.class));
        }
    }

}
