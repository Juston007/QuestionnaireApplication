package com.jnxxgc.answerclient.model;

import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AppendData {

	private String dataStr;

	public AppendData(String data) {
		// 去掉所有 ‘\’字符
		dataStr = data.replace('\\', ' ');
	}

	public ArrayList<QuestionModel> getQuestionInfoList() throws JSONException {
		if (dataStr.equals(""))
			return null;
		JSONArray jsonArray = new JSONArray(dataStr);
		ArrayList<QuestionModel> questionList = new ArrayList<QuestionModel>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			// ID
			int id = obj.getInt("Id");
			String content = obj.getString("Content");
			String option1 = obj.getString("Option1");
			String option2 = obj.getString("Option2");
			String option3 = obj.getString("Option3");
			String option4 = obj.getString("Option4");
			short passOption = (short) obj.getInt("PassOption");
			int score = obj.getInt("Score");
			QuestionModel model = new QuestionModel(id, content, option1,
					option2, option3, option4, passOption, score);
			questionList.add(model);
		}
		return questionList;
	}

	public ArrayList<ClassModel> getClassInfoList() throws JSONException {
		if (dataStr.equals(""))
			return null;
		JSONArray jsonArray = new JSONArray(dataStr);
		ArrayList<ClassModel> classInfoList = new ArrayList<ClassModel>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			// ID
			int id = obj.getInt("ClassID");
			// Name
			String className = obj.getString("ClassName");
			ClassModel model = new ClassModel(id, className);
			classInfoList.add(model);
		}
		return classInfoList;
	}

	public UserModel getUserInfo() throws JSONException, ParseException {
		return getUserInfo(dataStr);
	}

	private UserModel getUserInfo(String str) throws JSONException,
			ParseException {
		if (str.equals(""))
			return null;
		JSONObject obj = new JSONObject(str);
		String uid = obj.getString("Uid");
		String name = obj.getString("Name");
		String className = obj.getString("ClassName");
		String regTime = obj.getString("RegTime");
		String lastLoginTimeStr = obj.getString("LastLoginTime");
		lastLoginTimeStr = !lastLoginTimeStr.equals("") ? lastLoginTimeStr
				: "1970-01-1T0:0:0";
		boolean isComplete = obj.getBoolean("IsComplete");
		boolean locking = obj.getBoolean("Locking");
		boolean isAdmin = obj.getBoolean("IsAdmin");
		UserModel model = new UserModel(uid, name, className, regTime,
				lastLoginTimeStr, isComplete, locking, isAdmin);
		return model;
	}

	public ArrayList<UserModel> getClassCompletion() throws JSONException,
			ParseException {
		if (dataStr.equals(""))
			return null;
		ArrayList<UserModel> list = new ArrayList<UserModel>();
		JSONArray array = new JSONArray(dataStr);
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			String uid = obj.getString("Uid");
			String name = obj.getString("Name");
			String className = obj.getString("ClassName");
			String regTime = obj.getString("RegTime");
			String lastLoginTimeStr = obj.getString("LastLoginTime");
			lastLoginTimeStr = !lastLoginTimeStr.equals("") ? lastLoginTimeStr
					: "1970-01-1T0:0:0";
			boolean isComplete = obj.getBoolean("IsComplete");
			boolean locking = obj.getBoolean("Locking");
			boolean isAdmin = obj.getBoolean("IsAdmin");
			UserModel model = new UserModel(uid, name, className, regTime,
					lastLoginTimeStr, isComplete, locking, isAdmin);
			list.add(model);
		}
		return list;
	}

	public ArrayList<AnswerRecordModel> getAnswerReocrd() throws JSONException,
			ParseException {
		if (dataStr.equals(""))
			return null;
		JSONArray jsonArray = new JSONArray(dataStr);
		ArrayList<AnswerRecordModel> answerRecordList = new ArrayList<AnswerRecordModel>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			String name = obj.getString("Name");
			int grade = obj.getInt("Grade");
			int id = obj.getInt("Id");
			String startTime = obj.getString("StartTime");
			String submitTimeStr = obj.getString("SubmitTime");
			submitTimeStr = !submitTimeStr.equals("") ? submitTimeStr
					: "1970-01-1T0:0:0";
			String uid = obj.getString("Uid");
			AnswerRecordModel recordModel = new AnswerRecordModel(id, uid,
					grade, name, startTime, submitTimeStr);

			answerRecordList.add(recordModel);
		}
		return answerRecordList;
	}

	// 获取Token
	public String getToken() {
		return getString();
	}

	// 获取字符串
	public String getString() {
		return dataStr;
	}

	// 重写toString方法
	@Override
	public String toString() {
		return getString();
	}
}
