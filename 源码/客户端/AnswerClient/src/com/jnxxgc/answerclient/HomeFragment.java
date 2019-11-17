package com.jnxxgc.answerclient;

import com.jnxxgc.answerclient.view.MyDialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class HomeFragment extends Fragment implements OnClickListener {

	private View view = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_home_fragment, null);
		this.view = view;
		initEvent();
		return view;
	}

	private void initEvent() {
		view.findViewById(R.id.ll_Home_Class_Rank).setOnClickListener(this);
		view.findViewById(R.id.ll_Home_Grade_Record).setOnClickListener(this);
		view.findViewById(R.id.ll_Home_Start_Answer).setOnClickListener(this);
		view.findViewById(R.id.ll_Home_Class_Completion).setOnClickListener(
				this);
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.ll_Home_Class_Completion:
			intent = new Intent(getActivity(), ClassCompletionActivity.class);
			intent.putExtra("ClassName",
					MainFrameActivity.loginUserModel.getClassName());
			break;
		case R.id.ll_Home_Class_Rank:
			intent = new Intent(getActivity(), UserGradeRecordActivity.class);
			intent.putExtra("isQueryUser", false);
			intent.putExtra("ClassName",
					MainFrameActivity.loginUserModel.getClassName());
			break;
		case R.id.ll_Home_Grade_Record:
			intent = new Intent(getActivity(), UserGradeRecordActivity.class);
			intent.putExtra("Uid", MainFrameActivity.loginUserModel.getUid());
			break;
		case R.id.ll_Home_Start_Answer:
			final MyDialog dialog = new MyDialog(
					getActivity(),
					"��ʼ����",
					"ȷ��Ҫ��ʼ������\n���ظ����⡣\n�༶������ȡÿ����óɼ���\nÿ�����ƴ���ʱ��30�룬ÿ��2�֣���50����",
					"ȷ��", "ȡ��", false, true);
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
					Intent intent = new Intent(getActivity(),
							AnswerActivity.class);
					startActivity(intent);
				}
			});
			dialog.show();
			break;
		default:
			break;
		}
		if (intent != null)
			startActivity(intent);
	}
}
