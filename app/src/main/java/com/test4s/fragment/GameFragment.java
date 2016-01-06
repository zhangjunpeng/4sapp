package com.test4s.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.view.HorizontalListView;
import com.test4s.adapter.HorizontalListAdapter;
import com.test4s.adapter.IconUrl;
import com.test4s.myapp.R;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/7.
 */
public class GameFragment extends Fragment {

    ViewPager viewPager;
    Context mcontext;
    List<ImageView> dots;

    HorizontalListView rmList;
    HorizontalListView olList;
    HorizontalListView spList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_game,null);
        mcontext=getContext();
        initView(view);
        return view;
    }

    private void initView(View view) {

        //初始化三个HorizontalListView
        rmList= (HorizontalListView) view.findViewById(R.id.list_rm_game);
        olList= (HorizontalListView) view.findViewById(R.id.list_online_game);
        spList= (HorizontalListView) view.findViewById(R.id.list_sp_game);

        IconUrl iconUrl=new IconUrl();
        iconUrl.setName("百度");
        iconUrl.setUrl("https://www.baidu.com/img/bd_logo1.png");
        List<IconUrl> list_iconUrl=new ArrayList<>();
        for (int i=0;i<10;i++){
            list_iconUrl.add(iconUrl);
        }
        HorizontalListAdapter myAdaper=new HorizontalListAdapter(getContext(),list_iconUrl);
        rmList.setAdapter(myAdaper);
        olList.setAdapter(myAdaper);
        spList.setAdapter(myAdaper);

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


        x.image().bind(imageView1,"http://img2.imgtn.bdimg.com/it/u=3084601648,1719509390&fm=21&gp=0.jpg");
        x.image().bind(imageView2,"http://img.kejixun.com/2013/1126/20131126113620100.jpg");
        x.image().bind(imageView3,"http://360fu.1mod.org/data/attachment/forum/201402/09/000253o95rudbbor5un43r.png.thumb.jpg");

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

    private void setdot(int position) {
        if (dots==null){
            return;
        }
        for (ImageView iamge:dots){
            iamge.setImageResource(R.drawable.lucencydot);
        }
        dots.get(position).setImageResource(R.drawable.yellowdot);
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
