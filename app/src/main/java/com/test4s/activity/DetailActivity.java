package com.test4s.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.view.HorizontalListView;
import com.app.view.MyScrollView;
import com.test4s.adapter.HorizontalListAdapter;
import com.test4s.adapter.IconUrl;
import com.test4s.myapp.R;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends Activity {

    TextView introuduction;
    boolean flag_showall=false;
    HorizontalListView other_game;

    ImageView share;
    Dialog download_dialog;

    MyScrollView myScrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpdetail);

        initDialog();

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
        IconUrl iconUrl=new IconUrl();
        iconUrl.setName("百度");
        iconUrl.setUrl("https://www.baidu.com/img/bd_logo1.png");
        List<IconUrl> list_iconUrl=new ArrayList<>();
        for (int i=0;i<10;i++){
            list_iconUrl.add(iconUrl);
        }
        HorizontalListAdapter myAdaper=new HorizontalListAdapter(this,list_iconUrl);
        other_game.setAdapter(myAdaper);

        share= (ImageView) findViewById(R.id.share_titlebar_de);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                download_dialog.show();
            }
        });


    }


    private void initDialog() {
        download_dialog=new Dialog(DetailActivity.this);
        download_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        download_dialog.setCanceledOnTouchOutside(false);
        download_dialog.setContentView(R.layout.dialog_download);
        download_dialog.findViewById(R.id.channel_downdialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailActivity.this,"点击取消",Toast.LENGTH_SHORT).show();
                download_dialog.dismiss();
            }
        });
        download_dialog.findViewById(R.id.ok_downdialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailActivity.this,"点击确定",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
