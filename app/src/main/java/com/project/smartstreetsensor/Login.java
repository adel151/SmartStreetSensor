package com.project.smartstreetsensor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.badoo.mobile.util.WeakHandler;
import com.project.smartstreetsensor.Listener.Login_ServiceListener;
import com.project.smartstreetsensor.Service.Login_Service;
import com.project.smartstreetsensor.Utils.ActivityHelper;
import com.project.smartstreetsensor.Utils.SharedP_Login;


public class Login extends AppCompatActivity implements View.OnClickListener, Login_ServiceListener {

    private EditText login_edt_username, login_edt_password;
    private TextView login_txt_errorMsg;
    private CheckBox login_chk_rememberMe;
    private Button login_btn_signin, login_btn_register;

    private RelativeLayout rl_loading;
    private ProgressBar spinner;

    private Login_Service loginService;
    private SharedP_Login sharedP_Login;
    private WeakHandler mHandler;

    private String username = "", password = "";

    private ActivityHelper activityHelper;
    private Activity activity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InitUI();
        Init();
        LoadLoginPrefs();
    }

    private void InitUI() {
        rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);

        login_edt_username = (EditText) findViewById(R.id.login_edt_username);
        login_edt_password = (EditText) findViewById(R.id.login_edt_password);

        login_chk_rememberMe = (CheckBox) findViewById(R.id.login_chk_rememberMe);

        login_txt_errorMsg = (TextView) findViewById(R.id.login_txt_errorMsg);

        login_btn_signin = (Button) findViewById(R.id.login_btn_signin);
        login_btn_signin.setOnClickListener(this);

        login_btn_register = (Button) findViewById(R.id.login_btn_register);
        login_btn_register.setOnClickListener(this);
    }

    private void Init() {
        activityHelper = new ActivityHelper(this);

        sharedP_Login = new SharedP_Login(this);
        loginService = new Login_Service(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn_signin:
                Login();
                break;

            case R.id.login_btn_register:
                activityHelper.GoTo(activity, Register.class, null, true);
                break;
        }
    }


    private void Login() {
        login_txt_errorMsg.setVisibility(View.GONE);

        username = login_edt_username.getText().toString();
        password = login_edt_password.getText().toString();

        if (!username.trim().equals("") && !password.trim().equals("")) {
            login_edt_username.setBackgroundResource(R.drawable.border_full);
            login_edt_password.setBackgroundResource(R.drawable.border_full);

            login_btn_signin.setVisibility(View.GONE);
            rl_loading.setVisibility(View.VISIBLE);

            loginService.login(username, password);
        } else {
            if (username.trim().equals("")) {
                login_edt_username.setBackgroundResource(R.drawable.border_full_red);
            } else {
                login_edt_username.setBackgroundResource(R.drawable.border_full);
            }

            if (password.trim().equals("")) {
                login_edt_password.setBackgroundResource(R.drawable.border_full_red);
            } else {
                login_edt_password.setBackgroundResource(R.drawable.border_full);
            }
        }
    }

    public void LoadLoginPrefs() {
        if (sharedP_Login.getRememberMeStatus()) {
            login_chk_rememberMe.setChecked(true);
            login_edt_username.setText(sharedP_Login.getUsername());
            login_edt_password.setText(sharedP_Login.getPassword());
        }
    }

    public void SaveLoginPrefs(String userid) {
        if (login_chk_rememberMe.isChecked()) {
            sharedP_Login.setRememberMeStatus(true);
            sharedP_Login.setUserId(userid);
            sharedP_Login.setUserName(login_edt_username.getText().toString().trim());
            sharedP_Login.setPassword(login_edt_password.getText().toString().trim());
        }
        else {
            sharedP_Login.clear();
        }
    }


    @Override
    public void loginServiceSuccess(com.project.smartstreetsensor.models.Login response) {
        SaveLoginPrefs(response.user.id);

        mHandler = new WeakHandler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                activityHelper.GoTo(activity, Home.class, null, true);
            }
        }, 500);
    }

    @Override
    public void loginServiceFailure(Exception exception) {
        login_txt_errorMsg.setText("Invalid Username/Password");
        login_txt_errorMsg.setVisibility(View.VISIBLE);

        rl_loading.setVisibility(View.GONE);
        login_btn_signin.setVisibility(View.VISIBLE);
    }


    private void logOut(){
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        activity.finish();
                    }
                }).create().show();
    }

    @Override
    public void onPause() {
        super.onPause();

        loginService.cancelLoginRequest();

        rl_loading.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();

        rl_loading.setVisibility(View.GONE);
        login_btn_signin.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        logOut();
    }

}
