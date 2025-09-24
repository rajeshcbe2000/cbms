/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DCBFileUpload.java
 *
 * Created on June 18, 2003, 4:14 PM
 */
package com.see.truetransact.serverside.transaction.fileUpload;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.transferobject.product.operativeacct.*;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.product.mds.MDSProductSchemeTO;
import com.see.truetransact.transferobject.product.mds.MDSProductAcctHeadTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.transaction.common.product.gl.GLTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import java.io.File;
import java.io.FileInputStream;

/**
 * This is used for DCBFileUploadDAO Data Access.
 *
 * @author Suresh R
 *
 */
public class FileUploadDAO extends TTDAO {

    private final static Logger log = Logger.getLogger(FileUploadDAO.class);
    private static SqlMap sqlMap = null;
    private Date CurrDt = null;
    private TransactionDAO transactionDAO = null;
    TransferDAO transferDAO = new TransferDAO();
    private LogDAO logDAO;
    private LogTO logTO;
    List finalTableList = null;
    List finalFileTableList = null;
    HashMap returnMap = new HashMap();
    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public FileUploadDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public static void main(String str[]) {
        try {
            FileUploadDAO dao = new FileUploadDAO();
            HashMap inputMap = new HashMap();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getFileID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "FILE_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("######### DCB File Upload Map in DAO : " + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        CurrDt = ServerUtil.getCurrentDate(_branchCode);
        returnMap = new HashMap();
        logDAO = new LogDAO();
        logTO = new LogTO();
        finalTableList = new ArrayList();
        
        if (map.containsKey("FILE_DATA")) {
            finalTableList = (List) map.get("FILE_DATA");
        }
        if (map.containsKey("RTGS_FILE_DATA")) {
            finalFileTableList = (List) map.get("RTGS_FILE_DATA");
        }
        
        System.out.println("########### finalTableList : "+finalTableList);
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            if (map.containsKey("RTGS_FILE_DATA")) {
                insertFileUploadData(map);
            } else {
                insertData(map);
            }
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            //updateData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData(map);
        } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap, map);
            }
        }
        map = null;
        destroyObjects();
        System.out.println("###### returnMap : "+returnMap);
        return returnMap;
    }
    
     private void insertFileUploadData(HashMap map) throws Exception {
         try {
            sqlMap.startTransaction();
          if ((finalFileTableList!= null && finalFileTableList.size() > 0)) {
                String fileID = getFileID();                     // getUniqueID
                ArrayList singleRowList = new ArrayList();
                HashMap dataMap = new HashMap();
                if (fileID.length() > 0) {
                    dataMap = new HashMap();
                    dataMap.put("FILE_ID", fileID);
                    dataMap.put("FILE_NAME", map.get("FILE_NAME"));
                    sqlMap.executeUpdate("insertDCBFileName", dataMap); //INSERT DCB FILE_NAME
                    
                     //Transaction Start
                   TransferTrans transferTrans = new TransferTrans();
                  TxTransferTO transferTo = new TxTransferTO();
                  ArrayList TxTransferTO = new ArrayList();
                  double transAmount=0;
                  HashMap txMap = new HashMap();
                  transferTrans.setInitiatedBranch(_branchCode);
                  transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                  transactionDAO.setInitiatedBranch(_branchCode);
                  transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                    for (int i = 0; i < finalFileTableList.size(); i++) {
                        dataMap = new HashMap();
                        singleRowList = (ArrayList) finalFileTableList.get(i);
                        transAmount = transAmount + CommonUtil.convertObjToDouble(singleRowList.get(8));
                        HashMap actMap = new HashMap();
                        actMap.put("ACT_NUM", LPad(CommonUtil.convertObjToStr(singleRowList.get(5)), 13, '0'));
                        List accountList = (List) sqlMap.executeQueryForList("getCrDrAccountDetails", actMap);
                        if (accountList != null && accountList.size() > 0) {
                            actMap = (HashMap) accountList.get(0);
                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(actMap.get("AC_HD_ID")));
                            txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(actMap.get("PROD_TYPE")));
                            txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(actMap.get("PROD_ID")));
                            txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(actMap.get("ACT_NUM")));
                            txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(actMap.get("BRANCH_ID")));
                            txMap.put(TransferTrans.PARTICULARS, "Credit From :" + CommonUtil.convertObjToStr(singleRowList.get(13)));
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(singleRowList.get(8)));
                            transferTo.setTransModType(CommonUtil.convertObjToStr(actMap.get("PROD_TYPE")));
                            transferTo.setScreenName(CommonUtil.convertObjToStr(map.get("SCREEN")));
                            transferTo.setInstrumentNo1("");
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransType(CommonConstants.CREDIT);
                            transferTo.setAcHdId(CommonUtil.convertObjToStr(actMap.get("AC_HD_ID")));
                            transferTo.setTransDt(CurrDt);
                            transferTo.setBranchId(CommonUtil.convertObjToStr(actMap.get("BRANCH_ID")));
                            transferTo.setInitiatedBranch(_branchCode);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(actMap.get("ACT_NUM")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(actMap.get("ACT_NUM")));
                            TxTransferTO.add(transferTo);
                        }

                    }
                    HashMap actMap = new HashMap();
                    actMap.put("ACT_NUM", CommonUtil.convertObjToStr(map.get("DEBIT_ACCOUNT")));
                    List accountList = (List) sqlMap.executeQueryForList("getCrDrAccountDetails", actMap);
                    if (accountList != null && accountList.size() > 0) {
                        actMap = (HashMap) accountList.get(0);
                        txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(actMap.get("AC_HD_ID")));
                        txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(actMap.get("PROD_TYPE")));
                        txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(actMap.get("PROD_ID")));
                        txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(actMap.get("ACT_NUM")));
                        txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(actMap.get("BRANCH_ID")));
                        txMap.put(TransferTrans.PARTICULARS, "Bulk Transaction");
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmount);
                        transferTo.setTransModType(CommonUtil.convertObjToStr(actMap.get("PROD_TYPE")));
                        transferTo.setScreenName(CommonUtil.convertObjToStr(map.get("SCREEN")));
                        transferTo.setInstrumentNo1("BULK UPLOAD");
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setTransType(CommonConstants.DEBIT);
                        transferTo.setAcHdId(CommonUtil.convertObjToStr(actMap.get("AC_HD_ID")));
                        transferTo.setTransDt(CurrDt);
                        transferTo.setBranchId(CommonUtil.convertObjToStr(actMap.get("BRANCH_ID")));
                        transferTo.setInitiatedBranch(_branchCode);
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setGlTransActNum(CommonUtil.convertObjToStr(actMap.get("ACT_NUM")));
                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(actMap.get("ACT_NUM")));
                        TxTransferTO.add(transferTo);
                    } else {
                        throw new TTException("Invalid Account Number  Or  Account Number Pending for Authorization " + CommonUtil.convertObjToStr(map.get("DEBIT_ACCOUNT")));
                    }
                    HashMap applnMap = new HashMap();
                    map.put("MODE", "INSERT");
                    map.put("COMMAND", "INSERT");
                    map.put("TxTransferTO", TxTransferTO);
//                    map.put("LINK_BATCH_ID", fileID);
                    HashMap transMap = transferDAO.execute(map, false);
                    map.put("BATCH_ID", transMap.get("TRANS_ID"));
                   returnMap.put("BATCH_ID", transMap.get("TRANS_ID"));
              }
          }
           sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
        map = null;

     }
    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            if ((finalTableList != null && finalTableList.size() > 0)) {
                String fileID = getFileID();                     // getUniqueID
                ArrayList singleRowList = new ArrayList();
                HashMap dataMap = new HashMap();
                if (fileID.length() > 0) {
                    for (int i = 0; i < finalTableList.size(); i++) {
                        dataMap = new HashMap();
                        singleRowList = (ArrayList) finalTableList.get(i);
                        dataMap.put("FILE_ID", fileID);
                        dataMap.put("COL1", CommonUtil.convertObjToStr(singleRowList.get(0)));
                        dataMap.put("COL2", CommonUtil.convertObjToStr(singleRowList.get(1)));
                        dataMap.put("COL3", CommonUtil.convertObjToStr(singleRowList.get(2)));
                        dataMap.put("COL4", CommonUtil.convertObjToStr(singleRowList.get(3)));
                        dataMap.put("COL5", CommonUtil.convertObjToStr(singleRowList.get(4)));
                        dataMap.put("COL6", CommonUtil.convertObjToStr(singleRowList.get(5)));
                        dataMap.put("COL7", CommonUtil.convertObjToStr(singleRowList.get(6)).replaceAll(",", ""));
                        dataMap.put("COL8", CommonUtil.convertObjToStr(singleRowList.get(7)));
                        dataMap.put("COL9", CommonUtil.convertObjToStr(singleRowList.get(8)));
                        dataMap.put("COL10", CommonUtil.convertObjToStr(singleRowList.get(9)));
                        dataMap.put("COL11", CommonUtil.convertObjToStr(singleRowList.get(10)));
                        dataMap.put("COL12", CommonUtil.convertObjToStr(singleRowList.get(11)));
                        dataMap.put("COL13", CommonUtil.convertObjToStr(singleRowList.get(12)));
                        dataMap.put("COL14", CommonUtil.convertObjToStr(singleRowList.get(13)));
                        dataMap.put("COL15", CommonUtil.convertObjToStr(singleRowList.get(14)));
                        dataMap.put("COL16", CommonUtil.convertObjToStr(singleRowList.get(15)));
                        dataMap.put("COL17", "P");
                        dataMap.put("USERNAME", map.get("USER_ID"));
                        dataMap.put("INITIATEDBR", _branchCode);
                        System.out.println("############### dataMap : " + dataMap);
                        sqlMap.executeUpdate("insertDCBFileDetails", dataMap);
                    }
                    dataMap = new HashMap();
                    dataMap.put("FILE_ID", fileID);
                    dataMap.put("FILE_NAME", map.get("FILE_NAME"));
                    sqlMap.executeUpdate("insertDCBFileName", dataMap); //INSERT DCB FILE_NAME
                    
                    //Transaction Start
                    TransferTrans transferTrans = new TransferTrans();
                    TxTransferTO transferTo = new TxTransferTO();
                    ArrayList TxTransferTO = new ArrayList();
                    HashMap txMap = new HashMap();
                    transferTrans.setInitiatedBranch(_branchCode);
                    transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                    transactionDAO.setInitiatedBranch(_branchCode);
                    transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                    HashMap actMap = new HashMap();
                    dataMap = new HashMap();
                    String transType = "";
                    String act_Num = "";
                    String act_Branch = "";
                    String acHD_ID = "";
                    String prod_ID = "";
                    String prod_Type = "";
                    String particulars = "";
                    double transAmount = 0.0;
                    dataMap.put("FILE_ID", fileID);
                    dataMap.put("BRANCH_ID", _branchCode);
                    List fileDataList = (List) sqlMap.executeQueryForList("getDCBFileDetails", dataMap);
                    if (fileDataList != null && fileDataList.size() > 0) {
                        for (int i = 0; i < fileDataList.size(); i++) {
                            dataMap = (HashMap) fileDataList.get(i);
                            txMap = new HashMap();
                            actMap = new HashMap();
                            transferTo = new TxTransferTO();
                            transAmount = CommonUtil.convertObjToDouble(dataMap.get("AMOUNT"));
                            if (transAmount > 0) {
                                act_Num = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));
                                prod_ID = CommonUtil.convertObjToStr(dataMap.get("PROD_ID"));
                                acHD_ID = CommonUtil.convertObjToStr(dataMap.get("AC_HD_ID"));
                                transType = CommonUtil.convertObjToStr(dataMap.get("TRANS_TYPE"));
                                act_Branch = CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID"));
                                particulars = CommonUtil.convertObjToStr(dataMap.get("PARTICULARS"));
                                actMap.put("ACT_NUM", act_Num);
                                List accountList = (List) sqlMap.executeQueryForList("getCrDrAccountDetails", actMap);
                                if (accountList != null && accountList.size() > 0) {
                                    actMap = (HashMap) accountList.get(0);
                                }else{
                                    throw new TTException("Invalid Account Number : " + act_Num);
                                }
                                prod_Type = CommonUtil.convertObjToStr(actMap.get("PROD_TYPE"));
                                if (transType.equals("DEBIT")) {
                                    txMap.put(TransferTrans.DR_AC_HD, acHD_ID);
                                    txMap.put(TransferTrans.DR_PROD_TYPE, prod_Type);
                                    txMap.put(TransferTrans.DR_PROD_ID, prod_ID);
                                    txMap.put(TransferTrans.DR_ACT_NUM, act_Num);
                                    txMap.put(TransferTrans.DR_BRANCH, act_Branch);
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.PARTICULARS, particulars);
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmount);
                                    if (prod_Type.equals("AD")) {
                                        transferTo.setAuthorizeRemarks("DP");
                                    }
                                    transferTo.setTransModType(prod_Type);
                                    transferTo.setScreenName(CommonUtil.convertObjToStr(map.get("SCREEN")));
                                    transferTo.setInstrumentNo1("");
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setTransType(CommonConstants.DEBIT);
                                    transferTo.setAcHdId(acHD_ID);
                                    transferTo.setTransDt(CurrDt);
                                    transferTo.setBranchId(act_Branch);
                                    transferTo.setInitiatedBranch(_branchCode);
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setGlTransActNum(act_Num);
                                    transactionDAO.setLinkBatchID(fileID);
                                    TxTransferTO.add(transferTo);
                                } else if (transType.equals("CREDIT")) {
                                    txMap.put(TransferTrans.CR_AC_HD, acHD_ID);
                                    txMap.put(TransferTrans.CR_PROD_TYPE, prod_Type);
                                    txMap.put(TransferTrans.CR_PROD_ID, prod_ID);
                                    txMap.put(TransferTrans.CR_ACT_NUM, act_Num);
                                    txMap.put(TransferTrans.CR_BRANCH, act_Branch);
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.PARTICULARS, particulars);
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmount);
                                    transferTo.setTransModType(prod_Type);
                                    transferTo.setScreenName(CommonUtil.convertObjToStr(map.get("SCREEN")));
                                    transferTo.setInstrumentNo1("");
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setTransType(CommonConstants.CREDIT);
                                    transferTo.setAcHdId(acHD_ID);
                                    transferTo.setTransDt(CurrDt);
                                    transferTo.setBranchId(act_Branch);
                                    transferTo.setInitiatedBranch(_branchCode);
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setGlTransActNum(act_Num);
                                    transactionDAO.setLinkBatchID(fileID);
                                    TxTransferTO.add(transferTo);
                                }
                            }
                        }
                        HashMap applnMap = new HashMap();
                        map.put("MODE", "INSERT");
                        map.put("COMMAND", "INSERT");
                        map.put("TxTransferTO", TxTransferTO);
                        map.put("LINK_BATCH_ID", fileID);
                        HashMap transMap = transferDAO.execute(map, false);
                        map.put("BATCH_ID", transMap.get("TRANS_ID"));
                        transMap = null;
                    }//Transaction End
                    returnMap.put("FILE_ID", fileID);
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
        map = null;
    }
    
    private void deleteData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            if(map.containsKey("INWARD_ID")){
                sqlMap.executeUpdate("deleteNachInwardMasterDetails", map);
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void authorize(HashMap map, HashMap allMap) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        HashMap pendingMap = new HashMap();
        HashMap inwardFileMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        String fileName = "";
        try {
            sqlMap.startTransaction();
            logTO.setData(map.toString());
            String authorizeStatus = CommonUtil.convertObjToStr(AuthMap.get("STATUS"));
            String linkBatchId = CommonUtil.convertObjToStr(AuthMap.get("FILE_ID"));
            HashMap transAuthMap = new HashMap();
            transAuthMap.put(CommonConstants.BRANCH_ID, _branchCode);
            transAuthMap.put(CommonConstants.USER_ID, allMap.get(CommonConstants.USER_ID));
            transAuthMap.put("DEBIT_LOAN_TYPE", "DEBIT_LOAN_TYPE"); //It will take automatically Debit Principle (DEBIT_LOAN_TYPE=DP)
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, transAuthMap);
            transAuthMap = null;
            sqlMap.executeUpdate("deleteDCBFileDetails", AuthMap);
            if(authorizeStatus.equals("REJECTED")){
                sqlMap.executeUpdate("deleteDCBFileName", AuthMap);
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }
    
    private void getCurrentFile(String fileName) throws Exception { // To Process File Moving ftom Server to UI
        try {
            if (fileName.length() > 0) {
                System.out.println("############# getCurrentFile FILE_NAME : " + fileName);
                String sourceFilePath = "E:/NACH/APBS/OUTWARD/" + fileName;
                System.out.println("#### sourceFilePath : " + sourceFilePath);
                File file = new File(sourceFilePath);
                final FileInputStream reader = new FileInputStream(file);
                final int size = reader.available();
                byte[] byteArray = new byte[size];
                reader.read(byteArray);
                returnMap.put("FILE_NAME", file);
                returnMap.put("FILE_CONTENT", byteArray);
                reader.close();
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }
    
    public String setStringSpaceRPad(String name, int maxLength) {
        String string = "";
        StringBuffer fieldName = new StringBuffer(name);
        int nameLength = fieldName.length();
        for (int j = 0; j < maxLength - nameLength; j++) {
            fieldName = fieldName.append(" ");
        }
        return String.valueOf(fieldName);
    }
    
    public String setStringSpaceLPad(String name, int maxLength) {
        StringBuffer fieldName = new StringBuffer(name);
        String temp = " ", returnTemp = "";
        int nameLength = fieldName.length();
        for (int j = 0; j < maxLength - nameLength; j++) {
            returnTemp = returnTemp+temp;
        }
        return String.valueOf(returnTemp+fieldName);
    }
    
    public String setStringZeroesRpad(String name, int maxLength) {
        StringBuffer fieldName = new StringBuffer(name);
        int nameLength = fieldName.length();
        for (int j = 0; j < maxLength - nameLength; j++) {
            fieldName = fieldName.append("0");
        }
        return String.valueOf(fieldName);
    }
    
    public String setStringZeroesLPad(String name, int maxLength) {
        StringBuffer fieldName = new StringBuffer(name);
        String temp = "0", returnTemp = "";
        int nameLength = fieldName.length();
        for (int j = 0; j < maxLength - nameLength; j++) {
            returnTemp = returnTemp+temp;
        }
        return String.valueOf(returnTemp+fieldName);
    }
    
    public void autoTransactionTransfer(List inwardList, String link_BatchID, HashMap allMap, String fileName) throws Exception { //This Method Created By Suresh R  17-Apr-2017
        if (inwardList != null && inwardList.size() > 0) {
            HashMap inwardMap = new HashMap();
            TransferTrans transferTrans = new TransferTrans();
            TxTransferTO transferTo = new TxTransferTO();
            ArrayList TxTransferTO = new ArrayList();
            HashMap txMap = new HashMap();
            String aadhaar_Number = "";
            String acHdTrans = "";
            String curTransType ="";
            HashMap updateMap = new HashMap();
            HashMap aadhaarMap = new HashMap();
            HashMap authorizeMap = new HashMap();
            transferTrans.setInitiatedBranch(_branchCode);
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            transactionDAO.setInitiatedBranch(_branchCode);
            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
            transactionDAO.setTransactionType(transactionDAO.TRANSFER);

            ArrayList arrList = new ArrayList();
            HashMap authMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            HashMap executeMap = new HashMap();
            //Debit A/c Head
            HashMap debitHeadMap = new HashMap();
            List debitHeadList = (List) sqlMap.executeQueryForList("getParameterDetails", debitHeadMap);
            if (debitHeadList != null && debitHeadList.size() > 0) {
                debitHeadMap = (HashMap) debitHeadList.get(0);
            }
            for (int i = 0; i < inwardList.size(); i++) {
                updateMap = new HashMap();
                inwardMap = new HashMap();
                aadhaarMap = new HashMap();
                TxTransferTO = new ArrayList();
                transferDAO = new TransferDAO(); //This line Should be Required.
                inwardMap = (HashMap) inwardList.get(i);
                authorizeMap = new HashMap();
                arrList = new ArrayList();
                authMap = new HashMap();
                singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put("STATUS", "AUTHORIZED");
                singleAuthorizeMap.put("INWARD_ID", link_BatchID);
                singleAuthorizeMap.put("AUTHORIZED_BY", allMap.get("USER_ID"));
                arrList.add(singleAuthorizeMap);
                authMap.put(CommonConstants.AUTHORIZESTATUS, "AUTHORIZED");
                authMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                System.out.println("################# inwardMap : " + inwardMap);
                aadhaar_Number = CommonUtil.convertObjToStr(inwardMap.get("AADHAAR_NUMBER"));
                updateMap.put("INWARD_ID", link_BatchID);
                updateMap.put("AADHAAR_NUMBER", aadhaar_Number);
                updateMap.put("FILE_STATUS", "FAIL");
                updateMap.put("REMARKS", "");
                updateMap.put("ACT_NUM", "");
                aadhaarMap.put("AADHAAR_NUMBER", aadhaar_Number);
                List aadhaarList = (List) sqlMap.executeQueryForList("getAadhaarAccountDetails", aadhaarMap);
                if ((aadhaarList != null && aadhaarList.size() > 0)
                        && CommonUtil.convertObjToDouble(inwardMap.get("AMOUNT")) > 0) {
                    aadhaarMap = (HashMap) aadhaarList.get(0);
                    //CREDIT
                    transferTo = new TxTransferTO();
                    txMap = new HashMap();
                    HashMap actMap = new HashMap();
                    actMap.put("ACT_NUM", CommonUtil.convertObjToStr(aadhaarMap.get("ACT_NUM")));
                    List creditAccountList = (List) sqlMap.executeQueryForList("getCrAccountDetails", actMap);
                    if (creditAccountList != null && creditAccountList.size() > 0) {
                        actMap = (HashMap) creditAccountList.get(0);
                        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(actMap.get("AC_HD_ID")));
                        txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(actMap.get("PROD_TYPE")));
                        txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(actMap.get("PROD_ID")));
                        txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(actMap.get("ACT_NUM")));
                        txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(actMap.get("BRANCH_ID")));
                    } else {
                        throw new TTException("Account Number Pending for Authorization : " + CommonUtil.convertObjToStr(aadhaarMap.get("ACT_NUM")));
                    }
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    //txMap.put(TransferTrans.PARTICULARS, " APB Inward Transaction");
                    txMap.put(TransferTrans.PARTICULARS, " APB - " + fileName + "||" + CommonUtil.convertObjToStr(inwardMap.get("USER_NAME")));//Referred By Mr Srinath 05-Jul-2017
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(inwardMap.get("AMOUNT")));
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setTransType(CommonConstants.CREDIT);
                    transferTo.setTransDt(CurrDt);
                    transferTo.setInitiatedBranch(_branchCode);
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(allMap.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(allMap.get("USER_ID")));
                    transferTo.setScreenName(CommonUtil.convertObjToStr(allMap.get(CommonConstants.SCREEN)));
                    transactionDAO.setLinkBatchID(link_BatchID);
                    TxTransferTO.add(transferTo);

                    //DEBIT
                    transferTo = new TxTransferTO();
                    txMap = new HashMap();
                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(debitHeadMap.get("APBS_AC_HD")));
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    //txMap.put(TransferTrans.PARTICULARS, " APB - "+CommonUtil.convertObjToStr(aadhaarMap.get("AADHAR_NO")));//Referred By Mr Srinath 05-Jul-2017
                    txMap.put(TransferTrans.PARTICULARS, " APB - " + fileName + "||" + CommonUtil.convertObjToStr(aadhaarMap.get("AADHAR_NO")) + "||" + CommonUtil.convertObjToStr(inwardMap.get("HOLDER_NAME")));//Referred By Mr Srinath 05-Jul-2017
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(inwardMap.get("AMOUNT")));
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setTransType(CommonConstants.DEBIT);
                    transferTo.setTransDt(CurrDt);
                    transferTo.setBranchId(_branchCode);
                    transferTo.setInitiatedBranch(_branchCode);
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(allMap.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(allMap.get("USER_ID")));
                    transferTo.setScreenName(CommonUtil.convertObjToStr(allMap.get(CommonConstants.SCREEN)));
                    transactionDAO.setLinkBatchID(link_BatchID);
                    TxTransferTO.add(transferTo);
                    //To Call Transfer DAO
                    executeMap = new HashMap();
                    executeMap.put("MODE", "INSERT");
                    executeMap.put("COMMAND", "INSERT");
                    executeMap.put("TxTransferTO", TxTransferTO);
                    executeMap.put("LINK_BATCH_ID", link_BatchID);
                    //System.out.println("##### authMap : "+authMap);
                    executeMap.put(CommonConstants.AUTHORIZEMAP, authMap);
                    executeMap.put("INITIATED_BRANCH", _branchCode);
                    executeMap.put(CommonConstants.BRANCH_ID, _branchCode);
                    executeMap.put(CommonConstants.USER_ID, allMap.get("USER_ID"));
                    executeMap.put("DO_NOT_UPDATE_GL_BALANCE", "DO_NOT_UPDATE_GL_BALANCE");
                    //System.out.println("############## Before Transfer Execute Method  : " + executeMap);
                    HashMap transMap = transferDAO.execute(executeMap, false);     //map Contains Authorize_Map, So automattically it will Authorize
                    System.out.println("############## After insert transMap : " + transMap);
                    executeMap.put("BATCH_ID", transMap.get("TRANS_ID"));
                    transMap = null;
                    updateMap.put("FILE_STATUS", "PASS");
                    updateMap.put("ACT_NUM", CommonUtil.convertObjToStr(actMap.get("ACT_NUM")));
                    returnMap.put("INWARD_ID", link_BatchID);
                } else {
                    //Suspense Account Credit
                    if (CommonUtil.convertObjToDouble(inwardMap.get("AMOUNT")) > 0) {
                        TxTransferTO = new ArrayList();
                        transferDAO = new TransferDAO();
                        arrList = new ArrayList();
                        authMap = new HashMap();
                        singleAuthorizeMap = new HashMap();
                        singleAuthorizeMap.put("STATUS", "AUTHORIZED");
                        singleAuthorizeMap.put("INWARD_ID", link_BatchID);
                        singleAuthorizeMap.put("AUTHORIZED_BY", allMap.get("USER_ID"));
                        arrList.add(singleAuthorizeMap);
                        authMap.put(CommonConstants.AUTHORIZESTATUS, "AUTHORIZED");
                        authMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                        //CREDIT
                        transferTo = new TxTransferTO();
                        txMap = new HashMap();
                        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(debitHeadMap.get("APBS_SUSPENSE_GL")));
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put(TransferTrans.PARTICULARS, " APB - " + fileName + "||" + CommonUtil.convertObjToStr(inwardMap.get("USER_NAME"))
                                + "||" + CommonUtil.convertObjToStr(aadhaar_Number));//Referred By Mr Srinath 20-Jul-2017
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(inwardMap.get("AMOUNT")));
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setTransType(CommonConstants.CREDIT);
                        transferTo.setTransDt(CurrDt);
                        transferTo.setInitiatedBranch(_branchCode);
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(allMap.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(allMap.get("USER_ID")));
                        transferTo.setScreenName(CommonUtil.convertObjToStr(allMap.get(CommonConstants.SCREEN)));
                        transactionDAO.setLinkBatchID(link_BatchID);
                        TxTransferTO.add(transferTo);

                        //DEBIT
                        transferTo = new TxTransferTO();
                        txMap = new HashMap();
                        txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(debitHeadMap.get("APBS_AC_HD")));
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put(TransferTrans.PARTICULARS, " APB - " + fileName + "||" + CommonUtil.convertObjToStr(aadhaar_Number));//Referred By Mr Srinath 20-Jul-2017
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(inwardMap.get("AMOUNT")));
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setTransType(CommonConstants.DEBIT);
                        transferTo.setTransDt(CurrDt);
                        transferTo.setBranchId(_branchCode);
                        transferTo.setInitiatedBranch(_branchCode);
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(allMap.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(allMap.get("USER_ID")));
                        transferTo.setScreenName(CommonUtil.convertObjToStr(allMap.get(CommonConstants.SCREEN)));
                        transactionDAO.setLinkBatchID(link_BatchID);
                        TxTransferTO.add(transferTo);
                        //To Call Transfer DAO
                        executeMap = new HashMap();
                        executeMap.put("MODE", "INSERT");
                        executeMap.put("COMMAND", "INSERT");
                        executeMap.put("TxTransferTO", TxTransferTO);
                        executeMap.put("LINK_BATCH_ID", link_BatchID);
                        executeMap.put(CommonConstants.AUTHORIZEMAP, authMap);
                        executeMap.put("INITIATED_BRANCH", _branchCode);
                        executeMap.put(CommonConstants.BRANCH_ID, _branchCode);
                        executeMap.put(CommonConstants.USER_ID, allMap.get("USER_ID"));
                        executeMap.put("DO_NOT_UPDATE_GL_BALANCE", "DO_NOT_UPDATE_GL_BALANCE");
                        HashMap transMap = transferDAO.execute(executeMap, false);
                    }
                    if (aadhaarList == null || aadhaarList.size() <= 0) {
                        updateMap.put("REMARKS", "AADHAAR_NUMBER_NOT_LINKED");
                    } else {
                        updateMap.put("REMARKS", "AMOUNT_NOT_EXISTS");
                    }
                }
                sqlMap.executeUpdate("updateInwardFileStatus", updateMap);
            }
            //Added Suresh R   Ref By Mr Abi        31-Jul-2017    GL Updation
            System.out.println("####### GL UPDATION STARTED : " +link_BatchID);
            HashMap GLupdateMap = new HashMap();
            GLupdateMap.put("BATCH_ID", link_BatchID);
            GLupdateMap.put("TRANS_DT", CurrDt);
            GLupdateMap.put(CommonConstants.BRANCH_ID, _branchCode);
            HashMap whereMap = new HashMap();
            GLTO objGLTO = new GLTO();
            List transferGLList = (List) sqlMap.executeQueryForList("getTransferDetailsGLAmount", GLupdateMap);
            if (transferGLList != null && transferGLList.size() > 0) {
                for (int i = 0; i < transferGLList.size(); i++) {
                    GLupdateMap = new HashMap();
                    GLupdateMap = (HashMap) transferGLList.get(i);
                    //System.out.println("####### GLupdateMap : " + GLupdateMap);
                    objGLTO = new GLTO();
                    objGLTO.setAcHdId(CommonUtil.convertObjToStr(GLupdateMap.get("AC_HD_ID")));
                    objGLTO.setCurBal(CommonUtil.convertObjToDouble(GLupdateMap.get("AMOUNT")));
                    objGLTO.setBranchCode(CommonUtil.convertObjToStr(GLupdateMap.get("BRANCH_ID")));
                    objGLTO.setInitiatedBranch(_branchCode);
                    objGLTO.setLastTransDt(CurrDt);
                    curTransType = CommonUtil.convertObjToStr(GLupdateMap.get("TRANS_TYPE"));
                    //To Check GL Balance Type
                    whereMap = new HashMap();
                    whereMap.put("ACCT_HEAD", objGLTO.getAcHdId());
                    whereMap.put("BRANCH_ID", objGLTO.getBranchCode());
                    GLTO objBranchGLTO = (GLTO) sqlMap.executeQueryForObject("getSelectGLTO", whereMap);
                    acHdTrans = CommonUtil.convertObjToStr(objBranchGLTO.getBalanceType());
                    objGLTO.setBalanceType(acHdTrans);
                    System.out.println("###### acHdTrans : "+acHdTrans+" ##### trans : "+curTransType+" ##### CurrAmount : "+objGLTO.getCurBal());
                    if (acHdTrans.equals(curTransType) && objGLTO.getCurBal().doubleValue() < 0) {
                        objGLTO.setCurBal(new Double(-1 * objGLTO.getCurBal().doubleValue()));
                    } else if (!acHdTrans.equals(curTransType) && objGLTO.getCurBal().doubleValue() > 0) {
                        objGLTO.setCurBal(new Double(-1 * objGLTO.getCurBal().doubleValue()));
                    }
                    sqlMap.executeUpdate("updateGLTO", objGLTO);
                }
            }
        }
    }

    public static String LPad(String str, Integer length, char car) {
      return (String.format("%" + length + "s", "").replace(" ", String.valueOf(car)) + str).substring(str.length(), length + str.length());
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("##### DCB File Upload DAO getData() : " + map);
        returnMap = new HashMap();
        List inwardDataList = (List) sqlMap.executeQueryForList("getDCBFileDataDetails", map);
        if (inwardDataList != null && inwardDataList.size() > 0) {
            returnMap.put("FILE_DATA", inwardDataList);
        }
        System.out.println("########### GetData returnMap : " + returnMap);
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        CurrDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        finalTableList = null;
    }
}
