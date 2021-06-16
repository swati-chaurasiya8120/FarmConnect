package com.example.farm_connect.firebase;

import com.example.farm_connect.Question;
import com.example.farm_connect.UniqueIdentification;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class DatabaseHandler {
    private static final FirebaseDatabase database =  FirebaseDatabase.getInstance();
    private static DatabaseReference databaseReference;

    private static String AuthToken;


    public static boolean addQuestion(Question question)
    {
        databaseReference = database.getReference().child(DatabaseContract.Question_Database.ROOT);
        databaseReference = databaseReference.push();
        String key = databaseReference.getKey();
        question.setUniqueID(key);
        databaseReference.setValue(question);


        UniqueIdentification user = new UniqueIdentification(AuthToken, "Random User");
        databaseReference = database.getReference().child(DatabaseContract.User_Auth_Token.ROOT).child(key);
        databaseReference.setValue(user);

        databaseReference = null;
        return true;
    }

    public static boolean addAnswersAndRepliers(String uniqueId, List<String>answers, List<String> repliers)
    {
        databaseReference = database.getReference().child(DatabaseContract.Question_Database.ROOT).child(uniqueId)
                            .child(DatabaseContract.Question_Database.QUESTION_ANSWERS);
        databaseReference.setValue(answers);

        databaseReference = database.getReference().child(DatabaseContract.Question_Database.ROOT).child(uniqueId)
                .child(DatabaseContract.Question_Database.ANSWERED_BY);

        databaseReference.setValue(repliers);

        databaseReference = null;

        return true;
    }

    public static boolean addUserAuthToken(String uniqueId, UniqueIdentification user)
    {
        databaseReference = database.getReference().child(DatabaseContract.User_Auth_Token.ROOT).child(uniqueId);
        databaseReference.setValue(user);

        databaseReference = null;

        return true;
    }

    public static DatabaseReference getDatabaseReferenceOfUser(String questionId)
    {
        databaseReference = database.getReference().child(DatabaseContract.User_Auth_Token.ROOT).child(questionId);
        return databaseReference;
    }

    public static DatabaseReference getDatabaseReferenceOfQuestion(String questionID)
    {
        databaseReference = database.getReference().child(DatabaseContract.Question_Database.ROOT).child(questionID);
        return databaseReference;
    }

    public static DatabaseReference getDatabaseReferenceOfQuestionRoot()
    {
        databaseReference = database.getReference().child(DatabaseContract.Question_Database.ROOT);
        return databaseReference;
    }

    public static void saveAuthToken(String authToken)
    {
        AuthToken = authToken;
    }


    public static DatabaseReference getDatabaseReferenceToRepliers(String questionID)
    {
        databaseReference = database.getReference().child(DatabaseContract.Question_Database.ROOT).child(questionID)
                            .child(DatabaseContract.Question_Database.ANSWERED_BY);
        return databaseReference;
    }

    public static DatabaseReference getDatabaseReferenceToAnswers(String questionID)
    {
        databaseReference = database.getReference().child(DatabaseContract.Question_Database.ROOT).child(questionID)
                .child(DatabaseContract.Question_Database.QUESTION_ANSWERS);
        return databaseReference;
    }


}
