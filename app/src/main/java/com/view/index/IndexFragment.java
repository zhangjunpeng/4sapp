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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.app.tools.Timer;
import com.app.view.HorizontalListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.squareup.picasso.Picasso;
import com.test4s.gdb.DaoSession;
import com.test4s.gdb.GameInfoDao;
import com.test4s.gdb.IP;
import com.test4s.gdb.IPDao;
import com.test4s.gdb.IndexAdvert;
import com.test4s.gdb.IndexAdvertDao;
import com.test4s.gdb.IndexItemInfo;
import com.test4s.gdb.IndexItemInfoDao;
import com.test4s.gdb.Order;
import com.test4s.gdb.OrderDao;
import com.test4s.jsonparser.IndexJsonParser;
import com.test4s.myapp.MyApplication;
import com.test4s.net.BaseParams;
import com.view.activity.ListActivity;
import com.test4s.adapter.CP_HL_Adapter;
import com.test4s.adapter.IP_HL_Adapter;
import com.view.s4server.CPDetailActivity;
import com.test4s.myapp.R;
import com.test4s.net.Url;
import com.view.s4server.IPDetailActivity;
import com.view.s4server.IPSimpleInfo;
import com.view.s4server.InvesmentDetialActivity;
import com.view.s4server.IssueDetailActivity;
import com.view.s4server.OutSourceActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.dao.query.Query;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

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

    IndexJsonParser indexJsonParser;
    static  Integer currentItem=0;

    Thread thread;

    float density;

    private static boolean first=true;
    private Map<String, List> map;
    private List<Order> orders;
    private List<IndexAdvert> indexAdvertses;

    private DaoSession daoSession;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (thread==null){
            thread=new Timer(handler);
            thread.start();
        }

        daoSession= MyApplication.daoSession;

        map=new HashMap<>();
        orders=new ArrayList<>();
        indexAdvertses=new ArrayList<>();
        imageViewList=new ArrayList<>();


        super.onCreate(savedInstanceState);
    }

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
//        indexJsonParser=IndexJsonParser.getInstance();
        getDataFromDB();
        initData();

        getDensity();
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

        x.http().post(params.getRequestParams(), new Callback.CommonCallback<String>() {
            private String result=null;
            private boolean success=true;

            @Override
            public void onSuccess(String result) {
                MyLog.i("网络访问");
                this.result=result;
            }


            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                success=false;
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                MyLog.i("~~~~~~~~index"+result);
//                indexJsonParser.jsonParser(result);
//                indexAdvertses=indexJsonParser.indexAdvertses;
                if (success){
                    MyLog.i("网络获取数据");
                    deletAll();
                    jsonParser(result);
                    initView();
                }else {

                }

            }
        });
    }



    private void initView() {
        MyLog.i("initView1");
//        map=indexJsonParser.map;
//        order=indexJsonParser.order;
//        names=indexJsonParser.names;
        MyLog.i("map Size=="+map.size());
        content.clear();
        continer.removeAllViews();
        viewPager.removeAllViews();
        initViewPager();

        for (int i=0;i<map.size();i++){
//            MyLog.i("addView1");
            ViewHolder viewHolder=new ViewHolder();
            LinearLayout layout= (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.layout_index_horlistview,null);
//            MyLog.i("addView2");
            viewHolder.listView= (HorizontalScrollView) layout.findViewById(R.id.list_horizontalListview);


            OverScrollDecoratorHelper.setUpOverScroll(viewHolder.listView);
            viewHolder.listView.setHorizontalScrollBarEnabled(false);

            viewHolder.tj= (TextView) layout.findViewById(R.id.tj_horizontalListview);
            viewHolder.more= (TextView) layout.findViewById(R.id.more_horizontalListview);
            Order order=orders.get(i);
            viewHolder.tj.setText(order.getName());
//            MyLog.i("addView3");
            layout.setTag(viewHolder);
//            MyLog.i("addView4");
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            continer.addView(layout,layoutParams);
//            MyLog.i("addView5");
            content.add(layout);
//            MyLog.i("addView6");
        }
        MyLog.i("initView2");
        for (int i=0;i<map.size();i++){
            String method_name=orders.get(i).getMethod_name();
            ViewHolder viewHolder= (ViewHolder) content.get(i).getTag();
            Order order=orders.get(i);
            if (order.getMethod_name().equals("ip")){
                ArrayList<IP> ipSimpleInfos= (ArrayList<IP>) map.get(order.getMethod_name());
//                IP_HL_Adapter adapter=new IP_HL_Adapter(getActivity(),ipSimpleInfos);
                LinearLayout linearLayout=getLinearInScroll(ipSimpleInfos);
                viewHolder.listView.addView(linearLayout);

            }else {
                ArrayList<IndexItemInfo> indexSimpleinfos= (ArrayList<IndexItemInfo>) map.get(order.getMethod_name());
//                CP_HL_Adapter adapter=new CP_HL_Adapter(getActivity(),indexSimpleinfos);
                LinearLayout linearLayout=getLinearInScroll2(indexSimpleinfos,method_name);
//                viewHolder.listView.setAdapter(adapter);
                viewHolder.listView.addView(linearLayout);
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
                    Order order=orders.get(j);
                    switch (order.getMethod_name()){
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


        }


        MyLog.i("initView");
    }

    private LinearLayout getLinearInScroll2(final ArrayList<IndexItemInfo> indexSimpleinfos, final String methodname) {
        LinearLayout linear=new LinearLayout(getActivity());
        for (int i=0;i<indexSimpleinfos.size();i++){
            View convertView= LayoutInflater.from(getActivity()).inflate(R.layout.item_horizaontal_index,null);
            ImageView imageView= (ImageView) convertView.findViewById(R.id.imageView_item_hor_index);
            TextView textView= (TextView) convertView.findViewById(R.id.text_item_hor_index);
            IndexItemInfo cp=indexSimpleinfos.get(i);
            String imageUrl=Url.prePic+cp.getLogo();
            String name=cp.getCompany_name();
            Picasso.with(getActivity())
                    .load(imageUrl)
                    .into(imageView);
            textView.setText(name);
            linear.addView(convertView);
            final int j=i;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=null;
                    switch (methodname){
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
                    IndexItemInfo ipSimpleInfo=indexSimpleinfos.get(j);
                    intent.putExtra("user_id",ipSimpleInfo.getUser_id());
                    intent.putExtra("identity_cat",ipSimpleInfo.getIdentity_cat());
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                }
            });
        }
        return linear;
    }

    private LinearLayout getLinearInScroll(final ArrayList<IP> ipSimpleInfos) {
        LinearLayout linear=new LinearLayout(getActivity());
        linear.setOrientation(LinearLayout.HORIZONTAL);


        for (int i=0;i<ipSimpleInfos.size();i++){
            View convertView= LayoutInflater.from(getActivity()).inflate(R.layout.item_horizaontal_index,null);
            ImageView imageView= (ImageView) convertView.findViewById(R.id.imageView_item_hor_index);
            TextView textView= (TextView) convertView.findViewById(R.id.text_item_hor_index);
            IP ip=ipSimpleInfos.get(i);
            String imageUrl= Url.prePic+ip.getIp_logo();
            String name=ip.getIp_name();
            Picasso.with(getActivity())
                    .load(imageUrl)
                    .into(imageView);
            textView.setText(name);

            linear.addView(convertView);

            final int j=i;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(),IPDetailActivity.class);
                    IP ipSimpleInfo=ipSimpleInfos.get(j);
                    intent.putExtra("id",ipSimpleInfo.getId());
                    startActivity(intent);
                }
            });
        }

        return linear;

    }

    class ViewHolder{
        HorizontalScrollView listView;
        TextView tj;
        TextView more;
    }



    private void initViewPager() {

        MyLog.i("initViewPager");

        imageViewList.clear();
        whiteDots.removeAllViews();


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

    }
    android.os.Handler handler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    if (viewPager!=null&&indexAdvertses!=null) {
                        currentItem = viewPager.getCurrentItem();
                        currentItem++;
                        if (currentItem == indexAdvertses.size()) {
                            currentItem = 0;
                        }
//                        MyLog.i("currentItem=="+currentItem+"===time==="+new Date().getTime());
                        viewPager.setCurrentItem(currentItem);
//                        thread.start();
                        synchronized (currentItem){
                            currentItem.notify();

                        }

                    }
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

    private void getDataFromDB(){
        orders.clear();
        orders=searchOrder();
        if (orders!=null&&orders.size()!=0){
            MyLog.i("从数据库获取数据");
            map.clear();
            for (int i=0;i<orders.size();i++){
                Order order=orders.get(i);
                if (order.getMethod_name().equals("ip")){
                    ArrayList<IP> ipArrayList= (ArrayList<IP>) searchIP();
                    map.put(order.getMethod_name(),ipArrayList);
                }else {
                    ArrayList<IndexItemInfo> indexItemInfos= (ArrayList<IndexItemInfo>) searchIndexItem(order.getMethod_name());
                    map.put(order.getMethod_name(),indexItemInfos);
                }
            }
            indexAdvertses=seerchIndexAdvert();
            MyLog.i("order size=="+orders.size());
            initView();

        }
    }


    public void jsonParser(String res){
        map.clear();
        orders.clear();
        indexAdvertses.clear();
        try {
            JSONObject jsonObect=new JSONObject(res);
            boolean su=jsonObect.getBoolean("success");
            int code=jsonObect.getInt("code");
            if (su&&code==200){
                JSONObject data=jsonObect.getJSONObject("data");
                Url.prePic=data.getString("prefixPic");
                Url.saveUrl(Url.prePic);
                JSONArray reList=data.getJSONArray("reList");
                for (int i=0;i<reList.length();i++){
                    JSONObject info=reList.getJSONObject(i);
                    Order order=new Order();
                    String method_name=info.getString("method_name");
                    String name=info.getString("name");
                    order.setMethod_name(method_name);
                    order.setName(name);
                    orders.add(order);
                    addOrder(order);
                    JSONArray array=info.getJSONArray(method_name+"list");
                    if (method_name.equals("ip")){
                        ArrayList<IP> list=new ArrayList<>();
                        for (int j=0;j<array.length();j++){
                            JSONObject item=array.getJSONObject(j);
                            IP ipsimpleInfo=new IP();
                            ipsimpleInfo.setIp_name(item.getString("ip_name"));
                            ipsimpleInfo.setIp_logo(item.getString("ip_logo"));
                            ipsimpleInfo.setId(item.getString("id"));
                            list.add(ipsimpleInfo);
                            addIP(ipsimpleInfo);
                        }
                        map.put(method_name,list);
                    }else {
                        ArrayList<IndexItemInfo> list=new ArrayList<>();
                        for (int j=0;j<array.length();j++){
                            JSONObject item=array.getJSONObject(j);
                            IndexItemInfo simpleInfo=new IndexItemInfo();
                            simpleInfo.setUser_id(item.getString("user_id"));
                            simpleInfo.setLogo(item.getString("logo"));
                            simpleInfo.setIdentity_cat(item.getString("identity_cat"));
                            simpleInfo.setCompany_name(item.getString("company_name"));
                            simpleInfo.setMethod_name(method_name);
                            list.add(simpleInfo);
                            addIndexItem(simpleInfo);
                        }

                        map.put(method_name,list);
                    }
                }
                JSONArray adverts=data.getJSONArray("adverts");
                for (int i=0;i<adverts.length();i++){
                    IndexAdvert advert=new IndexAdvert();
                    JSONObject jadvert=adverts.getJSONObject(i);
                    advert.setUser_id(jadvert.getString("id"));
                    advert.setAdvert_name(jadvert.getString("advert_name"));
                    advert.setAdvert_pic(jadvert.getString("advert_pic"));
                    advert.setAdvert_url(jadvert.getString("advert_url"));
                    indexAdvertses.add(advert);
                    addIndexAdvert(advert);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        first=true;
    }

    private IndexItemInfoDao getIndexItemInfoDao(){
        return daoSession.getIndexItemInfoDao();
    }
    private IPDao getIPDao(){
        return daoSession.getIPDao();
    }
    private IndexAdvertDao getIndexAdverDao(){
        return daoSession.getIndexAdvertDao();
    }
    private OrderDao getOrderDao(){
        return daoSession.getOrderDao();
    }

    private void addIndexItem(IndexItemInfo info){
        getIndexItemInfoDao().insert(info);
    }
    private void addIndexAdvert(IndexAdvert advert){
        getIndexAdverDao().insert(advert);
    }
    private void addIP(IP ip){
        getIPDao().insert(ip);
    }
    private void addOrder(Order order){
        getOrderDao().insert(order);
    }

    private List searchIndexItem(String method_name){
        Query query = getIndexItemInfoDao().queryBuilder()
                .where(IndexItemInfoDao.Properties.Method_name.eq(method_name))
                .build();
        return query.list();
    }
    private List searchIP(){
        Query query = getIPDao().queryBuilder()
                .build();
        return query.list();
    }
    private List searchOrder(){
        Query query = getOrderDao().queryBuilder()
                .build();
        return query.list();
    }
    private List seerchIndexAdvert(){
        Query query = getIndexAdverDao().queryBuilder()
                .build();
        return query.list();
    }

    private void deletAll(){
        getIndexAdverDao().deleteAll();
        getOrderDao().deleteAll();
        getIndexItemInfoDao().deleteAll();
        getIPDao().deleteAll();
    }




}
