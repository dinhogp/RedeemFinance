package com.obinna.redeemfinance;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Created by hp 14 on 1/21/2017.
 */

public class Debit_Menu extends Activity {

    private Spinner _spinner;
    SQLiteDatabase sqLiteDatabase;
    Context context = this;
    DatabaseOpenHelper helper = new DatabaseOpenHelper(this);
    private EditText _debitInput;
    private EditText txtdate;
    String [] SPINNERLIST={"FUEL","PRINTING/STATIONARY","SALARY","REMITTANCES","RENT(EQUIPMENT)","TELEPHONE/INTERNET","OFFICIAL TRIP",
    "TRANSPORTATION","SUPPORT","INSTRUMENT REPAIR/MAINTENANCE","DECORATION",
    "BUS MAINTENANCE","BUS PURCHASE","WELFARE","SPECIAL PROGRAMME","HONORARIUM AND TRANSPORT","INSURANCE","VEHICLE LICENCE"};

    String lakey;
    int z=0;
    private DatabaseReference mRef;
    private DatabaseReference dbRef;
    int key;
	private Button Bdebit;
    private DatabaseReference mdatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // dateInput = (EditText)findViewById(R.id.dateInput);
       // Button btnDate = (Button)findViewById(R.id.setdate);
        setContentView(R.layout.debit);
        findViews();
        mAuth=FirebaseAuth.getInstance();
        mdatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        mdatabase.keepSynced(true);

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item,SPINNERLIST);
           Spinner betterSpinner=(Spinner)findViewById(R.id.spinner);
           betterSpinner.setAdapter(arrayAdapter);

        z=0;
		
		EditText txtDate=(EditText)findViewById(R.id.txtdate);
        txtDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                
                     DateDialog dialog=new DateDialog(v);
                     FragmentTransaction ft=getFragmentManager().beginTransaction();
                     dialog.show(ft,"DatePicker");

                
            }


        });
		final String user_id=mAuth.getCurrentUser().getUid();
		mRef= FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        dbRef= mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
		
    }
    @Override
    protected void onStart() {
        super.onStart();
        checkUserExist();
    }

    private void checkUserExist() {
        final String user_id=mAuth.getCurrentUser().getUid();

        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(user_id)){
                    Intent mainIntent=new Intent(Debit_Menu.this,LogIn.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
	public void debitButtonClicked(View view){
		        Bdebit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  AlertDialog.Builder altdial = new AlertDialog.Builder(Debit_Menu.this);
				  altdial.setMessage("Are you sure you want to credit this amount ???").setCancelable(false)
				  .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				   @Override
				    public void onClick(DialogInterface dialog, int which){
                       final String user_id=mAuth.getCurrentUser().getUid();
                mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(z<1) {
                        Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                        lakey = map.get("last_key");
                        key = Integer.parseInt(lakey);
                        key = key + 1;
                        lakey = Integer.toString(key);


                        final Calendar c = Calendar.getInstance();
                        int year = c.get(Calendar.YEAR);
                        int month = c.get(Calendar.MONTH);
                        int day = c.get(Calendar.DAY_OF_MONTH);
                        String income_type = _spinner.getSelectedItem().toString();
                        String profitname = _debitInput.getText().toString();
                        String date = txtdate.getText().toString();
                        String type = "-";
                        if (date.isEmpty()) {
                            //date=year+"-"+(month+1)+"-"+day;
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            c.set(year, month, day);
                            date = sdf.format(c.getTime());
                        }

                        Profits profits = new Profits(profitname, income_type, date, type);
                        dbRef.child("churchtable").child(lakey).setValue(profits);
                        dbRef.child("last_key").setValue(lakey);
                        z++;
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            Toast temp = Toast.makeText(Debit_Menu.this, "Amount Deducted Successfully!", Toast.LENGTH_SHORT);
            temp.show();


            finish();
				   }
				 })
           .setNegativeButton("No", new DialogInterface.OnClickListener() {
				   @Override
				   public void onClick(DialogInterface dialog, int which){
                       dialog.cancel();
				   }
				 });
				 AlertDialog alert = altdial.create();
				 alert.setTitle("Decision Box");
				 alert.show();
            }
        });
	}
	
/*
    public void onStart(){
        super.onStart();
        EditText txtDate=(EditText)findViewById(R.id.txtdate);
        txtDate.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                 if(hasFocus){
                     DateDialog dialog=new DateDialog(v);
                     FragmentTransaction ft=getFragmentManager().beginTransaction();
                     dialog.show(ft,"DatePicker");

                 }
            }


        });
    }*/
/*
    public void debitButtonClicked(View view) {
        if (view.getId() == R.id.Bdebit) {
                /*AddProfit();
                Toast temp = Toast.makeText(Profit_Menu.this, "Amount Added Successfully!", Toast.LENGTH_SHORT);
                temp.show();*/
            //printdatabase();
/*
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(z<1) {
                        Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                        lakey = map.get("last_key");
                        key = Integer.parseInt(lakey);
                        key = key + 1;
                        lakey = Integer.toString(key);


                        final Calendar c = Calendar.getInstance();
                        int year = c.get(Calendar.YEAR);
                        int month = c.get(Calendar.MONTH);
                        int day = c.get(Calendar.DAY_OF_MONTH);
                        String income_type = _spinner.getSelectedItem().toString();
                        String profitname = _debitInput.getText().toString();
                        String date = txtdate.getText().toString();
                        String type = "-";
                        if (date.isEmpty()) {
                            //date=year+"-"+(month+1)+"-"+day;
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            c.set(year, month, day);
                            date = sdf.format(c.getTime());
                        }

                        Profits profits = new Profits(profitname, income_type, date, type);
                        dbRef.child("churchtable").child(lakey).setValue(profits);
                        dbRef.child("last_key").setValue(lakey);
                        z++;
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            Toast temp = Toast.makeText(Debit_Menu.this, "Amount Deducted Successfully!", Toast.LENGTH_SHORT);
            temp.show();


            finish();
        }
    }*/

    private void findViews() {
        this._debitInput = (EditText) findViewById(R.id.debitInput);
        this._spinner = (Spinner) findViewById(R.id.spinner);
        //this.dinhosText = (TextView) findViewById(R.id.dinhosText);
        this.txtdate = (EditText) findViewById(R.id.txtdate);
        this.Bdebit = (Button) findViewById(R.id.Bdebit);

    }
}
