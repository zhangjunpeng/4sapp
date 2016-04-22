package com.test4s.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.MyLog;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.LoginParams;
import com.view.Evaluation.EvaluationActivity;
import com.view.accountsetting.BaseFragment;
import com.view.messagecenter.MessageList;
import com.view.myattention.AttentionActivity;
import com.view.myreport.ReprotListActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

/**
 * Created by Administrator on 2016/1/27.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener{


    private Button login;
    private TextView reg;
    private EditText editText_name;
    private EditText editText_pwd;
    private TextView findpwd;
    FragmentTransaction transition;

    private ImageView back;
    private TextView title;
    private TextView save;
    View view;

    private LinearLayout warning;
    private TextView warningtext;

    String tag="login";

    MyAccount myAccount;
    SinaWeiboLogin sinaWeiboLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_login,null);
        login= (Button) view.findViewById(R.id.loginButton_login);
        reg= (TextView) view.findViewById(R.id.reg_login);
        editText_name= (EditText)view.findViewById(R.id.name_login);
        editText_pwd= (EditText) view.findViewById(R.id.pwd_login);
        findpwd= (TextView) view.findViewById(R.id.findpwd_login);

        back= (ImageView) view.findViewById(R.id.back_savebar);
        title= (TextView) view.findViewById(R.id.textView_titlebar_save);
        save= (TextView) view.findViewById(R.id.save_savebar);

        warning= (LinearLayout) view.findViewById(R.id.warning_login);
        warningtext= (TextView) view.findViewById(R.id.waringtext_login);

        save.setVisibility(View.INVISIBLE);
        title.setText("登录");

        login.setClickable(false);

        myAccount=MyAccount.getInstance();

        Bundle bundle=getArguments();
        if (bundle!=null){
            tag=getArguments().getString("tag","login");
        }


        setImmerseLayout(view.findViewById(R.id.titlebar_login));

        transition= getActivity().getSupportFragmentManager().beginTransaction();
        initListener();
        return view;
    }

    private void initListener() {
        back.setOnClickListener(this);
        login.setOnClickListener(this);
        reg.setOnClickListener(this);
        findpwd.setOnClickListener(this);

        view.findViewById(R.id.weichat_login).setOnClickListener(this);
        view.findViewById(R.id.weibo_login).setOnClickListener(this);
        view.findViewById(R.id.qq_login).setOnClickListener(this);

        editText_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                MyLog.i("beforeTextChanged cout after"+count+"```"+after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                    changeButton();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changeButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void changeButton() {
        if (editText_name.getText().toString().length()>0&&editText_pwd.getText().toString().length()>=6){
            login.setClickable(true);
            login.setBackgroundResource(R.drawable.border_button_orange);
        }else {
            login.setClickable(false);
            login.setBackgroundResource(R.drawable.border_button_gray);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MyLog.i("fragment回调");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton_login:
                login();
                break;
            case R.id.reg_login:
                RegisterFragment registerFragment=new RegisterFragment();
                transition.setCustomAnimations(R.anim.in_from_right,R.anim.out_to_left);
                transition.replace(R.id.contianer_loginActivity,registerFragment).commit();
                break;
            case R.id.findpwd_login:
                FindPwdFragment findPwdFragment=new FindPwdFragment();
                transition.setCustomAnimations(R.anim.in_from_right,R.anim.out_to_left);
                transition.replace(R.id.contianer_loginActivity,findPwdFragment).commit();

                break;
            case R.id.back_savebar:
                getActivity().finish();
                break;
            case R.id.weichat_login:
                Intent intent1=new Intent(getActivity(),ThirdLoginActivity.class);
                intent1.putExtra("third","weixin");
                getActivity().startActivity(intent1);
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
                break;
            case R.id.weibo_login:
                Intent intent2=new Intent(getActivity(),ThirdLoginActivity.class);
                intent2.putExtra("third","sina");
                getActivity().startActivityForResult(intent2,AccountActivity.THIRD_LOGIN);
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
                break;
            case R.id.qq_login:
                Intent intent3=new Intent(getActivity(),ThirdLoginActivity.class);
                intent3.putExtra("third","qq");
                getActivity().startActivityForResult(intent3,AccountActivity.THIRD_LOGIN);
                break;
        }
    }
    private void login() {
        String name=editText_name.getText().toString();
        String pwd=editText_pwd.getText().toString();
        if (TextUtils.isEmpty(name)||TextUtils.isEmpty(pwd)){
            return;
        }
        BaseParams loginParams=new BaseParams("user/login");
        loginParams.addParams("username",name);
        loginParams.addParams("password",pwd);
        loginParams.addSign();
        x.http().post(loginParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("login_back:::"+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean login=jsonObject.getBoolean("success");
                    if (login){
                        JSONObject jsonObject1=jsonObject.getJSONObject("data");

                        myAccount.setNickname(jsonObject1.getString("nickname"));
                        myAccount.setUsername(jsonObject1.getString("username"));
                        myAccount.setToken(jsonObject1.getString("token"));
                        myAccount.setAvatar(jsonObject1.getString("avatar"));
                        Toast.makeText(getActivity(),"登录成功",Toast.LENGTH_SHORT).show();
                        myAccount.saveUserInfo();
                        loginSuccess();

                    }else {
                        String mes=jsonObject.getString("msg");
                        showwarning(mes);
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

    private void loginSuccess() {
        myAccount.isLogin=true;
        Intent intent=null;
        switch (tag){
            case "login":

                break;
            case "messagecenter":
                intent= new Intent(getActivity(), MessageList.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                break;
            case "pc":
                intent= new Intent(getActivity(), EvaluationActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                break;
            case "attention":
                intent= new Intent(getActivity(), AttentionActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                break;
            case "report":
                intent= new Intent(getActivity(), ReprotListActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                break;
        }

        MyLog.i("登录成功：："+myAccount.toString());
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    public void showwarning(String mes){
        warning.setVisibility(View.VISIBLE);
    }
}
