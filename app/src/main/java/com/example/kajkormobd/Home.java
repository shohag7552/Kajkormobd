package com.example.kajkormobd;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    TextView b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        loadfragment(new Home_fragment());

        navView.setOnNavigationItemSelectedListener(this);
    }

    private boolean loadfragment(Fragment fragment){
        if (fragment != null){

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();

            return true;
        }
        return false;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment =null;

        switch (menuItem.getItemId()){
            case R.id.navigation_home:
                fragment = new Home_fragment();
                    break;
            case R.id.navigation_favorite:
                if(FirebaseAuth.getInstance().getCurrentUser() == null){
                    Intent in = new Intent(this,LoginActivity.class);
                    startActivity(in);
                }else {
                    fragment =new favorite_fragment();
                }

                break;

            case R.id.navigation_dashboard:
                if(FirebaseAuth.getInstance().getCurrentUser() == null){
                    Intent in = new Intent(this,LoginActivity.class);
                    startActivity(in);
                }else {
                    fragment =new Ad_fragment();
                }

                break;
            case R.id.navigation_Chating:
                if(FirebaseAuth.getInstance().getCurrentUser() == null){
                    Intent in = new Intent(this,LoginActivity.class);
                    startActivity(in);
                }else {
                    fragment =new chat_fragment();
                }

                break;
            case R.id.navigation_notifications:
                if(FirebaseAuth.getInstance().getCurrentUser() == null){
                    Intent in = new Intent(this,LoginActivity.class);
                    startActivity(in);
                }else {
                    fragment = new profile_fragment();
                }
                break;
        }
        return loadfragment(fragment);

    }
}
