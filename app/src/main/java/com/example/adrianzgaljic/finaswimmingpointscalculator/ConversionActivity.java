package com.example.adrianzgaljic.finaswimmingpointscalculator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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
 */
public class ConversionActivity extends AppCompatActivity {

    private Spinner spinnerEvent;
    private Spinner spinnerCourseFrom;
    private Spinner spinnerCourseTo;
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

        spinnerEvent = (Spinner) findViewById(R.id.spinnerEventConversion);
        spinnerCourseFrom = (Spinner) findViewById(R.id.spinnerCourseFrom);
        spinnerCourseTo = (Spinner) findViewById(R.id.spinnerCourseTo);
        edFrom = (EditText) findViewById(R.id.etTimeFrom);
        edTo = (EditText) findViewById(R.id.etTimeTo);
        btnConvert = (Button) findViewById(R.id.btnConvert);

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
