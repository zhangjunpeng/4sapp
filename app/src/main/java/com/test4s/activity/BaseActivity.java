package com.test4s.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;


import com.app.tools.MyLog;

/**
 * Created by Administrator on 2015/12/1.
 */
public class BaseActivity  extends Activity{



    MyBroadcastReceiver myBroadcastReceiver=new MyBroadcastReceiver(getClass());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(getPackageName()+"."+getLocalClassName());


        registerReceiver(myBroadcastReceiver, intentFilter);
        

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }
    class  MyBroadcastReceiver extends BroadcastReceiver {



        Class<?> activityClass;
        public MyBroadcastReceiver(Class<?> cls){
            activityClass=cls;
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            Intent intent1=new Intent(context,activityClass);
            context.startActivity(intent1);
        }
    }
}
