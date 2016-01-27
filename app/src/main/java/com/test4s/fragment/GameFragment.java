package com.test4s.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.app.tools.MyLog;
import com.app.view.HorizontalListView;
import com.test4s.activity.GameDetailActivity;
import com.test4s.activity.GameListActivity;
import com.test4s.activity.ListActivity;
import com.test4s.adapter.Game_HL_Adapter;
import com.test4s.gdb.GameInfo;
import com.test4s.net.Adverts;
import com.test4s.net.GameIndexParams;
import com.test4s.net.GameIndexParser;
import com.test4s.myapp.R;
import com.test4s.net.JsonParser;

import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/7.
 */
public class GameFragment extends Fragment implements View.OnClickListener{

    ViewPager viewPager;
    Context mcontext;
    List<ImageView> dots;

    HorizontalListView rmList;
    HorizontalListView olList;
    HorizontalListView spList;

    GameIndexParser parser;
    String prePic;
    String[] imageUrl=new String[3];




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_game,null);
        mcontext=getContext();
        initData(view);

        return view;
    }

    private void initData(final View view) {
        GameIndexParams getIndexParams=new GameIndexParams();
        getIndexParams.setCacheMaxAge(1000*60*30);
        x.http().post(getIndexParams, new Callback.CacheCallback<String>() {
            private String result=null;
            @Override
            public boolean onCache(String result) {
                this.result=result;
                return true;
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
                MyLog.i("GameIndex====="+result);
                parser= JsonParser.getGameInexParser(result);
                prePic=parser.getPrefixPic();
                Game_HL_Adapter.prefixPic=parser.getPrefixPic();
                List<Adverts> list=parser.getAdvertsList();
                for (int i=0;i<imageUrl.length;i++){
                    imageUrl[i]=list.get(i).getAdvert_pic();
                }

                initView(view);
            }
        });
    }

    private void initView(View view) {

        //初始化三个HorizontalListView
        rmList= (HorizontalListView) view.findViewById(R.id.list_rm_game);
        olList= (HorizontalListView) view.findViewById(R.id.list_online_game);
        spList= (HorizontalListView) view.findViewById(R.id.list_sp_game);

        view.findViewById(R.id.more_rm_game).setOnClickListener(this);
        view.findViewById(R.id.more_online_game).setOnClickListener(this);
        view.findViewById(R.id.more_sp_game).setOnClickListener(this);

        if (parser==null){
            return;
        }

        rmList.setAdapter(new Game_HL_Adapter(mcontext,parser.getHotGameList()));
        olList.setAdapter(new Game_HL_Adapter(mcontext,parser.getLoveGameList()));
        spList.setAdapter(new Game_HL_Adapter(mcontext,parser.getLocalGameList()));

        rmList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GameInfo gameInfo=parser.getHotGameList().get(position);
                goDetail(gameInfo.getGame_id());
            }
        });
        olList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GameInfo gameInfo=parser.getLoveGameList().get(position);
                goDetail(gameInfo.getGame_id());
            }
        });
        spList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GameInfo gameInfo=parser.getLocalGameList().get(position);
                goDetail(gameInfo.getGame_id());
            }
        });

        //初始化viewpager
        viewPager= (ViewPager) view.findViewById(R.id.viewpager_game);
        ImageView imageView1=new ImageView(mcontext);
        ImageView imageView2=new ImageView(mcontext);
        ImageView imageView3=new ImageView(mcontext);

        ImageView dot1= (ImageView) view.findViewById(R.id.dot1_game);
        ImageView dot2= (ImageView) view.findViewById(R.id.dot2_game);
        ImageView dot3= (ImageView) view.findViewById(R.id.dot3_game);

        dots=new ArrayList<>();
        dots.add(dot1);
        dots.add(dot2);
        dots.add(dot3);


        x.image().bind(imageView1,prePic+imageUrl[0]);
        x.image().bind(imageView2,prePic+imageUrl[1]);
        x.image().bind(imageView3,prePic+imageUrl[2]);

        List<ImageView> images=new ArrayList<>();
        images.add(imageView1);
        images.add(imageView2);
        images.add(imageView3);

        MyPagerAdapter myAdapter=new MyPagerAdapter(images);
        viewPager.setAdapter(myAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setdot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
        switch (v.getId()){
            case R.id.more_rm_game:
            case R.id.more_online_game:
            case R.id.more_sp_game:
                Intent intent=new Intent(getActivity(), GameListActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                break;
        }

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
