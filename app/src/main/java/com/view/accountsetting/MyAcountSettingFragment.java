package com.view.accountsetting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.MyLog;
import com.app.tools.UploadUtil;
import com.squareup.picasso.Picasso;
import com.test4s.account.MyAccount;
import com.test4s.account.UserInfo;
import com.test4s.myapp.MyApplication;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.activity.SelectPicActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.TreeMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/2/22.
 */
public class MyAcountSettingFragment  extends BaseFragment implements View.OnClickListener,UploadUtil.OnUploadProcessListener{

    FragmentManager manager;
    FragmentTransaction transaction;
    UserInfo userInfo;

    View view;

    private CircleImageView icon;
    private TextView name;
    private TextView cp;
    private TextView tz;
    private TextView fx;
    private TextView wb;

    private RelativeLayout setnick;
    private RelativeLayout setad;
    private RelativeLayout setedu;
    private RelativeLayout setjob;
    private RelativeLayout setlovegame;
    private RelativeLayout bindphone;
    private RelativeLayout bindEmail;
    private RelativeLayout bindother;
    private RelativeLayout changepwd;

    private TextView nick_name;
    private TextView adress;
    private TextView edu;
    private TextView job;
    private TextView gamelove;
    private TextView phone;
    private TextView email;
    private TextView bind_other;

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

    private String picPath = null;

    private String requestURL="http://www.4stest.com/upload.php";

    public static final int RequestCode_login=101;
    public static final int RequestCode_setting=102;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_myacountsetting,null);

        setImmerseLayout(view.findViewById(R.id.namebg_myacc));

        manager=getFragmentManager();
        transaction=manager.beginTransaction();
        userInfo=MyAccount.getInstance().getUserInfo();
        initView();
        initData();



        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(getActivity(), SelectPicActivity.class);
                startActivityForResult(intent1, TO_SELECT_PHOTO);
            }
        });


        setnick.setOnClickListener(this);
        setad.setOnClickListener(this);
        setedu.setOnClickListener(this);
        setjob.setOnClickListener(this);
        setlovegame.setOnClickListener(this);
        bindphone.setOnClickListener(this);
        bindEmail.setOnClickListener(this);
        bindother.setOnClickListener(this);

        changepwd.setOnClickListener(this);

        return view;
    }



    private void initData() {
//        x.image().bind(icon, Url.prePic+userInfo.getAvatar());
//        String identuty=userInfo.getUser_identity();
        //身份的显示与隐藏

        if (userInfo==null){
            return;
        }
        if (TextUtils.isEmpty(userInfo.getNickname())){
            String nickname=userInfo.getUsername();
            String subs=nickname.substring(3,7);
            MyLog.i("subs==="+subs);
            nickname=nickname.replace(subs,"*****");
            name.setText(nickname);
            nick_name.setText(nickname);
        }else {
            name.setText(userInfo.getNickname());
            nick_name.setText(userInfo.getNickname());
        }

        if (!userInfo.getProvince_name().equals("null")){
            adress.setText(userInfo.getProvince_name()+" "+userInfo.getCity_name());
        }

        if (!userInfo.getEdu_name().equals("null")){
            edu.setText(userInfo.getEdu_name());
        }
        if (!userInfo.getJob_name().equals("null")){
            job.setText(userInfo.getJob_name());
        }
        if (!userInfo.getGame_like().equals("null")){

            gamelove.setText(userInfo.getGame_like());

        }

        if (!TextUtils.isEmpty(userInfo.getPhone())){
            String phoneNum=userInfo.getPhone();
            String mes=phoneNum.substring(3,7);
            phoneNum=phoneNum.replace(mes,"****");
            phone.setText(phoneNum);
        }

        if (!TextUtils.isEmpty(userInfo.getEmail())){
            email.setText(userInfo.getEmail());
        }
        if (!TextUtils.isEmpty(userInfo.getAvatar())){
            Picasso.with(getActivity())
                    .load(userInfo.getAvatar())
                    .placeholder(R.drawable.default_icon)
                    .into(icon);
        }


    }

    private void initView() {
        icon= (CircleImageView) view.findViewById(R.id.roundImage_myaccountsetting);
        name= (TextView) view.findViewById(R.id.name_myaccountsettting);
        cp= (TextView) view.findViewById(R.id.cp_myaccountysetting);
        tz= (TextView) view.findViewById(R.id.tz_myaccountysetting);
        fx= (TextView) view.findViewById(R.id.fx_myaccountysetting);
        wb= (TextView) view.findViewById(R.id.wb_myaccountysetting);
        setnick= (RelativeLayout) view.findViewById(R.id.setnick_myacset_fg);
        setad= (RelativeLayout) view.findViewById(R.id.setad_myacset_fg);
        setedu= (RelativeLayout) view.findViewById(R.id.setedu_myacset_fg);
        setjob= (RelativeLayout) view.findViewById(R.id.setjob_myacset_fg);
        setlovegame= (RelativeLayout) view.findViewById(R.id.setgamelove_myacset_fg);
        bindphone= (RelativeLayout) view.findViewById(R.id.bindphone_myset_fg);
        bindEmail= (RelativeLayout) view.findViewById(R.id.bindemail_myset_fg);

        bindother= (RelativeLayout) view.findViewById(R.id.bindother_myset_fg);
        changepwd= (RelativeLayout) view.findViewById(R.id.changepwd_myset_fg);

        nick_name= (TextView) view.findViewById(R.id.nickname_setting_myaccountsettting);
        adress= (TextView) view.findViewById(R.id.address_setting_myaccountsettting);
        edu= (TextView) view.findViewById(R.id.xl_setting_myaccountsettting);
        job= (TextView) view.findViewById(R.id.zy_setting_myaccountsettting);
        gamelove= (TextView) view.findViewById(R.id.like_setting_myaccountsettting);
        phone= (TextView) view.findViewById(R.id.phone_setting_myaccountsettting);
        email= (TextView) view.findViewById(R.id.email_setting_myaccountsettting);
        bind_other= (TextView) view.findViewById(R.id.bindother_setting_myaccountsettting);


    }

    @Override
    public void onClick(View v) {
        Fragment fragment=null;
        switch (v.getId()){
            case R.id.setnick_myacset_fg:
                fragment=new SetNickFragment();
                break;
            case R.id.setad_myacset_fg:
                fragment=new SetAddressFragment();
                break;
            case R.id.setedu_myacset_fg:
                fragment=new SetEduFragment();
                break;
            case R.id.setjob_myacset_fg:
                fragment=new SetJobFragment();
                break;
            case R.id.setgamelove_myacset_fg:
                fragment=new SetGameLoveFragment();
                break;
            case R.id.bindphone_myset_fg:
                if (TextUtils.isEmpty(userInfo.getPhone())){
                    fragment=new BindNewPhoneFragment();
                }else {
                    fragment=new BindPhoneFragment();
                }

                break;
            case R.id.bindemail_myset_fg:
                fragment=new BindEmailFragment();
                break;
            case R.id.bindother_myset_fg:
                fragment=new BindotherFragment();
                break;
            case R.id.changepwd_myset_fg:
                fragment=new ChangePwdFragment();
                break;
        }
        transaction.setCustomAnimations(R.anim.in_from_right,R.anim.out_to_left);
        transaction.replace(R.id.contianner_mysetting,fragment).commit();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        MyLog.i("onActivityResult");
        if(resultCode==Activity.RESULT_OK && requestCode == TO_SELECT_PHOTO)
        {
            picPath = data.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
            MyLog.i( "最终选择的图片="+picPath);
            Bitmap bm = BitmapFactory.decodeFile(picPath);
            icon.setImageBitmap(bm);
            if(picPath!=null)
            {
                handler.sendEmptyMessage(TO_UPLOAD_FILE);
            }else{
                Toast.makeText(getActivity(), "上传的文件路径出错", Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    private void toUploadFile()
    {




//        UploadUtil uploadUtil = UploadUtil.getInstance();;
//        uploadUtil.setOnUploadProcessListener(this);  //设置监听器监听上传状态
//        uploadUtil.uploadFile( picPath,fileKey, requestURL,params);

        RequestParams baseParams=new RequestParams(requestURL);

        baseParams.setMultipart(true);
        baseParams.addBodyParameter("imei", MyApplication.imei);
        baseParams.addBodyParameter("version",MyApplication.versionName);
        baseParams.addBodyParameter("package_name",MyApplication.packageName);
        baseParams.addBodyParameter("channel_id","1");
        baseParams.addBodyParameter("form","app");
        baseParams.addBodyParameter("token",MyAccount.getInstance().getToken());

        TreeMap<String,String> map=new TreeMap<>();
        map.put("imei",MyApplication.imei);
        map.put("version",MyApplication.versionName);
        map.put("package_name",MyApplication.packageName);
        map.put("channel_id","1");
        map.put("form","app");
        map.put("token",MyAccount.getInstance().getToken());
        baseParams.addBodyParameter("sign",Url.getSign(map.entrySet()));
        baseParams.addBodyParameter("filedata",new File(picPath),null);
        x.http().post(baseParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("updataImage==="+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean su=jsonObject.getBoolean("success");
                    if (su){
                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                        String url=jsonObject1.getString("url");

                        uploadurl(url);
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

    private void uploadurl(final String url) {
        BaseParams baseParams=new BaseParams("user/upavatar");
        baseParams.addParams("token",MyAccount.getInstance().getToken());
        baseParams.addParams("avatar",url);
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("上传图片链接"+result);
                userInfo.setAvatar(url);
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
