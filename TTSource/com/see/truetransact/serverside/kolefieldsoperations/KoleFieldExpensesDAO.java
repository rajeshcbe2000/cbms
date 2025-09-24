 /*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * UserDAO.java
 *
 * Created on September 08, 2003, 1:00 PM
 *
 * Modified on April 16, 2004, 10:42 AM
 *
 */
package com.see.truetransact.serverside.kolefieldsoperations;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.commonutil.CommonUtil;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.kolefieldsoperations.KoleFieldsExpensesTO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import java.util.Date;
/**
 * This is used for User Data Access.
 *
 * @author Karthik
 *
 * @modified Pinky
 */
public class KoleFieldExpensesDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private KoleFieldsExpensesTO objKoleFieldsExpensesTO;
     TransferDAO transferDAO = new TransferDAO();
    private List list;
    // For Maintaining Logs...
    private LogDAO logDAO;
    private LogTO logTO;
    private final static Logger log = Logger.getLogger(KoleFieldExpensesDAO.class);
    private Date currDt = null;
    private String generateSingleTransId="";
    private String transType ="";
    private TransactionDAO transactionDAO = null;
    /**
     * Creates a new instance of roleDAO
     */
    public KoleFieldExpensesDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSelectKoleFieldsExpensesTO", map);
        returnMap.put("KoleFieldsExpensesTO", list);
        HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.MAP_WHERE, CommonUtil.convertObjToStr(map.get("KOLE_FIELD_EXPENSE_ID")));
        List list1 = transactionDAO.getData(whereMap);
        returnMap.put("TransactionTO", list1);
        return returnMap;
    }

    private String getKoleFieldExpenseId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "KOLE_FIELD_EXPENSE_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }
    private void insertTransactionDetails(HashMap map) throws Exception {
        List loanAuthTransList = new ArrayList();
        System.out.println("map ---- :" + map.get("TransactionTo"));
        String screenName = "";
        if(map.containsKey("SCREEN") && map.get("SCREEN") != null){
            screenName = CommonUtil.convertObjToStr(map.get("SCREEN"));
        }
        HashMap editMap = new HashMap();
        HashMap transTomap = (HashMap) map.get("TransactionTo");
        System.out.println("transTomap  :" + transTomap);
        TransactionTO objTransTo = new TransactionTO();
        if (transTomap != null && transTomap.size() > 0) {

            objTransTo = (TransactionTO) transTomap.get("1");
            System.out.println("objTransTo-- :" + objTransTo);
        }
        generateSingleTransId = generateLinkID();
        HashMap acHeads = new HashMap();
        HashMap toMaap = new HashMap();
        toMaap.put("PROD_ID", objKoleFieldsExpensesTO.getProdId());
        System.out.println("toMaap DDD :" + toMaap);
        acHeads = (HashMap) sqlMap.executeQueryForObject("getSuspenseIntHead", toMaap);
        System.out.println("acHeads " + acHeads);
        transType = objTransTo.getTransType();
        HashMap loanAuthTransMap = new HashMap();
        if (objKoleFieldsExpensesTO.getTransType().equals("DEBIT")) {
            if (objTransTo != null && objTransTo.getTransType() != null && objTransTo.getTransType().equals("CASH")) {
                objKoleFieldsExpensesTO.setTransMode("CASH");
                loanAuthTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                loanAuthTransMap.put("BRANCH_CODE", _branchCode);
                loanAuthTransMap.put("ACCT_HEAD", acHeads.get("AC_HD_ID"));
                loanAuthTransMap.put("LIMIT", objTransTo.getTransAmt());//dataMap.get("LIMIT"));
                loanAuthTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                loanAuthTransMap.put("USER_ID", map.get("USER"));
                loanAuthTransMap.put("TRANS_MOD_TYPE", TransactionFactory.SUSPENSE);
                loanAuthTransMap.put("SCREEN_NAME", screenName);
                loanAuthTransMap.put("PROD_TYPE",TransactionFactory.SUSPENSE);
                loanAuthTransMap.put(TransferTrans.DR_INSTRUMENT_2, "PRINCIPAL");
                loanAuthTransMap.put("TRANS_TYPE",objKoleFieldsExpensesTO.getTransType());
                CashTransactionTO objCashTransactionTO = loanAuthorizeTimeTransaction(loanAuthTransMap);
                objCashTransactionTO.setActNum(objKoleFieldsExpensesTO.getAcctNum());
                objCashTransactionTO.setProdId(objKoleFieldsExpensesTO.getProdId());
                loanAuthTransList.add(objCashTransactionTO);
                loanAuthTransMap.put("DAILYDEPOSITTRANSTO", loanAuthTransList);
                CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                HashMap cashMap = new HashMap();
                if (objTransTo.getTransAmt() > 0) {
                    cashMap = cashTransactionDAO.execute(loanAuthTransMap, false);
                }
                cashMap.put("BRANCH_CODE", _branchCode);
                 if(cashMap != null && cashMap.containsKey("TRANS_ID") && cashMap.get("TRANS_ID") != null){
                    objKoleFieldsExpensesTO.setTransId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID") ));
                }
            } else {
                objKoleFieldsExpensesTO.setTransMode("TRANSFER");
                TxTransferTO transferTo = new TxTransferTO();
                TransactionDAO transactionDAO = null;
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                ArrayList transList = new ArrayList();
                HashMap crMap = new HashMap();
                String oldAcctNo = "";
                crMap.put("ACT_NUM", objTransTo.getDebitAcctNo());
                if (CommonUtil.convertObjToStr(objTransTo.getProductType()).equals(TransactionFactory.OPERATIVE)) {
                    List oaAcctHead = sqlMap.executeQueryForList("getAccNoProdIdDet", crMap);
                    if (oaAcctHead != null && oaAcctHead.size() > 0) {
                        crMap = (HashMap) oaAcctHead.get(0);
                    }
                } else if (CommonUtil.convertObjToStr(objTransTo.getProductType()).equals(TransactionFactory.SUSPENSE)) {
                    List oaAcctHead = sqlMap.executeQueryForList("getSAAccNoProdIdDet", crMap);
                    if (oaAcctHead != null && oaAcctHead.size() > 0) {
                        crMap = (HashMap) oaAcctHead.get(0);
                    }
                }
                HashMap txMap = new HashMap();
                txMap.put(TransferTrans.DR_AC_HD, acHeads.get("AC_HD_ID"));
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.SUSPENSE);
                txMap.put(TransferTrans.DR_PROD_ID, objKoleFieldsExpensesTO.getProdId());
                txMap.put(TransferTrans.DR_ACT_NUM, objKoleFieldsExpensesTO.getAcctNum());
                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put("DR_INST_TYPE", "VOUCHER");
                txMap.put(TransferTrans.DR_INSTRUMENT_2,"PRINCIPAL");
                txMap.put(TransferTrans.PARTICULARS, objKoleFieldsExpensesTO.getAcctNum());
                txMap.put("TRANS_MOD_TYPE", TransactionFactory.SUSPENSE);
                txMap.put("INITIATED_BRANCH", _branchCode);
                transferTo = transactionDAO.addTransferDebitLocal(txMap, objKoleFieldsExpensesTO.getTransAmount());
                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER")));
                transferTo.setSingleTransId(generateSingleTransId);
                transferTo.setLinkBatchId(objKoleFieldsExpensesTO.getKoleFieldExpenseId());
                transferTo.setGlTransActNum(objKoleFieldsExpensesTO.getAcctNum());
                transferTo.setScreenName(screenName);
                transferTo.setInitiatedBranch(_branchCode);
                transferTo.setNarration(objKoleFieldsExpensesTO.getKoleFieldExpenseId());
                transList.add(transferTo);
                System.out.println("dataMap KKK K:" + txMap);
                if (CommonUtil.convertObjToStr(objTransTo.getProductType()).equals(TransactionFactory.OPERATIVE)) {
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                    txMap.put(TransferTrans.CR_ACT_NUM, objTransTo.getDebitAcctNo());
                    txMap.put(TransferTrans.CR_AC_HD, crMap.get("AC_HD_ID"));
                    txMap.put(TransferTrans.CR_PROD_ID, crMap.get("PROD_ID"));
                } else if (CommonUtil.convertObjToStr(objTransTo.getProductType()).equals(TransactionFactory.SUSPENSE)) {
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.SUSPENSE);
                    txMap.put(TransferTrans.CR_ACT_NUM, objTransTo.getDebitAcctNo());
                    txMap.put(TransferTrans.CR_AC_HD, crMap.get("AC_HD_ID"));
                    txMap.put(TransferTrans.CR_PROD_ID, crMap.get("PROD_ID"));
                } else if (CommonUtil.convertObjToStr(objTransTo.getProductType()).equals(TransactionFactory.GL)) {
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.CR_AC_HD, objTransTo.getDebitAcctNo());
                }
                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                txMap.put("TRANS_MOD_TYPE", TransactionFactory.LOANS); 
                txMap.put(TransferTrans.PARTICULARS, objKoleFieldsExpensesTO.getAcctNum());
                txMap.put("generateSingleTransId", generateSingleTransId);
                if (CommonUtil.convertObjToStr(objTransTo.getProductType()).equals(TransactionFactory.OPERATIVE)) {
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.OPERATIVE);
                } else if (CommonUtil.convertObjToStr(objTransTo.getProductType()).equals(TransactionFactory.SUSPENSE)) {
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.SUSPENSE);
                } else if (CommonUtil.convertObjToStr(objTransTo.getProductType()).equals(TransactionFactory.GL)) {
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                }
                txMap.put("INITIATED_BRANCH", _branchCode);
                transferTo = transactionDAO.addTransferCreditLocal(txMap, objTransTo.getTransAmt());
                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER")));
                transferTo.setLinkBatchId(objKoleFieldsExpensesTO.getKoleFieldExpenseId());
                transferTo.setGlTransActNum(objKoleFieldsExpensesTO.getAcctNum());
                transferTo.setScreenName(screenName);
                transferTo.setInitiatedBranch(_branchCode);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                transList.add(transferTo);
                //transactionDAO.setInitiatedBranch(_branchCode);
                //transactionDAO.doTransferLocal(transList, _branchCode);
                map.put("MODE", map.get("COMMAND"));
                map.put("COMMAND", map.get("MODE"));
                map.put("TxTransferTO", transList);
                map.put(CommonConstants.SELECTED_BRANCH_ID, _branchCode);
                HashMap transMap = transferDAO.execute(map, false);
                if(transMap != null && transMap.containsKey("TRANS_ID") && transMap.get("TRANS_ID") != null){
                    objKoleFieldsExpensesTO.setBatchId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID") ));
                }
            }
        } else if (objKoleFieldsExpensesTO.getTransType().equals("CREDIT")) {
            if (objTransTo != null && objTransTo.getTransType() != null && objTransTo.getTransType().equals("CASH")) {
                HashMap cashMap = new HashMap();
                CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                objKoleFieldsExpensesTO.setTransMode("CASH");
                loanAuthTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                loanAuthTransMap.put("BRANCH_CODE", _branchCode);
                loanAuthTransMap.put("ACCT_HEAD", acHeads.get("AC_HD_ID"));
                loanAuthTransMap.put("LIMIT", objKoleFieldsExpensesTO.getTransAmount());//dataMap.get("LIMIT"));
                loanAuthTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                loanAuthTransMap.put("USER_ID", map.get("USER"));
                loanAuthTransMap.put(TransferTrans.DR_INSTRUMENT_2, "PRINCIPAL");
                loanAuthTransMap.put("PROD_TYPE", TransactionFactory.SUSPENSE);
                loanAuthTransMap.put("TRANS_MOD_TYPE", TransactionFactory.SUSPENSE);
                loanAuthTransMap.put("SCREEN_NAME", screenName);
                loanAuthTransMap.put("TRANS_TYPE",objKoleFieldsExpensesTO.getTransType());
                CashTransactionTO objCashTransactionTO = loanAuthorizeTimeTransaction(loanAuthTransMap);
                objCashTransactionTO.setActNum(objKoleFieldsExpensesTO.getAcctNum());
                objCashTransactionTO.setProdId(objKoleFieldsExpensesTO.getProdId());
                loanAuthTransList.add(objCashTransactionTO);
                System.out.println("loanAuthTransList first :: " + loanAuthTransList);
                loanAuthTransMap.put("DAILYDEPOSITTRANSTO", loanAuthTransList);
                cashMap = cashTransactionDAO.execute(loanAuthTransMap, false);
                System.out.println("cashMap principal :: " + cashMap);
                 if(cashMap != null && cashMap.containsKey("TRANS_ID") && cashMap.get("TRANS_ID") != null){
                    objKoleFieldsExpensesTO.setTransId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID") ));
                }
                if (objKoleFieldsExpensesTO.getIntAmount() > 0) {
                    loanAuthTransList = new ArrayList();
                    loanAuthTransMap.put("ACCT_HEAD", acHeads.get("INT_AC_HD"));
                    loanAuthTransMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                    loanAuthTransMap.put("PROD_TYPE", TransactionFactory.GL);
                    loanAuthTransMap.put("LIMIT", objKoleFieldsExpensesTO.getIntAmount());
                    loanAuthTransMap.put(TransferTrans.DR_INSTRUMENT_2, "INTEREST");
                    objCashTransactionTO = loanAuthorizeTimeTransaction(loanAuthTransMap);
                    loanAuthTransList.add(objCashTransactionTO);
                    System.out.println("loanAuthTransList last :: " + loanAuthTransList);
                    loanAuthTransMap.put("DAILYDEPOSITTRANSTO", loanAuthTransList);
                    cashMap = cashTransactionDAO.execute(loanAuthTransMap, false);
                    System.out.println("cashMap interest :: " + cashMap);
                    cashMap.put("BRANCH_CODE", _branchCode);
                    if (cashMap != null && cashMap.containsKey("TRANS_ID") && cashMap.get("TRANS_ID") != null) {
                        objKoleFieldsExpensesTO.setIntTransId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                    }
                }              
            } else {
                objKoleFieldsExpensesTO.setTransMode("TRANSFER");
                TxTransferTO transferTo = new TxTransferTO();
                TransactionDAO transactionDAO = null;
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                ArrayList transList = new ArrayList();
                HashMap crMap = new HashMap();
                String oldAcctNo = "";
                crMap.put("ACT_NUM", objTransTo.getDebitAcctNo());
                if (CommonUtil.convertObjToStr(objTransTo.getProductType()).equals(TransactionFactory.OPERATIVE)) {
                    List oaAcctHead = sqlMap.executeQueryForList("getAccNoProdIdDet", crMap);
                    if (oaAcctHead != null && oaAcctHead.size() > 0) {
                        crMap = (HashMap) oaAcctHead.get(0);
                    }
                } else if (CommonUtil.convertObjToStr(objTransTo.getProductType()).equals(TransactionFactory.SUSPENSE)) {
                    List oaAcctHead = sqlMap.executeQueryForList("getSAAccNoProdIdDet", crMap);
                    if (oaAcctHead != null && oaAcctHead.size() > 0) {
                        crMap = (HashMap) oaAcctHead.get(0);
                    }
                }
                HashMap txMap = new HashMap();
                
                //Debit from OA/GL/SA
                
                if (CommonUtil.convertObjToStr(objTransTo.getProductType()).equals(TransactionFactory.OPERATIVE)) {
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                    txMap.put(TransferTrans.DR_ACT_NUM, objTransTo.getDebitAcctNo());
                    txMap.put(TransferTrans.DR_AC_HD, crMap.get("AC_HD_ID"));
                    txMap.put(TransferTrans.DR_PROD_ID, crMap.get("PROD_ID"));
                } else if (CommonUtil.convertObjToStr(objTransTo.getProductType()).equals(TransactionFactory.SUSPENSE)) {
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.SUSPENSE);
                    txMap.put(TransferTrans.DR_ACT_NUM, objTransTo.getDebitAcctNo());
                    txMap.put(TransferTrans.DR_AC_HD, crMap.get("AC_HD_ID"));
                    txMap.put(TransferTrans.DR_PROD_ID, crMap.get("PROD_ID"));
                } else if (CommonUtil.convertObjToStr(objTransTo.getProductType()).equals(TransactionFactory.GL)) {
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.DR_AC_HD, objTransTo.getDebitAcctNo());
                }
                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put("DR_INST_TYPE", "VOUCHER");
                txMap.put(TransferTrans.PARTICULARS, objKoleFieldsExpensesTO.getAcctNum());
                txMap.put("TRANS_MOD_TYPE", TransactionFactory.SUSPENSE);
                txMap.put("INITIATED_BRANCH", _branchCode);
                txMap.put(TransferTrans.PARTICULARS, objKoleFieldsExpensesTO.getAcctNum());
                txMap.put("TRANS_MOD_TYPE", objTransTo.getProductType());
                transferTo = transactionDAO.addTransferDebitLocal(txMap, objTransTo.getTransAmt());
                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER")));
                transferTo.setSingleTransId(generateSingleTransId);
                transferTo.setLinkBatchId(objKoleFieldsExpensesTO.getKoleFieldExpenseId());
                transferTo.setGlTransActNum(objKoleFieldsExpensesTO.getAcctNum());
                transferTo.setScreenName(screenName);
                 transferTo.setInitiatedBranch(_branchCode);
                transList.add(transferTo);
                
                
                txMap = new HashMap();
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.SUSPENSE);
                txMap.put(TransferTrans.CR_ACT_NUM, objKoleFieldsExpensesTO.getAcctNum());
                txMap.put(TransferTrans.CR_AC_HD, acHeads.get("AC_HD_ID"));
                txMap.put(TransferTrans.CR_PROD_ID, objKoleFieldsExpensesTO.getProdId());
                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                txMap.put(TransferTrans.DR_INSTRUMENT_2,"PRINCIPAL");
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put("DR_INST_TYPE", "VOUCHER");
                txMap.put(TransferTrans.PARTICULARS, objKoleFieldsExpensesTO.getAcctNum());
                txMap.put("TRANS_MOD_TYPE", TransactionFactory.SUSPENSE);
                txMap.put("INITIATED_BRANCH", _branchCode);
                txMap.put("TRANS_MOD_TYPE", TransactionFactory.SUSPENSE);
                transferTo = transactionDAO.addTransferCreditLocal(txMap, objKoleFieldsExpensesTO.getTransAmount());
                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER")));
                transferTo.setSingleTransId(generateSingleTransId);
                transferTo.setLinkBatchId(objKoleFieldsExpensesTO.getKoleFieldExpenseId());
                transferTo.setGlTransActNum(objKoleFieldsExpensesTO.getAcctNum());
                transferTo.setScreenName(screenName);
                transferTo.setInitiatedBranch(_branchCode);
                transferTo.setNarration(objKoleFieldsExpensesTO.getKoleFieldExpenseId());
                transList.add(transferTo);
                
                if (objKoleFieldsExpensesTO.getIntAmount() > 0) {
                    txMap = new HashMap();
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.CR_ACT_NUM, objKoleFieldsExpensesTO.getAcctNum());
                    txMap.put(TransferTrans.CR_AC_HD, acHeads.get("INT_AC_HD"));
                    txMap.put(TransferTrans.CR_PROD_ID, TransactionFactory.GL);
                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                    txMap.put(TransferTrans.DR_INSTRUMENT_2,"INTEREST");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put("DR_INST_TYPE", "VOUCHER");
                    txMap.put(TransferTrans.PARTICULARS, "Interest Amount " +objKoleFieldsExpensesTO.getAcctNum());
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                    txMap.put("INITIATED_BRANCH", _branchCode);
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.SUSPENSE);
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, objKoleFieldsExpensesTO.getIntAmount());
                    transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER")));
                    transferTo.setSingleTransId(generateSingleTransId);
                    transferTo.setLinkBatchId(objKoleFieldsExpensesTO.getKoleFieldExpenseId());
                    transferTo.setGlTransActNum(objKoleFieldsExpensesTO.getAcctNum());
                    transferTo.setScreenName(screenName);
                     transferTo.setInitiatedBranch(_branchCode);
                    transferTo.setNarration(objKoleFieldsExpensesTO.getKoleFieldExpenseId());
                    transList.add(transferTo);
                }               
                //transactionDAO.setInitiatedBranch(_branchCode);
                //transactionDAO.doTransferLocal(transList, _branchCode);
              
                map.put("MODE", map.get("COMMAND"));
                map.put("COMMAND", map.get("MODE"));
                map.put("TxTransferTO", transList);
                map.put(CommonConstants.SELECTED_BRANCH_ID, _branchCode);
                HashMap transMap = transferDAO.execute(map, false);
                if (transMap != null && transMap.containsKey("TRANS_ID") && transMap.get("TRANS_ID") != null) {
                    objKoleFieldsExpensesTO.setBatchId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                }

            }
        }           
        
        TransactionTO objTransactionTO = null;
        if (transTomap != null && transTomap.size() > 0) {
            objTransactionTO = (TransactionTO) transTomap.get("1");
            objTransactionTO.setStatus(CommonConstants.STATUS_CREATED);
            objTransactionTO.setBatchId(objKoleFieldsExpensesTO.getKoleFieldExpenseId());
            objTransactionTO.setBatchDt(currDt);
            objTransactionTO.setTransId(objKoleFieldsExpensesTO.getKoleFieldExpenseId());
            objTransactionTO.setBranchId(_branchCode);
            sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);
         }
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

    private CashTransactionTO loanAuthorizeTimeTransaction(HashMap dataMap) throws Exception {
        ArrayList cashList = new ArrayList();
        String particulars = "";
        CashTransactionTO objCashTO = new CashTransactionTO();
        objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
        objCashTO.setProdType(CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")));
        if(dataMap.get("TRANS_TYPE").equals(CommonConstants.DEBIT)){
            objCashTO.setTransType(CommonConstants.DEBIT);
        } else if(dataMap.get("TRANS_TYPE").equals(CommonConstants.CREDIT)){
            objCashTO.setTransType(CommonConstants.CREDIT);
        }        
        objCashTO.setSingleTransId(generateSingleTransId);
        objCashTO.setParticulars(CommonUtil.convertObjToStr(objKoleFieldsExpensesTO.getAcctNum()));
        objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
        objCashTO.setInpAmount(CommonUtil.convertObjToDouble(dataMap.get("LIMIT")));
        objCashTO.setAmount(CommonUtil.convertObjToDouble(dataMap.get("LIMIT")));
        objCashTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
        objCashTO.setBranchId(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
        objCashTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
        objCashTO.setStatusDt(ServerUtil.getCurrentDate(_branchCode));
        objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
        objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
        objCashTO.setInitChannType(CommonConstants.CASHIER);
        objCashTO.setInstrumentNo2(CommonUtil.convertObjToStr(dataMap.get(TransferTrans.DR_INSTRUMENT_2)));
        objCashTO.setLinkBatchId(objKoleFieldsExpensesTO.getKoleFieldExpenseId());
        objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(objKoleFieldsExpensesTO.getAcctNum()));
        objCashTO.setCommand("INSERT");
        objCashTO.setNarration(objKoleFieldsExpensesTO.getKoleFieldExpenseId());
        objCashTO.setScreenName(CommonUtil.convertObjToStr(dataMap.get("SCREEN_NAME")));
        System.out.println("objCashTO 1st one:" + objCashTO);
        cashList.add(objCashTO);
        return objCashTO;
    }
    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            objKoleFieldsExpensesTO.setKoleFieldExpenseId(getKoleFieldExpenseId());
            insertTransactionDetails(map);
            objKoleFieldsExpensesTO.setBranchId(_branchCode);
            objKoleFieldsExpensesTO.setCreatedDt(currDt);
            objKoleFieldsExpensesTO.setStatusDt(currDt);
            objKoleFieldsExpensesTO.setStatusBy(CommonUtil.convertObjToStr(map.get("USER")));
            objKoleFieldsExpensesTO.setCreatedBy(CommonUtil.convertObjToStr(map.get("USER")));
            objKoleFieldsExpensesTO.setStatus("CREATED");
            objKoleFieldsExpensesTO.setTransDt(currDt);
            if(objKoleFieldsExpensesTO.getTransType().equals("CREDIT")){
                objKoleFieldsExpensesTO.setTotalAmount(objKoleFieldsExpensesTO.getTransAmount());           
            }else{
                objKoleFieldsExpensesTO.setTotalAmount(objKoleFieldsExpensesTO.getTransAmount() * -1);
            }
             sqlMap.executeUpdate("insertSuspenseTransDetailsTO", objKoleFieldsExpensesTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            //System.out.println("%%%%%Amount in daooo" + ObjDirectorBoardTO.getSittingFeeAmount());
            sqlMap.executeUpdate("updateDirectorBoardTO", objKoleFieldsExpensesTO);
            //  logTO.setData(ObjDirectorBoardTO.toString());
            //  logTO.setPrimaryKey(ObjDirectorBoardTO.getKeyData());
            //  logTO.setStatus(ObjDirectorBoardTO.getStatus());
            //  logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();
            String Statusby = objKoleFieldsExpensesTO.getStatusBy();
            objKoleFieldsExpensesTO.setStatusDt(currDt);
            sqlMap.executeUpdate("deleteDirectorBoardTO", objKoleFieldsExpensesTO);
            // logTO.setData(ObjDirectorBoardTO.toString());
            ///  logTO.setPrimaryKey(ObjDirectorBoardTO.getKeyData());
            //  logTO.setStatus(ObjDirectorBoardTO.getStatus());
            //  logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }

    /*
     * public static void main(String str[]) { try { UserDAO dao = new
     * UserDAO(); HashMap map=new HashMap();
     * map.put(CommonConstants.MAP_NAME,"getSelectTerminalMasterTO");
     * map.put(CommonConstants.MAP_WHERE,"T0001042"); map=dao.executeQuery(map);
     * } catch (Exception ex) { ex.printStackTrace(); } }
     */
    public HashMap execute(HashMap map) throws Exception {
        System.out.println("@@@@@@@ExecuteMap in KoleFieldExpensesDAO" + map);
        HashMap returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        logDAO = new LogDAO();
        logTO = new LogTO();
        objKoleFieldsExpensesTO = (KoleFieldsExpensesTO) map.get("KoleFieldExpensesTO");
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
            returnMap.put("KOLE_FIELD_EXPENSE_ID", objKoleFieldsExpensesTO.getKoleFieldExpenseId());
            returnMap.put("SINGLE_TRANS_ID", generateSingleTransId);
            returnMap.put("TRANS_TYPE", objKoleFieldsExpensesTO.getTransType());
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        }
        if(map.containsKey(CommonConstants.AUTHORIZEMAP)){
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            System.out.println("authMap --- :" + authMap);
            ArrayList alist = (ArrayList) authMap.get("AUTHORIZEDATA");
            System.out.println("alist-------- " + alist);
            String auth_status = CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS));
            System.out.println("auth_status########h- :" + auth_status);
            HashMap authMappp = (HashMap) alist.get(0);
            String linkBatchId = CommonUtil.convertObjToStr(authMappp.get("ACCT_NUM"));
            String koleFieldExpenseId = CommonUtil.convertObjToStr(authMappp.get("KOLE_FIELD_EXPENSE_ID"));
            authMappp.put(CommonConstants.BRANCH_ID, _branchCode);
            authMappp.put(CommonConstants.USER_ID, map.get("USER"));
            authMappp.put("INITIATED_BRANCH", _branchCode);
            authMappp.put("LINK_BATCH_ID", linkBatchId);
            authMappp.put("TODAY_DT", currDt);
            HashMap cashAuthMap = new HashMap();
            cashAuthMap.put(CommonConstants.BRANCH_ID, _branchCode);
            cashAuthMap.put(CommonConstants.USER_ID,map.get("USER"));
            TransactionDAO transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            transactionDAO.authorizeCashAndTransfer(koleFieldExpenseId, auth_status, cashAuthMap);
            authMappp.put("TRN_DT",currDt.clone());
            authMappp.put("KOLE_FIELD_EXPENSE_ID",koleFieldExpenseId);    
            sqlMap.executeUpdate("authorizeKoleFieldExpenses", authMappp);           
            if(auth_status.equals("AUTHORIZED") && authMappp.containsKey("INT_CALC_DT_UPDATE") && authMappp.get("INT_CALC_DT_UPDATE") != null){
                 authMappp.put("INT_CALC_UPTO_DT",currDt.clone());
                 sqlMap.executeUpdate("updateSuspenceIntCalcUpToDt", authMappp);
            }
        }

        map = null;
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
        return getData(obj);
    }
    //  public HashMap execute(HashMap obj) throws Exception {
    //   }
    //   public HashMap executeQuery(HashMap obj) throws Exception {
    //  }
//    public HashMap execute(HashMap obj) throws Exception {
    // }
    //   public HashMap executeQuery(HashMap obj) throws Exception {
    //  }
//    private void destroyObjects() {
//        ObjDirectorBoardTO = null;
//        logTO = null;
//        logDAO = null;
//    }
//    
//    private void authorize(HashMap map) throws Exception {
//        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
//        HashMap AuthMap= new HashMap();
//        AuthMap= (HashMap) selectedList.get(0);
//        System.out.println("@@@@@@@AuthMap"+AuthMap);
//        try {
//            sqlMap.startTransaction();
//            logTO.setData(map.toString());
//            sqlMap.executeUpdate("authorizeEmpTransfer", AuthMap);
//            if(AuthMap.get("STATUS").equals("AUTHORIZED")){
//              sqlMap.executeUpdate("updatePresentBranch", AuthMap);  
//            }
//            sqlMap.commitTransaction();
//        } catch (Exception e) {
//            sqlMap.rollbackTransaction();
//            e.printStackTrace();
//            throw new TransRollbackException(e);
//        }
//    }
}
