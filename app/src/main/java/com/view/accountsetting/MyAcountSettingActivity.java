package com.view.accountsetting;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.test4s.myapp.R;

public class MyAcountSettingActivity extends FragmentActivity {

    FragmentManager fragmentManager;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_acount_setting);
        fragmentManager=getSupportFragmentManager();

        fragment=new MyAcountSettingFragment();
        fragmentManager.beginTransaction().replace(R.id.contianner_mysetting,fragment).commit();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        if (BaseFragment.selectedFragment instanceof MyAcountSettingFragment){
            super.onBackPressed();
        }else {
            MyAcountSettingFragment myAcountSettingFragment=new MyAcountSettingFragment();
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_form_left,R.anim.out_to_right);
            transaction.replace(R.id.contianner_mysetting,myAcountSettingFragment).commit();
        }

    }

}
