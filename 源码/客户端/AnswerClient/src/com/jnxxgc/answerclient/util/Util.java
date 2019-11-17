package com.jnxxgc.answerclient.util;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.view.Display;
import android.view.WindowManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

//工具类 包含常用的转换方法
public class Util {

	private static final String appID = "1105965161";

	// UTC字符串转换为准确时间
	@SuppressLint("SimpleDateFormat")
	public static Date getDateFromUTCString(String datestr)
			throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		// 注意格式化的表达式
		Date date = format.parse(datestr);
		return date;
	}

	public static String secToTime(int time) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0)
			return "00:00";
		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = unitFormat(minute) + ":" + unitFormat(second);
			} else {
				hour = minute / 60;
				if (hour > 99)
					return "99:59:59";
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":"
						+ unitFormat(second);
			}
		}
		return timeStr;
	}

	public static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}

	// Long转换为准确时间
	@SuppressLint("SimpleDateFormat")
	public static String ToTimeStringFromLong(long time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd HH:mm");
		Date date = new Date(time);
		return df.format(date);
	}

	// Long转换为日期
	@SuppressLint("SimpleDateFormat")
	public static String ToDateStringFromLong(long time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(time);
		return df.format(date);
	}

	// 日期字符串转换为日期
	@SuppressLint("SimpleDateFormat")
	public static Date getDateFromDateString(String datestr)
			throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// 注意格式化的表达式
		Date date = format.parse(datestr);
		return date;
	}

	// base64字符串转byte[]
	public static byte[] ToByteFromBase64String(String base64Str) {
		return Base64.decode(base64Str, Base64.DEFAULT);
	}

	// byte[]转base64字符串
	public static String ToBaseStringFromByte(byte[] b) {
		return Base64.encodeToString(b, Base64.DEFAULT);
	}

	// 计算两个时间点相差的天数
	public static int differentDaysByMillisecond(Date date1, Date date2) {
		int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
		days = Math.abs(days);
		return days;
	}

	public static void setActionBarTilte(ActionBar bar, String str) {
		bar.setTitle(Html.fromHtml("<font color='#FFFFFF'>" + str + "</font>"));
	}

	public static Point getDisplaySize(Context context) {
		if (context == null)
			return null;
		WindowManager windowManage = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManage.getDefaultDisplay();
		Point point = new Point();
		display.getSize(point);
		return point;
	}

	public static Point getDialogSize(Context context) {
		Point point = getDisplaySize(context);
		if (point == null)
			return null;
		point.x = (point.x /= 10) * 8;
		point.y = (int) ((point.y /= 100) * 27);
		return point;
	}

	public static void share(final Activity aty, Context context, int grade,
			IUiListener listener) {
		Tencent tencent = Tencent.createInstance(appID, context);
		Bundle params = new Bundle();
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
				QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		params.putString(QQShare.SHARE_TO_QQ_TITLE, "济信百科达人挑战赛");// 标题
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "我在济信百科达人挑战赛中取得" + grade
				+ "分的成绩！");// 摘要
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,
				"https://weibo.com/jnxxgc");// 内容地址
		params.putString(
				QQShare.SHARE_TO_QQ_IMAGE_URL,
				"https://tva3.sinaimg.cn/crop.0.0.180.180.180/bef8c320jw1e8qgp5bmzyj2050050aa8.jpg");// 网络图片地址
		params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "百科达人");
		tencent.shareToQQ(aty, params, null);
	}
}
