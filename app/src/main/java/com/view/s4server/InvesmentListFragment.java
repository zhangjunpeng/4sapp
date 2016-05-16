package com.view.s4server;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.picasso.Picasso;
import com.test4s.myapp.BaseFragment;
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
public class InvesmentListFragment extends BaseFragment{
    View view;

    List<InvesmentSimpleInfo> invesmentSimpleInfos;
    PullToRefreshListView listView;
    MyIssueAdapter myAdapter;


    int p=1;

    private Dialog dialog;
    private Button refreash;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        dialog=showLoadingDialog(getActivity());

        invesmentSimpleInfos=new ArrayList<>();
        myAdapter=new MyIssueAdapter(getActivity(),invesmentSimpleInfos);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_list,null);
        listView= (PullToRefreshListView) view.findViewById(R.id.pullToRefresh_fglist);
        listView.setAdapter(myAdapter);
        refreash= (Button) view.findViewById(R.id.refeash_list);
        refreash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setVisibility(View.VISIBLE);

                dialog.show();
                initData("1");
            }
        });
        initLisener();
        initData("1");

        return view;
    }

    private void initLisener() {
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                invesmentSimpleInfos.clear();
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
                Intent intent=new Intent(getActivity(),InvesmentDetialActivity.class);
                InvesmentSimpleInfo info=invesmentSimpleInfos.get((int) id);
                intent.putExtra("user_id",info.getUser_id());
                intent.putExtra("identity_cat",info.getIdentity_cat());
                startActivity(intent);
            }
        });
    }

    private void initData(String p) {

        BaseParams baseParams=new BaseParams("index/investorlist");
        baseParams.addParams("p",p);
        baseParams.addSign();
        baseParams.getRequestParams().setCacheMaxAge(30*60*1000);
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            String res;

            @Override
            public void onSuccess(String result) {
                res=result;

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                listView.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                jsonParser(res);
                myAdapter.notifyDataSetChanged();
                listView.onRefreshComplete();
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
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
                if (issues.length()==0){
                    CusToast.showToast(getActivity(),"没有更多信息", Toast.LENGTH_SHORT);
                }
                for (int i=0;i<issues.length();i++){
                    JSONObject jsonObject2=issues.getJSONObject(i);
                    InvesmentSimpleInfo invesmentSimpleInfo=new InvesmentSimpleInfo();
                    invesmentSimpleInfo.setUser_id(jsonObject2.getString("user_id"));
                    invesmentSimpleInfo.setLogo(jsonObject2.getString("logo"));
                    invesmentSimpleInfo.setIdentity_cat(jsonObject2.getString("identity_cat"));
                    invesmentSimpleInfo.setCompany_name(jsonObject2.getString("company_name"));
                    invesmentSimpleInfo.setCompany_intro(jsonObject2.getString("company_intro"));
                    invesmentSimpleInfo.setArea_name(jsonObject2.getString("area_name"));
                    invesmentSimpleInfo.setInvest_cat_name(jsonObject2.getString("invest_cat_name"));
                    invesmentSimpleInfo.setInvest_stage_name(jsonObject2.getString("invest_stage_name"));
                    invesmentSimpleInfos.add(invesmentSimpleInfo);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                convertView= LayoutInflater.from(context).inflate(R.layout.item_iplistfragment,null);
                viewHolder=new ViewHolder();
                viewHolder.icon= (ImageView) convertView.findViewById(R.id.imageView_iplist);
                viewHolder.name= (TextView) convertView.findViewById(R.id.name_item_iplist);
                viewHolder.intro= (TextView) convertView.findViewById(R.id.introuduction_item_iplist);
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
            String mess="所在区域: "+invesmentSimpleInfo.getArea_name()+"\n机构类型: "+invesmentSimpleInfo.getInvest_cat_name()+"\n投资阶段： "+invesmentSimpleInfo.getInvest_stage_name();
            viewHolder.intro.setText(mess);
            return convertView;
        }
        class ViewHolder{
            ImageView icon;
            TextView name;
            TextView intro;
        }
    }
}
