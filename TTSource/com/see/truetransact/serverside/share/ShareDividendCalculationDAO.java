 /*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareDividendCalculationDAO.java
 *
 * Created on Wed May 26 10:59:57 GMT+05:30 2004
 */
package com.see.truetransact.serverside.share;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.transferobject.investments.InvestmentsTransTO;
import com.see.truetransact.serverside.investments.InvestmentsTransDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.share.ShareDividendCalculationTO;
import com.see.truetransact.transferobject.share.ShareDividendCalculationDetailsTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
//import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO ;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import java.sql.*;
import oracle.sql.*;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Date;
import java.util.Map;
import java.text.*;
import java.util.GregorianCalendar;

/**
 * ShareDividendCalculation DAO.
 *
 * @author nikhil
 *
 */
public class ShareDividendCalculationDAO extends TTDAO {

    private String branchId;
    private SqlMap sqlMap;
    private HashMap data;
    private HashMap shareProductDetails;
    private ShareDividendCalculationTO objDrfMasterTO;
    private ShareDividendCalculationTO objShareDividendCalculationTO;
    private TransactionDAO transactionDAO = null;
    private String command;
    private HashMap drfMasterMap;
    private HashMap deletedDrfMasterMap;
    private final static Logger log = Logger.getLogger(ShareDividendCalculationDAO.class);
    HashMap resultMap = new HashMap();
    Date currDt = null;
    private String whereCondition;
    private HashMap whereConditions;
    final String SCREEN = "CUS";
    private List list;
    private TransactionTO objTransactionTO;
    private Map returnMap;
    TransferDAO transferDAO = new TransferDAO();

    /**
     * Creates a new instance of DeductionDAO
     */
    public ShareDividendCalculationDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private void updateData() throws Exception {
    }

    private void deleteData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
    }

    private void makeDataNull() {
        data = null;
    }

    private void makeNull() {
        shareProductDetails = null;
        objShareDividendCalculationTO = null;
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
            ShareDividendCalculationDAO dao = new ShareDividendCalculationDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        HashMap transMap = new HashMap();
        String where = (String) obj.get("DRF_TRANS_ID");
        HashMap getRemitTransMap = new HashMap();
        getRemitTransMap.put("TRANS_ID", obj.get("DRF_TRANS_ID"));
        getRemitTransMap.put("TRANS_DT", currDt.clone());
        getRemitTransMap.put("BRANCH_CODE", _branchCode);
        System.out.println("@#%$#@%#$%getRemitTransMap:" + getRemitTransMap);
        List list = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", getRemitTransMap);
        if (list != null && list.size() > 0) {
            transMap.put("TRANSACTION_LIST", list);
        }
        return transMap;
    }

    private void makeQueryNull() {
        whereCondition = null;
        list = null;

    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("#### inside shareDAO execute() map : " + map);
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        LogDAO objLogDAO = new LogDAO();
        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        branchId = objLogTO.getBranchId();
//        System.out.println("#### branchId : "+branchId);
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));
        shareProductDetails = new HashMap();
        returnMap = new HashMap();
        if (map.containsKey("ShareDividendCalculationTO")) {
            objShareDividendCalculationTO = (ShareDividendCalculationTO) map.get("ShareDividendCalculationTO");
            final TOHeader toHeader = objShareDividendCalculationTO.getTOHeader();
            command = CommonUtil.convertObjToStr(map.get("COMMAND"));
//            System.out.println("@#$@#$#@command:"+command);
            //--- Selects the method according to the Command type
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                if (map.containsKey("LOAD_DATA")) {
                    if (map.containsKey("SHARE_PROD_DETAIL")) {
                        shareProductDetails = (HashMap) map.get("SHARE_PROD_DETAIL");
                    }
                    HashMap calculationMap = getShareAcctDetails(map);
//                    System.out.println("#@$@$#@#$calculationMap"+calculationMap);
                    returnMap = calculationMap;

                } else if (map.containsKey("SHARE_PROD_DETAIL_SAVE")) {
                    insertData(map, objLogDAO, objLogTO);
                }
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                //                updateData(map , objLogDAO, objLogTO);
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                //                deleteData(map ,objLogDAO, objLogTO);
            } else {
                throw new NoCommandException();
            }
            destroyObjects();
        }

        if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
//            System.out.println("map:" + map);
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            authMap.put(CommonConstants.BRANCH_ID, (String) map.get(CommonConstants.BRANCH_ID));
            authMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
            System.out.println("authMap:" + authMap);
            //            if (authMap != null) authorize(authMap);
        }
        return (HashMap) returnMap;
    }

    private void getdividendIDno() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "SHARE_DIVIDEND_CALC");
        String dividendID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        objShareDividendCalculationTO.setDividendID(dividendID);
    }

//    public CashTransactionTO setCashTransaction(HashMap cashMap) {
//        log.info("In setCashTransaction()");
//        Date curDate=(Date)currDt.clone();
//        final CashTransactionTO objCashTransactionTO = new CashTransactionTO();
//        try{
//            objCashTransactionTO.setAcHdId(CommonUtil.convertObjToStr(cashMap.get(CommonConstants.AC_HD_ID)));
//            objCashTransactionTO.setProdType(TransactionFactory.GL);
//            objCashTransactionTO.setInpAmount(CommonUtil.convertObjToDouble(cashMap.get("AMOUNT")));
//            objCashTransactionTO.setAmount(CommonUtil.convertObjToDouble(cashMap.get("AMOUNT")));
//            objCashTransactionTO.setTransType(CommonUtil.convertObjToStr(cashMap.get("TRANS_TYPE")));
//            objCashTransactionTO.setBranchId(_branchCode);
//            objCashTransactionTO.setStatusBy(CommonUtil.convertObjToStr(cashMap.get(CommonConstants.USER_ID)));
//            //            objCashTransactionTO.setInstrumentNo1(cashTo.getInstrumentNo1());
//            objCashTransactionTO.setInstrumentNo2("DEPOSIT_TRANS");
//            objCashTransactionTO.setInitTransId(CommonUtil.convertObjToStr(cashMap.get(CommonConstants.USER_ID)));
//            objCashTransactionTO.setInitChannType("CASHIER");
//            objCashTransactionTO.setParticulars("By "+CommonUtil.convertObjToStr(cashMap.get("LINK_BATCH_ID")));
//            objCashTransactionTO.setInitiatedBranch(_branchCode);
//            objCashTransactionTO.setLinkBatchId(CommonUtil.convertObjToStr(cashMap.get("LINK_BATCH_ID")));
//            objCashTransactionTO.setAuthorizeRemarks(CommonUtil.convertObjToStr(cashMap.get("AUTHORIZEREMARKS")));
//            objCashTransactionTO.setCommand(CommonConstants.TOSTATUS_INSERT);
//            System.out.println("objCashTransactionTO:" + objCashTransactionTO);
//        }catch(Exception e){
//            log.info("Error In setInwardClearing()");
//            e.printStackTrace();
//        }
//        return objCashTransactionTO;
//    }
//    private void getTransDetails(String batchId) throws Exception {
//        HashMap getTransMap = new HashMap();
//        getTransMap.put("BATCH_ID", batchId);
//        getTransMap.put("TRANS_DT", currDt);
//        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
//        //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
//        List transList = (List) sqlMap.executeQueryForList("getTransferDetails", getTransMap);
//        if(transList!=null && transList.size()>0){
//            returnMap.put("TRANSFER_TRANS_LIST",transList);
//        }
//        List cashList = (List) sqlMap.executeQueryForList("getCashDetails", getTransMap);
//        if(cashList!=null && cashList.size()>0){
//            returnMap.put("CASH_TRANS_LIST",cashList);
//        }
//        getTransMap.clear();
//        getTransMap = null;
//        transList = null;
//        cashList = null;
//    }
    private HashMap getShareAcctDetails(HashMap Sharemap) throws Exception {
        HashMap shareAcctDetailsMap = new HashMap();
        ArrayList shareCustDetailsList = new ArrayList();
        try {
            if (Sharemap.containsKey("ShareDividendCalculationTO")) {
                objShareDividendCalculationTO = (ShareDividendCalculationTO) Sharemap.get("ShareDividendCalculationTO");
//                System.out.println("@!#!$@#$objShareDividendCalculationTO:"+objShareDividendCalculationTO);
                Date fromPeriod = objShareDividendCalculationTO.getTdtFromPeriod();
                Date toPeriod = objShareDividendCalculationTO.getTdtToPeriod();
                String dividendPercent = objShareDividendCalculationTO.getTxtDividendPercent();
                 String closedYN ="";
                if(Sharemap!=null && Sharemap.containsKey("CLOSED_REQ")){
                 closedYN = CommonUtil.convertObjToStr(Sharemap.get("CLOSED_REQ"));
                }
                List shareMemberList = sqlMap.executeQueryForList("getShareDividendMemberDetails", objShareDividendCalculationTO);
                if (shareMemberList != null && shareMemberList.size() > 0) {
                    for (int i = 0; i < shareMemberList.size(); i++) {
                        HashMap shareMemberMap = (HashMap) shareMemberList.get(i);
//                        System.out.println("@#$@#$@#$shareMemberMap:"+shareMemberMap);
                        shareMemberMap.put("FROM_PERIOD", fromPeriod);
                        shareMemberMap.put("TO_PERIOD", toPeriod);
                        //      need to find out whether the account is closed before the period.
                        
                        double shAmt = 0;
                        if (closedYN != null && closedYN.equals("Y")) {
                            shareAcctDetailsMap = calculateAmountBeforeFromPeriod(shareMemberMap, objShareDividendCalculationTO);
                            shareAcctDetailsMap = calculateDividendAmount(shareAcctDetailsMap, objShareDividendCalculationTO);
                            if (shareCustDetailsList == null && shareCustDetailsList.size() <= 0) {
                                shareCustDetailsList = new ArrayList();
                            }
                            shareCustDetailsList.add(shareAcctDetailsMap);
                        } else {
//                        if(checkIfClosedLst == null || checkIfClosedLst.size() <=0){
                            List checkIfClosedLst = sqlMap.executeQueryForList("checkIfShareClosed", shareMemberMap);
                            if (checkIfClosedLst == null || checkIfClosedLst.size() <= 0) {
                                shareAcctDetailsMap = calculateAmountBeforeFromPeriod(shareMemberMap, objShareDividendCalculationTO);
                                shareAcctDetailsMap = calculateDividendAmount(shareAcctDetailsMap, objShareDividendCalculationTO);
                                if (shareCustDetailsList == null && shareCustDetailsList.size() <= 0) {
                                    shareCustDetailsList = new ArrayList();
                                }
                                shareCustDetailsList.add(shareAcctDetailsMap);
                            }
                        }
                        //                            adding each details of single customers into the arraylist

//                        }else{
////                            System.out.println("$@#$@$@$@#$checkIfClosedLst:"+checkIfClosedLst);
//                        }
                    }
                    shareAcctDetailsMap = new HashMap();
                    shareAcctDetailsMap.put("TOTAL_EMPLOYEE_DETAILS", shareCustDetailsList);

                }
//                System.out.println("#@$@#$@#Total details of employee shareCustDetailsList:"+shareCustDetailsList);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            System.out.println("e : " + e);
            throw e;
        }
        return shareAcctDetailsMap;
    }

    private HashMap calculateDividendAmount(HashMap individualDetailsMap, ShareDividendCalculationTO objShareDividendCalcTO) throws Exception {
        System.out.println("@#!$@#$@$shareProductDetails:" + shareProductDetails);
        System.out.println("@#!$@#$@individualDetailsMap:" + individualDetailsMap);
        if (individualDetailsMap != null && individualDetailsMap.containsKey("MEMBERSHIP_NO")
                && individualDetailsMap.get("MEMBERSHIP_NO") == null) {
            individualDetailsMap.put("MEMBERSHIP_NO", individualDetailsMap.get("SHARE_ACCT_NO"));
        }
        Date fromPeriod = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objShareDividendCalcTO.getTdtFromPeriod()));
        Date startDt = null;
        Date nextDate = null;
        Date toPeriod = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objShareDividendCalcTO.getTdtToPeriod()));
         Date toPeriod_Daily = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objShareDividendCalcTO.getTdtToPeriod()));
//        System.out.println("#@$@#$@#$toPeriod before :"+toPeriod);
        toPeriod = DateUtil.addDays(toPeriod, -1);
//        System.out.println("@#$@#$toPeriod after:"+toPeriod);
        double dividendPercent = CommonUtil.convertObjToDouble(objShareDividendCalcTO.getTxtDividendPercent()).doubleValue();
        double minimumShareDividend = CommonUtil.convertObjToDouble(shareProductDetails.get("MIN_DIVIDEND_AMOUNT")).doubleValue();
        double shareAmt = 0.0;
        if (individualDetailsMap.containsKey("BALANCE_AS_ON_START")) {
            //            contains the THE BALANCE SHARE AMOUNT AS ON THE DIVIDEND CALC START DATE
            shareAmt = CommonUtil.convertObjToDouble(individualDetailsMap.get("BALANCE_AS_ON_START")).doubleValue();
        }
        double dividendAmount = 0.0;
        if (shareProductDetails.containsKey("DIVIDEND_CALC_TYPE")) {
            String calcType = CommonUtil.convertObjToStr(shareProductDetails.get("DIVIDEND_CALC_TYPE"));
            if (calcType.equals("DAILY_BAL")) {
                List customerSlabsBtwPeriodDaily = sqlMap.executeQueryForList("getCustomerShareBetweenPeriodDaily", individualDetailsMap);
                if (customerSlabsBtwPeriodDaily != null && customerSlabsBtwPeriodDaily.size() > 0) {
                    startDt = fromPeriod;
                    Date tempDt = null;
                    for (int i = 0; i < customerSlabsBtwPeriodDaily.size(); i++) {
                        HashMap customerSlabsBtwPeriodDailyMap = (HashMap) customerSlabsBtwPeriodDaily.get(i);
//                        System.out.println("@#$@#$shareAmt:" +shareAmt+"customerSlabsBtwPeriodDailyMap :"+customerSlabsBtwPeriodDailyMap);
                        nextDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(customerSlabsBtwPeriodDailyMap.get("SHARE_CERT_ISSUE_DT")));
                        int noOfDays = (int) DateUtil.dateDiff(startDt, nextDate);
                        startDt = nextDate;
                        dividendAmount = dividendAmount + (shareAmt * dividendPercent * noOfDays) / 36500;
//                        System.out.println("@#$@#$@$#noOfDays 1:"+noOfDays+" : dividendAmount"+dividendAmount);
                        if (CommonUtil.convertObjToStr(customerSlabsBtwPeriodDailyMap.get("SHARE_NO_FROM")).equals("ADD")) {
                            //                            if share addition
//                            System.out.println("@#$@#$@#$in add");
                            shareAmt = shareAmt + CommonUtil.convertObjToDouble(customerSlabsBtwPeriodDailyMap.get("SHARE_VALUE")).doubleValue();

                            if (i == customerSlabsBtwPeriodDaily.size() - 1) {
                              //  noOfDays = (int) DateUtil.dateDiff(startDt, toPeriod);
                                 noOfDays = (int) DateUtil.dateDiff(startDt, toPeriod_Daily);
                                dividendAmount += shareAmt * dividendPercent * noOfDays / 36500;
//                                System.out.println("@#$@#$@$#noOfDays 2:"+noOfDays+" : dividendAmount"+dividendAmount);
                            }
                        } else if (CommonUtil.convertObjToStr(customerSlabsBtwPeriodDailyMap.get("SHARE_NO_FROM")).equals("WITHDRAWAL")) {
                            shareAmt = shareAmt - CommonUtil.convertObjToDouble(customerSlabsBtwPeriodDailyMap.get("SHARE_VALUE")).doubleValue();
                            if (i == customerSlabsBtwPeriodDaily.size() - 1) {
                               // noOfDays = (int) DateUtil.dateDiff(startDt, toPeriod);
                                 noOfDays = (int) DateUtil.dateDiff(startDt, toPeriod_Daily);
                                dividendAmount += shareAmt * dividendPercent * noOfDays / 36500;
//                                System.out.println("@#$@#$@$#noOfDays 3:"+noOfDays+" : dividendAmount"+dividendAmount);
                            }
//                            System.out.println("@#$@#$@#$in withdrawal");
                        }
                    }
//                    System.out.println("@#$@#$dividendAmount final:"+dividendAmount);
                } else {
                   // int noOfDays = (int) DateUtil.dateDiff(fromPeriod, toPeriod);
                     int noOfDays = (int) DateUtil.dateDiff(fromPeriod, toPeriod_Daily);
                    dividendAmount = noOfDays * shareAmt * dividendPercent / 36500;
//                    System.out.println("#$@$@#$dividendAmount:"+dividendAmount);
//                    System.out.println("@#$@#$@$#noOfDays 4:"+noOfDays+" : dividendAmount"+dividendAmount);
                }
                if (shareProductDetails.containsKey("DIVIDEND_ROUND_OFF")) {
                    if (CommonUtil.convertObjToStr(shareProductDetails.get("DIVIDEND_ROUND_OFF")).equals("NO_ROUND")) {
                        DecimalFormat df = new DecimalFormat("#.##");
                        dividendAmount = CommonUtil.convertObjToDouble(df.format(dividendAmount)).doubleValue();
//                        System.out.println("@#$@#$@#df.format(dividendAmount)"+df.format(dividendAmount));
//                        System.out.println("@#$@#$@#df.only account(dividendAmount)"+dividendAmount);
                    } else if (CommonUtil.convertObjToStr(shareProductDetails.get("DIVIDEND_ROUND_OFF")).equals("NEAREST_VAL")) {
                        dividendAmount = (double) getNearest((long) (dividendAmount * 100), 100) / 100;
                    }
                }
                if (dividendAmount < minimumShareDividend) {
                    dividendAmount = 0.0;
                }
                individualDetailsMap.put("DIVIDEND_AMOUNT", String.valueOf(dividendAmount));
            } else if (calcType.equals("MONTHLY_MIN_BAL")) {
//                System.out.println("$#%#$%#$%inside MonthlyBalance:"+shareAmt);
                double balanceAsOnStart = shareAmt;

                //                fromPeriod = startDt;
//                System.out.println("@#$@#$@#fromPeriod:"+fromPeriod+" :toPeriod :"+toPeriod);
                int dayDiff = (int) DateUtil.dateDiff(fromPeriod, toPeriod);
                int noOfMonths = dayDiff / 30;
                Date startDayMonth = fromPeriod;
                Date lastDayMonth = toPeriod;
//                System.out.println("@#$@#$@#$noOfMonths:"+noOfMonths);
                HashMap empDetMapForMonthly = new HashMap();
                empDetMapForMonthly.put("SHARE_ACCT_NO", CommonUtil.convertObjToStr(individualDetailsMap.get("SHARE_ACCT_NO")));
                for (int i = 0; i < noOfMonths; i++) {
                    GregorianCalendar firstdaymonth = new GregorianCalendar(startDayMonth.getYear(), startDayMonth.getMonth(), 1);
//                    System.out.println("@#$@#$@#$firstdaymonth :"+firstdaymonth );
                    //                    to get the no of days in the month
                    int noOfDays = firstdaymonth.getActualMaximum(firstdaymonth.DAY_OF_MONTH);
//                    System.out.println("@#$@#$@#$noOfDays:"+noOfDays);
                    Date lastDateOfMonth = (Date) currDt.clone();
                    lastDateOfMonth.setDate(noOfDays);
                    lastDateOfMonth.setMonth(startDayMonth.getMonth());
                    lastDateOfMonth.setYear(startDayMonth.getYear());
                    //        last day of the salary calculation month
                    lastDateOfMonth = setProperDtFormat(lastDateOfMonth);
//                    System.out.println("#@$@#$@#$lastDateOfMonth:"+lastDateOfMonth);
                    empDetMapForMonthly.put("FROM_PERIOD", startDayMonth);
                    empDetMapForMonthly.put("TO_PERIOD", lastDateOfMonth);
//                    System.out.println("#@$@#$@#$empDetMapForMonthly:"+empDetMapForMonthly);
                    List customerSlabBtwPeriodMonthly = sqlMap.executeQueryForList("getCustomerShareBetweenPeriodDaily", empDetMapForMonthly);
//                    System.out.println("#@$@#$@#$customerSlabBtwPeriodMonthly :"+customerSlabBtwPeriodMonthly);
                    if (customerSlabBtwPeriodMonthly != null && customerSlabBtwPeriodMonthly.size() > 0) {
//                        System.out.println("#@$@#$customerSlabBtwPeriodMonthly to calc minimum Amt:"+customerSlabBtwPeriodMonthly);
                        empDetMapForMonthly.put("MONTH_START_AMOUNT", String.valueOf(shareAmt));
                        HashMap minimumAmount = calculateMinimumAmount(calcType, customerSlabBtwPeriodMonthly, empDetMapForMonthly);
                        double mimimumAmt = CommonUtil.convertObjToDouble(minimumAmount.get("MINIMUM_AMOUNT")).doubleValue();
                        //                        dividendAmount += mimimumAmt * noOfDays * dividendPercent/36500;
                        dividendAmount += mimimumAmt * dividendPercent / 1200;
                        shareAmt = CommonUtil.convertObjToDouble(minimumAmount.get("NEXT_AMOUNT")).doubleValue();
//                        System.out.println("dividend amount wen dr is mimimum amount:"+dividendAmount);
                    } else {
                        //                        dividendAmount += shareAmt * noOfDays * dividendPercent/36500;
                        dividendAmount += shareAmt * dividendPercent / 1200;
//                        System.out.println("dividend amount wen dr is no add or with for particular month:"+dividendAmount);
                    }
                    startDayMonth.setMonth(startDayMonth.getMonth() + 1);
//                    System.out.println("#@#$@#$next month:"+startDayMonth);
                }
                if (shareProductDetails.containsKey("DIVIDEND_ROUND_OFF")) {
                    if (CommonUtil.convertObjToStr(shareProductDetails.get("DIVIDEND_ROUND_OFF")).equals("NO_ROUND")) {
                        DecimalFormat df = new DecimalFormat("#.##");
                        dividendAmount = CommonUtil.convertObjToDouble(df.format(dividendAmount)).doubleValue();
//                        System.out.println("@#$@#$@#df.format(dividendAmount)"+df.format(dividendAmount));
//                        System.out.println("@#$@#$@#df.only account(dividendAmount)"+dividendAmount);
                    } else if (CommonUtil.convertObjToStr(shareProductDetails.get("DIVIDEND_ROUND_OFF")).equals("NEAREST_VAL")) {
                        dividendAmount = (double) getNearest((long) (dividendAmount * 100), 100) / 100;
                    }
                }
                if (dividendAmount < minimumShareDividend) {
                    dividendAmount = 0.0;
                }
//                System.out.println("@#$@#$@#$final dividend amount:"+dividendAmount);
                individualDetailsMap.put("DIVIDEND_AMOUNT", String.valueOf(dividendAmount));

            } else if (calcType.equals("MONTH_END_BAL")) {
//                System.out.println("#@$@month end balance#$@#$#@$individualDetailsMap:"+individualDetailsMap);
//                System.out.println("$#%#$%#$%inside MonthlyBalance:"+shareAmt);
                double balanceAsOnStart = shareAmt;
//                System.out.println("@#$@#$@#fromPeriod:"+fromPeriod+" :toPeriod :"+toPeriod);
                int dayDiff = (int) DateUtil.dateDiff(fromPeriod, toPeriod);
                int noOfMonths = dayDiff / 30;
                Date startDayMonth = fromPeriod;
                Date lastDayMonth = toPeriod;
                dividendAmount = 0.0;
//                System.out.println("@#$@#$@#$asdnoOfMonths:"+noOfMonths);
                HashMap empDetMapForMonthly = new HashMap();
                empDetMapForMonthly.put("SHARE_ACCT_NO", CommonUtil.convertObjToStr(individualDetailsMap.get("SHARE_ACCT_NO")));
                empDetMapForMonthly.put("SHARE_TYPE", CommonUtil.convertObjToStr(individualDetailsMap.get("SHARE_TYPE")));
                for (int i = 0; i < noOfMonths; i++) {
                    GregorianCalendar firstdaymonth = new GregorianCalendar(startDayMonth.getYear(), startDayMonth.getMonth(), 1);
//                    System.out.println("@#$@#$@#$firstdaasdymonth :"+firstdaymonth );
                    //                    to get the no of days in the month
                    int noOfDays = firstdaymonth.getActualMaximum(firstdaymonth.DAY_OF_MONTH);
//                    System.out.println("@#$@#$@#$noOfDaysasd:"+noOfDays);
                    Date lastDateOfMonth = (Date) currDt.clone();
                    lastDateOfMonth.setDate(noOfDays);
                    lastDateOfMonth.setMonth(startDayMonth.getMonth());
                    lastDateOfMonth.setYear(startDayMonth.getYear());
                    //        last day of the salary calculation month
                    lastDateOfMonth = setProperDtFormat(lastDateOfMonth);
                    empDetMapForMonthly.put("FROM_PERIOD", lastDateOfMonth);
//                    System.out.println("@#$@#$@#$empDetMapForMonthly:"+empDetMapForMonthly);
                    List monthEndBalLst = sqlMap.executeQueryForList("getShareAmtAsonFrmPeriod", empDetMapForMonthly);
//                    System.out.println("#@$@#$@#$customerSlabBtwPeriodMonthend :"+monthEndBalLst);
                    if (monthEndBalLst != null && monthEndBalLst.size() > 0) {
                        HashMap monthEndBalMap = (HashMap) monthEndBalLst.get(0);
                        double monthEndAMt = CommonUtil.convertObjToDouble(monthEndBalMap.get("BALANCE_AS_ON_START")).doubleValue();
//                        System.out.println("#$%#$%#$%monthEndAMt :"+monthEndAMt);
                        dividendAmount += monthEndAMt * dividendPercent / 1200;
                    } else {
                        //                        dividendAmount += shareAmt * noOfDays * dividendPercent/36500;
                        //                        System.out.println("dividend amount wen dr is no add or with for particular month:"+dividendAmount);
                    }
                    startDayMonth.setMonth(startDayMonth.getMonth() + 1);
//                    System.out.println("#@#$@#$next month:"+startDayMonth);
                }
                if (shareProductDetails.containsKey("DIVIDEND_ROUND_OFF")) {
                    if (CommonUtil.convertObjToStr(shareProductDetails.get("DIVIDEND_ROUND_OFF")).equals("NO_ROUND")) {
                        DecimalFormat df = new DecimalFormat("#.##");
                        dividendAmount = CommonUtil.convertObjToDouble(df.format(dividendAmount)).doubleValue();
//                        System.out.println("@#$@#$@#df.format(dividendAmount)"+df.format(dividendAmount));
//                        System.out.println("@#$@#$@#df.only account(dividendAmount)"+dividendAmount);
                    } else if (CommonUtil.convertObjToStr(shareProductDetails.get("DIVIDEND_ROUND_OFF")).equals("NEAREST_VAL")) {
                        dividendAmount = (double) getNearest((long) (dividendAmount * 100), 100) / 100;
                    }
                }
                if (dividendAmount < minimumShareDividend) {
                    dividendAmount = 0.0;
                }
//                System.out.println("@#$@#$@#$final dividend amount:"+dividendAmount);
                individualDetailsMap.put("DIVIDEND_AMOUNT", String.valueOf(dividendAmount));
            } else if (calcType.equals("YR_END_BAL")) {
//                System.out.println("#@$@year end balance#$@#$#@$individualDetailsMap:"+individualDetailsMap);
//                System.out.println("$#%#$%#$%inside year end Balance:"+shareAmt);
                double balanceAsOnStart = shareAmt;
//                System.out.println("@#$@#$@#fromPeriod:"+fromPeriod+" :toPeriod :"+toPeriod);
                int dayDiff = (int) DateUtil.dateDiff(fromPeriod, toPeriod);
//                System.out.println("#$%#$%#$% dayDiff year end:"+dayDiff);
//                int noOfYears = dayDiff/365;
                int noOfYears = 1;
                Date startDayMonth = fromPeriod;
                Date lastDayMonth = toPeriod;
                dividendAmount = 0.0;
//                System.out.println("@#$@#$@#$asdnoOfYears:"+noOfYears);
                HashMap empDetMapForMonthly = new HashMap();
                empDetMapForMonthly.put("SHARE_ACCT_NO", CommonUtil.convertObjToStr(individualDetailsMap.get("SHARE_ACCT_NO")));
                empDetMapForMonthly.put("SHARE_TYPE", CommonUtil.convertObjToStr(individualDetailsMap.get("SHARE_TYPE")));
                for (int i = 0; i < noOfYears; i++) {
                    Date lastDateOfMonth = (Date) currDt.clone();
                    lastDateOfMonth = startDayMonth;
//                    System.out.println("@#!$@#after copying startdate:"+lastDateOfMonth);
                    lastDateOfMonth.setYear(startDayMonth.getYear() + 1);
//                    System.out.println("#$%#$%#$%after adding a year lastDateOfMonth:"+lastDateOfMonth);
                    lastDateOfMonth = DateUtil.addDays(lastDateOfMonth, -1);
                    //        last day of the salary calculation month
                    lastDateOfMonth = setProperDtFormat(lastDateOfMonth);
//                    System.out.println("@$#@#$@#after subtracting a day:"+lastDateOfMonth);
                    empDetMapForMonthly.put("FROM_PERIOD", lastDateOfMonth);
//                    System.out.println("@#$@#$@#$empDetMapForMonthly:"+empDetMapForMonthly);
                    List monthEndBalLst = sqlMap.executeQueryForList("getShareAmtAsonFrmPeriod", empDetMapForMonthly);
//                    System.out.println("#@$@#$@#$customerSlabBtwPeriodend :"+monthEndBalLst);
                    if (monthEndBalLst != null && monthEndBalLst.size() > 0) {
                        HashMap monthEndBalMap = (HashMap) monthEndBalLst.get(0);
                        double monthEndAMt = CommonUtil.convertObjToDouble(monthEndBalMap.get("BALANCE_AS_ON_START")).doubleValue();
//                        System.out.println("#$%#$%#$%yearEndAMt :"+monthEndAMt);
                        dividendAmount += monthEndAMt * dividendPercent / 100;
                    } else {
                        //                        dividendAmount += shareAmt * noOfDays * dividendPercent/36500;
                        //                        System.out.println("dividend amount wen dr is no add or with for particular month:"+dividendAmount);
                    }
                    startDayMonth = lastDateOfMonth;
                    startDayMonth = DateUtil.addDays(startDayMonth, 1);
//                    System.out.println("#@#$@#$next year:"+startDayMonth);
                }
                if (shareProductDetails.containsKey("DIVIDEND_ROUND_OFF")) {
                    if (CommonUtil.convertObjToStr(shareProductDetails.get("DIVIDEND_ROUND_OFF")).equals("NO_ROUND")) {
                        DecimalFormat df = new DecimalFormat("#.##");
                        dividendAmount = CommonUtil.convertObjToDouble(df.format(dividendAmount)).doubleValue();
//                        System.out.println("@#$@#$@#df.format(dividendAmount)"+df.format(dividendAmount));
//                        System.out.println("@#$@#$@#df.only account(dividendAmount)"+dividendAmount);
                    } else if (CommonUtil.convertObjToStr(shareProductDetails.get("DIVIDEND_ROUND_OFF")).equals("NEAREST_VAL")) {
                        dividendAmount = (double) getNearest((long) (dividendAmount * 100), 100) / 100;
                    }
                }
                if (dividendAmount < minimumShareDividend) {
                    dividendAmount = 0.0;
                }
//                System.out.println("@#$@#$@#$final dividend amount:"+dividendAmount);
                individualDetailsMap.put("DIVIDEND_AMOUNT", String.valueOf(dividendAmount));


            } else if (calcType.equals("YR_OPEN_BAL")) {
                double balanceAsOnStart = shareAmt;
                int dayDiff = (int) DateUtil.dateDiff(fromPeriod, toPeriod);
                int noOfYears = 1;
                Date startDayMonth = fromPeriod;
                Date lastDayMonth = toPeriod;
                dividendAmount = 0.0;
                double balBetweenPeriod = 0.0;
                HashMap empDetMapForMonthly = new HashMap();
                empDetMapForMonthly.put("SHARE_ACCT_NO", CommonUtil.convertObjToStr(individualDetailsMap.get("SHARE_ACCT_NO")));
                empDetMapForMonthly.put("SHARE_TYPE", CommonUtil.convertObjToStr(individualDetailsMap.get("SHARE_TYPE")));
                for (int i = 0; i < noOfYears; i++) {
                    Date lastDateOfMonth = (Date) currDt.clone();
                    lastDateOfMonth = startDayMonth;
//                    lastDateOfMonth.setYear(startDayMonth.getYear()+1);
                    lastDateOfMonth = DateUtil.addDays(fromPeriod, -1);
                    lastDateOfMonth = setProperDtFormat(lastDateOfMonth);
                    empDetMapForMonthly.put("FROM_PERIOD", lastDateOfMonth);

                    List monthEndBalLst = sqlMap.executeQueryForList("getShareAmtAsonFrmPeriod", empDetMapForMonthly);
                    if (monthEndBalLst != null && monthEndBalLst.size() > 0) {
                        HashMap monthEndBalMap = (HashMap) monthEndBalLst.get(0);
                        double monthEndAMt = CommonUtil.convertObjToDouble(monthEndBalMap.get("BALANCE_AS_ON_START")).doubleValue();
                        dividendAmount += monthEndAMt * dividendPercent / 100;
                    }
                    empDetMapForMonthly.put("FROM_PERIOD", toPeriod);
                    List balBetweenPeriodLst = sqlMap.executeQueryForList("getShareAmtAsonFrmPeriod", empDetMapForMonthly);
                    if (balBetweenPeriodLst != null && balBetweenPeriodLst.size() > 0) {
                        HashMap balBetweenPeriodMap = (HashMap) balBetweenPeriodLst.get(0);
                        balBetweenPeriod = CommonUtil.convertObjToDouble(balBetweenPeriodMap.get("BALANCE_AS_ON_START")).doubleValue();

                    }
                    startDayMonth = lastDateOfMonth;
                    startDayMonth = DateUtil.addDays(startDayMonth, 1);
                }
                if (shareProductDetails.containsKey("DIVIDEND_ROUND_OFF")) {
                    if (CommonUtil.convertObjToStr(shareProductDetails.get("DIVIDEND_ROUND_OFF")).equals("NO_ROUND")) {
                        DecimalFormat df = new DecimalFormat("#.##");
                        dividendAmount = CommonUtil.convertObjToDouble(df.format(dividendAmount)).doubleValue();

                    } else if (CommonUtil.convertObjToStr(shareProductDetails.get("DIVIDEND_ROUND_OFF")).equals("NEAREST_VAL")) {
                        dividendAmount = (double) getNearest((long) (dividendAmount * 100), 100) / 100;
                    }
                }
                if (dividendAmount < minimumShareDividend || balBetweenPeriod <= 0.0) {
                    dividendAmount = 0.0;
                }
                individualDetailsMap.put("DIVIDEND_AMOUNT", String.valueOf(dividendAmount));


            }
        }
        return individualDetailsMap;
    }

    public HashMap calculateMinimumAmount(String calcType, List monthlyShareList, HashMap empDetMapForMonthly) {
        HashMap monthlyShareMap = new HashMap();
        Date fromDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(empDetMapForMonthly.get("FROM_PERIOD")));
        Date toDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(empDetMapForMonthly.get("TO_PERIOD")));
        double shareDividendAmt = CommonUtil.convertObjToDouble(empDetMapForMonthly.get("MONTH_START_AMOUNT")).doubleValue();
        double mimimumShareAmount = 0.0;
        Date startMonthCalcDate = fromDt;
        startMonthCalcDate.setDate(10);
//        System.out.println("!@$#@!#$startMonthCalcDate:"+startMonthCalcDate);
        //find the total amounyt as on 10th of the month
        List calculateDividendList = new ArrayList();
        for (int i = 0; i < monthlyShareList.size(); i++) {
            monthlyShareMap = (HashMap) monthlyShareList.get(i);
//            System.out.println("@#$@#$@#$@#$monthlyShareMap: "+i+" :"+monthlyShareMap);
            double noOfShares = CommonUtil.convertObjToDouble(monthlyShareMap.get("SHARE_VALUE")).doubleValue();
            String typeOTrans = CommonUtil.convertObjToStr(monthlyShareMap.get("SHARE_NO_FROM"));

            Date dateOfShareTrans = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(monthlyShareMap.get("SHARE_CERT_ISSUE_DT")));
            if (dateOfShareTrans.equals(startMonthCalcDate) || dateOfShareTrans.before(startMonthCalcDate)) {
                if (typeOTrans.equals("ADD")) {
                    shareDividendAmt += noOfShares;
                } else {
                    shareDividendAmt -= noOfShares;
                }
            } else {
                calculateDividendList.add(monthlyShareList.get(i));
//                System.out.println("#$%#$%#$%calculateDividendList:"+calculateDividendList);
            }
//            System.out.println("shareDividendAmt!@$@#$@#$:"+shareDividendAmt);
        }
//        System.out.println("shareDividendAmt on 10th of month!@$@#$@#$:"+shareDividendAmt);
        if (calculateDividendList != null && calculateDividendList.size() > 0) {
            double[] sortList = new double[calculateDividendList.size() + 1];
            sortList[0] = shareDividendAmt;
            //        calculate the monthly minimum AMount
            for (int i = 0; i < calculateDividendList.size(); i++) {
                HashMap monthlyMinMap = (HashMap) calculateDividendList.get(i);
//                System.out.println("@#$@#$monthlyMinMap:"+monthlyMinMap);
                double noOfShares = CommonUtil.convertObjToDouble(monthlyMinMap.get("SHARE_VALUE")).doubleValue();
                String typeOTrans = CommonUtil.convertObjToStr(monthlyShareMap.get("SHARE_NO_FROM"));
                if (typeOTrans.equals("ADD")) {
                    shareDividendAmt += noOfShares;
                } else {
                    shareDividendAmt -= noOfShares;
                }
                //            sortList[i] = CommonUtil.convertObjToDouble(String.valueOf(shareDividendAmt)).doubleValue();
                sortList[i + 1] = shareDividendAmt;
            }

//            System.out.println("##$%#$%#$%total amount listl:"+sortList[0]);
            java.util.Arrays.sort(sortList);
//            System.out.println("@#$@#$@#$minimum Amount for :"+fromDt+" is: "+sortList[0]);
            monthlyShareMap.put("MINIMUM_AMOUNT", String.valueOf(sortList[0]));
            monthlyShareMap.put("NEXT_AMOUNT", String.valueOf(shareDividendAmt));
        } else {
            monthlyShareMap.put("MINIMUM_AMOUNT", String.valueOf(shareDividendAmt));
            monthlyShareMap.put("NEXT_AMOUNT", String.valueOf(shareDividendAmt));
        }
        return monthlyShareMap;
    }

    public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
        if ((mod < (roundingFactor / 2)) || (mod < (roundingFactorOdd / 2))) {
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

    private HashMap calculateAmountBeforeFromPeriod(HashMap individualDetailsMap, ShareDividendCalculationTO objShareDividendCalcTO) throws Exception {
        System.out.println("#$@#$@#$@individualDetailsMap:" + individualDetailsMap);
        System.out.println("#$@#$@#$@objShareDividendCalcTO:" + objShareDividendCalcTO);

        Date shareCreatedDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(individualDetailsMap.get("CREATED_DT")));
        Date fromPeriod = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(individualDetailsMap.get("FROM_PERIOD")));
        Date toPeriod = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(individualDetailsMap.get("TO_PERIOD")));
//        System.out.println("#@$@#$@shareCreatedDt:"+shareCreatedDt + " :fromPeriod "+fromPeriod + " :toPeriod "+toPeriod);
        individualDetailsMap.put("CREATED_DT", shareCreatedDt);
        individualDetailsMap.put("FROM_PERIOD", fromPeriod);
        individualDetailsMap.put("TO_PERIOD", toPeriod);
        individualDetailsMap.put("SHARE_TYPE", objShareDividendCalcTO.getCboShareClass());
        if (fromPeriod.after(shareCreatedDt)) {
            //            have to find the balance on that particular from Period
//            System.out.println("$#%#$%#$%individualDetailsMap before execution:"+individualDetailsMap);
            //            gets the details of the balance before the FromPeriod.
            List shareAmtAsOnFrmPeriod = sqlMap.executeQueryForList("getShareAmtAsonFrmPeriod", individualDetailsMap);
//            changed by nikhil
            if (shareAmtAsOnFrmPeriod != null && shareAmtAsOnFrmPeriod.size() > 0) {
                HashMap shareAmtAsOnFrmPeriodMap = (HashMap) shareAmtAsOnFrmPeriod.get(0);
                individualDetailsMap.put("BALANCE_AS_ON_START", shareAmtAsOnFrmPeriodMap.get("BALANCE_AS_ON_START"));
            }
//            System.out.println("@#$shareAmtAsOnFrmPeriodMap:"+individualDetailsMap);
        }
        return individualDetailsMap;
    }

    private void insertData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {

            sqlMap.startTransaction();
            objShareDividendCalculationTO.setStatus(CommonConstants.STATUS_CREATED);
            objLogTO.setData(objShareDividendCalculationTO.toString());
            objLogTO.setPrimaryKey(objShareDividendCalculationTO.getKeyData());
            objLogDAO.addToLog(objLogTO);
//            System.out.println("!@#!@#@#$inside Insert:"+map);
            executeTransactionPart(objLogDAO, objLogTO, map);
            final String USERID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            //            log.error(e);
            System.out.println("e : " + e);
            throw e;
        }
    }

    private void executeTransactionPart(LogDAO objLogDAO, LogTO objLogTO, HashMap map) throws Exception {
        try {
//            System.out.println("#@$@#$map:"+map);
            ArrayList standingLst = new ArrayList();
            if (map.containsKey("SHARE_PROD_DETAIL_SAVE")) {
                HashMap shareDividendMap = (HashMap) map.get("SHARE_PROD_DETAIL_SAVE");
                standingLst = (ArrayList) shareDividendMap.get("TOTAL_EMPLOYEE_DETAILS");
//                System.out.println("@#$%#$%#$%standingLst:"+standingLst);
            }
            if (standingLst != null && standingLst.size() > 0) {
                getdividendIDno();
                boolean SingleTransaction = true;
//                System.out.println("@#$@#$@#$@#$objShareDividendCalculationTO:"+objShareDividendCalculationTO.getDividendID());
                String dividendID = CommonUtil.convertObjToStr(objShareDividendCalculationTO.getDividendID());
                String debitGL = CommonUtil.convertObjToStr(objShareDividendCalculationTO.getTxtDebitGl());
                String payableGL = CommonUtil.convertObjToStr(objShareDividendCalculationTO.getTxtPayableGl());
                String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
                double totalAmount = CommonUtil.convertObjToDouble(objShareDividendCalculationTO.getTxtTotalAmount()).doubleValue();
//                System.out.println("@##$#$% BRANCH_ID   #### :"+BRANCH_ID);
                objShareDividendCalculationTO.setBranchCode(BRANCH_ID);
                sqlMap.executeUpdate("insertShareDividendCalcMaster", objShareDividendCalculationTO);
                boolean firstTransID = true;
                for (int i = 0; i < standingLst.size(); i++) {
                    HashMap dataMap = new HashMap();
                    transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                    transactionDAO.setInitiatedBranch(_branchCode);
                    transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                    ArrayList transferList = new ArrayList();
                    TransferTrans transferTrans = new TransferTrans();
                    TxTransferTO transferTo = new TxTransferTO();
                    ArrayList TxTransferTO = new ArrayList();
                    TransactionTO transactionTO = new TransactionTO();
                    HashMap txMap = new HashMap();
                    HashMap transactionListMap = new HashMap();
                    HashMap transMap = new HashMap();
                    double dividendAmount = 0.0;
                    String prodType = "";
                    String prodId = "";
                    String creditAccNo = "";
                    String payMode = "";
                    dataMap = (HashMap) standingLst.get(i);
                    dividendAmount = CommonUtil.convertObjToDouble(dataMap.get("DIVIDEND_AMOUNT")).doubleValue();
                    payMode = CommonUtil.convertObjToStr(dataMap.get("DIVIDEND_PAY_MODE"));
                    prodType = CommonUtil.convertObjToStr(dataMap.get("DIVIDEND_CREDIT_PRODUCT"));
                    prodId = CommonUtil.convertObjToStr(dataMap.get("DIVIDEND_CREDIT_PRODUCT_ID"));
                    creditAccNo = CommonUtil.convertObjToStr(dataMap.get("DIVIDEND_CREDIT_AC"));
                    if (totalAmount > 0.0 && i == 0) {
                        //                            credit total Amount to payable GL
                        System.out.println("Dividend credit Started");
                        transferTo = new TxTransferTO();
                        txMap = new HashMap();
                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                                transferTo.setInstrumentNo1("DIVIDEND_PAID");
                        txMap.put("DR_INSTRUMENT_1", "DIVIDEND_PAID");
                        txMap.put(TransferTrans.CR_AC_HD, payableGL);
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                        txMap.put(TransactionDAOConstants.CREDIT, TransactionDAOConstants.CREDIT);
                        txMap.put("LINK_BATCH_ID", dividendID);
                        txMap.put(TransferTrans.PARTICULARS, payMode + "-" + debitGL);

                        System.out.println("txMap1 : " + txMap + "totalAmount :" + totalAmount);
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, totalAmount);
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setTransDt(currDt);
                        transferTo.setInitiatedBranch(BRANCH_ID);
                        transferTo.setStatusBy(CommonConstants.TTSYSTEM);
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(dividendID));
                        TxTransferTO.add(transferTo);
                        //                            debit totalAmount to DebitGL
                        System.out.println("Dividend debit Started");
                        transferTo = new TxTransferTO();
                        txMap = new HashMap();
                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                                transferTo.setInstrumentNo1("DIVIDEND_PAID");
                        txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                        txMap.put("DR_INSTRUMENT_1", "DIVIDEND_PAID");
                        txMap.put(TransferTrans.DR_AC_HD, debitGL);
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                        txMap.put(TransactionDAOConstants.DEBIT, TransactionDAOConstants.DEBIT);
                        txMap.put("LINK_BATCH_ID", dividendID);
                        txMap.put(TransferTrans.PARTICULARS, payMode + "-" + payableGL);
                        System.out.println("txMap2 : " + txMap + "totalAmount :" + totalAmount);
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, totalAmount);
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setTransDt(currDt);
                        transferTo.setInitiatedBranch(BRANCH_ID);
                        transferTo.setStatusBy(CommonConstants.TTSYSTEM);
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(dividendID));
                        TxTransferTO.add(transferTo);
                        System.out.println("TxTransferTO #@$@#$: " + TxTransferTO);
                        HashMap applnMap = new HashMap();
                        transferDAO = new TransferDAO();
                        HashMap authorizeMap = new HashMap();
                        authorizeMap.put("BATCH_ID", null);
                        authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                        map.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
                        map.put("MODE", map.get("COMMAND"));
                        map.put("COMMAND", map.get("MODE"));
                        map.put("TxTransferTO", TxTransferTO);
                        try {
                            transMap = transferDAO.execute(map, false);
                        } catch (Exception e) {
                            System.out.println("#$#$ Error :" + e);
                            returnMap.put(dividendID, e);
                            continue;
                        }
                        HashMap linkBatchMap = new HashMap();
                        authorizeTransaction(transMap, map);
                    }
                    String crdAcc_status ="";
                    if (creditAccNo != null && creditAccNo.length() > 0 && payMode.equals("TRANSFER")) {
                        HashMap where = new HashMap();
                        where.put(CommonConstants.ACT_NUM, creditAccNo);
                        where = (HashMap) sqlMap.executeQueryForObject("getStatusForAccount", where);
                        if (where != null && where.size() > 0 && where.containsKey("ACT_STATUS_ID")) {
                            crdAcc_status = CommonUtil.convertObjToStr(where.get("ACT_STATUS_ID"));
                        }
                    }
                    if (payMode.equals("TRANSFER") && crdAcc_status!=null && !crdAcc_status.equalsIgnoreCase("CLOSED")) {
                        System.out.println("#@$@#$@#$@#$payMode:" + payMode);
                        System.out.println("#@$@#$@#$@#prodType:" + prodType);
                        System.out.println("#@$@#$@#$@#prodId:" + prodId);
                        System.out.println("#@$@#$@#$@#creditAccNo:" + creditAccNo);
                        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                        transactionDAO.setInitiatedBranch(_branchCode);
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        transferList = new ArrayList();
                        transferTrans = new TransferTrans();
                        transferTo = new TxTransferTO();
                        TxTransferTO = new ArrayList();
                        transactionTO = new TransactionTO();
                        txMap = new HashMap();
                        transactionListMap = new HashMap();
                        transMap = new HashMap();
                        System.out.println("@%#$%#%$$totalAmount:" + totalAmount);
                        if (dividendAmount > 0.0) {
                            // Credit dividend Amount to SI Account
                            System.out.println("Dividend credit Started");
                            //Added By Suresh
                            if (prodType.equals("INV")) {
                                System.out.println("####Investment Transaction");
                                HashMap invDataMap = new HashMap();
                                LinkedHashMap invTransMap = new LinkedHashMap();
                                LinkedHashMap notDelMap = new LinkedHashMap();
                                invDataMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                invDataMap.put(CommonConstants.BRANCH_ID, BRANCH_ID);
                                invDataMap.put("InvestmentsTransTO", getInvestmentsTransTO(CommonConstants.TOSTATUS_INSERT, dividendAmount, dataMap, map));
                                TransactionTO transfer = new TransactionTO();
                                transfer.setTransType("TRANSFER");
                                transfer.setTransAmt(new Double(dividendAmount));
                                transfer.setProductType("GL");
                                transfer.setDebitAcctNo(payableGL);
                                transfer.setApplName(payableGL);
                                transfer.setInstType("VOUCHER");
                                transfer.setCommand("INSERT");
                                notDelMap.put(String.valueOf(1), transfer);//"NOT_DELETED_TRANS_TOs"
                                invTransMap.put("NOT_DELETED_TRANS_TOs", notDelMap);
                                invDataMap.put("TransactionTO", invTransMap);
                                invDataMap.put("FROM_INTEREST_TASK", "FROM_INTEREST_TASK");
                                InvestmentsTransDAO investmentDAO = new InvestmentsTransDAO();
                                HashMap transDetMap = investmentDAO.execute(invDataMap);
                                System.out.println("###### After Insert transDetMap INV : " + transDetMap);
                                //Authorization
                                if (transDetMap != null && transDetMap.size() > 0 && transDetMap.containsKey("BATCH_ID")) {
                                    HashMap whereMap = new HashMap();
                                    invDataMap = new HashMap();
                                    String batchID = "";
                                    whereMap.put("BATCH_ID", transDetMap.get("BATCH_ID"));
                                    dataMap.put("BATCH_ID", transDetMap.get("BATCH_ID"));
                                    invDataMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    invDataMap.put(CommonConstants.BRANCH_ID, BRANCH_ID);
                                    invDataMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                                    transfer = new TransactionTO();
                                    transfer.setTransType("TRANSFER");
                                    transfer.setTransAmt(new Double(dividendAmount));
                                    transfer.setProductType("GL");
                                    transfer.setDebitAcctNo(payableGL);
                                    transfer.setApplName(payableGL);
                                    transfer.setInstType("VOUCHER");
                                    transfer.setBatchId(CommonUtil.convertObjToStr(transDetMap.get("BATCH_ID")));
                                    transfer.setTransId(CommonUtil.convertObjToStr(transDetMap.get("BATCH_ID")));
                                    batchID = CommonUtil.convertObjToStr(transDetMap.get("BATCH_ID"));
                                    transfer.setBatchDt(currDt);
                                    notDelMap.put(String.valueOf(1), transfer);//"NOT_DELETED_TRANS_TOs"
                                    invTransMap.put("NOT_DELETED_TRANS_TOs", notDelMap);
                                    invDataMap.put("TransactionTO", invTransMap);
                                    invDataMap.put("FROM_INTEREST_TASK", "FROM_INTEREST_TASK");
                                    ArrayList arrList = new ArrayList();
                                    HashMap authDataMap = new HashMap();
                                    HashMap singleAuthorizeMap = new HashMap();
                                    authDataMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    authDataMap.put("BATCH_ID", transDetMap.get("BATCH_ID"));
                                    arrList.add(authDataMap);
                                    singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                                    singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                                    singleAuthorizeMap.put("InvestmentsTransTO", getInvestmentsTransTO(CommonConstants.STATUS_AUTHORIZED, dividendAmount, dataMap, map));
                                    singleAuthorizeMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    invDataMap.put(CommonConstants.AUTHORIZEMAP, singleAuthorizeMap);
                                    investmentDAO = new InvestmentsTransDAO();
                                    transDetMap = investmentDAO.execute(invDataMap);
                                    try {
                                        if (firstTransID) {
                                            returnMap.put("FIRST_TRANS_ID", batchID);
                                            firstTransID = false;
                                        }
                                        returnMap.put("LAST_TRANS_ID", batchID);
                                    } catch (Exception e) {
                                        System.out.println("#$#$ Error :" + e);
                                        returnMap.put(dividendID, e);
                                        continue;
                                    }
                                }
                            } else {
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_ACT_NUM, creditAccNo);
                                txMap.put(TransferTrans.CR_PROD_TYPE, prodType);
                                txMap.put(TransferTrans.CR_PROD_ID, prodId);
                                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                txMap.put(TransactionDAOConstants.CREDIT, TransactionDAOConstants.CREDIT);
                                txMap.put("LINK_BATCH_ID", dividendID);
//                            txMap.put(TransferTrans.PARTICULARS,payMode+"-" + creditAccNo);
                                txMap.put(TransferTrans.PARTICULARS, payMode + "-" + objShareDividendCalculationTO.getDividendID() + "-DIVIDEND");
                                System.out.println("txMap3 : " + txMap + "dividendAmount :" + dividendAmount);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, dividendAmount);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setTransDt(currDt);
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonConstants.TTSYSTEM);
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(dividendID));
                                TxTransferTO.add(transferTo);

                                //                            debit Payable GL
                                System.out.println("Dividend debit Started");
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.DR_AC_HD, payableGL);
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransactionDAOConstants.DEBIT, TransactionDAOConstants.DEBIT);
                                txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                                txMap.put("LINK_BATCH_ID", dividendID);
                                txMap.put(TransferTrans.PARTICULARS, payMode + "-" + creditAccNo);
                                System.out.println("txMap4 : " + txMap + "dividendAmount :" + dividendAmount);
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, dividendAmount);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setTransDt(currDt);
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonConstants.TTSYSTEM);
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(dividendID));
                                TxTransferTO.add(transferTo);

                                System.out.println("TxTransferTO #@$@#$: " + TxTransferTO);
                                HashMap applnMap = new HashMap();
                                transferDAO = new TransferDAO();
                                HashMap authorizeMap = new HashMap();
                                authorizeMap.put("BATCH_ID", null);
                                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                                map.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
                                map.put("MODE", map.get("COMMAND"));
                                map.put("COMMAND", map.get("MODE"));
                                map.put("TxTransferTO", TxTransferTO);
                                try {
                                    transMap = transferDAO.execute(map, false);
                                    if (firstTransID) {
                                        returnMap.put("FIRST_TRANS_ID", transMap.get("TRANS_ID"));
                                        firstTransID = false;
                                    }
                                    returnMap.put("LAST_TRANS_ID", transMap.get("TRANS_ID"));
                                } catch (Exception e) {
                                    System.out.println("#$#$ Error :" + e);
                                    returnMap.put(dividendID, e);
                                    continue;
                                }
                                HashMap linkBatchMap = new HashMap();
                                authorizeTransaction(transMap, map);
                            }
                        }
                    }
                    SingleTransaction = false;
                }
                System.out.println("@!#$@#$@#$transaction over");
                System.out.println("!@#$@#$@#dividendID" + dividendID);
                for (int i = 0; i < standingLst.size(); i++) {
                    HashMap dataMap = new HashMap();
                    ShareDividendCalculationDetailsTO objShareDividendCalculationDetailsTO = new ShareDividendCalculationDetailsTO();
                    dataMap = (HashMap) standingLst.get(i);
                    objShareDividendCalculationDetailsTO.setDividendAmt(CommonUtil.convertObjToStr(dataMap.get("DIVIDEND_AMOUNT")));
                    objShareDividendCalculationDetailsTO.setDividendCalcID(dividendID);
                    String crdAcc_status = "";
                    String creditAccNo = CommonUtil.convertObjToStr(dataMap.get("DIVIDEND_CREDIT_AC"));
                    if (creditAccNo != null && creditAccNo.length() > 0 && CommonUtil.convertObjToStr(dataMap.get("DIVIDEND_PAY_MODE")).equals("TRANSFER")) {
                        HashMap where = new HashMap();
                        where.put(CommonConstants.ACT_NUM, creditAccNo);
                        where = (HashMap) sqlMap.executeQueryForObject("getStatusForAccount", where);
                        if (where != null && where.size() > 0 && where.containsKey("ACT_STATUS_ID")) {
                            crdAcc_status = CommonUtil.convertObjToStr(where.get("ACT_STATUS_ID"));
                        }
                    }
                    if (CommonUtil.convertObjToStr(dataMap.get("DIVIDEND_PAY_MODE")).equals("TRANSFER") && crdAcc_status!=null && !crdAcc_status.equalsIgnoreCase("CLOSED")) {
                        objShareDividendCalculationDetailsTO.setDividendPaidStatus("PAID");
                        objShareDividendCalculationDetailsTO.setAuthStatus("AUTHORIZED");
                    } else {
                        objShareDividendCalculationDetailsTO.setDividendPaidStatus("UNPAID");
                    }
                    objShareDividendCalculationDetailsTO.setMemberNO(CommonUtil.convertObjToStr(dataMap.get("MEMBERSHIP_NO")));
                    objShareDividendCalculationDetailsTO.setName(CommonUtil.convertObjToStr(dataMap.get("NAME")));
                    objShareDividendCalculationDetailsTO.setProdType(CommonUtil.convertObjToStr(dataMap.get("DIVIDEND_CREDIT_PRODUCT")));
                    objShareDividendCalculationDetailsTO.setProdID(CommonUtil.convertObjToStr(dataMap.get("DIVIDEND_CREDIT_PRODUCT_ID")));
                    objShareDividendCalculationDetailsTO.setSiAcctNo(CommonUtil.convertObjToStr(dataMap.get("DIVIDEND_CREDIT_AC")));
//                    System.out.println("!#@$@#$@#$objShareDividendCalculationDetailsTO"+objShareDividendCalculationDetailsTO);
                    sqlMap.executeUpdate("insertShareDivCalcMasterTO", objShareDividendCalculationDetailsTO);
                }

            }

        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    //Added By Suresh
    public InvestmentsTransTO getInvestmentsTransTO(String cmd, double intTrfAm, HashMap acctDtlMap, HashMap map) {
        HashMap whereMap = new HashMap();
        InvestmentsTransTO objgetInvestmentsTransTO = new InvestmentsTransTO();
        acctDtlMap.put("INT_PAY_ACC_NO", CommonUtil.convertObjToStr(acctDtlMap.get("DIVIDEND_CREDIT_AC")));
        List invList = ServerUtil.executeQuery("getInvestmentDetails", acctDtlMap);
        if (invList != null && invList.size() > 0) {
            whereMap = (HashMap) invList.get(0);
            objgetInvestmentsTransTO.setCommand(cmd);
            objgetInvestmentsTransTO.setStatus(CommonConstants.STATUS_CREATED);
            objgetInvestmentsTransTO.setInvestmentBehaves(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_TYPE")));
            objgetInvestmentsTransTO.setInvestmentID(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_PROD_ID")));
            objgetInvestmentsTransTO.setInvestment_internal_Id(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_ID")));
            objgetInvestmentsTransTO.setInvestment_Ref_No(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_REF_NO")));
            objgetInvestmentsTransTO.setInvestmentName(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_PROD_DESC")));
            objgetInvestmentsTransTO.setTransDT(currDt);
            objgetInvestmentsTransTO.setTransType("DEBIT");
            objgetInvestmentsTransTO.setTrnCode("Withdrawal");
            objgetInvestmentsTransTO.setAmount(new Double(0.0));
            objgetInvestmentsTransTO.setPurchaseDt(currDt);
            objgetInvestmentsTransTO.setInvestmentAmount(new Double(intTrfAm));
            objgetInvestmentsTransTO.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            objgetInvestmentsTransTO.setStatusDt(currDt);
            objgetInvestmentsTransTO.setDividendAmount(new Double(0));
            objgetInvestmentsTransTO.setLastIntPaidDate(currDt);
            objgetInvestmentsTransTO.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
            if (acctDtlMap.containsKey("BATCH_ID")) {
                objgetInvestmentsTransTO.setBatchID(CommonUtil.convertObjToStr(acctDtlMap.get("BATCH_ID")));
            }
        }
//        System.out.println("### objgetInvestmentsTransTO" +objgetInvestmentsTransTO);
        return objgetInvestmentsTransTO;
    }

    public void authorizeTransaction(HashMap transMap, HashMap map) throws Exception {
        try {
            if (transMap != null && transMap.get("TRANS_ID") != null && !transMap.get("TRANS_ID").equals("")) {
                System.out.println("TRANSFER TRANS_ID :" + transMap.get("TRANS_ID"));
                String authorizeStatus = CommonUtil.convertObjToStr(CommonConstants.STATUS_AUTHORIZED);
                String linkBatchId = CommonUtil.convertObjToStr(transMap.get("TRANS_ID"));
                HashMap cashAuthMap = new HashMap();
                cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
                cashAuthMap = null;
                transMap = null;
            }
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
        objShareDividendCalculationTO = null;
        drfMasterMap = null;
        deletedDrfMasterMap = null;
    }
}
