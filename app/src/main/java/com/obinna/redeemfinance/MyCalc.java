package com.obinna.redeemfinance;

import android.app.Activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

/**
 * Created by hp 14 on 1/14/2017.
 */

public class MyCalc extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.calculator,container, false);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText cfifty = (EditText) Objects.requireNonNull(getView()).findViewById(R.id.fifty);
        final EditText ctwenty = (EditText)Objects.requireNonNull(getView()).findViewById(R.id.twenty);
        final EditText cten = (EditText)Objects.requireNonNull(getView()).findViewById(R.id.ten);
        final EditText cfive = (EditText)Objects.requireNonNull(getView()).findViewById(R.id.five);
        final EditText ctwo = (EditText)Objects.requireNonNull(getView()).findViewById(R.id.two);
        final EditText cone = (EditText)Objects.requireNonNull(getView()).findViewById(R.id.one);
        final TextView display = (TextView)Objects.requireNonNull(getView()).findViewById(R.id.display);

        Button button= (Button)Objects.requireNonNull(getView()).findViewById(R.id.Bcalc);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double fifty = Double.parseDouble(cfifty.getText().toString());
                double twenty = Double.parseDouble(ctwenty.getText().toString());
                double ten = Double.parseDouble(cten.getText().toString());
                double five = Double.parseDouble(cfive.getText().toString());
                double two = Double.parseDouble(ctwo.getText().toString());
                double one = Double.parseDouble(cone.getText().toString());
                double total = (fifty * 50.0)+(twenty * 20.0)+(ten * 10.0)+(five * 5.0)+(two * 2.0)+ one;

                display.setText(total + "");
            }
        });
    }



}
