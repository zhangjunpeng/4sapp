package com.test4s.myapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.app.tools.MyLog;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.Introduce.IntroduceActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

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
        getKey();

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

    private void getKey() {
        MyLog.i("getkey");

        BaseParams baseparam=new BaseParams("api/getkey");
        x.http().post(baseparam.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("getkey back=="+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean su=jsonObject.getBoolean("success");
                    int code=jsonObject.getInt("code");
                    if (su&&code==200){
                        JSONObject data=jsonObject.getJSONObject("data");
                        Url.key=data.getString("md5key");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
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
