package com.obinna.redeemfinance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp 14 on 1/30/2017.
 */

public class ListDataAdapter extends ArrayAdapter {
    List list = new ArrayList();
    public ListDataAdapter(Context context, int resource) {
        super(context, resource);
    }
    static class LayoutHandler{
        TextView AMOUNT_TYPE,PROFITNAME,DATE,TYPE;
    }

    @Override
    public void add(Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutHandler layoutHandler;
        if(row==null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_credit_layout, parent, false);
            layoutHandler = new LayoutHandler();
            layoutHandler.AMOUNT_TYPE = (TextView) row.findViewById(R.id.txtAmount_type);
            layoutHandler.PROFITNAME = (TextView) row.findViewById(R.id.txtProfitname);
            layoutHandler.DATE = (TextView) row.findViewById(R.id.txtDate);
            layoutHandler.TYPE = (TextView) row.findViewById(R.id.txttype);
            row.setTag(layoutHandler);

        }else{

            layoutHandler=(LayoutHandler)row.getTag();

        }
        Profits profits = (Profits)this.getItem(position);
        layoutHandler.AMOUNT_TYPE.setText(profits.getincome_type());
        layoutHandler.PROFITNAME.setText(profits.getprofitname());
        layoutHandler.DATE.setText(profits.getdate());
        layoutHandler.TYPE.setText(profits.gettype());
        return row;


    }


}
