package com.view.s4server;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.app.tools.MyLog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
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
public class IPListFragment extends BaseFragment{
    View view;

    PullToRefreshListView listView;

    int p=1;

    List<IPSimpleInfo> ipSimpleInfos;

    MyIpListAdapter myAdapter;

    private Dialog dialog;
    private Button refreash;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        dialog=showLoadingDialog(getActivity());

        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_list,null);
        listView= (PullToRefreshListView) view.findViewById(R.id.pullToRefresh_fglist);
        ipSimpleInfos=new ArrayList<>();
        myAdapter=new MyIpListAdapter(getActivity(),ipSimpleInfos);
        refreash= (Button) view.findViewById(R.id.refeash_list);
        refreash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setVisibility(View.VISIBLE);

                dialog.show();
                initData("1");
            }
        });
        initListView();
        initData(1+"");
        return view;
    }

    private void initData(String p) {
        BaseParams baseParams=new BaseParams("index/iplist");
        baseParams.addParams("p",p);
        baseParams.addSign();
        baseParams.getRequestParams().setCacheMaxAge(10*60*1000);
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            String res;
            @Override
            public void onSuccess(String result) {
                res=result;
                MyLog.i("onsuccess=="+result);

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
                parser(res);
                myAdapter.notifyDataSetChanged();
                listView.onRefreshComplete();
                if (dialog.isShowing()){
                    dialog.dismiss();
                }

            }

        });

    }
    private  void parser(String res){
        try {
            JSONObject jsonObject=new JSONObject(res);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){

                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                Url.prePic=jsonObject1.getString("prefixPic");
                JSONArray jsonArray=jsonObject1.getJSONArray("ipList");
                if (jsonArray.length()==0){
                    CusToast.showToast(getActivity(),"没有更多信息", Toast.LENGTH_SHORT);
                }
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject2=jsonArray.getJSONObject(i);
                    IPSimpleInfo ipSimpleInfo=new IPSimpleInfo();
                    ipSimpleInfo.setId(jsonObject2.getString("id"));
                    ipSimpleInfo.setLogo(jsonObject2.getString("ip_logo"));
                    ipSimpleInfo.setIp_name(jsonObject2.getString("ip_name"));
                    ipSimpleInfo.setIp_cat(jsonObject2.getString("ip_cat"));
//                    ipSimpleInfo.setCompany_name(jsonObject2.getString("company_name"));
                    ipSimpleInfo.setIp_style(jsonObject2.getString("ip_style"));
                    ipSimpleInfo.setUthority(jsonObject2.getString("uthority"));
                    ipSimpleInfos.add(ipSimpleInfo);

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initListView() {
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                ipSimpleInfos.clear();
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
                Intent intent=new Intent(getActivity(),IPDetailActivity.class);
                IPSimpleInfo ipSimpleInfo=ipSimpleInfos.get((int) id);
                intent.putExtra("id",ipSimpleInfo.getId());
                startActivity(intent);
            }
        });
        listView.setAdapter(myAdapter);
    }



}
