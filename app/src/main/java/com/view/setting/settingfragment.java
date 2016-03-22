package com.view.setting;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.app.tools.MyLog;
import com.squareup.picasso.Picasso;
import com.test4s.account.AccountActivity;
import com.test4s.account.MyAccount;
import com.test4s.myapp.R;
import com.view.index.MySettingFragment;

import org.xutils.x;

import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/3/18.
 */
public class Settingfragment extends Fragment implements View.OnClickListener {

    private ToggleButton toggleButton;
    private Button loginout;

    private SettingActivity activity;
    Dialog dialog;
    ProgressDialog progressdialog;

    private TextView cahesize;
    private MyAccount myaccount;

    private static final String PICASSO_CACHE = "picasso-cache";
    private float density;

    private int windowWidth;

    private final static int LoginCode=201;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        activity= (SettingActivity) getActivity();
        myaccount=MyAccount.getInstance();
        //获取屏幕密度
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        windowWidth=metric.widthPixels;
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_setting,null);
        view.findViewById(R.id.clear_cahe_setting).setOnClickListener(this);
        view.findViewById(R.id.check_update).setOnClickListener(this);
        view.findViewById(R.id.advice_report).setOnClickListener(this);
        view.findViewById(R.id.about_us).setOnClickListener(this);

        loginout= (Button) view.findViewById(R.id.loginout_setting);
        cahesize= (TextView) view.findViewById(R.id.cahesize_setting);

        loginout.setOnClickListener(this);

        if (!myaccount.isLogin){
            loginout.setText("重新登录");
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.about_us:
                MyLog.i("点击about_us");
                activity.toAboutUs();
//               FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
//                fragmentManager.beginTransaction().setCustomAnimations(R.anim.in_from_right,R.anim.out_to_left);
//                fragmentManager.beginTransaction().replace(R.id.contianer_setting,new aboutusfragment());
                break;
            case R.id.advice_report:
                MyLog.i("点击advice_report");
                activity.toAdviseReprot();
                break;
            case R.id.clear_cahe_setting:
                showClearDialog();
                break;
            case R.id.loginout_setting:
                if(myaccount.isLogin){
                    MyAccount.getInstance().loginOut();
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                    loginout.setText("重新登录");
                }else {
                    Intent intent=new Intent(getActivity(), AccountActivity.class);
                    startActivityForResult(intent,LoginCode);
                    getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                }
                break;
            case R.id.check_update:
                showUpdateDialog();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode== Activity.RESULT_OK&&requestCode==LoginCode){
            loginout.setText("退出登录");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showClearDialog(){
        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.dialog_setting,null);
        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams((int) (windowWidth*0.8), LinearLayout.LayoutParams.MATCH_PARENT);
        params.leftMargin= (int) (14*density);
        params.rightMargin= (int) (14*density);
        MyLog.i("params width=="+params.width);
        dialog.setContentView(view,params);
        TextView mes= (TextView) view.findViewById(R.id.message_dialog_setting);
        TextView channel= (TextView) view.findViewById(R.id.channel_dialog_setting);
        TextView clear= (TextView) view.findViewById(R.id.positive_dialog_setting);
        dialog.show();
        channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCahe();
                dialog.dismiss();
            }
        });
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    progressdialog.dismiss();
                    cahesize.setText("0 MB");
                    Toast.makeText(getActivity(),"清理完成",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void clearCahe() {

        progressdialog=new ProgressDialog(getActivity());

        Picasso.with(getActivity()).invalidate(getActivity().getApplicationContext().getCacheDir());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setTitle("正在清理中。。。");
        progressdialog.show();
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2*1000);
                    handler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void showUpdateDialog(){
        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.dialog_setting,null);
        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams((int) (windowWidth*0.8), LinearLayout.LayoutParams.MATCH_PARENT);
        params.leftMargin= (int) (14*density);
        params.rightMargin= (int) (14*density);
        MyLog.i("params width=="+params.width);
        dialog.setContentView(view,params);
        TextView mes= (TextView) view.findViewById(R.id.message_dialog_setting);
        TextView channel= (TextView) view.findViewById(R.id.channel_dialog_setting);
        TextView clear= (TextView) view.findViewById(R.id.positive_dialog_setting);
        mes.setText("发现新版本！");
        clear.setText("立即更新");
        clear.setTextColor(Color.rgb(255,157,0));
        dialog.show();
        channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}
