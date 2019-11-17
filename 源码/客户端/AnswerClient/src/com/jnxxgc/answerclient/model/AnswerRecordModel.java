package com.jnxxgc.answerclient.model;

import java.text.ParseException;
import java.util.Date;

import com.jnxxgc.answerclient.util.Util;

public class AnswerRecordModel {

	public AnswerRecordModel(int id, String uid, int grade, String name,
			String startTime, String submitTime) throws ParseException {
		this.id = id;
		this.grade = grade;
		this.uid = uid;
		this.name = name;
		this.startTime = Util.getDateFromUTCString(startTime);
		this.submitTime = !submitTime.equals("") ? Util
				.getDateFromUTCString(submitTime) : Util
				.getDateFromUTCString("1970-01-1T0:0:0");
	}

	private int id, grade;
	private String name, uid;
	private Date startTime, submitTime;

	public int getId() {
		return id;
	}

	public int getGrade() {
		return grade;
	}

	public String getName() {
		return name;
	}

	public String getUid() {
		return uid;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getSubmitTime() {
		return submitTime;
	}
}
