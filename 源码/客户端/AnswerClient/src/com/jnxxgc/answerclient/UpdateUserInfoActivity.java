package com.jnxxgc.answerclient;

import java.util.ArrayList;

import org.json.JSONException;

import com.jnxxgc.answerclient.model.AppendData;
import com.jnxxgc.answerclient.model.ClassModel;
import com.jnxxgc.answerclient.model.UserModel;
import com.jnxxgc.answerclient.net.HttpEngine;
import com.jnxxgc.answerclient.net.NetworkRequestUtil;
import com.jnxxgc.answerclient.util.SystemBarTintManager;
import com.jnxxgc.answerclient.util.Util;
import com.jnxxgc.answerclient.view.MyDialog;
import com.jnxxgc.answerclient.view.ProgressDialog;
import com.jnxxgc.answerclient.view.ToastUtil;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateUserInfoActivity extends Activity {

	private TextView txtName, txtUid, txtClssName;
	private ProgressDialog progressDialog = null;
	private MyDialog nameDialog = null, UidDialog = null;
	private AlertDialog.Builder builder = null;
	private ArrayList<ClassModel> classList = new ArrayList<ClassModel>();
	private String[] items = null;
	private int classID = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ȥ��logo
		getActionBar().setDisplayShowHomeEnabled(false);
		// ����
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// ���ñ���
		Util.setActionBarTilte(getActionBar(), "�����û�����");
		setContentView(R.layout.activity_update_user_info);
		initView();
		initEvent();
		SystemBarTintManager.setStatusColor(this, getWindow());
	}

	private void initView() {
		txtName = (TextView) findViewById(R.id.txt_info_Name);
		txtUid = (TextView) findViewById(R.id.txt_info_Uid);
		txtClssName = (TextView) findViewById(R.id.txt_info_Class_Name);
		progressDialog = new ProgressDialog(this, "��������������...");
		nameDialog = new MyDialog(this, "��д����", "ȷ��", "ȡ��", "", false);
		UidDialog = new MyDialog(this, "��дUid", "ȷ��", "ȡ��", "", false);
		builder = new Builder(this);
		builder.setTitle("ѡ��༶");
	}

	private void initEvent() {
		nameDialog.setConfrimOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String str = nameDialog.getEdText().toString().trim();
				if (str.isEmpty()) {
					ToastUtil.makeText(UpdateUserInfoActivity.this, "������Ϊ�գ�",
							Toast.LENGTH_SHORT);
					return;
				}
				txtName.setText(str);
				nameDialog.dismiss();
			}
		});
		UidDialog.setConfrimOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String str = UidDialog.getEdText().toString().trim();
				if (str.isEmpty()) {
					ToastUtil.makeText(UpdateUserInfoActivity.this, "������Ϊ�գ�",
							Toast.LENGTH_SHORT);
					return;
				}
				txtUid.setText(str);
				UidDialog.dismiss();
			}
		});
		nameDialog.setCancelOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				nameDialog.dismiss();
			}
		});
		UidDialog.setCancelOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				UidDialog.dismiss();
			}
		});
		findViewById(R.id.ll_info_Class_Name).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						getClassInfo();
					}
				});
		findViewById(R.id.ll_info_Name).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						nameDialog.setEdText(txtName.getText().toString()
								.trim());
						nameDialog.show();
					}
				});
		findViewById(R.id.ll_info_Uid).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						UidDialog.setEdText(txtUid.getText().toString().trim());
						UidDialog.show();
					}
				});
		findViewById(R.id.btn_Update).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// �ύ
				String uid = txtUid.getText().toString().trim();
				String name = txtName.getText().toString().trim();
				String className = txtClssName.getText().toString().trim();
				if (uid.isEmpty() || name.isEmpty() || className.isEmpty()) {
					ToastUtil.makeText(UpdateUserInfoActivity.this, "������д������",
							Toast.LENGTH_LONG);
					return;
				}
				if (classID == -1) {
					ToastUtil.makeText(UpdateUserInfoActivity.this,
							"����û��ѡ��һ���༶", Toast.LENGTH_LONG);
					return;
				}
				progressDialog.show();
				new NetworkRequestUtil() {

					@Override
					public void onSuccess(String msg, AppendData appendData) {
						progressDialog.dismiss();
						ToastUtil.makeText(UpdateUserInfoActivity.this,
								"�����û����ϳɹ�!���µ������Ϣ����ͬ��", Toast.LENGTH_LONG,
								true);
					}

					@Override
					public void onFailed(String msg, String errorMsg) {
						progressDialog.dismiss();
						ToastUtil.makeText(UpdateUserInfoActivity.this, msg,
								Toast.LENGTH_LONG, false);
					}
				}.updateUserInfo(HttpEngine.AccessToken, uid, name, classID);
			}
		});
		findViewById(R.id.btn_Read).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// ��ȡ
				String uid = txtUid.getText().toString().trim();
				if (uid.isEmpty()) {
					ToastUtil.makeText(UpdateUserInfoActivity.this, "����дUID",
							Toast.LENGTH_LONG);
					return;
				}
				progressDialog.show();
				new NetworkRequestUtil() {

					@Override
					public void onSuccess(String msg, AppendData appendData) {
						progressDialog.dismiss();
						UserModel userModel = null;
						try {
							userModel = appendData.getUserInfo();
						} catch (Exception e) {
							ToastUtil.makeText(UpdateUserInfoActivity.this,
									e.getMessage(), Toast.LENGTH_LONG, false);
						}
						txtName.setText(userModel.getName());
						txtClssName.setText(userModel.getClassName());
					}

					@Override
					public void onFailed(String msg, String errorMsg) {
						progressDialog.dismiss();
						ToastUtil.makeText(UpdateUserInfoActivity.this, msg,
								Toast.LENGTH_LONG, false);
					}

				}.getUserInfo(HttpEngine.AccessToken, uid);
			}
		});
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
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
	protected void onDestroy() {
		super.onDestroy();
		if (progressDialog.isShowing())
			progressDialog.dismiss();
	}

	private void getClassInfo() {

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
									ClassModel classModel = classList.get(arg1);
									classID = classModel.getClassId();
									txtClssName.setText(classModel
											.getClassName());
								}
							});
					builder.show();
				} catch (JSONException e) {
					ToastUtil.makeText(UpdateUserInfoActivity.this,
							e.getMessage(), Toast.LENGTH_LONG, false);
				}
			}

			@Override
			public void onFailed(String msg, String errorMsg) {
				progressDialog.dismiss();
				ToastUtil.makeText(UpdateUserInfoActivity.this, msg,
						Toast.LENGTH_LONG, false);
			}

		}.getClassInfo(HttpEngine.AccessToken);

	}

}
