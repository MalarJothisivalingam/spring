package com.cg.datapump;

public class Column {
	private String sourceColumn;
	  private String destColumn;
	  private String value;
	  
	  public String getSourceColumn()
	  {
	    return this.sourceColumn;
	  }
	  
	  public void setSourceColumn(String sourceColumn)
	  {
	    this.sourceColumn = sourceColumn;
	  }
	  
	  public String getDestColumn()
	  {
	    return this.destColumn;
	  }
	  
	  public void setDestColumn(String destColumn)
	  {
	    this.destColumn = destColumn;
	  }
	  
	  public String getValue()
	  {
	    return this.value;
	  }
	  
	  public void setValue(String value)
	  {
	    this.value = value;
	  }

}
