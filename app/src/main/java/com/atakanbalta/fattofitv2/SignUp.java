package com.atakanbalta.fattofitv2;


import android.content.Intent;
import android.graphics.Color;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.Calendar;

import okhttp3.OkHttpClient;

public class SignUp extends AppCompatActivity {


    private String [] arraySpinnerDOBDay = new String[31];
    private String [] arraySpinnerDOBYear = new String[100];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        /*STETHO TOOL--------------------------------- */
        Stetho.initializeWithDefaults(this);
        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        /*-----------------------*/








        /* Hide error icon and message */
        ImageView imageViewError = (ImageView)findViewById(R.id.imageViewError);
        imageViewError.setVisibility(View.GONE);

        TextView textViewErrorMessage = (TextView)findViewById(R.id.textViewErrorMessage);
        textViewErrorMessage.setVisibility(View.GONE);
        /* Hide inches field */
        EditText editTextHeightInches = (EditText)findViewById(R.id.editTextHeightInches);
        editTextHeightInches.setVisibility(View.GONE);
        /* Listener Mesurment spinner */
        Spinner spinnerMesurment = (Spinner)findViewById(R.id.spinnerMesurment);
        spinnerMesurment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                mesurmentChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // mesurmentChanged();
            }
        });





        /*Fill with umbers for date of birth days */
        int human_counter = 0;
        for(int x=0; x<31; x++){
            human_counter = x+1;
            this.arraySpinnerDOBDay[x]= "" + human_counter;
        }

        Spinner spinnerDOBDay = (Spinner) findViewById(R.id.spinnerDOBDay);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arraySpinnerDOBDay);
        spinnerDOBDay.setAdapter(adapter);


        /* Fill numbers for date of birth year */
        // get current year、month and day
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int end = year-100;
        int index = 0;
        for(int x=year;x>end;x--){
            this.arraySpinnerDOBYear[index] = "" + x;
            // Toast.makeText(this, "x = " + x, Toast.LENGTH_SHORT).show();

            index++;
        }

        Spinner spinnerDOBYear = (Spinner) findViewById(R.id.spinnerDOBYear);
        ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinnerDOBYear);
        spinnerDOBYear.setAdapter(adapterYear);


        /*LISTENER */
        Button buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                signUpSubmit();

            }
        });
















    } // ONCREATE
    /*- Mesurment changed ------------------------------------------ */
    public void mesurmentChanged() {

        // Mesurment spinner
        Spinner spinnerMesurment = (Spinner) findViewById(R.id.spinnerMesurment);
        String stringMesurment = spinnerMesurment.getSelectedItem().toString();


        EditText editTextHeightCm = (EditText) findViewById(R.id.editTextHeightCm);
        EditText editTextHeightInches = (EditText) findViewById(R.id.editTextHeightInches);
        String stringHeightCm = editTextHeightCm.getText().toString();
        String stringHeightInches = editTextHeightInches.getText().toString();

        double heightCm = 0;
        double heightFeet = 0;
        double heightInches = 0;

        TextView textViewCm = (TextView) findViewById(R.id.textViewCm);
        TextView textViewKg = (TextView) findViewById(R.id.textViewKg);

        if (stringMesurment.startsWith("I")) {
            // Imperial
            editTextHeightInches.setVisibility(View.VISIBLE);
            textViewCm.setText("feet and inches");
            textViewKg.setText("pound");

            // Find feet
            try {
                heightCm = Double.parseDouble(stringHeightCm);
            } catch (NumberFormatException nfe) {

            }
            if (heightCm != 0) {
                // Convert CM into feet
                // feet = cm * 0.3937008)/12
                heightFeet = (heightCm * 0.3937008) / 12;
                // heightFeet = Math.round(heightFeet);
                int intHeightFeet = (int) heightFeet;

                editTextHeightCm.setText("" + intHeightFeet);

            }

        } // if(stringMesurment.startsWith("I")){
        else {
            // Metric
            editTextHeightInches.setVisibility(View.GONE);
            textViewCm.setText("cm");
            textViewKg.setText("kg");

            // Feet to cm

            // Convert Feet
            try {
                heightFeet = Double.parseDouble(stringHeightCm);
            } catch (NumberFormatException nfe) {

            }

            // Convert inches
            try {
                heightInches = Double.parseDouble(stringHeightInches);
            } catch (NumberFormatException nfe) {

            }

            // Need to convert, we want to save the number in cm
            // cm = ((foot * 12) + inches) * 2.54
            if (heightFeet != 0 && heightInches != 0) {
                heightCm = ((heightFeet * 12) + heightInches) * 2.54;
                heightCm = Math.round(heightCm);
                editTextHeightCm.setText("" + heightCm);
            }
        }
        /* Weight */
        EditText editTextWeight = (EditText)findViewById(R.id.editTextWeight);
        String stringWeight = editTextWeight.getText().toString();
        double doubleWeight = 0;
        try {
            doubleWeight = Double.parseDouble(stringWeight);
        }
        catch(NumberFormatException nfe) {
            String errorMessage = "Weight has to be a number.";
        }
        if(doubleWeight != 0 ) {
            if(stringMesurment.startsWith("I")){
                doubleWeight = Math.round(doubleWeight / 0.45359237);
            }else{
                doubleWeight = Math.round(doubleWeight*0.45359237);
            }
            editTextWeight.setText(""+doubleWeight);
        }


    }


    /* SIGN UP SUBMIT */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void signUpSubmit(){

        //ERROR
        //error
        ImageView imageViewError = (ImageView) findViewById(R.id.imageViewError);
        TextView textViewErrorMessage = (TextView) findViewById(R.id.textViewErrorMessage);
        String errorMessage = "";

        int error = 0;


        //E-MAIL
        TextView textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        EditText editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        final String stringEmail = editTextEmail.getText().toString();

        if (stringEmail.isEmpty() || stringEmail.startsWith(" ")) {
            error = 1;
            textViewEmail.setTextColor(Color.RED);
            errorMessage = "Email kısmını doldurunuz";
        } else {
            textViewEmail.setTextColor(Color.RED);
            textViewEmail.setTextColor(Color.GRAY);
        }
        //E-MAIL
        TextView textViewPassword = (TextView) findViewById(R.id.textViewPassword);
        EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        final String stringPassword = editTextPassword.getText().toString();
        if (stringPassword.isEmpty() || stringPassword.startsWith(" ")) {
            error = 1;
            textViewEmail.setTextColor(Color.RED);
            errorMessage = "Şifre kısmını doldurunuz";
        } else {
            textViewPassword.setTextColor(Color.RED);
            textViewPassword.setTextColor(Color.GRAY);
        }




        // Date of Birth Day
        Spinner spinnerDOBDay = (Spinner)findViewById(R.id.spinnerDOBDay);
        String stringDOBDay = spinnerDOBDay.getSelectedItem().toString();
        int intDOBDay = 0;
        try {
            intDOBDay = Integer.parseInt(stringDOBDay);

            if(intDOBDay < 10){
                stringDOBDay = "0" + stringDOBDay;
            }

        }
        catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
            errorMessage = "Lütfen doğum gününüzü seçiniz.";
        }
        // Date of Birth Month
        Spinner spinnerDOBMonth = (Spinner)findViewById(R.id.spinnerDOBMonth);
        String stringDOBMonth = spinnerDOBMonth.getSelectedItem().toString();
        int positionDOBMonth = spinnerDOBMonth.getSelectedItemPosition();
        int month = positionDOBMonth+1;
        if(month < 10){
            stringDOBMonth = "0" + month;
        }
        else{
            stringDOBMonth = "" + month;
        }



        // Date of Birth Year
        Spinner spinnerDOBYear = (Spinner)findViewById(R.id.spinnerDOBYear);
        String stringDOBYear = spinnerDOBYear.getSelectedItem().toString();
        int intDOBYear = 0;
        try {
            intDOBYear = Integer.parseInt(stringDOBYear);
        }
        catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
            errorMessage = "Lütfen doğum yılınızı seçiniz.";
        }
        // Put date of birth togheter
        String dateOfBirth = intDOBYear + "-" + stringDOBMonth + "-" + stringDOBDay;
        //.makeText(this,dateOfBirth,Toast.LENGTH_LONG).show();

        // Gender
        RadioGroup radioGroupGender = (RadioGroup)findViewById(R.id.radioGroupGender);
        int radioButtonID = radioGroupGender.getCheckedRadioButtonId(); // get selected radio button from radioGroup
        View radioButtonGender = radioGroupGender.findViewById(radioButtonID);
        int position = radioGroupGender.indexOfChild(radioButtonGender); // If you want position of Radiobutton

        String stringGender = "";
        if(position == 0){
            stringGender = "male";
        }
        else{
            stringGender = "female";
        }
        /* Height */
        EditText editTextHeightCm = (EditText)findViewById(R.id.editTextHeightCm);
        EditText editTextHeightInches = (EditText)findViewById(R.id.editTextHeightInches);
        String stringHeightCm = editTextHeightCm.getText().toString();
        String stringHeightInches = editTextHeightInches.getText().toString();

        double heightCm = 0;
        double heightFeet = 0;
        double heightInches = 0;
        boolean metric = true;

        // Metric or imperial?
        Spinner spinnerMesurment = (Spinner)findViewById(R.id.spinnerMesurment);
        String stringMesurment = spinnerMesurment.getSelectedItem().toString();

        int intMesurment = spinnerMesurment.getSelectedItemPosition();
        if(intMesurment == 0){
            stringMesurment = "metric";
        }
        else{
            stringMesurment = "imperial";
            metric = false;
        }

        if(metric == true) {

            // Convert CM
            try {
                heightCm = Double.parseDouble(stringHeightCm);
                heightCm = Math.round(heightCm);
            }
            catch(NumberFormatException nfe) {
                errorMessage = "Height (cm) has to be a number.";
            }
        }
        else {

            // Convert Feet
            try {
                heightFeet = Double.parseDouble(stringHeightCm);
            }
            catch(NumberFormatException nfe) {
                errorMessage = "Height (feet) has to be a number.";
            }

            // Convert inches
            try {
                heightInches = Double.parseDouble(stringHeightInches);
            }
            catch(NumberFormatException nfe) {
                errorMessage = "Height (inches) has to be a number.";
            }

            // Need to convert, we want to save the number in cm
            // cm = ((foot * 12) + inches) * 2.54
            heightCm = ((heightFeet * 12) + heightInches) * 2.54;
            heightCm = Math.round(heightCm);
        }

        // Weight
        EditText editTextWeight = (EditText)findViewById(R.id.editTextWeight);
        String stringWeight = editTextWeight.getText().toString();
        double doubleWeight = 0;
        try {
            doubleWeight = Double.parseDouble(stringWeight);
        }
        catch(NumberFormatException nfe) {
            errorMessage = "Kilonuz bir rakam olmalı";
        }
        if(metric == true) {
        }
        else{
            // Imperial
            // Pound to kg
            doubleWeight = Math.round(doubleWeight*0.45359237);
        }


        // Activity level
        Spinner spinnerActivityLevel = (Spinner)findViewById(R.id.spinnerActivityLevel);

        //  0: Little to no exercise
        // 1: Light exercise (1–3 days per week)
        // 2: Moderate exercise (3–5 days per week)
        // 3: Heavy exercise (6–7 days per week)
        // 4: Very heavy exercise (twice per day, extra heavy workouts)
        int intActivityLevel = spinnerActivityLevel.getSelectedItemPosition();

        // INSERT DB
        // Error handling
        if(errorMessage.isEmpty()){
            // Put data into database
            imageViewError.setVisibility(View.GONE);
            textViewErrorMessage.setVisibility(View.GONE);


            // Insert into database
            DBAdapter db = new DBAdapter(this);
            db.open();

            // Quote smart
            String stringEmailSQL = db.quoteSmart(stringEmail);
            String stringPasswordSQL =db.quoteSmart(stringPassword);
            String dateOfBirthSQL = db.quoteSmart(dateOfBirth);
            String stringGenderSQL = db.quoteSmart(stringGender);
            double heightCmSQL = db.quoteSmart(heightCm);
            int intActivityLevelSQL = db.quoteSmart(intActivityLevel);
            double doubleWeightSQL = db.quoteSmart(doubleWeight);
            String stringMesurmentSQL = db.quoteSmart(stringMesurment);

            // Input for users
            String stringInput = "NULL, " + stringEmailSQL + ","+stringPasswordSQL+"," + dateOfBirthSQL + "," + stringGenderSQL + "," + heightCmSQL + "," + intActivityLevelSQL + "," + stringMesurmentSQL;
            db.insert("users",
                    "user_id, user_email,user_password, user_dob, user_gender, user_height, user_activity_level, user_mesurment",
                    stringInput);

            // Input for goal

            DateFormat df1=new SimpleDateFormat("yyyy-MM-dd");//foramt date
            String goalDate=df1.format(android.icu.util.Calendar.getInstance().getTime());

            String goalDateSQL = db.quoteSmart(goalDate);

            stringInput = "NULL, " + doubleWeightSQL + "," + goalDateSQL;
            db.insert("goal",
                    "goal_id, goal_current_weight, goal_date",
                    stringInput);





            db.close();

            // Move user back to MainActivity
            Intent i = new Intent(SignUp.this, SignUpGoal.class);
            startActivity(i);

        }
        else {
            // There is error
            textViewErrorMessage.setText(errorMessage);
            imageViewError.setVisibility(View.VISIBLE);
            textViewErrorMessage.setVisibility(View.VISIBLE);
        }








    }  //SIGNUPSUBMIT





}/// MAIN CLASS
