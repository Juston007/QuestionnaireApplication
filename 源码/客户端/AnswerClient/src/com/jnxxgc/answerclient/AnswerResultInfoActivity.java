package com.jnxxgc.answerclient;

import java.util.ArrayList;

import com.jnxxgc.answerclient.adapter.AnswerResultAdapter;
import com.jnxxgc.answerclient.util.AnswerUtil;
import com.jnxxgc.answerclient.util.SystemBarTintManager;
import com.jnxxgc.answerclient.util.Util;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class AnswerResultInfoActivity extends Activity {

	private TextView txtQuestionCount, txtPassCount;
	private GridView gvInfo;

	private ArrayList<Boolean> data = new ArrayList<Boolean>();

	private AnswerResultAdapter answerResultAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉logo
		getActionBar().setDisplayShowHomeEnabled(false);
		// 返回
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// 设置标题
		Util.setActionBarTilte(getActionBar(), "答题详情");
		setContentView(R.layout.activity_answer_result_info);
		initView();
		initData();
		initAdapter();
		initEvent();
		SystemBarTintManager.setStatusColor(this, getWindow());
	}

	private void initEvent() {
		gvInfo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(AnswerResultInfoActivity.this,
						QuestionDetailsActivity.class);
				intent.putExtra("Index", arg2);
				startActivity(intent);
			}
		});
	}

	private void initAdapter() {
		answerResultAdapter = new AnswerResultAdapter(data, this);
		gvInfo.setAdapter(answerResultAdapter);
	}

	private void initData() {
		data.clear();
		data.addAll(AnswerUtil.getAnswerInfo());
		txtQuestionCount.setText(String.valueOf(data.size()));
		int passCount = 0;
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i))
				passCount++;
		}
		txtPassCount.setText(String.valueOf(passCount));
	}

	private void initView() {
		txtQuestionCount = (TextView) findViewById(R.id.txt_Question_Count);
		txtPassCount = (TextView) findViewById(R.id.txt_Answer_Result_Pass_Count);
		gvInfo = (GridView) findViewById(R.id.gv_Answer_Info);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home)
			finish();
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
}
