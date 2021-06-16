package com.example.farm_connect;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckValidation {

    private String Mail_Phone;

    public CheckValidation(){

    }
    public CheckValidation(String  Email_phone)
    {
        this.Mail_Phone = Email_phone;
    }

    public boolean isEmailValid()
    {
        String regex = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9.-]+$";
        Pattern Email_pattern = Pattern.compile(regex);
        Matcher matcher = Email_pattern.matcher(Mail_Phone);
        return matcher.matches();

    }
    public boolean isPhoneNumberValid()
    {
        String phone = "^[6-9]\\d{9}$";
        Pattern Email_pattern = Pattern.compile(phone);
        Matcher matcher = Email_pattern.matcher(Mail_Phone);
        return matcher.matches();
    }
}
