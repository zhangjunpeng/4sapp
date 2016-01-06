package com.test4s.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.test4s.gdb.CP;
import com.test4s.gdb.GameInfo;
import com.test4s.myapp.R;

import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2015/12/30.
 */
public class GameAdapter extends BaseAdapter {
    Context mcontext;
    List<GameInfo> gameList;

    public GameAdapter(Context context,List<GameInfo> list){
        mcontext=context;
        gameList=list;
    }

    @Override
    public int getCount() {
        return gameList.size();
    }

    @Override
    public Object getItem(int position) {
        return gameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_cplist_listactivity,null);
            viewHolder=new ViewHolder();
            viewHolder.imageView= (ImageView) convertView.findViewById(R.id.imageView_cplist_listac);
            viewHolder.name= (TextView) convertView.findViewById(R.id.name_item_cp_listac);
            viewHolder.introduction= (TextView) convertView.findViewById(R.id.introuduction_item_cp_listac);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
            x.image().bind(viewHolder.imageView,"http://store.akamai.steamstatic.com/public/shared/images/header/globalheader_logo.png");
            viewHolder.name.setText("莽荒纪");

        }
        return convertView;
    }
    class ViewHolder{
        ImageView imageView;
        TextView name;
        TextView introduction;
    }
}
