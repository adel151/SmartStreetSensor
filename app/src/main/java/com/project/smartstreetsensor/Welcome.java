package com.project.smartstreetsensor;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.project.smartstreetsensor.Utils.ActivityHelper;

public class Welcome extends AppCompatActivity {

    private ActivityHelper activityHelper;
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        activityHelper = new ActivityHelper(this);
        Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (waited < 3000) {
                        sleep(100);
                        waited += 100;
                    }
                } catch (InterruptedException e) {
                } finally {
                    activityHelper.GoTo(activity, Login.class, null, true);
                }
            }
        };

        splashThread.start();
    }

    @Override
    public void onBackPressed() {
    }
}
