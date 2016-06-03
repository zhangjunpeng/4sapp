package com.test4s.account;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

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
    private Fragment mFragment;
    private Activity mActivity;
    public Tencent mtencent;
    public String token;
    private IUiListener listener;
    private TencentLogin(Activity activity,IUiListener listener){
        this.listener=listener;
        mActivity=activity;
        mtencent=Tencent.createInstance("1105244367",activity.getApplicationContext());
    }
    private TencentLogin(Fragment fragment,IUiListener listener){
        this.listener=listener;
        mFragment=fragment;
        mtencent=Tencent.createInstance("1105244367",fragment.getActivity().getApplicationContext());
    }
    public static TencentLogin getIntance(Activity activity,IUiListener listener){
        if (intance==null){
            intance=new TencentLogin(activity,listener);
        }
        return intance;
    }
    public static TencentLogin getIntance(Fragment fragment, IUiListener listener){
        if (intance==null){
            intance=new TencentLogin(fragment,listener);
        }
        return intance;
    }

    public void login(){
        if (!mtencent.isSessionValid())
        {
            if (mActivity==null){
                mtencent.login(mFragment, "all", listener);
            }else if (mFragment==null){
                mtencent.login(mActivity, "all", listener);
            }
        }
    }


    public void getUserInfo(Context context,IUiListener listener){
        com.tencent.connect.UserInfo userInfo=new UserInfo(context,mtencent.getQQToken());
        userInfo.getUserInfo(listener);
    }
    public void loginOut(){
        if (mtencent!=null){
            mtencent.logout(mActivity);
        }
    }
}
