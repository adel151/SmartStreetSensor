package com.project.smartstreetsensor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.project.smartstreetsensor.Utils.ActivityHelper;


public class Home extends AppCompatActivity  implements View.OnClickListener {

    private LinearLayout ln_detect, ln_map, ln_exit;

    private ActivityHelper activityHelper;
    private Activity activity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUI();
        init();
    }

    private void initUI(){
        ln_detect = (LinearLayout) findViewById(R.id.ln_detect);
        ln_map = (LinearLayout) findViewById(R.id.ln_map);
        ln_exit = (LinearLayout) findViewById(R.id.ln_exit);

        ln_detect.setOnClickListener(this);
        ln_map.setOnClickListener(this);
        ln_exit.setOnClickListener(this);
    }

    private void init(){
        activityHelper = new ActivityHelper(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.ln_detect:
                activityHelper.GoTo(activity, Map_Detect.class, null, true);
                break;

            case R.id.ln_map:
                activityHelper.GoTo(activity, Map.class, null, true);
                break;

            case R.id.ln_exit:
                logOut();
                break;
        }
    }


    private void logOut(){
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        activityHelper.GoTo(activity, Login.class, null, true);
                    }
                }).create().show();
    }
//00

    @Override
    public void onBackPressed() {
        logOut();
    }
}
