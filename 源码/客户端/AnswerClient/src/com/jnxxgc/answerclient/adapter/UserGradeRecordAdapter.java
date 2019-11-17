package com.jnxxgc.answerclient.adapter;

import java.util.ArrayList;

import com.jnxxgc.answerclient.R;
import com.jnxxgc.answerclient.model.AnswerRecordModel;
import com.jnxxgc.answerclient.util.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UserGradeRecordAdapter extends BaseAdapter {

	private ArrayList<AnswerRecordModel> recordList = new ArrayList<AnswerRecordModel>();
	private Context context;

	public UserGradeRecordAdapter(ArrayList<AnswerRecordModel> recordList,
			Context context) {
		this.recordList = recordList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return recordList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return recordList.get(arg0);
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
			arg1 = LayoutInflater.from(context).inflate(
					R.layout.item_answer_record, null);
			vh.txtGrade = (TextView) arg1
					.findViewById(R.id.txt_Item_Record_Grade);
			vh.txtName = (TextView) arg1
					.findViewById(R.id.txt_Item_Record_Name);
			vh.txtTime = (TextView) arg1
					.findViewById(R.id.txt_Item_Record_Time);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}
		AnswerRecordModel model = recordList.get(arg0);
		vh.txtGrade.setText(String.valueOf(model.getGrade()));
		vh.txtName.setText(model.getName());
		vh.txtTime.setText(Util.ToTimeStringFromLong(model.getSubmitTime()
				.getTime()));
		return arg1;
	}

	private class ViewHolder {
		private TextView txtName, txtGrade, txtTime;
	}
}
