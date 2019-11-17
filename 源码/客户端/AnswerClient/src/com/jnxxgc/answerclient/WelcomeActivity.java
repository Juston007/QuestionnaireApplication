package com.jnxxgc.answerclient;

import com.jnxxgc.answerclient.net.HttpEngine;
import com.jnxxgc.answerclient.util.Paramters;
import com.jnxxgc.answerclient.util.SharedPreferencesUtil;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class WelcomeActivity extends Activity {

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// �ж�Ҫ������һ������ ���δ�����û���ô�����л���������� �����л�����ҳ��
			boolean islogin = SharedPreferencesUtil.shared.isLogin();
			Intent intent = new Intent(WelcomeActivity.this,
					islogin ? MainFrameActivity.class : LoginActivity.class);
			startActivity(intent);
			// ����aty��atyջ���г�ջ
			WelcomeActivity.this.finish();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// �ޱ���
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ȫ����ʾ
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_welcome);
		// ��ʼ��
		init();
		// ��ʱ2��
		new Thread() {
			public void run() {
				try {
					Thread.sleep(2000);
					handler.sendEmptyMessage(0);
				} catch (Exception ex) {
					Log.d("IMClient", WelcomeActivity.class.getCanonicalName()
							+ ":" + ex.getMessage());
				}
			};
		}.start();
		// ���Է���
		testMethod();
	}

	private void testMethod() {
	}

	private void init() {
		// ��ʼ��SharedPreferences���
		SharedPreferencesUtil sharedutil = new SharedPreferencesUtil();
		sharedutil.setSharedPreferces(getSharedPreferences("Preferences",
				Context.MODE_PRIVATE));

		// �����������ֵ����̬�������� �������
		SharedPreferencesUtil.shared = sharedutil;

		// ��ʼ����������
		if (sharedutil.readValue("ServerHostAddress", "").equals("")) {
			Log.d("IMClient", "InitParamter");
			sharedutil.writeValue("ServerHostAddress",
					Paramters.ServerHostAddress);
			sharedutil.writeValue("ConnectionTimeOut",
					String.valueOf(Paramters.ConnectionTimeOut));
			sharedutil.writeValue("ReadTimeOut",
					String.valueOf(Paramters.ReadTimeOut));
		}

		// ��ȡ��������
		Paramters.ServerHostAddress = sharedutil.readValue("ServerHostAddress",
				String.valueOf(Paramters.ServerHostAddress));
		Paramters.ConnectionTimeOut = Integer.parseInt(sharedutil.readValue(
				"ConnectionTimeOut",
				String.valueOf(Paramters.ConnectionTimeOut)));
		Paramters.ReadTimeOut = Integer.parseInt(sharedutil.readValue(
				"ReadTimeOut", String.valueOf(Paramters.ReadTimeOut)));
		// ��ʼ��HttpEngine
		// ��������ַ
		HttpEngine.serverUrl = sharedutil.readValue("ServerHostAddress",
				"10.3.0.18:81");
		// AccessToken
		HttpEngine.AccessToken = sharedutil.getToken();
	}
}
