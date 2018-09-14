package com.cg.datapump;

public class Table {
	 private String sourceTable;
	  private String destTable;
	  private String forceMatch;
	  private String sourceSchema;
	  private String destSchema;
	  private Column[] columns;
	  private String clause;
	  
	  public String getSourceTable()
	  {
	    return this.sourceTable;
	  }
	  
	  public void setSourceTable(String sourceTable)
	  {
	    this.sourceTable = sourceTable;
	  }
	  
	  public String getDestTable()
	  {
	    return this.destTable;
	  }
	  
	  public void setDestTable(String destTable)
	  {
	    this.destTable = destTable;
	  }
	  
	  public Column[] getColumns()
	  {
	    return this.columns;
	  }
	  
	  public void setColumns(Column[] column)
	  {
	    this.columns = column;
	  }
	  
	  public String getForceMatch()
	  {
	    return this.forceMatch;
	  }
	  
	  public void setForceMatch(String forceMatch)
	  {
	    this.forceMatch = forceMatch;
	  }
	  
	  public String getSourceSchema()
	  {
	    return this.sourceSchema;
	  }
	  
	  public void setSourceSchema(String srcSchema)
	  {
	    this.sourceSchema = srcSchema;
	  }
	  
	  public String getDestSchema()
	  {
	    return this.destSchema;
	  }
	  
	  public void setDestSchema(String destSchema)
	  {
	    this.destSchema = destSchema;
	  }
	  
	  public String getClause()
	  {
	    return this.clause;
	  }
	  
	  public void setClause(String clause)
	  {
	    this.clause = clause;
	  }
}
