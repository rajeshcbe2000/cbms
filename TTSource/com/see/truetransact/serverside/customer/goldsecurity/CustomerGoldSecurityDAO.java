/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * CustomerGoldSecurityDAO.java
 *
 * Created on January 13, 2005, 4:16 PM
 */
package com.see.truetransact.serverside.customer.goldsecurity;

import com.see.truetransact.serverside.customer.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.ibatis.db.sqlmap.cache.CacheModel;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.Dummy;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.transferobject.customer.security.InsuranceTO;

import com.see.truetransact.transferobject.TransferObject;
import com.see.truetransact.transferobject.customer.goldsecurity.CustomerGoldSecurityTO;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import oracle.jdbc.OracleResultSet;
import oracle.sql.BLOB;
/**
 *
 * @author 152713
 */
public class CustomerGoldSecurityDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LogDAO logDAO;
    private LogTO logTO;
    private HashMap securityMap;
    private HashMap insuranceMap;
    private Date currDt = null;
    private byte[] photoByteArray;
    private String driverName;
    private Connection conn;
    private Statement stmt;
    private ResultSet rset;
    private String cmd;
    private String dataBaseURL;
    private String userName;
    private String passWord;
    private String SERVER_ADDRESS;
    private String tableName;
    private String tableCondition;
    private File photoFile;
    private FileOutputStream writer = null;
    private HashMap mapPhotoSign;
    /**
     * Creates a new instance of SecurityInsuranceDAO
     */
    public CustomerGoldSecurityDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        String goldSecurityId = "";
        HashMap returnMap = new HashMap();
        String where = CommonUtil.convertObjToStr(map.get(CommonConstants.MAP_WHERE));

        List list = (List) sqlMap.executeQueryForList("getSelectCustomerGoldSecurityTO", where);
        returnMap.put("SecurityTO", list);
        if(list != null && list.size() > 0){
            CustomerGoldSecurityTO objCustomerGoldSecurityTO = (CustomerGoldSecurityTO)list.get(0);
            goldSecurityId = objCustomerGoldSecurityTO.getGoldSecurityId();
        }
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectInsuranceTO", where);
        returnMap.put("InsuranceTO", list);
        list = null;
        
        getStockPhoto(goldSecurityId);
        returnMap.put("STOCK_PHOTO_FILE", mapPhotoSign);
        mapPhotoSign = null;

        map = null;
        where = null;
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;
    }

    private void executeAllTabQuery() throws Exception {
        executeSecurityTabQuery();         
    }

    private String getDocumentID() throws Exception {      
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "GOLD_SECURITY_ID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");        
        where.put(CommonConstants.BRANCH_ID, _branchCode);  
        String securityId = CommonUtil.convertObjToStr((dao.executeQuery(where)).get(CommonConstants.DATA));
        return securityId;        
    }
    
    
    private void executeSecurityTabQuery() throws Exception {
        CustomerGoldSecurityTO objSecurityTO;
        Set keySet;
        Object[] objKeySet;
        try {
            keySet = securityMap.keySet();
            objKeySet = (Object[]) keySet.toArray();
            // To retrieve the SecurityTO from the securityMap
            for (int i = securityMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                objSecurityTO = (CustomerGoldSecurityTO) securityMap.get(objKeySet[j]);
                logTO.setData(objSecurityTO.toString());
                logTO.setPrimaryKey(objSecurityTO.getKeyData());
                logTO.setStatus(objSecurityTO.getCommand());
                if (objSecurityTO.getCommand().equalsIgnoreCase("INSERT")) {
                    String genId = getDocumentID();
                    objSecurityTO.setGoldSecurityId(genId);
                }else if(objSecurityTO.getCommand().equalsIgnoreCase("UPDATE")){
                  HashMap dataMap = new HashMap();
                  dataMap.put("CUST_ID",objSecurityTO.getCustId());
                  dataMap.put("RELEASE_DT",currDt.clone());
                  sqlMap.executeUpdate("updateCustomerGoldSecurityReleaseDt", dataMap);
                }
                executeOneTabQueries("CustomerGoldSecurityTO", objSecurityTO);
                if (photoByteArray != null) {
                    storePhotoSign(objSecurityTO.getGoldSecurityId());
                    photoByteArray = null;
                }
                logDAO.addToLog(logTO);
                objSecurityTO = null;
            }
            keySet = null;
            objKeySet = null;
            objSecurityTO = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void executeOneTabQueries(String strTOName, TransferObject TO) throws Exception {
        try {
            StringBuffer sbMapName = new StringBuffer();
            if (TO.getCommand() != null) {
                if(TO.getCommand().equalsIgnoreCase("UPDATE")){                 
                  TO.setCommand("INSERT");                                 
                }
                sbMapName.append(TO.getCommand().toLowerCase());
                sbMapName.append(strTOName);
                executeUpdate(sbMapName.toString(), TO);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void executeUpdate(String str, Object objTO) throws Exception {
        try {
            sqlMap.executeUpdate(str, objTO);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void authorize(HashMap map) throws Exception {
        String strCustID;
        CustomerGoldSecurityTO objTO = null;
        try {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            HashMap dataMap;
            String status = CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS));
            ArrayList selectedList = (ArrayList) authMap.get(CommonConstants.AUTHORIZEDATA);
            sqlMap.startTransaction();
            for (int i = selectedList.size() - 1, j = 0; i >= 0; --i, ++j) {
                dataMap = (HashMap) selectedList.get(j);

                dataMap.put(CommonConstants.AUTHORIZESTATUS, status);
                dataMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
                dataMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));

                /**
                 * Update the Authorization Fields and Update the Available
                 * Balance.
                 */
                sqlMap.executeUpdate("authorizeCustomerGoldSecurity", dataMap);
               // sqlMap.executeUpdate("authorizeCust_Insurance", dataMap);

                // AuthorizeStatus is Authorized
                if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                    //                        sqlMap.executeUpdate("updateInventoryMasterAvailBooks", dataMap);
                } else if (status.equalsIgnoreCase(CommonConstants.STATUS_REJECTED)) {
                    // Exisiting status is Created or Modified
                    if (!(CommonUtil.convertObjToStr(dataMap.get("STATUS")).equalsIgnoreCase(CommonConstants.STATUS_CREATED)
                            || CommonUtil.convertObjToStr(dataMap.get("STATUS")).equalsIgnoreCase(CommonConstants.STATUS_MODIFIED))) {

                        dataMap.put(CommonConstants.STATUS, CommonConstants.STATUS_MODIFIED);
                        //                            sqlMap.executeUpdate("rejectInventoryDetails", dataMap);
                    }
                }
                logTO.setStatus(CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS)));
                logTO.setData(dataMap.toString());
                logTO.setPrimaryKey(CommonUtil.convertObjToStr(dataMap.get("CUST_ID")));
                logDAO.addToLog(logTO);
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        
        System.out.println("inside CustomerGoldSecurityDAO map ::" + map);
        logDAO = new LogDAO();
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setSelectedBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.SELECTED_BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
        _branchCode = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
        currDt = ServerUtil.getCurrentDate(_branchCode);
        securityMap = (HashMap) map.get("SecurityTO");
        photoByteArray = (byte[]) map.get("PHOTO");
        //System.out.println("securityMap :: " + securityMap);

        if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(map);
            }
        } else {
            executeAllTabQuery();
        }

        destroyObjects();
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        logDAO = null;
        logTO = null;
        securityMap = null;
        insuranceMap = null;
    }
    
    
  private void setDriver() throws Exception {
        java.util.Properties serverProperties = new java.util.Properties();
        try {
            Dummy cons = new Dummy();
            serverProperties.load(cons.getClass().getResourceAsStream("/com/see/truetransact/serverutil/SqlMapConfig.properties"));
            dataBaseURL = serverProperties.getProperty("url");
            userName = serverProperties.getProperty("username");
            passWord = serverProperties.getProperty("password");
            driverName = serverProperties.getProperty("driver");
            Class.forName(serverProperties.getProperty("driver"));
            serverProperties = new java.util.Properties();
            serverProperties.load(cons.getClass().getResourceAsStream("/com/see/truetransact/commonutil/TT.properties"));
            SERVER_ADDRESS = "http://" + CommonConstants.SERVER_IP_ADDR + ":" + serverProperties.getProperty("HTTP_PORT") + "/" + serverProperties.getProperty("HTTP_CONTEXT");
            System.out.println("#### SERVER_ADDRESS : " + SERVER_ADDRESS);
            serverProperties = null;
            cons = null;
        } catch (Exception ex) {
        }
    }  
  
   private void storePhotoSignFilesInServer(String goldSecurityId) throws Exception {
        photoFile = new File(ServerConstants.SERVER_PATH + "/goldloan/" + goldSecurityId + "_stock.jpg");
        writer = new FileOutputStream(photoFile);
//        FileOutputStream writer = new FileOutputStream(file);
        System.out.println("#### inside storePhotoSignFilesInServer() After FileOutputStream creation...");
//        System.out.println("##### byteArray : "+byteArray);
        if (photoByteArray != null) {
            writer.write(photoByteArray);
            System.out.println("##### inside writer.write(photoByteArray) :" + photoByteArray.toString());
        } else {
            photoFile = null;
        }
        writer.flush();
        writer.close();       
    }
    
   private void storePhotoSign(String goldSecurityId)throws Exception {
//        DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
        setDriver();
        DriverManager.registerDriver(((java.sql.Driver) Class.forName(driverName).newInstance()));

        storePhotoSignFilesInServer(goldSecurityId);
        conn = DriverManager.getConnection(dataBaseURL, userName, passWord);
        // @machineName:port:SID,   userid,  password
        conn.setAutoCommit(false);

        if (driverName.equals("com.ibm.db2.jcc.DB2Driver")) {
            
        } else if (driverName.equals("oracle.jdbc.OracleDriver") || driverName.equals("oracle.jdbc.driver.OracleDriver")) {
            try {
                stmt = conn.createStatement();
                boolean b = false;
                String st;
             
                    st = "UPDATE " + "CUST_GOLD_SECURITY_DETAILS" + " SET STOCK_PHOTO_FILE = empty_blob() WHERE GOLD_SECURITY_ID ='" + goldSecurityId + "'";                    
                    //System.out.println("Update Statement executed : \n\t" + st);
                    b = stmt.execute(st);
                    //System.out.println("##### cmd.equals(CommonConstants.TOSTATUS_INSERT) so, update Statement executed..."); 
                    st = "SELECT STOCK_PHOTO_FILE, STOCK_PHOTO_FILE FROM " + "CUST_GOLD_SECURITY_DETAILS" + " WHERE GOLD_SECURITY_ID ='" + goldSecurityId + "'";                
                    st = st + " FOR UPDATE";
                //System.out.println("Statement execute query : \n\t" + st);
                rset = stmt.executeQuery(st);
                //System.out.println("Statement execute query : " + rset);
                rset.next();
                //System.out.println("rset.next()... Photo.... ");
                BLOB oracleBlob = ((OracleResultSet) rset).getBLOB(1);
                //System.out.println("#$#$ photoFile : " + photoFile);
                //System.out.println("selected file length = " + (photoFile == null ? "null" : photoFile.length()));
                //System.out.println("cust_id = " + rset.getString(2));
                FileInputStream reader = null;
                OutputStream outstream = null;
                int size = 0;
                byte[] buffer;
                int length = 0;
//                int oldLength = 0;
                if (photoFile != null) {
                    reader = new FileInputStream(photoFile);
                    //System.out.println("reader initialized ");
                    outstream = oracleBlob.getBinaryOutputStream();
                    //System.out.println("outstream initialized ");
                    size = oracleBlob.getBufferSize();
                    buffer = new byte[size];
                    length = -1;
//                    oldLength = 0;
                    while ((length = reader.read(buffer)) != -1) {
                        //System.out.println("length : " + length);
                        outstream.write(buffer, 0, length);
//                        oldLength = length;
                    }
                    //System.out.println("outstream written ");
                    reader.close();
                    outstream.close();
                }
                //System.out.println("rset.next()... Signature.... ");           
                //System.out.println("outstream closed ");
                stmt.close();
                //System.out.println("stmt closed ");
                conn.commit();
                conn.close();
                conn = null;
                rset.close();
                if (photoFile != null) {
                    reader.close();
                }
                outstream = null;
                reader = null;
                oracleBlob = null;
                photoFile = null;              
            } catch (Exception se) {
                se.printStackTrace();
                System.out.println("SQL Exception : " + se);
                conn.close();
                stmt.close();
                conn = null;
                rset.close();
                photoFile = null;               
            }
        }
    }
   
   
  
   private void getStockPhoto(String goldsecurityId) throws Exception {
        try {
            mapPhotoSign = new HashMap();           
            setDriver();
            conn = DriverManager.getConnection(dataBaseURL, userName, passWord);            
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            String st = "select GOLD_SECURITY_ID,STOCK_PHOTO_FILE from CUST_GOLD_SECURITY_DETAILS where " + "GOLD_SECURITY_ID = '" + goldsecurityId + "'";            
            //System.out.println("#### statement to be executed : " + st);
            rset = stmt.executeQuery(st);
            //System.out.println("#### rset.getRow() " + rset.getRow());
            if (rset != null) {
                //System.out.println("#### rset not null");
                while (rset.next()) {
                   // System.out.println("#### rset in while");
                    java.sql.Blob oracleBlob = null;
                    File f1 = null;
                    FileOutputStream out = null;
                    InputStream in = null;
                    java.net.URI serverURI = null;
                    int fileLength = 0;
                    byte[] blobBytes;
                    int len = 0;
                    if (rset.getBlob(2) != null) {
                        //System.out.println("#### Cust ID : " + rset.getString(1));   // Print col 1
                        oracleBlob = rset.getBlob(2);                       
                        long length = oracleBlob.length();                       
                        //System.out.println("photo blob length " + length);
                        if (length != 0) {
                            //System.out.println("#### " + SERVER_ADDRESS + "goldloan/" + actNo + "_photo.jpg");
                            serverURI = new java.net.URI(SERVER_ADDRESS + "goldloan/" + goldsecurityId + "_photo.jpg");
                            f1 = new File(ServerConstants.SERVER_PATH + "\\goldloan\\" + goldsecurityId + "_photo.jpg");
                            f1.createNewFile();
                            out = new FileOutputStream(f1);
                            in = oracleBlob.getBinaryStream();                          
                            //System.out.println("fileLength " + fileLength);
                            fileLength = (int) length;
                            blobBytes = new byte[fileLength];
                            //System.out.println("blobBytes.length " + blobBytes.length);
                            len = -1;
                            in.read(blobBytes);
                            //System.out.println("inside while len " + len);
                            out.write(blobBytes);
                            out.close();
                            in.close();                         
                            mapPhotoSign.put("PHOTO", blobBytes);
                        }
                    }                                     
                    f1 = null;
                    out = null;
                    in = null;
                    oracleBlob = null;
                }
            }           
            rset.close();
            stmt.close();
        } catch (Exception se) {
            se.printStackTrace();         
            rset.close();
            stmt.close();
            System.out.println("SQL Exception : " + se);
        }
    }
    
    // END
    
}
