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
public class IssueListFragment extends BaseFragment{
    View view;

    List<IssueSimpleInfo> issueSimpleInfos;
    PullListView listView;
    MyIssueAdapter myAdapter;


    private TextView title;
    private ImageView search;
    private ImageView back;

    int p=1;
    private Dialog dialog;
    private Button refreash;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        dialog=showLoadingDialog(getActivity());

        issueSimpleInfos=new ArrayList<>();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_list,null);
        listView= (PullListView) view.findViewById(R.id.pullToRefresh_fglist);
        myAdapter=new MyIssueAdapter(getActivity(),issueSimpleInfos);
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
                issueSimpleInfos.clear();
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
                Intent intent=new Intent(getActivity(),IssueDetailActivity.class);
                IssueSimpleInfo info=issueSimpleInfos.get((int) id);
                intent.putExtra("user_id",info.getUser_id());
                intent.putExtra("identity_cat",info.getIdentity_cat());
                startActivity(intent);
            }
        });
    }

    private void initData(String p) {
        BaseParams baseParams=new BaseParams("index/issuelist");
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

                JSONArray issues=jsonObject1.getJSONArray("issueList");
                MyLog.i("issues size=="+issues.length());

                if (issues.length()==0){
                    CusToast.showToast(getActivity(),"没有更多发行信息", Toast.LENGTH_SHORT);
                }
                for (int i=0;i<issues.length();i++){
                    JSONObject jsonObject2=issues.getJSONObject(i);
                    IssueSimpleInfo issueSimpleInfo=new IssueSimpleInfo();
                    issueSimpleInfo.setUser_id(jsonObject2.getString("user_id"));
                    issueSimpleInfo.setLogo(jsonObject2.getString("logo"));
                    issueSimpleInfo.setIdentity_cat(jsonObject2.getString("identity_cat"));
                    issueSimpleInfo.setCompany_name(jsonObject2.getString("company_name"));
                    issueSimpleInfo.setCompany_intro(jsonObject2.getString("company_intro"));
                    issueSimpleInfo.setArea_name(jsonObject2.getString("area_name"));
                    issueSimpleInfo.setBusine_cat_name(jsonObject2.getString("busine_cat_name"));
                    issueSimpleInfo.setCoop_cat_name(jsonObject2.getString("coop_cat_name"));
                    issueSimpleInfos.add(issueSimpleInfo);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    class MyIssueAdapter extends BaseAdapter{
        List<IssueSimpleInfo> list;
        Context context;

        public MyIssueAdapter(Context context, List<IssueSimpleInfo> list){
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
            IssueSimpleInfo issueSimpleInfo=list.get(position);
            Picasso.with(getActivity())
                    .load(Url.prePic+issueSimpleInfo.getLogo())
                    .placeholder(R.drawable.default_icon)
                    .into(viewHolder.icon);
            viewHolder.name.setText(issueSimpleInfo.getCompany_name());
//            String area="";
//            for (int i=0;i<areadrr.size();i++){
//                Map<String,String> map=areadrr.get(i);
//                if (map.get("id").equals(issueSimpleInfo.getArea_id())){
//                    area=map.get("name");
//                }
//            }
            String mess="所在区域: "+issueSimpleInfo.getArea_name()+"\n业务类型: "+issueSimpleInfo.getBusine_cat_name()+"\n合作类型： "+issueSimpleInfo.getCoop_cat_name();
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
