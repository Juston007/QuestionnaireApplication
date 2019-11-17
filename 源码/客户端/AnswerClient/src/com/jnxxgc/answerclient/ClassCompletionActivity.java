package com.jnxxgc.answerclient;

import java.util.ArrayList;
import java.util.HashMap;

import com.jnxxgc.answerclient.model.AppendData;
import com.jnxxgc.answerclient.model.UserModel;
import com.jnxxgc.answerclient.net.HttpEngine;
import com.jnxxgc.answerclient.net.NetworkRequestUtil;
import com.jnxxgc.answerclient.util.SystemBarTintManager;
import com.jnxxgc.answerclient.util.Util;
import com.jnxxgc.answerclient.view.ProgressDialog;
import com.jnxxgc.answerclient.view.ToastUtil;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

public class ClassCompletionActivity extends Activity {

	private ArrayList<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();

	private TextView txtClassCount, txtClassUnFinishCount;
	private ListView lvData;
	private ProgressDialog progressDialog = null;

	private SimpleAdapter simpleAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉logo
		getActionBar().setDisplayShowHomeEnabled(false);
		// 返回
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// 设置标题
		Util.setActionBarTilte(getActionBar(), "完成情况");
		setContentView(R.layout.activity_class_completion);
		initView();
		initData();
		initAdapter();
		SystemBarTintManager.setStatusColor(this, getWindow());
	}

	private void initView() {
		txtClassCount = (TextView) findViewById(R.id.txt_Class_Count);
		txtClassUnFinishCount = (TextView) findViewById(R.id.txt_Class_UnFinish_Count);
		lvData = (ListView) findViewById(R.id.lv_Class_Comption);
		progressDialog = new ProgressDialog(this, "正在请求数据中...");
	}

	private void initData() {
		progressDialog.show();
		new NetworkRequestUtil() {

			@Override
			public void onSuccess(String msg, AppendData appendData) {
				progressDialog.dismiss();
				ArrayList<UserModel> userList = null;
				try {
					userList = appendData.getClassCompletion();
				} catch (Exception e) {
					ToastUtil.makeText(ClassCompletionActivity.this,
							e.getMessage(), Toast.LENGTH_LONG, false);
					return;
				}
				int unFinishCount = 0;
				for (int i = 0; i < userList.size(); i++) {
					HashMap<String, Object> hashMap = new HashMap<String, Object>();
					UserModel model = userList.get(i);
					hashMap.put("Name", model.getName());
					hashMap.put("Status",
							model.getIsComplete() ? R.drawable.check
									: R.drawable.unckeck);
					// 未完成
					if (!model.getIsComplete()) {
						unFinishCount++;
					}
					dataList.add(hashMap);
				}
				// 显示数据
				txtClassCount.setText(String.valueOf(dataList.size()));
				txtClassUnFinishCount.setText(String.valueOf(unFinishCount));
				simpleAdapter.notifyDataSetChanged();
			}

			@Override
			public void onFailed(String msg, String errorMsg) {
				progressDialog.dismiss();
				ToastUtil.makeText(ClassCompletionActivity.this, msg,
						Toast.LENGTH_LONG, false);
				finish();
			}

		}.getClassCompletion(HttpEngine.AccessToken, getIntent()
				.getStringExtra("ClassName"));
	}

	private void initAdapter() {
		simpleAdapter = new SimpleAdapter(this, dataList,
				R.layout.item_class_comption,
				new String[] { "Name", "Status" }, new int[] {
						R.id.txt_Item_Comption_Name,
						R.id.txt_Item_Comption_Status });
		lvData.setAdapter(simpleAdapter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (progressDialog.isShowing())
			progressDialog.dismiss();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home)
			finish();
		return super.onOptionsItemSelected(item);
	}
}
