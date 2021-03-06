package com.example.farm_connect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class LoginType extends AppCompatActivity
{
    Button signMailBtn , createAccountBtn , SignPhoneBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_type);
        Objects.requireNonNull(getSupportActionBar()).hide();
        signMailBtn = (Button)findViewById(R.id.Sign_with_mailInLoginType);
        createAccountBtn = (Button)findViewById(R.id.create_account_btn_loginType);
        SignPhoneBtn = (Button)findViewById(R.id.Sign_in_with_phone_btn_login_type);
        signMailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
                startActivity(new Intent(LoginType.this , LoginwithEmail.class));
            }
        });
       createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(LoginType.this , Register.class));
            }
        });
        SignPhoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(LoginType.this , PhoneLogin.class));
            }
        });
    }
}
