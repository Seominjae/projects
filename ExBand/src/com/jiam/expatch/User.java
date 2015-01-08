package com.jiam.expatch;

import java.io.Serializable;

public class User implements Serializable{
	
	private static final long serialVersionUID = 5895L;
	
	private String name;
	private int age;
	private float height;
	private float weight;
	private String sex;
	
	public User() {
		this("guest", 20, "Male", 180f, 75f);
	}
	
	public User(String name, int age, String sex, float height, float weight) {
		this.name = name;
		this.age = age;
		this.sex = sex;
		this.height = height;
		this.weight = weight;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}	
		
}
