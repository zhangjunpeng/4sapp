package com.test4s.account;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.Transition;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.tools.MyLog;
import com.test4s.myapp.R;
import com.test4s.net.LoginParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

public class AccountActivity extends AppCompatActivity {


    LoginFragment loginFragment;
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        fragmentManager=getSupportFragmentManager();
        loginFragment=new LoginFragment();
        fragmentManager.beginTransaction().replace(R.id.contianer_loginActivity,loginFragment).commit();


    }


    public void backlogin(){
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.in_form_left,R.anim.out_to_right);
        transaction.replace(R.id.contianer_loginActivity,loginFragment).commit();
    }

}
