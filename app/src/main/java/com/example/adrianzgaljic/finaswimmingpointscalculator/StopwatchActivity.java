package com.example.adrianzgaljic.finaswimmingpointscalculator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.adrianzgaljic.finaswimmingpointscalculator.Chronometer;

/**
 * Created by adrianzgaljic on 07/10/15.
 */
public class StopwatchActivity extends FragmentActivity {

    private Button btnReset;
    private  Button btnStart;
    private EditText etPoints;
    boolean started = false;
    static float baseTime;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    public NavigationView nvDrawer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stopwatch);
        final Chronometer mChronometer = (Chronometer) findViewById(R.id.chrono);

        btnReset = (Button) findViewById(R.id.btnReset);
        btnStart = (Button) findViewById(R.id.btnStart);
        etPoints = (EditText) findViewById(R.id.swPoints);
        baseTime = MainActivity.baseTime*1000;
       // final com.example.adrianzgaljic.finaswimmingpointscalculator.Chronometer mChronometer = (com.example.adrianzgaljic.finaswimmingpointscalculator.Chronometer) findViewById(R.id.chronometer);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!started) {
                    started = true;
                    btnStart.setText("STOP");
                    mChronometer.start();
                } else {
                    started = false;
                    btnStart.setText("START");
                    mChronometer.stop();
                }
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChronometer.reset();
                started = false;
                btnStart.setText("START");
                etPoints.setText("");
            }
        });
        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            public void onChronometerTick(Chronometer chronometer) {
                //if (chronometer.getText().toString().equalsIgnoreCase("00:05:0")) { //When reaches 5 seconds.
                //Define here what happens when the Chronometer reaches the time above.
                //chronometer.stop();

                float time = chronometer.getTimeElapsed();
                etPoints.setText(Float.toString(1000 * (float) Math.pow((baseTime / time), 3)));

                //

                //}
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

}

