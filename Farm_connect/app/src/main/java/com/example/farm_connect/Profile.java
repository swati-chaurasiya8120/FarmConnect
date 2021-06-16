package com.example.farm_connect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Profile extends AppCompatActivity
{

    DrawerLayout drawerLayout;
    Button logout;
    FirebaseAuth fAuth;
    //crating firebase instance
    private FirebaseFirestore fstore;
    private String userId;
    private TextView usernameProfile , EmailProfile , ContactProfile,Location;
    private FirebaseDatabase database;
    private DatabaseReference userReference;
    private static final String USERS = "users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Objects.requireNonNull(getSupportActionBar()).hide();
        TextView user = findViewById(R.id.username);
        user.setText(UserDashboard.username);
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        drawerLayout = findViewById(R.id.drawer_layout);
        usernameProfile = findViewById(R.id.textViewName);
        usernameProfile.setText(UserDashboard.username);

        EmailProfile = findViewById(R.id.textViewuserEmail);
        String useremail = UserDashboard.username+"@gmail.com";
        EmailProfile.setText(useremail);
        Location = findViewById(R.id.textViewLocation);
        Location.setText(UserDashboard.cityName);
      //  ContactProfile = findViewById(R.id.textViewuserContact);
        logout = findViewById(R.id.button_logout);
        database = FirebaseDatabase.getInstance();
        userReference = database.getReference(USERS);
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren())
                {
                    if(ds.child("email").getValue().equals("email"))
                    {
                        usernameProfile.setText(ds.child("fullName").getValue(String.class));
                        EmailProfile.setText(email);
                        ContactProfile.setText(ds.child("contact").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


       /* fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        DocumentReference documentReference = fstore.collection(GlobalVariable.getGlobalglobalemail()).document(userId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Profile user = documentSnapshot.toObject(Profile.class);
                usernameProfile.setText(documentSnapshot.getString("name"));
                EmailProfile.setText(documentSnapshot.getString("email"));
                ContactProfile.setText(documentSnapshot.getString("PhoneNumber"));

            }
        });*/

    }




    @Nullable

    public void ClickMenu(View view)
    {
        UserDashboard.openDrawer(drawerLayout);
    }
    public void ClickLogo(View view)
    {
        UserDashboard.closeDrawer(drawerLayout);
    }
    public void ClickHome(View view)
    {
        UserDashboard.redirectActivity(this,UserDashboard.class);
    }
    public void ClickMandi(View view)
    {
        UserDashboard.redirectActivity(this,MandiPrice.class);
    }
    public void ClickProfile(View view)
    {
        recreate();
    }
    public void  ClickDiscussion(View view)
    {
        UserDashboard.redirectActivity(this,Discussion.class);
    }
    public void ClickLogout(View view)
    {
        logout(this);
    }

    public void logout(Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout ?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserDashboard.fAuth.signOut();
                Toast.makeText(Profile.this,"Logout Successfully",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Profile.this, LoginType.class));
                activity.finishAffinity();
                System.exit(0);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    @Override
    protected void onPause() {
        super.onPause();
        UserDashboard.closeDrawer(drawerLayout);
    }
}