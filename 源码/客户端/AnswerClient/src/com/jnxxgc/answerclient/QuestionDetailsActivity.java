package com.jnxxgc.answerclient;

import com.jnxxgc.answerclient.model.QuestionModel;
import com.jnxxgc.answerclient.util.AnswerUtil;
import com.jnxxgc.answerclient.util.SystemBarTintManager;
import com.jnxxgc.answerclient.util.Util;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;

public class QuestionDetailsActivity extends Activity {

	private TextView txtContent, txtOption1Content, txtOption2Content,
			txtOption3Content, txtOption4Content, txtStatus;
	private ImageView imgOption1, imgOption2, imgOption3, imgOption4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉logo
		getActionBar().setDisplayShowHomeEnabled(false);
		// 返回
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// 设置标题
		Util.setActionBarTilte(getActionBar(), "题目解析");
		setContentView(R.layout.activity_question_details);
		initView();
		initData();
		SystemBarTintManager.setStatusColor(this, getWindow());
	}

	private void initView() {
		txtStatus = (TextView) findViewById(R.id.txt_Details_Status);
		txtContent = (TextView) findViewById(R.id.txt_Details_Content);
		txtOption1Content = (TextView) findViewById(R.id.txt_Details_Option1_Content);
		txtOption2Content = (TextView) findViewById(R.id.txt_Details_Option2_Content);
		txtOption3Content = (TextView) findViewById(R.id.txt_Details_Option3_Content);
		txtOption4Content = (TextView) findViewById(R.id.txt_Details_Option4_Content);
		imgOption1 = (ImageView) findViewById(R.id.img_Details_Option1);
		imgOption2 = (ImageView) findViewById(R.id.img_Details_Option2);
		imgOption3 = (ImageView) findViewById(R.id.img_Details_Option3);
		imgOption4 = (ImageView) findViewById(R.id.img_Details_Option4);
	}

	private void initData() {
		imgOption1.setImageBitmap(null);
		imgOption2.setImageBitmap(null);
		imgOption3.setImageBitmap(null);
		imgOption4.setImageBitmap(null);
		int index = getIntent().getIntExtra("Index", 0);
		// 将信息显示到界面
		QuestionModel model = AnswerUtil.questionList.get(index);
		txtContent.setText(model.getContent());
		txtOption1Content.setText(model.getOption1());
		txtOption2Content.setText(model.getOption2());
		String option3Content = model.getOption3();
		// 决定要显示还是隐藏
		findViewById(R.id.ll_details_option3).setVisibility(
				option3Content.isEmpty() ? View.GONE : View.VISIBLE);
		txtOption3Content.setText(option3Content);
		String option4Content = model.getOption4();
		findViewById(R.id.ll_details_option4).setVisibility(
				option4Content.isEmpty() ? View.GONE : View.VISIBLE);
		txtOption4Content.setText(model.getOption4());
		switch (model.getPassOption()) {
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
		// 如果用户选错了，把选择的用错号标出来
		int userSelectedOption = AnswerUtil.resultList.get(index).intValue();
		// 隐藏
		findViewById(R.id.ll_Details_Status).setVisibility(View.GONE);
		if (model.getPassOption() != userSelectedOption) {
			switch (userSelectedOption) {
			case 1:
				imgOption1.setImageResource(R.drawable.unckeck);
				break;
			case 2:
				imgOption2.setImageResource(R.drawable.unckeck);
				break;
			case 3:
				imgOption3.setImageResource(R.drawable.unckeck);
				break;
			case 4:
				imgOption4.setImageResource(R.drawable.unckeck);
				break;

			default:
				// 显示出来
				findViewById(R.id.ll_Details_Status)
						.setVisibility(View.VISIBLE);
				txtStatus.setText("这道题您弃选了");
				break;
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}

}
