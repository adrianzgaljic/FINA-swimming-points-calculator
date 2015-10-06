package com.example.adrianzgaljic.finaswimmingpointscalculator;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;


public class MainActivity extends AppCompatActivity {

    public static String TAG = "logIspis";

    private Spinner spinnerEvent;
    private String event;
    private String gender;
    private Spinner spinnerCourse;
    private String course;
    private EditText etTime;
    private EditText etPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        event = null;
        course = null;

        spinnerEvent = (Spinner) findViewById(R.id.spinnerEvent);
        spinnerEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                event = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                event = null;
            }
        });

        spinnerCourse = (Spinner) findViewById(R.id.spinnerCourse);
        spinnerCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                course = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                course = null;
            }
        });

        final RadioGroup radioGender = (RadioGroup) findViewById(R.id.radioGender);

        final RadioButton radioM = (RadioButton) findViewById(R.id.radioM);
        final RadioButton radioF = (RadioButton) findViewById(R.id.radioF);

        radioM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioF.setChecked(false);
                gender = "m";
            }
        });

        radioF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioM.setChecked(false);
                gender = "f";
            }
        });

        etPoints = (EditText) findViewById(R.id.etPoints);
        etTime = (EditText) findViewById(R.id.etTime);

        Button btnCalculate = (Button) findViewById(R.id.btnCalculate);
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strTime = etTime.getText().toString();
                String strPoints = etPoints.getText().toString();
                if (!strTime.equals("") && etTime.isFocused()){
                    float time = parseTime(strTime);
                    float result = calculatePoints(time);
                    etPoints.setText(Float.toString(result));
                } else if (!strPoints.equals("") && etPoints.isFocused()){
                    float points = Float.parseFloat(strPoints);
                    String time = calculateTime(points);
                    etTime.setText(time);
                } else {
                    Toast.makeText(MainActivity.this,"Time and points fields are empty.",Toast.LENGTH_SHORT).show();
                }



            }
        });




    }

    private String calculateTime(Float points) {

        int min;
        int sec;
        int hund;

        float baseTime = getBaseTime();
        Log.i(TAG,Float.toString(baseTime));
        float time = (float) (baseTime * ((float)10/(Math.pow(points,((float)1/3)))));
        min = (int) Math.floor(time/60);
        sec = (int) (Math.floor(time)-60*min);
        hund = (int)((time-(int)time)*100);
        String res;
        if (min==0){
            res = sec+":"+hund;
        } else {
            res = min+":"+sec+":"+hund;
        }
        return res;


    }

    private float calculatePoints(float time) {

        float baseTime = getBaseTime();
        return 1000*(float)Math.pow((baseTime/time),3);
    }

    private float parseTime(String strTime) {
        int min;
        int sec;
        int hund;
        String[] digits = strTime.split(":");
        if (digits.length <2 || digits.length > 3){
            Toast.makeText(this,"Invalid time input",Toast.LENGTH_SHORT).show();
            return 0;
        } else if (digits.length == 2){
            min = 0;
            sec = Integer.parseInt(digits[0]);
            hund = Integer.parseInt(digits[1]);

        } else {
            min = Integer.parseInt(digits[0]);
            sec = Integer.parseInt(digits[1]);
            hund = Integer.parseInt(digits[2]);
        }

        return min*60+sec+(float)hund/100;


    }

    private float getBaseTime(){
        String eventPart = "";
        String coursePart = "";
        if (event == null){
            Toast.makeText(this,"Choose event.",Toast.LENGTH_SHORT).show();
            return 0;
        }
        String eventParts[] = event.split(" ");
        eventPart = eventParts[1]+eventParts[0];



        if (course.equals("Long course meters")) {
            coursePart = "lcm";
        } else if (course.equals("Short course meters")) {
            coursePart = "scm";
        } else {
            coursePart = "scy";
        }

        String query = eventPart+coursePart+gender;
        int id = getResources().getIdentifier(query,"integer",getPackageName());
        float baseTime = (float)getResources().getInteger(id)/100;
        return baseTime;
    }

}
