package com.view.s4server;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
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
public class CPListFragment extends BaseFragment {

    View view;

    PullListView listView;

    TextView title;
    ImageView back;
    ImageView search;

//    TextView showall;

    int p=1;

    List<CPSimpleInfo> cpSimpleInfos;

    private CpAdapter adapter;

    private Dialog dialog;

    private Button refreash;

    boolean recommend=false;

    private String all_url="index/cplist";
    private String tj_url="/ret/2";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        cpSimpleInfos=new ArrayList<>();
        Bundle bundle=getArguments();
        if (bundle!=null){
            recommend=bundle.getBoolean("recommend",false);
        }
        adapter=new CpAdapter(getActivity(),cpSimpleInfos);
        super.onCreate(savedInstanceState);

    }

    private void initData(String p) {
        BaseParams baseParams=null;
        if (recommend){
            baseParams=new BaseParams(all_url+tj_url);

        }else {
            baseParams =new BaseParams(all_url);
        }
        baseParams.addParams("p",p);
        baseParams.addSign();
        baseParams.getRequestParams().setCacheMaxAge(10*60*1000);
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                MyLog.i("cplist success=="+result);
                getcplistparser(result);

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
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
                listView.onRefreshComplete();
                adapter.notifyDataSetChanged();

            }
        });
    }

    private void initListView() {

        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            //下拉刷新
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                MyLog.i("下拉刷新");
                cpSimpleInfos.clear();
                p=1;
                initData(p+"");
            }
            //上拉加载
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                MyLog.i("上拉加载");
                p++;
                initData(p+"");
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),CPDetailActivity.class);
                CPSimpleInfo cpSimpleInfo=cpSimpleInfos.get((int) id);
                intent.putExtra("user_id",cpSimpleInfo.getUser_id());
                intent.putExtra("identity_cat",cpSimpleInfo.getIdentity_cat());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_list,null);
        listView= (PullListView) view.findViewById(R.id.pullToRefresh_fglist);
        title= (TextView) view.findViewById(R.id.title_titlebar);
        back= (ImageView) view.findViewById(R.id.back_titlebar);
        search= (ImageView) view.findViewById(R.id.search_titlebar);

//        showall= (TextView) view.findViewById(R.id.showall_listfragment);


        listView.setAdapter(adapter);



        dialog=showLoadingDialog(getActivity());
        refreash= (Button) view.findViewById(R.id.refeash_list);
        refreash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setVisibility(View.VISIBLE);
                dialog.show();
                initData("1");
            }
        });
        initData("1");
        initListView();
        return view;
    }
    class CpAdapter extends BaseAdapter{

        List<CPSimpleInfo> list;
        Context context;
        public CpAdapter(Context context,List<CPSimpleInfo> list){
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
            CPSimpleInfo cpinfo=list.get(position);
            Picasso.with(getActivity())
                    .load(Url.prePic+cpinfo.getLogo())
                    .into(viewHolder.icon);
            viewHolder.name.setText(cpinfo.getCompany_name());
            viewHolder.intro.setText(cpinfo.getCompany_intro());
            return convertView;
        }

        class ViewHolder{
            ImageView icon;
            TextView name;
            TextView intro;
        }
    }
    public void getcplistparser(String result){
        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                Url.prePic=jsonObject1.getString("prefixPic");
                JSONArray ja=jsonObject1.getJSONArray("cpList");
                MyLog.i("cplist size=="+ja.length());
                if (ja.length()==0){
                    if (recommend){
//                        addMessage();
                        TextView testview=new TextView(getActivity());
                        testview.setText("查看全部");
                        testview.setGravity(Gravity.CENTER);
                        testview.setTextSize(15);
                        listView.addView(testview);
                    }else {
                        CusToast.showToast(getActivity(), "没有更多开发者信息", Toast.LENGTH_SHORT);
                    }
                }
                for (int i=0;i<ja.length();i++){
                    JSONObject jsonObject2=ja.getJSONObject(i);
                    CPSimpleInfo cpSimpleInfo=new CPSimpleInfo();
                    cpSimpleInfo.setUser_id(jsonObject2.getString("user_id"));
                    cpSimpleInfo.setIdentity_cat(jsonObject2.getString("identity_cat"));
                    cpSimpleInfo.setLogo(jsonObject2.getString("logo"));
                    cpSimpleInfo.setCompany_intro(jsonObject2.getString("company_intro"));
                    cpSimpleInfo.setCompany_name(jsonObject2.getString("company_name"));
                    cpSimpleInfos.add(cpSimpleInfo);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
