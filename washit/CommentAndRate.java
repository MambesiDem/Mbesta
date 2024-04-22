package com.example.washit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CommentAndRate extends AppCompatActivity implements Subscriber{

    Statement statement;
    Connection connection;

    int studentNo;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_and_rate);

        Broker.subscribe(this,"Comment and rate");
        try {
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onSubmitBtnClicked(View view) throws SQLException {
        RadioGroup radioGroup = findViewById(R.id.ratingRadioGroup);

        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        TextInputEditText comment = findViewById(R.id.edtCommentRate);
        if (selectedRadioButtonId == R.id.badRating) {
            createStudentFeedbackRecord(comment.getText().toString(),1);
            createDialog();
        } else if (selectedRadioButtonId == R.id.moderateRating) {
            createStudentFeedbackRecord(comment.getText().toString(),2);
            createDialog();
        }
        else if(selectedRadioButtonId == R.id.goodRating){
            createStudentFeedbackRecord(comment.getText().toString(),3);
            createDialog();
        }
        else if(selectedRadioButtonId==R.id.greatRating){
            createStudentFeedbackRecord(comment.getText().toString(),4);
            createDialog();
        }
        else if(selectedRadioButtonId == R.id.perfectRating);{
            createStudentFeedbackRecord(comment.getText().toString(),5);
            createDialog();
        }
    }
    public void createDialog(){
        AlertDialog alertDialog1 = new AlertDialog.Builder(this).create();
        alertDialog1.setTitle("");
        alertDialog1.setMessage("Thank you for your comment and rating.");
        alertDialog1.setButton(AlertDialog.BUTTON_POSITIVE, "Done",
                (dialogInterface1, i2) -> {
                    dialogInterface1.dismiss();
                    Map.Entry<String,Object> stNumberNeeded = new AbstractMap.SimpleEntry<>("stNumberNeeded",1);
                    Broker.publish("Student Main Page",stNumberNeeded);
                    Intent intent = new Intent(this,StudentMainPage.class);
                    intent.putExtra("StudentNumber",studentNo);
                    startActivity(intent);
                });
        alertDialog1.show();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createStudentFeedbackRecord(String comment,int rating) throws SQLException {
        LocalDate currentDate = LocalDate.now();
        String query = "INSERT INTO StudentFeedback(date,commentText,Rating)"+"\n"
                +"VALUES('"+currentDate+"','"+comment+"','"+rating+"');";
        statement.execute(query);
    }

    @Override
    public void readPublished(Map.Entry<String, Object>... published) {
        for(Map.Entry<String, Object> msg: published){
            if(msg.getKey().equals("studentNumber")){
                studentNo = (int) msg.getValue();
            }
            else if(msg.getKey().equals("orderPrice")){
            }
        }
    }
}