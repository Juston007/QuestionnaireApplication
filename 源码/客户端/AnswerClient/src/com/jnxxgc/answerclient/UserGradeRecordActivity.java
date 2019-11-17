package com.jnxxgc.answerclient;

import java.util.ArrayList;

import com.jnxxgc.answerclient.adapter.UserGradeRecordAdapter;
import com.jnxxgc.answerclient.model.AnswerRecordModel;
import com.jnxxgc.answerclient.model.AppendData;
import com.jnxxgc.answerclient.net.HttpEngine;
import com.jnxxgc.answerclient.net.NetworkRequestUtil;
import com.jnxxgc.answerclient.util.SystemBarTintManager;
import com.jnxxgc.answerclient.util.Util;
import com.jnxxgc.answerclient.view.ProgressDialog;
import com.jnxxgc.answerclient.view.ToastUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class UserGradeRecordActivity extends Activity {

	private boolean isQueryUser = true;

	private ProgressDialog progressDialog = null;

	private TextView txtTotalCount, txtMaxGrade, txtDisplayText;
	private ListView lvAnswerRecord;

	private ArrayList<AnswerRecordModel> recordList = new ArrayList<AnswerRecordModel>();

	private UserGradeRecordAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉logo
		getActionBar().setDisplayShowHomeEnabled(false);
		// 返回
		getActionBar().setDisplayHomeAsUpEnabled(true);
		isQueryUser = getIntent().getBooleanExtra("isQueryUser", true);
		// 设置标题
		Util.setActionBarTilte(getActionBar(), isQueryUser ? "历史成绩" : "班级排名");
		setContentView(R.layout.activity_user_grade_record);
		initView();
		initData();
		initAdapter();
		SystemBarTintManager.setStatusColor(this, getWindow());
	}

	private void initData() {
		progressDialog.show();
		NetworkRequestUtil netRequest = new NetworkRequestUtil() {

			@Override
			public void onSuccess(String msg, AppendData appendData) {
				progressDialog.dismiss();
				try {
					recordList.addAll(appendData.getAnswerReocrd());
				} catch (Exception e) {
					ToastUtil.makeText(UserGradeRecordActivity.this,
							e.getMessage(), Toast.LENGTH_LONG, false);
					finish();
				}
				displayData();
			}

			@Override
			public void onFailed(String msg, String errorMsg) {
				progressDialog.dismiss();
				ToastUtil.makeText(UserGradeRecordActivity.this, msg,
						Toast.LENGTH_LONG, false);
				finish();
			}

		};
		if (isQueryUser) {
			netRequest.getUserGradeRecord(HttpEngine.AccessToken, getIntent()
					.getStringExtra("Uid"));
		} else {
			netRequest.getClassGradeRank(HttpEngine.AccessToken, getIntent()
					.getStringExtra("ClassName"));
		}
	}

	private void displayData() {
		txtTotalCount.setText(String.valueOf(recordList.size()));
		// 计算最好成绩
		int maxGrade = 0;
		for (int i = 0; i < recordList.size(); i++) {
			AnswerRecordModel model = recordList.get(i);
			int tempGrade = model.getGrade();
			if (tempGrade > maxGrade) {
				maxGrade = tempGrade;
			}
		}
		if (isQueryUser)
			for (int i = 1; i < recordList.size(); i++) {
				AnswerRecordModel temp = recordList.get(i);
				int j = 0;
				for (j = i; j > 0
						&& recordList.get((j - 1)).getSubmitTime().getTime() < temp
								.getSubmitTime().getTime(); j--) {
					recordList.set(j, recordList.get(j - 1));
				}
				recordList.set(j, temp);
			}

		txtMaxGrade.setText(String.valueOf(maxGrade));
		adapter.notifyDataSetChanged();
	}

	private void initView() {
		txtTotalCount = (TextView) findViewById(R.id.txt_User_Grade_Record_Total_Count);
		txtMaxGrade = (TextView) findViewById(R.id.txt_User_Grade_Record_Max_Grade);
		lvAnswerRecord = (ListView) findViewById(R.id.lv_User_Grade_Record);
		progressDialog = new ProgressDialog(this, "正在加载数据中...");
		txtDisplayText = (TextView) findViewById(R.id.txt_User_Grade_Record_Display_Text);
		txtDisplayText.setText(isQueryUser ? "答题次数" : "班级人数");
		if (MainFrameActivity.loginUserModel.getIsAdmin())
			if (!isQueryUser) {
				lvAnswerRecord
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								Intent intent = new Intent(
										UserGradeRecordActivity.this,
										UserGradeRecordActivity.class);
								String uid = recordList.get(arg2).getUid();
								Log.d("Juston", uid);
								intent.putExtra("Uid", uid);
								startActivity(intent);
							}
						});
			}
	}

	private void initAdapter() {
		adapter = new UserGradeRecordAdapter(recordList, this);
		lvAnswerRecord.setAdapter(adapter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home)
			finish();
		return super.onOptionsItemSelected(item);
	}

}
