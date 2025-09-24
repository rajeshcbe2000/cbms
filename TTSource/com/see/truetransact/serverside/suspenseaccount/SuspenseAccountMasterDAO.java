/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * SuspenseAccountMasterDAO.java
 *
 * Created on Fri Jun 10 15:52:50 IST 2011
 */
package com.see.truetransact.serverside.suspenseaccount;

import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
//import java.util.ArrayList;
import java.sql.SQLException;

import com.ibatis.db.sqlmap.SqlMap;

import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
//import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.transferobject.suspenseaccount.SuspenseAccountMasterTO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * SuspenseAccountMaster DAO.
 *
 */
public class SuspenseAccountMasterDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private SuspenseAccountMasterTO objTO;
    private String userID = "";
    private Date currDt = null;
    private HashMap whereConditions;
    private String whereCondition;
    private HashMap data;
    public String prod_id;
    private Map returnMap = null;
    boolean acNo = true;
    private TransactionDAO transactionDAO = null;
    TransferDAO transferDAO = new TransferDAO();
    private HashMap execReturnMap = null;

    /**
     * Creates a new instance of SuspenseAccountMasterDAO
     */
    public SuspenseAccountMasterDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectSuspenseAccountMasterTO", where);
        returnMap.put("SuspenseAccountMasterTO", list);
        return returnMap;
    }

    private void getSuspenseMaster_No() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "SUSPENSE_ACT_MASTER");
        String suspenseActNum = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        objTO.setTxtSuspenseActNum(suspenseActNum);
    }

    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            objTO.setStatusBy(userID);
            objTO.setStatusDt(currDt);
//            getSuspenseMaster_No();
            prod_id = objTO.getTxtSuspenseProdDescription();
            acNo = true;
            final String accountNumber = getAccountNumber();
            if (acNo) {
                objTO.setTxtSuspenseActNum(accountNumber);
                System.out.println("@#$@#$@#iNside insert objTO:" + objTO);
                sqlMap.executeUpdate("insertSuspenseAccountMaster", objTO);                
            }
            sqlMap.commitTransaction();
        } catch (SQLException e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    /*
     * this method is used to generate an account number on a fly when creating
     * a new account
     */
    private String getAccountNumber() throws Exception {
        HashMap where = new HashMap();
        String genID = null;
        HashMap mapData = new HashMap();
        String strPrefix = "";
        String strNum = "";
        int len = 13;
        where.put("PROD_ID", prod_id);
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        List lst = (List) sqlMap.executeQueryForList("getCoreBankNextActNum", where);
        if (lst != null && lst.size() > 0) {
            mapData = (HashMap) lst.get(0);
            if (mapData.containsKey("PREFIX")) {
                strPrefix = (String) mapData.get("PREFIX");
            }
            int numFrom = strPrefix.trim().length();
            String newID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))));
            System.out.println("@@@@@@@@" + newID);
            String nxtID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))) + 1);
            System.out.println("@@@@@@@@" + nxtID);
            genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
            where.put("VALUE", nxtID);
            sqlMap.executeUpdate("updateCoreBankNextActNum", where);
        } else {
            //Added By Suresh
            acNo = false;
//            returnMap.put("NO DATA IN BRANCH_ACNO_MAINTENANCE","NO DATA IN BRANCH_ACNO_MAINTENANCE");
        }
        return genID;
    }

    private void updateData(HashMap map,LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            objTO.setStatusBy(userID);
            objTO.setStatusDt(currDt);
            sqlMap.executeUpdate("updateSuspenseAccountMaster", objTO);
            if(map.containsKey("ACCOUNT_CLOSED_TRANSACTION")){
                    insertClosingTransaction(map,objLogDAO,objLogTO);
                    sqlMap.executeUpdate("updateSuspenseAccountUnAuthorized", objTO);
            }else if(map.containsKey("ACCOUNT_CLOSED_ONLY")){
                HashMap closeMap = new HashMap();
                closeMap.put("ACCT_STATUS", "CLOSED");
                closeMap.put("CLOSED_DT", currDt.clone());
                closeMap.put("ACT_NUM",objTO.getTxtSuspenseActNum());                     
                sqlMap.executeUpdate("updateClosedSuspenseAccount", closeMap);
            }
            sqlMap.commitTransaction();
        } catch (SQLException e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_DELETED);
            objTO.setStatusBy(userID);
            objTO.setStatusDt(currDt);
            sqlMap.executeUpdate("deleteSuspenseAccountMaster", objTO);            
            sqlMap.commitTransaction();
        } catch (SQLException e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            SuspenseAccountMasterDAO dao = new SuspenseAccountMasterDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        objTO = (SuspenseAccountMasterTO) map.get("SuspenseAccountMasterTO");
        System.out.println("#$%#$%inside execute:" + objTO);
        _branchCode = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
        userID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
        currDt = ServerUtil.getCurrentDate(_branchCode);
        HashMap returnMap = new HashMap();
        execReturnMap = new HashMap();
        // Log DAO
        LogDAO objLogDAO = new LogDAO();

        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId(userID);
        objLogTO.setBranchId(_branchCode);
        objLogTO.setSelectedBranchId(_branchCode);
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));

        final String command = objTO.getCommand();

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData();
            if (acNo == false) {
                returnMap.put("NO DATA IN BRANCH_ACNO_MAINTENANCE", "NO DATA IN BRANCH_ACNO_MAINTENANCE");
            }
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map,objLogDAO,objLogTO);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        } else {
            throw new NoCommandException();
        }
        objLogTO.setData(objTO.toString());
        objLogTO.setPrimaryKey(objTO.getKeyData());
        objLogDAO.addToLog(objLogTO);
        objLogTO = null;
        objLogDAO = null;
        map.clear();
        map = null;
        destroyObjects();
        makeDataNull();
        return execReturnMap;
    }
    
    private void insertClosingTransaction(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
                TransactionTO transactionTO = new TransactionTO();
                HashMap txMap = new HashMap();
                HashMap transMap = new HashMap();
                HashMap debitMap = new HashMap();
                HashMap prodMap = new HashMap();                
                System.out.println("map####"+map);
                System.out.println("ACT_NUM####"+CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                 System.out.println("objTO.getTxtSuspenseProdDescription()####"+objTO.getTxtSuspenseProdDescription());
                prodMap.put("prodId", CommonUtil.convertObjToStr(objTO.getTxtSuspenseProdDescription()));
                HashMap acHdmap = new HashMap();
                List acHdlst = sqlMap.executeQueryForList("getAccountHeadProdSAHead", prodMap);    // Acc Head
                if (acHdlst != null && acHdlst.size() > 0) {
                   acHdmap = (HashMap) acHdlst.get(0);
                }
                double totalExcessTransAmt = 0.0;
                String BRANCH_ID = _branchCode;
                String initiatedBranch = _branchCode;
                if (map.containsKey("TransactionTO")) {
                    HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                    if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                        allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                    }
                    transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                    String linkBatchId = CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo());
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")) {
                        debitMap.put("ACT_NUM", linkBatchId);
                        List lst = sqlMap.executeQueryForList("getAccNoProdIdDet", debitMap);
                        if (lst != null && lst.size() > 0) {
                            debitMap = (HashMap) lst.get(0);
                        }
                    }
                    if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL")) {
                        debitMap.put("prodId", transactionTO.getProductId());
                        List lst = sqlMap.executeQueryForList("TermLoan.getProdHead", debitMap);
                        if (lst != null && lst.size() > 0) {
                            debitMap = (HashMap) lst.get(0);
                        }
                    } 
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("SA")) {
                        debitMap.put("prodId", transactionTO.getProductId());
                        List lst = sqlMap.executeQueryForList("getAccountHeadProdSAHead", debitMap);
                        if (lst != null && lst.size() > 0) {
                            debitMap = (HashMap) lst.get(0);
                        }
                    }
                     if(transactionTO.getTransType().equals("CASH")){
                        CashTransactionTO objCashTO = new CashTransactionTO();
                        HashMap cashTransMap = new HashMap();
                        HashMap applicationMap = new HashMap();  
                        HashMap CashTransMap = new HashMap();
                        ArrayList cashList = new ArrayList();
                        cashTransMap.put("ACCT_NUM", objTO.getTxtSuspenseActNum());
                        cashTransMap.put("ACCT_HEAD", (String) acHdmap.get("AC_HD_ID"));
                        cashTransMap.put("PARTICULARS","By Suspense Account Closing : " + objTO.getTxtSuspenseActNum());
                        cashTransMap.put("PROD_TYPE", TransactionFactory.SUSPENSE);
                        cashTransMap.put("SELECTED_BRANCH_ID", BRANCH_ID);
                        cashTransMap.put("INITIATED_BRANCH", initiatedBranch);
                        cashTransMap.put("LINK_BATCH_ID", objTO.getTxtSuspenseActNum());
                        cashTransMap.put("PROD_ID", objTO.getTxtSuspenseProdDescription());
                        cashTransMap.put("BRANCH_CODE", BRANCH_ID);
                        cashTransMap.put("TOKEN_NO", "");
                        //cashTransMap.put("LOANDEBIT", "LOANCREDIT");
                        cashTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                        cashTransMap.put("USER_ID", (String) map.get(CommonConstants.USER_ID));
                        cashTransMap.put("TRANS_AMOUNT", transactionTO.getTransAmt());
                        cashTransMap.put("INSTRUMENT2","");
                        cashTransMap.put("REMARKS","");
                        cashTransMap.put("LOAN_HIERARCHY","1");
                        cashList.add(setCashTransactionValue(cashTransMap));
                        //To Cash Transaction
                        if(cashList!=null && cashList.size()>0) {
                            CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                            System.out.println("LOAN_CASH_TRANSACTION objCashTO Penal : " + cashList);
                            HashMap TransMap = new HashMap();
                            TransMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                            TransMap.put("DAILYDEPOSITTRANSTO", cashList);
                            TransMap.put("INITIATED_BRANCH", initiatedBranch);
                            TransMap.put(CommonConstants.BRANCH_ID, initiatedBranch);
                            TransMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                            System.out.println("transMap : " + TransMap);
                            transMap = cashTransactionDAO.execute(TransMap, false);
                            getTransDetails(objTO.getTxtSuspenseActNum());
                            System.out.println("execReturnMap##########"+execReturnMap);
                            //Remitt issue transaction
                            transactionTO.setBranchId(initiatedBranch);
                            transactionTO.setBatchId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                            transactionTO.setBatchDt(currDt);
                            transactionTO.setStatus(CommonConstants.STATUS_CREATED);
                            transactionTO.setTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                            sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", transactionTO);
                            //objCashTO = null;
                            transactionTO = null;
                        }
                    }else if (transactionTO.getTransType().equals("TRANSFER")) {
                            ArrayList transferList = new ArrayList();
                            TransferTrans transferTrans = new TransferTrans();
                            transferTrans.setInitiatedBranch(BRANCH_ID);
                            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                            transactionDAO.setInitiatedBranch(initiatedBranch);
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            HashMap tansactionMap = new HashMap();
                            TxTransferTO transferTo = new TxTransferTO();
                            ArrayList TxTransferTO = new ArrayList();
                            HashMap dataMap = new HashMap();
                            String loanAccNo = "";
                            loanAccNo = objTO.getTxtSuspenseActNum();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            if (transactionTO.getTransAmt() > 0) {
                                transferTrans.setInitiatedBranch(BRANCH_ID);
                                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                                transactionDAO.setInitiatedBranch(_branchCode);
                                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);                               
                                    txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTO.getTxtSuspenseActNum()));
                                    txMap.put(TransferTrans.DR_AC_HD,  acHdmap.get("AC_HD_ID"));
                                    //txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.SUSPENSE);
                                    txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTO.getTxtSuspenseProdDescription()));
                                    txMap.put(TransferTrans.PARTICULARS, "Suspense Account Closing : " + loanAccNo);
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                                    txMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
                                    txMap.put("TRANS_MOD_TYPE", CommonConstants.SUSPENSE_TRANSMODE_TYPE);
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, transactionTO.getTransAmt());
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setTransDt(currDt);
                                    transferTo.setBranchId(BRANCH_ID);
                                    transferTo.setInitiatedBranch(initiatedBranch);
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    //transferTo.setAuthorizeRemarks("DP");
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setStatusDt(currDt);
                                    transferTo.setLinkBatchId(loanAccNo);
                                    transferTo.setAuthorizeRemarks("");
                                    transferTo.setHierarchyLevel("1");
                                    transferTo.setInstrumentNo2("");
                                    transactionDAO.setLinkBatchID(loanAccNo);
                                    TxTransferTO.add(transferTo);
                                    //CREDIT
                                    transferTo = new TxTransferTO();
                                    txMap = new HashMap();
                                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL") && transactionTO.getProductId().equals("")) {
                                        System.out.println("$#$$$#$#$# Prod Type GL " + transactionTO.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_AC_HD, (String) transactionTO.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.PARTICULARS, "Suspense Account Closing : " + loanAccNo);
                                    } else if (!transactionTO.getProductType().equals("")) {
                                        System.out.println("$#$$$#$#$# Prod Type Not GL " + transactionTO.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_AC_HD, (String) debitMap.get("AC_HEAD"));
                                        txMap.put(TransferTrans.CR_ACT_NUM, transactionTO.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_PROD_ID, transactionTO.getProductId());
                                        txMap.put(TransferTrans.CR_PROD_TYPE, transactionTO.getProductType());
                                        txMap.put(TransferTrans.PARTICULARS, linkBatchId + "-" + ":SHG_PAYMENT");
                                    }
                                    txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
                                    txMap.put(TransferTrans.PARTICULARS, "Suspense Account Closing :  " + loanAccNo);
                                    txMap.put("TRANS_MOD_TYPE", transactionTO.getProductType());
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, transactionTO.getTransAmt());
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setTransDt(currDt);
                                    transferTo.setInitiatedBranch(initiatedBranch);
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setLinkBatchId(loanAccNo);
                                    transactionDAO.setLinkBatchID(loanAccNo);
                                    transactionTO.setChequeNo("SERVICE_TAX");
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setStatusDt(currDt);
                                    TxTransferTO.add(transferTo);
                                transferDAO = new TransferDAO();
                                tansactionMap.put("TxTransferTO", TxTransferTO);
                                tansactionMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                                tansactionMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                                tansactionMap.put(CommonConstants.BRANCH_ID, initiatedBranch);
                                System.out.println("################ tansactionMap :" + tansactionMap);
                                transMap = transferDAO.execute(tansactionMap, false);
                                getTransDetails(loanAccNo);
                        }
                        transactionTO.setBranchId(initiatedBranch);
                        transactionTO.setInitiatedBranch(initiatedBranch);
                        transactionTO.setBatchId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                        transactionTO.setBatchDt(currDt);
                        transactionTO.setStatus(CommonConstants.STATUS_CREATED);
                        transactionTO.setTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                        sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", transactionTO);                    
                    }                   
                }                
                objLogDAO.addToLog(objLogTO);
            
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
      
    private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        getTransMap.put("AUTHORIZE_STATUS", "AUTHORIZE_STATUS");
        //System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
        //returnMap = new HashMap();
        List transList = (List) sqlMap.executeQueryForList("getTransferDetails", getTransMap);
        if (transList != null && transList.size() > 0) {
            execReturnMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetails", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            execReturnMap.put("CASH_TRANS_LIST", cashList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }
        
    public CashTransactionTO setCashTransactionValue(HashMap txnMap) {
        //ArrayList cashTransactionList = new ArrayList();
        CashTransactionTO objCashTO = new CashTransactionTO();
        try {
            System.out.println("setCashTransactionValue txnMap : " + txnMap );
            //if (txnMap.containsKey("LOAN_PRINCIPLE_CASH_TRANSACTION")) {
                double TransAmount = CommonUtil.convertObjToDouble(txnMap.get("TRANS_AMOUNT"));
                if (TransAmount > 0) {
                    // = new ArrayList();
                    //CashTransactionTO objCashTO = new CashTransactionTO();
                    objCashTO.setAcHdId(CommonUtil.convertObjToStr(txnMap.get("ACCT_HEAD")));
                    objCashTO.setProdType(CommonUtil.convertObjToStr(txnMap.get("PROD_TYPE")));
                    if(objCashTO.getProdType().equals(TransactionFactory.SUSPENSE)){
                        objCashTO.setProdId(CommonUtil.convertObjToStr(txnMap.get("PROD_ID")));
                    }
                    objCashTO.setTransType(CommonConstants.DEBIT);
                    objCashTO.setActNum(CommonUtil.convertObjToStr(txnMap.get("ACCT_NUM")));
                    objCashTO.setBranchId(CommonUtil.convertObjToStr(txnMap.get("BRANCH_CODE")));
                    objCashTO.setStatusBy(CommonUtil.convertObjToStr(txnMap.get("USER_ID")));
                    objCashTO.setTransDt(currDt);
                    objCashTO.setStatusDt(currDt);
                    objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    objCashTO.setParticulars(CommonUtil.convertObjToStr(txnMap.get("PARTICULARS")));
                    objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(txnMap.get("INITIATED_BRANCH")));
                    objCashTO.setInitChannType(CommonConstants.CASHIER);
                    objCashTO.setInstType("VOUCHER");
                    objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                    objCashTO.setInpAmount(TransAmount);
                    objCashTO.setAmount(TransAmount);
                    objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(txnMap.get("LINK_BATCH_ID")));
                    objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(txnMap.get("ACCT_NUM")));
                    objCashTO.setScreenName("SUSPENSE_ACCOUNT_MASTER");
                    objCashTO.setTransModType(CommonConstants.SUSPENSE_TRANSMODE_TYPE);
                    objCashTO.setAuthorizeRemarks(CommonUtil.convertObjToStr(txnMap.get("REMARKS")));
                    objCashTO.setSingleTransId(CommonUtil.convertObjToStr(txnMap.get("SINGLE_TRANS_ID")));
                    objCashTO.setLoanHierarchy(CommonUtil.convertObjToStr(txnMap.get("LOAN_HIERARCHY")));
                    objCashTO.setInstrumentNo2(CommonUtil.convertObjToStr(txnMap.get("INSTRUMENT2")));
                    System.out.println("objCashTO############"+objCashTO);
                    //objCashTO = null;
                    //cashTransactionList = null;
                }
            //}
        }catch (Exception e) {
            e.printStackTrace();
        } 
        return objCashTO;
    }

    public HashMap executeQuery(HashMap condition) throws Exception {
        System.out.println("###condition" + condition);
        _branchCode = (String) condition.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        whereConditions = condition;
        if (condition.containsKey(CommonConstants.MAP_WHERE)) {
            whereCondition = (String) condition.get(CommonConstants.MAP_WHERE);
        }
        if (!condition.containsKey("AUTH_DATA")) {
            System.out.println("###whereconditions  SuspenseAccountMasterTO" + whereConditions);
            getSuspenseAccountData();
            makeNull();
        } else if (condition.containsKey("AUTH_DATA")) {
            System.out.println("@#$@#$@#$condition:" + condition);
            sqlMap.executeUpdate("authorizeSuspenseAccountMaster", condition);
            authorizeTransaction(condition);            
        }
        
        List list = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", condition);
        if (list != null && list.size() > 0) {
            data.put("TRANSACTION_LIST", list);
            list = null;
        }
        return data;

    }

    private void authorizeTransaction(HashMap map) throws Exception {
        System.out.println("######### map : " + map);
        String BRANCH_ID = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
        HashMap authMap = new HashMap();
        authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
        //ArrayList selectedList = (ArrayList) authMap.get(CommonConstants.AUTHORIZEDATA);
        //HashMap AuthorizeMap = new HashMap();
        //AuthorizeMap = (HashMap) selectedList.get(0);
        String status = CommonUtil.convertObjToStr(map.get(CommonConstants.STATUS));
        String linkBatchId = CommonUtil.convertObjToStr(map.get("SUSPENSE_ACCT_NUM"));
        transferDAO = new TransferDAO();
        double totalExcessTransAmt = 0.0;
        if (map.containsKey("TransactionTO")) {
            TransactionTO transactionTO = new TransactionTO();
            HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
            LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
            if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
            }
            transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
            totalExcessTransAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
            System.out.println("-------------------> totalExcessTransAmt : " + totalExcessTransAmt);   
                   
                System.out.println("linkBatchId :" + linkBatchId);
                System.out.println("-------------------> transactionTO : " + transactionTO);
                if (transactionTO.getTransType().equals("TRANSFER")) {
                    if (totalExcessTransAmt > 0) {
                        HashMap transferTransParam = new HashMap();
                        transferDAO = new TransferDAO();
                        transferTransParam.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                        transferTransParam.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                        transferTransParam.put("DEBIT_LOAN_TYPE", "DP");
                        transferTransParam.put("LINK_BATCH_ID", linkBatchId);
                        ArrayList transferTransList = (ArrayList) sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", transferTransParam);
                        if (transferTransList != null) {
                            String batchId = ((TxTransferTO) transferTransList.get(0)).getBatchId();
                            System.out.println("###@@# batchId : " + batchId);
                            HashMap transAuthMap = new HashMap();
                            transAuthMap.put("BATCH_ID", batchId);
                            transAuthMap.put(CommonConstants.AUTHORIZESTATUS, status);
                            transAuthMap.put(CommonConstants.AUTHORIZEDATA, transferTransList);
                            transferTransParam.put(CommonConstants.AUTHORIZEMAP, transAuthMap);
                            transferDAO.execute(transferTransParam, false);
                        }
                    }   
                }else if(transactionTO.getTransType().equals("CASH")){
                    System.out.println("transactionTO.getTransType() :" + transactionTO.getTransType());
                    System.out.println("transactionTO.getTransId() :" + transactionTO.getTransId());
                    CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                    ArrayList arrList = new ArrayList();
                    HashMap authorizeMap = new HashMap();
                    HashMap singleAuthorizeMap = new HashMap();
                    authorizeMap.put("LINK_BATCH_ID", linkBatchId);
                    authorizeMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                    authorizeMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                    authorizeMap.put("TRANS_DT", currDt);
                    ArrayList cashTransList = (ArrayList) sqlMap.executeQueryForList("getCashTransactionTOForAuthorzation", authorizeMap);
                    if (cashTransList != null) {
                        String transId = ((CashTransactionTO) cashTransList.get(0)).getTransId();
                        singleAuthorizeMap.put("AUTHORIZE_STATUS", status);
                        singleAuthorizeMap.put("STATUS", status);
                        singleAuthorizeMap.put("TRANS_ID", CommonUtil.convertObjToStr(transactionTO.getTransId()));
                        singleAuthorizeMap.put("USER_ID", map.get("USER_ID"));
                        singleAuthorizeMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                        singleAuthorizeMap.put("TRANS_ID",transId);
                        arrList.add(singleAuthorizeMap);
                        String branchCode = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
                        String userId = CommonUtil.convertObjToStr(map.get("USER_ID"));
                        System.out.println("before making new DAO map :" + map);
                        map = new HashMap();
                        map.put("SCREEN", "Cash");
                        map.put("USER_ID", userId);
                        map.put("SELECTED_BRANCH_ID", branchCode);
                        map.put("BRANCH_CODE", branchCode);
                        map.put("MODULE", "Transaction");
                        HashMap dataMap = new HashMap();
                        dataMap.put(CommonConstants.AUTHORIZESTATUS, status);
                        dataMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                        dataMap.put("DAILY", "DAILY");
                        map.put(CommonConstants.AUTHORIZEMAP, dataMap);
                        map.put("DEBIT_LOAN_TYPE", "DP");
                        System.out.println("before entering DAO map :" + map);
                        cashTransactionDAO.execute(map, false);
                        cashTransactionDAO = null;
                    }
                }    
                //Update Closed status
                if(status!=null && status.length()>0 && status.equals("AUTHORIZED")){
                  HashMap closeMap = new HashMap();
                  closeMap.put("ACCT_STATUS", "CLOSED");
                  closeMap.put("CLOSED_DT", currDt.clone());
                  closeMap.put("ACT_NUM",linkBatchId);                     
                  sqlMap.executeUpdate("updateClosedSuspenseAccount", closeMap);
                }else if(status!=null && status.length()>0 && status.equals("REJECTED")){
                    HashMap rejectMap = new HashMap();
                    rejectMap.put("STATUS","AUTHORIZED");   
                    rejectMap.put("AUTHORIZEDT",currDt.clone());
                    rejectMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                    rejectMap.put("SUSPENSE_ACCT_NUM",linkBatchId); 
                    sqlMap.executeUpdate("authorizeSuspenseAccountMaster", rejectMap);                    
                }
        }
        
          
    }
        
    private void makeDataNull() {
        data = null;
    }

    private void makeNull() {
        objTO = null;
    }

    private void getSuspenseAccountData() throws Exception {
        if (data == null) {
            data = new HashMap();
        }
        whereConditions.put("CURRENT_DT", currDt);
        System.out.println("#@$@#$whereConditions:" + whereConditions);
        List list = (List) sqlMap.executeQueryForList("getSelectSelectSuspenseAccountTO", whereConditions);
        System.out.println("@#$@#$@#4list :" + list);
        SuspenseAccountMasterTO objSuspenseAccountMasterTO = new SuspenseAccountMasterTO();
        if (list.size() > 0) {
            objSuspenseAccountMasterTO = (SuspenseAccountMasterTO) list.get(0);
            data.put("SuspenseAccountMasterTO", objSuspenseAccountMasterTO);
        }

        System.out.println("#@!$@#$@#$data:" + data);

    }

    private void destroyObjects() {
        objTO = null;
    }
}
