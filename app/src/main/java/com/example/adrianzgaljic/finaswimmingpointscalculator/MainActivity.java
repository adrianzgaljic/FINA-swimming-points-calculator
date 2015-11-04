package com.example.adrianzgaljic.finaswimmingpointscalculator;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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
import android.widget.Spinner;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    public static String TAG = "logIspis";

    public static String event;
    public static String gender;
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

        //default gender is male
        gender = "m";

        //spinner for choosing event
        Spinner spinnerEvent = (Spinner) findViewById(R.id.spinnerEvent);
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

        //spinner for choosing course
        Spinner spinnerCourse = (Spinner) findViewById(R.id.spinnerCourse);
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


        //radio buttons for choosing gender, radioM is checked by default
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

        //field for points input and displaying result
        etPoints = (EditText) findViewById(R.id.etPoints);

        //field for time input and displaying result
        etTime = (EditText) findViewById(R.id.etTime);

        //button for starting calculation
        //if time field is focused and not empty, points will be calculated and vice versa
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

        //toolbar with title
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Stopwatch");
        toolbar.setTitleTextColor(Color.WHITE);



        ActionBarDrawerToggle drawerToggle = setupDrawerToggle();
        drawerToggle.syncState();

        //navigation drawer
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.setDrawerListener(drawerToggle);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);


    }

    /**
     * Method which sets navigation drawer listener
     * @param navigationView navigation view
     */
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

    /**
     * Mathod which starts selected activity
     * @param menuItem item slected from navigtion drawer
     */
    public void selectDrawerItem(MenuItem menuItem) {

        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_calculator:
                fragmentClass = MainActivity.class;
                break;
            case R.id.nav_stopwatch:
                fragmentClass = StopwatchActivity.class;
                break;
            case R.id.nav_convert:
                fragmentClass = ConversionActivity.class;
                break;
            case R.id.nav_about:
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

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }


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

    /**
     * Method which calculates points
     * @param time input time
     * @return calculated points
     */
    public static int calculatePoints(float time) {

        float points = 1000*(float)Math.pow((baseTime/time),3);
        return  (int)points;

    }

    /**
     * Method which calculates float time from string input
     * @param strTime time in string
     * @param context this context
     * @return float time
     */
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

    /**
     * Method which return base time for selected event
     * @param context this context
     * @return base time
     */
    public static float getBaseTime(Context context){
        String eventPart;
        String coursePart;
        if (event == null){
            Toast.makeText(context,"Choose event.",Toast.LENGTH_SHORT).show();
            return 0;
        }
        String eventParts[] = event.split(" ");
        eventPart = eventParts[1]+eventParts[0];


        switch (course) {
            case "Long course meters":
                coursePart = "lcm";
                break;
            case "Short course meters":
                coursePart = "scm";
                break;
            default:
                coursePart = "scy";
                break;
        }

        if (coursePart.equals("scy")){
            String query = eventPart+"scm"+gender;
            int id = context.getResources().getIdentifier(query,"integer",context.getPackageName());
            baseTime = (float)context.getResources().getInteger(id)/100;
            switch (eventParts[0]) {
                case "50":
                    baseTime = (float) ((baseTime - 1) / 1.1);
                    break;
                case "100":
                    baseTime = (float) ((baseTime - 2) / 1.1);
                    break;
                case "200":
                    baseTime = (float) ((baseTime - 4) / 1.1);
                    break;
                case "400":
                    baseTime = (float) ((baseTime - 8) / 1.1);
                    break;
                default:
                    baseTime = 0;
                    Toast.makeText(context, "There is no " + eventParts[0] + " yard event.", Toast.LENGTH_SHORT).show();

                    break;
            }


        } else {
            String query = eventPart+coursePart+gender;
            int id = context.getResources().getIdentifier(query,"integer",context.getPackageName());
            baseTime = (float)context.getResources().getInteger(id)/100;

        }
        return baseTime;

    }


}
