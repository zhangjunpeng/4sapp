package com.test4s.myapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.view.Introduce.IntroduceActivity;

import java.util.concurrent.Executors;

import cn.jpush.android.api.JPushInterface;


public class FirsActivity extends AppCompatActivity {

    private static final String SP_NAME = "4stest";

    private boolean isFirstin=false;
    private SharedPreferences sharedPreferences;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Intent intent=new Intent(FirsActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };
    @Override
    protected void onResume() {
        JPushInterface.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        JPushInterface.onPause(this);
        super.onPause();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences= MyApplication.mcontext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        isFirstin=sharedPreferences.getBoolean("isFirstin",true);
        if (isFirstin){
            Intent intent=new Intent(this, IntroduceActivity.class);
            startActivity(intent);
            finish();
        }else {
            setContentView(R.layout.activity_firs);
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(0);
                }
            });
        }

    }

}
