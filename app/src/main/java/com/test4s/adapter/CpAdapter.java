package com.test4s.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.test4s.gdb.CP;
import com.test4s.myapp.R;

import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2015/12/24.
 */
public class CpAdapter extends BaseAdapter {
    Context mcontext;
    List<CP> cpList;

    public CpAdapter(Context context,List<CP> list){
        mcontext=context;
        cpList=list;
    }

    @Override
    public int getCount() {
        return cpList.size();
    }

    @Override
    public Object getItem(int position) {
        return cpList.get(position);
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
            viewHolder.name.setText("steam");
            viewHolder.introduction.setText("Steam平台是Valve公司聘请BitTorrent(BT下载)发明者布拉姆·科恩亲自开发设计的游戏平台。\n" +
                    "Steam平台是一款目前全球最大的综合性数字发行平台。玩家可以在该平台购买游戏、软件、下载、讨论、上传、分享。\n" +
                    "2015年10月，获第33届金摇杆奖最佳游戏平台。");
        }
        return convertView;
    }
    class ViewHolder{
        ImageView imageView;
        TextView name;
        TextView introduction;
    }
}
