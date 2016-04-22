package com.view.myreport;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.view.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExpertReportActivity extends BaseActivity {

    String gameid;
    ListView listView;
    List<Map<String,String>> datalist;
    ExpterListAdapter adapter;

    ImageView back;
    TextView title;
    TextView save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {  SystemBarTintManager tintManager = new SystemBarTintManager(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_report);
        setImmerseLayout(findViewById(R.id.titlebar_expertreport));

        listView= (ListView) findViewById(R.id.listview_expertreport);
        back= (ImageView) findViewById(R.id.back_savebar);
        title= (TextView) findViewById(R.id.textView_titlebar_save);
        save= (TextView) findViewById(R.id.save_savebar);


        gameid=getIntent().getStringExtra("game_id");
        datalist=new ArrayList<>();
        adapter=new ExpterListAdapter(this,datalist);

        listView.setAdapter(adapter);

        title.setText("专家评测报告");
        save.setVisibility(View.INVISIBLE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initData();
    }

    private void initData() {
        BaseParams params=new BaseParams("test/splreport");
        params.addParams("game_id",gameid);
        params.addSign();

        x.http().post(params.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("expert report=="+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean su=jsonObject.getBoolean("success");
                    int code= jsonObject.getInt("code");
                    if (su&&code==200){
                        JSONObject data=jsonObject.getJSONObject("data");
                        JSONObject info=data.getJSONObject("splreport");
                        Iterator<String> iterator=info.keys();
                        while (iterator.hasNext()){
                            String key=iterator.next();
                            String value=info.getString(key);
                            if (TextUtils.isEmpty(value)){
                                continue;
                            }
                            Map<String,String> map=new HashMap<String, String>();
                            if(key.equals("id")){
                                continue;
                            }
                            map.put("name",key);
                            map.put("content",value);
                            datalist.add(map);
                        }
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
                adapter.notifyDataSetChanged();
            }
        });

    }

    class ExpterListAdapter extends BaseAdapter{

        private Context context;
        private List<Map<String,String>> datalist;

        public ExpterListAdapter(Context context, List<Map<String, String>> datalist) {
            this.context=context;
            this.datalist=datalist;
        }

        @Override
        public int getCount() {
            return datalist.size();
        }

        @Override
        public Object getItem(int position) {
            return datalist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder=null;
            if (convertView==null){
                convertView= LayoutInflater.from(context).inflate(R.layout.item_expertreport,parent,false);
                viewHolder=new ViewHolder();
                viewHolder.name= (TextView) convertView.findViewById(R.id.name_item_expertreport);
                viewHolder.content= (TextView) convertView.findViewById(R.id.content_item_expertreport);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            Map<String,String> map=datalist.get(position);
            String content=map.get("content");
            content=content.replace("\r","");
            content=content.replace("&nbsp;"," ");
            MyLog.i("content=="+content);
            viewHolder.content.setText(content);
            switch (map.get("name")){
                case "taste":
                    viewHolder.name.setText("核心结论");
                    break;
                case "method":
                    viewHolder.name.setText("玩法概述");
                    break;
                case "advantage":
                    viewHolder.name.setText("优势");
                    break;
                case "bad":
                    viewHolder.name.setText("劣势");
                    break;
                case "art_quality":
                    viewHolder.name.setText("感官表现");
                    break;
                case "music":
                    viewHolder.name.setText("音乐和音效");
                    break;
                case "battle_show":
                    viewHolder.name.setText("背景题材");
                    break;
                case "mode_style":
                    viewHolder.name.setText("交互操作");
                    break;
                case "game_ui":
                    viewHolder.name.setText("游戏UI");
                    break;
                case "funny":
                    viewHolder.name.setText("可玩性");
                    break;
                case "rational":
                    viewHolder.name.setText("合理性");
                    break;
                case "plentiful":
                    viewHolder.name.setText("丰富性");
                    break;
                case "new_hand":
                    viewHolder.name.setText("新手引导");
                    break;
                case "business":
                    viewHolder.name.setText("商业化");
                    break;
                case "social":
                    viewHolder.name.setText("社交内容");
                    break;
                case "other":
                    viewHolder.name.setText("其他");
                    break;
                case "procedures":
                    viewHolder.name.setText("程序底层");
                    break;
                case "advise":
                    viewHolder.name.setText("优化建议");
                    break;

            }

            return convertView;
        }

        class ViewHolder{
            TextView name;
            TextView content;
        }
    }

}
