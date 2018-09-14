package com.cg.datapump;

public class MappingInfo {
	 private String srcColumn;
	  private String destColumn;
	  private Object srcValue;
	  private boolean Null;
	  private boolean isMapped;
	  private boolean isOverride;
	  private String type;
	  private boolean combineInserts;
	  
	  public String getType()
	  {
	    return this.type;
	  }
	  
	  public void setType(String type)
	  {
	    this.type = type;
	  }
	  
	  public String getSrcColumn()
	  {
	    return this.srcColumn;
	  }
	  
	  public void setSrcColumn(String srcColumn)
	  {
	    this.srcColumn = srcColumn;
	  }
	  
	  public String getDestColumn()
	  {
	    return this.destColumn;
	  }
	  
	  public void setDestColumn(String destColumn)
	  {
	    this.destColumn = destColumn;
	  }
	  
	  public boolean isNull()
	  {
	    return this.Null;
	  }
	  
	  public void setNull(boolean null1)
	  {
	    this.Null = null1;
	  }
	  
	  public boolean isMapped()
	  {
	    return this.isMapped;
	  }
	  
	  public void setMapped(boolean isMapped)
	  {
	    this.isMapped = isMapped;
	  }
	  
	  public boolean isCombineInserts()
	  {
	    return this.combineInserts;
	  }
	  
	  public void setCombineInserts(boolean combineInserts)
	  {
	    this.combineInserts = combineInserts;
	  }
	  
	  public Object getSrcValue()
	  {
	    return this.srcValue;
	  }
	  
	  public void setSrcValue(Object srcValue)
	  {
	    this.srcValue = srcValue;
	  }
	  
	  public int hashCode()
	  {
	    int prime = 31;
	    int result = 1;
	    result = 31 * result + (
	      this.destColumn == null ? 0 : this.destColumn.hashCode());
	    result = 31 * result + (
	      this.srcColumn == null ? 0 : this.srcColumn.hashCode());
	    result = 31 * result + (this.type == null ? 0 : this.type.hashCode());
	    return result;
	  }
	  
	  public boolean equals(Object obj)
	  {
	    if (this == obj) {
	      return true;
	    }
	    if (obj == null) {
	      return false;
	    }
	    if (getClass() != obj.getClass()) {
	      return false;
	    }
	    MappingInfo other = (MappingInfo)obj;
	    if (this.destColumn == null)
	    {
	      if (other.destColumn != null) {
	        return false;
	      }
	    }
	    else if (!this.destColumn.equals(other.destColumn)) {
	      return false;
	    }
	    if (this.srcColumn == null)
	    {
	      if (other.srcColumn != null) {
	        return false;
	      }
	    }
	    else if (!this.srcColumn.equals(other.srcColumn)) {
	      return false;
	    }
	    if (this.type == null)
	    {
	      if (other.type != null) {
	        return false;
	      }
	    }
	    else if (!this.type.equals(other.type)) {
	      return false;
	    }
	    return true;
	  }
	  
	  public boolean isOverride()
	  {
	    return this.isOverride;
	  }
	  
	  public void setOverride(boolean isOverride)
	  {
	    this.isOverride = isOverride;
	  }

	@Override
	public String toString() {
		return "MappingInfo [srcColumn=" + srcColumn + ", destColumn="
				+ destColumn + ", srcValue=" + srcValue + ", Null=" + Null
				+ ", isMapped=" + isMapped + ", isOverride=" + isOverride
				+ ", type=" + type + ", combineInserts=" + combineInserts + "]";
	}
	  
}
