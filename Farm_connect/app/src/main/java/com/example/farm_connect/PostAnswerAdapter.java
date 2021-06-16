package com.example.farm_connect;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PostAnswerAdapter extends RecyclerView.Adapter<PostAnswerAdapter.AnswerViewHolder>{

    List<String> answers;
    List<String> answersGivenBy;

    public PostAnswerAdapter()
    {
        answers = new ArrayList<>();
        answersGivenBy = new ArrayList<>();
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.answer;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new AnswerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        String ans = "", answerer = "";
        if(answers.size() > position)
        {
            ans = answers.get(position);
        }
        if(answersGivenBy.size() > position)
        {
            answerer = answersGivenBy.get(position);
        }
        holder.answer_text.setText(ans);
        holder.answeredBy_view.setText(answerer);
        Log.d("onBindView: ", "position " + position);
    }

    @Override
    public int getItemCount() {
        if(answers != null)
        {
            return answers.size();
        }
        return 0;
    }


    public static class AnswerViewHolder extends RecyclerView.ViewHolder {

        TextView answer_text;
        TextView answeredBy_view;
        public AnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            answer_text = itemView.findViewById(R.id.post_answer);
            answeredBy_view = itemView.findViewById(R.id.answeredBy);
        }
    }

    public void setAdapter(List<String> ans, List<String> answeredBy)
    {
        if(ans == null)
        {
            answers.clear();
            answersGivenBy.clear();
        }
        else
        {
            answers = ans;
            answersGivenBy = answeredBy;
        }
        notifyDataSetChanged();
    }

    public void addAnswerInAdapter(String ans)
    {
        answers.add(ans);
        notifyDataSetChanged();
    }

    public void addRepliersInAdapter(String answeredBy)
    {
        answersGivenBy.add(answeredBy);
        notifyDataSetChanged();
    }


    public void setAnswersAdapter(List<String> ans)
    {
        answers = ans;
        notifyDataSetChanged();
    }

    public void setRepliersAdapter(List<String> answeredBy)
    {
        answersGivenBy = answeredBy;
        notifyDataSetChanged();
    }


}
