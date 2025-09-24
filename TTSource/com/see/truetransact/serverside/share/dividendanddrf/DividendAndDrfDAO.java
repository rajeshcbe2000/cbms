 /*
 * Copyright 2010 Fincuro Solutions (P) Ltd. All rights reserved.
 *
 * This software is the proprietary information of Fincuro Solutions (P) Ltd..
 * 
 *
 * DividendAndDrfDAO.java
 *
 * Created on Mar  28 10:59:57 GMT+05:30 2020
 */
package com.see.truetransact.serverside.share.dividendanddrf;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.investments.InvestmentsTransDAO;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.investments.InvestmentsTransTO;
import com.see.truetransact.transferobject.share.DrfTransactionTO;
import com.see.truetransact.transferobject.share.ShareDividendCalculationDetailsTO;
import com.see.truetransact.transferobject.share.ShareDividendCalculationTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import java.text.DecimalFormat;
import java.util.*;
import org.apache.log4j.Logger;

/**
 * DividendAndDrfDAO DAO.
 *
 * @author Rishad M.P
 *
 */
public class DividendAndDrfDAO extends TTDAO {

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
    private final static Logger log = Logger.getLogger(DividendAndDrfDAO.class);
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
    public DividendAndDrfDAO() throws ServiceLocatorException {
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
            DividendAndDrfDAO dao = new DividendAndDrfDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        HashMap transMap = new HashMap();
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
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));
        shareProductDetails = new HashMap();
        returnMap = new HashMap();
        if (map.containsKey("ShareDividendCalculationTO")) {
            objShareDividendCalculationTO = (ShareDividendCalculationTO) map.get("ShareDividendCalculationTO");
            final TOHeader toHeader = objShareDividendCalculationTO.getTOHeader();
            command = CommonUtil.convertObjToStr(map.get("COMMAND"));
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                if (map.containsKey("LOAD_DATA")) {
                    if (map.containsKey("SHARE_PROD_DETAIL")) {
                        shareProductDetails = (HashMap) map.get("SHARE_PROD_DETAIL");
                    }
                    HashMap calculationMap = getShareAcctDetails(map);
                    returnMap = calculationMap;
                } else if (map.containsKey("SHARE_PROD_DETAIL_SAVE")) {
                    insertData(map, objLogDAO, objLogTO);
                }
            } else {
                throw new NoCommandException();
            }
            destroyObjects();
        } else if (map.containsKey("DRF_LOAD_MAP")) {
            HashMap drfCalculationMap = getShareDrfDetails(map);
            returnMap = drfCalculationMap;
        } else if (map.containsKey("DRF_LOAD_SAVE_MAP")) {
            insertDrfData(map, objLogDAO, objLogTO);
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

    private HashMap getShareAcctDetails(HashMap Sharemap) throws Exception {
        HashMap shareAcctDetailsMap = new HashMap();
        ArrayList shareCustDetailsList = new ArrayList();
        try {
            if (Sharemap.containsKey("ShareDividendCalculationTO")) {
                objShareDividendCalculationTO = (ShareDividendCalculationTO) Sharemap.get("ShareDividendCalculationTO");
                Date fromPeriod = objShareDividendCalculationTO.getTdtFromPeriod();
                Date toPeriod = objShareDividendCalculationTO.getTdtToPeriod();
                String dividendPercent = objShareDividendCalculationTO.getTxtDividendPercent();
                String closedYN = "";
                if (Sharemap != null && Sharemap.containsKey("CLOSED_REQ")) {
                    closedYN = CommonUtil.convertObjToStr(Sharemap.get("CLOSED_REQ"));
                    objShareDividendCalculationTO.setClosedFreq(closedYN);
                } else {
                    objShareDividendCalculationTO.setClosedFreq("N");
                }
                List shareMemberList = sqlMap.executeQueryForList("getShareDividendDetails", objShareDividendCalculationTO);
                if (shareMemberList != null && shareMemberList.size() > 0) {
                    for (int i = 0; i < shareMemberList.size(); i++) {
                        HashMap shareMemberMap = (HashMap) shareMemberList.get(i);
                        shareMemberMap.put("FROM_PERIOD", fromPeriod);
                        shareMemberMap.put("TO_PERIOD", toPeriod);
                        double shAmt = 0;
                        shareAcctDetailsMap = calculateAmountBeforeFromPeriod(shareMemberMap, objShareDividendCalculationTO);
                        shareAcctDetailsMap = calculateDividendAmount(shareAcctDetailsMap, objShareDividendCalculationTO);
                        if (shareCustDetailsList == null && shareCustDetailsList.size() <= 0) {
                            shareCustDetailsList = new ArrayList();
                        }
                        shareCustDetailsList.add(shareAcctDetailsMap);
                    }
                    shareAcctDetailsMap = new HashMap();
                    shareAcctDetailsMap.put("TOTAL_EMPLOYEE_DETAILS", shareCustDetailsList);
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            System.out.println("e : " + e);
            throw e;
        }
        return shareAcctDetailsMap;
    }

     private HashMap getShareDrfDetails(HashMap drfMap) throws Exception {
        HashMap shareDrfAcctDetailsMap = new HashMap();
        ArrayList drfCustDetailsList = new ArrayList();
        try {
            if (drfMap.containsKey("DRF_LOAD_MAP")) {
                HashMap whereMap = new HashMap();
                whereMap = (HashMap) drfMap.get("DRF_LOAD_MAP");
                List shareMemberList = sqlMap.executeQueryForList("getShareDrfDetails", whereMap);
                if (shareMemberList != null && shareMemberList.size() > 0) {
                    for (int i = 0; i < shareMemberList.size(); i++) {
                        HashMap shareMemberMap = (HashMap) shareMemberList.get(i);
                        if (drfCustDetailsList == null && drfCustDetailsList.size() <= 0) {
                            drfCustDetailsList = new ArrayList();
                        }
                        drfCustDetailsList.add(shareMemberMap);
                    }
                    shareDrfAcctDetailsMap = new HashMap();
                    shareDrfAcctDetailsMap.put("TOTAL_DRF_DETAILS", drfCustDetailsList);
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            System.out.println("e : " + e);
            throw e;
        }
        return shareDrfAcctDetailsMap;
    }

    
    private HashMap calculateDividendAmount(HashMap individualDetailsMap, ShareDividendCalculationTO objShareDividendCalcTO) throws Exception {
        if (individualDetailsMap != null && individualDetailsMap.containsKey("MEMBERSHIP_NO")
                && individualDetailsMap.get("MEMBERSHIP_NO") == null) {
            individualDetailsMap.put("MEMBERSHIP_NO", individualDetailsMap.get("SHARE_ACCT_NO"));
        }
        Date fromPeriod = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objShareDividendCalcTO.getTdtFromPeriod()));
        Date startDt = null;
        Date nextDate = null;
        Date toPeriod = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objShareDividendCalcTO.getTdtToPeriod()));
         Date toPeriod_Daily = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objShareDividendCalcTO.getTdtToPeriod()));
        toPeriod = DateUtil.addDays(toPeriod, -1);
        double dividendPercent = CommonUtil.convertObjToDouble(objShareDividendCalcTO.getTxtDividendPercent()).doubleValue();
        double minimumShareDividend = CommonUtil.convertObjToDouble(shareProductDetails.get("MIN_DIVIDEND_AMOUNT")).doubleValue();
        double shareAmt = 0.0;
        if (individualDetailsMap.containsKey("BALANCE_AS_ON_START")) {
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
                        nextDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(customerSlabsBtwPeriodDailyMap.get("SHARE_CERT_ISSUE_DT")));
                        int noOfDays = (int) DateUtil.dateDiff(startDt, nextDate);
                        startDt = nextDate;
                        dividendAmount = dividendAmount + (shareAmt * dividendPercent * noOfDays) / 36500;
                        if (CommonUtil.convertObjToStr(customerSlabsBtwPeriodDailyMap.get("SHARE_NO_FROM")).equals("ADD")) {
                            shareAmt = shareAmt + CommonUtil.convertObjToDouble(customerSlabsBtwPeriodDailyMap.get("SHARE_VALUE")).doubleValue();
                            if (i == customerSlabsBtwPeriodDaily.size() - 1) {
                                noOfDays = (int) DateUtil.dateDiff(startDt, toPeriod_Daily);
                                dividendAmount += shareAmt * dividendPercent * noOfDays / 36500;
                            }
                        } else if (CommonUtil.convertObjToStr(customerSlabsBtwPeriodDailyMap.get("SHARE_NO_FROM")).equals("WITHDRAWAL")) {
                            shareAmt = shareAmt - CommonUtil.convertObjToDouble(customerSlabsBtwPeriodDailyMap.get("SHARE_VALUE")).doubleValue();
                            if (i == customerSlabsBtwPeriodDaily.size() - 1) {
                                noOfDays = (int) DateUtil.dateDiff(startDt, toPeriod_Daily);
                                dividendAmount += shareAmt * dividendPercent * noOfDays / 36500;
                            }
                        }
                    }
                } else {
                     int noOfDays = (int) DateUtil.dateDiff(fromPeriod, toPeriod_Daily);
                    dividendAmount = noOfDays * shareAmt * dividendPercent / 36500;
                }
                if (shareProductDetails.containsKey("DIVIDEND_ROUND_OFF")) {
                    if (CommonUtil.convertObjToStr(shareProductDetails.get("DIVIDEND_ROUND_OFF")).equals("NO_ROUND")) {
                        DecimalFormat df = new DecimalFormat("#.##");
                        dividendAmount = CommonUtil.convertObjToDouble(df.format(dividendAmount)).doubleValue();
                    } else if (CommonUtil.convertObjToStr(shareProductDetails.get("DIVIDEND_ROUND_OFF")).equals("NEAREST_VAL")) {
                        dividendAmount = (double) getNearest((long) (dividendAmount * 100), 100) / 100;
                    }
                }
                if (dividendAmount < minimumShareDividend) {
                    dividendAmount = 0.0;
                }
                individualDetailsMap.put("DIVIDEND_AMOUNT", String.valueOf(dividendAmount));
            } else if (calcType.equals("MONTHLY_MIN_BAL")) {
                double balanceAsOnStart = shareAmt;
                int dayDiff = (int) DateUtil.dateDiff(fromPeriod, toPeriod);
                int noOfMonths = dayDiff / 30;
                Date startDayMonth = fromPeriod;
                Date lastDayMonth = toPeriod;
                HashMap empDetMapForMonthly = new HashMap();
                empDetMapForMonthly.put("SHARE_ACCT_NO", CommonUtil.convertObjToStr(individualDetailsMap.get("SHARE_ACCT_NO")));
                for (int i = 0; i < noOfMonths; i++) {
                    GregorianCalendar firstdaymonth = new GregorianCalendar(startDayMonth.getYear(), startDayMonth.getMonth(), 1);
                    int noOfDays = firstdaymonth.getActualMaximum(firstdaymonth.DAY_OF_MONTH);
                    Date lastDateOfMonth = (Date) currDt.clone();
                    lastDateOfMonth.setDate(noOfDays);
                    lastDateOfMonth.setMonth(startDayMonth.getMonth());
                    lastDateOfMonth.setYear(startDayMonth.getYear());
                    lastDateOfMonth = setProperDtFormat(lastDateOfMonth);
                    empDetMapForMonthly.put("FROM_PERIOD", startDayMonth);
                    empDetMapForMonthly.put("TO_PERIOD", lastDateOfMonth);
                    List customerSlabBtwPeriodMonthly = sqlMap.executeQueryForList("getCustomerShareBetweenPeriodDaily", empDetMapForMonthly);
                    if (customerSlabBtwPeriodMonthly != null && customerSlabBtwPeriodMonthly.size() > 0) {
                        empDetMapForMonthly.put("MONTH_START_AMOUNT", String.valueOf(shareAmt));
                        HashMap minimumAmount = calculateMinimumAmount(calcType, customerSlabBtwPeriodMonthly, empDetMapForMonthly);
                        double mimimumAmt = CommonUtil.convertObjToDouble(minimumAmount.get("MINIMUM_AMOUNT")).doubleValue();
                        dividendAmount += mimimumAmt * dividendPercent / 1200;
                        shareAmt = CommonUtil.convertObjToDouble(minimumAmount.get("NEXT_AMOUNT")).doubleValue();
                    } else {
                        dividendAmount += shareAmt * dividendPercent / 1200;
                    }
                    startDayMonth.setMonth(startDayMonth.getMonth() + 1);
                }
                if (shareProductDetails.containsKey("DIVIDEND_ROUND_OFF")) {
                    if (CommonUtil.convertObjToStr(shareProductDetails.get("DIVIDEND_ROUND_OFF")).equals("NO_ROUND")) {
                        DecimalFormat df = new DecimalFormat("#.##");
                        dividendAmount = CommonUtil.convertObjToDouble(df.format(dividendAmount)).doubleValue();
                    } else if (CommonUtil.convertObjToStr(shareProductDetails.get("DIVIDEND_ROUND_OFF")).equals("NEAREST_VAL")) {
                        dividendAmount = (double) getNearest((long) (dividendAmount * 100), 100) / 100;
                    }
                }
                if (dividendAmount < minimumShareDividend) {
                    dividendAmount = 0.0;
                }
                individualDetailsMap.put("DIVIDEND_AMOUNT", String.valueOf(dividendAmount));

            } else if (calcType.equals("MONTH_END_BAL")) {
                double balanceAsOnStart = shareAmt;
                int dayDiff = (int) DateUtil.dateDiff(fromPeriod, toPeriod);
                int noOfMonths = dayDiff / 30;
                Date startDayMonth = fromPeriod;
                Date lastDayMonth = toPeriod;
                dividendAmount = 0.0;
                HashMap empDetMapForMonthly = new HashMap();
                empDetMapForMonthly.put("SHARE_ACCT_NO", CommonUtil.convertObjToStr(individualDetailsMap.get("SHARE_ACCT_NO")));
                empDetMapForMonthly.put("SHARE_TYPE", CommonUtil.convertObjToStr(individualDetailsMap.get("SHARE_TYPE")));
                for (int i = 0; i < noOfMonths; i++) {
                    GregorianCalendar firstdaymonth = new GregorianCalendar(startDayMonth.getYear(), startDayMonth.getMonth(), 1);
                    int noOfDays = firstdaymonth.getActualMaximum(firstdaymonth.DAY_OF_MONTH);
                    Date lastDateOfMonth = (Date) currDt.clone();
                    lastDateOfMonth.setDate(noOfDays);
                    lastDateOfMonth.setMonth(startDayMonth.getMonth());
                    lastDateOfMonth.setYear(startDayMonth.getYear());
                    lastDateOfMonth = setProperDtFormat(lastDateOfMonth);
                    empDetMapForMonthly.put("FROM_PERIOD", lastDateOfMonth);
                    List monthEndBalLst = sqlMap.executeQueryForList("getShareAmtAsonFrmPeriod", empDetMapForMonthly);
                    if (monthEndBalLst != null && monthEndBalLst.size() > 0) {
                        HashMap monthEndBalMap = (HashMap) monthEndBalLst.get(0);
                        double monthEndAMt = CommonUtil.convertObjToDouble(monthEndBalMap.get("BALANCE_AS_ON_START")).doubleValue();
                        dividendAmount += monthEndAMt * dividendPercent / 1200;
                    } else {
                    }
                    startDayMonth.setMonth(startDayMonth.getMonth() + 1);
                }
                if (shareProductDetails.containsKey("DIVIDEND_ROUND_OFF")) {
                    if (CommonUtil.convertObjToStr(shareProductDetails.get("DIVIDEND_ROUND_OFF")).equals("NO_ROUND")) {
                        DecimalFormat df = new DecimalFormat("#.##");
                        dividendAmount = CommonUtil.convertObjToDouble(df.format(dividendAmount)).doubleValue();
                    } else if (CommonUtil.convertObjToStr(shareProductDetails.get("DIVIDEND_ROUND_OFF")).equals("NEAREST_VAL")) {
                        dividendAmount = (double) getNearest((long) (dividendAmount * 100), 100) / 100;
                    }
                }
                if (dividendAmount < minimumShareDividend) {
                    dividendAmount = 0.0;
                }
                individualDetailsMap.put("DIVIDEND_AMOUNT", String.valueOf(dividendAmount));
            } else if (calcType.equals("YR_END_BAL")) {
                double balanceAsOnStart = shareAmt;
                int dayDiff = (int) DateUtil.dateDiff(fromPeriod, toPeriod);
                int noOfYears = 1;
                Date startDayMonth = fromPeriod;
                Date lastDayMonth = toPeriod;
                dividendAmount = 0.0;
                HashMap empDetMapForMonthly = new HashMap();
                empDetMapForMonthly.put("SHARE_ACCT_NO", CommonUtil.convertObjToStr(individualDetailsMap.get("SHARE_ACCT_NO")));
                empDetMapForMonthly.put("SHARE_TYPE", CommonUtil.convertObjToStr(individualDetailsMap.get("SHARE_TYPE")));
                for (int i = 0; i < noOfYears; i++) {
                    Date lastDateOfMonth = (Date) currDt.clone();
                    lastDateOfMonth = startDayMonth;
                    lastDateOfMonth.setYear(startDayMonth.getYear() + 1);
                    lastDateOfMonth = DateUtil.addDays(lastDateOfMonth, -1);
                    lastDateOfMonth = setProperDtFormat(lastDateOfMonth);
                    empDetMapForMonthly.put("FROM_PERIOD", lastDateOfMonth);
                    List monthEndBalLst = sqlMap.executeQueryForList("getShareAmtAsonFrmPeriod", empDetMapForMonthly);
                    if (monthEndBalLst != null && monthEndBalLst.size() > 0) {
                        HashMap monthEndBalMap = (HashMap) monthEndBalLst.get(0);
                        double monthEndAMt = CommonUtil.convertObjToDouble(monthEndBalMap.get("BALANCE_AS_ON_START")).doubleValue();
                        dividendAmount += monthEndAMt * dividendPercent / 100;
                    } else {
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
                if (dividendAmount < minimumShareDividend) {
                    dividendAmount = 0.0;
                }
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
        //find the total amounyt as on 10th of the month
        List calculateDividendList = new ArrayList();
        for (int i = 0; i < monthlyShareList.size(); i++) {
            monthlyShareMap = (HashMap) monthlyShareList.get(i);
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
            }
        }
        if (calculateDividendList != null && calculateDividendList.size() > 0) {
            double[] sortList = new double[calculateDividendList.size() + 1];
            sortList[0] = shareDividendAmt;
            //        calculate the monthly minimum AMount
            for (int i = 0; i < calculateDividendList.size(); i++) {
                HashMap monthlyMinMap = (HashMap) calculateDividendList.get(i);
                double noOfShares = CommonUtil.convertObjToDouble(monthlyMinMap.get("SHARE_VALUE")).doubleValue();
                String typeOTrans = CommonUtil.convertObjToStr(monthlyShareMap.get("SHARE_NO_FROM"));
                if (typeOTrans.equals("ADD")) {
                    shareDividendAmt += noOfShares;
                } else {
                    shareDividendAmt -= noOfShares;
                }
                sortList[i + 1] = shareDividendAmt;
            }
            java.util.Arrays.sort(sortList);
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
        Date shareCreatedDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(individualDetailsMap.get("CREATED_DT")));
        Date fromPeriod = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(individualDetailsMap.get("FROM_PERIOD")));
        Date toPeriod = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(individualDetailsMap.get("TO_PERIOD")));
        individualDetailsMap.put("CREATED_DT", shareCreatedDt);
        individualDetailsMap.put("FROM_PERIOD", fromPeriod);
        individualDetailsMap.put("TO_PERIOD", toPeriod);
        individualDetailsMap.put("SHARE_TYPE", objShareDividendCalcTO.getCboShareClass());
        if (fromPeriod.after(shareCreatedDt)) {
            List shareAmtAsOnFrmPeriod = sqlMap.executeQueryForList("getShareAmtAsonFrmPeriod", individualDetailsMap);
            if (shareAmtAsOnFrmPeriod != null && shareAmtAsOnFrmPeriod.size() > 0) {
                HashMap shareAmtAsOnFrmPeriodMap = (HashMap) shareAmtAsOnFrmPeriod.get(0);
                individualDetailsMap.put("BALANCE_AS_ON_START", shareAmtAsOnFrmPeriodMap.get("BALANCE_AS_ON_START"));
            }
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
            executeTransactionPart(objLogDAO, objLogTO, map);
            final String USERID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
            sqlMap.commitTransaction();
        } catch (Exception e) {
           returnMap.put("ERROR_MESSGAE", e.getMessage());
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            System.out.println("e : " + e);
            throw e;
        }
    }

     private void insertDrfData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();
            executeDrfTransactionPart(objLogDAO, objLogTO, map);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
              returnMap.put("ERROR_MESSAGE",e.getMessage());
            throw e;
        }
    }

    private void executeTransactionPart(LogDAO objLogDAO, LogTO objLogTO, HashMap map) throws Exception {
        try {
            ArrayList standingLst = new ArrayList();
            if (map.containsKey("SHARE_PROD_DETAIL_SAVE")) {
                HashMap shareDividendMap = (HashMap) map.get("SHARE_PROD_DETAIL_SAVE");
                standingLst = (ArrayList) shareDividendMap.get("TOTAL_EMPLOYEE_DETAILS");
            }
            if (standingLst != null && standingLst.size() > 0) {
                getdividendIDno();
                boolean SingleTransaction = true;
                String dividendID = CommonUtil.convertObjToStr(objShareDividendCalculationTO.getDividendID());
                String debitGL = CommonUtil.convertObjToStr(objShareDividendCalculationTO.getTxtDebitGl());
                String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
                double totalAmount = CommonUtil.convertObjToDouble(objShareDividendCalculationTO.getTxtTotalAmount()).doubleValue();
                objShareDividendCalculationTO.setBranchCode(BRANCH_ID);
                sqlMap.executeUpdate("insertShareDividendCalcMaster", objShareDividendCalculationTO);
                boolean firstTransID = true;
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                transactionDAO.setInitiatedBranch(_branchCode);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER); 
                TxTransferTO transferTo = new TxTransferTO();
                ArrayList TxTransferTO = new ArrayList();
                HashMap txMap = new HashMap();
                double dividendTransAmt=0.0; 
                String payMode = "";
                HashMap transMap = new HashMap();
                for (int i = 0; i < standingLst.size(); i++) {
                    HashMap dataMap = new HashMap();
                    ArrayList transferList = new ArrayList();
                    TransferTrans transferTrans = new TransferTrans();
                    TransactionTO transactionTO = new TransactionTO();
                    HashMap transactionListMap = new HashMap();
                    double dividendAmount = 0.0;
                    String prodType = "";
                    String prodId = "";
                    String creditAccNo = "";
                    dataMap = (HashMap) standingLst.get(i);
                    dividendAmount = CommonUtil.convertObjToDouble(dataMap.get("DIVIDEND_AMOUNT")).doubleValue();
                    payMode = CommonUtil.convertObjToStr(dataMap.get("DIVIDEND_PAY_MODE"));
                    prodType = CommonUtil.convertObjToStr(dataMap.get("DIVIDEND_CREDIT_PRODUCT"));
                    prodId = CommonUtil.convertObjToStr(dataMap.get("DIVIDEND_CREDIT_PRODUCT_ID"));
                    creditAccNo = CommonUtil.convertObjToStr(dataMap.get("DIVIDEND_CREDIT_AC"));
                    String crdAcc_status = "";
                    if (creditAccNo != null && creditAccNo.length() > 0 && payMode.equalsIgnoreCase("TRANSFER")) {
                        HashMap where = new HashMap();
                        if (prodType.equalsIgnoreCase("OA")) {
                            where.put(CommonConstants.ACT_NUM, creditAccNo);
                            where = (HashMap) sqlMap.executeQueryForObject("getStatusForAccount", where);
                            if (where != null && where.size() > 0 && where.containsKey("ACT_STATUS_ID")) {
                                crdAcc_status = CommonUtil.convertObjToStr(where.get("ACT_STATUS_ID"));
                            }
                        } else if (prodType.equalsIgnoreCase("AD")) {
                            where.put("ACCT_NUM", creditAccNo);
                            where = (HashMap) sqlMap.executeQueryForObject("getStatusForAccountTL", where);
                            if (where != null && where.size() > 0 && where.containsKey("ACCT_STATUS")) {
                                crdAcc_status = CommonUtil.convertObjToStr(where.get("ACCT_STATUS"));
                            }
                        }
                    }
                    if (payMode.equalsIgnoreCase("TRANSFER") && crdAcc_status != null && !crdAcc_status.equalsIgnoreCase("CLOSED")) {
                        txMap = new HashMap();
                        transactionListMap = new HashMap();
                        if (dividendAmount > 0.0) {
                            if (prodType.equals("AD") || prodType.equals("OA")) {
                                dividendTransAmt = dividendTransAmt+dividendAmount;
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                txMap.put(TransferTrans.CR_ACT_NUM, creditAccNo);
                                txMap.put(TransferTrans.CR_PROD_TYPE, prodType);
                                txMap.put(TransferTrans.CR_PROD_ID, prodId);
                                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                txMap.put(TransactionDAOConstants.CREDIT, TransactionDAOConstants.CREDIT);
                                txMap.put("LINK_BATCH_ID", creditAccNo);
                                txMap.put(TransferTrans.PARTICULARS, payMode + "-" + objShareDividendCalculationTO.getDividendID() + "-DIVIDEND");
                                txMap.put("TRANS_MOD_TYPE", prodType);
                                txMap.put("GL_TRANS_ACT_NUM", creditAccNo);
                                txMap.put("DR_INSTRUMENT_2",dividendID);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, dividendAmount);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setTransDt(currDt);
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setInitChannType(branchId);
                                transferTo.setStatusBy(CommonConstants.TTSYSTEM);
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setScreenName("DIVIDEND AND DRF");
                                TxTransferTO.add(transferTo);
                            }
                        }
                    }
                }
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                txMap.put(TransferTrans.DR_AC_HD, debitGL);
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransactionDAOConstants.DEBIT, TransactionDAOConstants.DEBIT);
                txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                txMap.put(TransferTrans.PARTICULARS, payMode + "-" + dividendID);
                txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                txMap.put("DR_INSTRUMENT_2",dividendID);
                transferTo = transactionDAO.addTransferDebitLocal(txMap, dividendTransAmt);
                transferTo.setInitChannType(branchId);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransDt(currDt);
                transferTo.setInitiatedBranch(BRANCH_ID);
                transferTo.setStatusBy(CommonConstants.TTSYSTEM);
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setScreenName("DIVIDEND AND DRF");
                TxTransferTO.add(transferTo);
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
                    returnMap.put("SINGLE_TRANS_ID", transMap.get("SINGLE_TRANS_ID"));
                    returnMap.put("FIRST_TRANS_ID", transMap.get("TRANS_ID"));
                    for (int i = 0; i < standingLst.size(); i++) {
                        HashMap dataMap = new HashMap();
                        ShareDividendCalculationDetailsTO objShareDividendCalculationDetailsTO = new ShareDividendCalculationDetailsTO();
                        dataMap = (HashMap) standingLst.get(i);
                        objShareDividendCalculationDetailsTO.setDividendAmt(CommonUtil.convertObjToStr(dataMap.get("DIVIDEND_AMOUNT")));
                        objShareDividendCalculationDetailsTO.setDividendCalcID(dividendID);
                        String crdAcc_status = "";
                        String creditAccNo = CommonUtil.convertObjToStr(dataMap.get("DIVIDEND_CREDIT_AC"));
                        String prodType = CommonUtil.convertObjToStr(dataMap.get("DIVIDEND_CREDIT_PRODUCT"));
                        if (creditAccNo != null && creditAccNo.length() > 0 && CommonUtil.convertObjToStr(dataMap.get("DIVIDEND_PAY_MODE")).equals("TRANSFER")) {
                            HashMap where = new HashMap();
                            if (prodType.equalsIgnoreCase("OA")) {
                                where.put(CommonConstants.ACT_NUM, creditAccNo);
                                where = (HashMap) sqlMap.executeQueryForObject("getStatusForAccount", where);
                                if (where != null && where.size() > 0 && where.containsKey("ACT_STATUS_ID")) {
                                    crdAcc_status = CommonUtil.convertObjToStr(where.get("ACT_STATUS_ID"));
                                }
                            } else if (prodType.equalsIgnoreCase("AD")) {
                                where.put("ACCT_NUM", creditAccNo);
                                where = (HashMap) sqlMap.executeQueryForObject("getStatusForAccountTL", where);
                                if (where != null && where.size() > 0 && where.containsKey("ACCT_STATUS")) {
                                    crdAcc_status = CommonUtil.convertObjToStr(where.get("ACCT_STATUS"));
                                }
                            }
                        }
                        if (CommonUtil.convertObjToStr(dataMap.get("DIVIDEND_PAY_MODE")).equals("TRANSFER") && crdAcc_status != null && !crdAcc_status.equalsIgnoreCase("CLOSED")) {
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
                        sqlMap.executeUpdate("insertShareDivCalcMasterTO", objShareDividendCalculationDetailsTO);
                    }
                } catch (Exception e) {
                    System.out.println("#$#$ Error :" + e);
                    returnMap.put("ERROR_MESSGAE", e.getMessage());
                    returnMap.put(dividendID, e);
                    sqlMap.rollbackTransaction();
                } 
            }

        } catch (Exception e) {
            returnMap.put("ERROR_MESSGAE", e.getMessage());
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }
     private void executeDrfTransactionPart(LogDAO objLogDAO, LogTO objLogTO, HashMap map) throws Exception {
         try {
             DrfTransactionTO objDrfTransactionTO;
             ArrayList drfLst = new ArrayList();
             HashMap drfTransMap = new HashMap();
             if (map.containsKey("DRF_LOAD_SAVE_MAP")) {
                 drfTransMap = (HashMap) map.get("DRF_LOAD_SAVE_MAP");
                 drfLst = (ArrayList) drfTransMap.get("TOTAL_DRF_DETAILS");
             }
            if (drfLst != null && drfLst.size() > 0) {
                boolean SingleTransaction = true;
                String drfTransID = getDrfTrans_No();
                String payableGL = CommonUtil.convertObjToStr(drfTransMap.get("PAYABLEGL"));
                String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
                String drfProduct = CommonUtil.convertObjToStr(drfTransMap.get("DRF_PRODUCT"));
                double totalDrfAmount = CommonUtil.convertObjToDouble(drfTransMap.get("DRF_TOTAL_AMOUNT"));
                boolean firstTransID = true;
                double totalDrfTransAmount=0.0; 
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                transactionDAO.setInitiatedBranch(_branchCode);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                TxTransferTO transferTo = new TxTransferTO();
                ArrayList TxTransferTO = new ArrayList();
                HashMap txMap = new HashMap();
                double dividendTransAmt = 0.0;
                String payMode = "";
                HashMap transMap = new HashMap();
                for (int i = 0; i < drfLst.size(); i++) {
                    HashMap dataMap = new HashMap();
                    HashMap transactionListMap = new HashMap();
                    double drfAmount = 0.0;
                    String prodType = "";
                    String prodId = "";
                    String debiAccNo = "";
                    dataMap = (HashMap) drfLst.get(i);
                    drfAmount = CommonUtil.convertObjToDouble(dataMap.get("DRF_AMOUNT")).doubleValue();
                    payMode = CommonUtil.convertObjToStr(dataMap.get("DIVIDEND_PAY_MODE"));
                    prodType = CommonUtil.convertObjToStr(dataMap.get("DIVIDEND_CREDIT_PRODUCT"));
                    prodId = CommonUtil.convertObjToStr(dataMap.get("DIVIDEND_CREDIT_PRODUCT_ID"));
                    debiAccNo = CommonUtil.convertObjToStr(dataMap.get("DIVIDEND_CREDIT_AC"));
                    String memberNo = CommonUtil.convertObjToStr(dataMap.get("MEMBERSHIP_NO"));
                    String memberName = CommonUtil.convertObjToStr(dataMap.get("NAME"));
                    String crdAcc_status = "";
                    if (debiAccNo != null && debiAccNo.length() > 0 && payMode.equalsIgnoreCase("TRANSFER")) {
                        HashMap where = new HashMap();
                        if (prodType.equalsIgnoreCase("OA")) {
                            where.put(CommonConstants.ACT_NUM, debiAccNo);
                            where = (HashMap) sqlMap.executeQueryForObject("getStatusForAccount", where);
                            if (where != null && where.size() > 0 && where.containsKey("ACT_STATUS_ID")) {
                                crdAcc_status = CommonUtil.convertObjToStr(where.get("ACT_STATUS_ID"));
                            }
                        } else if (prodType.equalsIgnoreCase("AD")) {
                            where.put("ACCT_NUM", debiAccNo);
                            where = (HashMap) sqlMap.executeQueryForObject("getStatusForAccountTL", where);
                            if (where != null && where.size() > 0 && where.containsKey("ACCT_STATUS")) {
                                crdAcc_status = CommonUtil.convertObjToStr(where.get("ACCT_STATUS"));
                            }
                        }
                    }
                    if (payMode.equalsIgnoreCase("TRANSFER") && crdAcc_status != null && !crdAcc_status.equalsIgnoreCase("CLOSED")) {
                        transferTo = new TxTransferTO();
                        txMap = new HashMap();
                        transactionListMap = new HashMap();
                        transMap = new HashMap();
                        if (drfAmount > 0.0) {
                            if (prodType.equals("AD")) {
                                totalDrfTransAmount = totalDrfTransAmount + drfAmount;
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                txMap.put(TransferTrans.DR_ACT_NUM, debiAccNo);
                                txMap.put(TransferTrans.DR_PROD_TYPE, prodType);
                                txMap.put(TransferTrans.DR_PROD_ID, prodId);
                                txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                                txMap.put(TransactionDAOConstants.DEBIT, TransactionDAOConstants.DEBIT);
                                txMap.put("LINK_BATCH_ID", debiAccNo);
                                txMap.put(TransferTrans.PARTICULARS, payMode + "-" + drfTransID + "-DRFTRANSID");
                                txMap.put("TRANS_MOD_TYPE", "AD");
                                txMap.put("DEBIT_LOAN_TYPE", "DP");
                                txMap.put("AUTHORIZEREMARKS", "DP");
                                txMap.put("GL_TRANS_ACT_NUM", debiAccNo);
                                txMap.put("DR_INSTRUMENT_2",drfTransID);
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, drfAmount);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setTransDt(currDt);
                                transferTo.setInitChannType(BRANCH_ID);
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonConstants.TTSYSTEM);
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setScreenName("DIVIDEND AND DRF");
                                transferTo.setAuthorizeRemarks("DP");
                                TxTransferTO.add(transferTo);
                            }
                        }
                    }
                }
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                txMap.put(TransferTrans.CR_AC_HD, payableGL);
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransactionDAOConstants.CREDIT, TransactionDAOConstants.CREDIT);
                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                txMap.put(TransferTrans.PARTICULARS, payMode + "-" + drfTransID);
                txMap.put("TRANS_MOD_TYPE", "GL");
                txMap.put("DR_INSTRUMENT_2",drfTransID);
                transferTo = transactionDAO.addTransferCreditLocal(txMap, totalDrfTransAmount);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransDt(currDt);
                transferTo.setInitiatedBranch(BRANCH_ID);
                transferTo.setStatusBy(CommonConstants.TTSYSTEM);
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitChannType(BRANCH_ID);
                transactionDAO.setTransType(CommonConstants.DEBIT);
                transactionDAO.setLoanDebitInt("DP");
                transactionDAO.setInitiatedBranch(branchId);
                transferTo.setScreenName("DIVIDEND AND DRF");
                TxTransferTO.add(transferTo);
                HashMap applnMap = new HashMap();
                transferDAO = new TransferDAO();
                HashMap authorizeMap = new HashMap();
                authorizeMap.put("BATCH_ID", null);
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                map.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
                map.put("MODE", map.get("COMMAND"));
                map.put("COMMAND", map.get("MODE"));
                map.put("TxTransferTO", TxTransferTO);
                map.put("DEBIT_LOAN_TYPE","DP");
                try {
                    transMap = transferDAO.execute(map, false);
                    returnMap.put("FIRST_TRANS_ID", transMap.get("TRANS_ID"));
                    returnMap.put("SINGLE_TRANS_ID", transMap.get("SINGLE_TRANS_ID"));
                    for (int i = 0; i < drfLst.size(); i++) {
                        HashMap drfMap = new HashMap();
                        double drfAmount = 0.0;
                        String prodType = "";
                        String prodId = "";
                        String debiAccNo = "";
                        drfMap = (HashMap) drfLst.get(i);
                        drfAmount = CommonUtil.convertObjToDouble(drfMap.get("DRF_AMOUNT")).doubleValue();
                        payMode = CommonUtil.convertObjToStr(drfMap.get("DIVIDEND_PAY_MODE"));
                        prodType = CommonUtil.convertObjToStr(drfMap.get("DIVIDEND_CREDIT_PRODUCT"));
                        prodId = CommonUtil.convertObjToStr(drfMap.get("DIVIDEND_CREDIT_PRODUCT_ID"));
                        debiAccNo = CommonUtil.convertObjToStr(drfMap.get("DIVIDEND_CREDIT_AC"));
                        String memberNo = CommonUtil.convertObjToStr(drfMap.get("MEMBERSHIP_NO"));
                        String memberName = CommonUtil.convertObjToStr(drfMap.get("NAME"));
                        String crdAcc_status = "";
                        if (debiAccNo != null && debiAccNo.length() > 0 && payMode.equalsIgnoreCase("TRANSFER")) {
                            HashMap where = new HashMap();
                            if (prodType.equalsIgnoreCase("AD")) {
                                where.put("ACCT_NUM", debiAccNo);
                                where = (HashMap) sqlMap.executeQueryForObject("getStatusForAccountTL", where);
                                if (where != null && where.size() > 0 && where.containsKey("ACCT_STATUS")) {
                                    crdAcc_status = CommonUtil.convertObjToStr(where.get("ACCT_STATUS"));
                                }
                                if (payMode.equalsIgnoreCase("TRANSFER") && crdAcc_status != null && !crdAcc_status.equalsIgnoreCase("CLOSED")) {
                                    objDrfTransactionTO = new DrfTransactionTO();
                                    objDrfTransactionTO.setTxtDrfTransAmount(CommonUtil.convertObjToDouble(drfAmount));
                                    objDrfTransactionTO.setCboDrfTransProdID(drfProduct);
                                    objDrfTransactionTO.setTxtDrfTransMemberNo(memberNo);
                                    objDrfTransactionTO.setTxtDrfTransName(memberName);
                                    objDrfTransactionTO.setDrfTransID(drfTransID);
                                    objDrfTransactionTO.setRdoDrfTransaction("RECIEPT");
                                    objDrfTransactionTO.setChkDueAmtPayment("N");
                                    objDrfTransactionTO.setStatus(CommonConstants.NEW);
                                    objDrfTransactionTO.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    objDrfTransactionTO.setStatusDate(currDt);
                                    objDrfTransactionTO.setCommand(CommonConstants.NEW);
                                    objDrfTransactionTO.setResolutionNo("1");
                                    objDrfTransactionTO.setResolutionDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(currDt)));
                                    objDrfTransactionTO.setAuthorizeBy(CommonConstants.TTSYSTEM);
                                    objDrfTransactionTO.setAuthorizeDate(currDt);
                                    objDrfTransactionTO.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
                                    sqlMap.executeUpdate("insertDrfTransDetailsTO", objDrfTransactionTO);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("#$#$ Error :" + e);
                   returnMap.put("ERROR_MESSGAE", e.getMessage());
                    returnMap.put(drfTransID, e);
                    sqlMap.rollbackTransaction();
                }
             }

         } catch (Exception e) {
             returnMap.put("ERROR_MESSGAE", e.getMessage());
             sqlMap.rollbackTransaction();
             e.printStackTrace();
             throw e;
         }
    }

//    public void authorizeTransaction(HashMap transMap, HashMap map) throws Exception {
//        try {
//            if (transMap != null && transMap.get("TRANS_ID") != null && !transMap.get("TRANS_ID").equals("")) {
//                System.out.println("TRANSFER TRANS_ID :" + transMap.get("TRANS_ID"));
//                String authorizeStatus = CommonUtil.convertObjToStr(CommonConstants.STATUS_AUTHORIZED);
//                String linkBatchId = CommonUtil.convertObjToStr(transMap.get("TRANS_ID"));
//                HashMap cashAuthMap = new HashMap();
//                cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
//                cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
//                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
//                TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
//                cashAuthMap = null;
//                transMap = null;
//            }
//        } catch (Exception e) {
//            sqlMap.rollbackTransaction();
//            e.printStackTrace();
//            throw e;
//        }
//    }
    private String getDrfTrans_No() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "DRF_TRANSACTION");
        String drfTransID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return drfTransID;
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
