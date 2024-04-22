package com.example.washit;

import static com.example.washit.R.*;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
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
import java.sql.Statement;

public class EditProfile extends AppCompatActivity {

            Button btnSave;
            TextView genderError,titleError,lblLastTitle;
            EditText Name,Surname,StudentNo,ContactNo,Street,Suburb,City,PostalCode;
            static String name,surname,studentNumber,contactNumber,street,suburb,city,postalCode,gender,title,employeeID;
            RadioGroup genderGroup;
            RadioButton genderButton;
            Spinner spinner;
    public static Connection con;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_edit_profile);

        Name = findViewById(R.id.edtEDName);
        Surname = findViewById(id.edtEDSurname);
        StudentNo = findViewById(id.edtEDStudentNo);
        ContactNo = findViewById(id.edtEDContactNo);
        Street = findViewById(id.edtEDStreet);
        Suburb = findViewById(id.edtEDSuburb);
        City = findViewById(id.edtEDCity);
        PostalCode = findViewById(id.edtEDPostalCode);
        genderGroup = findViewById(id.rdEDGroupGender);
        genderError = findViewById(id.genderEDErrorText);
        spinner = findViewById(id.EDSpinner);
        btnSave = findViewById(R.id.btnEDSave);

        Intent editIntent = getIntent();
        if(editIntent != null){
            if (name != null) {
                name = editIntent.getStringExtra("Name").trim();

            }
            Name.setText(name);
            surname = editIntent.getStringExtra("Surname").trim();
            Surname.setText(surname);
            studentNumber = editIntent.getStringExtra("StudentNo").trim();
            StudentNo.setText(studentNumber);
            contactNumber = editIntent.getStringExtra("ContactNo").trim();
            ContactNo.setText(contactNumber);
            street = editIntent.getStringExtra("Street").trim();
            Street.setText(street);
            suburb = editIntent.getStringExtra("Suburb").trim();
            Suburb.setText(suburb);
            city = editIntent.getStringExtra("City").trim();
            City.setText(city);
            postalCode = editIntent.getStringExtra("ZipCode").trim();
            PostalCode.setText(postalCode);

        }


        ArrayAdapter<CharSequence> Adapter = ArrayAdapter.createFromResource(this,
                array.titles, android.R.layout.simple_spinner_item);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(Adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        Street.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String street = Street.getText().toString();
                    if(street.isEmpty()){
                        Street.setError("Street cannot be empty");
                    }
                }
            }
        });
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                StringBuilder builder = new StringBuilder();
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (Character.isLetterOrDigit(c) || Character.isSpaceChar(c)) {
                        builder.append(c);
                    } else {
                        Street.setError("Enter numbers, letters, and spaces");
                        return "";
                    }
                }
                return builder.toString();
            }
        };
        Street.setFilters(new InputFilter[]{filter});
        Suburb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // The EditText has lost focus, so perform validation.
                    String street = Suburb.getText().toString().trim();

                    if (street.isEmpty()) {
                        // Input is empty, show an error.
                        Suburb.setError("Suburb cannot be empty");
                    } else if (!isValidText(street)) {
                        // Input contains invalid characters, show an error.
                        Suburb.setError("Enter your Suburb/Town");
                    } else {
                        // Input is valid, clear any previous error.
                        Suburb.setError(null);
                    }
                }
            }

        });
        City.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // The EditText has lost focus, so perform validation.
                    String city = City.getText().toString().trim();

                    if (city.isEmpty()) {
                        // Input is empty, show an error.
                        City.setError("City cannot be empty");
                    } else if (!isValidText(city)) {
                        // Input contains invalid characters, show an error.
                        City.setError("Enter letters only");
                    } else {
                        // Input is valid, clear any previous error.
                        City.setError(null);
                    }
                }
            }
        });
        PostalCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // The EditText has lost focus, so perform validation.
                    String postalCode = PostalCode.getText().toString().trim();

                    if (postalCode.isEmpty()) {
                        // Input is empty, show an error.
                        PostalCode.setError("Postal Code cannot be empty");
                    } else {
                        // Input is valid, clear any previous error.
                        PostalCode.setError(null);
                    }
                }
            }
        });
        int ZipCodeNoLength = 4;

        InputFilter[] ZipCodeFilters = new InputFilter[1];
        ZipCodeFilters[0] = new InputFilter.LengthFilter(ZipCodeNoLength);

        PostalCode.setFilters(ZipCodeFilters);

        PostalCode.addTextChangedListener(new TextWatcher() {
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
                if (input.length() < 4) {
                    PostalCode.setError("Please enter at least 4 digits");
                } else {
                    PostalCode.setError(null);
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectID = genderGroup.getCheckedRadioButtonId();
                genderButton = findViewById(selectID);
                if(!(selectID == -1)){
                    gender = genderButton.getText().toString();
                    genderError.setVisibility(View.GONE);
                }

                name = Name.getText().toString();
                surname = Surname.getText().toString();
                contactNumber = ContactNo.getText().toString();
                street = Street.getText().toString();
                suburb = Suburb.getText().toString();
                city = City.getText().toString();
                postalCode = PostalCode.getText().toString();

                String address = street+","+suburb+","+city+","+postalCode;

                if(studentNumber != null){
                    if(name.length()==0){
                        Toast.makeText(getApplicationContext(),"Correct all errors",Toast.LENGTH_LONG).show();
                    }else if(gender == null){
                        genderError.setVisibility(View.VISIBLE);
                    }else if (title==null){
                        titleError.setVisibility(View.VISIBLE);
                    }else {
                        String query = "UPDATE Student SET StudentName = ?, Surname = ?, sContactNo = ?, sResidentialAddress = ?, Gender = ?, Title = ? WHERE StudentNo = ?";
                        boolean success = executeStatement(query, name, surname, contactNumber, address, gender, title, studentNumber);
                        if(success){
                            Toast.makeText(getApplicationContext(),"Profile updated!!",Toast.LENGTH_LONG).show();
                            Intent mainIntent = new Intent(EditProfile.this,StudentMainPage.class);
                            startActivity(mainIntent);
                        }else{
                            Toast.makeText(getApplicationContext(),"Update failed...",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

    }
    public static boolean executeStatement(String sql, String name, String surname, String contactNumber, String address, String gender, String title, String studentNumber) {
        PreparedStatement statement = null;

        try {
            con = LoginActivity.connection; // Get the connection instance
            if (con != null) {
                statement = con.prepareStatement(sql);
                ((PreparedStatement) statement).setString(1, name);
                statement.setString(2, surname);
                statement.setString(3, contactNumber);
                statement.setString(4, address);
                statement.setString(5, gender);
                statement.setString(6, title);
                statement.setString(7, studentNumber);
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
//            if (con != null) {
//                try {
//                    con.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
        }
        return false;
    }
    private boolean isValidText(String text) {
        // Use a regular expression to allow only letters (uppercase and lowercase) and spaces.
        return text.matches("[a-zA-Z\\s]+");
    }
}