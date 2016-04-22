package com.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tools.GameHttpConnection;
import com.app.tools.MyLog;
import com.app.tools.ScreenUtil;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.test4s.adapter.NameIcon;
import com.test4s.myapp.MyApplication;
import com.test4s.myapp.R;
import com.view.s4server.CPListFragment;
import com.view.s4server.IPListFragment;
import com.view.s4server.InvesmentListFragment;
import com.view.s4server.IssueListFragment;
import com.view.s4server.OutSourceListFragment;
import com.view.search.SearchActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.Executors;

public class ListActivity extends FragmentActivity implements OnClickListener{

    PullToRefreshListView listView;
    
    ImageView back;
    TextView title;
    ImageView search;

    String tag;
    String p="1";

    String host="http://app.4stest.com/index/";
    String url="";


    public final static String CP_TAG="cplist";
    public final static String IP_TAG="iplist";
    public final static String Invesment_TAG="investorlist";
    public final static String OutSource_TAG="outsourcelist";
    public final static String Issue_TAG="issuelist";


    FragmentManager manager;
    FragmentTransaction transaction;


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    MyLog.i("List====="+tag+"==="+msg.obj.toString());
                    initView(msg.obj.toString());
                    break;
            }
        }
    };


    protected void setImmerseLayout(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
                /*window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);*/
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            int statusBarHeight = ScreenUtil.getStatusBarHeight(this.getBaseContext());
            view.setPadding(0, statusBarHeight, 0, 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);

//        listView= (PullToRefreshListView) findViewById(R.id.listView_list);

        back= (ImageView) findViewById(R.id.back_titlebar);
        title= (TextView) findViewById(R.id.title_titlebar);
        search= (ImageView) findViewById(R.id.search_titlebar);


        back.setImageResource(R.drawable.back);

        back.setOnClickListener(this);
        search.setOnClickListener(this);

        setImmerseLayout(findViewById(R.id.title_list));


        tag=getIntent().getStringExtra("tag");

        manager=getSupportFragmentManager();
        transaction=manager.beginTransaction();

//        initData();

        initView();
    }

    private void initView() {
        Fragment fragment=null;
        switch (tag) {
            case CP_TAG:
                title.setText("开发者");
                fragment=new CPListFragment();
                break;
            case IP_TAG:
                title.setText("IP");
                fragment=new IPListFragment();
                break;
            case Invesment_TAG:
                title.setText("投资人");
                fragment=new InvesmentListFragment();
                break;
            case OutSource_TAG:
                title.setText("外包");
                fragment=new OutSourceListFragment();
                break;
            case Issue_TAG:
                title.setText("发行");
                fragment=new IssueListFragment();
                break;
        }
        transaction.setCustomAnimations(R.anim.in_from_right,R.anim.out_to_left);
        transaction.replace(R.id.contianner_listactivity,fragment).commit();
    }

    private void initData() {

        switch (tag){
            case CP_TAG:
                url=host+CP_TAG;
                break;
            case IP_TAG:
                url=host+IP_TAG;
                break;
            case Invesment_TAG:
                url=host+Invesment_TAG;
                break;
            case OutSource_TAG:
                url=host+OutSource_TAG;
                break;
            case Issue_TAG:
                url=host+Issue_TAG;
                break;
        }
        final TreeMap<String,String> map=new TreeMap<>();
        map.put("imei", MyApplication.imei);
        map.put("version","1.0");
        map.put("package_name",MyApplication.packageName);
        map.put("channel_id","1");
        map.put("p",p);
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                String result=  GameHttpConnection.doPost(url,map,null);
                handler.obtainMessage(0,result).sendToTarget();
            }
        });
    }

    private void initView(String res) {
        switch (tag){
            case CP_TAG:
                break;
            case IP_TAG:

                break;
            case Invesment_TAG:
                break;
            case OutSource_TAG:
                break;
            case Issue_TAG:
                break;

        }
    }

    private List<NameIcon> parser(String res){
        List<NameIcon> nameIcons;
        if (res.equals("")){
            return null;
        }
        nameIcons=new ArrayList<>();
        try {
            JSONObject jsonObject=new JSONObject(res);
            JSONObject jsonObject1=jsonObject.getJSONObject("data");
            switch (tag){
                case IP_TAG:
                    JSONArray jsonArray=jsonObject1.getJSONArray("ipList");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject2=jsonArray.getJSONObject(i);
                        NameIcon nameIcon=new NameIcon();
                        nameIcon.setImgUrl(jsonObject2.getString("ip_logo"));
                        nameIcon.setName(jsonObject2.getString("ip_name"));
                        nameIcon.setIntroduction("类型 : "+jsonObject2.getString("ip_cat")+"\n风格 : "+jsonObject2.getString("ip_style")+"\n授权范围 : "+jsonObject2.getString("uthority"));
                        nameIcons.add(nameIcon);
                    }
                    break;
                case CP_TAG:
                    JSONArray jsonArray1=jsonObject1.getJSONArray("cpList");
                    for (int i=0;i<jsonArray1.length();i++){
                        JSONObject jsonObject2=jsonArray1.getJSONObject(i);
                        NameIcon nameIcon=new NameIcon();
                        nameIcon.setImgUrl(jsonObject2.getString("logo"));
                        nameIcon.setName(jsonObject2.getString("company_name"));
                        nameIcon.setIntroduction("company_intro");
                        nameIcons.add(nameIcon);
                    }
                    break;
                case Invesment_TAG:
                    JSONArray jsonArray2=jsonObject1.getJSONArray("investorList");
                    for (int i=0;i<jsonArray2.length();i++){
                        JSONObject jsonObject2=jsonArray2.getJSONObject(i);
                        NameIcon nameIcon=new NameIcon();
                        nameIcon.setImgUrl(jsonObject2.getString("logo"));
                        nameIcon.setName(jsonObject2.getString("company_name"));
                        nameIcon.setIntroduction("company_intro");
                        nameIcons.add(nameIcon);
                    }
                    break;
                case Issue_TAG:
                    JSONArray jsonArray3=jsonObject1.getJSONArray("issueList");
                    for (int i=0;i<jsonArray3.length();i++){
                        JSONObject jsonObject2=jsonArray3.getJSONObject(i);
                        NameIcon nameIcon=new NameIcon();
                        nameIcon.setImgUrl(jsonObject2.getString("logo"));
                        nameIcon.setName(jsonObject2.getString("company_name"));
                        nameIcon.setIntroduction("company_intro");
                        nameIcons.add(nameIcon);
                    }
                    break;
                case OutSource_TAG:
                    JSONArray jsonArray4=jsonObject1.getJSONArray("outsourceList");
                    for (int i=0;i<jsonArray4.length();i++){
                        JSONObject jsonObject2=jsonArray4.getJSONObject(i);
                        NameIcon nameIcon=new NameIcon();
                        nameIcon.setImgUrl(jsonObject2.getString("logo"));
                        nameIcon.setName(jsonObject2.getString("company_name"));
                        nameIcon.setIntroduction("company_intro");
                        nameIcons.add(nameIcon);
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return nameIcons;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_titlebar:
                finish();
                break;
            case R.id.search_titlebar:
                Intent intent=new Intent(ListActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                break;
        }
    }
}
