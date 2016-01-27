package com.test4s.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.tools.MyLog;
import com.test4s.myapp.R;
import com.test4s.net.LoginParams;

import org.xutils.common.Callback;
import org.xutils.x;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{


    private Button login;
    private Button reg;
    private EditText editText_name;
    private EditText editText_pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login= (Button) findViewById(R.id.loginButton_login);
        reg= (Button) findViewById(R.id.reg_login);
        editText_name= (EditText) findViewById(R.id.name_login);
        editText_pwd= (EditText) findViewById(R.id.pwd_login);
        login.setOnClickListener(this);
        reg.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton_login:
                login();
                break;
            case R.id.reg_login:
                break;
        }

    }

    private void login() {
        String name=editText_name.getText().toString();
        String pwd=editText_pwd.getText().toString();
        if (TextUtils.isEmpty(name)||TextUtils.isEmpty(pwd)){
            Toast.makeText(this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        LoginParams loginParams=new LoginParams(name,pwd);
        x.http().post(loginParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("login_back:::"+result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
        
    }
}
