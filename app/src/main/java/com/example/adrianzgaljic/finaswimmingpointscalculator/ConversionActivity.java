package com.example.adrianzgaljic.finaswimmingpointscalculator;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by adrianzgaljic on 11/10/15.
 *
 * Acitivity which enables conversions between times in 3 different pool sizes:
 * long course meters, short course meters and short course yards
 */
public class ConversionActivity extends AppCompatActivity {

    private EditText edFrom;
    private EditText edTo;
    public String event;
    public String courseFrom;
    public String courseTo;
    public Button btnConvert;
    private String genderMain;
    private String courseMain;
    private String eventMain;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    public NavigationView nvDrawer;

    @Override
    public  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversion);

        //field for time input
        edFrom = (EditText) findViewById(R.id.etTimeFrom);

        //field for calculated result
        edTo = (EditText) findViewById(R.id.etTimeTo);


        //spinner for choosing event
        Spinner spinnerEvent = (Spinner) findViewById(R.id.spinnerEventConversion);
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

        //spinner for choosing course from which user wants to calculate time
        Spinner spinnerCourseFrom = (Spinner) findViewById(R.id.spinnerCourseFrom);
        spinnerCourseFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                courseFrom = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                courseFrom = null;
            }
        });

        //spinner for choosing course to which user wants to calculate time
        Spinner spinnerCourseTo = (Spinner) findViewById(R.id.spinnerCourseTo);
        spinnerCourseTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                courseTo = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                courseTo = null;
            }
        });

        //button for starting conversion
        btnConvert = (Button) findViewById(R.id.btnConvert);
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strTime = edFrom.getText().toString();
                float time = MainActivity.parseTime(strTime, ConversionActivity.this);
                genderMain = MainActivity.gender;
                courseMain = MainActivity.course;
                eventMain = MainActivity.event;
                MainActivity.gender = "m";
                MainActivity.course = courseFrom;
                MainActivity.event = event;
                MainActivity.getBaseTime(ConversionActivity.this);
                int points = MainActivity.calculatePoints(time);
                MainActivity.course = courseTo;
                MainActivity.getBaseTime(ConversionActivity.this);
                strTime = MainActivity.calculateTime(new Float(points));

                edTo.setText(strTime);
                MainActivity.gender = genderMain;
                MainActivity.course = courseMain;
                MainActivity.event = eventMain;


            }
        });


        //toolbar with title
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Convert times");
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
        Intent stopwatchIntent = new Intent(ConversionActivity.this, fragmentClass);
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

}
