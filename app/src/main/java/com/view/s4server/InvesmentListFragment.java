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
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.picasso.Picasso;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/18.
 */
public class InvesmentListFragment extends Fragment{
    View view;

    List<InvesmentSimpleInfo> invesmentSimpleInfos;
    PullToRefreshListView listView;
    MyIssueAdapter myAdapter;

    private TextView title;
    private ImageView search;
    private ImageView back;

    int p=1;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        invesmentSimpleInfos=new ArrayList<>();
        initData("1");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_list,null);
        listView= (PullToRefreshListView) view.findViewById(R.id.pullToRefresh_fglist);
        myAdapter=new MyIssueAdapter(getActivity(),invesmentSimpleInfos);
        initLisener();

        return view;
    }

    private void initLisener() {
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                invesmentSimpleInfos=new ArrayList<InvesmentSimpleInfo>();
                initData("1");
                myAdapter.notifyDataSetChanged();
                listView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                p++;
                initData(p+"");
                myAdapter.notifyDataSetChanged();
                listView.onRefreshComplete();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),InvesmentDetialActivity.class);
                InvesmentSimpleInfo info=invesmentSimpleInfos.get((int) id);
                intent.putExtra("id",info.getUser_id());
                startActivity(intent);
            }
        });
    }

    private void initData(String p) {
        BaseParams baseParams=new BaseParams("index/investorlist");
        baseParams.addParams("p",p);
        baseParams.addSign();
        baseParams.getRequestParams().setCacheMaxAge(30*60*1000);
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
                jsonParser(res);
                initView();

            }

            @Override
            public boolean onCache(String result) {
                res=result;

                return true;
            }
        });
    }

    private void jsonParser(String result){
        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                Url.prePic=jsonObject1.getString("prefixPic");
                JSONArray issues=jsonObject1.getJSONArray("investorList");
                for (int i=0;i<issues.length();i++){
                    JSONObject jsonObject2=issues.getJSONObject(i);
                    InvesmentSimpleInfo invesmentSimpleInfo=new InvesmentSimpleInfo();
                    invesmentSimpleInfo.setUser_id(jsonObject2.getString("user_id"));
                    invesmentSimpleInfo.setLogo(jsonObject2.getString("logo"));
                    invesmentSimpleInfo.setIdentity_cat(jsonObject2.getString("identity_cat"));
                    invesmentSimpleInfo.setCompany_name(jsonObject2.getString("company_name"));
                    invesmentSimpleInfo.setCompany_intro(jsonObject2.getString("company_intro"));
                    invesmentSimpleInfos.add(invesmentSimpleInfo);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        listView.setAdapter(myAdapter);
    }

    class MyIssueAdapter extends BaseAdapter {
        List<InvesmentSimpleInfo> list;
        Context context;

        public MyIssueAdapter(Context context, List<InvesmentSimpleInfo> list){
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
                convertView=LayoutInflater.from(context).inflate(R.layout.item_cplistfragment,null);
                viewHolder=new ViewHolder();
                viewHolder.icon= (ImageView) convertView.findViewById(R.id.imageView_cplist_listac);
                viewHolder.name= (TextView) convertView.findViewById(R.id.name_item_cp_listac);
                viewHolder.intro= (TextView) convertView.findViewById(R.id.introuduction_item_cp_listac);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            InvesmentSimpleInfo invesmentSimpleInfo=list.get(position);
            Picasso.with(getActivity())
                    .load(Url.prePic+invesmentSimpleInfo.getLogo())
                    .placeholder(R.drawable.default_icon)
                    .into(viewHolder.icon);
            viewHolder.name.setText(invesmentSimpleInfo.getCompany_name());
            viewHolder.intro.setText(invesmentSimpleInfo.getCompany_intro());
            return convertView;
        }
        class ViewHolder{
            ImageView icon;
            TextView name;
            TextView intro;
        }
    }
}
