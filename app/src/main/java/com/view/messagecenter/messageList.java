package com.view.messagecenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.test4s.account.MyAccount;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.view.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/16.
 */
public class MessageList extends BaseActivity implements View.OnClickListener {
    List<MessageInfo> messageInfos;

    DeleListView listView;
    private ImageView back;
    private TextView title;
    private TextView save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        listView= (DeleListView) findViewById(R.id.listView_messlist);
        back= (ImageView) findViewById(R.id.back_savebar);
        title= (TextView) findViewById(R.id.textView_titlebar_save);
        save= (TextView) findViewById(R.id.save_savebar);
        setImmerseLayout(findViewById(R.id.titlebar_messlist));

        save.setVisibility(View.INVISIBLE);
        title.setText("消息中心");
        back.setOnClickListener(this);

        initListener();

        initData("1");
    }

    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(listView.canClick()){
                    MyLog.i("listview click!!!");
                }
            }
        });

    }

    private void initData(String p) {
        BaseParams baseParams=new BaseParams("user/msg");
        baseParams.addParams("token", MyAccount.getInstance().getToken());
        baseParams.addParams("p",p);
        baseParams.addSign();
        baseParams.getRequestParams().setCacheMaxAge(1000*60*60);
        x.http().post(baseParams.getRequestParams(), new Callback.CacheCallback<String>() {
            String res;
            @Override
            public void onSuccess(String result) {
                res=result;
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                MyLog.i("MessageList=="+res);
                try {
                    JSONObject jsonObeject=new JSONObject(res);
                    boolean su=jsonObeject.getBoolean("success");
                    int code=jsonObeject.getInt("code");
                    if (su&&code==200){
                        JSONObject data=jsonObeject.getJSONObject("data");
                        messageInfos=new ArrayList<MessageInfo>();
                        JSONArray msgList=data.getJSONArray("msgList");
                        for (int i=0;i<msgList.length();i++){
                            JSONObject msg=msgList.getJSONObject(i);
                            MessageInfo message=new MessageInfo();
                            message.setId(msg.getString("id"));
                            message.setIs_read(msg.getString("is_read"));
                            message.setCreate_time(msg.getString("create_time"));
                            message.setContent(msg.getString("content"));
                            messageInfos.add(message);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initView();
            }

            @Override
            public boolean onCache(String result) {
                res=result;
                return true;
            }
        });

    }

    private void initView() {
        if (messageInfos.size()==0){
            listView.setVisibility(View.GONE);
        }else {
            listView.setAdapter(new MyAdapter(this));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_savebar:

                break;
        }
    }
    class MyAdapter extends BaseAdapter{
        private Context mcontext;

        public  MyAdapter(Context context){
            mcontext=context;

        }

        @Override
        public int getCount() {
            return messageInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return messageInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView==null){
                viewHolder=new ViewHolder();
                convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_message_list,null);
                viewHolder.timeicon= (ImageView) convertView.findViewById(R.id.timeicon_item_meslist);
                viewHolder.time= (TextView) convertView.findViewById(R.id.time_item_meslist);
                viewHolder.conment= (TextView) convertView.findViewById(R.id.message_item_meslist);
                viewHolder.delete= (ImageView) convertView.findViewById(R.id.dele_item_messlist);
                viewHolder.linear= (LinearLayout) convertView.findViewById(R.id.linear_messlist);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            final MessageInfo messageInfo=messageInfos.get(position);
            viewHolder.time.setText(messageInfo.getCreate_time());
            if (messageInfo.getIs_read().equals("1")){
                //已读
                viewHolder.timeicon.setImageResource(R.drawable.timeicon_mes_gray);
            }else {
                viewHolder.timeicon.setImageResource(R.drawable.timeicon_mes_orange);
            }
            viewHolder.conment.setText(messageInfo.getContent());
            final ImageView imageView=viewHolder.timeicon;
            viewHolder.conment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView.setImageResource(R.drawable.timeicon_mes_gray);
                    messageInfo.setIs_read("1");
                    Intent intent=new Intent(MessageList.this,MessageActivity.class);
                    intent.putExtra("time",messageInfo.getCreate_time());
                    intent.putExtra("id",messageInfo.getId());
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);

            }
            });
            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messageInfos.remove(messageInfo);
                    notifyDataSetChanged();
                    listView.turnToNormal();
                    deleMess(messageInfo.getId());
                    if (messageInfos.size()==0){
                        listView.setVisibility(View.GONE);
                    }
                }
            });
            return convertView;
        }
        class ViewHolder{
            ImageView timeicon;
            TextView time;
            TextView conment;
            ImageView delete;
            LinearLayout linear;
        }
    }

    private void deleMess(String id) {
        BaseParams baseParams=new BaseParams("user/msgdel");
        baseParams.addParams("id",id);
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

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
