package com.example.washit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class CommentSection {
    EditText edtCommentRate;
    RadioGroup ratingRadioGroup;
    HashMap<String, Integer> ratingIdMap;

    Context context;
    public CommentSection(Context context, LinearLayout linearLayout){

        this.context=context;
        ScrollView.LayoutParams scrollViewParams = new ScrollView.LayoutParams(
                ScrollView.LayoutParams.MATCH_PARENT,
                ScrollView.LayoutParams.MATCH_PARENT
        );

        // Apply layout parameters to the linearLayout
        linearLayout.setLayoutParams(scrollViewParams);
//        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT
//        ));
//        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));

        // Create a TextView for "Comment and Rate"
        TextView txtCommRate = new TextView(context);
        txtCommRate.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        txtCommRate.setText("Comment and Rate");
        txtCommRate.setTextColor(Color.parseColor("#E1F5FE"));
        txtCommRate.setTextSize(24);
        txtCommRate.setTypeface(null, Typeface.BOLD_ITALIC);
        txtCommRate.setGravity(Gravity.CENTER);
        linearLayout.addView(txtCommRate);

        // Create a TextInputLayout with EditText for comment
        LinearLayout textLayout = new LinearLayout(context);
        textLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        textLayout.setOrientation(LinearLayout.HORIZONTAL);

        edtCommentRate = new EditText(context);
        edtCommentRate.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1
        ));
        edtCommentRate.setHint("Write a comment");
        edtCommentRate.setHeight(500);
        textLayout.addView(edtCommentRate);

        linearLayout.addView(textLayout);

        // Create a RadioGroup for rating options
        ratingRadioGroup = new RadioGroup(context);
        ratingRadioGroup.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        ratingRadioGroup.setOrientation(LinearLayout.HORIZONTAL);

        ratingIdMap = new HashMap<>();

        String[] ratings = {"Bad", "Moderate", "Good", "Great", "Perfect"};
        for (String rating : ratings) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            radioButton.setText(rating);
            int radioButtonId = View.generateViewId();
            radioButton.setId(radioButtonId);
            ratingIdMap.put(rating, radioButtonId);

            ratingRadioGroup.addView(radioButton);
        }

        linearLayout.addView(ratingRadioGroup);

        // Create a Button for submission
        Button btnSubmit = new Button(context);
        btnSubmit.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        btnSubmit.setText("Submit");
        btnSubmitClicked(btnSubmit);
        linearLayout.addView(btnSubmit);

    }

    private void btnSubmitClicked(Button btnSubmit) {
        btnSubmit.setOnClickListener(view->{
            int selectedRadioButtonId = ratingRadioGroup.getCheckedRadioButtonId();
            if (selectedRadioButtonId != -1) {

                String selectedRating = getRatingString(selectedRadioButtonId);
                //assert selectedRating != null;
                try {

                    if(selectedRating.equals("Bad")){
                        createStudentFeedbackRecord(edtCommentRate.getText().toString(),1);
                        createDialog();
                    }else if (selectedRating.equals("Moderate")) {
                        createStudentFeedbackRecord(edtCommentRate.getText().toString(),2);
                        createDialog();
                    }
                    else if(selectedRating.equals("Good")){
                        createStudentFeedbackRecord(edtCommentRate.getText().toString(),3);
                        createDialog();
                    }
                    else if(selectedRating.equals("Great")){
                        createStudentFeedbackRecord(edtCommentRate.getText().toString(),4);
                        createDialog();
                    }
                    else if(selectedRating.equals("Perfect"));{
                        createStudentFeedbackRecord(edtCommentRate.getText().toString(),5);
                        createDialog();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createStudentFeedbackRecord(String comment,int rating) throws SQLException {
        LocalDate currentDate = LocalDate.now();
        String query = "INSERT INTO StudentFeedback(date,commentText,Rating)"+"\n"
                +"VALUES('"+currentDate+"','"+comment+"','"+rating+"');";
        LoginActivity.connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE).execute(query);
    }
    private String getRatingString(int radioButtonId) {
        // Iterate through the ratingIdMap to find the matching rating string
        for (Map.Entry<String, Integer> entry : ratingIdMap.entrySet()) {
            if (entry.getValue() == radioButtonId) {
                return entry.getKey(); // Return the rating string
            }
        }
        return null; // Return null if no matching rating is found
    }
    public void createDialog(){
        AlertDialog alertDialog1 = new AlertDialog.Builder(context).create();
        alertDialog1.setTitle("");
        alertDialog1.setMessage("Thank you for your comment and rating.");
        alertDialog1.setButton(AlertDialog.BUTTON_POSITIVE, "Done",
                (dialogInterface1, i2) -> {
                    dialogInterface1.dismiss();
                });
        alertDialog1.show();
    }
}
