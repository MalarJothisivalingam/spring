package com.cg.datapump;

public class DataPump {
	private Connection sourceConnection;
	  private Connection destConnection;
	  private Table table;
	  
	  public Connection getSourceConnection()
	  {
	    return this.sourceConnection;
	  }
	  
	  public void setSourceConnection(Connection sourceConnection)
	  {
	    this.sourceConnection = sourceConnection;
	  }
	  
	  public Connection getDestConnection()
	  {
	    return this.destConnection;
	  }
	  
	  public void setDestConnection(Connection destConnection)
	  {
	    this.destConnection = destConnection;
	  }
	  
	  public Table getTable()
	  {
	    return this.table;
	  }
	  
	  public void setTable(Table table)
	  {
	    this.table = table;
	  }
}
