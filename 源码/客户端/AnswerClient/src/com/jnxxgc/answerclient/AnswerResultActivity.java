package com.jnxxgc.answerclient;

import com.jnxxgc.answerclient.util.AnswerUtil;
import com.jnxxgc.answerclient.util.SystemBarTintManager;
import com.jnxxgc.answerclient.util.Util;
import com.jnxxgc.answerclient.view.ToastUtil;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class AnswerResultActivity extends Activity {

	private TextView txtDate, txtTime, txtGrade;
	IUiListener listener = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉logo
		getActionBar().setDisplayShowHomeEnabled(false);
		// 返回
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// 设置标题
		Util.setActionBarTilte(getActionBar(), "答题结果");
		setContentView(R.layout.activity_answer_result);
		initView();
		initEvent();
		displayInfo();
		SystemBarTintManager.setStatusColor(this, getWindow());
	}

	private void displayInfo() {
		int grade = getIntent().getIntExtra("Grade", 0);
		txtGrade.setText(String.valueOf(grade));
		txtDate.setText(Util.ToTimeStringFromLong(System.currentTimeMillis()));
		long time = AnswerUtil.submitTime - AnswerUtil.startTime;
		time /= 1000;
		txtTime.setText("用时：" + Util.secToTime((int) time));
	}

	private void initView() {
		txtDate = (TextView) findViewById(R.id.txt_Answer_Result_Date);
		txtTime = (TextView) findViewById(R.id.txt_Answer_Result_Time);
		txtGrade = (TextView) findViewById(R.id.txt_Answer_Result_Grade);
	}

	private void initEvent() {
		listener = new IUiListener() {

			@Override
			public void onError(UiError arg0) {
				ToastUtil.makeText(AnswerResultActivity.this,
						arg0.errorMessage, Toast.LENGTH_LONG, false);
			}

			@Override
			public void onComplete(Object arg0) {
				ToastUtil.makeText(AnswerResultActivity.this, "分享完成",
						Toast.LENGTH_LONG, true);
			}

			@Override
			public void onCancel() {
				ToastUtil.makeText(AnswerResultActivity.this, "用户取消分享",
						Toast.LENGTH_LONG);
			}
		};
		findViewById(R.id.ll_Answer_Result_Info).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// 跳转到答题情况
						Intent intent = new Intent(AnswerResultActivity.this,
								AnswerResultInfoActivity.class);
						startActivity(intent);
					}
				});

		findViewById(R.id.ll_Answer_Result_Share).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// 分享到QQ
						Util.share(AnswerResultActivity.this,
								getApplicationContext(), getIntent()
										.getIntExtra("Grade", 0), listener);
					}
				});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home)
			finish();
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Tencent.onActivityResultData(requestCode, resultCode, data, listener);
	}
}
