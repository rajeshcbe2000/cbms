/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DividendCalcTask.java
 *
 * Created on January 20, 2005, 6:28 PM
 */
package com.see.truetransact.serverside.batchprocess.task.share;

import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.common.transaction.TransferTransLog;
import com.see.truetransact.transferobject.common.transaction.FailureTxTransferTO;
import com.see.truetransact.transferobject.batchprocess.share.DividendBatchTO;
import com.see.truetransact.transferobject.product.share.ShareProductTO;
import com.see.truetransact.commonutil.CommonConstants;
import com.ibatis.db.sqlmap.SqlMap;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.remittance.RemittanceIssueDAO;
import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;
import com.see.truetransact.transferobject.remittance.RemittanceIssueTO;
import com.see.truetransact.serverside.batchprocess.task.operativeaccount.interest.DepositIntTask;

/**
 *
 * @author K.R.Jayakrishnan
 */
public class DividendCalcTask extends Task {

    private static SqlMap sqlMap = null;
    private static SqlMap sqlBatchMap = null;
    List listShareAcctDet;
    DividendBatchTO dividendBatchTO;
    ShareProductTO shareProductTO;
    TransferTrans transferTrans;
    Date todayDate = ServerUtil.getCurrentDate(super._branchCode);
    String BRAN = ServerConstants.HO == null ? "" : ServerConstants.HO;
    int daysinYear = 365;
    String payFlag = "";
    private String actBranch = _branchCode;
    private HashMap parmMap = null;
    private Date checkThisCDate = null;
    private TaskStatus status;

    /**
     * Creates a new instance of DividendCalcTask
     */
    public DividendCalcTask() throws Exception {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    /**
     * Creates a new instance of FolioChargesTask
     */
    public DividendCalcTask(TaskHeader header) throws Exception {
        System.out.println("#####DividendCalcTask : " + header);

        setHeader(header);
        parmMap = header.getTaskParam();
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            DividendCalcTask dct = new DividendCalcTask();
            dct.executeTask();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TaskStatus executeTask() throws Exception {
        status = new TaskStatus();
        //        try{

        status.setStatus(BatchConstants.STARTED);

        //--- Start the Transaction
        HashMap whereMap = new HashMap();
        //        System.out.println("parmMap-------------------->"+parmMap);
        if (CommonUtil.convertObjToStr(parmMap.get("PRODUCT_ID")).length() > 0) {
            whereMap.put("SHARE_TYPE", CommonUtil.convertObjToStr(parmMap.get("PRODUCT_ID")));
        }

        //        sqlMap.startTransaction();

        whereMap.put("CURR_DATE", ServerUtil.getCurrentDate(_branchCode));
        listShareAcctDet = (List) sqlMap.executeQueryForList("getSelectDividendCalProd", whereMap);
        if (listShareAcctDet != null && listShareAcctDet.size() > 0) {
            //            doTask(listShareAcctDet);
            //            doTransferUnclamiedDividend(listShareAcctDet);
            if (CommonUtil.convertObjToStr(parmMap.get("PROCESS")).length() > 0 && CommonUtil.convertObjToStr(parmMap.get("PROCESS")).equals("TRANSFER")) {
                getPendingDividentTransferToAccount(listShareAcctDet);
            } else if (CommonUtil.convertObjToStr(parmMap.get("PROCESS")).length() > 0 && CommonUtil.convertObjToStr(parmMap.get("PROCESS")).equals("CALCULATION")) {
                doTask(listShareAcctDet);
            } else if (CommonUtil.convertObjToStr(parmMap.get("PROCESS")).length() > 0 && CommonUtil.convertObjToStr(parmMap.get("PROCESS")).equals("UNCLAMIED")) {
                doTransferUnclamiedDividend(listShareAcctDet);
            }
        }

        //        sqlMap.commitTransaction();
        //--- End of transaction
        status.setStatus(BatchConstants.COMPLETED);
        whereMap = null;
        //        }catch (Exception e) {
        //            status.setStatus(BatchConstants.ERROR);
        //            sqlMap.rollbackTransaction();
        //            e.printStackTrace();
        ////            log.error(e);
        //            System.out.println("e : " + e);
        //            throw e;
        //        }
        return status;

    }

    private void doTransferUnclamiedDividend(List listShareAcctDet) throws Exception {
        HashMap mapShareAcctDet;
        double dividend = 0.0;
        System.out.println("In Side the Unclamied");
        int listShareAcctDetSize = listShareAcctDet.size();
        for (int i = 0; i < listShareAcctDetSize; i++) {
            mapShareAcctDet = (HashMap) listShareAcctDet.get(i);
            HashMap prodMap = new HashMap();

            String shareType = CommonUtil.convertObjToStr(mapShareAcctDet.get("SHARE_TYPE"));
            Date lastUnclaimedCalDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(mapShareAcctDet.get("LAST_UNCLAIMED_TRANSFERUPTO")));
            Date lastUnclaimedRunDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(mapShareAcctDet.get("UNCLAIMED_RUN_DATE")));
            int unclaimedPeriod = CommonUtil.convertObjToInt(mapShareAcctDet.get("UNCLAIMED_DIVIDEND_PERIOD"));
            unclaimedPeriod = unclaimedPeriod * -1;
            if (lastUnclaimedRunDt != null && lastUnclaimedRunDt.getDate() > 0) {
                Date unClaimedYear = DateUtil.addDays(lastUnclaimedCalDt, unclaimedPeriod);

                mapShareAcctDet.put("UPTO_DIVIDEND_PAID_DATE", DateUtil.addDays(unClaimedYear, 365));
                mapShareAcctDet.put("NEXT_DUE_DATE", DateUtil.addDays(unClaimedYear, 365));
                //                mapShareAcctDet.put("NEXT_DUE_DATE", DateUtil.addDays(lastUnclaimedCalDt, 365));
                holiydaychecking(lastUnclaimedRunDt);
                Date dbDate = ServerUtil.getCurrentDate(_branchCode);
                if (DateUtil.dateDiff(checkThisCDate, dbDate) >= 0) {

                    mapShareAcctDet.put("UNCLAIMED_DEVIDEND_DATE", unClaimedYear);
                    mapShareAcctDet.put("DIVIDEND_PAID_STATUS","DIVIDEND_PAID_STATUS");
                    List lst = ServerUtil.executeQuery("getSelectDividendUnclaimedTransferList", mapShareAcctDet);
                    if (lst != null && lst.size() > 0) {
                        for (int j = 0; j < lst.size(); j++) {
                            HashMap unClaimedMap = new HashMap();
                            unClaimedMap = (HashMap) lst.get(j);
                            mapShareAcctDet.put("SHARE_ACCT_NO", CommonUtil.convertObjToStr(unClaimedMap.get("SHARE_ACCT_NO")));
                            Date unDivUptoDate = DateUtil.addDays(unClaimedYear, 365);
                            System.out.println("unDivUptoDate------------>" + unDivUptoDate);
                            unClaimedMap.put("UNCLAIMED_DEVIDEND_DATE1", unDivUptoDate);
                            List unClaimedLst = ServerUtil.executeQuery("getSelectDividendUnclaimedIndividualList", unClaimedMap);
                            if (unClaimedLst != null && unClaimedLst.size() > 0) {
                                HashMap unClaimedAmtMap = new HashMap();
                                unClaimedAmtMap = (HashMap) unClaimedLst.get(0);
                                System.out.println("mapShareAcctDet------------>" + mapShareAcctDet);
                                try {

                                    sqlMap.startTransaction();
                                    transferUncalmiedDividend(unClaimedAmtMap, mapShareAcctDet);
                                    sqlMap.commitTransaction();
                                    //--- End of transaction
                                } catch (Exception e) {
                                    sqlMap.rollbackTransaction();
                                    e.printStackTrace();
                                    //            log.error(e);
                                    System.out.println("e : " + e);
                                }
                                unClaimedAmtMap = null;
                                unClaimedLst = null;
                                unClaimedLst = null;
                            }
                            unClaimedMap = null;
                        }
                    }
                    mapShareAcctDet.put("LAST_UNCLAIMED_TRANSFERUPTO", DateUtil.addDays(lastUnclaimedCalDt, 365));
                    System.out.println("mapShareAcctDet----------->" + mapShareAcctDet);
                    sqlMap.executeUpdate("UnClaimedDividend.updateShareProductNextDueDate", mapShareAcctDet);
                    mapShareAcctDet = null;
                }
            }
        }

    }

    private void transferUncalmiedDividend(HashMap unClaimedAmtMap, HashMap mapShareAcctDet) throws Exception {
        System.out.println("InSide transfer");
        double unClaimeddividend = 0.0;
        unClaimeddividend = CommonUtil.convertObjToDouble(unClaimedAmtMap.get("UNCLAIMEDAMT")).doubleValue();
        payFlag = "Unclaimed";
        System.out.println("unClaimeddividend    ---------->" + unClaimeddividend);
        if (unClaimeddividend > 0.0) {
            System.out.println("Branch_code-------------->" + _branchCode);
            mapShareAcctDet.put("BRANCH_CODE", _branchCode);
            Double dblDividend = new Double(unClaimeddividend);
            mapShareAcctDet.put("DIVIDEND_PAYMENT_ACHD", (String) mapShareAcctDet.get("DIVIDEND_PAYABLE_ACHD"));
            mapShareAcctDet.put("DIVIDEND_PAYABLE_ACHD", (String) mapShareAcctDet.get("UNCLAIMED_DIVIDEND_TRF_ACHD"));
            mapShareAcctDet.put("UNCLAIMED", "UNCLAIMED");
            transferAmount(mapShareAcctDet, unClaimeddividend);
        }
    }

    private void doTask(List listShareAcctDet) throws Exception {
        HashMap mapShareAcctDet;
        double dividend = 0.0;
        int listShareAcctDetSize = listShareAcctDet.size();
        for (int i = 0; i < listShareAcctDetSize; i++) {
            mapShareAcctDet = (HashMap) listShareAcctDet.get(i);
            HashMap prodMap = new HashMap();

            String shareType = CommonUtil.convertObjToStr(mapShareAcctDet.get("SHARE_TYPE"));
            Date lastCalDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(mapShareAcctDet.get("LAST_DIVIDEND_CALC")));
            Date nextCalDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(mapShareAcctDet.get("NEXT_DUE_DATE")));
            Date divRunDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(mapShareAcctDet.get("DIV_RUN_DATE")));
            double divPer = CommonUtil.convertObjToDouble(mapShareAcctDet.get("PERCENTAGE_DIVIDEND")).doubleValue();


            System.out.println("divRunDate------------->" + divRunDate + "divPer---------------->" + divPer + "Branch_code --->" + _branchCode);
            System.out.println("lastCalDt------------->" + lastCalDt);
            if (divRunDate != null && divRunDate.getDate() > 0 && divPer > 0.0) {
                holiydaychecking(divRunDate);
                Date dbDate = ServerUtil.getCurrentDate(_branchCode);
                if (DateUtil.dateDiff(checkThisCDate, dbDate) >= 0) {
                    System.out.println();
                    if (lastCalDt != null && lastCalDt.getDate() > 0) {
                        int applFreq = CommonUtil.convertObjToInt(mapShareAcctDet.get("DIVIDEND_APPL_FREQUENCY"));

                        nextCalDt = DateUtil.addDays(lastCalDt, applFreq);
                        if (DateUtil.dateDiff(nextCalDt, divRunDate) >= 0) {
                            mapShareAcctDet.put("NEXT_DUE_DATE", nextCalDt);
                            List lst = ServerUtil.executeQuery("getSelectDividendCalAccountList", mapShareAcctDet);
                            if (lst != null && lst.size() > 0) {
                                for (int j = 0; j < lst.size(); j++) {
                                    HashMap shareAcctMap = new HashMap();
                                    shareAcctMap = (HashMap) lst.get(j);
                                    mapShareAcctDet.put("SHARE_ACCT_NO", CommonUtil.convertObjToStr(shareAcctMap.get("SHARE_ACCT_NO")));
                                    mapShareAcctDet.put("DIVIDEND_PAY_MODE", CommonUtil.convertObjToStr(shareAcctMap.get("DIVIDEND_PAY_MODE")));
                                    mapShareAcctDet.put("DIVIDEND_CREDIT_PRODUCT", CommonUtil.convertObjToStr(shareAcctMap.get("DIVIDEND_CREDIT_PRODUCT")));
                                    mapShareAcctDet.put("DIVIDEND_CREDIT_PRODUCT_ID", CommonUtil.convertObjToStr(shareAcctMap.get("DIVIDEND_CREDIT_PRODUCT_ID")));
                                    mapShareAcctDet.put("DIVIDEND_CREDIT_AC", CommonUtil.convertObjToStr(shareAcctMap.get("DIVIDEND_CREDIT_AC")));
                                    mapShareAcctDet.put("BRANCH_CODE", CommonUtil.convertObjToStr(shareAcctMap.get("BRANCH_CODE")));
                                    mapShareAcctDet.put("BRANCH_ID", CommonUtil.convertObjToStr(shareAcctMap.get("BRANCH_CODE")));
                                    shareAcctMap = null;
                                    HashMap CalAmtMap = new HashMap();
                                    CalAmtMap = calculationForDiv(mapShareAcctDet);
                                    dividend = CommonUtil.convertObjToDouble(CalAmtMap.get("divAmt")).doubleValue();
                                    mapShareAcctDet.put("NO_OF_SHARES", CommonUtil.convertObjToStr(CalAmtMap.get("totNoShare")));
                                    System.out.println("shareAcctMap---------------------->" + shareAcctMap);
                                    System.out.println("dividend---------------------->" + dividend);
                                    if (dividend > 0.0) {
                                        try {

                                            sqlMap.startTransaction();
                                            sqlMap.commitTransaction();
                                            //--- End of transaction

                                            System.out.println("Branch_code-------------->" + _branchCode);
                                            Double dblDividend = new Double(dividend);
                                            transferAmount(mapShareAcctDet, dividend);
                                        } catch (Exception e) {
                                            sqlMap.rollbackTransaction();
                                            e.printStackTrace();
                                            //            log.error(e);
                                            System.out.println("e : " + e);
                                        }
                                    }

                                }

                            }

                            updateShareConfig(nextCalDt, lastCalDt, shareType);
                        }
                    } else {
                        System.out.println("Please Set Last Calculated date");
                    }
                }
            } else {
                System.out.println("Dividend Run Date  or Dividend Rate  Is Not Set Share Config Details  " + shareType);
            }
        }
        mapShareAcctDet = null;
    }

    private double calcDividend(HashMap mapShareAcctDet) throws Exception {

        long diff = 0;
        int count = 0;
        double divPer = 0;
        int noOfShares = 0;
        double divAmt = 0;
        double actDivper = 0;
        List listShareAcctDetCount;
        calculationForDiv(mapShareAcctDet);
        listShareAcctDetCount = (List) sqlMap.executeQueryForList("Dividend.getCountShareAcctDet", null);
        HashMap mapShareAcctDetCount = (HashMap) listShareAcctDetCount.get(0);

        //--- Retrive the data from hashmap
        count = CommonUtil.convertObjToInt(mapShareAcctDetCount.get("COUNT"));
        noOfShares = CommonUtil.convertObjToInt(mapShareAcctDet.get("NO_OF_SHARES"));
        divPer = CommonUtil.convertObjToDouble(mapShareAcctDet.get("PERCENTAGE_DIVIDEND")).doubleValue();
        Date shareIssDt = (Date) mapShareAcctDet.get("SHARE_CERT_ISSUE_DT");
        Date nextDueDt = (Date) mapShareAcctDet.get("NEXT_DUE_DATE");
        Date lastCalcDt = (Date) mapShareAcctDet.get("LAST_DIVIDEND_CALC");

        //--- If it is for the firt time then check the dividend percentage with issue date
        //--- else with the lat calulated date
        if (count == 0) {
            diff = com.see.truetransact.commonutil.DateUtil.dateDiff(shareIssDt, nextDueDt);
        } else if (count > 0) {
            diff = com.see.truetransact.commonutil.DateUtil.dateDiff(lastCalcDt, nextDueDt);
        }
        actDivper = (divPer * diff) / daysinYear;
        divAmt = noOfShares * actDivper;

        listShareAcctDetCount = null;
        mapShareAcctDetCount = null;

        return divAmt;


    }

    private HashMap calculationForDiv(HashMap mapShareAcctDet) {
        Date lstDivCalDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(mapShareAcctDet.get("LAST_DIVIDEND_CALC")));
        List lstShareTrans = ServerUtil.executeQuery("getSelectDividendCalAcctDetBF1Q", mapShareAcctDet);
        System.out.println("lstShareTrans-------------->" + lstShareTrans);
        double totNoShare = 0.0;

        Date nextDueDt = (Date) mapShareAcctDet.get("NEXT_DUE_DATE");
        System.out.println("mapShareAcctDet FIRST----------->" + mapShareAcctDet);
        double divAmt = 0.0;
        double divPer = CommonUtil.convertObjToDouble(mapShareAcctDet.get("PERCENTAGE_DIVIDEND")).doubleValue();
        double faceValue = CommonUtil.convertObjToDouble(mapShareAcctDet.get("FACE_VALUE")).doubleValue();
        if (lstShareTrans != null && lstShareTrans.size() > 0) {
            HashMap fisMap = new HashMap();
            fisMap = (HashMap) lstShareTrans.get(0);
            double noOfShares = CommonUtil.convertObjToDouble(fisMap.get("NO_OF_SHARES")).doubleValue();
            System.out.println("noOfShares-------------->" + noOfShares);
            fisMap = null;

            divAmt = (noOfShares * divPer * faceValue) / 100;
            totNoShare = noOfShares;
        }
        System.out.println("divAmt in before loop total " + divAmt);
        int calFreq = CommonUtil.convertObjToInt(mapShareAcctDet.get("DIVIDEND_CALC_FREQUENCY"));
        int quarte = 360 / calFreq;
        for (Date stDt = lstDivCalDt; DateUtil.dateDiff(stDt, nextDueDt) > 0; stDt = DateUtil.addDays(stDt, calFreq)) {

            quarte = quarte - 1;
            System.out.println("quarte---------------------->" + quarte);
            Date StDate = DateUtil.addDays(stDt, 1);
            Date endDate = DateUtil.addDays(stDt, calFreq);
            mapShareAcctDet.put("NEXT_DATE", StDate);
            mapShareAcctDet.put("END_DATE", endDate);
            List lstQuarter = ServerUtil.executeQuery("getSelectDividendCalAcctDetQu", mapShareAcctDet);
            System.out.println("mapShareAcctDet----------->" + mapShareAcctDet);
            if (lstQuarter != null && lstQuarter.size() > 0) {
                HashMap qMap = new HashMap();
                qMap = (HashMap) lstQuarter.get(0);
                System.out.println("qMap------------->" + qMap);
                System.out.println("mapShareAcctDet----------->" + mapShareAcctDet);
                int add = CommonUtil.convertObjToInt(qMap.get("ADD"));
                System.out.println("add------------>" + add);
                if (add > 0) {
                    double qDiv = 0.0;
                    totNoShare = totNoShare + add;
                    System.out.println("withDraw-------------->" + add);
                    System.out.println("witq-------------->" + quarte);
                    System.out.println("divPer-------------->" + divPer);
                    System.out.println("divPer-------------->" + faceValue);
                    qDiv = (add * quarte * divPer * faceValue) / 400;
                    if (qDiv > 0.0) {
                        divAmt = divAmt + qDiv;
                    }
                    System.out.println("divAmt in quarter total in ADD mode " + qDiv);

                }
                int withDraw = CommonUtil.convertObjToInt(qMap.get("WITHDRAWAL"));
                System.out.println("withDraw------------>" + withDraw);
                if (withDraw > 0) {
                    double qDiv = 0.0;
                    int witq = 0;
                    witq = quarte + 1;
                    System.out.println("withDraw-------------->" + withDraw);
                    System.out.println("witq-------------->" + witq);
                    System.out.println("divPer-------------->" + divPer);
                    System.out.println("divPer-------------->" + faceValue);
                    totNoShare = totNoShare - withDraw;
                    qDiv = (withDraw * witq * divPer * faceValue) / 400;
                    divAmt = divAmt - qDiv;
                    System.out.println("divAmt in quarter total in WITHDRAWAL mode " + qDiv);
                }
            }
            System.out.println("quarte---------------------->" + quarte);
            System.out.println("divAmt in loop total " + divAmt);
        }
        System.out.println("divAmt total  " + divAmt);
        divAmt = (double) getNearest((long) (divAmt * 100), 100) / 100;
        System.out.println("divAmt before Return " + divAmt);
        HashMap divMap = new HashMap();
        if (divAmt >= 1) {

            divAmt = (double) getNearest((long) (divAmt * 100), 100) / 100;
            System.out.println("divAmt before Return " + divAmt);

            divMap.put("divAmt", new Double(divAmt));
            divMap.put("totNoShare", new Double(totNoShare));
            return divMap;

        } else {
            divMap.put("divAmt", new Double(0));
            divMap.put("totNoShare", new Double(0));

            return divMap;
        }
    }

    public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
        if ((mod <= (roundingFactor / 2)) || (mod <= (roundingFactorOdd / 2))) {
            return lower(number, roundingFactor);
        } else {
            return higher(number, roundingFactor);
        }
    }

    public long lower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    public long higher(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        if (mod == 0) {
            return number;
        }
        return (number - mod) + roundingFactor;
    }

    private void insertDividend(HashMap mapShareAcctDet, Double dividend) throws Exception {
        System.out.println("mapShareAcctDet---------->" + mapShareAcctDet);
        dividendBatchTO = new DividendBatchTO();
        dividendBatchTO.setShareType(mapShareAcctDet.get("SHARE_TYPE").toString());
        dividendBatchTO.setShareAcctNo((String) mapShareAcctDet.get("SHARE_ACCT_NO"));
        dividendBatchTO.setDividendDt(ServerUtil.getCurrentDate(_branchCode));
        dividendBatchTO.setShareCount(CommonUtil.convertObjToDouble(mapShareAcctDet.get("NO_OF_SHARES")));
        dividendBatchTO.setDividendPer(CommonUtil.convertObjToDouble(mapShareAcctDet.get("PERCENTAGE_DIVIDEND")));
        dividendBatchTO.setDividendAmt(dividend);
        dividendBatchTO.setShareAcctDetailNo((String) mapShareAcctDet.get("SHARE_ACCT_DET_NO"));
        dividendBatchTO.setDividendUpTo(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(mapShareAcctDet.get("NEXT_DUE_DATE"))));
        dividendBatchTO.setDivPayFlag((String) mapShareAcctDet.get("PAYFLAG"));
        dividendBatchTO.setStatus("CREATED");
        dividendBatchTO.setStatusBy("TTSYSTEM");
        dividendBatchTO.setStatusDt(ServerUtil.getCurrentDate(_branchCode));
        dividendBatchTO.setAuthorizeBy("TTSYSTEM");
        dividendBatchTO.setAuthorizeStatus("AUTHORIZED");
        dividendBatchTO.setAuthorizeDt(ServerUtil.getCurrentDate(_branchCode));
        dividendBatchTO.setInitBran(_branchCode);
        sqlMap.executeUpdate("insertDividendBatchTO", dividendBatchTO);
        dividendBatchTO = null;
    }

    private void updateShareConfig(Date nextDt, Date lastDt, String sharetype) throws Exception {
        shareProductTO = new ShareProductTO();
        Date nextDueDt;
        nextDueDt = DateUtil.addDays(lastDt, 360);
        shareProductTO.setShareType(sharetype);
        shareProductTO.setLastDividendCalc(nextDt);
        shareProductTO.setNextDueDate(nextDueDt);
        sqlMap.executeUpdate("Dividend.updateShareProductNextDueDate", shareProductTO);

        shareProductTO = null;

    }

    private void transferAmount(HashMap mapShareAcctDet, double dividend) throws Exception {
        System.out.println("mapShareAcctDet:" + mapShareAcctDet);
        transferTrans = new TransferTrans();
        HashMap map = new HashMap();
        map.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
        map.put(TransferTrans.DR_BRANCH, (String) mapShareAcctDet.get("BRANCH_CODE"));
        map.put(TransferTrans.DR_AC_HD, (String) mapShareAcctDet.get("DIVIDEND_PAYMENT_ACHD"));
        //--- sets the credit details
        map.put(TransferTrans.CURRENCY, "INR");
        map.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
        map.put(TransferTrans.CR_BRANCH, (String) mapShareAcctDet.get("BRANCH_CODE"));
        map.put(TransferTrans.CR_AC_HD, (String) mapShareAcctDet.get("DIVIDEND_PAYABLE_ACHD"));

        TransferTrans trans = new TransferTrans();
        ArrayList batchList = new ArrayList();
        trans.setInitiatedBranch((String) mapShareAcctDet.get("BRANCH_CODE"));
        batchList.add(trans.getDebitTransferTO(map, dividend));
        batchList.add(trans.getCreditTransferTO(map, dividend));
        //        setBatchList(batchList); //returns the list so that it may be logged if failure
        trans.doDebitCredit(batchList, (String) mapShareAcctDet.get("BRANCH_CODE"));

        mapShareAcctDet.put("SHARE_ACCT_DET_NO", "CREDIT");
        if (mapShareAcctDet.containsKey("UNCLAIMED")) {
            mapShareAcctDet.put("SHARE_ACCT_DET_NO", "DEBIT");
        }
        //        payFlag="";
        mapShareAcctDet.put("PAYFLAG", payFlag);
        insertDividend(mapShareAcctDet, new Double(dividend));
        mapShareAcctDet.put("DIVIDEND_AMOUNT", new Double(dividend));
        mapShareAcctDet.put("UPTO_DIVIDEND_APPLDT", mapShareAcctDet.get("NEXT_DUE_DATE"));
        if (!mapShareAcctDet.containsKey("UNCLAIMED")) {
            sqlMap.executeUpdate("upDateDividendDateAndAmount", mapShareAcctDet);
        } else if (mapShareAcctDet.containsKey("UNCLAIMED")) {
            mapShareAcctDet.put("DIVIDEND_AMOUNT", new Double(dividend * -1));
            mapShareAcctDet.put("UPTO_DIVIDEND_PAID_DATE", mapShareAcctDet.get("UPTO_DIVIDEND_PAID_DATE"));
            sqlMap.executeUpdate("upDateDividendDateAndAmountpaid", mapShareAcctDet);
        }
        if (CommonUtil.convertObjToStr(mapShareAcctDet.get("DIVIDEND_PAY_MODE")).equals("TRANSFER") && !mapShareAcctDet.containsKey("UNCLAIMED")) {
            //            dointTransferToAccount(mapShareAcctDet,dividend);
            //            mapShareAcctDet.put("SHARE_ACCT_DET_NO","DEBIT");
            //            mapShareAcctDet.put("PAYFLAG",payFlag);
            //            insertDividend(mapShareAcctDet,new Double(dividend));
            //            mapShareAcctDet.put("DIVIDEND_AMOUNT",new Double(dividend*-1));
            //            mapShareAcctDet.put("UPTO_DIVIDEND_APPLDT" ,mapShareAcctDet.get("NEXT_DUE_DATE"));
            //            mapShareAcctDet.put("UPTO_DIVIDEND_PAID_DATE" ,mapShareAcctDet.get("NEXT_DUE_DATE"));
            //            sqlMap.executeUpdate("upDateDividendDateAndAmountpaid", mapShareAcctDet);
        }
        trans = null;
        transferTrans = null;
        batchList = null;
    }

    private void getPendingDividentTransferToAccount(List listShareAcctDet) throws Exception {
        if (listShareAcctDet != null && listShareAcctDet.size() > 0) {
            for (int i = 0; i < listShareAcctDet.size(); i++) {
                HashMap mapShareAcctDet = (HashMap) listShareAcctDet.get(i);
                String shareType = CommonUtil.convertObjToStr(mapShareAcctDet.get("SHARE_TYPE"));
                mapShareAcctDet.put("BRANCH_CODE", _branchCode);

                if (CommonUtil.convertObjToStr(parmMap.get("ACT_FROM")).length() > 0) {
                    mapShareAcctDet.put("SHARE_ACCT_NO_FROM", CommonUtil.convertObjToStr(parmMap.get("ACT_FROM")));
                }
                if (CommonUtil.convertObjToStr(parmMap.get("ACT_TO")).length() > 0) {
                    mapShareAcctDet.put("SHARE_ACCT_NO_TO", CommonUtil.convertObjToStr(parmMap.get("ACT_TO")));
                }
                mapShareAcctDet.put("DIVIDEND_PAY_MODE", "TRANSFER");

                //                SHARE_ACCT_NO_FROM
                List lst = ServerUtil.executeQuery("getDividentTransferList", mapShareAcctDet);
                if (lst != null && lst.size() > 0) {
                    try {

                        sqlMap.startTransaction();
                        dointTransferToAccount(lst, mapShareAcctDet);
                        sqlMap.commitTransaction();
                        //--- End of transaction
                    } catch (Exception e) {
                        sqlMap.rollbackTransaction();
                        e.printStackTrace();
                        //            log.error(e);
                        System.out.println("e : " + e);
                    }
                }

            }

        }

    }

    private void dointTransferToAccount(List lst, HashMap mapShareAcctDet) throws Exception {
        HashMap txMap = new HashMap();
        TransferTrans trans = new TransferTrans();
        ArrayList trfLst = new ArrayList();
        double totDivTrfAmt = 0.0;
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                HashMap acctDtlMap = new HashMap();
                double intTrfAmt = 0.0;
                acctDtlMap = (HashMap) lst.get(i);
                intTrfAmt = CommonUtil.convertObjToDouble(acctDtlMap.get("DIVIDEND_AMOUNT")).doubleValue();
                //                System.out.println("acctDtlMap---------------->"+acctDtlMap);
                String actBranch = CommonUtil.convertObjToStr(acctDtlMap.get("BRANCH_CODE"));
                if (!acctDtlMap.get("DIVIDEND_CREDIT_PRODUCT").equals("RM")) {
                    System.out.println("inside non Rm");


                    //                    TxTransferTO objTxTransferTO = new TxTransferTO();

                    trans.setInitiatedBranch(actBranch);
                    //                     txMap = new HashMap();
                    //                    //       txMap.put(TransferTrans.DR_PROD_ID,"GL");
                    //                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(acctDtlMap.get("DIVIDEND_PAYABLE_ACHD")));
                    //                    txMap.put(TransferTrans.DR_BRANCH, actBranch);
                    //                    txMap.put(TransferTrans.DR_PROD_TYPE,"GL");
                    //                    txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(acctDtlMap.get("SHARE_ACCT_NO"))+"DivPaid");
                    //                    txMap.put(TransferTrans.CURRENCY,"INR");
                    //                    //       txMap.put(TransferTrans.DR_BRANCH,branch);
                    //                    trans.setInitiatedBranch(actBranch);
                    //                    trfLst.add(trans.getDebitTransferTO(txMap,intTrfAmt));

                    txMap = new HashMap();
                    if (intTrfAmt > 0) {
                        if (!acctDtlMap.get("DIVIDEND_CREDIT_PRODUCT").equals("GL")) {
                            if (acctDtlMap.get("DIVIDEND_CREDIT_PRODUCT").equals("TL")) {
                                HashMap loanMap = interestCalculationTLAD(CommonUtil.convertObjToStr(acctDtlMap.get("DIVIDEND_CREDIT_AC")),
                                        CommonUtil.convertObjToStr(acctDtlMap.get("DIVIDEND_CREDIT_PRODUCT_ID")));
                                loanMap.put("LOAN_FROM_CHARGESUI", "");
                                trans.setAllAmountMap(loanMap);
                            }

                            txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(acctDtlMap.get("DIVIDEND_CREDIT_AC")));
                            txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(acctDtlMap.get("DIVIDEND_CREDIT_PRODUCT_ID")));
                            txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(acctDtlMap.get("DIVIDEND_CREDIT_PRODUCT")));
                        } else {
                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(acctDtlMap.get("DIVIDEND_CREDIT_AC")));
                            txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(acctDtlMap.get("DIVIDEND_CREDIT_PRODUCT")));
                        }
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(acctDtlMap.get("SHARE_ACCT_NO")) + "DivPaid");
                        txMap.put(TransferTrans.CR_BRANCH, actBranch);
                        trfLst.add(trans.getCreditTransferTO(txMap, intTrfAmt));
                        //                        System.out.println("trfLs--------------------------->"+trfLst);

                        payFlag = CommonUtil.convertObjToStr(acctDtlMap.get("DIVIDEND_CREDIT_AC"));
                        totDivTrfAmt = totDivTrfAmt + intTrfAmt;
                    }
                } else {
                    //                    if(intTrfAmt>0){
                    //                        //                Date todayDt = (Date) currentDate.clone();
                    //                        System.out.println("inside Rm");
                    //                        HashMap dtatMap=new HashMap();
                    //                        LinkedHashMap notDelMap=new LinkedHashMap();
                    //                        LinkedHashMap notDelRemMap=new LinkedHashMap();
                    //                        dtatMap.put("MODE","INSERT");
                    //
                    //                        TransactionTO transfer=new TransactionTO();
                    //                        transfer.setTransType("TRANSFER");
                    //                        transfer.setTransAmt(new Double(intTrfAmt));
                    //                        transfer.setProductType("GL");
                    //                        transfer.setDebitAcctNo(CommonUtil.convertObjToStr(acctDtlMap.get("DIVIDEND_PAYABLE_ACHD")));
                    //                        transfer.setApplName(CommonUtil.convertObjToStr(acctDtlMap.get("DIVIDEND_PAYABLE_ACHD")));  // Added by Rajesh
                    //                        //                transfer.setDebitAcctNo(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                    //                        transfer.setInstType("VOUCHER");
                    //                        transfer.setChequeDt(ServerUtil.getCurrentDate(_branchCode));
                    //                        LinkedHashMap transMap=new LinkedHashMap();
                    //                        LinkedHashMap remMap=new LinkedHashMap();
                    //                        transfer.setCommand("INSERT");
                    //
                    //
                    //                        notDelMap.put(String.valueOf(1),transfer);//"NOT_DELETED_TRANS_TOs"
                    //                        transMap.put("NOT_DELETED_TRANS_TOs",notDelMap);
                    //                        dtatMap.put("TransactionTO",transMap);
                    //                        dtatMap.put("BRANCH_CODE",actBranch);
                    //                        dtatMap.put("OPERATION_MODE","ISSUE");
                    //                        dtatMap.put("AUTHORIZEMAP",null);
                    //                        RemittanceIssueTO remt=new RemittanceIssueTO();
                    //                        remt.setDraweeBranchCode(actBranch);
                    //                        remt.setAmount(new Double(intTrfAmt));
                    //                        remt.setRemitForFlag("Dividend");
                    //                        remt.setCategory("GENERAL_CATEGORY");
                    //
                    //                        // The following block added by Rajesh
                    //                        HashMap behavesMap = new HashMap();
                    //                        behavesMap.put("BEHAVES_LIKE","PO");
                    //                        List lstRemit = sqlMap.executeQueryForList("selectRemitProductId",behavesMap);
                    //                        if(lstRemit!=null && lstRemit.size()>0){
                    //                            behavesMap = (HashMap)lstRemit.get(0);
                    //                            remt.setProdId(CommonUtil.convertObjToStr(behavesMap.get("PROD_ID")));
                    //                        }
                    //                        HashMap draweeMap = new HashMap();
                    //                        if(behavesMap.get("PAY_ISSUE_BRANCH").equals("ISSU_BRANCH")){
                    //                            lstRemit = sqlMap.executeQueryForList("getSelectBankTOList",null);
                    //                            if(lstRemit!=null && lstRemit.size()>0){
                    //                                draweeMap =(HashMap)lstRemit.get(0);
                    //                            }
                    //                        }
                    //
                    //                        //                remt.setProdId(CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_ID")));  // Commented by Rajesh
                    //
                    //                        remt.setFavouring(CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_ACC_NO")));
                    //                        //                remt.setDraweeBank(branch);   // Commented by Rajesh
                    //                        remt.setDraweeBank(CommonUtil.convertObjToStr(draweeMap.get("BANK_CODE")));
                    //                        remt.setBranchId(actBranch);
                    //                        remt.setCity("560");
                    //                        remt.setInstrumentNo1("PO");
                    //                        remt.setCommand("INSERT");
                    //                        remt.setTotalAmt(new Double(intTrfAmt));
                    //                        remt.setExchange(new Double(0.0));
                    //                        remt.setPostage(new Double(0.0));
                    //                        remt.setOtherCharges(new Double(0.0));
                    //                        remt.setStatusDt(ServerUtil.getCurrentDate(_branchCode));
                    //                        remt.setStatusBy("TTSYSTEM");
                    //                        notDelRemMap.put(String.valueOf(1),remt);
                    //                        remMap.put("NOT_DELETED_ISSUE_TOs",notDelRemMap);
                    //                        dtatMap.put("RemittanceIssueTO",remMap);
                    //                        System.out.println("dtatMap$$$$$$$$$$$$$$$"+dtatMap);
                    //                        RemittanceIssueDAO remDao=new RemittanceIssueDAO();
                    //                        HashMap resulMap=new HashMap();
                    //                        remDao.setFromotherDAo(false);
                    //                        resulMap=remDao.execute(dtatMap);
                    //                        System.out.println("resulMap$$$$$$$$$$$$$$$"+resulMap);
                    //                        payFlag=CommonUtil.convertObjToStr(resulMap.get("VARIABLE_NO"));
                    //                        behavesMap = null;
                    //                        draweeMap = null;
                    //                        lstRemit = null;
                    //
                    //                    }
                }
                acctDtlMap.put("PAYFLAG", "TRANSFER");
                acctDtlMap.put("SHARE_ACCT_DET_NO", "DEBIT");
                acctDtlMap.put("SHARE_TYPE", CommonUtil.convertObjToStr(mapShareAcctDet.get("SHARE_TYPE")));
                acctDtlMap.put("PERCENTAGE_DIVIDEND", CommonUtil.convertObjToStr(mapShareAcctDet.get("PERCENTAGE_DIVIDEND")));
                acctDtlMap.put("NEXT_DUE_DATE", mapShareAcctDet.get("NEXT_DUE_DATE"));


                insertDividend(acctDtlMap, new Double(intTrfAmt));
                //                System.out.println("DIVIDEND_AMOUNT-------------------------->"+intTrfAmt);
                acctDtlMap.put("DIVIDEND_AMOUNT", new Double(intTrfAmt * -1));
                acctDtlMap.put("UPTO_DIVIDEND_PAID_DATE", ServerUtil.getCurrentDate(_branchCode));
                //                System.out.println("acctDtlMap-------------------------->"+acctDtlMap);

                sqlMap.executeUpdate("upDateDividendDateAndAmountpaid", acctDtlMap);
                intTrfAmt = 0.0;
                acctDtlMap = new HashMap();
            }

            txMap = new HashMap();
            //       txMap.put(TransferTrans.DR_PROD_ID,"GL");
            txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(mapShareAcctDet.get("DIVIDEND_PAYABLE_ACHD")));
            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
            txMap.put(TransferTrans.DR_PROD_TYPE, "GL");
            txMap.put(TransferTrans.PARTICULARS, " To DiviDendTransfer" + CommonUtil.convertObjToStr(mapShareAcctDet.get("SHARE_TYPE")));
            txMap.put(TransferTrans.CURRENCY, "INR");
            //       txMap.put(TransferTrans.DR_BRANCH,branch);
            trans.setInitiatedBranch(_branchCode);
            trfLst.add(trans.getDebitTransferTO(txMap, totDivTrfAmt));
            trans.doDebitCredit(trfLst, _branchCode);


        }
    }

    private HashMap interestCalculationTLAD(String accountNo, String prodID) {
        HashMap map = new HashMap();
        HashMap insertPenal = new HashMap();
        HashMap hash = null;
        try {

            map.put("ACT_NUM", accountNo);
            map.put("PROD_ID", prodID);
            map.put("TRANS_DT", ServerUtil.getCurrentDate(_branchCode));
            map.put("INITIATED_BRANCH", _branchCode);
            List lst = sqlMap.executeQueryForList("IntCalculationDetail", map);
            if (lst == null || lst.isEmpty()) {
                lst = sqlMap.executeQueryForList("IntCalculationDetailAD", map);
            }
            if (lst != null && lst.size() > 0) {
                hash = (HashMap) lst.get(0);
                if (hash.get("AS_CUSTOMER_COMES") != null && hash.get("AS_CUSTOMER_COMES").equals("N")) {
                    hash = new HashMap();
                    return hash;
                }
                map.put("BRANCH_ID", _branchCode);
                map.put("BRANCH_CODE", _branchCode);
                map.putAll(hash);
                map.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
                map.put("CURR_DATE", ServerUtil.getCurrentDate(_branchCode));
                System.out.println("map before intereest###" + map);
                TaskHeader header = new TaskHeader();
                header.setBranchID(_branchCode);
                InterestCalculationTask interestcalTask = new InterestCalculationTask(header);
                hash = interestcalTask.interestCalcTermLoanAD(map);
                if (hash == null) {
                    hash = new HashMap();
                } else if (hash != null && hash.size() > 0) {
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
                            if (chargeMap.get("CHARGE_TYPE").equals("EXECUTION DECREE CHARGES") && chargeAmt > 0) {
                                insertPenal.put("EXECUTION DECREE CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("ARBITRARY CHARGES") && chargeAmt > 0) {
                                insertPenal.put("ARBITRARY CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            chargeMap = null;
                        }

                    }
                    chargeList = null;
                }
                interestcalTask = null;
                System.out.println("hashinterestoutput###" + insertPenal);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        map = null;
        hash = null;

        return insertPenal;
    }

    void holiydaychecking(Date lstintCr) {
        try {
            HashMap MonthEnd = new HashMap(); //traansferto holidaychecking method;
            boolean checkHoliday = true;
            MonthEnd.put("NEXT_DATE", lstintCr.clone());
            MonthEnd.put(CommonConstants.BRANCH_ID, _branchCode);
            lstintCr = lstintCr;
            while (checkHoliday) {
                boolean tholiday = false;
                System.out.println("enterytothecheckholiday" + checkHoliday);
                List Holiday = sqlMap.executeQueryForList("checkHolidayDateOD", MonthEnd);
                List weeklyOf = sqlMap.executeQueryForList("checkWeeklyOffOD", MonthEnd);
                boolean isHoliday = Holiday.size() > 0 ? true : false;
                boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
                if (isHoliday || isWeekOff) {
                    MonthEnd = dateMinus(MonthEnd);
                    checkHoliday = true;
                } else {
                    checkHoliday = doNoHoliday(CommonUtil.convertObjToStr(MonthEnd.get("NEXT_DATE")));;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HashMap dateMinus(HashMap dateMap) {
        String day = CommonUtil.convertObjToStr(dateMap.get("NEXT_DATE"));
        Date lastDay = (Date) dateMap.get("NEXT_DATE");
        int days = lastDay.getDate();
        days--;
        lastDay.setDate(days);
        dateMap.put("NEXT_DATE", lastDay);
        dateMap.put(CommonConstants.BRANCH_ID, _branchCode);
        return dateMap;
    }

    private boolean doNoHoliday(String nonHoliday) {
        checkThisCDate = DateUtil.getDateMMDDYYYY(nonHoliday);
        System.out.println("nonHoliday" + nonHoliday);
        return false;
    }
}
