package com.view.index;


import android.content.Context;
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

import com.app.tools.MyDisplayImageOptions;
import com.app.tools.MyLog;
import com.app.tools.Timer;
import com.app.view.HorizontalListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import com.test4s.gdb.Adverts;
import com.test4s.gdb.AdvertsDao;
import com.test4s.gdb.DaoSession;
import com.test4s.gdb.GameInfo;
import com.test4s.gdb.GameInfoDao;
import com.test4s.gdb.GameType;
import com.test4s.gdb.GameTypeDao;
import com.test4s.myapp.MyApplication;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.game.GameDetailActivity;
import com.view.game.GameListActivity;
import com.test4s.adapter.Game_HL_Adapter;
import com.test4s.myapp.R;
import com.test4s.jsonparser.GameJsonParser;

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

/**
 * Created by Administrator on 2015/12/7.
 */
public class GameFragment extends Fragment implements View.OnClickListener{

    ViewPager viewPager;
    Context mcontext;
    List<ImageView> dots;



    GameJsonParser parser;
    String[] imageUrl=new String[3];

//    List<Adverts> advertsList;
    List<GameType> titles;
    Map<String,List> map;

    LinearLayout continar;
    List<LinearLayout> content;

    List<ImageView> imageViewList;
    private float density;
    private LinearLayout whiteDots;

    private DaoSession daoSession;
    private ArrayList<Adverts> gameAdverts;
    View view;

    private ImageLoader imageloder=ImageLoader.getInstance();
    private Thread thread;

    private int currentItem;
    android.os.Handler handler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    if (viewPager!=null&&gameAdverts!=null) {
                        currentItem = viewPager.getCurrentItem();
                        currentItem++;
                        if (currentItem == gameAdverts.size()) {
                            currentItem = 0;
                        }
//                        MyLog.i("currentItem=="+currentItem+"===time==="+new Date().getTime());
                        viewPager.setCurrentItem(currentItem);
//                        thread.start();

                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (thread==null){
            thread=new Timer(handler);
            thread.start();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_game,null);
        continar= (LinearLayout) view.findViewById(R.id.contianer_game);
        whiteDots= (LinearLayout) view.findViewById(R.id.whitedot_linear_game);
        viewPager= (ViewPager) view.findViewById(R.id.viewpager_game);
        content=new ArrayList<>();
        mcontext=getContext();
        daoSession= MyApplication.daoSession;
        getDensity();
        parser=GameJsonParser.getIntance();
        getDateFromDB();
        
        initData();

        return view;
    }

    private void getDateFromDB() {
        MyLog.i("获取数据库中数据");
        titles= (ArrayList<GameType>) searchTitle();
        if (titles==null){
            return;
        }
        map=new HashMap<>();
        for (int i=0;i<titles.size();i++){
            GameType gametype=titles.get(i);
            String name=gametype.getTitle();
            ArrayList<GameInfo> gamelist1= (ArrayList<GameInfo>) searchGameInfo(name);
            MyLog.i("gamelist size=="+gamelist1.size());
            map.put(name,gamelist1);
        }
        gameAdverts= (ArrayList<Adverts>) searchAdverts();
        initView();
    }

    private void initData() {
        BaseParams baseParams=new BaseParams("game/index");
        baseParams.addSign();
        baseParams.getRequestParams().setCacheMaxAge(1000*60*30);
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            private String result=null;
            boolean update=true;

            @Override
            public void onSuccess(String result) {
                MyLog.i("联网更新数据");

                this.result=result;
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                update=false;
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                MyLog.i("GameIndex====="+result);
                if (update){
                    deleteAll();
                    parser(result);
                    MyLog.i("解析完成");

                    initView();
                }

            }
        });
    }
    public void getDensity(){
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
    }

    private void initViewPager() {

        viewPager.removeAllViews();
        whiteDots.removeAllViews();
        MyLog.i("initViewPager");
        imageViewList=new ArrayList<>();
//        MyLog.i("params");
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(viewPager.getLayoutParams().width,viewPager.getLayoutParams().height);
//        MyLog.i("params1");
        LinearLayout.LayoutParams params1=new LinearLayout.LayoutParams((int)(9*density),(int)(9*density));

        MyLog.i("advertsList size"+gameAdverts.size());
        for (int i=0;i<gameAdverts.size();i++){
            ImageView imageView=new ImageView(getActivity());
            imageView.setLayoutParams(params);

            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViewList.add(imageView);
//            MyLog.i("addimageView");
            if (i>0){
                params1.leftMargin=(int)(12.66*density);
            }
            ImageView dot=new ImageView(getActivity());
            dot.setImageResource(R.drawable.whitedotselected);
            dot.setLayoutParams(params1);
            whiteDots.addView(dot);
//            MyLog.i("imageUrl==="+Url.prePic+gameAdverts.get(i).getAdvert_pic());

            imageloder.displayImage(Url.prePic+gameAdverts.get(i).getAdvert_pic(),imageView, MyDisplayImageOptions.getdefaultImageOptions());


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

    private void initView() {

        initViewPager();

        //初始化三个HorizontalListView
        MyLog.i("初始化view");
        content.clear();
        continar.removeAllViews();
        if (parser==null){
            return;
        }
        MyLog.i("map size=="+map.size());
        for (int i=0;i<map.size();i++){
            MyLog.i("addView1");
            ViewHolder viewHolder=new ViewHolder();
            LinearLayout layout= (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.layout_horilistview_game,null);
//            MyLog.i("addView2");
            viewHolder.listView= (HorizontalListView) layout.findViewById(R.id.list_rm_game);
            viewHolder.tj= (TextView) layout.findViewById(R.id.tjrm_game);
            viewHolder.more= (TextView) layout.findViewById(R.id.more_rm_game);
            viewHolder.tj.setText(titles.get(i).getTitle());
//            MyLog.i("addView3");
            layout.setTag(viewHolder);
//            MyLog.i("addView4");
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            continar.addView(layout,layoutParams);
//            MyLog.i("addView5");
            content.add(layout);
//            MyLog.i("addView6");
        }
        for (int i=0;i<map.size();i++){
            ViewHolder viewHolder= (ViewHolder) content.get(i).getTag();
            ArrayList<GameInfo> gameInfos= (ArrayList<GameInfo>) map.get(titles.get(i).getTitle());
            Game_HL_Adapter  adapter=new Game_HL_Adapter(getActivity(),gameInfos);
            viewHolder.listView.setAdapter(adapter);
        }
        for (int i=0;i<map.size();i++){
            ViewHolder viewHolder= (ViewHolder) content.get(i).getTag();
            final ArrayList<GameInfo> gameInfos= (ArrayList<GameInfo>) map.get(titles.get(i).getTitle());
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

    public void parser(String result){
        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean success=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (success&&code==200){
                JSONObject data=jsonObject.getJSONObject("data");
                Url.prePic=data.getString("prefixPic");
                //
                JSONArray advert=data.getJSONArray("adverts");
                gameAdverts=new ArrayList<>();
                for (int i=0;i<advert.length();i++){
                    Adverts adverts=new Adverts();
                    JSONObject adv=advert.getJSONObject(i);
                    adverts.setAdvert_name(adv.getString("advert_name"));
                    adverts.setAdvert_pic(adv.getString("advert_pic"));
                    adverts.setAdvert_url(adv.getString("advert_url"));
                    gameAdverts.add(adverts);
                    addAdverts(adverts);
                }
                MyLog.i("gameAdverts");
                map=new HashMap<>();
                JSONArray games=data.getJSONArray("games");
                titles=new ArrayList<>();
                for (int i=0;i<games.length();i++){
                    JSONObject game=games.getJSONObject(i);
                    GameType title=new GameType();
                    String title_s=game.getString("title");
                    title.setTitle(title_s);

                    JSONArray content=game.getJSONArray("content");
                    ArrayList<GameInfo> gameInfos=new ArrayList<>();

                    for (int j=0;j<content.length();j++){
                        GameInfo gameInfo=new GameInfo();
                        JSONObject jsonObject1=content.getJSONObject(j);
                        gameInfo.setGame_img(jsonObject1.getString("game_img"));
                        gameInfo.setGame_id(jsonObject1.getString("game_id"));
                        gameInfo.setGame_name(jsonObject1.getString("game_name"));
                        gameInfo.setTitle(title_s);
                        gameInfos.add(gameInfo);
                        addGameInfo(gameInfo);
                    }
                    titles.add(title);
                    addGameType(title);
                    map.put(title_s,gameInfos);
                }
                MyLog.i("games");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private GameInfoDao getGameInfoDao(){
        return daoSession.getGameInfoDao();
    }
    private AdvertsDao getAdvertsDao(){
        return  daoSession.getAdvertsDao();
    }
    private GameTypeDao getGameTypeDao(){
        return daoSession.getGameTypeDao();
    }

    private void addGameInfo(GameInfo gameInfo){
        getGameInfoDao().insert(gameInfo);
    }
    private void addAdverts(Adverts advert){
        getAdvertsDao().insert(advert);
    }
    private void addGameType(GameType type){
        getGameTypeDao().insert(type);
    }

    private List searchGameInfo(String type){
        Query query = getGameInfoDao().queryBuilder()
                .where(GameInfoDao.Properties.Title.eq(type))
                .build();
        return query.list();
    }
    private List searchAdverts(){
        Query query = getAdvertsDao().queryBuilder()
                .build();
        return query.list();
    }
    private List searchTitle(){
        Query query = getGameTypeDao().queryBuilder()
                .build();
        return query.list();
    }

    private void deleteAll(){
        getAdvertsDao().deleteAll();
        getGameInfoDao().deleteAll();
        getGameTypeDao().deleteAll();
    }

}
