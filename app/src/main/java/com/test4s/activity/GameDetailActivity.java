package com.test4s.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tools.ImageDownloadHelper;
import com.app.tools.MyLog;
import com.app.view.HorizontalListView;
import com.test4s.adapter.Game_HL_Adapter;
import com.test4s.adapter.HorizontalListAdapter;
import com.test4s.gdb.GameInfo;
import com.test4s.myapp.R;
import com.test4s.net.GameDetailParams;
import com.test4s.net.GameDetialParser;
import com.test4s.net.JsonParser;

import org.xutils.common.Callback;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

public class GameDetailActivity extends AppCompatActivity {

    private String game_id="1";
    private GameDetialParser gameDetialParser;


    private ImageView icon;
    TextView game_name;
    LinearLayout find;
    List<String> find_msg;

    private ImageView care;
    private ImageView download;

    int[] star_id={R.id.star1_gamedetail,R.id.star2_gamedetail,R.id.star3_gamedetail,R.id.star4_gamedetail,R.id.star5_gamedetail};
    ImageView star1;
    ImageView star2;
    ImageView star3;
    ImageView star4;
    ImageView star5;
    ImageView[] stars={star1,star2,star3,star4,star5};

    private TextView canyu;
    private TextView baseinfo;

    private TextView cpInfo;

    private TextView game_intro;
    private LinearLayout gameShot;

    private HorizontalScrollView hs;
    private ImageView shot1;
    private ImageView shot2;
    private ImageView shot3;
    private ImageView shot4;


    private TextView up_info;

    private HorizontalListView other_game;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        find_msg=new ArrayList<>();
        find_msg.add("找投资");
        find_msg.add("找发行");
        find_msg.add("找外包");
        find_msg.add("找IP");

        game_id=getIntent().getStringExtra("game_id");

        icon= (ImageView) findViewById(R.id.image_game_detail);
        game_name= (TextView) findViewById(R.id.gamename_game);

        find= (LinearLayout) findViewById(R.id.linear2_gamedetail);

        for (int i=0;i<stars.length;i++){
            stars[i]= (ImageView) findViewById(star_id[i]);
        }
        canyu= (TextView) findViewById(R.id.canyu_game_detail);
        baseinfo= (TextView) findViewById(R.id.baseInfo_game_detail);
        cpInfo= (TextView) findViewById(R.id.cpInfo_game_detail);
        gameShot= (LinearLayout) findViewById(R.id.gameshot_game_detail);
        hs= (HorizontalScrollView) findViewById(R.id.scroll_game_shot_detail);

        shot1= (ImageView) findViewById(R.id.shot1_gameDetail);
        shot2= (ImageView) findViewById(R.id.shot2_gameDetail);
        shot3= (ImageView) findViewById(R.id.shot3_gameDetail);
        shot4= (ImageView) findViewById(R.id.shot4_gameDetail);

        game_intro= (TextView) findViewById(R.id.gameintro_game_detail);
        up_info= (TextView) findViewById(R.id.update_introduction_gamedetail);

        other_game= (HorizontalListView) findViewById(R.id.other_game_gamedetail);



        initData();

    }

    private void initData() {

        int id=Integer.parseInt(game_id);
        if (id<5000){
            MyLog.i("GameID错误，id为"+game_id);
            return;
        }
        final GameDetailParams gamedetail=new GameDetailParams(id);
        gamedetail.setCacheMaxAge(1000*60*30);

        x.http().post(gamedetail, new Callback.CacheCallback<String>() {

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
                gameDetialParser= JsonParser.getGameDetialParser(result);
                initView();

            }
        });
    }

    private void initView() {
        MyLog.i("GameDetial~~~~initView()");
        GameInfo game_info=gameDetialParser.getGameInfo();

        String imageUrl=gameDetialParser.getStaticUrl()+game_info.getGame_img();

       x.image().bind(icon,imageUrl);



        game_name.setText(game_info.getGame_name());

        String[] require=gameDetialParser.getCpInfo().getRequire().split(",");


        for (int j=0;j<require.length;j++){
            find_msg.remove(require[j]);

        }
        if (find_msg.size()>0){
            for (int i=0;i<find_msg.size();i++){
                switch (find_msg.get(i)){
                    case "找投资":
                        find.getChildAt(0).setVisibility(View.GONE);
                        break;
                    case "找发行":
                        find.getChildAt(1).setVisibility(View.GONE);
                        break;
                    case "找外包":
                        find.getChildAt(2).setVisibility(View.GONE);
                        break;
                    case "找IP":
                        find.getChildAt(3).setVisibility(View.GONE);
                        break;
                }
            }
        }


        setStarScore(gameDetialParser.getScore());


        canyu.setText("("+gameDetialParser.getGame_test_nums()+"人参与)");
        long time=Long.parseLong(gameDetialParser.getCreate_time());
        Date date=new Date();

        SimpleDateFormat simple=new SimpleDateFormat("yyyy-MM-dd");


        date.setTime(time);
        String date_s=simple.format(date);

        String down_nums=game_info.getGame_download_nums();
        Long nums=Long.parseLong(down_nums);
        if (nums>100000){
            down_nums=(nums/10000)+"万";
        }

        baseinfo.setText("上传时间："+date_s+"\n其他："+down_nums+"次下载"+" · "+game_info.getGame_size()+"M\n标签："+game_info.getGame_stage()+" · "+game_info.getGame_platform());
        GameDetialParser.CpInfo cpInfo_d=gameDetialParser.getCpInfo();
        cpInfo.setText("所在地："+cpInfo_d.getCity_id()+"\n公司规模："+cpInfo_d.getCompany_scale()+"\n电话："+cpInfo_d.getCompay_phone()+"\n网址："+cpInfo_d.getCompany_site());


        game_intro.setText(gameDetialParser.getGame_intro());
        up_info.setText(gameDetialParser.getGame_update_intro());

        List<String> shots= gameDetialParser.getGame_shot();
        if (shots!=null){

            x.image().bind(shot1,gameDetialParser.getStaticUrl()+shots.get(0));
            x.image().bind(shot2,gameDetialParser.getStaticUrl()+shots.get(1));
            x.image().bind(shot3,gameDetialParser.getStaticUrl()+shots.get(2));
            x.image().bind(shot4,gameDetialParser.getStaticUrl()+shots.get(3));
        }else {
            MyLog.i("shots为空");
        }


        other_game.setAdapter(new Game_HL_Adapter(this,gameDetialParser.getCpGame()));
        other_game.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GameInfo gameInfo=gameDetialParser.getCpGame().get(position);
                Intent intent= new Intent(GameDetailActivity.this,GameDetailActivity.class);
                intent.putExtra("game_id",gameInfo.getGame_id());
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });




    }

    private void setStarScore(float socre){
        int id= (int) socre;
        float yu=socre-(float) id;
        for (int i=0;i<id;i++){
            stars[i].setImageResource(R.drawable.greenstar);
        }
        if (yu>=0.5){
            stars[id].setImageResource(R.drawable.halfstar);

        }

    }

}
