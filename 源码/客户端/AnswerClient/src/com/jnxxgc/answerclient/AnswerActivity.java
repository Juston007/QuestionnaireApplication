package com.jnxxgc.answerclient;

import org.json.JSONException;

import com.jnxxgc.answerclient.model.AppendData;
import com.jnxxgc.answerclient.model.QuestionModel;
import com.jnxxgc.answerclient.net.HttpEngine;
import com.jnxxgc.answerclient.net.NetworkRequestUtil;
import com.jnxxgc.answerclient.util.AnswerUtil;
import com.jnxxgc.answerclient.util.SystemBarTintManager;
import com.jnxxgc.answerclient.util.Util;
import com.jnxxgc.answerclient.view.MyDialog;
import com.jnxxgc.answerclient.view.ProgressDialog;
import com.jnxxgc.answerclient.view.ToastUtil;

import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class AnswerActivity extends Activity implements OnClickListener {

	// View
	private TextView txtTime, txtCount, txtContent, txtOption1Content,
			txtOption2Content, txtOption3Content, txtOption4Content;
	private ImageView imgOption1, imgOption2, imgOption3, imgOption4;
	private Button btnNext;

	// Dialog
	private ProgressDialog progressDialog = null;
	private MyDialog dialog = null;

	// Params
	private short time = 30;
	private volatile boolean isClosed = false;
	private int nowIndex = -1;
	private short selectedIndex = 0;
	
	//Handler
	private Handler handlerMsg = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (time == 0) {
				// 下一题
				nextQuestion();
			}
			txtTime.setText(time + "秒");
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉logo
		getActionBar().setDisplayShowHomeEnabled(false);
		// 返回
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// 设置标题
		Util.setActionBarTilte(getActionBar(), "答题");
		setContentView(R.layout.activity_answer);
		initView();
		initData();
		initEvent();
		SystemBarTintManager.setStatusColor(this, getWindow());
	}

	private void initView() {
		txtTime = (TextView) findViewById(R.id.txt_Answer_Time);
		txtCount = (TextView) findViewById(R.id.txt_Answer_Count);
		txtContent = (TextView) findViewById(R.id.txt_Content);
		txtOption1Content = (TextView) findViewById(R.id.txt_Option1_Content);
		txtOption2Content = (TextView) findViewById(R.id.txt_Option2_Content);
		txtOption3Content = (TextView) findViewById(R.id.txt_Option3_Content);
		txtOption4Content = (TextView) findViewById(R.id.txt_Option4_Content);
		imgOption1 = (ImageView) findViewById(R.id.img_Option1);
		imgOption2 = (ImageView) findViewById(R.id.img_Option2);
		imgOption3 = (ImageView) findViewById(R.id.img_Option3);
		imgOption4 = (ImageView) findViewById(R.id.img_Option4);
		btnNext = (Button) findViewById(R.id.btn_Next);
		progressDialog = new ProgressDialog(this, "正在请求数据中");

		dialog = new MyDialog(AnswerActivity.this, "退出答题",
				"确定要退出答题吗？\n如果退出，您的这次答题会被记为0分。", "确定", "取消", false, true);
		dialog.setCancelOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		dialog.setConfrimOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				AnswerActivity.this.finish();
			}
		});
	}

	private void initEvent() {
		findViewById(R.id.ll_Option1).setOnClickListener(this);
		findViewById(R.id.ll_Option2).setOnClickListener(this);
		findViewById(R.id.ll_Option3).setOnClickListener(this);
		findViewById(R.id.ll_Option4).setOnClickListener(this);
		btnNext.setOnClickListener(this);
	}

	private void initData() {
		progressDialog.show();
		new NetworkRequestUtil() {

			@Override
			public void onSuccess(String msg, AppendData appendData) {
				progressDialog.dismiss();
				try {
					AnswerUtil.AnswerID = Integer.parseInt(msg);
					AnswerUtil.questionList.clear();
					AnswerUtil.resultList.clear();
					AnswerUtil.questionList.addAll(appendData
							.getQuestionInfoList());
					nextQuestion();
					new Thread() {
						public void run() {
							while (!isClosed) {
								try {
									Thread.sleep(1000);
									if (time != 0)
										time--;
									handlerMsg.sendEmptyMessage(0);
								} catch (InterruptedException e) {
									Log.d("Juston", e.getMessage());
								}
							}
						};
					}.start();
				} catch (JSONException e) {
					Log.d("Juston", e.getMessage());
					ToastUtil.makeText(AnswerActivity.this, e.getMessage(),
							Toast.LENGTH_LONG, false);
					finish();
				}
			}

			@Override
			public void onFailed(String msg, String errorMsg) {
				progressDialog.dismiss();
				ToastUtil.makeText(AnswerActivity.this, msg, Toast.LENGTH_LONG,
						false);
				finish();
			}
		}.startAnswer(HttpEngine.AccessToken);
	}

	private void nextQuestion() {
		if (AnswerUtil.questionList.size() == 0) {
			ToastUtil.makeText(AnswerActivity.this, "获取不到题目信息",
					Toast.LENGTH_LONG, false);
			finish();
			return;
		}
		if (!(nowIndex < 0))
			// 保存一下结果
			AnswerUtil.resultList.add((int) selectedIndex);
		else
			AnswerUtil.startTime = System.currentTimeMillis();
		// 前进到下一题
		nowIndex++;
		if (nowIndex == AnswerUtil.questionList.size() - 1) {
			btnNext.setText("提交");
		}
		if (nowIndex == AnswerUtil.questionList.size()) {
			btnNext.setEnabled(false);
			AnswerUtil.submitTime = System.currentTimeMillis();
			isClosed = true;
			final int grade = AnswerUtil.calculateTotalGrade();
			new NetworkRequestUtil() {

				@Override
				public void onSuccess(String msg, AppendData appendData) {
					ToastUtil.makeText(AnswerActivity.this, msg,
							Toast.LENGTH_LONG, true);
					finish();
					Intent intent = new Intent(AnswerActivity.this,
							AnswerResultActivity.class);
					intent.putExtra("Grade", grade);
					startActivity(intent);
				}

				@Override
				public void onFailed(String msg, String errorMsg) {
					ToastUtil.makeText(AnswerActivity.this, msg,
							Toast.LENGTH_LONG, false);
					finish();
				}

			}.submitAnswer(HttpEngine.AccessToken, AnswerUtil.AnswerID, grade);
			return;
		}
		// 将信息显示到界面
		QuestionModel model = AnswerUtil.questionList.get(nowIndex);
		txtContent.setText(model.getContent());
		txtOption1Content.setText(model.getOption1());
		txtOption2Content.setText(model.getOption2());

		String option3Content = model.getOption3();
		// 决定要显示还是隐藏
		findViewById(R.id.ll_Option3).setVisibility(
				option3Content.isEmpty() ? View.GONE : View.VISIBLE);
		txtOption3Content.setText(option3Content);
		String option4Content = model.getOption4();
		findViewById(R.id.ll_Option4).setVisibility(
				option4Content.isEmpty() ? View.GONE : View.VISIBLE);
		txtOption4Content.setText(model.getOption4());
		time = 30;
		txtCount.setText((nowIndex + 1) + "/" + AnswerUtil.questionList.size());
		checkOption((short) 0);
	}

	private void checkOption(short optionIndex) {
		imgOption1.setImageBitmap(null);
		imgOption2.setImageBitmap(null);
		imgOption3.setImageBitmap(null);
		imgOption4.setImageBitmap(null);
		selectedIndex = optionIndex;
		switch (selectedIndex) {
		case 1:
			imgOption1.setImageResource(R.drawable.check);
			break;
		case 2:
			imgOption2.setImageResource(R.drawable.check);
			break;
		case 3:
			imgOption3.setImageResource(R.drawable.check);
			break;
		case 4:
			imgOption4.setImageResource(R.drawable.check);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			dialog.show();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isClosed = true;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ll_Option1:
			checkOption((short) 1);
			break;
		case R.id.ll_Option2:
			checkOption((short) 2);
			break;
		case R.id.ll_Option3:
			checkOption((short) 3);
			break;
		case R.id.ll_Option4:
			checkOption((short) 4);
			break;
		case R.id.btn_Next:
			nextQuestion();
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		dialog.show();
	}
}
