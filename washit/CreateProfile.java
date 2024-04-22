package com.example.washit;

import static com.example.washit.R.*;
import static com.example.washit.R.id.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateProfile extends AppCompatActivity {
    public static Connection con;
    Button btnStudent,btnDriver;
    ScrollView scrollViewStudent,scrollViewDriver;
    TextView txtPDTitle,txtAddressTitle,txtViewStudentProfile,lblOptions;
    ImageView imageViewStudProfile;
    Button btnBack,btnDone;
    LinearLayout BackDone,StudDriverLayout;
    TextView lblDriverProfile;

    //region Student Profile
    EditText edStFName,edStSurname,edStNumber,edtStContactNumber,edStStreet,edStSuburb,edStCity,edStPostalCode;
    RadioGroup rdGroupGender; RadioButton rdButton;
    String genderText = "";
    static String stTitle = null;
    static int Pos;
    TextView titleErrorText,genderErrorText,driverTitleError,driverGenderError;
    Spinner StSpinner,drSpinner;
    //endregion

    //regionDriver Profile

        TextView lblDDPersonal,lblVehicleDetails;
        static String drTitle,drGender,passWord;
        RadioGroup rdDrGender; RadioButton rdDrButton;
        EditText driverName,driverSurname,driverEmpNumber,driverContactNo,driverMake,driverCarReg,driverCarColor;
    //endregion
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        //region Find Views by id
        lblDDPersonal = findViewById(txtViewDDTitle);
        lblVehicleDetails = findViewById(txtViewVehicleTitle);
        scrollViewDriver = findViewById(id.scrollViewDriver);

        scrollViewStudent = findViewById(id.scrollViewStudent);
        btnStudent = findViewById(btn_StudentOpt);
        btnDriver = findViewById(btn_DriverOpt);
        StudDriverLayout = findViewById(StudentDriverLayout);

        BackDone = findViewById(LinearBackDoneLayout);
        txtPDTitle = findViewById(txtViewPDTitle);
        txtAddressTitle = findViewById(txtViewAddressTitle);
        txtViewStudentProfile = findViewById(id.txtViewStudentProfile);
        lblOptions = findViewById(id.lblOptions);
        imageViewStudProfile = findViewById(ImageViewProfile);
        StSpinner = findViewById(spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                array.titles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        StSpinner.setAdapter(adapter);
        StSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    Toast.makeText(getApplicationContext(), "Please select Title", Toast.LENGTH_SHORT).show();
                    stTitle = null;

                }else {
                    stTitle = adapterView.getItemAtPosition(i).toString();
                    Toast.makeText(getApplicationContext(), stTitle +" is selected",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        drSpinner = findViewById(DriverSpinner);
        ArrayAdapter<CharSequence> driverAdapter = ArrayAdapter.createFromResource(this,
                array.titles, android.R.layout.simple_spinner_item);
        driverAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drSpinner.setAdapter(driverAdapter);
        drSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    drTitle = null;
                    Toast.makeText(getApplicationContext(), "Please select Title", Toast.LENGTH_SHORT).show();
                }else {
                    drTitle = adapterView.getItemAtPosition(i).toString();
                    Toast.makeText(getApplicationContext(), drTitle +" is selected",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //endregion
        //region Attach listeners to Edit Texts for Error trapping and validation
        edStFName = findViewById(editTextFullName);
        edStSurname = findViewById(editTextSurname);
        edStNumber = findViewById(editTextStudentNo);
        edtStContactNumber = findViewById(editTextContactNo);
        edStStreet = findViewById(editTexStreet);
        edStSuburb = findViewById(editTextSurburb);
        edStCity = findViewById(editTextCity);
        edStPostalCode = findViewById(editTextPostalCode);

        edStFName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // The EditText has lost focus, so perform validation.
                    String name = edStFName.getText().toString().trim();

                    if (name.isEmpty()) {
                        // Input is empty, show an error.
                        edStFName.setError("name cannot be empty");

                    } else if (!isValidText(name)) {
                        // Input contains invalid characters, show an error.
                        edStFName.setError("Invalid characters in name");

                    } else {
                        // Input is valid, clear any previous error.
                        edStFName.setError(null);
                    }
                }
            }
        });
        edStSurname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // The EditText has lost focus, so perform validation.
                    String surname = edStSurname.getText().toString().trim();

                    if (surname.isEmpty()) {
                        // Input is empty, show an error.
                        edStSurname.setError("surname cannot be empty");
                    } else if (!isValidText(surname)) {
                        // Input contains invalid characters, show an error.
                        edStSurname.setError("Invalid characters in surname");
                    } else {
                        // Input is valid, clear any previous error.
                        edStSurname.setError(null);
                    }
                }
            }
        });
        edStNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // The EditText has lost focus, so perform validation.
                    String studentNumber = edStNumber.getText().toString().trim();

                    if (studentNumber.isEmpty()) {
                        // Input is empty, show an error.
                        edStNumber.setError("student Number cannot be empty");
                    } else {
                        // Input is valid, clear any previous error.
                        edStNumber.setError(null);
                    }
                }
            }
        });
        int studentNoLength = 9;

        InputFilter[] studentFilters = new InputFilter[1];
        studentFilters[0] = new InputFilter.LengthFilter(studentNoLength);

        edStNumber.setFilters(studentFilters);

        edStNumber.addTextChangedListener(new TextWatcher() {
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
                if (input.length() < 9) {
                    edStNumber.setError("Please enter at least 9 digits");
                } else {
                    edStNumber.setError(null);
                }
            }
        });

        edtStContactNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // The EditText has lost focus, so perform validation.
                    String contactNumber = edtStContactNumber.getText().toString().trim();

                    if (contactNumber.isEmpty()) {
                        // Input is empty, show an error.
                        edtStContactNumber.setError("contact Number cannot be empty");
                    } else {
                        // Input is valid, clear any previous error.
                        edtStContactNumber.setError(null);
                    }
                }
            }
        });
        int maxLength = 10;

        InputFilter[] contactFilters = new InputFilter[1];
        contactFilters[0] = new InputFilter.LengthFilter(maxLength);

        edtStContactNumber.setFilters(contactFilters);

        edtStContactNumber.addTextChangedListener(new TextWatcher() {
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
                    edtStContactNumber.setError("Please enter at least 10 digits");
                } else {
                    edtStContactNumber.setError(null);
                }
            }
        });
        edStStreet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String street = edStStreet.getText().toString();
                    if(street.isEmpty()){
                        edStStreet.setError("Street cannot be empty");
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
                        edStStreet.setError("Enter numbers, letters, and spaces");
                        return "";
                    }
                }
                return builder.toString();
            }
        };
        edStStreet.setFilters(new InputFilter[]{filter});

        edStSuburb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // The EditText has lost focus, so perform validation.
                    String street = edStSuburb.getText().toString().trim();

                    if (street.isEmpty()) {
                        // Input is empty, show an error.
                        edStSuburb.setError("Suburb cannot be empty");
                    } else if (!isValidText(street)) {
                        // Input contains invalid characters, show an error.
                        edStSuburb.setError("Enter your Suburb/Town");
                    } else {
                        // Input is valid, clear any previous error.
                        edStSuburb.setError(null);
                    }
                }
            }

        });
        edStCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // The EditText has lost focus, so perform validation.
                    String city = edStCity.getText().toString().trim();

                    if (city.isEmpty()) {
                        // Input is empty, show an error.
                        edStCity.setError("City cannot be empty");
                    } else if (!isValidText(city)) {
                        // Input contains invalid characters, show an error.
                        edStCity.setError("Enter letters only");
                    } else {
                        // Input is valid, clear any previous error.
                        edStCity.setError(null);
                    }
                }
            }
        });
        edStPostalCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // The EditText has lost focus, so perform validation.
                    String postalCode = edStPostalCode.getText().toString().trim();

                    if (postalCode.isEmpty()) {
                        // Input is empty, show an error.
                        edStPostalCode.setError("Postal Code cannot be empty");
                    } else {
                        // Input is valid, clear any previous error.
                        edStPostalCode.setError(null);
                    }
                }
            }
        });
        int ZipCodeNoLength = 4;

        InputFilter[] ZipCodeFilters = new InputFilter[1];
        ZipCodeFilters[0] = new InputFilter.LengthFilter(ZipCodeNoLength);

        edStPostalCode.setFilters(ZipCodeFilters);

        edStPostalCode.addTextChangedListener(new TextWatcher() {
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
                    edStPostalCode.setError("Please enter at least 4 digits");
                } else {
                    edStPostalCode.setError(null);
                }
            }
        });

        driverName = findViewById(edtTxtD_FullName);
        driverSurname = findViewById(edtTxtD_Surname);
        driverEmpNumber = findViewById(edtTxtD_EmpID);
        driverContactNo = findViewById(edtTxtD_ContactNo);
        driverMake = findViewById(edtTxtCarMakeModel);
        driverCarReg = findViewById(edtTxtCarRegNo);
        driverCarColor = findViewById(edtTxtCarColor);

        driverName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // The EditText has lost focus, so perform validation.
                    String name = driverName.getText().toString().trim();

                    if (name.isEmpty()) {
                        // Input is empty, show an error.
                        driverName.setError("name cannot be empty");
                    } else if (!isValidText(name)) {
                        // Input contains invalid characters, show an error.
                        driverName.setError("Enter letters only");

                    } else {
                        // Input is valid, clear any previous error.
                        driverName.setError(null);
                    }
                }
            }
        });
        driverSurname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // The EditText has lost focus, so perform validation.
                    String surname = driverSurname.getText().toString().trim();

                    if (surname.isEmpty()) {
                        // Input is empty, show an error.
                        driverSurname.setError("surname cannot be empty");
                    } else if (!isValidText(surname)) {
                        // Input contains invalid characters, show an error.
                        driverSurname.setError("Enter letters only");
                    } else {
                        // Input is valid, clear any previous error.
                        driverSurname.setError(null);
                    }
                }
            }
        });
        driverEmpNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // The EditText has lost focus, so perform validation.
                    String employeeNumber = driverEmpNumber.getText().toString().trim();

                    if (employeeNumber.isEmpty()) {
                        // Input is empty, show an error.
                        driverEmpNumber.setError("Employee Number cannot be empty");
                    } else {
                        // Input is valid, clear any previous error.
                        driverEmpNumber.setError(null);
                    }
                }
            }
        });
        int empLength = 7;

        InputFilter[] empFilters = new InputFilter[1];
        empFilters[0] = new InputFilter.LengthFilter(empLength);

        driverEmpNumber.setFilters(empFilters);

        driverEmpNumber.addTextChangedListener(new TextWatcher() {
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
                if (input.length() < 7) {
                    driverEmpNumber.setError("Please enter at least 7 digits");
                } else {
                    driverEmpNumber.setError(null);
                }
            }
        });
        driverContactNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // The EditText has lost focus, so perform validation.
                    String driverContact = driverContactNo.getText().toString().trim();

                    if (driverContact.isEmpty()) {
                        // Input is empty, show an error.
                        driverContactNo.setError("Contact Number cannot be empty");
                    } else {
                        // Input is valid, clear any previous error.
                        driverContactNo.setError(null);
                    }
                }
            }
        });
        int drContactsLength = 10;

        InputFilter[] DrContactFilters = new InputFilter[1];
        DrContactFilters[0] = new InputFilter.LengthFilter(drContactsLength);

        driverContactNo.setFilters(DrContactFilters);

        driverContactNo.addTextChangedListener(new TextWatcher() {
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
                    driverContactNo.setError("Please enter at least 10 digits");
                } else {
                    driverContactNo.setError(null);
                }
            }
        });
        driverMake.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // Validate the input when the EditText loses focus
                    String input = driverMake.getText().toString().trim();

                    // Define a regular expression pattern to match two strings separated by a comma
                    String pattern = "^[a-zA-Z0-9]+,\\s*[a-zA-Z0-9]+$";

                    if (!input.matches(pattern)) {
                        // Input does not match the pattern, set an error message
                        driverMake.setError("Enter Make and Model separated by a comma");
                    } else {
                        // Input is valid, clear any previous error message
                        driverMake.setError(null);
                    }
                }
            }
        });
        driverCarReg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    // Validate the input when the EditText loses focus
                    String inputReg = driverCarReg.getText().toString().trim();

                    // Define a regular expression pattern to match letters (strings) and numbers
                    String pattern = "^[a-zA-Z0-9\\s]+$";

                    if (!inputReg.matches(pattern)) {
                        // Input does not match the pattern, set an error message
                        driverCarReg.setError("Enter only letters and numbers");
                    } else {
                        // Input is valid, clear any previous error message
                        driverCarReg.setError(null);
                    }
                }
            }
        });
        driverCarColor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // The EditText has lost focus, so perform validation.
                    String color = driverCarColor.getText().toString().trim();
                    //Focus only on Text field

                    if (color.isEmpty()) {
                        // Input is empty, show an error.
                        driverCarColor.setError("color cannot be empty");
                    } else if (!isValidText(color)) {
                        // Input contains invalid characters, show an error.
                        driverCarColor.setError("Enter alphabets only");
                    } else {
                        // Input is valid, clear any previous error.
                        driverCarColor.setError(null);
                    }
                }
            }
        });




        //endregion
        

        btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollViewStudent.setVisibility(view.VISIBLE);
                imageViewStudProfile.setVisibility(view.VISIBLE);
                txtViewStudentProfile.setVisibility(view.VISIBLE);
                BackDone.setVisibility(view.VISIBLE);


                btnStudent.setVisibility(view.GONE);
                btnDriver.setVisibility(view.GONE);
                lblOptions.setVisibility(view.GONE);


                StudDriverLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
            }
        });


        String text = "Personal Details";
        SpannableString spannableString = new SpannableString(text);
        UnderlineSpan underlineSpan = new UnderlineSpan();

        spannableString.setSpan(underlineSpan,0,16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtPDTitle.setText(spannableString);
        lblDDPersonal.setText(spannableString);


        String address = "Address";
        SpannableString spannableAddress = new SpannableString(address);
        UnderlineSpan underlineAddress = new UnderlineSpan();

        spannableAddress.setSpan(underlineAddress,0,7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtAddressTitle.setText(spannableAddress);

        String vehicle = getString(string.VehicleDetails);
        SpannableString spannableVehicle = new SpannableString(vehicle);
        UnderlineSpan underlineVehicle = new UnderlineSpan();

        spannableVehicle.setSpan(underlineVehicle,0,15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);



        lblVehicleDetails.setText(spannableVehicle);
        //region Driver Button Clicked
        btnDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollViewDriver.setVisibility(view.VISIBLE);
                imageViewStudProfile.setVisibility(view.VISIBLE);
                txtViewStudentProfile.setVisibility(view.VISIBLE);
                BackDone.setVisibility(view.VISIBLE);


                btnStudent.setVisibility(view.GONE);
                btnDriver.setVisibility(view.GONE);
                lblOptions.setVisibility(view.GONE);


                StudDriverLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
            }
        });
        //endregion

    }
    public void onBackClicked(View view){
        Intent intent = new Intent(CreateProfile.this,LoginActivity.class);
        startActivity(intent);
    }
    //Done button is clicked

    public void onDoneClicked(View view) throws SQLException {

        //region Initialise everything
            String drName,drSurname,drEmpNo,drContactNo,drMake,drCarReg,drCarColor;
            genderErrorText = findViewById(R.id.genderErrorText);
            titleErrorText = findViewById(R.id.titleErrorText);
            driverGenderError = findViewById(R.id.genderDrErrorText);
            driverTitleError = findViewById(titleDrErrorText);
        //endregion

        //region Find all the EditText from Student
        edStFName = findViewById(editTextFullName);
        edStSurname = findViewById(editTextSurname);
        edStNumber = findViewById(editTextStudentNo);
        edtStContactNumber = findViewById(editTextContactNo);
        edStStreet = findViewById(editTexStreet);
        edStSuburb = findViewById(editTextSurburb);
        edStCity = findViewById(editTextCity);
        edStPostalCode = findViewById(editTextPostalCode);
        // endregion

        //region Find all the EditText from Driver
            driverName = findViewById(edtTxtD_FullName);
            driverSurname = findViewById(edtTxtD_Surname);
            driverEmpNumber = findViewById(edtTxtD_EmpID);
            driverContactNo = findViewById(edtTxtD_ContactNo);
            driverMake = findViewById(edtTxtCarMakeModel);
            driverCarReg = findViewById(edtTxtCarRegNo);
            driverCarColor = findViewById(edtTxtCarColor);
        //endregion

        //region RadioGroup and the Event Handlers
            rdGroupGender = findViewById(id.rdGroupGender);
            int selectID = rdGroupGender.getCheckedRadioButtonId();
            rdButton = findViewById(selectID);
            if(!(selectID == -1)){
                genderText = rdButton.getText().toString();
                genderErrorText.setVisibility(View.GONE);
            }
        rdDrGender = findViewById(rdDrGroupGender);
        int selectDr = rdDrGender.getCheckedRadioButtonId();
        rdDrButton = findViewById(selectDr);
        if(!(selectDr == -1)){
            drGender = rdDrButton.getText().toString();
            driverGenderError.setVisibility(View.GONE);
        }
        //endregion
        //region Get text from the Student Edit Texts
        String fullName = edStFName.getText().toString();
        String Surname = edStSurname.getText().toString();
        String studentNumber = edStNumber.getText().toString();
        String sContactNumber = edtStContactNumber.getText().toString();
        String stStreet = edStStreet.getText().toString();
        String stSuburb = edStSuburb.getText().toString();
        String stCity = edStCity.getText().toString();
        String stPostalCode = edStPostalCode.getText().toString();

        String studentAddress = stStreet+","+stSuburb+","+stCity+","+stPostalCode;

        //endregion

        //region Get text from Driver EditTexts
            drName = driverName.getText().toString();
            drSurname = driverSurname.getText().toString();
            drEmpNo = driverEmpNumber.getText().toString();
            drContactNo = driverContactNo.getText().toString();
            drMake = driverMake.getText().toString();
            drCarReg = driverCarReg.getText().toString();
            drCarColor = driverCarColor.getText().toString();

            String carDetails = drMake+","+drCarReg+","+drCarColor;
        //endregion


       //region Error trapping and validation
        int isStudent = scrollViewStudent.getVisibility();
        if (isStudent == View.VISIBLE){
            if(fullName.length()==0|Surname.length()==0|studentNumber.length()==0|sContactNumber.length()==0|stStreet.length()==0
                    |stSuburb.length()==0|stCity.length()==0|stPostalCode.length()==0){
                Toast.makeText(this, "Fill all Text fields", Toast.LENGTH_SHORT).show();

            }else if (selectID == -1) {
                genderErrorText.setVisibility(View.VISIBLE);
            }else if(stTitle==null){
                titleErrorText.setVisibility(View.VISIBLE);
            }else if (passWord == null){
                showPasswordDialog();
            }
            else {
                String insertSql = "INSERT INTO Student (StudentNo, StudentName, sResidentialAddress,sContactNo,Password,Surname,Gender,Title) VALUES ('"+studentNumber+"', '"+fullName+"','"+studentAddress+"','"+sContactNumber+"','"+passWord+"','"+Surname+"','"+genderText+"','"+ stTitle +"')";
                boolean entered = executeStatement(insertSql);
                if(entered){
                    Intent intent = new Intent(CreateProfile.this,LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"Profile created",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(),"Error: Details not entered",Toast.LENGTH_LONG).show();
                }
            }
        }else {
            if(drName.length() == 0 | drSurname.length() == 0 | drEmpNo.length() == 0
                    | drContactNo.length() == 0 | drMake.length() == 0 | drCarReg.length() == 0|drCarColor.length() == 0)
            {
                Toast.makeText(getApplicationContext(),"Fill all the Driver details",Toast.LENGTH_LONG).show();

            }else if(selectDr == -1){
                driverGenderError.setVisibility(View.VISIBLE);
            } else if(drTitle==null){
                driverTitleError.setVisibility(View.VISIBLE);
            }else if(passWord==null){
                showPasswordDialog();
            }
            else {

                String Sql = "INSERT INTO Driver(EmployeeID,EmployeeName,ContactDetails,Surname,Gender,Title,VehicleDetails,Password) VALUES('"+drEmpNo+"','"+drName+"','"+drContactNo+"','"+drSurname+"','"+drGender+"','"+drTitle+"','"+carDetails+"','"+passWord+"')";

                boolean enter = executeStatement(Sql);
                if(enter){
                    Intent intent = new Intent(CreateProfile.this,LoginActivity.class);
                    startActivity(intent);
                    Log.d("Wash It","Values entered...");
                    Toast.makeText(getApplicationContext(),"Profile created",Toast.LENGTH_LONG).show();
                }else {
                    Log.d("Wash It","Not entered");
                    Toast.makeText(getApplicationContext(),"Error: Details not entered",Toast.LENGTH_LONG).show();
                }
            }
        }

        //endregion

    }
    public static boolean executeStatement(String sql) {
        Statement statement = null;

        try {
            con = DatabaseManager.getConnection(); // Get the connection instance
            if (con != null) {
                statement = con.createStatement();
                int rowsAffected = statement.executeUpdate(sql);
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

    private void showPasswordDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(layout.dialog_password, null);
        final TextInputEditText passwordEditText = dialogView.findViewById(R.id.passwordEditText);
        final TextInputEditText confirmPasswordEditText = dialogView.findViewById(R.id.confirmPasswordEditText);
        final TextView errorMessage = dialogView.findViewById(R.id.errorMessage);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Set Password")
                .setView(dialogView)
                .setPositiveButton("Done", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String password = passwordEditText.getText().toString();
                        String confirmPassword = confirmPasswordEditText.getText().toString();

                        if (password.equals(confirmPassword)) {
                            passWord = password;
                            alertDialog.dismiss();
                        } else {
                            errorMessage.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }


}

