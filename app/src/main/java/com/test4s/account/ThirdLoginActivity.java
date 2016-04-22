package com.test4s.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.app.tools.MyLog;
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

public class ThirdLoginActivity extends Activity implements IUiListener{

    static String third="";
    private SinaWeiboLogin sinaWeiboLogin;
    private MyAccount myAccount;
    public final static int BIND_PHONE=20009;
    private String qq_openid;
    private String qq_token;
    private String qq_expir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_login);
        third=getIntent().getStringExtra("third");
        myAccount=MyAccount.getInstance();
        starLogin();
    }

    private void starLogin() {
        switch (third){
            case "qq":
                TencentLogin tencentLogin=TencentLogin.getIntance(this);
                tencentLogin.login();
                break;
            case "weixin":
                MyLog.i("微信登录");
                WeiXinLogin.getInstance(this).login();
                setResult(Activity.RESULT_OK);
                finish();
                break;
            case "sina":
                sinaWeiboLogin=SinaWeiboLogin.getInstance(this);
                sinaWeiboLogin.login();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==BIND_PHONE && resultCode==Activity.RESULT_OK){
            setResult(Activity.RESULT_OK);
            finish();
        }
        switch (third){
            case "sina":
                MyLog.i("third activity login onActivityResult:sina");
                if (SinaWeiboLogin.getInstance(this).mSsoHandler != null) {
                    SinaWeiboLogin.getInstance(this).mSsoHandler .authorizeCallBack(requestCode, resultCode, data);
                }
                break;
            case "qq":
                MyLog.i("third activity login onActivityResult:qq");
                Tencent.onActivityResultData(requestCode,resultCode,data,this);
                break;
        }
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
    public void sendToServer(final String type, final String uid, final String info, String token) {
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
                            Toast.makeText(ThirdLoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
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

        TencentLogin tencentLogin=TencentLogin.getIntance(this);
        tencentLogin.mtencent.setOpenId(qq_openid);
        tencentLogin.mtencent.setAccessToken(qq_token,qq_expir);
        tencentLogin.getUserInfo(new IUiListener() {
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

            sendToServer("qq",qq_openid,null,null);

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
        MyLog.i("cancel login");
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

}
