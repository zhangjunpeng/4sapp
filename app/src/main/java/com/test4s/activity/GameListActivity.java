package com.test4s.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.MyLog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.test4s.adapter.GameAdapter;
import com.test4s.gdb.GameInfo;
import com.test4s.myapp.R;
import com.test4s.net.GameListParams;
import com.test4s.net.GameListParser;
import com.test4s.net.JsonParser;

import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class GameListActivity extends Activity {

    PullToRefreshListView pullToRefreshListView;

    ImageView back;
    TextView title;
    private List<GameInfo> gameInfos;
    String p="1";
    GameAdapter gameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        pullToRefreshListView= (PullToRefreshListView) findViewById(R.id.ptflistView_gamelist);

        back= (ImageView) findViewById(R.id.back_titlebar);
        title= (TextView) findViewById(R.id.title_titlebar);
        title.setText("游 戏");

        back.setImageResource(R.drawable.back);





        initData();




    }

    private void initData() {
        GameListParams gameParams=new GameListParams(p);
        gameParams.setCacheMaxAge(1000*60*5);
        x.http().post(gameParams,new Callback.CacheCallback<String>() {
            private String result;
            @Override
            public boolean onCache(String result) {
                this.result=result;
                MyLog.i("使用缓存");
                return true;
            }

            @Override
            public void onSuccess(String result) {
                MyLog.i("访问网络");
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
                MyLog.i("GameList==="+result);
                GameListParser gameListParser= JsonParser.getGameListParser(result);
                gameInfos=gameListParser.getGameInfoList();
//                listView.onRefreshComplete();
                gameAdapter=new GameAdapter(GameListActivity.this,gameInfos);
                pullToRefreshListView.setAdapter(gameAdapter);

                initView();

            }
        });

    }

    private void initView(){

        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GameInfo gameInfo=gameInfos.get(position);
                Intent intent= new Intent(GameListActivity.this,GameDetailActivity.class);
                intent.putExtra("game_id",gameInfo.getGame_id());
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Toast.makeText(GameListActivity.this,"下拉刷新",Toast.LENGTH_SHORT).show();
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        MyLog.i("initView");

    }

}
