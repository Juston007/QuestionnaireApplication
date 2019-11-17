package com.jnxxgc.answerclient.model;

import java.text.ParseException;
import java.util.Date;

import com.jnxxgc.answerclient.util.Util;

public class UserModel {

	public UserModel(String uid, String name, String className, String regTime,
			String lastLoginTime, boolean isComplete, boolean locking,
			boolean isAdmin) throws ParseException {
		this.uid = uid;
		this.name = name;
		this.className = className;
		this.regTime = !regTime.equals("") ? Util.getDateFromUTCString(regTime)
				: Util.getDateFromUTCString("1970-01-1T0:0:0");
		this.lastLoginTime = !lastLoginTime.equals("") ? Util
				.getDateFromUTCString(lastLoginTime) : Util
				.getDateFromUTCString("1970-01-1T0:0:0");
		this.locking = locking;
		this.isAdmin = isAdmin;
		this.isComplete = isComplete;
	}

	// ×Ö¶Î
	private String uid, name, className;
	private Date regTime, lastLoginTime;
	private boolean locking = false, isAdmin = false, isComplete = false;

	public String getUid() {
		return uid;
	}

	public String getName() {
		return name;
	}

	public String getClassName() {
		return className;
	}

	public Date getRegTime() {
		return regTime;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public boolean getLocking() {
		return locking;
	}

	public boolean getIsAdmin() {
		return isAdmin;
	}

	public boolean getIsComplete() {
		return isComplete;
	}

}
