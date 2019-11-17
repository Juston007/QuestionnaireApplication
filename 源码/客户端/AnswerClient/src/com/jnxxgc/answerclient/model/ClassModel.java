package com.jnxxgc.answerclient.model;

public class ClassModel {
	public ClassModel(int id, String className) {
		this.ClassId = id;
		this.className = className;
	}

	private int ClassId;
	private String className;

	public int getClassId() {
		return ClassId;
	}

	public String getClassName() {
		return className;
	}

}
