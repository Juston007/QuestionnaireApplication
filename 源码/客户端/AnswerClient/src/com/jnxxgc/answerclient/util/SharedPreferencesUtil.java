package com.jnxxgc.answerclient.util;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

//SharedPreferences工具类
@SuppressLint("DefaultLocale")
public class SharedPreferencesUtil {

	private SharedPreferences mSharedPreferences = null;

	// 工具类对象 静态方便调用
	public static SharedPreferencesUtil shared = null;

	// 设置对象
	public void setSharedPreferces(SharedPreferences arg) {
		mSharedPreferences = arg;
	}

	// 写入值
	public void writeValue(String key, String value) {
		if (mSharedPreferences == null)
			return;
		Editor editor = mSharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	// 读取值
	public String readValue(String key, String defaultvalue) {
		if (key.equals("") || mSharedPreferences == null)
			return null;
		return mSharedPreferences.getString(key, defaultvalue);
	}

	// 删除值
	public void removeValue(String key) {
		if (mSharedPreferences == null)
			return;
		Editor editor = mSharedPreferences.edit();
		editor.remove(key);
		editor.commit();
	}

	// 清除全部值
	public void clearValue() {
		if (mSharedPreferences == null)
			return;
		Editor editor = mSharedPreferences.edit();
		editor.clear();
		editor.commit();
	}

	// 是否首次进入
	public boolean isLogin() {
		if (mSharedPreferences == null)
			return false;
		return mSharedPreferences.contains("isLogin");
	}

	// 设置登入的用户
	public void setLoginUser(String user, String token, String name,
			String className, boolean isAdmin) {
		writeValue(user, token);
		writeValue("isLogin", user);
		writeValue("ClassName", className);
		writeValue("Name", name);
		writeValue("isAdmin", isAdmin ? "1" : "0");
	}

	// 获取Token
	public String getToken() {
		String uid = readValue("isLogin", "uid");
		if (!uid.equals("uid"))
			return readValue(uid, "token");
		return null;
	}

	public String getClassName() {
		String className = readValue("ClassName", "N/A");
		if (!className.equals("N/A"))
			return className;
		return null;
	}

	public String getName() {
		String name = readValue("Name", "N/A");
		if (!name.equals("N/A"))
			return name;
		return null;
	}

	public boolean isAdmin() {
		String isAdmin = readValue("isAdmin", "N/A");
		if (!isAdmin.equals("N/A"))
			return isAdmin.equals("1");
		return false;
	}

	// 获取Uid
	public String getUid() {
		String uid = readValue("isLogin", "uid");
		return uid;
	}

	// 登出账号
	public void removeUser() {
		clearValue();
	}
}
