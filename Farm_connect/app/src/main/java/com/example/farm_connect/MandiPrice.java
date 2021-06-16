package com.example.farm_connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;
import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

public class MandiPrice extends AppCompatActivity
{
    DrawerLayout drawerLayout;
    private MandiAdapter adapter;
    private ArrayList priceList;
    private RecyclerView recyclerView;

    private String[] priceLinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandi_price);
        Objects.requireNonNull(getSupportActionBar()).hide();
        drawerLayout = findViewById(R.id.drawer_layout);
        TextView user = findViewById(R.id.username);
        user.setText(UserDashboard.username);
        setBottomNav();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        priceLinks = getResources().getStringArray(R.array.mandilink);

        priceList = new ArrayList<>();

        priceList = new ArrayList();

        String[] array = getResources().getStringArray(R.array.mandi);

        for (int i = 0; i < array.length; i++) {
            priceList.add(array[i]);
        }

        adapter = new MandiAdapter(this, priceList, priceLinks);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        findViewById(R.id.progress).setVisibility(View.GONE);
    }

    private void setBottomNav()
    {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.mandi_price);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),UserDashboard.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.mandi_price:
                        return true;
                    case R.id.discussion_forum:
                        startActivity(new Intent(getApplicationContext(),Discussion.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });
    }



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
        recreate();
    }
    public void ClickProfile(View view)
    {
        UserDashboard.redirectActivity(this,Profile.class);
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
                Toast.makeText(MandiPrice.this,"Logout Successfully",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MandiPrice.this, LoginType.class));
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