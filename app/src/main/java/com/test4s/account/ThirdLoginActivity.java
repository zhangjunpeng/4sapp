package com.test4s.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.app.tools.MyLog;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.test4s.myapp.R;
import com.test4s.myapp.wxapi.WXEntryActivity;
import com.test4s.net.BaseParams;
import com.view.Evaluation.EvaluationActivity;
import com.view.messagecenter.MessageList;
import com.view.myattention.AttentionActivity;
import com.view.myreport.ReprotListActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.concurrent.Executors;

public class ThirdLoginActivity extends Activity implements IUiListener{

    static String third="";
    private SinaWeiboLogin sinaWeiboLogin;
    private MyAccount myAccount;
    public final static int BIND_PHONE=20009;
    private String qq_openid;
    private String qq_token;
    private String qq_expir;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:

                    break;
            }

        }
    };

    private static Oauth2AccessToken mAccessToken;

    private AuthListener listener;
    private ImageView image;
    private SsoHandler mSsoHandler;
    private AuthInfo mAuthInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_login);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        third=getIntent().getStringExtra("third");
        myAccount=MyAccount.getInstance();
        image= (ImageView) findViewById(R.id.image_third);
        AnimationDrawable drawable= (AnimationDrawable) image.getBackground();
        drawable.start();
        setFinishOnTouchOutside(false);

        MyLog.i(getLocalClassName()+" call");

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    starLogin();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
      initListener();
    }
    IUiListener iulistener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            MyLog.i("qq back=="+o.toString());
            String res=o.toString();
            try {
                JSONObject jsob=new JSONObject(res);
                qq_openid=jsob.getString("openid");
                qq_token=jsob.getString("access_token");
                qq_expir=jsob.getString("expires_in");

                send("qq",qq_openid,null,null);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            MyLog.i("qq login error::"+uiError.errorDetail);
        }

        @Override
        public void onCancel() {
            MyLog.i("qq login onCancel");
            finish();
        }


    };
    private void initListener() {
        listener=new AuthListener();
    }

    private void starLogin() {
        switch (third){
            case "qq":
                MyLog.i("qq登录1");



                TencentLogin tencentLogin=TencentLogin.getIntance(this,iulistener);
                MyAccount.tencentLogin=tencentLogin;
                MyLog.i("qq登录2");

                tencentLogin.login();
                break;
            case "weixin":
                MyLog.i("微信登录");

                boolean islogin=WeiXinLogin.getInstance(this).login();
                if (islogin){
                    setResult(WeiXinLogin.LOGIN_TRUE);
                }else {
                    setResult(WeiXinLogin.LOGIN_FALSE);
                }
                finish();
                break;
            case "sina":
                mAuthInfo = new AuthInfo(this, "963258147", "https://api.weibo.com/oauth2/default.html",SinaWeiboLogin.SCOPE);
                mSsoHandler = new SsoHandler(this, mAuthInfo);
                sinalogin();
                sinaWeiboLogin=SinaWeiboLogin.getInstance(this);
                sinaWeiboLogin.login(listener);
                break;
        }
    }

    private void sinalogin() {
        mSsoHandler.authorize(new AuthListener());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        MyLog.i("third::"+third);
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==BIND_PHONE && resultCode==Activity.RESULT_OK){
            setResult(Activity.RESULT_OK);
            finish();
        }else {
            switch (third){
                case "sina":
                    MyLog.i("third activity login onActivityResult:sina");
                    if (mSsoHandler != null) {
                        mSsoHandler .authorizeCallBack(requestCode, resultCode, data);
                    }
                    break;
                case "qq":
                    MyLog.i("third activity login onActivityResult:qq");

                    Tencent.onActivityResultData(requestCode,resultCode,data,this);
                    break;
            }
        }
//        finish();

    }
    private void thirdLoginBind(String regtype, String openid, String info) {
        MyLog.i("绑定手机号1");
        Intent intent=new Intent(this, AccountActivity.class);
        intent.putExtra("third",regtype);
        intent.putExtra("uniqueid",openid);
        intent.putExtra("info",info);
        startActivityForResult(intent,BIND_PHONE);
        overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
        MyLog.i("绑定手机号2");
    }
    public void send(final String type, final String uid, final String info, String token) {
        BaseParams baseParams=new BaseParams("user/thirdlogin");
        baseParams.addParams("logintype",type);
        baseParams.addParams("uniqueid",uid);
        if (!TextUtils.isEmpty(info)) {
            baseParams.addParams("otherinfo",info);

        }
        if (!TextUtils.isEmpty(token)) {
            baseParams.addParams("token", token);
        }
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("send uid back=="+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean login=jsonObject.getBoolean("success");
                    if (login){

                        boolean firstlogin=false;
                        if (result.contains("uniqueid")) {
                            firstlogin = true;
                        }

                        if (firstlogin){
                          getUserInfo(type,uid,info);

                        }else {
                            MyLog.i("第三方登录成功2");
                            JSONObject jsonObject1=jsonObject.getJSONObject("data");

                            myAccount.setNickname(jsonObject1.getString("nickname"));
                            myAccount.setUsername(jsonObject1.getString("username"));
                            myAccount.setToken(jsonObject1.getString("token"));
                            myAccount.setAvatar(jsonObject1.getString("avatar"));
                            CusToast.showToast(ThirdLoginActivity.this,"登录成功",Toast.LENGTH_SHORT);
                            myAccount.saveUserInfo();
                            MyLog.i("第三方登录成功1");
                            loginSuccess();
                        }

                    }else {
                        String mes=jsonObject.getString("msg");
//                        Toast.makeText(AccountActivity.this,mes,Toast.LENGTH_SHORT).show();
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

    private void getUserInfo(String type,String uid, String info) {
        switch (type){
            case "qq":
                getQQUserInfo(type);
                break;
            case "sina":
                thirdLoginBind(type,uid,info);
                break;
            case "weixin":
                thirdLoginBind(type,uid,info);
                break;
        }
    }

    private void getQQUserInfo(final String logintype) {

        TencentLogin tencentLogin=TencentLogin.getIntance(this,this);
        tencentLogin.mtencent.setOpenId(qq_openid);
        tencentLogin.mtencent.setAccessToken(qq_token,qq_expir);
        tencentLogin.getUserInfo(this,new IUiListener() {
            @Override
            public void onComplete(Object o) {
                MyLog.i("qq UserInfo==="+o.toString());
                try {
                    JSONObject userinfo=new JSONObject(o.toString());
                    String nick=userinfo.getString("nickname");
                    String head=userinfo.getString("figureurl_qq_2");
                    if (TextUtils.isEmpty(head)){
                        head=userinfo.getString("figureurl_qq_1");
                    }
                    JSONObject info=new JSONObject();
                    info.put("type","QQ");
                    info.put("name",nick);
                    info.put("nick",nick);
                    info.put("head",head);
                    thirdLoginBind(logintype,qq_openid,info.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void loginSuccess() {
        myAccount=MyAccount.getInstance();
        myAccount.isLogin=true;
        MyLog.i("登录成功1==");
//        switch (third){
//            case "sina":
//                break;
//            case "qq":
//                break;
//            case "weixin":
//

//                break;
//        }

        MyLog.i("登录成功：："+myAccount.toString());
        setResult(Activity.RESULT_OK);
        MyLog.i("登录成功2");
        finish();
    }

    @Override
    public void onComplete(Object o) {
        MyLog.i("qq back=="+o.toString());
        String res=o.toString();
        try {
            JSONObject jsob=new JSONObject(res);
            qq_openid=jsob.getString("openid");
            qq_token=jsob.getString("access_token");
            qq_expir=jsob.getString("expires_in");

            send("qq",qq_openid,null,null);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(UiError uiError) {
        MyLog.i("qq back UiError=="+uiError.toString());
    }

    @Override
    public void onCancel() {
        MyLog.i("qq cancel login");
    }

    public void getWXUserInfo(String token,String oid){
        RequestParams params=new RequestParams("https://api.weixin.qq.com/sns/userinfo");
        params.addBodyParameter("access_token",token);
        params.addBodyParameter("openid",oid);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("WX UserInfo=="+result);
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
    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     * 该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            MyLog.i("vaulues=="+values.toString());
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            //从这里获取用户输入的 电话号码信息
            String phoneNum = mAccessToken.getPhoneNum();
            if (mAccessToken.isSessionValid()) {
                // 显示 Token
//                updateTokenView(false);
                MyLog.i("weibo toke ==" + mAccessToken);
                String info="";
                JSONObject jsonObject=new JSONObject();

                String nickname=values.getString("userName","");
                if (!TextUtils.isEmpty(nickname)){
                    try {
                        jsonObject.put("type","SINA");
                        jsonObject.put("nick",nickname);
                        jsonObject.put("name",nickname);
                        jsonObject.put("head","");
                        info=jsonObject.toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                send("sina",mAccessToken.getUid(),info,null);
            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                if (!TextUtils.isEmpty(code)) {
                    MyLog.i("weibo "+code);
                }
            }
        }
        @Override
        public void onCancel() {
        }
        @Override
        public void onWeiboException(WeiboException e) {
        }
    }
    public static void WeiBologinout(Context context){
        mAccessToken=new Oauth2AccessToken();
        AccessTokenKeeper.clear(context);
    }


}
