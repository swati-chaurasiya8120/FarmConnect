package com.example.farm_connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farm_connect.firebase.DatabaseHandler;
import com.example.farm_connect.notifications.FcmNotificationsSender;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PostAnswerActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TextView posted_question;
    private MultiAutoCompleteTextView autoCompleteTextView;
    private Button postButton;
    private PostAnswerAdapter mPostAnswerAdapter;
    private Context context;
    private List<String> answers, answeredBy;
    String uniqueID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_answer);
        Objects.requireNonNull(getSupportActionBar()).hide();
        String question = "";
        answers = null;
        answeredBy = null;

        context = getApplicationContext();

        Intent intent = getIntent();
        if(intent != null)
        {
            question    = intent.getStringExtra("question");
            uniqueID    = intent.getStringExtra("unique_id");
            answers     = intent.getStringArrayListExtra("answers");
            answeredBy  = intent.getStringArrayListExtra("answeredBy");
        }

        posted_question = findViewById(R.id.posted_question);
        mRecyclerView = findViewById(R.id.answers_view);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(PostAnswerActivity.this, DividerItemDecoration.VERTICAL));
        autoCompleteTextView = findViewById(R.id.user_answer);
        postButton = findViewById(R.id.post_it_bro);

        posted_question.setText(question);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mPostAnswerAdapter = new PostAnswerAdapter();
        mPostAnswerAdapter.setAdapter(null, null);

        mRecyclerView.setAdapter(mPostAnswerAdapter);



        postButton.setOnClickListener(v -> {                //click listener here.......

            if(!TextUtils.isEmpty(autoCompleteTextView.getText()))
            {
                if(answers == null)
                {
                    answers = new ArrayList<>();
                    answeredBy = new ArrayList<>();
                }

                answers.add(autoCompleteTextView.getText().toString());
                answeredBy.add(UserDashboard.username); // add correct username

                autoCompleteTextView.setText("");

                DatabaseHandler.addAnswersAndRepliers(uniqueID, answers, answeredBy);

                Toast.makeText(PostAnswerActivity.this, "answer added successfully", Toast.LENGTH_SHORT)
                        .show();


                DatabaseReference dbReference = DatabaseHandler.getDatabaseReferenceOfUser(uniqueID);
                Task<DataSnapshot> task = dbReference.get();
                task.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        UniqueIdentification user = dataSnapshot.getValue(UniqueIdentification.class);
                        String token = user.getAuth_token();
                        Log.d("ttoookeeennn", token);

                        FcmNotificationsSender fcm = new FcmNotificationsSender(token, "Hurray!", "Hey, somebody answered your question.",
                                                     context, PostAnswerActivity.this);

                        fcm.SendNotifications();
                    }
                });

            }

            else
            {
                Toast.makeText(PostAnswerActivity.this, "answer field is empty", Toast.LENGTH_SHORT)
                        .show();
            }
        });


        DatabaseReference dbRefToAnswers = DatabaseHandler.getDatabaseReferenceToAnswers(uniqueID);
        DatabaseReference dbRefToRepliers = DatabaseHandler.getDatabaseReferenceToRepliers(uniqueID);

        dbRefToAnswers.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> ans = (List<String>) snapshot.getValue();

                if(ans != null && !ans.isEmpty())
                {
                    int lastPosition = ans.size() - 1;
                    mPostAnswerAdapter.setAnswersAdapter(ans);
                    Log.d("Answers", "answeres datachange " + ans.get(lastPosition));
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        dbRefToRepliers.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> repliers = (List<String>)snapshot.getValue();

                if(repliers != null && !repliers.isEmpty())
                {
                    int lastPosition = repliers.size() - 1;
                    mPostAnswerAdapter.setRepliersAdapter(repliers);
                    Log.d("Repliers", "repliers datachange " + repliers.get(lastPosition));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}