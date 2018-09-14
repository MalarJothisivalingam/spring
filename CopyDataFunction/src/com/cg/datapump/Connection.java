package com.cg.datapump;

public class Connection {
	private String driverName;
	  private String connectionURL;
	  private String userId;
	  private String password;
	  
	  public String getConnectionURL()
	  {
	    return this.connectionURL;
	  }
	  
	  public void setConnectionURL(String connectionURL)
	  {
	    this.connectionURL = connectionURL;
	  }
	  
	  public String getUserId()
	  {
	    return this.userId;
	  }
	  
	  public void setUserId(String userId)
	  {
	    this.userId = userId;
	  }
	  
	  public String getPassword()
	  {
	    return this.password;
	  }
	  
	  public void setPassword(String password)
	  {
	    this.password = password;
	  }
	  
	  public String getDriverName()
	  {
	    return this.driverName;
	  }
	  
	  public void setDriverName(String driverName)
	  {
	    this.driverName = driverName;
	  }
}
