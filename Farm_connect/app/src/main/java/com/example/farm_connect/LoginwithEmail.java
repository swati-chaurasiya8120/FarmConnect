package com.example.farm_connect;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;

public class LoginwithEmail extends AppCompatActivity {

    // declaration
    EditText EmailForLogin , Password_for_login;
    Button LoginPageLoginButton, LoginPageSignUpButton;
    TextView forgotPasswordTextInLoginPage;
    CheckValidation checkValidation_mail;
    FirebaseAuth fAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginwith_email);
        Objects.requireNonNull(getSupportActionBar()).hide();

        // initialization
        LoginPageLoginButton= (Button)findViewById(R.id.Login_btn_login_page);
        EmailForLogin = (EditText)findViewById(R.id.InputEmailInLogin);
        Password_for_login = (EditText)findViewById(R.id.InputLoginPassword);
        LoginPageSignUpButton = (Button)findViewById(R.id.Sign_up_btn_loginPage);
        forgotPasswordTextInLoginPage = (TextView)findViewById(R.id.forgot_password_login_page);
        fAuth = FirebaseAuth.getInstance() ;


        LoginPageLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email_for_login , password_for_login;

                email_for_login = EmailForLogin.getText().toString().trim();
                GlobalVariable.globalemail = email_for_login;
                checkValidation_mail = new CheckValidation(email_for_login);
                password_for_login  = Password_for_login.getText().toString().trim();


                if(email_for_login.isEmpty())
                    EmailForLogin.setError("Email is Required ");
                else if (!checkValidation_mail.isEmailValid())
                    EmailForLogin.setError("Email is Invalid");
                else if(password_for_login.isEmpty())
                    Password_for_login.setError("Password is required");
                else if(password_for_login.length() < 8)
                    Password_for_login.setError("Invalid Password");
                else {

                    // sign using registered email and password
                    fAuth.signInWithEmailAndPassword(email_for_login, password_for_login).addOnCompleteListener(LoginwithEmail.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {

                                Toast.makeText(LoginwithEmail.this, "Sign In Unsuccessful , Please try again", Toast.LENGTH_SHORT).show();
                            } else {
                                String username = GlobalVariable.getGlobalglobalemail();
                                finish();
                                Toast.makeText(LoginwithEmail.this, "Sign In Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginwithEmail.this,UserDashboard.class);
                                intent.putExtra("usern",username);
                                startActivity(intent);


                            }
                        }
                    });

                }
            }
        });

        // sign btn code
        // will take you  to the registration page where you can register again
        LoginPageSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });


        // forgot password code
        forgotPasswordTextInLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText resetMail = new EditText(v.getContext()) ;
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext()) ;
                passwordResetDialog.setTitle("Reset Password ?") ;
                passwordResetDialog.setMessage("Enter your Email to receive Reset Link") ;
                passwordResetDialog.setView(resetMail) ;

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String mail = resetMail.getText().toString() ;
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(LoginwithEmail.this, "Reset Link sent to your email.", Toast.LENGTH_SHORT).show() ;
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(LoginwithEmail.this, "Error ! Reset Link is not sent."+e.getMessage(), Toast.LENGTH_SHORT).show() ;

                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }) ;

                passwordResetDialog.create().show();

            }
        });

        forgotPasswordTextInLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText resetMail = new EditText(v.getContext()) ;
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext()) ;
                passwordResetDialog.setTitle("Reset Password ?") ;
                passwordResetDialog.setMessage("Enter your Email to receive Reset Link") ;
                passwordResetDialog.setView(resetMail) ;

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String mail = resetMail.getText().toString() ;
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(LoginwithEmail.this, "Reset Link sent to your email.", Toast.LENGTH_SHORT).show() ;
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(LoginwithEmail.this, "Error ! Reset Link is not sent."+e.getMessage(), Toast.LENGTH_SHORT).show() ;

                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }) ;

                passwordResetDialog.create().show();

            }
        });


    }
}