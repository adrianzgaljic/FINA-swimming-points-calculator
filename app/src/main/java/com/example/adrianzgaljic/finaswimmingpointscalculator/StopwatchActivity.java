package com.example.adrianzgaljic.finaswimmingpointscalculator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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


/**
 *
 * Created by adrianzgaljic on 07/10/15.
 *
 * Stopwatch activity which enables user to pick event and use stopwatch with FINA points calculated simultaneously.
 */
public class StopwatchActivity extends FragmentActivity {

    private  Button btnStart;
    boolean started = false;
    public static String event;
    public static String gender;
    public static String course;
    private EditText etPoints;
    public static float baseTime;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    public NavigationView nvDrawer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stopwatch);
        final Chronometer mChronometer = (Chronometer) findViewById(R.id.chrono);

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


        //calculated points are displayed here
        etPoints = (EditText) findViewById(R.id.swPoints);

        //button for starting and stopping stopwatch
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!started) {
                    baseTime = getBaseTime(StopwatchActivity.this)*10;
                    Log.i("LogIspis", "base time..." + baseTime);
                    started = true;
                    btnStart.setText(R.string.Stop);
                    mChronometer.start();
                } else {
                    started = false;
                    btnStart.setText(R.string.Start);
                    mChronometer.stop();
                }
            }
        });

        //button for resetting stopwatch
        Button btnReset = (Button) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChronometer.reset();
                started = false;
                btnStart.setText(R.string.Start);
                etPoints.setText("");
            }
        });

        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            public void onChronometerTick(Chronometer chronometer) {

                float time = chronometer.getTimeElapsed();
                etPoints.setText(Float.toString(1000 * (float) Math.pow((baseTime / time), 3)));

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
        Intent stopwatchIntent = new Intent(StopwatchActivity.this, fragmentClass);
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

    /**
     * Mathod which returns base time for selected event
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
            baseTime = (float)context.getResources().getInteger(id);
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
            baseTime = (float)context.getResources().getInteger(id);

        }
        return baseTime;

    }

}

