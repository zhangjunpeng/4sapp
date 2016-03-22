package com.view.s4server;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.picasso.Picasso;
import com.test4s.jsonparser.IPJsonParser;
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

/**
 * Created by Administrator on 2016/2/18.
 */
public class OutSourceListFragment extends Fragment{
    View view;

    PullToRefreshListView listView;

    int p=1;

    List<OutSourceSimpleInfo> osSimpleInfos;

    MyOutSourceListAdapter myAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        osSimpleInfos=new ArrayList<>();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_list,null);
        listView= (PullToRefreshListView) view.findViewById(R.id.pullToRefresh_fglist);

        myAdapter=new MyOutSourceListAdapter(getActivity(),osSimpleInfos);
        listView.setAdapter(myAdapter);
        initData(1+"");
        initListView();
        return view;
    }

    private void initData(String p) {
        BaseParams baseParams=new BaseParams("index/outsourcelist");
        baseParams.addParams("p",p);
        baseParams.addSign();
//        baseParams.getRequestParams().setCacheMaxAge(10*60*1000);
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("outsourcelist==="+result);
                jsonParser(result);
                myAdapter.notifyDataSetChanged();
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

    private void jsonParser(String res) {
        try {
            JSONObject jsonObject=new JSONObject(res);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                Url.prePic=jsonObject1.getString("prefixPic");
                JSONArray jsonArray=jsonObject1.getJSONArray("outsourceList");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject2=jsonArray.getJSONObject(i);
                    OutSourceSimpleInfo outsource=new OutSourceSimpleInfo();
                    outsource.setUser_id(jsonObject2.getString("user_id"));
                    outsource.setLogo(jsonObject2.getString("logo"));
                    outsource.setOutsource_name(jsonObject2.getString("outsource_name"));
                    outsource.setIdentity_cat(jsonObject2.getString("identity_cat"));
                    outsource.setCompany_name(jsonObject2.getString("company_name"));
                    outsource.setCompany_scale(jsonObject2.getString("company_scale"));
                    outsource.setArea_name(jsonObject2.getString("area_name"));
                    osSimpleInfos.add(outsource);

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyLog.i("解析完成");
    }

    private void initListView() {
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                osSimpleInfos.clear();
                initData("1");
                myAdapter.notifyDataSetChanged();
                listView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                p++;
                initData(p+"");
                listView.onRefreshComplete();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),IPDetailActivity.class);
                OutSourceSimpleInfo outsoucrxe=osSimpleInfos.get((int) id);
                intent.putExtra("id",outsoucrxe.getUser_id());
                startActivity(intent);
            }
        });

    }


    class MyOutSourceListAdapter extends BaseAdapter {
        List<OutSourceSimpleInfo> list;
        Context context;

        public MyOutSourceListAdapter(Context context, List<OutSourceSimpleInfo> list){
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
                convertView=LayoutInflater.from(context).inflate(R.layout.item_iplistfragment,null);
                viewHolder=new ViewHolder();
                viewHolder.icon= (ImageView) convertView.findViewById(R.id.imageView_iplist);
                viewHolder.name= (TextView) convertView.findViewById(R.id.name_item_iplist);
                viewHolder.intro= (TextView) convertView.findViewById(R.id.introuduction_item_iplist);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }

            OutSourceSimpleInfo osSimpleInfo=list.get(position);

            Picasso.with(getContext()).load(Url.prePic+osSimpleInfo.getLogo())
                    .into(viewHolder.icon);
            viewHolder.name.setText(osSimpleInfo.getCompany_name());
            viewHolder.intro.setText("所在区域 ："+osSimpleInfo.getArea_name()+"\n公司规模 ："+osSimpleInfo.getCompany_scale()+"\n类    型 ："+osSimpleInfo.getOutsource_name());
            return convertView;
        }
        class ViewHolder{
            ImageView icon;
            TextView name;
            TextView intro;
        }
    }
}
