package com.obinna.redeemfinance;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by hp 14 on 1/14/2017.
 */

public class MyCalc extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator);


    }
    public void onCalcClick (View v){
        if(v.getId()==R.id.Bcalc) {
            EditText cfifty = (EditText)findViewById(R.id.fifty);
            EditText ctwenty = (EditText)findViewById(R.id.twenty);
            EditText cten = (EditText)findViewById(R.id.ten);
            EditText cfive = (EditText)findViewById(R.id.five);
            EditText ctwo = (EditText)findViewById(R.id.two);
            EditText cone = (EditText)findViewById(R.id.one);
            TextView display = (TextView)findViewById(R.id.display);

            double fifty = Double.parseDouble(cfifty.getText().toString());
            double twenty = Double.parseDouble(ctwenty.getText().toString());
            double ten = Double.parseDouble(cten.getText().toString());
            double five = Double.parseDouble(cfive.getText().toString());
            double two = Double.parseDouble(ctwo.getText().toString());
            double one = Double.parseDouble(cone.getText().toString());
            double total = (fifty * 50.0)+(twenty * 20.0)+(ten * 10.0)+(five * 5.0)+(two * 2.0)+ one;

            display.setText(total + "");


        }
    }
}
