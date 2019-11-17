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

	// 加载框
	private ProgressDialog dialogProgress = null;
	// 账号密码输入框
	private EditText etUid, etPwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置为无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_login);
		// 初始化
		init();
		// 初始化事件
		initEvent();
	}

	private void init() {
		dialogProgress = new ProgressDialog(LoginActivity.this, "正在登入...");
		etUid = (EditText) findViewById(R.id.edit_login_userid);
		etPwd = (EditText) findViewById(R.id.edit_login_pwd);
	}

	private void initEvent() {
		// 初始化登入按钮事件
		findViewById(R.id.btn_login).setOnClickListener(new OnClickListener() {

			@SuppressLint("HandlerLeak")
			@Override
			public void onClick(View arg0) {
				// 账号密码不可为空
				final String uid = etUid.getText().toString().trim();
				final String pwd = etPwd.getText().toString().trim();
				if (uid.equals("") || pwd.equals("")) {
					ToastUtil.makeText(LoginActivity.this, "账户名和密码不可以为空！",
							Toast.LENGTH_SHORT);
					return;
				}
				// 显示加载框
				dialogProgress.show();
				// 发起登入请求
				new NetworkRequestUtil() {

					@Override
					public void onSuccess(String msg, AppendData appendData) {
						dialogProgress.setText("登入成功！正在获取资料中...");
						LoginActivity.this.getUserInfo(uid,
								appendData.getToken());
					}

					@Override
					public void onFailed(String msg, String errorMsg) {
						// 停止展示加载对话框
						dialogProgress.dismiss();
						// 显示失败原因
						ToastUtil.makeText(LoginActivity.this, msg,
								Toast.LENGTH_LONG, false);
					}
				}.login(uid, pwd);

			}
		});
		// 服务器地址
		findViewById(R.id.txt_server).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				final MyDialog dialog = new MyDialog(LoginActivity.this,
						"服务器地址", "确定", "取消", Paramters.ServerHostAddress, false);
				// 处理确定按钮事件
				dialog.setConfrimOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						String input = dialog.getEdText().toString();
						if (!input.equals("")) {
							// 更新Shared的服务器地址
							SharedPreferencesUtil.shared.writeValue(
									"ServerHostAddress", input);
							// 更新（参数）服务器地址
							Paramters.ServerHostAddress = input;
							// 更新HttpEngine
							HttpEngine.serverUrl = input;
							// 提示用户
							ToastUtil.makeText(LoginActivity.this,
									"服务器地址已经更新!", Toast.LENGTH_LONG, true);
							// 关闭对话框
							dialog.dismiss();
						} else {
							ToastUtil.makeText(LoginActivity.this, "不可以为空哦~",
									Toast.LENGTH_SHORT);
						}
					}
				});
				// 处理取消按钮事件
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
				// 设置Token
				SharedPreferencesUtil.shared.setLoginUser(uid, token, name,
						className, isAdmin);
				HttpEngine.AccessToken = token;
				// 停止展示加载对话框
				dialogProgress.dismiss();
				// 关闭登入界面
				finish();
				// 跳转到主页面
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
