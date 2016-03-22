package com.view.Evaluation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.app.delslidelistview.OnDeleteListioner;

import java.util.LinkedList;

/**
 * Created by Administrator on 2016/3/17.
 */
public class EvaluationAdapter extends BaseAdapter {

    private Context mContext;
    private LinkedList<String> mlist = null;
    private OnDeleteListioner mOnDeleteListioner;
    private boolean delete = false;

    // private Button curDel_btn = null;
    // private UpdateDate mUpdateDate = null;

    public EvaluationAdapter(Context mContext, LinkedList<String> mlist) {
        this.mContext = mContext;
        this.mlist = mlist;

    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setOnDeleteListioner(OnDeleteListioner mOnDeleteListioner) {
        this.mOnDeleteListioner = mOnDeleteListioner;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
