/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmployeeMasterDAO.java
 *
 * Created on June 18, 2003, 4:14 PM
 */
package com.see.truetransact.serverside.employee;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Date;
import java.rmi.RemoteException;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.transferobject.employee.EmployeeMasterTO;
import com.see.truetransact.transferobject.employee.EmployeeMasterAddressTO;
import com.see.truetransact.transferobject.employee.EmployeeMasterPassPortTO;
import com.see.truetransact.transferobject.employee.EmployeeMasterEducationTO;
import com.see.truetransact.transferobject.employee.EmployeeMasterTechnicalTO;
import com.see.truetransact.transferobject.employee.EmployeeMasterLanguageTO;
import com.see.truetransact.transferobject.employee.EmployeeMasterDependendTO;
import com.see.truetransact.transferobject.employee.EmployeeMasterPhoneTO;
import com.see.truetransact.transferobject.employee.EmployeeRelativeWorkingTO;
import com.see.truetransact.transferobject.employee.EmployeeRelativeDirectorTO;
import com.see.truetransact.transferobject.employee.EmployeePresentDetailsTO;
import com.see.truetransact.transferobject.employee.EmployeeOprativeTO;
import com.see.truetransact.transferobject.employee.EmployeeTermLoanTO;
import com.see.truetransact.transferobject.common.PromotionTO;
import com.see.truetransact.servicelocator.*;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;


import java.io.OutputStream;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.*;
import oracle.sql.*;
import oracle.jdbc.*;
import com.see.truetransact.commonutil.Dummy;
import com.see.truetransact.clientutil.ClientConstants;

import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverside.common.introducer.IntroducerDAO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.deposit.JointAccntTO;
import java.util.List;
import java.util.Date;

/**
 * This is used for AccountHead Data Access.
 *
 * @author Balachandar
 */
public class EmployeeMasterDAO extends TTDAO {

    private SqlMap sqlMap;
    private HashMap data;
    private EmployeeMasterTO objEmployeeMasterTO;
    private HashMap addressMap;
    private LinkedHashMap academicMap;
    private LinkedHashMap technicalMap;
    private HashMap languageMap;
    private HashMap promotionMap;
    private HashMap deletedPromotionMap;
    private LinkedHashMap dependentMap;
    private HashMap relativeMap;
    private LinkedHashMap directorMap;
    private HashMap loanMap;
    private HashMap deletedloanMap;
    private HashMap oprativeMap;
    private HashMap deletedoprativeMap;
    private HashMap deletedDirectorMap;
    private HashMap deletedTechnicalMap;
    private HashMap deletedLanguageMap;
    private HashMap deletedDependentMap;
    private HashMap deletedAddressMap;
    private HashMap deletedacademicMap;
    private HashMap deletedPhoneMap;
    private HashMap deletedRelativeMap;
    private HashMap deletedIncomeMap;
    private HashMap deletedLandDetMap;
    private HashMap IncParMap;
    private HashMap LandDetMap;
    private HashMap phoneMap;
    private EmployeeMasterAddressTO objEmployeeMasterAddressTO;
    private EmployeeMasterEducationTO objEmployeeMasterEducationTO;
    private EmployeeMasterPassPortTO objEmployeeMasterPassPortTO;
    private EmployeePresentDetailsTO objEmployeePresentDetailsTO;
    private EmployeeMasterTechnicalTO objEmployeeMasterTechnicalTO;
    private EmployeeMasterLanguageTO objEmployeeMasterLanguageTO;
    private EmployeeMasterDependendTO objEmployeeMasterDependendTO;
    private EmployeeMasterPhoneTO objEmployeeMasterPhoneTO;
    private EmployeeRelativeDirectorTO objEmployeeRelativeDirectorTO;
    private EmployeeOprativeTO objEmployeeOprativeTO;
    private EmployeeTermLoanTO objEmployeeTermLoanTO;
    private PromotionTO objPromotionTO;
    private EmployeeRelativeWorkingTO objEmployeeRelativeWorkingTO;
    private HashMap phoneList;
    private String key;
    private Iterator addressIterator;
    private Iterator incomeIterator;
    private LogDAO logDAO;
    private LogTO logTO;
    private Iterator phoneIterator;
    //Used in Executequery
    private String whereCondition;
    private HashMap whereConditions;
    private List list;
    private LinkedHashMap mapJointAccntTO;
    private JointAccntTO jointAccntTO;
    private HashMap mapAuthPersonsTO;  //Added by Rajesh
    //    private HashMap objAuthPersonsTO;  //Added by Rajesh
    private HashMap mapPhotoSign;
    //    private String photoFile;
    //    private String signFile;
    private String commonFile;
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
    private int isMore = -1;
    private String addCondition;
    private byte[] photoByteArray;
    private File photoFile;
    private final String CUST_PHOTO = "customer\\photos\\";
    //    private final String CUST_PHOTO = "customer/photos/";
    private byte[] signByteArray;
    private File signFile;
    private final String CUST_SIGN = "customer\\signatures\\";
    //    private final String CUST_SIGN = "customer/signatures/";
    private final int PHOTOGRAPH = 1;
    private final int SIGNATURE = 2;
    private String addressKey = new String();
    private String systemEmpID = "";
    final String SCREEN = "CUS";
    private final String YES = "Y";
    private Date CurrDt = null;

    /**
     * Creates a new instance of CustomerDAO for manipulating Customer data
     */
    public EmployeeMasterDAO() throws ServiceLocatorException {
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    //--- Inserts the data Record by record in JointAccntTO Table
    private void insertJointAccntDetails() throws Exception {
        JointAccntTO jointAccntTO;
        int jointAccntSize = mapJointAccntTO.size();
        for (int i = 0; i < jointAccntSize; i++) {
            try {
                jointAccntTO = (JointAccntTO) mapJointAccntTO.get(String.valueOf(i));
                //                jointAccntTO.setDepositNo(customerTO.getCustId());
                sqlMap.executeUpdate("insertCustJointAccntTO", jointAccntTO);
                logTO.setData(jointAccntTO.toString());
                logTO.setPrimaryKey(jointAccntTO.getKeyData());
                logDAO.addToLog(logTO);
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                throw new TransRollbackException(e);
            }
        }
    }

    /**
     * To insert data
     */
    private void insertData() throws Exception {
        systemEmpID = getEmployeeID();
        getAllTOs();
        objEmployeeMasterTO.setSysId(systemEmpID);
        objEmployeeMasterTO.setStatusBy(logTO.getUserId());
        logTO.setData(objEmployeeMasterTO.toString());
        logTO.setPrimaryKey(objEmployeeMasterTO.getKeyData());
        logTO.setStatus(objEmployeeMasterTO.getCommand());
        sqlMap.executeUpdate("insertEmployeeMasterTO", objEmployeeMasterTO);
        logDAO.addToLog(logTO);
        if (objEmployeePresentDetailsTO != null) {
            objEmployeePresentDetailsTO.setSysId(systemEmpID);
            objEmployeePresentDetailsTO.setStatusBy(objEmployeeMasterTO.getStatusBy());
            objEmployeePresentDetailsTO.setStatusDt(objEmployeeMasterTO.getStatusDt());
            objEmployeePresentDetailsTO.setStatus(objEmployeeMasterTO.getStatus());
            sqlMap.executeUpdate("insertEmployeePresentDetailsTO", objEmployeePresentDetailsTO);
        }
        if (objEmployeeMasterPassPortTO != null) {
            if (systemEmpID != null) {
                objEmployeeMasterPassPortTO.setTxtEmpId(systemEmpID);
            }
            logTO.setData(objEmployeeMasterPassPortTO.toString());
            logTO.setPrimaryKey(objEmployeeMasterPassPortTO.getKeyData());
            logTO.setStatus(objEmployeeMasterPassPortTO.getCommand());
            objEmployeeMasterPassPortTO.setStatus("CREATED");
            sqlMap.executeUpdate("insertEmployeeMasterPassPortTO", objEmployeeMasterPassPortTO);
        }
        logDAO.addToLog(logTO);
        processAddressData(objEmployeeMasterTO.getCommand());
        processAcademicData(objEmployeeMasterTO.getCommand());
        processTechnicalData(objEmployeeMasterTO.getCommand());
        processLanguageData(objEmployeeMasterTO.getCommand());
        processDependentData(objEmployeeMasterTO.getCommand());
        processRelativeData(objEmployeeMasterTO.getCommand());
        processDirectorData(objEmployeeMasterTO.getCommand());
        processOprativeData(objEmployeeMasterTO.getCommand());
        logDAO.addToLog(logTO);
        makeDataNull();
        makeNull();
    }

    private void storePhotoSign() throws Exception {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            setDriver();
            storePhotoSignFilesInServer();
            conn = DriverManager.getConnection(dataBaseURL, userName, passWord);
            // @machineName:port:SID,   userid,  password
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            boolean b = false;
            String st;
            if (isMore == 0) {
                st = "UPDATE " + tableName + " SET PHOTO_FILE = empty_blob(), SIGNATURE_FILE = empty_blob() WHERE " + tableCondition + "='" + systemEmpID + "'";
                if (tableName.equals("CORP_AUTH_CUST")) {
                    st = st + addCondition;
                }
                System.out.println("Update Statement executed : \n\t" + st);
                b = stmt.execute(st);
                System.out.println("##### cmd.equals(CommonConstants.TOSTATUS_INSERT) so, update Statement executed...");   // Print col 1
            }
            //        conn.commit();
            st = "SELECT PHOTO_FILE, SIGNATURE_FILE, " + tableCondition + " FROM " + tableName + " WHERE " + tableCondition + "='" + systemEmpID + "'";
            if (tableName.equals("CORP_AUTH_CUST")) {
                st = st + addCondition;
            }
            st = st + " FOR UPDATE";
            System.out.println("Statement execute query : \n\t" + st);
            rset = stmt.executeQuery(st);
            System.out.println("Statement execute query : " + rset);
            rset.next();
            System.out.println("rset.next()... Photo.... ");
            BLOB oracleBlob = null;//((OracleResultSet)rset).getBLOB(1);
            System.out.println("cust_id = " + rset.getString(3));
            FileInputStream reader = null;
            OutputStream outstream = null;
            int size = 0;
            byte[] buffer;
            int length = 0;
            int oldLength = 0;
            if (photoFile != null) {
                System.out.println("selected file length = " + photoFile.length());
                oracleBlob = ((OracleResultSet) rset).getBLOB(1);
                reader = new FileInputStream(photoFile);
                System.out.println("reader initialized ");
                outstream = oracleBlob.getBinaryOutputStream();
                System.out.println("outstream initialized ");
                size = oracleBlob.getBufferSize();
                buffer = new byte[size];
                length = -1;
                oldLength = 0;
                while ((length = reader.read(buffer)) != -1) {
                    System.out.println("length : " + length);
                    outstream.write(buffer, oldLength, length);
                    oldLength = length;
                }
                System.out.println("outstream written ");
                reader.close();
                outstream.close();
            }
            System.out.println("rset.next()... Signature.... ");
            System.out.println("cust_id = " + rset.getString(3));
            if (signFile != null) {
                System.out.println("selected file length = " + signFile.length());
                oracleBlob = ((OracleResultSet) rset).getBLOB(2);
                reader = new FileInputStream(signFile);
                System.out.println("reader initialized ");
                outstream = oracleBlob.getBinaryOutputStream();
                System.out.println("outstream initialized ");
                size = oracleBlob.getBufferSize();
                buffer = new byte[size];
                length = -1;
                oldLength = 0;
                while ((length = reader.read(buffer)) != -1) {
                    System.out.println("length : " + length);
                    //            length = oldLength + length;
                    outstream.write(buffer, oldLength, length);
                    oldLength = length;
                }
                System.out.println("outstream written ");
                reader.close();
                System.out.println("reader closed ");
                outstream.close();
            }
            System.out.println("outstream closed ");
            stmt.close();
            System.out.println("stmt closed ");
            conn.commit();
            conn.close();
            rset.close();
            photoFile = null;
            signFile = null;
            addCondition = "";
            if (cmd.equals(CommonConstants.TOSTATUS_INSERT)) {
                objEmployeeMasterTO = null;
            }
        } catch (Exception se) {
            //            System.out.println("SQL Exception : "+se);
            se.printStackTrace();
            conn.close();
            stmt.close();
            rset.close();
            photoFile = null;
            signFile = null;
            addCondition = "";
            if (cmd.equals(CommonConstants.TOSTATUS_INSERT)) {
                objEmployeeMasterTO = null;
            }
        }
    }

    private void storePhotoSignFilesInServer() throws Exception {
        photoFile = new File(ServerConstants.SERVER_PATH + "\\customer\\" + systemEmpID + "_photo.jpg");
        FileOutputStream writer = new FileOutputStream(photoFile);
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
        signFile = new File(ServerConstants.SERVER_PATH + "\\customer\\" + systemEmpID + "_sign.jpg");
        writer = new FileOutputStream(signFile);
        if (signByteArray != null) {
            writer.write(signByteArray);
            System.out.println("##### inside writer.write(signByteArray) :" + signByteArray.toString());
        } else {
            signFile = null;
        }
        writer.flush();
        writer.close();
        photoByteArray = null;
        signByteArray = null;
    }

    private byte[] createByteArray(String createFile) throws Exception {
        final File selFile = new File(createFile);
        final FileInputStream reader = new FileInputStream(selFile);
        final int size = reader.available();
        byte[] byteArr = new byte[size];
        reader.read(byteArr);
        reader.close();
        return byteArr;
    }

    /**
     * To get auto generated CustomerID from table
     */
    private String getEmployeeID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", "EMPLOYEEMASTER");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    /**
     * To get customerTO, addressMap & phoneMap from the HashMap
     */
    private void getAllTOs() throws Exception {
        objEmployeeMasterTO = (EmployeeMasterTO) data.get("EMPLOYEEMASTER");
        addressMap = (HashMap) data.get("ADDRESS");
        if (data.containsKey("PromotionTOs")) {
            System.out.println("!@#!@#!@#PromotionTOs:" + data.get("PromotionTOs"));
        }
        if (data.containsKey("ADDRESSDELETED")) {
            deletedAddressMap = (HashMap) data.get("ADDRESSDELETED");
        }

        if (data.containsKey("ACADEMIC")) {
            academicMap = (LinkedHashMap) data.get("ACADEMIC");
        }
        if (data.containsKey("ACADEMICDELETED")) {
            deletedacademicMap = (HashMap) data.get("ACADEMICDELETED");
        }

        if (data.containsKey("TECHNICAL")) {
            technicalMap = (LinkedHashMap) data.get("TECHNICAL");
        }
        if (data.containsKey("TECHNICALDELETED")) {
            deletedTechnicalMap = (HashMap) data.get("TECHNICALDELETED");
        }
        if (data.containsKey("LANGUAGE")) {
            languageMap = (HashMap) data.get("LANGUAGE");
        }
        if (data.containsKey("LANGUAGEDELETED")) {
            deletedLanguageMap = (HashMap) data.get("LANGUAGEDELETED");
        }
        if (data.containsKey("DEPENDENT")) {
            dependentMap = (LinkedHashMap) data.get("DEPENDENT");
        }
        if (data.containsKey("DEPENDENTDELETED")) {
            deletedDependentMap = (HashMap) data.get("DEPENDENTDELETED");
        }

        if (data.containsKey("PASSPORT")) {
            objEmployeeMasterPassPortTO = (EmployeeMasterPassPortTO) data.get("PASSPORT");
        }

        if (data.containsKey("PHONE")) {
            phoneMap = (HashMap) data.get("PHONE");
        }
        if (data.containsKey("PHONEDELETED")) {
            deletedPhoneMap = (HashMap) data.get("PHONEDELETED");
        }

        if (data.containsKey("RELATIVE")) {
            relativeMap = (HashMap) data.get("RELATIVE");
        }
        if (data.containsKey("RELATIVEDELETED")) {
            deletedRelativeMap = (HashMap) data.get("RELATIVEDELETED");
        }

        if (data.containsKey("DIRECTOR")) {
            directorMap = (LinkedHashMap) data.get("DIRECTOR");
        }
        if (data.containsKey("OPRATIVE")) {
            oprativeMap = (HashMap) data.get("OPRATIVE");
        }
        if (data.containsKey("LOANS")) {
            loanMap = (HashMap) data.get("LOANS");
        }//DIRECTOREDELETED
        if (data.containsKey("DIRECTORDELETED")) {
            deletedDirectorMap = (HashMap) data.get("DIRECTORDELETED");
        }
        if (data.containsKey("LOANSDELETED")) {
            deletedloanMap = (HashMap) data.get("LOANSDELETED");
        }
        if (data.containsKey("OPRATIVEDELETED")) {
            deletedoprativeMap = (HashMap) data.get("OPRATIVEDELETED");
        }
        if (data.containsKey("PROMOTION")) {
            promotionMap = (HashMap) data.get("PROMOTION");
        }
        if (data.containsKey("PROMOTIONDELETED")) {
            deletedPromotionMap = (HashMap) data.get("PROMOTIONDELETED");
        }

    }

    /**
     * To insert address data
     */
    private void processAddressData(String command) throws Exception {
        if (deletedAddressMap != null && deletedAddressMap.size() > 0) {
            addressIterator = deletedAddressMap.keySet().iterator();
            for (int i = deletedAddressMap.size(); i > 0; i--) {
                key = (String) addressIterator.next();
                objEmployeeMasterAddressTO = (EmployeeMasterAddressTO) deletedAddressMap.get(key);
                logTO.setData(objEmployeeMasterAddressTO.toString());
                logTO.setPrimaryKey(objEmployeeMasterTO.getKeyData());
                logTO.setStatus(objEmployeeMasterTO.getCommand());
                sqlMap.executeUpdate("deleteEmployeeAddressTO", objEmployeeMasterAddressTO);
                logDAO.addToLog(logTO);
            }
            deletedAddressMap = null;
        }

        if (addressMap != null) {
            addressIterator = addressMap.keySet().iterator();
            for (int i = addressMap.size(); i > 0; i--) {
                addressKey = (String) addressIterator.next();
                objEmployeeMasterAddressTO = (EmployeeMasterAddressTO) addressMap.get(addressKey);
                logTO.setData(objEmployeeMasterAddressTO.toString());
                logTO.setPrimaryKey(objEmployeeMasterTO.getKeyData());
                logTO.setStatus(objEmployeeMasterTO.getCommand());
                //if EMPLOYEE Id exists, set to customerAddressTO objectobj
                System.out.println("customerAddressTO" + objEmployeeMasterAddressTO);
                objEmployeeMasterAddressTO.setStatusBy(objEmployeeMasterTO.getStatusBy());
                objEmployeeMasterAddressTO.setStatusDt(objEmployeeMasterTO.getStatusDt());
                if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    if (objEmployeeMasterAddressTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                        objEmployeeMasterAddressTO.setTxtEmpId(objEmployeeMasterTO.getSysId());
                        sqlMap.executeUpdate("insertEmployeeAddressTO", objEmployeeMasterAddressTO);
                    } else {
                        objEmployeeMasterAddressTO.setTxtEmpId(objEmployeeMasterAddressTO.getTxtEmpId());
                        sqlMap.executeUpdate("updateEmployeeMasterAddressTO", objEmployeeMasterAddressTO);
                    }

                } else if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    objEmployeeMasterAddressTO.setTxtEmpId(systemEmpID);
                    sqlMap.executeUpdate("insertEmployeeAddressTO", objEmployeeMasterAddressTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    objEmployeeMasterAddressTO.setTxtEmpId(systemEmpID);
                    objEmployeeMasterAddressTO.setStatus(objEmployeeMasterTO.getStatus());
                    objEmployeeMasterAddressTO.setStatusBy(objEmployeeMasterTO.getStatusBy());
                    sqlMap.executeUpdate("deleteEmployeeAddressTO", objEmployeeMasterAddressTO);
                }
                processPhoneData(command);
                logDAO.addToLog(logTO);
            }
            phoneMap = null;
            addressMap = null;
        }
    }

    private void processDirectorData(String command) throws Exception {
        if (deletedDirectorMap != null) {
            addressIterator = deletedDirectorMap.keySet().iterator();
            for (int i = deletedDirectorMap.size(); i > 0; i--) {
                System.out.println("entering deleted director map!!" + deletedDirectorMap);

                key = (String) addressIterator.next();
                objEmployeeRelativeDirectorTO = (EmployeeRelativeDirectorTO) deletedDirectorMap.get(key);
                System.out.println("relative director map!!" + objEmployeeRelativeDirectorTO);
                logTO.setData(objEmployeeRelativeDirectorTO.toString());
                logTO.setPrimaryKey(objEmployeeRelativeDirectorTO.getKeyData());
                logTO.setStatus(objEmployeeRelativeDirectorTO.getCommand());
                sqlMap.executeUpdate("deleteEmployeeDirectorWorkingTO", objEmployeeRelativeDirectorTO);
                logDAO.addToLog(logTO);
            }
            deletedDirectorMap = null;
        }
        if (directorMap != null) {
            addressIterator = directorMap.keySet().iterator();

            for (int i = directorMap.size(); i > 0; i--) {
                addressKey = (String) addressIterator.next();
                objEmployeeRelativeDirectorTO = (EmployeeRelativeDirectorTO) directorMap.get(addressKey);
                logTO.setData(objEmployeeRelativeDirectorTO.toString());
                logTO.setPrimaryKey(objEmployeeMasterTO.getKeyData());
                logTO.setStatus(objEmployeeMasterTO.getCommand());
                //if customer Id exists, set to customerAddressTO objectobj
                System.out.println("EMPLOYEE RELATIVE DIRECTOR TO!@!@" + objEmployeeRelativeDirectorTO);
                objEmployeeRelativeDirectorTO.setStatusBy(objEmployeeMasterTO.getStatusBy());
                objEmployeeRelativeDirectorTO.setStatusDt(objEmployeeMasterTO.getStatusDt());
                if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    if (objEmployeeRelativeDirectorTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                        objEmployeeRelativeDirectorTO.setSysId(objEmployeeMasterTO.getSysId());
                        sqlMap.executeUpdate("insertEmployeeDirectorWorkingTO", objEmployeeRelativeDirectorTO);
                    } else {
                        objEmployeeRelativeDirectorTO.setSysId(objEmployeeRelativeDirectorTO.getSysId());
                        sqlMap.executeUpdate("updateEmployeeDirectorWorkingTO", objEmployeeRelativeDirectorTO);
                    }

                } else if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    objEmployeeRelativeDirectorTO.setSysId(systemEmpID);
                    sqlMap.executeUpdate("insertEmployeeDirectorWorkingTO", objEmployeeRelativeDirectorTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    objEmployeeRelativeDirectorTO.setSysId(systemEmpID);
                    objEmployeeRelativeDirectorTO.setStatus(objEmployeeMasterTO.getStatus());
                    objEmployeeRelativeDirectorTO.setStatusBy(objEmployeeMasterTO.getStatusBy());
                    sqlMap.executeUpdate("deleteEmployeeDirectorWorkingTO", objEmployeeRelativeDirectorTO);

                }

                logDAO.addToLog(logTO);
            }
            directorMap = null;
        }

    }

    private void processAcademicData(String command) throws Exception {

        if (deletedacademicMap != null) {
            addressIterator = deletedacademicMap.keySet().iterator();
            for (int i = deletedacademicMap.size(); i > 0; i--) {
                key = (String) addressIterator.next();
                System.out.println("entering deleted ACADEMIC map!!" + deletedacademicMap);
                objEmployeeMasterEducationTO = (EmployeeMasterEducationTO) deletedacademicMap.get(key);
                logTO.setData(objEmployeeMasterEducationTO.toString());
                logTO.setPrimaryKey(objEmployeeMasterEducationTO.getKeyData());
                logTO.setStatus(objEmployeeMasterEducationTO.getCommand());
                sqlMap.executeUpdate("deleteEmployeeAcademicTO", objEmployeeMasterEducationTO);
                logDAO.addToLog(logTO);
            }
            deletedacademicMap = null;
        }
        if (academicMap != null) {
            addressIterator = academicMap.keySet().iterator();

            for (int i = academicMap.size(); i > 0; i--) {
                addressKey = (String) addressIterator.next();
                objEmployeeMasterEducationTO = (EmployeeMasterEducationTO) academicMap.get(addressKey);
                logTO.setData(objEmployeeMasterEducationTO.toString());
                logTO.setPrimaryKey(objEmployeeMasterTO.getKeyData());
                logTO.setStatus(objEmployeeMasterTO.getCommand());
                //if customer Id exists, set to customerAddressTO objectobj
                System.out.println("customerAddressTO" + objEmployeeMasterEducationTO);
                objEmployeeMasterEducationTO.setStatusBy(objEmployeeMasterTO.getStatusBy());
                objEmployeeMasterEducationTO.setStatusDt(objEmployeeMasterTO.getStatusDt());
                if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    if (objEmployeeMasterEducationTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                        objEmployeeMasterEducationTO.setTxtEmpId(objEmployeeMasterTO.getSysId());
                        sqlMap.executeUpdate("insertEmployeeAcademicTO", objEmployeeMasterEducationTO);
                    } else {
                        objEmployeeMasterEducationTO.setTxtEmpId(objEmployeeMasterEducationTO.getTxtEmpId());
                        sqlMap.executeUpdate("updateEmployeeMasterEducationTO", objEmployeeMasterEducationTO);
                    }

                } else if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    objEmployeeMasterEducationTO.setTxtEmpId(systemEmpID);
                    sqlMap.executeUpdate("insertEmployeeAcademicTO", objEmployeeMasterEducationTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    objEmployeeMasterEducationTO.setTxtEmpId(systemEmpID);
                    objEmployeeMasterEducationTO.setStatus(objEmployeeMasterTO.getStatus());
                    objEmployeeMasterEducationTO.setStatusBy(objEmployeeMasterTO.getStatusBy());
                    sqlMap.executeUpdate("deleteEmployeeAcademicTO", objEmployeeMasterEducationTO);

                }

                logDAO.addToLog(logTO);
            }
            academicMap = null;
        }

    }

    private void processTechnicalData(String command) throws Exception {
        if (deletedTechnicalMap != null) {
            addressIterator = deletedTechnicalMap.keySet().iterator();
            for (int i = deletedTechnicalMap.size(); i > 0; i--) {
                System.out.println("entering  deleted Techncal map!!!!");
                key = (String) addressIterator.next();
                objEmployeeMasterTechnicalTO = (EmployeeMasterTechnicalTO) deletedTechnicalMap.get(key);
                logTO.setData(objEmployeeMasterTechnicalTO.toString());
                logTO.setPrimaryKey(objEmployeeMasterTechnicalTO.getKeyData());
                logTO.setStatus(objEmployeeMasterTechnicalTO.getCommand());
                sqlMap.executeUpdate("deleteEmployeeTechnicalTO", objEmployeeMasterTechnicalTO);
                logDAO.addToLog(logTO);
            }
            deletedTechnicalMap = null;
        }
        if (technicalMap != null) {
            addressIterator = technicalMap.keySet().iterator();

            for (int i = technicalMap.size(); i > 0; i--) {
                addressKey = (String) addressIterator.next();
                objEmployeeMasterTechnicalTO = (EmployeeMasterTechnicalTO) technicalMap.get(addressKey);
                logTO.setData(objEmployeeMasterTechnicalTO.toString());
                logTO.setPrimaryKey(objEmployeeMasterTO.getKeyData());
                logTO.setStatus(objEmployeeMasterTO.getCommand());
                //if customer Id exists, set to customerAddressTO objectobj
                System.out.println("customerAddressTO" + objEmployeeMasterTechnicalTO);
                objEmployeeMasterTechnicalTO.setStatusBy(objEmployeeMasterTO.getStatusBy());
                objEmployeeMasterTechnicalTO.setStatusDt(objEmployeeMasterTO.getStatusDt());
                Date toDt = (Date) CurrDt.clone();
                if (objEmployeeMasterTechnicalTO.getTdtTechnicalYearOfPassing() != null) {
                    toDt.setDate(objEmployeeMasterTechnicalTO.getTdtTechnicalYearOfPassing().getDate());
                    toDt.setMonth(objEmployeeMasterTechnicalTO.getTdtTechnicalYearOfPassing().getMonth());
                    toDt.setYear(objEmployeeMasterTechnicalTO.getTdtTechnicalYearOfPassing().getYear());
                    objEmployeeMasterTechnicalTO.setTdtTechnicalYearOfPassing(toDt);
                }
                if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    if (objEmployeeMasterTechnicalTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                        objEmployeeMasterTechnicalTO.setTxtEmpId(objEmployeeMasterTO.getSysId());
                        sqlMap.executeUpdate("insertEmployeeTechnicalTO", objEmployeeMasterTechnicalTO);
                    } else {
                        objEmployeeMasterTechnicalTO.setTxtEmpId(objEmployeeMasterTechnicalTO.getTxtEmpId());
                        sqlMap.executeUpdate("updateEmployeeMasterTechnicalTO", objEmployeeMasterTechnicalTO);
                    }

                } else if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    objEmployeeMasterTechnicalTO.setTxtEmpId(CommonUtil.convertObjToStr(objEmployeeMasterTO.getSysId()));
                    sqlMap.executeUpdate("insertEmployeeTechnicalTO", objEmployeeMasterTechnicalTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    objEmployeeMasterTechnicalTO.setTxtEmpId(systemEmpID);
                    objEmployeeMasterTechnicalTO.setStatus(objEmployeeMasterTO.getStatus());
                    objEmployeeMasterTechnicalTO.setStatusBy(objEmployeeMasterTO.getStatusBy());
                    sqlMap.executeUpdate("deleteEmployeeTechnicalTO", objEmployeeMasterTechnicalTO);

                }
                logDAO.addToLog(logTO);
            }
            technicalMap = null;
        }

    }

    private void processLanguageData(String command) throws Exception {
        if (deletedLanguageMap != null) {
            addressIterator = deletedLanguageMap.keySet().iterator();
            for (int i = deletedLanguageMap.size(); i > 0; i--) {
                System.out.println("Entering deleted language map!!!");
                key = (String) addressIterator.next();
                objEmployeeMasterLanguageTO = (EmployeeMasterLanguageTO) deletedLanguageMap.get(key);
                logTO.setData(objEmployeeMasterLanguageTO.toString());
                logTO.setPrimaryKey(objEmployeeMasterLanguageTO.getKeyData());
                logTO.setStatus(objEmployeeMasterLanguageTO.getCommand());
                sqlMap.executeUpdate("deleteEmployeeLanguageTO", objEmployeeMasterLanguageTO);
                logDAO.addToLog(logTO);
            }
            deletedLanguageMap = null;
        }
        if (languageMap != null) {
            addressIterator = languageMap.keySet().iterator();

            for (int i = languageMap.size(); i > 0; i--) {
                addressKey = (String) addressIterator.next();
                objEmployeeMasterLanguageTO = (EmployeeMasterLanguageTO) languageMap.get(addressKey);
                logTO.setData(objEmployeeMasterLanguageTO.toString());
                logTO.setPrimaryKey(objEmployeeMasterTO.getKeyData());
                logTO.setStatus(objEmployeeMasterTO.getCommand());
                //if customer Id exists, set to customerAddressTO objectobj
                System.out.println("customerAddressTO" + objEmployeeMasterLanguageTO);
                objEmployeeMasterLanguageTO.setStatusBy(objEmployeeMasterTO.getStatusBy());
                objEmployeeMasterLanguageTO.setStatusDt(objEmployeeMasterTO.getStatusDt());
                if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    if (objEmployeeMasterLanguageTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                        objEmployeeMasterLanguageTO.setTxtEmpId(objEmployeeMasterTO.getSysId());
                        sqlMap.executeUpdate("insertEmployeeLanguageTO", objEmployeeMasterLanguageTO);
                    } else {
                        objEmployeeMasterLanguageTO.setTxtEmpId(objEmployeeMasterLanguageTO.getTxtEmpId());
                        sqlMap.executeUpdate("updateEmployeeMasterLanguageTO", objEmployeeMasterLanguageTO);
                    }

                } else if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    objEmployeeMasterLanguageTO.setTxtEmpId(objEmployeeMasterTO.getSysId());
                    sqlMap.executeUpdate("insertEmployeeLanguageTO", objEmployeeMasterLanguageTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    objEmployeeMasterLanguageTO.setTxtEmpId(systemEmpID);
                    objEmployeeMasterLanguageTO.setStatus(objEmployeeMasterTO.getStatus());
                    objEmployeeMasterLanguageTO.setStatusBy(objEmployeeMasterTO.getStatusBy());
                    sqlMap.executeUpdate("deleteEmployeeLanguageTO", objEmployeeMasterLanguageTO);

                }
                logDAO.addToLog(logTO);
                //            processPhoneData(command);
            }
            languageMap = null;
        }

    }

    private void processRelativeData(String command) throws Exception {
        if (deletedRelativeMap != null) {
            addressIterator = deletedRelativeMap.keySet().iterator();
            for (int i = deletedRelativeMap.size(); i > 0; i--) {
                System.out.println("Entering deleted relative map!!");
                key = (String) addressIterator.next();
                objEmployeeRelativeWorkingTO = (EmployeeRelativeWorkingTO) deletedRelativeMap.get(key);
                logTO.setData(objEmployeeRelativeWorkingTO.toString());
                logTO.setPrimaryKey(objEmployeeRelativeWorkingTO.getKeyData());
                logTO.setStatus(objEmployeeRelativeWorkingTO.getCommand());
                sqlMap.executeUpdate("deleteEmployeeRelativeWorkingTO", objEmployeeRelativeWorkingTO);
                logDAO.addToLog(logTO);
            }
            deletedRelativeMap = null;
        }

        if (relativeMap != null) {
            addressIterator = relativeMap.keySet().iterator();

            for (int i = relativeMap.size(); i > 0; i--) {
                addressKey = (String) addressIterator.next();
                objEmployeeRelativeWorkingTO = (EmployeeRelativeWorkingTO) relativeMap.get(addressKey);
                logTO.setData(objEmployeeRelativeWorkingTO.toString());
                logTO.setPrimaryKey(objEmployeeMasterTO.getKeyData());
                logTO.setStatus(objEmployeeMasterTO.getCommand());
                //if customer Id exists, set to customerAddressTO objectobj
                System.out.println("objEmployeeRelativeWorkingTO" + objEmployeeRelativeWorkingTO);
                objEmployeeRelativeWorkingTO.setStatusBy(objEmployeeMasterTO.getStatusBy());
                objEmployeeRelativeWorkingTO.setStatusDt(objEmployeeMasterTO.getStatusDt());
                if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    if (objEmployeeRelativeWorkingTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                        System.out.println("objEmployeeMasterTO.getSysId()" + objEmployeeMasterTO.getSysId());

                        objEmployeeRelativeWorkingTO.setSysId(objEmployeeMasterTO.getSysId());
                        sqlMap.executeUpdate("insertEmployeeRelativeWorkingTO", objEmployeeRelativeWorkingTO);
                    } else {
                        objEmployeeRelativeWorkingTO.setSysId(objEmployeeRelativeWorkingTO.getSysId());
                        sqlMap.executeUpdate("updateEmployeeRelativeWorkingTO", objEmployeeRelativeWorkingTO);
                    }

                } else if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    objEmployeeRelativeWorkingTO.setSysId(objEmployeeMasterTO.getSysId());
                    sqlMap.executeUpdate("insertEmployeeRelativeWorkingTO", objEmployeeRelativeWorkingTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    objEmployeeRelativeWorkingTO.setSysId(systemEmpID);
                    objEmployeeRelativeWorkingTO.setStatus(objEmployeeMasterTO.getStatus());
                    objEmployeeRelativeWorkingTO.setStatusBy(objEmployeeMasterTO.getStatusBy());
                    sqlMap.executeUpdate("deleteEmployeeRelativeWorkingTO", objEmployeeRelativeWorkingTO);

                }
                logDAO.addToLog(logTO);
                //            processPhoneData(command);
            }
            relativeMap = null;
        }

    }

    private void processPromotionData(String command) throws Exception {
        System.out.println("ASDASDASD@!#!@#inside promotiondata:" + promotionMap);

        if (deletedPromotionMap != null) {
            addressIterator = deletedPromotionMap.keySet().iterator();
            for (int i = deletedPromotionMap.size(); i > 0; i--) {
                System.out.println("Entering deleted promotion map!!");
                key = (String) addressIterator.next();
                objPromotionTO = (PromotionTO) deletedPromotionMap.get(key);
                logTO.setData(objPromotionTO.toString());
                //                logTO.setPrimaryKey(objPromotionTO.getKeyData());
                logTO.setStatus(objPromotionTO.getCommand());
                sqlMap.executeUpdate("deletePromotionTO", objPromotionTO);
                logDAO.addToLog(logTO);
            }
            deletedPromotionMap = null;
        }

        if (promotionMap != null) {
            addressIterator = promotionMap.keySet().iterator();

            for (int i = promotionMap.size(); i > 0; i--) {
                addressKey = (String) addressIterator.next();
                objPromotionTO = (PromotionTO) promotionMap.get(addressKey);
                logTO.setData(objPromotionTO.toString());
                logTO.setPrimaryKey(objEmployeeMasterTO.getKeyData());
                logTO.setStatus(objEmployeeMasterTO.getCommand());
                //if customer Id exists, set to customerAddressTO objectobj
                System.out.println("objPromotionTO" + objPromotionTO);
                objPromotionTO.setStatusBy(objEmployeeMasterTO.getStatusBy());
                objPromotionTO.setStatusDt(objEmployeeMasterTO.getStatusDt());
                if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    if (objPromotionTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                        System.out.println("objEmployeeMasterTO.getSysId()" + objEmployeeMasterTO.getSysId());

                        objPromotionTO.setEmpId(objEmployeeMasterTO.getSysId());
                        sqlMap.executeUpdate("insertPromotionTO", objPromotionTO);
                    } else {
                        objPromotionTO.setEmpId(objEmployeeMasterTO.getSysId());
                        sqlMap.executeUpdate("updatePromotionTO", objPromotionTO);
                    }

                } else if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    objPromotionTO.setEmpId(objEmployeeMasterTO.getSysId());
                    sqlMap.executeUpdate("insertPromotionTO", objPromotionTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    objPromotionTO.setEmpId(systemEmpID);
                    objPromotionTO.setStatus(objEmployeeMasterTO.getStatus());
                    objPromotionTO.setStatusBy(objEmployeeMasterTO.getStatusBy());
                    sqlMap.executeUpdate("deletePromotionTO", objPromotionTO);

                }
                logDAO.addToLog(logTO);
                //            processPhoneData(command);
            }
            promotionMap = null;
        }
    }

    private void processDependentData(String command) throws Exception {
        if (deletedDependentMap != null) {
            addressIterator = deletedDependentMap.keySet().iterator();
            for (int i = deletedDependentMap.size(); i > 0; i--) {

                key = (String) addressIterator.next();
                System.out.println("Employee DELETED 1" + objEmployeeMasterTO + "map" + deletedDependentMap);
                objEmployeeMasterDependendTO = (EmployeeMasterDependendTO) deletedDependentMap.get(key);
                logTO.setData(objEmployeeMasterDependendTO.toString());
                logTO.setPrimaryKey(objEmployeeMasterDependendTO.getKeyData());
                logTO.setStatus(objEmployeeMasterDependendTO.getCommand());
                //                objEmployeeMasterDependendTO.setTxtEmpId(systemEmpID);
                //                objEmployeeMasterDependendTO.setStatus(objEmployeeMasterTO.getStatus());
                //                objEmployeeMasterDependendTO.setStatusBy(objEmployeeMasterTO.getStatusBy());
                System.out.println("Entering deleted dependent map!!!" + objEmployeeMasterDependendTO);
                sqlMap.executeUpdate("deleteEmployeeDependentTO", objEmployeeMasterDependendTO);
                logDAO.addToLog(logTO);
            }
            deletedDependentMap = null;
        }

        if (dependentMap != null) {
            addressIterator = dependentMap.keySet().iterator();
            objEmployeeMasterDependendTO = new EmployeeMasterDependendTO();
            for (int i = dependentMap.size(); i > 0; i--) {
                addressKey = (String) addressIterator.next();
                objEmployeeMasterDependendTO = (EmployeeMasterDependendTO) dependentMap.get(addressKey);
                logTO.setData(objEmployeeMasterDependendTO.toString());
                logTO.setPrimaryKey(objEmployeeMasterTO.getKeyData());
                logTO.setStatus(objEmployeeMasterTO.getCommand());
                //if customer Id exists, set to customerAddressTO objectobj
                System.out.println("customerAddressTO" + objEmployeeMasterDependendTO);
                objEmployeeMasterDependendTO.setStatusBy(objEmployeeMasterTO.getStatusBy());
                objEmployeeMasterDependendTO.setStatusDt(objEmployeeMasterTO.getStatusDt());
                Date dateOfJoin = (Date) objEmployeeMasterDependendTO.getTdtDepDateOfBirth();
                if (dateOfJoin != null) {
                    Date dJoin = (Date) CurrDt.clone();
                    dJoin.setDate(dateOfJoin.getDate());
                    dJoin.setMonth(dateOfJoin.getMonth());
                    dJoin.setYear(dateOfJoin.getYear());
                    objEmployeeMasterDependendTO.setTdtDepDateOfBirth(dJoin);
                }
                if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    if (objEmployeeMasterDependendTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                        objEmployeeMasterDependendTO.setTxtEmpId(objEmployeeMasterTO.getSysId());
                        sqlMap.executeUpdate("insertEmployeeDependentTO", objEmployeeMasterDependendTO);
                    } else {
                        System.out.println("Employee DependentTO inside Update" + objEmployeeMasterDependendTO);
                        objEmployeeMasterDependendTO.setTxtEmpId(objEmployeeMasterDependendTO.getTxtEmpId());
                        sqlMap.executeUpdate("updateEmployeeMasterDependentTO", objEmployeeMasterDependendTO);
                    }

                } else if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    objEmployeeMasterDependendTO.setTxtEmpId(objEmployeeMasterTO.getSysId());
                    sqlMap.executeUpdate("insertEmployeeDependentTO", objEmployeeMasterDependendTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    objEmployeeMasterDependendTO.setTxtEmpId(systemEmpID);
                    objEmployeeMasterDependendTO.setStatus(objEmployeeMasterTO.getStatus());
                    objEmployeeMasterDependendTO.setStatusBy(objEmployeeMasterTO.getStatusBy());
                    sqlMap.executeUpdate("deleteEmployeeDependentTO", objEmployeeMasterDependendTO);

                }

                logDAO.addToLog(logTO);
                //            processPhoneData(command);
            }
            dependentMap = null;
        }

    }

    /**
     * To make data object null
     */
    private void makeDataNull() {
        data = null;
    }

    /**
     * To make used objects null
     */
    private void makeNull() {
        //        customerTO = null;
        addressMap = null;
        academicMap = null;
        technicalMap = null;
        languageMap = null;
        dependentMap = null;
        relativeMap = null;
        directorMap = null;
        oprativeMap = null;
        objEmployeeRelativeDirectorTO = null;
        objEmployeeMasterAddressTO = null;
        objEmployeeMasterDependendTO = null;
        objEmployeeMasterEducationTO = null;
        objEmployeeMasterLanguageTO = null;
        objEmployeeMasterTO = null;
        objEmployeeMasterTechnicalTO = null;
        objEmployeeMasterPassPortTO = null;
        objEmployeeRelativeWorkingTO = null;
        objEmployeeMasterPhoneTO = null;
        objEmployeeOprativeTO = null;
        objEmployeeTermLoanTO = null;
        key = null;
        addressIterator = null;
        deletedAddressMap = null;
        deletedDependentMap = null;
        deletedLanguageMap = null;
        deletedacademicMap = null;
        deletedTechnicalMap = null;
        deletedRelativeMap = null;
        deletedDirectorMap = null;
        deletedoprativeMap = null;

    }

    /**
     * To Update customer data
     */
    private void updateData() throws Exception {

        getAllTOs();
        systemEmpID = objEmployeeMasterTO.getSysId();
        //        objEmployeePresentDetailsTO.setStatusBy(logTO.getUserId());
        //        objEmployeePresentDetailsTO.setStatusDt(objEmployeeMasterTO.getStatusDt());
        //        objEmployeePresentDetailsTO.setStatus(objEmployeeMasterTO.getStatus());
        tableName = "EMPLOYEE_MASTER";
        tableCondition = "SYS_EMPID";
        isMore = 0;
        storePhotoSign();
        logTO.setData("Photo has been Uploaded");
        logTO.setPrimaryKey(objEmployeeMasterTO.getKeyData());
        logTO.setStatus(objEmployeeMasterTO.getCommand());
        logDAO.addToLog(logTO);
        logTO.setData(objEmployeeMasterTO.toString());
        logTO.setPrimaryKey(objEmployeeMasterTO.getKeyData());
        logTO.setStatus(objEmployeeMasterTO.getCommand());
        sqlMap.executeUpdate("updateEmployeeMasterTO", objEmployeeMasterTO);
        if (objEmployeePresentDetailsTO != null) {
            objEmployeePresentDetailsTO.setSysId(systemEmpID);
            objEmployeePresentDetailsTO.setStatusBy(objEmployeeMasterTO.getStatusBy());
            objEmployeePresentDetailsTO.setStatusDt(objEmployeeMasterTO.getStatusDt());
            objEmployeePresentDetailsTO.setStatus(objEmployeeMasterTO.getStatus());
            sqlMap.executeUpdate("updateEmployeePresentDetailsTO", objEmployeePresentDetailsTO);
        }
        processPromotionData(objEmployeeMasterTO.getCommand());
        processAddressData(objEmployeeMasterTO.getCommand());
        //        processPhoneData(objEmployeeMasterTO.getCommand());
        processAcademicData(objEmployeeMasterTO.getCommand());
        processTechnicalData(objEmployeeMasterTO.getCommand());
        processLanguageData(objEmployeeMasterTO.getCommand());
        processDependentData(objEmployeeMasterTO.getCommand());
        processRelativeData(objEmployeeMasterTO.getCommand());
        processDirectorData(objEmployeeMasterTO.getCommand());
        //processLoanData(objEmployeeMasterTO.getCommand());
        processOprativeData(objEmployeeMasterTO.getCommand());
        final String USERID = logTO.getBranchId();

        logDAO.addToLog(logTO);
        if (objEmployeeMasterPassPortTO != null) {
            HashMap map = new HashMap();
            map.put("SYS_EMP_ID", objEmployeeMasterPassPortTO.getTxtEmpId());
            //if customer Id exists, set to customerAddressTO object
            objEmployeeMasterPassPortTO.setTxtEmpId(objEmployeeMasterTO.getSysId());
            logTO.setData(objEmployeeMasterPassPortTO.toString());
            logTO.setPrimaryKey(objEmployeeMasterPassPortTO.getKeyData());
            objEmployeeMasterPassPortTO.setCommand(objEmployeeMasterTO.getCommand());
            logTO.setStatus(objEmployeeMasterPassPortTO.getCommand());
            objEmployeeMasterPassPortTO.setStatus(objEmployeeMasterTO.getStatus());
            if (objEmployeeMasterPassPortTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                sqlMap.executeUpdate("insertEmployeeMasterPassPortTO", objEmployeeMasterPassPortTO);
            } else {
                List lst = sqlMap.executeQueryForList("checkForLengthEmployeePassPort", objEmployeeMasterPassPortTO);
                System.out.println("$$$$lst" + lst);
                if (lst.size() > 0) {
                    sqlMap.executeUpdate("updateEmployeeMasterPassPortTO", objEmployeeMasterPassPortTO);
                } else {
                    sqlMap.executeUpdate("insertEmployeeMasterPassPortTO", objEmployeeMasterPassPortTO);
                }
            }
            logDAO.addToLog(logTO);
        }
        makeDataNull();
        makeNull();

    }

    /**
     * To delete employee data
     */
    private void deleteData() throws Exception {
        //        try {
        getAllTOs();
        systemEmpID = objEmployeeMasterTO.getSysId();
        objEmployeePresentDetailsTO.setStatusBy(logTO.getUserId());
        System.out.println("@@@@data" + data);
        objEmployeeMasterTO = (EmployeeMasterTO) data.get("EMPLOYEEMASTER");
        logTO.setData("Photo has been Uploaded");
        logTO.setData(objEmployeeMasterTO.toString());
        logTO.setPrimaryKey(objEmployeeMasterTO.getKeyData());
        logTO.setStatus(objEmployeeMasterTO.getCommand());
        logDAO.addToLog(logTO);
        logTO.setData(objEmployeeMasterTO.toString());
        logTO.setPrimaryKey(objEmployeeMasterTO.getKeyData());
        logTO.setStatus(objEmployeeMasterTO.getCommand());
        sqlMap.executeUpdate("updateEmployeeMasterTO", objEmployeeMasterTO);
        //ADDED 01-12-2010
        processPromotionData(objEmployeeMasterTO.getCommand());
        processAddressData(objEmployeeMasterTO.getCommand());
        processAcademicData(objEmployeeMasterTO.getCommand());
        processTechnicalData(objEmployeeMasterTO.getCommand());
        processLanguageData(objEmployeeMasterTO.getCommand());
        processDependentData(objEmployeeMasterTO.getCommand());
        processRelativeData(objEmployeeMasterTO.getCommand());
        processDirectorData(objEmployeeMasterTO.getCommand());
        //processLoanData(objEmployeeMasterTO.getCommand());
        //processOprativeData(objEmployeeMasterTO.getCommand());
        if (objEmployeePresentDetailsTO != null) {
            if (systemEmpID != null) {
                objEmployeePresentDetailsTO.setSysId(systemEmpID);
            }
            logTO.setData(objEmployeePresentDetailsTO.toString());
            logTO.setPrimaryKey(objEmployeePresentDetailsTO.getKeyData());
            logTO.setStatus(objEmployeePresentDetailsTO.getCommand());
            objEmployeePresentDetailsTO.setStatusDt(objEmployeeMasterTO.getStatusDt());
            objEmployeePresentDetailsTO.setStatus(objEmployeeMasterTO.getStatus());
            sqlMap.executeUpdate("deleteEmployeePresentDetailsTO", objEmployeePresentDetailsTO);
        }
        logDAO.addToLog(logTO);
        if (objEmployeeMasterPassPortTO != null) {
            if (systemEmpID != null) {
                objEmployeeMasterPassPortTO.setTxtEmpId(systemEmpID);
            }
            logTO.setData(objEmployeeMasterPassPortTO.toString());
            logTO.setPrimaryKey(objEmployeeMasterPassPortTO.getKeyData());
            logTO.setStatus(objEmployeeMasterPassPortTO.getCommand());
            objEmployeeMasterPassPortTO.setStatus(objEmployeeMasterTO.getStatus());
            sqlMap.executeUpdate("deleteEmployeeMasterPassPortTO", objEmployeeMasterPassPortTO);
        }
        logDAO.addToLog(logTO);

        makeDataNull();
        makeNull();
    }

    /**
     * Standard method for insert, update & delete operations
     */
    public HashMap execute(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        CurrDt = ServerUtil.getCurrentDate(_branchCode);
        System.out.println("#### DAO execute obj map : " + obj);
        HashMap resultMap = new HashMap();
        try {
            EmployeeMasterTO objEmployeeMasterTO = (EmployeeMasterTO) obj.get("EMPLOYEEMASTER");
            if (obj.containsKey("PASSPORT")) {
                objEmployeeMasterPassPortTO = (EmployeeMasterPassPortTO) obj.get("PASSPORT");
            }
            if (obj.containsKey("EMPLOYEEPRESENTDETAILS")) {
                objEmployeePresentDetailsTO = (EmployeePresentDetailsTO) obj.get("EMPLOYEEPRESENTDETAILS");
            }
            logDAO = new LogDAO();
            logTO = new LogTO();
            logTO.setUserId(CommonUtil.convertObjToStr(obj.get(CommonConstants.USER_ID)));
            logTO.setBranchId(CommonUtil.convertObjToStr(obj.get(CommonConstants.SELECTED_BRANCH_ID)));
            logTO.setInitiatedBranch(CommonUtil.convertObjToStr(obj.get(CommonConstants.BRANCH_ID)));
            logTO.setIpAddr(CommonUtil.convertObjToStr(obj.get(CommonConstants.IP_ADDR)));
            logTO.setModule(CommonUtil.convertObjToStr(obj.get(CommonConstants.MODULE)));
            logTO.setScreen(CommonUtil.convertObjToStr(obj.get(CommonConstants.SCREEN)));
            photoByteArray = null;
            signByteArray = null;
            photoByteArray = (byte[]) obj.get("PHOTO");
            signByteArray = (byte[]) obj.get("SIGN");

            data = obj;
            //Start the transaction
            sqlMap.startTransaction();
            System.out.println("objEmployeeMasterTO.getCommand()--------------->" + objEmployeeMasterTO.getCommand());
            cmd = objEmployeeMasterTO.getCommand();
            System.out.println("cmd--------------->" + cmd);

            if (objEmployeeMasterTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData();
            } else if (objEmployeeMasterTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData();
            } else if (objEmployeeMasterTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                //updateData();
                deleteData();
            } else {
                sqlMap.rollbackTransaction();
                throw new NoCommandException();
            }
            resultMap.put("SYS_EMPID", objEmployeeMasterTO.getTxtEmpId());
            //Commit the transaction
            sqlMap.commitTransaction();
            if (cmd.equals(CommonConstants.TOSTATUS_INSERT)) {

                tableName = "EMPLOYEE_MASTER";
                tableCondition = "SYS_EMPID";
                isMore = 0;
                storePhotoSign();

            }
        } catch (Exception ex) {
            sqlMap.rollbackTransaction();
            throw ex;
        }
        return resultMap;
    }

    /**
     * Standard method to get data for a particular customer based on customer
     * id
     */
    public HashMap executeQuery(HashMap condition) throws Exception {
        System.out.println("###condition" + condition);
        _branchCode = (String) condition.get(CommonConstants.BRANCH_ID);
        CurrDt = ServerUtil.getCurrentDate(_branchCode);
        whereConditions = condition;
        if (condition.containsKey(CommonConstants.MAP_WHERE)) {
            whereCondition = (String) condition.get(CommonConstants.MAP_WHERE);
        }
        System.out.println("###whereconditions" + whereConditions);
        if (!condition.containsKey("PHOTOSIGNONLY") && !condition.containsKey("AUTH_DATA")) {
            getEmployeeMasterData();
            getAddressData();
            getPassPortData();
            getDependentData();
            getLanguageData();
            getAcadmicData();
            getTechnicalData();
            getPhoneData();
            getRelativeData();
            getDirectorData();
            getPresentDetails();
            getLoanDetails();
            getOprativeDetails();
            getPromotionData();
            makeNull();
            makeQueryNull();
        } else if (condition.containsKey("AUTH_DATA")) {
            sqlMap.executeUpdate("authorizeIndEmployee", condition);
            if (condition.containsKey("PROMOTION")) {
                if (condition.get("STATUS").equals("AUTHORIZED")) {
                    sqlMap.executeUpdate("updateEmpPresentDetPromotion", condition);
                    String incrementID = getIncrementIDGeneration();
                    System.out.println("@#$@#$@#incrementID:" + incrementID);
                    condition.put("INCREMENT_ID", incrementID);
                    sqlMap.executeUpdate("insertEmployeeBasic", condition);
                }
                sqlMap.executeUpdate("authorizeIndPromotion", condition);
                System.out.println("inside authorizing promotion:  :p" + condition);
            } else {
                if (condition.get("STATUS").equals("AUTHORIZED")) {
                    String incrementID = getIncrementIDGeneration();
                    System.out.println("@#$@#$@#incrementID:" + incrementID);
                    condition.put("INCREMENT_ID", incrementID);
                    sqlMap.executeUpdate("insertEmployeeBasic", condition);
                }
            }
        } else {
            data = new HashMap();
            tableName = "EMPLOYEE_MASTER";
            tableCondition = "SYS_EMPID";
            //            getPhotoSign();
        }
        return data;
    }

    private String getIncrementIDGeneration() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "INCREMENT_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void getPhoneData() throws Exception {
        list = (List) sqlMap.executeQueryForList("getSelectEmployeeMasterPhoneTO", whereConditions);
        System.out.println("#$%#$# Phone List : " + list);
        if (list.size() > 0) {
            phoneMap = new HashMap();
            String addrType;
            HashMap tmpPhoneMap;
            for (int i = list.size(), j = 0; i > 0; i--, j++) {
                objEmployeeMasterPhoneTO = (EmployeeMasterPhoneTO) list.get(j);
                System.out.println("#$%#$# customerPhoneTO : " + objEmployeeMasterPhoneTO);
                addrType = objEmployeeMasterPhoneTO.getAddrType();
                if (phoneMap.containsKey(addrType)) {
                    tmpPhoneMap = (HashMap) phoneMap.get(addrType);
                } else {
                    tmpPhoneMap = new HashMap();
                }
                tmpPhoneMap.put(objEmployeeMasterPhoneTO.getPhoneId(), objEmployeeMasterPhoneTO);
                phoneMap.put(addrType, tmpPhoneMap);
            }
            tmpPhoneMap = null;
            data.put("PHONE", phoneMap);
        }
    }

    private void getEmployeeMasterData() throws Exception {
        String introType = null;
        EmployeeMasterTO emp = null;
        list = (List) sqlMap.executeQueryForList("getSelectEmployeeMasterTO", whereConditions);
        if (list.size() > 0) {
            data = new HashMap();
            data.put("EMPLOYEEMASTER", list.get(0));
            emp = (EmployeeMasterTO) list.get(0);
        }

        /*
         * get the introduction type and based on that, get the data from
         * particular table
         */
        String where = CommonUtil.convertObjToStr(((EmployeeMasterTO) list.get(0)).getTxtEmpId());
        System.out.println("@@@where" + where);
        mapAuthPersonsTO = new HashMap();
        HashMap hashAuthPersons = new HashMap();
        HashMap rowMap;
        HashMap tempMap;
        String mainCustID = whereCondition;
        String custType = "";
        if (emp != null) {
            tableName = "EMPLOYEE_MASTER";
            tableCondition = "SYS_EMPID";
            getPhotoSign();
        }
        emp = null;
        System.out.println("##### getEmplyeeData() data : " + data);
        whereCondition = mainCustID;
        hashAuthPersons = null;
        rowMap = null;
        tempMap = null;
        mapPhotoSign = null;
    }

    private void getPhotoSign() throws Exception {
        try {
            mapPhotoSign = new HashMap();
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            setDriver();
            conn = DriverManager.getConnection(dataBaseURL, userName, passWord);
            // @machineName:port:SID,   userid,  password
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            String st = "select " + tableCondition + ", PHOTO_FILE, SIGNATURE_FILE from " + tableName + " where " + tableCondition + " = '" + whereCondition + "'";
            if (tableName.equals("CORP_AUTH_CUST")) {
                st = st + addCondition;
            }
            System.out.println("#### statement to be executed : " + st);
            rset = stmt.executeQuery(st);
            System.out.println("#### rset.getRow() " + rset.getRow());
            if (rset != null) {
                System.out.println("#### rset not null");
                while (rset.next()) {
                    System.out.println("#### rset in while");
                    System.out.println("#### Cust ID : " + ((OracleResultSet) rset).getString(1));   // Print col 1
                    BLOB oracleBlob = ((OracleResultSet) rset).getBLOB(2);
                    // get the length of the blob
                    long length = oracleBlob.length();

                    // print the length of the blob
                    System.out.println("photo blob length " + length);
                    try {
                        File f1 = null;
                        FileOutputStream out = null;
                        InputStream in = null;
                        java.net.URI serverURI = null;
                        int fileLength = 0;
                        byte[] blobBytes;
                        int len = 0;
                        if (length != 0) {
                            System.out.println("#### " + SERVER_ADDRESS + "customer/" + whereCondition + "_photo.jpg");
                            serverURI = new java.net.URI(SERVER_ADDRESS + "customer/" + whereCondition + "_photo.jpg");
                            f1 = new File(ServerConstants.SERVER_PATH + "\\customer\\" + whereCondition + "_photo.jpg");
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
                            //                mapPhotoSign.put("PHOTO",f1.getAbsolutePath());
                            //                mapPhotoSign.put("PHOTO","http://"+serverURI.getHost()+":"+serverURI.getPort()+serverURI.getPath());
                            mapPhotoSign.put("PHOTO", blobBytes);
                        }
                        oracleBlob = ((OracleResultSet) rset).getBLOB(3);
                        // get the length of the blob
                        length = oracleBlob.length();
                        if (length != 0) {
                            // print the length of the blob
                            System.out.println("sign blob length " + length);
                            serverURI = new java.net.URI(SERVER_ADDRESS + "customer/" + whereCondition + "_sign.jpg");
                            f1 = new File(ServerConstants.SERVER_PATH + "\\customer\\" + whereCondition + "_sign.jpg");
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
                            //                mapPhotoSign.put("SIGN",f1.getAbsolutePath());
                            //                mapPhotoSign.put("SIGN","http://"+serverURI.getHost()+":"+serverURI.getPort()+serverURI.getPath());
                            mapPhotoSign.put("SIGN", blobBytes);
                            System.out.println("##### PHOTO SIGN MAP : " + mapPhotoSign);
                            data.put("PHOTOSIGN", mapPhotoSign);
                        }
                        //                mapPhotoSign = null;
                        f1 = null;
                        out = null;
                        in = null;
                        oracleBlob = null;
                    } catch (Exception ioe) {
                        System.out.println("##### Exception : " + ioe);
                    }
                }
                addCondition = "";
                conn.close();
                stmt.close();
                rset.close();
            }
        } catch (java.sql.SQLException se) {
            addCondition = "";
            conn.close();
            stmt.close();
            rset.close();
            System.out.println("SQL Exception : " + se);
        }
    }

    private void setDriver() throws Exception {
        java.util.Properties serverProperties = new java.util.Properties();
        try {
            Dummy cons = new Dummy();
            serverProperties.load(cons.getClass().getResourceAsStream("/com/see/truetransact/serverutil/SqlMapConfig.properties"));
            dataBaseURL = serverProperties.getProperty("url");
            userName = serverProperties.getProperty("username");
            passWord = serverProperties.getProperty("password");
            serverProperties = new java.util.Properties();
            serverProperties.load(cons.getClass().getResourceAsStream("/com/see/truetransact/commonutil/TT.properties"));
            SERVER_ADDRESS = "http://" + CommonConstants.SERVER_IP_ADDR + ":" + serverProperties.getProperty("HTTP_PORT") + "/" + serverProperties.getProperty("HTTP_CONTEXT");
            System.out.println("#### SERVER_ADDRESS : " + SERVER_ADDRESS);
            serverProperties = null;
            cons = null;
        } catch (Exception ex) {
        }
    }

    private void getPassPortData() throws Exception {
        list = (List) sqlMap.executeQueryForList("getSelectEmployeeMasterPassPortTO", whereConditions);
        objEmployeeMasterPassPortTO = new EmployeeMasterPassPortTO();
        if (list.size() > 0) {
            objEmployeeMasterPassPortTO = (EmployeeMasterPassPortTO) list.get(0);
            data.put("PASSPORT", objEmployeeMasterPassPortTO);
        }
    }

    private void getPresentDetails() throws Exception {
//        CurrDt = ServerUtil.getCurrentDate(_branchCode);
        whereConditions.put("CURRENT_DT", CurrDt);
        System.out.println("#@$@#$whereConditions:" + whereConditions);
        list = (List) sqlMap.executeQueryForList("getSelectEmployeePresentDetailsTO", whereConditions);
        objEmployeePresentDetailsTO = new EmployeePresentDetailsTO();
        if (list.size() > 0) {
            objEmployeePresentDetailsTO = (EmployeePresentDetailsTO) list.get(0);
            data.put("EMPLOYEEPRESENTDETAILS", objEmployeePresentDetailsTO);
        }
    }

    private void getLoanDetails() throws Exception {
        list = (List) sqlMap.executeQueryForList("getSelectEmployeeLoanDetailsTO", whereConditions);
        if (list.size() > 0) {
            loanMap = new HashMap();
            for (int i = list.size(), j = 0; i > 0; i--, j++) {
                loanMap.put(((EmployeeTermLoanTO) list.get(j)).getCboEmployeeLoanType(), list.get(j));
            }
            data.put("EMPLOYEELOANDETAILS", loanMap);
        }
    }

    private void getOprativeDetails() throws Exception {
        list = (List) sqlMap.executeQueryForList("getSelectEmployeeOprativeTO", whereConditions);
        if (list.size() > 0) {
            oprativeMap = new HashMap();
            for (int i = list.size(), j = 0; i > 0; i--, j++) {
                oprativeMap.put(((EmployeeOprativeTO) list.get(j)).getOperativeId(), list.get(j));
            }
            data.put("OPRATIVE", oprativeMap);
        }
    }

    private void getAddressData() throws Exception {
        list = (List) sqlMap.executeQueryForList("getSelectEmployeeMasterAddressTO", whereConditions);
        if (list.size() > 0) {
            addressMap = new HashMap();
            for (int i = list.size(), j = 0; i > 0; i--, j++) {
                addressMap.put(((EmployeeMasterAddressTO) list.get(j)).getCboEmpAddressType(), list.get(j));
            }
            data.put("ADDRESS", addressMap);
        }
    }

    private void getDirectorData() throws Exception {
        list = (List) sqlMap.executeQueryForList("getSelectEmployeeDirectorWorkingTO", whereConditions);
        if (list.size() > 0) {
            directorMap = new LinkedHashMap();
            for (int i = 0; i < list.size(); i++) {
                directorMap.put(((EmployeeRelativeDirectorTO) list.get(i)).getDirectorID(), list.get(i));
            }
            System.out.println("#$#$#$ directorMap :" + directorMap);
            data.put("DIRECTOR", directorMap);
        }
    }

    private void getRelativeData() throws Exception {
        list = (List) sqlMap.executeQueryForList("getSelectEmployeeRelativeWorkingTO", whereConditions);
        if (list.size() > 0) {
            relativeMap = new HashMap();
            for (int i = list.size(), j = 0; i > 0; i--, j++) {
                relativeMap.put(((EmployeeRelativeWorkingTO) list.get(j)).getStaffId(), list.get(j));
            }
            data.put("RELATIVE", relativeMap);
        }
    }

    private void getPromotionData() throws Exception {
        list = (List) sqlMap.executeQueryForList("getSelectPromotionTO", whereConditions);
        if (list.size() > 0) {
            LinkedHashMap promotionDetailsMap = new LinkedHashMap();
            for (int i = list.size(), j = 0; i > 0; i--, j++) {
                promotionDetailsMap.put(((PromotionTO) list.get(j)).getPromotionID(), list.get(j));
            }
            data.put("PROMOTION", promotionDetailsMap);
        }
    }

    private void getAcadmicData() throws Exception {
        list = (List) sqlMap.executeQueryForList("getSelectEmployeeMasterAcademicTO", whereConditions);
        if (list.size() > 0) {
            academicMap = new LinkedHashMap();
            for (int i = list.size(), j = 0; i > 0; i--, j++) {
                academicMap.put(((EmployeeMasterEducationTO) list.get(j)).getAcademicID(), list.get(j));
            }
            data.put("ACADEMIC", academicMap);
        }
    }

    private void getTechnicalData() throws Exception {
        list = (List) sqlMap.executeQueryForList("getSelectEmployeeMasterTechnicalTO", whereConditions);
        if (list.size() > 0) {
            technicalMap = new LinkedHashMap();
            for (int i = list.size(), j = 0; i > 0; i--, j++) {
                technicalMap.put(((EmployeeMasterTechnicalTO) list.get(j)).getTechnicalID(), list.get(j));
            }
            data.put("TECHNICAL", technicalMap);
        }
    }

    private void getLanguageData() throws Exception {
        list = (List) sqlMap.executeQueryForList("getSelectEmployeeMasterLanguageTO", whereConditions);
        if (list.size() > 0) {
            languageMap = new HashMap();
            for (int i = list.size(), j = 0; i > 0; i--, j++) {
                languageMap.put(((EmployeeMasterLanguageTO) list.get(j)).getCboLanguageType(), list.get(j));
            }
            data.put("LANGUAGE", languageMap);
        }
    }

    private void getDependentData() throws Exception {
        list = (List) sqlMap.executeQueryForList("getSelectEmployeeMasterDependentTO", whereConditions);
        System.out.println("#$%#$# dependent data : " + list);
        if (list.size() > 0) {
            dependentMap = new LinkedHashMap();
            for (int i = 0; i < list.size(); i++) {
                objEmployeeMasterDependendTO = (EmployeeMasterDependendTO) list.get(i);
                System.out.println("#$%#$# dependent map : " + dependentMap);
                dependentMap.put(objEmployeeMasterDependendTO.getDependentId(), objEmployeeMasterDependendTO);
                //dependentMap.put( ((EmployeeMasterDependendTO)list.get(j)).getDependentId(),list.get(j));
            }
            data.put("DEPENDENT", dependentMap);
        }
    }

    /**
     * To make used object in executeQuery method as null
     */
    private void makeQueryNull() {
        whereCondition = null;
        list = null;
    }
    //    private void processPhoneData(String command) throws Exception{
    //        if(deletedPhoneMap!= null ){
    //            addressIterator = deletedPhoneMap.keySet().iterator();
    //            for(int i = deletedPhoneMap.size(); i > 0; i--){
    //                System.out.println("entering deleted phone map!!"+deletedPhoneMap);
    //
    //                key = (String)addressIterator.next();
    //                objEmployeeMasterPhoneTO = (EmployeeMasterPhoneTO)deletedPhoneMap.get(key);
    //                System.out.println("phone map!!"+objEmployeeMasterPhoneTO);
    //                logTO.setData(objEmployeeMasterPhoneTO.toString());
    //                logTO.setPrimaryKey(objEmployeeMasterPhoneTO.getKeyData());
    //                logTO.setStatus(objEmployeeMasterPhoneTO.getCommand());
    //                sqlMap.executeUpdate("deleteEmployeeDirectorWorkingTO", objEmployeeMasterPhoneTO);
    //                logDAO.addToLog(logTO);
    //            }
    //            deletedPhoneMap = null;
    //        }
    //        if(phoneMap!=null){
    //            addressIterator = phoneMap.keySet().iterator();
    //
    //            for(int i = phoneMap.size(); i > 0; i--){
    //                addressKey = (String)addressIterator.next();
    //                objEmployeeMasterPhoneTO = (EmployeeMasterPhoneTO)phoneMap.get(addressKey);
    //                logTO.setData(objEmployeeMasterPhoneTO.toString());
    //                logTO.setPrimaryKey(objEmployeeMasterTO.getKeyData());
    //                logTO.setStatus(objEmployeeMasterTO.getCommand());
    //                //if customer Id exists, set to customerAddressTO objectobj
    //                System.out.println("employee master phone!@!@"+objEmployeeMasterPhoneTO);
    //                objEmployeeMasterPhoneTO.setStatusBy(objEmployeeMasterTO.getStatusBy());
    //                objEmployeeMasterPhoneTO.setStatusDt(objEmployeeMasterTO.getStatusDt());
    //                if(command.equals(CommonConstants.TOSTATUS_UPDATE)){
    //                     if(objEmployeeMasterPhoneTO.getStatus().equals(CommonConstants.STATUS_CREATED)){
    //                            System.out.println("!@#!@#inside created"+objEmployeeMasterPhoneTO);
    ////                            sqlMap.executeUpdate("deleteEmployeeMasterPhone", objEmployeeMasterPhoneTO);
    //                            sqlMap.executeUpdate("insertEmployeeMasterPhoneTO", objEmployeeMasterPhoneTO);
    //                        }else{
    //                            sqlMap.executeUpdate("updateEmployeeMasterPhoneTO", objEmployeeMasterPhoneTO);
    //                        }
    //
    //                }else  if(command.equals(CommonConstants.TOSTATUS_INSERT)){
    //                        objEmployeeMasterTO.setSysId(objEmployeeMasterTO.getSysId());
    //                        sqlMap.executeUpdate("insertEmployeeMasterPhoneTO", objEmployeeMasterPhoneTO);
    //                    }
    //                else if(command.equals(CommonConstants.TOSTATUS_DELETE)){
    //                        objEmployeeMasterPhoneTO.setSysId(systemEmpID);
    //                        objEmployeeMasterPhoneTO.setStatus(objEmployeeMasterTO.getStatus());
    //                        objEmployeeMasterPhoneTO.setStatusBy(objEmployeeMasterTO.getStatusBy());
    //                        sqlMap.executeUpdate("deleteEmployeeMasterPhoneTO", objEmployeeMasterPhoneTO);
    //
    //                    }
    //
    //                logDAO.addToLog(logTO);
    //            }
    //            phoneMap = null;
    //        }
    //
    //    }

    private void processPhoneData(String command) throws Exception {
        System.out.println("@@@command" + command);
        try {
            System.out.println("phoneMap" + phoneMap);
            if (phoneMap != null) {
                phoneList = (HashMap) phoneMap.get(addressKey);
            }
            System.out.println("phoneList" + phoneList);
            if (phoneList != null) {
                ArrayList keys = new ArrayList(phoneList.keySet());
                for (int x = 0; x < keys.size(); x++) {
                    objEmployeeMasterPhoneTO = (EmployeeMasterPhoneTO) phoneList.get((Double) keys.get(x));
                    logTO.setData(objEmployeeMasterPhoneTO.toString());
                    logTO.setPrimaryKey(objEmployeeMasterTO.getKeyData());
                    logTO.setStatus(objEmployeeMasterTO.getCommand());
                    objEmployeeMasterPhoneTO.setStatusDt(objEmployeeMasterTO.getStatusDt());
                    objEmployeeMasterPhoneTO.setSysId(systemEmpID);
                    //if customer Id exists, set to customerPhoneTO object
                    if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                        objEmployeeMasterTO.setSysId(objEmployeeMasterTO.getSysId());
                        sqlMap.executeUpdate("insertEmployeeMasterPhoneTO", objEmployeeMasterPhoneTO);
                    } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                        if (objEmployeeMasterPhoneTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                            System.out.println("!@#!@#inside created" + objEmployeeMasterPhoneTO);
                            sqlMap.executeUpdate("deleteEmployeeMasterPhone", objEmployeeMasterPhoneTO);
                            sqlMap.executeUpdate("insertEmployeeMasterPhoneTO", objEmployeeMasterPhoneTO);
                        } else {
                            sqlMap.executeUpdate("updateEmployeeMasterPhoneTO", objEmployeeMasterPhoneTO);
                        }
                    } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                        objEmployeeMasterPhoneTO.setSysId(systemEmpID);
                        objEmployeeMasterPhoneTO.setStatus(objEmployeeMasterTO.getStatus());
                        objEmployeeMasterPhoneTO.setStatusBy(objEmployeeMasterTO.getStatusBy());
                        sqlMap.executeUpdate("deleteEmployeeMasterPhoneTO", objEmployeeMasterPhoneTO);

                    }

                    logDAO.addToLog(logTO);
                }
                phoneList = null;
            }
            System.out.println("deletedPhoneMap" + deletedPhoneMap);
            if (deletedPhoneMap != null) {
                ArrayList keys = new ArrayList(deletedPhoneMap.keySet());
                for (int i = 0; i < keys.size(); i++) {
                    Double key = (Double) keys.get(i);
                    objEmployeeMasterPhoneTO = (EmployeeMasterPhoneTO) deletedPhoneMap.get(key);
                    logTO.setData(objEmployeeMasterPhoneTO.toString());
                    logTO.setPrimaryKey(objEmployeeMasterTO.getKeyData());
                    logTO.setStatus(objEmployeeMasterTO.getCommand());
                    sqlMap.executeUpdate("deleteEmployeeMasterPhoneTO", objEmployeeMasterPhoneTO);
                    logDAO.addToLog(logTO);
                }
                deletedPhoneMap = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processLoanData(String command) throws Exception {
        if (deletedloanMap != null) {
            addressIterator = deletedloanMap.keySet().iterator();
            for (int i = deletedloanMap.size(); i > 0; i--) {
                key = (String) addressIterator.next();
                objEmployeeMasterTO = (EmployeeMasterTO) deletedloanMap.get(key);
                logTO.setData(objEmployeeMasterTO.toString());
                logTO.setPrimaryKey(objEmployeeMasterTO.getKeyData());
                logTO.setStatus(objEmployeeMasterTO.getCommand());
                objEmployeeTermLoanTO.setSysId(systemEmpID);
                sqlMap.executeUpdate("deleteEmployeeLoanDetailsTO", objEmployeeTermLoanTO);
                logDAO.addToLog(logTO);
            }
        }

        System.out.println("########command" + command);
        if (loanMap != null) {
            System.out.println("loanMap" + loanMap);
            addressIterator = loanMap.keySet().iterator();

            for (int i = loanMap.size(); i > 0; i--) {
                addressKey = (String) addressIterator.next();
                objEmployeeTermLoanTO = (EmployeeTermLoanTO) loanMap.get(addressKey);
                logTO.setData(objEmployeeTermLoanTO.toString());
                logTO.setPrimaryKey(objEmployeeMasterTO.getKeyData());
                logTO.setStatus(objEmployeeMasterTO.getCommand());
                //if customer Id exists, set to customerAddressTO objectobj
                objEmployeeTermLoanTO.setStatusBy(objEmployeeMasterTO.getStatusBy());
                objEmployeeTermLoanTO.setStatusDt(objEmployeeMasterTO.getStatusDt());
                System.out.println("objEmployeeTermLoanTO" + objEmployeeTermLoanTO);
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    System.out.println("systemEmpID------------>" + systemEmpID);
                    objEmployeeTermLoanTO.setSysId(systemEmpID);
                    sqlMap.executeUpdate("insertEmployeeLoanDetailsTO", objEmployeeTermLoanTO);
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    if (objEmployeeTermLoanTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                        System.out.println("INSIDE UPDATE BUT INSERT------>>>>" + objEmployeeTermLoanTO);
                        objEmployeeTermLoanTO.setSysId(objEmployeeMasterTO.getSysId());
                        sqlMap.executeUpdate("insertEmployeeLoanDetailsTO", objEmployeeTermLoanTO);
                    } else {
                        System.out.println("UPDATE------>>>>" + objEmployeeTermLoanTO);
                        objEmployeeTermLoanTO.setSysId(objEmployeeMasterTO.getSysId());
                        sqlMap.executeUpdate("updateEmployeeLoanDetailsTO", objEmployeeTermLoanTO);
                    }

                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    objEmployeeTermLoanTO.setSysId(objEmployeeMasterTO.getSysId());
                    objEmployeeTermLoanTO.setStatus(objEmployeeMasterTO.getStatus());
                    objEmployeeTermLoanTO.setStatusBy(objEmployeeMasterTO.getStatusBy());
                    sqlMap.executeUpdate("deleteEmployeeLoanDetailsTO", objEmployeeTermLoanTO);

                }
                logDAO.addToLog(logTO);
            }
            //            processPhoneData(command);
        }

    }

    private void processOprativeData(String command) throws Exception {
        if (deletedoprativeMap != null) {
            addressIterator = deletedoprativeMap.keySet().iterator();
            for (int i = deletedoprativeMap.size(); i > 0; i--) {
                key = (String) addressIterator.next();
                objEmployeeOprativeTO = (EmployeeOprativeTO) deletedoprativeMap.get(key);
                logTO.setData(objEmployeeOprativeTO.toString());
                logTO.setPrimaryKey(objEmployeeOprativeTO.getKeyData());
                logTO.setStatus(objEmployeeOprativeTO.getCommand());
                System.out.println("@$@#$@#$deletedoprativeMap" + deletedoprativeMap);
                sqlMap.executeUpdate("deleteEmployeeOprativeTO", objEmployeeOprativeTO);
                logDAO.addToLog(logTO);
            }
            deletedoprativeMap = null;
        }
        if (oprativeMap != null) {
            addressIterator = oprativeMap.keySet().iterator();

            for (int i = oprativeMap.size(); i > 0; i--) {
                System.out.println("@$@#$@#oprativeMap" + oprativeMap);
                addressKey = (String) addressIterator.next();
                objEmployeeOprativeTO = (EmployeeOprativeTO) oprativeMap.get(addressKey);
                logTO.setData(objEmployeeOprativeTO.toString());
                logTO.setPrimaryKey(objEmployeeMasterTO.getKeyData());
                logTO.setStatus(objEmployeeMasterTO.getCommand());
                //if customer Id exists, set to customerAddressTO objectobj
                System.out.println("objEmployeeOprativeTO" + objEmployeeOprativeTO);
                objEmployeeOprativeTO.setStatusBy(objEmployeeMasterTO.getStatusBy());
                objEmployeeOprativeTO.setStatusDt(objEmployeeMasterTO.getStatusDt());
                if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    if (objEmployeeOprativeTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                        objEmployeeOprativeTO.setSysId(objEmployeeMasterTO.getSysId());
                        sqlMap.executeUpdate("insertEmployeeOprativeTO", objEmployeeOprativeTO);
                    } else {
                        objEmployeeOprativeTO.setSysId(objEmployeeMasterTO.getSysId());
                        sqlMap.executeUpdate("updateEmployeeOprativeTO", objEmployeeOprativeTO);
                    }

                } else if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    System.out.println("systemEmpID------------>" + systemEmpID);
                    objEmployeeOprativeTO.setSysId(systemEmpID);
                    sqlMap.executeUpdate("insertEmployeeOprativeTO", objEmployeeOprativeTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    objEmployeeOprativeTO.setSysId(objEmployeeMasterTO.getSysId());
                    objEmployeeOprativeTO.setStatus(objEmployeeMasterTO.getStatus());
                    objEmployeeOprativeTO.setStatusBy(objEmployeeMasterTO.getStatusBy());
                    sqlMap.executeUpdate("deleteEmployeeOprativeTO", objEmployeeOprativeTO);

                }
                logDAO.addToLog(logTO);
            }
            //            processPhoneData(command);
            oprativeMap = null;
        }

    }
}
