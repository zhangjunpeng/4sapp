package com.test4s.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.view.HorizontalListView;
import com.app.view.MyScrollView;
import com.test4s.net.CPDetailParams;
import com.test4s.myapp.R;

import org.xutils.common.Callback;
import org.xutils.x;

public class CPDetailActivity extends Activity {

    TextView introuduction;
    boolean flag_showall=false;
    HorizontalListView other_game;

    ImageView share;
    Dialog download_dialog;

    MyScrollView myScrollView;

    private String user_id;
    private String identity_cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpdetail);

        initDialog();

        user_id=getIntent().getStringExtra("id");
        identity_cat=getIntent().getStringExtra("identity_cat");

        initData();

        myScrollView= (MyScrollView) findViewById(R.id.scrollView_cp);


        introuduction= (TextView) findViewById(R.id.introuduction_cp_detail);
        introuduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_showall=!flag_showall;
                if (flag_showall){
                    introuduction.setEllipsize(null);
                    introuduction.setMaxLines(100);
                }else {
                    introuduction.setMaxLines(3);
                    introuduction.setEllipsize(TextUtils.TruncateAt.END);
                }

            }
        });
        other_game= (HorizontalListView) findViewById(R.id.other_game_cp);

//        HorizontalListAdapter myAdaper=new HorizontalListAdapter(this,list_iconUrl);
//        other_game.setAdapter(myAdaper);

        share= (ImageView) findViewById(R.id.share_titlebar_de);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                download_dialog.show();
            }
        });


    }

    private void initData() {
        CPDetailParams cpParams=new CPDetailParams(user_id,identity_cat);
        cpParams.setCacheMaxAge(1000*60*30);
        x.http().post(cpParams, new Callback.CacheCallback<String>() {
            private String result;
            @Override
            public boolean onCache(String result) {
                this.result=result;
                return true;
            }

            @Override
            public void onSuccess(String result) {
                this.result=result;
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


    private void initDialog() {
        download_dialog=new Dialog(CPDetailActivity.this);
        download_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        download_dialog.setCanceledOnTouchOutside(false);
        download_dialog.setContentView(R.layout.dialog_download);
        download_dialog.findViewById(R.id.channel_downdialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CPDetailActivity.this,"点击取消",Toast.LENGTH_SHORT).show();
                download_dialog.dismiss();
            }
        });
        download_dialog.findViewById(R.id.ok_downdialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CPDetailActivity.this,"点击确定",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
