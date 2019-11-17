package com.jnxxgc.answerclient;

import com.jnxxgc.answerclient.model.UserModel;
import com.jnxxgc.answerclient.util.Paramters;
import com.jnxxgc.answerclient.util.SharedPreferencesUtil;
import com.jnxxgc.answerclient.view.MyDialog;
import com.jnxxgc.answerclient.view.ToastUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MineFragment extends Fragment implements OnClickListener {

	// View
	private View view;
	private EditText et = null;
	private MyDialog dialog;
	private TextView txtServerUrl, txtUserName, txtUid, txtClassName;

	// 点击事件
	OnClickListener cancelClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			dialog.dismiss();
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_me_fragment, null);
		this.view = view;
		// 初始化View
		initView();
		// 初始化事件
		initEvent();
		// 读取参数并显示
		readParamter();
		// 展示用户信息
		displayUserInfo();
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		// 展示用户信息
		Log.d("Juston", "onResume");
		displayUserInfo();
	}

	// 读取参数
	private void readParamter() {
		txtServerUrl.setText(String.valueOf(Paramters.ServerHostAddress));
	}

	private void displayUserInfo() {
		// 查询当前Uid然后查询出当前用户信息
		UserModel model = MainFrameActivity.loginUserModel;
		// 把用户信息展示出来
		txtUserName.setText(model.getName());
		txtUid.setText(model.getUid());
		txtClassName.setText(model.getClassName());
	}

	// 初始化View
	private void initView() {
		txtServerUrl = (TextView) view.findViewById(R.id.txt_Me_ServerUrl);
		txtUserName = (TextView) view.findViewById(R.id.txt_Me_User_Name);
		txtUid = (TextView) view.findViewById(R.id.txt_Me_Uid);
		txtClassName = (TextView) view.findViewById(R.id.txt_Me_ClassName);
	}

	// 初始化事件
	private void initEvent() {
		view.findViewById(R.id.ll_Me_Aboutme).setOnClickListener(this);
		view.findViewById(R.id.ll_ServerUrl).setOnClickListener(this);
		view.findViewById(R.id.ll_Me_About).setOnClickListener(this);
		view.findViewById(R.id.ll_Exit).setOnClickListener(this);
		view.findViewById(R.id.ll_Modify_Pwd).setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		et = new EditText(getActivity());
		et.setInputType(InputType.TYPE_CLASS_NUMBER);
		et.setTextColor(0xFF000000);

		switch (arg0.getId()) {
		case R.id.ll_Modify_Pwd:
			Intent inetnt = new Intent(getActivity(), ModifyPwdActivity.class);
			startActivity(inetnt);
			break;
		// 设置服务器地址
		case R.id.ll_ServerUrl:
			dialog = new MyDialog(getActivity(), "服务器地址", "保存", "取消",
					String.valueOf(Paramters.ServerHostAddress), true);
			dialog.setCancelOnClickListener(cancelClick);
			dialog.setConfrimOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					String str = dialog.getEdText();
					if (str.equals(""))
						ToastUtil.makeText(getActivity(), "不可以为空",
								Toast.LENGTH_SHORT);
					else {
						SharedPreferencesUtil.shared.writeValue(
								"ServerHostAddress", str);
						ToastUtil.makeText(getActivity(), "参数设置成功，重启生效!",
								Toast.LENGTH_SHORT, true);
						dialog.dismiss();
					}
				}
			});
			dialog.show();
			break;
		// 退出
		case R.id.ll_Exit:
			dialog = new MyDialog(getActivity(), "退出", "您要登出此账号吗?", "登出账号",
					"取消", true, true);
			dialog.setConfrimOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					dialog.dismiss();
					getActivity().finish();
					SharedPreferencesUtil.shared.removeUser();
					startActivity(new Intent(getActivity(), LoginActivity.class));
				}
			});
			dialog.setCancelOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					dialog.dismiss();
				}
			});
			dialog.show();
			break;
		// 关于开发者
		case R.id.ll_Me_About:
			final MyDialog dialog = new MyDialog(getActivity(), "关于我",
					"济南信息工程学校百科达人挑战赛\n\nBy 物联网实训小组-Juston", "确认", "取消",
					true, false);
			dialog.setConfrimOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					dialog.dismiss();
				}
			});
			dialog.show();
			break;
		default:
			break;
		}
	}
}
