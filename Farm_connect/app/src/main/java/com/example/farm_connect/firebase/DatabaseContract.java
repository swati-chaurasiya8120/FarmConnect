package com.example.farm_connect.firebase;

public class DatabaseContract {


    public static class Question_Database
    {
        public static final String ROOT                = "Questions";

        public static final String QUESTION_UNIQUE_ID  = "unique_id";

        public static final String QUESTION_TITLE      = "title";

        public static final String QUESTION_ANSWERS    = "answers";

        public static final String ANSWERED_BY         = "answersGivenBy";

        public static final String TAGS                = "tags";
    }


    public static class User_Auth_Token
    {
        public static final String ROOT                = "user_auth_token";

        public static final String QUESTION_ID         = "question_id";

        public static final String ASKED_BY            = "username";

        public static final String REGISTRATION_TOKEN  = "auth_token" ;
    }

}
