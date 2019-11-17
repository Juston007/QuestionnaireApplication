package com.jnxxgc.answerclient;

import java.util.ArrayList;

import org.json.JSONException;

import com.jnxxgc.answerclient.model.AppendData;
import com.jnxxgc.answerclient.model.ClassModel;
import com.jnxxgc.answerclient.net.HttpEngine;
import com.jnxxgc.answerclient.net.NetworkRequestUtil;
import com.jnxxgc.answerclient.view.MyDialog;
import com.jnxxgc.answerclient.view.ProgressDialog;
import com.jnxxgc.answerclient.view.ToastUtil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

public class ManageFragment extends Fragment implements OnClickListener {

	private boolean isCompletion;
	private MyDialog myDialog;
	private AlertDialog.Builder builder = null;
	private ProgressDialog progressDialog = null;
	private ArrayList<ClassModel> classList = new ArrayList<ClassModel>();
	private String[] items = null;

	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_manage, null);
		initView();
		initEvent();
		return view;
	}

	private void initView() {
		myDialog = new MyDialog(getActivity(), "根据UID查看用户答题记录", "确定", "取消",
				"请输入UID", false);
		// 点击取消按钮事件
		myDialog.setCancelOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				myDialog.dismiss();
			}
		});
		// 点击确定事件
		myDialog.setConfrimOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String inputStr = myDialog.getEdText().toString().trim();
				if (inputStr.isEmpty()) {
					ToastUtil.makeText(getActivity(), "Uid不允许为空！",
							Toast.LENGTH_SHORT);
					return;
				}
				myDialog.dismiss();
				Intent intent = new Intent(getActivity(),
						UserGradeRecordActivity.class);
				intent.putExtra("Uid", inputStr);
				startActivity(intent);
			}
		});

		progressDialog = new ProgressDialog(getActivity(), "正在请求班级信息");

		builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("选择班级");
		builder.setCancelable(false);
		builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
			}
		});
	}

	private void initEvent() {
		view.findViewById(R.id.ll_Manage_Class_Comption).setOnClickListener(
				this);
		view.findViewById(R.id.ll_Manage_Class_Grade_Rank).setOnClickListener(
				this);
		view.findViewById(R.id.ll_Manage_Modify_User_Info).setOnClickListener(
				this);
		view.findViewById(R.id.ll_Manage_User_Grade_Record).setOnClickListener(
				this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ll_Manage_Class_Comption:
			isCompletion = true;
			initClassInfo();
			break;

		case R.id.ll_Manage_Class_Grade_Rank:
			isCompletion = false;
			initClassInfo();
			break;
		case R.id.ll_Manage_Modify_User_Info:
			Intent intent = new Intent(getActivity(),
					UpdateUserInfoActivity.class);
			startActivity(intent);
			break;
		case R.id.ll_Manage_User_Grade_Record:
			myDialog.show();
			break;

		default:
			break;
		}
	}

	private void initClassInfo() {
		progressDialog.show();
		new NetworkRequestUtil() {

			@Override
			public void onSuccess(String msg, AppendData appendData) {
				progressDialog.dismiss();
				try {
					classList.clear();
					classList.addAll(appendData.getClassInfoList());
					items = new String[classList.size()];
					for (int i = 0; i < classList.size(); i++) {
						items[i] = classList.get(i).getClassName();
					}
					builder.setItems(items,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									Intent intent = null;
									if (isCompletion) {
										intent = new Intent(getActivity(),
												ClassCompletionActivity.class);
									} else {
										intent = new Intent(getActivity(),
												UserGradeRecordActivity.class);
										intent.putExtra("isQueryUser", false);
									}
									intent.putExtra("ClassName",
											classList.get(arg1).getClassName());
									startActivity(intent);
								}
							});
					builder.show();
				} catch (JSONException e) {
					ToastUtil.makeText(getActivity(), e.getMessage(),
							Toast.LENGTH_LONG, false);
				}
			}

			@Override
			public void onFailed(String msg, String errorMsg) {
				progressDialog.dismiss();
				ToastUtil
						.makeText(getActivity(), msg, Toast.LENGTH_LONG, false);
			}

		}.getClassInfo(HttpEngine.AccessToken);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (myDialog.isShowing())
			myDialog.dismiss();
	}

}
