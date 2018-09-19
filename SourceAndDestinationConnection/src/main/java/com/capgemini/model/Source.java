package com.capgemini.model;


public class Source {

	private String driverName;

	private String connectionString;

	private String userName;

	private String password;
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getConnectionString() {
		return connectionString;
	}
	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "Source [driverName=" + driverName + ", connectionString="
				+ connectionString + ", userName=" + userName + ", password="
				+ password + "]";
	}
	

	}


