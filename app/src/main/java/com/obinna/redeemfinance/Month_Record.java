package com.obinna.redeemfinance;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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
 * Created by Dinho on 3/8/2017.
 */

public class Month_Record extends Activity {
    private ListView listView;


    SQLiteDatabase sqLiteDatabase;
    DatabaseOpenHelper helper;
    Cursor cursor;
    ListDataAdapter listDataAdapter;
    TextView credit_display;
    TextView debit_display;
    TextView total_display;
    Spinner month;
    Spinner year;
    String fDate,Smonth;
    String tDate,Syear;
    double credit, debit;
    double total_sum=0, debit_sum = 0, credit_sum = 0;
    String[] MONTHLIST = {"JANUARY", "FEBRUARY", "MARCH","APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
    String[] YEARLIST = {"2017","2018","2019","2020","2021","2022","2023","2024","2025","2026","2027","2028","2029","2030","2031","2032","2033","2034","2035","2036","2037","2038","2039","2040","2041","2042","2043","2044","2045","2046","2047","2048","2049","2050"};
    Button button;
    public int key;
    private DatabaseReference mRef;
    String x;
    PdfPTable table=new PdfPTable(4);
    Document doc=new Document();
    private DatabaseReference mdatabase;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                setContentView(R.layout.month_view);
        final ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, MONTHLIST);
         Spinner betSpin= (Spinner)findViewById(R.id.fromDATE);
        
        betSpin.setAdapter(monthAdapter);
        mdatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        mdatabase.keepSynced(true);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, YEARLIST);
        Spinner yrSpinner = (Spinner)findViewById(R.id.toDATE);
        
        yrSpinner.setAdapter(arrayAdapter);
        mAuth=FirebaseAuth.getInstance();
        listDataAdapter = new ListDataAdapter(this, R.layout.list_credit_layout);
        // Find the GUI components
        listView = (ListView)findViewById(R.id.CreditView);

        credit_display = (TextView)findViewById(R.id.credit_display);
        debit_display = (TextView)findViewById(R.id.debit_display);
        total_display = (TextView)findViewById(R.id.total_display);
        mProgress = new ProgressDialog(this);

        listView.setAdapter(listDataAdapter);
        button=(Button)findViewById(R.id.bView);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.bPDF);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String outPath= Environment.getExternalStorageDirectory()+"/churchtable.pdf";
                try {
                    PdfWriter.getInstance(doc,new FileOutputStream(outPath));
                    doc.open();

                    PdfPCell cell= new PdfPCell(new Paragraph("CHURCH FINANCE CREDIT TABLE"));
                    cell.setColspan(4);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBackgroundColor(BaseColor.BLUE);



                    table.addCell(cell);

                    table.addCell("DATE");
                    table.addCell("AMOUNT TYPE");
                    table.addCell("AMOUNT");
                    table.addCell("TYPE");

                    debit_sum = 0;
                    credit_sum = 0;

                    mRef=FirebaseDatabase.getInstance().getReference();
                    mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Map<String, String> map=(Map<String, String>) dataSnapshot.getValue();
                            String lakey=map.get("last_key");
                            key=Integer.parseInt(lakey);
                            int y = 1;
                            do {

                                x=Integer.toString(y);
                                mRef = FirebaseDatabase.getInstance().getReference().child("churchtable").child(x);
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

                                            table.addCell(date);
                                            table.addCell(income_type);
                                            table.addCell(profitname);
                                            table.addCell(type);
                                            total_sum = credit_sum - debit_sum;
                                            PdfPCell debit= new PdfPCell(new Paragraph("DEBIT:  "+ debit_sum + "GHc"));
                                            debit.setColspan(4);
                                            debit.setHorizontalAlignment(Element.ALIGN_LEFT);
                                            debit.setBackgroundColor(BaseColor.RED);
                                            PdfPCell credit= new PdfPCell(new Paragraph("CREDIT:  "+ credit_sum + "GHc"));
                                            credit.setColspan(4);
                                            credit.setHorizontalAlignment(Element.ALIGN_LEFT);
                                            credit.setBackgroundColor(BaseColor.GREEN);
                                            PdfPCell balance= new PdfPCell(new Paragraph("BALANCE:  "+ total_sum + "GHc"));
                                            balance.setColspan(4);
                                            balance.setHorizontalAlignment(Element.ALIGN_LEFT);

                                            table.addCell(debit);
                                            table.addCell(credit);
                                            table.addCell(balance);
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
                            mProgress.dismiss();
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }


                    });

                    doc.add(table);
                    //table.addCell(debit);
                    //table.addCell(credit);
                    //table.addCell(balance);


                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }



                doc.close();
                Snackbar.make(view, "Pdf created Successfully!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        }


    public void onViewClick(View v){
        if(v.getId()==R.id.bView){
               // double credit, debit;
               //double total_sum=0, debit_sum = 0, credit_sum = 0;
                 month= (Spinner)findViewById(R.id.fromDATE);
                 year= (Spinner)findViewById(R.id.toDATE);
                listDataAdapter = new ListDataAdapter(this, R.layout.list_credit_layout);

                listView.setAdapter(listDataAdapter);

            final String user_id=mAuth.getCurrentUser().getUid();
            mProgress.setMessage("Data Loading...");
            mProgress.show();

                Smonth = month.getSelectedItem().toString();
                Syear = year.getSelectedItem().toString();
                if(Smonth.equals("JANUARY")){
                    fDate=Syear + "-01-01";
                    tDate=Syear + "-01-31";
                }
                if(Smonth.equals("FEBRUARY")){
                    double calc=Double.parseDouble(Syear);
                    calc=(calc)/4;
                    if(calc%4==0) {
                        fDate = Syear + "-02-01";
                        tDate = Syear + "-02-29";
                    }else{
                        fDate = Syear + "-02-01";
                        tDate = Syear + "-02-28";
                    }

                    //String yrcalc=;
                }
                if(Smonth.equals("MARCH")){
                    fDate = Syear + "-03-01";
                    tDate = Syear + "-03-31";
                }
                if(Smonth.equals("APRIL")){
                    fDate = Syear + "-04-01";
                    tDate = Syear + "-04-30";
                }
                if(Smonth.equals("MAY")){
                    fDate = Syear + "-05-01";
                    tDate = Syear + "-05-31";
                }
                if(Smonth.equals("JUNE")){
                    fDate = Syear + "-06-01";
                    tDate = Syear + "-06-30";
                }
                if(Smonth.equals("JULY")){
                    fDate = Syear + "-07-01";
                    tDate = Syear + "-07-31";
                }
                if(Smonth.equals("AUGUST")){
                    fDate = Syear + "-08-01";
                    tDate = Syear + "-08-31";
                }
                if(Smonth.equals("SEPTEMBER")){
                    fDate = Syear + "-09-01";
                    tDate = Syear + "-09-30";
                }
                if(Smonth.equals("OCTOBER")){
                    fDate = Syear + "-10-01";
                    tDate = Syear + "-10-31";
                }
                if(Smonth.equals("NOVEMBER")){
                    fDate = Syear + "-11-01";
                    tDate = Syear + "-11-30";
                }
                if(Smonth.equals("DECEMBER")){
                    fDate = Syear + "-12-01";
                    tDate = Syear + "-12-31";
                }

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
									    //fDate = fromDate.getText().toString();
                                        //tDate = toDate.getText().toString();
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
                        mProgress.dismiss();

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

		}
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
                    Intent mainIntent=new Intent(Month_Record.this,LogIn.class);
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
