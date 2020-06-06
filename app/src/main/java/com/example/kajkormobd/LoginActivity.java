package com.example.kajkormobd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;


import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private EditText phnnum_edittext;
    private ImageButton back_button;
    String UserPhoneNumber;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        phnnum_edittext = findViewById(R.id.phone_edit);
        back_button = findViewById(R.id.back_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Home_fragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName())
                        .addToBackStack(null)
                        .commit();
            }
        });

        findViewById(R.id.sendcodebtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = phnnum_edittext.getText().toString().trim();

                if (number.isEmpty()|number.length() < 11 ){
                    phnnum_edittext.setError("Provide a valid number");
                    phnnum_edittext.requestFocus();
                    return;
                }

                String phoneNumber = number;



                Intent intent =new Intent(LoginActivity.this, workerapplication3.class);
                intent.putExtra("phoneNumber", phoneNumber);
                startActivity(intent);



            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser() != null){

            Intent intent = new Intent(this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);

        }
    }
}
