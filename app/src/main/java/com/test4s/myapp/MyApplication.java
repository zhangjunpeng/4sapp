package com.test4s.myapp;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.app.tools.MyLog;
import com.test4s.account.MyAccount;
import com.test4s.gdb.DaoMaster;
import com.test4s.gdb.DaoSession;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2015/12/11.
 */
public class MyApplication extends Application {
    public static SQLiteDatabase db;
    public static DaoMaster daoMaster;
    public static DaoSession daoSession;

    public static Context mcontext;

    public static boolean DeBug=true;

    //参数
    public static String imei;
    public static String packageName;
    public static String versionName;

    @Override
    public void onCreate() {
        super.onCreate();
        mcontext=getApplicationContext();
        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);

        imei=tm.getDeviceId();
        packageName=mcontext.getPackageName();

        PackageInfo packageInfo= null;
        try {
            packageInfo = mcontext.getPackageManager().getPackageInfo(mcontext.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionName=packageInfo.versionName;


        //数据库开启
        DaoMaster.DevOpenHelper devOpenHelper=new DaoMaster.DevOpenHelper(this,"Test4s",null);
        db=devOpenHelper.getWritableDatabase();
        daoMaster=new DaoMaster(db);
        daoSession=daoMaster.newSession();

        //XUtil初始化
        x.Ext.init(this);
        x.Ext.setDebug(true);



        //获取账号
        MyAccount myAccount=MyAccount.getInstance();
        if (!TextUtils.isEmpty(myAccount.getToken())){
            myAccount.toString();
            MyAccount.isLogin=true;
        }else {
            MyLog.i("账号数据为空");
        }
        //极光推送
//        JPushInterface.setDebugMode(true);
//        JPushInterface.init(getApplicationContext());

    }

    public static boolean checkUpdate(){
        //检查更新
        RequestParams upParams=new RequestParams("");

        x.http().post(upParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

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

        return false;
    }

}
