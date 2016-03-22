package com.view.messagecenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test4s.myapp.R;

/**
 * Created by Administrator on 2016/3/16.
 */
public class SwipAdapter extends BaseAdapter {

    private Context mcontext;

    private  int mRightWidth=0;

    private IOnItemRightClickListener mListener=null;

    public interface IOnItemRightClickListener {
        void onRightClick(View v, int position);
    }

    public SwipAdapter(Context context,int rightWidth,IOnItemRightClickListener l){
        mcontext=context;
        mRightWidth=rightWidth;
        mListener=l;
    }


    @Override
    public int getCount() {
        return 100;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder item;
        final int thisPosition = position;
        if (convertView == null) {
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.item_message_list, parent, false);
//            item = new ViewHolder();
//            item.item_left = (View)convertView.findViewById(R.id.item_left);
//            item.item_right = (View)convertView.findViewById(R.id.item_right);
//            item.item_left_txt = (TextView)convertView.findViewById(R.id.item_left_txt);
//            item.item_right_txt = (TextView)convertView.findViewById(R.id.item_right_txt);
//            convertView.setTag(item);
        } else {// 有直接获得ViewHolder
            item = (ViewHolder)convertView.getTag();
        }
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
//        item.item_left.setLayoutParams(lp1);
//        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(mRightWidth, LinearLayout.LayoutParams.MATCH_PARENT);
//        item.item_right.setLayoutParams(lp2);
//        item.item_left_txt.setText("item " + thisPosition);
//        item.item_right_txt.setText("delete " + thisPosition);
//        item.item_right.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mListener != null) {
//                    mListener.onRightClick(v, thisPosition);
//                }
//            }
//        });
        return convertView;
    }
    private class ViewHolder {
        View item_left;

        View item_right;

        TextView item_left_txt;

        TextView item_right_txt;
    }
}
