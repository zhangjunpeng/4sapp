package com.view.messagecenter;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.delslidelistview.OnDeleteListioner;


public class SlideAdapter extends BaseAdapter {

	private Context mContext;
	private LinkedList<String> mlist = null;
	private OnDeleteListioner mOnDeleteListioner;
	private boolean delete = false;

	// private Button curDel_btn = null;
	// private UpdateDate mUpdateDate = null;

	public SlideAdapter(Context mContext, LinkedList<String> mlist) {
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

	public int getCount() {

		return mlist.size();
	}

	public Object getItem(int pos) {
		return mlist.get(pos);
	}

	public long getItemId(int pos) {
		return pos;
	}

	public View getView(final int pos, View convertView, ViewGroup p) {
		final ViewHolder viewHolder;
		if (convertView == null) {



		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final OnClickListener mOnClickListener = new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (mOnDeleteListioner != null)
					mOnDeleteListioner.onDelete(pos);

			}
		};

		return convertView;
	}

	public static class ViewHolder {


	}

}
