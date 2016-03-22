package com.view.index;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.app.view.RoundImageView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.picasso.Picasso;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.Information.InfomaionDetailActivity;
import com.view.Information.NewsInfo;
import com.view.s4server.InvesmentDetialActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/7.
 */
public class InformationFragment extends Fragment {

    List<NewsInfo> newInfos;

    PullToRefreshListView listView;
    MyAdapter myapapter;

    int p=1;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newInfos=new ArrayList<>();
        MyLog.i("newInfos=="+newInfos);
        myapapter=new MyAdapter(getActivity(),newInfos);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_information,null);
        listView= (PullToRefreshListView) view.findViewById(R.id.ptflistView_infolist);
        listView.setAdapter(myapapter);
        initData("1");
        initListener();
        return view;
    }

    private void initListener() {
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                newInfos.clear();
                initData("1");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                p++;
                initData(p+"");

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyLog.i("position==="+position);
                MyLog.i("id=="+newInfos.get((int) id).getId());
                Intent intent=new Intent(getActivity(), InfomaionDetailActivity.class);
                intent.putExtra("id",newInfos.get((int) id).getId());
                intent.putExtra("url",newInfos.get((int) id).getUrl());
                startActivity(intent);
            }
        });
    }

    private void initData(String p) {
        BaseParams baseParams=new BaseParams("news/newslist");
        baseParams.addParams("p",p);
        baseParams.addSign();
        baseParams.getRequestParams().setCacheMaxAge(60*10*1000);
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
                MyLog.i("infoList=="+res);
                jsonParser(res);
                myapapter.notifyDataSetChanged();
                listView.onRefreshComplete();
            }

            @Override
            public boolean onCache(String result) {
                res=result;
                return true;
            }
        });
    }

    private void jsonParser(String res) {
        try {
            JSONObject jsonObject=new JSONObject(res);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){
                JSONObject data=jsonObject.getJSONObject("data");
                Url.prePic=data.getString("prefixPic");
                InfomaionDetailActivity.prefixUrl=data.getString("prefixUrl");
                JSONArray array=data.getJSONArray("newsList");
                for (int i=0;i<array.length();i++){
                    NewsInfo newsinfo=new NewsInfo();
                    JSONObject info=array.getJSONObject(i);
                    newsinfo.setId(info.getString("id"));
                    newsinfo.setTitle(info.getString("title"));
                    newsinfo.setViews(info.getString("views"));
                    newsinfo.setComments(info.getString("comments"));
                    newsinfo.setCover_img(info.getString("cover_img"));
                    newsinfo.setUrl(info.getString("url"));
                    newInfos.add(newsinfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    class MyAdapter extends BaseAdapter{

        private List<NewsInfo> list;
        private Context mcontext;

        public MyAdapter(Context contex,List<NewsInfo> list){
            mcontext=contex;
            this.list=list;
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return newInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView==null){
                convertView=LayoutInflater.from(mcontext).inflate(R.layout.item_infolist,parent,false);
                viewHolder=new ViewHolder();
                viewHolder.icon= (RoundImageView) convertView.findViewById(R.id.image_item_infoList);
                viewHolder.name= (TextView) convertView.findViewById(R.id.title_item_infoList);
                viewHolder.conment_num= (TextView) convertView.findViewById(R.id.conmentnum_item_infolist);
                viewHolder.time= (TextView) convertView.findViewById(R.id.time_item_infolist);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            NewsInfo info=list.get(position);
            Picasso.with(mcontext)
                    .load(Url.prePic+info.getCover_img())
                    .placeholder(R.drawable.a2)
                    .into(viewHolder.icon);
            viewHolder.name.setText(info.getTitle());
            viewHolder.conment_num.setText(info.getComments());
            viewHolder.time.setText(info.getTime());
            return convertView;
        }
        class ViewHolder{
            private RoundImageView icon;
            private TextView name;
            private TextView conment_num;
            private TextView time;
        }
    }
}
