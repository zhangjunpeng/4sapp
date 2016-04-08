package com.view.myreport;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/30.
 */
public class PlayerAdviseFragment  extends Fragment{

    View view;

    PullToRefreshListView listView;

    List<Map<String,String>> dataList;
    String gameid;
    MyAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameid=getArguments().getString("game_id","");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_playeradvise,null);
        listView= (PullToRefreshListView) view.findViewById(R.id.listview_playeradvise);
        dataList=new ArrayList<>();
        adapter=new MyAdapter(getActivity(),dataList);
        listView.setAdapter(adapter);
        initData("1");
        return view;
    }

    private void initData(String p) {
        MyLog.i("advise请求");
        BaseParams params=new BaseParams("test/advise");
        params.addParams("p",p);
        params.addParams("game_id",gameid);
        params.addSign();
        x.http().post(params.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("advise==="+result);
                try {
                    jsonparer(result);
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

    private void jsonparer(String result) throws JSONException {
        JSONObject json=new JSONObject(result);
        boolean su=json.getBoolean("success");
        int code=json.getInt("code");
        if (su&&code==200){
            JSONObject data=json.getJSONObject("data");
            JSONArray list=data.getJSONArray("list");
            for (int i=0;i<list.length();i++){
                JSONObject obj=list.getJSONObject(i);
                Map<String,String> map=new HashMap<>();
                map.put("nickname",obj.getString("nickname"));
                map.put("create_time",obj.getString("create_time"));
                map.put("test_total_score",obj.getString("test_total_score"));
                map.put("advise",obj.getString("advise"));
                dataList.add(map);
            }
        }
    }

    class MyAdapter extends BaseAdapter{
        private List<Map<String,String>> datalist;
        private Context context;

        public MyAdapter(Context context,List<Map<String,String>> datalist){
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
            Viewholder viewHolder;
            if (convertView==null){
                convertView=LayoutInflater.from(context).inflate(R.layout.item_list_playeradvise,parent,false);
                viewHolder=new Viewholder();
                viewHolder.names= (TextView) convertView.findViewById(R.id.username_item_plyaeradvise);
                viewHolder.time= (TextView) convertView.findViewById(R.id.time_item_playeradvise);
                viewHolder.stars= (LinearLayout) convertView.findViewById(R.id.stars_item_playeradvise);
                viewHolder.advise= (TextView) convertView.findViewById(R.id.advise_item_playeradvise);
            }else {
                viewHolder= (Viewholder) convertView.getTag();
            }
            Map<String,String> map=datalist.get(position);
            viewHolder.names.setText(map.get("nickname"));
            viewHolder.time.setText(map.get("create_time"));
            float score=Float.parseFloat(map.get("test_total_score"));
            setStars(viewHolder.stars,score);
            viewHolder.advise.setText(map.get("advise"));
            return convertView;
        }
        class Viewholder{
            TextView names;
            TextView time;
            LinearLayout stars;
            TextView advise;
        }
    }
    private void setStars(LinearLayout stars,float score) {
        for (int i=0;i<stars.getChildCount();i++){
            ImageView image= (ImageView) stars.getChildAt(i);
            int orangestar= (int) score;
            if (i<orangestar){
                image.setImageResource(R.drawable.star_question_1);
            }else if(i==orangestar){
                if (score-orangestar>=0.5){
                    image.setImageResource(R.drawable.question_star_half);
                }else {
                    image.setImageResource(R.drawable.star_question_0);
                }
            }else {
                image.setImageResource(R.drawable.star_question_0);
            }
        }
    }
}
