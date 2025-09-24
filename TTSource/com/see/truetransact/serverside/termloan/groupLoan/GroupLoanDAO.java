/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * SHGTransactionDAO.java
 *
 * Created on June 18, 2011, 4:14 PM
 */
package com.see.truetransact.serverside.termloan.groupLoan;

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
public class GroupLoanDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private TransactionDAO transactionDAO = null;
    TransferDAO transferDAO = new TransferDAO();
    NomineeDAO objNomineeDAO = new NomineeDAO();
    private AccountTO accountTO;
    private Date currDt = null;
    final String SCREEN = "TD";
    public List groupLoanList = null;
    public List groupLoanPaymentList = null;
    private HashMap TermLoanCloseCharge = new HashMap();
    private HashMap execReturnMap = null;
    private String narration = "";
    GroupLoanTO GroupLoanTO = new GroupLoanTO();
    public List dailyGroupLoanList = null;
    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public GroupLoanDAO() throws ServiceLocatorException {
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
        System.out.println("Map in Group Loan DAO: " + map);
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
        if (map.containsKey("SHG_TABLE_DATA") || map.containsKey("SHG_TABLE_DATA_PAYMENT") || map.containsKey("DAILY_GROUP_LOAN_DATA")) {
            try {
                sqlMap.startTransaction();
                objLogTO.setStatus(command);
                if (map.containsKey("SHG_TABLE_DATA")){
                    groupLoanList = (List) map.get("SHG_TABLE_DATA");
                }
                if (map.containsKey("SHG_TABLE_DATA_PAYMENT")){
                    groupLoanPaymentList = (List) map.get("SHG_TABLE_DATA_PAYMENT");
                }
                if (map.containsKey("DAILY_GROUP_LOAN_DATA")){
                    dailyGroupLoanList = (List) map.get("DAILY_GROUP_LOAN_DATA");
                }
                if (map.containsKey("GroupLoanTO")){
                    GroupLoanTO = (GroupLoanTO) map.get("GroupLoanTO");
                    System.out.println("GroupLoanTO #### :" + GroupLoanTO);
                }
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    if(groupLoanList!=null && groupLoanList.size()>0){
                        System.out.println("@##$#$% shgList #### :" + groupLoanList);
                        insertData(map, objLogDAO, objLogTO);
                    }else if(groupLoanPaymentList!=null && groupLoanPaymentList.size()>0){
                        System.out.println("@##$#$% groupLoanPaymentList #### :" + groupLoanPaymentList);
                        insertPaymentData(map, objLogDAO, objLogTO);
                    }else if(dailyGroupLoanList!=null && dailyGroupLoanList.size()>0){
                        System.out.println("@##$#$% dailyGroupLoanList #### :" + dailyGroupLoanList);
                        insertDailyData(map, objLogDAO, objLogTO);
                    }
                            
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData(map, objLogDAO, objLogTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData(map, objLogDAO, objLogTO);
                } else if (map.containsKey("AUTHORIZEMAP")) {
                    if (map.containsKey("DAILY_GROUP_LOAN_DATA")){
                        DailyAuthorize(map);
                    }else{
                        authorize(map);
                    }
                } else {
                    throw new NoCommandException();
                }
                sqlMap.commitTransaction();
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw e;
            }
           finally
            {
            objLogDAO = null;
            objLogTO = null;
            destroyObjects();
            }

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
    
    private void insertPaymentData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            if (groupLoanPaymentList != null && groupLoanPaymentList.size() > 0) {
                String generateCashId = generateLinkID();
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
                double firstPayment = 0.0;
                System.out.println("groupLoanPaymentList map####"+map);
                System.out.println("ACT_NUM####"+CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                prodMap.put("ACT_NUM", CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                List prodList = sqlMap.executeQueryForList("getProdIdForGroupLoan", prodMap);
                if (prodList != null && prodList.size() > 0) {
                    prodMap = (HashMap) prodList.get(0);
                }
                System.out.println("PRODmAP####"+prodMap);
                HashMap acHdmap = new HashMap();
                List acHdlst = sqlMap.executeQueryForList("getAccountHeadProdGroupLoanAD", prodMap);    // Acc Head
                if (acHdlst != null && acHdlst.size() > 0) {
                   acHdmap = (HashMap) acHdlst.get(0);
                }
                String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_ID"));
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
                    //Repay date calculation - Starts..
                    Date repayDt = null;
                    DateFormat formatter;
                    formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(currDt);                    
                    int prodDay = 0;
                    int prodMonth= 0;                    
                    HashMap repayMap = new HashMap();
                    repayMap.put("PROD_ID",prodMap.get("PROD_ID"));
                    List repaylst = sqlMap.executeQueryForList("getRepayDetails", repayMap);
                    if (repaylst != null && repaylst.size() > 0) {
                        repayMap = (HashMap) repaylst.get(0);
                        prodDay = CommonUtil.convertObjToInt(repayMap.get("DAYS"));
                        prodMonth = CommonUtil.convertObjToInt(repayMap.get("MONTHS"));
                        System.out.println("month######"+prodMonth+"prodDay####"+prodDay);
                        cal.add(Calendar.MONTH, prodMonth);
                        cal.set(Calendar.DAY_OF_MONTH, prodDay);
                        repayDt = formatter.parse(formatter.format(cal.getTime()));
                        System.out.println("dATE######"+repayDt);
                    }else{                        
                        throw new TTException("Product Details Should not be empty....!!");               
                    }
                    
                    
                    for (int k = 0; k < groupLoanPaymentList.size(); k++) {
                        HashMap dataMap = new HashMap();
                        dataMap = (HashMap) groupLoanPaymentList.get(k);
                        paymentAmt = CommonUtil.convertObjToDouble(dataMap.get("PAYMENT"));
                        firstPayment +=paymentAmt;
                        //System.out.println("paymentAmt######1"+paymentAmt);
                        //System.out.println("firstPayment######1"+firstPayment);
                        //if(paymentAmt>0){
                       //     dataMap.put("CUST_ID", CommonUtil.convertObjToStr(dataMap.get("CUST_ID")));
                        //    dataMap.put("ACT_NUM", CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                        //    dataMap.put("PRINCIPLE", paymentAmt);
                        //    dataMap.put("TRANS_DT", currDt);
                         //   dataMap.put("TRANS_TYPE", CommonConstants.DEBIT);
                          //  dataMap.put("REPAY_DT", repayDt);
                          //  dataMap.put("STATUS_DT", currDt);
                         //   dataMap.put("STATUS_BY", (String) map.get(CommonConstants.USER_ID));
                         //   System.out.println("############## dataMap " + dataMap);
                         //   insertGroupLoan(dataMap);
                            //firstPayment = paymentAmt;
                         //   }
                     }
                     System.out.println("paymentAmt######2222"+paymentAmt);
                     System.out.println("firstPayment######222"+firstPayment);
                     if(transactionTO.getTransType().equals("CASH")){
                        CashTransactionTO objCashTO = new CashTransactionTO();
                        HashMap cashTransMap = new HashMap();
                        HashMap applicationMap = new HashMap();  
                        HashMap CashTransMap = new HashMap();
                        ArrayList cashList = new ArrayList();
                        String loanAccNo = "";
                        loanAccNo = CommonUtil.convertObjToStr(map.get("ACT_NUM"));
                        //paymentAmt = CommonUtil.convertObjToDouble(dataMap.get("PAYMENT"));
                        System.out.println("paymentAmt######"+paymentAmt);
                        System.out.println("firstPayment######"+firstPayment);
                        if(firstPayment>0){
                            cashTransMap.put("ACCT_HEAD", (String) acHdmap.get("AC_HEAD"));
                            cashTransMap.put("PARTICULARS","To Group Loan Principle : " + loanAccNo);
                            cashTransMap.put("PROD_TYPE", TransactionFactory.ADVANCES);
                            cashTransMap.put("SELECTED_BRANCH_ID", BRANCH_ID);
                            cashTransMap.put("ACCT_NUM", loanAccNo);
                            cashTransMap.put("LINK_BATCH_ID", loanAccNo);
                            cashTransMap.put("PROD_ID", prodMap.get("PROD_ID"));
                            cashTransMap.put("BRANCH_CODE", BRANCH_ID);
                            cashTransMap.put("INITIATED_BRANCH", initiatedBranch);
                            cashTransMap.put("TOKEN_NO", "");
                            cashTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                            cashTransMap.put("USER_ID", (String) map.get(CommonConstants.USER_ID));
                            cashTransMap.put("TRANS_AMOUNT", firstPayment);
                            cashTransMap.put("SINGLE_TRANS_ID",generateCashId);
                            cashList.add(setCashTransactionValuePayment(cashTransMap));
                        }
                         System.out.println("paymentAmt######33333"+paymentAmt);
                        System.out.println("firstPayment######3333"+firstPayment);
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
                            getTransDetails(loanAccNo);
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
                            double paymentAmtTran = 0.0;
                            double totAmtTran = firstPayment;
                            System.out.println("################ totAmtTran :" + totAmtTran);
                            String loanAccNo = "";
                            loanAccNo = CommonUtil.convertObjToStr(map.get("ACT_NUM"));
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            if (totAmtTran > 0) {
                                //DEBIT
                                transferTrans.setInitiatedBranch(BRANCH_ID);
                                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                                transactionDAO.setInitiatedBranch(initiatedBranch);
                                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                txMap.put(TransferTrans.DR_AC_HD, (String) (String) acHdmap.get("AC_HEAD"));
                                txMap.put(TransferTrans.DR_ACT_NUM, loanAccNo);
                                txMap.put(TransferTrans.DR_PROD_ID, prodMap.get("PROD_ID"));
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.ADVANCES);
                                txMap.put(TransferTrans.PARTICULARS, "To Group Loan Principle : " + loanAccNo);
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                                txMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
                                txMap.put("TRANS_MOD_TYPE", CommonConstants.ADVANCE_TRANSMODE_TYPE);
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, totAmtTran);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setTransDt(currDt);
                                transferTo.setBranchId(BRANCH_ID);
                                transferTo.setInitiatedBranch(initiatedBranch);
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setAuthorizeRemarks("DP");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setStatusDt(currDt);
                                transferTo.setLinkBatchId(loanAccNo);
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
                                    txMap.put(TransferTrans.PARTICULARS, "To Group Loan Principle : " + loanAccNo);
                                    txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                    txMap.put("TRANS_MOD_TYPE",TransactionFactory.GL);
                                } else if (!transactionTO.getProductType().equals("")) {
                                    System.out.println("$#$$$#$#$# Prod Type Not GL " + transactionTO.getDebitAcctNo());
                                    txMap.put(TransferTrans.CR_AC_HD, (String) debitMap.get("AC_HEAD"));
                                    txMap.put(TransferTrans.CR_ACT_NUM, transactionTO.getDebitAcctNo());
                                    txMap.put(TransferTrans.CR_PROD_ID, transactionTO.getProductId());
                                    txMap.put(TransferTrans.CR_PROD_TYPE, transactionTO.getProductType());
                                    txMap.put(TransferTrans.PARTICULARS, linkBatchId + "-" + ":SHG_PAYMENT");
                                    txMap.put(TransferTrans.CR_BRANCH, transactionTO.getBranchId());
                                    txMap.put("TRANS_MOD_TYPE",transactionTO.getProductType());
                                }                                
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
                                txMap.put(TransferTrans.PARTICULARS, "To Group Loan Principle : " + loanAccNo);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, totAmtTran);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setTransDt(currDt);
                                transferTo.setInitiatedBranch(initiatedBranch);
//                              transferTo.setStatusBy(CommonConstants.TTSYSTEM);
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
                        transactionTO.setBatchId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                        transactionTO.setBatchDt(currDt);
                        transactionTO.setStatus(CommonConstants.STATUS_CREATED);
                        transactionTO.setTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                        sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", transactionTO);
                    
                }
                     for (int k = 0; k < groupLoanPaymentList.size(); k++) {
                        HashMap dataMap = new HashMap();
                        long slNo = 0;
                        dataMap = (HashMap) groupLoanPaymentList.get(k);
                        paymentAmt = CommonUtil.convertObjToDouble(dataMap.get("PAYMENT"));
                        firstPayment +=paymentAmt;
                        //Trans count
                        HashMap slNoMap = new HashMap();
                        slNoMap.put("CUST_ID", CommonUtil.convertObjToStr(dataMap.get("CUST_ID")));
                        slNoMap.put("ACT_NUM", CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                        List slNolst = sqlMap.executeQueryForList("getSL_NO", slNoMap);
                        if (slNolst != null && slNolst.size() > 0) {
                            slNoMap = (HashMap) slNolst.get(0);
                            slNo = CommonUtil.convertObjToLong(slNoMap.get("SL_NO"));
                        }
                        slNo++;    
                        if(paymentAmt>0){                            
                            dataMap.put("CUST_ID", CommonUtil.convertObjToStr(dataMap.get("CUST_ID")));
                            dataMap.put("ACT_NUM", CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                            dataMap.put("PRINCIPLE", paymentAmt);
                            dataMap.put("TRANS_DT", map.get("PURCHASE_DT"));
                            dataMap.put("TRANS_TYPE", CommonConstants.DEBIT);
                            dataMap.put("REPAY_DT", repayDt);
                            dataMap.put("STATUS_DT", currDt);
                            dataMap.put("STATUS_BY", (String) map.get(CommonConstants.USER_ID));
                            dataMap.put("TRANS_ID", CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                            dataMap.put("TRANS_SLNO", new Long(slNo));
                            dataMap.put("NARRATION", CommonUtil.convertObjToStr(dataMap.get("NARRATION")));
                            dataMap.put("INITIATED_BRANCH", BRANCH_ID);
                            System.out.println("############## dataMap " + dataMap);
                            insertGroupLoan(dataMap);
                            //firstPayment = paymentAmt;
                            }
                     }
                groupLoanPaymentList = null;
                objLogDAO.addToLog(objLogTO);
            }
            
          } 
        }catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
    
    private void insertData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            if (groupLoanList != null && groupLoanList.size() > 0) {
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
                System.out.println("ACT_NUM####"+CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                prodMap.put("ACT_NUM", CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                List prodList = sqlMap.executeQueryForList("getProdIdForGroupLoan", prodMap);
                if (prodList != null && prodList.size() > 0) {
                    prodMap = (HashMap) prodList.get(0);
                }
                System.out.println("PRODmAP####"+prodMap);
                HashMap acHdmap = new HashMap();
                List acHdlst = sqlMap.executeQueryForList("getAccountHeadProdGroupLoanAD", prodMap);    // Acc Head
                if (acHdlst != null && acHdlst.size() > 0) {
                   acHdmap = (HashMap) acHdlst.get(0);
                }
                double totalExcessTransAmt = 0.0;
                String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_ID"));
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
                    for (int k = 0; k < groupLoanList.size(); k++) {
                        HashMap dataMap = new HashMap();
                        dataMap = (HashMap) groupLoanList.get(k);
                        receiptAmt = CommonUtil.convertObjToDouble(dataMap.get("RECEIPT"));
                        firstRec = receiptAmt;
                        principleAmt = CommonUtil.convertObjToDouble(dataMap.get("PRIN_DUE"));
                        interestAmt = CommonUtil.convertObjToDouble(dataMap.get("INTEREST"));
                        penalAmt = CommonUtil.convertObjToDouble(dataMap.get("PENAL"));
                        chargeAmt = CommonUtil.convertObjToDouble(dataMap.get("CHARGES"));
                        if(receiptAmt>0){
                            dataMap.put("CUST_ID", CommonUtil.convertObjToStr(dataMap.get("CUST_ID")));
                            dataMap.put("ACT_NUM", CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                            if(chargeAmt>0){
                                if (receiptAmt >= chargeAmt) {
                                    receiptAmt -= chargeAmt;
                                    dataMap.put("CHARGES", chargeAmt);
                                    totchargeAmt = totchargeAmt + chargeAmt;
                                }else{
                                    dataMap.put("CHARGES", receiptAmt);
                                    totchargeAmt = totchargeAmt + receiptAmt;
                                    receiptAmt -= receiptAmt;                                    
                                }
                           }if(penalAmt>0){
                               System.out.println("RECEIPT2222###"+receiptAmt);
                                    System.out.println("PENAL22222###"+penalAmt);
                                if (receiptAmt >= penalAmt) {
                                    receiptAmt -= penalAmt;
                                    dataMap.put("PENAL", penalAmt);
                                    totpenalAmt  = totpenalAmt + penalAmt;
                                }else{
                                    dataMap.put("PENAL", receiptAmt);
                                    totpenalAmt  = totpenalAmt + receiptAmt;
                                    receiptAmt -= receiptAmt;                                    
                                }
                                System.out.println("RECEIPT###"+receiptAmt);
                                
                           }if(interestAmt>0){
                                if (receiptAmt >= interestAmt) {
                                    receiptAmt -= interestAmt;
                                    dataMap.put("INTEREST", interestAmt);
                                    totIntAmt = totIntAmt + interestAmt;
                                }else{
                                   dataMap.put("INTEREST", receiptAmt);
                                   totIntAmt = totIntAmt + receiptAmt;
                                   receiptAmt -= receiptAmt;                                   
                                }
                           }
                           if(principleAmt>0 && (firstRec-(chargeAmt+penalAmt+interestAmt))>0){
                                dataMap.put("PRINCIPLE", receiptAmt);
                                totPrincple =  totPrincple + receiptAmt;
                            }
                            //dataMap.put("PAYMENT", paymentAmt);
                            dataMap.put("TRANS_DT", currDt);
                            dataMap.put("TRANS_TYPE", CommonConstants.CREDIT);
                            dataMap.put("STATUS_DT", currDt);
                            dataMap.put("STATUS_BY", (String) map.get(CommonConstants.USER_ID));
                            System.out.println("############## dataMap " + dataMap);
                            //insertGroupLoan(dataMap);
                            }
                     }
                    
                     if(transactionTO.getTransType().equals("CASH")){
                        CashTransactionTO objCashTO = new CashTransactionTO();
                        HashMap cashTransMap = new HashMap();
                        HashMap applicationMap = new HashMap();  
                        HashMap CashTransMap = new HashMap();
                        ArrayList cashList = new ArrayList();
                        String loanAccNo = "";
                        loanAccNo = CommonUtil.convertObjToStr(map.get("ACT_NUM"));
                        System.out.println("totPrincple######"+totPrincple);
                        System.out.println("totIntAmt######"+totIntAmt);
                        System.out.println("totpenalAmt######"+totpenalAmt);
                        System.out.println("totchargeAmt######"+totchargeAmt);
                        if(totchargeAmt>0){
                            cashTransMap.put("ACCT_HEAD", (String) acHdmap.get("OTHRCHRGS_HD"));
                            cashTransMap.put("PARTICULARS","By Group Loan Charges : " + loanAccNo);
                            cashTransMap.put("PROD_TYPE", TransactionFactory.GL);
                            cashTransMap.put("SELECTED_BRANCH_ID", BRANCH_ID);
                            cashTransMap.put("INITIATED_BRANCH", initiatedBranch);
                            cashTransMap.put("LINK_BATCH_ID", loanAccNo);
                            cashTransMap.put("PROD_ID", prodMap.get("PROD_ID"));
                            cashTransMap.put("BRANCH_CODE", BRANCH_ID);
                            cashTransMap.put("TOKEN_NO", "");
                            //cashTransMap.put("LOANDEBIT", "LOANCREDIT");
                            cashTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                            cashTransMap.put("USER_ID", (String) map.get(CommonConstants.USER_ID));
                            cashTransMap.put("TRANS_AMOUNT", totchargeAmt);
                            cashTransMap.put("SINGLE_TRANS_ID",generateCashId);
                            cashTransMap.put("INSTRUMENT2","LOAN_OTHER_CHARGES");
                            cashTransMap.put("REMARKS","OTHER_CHARGES");
                            cashTransMap.put("LOAN_HIERARCHY","1");
                            cashList.add(setCashTransactionValue(cashTransMap));
                        }
                        if(totpenalAmt>0){
                            cashTransMap.put("ACCT_HEAD", (String) acHdmap.get("PENAL_INT"));
                            cashTransMap.put("PARTICULARS","By Group Loan Penal : " + loanAccNo);
                            cashTransMap.put("PROD_TYPE", TransactionFactory.GL);
                            cashTransMap.put("SELECTED_BRANCH_ID", BRANCH_ID);
                            cashTransMap.put("LINK_BATCH_ID", loanAccNo);
                            cashTransMap.put("PROD_ID", prodMap.get("PROD_ID"));
                            cashTransMap.put("BRANCH_CODE", BRANCH_ID);
                            cashTransMap.put("INITIATED_BRANCH", initiatedBranch);
                            cashTransMap.put("TOKEN_NO", "");
                            //cashTransMap.put("LOANDEBIT", "LOANCREDIT");
                            cashTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                            cashTransMap.put("USER_ID", (String) map.get(CommonConstants.USER_ID));
                            cashTransMap.put("TRANS_AMOUNT", totpenalAmt);
                            cashTransMap.put("SINGLE_TRANS_ID",generateCashId);
                            cashTransMap.put("INSTRUMENT2","LOAN_PENAL_INT");
                            cashTransMap.put("REMARKS","PENAL_INT");
                            cashTransMap.put("LOAN_HIERARCHY","2");
                            cashList.add(setCashTransactionValue(cashTransMap));
                        }
                        if(totIntAmt>0){
                            cashTransMap.put("ACCT_HEAD", (String) acHdmap.get("AC_DEBIT_INT"));
                            cashTransMap.put("PARTICULARS","By Group Loan Interest : " + loanAccNo);
                            cashTransMap.put("PROD_TYPE", TransactionFactory.GL);
                            cashTransMap.put("SELECTED_BRANCH_ID", BRANCH_ID);
                            cashTransMap.put("LINK_BATCH_ID", loanAccNo);
                            cashTransMap.put("PROD_ID", prodMap.get("PROD_ID"));
                            cashTransMap.put("BRANCH_CODE", BRANCH_ID);
                            cashTransMap.put("INITIATED_BRANCH", initiatedBranch);
                            cashTransMap.put("TOKEN_NO", "");
                            //cashTransMap.put("LOANDEBIT", "LOANCREDIT");
                            cashTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                            cashTransMap.put("USER_ID", (String) map.get(CommonConstants.USER_ID));
                            cashTransMap.put("TRANS_AMOUNT", totIntAmt);
                            cashTransMap.put("SINGLE_TRANS_ID",generateCashId);
                            cashTransMap.put("INSTRUMENT2","LOAN_INTEREST");
                            cashTransMap.put("REMARKS","INTEREST");
                            cashTransMap.put("LOAN_HIERARCHY","3");
                            cashList.add(setCashTransactionValue(cashTransMap));
                        }
                        if(totPrincple>0){
                            cashTransMap.put("ACCT_HEAD", (String) acHdmap.get("AC_HEAD"));
                            cashTransMap.put("PARTICULARS","By Group Loan Principle : " + loanAccNo);
                            cashTransMap.put("PROD_TYPE", TransactionFactory.ADVANCES);
                            cashTransMap.put("SELECTED_BRANCH_ID", BRANCH_ID);
                            cashTransMap.put("ACCT_NUM", loanAccNo);
                            cashTransMap.put("LINK_BATCH_ID", loanAccNo);
                            cashTransMap.put("PROD_ID", prodMap.get("PROD_ID"));
                            cashTransMap.put("BRANCH_CODE", BRANCH_ID);
                            cashTransMap.put("INITIATED_BRANCH", initiatedBranch);
                            cashTransMap.put("TOKEN_NO", "");
                            cashTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                            cashTransMap.put("USER_ID", (String) map.get(CommonConstants.USER_ID));
                            cashTransMap.put("TRANS_AMOUNT", totPrincple);
                            cashTransMap.put("SINGLE_TRANS_ID",generateCashId);
                            cashTransMap.put("INSTRUMENT2","LOAN_PRINCIPAL");
                            cashTransMap.put("REMARKS","PRINCIPAL");
                            cashTransMap.put("LOAN_HIERARCHY","4");
                            cashList.add(setCashTransactionValue(cashTransMap));
                        }
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
                            getTransDetails(loanAccNo);
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
                            double paymentAmtTran = firstRec;
                            double totAmtTran = firstRec;
                            System.out.println("################ totAmtTran :" + totAmtTran+""+firstRec);
                            System.out.println("################ totchargeAmt :" + totchargeAmt);
                            System.out.println("################ totpenalAmt :" + totpenalAmt);
                            System.out.println("################ totIntAmt :" + totAmtTran);
                            System.out.println("################ totPrincple :" + totPrincple);
                            String loanAccNo = "";
                            loanAccNo = CommonUtil.convertObjToStr(map.get("ACT_NUM"));
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            if (transactionTO.getTransAmt() > 0) {
                                transferTrans.setInitiatedBranch(BRANCH_ID);
                                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                                transactionDAO.setInitiatedBranch(_branchCode);
                                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                //CREDIT CHARGES
                                if(totchargeAmt>0){
                                    txMap.put(TransferTrans.CR_AC_HD, (String) (String) acHdmap.get("OTHRCHRGS_HD"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, "To Group Loan Charge : " + loanAccNo);
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                    txMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
                                    txMap.put("TRANS_MOD_TYPE", CommonConstants.ADVANCE_TRANSMODE_TYPE);
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, totchargeAmt);
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
                                    transferTo.setAuthorizeRemarks("OTHER_CHARGES");
                                    transferTo.setHierarchyLevel("1");
                                    transferTo.setInstrumentNo2("LOAN_OTHER_CHARGES");
                                    transactionDAO.setLinkBatchID(loanAccNo);
                                    TxTransferTO.add(transferTo);
                                }
                                //CREDIT PENAL
                                if(totpenalAmt>0){
                                    txMap.put(TransferTrans.CR_AC_HD, (String) (String) acHdmap.get("PENAL_INT"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE , TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, "To Group Loan Penal : " + loanAccNo);
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                    txMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
                                    txMap.put("TRANS_MOD_TYPE", CommonConstants.ADVANCE_TRANSMODE_TYPE);
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, totpenalAmt);
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
                                    transferTo.setAuthorizeRemarks("PENAL_INT");
                                    transferTo.setHierarchyLevel("2");
                                    transferTo.setInstrumentNo2("LOAN_PENAL_INT");
                                    transactionDAO.setLinkBatchID(loanAccNo);
                                    TxTransferTO.add(transferTo);
                                }
                                //CREDIT INTEREST
                                if(totIntAmt>0){
                                    txMap.put(TransferTrans.CR_AC_HD, (String) (String) acHdmap.get("AC_DEBIT_INT"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, "To Group Loan Interest : " + loanAccNo);
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                    txMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
                                    txMap.put("TRANS_MOD_TYPE", CommonConstants.ADVANCE_TRANSMODE_TYPE);
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, totIntAmt);
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setTransDt(currDt);
                                    transferTo.setBranchId(BRANCH_ID);
                                    transferTo.setInitiatedBranch(initiatedBranch);
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setStatusDt(currDt);
                                    transferTo.setLinkBatchId(loanAccNo);
                                    transactionDAO.setLinkBatchID(loanAccNo);
                                    transferTo.setAuthorizeRemarks("INTEREST");
                                    transferTo.setHierarchyLevel("3");
                                    transferTo.setInstrumentNo2("LOAN_INTEREST");
                                    TxTransferTO.add(transferTo);
                                }
                                //CREDIT PRINCIPLE
                                if (totPrincple  > 0) {
                                    txMap.put(TransferTrans.CR_AC_HD, (String) (String) acHdmap.get("AC_HEAD"));
                                    txMap.put(TransferTrans.CR_ACT_NUM, loanAccNo);
                                    txMap.put(TransferTrans.CR_PROD_ID, prodMap.get("PROD_ID"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                                    txMap.put(TransferTrans.PARTICULARS, "To Group Loan Principle : " + loanAccNo);
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                    txMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
                                    txMap.put("TRANS_MOD_TYPE", CommonConstants.ADVANCE_TRANSMODE_TYPE);
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, totPrincple);
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
                                    transactionDAO.setLinkBatchID(loanAccNo);
                                    transferTo.setAuthorizeRemarks("PRINCIPAL");
                                    transferTo.setHierarchyLevel("4");
                                    transferTo.setInstrumentNo2("LOAN_PRINCIPAL");
                                    TxTransferTO.add(transferTo);
                                }
                                //DEBIT
                                if (transactionTO.getTransAmt() > 0) {
                                    transferTo = new TxTransferTO();
                                    txMap = new HashMap();
                                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL") && transactionTO.getProductId().equals("")) {
                                        System.out.println("$#$$$#$#$# Prod Type GL " + transactionTO.getDebitAcctNo());
                                        txMap.put(TransferTrans.DR_AC_HD, (String) transactionTO.getDebitAcctNo());
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.PARTICULARS, "To Group Loan : " + loanAccNo);
                                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                    } else if (!transactionTO.getProductType().equals("")) {
                                        System.out.println("$#$$$#$#$# Prod Type Not GL " + transactionTO.getDebitAcctNo());
                                        txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HEAD"));
                                        txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                                        txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                                        txMap.put(TransferTrans.DR_PROD_TYPE, transactionTO.getProductType());
                                        txMap.put(TransferTrans.PARTICULARS, linkBatchId + "-" + ":SHG_PAYMENT");
                                        txMap.put("TRANS_MOD_TYPE", transactionTO.getProductType());
                                    }
                                    txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
                                    txMap.put(TransferTrans.PARTICULARS, "To Group Loan Principle : " + loanAccNo);                                    
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, transactionTO.getTransAmt());
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
                                }
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
                     //TransID and TransSlNO
                     for (int k = 0; k < groupLoanList.size(); k++) {
                        HashMap dataMap = new HashMap();
                        long CreditSlNo = 0;
                        dataMap = (HashMap) groupLoanList.get(k);
                        receiptAmt = CommonUtil.convertObjToDouble(dataMap.get("RECEIPT"));
                        firstRec = receiptAmt;
                        principleAmt = CommonUtil.convertObjToDouble(dataMap.get("PRIN_DUE"));
                        interestAmt = CommonUtil.convertObjToDouble(dataMap.get("INTEREST"));
                        penalAmt = CommonUtil.convertObjToDouble(dataMap.get("PENAL"));
                        chargeAmt = CommonUtil.convertObjToDouble(dataMap.get("CHARGES"));
                        //Trans count
                        HashMap slNoMap = new HashMap();
                        slNoMap.put("CUST_ID", CommonUtil.convertObjToStr(dataMap.get("CUST_ID")));
                        slNoMap.put("ACT_NUM", CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                        List slNolst = sqlMap.executeQueryForList("getSL_NO", slNoMap);
                        if (slNolst != null && slNolst.size() > 0) {
                            slNoMap = (HashMap) slNolst.get(0);
                            CreditSlNo = CommonUtil.convertObjToLong(slNoMap.get("SL_NO"));
                        }
                        CreditSlNo++;
                        if(receiptAmt>0){
                            dataMap.put("CUST_ID", CommonUtil.convertObjToStr(dataMap.get("CUST_ID")));
                            dataMap.put("ACT_NUM", CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                            if(chargeAmt>0){
                                if (receiptAmt >= chargeAmt) {
                                    receiptAmt -= chargeAmt;
                                    dataMap.put("CHARGES", chargeAmt);
                                    totchargeAmt = totchargeAmt + chargeAmt;
                                }else{
                                    dataMap.put("CHARGES", receiptAmt);
                                    totchargeAmt = totchargeAmt + receiptAmt;
                                    receiptAmt -= receiptAmt;                                    
                                }
                           }if(penalAmt>0){
                               System.out.println("RECEIPT2222###"+receiptAmt);
                                    System.out.println("PENAL22222###"+penalAmt);
                                if (receiptAmt >= penalAmt) {
                                    receiptAmt -= penalAmt;
                                    dataMap.put("PENAL", penalAmt);
                                    totpenalAmt  = totpenalAmt + penalAmt;
                                }else{
                                    dataMap.put("PENAL", receiptAmt);
                                    totpenalAmt  = totpenalAmt + receiptAmt;
                                    receiptAmt -= receiptAmt;                                    
                                }
                                System.out.println("RECEIPT###"+receiptAmt);
                                
                           }if(interestAmt>0){
                                if (receiptAmt >= interestAmt) {
                                    receiptAmt -= interestAmt;
                                    dataMap.put("INTEREST", interestAmt);
                                    totIntAmt = totIntAmt + interestAmt;
                                }else{
                                   dataMap.put("INTEREST", receiptAmt);
                                   totIntAmt = totIntAmt + receiptAmt;
                                   receiptAmt -= receiptAmt;                                   
                                }
                           }
                           if(principleAmt>0 && (firstRec-(chargeAmt+penalAmt+interestAmt))>0){
                                dataMap.put("PRINCIPLE", receiptAmt);
                                totPrincple =  totPrincple + receiptAmt;
                            }
                            //dataMap.put("PAYMENT", paymentAmt);
                            dataMap.put("TRANS_DT", currDt);
                            dataMap.put("TRANS_TYPE", CommonConstants.CREDIT);
                            dataMap.put("STATUS_DT", currDt);
                            dataMap.put("STATUS_BY", (String) map.get(CommonConstants.USER_ID));
                            dataMap.put("TRANS_ID", CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                            dataMap.put("TRANS_SLNO", new Long(CreditSlNo));
                            dataMap.put("INITIATED_BRANCH", BRANCH_ID);
                            System.out.println("############## dataMap " + dataMap);
                            insertGroupLoan(dataMap);
                            }
                     }
                    
                }
                groupLoanList = null;
                objLogDAO.addToLog(objLogTO);
            }
            
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
    
    private void insertDailyData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            if (dailyGroupLoanList != null && dailyGroupLoanList.size() > 0) {
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
                System.out.println("dailyGroupLoanList map####"+map);
                System.out.println("ACT_NUM####"+CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                prodMap.put("ACT_NUM", CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                List prodList = sqlMap.executeQueryForList("getProdIdForGroupLoan", prodMap);
                if (prodList != null && prodList.size() > 0) {
                    prodMap = (HashMap) prodList.get(0);
                }
                System.out.println("PRODmAP####"+prodMap);
                HashMap acHdmap = new HashMap();
                List acHdlst = sqlMap.executeQueryForList("getAccountHeadProdGroupLoanAD", prodMap);    // Acc Head
                if (acHdlst != null && acHdlst.size() > 0) {
                   acHdmap = (HashMap) acHdlst.get(0);
                }
                HashMap dataMap = new HashMap();
                double totalExcessTransAmt = 0.0;
                TxTransferTO transferTo = new TxTransferTO();
                ArrayList TxTransferTO = new ArrayList();
                HashMap tansactionMap = new HashMap();
                String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_ID"));    
                String initiatedBranch = CommonUtil.convertObjToStr(map.get("INITIATED_BRANCH"));
                    for (int k = 0; k < dailyGroupLoanList.size(); k++) {                        
                        dataMap = (HashMap) dailyGroupLoanList.get(k);
                        HashMap debitAcMap = new HashMap();                        
                        debitAcMap.put("ACT_NUM", CommonUtil.convertObjToStr(dataMap.get("CUST_ACT_NUM")));
                        List debitAclst = sqlMap.executeQueryForList("getAccNoProdIdDet", debitAcMap);   // Acc Head
                        if (debitAclst != null && debitAclst.size() > 0) {
                            debitAcMap = (HashMap) debitAclst.get(0);
                        }
                        receiptAmt = CommonUtil.convertObjToDouble(dataMap.get("TRANS_AMT"));
                        firstRec = receiptAmt;
                        double transAmt = receiptAmt;
                        principleAmt = CommonUtil.convertObjToDouble(dataMap.get("PRIN_DUE"));
                        interestAmt = CommonUtil.convertObjToDouble(dataMap.get("INTEREST"));
                        penalAmt = CommonUtil.convertObjToDouble(dataMap.get("PENAL"));
                        chargeAmt = CommonUtil.convertObjToDouble(dataMap.get("CHARGES"));                       
                        if(receiptAmt>0){
                            dataMap.put("CUST_ID", CommonUtil.convertObjToStr(dataMap.get("CUST_ID")));
                            dataMap.put("ACT_NUM", CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                            if(chargeAmt>0){
                                if (receiptAmt >= chargeAmt) {
                                    receiptAmt -= chargeAmt;
                                    dataMap.put("CHARGES", chargeAmt);
                                    totchargeAmt = totchargeAmt + chargeAmt;
                                }else{
                                    dataMap.put("CHARGES", receiptAmt);
                                    totchargeAmt = totchargeAmt + receiptAmt;
                                    receiptAmt -= receiptAmt;                                    
                                }
                           }if(penalAmt>0){
                               System.out.println("RECEIPT2222###"+receiptAmt);
                                    System.out.println("PENAL22222###"+penalAmt);
                                if (receiptAmt >= penalAmt) {
                                    receiptAmt -= penalAmt;
                                    dataMap.put("PENAL", penalAmt);
                                    totpenalAmt  = totpenalAmt + penalAmt;
                                }else{
                                    dataMap.put("PENAL", receiptAmt);
                                    totpenalAmt  = totpenalAmt + receiptAmt;
                                    receiptAmt -= receiptAmt;                                    
                                }
                                System.out.println("RECEIPT###"+receiptAmt);
                                
                           }if(interestAmt>0){
                                if (receiptAmt >= interestAmt) {
                                    receiptAmt -= interestAmt;
                                    dataMap.put("INTEREST", interestAmt);
                                    totIntAmt = totIntAmt + interestAmt;
                                }else{
                                   dataMap.put("INTEREST", receiptAmt);
                                   totIntAmt = totIntAmt + receiptAmt;
                                   receiptAmt -= receiptAmt;                                   
                                }
                           }
                           if(principleAmt>0 && (firstRec-(chargeAmt+penalAmt+interestAmt))>0){
                                dataMap.put("PRINCIPLE", receiptAmt);
                                totPrincple =  totPrincple + receiptAmt;
                            }
                                //Debit
                              if(transAmt>0){                                        
                                        txMap = new HashMap(); 
                                        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                                        transactionDAO.setInitiatedBranch(initiatedBranch);
                                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                        if (!CommonUtil.convertObjToStr(dataMap.get("CUST_ACT_PROD_TYPE")).equals("") && CommonUtil.convertObjToStr(dataMap.get("CUST_ACT_PROD_TYPE")).equals("GL")) {
                                            System.out.println("$#$$$#$#$# Prod Type GL " + transactionTO.getDebitAcctNo());
                                            txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(dataMap.get("CUST_ACT_NUM")));
                                            txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(dataMap.get("CUST_ACT_PROD_TYPE")));
                                            txMap.put(TransferTrans.PARTICULARS, "To Group Loan : " + CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                                        } else if (!CommonUtil.convertObjToStr(dataMap.get("CUST_ACT_PROD_TYPE")).equals("") && !CommonUtil.convertObjToStr(dataMap.get("CUST_ACT_PROD_TYPE")).equals("GL")) {
                                            System.out.println("$#$$$#$#$# Prod Type Not GL " + CommonUtil.convertObjToStr(dataMap.get("CUST_ACT_PROD_TYPE")));
                                            txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(debitAcMap.get("AC_HEAD")));
                                            txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(dataMap.get("CUST_ACT_NUM")));
                                            txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(dataMap.get("CUST_ACT_PROD_ID")));
                                            txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(dataMap.get("CUST_ACT_PROD_TYPE")));
                                            txMap.put(TransferTrans.PARTICULARS, "To Group Loan : " + CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                                        }
                                        txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
                                        txMap.put("TRANS_MOD_TYPE", CommonUtil.convertObjToStr(dataMap.get("CUST_ACT_PROD_TYPE")));
                                        transferTo = transactionDAO.addTransferDebitLocal(txMap,transAmt);
                                        transferTo.setTransId("-");
                                        transferTo.setBatchId("-");
                                        transferTo.setTransDt(currDt);
                                        transferTo.setInitiatedBranch(initiatedBranch);
                                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        transferTo.setLinkBatchId(CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        transferTo.setStatusDt(currDt);
                                        TxTransferTO.add(transferTo);
                                    }
                                }
                            }
                            double paymentAmtTran = firstRec;
                            double totAmtTran = firstRec;
                            System.out.println("################ totAmtTran :" + totAmtTran+""+firstRec);
                            System.out.println("################ totchargeAmt :" + totchargeAmt);
                            System.out.println("################ totpenalAmt :" + totpenalAmt);
                            System.out.println("################ totIntAmt :" + totAmtTran);
                            System.out.println("################ totPrincple :" + totPrincple);
                            String loanAccNo = "";
                            loanAccNo = CommonUtil.convertObjToStr(map.get("ACT_NUM"));                            
                            //CREDIT CHARGES
                            if(totchargeAmt>0){
                                txMap.put(TransferTrans.CR_AC_HD, (String) (String) acHdmap.get("OTHRCHRGS_HD"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, "To Group Loan Charge : " + loanAccNo);
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                txMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
                                txMap.put("TRANS_MOD_TYPE",CommonConstants.ADVANCE_TRANSMODE_TYPE);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, totchargeAmt);
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
                                transferTo.setAuthorizeRemarks("OTHER_CHARGES");
                                transferTo.setHierarchyLevel("1");
                                transferTo.setInstrumentNo2("LOAN_OTHER_CHARGES");
                                transactionDAO.setLinkBatchID(loanAccNo);
                                TxTransferTO.add(transferTo);
                            }
                            //CREDIT PENAL
                            if(totpenalAmt>0){
                                txMap.put(TransferTrans.CR_AC_HD, (String) (String) acHdmap.get("PENAL_INT"));
                                txMap.put(TransferTrans.CR_PROD_TYPE , TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, "To Group Loan Penal : " + loanAccNo);
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                txMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
                                txMap.put("TRANS_MOD_TYPE",CommonConstants.ADVANCE_TRANSMODE_TYPE);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, totpenalAmt);
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
                                transferTo.setAuthorizeRemarks("PENAL_INT");
                                transferTo.setHierarchyLevel("2");
                                transferTo.setInstrumentNo2("LOAN_PENAL_INT");
                                transactionDAO.setLinkBatchID(loanAccNo);
                                TxTransferTO.add(transferTo);
                            }
                            //CREDIT INTEREST
                            if(totIntAmt>0){
                                txMap.put(TransferTrans.CR_AC_HD, (String) (String) acHdmap.get("AC_DEBIT_INT"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, "To Group Loan Interest : " + loanAccNo);
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                txMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
                                txMap.put("TRANS_MOD_TYPE",CommonConstants.ADVANCE_TRANSMODE_TYPE);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, totIntAmt);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setTransDt(currDt);
                                transferTo.setBranchId(BRANCH_ID);
                                transferTo.setInitiatedBranch(initiatedBranch);
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setStatusDt(currDt);
                                transferTo.setLinkBatchId(loanAccNo);
                                transactionDAO.setLinkBatchID(loanAccNo);
                                transferTo.setAuthorizeRemarks("INTEREST");
                                transferTo.setHierarchyLevel("3");
                                transferTo.setInstrumentNo2("LOAN_INTEREST");
                                TxTransferTO.add(transferTo);
                            }
                            //CREDIT PRINCIPLE
                            if (totPrincple  > 0) {
                                txMap.put(TransferTrans.CR_AC_HD, (String) (String) acHdmap.get("AC_HEAD"));
                                txMap.put(TransferTrans.CR_ACT_NUM, loanAccNo);
                                txMap.put(TransferTrans.CR_PROD_ID, prodMap.get("PROD_ID"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                                txMap.put(TransferTrans.PARTICULARS, "To Group Loan Principle : " + loanAccNo);
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                txMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
                                txMap.put("TRANS_MOD_TYPE",CommonConstants.ADVANCE_TRANSMODE_TYPE);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, totPrincple);
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
                                transactionDAO.setLinkBatchID(loanAccNo);
                                transferTo.setAuthorizeRemarks("PRINCIPAL");
                                transferTo.setHierarchyLevel("4");
                                transferTo.setInstrumentNo2("LOAN_PRINCIPAL");
                                TxTransferTO.add(transferTo);
                            }
                        transferDAO = new TransferDAO();
                        tansactionMap.put("TxTransferTO", TxTransferTO);
                        tansactionMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                        tansactionMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                        tansactionMap.put(CommonConstants.BRANCH_ID, initiatedBranch);
                        System.out.println("################ tansactionMap :" + tansactionMap);
                        transMap = transferDAO.execute(tansactionMap, false);
                        getTransDetails(loanAccNo);
                                             //TransID and TransSlNO
                     for (int k = 0; k < dailyGroupLoanList.size(); k++) {
                        HashMap data = new HashMap();
                        long CreditSlNo = 0;
                        data = (HashMap) dailyGroupLoanList.get(k);
                        receiptAmt = CommonUtil.convertObjToDouble(data.get("TRANS_AMT"));
                        firstRec = receiptAmt;
                        principleAmt = CommonUtil.convertObjToDouble(data.get("PRIN_DUE"));
                        interestAmt = CommonUtil.convertObjToDouble(data.get("INTEREST"));
                        penalAmt = CommonUtil.convertObjToDouble(data.get("PENAL"));
                        chargeAmt = CommonUtil.convertObjToDouble(data.get("CHARGES"));
                        //Trans count
                        HashMap slNoMap = new HashMap();
                        slNoMap.put("CUST_ID", CommonUtil.convertObjToStr(data.get("CUST_ID")));
                        slNoMap.put("ACT_NUM", CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                        List slNolst = sqlMap.executeQueryForList("getSL_NO", slNoMap);
                        if (slNolst != null && slNolst.size() > 0) {
                            slNoMap = (HashMap) slNolst.get(0);
                            CreditSlNo = CommonUtil.convertObjToLong(slNoMap.get("SL_NO"));
                        }
                        CreditSlNo++;
                        if(receiptAmt>0){
                            data.put("CUST_ID", CommonUtil.convertObjToStr(data.get("CUST_ID")));
                            data.put("ACT_NUM", CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                            if(chargeAmt>0){
                                if (receiptAmt >= chargeAmt) {
                                    receiptAmt -= chargeAmt;
                                    data.put("CHARGES", chargeAmt);
                                    totchargeAmt = totchargeAmt + chargeAmt;
                                }else{
                                    data.put("CHARGES", receiptAmt);
                                    totchargeAmt = totchargeAmt + receiptAmt;
                                    receiptAmt -= receiptAmt;                                    
                                }
                           }if(penalAmt>0){
                               System.out.println("RECEIPT2222###"+receiptAmt);
                                    System.out.println("PENAL22222###"+penalAmt);
                                if (receiptAmt >= penalAmt) {
                                    receiptAmt -= penalAmt;
                                    data.put("PENAL", penalAmt);
                                    totpenalAmt  = totpenalAmt + penalAmt;
                                }else{
                                    data.put("PENAL", receiptAmt);
                                    totpenalAmt  = totpenalAmt + receiptAmt;
                                    receiptAmt -= receiptAmt;                                    
                                }
                                System.out.println("RECEIPT###"+receiptAmt);
                                
                           }if(interestAmt>0){
                                if (receiptAmt >= interestAmt) {
                                    receiptAmt -= interestAmt;
                                    data.put("INTEREST", interestAmt);
                                    totIntAmt = totIntAmt + interestAmt;
                                }else{
                                   data.put("INTEREST", receiptAmt);
                                   totIntAmt = totIntAmt + receiptAmt;
                                   receiptAmt -= receiptAmt;                                   
                                }
                           }
                           if(principleAmt>0 && (firstRec-(chargeAmt+penalAmt+interestAmt))>0){
                                data.put("PRINCIPLE", receiptAmt);
                                totPrincple =  totPrincple + receiptAmt;
                            }
                            //dataMap.put("PAYMENT", paymentAmt);
                            data.put("TRANS_DT", currDt);
                            data.put("TRANS_TYPE", CommonConstants.CREDIT);
                            data.put("STATUS_DT", currDt);
                            data.put("STATUS_BY", (String) map.get(CommonConstants.USER_ID));
                            data.put("TRANS_ID", CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                            data.put("TRANS_SLNO", new Long(CreditSlNo));
                            data.put("INITIATED_BRANCH", BRANCH_ID);
                            System.out.println("############## data " + data);
                            insertGroupLoan(data);
                            }
                     }
                                  
                dailyGroupLoanList = null;
                objLogDAO.addToLog(objLogTO);
            }
            
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
    
    public CashTransactionTO setCashTransaction(CashTransactionTO cashTo) {
        //log.info("In setCashTransaction()");
        Date curDate = (Date) currDt.clone();
        final CashTransactionTO objCashTransactionTO = new CashTransactionTO();
        try {
            objCashTransactionTO.setTransId(cashTo.getTransId());
            objCashTransactionTO.setAcHdId(cashTo.getAcHdId());
            objCashTransactionTO.setProdId(cashTo.getProdId());
            objCashTransactionTO.setProdType(cashTo.getProdType());
            objCashTransactionTO.setActNum(cashTo.getActNum());
            objCashTransactionTO.setInpAmount(cashTo.getInpAmount());
            objCashTransactionTO.setInpCurr(cashTo.getInpCurr());
            objCashTransactionTO.setAmount(cashTo.getAmount());
            objCashTransactionTO.setTransType(cashTo.getTransType());
            objCashTransactionTO.setInstType(cashTo.getInstType());
            objCashTransactionTO.setBranchId(cashTo.getBranchId());
            objCashTransactionTO.setStatusBy(cashTo.getStatusBy());
            objCashTransactionTO.setInstrumentNo1(cashTo.getInstrumentNo1());
            objCashTransactionTO.setInstrumentNo2(cashTo.getInstrumentNo2());
            Date InsDt = cashTo.getInstDt();
            if (InsDt != null) {
                Date insDate = (Date) curDate.clone();
                insDate.setDate(InsDt.getDate());
                insDate.setMonth(InsDt.getMonth());
                insDate.setYear(InsDt.getYear());
                //            objCashTransactionTO.setInstDt(DateUtil.getDateMMDDYYYY(getTdtInstrumentDate()));
                objCashTransactionTO.setInstDt(insDate);
            } else {
                objCashTransactionTO.setInstDt(cashTo.getInstDt());
            }
            objCashTransactionTO.setTokenNo(cashTo.getTokenNo());
            objCashTransactionTO.setInitTransId(cashTo.getInitTransId());
            objCashTransactionTO.setInitChannType(cashTo.getInitChannType());
            objCashTransactionTO.setParticulars(cashTo.getParticulars());
            objCashTransactionTO.setInitiatedBranch(cashTo.getInitiatedBranch());
            objCashTransactionTO.setCommand(cashTo.getCommand());
            objCashTransactionTO.setAuthorizeStatus_2(cashTo.getAuthorizeStatus_2());
            objCashTransactionTO.setTransAllId(cashTo.getTransAllId());
            objCashTransactionTO.setSingleTransId(cashTo.getSingleTransId());
            objCashTransactionTO.setTransModType(cashTo.getTransModType());
            System.out.println("objCashTransactionTO:" + objCashTransactionTO);
        } catch (Exception e) {
            //log.info("Error In setInwardClearing()");
            e.printStackTrace();
        }
        return objCashTransactionTO;
    }
    
    private void insertGroupLoan(HashMap map) throws Exception {
        try {
            HashMap insertGroupLoanMap = setInsertGroupLoan(map);
            GroupLoanTO groupLoanTO = new GroupLoanTO();
            if (insertGroupLoanMap.containsKey("GROUP_LOAN_TRANS")) {
            	groupLoanTO = (GroupLoanTO) insertGroupLoanMap.get("GROUP_LOAN_TRANS");                
            }
            sqlMap.executeUpdate("insertGroupLoanTansactionDetails", groupLoanTO);
        }catch (Exception e) {
            e.printStackTrace();
            throw new TransRollbackException(e);        
        }
    }
    private HashMap setInsertGroupLoan(HashMap map) throws Exception {
        HashMap dataMap = new HashMap();
        GroupLoanTO groupLoanTO = new GroupLoanTO();
        groupLoanTO.setCustId(CommonUtil.convertObjToStr(map.get("CUST_ID")));
        groupLoanTO.setPrinciple(CommonUtil.convertObjToStr(map.get("PRINCIPLE")));
        groupLoanTO.setInterest(CommonUtil.convertObjToStr(map.get("INTEREST")));
        groupLoanTO.setPenal(CommonUtil.convertObjToStr(map.get("PENAL")));
        groupLoanTO.setCharges(CommonUtil.convertObjToStr(map.get("CHARGES")));
        groupLoanTO.setTransDt(getProperDateFormat(map.get("TRANS_DT")));
        groupLoanTO.setTransType(CommonUtil.convertObjToStr(map.get("TRANS_TYPE")));
        groupLoanTO.setStatus(CommonConstants.STATUS_CREATED);
        groupLoanTO.setStatusDt(getProperDateFormat(map.get("STATUS_DT")));
        groupLoanTO.setStatusBy(CommonUtil.convertObjToStr(map.get("STATUS_BY")));
        groupLoanTO.setGrpLoanNo(CommonUtil.convertObjToStr(map.get("ACT_NUM")));
        groupLoanTO.setRepayDt(getProperDateFormat(map.get("REPAY_DT")));
        groupLoanTO.setTransId(CommonUtil.convertObjToStr(map.get("TRANS_ID")));
        groupLoanTO.setSlNo(CommonUtil.convertObjToStr(map.get("TRANS_SLNO")));
        groupLoanTO.setNarration(CommonUtil.convertObjToStr(map.get("NARRATION")));
        groupLoanTO.setBranchId(CommonUtil.convertObjToStr(map.get("INITIATED_BRANCH")));
        dataMap.put("GROUP_LOAN_TRANS", groupLoanTO);
        System.out.println("groupLoanTO####"+groupLoanTO);
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
                    objCashTO.setAcHdId(CommonUtil.convertObjToStr(txnMap.get("ACCT_HEAD")));
                    objCashTO.setProdType(CommonUtil.convertObjToStr(txnMap.get("PROD_TYPE")));
                    if(objCashTO.getProdType().equals(TransactionFactory.ADVANCES)){
                        objCashTO.setProdId(CommonUtil.convertObjToStr(txnMap.get("PROD_ID")));
                    }
                    objCashTO.setTransType(CommonConstants.CREDIT);
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
                    objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(txnMap.get("LINK_BATCH_ID")));
                    objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(txnMap.get("ACCT_NUM")));
                    objCashTO.setScreenName("Group Loan Transaction");
                    objCashTO.setTransModType(CommonConstants.ADVANCE_TRANSMODE_TYPE);
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
            if (groupLoanList!= null) {
                System.out.println("IN Group groupLoanList####"+groupLoanList);
                String linkBatchId = "";
                for (int i = 0; i < groupLoanList.size(); i++) {
                    HashMap dataMap = new HashMap();
                    dataMap = (HashMap) groupLoanList.get(i);
                    double paymentAmt = 0.0;
                    linkBatchId = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));
                    AuthorizeMap.put("ACT_NUM",linkBatchId);
                    AuthorizeMap.put("CUST_ID",CommonUtil.convertObjToStr(dataMap.get("CUST_ID")));
                    System.out.println("#$$$$$$$$$$$$$ AuthorizeMap : " + AuthorizeMap);
                    sqlMap.executeUpdate("authorizeGroupLoanReceipt", AuthorizeMap);
                    //                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                    //paymentAmt = CommonUtil.convertObjToDouble(dataMap.get("PAYMENT")).doubleValue();
                }
                System.out.println("-------------------> transactionTO : " + transactionTO);
                if (transactionTO.getTransType().equals("TRANSFER")) {
                    if (totalExcessTransAmt > 0) {
                        HashMap transferTransParam = new HashMap();
                        transferDAO = new TransferDAO();
                        transferTransParam.put(CommonConstants.BRANCH_ID, BRANCH_ID);
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
                    HashMap authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
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
                        dataMap = null;
                    }
                }  
                groupLoanList = null;
            }
            if (groupLoanPaymentList!= null) {
                System.out.println("IN Group Payment####"+groupLoanPaymentList);
                String linkBatchId = "";
                for (int i = 0; i < groupLoanPaymentList.size(); i++) {
                    HashMap dataMap = new HashMap();
                    dataMap = (HashMap) groupLoanPaymentList.get(i);
                    double paymentAmt = 0.0;
                    linkBatchId = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));
                    AuthorizeMap.put("ACT_NUM",linkBatchId);
                    AuthorizeMap.put("CUST_ID",CommonUtil.convertObjToStr(dataMap.get("CUST_ID")));
                    System.out.println("#$$$$$$$$$$$$$ AuthorizeMap : " + AuthorizeMap);
                    sqlMap.executeUpdate("authorizeGroupLoanPayment", AuthorizeMap);
                    //                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                    //paymentAmt = CommonUtil.convertObjToDouble(dataMap.get("PAYMENT")).doubleValue();
                }
                System.out.println("-------------------> transactionTO : " + transactionTO);
                if (transactionTO.getTransType().equals("TRANSFER")) {
                    if (totalExcessTransAmt > 0) {
                        HashMap transferTransParam = new HashMap();
                        transferDAO = new TransferDAO();
                        transferTransParam.put(CommonConstants.BRANCH_ID, BRANCH_ID);
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
                    HashMap authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
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
                        dataMap = null;
                    }
                }
                groupLoanPaymentList = null;
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
        groupLoanList = null;
        groupLoanPaymentList = null;
        dailyGroupLoanList = null;
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
            GroupLoanDAO dao = new GroupLoanDAO();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
