package com.example.kajkormobd;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class workerapplication3 extends AppCompatActivity {

    private String VerificationID;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar1;
    private EditText editText;
    private String phoneNumber;


    private ProgressDialog loadingBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workerapplication3);

        mAuth =FirebaseAuth.getInstance();

        editText = findViewById(R.id.verify_edit);
        progressBar1 = findViewById(R.id.progressBar33);

        loadingBar = new ProgressDialog(this);


        phoneNumber = getIntent().getStringExtra("phoneNumber");

        sendVerificationCode(phoneNumber);

        findViewById(R.id.sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code =editText.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6){
                    editText.setError("Enter Code....");
                    editText.requestFocus();
                    return;
                }
                //SendUserToProfile();


                verifyCode(code);

            }
        });


    }

    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerificationID,code);
        signinWithCredential(credential);
    }

    private void signinWithCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    loadingBar.setTitle("Adding Profile");
                    loadingBar.setMessage("Please Wait");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();



                    Toast.makeText(workerapplication3.this, "successful", Toast.LENGTH_SHORT).show();

                    SendUserToProfile();


                }
                else {
                    Toast.makeText(workerapplication3.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void SendUserToProfile()
    {



        Intent profileIntent = new Intent(workerapplication3.this,worker_photo_name_activity.class);
        profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        profileIntent.putExtra("phoneNumber", phoneNumber );
        startActivity(profileIntent);
        finish();
    }


    private void sendVerificationCode(String number){

        progressBar1.setVisibility(View.VISIBLE);


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private  PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack =new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            VerificationID = s;


        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if(code != null){

                editText.setText(code);
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(workerapplication3.this, "Network Problem"+ e, Toast.LENGTH_LONG).show();
        }
    };


}
