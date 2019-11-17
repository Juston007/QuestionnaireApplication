package com.jnxxgc.answerclient.net;

import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.jnxxgc.answerclient.model.AppendData;
import com.jnxxgc.answerclient.model.ResponseModel;
import com.jnxxgc.answerclient.model.onDataResult;

//�����װ������������������ �����л�����
@SuppressLint("HandlerLeak")
public abstract class NetworkRequestUtil implements onDataResult {

	// �����л�Ϊ��Ӧʵ��
	public static ResponseModel deSerializeResponse(String str)
			throws Exception {
		if (str.equals(""))
			return null;
		// ���JsonObj����
		JSONObject jsonobj = new JSONObject(str);
		// ��ȡ����״̬��
		int status = jsonobj.getInt("Status");
		// ��ȡ״̬��
		int statuscode = jsonobj.getInt("StatusCode");
		// ��ȡ��Ϣ
		String msg = jsonobj.getString("Messgae");
		// ��ȡ����
		String data = jsonobj.getString("Data");
		// ��ȡ������Ϣ
		String errormsg = jsonobj.getString("ErrorMsg");
		// ��ȡ��Ӧʵ��
		ResponseModel response = new ResponseModel(status, statuscode, msg,
				new AppendData(data), errormsg);
		return response;
	}

	@SuppressLint("HandlerLeak")
	public void login(String uid, String password) {
		// POST�����б�
		ArrayList<BasicNameValuePair> paramter = new ArrayList<BasicNameValuePair>();
		// UID
		paramter.add(new BasicNameValuePair("uid", uid));
		// PWD
		paramter.add(new BasicNameValuePair("pwd", password));
		// ����POST����
		new NetworkRequest("http://" + HttpEngine.serverUrl + "/"
				+ MethodUrl.login, paramter, new Handler() {
			public void handleMessage(Message msg) {
				handlerResult(msg);
			}
		});
	}

	@SuppressLint("HandlerLeak")
	public void getClassCompletion(String token, String className) {
		// POST�����б�
		ArrayList<BasicNameValuePair> paramter = new ArrayList<BasicNameValuePair>();
		paramter.add(new BasicNameValuePair("token", token));
		paramter.add(new BasicNameValuePair("className", className));
		// ����POST����
		new NetworkRequest("http://" + HttpEngine.serverUrl + "/"
				+ MethodUrl.getClassCompletion, paramter, new Handler() {
			public void handleMessage(Message msg) {
				handlerResult(msg);
			}
		});

	}

	@SuppressLint("HandlerLeak")
	public void getClassGradeRank(String token, String className) {
		// POST�����б�
		ArrayList<BasicNameValuePair> paramter = new ArrayList<BasicNameValuePair>();
		paramter.add(new BasicNameValuePair("token", token));
		paramter.add(new BasicNameValuePair("className", className));
		// ����POST����
		new NetworkRequest("http://" + HttpEngine.serverUrl + "/"
				+ MethodUrl.getClassGradeRank, paramter, new Handler() {
			public void handleMessage(Message msg) {
				handlerResult(msg);
			}
		});

	}

	@SuppressLint("HandlerLeak")
	public void getClassInfo(String token) {
		// POST�����б�
		ArrayList<BasicNameValuePair> paramter = new ArrayList<BasicNameValuePair>();
		paramter.add(new BasicNameValuePair("token", token));
		// ����POST����
		new NetworkRequest("http://" + HttpEngine.serverUrl + "/"
				+ MethodUrl.getClassInfo, paramter, new Handler() {
			public void handleMessage(Message msg) {
				handlerResult(msg);
			}
		});

	}

	@SuppressLint("HandlerLeak")
	public void getUserGradeRecord(String token, String queryUid) {
		// POST�����б�
		ArrayList<BasicNameValuePair> paramter = new ArrayList<BasicNameValuePair>();
		paramter.add(new BasicNameValuePair("token", token));
		paramter.add(new BasicNameValuePair("queryUid", queryUid));
		// ����POST����
		new NetworkRequest("http://" + HttpEngine.serverUrl + "/"
				+ MethodUrl.getUserGradeRecord, paramter, new Handler() {
			public void handleMessage(Message msg) {
				handlerResult(msg);
			}
		});
	}

	@SuppressLint("HandlerLeak")
	public void getUserInfo(String token, String queryUid) {
		// POST�����б�
		ArrayList<BasicNameValuePair> paramter = new ArrayList<BasicNameValuePair>();
		paramter.add(new BasicNameValuePair("token", token));
		paramter.add(new BasicNameValuePair("queryUid", queryUid));
		// ����POST����
		new NetworkRequest("http://" + HttpEngine.serverUrl + "/"
				+ MethodUrl.getUserInfo, paramter, new Handler() {
			public void handleMessage(Message msg) {
				handlerResult(msg);
			}
		});
	}

	@SuppressLint("HandlerLeak")
	public void modifyPwd(String uid, String oldPwd, String newPwd) {
		// POST�����б�
		ArrayList<BasicNameValuePair> paramter = new ArrayList<BasicNameValuePair>();
		paramter.add(new BasicNameValuePair("uid", uid));
		paramter.add(new BasicNameValuePair("oldPwd", oldPwd));
		paramter.add(new BasicNameValuePair("newPwd", newPwd));
		// ����POST����
		new NetworkRequest("http://" + HttpEngine.serverUrl + "/"
				+ MethodUrl.modifyPwd, paramter, new Handler() {
			public void handleMessage(Message msg) {
				handlerResult(msg);
			}
		});
	}

	@SuppressLint("HandlerLeak")
	public void regUser(String uid, String pwd, String name, int classId,
			boolean isAdmin) {
		// POST�����б�
		ArrayList<BasicNameValuePair> paramter = new ArrayList<BasicNameValuePair>();
		paramter.add(new BasicNameValuePair("uid", uid));
		paramter.add(new BasicNameValuePair("pwd", pwd));
		paramter.add(new BasicNameValuePair("name", name));
		paramter.add(new BasicNameValuePair("classId", String.valueOf(classId)));
		paramter.add(new BasicNameValuePair("isAdmin", String.valueOf(isAdmin)));
		// ����POST����
		new NetworkRequest("http://" + HttpEngine.serverUrl + "/"
				+ MethodUrl.regUser, paramter, new Handler() {
			public void handleMessage(Message msg) {
				handlerResult(msg);
			}
		});
	}

	public void startAnswer(String token) {
		// POST�����б�
		ArrayList<BasicNameValuePair> paramter = new ArrayList<BasicNameValuePair>();
		paramter.add(new BasicNameValuePair("token", token));
		// ����POST����
		new NetworkRequest("http://" + HttpEngine.serverUrl + "/"
				+ MethodUrl.startAnswer, paramter, new Handler() {
			public void handleMessage(Message msg) {
				handlerResult(msg);
			}
		});

	}

	public void submitAnswer(String token, int id, int grade) {
		// POST�����б�
		ArrayList<BasicNameValuePair> paramter = new ArrayList<BasicNameValuePair>();
		paramter.add(new BasicNameValuePair("token", token));
		paramter.add(new BasicNameValuePair("id", String.valueOf(id)));
		paramter.add(new BasicNameValuePair("grade", String.valueOf(grade)));
		// ����POST����
		new NetworkRequest("http://" + HttpEngine.serverUrl + "/"
				+ MethodUrl.submitAnswer, paramter, new Handler() {
			public void handleMessage(Message msg) {
				handlerResult(msg);
			}
		});

	}

	public void updateUserInfo(String token, String updateUid, String name,
			int classID) {
		// POST�����б�
		ArrayList<BasicNameValuePair> paramter = new ArrayList<BasicNameValuePair>();
		paramter.add(new BasicNameValuePair("token", token));
		paramter.add(new BasicNameValuePair("updateUid", updateUid));
		paramter.add(new BasicNameValuePair("name", name));
		paramter.add(new BasicNameValuePair("classID", String.valueOf(classID)));
		// ����POST����
		new NetworkRequest("http://" + HttpEngine.serverUrl + "/"
				+ MethodUrl.updateUserInfo, paramter, new Handler() {
			public void handleMessage(Message msg) {
				handlerResult(msg);
			}
		});

	}

	// ���Msg
	private boolean checkMsg(Message msg) {
		if (msg.what == NetworkRequest.REQUEST_SUCCESS) {
			return true;
		} else {
			return false;
		}
	}

	// �����Ӧʵ��
	private boolean checkResponse(ResponseModel model, String errormsg) {
		boolean result = false;
		if (model.getStatus() == ResponseModel.STATUS_SUCCESS
				&& model.getStatusCode() == ResponseModel.STATUS_SUCCESS) {
			result = true;
		} else {
			errormsg = model.getStatus() != ResponseModel.STATUS_SUCCESS ? model
					.getErrorMsg() : model.getMessgae();
		}
		return result;
	}

	private void handlerResult(Message msg) {

		// ���Msg
		if (checkMsg(msg)) {
			ResponseModel model = null;
			try {
				model = NetworkRequestUtil.deSerializeResponse(msg.obj
						.toString());
			} catch (Exception e) {
				onFailed("�����쳣", e.getMessage());
				return;
			}
			String errormsg = "";
			// �����Ӧʵ��
			if (checkResponse(model, errormsg)) {
				// ������ȷ
				onSuccess(model.getMessgae(), model.getAppendData());
			} else {
				// ����ʧ��
				onFailed(model.getMessgae(), model.getErrorMsg());
			}
		} else {
			// ����ʧ��
			onFailed("����ʧ��", msg.obj.toString());
		}

	}
}
