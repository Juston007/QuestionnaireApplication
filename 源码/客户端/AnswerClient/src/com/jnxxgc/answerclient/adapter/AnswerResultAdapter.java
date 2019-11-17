package com.jnxxgc.answerclient.adapter;

import java.util.ArrayList;

import com.jnxxgc.answerclient.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AnswerResultAdapter extends BaseAdapter {

	private ArrayList<Boolean> list = new ArrayList<Boolean>();
	private Context contetx;

	public AnswerResultAdapter(ArrayList<Boolean> list, Context contetx) {
		this.list = list;
		this.contetx = contetx;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder vh = null;
		if (arg1 == null) {
			vh = new ViewHolder();
			arg1 = LayoutInflater.from(contetx).inflate(R.layout.item_result,
					null);
			vh.txtCount = (TextView) arg1.findViewById(R.id.txt_Count);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}
		vh.txtCount
				.setBackgroundResource(list.get(arg0) ? R.drawable.item_yes
						: R.drawable.item_no);
		vh.txtCount.setText(String.valueOf(arg0 + 1));
		return arg1;
	}

	private class ViewHolder {
		public TextView txtCount;
	}

}
