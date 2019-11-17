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

	// ����¼�
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
		// ��ʼ��View
		initView();
		// ��ʼ���¼�
		initEvent();
		// ��ȡ��������ʾ
		readParamter();
		// չʾ�û���Ϣ
		displayUserInfo();
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		// չʾ�û���Ϣ
		Log.d("Juston", "onResume");
		displayUserInfo();
	}

	// ��ȡ����
	private void readParamter() {
		txtServerUrl.setText(String.valueOf(Paramters.ServerHostAddress));
	}

	private void displayUserInfo() {
		// ��ѯ��ǰUidȻ���ѯ����ǰ�û���Ϣ
		UserModel model = MainFrameActivity.loginUserModel;
		// ���û���Ϣչʾ����
		txtUserName.setText(model.getName());
		txtUid.setText(model.getUid());
		txtClassName.setText(model.getClassName());
	}

	// ��ʼ��View
	private void initView() {
		txtServerUrl = (TextView) view.findViewById(R.id.txt_Me_ServerUrl);
		txtUserName = (TextView) view.findViewById(R.id.txt_Me_User_Name);
		txtUid = (TextView) view.findViewById(R.id.txt_Me_Uid);
		txtClassName = (TextView) view.findViewById(R.id.txt_Me_ClassName);
	}

	// ��ʼ���¼�
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
		// ���÷�������ַ
		case R.id.ll_ServerUrl:
			dialog = new MyDialog(getActivity(), "��������ַ", "����", "ȡ��",
					String.valueOf(Paramters.ServerHostAddress), true);
			dialog.setCancelOnClickListener(cancelClick);
			dialog.setConfrimOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					String str = dialog.getEdText();
					if (str.equals(""))
						ToastUtil.makeText(getActivity(), "������Ϊ��",
								Toast.LENGTH_SHORT);
					else {
						SharedPreferencesUtil.shared.writeValue(
								"ServerHostAddress", str);
						ToastUtil.makeText(getActivity(), "�������óɹ���������Ч!",
								Toast.LENGTH_SHORT, true);
						dialog.dismiss();
					}
				}
			});
			dialog.show();
			break;
		// �˳�
		case R.id.ll_Exit:
			dialog = new MyDialog(getActivity(), "�˳�", "��Ҫ�ǳ����˺���?", "�ǳ��˺�",
					"ȡ��", true, true);
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
		// ���ڿ�����
		case R.id.ll_Me_About:
			final MyDialog dialog = new MyDialog(getActivity(), "������",
					"������Ϣ����ѧУ�ٿƴ�����ս��\n\nBy ������ʵѵС��-Juston", "ȷ��", "ȡ��",
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
