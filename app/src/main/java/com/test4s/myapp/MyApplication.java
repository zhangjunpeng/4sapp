package com.test4s.myapp;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.TelephonyManager;

import com.test4s.gdb.DaoMaster;
import com.test4s.gdb.DaoSession;

import org.xutils.x;

/**
 * Created by Administrator on 2015/12/11.
 */
public class MyApplication extends Application {
    public static SQLiteDatabase db;
    public static DaoMaster daoMaster;
    public static DaoSession daoSession;

    public static Context mcontext;

    //参数
    public static String imei;
    public static String packageName;

    @Override
    public void onCreate() {
        super.onCreate();
        mcontext=getApplicationContext();
        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);

        imei=tm.getDeviceId();
        packageName=mcontext.getPackageName();


        DaoMaster.DevOpenHelper devOpenHelper=new DaoMaster.DevOpenHelper(this,"Test4s",null);
        db=devOpenHelper.getWritableDatabase();
        daoMaster=new DaoMaster(db);
        daoSession=daoMaster.newSession();

        x.Ext.init(this);
        x.Ext.setDebug(true);

    }
}
