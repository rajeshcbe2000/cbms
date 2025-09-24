/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CustomerDAO.java
 *
 * Created on June 18, 2003, 4:14 PM
 */
package com.see.truetransact.serverside.customer.gahan;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Date;
import java.rmi.RemoteException;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.transferobject.customer.CustomerTO;
import com.see.truetransact.transferobject.customer.CustomerAddressTO;
import com.see.truetransact.transferobject.customer.CustomerPhoneTO;
import com.see.truetransact.transferobject.customer.CustomerPassPortTO;
import com.see.truetransact.transferobject.customer.CustFinanceDetailsTO;
import com.see.truetransact.transferobject.customer.CustomerGaurdianTO;
import com.see.truetransact.transferobject.customer.CustomerIncomeParticularsTO;
import com.see.truetransact.transferobject.customer.CustomerLandDetailsTO;
import com.see.truetransact.transferobject.customer.AuthPersonsTO;  //Added by Rajesh
import com.see.truetransact.servicelocator.*;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.customer.CustomerSuspensionTO;
import com.see.truetransact.transferobject.customer.gahan.GahanCustomerDetailsTO;
import com.see.truetransact.transferobject.customer.gahan.GahanDocumentDetailsTO;
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

/**
 * This is used for AccountHead Data Access.
 *
 * @author Balachandar
 */
public class GahanCustomerDAO extends TTDAO {

    private SqlMap sqlMap;
    private HashMap data;
    private CustomerTO customerTO;
    private CustomerGaurdianTO objCustomerGaurdianTO;
    private CustomerSuspensionTO objCustomerSuspensionTO;
    private CustomerPassPortTO objCustomerPassPortTO;
    private CustomerIncomeParticularsTO objCustomerIncomeParticularsTO;
    private CustomerLandDetailsTO objCustomerLandDetailsTO;
    private CustFinanceDetailsTO objCustFinanceDetailsTO;
    private HashMap addressMap;
    private HashMap deletedAddressMap;
    private HashMap deletedPhoneMap;
    private HashMap deletedIncomeMap;
    private HashMap deletedLandDetMap;
    private HashMap IncParMap;
    private HashMap LandDetMap;
    private HashMap phoneMap;
    private CustomerAddressTO customerAddressTO;
    private CustomerIncomeParticularsTO customerIncomeParticularsTO;
    private CustomerLandDetailsTO customerLandDetailsTO;
    private CustomerPhoneTO customerPhoneTO;
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
    private int isMore = -1;
    private String addCondition;
    private String genId;
    private String recordNo = "";
    private String cusid = "";
    private byte[] photoByteArray;
    private File photoFile;
    private FileOutputStream writer = null;
    private final String CUST_PHOTO = "customer\\photos\\";
    //    private final String CUST_PHOTO = "customer/photos/";
    private byte[] signByteArray;
    private File signFile;
    private final String CUST_SIGN = "customer\\signatures\\";
    //    private final String CUST_SIGN = "customer/signatures/";
    private final int PHOTOGRAPH = 1;
    private final int SIGNATURE = 2;
    private String addressKey = new String();
    private String customerID;
    final String SCREEN = "CUS";
    private final String YES = "Y";
    ArrayList gahanCustomerJointDetailsTO;
    ArrayList gahanCustomerDetailsTO;
    ArrayList gahanCustomerPropertyDetails;
    GahanDocumentDetailsTO gahanCustomerDocumentTO;
    GahanCustomerDetailsTO gahanCustomerTO;
    List gahanDocDetails = new ArrayList();

    /**
     * Creates a new instance of CustomerDAO for manipulating Customer data
     */
    public GahanCustomerDAO() throws ServiceLocatorException {
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
                jointAccntTO.setDepositNo(customerTO.getCustId());
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
        logTO.setData(gahanCustomerDocumentTO.toString());
        logTO.setPrimaryKey(gahanCustomerDocumentTO.getKeyData());
        logTO.setStatus(gahanCustomerDocumentTO.getCommand());
        logDAO.addToLog(logTO);
        logDAO.addToLog(logTO);
        logDAO.addToLog(logTO);
        genId = getDocumentID();
        if (gahanCustomerDocumentTO != null) {
            System.out.println("gahanCustomerDocumentTO#####" + gahanCustomerDocumentTO);
            gahanCustomerDocumentTO.setDocumentGenId(genId);
            sqlMap.executeUpdate("insertGahanDocumentDetailsTO", gahanCustomerDocumentTO);
            for (int i = 0; i < gahanDocDetails.size(); i++) {
                HashMap docMap = new HashMap();
                docMap = (HashMap) gahanDocDetails.get(i);
                gahanCustomerDocumentTO.setDocumentNo(docMap.get("DOC_NO").toString());
                gahanCustomerDocumentTO.setDocumentType(docMap.get("DOC_TYPE").toString());
                gahanCustomerDocumentTO.setDocumentDt(DateUtil.getDateMMDDYYYY(docMap.get("DOC_DATE").toString()));
                gahanCustomerDocumentTO.setRegistredOffice(docMap.get("REG_OFFIC").toString());
                gahanCustomerDocumentTO.setSlno(docMap.get("SNO").toString());
                sqlMap.executeUpdate("insertGahanDocTO", gahanCustomerDocumentTO);
                gahanCustomerDocumentTO.setDocumentNo("");
                gahanCustomerDocumentTO.setDocumentType("");
                gahanCustomerDocumentTO.setDocumentDt(null);
                gahanCustomerDocumentTO.setRegistredOffice("");
                gahanCustomerDocumentTO.setSlno("");
            }
        }
        if (gahanCustomerTO != null) {
            gahanCustomerTO.setDocumentGenId(genId);
            sqlMap.executeUpdate("insertGahanCustomerDetailsTO", gahanCustomerTO);
        }
        if (gahanCustomerPropertyDetails != null) {
            for (int i = 0; i < gahanCustomerPropertyDetails.size(); i++) {
                GahanDocumentDetailsTO obj = (GahanDocumentDetailsTO) gahanCustomerPropertyDetails.get(i);
                System.out.println("nnnn obj " + obj.getResurveyNo());
                obj.setDocumentGenId(genId);
                System.out.println("GahanCustomerDetaicsdfdlsTsdO#####" + obj);
                sqlMap.executeUpdate("insertGahanPropertyDetailsTO", obj);

            }
        }
        if (gahanCustomerJointDetailsTO != null) {
//            System.out.println("in GahanCustomerJointDetails##### gahanCustomerJointDetailsTO.size() : "+gahanCustomerJointDetailsTO.size());
            for (int i = 0; i < gahanCustomerJointDetailsTO.size(); i++) {
//                System.out.println("GahanCustomerJointDetailsTO.size() ############ Value : " +i + " :  "+gahanCustomerJointDetailsTO.size());
                GahanCustomerDetailsTO obj = (GahanCustomerDetailsTO) gahanCustomerJointDetailsTO.get(i);
                obj.setDocumentGenId(genId);
                System.out.println("GahanCustomerDetailsTO#####" + obj);
                sqlMap.executeUpdate("insertGahanCustomerJointDetailsTO", obj);
                //                gahanCustomerJointDetailsTO=null;
            }
            gahanCustomerJointDetailsTO = null;
        }
    }

    private void storePhotoSign() throws Exception {
        //        DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
        setDriver();
        DriverManager.registerDriver(((java.sql.Driver) Class.forName(driverName).newInstance()));

        storePhotoSignFilesInServer();
        conn = DriverManager.getConnection(dataBaseURL, userName, passWord);
        // @machineName:port:SID,   userid,  password
        conn.setAutoCommit(false);

        if (driverName.equals("com.ibm.db2.jcc.DB2Driver")) {
            try {
                String photoString = "";
                String signString = "";
                if (photoByteArray != null) {
                    photoFile = new File(ServerConstants.SERVER_PATH + "/customer/" + customerID + "_photo.jpg");
                } else {
                    photoFile = null;
                }
                System.out.println("#$#$ photoFile : " + photoFile);
                if (photoByteArray != null) {
                    signFile = new File(ServerConstants.SERVER_PATH + "/customer/" + customerID + "_sign.jpg");
                } else {
                    signFile = null;
                }
                System.out.println("#$#$ signFile : " + signFile);
                if (photoFile != null) {
                    photoString = "?";
                } else {
                    photoString = "empty_blob()";
                }
                if (signFile != null) {
                    signString = "?";
                } else {
                    signString = "empty_blob()";
                }
                String st = "UPDATE " + tableName + " SET PHOTO_FILE = " + photoString
                        + ", SIGNATURE_FILE = " + signString + " WHERE " + tableCondition + "='" + customerID + "'";
                PreparedStatement ps = conn.prepareStatement(st);
                FileInputStream photoFis = null;
                if (photoFile != null) {
                    int fileLength = (int) photoFile.length();
                    photoFis = new FileInputStream(photoFile);
                    ps.setBinaryStream(1, photoFis, fileLength);
                    System.out.println("#$#$ photoFile...");
                }
                FileInputStream signFis = null;
                if (signFile != null) {
                    int fileLength = (int) signFile.length();
                    signFis = new FileInputStream(signFile);
                    ps.setBinaryStream(2, signFis, fileLength);
                    System.out.println("#$#$ signFile...");
                }
                System.out.println("#$#$ Update statement is : " + st);
                //        ps.setString(3, customerID);
                ps.executeUpdate();
                ps.close();
                conn.commit();
                conn.close();
                conn = null;
                photoByteArray = null;
                signByteArray = null;
                if (photoFile != null) {
                    photoFis.close();
                }
                if (signFile != null) {
                    signFis.close();
                }
                if (cmd.equals(CommonConstants.TOSTATUS_INSERT)) {
                    customerTO = null;
                }
            } catch (Exception se) {
                se.printStackTrace();
                System.out.println("SQL Exception : " + se);
                conn.close();
                conn = null;
                photoFile = null;
                signFile = null;
                //            writer.flush();
                //            writer.close();
                addCondition = "";
                if (cmd.equals(CommonConstants.TOSTATUS_INSERT)) {
                    customerTO = null;
                }
            }
        } else if (driverName.equals("oracle.jdbc.driver.OracleDriver")) {
            try {
                stmt = conn.createStatement();
                boolean b = false;
                String st;
                if (isMore == 0) {
                    st = "UPDATE " + tableName + " SET PHOTO_FILE = empty_blob(), SIGNATURE_FILE = empty_blob() WHERE " + tableCondition + "='" + customerID + "'";
                    if (tableName.equals("CORP_AUTH_CUST")) {
                        st = st + addCondition;
                    }
                    System.out.println("Update Statement executed : \n\t" + st);
                    b = stmt.execute(st);
                    System.out.println("##### cmd.equals(CommonConstants.TOSTATUS_INSERT) so, update Statement executed...");   // Print col 1
                }
                //        conn.commit();
                st = "SELECT PHOTO_FILE, SIGNATURE_FILE, " + tableCondition + " FROM " + tableName + " WHERE " + tableCondition + "='" + customerID + "'";
                if (tableName.equals("CORP_AUTH_CUST")) {
                    st = st + addCondition;
                }
                st = st + " FOR UPDATE";
                System.out.println("Statement execute query : \n\t" + st);
                rset = stmt.executeQuery(st);
                System.out.println("Statement execute query : " + rset);
                rset.next();
                System.out.println("rset.next()... Photo.... ");
                BLOB oracleBlob = ((OracleResultSet) rset).getBLOB(1);
                System.out.println("#$#$ photoFile : " + photoFile);
                System.out.println("selected file length = " + (photoFile == null ? "null" : photoFile.length()));
                System.out.println("cust_id = " + rset.getString(3));
                FileInputStream reader = null;
                OutputStream outstream = null;
                int size = 0;
                byte[] buffer;
                int length = 0;
                //                int oldLength = 0;
                if (photoFile != null) {
                    reader = new FileInputStream(photoFile);
                    System.out.println("reader initialized ");
                    outstream = oracleBlob.getBinaryOutputStream();
                    System.out.println("outstream initialized ");
                    size = oracleBlob.getBufferSize();
                    buffer = new byte[size];
                    length = -1;
                    //                    oldLength = 0;
                    while ((length = reader.read(buffer)) != -1) {
                        System.out.println("length : " + length);
                        outstream.write(buffer, 0, length);
                        //                        oldLength = length;
                    }
                    System.out.println("outstream written ");
                    reader.close();
                    outstream.close();
                }
                System.out.println("rset.next()... Signature.... ");
                oracleBlob = ((OracleResultSet) rset).getBLOB(2);
                System.out.println("#$#$ signFile : " + signFile);
                System.out.println("selected file length = " + (signFile == null ? "null" : signFile.length()));
                System.out.println("cust_id = " + rset.getString(3));
                if (signFile != null) {
                    reader = new FileInputStream(signFile);
                    System.out.println("reader initialized ");
                    outstream = oracleBlob.getBinaryOutputStream();
                    System.out.println("outstream initialized ");
                    size = oracleBlob.getBufferSize();
                    buffer = new byte[size];
                    length = 0;
                    //                    oldLength = 0;
                    while ((length = reader.read(buffer)) != -1) {
                        System.out.println("length : " + length);
                        //            length = oldLength + length;
                        outstream.write(buffer, 0, length);
                        //                        oldLength = length;
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
                conn = null;
                rset.close();
                if (photoFile != null || signFile != null) {
                    reader.close();
                }
                outstream = null;
                reader = null;
                oracleBlob = null;
                photoFile = null;
                signFile = null;
                addCondition = "";
                if (cmd.equals(CommonConstants.TOSTATUS_INSERT)) {
                    customerTO = null;
                }
            } catch (Exception se) {
                se.printStackTrace();
                System.out.println("SQL Exception : " + se);
                conn.close();
                stmt.close();
                conn = null;
                rset.close();
                photoFile = null;
                signFile = null;
                addCondition = "";
                if (cmd.equals(CommonConstants.TOSTATUS_INSERT)) {
                    customerTO = null;
                }
            }
        }
    }

    private void storePhotoSignFilesInServer() throws Exception {
        photoFile = new File(ServerConstants.SERVER_PATH + "/customer/" + customerID + "_photo.jpg");
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
        signFile = new File(ServerConstants.SERVER_PATH + "/customer/" + customerID + "_sign.jpg");
        writer = new FileOutputStream(signFile);
        if (signByteArray != null) {
            writer.write(signByteArray);
            System.out.println("##### inside writer.write(signByteArray) :" + signByteArray.toString());
        } else {
            signFile = null;
        }
        writer.flush();
        writer.close();
        //        photoByteArray = null;
        //        signByteArray = null;
    }

    // Method added by Rajesh
    private void populateCorpAuthPersons() throws Exception {
        int size = mapAuthPersonsTO.size();
        HashMap hashAuthPersons;
        HashMap rowMap;
        String mainCustID = customerID;
        //        java.util.Calendar c1=java.util.Calendar.getInstance();
        //        Date d1 = c1.getTime();
        System.out.println("#####Inside populateCorpAuthPersons()...");  //to be removed.
        for (int i = 1; i <= size; i++) {
            rowMap = (HashMap) mapAuthPersonsTO.get(String.valueOf(i));
            if (rowMap != null) {
                if (rowMap.containsKey("AUTH_CUST_ID")) {
                    if (cmd.equals(CommonConstants.TOSTATUS_INSERT)) {
                        rowMap.put("CUST_ID", mainCustID);
                    }
                    System.out.println("##### rowMap : " + rowMap);  //to be removed.
                    photoByteArray = (byte[]) rowMap.get("PHOTO_FILE_BYTE");
                    signByteArray = (byte[]) rowMap.get("SIGNATURE_FILE_BYTE");
                    //                if (photoByteArray!=null) {
                    //                    objAuthPersonsTO.setPhotoFile(objAuthPersonsTO.getPhotoFile().substring(objAuthPersonsTO.getPhotoFile().lastIndexOf(".")));
                    //                }
                    //                else {
                    //                    System.out.println("##### new StringBuffer(ServerConstants.SERVER_PATH).append(\"customer/photos/\").append(objAuthPersonsTO.getPhotoFile()).toString() : "+new StringBuffer(ServerConstants.SERVER_PATH).append(CUST_PHOTO).append(objAuthPersonsTO.getPhotoFile()).toString());  //to be removed.
                    ////                    photoByteArray = createByteArray(new StringBuffer(ServerConstants.SERVER_PATH).append(CUST_PHOTO).append(objAuthPersonsTO.getPhotoFile()).toString());
                    //                }
                    //                if (signByteArray!=null) {
                    //                    objAuthPersonsTO.setSignatureFile(objAuthPersonsTO.getSignatureFile().substring(objAuthPersonsTO.getSignatureFile().lastIndexOf(".")));
                    //                }
                    //                else {
                    //                    System.out.println("##### new StringBuffer(ServerConstants.SERVER_PATH).append(\"customer/signatures/\").append(objAuthPersonsTO.getPhotoFile()).toString() : "+new StringBuffer(ServerConstants.SERVER_PATH).append(CUST_SIGN).append(objAuthPersonsTO.getSignatureFile()).toString());  //to be removed.
                    ////                    signByteArray = createByteArray(new StringBuffer(ServerConstants.SERVER_PATH).append(CUST_SIGN).append(objAuthPersonsTO.getSignatureFile()).toString());
                    //                }
                    //                System.out.println("##### photoByteArray : "+photoByteArray.toString());
                    //                System.out.println("##### signByteArray : "+signByteArray.toString());
                    //                System.out.println("##### After checking bytearray if objAuthPersonsTO : "+objAuthPersonsTO);  //to be removed.
                    //                customerTO.setPhotoFile(objAuthPersonsTO.getPhotoFile());
                    //                customerTO.setSignatureFile(objAuthPersonsTO.getSignatureFile());
                    //                customerTO.setPhotoFile(photoByteArray);
                    //                customerTO.setSignatureFile(signByteArray);
                    //                System.out.println("##### customerTO.getPhotoFile().substring(0,customerTO.getPhotoFile().lastIndexOf(\".\")).length() : "+customerTO.getPhotoFile().substring(0,customerTO.getPhotoFile().lastIndexOf(".")).length());  //to be removed.
                    //                System.out.println("##### customerTO.getSignatureFile().substring(0,customerTO.getSignatureFile().lastIndexOf(\".\")).length() : "+customerTO.getSignatureFile().substring(0,customerTO.getSignatureFile().lastIndexOf(".")).length());  //to be removed.
                    //                if (customerTO.getPhotoFile() != null && customerTO.getPhotoFile().length() > 0) {
                    //                    if (customerTO.getPhotoFile().substring(0,customerTO.getPhotoFile().lastIndexOf(".")).length()<=0)
                    //                        customerTO.setPhotoFile(objAuthPersonsTO.getAuthCustID()+(String)customerTO.getPhotoFile());
                    //                    objAuthPersonsTO.setPhotoFile(customerTO.getPhotoFile());
                    //                    storePhoto();
                    //                    photoFile = commonFile;
                    //                }
                    //                if (customerTO.getSignatureFile() != null && customerTO.getSignatureFile().length() > 0){
                    //                    if(customerTO.getSignatureFile().substring(0,customerTO.getSignatureFile().lastIndexOf(".")).length()<=0)
                    //                        customerTO.setSignatureFile(objAuthPersonsTO.getAuthCustID()+(String)customerTO.getSignatureFile());
                    //                    objAuthPersonsTO.setSignatureFile(customerTO.getSignatureFile());
                    //                    storeSign();
                    //                    signFile = commonFile;
                    //                }
                    //                updateOldData(objAuthPersonsTO);
                    //                objAuthPersonsTO.setDateCreated(d1);
                    //                System.out.println("##### After photo setting objAuthPersonsTO : "+objAuthPersonsTO);  //to be removed.
                    boolean photoCreated = (new Boolean(String.valueOf(rowMap.get("photoCreated")))).booleanValue();
                    boolean signCreated = (new Boolean(String.valueOf(rowMap.get("signCreated")))).booleanValue();
                    //                if (customerTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT))
                    //                if (photoCreated || signCreated) {
                    //                    sqlMap.startTransaction();
                    //                    sqlMap.executeUpdate("insertCorpCustAuthTO",rowMap);
                    //                    sqlMap.commitTransaction();
                    //                }
                    HashMap depNo = new HashMap();
                    depNo.put("AUTH_CUST_ID", rowMap.get("AUTH_CUST_ID"));
                    depNo.put("CUST_ID", rowMap.get("CUST_ID"));
                    List list = (List) sqlMap.executeQueryForList("getCountCorpCustAuth", depNo);
                    HashMap corpAuthJointMap;
                    corpAuthJointMap = (HashMap) list.get(0);
                    isMore = -1;
                    isMore = (int) Integer.parseInt(corpAuthJointMap.get("COUNT").toString());
                    if (isMore == 0) { //--- If the data is not exisiting , insert the data
                        //                    if (cmd.equals(CommonConstants.TOSTATUS_INSERT))
                        sqlMap.startTransaction();
                        sqlMap.executeUpdate("insertCorpCustAuthTO", rowMap);
                        sqlMap.commitTransaction();
                        //                    if (!cmd.equals(CommonConstants.TOSTATUS_INSERT))
                        //                        sqlMap.startTransaction();
                    }
                    depNo = null;
                    list = null;
                    corpAuthJointMap = null;
                    customerID = (String) rowMap.get("AUTH_CUST_ID");
                    addCondition = " AND CUST_ID = '" + mainCustID + "'";
                    storePhotoSign();
                }
            }
            //            }
            //            objAuthPersonsTO=null;
            rowMap = null;
        }
        //        c1=null;
        //        d1=null;
        mapAuthPersonsTO = null;
        //        customerTO.setPhotoFile(null);
        //        customerTO.setSignatureFile(null);
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
     * Photograph is stored in the apt folder with apt file name
     *
     * @throws Exception
     */
    //    private void storePhoto() throws Exception{
    //        StringBuffer targetFileName = new StringBuffer(ServerConstants.SERVER_PATH).append(CUST_PHOTO);
    //        targetFileName.append(customerTO.getPhotoFile());
    //        fileWrite(CommonUtil.convertObjToStr(targetFileName),PHOTOGRAPH);
    //        System.out.println("##### StorePhoto called....");
    //    }
    /**
     * Signature is stored in the apt folder with apt file name
     *
     * @throws Exception
     */
    //    private void storeSign() throws Exception{
    //        StringBuffer targetFileName = new StringBuffer(ServerConstants.SERVER_PATH).append(CUST_SIGN);
    //        targetFileName.append(customerTO.getSignatureFile());
    //        fileWrite(CommonUtil.convertObjToStr(targetFileName),SIGNATURE);
    //        System.out.println("##### StoreSign called....");
    //    }
    /**
     * ByteArray is written in the apt file
     */
    //    private void fileWrite(String targetFileName,int byteArray) throws Exception{
    //        System.out.println("targetFileName:" + targetFileName);
    ////        File file = new File(targetFileName);
    //        File file = new File(targetFileName);
    //
    //        if (file.exists()) {
    //            String renFile = targetFileName.substring(0, targetFileName.lastIndexOf("."))
    //            + "_" + String.valueOf((new java.util.Date()).getTime())
    //            + targetFileName.substring(targetFileName.lastIndexOf(".")) ;
    //            System.out.println("renFile:" + renFile);
    //            file.renameTo(new File(renFile));
    //            commonFile = renFile.substring(renFile.lastIndexOf("\\")+1);
    //        }else commonFile = "";
    //        System.out.println("#### After FileExist...");
    //
    //        FileOutputStream writer = new FileOutputStream(targetFileName);
    ////        FileOutputStream writer = new FileOutputStream(file);
    //        System.out.println("#### After FileOutputStream creation...");
    //        System.out.println("##### byteArray : "+byteArray);
    //        if (byteArray == PHOTOGRAPH){
    //            if(photoByteArray!=null){
    //                writer.write(photoByteArray);
    //                System.out.println("##### inside writer.write(photoByteArray) :"+photoByteArray.toString());
    //            }
    //        }else if (byteArray == SIGNATURE){
    //            if(signByteArray!=null){
    //                writer.write(signByteArray);
    //                System.out.println("##### inside writer.write(signByteArray) :"+signByteArray.toString());
    //            }
    //        }
    //        writer.flush();
    //        writer.close();
    //        if (byteArray == PHOTOGRAPH){
    //                photoByteArray = null;
    //        }else if (byteArray == SIGNATURE){
    //                signByteArray = null;
    //        }
    //    }
    private void updateOldData(AuthPersonsTO objAuthPersonsTO) throws Exception {
        HashMap updateMap = new HashMap();
        updateMap.put("PHOTOFILE", photoFile);
        updateMap.put("SIGNATUREFILE", signFile);
        updateMap.put("CUST_ID", objAuthPersonsTO.getCustID());
        updateMap.put("AUTHORIZE_CUST_ID", objAuthPersonsTO.getAuthCustID());
        updateMap.put("OLDPHOTOFILE", objAuthPersonsTO.getPhotoFile());
        updateMap.put("OLDSIGNATUREFILE", objAuthPersonsTO.getSignatureFile());
        System.out.println("##### updateMap : " + updateMap);
        sqlMap.executeUpdate("updateCorpCustAuth", updateMap);
        updateMap = null;
    }

    /**
     * To get auto generated CustomerID from table
     */
    private String getDocumentID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", "DOCUMENT_GEN_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    /**
     * To get customerTO, addressMap & phoneMap from the HashMap
     */
    private void getAllTOs() throws Exception {
        customerTO = (CustomerTO) data.get("CUSTOMER");
        addressMap = (HashMap) data.get("ADDRESS");
        phoneMap = (HashMap) data.get("PHONE");
        IncParMap = (HashMap) data.get("INCOMEPAR");
        LandDetMap = (HashMap) data.get("LANDDETAILS");
        if (data.containsKey("ADDRESSDELETED")) {
            deletedAddressMap = (HashMap) data.get("ADDRESSDELETED");
        }
        if (data.containsKey("INCOMEDELETED")) {
            deletedIncomeMap = (HashMap) data.get("INCOMEDELETED");
        }
        if (data.containsKey("LANDDETAILSDELETED")) {
            deletedLandDetMap = (HashMap) data.get("LANDDETAILSDELETED");
        }
        if (data.containsKey("GAURDIAN")) {
            objCustomerGaurdianTO = (CustomerGaurdianTO) data.get("GAURDIAN");
        }
        if (data.containsKey("PHONEDELETED")) {
            deletedPhoneMap = (HashMap) data.get("PHONEDELETED");
        }
        if (data.containsKey("PASSPORT")) {
            objCustomerPassPortTO = (CustomerPassPortTO) data.get("PASSPORT");
        }

    }

    /**
     * To insert address data
     */
    private void processAddressData(String command) throws Exception {
        addressIterator = addressMap.keySet().iterator();
        for (int i = addressMap.size(); i > 0; i--) {
            addressKey = (String) addressIterator.next();
            customerAddressTO = (CustomerAddressTO) addressMap.get(addressKey);
            logTO.setData(customerAddressTO.toString());
            logTO.setPrimaryKey(customerTO.getKeyData());
            logTO.setStatus(customerTO.getCommand());
            //if customer Id exists, set to customerAddressTO object
            System.out.println("customerAddressTO" + customerAddressTO);
            if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                if (customerAddressTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                    customerAddressTO.setCustId(customerAddressTO.getCustId());
                    sqlMap.executeUpdate("insertCustomerAddressTO", customerAddressTO);
                } else {
                    customerAddressTO.setCustId(customerAddressTO.getCustId());
                    sqlMap.executeUpdate("updateCustomerAddressTO", customerAddressTO);
                }

            } else if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                customerAddressTO.setCustId(customerID);
                sqlMap.executeUpdate("insertCustomerAddressTO", customerAddressTO);
            }
            logDAO.addToLog(logTO);
            processPhoneData(command);
        }
        if (deletedAddressMap != null) {
            addressIterator = deletedAddressMap.keySet().iterator();
            for (int i = deletedAddressMap.size(); i > 0; i--) {
                key = (String) addressIterator.next();
                customerAddressTO = (CustomerAddressTO) deletedAddressMap.get(key);
                logTO.setData(customerAddressTO.toString());
                logTO.setPrimaryKey(customerTO.getKeyData());
                logTO.setStatus(customerTO.getCommand());
                sqlMap.executeUpdate("deleteCustomerAddressTO", customerAddressTO);
                logDAO.addToLog(logTO);
            }
        }
        //        System.out.println("processphonedate");
        //        processPhoneData(command);
    }

    /**
     * Income Particulars
     */
    private void processIncomePardata(String command) throws Exception {
        System.out.println("@@@@COMMAND" + command);
        if (deletedIncomeMap != null) {
            ArrayList addList1 = new ArrayList(deletedIncomeMap.keySet());
            System.out.println("@@@@addList" + addList1);
            if (addList1 != null) {
                for (int i = 0; i < addList1.size(); i++) {
                    customerIncomeParticularsTO = (CustomerIncomeParticularsTO) deletedIncomeMap.get(addList1.get(i));
                    logTO.setStatus(customerTO.getCommand());
                    System.out.println("@@@@customerIncomeParticularsTO" + customerIncomeParticularsTO);
                    logTO.setData(customerIncomeParticularsTO.toString());
                    logTO.setPrimaryKey(customerIncomeParticularsTO.getKeyData());
                    sqlMap.executeUpdate("deleteCustomerIncomeParticularsTO", customerIncomeParticularsTO);
                    logDAO.addToLog(logTO);
                }
                deletedIncomeMap = null;
            }
        }
        if (IncParMap != null) {
            ArrayList addList = new ArrayList(IncParMap.keySet());
            for (int i = 0; i < addList.size(); i++) {
                customerIncomeParticularsTO = (CustomerIncomeParticularsTO) IncParMap.get(addList.get(i));
                logTO.setData(customerIncomeParticularsTO.toString());
                logTO.setPrimaryKey(customerIncomeParticularsTO.getKeyData());
                logTO.setStatus(customerIncomeParticularsTO.getCommand());
                //if customer Id exists, set to customerAddressTO object
                System.out.println("customerIncomeParticularsTO" + customerIncomeParticularsTO);
                if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    if (customerIncomeParticularsTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                        System.out.println("@@@@UPDATE" + command);
                        customerIncomeParticularsTO.setCustId(customerAddressTO.getCustId());
                        sqlMap.executeUpdate("insertCustomerIncomeParticularsTO", customerIncomeParticularsTO);
                    } else {
                        System.out.println("@@@@UPDATE1" + command);
                        customerIncomeParticularsTO.setCustId(customerAddressTO.getCustId());
                        sqlMap.executeUpdate("updateCustomerIncomeParticularsTO", customerIncomeParticularsTO);
                    }

                } else if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    customerIncomeParticularsTO.setCustId(customerID);
                    sqlMap.executeUpdate("insertCustomerIncomeParticularsTO", customerIncomeParticularsTO);
                }
                logDAO.addToLog(logTO);
            }

        }
    }

    /**
     * Land Details
     */
    private void processLandDetailsData(String command) throws Exception {
        System.out.println("@@@@COMMAND" + command);
        if (deletedLandDetMap != null) {
            ArrayList addList2 = new ArrayList(deletedLandDetMap.keySet());
            System.out.println("@@@@addList" + addList2);
            if (addList2 != null) {
                for (int i = 0; i < addList2.size(); i++) {
                    customerLandDetailsTO = (CustomerLandDetailsTO) deletedLandDetMap.get(addList2.get(i));
                    logTO.setStatus(customerTO.getCommand());
                    System.out.println("@@@@customerLandDetailsTO" + customerLandDetailsTO);
                    logTO.setData(customerLandDetailsTO.toString());
                    logTO.setPrimaryKey(customerLandDetailsTO.getKeyData());
                    sqlMap.executeUpdate("deleteCustomerLandDetailsTO", customerLandDetailsTO);
                    logDAO.addToLog(logTO);
                }
                deletedLandDetMap = null;
            }
        }
        if (LandDetMap != null) {
            ArrayList addList = new ArrayList(LandDetMap.keySet());
            for (int i = 0; i < addList.size(); i++) {
                customerLandDetailsTO = (CustomerLandDetailsTO) LandDetMap.get(addList.get(i));
                logTO.setData(customerLandDetailsTO.toString());
                logTO.setPrimaryKey(customerLandDetailsTO.getKeyData());
                logTO.setStatus(customerLandDetailsTO.getCommand());
                //if customer Id exists, set to customerAddressTO object
                System.out.println("customerLandDetailsTO" + customerLandDetailsTO);
                if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    if (customerLandDetailsTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                        System.out.println("@@@@UPDATE" + command);
                        customerLandDetailsTO.setCustId(customerAddressTO.getCustId());
                        sqlMap.executeUpdate("insertCustomerLandDetailsTO", customerLandDetailsTO);
                    } else {
                        System.out.println("@@@@UPDATE1" + command);
                        customerLandDetailsTO.setCustId(customerAddressTO.getCustId());
                        sqlMap.executeUpdate("updateCustomerLandDetailsTO", customerLandDetailsTO);
                    }

                } else if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    customerLandDetailsTO.setCustId(customerID);
                    sqlMap.executeUpdate("insertCustomerLandDetailsTO", customerLandDetailsTO);
                }
                logDAO.addToLog(logTO);
            }
        }
    }

    private void processFinanceDetails() {
        try {
            if (objCustFinanceDetailsTO != null) {
                if (customerID != null) {
                    objCustFinanceDetailsTO.setCustId(customerID);
                }
                HashMap countMap = new HashMap();
                countMap.put("CUST_ID", customerID);
                List finList = ServerUtil.executeQuery("getFinanceDetailsCnt", countMap);
                countMap = null;
                countMap = (HashMap) finList.get(0);
                int cnt = CommonUtil.convertObjToInt(countMap.get("CNT"));
                logTO.setData(objCustFinanceDetailsTO.toString());
                logTO.setPrimaryKey(customerTO.getKeyData());
                logTO.setStatus(customerTO.getCommand());
                if (cnt <= 0) {
                    sqlMap.executeUpdate("insertCustFinanceDetailsTO", objCustFinanceDetailsTO);
                } else {
                    sqlMap.executeUpdate("updateCustFinanceDetailsTO", objCustFinanceDetailsTO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processSuspendRevokeCustomer() {
        try {
            if (objCustomerSuspensionTO != null) {
                if (customerID != null) {
                    objCustomerSuspensionTO.setCustId(customerID);
                }
                HashMap countMap = new HashMap();
                countMap.put("CUST_ID", customerID);
                List susList = ServerUtil.executeQuery("getSusCnt", countMap);
                countMap = null;
                countMap = (HashMap) susList.get(0);
                int cnt = CommonUtil.convertObjToInt(countMap.get("CNT"));
                logTO.setData(objCustomerSuspensionTO.toString());
                logTO.setPrimaryKey(customerTO.getKeyData());
                logTO.setStatus(customerTO.getCommand());
                if (cnt <= 0) {
                    sqlMap.executeUpdate("insertCustomerSuspensionTO", objCustomerSuspensionTO);
                } else {
                    sqlMap.executeUpdate("updateCustomerSuspensionTO", objCustomerSuspensionTO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * To insert phone data
     */
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
                    customerPhoneTO = (CustomerPhoneTO) phoneList.get((Double) keys.get(x));
                    logTO.setData(customerPhoneTO.toString());
                    logTO.setPrimaryKey(customerTO.getKeyData());
                    logTO.setStatus(customerTO.getCommand());
                    //if customer Id exists, set to customerPhoneTO object
                    if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                        customerPhoneTO.setCustId(customerID);
                        sqlMap.executeUpdate("insertCustomerPhoneTO", customerPhoneTO);
                    } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                        if (customerPhoneTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                            sqlMap.executeUpdate("insertCustomerPhoneTO", customerPhoneTO);
                        } else {
                            sqlMap.executeUpdate("updateCustomerPhoneTO", customerPhoneTO);
                        }
                    } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                        System.out.println("deleteCustomerPhoneTO" + customerPhoneTO);
                        sqlMap.executeUpdate("deleteCustomerPhoneTO", customerPhoneTO);
                    }

                    logDAO.addToLog(logTO);
                }
            }
            System.out.println("deletedPhoneMap" + deletedPhoneMap);
            if (deletedPhoneMap != null) {
                ArrayList keys = new ArrayList(deletedPhoneMap.keySet());
                for (int i = 0; i < keys.size(); i++) {
                    Double key = (Double) keys.get(i);
                    customerPhoneTO = (CustomerPhoneTO) deletedPhoneMap.get(key);
                    logTO.setData(customerPhoneTO.toString());
                    logTO.setPrimaryKey(customerTO.getKeyData());
                    logTO.setStatus(customerTO.getCommand());
                    sqlMap.executeUpdate("deleteCustomerPhoneTO", customerPhoneTO);
                    logDAO.addToLog(logTO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        phoneMap = null;
        customerAddressTO = null;
        customerPhoneTO = null;
        phoneList = null;
        key = null;
        addressIterator = null;
        incomeIterator = null;
        deletedAddressMap = null;
        deletedPhoneMap = null;
        IncParMap = null;
        LandDetMap = null;
        deletedIncomeMap = null;
        deletedLandDetMap = null;
        objCustomerPassPortTO = null;
        objCustomerSuspensionTO = null;
        objCustomerGaurdianTO = null;
    }

    private void updateJointAccountDetails() throws Exception {
        JointAccntTO jointAccntTO;
        HashMap depNo = new HashMap();
        depNo.put("CUST_ID", customerTO.getCustId());
        List list = (List) sqlMap.executeQueryForList("getCustCountJointAccntHolders", depNo);
        HashMap jntMapData = new HashMap();
        jntMapData.put("JointAccountHoldersData", list);
        HashMap JointAccntMap;
        JointAccntMap = (HashMap) ((List) jntMapData.get("JointAccountHoldersData")).get(0);
        int isMore = (int) Integer.parseInt(JointAccntMap.get("COUNT").toString());
        int jntAccntHolderSize = mapJointAccntTO.size();
        for (int i = 0; i < jntAccntHolderSize; i++) {
            try {
                jointAccntTO = (JointAccntTO) mapJointAccntTO.get(String.valueOf(i));
                jointAccntTO.setDepositNo(customerTO.getCustId());
                if (i < isMore) { //--- If the data is exisiting , update the data
                    sqlMap.executeUpdate("updateCustJointAccntTO", jointAccntTO);
                    logTO.setData(jointAccntTO.toString());
                    logTO.setPrimaryKey(jointAccntTO.getKeyData());
                    logDAO.addToLog(logTO);
                } else { //--- Else insert the data.
                    sqlMap.executeUpdate("insertCustJointAccntTO", jointAccntTO);
                    logTO.setData(jointAccntTO.toString());
                    logTO.setPrimaryKey(jointAccntTO.getKeyData());
                    logDAO.addToLog(logTO);
                }
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                throw new TransRollbackException(e);
            }
        }
    }

    /**
     * To Update customer data
     */
    private void updateData() throws Exception {
        logTO.setData("Photo has been Uploaded");
        logTO.setPrimaryKey(gahanCustomerDocumentTO.getKeyData());
        logTO.setStatus(gahanCustomerDocumentTO.getCommand());
        logDAO.addToLog(logTO);
        //        logTO.setData(customerTO.toString());
        //        logTO.setPrimaryKey(customerTO.getKeyData());
        //        logTO.setStatus(customerTO.getCommand());
        String id = gahanCustomerDocumentTO.getDocumentGenId();
        if (gahanCustomerDocumentTO != null) {
            sqlMap.executeUpdate("updateGahanCustomerDocumentTO", gahanCustomerDocumentTO);
            if (!gahanDocDetails.isEmpty()) {

                sqlMap.executeUpdate("deleteGhanDocTable", gahanCustomerDocumentTO);


                for (int i = 0; i < gahanDocDetails.size(); i++) {
                    HashMap docMap = new HashMap();
                    docMap = (HashMap) gahanDocDetails.get(i);
                    gahanCustomerDocumentTO.setDocumentNo(docMap.get("DOC_NO").toString());
                    gahanCustomerDocumentTO.setDocumentType(docMap.get("DOC_TYPE").toString());
                    gahanCustomerDocumentTO.setDocumentDt(DateUtil.getDateMMDDYYYY(docMap.get("DOC_DATE").toString()));
                    gahanCustomerDocumentTO.setRegistredOffice(docMap.get("REG_OFFIC").toString());
                    gahanCustomerDocumentTO.setSlno(docMap.get("SNO").toString());
                    sqlMap.executeUpdate("insertGahanDocTO", gahanCustomerDocumentTO);
                    gahanCustomerDocumentTO.setDocumentNo("");
                    gahanCustomerDocumentTO.setDocumentType("");
                    gahanCustomerDocumentTO.setDocumentDt(null);
                    gahanCustomerDocumentTO.setRegistredOffice("");
                    gahanCustomerDocumentTO.setSlno("");
                }


            }




        }
        if (gahanCustomerTO != null) {
            sqlMap.executeUpdate("updateGahanCustomerDetailsTO", gahanCustomerTO);
        }

        System.out.println("gahanCustomerPropertyDetails" + gahanCustomerPropertyDetails);
        if (gahanCustomerPropertyDetails != null && gahanCustomerPropertyDetails.size() > 0) {
            GahanDocumentDetailsTO doc = (GahanDocumentDetailsTO) gahanCustomerPropertyDetails.get(0);

            List propertylist = sqlMap.executeQueryForList("getSecurityDetails", id);
            System.out.println("propertylis" + propertylist);
            for (int i = 0; i < gahanCustomerPropertyDetails.size(); i++) {
                System.out.println("gahanCustomerPropertyDetails" + gahanCustomerPropertyDetails);
                String recordno = "";
                GahanDocumentDetailsTO obj = (GahanDocumentDetailsTO) gahanCustomerPropertyDetails.get(i);
                if (propertylist.size() > 0) {
                    //System.out.println("propertylist"+propertylist.get(i));
                    HashMap hmap = new HashMap();
                    if (i < propertylist.size()) {
                        hmap = (HashMap) propertylist.get(i);
                        recordno = CommonUtil.convertObjToStr(hmap.get("RECORD_NO"));
                        System.out.println(" recordno" + recordno);

                        String sno = obj.getSno();
                        if (recordno.equals(sno)) {
                            sqlMap.executeUpdate("updateGahanPropertyDetails", obj);
                        }
                    } else {
                        sqlMap.executeUpdate("insertGahanPropertyDetailsTO", obj);
                    }
                } else {
                    sqlMap.executeUpdate("insertGahanPropertyDetailsTO", obj);
                }

                //               if(custlist.size()==gahanCustomerPropertyDetails.size()){

                //               }
                System.out.println("@@@@@recordNo" + recordNo);
                System.out.println("@@@@@recordNo" + i);

                String genId = obj.getDocumentGenId();
                System.out.println("@@@@@genId" + genId);
                HashMap delmap = new HashMap();
                delmap.put("DOCUMENT_GEN_ID", genId);
                delmap.put("RECORD_NO", recordNo);
                sqlMap.executeUpdate("deleteGahanPropertyDetails", delmap);
            }
        }


        if (gahanCustomerJointDetailsTO != null && gahanCustomerJointDetailsTO.size() > 0) {

            List cust = sqlMap.executeQueryForList("getCustJoint", id);
            for (int i = 0; i < gahanCustomerJointDetailsTO.size(); i++) {
                System.out.println("gahanCustomerJointDetailsTO.size()" + gahanCustomerJointDetailsTO.size());
                GahanCustomerDetailsTO obj = (GahanCustomerDetailsTO) gahanCustomerJointDetailsTO.get(i);
                String cstid = "";
                if (cust.size() > 0) {

                    HashMap hmap = new HashMap();
                    if (i < cust.size()) {
                        hmap = (HashMap) cust.get(i);

                        cstid = CommonUtil.convertObjToStr(hmap.get("CUST_ID"));
                    }
                    System.out.println("obj is" + obj);
                    String custid = obj.getCustId();
                    if (cust.size() == gahanCustomerJointDetailsTO.size()) {
                        System.out.println("EQUAL UPDATION");
                        //               if(custid.equals(cstid)){
                        //                if(cust.size()==gahanCustomerJointDetailsTO.size()){
                        sqlMap.executeUpdate("updateGahanCustomerJointDetailsTO", obj);
                    } else {
                        if (custid.equals(cstid)) {
                            sqlMap.executeUpdate("updateGahanCustomerJointDetailsTO", obj);
                        } else {

                            sqlMap.executeUpdate("insertGahanCustomerJointDetailsTO", obj);
                        }
                    }
                } else {
                    sqlMap.executeUpdate("insertGahanCustomerJointDetailsTO", obj);
                }
                String genId = obj.getDocumentGenId();
                System.out.println("@@@@@genId" + genId);
                HashMap delmap = new HashMap();
                delmap.put("DOCUMENT_GEN_ID", genId);
                delmap.put("CUST_ID", cusid);

                sqlMap.executeUpdate("deleteGahanCustomerJointDetailsTO", delmap);


            }
        }

    }

    /**
     * To delete Customer data
     */
    private void deleteData() throws Exception {
        //        try {
        //        System.out.println("@@@@data"+data);

        logTO.setData("Photo has been Uploaded");
        logTO.setPrimaryKey(gahanCustomerDocumentTO.getKeyData());
        logTO.setStatus(gahanCustomerDocumentTO.getCommand());
        logDAO.addToLog(logTO);
        if (gahanCustomerDocumentTO != null) {
            sqlMap.executeUpdate("updateGahanCustomerDocumentTO", gahanCustomerDocumentTO);
        }
        if (gahanCustomerTO != null) {
            sqlMap.executeUpdate("updateGahanCustomerDetailsTO", gahanCustomerTO);
        }
        if (gahanCustomerJointDetailsTO != null) {
            for (int i = 0; i < gahanCustomerJointDetailsTO.size(); i++) {
                GahanCustomerDetailsTO obj = (GahanCustomerDetailsTO) gahanCustomerJointDetailsTO.get(i);

                System.out.println("obj is" + obj);
                sqlMap.executeUpdate("updateGahanCustomerJointDetailsTO", obj);
            }
        }
        if (gahanCustomerPropertyDetails != null) {
            for (int i = 0; i < gahanCustomerPropertyDetails.size(); i++) {
                GahanDocumentDetailsTO obj = (GahanDocumentDetailsTO) gahanCustomerPropertyDetails.get(i);

                System.out.println("GahanCustomerDetailsTO#####" + obj);
                sqlMap.executeUpdate("updateGahanPropertyDetails", obj);
            }
        }

        //        System.out.println("%%%phoneMap "+phoneMap);
        /**
         * HashMap addressMap = (HashMap) data.get("ADDRESS"); addressIterator =
         * addressMap.keySet().iterator(); for(int i = addressMap.size(); i > 0;
         * i--){ key = (String)addressIterator.next(); //         System.out.println("
         *
         * @@@@customerAddressTO"+customerAddressTO); customerAddressTO =
         * (CustomerAddressTO)addressMap.get(key);
         * logTO.setData(customerAddressTO.toString());
         * logTO.setPrimaryKey(customerTO.getKeyData());
         * logTO.setStatus(customerTO.getCommand());
         * sqlMap.executeUpdate("deleteCustomerAddressTO", customerAddressTO);
         * logDAO.addToLog(logTO); addressKey = key;
         * processPhoneData(customerTO.getCommand()); } HashMap IncParMap =
         * (HashMap) data.get("INCOMEPAR"); //          System.out.println("
         * @@@@IncParMap"+IncParMap); if (IncParMap!=null && IncParMap.size()>0)
         * { ArrayList addList =new ArrayList(IncParMap.keySet()); //          System.out.println("
         * @@@@addList"+addList); if(addList!=null && addList.size()>1){ for(int
         * i=0;i<addList.size();i++){ customerIncomeParticularsTO =
         * (CustomerIncomeParticularsTO) IncParMap.get(addList.get(i));
         * logTO.setStatus(customerTO.getCommand()); //
         * System.out.println("@@@@customerIncomeParticularsTO"+customerIncomeParticularsTO);
         * logTO.setData(customerIncomeParticularsTO.toString());
         * logTO.setPrimaryKey(customerIncomeParticularsTO.getKeyData());
         * sqlMap.executeUpdate("deleteCustomerIncomeParticularsTO",
         * customerIncomeParticularsTO); logDAO.addToLog(logTO); } } } HashMap
         * LandDetMap = (HashMap) data.get("LANDDETAILS"); //
         * System.out.println("@@@@LandDetMap"+LandDetMap); if (LandDetMap!=null
         * && LandDetMap.size()>0) { ArrayList addList1 =new
         * ArrayList(LandDetMap.keySet()); //          System.out.println("
         * @@@@addList"+addList1); if(addList1!=null && addList1.size()>1){
         * for(int i=0;i<addList1.size();i++){ customerLandDetailsTO =
         * (CustomerLandDetailsTO) LandDetMap.get(addList1.get(i));
         * logTO.setStatus(customerTO.getCommand()); //
         * System.out.println("@@@@customerLandDetailsTO"+customerLandDetailsTO);
         * logTO.setData(customerLandDetailsTO.toString());
         * logTO.setPrimaryKey(customerLandDetailsTO.getKeyData());
         * sqlMap.executeUpdate("deleteCustomerLandDetailsTO",
         * customerLandDetailsTO); logDAO.addToLog(logTO); } }
         * }*
         */
        //        System.out.println("@@@@check"+customerTO.getCommand());
        //        processPhoneData(customerTO.getCommand());
        //
        //        } catch (Exception e) {
        //            //sqlMap.rollbackTransaction();
        //            throw new TransRollbackException(e);
        //        }
    }

    /**
     * Standard method for insert, update & delete operations
     */
    public HashMap execute(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        System.out.println("#### DAO execute Gahan map : " + obj);
        HashMap resultMap = new HashMap();
        try {
            logDAO = new LogDAO();
            logTO = new LogTO();
            logTO.setUserId(CommonUtil.convertObjToStr(obj.get(CommonConstants.USER_ID)));
            logTO.setBranchId(CommonUtil.convertObjToStr(obj.get(CommonConstants.SELECTED_BRANCH_ID)));
            logTO.setInitiatedBranch(CommonUtil.convertObjToStr(obj.get(CommonConstants.BRANCH_ID)));
            logTO.setIpAddr(CommonUtil.convertObjToStr(obj.get(CommonConstants.IP_ADDR)));
            logTO.setModule(CommonUtil.convertObjToStr(obj.get(CommonConstants.MODULE)));
            logTO.setScreen(CommonUtil.convertObjToStr(obj.get(CommonConstants.SCREEN)));
            recordNo = CommonUtil.convertObjToStr(obj.get("RECORDNO"));
            cusid = CommonUtil.convertObjToStr(obj.get("CUSID"));
            System.out.println("######recordNo" + recordNo);
            gahanCustomerJointDetailsTO = null;

            gahanCustomerPropertyDetails = null;

            gahanCustomerDetailsTO = null;

            if (obj.containsKey("GahanCustomerJointDetails")) {
                gahanCustomerJointDetailsTO = (ArrayList) obj.get("GahanCustomerJointDetails");
                System.out.println("in GahanCustomerJointDetails");
            }
            if (obj.containsKey("GahanDocumentDetailsTO")) {
                gahanCustomerDocumentTO = (GahanDocumentDetailsTO) obj.get("GahanDocumentDetailsTO");
            }
            if (obj.containsKey("GahanaPropertyDetails")) {

                gahanCustomerPropertyDetails = (ArrayList) obj.get("GahanaPropertyDetails");
                System.out.println("propert details are" + gahanCustomerPropertyDetails);
            }
            if (obj.containsKey("GahanCustomerDetails")) {
                gahanCustomerTO = (GahanCustomerDetailsTO) obj.get("GahanCustomerDetails");

            }
            if (obj.containsKey("GahanDocDetails")) {
                gahanDocDetails = (List) obj.get("GahanDocDetails");
            }

            //Start the transaction
            sqlMap.startTransaction();
            cmd = CommonUtil.convertObjToStr(obj.get("COMMAND"));
            System.out.println("command is" + cmd);
            if (cmd.equals(CommonConstants.TOSTATUS_INSERT)) {

                insertData();
                resultMap.put("CUST_ID", genId);
            } else if (cmd.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData();
                genId = gahanCustomerDocumentTO.getDocumentGenId();
                resultMap.put("CUST_ID", genId);
            } else if (cmd.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData();
                resultMap.put("CUST_ID", genId);
            } else {
                sqlMap.rollbackTransaction();
                throw new NoCommandException();
            }
            //Commit the transaction
            sqlMap.commitTransaction();
        } catch (Exception ex) {
            sqlMap.rollbackTransaction();
            throw ex;
        }
        System.out.println("resultMap" + resultMap);
        return resultMap;
    }

    /**
     * Standard method to get data for a particular customer based on customer
     * id
     */
    public HashMap executeQuery(HashMap condition) throws Exception {
        System.out.println("###condition" + condition);
        _branchCode = (String) condition.get(CommonConstants.BRANCH_ID);
        whereConditions = condition;
        if (condition.containsKey(CommonConstants.MAP_WHERE)) {
            whereCondition = (String) condition.get(CommonConstants.MAP_WHERE);
        }
        //        System.out.println("###whereconditions"+whereConditions);
        if (!condition.containsKey("PHOTOSIGNONLY")) {
            getCustomerData();
            getAddressData();
            getGaurdianData();
            getPhoneData();
            getPassPortData();
            getIncparData();
            getLandDetailsData();
            getSuspRevokedDetails();
            makeNull();
            makeQueryNull();
        } else {
            data = new HashMap();
            List lst = (List) sqlMap.executeQueryForList("getCustomerCustType", condition);
            String custType = CommonUtil.convertObjToStr(((HashMap) lst.get(0)).get("CUST_TYPE"));
            if (custType.equals("INDIVIDUAL")) {
                tableName = "CUSTOMER";
                tableCondition = "CUST_ID";
            } else {
                tableName = "CORP_AUTH_CUST";
                tableCondition = "AUTHORIZE_CUST_ID";
            }
            lst = null;
            getPhotoSign();
        }
        return data;
    }

    private void getCustomerData() throws Exception {
        IntroducerDAO objIntroducerDAO = new IntroducerDAO();
        String introType = null;
        CustomerTO cust = null;
        list = (List) sqlMap.executeQueryForList("getSelectCustomerTO", whereConditions);
        if (list.size() > 0) {
            data = new HashMap();
            data.put("CUSTOMER", list.get(0));
            cust = (CustomerTO) list.get(0);
        }
        introType = CommonUtil.convertObjToStr(((CustomerTO) list.get(0)).getIntroType());
        /* get the introduction type and based on that, get the data
         * from particular table
         */
        String where = CommonUtil.convertObjToStr(((CustomerTO) list.get(0)).getCustId());
        System.out.println("@@@where" + where);
        if (introType != null) {
            //String introType, String acctNo, String screen
            data.put(introType, (List) objIntroducerDAO.getIntroData(introType, where, SCREEN));
        }
        objIntroducerDAO = null;

        list = (List) sqlMap.executeQueryForList("getCorpCustSelectJointAcctInfo", whereConditions);
        if (list.size() > 0) {
            data.put("JointAcctDetails", list);
        }
        list = (List) sqlMap.executeQueryForList("getSelectCorpAuthCustInfo", whereConditions);
        System.out.println("##### getSelectCorpAuthCustInfo : " + list);
        mapAuthPersonsTO = new HashMap();
        HashMap hashAuthPersons = new HashMap();
        HashMap rowMap;
        HashMap tempMap;
        String mainCustID = whereCondition;
        String custType = "";
        if (cust != null) {
            custType = cust.getCustType();
            if (!custType.equals("INDIVIDUAL")) {
                tableName = "CORP_AUTH_CUST";
                tableCondition = "AUTHORIZE_CUST_ID";
                addCondition = " AND CUST_ID = '" + mainCustID + "'";
                for (int i = 0; i < list.size(); i++) {
                    rowMap = new HashMap();
                    tempMap = (HashMap) list.get(i);
                    rowMap.put("CUST_ID", tempMap.get("CUST_ID"));
                    rowMap.put("AUTH_CUST_ID", tempMap.get("AUTHORIZE_CUST_ID"));
                    whereCondition = CommonUtil.convertObjToStr(tempMap.get("AUTHORIZE_CUST_ID"));
                    getPhotoSign();
                    rowMap.put("PHOTO_FILE_BYTE", mapPhotoSign.get("PHOTO"));
                    rowMap.put("SIGNATURE_FILE_BYTE", mapPhotoSign.get("SIGN"));
                    hashAuthPersons.put(tempMap.get("AUTHORIZE_CUST_ID"), rowMap);
                }
                data.put("AuthPersonsTO", hashAuthPersons);
            } else {
                tableName = "CUSTOMER";
                tableCondition = "CUST_ID";
                getPhotoSign();
            }
        }
        cust = null;
        System.out.println("##### getCustomerData() data : " + data);
        whereCondition = mainCustID;
        hashAuthPersons = null;
        rowMap = null;
        tempMap = null;
        mapPhotoSign = null;
    }

    private void getPhotoSign() throws Exception {
        try {
            mapPhotoSign = new HashMap();
            //        DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
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
                    }
                    if (rset.getBlob(3) != null) {
                        oracleBlob = rset.getBlob(3);
                        // get the length of the blob
                        long length = oracleBlob.length();
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
                        }
                        System.out.println("##### PHOTO SIGN MAP : " + mapPhotoSign);
                        data.put("PHOTOSIGN", mapPhotoSign);
                    }
                    //                mapPhotoSign = null;
                    f1 = null;
                    out = null;
                    in = null;
                    oracleBlob = null;
                }
            }
            addCondition = "";
            //        conn.commit();
            rset.close();
            stmt.close();
            //            conn.close();
        } catch (Exception se) {
            se.printStackTrace();
            addCondition = "";
            //            conn.rollback();
            rset.close();
            stmt.close();
            //            conn.close();
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

    private void getGaurdianData() throws Exception {
        list = (List) sqlMap.executeQueryForList("getSelectCustomerGaurdianTO", whereConditions);
        objCustomerGaurdianTO = new CustomerGaurdianTO();
        if (list.size() > 0) {
            objCustomerGaurdianTO = (CustomerGaurdianTO) list.get(0);
            data.put("GAURDIAN", objCustomerGaurdianTO);
        }
    }

    private void getSuspRevokedDetails() throws Exception {
        list = (List) sqlMap.executeQueryForList("getSelectCustomerSuspensionTO", whereConditions);
        objCustomerSuspensionTO = new CustomerSuspensionTO();
        if (list.size() > 0) {
            objCustomerSuspensionTO = (CustomerSuspensionTO) list.get(0);
            data.put("SUSPREVOKE", objCustomerSuspensionTO);
        }
    }

    private void getPassPortData() throws Exception {
        list = (List) sqlMap.executeQueryForList("getSelectCustomerPassPortTO", whereConditions);
        objCustomerPassPortTO = new CustomerPassPortTO();
        if (list.size() > 0) {
            objCustomerPassPortTO = (CustomerPassPortTO) list.get(0);
            data.put("PASSPORT", objCustomerPassPortTO);
        }
    }

    private void getAddressData() throws Exception {
        list = (List) sqlMap.executeQueryForList("getSelectCustomerAddressTO", whereConditions);
        if (list.size() > 0) {
            addressMap = new HashMap();
            for (int i = list.size(), j = 0; i > 0; i--, j++) {
                addressMap.put(((CustomerAddressTO) list.get(j)).getAddrType(), list.get(j));
            }
            data.put("ADDRESS", addressMap);
        }
    }

    private void getIncparData() throws Exception {
        list = (List) sqlMap.executeQueryForList("getSelectCustomerIncomeParticularsTO", whereConditions);
        if (list.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            System.out.println("@@@list" + list);
            for (int i = list.size(), j = 0; i > 0; i--, j++) {
                ParMap.put(((CustomerIncomeParticularsTO) list.get(j)).getSlno(), list.get(j));
            }
            data.put("INCOMEPAR", ParMap);
        }
    }

    private void getLandDetailsData() throws Exception {
        list = (List) sqlMap.executeQueryForList("getSelectCustomerLandDetailsTO", whereConditions);
        if (list.size() > 0) {
            LinkedHashMap LandMap = new LinkedHashMap();
            System.out.println("@@@list" + list);
            for (int i = list.size(), j = 0; i > 0; i--, j++) {
                LandMap.put(((CustomerLandDetailsTO) list.get(j)).getSlno(), list.get(j));
            }
            data.put("LANDDETAILS", LandMap);
        }
    }

    private void getPhoneData() throws Exception {
        list = (List) sqlMap.executeQueryForList("getSelectCustomerPhoneTO", whereConditions);
        System.out.println("#$%#$# Phone List : " + list);
        if (list.size() > 0) {
            phoneMap = new HashMap();
            String addrType;
            HashMap tmpPhoneMap;
            for (int i = list.size(), j = 0; i > 0; i--, j++) {
                customerPhoneTO = (CustomerPhoneTO) list.get(j);
                System.out.println("#$%#$# customerPhoneTO : " + customerPhoneTO);
                addrType = customerPhoneTO.getAddrType();
                if (phoneMap.containsKey(addrType)) {
                    tmpPhoneMap = (HashMap) phoneMap.get(addrType);
                } else {
                    tmpPhoneMap = new HashMap();
                }
                tmpPhoneMap.put(customerPhoneTO.getPhoneId(), customerPhoneTO);
                phoneMap.put(addrType, tmpPhoneMap);
            }
            tmpPhoneMap = null;
            data.put("PHONE", phoneMap);
        }
    }

    /**
     * To make used object in executeQuery method as null
     */
    private void makeQueryNull() {
        whereCondition = null;
        list = null;
    }
}
