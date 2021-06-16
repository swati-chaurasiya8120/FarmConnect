package com.example.farm_connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farm_connect.firebase.DatabaseHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Discussion extends AppCompatActivity implements QuestionAdapter.QuestionOnClickListener {

    private DatabaseReference mDatabaseReference;
    private RecyclerView mRecyclerView;
    private QuestionAdapter mQuestionAdapter;
    private ChildEventListener mChildEventListener;
    private List<String> selectedCrops;
    AlertDialog.Builder alertdialogbuilder;

    String[] crops;

    List<String> ItemsIntoList;

    boolean[] Selectedtruefalse = new boolean[]{
            false, false, false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false, false, false,
            false, false
    };
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);
        //Objects.requireNonNull(getSupportActionBar()).hide();
        drawerLayout = findViewById(R.id.drawer_layout);
        TextView user = findViewById(R.id.username);
        user.setText(UserDashboard.username);
        setBottomNav();
        mDatabaseReference = DatabaseHandler.getDatabaseReferenceOfQuestionRoot();

        mRecyclerView = findViewById(R.id.display_questions);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(Discussion.this, DividerItemDecoration.VERTICAL));

        selectedCrops = new ArrayList<>();

        crops = this.getResources().getStringArray(R.array.crops);

        mChildEventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Question question = snapshot.getValue(Question.class);
                mQuestionAdapter.addQuestionInAdapter(question);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        mDatabaseReference.addChildEventListener(mChildEventListener);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mQuestionAdapter = new QuestionAdapter(this, crops);
        mRecyclerView.setAdapter(mQuestionAdapter);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 200)
        {
            if(resultCode == RESULT_OK)
            {
                String ques = data.getStringExtra("question");
                String title = data.getStringExtra("title");
                List<String> tags = data.getStringArrayListExtra("tags");

                Question q = new Question(title, ques, null, tags);

                DatabaseHandler.addQuestion(q);
            }
        }
    }

    @Override
    public void onClick(String ques, String unique_id, List<String> answers, List<String> answeredBy) {

        Intent intent = new Intent(Discussion.this ,PostAnswerActivity.class);
        intent.putExtra("question", ques);
        intent.putExtra("unique_id", unique_id);
        intent.putStringArrayListExtra("answers", (ArrayList<String>)answers);
        intent.putStringArrayListExtra("answeredBy", (ArrayList<String>)answeredBy);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.menu_forum, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_ask_a_question) {
            Intent intent = new Intent(Discussion.this, PostQuestionActivity.class);
            startActivityForResult(intent, 200);
            return true;
        }

        else if(id == R.id.customize_result)
        {
            alertdialogbuilder = new AlertDialog.Builder(Discussion.this);
            ItemsIntoList = Arrays.asList(crops);
            alertdialogbuilder.setMultiChoiceItems(crops, Selectedtruefalse, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    if(isChecked)
                    {
                        selectedCrops.add(crops[which]);
                        Selectedtruefalse[which] = true;
                    }
                    else
                    {
                        selectedCrops.remove(crops[which]);
                        Selectedtruefalse[which] = false;
                    }
                }
            });

            alertdialogbuilder.setCancelable(false);

            alertdialogbuilder.setTitle("Select Crops here");

            alertdialogbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mQuestionAdapter.setAdapterWithGivenCrops(selectedCrops);
                }
            });

            alertdialogbuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog dialog = alertdialogbuilder.create();
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
    private void setBottomNav()
    {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.discussion_forum);

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
                        startActivity(new Intent(getApplicationContext(),MandiPrice.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.discussion_forum:
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
    public void ClickProfile(View view)
    {
        UserDashboard.redirectActivity(this,Profile.class);
    }
    public void ClickMandi(View view)
    {
        UserDashboard.redirectActivity(this,MandiPrice.class);
    }
    public void  ClickDiscussion(View view)
    {
        recreate();
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
                Toast.makeText(Discussion.this,"Logout Successfully",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Discussion.this, LoginType.class));
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