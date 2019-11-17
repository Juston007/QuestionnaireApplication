package com.jnxxgc.answerclient.model;

public class QuestionModel {
	public QuestionModel(long id, String content, String option1,
			String option2, String option3, String option4, Short passOption,
			int score) {
		this.id = id;
		this.content = content;
		this.option1 = option1;
		this.option2 = option2;
		this.option3 = option3;
		this.option4 = option4;
		this.passOption = passOption;
		this.score = score;
	}

	// 题目ID
	private long id;
	// 题目以及四个选项内容
	private String content, option1, option2, option3, option4;
	// 正确选项
	private Short passOption;
	// 分值
	private int score;

	public long getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public String getOption1() {
		return option1;
	}

	public String getOption2() {
		return option2;
	}

	public String getOption3() {
		return option3;
	}

	public String getOption4() {
		return option4;
	}

	public Short getPassOption() {
		return passOption;
	}

	public int getScore() {
		return score;
	}
}
