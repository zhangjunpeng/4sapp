package com.view.s4server;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.MyLog;
import com.app.view.HorizontalListView;
import com.app.view.MyScrollView;
import com.app.view.RoundImageView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.squareup.picasso.Picasso;
import com.test4s.adapter.Game_HL_Adapter;
import com.test4s.gdb.GameInfo;
import com.test4s.jsonparser.CPJsonParser;
import com.test4s.net.BaseParams;
import com.test4s.net.CPDetailParams;
import com.test4s.myapp.R;
import com.test4s.net.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class CPDetailActivity extends Activity {

    boolean flag_showall=false;


    ImageView back;
    TextView name_title;
    ImageView share;
    private ImageView care_title;
    Dialog download_dialog;


    int[] locations = new int[2];

    private String user_id;
    private String identity_cat;

    private AppBarLayout appBarLayout;
    private RoundImageView icon;
    private ImageView care;
    private TextView name;
    private TextView intro;
    private TextView info;
    private HorizontalListView horizontalListView;
    private Toolbar toolbar;
    private TextView all;

    private String namestring;
    private String logostring;
    private String scalestring;
    private String phonestring;
    private String cityname;
    private String introstring;

    List<GameInfo> othergames;
    private float density;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_cpdetail);
        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set a custom tint color for all system bars
        tintManager.setTintColor(Color.parseColor("#252525"));


        appBarLayout= (AppBarLayout) findViewById(R.id.appbar_cpdetail);
        toolbar= (Toolbar) findViewById(R.id.toolbar_cpDetail);
        back= (ImageView) findViewById(R.id.back_cpdetail);
        name_title= (TextView) findViewById(R.id.title_cpdetail);
        care_title= (ImageView) findViewById(R.id.care_cpdetatil);
        share= (ImageView) findViewById(R.id.share_cpdetail);
        icon= (RoundImageView) findViewById(R.id.roundImage_cpdetail);
        name= (TextView) findViewById(R.id.name_cpdetail);

        care= (ImageView) findViewById(R.id.attention_cpdetail);
        intro= (TextView) findViewById(R.id.introduction_cpdetail);
        info= (TextView) findViewById(R.id.info_cpdetail);
        horizontalListView= (HorizontalListView) findViewById(R.id.horListView_cpdetail);
        share= (ImageView) findViewById(R.id.share_titlebar_de);
        all= (TextView) findViewById(R.id.all_cpdetail);


        user_id=getIntent().getStringExtra("user_id");
        identity_cat=getIntent().getStringExtra("identity_cat");



        care= (ImageView) findViewById(R.id.attention_cpdetail);


        //获取屏幕密度
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        initListener();

        initData();

    }

    private void initListener() {

        intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_showall=!flag_showall;
                if (flag_showall){

                    intro.setEllipsize(null);
                    intro.setMaxLines(100);
                    intro.setText(introstring);
                    all.setText("收起");
                }else {
                    String into=introstring.substring(0,73);
                    intro.setText(into+"...");
                    intro.setMaxLines(3);
                    intro.setEllipsize(TextUtils.TruncateAt.END);
                    all.setText("全部");
                }
            }
        });
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;


            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (verticalOffset<-100*density){
                    toolbar.setBackgroundColor(Color.argb(15,255,255,255));
                }else {
                    toolbar.setBackgroundColor(Color.argb( 0,37,37,37));
                }
                if (verticalOffset>-200*density){
                    name_title.setVisibility(View.INVISIBLE);
                    care_title.setVisibility(View.INVISIBLE);
                }else {
                    name_title.setVisibility(View.VISIBLE);
                    care_title.setVisibility(View.VISIBLE);
                }
                if (scrollRange + verticalOffset == 0) {

                    isShow = true;
                } else if(isShow) {

                    isShow = false;
                }
            }
        });
        findViewById(R.id.back_cpdetail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_SCROLL:
                MyLog.i("care位置：："+locations[0]+"==="+locations[1]);
                break;
        }
        return super.onTouchEvent(event);
    }

    private void initData() {
        BaseParams baseParams=new BaseParams("index/cpdetail");
        baseParams.addParams("user_id",user_id);
        baseParams.addParams("identity_cat",identity_cat);
        baseParams.addSign();
        baseParams.getRequestParams().setCacheMaxAge(1000*60*30);
        x.http().post(baseParams.getRequestParams(), new Callback.CacheCallback<String>() {
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
                MyLog.i("cpdetail"+result);
                parser(result);

            }
        });

    }

    private void parser(String result) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){
                JSONObject data=jsonObject.getJSONObject("data");
                JSONObject detail=data.getJSONObject("detail");
                namestring=detail.getString("company_name");
                logostring=detail.getString("logo");
                scalestring=detail.getString("company_scale");
                phonestring=detail.getString("company_phone");
                introstring=detail.getString("company_intro");
                cityname=detail.getString("city_name");
                othergames=new ArrayList<>();
                JSONArray cpGames=data.getJSONArray("cpGameList");
                for (int i=0;i<cpGames.length();i++){
                    JSONObject game=cpGames.getJSONObject(i);
                    GameInfo gameInfo=new GameInfo();
                    gameInfo.setGame_id(game.getString("id"));
                    gameInfo.setGame_img(game.getString("game_img"));
                    gameInfo.setGame_name(game.getString("game_name"));
                    othergames.add(gameInfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyLog.i("initView");
        initView();

    }

    private void initView() {
        name_title.setText(namestring);
        name.setText(namestring);
        Picasso.with(this)
                .load(Url.prePic+logostring)
                .into(icon);
        intro.setText(introstring.substring(0,74)+"...");
        info.setText("所在地 ："+cityname+"\n公司规模 ："+scalestring+"\n电话 ："+phonestring);
        MyLog.i("other games size"+othergames.size());
        Game_HL_Adapter gameAdaper=new Game_HL_Adapter(this,othergames);
        horizontalListView.setAdapter(gameAdaper);
        horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
