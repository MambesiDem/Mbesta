package com.example.washit;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class editDriverProfile extends AppCompatActivity {
    public static Connection con;
    EditText Name,Surname,EmployeeNumber,ContactNo,driverCar,driverRegNo,driverColor;
    static String name,surname,contactNumber,gender,title,employeeID,vehicleDetails,regNo,carMake,color;
    RadioGroup driverGenderGroup;
    RadioButton driverGenderButton;
    Spinner spinnerDriver;
    TextView driverGenderErrorMsg,driverTitleErrorMsg;

    Button btnSave;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_driver_profile);
        Name = findViewById(R.id.edDriverName);
        Surname = findViewById(R.id.edDriverSurname);
        EmployeeNumber = findViewById(R.id.driverEmpID);
        ContactNo = findViewById(R.id.driverContactNo);
        driverCar = findViewById(R.id.carMakeDriver);
        driverRegNo = findViewById(R.id.DriverCarRegNo);
        driverColor = findViewById(R.id.DriverCarColor);
        driverGenderGroup = findViewById(R.id.DriverRadio);
        spinnerDriver = findViewById(R.id.DriverSpinner);
        driverGenderErrorMsg = findViewById(R.id.genderDrErrorTextDriver);
        driverTitleErrorMsg = findViewById(R.id.titleDriverErrorText);
        btnSave = findViewById(R.id.DriverSaveBtn);

        Intent editDriverIntent = getIntent();
        if(editDriverIntent != null){
            name = editDriverIntent.getStringExtra("Name").trim();
            Name.setText(name);
            surname = editDriverIntent.getStringExtra("Surname").trim();
            Surname.setText(surname);
            employeeID = editDriverIntent.getStringExtra("EmployeeID").trim();
            EmployeeNumber.setText(employeeID);
            contactNumber = editDriverIntent.getStringExtra("ContactNo").trim();
            ContactNo.setText(contactNumber);
            carMake = editDriverIntent.getStringExtra("CarMake").trim();
            driverCar.setText(carMake);
            regNo = editDriverIntent.getStringExtra("RegNo").trim();
            driverRegNo.setText(regNo);
            color = editDriverIntent.getStringExtra("color").trim();
            driverColor.setText(color);
        }

        ArrayAdapter<CharSequence> Adapter = ArrayAdapter.createFromResource(this,
                R.array.titles, android.R.layout.simple_spinner_item);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDriver.setAdapter(Adapter);
        spinnerDriver.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    title = null;
                    Toast.makeText(getApplicationContext(), "Please select Title", Toast.LENGTH_SHORT).show();
                }else {
                    title = adapterView.getItemAtPosition(i).toString();
                    Toast.makeText(getApplicationContext(), title +" is selected",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // The EditText has lost focus, so perform validation.
                    String name = Name.getText().toString().trim();

                    if (name.isEmpty()) {
                        // Input is empty, show an error.
                        Name.setError("name cannot be empty");

                    } else if (!isValidText(name)) {
                        // Input contains invalid characters, show an error.
                        Name.setError("Invalid characters in name");

                    } else {
                        // Input is valid, clear any previous error.
                        Name.setError(null);
                    }
                }
            }
        });
        Surname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // The EditText has lost focus, so perform validation.
                    String surname = Surname.getText().toString().trim();

                    if (surname.isEmpty()) {
                        // Input is empty, show an error.
                        Surname.setError("surname cannot be empty");
                    } else if (!isValidText(surname)) {
                        // Input contains invalid characters, show an error.
                        Surname.setError("Invalid characters in surname");
                    } else {
                        // Input is valid, clear any previous error.
                        Surname.setError(null);
                    }
                }
            }
        });

        ContactNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // The EditText has lost focus, so perform validation.
                    String contactNumber = ContactNo.getText().toString().trim();

                    if (contactNumber.isEmpty()) {
                        // Input is empty, show an error.
                        ContactNo.setError("contact Number cannot be empty");
                    } else {
                        // Input is valid, clear any previous error.
                        ContactNo.setError(null);
                    }
                }
            }
        });
        int maxLength = 10;

        InputFilter[] contactFilters = new InputFilter[1];
        contactFilters[0] = new InputFilter.LengthFilter(maxLength);

        ContactNo.setFilters(contactFilters);

        ContactNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed for this example
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed for this example
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().trim();
                if (input.length() < 10) {
                    ContactNo.setError("Please enter at least 10 digits");
                } else {
                    ContactNo.setError(null);
                }
            }
        });
        driverCar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String street = driverCar.getText().toString();
                    if(street.isEmpty()){
                        driverCar.setError("Car Make/Model cannot be empty");
                    }
                }
            }
        });
        driverRegNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // The EditText has lost focus, so perform validation.
                    String regNum = driverRegNo.getText().toString().trim();

                    if (regNum.isEmpty()) {
                        // Input is empty, show an error.
                        driverRegNo.setError("Suburb cannot be empty");
                    } else {
                        // Input is valid, clear any previous error.
                        driverRegNo.setError(null);
                    }
                }
            }

        });
        driverCar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // The EditText has lost focus, so perform validation.
                    String carColor = driverColor.getText().toString().trim();

                    if (carColor.isEmpty()) {
                        // Input is empty, show an error.
                        driverColor.setError("City cannot be empty");
                    } else {
                        // Input is valid, clear any previous error.
                        driverColor.setError(null);
                    }
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectID = driverGenderGroup.getCheckedRadioButtonId();
                driverGenderButton = findViewById(selectID);
                if(!(selectID == -1)){
                    gender = driverGenderButton.getText().toString();
                    driverGenderErrorMsg.setVisibility(View.GONE);
                }
                name = Name.getText().toString();
                surname = Surname.getText().toString();
                contactNumber = ContactNo.getText().toString();
                carMake = driverCar.getText().toString();
                regNo = driverRegNo.getText().toString();
                color = driverColor.getText().toString();

                vehicleDetails = carMake+","+regNo+","+color;

                if(name.length()==0){
                    Toast.makeText(getApplicationContext(),"Correct all errors",Toast.LENGTH_LONG).show();
                }else if(gender == null){
                    driverGenderErrorMsg.setVisibility(View.VISIBLE);
                }else if (title==null){
                    driverTitleErrorMsg.setVisibility(View.VISIBLE);
                }else {
                    String query = "UPDATE Driver SET EmployeeName = ?, Surname = ?, ContactDetails = ?, vehicleDetails = ?, Gender = ?, Title = ? WHERE EmployeeID = ?";
                    boolean success = executeStatement(query, name, surname, contactNumber, vehicleDetails, gender, title, employeeID);
                    if(success){
                        Toast.makeText(getApplicationContext(),"Profile updated!!",Toast.LENGTH_LONG).show();
                        Intent mainIntent = new Intent(editDriverProfile.this,DriverMainPage.class);
                        startActivity(mainIntent);
                    }else{
                        Toast.makeText(getApplicationContext(),"Update failed...",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }
    public static boolean executeStatement(String sql, String name, String surname, String contactNumber, String vehicleDetails, String gender, String title, String employeeID) {
        PreparedStatement statement = null;

        try {
            con = DatabaseManager.getConnection(); // Get the connection instance
            if (con != null) {
                statement = con.prepareStatement(sql);
                ((PreparedStatement) statement).setString(1, name);
                statement.setString(2, surname);
                statement.setString(3, contactNumber);
                statement.setString(4, vehicleDetails);
                statement.setString(5, gender);
                statement.setString(6, title);
                statement.setString(7, employeeID);
                int rowsAffected = statement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources in reverse order of opening
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
    private boolean isValidText(String text) {
        // Use a regular expression to allow only letters (uppercase and lowercase) and spaces.
        return text.matches("[a-zA-Z\\s]+");
    }
}