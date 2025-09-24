/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareDAO.java
 *
 * Created on Sat Dec 25 13:35:00 IST 2004
 * TODO : 13 Apr 2005, Check if the transaction part on Line 145 is correct.
 Need to check same with Bala
 */
package com.see.truetransact.serverside.share;

import java.util.List;
import java.util.ArrayList;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;

import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import java.util.HashMap;
import java.util.Date;
import com.see.truetransact.transferobject.share.ShareAccInfoTO;
import com.see.truetransact.transferobject.share.ShareAcctDetailsTO;
import java.util.LinkedHashMap;
import com.see.truetransact.transferobject.share.ShareJointTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.nominee.NomineeDAO;
//added by NIKHIL for DRF transaction
import com.see.truetransact.serverside.share.DrfTransactionDAO;
import com.see.truetransact.transferobject.share.DrfTransactionTO;

import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import org.apache.log4j.Logger;
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
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;
import com.see.truetransact.transferobject.common.mobile.SMSSubscriptionTO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * Share DAO.
 *
 */
public class ShareDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private String branchId;
    private String command;
    private ShareAccInfoTO objTO;
    private ShareAcctDetailsTO shareAcctDetailsTO;
    private TransactionDAO transactionDAO = null;
    LinkedHashMap shareAcctDetTOMap;
    LinkedHashMap mapJointAccntTO;
    private ShareJointTO shareJointTO;
    private final static Logger log = Logger.getLogger(ShareDAO.class);
    final String SCREEN = "SA";
    final String SCREEN1 = "DRF";//Added By Revathi.L
    private String returnShareNo = "";
    private String returnApplNo = "";
    private TransactionTO objTransactionTO;
    private TransactionTO objTransactionTO1;
    private Date currDt = null;
    private HashMap execReturnMap = null;
//    Added by Nikhil for DRF Applicable
    HashMap returnDrfMap = new HashMap();
    private boolean isDrfApplicable = false;
    HashMap drfMap = new HashMap();
    String shareType = "";
    String generateSingleTransId="";
    private int ibrHierarchy = 0;
	private TransactionTO transTo;
    private SMSSubscriptionTO objSMSSubscriptionTO = null;
    /**
     * Creates a new instance of ShareDAO
     */
    public ShareDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("map in getData of ShareDAO : " + map);

        NomineeDAO objNomineeDAO = new NomineeDAO();
        NomineeDAO objDRFNomineeDAO = new NomineeDAO();//Added By Revathi.L
        String where;
        List list;
        HashMap returnMap = new HashMap();
        HashMap drfTransDetMap = new HashMap();
        String accountno = CommonUtil.convertObjToStr(map.get("SHARE ACCOUNT NO"));
        String applno = CommonUtil.convertObjToStr(map.get("SHARE APPLICATION NO"));
        String acno = "";
        String apno = "";
        isDrfApplicable = false;
        if (!accountno.equals(acno)) {
            where = (String) map.get("SHARE ACCOUNT NO");
        } else {
            where = (String) map.get("SHARE APPLICATION NO");
        }
        if (!applno.equals(apno) && accountno.equals(acno)) {
            list = (List) sqlMap.executeQueryForList("getShareApplInfoTO", map);
            returnMap.put("ShareAcctInfo", list);
            list = (List) sqlMap.executeQueryForList("getShareApplDetailsTO", map);
            returnMap.put("ShareAcctDetails", list);
        } else {
            list = (List) sqlMap.executeQueryForList("getShareAccInfoTO", map);
            returnMap.put("ShareAcctInfo", list);
//            Added by Nikhil for DRF Applicable
            HashMap drfApplicableMap = new HashMap();
            drfApplicableMap = (HashMap) list.get(0);
            if (drfApplicableMap.containsKey("DRF_APPLICABLE") && CommonUtil.convertObjToStr(drfApplicableMap.get("DRF_APPLICABLE")).equals("Y")
                    && CommonUtil.convertObjToStr(drfApplicableMap.get("DRF_STATUS")).equals("UNAUTHORIZED")) {
                list = (List) sqlMap.executeQueryForList("getDrfTransactionForShare", drfApplicableMap);
                returnMap.put("DrfAccountDetails", list);
                drfTransDetMap = (HashMap) list.get(0);
                isDrfApplicable = true;
            } else {
                isDrfApplicable = false;
            }
            list = (List) sqlMap.executeQueryForList("getShareAcctDetailsTO", map);
            returnMap.put("ShareAcctDetails", list);
            list = (List) sqlMap.executeQueryForList("getShareJointTO", map);
            returnMap.put("JointAcctDetails", list);
        }
        returnMap.put("AccountNomineeList", (List) objNomineeDAO.getDataList(where, SCREEN));
        returnMap.put("DRFAccountNomineeList", (List) objDRFNomineeDAO.getDataList(where, SCREEN1));//Added By Revathi.L
        if (map.containsKey("CUST_ID") && (!applno.equals(apno))) {
            list = null;
            list = (List) sqlMap.executeQueryForList("getShareAppLoanDetail", map);
            returnMap.put("ShareLoanList", list);
            if (list != null && list.size() > 0) {
            }
        } else if (map.containsKey("CUST_ID") && (!accountno.equals(acno))) {
            list = null;
            list = (List) sqlMap.executeQueryForList("getShareAccLoanDetail", map);
            returnMap.put("ShareLoanList", list);
            if (list != null && list.size() > 0) {
            }
        }
        //ADDED BY ABI
        //         HashMap getTransMap = new HashMap();
        //        currDt = ServerUtil.getCurrentDate(_branchCode);
        //        getTransMap.put("TRANS_ID", where);
        //        getTransMap.put("TRANS_DT", currDt);
        //        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        //END
        //        map.put(CommonConstants.MAP_WHERE, getTransMap);
        //map.put(CommonConstants.MAP_WHERE, where);
        //list = transactionDAO.getData(map);
        list = null;
        map.put("TRANS_ID", (where+'_'+(String) map.get("SHARE DETAIL NO")));
        map.put("TRANS_DT", currDt.clone());
        map.put("BRANCH_CODE", CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
        list = (List) sqlMap.executeQueryForList("getSelectRemitIssueTransFromTransID", map);
        if (list != null && list.size() > 0) {
            returnMap.put("TransactionTO", list);
            transTo = (TransactionTO) list.get(0);
        } else {
            transTo = new TransactionTO();
        }
        if (map.containsKey("AUTH_TRANS_DETAILS")) {
            _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
            currDt = (Date) currDt.clone();
            if (!isDrfApplicable) {
                if (!accountno.equals(acno)) {
                    if (transTo != null && transTo.getProductType() != null && transTo.getProductType().equals("TL")) {
                        getShareTransDetails(CommonUtil.convertObjToStr(map.get("SHARE ACCOUNT NO")) + "_" + CommonUtil.convertObjToStr(map.get("SHARE DETAIL NO")), transTo.getDebitAcctNo());
                    } else {
                        getTransDetails(CommonUtil.convertObjToStr(map.get("SHARE ACCOUNT NO")) + "_" + CommonUtil.convertObjToStr(map.get("SHARE DETAIL NO")));
                    }

                } else {
                    if (transTo != null && transTo.getProductType() != null && transTo.getProductType().equals("TL")) {
                        getShareTransDetails(applno + "_" + CommonUtil.convertObjToStr(map.get("SHARE DETAIL NO")), transTo.getDebitAcctNo());
                    } else {
                        getTransDetails(applno + "_" + CommonUtil.convertObjToStr(map.get("SHARE DETAIL NO")));
                    }
                }
            } else {
                    HashMap transDetMap = new HashMap();
                    transDetMap.put("BATCH_ID", CommonUtil.convertObjToStr(map.get("SHARE ACCOUNT NO")) + "_" + CommonUtil.convertObjToStr(map.get("SHARE DETAIL NO")));
                    transDetMap.put("BATCH_ID1", CommonUtil.convertObjToStr(drfTransDetMap.get("DRF_TRANS_ID")));
                    transDetMap.put("BATCH_ID2", "");
                    getMultipleTransDetails(transDetMap);
                }
                returnMap.put("AUTH_TRANS_DETAILS", execReturnMap);
        }
        HashMap checkMap = new HashMap();
        checkMap.put("PROD_TYPE", "SH");
        checkMap.put("PROD_ID", map.get("SHARE TYPE"));
        checkMap.put("ACT_NUM", map.get("SHARE ACCOUNT NO"));
        list = sqlMap.executeQueryForList("getSelectSMSSubscriptionMap", checkMap);
        if (list != null && list.size() > 0) {
            returnMap.put("SMSSubscriptionTO", list);
        }
        list = null;
        return returnMap;
    }

    public CashTransactionTO setCashTransaction(HashMap cashMap, ShareAccInfoTO shareAcctDetailsTO) {
        log.info("In setCashTransaction()");
        Date curDate = (Date) currDt.clone();
        final CashTransactionTO objCashTransactionTO = new CashTransactionTO();
        try {
            objCashTransactionTO.setAcHdId(CommonUtil.convertObjToStr(cashMap.get(CommonConstants.AC_HD_ID)));
            objCashTransactionTO.setProdType(TransactionFactory.GL);
            objCashTransactionTO.setInpAmount(CommonUtil.convertObjToDouble(cashMap.get("AMOUNT")));
            objCashTransactionTO.setAmount(CommonUtil.convertObjToDouble(cashMap.get("AMOUNT")));
            objCashTransactionTO.setTransType(CommonConstants.CREDIT);
            objCashTransactionTO.setBranchId(shareAcctDetailsTO.getBranchCode());
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
            objCashTransactionTO.setLoanHierarchy(CommonUtil.convertObjToStr(cashMap.get("HIERARCHY")));
            objCashTransactionTO.setScreenName("Share Account");
            objCashTransactionTO.setTransModType(CommonUtil.convertObjToStr(cashMap.get("TRANS_MOD_TYPE")));
            objCashTransactionTO.setSingleTransId(CommonUtil.convertObjToStr(cashMap.get("generateSingleTransId")));
            objCashTransactionTO.setIbrHierarchy(CommonUtil.convertObjToStr(ibrHierarchy++));
        } catch (Exception e) {
            log.info("Error In setInwardClearing()");
            e.printStackTrace();
        }
        return objCashTransactionTO;
    }
    
    private HashMap interestCalculationTLAD(String accountNo, String prodID){
        HashMap map=new HashMap();
        HashMap insertPenal=new HashMap();
        HashMap hash=null;
        try{
            map.put("ACT_NUM", accountNo);
            map.put("PROD_ID", prodID);
            List lst = sqlMap.executeQueryForList("IntCalculationDetail", map);
            if (lst != null && lst.size() > 0) {
                hash = (HashMap) lst.get(0);
                insertPenal.put("AS_CUSTOMER_COMES", hash.get("AS_CUSTOMER_COMES"));
                if (hash.get("AS_CUSTOMER_COMES") != null && hash.get("AS_CUSTOMER_COMES").equals("N")) {
                    hash = new HashMap();
                    return hash;
                }
                map.put("BRANCH_ID", _branchCode);
                map.put(CommonConstants.BRANCH_ID, _branchCode);
                map.putAll(hash);
                map.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
                map.put("CURR_DATE", currDt);
                TaskHeader header = new TaskHeader();
                header.setBranchID(_branchCode);
                InterestCalculationTask interestcalTask = new InterestCalculationTask(header);
                hash = interestcalTask.interestCalcTermLoanAD(map);
                if (hash == null)
                    hash = new HashMap();
                else if (hash != null && hash.size() > 0) {
                    double interest = CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                    double penal = CommonUtil.convertObjToDouble(hash.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                    hash.put("ACT_NUM", accountNo);
                    hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                    hash.put("FROM_DT", DateUtil.addDays(((Date) hash.get("FROM_DT")), 2));
                    hash.put("TO_DATE", map.get("CURR_DATE"));
                    List facilitylst = sqlMap.executeQueryForList("getPaidPrinciple", hash);
                    if (facilitylst != null && facilitylst.size() > 0) {
                        hash = (HashMap) facilitylst.get(0);
                        interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                        penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
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
                    List chargeList = sqlMap.executeQueryForList("getChargeDetails", map);
                    if (chargeList != null && chargeList.size() > 0) {
                        for (int i = 0; i < chargeList.size(); i++) {
                            HashMap chargeMap = (HashMap) chargeList.get(i);
                            double chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                            if (chargeMap.get("CHARGE_TYPE").equals("POSTAGE CHARGES") && chargeAmt > 0) {
                                insertPenal.put("POSTAGE CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("MISCELLANEOUS CHARGES") && chargeAmt > 0) {
                                insertPenal.put("MISCELLANEOUS CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("LEGAL CHARGES") && chargeAmt > 0) {
                                insertPenal.put("LEGAL CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("INSURANCE CHARGES") && chargeAmt > 0) {
                                insertPenal.put("INSURANCE CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if(chargeMap.get("CHARGE_TYPE").equals("EXECUTION DECREE CHARGES") && chargeAmt>0){
                                insertPenal.put("EXECUTION DECREE CHARGES",chargeMap.get("CHARGE_AMT"));
                            }
                            if(chargeMap.get("CHARGE_TYPE").equals("ARBITRARY CHARGES") && chargeAmt>0){
                                insertPenal.put("ARBITRARY CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.containsKey("CHARGE_TYPE") && chargeMap.get("CHARGE_TYPE") != null && chargeMap.get("CHARGE_TYPE").equals("NOTICE CHARGES") && chargeAmt > 0) {
                                insertPenal.put("NOTICE CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.containsKey("CHARGE_TYPE") && chargeMap.get("CHARGE_TYPE") != null && chargeMap.get("CHARGE_TYPE").equals("EP_COST") && chargeAmt > 0) {
                                insertPenal.put("EP_COST", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.containsKey("CHARGE_TYPE") && chargeMap.get("CHARGE_TYPE") != null && chargeMap.get("CHARGE_TYPE").equals("LEGAL CHARGES") && chargeAmt > 0) {
                                insertPenal.put("LEGAL CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.containsKey("CHARGE_TYPE") && chargeMap.get("CHARGE_TYPE") != null && chargeMap.get("CHARGE_TYPE").equals("ARC_COST") && chargeAmt > 0) {
                                insertPenal.put("ARC_COST", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.containsKey("CHARGE_TYPE") && chargeMap.get("CHARGE_TYPE") != null && chargeMap.get("CHARGE_TYPE").equals("OTHER CHARGES") && chargeAmt > 0) {
                                HashMap otherChargesMap = new HashMap();
                                otherChargesMap.put("OTHER CHARGES", chargeMap.get("CHARGE_AMT"));
                                insertPenal.put("OTHER_CHARGES", otherChargesMap);
                            }
                            chargeMap = null;
                        }
                    }
                    chargeList = null;
                }
                interestcalTask = null;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        map=null;
        hash=null;
        
        return insertPenal;
    }

    private void insertShareAcctDetails(LogDAO objLogDAO, LogTO objLogTO, HashMap map) throws Exception, Exception {
        int sizeShareAcctDet = shareAcctDetTOMap.size();
        for (int i = 0; i < sizeShareAcctDet; i++) {
            try {
                shareAcctDetailsTO = (ShareAcctDetailsTO) shareAcctDetTOMap.get(String.valueOf(i));
                String ratification = objTO.getTxtRatification();
                String yes = "Y";
                String noofshare = "";
                String sharedet = "";
                String shareno = "";
                double memfee = 0.0;
                double sharevalue = 0.0;
                double sharefee = 0.0;
                double shareapplfee = 0.0;
                double shareamount = 0.0;
                String ShareAmount = "";
                double MemFee = CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtShareMemFee()).doubleValue();
                double ShareValue = CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtShareValue()).doubleValue();
                double ShareFee = CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtTotShareFee()).doubleValue();
                double ShareApplFee = CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtShareApplFee()).doubleValue();
                double totalShare = MemFee + ShareValue + ShareFee + ShareApplFee;
                String TotalShare = CommonUtil.convertObjToStr(new Double(totalShare));
//                added by Nikhil for Subsidy purpose
                double governmentsShare = 0.0;
                boolean isSCST = false;
                boolean isDrfApplicable = false;
                double drfTransAmt = 0.0;
                HashMap drfApplicableMap = new HashMap();
//                added by nikhil for DRF Applicable
                if (map.containsKey("DRF_APPLICABLE")) {
                    drfApplicableMap = (HashMap) map.get("DRF_APPLICABLE");
                    drfTransAmt = CommonUtil.convertObjToDouble(drfApplicableMap.get("DRF_AMOUNT")).doubleValue();
                    isDrfApplicable = true;
                }

                if (map.containsKey("SC/ST")) {
                    if (map.containsKey("GOVTS_SHARE") && CommonUtil.convertObjToStr(map.get("GOVTS_SHARE")).length() > 0) {
                        isSCST = true;
                        governmentsShare = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(map.get("GOVTS_SHARE"))).doubleValue();
                    }
                }
                String sharefrom = "";
                String transType = "";

                String noshare = shareAcctDetailsTO.getTxtNoShares();
                //  if(ratification.equals(yes))

                // shareAcctDetailsTO.setTxtApplicationNo(objTO. getTxtApplicationNo());
                // else
                if (ratification.equals(yes) && CommonUtil.convertObjToStr(objTO.getShareAcctNo()).equals("")) {
                    shareAcctDetailsTO.setTxtApplicationNo(objTO.getTxtApplicationNo());
                    HashMap hmap = new HashMap();
                    hmap = (HashMap) sqlMap.executeQueryForObject("getShareAppDet", shareAcctDetailsTO.getTxtApplicationNo());
                    if (hmap != null) {
                        noofshare = CommonUtil.convertObjToStr(hmap.get("NO_OF_SHARES"));
                        memfee = CommonUtil.convertObjToDouble(hmap.get("SHARE_MEM_FEE")).doubleValue();
                        sharevalue = CommonUtil.convertObjToDouble(hmap.get("SHARE_VALUE")).doubleValue();
                        sharefee = CommonUtil.convertObjToDouble(hmap.get("SHARE_FEE")).doubleValue();
                        shareapplfee = CommonUtil.convertObjToDouble(hmap.get("SHARE_APPL_FEE")).doubleValue();
                        // double nshare= noofshare.doubleValue();
                        sharedet = CommonUtil.convertObjToStr(hmap.get("SHARE_ACCT_DET_NO"));
                        sharefrom = CommonUtil.convertObjToStr(hmap.get("SHARE_NO_FROM"));
                        shareamount = memfee + sharevalue + sharefee + shareapplfee;
                        ShareAmount = CommonUtil.convertObjToStr(new Double(shareamount));
                        HashMap hmap1 = new HashMap();
                        hmap1.put("LINK_BATCH_ID", shareAcctDetailsTO.getTxtApplicationNo() + "_" + sharedet);
                        hmap1.put("SHARE_TO_LOAN","SHARE_TO_LOAN");
                        List lst1 = sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", hmap1);
                        if (lst1.size() > 0) {
                            transType = "TRANSFER";
                        } else {
                            transType = "CASH";
                        }
                    }

                } else {
                    shareAcctDetailsTO.setShareAcctNo(objTO.getShareAcctNo());
                    HashMap hmap = new HashMap();
                    hmap = (HashMap) sqlMap.executeQueryForObject("getShareAccDet", shareAcctDetailsTO.getShareAcctNo());
                    if (hmap != null) {
                        noofshare = CommonUtil.convertObjToStr(hmap.get("NO_OF_SHARES"));
                        sharedet = CommonUtil.convertObjToStr(hmap.get("SHARE_ACCT_DET_NO"));
                        memfee = CommonUtil.convertObjToDouble(hmap.get("SHARE_MEM_FEE")).doubleValue();
                        sharevalue = CommonUtil.convertObjToDouble(hmap.get("SHARE_VALUE")).doubleValue();
                        sharefee = CommonUtil.convertObjToDouble(hmap.get("SHARE_FEE")).doubleValue();
                        shareapplfee = CommonUtil.convertObjToDouble(hmap.get("SHARE_APPL_FEE")).doubleValue();
                        sharefrom = CommonUtil.convertObjToStr(hmap.get("SHARE_NO_FROM"));
                        shareamount = memfee + sharevalue + sharefee + shareapplfee;
                        ShareAmount = CommonUtil.convertObjToStr(new Double(shareamount));
                        HashMap hmap1 = new HashMap();
                        hmap1.put("LINK_BATCH_ID", shareAcctDetailsTO.getShareAcctNo() + "_" + sharedet);
                        List lst1 = sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", hmap1);
                        if (lst1.size() > 0) {
                            transType = "TRANSFER";
                        } else {
                            transType = "CASH";
                        }
                    }
                }
                ShareAccInfoTO objShareAccInfoTO = new ShareAccInfoTO();
                Integer nshare = shareAcctDetailsTO.getNoOfShares();
                String shrfrom = shareAcctDetailsTO.getShareNoFrom();
                //--- If "New" is displayed in the "Share Acct No." text,
                //--- then generate the new Share Acct No.
                //  objShareAccInfoTO   =(ShareAccInfoTO)map.get("objShareAccInfoTO");

                if (shareAcctDetailsTO.getCommand() != null) {
                    if (!shareAcctDetailsTO.getCommand().equals("DELETE") && map != null && map.size() > 0 && map.containsKey("TransactionTO")) {
                        LinkedHashMap TransDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                        if (TransDetailsMap != null && TransDetailsMap.size() > 0 && TransDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                            LinkedHashMap allowTransDetailsTO = (LinkedHashMap) TransDetailsMap.get("NOT_DELETED_TRANS_TOs");
                            TransactionTO transTo = (TransactionTO) allowTransDetailsTO.get(String.valueOf(1));
                            if (transTo.getProductType() != null && transTo.getProductType().equals("TL")) {
                                shareAcctDetailsTO.setLoanAcctNumber(transTo.getDebitAcctNo());
                            }
                        }
                    }
                    //                    if ((shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT ))
                    //                    || ((shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)))){
                    String trans = "";

                    shareAcctDetailsTO.setShareStatus("RES_ACCEPT");
                    if (shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                        LinkedHashMap TransactionDetailsMap1 = (LinkedHashMap) map.get("TransactionTO");
                        LinkedHashMap allowedTransDetailsTO1 = (LinkedHashMap) TransactionDetailsMap1.get("NOT_DELETED_TRANS_TOs");
                        objTransactionTO1 = (TransactionTO) allowedTransDetailsTO1.get(String.valueOf(1));
                        trans = objTransactionTO1.getTransType();
                    }
                    if (shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE) && (!ShareAmount.equals(TotalShare) || !trans.equals(transType)) || shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                        if (ratification.equals(yes) && CommonUtil.convertObjToStr(objTO.getShareAcctNo()).equals("")) {

                            sqlMap.executeUpdate("updateShareAppDetailsTO", shareAcctDetailsTO);
                            sqlMap.executeUpdate("getUpdateRemitDetailsForApp", shareAcctDetailsTO.getTxtApplicationNo() + "_" + sharedet);
                            HashMap shareAcctMap = new HashMap();
                            if (!objTO.getShareAcctNo().equals(shareno)) {
                                shareAcctMap.put("LINK_BATCH_ID", objTO.getShareAcctNo() + "_" + sharedet);
                            } else {
                                shareAcctMap.put("LINK_BATCH_ID", shareAcctDetailsTO.getTxtApplicationNo() + "_" + sharedet);
                            }
                            List lst = sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", shareAcctMap);
                            TxTransferTO txTransferTO = null;
                            ArrayList transferList = null;
                            double total = 0.0;
                            double oldAmount = 0;
                            HashMap oldAmountMap = new HashMap();
                            if (lst != null && lst.size() > 0) {
                                transferList = new ArrayList();
                                for (int j = 0; j < lst.size(); j++) {

                                    txTransferTO = (TxTransferTO) lst.get(j);
                                    if (shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                                        //                                    if(CommonUtil.convertObjToStr(shareAcctDetailsTO.getShareNoFrom()).equals("ADD")){
                                        total = total;
                                        if (CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("SHARE_ACHD")
                                                || CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("SHARE_SUSPENSE_ACHD")) {
//                                            total=CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtShareValue()).doubleValue() ;
                                            total = CommonUtil.convertObjToDouble(txTransferTO.getAmount()).doubleValue();
                                        }

                                        if (CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("SHARE_FEE_ACHD")) {
//                                            total= CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtTotShareFee()).doubleValue() ;
                                            total = CommonUtil.convertObjToDouble(txTransferTO.getAmount()).doubleValue();
                                        }

                                        if (CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("MEMBERSHIP_FEE_ACHD")) {
//                                            total= CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtShareMemFee()).doubleValue() ;
                                            total = CommonUtil.convertObjToDouble(txTransferTO.getAmount()).doubleValue();
                                        }
                                        if (CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("APPLICATION_FEE_ACHD")) {
//                                            total= CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtShareApplFee()).doubleValue() ;
                                            total = CommonUtil.convertObjToDouble(txTransferTO.getAmount()).doubleValue();
                                        }
                                        if (CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("") && txTransferTO.getTransType().equals("DEBIT")) {
                                            total = CommonUtil.convertObjToDouble(txTransferTO.getAmount()).doubleValue();
                                        }

                                        oldAmount = txTransferTO.getAmount().doubleValue();
                                        txTransferTO.setInpAmount(new Double(total));
                                        txTransferTO.setAmount(new Double(total));
                                        oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                                        txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                        txTransferTO.setStatusDt(currDt);
                                    } else {
                                        //                                    txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                                        txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                        txTransferTO.setStatusDt(currDt);
                                    }
                                    txTransferTO.setSingleTransId(generateSingleTransId);
                                    
                                    txTransferTO.setTransModType("SH");
                                    transferList.add(txTransferTO);
                                    //                                    }
                                }
                                TransferTrans transferTrans = new TransferTrans();
                                transferTrans.setOldAmount(oldAmountMap);
                                transferTrans.setInitiatedBranch(_branchCode);
                                transferTrans.doDebitCredit(transferList, _branchCode, false, "DELETED");
                                transferTrans = null;
                            }
//                            else{
                            //                                LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                            //
                            //                                LinkedHashMap allowedTransDetailsTO = (LinkedHashMap)TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                            //                                if(allowedTransDetailsTO!=null && allowedTransDetailsTO.size()>0){
                            //                                    CashTransactionTO txTransferTO1 = null;
                            //                                    for (int J = 0;J < allowedTransDetailsTO.size();J++)  {
                            //
                            //                                        objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                            HashMap tempMap = new HashMap();
                            List cLst1 = null;
                            cLst1 = sqlMap.executeQueryForList("getSelectShareCashTransactionTO", CommonUtil.convertObjToStr(objTO.getTxtApplicationNo() + "_" + sharedet));
                            if (cLst1 != null && cLst1.size() > 0) {
                                //                                    if(CommonUtil.convertObjToStr(shareAcctDetailsTO.getShareNoFrom()).equals("ADD")){
                                CashTransactionTO txTransferTO1 = null;
                                LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                                if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                                    LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                    transferList = new ArrayList();
//                                        objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                                    for (int l = 0; l < cLst1.size(); l++) {
                                        txTransferTO1 = (CashTransactionTO) cLst1.get(l);
                                        if (shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                                            oldAmount = CommonUtil.convertObjToDouble(txTransferTO1.getAmount()).doubleValue();
                                            //                                                double newAmount = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                            double newAmount = 0.0;
                                            if (CommonUtil.convertObjToStr(txTransferTO1.getAuthorizeRemarks()).equals("SHARE_ACHD")
                                                    || CommonUtil.convertObjToStr(txTransferTO1.getAuthorizeRemarks()).equals("SHARE_SUSPENSE_ACHD")) {
                                                // newAmount=CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtShareValue()).doubleValue() ;
                                                newAmount = CommonUtil.convertObjToDouble(txTransferTO1.getAmount()).doubleValue();
                                            }
                                            if (CommonUtil.convertObjToStr(txTransferTO1.getAuthorizeRemarks()).equals("SHARE_FEE_ACHD")) {
                                                //newAmount= CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtTotShareFee()).doubleValue() ;
                                                newAmount = CommonUtil.convertObjToDouble(txTransferTO1.getAmount()).doubleValue();
                                            }
                                            if (CommonUtil.convertObjToStr(txTransferTO1.getAuthorizeRemarks()).equals("MEMBERSHIP_FEE_ACHD")) {
//                                                newAmount= CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtShareMemFee()).doubleValue() ;
                                                newAmount = CommonUtil.convertObjToDouble(txTransferTO1.getAmount()).doubleValue();
                                            }
                                            if (CommonUtil.convertObjToStr(txTransferTO1.getAuthorizeRemarks()).equals("APPLICATION_FEE_ACHD")) {
//                                                newAmount= CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtShareApplFee()).doubleValue() ;
                                                newAmount = CommonUtil.convertObjToDouble(txTransferTO1.getAmount()).doubleValue();
                                            }
                                            oldAmount = txTransferTO1.getAmount().doubleValue();
                                            txTransferTO1.setInpAmount(new Double(newAmount));
                                            txTransferTO1.setAmount(new Double(newAmount));
                                            txTransferTO1.setCommand("DELETE");
                                            txTransferTO1.setStatus(CommonConstants.STATUS_DELETED);
                                            txTransferTO1.setStatusDt(currDt);
                                        } else {
                                            txTransferTO1.setCommand("DELETE");
                                            txTransferTO1.setStatus(CommonConstants.STATUS_DELETED);
                                            txTransferTO1.setStatusDt(currDt);
                                        }
                                        txTransferTO1.setSingleTransId(generateSingleTransId);
                                        txTransferTO1.setTransModType("SH");
                                        transferList.add(txTransferTO1);
                                    }
                                    map.put(CommonConstants.PRODUCT_ID, TransactionFactory.GL);
                                    map.put("PRODUCTTYPE", TransactionFactory.GL);
                                    map.put("OLDAMOUNT", new Double(oldAmount));
                                    map.put("DAILYDEPOSITTRANSTO", transferList);
                                    map.put("SCREEN_NAME", "Share Account");
                                    CashTransactionDAO cashTransDAO = new CashTransactionDAO();
                                    cashTransDAO.execute(map, false);
                                }
                            }
//                            }
                        } else {
                            HashMap updateShareAcctMap = new HashMap();
                            updateShareAcctMap.put("AUTHORIZE_STATUS",CommonConstants.STATUS_AUTHORIZED);
                            updateShareAcctMap.put("AUTHORIZE_BY",shareAcctDetailsTO.getStatusBy());
                            updateShareAcctMap.put("AUTHORIZE_DT",currDt);
                            updateShareAcctMap.put("SHARE_ACCT_NO",shareAcctDetailsTO.getShareAcctNo());
                            sqlMap.executeUpdate("updateShareAcctTable", updateShareAcctMap);
                            sqlMap.executeUpdate("updateShareAcctDetailsTO", shareAcctDetailsTO);
                            sqlMap.executeUpdate("getUpdateRemitDetailsForAcc", shareAcctDetailsTO.getShareAcctNo() + "_" + sharedet);
                           
                            HashMap shareAcctMap = new HashMap();
                            shareAcctMap.put("LINK_BATCH_ID", objTO.getShareAcctNo() + "_" + sharedet);
                            List lst = null;
                            if(!transTo.getTransType().equals("CASH")){
                                shareAcctMap.put("SHARE_ACCT_NO",objTransactionTO.getDebitAcctNo());
                                lst = sqlMap.executeQueryForList("getShareAuthBatchTxTransferTOs", shareAcctMap);
                            }else{
                                lst = sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", shareAcctMap);
                            }
                            TxTransferTO txTransferTO = null;
                            ArrayList transferList = new ArrayList();
                            double total = 0.0;
                            double oldAmount = 0;
                            HashMap oldAmountMap = new HashMap();

                            if (lst != null && lst.size() > 0) {
                                for (int j = 0; j < lst.size(); j++) {
                                    txTransferTO = (TxTransferTO) lst.get(j);
                                    LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                                    if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                                        LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                        
//                                        objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
//                                        System.out.println("!!!@@@objTransactionTO:"+objTransactionTO);
                                        //                                        if(CommonUtil.convertObjToStr(shareAcctDetailsTO.getShareNoFrom()).equals("ADD")){
                                        total = total;
                                        if (shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                                            if (CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("SHARE_ACHD")
                                                    || CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("SHARE_SUSPENSE_ACHD")) {
//                                                total=CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtShareValue()).doubleValue() ;
                                                total = CommonUtil.convertObjToDouble(txTransferTO.getAmount()).doubleValue();
                                            }
                                            if (CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("SHARE_FEE_ACHD")) {
//                                                total= CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtTotShareFee()).doubleValue() ;
                                                total = CommonUtil.convertObjToDouble(txTransferTO.getAmount()).doubleValue();
                                            }
                                            if (CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("MEMBERSHIP_FEE_ACHD")) {
//                                                total= CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtShareMemFee()).doubleValue() ;
                                                total = CommonUtil.convertObjToDouble(txTransferTO.getAmount()).doubleValue();
                                            }
                                            if (CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("APPLICATION_FEE_ACHD")) {
//                                                total= CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtShareApplFee()).doubleValue() ;
                                                total = CommonUtil.convertObjToDouble(txTransferTO.getAmount()).doubleValue();
                                            }
                                            if (CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("") && txTransferTO.getTransType().equals("DEBIT")) {
                                                total = CommonUtil.convertObjToDouble(txTransferTO.getAmount()).doubleValue();
                                            }
                                            oldAmount = txTransferTO.getAmount().doubleValue();
                                            txTransferTO.setInpAmount(new Double(total));
                                            txTransferTO.setAmount(new Double(total));

                                            txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                            txTransferTO.setStatusDt(currDt);
                                            //                                    txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                                            oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                                        } else {
                                            txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                            txTransferTO.setStatusDt(currDt);
                                        }
                                        txTransferTO.setSingleTransId(generateSingleTransId);
                                        txTransferTO.setTransModType("SH");
                                        transferList.add(txTransferTO);
                                        //                                        }
                                    }
                                }

                                TransferTrans transferTrans = new TransferTrans();
                                transferTrans.setOldAmount(oldAmountMap);
                                transferTrans.setInitiatedBranch(_branchCode);
                                transferTrans.doDebitCredit(transferList, _branchCode, false, "DELETED");
                                transferTrans = null;
                            } else {
                                HashMap tempMap = new HashMap();
                                List cLst1 = sqlMap.executeQueryForList("getSelectShareCashTransactionTO", CommonUtil.convertObjToStr(objTO.getShareAcctNo() + "_" + sharedet));
                                if (cLst1 != null && cLst1.size() > 0) {
                                    //                                    if(CommonUtil.convertObjToStr(shareAcctDetailsTO.getShareNoFrom()).equals("ADD")){
                                    CashTransactionTO txTransferTO1 = null;
                                    LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                                    if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                                        LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                        transferList = new ArrayList();
//                                        objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                                        for (int l = 0; l < cLst1.size(); l++) {
                                            txTransferTO1 = (CashTransactionTO) cLst1.get(l);
                                            oldAmount = CommonUtil.convertObjToDouble(txTransferTO1.getAmount()).doubleValue();
                                            //                                                double newAmount = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                            double newAmount = 0.0;
                                            if (shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                                                if (CommonUtil.convertObjToStr(txTransferTO1.getAuthorizeRemarks()).equals("SHARE_ACHD")
                                                        || CommonUtil.convertObjToStr(txTransferTO1.getAuthorizeRemarks()).equals("SHARE_SUSPENSE_ACHD")) {
                                                    // newAmount=CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtShareValue()).doubleValue() ;
                                                    newAmount = CommonUtil.convertObjToDouble(txTransferTO1.getAmount()).doubleValue();
                                                }
                                                if (CommonUtil.convertObjToStr(txTransferTO1.getAuthorizeRemarks()).equals("SHARE_FEE_ACHD")) {
                                                    //newAmount= CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtTotShareFee()).doubleValue() ;
                                                    newAmount = CommonUtil.convertObjToDouble(txTransferTO1.getAmount()).doubleValue();
                                                }
                                                if (CommonUtil.convertObjToStr(txTransferTO1.getAuthorizeRemarks()).equals("MEMBERSHIP_FEE_ACHD")) {
//                                                    newAmount= CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtShareMemFee()).doubleValue() ;
                                                    newAmount = CommonUtil.convertObjToDouble(txTransferTO1.getAmount()).doubleValue();
                                                }
                                                if (CommonUtil.convertObjToStr(txTransferTO1.getAuthorizeRemarks()).equals("APPLICATION_FEE_ACHD")) {
//                                                    newAmount= CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtShareApplFee()).doubleValue() ;
                                                    newAmount = CommonUtil.convertObjToDouble(txTransferTO1.getAmount()).doubleValue();
                                                }
                                                oldAmount = txTransferTO1.getAmount().doubleValue();
                                                txTransferTO1.setInpAmount(new Double(newAmount));
                                                txTransferTO1.setAmount(new Double(newAmount));
                                                txTransferTO1.setCommand("DELETE");
                                                txTransferTO1.setStatus(CommonConstants.STATUS_DELETED);
                                                txTransferTO1.setStatusDt(currDt);
                                            } else {
                                                txTransferTO1.setCommand("DELETE");
                                                txTransferTO1.setStatus(CommonConstants.STATUS_DELETED);
                                                txTransferTO1.setAuthorizeBy(objTO.getCreatedBy());
                                                txTransferTO1.setAuthorizeDt(currDt);
                                                txTransferTO1.setAuthorizeStatus(CommonConstants.STATUS_REJECTED);
                                                txTransferTO1.setStatusDt(currDt);
                                            }
                                            txTransferTO1.setSingleTransId(generateSingleTransId);
                                            txTransferTO1.setTransModType("SH");
                                            transferList.add(txTransferTO1);
                                        }
                                        map.put(CommonConstants.PRODUCT_ID, TransactionFactory.GL);
                                        map.put("PRODUCTTYPE", TransactionFactory.GL);
                                        map.put("OLDAMOUNT", new Double(oldAmount));
                                        map.put("DAILYDEPOSITTRANSTO", transferList);
                                        map.put("SCREEN_NAME", "Share Account");
                                        CashTransactionDAO cashTransDAO = new CashTransactionDAO();
                                        cashTransDAO.execute(map, false);
                                    }
                                }

                            }

                        }
                    } else if (shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                        if (ratification.equals(yes)) {
                            sqlMap.executeUpdate("updateShareAppDetailsTO", shareAcctDetailsTO);
                        } else {
                            sqlMap.executeUpdate("updateShareAcctDetailsTO", shareAcctDetailsTO);
                        }
                    } else {
                        sqlMap.executeUpdate("insertShareAcctDetailsTO", shareAcctDetailsTO);
                        if (map.containsKey("serviceTaxDetailsTO")) {
                            ServiceTaxDetailsTO objserviceTaxDetailsTO = (ServiceTaxDetailsTO) map.get("serviceTaxDetailsTO");
                            insertServiceTaxDetails(objserviceTaxDetailsTO);
                        }
                    }

                    double shareAmt = CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtShareValue()).doubleValue();
                    double shareFee = CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtTotShareFee()).doubleValue();
                    double memberFee = CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtShareMemFee()).doubleValue();
                    double applnFee = CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtShareApplFee()).doubleValue();
                    double noOfShares = shareAcctDetailsTO.getNoOfShares().doubleValue();
                    double total = shareAmt + shareFee + memberFee + applnFee;
                    HashMap txMap;
                    shareType = objTO.getShareType();
                    HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getShareSuspenseAcHd", objTO.getShareType());
                    TransferTrans objTransferTrans = new TransferTrans();
                    objTransferTrans.setInitiatedBranch(_branchCode);
                    TransactionTO objTransactionTONew = null; 
                    LinkedHashMap TransactionDetailedMap = (LinkedHashMap) map.get("TransactionTO");
                    if (TransactionDetailedMap.size() > 0) {
                        if (TransactionDetailedMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                            LinkedHashMap allowedTransactionDetailsTO = (LinkedHashMap) TransactionDetailedMap.get("NOT_DELETED_TRANS_TOs");
                            if(allowedTransactionDetailsTO!=null && allowedTransactionDetailsTO.size()>0){
                                for (int J = 0; J < allowedTransactionDetailsTO.size(); J++) {
                                    objTransactionTONew = (TransactionTO) allowedTransactionDetailsTO.get(String.valueOf(1));
                                }
                            }
                        }
                    }
                    if(objTransactionTONew!=null && objTransactionTONew.getProductType()!=null && !objTransactionTONew.getProductType().equals("TL")){
                        if (ratification.equals(yes) && CommonUtil.convertObjToStr(objTO.getShareAcctNo()).equals("")) {
                            objTransferTrans.setLinkBatchId(objTO.getTxtApplicationNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
                        } else {
                            objTransferTrans.setLinkBatchId(objTO.getShareAcctNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
                        }
                    }
                    txMap = new HashMap();
                    ArrayList transferList = new ArrayList();
                    if (shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT) || ((shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) && (!ShareAmount.equals(TotalShare) || !trans.equals(transType)))) {
                        if (CommonUtil.convertObjToStr(shareAcctDetailsTO.getShareNoFrom()).equals("ADD")) {
                            LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                            if (TransactionDetailsMap.size() > 0) {
                                if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                                    LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                    for (int J = 0; J < allowedTransDetailsTO.size(); J++) {
                                        objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                                        double debitAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
//                                        added by Nikhil for DRF Applicable
                                        if (isDrfApplicable) {
                                            debitAmt = debitAmt - drfTransAmt;
                                        }
//                                        added by nikhil for subsidy//changed by jiv govt share transaction needed at resolution time 
//                                        as part of peringandoor customization....
                                        if (isSCST && (!ratification.equals(yes))) {
                                            txMap.put(TransferTrans.PARTICULARS, objTO.getShareAcctNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
                                            txMap.put(CommonConstants.USER_ID, objTO.getCreatedBy());
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_BRANCH, objTO.getBranchCode());
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("GOVT_SUBSIDY_ACHD"));
                                            txMap.put("AUTHORIZEREMARKS", "GOVT_SUBSIDY_ACHD");
                                            txMap.put("SCREEN_NAME", "Share Account");
                                            txMap.put("generateSingleTransId",generateSingleTransId);
                                            if (objTransactionTO.getProductType().equals("OA")){
                                                txMap.put("TRANS_MOD_TYPE", "OA");
                                            }else if(objTransactionTO.getProductType().equals("AB")){
                                                txMap.put("TRANS_MOD_TYPE", "AB");
                                            }else if(objTransactionTO.getProductType().equals("SA")){
                                                txMap.put("TRANS_MOD_TYPE", "SA");
                                            }else if(objTransactionTO.getProductType().equals("TL")){
                                                txMap.put("TRANS_MOD_TYPE", "TL");
                                            }else if(objTransactionTO.getProductType().equals("AD")){
                                                txMap.put("TRANS_MOD_TYPE", "AD");
                                            }else
                                                txMap.put("TRANS_MOD_TYPE", "GL");
                                             txMap.put("SCREEN_NAME", "Share Account");
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, governmentsShare));
                                            if (governmentsShare > 0.0) {
                                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SHARE_ACHD"));
                                                txMap.put("AUTHORIZEREMARKS", "SHARE_ACHD");
                                                txMap.put("HIERARCHY", "1");
                                                txMap.put("TRANS_MOD_TYPE", "SH");
                                                txMap.put("SCREEN_NAME", "Share Account");
                                                transferList.add(objTransferTrans.getCreditTransferTO(txMap, governmentsShare));
                                            }
                                            objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                        }
                                        if (objTransactionTO.getTransType().equals("TRANSFER")) {

                                            if (ratification.equals("Y") && (objTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)
                                                    || CommonUtil.convertObjToStr(objTO.getShareAcctNo()).equals(""))) {
                                                insertRatification(map);
                                            } else {
//                                                added by Nikhil for DRF applicable
                                                if (isDrfApplicable) {
                                                    HashMap drfTransMap = new HashMap();
                                                    DrfTransactionTO objDrfTO = new DrfTransactionTO();
                                                    objDrfTO = getDrfTransTO(CommonConstants.TOSTATUS_INSERT, drfTransAmt, map, drfApplicableMap);
                                                    drfTransMap.put("DrfTransactionTO", objDrfTO);
                                                    objTransactionTO.setTransAmt(CommonUtil.convertObjToDouble(String.valueOf(drfTransAmt)));
                                                    drfTransMap.put("TransactionTO", TransactionDetailsMap);
                                                    DrfTransactionDAO drfTransDao = new DrfTransactionDAO();
                                                    drfTransMap.put("BANK_CODE", map.get("BANK_CODE"));
                                                    drfTransMap.put("IP_ADDR", map.get("IP_ADDR"));
                                                    drfTransMap.put("DB_DRIVER_NAME", map.get("DB_DRIVER_NAME"));
                                                    drfTransMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                                                    drfTransMap.put("USER_ID", map.get("USER_ID"));
                                                    drfTransMap.put("COMMAND", "INSERT");
                                                    drfTransMap.put("FROM_SHARE_DAO", "FROM_SHARE_DAO");
                                                    drfTransMap.put("generateSingleTransId",generateSingleTransId);
                                                    drfTransMap.put("SCREEN_NAME", "Share Account");
                                                    returnDrfMap = (HashMap) drfTransDao.execute(drfTransMap);
                                                    drfTransMap.put("DRF_STATUS", "UNAUTHORIZED");
                                                    drfTransMap.put("SHARE_ACCT_NO", CommonUtil.convertObjToStr(objTO.getShareAcctNo()));
                                                    sqlMap.executeUpdate("updateShareDrfStatus", drfTransMap);
                                                }
                                                objTransactionTO.setTransAmt(CommonUtil.convertObjToDouble(String.valueOf(debitAmt + drfTransAmt)));
                                                txMap.put(TransferTrans.PARTICULARS, objTO.getShareAcctNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
                                                txMap.put(CommonConstants.USER_ID, objTO.getCreatedBy());
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                                HashMap interBranchCodeMap = new HashMap();
                                                interBranchCodeMap.put("ACT_NUM", objTransactionTO.getDebitAcctNo());
                                                List interBranchCodeList = sqlMap.executeQueryForList("getSelectInterBranchCode", interBranchCodeMap);
                                                if (interBranchCodeList != null && interBranchCodeList.size() > 0) {
                                                    interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                                                    txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE")));
                                                }else{
                                                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                                }                                                
                                                txMap.put(TransferTrans.CR_BRANCH, objTO.getBranchCode());//_branchCode
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                 txMap.put("SCREEN_NAME", "Share Account");
                                                //txMap.put("TRANS_MOD_TYPE", CommonConstants.SHARE);
                                                txMap.put("generateSingleTransId",generateSingleTransId);
                                                if (objTransactionTO.getProductType().equals("OA")){
                                                    txMap.put("TRANS_MOD_TYPE", "OA");
                                                }else if(objTransactionTO.getProductType().equals("AB")){
                                                    txMap.put("TRANS_MOD_TYPE", "AB");
                                                }else if(objTransactionTO.getProductType().equals("SA")){
                                                    txMap.put("TRANS_MOD_TYPE", "SA");
                                                }else if(objTransactionTO.getProductType().equals("TL")){
                                                    txMap.put("TRANS_MOD_TYPE","TL");
                                                }else if(objTransactionTO.getProductType().equals("AD")){
                                                    txMap.put("TRANS_MOD_TYPE", "AD");
                                                }else
                                                    txMap.put("TRANS_MOD_TYPE","GL");
                                                if (objTransactionTO.getProductType().equals("GL")) {
                                                    HashMap debitAcHead = (HashMap) sqlMap.executeQueryForObject("getShareSuspenseAcHdForDebitShare", objTransactionTO.getDebitAcctNo());
                                                    if(debitAcHead!=null && debitAcHead.size()>0){
                                                        txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(debitAcHead.get("DEBIT_SHARE_ACHD")));
                                                        txMap.put(TransferTrans.DR_BRANCH,_branchCode);
                                                        //Added by sreekrishnan                                                
                                                        HashMap updateMap = new HashMap();
                                                        List shareDebitList=null;
                                                        if(objTransactionTO.getDebitAcctNo()!=null){
                                                            shareDebitList = sqlMap.executeQueryForList("getShareAccountDebitDetails", objTransactionTO.getDebitAcctNo());                       
                                                            if (shareDebitList!=null && shareDebitList.size()>0) {
                                                                sqlMap.executeUpdate("insertShareAcctDetailsTO", updateShareDebitAccount(shareDebitList,objTransactionTO.getTransAmt(),shareAcctDetailsTO));                                                        
                                                            }
                                                        }
                                                        txMap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo())+"_"+(shareDebitList.size()+1));
                                                        objTransferTrans.setLinkBatchId("");
                                                        txMap.put("TRANS_MOD_TYPE", "SH");
                                                    }else{
                                                        txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                                        txMap.put(TransferTrans.DR_BRANCH,_branchCode);
                                                    }
                                                } else {
                                                    txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                                    txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                                }
                                                txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));                                                
                                                txMap.put("SCREEN_NAME", "Share Account");
                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, debitAmt));
                                                //                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, shareAmt));
                                                //                                            objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                                if (shareAmt > 0.0) {
//                                                    changed for subsidy by Nikhil
                                                    if (isSCST) {
                                                        if (governmentsShare > 0.0) {
                                                            shareAmt = shareAmt - governmentsShare;
                                                        }
                                                    }
                                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SHARE_ACHD"));
                                                    txMap.put("AUTHORIZEREMARKS", "SHARE_ACHD");
                                                    //                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, shareAmt));
                                                    txMap.put("HIERARCHY", "2");
                                                    txMap.put("generateSingleTransId",generateSingleTransId);
                                                    txMap.put("TRANS_MOD_TYPE", "SH");
                                                    txMap.put("LINK_BATCH_ID",objTO.getShareAcctNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
                                                    txMap.put("SCREEN_NAME", "Share Account");
                                                    if(!(objTO.getBranchCode().equalsIgnoreCase(_branchCode))){// Added by nithya for 0009241: interbranch related issues (different date issue) [ share ]
                                                      txMap.put("SHARE_INTER_BRANCH_TRANSACTION","SHARE_INTER_BRANCH_TRANSACTION"); 
                                                      txMap.put("INITIATED_BRANCH",_branchCode);
                                                    }                                                    
                                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, shareAmt));                                                   
                                                    
                                                    //                                                objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                                }
                                                if (shareFee > 0.0) {
                                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SHARE_FEE_ACHD"));
                                                    txMap.put("AUTHORIZEREMARKS", "SHARE_FEE_ACHD");
                                                    //                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, shareFee));
                                                    txMap.put("HIERARCHY", "3");
                                                    txMap.put("generateSingleTransId",generateSingleTransId);
                                                    txMap.put("TRANS_MOD_TYPE", "SH");
                                                    txMap.put("LINK_BATCH_ID",objTO.getShareAcctNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
                                                    txMap.put("SCREEN_NAME", "Share Account");
                                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, shareFee));
                                                    //                                                objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                                }
                                                if (memberFee > 0.0) {
                                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("MEMBERSHIP_FEE_ACHD"));
                                                    txMap.put("AUTHORIZEREMARKS", "MEMBERSHIP_FEE_ACHD");
                                                    //                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, memberFee));
                                                    txMap.put("HIERARCHY", "4");
                                                    txMap.put("generateSingleTransId",generateSingleTransId);
                                                    txMap.put("TRANS_MOD_TYPE", "SH");
                                                    txMap.put("LINK_BATCH_ID",objTO.getShareAcctNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
                                                    txMap.put("SCREEN_NAME", "Share Account");
                                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, memberFee));
                                                    //                                                objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                                }
                                                if (applnFee > 0.0) {
                                                    txMap.put("AUTHORIZEREMARKS", "APPLICATION_FEE_ACHD");
                                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("APPLICATION_FEE_ACHD"));
                                                    //                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, applnFee));
                                                    txMap.put("HIERARCHY", "5");
                                                    txMap.put("generateSingleTransId",generateSingleTransId);
                                                    txMap.put("TRANS_MOD_TYPE", "SH");
                                                    txMap.put("LINK_BATCH_ID",objTO.getShareAcctNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
                                                    txMap.put("SCREEN_NAME", "Share Account");
                                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, applnFee));
                                                    //                                                objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                                }
												//added by chithra for service Tax
                                                if (map.containsKey("serviceTaxDetails")) {// Changing by nithya for GST
                                                    HashMap servicetax = (HashMap) map.get("serviceTaxDetails");
                                                    double swachhCess = 0.0;
                                                    double krishikalyanCess = 0.0;
                                                    double serTaxAmt = 0.0;
                                                    double normalServiceTax =0.0;
                                                    if (servicetax.containsKey("TOT_TAX_AMT") && servicetax.get("TOT_TAX_AMT") != null) {
                                                        serTaxAmt = CommonUtil.convertObjToDouble(servicetax.get("TOT_TAX_AMT"));
                                                    }
                                                    if (servicetax.containsKey(ServiceTaxCalculation.SWACHH_CESS) && servicetax.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                                        swachhCess = CommonUtil.convertObjToDouble(servicetax.get(ServiceTaxCalculation.SWACHH_CESS));
                                                    }
                                                    if (servicetax.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && servicetax.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                                        krishikalyanCess = CommonUtil.convertObjToDouble(servicetax.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                                                    }
                                                    if (servicetax.containsKey(ServiceTaxCalculation.SERVICE_TAX) && servicetax.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                                        normalServiceTax = CommonUtil.convertObjToDouble(servicetax.get(ServiceTaxCalculation.SERVICE_TAX));
                                                    }
                                                   // serTaxAmt -= (swachhCess + krishikalyanCess);
                                                    if (swachhCess > 0) {
                                                        txMap.put("AUTHORIZEREMARKS", "SERVICE_TAX_ACHD");
                                                        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(servicetax.get("SWACHH_HEAD_ID")));
                                                        txMap.put("HIERARCHY", "6");
                                                        txMap.put("generateSingleTransId", generateSingleTransId);
                                                        txMap.put("TRANS_MOD_TYPE", "SH");
                                                        txMap.put("LINK_BATCH_ID", objTO.getShareAcctNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
                                                        txMap.put("SCREEN_NAME", "Share Account");
                                                        txMap.put(TransferTrans.PARTICULARS, "CGST");
                                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, swachhCess));
                                                       // serTaxAmt -= swachhCess;
                                                    }
                                                    if (krishikalyanCess > 0) {
                                                        txMap.put("AUTHORIZEREMARKS", "SERVICE_TAX_ACHD");
                                                        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(servicetax.get("KRISHIKALYAN_HEAD_ID")));
                                                        txMap.put("HIERARCHY", "6");
                                                        txMap.put("generateSingleTransId", generateSingleTransId);
                                                        txMap.put("TRANS_MOD_TYPE", "SH");
                                                        txMap.put("LINK_BATCH_ID", objTO.getShareAcctNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
                                                        txMap.put("SCREEN_NAME", "Share Account");
                                                        txMap.put(TransferTrans.PARTICULARS, "SGST");
                                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, krishikalyanCess));
                                                       // serTaxAmt -= krishikalyanCess;
                                                    }
                                                    if (normalServiceTax > 0) {
                                                        txMap.put("AUTHORIZEREMARKS", "SERVICE_TAX_ACHD");
                                                        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(servicetax.get("TAX_HEAD_ID")));
                                                        txMap.put("HIERARCHY", "6");
                                                        txMap.put("generateSingleTransId", generateSingleTransId);
                                                        txMap.put("TRANS_MOD_TYPE", "SH");
                                                        txMap.put("LINK_BATCH_ID", objTO.getShareAcctNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
                                                        txMap.put("SCREEN_NAME", "Share Account");
                                                        txMap.put(TransferTrans.PARTICULARS, "SERVICE TAX");
                                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, normalServiceTax));
                                                    }
                                                }
                                                objTransferTrans.doDebitCredit(transferList, _branchCode, false);                                                
                                            }
                                        } else {
                                            if (ratification.equals(yes) && (objTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)
                                                    || CommonUtil.convertObjToStr(objTO.getShareAcctNo()).equals(""))) {
                                                insertCashTransaction(map,generateSingleTransId);
                                            } else {
                                                if (isDrfApplicable) {
                                                    HashMap drfTransMap = new HashMap();
                                                    DrfTransactionTO objDrfTO = new DrfTransactionTO();
                                                    objDrfTO = getDrfTransTO(CommonConstants.TOSTATUS_INSERT, drfTransAmt, map, drfApplicableMap);
                                                    drfTransMap.put("DrfTransactionTO", objDrfTO);
                                                    objTransactionTO.setTransAmt(CommonUtil.convertObjToDouble(String.valueOf(drfTransAmt)));
                                                    drfTransMap.put("TransactionTO", TransactionDetailsMap);
                                                    DrfTransactionDAO drfTransDao = new DrfTransactionDAO();
                                                    drfTransMap.put("BANK_CODE", map.get("BANK_CODE"));
                                                    drfTransMap.put("IP_ADDR", map.get("IP_ADDR"));
                                                    drfTransMap.put("DB_DRIVER_NAME", map.get("DB_DRIVER_NAME"));
                                                    drfTransMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                                                    drfTransMap.put("USER_ID", map.get("USER_ID"));
                                                    drfTransMap.put("COMMAND", "INSERT");
                                                    drfTransMap.put("FROM_SHARE_DAO", "FROM_SHARE_DAO");
                                                    drfTransMap.put("SHARE_ACCT_NUM", objTO.getShareAcctNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
                                                    drfTransMap.put("generateSingleTransId",generateSingleTransId);
                                                    returnDrfMap = (HashMap) drfTransDao.execute(drfTransMap);
                                                    drfTransMap.put("DRF_STATUS", "UNAUTHORIZED");
                                                    drfTransMap.put("SHARE_ACCT_NO", CommonUtil.convertObjToStr(objTO.getShareAcctNo()));
                                                    sqlMap.executeUpdate("updateShareDrfStatus", drfTransMap);
                                                }
                                                double transAmt;
                                                TransactionTO transTO = new TransactionTO();
                                                ArrayList cashList = new ArrayList();
                                                objTransactionTO.setTransAmt(CommonUtil.convertObjToDouble(String.valueOf(debitAmt + drfTransAmt)));
                                                if (CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue() > 0) {
                                                    txMap = new HashMap();
                                                    txMap.put(CommonConstants.BRANCH_ID, _branchCode);
                                                    txMap.put(CommonConstants.PRODUCT_ID, TransactionFactory.GL);
                                                    txMap.put(CommonConstants.USER_ID, objTO.getCreatedBy());
                                                    if (shareAmt > 0.0) {
                                                        if (isSCST) {
                                                            if (governmentsShare > 0.0) {
                                                                shareAmt = shareAmt - governmentsShare;
                                                            }
                                                        }
                                                        txMap.put(CommonConstants.AC_HD_ID, (String) acHeads.get("SHARE_ACHD"));
                                                        txMap.put("AUTHORIZEREMARKS", "SHARE_ACHD");
                                                        txMap.put("AMOUNT", new Double(shareAmt));
                                                        txMap.put("LINK_BATCH_ID", objTransferTrans.getLinkBatchId());
                                                        txMap.put("HIERARCHY", "2");
                                                        txMap.put("TRANS_MOD_TYPE", "SH");
                                                        txMap.put("generateSingleTransId",generateSingleTransId);
                                                        cashList.add(setCashTransaction(txMap, objTO));
                                                    }
                                                    if (shareFee > 0.0) {
                                                        txMap.put(CommonConstants.AC_HD_ID, (String) acHeads.get("SHARE_FEE_ACHD"));
                                                        txMap.put("AUTHORIZEREMARKS", "SHARE_FEE_ACHD");
                                                        txMap.put("AMOUNT", new Double(shareFee));
                                                        txMap.put("LINK_BATCH_ID", objTransferTrans.getLinkBatchId());
                                                        txMap.put("HIERARCHY", "3");
                                                        txMap.put("TRANS_MOD_TYPE", "SH");
                                                        txMap.put("generateSingleTransId",generateSingleTransId);
                                                        cashList.add(setCashTransaction(txMap, objTO));
                                                    }
                                                    if (memberFee > 0.0) {
                                                        txMap.put(CommonConstants.AC_HD_ID, (String) acHeads.get("MEMBERSHIP_FEE_ACHD"));
                                                        txMap.put("AUTHORIZEREMARKS", "MEMBERSHIP_FEE_ACHD");
                                                        txMap.put("AMOUNT", new Double(memberFee));
                                                        txMap.put("LINK_BATCH_ID", objTransferTrans.getLinkBatchId());
                                                        txMap.put("HIERARCHY", "4");
                                                        txMap.put("TRANS_MOD_TYPE", "SH");
                                                        txMap.put("generateSingleTransId",generateSingleTransId);
                                                        cashList.add(setCashTransaction(txMap, objTO));
                                                    }

                                                    if (applnFee > 0.0) {
                                                        txMap.put(CommonConstants.AC_HD_ID, (String) acHeads.get("APPLICATION_FEE_ACHD"));
                                                        txMap.put("AUTHORIZEREMARKS", "APPLICATION_FEE_ACHD");
                                                        txMap.put("AMOUNT", new Double(applnFee));
                                                        txMap.put("LINK_BATCH_ID", objTransferTrans.getLinkBatchId());
                                                        txMap.put("HIERARCHY", "5");
                                                        txMap.put("TRANS_MOD_TYPE", "SH");
                                                        txMap.put("generateSingleTransId",generateSingleTransId);
                                                        cashList.add(setCashTransaction(txMap, objTO));
                                                    }
													//added by chithra for service Tax
                                                    if (map.containsKey("serviceTaxDetails")) {// Changing block by nithya for GST changes
                                                        HashMap servicetax = (HashMap) map.get("serviceTaxDetails");
                                                        double swachhCess = 0.0;
                                                        double krishikalyanCess = 0.0;
                                                        double serTaxAmt = 0.0;
                                                        double normalServiceTax = 0.0;
                                                        if (servicetax.containsKey("TOT_TAX_AMT") && servicetax.get("TOT_TAX_AMT") != null) {
                                                            serTaxAmt = CommonUtil.convertObjToDouble(servicetax.get("TOT_TAX_AMT"));
                                                        }
                                                        if (servicetax.containsKey(ServiceTaxCalculation.SWACHH_CESS) && servicetax.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                                            swachhCess = CommonUtil.convertObjToDouble(servicetax.get(ServiceTaxCalculation.SWACHH_CESS));
                                                        }
                                                        if (servicetax.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && servicetax.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                                            krishikalyanCess = CommonUtil.convertObjToDouble(servicetax.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                                                        }
                                                        if (servicetax.containsKey(ServiceTaxCalculation.SERVICE_TAX) && servicetax.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                                            normalServiceTax = CommonUtil.convertObjToDouble(servicetax.get(ServiceTaxCalculation.SERVICE_TAX));
                                                        }
                                                        //serTaxAmt -= (swachhCess + krishikalyanCess);
                                                        if (swachhCess > 0) {
                                                            txMap.put(CommonConstants.AC_HD_ID, CommonUtil.convertObjToStr(servicetax.get("SWACHH_HEAD_ID")));
                                                            txMap.put("AUTHORIZEREMARKS", "SERVICE_TAX_ACHD");
                                                            txMap.put("AMOUNT", swachhCess);
                                                            txMap.put("LINK_BATCH_ID", objTransferTrans.getLinkBatchId());
                                                            txMap.put("HIERARCHY", "5");
                                                            txMap.put("TRANS_MOD_TYPE", "SH");
                                                            txMap.put("generateSingleTransId", generateSingleTransId);
                                                            cashList.add(setCashTransaction(txMap, objTO));
                                                            //serTaxAmt -= swachhCess;
                                                        }
                                                        if (krishikalyanCess > 0) {
                                                            txMap.put(CommonConstants.AC_HD_ID, CommonUtil.convertObjToStr(servicetax.get("KRISHIKALYAN_HEAD_ID")));
                                                            txMap.put("AUTHORIZEREMARKS", "SERVICE_TAX_ACHD");
                                                            txMap.put("AMOUNT", krishikalyanCess);
                                                            txMap.put("LINK_BATCH_ID", objTransferTrans.getLinkBatchId());
                                                            txMap.put("HIERARCHY", "5");
                                                            txMap.put("TRANS_MOD_TYPE", "SH");
                                                            txMap.put("generateSingleTransId", generateSingleTransId);
                                                            cashList.add(setCashTransaction(txMap, objTO));
                                                           // serTaxAmt -= krishikalyanCess;
                                                        }
                                                        if (normalServiceTax > 0) {
                                                            txMap.put(CommonConstants.AC_HD_ID, CommonUtil.convertObjToStr(servicetax.get("TAX_HEAD_ID")));
                                                            txMap.put("AUTHORIZEREMARKS", "SERVICE_TAX_ACHD");
                                                            txMap.put("AMOUNT", normalServiceTax);
                                                            txMap.put("LINK_BATCH_ID", objTransferTrans.getLinkBatchId());
                                                            txMap.put("HIERARCHY", "5");
                                                            txMap.put("TRANS_MOD_TYPE", "SH");
                                                            txMap.put("generateSingleTransId", generateSingleTransId);
                                                            cashList.add(setCashTransaction(txMap, objTO));
                                                        }                                                                                          
                                                    }
                                                    HashMap tranMap = new HashMap();
                                                    tranMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                                                    tranMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                                    tranMap.put(CommonConstants.IP_ADDR, CommonUtil.convertObjToStr(map.get("IP_ADDR")));
                                                    tranMap.put(CommonConstants.MODULE, CommonUtil.convertObjToStr(map.get("MODULE")));
                                                    tranMap.put(CommonConstants.SCREEN, CommonUtil.convertObjToStr(map.get("SCREEN")));
                                                    tranMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                                                    tranMap.put("DAILYDEPOSITTRANSTO", cashList);
                                                    tranMap.put("SCREEN_NAME", "Share Account");
                                                    CashTransactionDAO cashDao;
                                                    cashDao = new CashTransactionDAO();
                                                    tranMap = cashDao.execute(tranMap, false);
                                                    cashDao = null;
                                                    tranMap = null;
                                                }
                                            }
                                        }
                                        if (ratification.equals("Y") && CommonUtil.convertObjToStr(objTO.getShareAcctNo()).equals("")) {
                                            if (CommonUtil.convertObjToStr(objTO.getShareAcctNo()).equals("")) {
                                                objTransactionTO.setBatchId(objTO.getTxtApplicationNo());
                                                objTransactionTO.setTransId(shareAcctDetailsTO.getTxtApplicationNo()+"_"+shareAcctDetailsTO.getShareAcctDetNo());
                                                //objTransferTrans.setLinkBatchId(objTO.getTxtApplicationNo()+"_"+shareAcctDetailsTO.getShareAcctDetNo()); //Changed by Rajesh
                                                objTransferTrans.setLinkBatchId(objTO.getTxtApplicationNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
                                            }
                                        } else {
                                            objTransactionTO.setBatchId(objTO.getShareAcctNo());
                                            objTransactionTO.setTransId(shareAcctDetailsTO.getShareAcctNo()+"_"+shareAcctDetailsTO.getShareAcctDetNo());
                                        }
                                        objTransactionTO.setBatchDt(currDt);                                        
                                        objTransactionTO.setStatus("CREATED");
                                        objTransactionTO.setBranchId(_branchCode);
                                        sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);
                                    }
                                }
                            }
                            applnFee = 0.0;
                            memberFee = 0.0;
                            shareFee = 0.0;
                            shareAmt = 0.0;
                            objTransferTrans = null;
                            transferList = null;
                            txMap = null;
//                            added by nikhil for subsidy
                            isSCST = false;
                            governmentsShare = 0.0;

                            //added by Nikhil for DRF applicable
                            if (i == 0) {
                                System.out.println("@#$@#$@#$@#for DRF" + map);
                            }
                            // Code End
                        } else {
                            LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                            //                            System.out.println("TransactionDetailsMap---->"+TransactionDetailsMap);
                            if (TransactionDetailsMap.size() > 0) {
                                if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                                    LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                    for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                        objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                        //                                   System.out.println("objTransactionTO---->"+objTransactionTO);
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("SHARE_ACHD"));
                                        txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                        //                                        txMap.put(TransferTrans.CR_ACT_NUM,CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
                                        txMap.put(TransferTrans.PARTICULARS, objTO.getShareAcctNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
                                        txMap.put(TransferTrans.CR_BRANCH, objTO.getBranchCode());//_branchCode
                                        txMap.put("USER_ID", CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        txMap.put("NARRATION", CommonUtil.convertObjToStr(objTransactionTO.getParticulars()));
                                        if (objTransactionTO.getProductType().equals(TransactionFactory.GL)) {
                                            //txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo())); // Commented by nithya on 14-02-2017
                                           // txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SHARE_ACHD"));// Added by nithya on 14-02-2017 for 5822
                                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo())); // added by nithya on 11-06-2019 for KD 530 - share transfer (withdrawal ) to gl account head issue
                                        } else {
                                            txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        }
                                        shareAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                        if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                            txMap.put("HIERARCHY", "1");
                                            txMap.put("generateSingleTransId",generateSingleTransId);
                                            txMap.put("TRANS_MOD_TYPE", "SH");
                                            txMap.put("LINK_BATCH_ID",objTO.getShareAcctNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
                                            txMap.put("SCREEN_NAME", "Share Account");
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, shareAmt));
                                            txMap.put("HIERARCHY", "2");
                                            if (objTransactionTO.getProductType().equals("OA")){
                                                    txMap.put("TRANS_MOD_TYPE", "OA");
                                                }else if(objTransactionTO.getProductType().equals("AB")){
                                                    txMap.put("TRANS_MOD_TYPE", "AB");
                                                }else if(objTransactionTO.getProductType().equals("SA")){
                                                    txMap.put("TRANS_MOD_TYPE","SA");
                                                }else if(objTransactionTO.getProductType().equals("TL")){
                                                    txMap.put("TRANS_MOD_TYPE", "TL");
                                                }else if(objTransactionTO.getProductType().equals("AD")){
                                                    txMap.put("TRANS_MOD_TYPE", "AD");
                                                }else
                                                    txMap.put("TRANS_MOD_TYPE","GL");
                                            txMap.put("generateSingleTransId",generateSingleTransId);
                                            txMap.put("SCREEN_NAME", "Share Account");
//                                            txMap.put("LINK_BATCH_ID",objTransactionTO.getDebitAcctNo()); // commented by shihad
                                            txMap.put(TransferTrans.PARTICULARS, objTransactionTO.getDebitAcctNo());  // Added by nithya on 23-03-2017
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, shareAmt)); 
                                            txMap.put("generateSingleTransId",generateSingleTransId);
                                            if (objTransactionTO.getProductType().equals("TL")) {
                                                HashMap loanMap = new HashMap();
                                                HashMap where = new HashMap();
                                                where.put("PROD_ID", CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                                loanMap = interestCalculationTLAD(CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()),
                                                        CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                                double service_taxAmt = 0;
                                                HashMap serviceTaxMap = new HashMap();
                                                if (map.containsKey("serviceTaxDetails") && map.get("serviceTaxDetails") != null) {
                                                    serviceTaxMap = (HashMap) map.get("serviceTaxDetails");
                                                    if (serviceTaxMap.containsKey("TOT_TAX_AMT") && serviceTaxMap.get("TOT_TAX_AMT") != null) {
//                                                        service_taxAmt = CommonUtil.convertObjToDouble(serviceTaxMap.get("TOT_TAX_AMT"));
//                                                        loanMap.put("TOT_SER_TAX_AMT",CommonUtil.convertObjToDouble(service_taxAmt));
//                                                        loanMap.put("SER_TAX_HEAD",CommonUtil.convertObjToStr(serviceTaxMap.get("TAX_HEAD_ID"))); 
                                                          loanMap.put("TOT_SER_TAX_AMT", serviceTaxMap.get("TOT_TAX_AMT"));
                                                          loanMap.put("SER_TAX_HEAD", serviceTaxMap.get("TAX_HEAD_ID"));
                                                          loanMap.put("SER_TAX_MAP", serviceTaxMap);
                                                    }                                                   
                                                }
                                                List waiveList = sqlMap.executeQueryForList("getwaiveoffDetails", where);
                                                if (waiveList != null && waiveList.size() > 0) {
                                                    HashMap waiveMap = (HashMap) waiveList.get(0);
                                                    loanMap.put("PENAL_WAIVE_OFF", waiveMap.get("PENAL_WAIVER"));
                                                }
                                                objTransferTrans.setAllAmountMap(loanMap);
                                            }
                                            transferList.add("SHARE_SCREEN");
                                            objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                        } else {
                                            double transAmt;
                                            TransactionTO transTO = new TransactionTO();
                                            ArrayList cashList = new ArrayList();
                                            if (CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue() > 0) {
                                                /*
                                                 * transactionDAO.setTransType(CommonConstants.CREDIT);
                                                 * transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                                 * txMap.put(TransferTrans.DR_AC_HD,
                                                 * (String)
                                                 * acHeads.get("SHARE_ACHD"));
                                                 * // credit to Exchange Charge
                                                 * account head...... transAmt =
                                                 * CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                                 * txMap.put(TransferTrans.PARTICULARS,
                                                 * objTransactionTO.getProductId());
                                                 * txMap.put(TransferTrans.DR_PROD_TYPE,
                                                 * "GL");
                                                 * txMap.put("PARTICULARS",
                                                 * objTransferTrans.getLinkBatchId());
                                                 * txMap.put("USER_ID",
                                                 * CommonUtil.convertObjToStr(map.get("USER_ID")));
                                                 * txMap.put("HIERARCHY", "1");
                                                 * transactionDAO.addTransferDebit(txMap,
                                                 * transAmt);
                                                 * transactionDAO.deleteTxList();
                                                 * transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                                 *
                                                 * transTO.setTransType("CASH");
                                                 * transTO.setBatchId(objTransferTrans.getLinkBatchId());
                                                 * transTO.setTransAmt(new
                                                 * Double(transAmt)); //
                                                 * transTO.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                                 * cashList.add(transTO);
                                                 * System.out.println("cashList---------------->"
                                                 * + cashList); HashMap tranMap
                                                 * = new HashMap();
                                                 * tranMap.put(CommonConstants.BRANCH_ID,
                                                 * CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                                                 * tranMap.put("USER_ID",
                                                 * CommonUtil.convertObjToStr(map.get("USER_ID")));
                                                 * tranMap.put("IP_ADDR",
                                                 * CommonUtil.convertObjToStr(map.get("IP_ADDR")));
                                                 * tranMap.put("MODULE",
                                                 * CommonUtil.convertObjToStr(map.get("MODULE")));
                                                 * tranMap.put("SCREEN",
                                                 * CommonUtil.convertObjToStr(map.get("SCREEN")));
                                                 * tranMap.put("MODE",
                                                 * CommonConstants.TOSTATUS_INSERT);
                                                 * transactionDAO.execute(tranMap);
                                                 * transactionDAO.addCashList(cashList);
                                                 * transactionDAO.doTransfer();
                                                transactionDAO.setCommandMode(command);
                                                 */
                                                transAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                                txMap.put(CommonConstants.AC_HD_ID, (String) acHeads.get("SHARE_ACHD"));
                                                txMap.put("AUTHORIZEREMARKS", acHeads.get("SHARE_ACHD"));
                                                txMap.put("AMOUNT", new Double(transAmt));
                                                txMap.put("LINK_BATCH_ID", objTransferTrans.getLinkBatchId());
                                                txMap.put("USER_ID", CommonUtil.convertObjToStr(map.get("USER_ID")));
                                                txMap.put("HIERARCHY", "1");
                                                txMap.put("generateSingleTransId",generateSingleTransId);
                                                txMap.put("TRANS_MOD_TYPE", "SH");
                                                cashList.add(setCashTransactionDebit(txMap, objTO));
//                                                transTO.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                                HashMap tranMap = new HashMap();
                                                tranMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                                                tranMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                                tranMap.put(CommonConstants.IP_ADDR, CommonUtil.convertObjToStr(map.get("IP_ADDR")));
                                                tranMap.put(CommonConstants.MODULE, CommonUtil.convertObjToStr(map.get("MODULE")));
                                                tranMap.put(CommonConstants.SCREEN, CommonUtil.convertObjToStr(map.get("SCREEN")));
                                                tranMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                                                tranMap.put("DAILYDEPOSITTRANSTO", cashList);
                                                tranMap.put("SCREEN_NAME", "Share Account");
                                                CashTransactionDAO cashDao;
                                                cashDao = new CashTransactionDAO();
                                                tranMap = cashDao.execute(tranMap, false);
                                                cashDao = null;
                                                tranMap = null;
                                            }
                                        }
                                        objTransactionTO.setBatchId(objTO.getShareAcctNo());
                                        objTransactionTO.setBatchDt(currDt);
                                        //modified by risad 06/mar/2018 for proper authorization
                                        if (ratification.equals("Y") && CommonUtil.convertObjToStr(objTO.getShareAcctNo()).equals("")) {// Added by nithya on 19-12-2019 for KD-1061
                                            objTransactionTO.setTransId(objTO.getTxtApplicationNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
                                        }else{
                                            objTransactionTO.setTransId(objTO.getShareAcctNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
                                        }
                                        objTransactionTO.setStatus("CREATED");
                                        objTransactionTO.setBranchId(_branchCode);
                                        sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);

                                    }

                                }
                            }
                        }
                        if (!isDrfApplicable) {
                            if (ratification.equals("Y") && CommonUtil.convertObjToStr(objTO.getShareAcctNo()).equals("")) {
                                if(objTransactionTO.getProductType()!=null && objTransactionTO.getProductType().equals("TL")){
                                    getShareTransDetails(objTO.getTxtApplicationNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo(),objTransactionTO.getDebitAcctNo());
                                }else{
                                    getTransDetails(objTO.getTxtApplicationNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
                                }
                            } else {
                                if(objTransactionTO.getProductType()!=null && objTransactionTO.getProductType().equals("TL")){
                                    getShareTransDetails(objTO.getShareAcctNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo(),objTransactionTO.getDebitAcctNo());
                                }else{
                                    getTransDetails(objTO.getShareAcctNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
                                }
                            }
                        } else {
                            
                            HashMap transDetMap = new HashMap();
                            transDetMap.put("BATCH_ID", objTO.getShareAcctNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
                            transDetMap.put("BATCH_ID1", CommonUtil.convertObjToStr(returnDrfMap.get("DRF_LINKED_BATCH")));
                            transDetMap.put("BATCH_ID2", CommonUtil.convertObjToStr(objTO.getTxtApplicationNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo()));
                            getMultipleTransDetails(transDetMap);
                        }
                    }
                    //                    if (shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE ) || shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                    //                        //
                    //                        //                  /*  else {
                    //                        //                        if(ratification.equals(yes)) {
                    //                        //                            updateShareAppData(map);
                    //                        //                        }
                    //                        //                    else {
                    //                        HashMap shareAcctNoMap = new HashMap();
                    //                        shareAcctNoMap.put("SHARE ACCOUNT NO",objTO.getShareAcctNo());
                    //                        shareAcctNoMap.put("SHARE ACCOUNT DETAIL NO",shareAcctDetailsTO.getShareAcctDetNo());
                    //                        System.out.println("shareAcctNoMap     :"+shareAcctNoMap);
                    //                        //                        shareAcctNoMap = (HashMap)sqlMap.executeQueryForObject("transferResolvedShare", shareAcctNoMap);
                    //                        //                        System.out.println("shareAcctNoMap     :"+shareAcctNoMap);
                    //                        //                        int oldNoOfShares = (shareAcctNoMap == null && shareAcctNoMap.size()==0) ? 0 : CommonUtil.convertObjToInt(shareAcctNoMap.get("NO_OF_SHARES"));
                    //                        //                        sqlMap.executeUpdate("updateShareAcctDetailsTO", shareAcctDetailsTO);
                    //                        //                        double shareAmt = CommonUtil.convertObjToDouble(objTO.getShareAmount()).doubleValue() ;
                    //                        //                        double shareFee = CommonUtil.convertObjToDouble(objTO.getShareFee()).doubleValue() ;
                    //                        //                        double memberFee = CommonUtil.convertObjToDouble(objTO.getMemFee()).doubleValue() ;
                    //                        //                        double applnFee = CommonUtil.convertObjToDouble(objTO.getApplFee()).doubleValue() ;
                    //                        //                        double noOfShares = shareAcctDetailsTO.getNoOfShares().doubleValue();
                    //                        //                        double total = (shareAmt*noOfShares) + (shareFee*noOfShares) + memberFee + applnFee ;
                    //                        System.out.println("no of shares are"+noOfShares);
                    //                        //                        if (oldNoOfShares!=noOfShares ||  shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                    //                        shareAcctNoMap = new HashMap();
                    //                        //                        if(ratification.equals(yes) && objTO.getShareAcctNo()==null ){
                    //                        //                            shareAcctNoMap.put("LINK_BATCH_ID",objTO.getTxtApplicationNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
                    //                        //                        }
                    //                        //                        else{
                    //                        shareAcctNoMap.put("LINK_BATCH_ID",objTO.getShareAcctNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
                    //                        //                        }
                    //                        List lst = sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", shareAcctNoMap);
                    //                        TxTransferTO txTransferTO = null;
                    //                        double oldAmount = 0;
                    //                        HashMap oldAmountMap = new HashMap();
                    //                        transferList = new ArrayList();
                    //                        if (lst!=null && lst.size()>0) {
                    //                            for (int j=0; j<lst.size(); j++) {
                    //                                txTransferTO = (TxTransferTO) lst.get(j);
                    //                                if(CommonUtil.convertObjToStr(shareAcctDetailsTO.getShareNoFrom()).equals("ADD")){
                    //                                    total = total;
                    //                                    if(CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("SHARE_ACHD") ||
                    //                                    CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("SHARE_SUSPENSE_ACHD")){
                    //                                        total=CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtShareValue()).doubleValue() ;
                    //                                    }
                    //
                    //                                    if(CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("SHARE_FEE_ACHD")){
                    //                                        total= CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtTotShareFee()).doubleValue() ;
                    //                                    }
                    //
                    //                                    if(CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("MEMBERSHIP_FEE_ACHD")){
                    //                                        total= CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtShareMemFee()).doubleValue() ;
                    //                                    }
                    //
                    //
                    //                                    if(CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("APPLICATION_FEE_ACHD")){
                    //                                        total= CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtShareApplFee()).doubleValue() ;
                    //                                    }
                    //                                    oldAmount = txTransferTO.getAmount().doubleValue();
                    //                                    txTransferTO.setInpAmount(new Double(total));
                    //                                    txTransferTO.setAmount(new Double(total));
                    //                                    if( shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)){
                    //                                        txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                    //                                    } else{
                    //                                        txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    //                                    }
                    //                                    txTransferTO.setStatusDt(currDt);
                    //                                    txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                    //                                    oldAmountMap.put(txTransferTO.getTransId(),new Double(oldAmount));
                    //                                    transferList.add(txTransferTO);
                    //                                    //                                        TransferTrans transferTrans = new TransferTrans();
                    //                                    //                                        transferTrans.setOldAmount(oldAmountMap);
                    //                                    //                                        transferTrans.setInitiatedBranch(_branchCode);
                    //                                    //                                        transferTrans.doDebitCredit(transferList, _branchCode, false, shareAcctDetailsTO.getCommand());
                    //                                    //                                        transferTrans = null;
                    //
                    //
                    //
                    //
                    //                                }else if(!CommonUtil.convertObjToStr(shareAcctDetailsTO.getShareNoFrom()).equals("ADD")){
                    //                                    total=total;
                    //
                    //
                    //                                    oldAmount = txTransferTO.getAmount().doubleValue();
                    //                                    txTransferTO.setInpAmount(new Double(total));
                    //                                    txTransferTO.setAmount(new Double(total));
                    //                                    if( shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)){
                    //                                        txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                    //                                    }
                    //                                    else{
                    //                                        txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    //                                    }
                    //                                    txTransferTO.setStatusDt(currDt);
                    //                                    txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                    //                                    oldAmountMap.put(txTransferTO.getTransId(),new Double(oldAmount));
                    //                                    transferList.add(txTransferTO);
                    //
                    //
                    //                                    //                                        if (TransactionDetailsMap.size()>0)
                    //                                    //                                            if(TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    //                                    //                                                LinkedHashMap allowedTransDetailsTO = (LinkedHashMap)TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                    //                                    //                                                for (int J = 1;J <= allowedTransDetailsTO.size();J++){
                    //                                    //                                                    objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                    //                                    //
                    //                                    //                                                    if(objTransactionTO.getTransType().equals("TRANSFER")){
                    //                                    //                                                        TransferTrans transferTrans = new TransferTrans();
                    //                                    //                                                        transferTrans.setOldAmount(oldAmountMap);
                    //                                    //                                                        transferTrans.setInitiatedBranch(_branchCode);
                    //                                    //                                                        transferTrans.doDebitCredit(transferList, _branchCode, false, shareAcctDetailsTO.getCommand());
                    //                                    //                                                        transferTrans = null;
                    //                                    //                                                    }else  if(objTransactionTO.getTransType().equals("CASH")
                    //                                    //                                                    && !CommonUtil.convertObjToStr(shareAcctDetailsTO.getCommand()).equals("")
                    //                                    //                                                    && CommonUtil.convertObjToStr(shareAcctDetailsTO.getCommand()).length()>0) {
                    //                                    //                                                        System.out.println("In Cash");
                    //                                    //                                                        double newAmount = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                    //                                    //
                    //                                    //
                    //                                    //                                                        if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    //                                    //                                                            HashMap tempMap=new HashMap();
                    //                                    //                                                            List cLst1 = sqlMap.executeQueryForList("getSelectShareCashTransactionTO",   CommonUtil.convertObjToStr(objTransactionTO.getTransId()));
                    //                                    //                                                            if (cLst1!=null&& cLst1.size()>0) {
                    //                                    //                                                                CashTransactionTO txTransferTO1 = null;
                    //                                    //                                                                txTransferTO1 = (CashTransactionTO) cLst1.get(0);
                    //                                    //                                                                oldAmount= CommonUtil.convertObjToDouble(txTransferTO1.getAmount()).doubleValue();
                    //                                    //                                                                txTransferTO1.setInpAmount(new Double(newAmount));
                    //                                    //                                                                txTransferTO1.setAmount(new Double(newAmount));
                    //                                    //                                                                txTransferTO1.setCommand(command);
                    //                                    //                                                                txTransferTO1.setStatus(CommonConstants.STATUS_MODIFIED);
                    //                                    //                                                                txTransferTO1.setStatusDt(currDt);
                    //                                    //
                    //                                    //                                                                map.put("PRODUCTTYPE", TransactionFactory.GL);
                    //                                    //                                                                map.put("OLDAMOUNT", new Double(oldAmount));
                    //                                    //                                                                map.put("CashTransactionTO", txTransferTO1);
                    //                                    //                                                                CashTransactionDAO cashTransDAO = new CashTransactionDAO();
                    //                                    //                                                                cashTransDAO.execute(map,false);
                    //                                    //                                                            }
                    //                                    //                                                            cLst1 = null;
                    //                                    //                                                        }
                    //                                    //                                                        txTransferTO = null;
                    //                                    //                                                        oldAmountMap = null;
                    //                                    //                                                        //                            tempMap =  null;
                    //                                    //                                                        //                            objRemittanceIssueTO.setOtherCharges(new Double(0));
                    //                                    //
                    //                                    //                                                    }
                    //                                    //
                    //                                    //                                                }
                    //                                    //                                            }
                    //
                    //
                    //
                    //                                }
                    //                                objTransactionTO= new TransactionTO();
                    //                                LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    //                                System.out.println("TransactionDetailsMap---->"+TransactionDetailsMap);
                    //                                if(TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    //                                    LinkedHashMap allowedTransDetailsTO = (LinkedHashMap)TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                    //                                    for (int J = 1;J <= allowedTransDetailsTO.size();J++){
                    //                                        objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                    //
                    //                                        objTransactionTO.setTransId(objTO.getShareAcctNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
                    //                                        objTransactionTO.setBatchId(objTO.getShareAcctNo());
                    //                                        if(!shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                    //                                            objTransactionTO.setBranchId(_branchCode);
                    //                                            objTransactionTO.setStatus("DELETED");
                    //                                            sqlMap.executeUpdate("updateRemittanceIssueTransactionTO", objTransactionTO);
                    //                                        }
                    //
                    //                                    }
                    //                                }
                    //                            }
                    //
                    //                            TransferTrans transferTrans = new TransferTrans();
                    //                            transferTrans.setOldAmount(oldAmountMap);
                    //                            transferTrans.setInitiatedBranch(_branchCode);
                    //                            transferTrans.doDebitCredit(transferList, _branchCode, false, shareAcctDetailsTO.getCommand());
                    //                            transferTrans = null;
                    //
                    //                        }else{
                    //                            System.out.println("In Cash Edit");
                    //                            LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    //                            System.out.println("@@TransactionDetailsMap"+TransactionDetailsMap);
                    //                            if(TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    //                                LinkedHashMap allowedTransDetailsTO = (LinkedHashMap)TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                    //                                if(allowedTransDetailsTO!=null && allowedTransDetailsTO.size()>0){
                    //                                    CashTransactionTO txTransferTO1 = null;
                    //                                    for (int J = 0;J < allowedTransDetailsTO.size();J++)  {
                    //
                    //                                        objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                    //                                        System.out.println("!!!@@@objTransactionTO:"+objTransactionTO);
                    //                                        //                                if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    //                                        HashMap tempMap=new HashMap();
                    //                                        List cLst1 = sqlMap.executeQueryForList("getSelectShareCashTransactionTO",   CommonUtil.convertObjToStr(objTransactionTO.getTransId()));
                    //                                        System.out.println("!!!@@@cLst1:"+cLst1);
                    //                                        if (cLst1!=null && cLst1.size()>0) {
                    //                                            if(CommonUtil.convertObjToStr(shareAcctDetailsTO.getShareNoFrom()).equals("ADD")){
                    //                                                for(int l=0;l<cLst1.size();l++){
                    //                                                    txTransferTO1 = (CashTransactionTO) cLst1.get(l);
                    //                                                    oldAmount= CommonUtil.convertObjToDouble(txTransferTO1.getAmount()).doubleValue();
                    //                                                    double newAmount = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                    //                                                    if(CommonUtil.convertObjToStr(txTransferTO1.getAuthorizeRemarks()).equals("SHARE_ACHD") ||
                    //                                                    CommonUtil.convertObjToStr(txTransferTO1.getAuthorizeRemarks()).equals("SHARE_SUSPENSE_ACHD")){
                    //                                                        newAmount=CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtShareValue()).doubleValue() ;
                    //
                    //                                                    }
                    //
                    //                                                    if(CommonUtil.convertObjToStr(txTransferTO1.getAuthorizeRemarks()).equals("SHARE_FEE_ACHD")){
                    //                                                        newAmount= CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtTotShareFee()).doubleValue() ;
                    //                                                    }
                    //
                    //                                                    if(CommonUtil.convertObjToStr(txTransferTO1.getAuthorizeRemarks()).equals("MEMBERSHIP_FEE_ACHD")){
                    //                                                        newAmount= CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtShareMemFee()).doubleValue() ;
                    //                                                    }
                    //
                    //
                    //                                                    if(CommonUtil.convertObjToStr(txTransferTO1.getAuthorizeRemarks()).equals("APPLICATION_FEE_ACHD")){
                    //                                                        newAmount= CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtShareApplFee()).doubleValue() ;
                    //                                                    }
                    //                                                    oldAmount = txTransferTO1.getAmount().doubleValue();
                    //                                                    txTransferTO1.setInpAmount(new Double(newAmount));
                    //
                    //                                                    txTransferTO1.setAmount(new Double(newAmount));
                    //                                                    txTransferTO1.setCommand(command);
                    //                                                    txTransferTO1.setStatus(CommonConstants.STATUS_MODIFIED);
                    //                                                    if (shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                    //                                                        txTransferTO1.setStatus(CommonConstants.STATUS_DELETED);
                    //                                                    }
                    //                                                    txTransferTO1.setStatusDt(currDt);
                    //                                                    transferList.add(txTransferTO1);
                    //                                                }
                    //                                            }
                    //
                    //
                    //
                    //                                            else if(!CommonUtil.convertObjToStr(shareAcctDetailsTO.getShareNoFrom()).equals("ADD")){
                    //                                                double newAmount = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                    //
                    //
                    //                                                oldAmount = txTransferTO1.getAmount().doubleValue();
                    //                                                txTransferTO1.setInpAmount(new Double(newAmount));
                    //                                                txTransferTO1.setAmount(new Double(newAmount));
                    //
                    //                                                if( shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)){
                    //                                                    txTransferTO1.setStatus(CommonConstants.STATUS_DELETED);
                    //                                                }
                    //                                                else{
                    //                                                    txTransferTO1.setStatus(CommonConstants.STATUS_MODIFIED);
                    //                                                }
                    //                                                txTransferTO1.setStatusDt(currDt);
                    //                                                transferList.add(txTransferTO1);
                    //                                            }
                    //                                        }
                    //                                    }
                    //                                    map.put(CommonConstants.PRODUCT_ID, TransactionFactory.GL);
                    //                                    map.put("PRODUCTTYPE", TransactionFactory.GL);
                    //                                    map.put("OLDAMOUNT", new Double(oldAmount));
                    //                                    map.put("CashTransactionTO", transferList);
                    //                                    System.out.println("@@@@transferList"+transferList);
                    //                                    CashTransactionDAO cashTransDAO = new CashTransactionDAO();
                    //                                    cashTransDAO.execute(map,false);
                    //                                }
                    //                                objTransactionTO= new TransactionTO();
                    //                                LinkedHashMap TransactionDetailsMap1 = (LinkedHashMap) map.get("TransactionTO");
                    //                                System.out.println("TransactionDetailsMap---->"+TransactionDetailsMap1);
                    //
                    //                                if(TransactionDetailsMap1.containsKey("NOT_DELETED_TRANS_TOs")) {
                    //                                    LinkedHashMap allowedTransDetailsTO1 = (LinkedHashMap)TransactionDetailsMap1.get("NOT_DELETED_TRANS_TOs");
                    //                                    for (int k = 1; k<= allowedTransDetailsTO1.size();k++){
                    //                                        objTransactionTO = (TransactionTO) allowedTransDetailsTO1.get(String.valueOf(k));
                    //
                    //                                        objTransactionTO.setTransId(objTO.getShareAcctNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
                    //                                        objTransactionTO.setBatchId(objTO.getShareAcctNo());
                    //                                        if(!shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                    //                                            objTransactionTO.setBranchId(_branchCode);
                    //                                            sqlMap.executeUpdate("updateRemittanceIssueTransactionTO", objTransactionTO);
                    //                                        }
                    //
                    //                                    }
                    //                                }
                    //
                    //                                //                                                         for (int J = 1;J <= allowedTransDetailsTO.size();J++){
                    //
                    //
                    //
                    //
                    //                                //                                                         }
                    //                            }
                    //
                    //
                    //
                    //
                    //                            //                                }
                    //
                    //                        }
                    //
                    //
                    //                        lst = null;
                    //                        oldAmountMap = null;
                    //                        transferList = null;
                    //                        shareAcctNoMap = null;
                    //                        txTransferTO = null;
                    //
                    //
                    //                        //                    }
                    //
                    //                    }

                    objLogTO.setData(objTO.toString());
                    objLogTO.setPrimaryKey(objTO.getKeyData());
                    objLogDAO.addToLog(objLogTO);
                }
            } catch (Exception e) {
                //                sqlMap.rollbackTransaction();
                e.printStackTrace();
                //                throw new TransRollbackException(e);
                throw e;
            }
        }
    }

//    added by Nikhil for DRF Applicable
    public DrfTransactionTO getDrfTransTO(String cmd, double intTrfAm, HashMap map, HashMap whereMap) {
        DrfTransactionTO objgetDrfTransactionTO = new DrfTransactionTO();
        objgetDrfTransactionTO.setCommand(cmd);
        objgetDrfTransactionTO.setStatus(CommonConstants.STATUS_CREATED);
        objgetDrfTransactionTO.setTxtDrfTransMemberNo(CommonUtil.convertObjToStr(whereMap.get("DRF_MEMBER")));
        objgetDrfTransactionTO.setTxtDrfTransAmount(CommonUtil.convertObjToDouble(whereMap.get("DRF_AMOUNT")));
        objgetDrfTransactionTO.setTxtDrfTransName(CommonUtil.convertObjToStr(whereMap.get("DRF_MEMBER_NAME")));
        objgetDrfTransactionTO.setCboDrfTransProdID(CommonUtil.convertObjToStr(whereMap.get("DRF_PRODUCT")));
        objgetDrfTransactionTO.setDrfProdPaymentAmt(CommonUtil.convertObjToStr(whereMap.get("DRF_PRODUCT_AMOUNT")));
        objgetDrfTransactionTO.setDrfProductAmount(CommonUtil.convertObjToStr(whereMap.get("DRF_AMOUNT")));
        objgetDrfTransactionTO.setRdoDrfTransaction("RECIEPT");
        objgetDrfTransactionTO.setChkDueAmtPayment("N");
        objgetDrfTransactionTO.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
        objgetDrfTransactionTO.setStatusDate(currDt);
        objgetDrfTransactionTO.setInitiatedBranch((String) map.get(CommonConstants.BRANCH_ID));
        return objgetDrfTransactionTO;
    }

    public void updateShareAppData(HashMap map) throws Exception {


        HashMap shareAcctNoMap = new HashMap();
        String no = objTO.getTxtApplicationNo();

        shareAcctNoMap.put("SHARE APPLICATION NO", no);
        shareAcctNoMap = (HashMap) sqlMap.executeQueryForObject("transferResolvedShareApp", shareAcctNoMap);
        int oldNoOfShares = (shareAcctNoMap == null && shareAcctNoMap.size() == 0) ? 0 : CommonUtil.convertObjToInt(shareAcctNoMap.get("NO_OF_SHARES"));
        sqlMap.executeUpdate("updateShareAppDetailsTO", shareAcctDetailsTO);
        double shareAmt = CommonUtil.convertObjToDouble(objTO.getShareAmount()).doubleValue();
        double shareFee = CommonUtil.convertObjToDouble(objTO.getShareFee()).doubleValue();
        double memberFee = CommonUtil.convertObjToDouble(objTO.getMemFee()).doubleValue();
        double applnFee = CommonUtil.convertObjToDouble(objTO.getApplFee()).doubleValue();
        double noOfShares = shareAcctDetailsTO.getNoOfShares().doubleValue();
        double total = (shareAmt * noOfShares) + (shareFee * noOfShares) + memberFee + applnFee;
        if (oldNoOfShares != noOfShares || shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
            shareAcctNoMap = new HashMap();
            shareAcctNoMap.put("LINK_BATCH_ID", no);
            List lst = sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", shareAcctNoMap);
            TxTransferTO txTransferTO = null;
            double oldAmount = 0;
            HashMap oldAmountMap = new HashMap();
            ArrayList transferList = new ArrayList();
            if (lst != null && lst.size() > 0) {
                for (int j = 0; j < lst.size(); j++) {
                    txTransferTO = (TxTransferTO) lst.get(j);
                    if (CommonUtil.convertObjToStr(shareAcctDetailsTO.getShareNoFrom()).equals("ADD")) {
                        if (CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("SHARE_SUSPENSE_ACHD")) {
                            total = (shareAmt * noOfShares) + (shareFee * noOfShares) + memberFee + applnFee;
                        }
                        /*
                         * if(CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("SHARE_ACHD")){
                         * total=CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtShareValue()).doubleValue()
                         * ; }
                         * if(CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("SHARE_FEE_ACHD")){
                         * total=
                         * CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtTotShareFee()).doubleValue()
                         * ; }
                         *
                         * if(CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("MEMBERSHIP_FEE_ACHD")){
                         * total=
                         * CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtShareMemFee()).doubleValue()
                         * ; }
                         *
                         *
                         * if(CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("APPLICATION_FEE_ACHD")){
                         * total=
                         * CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtShareApplFee()).doubleValue()
                         * ;
                         }
                         */
                        oldAmount = txTransferTO.getAmount().doubleValue();
                        txTransferTO.setInpAmount(new Double(total));
                        txTransferTO.setAmount(new Double(total));
                        if (shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                            txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                        } else {
                            txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        }
                        txTransferTO.setStatusDt(currDt);
                        txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                        oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                        transferList.add(txTransferTO);
                        TransferTrans transferTrans = new TransferTrans();
                        transferTrans.setOldAmount(oldAmountMap);
                        transferTrans.setInitiatedBranch(_branchCode);
                        transferTrans.doDebitCredit(transferList, _branchCode, false, shareAcctDetailsTO.getCommand());
                        transferTrans = null;
                    }
                }
            } else {
                LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                    if (allowedTransDetailsTO != null && allowedTransDetailsTO.size() > 0) {
                        for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                            objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));

                            //                                if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                            HashMap tempMap = new HashMap();
                            if (!CommonUtil.convertObjToStr(shareAcctDetailsTO.getShareNoFrom()).equals("ADD")) {

                                List cLst1 = sqlMap.executeQueryForList("getSelectShareCashTransactionTO", CommonUtil.convertObjToStr(objTransactionTO.getTransId()));
                                if (cLst1 != null && cLst1.size() > 0) {
                                    CashTransactionTO txTransferTO1 = null;
                                    txTransferTO1 = (CashTransactionTO) cLst1.get(0);
                                    oldAmount = CommonUtil.convertObjToDouble(txTransferTO1.getAmount()).doubleValue();
                                    double newAmount = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                    txTransferTO1.setInpAmount(new Double(newAmount));
                                    txTransferTO1.setAmount(new Double(newAmount));
                                    txTransferTO1.setCommand(command);
                                    txTransferTO1.setStatus(CommonConstants.STATUS_MODIFIED);
                                    txTransferTO1.setStatusDt(currDt);

                                    map.put("PRODUCTTYPE", TransactionFactory.GL);
                                    map.put("OLDAMOUNT", new Double(oldAmount));
                                    map.put("CashTransactionTO", txTransferTO1);
                                    map.put("SCREEN_NAME", "Share Account");
                                    CashTransactionDAO cashTransDAO = new CashTransactionDAO();
                                    cashTransDAO.execute(map, false);
                                }
                                cLst1 = null;
                            }


                            objTransactionTO.setTransId(objTO.getTxtApplicationNo());
                            objTransactionTO.setBatchId(objTO.getTxtApplicationNo());
                            objTransactionTO.setBranchId(_branchCode);
                            if (!shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                                sqlMap.executeUpdate("updateRemittanceIssueTransactionTO", objTransactionTO);
                            }

                        }
                    }

                }
            }
        }
    }

    public void insertCashTransaction(HashMap map,String generateSingleTransId) throws Exception, Exception {
        double transAmt;
        TransactionTO transTO = new TransactionTO();
        HashMap tranMap = new HashMap();
        ArrayList cashList = new ArrayList();
        shareType = objTO.getShareType();
        HashMap DRF_APPLICABLE = new HashMap();
        double drfAmt = 0.0;
        String drfHead = "";
        String Split_needed = "N";
        if (map.containsKey("DRF_APPLICABLE")) {
            DRF_APPLICABLE = (HashMap) map.get("DRF_APPLICABLE");
            drfAmt = Double.parseDouble(DRF_APPLICABLE.get("DRF_AMOUNT").toString());
            HashMap aMap = new HashMap();
            aMap.put("SHARE_TYPE", shareType);
            List aList = sqlMap.executeQueryForList("getDRFAccountDts", aMap);
            aMap = (HashMap) aList.get(0);
            drfHead = aMap.get("DRF_SUSPENSE_HEAD").toString();
            Split_needed = aMap.get("SPLIT_DRF").toString();
        }
        HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getShareSuspenseAcHd", objTO.getShareType());
        if (CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue() > 0) {
            transAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
            if (Split_needed.equals("Y")) {
                transAmt = transAmt - drfAmt;
            }
            HashMap txMap = new HashMap();
            txMap.put(CommonConstants.BRANCH_ID, _branchCode);
            txMap.put(CommonConstants.PRODUCT_ID, TransactionFactory.GL);
            txMap.put(CommonConstants.USER_ID, objTO.getCreatedBy());
            txMap.put(CommonConstants.AC_HD_ID, acHeads.get("SHARE_SUSPENSE_ACHD"));
            txMap.put("AUTHORIZEREMARKS", "SHARE_SUSPENSE_ACHD");
            txMap.put("AMOUNT", new Double(transAmt));
            txMap.put("LINK_BATCH_ID", objTO.getTxtApplicationNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
            txMap.put("HIERARCHY", "2");
            txMap.put("TRANS_MOD_TYPE", "SH");
            txMap.put("generateSingleTransId", generateSingleTransId);
            cashList.add(setCashTransaction(txMap, objTO));
            /////////////////////////////////////
            if (Split_needed.equals("Y") && drfAmt > 0.0) {
                txMap = new HashMap();
                txMap.put(CommonConstants.BRANCH_ID, _branchCode);
                txMap.put(CommonConstants.PRODUCT_ID, TransactionFactory.GL);
                txMap.put(CommonConstants.USER_ID, objTO.getCreatedBy());
                // System.out.println("the linkbatch id is"+objTransferTrans.getLinkBatchId());
                txMap.put(CommonConstants.AC_HD_ID, drfHead);
                txMap.put("AUTHORIZEREMARKS", "SHARE_SUSPENSE_ACHD");
                txMap.put("AMOUNT", new Double(drfAmt));
                txMap.put("LINK_BATCH_ID", objTO.getTxtApplicationNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
                txMap.put("HIERARCHY", "2");
                txMap.put("TRANS_MOD_TYPE", "SH");
                txMap.put("generateSingleTransId", generateSingleTransId);
                cashList.add(setCashTransaction(txMap, objTO));
            }
            /////////////////////////
            tranMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
            tranMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
            tranMap.put(CommonConstants.IP_ADDR, CommonUtil.convertObjToStr(map.get("IP_ADDR")));
            tranMap.put(CommonConstants.MODULE, CommonUtil.convertObjToStr(map.get("MODULE")));
            tranMap.put(CommonConstants.SCREEN, "Share Account");
            tranMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
            tranMap.put("DAILYDEPOSITTRANSTO", cashList);
            tranMap.put("SCREEN_NAME", "Share Account");
            txMap.put("TRANS_MOD_TYPE","SH");
            CashTransactionDAO cashDao;
            cashDao = new CashTransactionDAO();
            tranMap = cashDao.execute(tranMap, false);
            cashDao = null;
            tranMap = null;
//            if(Split_needed.equals("Y"))
//            {
//               txMap = new HashMap();
//            txMap.put(CommonConstants.BRANCH_ID, _branchCode);
//            txMap.put(CommonConstants.PRODUCT_ID, TransactionFactory.GL);
//            txMap.put(CommonConstants.USER_ID, objTO.getCreatedBy());
//            
//            
//            
//            System.out.println("the head is"+(String)acHeads.get("SHARE_SUSPENSE_ACHD"));
//            // System.out.println("the linkbatch id is"+objTransferTrans.getLinkBatchId());
//            txMap.put(CommonConstants.AC_HD_ID,drfHead);
//            txMap.put("AUTHORIZEREMARKS","SHARE_SUSPENSE_ACHD");
//            txMap.put("AMOUNT", new Double(drfAmt));
//            txMap.put("LINK_BATCH_ID", objTO.getTxtApplicationNo()+"_"+shareAcctDetailsTO.getShareAcctDetNo());
//            txMap.put("HIERARCHY","2");
//            cashList.add(setCashTransaction(txMap,objTO));
//            
//            tranMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
//            tranMap.put(CommonConstants.USER_ID,CommonUtil.convertObjToStr(map.get("USER_ID")));
//            tranMap.put(CommonConstants.IP_ADDR,CommonUtil.convertObjToStr(map.get("IP_ADDR")));
//            tranMap.put(CommonConstants.MODULE,CommonUtil.convertObjToStr(map.get("MODULE")));
//            tranMap.put(CommonConstants.SCREEN, "Share Account");
//            tranMap.put("MODE",CommonConstants.TOSTATUS_INSERT);
//            
//            tranMap.put("DAILYDEPOSITTRANSTO",cashList);
//            tranMap.put("SCREEN_NAME","Share Account");
////            CashTransactionDAO cashDao ;
//            cashDao = new CashTransactionDAO() ;
//            System.out.println("the map innnnnncccs"+tranMap);
//            tranMap=cashDao.execute(tranMap, false);
//            cashDao = null ;
//            tranMap = null ;
//            
//            } 
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

    public void insertRatification(HashMap map) throws Exception, Exception {
        HashMap txMap = new HashMap();
        shareType = objTO.getShareType();
        HashMap DRF_APPLICABLE = new HashMap();
        double drfAmt = 0.0;
        String drfHead = "";
        String Split_needed = "N";
        if (map.containsKey("DRF_APPLICABLE")) {
            DRF_APPLICABLE = (HashMap) map.get("DRF_APPLICABLE");
            drfAmt = Double.parseDouble(DRF_APPLICABLE.get("DRF_AMOUNT").toString());
            HashMap aMap = new HashMap();
            aMap.put("SHARE_TYPE", shareType);
            List aList = sqlMap.executeQueryForList("getDRFAccountDts", aMap);
            aMap = (HashMap) aList.get(0);
            drfHead = aMap.get("DRF_SUSPENSE_HEAD").toString();
            Split_needed = aMap.get("SPLIT_DRF").toString();
        }
        //shareAcctDetailsTO = (ShareAcctDetailsTO) shareAcctDetTOMap.get(String.valueOf(i));
        ArrayList transferList = new ArrayList();
        TransferTrans objTransferTrans = new TransferTrans();
        objTransferTrans.setInitiatedBranch(_branchCode);
        objTransferTrans.setLinkBatchId(objTO.getTxtApplicationNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
        HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getShareSuspenseAcHd", objTO.getShareType());
        txMap.put(TransferTrans.PARTICULARS, objTO.getTxtApplicationNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
        txMap.put(CommonConstants.USER_ID, objTO.getCreatedBy());
        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
        txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
        double debitAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
        if (Split_needed.equals("Y")) {
            debitAmt = debitAmt - drfAmt;
        }
        if (objTransactionTO.getProductType().equals("GL")) {
            txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
        } else {
            txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
            txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
        }
        txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
        txMap.put("generateSingleTransId",generateSingleTransId);
        
        if (objTransactionTO.getProductType().equals("OA")){
             txMap.put("TRANS_MOD_TYPE", "OA");
        }else if(objTransactionTO.getProductType().equals("AB")){
             txMap.put("TRANS_MOD_TYPE", "AB");
        }else if(objTransactionTO.getProductType().equals("SA")){
             txMap.put("TRANS_MOD_TYPE", "SA");
        }else if(objTransactionTO.getProductType().equals("TL")){
            txMap.put("TRANS_MOD_TYPE", "TL");
        }else if(objTransactionTO.getProductType().equals("AD")){
            txMap.put("TRANS_MOD_TYPE", "AD");
        }else
            txMap.put("TRANS_MOD_TYPE", "GL");
         txMap.put("SCREEN_NAME", "Share Account");
        transferList.add(objTransferTrans.getDebitTransferTO(txMap, debitAmt));
        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SHARE_SUSPENSE_ACHD"));
        txMap.put("AUTHORIZEREMARKS", "SHARE_SUSPENSE_ACHD");
        txMap.put("HIERARCHY", "1");
        txMap.put("TRANS_MOD_TYPE", "SH");
        txMap.put("generateSingleTransId",generateSingleTransId);
        txMap.put("SCREEN_NAME", "Share Account");
        transferList.add(objTransferTrans.getCreditTransferTO(txMap, debitAmt));
        ///////////////////////////////////////////////////////////////////////////////////
        if (Split_needed.equals("Y") && drfAmt > 0.0) {
            txMap = new HashMap();
            txMap.put(TransferTrans.PARTICULARS, objTO.getTxtApplicationNo() + "_" + shareAcctDetailsTO.getShareAcctDetNo());
            txMap.put(CommonConstants.USER_ID, objTO.getCreatedBy());
            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
//        double debitAmt= CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
//       
//        
//          if(Split_needed.equals("Y"))
//            {
//            debitAmt=debitAmt-drfAmt;
//          }
//        
            if (objTransactionTO.getProductType().equals("GL")) {
                txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));

            } else {
                txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
            }
            txMap.put("generateSingleTransId", generateSingleTransId);
            txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
            if (objTransactionTO.getProductType().equals("OA")) {
                txMap.put("TRANS_MOD_TYPE", "OA");
            } else if (objTransactionTO.getProductType().equals("AB")) {
                txMap.put("TRANS_MOD_TYPE", "AB");
            } else if (objTransactionTO.getProductType().equals("SA")) {
                txMap.put("TRANS_MOD_TYPE", "SA");
            } else if (objTransactionTO.getProductType().equals("TL")) {
                txMap.put("TRANS_MOD_TYPE", "TL");
            } else if (objTransactionTO.getProductType().equals("AD")) {
                txMap.put("TRANS_MOD_TYPE", "AD");
            } else {
                txMap.put("TRANS_MOD_TYPE", "GL");
            }
            txMap.put("SCREEN_NAME", "Share Account");
            transferList.add(objTransferTrans.getDebitTransferTO(txMap, drfAmt));
            txMap.put(TransferTrans.CR_AC_HD, drfHead);
            txMap.put("AUTHORIZEREMARKS", "DRF_SUSPENSE_ACHD");
            txMap.put("HIERARCHY", "1");
            txMap.put("TRANS_MOD_TYPE", "SH");
            txMap.put("generateSingleTransId", generateSingleTransId);
            transferList.add(objTransferTrans.getCreditTransferTO(txMap, drfAmt));
        }
        ///////////////////////////////////////////////////////////////////////////
        objTransferTrans.doDebitCredit(transferList, _branchCode, false);
    }
    
    private void getShareTransDetails(String batchId ,String acctNo ) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put("SHARE_ACCT_NO",acctNo);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        getTransMap.put("AUTHORIZE_STATUS", "AUTHORIZE_STATUS");
        execReturnMap = new HashMap();
        List transList = (List) sqlMap.executeQueryForList("getShareTransferDetails", getTransMap);
        if (transList != null && transList.size() > 0) {
            execReturnMap.put("TRANSFER_TRANS_LIST", transList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
    }
    
    
    private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        getTransMap.put("AUTHORIZE_STATUS", "AUTHORIZE_STATUS");
        execReturnMap = new HashMap();
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
//    added by Nikhil for DRF Applicable

    private void getMultipleTransDetails(HashMap getTransMap) throws Exception {
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        getTransMap.put("AUTHORIZE_STATUS", "AUTHORIZE_STATUS");
        execReturnMap = new HashMap();
        List transList = (List) sqlMap.executeQueryForList("getMultipleTransferDetails", getTransMap);
        if (transList != null && transList.size() > 0) {
            execReturnMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getMultipleCashTransDetails", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            execReturnMap.put("CASH_TRANS_LIST", cashList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    private String getIBRAcctHead() throws Exception {
        HashMap transMap = (HashMap) sqlMap.executeQueryForObject("getSelectTransParams", null);
        return CommonUtil.convertObjToStr(transMap.get("IBR_AC_HD"));
    }

    private void insertData(HashMap map, LogDAO objLogDAO, LogTO objLogTO,String generateSingleTransId) throws Exception {
        try {
            NomineeDAO objNomineeDAO = new NomineeDAO();
            NomineeDAO objDRFNomineeDAO = new NomineeDAO();//Added By Revathi.L
            String applno = "";
            String apno = "";
            String acno = "";
            sqlMap.startTransaction();
            ShareAccInfoTO objShareAccInfoTO = new ShareAccInfoTO();
            //--- If "New" is displayed in the "Share Acct No." text,
            //--- then generate the new Share Acct No.
            //  objShareAccInfoTO   =(ShareAccInfoTO)map.get("objShareAccInfoTO");
            // String ratification= objShareAccInfoTO.getTxtRatification();
            String ratification = objTO.getTxtRatification();
            String yes = "Y";
            String noofshare = "";
            String sharedet = "";
            String SHAREACC = "";
            HashMap hsmap = new HashMap();
            if (ratification.equals(yes)) {
                if (objTO.getTxtApplicationNo() == null) {
                    getShareAppl_No();
                    applno = objTO.getTxtApplicationNo();

                } else {
                    returnApplNo = objTO.getTxtApplicationNo();
                    applno = objTO.getTxtApplicationNo();
                }
                hsmap.put("MEMBERSHIP_NO", objTO.getTxtApplicationNo());
            } else {
                if (objTO.getShareAcctNo() == null) {
                    getShareAcct_No();
                } else {
                    returnShareNo = objTO.getShareAcctNo();
                }
                hsmap.put("MEMBERSHIP_NO", objTO.getShareAcctNo());
            }
            // applno=objTO.getTxtApplicationNo();
            String custid = objTO.getCustId();
            String stype = objTO.getShareType();
            //HashMap hsmap = new HashMap();
            hsmap.put("CUST_ID", custid);
            hsmap.put("MEMBERSHIP_CLASS", stype);
            //hsmap.put("MEMBERSHIP_NO", objTO.getTxtApplicationNo());
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            //objTO.setBranchCode(_branchCode);

            if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                if (!applno.equals(apno)) {
                    if (map.containsKey("SHARE_STATUS")) {
                        objTO.setAcctStatus("CLOSED");
                        objTO.setCloseDt(CommonUtil.getProperDate(currDt, currDt));//added by shihad for mantis 10387 on 24.02.2015
                    }
                    sqlMap.executeUpdate("updateShareAppInfoTO", objTO);
                } else {
                    if (map.containsKey("SHARE_STATUS")) {
                        objTO.setAcctStatus("CLOSED");
                        objTO.setCloseDt(CommonUtil.getProperDate(currDt, currDt));//added by shihad for mantis 10387 on 24.02.2015
                    }
                    sqlMap.executeUpdate("updateShareAccInfoTO", objTO);
                    objTO.setStatus(CommonConstants.STATUS_CREATED);
                    //Added by sreekrishnan for 0004374
                    //if (true) {
                    HashMap hmap = new HashMap();
                    hmap.put("CUST_ID", CommonUtil.convertObjToStr(objTO.getCustId()));
                    hmap.put("SHARE_ACT_NO", CommonUtil.convertObjToStr(objTO.getShareAcctNo()));
                    List chkList = sqlMap.executeQueryForList("getSharePriorityClosing", hmap);
                    if (chkList != null && chkList.size() > 0) {
                        HashMap oldMap = (HashMap) chkList.get(0);  
                        hmap.putAll(oldMap);
                        sqlMap.executeUpdate("getUpdateCustomer", hmap);
                    }else{
                        if (map.containsKey("SHARE_STATUS")) {
                            sqlMap.executeUpdate("updateClosedShare", hmap);
                        }
                    } 
                        //sqlMap.executeUpdate("getUpdateCustomer", hmap);
                    //}
                }
            } else {
                sqlMap.executeUpdate("insertShareAccInfoTO", objTO);

            }

            if (/*!applno.equals(apno)*/ command.equals(CommonConstants.TOSTATUS_INSERT)) {
                sqlMap.executeUpdate("getUpdateCustomer", hsmap);
            }
            objLogTO.setData(objTO.toString());
            objLogTO.setPrimaryKey(objTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            // Inserting into Customer History Table
            CustomerHistoryTO objCustomerHistoryTO = new CustomerHistoryTO();
            objCustomerHistoryTO.setAcctNo(objTO.getShareAcctNo());
            objCustomerHistoryTO.setCustId(objTO.getCustId());
            objCustomerHistoryTO.setProductType("SH");
            objCustomerHistoryTO.setProdId(objTO.getShareType());
            objCustomerHistoryTO.setRelationship(AcctStatusConstants.ACCT_HOLDER);
            objCustomerHistoryTO.setFromDt(currDt);
            CustomerHistoryDAO.insertToHistory(objCustomerHistoryTO);
            objCustomerHistoryTO = null;


            //--- Inserts the ShareAcct Details data into the database if the Customer is not a Nominal member.
            if ((shareAcctDetTOMap != null) || (shareAcctDetTOMap.size() != 0)) {
                insertShareAcctDetails(objLogDAO, objLogTO, map);

                if (!ratification.equals(yes)) {
                    HashMap hmap = new HashMap();
                    hmap.put("SHARE ACCOUNT NO", objTO.getShareAcctNo());
                    hmap.put("CUST_ID", objTO.getCustId());
                    hmap.put("MEMBERSHIP_CLASS", objTO.getShareType());
                    sqlMap.executeUpdate("updateShareAcc", hmap);
                    sqlMap.executeUpdate("updateShareDet", hmap);
                    String multiShareAllowed = "";
                    HashMap multiShareMap = (HashMap) (sqlMap.executeQueryForList("getMultiShareAllowed", map)).get(0);
                    if (multiShareMap.get("MULTI_SHARE_ALLOWED") != null) {
                        multiShareAllowed = multiShareMap.get("MULTI_SHARE_ALLOWED").toString();
                    } else {
                        multiShareAllowed = "N";
                    }
                    //Added by nithya on 6-12-2017 for 0007609
                    boolean memberUpdationFlag = true;
                    if(map.containsKey("SHARE_STATUS") && CommonUtil.convertObjToStr(map.get("SHARE_STATUS")).equalsIgnoreCase("CLOSED")){
                        memberUpdationFlag = false;
                    }// End
                    if (multiShareAllowed.equals("Y")) {
                        List chkList = sqlMap.executeQueryForList("getMembershipPriority", hmap);
                        if (chkList != null && chkList.size() > 0) {
                            HashMap oldMap = (HashMap) chkList.get(0);
                            int old_priority = 0;
                            if (oldMap.get("PRIORITY") != null || (!oldMap.get("PRIORITY").toString().equals(""))) {
                                old_priority = CommonUtil.convertObjToInt(oldMap.get("PRIORITY"));
                            }
                            hmap.put("SHARE TYPE", hmap.get("MEMBERSHIP_CLASS"));
                            HashMap newMap = (HashMap) (sqlMap.executeQueryForList("getShare_priority", hmap)).get(0);
                            int new_priority = CommonUtil.convertObjToInt(newMap.get("PRIORITY"));
                            if (new_priority > old_priority) {
                                sqlMap.executeUpdate("updatecust", hmap);
                            }
                        } else {
                            sqlMap.executeUpdate("updatecust", hmap);
                        }
                    } else {
                        if(memberUpdationFlag)//Added by nithya on 6-12-2017 for 0007609
                        sqlMap.executeUpdate("updatecust", hmap);
                    }
                    //Added by nithya on 09-12-2016 for 5525 start
                    // check if there is entry in cust_regional table
                    // if yes, update membership no.
                    int regCount = 0;
                    List chkRegLangLst = sqlMap.executeQueryForList("customer.getCustRegionalCount", hmap);
                    if(chkRegLangLst != null && chkRegLangLst.size() > 0){
                        HashMap regMap = (HashMap)chkRegLangLst.get(0);
                        regCount = CommonUtil.convertObjToInt(regMap.get("REG_COUNT"));
                    }
                    if(regCount > 0){
                       sqlMap.executeUpdate("updateMembershipNoInCustRegion", hmap); 
                    }
                    // //Added by nithya on 09-12-2016 for 5525 End
                }

            }

            //--- Inserts the Joint Account Holder  Details data into the database if the Constitution is Joint_Account.
            if (objTO.getConstitution().equals("JOINT_ACCOUNT")) {
                insertJointAccntDetails(objLogDAO, objLogTO);
            }


            final String USERID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
            if (ratification.equals(yes)) {
                SHAREACC = CommonUtil.convertObjToStr(objTO.getTxtApplicationNo());
            } else {
                SHAREACC = CommonUtil.convertObjToStr(objTO.getShareAcctNo());
            }
            //--- To Add the Data Related to Nomine(s)...
            ArrayList nomineeTOList = (ArrayList) map.get("AccountNomineeTO");
            ArrayList drfNomineeTOList = (ArrayList) map.get("DRFAccountNomineeTO");
            if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData(map, objLogDAO, objLogTO);
            } else {
                objNomineeDAO.insertData(nomineeTOList, SHAREACC, false, USERID, SCREEN, objLogTO, objLogDAO);
            }
            if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {//Added By Revathi.L
                updateData(map, objLogDAO, objLogTO);
            } else {
                if (drfNomineeTOList != null) {//Added By Revathi.L
                    objDRFNomineeDAO.insertData(drfNomineeTOList, SHAREACC, false, USERID, SCREEN1, objLogTO, objLogDAO);
                }
            }

            //            if (command.equals(CommonConstants.TOSTATUS_INSERT)){
            //                    double shareAmt = CommonUtil.convertObjToDouble(objTO.getShareAmount()).doubleValue() ;
            //                    double shareFee = CommonUtil.convertObjToDouble(objTO.getShareFee()).doubleValue() ;
            //                    double memberFee = CommonUtil.convertObjToDouble(objTO.getMemFee()).doubleValue() ;
            //                    double applnFee = CommonUtil.convertObjToDouble(objTO.getApplFee()).doubleValue() ;
            //
            //                    TxTransferTO transferTo = null ;
            //                    ArrayList transferList = new ArrayList();;
            //                    HashMap txMap ;
            //                    HashMap acHeads = (HashMap)sqlMap.executeQueryForObject("getShareSuspenseAcHd", objTO.getShareType());
            //
            //                    if(memberFee != 0){
            //                        transactionDAO.setBatchId(objTO.getShareAcctNo());
            //                        transactionDAO.setBatchDate(currDt);
            //                        txMap = new HashMap();
            //                        txMap.put(TransferTrans.CR_AC_HD, (String)acHeads.get("MEMBERSHIP_FEE_ACHD"));
            //                        txMap.put(TransferTrans.CR_BRANCH, branchId);
            //                        txMap.put(TransferTrans.CURRENCY, "INR");
            //                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            //                        transferTo =  transactionDAO.addTransferCreditLocal(txMap,memberFee) ;
            //                        transferList.add(transferTo);
            //                    }
            //
            //                    if(applnFee != 0){
            //                        transactionDAO.setBatchId(objTO.getShareAcctNo());
            //                        transactionDAO.setBatchDate(currDt);
            //                        txMap = new HashMap();
            //                        txMap.put(TransferTrans.CR_AC_HD, (String)acHeads.get("APPLICATION_FEE_ACHD"));
            //                        txMap.put(TransferTrans.CR_BRANCH, branchId);
            //                        txMap.put(TransferTrans.CURRENCY, "INR");
            //                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            //                        transferTo =  transactionDAO.addTransferCreditLocal(txMap,applnFee) ;
            //                        transferList.add(transferTo);
            //                    }
            //
            //                    if(shareFee != 0){
            //                        transactionDAO.setBatchId(objTO.getShareAcctNo());
            //                        transactionDAO.setBatchDate(currDt);
            //                        txMap = new HashMap();
            //                        txMap.put(TransferTrans.CR_AC_HD, (String)acHeads.get("SHARE_FEE_ACHD"));
            //                        txMap.put(TransferTrans.CR_BRANCH, branchId);
            //                        txMap.put(TransferTrans.CURRENCY, "INR");
            //                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            //                        transferTo =  transactionDAO.addTransferCreditLocal(txMap,shareFee) ;
            //                        transferList.add(transferTo);
            //                    }
            //
            //                    if(shareAmt != 0){
            //                        transactionDAO.setBatchId(objTO.getShareAcctNo());
            //                        transactionDAO.setBatchDate(currDt);
            //                        txMap = new HashMap();
            //                        txMap.put(TransferTrans.CR_AC_HD, (String)acHeads.get("SHARE_SUSPENSE_ACHD"));
            //                        txMap.put(TransferTrans.CR_BRANCH, branchId);
            //                        txMap.put(TransferTrans.CURRENCY, "INR");
            //                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            //                        transferTo =  transactionDAO.addTransferCreditLocal(txMap,shareAmt) ;
            //                        transferList.add(transferTo);
            //                    }
            //                    map.put("MODE", command);
            //                    transactionDAO.execute(map);
            //                    System.out.println("transactionDAO.getBatchId() " + transactionDAO.getBatchId());
            //                    transactionDAO.doTransfer();
            //                    transactionDAO.doTransferLocal(transferList, branchId);
            //                }

            objNomineeDAO = null;
            objDRFNomineeDAO = null;
            drfNomineeTOList = null;
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            log.error(e);
            System.out.println("e : " + e);
            throw e;
        }
    }

    //--- Inserts the data Record by record in ShareJoint Table
    private void insertJointAccntDetails(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        int jointAccntSize = mapJointAccntTO.size();
        for (int i = 0; i < jointAccntSize; i++) {
            try {
                shareJointTO = (ShareJointTO) mapJointAccntTO.get(String.valueOf(i));
                shareJointTO.setShareAcctNo(objTO.getShareAcctNo());
                sqlMap.executeUpdate("insertShareJointTO", shareJointTO);
                objLogTO.setData(objTO.toString());
                objLogTO.setPrimaryKey(objTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                //                throw new TransRollbackException(e);
                throw e;
            }
        }
    }

    private void getShareAcct_No() throws Exception {
        //        final IDGenerateDAO dao = new IDGenerateDAO();
        HashMap where = new HashMap();
        String shareType = objTO.getShareType();
        where.put("SHARE_TYPE", shareType);
        List accNolist = sqlMap.executeQueryForList("getMinIntialShares", where);
        if (accNolist != null && accNolist.size() > 0) {
            HashMap hash = (HashMap) accNolist.get(0);
            String prefix = CommonUtil.convertObjToStr(hash.get("NUM_PATTERN_PREFIX"));
            int suffix = CommonUtil.convertObjToInt(hash.get("NUM_PATTERN_SUFFIX"));
            String sufixnum = CommonUtil.convertObjToStr(hash.get("NUM_PATTERN_SUFFIX"));
            suffix = suffix + 1;
            String suf = CommonUtil.convertObjToStr(new Integer(suffix));
            String shareNo = prefix + sufixnum;
            where.put("PREFIX", prefix);
            where.put("SUFFIX", suf);
            sqlMap.executeUpdate("updateShareProduct", where);
            objTO.setShareAcctNo(shareNo);
            returnShareNo = shareNo;
        }
        //        where.put(CommonConstants.MAP_WHERE, "SHARE_ACCT_NO");
        //        String ShareAcctNo =  (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        //        objTO.setShareAcctNo(ShareAcctNo);
        //        returnShareNo = ShareAcctNo;
    }

    private void getShareAppl_No() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "SHARE_APPL_NO");
        String ShareApplNo = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        objTO.setTxtApplicationNo(ShareApplNo);
        returnApplNo = ShareApplNo;
    }

    private void updateData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        NomineeDAO objNomineeDAO = new NomineeDAO();
        NomineeDAO objDRFNomineeDAO = new NomineeDAO();//Added By Revathi.L
        ArrayList nomineeTOList = (ArrayList) map.get("AccountNomineeTO");
        ArrayList nomineeDeleteList = (ArrayList) map.get("AccountNomineeDeleteTO");
        ArrayList drfNomineeTOList = (ArrayList) map.get("DRFAccountNomineeTO");
        ArrayList drfNomineeDeleteList = (ArrayList) map.get("DRFAccountNomineeDeleteTO");

        objTO.setStatus(CommonConstants.STATUS_MODIFIED);
//        objTO.setBranchCode(_branchCode);
        try {
//            sqlMap.startTransaction();
            String ratification = objTO.getTxtRatification();
            String no = objTO.getTxtApplicationNo();
            String appNo = "";
            String yes = "Y";
            String SHAREACC = "";
//            objTO.setBranchCode(_branchCode);
//            if(!no.equals(appNo)) {
//                sqlMap.executeUpdate("updateShareAppInfoTO", objTO);
//            } else {
//                 if(map.containsKey("SHARE_STATUS")){
//                       objTO.setStatus("CLOSED");
//                   }
//                sqlMap.executeUpdate("updateShareAccInfoTO", objTO);
//                objTO.setStatus(CommonConstants.STATUS_MODIFIED);
//            }
            objLogTO.setData(objTO.toString());
            objLogTO.setPrimaryKey(objTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            //--- Updates the Share Acct details if its map is not null
//            if(shareAcctDetTOMap!=null){
//                insertShareAcctDetails(objLogDAO, objLogTO,map);   // updateShareAccDetails() method call changed to insertShareAcctDetails()
//            }

            //--- Updates the Joint Account Holder  Details data into the database if the Constitution is Joint_Account.
            if (mapJointAccntTO != null) {
                updateJointAccountDetails(objLogDAO, objLogTO);
            }
            final String USERID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
//            final String SHAREACC = CommonUtil.convertObjToStr(objTO.getShareAcctNo());
            if (ratification.equals(yes)) {
                if(CommonUtil.convertObjToStr(objTO.getShareAcctNo()).length() < 0){// Added by nithya on 19-11-2018 for KD 323 - ISSUE IN SAVING OF SHARE NOMINE DETAILS
                  SHAREACC = CommonUtil.convertObjToStr(objTO.getTxtApplicationNo());  
                }else{
                  SHAREACC = CommonUtil.convertObjToStr(objTO.getShareAcctNo());  
                }                    
            } else {
                SHAREACC = CommonUtil.convertObjToStr(objTO.getShareAcctNo());
            }
            // Update the data regarding the NomineeTable...
            if (nomineeTOList != null) {
                if (nomineeTOList.size() > 0) {
                    objNomineeDAO.deleteData(SHAREACC, SCREEN);
                    objNomineeDAO.insertData(nomineeTOList, SHAREACC, false, USERID, SCREEN, objLogTO, objLogDAO);
                }
            }

            if (nomineeDeleteList != null) {
                if (nomineeTOList.size() <= 0) {
                    objNomineeDAO.deleteData(SHAREACC, SCREEN);
                }
                if (nomineeDeleteList.size() > 0) {
                    objNomineeDAO.insertData(nomineeDeleteList, SHAREACC, true, USERID, SCREEN, objLogTO, objLogDAO);
                }
            }
            
            if (drfNomineeTOList != null) {//Added By Revathi.L
                if (drfNomineeTOList.size() > 0) {
                    objDRFNomineeDAO.deleteData(SHAREACC, SCREEN1);
                    objDRFNomineeDAO.insertData(drfNomineeTOList, SHAREACC, false, USERID, SCREEN1, objLogTO, objLogDAO);
                }
            }

            if (drfNomineeDeleteList != null) {
                if (drfNomineeTOList.size() <= 0) {
                    objDRFNomineeDAO.deleteData(SHAREACC, SCREEN1);
                }
                if (drfNomineeDeleteList.size() > 0) {
                    objDRFNomineeDAO.insertData(drfNomineeDeleteList, SHAREACC, true, USERID, SCREEN1, objLogTO, objLogDAO);
                }
            }

//            sqlMap.commitTransaction();
            nomineeTOList = null;
            nomineeDeleteList = null;
            objNomineeDAO = null;
            //Added By Revathi.L
            drfNomineeTOList = null;
            drfNomineeDeleteList = null;
            objDRFNomineeDAO = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            //            throw new TransRollbackException(e);
            throw e;
        }
    }

    private void updateJointAccountDetails(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        HashMap shareAcctNo = new HashMap();
        shareAcctNo.put("SHARE_ACCT_NO", objTO.getShareAcctNo());
        List list = (List) sqlMap.executeQueryForList("getCountShareJointTO", shareAcctNo);
        HashMap jntMapData = new HashMap();
        jntMapData.put("JointAccountHoldersData", list);
        HashMap JointAccntMap;
        JointAccntMap = (HashMap) ((List) jntMapData.get("JointAccountHoldersData")).get(0);
        int isMore = (int) Integer.parseInt(JointAccntMap.get("COUNT").toString());
        int jntAccntHolderSize = mapJointAccntTO.size();
        for (int i = 0; i < jntAccntHolderSize; i++) {
            try {
                shareJointTO = (ShareJointTO) mapJointAccntTO.get(String.valueOf(i));
                shareJointTO.setShareAcctNo(objTO.getShareAcctNo());
                if (i < isMore) { //--- If the data is exisiting , update the data
                    sqlMap.executeUpdate("updateShareJointTO", shareJointTO);
                    objLogTO.setData(shareJointTO.toString());
                    objLogTO.setPrimaryKey(shareJointTO.getKeyData());
                    objLogDAO.addToLog(objLogTO);
                } else { //--- Else insert the data.
                    sqlMap.executeUpdate("insertShareJointTO", shareJointTO);
                    objLogTO.setData(shareJointTO.toString());
                    objLogTO.setPrimaryKey(shareJointTO.getKeyData());
                    objLogDAO.addToLog(objLogTO);
                }
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                //                throw new TransRollbackException(e);
                throw e;
            }
        }
    }

    //--- Updates the ShareAcctDet table
    /*
     * private void updateShareAccDetails(LogDAO objLogDAO, LogTO objLogTO)
     * throws Exception { HashMap shareAcctNoAndShareAcctDet = new HashMap();
     * shareAcctNoAndShareAcctDet.put("SHARE_ACCT_NO", objTO.getShareAcctNo());
     * shareAcctNoAndShareAcctDet.put("SHARE APPLICATION NO",
     * objTO.getTxtApplicationNo()); List list = (List)
     * sqlMap.executeQueryForList("getShareAcctDetCount",
     * shareAcctNoAndShareAcctDet); HashMap shareAcctDetData = new HashMap();
     * shareAcctDetData.put("shareAcctDetData", list); HashMap shareAcctDetMap;
     * shareAcctDetMap = (HashMap) ((List)
     * shareAcctDetData.get("shareAcctDetData")).get(0); int isMore = (int)
     * Integer.parseInt(shareAcctDetMap.get("COUNT").toString()); int
     * shareAcctDetSize = shareAcctDetTOMap.size(); for(int
     * i=0;i<shareAcctDetSize;i++){ try { shareAcctDetailsTO =
     * (ShareAcctDetailsTO) shareAcctDetTOMap.get(String.valueOf(i));
     * shareAcctDetailsTO.setShareAcctNo(objTO.getShareAcctNo());
     * shareAcctDetailsTO.setTxtApplicationNo(objTO.getTxtApplicationNo());
     * String appno=objTO.getTxtApplicationNo(); String no=""; if(i<isMore){
     * //--- If the data is exisiting , update the data if(!appno.equals(no)){
     * sqlMap.executeUpdate("updateShareAppDetailsTO", shareAcctDetailsTO);
     * }else{ sqlMap.executeUpdate("updateShareAcctDetailsTO",
     * shareAcctDetailsTO); } objLogTO.setData(shareAcctDetailsTO.toString());
     * objLogTO.setPrimaryKey(shareAcctDetailsTO.getKeyData());
     * objLogDAO.addToLog(objLogTO); } else{ //--- Else insert the data.
     * sqlMap.executeUpdate("insertShareAcctDetailsTO", shareAcctDetailsTO);
     * objLogTO.setData(shareAcctDetailsTO.toString());
     * objLogTO.setPrimaryKey(shareAcctDetailsTO.getKeyData());
     * objLogDAO.addToLog(objLogTO); } } catch (Exception e) {
     * sqlMap.rollbackTransaction(); // throw new TransRollbackException(e);
     * throw e; } }
     }
     */
    private void deleteData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        objTO.setStatus(CommonConstants.STATUS_DELETED);
        try {
            sqlMap.startTransaction();
            objTO.setStatusDt(currDt);
            sqlMap.executeUpdate("deleteShareAccInfoTO", objTO);
            if (shareAcctDetTOMap != null) {
                insertShareAcctDetails(objLogDAO, objLogTO, null);   // updateShareAccDetails() method call changed to insertShareAcctDetails()
            }
            sqlMap.commitTransaction();
            objLogTO.setData(objTO.toString());
            objLogTO.setPrimaryKey(objTO.getKeyData());
            objLogDAO.addToLog(objLogTO);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            //            throw new TransRollbackException(e);
            throw e;
        }
    }

    public static void main(String str[]) {
        try {
            //            ShareDAO dao = new ShareDAO();
            //            HashMap map = new HashMap();
            //            map.put("SHARE_ACCT_NO", "SH001034");
            //            dao.executeQuery(map);
            ShareDAO acobj = new ShareDAO();
            acobj.execute(CommonUtil.serializeObjRead("D:\\Share.txt"));
            acobj = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void insertTransactionForDiffAmt(HashMap map,String generateSingleTransId) throws Exception {
        try {
            sqlMap.startTransaction();  
            HashMap txMap = new HashMap();
            double tranAmt = 0.0;
            String transType = "";
            double shareValue = 0.0;
            double changedShare = 0.0;
            String changed = "FALSE";
            ArrayList transferList = new ArrayList();
            TransferTrans objTransferTrans = new TransferTrans();
            objTransferTrans.setInitiatedBranch(_branchCode);
            String det = CommonUtil.convertObjToStr(shareAcctDetailsTO.getShareAcctDetNo());
            String linkBatchId = CommonUtil.convertObjToStr(map.get("SHARE_NO")) + "_" + shareAcctDetailsTO.getShareAcctDetNo();
            HashMap hmap = new HashMap();

            hmap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(map.get("SHARE_NO")) + "_" + map.get("DET_NO"));
            List lst = sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", hmap);
            List cLst1 = sqlMap.executeQueryForList("getSelectShareCashTransactionTO", CommonUtil.convertObjToStr(map.get("SHARE_NO")) + "_" + map.get("DET_NO"));
            TxTransferTO txTransferTO = null;

            double total = 0.0;
            double oldAmount = 0;
            HashMap oldAmountMap = new HashMap();
            LinkedHashMap TransactionMap = (LinkedHashMap) map.get("TransactionTO");
            if (TransactionMap.size() > 0) {
                if (TransactionMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionMap.get("NOT_DELETED_TRANS_TOs");
                    for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                        objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                    }
                }
            }
            HashMap sharehmap = (HashMap) sqlMap.executeQueryForObject("getShareAccDet", shareAcctDetailsTO.getShareAcctNo());
            if (sharehmap != null) {
                shareValue = CommonUtil.convertObjToDouble(sharehmap.get("SHARE_VALUE")).doubleValue();
                changedShare = CommonUtil.convertObjToDouble(shareAcctDetailsTO.getTxtShareValue()).doubleValue();
            }
            if (lst != null && lst.size() > 0) {
                transferList = new ArrayList();
                changed = "AVIALBLE";
                if (map.containsKey("STATUS")) {
                    shareAcctDetailsTO.setStatus(CommonUtil.convertObjToStr(map.get("STATUS")));
                    shareAcctDetailsTO.setCommand(CommonUtil.convertObjToStr(map.get("STATUS")));
                    if (shareAcctDetailsTO.getCommand().equals("DELETE")) {
                        shareAcctDetailsTO.setStatus("DELETED");
                        shareAcctDetailsTO.setShareAcctDetNo(CommonUtil.convertObjToStr(map.get("DET_NO")));
                        shareAcctDetailsTO.setAuthorize(CommonConstants.STATUS_REJECTED);
                        //shareAcctDetailsTO.setAuthorizeBy(changed);
                        shareAcctDetailsTO.setAuthorizeDt(currDt);
                        sqlMap.executeUpdate("deleteShareAcctDetailsTO", shareAcctDetailsTO);
                        HashMap updateShareAcctMap = new HashMap();
                        updateShareAcctMap.put("AUTHORIZE_STATUS",CommonConstants.STATUS_AUTHORIZED);
                        updateShareAcctMap.put("AUTHORIZE_BY",shareAcctDetailsTO.getStatusBy());
                        updateShareAcctMap.put("AUTHORIZE_DT",currDt);
                        updateShareAcctMap.put("SHARE_ACCT_NO",shareAcctDetailsTO.getShareAcctNo());
                        sqlMap.executeUpdate("updateShareAcctTable", updateShareAcctMap);
                    } else {
                        sqlMap.executeUpdate("updateShareAcctDetailsTO", shareAcctDetailsTO);
                    }

                }
                if ((shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE) && (objTransactionTO.getTransType().equals("TRANSFER") || shareValue != changedShare)) || shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                    for (int j = 0; j < lst.size(); j++) {

                        txTransferTO = (TxTransferTO) lst.get(j);
                        if (shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                            total = total;
                            total = CommonUtil.convertObjToDouble(txTransferTO.getAmount()).doubleValue();
                            oldAmount = txTransferTO.getAmount().doubleValue();
                            txTransferTO.setInpAmount(new Double(total));
                            txTransferTO.setAmount(new Double(total));
                            oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                            txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                            txTransferTO.setStatusDt(currDt);
                        } else {
                            //                                    txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                            txTransferTO.setAuthorizeStatus(CommonConstants.STATUS_REJECTED);
                            txTransferTO.setAuthorizeDt(currDt);
                            txTransferTO.setAuthorizeRemarks("SHARE_ACHD");
                            txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                            txTransferTO.setStatusDt(currDt);
                        }
                        
                        txTransferTO.setSingleTransId(generateSingleTransId);
                        txTransferTO.setTransModType("SH");
                        transferList.add(txTransferTO);
                        //                                    }
                    }
                    TransferTrans transferTrans = new TransferTrans();
                    transferTrans.setOldAmount(oldAmountMap);
                    transferTrans.setInitiatedBranch(_branchCode);
                    transferTrans.doDebitCredit(transferList, _branchCode, false, "DELETED");
                    transferTrans = null;
                    changed = "TRUE";
                }
            } else if (cLst1 != null && cLst1.size() > 0) {
                CashTransactionTO txTransferTO1 = null;
                changed = "AVIALBLE";
                transferList = new ArrayList();
                if (map.containsKey("STATUS")) {
                    shareAcctDetailsTO.setStatus(CommonUtil.convertObjToStr(map.get("STATUS")));
                    shareAcctDetailsTO.setCommand(CommonUtil.convertObjToStr(map.get("STATUS")));
                    if (shareAcctDetailsTO.getCommand().equals("DELETE")) {
                        shareAcctDetailsTO.setStatus("DELETED");
                        shareAcctDetailsTO.setAuthorizeDt(currDt);
                        shareAcctDetailsTO.setAuthorize(CommonConstants.STATUS_REJECTED);
                        shareAcctDetailsTO.setShareAcctDetNo(CommonUtil.convertObjToStr(map.get("DET_NO")));
                        sqlMap.executeUpdate("deleteShareAcctDetailsTO", shareAcctDetailsTO);
                        HashMap updateShareAcctMap = new HashMap();
                        updateShareAcctMap.put("AUTHORIZE_STATUS", CommonConstants.STATUS_AUTHORIZED);
                        updateShareAcctMap.put("AUTHORIZE_BY", shareAcctDetailsTO.getStatusBy());
                        updateShareAcctMap.put("AUTHORIZE_DT", currDt);
                        updateShareAcctMap.put("SHARE_ACCT_NO", shareAcctDetailsTO.getShareAcctNo());
                        sqlMap.executeUpdate("updateShareAcctTable", updateShareAcctMap);
                    } else {
                        sqlMap.executeUpdate("updateShareAcctDetailsTO", shareAcctDetailsTO);
                    }
                }

                if ((shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE) && (objTransactionTO.getTransType().equals("CASH") || shareValue != changedShare)) || shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                    for (int l = 0; l < cLst1.size(); l++) {
                        txTransferTO1 = (CashTransactionTO) cLst1.get(l);
                        if (shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                            oldAmount = CommonUtil.convertObjToDouble(txTransferTO1.getAmount()).doubleValue();
                            double newAmount = 0.0;
                            newAmount = CommonUtil.convertObjToDouble(txTransferTO1.getAmount()).doubleValue();
                            oldAmount = txTransferTO1.getAmount().doubleValue();
                            txTransferTO1.setInpAmount(new Double(newAmount));
                            txTransferTO1.setAmount(new Double(newAmount));
                            txTransferTO1.setCommand("DELETE");
                            txTransferTO1.setStatus(CommonConstants.STATUS_DELETED);
                            txTransferTO1.setStatusDt(currDt);
                        } else {
                            txTransferTO1.setCommand("DELETE");
                            txTransferTO1.setAuthorizeStatus(CommonConstants.STATUS_REJECTED);
                            txTransferTO1.setAuthorizeDt(currDt);
                            txTransferTO1.setAuthorizeRemarks("SHARE_ACHD");
                            txTransferTO1.setStatus(CommonConstants.STATUS_DELETED);
                            txTransferTO1.setStatusDt(currDt);
                        }
                        txTransferTO1.setSingleTransId(generateSingleTransId);
                        txTransferTO1.setTransModType("SH");
                        transferList.add(txTransferTO1);
                    }
                    map.put(CommonConstants.PRODUCT_ID, TransactionFactory.GL);
                    map.put("PRODUCTTYPE", TransactionFactory.GL);
                    map.put("OLDAMOUNT", new Double(oldAmount));
                    map.put("DAILYDEPOSITTRANSTO", transferList);
                    map.put("SCREEN_NAME", "Share Account");
                    CashTransactionDAO cashTransDAO = new CashTransactionDAO();
                    cashTransDAO.execute(map, false);
                    changed = "TRUE";
                }
            } else {
                HashMap shareAcctNoMap = new HashMap();
                HashMap detNoMap = new HashMap();
                shareAcctNoMap.put("SHARE_ACCT_NO", map.get("SHARE_NO"));
                List getDetnoList = sqlMap.executeQueryForList("getLatestShareAcctDetNo", shareAcctNoMap);
                if (getDetnoList != null && getDetnoList.size() > 0) {
                    detNoMap = (HashMap) getDetnoList.get(0);
					if (detNoMap != null & detNoMap.containsKey("SHARE_ACCT_DET_NO")) {
	                    double shareDetNO = CommonUtil.convertObjToDouble(detNoMap.get("SHARE_ACCT_DET_NO"));
	                    shareDetNO+=1;
	                    shareAcctDetailsTO.setShareAcctDetNo(CommonUtil.convertObjToStr(shareDetNO));
	                }
            	}                
                sqlMap.executeUpdate("insertShareAcctDetailsTO", shareAcctDetailsTO);
            }
            if (shareAcctDetailsTO.getCommand().equals("DELETE")) {
                if (TransactionMap.size() > 0) {
                    if (TransactionMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                        LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionMap.get("NOT_DELETED_TRANS_TOs");
                        if (allowedTransDetailsTO.size() > 0) {
                            shareAcctDetailsTO.setShareAcctDetNo(det);
                            shareAcctDetailsTO.setStatus("CREATED");
                            HashMap shareAcctNoMap = new HashMap();
                            HashMap detNoMap = new HashMap();
                            shareAcctNoMap.put("SHARE_ACCT_NO", map.get("SHARE_NO"));
                            List getDetnoList = sqlMap.executeQueryForList("getLatestShareAcctDetNo", shareAcctNoMap);
                            if (getDetnoList != null && getDetnoList.size() > 0) {
                                detNoMap = (HashMap) getDetnoList.get(0);
                                if (detNoMap != null & detNoMap.containsKey("SHARE_ACCT_DET_NO")) {
                                    double shareDetNO = CommonUtil.convertObjToDouble(detNoMap.get("SHARE_ACCT_DET_NO"));
                                    shareDetNO += 1;
                                    shareAcctDetailsTO.setShareAcctDetNo(CommonUtil.convertObjToStr(shareDetNO));
                                }
                            }
                            sqlMap.executeUpdate("insertShareAcctDetailsTO", shareAcctDetailsTO);
                        }
                    }
                }
            }
            ArrayList cashList = null;
            objTransferTrans.setLinkBatchId(CommonUtil.convertObjToStr(map.get("SHARE_NO")) + "_" + shareAcctDetailsTO.getShareAcctDetNo());
            LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
            String sharetype = CommonUtil.convertObjToStr(map.get("SHARE_TYPE"));
            shareType = sharetype;
            HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getShareSuspenseAcHd", sharetype);
//                  HashMap DRF_APPLICABLE=new HashMap();
//                  double drfAmt=0.0;
//                  String drfHead="";
//                  String Split_needed="N";
//                  System.out.println("here mappp"+map);
//                 if(map.containsKey("DRF_APPLICABLE"))
//                 { 
//                    
//               DRF_APPLICABLE=(HashMap)map.get("DRF_APPLICABLE");
//                    drfAmt= Double.parseDouble(DRF_APPLICABLE.get("DRF_AMOUNT").toString());
//                     System.out.println("DRF_APPLICABLE"+DRF_APPLICABLE);
//                  HashMap aMap=new HashMap();
//                  aMap.put("SHARE_TYPE","AA");
//                    List aList=sqlMap.executeQueryForList("getDRFAccountDts", aMap);
//                    aMap=(HashMap)aList.get(0);
//                    drfHead=aMap.get("DRF_SUSPENSE_HEAD").toString();
//                    Split_needed=aMap.get("SPLIT_DRF").toString();
//                 }
//                  
//                 
            if ((TransactionDetailsMap.size() > 0 && changed.equals("TRUE")) || (TransactionDetailsMap.size() > 0 && changed.equals("FALSE"))) {
                if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                    for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                        objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                        transferList = new ArrayList();
                        if (objTransactionTO.getTransType().equals("TRANSFER")) {
                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SHARE_ACHD"));
                            txMap.put(TransferTrans.CURRENCY, LocaleConstants.DEFAULT_CURRENCY);
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, "DUE_AMOUNT");
                            txMap.put("AUTHORIZEREMARKS", "TRANSACTIONAMOUNT");
                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                            txMap.put("TRANS_MOD_TYPE", "SH");
                            txMap.put("SCREEN_NAME", "Share Account");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT"); // Added by nithya on 15-01-2019 for KD 375 - share showing authorozation pending
                            tranAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
//                                if(Split_needed.equals("Y"))
//                                     {
//                                        tranAmt=tranAmt-drfAmt;
//                                        System.out.println("trannsasammmyy and all "+tranAmt+" vxcv  "+drfAmt);
//                                    }

                            txMap.put("generateSingleTransId",generateSingleTransId);//KD-3864 : Share differential transaction

                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, tranAmt));
                            HashMap interBranchCodeMap = new HashMap();
                            interBranchCodeMap.put("ACT_NUM", CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                            List interBranchCodeList = sqlMap.executeQueryForList("getSelectInterBranchCode", interBranchCodeMap);
                            if (interBranchCodeList != null && interBranchCodeList.size() > 0) {
                                interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                                txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE")));
                            } else {
                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                            }
                            txMap.put(TransferTrans.DR_PROD_TYPE, objTransactionTO.getProductType());
                            txMap.put("AUTHORIZEREMARKS", "DUEAMOUNT");
                            txMap.put(TransferTrans.PARTICULARS, "DUE_AMOUNT");
                            txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                            txMap.put("TRANS_MOD_TYPE", "SH");
                            if (objTransactionTO.getProductType().equals("GL")) {
                                txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                            } else {
                                txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                            }
                            txMap.put("generateSingleTransId",generateSingleTransId);
                            txMap.put("SCREEN_NAME", "Share Account");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT"); // Added by nithya on 15-01-2019 for KD 375 - share showing authorozation pending
                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, tranAmt));
                            if (transferList != null && transferList.size() > 0) {
                                objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                            }

                        } else if (objTransactionTO.getTransType().equals("CASH")) {
                            HashMap dueCashMap = new HashMap();
                            cashList = new ArrayList();
                            tranAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
//                               if(Split_needed.equals("Y"))
//                                     {
//                                        tranAmt=tranAmt-drfAmt;
//                                         System.out.println("trannsasammmyy and all "+tranAmt+" vxcv  "+drfAmt);
//                                    }
                            dueCashMap.put("DIFFERENTIAL_TRANSACTION", "DIFFERENTIAL_TRANSACTION");
                            dueCashMap.put("SELECTED_BRANCH_ID", objTO.getBranchCode());
                            //dueCashMap.put("SELECTED_BRANCH_ID", _branchCode);
                            dueCashMap.put("BRANCH_CODE", _branchCode);
                            dueCashMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                            dueCashMap.put("USER_ID", map.get("USER_ID"));
                            dueCashMap.put("TRANS_AMOUNT", new Double(tranAmt));
                            dueCashMap.put("AUTHORIZEREMARKS", "DUE_AMOUNT");
                            dueCashMap.put("ACCT_HEAD", (String) acHeads.get("SHARE_ACHD"));
                            dueCashMap.put("SHARE_NO", map.get("SHARE_NO"));
                            dueCashMap.put("TRANS_MOD_TYPE", "SH");
                            dueCashMap.put("generateSingleTransId",generateSingleTransId);
                            cashList.add(createCashTransactionTO(dueCashMap));

                        }

                        /////////////////////////////////////////////////////////////////////////////////

//                             if(Split_needed.equals("Y"))
//                             {
//                                if (objTransactionTO.getTransType().equals("TRANSFER")) {
//                                 txMap.put(TransferTrans.CR_AC_HD, drfHead);
//                                 txMap.put(TransferTrans.CURRENCY, LocaleConstants.DEFAULT_CURRENCY);
//                                 txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
//                                 txMap.put(TransferTrans.PARTICULARS, "DUE_AMOUNT");
//                                 txMap.put("AUTHORIZEREMARKS","TRANSACTIONAMOUNT");
//                                 txMap.put(TransferTrans.CR_BRANCH, _branchCode);
//                                 tranAmt=CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
//                                 transferList.add(objTransferTrans.getCreditTransferTO(txMap,tranAmt));                                                                                                                                                             txMap.put(TransferTrans.DR_BRANCH, _branchCode);
//                                 txMap.put(TransferTrans.DR_PROD_TYPE, objTransactionTO.getProductType());
//                                 txMap.put("AUTHORIZEREMARKS","DUEAMOUNT");
//                                 txMap.put(TransferTrans.PARTICULARS, "DUE_AMOUNT");
//                                 txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
//                                 if (objTransactionTO.getProductType().equals("GL")) {
//                                     txMap.put(TransferTrans.DR_AC_HD,CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
//                                     
//                                 } else {
//                                     txMap.put(TransferTrans.DR_ACT_NUM,CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
//                                    txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
//                                }
//                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, tranAmt));
//                                System.out.println("transferList  ------------->"+transferList);
//                                if (transferList!=null && transferList.size()>0) {
//                                    objTransferTrans.doDebitCredit(transferList, _branchCode, false);
//                                }
//                                
//                            }else if(objTransactionTO.getTransType().equals("CASH")){
//                                HashMap dueCashMap = new HashMap();
//                                cashList= new ArrayList();
//                                tranAmt=CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
//                                dueCashMap.put("SELECTED_BRANCH_ID", _branchCode);
//                                dueCashMap.put("BRANCH_CODE", _branchCode);
//                                dueCashMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
//                                dueCashMap.put("USER_ID",map.get("USER_ID"));
//                                dueCashMap.put("TRANS_AMOUNT", new Double(tranAmt));
//                                dueCashMap.put("AUTHORIZEREMARKS","DUE_AMOUNT");
//                                dueCashMap.put("ACCT_HEAD", drfHead);
//                                dueCashMap.put("SHARE_NO",map.get("SHARE_NO"));
//                                cashList.add(createCashTransactionTO(dueCashMap));
//                                
//                            }
//                             
//                             
//                             
//                             } 
//                             
//                             
//                             
//                             
//                             


                        //////////////////////////////////////////////////////////////////////////////////


                        if (cashList != null && cashList.size() > 0) {
                            HashMap tranMap = new HashMap();
                            tranMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                            tranMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                            tranMap.put("INITIATED_BRANCH", _branchCode);
                            tranMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                            tranMap.put("DAILYDEPOSITTRANSTO", cashList);
                            tranMap.put("SCREEN_NAME", "Share Account");
                            CashTransactionDAO cashDao;
                            cashDao = new CashTransactionDAO();
                            tranMap = cashDao.execute(tranMap, false);
                        }
                        objTransactionTO.setBatchId(CommonUtil.convertObjToStr(map.get("SHARE_NO")));
                        objTransactionTO.setBatchDt(currDt);
                        objTransactionTO.setTransId(objTransferTrans.getLinkBatchId());
                        objTransactionTO.setStatus("CREATED");
                        objTransactionTO.setBranchId(_branchCode);
                        if (!shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                            sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);
                        }
                    }
                    if (shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                        objTransactionTO.setStatus("MODIFIED");
                        sqlMap.executeUpdate("updateRemittanceIssueTransactionTO", objTransactionTO);
                    } else if (shareAcctDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                        objTransactionTO.setStatus("DELETED");
                        objTransactionTO.setTransId(CommonUtil.convertObjToStr(map.get("SHARE_NO") + "_" + map.get("DET_NO")));
                        objTransactionTO.setBatchId(CommonUtil.convertObjToStr(map.get("SHARE_NO")));
                        sqlMap.executeUpdate("updateRemittanceIssueTransactionTO", objTransactionTO);
                    }
                    HashMap cashAuthMap = new HashMap();
                    cashAuthMap.put(CommonConstants.BRANCH_ID, map.get("BRANCH_CODE"));
                    cashAuthMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                    getTransDetails(objTransferTrans.getLinkBatchId());
                    //                            TransactionDAO.authorizeCashAndTransfer(linkBatchId, "AUTHORIZED", cashAuthMap);
                }
            }
           sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            log.error(e);
            System.out.println("e : " + e);
            throw e;
        }
    }
    
    public HashMap execute(HashMap map) throws Exception {
        //CommonUtil.serializeObjWrite("D:\\share.txt", map);
        execReturnMap = new HashMap();
        System.out.println("#### inside shareDAO execute() map : " + map);
        LogDAO objLogDAO = new LogDAO();
        ArrayList delshare = new ArrayList();
        drfMap = new HashMap();
        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        branchId = objLogTO.getBranchId();
        objLogTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        objLogTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        objLogTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
        generateSingleTransId = generateLinkID();
        ibrHierarchy = 1;
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        transactionDAO.setInitiatedBranch(_branchCode);

        if (map.containsKey("ShareResolutionInfo")) {
            doCentralOfficeShareSuspense(map,generateSingleTransId);
        }
        if (map.containsKey("COLLECTING_DUE_AMOUNT")) {
            if (map.containsKey("SHARE_ACC_INFO")) {
                objTO = (ShareAccInfoTO) map.get("SHARE_ACC_INFO");
                shareAcctDetTOMap = (LinkedHashMap) map.get("SHARE_ACCT_DETAILS");
                shareAcctDetailsTO = (ShareAcctDetailsTO) shareAcctDetTOMap.get(String.valueOf(1));
                sqlMap.executeUpdate("updateShareAccInfoTO", objTO);

            }
            if (map.containsKey("TransactionTO")) {
                insertTransactionForDiffAmt(map,generateSingleTransId);

            }
            return execReturnMap;
        }
        if (map.containsKey("ShareAccInfo")) {
            objTO = (ShareAccInfoTO) map.get("ShareAccInfo");
            shareAcctDetTOMap = (LinkedHashMap) map.get("ShareAccDet");
            mapJointAccntTO = (LinkedHashMap) map.get("JointAccntTO");
            final TOHeader toHeader = objTO.getTOHeader();
            command = objTO.getCommand();
            //--- Selects the method according to the Command type
            if (command.equals(CommonConstants.TOSTATUS_INSERT) || command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                String appno = objTO.getTxtApplicationNo();
                String accno = objTO.getShareAcctNo();

                String Accno = "";
                String Appno = "";
                String noofshare = "";
                String sharedet = "";
                String sharefrom = "";

                //                if(command.equals(CommonConstants.TOSTATUS_UPDATE )){
                //
                //                    List list=null;
                //                    if(!appno.equals(Appno)){
                //                        HashMap hmap=new HashMap();
                //                        hmap=  (HashMap) sqlMap.executeQueryForObject("getShareAppDet",appno);
                //                        if(hmap!=null){
                //                            noofshare=CommonUtil.convertObjToStr(hmap.get("NO_OF_SHARES"));
                //                            sharedet=CommonUtil.convertObjToStr(hmap.get("SHARE_ACCT_DET_NO"));
                //                            sharefrom=CommonUtil.convertObjToStr(hmap.get("SHARE_NO_FROM"));
                //                            System.out.println("no of share app"+ noofshare);
                //                            System.out.println("share det app"+ sharedet);
                //                        }
                //                    }else {
                //                        HashMap hmap=new HashMap();
                //                        hmap=  (HashMap) sqlMap.executeQueryForObject("getShareAccDet",accno);
                //                        if(hmap!=null){
                //                            noofshare=CommonUtil.convertObjToStr(hmap.get("NO_OF_SHARES"));
                //                            sharedet=CommonUtil.convertObjToStr(hmap.get("SHARE_ACCT_DET_NO"));
                //                            sharefrom=CommonUtil.convertObjToStr(hmap.get("SHARE_NO_FROM"));
                //                            System.out.println("no of share acc"+ noofshare);
                //                            System.out.println("share det acc"+ sharedet);
                //                        }
                //                    }
                //                    int sizeShareAcctDet = shareAcctDetTOMap.size();
                //                    for(int i=0;i<sizeShareAcctDet;i++){
                //                        appno= objTO.getTxtApplicationNo();
                //                        accno= objTO.getShareAcctNo();
                //                        System.out.println(" sizeShareAcctDet"+ sizeShareAcctDet);
                //                        shareAcctDetailsTO = (ShareAcctDetailsTO) shareAcctDetTOMap.get(String.valueOf( sizeShareAcctDet-1));
                //                        String detno=shareAcctDetailsTO.getShareAcctDetNo();
                //                        String noshare=shareAcctDetailsTO.getTxtNoShares();
                //                        String shrFrom=shareAcctDetailsTO.getShareNoFrom();
                //
                //                        if(!appno.equals(Appno) && (!noofshare.equals(noshare)) ){
                //                            if( noofshare.length()>0){
                //
                //
                //                                appno=appno+"_"+sharedet;
                //                                sqlMap.executeUpdate("getUpdateTransDetailsForApp",appno);
                //                                sqlMap.executeUpdate("getUpdateCashDetailsForApp",appno);
                //                                sqlMap.executeUpdate("getUpdateRemitDetailsForApp",appno);
                //
                //                            }
                //                        }
                //                        else  if((!accno.equals(Accno))&& ((!noofshare.equals(noshare) )|| (!sharefrom.equals(shrFrom) ))){
                //
                //                            //hmap.put("SHARE ACCOUNT NO",accno);
                //                            if( noofshare.length()>0){
                //
                //                                //hmap=null;
                //                                accno= accno +"_"+sharedet;
                //                                sqlMap.executeUpdate("getUpdateRemitDetailsForAcc",accno);
                //                                // hmap.put("SHARE ACCOUNT NO",accno);
                //                                sqlMap.executeUpdate("getUpdateTransDetailsForAcc",accno);
                //                                sqlMap.executeUpdate("getUpdateCashDetailsForAcc",accno);
                //                            }
                //                        }
                //
                //                    }
                //                    if(map.containsKey("DeletedShareDet") && map.containsKey("DeletedTrans")){
                //                         sqlMap.executeUpdate("getUpdateShareAcctDet",accno);
                //                        accno= accno +"_"+sharedet;
                //                        sqlMap.executeUpdate("getUpdateTransDetailsForApp",accno);
                //                        sqlMap.executeUpdate("getUpdateCashDetailsForApp",accno);
                //                        sqlMap.executeUpdate("getUpdateRemitDetailsForApp",accno);
                //
                //                    }
                //                }
                insertData(map, objLogDAO, objLogTO,generateSingleTransId);
                //                transactionDAO.execute(map);
                String ratification = objTO.getTxtRatification();
                String yes = "Y";
                String shareno = "";
                String applicationNo = objTO.getTxtApplicationNo();
                String accountNo = objTO.getShareAcctNo();
                if (ratification.equals(yes)) {
                    if (!applicationNo.equals(Appno)) {
                        if (!applicationNo.equals(appno)) {
                            execReturnMap.put(CommonConstants.TRANS_ID, returnApplNo);
                        }
                    }
                } else {
                    if (!accountNo.equals(Accno)) {
                        if (!accountNo.equals(accno)) {
                            execReturnMap.put(CommonConstants.TRANS_ID, returnShareNo);
                        }
                    }
                }
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData(map, objLogDAO, objLogTO);

            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData(objLogDAO, objLogTO);

            } else {
                throw new NoCommandException();
            }
            objSMSSubscriptionTO = null;
            if (map.containsKey("SMSSubscriptionTO")) {
                objSMSSubscriptionTO = (SMSSubscriptionTO) map.get("SMSSubscriptionTO");
            	if (objSMSSubscriptionTO != null) {
        	        objSMSSubscriptionTO.setActNum(objTO.getShareAcctNo());
    	            updateSMSSubscription();
	            }
            }

            destroyObjects();
            System.out.println("the map is" + execReturnMap);
            return execReturnMap;
        }

        if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            drfMap = map;
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            authMap.put(CommonConstants.BRANCH_ID, (String) map.get(CommonConstants.BRANCH_ID));
            if (map.containsKey("SERVICE_TAX_AUTH")) {
                authMap.put("SERVICE_TAX_AUTH", "SERVICE_TAX_AUTH");
            }
            if (authMap != null) {
                authorize(authMap);
            }
        }
        return null;
    }

    private CashTransactionTO createCashTransactionTO(HashMap dataMap) throws Exception {
        CashTransactionTO objCashTO = new CashTransactionTO();
        if (dataMap.get("STATUS").equals(CommonConstants.TOSTATUS_INSERT)) {
            objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
            objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.GL));
            objCashTO.setTransType(CommonConstants.CREDIT);
            objCashTO.setParticulars("By Cash : " + "DUE_AMOUNT" + dataMap.get("SHARE_NO"));
            objCashTO.setAuthorizeRemarks(CommonUtil.convertObjToStr(dataMap.get("AUTHORIZEREMARKS")));
            objCashTO.setInpAmount(CommonUtil.convertObjToDouble(dataMap.get("TRANS_AMOUNT")));
            objCashTO.setAmount(CommonUtil.convertObjToDouble(dataMap.get("TRANS_AMOUNT")));
            objCashTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            if(dataMap.containsKey("DIFFERENTIAL_TRANSACTION")){
                objCashTO.setBranchId(CommonUtil.convertObjToStr(dataMap.get("SELECTED_BRANCH_ID")));
            }else{
                objCashTO.setBranchId(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
            }
            objCashTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            objCashTO.setStatusDt(ServerUtil.getCurrentDate(_branchCode));
            objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            objCashTO.setScreenName("Share Account");
            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
            objCashTO.setSingleTransId(CommonUtil.convertObjToStr(dataMap.get("generateSingleTransId")));
            objCashTO.setCommand("INSERT");
            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("SHARE_NO")) + "_" + shareAcctDetailsTO.getShareAcctDetNo());
            System.out.println("objCashTO 1st one:" + objCashTO);
        }
        return objCashTO;
    }

    private void doCentralOfficeShareSuspense(HashMap map,String generateSingleTransId) throws Exception {
        ArrayList cOList = (ArrayList) map.get("ShareResolutionInfo");
        HashMap cOMap = new HashMap();
        HashMap transMap;
        TransferTrans trans;
        ArrayList batchList;
        for (int i = 0; i < cOList.size(); i++) {
            cOMap = (HashMap) cOList.get(i);
            shareType = (String) cOMap.get("SHARETYPE");
            HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getShareSuspenseAcHd", (String) cOMap.get("SHARETYPE"));
            double transAmt = new Double((String) cOMap.get("AMOUNT")).doubleValue();
            transMap = new HashMap();
            transMap.put(TransferTrans.DR_BRANCH, ServerConstants.HO == null ? "" : ServerConstants.HO);
            transMap.put(TransferTrans.CURRENCY, LocaleConstants.DEFAULT_CURRENCY);
            transMap.put(TransferTrans.DR_AC_HD, getIBRAcctHead());
            transMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            transMap.put("TRANS_MOD_TYPE","SH");
            transMap.put("generateSingleTransId",generateSingleTransId);
            trans = new TransferTrans();
            trans.setInitiatedBranch((String) map.get(CommonConstants.BRANCH_ID));
            batchList = new ArrayList();
            batchList.add(trans.getDebitTransferTO(transMap, transAmt));
            transMap = null;

            transMap = new HashMap();
            transMap.put(TransferTrans.CR_BRANCH, ServerConstants.HO == null ? "" : ServerConstants.HO);
            transMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SHARE_SUSPENSE_ACHD"));
            transMap.put(TransferTrans.CURRENCY, LocaleConstants.DEFAULT_CURRENCY);
            transMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            transMap.put("TRANS_MOD_TYPE", "SH");
            transMap.put("generateSingleTransId",generateSingleTransId);
            batchList.add(trans.getCreditTransferTO(transMap, transAmt));
            transMap = null;
            System.out.println("#### Inside shareDAO doCentralOfficeShareSuspense() batchList : " + batchList);
            trans.doDebitCredit(batchList, (String) map.get(CommonConstants.BRANCH_ID));
            acHeads = null;
            batchList = null;
            trans = null;
        }
        cOList = null;
        cOMap = null;
    }

    private ShareAccInfoTO getShareAccInfoData(String shareAcctNo) throws Exception {
        List list = (List) sqlMap.executeQueryForList("getShareAccInfoData", shareAcctNo);
        return ((ShareAccInfoTO) list.get(0));
    }

    //private void doAuthorize(HashMap map) throws Exception{
    //        //Test Code....
    //        /*
    //        FileOutputStream out;
    //        out = new FileOutputStream("D:\\myfile1.txt");
    //        ObjectOutputStream dataOut = new ObjectOutputStream(out);
    //        dataOut.writeObject(map);
    //        dataOut.flush();
    //        dataOut.close();
    //        */
    //        //End of Test Code
    //        HashMap singleAuthorizeMap ;
    //        String linkBatchId = null;
    //        HashMap cashAuthMap ;
    //        String authorizeStatus ;
    //        HashMap authMap = (HashMap)map.get(CommonConstants.AUTHORIZEMAP) ;
    //        List authData = (List)authMap.get(CommonConstants.AUTHORIZEDATA);
    //        for(int i = 0, j = authData.size(); i < j ; i++){
    //            linkBatchId = CommonUtil.convertObjToStr(((HashMap)authData.get(i)).get("ACCOUNTNO"));//Transaction Batch Id
    //            singleAuthorizeMap = new HashMap();
    //            authorizeStatus =  CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS));
    //            singleAuthorizeMap.put("STATUS", authorizeStatus);
    //            singleAuthorizeMap.put("ACCOUNTNO", linkBatchId);
    //            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
    //            singleAuthorizeMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
    //            singleAuthorizeMap.put("AUTHORIZEDT", ServerUtil.getCurrentDate(_branchCode));
    //
    //            sqlMap.executeUpdate("authorizeUpdateAccountCloseTO", singleAuthorizeMap);
    //            singleAuthorizeMap = null ;
    //            //Separation of Authorization for Cash and Transfer
    //            //Call this in all places that need Authorization for Transaction
    //            cashAuthMap = new HashMap();
    //            cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
    //            cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
    //            TransactionDAO.authorizeCashAndTransfer(linkBatchId, authorizeStatus, cashAuthMap);
    //            cashAuthMap = null ;
    //        }
    //    }
    private void authorize(HashMap map) throws Exception {
        String status = (String) map.get(CommonConstants.AUTHORIZESTATUS);
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap dataMap = new HashMap();
        String shareAcctNo="";
        String shareNo = "";
        String shareAppno = "";
        ShareAccInfoTO objTO = null;
        String linkBatchId = null;
        HashMap cashAuthMap;
        String share = "";
        String shareapp = "";
        HashMap returnDrfMap = new HashMap();
        try {
            sqlMap.startTransaction();
            for (int i = 0; i < selectedList.size(); i++) {
                dataMap = (HashMap) selectedList.get(i);
                shareAcctNo = CommonUtil.convertObjToStr(dataMap.get("SHARE ACCOUNT NO"));
                shareAppno = CommonUtil.convertObjToStr(dataMap.get("SHARE APPLICATION NO"));
                dataMap.put(CommonConstants.STATUS, status);
                dataMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                dataMap.put("CURR_DATE", currDt);
                if (status.equals("REJECTED") && CommonUtil.convertObjToInt(dataMap.get("SHARE DETAIL NO")) > 1) {
                    String status1 = CommonConstants.STATUS_AUTHORIZED;
                    dataMap.put(CommonConstants.STATUS, status1);
                    status1 = "";
                }
                dataMap.put(CommonConstants.STATUS, status);

                // The following block commented by Rajesh
                //                if(status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)){
                //                    objTO = getShareAccInfoData(shareAcctNo);
                //                    if(objTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED) || objTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_MODIFIED)){
                //                        authorizeUpdate(objTO, dataMap);
                //                    }else if(status.equalsIgnoreCase(CommonConstants.STATUS_REJECTED)){
                //                        objTO = getShareAccInfoData(shareAcctNo);
                //                        if(objTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED) ||
                //                        objTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_MODIFIED) ){
                //                            sqlMap.executeUpdate("authorizeShareAccInfo", dataMap);
                //                            sqlMap.executeUpdate("authorizeShareAccDet", dataMap);
                //                        }
                //                    }
                //                }

                if (!shareAppno.equals(shareapp) && !shareAcctNo.equals(shareNo)) {
                    if (status.equals("REJECTED")) {
                        shareAppno = CommonUtil.convertObjToStr(dataMap.get("SHARE APPLICATION NO"));
                        List lst = sqlMap.executeQueryForList("getShareAccountdetailsforAcct", dataMap);
                        if (lst.size() == 1) {
                            sqlMap.executeUpdate("authorizeShareAccInfoStatus", dataMap);
                        } else {
                            sqlMap.executeUpdate("authorizeShareAcc", dataMap);
                        }
                    } else {
                        sqlMap.executeUpdate("authorizeShareAccInfo", dataMap);
                        if (CommonConstants.SAL_REC_MODULE != null && !CommonConstants.SAL_REC_MODULE.equals("") && CommonConstants.SAL_REC_MODULE.equals("Y")) {
                            HashMap newMap = new HashMap();
                            newMap.put("SHARE_ACT_NO", shareAcctNo);
                            newMap.put("STATUS", status);
                            updateEmpRefNo(newMap);
                        }
                    }
                    dataMap.put(CommonConstants.STATUS, status);
                    if (status.equals("REJECTED")) {
                        sqlMap.executeUpdate("rejectedCustomer", shareAcctNo);
                    }
                    sqlMap.executeUpdate("authorizeShareAccDet", dataMap);
                    linkBatchId = shareAcctNo + "_" + CommonUtil.convertObjToStr(dataMap.get("SHARE DETAIL NO"));//Transaction Batch Id
                } else if (!shareAcctNo.equals(shareNo)) {
                    sqlMap.executeUpdate("authorizeShareAccInfo", dataMap);
                    if (CommonConstants.SAL_REC_MODULE != null && !CommonConstants.SAL_REC_MODULE.equals("") && CommonConstants.SAL_REC_MODULE.equals("Y")) {
                        HashMap newMap = new HashMap();
                        newMap.put("SHARE_ACT_NO", shareAcctNo);
                        newMap.put("STATUS", status);
                        updateEmpRefNo(newMap);
                    }
                    sqlMap.executeUpdate("authorizeShareAccDet", dataMap);
                    if (status.equals("REJECTED")) {
                        sqlMap.executeUpdate("rejectedCustomer", shareAcctNo);
                    }
                     if (map.containsKey("SERVICE_TAX_AUTH")) {
                        HashMap servTaxWhr = new HashMap();
                        servTaxWhr.put("STATUS", (String) map.get(CommonConstants.AUTHORIZESTATUS));
                        servTaxWhr.put("USER_ID", map.get(CommonConstants.USER_ID));
                        servTaxWhr.put("AUTHORIZEDT", currDt);
                        servTaxWhr.put("ACCT_NUM", shareAcctNo);
                        sqlMap.executeUpdate("authorizeServiceTaxDetails", servTaxWhr);
                    }
                    linkBatchId = shareAcctNo + "_" + CommonUtil.convertObjToStr(dataMap.get("SHARE DETAIL NO"));
                } else {
                    if (status.equals("REJECTED")) {
                        List lst = sqlMap.executeQueryForList("getShareAccountdetails", dataMap);
                        if (lst.size() == 1) {
                            sqlMap.executeUpdate("authorizeShareAppInfoStatus", dataMap);
                        } else {
                            sqlMap.executeUpdate("authorizeShareApp", dataMap);
                        }
                        
                    } else {
                        sqlMap.executeUpdate("authorizeShareAppInfo", dataMap);
                    }
                    if (!shareAcctNo.equals(share)) {
                        sqlMap.executeUpdate("updateRemitIssue", dataMap);
                    }
                    dataMap.put(CommonConstants.STATUS, status);
                    if (status.equals("REJECTED")) {
                        sqlMap.executeUpdate("rejectedCustomer", shareAppno);
                    }
                    sqlMap.executeUpdate("authorizeShareAppDet", dataMap);
                    linkBatchId = shareAppno + "_" + CommonUtil.convertObjToStr(dataMap.get("SHARE DETAIL NO"));
                    //sqlMap.executeUpdate("authorizeCashTransaction", dataMap);
                    //sqlMap.executeUpdate("authorizeTransferTransaction", dataMap);
                }
                //Separation of Authorization for Cash and Transfer
                //Call this in all places that need Authorization for Transaction
                cashAuthMap = new HashMap();
//                cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
//                cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
//                cashAuthMap.put("DAILY", "DAILY");
//                System.out.println("map:" + map);
//                System.out.println("cashAuthMap:" + cashAuthMap);
//                // The following line commented because no need of doing interbranch transaction in GL Transaction
//                //                cashAuthMap.put("PRODUCT", "SHARE");
//                TransactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
                cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                cashAuthMap.put("DEBIT_LOAN_TYPE", "DP");
                cashAuthMap.put("DAILY", "DAILY");
                if (transTo.getProductType() != null && transTo.getProductType().equals("TL")) {
                    cashAuthMap.put("SHARE_ACCT","SHARE_ACCT");
                    transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
                    if(dataMap.containsKey("TRANS_ID") && dataMap.get("TRANS_ID") != null){
                    	transactionDAO.authorizeCashAndTransfer(CommonUtil.convertObjToStr(dataMap.get("TRANS_ID")), status, cashAuthMap);
                    }
                }else{
                    //Added by sreekrishnan for share debit transfer
                    if (status.equals(CommonConstants.STATUS_AUTHORIZED)) {
                        if (transTo.getTransType() != null && !transTo.getTransType().equals("") && transTo.getTransType().equalsIgnoreCase("TRANSFER")) {
                            HashMap updateMap = new HashMap();
                            List shareDebitList = null;
                            if (transTo.getDebitAcctNo() != null) {
                                shareDebitList = sqlMap.executeQueryForList("getShareAccountDebitDetails", transTo.getDebitAcctNo());
                            }
                            if (shareDebitList != null && shareDebitList.size() > 0) {
                                ShareAcctDetailsTO objshareAcctDetailsTO = null;
                                objshareAcctDetailsTO = updateShareDebitAccount(shareDebitList, transTo.getTransAmt(), objshareAcctDetailsTO);
                                objshareAcctDetailsTO.setShareNoFrom("ADD");
                                sqlMap.executeUpdate("insertShareAcctDetailsTO", objshareAcctDetailsTO);
                                updateMap.put("SHARE_ACCT_NO", transTo.getDebitAcctNo());
                                //updateMap.put("OUTSTANDING_AMOUNT", -1*transTo.getTransAmt()); 
                                updateMap.put("OUTSTANDING_AMOUNT", transTo.getTransAmt());
                                updateMap.put("SHARE_AMOUNT", transTo.getTransAmt());
                                updateMap.put("AVILABLE_NO_SHARES", -1 * (CommonUtil.convertObjToInt(objshareAcctDetailsTO.getTxtTotShareFee())));
                                System.out.println("updateMap is" + updateMap);
                                sqlMap.executeUpdate("upDateNoOfShareAndAmountDebitShare", updateMap);
                            }
                        }
                    }
                    transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
                    transactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
                }
                transTo = null;
                HashMap transMap = new HashMap();
                transMap.put("LINK_BATCH_ID", linkBatchId);
                sqlMap.executeUpdate("updateInstrumentNO1Transfer", transMap);
                sqlMap.executeUpdate("updateInstrumentNO1Cash", transMap);
                transMap = null;
                //                System.out.println();
                objTransactionTO = new TransactionTO();
                if (!shareAcctNo.equals(share)) {
                    objTransactionTO.setBatchId(CommonUtil.convertObjToStr(shareAcctNo));
                } else {
                    objTransactionTO.setBatchId(CommonUtil.convertObjToStr(shareAppno));
                }
                objTransactionTO.setTransId(CommonUtil.convertObjToStr(linkBatchId));
                objTransactionTO.setBranchId(_branchCode);
                //sqlMap.executeUpdate("deleteRemittanceIssueTransactionTO", objTransactionTO);
                //sqlMap.executeUpdate("AuthorizeRemittanceIssueTransactionTO" , objTransactionTO.getBatchId());
                cashAuthMap = null;
                dataMap.put("SHARE ACCOUNT DETAIL NO", dataMap.get("SHARE DETAIL NO"));
                // if(!status.equals("REJECTED") && CommonUtil.convertObjToInt(dataMap.get("SHARE DETAIL NO"))==1)
                // sqlMap.executeUpdate("updateMembershipShareCust",dataMap); 
            }
//            Added by Nikhil for DRF Applicable
            if (drfMap.containsKey("DRF_APPLICABLE")) {
                isDrfApplicable = true;
                HashMap drfDataMap = (HashMap) drfMap.get("DRF_APPLICABLE");
                drfDataMap.put("SHARE_ACCT_NO", CommonUtil.convertObjToStr(drfDataMap.get("DRF_MEMBER")));
                drfDataMap.put("CURR_DT", (Date) currDt.clone());
                List list = (List) sqlMap.executeQueryForList("getDrfTransactionForShare", drfDataMap);
                drfDataMap = (HashMap) list.get(0);
                ArrayList arrList = new ArrayList();
                HashMap authorizeMap = new HashMap();
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put("STATUS", status);
                singleAuthorizeMap.put("AUTH_BY", map.get(CommonConstants.USER_ID));
                singleAuthorizeMap.put("AUTH_DT", currDt);
                if (linkBatchId.length() > 0) {
                    singleAuthorizeMap.put("DRF_TRANS_ID", linkBatchId);
                } else {
                    singleAuthorizeMap.put("DRF_TRANS_ID", drfDataMap.get("DRF_TRANS_ID"));
                }
                singleAuthorizeMap.put("DRF_TRANSACTION_ID", drfDataMap.get("DRF_TRANS_ID"));
                singleAuthorizeMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                arrList.add(singleAuthorizeMap);
                authorizeMap.put("FROM_SHARE_DAO", "FROM_SHARE_DAO");
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, status);
                authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                drfMap.put("AUTHORIZEMAP", authorizeMap);
                DrfTransactionDAO drfTransDao = new DrfTransactionDAO();
                returnDrfMap = (HashMap) drfTransDao.execute(drfMap);
            } else {
                isDrfApplicable = false;
            }
            
            if (!status.equals("REJECTED")) {
                if (!shareAppno.equals(shareapp)) {
                    map.put("SHARE_APPL_NO", shareAppno);
                    List lst = (List) map.get("AUTHORIZEDATA");
                    HashMap hashmap = new HashMap();
                    hashmap = (HashMap) lst.get(0);
                    sqlMap.executeUpdate("upDateNoOfShareAndAmountforApp", map);
                } else {
                    map.put("SHARE_ACCT_NO", shareAcctNo);
                    sqlMap.executeUpdate("upDateNoOfShareAndAmount", map);
                }
//                added by Nikhil for DRF Applicable
                if (isDrfApplicable) {
//                    update share Acct for DRF Status
                    map.put("DRF_STATUS", map.get("AUTHORIZESTATUS"));
                    sqlMap.executeUpdate("updateShareDrfStatus", map);
                }
            }
           	if (map.containsKey("SERVICE_TAX_AUTH")) {
                HashMap servTaxWhr = new HashMap();
                servTaxWhr.put("STATUS", (String) map.get(CommonConstants.AUTHORIZESTATUS));
                servTaxWhr.put("USER_ID", map.get(CommonConstants.USER_ID));
                servTaxWhr.put("AUTHORIZEDT", currDt);
                servTaxWhr.put("ACCT_NUM", shareAcctNo);
                sqlMap.executeUpdate("authorizeServiceTaxDetails", servTaxWhr);
            }
            dataMap.put("AUTHORIZESTATUS", (String) map.get(CommonConstants.AUTHORIZESTATUS));
            updateMobilebanking(dataMap);               
            selectedList = null;
            dataMap = null;
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void authorizeUpdate(ShareAccInfoTO objTO, HashMap dataMap) throws Exception {
        try {
            sqlMap.executeUpdate("authorizeShareAccInfo", dataMap);
            sqlMap.executeUpdate("authorizeShareAccDet", dataMap);
        } catch (Exception e) {
            //            sqlMap.rollbackTransaction();
            throw e;
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
        shareAcctDetailsTO = null;
        shareAcctDetTOMap = null;
        transactionDAO = null;
    }

    public CashTransactionTO setCashTransactionDebit(HashMap cashMap, ShareAccInfoTO shareAcctDetailsTO) {
        log.info("In setCashTransaction() : " + shareAcctDetailsTO);
        Date curDate = (Date) currDt.clone();
        final CashTransactionTO objCashTransactionTO = new CashTransactionTO();
        try {
            objCashTransactionTO.setAcHdId(CommonUtil.convertObjToStr(cashMap.get(CommonConstants.AC_HD_ID)));
            objCashTransactionTO.setProdType(TransactionFactory.GL);
            objCashTransactionTO.setInpAmount(CommonUtil.convertObjToDouble(cashMap.get("AMOUNT")));
            objCashTransactionTO.setAmount(CommonUtil.convertObjToDouble(cashMap.get("AMOUNT")));
            objCashTransactionTO.setTransType(CommonConstants.DEBIT);
            objCashTransactionTO.setBranchId(shareAcctDetailsTO.getBranchCode());
            objCashTransactionTO.setStatusBy(CommonUtil.convertObjToStr(cashMap.get(CommonConstants.USER_ID)));
            objCashTransactionTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            objCashTransactionTO.setInitTransId(CommonUtil.convertObjToStr(cashMap.get(CommonConstants.USER_ID)));
            objCashTransactionTO.setInitChannType("CASHIER");
            objCashTransactionTO.setParticulars("By " + CommonUtil.convertObjToStr(cashMap.get("LINK_BATCH_ID")));
            objCashTransactionTO.setNarration(CommonUtil.convertObjToStr(cashMap.get("NARRATION")));
            objCashTransactionTO.setInitiatedBranch(_branchCode);
            objCashTransactionTO.setLinkBatchId(CommonUtil.convertObjToStr(cashMap.get("LINK_BATCH_ID")));
            objCashTransactionTO.setAuthorizeRemarks(CommonUtil.convertObjToStr(cashMap.get("AUTHORIZEREMARKS")));
            objCashTransactionTO.setCommand(CommonConstants.TOSTATUS_INSERT);
            objCashTransactionTO.setLoanHierarchy(CommonUtil.convertObjToStr(cashMap.get("HIERARCHY")));
            objCashTransactionTO.setTransModType(CommonUtil.convertObjToStr(cashMap.get("TRANS_MOD_TYPE")));
            objCashTransactionTO.setSingleTransId(CommonUtil.convertObjToStr(cashMap.get("generateSingleTransId")));
            objCashTransactionTO.setScreenName("Share Account");
        } catch (Exception e) {
            log.info("Error In setInwardClearing()");
            e.printStackTrace();
        }
        return objCashTransactionTO;
    }
     private void insertServiceTaxDetails(ServiceTaxDetailsTO objserviceTaxDetailsTO) {
        try {
            objserviceTaxDetailsTO.setServiceTaxDet_Id(getServiceTaxNo());
            objserviceTaxDetailsTO.setAcct_Num(shareAcctDetailsTO.getShareAcctNo());
            objserviceTaxDetailsTO.setParticulars("Share Account");
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
      
      //this function added by Anju Anand on 06 Mar 2015
      private void updateEmpRefNo(HashMap dataMap) {
        try {
            HashMap newMap = new HashMap();
            newMap.put("value", dataMap.get("SHARE_ACT_NO"));
            String status = "";
            status = CommonUtil.convertObjToStr(dataMap.get("STATUS"));
            List shareList = sqlMap.executeQueryForList("getShareAccInfoData", newMap);
            if (shareList != null && shareList.size() > 0) {
                ShareAccInfoTO newObj = (ShareAccInfoTO) shareList.get(0);
                //added by Anju Anand for Mantis Id: 10392 and Mantis Id: 10393
                HashMap newDataMap = new HashMap();
                HashMap newDetailsMap = new HashMap();
                newDataMap.put("EMP_REF_NO", newObj.getEmpRefNoOld());
                if (status.equals("REJECTED")) {
                    newDetailsMap.put("EMP_REFNO_OLD", newObj.getEmpRefNoOld());
                    newDetailsMap.put("EMP_REFNO_NEW", newObj.getEmpRefNoOld());
                    sqlMap.executeUpdate("updateShareAcctEmpRefNo", newDetailsMap);
                } else if (status.equals("AUTHORIZED")) {
                    List dedList = sqlMap.executeQueryForList("getAuthorizeDeductionMapping", newDataMap);
                    List suspList = sqlMap.executeQueryForList("getAuthorizeSuspenseRefNo", newDataMap);
                    if ((dedList != null && dedList.size() > 0) || (suspList != null && suspList.size() > 0)) {
                        newDetailsMap.put("EMP_REFNO_NEW", newObj.getEmpRefNoNew());
                        newDetailsMap.put("EMP_REFNO_OLD", newObj.getEmpRefNoOld());
                        if (dedList != null && dedList.size() > 0) {
                            HashMap dedMap = new HashMap();
                            dedMap = (HashMap) dedList.get(0);
                            String empRefNo = "";
                            empRefNo = CommonUtil.convertObjToStr(dedMap.get("EMP_REF_NO"));
                            if (empRefNo != null && !empRefNo.equals("")) {
                                sqlMap.executeUpdate("updateDedExemptionEmpRefNo", newDetailsMap);
                            }
                        }
                        if (suspList != null && suspList.size() > 0) {
                            HashMap suspMap = new HashMap();
                            suspMap = (HashMap) suspList.get(0);
                            String suspRefNo = "";
                            suspRefNo = CommonUtil.convertObjToStr(suspMap.get("SUSPENSE_REF_NO"));
                            if (suspRefNo != null && !suspRefNo.equals("")) {
                                sqlMap.executeUpdate("updateSuspAccMasterEmpRefNo", newDetailsMap);
                            }
                        }
                    }
                } //end of code by Anju Anand
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ShareDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
    private ShareAcctDetailsTO updateShareDebitAccount(List ShareAcctDet,double debitAmount,ShareAcctDetailsTO accTo) throws Exception {        
        ShareAcctDetailsTO objShareAcctDetailsTO=null; 
        HashMap singleRecordShareAcctDet = null;
        try{
            if(ShareAcctDet!=null && ShareAcctDet.size()>0) {
                singleRecordShareAcctDet = (HashMap) ShareAcctDet.get(0);
                objShareAcctDetailsTO = new ShareAcctDetailsTO();
                objShareAcctDetailsTO.setShareAcctDetNo(CommonUtil.convertObjToStr(ShareAcctDet.size()+1));             
                objShareAcctDetailsTO.setShareAcctNo(CommonUtil.convertObjToStr(singleRecordShareAcctDet.get("SHARE_ACCT_NO")));                
                objShareAcctDetailsTO.setShareCertIssueDt((Date)currDt.clone());               
                objShareAcctDetailsTO.setNoOfShares(CommonUtil.convertObjToInt(debitAmount/CommonUtil.convertObjToDouble(singleRecordShareAcctDet.get("FACE_VALUE"))));                
                objShareAcctDetailsTO.setShareNoFrom("WITHDRAWAL"); 
                objShareAcctDetailsTO.setTxtShareValue(CommonUtil.convertObjToDouble(debitAmount));
                objShareAcctDetailsTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                objShareAcctDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
                objShareAcctDetailsTO.setStatusDt((Date)currDt.clone()); 
                objShareAcctDetailsTO.setAuthorize(CommonConstants.STATUS_AUTHORIZED);
                objShareAcctDetailsTO.setAuthorizeBy("");
                objShareAcctDetailsTO.setAuthorizeDt((Date)currDt.clone());  
                objShareAcctDetailsTO.setShareStatus(CommonConstants.STATUS_RESOLUTION_ACCEPT);
                //objShareAcctDetailsTO.setResolutionNo(CommonUtil.convertObjToStr(singleRecordShareAcctDet.get("")));  
                objShareAcctDetailsTO.setTxtFromSL_No(accTo.getTxtFromSL_No());
                objShareAcctDetailsTO.setTxtToSL_No(accTo.getTxtToSL_No());
                singleRecordShareAcctDet = null;
            }
        } catch(Exception e){
            e.printStackTrace();
        }  
        return objShareAcctDetailsTO;
}
    
    //--- Inserts the data Record in SMS_SUBSCRIPTION Table
    private void updateSMSSubscription() throws Exception {
        objSMSSubscriptionTO.setStatusBy(objTO.getStatusBy());
        objSMSSubscriptionTO.setStatusDt(objTO.getStatusDt());
        objSMSSubscriptionTO.setCreatedBy(objTO.getStatusBy());
        objSMSSubscriptionTO.setCustId(objTO.getCustId());
        if (CommonUtil.convertObjToStr(objSMSSubscriptionTO.getMobileNo()) != null && CommonUtil.convertObjToStr(objSMSSubscriptionTO.getMobileNo()).length() > 0 && objSMSSubscriptionTO.getSubscriptionDt() != null) {
            List list = (List) sqlMap.executeQueryForList("getRecordExistorNotinSMSSub", objSMSSubscriptionTO);
            if (list != null && list.size() > 0) {
                sqlMap.executeUpdate("updateSMSSubscriptionMap", objSMSSubscriptionTO);
            } else {
                objSMSSubscriptionTO.setCreatedDt((Date) currDt.clone());
                sqlMap.executeUpdate("insertSMSSubscriptionMap", objSMSSubscriptionTO);
            }
        }
    }
    
    private void updateMobilebanking(HashMap dataMap) throws Exception {
        dataMap.put("PROD_TYPE", "SH");
        dataMap.put("ACCOUNTNO", dataMap.get("SHARE ACCOUNT NO"));
        dataMap.put("PROD_ID", dataMap.get("SHARE TYPE"));
        dataMap.put("USER_ID",dataMap.get("USER_ID"));
        dataMap.put("STATUS", dataMap.get("AUTHORIZESTATUS"));
        dataMap.put("AUTHORIZEDT",currDt.clone());
        sqlMap.executeUpdate("authorizeSMSSubscriptionMap", dataMap);
    }

}