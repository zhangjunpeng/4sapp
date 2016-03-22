package com.view.s4server;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.app.view.RoundImageView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.squareup.picasso.Picasso;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class IssueDetailActivity extends AppCompatActivity {

    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private TextView name_title;
    private ImageView care_title;
    private ImageView care;
    private TextView info;
    private TextView intro;
    private LinearLayout continar;
    private TextView all;
    private TextView name;
    private ImageView icon;

    String user_id;
    String identity_cat;

    String namestring;
    String logostring;
    String scalestring;
    String websitstring;
    String introstring;
    String areastring;
    String business_catstring;
    List<IssueCaseInfo> issuecases;
    String coop_catstring;
    List<LinearLayout> content;
    private boolean flag_showall=false;
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
        setContentView(R.layout.activity_fx_detail);
        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#252525"));

        appBarLayout= (AppBarLayout) findViewById(R.id.appbar_fxdetail);
        toolbar= (Toolbar) findViewById(R.id.toolbar_fxDetail);
        care= (ImageView) findViewById(R.id.care_fxdetatil);
        name_title= (TextView) findViewById(R.id.title_fxdetail);
        care_title= (ImageView) findViewById(R.id.care_fxdetatil);
        info= (TextView) findViewById(R.id.info_fxdetail);
        intro= (TextView) findViewById(R.id.introduction_fxdetail);
        all= (TextView) findViewById(R.id.all_fxdetail);
        name= (TextView) findViewById(R.id.name_fxdetail);
        icon= (ImageView) findViewById(R.id.roundImage_fxdetail);
        continar= (LinearLayout) findViewById(R.id.contianer_fxdetail);


        content=new ArrayList<>();

        //获取屏幕密度
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        initListener();


        initData();

    }
    private void initData() {
        user_id=getIntent().getStringExtra("user_id");
        identity_cat=getIntent().getStringExtra("identity_cat");
        BaseParams baseParams=new BaseParams("index/detail");
        baseParams.addParams("user_id",user_id);
        baseParams.addParams("identity_cat",identity_cat);
        baseParams.addSign();
        baseParams.getRequestParams().setCacheMaxAge(60*1000*60);
        x.http().post(baseParams.getRequestParams(), new Callback.CacheCallback<String>() {
            String res;
            @Override
            public void onSuccess(String result) {
                res=result;
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                MyLog.i("invesdetail==="+res);
                jsonparser(res);
            }

            @Override
            public boolean onCache(String result) {
                res=result;
                return true;
            }
        });
    }
    private void jsonparser(String res) {
        try {
            JSONObject jsonObect=new JSONObject(res);
            boolean su=jsonObect.getBoolean("success");
            int code=jsonObect.getInt("code");
            if (su&&code==200){
                JSONObject data=jsonObect.getJSONObject("data");
                Url.prePic=data.getString("prefixPic");
                JSONObject jinfo=data.getJSONObject("info");
                namestring=jinfo.getString("company_name");
                logostring=jinfo.getString("logo");
                introstring=jinfo.getString("company_intro");
                business_catstring=jinfo.getString("business_cat");
                scalestring=jinfo.getString("company_scale");
                coop_catstring=jinfo.getString("coop_cat");
                areastring=jinfo.getString("area");
                issuecases=new ArrayList<>();
                JSONArray cases=jinfo.getJSONArray("issue_case");
                for (int i=0;i<cases.length();i++){
                    JSONObject investcase=cases.getJSONObject(i);
                    IssueCaseInfo caseInfo=new IssueCaseInfo();
                    caseInfo.setName(investcase.getString("name"));
                    caseInfo.setLogo(investcase.getString("logo"));
                    caseInfo.setGame_type(investcase.getString("game_type"));
                    caseInfo.setCoop_cat(investcase.getString("coop_cat"));
                    caseInfo.setOnline_time(investcase.getString("online_time"));
                    issuecases.add(caseInfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initView();
    }
    private void initView() {
        name_title.setText(namestring);
        name.setText(namestring);
        Picasso.with(this)
                .load(Url.prePic+logostring)
                .into(icon);
        intro.setText(introstring.substring(0,74)+"...");
        info.setText("所在区域 ："+areastring+"\n业务类型 ："+business_catstring+"\n合作类型 ："+coop_catstring+"\n公司规模 ："+scalestring);
        addcase(issuecases);
    }

    private void initListener() {
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
                    care.setVisibility(View.INVISIBLE);
                }else {
                    name_title.setVisibility(View.VISIBLE);
                    care.setVisibility(View.VISIBLE);
                }
                if (scrollRange + verticalOffset == 0) {

                    isShow = true;
                } else if(isShow) {

                    isShow = false;
                }
            }
        });
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
        findViewById(R.id.back_fxdetail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    public void addcase(List<IssueCaseInfo> cases){
        for (int i=0;i<cases.size();i++){
            MyLog.i("addView1");
            ViewHolder viewHolder=new ViewHolder();
            LinearLayout layout= (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_item_invesdetail,null);
            MyLog.i("addView2");
            IssueCaseInfo caseInfo=cases.get(i);
            viewHolder.icon= (RoundImageView) layout.findViewById(R.id.icon_item_invesdetail);
            viewHolder.time= (TextView) layout.findViewById(R.id.time_item_invesdetail);
            viewHolder.name= (TextView) layout.findViewById(R.id.name_item_invesdetail);
            viewHolder.money= (TextView) layout.findViewById(R.id.money_item_invesdetail);
            viewHolder.stage= (TextView) layout.findViewById(R.id.stage_item_invesdetail);
            Picasso.with(this)
                    .load(Url.prePic+caseInfo.getLogo())
                    .placeholder(R.drawable.default_icon)
                    .into(viewHolder.icon);
            viewHolder.time.setText("上线时间 ："+caseInfo.getOnline_time());
            viewHolder.name.setText(caseInfo.getName());
            viewHolder.money.setText("合作类型 ："+caseInfo.getCoop_cat());
            viewHolder.stage.setText("游戏类型 ："+caseInfo.getGame_type());

            if (i==0){
                layout.findViewById(R.id.topline_item_invesdetail).setVisibility(View.INVISIBLE);
            }
            if (i==cases.size()-1){
                layout.findViewById(R.id.boomline_item_invesdetail).setVisibility(View.INVISIBLE);
            }
            MyLog.i("addView3");
            layout.setTag(viewHolder);
            MyLog.i("addView4");
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            continar.addView(layout,layoutParams);
            MyLog.i("addView5");
            content.add(layout);
            MyLog.i("addView6");
        }

    }
    class ViewHolder{
        TextView time;
        RoundImageView icon;
        TextView name;
        TextView money;
        TextView stage;
    }

}
