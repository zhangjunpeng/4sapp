package com.test4s.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.app.tools.MyLog;
import com.app.view.HorizontalListView;

import com.test4s.activity.CPDetailActivity;
import com.test4s.activity.ListActivity;
import com.test4s.adapter.CP_HL_Adapter;
import com.test4s.adapter.IP_HL_Adapter;
import com.test4s.adapter.Invesment_HL_Adapter;
import com.test4s.adapter.OutSource_HL_Adapter;
import com.test4s.gdb.CP;
import com.test4s.myapp.R;
import com.test4s.net.IndexParams;
import com.test4s.net.IndexParser;
import com.test4s.net.JsonParser;
import com.test4s.net.Url;

import org.xutils.common.Callback;
import org.xutils.x;

/**
 * Created by Administrator on 2015/12/7.
 */
public class IndexFragment extends Fragment implements View.OnClickListener{

    HorizontalListView cpList;
    HorizontalListView ipList;
    HorizontalListView wbList;
    HorizontalListView tzList;

    IndexParser indexParser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_index,null);
        initData(view);

        cpList= (HorizontalListView) view.findViewById(R.id.list_cp);
        ipList= (HorizontalListView) view.findViewById(R.id.list_ip);
        wbList= (HorizontalListView) view.findViewById(R.id.list_wb);
        tzList= (HorizontalListView) view.findViewById(R.id.list_tz);

        return  view;
    }

    private void initData(final View view) {
        IndexParams params=new IndexParams();
        params.setCacheMaxAge(1000*60*30);
//        params.wd="xUtils";
        Callback.Cancelable cancelable= x.http().post(params, new Callback.CacheCallback<String>() {
            private String result=null;
            @Override
            public boolean onCache(String result) {
                // 得到缓存数据, 缓存过期后不会进入这个方法.
                // 如果服务端没有返回过期时间, 参考params.setCacheMaxAge(maxAge)方法.
                //
                // * 客户端会根据服务端返回的 header 中 max-age 或 expires 来确定本地缓存是否给 onCache 方法.
                //   如果服务端没有返回 max-age 或 expires, 那么缓存将一直保存, 除非这里自己定义了返回false的
                //   逻辑, 那么xUtils将请求新数据, 来覆盖它.
                //
                // * 如果信任该缓存返回 true, 将不再请求网络;
                //   返回 false 继续请求网络, 但会在请求头中加上ETag, Last-Modified等信息,
                //   如果服务端返回304, 则表示数据没有更新, 不继续加载数据.
                //
                this.result = result;
                return true; // true: 信任缓存数据, 不在发起网络请求; false不信任缓存数据.
            }

            @Override
            public void onSuccess(String result) {
                this.result=result;
            }


            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                MyLog.i("xutils~~~~~~~~~~~~index"+result);
                indexParser= JsonParser.getIndexParser(result);
                Url.prePic=indexParser.getData().getPrefixPic();

                initView(view);
            }
        });
    }

    private void initView(View view) {


        CP_HL_Adapter cp_hl_adapter=new CP_HL_Adapter(getContext(),indexParser.getData().getCpList());
        IP_HL_Adapter ip_hl_adapter=new IP_HL_Adapter(getContext(),indexParser.getData().getIpList());
        Invesment_HL_Adapter invesmnet_hl_adapter=new Invesment_HL_Adapter(getContext(),indexParser.getData().getInvestorList());
        OutSource_HL_Adapter outSource_hl_adapter=new OutSource_HL_Adapter(getContext(),indexParser.getData().getOutSourceList());
        cpList.setAdapter(cp_hl_adapter);
        ipList.setAdapter(ip_hl_adapter);
        wbList.setAdapter(invesmnet_hl_adapter);
        tzList.setAdapter(outSource_hl_adapter);

        view.findViewById(R.id.ip_fg_index).setOnClickListener(this);

        cpList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CP cp=indexParser.getData().getCpList().get(position);

                Intent intent=new Intent(getActivity(), CPDetailActivity.class);
                intent.putExtra("id",cp.getUser_id());
                intent.putExtra("cat",cp.getIdentity_cat());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.ip_fg_index:
                intent=new Intent(getActivity(), ListActivity.class);
                intent.putExtra("tag",ListActivity.IP_TAG);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                break;
            case R.id.wb_fg_index:
                intent=new Intent(getActivity(), ListActivity.class);
                intent.putExtra("tag",ListActivity.OutSource_TAG);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                break;
            case R.id.tz_fg_index:
                intent=new Intent(getActivity(), ListActivity.class);
                intent.putExtra("tag",ListActivity.Invesment_TAG);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                break;
            case R.id.fx_fg_index:
                intent=new Intent(getActivity(), ListActivity.class);
                intent.putExtra("tag",ListActivity.Issue_TAG);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                break;
            case R.id.pc_fg_index:

                break;
            case R.id.bg_fg_index:

                break;
            case R.id.more_cp:
                intent=new Intent(getActivity(), ListActivity.class);
                intent.putExtra("tag",ListActivity.CP_TAG);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                break;
            case R.id.more_ip:
                intent=new Intent(getActivity(), ListActivity.class);
                intent.putExtra("tag",ListActivity.IP_TAG);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                break;
            case R.id.more_wb:
                intent=new Intent(getActivity(), ListActivity.class);
                intent.putExtra("tag",ListActivity.OutSource_TAG);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                break;
            case R.id.more_tz:
                intent=new Intent(getActivity(), ListActivity.class);
                intent.putExtra("tag",ListActivity.Invesment_TAG);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                break;

        }
    }
}
