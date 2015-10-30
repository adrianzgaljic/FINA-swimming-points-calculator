package com.example.adrianzgaljic.finaswimmingpointscalculator;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


import java.lang.reflect.Array;
import java.math.RoundingMode;
import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {

    public static String TAG = "logIspis";

    private Spinner spinnerEvent;
    public static String event;
    public static String gender;
    private Spinner spinnerCourse;
    public static String course;
    private EditText etTime;
    private EditText etPoints;
    public static float baseTime;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    public NavigationView nvDrawer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        event = null;
        course = null;
        gender = "m";


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
                baseTime = getBaseTime(MainActivity.this);

                String strTime = etTime.getText().toString();
                String strPoints = etPoints.getText().toString();
                if (!strTime.equals("") && etTime.isFocused()){
                    float time = parseTime(strTime,MainActivity.this);
                    int result = calculatePoints(time);
                    etPoints.setText(Integer.toString(result));
                } else if (!strPoints.equals("") && etPoints.isFocused()){
                    float points = Float.parseFloat(strPoints);
                    String time = calculateTime(points);
                    etTime.setText(time);
                } else {
                    Toast.makeText(MainActivity.this,"Time and points fields are empty.",Toast.LENGTH_SHORT).show();
                }



            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("FINA Swimming Points Calculator");

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle drawerToggle = setupDrawerToggle();
        drawerToggle.syncState();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.setDrawerListener(drawerToggle);


        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);




    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on
        // position
        Fragment fragment = null;

        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = StopwatchActivity.class;
                break;
            case R.id.nav_second_fragment:
                fragmentClass = ConversionActivity.class;
                break;
            case R.id.nav_third_fragment:
                fragmentClass = ShowInfoActivity.class;
                break;
            default:
                fragmentClass = ShowInfoActivity.class;
        }


        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
        Intent stopwatchIntent = new Intent(MainActivity.this, fragmentClass);
        startActivity(stopwatchIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Make sure this is the method with just `Bundle` as the signature
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public static String calculateTime(Float points) {

        int min;
        int sec;
        int hund;


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

    public static int calculatePoints(float time) {

        float points = 1000*(float)Math.pow((baseTime/time),3);
        return  (int)points;
       // return (float)pointsInt;
    }

    public static float parseTime(String strTime, Context context) {
        int min;
        int sec;
        int hund;
        String[] digits = strTime.split(":");
        if (digits.length <2 || digits.length > 3){
            Toast.makeText(context,"Invalid time input",Toast.LENGTH_SHORT).show();
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

    public static float getBaseTime(Context context){
        String eventPart = "";
        String coursePart = "";
        if (event == null){
            Toast.makeText(context,"Choose event.",Toast.LENGTH_SHORT).show();
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

        if (coursePart.equals("scy")){
            String query = eventPart+"scm"+gender;
            int id = context.getResources().getIdentifier(query,"integer",context.getPackageName());
            baseTime = (float)context.getResources().getInteger(id)/100;
            if (eventParts[0].equals("50")){
                baseTime = (float) ((baseTime-1)/1.1);
            } else if (eventParts[0].equals("100")){
                baseTime = (float) ((baseTime-2)/1.1);
            }else if (eventParts[0].equals("200")){
                baseTime = (float) ((baseTime-4)/1.1);
            }else if (eventParts[0].equals("400")){
                baseTime = (float) ((baseTime-8)/1.1);
            }else {
                baseTime = 0;
                Toast.makeText(context,"There is no "+eventParts[0]+" yard event.",Toast.LENGTH_SHORT).show();

            }


        } else {
            String query = eventPart+coursePart+gender;
            int id = context.getResources().getIdentifier(query,"integer",context.getPackageName());
            baseTime = (float)context.getResources().getInteger(id)/100;

        }
        return baseTime;

    }

    public float toYards(float meters){
        String eventParts[] = event.split(" ");
        String length = eventParts[1];

        course = "scm";
        Float scmBaseTime = getBaseTime(MainActivity.this);


        return scmBaseTime;
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

}
