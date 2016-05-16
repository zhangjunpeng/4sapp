package com.view.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.app.tools.MyLog;
import com.app.tools.ScreenUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.picasso.Picasso;
import com.test4s.account.MyAccount;
import com.test4s.gdb.GameInfo;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.search.SearchActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class GameListActivity extends Activity implements View.OnClickListener{

    PullToRefreshListView pullToRefreshListView;

    ImageView back;
    TextView title;
    ImageView search;
    private List<GameInfo> gameInfos;
    MyGameListAdapter gameAdapter;
    int p=1;

    String packageurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        pullToRefreshListView= (PullToRefreshListView) findViewById(R.id.ptflistView_gamelist);

        setImmerseLayout(findViewById(R.id.title_gamelist));

        back= (ImageView) findViewById(R.id.back_titlebar);
        title= (TextView) findViewById(R.id.title_titlebar);
        search= (ImageView) findViewById(R.id.search_titlebar);
        title.setText("游 戏");

        back.setImageResource(R.drawable.back);




        initListener();

        gameInfos=new ArrayList<>();
        gameAdapter=new MyGameListAdapter(this,gameInfos);
        pullToRefreshListView.setAdapter(gameAdapter);
        initData("1");

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GameListActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });


    }

    protected void setImmerseLayout(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
                /*window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);*/
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            int statusBarHeight = ScreenUtil.getStatusBarHeight(this.getBaseContext());
            view.setPadding(0, statusBarHeight, 0, 0);
        }
    }

    private void initData(String p) {
        BaseParams gameParams=new BaseParams("game/gamelist");
        gameParams.addParams("p",p);
        gameParams.addSign();
        gameParams.getRequestParams().setCacheMaxAge(1000*60*5);
        x.http().post(gameParams.getRequestParams(),new Callback.CacheCallback<String>() {
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
                gameListParser(result);

            }
        });

    }

    private void gameListParser(String result) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            int code=jsonObject.getInt("code");
            boolean su=jsonObject.getBoolean("success");
            if (su&&code==200){
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                Url.prePic=jsonObject1.getString("prefixPic");
                packageurl=jsonObject1.getString("prefixPackage");
                JSONArray jsonArray=jsonObject1.getJSONArray("gameList");
                if (jsonArray.length()==0){
                    CusToast.showToast(this,"没有更多游戏", Toast.LENGTH_SHORT);
                }
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject game=jsonArray.getJSONObject(i);
                    GameInfo gameInfo=new GameInfo();
                    gameInfo.setGame_name(game.getString("game_name"));
                    gameInfo.setGame_id(game.getString("game_id"));
                    gameInfo.setGame_img(game.getString("game_img"));
                    gameInfo.setGame_download_url(game.getString("game_download_url"));
                    gameInfo.setGame_download_nums(game.getString("game_download_nums"));
                    gameInfo.setRequire(game.getString("require"));
                    gameInfo.setGame_size(game.getString("game_size"));
                    gameInfo.setNorms(game.getString("norms"));
                    gameInfo.setGame_grade(game.getString("game_grade"));
                    gameInfo.setPack(game.getString("pack"));
                    gameInfo.setChecked(game.getString("checked"));
                    gameInfos.add(gameInfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gameAdapter.notifyDataSetChanged();
        pullToRefreshListView.onRefreshComplete();
    }

    private void initListener(){
        back.setOnClickListener(this);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyLog.i("position===="+position);
                MyLog.i("id==="+id);
                GameInfo gameInfo=gameInfos.get((int) id);
                Intent intent= new Intent(GameListActivity.this,GameDetailActivity.class);
                MyLog.i("game_id==="+gameInfo.getGame_id());
                intent.putExtra("game_id",gameInfo.getGame_id());
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                Toast.makeText(GameListActivity.this,"下拉刷新",Toast.LENGTH_SHORT).show();
                gameInfos.clear();
                initData("1");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                p++;
                initData(p+"");
            }
        });
        MyLog.i("initView");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_titlebar:
                finish();
                overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);
                break;
        }
    }

    class MyGameListAdapter extends BaseAdapter{
        private Context mcontext;
        private List<GameInfo> gameInfos;
        public  MyGameListAdapter(Context context,List<GameInfo> list){
            mcontext=context;
            this.gameInfos=list;
        }

        @Override
        public int getCount() {
            return gameInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return gameInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView==null){
                viewHolder=new ViewHolder();
                convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_gamelist_listactivity,null);
                viewHolder.icon= (ImageView) convertView.findViewById(R.id.imageView_gamelist);
                viewHolder.name= (TextView) convertView.findViewById(R.id.name_item_gamelist);
                viewHolder.down= (ImageView) convertView.findViewById(R.id.download_item_gamelist);
                viewHolder.info= (TextView) convertView.findViewById(R.id.introuduction_item_gamelist);
                viewHolder.gamerating= (ImageView) convertView.findViewById(R.id.gamerating);
                viewHolder.norms= (TextView) convertView.findViewById(R.id.norms_item_gamelist);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            final GameInfo gameInfo=gameInfos.get(position);
            Picasso.with(mcontext)
                    .load(Url.prePic+gameInfo.getGame_img())
                    .placeholder(R.drawable.default_icon)
                    .into(viewHolder.icon);
            Picasso.with(mcontext)
                    .load(Url.prePic+gameInfo.getGame_grade())
                    .into(viewHolder.gamerating);
            viewHolder.name.setText(gameInfo.getGame_name());
            if ("1".equals(gameInfo.getNorms())){
                viewHolder.norms.setVisibility(View.VISIBLE);
            }else if("0".equals(gameInfo.getNorms())){
                viewHolder.norms.setVisibility(View.INVISIBLE);
            }
            String down_nums=gameInfo.getGame_download_nums();
            Long nums=Long.parseLong(down_nums);
            if (nums>100000){
                down_nums=(nums/10000)+"万";
            }
            String mess="";
            if ("1".equals(gameInfo.getPack())&&"1".equals(gameInfo.getChecked())){
                mess=down_nums+"下载/"+gameInfo.getGame_size()+"M\n"+gameInfo.getRequire();
                viewHolder.down.setVisibility(View.VISIBLE);
                viewHolder.down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downLoadGame(packageurl+gameInfo.getGame_download_url());
                        if (MyAccount.isLogin){
                            addMyEvaluation(gameInfo.getGame_id());
                        }
                    }
                });
            }else {
                viewHolder.down.setClickable(false);
                viewHolder.down.setVisibility(View.INVISIBLE);
                mess=gameInfo.getRequire()+"\n";

            }
            viewHolder.info.setText(mess);

            return convertView;
        }
        class ViewHolder{
            ImageView icon;
            TextView name;
            ImageView down;
            ImageView gamerating;
            TextView info;
            TextView norms;
        }
    }
    private void downLoadGame(String url){
        //调用外部浏览器下载文件
        MyLog.i("Url==="+url);
        Uri uri = Uri.parse(url);
        Intent downloadIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(downloadIntent);
    }
    private void addMyEvaluation(String game_id){
        BaseParams baseParams=new BaseParams("test/downloadgame");
        baseParams.addParams("game_id",game_id);
        baseParams.addParams("token",MyAccount.getInstance().getToken());
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("add game to eva back=="+result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

}
