package com.obinna.redeemfinance;

import android.os.SystemClock;
import android.view.View;

/**
 * Created by Dinho on 3/24/2017.
 */

public class CustomClickListener implements View.OnClickListener {

    protected int defaultInterval;
    private long lastTimeClicked = 0;

    public CustomClickListener() {
        this(1000);
    }

    public CustomClickListener(int minInterval) {
        this.defaultInterval = minInterval;
    }

    @Override
    public void onClick(View v) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return;
        }
        lastTimeClicked = SystemClock.elapsedRealtime();
        performClick(v);
    }

    public void performClick(View v) {

    }

}
