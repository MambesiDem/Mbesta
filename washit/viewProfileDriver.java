package com.example.washit;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class viewProfileDriver extends AppCompatActivity {
    static Connection con = null;
    static String Surname, name, gender, title,employeeID,vehicleDetails,carMake,RegNo,color,contactNo;
    EditText txtFldSurname, txtFldName, txtFldEmployee, txtFldContactNo, txtFldCarMake, txtFldRegNo, txtFldColor;
    TextView labelGender,labelTitle,lblProfileName;
    ScrollView driverScroll;
    Button btnEdit;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile_driver);
        btnEdit = findViewById(R.id.btnDriverEdit);
        lblProfileName = findViewById(R.id.lblVPProfileName);
        driverScroll  = findViewById(R.id.myScroller);
        txtFldColor = findViewById(R.id.VPCarColor);
        txtFldSurname = findViewById(R.id.VPSurname);
        txtFldName = findViewById(R.id.VPFullName);
        labelGender = findViewById(R.id.txtDriverGender);
        labelTitle = findViewById(R.id.txtDriverTitle);
        txtFldEmployee = findViewById(R.id.VPEmpNo);
        txtFldContactNo = findViewById(R.id.VPContactNo);
        txtFldCarMake = findViewById(R.id.VPCarMakeModel);
        txtFldRegNo = findViewById(R.id.VPCarRegNo);

        Intent studEmpNoIntent = getIntent();
        if (studEmpNoIntent != null) {
            Bundle extras = studEmpNoIntent.getExtras();
            int empId = extras.getInt("EmployeeID");
            String studentNo = Integer.toString(empId);
            try {
                getFromDriverTable(studentNo);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            txtFldSurname.setText(Surname);
            txtFldName.setText(name);
            labelGender.setText(gender);
            labelTitle.setText(title);
            lblProfileName.setText(name);
            txtFldEmployee.setText(employeeID);
            txtFldContactNo.setText(contactNo);

            String[] car = vehicleDetails.split(",");
            if(car.length>1){
                carMake = car[0]+","+car[1];
                txtFldCarMake.setText(carMake);
                RegNo = car[2];
                txtFldRegNo.setText(RegNo);
                color = car[3];
                txtFldColor.setText(color);
            }
            else{
                carMake = "";
                txtFldCarMake.setText(carMake);
                RegNo = "";
                txtFldRegNo.setText(RegNo);
                color = "";
                txtFldColor.setText(color);
            }

        }
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewProfileDriver.this,editDriverProfile.class);
                intent.putExtra("Name",name);
                intent.putExtra("Surname",Surname);
                intent.putExtra("EmployeeID",employeeID);
                intent.putExtra("ContactNo",contactNo);
                intent.putExtra("CarMake",carMake);
                intent.putExtra("RegNo",RegNo);
                intent.putExtra("color",color);
                startActivity(intent);
            }
        });
    }
    public void onVPBackClicked (View view){
        Intent intent = new Intent(viewProfileDriver.this, DriverMainPage.class);
        startActivity(intent);
    }
    public static void getFromDriverTable(String epmpId) throws SQLException {

        con = DatabaseManager.getConnection();
        Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
        if (con != null) {
            epmpId = epmpId+"   ";
            try {
                String query = "SELECT * FROM Driver WHERE EmployeeID = "+epmpId+"";
                ResultSet resultSet = statement.executeQuery(query);

                if (resultSet.next()) {
                    name = resultSet.getString("EmployeeName").trim();
                    Surname = resultSet.getString("Surname").trim();
                    gender = resultSet.getString("Gender").trim();
                    title = resultSet.getString("Title").trim();
                    employeeID = resultSet.getString("EmployeeID").trim();
                    contactNo = resultSet.getString("ContactDetails").trim();
                    vehicleDetails = resultSet.getString("vehicleDetails").trim();
                }

                resultSet.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}