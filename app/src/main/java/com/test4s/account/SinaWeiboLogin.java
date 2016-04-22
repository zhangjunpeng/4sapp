package com.test4s.account;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.app.tools.MyLog;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.test4s.net.BaseParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/4/13.
 */
public class SinaWeiboLogin implements WeiboAuthListener{
    private static SinaWeiboLogin instance;

    public static final String SCOPE =                               // 应用申请的高级权限
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";
    private AuthInfo mAuthInfo;

    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private Oauth2AccessToken mAccessToken;

    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    public SsoHandler mSsoHandler;
    public ThirdLoginActivity activity;

    private SinaWeiboLogin(Activity context) {
        // 创建微博实例
        //mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        // 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
        mAuthInfo = new AuthInfo(context, "963258147", "https://api.weibo.com/oauth2/default.html",SCOPE);
        mSsoHandler = new SsoHandler(context, mAuthInfo);
        activity= (ThirdLoginActivity) context;
    }

    public static SinaWeiboLogin getInstance(Activity ac){
        if (instance==null){
            instance=new SinaWeiboLogin(ac);
        }
        return instance;
    }

    public void login(){
        MyLog.i("微博登录");
        mSsoHandler.authorize(this);
    }

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
            activity.sendToServer("sina",mAccessToken.getUid(),info,null);
        } else {
            // 以下几种情况，您会收到 Code：
            // 1. 当您未在平台上注册的应用程序的包名与签名时；
            // 2. 当您注册的应用程序包名与签名不正确时；
            // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
            String code = values.getString("code");
            if (!TextUtils.isEmpty(code)) {
                MyLog.i("weibo ");
            }
        }
    }

    @Override
    public void onWeiboException(WeiboException e) {

    }

    @Override
    public void onCancel() {

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

        }
        @Override
        public void onCancel() {
        }
        @Override
        public void onWeiboException(WeiboException e) {
        }
    }

    public void loginout(){
}


}
