package com.example.farm_connect;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionAdapterViewHolder> {

    private List<Question> mQuestions;
    private ArrayList<Question> mQuestions_2;
    HashSet<String> selectedCrops;
    private QuestionOnClickListener onClickListener;
    String[] crops;

    HashMap<String, ArrayList<Integer>> mapOfCrops;

    public interface QuestionOnClickListener
    {
        void onClick(String ques, String unique_id, List<String> answers, List<String> answeredBy);
    }


    public QuestionAdapter(QuestionOnClickListener clickListener, String[] Crops) {
        super();
        crops = Crops;
        mQuestions = new ArrayList<>();
        mQuestions_2 = new ArrayList<>();
        mapOfCrops = new HashMap<>();
        for(String s: crops)
        {
            mapOfCrops.put(s, new ArrayList<Integer>());
        }
        selectedCrops = new HashSet<>();
        onClickListener = clickListener;
    }

    @NonNull
    @Override
    public QuestionAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.question;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new QuestionAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapterViewHolder holder, int position) {
        if(mQuestions != null)
        {
            Question obj = mQuestions.get(position);
            String title = obj.getTitle();
            List<String> tags = obj.getTags();
            holder.question_textview.setText(title);


            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClick(obj.getQuestion(), obj.getUniqueID(), obj.getAnswers(), obj.getAnswersGivenBy());
                }
            });

            if(tags != null)
            {
                if(tags.size() == 3)
                {
                    holder.tag1_text_view.setText(tags.get(0));
                    holder.tag2_text_view.setText(tags.get(1));
                    holder.tag3_text_view.setText(tags.get(2));
                }
                else if(tags.size() == 2)
                {
                    holder.tag1_text_view.setText(tags.get(0));
                    holder.tag2_text_view.setText(tags.get(1));
                    holder.tag3_text_view.setVisibility(View.INVISIBLE);
                }
                else if(tags.size() == 1)
                {
                    holder.tag1_text_view.setText(tags.get(0));
                    holder.tag2_text_view.setVisibility(View.INVISIBLE);
                    holder.tag3_text_view.setVisibility(View.INVISIBLE);
                }
            }
            else
            {
                holder.tag1_text_view.setText(R.string.no_tags);
                holder.tag2_text_view.setVisibility(View.INVISIBLE);
                holder.tag3_text_view.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if(mQuestions != null)
        {
            return mQuestions.size();
        }
        return 0;
    }


    public static class QuestionAdapterViewHolder extends RecyclerView.ViewHolder
    {
        TextView question_textview;
        TextView tag1_text_view, tag2_text_view, tag3_text_view;
        View view;

        public QuestionAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            question_textview = (TextView) itemView.findViewById(R.id.question_title);
            tag1_text_view = itemView.findViewById(R.id.tag1_text_view);
            tag2_text_view = itemView.findViewById(R.id.tag2_text_view);
            tag3_text_view = itemView.findViewById(R.id.tag3_text_view);
            view = itemView;

        }
    }

    /**
     * To change the contents in the view */
    public void setQuestionsInAdapter(ArrayList<Question> ques)
    {
        mQuestions = ques;
        notifyDataSetChanged();
    }

    /**
     * To add another question in recycler view */
    public void addQuestionInAdapter(Question ques)
    {
        int count = 0;
        mQuestions_2.add(ques);
        List<String> tags = ques.getTags();
        if(tags != null)
        {
            for(int i = 0; i < tags.size(); i++)
            {
                if(mapOfCrops.containsKey(tags.get(i)))
                {
                    ArrayList<Integer> arr = mapOfCrops.get(tags.get(i));
                    arr.add(mQuestions_2.size() - 1);
                }

                if(selectedCrops.isEmpty() || (selectedCrops.contains(tags.get(i)) && count == 0))
                {
                    mQuestions.add(ques);
                    Log.d("oookkk", "in here");
                    count++;
                }
            }
        }

        notifyDataSetChanged();
    }

    public void setAdapterWithGivenCrops(List<String> crops)
    {
        List<Question> newQuestions = new ArrayList<>();
        selectedCrops.clear();
        selectedCrops.addAll(crops);
        for(String s: selectedCrops)
        {
            Log.d("Crop: ", s);
        }

        LinkedHashSet<Integer> UniqueList = new LinkedHashSet<Integer>();
        for(int i = 0; i < crops.size(); i++)
        {
            if(mapOfCrops.containsKey(crops.get(i)))
            {
                UniqueList.addAll(Objects.requireNonNull(mapOfCrops.get(crops.get(i))));
            }
        }

        for(int i: UniqueList)
        {
            newQuestions.add(mQuestions_2.get(i));
        }
        if(newQuestions.isEmpty())
        {
            mQuestions = mQuestions_2;
        }
        else
        {
            mQuestions = newQuestions;
        }
        notifyDataSetChanged();
    }

}
