package com.view.index;


import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.app.view.HorizontalListView;

import com.squareup.picasso.Picasso;
import com.test4s.jsonparser.IndexJsonParser;
import com.test4s.net.BaseParams;
import com.view.activity.ListActivity;
import com.test4s.adapter.CP_HL_Adapter;
import com.test4s.adapter.IP_HL_Adapter;
import com.test4s.adapter.Invesment_HL_Adapter;
import com.test4s.adapter.OutSource_HL_Adapter;
import com.test4s.gdb.CP;
import com.view.s4server.CPDetailActivity;
import com.test4s.myapp.R;
import com.test4s.net.IndexParser;
import com.test4s.jsonparser.GameJsonParser;
import com.test4s.net.Url;
import com.view.s4server.CPSimpleInfo;
import com.view.s4server.IPDetailActivity;
import com.view.s4server.IPSimpleInfo;
import com.view.s4server.InvesmentDetialActivity;
import com.view.s4server.IssueDetailActivity;
import com.view.s4server.OutSourceActivity;

import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

/**
 * Created by Administrator on 2015/12/7.
 */
public class IndexFragment extends Fragment implements View.OnClickListener{

    List<HorizontalListView> horizonlist;

    TextView tj1;
    TextView tj2;
    TextView tj3;
    TextView tj4;
    TextView tj5;

    AdapterView.OnItemClickListener listener1;
    AdapterView.OnItemClickListener listener2;
    AdapterView.OnItemClickListener listener3;
    AdapterView.OnItemClickListener listener4;
    AdapterView.OnItemClickListener listener5;

    List<LinearLayout> content;

    LinearLayout continer;

    private ViewPager viewPager;

    List<ImageView> imageViewList;
    LinearLayout whiteDots;
    List<IndexAdverts> indexAdvertses;

    IndexJsonParser indexJsonParser;
    int currentItem;

    float density;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_index,null);


        viewPager= (ViewPager) view.findViewById(R.id.viewpager_index);
        whiteDots= (LinearLayout) view.findViewById(R.id.whitedot_linear);
        continer= (LinearLayout) view.findViewById(R.id.contianer_index);

        content=new ArrayList<>();

        view.findViewById(R.id.fx_fg_index).setOnClickListener(this);
        view.findViewById(R.id.ip_fg_index).setOnClickListener(this);
        view.findViewById(R.id.tz_fg_index).setOnClickListener(this);
        view.findViewById(R.id.wb_fg_index).setOnClickListener(this);



        indexJsonParser=IndexJsonParser.getInstance();

        getDensity();

        initData();

        return  view;
    }

    public void getDensity(){
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
    }

    private void initData() {
        BaseParams params=new BaseParams("index/index.html");

        params.addSign();
        params.getRequestParams().setCacheMaxAge(1000*60*30);

        Callback.Cancelable cancelable= x.http().post(params.getRequestParams(), new Callback.CacheCallback<String>() {
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
                MyLog.i("缓存");
                this.result = result;
                return true; // true: 信任缓存数据, 不在发起网络请求; false不信任缓存数据.
            }

            @Override
            public void onSuccess(String result) {
                MyLog.i("网络访问");
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
                indexJsonParser.jsonParser(result);
                indexAdvertses=indexJsonParser.indexAdvertses;
                initViewPager();
                initView();

            }
        });
    }



    private void initView() {
        MyLog.i("initView1");
        Map<String,List> map=indexJsonParser.map;
        final List<String> order=indexJsonParser.order;
        List<String> names=indexJsonParser.names;
        MyLog.i("for1");
        MyLog.i("map Size=="+map.size());
        for (int i=0;i<map.size();i++){
            MyLog.i("addView1");
            ViewHolder viewHolder=new ViewHolder();
            LinearLayout layout= (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.layout_index_horlistview,null);
            MyLog.i("addView2");
            viewHolder.listView= (HorizontalListView) layout.findViewById(R.id.list_horizontalListview);
            viewHolder.tj= (TextView) layout.findViewById(R.id.tj_horizontalListview);
            viewHolder.more= (TextView) layout.findViewById(R.id.more_horizontalListview);
            viewHolder.tj.setText(names.get(i));
            MyLog.i("addView3");
            layout.setTag(viewHolder);
            MyLog.i("addView4");
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            continer.addView(layout,layoutParams);
            MyLog.i("addView5");
            content.add(layout);
            MyLog.i("addView6");
        }
        MyLog.i("initView2");
        for (int i=0;i<map.size();i++){
            ViewHolder viewHolder= (ViewHolder) content.get(i).getTag();
            if (order.get(i).equals("ip")){
                ArrayList<IPSimpleInfo> ipSimpleInfos= (ArrayList<IPSimpleInfo>) map.get(order.get(i));
                IP_HL_Adapter adapter=new IP_HL_Adapter(getActivity(),ipSimpleInfos);
                viewHolder.listView.setAdapter(adapter);
            }else {
                ArrayList<IndexItemSipleInfo> indexSimpleinfos= (ArrayList<IndexItemSipleInfo>) map.get(order.get(i));
                CP_HL_Adapter adapter=new CP_HL_Adapter(getActivity(),indexSimpleinfos);
                viewHolder.listView.setAdapter(adapter);
            }
        }
        MyLog.i("initView3");
        for (int i=0;i<map.size();i++){
            ViewHolder viewHolder= (ViewHolder) content.get(i).getTag();
            final int j=i;
            viewHolder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), ListActivity.class);
                    switch (order.get(j)){
                        case "ip":
                            intent.putExtra("tag",ListActivity.IP_TAG);
                            break;
                        case "cp":
                            intent.putExtra("tag",ListActivity.CP_TAG);
                            break;
                        case "issue":
                            intent.putExtra("tag",ListActivity.Issue_TAG);
                            break;
                        case "outsource":
                            intent.putExtra("tag",ListActivity.OutSource_TAG);
                            break;
                        case "investor":
                            intent.putExtra("tag",ListActivity.Invesment_TAG);
                            break;
                    }
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                }
            });
            final String method_name=order.get(i);
            if (method_name.equals("ip")){
                final ArrayList<IPSimpleInfo> ipSimpleInfos= (ArrayList<IPSimpleInfo>) map.get(method_name);
                viewHolder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent=new Intent(getActivity(), IPDetailActivity.class);
                        IPSimpleInfo ipSimpleInfo=ipSimpleInfos.get(position);
                        intent.putExtra("id",ipSimpleInfo.getId());
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                    }
                });
            }else {
                final ArrayList<IndexItemSipleInfo> infos= (ArrayList<IndexItemSipleInfo>) map.get(method_name);
                viewHolder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent=null;
                        switch (method_name){
                            case "cp":
                                intent=new Intent(getActivity(), CPDetailActivity.class);
                                break;
                            case "issue":
                                intent=new Intent(getActivity(), IssueDetailActivity.class);
                                break;
                            case "outsource":
                                intent=new Intent(getActivity(), OutSourceActivity.class);

                                break;
                            case "investor":
                                intent=new Intent(getActivity(), InvesmentDetialActivity.class);
                                break;
                        }
                        IndexItemSipleInfo ipSimpleInfo=infos.get(position);
                        intent.putExtra("user_id",ipSimpleInfo.getUser_id());
                        intent.putExtra("identity_cat",ipSimpleInfo.getIdentity_cat());
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                    }
                });

            }

        }

        MyLog.i("initView");
    }
    class ViewHolder{
        HorizontalListView listView;
        TextView tj;
        TextView more;
    }



    private void initViewPager() {

        MyLog.i("initViewPager");


        imageViewList=new ArrayList<>();


        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(viewPager.getLayoutParams().width,viewPager.getLayoutParams().height);
        LinearLayout.LayoutParams params1=new LinearLayout.LayoutParams((int)(9*density),(int)(9*density));

        for (int i=0;i<indexAdvertses.size();i++){
            ImageView imageView=new ImageView(getActivity());
            imageView.setLayoutParams(params);

            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViewList.add(imageView);
            MyLog.i("addimageView");
            if (i>0){
                params1.leftMargin=(int)(12.66*density);
            }
            ImageView dot=new ImageView(getActivity());
            dot.setImageResource(R.drawable.whitedotselected);
            dot.setLayoutParams(params1);
            whiteDots.addView(dot);
            MyLog.i("imageUrl==="+Url.prePic+indexAdvertses.get(i).getAdvert_pic());
            Picasso.with(getActivity())
                    .load(Url.prePic+indexAdvertses.get(i).getAdvert_pic())
                    .into(imageView);
        }
        setDot(0);
        MyLog.i("setAdapter");
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imageViewList.size();
            }


            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView=imageViewList.get(position);
                container.addView(imageView);
                MyLog.i("添加imageview");
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(imageViewList.get(position));
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true){
                        Thread.sleep(5*1000);
                        handler.sendEmptyMessage(0);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    android.os.Handler handler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    currentItem=viewPager.getCurrentItem();
                    currentItem++;
                    if (currentItem==indexAdvertses.size()){
                        currentItem=0;
                    }
                    viewPager.setCurrentItem(currentItem);
                    break;
            }
        }
    };

    private void setDot(int position) {
        for (int i=0;i<whiteDots.getChildCount();i++){
            if (i==position){
                whiteDots.getChildAt(i).setSelected(true);
            }else {
                whiteDots.getChildAt(i).setSelected(false);
            }
        }

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

        }
    }
}
