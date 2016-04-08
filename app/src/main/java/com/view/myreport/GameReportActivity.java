package com.view.myreport;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.squareup.picasso.Picasso;
import com.test4s.account.MyAccount;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

/*
* 游戏评测报告主页
* */
public class GameReportActivity extends FragmentActivity implements View.OnClickListener {

    String gameid;

    private ImageView back;
    private ImageView share;
    private ImageView icon;
    private ImageView grade;
    private TextView name;
    private TextView fenji;
    private TextView expertreport;
    private TextView playerreport;
    private LinearLayout stars;
    private TextView time;
    private TextView stage;
    private TextView type;
    private TextView theme;
    private TextView paintstyle;
    private TextView horizon;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_game_report);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set a custom tint color for all system bars
        tintManager.setTintColor(Color.parseColor("#252525"));

        gameid=getIntent().getStringExtra("game_id");


        
        initListener();
        initData();
    }

    private void initListener() {
        back= (ImageView) findViewById(R.id.back_gamereport);
        playerreport= (TextView) findViewById(R.id.playerreport_gamereport);
        expertreport= (TextView) findViewById(R.id.expertreport_gamereport);
        icon= (ImageView) findViewById(R.id.icon_gamereport);
        grade= (ImageView) findViewById(R.id.grade_gamereport);
        name= (TextView) findViewById(R.id.gamename_gamereport);
        fenji= (TextView) findViewById(R.id.gamepz_gamereport);
        stars= (LinearLayout) findViewById(R.id.stars_gamereport);
        time= (TextView) findViewById(R.id.time_gamereport);
        stage= (TextView) findViewById(R.id.stage_gamereport);
        type= (TextView) findViewById(R.id.type_gamereport);
        theme= (TextView) findViewById(R.id.ticai_gamereport);
        horizon= (TextView) findViewById(R.id.shijiao_gamereport);
        paintstyle= (TextView) findViewById(R.id.huafeng_gamereport);




        expertreport.setOnClickListener(this);
        playerreport.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    private void initData() {
        BaseParams baseparams=new BaseParams("test/reportdetail");
        baseparams.addParams("game_id",gameid);
        baseparams.addParams("token", MyAccount.getInstance().getToken());
        baseparams.addSign();
        x.http().post(baseparams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("gamereport back=="+result);
                parserJson(result);
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

    private void parserJson(String result) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){
                JSONObject data=jsonObject.getJSONObject("data");
                JSONObject info=data.getJSONObject("gameInfo");
                Picasso.with(this)
                        .load(Url.prePic+info.getString("game_img"))
                        .into(icon);
                Picasso.with(this)
                        .load(Url.prePic+info.getString("game_grade"))
                        .into(grade);

                name.setText(info.getString("game_name"));
                fenji.setText(info.getString("game_grade_text"));
                int score=info.getInt("score");
                setStar(score,stars);
                time.setText(info.getString("create_time"));
                type.setText(info.getString("game_type"));
                stage.setText(info.getString("game_stage"));
                paintstyle.setText(info.getString("paint_style"));
                horizon.setText(info.getString("horizon"));
                theme.setText(info.getString("theme"));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.expertreport_gamereport:
                Intent intent=new Intent(this,ExpertReportActivity.class);
                intent.putExtra("game_id",gameid);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                break;
            case R.id.playerreport_gamereport:
                Intent intent1=new Intent(this,PlayerReportActivity.class);
                intent1.putExtra("game_id",gameid);
                startActivity(intent1);
                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                break;
            case R.id.back_gamereport:
                finish();
                break;
        }
    }
    private void setStar(int j,LinearLayout stars) {
        for (int i=0;i<stars.getChildCount();i++){
            ImageView star= (ImageView) stars.getChildAt(i);
            if (i<j){
                star.setImageResource(R.drawable.star_question_1);
            }else {
                star.setImageResource(R.drawable.star_question_0);
            }

        }
    }
}
