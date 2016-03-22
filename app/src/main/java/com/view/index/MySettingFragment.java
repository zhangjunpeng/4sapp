package com.view.index;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.MyLog;
import com.app.tools.UploadUtil;
import com.squareup.picasso.Picasso;
import com.test4s.account.MyAccount;
import com.test4s.account.AccountActivity;
import com.test4s.account.UserInfo;
import com.test4s.net.BaseParams;
import com.view.Evaluation.EvaluationActivity;
import com.view.messagecenter.MessageList;
import com.view.accountsetting.MyAcountSettingActivity;
import com.view.activity.SelectPicActivity;
import com.test4s.myapp.R;
import com.view.myattention.AttentionActivity;
import com.view.setting.SettingActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2015/12/7.
 */
public class MySettingFragment extends Fragment implements View.OnClickListener, UploadUtil.OnUploadProcessListener{

    /**
     * 去上传文件
     */
    protected static final int TO_UPLOAD_FILE = 1;
    /**
     * 上传文件响应
     */
    protected static final int UPLOAD_FILE_DONE = 2;  //
    /**
     * 选择文件
     */
    public static final int TO_SELECT_PHOTO = 3;
    /**
     * 上传初始化
     */
    private static final int UPLOAD_INIT_PROCESS = 4;
    /**
     * 上传中
     */
    private static final int UPLOAD_IN_PROCESS = 5;

    private CircleImageView roundedIcon;
    private TextView textView;
    private TextView name2;

    private ImageView reddot;

    private String picPath = null;

    private String requestURL="http://www.4stest.com/upload.php";

    public static final int RequestCode_login=101;
    public static final int RequestCode_setting=102;

    public MyAccount myAccount;

    public boolean network=true;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_mysetting,null);
        roundedIcon= (CircleImageView) view.findViewById(R.id.icon_roundImage);
        roundedIcon.setOnClickListener(this);
        textView= (TextView) view.findViewById(R.id.name_my);
        name2= (TextView) view.findViewById(R.id.name2_my);
        name2.setAlpha((float)0.4);
        view.findViewById(R.id.myAccount_my).setOnClickListener(this);
        view.findViewById(R.id.seting_mysetting).setOnClickListener(this);
        view.findViewById(R.id.messagecenter_my).setOnClickListener(this);
        view.findViewById(R.id.pc_my).setOnClickListener(this);
        view.findViewById(R.id.myattention_mysetting).setOnClickListener(this);

        myAccount=MyAccount.getInstance();

        initView();

        return view;
    }

    @Override
    public void onResume() {
        if (myAccount.getUserInfo()!=null){
            if (!myAccount.getAvatar().equals(myAccount.getUserInfo().getAvatar())){
                myAccount.setAvatar(myAccount.getUserInfo().getAvatar());
                Picasso.with(getActivity())
                        .load(myAccount.getAvatar())
                        .placeholder(R.drawable.default_icon)
                        .into(roundedIcon);
                myAccount.saveUserInfo();
            }
        }

        super.onResume();
    }

    private void initView() {
        //设置界面
        if (MyAccount.isLogin){
            initData();
            initUserInfo();

            name2.setVisibility(View.GONE);
        }else{
            textView.setText("未登录");
            name2.setVisibility(View.VISIBLE);
        }

    }

    private void initData() {

        if (!TextUtils.isEmpty(myAccount.getAvatar())){


            Picasso.with(getActivity().getApplication()).load(myAccount.getAvatar())
                    .placeholder(R.drawable.default_icon)
                    .error(R.drawable.default_icon)
                    .into(roundedIcon);
        }

        if (TextUtils.isEmpty(myAccount.getNickname())){
            String nickname=myAccount.getUsername();
            String subs=nickname.substring(3,7);
            MyLog.i("subs==="+subs);
            nickname=nickname.replace(subs,"*****");
            textView.setText(nickname);
        }else {
            textView.setText(myAccount.getNickname());
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.myAccount_my:
                Intent intent;
                if (MyAccount.isLogin){
                    if (!network){
                        Toast.makeText(getActivity(),"网络连接失败，请检查网络",Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        intent=new Intent(getActivity(), MyAcountSettingActivity.class);
                        startActivityForResult(intent,RequestCode_setting);
                    }

                }else {
                    intent=new Intent(getActivity(), AccountActivity.class);
                    startActivityForResult(intent,RequestCode_login);
                }

                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                break;
            case R.id.icon_roundImage:
//                Dialog dialog=new Dialog(getActivity());
//                dialog.setContentView(R.layout.dialog_mysetting);
//                Intent intent1=new Intent(getActivity(), SelectPicActivity.class);
//                startActivityForResult(intent1, TO_SELECT_PHOTO);

                break;
            case R.id.seting_mysetting:
//                Toast.makeText(getActivity(),"退出登录",Toast.LENGTH_SHORT).show();
//                myAccount.loginOut();
//                initView();
                Intent intent3 = new Intent(getActivity(), SettingActivity.class);
                startActivityForResult(intent3,RequestCode_setting);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                break;
            case R.id.messagecenter_my:
                if (MyAccount.isLogin) {
                    Intent intent2 = new Intent(getActivity(), MessageList.class);
                    startActivity(intent2);
                    getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                }else {
                    //未登录
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.pc_my:
                if (MyAccount.isLogin) {
                    Intent intent2 = new Intent(getActivity(), EvaluationActivity.class);
                    startActivity(intent2);
                    getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                }else {
                    //未登录
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.myattention_mysetting:
                if (MyAccount.isLogin) {
                    Intent intent1 = new Intent(getActivity(), AttentionActivity.class);
                    startActivity(intent1);
                    getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                }else {
                    //未登录
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        MyLog.i("onActivityResult");
        if(resultCode==Activity.RESULT_OK && requestCode == TO_SELECT_PHOTO)
        {
            picPath = data.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
            MyLog.i( "最终选择的图片="+picPath);
            Bitmap bm = BitmapFactory.decodeFile(picPath);
            roundedIcon.setImageBitmap(bm);
            if(picPath!=null)
            {
                handler.sendEmptyMessage(TO_UPLOAD_FILE);
            }else{
                Toast.makeText(getActivity(), "上传的文件路径出错", Toast.LENGTH_LONG).show();
            }
        }
        if (resultCode==Activity.RESULT_OK&&requestCode==RequestCode_login){
            //登录成功返回
            MyLog.i("登录成功~~~~~onActivityResult");
            Toast.makeText(getActivity(),"登录成功",Toast.LENGTH_SHORT).show();
            initView();
        }
        if (resultCode==Activity.RESULT_OK&&requestCode==RequestCode_setting){
            initView();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void toUploadFile()
    {


        String fileKey = "pic";

//        UploadUtil uploadUtil = UploadUtil.getInstance();;
//        uploadUtil.setOnUploadProcessListener(this);  //设置监听器监听上传状态
//
//
//
//        uploadUtil.uploadFile( picPath,fileKey, requestURL,params);

        RequestParams requestParams=new RequestParams(requestURL);

        requestParams.setMultipart(true);
        requestParams.addBodyParameter("filedata",new File(picPath),null);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("updataImage==="+result);
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

    @Override
    public void onUploadDone(int responseCode, String message) {

    }

    @Override
    public void onUploadProcess(int uploadSize) {
        Message msg = Message.obtain();
        msg.what = UPLOAD_IN_PROCESS;
        msg.arg1 = uploadSize;
        handler.sendMessage(msg );
    }

    @Override
    public void initUpload(int fileSize) {
        Message msg = Message.obtain();
        msg.what = UPLOAD_INIT_PROCESS;
        msg.arg1 = fileSize;
        handler.sendMessage(msg );
    }

    private void initUserInfo() {
        BaseParams baseParams=new BaseParams("user/index");
        baseParams.addParams("token", MyAccount.getInstance().getToken());
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("UserInfo==="+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean su=jsonObject.getBoolean("success");
                    int code=jsonObject.getInt("code");
                    if (su&&code==200){
                        UserInfo userInfo=new UserInfo();
                        JSONObject jsonObject2=jsonObject.getJSONObject("data");
                        JSONObject jsonObject3=jsonObject2.getJSONObject("userInfo");
                        userInfo.setId(jsonObject3.getString("id"));
                        userInfo.setUsername(jsonObject3.getString("username"));
                        userInfo.setNickname(jsonObject3.getString("nickname"));
                        userInfo.setEmail(jsonObject3.getString("email"));
                        userInfo.setPhone(jsonObject3.getString("phone"));
                        userInfo.setAvatar(jsonObject3.getString("avatar"));
                        userInfo.setUser_identity(jsonObject3.getString("user_identity"));
                        userInfo.setGame_like(jsonObject3.getString("game_like"));
                        userInfo.setJob_id(jsonObject3.getString("job_id"));
                        userInfo.setEdu_id(jsonObject3.getString("edu_id"));
                        userInfo.setProvince(jsonObject3.getString("province"));
                        userInfo.setCity(jsonObject3.getString("city"));
                        userInfo.setCounty(jsonObject3.getString("county"));
                        userInfo.setAddr(jsonObject3.getString("addr"));
                        userInfo.setProvince_name(jsonObject3.getString("province_name"));
                        userInfo.setCity_name(jsonObject3.getString("city_name"));
                        userInfo.setCounty_name(jsonObject3.getString("county_name"));
                        userInfo.setEdu_name(jsonObject3.getString("edu_name"));
                        userInfo.setJob_name(jsonObject3.getString("job_name"));

                        MyAccount.getInstance().setUserInfo(userInfo);
                        network=true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyLog.i("UserInfo==="+ex.toString());
                network=false;
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TO_UPLOAD_FILE:
                    toUploadFile();
                    break;

                case UPLOAD_INIT_PROCESS:

                    break;
                case UPLOAD_IN_PROCESS:

                    break;
                case UPLOAD_FILE_DONE:
                    String result = "响应码："+msg.arg1+"\n响应信息："+msg.obj+"\n耗时："+UploadUtil.getRequestTime()+"秒";
                    Toast.makeText(getActivity(),result,Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };
}
