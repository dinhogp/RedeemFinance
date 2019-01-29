package com.obinna.redeemfinance;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Objects;


public class Profit_Menu extends Fragment {
    private EditText _dinhosInput;
    //private TextView dinhosText;
    private EditText dateInput;

    private Button Badd;

    Context context = getContext();
    SQLiteDatabase sqLiteDatabase;
    String[] SPINNERLIST = {"OFFERING", "TITHE"};
    String [] SPINNERLIST2={"FUEL","PRINTING/STATIONARY","SALARY","REMITTANCES","RENT(EQUIPMENT)","TELEPHONE/INTERNET","OFFICIAL TRIP",
            "TRANSPORTATION","SUPPORT","INSTRUMENT REPAIR/MAINTENANCE","DECORATION",
            "BUS MAINTENANCE","BUS PURCHASE","WELFARE","SPECIAL PROGRAMME","HONORARIUM AND TRANSPORT","INSURANCE","VEHICLE LICENCE"};

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

    EditText _debitInput;
    Spinner _spinner2;
    EditText txtdate2;
    Button Bdebit;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.profit,container, false);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //this.databaseAccess = DatabaseAccess.getInstance(getApplicationContext())

        findViews();
        mAuth=FirebaseAuth.getInstance();
        mdatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        mdatabase.keepSynced(true);

        // checkIntentForProfits();

        z=0;
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.support_simple_spinner_dropdown_item, SPINNERLIST);
        Spinner betterSpinner = (Spinner) Objects.requireNonNull(getView()).findViewById(R.id.spinner);
        betterSpinner.setAdapter(arrayAdapter);

        final ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getContext(),
                R.layout.support_simple_spinner_dropdown_item, SPINNERLIST2);
        Spinner betterSpinner2 = (Spinner) Objects.requireNonNull(getView()).findViewById(R.id.debitspinner);
        betterSpinner2.setAdapter(arrayAdapter2);



        //printdatabase();
		final String user_id=mAuth.getCurrentUser().getUid();
		mRef= FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        dbRef= mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        //button=(Button)findViewById(R.id.Badd);



		EditText txtDate=(EditText)Objects.requireNonNull(getView()).findViewById(R.id.txtdate);
        txtDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                
                     DateDialog dialog=new DateDialog(v);
                     android.support.v4.app.FragmentTransaction ft=getFragmentManager().beginTransaction();
                     dialog.show(ft,"DatePicker");

                
            }


        });

        EditText txtDate2=(EditText)Objects.requireNonNull(getView()).findViewById(R.id.txtdate2);
        txtDate2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                DateDialog dialog=new DateDialog(v);
                android.support.v4.app.FragmentTransaction ft=getFragmentManager().beginTransaction();
                dialog.show(ft,"DatePicker");


            }


        });

        Badd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String check = _dinhosInput.getText().toString();
                if (check.isEmpty()) {
                    Toast temp = Toast.makeText(getContext(), "Amount is Empty", Toast.LENGTH_SHORT);
                    temp.show();
                } else {
                    AlertDialog.Builder altdial = new AlertDialog.Builder(getContext());
                    altdial.setMessage("Are you sure you want to credit this amount ???").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final String user_id = mAuth.getCurrentUser().getUid();

                                    mRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (z < 1) {
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
                                    Toast temp = Toast.makeText(getContext(), "Amount Added Successfully!", Toast.LENGTH_SHORT);
                                    temp.show();

                                    Intent intent = getActivity().getIntent();
                                    getActivity().finish();
                                    startActivity(intent);
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = altdial.create();
                    alert.setTitle("Decision Box");
                    alert.show();
                }
            }
        });

        Bdebit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String check = _debitInput.getText().toString();
                if (check.isEmpty()) {
                    Toast temp = Toast.makeText(getContext(), "Amount is Empty", Toast.LENGTH_SHORT);
                    temp.show();
                } else {
                    AlertDialog.Builder altdial = new AlertDialog.Builder(getContext());
                    altdial.setMessage("Are you sure you want to credit this amount ???").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final String user_id = mAuth.getCurrentUser().getUid();
                                    mRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (z < 1) {
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
                                                String date = txtdate2.getText().toString();
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
                                    Toast temp = Toast.makeText(getContext(), "Amount Deducted Successfully!", Toast.LENGTH_SHORT);
                                    temp.show();

                                    Intent intent = getActivity().getIntent();
                                    getActivity().finish();
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = altdial.create();
                    alert.setTitle("Decision Box");
                    alert.show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        checkUserExist();
    }
    /*public void onAddClick(View view){
		        Badd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  AlertDialog.Builder altdial = new AlertDialog.Builder(getContext());
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
                   Toast temp = Toast.makeText(getContext(), "Amount Added Successfully!", Toast.LENGTH_SHORT);
                   temp.show();


                   //finish();
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
	}*/

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void findViews() {
        this._dinhosInput = (EditText) Objects.requireNonNull(getView()).findViewById(R.id.dinhosInput);
        this._spinner = (Spinner) Objects.requireNonNull(getView()).findViewById(R.id.spinner);

        this.dateInput = (EditText) Objects.requireNonNull(getView()).findViewById(R.id.txtdate);
        this.Badd = (Button) Objects.requireNonNull(getView()).findViewById(R.id.Badd);

        //------
        this._debitInput = (EditText) Objects.requireNonNull(getView()).findViewById(R.id.debitInput);
        this._spinner2 = (Spinner) Objects.requireNonNull(getView()).findViewById(R.id.debitspinner);
        //this.dinhosText = (TextView) findViewById(R.id.dinhosText);
        this.txtdate2 = (EditText) Objects.requireNonNull(getView()).findViewById(R.id.txtdate2);
        this.Bdebit = (Button) Objects.requireNonNull(getView()).findViewById(R.id.Bdebit);


    }



    private void checkUserExist() {
        final String user_id=mAuth.getCurrentUser().getUid();

        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(user_id)){
                    Intent mainIntent=new Intent(getContext(),LogIn.class);
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