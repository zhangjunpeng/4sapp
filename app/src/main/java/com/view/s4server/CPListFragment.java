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
import com.test4s.jsonparser.CPJsonParser;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;

import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/18.
 */
public class CPListFragment extends Fragment {

    View view;

    PullToRefreshListView listView;

    TextView title;
    ImageView back;
    ImageView search;

    List<CPSimpleInfo> cpSimpleInfos;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cpSimpleInfos=new ArrayList<>();
        int p=1;
        initData(p+"");
    }

    private void initData(String p) {
        BaseParams baseParams=new BaseParams("index/cplist");
        baseParams.addParams("p",p);
        baseParams.addSign();
        baseParams.getRequestParams().setCacheMaxAge(10*60*1000);
        x.http().post(baseParams.getRequestParams(), new Callback.CacheCallback<String>() {
            String res="";
            @Override
            public void onSuccess(String result) {
                MyLog.i("cplist success=="+result);
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
                cpSimpleInfos= CPJsonParser.getInstance().getcplistparser(res);
                initListView();
            }

            @Override
            public boolean onCache(String result) {
                MyLog.i("cplis cache==="+result);
                res=result;
                return true;
            }
        });
    }

    private void initListView() {

        listView.setAdapter(new CpAdapter(getActivity(),cpSimpleInfos));


        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            //下拉刷新
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                MyLog.i("下拉刷新");
            }

            //上拉加载
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                MyLog.i("上拉加载");
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),CPDetailActivity.class);
                CPSimpleInfo cpSimpleInfo=cpSimpleInfos.get((int) id);
                intent.putExtra("id",cpSimpleInfo.getUser_id());
                intent.putExtra("identity_cat",cpSimpleInfo.getIdentity_cat());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_cplist,null);
        listView= (PullToRefreshListView) view.findViewById(R.id.listview_cplist);
        title= (TextView) view.findViewById(R.id.title_titlebar);
        back= (ImageView) view.findViewById(R.id.back_titlebar);
        search= (ImageView) view.findViewById(R.id.search_titlebar);


        listView.setMode(PullToRefreshBase.Mode.BOTH);
//        initListView();
        initData("1");
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
}
