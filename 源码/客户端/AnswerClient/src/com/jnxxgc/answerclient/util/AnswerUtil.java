package com.jnxxgc.answerclient.util;

import java.util.ArrayList;

import com.jnxxgc.answerclient.model.QuestionModel;

public class AnswerUtil {

	public static int AnswerID = 0;
	public static ArrayList<QuestionModel> questionList = new ArrayList<QuestionModel>();
	public static ArrayList<Integer> resultList = new ArrayList<Integer>();
	public static long startTime, submitTime;

	public static int calculateTotalGrade() {
		int grade = 0;
		for (int i = 0; i < questionList.size(); i++) {
			QuestionModel question = questionList.get(i);
			if (question.getPassOption() == resultList.get(i).shortValue()) {
				grade += question.getScore();
			}
		}
		return grade;
	}

	public static ArrayList<Boolean> getAnswerInfo() {
		if (questionList == null || resultList == null) {
			return null;
		}
		ArrayList<Boolean> list = new ArrayList<Boolean>();
		for (int i = 0; i < questionList.size(); i++) {
			QuestionModel question = questionList.get(i);
			boolean result = question.getPassOption() == resultList.get(i)
					.shortValue();
			list.add(result);
		}
		return list;
	}
}
