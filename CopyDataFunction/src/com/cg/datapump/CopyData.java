package com.cg.datapump;

import java.io.FileInputStream;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class CopyData
{
  private static final XStream xstream = new XStream(new StaxDriver());
  private static DataPump dataPump = null;  
  private static java.sql.Connection sourceCon = null;
  private static java.sql.Connection destCon = null;
  private List<MappingInfo> srcColumLst = new ArrayList();
  private static String srcQuery = "";
  private static String destQuery = "";
  private int count = 0;
  
  public static void main(String[] args)
  {
    CopyData copyData = new CopyData();
    
    System.out.print("Initializing....");
    copyData.init();
    System.out.println("   Done.");
    
    System.out.print("Connecting...");
    copyData.Connect();
    System.out.println("   Done.");
    
    System.out.print("Generating Metadata....");
    if (dataPump.getTable().getForceMatch().equals("true")) {
      copyData.populateMetaData();
    }
    //copyData.overrideColumnMatch(false);
    
    //copyData.isCombine();
    copyData.constuctQueries();
    System.out.println("   Done.");
    
    System.out.println("Initiating data transfer...");
    copyData.transferData();
    
    copyData.closeConnection();
  }
  
  private boolean init()
  {
    xstream.alias("DataPump", DataPump.class);
    xstream.alias("Connection", com.cg.datapump.Connection.class);
    xstream.alias("Table", Table.class);
    xstream.alias("Column", Column.class);
    try
    {
      FileInputStream inputStream = new FileInputStream("./datapump_config.xml");
      dataPump = (DataPump)xstream.fromXML(inputStream);
    }
    catch (Exception e)
    {
      System.err.print("Error: " + e.getMessage());
      return false;
    }
    return true;
  }
  
  private boolean Connect()
  {

    com.cg.datapump.Connection srcCon = dataPump.getSourceConnection();
    com.cg.datapump.Connection detCon = dataPump.getDestConnection();
   
    try
    {
      Class.forName(srcCon.getDriverName());
      Class.forName(detCon.getDriverName());
      sourceCon = DriverManager.getConnection(srcCon.getConnectionURL(), srcCon.getUserId(), srcCon.getPassword());
      destCon = DriverManager.getConnection(detCon.getConnectionURL(), detCon.getUserId(), detCon.getPassword());
    }
    catch (Exception e)
    {
    
      System.err.print("Error: " + e.getMessage());
      return false;
    }
    return true;
  }
  
  private void flushConnection()
  {
    com.cg.datapump.Connection detCon = dataPump.getDestConnection();
    try
    {
      destCon.close();
      destCon = DriverManager.getConnection(detCon.getConnectionURL(), detCon.getUserId(), detCon.getPassword());
    }
    catch (Exception e)
    {
      System.err.print("Error: " + e.getMessage());
    }
  }
  
  private void closeConnection()
  {
    try
    {
      sourceCon.close();
      destCon.close();
    }
    catch (Exception e)
    {
      System.err.print("Error: " + e.getMessage());
    }
  }
  
  private void populateMetaData()
  {
    fetchMetaData(sourceCon, dataPump.getTable().getSourceSchema(), dataPump.getTable().getSourceTable(), false);
    
    fetchMetaData(destCon, dataPump.getTable().getDestSchema(), dataPump.getTable().getDestTable(), true);
  }
  
  private void fetchMetaData(java.sql.Connection con, String schema, String table, boolean flag)
  {
    try
    {
      DatabaseMetaData md = con.getMetaData();
      ResultSet rs = md.getColumns(null, schema.toUpperCase(), table.toUpperCase(), "%");
      while (rs.next())
      {
        MappingInfo mapping = new MappingInfo();
        if (!flag) {
          mapping.setSrcColumn(rs.getString(4));//set column name 
      
        } else {
          mapping.setDestColumn(rs.getString(4));
        }
        mapping.setType(rs.getString(6));
        if (rs.getString(18).equals("NO")) {
          mapping.setNull(false);
          
        } else {
          mapping.setNull(true);
        }
        if (!flag) {
          this.srcColumLst.add(mapping);
        } else {
          populateMappingInfo(mapping);
        }
      }
    }
    catch (Exception e)
    {
      System.err.print("Error: " + e.getMessage());
    }
  }
  
  private void populateMappingInfo(MappingInfo mappingInfo)
  {
    String col = mappingInfo.getDestColumn();
    List<MappingInfo> list = new ArrayList();
    for (MappingInfo info : this.srcColumLst) {
      if (info.getSrcColumn().equals(col))
      {
        info.setDestColumn(col);
        info.setMapped(true);
        break;
      }
    }
    this.srcColumLst.addAll(list);
  }
  
  
 /* private void overrideColumnMatch(boolean usecolumn)
  {
      List list = new ArrayList();
      if(usecolumn && dataPump.getTable().getForceMatch().equals("true") && dataPump.getTable().getColumns().length > 0)
      {
    	  
          for(Iterator iterator = srcColumLst.iterator(); iterator.hasNext();)
          {
        	  
              MappingInfo info = (MappingInfo)iterator.next();
              Column acolumn1[];
              int l = (acolumn1 = dataPump.getTable().getColumns()).length;
             // System.out.println("length of L : "+l);
              for(int k = 0; k < l; k++)
              {
                  Column column = acolumn1[k];
                  if(column.getSourceColumn() != null && !column.getSourceColumn().equals(""))
                  {
                      if(!info.getSrcColumn().equals(column.getSourceColumn()))
                          continue;
                      info.setDestColumn(column.getDestColumn());
                      info.setMapped(true);
                      info.setOverride(true);
                      break;
                  }
                  if(column.getValue() == null || getInfo(column.getDestColumn(), list) != null)
                      continue;
                  MappingInfo info2 = new MappingInfo();
                  info2.setDestColumn(column.getDestColumn());
                  info2.setSrcValue(column.getValue());
                  info2.setMapped(true);
                  info2.setOverride(true);
                  list.add(info2);
                  break;
              }

          }

          srcColumLst.addAll(list);
          removeAllDuplicates();
      } else if(usecolumn)
      {
          Column acolumn[];
          int j = (acolumn = dataPump.getTable().getColumns()).length;
          for(int i = 0; i < j; i++)
          {
              Column column = acolumn[i];
              if(column.getSourceColumn() != null && !column.getSourceColumn().equals(""))
              {
                  MappingInfo info = new MappingInfo();
                  info.setSrcColumn(column.getSourceColumn());
                  info.setDestColumn(column.getDestColumn());
                  info.setMapped(true);
                  info.setOverride(true);
                  srcColumLst.add(info);
              } else
              if(column.getValue() != null)
              {
                  MappingInfo info2 = new MappingInfo();
                  info2.setDestColumn(column.getDestColumn());
                  info2.setSrcValue(column.getValue());
                  info2.setMapped(true);
                  info2.setOverride(true);
                  list.add(info2);
              }
          }

          srcColumLst.addAll(list);
      }
  }
  private void removeAllDuplicates()
  {
    List<MappingInfo> list = new ArrayList();
    for (MappingInfo info : this.srcColumLst) {
      if (info.isOverride()) {
        for (MappingInfo info1 : this.srcColumLst) {
          if (info.getSrcColumn() != null)
          {
            if ((info.getDestColumn().equals(info1.getDestColumn())) && (!info1.isOverride())) {
              list.add(info1);
            } else if ((info.getSrcColumn().equals(info1.getSrcColumn())) && (!info1.isOverride())) {
              list.add(info1);
            }
          }
          else if ((info.getDestColumn().equals(info1.getDestColumn())) && (!info1.isOverride())) {
            list.add(info1);
          }
        }
      }
    }
    this.srcColumLst.removeAll(list);
  }
  
  private boolean isCombine()
  {
    boolean flag = false;
    List<MappingInfo> tmpLst = new ArrayList();
    if (dataPump.getTable().getColumns().length > 0) {
      for (MappingInfo info1 : this.srcColumLst)
      {
        int j = 0;
        if (info1.getDestColumn() != null) {
          for (MappingInfo info2 : this.srcColumLst) {
            if (info1.getDestColumn().equals(info2.getDestColumn()))
            {
              j++;
              if (j > 1)
              {
                info2.setCombineInserts(true);
                flag = true;
              }
            }
          }
        } else {
          tmpLst.add(info1);
        }
      }
    }
    this.srcColumLst.removeAll(tmpLst);
    return flag;
  }*/
  
  private MappingInfo getInfo(String destColm, List<MappingInfo> lst)
  {
    for (MappingInfo info : lst) {
      if (info.getDestColumn().equals(destColm)) {
        return info;
      }
    }
    return null;
  }
  
  private void constuctQueries()
  {
    StringBuffer srcBuffer = new StringBuffer("Select ");
    StringBuffer dstBuffer = new StringBuffer("Insert into " + dataPump.getTable().getDestSchema() + "." + dataPump.getTable().getDestTable() + " ( ");
    StringBuffer tmpHolder = new StringBuffer();
    List<MappingInfo> tmpLst = new ArrayList();
    for (MappingInfo info : this.srcColumLst) {
      if (info.isMapped())
      {
        if (this.count != 0)
        {
          if (info.getSrcColumn() != null) {
            srcBuffer.append(" ,");
           
          }
          if (!info.isCombineInserts())
          {
            dstBuffer.append(" ,");
            tmpHolder.append(" ,");
          }
        }
        if (info.getSrcColumn() != null) {
          srcBuffer.append(info.getSrcColumn());
        }
        if (!info.isCombineInserts())
        {
          dstBuffer.append(info.getDestColumn());
          tmpHolder.append(" ? ");
        }
        this.count += 1;
      }
      else
      {
        tmpLst.add(info);
      }
    }
    this.srcColumLst.remove(tmpLst);
    
    srcBuffer.append(" from ").append(dataPump.getTable().getSourceSchema()).append(".").append(dataPump.getTable().getSourceTable()).append(" ");
    if (dataPump.getTable().getClause() != null) {
      srcBuffer.append(dataPump.getTable().getClause());
    }
    dstBuffer.append(" ) values ( ").append(tmpHolder).append(" )");
  
    srcQuery = srcBuffer.toString();
    destQuery = dstBuffer.toString();
    
  }
  
  private void transferData()
  {
    try
    {
      List<MappingInfo> tmpLst = null;
      MappingInfo mappingInfo = null;
      PreparedStatement srcPstmt = sourceCon.prepareStatement(srcQuery);
      PreparedStatement destPstmt = destCon.prepareStatement(destQuery);
      int flushCount = 0;
      int totalCount = 0;
      
      ResultSet srcRS = srcPstmt.executeQuery();
      while (srcRS.next())
      {
        tmpLst = new ArrayList();
    
        for (MappingInfo info : this.srcColumLst) {
          if (info.getSrcColumn() != null)
          {
        	 
            if (info.isCombineInserts())
            {
              mappingInfo = getInfo(info.getDestColumn(), tmpLst);
              if (mappingInfo == null)
              {
                mappingInfo = new MappingInfo();
                mappingInfo.setDestColumn(info.getDestColumn());
                mappingInfo.setSrcValue(srcRS.getObject(info.getSrcColumn()));
                tmpLst.add(mappingInfo);
              }
              else
              {
            	  mappingInfo.setSrcValue((new StringBuilder()).append(mappingInfo.getSrcValue()).append(srcRS.getObject(info.getSrcColumn())).toString());
              }
            }
            else
            {
              mappingInfo = new MappingInfo();
              mappingInfo.setDestColumn(info.getDestColumn());
              mappingInfo.setSrcValue(srcRS.getObject(info.getSrcColumn()));
              tmpLst.add(mappingInfo);
            }
          }
          else {
            tmpLst.add(info);
          }
        }
        for (int i = 1; i <= tmpLst.size(); i++) {
          destPstmt.setObject(i, ((MappingInfo)tmpLst.get(i - 1)).getSrcValue());
        }
        try
        {
          destPstmt.execute();
          flushCount++;
          totalCount++;
        }
        catch (Exception e)
        {
          System.err.print("Warn: " + e.getMessage());
        }
        if (flushCount > 2000)
        {
          flushConnection();
          destPstmt = destCon.prepareStatement(destQuery);
          flushCount = 0;
        }
      }
      System.out.println("\nTotal Inserted Records: " + totalCount);
    }
    catch (Exception e)
    {
      System.err.print("Error: " + e.getMessage());
    }
  }
}
