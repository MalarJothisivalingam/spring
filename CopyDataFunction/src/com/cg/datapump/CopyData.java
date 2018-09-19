package com.cg.datapump;

import java.io.FileInputStream;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
public class CopyData
{
  private static final XStream xstream = new XStream(new StaxDriver());
  private static DataPump dataPump = null;  
  private static java.sql.Connection sourceCon = null;
  private static java.sql.Connection destCon = null;
  private List<MappingInfo> srcColumLst = new ArrayList<MappingInfo>();
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
    if (dataPump.getTables()[0].getForceMatch().equals("true")) {
      copyData.populateMetaData();
    }
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
    xstream.alias("table", Table.class);
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
    fetchMetaData(sourceCon, dataPump.getTables()[0].getSourceSchema(), dataPump.getTables()[0].getSourceTable(), false);
    
    fetchMetaData(destCon, dataPump.getTables()[0].getDestSchema(), dataPump.getTables()[0].getDestTable(), true);
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
    List<MappingInfo> list = new ArrayList<MappingInfo>();
    for (MappingInfo info : this.srcColumLst) {
      if (info.getSrcColumn().equals(col))
      {
        info.setDestColumn(col);
        info.setMapped(true);
        break;
      }
    }
   // this.srcColumLst.addAll(list);
  }
   
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
    StringBuffer dstBuffer = new StringBuffer("Insert into " + dataPump.getTables()[0].getDestSchema() + "." + dataPump.getTables()[0].getDestTable() + " ( ");
    StringBuffer tmpHolder = new StringBuffer();
    List<MappingInfo> tmpLst = new ArrayList<MappingInfo>();
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
    
    srcBuffer.append(" from ").append(dataPump.getTables()[0].getSourceSchema()).append(".").append(dataPump.getTables()[0].getSourceTable()).append(" ");
    if (dataPump.getTables()[0].getClause() != null) {
      srcBuffer.append(dataPump.getTables()[0].getClause());
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
        tmpLst = new ArrayList<MappingInfo>();
    
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
