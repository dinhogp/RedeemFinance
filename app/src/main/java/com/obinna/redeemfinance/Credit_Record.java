package com.obinna.redeemfinance;

//import android.app.FragmentTransaction;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.R.id.list;

/**
 * Created by hp 14 on 1/29/2017.
 */

public class Credit_Record extends Fragment {


    private ListView listView;


    SQLiteDatabase sqLiteDatabase;
    DatabaseOpenHelper helper;
    Cursor cursor;
    ListDataAdapter listDataAdapter;
    TextView credit_display;
    TextView debit_display;
    TextView total_display;
    EditText fromDate;
    EditText toDate;
    String fDate;
    String tDate;
    double credit, debit;
    double total_sum, debit_sum = 0, credit_sum = 0;
    EditText txtFDate;
    EditText txtTDate;
    private Button button ;
    //Button button;
    //public static Context context;
    private DatabaseReference mRef;
    String x;
    public int key;
    private Toolbar supportActionBar;
    private ProgressDialog mProgress;
    private DatabaseReference mdatabase;
    private FirebaseAuth mAuth;
    private RadioButton radioProfit;
    private RadioButton radioDebit;
    private RadioButton radioBoth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        return inflater.inflate(R.layout.profit_layout, container, false);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mdatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        mdatabase.keepSynced(true);
        //setContentView(R.layout.full_layout);
        //context=getActivity().getApplicationContext();
        //setContentView(R.layout.full_layout);
        listDataAdapter = new ListDataAdapter(getContext(), R.layout.list_credit_layout);
        // Find the GUI components
        listView = (ListView) Objects.requireNonNull(getView()).findViewById(R.id.CreditView);
        mAuth=FirebaseAuth.getInstance();
         credit_display = (TextView)Objects.requireNonNull(getView()).findViewById(R.id.credit_display);
         debit_display = (TextView)Objects.requireNonNull(getView()).findViewById(R.id.debit_display);
         total_display = (TextView)Objects.requireNonNull(getView()).findViewById(R.id.total_display);
         fromDate= (EditText)Objects.requireNonNull(getView()).findViewById(R.id.fromDATE);
         toDate= (EditText)Objects.requireNonNull(getView()).findViewById(R.id.toDATE);
        fromDate.setInputType(InputType.TYPE_NULL);
        toDate.setInputType(InputType.TYPE_NULL);
        listView.setAdapter(listDataAdapter);
        button=(Button)Objects.requireNonNull(getView()).findViewById(R.id.bView);
        radioProfit=(RadioButton)Objects.requireNonNull(getView()).findViewById(R.id.radioProfit);
        radioDebit=(RadioButton)Objects.requireNonNull(getView()).findViewById(R.id.radioDebit);
        radioBoth=(RadioButton)Objects.requireNonNull(getView()).findViewById(R.id.radioBoth);
        mProgress = new ProgressDialog(getContext());
        radioProfit.setChecked(true);

        radioProfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                radioProfit.setChecked(true);
                radioDebit.setChecked(false);
                radioBoth.setChecked(false);
            }
        });

        radioDebit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioProfit.setChecked(false);
                radioDebit.setChecked(true);
                radioBoth.setChecked(false);
            }
        });

        radioBoth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioProfit.setChecked(false);
                radioDebit.setChecked(false);
                radioBoth.setChecked(true);

            }
        });






        fromDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                DateDialog Fdialog = new DateDialog(v);
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                //Fdialog.show(ft,"DatePicker");
                Fdialog.show(ft,"DatePicker");



            }


        });
        toDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                DateDialog Fdialog = new DateDialog(v);
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                Fdialog.show(ft,"DatePicker");


            }


        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioProfit.isChecked()){
                    Credit_view();
                }
                else if(radioDebit.isChecked()){
                    Debit_view();
                }
                else {
                    Total_view();
                }
            }
        });

    }
    /*@SuppressLint("NewApi")
    public void onViewClick(View v) {
        if (v.getId() == R.id.bView) {
            //double credit, debit;




        }
    }*/

    @Override
    public void onStart() {
        super.onStart();
        checkUserExist();
    }

    private void checkUserExist() {
        final String user_id=mAuth.getCurrentUser().getUid();

        mdatabase.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(user_id)){
                    @SuppressLint({"NewApi", "LocalSuppress"}) Intent mainIntent=new Intent(getContext(),LogIn.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void Credit_view(){
        total_sum=0;
        debit_sum = 0;
        credit_sum = 0;
        credit=0;
        debit = 0;

        listDataAdapter = new ListDataAdapter(getContext(), R.layout.list_credit_layout);
        listView.setAdapter(listDataAdapter);
        debit_sum = 0;
        credit_sum = 0;
        final String user_id=mAuth.getCurrentUser().getUid();
        mProgress.setMessage("Data Loading...");
        mProgress.show();

        mRef=FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                String lakey = map.get("last_key");
                key = Integer.parseInt(lakey);
                int y = 1;
                do {

                    x = Integer.toString(y);
                    mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("churchtable").child(x);
                    //mRef.orderByChild("date").startAt(fDate).endAt(tDate);
                    mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();

                            String date = map.get("date");
                            String income_type = map.get("income_type");
                            String profitname = map.get("profitname");
                            String type = map.get("type");
                            Profits profits = new Profits(income_type, profitname, date, type);
                            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                fDate = fromDate.getText().toString();
                                tDate = toDate.getText().toString();
                                Date Cdate = sd.parse(date);
                                Date Fmdate = sd.parse(fDate);
                                Date Tmdate = sd.parse(tDate);

                                if (!Fmdate.after(Cdate) && !Tmdate.before(Cdate)) {

                                    if (type.equals("+")) {
                                        credit = Double.parseDouble(profitname);
                                        credit_sum = credit_sum + credit;
                                        listDataAdapter.add(profits);
                                    }

                                }

                                total_sum = credit_sum - debit_sum;
                                credit_display.setText(credit_sum + "GHc");
                                debit_display.setText(debit_sum + "GHc");
                                total_display.setText(total_sum + "GHc");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    y++;

                } while (y <= key);

                mProgress.dismiss();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void Debit_view(){
        total_sum=0;
        debit_sum = 0;
        credit_sum = 0;
        credit=0;
        debit = 0;
        listDataAdapter = new ListDataAdapter(getContext(), R.layout.list_credit_layout);
        listView.setAdapter(listDataAdapter);
        debit_sum = 0;
        credit_sum = 0;
        final String user_id=mAuth.getCurrentUser().getUid();
        mProgress.setMessage("Data Loading...");
        mProgress.show();

        mRef=FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                String lakey = map.get("last_key");
                key = Integer.parseInt(lakey);
                int y = 1;
                do {

                    x = Integer.toString(y);
                    mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("churchtable").child(x);
                    //mRef.orderByChild("date").startAt(fDate).endAt(tDate);
                    mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();

                            String date = map.get("date");
                            String income_type = map.get("income_type");
                            String profitname = map.get("profitname");
                            String type = map.get("type");
                            Profits profits = new Profits(income_type, profitname, date, type);
                            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                fDate = fromDate.getText().toString();
                                tDate = toDate.getText().toString();
                                Date Cdate = sd.parse(date);
                                Date Fmdate = sd.parse(fDate);
                                Date Tmdate = sd.parse(tDate);

                                if (!Fmdate.after(Cdate) && !Tmdate.before(Cdate)) {

                                    if (type.equals("-")) {
                                        debit = Double.parseDouble(profitname);
                                        debit_sum = debit_sum + debit;
                                        listDataAdapter.add(profits);
                                    }

                                }

                                total_sum = credit_sum - debit_sum;
                                credit_display.setText(credit_sum + "GHc");
                                debit_display.setText(debit_sum + "GHc");
                                total_display.setText(total_sum + "GHc");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    y++;

                } while (y <= key);

                mProgress.dismiss();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void Total_view(){
        total_sum=0;
        debit_sum = 0;
        credit_sum = 0;
        credit=0;
        debit = 0;
        listDataAdapter = new ListDataAdapter(getContext(), R.layout.list_credit_layout);
        listView.setAdapter(listDataAdapter);
        debit_sum = 0;
        credit_sum = 0;
        final String user_id=mAuth.getCurrentUser().getUid();
        mRef=FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map=(Map<String, String>) dataSnapshot.getValue();
                String lakey=map.get("last_key");
                key=Integer.parseInt(lakey);
                int y = 1;
                do {

                    x=Integer.toString(y);
                    mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("churchtable").child(x);
                    //mRef.orderByChild("date").startAt(fDate).endAt(tDate);
                    mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();

                            String date = map.get("date");
                            String income_type = map.get("income_type");
                            String profitname = map.get("profitname");
                            String type = map.get("type");
                            Profits profits = new Profits(income_type, profitname, date, type);
                            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                            try{
                                fDate = fromDate.getText().toString();
                                tDate = toDate.getText().toString();
                                Date Cdate=sd.parse(date);
                                Date Fmdate=sd.parse(fDate);
                                Date Tmdate=sd.parse(tDate);

                                if(!Fmdate.after(Cdate) && !Tmdate.before(Cdate)){
                                    if (type.equals("+")) {
                                        credit = Double.parseDouble(profitname);
                                        credit_sum = credit_sum + credit;

                                    }
                                    if (type.equals("-")) {
                                        debit = Double.parseDouble(profitname);
                                        debit_sum = debit_sum + debit;
                                    }
                                    listDataAdapter.add(profits);
                                }

                                total_sum = credit_sum - debit_sum;
                                credit_display.setText(credit_sum + "GHc");
                                debit_display.setText(debit_sum + "GHc");
                                total_display.setText(total_sum + "GHc");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    y++;

                }while(y<=key);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
/*    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }*/


}
