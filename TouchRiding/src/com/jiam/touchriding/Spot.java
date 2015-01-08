package com.jiam.touchriding;

/*
 * 2014-06-06
 * Spot information class
 */
public class Spot {

	private String name;
	private int id;
	private String nfcId;
	
	public Spot() {
		
	}
	
	public Spot(int id, String name, String nfcId) {
		super();
		this.id = id;
		this.name = name;
		this.nfcId = nfcId;
	}
	
	public Spot(String name, String nfcId) {
		super();
		this.name = name;
		this.nfcId = nfcId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNfcId() {
		return nfcId;
	}

	public void setNfcId(String nfcId) {
		this.nfcId = nfcId;
	}	
}
