/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SHGTransactionDAO.java
 *
 * Created on June 18, 2011, 4:14 PM
 */
package com.see.truetransact.serverside.share.pensionScheme;

import com.see.truetransact.serverside.termloan.groupLoan.*;
import com.see.truetransact.serverside.termloan.SHG.*;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.common.nominee.NomineeDAO;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.operativeaccount.AccountTO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;


// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.generalledger.GLOpeningUpdateTO;
import com.see.truetransact.transferobject.share.PensionSchemeTO;
import com.see.truetransact.transferobject.termloan.groupLoan.GroupLoanTO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This is used for OperativeAcctProductDAO Data Access.
 *
 * @author Sreekrishnan
 *
 *
 */
public class PensionSchemeDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private TransactionDAO transactionDAO = null;
    TransferDAO transferDAO = new TransferDAO();
    NomineeDAO objNomineeDAO = new NomineeDAO();
    private AccountTO accountTO;
    private Date currDt = null;
    final String SCREEN = "TD";
    public List pensionList = null;
    public List groupLoanPaymentList = null;
    private HashMap TermLoanCloseCharge = new HashMap();
    private HashMap execReturnMap = null;
    private String narration = "";
    GroupLoanTO GroupLoanTO = new GroupLoanTO();
    public List dailyGroupLoanList = null;
    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public PensionSchemeDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("########" + map);
        HashMap transMap = new HashMap();
        //String where = (String) map.get("TRANS_ID");
        HashMap narrationMap = new HashMap();
        if(map.containsKey("PURCHASE_DT")){       
            List list = (List) sqlMap.executeQueryForList("getSelectGroupLoanTO", map);
            if (list != null && list.size() > 0) {
                narrationMap = (HashMap) list.get(0);
                transMap.put("PURCHASE_DT", narrationMap.get("TRANS_DT"));
                list = null;
            }
        }
        //if(map.containsKey("FOR_NARRATION")){       
        List list = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", map);
        if (list != null && list.size() > 0) {
            transMap.put("TRANSACTION_LIST", list);
            list = null;
        }
       // }
        System.out.println("transMap%#^#^"+transMap);
        return transMap;
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        System.out.println("Map in PensionSchemeDAO  DAO: " + map);
        // Log DAO
        LogDAO objLogDAO = new LogDAO();
        execReturnMap = new HashMap();
        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        System.out.println("$#$#$#$#$ : Command : " + command);
        if (map.containsKey("PENSION_SCHEME_DATA")) {
            try {
                sqlMap.startTransaction();
                objLogTO.setStatus(command);
                if (map.containsKey("PENSION_SCHEME_DATA")){
                    pensionList = (List) map.get("PENSION_SCHEME_DATA");
                }                             
                System.out.println("@##$#$% pensionList #### :" + pensionList);
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    if(pensionList!=null && pensionList.size()>0){
                        if(map.containsKey("MULTIPLE_MEMBER_PENSION_POSTING") && map.get("MULTIPLE_MEMBER_PENSION_POSTING") != null && map.get("MULTIPLE_MEMBER_PENSION_POSTING").equals("MULTIPLE_MEMBER_PENSION_POSTING")){
                            System.out.println("Executing for multiple pension transfer"); 
                            insertDataForMultipleTransfer(map, objLogDAO, objLogTO);
                        }else{
                          insertData(map, objLogDAO, objLogTO);  
                        }                        
                    }     
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData(map, objLogDAO, objLogTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData(map, objLogDAO, objLogTO);
                } else if (map.containsKey("AUTHORIZEMAP")) {                    
                    authorize(map);
                } else {
                    throw new NoCommandException();
                }
                sqlMap.commitTransaction();
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw e;
            }
            objLogDAO = null;
            objLogTO = null;
            destroyObjects();

        }
        return execReturnMap;
    }

    private String getSHGTransId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "SHG_TRANS_ID");
        String shgTransId = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return shgTransId;
    }
    
    private void insertData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            if (pensionList != null && pensionList.size() > 0) {
                String generateCashId = generateLinkID();
                String shgTransID = "";
                shgTransID = getSHGTransId();
                //execReturnMap.put("SHG_TRANS_ID", shgTransID);
                String group_id = "";
                group_id = CommonUtil.convertObjToStr(map.get("GROUP_ID"));
                TransactionTO transactionTO = new TransactionTO();
                HashMap txMap = new HashMap();
                HashMap transMap = new HashMap();
                HashMap debitMap = new HashMap();
                HashMap prodMap = new HashMap();
                double principleAmt = 0.0;
                double receiptAmt = 0.0;
                double interestAmt = 0.0;
                double penalAmt = 0.0;
                double chargeAmt = 0.0;
                double totDueAmt = 0.0;
                double paymentAmt = 0.0;
                double totPrincple = 0.0;
                double totIntAmt = 0.0;
                double totpenalAmt = 0.0;
                double totchargeAmt = 0.0;
                double firstRec = 0.0;
                System.out.println("map####"+map); 
                double totalExcessTransAmt = 0.0;                
                String initiatedBranch = CommonUtil.convertObjToStr(map.get("INITIATED_BRANCH"));
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
                    for (int k = 0; k < pensionList.size(); k++) {
                        HashMap dataMap = new HashMap();
                        dataMap = (HashMap) pensionList.get(k);              
                        System.out.println("dataMap####"+dataMap);
                        String BRANCH_ID = CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID"));
                        if(transactionTO.getTransType().equals("CASH")){
                            CashTransactionTO objCashTO = new CashTransactionTO();
                            HashMap cashTransMap = new HashMap();
                            HashMap applicationMap = new HashMap();  
                            HashMap CashTransMap = new HashMap();
                            ArrayList cashList = new ArrayList();                        
                            if(CommonUtil.convertObjToDouble(dataMap.get("MIN_PENSION"))>0){
                                if(!CommonUtil.convertObjToStr(dataMap.get("PENSION_PROD_TYPE")).equals("")&& CommonUtil.convertObjToStr(dataMap.get("PENSION_PROD_TYPE")).equals("GL")){
                                    cashTransMap.put("ACCT_HEAD", CommonUtil.convertObjToStr(dataMap.get("PENSION_AC_HD"))); 
                                    cashTransMap.put("PROD_TYPE", TransactionFactory.GL);
                                    cashTransMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                }else if(!CommonUtil.convertObjToStr(dataMap.get("PENSION_PROD_TYPE")).equals("") && !CommonUtil.convertObjToStr(dataMap.get("PENSION_PROD_TYPE")).equals("GL")){
                                    cashTransMap.put("PROD_TYPE", CommonUtil.convertObjToStr(dataMap.get("PENSION_PROD_TYPE")));
                                    cashTransMap.put("ACCT_NUM", CommonUtil.convertObjToStr(dataMap.get("PENSION_AC_HD"))); 
                                    cashTransMap.put("TRANS_MOD_TYPE", CommonUtil.convertObjToStr(dataMap.get("PENSION_PROD_TYPE")));                                                              
                                    cashTransMap.put("PROD_ID", CommonUtil.convertObjToStr(dataMap.get("PENSION_PROD_ID")));
                                }
                                if(transactionTO.getParticulars()!=null && transactionTO.getParticulars().length()>0){
                                    cashTransMap.put("PARTICULARS",transactionTO.getParticulars());  
                                }else{
                                    cashTransMap.put("PARTICULARS","By SHARE PENSION SCHEME");
                                }
                                cashTransMap.put("SELECTED_BRANCH_ID", BRANCH_ID);
                                cashTransMap.put("INITIATED_BRANCH", initiatedBranch);  
                                cashTransMap.put("BRANCH_CODE", BRANCH_ID);
                                cashTransMap.put("TOKEN_NO", "");
                                cashTransMap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(dataMap.get("SHARE_ACCT_NO")));
                                cashTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                cashTransMap.put("USER_ID", (String) map.get(CommonConstants.USER_ID));
                                cashTransMap.put("TRANS_AMOUNT", CommonUtil.convertObjToDouble(dataMap.get("MIN_PENSION")));
                                cashTransMap.put("SINGLE_TRANS_ID",generateCashId);
                                cashTransMap.put("LOAN_HIERARCHY","1"); 
                                cashTransMap.put(CommonConstants.SCREEN,(String) map.get(CommonConstants.SCREEN));
                                cashList.add(setCashTransactionValue(cashTransMap));
                            }                        
                        //To Cash Transaction
                        if(cashList!=null && cashList.size()>0) {
                            CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                            System.out.println("LOAN_CASH_TRANSACTION objCashTO Penal : " + cashList);
                            HashMap TransMap = new HashMap();
                            TransMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                            TransMap.put("DAILYDEPOSITTRANSTO", cashList);
                            //TransMap.put("INITIATED_BRANCH", initiatedBranch);
                            TransMap.put(CommonConstants.BRANCH_ID, initiatedBranch);
                            TransMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                            System.out.println("transMap : " + TransMap);
                            transMap = cashTransactionDAO.execute(TransMap, false);
                            getTransDetails(CommonUtil.convertObjToStr(dataMap.get("SHARE_ACCT_NO")));
                            System.out.println("execReturnMap##########"+execReturnMap);
                            //Remitt issue transaction
                            transactionTO.setBranchId(initiatedBranch);
                            transactionTO.setBatchId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                            transactionTO.setBatchDt(currDt);
                            transactionTO.setStatus(CommonConstants.STATUS_CREATED);
                            transactionTO.setTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                            transactionTO.setTransAmt(CommonUtil.convertObjToDouble(dataMap.get("MIN_PENSION")));
                            sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", transactionTO);
                            //objCashTO = null;
                            transactionTO = null;
                        }
                    }else if (transactionTO.getTransType().equals("TRANSFER")) {
                            ArrayList transferList = new ArrayList();
                            TransferTrans transferTrans = new TransferTrans();
                            transferTrans.setInitiatedBranch(initiatedBranch);
                            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                            transactionDAO.setInitiatedBranch(initiatedBranch);
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            HashMap tansactionMap = new HashMap();
                            TxTransferTO transferTo = new TxTransferTO();
                            ArrayList TxTransferTO = new ArrayList();
                            double paymentAmtTran = firstRec;
                            double totAmtTran = firstRec;
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            if (transactionTO.getTransAmt() > 0) {
                                transferTrans.setInitiatedBranch(initiatedBranch);
                                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                                transactionDAO.setInitiatedBranch(_branchCode);
                                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                //CREDIT CHARGES
                                if(CommonUtil.convertObjToDouble(dataMap.get("MIN_PENSION"))>0){
                                    if(!CommonUtil.convertObjToStr(dataMap.get("PENSION_PROD_TYPE")).equals("")&& CommonUtil.convertObjToStr(dataMap.get("PENSION_PROD_TYPE")).equals("GL")){
                                        txMap.put(TransferTrans.DR_AC_HD,CommonUtil.convertObjToStr(dataMap.get("PENSION_AC_HD")));
                                        txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(dataMap.get("PENSION_PROD_TYPE")));
                                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                    }else if(!CommonUtil.convertObjToStr(dataMap.get("PENSION_PROD_TYPE")).equals("") && !CommonUtil.convertObjToStr(dataMap.get("PENSION_PROD_TYPE")).equals("GL")){
                                        txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(dataMap.get("PENSION_AC_HD"))); 
                                        txMap.put("TRANS_MOD_TYPE", CommonUtil.convertObjToStr(dataMap.get("PENSION_PROD_TYPE")));                                                              
                                        txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(dataMap.get("PENSION_PROD_ID")));
                                        txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(dataMap.get("PENSION_PROD_TYPE")));
                                    }
                                    if(transactionTO.getParticulars()!=null && transactionTO.getParticulars().length()>0){
                                        txMap.put(TransferTrans.PARTICULARS, transactionTO.getParticulars());    
                                    }else{
                                        txMap.put(TransferTrans.PARTICULARS, "By SHARE PENSION SCHEME");
                                    }
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                                    txMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
                                    txMap.put("SCREEN_NAME", "SHARE PENSION SCHEME");
                                    txMap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(dataMap.get("SHARE_ACCT_NO")));
                                    txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(dataMap.get("MIN_PENSION")));
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setTransDt(currDt);
                                    transferTo.setBranchId(BRANCH_ID);
                                    transferTo.setInitiatedBranch(initiatedBranch);
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("SHARE_ACCT_NO")));
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setStatusDt(currDt);
                                    transferTo.setHierarchyLevel("1");
                                    TxTransferTO.add(transferTo);
                                }
                                //DEBIT
                                if (transactionTO.getTransAmt() > 0) {
                                    transferTo = new TxTransferTO();
                                    txMap = new HashMap();
                                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL") && transactionTO.getProductId().equals("")) {
                                        System.out.println("$#$$$#$#$# Prod Type GL " + transactionTO.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_AC_HD, (String) transactionTO.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put("TRANS_MOD_TYPE", transactionTO.getProductType());
                                        txMap.put(TransferTrans.CR_BRANCH, initiatedBranch);
                                    } else if(!transactionTO.getProductType().equals("")) {
                                        System.out.println("$#$$$#$#$# Prod Type Not GL " + transactionTO.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_AC_HD, (String) debitMap.get("AC_HEAD"));
                                        txMap.put(TransferTrans.CR_ACT_NUM, transactionTO.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_PROD_ID, transactionTO.getProductId());
                                        txMap.put(TransferTrans.CR_PROD_TYPE, transactionTO.getProductType());
                                        txMap.put("TRANS_MOD_TYPE", transactionTO.getProductType());
                                        txMap.put(TransferTrans.CR_BRANCH, transactionTO.getBranchId());
                                    }
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
                                    if(transactionTO.getParticulars()!=null && transactionTO.getParticulars().length()>0){
                                        txMap.put(TransferTrans.PARTICULARS, transactionTO.getParticulars());    
                                    }else{
                                        txMap.put(TransferTrans.PARTICULARS, "To SHARE PENSION SCHEME");
                                    }                                 
                                    txMap.put("SCREEN_NAME", "SHARE PENSION SCHEME");
                                    txMap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(dataMap.get("SHARE_ACCT_NO")));
                                    txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(dataMap.get("MIN_PENSION")));
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setTransDt(currDt);
                                    transferTo.setInitiatedBranch(initiatedBranch);
                                    transferTo.setBranchId(transactionTO.getBranchId());
                                    transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("SHARE_ACCT_NO")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID"))); 
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setStatusDt(currDt);
                                    TxTransferTO.add(transferTo);
                                }
                                transferDAO = new TransferDAO();
                                tansactionMap.put("TxTransferTO", TxTransferTO);
                                tansactionMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                                tansactionMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                                tansactionMap.put(CommonConstants.BRANCH_ID, initiatedBranch);
                                System.out.println("################ tansactionMap :" + tansactionMap);
                                transMap = transferDAO.execute(tansactionMap, false);
                                getTransDetails(CommonUtil.convertObjToStr(dataMap.get("SHARE_ACCT_NO")));
                        }
                        transactionTO.setBranchId(initiatedBranch);
                        //transactionTO.setInitiatedBranch(initiatedBranch);
                        transactionTO.setBatchId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                        transactionTO.setBatchDt(currDt);
                        transactionTO.setStatus(CommonConstants.STATUS_CREATED);
                        transactionTO.setTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                        transactionTO.setTransAmt(CommonUtil.convertObjToDouble(dataMap.get("MIN_PENSION")));
                        sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", transactionTO);                    
                }
                   //To pension trans table
                if(pensionList!=null && pensionList.size()>0){
                  dataMap.put(CommonConstants.BRANCH_ID, initiatedBranch);  
                  dataMap.put("SHARE_ACCT_NO", CommonUtil.convertObjToStr(dataMap.get("SHARE_ACCT_NO"))); 
                  dataMap.put("TRANS_AMOUNT", CommonUtil.convertObjToStr(dataMap.get("MIN_PENSION"))); 
                  dataMap.put("SHARE_RUN_PERIOD", CommonUtil.convertObjToStr(dataMap.get("DURATION"))); 
                  dataMap.put("CUST_AGE",CommonUtil.convertObjToStr(dataMap.get("AGE"))); 
                  dataMap.put("TRANS_DT", currDt.clone());   
                  dataMap.put("TRANS_ID", CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));  
                  dataMap.put("TRANS_TYPE", CommonConstants.DEBIT); 
                  dataMap.put("STATUS", CommonConstants.STATUS_CREATED); 
                  dataMap.put("STATUS_BY", (String) map.get(CommonConstants.USER_ID)); 
                  dataMap.put("STATUS_DT", currDt.clone());                        
                  insertPensionScheme(dataMap);         
                }
              }    
            }
            pensionList = null;
            objLogDAO.addToLog(objLogTO);
            }
            
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    } 
    
    // Added by nithya for KDSA - 118 0009951: Member pension bulk posting required
     private void insertDataForMultipleTransfer(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
         try {
             if (pensionList != null && pensionList.size() > 0) {
                 String generateCashId = generateLinkID();
                 String shgTransID = "";
                 double totalPayment = 0.0;
                 shgTransID = getSHGTransId();
                 String group_id = "";
                 group_id = CommonUtil.convertObjToStr(map.get("GROUP_ID"));
                 TransactionTO transactionTO = new TransactionTO();
                 HashMap txMap = new HashMap();
                 HashMap transMap = new HashMap();
                 HashMap debitMap = new HashMap();
                 double firstRec = 0.0;
                 //System.out.println("map####" + map);
                 String initiatedBranch = CommonUtil.convertObjToStr(map.get("INITIATED_BRANCH"));
                 totalPayment = CommonUtil.convertObjToDouble(map.get("TOTAL_FOR_MULTIPLE_TRANSFER"));
                 System.out.println("TOTAL_FOR_MULTIPLE_TRANSFER"+map.get("TOTAL_FOR_MULTIPLE_TRANSFER"));
                 System.out.println("totalPayment outside... "+ totalPayment);
                 HashMap debitDataMap = new HashMap();
                 debitDataMap = (HashMap) pensionList.get(0);
                 //System.out.println("dataMap####" + debitDataMap);
                 String BRANCH_ID = CommonUtil.convertObjToStr(debitDataMap.get("BRANCH_ID"));
                 ArrayList transferList = new ArrayList();
                 TransferTrans transferTrans = new TransferTrans();
                 transferTrans.setInitiatedBranch(initiatedBranch);
                 transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                 transactionDAO.setInitiatedBranch(initiatedBranch);
                 transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                 transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                 HashMap tansactionMap = new HashMap();
                 TxTransferTO transferTo = new TxTransferTO();
                 ArrayList TxTransferTO = new ArrayList();
                 double paymentAmtTran = firstRec;
                 double totAmtTran = firstRec;
                 transferTo.setInstrumentNo2("APPL_GL_TRANS");
                 transferTrans.setInitiatedBranch(initiatedBranch);
                 transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                 transactionDAO.setInitiatedBranch(_branchCode);
                 transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                 transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                 //CREDIT CHARGES
                 if (totalPayment > 0) {
                     //System.out.println("executing for debit..");
                     if (!CommonUtil.convertObjToStr(debitDataMap.get("PENSION_PROD_TYPE")).equals("") && CommonUtil.convertObjToStr(debitDataMap.get("PENSION_PROD_TYPE")).equals("GL")) {
                         txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(debitDataMap.get("PENSION_AC_HD")));
                         txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(debitDataMap.get("PENSION_PROD_TYPE")));
                         txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                     } else if (!CommonUtil.convertObjToStr(debitDataMap.get("PENSION_PROD_TYPE")).equals("") && !CommonUtil.convertObjToStr(debitDataMap.get("PENSION_PROD_TYPE")).equals("GL")) {
                         txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(debitDataMap.get("PENSION_AC_HD")));
                         txMap.put("TRANS_MOD_TYPE", CommonUtil.convertObjToStr(debitDataMap.get("PENSION_PROD_TYPE")));
                         txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(debitDataMap.get("PENSION_PROD_ID")));
                         txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(debitDataMap.get("PENSION_PROD_TYPE")));
                     }
                     txMap.put(TransferTrans.PARTICULARS, "By SHARE PENSION SCHEME");
                     txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                     txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                     txMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
                     txMap.put("SCREEN_NAME", "SHARE PENSION SCHEME");
                     txMap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(debitDataMap.get("SHARE_ACCT_NO")));
                     txMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                     transferTo = transactionDAO.addTransferDebitLocal(txMap, totalPayment);
                     transferTo.setTransId("-");
                     transferTo.setBatchId("-");
                     transferTo.setTransDt(currDt);
                     transferTo.setBranchId(BRANCH_ID);
                     transferTo.setInitiatedBranch(initiatedBranch);
                     transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                     transferTo.setLinkBatchId(CommonUtil.convertObjToStr(debitDataMap.get("SHARE_ACCT_NO")));
                     transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                     transferTo.setStatusDt(currDt);
                     transferTo.setHierarchyLevel("1");
                     TxTransferTO.add(transferTo);
                 }
                 //System.out.println("totalPayment outside... after debit block .."+ totalPayment);
                 for (int k = 0; k < pensionList.size(); k++) {
                     HashMap dataMap = new HashMap();
                     dataMap = (HashMap) pensionList.get(k);
                     //System.out.println("dataMap####inside...." + dataMap);
                     //System.out.println("totalPayment... inside .." + totalPayment);
                     if (totalPayment > 0) {
                         transferTo = new TxTransferTO();
                         txMap = new HashMap();
                         transferTo.setInstrumentNo2("APPL_GL_TRANS");
                         if (!dataMap.get("DIVIDEND_CREDIT_PRODUCT").equals("") && dataMap.get("DIVIDEND_CREDIT_PRODUCT").equals("GL") && transactionTO.getProductId().equals("")) {
                             //System.out.println("$#$$$#$#$# Prod Type GL " + transactionTO.getDebitAcctNo());
                             txMap.put(TransferTrans.CR_AC_HD, dataMap.get("DIVIDEND_CREDIT_AC"));
                             txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                             txMap.put("TRANS_MOD_TYPE", dataMap.get("DIVIDEND_CREDIT_PRODUCT"));
                             txMap.put(TransferTrans.CR_BRANCH, initiatedBranch);
                         } else if (!dataMap.get("DIVIDEND_CREDIT_PRODUCT").equals("")) {
                             //System.out.println("$#$$$#$#$# Prod Type Not GL " + transactionTO.getDebitAcctNo());
                             txMap.put(TransferTrans.CR_AC_HD, (String) debitMap.get("AC_HEAD"));
                             txMap.put(TransferTrans.CR_ACT_NUM, dataMap.get("DIVIDEND_CREDIT_AC"));
                             txMap.put(TransferTrans.CR_PROD_ID, dataMap.get("DIVIDEND_CREDIT_PRODUCT_ID"));
                             txMap.put(TransferTrans.CR_PROD_TYPE, dataMap.get("DIVIDEND_CREDIT_PRODUCT"));
                             txMap.put("TRANS_MOD_TYPE", dataMap.get("DIVIDEND_CREDIT_PRODUCT"));
                             txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                         }
                         txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                         txMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
                         txMap.put(TransferTrans.PARTICULARS, "To SHARE PENSION SCHEME");
                         txMap.put("SCREEN_NAME", "SHARE PENSION SCHEME");
                         txMap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(dataMap.get("SHARE_ACCT_NO")));
                         txMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                         transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(dataMap.get("MIN_PENSION")));
                         transferTo.setTransId("-");
                         transferTo.setBatchId("-");
                         transferTo.setTransDt(currDt);
                         transferTo.setInitiatedBranch(initiatedBranch);
                         transferTo.setBranchId(transactionTO.getBranchId());
                         transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("SHARE_ACCT_NO")));
                         transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                         transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                         transferTo.setStatusDt(currDt);
                         TxTransferTO.add(transferTo);
                     }
                 }
                 transferDAO = new TransferDAO();
                 tansactionMap.put("TxTransferTO", TxTransferTO);
                 tansactionMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                 tansactionMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                 tansactionMap.put(CommonConstants.BRANCH_ID, initiatedBranch);
                 HashMap authorizeMap = new HashMap();
                 authorizeMap.put("BATCH_ID", null);
                 authorizeMap.put("USER_ID", CommonUtil.convertObjToStr(map.get("USER_ID")));
                 authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                 tansactionMap.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
                 //System.out.println("################ tansactionMap :" + tansactionMap);
                 transMap = transferDAO.execute(tansactionMap, false);
                 //System.out.println("transMap .. " + transMap);

                 transactionTO.setBranchId(initiatedBranch);
                 transactionTO.setBatchId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                 transactionTO.setBatchDt(currDt);
                 transactionTO.setStatus(CommonConstants.STATUS_CREATED);
                 transactionTO.setTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                 transactionTO.setTransAmt(totalPayment);
                 transactionTO.setTransType("TRANSFER");
                 sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", transactionTO);                 
                 if (transMap != null && transMap.containsKey("TRANS_ID") && transMap.get("TRANS_ID") != null) {
                     if (pensionList != null && pensionList.size() > 0) {
                         for (int k = 0; k < pensionList.size(); k++) {
                             HashMap dataMap = new HashMap();
                             dataMap = (HashMap) pensionList.get(k);
                             dataMap.put(CommonConstants.BRANCH_ID, initiatedBranch);
                             dataMap.put("SHARE_ACCT_NO", CommonUtil.convertObjToStr(dataMap.get("SHARE_ACCT_NO")));
                             dataMap.put("TRANS_AMOUNT", CommonUtil.convertObjToStr(dataMap.get("MIN_PENSION")));
                             dataMap.put("SHARE_RUN_PERIOD", CommonUtil.convertObjToStr(dataMap.get("DURATION")));
                             dataMap.put("CUST_AGE", CommonUtil.convertObjToStr(dataMap.get("AGE")));
                             dataMap.put("TRANS_DT", currDt.clone());
                             dataMap.put("TRANS_ID", CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                             dataMap.put("TRANS_TYPE", CommonConstants.DEBIT);
                             dataMap.put("STATUS", CommonConstants.STATUS_CREATED);
                             dataMap.put("STATUS_BY", (String) map.get(CommonConstants.USER_ID));
                             dataMap.put("STATUS_DT", currDt.clone());
                             insertPensionScheme(dataMap);
                             double paymentAmt = 0.0;
                             HashMap AuthorizeMap = new HashMap();
                             AuthorizeMap.put("TRANS_DT", currDt.clone());
                             AuthorizeMap.put("TRANS_ID", CommonUtil.convertObjToStr(dataMap.get("TRANS_ID")));
                             AuthorizeMap.put("BRANCH_CODE", map.get(CommonConstants.BRANCH_ID));
                             AuthorizeMap.put("STATUS", CommonConstants.STATUS_AUTHORIZED);
                             AuthorizeMap.put("AUTHORIZED_DT", currDt.clone());
                             AuthorizeMap.put("AUTHORIZED_BY", "TTSYSTEM");                             
                             //System.out.println("#$$$$$$$$$$$$$ AuthorizeMap : " + AuthorizeMap);
                             sqlMap.executeUpdate("authorizePensionSchemeTrans", AuthorizeMap);
                         }
                     }
                 }            
                 pensionList = null;
                 objLogDAO.addToLog(objLogTO);
             }

         } catch (Exception e) {
             sqlMap.rollbackTransaction();
             e.printStackTrace();
             throw new TransRollbackException(e);
         }
     }
    
    private void insertPensionScheme(HashMap map) throws Exception {
        try {
            HashMap insertPensionSchemeMap = setPensionSchemeTO(map);
            PensionSchemeTO pensionSchemeTO = new PensionSchemeTO();
            if (insertPensionSchemeMap.containsKey("PENSION_SCHEME_TRANS")) {
            	pensionSchemeTO = (PensionSchemeTO) insertPensionSchemeMap.get("PENSION_SCHEME_TRANS");    
                sqlMap.executeUpdate("insertPensionSchemeTO", pensionSchemeTO);
            }            
        }catch (Exception e) {
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
    private HashMap setPensionSchemeTO(HashMap map) throws Exception {
        HashMap dataMap = new HashMap();
        PensionSchemeTO pensionSchemeTO = new PensionSchemeTO();
        pensionSchemeTO.setShareAcctNo(CommonUtil.convertObjToStr(map.get("SHARE_ACCT_NO")));
        pensionSchemeTO.setCustAge(CommonUtil.convertObjToDouble(map.get("CUST_AGE")));
        pensionSchemeTO.setShareRunPeriod(CommonUtil.convertObjToDouble(map.get("SHARE_RUN_PERIOD")));
        pensionSchemeTO.setTransAmount(CommonUtil.convertObjToDouble(map.get("TRANS_AMOUNT")));
        pensionSchemeTO.setTransDt(getProperDateFormat(map.get("TRANS_DT")));
        pensionSchemeTO.setTransId(CommonUtil.convertObjToStr(map.get("TRANS_ID")));
        pensionSchemeTO.setTransType(CommonUtil.convertObjToStr(map.get("TRANS_TYPE")));
        pensionSchemeTO.setStatus(CommonConstants.STATUS_CREATED);
        pensionSchemeTO.setStatusDt(getProperDateFormat(map.get("STATUS_DT")));
        pensionSchemeTO.setStatusBy(CommonUtil.convertObjToStr(map.get("STATUS_BY")));   
        pensionSchemeTO.setBranchCode(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));   
        dataMap.put("PENSION_SCHEME_TRANS", pensionSchemeTO);
        System.out.println("pensionSchemeTO####"+pensionSchemeTO);
        return dataMap;
        
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
                    if(objCashTO.getProdType().equals(TransactionFactory.ADVANCES)){
                        objCashTO.setProdId(CommonUtil.convertObjToStr(txnMap.get("PROD_ID")));
                    }
                    if(!CommonUtil.convertObjToStr(txnMap.get("PROD_TYPE")).equals("")&& !CommonUtil.convertObjToStr(txnMap.get("PROD_TYPE")).equals("GL")){
                        objCashTO.setActNum(CommonUtil.convertObjToStr(txnMap.get("ACCT_NUM")));    
                        objCashTO.setProdId(CommonUtil.convertObjToStr(txnMap.get("PROD_ID")));
                    }else{
                        objCashTO.setAcHdId(CommonUtil.convertObjToStr(txnMap.get("ACCT_HEAD")));
                    }
                    objCashTO.setProdType(CommonUtil.convertObjToStr(txnMap.get("PROD_TYPE")));
                    if (objCashTO.getAcHdId() == null || objCashTO.getAcHdId().trim().equals("")){
                        String qry = "getAccountHead" + objCashTO.getProdType();
                        objCashTO.setAcHdId((String) sqlMap.executeQueryForObject(qry, objCashTO.getProdId()));
                    }                    
                    objCashTO.setTransType(CommonConstants.DEBIT);                    
                    objCashTO.setBranchId(CommonUtil.convertObjToStr(txnMap.get("BRANCH_CODE")));
                    objCashTO.setStatusBy(CommonUtil.convertObjToStr(txnMap.get("USER_ID")));
                    objCashTO.setTransDt(currDt);
                    objCashTO.setStatusDt(currDt);
                    objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    objCashTO.setParticulars(CommonUtil.convertObjToStr(txnMap.get("PARTICULARS")));
                    objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(txnMap.get("INITIATED_BRANCH")));
                    objCashTO.setInitChannType(CommonConstants.CASHIER);
                    objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                    objCashTO.setInpAmount(CommonUtil.convertObjToDouble(0));
                    objCashTO.setAmount(TransAmount);
                    objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(txnMap.get("LINK_BATCH_ID")));
                    objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(txnMap.get("ACCT_NUM")));
                   // objCashTO.setScreenName("SHARE PENSION SCHEME");
                    objCashTO.setScreenName(CommonUtil.convertObjToStr(txnMap.get(CommonConstants.SCREEN)));
                    objCashTO.setTransModType(CommonUtil.convertObjToStr(txnMap.get("TRANS_MOD_TYPE")));
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
    
    public CashTransactionTO setCashTransactionValuePayment(HashMap txnMap) {
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
                    objCashTO.setProdId(CommonUtil.convertObjToStr(txnMap.get("PROD_ID")));
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
                    objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                    objCashTO.setInpAmount(CommonUtil.convertObjToDouble(0));
                    objCashTO.setAmount(TransAmount);
                    objCashTO.setInstType(TransactionDAOConstants.WITHDRAWLSLIP);
                    objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(txnMap.get("LINK_BATCH_ID")));
                    objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(txnMap.get("ACCT_NUM")));
                    objCashTO.setScreenName("Group Loan Payment Transaction");
                    objCashTO.setTransModType(CommonConstants.ADVANCE_TRANSMODE_TYPE);
                    objCashTO.setAuthorizeRemarks("GROUP_LOAN");
                    objCashTO.setSingleTransId(CommonUtil.convertObjToStr(txnMap.get("SINGLE_TRANS_ID")));
                    //cashTransactionList.add(objCashTO);
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
    
    private void updateData(HashMap chargesMap, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
    }

    private void deleteData(HashMap chargesMap, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
    }

    private void authorize(HashMap map) throws Exception {
        System.out.println("######### map : " + map);
        String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_ID"));
        HashMap authMap = new HashMap();
        authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
        ArrayList selectedList = (ArrayList) authMap.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthorizeMap = new HashMap();
        AuthorizeMap = (HashMap) selectedList.get(0);
        String status = CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS));
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
            if (pensionList!= null) {
                System.out.println("IN pensionList####"+pensionList);
                String linkBatchId = "";
                for (int i = 0; i < pensionList.size(); i++) {
                    HashMap dataMap = new HashMap();
                    dataMap = (HashMap) pensionList.get(i);
                    double paymentAmt = 0.0;
                    linkBatchId = CommonUtil.convertObjToStr(dataMap.get("SHARE_ACCT_NO"));
                    AuthorizeMap.put("TRANS_DT",currDt.clone());
                    AuthorizeMap.put("TRANS_ID",CommonUtil.convertObjToStr(dataMap.get("TRANS_ID")));
                    AuthorizeMap.put("BRANCH_CODE",map.get(CommonConstants.BRANCH_ID));
                    System.out.println("#$$$$$$$$$$$$$ AuthorizeMap : " + AuthorizeMap);
                    sqlMap.executeUpdate("authorizePensionSchemeTrans", AuthorizeMap);
                }
                System.out.println("-------------------> transactionTO : " + transactionTO);
                if (transactionTO.getTransType().equals("TRANSFER")) {
                    if (totalExcessTransAmt > 0) {
                        HashMap transferTransParam = new HashMap();
                        transferDAO = new TransferDAO();
                        transferTransParam.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                        transferTransParam.put(CommonConstants.USER_ID, AuthorizeMap.get("AUTHORIZED_BY"));
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
                    HashMap authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
                    HashMap singleAuthorizeMap = new HashMap();
                    authorizeMap.put("LINK_BATCH_ID", linkBatchId);
                    authorizeMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                    authorizeMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                    authorizeMap.put("TRANS_DT", currDt);
                    authorizeMap.put("TRANS_ID",CommonUtil.convertObjToStr(AuthorizeMap.get("TRANS_ID")));
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
                        dataMap = null;
                    }
                }  
                pensionList = null;
            }
            
        }
    }
    
    private void DailyAuthorize(HashMap map) throws Exception {
        System.out.println("authorize#########" + map);
        HashMap authMap = new HashMap();
        authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
        ArrayList selectedList = (ArrayList) authMap.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthorizeMap = new HashMap();
        AuthorizeMap = (HashMap) selectedList.get(0);
        String status = CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS));
        String linkBatchId = null;
        String disbursalNo = null;
        HashMap cashAuthMap;
        TransactionTO objTransactionTO = null;
        String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_ID"));
        try {
            if (dailyGroupLoanList!= null) {
                System.out.println("IN Group groupLoanList####"+dailyGroupLoanList);                
                for (int i = 0; i < dailyGroupLoanList.size(); i++) {
                    HashMap dataMap = new HashMap();
                    dataMap = (HashMap) dailyGroupLoanList.get(i);
                    double paymentAmt = 0.0;
                    linkBatchId = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));
                    AuthorizeMap.put("ACT_NUM",linkBatchId);
                    AuthorizeMap.put("CUST_ID",CommonUtil.convertObjToStr(dataMap.get("CUST_ID")));
                    System.out.println("#$$$$$$$$$$$$$ AuthorizeMap : " + AuthorizeMap);
                    sqlMap.executeUpdate("authorizeGroupLoanReceipt", AuthorizeMap);                    
                }
                HashMap transferTransParam = new HashMap();
                transferDAO = new TransferDAO();
                transferTransParam.put(CommonConstants.BRANCH_ID, BRANCH_ID);
                transferTransParam.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                transferTransParam.put("DEBIT_LOAN_TYPE", "DP");
                transferTransParam.put("LINK_BATCH_ID", linkBatchId);
                //transferTransParam.put("BATCH_ID", linkBatchId);
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
            map = null;          
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
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
    
    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        pensionList = null;
    }
    
    public Date getProperDateFormat(Object obj) {
        Date currDate = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDate = (Date) currDt.clone();
            currDate.setDate(tempDt.getDate());
            currDate.setMonth(tempDt.getMonth());
            currDate.setYear(tempDt.getYear());
        }
        return currDate;
    }
    private String generateLinkID() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "GENERATE_LINK_ID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//for trans_batch_id resetting, branch code
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        String batchID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        return batchID;
    }
    public static void main(String str[]) {
        try {
            PensionSchemeDAO dao = new PensionSchemeDAO();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
