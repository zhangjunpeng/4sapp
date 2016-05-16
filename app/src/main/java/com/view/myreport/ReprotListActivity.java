package com.view.myreport;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.squareup.picasso.Picasso;
import com.test4s.account.MyAccount;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.activity.BaseActivity;
import com.view.game.GameDetailActivity;
import com.app.delslidelistview.DeleListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReprotListActivity extends BaseActivity {

    List<GameReportInfo> gameReportInfos;

    DeleListView listview;
    MyListAdapter myadapter;

    ImageView back;
    TextView title;
    TextView save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reprot_list);
        setImmerseLayout(findViewById(R.id.titlebar_reportlist));

        listview= (DeleListView) findViewById(R.id.listview_reportlist);
        back= (ImageView) findViewById(R.id.back_savebar);
        title= (TextView) findViewById(R.id.textView_titlebar_save);
        save= (TextView) findViewById(R.id.save_savebar);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title.setText("我的报告");
        save.setVisibility(View.INVISIBLE);

        gameReportInfos=new ArrayList<>();
        myadapter=new MyListAdapter(this,gameReportInfos);
        listview.setAdapter(myadapter);

        initData(1+"");
    }

    private void initData(String p) {
        BaseParams baseParams=new BaseParams("test/reportlist");
        baseParams.addParams("p",p);
        baseParams.addParams("token", MyAccount.getInstance().getToken());
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("My report list=="+result);
                try {
                    JSONObject json=new JSONObject(result);
                    boolean su=json.getBoolean("success");
                    int code=json.getInt("code");
                    if (su&&code==200){
                        JSONObject data=json.getJSONObject("data");
                        JSONArray reports=data.getJSONArray("reportList");
                        for (int i=0;i<reports.length();i++){
                            JSONObject info=reports.getJSONObject(i);
                            GameReportInfo gamereportInfo=new GameReportInfo();
                            gamereportInfo.setGame_id(info.getString("game_id"));
                            gamereportInfo.setCreate_time(info.getString("create_time"));
                            gamereportInfo.setTest_total_score(info.getString("test_total_score"));
                            gamereportInfo.setGame_img(info.getString("game_img"));
                            gamereportInfo.setGame_name(info.getString("game_name"));
                            gamereportInfo.setGame_grade(info.getString("game_grade"));
                            gamereportInfo.setGame_test_nums(info.getString("game_test_nums"));
                            gamereportInfo.setGame_stage(info.getString("game_stage"));
                            gamereportInfo.setGame_platform(info.getString("game_platform"));
                            gamereportInfo.setGame_type(info.getString("game_type"));
                            gamereportInfo.setStatus(info.getString("status"));
                            gameReportInfos.add(gamereportInfo);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                initListView();
            }
        });

    }
    class MyListAdapter extends BaseAdapter {

        Context context;
        List<GameReportInfo> gameInfos;

        public MyListAdapter(Context context,List<GameReportInfo> gameInfos){
            this.context=context;
            this.gameInfos=gameInfos;


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
                convertView= LayoutInflater.from(context).inflate(R.layout.item_evaluationlist,parent,false);
                viewHolder.icon= (ImageView) convertView.findViewById(R.id.imageView_gameevalu);
                viewHolder.name= (TextView) convertView.findViewById(R.id.name_item_gameevalu);
                viewHolder.bg= (Button) convertView.findViewById(R.id.cancel_care_evalu);
                viewHolder.info= (TextView) convertView.findViewById(R.id.introuduction_item_gameevalu);
                viewHolder.gamerating= (ImageView) convertView.findViewById(R.id.gamerating_gameevalu);
                viewHolder.delete= (ImageView) convertView.findViewById(R.id.delete_item_evalu);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            final GameReportInfo gameInfo= (GameReportInfo) gameReportInfos.get(position);
            viewHolder.info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, GameDetailActivity.class);
                    intent.putExtra("game_id",gameInfo.getGame_id());
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                }
            });
            viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, GameDetailActivity.class);
                    intent.putExtra("game_id",gameInfo.getGame_id());
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                }
            });

            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteTestGame(gameInfo.getGame_id());
                }
            });
            Picasso.with(context)
                    .load(Url.prePic+gameInfo.getGame_img())
                    .placeholder(R.drawable.default_icon)
                    .into(viewHolder.icon);
            viewHolder.name.setText(gameInfo.getGame_name());
            Picasso.with(context)
                    .load(Url.prePic+gameInfo.getGame_grade())
                    .into(viewHolder.gamerating);
            viewHolder.info.setText(gameInfo.getGame_platform()+"\n"+gameInfo.getGame_stage()+" / "+gameInfo.getGame_type()+"\n"+timeToDate(gameInfo.getCreate_time()));
//            switch (gameInfo.getStatus()){
//                case "查看报告":
                    viewHolder.bg.setText(gameInfo.getStatus());
                    viewHolder.bg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(ReprotListActivity.this,GameReportActivity.class);
                            intent.putExtra("game_id",gameInfo.getGame_id());
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                        }
                    });
//                    break;
//                case "测评中":
//                    viewHolder.bg.setText(gameInfo.getStatus());
//                    viewHolder.bg.setTextColor(Color.rgb(76,76,76));
//                    viewHolder.bg.setBackgroundResource(R.drawable.grayborder_button);
//                    viewHolder.bg.setClickable(false);
//                    break;
//                case "审核失败":
//                    viewHolder.bg.setText(gameInfo.getStatus());
//                    viewHolder.bg.setTextColor(Color.rgb(76,76,76));
//                    viewHolder.bg.setBackgroundResource(R.drawable.grayborder_button);
//                    viewHolder.bg.setClickable(false);
//                    break;
//            }
            return convertView;
        }
        class ViewHolder{
            ImageView icon;
            TextView name;
            Button bg;
            TextView info;
            ImageView gamerating;
            ImageView delete;
        }
    }

    private void initListView() {
        if (gameReportInfos.size()==0){
            listview.setVisibility(View.GONE);
        }else {
            myadapter.notifyDataSetChanged();
        }
    }
    private void deleteTestGame(String gameid){
        BaseParams baseParams=new BaseParams("test/deltestgame");
        baseParams.addParams("game_id",gameid);
        baseParams.addParams("token",MyAccount.getInstance().getToken());
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("params==="+result);
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

    public String timeToDate(String times){
        long time=Long.parseLong(times+"000");
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date date=new Date(time);
        return simpleDateFormat.format(date);
    }
}
