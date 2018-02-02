package com.obinna.redeemfinance;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class Profit_Menu extends Activity {
    private EditText _dinhosInput;
    //private TextView dinhosText;
    private EditText dateInput;

    private Button Badd;
    DatabaseOpenHelper helper = new DatabaseOpenHelper(this);
    Context context = this;
    SQLiteDatabase sqLiteDatabase;
    String[] SPINNERLIST = {"OFFERING", "TITHE"};
    private Spinner _spinner;
    private DatabaseReference mRef;
    private DatabaseReference dbRef;
    int key;
    int x;
    private long mLastClickTime=0;
    Button button;
    String lakey;
    int z=0;
    private DatabaseReference mdatabase;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //this.databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        setContentView(R.layout.profit);
        findViews();
        mAuth=FirebaseAuth.getInstance();
        mdatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        mdatabase.keepSynced(true);

        // checkIntentForProfits();

        z=0;
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, SPINNERLIST);
        Spinner betterSpinner = (Spinner) findViewById(R.id.spinner);
        betterSpinner.setAdapter(arrayAdapter);
//        betterSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                income_type = arrayAdapter.getItem(position);
//            }
//        });

        //printdatabase();
		final String user_id=mAuth.getCurrentUser().getUid();
		mRef= FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        dbRef= mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        //button=(Button)findViewById(R.id.Badd);



		EditText txtDate=(EditText)findViewById(R.id.txtdate);
        txtDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                
                     DateDialog dialog=new DateDialog(v);
                     FragmentTransaction ft=getFragmentManager().beginTransaction();
                     dialog.show(ft,"DatePicker");

                
            }


        });
    }

    public void onAddClick(View view){
		        Badd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  AlertDialog.Builder altdial = new AlertDialog.Builder(Profit_Menu.this);
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
                               String profitname = _dinhosInput.getText().toString();
                               String date = dateInput.getText().toString();
                               String type = "+";
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
                   Toast temp = Toast.makeText(Profit_Menu.this, "Amount Added Successfully!", Toast.LENGTH_SHORT);
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
        public void onStart() {
            super.onStart();
            EditText txtDate = (EditText) findViewById(R.id.txtdate);
            txtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        DateDialog dialog = new DateDialog(v);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        dialog.show(ft, "DatePicker");

                  }
                }


            });
        }

        /*
            private void AddProfit() {

                Profits newProfit = new Profits();
                newProfit.setincome_type(_spinner.getText().toString());
                newProfit.setprofitname(_dinhosInput.getText().toString());
                newProfit.setDate(dateInput.getText().toString());
                //databaseAccess.open();
                //newProfit.setincome_type(spinner.getSelectedItem().toString());

                helper.insertProfit(newProfit);
                //databaseAccess.close();
                //this.finish();
            }
        */
    private void findViews() {
        this._dinhosInput = (EditText) findViewById(R.id.dinhosInput);
        this._spinner = (Spinner) findViewById(R.id.spinner);

        this.dateInput = (EditText) findViewById(R.id.txtdate);
        this.Badd = (Button) findViewById(R.id.Badd);

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
                    Intent mainIntent=new Intent(Profit_Menu.this,LogIn.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}