package com.test4s.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.test4s.adapter.CpAdapter;
import com.test4s.gdb.CP;
import com.test4s.myapp.R;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.LogRecord;

public class ListActivity extends AppCompatActivity {

    PullToRefreshListView listView;

    ImageView back;
    TextView title;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    listView.onRefreshComplete();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView= (PullToRefreshListView) findViewById(R.id.listView_list);

        back= (ImageView) findViewById(R.id.back_titlebar);
        title= (TextView) findViewById(R.id.title_titlebar);

        back.setImageResource(R.drawable.back);

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Toast.makeText(ListActivity.this,"下拉刷新",Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(0);
                        }

                    }
                }).start();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });





        CP cp=new CP();
        List<CP> list=new ArrayList<>();
        for (int i=0;i<10;i++){

            list.add(cp);
        }
        CpAdapter cpAdapter=new CpAdapter(this,list);
        listView.setAdapter(cpAdapter);
        title.setText("cp");


    }




}
