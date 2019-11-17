package com.jnxxgc.answerclient;

import com.jnxxgc.answerclient.model.AppendData;
import com.jnxxgc.answerclient.model.UserModel;
import com.jnxxgc.answerclient.net.HttpEngine;
import com.jnxxgc.answerclient.net.NetworkRequestUtil;
import com.jnxxgc.answerclient.util.Paramters;
import com.jnxxgc.answerclient.util.SharedPreferencesUtil;
import com.jnxxgc.answerclient.view.MyDialog;
import com.jnxxgc.answerclient.view.ProgressDialog;
import com.jnxxgc.answerclient.view.ToastUtil;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

public class LoginActivity extends Activity {

	// ���ؿ�
	private ProgressDialog dialogProgress = null;
	// �˺����������
	private EditText etUid, etPwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ����Ϊ�ޱ���
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ȫ����ʾ
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_login);
		// ��ʼ��
		init();
		// ��ʼ���¼�
		initEvent();
	}

	private void init() {
		dialogProgress = new ProgressDialog(LoginActivity.this, "���ڵ���...");
		etUid = (EditText) findViewById(R.id.edit_login_userid);
		etPwd = (EditText) findViewById(R.id.edit_login_pwd);
	}

	private void initEvent() {
		// ��ʼ�����밴ť�¼�
		findViewById(R.id.btn_login).setOnClickListener(new OnClickListener() {

			@SuppressLint("HandlerLeak")
			@Override
			public void onClick(View arg0) {
				// �˺����벻��Ϊ��
				final String uid = etUid.getText().toString().trim();
				final String pwd = etPwd.getText().toString().trim();
				if (uid.equals("") || pwd.equals("")) {
					ToastUtil.makeText(LoginActivity.this, "�˻��������벻����Ϊ�գ�",
							Toast.LENGTH_SHORT);
					return;
				}
				// ��ʾ���ؿ�
				dialogProgress.show();
				// �����������
				new NetworkRequestUtil() {

					@Override
					public void onSuccess(String msg, AppendData appendData) {
						dialogProgress.setText("����ɹ������ڻ�ȡ������...");
						LoginActivity.this.getUserInfo(uid,
								appendData.getToken());
					}

					@Override
					public void onFailed(String msg, String errorMsg) {
						// ֹͣչʾ���ضԻ���
						dialogProgress.dismiss();
						// ��ʾʧ��ԭ��
						ToastUtil.makeText(LoginActivity.this, msg,
								Toast.LENGTH_LONG, false);
					}
				}.login(uid, pwd);

			}
		});
		// ��������ַ
		findViewById(R.id.txt_server).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				final MyDialog dialog = new MyDialog(LoginActivity.this,
						"��������ַ", "ȷ��", "ȡ��", Paramters.ServerHostAddress, false);
				// ����ȷ����ť�¼�
				dialog.setConfrimOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						String input = dialog.getEdText().toString();
						if (!input.equals("")) {
							// ����Shared�ķ�������ַ
							SharedPreferencesUtil.shared.writeValue(
									"ServerHostAddress", input);
							// ���£���������������ַ
							Paramters.ServerHostAddress = input;
							// ����HttpEngine
							HttpEngine.serverUrl = input;
							// ��ʾ�û�
							ToastUtil.makeText(LoginActivity.this,
									"��������ַ�Ѿ�����!", Toast.LENGTH_LONG, true);
							// �رնԻ���
							dialog.dismiss();
						} else {
							ToastUtil.makeText(LoginActivity.this, "������Ϊ��Ŷ~",
									Toast.LENGTH_SHORT);
						}
					}
				});
				// ����ȡ����ť�¼�
				dialog.setCancelOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dialog.dismiss();
					}
				});
				dialog.show();

			}
		});
		findViewById(R.id.txt_resetpwd).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent inetnt = new Intent(LoginActivity.this,
								ModifyPwdActivity.class);
						startActivity(inetnt);
					}
				});
	}

	private void getUserInfo(final String uid, final String token) {

		new NetworkRequestUtil() {

			@Override
			public void onSuccess(String msg, AppendData appendData) {
				UserModel model = null;
				try {
					model = appendData.getUserInfo();
				} catch (Exception e) {
					ToastUtil.makeText(LoginActivity.this, e.getMessage(),
							Toast.LENGTH_LONG, false);
					return;
				}
				String name = model.getName();
				String className = model.getClassName();
				boolean isAdmin = model.getIsAdmin();
				// ����Token
				SharedPreferencesUtil.shared.setLoginUser(uid, token, name,
						className, isAdmin);
				HttpEngine.AccessToken = token;
				// ֹͣչʾ���ضԻ���
				dialogProgress.dismiss();
				// �رյ������
				finish();
				// ��ת����ҳ��
				startActivity(new Intent(LoginActivity.this,
						MainFrameActivity.class));
			}

			@Override
			public void onFailed(String msg, String errorMsg) {
				ToastUtil.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG,
						false);
			}
		}.getUserInfo(token, uid);
	}
}
