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
package com.see.truetransact.serverside.deposit.interestprocessing;

//import COM.rsa.jsafe.dp;
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
import com.see.truetransact.serverside.common.nominee.NomineeDAO;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
//import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO ;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.transferobject.batchprocess.interest.InterestBatchTO;
import com.see.truetransact.transferobject.deposit.closing.DepositInterestTO;
import com.see.truetransact.transferobject.deposit.interestprocessing.ThriftBenCalculationTO;
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
import java.util.*;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.print.attribute.HashPrintJobAttributeSet;

/**
 * ShareDividendCalculation DAO.
 *
 * @author nikhil
 *
 */
public class InterestProcessingDAO extends TTDAO {

    private String branchId;
    private SqlMap sqlMap;
    private HashMap data;
    private HashMap shareProductDetails;
    private List depositInterestDetailsList;
    private TransactionDAO transactionDAO = null;
    private String command;
    private final static Logger log = Logger.getLogger(InterestProcessingDAO.class);
    HashMap resultMap = new HashMap();
    Date currDt = null;
    final String SCREEN = "CUS";
    private HashMap returnMap;
    TransferDAO transferDAO = new TransferDAO();

    /**
     * Creates a new instance of DeductionDAO
     */
    public InterestProcessingDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
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
            InterestProcessingDAO dao = new InterestProcessingDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return getData(obj);
    }

    private List mergeDataList(List depositList, List depositDayEndCheckList, Date fromDate) {
        List accDepositList = new ArrayList();
        List accDepositDayEndCheckList = new ArrayList();
        if (depositList != null && depositList.size() > 0) {
            int size = depositList.size();
            for (int i = 0; i < size; i++) {
                if (!accDepositList.contains(CommonUtil.convertObjToStr(((HashMap) depositList.get(i)).get("ACT_NUM")))) {
                    accDepositList.add(CommonUtil.convertObjToStr(((HashMap) depositList.get(i)).get("ACT_NUM")));
                }
            }
        }
        if (depositDayEndCheckList != null && depositDayEndCheckList.size() > 0) {
            int size = depositDayEndCheckList.size();
            for (int i = 0; i < size; i++) {
                if (!accDepositDayEndCheckList.contains(CommonUtil.convertObjToStr(((HashMap) depositDayEndCheckList.get(i)).get("ACT_NUM")))) {
                    accDepositDayEndCheckList.add(CommonUtil.convertObjToStr(((HashMap) depositDayEndCheckList.get(i)).get("ACT_NUM")));
                }
            }
        }
        System.out.println("accDepositList" + accDepositList + "accDepositDayEndCheckList" + accDepositDayEndCheckList);
        for (int i = 0; i < accDepositDayEndCheckList.size(); i++) {
            String accNo = CommonUtil.convertObjToStr(accDepositDayEndCheckList.get(i));
            System.out.println("accNo" + accNo);
            if (!accDepositList.contains(accNo)) {
                System.out.println("not now!!!!!!");
                for (int j = 0; j < depositDayEndCheckList.size(); j++) {
                    HashMap newDataMap = (HashMap) depositDayEndCheckList.get(j);
                    String acctNo = CommonUtil.convertObjToStr(newDataMap.get("ACT_NUM"));
                    if (accNo.equals(acctNo)) {
                        HashMap detailsMap = new HashMap();
                        detailsMap.put("ACT_NUM", acctNo);
                        detailsMap.put("AMT", CommonUtil.convertObjToStr(newDataMap.get("AMT")));
                        detailsMap.put("DAY_END_DT", fromDate);
                        detailsMap.put("PROD_ID", CommonUtil.convertObjToStr(newDataMap.get("PROD_ID")));
                        depositList.add(detailsMap);
                    }
                }
            }
        }
        return depositList;
    }

    private List splitDatelist(Date fromDt, Date toDt, int monthsDiff) {
        List detailedList = new ArrayList();
        java.util.GregorianCalendar fromDate = new java.util.GregorianCalendar();
        fromDate.setGregorianChange(fromDt);
        fromDate.setTime(fromDt);
        java.util.GregorianCalendar toDate = new java.util.GregorianCalendar();
        toDate.setGregorianChange(toDt);
        toDate.setTime(toDt);
        int month = fromDate.get(fromDate.MONTH);
        int year = fromDate.get(fromDate.YEAR);
        for (int i = 0; i < monthsDiff; i++) {
            java.util.GregorianCalendar endDate = new java.util.GregorianCalendar();
            endDate.set(year, month, fromDate.getActualMaximum(fromDate.DATE));
            Date newStartDt = (Date) fromDate.getTime();
            Date newEndDt = (Date) endDate.getTime();
            HashMap detailedMap = new HashMap();
            detailedMap.put("START_DATE", newStartDt);
            detailedMap.put("END_DATE", newEndDt);
            detailedList.add(detailedMap);
            if (month < 11) {
                month += 1;
                fromDate.set(year, month, fromDate.get(fromDate.DAY_OF_MONTH));
            } else {
                month = 0;
                year += 1;
                fromDate.set(year, month, fromDate.get(fromDate.DAY_OF_MONTH));
            }
        }
        return detailedList;
    }

    public static boolean dateDiffBetween(Date date, Date dateStart, Date dateEnd) {
        if (date != null && dateStart != null && dateEnd != null) {
            if (date.after(dateStart) && date.before(dateEnd)) {
                return true;
            } else if (date.equals(dateStart)) {
                return true;
            } else if (date.equals(dateEnd)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private List splitDepositDetails(List depositDetails, String accNo) {
        List depositListDetails = new ArrayList();
        if (depositDetails != null && depositDetails.size() > 0) {
            for (int i = 0; i < depositDetails.size(); i++) {
                HashMap depositDetailsMap = (HashMap) depositDetails.get(i);
                if (depositDetailsMap != null && depositDetailsMap.size() > 0 && depositDetailsMap.containsKey("ACT_NUM") && CommonUtil.convertObjToStr(depositDetailsMap.get("ACT_NUM")).equals(accNo)) {
                    depositListDetails.add(depositDetailsMap);
                }
            }
        }
        return depositListDetails;
    }

    private List detailedDayendDate(List depositDetails, String accNo) {
        List depositDayeEndDateListDetails = new ArrayList();
        if (depositDetails != null && depositDetails.size() > 0) {
            for (int i = 0; i < depositDetails.size(); i++) {
                HashMap depositDetailsMap = (HashMap) depositDetails.get(i);
                if (depositDetailsMap != null && depositDetailsMap.size() > 0 && depositDetailsMap.containsKey("ACT_NUM") && CommonUtil.convertObjToStr(depositDetailsMap.get("ACT_NUM")).equals(accNo)) {
                    HashMap depositDayeEndDateListDetailsMap = new HashMap();
                    depositDayeEndDateListDetailsMap.put("DAY_END_DT", CommonUtil.convertObjToStr(depositDetailsMap.get("DAY_END_DT")));
                    depositDayeEndDateListDetailsMap.put("AMT", CommonUtil.convertObjToStr(depositDetailsMap.get("AMT")));
                    depositDayeEndDateListDetails.add(depositDayeEndDateListDetailsMap);
                }
            }
        }
        return depositDayeEndDateListDetails;
    }

    private List missedDateList(List detailsList, List dayEndDateList, List detailedList, int diffMonths, String accNo, Date dayEndChkDate) {
        List missedList = new ArrayList();
        for (int l = 0; l < dayEndDateList.size(); l++) {
            HashMap dayEndDateListMap = (HashMap) dayEndDateList.get(l);
            Date newDayEndDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dayEndDateListMap.get("DAY_END_DT")));
            for (int j = 0; j < detailsList.size(); j++) {
                HashMap ssMap = new HashMap();
                HashMap splitNewMap = (HashMap) detailsList.get(j);
                Date fromDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(splitNewMap.get("START_DATE")));
                Date toDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(splitNewMap.get("END_DATE")));
                boolean status = dateDiffBetween(newDayEndDate, fromDt, toDt);
                if (status) {
                    ssMap.put("START_DATE", fromDt);
                    ssMap.put("END_DATE", toDt);
                    ssMap.put("BETWEEN_DATE", newDayEndDate);
                    missedList.add(ssMap);
                }
            }
        }
        if (missedList.size() < diffMonths) {
            for (int i = 0; i < diffMonths; i++) {
                HashMap dateMap = (HashMap) detailsList.get(i);
                Date fromDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dateMap.get("START_DATE")));
                Date toDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dateMap.get("END_DATE")));
                boolean entry = false;
                for (int j = 0; j < missedList.size(); j++) {
                    HashMap missedMap = (HashMap) missedList.get(j);
                    Date inBetDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(missedMap.get("BETWEEN_DATE")));
                    boolean status = dateDiffBetween(inBetDt, fromDt, toDt);
                    if (status) {
                        entry = true;
                    }
                }
                if (!entry) {
                    HashMap detailsDepositMap = (HashMap) detailedList.get(0);
                    java.util.GregorianCalendar dayFrmDate = new java.util.GregorianCalendar();
                    dayFrmDate.setGregorianChange(fromDt);
                    dayFrmDate.setTime(fromDt);
                    java.util.GregorianCalendar finalDate = new java.util.GregorianCalendar();
                    finalDate.set(dayFrmDate.get(dayFrmDate.YEAR), dayFrmDate.get(dayFrmDate.MONTH), dayFrmDate.getActualMaximum(dayFrmDate.DATE) - 1);
                    Date finalDateNew = (Date) finalDate.getTime();
                    int finalMonth = finalDate.get(finalDate.MONTH);
                    int finalYear = finalDate.get(finalDate.YEAR);
                    int finalChkMonth = finalMonth - 1;
                    double amount = 0.0;
                    for (int m = 0; m < dayEndDateList.size(); m++) {
                        HashMap newdayEndDateListMap = (HashMap) dayEndDateList.get(m);
                        java.util.GregorianCalendar chkDate = new java.util.GregorianCalendar();
                        chkDate.setGregorianChange(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(newdayEndDateListMap.get("DAY_END_DT"))));
                        chkDate.setTime(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(newdayEndDateListMap.get("DAY_END_DT"))));
                        Date chkDt = (Date) chkDate.getTime();
                        int chkMonth = chkDate.get(chkDate.MONTH);
                        int chkYear = chkDate.get(chkDate.YEAR);
                        if (finalChkMonth < 0) {
                            finalChkMonth = 11;
                        }
                        if (chkMonth <= finalChkMonth) {
                            amount = CommonUtil.convertObjToDouble(newdayEndDateListMap.get("AMT"));
                        } else if (chkMonth > finalChkMonth && finalYear > chkYear) {
                            amount = CommonUtil.convertObjToDouble(newdayEndDateListMap.get("AMT"));
                        }
                    }
                    if (amount <= 0) {
                        HashMap whereMap = new HashMap();
                        whereMap.put("ACC_NO", accNo);
                        whereMap.put("FROM_DT", dayEndChkDate);
                        try {
                            List depositBalanceList = sqlMap.executeQueryForList("getThriftBenevolentDepositDayEndBalance", whereMap);
                            if (depositBalanceList != null && depositBalanceList.size() > 0) {
                                for (int p = 0; p < depositBalanceList.size(); p++) {
                                    HashMap newDayEndDetailsMap = (HashMap) depositBalanceList.get(p);
                                    if (newDayEndDetailsMap != null && newDayEndDetailsMap.size() > 0 && newDayEndDetailsMap.containsKey("OPENING")) {
                                        amount = CommonUtil.convertObjToDouble(newDayEndDetailsMap.get("OPENING"));
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    HashMap newDetailsDepositMap = new HashMap();
                    newDetailsDepositMap.put("ACT_NUM", CommonUtil.convertObjToStr(detailsDepositMap.get("ACT_NUM")));
                    newDetailsDepositMap.put("AMT", amount);
                    newDetailsDepositMap.put("DAY_END_DT", finalDateNew);
                    newDetailsDepositMap.put("PROD_ID", CommonUtil.convertObjToStr(detailsDepositMap.get("PROD_ID")));
                    detailedList.add(newDetailsDepositMap);
                }
            }
        }
        System.out.println("latest detailedList" + detailedList);
        return detailedList;
    }

    private int getCount(String accNo, List depositList) {
        int count = 0;
        int size = depositList.size();
        for (int i = 0; i < size; i++) {
            if (accNo.equals(CommonUtil.convertObjToStr(((HashMap) depositList.get(i)).get("ACT_NUM")))) {
                count += 1;
            }
        }
        return count;
    }


    private HashMap calcInterest(List depositList, String percent,double reservePercent, HashMap newMap, String behavesLike) {
        Date fromDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(newMap.get("FROM_DATE")));
        Date toDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(newMap.get("TO_DATE")));
        System.out.println("reservePercent---Jo---"+reservePercent);
        int difInDays = (int) ((toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
        int diffMonths = Math.round(difInDays / 30);
        List splitList = splitDatelist(fromDate, toDate, diffMonths);
        int splitSize = splitList.size();
        HashMap map = new HashMap();
        HashMap dataMap = new HashMap();
        if (depositList != null && depositList.size() > 0) {
            int size = depositList.size();
            List actNoList = new ArrayList();
            for (int i = 0; i < size; i++) {
                if (!actNoList.contains(CommonUtil.convertObjToStr(((HashMap) depositList.get(i)).get("ACT_NUM")))) {
                    actNoList.add(CommonUtil.convertObjToStr(((HashMap) depositList.get(i)).get("ACT_NUM")));
                }
            }
            if (behavesLike.equals("THRIFT")) {
                System.out.println("inside thrift");
                for (int k = 0; k < actNoList.size(); k++) {
                    double interestAmount = 0.0;
                    String actNum = CommonUtil.convertObjToStr(actNoList.get(k));
                    List depositDetailedList = splitDepositDetails(depositList, actNum);
                    List detailedDayendDateList = detailedDayendDate(depositList, actNum);
                    List missedOutList = missedDateList(splitList, detailedDayendDateList, depositDetailedList, diffMonths, actNum, fromDate);
                    int countDeposit = getCount(actNum, depositList);
                    int missedOutListSize = missedOutList.size();
                    for (int i = 0; i < missedOutListSize; i++) {
                        dataMap = (HashMap) missedOutList.get(i);
                        if (countDeposit > 0 && countDeposit == splitSize) {
                            double amt = CommonUtil.convertObjToDouble(dataMap.get("AMT"));
                            if (amt > 0) {
                                double interestAmt = (amt * CommonUtil.convertObjToDouble(percent)) / 1200;
                                if (interestAmt > 0) {
                                    interestAmount += interestAmt;
                                }
                            }
                        } else {
                            double amt = CommonUtil.convertObjToDouble(dataMap.get("AMT"));
                            if (amt > 0) {
                                double interestAmt = (amt * CommonUtil.convertObjToDouble(percent)) / 1200;
                                if (interestAmt > 0) {
                                    interestAmount += interestAmt;
                                }
                            }
                        }
                    }
                    map.put(actNum, Math.round(interestAmount));
                    map.put("actNoList", actNoList);
                }
            }
            if (behavesLike.equals("BENEVOLENT")) {
                System.out.println("inside benevolent");
                for (int k = 0; k < actNoList.size(); k++) {
                    double interestAmount = 0.0;
                    double totalReserveAmount = 0.0;
                    String actNum = CommonUtil.convertObjToStr(actNoList.get(k));
                    List depositDetailedList = splitDepositDetails(depositList, actNum);
                    List detailedDayendDateList = detailedDayendDate(depositList, actNum);
                    List missedOutList = missedDateList(splitList, detailedDayendDateList, depositDetailedList, diffMonths, actNum, fromDate);
                    int countDeposit = getCount(actNum, depositList);
                    int missedOutListSize = missedOutList.size();
                    for (int i = 0; i < missedOutListSize; i++) {
                        dataMap = (HashMap) missedOutList.get(i);
                        if (countDeposit > 0 && countDeposit == splitSize) {
                            double amt = CommonUtil.convertObjToDouble(dataMap.get("AMT"));
                            if (amt > 0) {
                                double calcPercent = CommonUtil.convertObjToDouble(percent)-reservePercent;
                                System.out.println("calPercent--Jeff---"+calcPercent+"reservePercent---Jeff---"+reservePercent);
                                double interestAmt = (amt * calcPercent) / 1200;
                                double reserveAmt = (amt * CommonUtil.convertObjToDouble(reservePercent))/1200;
                                System.out.println("interestAmt"+interestAmt+"reserveAmt"+reserveAmt);
                                if (interestAmt > 0) {
                                    interestAmount += interestAmt;
                                }
                                if(reserveAmt>0) {
                                    totalReserveAmount += reserveAmt;
                                }
                            }
                        } else {
                            double amt = CommonUtil.convertObjToDouble(dataMap.get("AMT"));
                            if (amt > 0) {
                                double calcPercent = CommonUtil.convertObjToDouble(percent)-reservePercent;
                                System.out.println("calPercent--Jeff---"+calcPercent+"reservePercent---Jeff---"+reservePercent+"hereeeeeee");
                                double interestAmt = (amt * calcPercent) / 1200;
                                double reserveAmt = (amt * CommonUtil.convertObjToDouble(reservePercent))/1200;
                                System.out.println("interestAmt"+interestAmt+"reserveAmt"+reserveAmt);
                                if (interestAmt > 0) {
                                    interestAmount += interestAmt;
                                }
                                if(reserveAmt>0) {
                                    totalReserveAmount += reserveAmt;
                                }
                            }
                        }
                    }
                    map.put(actNum, Math.round(interestAmount));
                    map.put(actNum+"_1", Math.round(totalReserveAmount));
                    map.put("actNoList", actNoList);
                }
            }
        }
        return map;
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("map in getData of InterestProcessingDAO : " + map);
        _branchCode = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
        currDt = ServerUtil.getCurrentDate(_branchCode);
        HashMap returnMap = new HashMap();
        Date fromDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("FROM_DATE")));
        Date toDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("TO_DATE")));
        String percent = CommonUtil.convertObjToStr(map.get("PERCENT"));
        double reservePercent = 0.0;
        if(map.containsKey("RESERVE_PERCENT")){
            reservePercent = CommonUtil.convertObjToDouble(map.get("RESERVE_PERCENT"));
        }
        System.out.println("reservePercent---Jeff---"+reservePercent);
        String behavesLike = CommonUtil.convertObjToStr(map.get("BEHAVES_LIKE"));
        HashMap dateMap = new HashMap();
        dateMap.put("BEHAVES_LIKE", behavesLike);
        dateMap.put("FROM_DATE", fromDate);
        dateMap.put("TO_DATE", toDate);
        dateMap.put(CommonConstants.BRANCH_ID, _branchCode);
        List completeList = new ArrayList();
        List depositList = sqlMap.executeQueryForList("getThriftBenevolentDepositDetails", dateMap);
        HashMap dayEndCheckMap = new HashMap();
        dayEndCheckMap.put("BEHAVES_LIKE", behavesLike);
        dayEndCheckMap.put("CHECK_DATE", fromDate);
        dayEndCheckMap.put(CommonConstants.BRANCH_ID, _branchCode);
        List depositDayEndCheckList = sqlMap.executeQueryForList("getThriftBenevolentDepositDetails", dayEndCheckMap);
        if (depositDayEndCheckList != null && depositDayEndCheckList.size() > 0 && depositList != null && depositList.size() > 0) {
            depositList = mergeDataList(depositList, depositDayEndCheckList, fromDate);
        }
        if (depositList != null && depositList.size() > 0) {
            HashMap dataMap = calcInterest(depositList, percent, reservePercent, map, behavesLike);
            System.out.println("dataMap"+dataMap);
            if (dataMap != null && dataMap.size() > 0) {
                List actNoList = (List) dataMap.get("actNoList");
                System.out.println("actNoList"+actNoList);
                if (actNoList != null && actNoList.size() > 0) {
                    for (int i = 0; i < actNoList.size(); i++) {
                        HashMap depositNumMap = new HashMap();
                        List dataList = new ArrayList();
                        depositNumMap.put("ACC_NUM", CommonUtil.convertObjToStr(actNoList.get(i)));
                        List depositCustomerDetailsList = sqlMap.executeQueryForList("getAccountNumberForDeposit", depositNumMap);
                        if (depositCustomerDetailsList != null && depositCustomerDetailsList.size() > 0) {
                            if (behavesLike.equals("THRIFT")) {
                                HashMap detailedDataMap = (HashMap) depositCustomerDetailsList.get(0);
                                dataList.add(CommonUtil.convertObjToStr(detailedDataMap.get("DEPOSIT_NO")));
                                dataList.add(CommonUtil.convertObjToStr(detailedDataMap.get("CUSTOMER_NAME")));
                                HashMap outStandingMap = new HashMap();
                                outStandingMap.put("ACT_NUM", CommonUtil.convertObjToStr(detailedDataMap.get("DEPOSIT_NO")));
                                outStandingMap.put("TO_DATE", toDate);
                                List getOuststandingForPeriod = sqlMap.executeQueryForList("getOutstandingAmountThriftBenevolent", outStandingMap);
                                if (getOuststandingForPeriod != null && getOuststandingForPeriod.size() > 0) {
                                    HashMap amountMap = (HashMap) getOuststandingForPeriod.get(0);
                                    dataList.add(CommonUtil.convertObjToStr(amountMap.get("AMOUNT")));
                                }
                                dataList.add(CommonUtil.convertObjToStr(detailedDataMap.get("PROD_ID")));
                                dataList.add(CommonUtil.convertObjToStr(dataMap.get(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(actNoList.get(i))))));
                            }
                            if (behavesLike.equals("BENEVOLENT")) {
                                HashMap detailedDataMap = (HashMap) depositCustomerDetailsList.get(0);
                                dataList.add(CommonUtil.convertObjToStr(detailedDataMap.get("DEPOSIT_NO")));
                                dataList.add(CommonUtil.convertObjToStr(detailedDataMap.get("CUSTOMER_NAME")));
                                HashMap outStandingMap = new HashMap();
                                outStandingMap.put("ACT_NUM", CommonUtil.convertObjToStr(detailedDataMap.get("DEPOSIT_NO")));
                                outStandingMap.put("TO_DATE", toDate);
                                List getOuststandingForPeriod = sqlMap.executeQueryForList("getOutstandingAmountThriftBenevolent", outStandingMap);
                                if (getOuststandingForPeriod != null && getOuststandingForPeriod.size() > 0) {
                                    HashMap amountMap = (HashMap) getOuststandingForPeriod.get(0);
                                    dataList.add(CommonUtil.convertObjToStr(amountMap.get("AMOUNT")));
                                }
                                dataList.add(CommonUtil.convertObjToStr(detailedDataMap.get("PROD_ID")));
                                dataList.add(CommonUtil.convertObjToStr(dataMap.get(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(actNoList.get(i))))));
                                String acctNo = CommonUtil.convertObjToStr(actNoList.get(i));
                                System.out.println("acctNo"+acctNo);
                                dataList.add(CommonUtil.convertObjToStr(dataMap.get(acctNo+"_1")));
                            }                            
                        }
                        completeList.add(dataList);
                    }
                }
            }
        }
        returnMap.put("detailedList", completeList);
        System.out.println("returnMap---Jeff---" + returnMap);
        return returnMap;
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("#### inside InterestProcessingDAO execute() map : " + map);
        _branchCode = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        LogDAO objLogDAO = new LogDAO();
        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        currDt = ServerUtil.getCurrentDate(_branchCode);
        branchId = objLogTO.getBranchId();
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));

        depositInterestDetailsList = new ArrayList();
        returnMap = new HashMap();
        command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        System.out.println("command" + command);
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            if (map.containsKey("DEPOSIT_INTEREST_DETAILS")) {
                depositInterestDetailsList = (ArrayList) map.get("DEPOSIT_INTEREST_DETAILS");
                System.out.println("depositInterestDetailsList---Jeff---" + depositInterestDetailsList);
                insertData(map, objLogDAO, objLogTO);
            }
        }else {
            throw new NoCommandException();
        }
        return resultMap;
    }


    private void insertData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {

            sqlMap.startTransaction();
//            objShareDividendCalculationTO.setStatus(CommonConstants.STATUS_CREATED);
//            objLogTO.setData(objShareDividendCalculationTO.toString());
//            objLogTO.setPrimaryKey(objShareDividendCalculationTO.getKeyData());
//            objLogDAO.addToLog(objLogTO);
//            System.out.println("!@#!@#@#$inside Insert:"+map);
            executeTransactionPart(objLogDAO, objLogTO, map);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            System.out.println("e : " + e);
            throw e;
        }
    }

    private void executeTransactionPart(LogDAO objLogDAO, LogTO objLogTO, HashMap map) throws Exception {
        try {
            System.out.println("#@$@#$map:" + map);
            List depositInterestDetails = new ArrayList();
            if (map.containsKey("DEPOSIT_INT_MASTER")) {
                ThriftBenCalculationTO objThriftBenCalculationTo = (ThriftBenCalculationTO) map.get("DEPOSIT_INT_MASTER");
                sqlMap.executeUpdate("inserThriftBenCalcMasterTO", objThriftBenCalculationTo);
            }
            if (map.containsKey("DEPOSIT_INTEREST_DETAILS")) {
                depositInterestDetails = (List) map.get("DEPOSIT_INTEREST_DETAILS");
            }

            if (depositInterestDetails != null && depositInterestDetails.size() > 0) {
                double totalAmount = CommonUtil.convertObjToDouble(map.get("TOTAL_AMT"));
                String debitGl = CommonUtil.convertObjToStr(map.get("DEBIT_GL"));
                String payableGl = CommonUtil.convertObjToStr(map.get("PAYABLE_GL"));
                Date frmDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("FROM_DT")));
                String percent = CommonUtil.convertObjToStr(map.get("PERCENT"));
                Date toDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("TO_DT")));
                Date resolutionDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("RESOLUTION_DT")));
                String resolutionNo = CommonUtil.convertObjToStr(map.get("RESOLUTION_NO"));
                String remarks = CommonUtil.convertObjToStr(map.get("REMARKS"));
                String behavesLike = CommonUtil.convertObjToStr(map.get("BEHAVES_LIKE"));
                String reserveFundGl = null;
                String reserveFundPercent = null;
                if(map.containsKey("RESERVE_FUND_GL") && map.containsKey("RESERVE_FUND_PERCENT")){
                    reserveFundGl = CommonUtil.convertObjToStr(map.get("RESERVE_FUND_GL"));
                    reserveFundPercent = CommonUtil.convertObjToStr(map.get("RESERVE_FUND_PERCENT"));
                }
                System.out.println("reserveFundGl"+reserveFundGl+"reserveFundPercent"+reserveFundPercent);
                System.out.println("totalAmount" + totalAmount + "debitGl" + debitGl + "payableGl" + payableGl
                        + "frmDate" + frmDate + "percent" + percent + "toDate" + toDate
                        + "resolutionDate" + resolutionDate + "resolutionNo" + resolutionNo
                        + "remarks" + remarks + "behavesLike" + behavesLike);
                if (behavesLike != null && !behavesLike.equals("") && behavesLike.equals("THRIFT")) {
                    System.out.println("%%%######----BEHAVES_LIKE" + behavesLike);
                    System.out.println("@##$#$% _branchCode   #### :" + _branchCode);
                    transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                    transactionDAO.setInitiatedBranch(_branchCode);
                    transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                    TxTransferTO transferTo = new TxTransferTO();
                    ArrayList TxTransferTO = new ArrayList();
                    HashMap txMap = new HashMap();
                    HashMap transMap = new HashMap();
                    String prodId = "";
                    String creditAccNo = "";
                    if (totalAmount > 0.0) {
                        /*
                         * --------------------------------------------DEBIT---------------------------------------------------
                         */
                        System.out.println("Interest debit Started");
                        transferTo = new TxTransferTO();
                        txMap = new HashMap();
                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
                        txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                        txMap.put("DR_INSTRUMENT_1", "TO "+behavesLike+" INTEREST");
                        txMap.put(TransferTrans.DR_AC_HD, debitGl);
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                        txMap.put(TransactionDAOConstants.DEBIT, TransactionDAOConstants.DEBIT);
                        //txMap.put("LINK_BATCH_ID", "");                        
                        if(map.containsKey(CommonConstants.SCREEN) && map.get(CommonConstants.SCREEN) != null){
                           txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN)); 
                        }                           
                        txMap.put(TransferTrans.PARTICULARS, "TRANSFER TO "+behavesLike+" DEPOSIT");
                        System.out.println("txMap2 : " + txMap + "totalAmount :" + totalAmount);
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, totalAmount);                        
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setTransDt(currDt);
                        transferTo.setInitiatedBranch(_branchCode);
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        TxTransferTO.add(transferTo);
                        System.out.println("TxTransferTO #@$@#$: DEBIT" + TxTransferTO);
                        /*
                         * --------------------------------------------CREDIT---------------------------------------------------
                         */
                        for (int j = 0; j < depositInterestDetails.size(); j++) {
                            ArrayList depositList = (ArrayList) depositInterestDetails.get(j);
                            System.out.println("depositList---Jeff---Here" + depositList);
                            creditAccNo = CommonUtil.convertObjToStr(depositList.get(0));
                            //prodId = CommonUtil.convertObjToStr(depositList.get(3));
                            prodId = CommonUtil.convertObjToStr(depositList.get(depositList.size() - 1));
                            //double creditAmount = CommonUtil.convertObjToDouble(depositList.get(4));
                            double creditAmount = CommonUtil.convertObjToDouble(depositList.get(2));
                            System.out.println("Interest Credit Started");
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            txMap.put("DR_INSTRUMENT_1", behavesLike+" INTEREST PAID");
                            txMap.put(TransferTrans.CR_AC_HD, payableGl);
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                            txMap.put(TransferTrans.CR_PROD_ID, prodId);
                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                            txMap.put(TransferTrans.CR_ACT_NUM, creditAccNo+"_1");
                            txMap.put(TransactionDAOConstants.CREDIT, TransactionDAOConstants.CREDIT);
                            txMap.put(TransferTrans.PARTICULARS, "Interest upto" + DateUtil.getStringDate(toDate));
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                            txMap.put("LINK_BATCH_ID", creditAccNo+"_1");
                            if(map.containsKey(CommonConstants.SCREEN) && map.get(CommonConstants.SCREEN) != null){
                              txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN)); 
                            }
                            System.out.println("txMap1 : " + txMap + "totalAmount :" + creditAmount);
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, creditAmount);
                            transferTo.setGlTransActNum(creditAccNo+"_1");
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt(currDt);
                            transferTo.setInitiatedBranch(_branchCode);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            TxTransferTO.add(transferTo);
                        }
                        System.out.println("TxTransferTO #@$@#$: CREDIT" + TxTransferTO);
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
                            e.printStackTrace();
                        }
                        authorizeTransaction(transMap, map);
                    }
                    System.out.println("depositInterestDetails---Here---" + depositInterestDetails);
                    for (int j = 0; j < depositInterestDetails.size(); j++) {
                        ArrayList depositList = (ArrayList) depositInterestDetails.get(j);
                        System.out.println("depositList---Jeff---" + depositList);
                        Date intApplyDate = DateUtil.addDays(toDate, -1);
                        String depNo = CommonUtil.convertObjToStr(depositList.get(0));
//                        double creditAmt = CommonUtil.convertObjToDouble(depositList.get(4));
//                        String productId = CommonUtil.convertObjToStr(depositList.get(3));
                        double creditAmt = CommonUtil.convertObjToDouble(depositList.get(2));
                        String productId = CommonUtil.convertObjToStr(depositList.get(depositList.size()-1));
                        double outstandingAmt = CommonUtil.convertObjToDouble(depositList.get(2));
                        System.out.println("depNo---Jeff---:::" + depNo + "creditAmt---Jeff---:::" + creditAmt + "productId--Jeff" + productId+
                                "outstandingAmt---Jeff---"+outstandingAmt);
                        HashMap newBalanceMap = new HashMap();
                        newBalanceMap.put("AMOUNT", creditAmt);
                        newBalanceMap.put("ACT_NUM", depNo);
                        sqlMap.executeUpdate("updateAvailableClearTotalThriftBenevolentBalance", newBalanceMap);
                        HashMap intUpdateMap = new HashMap();
                        intUpdateMap.put("DEPOSIT_NO", depNo);
                        intUpdateMap.put("LAST_TRANS_DT", currDt);
                        intUpdateMap.put("LAST_INT_APPL_DT", intApplyDate);
                        sqlMap.executeUpdate("updateLastIntApplDtLastTransDtThriftBenevolent", intUpdateMap);
                        InterestBatchTO interestBatchTO = new InterestBatchTO();
                        //interestBatchTO.setIntDt(frmDate);
                        interestBatchTO.setIntDt(setProperDtFormat(frmDate));// KD-2818 - On 24-04-2021
                        interestBatchTO.setApplDt(intApplyDate);
                        interestBatchTO.setActNum(depNo + "_1");
                        interestBatchTO.setProductId(productId);
                        interestBatchTO.setPrincipleAmt(new Double(outstandingAmt));
                        HashMap custMap = new HashMap();
                        custMap.put("ACT_NUM", depNo);
                        List custIdList = sqlMap.executeQueryForList("getThriftBenevolentCustId",custMap);
                        if(custIdList != null && custIdList.size() > 0) {
                            HashMap customerIdMap = (HashMap) custIdList.get(0);
                            if (customerIdMap.containsKey("CUST_ID")) {
                                String custId = CommonUtil.convertObjToStr(customerIdMap.get("CUST_ID"));
                                interestBatchTO.setCustId(custId);
                            }
                        }
                        interestBatchTO.setIntRate(CommonUtil.convertObjToDouble(percent));
                        interestBatchTO.setIntType("COMPOUND");
                        interestBatchTO.setProductType(TransactionFactory.DEPOSITS);
                        interestBatchTO.setTrnDt(currDt);
                        System.out.println("interest"+interestBatchTO);
                        HashMap renewalCountMap = new HashMap();
                        renewalCountMap.put("ACT_NUM", depNo);
                        List lstCount = sqlMap.executeQueryForList("getSelectRenewalCount", renewalCountMap);
                        if (lstCount != null && lstCount.size() > 0) {
                            renewalCountMap = (HashMap) lstCount.get(0);
                            interestBatchTO.setSlNo(CommonUtil.convertObjToDouble(renewalCountMap.get("RENEWAL_COUNT")));
                        }
                        if (creditAmt > 0) {
                            interestBatchTO.setDrCr("CREDIT");
                            interestBatchTO.setTransLogId("A");
                            interestBatchTO.setIntAmt(new Double(creditAmt));
                            interestBatchTO.setTot_int_amt(new Double(creditAmt));
                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                            interestBatchTO.setDrCr("DEBIT");
                            interestBatchTO.setTransLogId("A");
                            interestBatchTO.setIntAmt(new Double(creditAmt));
                            interestBatchTO.setTot_int_amt(new Double(0.0));
                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                        } 
                        creditAmt = 0.0;
                        interestBatchTO = null;
                    }
                    /*---------------------------- THRIFT OVER ------------------------------*/
                }
                if (behavesLike != null && !behavesLike.equals("") && behavesLike.equals("BENEVOLENT")) {
                    System.out.println("%%%######----BEHAVES_LIKE" + behavesLike);
                    System.out.println("reserveFundGl"+reserveFundGl+"reserveFundPercent"+reserveFundPercent);
                    transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                    transactionDAO.setInitiatedBranch(_branchCode);
                    transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                    TxTransferTO transferTo = new TxTransferTO();
                    ArrayList TxTransferTO = new ArrayList();
                    HashMap txMap = new HashMap();
                    HashMap transMap = new HashMap();
                    String prodId = "";
                    String creditAccNo = "";
                    if (totalAmount > 0.0) {
                        /*
                         * --------------------------------------------DEBIT--------------------------------------------------- *
                         */
                        System.out.println("Interest debit Started");
                        transferTo = new TxTransferTO();
                        txMap = new HashMap();
                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
                        txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                        txMap.put("DR_INSTRUMENT_1", "TO "+behavesLike+" INTEREST");
                        txMap.put(TransferTrans.DR_AC_HD, debitGl);
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                        txMap.put(TransactionDAOConstants.DEBIT, TransactionDAOConstants.DEBIT);
                        txMap.put("LINK_BATCH_ID", "");
                        txMap.put(TransferTrans.PARTICULARS, "TRANSFER TO "+behavesLike+" DEPOSIT");
                        if(map.containsKey(CommonConstants.SCREEN) && map.get(CommonConstants.SCREEN) != null){
                              txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN)); 
                        }
                        System.out.println("txMap2 : " + txMap + "totalAmount :" + totalAmount);
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, totalAmount);
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setTransDt(currDt);
                        transferTo.setInitiatedBranch(_branchCode);
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        TxTransferTO.add(transferTo);
                        System.out.println("TxTransferTO #@$@#$: DEBIT" + TxTransferTO);
                        /*
                         * --------------------------------------------CREDIT PAYABLE GL--------------------------------------------------- *
                         */
                        for (int j = 0; j < depositInterestDetails.size(); j++) {
                            ArrayList depositList = (ArrayList) depositInterestDetails.get(j);
                            System.out.println("depositList---Jeff---Here" + depositList);
                            creditAccNo = CommonUtil.convertObjToStr(depositList.get(0));
//                            prodId = CommonUtil.convertObjToStr(depositList.get(3));
//                            double creditAmount = CommonUtil.convertObjToDouble(depositList.get(4));
//                            double reserveFund = CommonUtil.convertObjToDouble(depositList.get(5));
                            prodId = CommonUtil.convertObjToStr(depositList.get(depositList.size()-1));
                            double creditAmount = CommonUtil.convertObjToDouble(depositList.get(4));
                            double reserveFund = CommonUtil.convertObjToDouble(depositList.get(3));
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            txMap.put("DR_INSTRUMENT_1", behavesLike+" INTEREST PAID");
                            txMap.put(TransferTrans.CR_AC_HD, payableGl);
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransactionDAOConstants.CREDIT, TransactionDAOConstants.CREDIT);
                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                            txMap.put(TransferTrans.PARTICULARS, "Benevolent Interest upto" + DateUtil.getStringDate(toDate));
                            txMap.put(TransferTrans.CR_ACT_NUM, creditAccNo+"_1");
                            txMap.put("LINK_BATCH_ID", creditAccNo);
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                            txMap.put("LINK_BATCH_ID", creditAccNo+"_1");
                            if(map.containsKey(CommonConstants.SCREEN) && map.get(CommonConstants.SCREEN) != null){
                              txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN)); 
                            }
                            System.out.println("txMap1 : " + txMap + "totalAmount :" + creditAmount);
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, creditAmount);
                            transferTo.setGlTransActNum(creditAccNo+"_1");
                            transferTo.setActNum(creditAccNo+"_1");
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt(currDt);
                            transferTo.setInitiatedBranch(_branchCode); 
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            TxTransferTO.add(transferTo);
                            /*
                        * --------------------------------------------CREDIT RESERVE GL--------------------------------------------------- *
                            */
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            txMap.put("DR_INSTRUMENT_1", behavesLike+" RESERVE PAID");
                            txMap.put(TransferTrans.CR_AC_HD, reserveFundGl);
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                            txMap.put(TransactionDAOConstants.CREDIT, TransactionDAOConstants.CREDIT);
                            txMap.put(TransferTrans.PARTICULARS, "Reverse Int upto" + DateUtil.getStringDate(toDate));
                            txMap.put(TransferTrans.CR_ACT_NUM, creditAccNo+"_1");
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                            txMap.put("LINK_BATCH_ID", creditAccNo+"_1");
                            if(map.containsKey(CommonConstants.SCREEN) && map.get(CommonConstants.SCREEN) != null){
                              txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN)); 
                            }
                            System.out.println("txMap1 : " + txMap + "totalAmount :" + reserveFund);
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, reserveFund);
                            transferTo.setGlTransActNum(creditAccNo+"_1");
                            transferTo.setActNum(creditAccNo+"_1");
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt(currDt);
                            transferTo.setInitiatedBranch(_branchCode);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            TxTransferTO.add(transferTo);
                        }
                        
                        System.out.println("TxTransferTO #@$@#$: CREDIT" + TxTransferTO);
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
                            e.printStackTrace();
                        }
                        authorizeTransaction(transMap, map);
                    }
                    for (int j = 0; j < depositInterestDetails.size(); j++) {
                        ArrayList depositList = (ArrayList) depositInterestDetails.get(j);
                        System.out.println("depositList---Jeff---" + depositList);
                        Date intApplyDate = DateUtil.addDays(toDate, -1);
                        String depNo = CommonUtil.convertObjToStr(depositList.get(0));
//                        double creditAmt = CommonUtil.convertObjToDouble(depositList.get(4));
//                        String productId = CommonUtil.convertObjToStr(depositList.get(3));
//                        double outstandingAmt = CommonUtil.convertObjToDouble(depositList.get(2));
//                        double reserveAmount = CommonUtil.convertObjToDouble(depositList.get(5));
                       
                        String productId = CommonUtil.convertObjToStr(depositList.get(depositList.size()-1));
                        double creditAmt = CommonUtil.convertObjToDouble(depositList.get(4));
                        double outstandingAmt = CommonUtil.convertObjToDouble(depositList.get(2));
                        double reserveAmount = CommonUtil.convertObjToDouble(depositList.get(3));
                        
                        System.out.println("depNo---Jeff---:::" + depNo + "creditAmt---Jeff---:::" + creditAmt + "productId--Jeff" + productId+
                                "outstandingAmt---Jeff---"+outstandingAmt);
                        HashMap newBalanceMap = new HashMap();
                        newBalanceMap.put("AMOUNT", creditAmt);
                        newBalanceMap.put("ACT_NUM", depNo);
                        sqlMap.executeUpdate("updateTotIntCreditBenevolent", newBalanceMap);
                        HashMap intUpdateMap = new HashMap();
                        intUpdateMap.put("DEPOSIT_NO", depNo);
                        intUpdateMap.put("LAST_TRANS_DT", currDt);
                        intUpdateMap.put("LAST_INT_APPL_DT", intApplyDate);
                        sqlMap.executeUpdate("updateLastIntApplDtLastTransDtThriftBenevolent", intUpdateMap);
                        InterestBatchTO interestBatchTO = new InterestBatchTO();
                        //interestBatchTO.setIntDt(frmDate);
                        interestBatchTO.setIntDt(setProperDtFormat(frmDate)); // KD-2818 - On 24-04-2021
                        interestBatchTO.setApplDt(intApplyDate);
                        interestBatchTO.setActNum(depNo + "_1");
                        interestBatchTO.setProductId(productId);
                        interestBatchTO.setPrincipleAmt(new Double(outstandingAmt));
                        HashMap custMap = new HashMap();
                        custMap.put("ACT_NUM", depNo);
                        List custIdList = sqlMap.executeQueryForList("getThriftBenevolentCustId",custMap);
                        if(custIdList != null && custIdList.size() > 0) {
                            HashMap customerIdMap = (HashMap) custIdList.get(0);
                            if (customerIdMap.containsKey("CUST_ID")) {
                                String custId = CommonUtil.convertObjToStr(customerIdMap.get("CUST_ID"));
                                interestBatchTO.setCustId(custId);
                            }
                        }
                        interestBatchTO.setIntRate(CommonUtil.convertObjToDouble(percent));
                        interestBatchTO.setReserveFundPercent(CommonUtil.convertObjToDouble(reserveFundPercent));
                        interestBatchTO.setIntType("COMPOUND");
                        interestBatchTO.setProductType(TransactionFactory.DEPOSITS);
                        interestBatchTO.setTrnDt(currDt);
                        System.out.println("interest"+interestBatchTO);
                        HashMap renewalCountMap = new HashMap();
                        renewalCountMap.put("ACT_NUM", depNo);
                        List lstCount = sqlMap.executeQueryForList("getSelectRenewalCount", renewalCountMap);
                        if (lstCount != null && lstCount.size() > 0) {
                            renewalCountMap = (HashMap) lstCount.get(0);
                            interestBatchTO.setSlNo(CommonUtil.convertObjToDouble(renewalCountMap.get("RENEWAL_COUNT")));
                        }
                        if (creditAmt > 0) {
                            interestBatchTO.setDrCr("CREDIT");
                            interestBatchTO.setTransLogId("A");
                            interestBatchTO.setIntAmt(new Double(creditAmt));
                            interestBatchTO.setReserveFundAmt(reserveAmount);
                            interestBatchTO.setTot_int_amt(new Double(creditAmt));
                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                        } 
                        creditAmt = 0.0;
                        interestBatchTO = null;
                    }
                    /*---------------------------- BENEVOLENT OVER ------------------------------*/
                }
            }

        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
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

}
