package com.project.smartstreetsensor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.badoo.mobile.util.WeakHandler;
import com.project.smartstreetsensor.Listener.Register_ServiceListener;
import com.project.smartstreetsensor.Service.Register_Service;
import com.project.smartstreetsensor.Utils.ActivityHelper;

public class Register extends AppCompatActivity implements View.OnClickListener, Register_ServiceListener {

    private TextView txt_error;
    private EditText edt_fullName, edt_email, edt_password, edt_contactNo, edt_address;
    private FloatingActionButton fab;

    private Register_Service register_service;
    private ProgressDialog pDialog;
    private WeakHandler mHandler;

    private String name, email, password, contactNo, address;
    private boolean isFieldEmpty = false;

    private ActivityHelper activityHelper;
    private Activity activity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        InitUI();
        Init();
    }

    private void InitUI() {
        edt_fullName = (EditText) findViewById(R.id.edt_fullName);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_password = (EditText) findViewById(R.id.edt_password);
        edt_contactNo = (EditText) findViewById(R.id.edt_contactNo);
        edt_address = (EditText) findViewById(R.id.edt_address);

        txt_error = (TextView) findViewById(R.id.txt_error);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    private void Init() {
        activityHelper = new ActivityHelper(this);
        register_service = new Register_Service(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                register();
                break;
        }
    }


    private void register() {
        showPDialog();

        if (validateFormFields())
            register_service.register(name, email, password, contactNo, address);
        else
            hidePDialog();
    }

    @Override
    public void onRegister_ServiceSuccess(com.project.smartstreetsensor.models.Login response) {
        hidePDialog();

        mHandler = new WeakHandler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                activityHelper.GoTo(activity, Home.class, null, true);
            }
        }, 200);
    }

    @Override
    public void onRegister_ServiceFailure(Exception exception) {
        hidePDialog();
    }


    private boolean validateFormFields() {
        name = edt_fullName.getText().toString();
        password = edt_password.getText().toString();
        email = edt_email.getText().toString();
        contactNo = edt_contactNo.getText().toString();
        address = edt_address.getText().toString();

        isFieldEmpty = TextUtils.isEmpty(name) ? true : false;
        if (isFieldEmpty) {
            edt_fullName.setBackgroundResource(R.drawable.border_full_red);
            return false;
        } else
            edt_fullName.setBackgroundResource(R.drawable.border_full_green);

        isFieldEmpty = TextUtils.isEmpty(email) ? true : false;
        if (isFieldEmpty || !isValidEmailAddress(email)) {
            edt_email.setBackgroundResource(R.drawable.border_full_red);
            return false;
        } else
            edt_email.setBackgroundResource(R.drawable.border_full_green);

        isFieldEmpty = TextUtils.isEmpty(password) ? true : false;
        if (isFieldEmpty) {
            edt_password.setBackgroundResource(R.drawable.border_full_red);
            return false;
        } else
            edt_password.setBackgroundResource(R.drawable.border_full_green);

        isFieldEmpty = TextUtils.isEmpty(contactNo) ? true : false;
        if (isFieldEmpty) {
            edt_contactNo.setBackgroundResource(R.drawable.border_full_red);
            return false;
        } else
            edt_contactNo.setBackgroundResource(R.drawable.border_full_green);

        isFieldEmpty = TextUtils.isEmpty(address) ? true : false;
        if (isFieldEmpty) {
            edt_address.setBackgroundResource(R.drawable.border_full_red);
            return false;
        } else
            edt_address.setBackgroundResource(R.drawable.border_full_green);

        return true;
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }


    private void showPDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(true);
        pDialog.setMessage("Registering...");
        pDialog.show();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }


    @Override
    public void onPause() {
        super.onPause();

        register_service.cancelRegisterRequest();

        hidePDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        activityHelper.GoTo(activity, Login.class, null, true);
    }


}
