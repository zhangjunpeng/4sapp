package com.test4s.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.GameHttpConnection;
import com.app.tools.MyLog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.test4s.adapter.NameIcon;
import com.test4s.myapp.MyApplication;
import com.test4s.myapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.Executors;

public class ListActivity extends AppCompatActivity {

    PullToRefreshListView listView;

    ImageView back;
    TextView title;

    String tag;
    String p="1";

    String host="http://app.4stest.com/index/";
    String url="";

    public final static String CP_TAG="cplist";
    public final static String IP_TAG="iplist";
    public final static String Invesment_TAG="investorlist";
    public final static String OutSource_TAG="outsourcelist";
    public final static String Issue_TAG="issuelist";



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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);

        listView= (PullToRefreshListView) findViewById(R.id.listView_list);

        back= (ImageView) findViewById(R.id.back_titlebar);
        title= (TextView) findViewById(R.id.title_titlebar);

        back.setImageResource(R.drawable.back);

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Toast.makeText(ListActivity.this,"下拉刷新",Toast.LENGTH_SHORT).show();
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


        tag=getIntent().getStringExtra("tag");

        initData();

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


}
