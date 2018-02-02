package com.obinna.redeemfinance;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

public class Scroll_menu extends Activity {

    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;

    private View mControlsView;

    private boolean mVisible;

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.menu_scroll);




        // Set up the user interaction to manually show or hide the system UI.


        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }




    public void on_CreditClick(View v) {
        if (v.getId() == R.id.Bprof) {
            startActivity(new Intent(Scroll_menu.this, Credit_Record.class));
        }
    }

    public void on_DebitClick(View v) {
        if (v.getId() == R.id.Bdebit) {
            startActivity(new Intent(Scroll_menu.this, Debit_Record.class));
        }
    }
    public void on_FullClick(View v) {
        if (v.getId() == R.id.Bfull) {
            startActivity(new Intent(Scroll_menu.this, Full_Record.class));
        }
    }
    public void on_MonthClick(View v) {
        if (v.getId() == R.id.Bmonth) {
            startActivity(new Intent(Scroll_menu.this, Month_Record.class));
        }
    }
}
