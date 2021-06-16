package com.example.farm_connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.Address;
import android.location.Geocoder;

import cz.msebera.android.httpclient.Header;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class UserDashboard extends AppCompatActivity
{
    private DrawerLayout drawerLayout;
    public static FirebaseAuth fAuth;
    public static String username ;
    final String APP_ID = "dab3af44de7d24ae7ff86549334e45bd";
    final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    final int REQUEST_CODE = 44;
    private TextView NameofCity;
    private TextView weatherState;
    private TextView Temperature;
    private FusedLocationProviderClient fusedLocationProviderClient ;
    public static String cityName;
    private PolicyAdapter adapter;
    private  BlogAdapter blogAdapter;
    private ArrayList list;
    private ArrayList list2;
    private RecyclerView recyclerView;
    private String[] links;
    private String[] bloglinks;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        Objects.requireNonNull(getSupportActionBar()).hide();

        fAuth = FirebaseAuth.getInstance();
        //drawer layout
        drawerLayout=findViewById(R.id.drawer_layout);

        //set user name
        TextView user = findViewById(R.id.username);
        username = getIntent().getStringExtra("usern");
        user.setText(username);

        //weather information
        weatherState = findViewById(R.id.weathercondition);
        Temperature = findViewById(R.id.tempreture);
        NameofCity = findViewById(R.id.cityname);

        //set Bottom Navigation
        setBottomNav();
        //Weather Info
        getCurrLocation();
        //Govt Policy
        govtPolicy();
        //blog
        blog();
    }

    private void blog()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bloglinks = getResources().getStringArray(R.array.bloglink);

        list2 = new ArrayList<>();

        list2 = new ArrayList();

        String[] array = getResources().getStringArray(R.array.blog);

        for (int i = 0; i < array.length; i++) {
            list2.add(array[i]);
        }

        blogAdapter = new BlogAdapter(this, list2, bloglinks);

        recyclerView = (RecyclerView) findViewById(R.id.recycler1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(blogAdapter);
        findViewById(R.id.progress1).setVisibility(View.GONE);

    }

    private void govtPolicy()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        links = getResources().getStringArray(R.array.policies_link);

        list = new ArrayList<>();

        list = new ArrayList();

        String[] array = getResources().getStringArray(R.array.policies);

        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }

        adapter = new PolicyAdapter(this, list, links);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        findViewById(R.id.progress).setVisibility(View.GONE);


     }

    private void setBottomNav()
    {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        return true;
                    case R.id.mandi_price:
                        startActivity(new Intent(getApplicationContext(),MandiPrice.class));
                        overridePendingTransition(0,0);
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

    private void getCurrLocation()
    {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if(ActivityCompat.checkSelfPermission(UserDashboard.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if(location != null)
                    {
                        try {
                            Geocoder geocoder = new Geocoder(UserDashboard.this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                            String city = (addresses.get(0).getLocality());
                            cityName = city;
                            RequestParams params=new RequestParams();
                            params.put("q",city);
                            params.put("appid",APP_ID);
                            getDataFromJson(params);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        else
        {
            ActivityCompat.requestPermissions(UserDashboard.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==REQUEST_CODE)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(UserDashboard.this,"Get Location Succesffully",Toast.LENGTH_SHORT).show();
                getCurrLocation();
            }
            else
            {
                Toast.makeText(UserDashboard.this,"Access Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private  void getDataFromJson(RequestParams params)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL,params,new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                Toast.makeText(UserDashboard.this,"Get data Successfully",Toast.LENGTH_SHORT).show();
                weatherData weatherD=weatherData.fromJson(response);
                Temperature.setText(weatherD.getmTemperature());
                NameofCity.setText(weatherD.getMcity());
                weatherState.setText(weatherD.getmWeatherType());
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                Toast.makeText(UserDashboard.this,"No Data Found",Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void ClickMenu(View view)
    {
        openDrawer(drawerLayout);
    }

    public  void ClickLogo(View view)
    {
        closeDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout)
    {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickHome(View view)
    {
        recreate();
    }
    public void ClickProfile(View view)
    {
        redirectActivity(this,Profile.class);
    }

    public void ClickMandi(View view)
    {
        redirectActivity(this,MandiPrice.class);
    }

    public void  ClickDiscussion(View view)
    {
        redirectActivity(this,Discussion.class);
    }
    public void ClickLogout(View view)
    {
        logout(this);
    }
    public static void closeDrawer(DrawerLayout drawerLayout)
    {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void logout(Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout ?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fAuth.signOut();
                Toast.makeText(UserDashboard.this,"Logout Successfully",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserDashboard.this, LoginType.class));
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

    public static void redirectActivity(Activity activity,Class aClass)
    {
        Intent intent = new Intent(activity,aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);

    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}