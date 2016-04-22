package com.test4s.account;

import android.app.Activity;
import android.content.Context;

import com.app.tools.MyLog;
import com.tencent.connect.*;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * Created by Administrator on 2016/4/13.
 */
public class TencentLogin {
    private static TencentLogin intance;
    private ThirdLoginActivity mcontext;
    public Tencent mtencent;
    public String token;
    private TencentLogin(ThirdLoginActivity context){
        mcontext=context;
        mtencent=Tencent.createInstance("1105244367",context.getApplicationContext());
    }
    public static TencentLogin getIntance(ThirdLoginActivity activity){
        if (intance==null){
            intance=new TencentLogin(activity);
        }
        return intance;
    }

    public void login(){
        if (!mtencent.isSessionValid())
        {
            mtencent.login(mcontext, "all", mcontext);
        }
    }

    private class BaseUiListener implements IUiListener{

        @Override
        public void onComplete(Object o) {
            MyLog.i("qq userinfo =="+o.toString());
        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    }

    public void getUserInfo(IUiListener listener){
        com.tencent.connect.UserInfo userInfo=new UserInfo(mcontext,mtencent.getQQToken());
        userInfo.getUserInfo(listener);
    }
    public void loginOut(){
        if (mtencent!=null){
            mtencent.logout(mcontext);
        }
    }
}
