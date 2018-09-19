package com.capgemini.model;


public class Destination {

	private String DdriverName;

	private String DconnectionString;

	private String DuserName;

	private String Dpassword;
	public String getDdriverName() {
		return DdriverName;
	}
	public void setDdriverName(String ddriverName) {
		DdriverName = ddriverName;
	}
	public String getDconnectionString() {
		return DconnectionString;
	}
	public void setDconnectionString(String dconnectionString) {
		DconnectionString = dconnectionString;
	}
	public String getDuserName() {
		return DuserName;
	}
	public void setDuserName(String duserName) {
		DuserName = duserName;
	}
	public String getDpassword() {
		return Dpassword;
	}
	public void setDpassword(String dpassword) {
		Dpassword = dpassword;
	}
	@Override
	public String toString() {
		return "Destination [DdriverName=" + DdriverName
				+ ", DconnectionString=" + DconnectionString + ", DuserName="
				+ DuserName + ", Dpassword=" + Dpassword + "]";
	}
	
}
