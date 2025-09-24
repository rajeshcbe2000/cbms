/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareResolutionDAO.java
 *
 * Created on Thu Apr 28 12:59:32 IST 2005
 */
package com.see.truetransact.serverside.share.shareresolution;

import java.util.List;
import java.util.ArrayList;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import java.util.HashMap;
import java.util.Date;
import org.apache.log4j.Logger;
import com.see.truetransact.transferobject.share.ShareAccInfoTO;
import com.see.truetransact.transferobject.share.ShareAcctDetailsTO;
import com.see.truetransact.transferobject.share.shareresolution.ShareResolutionTO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.commonutil.LocaleConstants;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.share.DrfTransactionDAO;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.transferobject.share.DrfTransactionTO;

/**
 * ShareResolution DAO.
 *
 */
public class ShareResolutionDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private ShareResolutionTO objTO;
    private TransactionTO objTransactionTO;
    HashMap shareAcctDet;
    private Date currDt = null;
    private final String INT_GET_FROM = "INT_GET_FROM";
    private final String LIMIT = "LIMIT";
    private final String PROD_ID = "PROD_ID";
    private final String SECURITY_DETAILS = "SECURITY_DETAILS";
    private final static Logger log = Logger.getLogger(ShareResolutionDAO.class);
    ArrayList listShareAcctDet;
    String branchId = "";
    String regShareNoFrom = "";
    String ShareAcctNo = "";
    String ShareAcctClass = "";
    HashMap execReturnMap = new HashMap();
    String multiShareAllowed = "";
    String userid = "";
    String branchcode = "";
    String singleTransId = "";

    /**
     * Creates a new instance of ShareResolutionDAO
     */
    public ShareResolutionDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        //String where = (String) map.get(CommonConstants.MAP_WHERE);
        //List list = (List) sqlMap.executeQueryForList("select query map", where);
        //returnMap.put("return key ", list);
        return returnMap;
    }

    private void getShareAcct_No(HashMap where) throws Exception {
//        HashMap where=new HashMap();
//        where=(HashMap) listShareAcctDet.get(0);
        System.out.println("map is" + where);
        String shareType = CommonUtil.convertObjToStr(where.get("SHARE TYPE"));
        where.put("SHARE_TYPE", shareType);
        List accNolist = sqlMap.executeQueryForList("getMinIntialShares", where);
        if (accNolist != null && accNolist.size() > 0) {
            HashMap hash = (HashMap) accNolist.get(0);
            String prefix = CommonUtil.convertObjToStr(hash.get("NUM_PATTERN_PREFIX"));
            int suffix = CommonUtil.convertObjToInt(hash.get("NUM_PATTERN_SUFFIX"));
            String sufixnum = CommonUtil.convertObjToStr(hash.get("NUM_PATTERN_SUFFIX"));
            suffix = suffix + 1;
            System.out.println("Suffix$$$$" + suffix);
            String suf = CommonUtil.convertObjToStr(new Integer(suffix));
            where.put("PREFIX", prefix);
            where.put("SUFFIX", suf);
            sqlMap.executeUpdate("updateShareProduct", where);
            ShareAcctNo = prefix + sufixnum;
            ShareAcctClass=prefix;

        }





        //        final IDGenerateDAO dao = new IDGenerateDAO();
        //        final HashMap where = new HashMap();
        //        where.put(CommonConstants.MAP_WHERE, "SHARE_ACCT_NO");
        //        ShareAcctNo =  (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        System.out.println("ShareAcctNo");
    }

    public CashTransactionTO setCashCreditTransaction(HashMap cashMap) {
        log.info("In setCashTransaction()");
        // java.util.Date curDate=(java.util.Date)currDt.clone();
        final CashTransactionTO objCashTransactionTO = new CashTransactionTO();
        try {
            objCashTransactionTO.setAcHdId(CommonUtil.convertObjToStr(cashMap.get(CommonConstants.AC_HD_ID)));
            objCashTransactionTO.setProdType(TransactionFactory.GL);
            objCashTransactionTO.setInpAmount(CommonUtil.convertObjToDouble(cashMap.get("AMOUNT")));
            objCashTransactionTO.setAmount(CommonUtil.convertObjToDouble(cashMap.get("AMOUNT")));
            objCashTransactionTO.setTransType(CommonConstants.CREDIT);
            objCashTransactionTO.setBranchId(CommonUtil.convertObjToStr(cashMap.get("BRANCH_ID")));
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
            System.out.println("objCashTransactionTO:" + objCashTransactionTO);
        } catch (Exception e) {
            log.info("Error In setInwardClearing()");
            e.printStackTrace();
        }
        return objCashTransactionTO;
    }

    public CashTransactionTO setCashDebitTransaction(HashMap cashMap) {
        log.info("In setCashTransaction()");
        //java.util.Date curDate=(java.util.Date)currDt.clone();
        final CashTransactionTO objCashTransactionTO = new CashTransactionTO();
        try {
            objCashTransactionTO.setAcHdId(CommonUtil.convertObjToStr(cashMap.get(CommonConstants.AC_HD_ID)));
            objCashTransactionTO.setProdType(TransactionFactory.GL);
            objCashTransactionTO.setInpAmount(CommonUtil.convertObjToDouble(cashMap.get("AMOUNT")));
            objCashTransactionTO.setAmount(CommonUtil.convertObjToDouble(cashMap.get("AMOUNT")));
            objCashTransactionTO.setTransType(CommonConstants.DEBIT);
            objCashTransactionTO.setBranchId(CommonUtil.convertObjToStr(cashMap.get("BRANCH_ID")));
            objCashTransactionTO.setStatusBy(CommonUtil.convertObjToStr(cashMap.get(CommonConstants.USER_ID)));
            //            objCashTransactionTO.setInstrumentNo1(cashTo.getInstrumentNo1());
            // objCashTransactionTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            objCashTransactionTO.setInitTransId(CommonUtil.convertObjToStr(cashMap.get(CommonConstants.USER_ID)));
            objCashTransactionTO.setInitChannType("CASHIER");
            objCashTransactionTO.setParticulars("To " + CommonUtil.convertObjToStr(cashMap.get("LINK_BATCH_ID")));
            objCashTransactionTO.setInitiatedBranch(_branchCode);
            objCashTransactionTO.setLinkBatchId(CommonUtil.convertObjToStr(cashMap.get("LINK_BATCH_ID")));
            objCashTransactionTO.setAuthorizeRemarks(CommonUtil.convertObjToStr(cashMap.get("AUTHORIZEREMARKS")));
            objCashTransactionTO.setCommand(CommonConstants.TOSTATUS_INSERT);
            System.out.println("objCashTransactionTO:" + objCashTransactionTO);
        } catch (Exception e) {
            log.info("Error In setInwardClearing()");
            e.printStackTrace();
        }
        return objCashTransactionTO;
    }

    private void ActionOnResolutionRejectData(String staus, LogDAO objLogDAO, LogTO objLogTO, HashMap map) throws Exception {
        System.out.println("the map1111 is" + map);
        try {
            // sqlMap.startTransaction();
            listShareAcctDet = (ArrayList) map.get("ShareAccDet");
            HashMap h = new HashMap();
            String applNo = "";
            String shareType = "";
            String userid = "";
            String branchcode = "";
            String sharedetno = "";
            if (listShareAcctDet != null && listShareAcctDet.size() > 0) {
                h = (HashMap) listShareAcctDet.get(0);
                applNo = CommonUtil.convertObjToStr(h.get("SHARE APPLICATION NO"));
                shareType = CommonUtil.convertObjToStr(h.get("SHARE TYPE"));
                userid = objTO.getStatusBy();
                branchcode = CommonUtil.convertObjToStr(h.get("BRANCH CODE"));
                sharedetno = CommonUtil.convertObjToStr(h.get("SHARE ACCOUNT DETAIL NO"));
                System.out.println("the map is" + map);
                System.out.println("the application no is" + applNo);
            }
            HashMap hmap = new HashMap();
            hmap.put("SHARE APPLICATION NO", applNo);
            HashMap shareFees = (HashMap) sqlMap.executeQueryForObject("getShareFeeDetails", hmap);
            double shareValue = CommonUtil.convertObjToDouble(shareFees.get("SHARE_VALUE")).doubleValue();
            double sharefee = CommonUtil.convertObjToDouble(shareFees.get("SHARE_FEE")).doubleValue();
            double shareApplFee = CommonUtil.convertObjToDouble(shareFees.get("SHARE_APPL_FEE")).doubleValue();
            double shareMemFee = CommonUtil.convertObjToDouble(shareFees.get("SHARE_MEM_FEE")).doubleValue();
            double totamount = shareValue + sharefee + shareApplFee + shareMemFee;
            double amount = shareValue;
            double income = sharefee + shareApplFee + shareMemFee;
            HashMap transDetailMap = new HashMap();
            if (map.containsKey("Transaction Details Data")) {
                transDetailMap = (HashMap) map.get("Transaction Details Data");
                System.out.println("@##$#$% transDetailMap #### :" + transDetailMap);
            }
            // executeTransactionPart(map, transDetailMap);
            HashMap txMap;

            TransferTrans objTransferTrans = new TransferTrans();
            objTransferTrans.setLinkBatchId(applNo + "_" + sharedetno);
            objTransferTrans.setInitiatedBranch(_branchCode);

            txMap = new HashMap();
            ArrayList transferList = new ArrayList();
            HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getShareSuspenseAcHd", shareType);


            txMap.put(CommonConstants.USER_ID, userid);
            //txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
            txMap.put(TransferTrans.DR_BRANCH, branchcode);
            txMap.put(TransferTrans.CR_BRANCH, branchcode);
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.PARTICULARS, applNo);
            txMap.put("LINK_BATCH_ID", applNo);


            //txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            // txMap.put(TransferTrans.DR_AC_HD, (String)acHeads.get("SHARE_SUSPENSE_ACHD") );
            //  txMap.put("AUTHORIZEREMARKS","SHARE_SUSPENSE_ACHD");
            //  transferList.add(objTransferTrans.getDebitTransferTO(txMap,shareValue ));
            if (transDetailMap.get("TRANS_TYPE") != null && transDetailMap.get("TRANS_TYPE").equals("CASH")) {
                double transAmt;
                TransactionTO transTO = new TransactionTO();
                ArrayList cashList = new ArrayList();

                System.out.println("line no 753^^^^^^^");
                txMap = new HashMap();
                txMap.put(CommonConstants.BRANCH_ID, branchcode);
                txMap.put(CommonConstants.PRODUCT_ID, TransactionFactory.GL);
                System.out.println("the branch is" + branchcode);

                txMap.put(CommonConstants.USER_ID, userid);
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                // txMap.put(CommonConstants.AC_HD_ID,(String)acHeads.get("SHARE_SUSPENSE_ACHD") );
                // txMap.put("AUTHORIZEREMARKS","SHARE_SUSPENSE_ACHD");
                // txMap.put("AMOUNT",CommonUtil.convertObjToStr(new Double(totamount)));
                txMap.put("LINK_BATCH_ID", applNo);
                txMap.put("BRANCH_ID", branchcode);
                //cashList.add(setCashDebitTransaction(txMap));
                if (!map.containsKey("SHARE_FEE") && !map.containsKey("APPLICATION_FEE") && !map.containsKey("MEMBERSHIP_FEE")) {
                    txMap.put(CommonConstants.AC_HD_ID, (String) acHeads.get("SHARE_SUSPENSE_ACHD"));
                    txMap.put("AUTHORIZEREMARKS", "SHARE_SUSPENSE_ACHD");
                    txMap.put("AMOUNT", CommonUtil.convertObjToStr(new Double(amount)));
                    txMap.put("LINK_BATCH_ID", applNo);
                    cashList.add(setCashDebitTransaction(txMap));
                    HashMap tranMap = new HashMap();
                    tranMap.put(CommonConstants.BRANCH_ID, branchcode);
                    tranMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                    tranMap.put(CommonConstants.IP_ADDR, CommonUtil.convertObjToStr(map.get("IP_ADDR")));
                    tranMap.put(CommonConstants.MODULE, CommonUtil.convertObjToStr(map.get("MODULE")));
                    tranMap.put(CommonConstants.SCREEN, CommonUtil.convertObjToStr(map.get("SCREEN")));
                    tranMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                    tranMap.put("DAILYDEPOSITTRANSTO", cashList);
                    CashTransactionDAO cashDao;
                    cashDao = new CashTransactionDAO();
                    System.out.println("the map is" + tranMap);
                    tranMap = cashDao.execute(tranMap, false);
                    cashDao = null;
                    tranMap = null;
                    txMap.put(TransferTrans.PARTICULARS, applNo);
                    txMap.put(CommonConstants.USER_ID, userid);
                    //txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
                    txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    // cashList.add(setCashCreditTransaction(txMap));
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("SHARE_SUSPENSE_ACHD"));
                    txMap.put("AUTHORIZEREMARKS", "SHARE_SUSPENSE_ACHD");
                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, income));
                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INCOME_ACCOUNT_HEAD"));
                    txMap.put("AUTHORIZEREMARKS", "INCOME_ACCOUNT_HEAD");
                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, income));
                    objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                } else if (map.containsKey("SHARE_FEE") && map.containsKey("APPLICATION_FEE") && map.containsKey("MEMBERSHIP_FEE")) {
                    txMap.put(CommonConstants.AC_HD_ID, (String) acHeads.get("SHARE_SUSPENSE_ACHD"));
                    txMap.put("AUTHORIZEREMARKS", "SHARE_SUSPENSE_ACHD");
                    txMap.put("AMOUNT", CommonUtil.convertObjToStr(new Double(totamount)));
                    txMap.put("LINK_BATCH_ID", applNo);
                    cashList.add(setCashDebitTransaction(txMap));
                    HashMap tranMap = new HashMap();
                    tranMap.put(CommonConstants.BRANCH_ID, branchcode);
                    tranMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                    tranMap.put(CommonConstants.IP_ADDR, CommonUtil.convertObjToStr(map.get("IP_ADDR")));
                    tranMap.put(CommonConstants.MODULE, CommonUtil.convertObjToStr(map.get("MODULE")));
                    tranMap.put(CommonConstants.SCREEN, CommonUtil.convertObjToStr(map.get("SCREEN")));
                    tranMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                    tranMap.put("DAILYDEPOSITTRANSTO", cashList);
                    CashTransactionDAO cashDao;
                    cashDao = new CashTransactionDAO();
                    System.out.println("the map is" + tranMap);
                    tranMap = cashDao.execute(tranMap, false);
                    cashDao = null;
                    tranMap = null;
                } else {
                    if (map.containsKey("SHARE_FEE")) {
                        income = income - sharefee;
                        totamount = amount + sharefee;


                        txMap.put(CommonConstants.AC_HD_ID, (String) acHeads.get("SHARE_SUSPENSE_ACHD"));
                        txMap.put("AUTHORIZEREMARKS", "SHARE_SUSPENSE_ACHD");
                        txMap.put("AMOUNT", CommonUtil.convertObjToStr(new Double(totamount)));
                        txMap.put("LINK_BATCH_ID", applNo);
                        cashList.add(setCashDebitTransaction(txMap));
                        //txMap.put("AMOUNT", CommonUtil.convertObjToStr(new Double(totamount)));
                        txMap.put("LINK_BATCH_ID", applNo);
                        HashMap tranMap = new HashMap();
                        tranMap.put(CommonConstants.BRANCH_ID, branchcode);
                        tranMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                        tranMap.put(CommonConstants.IP_ADDR, CommonUtil.convertObjToStr(map.get("IP_ADDR")));
                        tranMap.put(CommonConstants.MODULE, CommonUtil.convertObjToStr(map.get("MODULE")));
                        tranMap.put(CommonConstants.SCREEN, CommonUtil.convertObjToStr(map.get("SCREEN")));
                        tranMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
                        CashTransactionDAO cashDao;
                        cashDao = new CashTransactionDAO();
                        System.out.println("the map is" + tranMap);
                        tranMap = cashDao.execute(tranMap, false);
                        cashDao = null;
                        tranMap = null;
                        txMap.put(TransferTrans.PARTICULARS, applNo);
                        txMap.put(CommonConstants.USER_ID, userid);
                        // txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
                        txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        // cashList.add(setCashCreditTransaction(txMap));
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("SHARE_SUSPENSE_ACHD"));
                        txMap.put("AUTHORIZEREMARKS", "SHARE_SUSPENSE_ACHD");
                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, income));
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INCOME_ACCOUNT_HEAD"));
                        txMap.put("AUTHORIZEREMARKS", "INCOME_ACCOUNT_HEAD");
                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, income));
                        objTransferTrans.doDebitCredit(transferList, _branchCode, false);


                    }
                    if (map.containsKey("APPLICATION_FEE")) {
                        income = income - shareApplFee;
                        totamount = amount + shareApplFee;
                        txMap.put(CommonConstants.AC_HD_ID, (String) acHeads.get("SHARE_SUSPENSE_ACHD"));
                        txMap.put("AUTHORIZEREMARKS", "SHARE_SUSPENSE_ACHD");
                        txMap.put("AMOUNT", CommonUtil.convertObjToStr(new Double(totamount)));
                        txMap.put("LINK_BATCH_ID", applNo);
                        cashList.add(setCashDebitTransaction(txMap));
                        HashMap tranMap = new HashMap();
                        tranMap.put(CommonConstants.BRANCH_ID, branchcode);
                        tranMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                        tranMap.put(CommonConstants.IP_ADDR, CommonUtil.convertObjToStr(map.get("IP_ADDR")));
                        tranMap.put(CommonConstants.MODULE, CommonUtil.convertObjToStr(map.get("MODULE")));
                        tranMap.put(CommonConstants.SCREEN, CommonUtil.convertObjToStr(map.get("SCREEN")));
                        tranMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
                        CashTransactionDAO cashDao;
                        cashDao = new CashTransactionDAO();
                        System.out.println("the map is" + tranMap);
                        tranMap = cashDao.execute(tranMap, false);
                        cashDao = null;
                        tranMap = null;
                        txMap.put(TransferTrans.PARTICULARS, applNo);
                        txMap.put(CommonConstants.USER_ID, userid);
                        //  txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
                        txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        // txMap.put("AMOUNT", CommonUtil.convertObjToStr(new Double(totamount)));
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("SHARE_SUSPENSE_ACHD"));
                        txMap.put("AUTHORIZEREMARKS", "SHARE_SUSPENSE_ACHD");
                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, income));
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INCOME_ACCOUNT_HEAD"));
                        txMap.put("AUTHORIZEREMARKS", "INCOME_ACCOUNT_HEAD");
                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, income));
                        objTransferTrans.doDebitCredit(transferList, _branchCode, false);


                    }
                    if (map.containsKey("MEMBERSHIP_FEE")) {
                        income = income - shareMemFee;
                        totamount = amount + shareMemFee;

                        txMap.put(CommonConstants.AC_HD_ID, (String) acHeads.get("SHARE_SUSPENSE_ACHD"));
                        txMap.put("AUTHORIZEREMARKS", "SHARE_SUSPENSE_ACHD");
                        txMap.put("AMOUNT", CommonUtil.convertObjToStr(new Double(totamount)));
                        txMap.put("LINK_BATCH_ID", applNo);
                        cashList.add(setCashDebitTransaction(txMap));
                        HashMap tranMap = new HashMap();
                        tranMap.put(CommonConstants.BRANCH_ID, branchcode);
                        tranMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                        tranMap.put(CommonConstants.IP_ADDR, CommonUtil.convertObjToStr(map.get("IP_ADDR")));
                        tranMap.put(CommonConstants.MODULE, CommonUtil.convertObjToStr(map.get("MODULE")));
                        tranMap.put(CommonConstants.SCREEN, CommonUtil.convertObjToStr(map.get("SCREEN")));
                        tranMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
                        CashTransactionDAO cashDao;
                        cashDao = new CashTransactionDAO();
                        System.out.println("the map is" + tranMap);
                        tranMap = cashDao.execute(tranMap, false);
                        cashDao = null;
                        tranMap = null;
                        txMap.put(TransferTrans.PARTICULARS, applNo);
                        txMap.put(CommonConstants.USER_ID, userid);
                        // txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
                        txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        //double debitAmt= CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();

                        //txMap.put("AMOUNT", CommonUtil.convertObjToStr(new Double(totamount)));
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("SHARE_SUSPENSE_ACHD"));
                        txMap.put("AUTHORIZEREMARKS", "SHARE_SUSPENSE_ACHD");
                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, income));
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INCOME_ACCOUNT_HEAD"));
                        txMap.put("AUTHORIZEREMARKS", "INCOME_ACCOUNT_HEAD");
                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, income));
                        objTransferTrans.doDebitCredit(transferList, _branchCode, false);


                    }

                }
            }

            if (transDetailMap.get("TRANS_TYPE") != null && transDetailMap.get("TRANS_TYPE").equals("TRANSFER")) {
                HashMap crMap = new HashMap();
                crMap.put("ACT_NUM", transDetailMap.get("CR_ACT_NUM"));
                List oaAcctHead = sqlMap.executeQueryForList("getAccNoProdIdDet", crMap);
                if (oaAcctHead != null && oaAcctHead.size() > 0) {
                    crMap = (HashMap) oaAcctHead.get(0);
                }
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("SHARE_SUSPENSE_ACHD"));
                txMap.put("AUTHORIZEREMARKS", "SHARE_SUSPENSE_ACHD");
                transferList.add(objTransferTrans.getDebitTransferTO(txMap, totamount));
                if ((map.containsKey("SHARE_FEE")) && (map.containsKey("APPLICATION_FEE")) && (map.containsKey("MEMBERSHIP_FEE"))) {
                    txMap.put(TransferTrans.CR_ACT_NUM, transDetailMap.get("CR_ACT_NUM"));
                    txMap.put(TransferTrans.CR_AC_HD, crMap.get("AC_HD_ID"));
                    txMap.put(TransferTrans.CR_PROD_ID, crMap.get("PROD_ID"));
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, totamount));
                } else if ((!map.containsKey("SHARE_FEE")) && (!map.containsKey("APPLICATION_FEE")) && (!map.containsKey("MEMBERSHIP_FEE"))) {

                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INCOME_ACCOUNT_HEAD"));
                    txMap.put("AUTHORIZEREMARKS", "INCOME_ACCOUNT_HEAD");
                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, income));

                    txMap.put(TransferTrans.CR_ACT_NUM, transDetailMap.get("CR_ACT_NUM"));

                    txMap.put(TransferTrans.CR_AC_HD, crMap.get("AC_HD_ID"));
                    txMap.put(TransferTrans.CR_PROD_ID, crMap.get("PROD_ID"));
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, amount));

                } else {
                    if (map.containsKey("SHARE_FEE")) {
                        income = income - sharefee;
                        totamount = amount + sharefee;
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INCOME_ACCOUNT_HEAD"));
                        txMap.put("AUTHORIZEREMARKS", "INCOME_ACCOUNT_HEAD");
                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, income));
                        txMap.put(TransferTrans.CR_ACT_NUM, transDetailMap.get("CR_ACT_NUM"));
                        txMap.put(TransferTrans.CR_AC_HD, crMap.get("AC_HD_ID"));
                        txMap.put(TransferTrans.CR_PROD_ID, crMap.get("PROD_ID"));
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, totamount));


                    }
                    if (map.containsKey("APPLICATION_FEE")) {
                        income = income - shareApplFee;
                        totamount = amount + shareApplFee;
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INCOME_ACCOUNT_HEAD"));
                        txMap.put("AUTHORIZEREMARKS", "INCOME_ACCOUNT_HEAD");
                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, income));
                        txMap.put(TransferTrans.CR_ACT_NUM, transDetailMap.get("CR_ACT_NUM"));
                        txMap.put(TransferTrans.CR_AC_HD, crMap.get("AC_HD_ID"));
                        txMap.put(TransferTrans.CR_PROD_ID, crMap.get("PROD_ID"));
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, totamount));


                    }
                    if (map.containsKey("MEMBERSHIP_FEE")) {
                        income = income - shareMemFee;
                        totamount = amount + shareMemFee;
                        System.out.println("income is" + income);
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INCOME_ACCOUNT_HEAD"));
                        txMap.put("AUTHORIZEREMARKS", "INCOME_ACCOUNT_HEAD");
                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, income));
                        txMap.put(TransferTrans.CR_ACT_NUM, transDetailMap.get("CR_ACT_NUM"));
                        txMap.put(TransferTrans.CR_AC_HD, crMap.get("AC_HD_ID"));
                        txMap.put(TransferTrans.CR_PROD_ID, crMap.get("PROD_ID"));
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, totamount));

                    }
                }
                objTransferTrans.doDebitCredit(transferList, _branchCode, false);

            }

        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
            // log.info("Exception in populateData..." + e.toString());
        }

    }

    private void ActionOnResolutionData(String staus, LogDAO objLogDAO, LogTO objLogTO, HashMap map) throws Exception {
        sqlMap.startTransaction();
        listShareAcctDet = (ArrayList) map.get("ShareAccDet");
        String drfHead = "";
        String Split_needed = "N";
        HashMap h = new HashMap();
        ArrayList transferList = new ArrayList();
        TransferTrans objTransferTrans = new TransferTrans();
        for (int k = 0; k < listShareAcctDet.size(); k++) {
            h = (HashMap) listShareAcctDet.get(k);
            System.out.println("map is" + h);
            String applNo = CommonUtil.convertObjToStr(h.get("SHARE APPLICATION NO"));
            String shareType = CommonUtil.convertObjToStr(h.get("SHARE TYPE"));
            String sharedetno = CommonUtil.convertObjToStr(h.get("SHARE ACCOUNT DETAIL NO"));
            String userid = objTO.getStatusBy();
            String branchcode = CommonUtil.convertObjToStr(h.get("BRANCH CODE"));

            System.out.println("the application no is" + applNo);


            HashMap aMap = new HashMap();
            aMap.put("SHARE_TYPE", shareType);
            List aList = sqlMap.executeQueryForList("getDRFAccountDts", aMap);
            if (aList != null && aList.size() > 0) {
                aMap = (HashMap) aList.get(0);
                if (aMap.get("DRF_SUSPENSE_HEAD") != null) {
                    drfHead = aMap.get("DRF_SUSPENSE_HEAD").toString();
                }
                if (aMap.get("SPLIT_DRF") != null && !aMap.get("SPLIT_DRF").toString().equals("")) {
                    Split_needed = aMap.get("SPLIT_DRF").toString();
                }

            }



            HashMap hmap = new HashMap();
            hmap.put("SHARE APPLICATION NO", applNo);
            HashMap shareFees = (HashMap) sqlMap.executeQueryForObject("getShareFeeDetails", hmap);
            double shareValue = CommonUtil.convertObjToDouble(shareFees.get("SHARE_VALUE")).doubleValue();
            double sharefee = CommonUtil.convertObjToDouble(shareFees.get("SHARE_FEE")).doubleValue();
            double shareApplFee = CommonUtil.convertObjToDouble(shareFees.get("SHARE_APPL_FEE")).doubleValue();
            double shareMemFee = CommonUtil.convertObjToDouble(shareFees.get("SHARE_MEM_FEE")).doubleValue();
            HashMap newMap = (HashMap) listShareAcctDet.get(k);
            newMap.put("SHARE_TYPE", newMap.get("SHARE TYPE"));
            List ratificList = sqlMap.executeQueryForList("getFeeData", newMap);
            String ratification = "";
            String subsidy = "";
            String cast = "";
            double governmentsShare = 0.0;
            HashMap ratificMap = new HashMap();
            if (ratificList != null && ratificList.size() > 0) {
                ratificMap = (HashMap) ratificList.get(0);
                ratification = CommonUtil.convertObjToStr(ratificMap.get("RATIFICATION_REQUIRED"));
                subsidy = CommonUtil.convertObjToStr(ratificMap.get("SUBSIDY_FOR_SCST"));
                // governmentsShare=CommonUtil.convertObjToDouble(ratificMap.get("GOVTS_SHARE"));
                System.out.println("governmentsShare11" + governmentsShare);
                if (ratification.equals("Y") && subsidy.equals("Y")) {
                    List castList = sqlMap.executeQueryForList("getCasteForAppNo", newMap);
                    if (castList != null && castList.size() > 0) {
                        HashMap castMap = (HashMap) castList.get(0);
                        if (castMap.get("CASTE") != null) {
                            cast = CommonUtil.convertObjToStr(castMap.get("CASTE")).toString();
                            if (cast.equals("SC") || cast.equals("ST")) {
                                governmentsShare = CommonUtil.convertObjToDouble(ratificMap.get("GOVTS_SHARE"));
                                System.out.println("newMap" + newMap);
                                //governmentsShare=governmentsShare*(CommonUtil.convertObjToDouble(newMap.get("NO. OF SHARES")));
                            }
                        }
                    }
                }
            }
            shareValue = shareValue - governmentsShare;
            double totamount = shareValue + sharefee + shareApplFee + shareMemFee;
            // map.put("TransactionTo",)
            HashMap txMap;

            objTransferTrans.setLinkBatchId(applNo + "_" + sharedetno);
            objTransferTrans.setInitiatedBranch(_branchCode);

            txMap = new HashMap();

            HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getShareSuspenseAcHd", shareType);
            txMap.put(CommonConstants.USER_ID, userid);
            // txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.PARTICULARS, applNo + "_1");
            txMap.put("LINK_BATCH_ID", objTransferTrans.getLinkBatchId());
            txMap.put("generateSingleTransId",singleTransId);
            //txMap.put(TransferTrans.DR_ACT_NUM,CommonUtil.convertObjToStr());
            // txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
            // txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
            if (totamount > 0) {
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("SHARE_SUSPENSE_ACHD"));
                txMap.put("AUTHORIZEREMARKS", "SHARE_SUSPENSE_ACHD");
                txMap.put("TRANS_MOD_TYPE","SH");
                transferList.add(objTransferTrans.getDebitTransferTO(txMap, totamount));
            }
            if (shareValue > 0.0) {
                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SHARE_ACHD"));
                txMap.put("AUTHORIZEREMARKS", "SHARE_ACHD");
                txMap.put("TRANS_MOD_TYPE","SH");
                transferList.add(objTransferTrans.getCreditTransferTO(txMap, shareValue));
            }
            if (sharefee > 0.0) {
                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SHARE_FEE_ACHD"));
                txMap.put("AUTHORIZEREMARKS", "SHARE_FEE_ACHD");
                txMap.put("TRANS_MOD_TYPE","SH");
                transferList.add(objTransferTrans.getCreditTransferTO(txMap, sharefee));

            }
            if (shareApplFee > 0.0) {
                txMap.put("AUTHORIZEREMARKS", "APPLICATION_FEE_ACHD");
                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("APPLICATION_FEE_ACHD"));
                txMap.put("TRANS_MOD_TYPE","SH");
                transferList.add(objTransferTrans.getCreditTransferTO(txMap, shareApplFee));
            }
            if (shareMemFee > 0.0) {
                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("MEMBERSHIP_FEE_ACHD"));
                txMap.put("AUTHORIZEREMARKS", "MEMBERSHIP_FEE_ACHD");
                txMap.put("TRANS_MOD_TYPE","SH");
                transferList.add(objTransferTrans.getCreditTransferTO(txMap, shareMemFee));
            }
            if (Split_needed.equals("Y")) {
                HashMap testMap = new HashMap();
                testMap.put("SHARE_APPL_NO", applNo);

                List drflist = sqlMap.executeQueryForList("CheckDrfApplicable", testMap);
                if (drflist != null && drflist.size() > 0) {
                    testMap = (HashMap) drflist.get(0);
                    String drf_Prod_Id = "";
                    double drfAmount = 0.0;
                    String drfApplicable = testMap.get("DRF_APPLICABLE").toString();
                    if (drfApplicable != null && !drfApplicable.equals("") && drfApplicable.equals("Y")) {
                        drf_Prod_Id = testMap.get("DRF_PRODUCT").toString();
                        List details = sqlMap.executeQueryForList("getDrfDts", testMap);
                        HashMap detailsMap = (HashMap) details.get(0);
                        drfAmount = Double.parseDouble(detailsMap.get("AMOUNT").toString());
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.DR_AC_HD, drfHead);
                        txMap.put("AUTHORIZEREMARKS", "DRF_SUSPENSE_ACHD");
                        txMap.put("TRANS_MOD_TYPE","SH");
                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, drfAmount));

                        txMap.put(TransferTrans.CR_AC_HD, detailsMap.get("DRF_PAYMENT_ACHD").toString());
                        txMap.put("AUTHORIZEREMARKS", "DRF_ACHD");
                        txMap.put("TRANS_MOD_TYPE","SH");
                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, drfAmount));

                    }

//              ////////////////////////////////////////////////////////////////
//              
//            
//                   DrfTransactionTO objDrfTransactionTO=new DrfTransactionTO();    
//            DrfTransactionDAO d=new DrfTransactionDAO();
//               currDt = currDt.clone();
//               objDrfTransactionTO.setDrfTransID(d.getDrfTrans_No_For_Share_Resolution());
//            objDrfTransactionTO.setCommand("INSERT");
//            objDrfTransactionTO.setStatus(CommonConstants.STATUS_CREATED);
//            objDrfTransactionTO.setTxtDrfTransMemberNo(ShareAcctNo);
//            objDrfTransactionTO.setTxtDrfTransAmount(""+drfAmount);
////            objgetDrfTransactionTO.setTxtDrfTransName(CommonUtil.convertObjToStr(whereMap.get("DRF_MEMBER_NAME")));
//            objDrfTransactionTO.setCboDrfTransProdID(drf_Prod_Id);
////            objgetDrfTransactionTO.setDrfProdPaymentAmt(CommonUtil.convertObjToStr(whereMap.get("DRF_PRODUCT_AMOUNT")));
////            objgetDrfTransactionTO.setDrfProductAmount(CommonUtil.convertObjToStr(whereMap.get("DRF_AMOUNT")));
//            objDrfTransactionTO.setRdoDrfTransaction("RECIEPT");
//            objDrfTransactionTO.setChkDueAmtPayment("N");
//            objDrfTransactionTO.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
//            objDrfTransactionTO.setStatusDate(currDt);
//            objDrfTransactionTO.setInitiatedBranch((String) map.get(CommonConstants.BRANCH_ID));  
//            objDrfTransactionTO.setAuthorizeBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
//            objDrfTransactionTO.setAuthorizeDate(currDt);
//            objDrfTransactionTO.setAuthorizeStatus("AUTHORIZED");
//            objDrfTransactionTO.setResolutionDate(objTO.getResolutionDt());
//            objDrfTransactionTO.setResolutionNo(objTO.getResolutionNo());
//             
//               System.out.println("objDrfTransactionTO"+objDrfTransactionTO);
//              sqlMap.executeUpdate("insertDrfTransDetailsTO", objDrfTransactionTO);
//              
//              
//              ////////////////////////////////////////////////////////////////////






                }

            }
            if (governmentsShare > 0.0) {
                txMap = new HashMap();
                txMap.put(TransferTrans.PARTICULARS, newMap.get("SHARE APPLICATION NO") + "_" + newMap.get("SHARE ACCOUNT DETAIL NO"));
                txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.DR_AC_HD, (String) ratificMap.get("GOVT_SUBSIDY_ACHD"));
                txMap.put("generateSingleTransId",singleTransId);
                txMap.put("AUTHORIZEREMARKS", "GOVT_SUBSIDY_ACHD");
                if (governmentsShare > 0.0) {
                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, governmentsShare));
                    txMap.put(TransferTrans.CR_AC_HD, (String) ratificMap.get("SHARE_ACHD"));
                    txMap.put("AUTHORIZEREMARKS", "SHARE_ACHD");
                    txMap.put("HIERARCHY", "1");
                    txMap.put("TRANS_MOD_TYPE","SH");
                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, governmentsShare));
                }
            }
//                            }
//                        }
//                    }
//                }
//            }
        }

        objTransferTrans.doDebitCredit(transferList, _branchCode, true);
    }

    public void updateshareDrfData(HashMap ShareData) {
        try {
            HashMap testMap = new HashMap();
            testMap.put("SHARE_APPL_NO", ShareData.get("SHARE APPLICATION NO"));

            List drflist = sqlMap.executeQueryForList("CheckDrfApplicable", testMap);
            if (drflist != null && drflist.size() > 0) {
                testMap = (HashMap) drflist.get(0);
                String drf_Prod_Id = "";
                double drfAmount = 0.0;
                String drfApplicable = testMap.get("DRF_APPLICABLE").toString();
                if (drfApplicable != null && !drfApplicable.equals("") && drfApplicable.equals("Y")) {
                    drf_Prod_Id = testMap.get("DRF_PRODUCT").toString();
                    List details = sqlMap.executeQueryForList("getDrfDts", testMap);
                    HashMap detailsMap = (HashMap) details.get(0);
                    drfAmount = Double.parseDouble(detailsMap.get("AMOUNT").toString());
                }

                ////////////////////////////////////////////////////////////////


                DrfTransactionTO objDrfTransactionTO = new DrfTransactionTO();
                DrfTransactionDAO d = new DrfTransactionDAO();
                System.out.println("branchCode" + _branchCode);
                //currDt = currDt.clone();
                objDrfTransactionTO.setDrfTransID(d.getDrfTrans_No_For_Share_Resolution());
                objDrfTransactionTO.setCommand("INSERT");
                objDrfTransactionTO.setStatus(CommonConstants.STATUS_CREATED);
                objDrfTransactionTO.setTxtDrfTransMemberNo(ShareAcctNo);
                objDrfTransactionTO.setTxtDrfTransAmount(new Double(0) + drfAmount);
//            objgetDrfTransactionTO.setTxtDrfTransName(CommonUtil.convertObjToStr(whereMap.get("DRF_MEMBER_NAME")));
                objDrfTransactionTO.setCboDrfTransProdID(drf_Prod_Id);
//            objgetDrfTransactionTO.setDrfProdPaymentAmt(CommonUtil.convertObjToStr(whereMap.get("DRF_PRODUCT_AMOUNT")));
//            objgetDrfTransactionTO.setDrfProductAmount(CommonUtil.convertObjToStr(whereMap.get("DRF_AMOUNT")));
                objDrfTransactionTO.setRdoDrfTransaction("RECIEPT");
                objDrfTransactionTO.setChkDueAmtPayment("N");
                objDrfTransactionTO.setStatusBy(userid);
                objDrfTransactionTO.setStatusDate(currDt);
                objDrfTransactionTO.setInitiatedBranch(branchcode);
                objDrfTransactionTO.setAuthorizeBy(userid);
                objDrfTransactionTO.setAuthorizeDate(currDt);
                objDrfTransactionTO.setAuthorizeStatus("AUTHORIZED");
                objDrfTransactionTO.setResolutionDate(objTO.getResolutionDt());
                objDrfTransactionTO.setResolutionNo(objTO.getResolutionNo());
                System.out.println("currDt" + currDt);
                System.out.println("objDrfTransactionTO" + objDrfTransactionTO);
                sqlMap.executeUpdate("insertDrfTransDetailsTO", objDrfTransactionTO);
            }
        } catch (Exception e) {
        }
    }

    private void updateResolutionData(String status, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {

            sqlMap.executeUpdate("insertShareResolutionTO", objTO);
            objLogTO.setData(objTO.toString());
            objLogTO.setPrimaryKey(objTO.getKeyData());
            objLogDAO.addToLog(objLogTO);
            updateShareResolutionData(status, objLogDAO, objLogTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateResolutionRejectData(String status, LogDAO objLogDAO, LogTO objLogTO, HashMap map) throws Exception {
        try {

            sqlMap.executeUpdate("insertShareResolutionTO", objTO);
            objLogTO.setData(objTO.toString());
            objLogTO.setPrimaryKey(objTO.getKeyData());
            objLogDAO.addToLog(objLogTO);
            updateShareResolutionRejectData(status, objLogDAO, objLogTO, map);
            // sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void executeTransactionPart(HashMap map, HashMap transDetailMap) throws Exception {
        try {
            //transDetailMap.put("ACCT_NUM", acct_No);
            transDetailMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
            System.out.println("@##$#$% map #### :" + map);
            System.out.println("@##$#$% transDetailMap #### :" + transDetailMap);
            if (transDetailMap != null && transDetailMap.size() > 0) {
                if (transDetailMap.containsKey("TRANSACTION_PART")) {
                    //transDetailMap = getProdBehavesLike(transDetailMap);
                    insertTimeTransaction(map, transDetailMap);
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void insertTimeTransaction(HashMap map, HashMap dataMap) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap acHeads = new HashMap();
        TransactionDAO transactionDAO = null;
        TxTransferTO transferDao = null;
        ArrayList transList = new ArrayList();
        HashMap txMap = new HashMap();
        TransferTrans transferTrans = new TransferTrans();
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        System.out.println("mapTRANSACTION_PART " + map + "dataMap :" + dataMap);
        if (dataMap.containsKey("TRANSACTION_PART")) {
            HashMap reserchMap = new HashMap();
            String branchId = CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE"));
            try {
                // dataMap.put("ACT_NUM",dataMap.get("ACCT_NUM"));
                //acHeads = (HashMap)sqlMap.executeQueryForObject("getLoanAccountClosingHeads", dataMap.get("ACT_NUM"));
                // System.out.println("acHeads "+acHeads);
                // double appraiserAmt = CommonUtil.convertObjToDouble(dataMap.get("APPRAISER_AMT")).doubleValue();
                // double serviceTaxAmt = CommonUtil.convertObjToDouble(dataMap.get("SERVICE_TAX_AMT")).doubleValue();
                if (dataMap.get("TRANS_TYPE") != null && dataMap.get("TRANS_TYPE").equals("CASH")) {
                    HashMap loanAuthTransMap = new HashMap();
                    loanAuthTransMap.put("SELECTED_BRANCH_ID", dataMap.get("BRANCH_CODE"));
                    loanAuthTransMap.put("ACCT_NUM", dataMap.get("ACCT_NUM"));
                    loanAuthTransMap.put("PROD_ID", dataMap.get("PROD_ID"));
                    loanAuthTransMap.put("BRANCH_CODE", dataMap.get("BRANCH_CODE"));
                    loanAuthTransMap.put("ACCT_HEAD", acHeads.get("ACCT_HEAD"));
                    loanAuthTransMap.put("TOKEN_NO", dataMap.get("TOKEN_NO"));
                    loanAuthTransMap.put("LIMIT", dataMap.get("LIMIT"));
                    loanAuthTransMap.put("LOANDEBIT", "LOANDEBIT");
                    loanAuthTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                    loanAuthTransMap.put("USER_ID", dataMap.get("USER_ID"));
                    // ArrayList loanAuthTransList = loanAuthorizeTimeTransaction(loanAuthTransMap);
                    //loanAuthTransMap.put("DAILYDEPOSITTRANSTO",loanAuthTransList);
                    loanAuthTransMap.put("DEBIT_LOAN_TYPE", "DP");
                    CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                    HashMap cashMap = cashTransactionDAO.execute(loanAuthTransMap, false);
                    System.out.println("cashMap :" + cashMap);
                    cashMap.put("BRANCH_CODE", dataMap.get("BRANCH_CODE"));
                    String authorizeStatus = CommonConstants.STATUS_AUTHORIZED;
                    ArrayList arrList = new ArrayList();
                    HashMap authDataMap = new HashMap();
                    HashMap authStatusMap = new HashMap();
                    arrList = null;
                    returnMap = null;
                    acHeads = null;
                    txMap = null;
                    reserchMap = null;
                    loanAuthTransMap = null;
                    authDataMap = null;
                    authStatusMap = null;
                } else if (dataMap.get("TRANS_TYPE") != null && dataMap.get("TRANS_TYPE").equals("TRANSFER")) {
                    listShareAcctDet = (ArrayList) map.get("ShareAccDet");
                    HashMap h = new HashMap();
                    h = (HashMap) listShareAcctDet.get(0);
                    String applNo = CommonUtil.convertObjToStr(h.get("SHARE APPLICATION NO"));
                    String shareType = CommonUtil.convertObjToStr(h.get("SHARE TYPE"));
                    String userid = objTO.getStatusBy();
                    String branchcode = CommonUtil.convertObjToStr(h.get("BRANCH CODE"));
                    String sharedetno = CommonUtil.convertObjToStr(h.get("SHARE ACCOUNT DETAIL NO"));

                    HashMap hmap = new HashMap();
                    hmap.put("SHARE APPLICATION NO", applNo);
                    HashMap shareFees = (HashMap) sqlMap.executeQueryForObject("getShareFeeDetails", hmap);
                    double shareValue = CommonUtil.convertObjToDouble(shareFees.get("SHARE_VALUE")).doubleValue();
                    double sharefee = CommonUtil.convertObjToDouble(shareFees.get("SHARE_FEE")).doubleValue();
                    double shareApplFee = CommonUtil.convertObjToDouble(shareFees.get("SHARE_APPL_FEE")).doubleValue();
                    double shareMemFee = CommonUtil.convertObjToDouble(shareFees.get("SHARE_MEM_FEE")).doubleValue();
                    double totamount = shareValue + sharefee + shareApplFee + shareMemFee;
                    transactionDAO.setLinkBatchID(applNo + "_" + sharedetno);
                    transactionDAO.setInitiatedBranch(_branchCode);
                    HashMap crMap = new HashMap();
                    crMap.put("ACT_NUM", dataMap.get("CR_ACT_NUM"));
                    List oaAcctHead = sqlMap.executeQueryForList("getAccNoProdIdDet", crMap);
                    if (oaAcctHead != null && oaAcctHead.size() > 0) {
                        crMap = (HashMap) oaAcctHead.get(0);
                    }
                    txMap = new HashMap();
                    ArrayList transferList = new ArrayList();
                    TxTransferTO transferTo = new TxTransferTO();
                    if (map.containsKey("SHARE_FEE")) {

                        double interestAmt = totamount - sharefee;


                        /**
                         * txMap.put(TransferTrans.DR_ACT_NUM,
                         * dataMap.get("ACT_NUM"));
                         * txMap.put(TransferTrans.DR_AC_HD,
                         * acHeads.get("ACCT_HEAD"));
                         * txMap.put(TransferTrans.DR_PROD_ID,
                         * dataMap.get("PROD_ID"));
                         * txMap.put(TransferTrans.DR_PROD_TYPE,
                         * TransactionFactory.LOANS);
                         * txMap.put(TransferTrans.DR_BRANCH,
                         * dataMap.get("BRANCH_CODE"));
                         * txMap.put(TransferTrans.PARTICULARS, "disbursement -
                         * "+dataMap.get("CR_ACT_NUM"));
                         * txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
                         * txMap.put(TransferTrans.DR_INST_TYPE,"VOUCHER");
                         * txMap.put("AUTHORIZEREMARKS", "DP");
                         * txMap.put("DEBIT_LOAN_TYPE", "DP");*
                         */
                        txMap.put(TransferTrans.CR_ACT_NUM, dataMap.get("CR_ACT_NUM"));
                        txMap.put(TransferTrans.CR_AC_HD, crMap.get("AC_HD_ID"));
                        txMap.put(TransferTrans.CR_PROD_ID, crMap.get("PROD_ID"));
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                        txMap.put(TransferTrans.CR_BRANCH, dataMap.get("BRANCH_CODE"));
                        txMap.put("generateSingleTransId",singleTransId);
                        System.out.println("txMap  ###" + txMap + "transferDao   :" + transferDao);

                        transferTo = transactionDAO.addTransferDebitLocal(txMap, interestAmt);
                        transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                        transferTo.setLinkBatchId(applNo + "_" + sharedetno);
                        transferList.add(transferTo);
                        txMap.put(TransferTrans.PARTICULARS, applNo);
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, interestAmt);
                        transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                        transferTo.setLinkBatchId(applNo + "_" + sharedetno);
                        transferList.add(transferTo);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        transactionDAO.doTransferLocal(transferList, CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));

                        transList = null;
                        transactionDAO = null;
                        transferDao = null;
                        txMap = null;
                        acHeads = null;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            transList = null;
            transactionDAO = null;
            transferDao = null;
            txMap = null;
            acHeads = null;
        }
    }

    /**
     * private HashMap getProdBehavesLike(HashMap dataMap) throws Exception{
     * List list = (List) sqlMap.executeQueryForList("TermLoan.getBehavesLike",
     * dataMap); if (list.size() > 0){ HashMap retrieveMap = (HashMap)
     * list.get(0); dataMap.put(PROD_ID, retrieveMap.get(PROD_ID));
     * dataMap.put("prodId", retrieveMap.get(PROD_ID)); dataMap.put(LIMIT,
     * retrieveMap.get(LIMIT)); dataMap.put(BEHAVES_LIKE,
     * retrieveMap.get(BEHAVES_LIKE)); dataMap.put(INT_GET_FROM,
     * retrieveMap.get(INT_GET_FROM)); dataMap.put(SECURITY_DETAILS,
     * retrieveMap.get(SECURITY_DETAILS)); System.out.println("dataMap
     * ##"+dataMap); retrieveMap = null; } list = null; return dataMap; }  *
     */
    private void updateShareResolutionRejectData(String status, LogDAO objLogDAO, LogTO objLogTO, HashMap map) throws Exception {
        listShareAcctDet = (ArrayList) map.get("ShareAccDet");
        int sizeListShareAcctDet = listShareAcctDet.size();
        //shareAcctDet = (HashMap) listShareAcctDet.get(0);
        //shareAcctDet.put("STATUS", status);
        // shareAcctDet.put("RESOLUTION_NO", objTO.getResolutionNo());
        HashMap h = new HashMap();
        h = (HashMap) listShareAcctDet.get(0);
        String applNo = CommonUtil.convertObjToStr(h.get("SHARE APPLICATION NO"));
        sqlMap.executeUpdate("updateShareAccReject", applNo);
        sqlMap.executeUpdate("updateShareAccDetReject", applNo);
    }

    private void updateShareResolutionData(String status, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        int sizeListShareAcctDet = listShareAcctDet.size();
        System.out.println("sizeListShareAcctDet" + sizeListShareAcctDet);
        currDt = (Date) currDt.clone();
        ArrayList shareList = new ArrayList();
        for (int i = 0; i < sizeListShareAcctDet; i++) {
            try {
                shareAcctDet = (HashMap) listShareAcctDet.get(i);
                shareAcctDet.put("STATUS", status);
                shareAcctDet.put("RESOLUTION_NO", objTO.getResolutionNo());
                shareAcctDet.put("CURR_DT", currDt);
                System.out.println("shareAcctDet : " + shareAcctDet);
                if (objTO.getCommand().equals(CommonConstants.TOSTATUS_RESOLUTION_ACCEPT)) {
                    getShareAcct_No(shareAcctDet);
                    System.out.println("chkCurDt" + currDt);
                    updateshareDrfData(shareAcctDet);
                }
                // Generateing Share Nos
                HashMap genShareNoMap = generateShareNo(shareAcctDet);
                genShareNoMap.put("SHARE ACCOUNT NO", ShareAcctNo);
                genShareNoMap.put("CURR_DT", currDt);
                System.out.println("the genShareNoMap is"+genShareNoMap );
                shareAcctDet.put("SHARE_ACCOUNT_NO",ShareAcctNo);
          		System.out.println("ShareAcctClass : " + ShareAcctClass);
                shareAcctDet.put("SHARE_ACCOUNT_CLASS",ShareAcctClass);
                
                
                
                
                sqlMap.executeUpdate("updateResolutionShareStatus",genShareNoMap);
                sqlMap.executeUpdate("updateIdIssDtShareApp",shareAcctDet);
                sqlMap.executeUpdate("updatesharecustomer",shareAcctDet);
               
                
                //Updating Customer Membership_class (created by Rajesh)
                //added by jiv for multiple type of share
                if (multiShareAllowed.equals("Y")) {
                    List chkList = sqlMap.executeQueryForList("getMembershipPriorityofCustomer", shareAcctDet);
                    if (chkList != null && chkList.size() > 0) {
                        HashMap oldMap = (HashMap) chkList.get(0);
                        int old_priority = CommonUtil.convertObjToInt(oldMap.get("PRIORITY"));
                        HashMap newMap = (HashMap) (sqlMap.executeQueryForList("getShare_priority", shareAcctDet));
                        int new_priority = CommonUtil.convertObjToInt(newMap.get("PRIORITY"));
                        if (new_priority > old_priority) {
                            sqlMap.executeUpdate("updateMembershipShareCust", shareAcctDet);
                        }
                    } else {
                        sqlMap.executeUpdate("updateMembershipShareCust", shareAcctDet);
                    }
                } else {
                    sqlMap.executeUpdate("updateMembershipShareCust", shareAcctDet);
                }
                sqlMap.executeUpdate("updateRemitIssue", shareAcctDet);
                sqlMap.executeUpdate("updateNomineeShareAccNo", shareAcctDet);

                // Tranfer from Suspense to A/c heads
                //doResolution(shareAcctDet);
                
                shareList.add(ShareAcctNo);
                
                objLogTO.setData(objTO.toString());
                objLogTO.setPrimaryKey(objTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        execReturnMap.put(CommonConstants.TRANS_ID, shareList);
        execReturnMap.put("SingleTransID", singleTransId);
        
    }

    //--- Generate ShareNo.From and To.
    public HashMap generateShareNo(HashMap shareAcctDet) throws Exception {
        int noOfShares = CommonUtil.convertObjToInt(shareAcctDet.get("NO. OF SHARES"));
        String shareType = CommonUtil.convertObjToStr(shareAcctDet.get("SHARE TYPE"));
        //            if(shareType.equals("REGULAR")){
        String regShareNoTo = (String) (executeQueryShareNoGen(shareAcctDet)).get(CommonConstants.DATA);
        shareAcctDet.put("SHARE_CERT_ISSUE_DT", objTO.getResolutionDt());
        shareAcctDet.put("SHARE_NO_FROM", regShareNoFrom);
        shareAcctDet.put("SHARE_NO_TO", regShareNoTo);
        //            }else  if(shareType.equals("AA")){
        //                String regShareNoTo =  (String) (executeQueryShareNoGen(shareAcctDet)).get(CommonConstants.DATA);
        //                shareAcctDet.put("SHARE_CERT_ISSUE_DT", objTO.getResolutionDt());
        //                shareAcctDet.put("SHARE_NO_FROM", regShareNoFrom);
        //                shareAcctDet.put("SHARE_NO_TO", regShareNoTo);
        //            }
        //            else if(shareType.equals("ASSOCIATE")){
        //                String regShareNoTo =  (String) (executeQueryShareNoGen(shareAcctDet)).get(CommonConstants.DATA);
        //                shareAcctDet.put("SHARE_CERT_ISSUE_DT", objTO.getResolutionDt());
        //                shareAcctDet.put("SHARE_NO_FROM", regShareNoFrom);
        //                shareAcctDet.put("SHARE_NO_TO", regShareNoTo);
        //            }else if(shareType.equals("NOMINAL")){
        //                String regShareNoTo =  (String) (executeQueryShareNoGen(shareAcctDet)).get(CommonConstants.DATA);
        //                shareAcctDet.put("SHARE_CERT_ISSUE_DT", objTO.getResolutionDt());
        //                shareAcctDet.put("SHARE_NO_FROM", regShareNoFrom);
        //                shareAcctDet.put("SHARE_NO_TO", regShareNoTo);
        //            }
        return shareAcctDet;
    }

    private HashMap executeQueryShareNoGen(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getDataShareNoGen(obj);
    }

    private HashMap getDataShareNoGen(HashMap map) throws Exception {
        HashMap result = null;
        try {
            HashMap hash = null;

            List list = (List) sqlMap.executeQueryForList("getCurrentShareNo", map);
            map.put("NO_OF_SHARES", map.get("NO. OF SHARES"));

            if (list.size() > 0) {
                sqlMap.executeUpdate("updateShareNoGenerated", map);
                hash = (HashMap) list.get(0);

                int lastShareNo = CommonUtil.convertObjToInt(hash.get("LAST_SHARE_NO"));

                regShareNoFrom = String.valueOf(lastShareNo + 1);
                String newID = String.valueOf(lastShareNo + CommonUtil.convertObjToInt(map.get("NO. OF SHARES")));
                System.out.println("newID:" + newID);
                result = new HashMap();
                result.put(CommonConstants.DATA, newID);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            throw new TransRollbackException(exc);
        }
        return result;
    }

    public static void main(String str[]) {
        try {
            ShareResolutionDAO dao = new ShareResolutionDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        singleTransId = "";
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        singleTransId = generateLinkID();
        HashMap multiShareMap = (HashMap) (sqlMap.executeQueryForList("getMultiShareAllowed", map)).get(0);
        if (multiShareMap.get("MULTI_SHARE_ALLOWED") != null) {
            multiShareAllowed = multiShareMap.get("MULTI_SHARE_ALLOWED").toString();
        } else {
            multiShareAllowed = "N";
        }
        LogDAO objLogDAO = new LogDAO();
        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        branchId = objLogTO.getBranchId();
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));

        listShareAcctDet = (ArrayList) map.get("ShareAccDet");
        System.out.println("listShareAcctDet#######"+listShareAcctDet);
        objTO = (ShareResolutionTO) map.get("ShareResolution");
        userid = CommonUtil.convertObjToStr(map.get("USER_ID"));
        branchcode = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
        final String command = objTO.getCommand();
        //		if (command.equals(CommonConstants.TOSTATUS_INSERT)){
        //			insertData();
        //		} else if (command.equals(CommonConstants.TOSTATUS_UPDATE)){
        //			updateData();
        //		} else if (command.equals(CommonConstants.TOSTATUS_DELETE)){
        //			deleteData();
        //		} else {
        //			throw new NoCommandException();
        //		}
        if (command.equals(CommonConstants.TOSTATUS_RESOLUTION_ACCEPT)) {
            //   getShareAcct_No();
            ActionOnResolutionData(CommonConstants.STATUS_RESOLUTION_ACCEPT, objLogDAO, objLogTO, map);

            // execReturnMap.put(CommonConstants.TRANS_ID, ShareAcctNo );
            updateResolutionData(CommonConstants.STATUS_RESOLUTION_ACCEPT, objLogDAO, objLogTO);
        } else if (command.equals(CommonConstants.TOSTATUS_RESOLUTION_REJECT)) {
            ActionOnResolutionRejectData(CommonConstants.STATUS_RESOLUTION_REJECT, objLogDAO, objLogTO, map);
            updateResolutionRejectData(CommonConstants.STATUS_RESOLUTION_REJECT, objLogDAO, objLogTO, map);
        } else if (command.equals(CommonConstants.TOSTATUS_RESOLUTION_DEFFERED)) {
            updateResolutionData(CommonConstants.STATUS_RESOLUTION_DEFFERED, objLogDAO, objLogTO);
        } else {
            throw new NoCommandException();
        }

        destroyObjects();
        return execReturnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
//        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
        shareAcctDet = null;
        listShareAcctDet = null;
    }

    private void doResolution(HashMap shareAcctNoMap) throws Exception {
        /*
         * Execute query to get all amounts from Share_Acct and
         * Share_Acct_Details Save all the values in variables. Do transfer
         * Method needs to be called in Resolution screen... Needs to be run
         * inside a transaction block
         */
        HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("transferResolvedShare", shareAcctNoMap);

        String shareType = CommonUtil.convertObjToStr(acHeads.get("SHARE_TYPE"));
        double shareAmt = CommonUtil.convertObjToDouble(acHeads.get("SHARE_AMOUNT")).doubleValue();
        double shareFee = CommonUtil.convertObjToDouble(acHeads.get("SHARE_FEE")).doubleValue();
        double memberFee = CommonUtil.convertObjToDouble(acHeads.get("MEM_FEE")).doubleValue();
        double applnFee = CommonUtil.convertObjToDouble(acHeads.get("APPL_FEE")).doubleValue();
        int noOfShares = CommonUtil.convertObjToInt(acHeads.get("NO_OF_SHARES"));

        double total = (shareAmt * noOfShares) + (shareFee * noOfShares) + memberFee + applnFee;

        System.out.println("total : " + total);

        shareAmt = shareAmt * noOfShares;
        shareFee = shareFee * noOfShares;
        acHeads = null;

        acHeads = (HashMap) sqlMap.executeQueryForObject("getShareSuspenseAcHd", shareType);

        HashMap map = new HashMap();
        map.put(TransferTrans.DR_BRANCH, ServerConstants.HO == null ? "" : ServerConstants.HO);
        map.put(TransferTrans.CURRENCY, LocaleConstants.DEFAULT_CURRENCY);
        map.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(acHeads.get("SHARE_SUSPENSE_ACHD")));
        map.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);

        TransferTrans trans = new TransferTrans();
        trans.setInitiatedBranch("Bran");
        ArrayList batchList = new ArrayList();
        batchList.add(trans.getDebitTransferTO(map, total));
        map = null;

        if (memberFee != 0) {
            map = new HashMap();
            map.put(TransferTrans.CR_BRANCH, ServerConstants.HO == null ? "" : ServerConstants.HO);
            map.put(TransferTrans.CR_AC_HD, (String) acHeads.get("MEMBERSHIP_FEE_ACHD"));
            map.put(TransferTrans.CURRENCY, "INR");
            map.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            batchList.add(trans.getCreditTransferTO(map, memberFee));
            map = null;
        }
        if (applnFee != 0) {
            map = new HashMap();
            map.put(TransferTrans.CR_BRANCH, ServerConstants.HO == null ? "" : ServerConstants.HO);
            map.put(TransferTrans.CR_AC_HD, (String) acHeads.get("APPLICATION_FEE_ACHD"));
            map.put(TransferTrans.CURRENCY, "INR");
            map.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            batchList.add(trans.getCreditTransferTO(map, applnFee));
            map = null;
        }
        if (shareFee != 0) {
            map = new HashMap();
            map.put(TransferTrans.CR_BRANCH, ServerConstants.HO == null ? "" : ServerConstants.HO);
            map.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SHARE_FEE_ACHD"));
            map.put(TransferTrans.CURRENCY, "INR");
            map.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            batchList.add(trans.getCreditTransferTO(map, shareFee));
            map = null;
        }
        if (shareAmt != 0) {
            map = new HashMap();
            map.put(TransferTrans.CR_BRANCH, ServerConstants.HO == null ? "" : ServerConstants.HO);
            map.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SHARE_ACHD"));
            map.put(TransferTrans.CURRENCY, "INR");
            map.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            batchList.add(trans.getCreditTransferTO(map, shareAmt));
            map = null;
        }

        trans.doDebitCredit(batchList, _branchCode);
        acHeads = null;
        batchList = null;
        trans = null;
    }
        private String generateLinkID() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
            System.out.println("Branch code"+_branchCode+"1 "+branchcode);
        where.put(CommonConstants.MAP_WHERE, "GENERATE_LINK_ID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//for trans_batch_id resetting, branch code
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        String batchID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        return batchID;
    }
}
