package com.example.washit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.google.android.material.textfield.TextInputEditText;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {
    public static Connection connection = null;
    Statement statement = null;
    static Connection con = null;
    TextView newProfile;
    Button btnLogin;
    EditText edtStudentNoEmpID,edtPassword;
    TextInputEditText txtInpPassword;
    ImageView imgShowHide;
    Boolean isPasswordVisible = false;
    static String studentNumber,password;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            connection = DatabaseManager.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        imgShowHide = findViewById(R.id.imgVisibilityTog);
        txtInpPassword = findViewById(R.id.edtTxtPassword);
        newProfile = findViewById(R.id.TextViewCreateProfile);
        btnLogin = findViewById(R.id.btn_Login);
        edtStudentNoEmpID = findViewById(R.id.editTextUsername);
//        edtPassword = findViewById(R.id.editTextPassword);

        //region Attach listener on ShowHide icon and EditTextUsername
        imgShowHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the password visibility flag
                isPasswordVisible = !isPasswordVisible;

                // Change the drawable resource based on password visibility
                int drawableRes = isPasswordVisible ?
                        R.drawable.ic_baseline_visibility_24 :
                        R.drawable.ic_baseline_visibility_off_24;

                // Set the new drawable for the ImageView
                imgShowHide.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),drawableRes));

                // Toggle the input type of the EditText to show/hide password
                int inputType = isPasswordVisible ?
                        InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD :
                        InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;

                txtInpPassword.setInputType(inputType);

                // Move the cursor to the end of the text
                if (txtInpPassword.getText() != null) {
                    txtInpPassword.setSelection(txtInpPassword.getText().length());
                }
            }
        });
        //endregion


        String text = "New user? Create Profile";
        SpannableString spannableString = new SpannableString(text);
        UnderlineSpan underlineSpan = new UnderlineSpan();

        spannableString.setSpan(underlineSpan,0,24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        newProfile.setText(spannableString);

        newProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,CreateProfile.class);
                startActivity(intent);
            }
        });
    }
    public void onBtnLoginClicked(View view) throws SQLException {
        if(connection==null){
            Toast.makeText(this, "Not connected to the database.", Toast.LENGTH_SHORT).show();
            return;
        }
        String studEmpNo = edtStudentNoEmpID.getText().toString();
        String pass = txtInpPassword.getText().toString();

        int loginID = Integer.parseInt(studEmpNo);
        try {
            if (isValidStudentCredentials(loginID, pass)) {
                Intent intent = new Intent(this,StudentMainPage.class);
                intent.putExtra("StudentNumber",loginID);
                startActivity(intent);
            }
            else if(isValidDriverCredentials(studEmpNo,pass)){
                Intent intent = new Intent(this,DriverMainPage.class);
                intent.putExtra("EmployeeID",Integer.parseInt(studEmpNo));
                startActivity(intent);
            }
            else{
                Toast.makeText(this, "Incorrect password and/or studentNo/EmpID", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //connection.close();
    }
    public boolean isValidStudentCredentials(int studentId, String passcode) throws SQLException {

        if (connection != null) {
            try {
                String query = "SELECT * FROM Student;";
                ResultSet resultSet = statement.executeQuery(query);

                while(resultSet.next()) {
                    int studentNo = resultSet.getInt("StudentNo");
                    String code = resultSet.getString("Password");
                    String s ="";
                    for(int i=0;i<code.length();i++){
                        if(code.charAt(i)!=' '){
                            s +=code.charAt(i);
                        }
                    }
                    if(studentNo == studentId && s.equals(passcode)) return true;
                }
                resultSet.close();
                //connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean isValidDriverCredentials(String empId, String passcode) throws SQLException {

        if (connection != null) {
            try {
//                String query = "SELECT * FROM Student WHERE StudentNo = "+studentId+"";
//                PreparedStatement preparedStatement = con.prepareStatement(query);
//
//                ResultSet resultSet = preparedStatement.executeQuery();
                String query = "SELECT * FROM Driver;";
                ResultSet resultSet = statement.executeQuery(query);

                while(resultSet.next()) {
                    String id = resultSet.getString("EmployeeID");
                    String code = resultSet.getString("Password");
                    String s ="";
                    for(int i=0;i<code.length();i++){
                        if(code.charAt(i)!=' '){
                            s +=code.charAt(i);
                        }
                    }
                    String x ="";
                    for(int i=0;i<id.length();i++){
                        if(id.charAt(i)!=' '){
                            x +=id.charAt(i);
                        }
                    }
                    if(x.equals(empId) && s.equals(passcode)) return true;
                }

                resultSet.close();
                //preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}