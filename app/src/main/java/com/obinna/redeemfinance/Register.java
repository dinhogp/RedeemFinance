package com.obinna.redeemfinance;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends Activity {
    private EditText mUserName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mBranch;

    private Button mRegisterBtn;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");

        mRegisterBtn=(Button)findViewById(R.id.idsign);
        mProgress=new ProgressDialog(this);
        mUserName=(EditText) findViewById(R.id.id_user);
        mPassword=(EditText) findViewById(R.id.id_password);
        mEmail=(EditText) findViewById(R.id.id_email);
        mBranch=(EditText) findViewById(R.id.id_branch);

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });
    }

    private void startRegister() {
        final String username=mUserName.getText().toString();
        String email=mEmail.getText().toString();
        String password=mPassword.getText().toString();
        final String branch=mBranch.getText().toString();

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(branch)) {
            mProgress.setMessage("Signing Up...");
            mProgress.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        String user_id=mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user=mDatabase.child(user_id);
                        current_user.child("Username").setValue(username);
                        current_user.child("RCCGbranch").setValue(branch);

                        mProgress.dismiss();
                        Intent mainIntent=new Intent(Register.this,LogIn.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);

                    } else{
                        mProgress.dismiss();
                        Toast temp = Toast.makeText(Register.this, "Auth failed...!", Toast.LENGTH_SHORT);
                        temp.show();
                    }
                }
            });
        }
    }
}
