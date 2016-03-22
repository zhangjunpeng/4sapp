package com.view.game;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.app.view.HorizontalListView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.squareup.picasso.Picasso;
import com.test4s.adapter.Game_HL_Adapter;
import com.test4s.gdb.GameInfo;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.GameDetailParams;
import com.test4s.net.GameDetialParser;
import com.test4s.jsonparser.GameJsonParser;
import com.test4s.net.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameDetailActivity extends AppCompatActivity {

    private ImageView icon;
    TextView game_name;
    LinearLayout find;
    String[] find_msg;
    private TextView companyname;
    private TextView canyu;
    private TextView baseinfo;
    private TextView game_intro;
    private TextView up_info;
    private LinearLayout gameShot;
    private AppBarLayout appBarLayout;
    private ImageView down_title;
    private ImageView care_title;
    private ImageView care;
    private ImageView download;

    private HorizontalListView other_game;
    private TextView game_intro_more;
    private TextView game_update_more;
    private TextView other_geme_more;
    private TextView conmentnum;
    private LinearLayout continer_advice;


    int[] star_id={R.id.star1_gamedetail,R.id.star2_gamedetail,R.id.star3_gamedetail,R.id.star4_gamedetail,R.id.star5_gamedetail};
    ImageView star1;
    ImageView star2;
    ImageView star3;
    ImageView star4;
    ImageView star5;
    ImageView[] stars={star1,star2,star3,star4,star5};



    private HorizontalScrollView hs;
    Toolbar toolbar;

    private String game_id;

    private float density;



    private GameInfo gameInfo;


    private String create_timestring;
    private String game_test_numsstring;
    private String game_update_introstring;
    private boolean focus;
    private String score;
    private String webpre;
    private int advise_num;
    List<GameInfo> game_list;
    List<Advise> adviseList;
    private String companynamestring;
    private String game_imgstring;
    private String game_namestring;
    private String game_platformstring;
    private String game_typestring;
    private String game_stagestring;
    private String game_sizestring;
    private String game_download_url;

    private String pic_dir;
    private List<String> game_shots;
    private String game_introstring;
    private String game_updatestring;
    private String game_download_unit;
    private String game_download_nums;
    private String requirement;
    private boolean flag_showall_inro=false;
    private boolean flag_showall_update=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_game_detail);
        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set a custom tint color for all system bars

        tintManager.setTintColor(Color.parseColor("#252525"));
        toolbar= (Toolbar) findViewById(R.id.toolbar_gameDetail);
        setSupportActionBar(toolbar);



        game_id=getIntent().getStringExtra("game_id");

        icon= (ImageView) findViewById(R.id.image_game_detail);
        game_name= (TextView) findViewById(R.id.gamename_game);
        find= (LinearLayout) findViewById(R.id.linear2_gamedetail);
        for (int i=0;i<stars.length;i++){
            stars[i]= (ImageView) findViewById(star_id[i]);
        }
        canyu= (TextView) findViewById(R.id.canyu_game_detail);
        baseinfo= (TextView) findViewById(R.id.baseInfo_game_detail);
        gameShot= (LinearLayout) findViewById(R.id.gameshot_game_detail);
        hs= (HorizontalScrollView) findViewById(R.id.scroll_game_shot_detail);
        game_intro= (TextView) findViewById(R.id.gameintro_game_detail);
        up_info= (TextView) findViewById(R.id.update_introduction_gamedetail);
        other_game= (HorizontalListView) findViewById(R.id.other_game_gamedetail);
        appBarLayout= (AppBarLayout) findViewById(R.id.appbar_gamedetail);
        down_title= (ImageView) findViewById(R.id.download_title_gamedetail);
        care_title= (ImageView) findViewById(R.id.care_title_gamedetail);
        care= (ImageView) findViewById(R.id.attention_gametail);
        download= (ImageView) findViewById(R.id.download_gamedetail);
        game_intro_more= (TextView) findViewById(R.id.more_introduction_game);
        game_update_more= (TextView) findViewById(R.id.more_update_game);

        gameShot= (LinearLayout) findViewById(R.id.gameshot_game_detail);
        conmentnum= (TextView) findViewById(R.id.conment_num);
        companyname= (TextView) findViewById(R.id.name_company_game);
        continer_advice= (LinearLayout) findViewById(R.id.continer_comment_gamedetail);


        //获取屏幕密度
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        initData();
        initListener();

    }

    private void initListener() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;


            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (verticalOffset<-100*density){
                    toolbar.setBackgroundColor(Color.argb(15,255,255,255));
                }else {
                    toolbar.setBackgroundColor(Color.argb( 0,37,37,37));
                }
                if (verticalOffset>-250*density){
                    down_title.setVisibility(View.INVISIBLE);
                    care_title.setVisibility(View.INVISIBLE);
                }else {
                    down_title.setVisibility(View.VISIBLE);
                    care_title.setVisibility(View.VISIBLE);
                }
                if (scrollRange + verticalOffset == 0) {

                    isShow = true;
                } else if(isShow) {

                    isShow = false;
                }
            }
        });
        game_intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_showall_inro=!flag_showall_inro;
                if (flag_showall_inro){
                    MyLog.i("game_intro=="+game_introstring);
                    game_intro.setEllipsize(null);
                    game_intro.setMaxLines(100);
                    game_intro.setText(game_introstring);
                    game_intro_more.setText("收起");
                }else {

                    String into=game_introstring.substring(0,78);
                    game_intro.setText(into+"...");
                    game_intro.setMaxLines(3);
                    game_intro.setEllipsize(TextUtils.TruncateAt.END);
                    game_intro_more.setText("更多");
                }
            }
        });
        up_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (game_updatestring.length()<78){
                    return;
                }
                flag_showall_update=!flag_showall_update;
                if (flag_showall_update){

                    up_info.setEllipsize(null);
                    up_info.setMaxLines(100);
                    up_info.setText(game_update_introstring);
                    game_update_more.setText("收起");
                }else {

                    String into=game_update_introstring.substring(0,74);
                    up_info.setText(into+"...");
                    up_info.setMaxLines(3);
                    up_info.setEllipsize(TextUtils.TruncateAt.END);
                    game_update_more.setText("更多");
                }
            }
        });
    }

    private void initData() {

        int id=Integer.parseInt(game_id);
        if (id<5000){
            MyLog.i("GameID错误，id为"+game_id);
            return;
        }
        BaseParams gamedetail=new BaseParams("game/gamedetail");
        gamedetail.addParams("game_id",game_id);
        gamedetail.addSign();
        gamedetail.getRequestParams().setCacheMaxAge(1000*60*30);

        x.http().post(gamedetail.getRequestParams(), new Callback.CacheCallback<String>() {

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
                MyLog.i("GameDetail===~~~~~"+result);
                gameDetailParser(result);
                initView();

            }
        });
    }

    public void gameDetailParser(String result){
        try {
            JSONObject jsonObject=new JSONObject(result);
            int code=jsonObject.getInt("code");
            boolean su=jsonObject.getBoolean("success");
            if (su&&code==200){
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                Url.prePic=jsonObject1.getString("prefixPic");
                webpre=jsonObject1.getString("prefixPackage");

                JSONObject jsonObject2=jsonObject1.getJSONObject("gameInfo");
                JSONObject info=jsonObject2.getJSONObject("info");
                gameInfo=new GameInfo();
                gameInfo.setGame_name(info.getString("game_name"));
                gameInfo.setGame_size(info.getString("game_size"));

                gameInfo.setGame_download_nums(info.getString("game_download_nums"));
                gameInfo.setGame_stage(info.getString("game_stage"));
                gameInfo.setGame_download_url(info.getString("game_download_url"));
                gameInfo.setGame_img(info.getString("game_img"));
                gameInfo.setGame_platform(info.getString("game_platform"));

                game_typestring=info.getString("game_type");
                create_timestring=info.getString("create_time");
                game_test_numsstring=info.getString("game_test_nums");
                requirement=info.getString("requirement");

                JSONObject game_shot=info.getJSONObject("game_shot");

                pic_dir=game_shot.getString("pic_dir");
                JSONArray shots=game_shot.getJSONArray("pic_"+pic_dir);

                game_shots=new ArrayList<>();
                for (int i=0;i<shots.length();i++){
                    JSONArray jsonarray=shots.getJSONArray(i);
                    for (int j=0;j<jsonarray.length();j++){
                        String imgs=jsonarray.getString(j);
                        game_shots.add(imgs);
                    }

                }

                game_introstring=info.getString("game_intro");
                game_update_introstring=info.getString("game_update_intro");
                companynamestring=info.getString("company_name");
                focus=info.getBoolean("focus");
                score=info.getString("playScore");
                game_download_unit=info.getString("game_download_unit");
                game_download_nums=info.getString("game_download_nums");

                JSONArray cpGame=info.getJSONArray("gameList");
               game_list=new ArrayList<>();

                for (int i=0;i<cpGame.length();i++){
                    JSONObject jsonObject3=cpGame.getJSONObject(i);
                    GameInfo gameInfo1=new GameInfo();
                    gameInfo1.setGame_id(jsonObject3.getString("id"));
                    gameInfo1.setGame_img(jsonObject3.getString("game_img"));
                    gameInfo1.setGame_name(jsonObject3.getString("game_name"));
                    game_list.add(gameInfo1);
                }
                adviseList=new ArrayList<>();
                advise_num=info.getInt("advise_num");
                JSONArray advices=info.getJSONArray("advise");
                for (int i=0;i<advices.length();i++){
                    JSONObject adv=advices.getJSONObject(i);
                    Advise advise=new Advise();
                    advise.setShowname(adv.getString("showname"));
                    advise.setAdvise(adv.getString("advise"));
                    advise.setDate_time(adv.getString("date_time"));
                    advise.setTest_total_score(adv.getString("test_total_score"));
                    advise.setHour_time(adv.getString("hour_time"));
                    adviseList.add(advise);
                }

            }
            MyLog.i("解析完成");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initView() {
        MyLog.i("GameDetial~~~~initView()");

        String imageUrl=Url.prePic+gameInfo.getGame_img();

        Picasso.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.default_icon)
                .into(icon);


        MyLog.i("game_namestring==="+game_namestring);
        game_name.setText(gameInfo.getGame_name());
        companyname.setText(companynamestring);

//        find_msg= requirement.split(",");
//        if (find_msg.length>0){
//            for (int i=0;i<find_msg.length;i++){
//                switch (find_msg[i]){
//                    case "找投资":
//                        find.getChildAt(0).setVisibility(View.GONE);
//                        break;
//                    case "找发行":
//                        find.getChildAt(1).setVisibility(View.GONE);
//                        break;
//                    case "找外包":
//                        find.getChildAt(2).setVisibility(View.GONE);
//                        break;
//                    case "找IP":
//                        find.getChildAt(3).setVisibility(View.GONE);
//                        break;
//                }
//            }
//        }

        if (focus){
            //已关注
        }else {

        }
        MyLog.i("score=="+score);
        if (score.equals("null")){

        }else {
            setStarScore(Float.parseFloat(score));
        }

        MyLog.i("game_test_numsstring=="+game_test_numsstring);
        canyu.setText("("+game_test_numsstring+"人参与)");
        baseinfo.setText("上传时间："+create_timestring+"\n其他："+game_download_nums+game_download_unit+"下载"+" · "+gameInfo.getGame_size()+"M\n标   签："+gameInfo.getGame_stage()+" · "+gameInfo.getGame_platform());

        try {
            String into=game_update_introstring.substring(0,74);
            up_info.setText(into+"...");
        }catch (Exception e){
            MyLog.i("报错1");
            up_info.setText(game_update_introstring);
        }
        try {
            String into=game_introstring.substring(0,78);
            game_intro.setText(into+"...");
        }catch (Exception e){
            MyLog.i("报错2");
            game_intro.setText(game_introstring);
        }


        MyLog.i("game_update_introstring=="+game_update_introstring);


        addshots(game_shots);
        other_game.setAdapter(new Game_HL_Adapter(this,game_list));
        other_game.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GameInfo gameInfo=game_list.get(position);
                Intent intent= new Intent(GameDetailActivity.this,GameDetailActivity.class);
                intent.putExtra("game_id",gameInfo.getGame_id());
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });
        
        conmentnum.setText("("+advise_num+")");

        addAdvices(adviseList);

    }

    private void addAdvices(List<Advise> adviseList) {
        MyLog.i("添加评论1,adviseList size=="+adviseList.size());
//        LinearLayoutCompat.LayoutParams params=new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i=0;i<adviseList.size();i++){
            Advise ad=adviseList.get(i);
            MyLog.i("添加评论2");
            View view= LayoutInflater.from(this).inflate(R.layout.item_advicelist,null);
            TextView name= (TextView) view.findViewById(R.id.showname_item_advice);
            TextView time= (TextView) view.findViewById(R.id.time_item_advice);
            final TextView advice= (TextView) view.findViewById(R.id.advice_item_advice);
            MyLog.i("添加评论3");
            name.setText(ad.getShowname());
            time.setText(ad.getDate_time()+"  "+ad.getHour_time());
            advice.setText(ad.getAdvise());
            MyLog.i("添加评论4");
            continer_advice.addView(view);
            MyLog.i("添加评论5");
            advice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (advice.getMaxLines()==5){
                        advice.setEllipsize(null);
                        advice.setMaxLines(100);
                    }else {
                        advice.setEllipsize(TextUtils.TruncateAt.END);
                        advice.setMaxLines(5);
                    }

                }
            });
        }

    }

    private void addshots(List<String> game_shots) {
        LinearLayoutCompat.LayoutParams params;
        if (pic_dir.equals("hori")){
            params=new LinearLayoutCompat.LayoutParams((int)(263*density),(int)(148*density));
        }else {
            params=new LinearLayoutCompat.LayoutParams((int)(160*density),(int)(238*density));
        }
        for (int i=0;i<game_shots.size();i++) {
            ImageView imageView = new ImageView(this);

            params.rightMargin = (int) (3 * density);
            params.bottomMargin = (int) (18 * density);
            params.leftMargin= (int) (3*density);
            imageView.setId(i);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            gameShot.addView(imageView, params);
            MyLog.i("添加ImageView==="+Url.prePic + game_shots.get(i));
            Picasso.with(this)
                    .load(Url.prePic + game_shots.get(i))
                    .placeholder(R.drawable.default_icon)
                    .into(imageView);
        }
    }

    private void setStarScore(float socre){
        int id= (int) socre;
        float yu=socre-(float) id;
        for (int i=0;i<id;i++){
            stars[i].setImageResource(R.drawable.orangestar);
        }
        if (yu>=0.5){
            stars[id].setImageResource(R.drawable.halforangestar);
        }
    }

    private void downLoadGame(String url){
        //调用外部浏览器下载文件
        Uri uri = Uri.parse(url);
        Intent downloadIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(downloadIntent);
    }

}
