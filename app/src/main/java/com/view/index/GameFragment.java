package com.view.index;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.test4s.adapter.CP_HL_Adapter;
import com.test4s.adapter.IP_HL_Adapter;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.game.GameDetailActivity;
import com.view.game.GameListActivity;
import com.test4s.adapter.Game_HL_Adapter;
import com.test4s.gdb.GameInfo;
import com.test4s.net.Adverts;
import com.test4s.net.GameIndexParams;
import com.test4s.net.GameIndexParser;
import com.test4s.myapp.R;
import com.test4s.jsonparser.GameJsonParser;
import com.view.s4server.IPSimpleInfo;

import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/7.
 */
public class GameFragment extends Fragment implements View.OnClickListener{

    ViewPager viewPager;
    Context mcontext;
    List<ImageView> dots;



    GameJsonParser parser;
    String[] imageUrl=new String[3];

    List<Adverts> advertsList;
    List<String> titles;
    Map<String,List> map;

    LinearLayout continar;
    List<LinearLayout> content;

    List<ImageView> imageViewList;
    private float density;
    private LinearLayout whiteDots;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_game,null);
        continar= (LinearLayout) view.findViewById(R.id.contianer_game);
        whiteDots= (LinearLayout) view.findViewById(R.id.whitedot_linear_game);
        viewPager= (ViewPager) view.findViewById(R.id.viewpager_game);
        content=new ArrayList<>();
        mcontext=getContext();
        getDensity();
        parser=GameJsonParser.getIntance();
        initData(view);

        return view;
    }

    private void initData(final View view) {
        BaseParams baseParams=new BaseParams("game/index");
        baseParams.addSign();
        baseParams.getRequestParams().setCacheMaxAge(1000*60*30);
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            private String result=null;


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
                MyLog.i("GameIndex====="+result);
                parser.parser(result);
                MyLog.i("解析完成");
                titles=parser.titles;
                MyLog.i("title"+titles.toString());
                advertsList=parser.gameAdverts;
                MyLog.i("advertsList"+advertsList.toString());
                map=parser.map;
                initView(view);
                initViewPager();
            }
        });
    }
    public void getDensity(){
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
    }

    private void initViewPager() {
        MyLog.i("initViewPager");

        imageViewList=new ArrayList<>();
        MyLog.i("params");
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(viewPager.getLayoutParams().width,viewPager.getLayoutParams().height);
        MyLog.i("params1");
        LinearLayout.LayoutParams params1=new LinearLayout.LayoutParams((int)(9*density),(int)(9*density));

        MyLog.i("advertsList size"+advertsList.size());
        for (int i=0;i<advertsList.size();i++){
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
            MyLog.i("imageUrl==="+Url.prePic+advertsList.get(i).getAdvert_pic());
            Picasso.with(getActivity())
                    .load(Url.prePic+advertsList.get(i).getAdvert_pic())
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
    private void setDot(int position) {
        for (int i=0;i<whiteDots.getChildCount();i++){
            if (i==position){
                whiteDots.getChildAt(i).setSelected(true);
            }else {
                whiteDots.getChildAt(i).setSelected(false);
            }
        }

    }

    private void initView(View view) {

        //初始化三个HorizontalListView
        MyLog.i("初始化view");

        if (parser==null){
            return;
        }
        MyLog.i("map size=="+map.size());
        for (int i=0;i<map.size();i++){
            MyLog.i("addView1");
            ViewHolder viewHolder=new ViewHolder();
            LinearLayout layout= (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.layout_horilistview_game,null);
            MyLog.i("addView2");
            viewHolder.listView= (HorizontalListView) layout.findViewById(R.id.list_rm_game);
            viewHolder.tj= (TextView) layout.findViewById(R.id.tjrm_game);
            viewHolder.more= (TextView) layout.findViewById(R.id.more_rm_game);
            viewHolder.tj.setText(titles.get(i));
            MyLog.i("addView3");
            layout.setTag(viewHolder);
            MyLog.i("addView4");
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            continar.addView(layout,layoutParams);
            MyLog.i("addView5");
            content.add(layout);
            MyLog.i("addView6");
        }
        for (int i=0;i<map.size();i++){
            ViewHolder viewHolder= (ViewHolder) content.get(i).getTag();
            ArrayList<GameInfo> gameInfos= (ArrayList<GameInfo>) map.get(i+"");
            Game_HL_Adapter  adapter=new Game_HL_Adapter(getActivity(),gameInfos);
            viewHolder.listView.setAdapter(adapter);
        }
        for (int i=0;i<map.size();i++){
            ViewHolder viewHolder= (ViewHolder) content.get(i).getTag();
            final ArrayList<GameInfo> gameInfos= (ArrayList<GameInfo>) map.get(i+"");
            viewHolder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    GameInfo gameInfo=gameInfos.get(position);
                    goDetail(gameInfo.getGame_id());
                }
            });
            viewHolder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), GameListActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                }
            });
        }
    }

    class ViewHolder{
        HorizontalListView listView;
        TextView tj;
        TextView more;
    }
    private void goDetail(String gameid){
        Intent intent= new Intent(getActivity(),GameDetailActivity.class);
        intent.putExtra("game_id",gameid);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);

    }

    private void setdot(int position) {
        if (dots==null){
            return;
        }
        for (ImageView iamge:dots){
            iamge.setImageResource(R.drawable.lucencydot);
        }
        dots.get(position).setImageResource(R.drawable.yellowdot);
    }

    @Override
    public void onClick(View v) {

    }


    class MyPagerAdapter extends PagerAdapter{

        List<ImageView> imageViews;

        public  MyPagerAdapter(List<ImageView> list){
            imageViews=list;
        }

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView image=imageViews.get(position);
            container.addView(image);
            return image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViews.get(position));
        }
    }


}
