package com.example.washit;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ViewProfile extends AppCompatActivity {
    static Connection con = null;
    static String Surname, name, gender, title, studentNumber = null, contactNo, street, suburb, city, postalCode, address;
    EditText edtSurname, edtName, edtStudentNumber, edtContactNo, edtStreet, edtSuburb, edtCity, edtPostalCode;
    TextView lblGender, lblTitle, lblProfileName;
    Button btnEdit;

    ScrollView studentScroll;

    int stNo;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);


        studentScroll = findViewById(R.id.VPscrollStudent);
        btnEdit = findViewById(R.id.btnDriverEdit);
        edtSurname = findViewById(R.id.edtVPSurname);
        edtName = findViewById(R.id.edtVPName);
        edtStudentNumber = findViewById(R.id.edtVPStudentNo);
        edtContactNo = findViewById(R.id.edtVPContactNo);
        edtStreet = findViewById(R.id.edtVPStreet);
        edtSuburb = findViewById(R.id.edtVPSuburb);
        edtCity = findViewById(R.id.edtVPCity);
        edtPostalCode = findViewById(R.id.edtVPPostalCode);
        lblGender = findViewById(R.id.lblGender);
        lblTitle = findViewById(R.id.lblTitle);
        lblProfileName = findViewById(R.id.lblVPProfileName);
        btnEdit = findViewById(R.id.btnVPEdit);
        Intent studEmpNoIntent = getIntent();

        if (studEmpNoIntent != null) {
            Bundle extras = studEmpNoIntent.getExtras();
            stNo = extras.getInt("StudentNumber");
            String studentNo = Integer.toString(stNo);
            if (studentNo.length() == 9) {
                int StudNo = Integer.parseInt(studentNo);
                try {
                    getFromStudentTable(StudNo);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                edtSurname.setText(Surname);
                edtName.setText(name);
                lblGender.setText(gender);
                lblTitle.setText(title);
                lblProfileName.setText(name);
                edtStudentNumber.setText(studentNumber);
                edtContactNo.setText(contactNo);

                String[] myAddress = address.split(",");
                street = myAddress[0];
                suburb = myAddress[1];
                city = myAddress[2];
                postalCode = myAddress[3];
                edtStreet.setText(street);
                edtSuburb.setText(suburb);
                edtCity.setText(city);
                edtPostalCode.setText(postalCode);
            }
//            }else{
//                try {
//                    getFromDriverTable(studentNo);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//
//                txtFldSurname.setText(Surname);
//                txtFldName.setText(name);
//                labelGender.setText(gender);
//                labelTitle.setText(title);
//                lblProfileName.setText(name);
//                txtFldEmployee.setText(employeeID);
//                txtFldContactNo.setText(contactNo);
//
//                String[] car = vehicleDetails.split(",");
//                carMake = car[0]+","+car[1];
//                txtFldCarMake.setText(carMake);
//                RegNo = car[2];
//                txtFldRegNo.setText(RegNo);
//                color = car[3];
//                txtFldColor.setText(color);
//
//                 driverScroll.setVisibility(View.VISIBLE);
//            }


        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewProfile.this,EditProfile.class);
                intent.putExtra("Name",name);
                intent.putExtra("Surname",Surname);
                intent.putExtra("StudentNo",studentNumber);
                intent.putExtra("ContactNo",contactNo);
                intent.putExtra("Street",street);
                intent.putExtra("Suburb",suburb);
                intent.putExtra("City",city);
                intent.putExtra("ZipCode",postalCode);
                startActivity(intent);
            }
        });


    }
    public void onVPBackClicked (View view){
        Intent intent = new Intent(this,StudentMainPage.class);
        intent.putExtra("StudentNumber",stNo);
        startActivity(intent);
    }
    public static void getFromStudentTable(int studentId) throws SQLException {


        con = DatabaseManager.getConnection();
        if (con != null) {
            try {
                String query = "SELECT * FROM Student WHERE StudentNo = ?";
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, studentId);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    name = resultSet.getString("StudentName").trim();
                    Surname = resultSet.getString("Surname").trim();
                    gender = resultSet.getString("Gender").trim();
                    title = resultSet.getString("Title").trim();
                    studentNumber = resultSet.getString("StudentNo").trim();
                    contactNo = resultSet.getString("sContactNo").trim();
                    address = resultSet.getString("sResidentialAddress").trim();

                }

                resultSet.close();
                preparedStatement.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
//    public static void getFromDriverTable(String epmpId) throws SQLException {
//
//        con = ConnectionClass.ConnectToDB();
//        Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
//        if (con != null) {
//            epmpId = epmpId+"   ";
//            try {
//                String query = "SELECT * FROM Driver WHERE EmployeeID = "+epmpId+"";
//                ResultSet resultSet = statement.executeQuery(query);
//
//                if (resultSet.next()) {
//                    name = resultSet.getString("EmployeeName").trim();
//                    Surname = resultSet.getString("Surname").trim();
//                    gender = resultSet.getString("Gender").trim();
//                    title = resultSet.getString("Title").trim();
//                    employeeID = resultSet.getString("EmployeeID").trim();
//                    contactNo = resultSet.getString("ContactDetails").trim();
//                    vehicleDetails = resultSet.getString("vehicleDetails").trim();
//                }
//
//                resultSet.close();
//                con.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
}