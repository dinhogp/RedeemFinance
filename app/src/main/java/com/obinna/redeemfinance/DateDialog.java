package com.obinna.redeemfinance;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hp 14 on 1/24/2017.
 */

@SuppressLint("ValidFragment")
public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    EditText txtDate;
    public final Calendar c=Calendar.getInstance();;
    @SuppressLint("ValidFragment")
    public DateDialog(View view){
        txtDate=(EditText)view;
    }
    public Dialog onCreateDialog(Bundle savedInstanceState){

        int year=c.get(Calendar.YEAR);
        int month=c.get(Calendar.MONTH);
        int day=c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this,year,month,day);


    }

    public void onDateSet(DatePicker view,int year, int month, int day){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        c.set(year, month, day);
        String date = sdf.format(c.getTime());
        txtDate.setText(date);
        //String date=year+"-"+(month+1)+"-"+day;
    }
    /*

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        populateSetDate(yy, mm+1, dd);
    }
    public void populateSetDate(int year, int month, int day) {
        dob.setText(month+"/"+day+"/"+year);
    }
*/

}
