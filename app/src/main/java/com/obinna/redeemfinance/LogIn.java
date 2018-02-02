package com.obinna.redeemfinance;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogIn extends Activity {
    private Button mRegisterBtn;
    private EditText mlogmail;
    private EditText mlogpass;
    private Button mlogin;
    private FirebaseAuth mAuth;
    private DatabaseReference mdatabase;
    private ProgressDialog mProgress;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        mAuth= FirebaseAuth.getInstance();
        mdatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        mdatabase.keepSynced(true);

        mProgress=new ProgressDialog(this);

        mRegisterBtn=(Button)findViewById(R.id.register_click);
        mlogmail=(EditText)findViewById(R.id.log_email);
        mlogpass=(EditText)findViewById(R.id.log_password);
        mlogin=(Button)findViewById(R.id.login);

        isNetworkAvailable();

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, Register.class));
            }
        });

    }

    private void checkLogin() {
        String email = mlogmail.getText().toString().trim();
        String password=mlogpass.getText().toString().trim();

        if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){


            mProgress.setMessage("Checking Login...");
            mProgress.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful()){
                       mProgress.dismiss();


                       checkUserExist();
                   }


                   else{
                       mProgress.dismiss();
                       
                       Toast.makeText(LogIn.this,"Error Login",Toast.LENGTH_SHORT).show();
                   }
                }
            });
        }
    }


/*    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }*/



    private void checkUserExist() {
        final String user_id=mAuth.getCurrentUser().getUid();

        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
             if(dataSnapshot.hasChild(user_id)){
                 Intent mainIntent=new Intent(LogIn.this,MyMenu.class);

                 mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                 startActivity(mainIntent);
             }else{
                 Toast.makeText(LogIn.this,"Account Not Found...Create Account",Toast.LENGTH_SHORT).show();
             }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public boolean isNetworkAvailable(){
        ConnectivityManager manager= (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable=false;
        if(networkInfo != null && networkInfo.isConnectedOrConnecting()){
            isAvailable=true;
        }else{
            Toast.makeText(LogIn.this,"Not Connected",Toast.LENGTH_SHORT).show();
        }
        return isAvailable;
    }

}