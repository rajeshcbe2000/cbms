/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TransferDAO.java
 *
 * Created on June 18, 2003, 4:14 PM
 */
package com.see.truetransact.serverside.transaction.transfer;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.clientexception.ClientParseException;

// For the Business Rules...
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;   //For Processing Charge
import com.see.truetransact.transferobject.termloan.loanrebate.LoanRebateTO;

import com.see.truetransact.serverside.transaction.common.Transaction;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.exceptionconstants.transaction.TransactionConstants;
import com.see.truetransact.transferobject.transaction.reconciliation.ReconciliationTO;
//interestcalculation TL AD
import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;
import com.see.truetransact.transferobject.termloan.TermLoanPenalWaiveOffTO;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.common.sms.SmsConfigDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.viewall.SelectAllDAO;
import com.see.truetransact.serverside.deposit.lien.DepositLienDAO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.transferobject.deposit.lien.DepositLienTO;
import com.see.truetransact.transferobject.termloan.chargesTo.TermLoanChargesTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import org.apache.log4j.Logger;
import com.see.truetransact.transferobject.transaction.dailyDepositTrans.DailyDepositTransTO;
import java.util.Map;

// for sending SMS alerts 
import com.see.truetransact.transferobject.sms.SMSParameterTO;
import com.see.truetransact.transferobject.common.mobile.SMSSubscriptionTO;
import com.see.truetransact.transferobject.product.loan.LoanProductAccountParamTO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
/**
 * This is used for Transfer Data Access.
 *
 * @author Balachandar
 *
 * @modified Pinky
 */
public class TransferDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private final Logger log = Logger.getLogger(TransferDAO.class);
    private Transaction transModuleBased;
    private String loanParticulars = "";
    private String batchId = "";
    private String singleTransID = "";
    private String generateSingleID = null;
    private ArrayList toList;
    private double drAmt = 0;
    private double crAmt = 0;
    private boolean isException = false;
    private HashMap insertMap = new HashMap();
    private boolean forProcCharge = false;
    private boolean forLoanDebitInt = false;
    private HashMap allTermLoanAmt = null;
    private String debitLoanType = null;
    private ArrayList dailyDepList;
    private Date properFormatDate;
    private DepositLienDAO depositLienDAO = new DepositLienDAO();
    private DepositLienTO depositLienTO = new DepositLienTO();
    private double flexiAmount = 0.0;
    private HashMap flexiDeletionMap = new HashMap();
    private boolean depositClosingFlag = false;
    private boolean valueDateFlag = false;
    private HashMap valueDateMap = null;
    private int transferTOID = 0;
    ReconciliationTO reconciliationTO = new ReconciliationTO();
    private String act_closing_min_bal_check = null;
    private final String FLEXI_LIEN_CREATION = "FLEXI_LIEN_CREATION";
    private final String ACCOUNTNO = "ACCOUNTNO";
    private final String DEPOSIT_NO = "DEPOSIT_NO";
    private final String COMMAND = "COMMAND";
    private final String SHADOWLIEN = "SHADOWLIEN";
    private final String AUTHORIZE_STATUS = "AUTHORIZE_STATUS";
    private final String ACTION = "ACTION";
    private final String DEPOSIT_ACT_NUM = "DEPOSIT_ACT_NUM";
    private final String BEHAVES_LIKE = "BEHAVES_LIKE";
    private final String NORMAL = "NORMAL";
    private final String TODUTILIZEDCBMORE = "TODUTILIZEDCBMORE";
    private final String LIMIT = "LIMIT";
    private final String CLEAR_BALANCE = "CLEAR_BALANCE";
    private final String TODAY_DT = "TODAY_DT";
    private final String LIEN_AMOUNT = "LIEN_AMOUNT";
    private final String INTEREST = "INTEREST";
    private final String PENAL_INT = "PENAL_INT";
    private final String DAILYDEPOSITTRANSTO = "DAILYDEPOSITTRANSTO";
    private final String REMARKS = "REMARKS";
    private final String AS_CUSTOMER_COMES = "AS_CUSTOMER_COMES";
    private final String LIENNO = "LIENNO";
    private final String LIENAMOUNT = "LIENAMOUNT";
    private final String NORMALDEBIT = "NORMALDEBIT";
    private final String LIEN_AC_NO = "LIEN_AC_NO";
    private final String LIEN_NO = "LIEN_NO";
    private final String PRESENT_AMOUNT = "PRESENT_AMOUNT";
    private final String PRESENT_BATCH_ID = "PRESENT_BATCH_ID";
    private final String PRESENT_TRANS_ID = "PRESENT_TRANS_ID";
    private final String LIST_OF_REDUCED = "LIST_OF_REDUCED";
    private final String DEP_INTEREST_AMT = "DEP_INTEREST_AMT";
    private final String PROD_ID = "PROD_ID";
    private final String DEBIT_LOAN_TYPE = "DEBIT_LOAN_TYPE";
    private final String BATCH_ID = "BATCH_ID";
    private final String DEPOSIT_PENAL_AMT = "DEPOSIT_PENAL_AMT";
    private final String ALL_AMOUNT = "ALL_AMOUNT";
    private final String LINK_BATCH_ID = "LINK_BATCH_ID";
    private final String PROCCHARGEHASH = "PROCCHARGEHASH";
    private Map corpLoanMap = null; // For Corporate Loan purpose added by Rajesh
    private String user = "";
    HashMap otherChargesMap;
    private String shift = "";
    private String shifttime = "";
    private Map cache;                  //used to hold references to Resources for re-use
    private Date currDate = null;
    private boolean fromTransferModule = false;
    private String fromTransferModuleProdTypeDebit = "";
    private String fromTransferModuleProdTypeCredit = "";
    private String cash_transid="";
    private TransactionDAO transactionDAO = null;
    private Date futureSILastIntCalcDt = null; // Added by nithya on 28-08-2017
    
    public TransferDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        //  currDate= ServerUtil.getCurrentDate(_branchCode);
    }

    private void updateData(HashMap map, TxTransferTO txTransferTO) {
        ArrayList list = (ArrayList) map.get("ORG_RESP_DETAILS");
        if (list != null && list.size() > 0) {
            try {
                map = (HashMap) list.get(0);
                map.put("STATUS", "MODIFIED");
                map.put("STATUS_DT", properFormatDate);
                map.put("STATUS_BY", user);
                map.put("BRANCH_ID", _branchCode);
                map.put("TRANS_ID", txTransferTO.getBatchId());
                sqlMap.executeUpdate("updateOrgRespDetails", map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /*
     * method for EDIT operation
     */

    private void updateData(TxTransferTO txTransferTO, double prevAmount) throws Exception {
        // ******** important ****** Don't give prevAmount in getRuleMap, the amount should be 0
        HashMap ruleMap = getRuleMap(txTransferTO, 0, true);
        double amt = CommonUtil.convertObjToDouble(ruleMap.get(TransactionDAOConstants.AMT)).doubleValue();
        if (amt != 0) {
            transModuleBased.validateRules(ruleMap, isException);
            System.out.println("Flexi Deposit Updation...");
            // ******** important ****** Don't give 0 in getRuleMap, the amount should be prevAmount
            ruleMap = getRuleMap(txTransferTO, prevAmount, true);
            transModuleBased.performShadowAdd(ruleMap);
        }
        //transModuleBased.performOtherBalanceAdd(getRuleMap(txTransferTO, prevAmount, true));
        txTransferTO.setStatusDt(properFormatDate);
        txTransferTO.setInitiatedBranch(_branchCode);
        txTransferTO.setTransDt(currDate);
        txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
        sqlMap.executeUpdate("updateTxTransferTO", txTransferTO);
        log.info("Update ... objTransferTransactionTO : " + txTransferTO.toString());
        if (valueDateFlag) {
            storeValueDate(txTransferTO);
        }
    }

    private String getAdviceNo() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "ADVICE_NO");
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void insertData(HashMap hmap) {
        ArrayList list = null;
        list = (ArrayList) hmap.get("ORG_RESP_DETAILS");
        if (list != null && list.size() > 0) {
            try {

                hmap = (HashMap) list.get(0);
                if (CommonUtil.convertObjToStr(hmap.get("OrgOrRespTransType")).equals("O")) {
                    hmap.put("ADVICE_NO", getAdviceNo());
                }
                System.out.println("hmap" + hmap);
                hmap.put("TRAN_ID", batchId);
                hmap.put("TRAN_DT", properFormatDate);
                hmap.put("STATUS", "CREATED");
                hmap.put("STATUS_DT", properFormatDate);
                hmap.put("STATUS_BY", user);
                hmap.put("BRANCH", _branchCode);
                System.out.println("hmap");
                sqlMap.executeUpdate("insertOrgOrResp", hmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * method for NEW operation
     */
    private void insertData(TxTransferTO txTransferTO, HashMap transMap) throws Exception {
        HashMap map = new HashMap();
        if (!txTransferTO.getProdType().equals("") && txTransferTO.getProdType().equals(TransactionFactory.OPERATIVE)
                && txTransferTO.getActNum() != null && flexiDeletionMap != null
                && flexiDeletionMap.containsKey(FLEXI_LIEN_CREATION)) {
            HashMap flexiMap = new HashMap();
            depositLienDAO = new DepositLienDAO();
            depositLienTO = new DepositLienTO();
            ArrayList lienTOs = new ArrayList();
            flexiMap.put(TransactionDAOConstants.ACCT_NO, txTransferTO.getActNum());
            List getList = sqlMap.executeQueryForList("getFlexiDetails", flexiMap);
            if (getList != null && getList.size() > 0) {
//                System.out.println("getList : " + getList);
                flexiMap = (HashMap) getList.get(0);
                HashMap eachLienMap = new HashMap();
                double depAmt = 0.0;
                eachLienMap.put("FLEXI_ACT_NUM", txTransferTO.getActNum());
                List lstEachLien = sqlMap.executeQueryForList("getSelectSumOfEachDepAmount", eachLienMap);
                if (lstEachLien != null && lstEachLien.size() > 0) {
                    for (int i = 0; i < lstEachLien.size(); i++) {
                        eachLienMap = (HashMap) lstEachLien.get(i);
                        depAmt = CommonUtil.convertObjToDouble(eachLienMap.get("TOTAL_BALANCE")).doubleValue();
//                        System.out.println("depAmt :" + depAmt + "flexiAmount :" + flexiAmount);
                        if (flexiAmount >= depAmt) {
                            depAmt = depAmt;
//                            System.out.println("if depAmt :" + depAmt + "if flexiAmount :" + flexiAmount);
                        } else {
                            depAmt = flexiAmount;
//                            System.out.println("else depAmt :" + depAmt + "else flexiAmount :" + flexiAmount);
                        }
                        if (depAmt > 0) {
                            depositLienTO.setLienNo("-");
                            depositLienTO.setDepositNo(CommonUtil.convertObjToStr(eachLienMap.get(DEPOSIT_NO)));
                            depositLienTO.setDepositSubNo(CommonUtil.convertObjToInt("1"));
                            depositLienTO.setLienDt(properFormatDate);
                            depositLienTO.setStatus(CommonConstants.STATUS_CREATED);
                            depositLienTO.setLienAcHd(CommonUtil.convertObjToStr(txTransferTO.getAcHdId()));
                            depositLienTO.setLienAcNo(CommonUtil.convertObjToStr(txTransferTO.getActNum()));
                            depositLienTO.setRemarks(batchId);
                            depositLienTO.setStatusBy(CommonUtil.convertObjToStr(txTransferTO.getStatusBy()));
                            depositLienTO.setLienAmount(new Double(depAmt));
                            depositLienTO.setLienProdId(CommonUtil.convertObjToStr(txTransferTO.getProdId()));
                            depositLienTO.setStatusDt(properFormatDate);
                            depositLienTO.setCreditLienAcct("NA");
                            depositLienTO.setUnlienRemarks("FLEXI_DEPOSITS");
                            lienTOs.add(depositLienTO);
//                            System.out.println("lienTOs: " + lienTOs);
                            map.put(CommonConstants.BRANCH_ID, txTransferTO.getBranchId());
                            map.put(COMMAND, CommonConstants.TOSTATUS_INSERT);
                            map.put("lienTOs", lienTOs);
                            map.put(SHADOWLIEN, new Double(depAmt));
                            map.put("LIENAMOUNT", new Double(depAmt));
                            map.put("USER_ID", txTransferTO.getStatusBy());
                            map.put(CommonConstants.BRANCH_ID, _branchCode);
//                            System.out.println("map : " + map);
                            depositLienDAO.setCallFromOtherDAO(true);
                            HashMap lienCreatedMap = depositLienDAO.execute(map);
                            List selected = (ArrayList) lienCreatedMap.get(LIENNO);
//                            System.out.println("selected : " + selected);
                            flexiAmount = flexiAmount - depAmt;
                            if (flexiAmount < 0) {
                                break;
                            }
                        }
                    }
                }
            }
            depositLienDAO = null;
            depositLienTO = null;
            lienTOs = null;
            flexiMap = null;
            flexiAmount = 0.0;
        }
        //Added By Suresh       
        if (transMap.containsKey("BACK_DATED_TRANSACTION")) {//Only For Back Dated Transaction
            txTransferTO.setTransId(getBackDateTransID());
            //txTransferTO.setTransModType(CommonConstants.TX_TRANSFER);  commented by srekrishnan for mantis:0010599
        } else {
//            if(transMap.containsKey("LOGGED_BRANCH_ID") && transMap.containsKey("MDS_BONUS_ENTRY")){
//                if(transMap.containsKey(CommonConstants.BRANCH_ID) && transMap.get(CommonConstants.BRANCH_ID) != null && 
//                transMap.containsKey(CommonConstants.SELECTED_BRANCH_ID) && transMap.get(CommonConstants.SELECTED_BRANCH_ID) != null && 
//                !CommonUtil.convertObjToStr(transMap.get(CommonConstants.BRANCH_ID)).equals(transMap.get(CommonConstants.SELECTED_BRANCH_ID))){
//                    HashMap mdsMap = new HashMap();
//                    mdsMap.put("TRANS_DT", currDate.clone());
//                    mdsMap.put("BRANCH_ID", transMap.get(CommonConstants.SELECTED_BRANCH_ID));
//                    List mdsList = sqlMap.executeQueryForList("getSelectMaxBatchIdDiffBranch", mdsMap);
//                    if (mdsList != null && mdsList.size() > 0) {
//                        mdsMap = (HashMap) mdsList.get(0);
//                        System.out.println("transId : "+CommonUtil.convertObjToLong(mdsMap.get("TRANS_ID"))+" transferTOID : "+transferTOID);
//                        long diffDtTransId = CommonUtil.convertObjToLong(mdsMap.get("TRANS_ID"));
//                        txTransferTO.setTransId(CommonUtil.convertObjToStr("TT0"+diffDtTransId));
//                    }else{
//                        txTransferTO.setTransId(getTransID());
//                    }
//                }else{
//                    txTransferTO.setTransId(getTransID());
//                }
//            }else{
                txTransferTO.setTransId(getTransID());
//            }
        }
        HashMap validateMap = getRuleMap(txTransferTO, 0.0, true);
     //   System.out.println("######isForProcCharge()" + isForProcCharge());
        if (isForProcCharge()) {
            validateMap.put("FORPROCCHARGE", "TRUE");
        }
        if (!isForLoanDebitInt()) {
            transModuleBased.validateRules(validateMap, isException);
        }

        transModuleBased.performShadowAdd(getRuleMap(txTransferTO, 0.0, true));
        //transModuleBased.performOtherBalanceAdd(getRuleMap(txTransferTO, 0.0, true));
        txTransferTO.setStatusDt(properFormatDate);
        //        if (txTransferTO.getAcHdId()==null || txTransferTO.getAcHdId().length()<=0)
        if (txTransferTO.getAcHdId() == null || txTransferTO.getAcHdId().length() <= 0) {
            throw new TTException(txTransferTO.getTransType() + " Account Head not set...\nAmount : "
                    + txTransferTO.getAmount().toString() + " (" + txTransferTO.getParticulars() + ")");
        }
        txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
        if (shift != null && !shift.equals("") && shift.length() > 0 && shift.equals("T")) {
            txTransferTO.setShift(shifttime);
        } else {
            txTransferTO.setShift("");
        }
        txTransferTO.setShift(shifttime);
        //added by sreekrishnan
        if (fromTransferModule) {
            if (!txTransferTO.getTransType().equals("") && txTransferTO.getTransType().equals(CommonConstants.DEBIT)) {
                if (fromTransferModuleProdTypeDebit.equals("OA")) {
                    txTransferTO.setTransModType("OA");
                } else if (fromTransferModuleProdTypeDebit.equals("AB")) {
                    txTransferTO.setTransModType("AB");
                } else if (fromTransferModuleProdTypeDebit.equals("SA")) {

                    txTransferTO.setTransModType("SA");
                } else if (fromTransferModuleProdTypeDebit.equals("TL")) {
                    txTransferTO.setTransModType("TL");
                } else if (fromTransferModuleProdTypeDebit.equals("AD")) {
                    txTransferTO.setTransModType("AD");
                } else if (fromTransferModuleProdTypeDebit.equals("TD")) {
                    txTransferTO.setTransModType("TD");
                } else {
                    txTransferTO.setTransModType("GL");
                }
            }
            if (!txTransferTO.getTransType().equals("") && txTransferTO.getTransType().equals(CommonConstants.CREDIT)) {
                if (fromTransferModuleProdTypeCredit.equals("OA")) {
                    txTransferTO.setTransModType("OA");
                } else if (fromTransferModuleProdTypeCredit.equals("AB")) {
                    txTransferTO.setTransModType("AB");
                } else if (fromTransferModuleProdTypeCredit.equals("SA")) {
                    txTransferTO.setTransModType("SA");
                } else if (fromTransferModuleProdTypeCredit.equals("TL")) {
                    txTransferTO.setTransModType("TL");
                } else if (fromTransferModuleProdTypeCredit.equals("AD")) {
                    txTransferTO.setTransModType("AD");
                } else if (fromTransferModuleProdTypeDebit.equals("TD")) {
                    txTransferTO.setTransModType("TD");
                } else {
                    txTransferTO.setTransModType("GL");
                }
            }
            //txTransferTO.setSingleTransId(generateCashId);


        }
        //Added by Chithra 8-07-14
        if (txTransferTO.getGlTransActNum() == null || txTransferTO.getGlTransActNum().length() <= 0) {
            if (txTransferTO.getLinkBatchId() != null && txTransferTO.getLinkBatchId().length() > 0) {
                txTransferTO.setGlTransActNum(txTransferTO.getLinkBatchId());
            } else if (txTransferTO.getActNum() != null && txTransferTO.getActNum().length() > 0) {
                txTransferTO.setGlTransActNum(txTransferTO.getActNum());
            }
        }//End...... 
        //System.out.println("@#@#@#@#@##@#@#@   txTransferTO.getLinkBatchId() :"+txTransferTO.getLinkBatchId());
       if(txTransferTO.getProdType().equals("GL") && CommonUtil.convertObjToStr(txTransferTO.getLinkBatchId()).equals("")){
           txTransferTO.setLinkBatchId(null);
       }
        sqlMap.executeUpdate("insertTxTransferTO", txTransferTO);
        if (isException) {
            HashMap exceptionmap = new HashMap();
            exceptionmap = exceptionMap(txTransferTO);
            System.out.println("exceptionmap###" + exceptionmap);
            //            exceptionmap.put("")
            exceptionmap.put("BRANCH_ID", _branchCode);
            sqlMap.executeUpdate("insertExceptionTrans", exceptionmap);
            exceptionmap = new HashMap();

        }
        if (valueDateFlag) {
            storeValueDate(txTransferTO);
        }
        log.info("Insert ... objTransferTransactionTO : " + txTransferTO.toString());
    }

    private void storeValueDate(TxTransferTO txTransferTO) throws Exception {
        Date valueDt = null;
        if (valueDateMap.containsKey(String.valueOf(transferTOID))) {
            valueDt = (Date) valueDateMap.get(String.valueOf(transferTOID));
        }
//        if (transferTOID==0)
        txTransferTO.setInitiatedBranch(_branchCode);
        sqlMap.executeUpdate("deleteValueDateTO", txTransferTO);
        if (valueDt != null) {
            if (DateUtil.dateDiff(valueDt, txTransferTO.getTransDt()) != 0) {
                HashMap valueDateStoreMap = new HashMap();
                valueDateStoreMap.put(CommonConstants.TRANS_ID, txTransferTO.getTransId());
                valueDateStoreMap.put(CommonConstants.ACT_NUM, txTransferTO.getActNum());
                valueDateStoreMap.put(BATCH_ID, txTransferTO.getBatchId());
                valueDateStoreMap.put(TransactionDAOConstants.AMT, txTransferTO.getAmount());
                valueDateStoreMap.put("TRANS_DT", txTransferTO.getTransDt());
                valueDateStoreMap.put("TRANS_TYPE", txTransferTO.getTransType());
                valueDateStoreMap.put(PROD_ID, txTransferTO.getProdId());
                valueDateStoreMap.put("STATUS", txTransferTO.getStatus());
                valueDateStoreMap.put("BRANCH_ID", txTransferTO.getBranchId());
                valueDateStoreMap.put("VALUE_DT", valueDt);
                valueDateStoreMap.put("PROD_TYPE", txTransferTO.getProdType());
                valueDateStoreMap.put("TRANS_MODE", txTransferTO.getTransMode());
                valueDateStoreMap.put("INITIATED_BRANCH", txTransferTO.getInitiatedBranch());
                sqlMap.executeUpdate("insertValueDateTO", valueDateStoreMap);
                valueDateStoreMap = null;
            }
        }
    }

    private HashMap exceptionMap(TxTransferTO txTransferTO) {
        HashMap exceptionmap = new HashMap();
        exceptionmap.put(CommonConstants.TRANS_ID, txTransferTO.getTransId());
        exceptionmap.put(CommonConstants.ACT_NUM, txTransferTO.getActNum());
        exceptionmap.put(BATCH_ID, txTransferTO.getBatchId());
        return exceptionmap;

    }

    private void updateProcessingChargesFromOperative(HashMap procFullHash) throws Exception {
        System.out.println("#####collectfromoperative" + procFullHash);
        HashMap procHash = (HashMap) procFullHash.get(PROCCHARGEHASH);
        double procAmt = CommonUtil.convertObjToDouble(procHash.get("PROC_AMT")).doubleValue();
        HashMap txMap = new HashMap();
        if (procAmt > 0) {
            txMap = new HashMap();
            ArrayList transferList = new ArrayList(); // for local transfer
            txMap.put(TransferTrans.DR_PROD_ID, (String) procHash.get("OA_PROD_ID"));
            txMap.put(TransferTrans.DR_ACT_NUM, (String) procHash.get("OA_ACT_NUM"));
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
            List lst = (List) sqlMap.executeQueryForList("getProcChargeAcHd", procHash);
            HashMap acHeads = (HashMap) lst.get(0);
            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("PROC_CHRG"));
            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
            txMap.put(TransferTrans.CURRENCY, "INR");
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            System.out.println("####### insertAccHead txMap 22==" + txMap);
            TransferTrans trans = new TransferTrans();
            trans.setTransMode(CommonConstants.TX_TRANSFER);
            trans.setInitiatedBranch(_branchCode);
            trans.setForProcCharge(true);
            trans.setLinkBatchId(CommonUtil.convertObjToStr(procHash.get(LINK_BATCH_ID)));
            transferList.add(trans.getDebitTransferTO(txMap, procAmt));
            transferList.add(trans.getCreditTransferTO(txMap, procAmt));
            String cmd = CommonUtil.convertObjToStr(procFullHash.get(COMMAND));
            trans.doDebitCredit(transferList, _branchCode, false, cmd);
        }
    }

    /**
     * ***************babu added for daily loan balance****************
     */
    public HashMap interestCalculationTLAD1(Object accountNo, Object prod_id, String prodType) throws Exception {

        HashMap dataMap = new HashMap();
        HashMap hash = new HashMap();
        Date date = (Date) currDate.clone();
        try {
            hash.put("ACT_NUM", accountNo);
            hash.put("PRODUCT_TYPE", prodType);
            hash.put("PROD_ID", prod_id);
            //            hash.put("TRANS_DT", interestUptoDt);
            hash.put("TRANS_DT", date);
            hash.put("INITIATED_BRANCH", _branchCode);
            String mapNameForCalcInt = "IntCalculationDetail";
            if (prodType.equals("AD")) {
                mapNameForCalcInt = "IntCalculationDetailAD";
            }
            List lst = ServerUtil.executeQuery(mapNameForCalcInt, hash);
            System.out.println(accountNo + "," + prod_id + "," + "LIST   1>>>>>>" + lst + " prodType==" + prodType);
            if (lst != null && lst.size() > 0) {
                hash = (HashMap) lst.get(0);
                java.util.Iterator iterator = hash.keySet().iterator();
                while (iterator.hasNext()) {
                    String value = "";
                    String key = iterator.next().toString();
                    if (hash.get(key) != null) {
                        value = hash.get(key).toString();
                    }
                    System.out.println(key + " " + value);
                }
                if (hash.get("AS_CUSTOMER_COMES") != null && hash.get("AS_CUSTOMER_COMES").equals("N")) {
                    hash = new HashMap();
                    return hash;
                }
                hash.put("ACT_NUM", accountNo);
                hash.put("PRODUCT_TYPE", prodType);
                hash.put("PROD_ID", prod_id);
                //                hash.put("TRANS_DT", intUptoDt);
                hash.put("TRANS_DT", date);
                hash.put("INITIATED_BRANCH", _branchCode);
                hash.put("ACT_NUM", accountNo);
                hash.put("BRANCH_ID", _branchCode);
                hash.put("BRANCH_CODE", _branchCode);
                hash.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
                //                hash.put("CURR_DATE", interestUptoDt);
                hash.put("CURR_DATE", date);
                dataMap.put(CommonConstants.MAP_WHERE, hash);
                System.out.println("map before actBranch###" + _branchCode);
                System.out.println("map before intereest###" + dataMap);
                hash = new SelectAllDAO().executeQuery(dataMap);
                if (hash == null) {
                    hash = new HashMap();
                }
                if (hash.containsKey("DATA") && hash.get("DATA") != null) {
                    hash.putAll((HashMap) ((List) hash.get("DATA")).get(0));
                }
                hash.putAll((HashMap) dataMap.get(CommonConstants.MAP_WHERE));
                System.out.println("hashinterestoutput###" + hash);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }

    public double calcLoanPayments(String actNum, String prodId, String prodType) throws Exception {
        double totalDemand = 0.0;
        prodId = actNum.substring(4, 7);
        System.out.println("prodId  ======== " + prodId);
        HashMap dataMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put("ACT_NUM", actNum);
        int firstDay = 0;
        Date inst_dt = null;
        String clear_balance = "";
        HashMap asAndWhenMap = interestCalculationTLAD1(actNum, prodId, prodType);
        System.out.println("@#@ asAndWhenMap is >>>>" + asAndWhenMap);
        // System.out.println("transDetail"+actNum+_branchCode);
        HashMap insertPenal = new HashMap();
        List chargeList = null;
        HashMap loanInstall = new HashMap();
        loanInstall.put("ACT_NUM", actNum);
        loanInstall.put("BRANCH_CODE", _branchCode);
        if (prodType != null && prodType.equals("TL")) {      //Only TL
            HashMap allInstallmentMap = null;
            List paidAmt = ServerUtil.executeQuery("getPaidPrinciple", loanInstall);
            allInstallmentMap = (HashMap) paidAmt.get(0);
            System.out.println("!!!!asAndWhenMap:" + asAndWhenMap);
            double totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
            if (asAndWhenMap == null || (asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && (asAndWhenMap.get("AS_CUSTOMER_COMES") != null) && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("N"))) {
                paidAmt = ServerUtil.executeQuery("getIntDetails", loanInstall);
                if (paidAmt != null && paidAmt.size() > 0) {
                    allInstallmentMap = (HashMap) paidAmt.get(0);
                }
                double totExcessAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
                totPrinciple += totExcessAmt;
            }
            List lst = ServerUtil.executeQuery("getAllLoanInstallment", loanInstall);
            //                    Date inst_dt=null;
            System.out.println("totPrinciple ----- " + totPrinciple);

            for (int i = 0; i < lst.size(); i++) {
                allInstallmentMap = (HashMap) lst.get(i);
                double instalAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                System.out.println("instalAmt 33 ----- " + instalAmt);
                if (instalAmt <= totPrinciple) {
                    totPrinciple -= instalAmt;
                    inst_dt = new Date();
                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                    System.out.println("inst_dt 33 ----- " + inst_dt);
                } else {
                    inst_dt = new Date();
                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                    totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue() - totPrinciple;
                    System.out.println("inst_dt 2222 ----- " + inst_dt);
                    break;
                }
            }
            Date addDt = (Date) currDate.clone();
            Date instDt = DateUtil.addDays(inst_dt, 1);
            addDt.setDate(instDt.getDate());
            addDt.setMonth(instDt.getMonth());
            addDt.setYear(instDt.getYear());
            loanInstall.put("FROM_DATE", addDt);//DateUtil.addDays(inst_dt,1));
            //                    loanInstall.put("TO_DATE",interestUptoDt);
            loanInstall.put("TO_DATE", currDate.clone());
            System.out.println("!! getTotalamount#####" + loanInstall);
            List lst1 = null;
            if (inst_dt != null && (totPrinciple > 0)) {
                lst1 = ServerUtil.executeQuery("getTotalAmountOverDue", loanInstall);
                System.out.println("listsize####" + lst1);
            }
            double principle = 0;
            if (lst1 != null && lst1.size() > 0) {
                HashMap map = (HashMap) lst1.get(0);
                principle = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue();
            }
            totPrinciple += principle;
            insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(totPrinciple));
            insertPenal.put("INSTALL_DT", inst_dt);
            if (asAndWhenMap.containsKey("MORATORIUM_INT_FOR_EMI")) {
                insertPenal.put("MORATORIUM_INT_FOR_EMI", asAndWhenMap.get("MORATORIUM_INT_FOR_EMI"));
            }
            if (asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && (asAndWhenMap.get("AS_CUSTOMER_COMES") != null) && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")) {
                double interest = CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
                double penal = CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                List facilitylst = ServerUtil.executeQuery("LoneFacilityDetailAD", loanInstall);
                if (facilitylst != null && facilitylst.size() > 0) {
                    HashMap hash = (HashMap) facilitylst.get(0);

                    if (hash.get("CLEAR_BALANCE") != null) {
                        clear_balance = hash.get("CLEAR_BALANCE").toString();
                    }
                    hash.put("ACT_NUM", loanInstall.get("ACT_NUM"));
                    if (asAndWhenMap.containsKey("PREMATURE")) {
                        insertPenal.put("PREMATURE", asAndWhenMap.get("PREMATURE"));
                    }
                    if (asAndWhenMap.containsKey("PREMATURE") && asAndWhenMap.containsKey("PREMATURE_INT_CALC_AMT")
                            && CommonUtil.convertObjToStr(asAndWhenMap.get("PREMATURE_INT_CALC_AMT")).equals("LOANSANCTIONAMT")) {
                        hash.put("FROM_DT", hash.get("ACCT_OPEN_DT"));
                    } else {
                        hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                        hash.put("FROM_DT", DateUtil.addDays(((Date) hash.get("FROM_DT")), 2));
                    }
                    //                            hash.put("TO_DATE", interestUptoDt.clone());
                    hash.put("TO_DATE", currDate.clone());
                    if (!(asAndWhenMap != null && asAndWhenMap.containsKey("INSTALL_TYPE") && asAndWhenMap.get("INSTALL_TYPE") != null && asAndWhenMap.get("INSTALL_TYPE").equals("EMI"))) {
                        facilitylst = ServerUtil.executeQuery("getPaidPrinciple", hash);
                    } else {
                        facilitylst = null;
                        if (asAndWhenMap.containsKey("PRINCIPAL_DUE") && asAndWhenMap.get("PRINCIPAL_DUE") != null) {
                            insertPenal.put("CURR_MONTH_PRINCEPLE", asAndWhenMap.get("PRINCIPAL_DUE"));
                        }
                    }
                    if (facilitylst != null && facilitylst.size() > 0) {
                        hash = (HashMap) facilitylst.get(0);
                        interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                        penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();

                        insertPenal.put("PAID_INTEREST", hash.get("INTEREST"));
                    }
                }
                System.out.println("####interest:" + interest);
                if (interest > 0) {
                    insertPenal.put("CURR_MONTH_INT", new Double(interest));
                } else {
                    insertPenal.put("CURR_MONTH_INT", new Double(0));
                }
                if (penal > 0) {
                    insertPenal.put("PENAL_INT", new Double(penal));
                } else {
                    insertPenal.put("PENAL_INT", new Double(0));
                }
                insertPenal.put("INTEREST", asAndWhenMap.get("INTEREST"));
                insertPenal.put("LOAN_CLOSING_PENAL_INT", asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));

                insertPenal.put("LAST_INT_CALC_DT", asAndWhenMap.get("LAST_INT_CALC_DT"));
                insertPenal.put("ROI", asAndWhenMap.get("ROI"));
                chargeList = ServerUtil.executeQuery("getChargeDetails", loanInstall);
            } else {
                List getIntDetails = ServerUtil.executeQuery("getIntDetails", loanInstall);
                HashMap hash = null;
                if (getIntDetails != null) {
                    for (int i = 0; i < getIntDetails.size(); i++) {
                        hash = (HashMap) getIntDetails.get(i);
                        String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
                        double pBal = CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue();
                        double iBal = CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
                        double pibal = CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
                        double excess = CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT")).doubleValue();
                        pBal -= excess;
                        if (pBal < totPrinciple) {
                            insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(pBal));
                        }
                        if (trn_mode.equals("C*")) {
                            insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                            insertPenal.put("PRINCIPLE_BAL", new Double(pBal));
                            insertPenal.put("EBAL", hash.get("EBAL"));
                            break;
                        } else {
                            if (!trn_mode.equals("DP")) {
                                insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                            }
                            insertPenal.put("EBAL", hash.get("EBAL"));
                            insertPenal.put("PRINCIPLE_BAL", new Double(pBal));
                        }
                        System.out.println("int principel detailsINSIDE LOAN##" + insertPenal);
                    }
                }
                getIntDetails = ServerUtil.executeQuery("getPenalIntDetails", loanInstall);
                hash = (HashMap) getIntDetails.get(0);
                insertPenal.put("PENAL_INT", hash.get("PIBAL"));
            }
        }

        if (prodType != null && prodType.equals("AD")) // Only  AD
        {
                //if condition modified by chithra for mantis 10401: in advances acct charges is not shown in the cash screen
            if (prodType.equals("AD") ||(asAndWhenMap != null && asAndWhenMap.size() > 0)) {
                if (prodType.equals("AD") || asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")) {
                    List facilitylst = ServerUtil.executeQuery("LoneFacilityDetailAD", loanInstall);
                    double interest = CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
                    double penal = CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                    if (facilitylst != null && facilitylst.size() > 0) {
                        HashMap hash = (HashMap) facilitylst.get(0);
                        if (hash.get("CLEAR_BALANCE") != null) {
                            clear_balance = hash.get("CLEAR_BALANCE").toString();
                        }
                        hash.put("ACT_NUM", loanInstall.get("ACT_NUM"));
                        hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                        hash.put("FROM_DT", DateUtil.addDays(((Date) hash.get("FROM_DT")), 2));
                        //                                hash.put("TO_DATE",DateUtil.addDaysProperFormat(interestUptoDt,-1));
                        hash.put("TO_DATE", DateUtil.addDaysProperFormat(currDate, -1));
                        facilitylst = ServerUtil.executeQuery("getPaidPrincipleAD", hash);
                        if (facilitylst != null && facilitylst.size() > 0) {
                            hash = (HashMap) facilitylst.get(0);
                            interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                            penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                        }
                    }
                    if (interest > 0) {
                        insertPenal.put("CURR_MONTH_INT", new Double(interest));
                    } else {
                        insertPenal.put("CURR_MONTH_INT", new Double(0));
                    }
                    if (penal > 0) {
                        insertPenal.put("PENAL_INT", new Double(penal));
                    } else {
                        insertPenal.put("PENAL_INT", new Double(0));
                    }
                    insertPenal.put("INTEREST", asAndWhenMap.get("INTEREST"));
                    insertPenal.put("LOAN_CLOSING_PENAL_INT", asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
                    chargeList = ServerUtil.executeQuery("getChargeDetails", loanInstall);
                } else {
                    if (prodType != null && prodType.equals("AD")) {
                        List getIntDetails = ServerUtil.executeQuery("getIntDetailsAD", loanInstall);
                        HashMap hash = null;

                        for (int i = 0; i < getIntDetails.size(); i++) {
                            hash = (HashMap) getIntDetails.get(i);
                            String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
                            double iBal = CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
                            double pibal = CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
                            double excess = CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT")).doubleValue();
                            if (trn_mode.equals("C*")) {
                                insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                                insertPenal.put("PRINCIPLE_BAL", new Double(CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue() - excess));
                                insertPenal.put("EBAL", hash.get("EBAL"));
                                break;
                            } else {
                                insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                                insertPenal.put("PRINCIPLE_BAL", new Double(CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue() - excess));
                                insertPenal.put("EBAL", hash.get("EBAL"));
                            }
                            System.out.println("int principel detailsINSIDE OD" + insertPenal);
                        }
                        getIntDetails = ServerUtil.executeQuery("getPenalIntDetailsAD", loanInstall);
                        if (getIntDetails.size() > 0) {
                            hash = (HashMap) getIntDetails.get(0);
                            insertPenal.put("PENAL_INT", hash.get("PIBAL"));
                        }
                        insertPenal.remove("PRINCIPLE_BAL");

                    }
                }
            }
        }
        //Added By Suresh  (Current Dt > To Date AND PBAL >0 in ADV_TRANS_DETAILS, Add Principle_Balance)
        if (prodType != null && prodType.equals("AD")) {
            double pBalance = 0.0;
            Date expDt = null;
            List expDtList = ServerUtil.executeQuery("getLoanExpDate", loanInstall);
            if (expDtList != null && expDtList.size() > 0) {
                whereMap = new HashMap();
                whereMap = (HashMap) expDtList.get(0);
                pBalance = CommonUtil.convertObjToDouble(whereMap.get("PBAL")).doubleValue();
                expDt = (Date) whereMap.get("TO_DT");
                //                        long diffDayPending = DateUtil.dateDiff(expDt,intUptoDt);
                long diffDayPending = DateUtil.dateDiff(expDt, currDate);
                System.out.println("############# Insert PBalance" + pBalance + "######diffDayPending :" + diffDayPending);
                if (diffDayPending > 0 && pBalance > 0) {
                    insertPenal.put("PRINCIPLE_BAL", new Double(pBalance));
                }
            }
        }
        System.out.println("####### insertPenal : " + insertPenal);
        //Charges
        double chargeAmt = 0.0;
        whereMap = new HashMap();
        whereMap.put("ACT_NUM", actNum);
        chargeAmt = getChargeAmount(whereMap, prodType);
        /*
         * if(chargeAmt>0){ dataMap.put("CHARGES", String.valueOf(chargeAmt));
         * }else{ dataMap.put("CHARGES", "0"); }
         */
        System.out.println("####### Single Row insertPenal : " + insertPenal);

        double principalAmount = 0.0;
        System.out.println("###insertPenal : " + insertPenal);
        if (insertPenal.containsKey("CURR_MONTH_PRINCEPLE")) {
            principalAmount = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_PRINCEPLE")).doubleValue();     // Principal Amount
            totalDemand = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue()
                    + CommonUtil.convertObjToDouble(insertPenal.get("PENAL_INT")).doubleValue() + chargeAmt;
            System.out.println("###totalDemand 1111 : " + totalDemand);
        } else {
            principalAmount = CommonUtil.convertObjToDouble(insertPenal.get("PRINCIPLE_BAL")).doubleValue();     // Principal Amount
            totalDemand = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue()
                    + CommonUtil.convertObjToDouble(insertPenal.get("PENAL_INT")).doubleValue() + chargeAmt;
            System.out.println("###totalDemand 2222: " + totalDemand);
        }
        System.out.println("###totalDemand : " + totalDemand);

        if (inst_dt != null && prodType.equals("TL")) {
            //                    if(DateUtil.dateDiff(intUptoDt, inst_dt)<=0 ){
            if (DateUtil.dateDiff(currDate, inst_dt) <= 0) {
                // dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
            } else {
                // dataMap.put("PRINCIPAL", "0");
                // principalAmount =0.0;
            }
        }
        /*
         * if (prodType.equals("AD")){ if(principalAmount>0){
         * dataMap.put("PRINCIPAL", String.valueOf(principalAmount)); }else{
         * dataMap.put("PRINCIPAL", "0"); } }
         */
        System.out.println("###totalDemand: " + totalDemand);
        // totalDemand+=principalAmount;babu comm
        totalDemand = (CommonUtil.convertObjToDouble(clear_balance) * -1) + totalDemand;
        System.out.println("###totalDemand 999 : " + totalDemand);
        //  dataMap.put("INTEREST", insertPenal.get("CURR_MONTH_INT"));
        //  dataMap.put("PENAL", insertPenal.get("PENAL_INT"));
        // dataMap.put("TOTAL_DEMAND", new Double(totalDemand));
        // if(totalDemand<=0){
        //    dataMap = null;
        // }
        //            }else{
        //                System.out.println("### Not Allow : "+checkDate);
        //                dataMap = null;
        //            }
        //        }


        System.out.println("####### Single Row DataMap : " + dataMap);
        return totalDemand;
    }

    private double getChargeAmount(HashMap whereMap, String prodType) {   //Charges
        double chargeAmount = 0.0;
        try {
            List chargeList = null;
            String actNo = "";
            HashMap recoverChrgMap = new HashMap();
            actNo = CommonUtil.convertObjToStr(whereMap.get("ACT_NUM"));
            chargeList = ServerUtil.executeQuery("getChargeDetails", whereMap);
            if (chargeList != null && chargeList.size() > 0) {
                for (int i = 0; i < chargeList.size(); i++) {
                    whereMap = (HashMap) chargeList.get(i);
                    chargeAmount += CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();


                }
            }
            chargeList = null;
            recoverChrgMap = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chargeAmount;
    }

    /**
     * ***********************End******************************
     */
    /*
     * method for getting the desired data from the database against some query
     */
    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDate = ServerUtil.getCurrentDate(_branchCode);
        HashMap returnMap = new HashMap();
        // System.out.println("obj innnnnnn :"+obj);
        if (obj.containsKey("FROM_DAILY_LOAN_BALANCE")) {
            double val = calcLoanPayments(obj.get("ACT_NUM").toString(), obj.get("PROD_ID").toString(),
                    obj.get("PROD_TYPE").toString());
            System.out.println("val  nnnnn :" + val);
            returnMap.put("BALANCE_LOAN", val);
            return returnMap;
        }
        // get the map name and where condition
        String map = (String) obj.get(CommonConstants.MAP_NAME);
        System.out.println("ExecuteQuery :" + map);
        HashMap whereMap = null;
        String where = null;
        //Modified on 7 June 2005 by Sunil
        //Used to retrieve data based on Hierarchy Id.
        //Map is populated in ActionPopupOB. Map Name is getAuthorizeMasterTransferTO
        List list = null;
        if (obj.get(CommonConstants.MAP_WHERE) instanceof HashMap) {
            whereMap = (HashMap) obj.get(CommonConstants.MAP_WHERE);
            whereMap.put("TRANS_DT", currDate.clone());//Added By Kannan AR            
            list = (List) sqlMap.executeQueryForList(map, whereMap);
        } else {
            where = (String) obj.get(CommonConstants.MAP_WHERE);
            list = (List) sqlMap.executeQueryForList(map, where);
        }

        returnMap.put(CommonConstants.DATA, (ArrayList) list);
        //        if (map.equals("getMasterTransferTO")) {
        //            List list = (List) sqlMap.executeQueryForList(map, where);
        //            returnMap.put(CommonConstants.DATA,(ArrayList)list);
        //        }else if(map.equals("getDetailTransferTO")) {
        //            List list = (List) sqlMap.executeQueryForList(map, where);
        //            returnMap.put(CommonConstants.DATA,(ArrayList)list);
        //        }else if (map.equals("getAuthorizeMasterTransferTO")) {
        //            List list = (List) sqlMap.executeQueryForList(map, whereMap);
        //            returnMap.put(CommonConstants.DATA,(ArrayList)list);
        //        }else if(map.equals("getAuthorizeDetailTransferTO")) {
        //            List list = (List) sqlMap.executeQueryForList(map, where);
        //            returnMap.put(CommonConstants.DATA,(ArrayList)list);
        //        }
        //        else if(map.equals("getSelectTermLoanDocumentTO")){
        //            List list=(List)sqlMap.executeQueryForList(map,where);
        //            returnMap.put(CommonConstants.DATA,list);
        //        }
        obj = null;
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;
    }
    /*
     * method which will do INSERT/ UPDATE operation based on the command passed
     */

    public HashMap execute(HashMap map) throws Exception {
        return execute(map, true);
    }

    /*
     * method which will do INSERT/ UPDATE operation based on the command passed
     */
    public HashMap execute(HashMap map, boolean isTransaction) throws Exception {
        HashMap execReturnMap = new HashMap();
        System.out.println("############# transferDAO execute() : " + map);

        try {
       //     System.out.println("IN SERRRRR ===" + map.get(""));
            //Commented
            //if ((map.containsKey("DEBIT_TRANSFER_TRANSACTION"))) {
            //fromTransferModule = true;
            fromTransferModuleProdTypeDebit = CommonUtil.convertObjToStr(map.get("DEBIT_TRANSFER_TRANSACTION"));
            //  System.out.println("TRANSFER_TRANSACTION=====transfer==Debit" + fromTransferModule);
            //  System.out.println("TRANSFER_TRANSACTION=====map==Debit" + fromTransferModuleProdTypeDebit);
            // }
            //if ((map.containsKey("CREDIT_TRANSFER_TRANSACTION"))) {
            //fromTransferModule = true;
            //   fromTransferModuleProdTypeCredit=CommonUtil.convertObjToStr(map.get("CREDIT_TRANSFER_TRANSACTION"));
            //  System.out.println("TRANSFER_TRANSACTION=====transfer==Credit" + fromTransferModule);
            //  System.out.println("TRANSFER_TRANSACTION=====map==Credit" + fromTransferModuleProdTypeCredit);
            //}
            _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
            currDate = ServerUtil.getCurrentDate(_branchCode);
            properFormatDate = (Date) currDate.clone();
            HashMap oldAmountMap = (HashMap) map.get(TransactionDAOConstants.OLDAMT);
            Double oldAmount;
            String linkBatchId = null;
            depositClosingFlag = false;
         //   System.out.println("DAO  MAP   :" + map);

            //Added By Suresh R         03-Mar-2015
            if (map.containsKey("TxTransferTO")) {
                TxTransferTO transferTO;
//                HashMap prodHeadMap = new HashMap();
                ArrayList transferTOs = (ArrayList) map.get("TxTransferTO");
                
                //To Check Product_ID, Branch_Id and Account Number is Correct or Not.     //Refered By Abi
                for (int i = 0; i < transferTOs.size(); i++) {
                    transferTO = (TxTransferTO) transferTOs.get(i);
                    if ((transferTO.getProdId() != null) && !(transferTO.getProdType() !=null && transferTO.getProdType().length()>0 && transferTO.getProdType().equals("GL") || transferTO.getProdType().equals("AB"))) {
                        String prod_ID = CommonUtil.convertObjToStr(transferTO.getProdId());
                        String act_Num = CommonUtil.convertObjToStr(transferTO.getActNum());
                        if (prod_ID.length() > 0 && act_Num.length() == 13) {
                            transferTO.setProdId(act_Num.substring(4, 7));
                        }
                        if (act_Num.length() == 13) {
                            transferTO.setBranchId(act_Num.substring(0, 4));    //Resetting Branch Code
                        }
                    }else if(transferTO.getProdType() !=null && transferTO.getProdType().length()>0 && transferTO.getProdType().equals("GL")){
                        //condition added by rishad 31/aug/2018 for adding product id for locker but its prodtype GL ( for identifying product level of locker so divierting common gl format) 
                        if(!(transferTO.getTransModType()!=null && transferTO.getTransModType().length()>0 && transferTO.getTransModType().equals("LK"))){
                          transferTO.setProdId(null);}
                    }
                }
                //Account Head resetting (Enough changes done in TransferOB,below code commenting //Refered By Rajesh Sir
                /*for (int i = 0; i < transferTOs.size(); i++) {
                    // TxTransferTO transferTO;
                    prodHeadMap = new HashMap();
                    transferTO = (TxTransferTO) transferTOs.get(i);
                    String prod_ID = CommonUtil.convertObjToStr(transferTO.getProdId());
                    if ((transferTO.getProdId() != null) && !(transferTO.getProdType().equals("GL") || transferTO.getProdType().equals("TD"))) {
                        if (prod_ID.length() > 0) {
                            prodHeadMap.put("PROD_ID", prod_ID);
                            List prodHeadLst = sqlMap.executeQueryForList("getAccHeadHashMap", prodHeadMap);
                            if (prodHeadLst != null && prodHeadLst.size() > 0) {
                                prodHeadMap = (HashMap) prodHeadLst.get(0);
                                transferTO.setAcHdId(CommonUtil.convertObjToStr(prodHeadMap.get("AC_HD_ID")));
                            }
                        }
                    }
                }*/
//                prodHeadMap = null;
            }
            
            HashMap whereMap = new HashMap();
            whereMap.put("BRANCH_CODE", _branchCode);
            List shftlst = sqlMap.executeQueryForList("getBranchShiftDetails", whereMap);
            if (shftlst != null && shftlst.size() > 0) {
                HashMap hmap = (HashMap) shftlst.get(0);
                shift = CommonUtil.convertObjToStr(hmap.get("TRANSAUTH_TIME"));
                shifttime = CommonUtil.convertObjToStr(hmap.get("SHIFT"));
            }
            if (map.containsKey(CommonConstants.USER_ID)) {
                user = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
            } else {
                user = "";
            }

            if ((map.containsKey("ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER")) && (map.get("ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER") != null)) {
                act_closing_min_bal_check = CommonUtil.convertObjToStr(map.get("ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER"));
            }
//            if(map.containsKey("ALL_AMOUNT") && map.get("ALL_AMOUNT")!=null ){
            allTermLoanAmt = new HashMap();
            if (map.containsKey("ALL_AMOUNT")) {               
//                allTermLoanAmt = new HashMap();
                if(map.get(ALL_AMOUNT)!=null){
                allTermLoanAmt = (HashMap) map.get(ALL_AMOUNT);}
            }
         //   System.out.println("allTermLoanAmt : "+allTermLoanAmt);
//                if(/*(!map.containsKey(CommonConstants.AUTHORIZEMAP))||*/map.containsKey("LOAN_FROM_CHARGESUI")){  // Commented by Rajesh
            if (allTermLoanAmt != null && !allTermLoanAmt.containsKey("EXCESS_INTEREST") && map.containsKey("TxTransferTO")) {
                System.out.println("allTermLoanAmt######"+allTermLoanAmt);
                ArrayList toList = setTransactionDetailTLAD(map);
                map.put("TxTransferTO", toList);
            }
            if (!map.containsKey(CommonConstants.AUTHORIZEMAP)) {                
                ArrayList toList = setOtherBankTransactionDetails(map);
                if (!(toList != null && toList.size() > 0)) {
                    map.put("TxTransferTO", toList);
                }
            }
//                }
//            }
            // For Corporate Loan purpose added by Rajesh
            corpLoanMap = new HashMap();
            if (map.containsKey("CORP_LOAN_MAP")) {
//                corpLoanMap = new HashMap();
                corpLoanMap = (HashMap) map.get("CORP_LOAN_MAP");
            }
//                 }
            if (map.containsKey(DEPOSIT_PENAL_AMT)/*
                     * &&!map.containsKey(CommonConstants.AUTHORIZEMAP)
                     */) { // Commented by Rajesh
                double penalAmt = CommonUtil.convertObjToDouble(map.get(DEPOSIT_PENAL_AMT)).doubleValue();
                if (penalAmt > 0) {
                    ArrayList depositPenalList = penalReceived(map);
                    map.put("TxTransferTO", depositPenalList);
                    depositPenalList = new ArrayList();
                }
            } else if (map.containsKey("MULTIPLE_DEPOSIT_PENAL")) {
                ArrayList depositPenalList = penalReceived(map);
                map.put("TxTransferTO", depositPenalList);
                depositPenalList = new ArrayList();
            }
			//added by chithra for service Tax
            if (map.containsKey("SERVICE_TAX_DETAILS") && map.containsKey("SERVICE_TAX_MAP")) {
                ArrayList serviceTaxList = setServiceTax(map);
                map.put("TxTransferTO", serviceTaxList);
                serviceTaxList = new ArrayList();
           
            }
          //  System.out.println("LOANS_PARITICULARS :" + loanParticulars + ":");
        //    System.out.println("DEBIT_LOAN_TYPE :" + debitLoanType + ":");
            if (map.containsKey("LOAN_PARTICULARS") && map.get("LOAN_PARTICULARS") != null) {
                loanParticulars = "";
                loanParticulars = CommonUtil.convertObjToStr(map.get("LOAN_PARTICULARS"));
            }
            if (map.containsKey(FLEXI_LIEN_CREATION)) {
                flexiDeletionMap = new HashMap();
                flexiAmount = CommonUtil.convertObjToDouble(map.get("FLEXI_AMOUNT")).doubleValue();
                flexiDeletionMap.put(FLEXI_LIEN_CREATION, map.get(FLEXI_LIEN_CREATION));
            }
            if (map.containsKey("FLEXI_LIEN_DELETION")) {
                flexiDeletionMap = new HashMap();
                flexiDeletionMap.put("FLEXI_LIEN_DELETION", map.get("FLEXI_LIEN_DELETION"));
            }
            if (map.containsKey(LINK_BATCH_ID)) {
                if (map.get(LINK_BATCH_ID) != null && CommonUtil.convertObjToStr(map.get(LINK_BATCH_ID)).length()>0) {
                    linkBatchId = CommonUtil.convertObjToStr(map.get(LINK_BATCH_ID));
                }
            }
            if (map.containsKey("DEBIT_LOAN_TYPE") && map.get("DEBIT_LOAN_TYPE") != null) {
                debitLoanType = "";
                debitLoanType = CommonUtil.convertObjToStr(map.get("DEBIT_LOAN_TYPE"));
            }
           // System.out.println("@#@# LOANS_PARITICULARS :" + loanParticulars + ":");
         //   System.out.println("@#@# DEBIT_LOAN_TYPE :" + debitLoanType + ":");
            if (map.containsKey("EXCEPTION")) {
                isException = true;
            } else {
                isException = false;
            }
            // Log DAO
            LogDAO objLogDAO = new LogDAO();

            // Log Transfer Object
            LogTO objLogTO = new LogTO();

            objLogTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
            objLogTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
            objLogTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
            objLogTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
            objLogTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

            valueDateFlag = false;
            valueDateMap = null;
            if (map.containsKey("VALUE_DATE")) {
                valueDateFlag = true;
                valueDateMap = (HashMap) map.get("VALUE_DATE");
            }
            //This part is for koorkkancherry agent cash credit 
            if (map != null && map.containsKey("CASH_DAILY") && (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("KOORKKENCHERRY") || CommonConstants.VENDOR.equals("CHELLANOOR"))) {
                if (map.containsKey("CashTransactionTO")) {
                    HashMap applicationMap = new HashMap();
                    applicationMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                    applicationMap.put("USER_ID",CommonUtil.convertObjToStr( map.get(CommonConstants.USER_ID)));
                    applicationMap.put("TRANS_MOD_TYPE", "GL");
                    CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                    ArrayList applncashList = (ArrayList) map.get("CashTransactionTO");
                //    System.out.println("applncashList   " + applncashList);
                    applicationMap.put("DAILYDEPOSITTRANSTO", applncashList);
                    HashMap cashMap = cashTransactionDAO.execute(applicationMap,false);
                    cash_transid = CommonUtil.convertObjToStr(cashMap.get("TRANS_ID"));
                    execReturnMap.put("CASH_TRANS_ID", cash_transid);
                    if (CommonConstants.VENDOR.equals("CHELLANOOR")) {
                        execReturnMap.put("TRANS_MOD", CommonConstants.TX_CASH);
                        execReturnMap.put(CommonConstants.TRANS_ID, cash_transid);
                        if (map.containsKey(DAILYDEPOSITTRANSTO)) {
                            dailyDepList = (ArrayList) map.get(DAILYDEPOSITTRANSTO);
                            InsertintoDailyDepositCashTabl(dailyDepList, map);
                        }
                    }
                }
            }
            //end
            if (map.containsKey("TxTransferTO")) {
                if (isTransaction) {
                    sqlMap.startTransaction();
                }

                String command = CommonUtil.convertObjToStr(map.get(COMMAND));
               // System.out.println("command%%%%%" + command);
                ArrayList transferTOs = (ArrayList) map.get("TxTransferTO");
                int reconcilesize = 0;
                ArrayList reconciliationList = new ArrayList();
                if (map.containsKey("ReconciliationTO")) {
                    reconciliationList = (ArrayList) map.get("ReconciliationTO");
                    reconcilesize = reconciliationList.size();
                }
                boolean isBatch = false;
                for (int a = 0; a < transferTOs.size(); a++) {
              //      System.out.println("transferTOs.size()%%%%" + transferTOs.size());
                    TxTransferTO transferTO ;
                    transferTO = (TxTransferTO) transferTOs.get(a);
                    String batch = CommonUtil.convertObjToStr(transferTO.getBatchId());
//                    System.out.println("batch%%%%%" + batch);
//                    System.out.println("transferTO.getsingletransid %%%%% : " + transferTO.getSingleTransId());
                    if (batch.equalsIgnoreCase("-") || batch.equals("")) {
                        isBatch = false;
                    } else {
                        isBatch = true;
                        break;
                    }
                }
             //   System.out.println("isBatch%%%%%" + isBatch);
                if (!isBatch) {
                    command = CommonConstants.TOSTATUS_INSERT;
                }
                if (checkBatchTally(transferTOs)) {
                    TxTransferTO txTransferTO;
                    int size = transferTOs.size();
                 //   System.out.println("size^^^^^^" + size);
                    String batchIDD="";
                    int j = 0;
                    if (size > 0) {
                        String singleTransId = "";
                        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                            //Added By Suresh       
                            if (map.containsKey("BACK_DATED_TRANSACTION")) {//Only For Back Dated Transaction
                                batchId = this.getBackDateBatchID();
                                singleTransId = this.generateLinkID();
                                singleTransID = singleTransId;
                            } else {
//                                if(map.containsKey("LOGGED_BRANCH_ID") && map.containsKey("MDS_BONUS_ENTRY")){
//                                    if(map.containsKey(CommonConstants.BRANCH_ID) && map.get(CommonConstants.BRANCH_ID) != null && 
//                                    map.containsKey(CommonConstants.SELECTED_BRANCH_ID) && map.get(CommonConstants.SELECTED_BRANCH_ID) != null && 
//                                    !CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)).equals(map.get(CommonConstants.SELECTED_BRANCH_ID))){
//                                        HashMap mdsMap = new HashMap();
//                                        mdsMap.put("TRANS_DT", currDate.clone());
//                                        mdsMap.put("BRANCH_ID", map.get(CommonConstants.SELECTED_BRANCH_ID));
//                                        List mdsList = sqlMap.executeQueryForList("getSelectMaxBatchIdDiffBranch", mdsMap);
//                                        if (mdsList != null && mdsList.size() > 0) {
//                                            mdsMap = (HashMap) mdsList.get(0);
//                                            System.out.println("BatchId : "+CommonUtil.convertObjToLong(mdsMap.get("BATCH_ID")));
//                                            long diffDtBatchId = CommonUtil.convertObjToLong(mdsMap.get("BATCH_ID"));
//                                            batchId = CommonUtil.convertObjToStr("TBT"+diffDtBatchId);
//                                        }else{
//                                            batchId = this.getBatchID();                                            
//                                        }
//                                    }else{
//                                        batchId = this.getBatchID();
//                                    }
//                                }else{
                                    batchId = this.getBatchID();
//                                }
                                singleTransId = this.generateLinkID();
                                singleTransID = singleTransId;
                            }
                        }
                        for (int i = 0; i < size; i++) {
                            transferTOID = i;
                            txTransferTO = (TxTransferTO) transferTOs.get(i);
                            if (txTransferTO.getSingleTransId() != null && !txTransferTO.getSingleTransId().equals("") && txTransferTO.getSingleTransId().length() > 0) {
                                txTransferTO.setSingleTransId(txTransferTO.getSingleTransId());
                                generateSingleID = txTransferTO.getSingleTransId();
                              //  System.out.println("from other screen singleTransId : " + txTransferTO.getSingleTransId());
                            } else {
                                txTransferTO.setSingleTransId(singleTransId);
                              //  System.out.println("Same Screen singleTransId : " + singleTransId);
                            }
                        //    System.out.println("size^^^^^^" + txTransferTO);
                            if (txTransferTO.getGlTransActNum() != null && !txTransferTO.getGlTransActNum().equals("") && txTransferTO.getGlTransActNum().length() > 0) {
                                txTransferTO.setGlTransActNum(txTransferTO.getGlTransActNum());
                               // System.out.println("from other screen getGlTransActNum : " + txTransferTO.getGlTransActNum());
                            }
                            if (txTransferTO.getTransModType() != null && !txTransferTO.getTransModType().equals("") && txTransferTO.getTransModType().length() > 0) {
                                txTransferTO.setTransModType(txTransferTO.getTransModType());
                               // System.out.println("from other screen getTransModType : " + txTransferTO.getTransModType());
                            }
                            Date tempDt = null;
                            tempDt = (Date) properFormatDate.clone();
                            if (txTransferTO.getInstDt() != null) {
                                tempDt.setDate(txTransferTO.getInstDt().getDate());
                                tempDt.setMonth(txTransferTO.getInstDt().getMonth());
                                tempDt.setYear(txTransferTO.getInstDt().getYear());
                                txTransferTO.setInstDt(tempDt);
                            }
                            tempDt = (Date) properFormatDate.clone();
                            if (txTransferTO.getTransDt() != null) {
                                tempDt.setDate(txTransferTO.getTransDt().getDate());
                                tempDt.setMonth(txTransferTO.getTransDt().getMonth());
                                tempDt.setYear(txTransferTO.getTransDt().getYear());
                                txTransferTO.setTransDt(tempDt);
                            }
                            if (map.containsKey("INV_BACK_DATED_TRANSACTION")) {
                                Date transDt = (Date) map.get("TRANS_DATE");
                                tempDt.setDate(transDt.getDate());
                                tempDt.setMonth(transDt.getMonth());
                                tempDt.setYear(transDt.getYear());
                                txTransferTO.setTransDt(tempDt);
                            }
                            if (map.containsKey("INITIATED_BRANCH")) {
                                txTransferTO.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("INITIATED_BRANCH")));
                            }
                            if (CommonUtil.convertObjToDouble(txTransferTO.getAmount()).doubleValue() > 0) {

                                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                                    txTransferTO.setBatchId(batchId);
                                    // Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ] - added gold loan renewal key
                                    if (linkBatchId != null && linkBatchId.length() > 0 && (!map.containsKey("SHARE_SCREEN")&& !map.containsKey("DEPOSIT_TO_LOAN") && !map.containsKey("GOLD_LOAN_RENEWAL"))) {
                                        txTransferTO.setLinkBatchId(linkBatchId);
                                    }else{
                                         txTransferTO.setLinkBatchId(txTransferTO.getLinkBatchId());
                                    }
                                }
                                if (map.containsKey("DEP_INTEREST_AMT") && txTransferTO.getTransType().equals(TransactionDAOConstants.DEBIT)) {
                         //           System.out.println("##### DEP_INTEREST_AMT :" + map);
                                    HashMap depIntMap = new HashMap();
                                    depIntMap.put(TransactionDAOConstants.AMT, txTransferTO.getAmount());
                                    depIntMap.put(PROD_ID, txTransferTO.getProdId());
                                    depIntMap.put("TRANS_TYPE", txTransferTO.getTransType());
                                    depIntMap.put(DEPOSIT_NO, txTransferTO.getActNum());
                                    depIntMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                                    depIntMap = depositInterestTransfer(depIntMap);
                                 //   System.out.println("#####inside depIntMap :" + depIntMap);
                                    txTransferTO.setAcHdId(CommonUtil.convertObjToStr(depIntMap.get("INT_PAY")));
                                    txTransferTO.setActNum("");
                                    txTransferTO.setProdId("");
                                    txTransferTO.setProdType(TransactionFactory.GL);
                                    txTransferTO.setAmount(CommonUtil.convertObjToDouble(depIntMap.get("BALANCE_AMT")));
                                    txTransferTO.setLinkBatchId(CommonUtil.convertObjToStr(depIntMap.get(DEPOSIT_NO)));
                                    txTransferTO.setBranchId(CommonUtil.convertObjToStr(depIntMap.get(CommonConstants.BRANCH_ID)));
                                    txTransferTO.setInstrumentNo1("INTEREST_AMT");
                                    txTransferTO.setInstrumentNo2("DEPOSIT_RENEWAL");
                                 //   System.out.println("#####inside interestMap :" + txTransferTO);
                                }
                                if (map.containsKey("INV_BACK_DATED_TRANSACTION")) {
                                    txTransferTO.setScreenName("INV_BACK_DATED_TRANSACTION");
                                }else{                                    
                                    txTransferTO.setScreenName(txTransferTO.getScreenName());
                                }
                              //  System.out.println("#####bills :" + txTransferTO);
                                if (txTransferTO.getAuthorizeRemarks() != null && txTransferTO.getAuthorizeRemarks().equals("FROM_BILLS_MODULE") && txTransferTO.getProdType().equals(TransactionFactory.ADVANCES)) {
                                    transModuleBased = TransactionFactory.createTransaction("BILLS");
                                } else {
                                    transModuleBased = TransactionFactory.createTransaction(txTransferTO.getProdType());
                                }

//                                transModuleBased = TransactionFactory.createTransaction(txTransferTO.getProdType());
//                                if (txTransferTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED))
//                                    txTransferTO.setTransMode(TransactionDAOConstants.TRANSFER);//this line added.. incase edit mode  
//                                //Status where ever CREATED is trans mode is not updated...
                                if (txTransferTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED)
                                        && (txTransferTO.getTransId() == null || txTransferTO.getTransId().trim().length() == 0 || txTransferTO.getTransId().equalsIgnoreCase("-"))) {
                                     batchIDD=txTransferTO.getBatchId();
                                    this.insertData(txTransferTO, map);
                                    if (i == 0) {
                                        if (map.containsKey("ORG_RESP_DETAILS")) {
                                            insertData(map);
                                        }
                                    }
                                    if (!map.containsKey(LIST_OF_REDUCED) && reconcilesize > 0) {//incase reconciliation size is greater than 0,
                                        reconciliationTO = new ReconciliationTO();// it will store into reconciliation_tarans table
                                        for (j = j; j < reconcilesize; j++) {
                                            reconciliationTO = (ReconciliationTO) reconciliationList.get(j);
                                            if (!reconciliationTO.getAcHdId().equals("") && reconciliationTO.getAcHdId().length() > 0
                                                    && txTransferTO.getAcHdId().equals(reconciliationTO.getAcHdId())
                                                    && reconciliationTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED)) {
                                                reconciliationTO.setTransId(txTransferTO.getTransId());
                                                reconciliationTO.setBatchId(txTransferTO.getBatchId());
                                                reconciliationTO.setTransDt(txTransferTO.getTransDt());
                                                sqlMap.executeUpdate("insertReconciliationTO", reconciliationTO);//reconciliation table it will store
                                                j = j + 1;
                                                break;
                                            } else {
                                                break;
                                            }
                                        }
                                    }
                                    if (map.containsKey(LIST_OF_REDUCED) && map.get(LIST_OF_REDUCED) != null) {
                                        HashMap listMap = new HashMap();
                                        listMap = (HashMap) map.get(LIST_OF_REDUCED);
                                        ArrayList updateList = new ArrayList();
                                        updateList = (ArrayList) listMap.get("TOTAL_LIST");
                                        HashMap updateMap = new HashMap();
                                        if (updateList.size() > 0) {
                                            for (int k = 0; k < updateList.size(); k++) {
                                                reconciliationTO = new ReconciliationTO();// it will store into reconciliation_tarans table
                                                updateMap = new HashMap();
                                                String value = CommonUtil.convertObjToStr(((ArrayList) updateList.get(k)).get(0));
                                                String acHdId = CommonUtil.convertObjToStr(((ArrayList) updateList.get(k)).get(9));
                                                if (!value.equals("") && value.equals("true") && acHdId.equals(txTransferTO.getAcHdId())) {
                                                    double balance = CommonUtil.convertObjToDouble(((ArrayList) updateList.get(k)).get(7)).doubleValue();
                                                    String oldTransId = CommonUtil.convertObjToStr(((ArrayList) updateList.get(k)).get(8));
                                                    String oldBatchId = CommonUtil.convertObjToStr(((ArrayList) updateList.get(k)).get(1));
                                                    HashMap presentMap = new HashMap();
                                                    presentMap.put("PRESENT_TRANS_ID", txTransferTO.getTransId());
                                                    presentMap.put("PRESENT_BATCH_ID", txTransferTO.getBatchId());
                                                    presentMap.put("PRESENT_TRANS_DT", currDate.clone());
                                                    presentMap.put("PRESENT_AMOUNT", new Double(balance));
                                                    presentMap.put("BATCH_ID", oldBatchId);
                                                    presentMap.put("TRANS_ID", oldTransId);
                                                    presentMap.put("TRANS_DT", CommonUtil.convertObjToStr(((ArrayList) updateList.get(k)).get(2)));
                                                    presentMap.put("INITIATED_BRANCH", _branchCode);
                                              //      System.out.println("presentMap :" + presentMap);
                                                    sqlMap.executeUpdate("updatePresentTransaction", presentMap);
                                                }
                                            }
                                        }
                                    }
                                } else if (txTransferTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_MODIFIED)) {
                                    oldAmount = (Double) oldAmountMap.get(txTransferTO.getTransId());
                                     batchIDD=txTransferTO.getBatchId();
                                    this.updateData(txTransferTO, oldAmount.doubleValue());

                                    if (map.containsKey("ORG_RESP_DETAILS")) {
                                        updateData(map, txTransferTO);
                                    }

                                    if (!map.containsKey(LIST_OF_REDUCED)) {
                                        HashMap reconcileMap = new HashMap();
                                        reconcileMap.put("BATCH_ID", txTransferTO.getBatchId());
                                        reconcileMap.put("TRANS_DT", currDate.clone());
                                        reconcileMap.put("INITIATED_BRANCH", _branchCode);
                                        List reconcile = sqlMap.executeQueryForList("getSelectReconciliation", reconcileMap);
                                        if (reconcile != null && reconcile.size() > 0) {//incase reconciliation size is greater than 0,
                                            reconciliationTO = new ReconciliationTO();// it will store into reconciliation_tarans table
                                            for (j = j; j < reconcile.size(); j++) {
                                                reconciliationTO = (ReconciliationTO) reconcile.get(j);
                                                if (!reconciliationTO.getAcHdId().equals("") && reconciliationTO.getAcHdId().length() > 0
                                                        && txTransferTO.getAcHdId().equals(reconciliationTO.getAcHdId())
                                                        && txTransferTO.getTransId().equals(reconciliationTO.getTransId())
                                                        && txTransferTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_MODIFIED)) {
                                                    reconciliationTO.setStatus(txTransferTO.getStatus());
                                                    reconciliationTO.setStatusBy(txTransferTO.getStatusBy());
                                                    reconciliationTO.setTransAmount(txTransferTO.getAmount());
                                                    reconciliationTO.setBalanceAmount(txTransferTO.getAmount());
                                                    reconciliationTO.setTransDt(txTransferTO.getTransDt());
                                                    reconciliationTO.setStatusDt(txTransferTO.getStatusDt());
                                                    reconciliationTO.setInitiatedBranch(txTransferTO.getInitiatedBranch());
                                                    sqlMap.executeUpdate("updateReconciliationTO", reconciliationTO);//reconciliation table it will store
                                                    j = j + 1;
                                                    break;
                                                } else {
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    if (map.containsKey(LIST_OF_REDUCED) && map.get(LIST_OF_REDUCED) != null) {
                                        HashMap listMap = new HashMap();
                                        listMap = (HashMap) map.get(LIST_OF_REDUCED);
                                        ArrayList updateList = new ArrayList();
                                        updateList = (ArrayList) listMap.get("TOTAL_LIST");
                                        HashMap updateMap = new HashMap();
                                        if (updateList.size() > 0) {
                                            for (int k = 0; k < updateList.size(); k++) {
                                                reconciliationTO = new ReconciliationTO();// it will store into reconciliation_tarans table
                                                updateMap = new HashMap();
                                                String value = CommonUtil.convertObjToStr(((ArrayList) updateList.get(k)).get(0));
                                                double balance = CommonUtil.convertObjToDouble(((ArrayList) updateList.get(k)).get(7)).doubleValue();
                                                if (!value.equals("") && value.equals("true")) {
                                                    String oldTransId = CommonUtil.convertObjToStr(((ArrayList) updateList.get(k)).get(8));
                                                    String oldBatchId = CommonUtil.convertObjToStr(((ArrayList) updateList.get(k)).get(1));
                                                    HashMap presentMap = new HashMap();
                                                    presentMap.put("PRESENT_TRANS_ID", txTransferTO.getTransId());
                                                    presentMap.put("PRESENT_BATCH_ID", txTransferTO.getBatchId());
                                                    presentMap.put("PRESENT_AMOUNT", new Double(balance));
                                                    presentMap.put("BATCH_ID", oldBatchId);
                                                    presentMap.put("TRANS_ID", oldTransId);
                                                    presentMap.put("PRESENT_TRANS_DT", currDate.clone());
                                                    presentMap.put("TRANS_DT", CommonUtil.convertObjToStr(((ArrayList) updateList.get(k)).get(2)));
                                                    presentMap.put("INITIATED_BRANCH", _branchCode);
                                                    System.out.println("presentMap :" + presentMap);
                                                    sqlMap.executeUpdate("updatePresentTransaction", presentMap);
                                                } else if (!value.equals("") && value.equals("false") && balance == 0) {
                                                    String oldTransId = CommonUtil.convertObjToStr(((ArrayList) updateList.get(k)).get(8));
                                                    String oldBatchId = CommonUtil.convertObjToStr(((ArrayList) updateList.get(k)).get(1));
                                                    HashMap presentMap = new HashMap();
                                                    presentMap.put("PRESENT_TRANS_ID", "");
                                                    presentMap.put("PRESENT_BATCH_ID", "");
                                                    presentMap.put("PRESENT_AMOUNT", new Double(0));
                                                    presentMap.put("BATCH_ID", oldBatchId);
                                                    presentMap.put("TRANS_ID", oldTransId);
                                                    presentMap.put("PRESENT_TRANS_DT", currDate.clone());
                                                    presentMap.put("TRANS_DT", CommonUtil.convertObjToStr(((ArrayList) updateList.get(k)).get(2)));
                                                    presentMap.put("INITIATED_BRANCH", _branchCode);
                                                    System.out.println("presentMap :" + presentMap);
                                                    sqlMap.executeUpdate("updatePresentTransaction", presentMap);
                                                }
                                            }
                                        }
                                    }
                                } else if (txTransferTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_DELETED)
                                        && !(txTransferTO.getTransId() == null || txTransferTO.getTransId().trim().length() == 0 || txTransferTO.getTransId().equalsIgnoreCase("-"))) {  // This line added because without creating transaction updating shadow credit/debit
                                    if (map.containsKey("BACK_DATED_TRANSACTION_DELETION")) {
                                        this.deleteData(txTransferTO,objLogTO);
                                    }else{
                                        this.deleteData(txTransferTO);
                                    }

                                    if (map.containsKey("ORG_RESP_DETAILS")) {
                                        deleteData(map, txTransferTO);
                                    }

                                    if (!map.containsKey(LIST_OF_REDUCED)) {
                                        HashMap reconcileMap = new HashMap();
                                        reconcileMap.put("BATCH_ID", txTransferTO.getBatchId());
                                        reconcileMap.put("TRANS_DT", currDate.clone());
                                        reconcileMap.put("INITIATED_BRANCH", _branchCode);
                                        List reconcile = sqlMap.executeQueryForList("getSelectReconciliation", reconcileMap);
                                        if (reconcile != null && reconcile.size() > 0) {//incase reconciliation size is greater than 0,
                                            reconciliationTO = new ReconciliationTO();// it will store into reconciliation_tarans table
                                            for (j = j; j < reconcile.size(); j++) {
                                                reconciliationTO = (ReconciliationTO) reconcile.get(j);
                                                if (!reconciliationTO.getAcHdId().equals("") && reconciliationTO.getAcHdId().length() > 0
                                                        && txTransferTO.getAcHdId().equals(reconciliationTO.getAcHdId())
                                                        && txTransferTO.getTransId().equals(reconciliationTO.getTransId())
                                                        && txTransferTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_DELETED)) {
                                                    reconciliationTO.setStatus(txTransferTO.getStatus());
                                                    reconciliationTO.setStatusBy(txTransferTO.getStatusBy());
                                                    reconciliationTO.setTransAmount(txTransferTO.getAmount());
                                                    reconciliationTO.setBalanceAmount(txTransferTO.getAmount());
                                                    reconciliationTO.setTransDt(txTransferTO.getTransDt());
                                                    reconciliationTO.setStatusDt(txTransferTO.getStatusDt());
                                                    reconciliationTO.setInitiatedBranch(txTransferTO.getInitiatedBranch());
                                                    sqlMap.executeUpdate("updateReconciliationTO", reconciliationTO);//reconciliation table it will store
                                                    j = j + 1;
                                                    break;
                                                } else {
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    HashMap reconcileAuthMap = new HashMap();
                                    reconcileAuthMap.put("PRESENT_TRANS_ID", txTransferTO.getTransId());
//                                    properFormatDate = txTransferTO.getTransDt();
                               //     System.out.println("txTransferTO.getTransDt()#### :" + txTransferTO.getTransDt());
                                    Date trDt = (Date) properFormatDate.clone();
                                    trDt.setDate(txTransferTO.getTransDt().getDate());
                                    trDt.setMonth(txTransferTO.getTransDt().getMonth());
                                    trDt.setYear(txTransferTO.getTransDt().getYear());
//                                    reconcileAuthMap.put("TRANS_DT",trDt);
                                    reconcileAuthMap.put("PRESENT_TRANS_DT", trDt);
                                 //   System.out.println("reconcileAuthMapsDt()#### :" + reconcileAuthMap);
                                    List lst = sqlMap.executeQueryForList("getSelectAllinDeleteMode", reconcileAuthMap);
                                    if (lst != null && lst.size() > 0) {
                                        for (int k = 0; k < lst.size(); k++) {
                                            reconcileAuthMap = (HashMap) lst.get(k);
                                            HashMap presentMap = new HashMap();
                                            presentMap.put("PRESENT_TRANS", "");
                                            presentMap.put("PRESENT_BATCH", "");
                                            presentMap.put("PRESENT_AMOUNT", new Double(0));
                                            presentMap.put("PRESENT_TRANS_ID", reconcileAuthMap.get("PRESENT_TRANS_ID"));
                                            presentMap.put("PRESENT_TRANS_DT", reconcileAuthMap.get("PRESENT_TRANS_DT"));
                                            System.out.println("presentMap :" + presentMap);
                                            sqlMap.executeUpdate("deleteOldReconcileAmt", presentMap);
                                        }
                                    }
                                }
                                if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
                                   // System.out.println("toList before ----------------->" + toList);
                                    if (toList == null) {
                                        toList = new ArrayList();
                                    }
                                    toList.add(txTransferTO);
                                }
                                objLogTO.setData(txTransferTO.toString());
                                objLogTO.setPrimaryKey(txTransferTO.getKeyData());
                                objLogTO.setStatus(command);

                                objLogDAO.addToLog(objLogTO);
                            }
                        }
						//added by chithra for service Tax
                        if (batchIDD != null && !batchIDD.equals("") && map.containsKey("SERVICE_TAX_DETAILS")) {
                            deleteFromServiceTaxDetails(batchIDD);
				ArrayList serviceTaxList = (ArrayList) map.get("SERVICE_TAX_DETAILS");
                            if (serviceTaxList != null && serviceTaxList.size() > 0) {
                                ServiceTaxDetailsTO serObj = (ServiceTaxDetailsTO) serviceTaxList.get(0);
                                serObj.setAcct_Num(batchIDD);
                                insertServiceTaxDetails(serObj);
                            }
                        }
                        if (map.containsKey(DAILYDEPOSITTRANSTO)) {
                            dailyDepList = (ArrayList) map.get(DAILYDEPOSITTRANSTO);
                           // System.out.println("dailyDepList 000====" + dailyDepList + " map 0000====" + map);
                            InsertintoDailyDepositTabl(dailyDepList, map);

                        }
                        if (map.containsKey("PROCCHARGEHASH")) {        //This for transfer termloan processingcharge
                            insertMap.put(COMMAND, map.get(COMMAND));
                            insertMap.put("PROCCHARGEHASH", map.get("PROCCHARGEHASH"));
                          //  System.out.println("beforegoing to operative#### :" + insertMap);
                            updateProcessingChargesFromOperative(insertMap);
                        }
                        System.out.println(this.toList);

                        txTransferTO = null;
                        transferTOs = null;
                    }
                } else {
                    throw new TTException("Batch not tallied...\n" + "Debit Amount :" + drAmt + "\n" + "Credit Amount :" + crAmt);
                }
                if (isTransaction) {
                    sqlMap.commitTransaction();
                }

                if (isTransaction && command.equals(CommonConstants.TOSTATUS_INSERT) || map.containsKey("MODE")) {
                    execReturnMap.put(CommonConstants.TRANS_ID, batchId);
                  //  System.out.println("generateSingleID###" + generateSingleID);
               //     System.out.println("singleTransID###" + singleTransID);
                 //   System.out.println("batchId###" + batchId);
                    if (generateSingleID != null && generateSingleID.length() > 0) {
                        execReturnMap.put("SINGLE_TRANS_ID", generateSingleID);
                    } else {
                        execReturnMap.put("SINGLE_TRANS_ID", singleTransID);
                    }
                }
                // commented on  from svn machine 10/04/2014
//                    if(singleTransID!=null){
//                        execReturnMap.put("SINGLE_TRANS_ID", singleTransID);
//                    }else
//                        execReturnMap.put("SINGLE_TRANS_ID", generateSingleID);
//                    }
            }

            if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
                HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
                String status = (String) authMap.get(CommonConstants.AUTHORIZESTATUS);
                authMap.put("DB_DRIVER_NAME", map.get("DB_DRIVER_NAME"));
                if (authMap != null) {
                //    System.out.println("authMap in TransferDao execute : " + authMap);
                    if (isTransaction) {
                        sqlMap.startTransaction();
                    }
		//added by chithra for service Tax
                    if (this.batchId != null && map.containsKey("SERVICE_TAX_AUTH")) {
                        HashMap serMapAuth = new HashMap();
                        serMapAuth.put("STATUS", status);
                        serMapAuth.put("USER_ID", objLogTO.getUserId());
                        serMapAuth.put("AUTHORIZEDT", properFormatDate.clone());
                        serMapAuth.put("ACCT_NUM",authMap.get("BATCH_ID"));
                        sqlMap.executeUpdate("authorizeServiceTaxDetails", serMapAuth);
                    }
                    if (map.containsKey(LINK_BATCH_ID)) {
                        if (map.get(LINK_BATCH_ID) != null && CommonUtil.convertObjToStr(map.get(LINK_BATCH_ID)).length()>0) {
                            HashMap procMap = new HashMap();
                            procMap.put(LINK_BATCH_ID, map.get(LINK_BATCH_ID));
                            procMap.put(BATCH_ID, map.get(BATCH_ID));
                            procMap.put("REMARKS", map.get("REMARKS"));
                            if (map.get("REMARKS") != null) {
                                authMap.put("PROCMAP", procMap);
                            }
                    //        System.out.print("SETVALUEOF####" + authMap);
                        }
                    }
                    if (map.containsKey("ORG_RESP_DETAILS")) {
                        ArrayList list = null;
                        list = (ArrayList) map.get("ORG_RESP_DETAILS");
                        if (list != null && list.size() > 0) {
                            HashMap hmap = (HashMap) list.get(0);
                            String type = CommonUtil.convertObjToStr(hmap.get("OrgOrRespTransType"));
                            if (type.equals("R")) {
                                hmap.put("RECONSILED", "RESPONDED");
                                hmap.put("DATE", properFormatDate.clone());
                                sqlMap.executeUpdate("updateReconsiledOrg", hmap);
                            }
                            hmap.put("STATUS", status);
                            hmap.put("STATUS_BY", objLogTO.getUserId());
                            hmap.put("STATUS_DT", properFormatDate.clone());
                            sqlMap.executeUpdate("authorizeOrgRespDetails", hmap);
                        }
                    }
                    if (map.containsKey("DEPOSIT_CLOSING")) {
                        depositClosingFlag = true;
                    }
                    if (map.containsKey("LOAN_TRANS_OUT")) {
                        authMap.put("LOAN_TRANS_OUT", map.get("LOAN_TRANS_OUT"));
                    }
                    if (map.containsKey("PBAL")) {
                        authMap.put("PBAL", map.get("PBAL"));
                    }
                    if (map.containsKey("INTER_BRANCH_TRANS") && new Boolean(CommonUtil.convertObjToStr(map.get("INTER_BRANCH_TRANS"))).booleanValue()) {

                        authMap.put("INTER_BRANCH_TRANS", map.get("INTER_BRANCH_TRANS"));
                    }
                    if (map.containsKey("INVESTMENT_CONTRA")) {
                        authMap.put("INVESTMENT_CONTRA", "INVESTMENT_CONTRA");
                    }
                    //Added By Suresh   BackDated Transaction
                 //   System.out.println("############ Before Calling Authorize Method : " + map);
                    if (map.containsKey("BACK_DATED_TRANSACTION")) {
                        authMap.put("BACK_DATED_TRANSACTION", "BACK_DATED_TRANSACTION");
                        authMap.put("TRANS_DATE", map.get("TRANS_DATE"));
                    }
                    if (map.containsKey("INTERBRANCH_CREATION_SCREEN")) {
                        authMap.put("INTERBRANCH_CREATION_SCREEN", map.get("INTERBRANCH_CREATION_SCREEN"));
                    }
                    if (map.containsKey("ACTUAL_INTEREST") && map.get("ACTUAL_INTEREST") != null) {
                        authMap.put("ACTUAL_INTEREST", map.get("ACTUAL_INTEREST"));
                    }
                    if(map.containsKey("LOAN_CLOSING_SCREEN")){
                        authMap.put("LOAN_CLOSING_SCREEN", "LOAN_CLOSING_SCREEN");
                    }
                    if(map.containsKey("VOUCHER_RELEASE")){
                        authMap.put("VOUCHER_RELEASE", CommonUtil.getProperDate(currDate, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("VOUCHER_RELEASE")))));
                    }
                    if(map.containsKey("REC_PRINCIPAL")){
                        authMap.put("REC_PRINCIPAL", CommonUtil.convertObjToDouble(map.get("REC_PRINCIPAL")));
                    }
                    if(map.containsKey("MDS_BONUS_ENTRY")){
                        authMap.put("MDS_BONUS_ENTRY", CommonUtil.convertObjToStr(map.get("MDS_BONUS_ENTRY")));
                    }
                    if(map.containsKey("LOGGED_BRANCH_ID")){
                        authMap.put("LOGGED_BRANCH_ID", CommonUtil.convertObjToStr(map.get("LOGGED_BRANCH_ID")));
                    }
                    if (map.containsKey("GROUP_SMS") || depositClosingFlag) { //Added By Kannan AR
                        authMap.put("GROUP_SMS", "GROUP_SMS");
                    }
                    if (map.containsKey("DEPOSIT_MULTIPLE_RENEWAL")) { //Added By Kannan AR
                        authMap.put("DEPOSIT_MULTIPLE_RENEWAL", "DEPOSIT_MULTIPLE_RENEWAL");
                    }

                    if (map.containsKey("CREDIT_TO_DEPOSIT_TRANSFER_SCREEN")) {
                        authMap.put("CREDIT_TO_DEPOSIT_TRANSFER_SCREEN", "CREDIT_TO_DEPOSIT_TRANSFER_SCREEN");
                    }
                    
                    if (map.containsKey("DAILY_DEPOSIT_AGENT_ID") && map.get("DAILY_DEPOSIT_AGENT_ID") != null) {
                        authMap.put("DAILY_DEPOSIT_AGENT_ID",map.get("DAILY_DEPOSIT_AGENT_ID"));
                        authMap.put("CREDIT_TO_DEPOSIT_TRANSFER_SCREEN", "CREDIT_TO_DEPOSIT_TRANSFER_SCREEN");
                    }
                    
                    authorize(authMap, objLogDAO, objLogTO);
                    if (map.containsKey(DAILYDEPOSITTRANSTO)) {
                      //  System.out.println("DAILYDEPOSITTRANSTO inside &&&&&&&&&&&&&&&&&&=" + map);
                        ArrayList upList = new ArrayList();
                        upList = (ArrayList) map.get(DAILYDEPOSITTRANSTO);
                      //  System.out.println("DAILYDEPOSIupList  &&&&&&&&&&&&&=" + upList);
                      //  System.out.println("DAILYDEmapmapmap  &&&&&&&&&&&&&=" + map);
                        if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("POLPULLY")) {
                            authorizeDailyDepositOnlyPolpully(upList, map);
                        //}
                        //else if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("KOORKKENCHERRY")) {
                        //    authorizeDailyDepositOnlyKorr(upList, map);
                        }else if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("CHELLANOOR")) {
                            authorizeDailyDepositOnly(upList, map);
                        } else {
                            authorizeDailyDepositOnlyKorr(upList, map);
                        }

                    }
                    depositClosingFlag = false;

                    if (isTransaction) {
                        sqlMap.commitTransaction();
                    }
                    authMap = null;
                }
            }
            //else
            // throw new NoCommandException();
            oldAmount = null;
            objLogDAO = null;
            objLogTO = null;
            map = null;
            flexiDeletionMap = null;
            debitLoanType = "";
            loanParticulars = "";
            act_closing_min_bal_check = null;
        } catch (Exception e) {
            e.printStackTrace();

            if (isTransaction) {
                sqlMap.rollbackTransaction();
            }
            if (e instanceof TTException) {

            }else if (e.getMessage().contains("integrity constraint")) {
                throw new TTException("Account head not set/entered");
            }
            throw e;
        }
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return execReturnMap;
    }

    private boolean checkBatchTally(ArrayList transferTOs) throws Exception {
        TxTransferTO txTransferTO;

        int size = transferTOs.size();
        drAmt = 0;
        crAmt = 0;
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                txTransferTO = (TxTransferTO) transferTOs.get(i);
                if (txTransferTO.getTransType().equals(CommonConstants.CREDIT)) {
                    crAmt += txTransferTO.getAmount().doubleValue();
                } else {
                    drAmt += txTransferTO.getAmount().doubleValue();
                }
            }
            if (drAmt == crAmt) {
                return true;
            }
        }
        return true;
    }

    private void deleteData(HashMap map, TxTransferTO txTransferTO) {
        ArrayList list = (ArrayList) map.get("ORG_RESP_DETAILS");
        if (list != null && list.size() > 0) {
            try {
                map = (HashMap) list.get(0);
                map.put("STATUS", "DELETED");
                map.put("STATUS_DT", properFormatDate);
                map.put("STATUS_BY", user);
                map.put("BRANCH_ID", _branchCode);
                map.put("TRANS_ID", txTransferTO.getBatchId());
                sqlMap.executeUpdate("deleteOrgRespDetails", map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private HashMap getTransferRuleMap(TxTransferTO objTxTransferTO) throws Exception {
        HashMap inputMap = new HashMap();
        System.out.println("objobjTxTransferTO:" + objTxTransferTO);
        double amount = objTxTransferTO.getAmount().doubleValue();
        String acctNo = objTxTransferTO.getActNum();
        if (objTxTransferTO.getTransType().equals(TransactionDAOConstants.DEBIT)) {
            inputMap.put(TransactionDAOConstants.TRANS_TYPE, TransactionDAOConstants.CREDIT);
            inputMap.put("NORMAL", "");
        } else {
            inputMap.put(TransactionDAOConstants.TRANS_TYPE, TransactionDAOConstants.DEBIT);
            inputMap.put("NORMALDEBIT", "");
            amount = -1 * amount;
        }
        if (objTxTransferTO.getProdType().equals(TransactionFactory.GL)) {
            objTxTransferTO.setActNum(null);  // This condition added to prevent actnum entries in GL
        }
        if ((acctNo == null || acctNo.equals("")) && (objTxTransferTO.getAcHdId() != null)) {
            acctNo = objTxTransferTO.getAcHdId();
        }
        inputMap.put(TransactionDAOConstants.ACCT_NO, acctNo);
        inputMap.put("ACT_NUM", acctNo);
        inputMap.put("TRANS_DT", objTxTransferTO.getTransDt());
        inputMap.put("AMT", objTxTransferTO.getAmount());
        inputMap.put(TransactionDAOConstants.AMT, CommonUtil.convertObjToDouble(String.valueOf(amount)));
        inputMap.put(TransactionDAOConstants.INSTRUMENT_TYPE, objTxTransferTO.getInstType());
        inputMap.put(TransactionDAOConstants.INSTRUMENT_1, CommonUtil.convertObjToStr(objTxTransferTO.getInstrumentNo1()));
        inputMap.put(TransactionDAOConstants.INSTRUMENT_2, CommonUtil.convertObjToStr(objTxTransferTO.getInstrumentNo2()));
        inputMap.put(TransactionDAOConstants.DATE, objTxTransferTO.getInstDt());
        inputMap.put(TransactionDAOConstants.BRANCH_CODE, objTxTransferTO.getBranchId());
        inputMap.put(TransactionDAOConstants.INITIATED_BRANCH, objTxTransferTO.getInitiatedBranch());
        inputMap.put(TransactionDAOConstants.TO_STATUS, objTxTransferTO.getStatus());
        inputMap.put(TransactionDAOConstants.AUTHORIZE_BY, objTxTransferTO.getAuthorizeBy());
        inputMap.put(TransactionDAOConstants.AUTHORIZE_STATUS, objTxTransferTO.getAuthorizeStatus());
        inputMap.put(TransactionDAOConstants.TRANS_ID, objTxTransferTO.getTransId());
        inputMap.put("BATCH_ID", objTxTransferTO.getBatchId());
        inputMap.put(TransactionDAOConstants.TODAY_DT, currDate);
        inputMap.put(TransactionDAOConstants.TRANS_MODE, TransactionDAOConstants.CASH);
        inputMap.put("ACTUAL_AMT", objTxTransferTO.getAmount()); // Added by Rajesh for LimitCheckingRule. No need to deduct the previous amount for Operative. Because the system should check the actual amount with Minimum Balance (on product level)
        if (objTxTransferTO.getProdType().equals(TransactionFactory.LOANS) || objTxTransferTO.getProdType().equals(TransactionFactory.ADVANCES) || objTxTransferTO.getProdType().equals("ATL")) {
            inputMap.put(PROD_ID, objTxTransferTO.getProdId());
            inputMap.put("PROD_TYPE", objTxTransferTO.getProdType());
            inputMap.put("ROLLBACKTRANSACTION", "ROLLBACKTRANSACTION");
            inputMap.put("PRINCIPAL_AMOUNT", objTxTransferTO.getAmount());
        }
        if (objTxTransferTO.getParticulars() != null) {
            inputMap.put(TransactionDAOConstants.PARTICULARS, objTxTransferTO.getParticulars());
        }
        return inputMap;
    }
    
    private void deleteData(TxTransferTO obj,LogTO objLogTO) throws Exception {
        obj.setStatus(CommonConstants.STATUS_DELETED);
        obj.setStatusDt(properFormatDate);
        obj.setTransDt(currDate);
        obj.setInitiatedBranch(_branchCode);
        Date backdatedDate = obj.getTransDt();
        if ((obj.getTransType().equals("DEBIT") || (obj.getTransType().equals("CREDIT")))) {
            obj.setAuthorizeStatus(CommonConstants.STATUS_REJECTED);
            obj.setLinkBatchId(obj.getLinkBatchId());
            obj.setAuthorizeBy(obj.getStatusBy());
            obj.setAuthorizeDt(obj.getTransDt());
            System.out.println("&*&*&* valueDateMap : "+valueDateMap);
            if (valueDateMap != null && valueDateMap.size() > 0) {
                if (valueDateMap.containsKey(String.valueOf(transferTOID))) {
                    backdatedDate = (Date) valueDateMap.get(String.valueOf(transferTOID));
                    obj.setTransDt(backdatedDate);
                }
            }
        }
        sqlMap.executeUpdate("deleteBackDatedDetailTransferTO", obj);
        // Deleting inter branch entry -- added by nithya on 07-11-2022 for KD-3436
        if (obj.getBranchId() != null && obj.getInitiatedBranch() != null && !obj.getBranchId().equals(obj.getInitiatedBranch())) {
            HashMap deleteIBRMap = new HashMap();
            deleteIBRMap.put("TRANS_ID", obj.getTransId());
            deleteIBRMap.put("BRANCH_ID", obj.getBranchId());
            deleteIBRMap.put("INITIATED_BRANCH", obj.getInitiatedBranch());
            deleteIBRMap.put("APP_DT", getProperDateFormat(obj.getTransDt()));
            System.out.println("deleteIBRMap : " + deleteIBRMap);
            sqlMap.executeUpdate("deleteIBRRollbackRecords", deleteIBRMap);
            HashMap transRefMap = new HashMap();
            transRefMap.put("TRANS_ID", obj.getBatchId() + "_" + obj.getTransId());
            transRefMap.put("STATUS", CommonConstants.STATUS_DELETED);
            transRefMap.put("INITIATED_BRANCH", obj.getInitiatedBranch());
            transRefMap.put("TRANS_DT", obj.getTransDt());
            System.out.println("transRefMap ### : " + transRefMap);
            sqlMap.executeUpdate("updateTransRefGLStatus", transRefMap);
        }
        // End
        double amount = 0;
        HashMap whereMap = new HashMap();
        whereMap.put("AC_HD_ID", CommonUtil.convertObjToStr(obj.getAcHdId()));
        whereMap.put("BRANCH_CODE", CommonUtil.convertObjToStr(obj.getBranchId()));
        whereMap.put("AMOUNT", amount);
        whereMap.put("FROM_DT", backdatedDate);
        whereMap.put("TO_DT", properFormatDate);
        whereMap.put("TRANS_TYPE", CommonUtil.convertObjToStr(obj.getTransType()));
        whereMap.put("DT", DateUtil.addDays(backdatedDate,-1));
        System.out.println("############# whereMap : " + whereMap);  
        sqlMap.executeUpdate("glupdate", whereMap);
        if(obj.getProdType().equals(CommonConstants.TXN_PROD_TYPE_OPERATIVE)){
            whereMap.put("ACT_NUM",obj.getActNum());
            sqlMap.executeUpdate("updateBackDatedDayEndBalance", whereMap);
            sqlMap.executeUpdate("updateBackDatedPassbookBalance", whereMap);
        }
        else if(obj.getProdType().equals(CommonConstants.TXN_PROD_TYPE_SUSPENSE)){
            sqlMap.executeUpdate("updateBackDatedSADayEndBalance", whereMap);
        }
        System.out.println("######TransferDAO obj" + obj);
        if (valueDateFlag) {
            storeValueDate(obj);
        }
        log.info("Delete ... objTransferTransactionTO : " + obj.toString());
    }
    
    private void deleteData(TxTransferTO obj) throws Exception {
        transModuleBased.performShadowMinus(getRuleMap(obj, 0.0, false));
        //transModuleBased.performOtherBalanceMinus(getRuleMap(obj, 0.0, true));
        obj.setStatusDt(properFormatDate);
        if (!obj.getTransId().equals("") && obj.getTransId().length() > 0 && obj.getActNum() != null && obj.getTransType().equals(TransactionDAOConstants.DEBIT)) {
            HashMap cashMap = new HashMap();
            cashMap.put(BATCH_ID, obj.getBatchId());
            System.out.println("cashMap " + cashMap);
            List lstLien = sqlMap.executeQueryForList("getSBLienTransferAccountNo", cashMap);
            if (lstLien != null && lstLien.size() > 0) {
                for (int i = 0; i < lstLien.size(); i++) {
                    cashMap = (HashMap) lstLien.get(i);
                    System.out.println("cashMap " + cashMap);
                    HashMap flexiMap = new HashMap();
                    String lienNo = CommonUtil.convertObjToStr(cashMap.get("LIEN_NO"));
                    flexiMap.put("DEPOSIT_ACT_NUM", cashMap.get(DEPOSIT_NO));
                    flexiMap.put(SHADOWLIEN, new Double(CommonUtil.convertObjToDouble(cashMap.get(LIEN_AMOUNT)).doubleValue()));
                    flexiMap.put(COMMAND, CommonConstants.STATUS_DELETED);
                    flexiMap.put("STATUS", CommonConstants.STATUS_DELETED);
                    flexiMap.put("USER_ID", obj.getStatusBy());
                    flexiMap.put("LIEN_AC_NO", obj.getActNum());
                    flexiMap.put(AUTHORIZE_STATUS, CommonConstants.STATUS_REJECTED);
                    flexiMap.put("AUTHORIZE_DATE", properFormatDate);
                    flexiMap.put(LIENNO, lienNo);
                    flexiMap.put(LIEN_NO, lienNo);
                    flexiMap.put("SUBNO", CommonUtil.convertObjToInt("1"));
                    flexiMap.put("UNLIEN_DT", properFormatDate);
                    flexiMap.put(CommonConstants.ACT_NUM, obj.getActNum());
                    flexiMap.put("LIENAMOUNT", new Double(0.0));//String.valueOf(0.0));
                    flexiMap.put(LIEN_AMOUNT, cashMap.get(LIEN_AMOUNT));
                    System.out.println("flexiMap : " + flexiMap);
                    sqlMap.executeUpdate("updateSubAcInfoBal", flexiMap);
                    sqlMap.executeUpdate("updateForSBLienMarking", flexiMap);
                    sqlMap.executeUpdate("updateReducingLienAmountDAO", flexiMap);
                    flexiMap = null;
                }
                cashMap = null;
            }
        }
        obj.setTransDt(currDate);
        obj.setInitiatedBranch(_branchCode);
        if ((obj.getTransType().equals("DEBIT") || (obj.getTransType().equals("CREDIT")))) {
            obj.setAuthorizeStatus(CommonConstants.STATUS_REJECTED);
            obj.setLinkBatchId(obj.getLinkBatchId());
            obj.setAuthorizeBy(CommonConstants.USER_ID);
            obj.setAuthorizeDt(obj.getTransDt());
        }
        sqlMap.executeUpdate("deleteDetailTransferTO", obj);
        System.out.println("######TransferDAO obj" + obj);
        if (valueDateFlag) {
            storeValueDate(obj);
        }
        log.info("Delete ... objTransferTransactionTO : " + obj.toString());
    }
    /*
     * method to get the batch id, will be called once for one batch
     */

    private String getBatchID() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "TRANSFER.BATCH_ID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//for trans_batch_id resetting, branch code
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        String batchID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        return batchID;
    }

    //Added BY Suresh For Only Back Dated Transaction
    private String getBackDateBatchID() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "BACK_DATE.BATCH_ID");
        String batchID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        return batchID;
    }
    /*
     * method to get the transaction id, will be called for each insert
     */

    private String getTransID() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "TRANSFER_TRANS_ID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//for trans_batch_id resetting, branch code
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        String transID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        return transID;
    }

    //Added By Suresh
    private String getBackDateTransID() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "BACK_DATE_TRANS_ID");
        String transID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        return transID;
    }

    private HashMap getRuleMap(TxTransferTO txTransferTORuleMap, double prevAmount, boolean makeNegative) throws Exception {
        HashMap inputMap = new HashMap();
        double amount = txTransferTORuleMap.getAmount().doubleValue();

        if (txTransferTORuleMap.getTransType().equals(TransactionDAOConstants.DEBIT) && makeNegative) {
            amount = -amount + prevAmount;
        } else {
            amount -= prevAmount;
        }

        if (txTransferTORuleMap.getProdType().equals(TransactionFactory.GL)) {
            txTransferTORuleMap.setActNum(null);  // This condition added to prevent actnum entries in GL
        }

        String acctNo = txTransferTORuleMap.getActNum();

        if ((acctNo == null || acctNo.equals("")) && (txTransferTORuleMap.getAcHdId() != null)) {
            acctNo = txTransferTORuleMap.getAcHdId();
        }

        inputMap.put(TransactionDAOConstants.ACCT_NO, acctNo);
        inputMap.put(TransactionDAOConstants.AMT, CommonUtil.convertObjToDouble(String.valueOf(amount)));
        inputMap.put(TransactionDAOConstants.INSTRUMENT_TYPE, txTransferTORuleMap.getInstType());
        inputMap.put(TransactionDAOConstants.INSTRUMENT_1, CommonUtil.convertObjToStr(txTransferTORuleMap.getInstrumentNo1()));
        inputMap.put(TransactionDAOConstants.INSTRUMENT_2, CommonUtil.convertObjToStr(txTransferTORuleMap.getInstrumentNo2()));
        inputMap.put(TransactionDAOConstants.DATE, txTransferTORuleMap.getInstDt());
        inputMap.put(TransactionDAOConstants.TRANS_TYPE, txTransferTORuleMap.getTransType());
        inputMap.put(TransactionDAOConstants.BRANCH_CODE, txTransferTORuleMap.getBranchId());
        inputMap.put(TransactionDAOConstants.INITIATED_BRANCH, txTransferTORuleMap.getInitiatedBranch());
        inputMap.put(TransactionDAOConstants.TO_STATUS, txTransferTORuleMap.getStatus());
        inputMap.put(TransactionDAOConstants.AUTHORIZE_BY, txTransferTORuleMap.getAuthorizeBy());
        inputMap.put(TransactionDAOConstants.AUTHORIZE_STATUS, txTransferTORuleMap.getAuthorizeStatus());
        inputMap.put(TransactionDAOConstants.TRANS_ID, txTransferTORuleMap.getBatchId() + "_" + txTransferTORuleMap.getTransId());
        inputMap.put(TransactionDAOConstants.TODAY_DT, properFormatDate.clone());
        inputMap.put(TransactionDAOConstants.TRANS_MODE, TransactionDAOConstants.TRANSFER);
        inputMap.put("ACTUAL_AMT", txTransferTORuleMap.getAmount()); // Added by Rajesh for LimitCheckingRule. No need to deduct the previous amount for Operative. Because the system should check the actual amount with Minimum Balance (on product level)
        //        inputMap.put("PARTICULARS",txTransferTORuleMap.getParticulars()); particular using passbook only
        inputMap.put("LOANPARTICULARS", loanParticulars);
        if (debitLoanType != null && debitLoanType.length() > 0) {
            inputMap.put("DEBIT_LOAN_TYPE", debitLoanType);
        } else {
            inputMap.put("DEBIT_LOAN_TYPE", loanParticulars);
        }
        //for termloan
        if (isForLoanDebitInt()) {
            boolean debit_int = true;
            inputMap.put("DEBIT_INT", new Boolean(debit_int));
        }
        if (txTransferTORuleMap.getProdType().equals(TransactionFactory.ADVANCES)) {
            inputMap.put(TransactionDAOConstants.PARTICULARS, txTransferTORuleMap.getParticulars());
        }

        if (txTransferTORuleMap.getProdType().equals(TransactionFactory.LOANS) || txTransferTORuleMap.getProdType().equals(TransactionFactory.AGRILOANS) || txTransferTORuleMap.getProdType().equals(TransactionFactory.ADVANCES) || txTransferTORuleMap.getProdType().equals(TransactionFactory.AGRIADVANCES)) {
            inputMap.put(PROD_ID, txTransferTORuleMap.getProdId());
            inputMap.put("PROD_TYPE", txTransferTORuleMap.getProdType());
        }
        inputMap.put("ACCOUNT_CLOSING", "ACCOUNT_CLOSING");
        if (act_closing_min_bal_check != null && act_closing_min_bal_check.equalsIgnoreCase("ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER")) {
            inputMap.put("ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER", "ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER");
        }
        if (depositClosingFlag == true) {
            inputMap.put("DEPOSIT_CLOSING", "DEPOSIT_CLOSING");
        }
        // For Corporate Loan purpose added by Rajesh
        if (corpLoanMap != null && corpLoanMap.size() > 0) {
            inputMap.putAll(corpLoanMap);
        }
        if (!inputMap.containsKey(TransactionDAOConstants.PARTICULARS)) {
            inputMap.put(TransactionDAOConstants.PARTICULARS, txTransferTORuleMap.getParticulars());
        }
        //added by rishad 07/01/2020 for avoiding rule screen wise
         inputMap.put("SCREEN_NAME", CommonUtil.convertObjToStr(txTransferTORuleMap.getScreenName()));
        

//        System.out.println("#$#$LOANS_PARITICULARS :" + loanParticulars + ":");
//        System.out.println("#$#$DEBIT_LOAN_TYPE :" + debitLoanType + ":");
//        System.out.println("#$#$inputMap :" + inputMap + ":");

        return inputMap;
    }

    private ArrayList setOtherBankTransactionDetails(HashMap dataMap) throws Exception {
        ArrayList transferTOs = new ArrayList();
        HashMap otherBankMap = new HashMap();
        HashMap map = new HashMap();
        String generateSingleTransId = "";
        double transAmt = 0.0;
        double paidInterest = 0;
        double paidPenalInterest = 0;
        List lst = null;
        if(dataMap.containsKey("TxTransferTO") && !dataMap.get("TxTransferTO").equals("") && dataMap.get("TxTransferTO")!=null){
        transferTOs = (ArrayList) dataMap.get("TxTransferTO");
        int size = transferTOs.size();
        System.out.println("transferTOs@#$@#$@#$@ start"+transferTOs);
        if(transferTOs!=null){
            for (int i = 0; i < size ; i++) {
                TxTransferTO txtransferTo = (TxTransferTO) transferTOs.get(i);
                TxTransferTO txtransfer = setTransactiontoTLAD(txtransferTo);
                System.out.println("txtransfer@#$@#$@#$@ "+txtransfer);
                //if (CommonUtil.convertObjToStr(txtransfer.getTransType()).equals(TransactionDAOConstants.CREDIT) && (CommonUtil.convertObjToStr(txtransfer.getProdType()).equals(TransactionFactory.OTHERBANKACTS))) {
                if ((CommonUtil.convertObjToStr(txtransfer.getProdType()).equals(TransactionFactory.OTHERBANKACTS))) {    
                lst = sqlMap.executeQueryForList("getOtherBankAcHd", CommonUtil.convertObjToStr(txtransferTo.getProdId()));
                    if (lst != null && lst.size() > 0) {
                        map = (HashMap) lst.get(0); 
                        if(dataMap.containsKey("OTHER_BANK_MAP") && dataMap.get("OTHER_BANK_MAP")!=null && !dataMap.get("OTHER_BANK_MAP").equals("")){
                        otherBankMap = (HashMap)((HashMap) dataMap.get("OTHER_BANK_MAP")).get(txtransferTo.getActNum());
                        System.out.println("otherBankMap$@#$@#$@#$@"+otherBankMap);
                        transAmt = CommonUtil.convertObjToDouble(txtransferTo.getAmount()).doubleValue();
                        if (otherBankMap.containsKey("CHARGES")) {
                            txtransferTo = (TxTransferTO) transferTOs.get(i);
                            txtransfer = new TxTransferTO();
                            txtransfer = setTransactiontoTLAD(txtransferTo);     
                            double interest = CommonUtil.convertObjToDouble(otherBankMap.get("CHARGES")).doubleValue();   
                            if (transAmt > 0 && interest > 0) {
                            if (transAmt >= interest) {
                                transAmt -= interest;
                                paidInterest = interest;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= interest;
                            }
                            txtransfer.setAmount(new Double(paidInterest));
                            txtransfer.setInpAmount(new Double(paidInterest));
                            txtransfer.setActNum("");
                            txtransfer.setProdType(TransactionFactory.GL);
                            txtransfer.setProdId("");
                            txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("CHARGE_PAID_AC_HD")));                           
                            txtransfer.setLinkBatchId(txtransferTo.getActNum()); 
                            txtransfer.setParticulars(txtransferTo.getActNum() + ":" + "CHARGES");
                            txtransfer.setAuthorizeRemarks("AB_CHARGE");
                            txtransfer.setNarration(txtransferTo.getNarration());
                            txtransfer.setInstrumentNo2("AB_CHARGE");
                            txtransfer.setTransModType(txtransferTo.getTransModType());
                            transferTOs.add(txtransfer);
                            }
                        }
                        if (otherBankMap.containsKey("PENAL_INT")) {
                            txtransferTo = (TxTransferTO) transferTOs.get(i);
                            txtransfer = new TxTransferTO();
                            txtransfer = setTransactiontoTLAD(txtransferTo);     
                            double interest = CommonUtil.convertObjToDouble(otherBankMap.get("PENAL_INT")).doubleValue();   
                            if (transAmt > 0 && interest > 0) {
                            if (transAmt >= interest) {
                                transAmt -= interest;
                                paidInterest = interest;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= interest;
                            }
                            txtransfer.setAmount(new Double(paidInterest));
                            txtransfer.setInpAmount(new Double(paidInterest));
                            txtransfer.setActNum("");
                            txtransfer.setProdType(TransactionFactory.GL);
                            txtransfer.setProdId("");
                            txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("PENAL_AC_HD")));                           
                            txtransfer.setLinkBatchId(txtransferTo.getActNum()); 
                            txtransfer.setParticulars(txtransferTo.getActNum() + ":" + "PENAL");
                            txtransfer.setAuthorizeRemarks("AB_PENAL");
                            txtransfer.setNarration(txtransferTo.getNarration());
                            txtransfer.setInstrumentNo2("AB_PENAL");
                            txtransfer.setTransModType(txtransferTo.getTransModType());
                            transferTOs.add(txtransfer);
                            System.out.println("transferTOs@#$@#$@#$@"+transferTOs);
                            }
                        }
                        if (otherBankMap.containsKey("INTEREST")) { 
                            txtransferTo = (TxTransferTO) transferTOs.get(i);
                            txtransfer = new TxTransferTO();
                            txtransfer = setTransactiontoTLAD(txtransferTo);     
                            double interest = CommonUtil.convertObjToDouble(otherBankMap.get("INTEREST")).doubleValue();   
                            System.out.println("interest@#$@#$@#$@"+interest);
                            if (transAmt > 0 && interest > 0) {
                            if (transAmt >= interest) {
                                transAmt -= interest;
                                paidInterest = interest;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= interest;
                            }
                            txtransfer.setAmount(new Double(paidInterest));
                            txtransfer.setInpAmount(new Double(paidInterest));
                            txtransfer.setActNum("");
                            txtransfer.setProdType(TransactionFactory.GL);
                            txtransfer.setProdId("");
                            if(otherBankMap.containsKey("ACCOUNT_TYPE")&& otherBankMap.get("ACCOUNT_TYPE") != null && otherBankMap.get("ACCOUNT_TYPE").equals("OD")){
                                txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("INTEREST_PAID_AC_HD"))); 
                            }else{
                            txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("INTEREST_RECEIVED_AC_HD")));     
                            }
                            txtransfer.setLinkBatchId(txtransferTo.getActNum()); 
                            txtransfer.setParticulars(txtransferTo.getActNum() + ":" + "INTEREST");
                            txtransfer.setAuthorizeRemarks("AB_INTEREST");
                            txtransfer.setNarration(txtransferTo.getNarration());
                            txtransfer.setInstrumentNo2("AB_INTEREST");
                            txtransfer.setTransModType(txtransferTo.getTransModType());
                            transferTOs.add(txtransfer);
                            System.out.println("interest@#$@#$@#$@transferTOs"+transferTOs);
                            }
                        }
                        //if (otherBankMap.containsKey("CHARGES")) {
//                            txtransfer = new TxTransferTO();
//                            txtransfer = setTransactiontoTLAD(txtransferTo);     
//                            double interest = CommonUtil.convertObjToDouble(otherBankMap.get("CHARGES")).doubleValue();  
                        System.out.println("transAmt@#$@#$@#$@transferTOs"+transAmt);
                            if (transAmt > 0) {       
                                txtransfer = (TxTransferTO) transferTOs.get(i);
                                txtransfer = setTransactiontoTLAD(txtransferTo);
                                txtransfer = new TxTransferTO();
                                txtransfer = setTransactiontoTLAD(txtransferTo);
                                txtransferTo.setAmount(new Double(transAmt));
                                txtransferTo.setInpAmount(new Double(transAmt));
                                txtransferTo.setActNum(txtransferTo.getActNum());
                                txtransferTo.setProdType(txtransferTo.getProdType());
                                txtransferTo.setProdId(txtransferTo.getProdId());
                                txtransferTo.setAcHdId(CommonUtil.convertObjToStr(map.get("PRINCIPAL_AC_HD")));                           
                                txtransferTo.setLinkBatchId(txtransferTo.getActNum()); 
                                //txtransferTo.setParticulars(txtransferTo.getActNum() + ":" + "PRINCIPAL"); // Commented by nithya on 18-08-2016 for 4999
                                txtransferTo.setAuthorizeRemarks("AB_PRINCIPAL");
                                txtransferTo.setNarration(txtransferTo.getNarration());
                                txtransferTo.setInstrumentNo2("AB_PRINCIPAL");
                                txtransferTo.setTransModType(txtransferTo.getTransModType());
                                //transferTOs.add(txtransfer);
                            } else{
                                transferTOs.remove(i);
                            }
                        //}
                    }
                }
            } 
        } 
     } 
    }
        System.out.println("transferTOs@#$@#$@#$@ final"+transferTOs);
        return transferTOs;
    }
        
    private ArrayList setTransactionDetailTLAD(HashMap dataMap) throws Exception {
        ArrayList transferTOs = new ArrayList();
        String linkBatchId = "";
        String glTransActNum = "";
        String ots = "";
        System.out.println("$#%$#%$#%$#$ dataMap: " + dataMap);
        if (dataMap.containsKey("LINK_BATCH_ID") && CommonUtil.convertObjToStr(dataMap.get("LINK_BATCH_ID")).length() > 0) {
            linkBatchId = CommonUtil.convertObjToStr(dataMap.get("LINK_BATCH_ID"));
        }
        if (dataMap.containsKey("GL_TRANS_ACT_NUM") && CommonUtil.convertObjToStr(dataMap.get("GL_TRANS_ACT_NUM")).length() > 0) {
            glTransActNum = CommonUtil.convertObjToStr(dataMap.get("GL_TRANS_ACT_NUM"));
        }
        transferTOs = (ArrayList) dataMap.get("TxTransferTO");
        System.out.println("$#%$#%$#%$#$ transferTOs: " + transferTOs);
        double paidInterest = 0, paidprincipal = 0;
        List appList = null;
        double rebateInterest = 0;
        Date rebateUptoDate = null;
        double waiveOffInterest = 0;
        boolean isTLAvailable = false;
        long no_of_installment = 0;
        boolean actClosing_Charge = true;
        String penalWaiveOff = "";
        ArrayList tempList = new ArrayList();
        ArrayList tempWaiveOffList = new ArrayList();
        ArrayList waiveOffList=new ArrayList();
        String rebateAllowed = "";
        String linkGenerateId = "";
        //added by rishad 14/03/2014 for waiving
        double waiveOffPenal = 0.0;
        double waiveOffNotice = 0.0;
        double waiveOffPrincipal = 0.0;
        double waiveOffArcCost=0.0;
        double waiveOffMiss=0.0;
        double waiveOffinsur=0.0;
        double waiveOfflegal=0.0;
        double waiveOffArbitary=0.0;
        double waiveOffDegree=0.0;
        double waiveOffEp=0.0;
        double waiveOffAdvertise=0.0;
        double waiveOffPostage=0.0;
        double waiveOffRecovery = 0.0;
        double waiveOffMeasurement = 0.0;
        double waiveOffKoleFieldExpense = 0.0;
        double waiveOffKoleFieldOperation = 0.0;
      
        String overDueIntWaiveOff = "";// for overdue interest
        double waiveOffOverDueInt = 0.0;// for overdue interest
        HashMap serviceTax_Map = new HashMap();
        String generateWaiveId = CommonUtil.convertObjToStr(generateWaiveOffBatchID());
        if (dataMap.containsKey(CommonConstants.BREAK_LOAN_HIERARCHY) && dataMap.get(CommonConstants.BREAK_LOAN_HIERARCHY) != null
                && CommonUtil.convertObjToStr(dataMap.get(CommonConstants.BREAK_LOAN_HIERARCHY)).equals("Y")) {
            return transferTOs;
        }
        if (transferTOs != null) {
            HashMap AllLoanMap = new HashMap();
            double depo_princ_amt = 0, depo_int_amt = 0, depo_add_int_amt = 0;
            if (dataMap != null && dataMap.containsKey("ALL_AMOUNT")) {
                AllLoanMap = (HashMap) dataMap.get("ALL_AMOUNT");
                if (AllLoanMap != null) {
                    depo_princ_amt = CommonUtil.convertObjToDouble(AllLoanMap.get("DEPOSIT_AMT"));
                    depo_int_amt = CommonUtil.convertObjToDouble(AllLoanMap.get("DEPOSIT_INTEREST_AMT"));
                    depo_add_int_amt = CommonUtil.convertObjToDouble(AllLoanMap.get("ADD_INT_AMOUNT"));
                }
            }
            for (int i = 0; i < transferTOs.size(); i++) {
                  double serviceTax = 0.0;
                  double swachhCess = 0.0;
                  double krishikalyanCess = 0.0;
                  double normalServiceTax = 0.0;
                TxTransferTO txtransferTo = (TxTransferTO) transferTOs.get(i);
                TxTransferTO txtransfer = setTransactiontoTLAD(txtransferTo);
                String batch = CommonUtil.convertObjToStr(txtransfer.getBatchId());
               // System.out.println("###################rish"+batch);
              //  System.out.println("batch%%%%% LTD" + batch);
                if (batch.equalsIgnoreCase("-") || batch.equals("")) {
                    //txtransfer.setSingleTransId(linkGenerateId);
                    //added by chithra
                    if (txtransfer.getTransType().equals(TransactionDAOConstants.DEBIT) && txtransfer.getProdType().equals(TransactionFactory.DEPOSITS)) {
                        double transAmt = CommonUtil.convertObjToDouble(txtransfer.getAmount());
                        if (depo_princ_amt < transAmt) {
                            txtransfer = (TxTransferTO) transferTOs.get(i);
                            txtransfer = setTransactiontoTLAD(txtransferTo);
                            double balanceAmt = transAmt - depo_princ_amt;
                            if (transAmt > 0 && depo_princ_amt > 0) {
                                if (transAmt >= depo_princ_amt) {
                                    transAmt = depo_princ_amt;
                                    paidprincipal = transAmt;
                                } else {
                                    paidprincipal = transAmt;
                                }
                                txtransferTo.setAmount(new Double(paidprincipal));
                                txtransferTo.setInpAmount(new Double(paidprincipal));
                                HashMap wherMap = new HashMap();
                                wherMap.put("PROD_ID", txtransfer.getProdId());
                                HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getDepositClosingHeads", wherMap);

                                if (balanceAmt > 0 && depo_int_amt > 0) {
                                    if (balanceAmt >= depo_int_amt) {
                                        paidInterest = depo_int_amt;
                                    } else if (balanceAmt < depo_int_amt) {
                                        paidInterest = balanceAmt;
                                    }
                                    balanceAmt = balanceAmt - depo_int_amt;
                                    txtransfer = new TxTransferTO();
                                    txtransfer = setTransactiontoTLAD(txtransferTo);
                                    txtransfer.setAmount(new Double(paidInterest));
                                    txtransfer.setInpAmount(new Double(paidInterest));
                                    txtransfer.setActNum("");
                                    txtransfer.setProdType(TransactionFactory.GL);
                                    txtransfer.setProdId("");
                                    txtransfer.setAcHdId(CommonUtil.convertObjToStr(acHeads.get("INT_PAY")));
                                    txtransfer.setParticulars(txtransferTo.getActNum() + ": Deposit " + INTEREST);
                                    txtransfer.setNarration(txtransferTo.getNarration());
                                    txtransfer.setInstrumentNo2("DEPOSIT_INTEREST");
                                    txtransfer.setAuthorizeRemarks(INTEREST);
                                    transferTOs.add(txtransfer);

                                    if (balanceAmt > 0 && depo_add_int_amt > 0) {

                                        if (balanceAmt >= depo_add_int_amt) {
                                            paidInterest = depo_add_int_amt;
                                        } else if (balanceAmt < depo_add_int_amt) {
                                            paidInterest = balanceAmt;
                                        }

                                        txtransfer = new TxTransferTO();
                                        txtransfer = setTransactiontoTLAD(txtransferTo);
                                        txtransfer.setAmount(new Double(paidInterest));
                                        txtransfer.setInpAmount(new Double(paidInterest));
                                        txtransfer.setActNum("");
                                        txtransfer.setProdType(TransactionFactory.GL);
                                        txtransfer.setProdId("");
                                        txtransfer.setAcHdId(CommonUtil.convertObjToStr(acHeads.get("INT_PAY")));
                                        txtransfer.setParticulars(txtransferTo.getActNum() + ": Deposit Additional" + INTEREST);
                                        txtransfer.setNarration(txtransferTo.getNarration());
                                        txtransfer.setInstrumentNo2("ADDITIONAL_DEPOSIT_INTEREST");
                                        txtransfer.setAuthorizeRemarks(INTEREST);
                                        transferTOs.add(txtransfer);
                                    }
                                }
                            }
                        }
                        double negativeInt = CommonUtil.convertObjToDouble(AllLoanMap.get("CURR_MONTH_INT")).doubleValue();
                        transAmt = CommonUtil.convertObjToDouble(txtransfer.getAmount());
                        if (negativeInt < 0.0) {

                            txtransfer = (TxTransferTO) transferTOs.get(i);
                            txtransfer = setTransactiontoTLAD(txtransferTo);
                            double reverseInt = negativeInt * (-1);
                            txtransferTo.setAmount(new Double(transAmt));
                            txtransferTo.setInpAmount(new Double(transAmt));
                            HashMap wherMap = new HashMap();
                            wherMap.put("PROD_ID", txtransfer.getProdId());
                            HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getDepositClosingHeads", wherMap);
                            txtransfer = new TxTransferTO();
                            txtransfer = setTransactiontoTLAD(txtransferTo);
                            txtransfer.setAmount(new Double(reverseInt));
                            txtransfer.setInpAmount(new Double(reverseInt));
                            txtransfer.setActNum("");
                            txtransfer.setProdType(TransactionFactory.GL);
                            txtransfer.setProdId("");
                            txtransfer.setAcHdId(CommonUtil.convertObjToStr(acHeads.get("INT_PAY")));
                            txtransfer.setParticulars(txtransferTo.getActNum() + ": Deposit " + INTEREST);
                            txtransfer.setNarration(txtransferTo.getNarration());
                            txtransfer.setInstrumentNo2("EXCESS INTREST");
                            txtransfer.setAuthorizeRemarks(INTEREST);
                            transferTOs.add(txtransfer);
                        }
                    }
                    //End.........  
                    if (CommonUtil.convertObjToStr(txtransfer.getTransType()).equals(TransactionDAOConstants.CREDIT) && (CommonUtil.convertObjToStr(txtransfer.getProdType()).equals(TransactionFactory.LOANS) || CommonUtil.convertObjToStr(txtransfer.getProdType()).equals(TransactionFactory.ADVANCES))) {
                       
                        HashMap map = new HashMap();
                        HashMap ALL_LOAN_AMOUNT = new HashMap();
                        HashMap waivemap = new HashMap();
                        if (dataMap.containsKey("MULTIPLE_ALL_AMOUNT") && dataMap.get("MULTIPLE_ALL_AMOUNT") != null) {
                            ALL_LOAN_AMOUNT = (HashMap) ((HashMap) dataMap.get("MULTIPLE_ALL_AMOUNT")).get(txtransfer.getActNum());
                            waivemap = (HashMap) dataMap.get("MULTIPLE_ALL_AMOUNT");
                        } else {
                            ALL_LOAN_AMOUNT = (HashMap) dataMap.get("ALL_AMOUNT");
                            waivemap = (HashMap) dataMap.get("ALL_AMOUNT");
                        }
                        if (ALL_LOAN_AMOUNT == null || ALL_LOAN_AMOUNT.isEmpty()) {
                            //System.out.println("#@#@#@#@#@#@#@#@3   ALL_LOAN_AMOUNT : "+ALL_LOAN_AMOUNT);
                            //return transferTOs;//Commented By Revathi.L reff By Mr.Abi
                            continue;
                        }
                        if (ALL_LOAN_AMOUNT.containsKey("REBATE_INTEREST") && CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("REBATE_INTEREST")).doubleValue() > 0) {
                            rebateInterest = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("REBATE_INTEREST")).doubleValue();
                        }
                        if (ALL_LOAN_AMOUNT != null && ALL_LOAN_AMOUNT.containsKey("REBATE_INTEREST_UPTO") && !CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("REBATE_INTEREST_UPTO")).equals("")) {
                            rebateUptoDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(ALL_LOAN_AMOUNT.get("REBATE_INTEREST_UPTO")));
                        }
                        if (waivemap!= null && waivemap.containsKey("INTEREST_WAIVE_AMT") && CommonUtil.convertObjToDouble(waivemap.get("INTEREST_WAIVE_AMT")).doubleValue() > 0) {
                            waiveOffInterest = CommonUtil.convertObjToDouble(waivemap.get("INTEREST_WAIVE_AMT")).doubleValue();
                        }
                        if (waivemap.containsKey("PENAL_WAIVE_OFF") && CommonUtil.convertObjToStr(waivemap.get("PENAL_WAIVE_OFF")).equals("Y")) {
                            penalWaiveOff = "Y";
                            //  waiveOffPenal = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("PENAL_WAIVE_AMT"));
                            waiveOffPenal = CommonUtil.convertObjToDouble(waivemap.get("PENAL_WAIVE_AMT"));
                        } else {
                            penalWaiveOff = "N";
                          }
                        
                        // Added by nithya on 15-03-2018 for 
                        if (waivemap != null && waivemap.containsKey("OVERDUEINT_WAIVE_OFF") && CommonUtil.convertObjToStr(waivemap.get("OVERDUEINT_WAIVE_OFF")).equals("Y")) {
                            overDueIntWaiveOff = "Y";
                            waiveOffOverDueInt = CommonUtil.convertObjToDouble(waivemap.get("OVERDUEINT_WAIVE_AMT"));
                        } else {
                            overDueIntWaiveOff = "N";
                        }
                        // End
                        
                        //added rishad 05/01/2017  for servise tax and cess calulation purpose
                        if (ALL_LOAN_AMOUNT != null && ALL_LOAN_AMOUNT.containsKey("SER_TAX_MAP") && ALL_LOAN_AMOUNT.get("SER_TAX_MAP") != null) {
                            serviceTax_Map = (HashMap) ALL_LOAN_AMOUNT.get("SER_TAX_MAP");
                            if (serviceTax_Map.containsKey("TOT_TAX_AMT") && serviceTax_Map.get("TOT_TAX_AMT") != null) {
                                serviceTax = CommonUtil.convertObjToDouble(serviceTax_Map.get("TOT_TAX_AMT"));
                            }
                            if (serviceTax_Map.containsKey(ServiceTaxCalculation.SWACHH_CESS) && serviceTax_Map.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                swachhCess = CommonUtil.convertObjToDouble(serviceTax_Map.get(ServiceTaxCalculation.SWACHH_CESS));
                            }
                            if (serviceTax_Map.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && serviceTax_Map.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                krishikalyanCess = CommonUtil.convertObjToDouble(serviceTax_Map.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                            }
                            if (serviceTax_Map.containsKey(ServiceTaxCalculation.SERVICE_TAX) && serviceTax_Map.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                normalServiceTax = CommonUtil.convertObjToDouble(serviceTax_Map.get(ServiceTaxCalculation.SERVICE_TAX));
                            }
                        }
                        
                        //added by rishad 
                        if (waivemap != null && waivemap.containsKey("NOTICE_WAIVE_OFF") && CommonUtil.convertObjToStr(waivemap.get("NOTICE_WAIVE_OFF")).equals("Y")) {
                            waiveOffNotice = CommonUtil.convertObjToDouble(waivemap.get("NOTICE_WAIVE_AMT"));
                        }
                        if (waivemap != null && waivemap.containsKey("PRINCIPAL_WAIVE_OFF") && CommonUtil.convertObjToStr(waivemap.get("PRINCIPAL_WAIVE_OFF")).equals("Y")) {
                            waiveOffPrincipal = CommonUtil.convertObjToDouble(waivemap.get("PRINCIPAL_WAIVE_AMT"));
                        }
                        if (waivemap != null && waivemap.containsKey("ARC_WAIVE_OFF") && CommonUtil.convertObjToStr(waivemap.get("ARC_WAIVE_OFF")).equals("Y")) {
                            waiveOffArcCost = CommonUtil.convertObjToDouble(waivemap.get("ARC_WAIVE_AMT"));
                        }
                        if (waivemap != null && waivemap.containsKey("POSTAGE_WAIVE_OFF") && CommonUtil.convertObjToStr(waivemap.get("POSTAGE_WAIVE_OFF")).equals("Y")) {
                            waiveOffPostage = CommonUtil.convertObjToDouble(waivemap.get("POSTAGE_WAIVE_AMT"));
                        }
                        if (waivemap != null && waivemap.containsKey("RECOVERY_WAIVE_OFF") && CommonUtil.convertObjToStr(waivemap.get("RECOVERY_WAIVE_OFF")).equals("Y")) {
                            waiveOffRecovery = CommonUtil.convertObjToDouble(waivemap.get("RECOVERY_WAIVE_AMT"));
                        }
                        if (waivemap != null && waivemap.containsKey("MEASUREMENT_WAIVE_OFF") && CommonUtil.convertObjToStr(waivemap.get("MEASUREMENT_WAIVE_OFF")).equals("Y")) {
                            waiveOffMeasurement = CommonUtil.convertObjToDouble(waivemap.get("MEASUREMENT_WAIVE_AMT"));
                        }
                        
                        if (ALL_LOAN_AMOUNT != null && ALL_LOAN_AMOUNT.containsKey("KOLE_FIELD_EXPENSE_WAIVE_OFF") && ALL_LOAN_AMOUNT.get("KOLE_FIELD_EXPENSE_WAIVE_OFF") != null && CommonUtil.convertObjToStr(ALL_LOAN_AMOUNT.get("KOLE_FIELD_EXPENSE_WAIVE_OFF")).equals("Y")) {
                            waiveOffKoleFieldExpense = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("KOLE_FIELD_EXPENSE_WAIVE_AMT"));
                        }

                        if (ALL_LOAN_AMOUNT != null && ALL_LOAN_AMOUNT.containsKey("KOLE_FIELD_OPERATION_WAIVE_OFF") && ALL_LOAN_AMOUNT.get("KOLE_FIELD_OPERATION_WAIVE_OFF") != null && CommonUtil.convertObjToStr(ALL_LOAN_AMOUNT.get("KOLE_FIELD_OPERATION_WAIVE_OFF")).equals("Y")) {
                            waiveOffKoleFieldOperation = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("KOLE_FIELD_OPERATION_WAIVE_AMT"));
                        }
                        
                        if (waivemap != null && waivemap.containsKey("MISCELLANEOUS_WAIVE_OFF") && CommonUtil.convertObjToStr(waivemap.get("MISCELLANEOUS_WAIVE_OFF")).equals("Y")) {
                            waiveOffMiss = CommonUtil.convertObjToDouble(waivemap.get("MISCELLANEOUS_WAIVE_AMT"));
                        }
                        if (waivemap != null && waivemap.containsKey("LEGAL_WAIVE_OFF") && CommonUtil.convertObjToStr(waivemap.get("LEGAL_WAIVE_OFF")).equals("Y")) {
                            waiveOfflegal = CommonUtil.convertObjToDouble(waivemap.get("LEGAL_WAIVE_AMT"));
                        }
                        if (waivemap != null && waivemap.containsKey("ADVERTISE_WAIVE_OFF") && CommonUtil.convertObjToStr(waivemap.get("ADVERTISE_WAIVE_OFF")).equals("Y")) {
                            waiveOffAdvertise = CommonUtil.convertObjToDouble(waivemap.get("ADVERTISE_WAIVE_AMT"));
                        }
                        if (waivemap != null && waivemap.containsKey("EP_WAIVE_OFF") && CommonUtil.convertObjToStr(waivemap.get("EP_WAIVE_OFF")).equals("Y")) {
                            waiveOffEp = CommonUtil.convertObjToDouble(waivemap.get("EP_WAIVE_AMT"));
                        }
                        if (waivemap != null && waivemap.containsKey("DECREE_WAIVE_OFF") && CommonUtil.convertObjToStr(waivemap.get("DECREE_WAIVE_OFF")).equals("Y")) {
                            waiveOffDegree = CommonUtil.convertObjToDouble(waivemap.get("DECREE_WAIVE_AMT"));
                        }
                        if (waivemap != null && waivemap.containsKey("ARBITRAY_WAIVE_OFF") && CommonUtil.convertObjToStr(waivemap.get("ARBITRAY_WAIVE_OFF")).equals("Y")) {
                            waiveOffArbitary = CommonUtil.convertObjToDouble(waivemap.get("ARBITRAY_WAIVE_AMT"));
                        }
                        if (waivemap != null && waivemap.containsKey("INSURENCE_WAIVE_OFF") && CommonUtil.convertObjToStr(waivemap.get("INSURENCE_WAIVE_OFF")).equals("Y")) {
                            waiveOffinsur = CommonUtil.convertObjToDouble(waivemap.get("INSURENCE_WAIVE_AMT"));
                        }
                        if (dataMap.containsKey("REBATE_INTEREST") && CommonUtil.convertObjToStr(dataMap.get("REBATE_INTEREST")).equals("Y")) {
                            rebateAllowed = "Y";
                        } else {
                            rebateAllowed = "N";
                        }
                        map.put(CommonConstants.ACT_NUM, txtransfer.getActNum());
                        map.put(PROD_ID, txtransfer.getProdId());
                        List lst = null;
                        map.put("TRANS_DT", currDate.clone());
                        map.put("INITIATED_BRANCH", _branchCode);
                        if (txtransfer.getProdType().equals("TL")) {
                            lst = sqlMap.executeQueryForList("IntCalculationDetail", map);
                        }
                        if (txtransfer.getProdType().equals(TransactionFactory.ADVANCES)) {
                            lst = sqlMap.executeQueryForList("IntCalculationDetailAD", map);
                        }
                        if (lst != null && lst.size() > 0) {
                            map = (HashMap) lst.get(0);
                            ots = CommonUtil.convertObjToStr(map.get("OTS"));
                        }

//                    getDateMap.put(BEHAVES_LIKE,behaves.get(BEHAVES_LIKE));
                           //if condition modified by chithra for mantis 10401: in advances acct charges is not shown in the cash screen
                        if ((txtransfer.getProdType()!=null && txtransfer.getProdType().equals(TransactionFactory.ADVANCES)) ||(map.containsKey("AS_CUSTOMER_COMES") && map.get("AS_CUSTOMER_COMES") != null && map.get("AS_CUSTOMER_COMES").equals("Y"))) {
                            map.put(PROD_ID, txtransfer.getProdId());
                            map.put(CommonConstants.ACT_NUM, txtransfer.getActNum());
                            if (ALL_LOAN_AMOUNT == null || ALL_LOAN_AMOUNT.isEmpty()) {
                                ALL_LOAN_AMOUNT = new HashMap();
                                if (dataMap.containsKey("EMI_INSTALLMENT")) {
                                    map.put("NO_OF_INSTALLMENT", dataMap.get("EMI_INSTALLMENT"));
                                }
                                ALL_LOAN_AMOUNT = (HashMap) asAnWhenCustomerTransCreation(map);
                               // System.out.println("ALL_LOAN_AMOUNT####" + ALL_LOAN_AMOUNT);
                            }
                            if (ALL_LOAN_AMOUNT.containsKey("NO_OF_INSTALLMENT") && ALL_LOAN_AMOUNT.get("NO_OF_INSTALLMENT") != null) {
                                no_of_installment = CommonUtil.convertObjToLong(ALL_LOAN_AMOUNT.get("NO_OF_INSTALLMENT"));
                            }

//                        if(map.containsKey("AS_CUSTOMER_COMES") && map.get("AS_CUSTOMER_COMES")!=null &&  map.get("AS_CUSTOMER_COMES").equals("Y")){
                            map.put(PROD_ID, txtransfer.getProdId());
                            map.put("ACCT_NUM", txtransfer.getActNum());
                            System.out.println(map);
                            lst = sqlMap.executeQueryForList("getInterestAndPenalIntActHead", map);
                            if (lst != null && lst.size() > 0) {
                                map = (HashMap) lst.get(0);
                            }
                            double transAmt = txtransfer.getAmount().doubleValue();
//                            System.out.println("all loan amount####" + ALL_LOAN_AMOUNT);
//                            System.out.println("$@@#$@#@$##### transAmt: " + transAmt);
//                        //account closing charges
//                        double accountClosing=CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("ACT_CLOSING_CHARGE")).doubleValue();
//                        if(transAmt>0 && accountClosing>0 ){
//                            System.out.println("INSIDE accountClosing"+accountClosing+"transAmt"+transAmt);
//                            if( transAmt>=accountClosing){
//                                transAmt-=accountClosing;
//                                paidInterest=accountClosing;
//                            }else{
//                                paidInterest=transAmt;
//                                transAmt-=accountClosing;
//                                
//                            }
//                            txtransfer.setAmount(new Double(paidInterest));
//                            txtransfer.setInpAmount(new Double(paidInterest));
//                            txtransfer.setActNum("");
//                            txtransfer.setProdType(TransactionFactory.GL);
//                            txtransfer.setProdId("");
//                            txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("AC_CLOSING_CHRG")));
//                            txtransfer.setLinkBatchId(txtransferTo.getActNum());
//                            txtransfer.setParticulars(txtransferTo.getActNum()+":"+"ACT CLOSING CHARGE");
//                            txtransfer.setAuthorizeRemarks("ACT CLOSING CHARGE");
//                            transferTOs.add(txtransfer);
//                        }
//                        paidInterest=0;
                            txtransferTo = (TxTransferTO) transferTOs.get(i);
                            txtransfer = new TxTransferTO();
                            txtransfer = setTransactiontoTLAD(txtransferTo);
                            if (ots.equals("Y")) {
                                appList = sqlMap.executeQueryForList("selectAppropriatTransaction_OTS", map.get("PROD_ID"));
                            } else {
                                appList = sqlMap.executeQueryForList("selectAppropriatTransaction", map.get("PROD_ID"));
                            }

                            HashMap appropriateMap = new HashMap();
                            if (appList != null && appList.size() > 0) {
                                appropriateMap = (HashMap) appList.get(0);
                                appropriateMap.remove("PROD_ID");
                            } else {
                                throw new TTException("Please Enter Hierachy of Transaction  in This Product ");
                            }
                            System.out.println("appropriateMap####" + appropriateMap);
                            java.util.Collection collectedValues = appropriateMap.values();
                            java.util.Iterator it = collectedValues.iterator();
                            int hierarchyLevel = 0;
                            while (it.hasNext()) {
                                hierarchyLevel++;
                                String hierachyValue = CommonUtil.convertObjToStr(it.next());
                                System.out.println("hierachyValue####" + hierachyValue);
                                //                              objCashTO = setCashTransaction(objCashTransactionTO);
                                if (hierachyValue.equals("CHARGES")) {
                                    //Account Closing Charges
                                    if (ALL_LOAN_AMOUNT.containsKey("ACT_CLOSING_CHARGES") && actClosing_Charge == true) {
                                        List chargeLst = (List) ALL_LOAN_AMOUNT.get("ACT_CLOSING_CHARGES");
                                        if (chargeLst != null && chargeLst.size() > 0) {
                                            System.out.println("@##$#$% chargeLst #### :" + chargeLst);
                                            for (int k = 0; k < chargeLst.size(); k++) {
                                                txtransfer = setTransactiontoTLAD(txtransferTo);
                                                HashMap chargeMap = new HashMap();
                                                String accHead = "";
                                                double chargeAmt = 0;
                                                chargeMap = (HashMap) chargeLst.get(k);
                                                accHead = CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"));
                                                chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")).doubleValue();
                                                System.out.println("$#@@$ accHead" + accHead);
                                                System.out.println("$#@@$ chargeAmt" + chargeAmt);
                                                if (transAmt > 0 && chargeAmt > 0) {
                                                    if (transAmt >= chargeAmt) {
                                                        transAmt -= chargeAmt;
                                                        paidInterest = chargeAmt;
                                                    } else {
                                                        paidInterest = transAmt;
                                                        transAmt -= chargeAmt;
                                                    }
                                                    txtransfer.setAmount(new Double(paidInterest));
                                                    txtransfer.setInpAmount(new Double(paidInterest));
                                                    txtransfer.setActNum("");
                                                    txtransfer.setProdType(TransactionFactory.GL);
                                                    txtransfer.setProdId("");
                                                    txtransfer.setAcHdId(CommonUtil.convertObjToStr(accHead));
                                                    if (linkBatchId.length() > 0) {
                                                        txtransfer.setLinkBatchId(linkBatchId);
                                                    } else if(txtransferTo.getActNum()!=null && txtransferTo.getActNum().length()>0){
                                                        txtransfer.setLinkBatchId(txtransferTo.getActNum());
                                                    }
                                                    txtransfer.setGlTransActNum(glTransActNum);
                                                    //txtransfer.setParticulars(txtransferTo.getActNum() + ":" + "ACT CLOSING CHARGE");
                                                    txtransfer.setParticulars(txtransferTo.getActNum() + ":" +chargeMap.get("CHARGE_DESC"));
                                                    txtransfer.setAuthorizeRemarks("ACT CLOSING CHARGE");
                                                    txtransfer.setNarration(txtransferTo.getNarration());
                                                    txtransfer.setInstrumentNo2("LOAN_ACT_CLOSING_CHARGE");
                                                    txtransfer.setHierarchyLevel(String.valueOf(hierarchyLevel));
                                                    txtransfer.setTransModType(txtransferTo.getTransModType());
                                                    transferTOs.add(txtransfer);
                                                }
                                                paidInterest = 0;
                                                chargeAmt = 0;
                                            }
                                            actClosing_Charge = false;
                                        }
                                    }
                                    if (swachhCess > 0) {
                                        txtransferTo = (TxTransferTO) transferTOs.get(i);
                                        txtransfer = new TxTransferTO();
                                        txtransfer = setTransactiontoTLAD(txtransferTo);
                                        if (transAmt > 0 && swachhCess > 0) {
                                            if (transAmt >= swachhCess) {
                                                transAmt -= swachhCess;
                                                serviceTax -= swachhCess;
                                                paidInterest = swachhCess;
                                            } else {
                                                paidInterest = transAmt;
                                                transAmt -= swachhCess;
                                                serviceTax -= swachhCess;

                                            }
                                            txtransfer.setAmount(new Double(paidInterest));
                                            txtransfer.setInpAmount(new Double(paidInterest));
                                            txtransfer.setActNum("");
                                            txtransfer.setProdType(TransactionFactory.GL);
                                            txtransfer.setProdId("");
                                            txtransfer.setAcHdId(CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.SWACHH_HEAD_ID)));
                                            if (linkBatchId.length() > 0) {
                                                txtransfer.setLinkBatchId(linkBatchId);
                                            } else if (txtransferTo.getActNum() != null && txtransferTo.getActNum().length() > 0) {
                                                txtransfer.setLinkBatchId(txtransferTo.getActNum());
                                            }
                                            txtransfer.setGlTransActNum(glTransActNum);
                                            txtransfer.setParticulars(txtransferTo.getActNum() + ":" + "CGST");
                                            // txtransfer.setAuthorizeRemarks("SERVICE TAX CHARGE");
                                            txtransfer.setAuthorizeRemarks("MISCELLANEOUS CHARGES");
                                            txtransfer.setNarration(txtransferTo.getNarration());
                                            txtransfer.setHierarchyLevel(String.valueOf(hierarchyLevel));
                                            txtransfer.setTransModType(txtransferTo.getTransModType());
                                            txtransfer.setInstrumentNo2(CommonConstants.SERVICE_TAX_CHRG);
                                            transferTOs.add(txtransfer);
                                        }
                                        paidInterest = 0;
                                    }
                                    if (krishikalyanCess > 0) {
                                        txtransferTo = (TxTransferTO) transferTOs.get(i);
                                        txtransfer = new TxTransferTO();
                                        txtransfer = setTransactiontoTLAD(txtransferTo);
                                        if (transAmt > 0 && krishikalyanCess > 0) {
                                            if (transAmt >= krishikalyanCess) {
                                                transAmt -= krishikalyanCess;
                                                serviceTax -= krishikalyanCess;
                                                paidInterest = krishikalyanCess;
                                            } else {
                                                paidInterest = transAmt;
                                                transAmt -= krishikalyanCess;
                                                serviceTax -= krishikalyanCess;

                                            }
                                            txtransfer.setAmount(new Double(paidInterest));
                                            txtransfer.setInpAmount(new Double(paidInterest));
                                            txtransfer.setActNum("");
                                            txtransfer.setProdType(TransactionFactory.GL);
                                            txtransfer.setProdId("");
                                            txtransfer.setAcHdId(CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.KRISHIKALYAN_HEAD_ID)));
                                            if (linkBatchId.length() > 0) {
                                                txtransfer.setLinkBatchId(linkBatchId);
                                            } else if (txtransferTo.getActNum() != null && txtransferTo.getActNum().length() > 0) {
                                                txtransfer.setLinkBatchId(txtransferTo.getActNum());
                                            }
                                            txtransfer.setGlTransActNum(glTransActNum);
                                            txtransfer.setParticulars(txtransferTo.getActNum() + ":" + "SGST");
                                            txtransfer.setAuthorizeRemarks("MISCELLANEOUS CHARGES");
                                            txtransfer.setNarration(txtransferTo.getNarration());
                                            txtransfer.setHierarchyLevel(String.valueOf(hierarchyLevel));
                                            txtransfer.setTransModType(txtransferTo.getTransModType());
                                            txtransfer.setInstrumentNo2(CommonConstants.SERVICE_TAX_CHRG);
                                            transferTOs.add(txtransfer);
                                        }
                                        paidInterest = 0;
                                    }
                                    //added by chithra for service Tax
                                    if (normalServiceTax > 0) {
                                        txtransferTo = (TxTransferTO) transferTOs.get(i);
                                        txtransfer = new TxTransferTO();
                                        txtransfer = setTransactiontoTLAD(txtransferTo);
                                        //   double serTaxAmt = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("TOT_SER_TAX_AMT")).doubleValue();
                                        if (transAmt > 0 && normalServiceTax > 0) {
//                                            if (transAmt >= serviceTax) {
//                                                transAmt -= serviceTax;
//                                                paidInterest = serviceTax;
//                                            } else {
//                                                paidInterest = transAmt;
//                                                transAmt -= serviceTax;
//
//                                            }
                                            if (transAmt >= normalServiceTax) {
                                                transAmt -= normalServiceTax;
                                                paidInterest = normalServiceTax;
                                            } else {
                                                paidInterest = transAmt;
                                                transAmt -= normalServiceTax;

                                            }
                                            txtransfer.setAmount(new Double(paidInterest));
                                            txtransfer.setInpAmount(new Double(paidInterest));
                                            txtransfer.setActNum("");
                                            txtransfer.setProdType(TransactionFactory.GL);
                                            txtransfer.setProdId("");
                                            txtransfer.setAcHdId(CommonUtil.convertObjToStr(ALL_LOAN_AMOUNT.get("SER_TAX_HEAD")));
                                            if (linkBatchId.length() > 0) {
                                                txtransfer.setLinkBatchId(linkBatchId);
                                            } else  if(txtransferTo.getActNum()!=null && txtransferTo.getActNum().length()>0){
                                                txtransfer.setLinkBatchId(txtransferTo.getActNum());
                                            }
                                            txtransfer.setGlTransActNum(glTransActNum);
                                            txtransfer.setParticulars(txtransferTo.getActNum() + ":" + "SERVICE TAX CHARGE");
                                           // txtransfer.setAuthorizeRemarks("SERVICE TAX CHARGE");
                                            txtransfer.setAuthorizeRemarks("MISCELLANEOUS CHARGES");
                                            txtransfer.setNarration(txtransferTo.getNarration());
                                            txtransfer.setInstrumentNo2("SERVICE TAX CHARGE");
                                            txtransfer.setHierarchyLevel(String.valueOf(hierarchyLevel));
                                            txtransfer.setTransModType(txtransferTo.getTransModType());
                                            txtransfer.setInstrumentNo2(CommonConstants.SERVICE_TAX_CHRG);
                                            transferTOs.add(txtransfer);
                                        }
                                        paidInterest = 0;
                                    }
                                    //added by rishad 14/06/2015 0010751: Transfer Tally Issue--service tax case
                                    txtransferTo = (TxTransferTO) transferTOs.get(i);
                                    txtransfer = new TxTransferTO();
                                    txtransfer = setTransactiontoTLAD(txtransferTo);
                                    //end 
                                    double epchg = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("EP_COST")).doubleValue();
                                    if (epchg > 0 && waiveOffEp > 0) {
                                        transferTOs = commonWaiveOff(transferTOs, txtransferTo, waiveOffEp, generateWaiveId, "EP_WAIVE_OFF");
                                        epchg -= waiveOffEp;
                                    }
                                    if (transAmt > 0 && epchg > 0) {
                                        if (transAmt >= epchg) {
                                            transAmt -= epchg;
                                            paidInterest = epchg;
                                        } else {
                                            paidInterest = transAmt;
                                            transAmt -= epchg;
                                        }
                                        txtransfer.setAmount(new Double(paidInterest));
                                        txtransfer.setInpAmount(new Double(paidInterest));
                                        txtransfer.setActNum("");
                                        txtransfer.setProdType(TransactionFactory.GL);
                                        txtransfer.setProdId("");
                                        txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("EP COST")));
                                        if (linkBatchId.length() > 0) {
                                            txtransfer.setLinkBatchId(linkBatchId);
                                        } else  if(txtransferTo.getActNum()!=null && txtransferTo.getActNum().length()>0){
                                            txtransfer.setLinkBatchId(txtransferTo.getActNum());
                                        }
                                        txtransfer.setGlTransActNum(glTransActNum);
                                        txtransfer.setParticulars(txtransferTo.getActNum() + ":" + "EP CHARGE");
                                        txtransfer.setAuthorizeRemarks("EP_COST");
                                        txtransfer.setNarration(txtransferTo.getNarration());
                                        txtransfer.setInstrumentNo2("LOAN_EP_CHARGE");
                                        txtransfer.setHierarchyLevel(String.valueOf(hierarchyLevel));
                                        txtransfer.setTransModType(txtransferTo.getTransModType());
                                        transferTOs.add(txtransfer);
                                    }
                                    paidInterest = 0;
                                    txtransferTo = (TxTransferTO) transferTOs.get(i);
                                    txtransfer = new TxTransferTO();
                                    txtransfer = setTransactiontoTLAD(txtransferTo);
                                    double arcchg = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("ARC_COST")).doubleValue();
                                    if (arcchg > 0 && waiveOffArcCost > 0) {
                                        transferTOs=arcCostWaiveOff(transferTOs,txtransferTo, waiveOffArcCost, generateWaiveId);
                                       // tempWaiveOffList.add(waiveOffList);
                                       arcchg -= waiveOffArcCost;
                                        //  continue;
                                    }
                                    if (transAmt > 0 && arcchg > 0) {
                                        if (transAmt >= arcchg) {
                                            transAmt -= arcchg;
                                            paidInterest = arcchg;
                                        } else {
                                            paidInterest = transAmt;
                                            transAmt -= arcchg;
                                        }
                                        txtransfer.setAmount(new Double(paidInterest));
                                        txtransfer.setInpAmount(new Double(paidInterest));
                                        txtransfer.setActNum("");
                                        txtransfer.setProdType(TransactionFactory.GL);
                                        txtransfer.setProdId("");
                                        txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("ARC COST")));
                                        if (linkBatchId.length() > 0) {
                                            txtransfer.setLinkBatchId(linkBatchId);
                                        } else  if(txtransferTo.getActNum()!=null&&txtransferTo.getActNum().length()>0){
                                            txtransfer.setLinkBatchId(txtransferTo.getActNum());
                                        }
                                        txtransfer.setGlTransActNum(glTransActNum);
                                        txtransfer.setParticulars(txtransferTo.getActNum() + ":" + "ARC CHARGE");
                                        txtransfer.setAuthorizeRemarks("ARC_COST");
                                        txtransfer.setNarration(txtransferTo.getNarration());
                                        txtransfer.setInstrumentNo2("LOAN_ARC_CHARGE");
                                        txtransfer.setHierarchyLevel(String.valueOf(hierarchyLevel));
                                        txtransfer.setTransModType(txtransferTo.getTransModType());
                                        transferTOs.add(txtransfer);
                                    }
                                    paidInterest = 0;
                                    txtransferTo = (TxTransferTO) transferTOs.get(i);
                                    txtransfer = new TxTransferTO();
                                    txtransfer = setTransactiontoTLAD(txtransferTo);
                                    //account closing misc charges
                                    double actclosingmisc = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("ACT_CLOSING_MISC_CHARGE")).doubleValue();
                                    if (transAmt > 0 && actclosingmisc > 0) {
                                        if (transAmt >= actclosingmisc) {
                                            transAmt -= actclosingmisc;
                                            paidInterest = actclosingmisc;
                                        } else {
                                            paidInterest = transAmt;
                                            transAmt -= actclosingmisc;

                                        }
                                        txtransfer.setAmount(new Double(paidInterest));
                                        txtransfer.setInpAmount(new Double(paidInterest));
                                        txtransfer.setActNum("");
                                        txtransfer.setProdType(TransactionFactory.GL);
                                        txtransfer.setProdId("");
                                        txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("MISC_SERV_CHRG")));
                                        if (linkBatchId.length() > 0) {
                                            txtransfer.setLinkBatchId(linkBatchId);
                                        } else  if(txtransferTo.getActNum()!=null&&txtransferTo.getActNum().length()>0){
                                            txtransfer.setLinkBatchId(txtransferTo.getActNum());
                                        }
                                        txtransfer.setGlTransActNum(glTransActNum);
                                        txtransfer.setParticulars(txtransferTo.getActNum() + ":" + "ACT CLOSING MISC CHARGE");
                                        txtransfer.setAuthorizeRemarks("ACT CLOSING MISC CHARGE");
                                        txtransfer.setNarration(txtransferTo.getNarration());
                                        txtransfer.setInstrumentNo2("LOAN_MISC_SERV_CHRG");
                                        txtransfer.setHierarchyLevel(String.valueOf(hierarchyLevel));
                                        txtransfer.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
                                        transferTOs.add(txtransfer);
                                    }
                                    paidInterest = 0;
                                    txtransferTo = (TxTransferTO) transferTOs.get(i);
                                    txtransfer = new TxTransferTO();
                                    txtransfer = setTransactiontoTLAD(txtransferTo);
                                    //postage charges
                                    double postageCharges = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("POSTAGE CHARGES")).doubleValue();
                                       if (postageCharges> 0 && waiveOffPostage> 0) {
                                        transferTOs=commonWaiveOff(transferTOs,txtransferTo, waiveOffPostage, generateWaiveId,"POSTAGE_WAIVE_OFF");
                                       postageCharges-= waiveOffPostage;
                                    }
                                    
                                    if (transAmt > 0 && postageCharges > 0) {
                                        if (transAmt >= postageCharges) {
                                            transAmt -= postageCharges;
                                            paidInterest = postageCharges;
                                        } else {
                                            paidInterest = transAmt;
                                            transAmt -= postageCharges;

                                        }
                                        txtransfer.setAmount(new Double(paidInterest));
                                        txtransfer.setInpAmount(new Double(paidInterest));
                                        txtransfer.setActNum("");
                                        txtransfer.setProdType(TransactionFactory.GL);
                                        txtransfer.setProdId("");
                                        txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("POSTAGE_CHARGES")));
                                        if (linkBatchId.length() > 0) {
                                            txtransfer.setLinkBatchId(linkBatchId);
                                        } else  if(txtransferTo.getActNum()!=null && txtransferTo.getActNum().length()>0){
                                            txtransfer.setLinkBatchId(txtransferTo.getActNum());
                                        }
                                        txtransfer.setGlTransActNum(glTransActNum);
                                        txtransfer.setParticulars(txtransferTo.getActNum() + ":" + "POSTAGE_CHARGES");
                                        txtransfer.setAuthorizeRemarks("POSTAGE CHARGES");
                                        txtransfer.setNarration(txtransferTo.getNarration());
                                        txtransfer.setInstrumentNo2("LOAN_POSTAGE_CHARGES");
                                        txtransfer.setHierarchyLevel(String.valueOf(hierarchyLevel));
                                        txtransfer.setTransModType(txtransferTo.getTransModType());
                                        transferTOs.add(txtransfer);
                                    }
                                    paidInterest = 0;

                                    //Advertise charges
                                    txtransferTo = (TxTransferTO) transferTOs.get(i);
                                    txtransfer = new TxTransferTO();
                                    txtransfer = setTransactiontoTLAD(txtransferTo);
                                    double advertiseCharges = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("ADVERTISE CHARGES")).doubleValue();
                                    if (advertiseCharges> 0 && waiveOffAdvertise> 0) {
                                        transferTOs=commonWaiveOff(transferTOs,txtransferTo, waiveOffAdvertise, generateWaiveId,"ADVERTISE_WAIVE_OFF");
                                       advertiseCharges-= waiveOffAdvertise;
                                    }
                                    if (transAmt > 0 && advertiseCharges > 0) {
                                        if (transAmt >= advertiseCharges) {
                                            transAmt -= advertiseCharges;
                                            paidInterest = advertiseCharges;
                                        } else {
                                            paidInterest = transAmt;
                                            transAmt -= advertiseCharges;

                                        }
                                        txtransfer.setAmount(new Double(paidInterest));
                                        txtransfer.setInpAmount(new Double(paidInterest));
                                        txtransfer.setActNum("");
                                        txtransfer.setProdType(TransactionFactory.GL);
                                        txtransfer.setProdId("");
                                        txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("ADVERTISE_ACHEAD")));
                                        if (linkBatchId.length() > 0) {
                                            txtransfer.setLinkBatchId(linkBatchId);
                                        } else  if(txtransferTo.getActNum()!=null && txtransferTo.getActNum().length()>0){
                                            txtransfer.setLinkBatchId(txtransferTo.getActNum());
                                        }
                                        txtransfer.setGlTransActNum(glTransActNum);
                                        txtransfer.setParticulars(txtransferTo.getActNum() + ":" + "ADVERTISE_CHARGES");
                                        txtransfer.setAuthorizeRemarks("ADVERTISE CHARGES");
                                        txtransfer.setNarration(txtransferTo.getNarration());
                                        txtransfer.setInstrumentNo2("LOAN_ADVERTISE_CHARGES");
                                        txtransfer.setHierarchyLevel(String.valueOf(hierarchyLevel));
                                        txtransfer.setTransModType(txtransferTo.getTransModType());
                                        transferTOs.add(txtransfer);
                                    }
                                    paidInterest = 0;
                                    
                                    //Recovery Charges starts
                                    
                                    if (ALL_LOAN_AMOUNT.containsKey("RECOVERY CHARGES") && ALL_LOAN_AMOUNT.get("RECOVERY CHARGES") != null) {
                                        txtransferTo = (TxTransferTO) transferTOs.get(i);
                                        txtransfer = new TxTransferTO();
                                        txtransfer = setTransactiontoTLAD(txtransferTo);
                                        double recoveryCharges = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("RECOVERY CHARGES")).doubleValue();
                                        if (recoveryCharges > 0 && waiveOffRecovery > 0) {
                                            transferTOs = commonWaiveOff(transferTOs, txtransferTo, waiveOffRecovery, generateWaiveId, "RECOVERY_WAIVE_OFF");
                                            recoveryCharges -= waiveOffRecovery;
                                        }
                                        if (transAmt > 0 && recoveryCharges > 0) {
                                            if (transAmt >= recoveryCharges) {
                                                transAmt -= recoveryCharges;
                                                paidInterest = recoveryCharges;
                                            } else {
                                                paidInterest = transAmt;
                                                transAmt -= recoveryCharges;

                                            }
                                            txtransfer.setAmount(new Double(paidInterest));
                                            txtransfer.setInpAmount(new Double(paidInterest));
                                            txtransfer.setActNum("");
                                            txtransfer.setProdType(TransactionFactory.GL);
                                            txtransfer.setProdId("");
                                            txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("RECOVERY_CHARGES")));
                                            if (linkBatchId.length() > 0) {
                                                txtransfer.setLinkBatchId(linkBatchId);
                                            } else if (txtransferTo.getActNum() != null && txtransferTo.getActNum().length() > 0) {
                                                txtransfer.setLinkBatchId(txtransferTo.getActNum());
                                            }
                                            txtransfer.setGlTransActNum(glTransActNum);
                                            txtransfer.setParticulars(txtransferTo.getActNum() + ":" + "RECOVERY CHARGES");
                                            txtransfer.setAuthorizeRemarks("RECOVERY CHARGES");
                                            txtransfer.setNarration(txtransferTo.getNarration());
                                            txtransfer.setInstrumentNo2("LOAN_RECOVERY_CHARGES");
                                            txtransfer.setHierarchyLevel(String.valueOf(hierarchyLevel));
                                            txtransfer.setTransModType(txtransferTo.getTransModType());
                                            transferTOs.add(txtransfer);
                                        }
                                        paidInterest = 0;
                                    }
                                    // End of Recovery charges
                                    
                                     //Measurement Charges starts
                                    if (ALL_LOAN_AMOUNT.containsKey("MEASUREMENT CHARGES") && ALL_LOAN_AMOUNT.get("MEASUREMENT CHARGES") != null) {
                                        txtransferTo = (TxTransferTO) transferTOs.get(i);
                                        txtransfer = new TxTransferTO();
                                        txtransfer = setTransactiontoTLAD(txtransferTo);
                                        double measurementCharges = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("MEASUREMENT CHARGES")).doubleValue();
                                        if (measurementCharges > 0 && waiveOffMeasurement > 0) {
                                            transferTOs = commonWaiveOff(transferTOs, txtransferTo, waiveOffMeasurement, generateWaiveId, "MEASUREMENT_WAIVE_OFF");
                                            measurementCharges -= waiveOffMeasurement;
                                        }
                                        if (transAmt > 0 && measurementCharges > 0) {
                                            if (transAmt >= measurementCharges) {
                                                transAmt -= measurementCharges;
                                                paidInterest = measurementCharges;
                                            } else {
                                                paidInterest = transAmt;
                                                transAmt -= measurementCharges;

                                            }
                                            txtransfer.setAmount(new Double(paidInterest));
                                            txtransfer.setInpAmount(new Double(paidInterest));
                                            txtransfer.setActNum("");
                                            txtransfer.setProdType(TransactionFactory.GL);
                                            txtransfer.setProdId("");
                                            txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("MEASUREMENT_CHARGES")));
                                            if (linkBatchId.length() > 0) {
                                                txtransfer.setLinkBatchId(linkBatchId);
                                            } else if (txtransferTo.getActNum() != null && txtransferTo.getActNum().length() > 0) {
                                                txtransfer.setLinkBatchId(txtransferTo.getActNum());
                                            }
                                            txtransfer.setGlTransActNum(glTransActNum);
                                            txtransfer.setParticulars(txtransferTo.getActNum() + ":" + "MEASUREMENT CHARGES");
                                            txtransfer.setAuthorizeRemarks("MEASUREMENT CHARGES");
                                            txtransfer.setNarration(txtransferTo.getNarration());
                                            txtransfer.setInstrumentNo2("LOAN_MEASUREMENT_CHARGES");
                                            txtransfer.setHierarchyLevel(String.valueOf(hierarchyLevel));
                                            txtransfer.setTransModType(txtransferTo.getTransModType());
                                            transferTOs.add(txtransfer);
                                        }
                                        paidInterest = 0;
                                    }
                                    // End of Measurement charges
                                    
                                    
                                    // Kole Field Expense charges starts    
                                    if (ALL_LOAN_AMOUNT.containsKey("KOLEFIELD EXPENSE") && ALL_LOAN_AMOUNT.get("KOLEFIELD EXPENSE") != null) {
                                        txtransferTo = (TxTransferTO) transferTOs.get(i);
                                        txtransfer = new TxTransferTO();
                                        txtransfer = setTransactiontoTLAD(txtransferTo);
                                        double koleFieldExpense = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("KOLEFIELD EXPENSE")).doubleValue();
                                        if (koleFieldExpense > 0 && waiveOffKoleFieldExpense > 0) {
                                            transferTOs = commonWaiveOff(transferTOs, txtransferTo, waiveOffKoleFieldExpense, generateWaiveId, "KOLE_FIELD_EXPENSE_WAIVE_OFF");
                                            koleFieldExpense -= waiveOffKoleFieldExpense;
                                        }
                                        if (transAmt > 0 && koleFieldExpense > 0) {
                                            if (transAmt >= koleFieldExpense) {
                                                transAmt -= koleFieldExpense;
                                                paidInterest = koleFieldExpense;
                                            } else {
                                                paidInterest = transAmt;
                                                transAmt -= koleFieldExpense;

                                            }
                                            txtransfer.setAmount(new Double(paidInterest));
                                            txtransfer.setInpAmount(new Double(paidInterest));
                                            txtransfer.setActNum("");
                                            txtransfer.setProdType(TransactionFactory.GL);
                                            txtransfer.setProdId("");
                                            txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("KOLE_FIELD_EXPENSE")));
                                            if (linkBatchId.length() > 0) {
                                                txtransfer.setLinkBatchId(linkBatchId);
                                            } else if (txtransferTo.getActNum() != null && txtransferTo.getActNum().length() > 0) {
                                                txtransfer.setLinkBatchId(txtransferTo.getActNum());
                                            }
                                            txtransfer.setGlTransActNum(glTransActNum);
                                            txtransfer.setParticulars(txtransferTo.getActNum() + ":" + "KOLEFIELD EXPENSE");
                                            txtransfer.setAuthorizeRemarks("KOLEFIELD EXPENSE");
                                            txtransfer.setNarration(txtransferTo.getNarration());
                                            txtransfer.setInstrumentNo2("KOLEFIELD EXPENSE");
                                            txtransfer.setHierarchyLevel(String.valueOf(hierarchyLevel));
                                            txtransfer.setTransModType(txtransferTo.getTransModType());
                                            transferTOs.add(txtransfer);
                                        }
                                        paidInterest = 0;
                                    }
                                    // End of Kole Field Expense charges starts

                                    // Kole Field Expense charges starts    
                                    if (ALL_LOAN_AMOUNT.containsKey("KOLEFIELD OPERATION") && ALL_LOAN_AMOUNT.get("KOLEFIELD OPERATION") != null) {
                                        txtransferTo = (TxTransferTO) transferTOs.get(i);
                                        txtransfer = new TxTransferTO();
                                        txtransfer = setTransactiontoTLAD(txtransferTo);
                                        double koleFieldOperation = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("KOLEFIELD OPERATION")).doubleValue();
                                        if (koleFieldOperation > 0 && waiveOffKoleFieldOperation > 0) {
                                            transferTOs = commonWaiveOff(transferTOs, txtransferTo, waiveOffKoleFieldOperation, generateWaiveId, "KOLE_FIELD_OPERATION_WAIVE_OFF");
                                            koleFieldOperation -= waiveOffKoleFieldOperation;
                                        }
                                        if (transAmt > 0 && koleFieldOperation > 0) {
                                            if (transAmt >= koleFieldOperation) {
                                                transAmt -= koleFieldOperation;
                                                paidInterest = koleFieldOperation;
                                            } else {
                                                paidInterest = transAmt;
                                                transAmt -= koleFieldOperation;

                                            }
                                            txtransfer.setAmount(new Double(paidInterest));
                                            txtransfer.setInpAmount(new Double(paidInterest));
                                            txtransfer.setActNum("");
                                            txtransfer.setProdType(TransactionFactory.GL);
                                            txtransfer.setProdId("");
                                            txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("KOLE_FIELD_OPERATION")));
                                            if (linkBatchId.length() > 0) {
                                                txtransfer.setLinkBatchId(linkBatchId);
                                            } else if (txtransferTo.getActNum() != null && txtransferTo.getActNum().length() > 0) {
                                                txtransfer.setLinkBatchId(txtransferTo.getActNum());
                                            }
                                            txtransfer.setGlTransActNum(glTransActNum);
                                            txtransfer.setParticulars(txtransferTo.getActNum() + ":" + "KOLEFIELD OPERATION");
                                            txtransfer.setAuthorizeRemarks("KOLEFIELD OPERATION");
                                            txtransfer.setNarration(txtransferTo.getNarration());
                                            txtransfer.setInstrumentNo2("KOLEFIELD OPERATION");
                                            txtransfer.setHierarchyLevel(String.valueOf(hierarchyLevel));
                                            txtransfer.setTransModType(txtransferTo.getTransModType());
                                            transferTOs.add(txtransfer);
                                        }
                                        paidInterest = 0;
                                    }
                                    // End of Kole Field Expense charges starts  
                                    
                                    
                                    
                                    //arbitary charges
                                    txtransferTo = (TxTransferTO) transferTOs.get(i);
                                    txtransfer = new TxTransferTO();
                                    txtransfer = setTransactiontoTLAD(txtransferTo);
                                    double arbitaryCharges = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("ARBITRARY CHARGES")).doubleValue();
                                    if (arbitaryCharges > 0 && waiveOffArbitary > 0) {
                                        transferTOs = commonWaiveOff(transferTOs, txtransferTo, waiveOffArbitary, generateWaiveId, "ARBITRARY_WAIVE_OFF");
                                        arbitaryCharges -= waiveOffArbitary;
                            
                                    }
                                    if (transAmt > 0 && arbitaryCharges > 0) {
                                        if (transAmt >= arbitaryCharges) {
                                            transAmt -= arbitaryCharges;
                                            paidInterest = arbitaryCharges;
                                        } else {
                                            paidInterest = transAmt;
                                            transAmt -= arbitaryCharges;

                                        }
                                        txtransfer.setAmount(new Double(paidInterest));
                                        txtransfer.setInpAmount(new Double(paidInterest));
                                        txtransfer.setActNum("");
                                        txtransfer.setProdType(TransactionFactory.GL);
                                        txtransfer.setProdId("");
                                        txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("ARBITRARY_CHARGES")));//MISC_SERV_CHRG
                                        if (linkBatchId.length() > 0) {
                                            txtransfer.setLinkBatchId(linkBatchId);
                                        } else if (txtransferTo.getActNum() != null && txtransferTo.getActNum().length() > 0) {
                                            txtransfer.setLinkBatchId(txtransferTo.getActNum());
                                        }
                                        txtransfer.setGlTransActNum(glTransActNum);
                                        txtransfer.setParticulars(txtransferTo.getActNum() + ":" + "ARBITRARY CHARGES");
                                        txtransfer.setAuthorizeRemarks("ARBITRARY CHARGES");
                                        txtransfer.setNarration(txtransferTo.getNarration());
                                        txtransfer.setInstrumentNo2("LOAN_ARBITRARY_CHARGES");
                                        txtransfer.setHierarchyLevel(String.valueOf(hierarchyLevel));
                                        txtransfer.setTransModType(txtransferTo.getTransModType());
                                        transferTOs.add(txtransfer);
                                    }
                                    paidInterest = 0;
                                    //legal charges
                                    txtransferTo = (TxTransferTO) transferTOs.get(i);
                                    txtransfer = new TxTransferTO();
                                    txtransfer = setTransactiontoTLAD(txtransferTo);
                                    double legalCharges = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("LEGAL CHARGES")).doubleValue();
                                    if (legalCharges> 0 && waiveOfflegal> 0) {
                                     transferTOs=commonWaiveOff(transferTOs,txtransferTo, waiveOfflegal, generateWaiveId,"LEGAL_WAIVE_OFF");
                                      legalCharges-= waiveOfflegal;
                                    }
                                    if (transAmt > 0 && legalCharges > 0) {
                                        if (transAmt >= legalCharges) {
                                            transAmt -= legalCharges;
                                            paidInterest = legalCharges;
                                        } else {
                                            paidInterest = transAmt;
                                            transAmt -= legalCharges;

                                        }
                                        txtransfer.setAmount(new Double(paidInterest));
                                        txtransfer.setInpAmount(new Double(paidInterest));
                                        txtransfer.setActNum("");
                                        txtransfer.setProdType(TransactionFactory.GL);
                                        txtransfer.setProdId("");
                                        txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("LEGAL_CHARGES")));//NOTICE_CHARGES
                                        if (linkBatchId.length() > 0) {
                                            txtransfer.setLinkBatchId(linkBatchId);
                                        } else  if(txtransferTo.getActNum()!=null&&txtransferTo.getActNum().length()>0){
                                            txtransfer.setLinkBatchId(txtransferTo.getActNum());
                                        }
                                        txtransfer.setGlTransActNum(glTransActNum);
                                        txtransfer.setParticulars(txtransferTo.getActNum() + ":" + "LEGAL CHARGES");
                                        txtransfer.setAuthorizeRemarks("LEGAL CHARGES");
                                        txtransfer.setNarration(txtransferTo.getNarration());
                                        txtransfer.setInstrumentNo2("LOAN_LEGAL_CHARGES");
                                        txtransfer.setHierarchyLevel(String.valueOf(hierarchyLevel));
                                        txtransfer.setTransModType(txtransferTo.getTransModType());
                                        transferTOs.add(txtransfer);
                                    }
                                    paidInterest = 0;
                                    //insurnces charges
                                    txtransferTo = (TxTransferTO) transferTOs.get(i);
                                    txtransfer = new TxTransferTO();
                                    txtransfer = setTransactiontoTLAD(txtransferTo);
                                    double noticeCharges = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("NOTICE CHARGES")).doubleValue();
                                    if (noticeCharges > 0 && waiveOffNotice > 0) {
                                      transferTOs= noticeWaiveOff(transferTOs,txtransferTo, waiveOffNotice, generateWaiveId);
                                       // tempWaiveOffList.add(waiveOffList);
                                        noticeCharges -= waiveOffNotice;
                                        //  continue;
                                    }
                                    if (transAmt > 0 && noticeCharges > 0) {
                                        if (transAmt >= noticeCharges) {
                                            transAmt -= noticeCharges;
                                            paidInterest = noticeCharges;
                                        } else {
                                            paidInterest = transAmt;
                                            transAmt -= noticeCharges;
                                        }
                                        txtransfer.setAmount(new Double(paidInterest));
                                        txtransfer.setInpAmount(new Double(paidInterest));
                                        txtransfer.setActNum("");
                                        txtransfer.setProdType(TransactionFactory.GL);
                                        txtransfer.setProdId("");
                                        txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("NOTICE_CHARGES")));//NOTICE_CHARGES
                                        if (linkBatchId.length() > 0) {
                                            txtransfer.setLinkBatchId(linkBatchId);
                                        } else  if(txtransferTo.getActNum()!=null && txtransferTo.getActNum().length()>0){
                                            txtransfer.setLinkBatchId(txtransferTo.getActNum());
                                        }
                                        txtransfer.setGlTransActNum(glTransActNum);
                                        txtransfer.setParticulars(txtransferTo.getActNum() + ":" + "NOTICE CHARGES");
                                        txtransfer.setAuthorizeRemarks("NOTICE CHARGES");
                                        txtransfer.setNarration(txtransferTo.getNarration());
                                        txtransfer.setInstrumentNo2("LOAN_NOTICE_CHARGES");
                                        txtransfer.setHierarchyLevel(String.valueOf(hierarchyLevel));
                                        txtransfer.setTransModType(txtransferTo.getTransModType());
                                        transferTOs.add(txtransfer);
                                    }
                                    paidInterest = 0;
                                    //insurnces charges
                                    txtransferTo = (TxTransferTO) transferTOs.get(i);
                                    txtransfer = new TxTransferTO();
                                    txtransfer = setTransactiontoTLAD(txtransferTo);
                                    double insuranceCharge = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("INSURANCE CHARGES")).doubleValue();
                                    if (insuranceCharge> 0 && waiveOffinsur> 0) {
                                        transferTOs=commonWaiveOff(transferTOs,txtransferTo, waiveOffinsur, generateWaiveId,"INSURANCE_WAIVE_OFF");
                                        insuranceCharge-= waiveOffinsur;
                                    }
                                    if (transAmt > 0 && insuranceCharge > 0) {
                                        if (transAmt >= insuranceCharge) {
                                            transAmt -= insuranceCharge;
                                            paidInterest = insuranceCharge;
                                        } else {
                                            paidInterest = transAmt;
                                            transAmt -= insuranceCharge;

                                        }
                                        txtransfer.setAmount(new Double(paidInterest));
                                        txtransfer.setInpAmount(new Double(paidInterest));
                                        txtransfer.setActNum("");
                                        txtransfer.setProdType(TransactionFactory.GL);
                                        txtransfer.setProdId("");
                                        txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("INSURANCE_CHARGES")));//MISC_SERV_CHRG
                                        if (linkBatchId.length() > 0) {
                                            txtransfer.setLinkBatchId(linkBatchId);
                                        } else  if(txtransferTo.getActNum()!=null&&txtransferTo.getActNum().length()>0){
                                            txtransfer.setLinkBatchId(txtransferTo.getActNum());
                                        }
                                        txtransfer.setGlTransActNum(glTransActNum);
                                        txtransfer.setParticulars(txtransferTo.getActNum() + ":" + "INSURANCE CHARGES");
                                        txtransfer.setAuthorizeRemarks("INSURANCE CHARGES");
                                        txtransfer.setNarration(txtransferTo.getNarration());
                                        txtransfer.setInstrumentNo2("LOAN_INSURANCE_CHARGES");
                                        txtransfer.setHierarchyLevel(String.valueOf(hierarchyLevel));
                                        txtransfer.setTransModType(txtransferTo.getTransModType());
                                        transferTOs.add(txtransfer);
                                    }
                                    paidInterest = 0;
                                    //misc
                                    txtransferTo = (TxTransferTO) transferTOs.get(i);
                                    txtransfer = new TxTransferTO();
                                    txtransfer = setTransactiontoTLAD(txtransferTo);
                                    double miscellous = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("MISCELLANEOUS CHARGES")).doubleValue();
                                    if (miscellous> 0 && waiveOffMiss> 0) {
                                        transferTOs=commonWaiveOff(transferTOs,txtransferTo, waiveOffMiss, generateWaiveId,"MISCELLANEOUS_WAIVE_OFF");
                                        miscellous-= waiveOffMiss;
                                    }
                                    
                                    if (transAmt > 0 && miscellous > 0) {
                                        if (transAmt >= miscellous) {
                                            transAmt -= miscellous;
                                            paidInterest = miscellous;
                                        } else {
                                            paidInterest = transAmt;
                                            transAmt -= miscellous;

                                        }
                                        txtransfer.setAmount(new Double(paidInterest));
                                        txtransfer.setInpAmount(new Double(paidInterest));
                                        txtransfer.setActNum("");
                                        txtransfer.setProdType(TransactionFactory.GL);
                                        txtransfer.setProdId("");
                                        txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("MISC_SERV_CHRG")));
                                        if (linkBatchId.length() > 0) {
                                            txtransfer.setLinkBatchId(linkBatchId);
                                        } else  if(txtransferTo.getActNum()!=null && txtransferTo.getActNum().length()>0){
                                            txtransfer.setLinkBatchId(txtransferTo.getActNum());
                                        }
                                        txtransfer.setGlTransActNum(glTransActNum);
                                        txtransfer.setParticulars(txtransferTo.getActNum() + ":" + "MISCELLANEOUS CHARGES");
                                        txtransfer.setAuthorizeRemarks("MISCELLANEOUS CHARGES");
                                        txtransfer.setNarration(txtransferTo.getNarration());
                                        txtransfer.setInstrumentNo2("LOAN_MISC_SERV_CHRG");
                                        txtransfer.setHierarchyLevel(String.valueOf(hierarchyLevel));
                                        txtransfer.setTransModType(txtransferTo.getTransModType());
                                        transferTOs.add(txtransfer);
                                    }
                                    paidInterest = 0;
 				
                                    //exec degree
                                    txtransferTo = (TxTransferTO) transferTOs.get(i);
                                    txtransfer = new TxTransferTO();
                                    txtransfer = setTransactiontoTLAD(txtransferTo);
                                    double executionDegree = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("EXECUTION DECREE CHARGES")).doubleValue();
                                if (executionDegree> 0 && waiveOffDegree> 0) {
                                        transferTOs=commonWaiveOff(transferTOs,txtransferTo, waiveOffDegree, generateWaiveId,"DECREE_WAIVE_OFF");
                                        executionDegree-= waiveOffDegree;
                                    }
                                    if (transAmt > 0 && executionDegree > 0) {
                                        if (transAmt >= executionDegree) {
                                            transAmt -= executionDegree;
                                            paidInterest = executionDegree;
                                        } else {
                                            paidInterest = transAmt;
                                            transAmt -= executionDegree;

                                        }
                                        txtransfer.setAmount(new Double(paidInterest));
                                        txtransfer.setInpAmount(new Double(paidInterest));
                                        txtransfer.setActNum("");
                                        txtransfer.setProdType(TransactionFactory.GL);
                                        txtransfer.setProdId("");
                                        txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("EXECUTION_DECREE_CHARGES")));//MISC_SERV_CHRG
                                        if (linkBatchId.length() > 0) {
                                            txtransfer.setLinkBatchId(linkBatchId);
                                        } else  if(txtransferTo.getActNum()!=null && txtransferTo.getActNum().length()>0){
                                            txtransfer.setLinkBatchId(txtransferTo.getActNum());
                                        }
                                        txtransfer.setGlTransActNum(glTransActNum);
                                        txtransfer.setParticulars(txtransferTo.getActNum() + ":" + "EXECUTION DECREE CHARGES");
                                        txtransfer.setAuthorizeRemarks("EXECUTION DECREE CHARGES");
                                        txtransfer.setNarration(txtransferTo.getNarration());
                                        txtransfer.setInstrumentNo2("LOAN_EXECUTION_DECREE_CHARGES");
                                        txtransfer.setHierarchyLevel(String.valueOf(hierarchyLevel));
                                        txtransfer.setTransModType(txtransferTo.getTransModType());
                                        transferTOs.add(txtransfer);
                                    }
                                    paidInterest = 0;
                                    // case transaction
                                    txtransferTo = (TxTransferTO) transferTOs.get(i);
//                        txtransfer=new TxTransferTO();
//                        txtransfer = setTransactiontoTLAD(txtransferTo);
//                        double eaCost=CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("EA_COST")).doubleValue();
//                        if(transAmt>0 && eaCost>0 ){
//                            if( transAmt>=eaCost){
//                                transAmt-=eaCost;
//                                paidInterest=eaCost;
//                            }else{
//                                paidInterest=transAmt;
//                                transAmt-=eaCost;
//                                
//                            }
//                            txtransfer.setAmount(new Double(paidInterest));
//                            txtransfer.setInpAmount(new Double(paidInterest));
//                            txtransfer.setActNum("");
//                            txtransfer.setProdType(TransactionFactory.GL);
//                            txtransfer.setProdId("");
//                            txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("EA Cost")));//MISC_SERV_CHRG
//                            txtransfer.setLinkBatchId(txtransferTo.getActNum());
//                            txtransfer.setParticulars(txtransferTo.getActNum()+":"+"EXECUTION OF AWARD COST");
//                            txtransfer.setAuthorizeRemarks("EA");
//                            transferTOs.add(txtransfer);
//                        }
//                        paidInterest=0;              
//                        // case transaction
//                       txtransferTo=(TxTransferTO)transferTOs.get(i);
//                        txtransfer=new TxTransferTO();
//                        txtransfer = setTransactiontoTLAD(txtransferTo);
//                        double eaExpence=CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("EA_EXPENCE")).doubleValue();
//                        if(transAmt>0 && eaExpence>0 ){
//                            if( transAmt>=eaExpence){
//                                transAmt-=eaExpence;
//                                paidInterest=eaExpence;
//                            }else{
//                                paidInterest=transAmt;
//                                transAmt-=eaExpence;
//                                
//                            }
//                            txtransfer.setAmount(new Double(paidInterest));
//                            txtransfer.setInpAmount(new Double(paidInterest));
//                            txtransfer.setActNum("");
//                            txtransfer.setProdType(TransactionFactory.GL);
//                            txtransfer.setProdId("");
//                            txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("EA Expense")));//MISC_SERV_CHRG
//                            txtransfer.setLinkBatchId(txtransferTo.getActNum());
//                            txtransfer.setParticulars(txtransferTo.getActNum()+":"+"EXECUTION OF AWARD EXPENCE");
//                            txtransfer.setAuthorizeRemarks("EA");
//                            transferTOs.add(txtransfer);
//                        }
//                        paidInterest=0;          
//                        // case transaction
//                        txtransferTo=(TxTransferTO)transferTOs.get(i);
//                        txtransfer=new TxTransferTO();
//                        txtransfer = setTransactiontoTLAD(txtransferTo);
//                        double epCost=CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("EP_COST")).doubleValue();
//                        if(transAmt>0 && epCost>0 ){
//                            if( transAmt>=epCost){
//                                transAmt-=epCost;
//                                paidInterest=epCost;
//                            }else{
//                                paidInterest=transAmt;
//                                transAmt-=epCost;
//                                
//                            }
//                            txtransfer.setAmount(new Double(paidInterest));
//                            txtransfer.setInpAmount(new Double(paidInterest));
//                            txtransfer.setActNum("");
//                            txtransfer.setProdType(TransactionFactory.GL);
//                            txtransfer.setProdId("");
//                            txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("EP Cost")));//MISC_SERV_CHRG
//                            txtransfer.setLinkBatchId(txtransferTo.getActNum());
//                            txtransfer.setParticulars(txtransferTo.getActNum()+":"+"EXECUTION PROCESS COST");
//                            txtransfer.setAuthorizeRemarks("EP");
//                            transferTOs.add(txtransfer);
//                        }
//                        paidInterest=0;              
//                        // case transaction
//                        txtransferTo=(TxTransferTO)transferTOs.get(i);
//                        txtransfer=new TxTransferTO();
//                        txtransfer = setTransactiontoTLAD(txtransferTo);
//                        double epExpence=CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("EP_EXPENCE")).doubleValue();
//                        if(transAmt>0 && epExpence>0 ){
//                            if( transAmt>=epExpence){
//                                transAmt-=epExpence;
//                                paidInterest=epExpence;
//                            }else{
//                                paidInterest=transAmt;
//                                transAmt-=epExpence;
//                                
//                            }
//                            txtransfer.setAmount(new Double(paidInterest));
//                            txtransfer.setInpAmount(new Double(paidInterest));
//                            txtransfer.setActNum("");
//                            txtransfer.setProdType(TransactionFactory.GL);
//                            txtransfer.setProdId("");
//                            txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("EP Expense")));//MISC_SERV_CHRG
//                            txtransfer.setLinkBatchId(txtransferTo.getActNum());
//                            txtransfer.setParticulars(txtransferTo.getActNum()+":"+"EXECUTION PROCESS EXPENCE");
//                            txtransfer.setAuthorizeRemarks("EP");
//                            transferTOs.add(txtransfer);
//                        }
//                        paidInterest=0;                                                  
//                        
//                       // case transaction
//                        txtransferTo=(TxTransferTO)transferTOs.get(i);
//                        txtransfer=new TxTransferTO();
//                        txtransfer = setTransactiontoTLAD(txtransferTo);
//                        double arcCost=CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("ARC_COST")).doubleValue();
//                        if(transAmt>0 && arcCost>0 ){
//                            if( transAmt>=arcCost){
//                                transAmt-=arcCost;
//                                paidInterest=arcCost;
//                            }else{
//                                paidInterest=transAmt;
//                                transAmt-=arcCost;
//                                
//                            }
//                            txtransfer.setAmount(new Double(paidInterest));
//                            txtransfer.setInpAmount(new Double(paidInterest));
//                            txtransfer.setActNum("");
//                            txtransfer.setProdType(TransactionFactory.GL);
//                            txtransfer.setProdId("");
//                            txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("ARC Cost")));//MISC_SERV_CHRG
//                            txtransfer.setLinkBatchId(txtransferTo.getActNum());
//                            txtransfer.setParticulars(txtransferTo.getActNum()+":"+"ARC COST");
//                            txtransfer.setAuthorizeRemarks("ARC");
//                            transferTOs.add(txtransfer);
//                        }
//                        paidInterest=0;          
//                        
//                        txtransferTo=(TxTransferTO)transferTOs.get(i);
//                        txtransfer=new TxTransferTO();
//                        txtransfer = setTransactiontoTLAD(txtransferTo);
//                        double arcExpence=CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("ARC_EXPENCE")).doubleValue();
//                        if(transAmt>0 && arcExpence>0 ){
//                            if( transAmt>=arcExpence){
//                                transAmt-=arcExpence;
//                                paidInterest=arcExpence;
//                            }else{
//                                paidInterest=transAmt;
//                                transAmt-=arcExpence;
//                                
//                            }
//                            txtransfer.setAmount(new Double(paidInterest));
//                            txtransfer.setInpAmount(new Double(paidInterest));
//                            txtransfer.setActNum("");
//                            txtransfer.setProdType(TransactionFactory.GL);
//                            txtransfer.setProdId("");
//                            txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("ARC Expense")));//MISC_SERV_CHRG
//                            txtransfer.setLinkBatchId(txtransferTo.getActNum());
//                            txtransfer.setParticulars(txtransferTo.getActNum()+":"+"ARC EXPENCE");
//                            txtransfer.setAuthorizeRemarks("ARC");
//                            transferTOs.add(txtransfer);
//                        }
//                        paidInterest=0;                                                  


                                    //CASE EXPENCE AND CHARGES TRANSACTION



                                    //CASE DETAILS Changed By Suresh
                                    if (ALL_LOAN_AMOUNT.containsKey("OTHER_CHARGES")) {
                                        if (otherChargesMap == null) {
                                            otherChargesMap = new HashMap();
                                        }
                                        otherChargesMap = (HashMap) ALL_LOAN_AMOUNT.get("OTHER_CHARGES");
                                        System.out.println("@#$@#otherChargesMap:" + otherChargesMap);
                                        Object keys[] = otherChargesMap.keySet().toArray();
                                        for (int k = 0; k < otherChargesMap.size(); k++) {
                                            txtransfer = setTransactiontoTLAD(txtransferTo);
                                            double otherCharge = CommonUtil.convertObjToDouble(otherChargesMap.get(keys[k])).doubleValue();
                                            System.out.println("$#@$#@$@otherCharge : " + otherCharge + ":" + keys[k]);
                                            if (transAmt > 0 && otherCharge > 0) {
                                                if (transAmt >= otherCharge) {
                                                    transAmt -= otherCharge;
                                                    paidInterest = otherCharge;
                                                } else {
                                                    paidInterest = transAmt;
                                                    transAmt -= otherCharge;
                                                }
                                                txtransfer.setAmount(new Double(paidInterest));
                                                txtransfer.setInpAmount(new Double(paidInterest));
                                                txtransfer.setActNum("");
                                                txtransfer.setProdType(TransactionFactory.GL);
                                                txtransfer.setProdId("");
                                                if (CommonUtil.convertObjToStr(keys[k]).equals("NOTICE CHARGES")) {
                                                    txtransfer.setAcHdId(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(map.get("NOTICE_CHARGES"))));
                                                } else if (CommonUtil.convertObjToStr(keys[k]).equals("OTHER CHARGES")) {
                                                    txtransfer.setAcHdId(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(map.get("OTHER_CHARGES"))));
                                                } else {
                                                    txtransfer.setAcHdId(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(keys[k])));
                                                }
                                                if (linkBatchId.length() > 0) {
                                                    txtransfer.setLinkBatchId(linkBatchId);
                                                } else  if(txtransferTo.getActNum()!=null && txtransferTo.getActNum().length()>0){
                                                    txtransfer.setLinkBatchId(txtransferTo.getActNum());
                                                }
                                                txtransfer.setGlTransActNum(glTransActNum);
                                                txtransfer.setParticulars(txtransferTo.getActNum() + ":" + keys[k]);
                                                txtransfer.setAuthorizeRemarks(CommonUtil.convertObjToStr(keys[k]));
                                                txtransfer.setNarration(txtransferTo.getNarration());
                                                txtransfer.setInstrumentNo2("LOAN_OTHER_CHARGES");
                                                txtransfer.setHierarchyLevel(String.valueOf(hierarchyLevel));
                                                txtransfer.setTransModType(txtransferTo.getTransModType());
                                                transferTOs.add(txtransfer);
                                            }
                                            paidInterest = 0;
                                            otherCharge = 0;
                                        }
                                    }
                                }

                                if (hierachyValue.equals("PENALINTEREST")) {
                                    
                                    // Added by nithya for 0008470 : overdue interest for EMI Loans
                                     txtransferTo = (TxTransferTO) transferTOs.get(i);
                                    txtransfer = new TxTransferTO();
                                    txtransfer = setTransactiontoTLAD(txtransferTo);
                                    double overDueInt = 0.0;
                                    if(ALL_LOAN_AMOUNT.containsKey("EMI_OVERDUE_CHARGE") && ALL_LOAN_AMOUNT.get("EMI_OVERDUE_CHARGE") != null){
                                       overDueInt =  CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("EMI_OVERDUE_CHARGE")).doubleValue();
                                    }                                          
                                    if (overDueIntWaiveOff.equals("Y") && waiveOffOverDueInt > 0) {
                                        transferTOs = overDueIntWaiveOff(transferTOs, txtransferTo, waiveOffOverDueInt, generateWaiveId);                                                                                
                                        overDueInt -= waiveOffOverDueInt;
                                    }
                                    //System.out.println("txtransferTo##" + txtransferTo + "overDueInt" + overDueInt );
                                    if (transAmt > 0 && overDueInt > 0) {
                                        if (transAmt >= overDueInt) {
                                            transAmt -= overDueInt;
                                            paidInterest = overDueInt;
                                        } else {
                                            paidInterest = transAmt;
                                            transAmt -= overDueInt;

                                        }
                                        txtransfer.setAmount(new Double(paidInterest));
                                        txtransfer.setInpAmount(new Double(paidInterest));
                                        txtransfer.setActNum("");
                                        txtransfer.setProdType(TransactionFactory.GL);
                                        txtransfer.setProdId("");
                                        txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("OVER_DUEINT_ACHD")));
                                        if (linkBatchId.length() > 0) {
                                            txtransfer.setLinkBatchId(linkBatchId);
                                        } else if (txtransferTo.getActNum() != null && txtransferTo.getActNum().length() > 0) {
                                            txtransfer.setLinkBatchId(txtransferTo.getActNum());
                                        }
                                        txtransfer.setGlTransActNum(glTransActNum);
                                        txtransfer.setParticulars(txtransferTo.getActNum() + ":" + "OVERDUE_INT");
                                        txtransfer.setAuthorizeRemarks("OVERDUE_INT");
                                        txtransfer.setNarration(txtransferTo.getNarration());
                                        txtransfer.setInstrumentNo2("LOAN_OVERDUE_INT");
                                        txtransfer.setHierarchyLevel(String.valueOf(hierarchyLevel));
                                        txtransfer.setTransModType(txtransferTo.getTransModType());
                                        txtransfer.setInpCurr(txtransferTo.getInpCurr());
                                        transferTOs.add(txtransfer);
                                    }
                                    // End
                                    
                                    
                                    //penal interest
                                    txtransferTo = (TxTransferTO) transferTOs.get(i);
                                    txtransfer = new TxTransferTO();
                                    txtransfer = setTransactiontoTLAD(txtransferTo);
                                    double penalInterest = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("PENAL_INT")).doubleValue();
                                    System.out.println("txtransferTo##" + txtransferTo + "penalInterest" + penalInterest + "waiveOffInterest" + waiveOffInterest + penalWaiveOff);
                                    //modified by rishad 18/04/2014
                                    if (penalWaiveOff.equals("Y") || waiveOffPenal > 0) {
                                        // tempWaiveOffList = penalWaiveOff(txtransferTo, penalInterest, waiveOffInterest);
                                        // tempWaiveOffList = penalWaiveOff(txtransferTo,waiveOffPenal,waiveOffInterest);
                                      transferTOs = penalWaiveOff(transferTOs,txtransferTo, waiveOffPenal, generateWaiveId);
                                      // tempWaiveOffList.add(waiveOffList);
                                        //penalWaiveOff(txtransferTo,penalInterest);
                                        penalInterest -= waiveOffPenal;
                                        //continue;
                                    }

//                                    double penalInterest=CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get(PENAL_INT)).doubleValue();
                                    if (transAmt > 0 && penalInterest > 0) {
                                        if (transAmt >= penalInterest) {
                                            transAmt -= penalInterest;
                                            paidInterest = penalInterest;
                                        } else {
                                            paidInterest = transAmt;
                                            transAmt -= penalInterest;

                                        }
                                        txtransfer.setAmount(new Double(paidInterest));
                                        txtransfer.setInpAmount(new Double(paidInterest));
                                        txtransfer.setActNum("");
                                        txtransfer.setProdType(TransactionFactory.GL);
                                        txtransfer.setProdId("");
                                        txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get(PENAL_INT)));
                                        if (linkBatchId.length() > 0) {
                                            txtransfer.setLinkBatchId(linkBatchId);
                                        } else  if(txtransferTo.getActNum()!=null && txtransferTo.getActNum().length()>0){
                                            txtransfer.setLinkBatchId(txtransferTo.getActNum());
                                        }
                                        txtransfer.setGlTransActNum(glTransActNum);
                                        txtransfer.setParticulars(txtransferTo.getActNum() + ":" + "PENAL INTEREST");
                                        txtransfer.setAuthorizeRemarks(PENAL_INT);
                                        txtransfer.setNarration(txtransferTo.getNarration());
                                        txtransfer.setInstrumentNo2("LOAN_PENAL_INTEREST");
                                        txtransfer.setHierarchyLevel(String.valueOf(hierarchyLevel));
                                        txtransfer.setTransModType(txtransferTo.getTransModType());
                                        txtransfer.setInpCurr(txtransferTo.getInpCurr());
                                        transferTOs.add(txtransfer);
                                    }
                                }

                                if (hierachyValue.equals("INTEREST")) {
                                    //interest
                                    txtransferTo = (TxTransferTO) transferTOs.get(i);
                                    txtransfer = new TxTransferTO();
                                    txtransfer = setTransactiontoTLAD(txtransferTo);
                                    double interest = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("CURR_MONTH_INT")).doubleValue();
                                    double addLoanIntAmt = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("ADD_LOAN_INT_AMOUNT"));
                                    // interest -= waiveOffInterest;
                                    if (interest > 0 && waiveOffInterest > 0) {
                                        transferTOs= interestWaiveOff(transferTOs,txtransferTo, waiveOffInterest, generateWaiveId);
                                        //tempWaiveOffList.add(waiveOffList);
                                        interest -= waiveOffInterest;
                                        //  continue;
                                    }
                                    if (rebateInterest > 0 && interest > 0) {
                                        if (rebateAllowed.equals("Y")) {
                                            if (interest >= rebateInterest) {
                                                interest -= rebateInterest;
                                                tempList = rebateInterestTransaction(txtransfer, rebateInterest, 0.0,rebateUptoDate);
                                            } else {
                                                tempList = rebateInterestTransaction(txtransfer, rebateInterest, interest,rebateUptoDate);
                                                interest = 0;
                                            }

//                                            interest-=rebateInterest;
//                                            ArrayList tempList=rebateInterestTransaction(txtransfer,rebateInterest);
//                                            if(tempList !=null && tempList.size()>0){
//                                                for(int a=0;a<tempList.size();a++){
//                                                    transferTOs.add(tempList.get(a));
//                                                }
//                                                System.out.println("transferTOs#####"+transferTOs);
//                                            }
                                        }

                                    }

                                    if (transAmt > 0 && interest > 0) {
                                        if (transAmt >= interest) {
                                            transAmt -= interest;
                                            paidInterest = interest;
                                        } else {
                                            paidInterest = transAmt;
                                            transAmt -= interest;

                                        }
                                        txtransfer.setAmount(new Double(paidInterest));
                                        txtransfer.setInpAmount(new Double(paidInterest));
                                        txtransfer.setActNum("");
                                        txtransfer.setProdType(TransactionFactory.GL);
                                        txtransfer.setProdId("");
                                        txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("AC_DEBIT_INT")));
                                        //txtransfer.setParticulars(txtransferTo.getActNum() + ":" + INTEREST);
                                        Date intUpToDt = getIntUpToDtForLoan(txtransferTo.getActNum(), paidInterest);   
                                        
                                        //Calculate balance interest
                                        String balanceIntAmtVal = "";
                                        double calculatedIterest = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("CURR_MONTH_INT")).doubleValue();
                                        double balanceInterest = calculatedIterest - paidInterest;
                                        if (balanceInterest >= 0) {
                                            balanceIntAmtVal = CommonUtil.convertObjToStr("#Balance Interest :" + CommonUtil.convertObjToStr(balanceInterest));
                                        }
                                        //End
                                        
                                        txtransfer.setParticulars(txtransferTo.getActNum() + ":" + "INTEREST : Paid Up To -"+ DateUtil.getStringDate(intUpToDt));
                                        txtransfer.setNarration(txtransferTo.getNarration()+balanceIntAmtVal);
                                        txtransfer.setInstrumentNo2("LOAN_INTEREST");
                                        txtransfer.setHierarchyLevel(String.valueOf(hierarchyLevel));
                                        txtransfer.setTransModType(txtransferTo.getTransModType());
                                        /*
                                         * particualr upto date formula
                                         * interestamt/interestrate/principal
                                         * amount*365=no of days
                                         */
                                        if (linkBatchId.length() > 0) {
                                            txtransfer.setLinkBatchId(linkBatchId);
                                        } else  if(txtransferTo.getActNum()!=null && txtransferTo.getActNum().length()>0){
                                            txtransfer.setLinkBatchId(txtransferTo.getActNum());
                                        }
                                        txtransfer.setGlTransActNum(glTransActNum);
                                        txtransfer.setAuthorizeRemarks(INTEREST);
                                        txtransfer.setInpCurr(txtransferTo.getInpCurr());
                                        transferTOs.add(txtransfer);
                                        System.out.println("INTEREST#####" + txtransfer.getTransModType());
                                    }
                                    if (transAmt > 0 && addLoanIntAmt > 0) {
                                        txtransfer = new TxTransferTO();
                                        txtransfer = setTransactiontoTLAD(txtransferTo);
                                        if (transAmt >= addLoanIntAmt) {
                                            transAmt -= addLoanIntAmt;
                                            paidInterest = addLoanIntAmt;
                                        } else {
                                            paidInterest = transAmt;
                                            transAmt -= addLoanIntAmt;

                                        }
                                        txtransfer.setAmount(new Double(paidInterest));
                                        txtransfer.setInpAmount(new Double(paidInterest));
                                        txtransfer.setActNum("");
                                        txtransfer.setProdType(TransactionFactory.GL);
                                        txtransfer.setProdId("");
                                        txtransfer.setAcHdId(CommonUtil.convertObjToStr(map.get("AC_DEBIT_INT")));
                                        txtransfer.setParticulars(txtransferTo.getActNum() + ": Additional " + INTEREST);
                                        txtransfer.setNarration(txtransferTo.getNarration());
                                        txtransfer.setInstrumentNo2("ADDITIONAL_LOAN_INTEREST");
                                        txtransfer.setHierarchyLevel(String.valueOf(hierarchyLevel));
                                        txtransfer.setTransModType(txtransferTo.getTransModType());
                                        /*
                                         * particualr upto date formula
                                         * interestamt/interestrate/principal
                                         * amount*365=no of days
                                         */
                                        if (linkBatchId.length() > 0) {
                                            txtransfer.setLinkBatchId(linkBatchId);
                                        } else  if(txtransferTo.getActNum()!=null && txtransferTo.getActNum().length()>0){
                                            txtransfer.setLinkBatchId(txtransferTo.getActNum());
                                        }
                                        txtransfer.setGlTransActNum(glTransActNum);
                                        txtransfer.setAuthorizeRemarks(INTEREST);
                                        txtransfer.setInpCurr(txtransferTo.getInpCurr());
                                        transferTOs.add(txtransfer);
                                        System.out.println("addLoanIntAmtaddLoanIntAmtaddLoanIntAmt#####" + txtransfer);
                                    }
                                }
                                //principal
                                //             objCashTransactionTO = setCashTransaction();
                                if (hierachyValue.equals("PRINCIPAL")) {
                                    txtransfer = (TxTransferTO) transferTOs.get(i);
                                    txtransfer = setTransactiontoTLAD(txtransferTo);
                                    double principal = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("CURR_MONTH_PRINCEPLE")).doubleValue();
//                                    txtransferTo = (TxTransferTO) transferTOs.get(i);
//                                    txtransfer = new TxTransferTO();
//                                    txtransfer = setTransactiontoTLAD(txtransferTo);
                                    double interest = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("CURR_MONTH_INT")).doubleValue();
                                    if (waiveOffPrincipal > 0 && principal > 0) {
                                        principalWaiveOff(transferTOs, txtransfer, waiveOffPrincipal, generateWaiveId);
                                        // tempWaiveOffList.add(waiveOffList);
                                        principal -= waiveOffPrincipal;
                                    }
                                    if (transAmt > 0) {
                                        if (transAmt >= principal) {
                                            transAmt -= principal;
                                            paidprincipal = principal;
                                        } else {
                                            paidprincipal = transAmt;
                                            transAmt -= principal;

                                        }
                                        if (interest < 0) {
                                            transAmt = transAmt + (interest * -1);
                                        } //chithraaa
                                        txtransferTo.setAmount(new Double(paidprincipal));
                                        txtransferTo.setInpAmount(new Double(paidprincipal));
                                        if (linkBatchId.length() > 0) {
                                            txtransferTo.setLinkBatchId(linkBatchId);
                                        } else  if(txtransferTo.getActNum()!=null && txtransferTo.getActNum().length()>0){
                                            txtransferTo.setLinkBatchId(txtransferTo.getActNum());
                                        }
                                        txtransfer.setGlTransActNum(glTransActNum);
                                        if(!txtransferTo.getProdType().equals("AD"))
                                            txtransferTo.setInstrumentNo2("LOAN_PRINCIPAL"); //Added by sreekrishnan for 10705
                                        txtransferTo.setHierarchyLevel(String.valueOf(hierarchyLevel));
//                            if(no_of_installment>0)
                                        txtransferTo.setAuthorizeRemarks(String.valueOf(no_of_installment));
                                        txtransfer.setTransModType(txtransferTo.getTransModType());
                                        txtransfer.setInpCurr(txtransferTo.getInpCurr());
                                        System.out.println("txtransferTo###" + txtransferTo);
                                        //                        transferTOs.add(i,txtransfer);
                                    } else {
                                       transferTOs.remove(i);
                                       i--;
                                    }
                                }
                            }

                            if (transAmt > 0) {
                                for (int j = 0; j < transferTOs.size(); j++) {
                                    TxTransferTO obj = (TxTransferTO) transferTOs.get(j);
                                    if ((obj.getProdType().equals("TL") || obj.getProdType().equals("AD")) && obj.getTransType().equals("CREDIT") && txtransfer.getActNum().equals(obj.getActNum())) {
                                        double principal = obj.getAmount().doubleValue();
                                        principal += transAmt;
                                        obj.setAmount(new Double(principal));
                                        obj.setInpAmount(new Double(principal));
                                        obj.setHierarchyLevel(String.valueOf(hierarchyLevel));
                                        transferTOs.set(j, obj);
                                        isTLAvailable = true;
                                        break;
                                    }
                                }
                                if (!isTLAvailable) {
                                    txtransferTo.setAmount(new Double(transAmt));
                                    txtransferTo.setInpAmount(new Double(transAmt));
                                    if (linkBatchId.length() > 0) {
                                        txtransferTo.setLinkBatchId(linkBatchId);
                                    } else  if(txtransferTo.getActNum()!=null && txtransferTo.getActNum().length()>0){
                                        txtransferTo.setLinkBatchId(txtransferTo.getActNum());
                                    }
                                    txtransfer.setGlTransActNum(glTransActNum);
                                    if(!txtransferTo.getProdType().equals("AD"))
                                        txtransferTo.setInstrumentNo2("LOAN_PRINCIPAL"); //Added by sreekrishnan for 10705
                                    txtransferTo.setHierarchyLevel(String.valueOf(hierarchyLevel));
                                    //                            if(no_of_installment>0)
                                    txtransferTo.setAuthorizeRemarks(String.valueOf(no_of_installment));
                                    txtransfer.setTransModType(txtransferTo.getTransModType());
                                }
                            }
                        }
                        //         else{
                        //              final CashTransactionTO objCashTransactionTO = setCashTransaction();
                        //              cashList.add(objCashTransactionTO);
                        //         }
                        if (!dataMap.containsKey("MULTIPLE_ALL_AMOUNT")) {
                            if (linkBatchId.length() > 0) {
                                dataMap.put(LINK_BATCH_ID, linkBatchId);
                            } else {
                                dataMap.put(LINK_BATCH_ID, txtransferTo.getActNum());
                            }
                        }
//            }
                    }
                }
            }
        }
        if (tempList != null && tempList.size() > 0) {
            for (int a = 0; a < tempList.size(); a++) {
                transferTOs.add(tempList.get(a));
                //System.out.println("tempList#####"+tempList);
            }
        }
        if (tempWaiveOffList != null && tempWaiveOffList.size() > 0) {
            for (int a = 0; a < tempWaiveOffList.size(); a++) {
                transferTOs.add(tempWaiveOffList.get(a));
                System.out.println("tempWaiveOffList#####" + tempWaiveOffList);
            }
        }
        System.out.println("transferTOs#####" + transferTOs);
        return transferTOs;
    }
    /*
     * commented by rishad 19/04/2014 replaced function are mentioned below
     * ..reason for comenting this function: waive transaction flow changed
     */
//    private ArrayList penalWaiveOff(TxTransferTO obj, double waivePenalAmt, double waiveInterestAmt) throws Exception {
//        TermLoanPenalWaiveOffTO objpenalWaive = new TermLoanPenalWaiveOffTO();
//        ArrayList tempWaiveOffList = new ArrayList();
//        //tempWaiveOffList = interestWaiveoffTransaction(obj, waivePenalAmt, waiveInterestAmt);
//         tempWaiveOffList = penalWaiveoffTransaction(obj, waivePenalAmt, waiveInterestAmt);
//        if (obj != null) {
//            objpenalWaive.setAcctNum(obj.getActNum());
//            objpenalWaive.setWaiveDt((Date) properFormatDate.clone());
//            objpenalWaive.setInterestAmt(new Double(waiveInterestAmt));
//            objpenalWaive.setPenalAmt(new Double(waivePenalAmt));
//            objpenalWaive.setStatus(CommonConstants.STATUS_CREATED);
//            objpenalWaive.setStatusBy(obj.getStatusBy());
//            objpenalWaive.setStatusDt((Date) properFormatDate.clone());
//            objpenalWaive.setWaiveOffId(CommonUtil.convertObjToStr(generateWaiveOffBatchID()));
//            sqlMap.executeUpdate("insertTermLoanInterestWaiveOffTO", objpenalWaive);
//
//
//        }
//        return tempWaiveOffList;
//    }

    private ArrayList penalWaiveOff(ArrayList transferTOs,TxTransferTO obj, double waivePenalAmt, String generateWaiveId) throws Exception {
        TermLoanPenalWaiveOffTO objpenalWaive = new TermLoanPenalWaiveOffTO();
        ArrayList tempWaiveOffList = new ArrayList();
        tempWaiveOffList = penalWaiveoffTransaction(transferTOs,obj, waivePenalAmt);
        if (obj != null) {
            objpenalWaive.setAcctNum(obj.getActNum());
            objpenalWaive.setWaiveDt((Date) properFormatDate.clone());
            objpenalWaive.setWaiveAmt(new Double(waivePenalAmt));
            objpenalWaive.setRemarks("PENAL_WAIVEOFF");
            objpenalWaive.setStatus(CommonConstants.STATUS_CREATED);
            objpenalWaive.setStatusBy(obj.getStatusBy());
            objpenalWaive.setStatusDt((Date) properFormatDate.clone());
            //objpenalWaive.setWaiveOffId(CommonUtil.convertObjToStr(generateWaiveOffBatchID()));
            objpenalWaive.setWaiveOffId(generateWaiveId);
            sqlMap.executeUpdate("insertTermLoanWaiveOffTO", objpenalWaive);
        }
        return tempWaiveOffList;
    }
    
        private ArrayList arcCostWaiveOff(ArrayList transferTOs, TxTransferTO obj, double waiveArcAmt, String generateWaiveId) throws Exception {
        TermLoanPenalWaiveOffTO objArcWaive = new TermLoanPenalWaiveOffTO();
        ArrayList tempWaiveOffList = new ArrayList();
        tempWaiveOffList = arcWaiveoffTransaction(transferTOs,obj, waiveArcAmt);
        if (obj != null) {
            objArcWaive.setAcctNum(obj.getActNum());
            objArcWaive.setWaiveDt((Date) properFormatDate.clone());
            objArcWaive.setWaiveAmt(new Double(waiveArcAmt));
            objArcWaive.setRemarks("ARC_WAIVEOFF");
            objArcWaive.setStatus(CommonConstants.STATUS_CREATED);
            objArcWaive.setStatusBy(obj.getStatusBy());
            objArcWaive.setStatusDt((Date) properFormatDate.clone());
            //objpenalWaive.setWaiveOffId(CommonUtil.convertObjToStr(generateWaiveOffBatchID()));
            objArcWaive.setWaiveOffId(generateWaiveId);
            sqlMap.executeUpdate("insertTermLoanWaiveOffTO", objArcWaive);
        }
        return tempWaiveOffList;
    }
        
        
     private ArrayList commonWaiveOff(ArrayList transferTOs, TxTransferTO obj, double waiveAmt, String generateWaiveId,String type) throws Exception {
        TermLoanPenalWaiveOffTO objArcWaive = new TermLoanPenalWaiveOffTO();
        ArrayList tempWaiveOffList = new ArrayList();
        tempWaiveOffList = commonWaiveoffTransaction(transferTOs,obj, waiveAmt,type);
        if (obj != null) {
            objArcWaive.setAcctNum(obj.getActNum());
            objArcWaive.setWaiveDt((Date) properFormatDate.clone());
            objArcWaive.setWaiveAmt(new Double(waiveAmt));
            objArcWaive.setRemarks(type); 
            objArcWaive.setStatus(CommonConstants.STATUS_CREATED);
            objArcWaive.setStatusBy(obj.getStatusBy());
            objArcWaive.setStatusDt((Date) properFormatDate.clone());
            //objpenalWaive.setWaiveOffId(CommonUtil.convertObjToStr(generateWaiveOffBatchID()));
            objArcWaive.setWaiveOffId(generateWaiveId);
            sqlMap.executeUpdate("insertTermLoanWaiveOffTO", objArcWaive);
        }
        return tempWaiveOffList;
    }     

    private ArrayList interestWaiveOff(ArrayList transferTOs,TxTransferTO obj, double waiveinterestAmt, String generateWaiveId) throws Exception {
        TermLoanPenalWaiveOffTO objinterestWaive = new TermLoanPenalWaiveOffTO();
        ArrayList tempWaiveOffList = new ArrayList();
        tempWaiveOffList = interestWaiveoffTransaction(transferTOs,obj, waiveinterestAmt);
        if (obj != null) {
            objinterestWaive.setAcctNum(obj.getActNum());
            objinterestWaive.setWaiveDt((Date) properFormatDate.clone());
            objinterestWaive.setWaiveAmt(new Double(waiveinterestAmt));
            objinterestWaive.setRemarks("INTEREST_WAIVEOFF");
            objinterestWaive.setStatus(CommonConstants.STATUS_CREATED);
            objinterestWaive.setStatusBy(obj.getStatusBy());
            objinterestWaive.setStatusDt((Date) properFormatDate.clone());
            //  objinterestWaive.setWaiveOffId(CommonUtil.convertObjToStr(generateWaiveOffBatchID()));
            objinterestWaive.setWaiveOffId(generateWaiveId);
            sqlMap.executeUpdate("insertTermLoanWaiveOffTO", objinterestWaive);
        }
        return tempWaiveOffList;
    }

    private ArrayList noticeWaiveOff(ArrayList transferTOs,TxTransferTO obj, double noticewaiveAmt, String generateWaiveId) throws Exception {
        TermLoanPenalWaiveOffTO objnoticeWaive = new TermLoanPenalWaiveOffTO();
        ArrayList tempWaiveOffList = new ArrayList();
        tempWaiveOffList = NoticeWaiveoffTransaction(transferTOs,obj, noticewaiveAmt);
        if (obj != null) {
            objnoticeWaive.setAcctNum(obj.getActNum());
            objnoticeWaive.setWaiveDt((Date) properFormatDate.clone());
            objnoticeWaive.setWaiveAmt(new Double(noticewaiveAmt));
            objnoticeWaive.setRemarks("NOTICE_WAIVEOFF");
            objnoticeWaive.setStatus(CommonConstants.STATUS_CREATED);
            objnoticeWaive.setStatusBy(obj.getStatusBy());
            objnoticeWaive.setStatusDt((Date) properFormatDate.clone());
            //objnoticeWaive.setWaiveOffId(CommonUtil.convertObjToStr(generateWaiveOffBatchID()));
            objnoticeWaive.setWaiveOffId(generateWaiveId);
            sqlMap.executeUpdate("insertTermLoanWaiveOffTO", objnoticeWaive);
        }
        return tempWaiveOffList;
    }

    private ArrayList principalWaiveOff(ArrayList transferTOs,TxTransferTO obj, double waivePrincipalAmt, String generateWaiveId) throws Exception {
        TermLoanPenalWaiveOffTO objprincipalWaive = new TermLoanPenalWaiveOffTO();
        ArrayList tempWaiveOffList = new ArrayList();
        tempWaiveOffList = principalWaiveoffTransaction(transferTOs,obj, waivePrincipalAmt);
        if (obj != null) {
            objprincipalWaive.setAcctNum(obj.getActNum());
            objprincipalWaive.setWaiveDt((Date) properFormatDate.clone());
            objprincipalWaive.setWaiveAmt(new Double(waivePrincipalAmt));
            objprincipalWaive.setRemarks("PRINCIPAL");
            objprincipalWaive.setStatus(CommonConstants.STATUS_CREATED);
            objprincipalWaive.setStatusBy(obj.getStatusBy());
            objprincipalWaive.setStatusDt((Date) properFormatDate.clone());
            //  objprincipalWaive.setWaiveOffId(CommonUtil.convertObjToStr(generateWaiveOffBatchID()));
            objprincipalWaive.setWaiveOffId(generateWaiveId);
            sqlMap.executeUpdate("insertTermLoanWaiveOffTO", objprincipalWaive);
        }
        return tempWaiveOffList;
    }

    //end
    private String generateWaiveOffBatchID() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "LOAN.WAIVE_OFF_ID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//for trans_batch_id resetting, branch code
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        String batchID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        return batchID;
    }

    public static void main(String str[]) {
        try {
            TransferDAO dao = new TransferDAO();
            ArrayList listTOs = new ArrayList();
            TxTransferTO txTransferTO = new TxTransferTO();

            //txTransferTO.setTransId("TT000072");
            //txTransferTO.setBatchId("TBT00058");
            txTransferTO.setStatus(CommonConstants.STATUS_CREATED);
            txTransferTO.setAcHdId("ROYAL");
            txTransferTO.setActNum("");
            txTransferTO.setInpAmount(new Double(1000));
            txTransferTO.setInpCurr("INR");
            txTransferTO.setAmount(new Double(1000));
            txTransferTO.setTransType(TransactionDAOConstants.CREDIT);
            txTransferTO.setInstType("WITHDRAW_SLIP");

            txTransferTO.setInstrumentNo1("BCD");
            txTransferTO.setInstrumentNo2("10000");
            txTransferTO.setInstDt(DateUtil.getDateMMDDYYYY("06/07/2004"));
            //txTransferTO.setTokenNo("11");
            txTransferTO.setInitTransId("********");
            txTransferTO.setInitChannType("ATM");
            txTransferTO.setParticulars("Transfer Deposit");
            listTOs.add(txTransferTO);
            txTransferTO = new TxTransferTO();

            //txTransferTO.setTransId("TT000072");
            //txTransferTO.setBatchId("TBT00058");
            txTransferTO.setStatus(CommonConstants.STATUS_CREATED);
            txTransferTO.setAcHdId("ROYAL");
            txTransferTO.setActNum("");
            txTransferTO.setInpAmount(new Double(1000));
            txTransferTO.setInpCurr("INR");
            txTransferTO.setAmount(new Double(1000));
            txTransferTO.setTransType(TransactionDAOConstants.CREDIT);
            txTransferTO.setInstType("WITHDRAW_SLIP");
            txTransferTO.setInstrumentNo1("BCD");
            txTransferTO.setInstrumentNo2("10000");
            txTransferTO.setInstDt(DateUtil.getDateMMDDYYYY("06/07/2004"));
            //txTransferTO.setTokenNo("11");
            txTransferTO.setInitTransId("********");
            txTransferTO.setInitChannType("ATM");
            txTransferTO.setParticulars("Transfer Deposit");
            listTOs.add(txTransferTO);
            HashMap hash = new HashMap();
            hash.put("TxTransferTO", listTOs);
            hash.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
            HashMap hash1 = new HashMap();
            hash1.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
            hash1.put("REMARKS", "system generated");
            hash.put(CommonConstants.AUTHORIZEMAP, hash1);
            dao.execute(hash);
        } catch (Exception ex) {
            ClientParseException.getInstance().logException(ex, true);
            ex.printStackTrace();
        }
    }

    private boolean checkInterBranchTransaction(ArrayList selectedList) throws Exception {
        int size = selectedList.size();
        TxTransferTO objTxTransferTO;
        boolean isInterBranch = false;
        for (int i = 0; i < size; i++) {
            objTxTransferTO = (TxTransferTO) selectedList.get(i);
            if (objTxTransferTO != null && !objTxTransferTO.getBranchId().equals(objTxTransferTO.getInitiatedBranch())) {
                isInterBranch = true;
                return isInterBranch;
            }
        }
        String oldBranch = "";
        int cnt = 1;
        for (int i = 0; i < size; i++) {
            objTxTransferTO = (TxTransferTO) selectedList.get(i);
            if (objTxTransferTO != null && objTxTransferTO.getBranchId().equals(oldBranch)) {
                cnt++;
            }
            oldBranch = objTxTransferTO.getBranchId();
        }
        if (cnt == size) {
            isInterBranch = false;
        }
        return isInterBranch;

    }

    private void authorize(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        System.out.println("map in transfer DAO : " + map);
        boolean isInterBranch = false;
        if (map.get(BATCH_ID) == null) {
            if (toList == null || toList.size() == 0) {
                throw new TTException("Transaction List is null...");
            }
            map.put(BATCH_ID, this.batchId);
            map.put(CommonConstants.AUTHORIZEDATA, toList);
        }
        if (map.containsKey("ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER")) {
            act_closing_min_bal_check = "ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER";
        }
        String status = (String) map.get(CommonConstants.AUTHORIZESTATUS);
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
     
        // need to proceed with Authorization Data List In Authorization Tme --Added by Rishad 16/11/2019 For sub table updation and insertion Missing at the time Of Authorization
        if (selectedList == null || selectedList.size() == 0) {
            throw new TTException("Transaction List is null...");
        }

        if (selectedList != null && checkInterBranchTransaction(selectedList)) {
            map.put("INTER_BRANCH_TRANS", new Boolean(true));
            isInterBranch = true;
        } else {
            isInterBranch = false;
        }
      //  System.out.println("isInterBranch#####" + isInterBranch);
        Double amount;
        HashMap dataMap = new HashMap();

        int size = selectedList.size();
        TxTransferTO objTxTransferTO;

        dataMap.put(CommonConstants.STATUS, status);
        dataMap.put(CommonConstants.USER_ID, objLogTO.getUserId());
        dataMap.put(BATCH_ID, map.get(BATCH_ID));
        dataMap.put("REMARKS", map.get("REMARKS"));
        dataMap.put("TODAY_DT", properFormatDate.clone());
        //Added by sreekrishnan for checking authorization
        HashMap authCheckMap = new HashMap();
        authCheckMap.put(BATCH_ID, map.get(BATCH_ID));
        authCheckMap.put("TRANS_DT", currDate.clone());
        authCheckMap.put("INITIATED_BRANCH", _branchCode);
        if(map.containsKey("MDS_BONUS_ENTRY") && map.containsKey("LOGGED_BRANCH_ID")){
            authCheckMap.put("TRANS_DT", ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(map.get("LOGGED_BRANCH_ID"))));
            _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
            currDate = ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(map.get("LOGGED_BRANCH_ID")));
        }
        List authlst = sqlMap.executeQueryForList("getTransferAuthorizeStatus", authCheckMap);
        if (authlst != null && authlst.size() > 0) {
            authCheckMap = (HashMap) authlst.get(0);
            String authStatus = CommonUtil.convertObjToStr(authCheckMap.get("AUTHORIZE_STATUS"));
            String authBy = CommonUtil.convertObjToStr(authCheckMap.get("AUTHORIZE_BY"));
            if (!authStatus.equals("") && authStatus.length()> 0 && (authStatus.equals(CommonConstants.STATUS_AUTHORIZED)|| authStatus.equals(CommonConstants.STATUS_REJECTED)) &&  !status.equals(CommonConstants.STATUS_EXCEPTION)) {               
                throw new TTException("This transaction already " + authStatus.toLowerCase() + " by " + authBy);
            }
        }

        //        sqlMap.executeUpdate("updateMasterTransferTO",dataMap);
        //for update exception_trans
        if (status.equalsIgnoreCase(CommonConstants.STATUS_EXCEPTION)) {
            dataMap.put("BRANCH_ID", _branchCode);
            sqlMap.executeUpdate("exceptionTransaction", dataMap);
        }
        //
        //        sqlMap.executeUpdate("authorizePassBookTT",dataMap);
        if (shift != null && !shift.equals("") && shift.length() > 0 && shift.equals("A")) {
            dataMap.put("SHIFT", shifttime);
        } else {
            dataMap.put("SHIFT", "");
        }
        if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
            dataMap.put("TRANS_DT", currDate.clone());
            dataMap.put("INITIATED_BRANCH", _branchCode);
            HashMap hash = (HashMap) sqlMap.executeQueryForObject("batchTallying", dataMap);
           // System.out.println("hash######" + hash + "dataMap#####" + dataMap);
            if (hash != null && hash.containsKey("DIFFERENCE") && ((java.math.BigDecimal) hash.get("DIFFERENCE")).doubleValue() != 0) {
                if (((java.math.BigDecimal) hash.get("DIFFERENCE")).doubleValue() != 0) {
             //////       System.out.println("Batch not tallied");
                    HashMap exception = new HashMap();
                    ArrayList list = new ArrayList();
                    list.add(new ValidationRuleException(TransactionConstants.BATCH_TALLY));
                    exception.put(CommonConstants.EXCEPTION_LIST, list);
                    exception.put(CommonConstants.CONSTANT_CLASS, TransactionDAOConstants.TRANS_RULE_MAP);
                    throw new TTException(exception);
                    //return;
                }
            }
            //            if (map.containsKey("PROCCHARGEMAP")) {
            //                HashMap procHash = (HashMap) map.get("PROCCHARGEMAP");
            //                System.out.println("@@@#####procchargemap"+procHash);
            //                updateProcessingChargesForLoan(procHash);
            //            }
        }
        double enteredInterest = 0, actualInterest = 0,penalInterset =0;
        if (map.containsKey("ACTUAL_INTEREST") && map.get("ACTUAL_INTEREST") != null) {
            actualInterest = CommonUtil.convertObjToDouble(map.get("ACTUAL_INTEREST"));
        }
        for (int i = 0; i < size; i++) {
            objTxTransferTO = (TxTransferTO) selectedList.get(i);
         //   System.out.println("@@@@objTxTransferTO" + objTxTransferTO);
            if (objTxTransferTO.getAuthorizeRemarks() != null && objTxTransferTO.getAuthorizeRemarks().equals("FROM_BILLS_MODULE") && objTxTransferTO.getProdType().equals(TransactionFactory.ADVANCES)) {
                transModuleBased = TransactionFactory.createTransaction("BILLS");
            } else {
                transModuleBased = TransactionFactory.createTransaction(objTxTransferTO.getProdType());
            }
            if (objTxTransferTO != null && !objTxTransferTO.getBranchId().equals(objTxTransferTO.getInitiatedBranch())) {
                isInterBranch = true;
                map.put("INTER_BRANCH_TRANS", new Boolean(true));
            } else if(map.containsKey("INTER_BRANCH_TRANS")){
                map.remove("INTER_BRANCH_TRANS");
                isInterBranch = false;
            }
          //  System.out.println("isInterBranch#####" + isInterBranch);
            objTxTransferTO.setAuthorizeBy(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.USER_ID)));
            objTxTransferTO.setAuthorizeStatus(status);
            if (objTxTransferTO.getAuthorizeRemarks() != null && objTxTransferTO.getAuthorizeRemarks().equals("INTEREST")) {
                enteredInterest = objTxTransferTO.getAmount();
            }
            if (objTxTransferTO.getScreenName() != null && objTxTransferTO.getScreenName().equals("CHARGESUI")
                    && objTxTransferTO.getAuthorizeRemarks() != null && objTxTransferTO.getAuthorizeRemarks().equals("PENAL_INT")) {
                penalInterset = objTxTransferTO.getAmount();
            }
            // AuthorizeStatus is Authorized
            if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                // Exisiting status is Created or Modified
                if (objTxTransferTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED)
                        || objTxTransferTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_MODIFIED)) {

                    HashMap ruleMap = getRuleMap(objTxTransferTO, 0.0, true);
                    if (map.containsKey("BACK_DATED_TRANSACTION")) {
                        ruleMap.put(TransactionDAOConstants.TODAY_DT, getProperDateFormat(objTxTransferTO.getTransDt()));
                        System.out.println("BACK_DATED_TRANSACTION ruleMap : "+ruleMap);
                    }
                    if (map.containsKey("INTERBRANCH_CREATION_SCREEN")) {
                        ruleMap.put("INTERBRANCH_CREATION_SCREEN", "INTERBRANCH_CREATION_SCREEN");
                    }
                    if (!isForLoanDebitInt()) {
                        if (isException) {
                            transModuleBased.validateRules(ruleMap, true);
                        } else {
                            transModuleBased.validateRules(ruleMap, false);
                        }
                    }
                 //   System.out.println("actualInterest-TR-->" + actualInterest + "enteredInterest-TR-->" + enteredInterest);
                    if (actualInterest > 0) {
                        ruleMap.put("ACTUAL_INTEREST", actualInterest);
                    }
                    if (enteredInterest > 0) {
                        ruleMap.put("ENTERED_INTEREST", enteredInterest);
                    }
                    if (penalInterset > 0) {
                        ruleMap.put("PENAL_INT_CHARGES", penalInterset);
                    }
                    String ACT_NUM = objTxTransferTO.getActNum();
                    String prod = objTxTransferTO.getProdType();
                    HashMap where = new HashMap();
                    where.put(CommonConstants.ACT_NUM, ACT_NUM);
                    double limit = 0.0;
                    double dp = 0.0;
                    if (objTxTransferTO.getProdType().equals(TransactionFactory.OPERATIVE) || (objTxTransferTO.getProdType().equals(TransactionFactory.ADVANCES) || objTxTransferTO.getProdType().equals(TransactionFactory.AGRIADVANCES) && (!loanParticulars.equals("CLEARING_BOUNCED")))) {
                        String type_of_tod = "";
                        List lstBal = sqlMap.executeQueryForList("getBalance" + prod, where);
                        where = null;
                        if (lstBal != null && lstBal.size() > 0) {
                            where = (HashMap) lstBal.get(0);
                            double clBal = CommonUtil.convertObjToDouble(where.get("CLEAR_BALANCE")).doubleValue();
                            if (objTxTransferTO.getProdType().equals(TransactionFactory.ADVANCES) || objTxTransferTO.getProdType().equals(TransactionFactory.AGRIADVANCES)) {
                                clBal = CommonUtil.convertObjToDouble(where.get("LOAN_PAID_INT")).doubleValue();
                                limit = CommonUtil.convertObjToDouble(where.get(LIMIT)).doubleValue();
                                dp = CommonUtil.convertObjToDouble(where.get("DRAWING_POWER")).doubleValue();
                                if (dp > 0 && dp < limit) {
                                    limit = dp;
                                }
                            }
                            ruleMap.put("LIMIT", String.valueOf(limit));
                            double amt = CommonUtil.convertObjToDouble(objTxTransferTO.getAmount()).doubleValue();
                            double tod_amt = CommonUtil.convertObjToDouble(where.get("TOD_AMOUNT")).doubleValue();
                            double tod_util = CommonUtil.convertObjToDouble(where.get("TOD_UTILIZED")).doubleValue();
                            double tod_left = tod_amt - tod_util;
                            where.put(TransactionDAOConstants.AMT, objTxTransferTO.getAmount());
                            Date toDt = (Date) properFormatDate.clone();
                            if (objTxTransferTO.getTransDt() != null) {
                                toDt.setDate(objTxTransferTO.getTransDt().getDate());
                                toDt.setMonth(objTxTransferTO.getTransDt().getMonth());
                                toDt.setYear(objTxTransferTO.getTransDt().getYear());
                                where.put(TODAY_DT, toDt);
                            }
                            //                                        where.put("TODAY_DT",objCashTrans.getTransDt());
                            where.put(CommonConstants.ACT_NUM, ACT_NUM);
                         //   System.out.println("For Updating Balances" + where);
                            double posBal = Math.abs(clBal);
                            String trans_type = CommonUtil.convertObjToStr(objTxTransferTO.getTransType());
                          //  System.out.println("posBal" + posBal);
                            List lst = sqlMap.executeQueryForList("getTypeOfTod", where);
                            HashMap hash = new HashMap();
                            if (lst != null && lst.size() > 0) {
                                hash = (HashMap) lst.get(0);
                                type_of_tod = CommonUtil.convertObjToStr(hash.get("TYPE_OF_TOD"));

                             //   System.out.println(" list size " + lst.size());
                                if (type_of_tod.equals("SINGLE")) {
                                //    System.out.println(" Inside SINGLE1 " + ruleMap);
                                    if (objTxTransferTO.getProdType().equals(TransactionFactory.OPERATIVE)) {
                                        if (clBal >= 0.0 && clBal <= amt && trans_type.equals(TransactionDAOConstants.DEBIT)) {
                                            where.put("TODUTILIZED", "");
                                        }
                                    }
                                    if (objTxTransferTO.getProdType().equals(TransactionFactory.ADVANCES) || objTxTransferTO.getProdType().equals(TransactionFactory.AGRIADVANCES)) {
                                        if (clBal >= 0.0 && clBal <= amt && trans_type.equals(TransactionDAOConstants.DEBIT)) {
                                            where.put("TODUTILIZEDAD", "");
                                        }
                                    }
                                    if (clBal >= 0.0 && clBal <= amt && trans_type.equals(TransactionDAOConstants.CREDIT)) {
                                        where.put("TODUTILIZEDCBMORE", "");
                                    }
                                    if (clBal < 0.0 && trans_type.equals(TransactionDAOConstants.DEBIT)) {
                                        where.put("TODUTILIZEDCBLESS", "");
                                    }
                                    if (clBal < 0.0 && trans_type.equals(TransactionDAOConstants.CREDIT)) {
                                        where.put("TODUTILIZEDCBMORE", "");
                                    }
                                    if (clBal > 0.0 && clBal > amt) {
                                        where.put("TODUTILIZEDCBMORE", "");
                                    }

                                    if (trans_type.equals(TransactionDAOConstants.CREDIT)) {
                                        //KD 370
                                        // Added by nithya on 30-01-2019 for KD 370 - For Savings Bank - Available Balance getting wrong.
                                        String sbODAct = "";
                                        HashMap inputMap = new HashMap();
                                        inputMap.put("ACCOUNTNO", objTxTransferTO.getActNum());
                                        List todAccInfoList = sqlMap.executeQueryForList("getMinBalance", inputMap);
                                        if (todAccInfoList != null && todAccInfoList.size() > 0 && objTxTransferTO.getProdType().equals(TransactionFactory.OPERATIVE)) {
                                            HashMap minMap = new HashMap();
                                            minMap = (HashMap) todAccInfoList.get(0);
                                            if (minMap.containsKey("TEMP_OD_ALLOWED") && CommonUtil.convertObjToStr(minMap.get("TEMP_OD_ALLOWED")).equalsIgnoreCase("Y")) {
                                                if (CommonUtil.convertObjToDouble(minMap.get("TOD_LIMIT")) >= 0) {
                                                    sbODAct = "Y";
                                                }
                                            }
                                        } 
                                        // End KD 370
                                        ruleMap.put("TOD_LEFT", String.valueOf(tod_left));
                                        ruleMap.put(LIMIT, String.valueOf(limit));
                                        System.out.println(" Inside CREDIT " + ruleMap);
                                        if (clBal < 0.0 && amt >= posBal) {
                                            if (sbODAct.equalsIgnoreCase("Y")) {// Added by nithya on 30-01-2019 for KD 370 - For Savings Bank - Available Balance getting wrong.
                                                ruleMap.put("NORMALSBOD", "");
                                            } else {
                                                ruleMap.put("GREATERAMTCREDIT", "");
                                            }
                                        } else if (clBal < 0.0 && amt < posBal) {
                                            if (sbODAct.equalsIgnoreCase("Y")) {// Added by nithya on 30-01-2019 for KD 370 - For Savings Bank - Available Balance getting wrong.
                                                ruleMap.put("NORMALSBOD", "");
                                            } else {
                                                ruleMap.put("LESSERAMTCREDIT", "");
                                            }
                                        } else if (clBal >= 0.0) {
                                            if (lst != null && lst.size() > 0) {
                                                if (objTxTransferTO.getProdType().equals(TransactionFactory.ADVANCES) || objTxTransferTO.getProdType().equals(TransactionFactory.AGRIADVANCES)) {
                                                    ruleMap.put("NORMALAD", "");
                                                } else {
                                                    if (sbODAct.equalsIgnoreCase("Y")) {// Added by nithya on 30-01-2019 for KD 370 - For Savings Bank - Available Balance getting wrong.
                                                        ruleMap.put("NORMALSBOD", "");
                                                    } else {
                                                        ruleMap.put("NORMAL", null);
                                                    }
                                                }
                                            } else {
                                                if (sbODAct.equalsIgnoreCase("Y")) {// Added by nithya on 30-01-2019 for KD 370 - For Savings Bank - Available Balance getting wrong.
                                                    ruleMap.put("NORMALSBOD", "");
                                                } else {
                                                    ruleMap.put("NORMAL", null);
                                                }
                                            }
                                            System.out.println("ruleMap Inside CREDIT" + ruleMap);
                                        }
                                    }
                                } else if (type_of_tod.equals("RUNNING")) {
                                 //   System.out.println(" Inside Running " + ruleMap);
                                    if (objTxTransferTO.getProdType().equals(TransactionFactory.OPERATIVE)) {
                                        if (clBal >= 0.0 && clBal <= amt && trans_type.equals(TransactionDAOConstants.DEBIT)) {
                                            where.put("TODUTILIZED", "");
                                        }
                                    } else if (objTxTransferTO.getProdType().equals(TransactionFactory.ADVANCES) || objTxTransferTO.getProdType().equals(TransactionFactory.AGRIADVANCES)) {
                                        if (clBal >= 0.0 && clBal <= amt && trans_type.equals(TransactionDAOConstants.DEBIT)) {
                                            where.put("TODUTILIZEDAD", "");
                                        }
                                    }
                                    if (clBal >= 0.0 && clBal <= amt && trans_type.equals(TransactionDAOConstants.CREDIT)) {
                                        where.put("TODUTILIZEDCBMORERUNNING", "");
                                    }
                                    if (clBal < 0.0 && trans_type.equals(TransactionDAOConstants.DEBIT)) {
                                        where.put("TODUTILIZEDCBLESS", "");
                                    }
                                    if (clBal < 0.0 && trans_type.equals(TransactionDAOConstants.CREDIT)) {
                                        where.put("TODUTILIZEDCBMORERUNNING", "");
                                    }
                                    if (clBal > 0.0 && clBal > amt) {
                                        where.put("TODUTILIZEDCBMORE", "");
                                    }
                                    if (trans_type.equals(TransactionDAOConstants.CREDIT)) {
                                        ruleMap.put("TOD_AMOUNT", String.valueOf(tod_amt));
                                        ruleMap.put("TOD_UTILIZED", String.valueOf(tod_util));
                                        ruleMap.put("TOD_LEFT", String.valueOf(tod_left));
                                        ruleMap.put("LIMIT", String.valueOf(limit));
                                        if (clBal < 0.0) {
                                            ruleMap.put("GREATERAMTCREDITRUNNING", "");
                                        } else if (clBal >= 0.0) {
                                            if (lst != null && lst.size() > 0) {
                                                if (objTxTransferTO.getProdType().equals(TransactionFactory.ADVANCES) || objTxTransferTO.getProdType().equals(TransactionFactory.AGRIADVANCES)) {
                                                    ruleMap.put("NORMALAD", "");
                                                } else {
                                                    ruleMap.put("NORMAL", "");
                                                }
                                            } else {
                                                ruleMap.put(NORMAL, "");
                                            }
                                        }
                                      //  System.out.println("ruleMap Inside CREDIT" + ruleMap);
                                    }
                                }
                                if (trans_type.equals(TransactionDAOConstants.DEBIT)) {
                                //    System.out.println(" Inside DEBIT " + ruleMap);
                                    if (lst != null && lst.size() > 0) {
                                        if (objTxTransferTO.getProdType().equals(TransactionFactory.ADVANCES) || objTxTransferTO.getProdType().equals(TransactionFactory.AGRIADVANCES)) {
                                            ruleMap.put("NORMALDEBITAD", "");
                                        } else {
                                            ruleMap.put("NORMALDEBIT", "");
                                        }
                                    } else {
                                        ruleMap.put(NORMALDEBIT, "");
                                    }
                                   // System.out.println("ruleMap Inside DEBIT" + ruleMap);
                                }
                            } else {
                       //         System.out.println(" Inside Common " + ruleMap);
                                if (trans_type.equals(TransactionDAOConstants.CREDIT)) {
                                    if (objTxTransferTO.getProdType().equals(TransactionFactory.ADVANCES) || objTxTransferTO.getProdType().equals(TransactionFactory.AGRIADVANCES)) {
                                        ruleMap.put("LIMIT", String.valueOf(limit));
                                        ruleMap.put("NORMALWOTOD", "");
                                    } else {
                                        
                                        // Added by nithya for 4743 SBOD on 07-09-2016
                                        HashMap inputMap = new HashMap();
                                        inputMap.put("ACCOUNTNO", objTxTransferTO.getActNum());
                                        List todAccInfoList = sqlMap.executeQueryForList("getMinBalance", inputMap);                                        
                                        if(todAccInfoList != null && todAccInfoList.size() > 0 && objTxTransferTO.getProdType().equals(TransactionFactory.OPERATIVE)){
                                            HashMap minMap = new HashMap();
                                            minMap = (HashMap) todAccInfoList.get(0);
                                            if (minMap.containsKey("TEMP_OD_ALLOWED") && CommonUtil.convertObjToStr(minMap.get("TEMP_OD_ALLOWED")).equalsIgnoreCase("Y")) {
                                                if(CommonUtil.convertObjToDouble(minMap.get("TOD_LIMIT")) >= 0){ // Added checking equal to by nithya on 30-01-2019 for KD 370 - For Savings Bank - Available Balance getting wrong.
                                                   ruleMap.put("NORMALSBOD", "");
                                                }else{
                                                   ruleMap.put(NORMAL, "");  
                                                }
                                            }else{
                                                ruleMap.put(NORMAL, "");
                                            }
                                        }else{                                        
                                         ruleMap.put(NORMAL, "");
                                       }
                                    }
                                } else {
                                    ruleMap.put("NORMALDEBIT", "");
                                }
                            }
                            System.out.println("updateTODUtilized" + where);
                            if (lst != null && lst.size() > 0 && (!loanParticulars.equals("CLEARING_BOUNCED"))) {
                                if (objTxTransferTO.getProdType().equals(TransactionFactory.OPERATIVE) || objTxTransferTO.getProdType().equals(TransactionFactory.ADVANCES) || objTxTransferTO.getProdType().equals(TransactionFactory.AGRIADVANCES)) {
                                    sqlMap.executeUpdate("updateTODUtilized", where);
                                }
                            }
                        }
                    } else {
                      //  System.out.println(" Inside CommonMain " + ruleMap);
                        if (objTxTransferTO.getTransType().equals(TransactionDAOConstants.CREDIT)) {
                            ruleMap.put("NORMAL", "");
                        } else {
                            ruleMap.put("NORMALDEBIT", "");
                        }
                    }
                    if (objTxTransferTO.getAuthorizeRemarks() != null && objTxTransferTO.getAuthorizeRemarks().equals("REVERSE_FLEXI")) {
                        ruleMap.remove(NORMAL);
                        ruleMap.put(NORMALDEBIT, "");
                    }
                    if (!objTxTransferTO.getProdType().equals("") && objTxTransferTO.getProdType().equals(TransactionFactory.OPERATIVE)
                            && objTxTransferTO.getActNum() != null && objTxTransferTO.getTransType().equals(TransactionDAOConstants.CREDIT)) {
                        HashMap cashMap = new HashMap();
                        HashMap flexiMap = new HashMap();
                        cashMap.put(LIEN_AC_NO, objTxTransferTO.getActNum());
                        double transAmt = CommonUtil.convertObjToDouble(objTxTransferTO.getAmount()).doubleValue();
                    //    System.out.println("cashMap " + cashMap);
                        List lstLien = sqlMap.executeQueryForList("getSelectReducingLienAmountDAO", cashMap);
                        if (lstLien != null && lstLien.size() > 0) {
                            for (int k = 0; k < lstLien.size(); k++) {
                                cashMap = (HashMap) lstLien.get(k);
                             //   System.out.println("cashMap " + cashMap + "SIZE" + lstLien.size());
                                double lienAmt = CommonUtil.convertObjToDouble(cashMap.get(LIEN_AMOUNT)).doubleValue();
                                flexiAmount = lienAmt;
                              //  System.out.println("cashMap " + lienAmt + "SIZE" + lstLien.size());
                                if (transAmt >= lienAmt) {
                                    lienAmt = 0;
                                    flexiMap.put(LIENAMOUNT, new Double(flexiAmount * -1));
                                    flexiMap.put("AVAILABLE_BALANCE", new Double(lienAmt));
                                    flexiMap.put(CommonConstants.STATUS, "UNLIENED");
                                  //  System.out.println("transAmt if " + transAmt);
                                   // System.out.println("lienAmt if " + lienAmt);
                                } else {
                                    flexiMap.put("LIENAMOUNT", new Double(transAmt * -1));
                                    flexiMap.put("STATUS", CommonConstants.STATUS_MODIFIED);
                                    flexiMap.put(LIEN_AMOUNT, new Double(lienAmt - transAmt));
                                    flexiMap.put("AVAILABLE_BALANCE", new Double(lienAmt - transAmt));
                                   // System.out.println("transAmt else " + transAmt);
                                    //System.out.println("lienAmt else " + lienAmt);
                                }
                                System.out.println("cashMap " + cashMap);
                                String lienNo = CommonUtil.convertObjToStr(cashMap.get("LIEN_NO"));
                                flexiMap.put("LIEN_NO", lienNo);
                                flexiMap.put("DEPOSIT_ACT_NUM", cashMap.get(DEPOSIT_NO));
                                flexiMap.put(SHADOWLIEN, new Double(0.0));
                                flexiMap.put(COMMAND, CommonConstants.TOSTATUS_UPDATE);
                                flexiMap.put("USER_ID", objTxTransferTO.getStatusBy());
                                flexiMap.put("LIEN_AC_NO", objTxTransferTO.getActNum());
                                flexiMap.put(AUTHORIZE_STATUS, CommonConstants.STATUS_AUTHORIZED);
                                flexiMap.put("AUTHORIZE_DATE", properFormatDate);
                                flexiMap.put(LIENNO, lienNo);
                                flexiMap.put("SUBNO", CommonUtil.convertObjToInt("1"));
                                flexiMap.put("UNLIEN_DT", properFormatDate);
                                flexiMap.put(CommonConstants.ACT_NUM, objTxTransferTO.getActNum());
                             //   System.out.println("flexiMap : " + flexiMap);
                                sqlMap.executeUpdate("updateSubAcInfoBal", flexiMap);
                                sqlMap.executeUpdate("updateForSBLienMarking", flexiMap);
                                if (lienAmt != 0) {
                                    sqlMap.executeUpdate("updateReducingLienAmountDAO", flexiMap);
                                }
                                lienAmt = CommonUtil.convertObjToDouble(cashMap.get(LIEN_AMOUNT)).doubleValue();
                                transAmt = transAmt - lienAmt;
                                if (transAmt < 0) {
                                    break;
                                }
                                System.out.println("Flexi Deposit Updation Completed...");
                            }
                        }
                        cashMap = null;
                        flexiMap = null;
                        lstLien = null;
                    }
                    if (map.containsKey("DB_DRIVER_NAME")) {
                        ruleMap.put("DB_DRIVER_NAME", map.get("DB_DRIVER_NAME"));
                    }
                    if(objTxTransferTO.getProdType().equals(TransactionFactory.ADVANCES) && 
                        objTxTransferTO.getTransType().equals(TransactionDAOConstants.DEBIT) && (objTxTransferTO.getInstrumentNo2()!=null && 
                        objTxTransferTO.getInstrumentNo2().equals("CHARGESUI"))){
                        ruleMap.put("CHARGESUI","CHARGESUI");
                    }
                  //  log.info("authorize ... objTransferTrans 666: " + objTxTransferTO.toString());
                    transModuleBased.authorizeUpdate(ruleMap, objTxTransferTO.getAmount());
                    amount = getTransAmount(objTxTransferTO);
                    //interbranch code
                    objLogTO.setInitiatedBranch(objTxTransferTO.getInitiatedBranch());
                    //end
                  //  System.out.println("map#####INTER_BRANCH_TRANS" + map);
                    if (map.containsKey("INTER_BRANCH_TRANS")
                            && new Boolean(CommonUtil.convertObjToStr(map.get("INTER_BRANCH_TRANS"))).booleanValue() && isInterBranch) {
                        ruleMap.put("IS_INTER_BRANCH_TRANS", map.get("INTER_BRANCH_TRANS"));
                        ruleMap.put("INTER_BRANCH", new Boolean(isInterBranch));
                        ruleMap.put("ACTUAL_TRANSFER_TRANS", "TRANSFER");
                      //  System.out.println("######TransferDAO ruelMap" + ruleMap);
                        transModuleBased.updateGL(objTxTransferTO.getAcHdId(), amount, objLogTO, ruleMap);
                        objTxTransferTO.setTransDt(properFormatDate);  
                        //added by nithya on 07-11-2022 for KD-3436
                        if(map.containsKey("BACK_DATED_TRANSACTION") && map.get("BACK_DATED_TRANSACTION") != null && map.containsKey("TRANS_DATE") && map.get("TRANS_DATE") != null){
                            objTxTransferTO.setTransDt(getProperDateFormat(map.get("TRANS_DATE")));
                        }
                        sqlMap.executeUpdate("insertInterBranchTxTransferTO", objTxTransferTO);     
						insertDayEndBalance(objTxTransferTO);
                    } else {
                      //  System.out.println("######TransferDAO ruelMap" + ruleMap);
                        HashMap updateMap = getRuleMap(objTxTransferTO, 0.0, true);
                        updateMap.put("INTER_BRANCH", new Boolean(isInterBranch));
                        transModuleBased.updateGL(objTxTransferTO.getAcHdId(), amount, objLogTO, updateMap);
                        if(map.containsKey("LOGGED_BRANCH_ID") && map.containsKey("MDS_BONUS_ENTRY")){
                            HashMap diffBrMap = new HashMap();
                            diffBrMap.put("TRANS_ID",objTxTransferTO.getTransId());
                            diffBrMap.put("AC_HD_ID",objTxTransferTO.getAcHdId());
                            diffBrMap.put("ACT_NUM",objTxTransferTO.getActNum());
                            diffBrMap.put("TRANS_DT",objTxTransferTO.getTransDt());
                            diffBrMap.put("AMOUNT",objTxTransferTO.getAmount());
                            diffBrMap.put("TRANS_TYPE",objTxTransferTO.getTransType());
                            diffBrMap.put("PROD_ID",objTxTransferTO.getProdId());
                            diffBrMap.put("PROD_TYPE",objTxTransferTO.getProdType());
                            diffBrMap.put("BRANCH_ID",objTxTransferTO.getBranchId());
                            diffBrMap.put("INITIATED_BRANCH",objTxTransferTO.getInitiatedBranch());
                            diffBrMap.put("OTHER_BRANCH_DT",objTxTransferTO.getTransDt());
                            diffBrMap.put("LOGGED_IN_BRANCH",map.get("LOGGED_BRANCH_ID"));
                            sqlMap.executeUpdate("insertDiffBranchTxTransferTO", diffBrMap);
                        }
                    }
                    updatepassbook(objTxTransferTO);
                    if (objTxTransferTO.getTransType().equals(TransactionDAOConstants.CREDIT) && objTxTransferTO.getProdType().equals("TD")
                            && map.containsKey("DEPOSIT_PENAL_AMT")) {
                        HashMap prodMap = new HashMap();
                        prodMap.put(PROD_ID, objTxTransferTO.getProdId());
                        double penalMonth = CommonUtil.convertObjToDouble(map.get("DEPOSIT_PENAL_MONTH")).doubleValue();
                        double penalAmt = CommonUtil.convertObjToDouble(map.get(DEPOSIT_PENAL_AMT)).doubleValue();
                        List lstProd = sqlMap.executeQueryForList("getBehavesLikeForDeposit", prodMap);
                        if (lstProd != null && lstProd.size() > 0) {
                            prodMap = (HashMap) lstProd.get(0);
                            if (prodMap.get(BEHAVES_LIKE).equals("RECURRING") && penalAmt > 0) {
                                String act_Num = objTxTransferTO.getActNum();
                                if (act_Num.lastIndexOf("_") != -1) {
                                    act_Num = act_Num.substring(0, act_Num.lastIndexOf("_"));
                                }
                                HashMap penalMap = new HashMap();
                                penalMap.put(DEPOSIT_NO, act_Num);
                                /*penalMap.put("DELAYED_MONTH", new Double(penalMonth));
                                penalMap.put("DELAYED_AMOUNT", new Double(penalAmt));*/
                                //Changed By Kannan AR
//                                penalMap.put("DELAYED_MONTH", String.valueOf(penalMonth));
//                                penalMap.put("DELAYED_AMOUNT", String.valueOf(penalAmt));
                                penalMap.put("DELAYED_MONTH", CommonUtil.convertObjToDouble(penalMonth));
                                penalMap.put("DELAYED_AMOUNT", CommonUtil.convertObjToDouble(penalAmt));
                                sqlMap.executeUpdate("updateDepositPenalAmount", penalMap);

                                HashMap updateMap = new HashMap();
                                updateMap.put("INSTRUMENT_NO2", objTxTransferTO.getInstrumentNo2());
                                updateMap.put("BATCH_ID", objTxTransferTO.getBatchId());
                                updateMap.put("TRANS_DT", currDate.clone());
                                updateMap.put("INITIATED_BRANCH", _branchCode);
                                sqlMap.executeUpdate("updateDepTransPenalMakingNull", updateMap);

                            }
                        }
                    }
                    
                    // Added by nithya on 21-11-2018 for KD 340 - Group deposit receipt via cash & Transfer-screen : entry not inserted daily deposit trans details table
                    if (map.containsKey("CREDIT_TO_DEPOSIT_TRANSFER_SCREEN") && map.get("CREDIT_TO_DEPOSIT_TRANSFER_SCREEN")!= null && CommonUtil.convertObjToStr("CREDIT_TO_DEPOSIT_TRANSFER_SCREEN").equalsIgnoreCase("CREDIT_TO_DEPOSIT_TRANSFER_SCREEN") && objTxTransferTO.getProdType().equals(TransactionFactory.DEPOSITS) && objTxTransferTO.getTransType().equals(TransactionDAOConstants.CREDIT)) {
                        HashMap prodMap = new HashMap();
                        prodMap.put(PROD_ID, objTxTransferTO.getProdId());
                        List lstProd = sqlMap.executeQueryForList("getBehavesLikeForDeposit", prodMap);
                        if (lstProd != null && lstProd.size() > 0) {
                            prodMap = (HashMap) lstProd.get(0);
                            if (prodMap.get(BEHAVES_LIKE).equals("DAILY")) {
                                 //System.out.println("map here---- :: " + map);
                                  String agentId = "";
                                 if (map != null && map.containsKey("DAILY_DEPOSIT_AGENT_ID") && map.get("DAILY_DEPOSIT_AGENT_ID") != null) {
                                    agentId = CommonUtil.convertObjToStr(map.get("DAILY_DEPOSIT_AGENT_ID"));
                                }
//                                HashMap checkMap = new HashMap();
//                                checkMap.put(PROD_ID, objTxTransferTO.getProdId());
//                                List groupDepositProdList = sqlMap.executeQueryForList("getIsGroupDepositProduct", checkMap);
//                                if (groupDepositProdList != null && groupDepositProdList.size() > 0) {
//                                    HashMap groupDepositProdMap = (HashMap) groupDepositProdList.get(0);
//                                    if (groupDepositProdMap != null && groupDepositProdMap.containsKey("IS_GROUP_DEPOSIT") && groupDepositProdMap.get("IS_GROUP_DEPOSIT") != null) {
//                                        if (CommonUtil.convertObjToStr(groupDepositProdMap.get("IS_GROUP_DEPOSIT")).equalsIgnoreCase("Y")) {
                                            DailyDepositTransTO dailyTO = new DailyDepositTransTO();
                                            dailyTO.setBatch_id(objTxTransferTO.getBatchId());
                                            dailyTO.setTrn_dt(currDate);
                                            dailyTO.setColl_dt(currDate);
                                            //dailyTO.setAgent_no("");
                                            dailyTO.setAgent_no(agentId);
                                            dailyTO.setAcct_num(objTxTransferTO.getActNum());
                                            dailyTO.setTrans_mode("TRANSFER");
                                            dailyTO.setTrans_type("CREDIT");
                                            dailyTO.setAmount(objTxTransferTO.getAmount());
                                            dailyTO.setProd_Type(objTxTransferTO.getProdId());
                                            dailyTO.setScreenName("Transfer");
                                            dailyTO.setCreated_by(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                            dailyTO.setCreated_dt(currDate);
                                            dailyTO.setAuthorize_by(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                            dailyTO.setAuthorize_dt(currDate);
                                            dailyTO.setInitiatedBranch(objTxTransferTO.getInitiatedBranch());
                                            dailyTO.setAuthorize_status("AUTHORIZED");
                                            dailyTO.setStatus("CREATED");
                                            sqlMap.executeUpdate("INSERTINTODAILYDEPOSIT", dailyTO);
                                       // }
                                   // }
                               // }
                            }
                        }
                    }
                                // End
                    
                    //                    if(objTxTransferTO.getProdType().equals(TransactionFactory.DEPOSITS)){
                    //                    if(objTxTransferTO.getInstrumentNo1()!= null && objTxTransferTO.getInstrumentNo1().equals("Payable")&& !objTxTransferTO.getLinkBatchId().equals("")){
                    //                        HashMap depIntPayMap=new HashMap();
                    //                        depIntPayMap.put("ACCT_NUM",objTxTransferTO.getLinkBatchId());
                    //                        List dpLst =sqlMap.executeQueryForList("getDepositIntPayableDET",depIntPayMap);
                    //                        if(dpLst!=null && dpLst.size()>0){
                    //                            depIntPayMap=(HashMap)dpLst.get(0);
                    //                            InterestBatchTO dpIntTO =new InterestBatchTO();
                    //                            dpIntTO.setIntDt(currDate);
                    //                            dpIntTO.setAcHdId(objTxTransferTO.getAcHdId());
                    //                            dpIntTO.setApplDt(currDate);
                    //                            dpIntTO.setIntAmt(objTxTransferTO.getAmount());
                    //                            dpIntTO.setProductId(CommonUtil.convertObjToStr(depIntPayMap.get(PROD_ID)));
                    //                            dpIntTO.setProductType("TD");
                    //                            dpIntTO.setActNum(objTxTransferTO.getLinkBatchId());
                    //                            dpIntTO.setTrnDt(currDate);
                    //                            double tot_int_cr=CommonUtil.convertObjToDouble(depIntPayMap.get("TOTAL_INT_CREDIT")).doubleValue();
                    //                            double tot_int_dr=CommonUtil.convertObjToDouble(depIntPayMap.get("TOTAL_INT_DRAWN")).doubleValue();
                    //                            double diffAmt=0.0;
                    //                            diffAmt=tot_int_cr-tot_int_dr;
                    //                            depIntPayMap.put("actNum",objTxTransferTO.getLinkBatchId());
                    //                            depIntPayMap.put(CommonConstants.ACT_NUM,objTxTransferTO.getLinkBatchId());
                    //                            depIntPayMap.put("intAmt",objTxTransferTO.getAmount());
                    //                            depIntPayMap.put("INT_AMT",objTxTransferTO.getAmount());
                    //
                    //                            double depAmt = CommonUtil.convertObjToDouble(depIntPayMap.get("DEPOSIT_AMT")).doubleValue();
                    //                            dpIntTO.setPrincipleAmt(new Double(depAmt));
                    //                            dpIntTO.setCustId(CommonUtil.convertObjToStr(depIntPayMap.get("CUST_ID")));
                    //                            if(objTxTransferTO.getTransType().equals(TransactionDAOConstants.CREDIT)){
                    //                                dpIntTO.setDrCr(TransactionDAOConstants.CREDIT);
                    //                                dpIntTO.setTransLogId("Payable");
                    //                                dpIntTO.setIntAmt(objTxTransferTO.getAmount());
                    //                                diffAmt=diffAmt+CommonUtil.convertObjToDouble(objTxTransferTO.getAmount()).doubleValue();
                    //                                dpIntTO.setTot_int_amt(new Double(diffAmt));
                    //                                sqlMap.executeUpdate("insertDepositInterestTO", dpIntTO);
                    //                                sqlMap.executeUpdate("updateBalIntCreditAmount", depIntPayMap);
                    //                            }else{
                    //                                dpIntTO.setDrCr(TransactionDAOConstants.DEBIT);
                    //                                dpIntTO.setTransLogId("Payable");
                    //                                dpIntTO.setIntAmt(objTxTransferTO.getAmount());
                    //                                diffAmt=diffAmt-CommonUtil.convertObjToDouble(objTxTransferTO.getAmount()).doubleValue();
                    //                                dpIntTO.setTot_int_amt(new Double(diffAmt));
                    //                                sqlMap.executeUpdate("insertDepositInterestTO", dpIntTO);
                    //                                sqlMap.executeUpdate("updateTotIndDrawnAmount", depIntPayMap);
                    //                            }
                    //                        }
                    //                      depIntPayMap=null;
                    //                    }
                    //                }
                }
                // Checking for Rejected Status
            } else if (status.equalsIgnoreCase(CommonConstants.STATUS_REJECTED)) {
                // Exisiting status is Created or Modified
                HashMap revertPenalAmtAndMon = new HashMap();
                dataMap.put("SHIFT", "");
                if (objTxTransferTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED)
                        || objTxTransferTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_MODIFIED)) {

                    transModuleBased.performShadowMinus(getRuleMap(objTxTransferTO, 0.0, false));
                    //transModuleBased.performOtherBalanceMinus(getRuleMap(objTxTransferTO, 0.0, true));
                    revertPenalAmtAndMon.put("DEPOSIT_NO", objTxTransferTO.getActNum());
                    List lstDep = sqlMap.executeQueryForList("getPenalAmtForReverse", revertPenalAmtAndMon);
                    if (lstDep != null && lstDep.size() > 0) {
                        HashMap PenalMapReverse = (HashMap) lstDep.get(0);
                        revertPenalAmtAndMon.put("DELAYED_MONTH", PenalMapReverse.get("DELAYED_MONTH"));
                        revertPenalAmtAndMon.put("DELAYED_AMOUNT", PenalMapReverse.get("DELAYED_AMOUNT"));
                     //   System.out.println("*** revertPenalAmtAndMon 2: " + revertPenalAmtAndMon);
                        sqlMap.executeUpdate("UpdatePenalAmtAfterReverse", revertPenalAmtAndMon);
                    }

                } else {

                    transModuleBased.performShadowAdd(getRuleMap(objTxTransferTO, 0.0, true));
                    //transModuleBased.performOtherBalanceAdd(getRuleMap(objTxTransferTO, 0.0, true));

                    objTxTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objTxTransferTO.setTransDt(currDate);
                    objTxTransferTO.setInitiatedBranch(_branchCode);
                    sqlMap.executeUpdate("rejectTransferTransaction", objTxTransferTO);

                }
            }
            if (!objTxTransferTO.getTransId().equals("") && objTxTransferTO.getTransId().length() > 0
                    && objTxTransferTO.getActNum() != null && objTxTransferTO.getTransType().equals(TransactionDAOConstants.DEBIT)) {
                HashMap cashMap = new HashMap();
                cashMap.put(BATCH_ID, objTxTransferTO.getBatchId());
             //   System.out.println("cashMap " + cashMap);
                List lstLien = sqlMap.executeQueryForList("getSBLienTransferAccountNo", cashMap);
                if (lstLien != null && lstLien.size() > 0) {
                    for (int j = 0; j < lstLien.size(); j++) {
                        cashMap = (HashMap) lstLien.get(j);
                    //    System.out.println("cashMap " + cashMap);
                        HashMap flexiMap = new HashMap();
                        String lienNo = CommonUtil.convertObjToStr(cashMap.get("LIEN_NO"));
                        flexiMap.put("DEPOSIT_ACT_NUM", cashMap.get(DEPOSIT_NO));
                        flexiMap.put(SHADOWLIEN, new Double(CommonUtil.convertObjToDouble(cashMap.get(LIEN_AMOUNT)).doubleValue()));
                        flexiMap.put("USER_ID", objTxTransferTO.getStatusBy());
                        flexiMap.put("LIEN_AC_NO", objTxTransferTO.getActNum());
                        flexiMap.put("AUTHORIZE_DATE", properFormatDate);
                        flexiMap.put(LIENNO, lienNo);
                        flexiMap.put("SUBNO", CommonUtil.convertObjToInt("1"));
                        flexiMap.put(CommonConstants.ACT_NUM, objTxTransferTO.getActNum());
                        flexiMap.put(AUTHORIZE_STATUS, status);
                        if (status.equals(CommonConstants.STATUS_AUTHORIZED)) {
                            flexiMap.put("LIENAMOUNT", CommonUtil.convertObjToDouble(cashMap.get(LIEN_AMOUNT)));
                            flexiMap.put("STATUS", objTxTransferTO.getStatus());
                        } else if (status.equals(CommonConstants.STATUS_REJECTED)) {
                            flexiMap.put("LIENAMOUNT", new Double(0.0));
                            flexiMap.put("LIEN_NO", lienNo);
                            flexiMap.put(LIEN_AMOUNT, String.valueOf(CommonUtil.convertObjToDouble(cashMap.get(LIEN_AMOUNT)).doubleValue()));
                            flexiMap.put("UNLIEN_DT", properFormatDate);
                            flexiMap.put(CommonConstants.STATUS, CommonConstants.STATUS_DELETED);
                            sqlMap.executeUpdate("updateReducingLienAmountDAO", flexiMap);
                        }
                       // System.out.println("flexiMap : " + flexiMap);
                        sqlMap.executeUpdate("updateSubAcInfoBal", flexiMap);
                        sqlMap.executeUpdate("updateForSBLienMarking", flexiMap);
                        flexiMap = null;
                    }
                    cashMap = null;
                }
            }
            if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED) && !objTxTransferTO.getTransId().equals("")
                    && objTxTransferTO.getTransId().length() > 0) {//reconciliation..
                HashMap reconcileAuthMap = new HashMap();//transaction authorizing
                reconcileAuthMap.put("PRESENT_TRANS_ID", objTxTransferTO.getTransId());
                reconcileAuthMap.put("PRESENT_TRANS_DT", properFormatDate);
                List lst = sqlMap.executeQueryForList("getSelectEditModeReconciliationTransaction", reconcileAuthMap);
                if (lst != null && lst.size() > 0) {
                    for (int k = 0; k < lst.size(); k++) {
                        reconcileAuthMap = (HashMap) lst.get(k);
                        double presentAmt = 0.0;
                        double balance = 0.0;
                        presentAmt = CommonUtil.convertObjToDouble(reconcileAuthMap.get(PRESENT_AMOUNT)).doubleValue();
                        balance = CommonUtil.convertObjToDouble(reconcileAuthMap.get("BALANCE_AMOUNT")).doubleValue();
                        double tempAmt = balance - presentAmt;
                        reconciliationTO = new ReconciliationTO();// it will store into
                        reconciliationTO.setTransId(CommonUtil.convertObjToStr(reconcileAuthMap.get(CommonConstants.TRANS_ID)));
                        reconciliationTO.setBatchId(CommonUtil.convertObjToStr(reconcileAuthMap.get(BATCH_ID)));
                        reconciliationTO.setAcHdId(CommonUtil.convertObjToStr(reconcileAuthMap.get("AC_HD_ID")));
                        reconciliationTO.setTransAmount(new Double(presentAmt));
                        reconciliationTO.setReconcileAmount(new Double(presentAmt));
                        reconciliationTO.setBalanceAmount(new Double(tempAmt));
                        reconciliationTO.setStatus(CommonConstants.STATUS_CREATED);
                        reconciliationTO.setStatusBy(objTxTransferTO.getStatusBy());
                        reconciliationTO.setStatusDt(properFormatDate);
                        reconciliationTO.setReconcileTransId(CommonUtil.convertObjToStr(reconcileAuthMap.get(PRESENT_TRANS_ID)));
                        reconciliationTO.setReconcileBatchId(CommonUtil.convertObjToStr(reconcileAuthMap.get(PRESENT_BATCH_ID)));
                        reconciliationTO.setTransMode(CommonUtil.convertObjToStr(reconcileAuthMap.get("TRANS_MODE")));
                        reconciliationTO.setInitiatedBranch(objTxTransferTO.getInitiatedBranch());
                        reconciliationTO.setBranchId(objTxTransferTO.getBranchId());
                        reconciliationTO.setTransType(CommonUtil.convertObjToStr(reconcileAuthMap.get("TRANS_TYPE")));
                        reconciliationTO.setTransDt(properFormatDate);
                        reconciliationTO.setAuthorizeStatus(String.valueOf(status));
                        reconciliationTO.setAuthorizeDt(properFormatDate);
                        reconciliationTO.setAuthorizeBy(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.USER_ID)));
                        reconciliationTO.setRecTranDt(properFormatDate);
                      //  System.out.println("reconciliationTO :" + reconciliationTO);
                        sqlMap.executeUpdate("insertReconciliationTO", reconciliationTO);
                        reconciliationTO = null;
                        HashMap presentMap = new HashMap();
                        presentMap.put(PRESENT_TRANS_ID, "");
                        presentMap.put(PRESENT_BATCH_ID, "");
                        presentMap.put(PRESENT_AMOUNT, new Double(0));
                        presentMap.put(BATCH_ID, reconcileAuthMap.get(BATCH_ID));
                        presentMap.put(CommonConstants.TRANS_ID, reconcileAuthMap.get(CommonConstants.TRANS_ID));
                        presentMap.put("RECONCILE_AMOUNT", new Double(presentAmt));
                        presentMap.put("BALANCE_AMOUNT", new Double(tempAmt));
                        presentMap.put("TRANS_DT", currDate.clone());
                        presentMap.put("INITIATED_BRANCH", _branchCode);
                     //   System.out.println("presentMap :" + presentMap);
                        sqlMap.executeUpdate("updateBalanceAmountTrans", presentMap);
                        presentMap = null;
                    }
                }
                lst = null;
                reconcileAuthMap = null;
            } else if (status.equalsIgnoreCase(CommonConstants.STATUS_REJECTED) && !objTxTransferTO.getTransId().equals("")
                    && objTxTransferTO.getTransId().length() > 0) {
                HashMap reconcileAuthMap = new HashMap();
                reconcileAuthMap.put("PRESENT_TRANS_ID", objTxTransferTO.getTransId());
//                reconcileAuthMap.put("PRESENT_TRANS_DT",objTxTransferTO.getTransDt());
//                 properFormatDate = objTxTransferTO.getTransDt();
              //  System.out.println("txTransferTO.getTransDt()#### :" + objTxTransferTO.getTransDt());
                Date trDt = (Date) properFormatDate.clone();
                trDt.setDate(objTxTransferTO.getTransDt().getDate());
                trDt.setMonth(objTxTransferTO.getTransDt().getMonth());
                trDt.setYear(objTxTransferTO.getTransDt().getYear());
//                                    reconcileAuthMap.put("TRANS_DT",trDt);
                reconcileAuthMap.put("PRESENT_TRANS_DT", trDt);
            //    System.out.println("reconcileAuthMapsDt()#### :" + reconcileAuthMap);
            //    System.out.println("DDSDSSDFSDF#$#$$%" + reconcileAuthMap);
                List lst = sqlMap.executeQueryForList("getSelectAllinDeleteMode", reconcileAuthMap);
                if (lst != null && lst.size() > 0) {
                    for (int k = 0; k < lst.size(); k++) {
                        reconcileAuthMap = (HashMap) lst.get(k);
                        HashMap presentMap = new HashMap();
                        presentMap.put("PRESENT_TRANS", "");
                        presentMap.put("PRESENT_BATCH", "");
                        presentMap.put("PRESENT_AMOUNT", new Double(0));
                        presentMap.put("PRESENT_TRANS_ID", reconcileAuthMap.get("PRESENT_TRANS_ID"));
                        presentMap.put("PRESENT_TRANS_DT", reconcileAuthMap.get("PRESENT_TRANS_DT"));
                        System.out.println("presentMap :" + presentMap);
                        sqlMap.executeUpdate("deleteOldReconcileAmt", presentMap);
                    }
                }
            }
            if (!objTxTransferTO.getTransId().equals("") && objTxTransferTO.getTransId().length() > 0) {
                HashMap reconcileAuthMap = new HashMap();//transaction authorizing
                reconcileAuthMap.put("BATCH_ID", objTxTransferTO.getBatchId());
                reconcileAuthMap.put("TRANS_DT", currDate.clone());
                reconcileAuthMap.put("INITIATED_BRANCH", _branchCode);
                List lst = sqlMap.executeQueryForList("getSelectAuthorizeListReconcile", reconcileAuthMap);
                if (lst != null && lst.size() > 0) {
                    reconcileAuthMap.put(AUTHORIZE_STATUS, status);
                    reconcileAuthMap.put("AUTHORIZE_BY", objLogTO.getUserId());
                    reconcileAuthMap.put("AUTHORIZE_DT", properFormatDate);
                    reconcileAuthMap.put("TRANS_DT", currDate.clone());
                    reconcileAuthMap.put("INITIATED_BRANCH", _branchCode);
                    sqlMap.executeUpdate("updateReconcileAuthorize", reconcileAuthMap);
                }
            }
            objLogTO.setData(objTxTransferTO.toString());
            objLogTO.setPrimaryKey(objTxTransferTO.getTransId());
            objLogTO.setStatus(status);

            objLogDAO.addToLog(objLogTO);
        }
        dataMap.put("TRANS_DT", currDate.clone());
        dataMap.put("INITIATED_BRANCH", _branchCode);
        if (map.containsKey("INVESTMENT_CONTRA")) {
            dataMap.put("REMARKS", "INVESTMENT_CONTRA");
        }
        //Added By Suresh       BackDated Transaction       //Setting Transaction Date
        if (map.containsKey("BACK_DATED_TRANSACTION")) {
         ////   System.out.println("######### BACK_DATED_TRANSACTION ");
            dataMap.put("TODAY_DT", map.get("TRANS_DATE"));
            dataMap.put("REMARKS", "BACK_DATED_TRANSACTION");
            Date tempDt = (Date) properFormatDate.clone();
            Date transDt = (Date) map.get("TRANS_DATE");
            tempDt.setDate(transDt.getDate());
            tempDt.setMonth(transDt.getMonth());
            tempDt.setYear(transDt.getYear());
            dataMap.put("TRANS_DT", tempDt);
        }
    //    System.out.println("dataMap");
        sqlMap.executeUpdate("updateMasterTransferTO", dataMap);
        HashMap closingMap = new HashMap();
        if(map.containsKey("LOAN_CLOSING_SCREEN")){
            closingMap.put("LOAN_CLOSING_SCREEN","LOAN_CLOSING_SCREEN");
        }
        if (!map.containsKey("LOAN_TRANS_OUT")) {
            if(map.containsKey("VOUCHER_RELEASE")) {
                closingMap.put("VOUCHER_RELEASE", CommonUtil.getProperDate(currDate, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("VOUCHER_RELEASE")))));
            }
            if (map.containsKey("REC_PRINCIPAL")) {
                closingMap.put("REC_PRINCIPAL", CommonUtil.convertObjToDouble(map.get("REC_PRINCIPAL")));
            }
             if (map.containsKey("DEDUCTION_SI")) {
                  closingMap.put("DEDUCTION_SI", map.get("DEDUCTION_SI"));
             }
            closingMap.put("USER_ID", map.get("USER_ID"));
            asAnCustomerCreditLoanAdvances(selectedList, status, closingMap);
        }

        //Added By Suresh R Back Dated Transaction        Updating GL_ABSTRACT table Opening And Closing Balance
        if (map.containsKey("BACK_DATED_TRANSACTION") && status.equals(CommonConstants.STATUS_AUTHORIZED)) {
        //    System.out.println("######### BACK_DATED_TRANSACTION Updating GL_ABSTRACT table Opening And Closing Balance(List size): " + size);
            for (int i = 0; i < size; i++) {
                objTxTransferTO = (TxTransferTO) selectedList.get(i);
               // System.out.println("############# objTxTransferTO : " + i + " : " + objTxTransferTO);
                HashMap whereMap = new HashMap();
                whereMap.put("AC_HD_ID", CommonUtil.convertObjToStr(objTxTransferTO.getAcHdId()));
                whereMap.put("BRANCH_CODE", CommonUtil.convertObjToStr(objTxTransferTO.getBranchId()));
                whereMap.put("AMOUNT", CommonUtil.convertObjToDouble(objTxTransferTO.getAmount()));
                whereMap.put("FROM_DT", dataMap.get("TRANS_DT"));
                whereMap.put("TO_DT", properFormatDate);
                whereMap.put("TRANS_TYPE", CommonUtil.convertObjToStr(objTxTransferTO.getTransType()));
            //    System.out.println("############# whereMap : " + whereMap);
                sqlMap.executeQueryForList("updateGL_Abstract", whereMap);
                if(objTxTransferTO.getProdType().equals(CommonConstants.TXN_PROD_TYPE_OPERATIVE)){
                    whereMap.put("ACT_NUM",objTxTransferTO.getActNum());
                    sqlMap.executeUpdate("updateBackDatedDayEndBalance", whereMap);
                    sqlMap.executeUpdate("updateBackDatedPassbookBalance", whereMap);
                }
                 else if(objTxTransferTO.getProdType().equals(CommonConstants.TXN_PROD_TYPE_SUSPENSE)){
                      whereMap.put("ACT_NUM",objTxTransferTO.getActNum());
                    sqlMap.executeUpdate("updateBackDatedSADayEndBalance", whereMap);
        }
            }
        }

        // The following block added by Rajesh for sending SMS alerts
        String mobieBanking = readFromTemplate("MOBILE_BANKING");
        if (mobieBanking != null && CommonUtil.convertObjToStr(mobieBanking).equals("Y")) {
            String mobieBankingUserName = readFromTemplate("MOBILE_BANKING_USERNAME");
            String mobieBankingPwd = readFromTemplate("MOBILE_BANKING_PWD");
            String mobieBankingSenderId = readFromTemplate("MOBILE_BANKING_SENDERID");
            String smsServer = readFromTemplate("SMS_SERVER");
            String bankSMSDescription = readFromTemplate("BANK_SMS_DESCRIPTION");
            if (CommonUtil.convertObjToStr(mobieBankingUserName).length() > 0
                    && CommonUtil.convertObjToStr(mobieBankingPwd).length() > 0
                    && CommonUtil.convertObjToStr(mobieBankingSenderId).length() > 0) {
                if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                    SmsConfigDAO smsDAO = new SmsConfigDAO();
                    List lst = selectedList;
                    String prodType = "";
                    String prodId = "";
                    String acctNum = "";
                    double crAmount = 0;
                     //Added By Kannan AR DEPOSIT_MULTIPLE_RENEWAL key available sms will be send manually using multiple msg screen 
                    if (map.containsKey("DEPOSIT_MULTIPLE_RENEWAL") && lst != null && lst.size() > 0) {
                        String DepNum = "";
                        for (int m = 0; m < lst.size(); m++) {
                            objTxTransferTO = (TxTransferTO) lst.get(m);
                            if (CommonUtil.convertObjToStr(objTxTransferTO.getTransType()).equals("CREDIT") && CommonUtil.convertObjToStr(objTxTransferTO.getProdType()).equals(TransactionFactory.DEPOSITS)) {
                                HashMap custMap = new HashMap();
                                DepNum = CommonUtil.convertObjToStr(objTxTransferTO.getActNum());
                                if (DepNum.lastIndexOf("_") != -1) {                                    
                                    DepNum = DepNum.substring(0, DepNum.lastIndexOf("_"));
                                }                                
                                custMap.put("DEPOSIT_NO", DepNum);
                                List custList = sqlMap.executeQueryForList("getSelectPanNoandPrintingNo", custMap);
                                if (custList != null && custList.size() > 0) {
                                    custMap = (HashMap) custList.get(0);
                                    HashMap multipletoSingleMap = new HashMap();
                                    multipletoSingleMap.put("CUST_ID", CommonUtil.convertObjToStr(custMap.get("CUST_ID")));
                                    multipletoSingleMap.put("ACT_NUM", DepNum);
                                    multipletoSingleMap.put("STATUS_BY", CommonUtil.convertObjToStr(objTxTransferTO.getStatusBy()));
                                    multipletoSingleMap.put("TRANS_DT", currDate.clone());
                                    multipletoSingleMap.put("STATUS", CommonConstants.STATUS_CREATED);
                                    multipletoSingleMap.put("INT_AMT", objTxTransferTO.getAmount());
                                    multipletoSingleMap.put("SCREEN_NAME","DEPOSIT_MULTIPLE_RENEWAL");
                                    sqlMap.executeUpdate("insertMultipletoSingleMSGDeliver", multipletoSingleMap);
                                }
                            }
                        }
                    } else {
                    for (int j = 0; j < lst.size(); j++) {
                        objTxTransferTO = (TxTransferTO) lst.get(j);
                        if (objTxTransferTO.getProdType().equals(TransactionFactory.LOANS)
                                && objTxTransferTO.getTransType().equals("CREDIT")) {//This line added because if TransType is CREDIT then only should set ProdType & AcctNum otherwise it's not inserting record into LOAN_TRANS_DETAILS table
                            prodType = TransactionFactory.LOANS;
                            acctNum = objTxTransferTO.getActNum();
                        }
                        if (objTxTransferTO.getProdType().equals(TransactionFactory.ADVANCES)
                                && objTxTransferTO.getTransType().equals("CREDIT")) {//This line added because if TransType is CREDIT then only should set ProdType & AcctNum otherwise it's not inserting record into ADV_TRANS_DETAILS table
                            prodType = TransactionFactory.ADVANCES;
                            acctNum = objTxTransferTO.getActNum();
                        }
                        if (objTxTransferTO.getProdType().equals(TransactionFactory.DEPOSITS)
                                && objTxTransferTO.getTransType().equals("CREDIT")) {//This line added because if TransType is CREDIT then only should set ProdType & AcctNum 
                            prodType = TransactionFactory.DEPOSITS;
                            acctNum = objTxTransferTO.getActNum();
                        }
                        if (objTxTransferTO.getTransType().equals("CREDIT")) {
                            crAmount += objTxTransferTO.getAmount().doubleValue();
                        }
                    }
                    for (int j = 0; j < lst.size(); j++) {
                        objTxTransferTO = (TxTransferTO) lst.get(j);
                        HashMap interestMap = new HashMap();
                        HashMap getDateMap = new HashMap();
                        if (objTxTransferTO.getProdType().equals(TransactionFactory.LOANS) || objTxTransferTO.getProdType().equals(TransactionFactory.ADVANCES) || objTxTransferTO.getProdType().equals(TransactionFactory.GL)) {
                            List list = null;
                            if (acctNum != null && acctNum.length() > 0) {
                                interestMap.put(CommonConstants.ACT_NUM, acctNum);
                            } else {
                                interestMap.put(CommonConstants.ACT_NUM, objTxTransferTO.getLinkBatchId());
                                acctNum = (String) objTxTransferTO.getLinkBatchId();
                            }
                            if (interestMap.get(CommonConstants.ACT_NUM) != null) {
                                list = sqlMap.executeQueryForList("getBehavesLikeTLAD", interestMap);
                            }
                            if (list != null && list.size() > 0) {
                                HashMap behaves = (HashMap) list.get(0);
                                prodType = CommonUtil.convertObjToStr(behaves.get("BEHAVES_LIKE"));
                                prodType = prodType.equals("OD") ? "AD" : "TL";
                                prodId = CommonUtil.convertObjToStr(behaves.get("PROD_ID"));
                                break;
                            }
                        }
                        if (objTxTransferTO.getProdType().equals(TransactionFactory.DEPOSITS) || objTxTransferTO.getProdType().equals(TransactionFactory.GL)) {
                            List intList = null;
                            if (acctNum != null && acctNum.length() > 0) {
                                interestMap.put(CommonConstants.ACT_NUM, acctNum);
                            } else {
                                interestMap.put(CommonConstants.ACT_NUM, objTxTransferTO.getLinkBatchId());
                                acctNum = (String) objTxTransferTO.getLinkBatchId();
                            }
                            if (interestMap.get(CommonConstants.ACT_NUM) != null) {
                                intList = sqlMap.executeQueryForList("getBehavesLikeForDepositNo", interestMap);
                            }
                            if (intList != null && intList.size() > 0) {
                                HashMap behaves = (HashMap) intList.get(0);
                                prodType = TransactionFactory.DEPOSITS;
                                prodId = CommonUtil.convertObjToStr(behaves.get("PROD_ID"));
                                break;
                            }
                        }
                    }
                    if ((prodType.equals(TransactionFactory.ADVANCES) || prodType.equals(TransactionFactory.LOANS)
                            || prodType.equals(TransactionFactory.DEPOSITS)) && prodId.length() > 0 && crAmount > 0) {
                        List loanLst = new ArrayList();
                        boolean setDebit = false;
                        boolean setCredit = false;
                        for (int j = 0; j < lst.size(); j++) {
                            objTxTransferTO = (TxTransferTO) lst.get(j);
                            if (objTxTransferTO.getTransType().equals("DEBIT") && !setDebit) {
                                loanLst.add(objTxTransferTO);
                                setDebit = true;
                            }
                            if (objTxTransferTO.getTransType().equals("CREDIT") && !setCredit) {
                                objTxTransferTO.setProdType(prodType);
                                objTxTransferTO.setProdId(prodId);
                                objTxTransferTO.setActNum(acctNum);
                                objTxTransferTO.setAmount(CommonUtil.convertObjToDouble(String.valueOf(crAmount)));
                                loanLst.add(objTxTransferTO);
                                setCredit = true;
                            }
                            if (setDebit && setCredit) {
                                break;
                            }
                        }
                        lst = new ArrayList();
                        lst.addAll(loanLst);
                        loanLst = null;
                    }                                        
                    //This code added by Kannan AR (JIRA : KDSA - 301)
                    //Note: Considering the credit transaction amount for sending Debit message purpose only.
                    double debitTotAmt = 0.0;
                    if(map.containsKey("GROUP_SMS") && lst != null && lst.size() > 0) {
                        for (int k = 0; k < lst.size(); k++) {
                            objTxTransferTO = (TxTransferTO) lst.get(k);
                            if (objTxTransferTO.getTransType().equals("CREDIT") &&
                                    (objTxTransferTO.getProdType().equals(TransactionFactory.DEPOSITS) ||  objTxTransferTO.getProdType().equals(TransactionFactory.OPERATIVE))){
                                debitTotAmt = debitTotAmt + CommonUtil.convertObjToDouble(objTxTransferTO.getAmount());
                            }                                    
                        }
                    }                    
                    HashMap smsAlertMap = new HashMap();
                    if (lst != null && lst.size() > 0) {
                        for (int s = 0; s < lst.size(); s++) {
                            objTxTransferTO = (TxTransferTO) lst.get(s);
                            if ((!objTxTransferTO.getProdType().equals(TransactionFactory.GL))&&(!objTxTransferTO.getProdType().equals(TransactionFactory.OTHERBANKACTS))
                                    && CommonUtil.convertObjToStr(objTxTransferTO.getProdId()).length() > 0) {
                                smsAlertMap.put("PROD_TYPE", objTxTransferTO.getProdType());
                                smsAlertMap.put("PROD_ID", objTxTransferTO.getProdId());
                                List smsParamList = sqlMap.executeQueryForList("getSelectSMSParameterForAlerts", smsAlertMap);
                                smsAlertMap.put(CommonConstants.ACT_NUM, objTxTransferTO.getActNum().lastIndexOf("_") != -1
                                        ? objTxTransferTO.getActNum().substring(0, objTxTransferTO.getActNum().lastIndexOf("_")) : objTxTransferTO.getActNum());
                                List smsAccountList = sqlMap.executeQueryForList("getSelectSMSSubscriptionMap", smsAlertMap);
                                smsAlertMap.put(CommonConstants.ACT_NUM, objTxTransferTO.getActNum());
                                if (smsAccountList != null && smsAccountList.size() > 0) {
                                    SMSSubscriptionTO objSMSSubscriptionTO = (SMSSubscriptionTO) smsAccountList.get(0);
                                    if (CommonUtil.convertObjToStr(objSMSSubscriptionTO.getMobileNo()) != null && CommonUtil.convertObjToStr(objSMSSubscriptionTO.getMobileNo()).length() == 10) {
                                        List actList = sqlMap.executeQueryForList("getBalance" + objTxTransferTO.getProdType(), smsAlertMap);
                                        if (actList != null && actList.size() > 0) {
                                            HashMap actMap = (HashMap) actList.get(0);
                                            // Merged from file given by Kannan on 30-01-2019
                                            String particulars = CommonUtil.convertObjToStr(objTxTransferTO.getParticulars());
                                                if (particulars.length() > 80) {
                                                    particulars = particulars.substring(0, 80);
                                                }
                                            // End
                                            String actBal = CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(actMap.get("CLEAR_BALANCE")).doubleValue() > 0
                                                    ? actMap.get("CLEAR_BALANCE") : new Double(-1 * CommonUtil.convertObjToDouble(actMap.get("CLEAR_BALANCE")).doubleValue()))
                                                    + (CommonUtil.convertObjToDouble(actMap.get("CLEAR_BALANCE")).doubleValue() > 0 ? " CR." : " DR.");
                                            String balString = objTxTransferTO.getProdType().equals(TransactionFactory.LOANS) ? "Total Bal" : "Total Avail Bal";
                                            //Added By Kannan AR
                                            String TempSBOD = "N";
                                            if (objTxTransferTO.getProdType().equals(TransactionFactory.OPERATIVE)) {
                                                HashMap todCheckMap = new HashMap();
                                                todCheckMap.put("PROD_ID", CommonUtil.convertObjToStr(objTxTransferTO.getProdId()));
                                                List todList = sqlMap.executeQueryForList("isTODSetForProduct", todCheckMap);
                                                if (todList != null && todList.size() > 0) {
                                                    todCheckMap = (HashMap) todList.get(0);
                                                    TempSBOD = CommonUtil.convertObjToStr(todCheckMap.get("TEMP_OD_ALLOWED"));
                                                }
                                            }
                                              //The following line added by Kannan AR advance accounts need to show available balance. (JIRA : KDSA - 301)
                                            if (objTxTransferTO.getProdType().equals(TransactionFactory.ADVANCES) || TempSBOD.equals("Y")) {
                                                if (TempSBOD.equals("Y")) {
                                                    actBal = CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(actMap.get("AVAILABLE_BALANCE")).doubleValue());
                                                } else {
                                                    actBal = CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(actMap.get("AV_BALANCE")).doubleValue());
                                                }
                                            }
                                            if (smsParamList != null && smsParamList.size() > 0) {
                                                SMSParameterTO objSMSParameterTO = (SMSParameterTO) smsParamList.get(0);
                                                // checking cr_transfer from sms_parameter for transfer transactions 
                                                if (objSMSParameterTO.getTxnAllowed().equals("Y")) {                                                    
                                                     String transType="CREDITED";
                                                     String prodTypeDesc="";
                                                        if(objTxTransferTO.getProdType().equals(TransactionFactory.OPERATIVE)){
                                                            prodTypeDesc = "Operative";
                                                        }else if(objTxTransferTO.getProdType().equals(TransactionFactory.LOANS)){
                                                             if(objTxTransferTO.getInstrumentNo2() != null && CommonUtil.convertObjToStr(objTxTransferTO.getInstrumentNo2()).equals("LOAN_PRINCIPAL")){
                                                                prodTypeDesc = "Loan";
                                                            }else{
                                                            	transType = "Posted";
                                                                prodTypeDesc="Loan";
                                                            }
                                                        } else{
                                                            prodTypeDesc="";
                                                        }
                                                    if (objTxTransferTO.getTransType().equals("CREDIT") && objSMSParameterTO.getCrTransfer().equals("Y")
                                                            && objTxTransferTO.getAmount().doubleValue() >= objSMSParameterTO.getCrTransferAmt().doubleValue()) {
                                                        // Changes done by nithya for sending transaction messages from  template - KD-2600
//                                                        String message = "An amount of Rs." + CommonUtil.convertObjToStr(objTxTransferTO.getAmount())
//                                                                + " has been "+transType+"  to your "+prodTypeDesc+" Account " + getAccountNo(objTxTransferTO.getActNum()) + ". " + balString + ": Rs."
//                                                                + actBal + " (" + getFormattedFullDate() + ") - " + bankSMSDescription + " - " + CommonUtil.convertObjToStr(mobieBankingSenderId);
                                                        String message = "";
                                                        String smsString = readFromTemplate("TransactionCreditMsg");
                                                        if (smsString != null && smsString.length() > 0) {
                                                            message = smsString.replaceAll("%%amount%%", CommonUtil.convertObjToStr(objTxTransferTO.getAmount()));
                                                            message = message.replaceAll("%%transType%%", transType);
                                                            message = message.replaceAll("%%prodType%%", prodTypeDesc);
                                                            message = message.replaceAll("%%ActNum%%", getAccountNo(objTxTransferTO.getActNum()));
                                                            message = message.replaceAll("%%balStr%%", balString);
                                                            message = message.replaceAll("%%actBal%%", actBal);
                                                            message = message.replaceAll("%%transDt%%", getFormattedFullDate());
                                                            message = message.replaceAll("%%bankSMSDescription%%", bankSMSDescription);
                                                            message = message.replaceAll("%%mobieBankingSenderId%%", CommonUtil.convertObjToStr(mobieBankingSenderId));
                                                            message = message.replaceAll("%%particulars%%", CommonUtil.convertObjToStr(particulars));
                                                        }
                                                        // Below code block commented - particulars can be passed to template if needed- Changed by nithya for KD-2600
                                                        //isTODSetForProduct query checking by Kannan AR TOD type SB need to be sent SMS with particulars KD-390 // Merged from file given by Kannan on 30-01-2019
//                                                            if (objTxTransferTO.getProdType().equals(TransactionFactory.OPERATIVE)) {
//                                                                HashMap todCheckMap = new HashMap();
//                                                                todCheckMap.put("PROD_ID", CommonUtil.convertObjToStr(objTxTransferTO.getProdId()));
//                                                                List todList = sqlMap.executeQueryForList("isTODSetForProduct", todCheckMap);
//                                                                if (todList != null && todList.size() > 0) {
//                                                                    todCheckMap = (HashMap) todList.get(0);
//                                                                    if (CommonUtil.convertObjToStr(todCheckMap.get("TEMP_OD_ALLOWED")).equals("Y")) {
//                                                                        message = "Your A/c " + getAccountNo(objTxTransferTO.getActNum()) + " has been CREDITED : Rs." + CommonUtil.convertObjToStr(objTxTransferTO.getAmount())
//                                                                                + " On (" + getFormattedFullDate() + ") - " + particulars + "  " + balString + ": Rs."
//                                                                                + actBal + " " + bankSMSDescription + " - " + CommonUtil.convertObjToStr(mobieBankingSenderId);
//                                                                    }
//                                                                }
//                                                            }
                                                        System.out.println("#$#$ SMS:" + message);
                                                        if (!map.containsKey("INTEREST_APPL_TRANSFER_CUST_ID")) {
                                                            //                                                    System.out.println("interestapplnscreen : " + map.get("INTEREST_APPL_TRANSFER_CUST_ID"));
                                                            //                                                    smsDAO.sendSMS(message, objSMSSubscriptionTO.getMobileNo(),true,_branchCode,CommonUtil.convertObjToStr(map.get("INTEREST_APPL_TRANSFER_CUST_ID")),"DepositInterestApplication");
                                                            //                                                }else{
                                                            smsDAO.sendSMS(message, CommonUtil.convertObjToStr(objSMSSubscriptionTO.getMobileNo()), "", _branchCode, CommonUtil.convertObjToStr(objTxTransferTO.getActNum()), "TransferTransaction");
                                                        }
                                                    } else if (objTxTransferTO.getTransType().equals("DEBIT") && objSMSParameterTO.getDrTransfer().equals("Y")
                                                            && objTxTransferTO.getAmount().doubleValue() >= objSMSParameterTO.getDrTransferAmt().doubleValue()) {
                                                        String drAmt = CommonUtil.convertObjToStr(objTxTransferTO.getAmount());
                                                        if (map.containsKey("GROUP_SMS") && debitTotAmt > 0.0) { //Added By Kannan AR
                                                            drAmt = CommonUtil.convertObjToStr(debitTotAmt);
                                                        }
                                                        // Changes done by nithya for sending transaction messages from  template - KD-2600
//                                                        String message = "An amount of Rs." + drAmt //CommonUtil.convertObjToStr(objTxTransferTO.getAmount()) 
//                                                                + " has been DEBITED to your "+prodTypeDesc+" Account " + getAccountNo(objTxTransferTO.getActNum()) + ". " + balString + ": Rs."
//                                                                + actBal + " (" + getFormattedFullDate() + ") - " + bankSMSDescription + " - " + CommonUtil.convertObjToStr(mobieBankingSenderId);
                                                        String message = "";
                                                        String smsString = readFromTemplate("TransactionDebitMsg");
                                                        if (smsString != null && smsString.length() > 0) {
                                                            message = smsString.replaceAll("%%amount%%", CommonUtil.convertObjToStr(objTxTransferTO.getAmount()));
                                                            message = message.replaceAll("%%prodType%%", prodTypeDesc);
                                                            message = message.replaceAll("%%ActNum%%", getAccountNo(objTxTransferTO.getActNum()));
                                                            message = message.replaceAll("%%balStr%%", balString);
                                                            message = message.replaceAll("%%actBal%%", actBal);
                                                            message = message.replaceAll("%%transDt%%", getFormattedFullDate());
                                                            message = message.replaceAll("%%bankSMSDescription%%", bankSMSDescription);
                                                            message = message.replaceAll("%%mobieBankingSenderId%%", CommonUtil.convertObjToStr(mobieBankingSenderId));
                                                            message = message.replaceAll("%%particulars%%", CommonUtil.convertObjToStr(particulars));
                                                        }
                                                        // Below code block commented - particulars can be passed to template if needed- Changed by nithya for KD-2600
                                                        //isTODSetForProduct query checking by Kannan AR TOD type SB need to be sent SMS with particulars KD-390 // Merged from file given by Kannan on 30-01-2019
//                                                            if (objTxTransferTO.getProdType().equals(TransactionFactory.OPERATIVE)) {
//                                                                HashMap todCheckMap = new HashMap();
//                                                                todCheckMap.put("PROD_ID", CommonUtil.convertObjToStr(objTxTransferTO.getProdId()));
//                                                                List todList = sqlMap.executeQueryForList("isTODSetForProduct", todCheckMap);
//                                                                if (todList != null && todList.size() > 0) {
//                                                                    todCheckMap = (HashMap) todList.get(0);
//                                                                    if (CommonUtil.convertObjToStr(todCheckMap.get("TEMP_OD_ALLOWED")).equals("Y")) {
//                                                                        message = "Your A/c " + getAccountNo(objTxTransferTO.getActNum()) + " has been DEBITED : Rs." + CommonUtil.convertObjToStr(objTxTransferTO.getAmount())
//                                                                                + " On (" + getFormattedFullDate() + ") - " + particulars + "  " + balString + ": Rs."
//                                                                                + actBal + " " + bankSMSDescription + " - " + CommonUtil.convertObjToStr(mobieBankingSenderId);
//                                                                    }
//                                                                }
//                                                            }
                                                        System.out.println("#$#$ SMS:" + message);
                                                        smsDAO.sendSMS(message, CommonUtil.convertObjToStr(objSMSSubscriptionTO.getMobileNo()), "", _branchCode, CommonUtil.convertObjToStr(objTxTransferTO.getActNum()), "TransferTransaction");
                                                    }
                                                }
                                                // Commented the code in which cr_cash was checking for transfer transaction
//                                            else{
//                                            if (objTxTransferTO.getTransType().equals("CREDIT") && objSMSParameterTO.getCrCash().equals("Y")
//                                                    && objTxTransferTO.getAmount().doubleValue() >= objSMSParameterTO.getCrCashAmt().doubleValue()) {
//                                                String message = "An amount of Rs." + CommonUtil.convertObjToStr(objTxTransferTO.getAmount())
//                                                        + " has been CREDITED to your Account " + getAccountNo(objTxTransferTO.getActNum()) + ". " + balString + ": Rs."
//                                                        + actBal + " (" + getFormattedFullDate() + ") - " + CommonConstants.BANK_SMS_DESCRIPTION + " - " + CommonConstants.MOBILE_BANKING_SENDERID;
//                                                System.out.println("#$#$ SMS:" + message);
//                                                smsDAO.sendSMS(message, objSMSSubscriptionTO.getMobileNo(),true,_branchCode,CommonUtil.convertObjToStr(objTxTransferTO.getActNum()),"TransferTransaction");
//                                            } else if (objTxTransferTO.getTransType().equals("DEBIT") && objSMSParameterTO.getDrCash().equals("Y")
//                                                    && objTxTransferTO.getAmount().doubleValue() >= objSMSParameterTO.getDrCashAmt().doubleValue()) {
//                                                String message = "An amount of Rs." + CommonUtil.convertObjToStr(objTxTransferTO.getAmount())
//                                                        + " has been DEBITED to your Account " + getAccountNo(objTxTransferTO.getActNum()) + ". " + balString + ": Rs."
//                                                        + actBal + " (" + getFormattedFullDate() + ") - " + CommonConstants.BANK_SMS_DESCRIPTION + " - " + CommonConstants.MOBILE_BANKING_SENDERID;
//                                                System.out.println("#$#$ SMS:" + message);
//                                                smsDAO.sendSMS(message, objSMSSubscriptionTO.getMobileNo(),true,_branchCode,CommonUtil.convertObjToStr(objTxTransferTO.getActNum()),"TransferTransaction");
//                                            }
//                                            }
                                                smsParamList.clear();
                                            }
                                            actList.clear();
                                        }
                                        actList = null;
                                    }
                                    smsAccountList.clear();
                                }
                                smsAccountList = null;
                                smsParamList = null;
                                objTxTransferTO = null;
                            }
                        }
                        //                        lst.clear();
                    }
                    smsAlertMap.clear();
                    smsAlertMap = null;
                    lst = null;
                }
                } 
            } else {
                System.out.println("Mobile Banking settings not configured properly, please enable from smstemplate server jboss bin folder : ");
            }
        }
        // End Send SMS block


        if (map.containsKey("PROCMAP")) {
            if (map.get("PROCMAP") != null) {
                HashMap procMap = (HashMap) map.get("PROCMAP");
                ArrayList transferTransList = (ArrayList) sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", procMap);
                TransferTrans trans = new TransferTrans();
                procMap.put(CommonConstants.AUTHORIZESTATUS, status);
                procMap.put(CommonConstants.BRANCH_ID, _branchCode);
                procMap.remove(LINK_BATCH_ID);
           //     System.out.println("TRANSFERTRANS####LIST" + transferTransList + "PROCMAP" + procMap);
                trans.doTransferAuthorize(transferTransList, procMap);
            }
        }
        forLoanDebitInt = false;
        status = null;
        objTxTransferTO = null;
        amount = null;
    }

    private String getFormattedFullDate() throws Exception {
        java.text.SimpleDateFormat DATE_FORMAT = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return DATE_FORMAT.format(new Date());
    }

//    private String sendSMS(String message, String phoneNo) {
//        String reply = "";
//        BufferedReader in = null;
//        com.see.truetransact.sendsms.SendSMSRemote remoteInter = null;
//        com.see.truetransact.sendsms.SendSMSHome home = null;
//        try {
//            String url = "http://easyhops.co.in/sendsms/" + CommonConstants.MOBILE_BANKING_USERNAME
//                    + "/" + CommonConstants.MOBILE_BANKING_PWD + "/" + CommonConstants.MOBILE_BANKING_SENDERID + "/" + phoneNo + "/"
//                    + URLEncoder.encode(message) + "/T";
//            if (cache == null) {
//                cache = new HashMap();
//            }
//            if (cache.containsKey("SendSMSJNDI")) {
//                home = (com.see.truetransact.sendsms.SendSMSHome) cache.get("SendSMSJNDI");
//                System.out.println("-->> from cache:" + home);
//            } else {
//                java.util.Hashtable environment = new java.util.Hashtable();
//                environment.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, CommonConstants.INITIAL_CONTEXT_FACTORY);
//                environment.put(javax.naming.Context.URL_PKG_PREFIXES, CommonConstants.URL_PKG_PREFIXES);
//                if (CommonConstants.SMS_SERVER.length() > 0) {
//                    environment.put(javax.naming.Context.PROVIDER_URL, "jnp://" + CommonConstants.SMS_SERVER + ":1099"); // remote machine IP
//                } else {
//                    environment.put(javax.naming.Context.PROVIDER_URL, CommonConstants.PROVIDER_URL); // remote machine IP
//                }
//                System.out.println("-->> environment.get(javax.naming.Context.PROVIDER_URL):" + environment.get(javax.naming.Context.PROVIDER_URL));
//                javax.naming.InitialContext context = new javax.naming.InitialContext(environment);
//                //            com.see.truetransact.serverside.sms.SendSMSBean obj = (com.see.truetransact.serverside.sms.SendSMSBean)context.lookup("SendSMSJNDI"); //ejb-name
//                Object objRef = context.lookup("SendSMSJNDI"); //ejb-name
//                Object obj = javax.rmi.PortableRemoteObject.narrow(objRef, com.see.truetransact.sendsms.SendSMSHome.class);
//                //            System.out.println("-->> obj:"+obj);
//                home = (com.see.truetransact.sendsms.SendSMSHome) obj;
//                cache.put("SendSMSJNDI", home);
//                objRef = null;
//                obj = null;
//                environment.clear();
//                environment = null;
//                System.out.println("-->> lookup object successfully");
//            }
//            remoteInter = (com.see.truetransact.sendsms.SendSMSRemote) javax.rmi.PortableRemoteObject.narrow(home.create(), com.see.truetransact.sendsms.SendSMSRemote.class);
//            HashMap smsMap = new HashMap();
//            smsMap.put("URL", url);
//            remoteInter.execute(smsMap, new HashMap());
//            remoteInter = null;
//            smsMap.clear();
//            smsMap = null;
////            String url = "http://easyhops.co.in/sendsms/rajeshsee/greshmaa/"+URLEncoder.encode("TMAGIC")+"/9916174195/"+
////                URLEncoder.encode("Dear Parent,  Rajesh  has  Boarded  the bus at Location at 04:00pm - Message Generated by SeE")+"/T";
////            System.out.println("#$#$ URL "+url);
//            // Uncomment the following block for sending SMS
////            URL myURL = new URL(url);
////            in = new BufferedReader(new InputStreamReader(myURL.openStream()));
////            String inputLine="";
////            while((inputLine = in.readLine()) != null) {
////                reply=inputLine;
////                System.out.println(reply);
////            }
////            if (in!=null) {
////                in.close();
////                myURL = null;
////            }
//        } catch (Exception e) {
//            e.printStackTrace();
////            try {
////                if (in!=null) {
////                    in.close();
////                }
////            } catch (Exception ee) {
////                ee.printStackTrace();
////            }
//        }
//        return reply;
//    }

    private String getAccountNo(String actNum) {
        String tempAcNo = CommonUtil.lpad(actNum.substring(7, 13), 13, '*');
        System.out.println("@#@ tempAcNo:" + tempAcNo);
        return tempAcNo;
    }
    //added by rishad 18/03/2014  

    private ArrayList principalWaiveoffTransaction(ArrayList transferTOs,TxTransferTO txTransTO, double waivePrincipalAmt) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap transMap = new HashMap();
        ArrayList transferList = new ArrayList();
        int count = 0;
        if (waivePrincipalAmt > 0) {
            transMap.put("WAIVE_PRINCIPAL", new Double(waivePrincipalAmt));
            count++;
        }
        dataMap.put("ACCT_NUM", txTransTO.getActNum());
        List lst = (List) sqlMap.executeQueryForList("getInterestAndPenalIntActHead", dataMap);
        if (lst != null && lst.size() > 0) {
            HashMap acHeads = (HashMap) lst.get(0);
            TransferTrans trans = new TransferTrans();
            HashMap txMap = new HashMap();
            double principalWaive = 0;
            double transAmt = 0;
            principalWaive = CommonUtil.convertObjToDouble(transMap.get("WAIVE_PRINCIPAL")).doubleValue();
            transAmt = principalWaive;
            txMap = new HashMap();
            transferList = new ArrayList(); // for local transfer
            trans = new TransferTrans();
            trans.setTransMode(CommonConstants.TX_TRANSFER);
            if (principalWaive > 0) {
                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("DEBIT_PRINCIPAL_HEAD"));
            }
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.DR_BRANCH, txTransTO.getBranchId());
            if (principalWaive > 0) {
                txMap.put(TransferTrans.PARTICULARS, "WaiveOff principal for " + txTransTO.getActNum());
                txMap.put("AUTHORIZEREMARKS", "PRINCIPAL_WAIVEOFF");
                txMap.put("DR_INST_TYPE", "PRINCIPAL_WAIVEOFF");
                txMap.put("DR_INSTRUMENT_2", "PRINCIPAL_WAIVEOFF");
            }
            txMap.put("generateSingleTransId", txTransTO.getSingleTransId());
            txMap.put("TRANS_MOD_TYPE", txTransTO.getTransModType());
            txMap.put("USER_ID", txTransTO.getStatusBy());
            //transferTOs.add(trans.getDebitTransferTO(txMap, transAmt));
            trans.setInitiatedBranch(_branchCode);
            txMap.put("SCREEN_NAME", txTransTO.getScreenName());
            transferList.add(trans.getDebitTransferTO(txMap, transAmt));
            if (principalWaive > 0) {
                txMap.put(TransferTrans.CR_AC_HD, txTransTO.getAcHdId());
                txMap.put(TransferTrans.CR_ACT_NUM, txTransTO.getActNum());
            }
            txMap.put(TransferTrans.CR_BRANCH,  txTransTO.getBranchId());
            txMap.put(TransferTrans.CURRENCY, "INR");
            txMap.put(TransferTrans.CR_PROD_TYPE, txTransTO.getProdType());
            if (principalWaive > 0) {
                txMap.put(TransferTrans.PARTICULARS, "WaiveOff principal upto" + CommonUtil.convertObjToStr(properFormatDate));
                //txMap.put("CR_INSTRUMENT_2", "LOAN_PRINCIPAL");
                //txMap.put(TransferTrans.PARTICULARS,"CASH");
            }
            trans.setInitiatedBranch(_branchCode);
            trans.setLinkBatchId(CommonUtil.convertObjToStr(txTransTO.getActNum()));
            trans.setBreakLoanHierachy("Y");
            txMap.put("generateSingleTransId", txTransTO.getSingleTransId());
            txMap.put("TRANS_MOD_TYPE", txTransTO.getTransModType());
            txMap.put("USER_ID", txTransTO.getStatusBy());
          //transferTOs.add(trans.getCreditTransferTO(txMap, transAmt));//CommonUtil.convertObjToDouble(obj.getPenalAmt()).doubleValue()));
            txMap.put("SCREEN_NAME", txTransTO.getScreenName()); 
            transferList.add(trans.getCreditTransferTO(txMap, transAmt));
            trans.doDebitCredit(transferList, _branchCode, false);
            transMap.remove("WAIVE_PRINCIPAL");
           transferList = null;
            trans = null;
            acHeads = null;
            lst = null;
            txMap = null;
        }
        return transferTOs;
    }

    private ArrayList NoticeWaiveoffTransaction(ArrayList transferTOs,TxTransferTO txTransTO, double noticeWaiveAmount) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap transMap = new HashMap();
        ArrayList transferList = new ArrayList();
        int count = 0;
        if (noticeWaiveAmount > 0) {
            transMap.put("WAIVE_NOTICE", new Double(noticeWaiveAmount));
            count++;
        }
        dataMap.put("ACCT_NUM", txTransTO.getActNum());
        List lst = (List) sqlMap.executeQueryForList("getInterestAndPenalIntActHead", dataMap);
        if (lst != null && lst.size() > 0) {
            HashMap acHeads = (HashMap) lst.get(0);
            TransferTrans trans = new TransferTrans();
            HashMap txMap = new HashMap();
            double noticeWaive = 0;
            double transAmt = 0;
            noticeWaive = CommonUtil.convertObjToDouble(transMap.get("WAIVE_NOTICE")).doubleValue();
            transAmt = noticeWaive;
            txMap = new HashMap();
            transferList = new ArrayList(); // for local transfer
            trans = new TransferTrans();
            trans.setTransMode(CommonConstants.TX_TRANSFER);
            if (noticeWaive > 0) {
                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("DEBIT_NOTICE_HEAD"));
            }
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            // Added by nithya on 01-03-2019 for KD 196 - 0014990: InterBranch Transaction Issues.
                if (!(txTransTO.getBranchId().equalsIgnoreCase(_branchCode))) {
                    txMap.put("WAIVE_OFF_INTER_BRANCH_TRANS", "WAIVE_OFF_INTER_BRANCH_TRANS");
                    txMap.put("INITIATED_BRANCH", _branchCode);
                }
            // End
            txMap.put(TransferTrans.DR_BRANCH, txTransTO.getBranchId());
            if (noticeWaive > 0) {
                txMap.put(TransferTrans.PARTICULARS, "WaiveOff notice for " + txTransTO.getActNum());
                txMap.put("AUTHORIZEREMARKS", "NOTICE_WAIVEOFF");
                txMap.put("DR_INST_TYPE", "NOTICE_WAIVEOFF");
                txMap.put("DR_INSTRUMENT_2", "NOTICE_WAIVEOFF");
            }
            txMap.put("USER_ID", txTransTO.getStatusBy());
            txMap.put("INIT", _branchCode);
            txMap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(txTransTO.getActNum()));
            //txMap.put("TRANS_MOD_TYPE", txTransTO.getTransModType());// Commented and changed the transmodetype to GL by nithya on 29-12-2018 for KD 363
            txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
            txMap.put("generateSingleTransId", txTransTO.getSingleTransId());
            trans.setInitiatedBranch(_branchCode);
            txMap.put("SCREEN_NAME", txTransTO.getScreenName());
            transferTOs.add(trans.getDebitTransferTO(txMap, transAmt));
            if (noticeWaive > 0) {
                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("NOTICE_CHARGES"));
            }
            txMap.put(TransferTrans.CR_BRANCH,  txTransTO.getBranchId());
            txMap.put(TransferTrans.CURRENCY, "INR");
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            if (noticeWaive > 0) {
                txMap.put(TransferTrans.PARTICULARS, "WaiveOff notice upto" + CommonUtil.convertObjToStr(properFormatDate));;
            }
            trans.setInitiatedBranch(_branchCode);
            trans.setLinkBatchId(CommonUtil.convertObjToStr(txTransTO.getActNum()));
            trans.setBreakLoanHierachy("Y");
            txMap.put("INIT", _branchCode);
            txMap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(txTransTO.getActNum()));
            txMap.put("generateSingleTransId", txTransTO.getSingleTransId());
            txMap.put("TRANS_MOD_TYPE", txTransTO.getTransModType());
            txMap.put("USER_ID", txTransTO.getStatusBy());
            txMap.put("SCREEN_NAME", txTransTO.getScreenName());
            transferTOs.add(trans.getCreditTransferTO(txMap, transAmt));//CommonUtil.convertObjToDouble(obj.getPenalAmt()).doubleValue()));
           // trans.doDebitCredit(transferList, _branchCode, false);
            transMap.remove("WAIVE_NOTICE");
            transferList = null;
            trans = null;
            acHeads = null;
            lst = null;
            txMap = null;
        }
        return transferTOs;
    }

//    private ArrayList interestWaiveoffTransaction(TxTransferTO txTransTO, double waivePenal, double waiveInterest) throws Exception {
//
//        HashMap dataMap = new HashMap();
//        HashMap transMap = new HashMap();
//
//        ArrayList transferList = new ArrayList();
//        int count = 0;
////        List waiveOffList=(List)sqlMap.executeQueryForList("getSelectTermLoanWaiveOffTO",txTransTO.getLinkBatchId());
////        if(waiveOffList !=null && waiveOffList.size()>0){
////            TermLoanPenalWaiveOffTO obj =new TermLoanPenalWaiveOffTO();
////            obj =(TermLoanPenalWaiveOffTO)waiveOffList.get(0);
//        if (waivePenal > 0) {
//            transMap.put("WAIVE_PENAL", new Double(waivePenal));
//            count++;
//        }
//        if (waiveInterest > 0) {
//            transMap.put("WAIVE_INTEREST", new Double(waiveInterest));
//            count++;
//        }
//        dataMap.put("ACCT_NUM", txTransTO.getActNum());
//        List lst = (List) sqlMap.executeQueryForList("getInterestAndPenalIntActHead", dataMap);
//        if (lst != null && lst.size() > 0) {
//            HashMap acHeads = (HashMap) lst.get(0);
//            //         System.out.println("#####collectfromloan"+waiveOffMap);
////                ArrayList transferList = new ArrayList(); // for local transfer
//            TransferTrans trans = new TransferTrans();
//            HashMap txMap = new HashMap();
//            double penal = 0;
//            double interest = 0, transAmt = 0;
//            for (int i = 0; i < count; i++) {
//
//                penal = CommonUtil.convertObjToDouble(transMap.get("WAIVE_PENAL")).doubleValue();
//                if (penal == 0) {
//                    interest = CommonUtil.convertObjToDouble(transMap.get("WAIVE_INTEREST")).doubleValue();
//                    transAmt = interest;
//                } else {
//                    transAmt = penal;
//                }
//
//                txMap = new HashMap();
//                transferList = new ArrayList(); // for local transfer
//                trans = new TransferTrans();
//                trans.setTransMode(CommonConstants.TX_TRANSFER);
//                trans.setInitiatedBranch(_branchCode);
//                trans.setLinkBatchId(CommonUtil.convertObjToStr(txTransTO.getActNum()));
//                System.out.println("penal ================== " + penal);
//                System.out.println("acHeads ================== " + (String) acHeads.get("DEBIT_PENAL_HEAD"));
//                if (penal > 0) {
//                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("DEBIT_PENAL_HEAD"));
//                } else {
//                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("DEBIT_DISCOUNT_ACHD"));
//                }
//                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
//                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
//                if (penal > 0) {
//                    txMap.put(TransferTrans.PARTICULARS, "WaiveOff Penal for " + txTransTO.getActNum());
//                    txMap.put("AUTHORIZEREMARKS", "PENAL_WAIVEOFF");
//                    txMap.put("DR_INST_TYPE", "PENAL_WAIVEOFF");
//                    txMap.put("DR_INSTRUMENT_2", "PENAL_WAIVEOFF");
//                } else {
//                    txMap.put(TransferTrans.PARTICULARS, "WaiveOff Interest for " + txTransTO.getActNum());
//                    txMap.put("AUTHORIZEREMARKS", "INTEREST_WAIVEOFF");
//                    txMap.put("DR_INST_TYPE", "INTEREST_WAIVEOFF");
//                    txMap.put("DR_INSTRUMENT_2", "INTEREST_WAIVEOFF");
//                }
//                txMap.put("USER_ID", txTransTO.getStatusBy());
//                txMap.put("INIT", _branchCode);
//                txMap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(txTransTO.getActNum()));
//                transferList.add(trans.getDebitTransferTO(txMap, transAmt));//CommonUtil.convertObjToDouble(obj.getPenalAmt()).doubleValue()));
//                if (penal > 0) {
//                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("PENAL_INT"));
//                } else {
//                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("AC_DEBIT_INT"));
//                }
//                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
//                txMap.put(TransferTrans.CURRENCY, "INR");
//                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
//                if (penal > 0) {
//                    txMap.put(TransferTrans.PARTICULARS, "WaiveOff Penal upto" + CommonUtil.convertObjToStr(properFormatDate));
//                } else {
//                    txMap.put(TransferTrans.PARTICULARS, "WaiveOff Interest upto" + CommonUtil.convertObjToStr(properFormatDate));
//                }
//                System.out.println("set.size() " + " ####### insertAccHead txMap 33=" + txMap);
//
//
//                trans.setInitiatedBranch(_branchCode);
//                trans.setLinkBatchId(CommonUtil.convertObjToStr(txTransTO.getActNum()));
//                txMap.put("USER", txTransTO.getStatusBy());
//                transferList.add(trans.getCreditTransferTO(txMap, transAmt));//CommonUtil.convertObjToDouble(obj.getPenalAmt()).doubleValue()));
////                    trans.doDebitCredit(transferList, _branchCode, false);
////                    obj.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
////                    obj.setAuthorizedBy(user);
////                    obj.setAuthorizedDt(properFormatDate);
//                System.out.println("transferList 000===" + transferList);
//                transMap.remove("WAIVE_PENAL");
//            }
//            // transferList = null;
//            trans = null;
//            acHeads = null;
//            lst = null;
//
//            txMap = null;
//        }
////        }
//        return transferList;
//    }
    private ArrayList interestWaiveoffTransaction(ArrayList transferTOs,TxTransferTO txTransTO, double waiveInterest) throws Exception {

        HashMap dataMap = new HashMap();
        HashMap transMap = new HashMap();
        ArrayList transferList = new ArrayList();
        int count = 0;
        if (waiveInterest > 0) {
            transMap.put("WAIVE_INTEREST", new Double(waiveInterest));
            count++;
        }
        dataMap.put("ACCT_NUM", txTransTO.getActNum());
        List lst = (List) sqlMap.executeQueryForList("getInterestAndPenalIntActHead", dataMap);
        if (lst != null && lst.size() > 0) {
            HashMap acHeads = (HashMap) lst.get(0);
            TransferTrans trans = new TransferTrans();
            HashMap txMap = new HashMap();
            double penal = 0;
            double interest = 0, transAmt = 0;
            interest = CommonUtil.convertObjToDouble(transMap.get("WAIVE_INTEREST")).doubleValue();
            if (interest > 0) {
                transAmt = interest;
            }
            txMap = new HashMap();
            transferList = new ArrayList(); // for local transfer
            trans = new TransferTrans();
            trans.setTransMode(CommonConstants.TX_TRANSFER);
            trans.setInitiatedBranch(_branchCode);
            trans.setLinkBatchId(CommonUtil.convertObjToStr(txTransTO.getActNum()));
            if (interest > 0) {
                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("DEBIT_DISCOUNT_ACHD"));
            }
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.DR_BRANCH,  txTransTO.getBranchId());
            // Added by nithya on 01-03-2019 for KD 196 - 0014990: InterBranch Transaction Issues.
                if (!(txTransTO.getBranchId().equalsIgnoreCase(_branchCode))) {
                    txMap.put("WAIVE_OFF_INTER_BRANCH_TRANS", "WAIVE_OFF_INTER_BRANCH_TRANS");
                    txMap.put("INITIATED_BRANCH", _branchCode);
                }
            // End
            if (interest > 0) {
                txMap.put(TransferTrans.PARTICULARS, "WaiveOff Interest for " + txTransTO.getActNum());
                txMap.put("AUTHORIZEREMARKS", "INTEREST_WAIVEOFF");
                txMap.put("DR_INST_TYPE", "INTEREST_WAIVEOFF");
                txMap.put("DR_INSTRUMENT_2", "INTEREST_WAIVEOFF");
            }
            txMap.put("USER_ID", txTransTO.getStatusBy());
            txMap.put("INIT", _branchCode);
            txMap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(txTransTO.getActNum()));
            txMap.put("generateSingleTransId", txTransTO.getSingleTransId());
            //txMap.put("TRANS_MOD_TYPE", txTransTO.getTransModType());// Commented and changed the transmodetype to GL by nithya on 29-12-2018 for KD 363
            txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
            txMap.put("SCREEN_NAME", txTransTO.getScreenName());
            transferTOs.add(trans.getDebitTransferTO(txMap, transAmt));//CommonUtil.convertObjToDouble(obj.getPenalAmt()).doubleValue()));
            if (interest > 0) {
                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("AC_DEBIT_INT"));
            }
            txMap.put(TransferTrans.CR_BRANCH,  txTransTO.getBranchId());
            txMap.put(TransferTrans.CURRENCY, "INR");
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            if (interest > 0) {
                txMap.put(TransferTrans.PARTICULARS, "WaiveOff Interest upto" + CommonUtil.convertObjToStr(properFormatDate));
            }
            trans.setInitiatedBranch(_branchCode);
            trans.setLinkBatchId(CommonUtil.convertObjToStr(txTransTO.getActNum()));
            txMap.put("USER_ID", txTransTO.getStatusBy());
            txMap.put("generateSingleTransId", txTransTO.getSingleTransId());
            txMap.put("TRANS_MOD_TYPE", txTransTO.getTransModType());
            txMap.put("SCREEN_NAME", txTransTO.getScreenName());
            transferTOs.add(trans.getCreditTransferTO(txMap, transAmt));//CommonUtil.convertObjToDouble(obj.getPenalAmt()).doubleValue()));
           // trans.doDebitCredit(transferList, _branchCode, false);
            System.out.println("transferList 000===" + transferList);
            transMap.remove("WAIVE_INTEREST");
            // }
          transferList = null;
            trans = null;
            acHeads = null;
            lst = null;
            txMap = null;
        }
//        }
        return transferTOs;
    }

    private ArrayList penalWaiveoffTransaction(ArrayList transferTOs,TxTransferTO txTransTO, double waivePenal) throws Exception {

        HashMap dataMap = new HashMap();
        HashMap transMap = new HashMap();

        ArrayList transferList = new ArrayList();
        int count = 0;
        if (waivePenal > 0) {
            transMap.put("WAIVE_PENAL", new Double(waivePenal));
            count++;
        }

        dataMap.put("ACCT_NUM", txTransTO.getActNum());
        List lst = (List) sqlMap.executeQueryForList("getInterestAndPenalIntActHead", dataMap);
        if (lst != null && lst.size() > 0) {
            HashMap acHeads = (HashMap) lst.get(0);
            TransferTrans trans = new TransferTrans();
            HashMap txMap = new HashMap();
            double penal = 0;
            double interest = 0, transAmt = 0;
            penal = CommonUtil.convertObjToDouble(transMap.get("WAIVE_PENAL")).doubleValue();
            if (penal > 0) {
                transAmt = penal;
            }
            txMap = new HashMap();
            transferList = new ArrayList(); // for local transfer
            trans = new TransferTrans();
            trans.setTransMode(CommonConstants.TX_TRANSFER);
            trans.setInitiatedBranch(_branchCode);
            trans.setLinkBatchId(CommonUtil.convertObjToStr(txTransTO.getActNum()));
            if (penal > 0) {
                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("DEBIT_PENAL_HEAD"));
            }
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.DR_BRANCH, txTransTO.getBranchId());
            // Added by nithya on 01-03-2019 for KD 196 - 0014990: InterBranch Transaction Issues.
                if (!(txTransTO.getBranchId().equalsIgnoreCase(_branchCode))) {
                    txMap.put("WAIVE_OFF_INTER_BRANCH_TRANS", "WAIVE_OFF_INTER_BRANCH_TRANS");
                    txMap.put("INITIATED_BRANCH", _branchCode);
                }
            // End
            if (penal > 0) {
                txMap.put(TransferTrans.PARTICULARS, "WaiveOff Penal for " + txTransTO.getActNum());
                txMap.put("AUTHORIZEREMARKS", "PENAL_WAIVEOFF");
                txMap.put("DR_INST_TYPE", "PENAL_WAIVEOFF");
                txMap.put("DR_INSTRUMENT_2", "PENAL_WAIVEOFF");
            }
            txMap.put("USER_ID", txTransTO.getStatusBy());
            txMap.put("INIT", _branchCode);
            txMap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(txTransTO.getActNum()));
            txMap.put("generateSingleTransId", txTransTO.getSingleTransId());
            //txMap.put("TRANS_MOD_TYPE", txTransTO.getTransModType());// Commented and changed the transmodetype to GL by nithya on 29-12-2018 for KD 363
            txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
          //  transferList.add(trans.getDebitTransferTO(txMap, transAmt));//CommonUtil.convertObjToDouble(obj.getPenalAmt()).doubleValue()));
            txMap.put("SCREEN_NAME", txTransTO.getScreenName());
            transferTOs.add(trans.getDebitTransferTO(txMap, transAmt));
            if (penal > 0) {
                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("PENAL_INT"));
            }
          //  txMap.put(TransferTrans.CR_BRANCH, _branchCode);
            txMap.put(TransferTrans.CR_BRANCH,  txTransTO.getBranchId());
            txMap.put(TransferTrans.CURRENCY, "INR");
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            if (penal > 0) {
                txMap.put(TransferTrans.PARTICULARS, "WaiveOff Penal upto" + CommonUtil.convertObjToStr(properFormatDate));
            }
            trans.setInitiatedBranch(_branchCode);
            trans.setLinkBatchId(CommonUtil.convertObjToStr(txTransTO.getActNum()));
            txMap.put("USER_ID", txTransTO.getStatusBy());
            txMap.put("generateSingleTransId", txTransTO.getSingleTransId());
            txMap.put("TRANS_MOD_TYPE", txTransTO.getTransModType());
           // transferList.add(trans.getCreditTransferTO(txMap, transAmt));//CommonUtil.convertObjToDouble(obj.getPenalAmt()).doubleValue()));
            txMap.put("SCREEN_NAME", txTransTO.getScreenName());
            transferTOs.add(trans.getCreditTransferTO(txMap, transAmt));
            // trans.doDebitCredit(transferList, _branchCode, false);
            transMap.remove("WAIVE_PENAL");
            // }
           transferList = null;
            trans = null;
            acHeads = null;
            lst = null;

            txMap = null;
        }
//        }
       // return transferList;
        return  transferTOs;
    }
    
    
   private ArrayList arcWaiveoffTransaction(ArrayList transferTOs,TxTransferTO txTransTO, double waiveArc) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap transMap = new HashMap();
        ArrayList transferList = new ArrayList();
        int count = 0;
        if (waiveArc > 0) {
            transMap.put("WAIVE_ARC", new Double(waiveArc));
            count++;
        }
        dataMap.put("ACCT_NUM", txTransTO.getActNum());
        List lst = (List) sqlMap.executeQueryForList("getInterestAndPenalIntActHead", dataMap);
        if (lst != null && lst.size() > 0) {
            HashMap acHeads = (HashMap) lst.get(0);
            TransferTrans trans = new TransferTrans();
            HashMap txMap = new HashMap();
            double arc = 0;
            double interest = 0, transAmt = 0;
            arc = CommonUtil.convertObjToDouble(transMap.get("WAIVE_ARC")).doubleValue();
            if (arc > 0) {
                transAmt = arc;
            }
            txMap = new HashMap();
            transferList = new ArrayList(); // for local transfer
            trans = new TransferTrans();
            trans.setTransMode(CommonConstants.TX_TRANSFER);
            trans.setInitiatedBranch(_branchCode);
            trans.setLinkBatchId(CommonUtil.convertObjToStr(txTransTO.getActNum()));
            if (arc > 0) {
                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("DEBIT_ARC_HEAD"));
            }
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.DR_BRANCH, txTransTO.getBranchId());
            // Added by nithya on 01-03-2019 for KD 196 - 0014990: InterBranch Transaction Issues.
                if (!(txTransTO.getBranchId().equalsIgnoreCase(_branchCode))) {
                    txMap.put("WAIVE_OFF_INTER_BRANCH_TRANS", "WAIVE_OFF_INTER_BRANCH_TRANS");
                    txMap.put("INITIATED_BRANCH", _branchCode);
                }
            // End
            if (arc> 0) {
                txMap.put(TransferTrans.PARTICULARS, "WaiveOff Arc for " + txTransTO.getActNum());
                txMap.put("AUTHORIZEREMARKS", "ARC_WAIVEOFF");
                txMap.put("DR_INST_TYPE", "ARC_WAIVEOFF");
                txMap.put("DR_INSTRUMENT_2", "ARC_WAIVEOFF");
            }
            txMap.put("USER_ID", txTransTO.getStatusBy());
            txMap.put("INIT", _branchCode);
            txMap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(txTransTO.getActNum()));
            txMap.put("generateSingleTransId", txTransTO.getSingleTransId());
            //txMap.put("TRANS_MOD_TYPE", txTransTO.getTransModType());// Commented and changed the transmodetype to GL by nithya on 29-12-2018 for KD 363
            txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
            txMap.put("SCREEN_NAME", txTransTO.getScreenName());
            transferTOs.add(trans.getDebitTransferTO(txMap, transAmt));//CommonUtil.convertObjToDouble(obj.getPenalAmt()).doubleValue()));
            if (arc > 0) {
                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("ARC COST"));
            }
            txMap.put(TransferTrans.CR_BRANCH, txTransTO.getBranchId());
            txMap.put(TransferTrans.CURRENCY, "INR");
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            if (arc> 0) {
                txMap.put(TransferTrans.PARTICULARS, "WaiveOff arc upto" + CommonUtil.convertObjToStr(properFormatDate));
            }
            trans.setInitiatedBranch(_branchCode);
            trans.setLinkBatchId(CommonUtil.convertObjToStr(txTransTO.getActNum()));
            txMap.put("USER_ID", txTransTO.getStatusBy());
            txMap.put("generateSingleTransId", txTransTO.getSingleTransId());
            txMap.put("TRANS_MOD_TYPE", txTransTO.getTransModType());
            txMap.put("SCREEN_NAME", txTransTO.getScreenName());
            transferTOs.add(trans.getCreditTransferTO(txMap, transAmt));//CommonUtil.convertObjToDouble(obj.getPenalAmt()).doubleValue()));
            //trans.doDebitCredit(transferList, _branchCode, false);
            transMap.remove("WAIVE_ARC");
            // }
           // transferList = null;
            trans = null;
            acHeads = null;
            lst = null;

            txMap = null;
        }
//        }
       return  transferTOs;
    }
    private ArrayList commonWaiveoffTransaction(ArrayList transferTOs, TxTransferTO txTransTO, double waiveAmount, String type) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap transMap = new HashMap();
        ArrayList transferList = new ArrayList();
        int count = 0;
        if (waiveAmount > 0) {
            transMap.put("WAIVE_AMOUNT", new Double(waiveAmount));
            count++;
        }
        dataMap.put("ACCT_NUM", txTransTO.getActNum());
        List lst = (List) sqlMap.executeQueryForList("getInterestAndPenalIntActHead", dataMap);
        if (lst != null && lst.size() > 0) {
            HashMap acHeads = (HashMap) lst.get(0);
            TransferTrans trans = new TransferTrans();
            HashMap txMap = new HashMap();
            double amount = 0;
            double interest = 0, transAmt = 0;
            amount = CommonUtil.convertObjToDouble(transMap.get("WAIVE_AMOUNT")).doubleValue();
            if (amount > 0) {
                transAmt = amount;
                txMap = new HashMap();
                transferList = new ArrayList(); // for local transfer
                trans = new TransferTrans();
                trans.setTransMode(CommonConstants.TX_TRANSFER);
                trans.setInitiatedBranch(_branchCode);
                trans.setLinkBatchId(CommonUtil.convertObjToStr(txTransTO.getActNum()));
                if (type.equalsIgnoreCase("POSTAGE_WAIVE_OFF")) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("DEBIT_POSTAGE_HEAD"));
                } else if (type.equalsIgnoreCase("MISCELLANEOUS_WAIVE_OFF")) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("DEBIT_MISCELLANEOUS_HEAD"));
                } else if (type.equalsIgnoreCase("LEGAL_WAIVE_OFF")) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("DEBIT_LEGAL_HEAD"));
                } else if (type.equalsIgnoreCase("INSURENCE_WAIVE_OFF")) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("DEBIT_INSURANCE_HEAD"));
                } else if (type.equalsIgnoreCase("ADVERTISE_WAIVE_OFF")) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("DEBIT_ADVERTISE_HEAD"));
                } else if (type.equalsIgnoreCase("EP_WAIVE_OFF")) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("DEBIT_EP_COST_HEAD"));
                } else if (type.equalsIgnoreCase("DECREE_WAIVE_OFF")) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("DEBIT_DECREE_HEAD"));
                } else if (type.equalsIgnoreCase("ARBITRARY_WAIVE_OFF")) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("DEBIT_ARBITRARY_HEAD"));
                } else if (type.equalsIgnoreCase("RECOVERY_WAIVE_OFF")) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("DEBIT_RECOVERY_HEAD"));
                }else if (type.equalsIgnoreCase("MEASUREMENT_WAIVE_OFF")) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("DEBIT_MEASUREMENT_HEAD"));
                }
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                // Added by nithya on 01-03-2019 for KD 196 - 0014990: InterBranch Transaction Issues.
                if (!(txTransTO.getBranchId().equalsIgnoreCase(_branchCode))) {
                    txMap.put("WAIVE_OFF_INTER_BRANCH_TRANS", "WAIVE_OFF_INTER_BRANCH_TRANS");
                    txMap.put("INITIATED_BRANCH", _branchCode);
                }
               // End
                txMap.put(TransferTrans.DR_BRANCH,  txTransTO.getBranchId());
                txMap.put(TransferTrans.PARTICULARS, type + "for " + txTransTO.getActNum());
                txMap.put("AUTHORIZEREMARKS", type);
                txMap.put("DR_INST_TYPE", type);
                txMap.put("DR_INSTRUMENT_2", type);
                txMap.put("USER_ID", txTransTO.getStatusBy());
                txMap.put("INIT", _branchCode);
                txMap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(txTransTO.getActNum()));
                txMap.put("generateSingleTransId", txTransTO.getSingleTransId());                
                //txMap.put("TRANS_MOD_TYPE", txTransTO.getTransModType());// Commented and changed the transmodetype to GL by nithya on 29-12-2018 for KD 363
                txMap.put("TRANS_MOD_TYPE",TransactionFactory.GL);
                txMap.put("SCREEN_NAME", txTransTO.getScreenName());
                transferTOs.add(trans.getDebitTransferTO(txMap, transAmt));//CommonUtil.convertObjToDouble(obj.getPenalAmt()).doubleValue()));
                if (type.equalsIgnoreCase("POSTAGE_WAIVE_OFF")) {
                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("POSTAGE_CHARGES"));
                } else if (type.equalsIgnoreCase("MISCELLANEOUS_WAIVE_OFF")) {
                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("MISC_SERV_CHRG"));
                } else if (type.equalsIgnoreCase("LEGAL_WAIVE_OFF")) {
                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("LEGAL_CHARGES"));
                } else if (type.equalsIgnoreCase("INSURENCE_WAIVE_OFF")) {
                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INSURANCE_CHARGES"));
                } else if (type.equalsIgnoreCase("ADVERTISE_WAIVE_OFF")) {
                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("ADVERTISE_ACHEAD"));
                } else if (type.equalsIgnoreCase("EP_WAIVE_OFF")) {
                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("EP Cost"));
                } else if (type.equalsIgnoreCase("DECREE_WAIVE_OFF")) {
                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("EXECUTION_DECREE_CHARGES"));
                } else if (type.equalsIgnoreCase("ARBITRARY_WAIVE_OFF")) {
                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("ARBITRARY_CHARGES"));
                } else if (type.equalsIgnoreCase("RECOVERY_WAIVE_OFF")) {
                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("RECOVERY_CHARGES"));
                } else if (type.equalsIgnoreCase("MEASUREMENT_WAIVE_OFF")) {
                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("MEASUREMENT_CHARGES"));
                }
                txMap.put(TransferTrans.CR_BRANCH,  txTransTO.getBranchId());
                txMap.put(TransferTrans.CURRENCY, "INR");
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);

                txMap.put(TransferTrans.PARTICULARS, type + "upto" + CommonUtil.convertObjToStr(properFormatDate));

                trans.setInitiatedBranch(_branchCode);
                trans.setLinkBatchId(CommonUtil.convertObjToStr(txTransTO.getActNum()));
                txMap.put("USER_ID", txTransTO.getStatusBy());
                txMap.put("generateSingleTransId", txTransTO.getSingleTransId());
                txMap.put("TRANS_MOD_TYPE", txTransTO.getTransModType());
                txMap.put("SCREEN_NAME", txTransTO.getScreenName());
                transferTOs.add(trans.getCreditTransferTO(txMap, transAmt));//CommonUtil.convertObjToDouble(obj.getPenalAmt()).doubleValue()));
               //trans.doDebitCredit(transferList, _branchCode, false);
            }
            transMap.remove("WAIVE_AMOUNT");
            // }
            // transferList = null;
            trans = null;
            acHeads = null;
            lst = null;

            txMap = null;
        }
//        }
        return transferTOs;
    }

    private void updateRebateFacilityDetails(TxTransferTO txTo) throws Exception {
        HashMap dataMap = new HashMap();
        LoanRebateTO obj=null;
        dataMap.put("ACCT_NUM", txTo.getLinkBatchId());
        dataMap.put("REBATE_AMT", txTo.getAmount());
        dataMap.put("CURR_DT", txTo.getTransDt());        
        //sqlMap.executeUpdate("updateTermLoanFacilityRebateDetailsTO", dataMap);
        dataMap.put("ACCT_NUM", txTo.getLinkBatchId());
        List lst = sqlMap.executeQueryForList("selectLoanRebateTO", dataMap);
        if (lst != null && lst.size() > 0) {
            obj = new LoanRebateTO();
            obj = (LoanRebateTO) lst.get(0);
            HashMap update = new HashMap();
            update.put("ACCT_NUM", obj.getAccNo());
            update.put("REBATE_ID", obj.getRebateId());
            update.put("AUTHORIZE_STATUS", CommonConstants.STATUS_AUTHORIZED);
            update.put("AUTHORIZE_BY", user);
            update.put("AUTHORIZE_DT", properFormatDate);
            sqlMap.executeUpdate("authorizeLoanRebate", update);
        }
        dataMap.put("CURR_DT", obj.getRebateUpto());    
        sqlMap.executeUpdate("updateTermLoanFacilityRebateDetailsTO", dataMap);
    }

    private void updateWaiveOffInterestPenalDetails(TxTransferTO txTo) throws Exception {
        HashMap dataMap = new HashMap();
        List waiveOffList = (List) sqlMap.executeQueryForList("getSelectTermLoanWaiveOffTO", txTo.getLinkBatchId());
        if (waiveOffList != null && waiveOffList.size() > 0) {
            TermLoanPenalWaiveOffTO obj = new TermLoanPenalWaiveOffTO();
            obj = (TermLoanPenalWaiveOffTO) waiveOffList.get(0);
            obj.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
            obj.setAuthorizedBy(user);
            obj.setAuthorizedDt(properFormatDate);
            dataMap.put("CURR_DT", properFormatDate);
            dataMap.put("ACCT_NUM", obj.getAcctNum());

            dataMap.put("INTEREST_TRANS_AMT", obj.getInterestAmt());
            dataMap.put("PENAL_TRANS_AMT", obj.getPenalAmt());

            System.out.println("dataMap####" + dataMap);
            sqlMap.executeUpdate("updateTermLoanInterestWaiveOffTO", obj);
            sqlMap.executeUpdate("updateTermLoanFacilityWaiveofDetailsTO", dataMap);

        }


    }

    private void updateWaiveOffDetails(TxTransferTO txTo) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put("LINKBATCHID", txTo.getLinkBatchId());
        whereMap.put("REMARKS", txTo.getAuthorizeRemarks());
        System.out.println("01111111111" + txTo.getAuthorizeRemarks());
        //List waiveOffList = (List) sqlMap.executeQueryForList("getSelectTermLoanWaiveOffTO", txTo.getLinkBatchId());
        List waiveOffList = (List) sqlMap.executeQueryForList("getSelectTermLoanAllWaiveOffTO", whereMap);
        if (waiveOffList != null && waiveOffList.size() > 0) {
            TermLoanPenalWaiveOffTO obj = new TermLoanPenalWaiveOffTO();
            obj = (TermLoanPenalWaiveOffTO) waiveOffList.get(0);
            obj.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
            obj.setAuthorizedBy(user);
            obj.setAuthorizedDt(properFormatDate);

            dataMap.put("CURR_DT", properFormatDate);
            dataMap.put("ACCT_NUM", obj.getAcctNum());
            dataMap.put("WAIVE_TRANS_AMT", obj.getWaiveAmt());
            sqlMap.executeUpdate("updateTermLoanWaiveOffTO", obj);
            /*
             * if any time repoting from supporting they need to update waive
             * amount in loan_facilty_detail also then uncomment following code
             * --why am commenting this portion code because of am keeping
             * waiving details in seperate table (loan_waiveiff--table name)..
             * in this having a link with loan_facility_detail
             */
//            if(txTo.getAuthorizeRemarks().equals("NOTICE_WAIVEOFF"))
//            {
//            sqlMap.executeUpdate("updateTermLoanFacilityNoticeWaiveofDetailsTO", dataMap);
//            }
//            else if(txTo.getAuthorizeRemarks().equals("PRINCIPAL_WAIVEOFF"))
//            {
//                sqlMap.executeUpdate("updateTermLoanFacilityPrincipalWaiveofDetailsTO", dataMap);
//            }



        }


    }

    private ArrayList rebateInterestTransaction(TxTransferTO transTO, double rebateInterest, double interest,Date rebateUpto) throws Exception {
        LoanRebateTO rebateTo = new LoanRebateTO();
        ArrayList transferList = new ArrayList();
        HashMap dataMap = new HashMap();
        dataMap.put("ACCT_NUM", transTO.getActNum());
        List lst = (List) sqlMap.executeQueryForList("getInterestAndPenalIntActHead", dataMap);
        if (lst != null && lst.size() > 0) {
            HashMap acHeads = (HashMap) lst.get(0);
            HashMap txMap = new HashMap();
            txMap = new HashMap();
            String crdrBranch = "";
            TransferTrans trans = new TransferTrans();
            trans.setTransMode(CommonConstants.TX_TRANSFER);
            trans.setInitiatedBranch(_branchCode);
            trans.setLinkBatchId(CommonUtil.convertObjToStr(transTO.getActNum()));
            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("REBATE_INTEREST_ACHD"));
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            HashMap interBranchCodeMap = new HashMap();
            List interBranchCodeList = sqlMap.executeQueryForList("getBranchTL", transTO.getActNum());           
            if (interBranchCodeList != null && interBranchCodeList.size() > 0) {
                crdrBranch = CommonUtil.convertObjToStr(interBranchCodeList.get(0));
            }
            //txMap.put(TransferTrans.DR_BRANCH, _branchCode);
            if(!crdrBranch.equals(_branchCode)){
              txMap.put(TransferTrans.DR_BRANCH, crdrBranch);  
            }else{
            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
            }
            txMap.put("USER_ID", transTO.getStatusBy());
            txMap.put(TransferTrans.PARTICULARS, "Rebate interest  " + transTO.getActNum());
            txMap.put("AUTHORIZEREMARKS", "REBATE_INTEREST");
            txMap.put("DR_INST_TYPE", "REBATE_INTEREST");
            txMap.put("DR_INSTRUMENT_2", "REBATE_INTEREST");
            txMap.put("INIT", _branchCode);
            //txMap.put("TRANS_MOD_TYPE", transTO.getTransModType());
            txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
            txMap.put("LINK_BATCH_ID",transTO.getActNum());
            transferList.add(trans.getDebitTransferTO(txMap, rebateInterest));
            txMap = new HashMap();
            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("AC_DEBIT_INT"));
            //txMap.put(TransferTrans.CR_BRANCH, _branchCode);
            if(!crdrBranch.equals(_branchCode)){
              txMap.put(TransferTrans.CR_BRANCH, crdrBranch);  
            }else{
            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
            }
            txMap.put(TransferTrans.CURRENCY, "INR");
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            txMap.put("USER", transTO.getStatusBy());
            txMap.put(TransferTrans.PARTICULARS, "Rebate interest upto " + CommonUtil.convertObjToStr(properFormatDate));
            txMap.put("TRANS_MOD_TYPE", transTO.getTransModType());
            txMap.put("LINK_BATCH_ID",transTO.getActNum());
            if (interest > 0) {
                transferList.add(trans.getCreditTransferTO(txMap, interest));
            } else {
                txMap.put("AUTHORIZEREMARKS", "REBATE_INTEREST");//KD-3630
                txMap.put("DR_INST_TYPE", "REBATE_INTEREST");
                txMap.put("DR_INSTRUMENT_2", "REBATE_INTEREST");
                transferList.add(trans.getCreditTransferTO(txMap, rebateInterest));
            }
            if (interest > 0) {
                txMap = new HashMap();
                txMap.put(TransferTrans.CR_ACT_NUM, transTO.getActNum());
                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("ACCT_HEAD"));
                txMap.put(TransferTrans.CR_PROD_ID, transTO.getProdId());
                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                txMap.put(TransferTrans.CURRENCY, "INR");
                txMap.put("USER", transTO.getStatusBy());
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
                txMap.put(TransferTrans.PARTICULARS, "Rebate interest paid to principal upto " + CommonUtil.convertObjToStr(properFormatDate));
                txMap.put("TRANS_MOD_TYPE", transTO.getTransModType());
                txMap.put("LINK_BATCH_ID",transTO.getActNum());
                transferList.add(trans.getCreditTransferTO(txMap, rebateInterest - interest));
            }
            trans.setInitiatedBranch(_branchCode);
            trans.setLinkBatchId(CommonUtil.convertObjToStr(transTO.getActNum()));
            rebateTo.setAccNo(CommonUtil.convertObjToStr(transTO.getActNum()));
            rebateTo.setBranchCode(_branchCode);
            rebateTo.setIntAmount(String.valueOf(rebateInterest));
            rebateTo.setStatus(CommonConstants.STATUS_CREATED);
            rebateTo.setStatusDt((Date) properFormatDate.clone());
            rebateTo.setStatusBy(user);
            rebateTo.setRebateId(generateRebateBatchID());
            if(rebateUpto!=null && !rebateUpto.equals("")){
                rebateTo.setRebateUpto(rebateUpto);
            }else{
                rebateTo.setRebateUpto((Date)currDate.clone());
            } 
            rebateTo.setRebateUpto(getProperDateFormat(rebateTo.getRebateUpto()));// Added by nithya on 10-04-2019 for KD-466
            sqlMap.executeUpdate("insertLoanRebate", rebateTo);
            HashMap whereMap = new HashMap();
            whereMap.put("ACCOUNTNO", CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(transTO.getActNum())));
            //whereMap.put("LAST_REBATE_DT", currDate.clone());// Changed by nithya on 10-07-2018 for KD 137 0008623: 18/12/17--'Rebate Paid Dt Upto' in cash screen is wrong. It shows the date of rebate paid. Not showing
            whereMap.put("LAST_REBATE_DT", rebateTo.getRebateUpto());
            sqlMap.executeUpdate("updateRebateBalnce", whereMap);
            trans = null;
            acHeads = null;
            lst = null;
            txMap = null;
        }
//        }
        return transferList;
    }


    /*
     * method to get the batch id, will be called once for one batch
     */
    private String generateRebateBatchID() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "REBATE_ID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//for trans_batch_id resetting, branch code
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        String batchID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        return batchID;
    }

    private void asAnCustomerCreditLoanAdvances(ArrayList transferTOs, String status,HashMap closingMap) throws Exception {
        String prodType = "";
        String acctNum = "";
        String batch_id = null;
        long transSlNo = 0;
        double zeroAmt = 0.0;
        double transactionPaidAmt = 0;
        HashMap insetMap = new HashMap();
        TxTransferTO txTransfer = null;
        //System.out.println("transferTOs &$&$&$ in transferTOs"+transferTOs);
        if (status.equals(CommonConstants.STATUS_AUTHORIZED)) {
            for (int j = 0; j < transferTOs.size(); j++) {
                //System.out.println("transferTOs &$&$&$ in loop"+transferTOs.get(j));
                insetMap = new HashMap();
                txTransfer = new TxTransferTO();
                txTransfer = (TxTransferTO) transferTOs.get(j);
                if (txTransfer.getProdType().equals(TransactionFactory.LOANS)
                        && txTransfer.getTransType().equals("CREDIT")) {//This line added because if TransType is CREDIT then only should set ProdType & AcctNum otherwise it's not inserting record into LOAN_TRANS_DETAILS table
                    prodType = TransactionFactory.LOANS;
                    acctNum = txTransfer.getActNum();
                    transactionPaidAmt = CommonUtil.convertObjToDouble(txTransfer.getAmount()).doubleValue();
                    break;
                }
                if (txTransfer.getProdType().equals(TransactionFactory.ADVANCES)
                        && txTransfer.getTransType().equals("CREDIT")) {//This line added because if TransType is CREDIT then only should set ProdType & AcctNum otherwise it's not inserting record into ADV_TRANS_DETAILS table
                    prodType = TransactionFactory.ADVANCES;
                    acctNum = txTransfer.getActNum();
                    break;
                }
            }
            for (int j = 0; j < transferTOs.size(); j++) {
                txTransfer = new TxTransferTO();
                System.out.println("transferTOs &$&$&$ in second loop"+transferTOs.get(j));
                txTransfer = (TxTransferTO) transferTOs.get(j);
                HashMap interestMap = new HashMap();
                HashMap getDateMap = new HashMap();
                if (!CommonUtil.convertObjToStr(txTransfer.getScreenName()).equals("CHARGESUI") && (txTransfer.getProdType().equals(TransactionFactory.LOANS)
                        || txTransfer.getProdType().equals(TransactionFactory.ADVANCES)
                        || txTransfer.getProdType().equals(TransactionFactory.GL))) {
                    List list = null;
//                    if(acctNum !=null && acctNum.length()>0){
//                        interestMap.put(CommonConstants.ACT_NUM,acctNum);
//                        
//                        
//                    }else{
                    interestMap.put(CommonConstants.ACT_NUM, txTransfer.getLinkBatchId());
                    acctNum = (String) txTransfer.getLinkBatchId();
//                    }
                    if (interestMap.get(CommonConstants.ACT_NUM) != null) {
                        list = sqlMap.executeQueryForList("getBehavesLikeTLAD", interestMap);
                    }
                    if (list != null && list.size() > 0) {
                        HashMap behaves = (HashMap) list.get(0);
                        prodType = CommonUtil.convertObjToStr(behaves.get("BEHAVES_LIKE"));
                        prodType = prodType.equals("OD") ? "AD" : "TL";
                        interestMap.put("TRANS_DT", currDate.clone());
                        interestMap.put("INITIATED_BRANCH", _branchCode);
                        List intList = null;
                        if (prodType.equals(TransactionFactory.LOANS)) {
                            intList = sqlMap.executeQueryForList("IntCalculationDetail", interestMap);
                        }
                        if (prodType.equals(TransactionFactory.ADVANCES)) {
                            intList = sqlMap.executeQueryForList("IntCalculationDetailAD", interestMap);
                        }
                        if (intList != null && intList.size() > 0) {
                            getDateMap = (HashMap) intList.get(0);
                            getDateMap.put(CommonConstants.ACT_NUM, interestMap.get(CommonConstants.ACT_NUM));
                            getDateMap.put(PROD_ID, interestMap.get(PROD_ID));
                            getDateMap.put(BEHAVES_LIKE, behaves.get(BEHAVES_LIKE));
                        }
                    }
                    List lst = null;
                    if (list != null && list.size() > 0) {
                        if (prodType.equals(TransactionFactory.LOANS)) {
                            lst = (List) sqlMap.executeQueryForList("getIntDetails", interestMap);
                            //                            prodType=TransactionFactory.LOANS;
                        }
                        if (prodType.equals(TransactionFactory.ADVANCES)) {
                            lst = sqlMap.executeQueryForList("getIntDetailsAD", interestMap);
                            //                            prodType=TransactionFactory.ADVANCES;
                        }
                        if (lst != null && lst.size() > 0) {
                            interestMap = (HashMap) lst.get(0);
                            insetMap.put(TransactionDAOConstants.ACCT_NO, acctNum);
                            insetMap.put("TRANSTYPE", TransactionDAOConstants.CREDIT);
                           // insetMap.put(CommonConstants.BRANCH_ID, _branchCode);
                           //added by rishad for interbranch  issue  check mantis 0010806
                            insetMap.put(CommonConstants.BRANCH_ID,txTransfer.getBranchId()) ;
                            
                            transSlNo = CommonUtil.convertObjToLong(interestMap.get("TRANS_SLNO"));
                        }
                        if (prodType.equals(TransactionFactory.ADVANCES) || (getDateMap.get("AS_CUSTOMER_COMES") != null && getDateMap.get("AS_CUSTOMER_COMES").equals("Y"))) {
                            System.out.println("getDateMap.get(AS_CUSTOMER_COMES)#^^#^#"+getDateMap.get("AS_CUSTOMER_COMES"));
                              System.out.println("getDateMap.get(AS_CUSTOMER_COMES)txTransfer.getProdType()#^^#^#"+txTransfer.getProdType());
                            if (txTransfer.getProdType().equals(TransactionFactory.LOANS)) {
                                //EMI ONLY UPDATE STATUS
                                if (txTransfer.getAuthorizeRemarks() != null && (CommonUtil.convertObjToLong(txTransfer.getAuthorizeRemarks())) > 0) {
                                    HashMap map = new HashMap();
                                    long count = CommonUtil.convertObjToLong(txTransfer.getAuthorizeRemarks());
                                    map.put(CommonConstants.ACT_NUM, txTransfer.getLinkBatchId());
                                    List installmentLst = sqlMap.executeQueryForList("getMinimaminstallmentTL", map);
                                    if (installmentLst != null && installmentLst.size() > 0) {
                                        map = (HashMap) installmentLst.get(0);
                                        int instalmentNo = CommonUtil.convertObjToInt(map.get("INSTALLMENT_SLNO"));
                                        HashMap updateMap = new HashMap();
                                        for (int i = 0; i < count; i++) {
                                            updateMap = new HashMap();
                                            updateMap.put("ACCT_NUM", txTransfer.getLinkBatchId());
                                            updateMap.put("INSTALLMENT_NO", new Integer(instalmentNo));
                                            System.out.println("updateMap###" + updateMap);
                                            sqlMap.executeUpdate("updateloanInstallment", updateMap);
                                            instalmentNo++;
                                        }
                                        sqlMap.executeUpdate("updateEMIlastIntCalc", updateMap);

                                    }
                                }
                                if (txTransfer.getAuthorizeRemarks() != null && txTransfer.getAuthorizeRemarks().equals("PRINCIPAL_WAIVEOFF")) {

                                    if (txTransfer.getAuthorizeRemarks().equals("PRINCIPAL_WAIVEOFF")) {
                                        updateWaiveOffDetails(txTransfer);
                                    }

                                }
                                //END
                            }
                            
                            if (txTransfer.getProdType().equals(TransactionFactory.GL)) {
                                if (txTransfer.getTransType().equals("DEBIT")) {
                                    continue;
                                }
                                double amount = txTransfer.getAmount().doubleValue();
                                //                                long transSlNo=CommonUtil.convertObjToLong(interestMap.get("TRANS_SLNO"));
                                transSlNo++;
                                boolean otherCharges = true;

//                                List waiveOffList=(List)sqlMap.executeQueryForList("getSelectTermLoanWaiveOffTO",txTransfer.getLinkBatchId());
//                                if(waiveOffList !=null && waiveOffList.size()>0){
//                                    interestWaiveoffTransaction(txTransfer);
//                                }
                                // Added by nithya on 28-08-2017                                
                                if (CommonConstants.SAL_REC_MODULE != null && !CommonConstants.SAL_REC_MODULE.equals("")
                                        && CommonConstants.SAL_REC_MODULE.equalsIgnoreCase("Y")) {
                                    if (txTransfer.getAuthorizeRemarks() != null && txTransfer.getAuthorizeRemarks().equals(INTEREST)) {
                                        if (txTransfer.getProdType().equals(TransactionFactory.GL)) {
                                            if (txTransfer.getTransType().equals("CREDIT") && txTransfer.getInstDt() != null && txTransfer.getInstrumentNo1().equalsIgnoreCase("FUTURE_DT_SI_PROCESS")) {
                                                futureSILastIntCalcDt = getProperDateFormat(txTransfer.getInstDt());// Date formatting added by nithya on 31-08-2018 for KD-232 : RBIECS - Vehicle Loan Standing Instruction , While authorizing the transfer Entry Class Cast exception error showing in JBoss and needed to restart the Jboss to solve the issue
                                            }
                                        }
                                    }
                                }
                            // End
                                if (txTransfer.getAuthorizeRemarks() != null && (txTransfer.getAuthorizeRemarks().equals(INTEREST) || txTransfer.getAuthorizeRemarks().equals("REBATE_INTEREST") || txTransfer.getAuthorizeRemarks().equals("INTEREST_WAIVEOFF"))) {
                                    if (txTransfer.getAuthorizeRemarks().equals("REBATE_INTEREST")) {
                                        updateRebateFacilityDetails(txTransfer);
                                    }
                                    if (txTransfer.getAuthorizeRemarks().equals("INTEREST_WAIVEOFF")) {
                                        // updateWaiveOffInterestPenalDetails(txTransfer);
                                        updateWaiveOffDetails(txTransfer);
                                    }
                                    insetMap.put("IBAL", new Double(0));
                                    insetMap.put(INTEREST, amount);
                                    otherCharges = false;
                                } else {
                                    insetMap.put("IBAL", new Double(0));
                                    insetMap.put(INTEREST, new Double(0));
                                }
                                if (txTransfer.getAuthorizeRemarks() != null && (txTransfer.getAuthorizeRemarks().equals(PENAL_INT) || txTransfer.getAuthorizeRemarks().equals("PENAL_WAIVEOFF"))) {
                                    insetMap.put("PENAL", amount);
                                    insetMap.put("PIBAL", new Double(0));
                                    if (txTransfer.getAuthorizeRemarks().equals("PENAL_WAIVEOFF")) {
                                        //  updateWaiveOffInterestPenalDetails(txTransfer);
                                        updateWaiveOffDetails(txTransfer);
                                    }
                                    otherCharges = false;
                                } else {
                                    insetMap.put("PENAL", new Double(0));
                                    insetMap.put("PIBAL", new Double(0));
                                }
                                if (txTransfer.getAuthorizeRemarks() != null && (txTransfer.getAuthorizeRemarks().equals("ACT CLOSING CHARGE")
                                        || txTransfer.getAuthorizeRemarks().equals("ACT CLOSING MISC CHARGE"))) {
                                    insetMap.put("EXPENSE", amount);
                                    insetMap.put("EBAL", new Double(0));
                                    otherCharges = false;
                                } else {
                                    insetMap.put("EXPENSE", new Double(0));
                                    insetMap.put("EBAL", new Double(0));
                                }
                                //Notice charge 
                                  /*added by rishad 21/04/2014 for updating LOANS_ACCT_CHARGE_DETAILS At the Time Of Authorization*/
                                if (txTransfer.getAuthorizeRemarks() != null && (txTransfer.getAuthorizeRemarks().equals("NOTICE CHARGES") || txTransfer.getAuthorizeRemarks().equals("NOTICE_WAIVEOFF"))) {
                                    insetMap.put("NOTICE_CHARGES", amount);
                                    insetMap.put("NOTICE_CHARGE_BAL", new Double(0));
                                    if (txTransfer.getAuthorizeRemarks().equals("POSTAGE CHARGES")) {
                                        chargesCollected(txTransfer.getLinkBatchId(), txTransfer.getAuthorizeRemarks(), amount, null);
                                    } else {
                                        chargesCollected(txTransfer.getLinkBatchId(), "NOTICE CHARGES", amount, null);
                                    }
                                    if (txTransfer.getAuthorizeRemarks().equals("NOTICE_WAIVEOFF")) {
                                        updateWaiveOffDetails(txTransfer);
                                    }
                                    otherCharges = false;
                                } else {
                                    insetMap.put("NOTICE_CHARGES", new Double(0));
                                    insetMap.put("NOTICE_CHARGE_BAL", new Double(0));
                                }
                                //postage
                                if (txTransfer.getAuthorizeRemarks() != null && (txTransfer.getAuthorizeRemarks().equals("POSTAGE CHARGES") || txTransfer.getAuthorizeRemarks().equals("POSTAGE_WAIVE_OFF"))) {
                                    insetMap.put("POSTAGE_CHARGE", amount);
                                    insetMap.put("POSTAGE_CHARGE_BAL", new Double(0));
                                    if (txTransfer.getAuthorizeRemarks().equals("POSTAGE CHARGES")) {
                                        chargesCollected(txTransfer.getLinkBatchId(), txTransfer.getAuthorizeRemarks(), amount, null);
                                    } else {
                                        chargesCollected(txTransfer.getLinkBatchId(), "POSTAGE CHARGES", amount, null);
                                    }

                                    if (txTransfer.getAuthorizeRemarks().equals("POSTAGE_WAIVE_OFF")) {
                                        updateWaiveOffDetails(txTransfer);
                                    }
                                    otherCharges = false;
                                } else {
                                    insetMap.put("POSTAGE_CHARGE", new Double(0));
                                    insetMap.put("POSTAGE_CHARGE_BAL", new Double(0));
                                }
                                //advertise
                                if (txTransfer.getAuthorizeRemarks() != null && (txTransfer.getAuthorizeRemarks().equals("ADVERTISE CHARGES") || txTransfer.getAuthorizeRemarks().equals("ADVERTISE_WAIVE_OFF"))) {
                                    insetMap.put("ADVERTISE_CHARGES", amount);
                                    insetMap.put("ADVERTISE_CHARGES_BAL", new Double(0));
                                    if (txTransfer.getAuthorizeRemarks().equals("ADVERTISE CHARGES")) {
                                        chargesCollected(txTransfer.getLinkBatchId(), txTransfer.getAuthorizeRemarks(), amount, null);
                                    } else {
                                        chargesCollected(txTransfer.getLinkBatchId(), "ADVERTISE CHARGES", amount, null);
                                    }
                                    if (txTransfer.getAuthorizeRemarks().equals("ADVERTISE_WAIVE_OFF")) {
                                        updateWaiveOffDetails(txTransfer);
                                    }
                                    otherCharges = false;
                                } else {
                                    insetMap.put("ADVERTISE_CHARGES", new Double(0));
                                    insetMap.put("ADVERTISE_CHARGES_BAL", new Double(0));
                                }
                                //orbitary
                                if (txTransfer.getAuthorizeRemarks() != null && (txTransfer.getAuthorizeRemarks().equals("ARBITRARY CHARGES") || txTransfer.getAuthorizeRemarks().equals("ARBITRARY_WAIVE_OFF"))) {
                                    insetMap.put("ARBITARY_CHARGE", amount);
                                    insetMap.put("ARBITARY_CHARGE_BAL", new Double(0));
                                    if (txTransfer.getAuthorizeRemarks().equals("ARBITRARY CHARGES")) {
                                        chargesCollected(txTransfer.getLinkBatchId(), txTransfer.getAuthorizeRemarks(), amount, null);
                                    } else {
                                        chargesCollected(txTransfer.getLinkBatchId(), "ARBITRARY CHARGES", amount, null);
                                    }
                                    if (txTransfer.getAuthorizeRemarks().equals("ARBITRARY_WAIVE_OFF")) {
                                        updateWaiveOffDetails(txTransfer);
                                    }
                                    otherCharges = false;
                                } else {
                                    insetMap.put("ARBITARY_CHARGE", new Double(0));
                                    insetMap.put("ARBITARY_CHARGE_BAL", new Double(0));
                                }
                                //insurance
                                if (txTransfer.getAuthorizeRemarks() != null && (txTransfer.getAuthorizeRemarks().equals("INSURANCE CHARGES") || txTransfer.getAuthorizeRemarks().equals("INSURENCE_WAIVE_OFF"))) {
                                    insetMap.put("INSURANCE_CHARGE", amount);
                                    insetMap.put("INSURANCE_CHARGE_BAL", new Double(0));
                                    if (txTransfer.getAuthorizeRemarks().equals("INSURANCE CHARGES")) {
                                        chargesCollected(txTransfer.getLinkBatchId(), txTransfer.getAuthorizeRemarks(), amount, null);
                                    } else {
                                        chargesCollected(txTransfer.getLinkBatchId(), "INSURANCE CHARGES", amount, null);
                                    }
                                    if (txTransfer.getAuthorizeRemarks().equals("INSURENCE_WAIVE_OFF")) {
                                        updateWaiveOffDetails(txTransfer);
                                    }
                                    otherCharges = false;
                                } else {
                                    insetMap.put("INSURANCE_CHARGE", new Double(0));
                                    insetMap.put("INSURANCE_CHARGE_BAL", new Double(0));
                                }
                                //execDegree
                                if (txTransfer.getAuthorizeRemarks() != null && (txTransfer.getAuthorizeRemarks().equals("EXECUTION DECREE CHARGES") || txTransfer.getAuthorizeRemarks().equals("DECREE_WAIVE_OFF"))) {
                                    insetMap.put("EXE_DEGREE", amount);
                                    insetMap.put("EXE_DEGREE_BAL", new Double(0));
                                    if (txTransfer.getAuthorizeRemarks().equals("EXECUTION DECREE CHARGES")) {
                                        chargesCollected(txTransfer.getLinkBatchId(), txTransfer.getAuthorizeRemarks(), amount, null);
                                    } else {
                                        chargesCollected(txTransfer.getLinkBatchId(), "EXECUTION DECREE CHARGES", amount, null);
                                    }
                                    if (txTransfer.getAuthorizeRemarks().equals("DECREE_WAIVE_OFF")) {
                                        updateWaiveOffDetails(txTransfer);
                                    }
                                    otherCharges = false;
                                } else {
                                    insetMap.put("EXE_DEGREE", new Double(0));
                                    insetMap.put("EXE_DEGREE_BAL", new Double(0));
                                }
                                //misc
                                if (txTransfer.getAuthorizeRemarks() != null && (txTransfer.getAuthorizeRemarks().equals("MISCELLANEOUS CHARGES") || txTransfer.getAuthorizeRemarks().equals("MISCELLANEOUS_WAIVE_OFF"))) {
                                    insetMap.put("MISC_CHARGES", amount);
                                    insetMap.put("MISC_CHARGES_BAL", new Double(0));
                                    if (txTransfer.getAuthorizeRemarks().equals("MISCELLANEOUS CHARGES")) {
                                        chargesCollected(txTransfer.getLinkBatchId(), txTransfer.getAuthorizeRemarks(), amount, null);
                                    } else {
                                        chargesCollected(txTransfer.getLinkBatchId(), "MISCELLANEOUS CHARGES", amount, null);
                                    }
                                    if (txTransfer.getAuthorizeRemarks().equals("MISCELLANEOUS_WAIVE_OFF")) {
                                        updateWaiveOffDetails(txTransfer);
                                    }
                                    otherCharges = false;
                                } else {
                                    insetMap.put("MISC_CHARGES", new Double(0));
                                    insetMap.put("MISC_CHARGES_BAL", new Double(0));
                                }
                                //legal
                                if (txTransfer.getAuthorizeRemarks() != null && (txTransfer.getAuthorizeRemarks().equals("LEGAL CHARGES") || txTransfer.getAuthorizeRemarks().equals("LEGAL_WAIVE_OFF"))) {
                                    insetMap.put("LEGAL_CHARGE", amount);
                                    insetMap.put("LEGAL_CHARGE_BAL", new Double(0));
                                    if (txTransfer.getAuthorizeRemarks().equals("LEGAL CHARGES")) {
                                        chargesCollected(txTransfer.getLinkBatchId(), txTransfer.getAuthorizeRemarks(), amount, null);
                                    } else {
                                        chargesCollected(txTransfer.getLinkBatchId(), "LEGAL CHARGES", amount, null);
                                    }
                                    if (txTransfer.getAuthorizeRemarks().equals("LEGAL_WAIVE_OFF")) {
                                        updateWaiveOffDetails(txTransfer);
                                    }
                                    otherCharges = false;
                                } else {
                                    insetMap.put("LEGAL_CHARGE", new Double(0));
                                    insetMap.put("LEGAL_CHARGE_BAL", new Double(0));
                                }
                                //for arc 

                                if (txTransfer.getAuthorizeRemarks() != null && txTransfer.getAuthorizeRemarks().equals("EA")) {
                                    insetMap.put("EXPENSE", amount);
                                    insetMap.put("EBAL", new Double(0));
                                    chargesCollected(txTransfer.getLinkBatchId(), txTransfer.getAuthorizeRemarks(), amount, txTransfer.getParticulars());
                                    otherCharges = false;
                                } else if (txTransfer.getAuthorizeRemarks() != null && (txTransfer.getAuthorizeRemarks().equals("EP") || txTransfer.getAuthorizeRemarks().equals("EP_WAIVE_OFF"))) {
                                    insetMap.put("EXPENSE", amount);
                                    insetMap.put("EBAL", new Double(0));
                                    if (txTransfer.getAuthorizeRemarks().equals("EP")) {
                                        chargesCollected(txTransfer.getLinkBatchId(), txTransfer.getAuthorizeRemarks(), amount, null);
                                    } else {
                                        chargesCollected(txTransfer.getLinkBatchId(), "EP", amount, null);
                                    }
                                    if (txTransfer.getAuthorizeRemarks().equals("EP_WAIVE_OFF")) {
                                        updateWaiveOffDetails(txTransfer);
                                    }
                                    otherCharges = false;
                                } else if (txTransfer.getAuthorizeRemarks() != null &&( txTransfer.getAuthorizeRemarks().equals("ARC") || txTransfer.getAuthorizeRemarks().equals("ARC_WAIVEOFF"))) {
                                    insetMap.put("EXPENSE", amount);
                                    insetMap.put("EBAL", new Double(0));
                                    if (txTransfer.getAuthorizeRemarks().equals("ARC")) {
                                        chargesCollected(txTransfer.getLinkBatchId(), txTransfer.getAuthorizeRemarks(), amount, null);
                                    } else {
                                        chargesCollected(txTransfer.getLinkBatchId(), "ARC", amount, null);
                                    }
                                    if (txTransfer.getAuthorizeRemarks().equals("ARC_WAIVEOFF")) {
                                        updateWaiveOffDetails(txTransfer);
                                    }
                                    otherCharges = false;
                                }else if (txTransfer.getAuthorizeRemarks() != null &&( txTransfer.getAuthorizeRemarks().equals("RECOVERY CHARGES") || txTransfer.getAuthorizeRemarks().equals("RECOVERY_WAIVE_OFF"))) {
                                    insetMap.put("EXPENSE", amount);
                                    insetMap.put("EBAL", new Double(0));
                                    System.out.println("amount .... hhh ::"+ amount);
                                    if (txTransfer.getAuthorizeRemarks().equals("RECOVERY CHARGES")) {
                                        chargesCollected(txTransfer.getLinkBatchId(), txTransfer.getAuthorizeRemarks(), amount, null);
                                    } else {
                                        chargesCollected(txTransfer.getLinkBatchId(), "RECOVERY CHARGES", amount, null);
                                    }
                                    if (txTransfer.getAuthorizeRemarks().equals("RECOVERY_WAIVE_OFF")) {
                                        updateWaiveOffDetails(txTransfer);
                                    }
                                    otherCharges = false;
                                }else if (txTransfer.getAuthorizeRemarks() != null &&( txTransfer.getAuthorizeRemarks().equals("MEASUREMENT CHARGES") || txTransfer.getAuthorizeRemarks().equals("MEASUREMENT_WAIVE_OFF"))) {
                                    insetMap.put("EXPENSE", amount);
                                    insetMap.put("EBAL", new Double(0));
                                    if (txTransfer.getAuthorizeRemarks().equals("MEASUREMENT CHARGES")) {
                                        chargesCollected(txTransfer.getLinkBatchId(), txTransfer.getAuthorizeRemarks(), amount, null);
                                    } else {
                                        chargesCollected(txTransfer.getLinkBatchId(), "MEASUREMENT CHARGES", amount, null);
                                    }
                                    if (txTransfer.getAuthorizeRemarks().equals("MEASUREMENT_WAIVE_OFF")) {
                                        updateWaiveOffDetails(txTransfer);
                                    }
                                    otherCharges = false;
                                }
                                // for overdue interest 
                                if (txTransfer.getAuthorizeRemarks() != null && txTransfer.getAuthorizeRemarks().equals("OVERDUE_INT")) {//Added By Kannan AR
                                    //System.out.println("amount"+amount);                                    
                                   insetMap.put("OVER_DUE_INT", amount);
                                    insetMap.put("PIBAL", new Double(0));
                                    if (txTransfer.getAuthorizeRemarks().equals("OVERDUEINT_WAIVEOFF")) {
                                        //  updateWaiveOffInterestPenalDetails(txTransfer);
                                        updateWaiveOffDetails(txTransfer);
                                    }
                                    otherCharges = false;
                                } else {
                                    insetMap.put("OVER_DUE_INT", new Double(0));
                                }
                                // End
                                
                                // Added by nithya on 29-11-2018 for KD 335 - In Advance transaction notice charge not inserting advtrans detail table
                                    if (prodType.equals(TransactionFactory.ADVANCES)) {
                                        //System.out.println("executing inside....adv");
                                        if (txTransfer.getAuthorizeRemarks() != null && txTransfer.getAuthorizeRemarks().equals("NOTICE CHARGES")) {
                                          otherCharges = true;
                                        }
                                    }
                                 // End


                                if (otherCharges) {
                                    chargesCollected(txTransfer.getLinkBatchId(), txTransfer.getAuthorizeRemarks(), amount, null);
                                    insetMap.put("EXPENSE", amount);
                                }

                                insetMap.put("TODAY_DT", properFormatDate.clone());
                                insetMap.put("TRANS_SLNO", new Long(transSlNo));
                                insetMap.put("PRINCIPAL", new Double(0));

                                //                            insetMap.put("PBAL",interestMap.get("PBAL"));
                                double zero = CommonUtil.convertObjToDouble("0").doubleValue();
                                insetMap.put("NPA_INTEREST", CommonUtil.convertObjToDouble(0));
                                insetMap.put("NPA_INT_BAL", CommonUtil.convertObjToDouble(0));
                                insetMap.put("NPA_PENAL", CommonUtil.convertObjToDouble(0));
                                insetMap.put("NPA_PENAL_BAL", CommonUtil.convertObjToDouble(0));
                                insetMap.put("EXCESS_AMT", new Double(0));
//                                amount = CommonUtil.convertObjToDouble(interestMap.get("PBAL")).doubleValue();
                                insetMap.put("PBAL", CommonUtil.convertObjToDouble(interestMap.get("PBAL")));
                                insetMap.put(AUTHORIZE_STATUS, "AUTHORIZED");
                                //                            insetMap.put("EXPENSE",new Double(0));
                                //                            insetMap.put("EBAL",new Double(0));

                                insetMap.put(CommonConstants.TRANS_ID, txTransfer.getBatchId() + "_" + txTransfer.getTransId());
                                if (otherCharges) {
                                    insetMap.put("PARTICULARS", txTransfer.getAuthorizeRemarks() == null
                                            ? txTransfer.getParticulars() : txTransfer.getAuthorizeRemarks());
                                } else {
                                    insetMap.put("PARTICULARS", txTransfer.getParticulars());
                                }
                                insetMap.put("TRANS_MODE", "TRANSFER");
                                insetMap.put("TRN_CODE", String.valueOf("C*"));
                                String uptoDtYN = "N";
//                                uptoDtYN=asAnWhenCustomer(getDateMap);
                                insetMap.put("UPTO_DT_INT", String.valueOf(uptoDtYN));
                                System.out.println("insetMap######" + insetMap);

                                if (prodType.equals(TransactionFactory.LOANS) && txTransfer.getAuthorizeRemarks() != null && !txTransfer.getAuthorizeRemarks().equals("NOT_INCLUDEDINTL")) {
                                    sqlMap.executeUpdate("insertLoansDisbursementDetailsCumLoan", insetMap);
                                } //Modified By Nidhin for agent commsion inserted to LOAN_TRANS_DETAILS
                                else if (prodType.equals(TransactionFactory.LOANS) && txTransfer.getAuthorizeRemarks() == null) {
                                    sqlMap.executeUpdate("insertLoansDisbursementDetailsCumLoan", insetMap);
                                }
                                if (prodType.equals(TransactionFactory.ADVANCES)) {
//                                    insetMap.put(INTEREST,String.valueOf(insetMap.get(INTEREST)));
                                    sqlMap.executeUpdate("insertAuthorizeAdvTransDetails", insetMap);
                                }
                                if (closingMap != null && closingMap.containsKey("LOAN_CLOSING_SCREEN")) {
                                    getDateMap.put("LOAN_CLOSING_SCREEN", "LOAN_CLOSING_SCREEN");
                                }
                                if (closingMap.containsKey("VOUCHER_RELEASE")) {
                                    getDateMap.put("VOUCHER_RELEASE", CommonUtil.getProperDate(currDate, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(closingMap.get("VOUCHER_RELEASE")))));
                                }
                                if (closingMap.containsKey("REC_PRINCIPAL")) {
                                    getDateMap.put("REC_PRINCIPAL", CommonUtil.convertObjToDouble(closingMap.get("REC_PRINCIPAL")));
                                }
                                if (closingMap.containsKey("DEDUCTION_SI")) {
                                    getDateMap.put("DEDUCTION_SI", closingMap.get("DEDUCTION_SI"));
                                }
                                getDateMap.put("USER_ID", closingMap.get("USER_ID"));
                                getDateMap.put("TRANSACTION_PAID_AMT",transactionPaidAmt);
                                HashMap resultMap = asAnWhenCustomer(getDateMap);
                                uptoDtYN = CommonUtil.convertObjToStr(resultMap.get("UPTO_DT"));
                                if (uptoDtYN.equals("Y")) {
                                    sqlMap.executeUpdate("updateinterestYes", insetMap);
                                }
                                interestMap = new HashMap();
                            }
                        }
                    }
                }
            }
            //regarding mahila
            if (batch_id != null && (acctNum != null && (!acctNum.equals("")))) {
                HashMap map = new HashMap();
                map.put("BATCH_ID", batch_id);
                map.put("ACT_NUM", acctNum);
                List lst = sqlMap.executeQueryForList("selectRetraspectiveBatchId", map);
                if (lst != null && lst.size() > 0) {
                    map = (HashMap) lst.get(0);
                    HashMap update = new HashMap();
                    update.put("ACCOUNTNO", acctNum);
                    update.put("UPDATE_RET_APP_DT", map.get("AUTHORIZE_DT_2"));

                    sqlMap.executeUpdate("updateRetrasPectiveDt", update);
                }

            }
        } else if (status.equals(CommonConstants.STATUS_REJECTED)) {
            for (int j = 0; j < transferTOs.size(); j++) {
                insetMap = new HashMap();
                txTransfer = new TxTransferTO();
                txTransfer = (TxTransferTO) transferTOs.get(j);
                if (txTransfer.getProdType().equals(TransactionFactory.GL) && CommonUtil.convertObjToStr(txTransfer.getLinkBatchId()).length() > 0) {
                    insetMap.put("ACCT_NUM", txTransfer.getLinkBatchId());
                    List lst = sqlMap.executeQueryForList("selectLoanRebateTO", insetMap);
                    if (lst != null && lst.size() > 0) {
                        LoanRebateTO obj = new LoanRebateTO();
                        obj = (LoanRebateTO) lst.get(0);
                        HashMap update = new HashMap();
                        update.put("ACCT_NUM", obj.getAccNo());
                        update.put("REBATE_ID", obj.getRebateId());
                        update.put("AUTHORIZE_STATUS", CommonConstants.STATUS_REJECTED);
                        update.put("AUTHORIZE_BY", user);
                        update.put("AUTHORIZE_DT", properFormatDate);
                        sqlMap.executeUpdate("authorizeLoanRebate", update);
                        break;
                    }

                }
            }


        }
    }
    //as an whenc customer updation of charges

    private void chargesCollected(String act_num, String chargeType, double amount, String caseAccountParticulares) throws Exception {
        HashMap chargeMap = new HashMap();
        chargeMap.put(CommonConstants.ACT_NUM, act_num);
        chargeMap.put("CHARGE_TYPE", chargeType);
        double amt = amount;
        List lst = sqlMap.executeQueryForList("getChargeDetailsforUpdate", chargeMap);
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                chargeMap = (HashMap) lst.get(i);
                chargeMap.put("CHARGE_NO", CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_NO"))); //avoid class cast exception
                chargeMap.put("CHARGE_TYPE", CommonUtil.convertObjToStr(chargeMap.get("CHARGE_TYPE"))); //avoid class cast exception
                chargeMap.put("ACT_NUM", CommonUtil.convertObjToStr(chargeMap.get("ACT_NUM"))); //avoid class cast exception
                double paidAmt = CommonUtil.convertObjToDouble(chargeMap.get("PAID_AMT")).doubleValue();
                double payableAmt = CommonUtil.convertObjToDouble(chargeMap.get("PAYABLE_AMT")).doubleValue();
                if (amt > 0) {
                    if (amt > payableAmt) {
                        amt -= payableAmt;
                        chargeMap.put("PAID_AMT", new Double(paidAmt + payableAmt));
                    } else {
                        chargeMap.put("PAID_AMT", new Double(paidAmt + amt));
                        amt = 0;
                    }
                    sqlMap.executeUpdate("updateChargeDetails", chargeMap);
                }
            }
        }

//       lst= sqlMap.executeQueryForList("getSelectTermLoanCaseChargeDetails",chargeMap);
//        if(lst!=null && lst.size()>0){
//            boolean isExpence=false;
//             System.out.println("caseAccountParticularesbefore####"+caseAccountParticulares);
//             if(caseAccountParticulares !=null)
//             caseAccountParticulares=caseAccountParticulares.substring(caseAccountParticulares.length()-7 ,caseAccountParticulares.length());
//            if(caseAccountParticulares.equals("EXPENCE"))
//                  isExpence=true;
//            System.out.println("caseAccountParticulares####"+caseAccountParticulares);
//            for(int i=0;i<lst.size();i++){
//                chargeMap=(HashMap)lst.get(i);
//                chargeMap.put("CHARGE_TYPE",chargeType);
//                if(!chargeType.equals(CommonUtil.convertObjToStr(chargeMap.get("CASE_STATUS"))))
//                    continue;
//                double paidAmt=CommonUtil.convertObjToDouble(chargeMap.get("PAID_FILING_FEES")).doubleValue();
//                double payableAmt=CommonUtil.convertObjToDouble(chargeMap.get("FILING_FEES")).doubleValue();
//                if(caseAccountParticulares !=null && caseAccountParticulares.endsWith("EXPENCE")){
//                     paidAmt=CommonUtil.convertObjToDouble(chargeMap.get("PAID_MISC_FEES")).doubleValue();
//                     payableAmt=CommonUtil.convertObjToDouble(chargeMap.get("MISC_CHARGES")).doubleValue();
//                }
//                if(amt>0){
//                    if(amt>payableAmt){
//                        amt-=payableAmt;
//                        if(isExpence){
//                        chargeMap.put("PAID_MISC_FEES",new Double(paidAmt+payableAmt));
////                        chargeMap.put("PAID_FILING_FESS_AMT",new Double(0));
//                        }else{
//                             chargeMap.put("PAID_FILING_FESS_AMT",new Double(paidAmt+payableAmt));
////                              chargeMap.put("PAID_MISC_FEES",new Double(0));
//                        }
//                    }else {
//                        if(isExpence)
//                            chargeMap.put("PAID_MISC_FEES",new Double(paidAmt+amt));
//                        else
//                            chargeMap.put("PAID_FILING_FESS_AMT",new Double(paidAmt+amt));
////                        chargeMap.put("PAID_AMT",new Double(paidAmt+amt));
//                        amt=0;
//                    }
//                    System.out.println("chargeMap#####"+chargeMap);
//                    if(isExpence)
//                        sqlMap.executeUpdate("updatePaidCaseExpenceAmt", chargeMap);
//                    else
//                        sqlMap.executeUpdate("updatePaidCaseFileAmt", chargeMap);
//                }
//            }
//        }

    }

    private HashMap depositInterestTransfer(HashMap depIntMap) throws Exception {
        System.out.println("##### DEP_INTEREST_AMT :" + depIntMap);
        double balPay = 0.0;
        double balPayable = 0.0;
        double balInt = 0.0;
        double intAmt = CommonUtil.convertObjToDouble(depIntMap.get(TransactionDAOConstants.AMT)).doubleValue();
        String depositSubNo = CommonUtil.convertObjToStr(depIntMap.get(DEPOSIT_NO));
        String branchID = CommonUtil.convertObjToStr(depIntMap.get(CommonConstants.BRANCH_ID));
        if (depositSubNo.lastIndexOf("_") != -1) {
            depositSubNo = depositSubNo.substring(0, depositSubNo.lastIndexOf("_"));
        }
        HashMap balanceMap = new HashMap();
        balanceMap.put(DEPOSIT_NO, depositSubNo);
        List lst = sqlMap.executeQueryForList("getTotalIntBalanceForDeposit", balanceMap);
        if (lst.size() > 0) {
            balanceMap = (HashMap) lst.get(0);
            System.out.println("##### balanceMap :" + balanceMap);
            HashMap behavesMap = new HashMap();
            behavesMap.put(PROD_ID, depIntMap.get(PROD_ID));
            lst = sqlMap.executeQueryForList("getBehavesLikeForDeposit", behavesMap);
            if (lst.size() > 0) {
                behavesMap = (HashMap) lst.get(0);
            }
            if (behavesMap.get(BEHAVES_LIKE).equals("FIXED")) {
                double totIntAmt = CommonUtil.convertObjToDouble(balanceMap.get("TOT_INT_AMT")).doubleValue();
                double totIntCredit = CommonUtil.convertObjToDouble(balanceMap.get("TOTAL_INT_CREDIT")).doubleValue();
                double totIntDrawn = CommonUtil.convertObjToDouble(balanceMap.get("TOTAL_INT_DRAWN")).doubleValue();
                balInt = totIntAmt - totIntCredit;
                balPayable = totIntCredit - totIntDrawn;
                balPay = intAmt - balPayable;
            } else {
                balPay = intAmt;
            }
            lst = sqlMap.executeQueryForList("getDepositClosingHeads", depIntMap);
            if (lst.size() > 0) {
                depIntMap = (HashMap) lst.get(0);
                if (balPay > 0) {
                    HashMap txMap = new HashMap();
                    ArrayList transferList = new ArrayList();
                    txMap.put(TransferTrans.DR_AC_HD, (String) depIntMap.get("INT_PROV_ACHD"));//debiting to int paid a/c head
                    txMap.put(TransferTrans.PARTICULARS, depositSubNo + "_1");


                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);

                    txMap.put(TransferTrans.CR_AC_HD, (String) depIntMap.get("INT_PAY")); // Debited to interest payable account head......
                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                    txMap.put(TransferTrans.PARTICULARS, depositSubNo + "_1");
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.DR_INSTRUMENT_2, "DEPOSIT_RENEWAL");
                    txMap.put(TransferTrans.DR_INSTRUMENT_1, "INTEREST_AMT");
                    System.out.println("DEP_INTEREST_AMT txMap:" + txMap);

                    TransferTrans trans = new TransferTrans();

                    trans.setTransMode(CommonConstants.TX_TRANSFER);
                    trans.setLinkBatchId(CommonUtil.convertObjToStr(depositSubNo + "_1"));
                    trans.setInitiatedBranch(_branchCode);
                    transferList.add(trans.getDebitTransferTO(txMap, balPay));
                    transferList.add(trans.getCreditTransferTO(txMap, balPay));
                    trans.doDebitCredit(transferList, _branchCode, false);
                }
            }
        }
        depIntMap.put(DEPOSIT_NO, depositSubNo + "_1");
        depIntMap.put(CommonConstants.BRANCH_ID, branchID);
        balPay = balPay + balPayable;
        depIntMap.put("BALANCE_AMT", new Double(balPay));
        return depIntMap;

    }

    private void updateProcessingChargesForLoan(HashMap procHash) throws Exception {
        double procAmt = CommonUtil.convertObjToDouble(procHash.get("PROC_AMT")).doubleValue();
        HashMap txMap = new HashMap();
        if (procAmt > 0) {
            txMap = new HashMap();
            ArrayList transferList = new ArrayList(); // for local transfer
            txMap.put(TransferTrans.DR_PROD_ID, (String) procHash.get(PROD_ID));
            txMap.put(TransferTrans.DR_ACT_NUM, (String) procHash.get(CommonConstants.ACT_NUM));
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
            List lst = (List) sqlMap.executeQueryForList("getProcChargeAcHd", procHash.get("LOAN_PROD_ID"));
            HashMap acHeads = (HashMap) lst.get(0);
            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("PROC_CHRG"));
            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
            txMap.put(TransferTrans.CURRENCY, "INR");
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            System.out.println("####### insertAccHead txMap 11==" + txMap);
            TransferTrans trans = new TransferTrans();
            trans.setTransMode(CommonConstants.TX_TRANSFER);
            trans.setInitiatedBranch(_branchCode);

            transferList.add(trans.getDebitTransferTO(txMap, procAmt));
            transferList.add(trans.getCreditTransferTO(txMap, procAmt));
            trans.doDebitCredit(transferList, _branchCode);
        }
    }

    private Double getTransAmount(TxTransferTO objTO) throws Exception {
        String transType = objTO.getTransType();
        Double amount = objTO.getAmount();

        if (transType.equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
            amount = new Double(String.valueOf("-" + amount.toString()));
        }
        return amount;
    }
    /*
     * suppose throw the ui not taking any charges then this methode call and
     * working *
     */

    private HashMap getAsAnWhenChargesDetails(HashMap whereMap) throws Exception {
        List lst = sqlMap.executeQueryForList("getSelectTermLoanChargeDetailsTO", whereMap.get("ACT_NUM"));
        HashMap singleMap = new HashMap();
        HashMap resultMap = new HashMap();
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                TermLoanChargesTO objTermLoanCharge = (TermLoanChargesTO) lst.get(i);
                singleMap.put("ACT_NUM", objTermLoanCharge.getAct_num());
                singleMap.put("CHARGE_TYPE", objTermLoanCharge.getCharge_Type());
                List lstCharge = sqlMap.executeQueryForList("getChargeDetailsforUpdate", singleMap);
                if (lstCharge != null && lstCharge.size() > 0) {
                    HashMap chargeMap = (HashMap) lstCharge.get(0);
                    double payableAmt = CommonUtil.convertObjToDouble(chargeMap.get("PAYABLE_AMT")).doubleValue();
                    System.out.println("chargeMap###" + chargeMap + payableAmt);
                    if (chargeMap.containsValue("POSTAGE CHARGES") && payableAmt != 0) {
                        resultMap.put("POSTAGE CHARGES", chargeMap.get("PAYABLE_AMT"));
                    }
                    if (chargeMap.containsValue("ARBITRARY CHARGES") && payableAmt != 0) {
                        resultMap.put("ARBITRARY CHARGES", chargeMap.get("PAYABLE_AMT"));
                    }

                    if (chargeMap.containsValue("LEGAL CHARGES") && payableAmt != 0) {
                        resultMap.put("LEGAL CHARGES", chargeMap.get("PAYABLE_AMT"));
                    }

                    if (chargeMap.containsValue("MISCELLANEOUS CHARGES") && payableAmt != 0) {
                        resultMap.put("MISCELLANEOUS CHARGES", chargeMap.get("PAYABLE_AMT"));
                    }

                    if (chargeMap.containsValue("EXECUTION DECREE CHARGES") && payableAmt != 0) {
                        resultMap.put("EXECUTION DECREE CHARGES", chargeMap.get("PAYABLE_AMT"));
                    }

                    if (chargeMap.containsValue("INSURANCE CHARGES") && payableAmt != 0) {
                        resultMap.put("INSURANCE CHARGES", chargeMap.get("PAYABLE_AMT"));
                    }

                    if (chargeMap.containsValue("NOTICE CHARGES") && payableAmt != 0) {
                        resultMap.put("NOTICE CHARGES", chargeMap.get("PAYABLE_AMT"));
                    }

                    if (chargeMap.containsValue("EP_COST") && payableAmt != 0) {
                        resultMap.put("EP_COST", chargeMap.get("PAYABLE_AMT"));
                    }

                    if (chargeMap.containsValue("ARC_COST") && payableAmt != 0) {
                        resultMap.put("ARC_COST", chargeMap.get("PAYABLE_AMT"));
                    }
                    
                    if (chargeMap.containsValue("RECOVERY CHARGES") && payableAmt != 0) {
                        resultMap.put("RECOVERY CHARGES", chargeMap.get("PAYABLE_AMT"));
                    }
                    
                    if (chargeMap.containsValue("MEASUREMENT CHARGES") && payableAmt != 0) {
                        resultMap.put("MEASUREMENT CHARGES", chargeMap.get("PAYABLE_AMT"));
                    }
                    
                }
            }
        }
        System.out.println("resultMapcharges$$$$$" + resultMap);
        return resultMap;
    }

    /**
     * Getter for property forProcCharge.
     *
     * @return Value of property forProcCharge.
     */
    public boolean isForProcCharge() {
        return forProcCharge;
    }

    /**
     * Setter for property forProcCharge.
     *
     * @param forProcCharge New value of property forProcCharge.
     */
    public void setForProcCharge(boolean forProcCharge) {
        this.forProcCharge = forProcCharge;
    }

    /**
     * Getter for property forLoanDebitInt.
     *
     * @return Value of property forLoanDebitInt.
     */
    public boolean isForLoanDebitInt() {
        return forLoanDebitInt;
    }

    /**
     * Setter for property forLoanDebitInt.
     *
     * @param forLoanDebitInt New value of property forLoanDebitInt.
     */
    public void setForLoanDebitInt(boolean forLoanDebitInt) {
        this.forLoanDebitInt = forLoanDebitInt;
    }

    public void updatepassbook(TxTransferTO txTransferTO) throws Exception {
        if (txTransferTO.getProdType().equals(TransactionFactory.OPERATIVE) || txTransferTO.getProdType().equals(TransactionFactory.ADVANCES)) {
            //            map.put(CommonConstants.ACT_NUM,txTransferTO.getActNum());
            //
            //            System.out.println("@@@@@@@@@@@@$$%^^%map"+map);
            //            List lst = (List)sqlMap.executeQueryForList("chkForPassBook",map);
            //            System.out.println("@@@@@@@@@@@@$$%^^%list"+lst);
            //            System.out.println("@@@@@@@@@@@@$$%^^%list"+lst.size());
            //            map=null;
            //            if(lst.size()>0) {
            //                map=(HashMap)lst.get(0);
            //                System.out.println("@@@@@@@@@@@@$$%^^%maptochecky"+map);
            //                if(map.containsValue("Y")){
            double finamt = 0;
            HashMap where = new HashMap();
            if (txTransferTO.getTransType().equals(TransactionDAOConstants.CREDIT)) {
                where.put(TransactionDAOConstants.CREDIT, txTransferTO.getAmount());
            } else if (txTransferTO.getTransType().equals(TransactionDAOConstants.DEBIT)) {
                where.put(TransactionDAOConstants.DEBIT, txTransferTO.getAmount());
            }
            HashMap map1 = new HashMap();
            map1.put(CommonConstants.ACT_NUM, txTransferTO.getActNum());
            List lst1 = null;
            if (txTransferTO.getAuthorizeRemarks() != null && txTransferTO.getAuthorizeRemarks().equals("FROM_BILLS_MODULE") && txTransferTO.getProdType().equals(TransactionFactory.ADVANCES)) {
                lst1 = (List) sqlMap.executeQueryForList("getClearBalance" + "BILLS", map1);
            } else {
                lst1 = (List) sqlMap.executeQueryForList("getClearBalance" + txTransferTO.getProdType(), map1);
            }
        //    System.out.println("@@@@@@@@@@@@$$%^^%listdebit" + lst1);
            map1 = null;
            if (lst1 != null && lst1.size() > 0) {
                map1 = (HashMap) lst1.get(0);
                finamt = CommonUtil.convertObjToDouble(map1.get("TOTAL_BALANCE")).doubleValue();
            }
           // System.out.println(txTransferTO.getParticulars() + "@@@@@@@@@@@@$$%^^%finamtdebit" + finamt);
//            properFormatDate = ServerUtil.getCurrentDate(_bankCode, _branchCode);
            Date tempDt = (Date) properFormatDate.clone();
            where.put("BALANCE", new Double(finamt));
            where.put(CommonConstants.TRANS_ID, txTransferTO.getTransId());
            where.put(BATCH_ID, txTransferTO.getBatchId());
            where.put(CommonConstants.ACT_NUM, txTransferTO.getActNum());
            tempDt.setDate(txTransferTO.getTransDt().getDate());
            tempDt.setMonth(txTransferTO.getTransDt().getMonth());
            tempDt.setYear(txTransferTO.getTransDt().getYear());
            where.put("TRANS_DT", tempDt);
            String particulars = txTransferTO.getParticulars();
            if (txTransferTO.getInstrumentNo1() != null && txTransferTO.getInstrumentNo1().length() > 0
                    && txTransferTO.getInstrumentNo1().equals("INTEREST_AMT")) {
                txTransferTO.setInstrumentNo1("");
            }
//            if(txTransferTO.getInstrumentNo1() !=null && txTransferTO.getInstrumentNo1().length()>0 &&
//            txTransferTO.getInstrumentNo2().equals("DEPOSIT_RENEWAL"))
            if (txTransferTO.getInstrumentNo2() != null && txTransferTO.getInstrumentNo2().length() > 0
                    && txTransferTO.getInstrumentNo2().equals("DEPOSIT_RENEWAL")) {
                txTransferTO.setInstrumentNo2("");
            }
            if (particulars != null) {
                where.put("PARTICULARS", txTransferTO.getParticulars());
            } else {
                where.put("PARTICULARS", "SYS");
            }
            where.put("INSTRUMENT_NO1", txTransferTO.getInstrumentNo1());
            where.put("INSTRUMENT_NO2", txTransferTO.getInstrumentNo2());
            where.put("PBOOKFLAG", "0");
            //                    where.put("SLNO", "0");
            where.put("INST_TYPE", txTransferTO.getInstType());
            tempDt = (Date) properFormatDate.clone();
            if (txTransferTO.getInstDt() != null) {
                tempDt.setDate(txTransferTO.getInstDt().getDate());
                tempDt.setMonth(txTransferTO.getInstDt().getMonth());
                tempDt.setYear(txTransferTO.getInstDt().getYear());
                where.put("INST_DT", tempDt);
            } else {
                where.put("INST_DT", txTransferTO.getInstDt());
            }
            where.put("STATUS", txTransferTO.getStatus());
            where.put(AUTHORIZE_STATUS, CommonConstants.STATUS_AUTHORIZED);
            where.put("AUTHORIZE_DT", properFormatDate);
            where.put("NARRATION", txTransferTO.getNarration());
       //     System.out.println("@@@@@@@@@@@@$$%^^%where" + where);
            sqlMap.executeUpdate("insertPassBook", where);
            where = null;
            map1 = null;
            lst1 = null;
            //                }
            //            }

        }

    }

    /**
     * Getter for property loanParticulars.
     *
     * @return Value of property loanParticulars.
     */
    public java.lang.String getLoanParticulars() {
        return loanParticulars;
    }

    /**
     * Setter for property loanParticulars.
     *
     * @param loanParticulars New value of property loanParticulars.
     */
    public void setLoanParticulars(java.lang.String loanParticulars) {
        this.loanParticulars = loanParticulars;
    }

    private String getLoanTransId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "LOAN_COLLECTION_ID");
        String loanCollectionId = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return loanCollectionId;
    }
    public void InsertintoDailyDepositCashTabl(ArrayList dailyDepList, HashMap map) throws Exception {
        try {
            int dailySize = dailyDepList.size();
            int Count = 0;
            String agentid = "";
            if (map.containsKey(ACTION) && !CommonUtil.convertObjToStr(map.get(ACTION)).equals("NEW")) {
                List lst = new ArrayList();
                map.put("TRANS_DT", currDate.clone());
                map.put("INITIATED_BRANCH", _branchCode);
                lst = sqlMap.executeQueryForList("getSelectCashTransactionTODAILY", map);
                if (lst != null && lst.size() > 0) {
                    for (int j = 0; j < lst.size(); j++) {
                        DailyDepositTransTO deleteDailyTo = new DailyDepositTransTO();
                        deleteDailyTo = (DailyDepositTransTO) lst.get(j);
                        System.out.println("Inside Delete method " + deleteDailyTo);
                        HashMap deletMap = new HashMap();
                        deletMap.put(TransactionDAOConstants.ACCT_NO, deleteDailyTo.getAcct_num());
                        deletMap.put(BATCH_ID, deleteDailyTo.getBatch_id());
                        deletMap.put("AGENT_NO", deleteDailyTo.getAgent_no());
                        double minuAmt = CommonUtil.convertObjToDouble(deleteDailyTo.getAmount()).doubleValue();
                        minuAmt = -1 * minuAmt;
                        deletMap.put(TransactionDAOConstants.AMT, new Double(minuAmt));
                        deletMap.put("TODAY_DT", deleteDailyTo.getTrn_dt());
                        deletMap.put("TRANS_DT", currDate.clone());
                        deletMap.put("INITIATED_BRANCH", _branchCode);
                        sqlMap.executeUpdate("deleteDAilyDepsot", deletMap);
                        sqlMap.executeUpdate("updateShadowCreditTD", deletMap);
                        deletMap = null;
                        deleteDailyTo = null;
                    }
                }
            }
            if (dailySize > 0) {
                String loanCollectionId = getLoanTransId();
                for (int i = 0; i < dailySize; i++) {
                    DailyDepositTransTO dailyTO = new DailyDepositTransTO();
                    dailyTO = (DailyDepositTransTO) dailyDepList.get(i);
                    HashMap updateMap = new HashMap();
                    updateMap.put(TransactionDAOConstants.ACCT_NO, dailyTO.getAcct_num());
                    updateMap.put(BATCH_ID, dailyTO.getBatch_id());
                    updateMap.put("AGENT_NO", dailyTO.getAgent_no());
                    updateMap.put(TransactionDAOConstants.AMT, dailyTO.getAmount());
                    Date tempDt = (Date) properFormatDate.clone();
                    if (dailyTO.getTrn_dt() != null) {
                        tempDt.setDate(dailyTO.getTrn_dt().getDate());
                        tempDt.setMonth(dailyTO.getTrn_dt().getMonth());
                        tempDt.setYear(dailyTO.getTrn_dt().getYear());
                        updateMap.put(TODAY_DT, tempDt);
                        dailyTO.setTrn_dt(tempDt);
                    }
                    Date createDt = (Date) properFormatDate.clone();
                    if (dailyTO.getCreated_dt() != null) {
                        createDt.setDate(dailyTO.getCreated_dt().getDate());
                        createDt.setMonth(dailyTO.getCreated_dt().getMonth());
                        createDt.setYear(dailyTO.getCreated_dt().getYear());
                        dailyTO.setCreated_dt(createDt);
                    }
                   if (map.containsKey(ACTION) && CommonUtil.convertObjToStr(map.get(ACTION)).equals("NEW") ) {
                        dailyTO.setBatch_id(cash_transid);
                        dailyTO.setTrans_mode(CommonConstants.TX_CASH);
                    } else {
                        dailyTO.setBatch_id(CommonUtil.convertObjToStr(map.get(BATCH_ID)));
                    }
                    if (map.containsKey("ACTION") && !CommonUtil.convertObjToStr(map.get("ACTION")).equals("DELETE") && !CommonUtil.convertObjToStr(map.get("ACTION")).equals("REJECTION")) {
                        dailyTO.setInitiatedBranch(_branchCode);
                        dailyTO.setScreenName("Transfer");
                        sqlMap.executeUpdate("INSERTINTODAILYDEPOSIT", dailyTO);
                        sqlMap.executeUpdate("updateShadowCreditTD", updateMap);
                        HashMap authMap = new HashMap();
                        authMap.put(BATCH_ID, dailyTO.getBatch_id());
                        authMap.put("ACC_NUM", dailyTO.getAcct_num());
                        authMap.put(CommonConstants.ACT_NUM, dailyTO.getAcct_num());
                        authMap.put(TransactionDAOConstants.ACCT_NO, dailyTO.getAcct_num());
                        authMap.put("AGENT_NO", dailyTO.getAgent_no());
                        authMap.put("AUTHORIZE_BY", CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                        authMap.put("UNCLEAR_AMT", dailyTO.getAmount());
                        authMap.put(TransactionDAOConstants.AMT, dailyTO.getAmount());
                        authMap.put("TODAY_DT", properFormatDate);
                        authMap.put("AUTHORIZE_DT", properFormatDate);
                        authMap.put(AUTHORIZE_STATUS, CommonConstants.STATUS_AUTHORIZED);
                        Count = Count + 1;
                        agentid = CommonUtil.convertObjToStr(dailyTO.getAgent_no());
                        sqlMap.executeUpdate("updateAvailBalanceTD", authMap);
                        sqlMap.executeUpdate("updateOtherBalancesTD", authMap);
                        double shdowMinus = dailyTO.getAmount().doubleValue();
                        shdowMinus = shdowMinus * -1;
                        authMap.put(TransactionDAOConstants.AMT, new Double(shdowMinus));
                        sqlMap.executeUpdate("updateShadowCreditTD", authMap);
                        sqlMap.executeQueryForList("getBalanceTD", authMap);
                        authMap.put(TransactionDAOConstants.AMT, new Double(shdowMinus));
                        List clearlst = sqlMap.executeQueryForList("getBalanceTD", authMap);
                        if (clearlst != null && clearlst.size() > 0) {
                            HashMap clearMap = new HashMap();
                            clearMap = (HashMap) clearlst.get(0);
                            authMap.put("CLEAR_BALANCE", CommonUtil.convertObjToDouble(clearMap.get("CLEAR_BALANCE")));
                            authMap.put("TRANS_DT", currDate.clone());
                            authMap.put("INITIATED_BRANCH", _branchCode);
                            sqlMap.executeUpdate("authorizeDailyDeposit", authMap);
                            clearMap = null;
                            clearlst = null;
                        }

                    }
                    updateMap = null;
                }
                HashMap cashAuthMap = new HashMap();
                cashAuthMap.put(CommonConstants.BRANCH_ID, _branchCode);
                cashAuthMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                cashAuthMap.put("DAILY", "DAILY");
                String status = CommonUtil.convertObjToStr(CommonConstants.STATUS_AUTHORIZED);
                transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
                transactionDAO.authorizeCashAndTransfer(agentid, status, cashAuthMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sqlMap.rollbackTransaction();
            throw e;

        }
    }
    public void InsertintoDailyDepositTabl(ArrayList dailyDepList, HashMap map) throws Exception {
        int dailySize = dailyDepList.size();
        int Count = 0;
        System.out.println("dailySize**********" + dailySize);
        System.out.println("dailyDepList**********" + dailyDepList);
        System.out.println("Map&&&&&&&&&===" + map);
        if (map.containsKey(ACTION) && !CommonUtil.convertObjToStr(map.get(ACTION)).equals("NEW")) {
            List lst = new ArrayList();
            map.put("TRANS_DT", currDate.clone());
            map.put("INITIATED_BRANCH", _branchCode);
            lst = sqlMap.executeQueryForList("getSelectCashTransactionTODAILY", map);
            if (lst != null && lst.size() > 0) {
                for (int j = 0; j < lst.size(); j++) {

                    DailyDepositTransTO deleteDailyTo = new DailyDepositTransTO();
                    deleteDailyTo = (DailyDepositTransTO) lst.get(j);
                    System.out.println("Inside Delete method " + deleteDailyTo);
                    HashMap deletMap = new HashMap();
                    deletMap.put(TransactionDAOConstants.ACCT_NO, deleteDailyTo.getAcct_num());
                    deletMap.put(BATCH_ID, deleteDailyTo.getBatch_id());
                    deletMap.put("AGENT_NO", deleteDailyTo.getAgent_no());

                    double minuAmt = CommonUtil.convertObjToDouble(deleteDailyTo.getAmount()).doubleValue();
                    minuAmt = -1 * minuAmt;
                    deletMap.put(TransactionDAOConstants.AMT, new Double(minuAmt));
                    deletMap.put("TODAY_DT", deleteDailyTo.getTrn_dt());
                    deletMap.put("TRANS_DT", currDate.clone());
                    deletMap.put("INITIATED_BRANCH", _branchCode);
                    sqlMap.executeUpdate("deleteDAilyDepsot", deletMap);
                    sqlMap.executeUpdate("updateShadowCreditTD", deletMap);
                    deletMap = null;
                    deleteDailyTo = null;
                    System.out.println("Completed the one record");
                }
                System.out.println("Completed the  record deletion");
            }
        }

        if (dailySize > 0) {
            String loanCollectionId = getLoanTransId();
            for (int i = 0; i < dailySize; i++) {
                DailyDepositTransTO dailyTO = new DailyDepositTransTO();
                dailyTO = (DailyDepositTransTO) dailyDepList.get(i);
                System.out.println("TT YUPE========" + dailyTO.getTrans_type());
                //Loan
                if (dailyTO.getTrans_type() != null && dailyTO.getTrans_type().equals("L")) {

                    HashMap updateMap = new HashMap();
                    //updateMap.put(TransactionDAOConstants.ACCT_NO,dailyTO.getAcct_num());
                    // updateMap.put(BATCH_ID,dailyTO.getBatch_id());
                    // updateMap.put("AGENT_NO",dailyTO.getAgent_no());
                    // updateMap.put(TransactionDAOConstants.AMT,dailyTO.getAmount());
             /*
                     * Date tempDt = (Date)properFormatDate.clone();
                     * if(dailyTO.getTrn_dt() !=null){
                     * tempDt.setDate(dailyTO.getTrn_dt().getDate());
                     * tempDt.setMonth(dailyTO.getTrn_dt().getMonth());
                     * tempDt.setYear(dailyTO.getTrn_dt().getYear());
                     * updateMap.put(TODAY_DT, tempDt); //
                     * dailyTO.setTrn_dt(tempDt); }
                     */

                    if (map.containsKey(ACTION) && CommonUtil.convertObjToStr(map.get(ACTION)).equals("NEW")) {
                        // dailyTO.setBatch_id(batchId);
                    }
                    if (map.containsKey("ACTION") && !CommonUtil.convertObjToStr(map.get("ACTION")).equals("DELETE") && !CommonUtil.convertObjToStr(map.get("ACTION")).equals("REJECTION")) {
                        String agentCustId = "";
                        HashMap dataMap = new HashMap();
                        HashMap whereMap = new HashMap();
                        HashMap map1 = new HashMap();
                        map1.put("AGENT_MACHINE_ID", dailyTO.getAgent_no());
                        List Ag = ServerUtil.executeQuery("getCustIdAgent", map1);
                        map1 = (HashMap) Ag.get(0);
                        if (map1.get("AGENT_ID") != null) {
                            agentCustId = map1.get("AGENT_ID").toString();
                        }
                        whereMap.put("AGENT_ID", agentCustId);
                        whereMap.put("BRANCH_CODE", _branchCode);
                        whereMap.put("ACT_NUM", dailyTO.getAcct_num());
                        List loanList = ServerUtil.executeQuery("getDailyLoanDetails1", whereMap);
                        System.out.println("#$@$@$#@$@#$@ List : " + loanList);
                        if (loanList != null && loanList.size() > 0) {
                            for (int j = 0; j < loanList.size(); j++) {
                                double princ_Due = 0.0;
                                double int_Due = 0.0;
                                double penal = 0.0;
                                double charge = 0.0;
                                double total_Due = 0.0;
                                dataMap = (HashMap) loanList.get(j);
                                updateMap.put("FNAME", CommonUtil.convertObjToStr(dataMap.get("FNAME")));
                                updateMap.put("LIMIT", CommonUtil.convertObjToStr(dataMap.get("LIMIT")));
                                updateMap.put("LAST_INT_CALC_DT", dataMap.get("LAST_INT_CALC_DT"));
                                updateMap.put("BALANCE", CommonUtil.convertObjToStr(dataMap.get("BALANCE")));
                                updateMap.put("PRINC_DUE", CommonUtil.convertObjToStr(dataMap.get("PRINC_DUE")));
                                updateMap.put("INT_DUE", CommonUtil.convertObjToStr(dataMap.get("INT_DUE")));
                                updateMap.put("PENAL", CommonUtil.convertObjToStr(dataMap.get("PENAL")));
                                updateMap.put("CHARGES", CommonUtil.convertObjToStr(dataMap.get("CHARGES")));
                                updateMap.put("PROD_ID", CommonUtil.convertObjToStr(dataMap.get("PROD_ID")));
                                princ_Due = CommonUtil.convertObjToDouble(dataMap.get("PRINC_DUE")).doubleValue();
                                charge = CommonUtil.convertObjToDouble(dataMap.get("CHARGES")).doubleValue();
                                HashMap hash = interestCalculationTLAD1(dataMap.get("ACT_NUM"), dataMap.get("PROD_ID"), "TL");
                                if (hash != null && hash.size() > 0) {
                                    int_Due = CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                                    penal = CommonUtil.convertObjToDouble(hash.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                                    hash.put("ACT_NUM", dataMap.get("ACT_NUM"));
                                    System.out.println("DDD 22222== " + hash.get("LAST_INT_CALC_DT"));
                                    hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                                    hash.put("FROM_DT", DateUtil.addDays(((Date) hash.get("FROM_DT")), 2));
                                    System.out.println("DDD 111=== " + currDate.clone());
                                    hash.put("TO_DATE", currDate.clone());
                                    List facilitylst = ServerUtil.executeQuery("getPaidPrinciple", hash);
                                    if (facilitylst != null && facilitylst.size() > 0) {
                                        hash = (HashMap) facilitylst.get(0);
                                        int_Due -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                                        penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                                    }
                                }
                                int_Due = int_Due <= 0 ? 0 : int_Due;
                                penal = penal <= 0 ? 0 : penal;
                                total_Due = princ_Due + int_Due + penal + charge;

                                updateMap.put("TOTAL_DUE", String.valueOf(total_Due));
                            }
                        }
                        updateMap.put("ACT_NUM", dailyTO.getAcct_num());

                        updateMap.put("PAYMENT", dailyTO.getAmount());
                        updateMap.put("ADJUSTED_LOAN", "N");
                        updateMap.put("AGENT_ID", agentCustId);
                        updateMap.put("LOAN_COLLECTION_NO", loanCollectionId);
                        updateMap.put("STATUS", CommonConstants.STATUS_CREATED);
                        System.out.println("DDD === " + currDate.clone());
                        updateMap.put("STATUS_DT", currDate);
                        updateMap.put("STATUS_BY", (String) map.get(CommonConstants.USER_ID));
                        sqlMap.executeUpdate("insertDailyLoanTansaction", updateMap);

                    }
                    updateMap = null;
                } else {
                    HashMap updateMap = new HashMap();
                    updateMap.put(TransactionDAOConstants.ACCT_NO, dailyTO.getAcct_num());
                    updateMap.put(BATCH_ID, dailyTO.getBatch_id());
                    updateMap.put("AGENT_NO", dailyTO.getAgent_no());
                    updateMap.put(TransactionDAOConstants.AMT, dailyTO.getAmount());
                    Date tempDt = (Date) properFormatDate.clone();
                    if (dailyTO.getTrn_dt() != null) {
                        tempDt.setDate(dailyTO.getTrn_dt().getDate());
                        tempDt.setMonth(dailyTO.getTrn_dt().getMonth());
                        tempDt.setYear(dailyTO.getTrn_dt().getYear());
                        updateMap.put(TODAY_DT, tempDt);
                        dailyTO.setTrn_dt(tempDt);
                    }
                    Date createDt = (Date) properFormatDate.clone();
                    if (dailyTO.getCreated_dt() != null) {
                        createDt.setDate(dailyTO.getCreated_dt().getDate());
                        createDt.setMonth(dailyTO.getCreated_dt().getMonth());
                        createDt.setYear(dailyTO.getCreated_dt().getYear());
                        dailyTO.setCreated_dt(createDt);
                    }
                    
                    dailyTO.setColl_dt(getProperDateFormat(dailyTO.getColl_dt())); // Added by nithya on 02-04-2019 for KD 449 - Cannot save Daily deposit
                    
                    System.out.println("updateMap$$$$$$$$$$$$$$$" + updateMap);
                     
                    if (map.containsKey(ACTION) && CommonUtil.convertObjToStr(map.get(ACTION)).equals("NEW")) {
                        dailyTO.setBatch_id(batchId);
                    }   else {
                        dailyTO.setBatch_id(CommonUtil.convertObjToStr(map.get(BATCH_ID)));
                    }
                    if (map.containsKey("ACTION") && !CommonUtil.convertObjToStr(map.get("ACTION")).equals("DELETE") && !CommonUtil.convertObjToStr(map.get("ACTION")).equals("REJECTION")) {
                        dailyTO.setInitiatedBranch(_branchCode);
                        if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("KOORKKENCHERRY")) {
                            dailyTO.setScreenName("Transfer");
                            sqlMap.executeUpdate("INSERTINTODAILYDEPOSIT", dailyTO);
                            Count = Count + 1;
                            if (dailyTO.getProd_Type() != null && dailyTO.getProd_Type().equals("TD") && dailyTO.getConsolidated().equalsIgnoreCase("Y")) {
                                sqlMap.executeUpdate("updateShadowCreditTD", updateMap);
                            }
                            System.out.println("Inside dailyTO$$$$$$$$$ttoooo$$$" + dailyTO);
                        }
                        else{
                            boolean fromDailyDepositScreen = false; // Rework - KD-2034 by nithya
                            if(dailyTO.getScreenName() != null && dailyTO.getScreenName().equals("Daily Deposit")){
                                fromDailyDepositScreen = true;                                
                            }
                            dailyTO.setScreenName("Transfer");
                            sqlMap.executeUpdate("INSERTINTODAILYDEPOSIT", dailyTO);
                            Count = Count + 1;
                             if (fromDailyDepositScreen || (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("POLPULLY") &&dailyTO.getProd_Type() != null && dailyTO.getProd_Type().equals("TD") && dailyTO.getConsolidated().equalsIgnoreCase("Y"))) {  //Added by nithya on 04-07-2020 for KD-2034                          
                            sqlMap.executeUpdate("updateShadowCreditTD", updateMap); 
                             }
                            System.out.println("Inside dailyTO$$$$$$$$$$$$" + dailyTO);
                        }
                    }
                    System.out.println("INSERTINTODAILYDEPOSIT Count" + Count);
                    updateMap = null;
                }
            }

        }
    }
    //AS AN WHEN CUSTOMER COMES

    private TxTransferTO setTransactiontoTLAD(TxTransferTO txtransTo) {
        TxTransferTO txTo = new TxTransferTO();
        txTo.setProdType(txtransTo.getProdType());
        txTo.setProdId(txtransTo.getProdId());
        txTo.setActNum(txtransTo.getActNum());
        txTo.setAcHdId(txtransTo.getAcHdId());
        txTo.setAmount(txtransTo.getAmount());
        txTo.setAuthorizeBy(txtransTo.getAuthorizeBy());
        txTo.setAuthorizeDt(txtransTo.getAuthorizeDt());
        txTo.setAuthorizeRemarks(txtransTo.getAuthorizeRemarks());
        txTo.setAuthorizeStatus(txtransTo.getAuthorizeStatus());
        txTo.setBatchId(txtransTo.getBatchId());
        txTo.setBranchId(txtransTo.getBranchId());
        txTo.setInitChannType(txtransTo.getInitChannType());
        txTo.setInitTransId(txtransTo.getInitTransId());
        txTo.setInitiatedBranch(txtransTo.getInitiatedBranch());
        txTo.setInpAmount(txtransTo.getInpAmount());
        txTo.setAuthorizeBy(txtransTo.getAuthorizeBy());
        txTo.setTransDt(txtransTo.getTransDt());
        txTo.setTransType(txtransTo.getTransType());
        txTo.setInstType(txtransTo.getInstType());
        txTo.setInstrumentNo1(txtransTo.getInstrumentNo1());
        txTo.setInstrumentNo2(txtransTo.getInstrumentNo2());
        txTo.setInstDt(txtransTo.getInstDt());
        txTo.setTransId(txtransTo.getTransId());
        txTo.setTransMode(txtransTo.getTransMode());
        txTo.setTransType(txtransTo.getTransType());
        txTo.setStatus(txtransTo.getStatus());
        txTo.setStatusBy(txtransTo.getStatusBy());
        txTo.setStatusDt(txtransTo.getStatusDt());
        txTo.setAuthorizeStatus_2(txtransTo.getAuthorizeStatus_2());
        if(txtransTo.getLinkBatchId() != null){
            txTo.setLinkBatchId(txtransTo.getLinkBatchId());
        }
        if (txtransTo.getTransAllId() != null) {
            txTo.setTransAllId(txtransTo.getTransAllId());
        }
        if (txtransTo.getRec_mode() != null) {
            txTo.setRec_mode(txtransTo.getRec_mode());
        }
        txTo.setSingleTransId(txtransTo.getSingleTransId());
        txTo.setTransModType(txtransTo.getTransModType());
        txTo.setScreenName(txtransTo.getScreenName());
        return txTo;

    }

    private HashMap asAnWhenCustomer(HashMap authorizeMap) throws Exception {
        System.out.println("authorizeMap#####" + authorizeMap);
        HashMap getDateMap = new HashMap();
        HashMap lastIntCalcDt = new HashMap();
        TaskHeader header = new TaskHeader();
        header.setBranchID(_branchCode);
        HashMap returnMap = new HashMap();
        getDateMap.put("ACCT_NUM", authorizeMap.get(CommonConstants.ACT_NUM));
        getDateMap.put(PROD_ID, authorizeMap.get(TransactionDAOConstants.PROD_ID));
        getDateMap.put("BRANCH_ID", _branchCode);
        //        HashMap depositcummap=(HashMap)((List)sqlMap.executeQueryForList("getDepositBehavesforLoan",getDateMap));
        //        if(depositcummap !=null && depositcummap.size()>0 && depositcummap.containsKey(BEHAVES_LIKE) && (!depositcummap.get(BEHAVES_LIKE).equals("CUMMULATIVE"))){

        InterestCalculationTask interestCalTask = new InterestCalculationTask(header);
        authorizeMap.put("WHERE", authorizeMap.get(CommonConstants.ACT_NUM));
        String mapNameForLastIntCalcDt = "getLastIntCalDate";
        if (CommonUtil.convertObjToStr(authorizeMap.get(BEHAVES_LIKE)).equals("OD")) {
            mapNameForLastIntCalcDt = "getLastIntCalDateAD";
        }
        List lst = (List) sqlMap.executeQueryForList(mapNameForLastIntCalcDt, authorizeMap);
//        List lst=(List)sqlMap.executeQueryForList("getLastIntCalDate",authorizeMap);
        getDateMap = (HashMap) lst.get(0);
        insertInterestDetails(getDateMap);
//        if(getDateMap !=null && getDateMap.containsKey("INSTALL_TYPE") &&  getDateMap.get("INSTALL_TYPE").equals("EMI")) {
//
//            returnMap.put("UPTO_DT","N");
//            return returnMap;
//        }
        lastIntCalcDt.put("LASTINTCALCDT", authorizeMap.get("LAST_INT_CALC_DT"));
        lastIntCalcDt.put("ACCOUNTNO", authorizeMap.get("ACT_NUM"));
        System.out.println("value of lastIntCalcDt in Transfer: " + lastIntCalcDt);
        // Commented by nithya on 08-09-2021 for KD-3021
//        if(!authorizeMap.containsKey("LOAN_CLOSING_SCREEN")){
//            sqlMap.executeUpdate("updateLoansFacilityDetailsTemp", lastIntCalcDt);
//        }
        authorizeMap.put(PROD_ID, authorizeMap.get(PROD_ID));
        Date CURR_DATE = new Date();
        CURR_DATE = (Date) properFormatDate.clone();
        System.out.println("curr_date###1" + CURR_DATE);
        getDateMap.put("CURR_DATE", CURR_DATE);
        getDateMap.put("BRANCH_ID", _branchCode);
        getDateMap.put(CommonConstants.BRANCH_ID, _branchCode);
        //            HashMap behaveLike=(HashMap)(sqlMap.executeQueryForList("getLoanBehaves",authorizeMap).get(0));
        getDateMap.put(CommonConstants.ACT_NUM, authorizeMap.get(CommonConstants.ACT_NUM));
        getDateMap.put(BEHAVES_LIKE, authorizeMap.get(BEHAVES_LIKE));
        getDateMap.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
        System.out.println("before od interest calculation####" + getDateMap);
        HashMap resultMap = new HashMap();
        resultMap = interestCalTask.interestCalcTermLoanAD(getDateMap); // we need same used for TL also
        double penalInt = 0;
        double interest = 0;
        if (resultMap != null && resultMap.containsKey("LOAN_CLOSING_PENAL_INT")) {
            penalInt = CommonUtil.convertObjToDouble(resultMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
        }
        if (resultMap != null && resultMap.containsKey(INTEREST)) {
            interest = CommonUtil.convertObjToDouble(resultMap.get(INTEREST)).doubleValue();
        }
        //                lst=sqlMap.executeQueryForList("getSumProductOD",getDateMap);
        //                getDateMap=(HashMap)lst.get(0);
        //        System.out.println("OD  #####!"+getDateMap);
        //                    returnMap.put("AccountInterest",getDateMap.get(INTEREST));
        //        returnMap.put("AccountInterest",new Double(interest));
        //        returnMap.put("AccountPenalInterest",new Double(penalInt));
        //calculated interest
        System.out.println("transfre DAO resultMap : "+resultMap);
        double totCalInterest = penalInt + interest;
        System.out.println("totCalInterest : "+totCalInterest+"penalInt : "+penalInt+"interest : "+interest);
        //Added by nithya on 28-08-2017
        if (CommonConstants.SAL_REC_MODULE != null && !CommonConstants.SAL_REC_MODULE.equals("")
                && CommonConstants.SAL_REC_MODULE.equalsIgnoreCase("Y")) {
            if (futureSILastIntCalcDt != null) {
                HashMap updateMap = new HashMap();
                updateMap.put("ACCOUNTNO", authorizeMap.get(CommonConstants.ACT_NUM));
                updateMap.put("LAST_CALC_DT", futureSILastIntCalcDt);
                sqlMap.executeUpdate("updateclearBal", updateMap);
                returnMap.put("UPTO_DT", "Y");
                updateMap = null;
                futureSILastIntCalcDt = null;
            }
        }
        // End
        if (totCalInterest > 0) {
             boolean updtFlag = true;
            // Added by nithya on 08-09-2021 for KD-3021
            if (!authorizeMap.containsKey("LOAN_CLOSING_SCREEN")) {
                sqlMap.executeUpdate("updateLoansFacilityDetailsTemp", lastIntCalcDt);
            }
            HashMap accDetailMap = new HashMap();
            accDetailMap.put(CommonConstants.ACT_NUM, getDateMap.get(CommonConstants.ACT_NUM));
            accDetailMap.put("FROM_DT", getDateMap.get("LAST_INT_CALC_DT"));
            accDetailMap.put("FROM_DT", DateUtil.addDays(((Date) accDetailMap.get("FROM_DT")), 2));
            accDetailMap.put("TO_DATE", getDateMap.get("CURR_DATE"));
            //paid interest
            if (resultMap.containsKey("BEHAVES_LIKE") && CommonUtil.convertObjToStr(resultMap.get("BEHAVES_LIKE")).equals("OD")) {
                lst = sqlMap.executeQueryForList("getPaidPrincipleAD", accDetailMap);
            } else {
                lst = sqlMap.executeQueryForList("getPaidPrinciple", accDetailMap);
            }
            accDetailMap = (HashMap) lst.get(0);
            double paidInt = CommonUtil.convertObjToDouble(accDetailMap.get(INTEREST)).doubleValue();
            double paidPenalInt = CommonUtil.convertObjToDouble(accDetailMap.get("PENAL")).doubleValue();
            if (resultMap.containsKey("FUTURE_LAST_INT_CALC_DT")) {
                accDetailMap.put("LAST_CALC_DT", resultMap.get("FUTURE_LAST_INT_CALC_DT"));
            } else {
                accDetailMap.put("LAST_CALC_DT", DateUtil.addDaysProperFormat(CURR_DATE, -1));
               
                if (CommonConstants.SAL_REC_MODULE != null && !CommonConstants.SAL_REC_MODULE.equals("")
                        && CommonConstants.SAL_REC_MODULE.equalsIgnoreCase("Y")) {
                    HashMap loanClosingMap = new HashMap();
                    loanClosingMap.put("ACCOUNTNO", getDateMap.get(CommonConstants.ACT_NUM));
                    lst = sqlMap.executeQueryForList("getBalanceLoanPrincipalAmt", loanClosingMap);
                    if (lst != null && lst.size() > 0) {
                        loanClosingMap = (HashMap) lst.get(0);
                        double loanBalancePrincipal = CommonUtil.convertObjToDouble(loanClosingMap.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();
                        double principalAmount = CommonUtil.convertObjToDouble(authorizeMap.get("TRANSACTION_PAID_AMT")).doubleValue();                        
                        System.out.println("loanBalancePrincipal : " + loanBalancePrincipal + " principalAmount : " + principalAmount);
                        if (principalAmount == loanBalancePrincipal) {
                            loanClosingMap.put("STATUS_DT", currDate.clone());
                            loanClosingMap.put("STATUS_BY", authorizeMap.get("USER_ID"));
                            loanClosingMap.put("ACCT_STATUS", CommonConstants.CLOSED);
                            loanClosingMap.put("ACCT_CLOSE_DT", currDate.clone());
                            loanClosingMap.put("ACCT_NUM", getDateMap.get(CommonConstants.ACT_NUM));
                            sqlMap.executeUpdate("updateLoanActClosingDetailSalRecovery", loanClosingMap);
                        } else {
                            String prodId = CommonUtil.convertObjToStr(getDateMap.get(PROD_ID));
                            accDetailMap.put("ACCOUNTNO", getDateMap.get(CommonConstants.ACT_NUM));
                            if (authorizeMap.containsKey("VOUCHER_RELEASE")) {
                                Date intCalcUptoDt = CommonUtil.getProperDate(currDate, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(authorizeMap.get("VOUCHER_RELEASE"))));
                                double recPrincipal = CommonUtil.convertObjToDouble(authorizeMap.get("REC_PRINCIPAL"));
                                if (recPrincipal > 0) {
                                    accDetailMap.put("LAST_CALC_DT", intCalcUptoDt);
                                    sqlMap.executeUpdate("updateclearBal", accDetailMap);
                                    returnMap.put("UPTO_DT", "Y");
                                    return returnMap;
                                }
                            } else {
                                if (totCalInterest <= (paidInt + paidPenalInt)) {
                                   // System.out.print("totCalInterest : " + totCalInterest + "paidInt : " + paidInt + "paidPenalInt : " + paidPenalInt);
                                    if (lastIntCalcDateUpdateOrNot(prodId) == false) {
                                        if (updtFlag) {
                                            double recPrincipal = CommonUtil.convertObjToDouble(authorizeMap.get("TRANSACTION_PAID_AMT"));
                                            if (authorizeMap.containsKey("DEDUCTION_SI")) {
                                                recPrincipal = CommonUtil.convertObjToDouble(authorizeMap.get("TRANSACTION_PAID_AMT"));
                                                System.out.println("principalAmount >>>"+principalAmount +" recPrincipal :"+recPrincipal);
                                                if (recPrincipal > 0) {
                                                    sqlMap.executeUpdate("updateclearBal", accDetailMap);
                                                }
                                            } else {
                                                System.out.println("I m hereeeeeee");
                                                sqlMap.executeUpdate("updateclearBal", accDetailMap);
                                            }

                                        }
                                    }
                                    returnMap.put("UPTO_DT", "Y");
                                  //  System.out.print(returnMap + "first#######1");
                                    return returnMap;
                                }
                            }
                            System.out.println("Not Closed : " + getDateMap.get(CommonConstants.ACT_NUM));
                        }
                    }
//                    HashMap whrMap = new HashMap();
//                    whrMap.put("ACT_NUM", getDateMap.get(CommonConstants.ACT_NUM));
//                    List templst = sqlMap.executeQueryForList("getLastIntCalcDateForSalaryRecovery", whrMap);
//                    if (templst != null && templst.size() > 0) {
//                        HashMap datmap = (HashMap) templst.get(0);
//                        if (datmap != null && datmap.containsKey("INT_CALC_UPTO_DT")) {
//                            String lstIntcalcDat = CommonUtil.convertObjToStr(datmap.get("INT_CALC_UPTO_DT"));
//                            Date dtVal = CommonUtil.getProperDate(currDate, DateUtil.getDateMMDDYYYY(lstIntcalcDat));
//                            whrMap.put("INT_CALC_UPTO_DT", dtVal);
//                            List templstVal = sqlMap.executeQueryForList("getRecPrincipalForSalaryRecovery", whrMap);
//                            if (templstVal != null && templstVal.size() > 0) {
//                                HashMap sing = (HashMap) templstVal.get(0);
//                                if (sing != null && sing.containsKey("REC_PRINCIPAL")) {
//                                    double recAmt = CommonUtil.convertObjToDouble(sing.get("REC_PRINCIPAL"));
//                                    if(recAmt>0){
//                                        accDetailMap.put("LAST_CALC_DT", DateUtil.addDaysProperFormat(dtVal, -1)); 
//                                        updtFlag = true;
//                                    } else if(recAmt==0){
//                                        updtFlag = false;
//                                    }
//                                }
//                            }
//                        }
//                    }else{
//                        String prodId = CommonUtil.convertObjToStr(getDateMap.get(PROD_ID));
//                        if (totCalInterest <= (paidInt + paidPenalInt)) {
//                            if (lastIntCalcDateUpdateOrNot(prodId) == false) {
//                                if (updtFlag) {
//                                    sqlMap.executeUpdate("updateclearBal", accDetailMap);
//                                }
//                            }
//                            returnMap.put("UPTO_DT", "Y");
//                            System.out.print(returnMap + "first#######");
//                            return returnMap;
//                        }
//                    }

                }
            }
            accDetailMap.put(TransactionDAOConstants.ACCT_NO, getDateMap.get(CommonConstants.ACT_NUM));
            System.out.println("accDetailMap  ###" + accDetailMap);
            double actualInterest = interest - paidInt;
            double actualPenalInt = penalInt - paidPenalInt;
            returnMap.put("CURR_MONTH_INT", new Double(actualInterest));
            returnMap.put("PENAL_INT", new Double(actualPenalInt));//LOAN_CLOSING_PENAL_INT
            HashMap chargeDetails = getAsAnWhenChargesDetails(getDateMap);
            returnMap.putAll(chargeDetails);
            System.out.print(returnMap + "#######");
            String prodId = CommonUtil.convertObjToStr(getDateMap.get(PROD_ID));
            if (authorizeMap.containsKey("VOUCHER_RELEASE")) {
                Date intCalcUptoDt = CommonUtil.getProperDate(currDate, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(authorizeMap.get("VOUCHER_RELEASE"))));
                double recPrincipal = CommonUtil.convertObjToDouble(authorizeMap.get("REC_PRINCIPAL"));
                if (recPrincipal > 0) {
                    accDetailMap.put("LAST_CALC_DT", intCalcUptoDt);
                    sqlMap.executeUpdate("updateclearBal", accDetailMap);
                    returnMap.put("UPTO_DT", "Y");
                    return returnMap;
                }
            } else {
                if (totCalInterest <= (paidInt + paidPenalInt)) {
                    if (lastIntCalcDateUpdateOrNot(prodId) == false) {
                        if (updtFlag) {
                            sqlMap.executeUpdate("updateclearBal", accDetailMap);
                        }
                    }
                    returnMap.put("UPTO_DT", "Y");
                    System.out.print(returnMap + "first#######");
                    return returnMap;
                }
            }
        } else {
            HashMap chargeDetails = getAsAnWhenChargesDetails(getDateMap);
            returnMap.putAll(chargeDetails);
        }
        returnMap.put("UPTO_DT", "N");
        System.out.print(returnMap + "se#######");
        return returnMap;
    }

    private HashMap asAnWhenCustomerTransCreation(HashMap authorizeMap) throws Exception {
        System.out.println("authorizeMap#####" + authorizeMap);
        HashMap getDateMap = new HashMap();
        TaskHeader header = new TaskHeader();
        header.setBranchID(_branchCode);
        HashMap returnMap = new HashMap();
        getDateMap.put("ACCT_NUM", authorizeMap.get(CommonConstants.ACT_NUM));
        getDateMap.put(PROD_ID, authorizeMap.get(TransactionDAOConstants.PROD_ID));
        getDateMap.put("BRANCH_ID", _branchCode);
        //        HashMap depositcummap=(HashMap)((List)sqlMap.executeQueryForList("getDepositBehavesforLoan",getDateMap));
        //        if(depositcummap !=null && depositcummap.size()>0 && depositcummap.containsKey(BEHAVES_LIKE) && (!depositcummap.get(BEHAVES_LIKE).equals("CUMMULATIVE"))){

        InterestCalculationTask interestCalTask = new InterestCalculationTask(header);
        authorizeMap.put("WHERE", authorizeMap.get(CommonConstants.ACT_NUM));
        List lst = (List) sqlMap.executeQueryForList("getLastIntCalDate", authorizeMap);
        if (lst != null && lst.size() > 0) {
            getDateMap = (HashMap) lst.get(0);
        }
//        if(getDateMap !=null && getDateMap.containsKey("INSTALL_TYPE") &&  getDateMap.get("INSTALL_TYPE").equals("EMI")) {
////            return "N";
//            returnMap.put("UPTO_DT","N");
//            return returnMap;
//        }
        authorizeMap.put(PROD_ID, authorizeMap.get(PROD_ID));
        Date CURR_DATE = new Date();
        CURR_DATE = (Date) properFormatDate.clone();
        System.out.println("curr_date###1" + CURR_DATE);
        getDateMap.put("CURR_DATE", CURR_DATE);
        getDateMap.put("BRANCH_ID", _branchCode);
        getDateMap.put(CommonConstants.BRANCH_ID, _branchCode);
        //            HashMap behaveLike=(HashMap)(sqlMap.executeQueryForList("getLoanBehaves",authorizeMap).get(0));
        getDateMap.put(CommonConstants.ACT_NUM, authorizeMap.get(CommonConstants.ACT_NUM));
        getDateMap.put(BEHAVES_LIKE, authorizeMap.get(BEHAVES_LIKE));
        getDateMap.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
        System.out.println("before od interest calculation####" + getDateMap);
        HashMap resultMap = new HashMap();
        getDateMap.put("NO_OF_INSTALLMENT", authorizeMap.get("NO_OF_INSTALLMENT"));
        resultMap = interestCalTask.interestCalcTermLoanAD(getDateMap); // we need same used for TL also
        double penalInt = 0;
        double interest = 0;
        if (resultMap != null && resultMap.containsKey("LOAN_CLOSING_PENAL_INT")) {
            penalInt = CommonUtil.convertObjToDouble(resultMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
        }
        if (resultMap != null && resultMap.containsKey(INTEREST)) {
            interest = CommonUtil.convertObjToDouble(resultMap.get(INTEREST)).doubleValue();
        }
        //                lst=sqlMap.executeQueryForList("getSumProductOD",getDateMap);
        //                getDateMap=(HashMap)lst.get(0);
        //        System.out.println("OD  #####!"+getDateMap);
        //                    returnMap.put("AccountInterest",getDateMap.get(INTEREST));
        //        returnMap.put("AccountInterest",new Double(interest));
        //        returnMap.put("AccountPenalInterest",new Double(penalInt));
        //calculated interest
        double totCalInterest = penalInt + interest;
        if (totCalInterest > 0) {
            HashMap accDetailMap = new HashMap();
            accDetailMap.put(CommonConstants.ACT_NUM, getDateMap.get(CommonConstants.ACT_NUM));
            accDetailMap.put("FROM_DT", getDateMap.get("LAST_INT_CALC_DT"));
            accDetailMap.put("FROM_DT", DateUtil.addDays(((Date) accDetailMap.get("FROM_DT")), 2));
            accDetailMap.put("TO_DATE", getDateMap.get("CURR_DATE"));
            //paid interest
            lst = sqlMap.executeQueryForList("getPaidPrinciple", accDetailMap);
            accDetailMap = (HashMap) lst.get(0);
            double paidInt = CommonUtil.convertObjToDouble(accDetailMap.get(INTEREST)).doubleValue();
            double paidPenalInt = CommonUtil.convertObjToDouble(accDetailMap.get("PENAL")).doubleValue();
            accDetailMap.put("LAST_CALC_DT", DateUtil.addDaysProperFormat(CURR_DATE, -1));
            accDetailMap.put(TransactionDAOConstants.ACCT_NO, getDateMap.get(CommonConstants.ACT_NUM));
            System.out.println("accDetailMap  ###" + accDetailMap);
            double actualInterest = interest - paidInt;
            double actualPenalInt = penalInt - paidPenalInt;
            returnMap.put("CURR_MONTH_INT", new Double(actualInterest));
            returnMap.put("PENAL_INT", new Double(actualPenalInt));//LOAN_CLOSING_PENAL_INT
            HashMap chargeDetails = getAsAnWhenChargesDetails(getDateMap);
            returnMap.putAll(chargeDetails);
            System.out.print(returnMap + "#######");
            String prodId = CommonUtil.convertObjToStr(getDateMap.get(PROD_ID));
            if (authorizeMap.containsKey("VOUCHER_RELEASE")) {
                Date intCalcUptoDt = CommonUtil.getProperDate(currDate, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(authorizeMap.get("VOUCHER_RELEASE"))));
                double recPrincipal = CommonUtil.convertObjToDouble(authorizeMap.get("REC_PRINCIPAL"));
                if (recPrincipal > 0) {
                    accDetailMap.put("LAST_CALC_DT", intCalcUptoDt);
                    sqlMap.executeUpdate("updateclearBal", accDetailMap);
                    returnMap.put("UPTO_DT", "Y");
                    return returnMap;
                }
            }else{ 
            if (totCalInterest <= (paidInt + paidPenalInt)) {
                if (lastIntCalcDateUpdateOrNot(prodId) == false) {
                    sqlMap.executeUpdate("updateclearBal", accDetailMap);
                }
//            return "Y";
                returnMap.put("UPTO_DT", "Y");
                System.out.print(returnMap + "first#######");
                return returnMap;
            }
        }
            //        }
        } else {
            HashMap chargeDetails = getAsAnWhenChargesDetails(getDateMap);
            returnMap.putAll(chargeDetails);
        }
//        return "N";
        returnMap.put("UPTO_DT", "N");
        System.out.print(returnMap + "se#######");
        return returnMap;
    }

    private boolean lastIntCalcDateUpdateOrNot(String prodId) throws Exception {
        boolean checkIntcalcDate = false;
        HashMap wheremap = new HashMap();
        wheremap.put("value", prodId);
        List list = sqlMap.executeQueryForList("getSelectLoanProductAccountParamTO", wheremap);
        if (list != null && list.size() > 0) {
            LoanProductAccountParamTO loanProductAccountParamTO = (LoanProductAccountParamTO) list.get(0);
            if (loanProductAccountParamTO.getIsCreditAllowedToPricipal() != null && loanProductAccountParamTO.getIsCreditAllowedToPricipal().equals("Y")) {
                List appList = sqlMap.executeQueryForList("selectAppropriatTransaction", wheremap);
                HashMap appropriateMap = new HashMap();
                if (appList != null && appList.size() > 0) {
                    appropriateMap = (HashMap) appList.get(0);
                    appropriateMap.remove("PROD_ID");
                }
                if (appropriateMap.containsKey("1_PRIORITY") && appropriateMap.get("1_PRIORITY") != null && appropriateMap.get("1_PRIORITY").equals("PRINCIPAL")) {
                    checkIntcalcDate = true;
                } else {
                    checkIntcalcDate = false;
                }
                System.out.println("appropriateMap#### in Transfer DAO File" + appropriateMap);
                System.out.println("checkIntcalcDate" + checkIntcalcDate);
            }
        }
        return checkIntcalcDate;
    }
    public void authorizeDailyDepositOnlyKorr(ArrayList upList, HashMap Map) throws Exception {
        int listsize = upList.size();
        System.out.println("Out side  authorizeDailyDeposit" + Map);
        HashMap chechMap = new HashMap();
        chechMap = (HashMap) Map.get(CommonConstants.AUTHORIZEMAP);
        if (chechMap.containsKey(CommonConstants.AUTHORIZESTATUS)) {
            System.out.println("Out side  authorizeDailyDeposit");
            System.out.println("Out side  listsize&&&&&" + listsize);
            if (listsize > 0) {
                System.out.println("inside   listsize&&&&&" + listsize);
                for (int i = 0; i < listsize; i++) {
                    DailyDepositTransTO dailyTO = new DailyDepositTransTO();
                    dailyTO = (DailyDepositTransTO) upList.get(i);
                    System.out.println("insede dailyTO $$$$$$$$$" + dailyTO);
                    HashMap prodMap=new HashMap();
                    prodMap.put("ACT_NUM",dailyTO.getAcct_num());
                    prodMap.put("BATCH_ID",dailyTO.getBatch_id());
                    prodMap.put("TRANS_DT",currDate.clone());
                    List prodList=sqlMap.executeQueryForList("getProdTypeDaily",prodMap );
                    String prodTypeDaily="";
                    if(prodList!=null && prodList.size()>0){
                        prodMap=(HashMap) prodList.get(0);
                        prodTypeDaily=CommonUtil.convertObjToStr(prodMap.get("PROD_TYPE"));
                    }
                    if(i==0){
                        String transId="";
                        HashMap dataMap=new HashMap();
                        dataMap.put("GL_TRANS_ACT_NUM",  dailyTO.getAgent_no());
                        dataMap.put("TODAY_DT",properFormatDate);
                        dataMap.put("AUTHORIZED_STATUS","AUTHORIZED_STATUS");
                        List clearlst = sqlMap.executeQueryForList("getTransIdForDailyCash", dataMap);
                        if (clearlst != null && clearlst.size() > 0) {
                           dataMap =(HashMap) clearlst.get(0);
                           transId=CommonUtil.convertObjToStr(dataMap.get("TRANS_ID"));
                        }
                    /*     HashMap whereMap = new HashMap();
                       // HashMap cashMap  = (HashMap)cashList.get(0);
                        whereMap.put("STATUS","AUTHORIZED");
                        whereMap.put("INITIATED_BRANCH",_branchCode);
                        whereMap.put("SHIFT","");
                        whereMap.put("TRANS_DT",properFormatDate);
                        whereMap.put("TRANS_ID", CommonUtil.convertObjToStr(transId));
                        whereMap.put("USER_ID",dailyTO.getCreated_by());
                        whereMap.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(dailyTO.getAgent_no()));
                        sqlMap.executeUpdate("authorizeCashTransaction", whereMap);*/
                        if(transId!=null && !transId.equals("")){
                            HashMap data=new HashMap();
                            HashMap authorizeMap = new HashMap();
                            ArrayList arrList = new ArrayList();
                            HashMap singleAuthorizeMap = new HashMap();
                            singleAuthorizeMap.put("STATUS", CommonUtil.convertObjToStr(chechMap.get(CommonConstants.AUTHORIZESTATUS)));
                            singleAuthorizeMap.put("TRANS_ID", CommonUtil.convertObjToStr(transId));
                            singleAuthorizeMap.put("AUTHORIZED_BY",CommonUtil.convertObjToStr(Map.get("USER_ID")));
                            singleAuthorizeMap.put("AUTHORIZED_DT", properFormatDate);
                            singleAuthorizeMap.put(CommonConstants.USER_ID,CommonUtil.convertObjToStr(Map.get("USER_ID")));
                            arrList.add(singleAuthorizeMap);
                            authorizeMap.put("TRANS_ID", CommonUtil.convertObjToStr(transId));
                           // authorizeMap.put("SINGLE_TRANS_ID", observable.getSingleTransId());
                            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonUtil.convertObjToStr(chechMap.get(CommonConstants.AUTHORIZESTATUS)));
                            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                            authorizeMap.put(CommonConstants.USER_ID,CommonUtil.convertObjToStr(Map.get("USER_ID")));
                            authorizeMap.put("DAILY","DAILY");
                            data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
                            data.put(CommonConstants.BRANCH_ID,_branchCode);
                            data.put(CommonConstants.USER_ID,CommonUtil.convertObjToStr(Map.get("USER_ID")));
                            CashTransactionDAO daoAuthorize=new CashTransactionDAO();
                            daoAuthorize.execute(data,false);
                        }                        
                    }
                    HashMap authMap = new HashMap();
                    authMap.put(BATCH_ID, dailyTO.getBatch_id());
                    authMap.put("ACC_NUM", dailyTO.getAcct_num());
                    authMap.put(CommonConstants.ACT_NUM, dailyTO.getAcct_num());
                    authMap.put(TransactionDAOConstants.ACCT_NO, dailyTO.getAcct_num());
                    authMap.put("AGENT_NO", dailyTO.getAgent_no());
                    authMap.put("AUTHORIZE_BY", Map.get(CommonConstants.USER_ID));
                    authMap.put("UNCLEAR_AMT", dailyTO.getAmount());
                    authMap.put(TransactionDAOConstants.AMT, dailyTO.getAmount());
                    authMap.put("TODAY_DT", properFormatDate);
                    authMap.put("AUTHORIZE_DT", properFormatDate);
                    authMap.put(AUTHORIZE_STATUS, chechMap.get("AUTHORIZESTATUS"));
                    System.out.println("insede authMap $$$$$$$$$" + authMap);
                    if (chechMap.get(CommonConstants.AUTHORIZESTATUS).equals(CommonConstants.STATUS_AUTHORIZED) || chechMap.get(CommonConstants.AUTHORIZESTATUS).equals(CommonConstants.STATUS_EXCEPTION)) {
                        
                      //  List clearlst = sqlMap.executeQueryForList("getBalanceTD", authMap);
                        System.out.println("prodTypeDaily---------------->" + prodTypeDaily);
                        if (prodTypeDaily != null && prodTypeDaily.equals("TD")) {
                            if (dailyTO.getConsolidated().equalsIgnoreCase("Y")) {
                                sqlMap.executeUpdate("updateAvailBalanceTD", authMap);
                                sqlMap.executeUpdate("updateOtherBalancesTD", authMap);
                            }
                             double shdowMinus = dailyTO.getAmount().doubleValue();
                            shdowMinus = shdowMinus * -1;
                            authMap.put(TransactionDAOConstants.AMT, new Double(shdowMinus));
                            if (dailyTO.getConsolidated().equalsIgnoreCase("Y")) {
                                sqlMap.executeUpdate("updateShadowCreditTD", authMap);
                            }
                            HashMap clearMap = new HashMap();
                            List clearlst = sqlMap.executeQueryForList("getBalanceTD", authMap);
                            clearMap = (HashMap) clearlst.get(0);
                            authMap.put("CLEAR_BALANCE", CommonUtil.convertObjToDouble(clearMap.get("CLEAR_BALANCE")));
                            authMap.put("TRANS_DT", currDate.clone());
                            authMap.put("INITIATED_BRANCH", _branchCode);
                            sqlMap.executeUpdate("authorizeDailyDeposit", authMap);
                            clearMap = null;
                          //  clearlst = null;
                        }
                       if (prodTypeDaily!=null && prodTypeDaily.equals("SA")){
                             HashMap clearMap = new HashMap();
                             authMap.put(TransactionDAOConstants.UNCLEAR_AMT, new Double(0));
                             authMap.put(TransactionDAOConstants.AMT, dailyTO.getAmount());
                             authMap.put("TRANS_DT", currDate.clone());
                             authMap.put("INITIATED_BRANCH", _branchCode);
                             sqlMap.executeUpdate("authorizeDailyDeposit", authMap);
                         //    sqlMap.executeUpdate("updateOtherBalancesSA", authMap);
                             clearMap = null;
                            //clearlst = null;
                        }
                       //For OA
                        if (prodTypeDaily!=null && prodTypeDaily.equals("OA")){
                            List clearlst = sqlMap.executeQueryForList("getBalanceOA", authMap);
                            authMap.put("ACCOUNTNO", dailyTO.getAcct_num());
                            //sqlMap.executeUpdate("updateAvailBalanceOA", authMap);
                            //sqlMap.executeUpdate("updateOtherBalancesOA", authMap);
                            double shdowMinus = dailyTO.getAmount().doubleValue();
                            shdowMinus = shdowMinus * -1;
                            authMap.put(TransactionDAOConstants.AMT, new Double(shdowMinus));
                            sqlMap.executeQueryForList("getBalanceOA", authMap);
                            authMap.put(TransactionDAOConstants.AMT, new Double(shdowMinus));
                            //
                            HashMap clearMap = new HashMap();
                            clearMap = (HashMap) clearlst.get(0);
                            authMap.put("CLEAR_BALANCE", CommonUtil.convertObjToDouble(clearMap.get("CLEAR_BALANCE")));
                            authMap.put("TRANS_DT", currDate.clone());
                            authMap.put("INITIATED_BRANCH", _branchCode);
                            sqlMap.executeUpdate("authorizeDailyDeposit", authMap);
                            clearMap = null;
                            clearlst = null;
                        }
                        //
                    } else {
                        Map.put(ACTION, "REJECTION");
                        double shdowMinus = dailyTO.getAmount().doubleValue();
                        shdowMinus = shdowMinus * -1;
                        authMap.put(TransactionDAOConstants.AMT, new Double(shdowMinus));
                        authMap.put("CLEAR_BALANCE", new Double(0));
                        if (prodTypeDaily!=null && prodTypeDaily.equals("TD")&&dailyTO.getConsolidated().equalsIgnoreCase("Y")) {
                        sqlMap.executeUpdate("updateShadowCreditTD", authMap);
                        }
                        authMap.put("TRANS_DT", currDate.clone());
                        authMap.put("INITIATED_BRANCH", _branchCode);
                        sqlMap.executeUpdate("authorizeDailyDeposit", authMap);
                    }
                    authMap = null;
                    dailyTO = null;
                }
            }
        }
    }
    
        public void authorizeDailyDepositOnly(ArrayList upList, HashMap Map) throws Exception {
        int listsize = upList.size();
        System.out.println("Out side  authorizeDailyDeposit" + Map);
        HashMap chechMap = new HashMap();
        chechMap = (HashMap) Map.get(CommonConstants.AUTHORIZEMAP);
        if (chechMap.containsKey(CommonConstants.AUTHORIZESTATUS)) {
            System.out.println("Out side  authorizeDailyDeposit");
            System.out.println("Out side  listsize&&&&&" + listsize);
            if (listsize > 0) {
                System.out.println("inside   listsize&&&&&" + listsize);
                for (int i = 0; i < listsize; i++) {
                    DailyDepositTransTO dailyTO = new DailyDepositTransTO();
                    dailyTO = (DailyDepositTransTO) upList.get(i);
                    System.out.println("insede dailyTO $$$$$$$$$" + dailyTO);
                    HashMap authMap = new HashMap();
                    authMap.put(BATCH_ID, dailyTO.getBatch_id());
                    authMap.put("ACC_NUM", dailyTO.getAcct_num());
                    authMap.put(CommonConstants.ACT_NUM, dailyTO.getAcct_num());
                    authMap.put(TransactionDAOConstants.ACCT_NO, dailyTO.getAcct_num());
                    authMap.put("AGENT_NO", dailyTO.getAgent_no());
                    authMap.put("AUTHORIZE_BY", Map.get(CommonConstants.USER_ID));
                    authMap.put("UNCLEAR_AMT", dailyTO.getAmount());
                    authMap.put(TransactionDAOConstants.AMT, dailyTO.getAmount());
                    authMap.put("TODAY_DT", properFormatDate);
                    authMap.put("AUTHORIZE_DT", properFormatDate);
                    authMap.put(AUTHORIZE_STATUS, chechMap.get("AUTHORIZESTATUS"));
                    System.out.println("insede authMap $$$$$$$$$" + authMap);
                    if (chechMap.get(CommonConstants.AUTHORIZESTATUS).equals(CommonConstants.STATUS_AUTHORIZED) || chechMap.get(CommonConstants.AUTHORIZESTATUS).equals(CommonConstants.STATUS_EXCEPTION)) {
                        //sqlMap.executeUpdate("updateAvailBalanceTD", authMap);
                        //sqlMap.executeUpdate("updateOtherBalancesTD", authMap);
                        double shdowMinus = dailyTO.getAmount().doubleValue();
                        shdowMinus = shdowMinus * -1;
                        authMap.put(TransactionDAOConstants.AMT, new Double(shdowMinus));
                        //sqlMap.executeUpdate("updateShadowCreditTD", authMap);
                        sqlMap.executeQueryForList("getBalanceTD", authMap);
                        authMap.put(TransactionDAOConstants.AMT, new Double(shdowMinus));
                        List clearlst = sqlMap.executeQueryForList("getBalanceTD", authMap);
                        if (clearlst != null && clearlst.size() > 0) {
                            HashMap clearMap = new HashMap();
                            clearMap = (HashMap) clearlst.get(0);
                            authMap.put("CLEAR_BALANCE", CommonUtil.convertObjToDouble(clearMap.get("CLEAR_BALANCE")));
                            authMap.put("TRANS_DT", currDate.clone());
                            authMap.put("INITIATED_BRANCH", _branchCode);
                            sqlMap.executeUpdate("authorizeDailyDeposit", authMap);
                            clearMap = null;
                            clearlst = null;
                        }
                    } else {
                        Map.put(ACTION, "REJECTION");
                        double shdowMinus = dailyTO.getAmount().doubleValue();
                        shdowMinus = shdowMinus * -1;
                        authMap.put(TransactionDAOConstants.AMT, new Double(shdowMinus));
                        authMap.put("CLEAR_BALANCE", new Double(0));
                        //sqlMap.executeUpdate("updateShadowCreditTD", authMap);
                        authMap.put("TRANS_DT", currDate.clone());
                        authMap.put("INITIATED_BRANCH", _branchCode);
                        sqlMap.executeUpdate("authorizeDailyDeposit", authMap);
                    }
                    authMap = null;
                    dailyTO = null;
                }
            }
        }
    }
        
    public String getProductType(String prodId){
        String prodType = "";
        try{        
            HashMap prodTypeMap = new HashMap();
            prodTypeMap.put("value", CommonUtil.convertObjToStr(prodId));
            List lst = sqlMap.executeQueryForList("getProductTypeForProductId", prodTypeMap);
            if (lst != null && lst.size() > 0) {
                prodTypeMap = (HashMap) lst.get(0);    
                prodType = CommonUtil.convertObjToStr(prodTypeMap.get("PROD_TYPE"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("prodType$@$@$@$"+prodType);
        return prodType;
    }    
        
    public void authorizeDailyDepositOnlyPolpully(ArrayList upList, HashMap Map) throws Exception {
        int listsize = upList.size();
        System.out.println(new StringBuilder().append("Out side  authorizeDailyDeposit").append(Map).toString());
        HashMap chechMap = new HashMap();
        chechMap = (HashMap)Map.get("AUTHORIZEMAP");
        if (chechMap.containsKey("AUTHORIZESTATUS")) {
          System.out.println("Out side  authorizeDailyDeposit");
          System.out.println(new StringBuilder().append("Out side  listsize&&&&&").append(listsize).toString());
          if (listsize > 0) {
            System.out.println(new StringBuilder().append("inside   listsize&&&&&").append(listsize).toString());
            for (int i = 0; i < listsize; i++) {
              DailyDepositTransTO dailyTO = new DailyDepositTransTO();
              dailyTO = (DailyDepositTransTO)upList.get(i);
              System.out.println(new StringBuilder().append("insede dailyTO $$$$$$$$$").append(dailyTO).toString());
              HashMap authMap = new HashMap();
              authMap.put("BATCH_ID", dailyTO.getBatch_id());
              authMap.put("ACC_NUM", dailyTO.getAcct_num());
              authMap.put("ACT_NUM", dailyTO.getAcct_num());
              authMap.put("ACCOUNTNO", dailyTO.getAcct_num());
              authMap.put("AGENT_NO", dailyTO.getAgent_no());
              authMap.put("AUTHORIZE_BY", Map.get("USER_ID"));
              authMap.put("UNCLEAR_AMT", dailyTO.getAmount());
              authMap.put("AMOUNT", dailyTO.getAmount());
              authMap.put("TODAY_DT", this.properFormatDate);
              authMap.put("AUTHORIZE_DT", this.properFormatDate);
              authMap.put("AUTHORIZE_STATUS", chechMap.get("AUTHORIZESTATUS"));
              String acctnum=CommonUtil.convertObjToStr(dailyTO.getAcct_num());
              System.out.println(new StringBuilder().append("insede authMap $$$$$$$$$").append(authMap).toString());
              if ((chechMap.get("AUTHORIZESTATUS").equals("AUTHORIZED")) || (chechMap.get("AUTHORIZESTATUS").equals("EXCEPTION"))) {
                if (dailyTO!=null && getProductType(acctnum.substring(4, 7)).equals("TD")){ 
                    boolean fromDailyDepositScreen = false; // Rework - KD-2034 by nithya
                    if (dailyTO.getScreenName() != null && dailyTO.getScreenName().equals("Daily Deposit")) {
                        fromDailyDepositScreen = true;                        
                    }
                    if (fromDailyDepositScreen || (dailyTO.getConsolidated().equalsIgnoreCase("Y"))) { //Added by nithya on 04-07-2020 for KD-2034
                    sqlMap.executeUpdate("updateAvailBalanceTD", authMap);
                    sqlMap.executeUpdate("updateOtherBalancesTD", authMap);
                    }
                    double shdowMinus = dailyTO.getAmount().doubleValue();
                    shdowMinus *= -1.0D;
                    authMap.put("AMOUNT", new Double(shdowMinus));
                    if (fromDailyDepositScreen || (dailyTO.getConsolidated().equalsIgnoreCase("Y"))) { //Added by nithya on 04-07-2020 for KD-2034
                    sqlMap.executeUpdate("updateShadowCreditTD", authMap);
                    }
                    sqlMap.executeQueryForList("getBalanceTD", authMap);
                    authMap.put("AMOUNT", new Double(shdowMinus));
                    List clearlst = sqlMap.executeQueryForList("getBalanceTD", authMap);
                    if ((clearlst != null) && (clearlst.size() > 0)) {
                      HashMap clearMap = new HashMap();
                      clearMap = (HashMap)clearlst.get(0);
                      authMap.put("CLEAR_BALANCE", CommonUtil.convertObjToDouble(clearMap.get("CLEAR_BALANCE")));
                      authMap.put("TRANS_DT", ServerUtil.getCurrentDate(this._branchCode));
                      authMap.put("INITIATED_BRANCH", this._branchCode);
                      sqlMap.executeUpdate("authorizeDailyDeposit", authMap);
                      clearMap = null;
                      clearlst = null;
                    }
                }
                //For OA
                if (dailyTO!=null && getProductType(acctnum.substring(4, 7)).equals("OA")){
                    List OAclearlst = sqlMap.executeQueryForList("getBalanceOA", authMap);
                    authMap.put("ACCOUNTNO", dailyTO.getAcct_num());
                    double OAshdowMinus = dailyTO.getAmount().doubleValue();
                    OAshdowMinus = OAshdowMinus * -1;
                    authMap.put(TransactionDAOConstants.AMT, new Double(OAshdowMinus));
                    sqlMap.executeQueryForList("getBalanceOA", authMap);
                    authMap.put(TransactionDAOConstants.AMT, new Double(OAshdowMinus));
                    HashMap OAclearMap = new HashMap();
                    OAclearMap = (HashMap) OAclearlst.get(0);
                    authMap.put("CLEAR_BALANCE", CommonUtil.convertObjToDouble(OAclearMap.get("CLEAR_BALANCE")));
                    authMap.put("TRANS_DT", currDate.clone());
                    authMap.put("INITIATED_BRANCH", _branchCode);
                    sqlMap.executeUpdate("authorizeDailyDeposit", authMap);
                    OAclearMap = null;
                    OAclearlst = null;
                }
                //For SA
                if (dailyTO!=null && getProductType(acctnum.substring(4, 7)).equals("SA")){
                     authMap.put(TransactionDAOConstants.UNCLEAR_AMT, new Double(0));
                     authMap.put(TransactionDAOConstants.AMT, dailyTO.getAmount());
                     authMap.put("TRANS_DT", currDate.clone());
                     authMap.put("INITIATED_BRANCH", _branchCode);
                     sqlMap.executeUpdate("authorizeDailyDeposit", authMap);
                }
                //added by rishad for od and loan daily deposit trans updation
                  if (dailyTO!=null && getProductType(acctnum.substring(4, 7)).equals("TL")){
                     authMap.put(TransactionDAOConstants.UNCLEAR_AMT, new Double(0));
                     authMap.put(TransactionDAOConstants.AMT, dailyTO.getAmount());
                     authMap.put("TRANS_DT", currDate.clone());
                     authMap.put("INITIATED_BRANCH", _branchCode);
                     sqlMap.executeUpdate("authorizeDailyDeposit", authMap);
                }
                    
              } else {
                Map.put("ACTION", "REJECTION");
                double shdowMinus = dailyTO.getAmount().doubleValue();
                shdowMinus *= -1.0D;
                authMap.put("AMOUNT", new Double(shdowMinus));
                authMap.put("CLEAR_BALANCE", new Double(0.0D));
                sqlMap.executeUpdate("updateShadowCreditTD", authMap);
                authMap.put("TRANS_DT", ServerUtil.getCurrentDate(this._branchCode));
                authMap.put("INITIATED_BRANCH", this._branchCode);
                sqlMap.executeUpdate("authorizeDailyDeposit", authMap);
              }
              authMap = null;
              dailyTO = null;
            }
          }
        }
    }

    public void authorizeDailyDeposit(ArrayList upList, HashMap Map) throws Exception {
        int listsize = upList.size();
        System.out.println("Out side  authorizeDailyDeposit" + Map);
        HashMap chechMap = new HashMap();
        chechMap = (HashMap) Map.get(CommonConstants.AUTHORIZEMAP);
        if (chechMap.containsKey(CommonConstants.AUTHORIZESTATUS)) {
            System.out.println("Out side  authorizeDailyDeposit");
            System.out.println("Out side  listsize&&&&&" + listsize);
            if (listsize > 0) {
                System.out.println("inside   listsize&&&&&" + listsize);
                for (int i = 0; i < listsize; i++) {
                    DailyDepositTransTO dailyTO = new DailyDepositTransTO();
                    dailyTO = (DailyDepositTransTO) upList.get(i);
                    HashMap authMap = new HashMap();
                    System.out.println("insede dailyTO $$$$$$$$$" + dailyTO);
                    if (dailyTO.getTrans_type() != null && dailyTO.getTrans_type().equals("D")) {

                        authMap.put(BATCH_ID, dailyTO.getBatch_id());
                        authMap.put("ACC_NUM", dailyTO.getAcct_num());
                        authMap.put(CommonConstants.ACT_NUM, dailyTO.getAcct_num());
                        authMap.put(TransactionDAOConstants.ACCT_NO, dailyTO.getAcct_num());
                        authMap.put("AGENT_NO", dailyTO.getAgent_no());
                        authMap.put("AUTHORIZE_BY", Map.get(CommonConstants.USER_ID));
                        authMap.put("UNCLEAR_AMT", dailyTO.getAmount());
                        authMap.put(TransactionDAOConstants.AMT, dailyTO.getAmount());
                        authMap.put("TODAY_DT", properFormatDate);
                        authMap.put("AUTHORIZE_DT", properFormatDate);
                        authMap.put(AUTHORIZE_STATUS, chechMap.get("AUTHORIZESTATUS"));
                        System.out.println("insede authMap $$$$$$$$$" + authMap);
                        if (chechMap.get(CommonConstants.AUTHORIZESTATUS).equals(CommonConstants.STATUS_AUTHORIZED) || chechMap.get(CommonConstants.AUTHORIZESTATUS).equals(CommonConstants.STATUS_EXCEPTION)) {
                            //                       List clearlst=sqlMap.executeQueryForList("getBalanceTD", authMap);
                            //                       if(clearlst!=null && clearlst.size()>0){
                            //                          HashMap clearMap=new HashMap();
                            //                          clearMap=(HashMap)clearlst.get(0);
                            //                           authMap.put(CLEAR_BALANCE,clearMap.get(CLEAR_BALANCE));
                            //
                            sqlMap.executeUpdate("updateAvailBalanceTD", authMap);
                            sqlMap.executeUpdate("updateOtherBalancesTD", authMap);
                            double shdowMinus = dailyTO.getAmount().doubleValue();
                            shdowMinus = shdowMinus * -1;
                            authMap.put(TransactionDAOConstants.AMT, new Double(shdowMinus));
                            sqlMap.executeUpdate("updateShadowCreditTD", authMap);
                            //                         shdowMinus=shdowMinus*-1;
                            sqlMap.executeQueryForList("getBalanceTD", authMap);
                            authMap.put(TransactionDAOConstants.AMT, new Double(shdowMinus));
                            List clearlst = sqlMap.executeQueryForList("getBalanceTD", authMap);
                            if (clearlst != null && clearlst.size() > 0) {
                                HashMap clearMap = new HashMap();
                                clearMap = (HashMap) clearlst.get(0);
                                authMap.put("CLEAR_BALANCE", CommonUtil.convertObjToDouble(clearMap.get("CLEAR_BALANCE")));
                                authMap.put("TRANS_DT", currDate.clone());
                                authMap.put("INITIATED_BRANCH", _branchCode);
                                sqlMap.executeUpdate("authorizeDailyDeposit", authMap);

                                clearMap = null;
                                clearlst = null;
                            }
                        } else {
                            Map.put(ACTION, "REJECTION");
                            //                        sqlMap.executeUpdate("authorizeDailyDeposit", authMap);
                            double shdowMinus = dailyTO.getAmount().doubleValue();
                            shdowMinus = shdowMinus * -1;
                            authMap.put(TransactionDAOConstants.AMT, new Double(shdowMinus));
                            authMap.put("CLEAR_BALANCE", new Double(0));
                            sqlMap.executeUpdate("updateShadowCreditTD", authMap);
                            authMap.put("TRANS_DT", currDate.clone());
                            authMap.put("INITIATED_BRANCH", _branchCode);
                            sqlMap.executeUpdate("authorizeDailyDeposit", authMap);

                        }
                    } else if (dailyTO.getTrans_type() != null && dailyTO.getTrans_type().equals("L")) {
                        /*
                         * authMap.put(BATCH_ID,dailyTO.getBatch_id());
                         * authMap.put("ACC_NUM",dailyTO.getAcct_num());
                         * authMap.put(CommonConstants.ACT_NUM,dailyTO.getAcct_num());
                         * authMap.put(TransactionDAOConstants.ACCT_NO,dailyTO.getAcct_num());
                         * authMap.put("AGENT_NO",dailyTO.getAgent_no());
                         * authMap.put("AUTHORIZE_BY",Map.get(CommonConstants.USER_ID));
                         * authMap.put("UNCLEAR_AMT",dailyTO.getAmount());
                         * authMap.put(TransactionDAOConstants.AMT,dailyTO.getAmount());
                         * authMap.put("TODAY_DT", properFormatDate);
                         * authMap.put("AUTHORIZE_DT",properFormatDate);
                         * authMap.put(AUTHORIZE_STATUS,chechMap.get("AUTHORIZESTATUS"));
                         * System.out.println("insede authMap
                         * $$$$$$$$$"+authMap); if(
                         * chechMap.get(CommonConstants.AUTHORIZESTATUS).equals(CommonConstants.STATUS_AUTHORIZED)||chechMap.get(CommonConstants.AUTHORIZESTATUS).equals(CommonConstants.STATUS_EXCEPTION)){
                         * * Transaction transModuleBased; transModuleBased =
                         * TransactionFactory.createTransaction("TL"); HashMap
                         * ruleMapLoan = getRuleMapLoan(dailyTO,0.0,true);
                         * transModuleBased.authorizeUpdate(ruleMapLoan,dailyTO.getAmount());//ruleMap
                         *
                         * List
                         * clearlst=sqlMap.executeQueryForList("getBalanceTL",
                         * authMap); if(clearlst!=null && clearlst.size()>0){
                         * HashMap clearMap=new HashMap();
                         * clearMap=(HashMap)clearlst.get(0);
                         * authMap.put("CLEAR_BALANCE",clearMap.get("CLEAR_BALANCE"));
                         * authMap.put("TRANS_DT",
                         * currDate);
                         * authMap.put("INITIATED_BRANCH",_branchCode);
                         * sqlMap.executeUpdate("authorizeDailyDeposit",
                         * authMap);
                         *
                         * clearMap=null; clearlst=null; } } else{
                         * Map.put(ACTION,"REJECTION"); //
                         * sqlMap.executeUpdate("authorizeDailyDeposit",
                         * authMap); double
                         * shdowMinus=dailyTO.getAmount().doubleValue();
                         * shdowMinus=shdowMinus*-1;
                         * authMap.put(TransactionDAOConstants.AMT,new
                         * Double(shdowMinus)); authMap.put("CLEAR_BALANCE",new
                         * Double(0)); Transaction transModuleBased;
                         * transModuleBased =
                         * TransactionFactory.createTransaction("TL"); HashMap
                         * ruleMapLoan = getRuleMapLoan(dailyTO,0.0,true);
                         * transModuleBased.authorizeUpdate(ruleMapLoan,new
                         * Double(shdowMinus));//ruleMap authMap.put("TRANS_DT",
                         * currDate);
                         * authMap.put("INITIATED_BRANCH",_branchCode);
                         * sqlMap.executeUpdate("authorizeDailyDeposit",
                         * authMap);
                         *
                         *
                         *
                         *
                         *
                         * }
                         *
                         */
                    }
                    authMap = null;
                    dailyTO = null;
                }
            }
        }
    }

    public HashMap getRuleMapLoan(DailyDepositTransTO dailyTO, double prevAmount, boolean makeNegative) throws Exception {
        HashMap inputMap = new HashMap();
        double amount = dailyTO.getAmount().doubleValue();

        //  if(dailyTO.getTrans_mode().equals(TransactionDAOConstants.DEBIT) && makeNegative) {
        //      amount = -amount + prevAmount;
        //  } else {
        amount -= prevAmount;
        //  }

        //  if (txTransferTORuleMap.getProdType().equals(TransactionFactory.GL)) {
        //     txTransferTORuleMap.setActNum(null);  // This condition added to prevent actnum entries in GL
        // }

        String acctNo = dailyTO.getAcct_num();

        //  if ((acctNo == null || acctNo.equals("")) && (txTransferTORuleMap.getAcHdId() != null)) {
        //     acctNo = txTransferTORuleMap.getAcHdId();
        // }
        String prod_id = "";
        if (acctNo != null) {
            prod_id = acctNo.substring(3, 6);
            System.out.println("acctNo UIoooooooooooooooo============" + acctNo);
        }
        inputMap.put(TransactionDAOConstants.ACCT_NO, acctNo);
        inputMap.put(TransactionDAOConstants.AMT, CommonUtil.convertObjToDouble(String.valueOf(amount)));
        inputMap.put(TransactionDAOConstants.INSTRUMENT_TYPE, "");
        inputMap.put(TransactionDAOConstants.INSTRUMENT_1, "");
        inputMap.put(TransactionDAOConstants.INSTRUMENT_2, "");
        inputMap.put(TransactionDAOConstants.DATE, dailyTO.getTrn_dt());
        inputMap.put(TransactionDAOConstants.TRANS_TYPE, dailyTO.getTrans_mode());
        inputMap.put(TransactionDAOConstants.BRANCH_CODE, _branchCode);
        inputMap.put(TransactionDAOConstants.INITIATED_BRANCH, _branchCode);
        inputMap.put(TransactionDAOConstants.TO_STATUS, dailyTO.getStatus());
        inputMap.put(TransactionDAOConstants.AUTHORIZE_BY, dailyTO.getAuthorize_by());
        inputMap.put(TransactionDAOConstants.AUTHORIZE_STATUS, dailyTO.getAuthorize_status());
        inputMap.put(TransactionDAOConstants.TRANS_ID, dailyTO.getBatch_id() + "_" + dailyTO.getTrans_id());
        inputMap.put(TransactionDAOConstants.TODAY_DT, properFormatDate.clone());
        inputMap.put(TransactionDAOConstants.TRANS_MODE, TransactionDAOConstants.TRANSFER);
        inputMap.put("ACTUAL_AMT", CommonUtil.convertObjToDouble(String.valueOf(amount))); // Added by Rajesh for LimitCheckingRule. No need to deduct the previous amount for Operative. Because the system should check the actual amount with Minimum Balance (on product level)
        //        inputMap.put("PARTICULARS",txTransferTORuleMap.getParticulars()); particular using passbook only
        inputMap.put("LOANPARTICULARS", loanParticulars);
        if (debitLoanType != null && debitLoanType.length() > 0) {
            inputMap.put("DEBIT_LOAN_TYPE", debitLoanType);
        } else {
            inputMap.put("DEBIT_LOAN_TYPE", loanParticulars);
        }
        //for termloan
        if (isForLoanDebitInt()) {
            boolean debit_int = true;
            inputMap.put("DEBIT_INT", new Boolean(debit_int));
        }
        //  if(txTransferTORuleMap.getProdType().equals(TransactionFactory.ADVANCES)) {
        //      inputMap.put(TransactionDAOConstants.PARTICULARS ,txTransferTORuleMap.getParticulars());
        // }

        // if(txTransferTORuleMap.getProdType().equals(TransactionFactory.LOANS)|| txTransferTORuleMap.getProdType().equals(TransactionFactory.AGRILOANS)||  txTransferTORuleMap.getProdType().equals(TransactionFactory.ADVANCES)||  txTransferTORuleMap.getProdType().equals(TransactionFactory.AGRIADVANCES) ) {
        inputMap.put(PROD_ID, prod_id);
        inputMap.put("PROD_TYPE", "TL");
        // }
        inputMap.put("ACCOUNT_CLOSING", "ACCOUNT_CLOSING");
        if (act_closing_min_bal_check != null && act_closing_min_bal_check.equalsIgnoreCase("ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER")) {
            inputMap.put("ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER", "ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER");
        }
        if (depositClosingFlag == true) {
            inputMap.put("DEPOSIT_CLOSING", "DEPOSIT_CLOSING");
        }
        // For Corporate Loan purpose added by Rajesh
        if (corpLoanMap != null && corpLoanMap.size() > 0) {
            inputMap.putAll(corpLoanMap);
        }
        if (!inputMap.containsKey(TransactionDAOConstants.PARTICULARS)) {
            inputMap.put(TransactionDAOConstants.PARTICULARS, loanParticulars);
        }

        System.out.println("#$#$LOANS_PARITICULARS :" + loanParticulars + ":");
        System.out.println("#$#$DEBIT_LOAN_TYPE :" + debitLoanType + ":");
        System.out.println("#$#$inputMap :" + inputMap + ":");

        return inputMap;
    }
    //for interest details transfer from tmp to loans_interest

    void insertInterestDetails(HashMap Interest) throws Exception {
        HashMap singleRecord = new HashMap();
        Interest.put("ACT_NUM", Interest.get("ACCT_NUM"));
        List lst = sqlMap.executeQueryForList("selectLoanInterestTMP", Interest);
        if (lst != null && lst.size() > 0) {

            sqlMap.executeUpdate("deleteLoanInterest", Interest);
            for (int i = 0; i < lst.size(); i++) {
                HashMap insertRecord = new HashMap();
                singleRecord = (HashMap) lst.get(i);
                insertRecord.put("ACT_NUM", singleRecord.get("ACT_NUM"));
                String from_dt = CommonUtil.convertObjToStr(singleRecord.get("FROM_DT"));
                Date fromDate = DateUtil.getDateMMDDYYYY(from_dt);
                insertRecord.put("FROM_DT", CommonUtil.getProperDate(currDate, fromDate));
                String to_dt = CommonUtil.convertObjToStr(singleRecord.get("TO_DATE"));
                Date toDate = DateUtil.getDateMMDDYYYY(to_dt);
                insertRecord.put("TO_DATE", CommonUtil.getProperDate(currDate, toDate));
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
                insertRecord.put("PROD_ID", singleRecord.get("PROD_ID"));
                String intcalcdt = CommonUtil.convertObjToStr(singleRecord.get("INT_CALC_DT"));
                Date intcalcDt = DateUtil.getDateMMDDYYYY(intcalcdt);
                insertRecord.put("INT_CALC_DT",CommonUtil.getProperDate(currDate, intcalcDt));
                insertRecord.put("CUST_ID", singleRecord.get("CUST_ID"));
                System.out.println("insertrecord####" + insertRecord);
                sqlMap.executeUpdate("insertLoanInterest", insertRecord);//singleRecord
            }
            sqlMap.executeUpdate("deleteLoanInterestTMP", Interest);
        }
    }

    private TxTransferTO setDepositPenal(TxTransferTO txtransTo) {
        TxTransferTO txTo = new TxTransferTO();
        txTo.setProdType(txtransTo.getProdType());
        txTo.setProdId(txtransTo.getProdId());
        txTo.setActNum(txtransTo.getActNum());
        txTo.setAcHdId(txtransTo.getAcHdId());
        txTo.setAmount(txtransTo.getAmount());
        txTo.setAuthorizeBy(txtransTo.getAuthorizeBy());
        txTo.setAuthorizeDt(txtransTo.getAuthorizeDt());
        txTo.setAuthorizeRemarks(txtransTo.getAuthorizeRemarks());
        txTo.setAuthorizeStatus(txtransTo.getAuthorizeStatus());
        txTo.setBatchId(txtransTo.getBatchId());
        txTo.setBranchId(txtransTo.getBranchId());
        txTo.setInitChannType(txtransTo.getInitChannType());
        txTo.setInitTransId(txtransTo.getInitTransId());
        txTo.setInitiatedBranch(txtransTo.getInitiatedBranch());
        txTo.setInpAmount(txtransTo.getInpAmount());
        txTo.setAuthorizeBy(txtransTo.getAuthorizeBy());
        txTo.setTransDt(txtransTo.getTransDt());
        txTo.setTransType(txtransTo.getTransType());
        txTo.setInstType(txtransTo.getInstType());
        txTo.setInstrumentNo1(txtransTo.getInstrumentNo1());
        txTo.setInstrumentNo2(txtransTo.getInstrumentNo2());
        txTo.setInstDt(txtransTo.getInstDt());
        txTo.setTransId(txtransTo.getTransId());
        txTo.setTransMode(txtransTo.getTransMode());
        txTo.setTransType(txtransTo.getTransType());
        txTo.setStatus(txtransTo.getStatus());
        txTo.setStatusBy(txtransTo.getStatusBy());
        txTo.setStatusDt(txtransTo.getStatusDt());
        txTo.setLinkBatchId(txtransTo.getLinkBatchId());
        txTo.setParticulars(txtransTo.getParticulars());
        return txTo;

    }

    private ArrayList penalReceived(HashMap penalMap) throws Exception {
        ArrayList transferTOs = new ArrayList();
        double penalAmt = 0.0;
        HashMap multiDepositMap = new HashMap();
        transferTOs = (ArrayList) penalMap.get("TxTransferTO");
        System.out.println("transferTOs.size()@@@@@" + transferTOs.size());
        if (penalMap.containsKey("MULTIPLE_DEPOSIT_PENAL")) {
            multiDepositMap = (HashMap) penalMap.get("MULTIPLE_DEPOSIT_PENAL");
        } else {
            penalAmt = CommonUtil.convertObjToDouble(penalMap.get(DEPOSIT_PENAL_AMT)).doubleValue();
        }
        for (int i = 0; i < transferTOs.size(); i++) {
            TxTransferTO txtransferTo = (TxTransferTO) transferTOs.get(i);
            TxTransferTO txtransfer = setDepositPenal(txtransferTo);
            String batch = CommonUtil.convertObjToStr(txtransfer.getBatchId());
            System.out.println("batch%%%%%" + batch + "txtransfer#####" + txtransfer);
//            double penalAmt = CommonUtil.convertObjToDouble(penalMap.get(DEPOSIT_PENAL_AMT)).doubleValue();
            if (batch.equalsIgnoreCase("-") || batch.equals("")) {
                if (txtransfer.getTransType().equals(TransactionDAOConstants.CREDIT) && (txtransfer.getProdType().equals("TD"))) {
                    String act_Num = CommonUtil.convertObjToStr(txtransfer.getActNum());
                    System.out.println("penalMap.containsKey(act_Num)####" + multiDepositMap.containsKey(act_Num));
                    if (multiDepositMap.containsKey(act_Num)) {
                        System.out.println("act_Num####" + act_Num);
                        HashMap penalDataMap = (HashMap) multiDepositMap.get(act_Num);
                        System.out.println("act_Num####penalDataMap" + penalDataMap + act_Num);
                        penalAmt = CommonUtil.convertObjToDouble(penalDataMap.get(DEPOSIT_PENAL_AMT)).doubleValue();
                    } else {
                        penalAmt = CommonUtil.convertObjToDouble(penalMap.get(DEPOSIT_PENAL_AMT)).doubleValue();
                    }
                    double totalAmt = CommonUtil.convertObjToDouble(txtransfer.getAmount()).doubleValue();
                    double balanceAmt = totalAmt - penalAmt;
//                    String act_Num = CommonUtil.convertObjToStr(txtransferTo.getActNum());
                    System.out.println("balanceAmt" + balanceAmt + "penalAmt" + penalAmt + "totalAmt" + totalAmt);
                    txtransfer.setAmount(new Double(balanceAmt));
                    txtransfer.setTransModType("TD"); // added by nithya on 25-09-2019 for KD 622 - STANDING INSTRUCTION ISSUE
                    txtransfer.setInstrumentNo2("DEPOSIT_PENAL");
                    txtransfer.setInpAmount(new Double(balanceAmt));
                    txtransfer.setLinkBatchId(act_Num);
                    if (penalMap != null && penalMap.containsKey("TRANS_ALL_ID")) {
                        txtransfer.setTransAllId(CommonUtil.convertObjToStr(penalMap.get("TRANS_ALL_ID")));
                        txtransfer.setParticulars("By Principal " + txtransferTo.getActNum());
                        txtransfer.setAuthorizeStatus_2("ENTERED_AMOUNT");
                        //txtransfer.setGlTransActNum(txtransferTo.getActNum());
                    }
                    else
                    {
                         txtransfer.setParticulars(txtransferTo.getParticulars());
                    }
                    txtransfer.setGlTransActNum(txtransferTo.getActNum());
                    if (penalMap.containsKey("FROM_RD_RECOVERY_SCREEN")) {
                        txtransfer.setRec_mode("RP");
                    }
                    if (penalMap.containsKey("RD_PENAL_SINGLE_TRANS_ID") && penalMap.get("RD_PENAL_SINGLE_TRANS_ID") != null) {//KD-3586
                        txtransfer.setSingleTransId(CommonUtil.convertObjToStr(penalMap.get("RD_PENAL_SINGLE_TRANS_ID")));
                    }
                    transferTOs.set(i, txtransfer);
                    System.out.println("transferTOs Not a penal Amount" + transferTOs);
                    if (penalAmt > 0) {
                        HashMap prodMap = new HashMap();
                        txtransferTo = (TxTransferTO) transferTOs.get(i);
                        txtransfer = new TxTransferTO();
                        txtransfer = setDepositPenal(txtransferTo);
                        prodMap.put(PROD_ID, txtransfer.getProdId());
                        List lst = sqlMap.executeQueryForList("getBehavesLikeForDeposit", prodMap);
                        if (lst != null && lst.size() > 0) {
                            prodMap = (HashMap) lst.get(0);
                        }
                        if (prodMap.get(BEHAVES_LIKE).equals("RECURRING")) {
                            prodMap.put(PROD_ID, txtransfer.getProdId());
                            lst = sqlMap.executeQueryForList("getDepositClosingHeads", prodMap);
                            if (lst != null && lst.size() > 0) {
                                prodMap = (HashMap) lst.get(0);
                                txtransfer.setAmount(new Double(penalAmt));
                                txtransfer.setInpAmount(new Double(penalAmt));
                                txtransfer.setActNum("");
                                txtransfer.setTransModType(TransactionFactory.GL);// added by nithya on 25-09-2019 for KD 622 - STANDING INSTRUCTION ISSUE
                                txtransfer.setProdType(TransactionFactory.GL);
                                txtransfer.setProdId("");
                                txtransfer.setAcHdId(CommonUtil.convertObjToStr(prodMap.get("DELAYED_ACHD")));
                                txtransfer.setInstrumentNo2("DEPOSIT_PENAL");
                                if (penalMap.containsKey("FROM_RD_RECOVERY_SCREEN")) {
                                    txtransfer.setRec_mode("RP");
                                } 
                                if(penalMap.containsKey("RD_PENAL_SINGLE_TRANS_ID") && penalMap.get("RD_PENAL_SINGLE_TRANS_ID") != null){
                                    txtransfer.setSingleTransId(CommonUtil.convertObjToStr(penalMap.get("RD_PENAL_SINGLE_TRANS_ID")));
                                }
                                txtransfer.setLinkBatchId(act_Num);
                                if (penalMap != null && penalMap.containsKey("TRANS_ALL_ID")) {
                                    txtransfer.setTransAllId(CommonUtil.convertObjToStr(penalMap.get("TRANS_ALL_ID")));
                                    txtransfer.setParticulars("By Penal " + txtransferTo.getActNum());
                                    txtransfer.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                } else {
                                    txtransfer.setParticulars("Penal Amount Received " + txtransferTo.getActNum());
                                }
                                txtransfer.setGlTransActNum(txtransferTo.getActNum());
                                transferTOs.add(txtransfer);
                                penalAmt = 0;
                                System.out.println("transferTOs penal Amount" + transferTOs);
                            }
                        }
                    }
//                    transferTOs.remove(i);                    
                }
            }
        }
        return transferTOs;
    }

    /**
     * Getter for property act_closing_min_bal_check.
     *
     * @return Value of property act_closing_min_bal_check.
     */
    public java.lang.String getAct_closing_min_bal_check() {
        return act_closing_min_bal_check;
    }

    /**
     * Setter for property act_closing_min_bal_check.
     *
     * @param act_closing_min_bal_check New value of property
     * act_closing_min_bal_check.
     */
    public void setAct_closing_min_bal_check(java.lang.String act_closing_min_bal_check) {
        this.act_closing_min_bal_check = act_closing_min_bal_check;
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
    private ArrayList setServiceTax(HashMap fromMap) throws Exception {
        ArrayList transferTOs = new ArrayList();
        double taxAmt = 0.0;
        transferTOs = (ArrayList) fromMap.get("TxTransferTO");
        HashMap serTaxMap = new HashMap();
        String linkId="";
        if (fromMap.containsKey("SERVICE_TAX_MAP")) {
            serTaxMap = (HashMap) fromMap.get("SERVICE_TAX_MAP");
        }
        double serviceTax = 0.0;
        double swachhCess = 0.0;
        double krishikalyanCess = 0.0;
        double normalServiceTax = 0.0;
        // taxAmt = CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT"));
        if (serTaxMap.containsKey("TOT_TAX_AMT") && serTaxMap.get("TOT_TAX_AMT") != null) {
            taxAmt = CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT"));
        }
        if (serTaxMap.containsKey(ServiceTaxCalculation.SWACHH_CESS) && serTaxMap.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
            swachhCess = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.SWACHH_CESS));
        }
        if (serTaxMap.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && serTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
            krishikalyanCess = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
        }
        if (serTaxMap.containsKey(ServiceTaxCalculation.SERVICE_TAX) && serTaxMap.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
            normalServiceTax = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.SERVICE_TAX));
        }
        int size = transferTOs.size();
        int t = 0;
        for (int i = 0; i < size; i++) {
            TxTransferTO txtransferTo = (TxTransferTO) transferTOs.get(i);
            TxTransferTO txtransfer = setDepositPenal(txtransferTo);
            String batch = CommonUtil.convertObjToStr(txtransfer.getBatchId());
            if (batch.equalsIgnoreCase("-") || batch.equals("")) {
                if (txtransfer.getTransType().equals(TransactionDAOConstants.CREDIT)) {
                    //txtransfer.setTransModType(CommonConstants.GL_TRANSMODE_TYPE);
                    txtransfer.setTransModType(txtransferTo.getTransModType());// Added by nithya on 05-11-2018 for KD 310 - 0018191: Trans_mode shows GL instead of TL
                    String act_Num = CommonUtil.convertObjToStr(txtransfer.getActNum());
                    taxAmt = CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT"));
                    double totalAmt = CommonUtil.convertObjToDouble(txtransfer.getAmount()).doubleValue();
                    //double balanceAmt = totalAmt - taxAmt;
                   // txtransfer.setAmount(new Double(balanceAmt));
                   // txtransfer.setInpAmount(new Double(balanceAmt));
                    if(txtransfer.getLinkBatchId()==null || txtransfer.getLinkBatchId().length()<0){
                  //   txtransfer.setLinkBatchId(act_Num);
                    //txtransfer.setGlTransActNum(txtransferTo.getActNum());
                    }
                    transferTOs.set(i, txtransfer);
                    if (taxAmt > 0 && t==0) {
                        t++;
                        taxAmt -= (swachhCess + krishikalyanCess);
                        txtransferTo = (TxTransferTO) transferTOs.get(i);
                        txtransfer = new TxTransferTO();
                        txtransfer = setDepositPenal(txtransferTo);
                        txtransfer.setAmount(new Double(normalServiceTax));
                        txtransfer.setInpAmount(new Double(normalServiceTax));
                        txtransfer.setAcHdId(CommonUtil.convertObjToStr(serTaxMap.get("TAX_HEAD_ID")));
                        txtransfer.setLinkBatchId("");
                        txtransfer.setActNum("");
                        txtransfer.setProdId(""); 
                        txtransfer.setGlTransActNum("");
                        txtransfer.setProdType(TransactionFactory.GL);
                        txtransfer.setParticulars("Service Tax Recived" + txtransferTo.getActNum());
                        txtransfer.setTransModType(CommonConstants.GL_TRANSMODE_TYPE);
                        txtransfer.setGlTransActNum(txtransferTo.getActNum());
                        txtransfer.setInstrumentNo2(CommonConstants.SERVICE_TAX_CHRG);
                        linkId=txtransfer.getLinkBatchId();
                        transferTOs.add(txtransfer);
                        if (swachhCess > 0) {
                            txtransferTo = (TxTransferTO) transferTOs.get(i);
                            txtransfer = new TxTransferTO();
                            txtransfer = setDepositPenal(txtransferTo);
                            txtransfer.setAmount(new Double(swachhCess));
                            txtransfer.setInpAmount(new Double(swachhCess));
                            txtransfer.setAcHdId(CommonUtil.convertObjToStr(serTaxMap.get(ServiceTaxCalculation.SWACHH_HEAD_ID)));
                            txtransfer.setLinkBatchId("");
                            txtransfer.setActNum("");
                            txtransfer.setProdId("");
                            txtransfer.setGlTransActNum("");
                            txtransfer.setProdType(TransactionFactory.GL);
                            txtransfer.setParticulars("CGST" + txtransferTo.getActNum());
                            txtransfer.setTransModType(CommonConstants.GL_TRANSMODE_TYPE);
                            txtransfer.setGlTransActNum(txtransferTo.getActNum());
                            txtransfer.setInstrumentNo2(CommonConstants.SERVICE_TAX_CHRG);
                            linkId = txtransfer.getLinkBatchId();
                            transferTOs.add(txtransfer);
                        }
                        if (krishikalyanCess > 0) {
                            txtransferTo = (TxTransferTO) transferTOs.get(i);
                            txtransfer = new TxTransferTO();
                            txtransfer = setDepositPenal(txtransferTo);
                            txtransfer.setAmount(new Double(krishikalyanCess));
                            txtransfer.setInpAmount(new Double(krishikalyanCess));
                            txtransfer.setAcHdId(CommonUtil.convertObjToStr(serTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_HEAD_ID)));
                            txtransfer.setLinkBatchId("");
                            txtransfer.setActNum("");
                            txtransfer.setProdId("");
                            txtransfer.setGlTransActNum("");
                            txtransfer.setProdType(TransactionFactory.GL);
                            txtransfer.setParticulars("SGST" + txtransferTo.getActNum());
                            txtransfer.setTransModType(CommonConstants.GL_TRANSMODE_TYPE);
                            txtransfer.setGlTransActNum(txtransferTo.getActNum());
                            txtransfer.setInstrumentNo2(CommonConstants.SERVICE_TAX_CHRG);
                            linkId = txtransfer.getLinkBatchId();
                            transferTOs.add(txtransfer);
                        }
                        taxAmt = 0;
                        
                    }
                }
            }
        }
        return transferTOs;
    }
   private void insertServiceTaxDetails(ServiceTaxDetailsTO objserviceTaxDetailsTO) {
        try {
            objserviceTaxDetailsTO.setServiceTaxDet_Id(getServiceTaxNo());
            objserviceTaxDetailsTO.setParticulars("Transfer Screen");
            sqlMap.executeUpdate("insertServiceTaxDetailsTO", objserviceTaxDetailsTO);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
     private String getServiceTaxNo() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "SERVICETAX_DET_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }
     private void deleteFromServiceTaxDetails(String accNo) {
        try {
            HashMap newMap= new HashMap();
            newMap.put("ACCT_NUM",accNo);
            newMap.put("CREATED_DT",currDate.clone());
            sqlMap.executeUpdate("deleteServiceTaxDetails", newMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
     
     public Date getProperDateFormat(Object obj) {
        Date currDate1 = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDate1 = (Date) properFormatDate.clone();
            currDate1.setDate(tempDt.getDate());
            currDate1.setMonth(tempDt.getMonth());
            currDate1.setYear(tempDt.getYear());
        }
        return currDate1;
    }
     
     /*
     * Day end completed in 0001 branch but after completing day end 0002 branch has to debit the 8001 branch account. 
     * Day end balance already inserted while doing day end of 0001 branch so again we need to insert the dayend once again 
     * because of schedule should be tally on that day
     */
    private void insertDayEndBalance(TxTransferTO objTxTransferTO) throws Exception{
        if(!CommonUtil.convertObjToStr(objTxTransferTO.getProdType()).equals("GL") && !CommonUtil.convertObjToStr(objTxTransferTO.getProdType()).equals("AB")){
            HashMap map =new HashMap();
            map.put("ACT_NUM",objTxTransferTO.getActNum());
            map.put("APP_DT",getProperDateFormat(objTxTransferTO.getTransDt())); // Added by nithya for solving casting date to timestamp issue
            if (CommonUtil.convertObjToStr(objTxTransferTO.getProdType()).equals("AD")) {
                double tod_lim = 0.0;
                double advLimit = 0.0;
                double overAllLimit = 0.0;
                List todlimit = sqlMap.executeQueryForList("getTODLimit", map);
                if (todlimit != null && todlimit.size() > 0) {
                    HashMap where = (HashMap) todlimit.get(0);
                    tod_lim = CommonUtil.convertObjToDouble(where.get("TOD_AMOUNT")).doubleValue();
                }
                List dpList = sqlMap.executeQueryForList("getDrawPowOrLim", map);
                if (dpList != null && dpList.size() > 0) {
                    HashMap advLimitMap = new HashMap();
                    advLimitMap = (HashMap) dpList.get(0);
                    advLimit = CommonUtil.convertObjToDouble(advLimitMap.get("LIMIT")).doubleValue();
                    double dp = CommonUtil.convertObjToDouble(advLimitMap.get("DRAWING_POWER")).doubleValue();
                    if (dp > 0 && dp < advLimit) {
                        advLimit = dp;
                    }
                    overAllLimit = tod_lim + advLimit;
                } else {
//                    advLimit = CommonUtil.convertObjToDouble(((HashMap) accountList.get(i)).get("LIMIT")).doubleValue();
                    overAllLimit = tod_lim + advLimit;
                }
                map.put("TOD_LIMIT", new Double(tod_lim));
                map.put("DPORLIMIT", new Double(advLimit));
                map.put("OVERALL_LIMIT", new Double(overAllLimit));
            }
            System.out .println("map#####"+map);
            String depositSubNo = objTxTransferTO.getActNum();
            if (objTxTransferTO.getProdType()!=null && objTxTransferTO.getProdType().equals("TD") && depositSubNo.lastIndexOf("_") != -1) {
                depositSubNo = depositSubNo.substring(0, depositSubNo.lastIndexOf("_"));
                map.put("ACT_NUM",depositSubNo);
                map.put("DEPOSIT_NO",depositSubNo);
                if(objTxTransferTO.getScreenName() != null && CommonUtil.convertObjToStr(objTxTransferTO.getScreenName()).equals("Deposit Account Renewal")){
                    if(objTxTransferTO.getActNum() != null && objTxTransferTO.getLinkBatchId() != null && objTxTransferTO.getActNum().equals(objTxTransferTO.getLinkBatchId())){
                        map.put("AMOUNT",new Double(0));                
                    }else{
                        List depositAmtList = sqlMap.executeQueryForList("getDepAmtForOldDeposit", map);
                        if (depositAmtList != null && depositAmtList.size() > 0) {
                            HashMap depositMap = new HashMap();
                            depositMap = (HashMap) depositAmtList.get(0);
                            map.put("AMOUNT",depositMap.get("DEPOSIT_AMT"));                
                        }
                    }
                }else{
                    map.put("AMOUNT",new Double(0));                
                }
            }
            map.put("ANY_DATE_IBR","ANY_DATE_IBR");
            sqlMap.executeUpdate("updateDayendBalance" + objTxTransferTO.getProdType(), map);
        }
    }
    
    public String readFromTemplate(String smsTag) throws SAXException {
        String smsString = "";
        try {
            //String hostDir = "D:\\TTCBS\\jboss-3.2.7\\bin\\template\\" + fileName;
            //System.out.println("fileName@$#$@#"+fileName);
            //File file = new File(fileName);
            //String absoluteFilePath = file.getAbsolutePath();
            String smsTemplatePath = CommonUtil.convertObjToStr(ServerUtil.getCbmsParameterMap().get("SMS_TEMPLATE_PATH"));
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(smsTemplatePath));
            if(doc != null){
                doc.getDocumentElement().normalize();
                NodeList listOfMapping = doc.getElementsByTagName(smsTag);
                int totalMappings = listOfMapping.getLength();
    //            System.out.println("readFromTemplate Total no of message : " + totalMappings);
                Element ModuleElement = (Element) listOfMapping.item(0);
                NodeList textMList = ModuleElement.getChildNodes();
                smsString = ((Node) textMList.item(0)).getNodeValue().trim();
            }else{
                System.out.println("Template path not configured,Please configure it : ");
            }
        } catch (SAXParseException err) {
            System.out.println("** Parsing error" + ", line "
                    + err.getLineNumber() + ", uri " + err.getSystemId());
            System.out.println(" " + err.getMessage());
        } catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return smsString;
    }
    
     private ArrayList overDueIntWaiveOff(ArrayList transferTOs, TxTransferTO obj,double waiveAmt, String generateWaiveId) throws Exception {
        TermLoanPenalWaiveOffTO objpenalWaive = new TermLoanPenalWaiveOffTO();
        ArrayList tempWaiveOffList = new ArrayList();
        tempWaiveOffList = overDueIntWaiveoffTransaction(transferTOs, obj, waiveAmt);
        if (obj != null) {
            objpenalWaive.setAcctNum(obj.getActNum());
            objpenalWaive.setWaiveDt((Date) properFormatDate.clone());
            objpenalWaive.setWaiveAmt(new Double(waiveAmt));
            objpenalWaive.setRemarks("OVERDUEINT_WAIVEOFF");
            objpenalWaive.setStatus(CommonConstants.STATUS_CREATED);
            objpenalWaive.setStatusBy(obj.getStatusBy());
            objpenalWaive.setStatusDt((Date) properFormatDate.clone());            
            objpenalWaive.setWaiveOffId(generateWaiveId);
            sqlMap.executeUpdate("insertTermLoanWaiveOffTO", objpenalWaive);
        }
        return tempWaiveOffList;
    }
     
    private ArrayList overDueIntWaiveoffTransaction(ArrayList transferTOs, TxTransferTO txTransTO, double waiveOffAmt) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap transMap = new HashMap();
        double transAmt = waiveOffAmt;
        dataMap.put("ACCT_NUM", txTransTO.getActNum());
        List lst = (List) sqlMap.executeQueryForList("getInterestAndPenalIntActHead", dataMap);
        if (lst != null && lst.size() > 0 && transAmt > 0) {
            HashMap acHeads = (HashMap) lst.get(0);
            TransferTrans trans = new TransferTrans();
            HashMap txMap = new HashMap();
            txMap = new HashMap();
            trans = new TransferTrans();
            trans.setTransMode(CommonConstants.TX_TRANSFER);
            trans.setInitiatedBranch(_branchCode);
            trans.setLinkBatchId(CommonUtil.convertObjToStr(txTransTO.getActNum()));
            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("DEBIT_OVERDUEINT_HEAD"));
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            // Added by nithya on 01-03-2019 for KD 196 - 0014990: InterBranch Transaction Issues.
                if (!(txTransTO.getBranchId().equalsIgnoreCase(_branchCode))) {
                    txMap.put("WAIVE_OFF_INTER_BRANCH_TRANS", "WAIVE_OFF_INTER_BRANCH_TRANS");
                    txMap.put("INITIATED_BRANCH", _branchCode);
                }
            // End
            txMap.put(TransferTrans.DR_BRANCH, txTransTO.getBranchId());
            txMap.put(TransferTrans.PARTICULARS, "WaiveOff OverDueInt for " + txTransTO.getActNum());
            txMap.put("AUTHORIZEREMARKS", "OVERDUEINT_WAIVEOFF");
            txMap.put("DR_INST_TYPE", "OVERDUEINT_WAIVEOFF");
            txMap.put("DR_INSTRUMENT_2", "OVERDUEINT_WAIVEOFF");
            txMap.put("USER_ID", txTransTO.getStatusBy());
            txMap.put("INIT", _branchCode);
            txMap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(txTransTO.getActNum()));
            txMap.put("generateSingleTransId", txTransTO.getSingleTransId());
            //txMap.put("TRANS_MOD_TYPE", txTransTO.getTransModType());// Commented and changed the transmodetype to GL by nithya on 29-12-2018 for KD 363
            txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
            txMap.put("SCREEN_NAME", txTransTO.getScreenName());
            transferTOs.add(trans.getDebitTransferTO(txMap, transAmt));
            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("OVER_DUEINT_ACHD"));
            txMap.put(TransferTrans.CR_BRANCH, txTransTO.getBranchId());
            txMap.put(TransferTrans.CURRENCY, "INR");
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.PARTICULARS, "OverDueIntWaiveOff upto " + CommonUtil.convertObjToStr(properFormatDate));
            trans.setInitiatedBranch(_branchCode);
            trans.setLinkBatchId(CommonUtil.convertObjToStr(txTransTO.getActNum()));
            txMap.put("USER_ID", txTransTO.getStatusBy());
            txMap.put("generateSingleTransId", txTransTO.getSingleTransId());
            txMap.put("TRANS_MOD_TYPE", txTransTO.getTransModType());
            txMap.put("SCREEN_NAME", txTransTO.getScreenName());
            transferTOs.add(trans.getCreditTransferTO(txMap, transAmt));
            trans = null;
            acHeads = null;
            lst = null;
            txMap = null;
        }
        return transferTOs;
    }
     
     private Date getIntUpToDtForLoan(String actNum,double intAmt) throws Exception{
        Date intUpToDt = null;
        try {            
            HashMap whereMap =  new HashMap();
            whereMap.put("ACCOUNTNO", actNum);
            whereMap.put("ASON_DT",currDate.clone());
            whereMap.put("TRANS_AMOUNT",intAmt);
            List intUpToList = sqlMap.executeQueryForList("getInterestPaidUpToDate", whereMap);
            if(intUpToList != null && intUpToList.size() > 0){
                HashMap intUpToMap = (HashMap)intUpToList.get(0);
                if(intUpToMap.containsKey("INTEREST_UPTO") && intUpToMap.get("INTEREST_UPTO") != null){
                    intUpToDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(intUpToMap.get("INTEREST_UPTO")));                    
                }
            }            
        } catch (Exception e) {
            e.printStackTrace();
            sqlMap.rollbackTransaction();
            throw e;
        }
        return intUpToDt;
    } 
     
}
