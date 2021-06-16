package com.example.farm_connect;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Question{
    private String question;
    private List<String> answers;
    private List<String> tags;
    private List<String> answersGivenBy;
    private String title;
    private String unique_id;

    public Question()
    {

    }

    public Question(String Title, String ques, List<String> ans, List<String> Tags)
    {
        title = Title;
        question = ques;
        answers = ans;
        tags = Tags;
        unique_id = "";
        answersGivenBy = null;
    }

    public Question(String question, List<String> answers, List<String> tags, List<String> answersGivenBy, String title) {
        this.question = question;
        this.answers = answers;
        this.tags = tags;
        this.answersGivenBy = answersGivenBy;
        this.title = title;
    }

    public String getUniqueID() {
        return unique_id;
    }

    public void setUniqueID(String unique_id) {
        this.unique_id = unique_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAnswersGivenBy() {
        return answersGivenBy;
    }

    public void setAnswersGivenBy(List<String> answersGivenBy) {
        this.answersGivenBy = answersGivenBy;
    }
}
