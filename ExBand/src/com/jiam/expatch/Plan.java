package com.jiam.expatch;

import java.io.Serializable;

public class Plan implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	private int times;
	private String user;
	
	public Plan() {
		this(0, "Guest", "Default", 10);
	}
	
	public Plan(int id, String user, String name, int times) {
		this.id = id;
		this.name = name;
		this.times = times;
		this.user = user;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	

}
