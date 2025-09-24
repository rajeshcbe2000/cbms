/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * SelectAllDAO.java
 *
 * Created on August 18, 2003, 4:19 PM
 */
package com.see.truetransact.serverside.common.viewall;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.sysadmin.servicetax.ServiceTaxMaintenanceGroupDAO;
import com.see.truetransact.serverside.batchprocess.task.authorizechk.LoanClosedAccountInterestCalcTask;
import com.see.truetransact.commonutil.Dummy;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import com.see.truetransact.serverutil.ServerConstants;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.*;
import oracle.sql.*;
import oracle.jdbc.OracleResultSet;
import com.see.truetransact.commonutil.Dummy;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.rmi.RemoteException;

import java.sql.*;
import com.see.truetransact.commonutil.Dummy;
import com.see.truetransact.ui.common.viewall.QRCodeUI;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import com.see.truetransact.commonutil.StringEncrypter;
import com.see.truetransact.serverside.common.sms.SmsConfigDAO;
import org.apache.log4j.Logger;

/**
 *
 * @author Balachandar
 */
public class SelectAllDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private String dataBaseURL;
    private String userName;
    private String passWord;
    private String SERVER_ADDRESS;
    private String tableName;
    private String driverName;
    private Connection conn;
    private Statement stmt;
    private ResultSet rset;
    private String cmd;
    private String tableCondition;
    private int isMore = -1;
    private String addCondition;
    private byte[] photoByteArray;
    private File qrCodeFile;
    private HashMap qrCodeMap;
    private String whereCondition;
    private HashMap mapQRCode;
    private FileOutputStream writer = null;
    private File signFile;
    private String customerID;
    private Date currDt = null;
    private StringEncrypter encrypt = null;
    private HashMap getData(HashMap map) throws Exception {
        StringEncrypter encrypt = new StringEncrypter();
        String mapName = (String) map.get(CommonConstants.MAP_NAME);
        HashMap where = null;
        HashMap returnMap =null;
        String strWhere = "";
        if (map.containsKey(CommonConstants.MAP_WHERE)) {
            if (map.get(CommonConstants.MAP_WHERE) instanceof HashMap) {
                where = (HashMap) map.get(CommonConstants.MAP_WHERE);
            } else {
                strWhere = CommonUtil.convertObjToStr(map.get(CommonConstants.MAP_WHERE));
            }
        }

        if (mapName.trim().length() == 0) {
            throw new TTException("Map Name is Null");
        }
        if(mapName.equals("PRINT_SERVRSIDE")){//To print in server log UI data by Kannan AR
            return returnMap = new HashMap();
        }
        if (mapName != null && mapName.equals("getPaidPrinciple") && CommonConstants.SAL_REC_MODULE != null && CommonConstants.SAL_REC_MODULE.equals("Y")) {
            where.put("SALARY_RECOVERY","SALARY_RECOVERY");
        }
        if (mapName != null && mapName.equals("getQRMasterMap")) {
            return getQRFile(map);
        }
        List list = null;
        System.out.println("!!!!! mapName : " + mapName + " / where : " + where + " / strWhere : " + strWhere);
        if (mapName.equals("REPORTS")) {
            list = getReportData(where);
        } else {
            try {
                if (strWhere.length() > 0) {
                    list = (List) sqlMap.executeQueryForList(mapName, strWhere);
                } else {
                    list = (List) sqlMap.executeQueryForList(mapName, where);
                }
            } catch (Exception e) {
                // If you get an error at this point, it matters little what it was. It is going to be
                // unrecoverable and we will want the app to blow up good so we are aware of the
                // problem. You should always log such errors and re-throw them in such a way that
                // you can be made immediately aware of the problem.
                e.printStackTrace();

                throw new TTException("Error initializing SqlConfig class. Cause: " + e);
            }
        }
        where = new HashMap();
        where.put(CommonConstants.DATA, list);
        return where;
    }

    private List getReportData(HashMap where) throws Exception {
        List lst = null;
        java.util.Properties serverProperties = new java.util.Properties();
        try {
            Dummy cons = new Dummy();
            serverProperties.load(cons.getClass().getResourceAsStream("/com/see/truetransact/serverutil/SqlMapConfig.properties"));
            String dataBaseURL = serverProperties.getProperty("url");
            String userName = serverProperties.getProperty("username");
            String passWord = serverProperties.getProperty("password");
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            Connection conn = DriverManager.getConnection(dataBaseURL, userName, passWord);
            Statement stmt = conn.createStatement();
            String st = CommonUtil.convertObjToStr(where.get("STATEMENT"));
            ResultSet rs = stmt.executeQuery(st);
            lst = new ArrayList();
            lst.add(rs);
            serverProperties = null;
            cons = null;
            conn = null;
            stmt = null;
            rs = null;
        } catch (Exception ex) {
            System.out.println("#$#$ Exception in SelectAllDAO getReportData() : " + ex);
            ex.printStackTrace();
        }
        return lst;
    }

    /**
     * Creates a new instance of ViewAllDAO
     */
    public SelectAllDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            SelectAllDAO dao = new SelectAllDAO();
            HashMap mapParam = new HashMap();

            /* HashMap where = new HashMap();
             where.put("beh", "CA");
            
             mapParam.put(CommonConstants.MAP_NAME, "getSelectOperativeAcctProductTOList");
             //mapParam.put("WHERE", where);updateDenominationCount
             List rMap = (List) dao.getData(mapParam).get("DATA");
             System.out.println (rMap.toString());*/

            HashMap where = new HashMap();
//            where.put("CURRENCY", "INR");
//            where.put("DENOMINATION", new Float("500.0"));
//            where.put("COUNT", new Double("-5.0"));
//            
//            mapParam.put(CommonConstants.MAP_NAME, "updateDenominationCount");
//            mapParam.put("WHERE", where);
            ArrayList arrList = new ArrayList();
            ArrayList tmpList = new ArrayList();
            tmpList.add("Account Opening / Modifying pending for Approval");
            tmpList.add("COMPLETED");
            arrList.add(tmpList);
            tmpList = new ArrayList();
            tmpList.add("Check for Accounts Opened with Zero Balance");
            tmpList.add("COMPLETED");
            arrList.add(tmpList);
            mapParam.put("DB_DRIVER_NAME", "com.ibm.db2.jcc.DB2Driver");
            mapParam.put("USER_ID", "sysadmin");
            mapParam.put("BRANCH_CODE", "TMCBHO");
            mapParam.put("EXECUTE_LIST", arrList);
            dao.execute(mapParam);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("!!!!!map : " + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        String mapName = (String) map.get(CommonConstants.MAP_NAME);
        String driverName = (String) map.get("DB_DRIVER_NAME");
        HashMap where = null;
        ServerUtil.callingSMSSchedule(_branchCode);
        if (map.containsKey("EXECUTE_LIST")) {
            ArrayList tblArrayList = (ArrayList) map.get("EXECUTE_LIST");
            java.util.Date currDt = ServerUtil.getCurrentDate(_branchCode);
            java.sql.Date sqlCurrDt = new java.sql.Date(currDt.getTime());
            java.util.Properties serverProperties = new java.util.Properties();
            try {
                Dummy cons = new Dummy();
                serverProperties.load(cons.getClass().getResourceAsStream("/com/see/truetransact/serverutil/SqlMapConfig.properties"));
                String dataBaseURL = serverProperties.getProperty("url");
                String userName = serverProperties.getProperty("username");
                String passWord = serverProperties.getProperty("password");
//                                    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                Class.forName(driverName);
//                                    System.out.println("!!!!!Class.forName(driverName) driver initialized...");
                Connection conn = DriverManager.getConnection(dataBaseURL, userName, passWord);
//                                    System.out.println("!!!!!Connection created...");
//                                    Statement stmt = null;
                ArrayList tempList = null;
                java.sql.CallableStatement cst = null;
//                                    System.out.println("!!!!!tblArrayList..."+tblArrayList);
                for (int i = 0; i < tblArrayList.size(); i++) {
                    tempList = (ArrayList) tblArrayList.get(i);
//                                        System.out.println("!!!!!inside for loop..."+tempList);
                    cst = conn.prepareCall("call EXECUTE_DAYEND_STATUS ( ?, ?, ?, ?, ? )");
//                                        System.out.println("!!!!!Callable Statement created...");
                    cst.setString(1, _branchCode);
                    cst.setString(2, CommonUtil.convertObjToStr(tempList.get(0)));
                    cst.setString(3, CommonUtil.convertObjToStr(tempList.get(1)));
                    cst.setString(4, CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                    cst.setDate(5, sqlCurrDt);
//                                        System.out.println("!!!!!All values set...");
                    cst.executeUpdate();
//                                        System.out.println("!!!!!executeQuery called...");
                }
               // conn.commit();
                cst.close();
                conn.close();
                cst = null;
                serverProperties = null;
                cons = null;
                conn = null;
//                                    stmt = null;
                tempList = null;
            } catch (Exception ex) {
                System.out.println("#$#$ Exception in SelectAllDAO getReportData() : " + ex);
                ex.printStackTrace();
            }
        }else if (map.containsKey("insertQRMasterMap")) {
           return storeQRCode(map);
        }else if (map.containsKey("updateResetOTPQRMasterMap")) {
           return regenerateOTP(map);
        }else {
            if (map.containsKey(CommonConstants.MAP_WHERE)) {
                where = (HashMap) map.get(CommonConstants.MAP_WHERE);
            }

            if (mapName.trim().length() == 0) {
                throw new TTException("Map Name is Null");
            }

            try {
                sqlMap.startTransaction();
                sqlMap.executeUpdate(mapName, where);
                sqlMap.commitTransaction();
            } 
            catch (SQLException e) {
                // If you get an error at this point, it matters little what it was. It is going to be
                // unrecoverable and we will want the app to blow up good so we are aware of the
                // problem. You should always log such errors and re-throw them in such a way that
                // you can be made immediately aware of the problem.
                e.printStackTrace();
                sqlMap.rollbackTransaction();
                throw e;
            }
            catch (Exception e) {
                // If you get an error at this point, it matters little what it was. It is going to be
                // unrecoverable and we will want the app to blow up good so we are aware of the
                // problem. You should always log such errors and re-throw them in such a way that
                // you can be made immediately aware of the problem.
                e.printStackTrace();
                sqlMap.rollbackTransaction();
                throw new TTException("Error initializing SqlConfig class. Cause: " + e);
            }
        }
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        System.out.println("!!!!!obj -----> "+obj);
       // System.out.println("OBJECTX"+obj.get("CALC_ON_MATURITY"));
        HashMap returnMap=new HashMap();
        HashMap loanInterest=new HashMap();
        if(obj.get("WHERE")!=null && obj.get("WHERE") instanceof HashMap){
            loanInterest=(HashMap)obj.get("WHERE");
            if(loanInterest.containsKey("MATURITY_MAP")){
            if(loanInterest != null && loanInterest.get("MATURITY_MAP") != null){
            HashMap matMaps = (HashMap)loanInterest.get("MATURITY_MAP");
            System.out.println("matMaps"+matMaps);
            if(matMaps != null && matMaps.get("CALC_ON_MATURITY") != null){
                if(matMaps.get("CALC_ON_MATURITY").equals("Y")){
                loanInterest.put("CALC_ON_MATURITY", "Y");
                }
                System.out.println("PUTTING"+loanInterest.get("CALC_ON_MATURITY"));
            }
            }
        }
        }
        if (loanInterest.containsKey("LOAN_ACCOUNT_CLOSING")) {
			loanInterest.put("USER_ID",obj.get("USER_ID"));
            returnMap = asAndWhenCustomer(loanInterest);
        } else if (loanInterest.containsKey("SERVICE_TAX")) {
            ServiceTaxMaintenanceGroupDAO serTax = new ServiceTaxMaintenanceGroupDAO();
            HashMap calMap = new HashMap();
            calMap = (HashMap) loanInterest.get("SERVICE_TAX");
            calMap = serTax.execute(calMap);
            System.out.println("calMap----->" + calMap);
            List lst = new ArrayList();
            lst.add(calMap);
            returnMap.put(CommonConstants.DATA, lst);
            System.out.println("returnMap----->" + returnMap);
        } else {
            returnMap = getData(obj);
        }
        return returnMap;
    }
    //as and when customer comes interest calculaion

    private HashMap asAndWhenCustomer(HashMap obj) throws Exception {
        HashMap returnMap = new HashMap();

        TaskHeader header = new TaskHeader();
        header.setBranchID(CommonUtil.convertObjToStr(obj.get("BRANCH_CODE")));
		if (CommonUtil.convertObjToStr(obj.get("FROM_INTERST_REPORT_UI")).equals("FROM_INTERST_REPORT_UI")) {
            LoanClosedAccountInterestCalcTask interestcalTask = new LoanClosedAccountInterestCalcTask(header);
            HashMap resultMap = interestcalTask.interestCalc(obj);
            List lst = new ArrayList();
            lst.add(resultMap);
            returnMap.put(CommonConstants.DATA, lst);
        }else{
            InterestCalculationTask interestcalTask = new InterestCalculationTask(header);
            HashMap resultMap = interestcalTask.interestCalcTermLoanAD(obj);
            List lst = new ArrayList();
            lst.add(resultMap);
            returnMap.put(CommonConstants.DATA, lst);
            return returnMap;
        }
        return returnMap;
    }
    
    private HashMap storeQRCode(HashMap qrCodeMap) throws Exception {
        setDriver();
        HashMap resultMap = new HashMap();
        List lst = sqlMap.executeQueryForList("getRecordExistorNotQRMaster", qrCodeMap);
        if (lst != null && lst.size() > 0) {
            resultMap.put("RE","Image Exist");
            System.out.println("image already exist...");
        } else {
            sqlMap.startTransaction();
            SmsConfigDAO smsDAO = new SmsConfigDAO();
            StringEncrypter encrypt = new StringEncrypter();
            String otpNum = generateOTP();
            qrCodeMap.put("OTP_NUM",otpNum);
            qrCodeMap.put("ACT_NUM",qrCodeMap.get("QR_ACT_NUM"));
            String prodId = CommonUtil.convertObjToStr(qrCodeMap.get("QR_ACT_NUM")).substring(4, 7);
            qrCodeMap.put("PROD_ID",prodId);
            smsDAO.sendOTP(qrCodeMap);
            otpNum = encrypt.encrypt(otpNum);
            qrCodeMap.put("OTP_NUM",otpNum);
            resultMap.put("QR_BANK", getActualQRBankCode());
            qrCodeMap.put("STATUS",CommonConstants.STATUS_CREATED);
            sqlMap.executeUpdate("insertQRMasterMap", qrCodeMap);
            sqlMap.commitTransaction();
            generateQRFileImage(qrCodeMap);
            System.out.println("New image started inserting qrCodeMap : " + qrCodeMap);
            DriverManager.registerDriver(((java.sql.Driver) Class.forName(driverName).newInstance()));
            customerID = CommonUtil.convertObjToStr(qrCodeMap.get("QR_ACT_NUM"));
            conn = DriverManager.getConnection(dataBaseURL, userName, passWord);
            conn.setAutoCommit(false);
            if (driverName.equals("com.ibm.db2.jcc.DB2Driver")) {
                try {
                    String photoString = "";
                    String signString = "";
                    if (photoByteArray != null) {
                        qrCodeFile = new File(ServerConstants.SERVER_PATH + "/customer/" + customerID + "_photo.jpg");
                    } else {
                        qrCodeFile = null;
                    }
                    System.out.println("#$#$ photoFile : " + qrCodeFile);
                    
                    if (qrCodeFile != null) {
                        photoString = "?";
                    } else {
                        photoString = "empty_blob()";
                    }
                    
                    String st = "UPDATE " + tableName + " SET PHOTO_FILE = " + photoString
                            + ", SIGNATURE_FILE = " + signString + " WHERE " + tableCondition + "='" + customerID + "'";
                    PreparedStatement ps = conn.prepareStatement(st);
                    FileInputStream photoFis = null;
                    if (qrCodeFile != null) {
                        int fileLength = (int) qrCodeFile.length();
                        photoFis = new FileInputStream(qrCodeFile);
                        ps.setBinaryStream(1, photoFis, fileLength);
                        System.out.println("#$#$ photoFile...");
                    }
                    
                    System.out.println("#$#$ Update statement is : " + st);
                    //        ps.setString(3, customerID);
                    ps.executeUpdate();
                    ps.close();
                    conn.commit();
                    conn.close();
                    conn = null;
                    photoByteArray = null;
//                    signByteArray = null;
                    if (qrCodeFile != null) {
                        photoFis.close();
                    }
                } catch (Exception se) {
                    se.printStackTrace();
                    System.out.println("SQL Exception : " + se);
                    conn.close();
                    conn = null;
                    qrCodeFile = null;
                    signFile = null;
                    //            writer.flush();
                    //            writer.close();
                    addCondition = "";
//                if (cmd.equals(CommonConstants.TOSTATUS_INSERT)) {
//                    customerTO = null;
//                }
                }
            } else if (driverName.equals("oracle.jdbc.OracleDriver") || driverName.equals("oracle.jdbc.driver.OracleDriver")) {
                try {
                    stmt = conn.createStatement();
                    boolean b = false;
                    String st;
                    tableName = "QR_MASTER";
                    tableCondition = "QR_ACT_NUM";
                    st = "UPDATE " + tableName + " SET QR_FILE = empty_blob() WHERE " + tableCondition + "='" + customerID + "'";
                    System.out.println("Update Statement executed : \n\t" + st);
                    b = stmt.execute(st);
                    System.out.println("##### cmd.equals(CommonConstants.TOSTATUS_INSERT) so, update Statement executed...");   // Print col 1

                    st = "SELECT QR_FILE, " + tableCondition + " FROM " + tableName + " WHERE " + tableCondition + "='" + customerID + "'";
                    st = st + " FOR UPDATE";
                    System.out.println("Statement execute query : \n\t" + st);
                    rset = stmt.executeQuery(st);
                    System.out.println("Statement execute query : " + rset);
                    rset.next();
                    System.out.println("rset.next()... QRFile.... ");
                    BLOB oracleBlob = ((OracleResultSet) rset).getBLOB(1);
                    System.out.println("#$#$ QRFile : " + qrCodeFile);
                    System.out.println("selected file length = " + (qrCodeFile == null ? "null" : qrCodeFile.length()));
                    System.out.println("cust_id = " + rset.getString(2));
                    FileInputStream reader = null;
                    OutputStream outstream = null;
                    int size = 0;
                    byte[] buffer;
                    int length = 0;
                    if (qrCodeFile != null) {
                        reader = new FileInputStream(qrCodeFile);
                        System.out.println("reader initialized ");
                        outstream = oracleBlob.getBinaryOutputStream();
                        System.out.println("outstream initialized ");
                        size = oracleBlob.getBufferSize();
                        buffer = new byte[size];
                        length = -1;
                        while ((length = reader.read(buffer)) != -1) {
                            System.out.println("length : " + length);
                            outstream.write(buffer, 0, length);
                        }
                        resultMap.put("QR_ACT_NUM", qrCodeMap.get("QR_ACT_NUM"));
                        resultMap.put("QR_BANK", getActualQRBankCode());
                        resultMap.put("QR_DETAILS", qrCodeMap.get("QR_DETAILS"));
//                        resultMap.put("QR_FILE", qrCodeFile);
                        resultMap.put("QR_FILE", buffer);
                        System.out.println("outstream written ");
                        System.out.println("#$#$ signFile...");
                        reader.close();
                        outstream.close();
                    }
                    System.out.println("#$#$ Update statement is : " + st);
                    System.out.println("outstream closed ");
                    stmt.close();
                    System.out.println("stmt closed ");
                    conn.commit();
                    conn.close();
                    conn = null;
                    rset.close();
                    outstream = null;
                    reader = null;
                    oracleBlob = null;
                } catch (Exception se) {
                    se.printStackTrace();
                    System.out.println("SQL Exception : " + se);
                    conn.close();
                    stmt.close();
                    conn = null;
                    rset.close();
                    qrCodeFile = null;
                    signFile = null;
                    addCondition = "";
                }
            }
        }
        return resultMap;
    }
    
    private HashMap regenerateOTP(HashMap qrCodeMap) throws Exception {
        sqlMap.startTransaction();
        SmsConfigDAO smsDAO = new SmsConfigDAO();
        StringEncrypter encrypt = new StringEncrypter();
        String otpNum = generateOTP();
//            System.out.println("otpNum : " + otpNum);
        qrCodeMap.put("OTP_NUM",otpNum);
        qrCodeMap.put("ACT_NUM",qrCodeMap.get("QR_ACT_NUM"));
        String prodId = CommonUtil.convertObjToStr(qrCodeMap.get("QR_ACT_NUM")).substring(4, 7);
        qrCodeMap.put("PROD_ID",prodId);
        smsDAO.sendOTP(qrCodeMap);
        otpNum = encrypt.encrypt(otpNum);
//            System.out.println("otpNum : " + otpNum);
        qrCodeMap.put("OTP_NUM",otpNum);
//        qrCodeMap.put("STATUS",CommonConstants.STATUS_CREATED);
        System.out.println("qrCodeMap : "+qrCodeMap);
        sqlMap.executeUpdate("updateResetOTPQRMasterMap", qrCodeMap);
        sqlMap.commitTransaction();
        qrCodeMap.put("STATUS","Successfully OTP sent to registered mobile");
        return qrCodeMap;
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
    
    private HashMap getQRFile(HashMap qrFileMap) throws Exception {
        HashMap resultMap = new HashMap();
        try {
            setDriver();
            conn = DriverManager.getConnection(dataBaseURL, userName, passWord);
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            tableName = "QR_MASTER";
            tableCondition = "QR_ACT_NUM";
            whereCondition = CommonUtil.convertObjToStr(qrFileMap.get("QR_ACT_NUM"));
            String st = "select " + tableCondition + ", QR_FILE from " + tableName + " where " + tableCondition + " = '" + whereCondition + "'";
            System.out.println("#### statement to be executed : " + st);
            rset = stmt.executeQuery(st);
            System.out.println("#### rset.getRow() " + rset.getRow());
            if (rset != null) {
                System.out.println("#### rset not null");
                while (rset.next()) {
                    System.out.println("#### rset in while");
                    java.sql.Blob oracleBlob = null;
                    File f1 = null;
                    FileOutputStream out = null;
                    InputStream in = null;
                    java.net.URI serverURI = null;
                    int fileLength = 0;
                    byte[] blobBytes;
                    int len = 0;
                    if (rset.getBlob(2) != null) {
                        System.out.println("#### Cust ID : " + rset.getString(1));   // Print col 1
                        oracleBlob = rset.getBlob(2);
                        // get the length of the blob
                        long length = oracleBlob.length();

                        // print the length of the blob
                        System.out.println("photo blob length " + length);
                        if (length != 0) {
                            System.out.println("#### " + SERVER_ADDRESS + "QRCode/" + whereCondition + "_QRCode.jpg");
                            serverURI = new java.net.URI(SERVER_ADDRESS + "QRCode/" + whereCondition + "_QRCode.jpg");
                            f1 = new File(ServerConstants.SERVER_PATH + "\\QRCode\\" + whereCondition + "_QRCode.jpg");
                            f1.createNewFile();
                            out = new FileOutputStream(f1);
                            in = oracleBlob.getBinaryStream();
                            //                fileLength = oracleBlob.getBufferSize();
                            System.out.println("fileLength " + fileLength);
                            fileLength = (int) length;
                            blobBytes = new byte[fileLength];
                            System.out.println("blobBytes.length " + blobBytes.length);
                            len = -1;
                            in.read(blobBytes);
                            System.out.println("inside while len " + len);
                            out.write(blobBytes);
                            out.close();
                            in.close();
//                            mapQRCode.put("PHOTO", blobBytes);
                            resultMap.put("QR_FILE", blobBytes);
                        }
                    }
                    f1 = null;
                    out = null;
                    in = null;
                    oracleBlob = null;
                }
                
            }
//            qrCodeFile = new File(ServerConstants.SERVER_PATH+"/QRCode/"+qrFileMap.get("QR_ACT_NUM")+"_QRCode.png");
            resultMap.put("QR_ACT_NUM", qrFileMap.get("QR_ACT_NUM"));
            resultMap.put("QR_BANK", getActualQRBankCode());
            resultMap.put("QR_DETAILS", qrFileMap.get("QR_DETAILS"));
//            resultMap.put("QR_FILE", qrCodeFile);
            addCondition = "";
            //        conn.commit();
//            rset.close();
//            stmt.close();
//            conn.close();
        } catch (Exception se) {
            se.printStackTrace();
            addCondition = "";
            rset.close();
            stmt.close();
            System.out.println("SQL Exception : " + se);
        }
        return resultMap;
    }
    
    private void generateQRFileImage(HashMap qrCodeMap){
        String qrCodeData;
        try {
            StringEncrypter encrypt = new StringEncrypter();
            qrCodeData = "Fin"+getActualQRBankCode()+qrCodeMap.get("QR_ACT_NUM")+"Curo";
            System.out.println("qrCodeData : "+qrCodeData);
            qrCodeData = encrypt.encrypt(qrCodeData);
            System.out.println("encrypted qrCodeData : "+qrCodeData);                    
            String filePath = ServerConstants.SERVER_PATH+"/QRCode/"+qrCodeMap.get("QR_ACT_NUM")+"_QRCode.png";
            String charset = "UTF-8"; // or "ISO-8859-1"
            Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            createQRCode(qrCodeData, filePath, charset, hintMap, 200, 200);
            qrCodeFile = new File(ServerConstants.SERVER_PATH+"/QRCode/"+qrCodeMap.get("QR_ACT_NUM")+"_QRCode.png");
            System.out.println("filePath : "+filePath+" qrCodeFile : "+qrCodeFile);              
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(QRCodeUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void createQRCode(String qrCodeData, String filePath,
            String charset, Map hintMap, int qrCodeheight, int qrCodewidth)
            throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(
                new String(qrCodeData.getBytes(charset), charset),
                BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
        MatrixToImageWriter.writeToFile(matrix, filePath.substring(filePath.lastIndexOf('.') + 1), new File(filePath));
    }

    public static String readQRCode(String filePath, String charset, Map hintMap)
            throws FileNotFoundException, IOException, NotFoundException {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(
                ImageIO.read(new FileInputStream(filePath)))));
        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,
                hintMap);
        return qrCodeResult.getText();
    }
    
    private String generateOTP(){
        Random r = new Random();
        String otp = new String();
        for(int i=0 ; i < 6 ; i++) {
            otp += r.nextInt(10);
        }
        return otp;
    }
    
    private String getActualQRBankCode() throws Exception{//This query to get actual bank code from branch_master table, for that hardcoded this branch code
        String qrBankCode = "";
        HashMap qrBankCodeMap = new HashMap();
        List lst = sqlMap.executeQueryForList("getBankCodeDetails", null);
        if (lst != null && lst.size() > 0) {
            qrBankCodeMap = (HashMap)lst.get(0);
            qrBankCode = CommonUtil.convertObjToStr(qrBankCodeMap.get("BANK_CODE"));
            System.out.println("qrBankCode : "+qrBankCode);
        }
        return qrBankCode;
    }
}
