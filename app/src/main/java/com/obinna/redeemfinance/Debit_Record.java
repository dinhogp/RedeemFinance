package com.obinna.redeemfinance;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.Date;
import java.util.Map;

/**
 * Created by Dinho on 2/23/2017.
 */

public class Debit_Record extends Activity

{


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
    Spinner Fspin;
    Spinner Tspin;
    String fDate;
    String tDate;
    double credit, debit;
    double total_sum = 0, debit_sum = 0, credit_sum = 0;
    //public static Context context;
    private DatabaseReference mRef;
    String x;
    public int key;
    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    Button button;
    private DatabaseReference mdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debit_layout);

        listDataAdapter = new ListDataAdapter(this, R.layout.list_credit_layout);
        // Find the GUI components
        mdatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        mdatabase.keepSynced(true);
        //context=getActivity().getApplicationContext();
        listView = (ListView) findViewById(R.id.CreditView);

        mAuth=FirebaseAuth.getInstance();
        credit_display = (TextView) findViewById(R.id.credit_display);
        debit_display = (TextView) findViewById(R.id.debit_display);
        total_display = (TextView) findViewById(R.id.total_display);
        fromDate = (EditText) findViewById(R.id.fromDATE);
        toDate = (EditText) findViewById(R.id.toDATE);
        // helper = new DatabaseOpenHelper(this);
        //this.btnAdd = (Button) findViewById(R.id.btnAdd);
        listView.setAdapter(listDataAdapter);
        fromDate.setInputType(InputType.TYPE_NULL);
        toDate.setInputType(InputType.TYPE_NULL);
        mProgress = new ProgressDialog(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.bPDF);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Debit_Record.this, Debit_Menu.class));
            }
        });

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateDialog Fdialog = new DateDialog(v);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fdialog.show(ft, "DatePicker");


            }


        });
        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateDialog Fdialog = new DateDialog(v);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fdialog.show(ft, "DatePicker");


            }


        });


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
                    Intent mainIntent=new Intent(Debit_Record.this,LogIn.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void onViewClick(View v) {
        if (v.getId() == R.id.bView) {
            //double credit, debit;

            listDataAdapter = new ListDataAdapter(this, R.layout.list_credit_layout);
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
    }



    }

