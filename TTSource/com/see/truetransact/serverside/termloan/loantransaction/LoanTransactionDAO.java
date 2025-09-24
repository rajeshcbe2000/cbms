/*
 * Copyright 2012 Fincuro Solutions (P) Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * LoanTransactionDAO.java
 *
 * Created on Fri Jan 11 13:11:28 IST 2019
 */
package com.see.truetransact.serverside.termloan.loantransaction;

import java.util.HashMap;
import java.util.Date;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.*;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;
import com.see.truetransact.serverside.batchprocess.task.operativeaccount.interest.InterestTask;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.termloan.loanTransaction.LoanTransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import java.util.*;

/**
 * LoanTransaction DAO.
 *
 */
public class LoanTransactionDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private double paid_interest = 0;
    private double paid_penal_int = 0;
    private double paid_principal = 0;
    private String id = "";
    private TransactionDAO transactionDAO;
    private HashMap prodMap = new HashMap();
    private Date currDt = null;
    private String user = "";
    TransferDAO transferDAO = new TransferDAO();
    TransactionTO tto = null;
    LinkedHashMap transactionDetailsMap = new LinkedHashMap();
    TransactionTO transactionTODebit = new TransactionTO();
    private String generateSingleTransId = "";
    private String asAnWhen = "";
    private String transmode = "";
    private String commandMode = CommonConstants.TOSTATUS_INSERT;
    private HashMap acHeads = new HashMap();
    private String prodType = "";
    private String acno = "";
    private LoanTransactionTO objLoanTransactionTO;
    private HashMap resultMap = null;
    private String productBehaveLike = "";
    
    /**
     * Creates a new instance of LoanTransactionDAO
     */
    public LoanTransactionDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public static void main(String str[]) {
        try {
            LoanTransactionDAO dao = new LoanTransactionDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap obj) throws Exception {
        System.out.println("LoanTransaction Map Dao : " + obj);
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        user = CommonUtil.convertObjToStr(obj.get(CommonConstants.USER_ID));
        generateSingleTransId = generateLinkID();
        HashMap notdeleted = null;
        tto = null;
        if (obj.containsKey("TransactionTO") && obj.get("TransactionTO") != null) {
            notdeleted = (HashMap) obj.get("TransactionTO");
            transactionDetailsMap = (LinkedHashMap) obj.get("TransactionTO");
            if (notdeleted.containsKey("NOT_DELETED_TRANS_TOs") && notdeleted.get("NOT_DELETED_TRANS_TOs") != null) {
                notdeleted = (HashMap) notdeleted.get("NOT_DELETED_TRANS_TOs");
                if (notdeleted.containsKey("1")) {
                    tto = new TransactionTO();
                    tto = (TransactionTO) notdeleted.get("1");
                }
            }
        }
        if (obj.containsKey("AS_CUSTOMER_COMES") && obj.get("AS_CUSTOMER_COMES") != null) {
            asAnWhen = CommonUtil.convertObjToStr(obj.get("AS_CUSTOMER_COMES"));
        } else {
            asAnWhen = "";
        }
        if (tto != null) {
            transmode = CommonUtil.convertObjToStr(tto.getTransType());
        }
        objLoanTransactionTO = (LoanTransactionTO) obj.get("loantransaction");
        try {
            sqlMap.startTransaction();
            resultMap = new HashMap();
            //start
            if (obj.containsKey("AUTHORIZEMAP") && (obj.get("AUTHORIZEMAP") == null || ((HashMap) obj.get("AUTHORIZEMAP")).isEmpty())) {
            if (objLoanTransactionTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                transactionPartLoans(obj);
            }  
            else {
                throw new NoCommandException();
            }
        } else if (obj.containsKey("AUTHORIZEMAP") && obj.get("AUTHORIZEMAP") != null) {
            doAuthorize(obj);
        }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            sqlMap.rollbackTransaction();
        }
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return (resultMap);
    }
      private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        if (prodType.equals("TermLoan")){
            getTransMap.put("AUTHORIZE_STATUS", "AUTHORIZE_STATUS");
            getTransMap.put("SCREEN_NAME","LOAN FUTURE TRANSACTION");
        }
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        List transList = (List) sqlMap.executeQueryForList("getTransferDetails", getTransMap);
        if (transList != null && transList.size() > 0) {
            resultMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetails", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            resultMap.put("CASH_TRANS_LIST", cashList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    private void doAuthorize(HashMap map) throws Exception {
        HashMap dataMap = null;
        HashMap singleAuthorizeMap;
        String linkBatchId = null;
        HashMap cashAuthMap;
        String authorizeStatus;
        HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
        List authData = (List) authMap.get(CommonConstants.AUTHORIZEDATA);
        HashMap acHeads = null;
        HashMap getTransMap = null;
        String intAcHd = null;
        double intAmt = 0;
        if (authMap.containsKey("SER_TAX_AUTH")) {
            HashMap serMapAuth = (HashMap) authMap.get("SER_TAX_AUTH");
            sqlMap.executeUpdate("authorizeServiceTaxDetails", serMapAuth);
        }
        for (int i = 0, j = authData.size(); i < j; i++) {
            linkBatchId = CommonUtil.convertObjToStr(((HashMap) authData.get(i)).get("ACCOUNTNO"));//Transaction Batch Id
            singleAuthorizeMap = new HashMap();
            dataMap = new HashMap();
            authorizeStatus = CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS));
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("ACCOUNTNO", linkBatchId);
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
            singleAuthorizeMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            singleAuthorizeMap.put("AUTHORIZEDT", currDt);
            if (prodType.equals("TermLoan")) {
                if (authorizeStatus.equals("AUTHORIZED")) {
                    List intList = null;
                    HashMap prodMap = new HashMap();
                    prodMap.put("PROD_ID", CommonUtil.convertObjToStr(map.get("PROD_ID")));
                    List behavesLikeList = sqlMap.executeQueryForList("getLoanBehaves", prodMap);
                    HashMap behavesLikeMap = null;
                    String behavesLike = null;
                    if(behavesLikeList != null && behavesLikeList.size()>0){
                        behavesLikeMap = (HashMap) behavesLikeList.get(0);
                        if (behavesLikeMap != null && behavesLikeMap.size() > 0 && behavesLikeMap.containsKey("BEHAVES_LIKE")) {
                            behavesLike = CommonUtil.convertObjToStr(behavesLikeMap.get("BEHAVES_LIKE"));
                            HashMap interestMap = new HashMap();
                            interestMap.put(CommonConstants.ACT_NUM, linkBatchId);
                            if (behavesLike.equals("OD")) {
                                intList = sqlMap.executeQueryForList("IntCalculationDetailAD", interestMap);
                            } else {
                                intList = sqlMap.executeQueryForList("IntCalculationDetail", interestMap);
                            }
                        }
                    }
                    HashMap intMap = null;
                    if(intList != null && intList.size()>0){
                        intMap = (HashMap) intList.get(0);
                    }
                    Date lstIntCalcDt = null;
                    if(intMap != null && intMap.size()>0 && intMap.containsKey("LAST_INT_CALC_DT")){
                        lstIntCalcDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(intMap.get("LAST_INT_CALC_DT")));
                    }
                    HashMap lastIntCalcDt = new HashMap();
                    lastIntCalcDt.put("LASTINTCALCDT", CommonUtil.getProperDate(currDt, lstIntCalcDt));
                    lastIntCalcDt.put("ACCOUNTNO", linkBatchId);
                    sqlMap.executeUpdate("updateLoansFacilityDetailsTemp", lastIntCalcDt);
                    List resultList = sqlMap.executeQueryForList("getLoanFutureCalcDt", singleAuthorizeMap);
                    HashMap resultMap = null;
                    if (resultList != null && resultList.size() > 0) {
                        resultMap = (HashMap) resultList.get(0);
                    }
                    Date futureIntCalcDt = null;
                    if (resultMap != null && resultMap.size() > 0 && resultMap.containsKey("INT_CALC_UPTO_DT")) {
                        futureIntCalcDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(resultMap.get("INT_CALC_UPTO_DT")));
                    }
                    singleAuthorizeMap.put("LAST_CALC_DT", getProperDateFormat(DateUtil.addDaysProperFormat(futureIntCalcDt, -1)));
                    singleAuthorizeMap.put("END", currDt);
                    singleAuthorizeMap.put("CR_ACT_NUM", singleAuthorizeMap.get("ACCOUNTNO"));
                    sqlMap.executeUpdate("updateclearBal", singleAuthorizeMap);
                    //last int credit date updated for AD
                    sqlMap.executeUpdate("updateAdvancesAppDate", singleAuthorizeMap);
                    insertInterestDetails(singleAuthorizeMap);
                }
                   sqlMap.executeUpdate("authorizeUpdateLoanFutureTransactionTO", singleAuthorizeMap);
            }
            if (map.containsKey("TransactionTO") && map.get("TransactionTO") != null) {
                TransactionTO transactionTODebit = new TransactionTO();
                LinkedHashMap transactionDetailsMap = new LinkedHashMap();
                TransactionTO transactionTO;
                transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                LinkedHashMap allowedTransDetailsTO;
                if (transactionDetailsMap != null && transactionDetailsMap.size() > 0) {
                    if (transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs") && transactionDetailsMap.get("NOT_DELETED_TRANS_TOs") != null) {
                        allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                        if (allowedTransDetailsTO != null && (!allowedTransDetailsTO.isEmpty())) {
                            transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                            transactionTO.setStatus(CommonConstants.STATUS_DELETED);
                            transactionTO.setBranchId(_branchCode);
                            sqlMap.executeUpdate("deleteRemittanceIssueTransactionTO", transactionTO);
                        }
                    }
                }
            }
            singleAuthorizeMap = null;
            cashAuthMap = new HashMap();
            cashAuthMap.put("LOAN_CLOSING_SCREEN", "LOAN_CLOSING_SCREEN");
            cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
            cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            if (asAnWhen != null && asAnWhen.equals("Y")) {
                cashAuthMap.put("DAILY", "DAILY");
            }
            TransactionDAO.authorizeCashAndTransfer(linkBatchId, authorizeStatus, cashAuthMap);
            cashAuthMap = null;
        }
    }
    
        void insertInterestDetails(HashMap Interest) throws Exception {
        HashMap singleRecord = new HashMap();
        Interest.put("ACT_NUM", Interest.get("ACCOUNTNO"));
        List lst = sqlMap.executeQueryForList("selectLoanInterestTMP", Interest);
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                HashMap insertRecord = new HashMap();
                singleRecord = (HashMap) lst.get(i);
                insertRecord.put("ACT_NUM", singleRecord.get("ACT_NUM"));
                String from_dt = CommonUtil.convertObjToStr(singleRecord.get("FROM_DT"));
                Date fromDate = DateUtil.getDateMMDDYYYY(from_dt);
                insertRecord.put("FROM_DT",CommonUtil.getProperDate(currDt, fromDate));
                String to_dt = CommonUtil.convertObjToStr(singleRecord.get("TO_DATE"));
                Date toDate = DateUtil.getDateMMDDYYYY(to_dt);
                insertRecord.put("TO_DATE", CommonUtil.getProperDate(currDt,toDate));
                Double amt = CommonUtil.convertObjToDouble(singleRecord.get("AMT"));
                insertRecord.put("AMT", amt);
                int noOfdays = CommonUtil.convertObjToInt(singleRecord.get("NO_OF_DAYS"));
                insertRecord.put("NO_OF_DAYS", new Long(noOfdays));
                double totProduct = CommonUtil.convertObjToDouble(singleRecord.get("TOT_PRODUCT")).doubleValue();
                insertRecord.put("TOT_PRODUCT", new Double(totProduct));
                double intamt = CommonUtil.convertObjToDouble(singleRecord.get("INT_AMT")).doubleValue();
                insertRecord.put("INT_AMT", new Double(intamt));
                intamt = CommonUtil.convertObjToDouble(singleRecord.get("INT_RATE")).doubleValue();
                insertRecord.put("INT_RATE", new Double(intamt));
                insertRecord.put("ACT_NUM", singleRecord.get("PROD_ID"));
                String intcalcdt = CommonUtil.convertObjToStr(singleRecord.get("INT_CALC_DT"));
                Date intcalcDt = DateUtil.getDateMMDDYYYY(intcalcdt);
                insertRecord.put("INT_CALC_DT", CommonUtil.getProperDate(currDt,intcalcDt));
                insertRecord.put("CUST_ID", singleRecord.get("CUST_ID"));
                System.out.println("insertrecord####" + insertRecord);
                sqlMap.executeUpdate("insertLoanInterest", insertRecord);//singleRecord
            }
        }
    }
       
    private void transactionPartLoans(HashMap obj) throws Exception {
        try {
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            transactionDAO.setInitiatedBranch(_branchCode);
            if (obj.containsKey("MODE")) {
                commandMode = CommonUtil.convertObjToStr(obj.get("MODE"));
                transactionDAO.setCommandMode(CommonUtil.convertObjToStr(obj.get("MODE")));
            }
            transactionDAO.setInitiatedBranch(_branchCode);
            transactionDAO.setBatchId(objLoanTransactionTO.getActNum());
            transactionDAO.setBatchDate(currDt);            
            obj.put(TransferTrans.PARTICULARS, "A/c No " + "" + objLoanTransactionTO.getActNum());
            HashMap acHeadMap = new HashMap();
            acHeadMap.put("PROD_ID", obj.get("PROD_ID"));
            List lst = sqlMap.executeQueryForList("getAccountHeadProdTL", acHeadMap);
            if (lst != null && lst.size() > 0) {
                acHeadMap = (HashMap) lst.get(0);
            }
            HashMap debitMap = new HashMap();
            HashMap txMap = new HashMap();
            TxTransferTO transferTo = new TxTransferTO();
            //CREDIT
            txMap = new HashMap();
            transferTo = new TxTransferTO();
            txMap.put(TransferTrans.CR_PROD_TYPE, objLoanTransactionTO.getProdType());
            txMap.put(TransferTrans.CR_PROD_ID, obj.get("PROD_ID"));
            txMap.put(TransferTrans.CR_AC_HD, acHeadMap.get("AC_HEAD"));
            txMap.put(TransferTrans.CR_ACT_NUM, objLoanTransactionTO.getActNum());
            txMap.put(TransferTrans.PARTICULARS, objLoanTransactionTO.getActNum());
            txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(obj.get("BRANCH_CODE")));
            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
            if (objLoanTransactionTO.getProdType() != null && objLoanTransactionTO.getProdType().equalsIgnoreCase("AD")) {
                txMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
            } else {
                txMap.put("TRANS_MOD_TYPE", TransactionFactory.LOANS);
            }
            txMap.put("generateSingleTransId", generateSingleTransId);
            txMap.put("SCREEN_NAME", "LOAN FUTURE TRANSACTION");
            txMap.put(CommonConstants.USER_ID, obj.get(CommonConstants.USER_ID));
            txMap.put("USER", obj.get(CommonConstants.USER_ID));
            transactionDAO.addTransferCredit(txMap, tto.getTransAmt());
            obj.put("generateSingleTransId", generateSingleTransId);
            transactionDAO.setBreakLoanHierachy("");
            transactionDAO.setLoanAmtMap((HashMap) obj.get("TOTAL_AMOUNT"));
            transactionDAO.setScreenName("LOAN FUTURE TRANSACTION");
            transactionDAO.execute(obj);
            transactionDAO.doTransfer();
            transactionDAO.setLoanAmtMap(new HashMap());
            if (objLoanTransactionTO != null) {
                objLoanTransactionTO.setTransactionId(generateSingleTransId);
                sqlMap.executeUpdate("insertLoanTransactionTO", objLoanTransactionTO);
            }
            getTransDetails(objLoanTransactionTO.getActNum());
    
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
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
 

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        prodType = "";
        if (obj.containsKey("PROD_TYPE") && obj.get("PROD_TYPE") != null) {
            prodType = CommonUtil.convertObjToStr(obj.get("PROD_TYPE"));
        }
        System.out.println("obj : " + obj);
        if (prodType.equals("TermLoan")) {
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        } else {
            transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
        }
        if (obj.containsKey("MODE")) {
            this.commandMode = CommonUtil.convertObjToStr(obj.get("MODE"));
        }
        return getData(obj);
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

   
    private HashMap getData(HashMap map) throws Exception {
        System.out.println("Map in getData : " + map);
        HashMap returnMap = new HashMap();
        List list = null;
        HashMap where = new HashMap();
        HashMap clearBalMap = new HashMap();
        long noOfDays = 0;
        where.put("ACT_NUM", map.get(CommonConstants.MAP_WHERE));
        System.out.println("!!!!!!! where : " + where);
        String branchcode="";
        if (map.containsKey("PROD_TYPE") && prodType.equals("TermLoan")) {
            List branchList = sqlMap.executeQueryForList("getBranchTL", CommonUtil.convertObjToStr(map.get(CommonConstants.MAP_WHERE)));
            if (!branchList.isEmpty() && branchList != null) {
                branchcode = CommonUtil.convertObjToStr(branchList.get(0));
            } else {
                branchcode = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
            }
        }
        if (this.commandMode == null || this.commandMode.equals("") || this.commandMode.equals(CommonConstants.TOSTATUS_UPDATE) || this.commandMode.equals("AUTHORIZE") || this.commandMode.equals(CommonConstants.TOSTATUS_DELETE)) {     
            if (prodType.equals("TermLoan") && map.containsKey("PROD_TYPE")) {
                where.put("BRANCH_CODE", branchcode);
                list = (List) sqlMap.executeQueryForList("getLoanFutureTransactionChargeInfoInt", where);
            } 
        } 
        System.out.println("list : " + list);
        if(list!=null && list.size()>0)
        returnMap.put("AccountDetailsTO", (HashMap) list.get(0));
        System.out.println(">>> returnMap : " + returnMap);
        HashMap getDateMap = new HashMap();
        Date lastPayDate = new Date();
        List lst;
        where = null;
        list = transactionDAO.getData(map);
        System.out.println("list in A DAO " + list);
        if (list != null) {
            if (list.size() > 0) {
                HashMap whereMap = new HashMap();
                TransactionTO transactionTO = null;
                whereMap.put("LINK_BATCH_ID", map.get(CommonConstants.MAP_WHERE));
                whereMap.put("TODAY_DT", ServerUtil.getCurrentDateProperFormat(_branchCode));
                whereMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(_branchCode)));
                for (int i = 0; i < list.size(); i++) {
                    transactionTO = (TransactionTO) list.get(i);
                    List lstTrans = null;
                    if (transactionTO != null) {
                        if (transactionTO.getTransType().equals(CommonConstants.TX_CASH)) {
                            lstTrans = sqlMap.executeQueryForList("getCashTransBatchID", whereMap);
                            if (lstTrans != null) {
                                if (lstTrans.size() > 0) {
                                    returnMap.put("CASH_TRANS_DETAILS", lstTrans.get(0));
                                }
                            }
                        } else {
                            whereMap.put("AMOUNT", transactionTO.getTransAmt());
                            lstTrans = sqlMap.executeQueryForList("getTransferTransBatchID", whereMap);
                            if (lstTrans != null) {
                                if (lstTrans.size() > 0) {
                                    returnMap.put("TRANSFER_TRANS_DETAILS", lstTrans.get(0));
                                }
                            }
                        }
                    }
                    lstTrans = null;
                    transactionTO = null;
                }
                whereMap = null;
            }
        }
        returnMap.put("TransactionTO", list);
        list = null;
        System.out.println(">>> returnMap : " + returnMap);
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;
    }
    

    private void destroyObjects() {
        transactionDAO.setLoanAmtMap(new HashMap());
    }
}
