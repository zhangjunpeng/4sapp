package com.view.s4server;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.app.tools.ScreenUtil;
import com.app.tools.StringTools;
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

public class IPDetailActivity extends AppCompatActivity implements View.OnClickListener{

    String id;

    private CollapsingToolbarLayout toolbarLayout;
    private AppBarLayout appBarLayout;

    ImageView back;
    TextView title;
    ImageView care;
    ImageView share;

    TextView name_title;
    ImageView icon;
    TextView name_ipdetail;
    TextView ipcat;
    TextView ipintro;
    WebView webView;
    ListView deslistView;
    ListView otheripListView;
    TextView all;


    LinearLayout ipshotContinar;

    String ip_name;
    String ip_logo;

    String ip_cat;//类型
    String ip_style;//风格
    String uthority;//授权范围
    String over_time;//上市时间

    String ip_info;

    String video_url;

    List<String> ipshot;

    List<IPSimpleInfo> ipderivatives;
    List<IPSimpleInfo> otherIp;

    Toolbar toolbar;

    float density;


    RelativeLayout intro_re;
    boolean flag_showall=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_ipdetail);
        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set a custom tint color for all system bars
        tintManager.setTintColor(Color.parseColor("#252525"));

        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collToolbar_ipDetail);
        appBarLayout= (AppBarLayout) findViewById(R.id.appbar_ipdetail);
        ipshotContinar= (LinearLayout) findViewById(R.id.imageContinar_ipdetail);
        back= (ImageView) findViewById(R.id.back_ipdetail);
        name_title= (TextView) findViewById(R.id.title_ipdetail);
        care= (ImageView) findViewById(R.id.care_ipdetatil);
        toolbar= (Toolbar) findViewById(R.id.toolbar_ipDetail);

        icon= (ImageView) findViewById(R.id.roundImage_ipdetail);
        name_ipdetail= (TextView) findViewById(R.id.name_ipdetail);
        ipcat= (TextView) findViewById(R.id.cat_ipdetail);
        ipintro= (TextView) findViewById(R.id.introduction_ipdetail);
        deslistView= (ListView) findViewById(R.id.ipdes_ipdetail);
        otheripListView= (ListView) findViewById(R.id.relatedip_ipdetail);
        intro_re= (RelativeLayout) findViewById(R.id.intro_ipdetail_re);
        all= (TextView) findViewById(R.id.all_ipdetail);





        intro_re.setOnClickListener(this);
        findViewById(R.id.back_ipdetail).setOnClickListener(this);

        webView= (WebView) findViewById(R.id.video_ipdetail);
        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setPluginState(WebSettings.PluginState.ON);

//        webView.loadData("<iframe height=498 width=510 src=\"http://player.youku.com/embed/XMTQ5MjA1NTgwNA==\" frameborder=0 allowfullscreen></iframe>","html/text",null);
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        //获取屏幕密度
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）

        id=getIntent().getStringExtra("id");
        initListener();
        initData(id);


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
    }

    @Override
    protected void onResume() {
        super.onResume();
        toggleWebViewState(true);

    }
    private void toggleWebViewState(boolean pause)
    {
        try
        {
            Class.forName("android.webkit.WebView")
                    .getMethod(pause
                            ? "onPause"
                            : "onResume", (Class[]) null)
                    .invoke(webView, (Object[]) null);
        }
        catch (Exception e){}
    }

    @Override
    protected void onPause() {
        super.onPause();
//        toggleWebViewState(true);
//        webView.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        webView.loadUrl("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    private void initData(String id) {
        BaseParams baseParams=new BaseParams("index/ipdetail");
        baseParams.addParams("id",id);
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("IPDetail==="+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean su=jsonObject.getBoolean("success");
                    int code=jsonObject.getInt("code");
                    if (su&&code==200){
                        parser(jsonObject.getString("data"));
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

    private void parser(String result) {
        ipshot=new ArrayList<>();
        ipderivatives=new ArrayList<>();
        otherIp=new ArrayList<>();
        try {
            JSONObject jsonObject=new JSONObject(result);
            Url.prePic=jsonObject.getString("prefixPic");
            JSONObject info=jsonObject.getJSONObject("info");
            ip_name=info.getString("ip_name");
            ip_logo=info.getString("ip_logo");
            ip_cat =info.getString("ip_cat");
            ip_style=info.getString("ip_style");
            uthority=info.getString("uthority");
            over_time=info.getString("over_time");
            ip_info=info.getString("ip_info");
            video_url=info.getString("video_url");
            JSONArray ipshotArray=info.getJSONArray("ip_shot");
            if (ipshotArray!=null){
                MyLog.i("ip_shot not null");
                for (int i=0;i<ipshotArray.length();i++){
                    String ipshot_url=ipshotArray.getString(i);
                    ipshot.add(ipshot_url);
                }
            }else {
                MyLog.i("ip_shot null");
            }

            JSONArray derivatives=info.getJSONArray("derivative");
            if (derivatives!=null){
                MyLog.i("derivatives not null");
            for (int i=0;i<derivatives.length();i++){
                JSONObject ip=derivatives.getJSONObject(i);
                IPSimpleInfo ipSimpleInfo=new IPSimpleInfo();
                ipSimpleInfo.setLogo(ip.getString("ip_logo"));
                ipSimpleInfo.setIp_name(ip.getString("ip_name"));
                ipSimpleInfo.setIp_info(ip.getString("ip_info"));
                ipSimpleInfo.setOver_time(ip.getString("over_time"));
                ipderivatives.add(ipSimpleInfo);
            }
            } else {
                MyLog.i("derivatives null");
            }
            JSONArray others=info.getJSONArray("ip_list");
            if (others!=null) {
                MyLog.i("others not null");
                for (int i = 0; i < others.length(); i++) {
                    IPSimpleInfo ipSimpleInfo = new IPSimpleInfo();
                    JSONObject ip = others.getJSONObject(i);
                    ipSimpleInfo.setId(ip.getString("id"));
                    ipSimpleInfo.setIp_name(ip.getString("ip_name"));
                    ipSimpleInfo.setLogo(ip.getString("ip_logo"));
                    ipSimpleInfo.setIp_cat(ip.getString("ip_cat"));
                    ipSimpleInfo.setIp_style(ip.getString("ip_style"));
                    ipSimpleInfo.setUthority(ip.getString("uthority"));
                    otherIp.add(ipSimpleInfo);
                }
            }else {
                MyLog.i("others null");
            }
            MyLog.i("ipdetail解析完成");

            initIpshot();
            initView();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initView() {
        Picasso.with(this)
                .load(Url.prePic+ip_logo)
                .into(icon);
        name_ipdetail.setText(ip_name);
        name_title.setText(ip_name);

        ipcat.setText("类型 ："+ip_cat+"\n风格 ："+ip_style+"\n授权范围 ："+uthority+"\n上市时间 ："+over_time);

        ipintro.setText(ip_info.substring(0,74)+"...");

        deslistView.setAdapter(new MyDesAdapter(this,ipderivatives));
        otheripListView.setAdapter(new MyIpListAdapter(this,otherIp));




        webView.loadUrl(video_url);
        setListViewHeightBasedOnChildren(deslistView);
        setListViewHeightBasedOnChildren(otheripListView);
        otheripListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(IPDetailActivity.this,IPDetailActivity.class);
                intent.putExtra("id",otherIp.get(position).getId());
                startActivity(intent);
                finish();
            }
        });

    }
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.intro_ipdetail_re:
                flag_showall=!flag_showall;
                if (flag_showall){

                    ipintro.setEllipsize(null);
                    ipintro.setMaxLines(100);
                    ipintro.setText(ip_info);
                    all.setText("收起");
                }else {
                    String into=ip_info.substring(0,73);
                    ipintro.setText(into+"...");
                    ipintro.setMaxLines(3);
                    ipintro.setEllipsize(TextUtils.TruncateAt.END);
                    all.setText("全部");
                }
                break;
            case R.id.back_ipdetail:
                finish();
                break;
        }
    }

    class MyDesAdapter extends BaseAdapter{
        List<IPSimpleInfo> list;
        Context context;

        public MyDesAdapter(Context context, List<IPSimpleInfo> list){
            this.context=context;
            this.list=list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView==null){
                convertView= LayoutInflater.from(context).inflate(R.layout.item_ipderivative,null);
                viewHolder=new ViewHolder();
                viewHolder.icon= (ImageView) convertView.findViewById(R.id.imageView_ipdes);
                viewHolder.name= (TextView) convertView.findViewById(R.id.name_item_ipdes);
                viewHolder.intro= (TextView) convertView.findViewById(R.id.introuduction_item_ipdes);
                viewHolder.voertime= (TextView) convertView.findViewById(R.id.overtime_ipdes);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }

            IPSimpleInfo ipSimpleInfo=list.get(position);

            Picasso.with(context).load(Url.prePic+ipSimpleInfo.getLogo())
                    .into(viewHolder.icon);
            viewHolder.name.setText(ipSimpleInfo.getIp_name());
            viewHolder.intro.setText("简介 ："+ipSimpleInfo.getIp_info());
            viewHolder.voertime.setText("上线时间 ："+ipSimpleInfo.getOver_time());
            return convertView;
        }
        class ViewHolder{
            ImageView icon;
            TextView name;
            TextView intro;
            TextView voertime;
        }
    }


    private void initIpshot() {

        if (ipshot==null){
            MyLog.i("ipshot null");
            return;
        }
        for (int i=0;i<ipshot.size();i++){
            ImageView imageView=new ImageView(this);
            int height=getWindow().getAttributes().height;
            int width=getWindow().getAttributes().width;
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams((int)(332*density),(int)(187*density));

            params.leftMargin= (int) (14*density);
            params.rightMargin= (int) (14*density);
            params.bottomMargin= (int) (6*density);
            imageView.setImageResource(R.drawable.a1);
            imageView.setId(i);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            ipshotContinar.addView(imageView,params);
            MyLog.i("添加ImageView");
            Picasso.with(this).load(Url.prePic+ipshot.get(i))
                    .placeholder(R.drawable.a2)
                    .into(imageView);
        }
    }

}
