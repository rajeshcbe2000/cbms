/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SalaryStructureDAO.java
 *
 * Created on Wed May 26 10:59:57 GMT+05:30 2004
 */
package com.see.truetransact.serverside.share;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.share.DrfTransactionTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.TOHeader;

import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;

import com.see.truetransact.serverside.customer.CustomerHistoryDAO;
import com.see.truetransact.transferobject.customer.CustomerHistoryTO;
import com.see.truetransact.commonutil.AcctStatusConstants;
import com.see.truetransact.commonutil.LocaleConstants;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import java.sql.*;
import oracle.sql.*;
import org.apache.log4j.Logger;
import oracle.jdbc.driver.*;
import com.see.truetransact.commonutil.Dummy;
import com.see.truetransact.clientutil.ClientConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Date;
import java.util.Map;
import com.see.truetransact.transferobject.share.DrfRecoveryTO;

/**
 * DrfTransaction DAO.
 *
 * @author
 *
 */
public class DrfRecoveryDAO extends TTDAO {

    private String branchId;
    private SqlMap sqlMap;
    private HashMap data;
    private Iterator addressIterator;
    private DrfTransactionTO objDrfMasterTO;
    private DrfTransactionTO objDrfTransactionTO;
    private TransactionDAO transactionDAO = null;
    private String command;
    private String _userId = "";
    private HashMap drfMasterMap;
    private HashMap deletedDrfMasterMap;
    private String key;
    private LogDAO logDAO;
    private LogTO logTO;
    private final static Logger log = Logger.getLogger(DrfTransactionDAO.class);
    private String addressKey = new String();
    HashMap resultMap = new HashMap();
    Date currDt = null;
    private String whereCondition;
    private HashMap whereConditions;
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
    final String SCREEN = "CUS";
    private final String YES = "Y";
    private List list;
    private TransactionTO objTransactionTO;
    private Map returnMap;

    /**
     * Creates a new instance of DeductionDAO
     */
    public DrfRecoveryDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private void updateData() throws Exception {
        getAllTOs();
        logTO.setData(objDrfMasterTO.toString());
        logTO.setPrimaryKey(objDrfMasterTO.getKeyData());
        logTO.setStatus(objDrfMasterTO.getCommand());
        sqlMap.executeUpdate("updateDrfTransaction", objDrfMasterTO);
        processDrfMasterData(objDrfMasterTO.getCommand());
        final String USERID = logTO.getBranchId();
        logDAO.addToLog(logTO);
        makeDataNull();
        makeNull();

    }

    private void deleteData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        objDrfTransactionTO.setStatus(CommonConstants.STATUS_DELETED);
        try {
            sqlMap.startTransaction();
            objDrfTransactionTO.setStatusDate(currDt);
            sqlMap.executeUpdate("deleteDrfTransInfoTO", objDrfTransactionTO);
            insertDrfTransDetails(objLogDAO, objLogTO, map);
            // updateShareAccDetails() method call changed to insertShareAcctDetails()
            sqlMap.commitTransaction();
            objLogTO.setData(objDrfTransactionTO.toString());
            objLogTO.setPrimaryKey(objDrfTransactionTO.getKeyData());
            objLogDAO.addToLog(objLogTO);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            //            throw new TransRollbackException(e);
            throw e;
        }
    }

    private void makeDataNull() {
        data = null;
    }

    private void makeNull() {
        drfMasterMap = null;
        deletedDrfMasterMap = null;
        objDrfMasterTO = null;

    }

    private void processDrfMasterData(String command) throws Exception {

        if (deletedDrfMasterMap != null) {
            addressIterator = deletedDrfMasterMap.keySet().iterator();
            for (int i = deletedDrfMasterMap.size(); i > 0; i--) {
                key = (String) addressIterator.next();
                objDrfTransactionTO = (DrfTransactionTO) deletedDrfMasterMap.get(key);
                logTO.setData(objDrfTransactionTO.toString());
                logTO.setPrimaryKey(objDrfTransactionTO.getKeyData());
                logTO.setStatus(objDrfTransactionTO.getCommand());
                sqlMap.executeUpdate("deleteDrfTransaction", objDrfTransactionTO);
                logDAO.addToLog(logTO);
            }
            deletedDrfMasterMap = null;
        }
        if (drfMasterMap != null) {
            addressIterator = drfMasterMap.keySet().iterator();

            for (int i = drfMasterMap.size(); i > 0; i--) {
                addressKey = (String) addressIterator.next();
                objDrfTransactionTO = (DrfTransactionTO) drfMasterMap.get(addressKey);
                logTO.setData(objDrfTransactionTO.toString());
                logTO.setPrimaryKey(objDrfTransactionTO.getKeyData());
                logTO.setStatus(objDrfTransactionTO.getCommand());
                //if customer Id exists, set to customerAddressTO objectobj
                if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    if (objDrfTransactionTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                        sqlMap.executeUpdate("insertDrfTransactionDetails", objDrfTransactionTO);
                    } else {
                        sqlMap.executeUpdate("updateDrfMasterDetails", objDrfTransactionTO);
                    }

                } else if (command.equals(CommonConstants.TOSTATUS_INSERT)) {

                    sqlMap.executeUpdate("insertDrfTransactionDetails", objDrfTransactionTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    sqlMap.executeUpdate("deleteDrfTransaction", objDrfTransactionTO);

                }

                logDAO.addToLog(logTO);
            }
            drfMasterMap = null;
        }

    }

    private void getAllTOs() throws Exception {
        objDrfMasterTO = (DrfTransactionTO) data.get("DRFMASTERTO");

        if (data.containsKey("DELETEDDRFMASTER")) {
            deletedDrfMasterMap = (HashMap) data.get("DELETEDDRFMASTER");
        }
        if (data.containsKey("DRFMASTER")) {
            drfMasterMap = (HashMap) data.get("DRFMASTER");
        }
    }

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    public static void main(String str[]) {
        try {
            DrfTransactionDAO dao = new DrfTransactionDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        HashMap returnMap = new HashMap();
        //HashMap where = (HashMap) obj.get(CommonConstants.MAP_WHERE);
        String drftransid = obj.get("TRANS_ID").toString();
        List list = (List) sqlMap.executeQueryForList("getSelectDrfRec", obj);
        returnMap.put("DRFREC_LIST", list);
        HashMap getRemitTransMap = new HashMap();
        getRemitTransMap.put("TRANS_ID", drftransid);
        getRemitTransMap.put("TRANS_DT", currDt.clone());
        getRemitTransMap.put("BRANCH_CODE", _branchCode);
        list = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", getRemitTransMap);
        if (list != null && list.size() > 0) {
            returnMap.put("TRANSACTION_LIST", list);
        }

        return returnMap;
    }

    private void getDrfTableData() throws Exception {
        if (data == null) {
            data = new HashMap();
        }
        list = (List) sqlMap.executeQueryForList("getSelectDrfProuctTO", whereConditions);
        if (list.size() > 0) {
            drfMasterMap = new HashMap();
            for (int i = list.size(), j = 0; i > 0; i--, j++) {
                //                drfMasterMap.put( ((DrfTransactionTO)list.get(j)).getDrfSlNo(),list.get(j));
            }
            data.put("DRFPRODUCT", drfMasterMap);
        }
    }

    private void getDrfMasterData() throws Exception {
        if (data == null) {
            data = new HashMap();
        }
//        currDt = ServerUtil.getCurrentDate(_branchCode);
        whereConditions.put("CURRENT_DT", currDt);

        list = (List) sqlMap.executeQueryForList("getSelectDrfMasterTO", whereConditions);
        objDrfMasterTO = new DrfTransactionTO();
        if (list.size() > 0) {
            objDrfMasterTO = (DrfTransactionTO) list.get(0);
            data.put("DRFMASTERTO", objDrfMasterTO);
        }


    }

    private void makeQueryNull() {
        whereCondition = null;
        list = null;
    }

    public HashMap execute(HashMap map) throws Exception {
        //CommonUtil.serializeObjWrite("D:\\share.txt", map);
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        LogDAO objLogDAO = new LogDAO();
        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        branchId = objLogTO.getBranchId();
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));
        returnMap = new HashMap();
        if (map.containsKey("DrfTransactionTO")) {

            objDrfTransactionTO = (DrfTransactionTO) map.get("DrfTransactionTO");
            final TOHeader toHeader = objDrfTransactionTO.getTOHeader();
            command = CommonUtil.convertObjToStr(map.get("COMMAND"));
            //--- Selects the method according to the Command type
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData(map, objLogDAO, objLogTO);
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                //                updateData(map , objLogDAO, objLogTO);
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData(map, objLogDAO, objLogTO);

            } else {
                throw new NoCommandException();
            }
            destroyObjects();
        }

        if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            authMap.put(CommonConstants.BRANCH_ID, (String) map.get(CommonConstants.BRANCH_ID));
            authMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
            if (authMap != null) {
                authorize(authMap);
            }
        }
        return (HashMap) returnMap;
    }

    private void getDrfTrans_No() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "DRF_TRANSACTION");
        String drfTransID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        objDrfTransactionTO.setDrfTransID(drfTransID);
        returnMap.put("DRF_TRAN_ID", drfTransID);
    }

    public CashTransactionTO setCashTransaction(HashMap cashMap) {
        log.info("In setCashTransaction()");
        Date curDate = (Date) currDt.clone();
        final CashTransactionTO objCashTransactionTO = new CashTransactionTO();
        try {
            objCashTransactionTO.setAcHdId(CommonUtil.convertObjToStr(cashMap.get(CommonConstants.AC_HD_ID)));
            objCashTransactionTO.setProdType(TransactionFactory.GL);
            objCashTransactionTO.setInpAmount(CommonUtil.convertObjToDouble(cashMap.get("AMOUNT")));
            objCashTransactionTO.setAmount(CommonUtil.convertObjToDouble(cashMap.get("AMOUNT")));
            objCashTransactionTO.setTransType(CommonUtil.convertObjToStr(cashMap.get("TRANS_TYPE")));
            objCashTransactionTO.setBranchId(_branchCode);
            objCashTransactionTO.setStatusBy(CommonUtil.convertObjToStr(cashMap.get(CommonConstants.USER_ID)));
            //            objCashTransactionTO.setInstrumentNo1(cashTo.getInstrumentNo1());
            objCashTransactionTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            objCashTransactionTO.setInitTransId(CommonUtil.convertObjToStr(cashMap.get(CommonConstants.USER_ID)));
            objCashTransactionTO.setInitChannType("CASHIER");
            objCashTransactionTO.setParticulars("By " + CommonUtil.convertObjToStr(cashMap.get("LINK_BATCH_ID")));
            objCashTransactionTO.setInitiatedBranch(_branchCode);
            objCashTransactionTO.setLinkBatchId(CommonUtil.convertObjToStr(cashMap.get("LINK_BATCH_ID")));
            objCashTransactionTO.setAuthorizeRemarks(CommonUtil.convertObjToStr(cashMap.get("AUTHORIZEREMARKS")));
            objCashTransactionTO.setCommand(CommonConstants.TOSTATUS_INSERT);
        } catch (Exception e) {
            log.info("Error In setInwardClearing()");
            e.printStackTrace();
        }
        return objCashTransactionTO;
    }

    private void insertDrfTransDetails(LogDAO objLogDAO, LogTO objLogTO, HashMap map) throws Exception, Exception {
        try {


            if (objDrfTransactionTO.getCommand() != null) {
                if (objDrfTransactionTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                    getDrfTrans_No();
                    ArrayList tblList = new ArrayList();
                    tblList = (ArrayList) map.get("tblData");
                    for (int i = 0; i < tblList.size(); i++) {
                        DrfRecoveryTO objDRFRecTo = new DrfRecoveryTO();
                        List aList = (List) tblList.get(i);
                        objDRFRecTo.setDrfTransID(aList.get(0).toString());
                        objDRFRecTo.setLinkid(objDrfTransactionTO.getDrfTransID());
                        objDRFRecTo.setStatusDate(currDt);
                        sqlMap.executeUpdate("updateDrfRecovery", objDRFRecTo);
                    }

                    //                    drfAmt IS THE AMOUNT THAT IS COMING FROM THE TRANSACTION UI
                    double drfAmt = CommonUtil.convertObjToDouble(objDrfTransactionTO.getTxtDrfTransAmount()).doubleValue();
                    //                    productAmt IS THE RECIEPT AMOUNT
                    double productAmt = CommonUtil.convertObjToDouble(objDrfTransactionTO.getDrfProductAmount()).doubleValue();
                    //                    paymentAmt IS THE PAYMENT AMOUNT
                    double paymentAmt = CommonUtil.convertObjToDouble(objDrfTransactionTO.getDrfProdPaymentAmt()).doubleValue();
                    //                    ITS THE DIFFERENCE BETWEEN THE RECIEPT AMOUNT AND PAYMENT AMOUNT
                    double balanceAmount = drfAmt - productAmt;
                    HashMap txMap;

                    HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getDrfRecoveryHead", objDrfTransactionTO.getCboDrfTransProdID());

                    TransferTrans objTransferTrans = new TransferTrans();
                    objTransferTrans.setInitiatedBranch(_branchCode);

                    objTransferTrans.setLinkBatchId(objDrfTransactionTO.getDrfTransID());
                    txMap = new HashMap();
                    ArrayList transferList = new ArrayList();

                    LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    if (TransactionDetailsMap.size() > 0) {
                        if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                            LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                            for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                double debitAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                    //                                            txMap.put(TransferTrans.DR_AC_HD, (String)acHeads.get("SHARE_ACHD"));
                                    txMap.put(TransferTrans.PARTICULARS, objDrfTransactionTO.getDrfTransID());
                                    txMap.put(CommonConstants.USER_ID, objDrfTransactionTO.getStatusBy());
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                    HashMap interBranchCodeMap = new HashMap();
                                    interBranchCodeMap.put("ACT_NUM", CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                    List interBranchCodeList = sqlMap.executeQueryForList("getSelectInterBranchCode", interBranchCodeMap);
                                    if (interBranchCodeList != null && interBranchCodeList.size() > 0) {
                                        interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                                        System.out.println("interBranchCodeMap : " + interBranchCodeMap);
                                        txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE")));
                                    } else {
                                        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                    }
                                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    if (objTransactionTO.getProductType().equals("GL")) {
                                        txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                    } else {
                                        txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                    }
                                    txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, debitAmt));
                                    if (drfAmt > 0.0) {
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("RECOVERY_HEAD"));
                                        txMap.put("AUTHORIZEREMARKS", "RECOVERY_HEAD");
                                        //                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, shareAmt));
                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, drfAmt));
                                        //                                                objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                    }
                                    objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                } else {
                                    double transAmt;
                                    TransactionTO transTO = new TransactionTO();
                                    ArrayList cashList = new ArrayList();
                                    if (CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue() > 0) {
                                        txMap = new HashMap();
                                        txMap.put(CommonConstants.BRANCH_ID, _branchCode);
                                        txMap.put(CommonConstants.PRODUCT_ID, TransactionFactory.GL);
                                        txMap.put(CommonConstants.USER_ID, objDrfTransactionTO.getStatusBy());
                                        txMap.put("TRANS_TYPE", CommonConstants.CREDIT);
                                        if (drfAmt > 0.0) {
                                            txMap.put(CommonConstants.AC_HD_ID, (String) acHeads.get("RECOVERY_HEAD"));
                                            txMap.put("AUTHORIZEREMARKS", "RECOVERY_HEAD");
                                            txMap.put("AMOUNT", new Double(drfAmt));
                                            txMap.put("LINK_BATCH_ID", objTransferTrans.getLinkBatchId());
                                        }
                                        cashList.add(setCashTransaction(txMap));
                                        HashMap tranMap = new HashMap();
                                        tranMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                                        tranMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        tranMap.put(CommonConstants.IP_ADDR, CommonUtil.convertObjToStr(map.get("IP_ADDR")));
                                        tranMap.put(CommonConstants.MODULE, CommonUtil.convertObjToStr(map.get("MODULE")));
                                        tranMap.put(CommonConstants.SCREEN, CommonUtil.convertObjToStr(map.get("SCREEN")));
                                        tranMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
                                        CashTransactionDAO cashDao;
                                        cashDao = new CashTransactionDAO();
                                        tranMap = cashDao.execute(tranMap, false);
                                        cashDao = null;
                                        tranMap = null;
                                    }
                                }
                                objTransactionTO.setStatus(CommonConstants.STATUS_CREATED);
                                objTransactionTO.setBatchId(objDrfTransactionTO.getDrfTransID());
                                objTransactionTO.setBatchDt(currDt);
                                objTransactionTO.setTransId(objTransferTrans.getLinkBatchId());
                                objTransactionTO.setBranchId(_branchCode);
                                sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);
                            }
                        }
                    }
                    drfAmt = 0.0;
                    objTransferTrans = null;
                    transferList = null;
                    txMap = null;
                } else {
                    HashMap shareAcctNoMap = new HashMap();
                    double drfAmt = CommonUtil.convertObjToDouble(objDrfTransactionTO.getTxtDrfTransAmount()).doubleValue();
                    double productAmt = CommonUtil.convertObjToDouble(objDrfTransactionTO.getDrfProductAmount()).doubleValue();
                    double paymentAmt = CommonUtil.convertObjToDouble(objDrfTransactionTO.getDrfProdPaymentAmt()).doubleValue();
                    double balanceAmount = paymentAmt - drfAmt;
                    if (objDrfTransactionTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                        shareAcctNoMap = new HashMap();
                        shareAcctNoMap.put("LINK_BATCH_ID", objDrfTransactionTO.getDrfTransID());
                        List lst = sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", shareAcctNoMap);
                        TxTransferTO txTransferTO = null;
                        double oldAmount = 0;
                        HashMap oldAmountMap = new HashMap();
                        ArrayList transferList = new ArrayList();
                        if (lst != null && lst.size() > 0) {
                            for (int j = 0; j < lst.size(); j++) {
                                txTransferTO = (TxTransferTO) lst.get(j);
                            }

                        } else {
                            LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                            if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                                LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                if (allowedTransDetailsTO != null && allowedTransDetailsTO.size() > 0) {
                                    for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                        objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                        HashMap tempMap = new HashMap();
                                        List cLst1 = sqlMap.executeQueryForList("getSelectShareCashTransactionTO", CommonUtil.convertObjToStr(objTransactionTO.getTransId()));
                                        if (cLst1 != null && cLst1.size() > 0) {
                                            CashTransactionTO txTransferTO1 = null;
                                            txTransferTO1 = (CashTransactionTO) cLst1.get(0);
                                            oldAmount = CommonUtil.convertObjToDouble(txTransferTO1.getAmount()).doubleValue();
                                            double newAmount = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                            txTransferTO1.setInpAmount(new Double(newAmount));
                                            txTransferTO1.setAmount(new Double(newAmount));
                                            txTransferTO1.setCommand(command);
                                            txTransferTO1.setStatus(CommonConstants.STATUS_DELETED);
                                            txTransferTO1.setStatusDt(currDt);

                                            map.put("PRODUCTTYPE", TransactionFactory.GL);
                                            map.put("OLDAMOUNT", new Double(oldAmount));
                                            map.put("CashTransactionTO", txTransferTO1);
                                            CashTransactionDAO cashTransDAO = new CashTransactionDAO();
                                            cashTransDAO.execute(map, false);
                                        }
                                        cLst1 = null;
                                        //                                        }

                                        //                                                         for (int J = 1;J <= allowedTransDetailsTO.size();J++){

                                        objTransactionTO.setStatus(CommonConstants.STATUS_DELETED);
                                        objTransactionTO.setTransId(objDrfTransactionTO.getDrfTransID());
                                        objTransactionTO.setBatchId(objDrfTransactionTO.getDrfTransID());
                                        objTransactionTO.setBranchId(_branchCode);
                                        sqlMap.executeUpdate("updateRemittanceIssueTransactionTO", objTransactionTO);

                                    }

                                }

                                //                                }


                            }
                            lst = null;
                            oldAmountMap = null;
                            transferList = null;
                            shareAcctNoMap = null;
                            txTransferTO = null;
                        }
                    }
                    objLogTO.setData(objDrfTransactionTO.toString());
                    objLogTO.setPrimaryKey(objDrfTransactionTO.getKeyData());
                    objLogDAO.addToLog(objLogTO);
                }
                getTransDetails(objDrfTransactionTO.getDrfTransID());
            }
        } catch (Exception e) {
            //                sqlMap.rollbackTransaction();
            e.printStackTrace();
            //                throw new TransRollbackException(e);
            throw e;
        }
    }

    private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        //        returnMap = new HashMap();
        List transList = (List) sqlMap.executeQueryForList("getTransferDetails", getTransMap);
        if (transList != null && transList.size() > 0) {
            returnMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetails", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            returnMap.put("CASH_TRANS_LIST", cashList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    private void insertData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {

            sqlMap.startTransaction();
            objDrfTransactionTO.setStatus(CommonConstants.STATUS_CREATED);
            objLogTO.setData(objDrfTransactionTO.toString());
            objLogTO.setPrimaryKey(objDrfTransactionTO.getKeyData());
            objLogDAO.addToLog(objLogTO);


            insertDrfTransDetails(objLogDAO, objLogTO, map);
            final String USERID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));


            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void authorize(HashMap map) throws Exception {

        String status = (String) map.get(CommonConstants.AUTHORIZESTATUS);
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap dataMap;
        String DrfTransID = null;
        DrfTransactionTO objTO = null;
        String linkBatchId = null;
        HashMap cashAuthMap;

        try {
            sqlMap.startTransaction();
            for (int i = 0; i < selectedList.size(); i++) {
                dataMap = (HashMap) selectedList.get(i);
                DrfTransID = CommonUtil.convertObjToStr(dataMap.get("DRF_TRANS_ID"));
                dataMap.put(CommonConstants.STATUS, status);
                dataMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                dataMap.put("CURR_DATE", currDt);
                sqlMap.executeUpdate("authorizeDrfRec", dataMap);
                if (status.equals("") || status == null) {
                    sqlMap.executeUpdate("RejectDrfRec", dataMap);
                }
                linkBatchId = CommonUtil.convertObjToStr(dataMap.get("DRF_TRANS_ID"));//Transaction Batch Id
                //Separation of Authorization for Cash and Transfer
                //Call this in all places that need Authorization for Transaction
                cashAuthMap = new HashMap();
                cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                cashAuthMap.put("DAILY", "DAILY");
                // The following line commented because no need of doing interbranch transaction in GL Transaction
                //                cashAuthMap.put("PRODUCT", "SHARE");
                TransactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
                HashMap transMap = new HashMap();
                transMap.put("LINK_BATCH_ID", linkBatchId);
                sqlMap.executeUpdate("updateInstrumentNO1Transfer", transMap);
                sqlMap.executeUpdate("updateInstrumentNO1Cash", transMap);
                transMap = null;
                objTransactionTO = new TransactionTO();
                objTransactionTO.setBatchId(CommonUtil.convertObjToStr(DrfTransID));
                objTransactionTO.setTransId(CommonUtil.convertObjToStr(linkBatchId));
                objTransactionTO.setBranchId(_branchCode);
                sqlMap.executeUpdate("deleteRemittanceIssueTransactionTO", objTransactionTO);
            }
            if (!status.equals("REJECTED")) {
                //                sqlMap.executeUpdate("upDateNoOfShareAndAmount",map);
            }
            selectedList = null;
            dataMap = null;
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }

    }

    private void deleteData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void destroyObjects() {
        objTransactionTO = null;
        objDrfTransactionTO = null;
        drfMasterMap = null;
        deletedDrfMasterMap = null;
    }
}
