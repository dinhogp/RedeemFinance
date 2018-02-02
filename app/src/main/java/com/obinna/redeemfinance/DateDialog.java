package com.obinna.redeemfinance;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hp 14 on 1/24/2017.
 */

public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    EditText txtDate;
    public final Calendar c=Calendar.getInstance();;
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


}
