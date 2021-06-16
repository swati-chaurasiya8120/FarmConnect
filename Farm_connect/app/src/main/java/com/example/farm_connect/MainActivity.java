package com.example.farm_connect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.os.Handler;


import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity
{

    FirebaseAuth firebaseAuthSplash;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       Objects.requireNonNull(getSupportActionBar()).hide();

        firebaseAuthSplash = FirebaseAuth.getInstance();
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {

                    startActivity(new Intent(getApplicationContext(), LoginType.class));
                    finish();

                    if(firebaseAuthSplash.getCurrentUser() == null)
                    {
                        startActivity(new Intent(getApplicationContext(), LoginType.class));
                        finish();
                    }
                    else
                    {

                        GlobalVariable.globalemail = firebaseAuthSplash.getCurrentUser().getEmail();
                        startActivity(new Intent(getApplicationContext(),UserDashboard.class));
                        finish();
                    }
                }


        },3000);
    }
}