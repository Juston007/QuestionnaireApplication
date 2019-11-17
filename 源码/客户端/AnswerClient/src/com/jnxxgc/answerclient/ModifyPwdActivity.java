package com.jnxxgc.answerclient;

import com.jnxxgc.answerclient.model.AppendData;
import com.jnxxgc.answerclient.net.NetworkRequestUtil;
import com.jnxxgc.answerclient.util.SystemBarTintManager;
import com.jnxxgc.answerclient.util.Util;
import com.jnxxgc.answerclient.view.ProgressDialog;
import com.jnxxgc.answerclient.view.ToastUtil;

import android.os.Bundle;
import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class ModifyPwdActivity extends Activity {

	private ProgressDialog progressDialog = null;
	private TextView txtUid, txtOldPwd, txtNewPwd, txtConfrimNewPwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ȥ��logo
		getActionBar().setDisplayShowHomeEnabled(false);
		// ����
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// ���ñ���
		Util.setActionBarTilte(getActionBar(), "�޸�����");
		setContentView(R.layout.activity_modify_pwd);
		initView();
		initEvent();
		SystemBarTintManager.setStatusColor(this, getWindow());
	}

	private void initView() {
		txtUid = (TextView) findViewById(R.id.edit_Modify_Uid);
		txtOldPwd = (TextView) findViewById(R.id.edit_Modify_Old_Pwd);
		txtNewPwd = (TextView) findViewById(R.id.edit_Modify_New_Pwd);
		txtConfrimNewPwd = (TextView) findViewById(R.id.edit_Modify_Confrim_Pwd);
		progressDialog = new ProgressDialog(this, "��������������...");
	}

	private void initEvent() {
		findViewById(R.id.btn_Modify_Pwd).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						String uid = txtUid.getText().toString().trim();
						String oldPwd = txtOldPwd.getText().toString().trim();
						String newPwd = txtNewPwd.getText().toString().trim();
						String confirmNewPwd = txtConfrimNewPwd.getText()
								.toString().trim();
						if (uid.isEmpty() || oldPwd.isEmpty()
								|| newPwd.isEmpty() || confirmNewPwd.isEmpty()) {
							ToastUtil.makeText(ModifyPwdActivity.this, "����д��ȫ",
									Toast.LENGTH_SHORT);
						} else {
							if (newPwd.equals(confirmNewPwd)) {
								if (newPwd.equals(oldPwd)) {
									ToastUtil.makeText(ModifyPwdActivity.this,
											"ԭ�����������벻��һ�£�", Toast.LENGTH_SHORT);
								} else {
									// ���������޸�����
									progressDialog.show();
									new NetworkRequestUtil() {

										@Override
										public void onSuccess(String msg,
												AppendData appendData) {
											progressDialog.dismiss();
											ToastUtil
													.makeText(
															ModifyPwdActivity.this,
															"�޸ĳɹ��������ʱ���ѵ��룬�����µ������»�ȡ�������ơ�",
															Toast.LENGTH_LONG,
															true);
											String emptyStr = "";
											txtUid.setText(emptyStr);
											txtConfrimNewPwd.setText(emptyStr);
											txtNewPwd.setText(emptyStr);
											txtOldPwd.setText(emptyStr);
										}

										@Override
										public void onFailed(String msg,
												String errorMsg) {
											progressDialog.dismiss();
											ToastUtil.makeText(
													ModifyPwdActivity.this,
													msg, Toast.LENGTH_LONG,
													false);
										}
									}.modifyPwd(uid, oldPwd, newPwd);
								}
							} else {
								ToastUtil.makeText(ModifyPwdActivity.this,
										"��������ȷ�������벻һ�£�", Toast.LENGTH_SHORT);
							}
						}
					}
				});
	}

	@Override
	protected void onStop() {
		super.onStop();
		finish();
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
