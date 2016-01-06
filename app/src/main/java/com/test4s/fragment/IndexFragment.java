package com.test4s.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.app.view.HorizontalListView;
import com.test4s.activity.DetailActivity;
import com.test4s.activity.ListActivity;
import com.test4s.adapter.HorizontalListAdapter;
import com.test4s.adapter.IconUrl;
import com.test4s.myapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/7.
 */
public class IndexFragment extends Fragment implements View.OnClickListener{

    HorizontalListView cpList;
    HorizontalListView ipList;
    HorizontalListView wbList;
    HorizontalListView tzList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_index,null);
        initView(view);

        return  view;
    }

    private void initView(View view) {
        cpList= (HorizontalListView) view.findViewById(R.id.list_cp);
        ipList= (HorizontalListView) view.findViewById(R.id.list_ip);
        wbList= (HorizontalListView) view.findViewById(R.id.list_wb);
        tzList= (HorizontalListView) view.findViewById(R.id.list_tz);

        IconUrl iconUrl=new IconUrl();
        iconUrl.setName("百度");
        iconUrl.setUrl("https://www.baidu.com/img/bd_logo1.png");
        List<IconUrl> list_iconUrl=new ArrayList<>();
        for (int i=0;i<10;i++){
            list_iconUrl.add(iconUrl);
        }
        HorizontalListAdapter myAdaper=new HorizontalListAdapter(getContext(),list_iconUrl);
        cpList.setAdapter(myAdaper);
        ipList.setAdapter(myAdaper);
        wbList.setAdapter(myAdaper);
        tzList.setAdapter(myAdaper);

        view.findViewById(R.id.ip_fg_index).setOnClickListener(this);

        cpList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), DetailActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ip_fg_index:
                Intent intent=new Intent(getActivity(), ListActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                break;
            case R.id.wb_fg_index:
                break;
            case R.id.tz_fg_index:
                break;
            case R.id.fx_fg_index:
                break;
            case R.id.pc_fg_index:
                break;
            case R.id.bg_fg_index:
                break;
            case R.id.more_cp:
                break;
            case R.id.more_ip:
                break;
            case R.id.more_wb:
                break;
            case R.id.more_tz:
                break;

        }
    }
}
