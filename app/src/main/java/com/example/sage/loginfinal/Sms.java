package com.example.sage.loginfinal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Sms extends AppCompatActivity {

    FirebaseAuth auth;
    EditText e1,e2;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    String verification_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        e1 = (EditText)findViewById(R.id.editText);
        e2 = (EditText)findViewById(R.id.editText2);
        auth = FirebaseAuth.getInstance();

        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verification_code = s;
                Toast.makeText(getApplicationContext(),"Code sent to the number",Toast.LENGTH_SHORT).show();

            }
        };
    }
    public void send_sms(View v)
    {
        String number = e1.getText().toString();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,60, TimeUnit.SECONDS,this,mCallback
        );
    }

    public void signInWithPhone(PhoneAuthCredential credential)
    {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"User signed in successfully",Toast.LENGTH_SHORT).show();
                            Intent intent =new Intent(Sms.this,Inside.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        else if(task.getException() instanceof FirebaseAuthUserCollisionException)
                        {
                            Toast.makeText(getApplicationContext(),"User already exist",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Wrong code. please try again",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void verify(View v)
    {
        String input_code = e2.getText().toString();
        if(verification_code!=null)
        {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification_code,input_code);
            signInWithPhone(credential);
        }


    }

}
