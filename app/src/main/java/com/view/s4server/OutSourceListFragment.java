package com.view.s4server;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.app.tools.MyLog;
import com.app.view.PullListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.squareup.picasso.Picasso;
import com.test4s.myapp.BaseFragment;
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
public class OutSourceListFragment extends BaseFragment{
    View view;

    PullListView listView;

    int p=1;

    List<OutSourceSimpleInfo> osSimpleInfos;

    MyOutSourceListAdapter myAdapter;

    private Dialog dialog;
    private Button refreash;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        dialog=showLoadingDialog(getActivity());

        osSimpleInfos=new ArrayList<>();
        myAdapter=new MyOutSourceListAdapter(getActivity(),osSimpleInfos);

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_list,null);
        listView= (PullListView) view.findViewById(R.id.pullToRefresh_fglist);
        refreash= (Button) view.findViewById(R.id.refeash_list);
        refreash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setVisibility(View.VISIBLE);
                dialog.show();
                initData("1");
            }
        });
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
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
                listView.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                listView.onRefreshComplete();
                myAdapter.notifyDataSetChanged();
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
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
                if (jsonArray.length()==0){
                    CusToast.showToast(getActivity(),"没有更多外包信息", Toast.LENGTH_SHORT);
                }
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
                Intent intent=new Intent(getActivity(),OutSourceActivity.class);
                OutSourceSimpleInfo outsoucrxe=osSimpleInfos.get((int) id);
                intent.putExtra("user_id",outsoucrxe.getUser_id());
                intent.putExtra("identity_cat",outsoucrxe.getIdentity_cat());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
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
