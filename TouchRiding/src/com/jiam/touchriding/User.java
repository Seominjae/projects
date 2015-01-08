package com.jiam.touchriding;

import java.io.Serializable;

/*
 * 2014-06-06
 * User information class
 */
public class User implements Serializable{
	
	private static final long serialVersionUID = 5895L;
	public final static int NUMBER_MAX_NFC = 6;
	
	private String email;
	private String nickname;
	private int id;
	private boolean isVisited[];
	
	public User() {
		
	}	
	
	public User(String email) {
		this(email, "guest", 0, new boolean[NUMBER_MAX_NFC]);
	}

	public User(String email, String nickname, int id) {
		this(email, nickname, id, new boolean[NUMBER_MAX_NFC]);
	}

	public User(String email, String nickname, int id, boolean[] isVisited) {
		super();
		this.email = email;
		this.nickname = nickname;
		this.id = id;
		this.isVisited = isVisited;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean[] getIsVisited() {
		return isVisited;
	}

	public void setIsVisited(boolean[] isVisited) {
		this.isVisited = isVisited;
	}
	
	public void setIsVisited(int i) {
		if(i>=0) {
			isVisited[i] = true;
		}
	}
	
	
}
