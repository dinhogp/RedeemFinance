package com.obinna.redeemfinance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by hp 14 on 1/8/2017.
 */

public class MyMenu extends Activity {

    private DatabaseReference mDatabase;
    private DatabaseReference mdatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mdatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        mdatabase.keepSynced(true);
        mAuth=FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() == null ){
                    Intent logIntent=new Intent(MyMenu.this,LogIn.class);
                    logIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(logIntent);
                }
            }
        };
        isNetworkAvailable();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void oncreditBClick(View v){
        if(v.getId()==R.id.creditB){
           startActivity(new Intent(MyMenu.this,Profit_Menu.class));
        }
    }
	
    public void ondebitBClick(View v){
        if(v.getId()==R.id.debitB){
           startActivity(new Intent(MyMenu.this,Debit_Menu.class));
        }
    }
	
    public void onmonthBClick(View v){
        if(v.getId()==R.id.monthB){
           startActivity(new Intent(MyMenu.this,MyCalc.class));
        }
    }

    public void onrecordBClick(View v){
        if(v.getId()==R.id.recordB){
           startActivity(new Intent(MyMenu.this,Scroll_menu.class));
        }
    }

    public void onLogOutClick(View v){
        if(v.getId()==R.id.bLogOut){
            logOut();
        }
    }

    public boolean isNetworkAvailable(){
        ConnectivityManager manager= (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable=false;
        if(networkInfo != null && networkInfo.isConnectedOrConnecting()){
            isAvailable=true;
        }else{
            Toast.makeText(MyMenu.this,"Not Connected",Toast.LENGTH_SHORT).show();
        }
        return isAvailable;
    }
    private void logOut() {
        mAuth.signOut();
    }

}
