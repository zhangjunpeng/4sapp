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
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.test4s.account.MyAccount;
import com.test4s.gdb.DaoMaster;
import com.test4s.gdb.DaoSession;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

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

    private static final String APP_ID="wx53c55fc6f1efaad2";
    public static IWXAPI api;

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
        JPushInterface.setDebugMode(true);
        JPushInterface.init(getApplicationContext());

        //注册到微信
//        regToWx();

        //UIL图片库初始化
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), getPackageResourcePath()+"image/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(mcontext)
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                //.discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null) // Can slow ImageLoader, use it carefully (Better don't use it)/设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheFileCount(100) //缓存的文件数量
                .diskCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(mcontext, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();//开始构建
        ImageLoader.getInstance().init(config);
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
