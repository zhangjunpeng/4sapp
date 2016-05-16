package com.app.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.app.tools.MyLog;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.internal.LoadingLayout;
import com.test4s.myapp.R;

/**
 * Created by Administrator on 2016/5/9.
 */
public class PullListView extends PullToRefreshListView{
    Context context;


    public PullListView(Context context) {
        super(context);
        this.context=context;
        setHand();
    }

    public PullListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        setHand();

    }

    public PullListView(Context context, Mode mode) {
        super(context, mode);
        this.context=context;
//        setHand();

    }

    public PullListView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
        this.context=context;
//        setHand();

    }

    public void setHand(){
        LoadingLayout headerLoadingLayout = getHeaderLayout();
//        headerLoadingLayout.removeAllViews();
        View handerView= LayoutInflater.from(context).inflate(R.layout.handerloading,null);
        ImageView imageView= (ImageView) handerView.findViewById(R.id.image_handerloading);
        AnimationDrawable ad = (AnimationDrawable)imageView.getBackground();
        ad.start();
        headerLoadingLayout.addView(handerView);

        LoadingLayout footerLoadingLayout = getFooterLayout();
//        footerLoadingLayout.removeAllViews();


        View footerView= LayoutInflater.from(context).inflate(R.layout.footerloading,null);
        ImageView imageView1= (ImageView) footerView.findViewById(R.id.image_footerloading);
        AnimationDrawable ad1 = (AnimationDrawable)imageView1.getBackground();
        ad1.start();
        footerLoadingLayout.addView(footerView);
        MyLog.i("mShowViewWhileRefreshing==00"+getShowViewWhileRefreshing());


    }
    public void setRefreshListener(OnRefreshListener2 listener){
//        headerLoadingLayout.hideAllViews();
//        footerLoadingLayout.hideAllViews();
        MyLog.i("mShowViewWhileRefreshing==111"+getShowViewWhileRefreshing());

        setOnRefreshListener(listener);
    }



}
