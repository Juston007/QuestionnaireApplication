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

//此类封装各个方法的网络请求 反序列化操作
@SuppressLint("HandlerLeak")
public abstract class NetworkRequestUtil implements onDataResult {

	// 反序列化为响应实体
	public static ResponseModel deSerializeResponse(String str)
			throws Exception {
		if (str.equals(""))
			return null;
		// 获得JsonObj对象
		JSONObject jsonobj = new JSONObject(str);
		// 获取请求状态码
		int status = jsonobj.getInt("Status");
		// 获取状态码
		int statuscode = jsonobj.getInt("StatusCode");
		// 获取信息
		String msg = jsonobj.getString("Messgae");
		// 获取数据
		String data = jsonobj.getString("Data");
		// 获取错误信息
		String errormsg = jsonobj.getString("ErrorMsg");
		// 获取响应实体
		ResponseModel response = new ResponseModel(status, statuscode, msg,
				new AppendData(data), errormsg);
		return response;
	}

	@SuppressLint("HandlerLeak")
	public void login(String uid, String password) {
		// POST参数列表
		ArrayList<BasicNameValuePair> paramter = new ArrayList<BasicNameValuePair>();
		// UID
		paramter.add(new BasicNameValuePair("uid", uid));
		// PWD
		paramter.add(new BasicNameValuePair("pwd", password));
		// 发起POST请求
		new NetworkRequest("http://" + HttpEngine.serverUrl + "/"
				+ MethodUrl.login, paramter, new Handler() {
			public void handleMessage(Message msg) {
				handlerResult(msg);
			}
		});
	}

	@SuppressLint("HandlerLeak")
	public void getClassCompletion(String token, String className) {
		// POST参数列表
		ArrayList<BasicNameValuePair> paramter = new ArrayList<BasicNameValuePair>();
		paramter.add(new BasicNameValuePair("token", token));
		paramter.add(new BasicNameValuePair("className", className));
		// 发起POST请求
		new NetworkRequest("http://" + HttpEngine.serverUrl + "/"
				+ MethodUrl.getClassCompletion, paramter, new Handler() {
			public void handleMessage(Message msg) {
				handlerResult(msg);
			}
		});

	}

	@SuppressLint("HandlerLeak")
	public void getClassGradeRank(String token, String className) {
		// POST参数列表
		ArrayList<BasicNameValuePair> paramter = new ArrayList<BasicNameValuePair>();
		paramter.add(new BasicNameValuePair("token", token));
		paramter.add(new BasicNameValuePair("className", className));
		// 发起POST请求
		new NetworkRequest("http://" + HttpEngine.serverUrl + "/"
				+ MethodUrl.getClassGradeRank, paramter, new Handler() {
			public void handleMessage(Message msg) {
				handlerResult(msg);
			}
		});

	}

	@SuppressLint("HandlerLeak")
	public void getClassInfo(String token) {
		// POST参数列表
		ArrayList<BasicNameValuePair> paramter = new ArrayList<BasicNameValuePair>();
		paramter.add(new BasicNameValuePair("token", token));
		// 发起POST请求
		new NetworkRequest("http://" + HttpEngine.serverUrl + "/"
				+ MethodUrl.getClassInfo, paramter, new Handler() {
			public void handleMessage(Message msg) {
				handlerResult(msg);
			}
		});

	}

	@SuppressLint("HandlerLeak")
	public void getUserGradeRecord(String token, String queryUid) {
		// POST参数列表
		ArrayList<BasicNameValuePair> paramter = new ArrayList<BasicNameValuePair>();
		paramter.add(new BasicNameValuePair("token", token));
		paramter.add(new BasicNameValuePair("queryUid", queryUid));
		// 发起POST请求
		new NetworkRequest("http://" + HttpEngine.serverUrl + "/"
				+ MethodUrl.getUserGradeRecord, paramter, new Handler() {
			public void handleMessage(Message msg) {
				handlerResult(msg);
			}
		});
	}

	@SuppressLint("HandlerLeak")
	public void getUserInfo(String token, String queryUid) {
		// POST参数列表
		ArrayList<BasicNameValuePair> paramter = new ArrayList<BasicNameValuePair>();
		paramter.add(new BasicNameValuePair("token", token));
		paramter.add(new BasicNameValuePair("queryUid", queryUid));
		// 发起POST请求
		new NetworkRequest("http://" + HttpEngine.serverUrl + "/"
				+ MethodUrl.getUserInfo, paramter, new Handler() {
			public void handleMessage(Message msg) {
				handlerResult(msg);
			}
		});
	}

	@SuppressLint("HandlerLeak")
	public void modifyPwd(String uid, String oldPwd, String newPwd) {
		// POST参数列表
		ArrayList<BasicNameValuePair> paramter = new ArrayList<BasicNameValuePair>();
		paramter.add(new BasicNameValuePair("uid", uid));
		paramter.add(new BasicNameValuePair("oldPwd", oldPwd));
		paramter.add(new BasicNameValuePair("newPwd", newPwd));
		// 发起POST请求
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
		// POST参数列表
		ArrayList<BasicNameValuePair> paramter = new ArrayList<BasicNameValuePair>();
		paramter.add(new BasicNameValuePair("uid", uid));
		paramter.add(new BasicNameValuePair("pwd", pwd));
		paramter.add(new BasicNameValuePair("name", name));
		paramter.add(new BasicNameValuePair("classId", String.valueOf(classId)));
		paramter.add(new BasicNameValuePair("isAdmin", String.valueOf(isAdmin)));
		// 发起POST请求
		new NetworkRequest("http://" + HttpEngine.serverUrl + "/"
				+ MethodUrl.regUser, paramter, new Handler() {
			public void handleMessage(Message msg) {
				handlerResult(msg);
			}
		});
	}

	public void startAnswer(String token) {
		// POST参数列表
		ArrayList<BasicNameValuePair> paramter = new ArrayList<BasicNameValuePair>();
		paramter.add(new BasicNameValuePair("token", token));
		// 发起POST请求
		new NetworkRequest("http://" + HttpEngine.serverUrl + "/"
				+ MethodUrl.startAnswer, paramter, new Handler() {
			public void handleMessage(Message msg) {
				handlerResult(msg);
			}
		});

	}

	public void submitAnswer(String token, int id, int grade) {
		// POST参数列表
		ArrayList<BasicNameValuePair> paramter = new ArrayList<BasicNameValuePair>();
		paramter.add(new BasicNameValuePair("token", token));
		paramter.add(new BasicNameValuePair("id", String.valueOf(id)));
		paramter.add(new BasicNameValuePair("grade", String.valueOf(grade)));
		// 发起POST请求
		new NetworkRequest("http://" + HttpEngine.serverUrl + "/"
				+ MethodUrl.submitAnswer, paramter, new Handler() {
			public void handleMessage(Message msg) {
				handlerResult(msg);
			}
		});

	}

	public void updateUserInfo(String token, String updateUid, String name,
			int classID) {
		// POST参数列表
		ArrayList<BasicNameValuePair> paramter = new ArrayList<BasicNameValuePair>();
		paramter.add(new BasicNameValuePair("token", token));
		paramter.add(new BasicNameValuePair("updateUid", updateUid));
		paramter.add(new BasicNameValuePair("name", name));
		paramter.add(new BasicNameValuePair("classID", String.valueOf(classID)));
		// 发起POST请求
		new NetworkRequest("http://" + HttpEngine.serverUrl + "/"
				+ MethodUrl.updateUserInfo, paramter, new Handler() {
			public void handleMessage(Message msg) {
				handlerResult(msg);
			}
		});

	}

	// 检查Msg
	private boolean checkMsg(Message msg) {
		if (msg.what == NetworkRequest.REQUEST_SUCCESS) {
			return true;
		} else {
			return false;
		}
	}

	// 检查响应实体
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

		// 检查Msg
		if (checkMsg(msg)) {
			ResponseModel model = null;
			try {
				model = NetworkRequestUtil.deSerializeResponse(msg.obj
						.toString());
			} catch (Exception e) {
				onFailed("发生异常", e.getMessage());
				return;
			}
			String errormsg = "";
			// 检查响应实体
			if (checkResponse(model, errormsg)) {
				// 返回正确
				onSuccess(model.getMessgae(), model.getAppendData());
			} else {
				// 返回失败
				onFailed(model.getMessgae(), model.getErrorMsg());
			}
		} else {
			// 返回失败
			onFailed("请求失败", msg.obj.toString());
		}

	}
}
