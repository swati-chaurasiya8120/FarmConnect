package com.example.farm_connect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farm_connect.firebase.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PostQuestionActivity extends AppCompatActivity {

    private TextView mTitle, mTags;
    private AutoCompleteTextView mQuestion;
    private Button post_button;
    private static final int WORDS_IN_LINE = 34;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_question);
        Objects.requireNonNull(getSupportActionBar()).hide();
        String token = MyFirebaseMessagingService.getToken(this);
        Log.d("AuthToken ", token);
        DatabaseHandler.saveAuthToken(token);

        mTitle = findViewById(R.id.title_text_view);
        mQuestion = findViewById(R.id.user_question);
        mTags = findViewById(R.id.tags);
        post_button = findViewById(R.id.post_button);

        mTitle.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0){
                    post_button.setEnabled(false);
                } else {
                    post_button.setEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() % WORDS_IN_LINE == 0)
                {
                    s.append('\n');
                }
            }
        });

        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question;
                String title;

                if(TextUtils.isEmpty(mQuestion.getText()) || TextUtils.isEmpty(mTitle.getText()))
                {
                    Toast.makeText(PostQuestionActivity.this, R.string.empty_edit_text, Toast.LENGTH_SHORT).show();
                    return;
                }

                title = mTitle.getText().toString().trim();
                question = mQuestion.getText().toString().trim();

                String[] tags = null;
                List<String> Tags = null;
                if(!TextUtils.isEmpty(mTags.getText()))
                {
                    String tag = mTags.getText().toString();
                    tags = new String[tag.length()];
                    Tags = new ArrayList<>();

                    tags = tag.split(",");

                    for(int i = 0; i < tags.length; i++)
                    {
                        Tags.add(tags[i].trim());
                    }
                }

                Intent intent = new Intent();
                intent.putExtra("title", title);
                intent.putExtra("question", question);
                intent.putExtra("tags", tags);
                intent.putStringArrayListExtra("tags", (ArrayList<String>)Tags);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}