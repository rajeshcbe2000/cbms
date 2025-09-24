/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SalaryStructureDAO.java
 *
 * Created on Wed May 26 10:59:57 GMT+05:30 2004
 */
package com.see.truetransact.serverside.share;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.share.ShareDividendCalculationTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;
import com.see.truetransact.transferobject.investments.InvestmentsTransTO;
import com.see.truetransact.serverside.investments.InvestmentsTransDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
//import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import java.sql.*;
import oracle.sql.*;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Date;
import java.util.Map;
import java.text.*;

/**
 * ShareDividendCalculation DAO.
 *
 * @author Nikhil
 *
 */
public class ShareDividendPaymentDAO extends TTDAO {

    private String branchId;
    private SqlMap sqlMap;
    private HashMap data;
    private HashMap shareProductDetails;
    private Iterator addressIterator;
    private TransactionDAO transactionDAO = null;
    private String command;
//    private String _userId = "";
//    private HashMap drfMasterMap;
//    private HashMap deletedDrfMasterMap;
//    private String key;
//    private LogDAO logDAO;
//    private LogTO logTO;
    private final static Logger log = Logger.getLogger(ShareDividendCalculationDAO.class);
//    private String addressKey = new String();
//    HashMap resultMap = new HashMap();
    Date currDt = null;
    private String invTransBatchID = "";
//    private String whereCondition;
//    private HashMap whereConditions;
//    private Connection conn;
//    private Statement stmt;
//    private ResultSet rset;
//    private String cmd;
//    private String dataBaseURL;
//    private String userName;
//    private String passWord;
//    private String SERVER_ADDRESS;
//    private String tableName;
//    private String tableCondition;
//    private int isMore = -1;
//    private String addCondition;
    final String SCREEN = "CUS";
    private final String YES = "Y";
    private List list;
    private TransactionTO objTransactionTO;
    private Map returnMap;
    TransferDAO transferDAO = new TransferDAO();
    private String sharePaymentID = "";
    private String BRANCH_CODE = "";
    private int ibrHierarchy = 0;

    /**
     * Creates a new instance of DeductionDAO
     */
    public ShareDividendPaymentDAO() throws ServiceLocatorException {
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
    }

    private void getAllTOs() throws Exception {
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
        String where = (String) obj.get("MEMBER_NO");
        HashMap getRemitTransMap = new HashMap();
        getRemitTransMap.put("TRANS_ID", obj.get("PAYMENT_ID"));
        getRemitTransMap.put("TRANS_DT", currDt.clone());
        getRemitTransMap.put("BRANCH_CODE", _branchCode);
        System.out.println("@#%$#@%#$%getRemitTransMap:" + getRemitTransMap);
        List list = (List) sqlMap.executeQueryForList("getSelectRemitIssueTransFromTransID", getRemitTransMap);
//        List list = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", getRemitTransMap);
        if (list != null && list.size() > 0) {
            transMap.put("TRANSACTION_LIST", list);
        }
        List getShareAuth = (List) sqlMap.executeQueryForList("getShareDetailsForDivPaymentAuthMode", obj);
        if (getShareAuth != null && getShareAuth.size() > 0) {
            transMap.put("SHARE_DETAILS_LIST", getShareAuth);
        }

        return transMap;
    }

    private void makeQueryNull() {
        list = null;
    }

    public HashMap execute(HashMap map) throws Exception {
        //CommonUtil.serializeObjWrite("D:\\share.txt", map);
        System.out.println("#### inside shareDAO execute() map : " + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        LogDAO objLogDAO = new LogDAO();
        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        currDt = ServerUtil.getCurrentDate(_branchCode);
        branchId = objLogTO.getBranchId();
        System.out.println("#### branchId : " + branchId);
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));
        shareProductDetails = new HashMap();
        returnMap = new HashMap();
        ibrHierarchy = 1;
        if (map.containsKey("FINAL_LIST")) {
            command = CommonUtil.convertObjToStr(map.get("COMMAND"));
            System.out.println("@#$@#$#@command:" + command);
            //--- Selects the method according to the Command type
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                if (map.containsKey("FINAL_LIST")) {
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
            System.out.println("map:" + map);
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            authMap.put(CommonConstants.BRANCH_ID, (String) map.get(CommonConstants.BRANCH_ID));
            authMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
            System.out.println("authMap:" + authMap);
            System.out.println("%%%%%%%%%% authMap:" + authMap);
            System.out.println("%%%%%%%%%% map:" + map);
            if (authMap != null) {
                authorize(authMap, map);
            }
        }
        return (HashMap) returnMap;
    }

    private void authorize(HashMap map, HashMap shareMap) throws Exception {
        String status = (String) map.get(CommonConstants.AUTHORIZESTATUS);
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        System.out.println("selectedList#########"+selectedList);
        HashMap dataMap;
        String shareDivTransID = null;
        String linkBatchId = null;
        String singleTransId = null;
        HashMap cashAuthMap;
        try {
             if(!shareMap.containsKey("SHARE_DIVIDEND_TRANSFER")){
                sqlMap.startTransaction();
             }
             System.out.println("selectedList#########size"+selectedList.size());
            for (int i = 0; i < selectedList.size(); i++) {
                dataMap = (HashMap) selectedList.get(i);
                System.out.println("dataMap:" + dataMap);
                linkBatchId = CommonUtil.convertObjToStr(dataMap.get("PAYMENT_ID"));//Transaction Batch Id
                singleTransId = CommonUtil.convertObjToStr(dataMap.get("SINGLE_TRANS_ID"));
                if(shareMap.containsKey("SHARE_DIVIDEND_TRANSFER")){
                   shareDivTransID = CommonUtil.convertObjToStr(shareMap.get("MEMBER_NO"));
                   dataMap.put("MEMBER_NO", shareDivTransID); 
                }else{
                   shareDivTransID = CommonUtil.convertObjToStr(dataMap.get("MEMBER_NO"));
                   dataMap.put("MEMBER_NO", shareDivTransID);  
                }                
                dataMap.put(CommonConstants.STATUS, status);
                dataMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                dataMap.put("CURR_DATE", currDt);
                System.out.println("status------------>" + status);
                if (status.equals("REJECTED")) {
                    dataMap.put(CommonConstants.STATUS, "");
                    dataMap.put("PAID_STATUS", "UNPAID");
                    dataMap.put("PAYMENT_BRANCH_ID", "");
                    dataMap.put("PAYMENT_ID",null);    
                } else {
                    dataMap.put("PAID_STATUS", "PAID");
                    dataMap.put("PAYMENT_BRANCH_ID", _branchCode);                    
                }
                sqlMap.executeUpdate("authorizeShareDividendDetails", dataMap);
                if (shareMap.containsKey("TransactionTO")) {
                    HashMap transactionDetailsMap = (LinkedHashMap) shareMap.get("TransactionTO");
                    TransactionTO transactionTO = new TransactionTO();
                    LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                    if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                        allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                    }
                    transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                    if (!CommonUtil.convertObjToStr(transactionTO.getProductType()).equals("") && transactionTO.getProductType().equals("INV")) {
                        double dividendAmount = 0.0;
                        HashMap whereMap = new HashMap();
                        dividendAmount = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                        System.out.println("####Investment Transaction");
                        //Authorization
                        whereMap.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                        whereMap.put("BATCH_ID", CommonUtil.convertObjToStr(transactionTO.getBatchId()));
                        shareMap.put("FROM_INTEREST_TASK", "FROM_INTEREST_TASK");
                        ArrayList arrList = new ArrayList();
                        HashMap authDataMap = new HashMap();
                        HashMap singleAuthorizeMap = new HashMap();
                        authDataMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(shareMap.get("USER_ID")));
                        authDataMap.put("BATCH_ID", CommonUtil.convertObjToStr(transactionTO.getBatchId()));
                        arrList.add(authDataMap);
                        singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, status);
                        singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                        singleAuthorizeMap.put("InvestmentsTransTO", getInvestmentsTransTO(status, dividendAmount, whereMap, map));
                        singleAuthorizeMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(shareMap.get("USER_ID")));
                        shareMap.put(CommonConstants.AUTHORIZEMAP, singleAuthorizeMap);
                        InvestmentsTransDAO investmentDAO = new InvestmentsTransDAO();
                        whereMap = investmentDAO.execute(shareMap);
                    } else {
                        cashAuthMap = new HashMap();
                        cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                        cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonUtil.convertObjToStr(map.get("USER_ID"))));
                        cashAuthMap.put("DAILY", "DAILY");
                        cashAuthMap.put("SINGLE_TRANS_ID", singleTransId);
                        cashAuthMap.put("INTERBRANCH_CREATION_SCREEN", "INTERBRANCH_CREATION_SCREEN");
                        //cashAuthMap.put("SHARE_PAYMENT_DAO", "SHARE_PAYMENT_DAO");
                        System.out.println("cashAuthMap:" + cashAuthMap);
                        TransactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
                    }
                    HashMap transMap = new HashMap();
                    transMap.put("LINK_BATCH_ID", linkBatchId);//dIVIDENT ID SAVED INTO InstrumentNO1
                //    sqlMap.executeUpdate("updateInstrumentNO1Transfer", transMap);
                 //   sqlMap.executeUpdate("updateInstrumentNO1Cash", transMap);
                    transMap = null;
                }
            }
            selectedList = null;
            dataMap = null;
            if(!shareMap.containsKey("SHARE_DIVIDEND_TRANSFER")){
                sqlMap.commitTransaction();
             }            
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }

    }

    public CashTransactionTO setCashTransaction(HashMap cashMap, String generateSingleCashId,String brId) {
        log.info("In setCashTransaction()");
        Date curDate = (Date) currDt.clone();
        final CashTransactionTO objCashTransactionTO = new CashTransactionTO();
        try {
            objCashTransactionTO.setAcHdId(CommonUtil.convertObjToStr(cashMap.get(CommonConstants.AC_HD_ID)));
            objCashTransactionTO.setProdType(TransactionFactory.GL);
            objCashTransactionTO.setInpAmount(CommonUtil.convertObjToDouble(cashMap.get("AMOUNT")));
            objCashTransactionTO.setAmount(CommonUtil.convertObjToDouble(cashMap.get("AMOUNT")));
            objCashTransactionTO.setTransType(CommonUtil.convertObjToStr(cashMap.get("TRANS_TYPE")));
           // objCashTransactionTO.setBranchId(_branchCode);//bbb11
            objCashTransactionTO.setBranchId(brId);
            objCashTransactionTO.setStatusBy(CommonUtil.convertObjToStr(cashMap.get(CommonConstants.USER_ID)));
            objCashTransactionTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            objCashTransactionTO.setInitTransId(CommonUtil.convertObjToStr(cashMap.get(CommonConstants.USER_ID)));
            objCashTransactionTO.setInitChannType("CASHIER");
            objCashTransactionTO.setParticulars("By " + CommonUtil.convertObjToStr(cashMap.get("LINK_BATCH_ID")));
            objCashTransactionTO.setInitiatedBranch(_branchCode);
            objCashTransactionTO.setLinkBatchId(CommonUtil.convertObjToStr(cashMap.get("LINK_BATCH_ID")));
            objCashTransactionTO.setAuthorizeRemarks(CommonUtil.convertObjToStr(cashMap.get("AUTHORIZEREMARKS")));
            objCashTransactionTO.setLoanHierarchy(CommonUtil.convertObjToStr(cashMap.get("HIERARCHY_VALUE")));
           // objCashTransactionTO.setIbrHierarchy(CommonUtil.convertObjToStr(cashMap.get("HIERARCHY_VALUE")));
            objCashTransactionTO.setIbrHierarchy(CommonUtil.convertObjToStr(ibrHierarchy++));
            objCashTransactionTO.setSingleTransId(generateSingleCashId);
            objCashTransactionTO.setTransModType(CommonUtil.convertObjToStr(cashMap.get("TRANS_MOD_TYPE")));
            objCashTransactionTO.setCommand(CommonConstants.TOSTATUS_INSERT);
            objCashTransactionTO.setInstrumentNo1(CommonUtil.convertObjToStr(cashMap.get("INSTRUMENT_1")));
            if(cashMap.containsKey("INSTRUMENT_2") && cashMap.get("INSTRUMENT_2") != null){ // Added by nithya on 19-03-2021 for KD-2750
               objCashTransactionTO.setInstrumentNo2(CommonUtil.convertObjToStr(cashMap.get("INSTRUMENT_2"))); 
            }
            if (cashMap.containsKey(CommonConstants.SCREEN) && cashMap.get(CommonConstants.SCREEN) != null) {
                objCashTransactionTO.setScreenName(CommonUtil.convertObjToStr(cashMap.get("SCREEN")));
            }
            System.out.println("objCashTransactionTO:" + objCashTransactionTO);
        } catch (Exception e) {
            log.info("Error In setInwardClearing()");
            e.printStackTrace();
        }
        return objCashTransactionTO;
    }

    private HashMap calculateDividendAmount(HashMap individualDetailsMap, ShareDividendCalculationTO objShareDividendCalcTO) throws Exception {
        System.out.println("@#!$@#$@$shareProductDetails:" + shareProductDetails);
        System.out.println("@#!$@#$@individualDetailsMap:" + individualDetailsMap);
        Date fromPeriod = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objShareDividendCalcTO.getTdtFromPeriod()));
        Date startDt = null;
        Date nextDate = null;
        Date toPeriod = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objShareDividendCalcTO.getTdtToPeriod()));
        double dividendPercent = CommonUtil.convertObjToDouble(objShareDividendCalcTO.getTxtDividendPercent()).doubleValue();
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
                        System.out.println("@#$@#$shareAmt:" + shareAmt + " ustomerSlabsBtwPeriodDailyMap :" + customerSlabsBtwPeriodDailyMap);
                        nextDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(customerSlabsBtwPeriodDailyMap.get("STATUS_DT")));
                        int noOfDays = (int) DateUtil.dateDiff(startDt, nextDate);
                        startDt = nextDate;
                        dividendAmount = dividendAmount + (shareAmt * dividendPercent * noOfDays) / 36500;
                        System.out.println("@#$@#$@$#noOfDays 1:" + noOfDays + " : dividendAmount" + dividendAmount);
                        if (CommonUtil.convertObjToStr(customerSlabsBtwPeriodDailyMap.get("SHARE_NO_FROM")).equals("ADD")) {
                            //                            if share addition
                            System.out.println("@#$@#$@#$in add");
                            shareAmt = shareAmt + CommonUtil.convertObjToDouble(customerSlabsBtwPeriodDailyMap.get("SHARE_VALUE")).doubleValue();

                            if (i == customerSlabsBtwPeriodDaily.size() - 1) {
                                noOfDays = (int) DateUtil.dateDiff(startDt, toPeriod);
                                dividendAmount += shareAmt * dividendPercent * noOfDays / 36500;
                                System.out.println("@#$@#$@$#noOfDays 2:" + noOfDays + " : dividendAmount" + dividendAmount);
                            }
                        } else if (CommonUtil.convertObjToStr(customerSlabsBtwPeriodDailyMap.get("SHARE_NO_FROM")).equals("WITHDRAWAL")) {
                            shareAmt = shareAmt - CommonUtil.convertObjToDouble(customerSlabsBtwPeriodDailyMap.get("SHARE_VALUE")).doubleValue();
                            if (i == customerSlabsBtwPeriodDaily.size() - 1) {
                                noOfDays = (int) DateUtil.dateDiff(startDt, toPeriod);
                                dividendAmount += shareAmt * dividendPercent * noOfDays / 36500;
                                System.out.println("@#$@#$@$#noOfDays 3:" + noOfDays + " : dividendAmount" + dividendAmount);
                            }
                            System.out.println("@#$@#$@#$in withdrawal");
                        }
                    }
                    System.out.println("@#$@#$dividendAmount final:" + dividendAmount);
                } else {
                    int noOfDays = (int) DateUtil.dateDiff(fromPeriod, toPeriod);
                    dividendAmount = noOfDays * shareAmt * dividendPercent / 36500;
                    System.out.println("#$@$@#$dividendAmount:" + dividendAmount);
                    System.out.println("@#$@#$@$#noOfDays 4:" + noOfDays + " : dividendAmount" + dividendAmount);
                }
                if (shareProductDetails.containsKey("DIVIDEND_ROUND_OFF")) {
                    if (CommonUtil.convertObjToStr(shareProductDetails.get("DIVIDEND_ROUND_OFF")).equals("NO_ROUND")) {
                        DecimalFormat df = new DecimalFormat("#.##");
                        dividendAmount = CommonUtil.convertObjToDouble(df.format(dividendAmount)).doubleValue();
                        System.out.println("@#$@#$@#df.format(dividendAmount)" + df.format(dividendAmount));
                        System.out.println("@#$@#$@#df.only account(dividendAmount)" + dividendAmount);
                    } else if (CommonUtil.convertObjToStr(shareProductDetails.get("DIVIDEND_ROUND_OFF")).equals("NEAREST_VAL")) {
                        dividendAmount = (double) getNearest((long) (dividendAmount * 100), 100) / 100;
                    }
                }
                individualDetailsMap.put("DIVIDEND_AMOUNT", String.valueOf(dividendAmount));
            } else if (calcType.equals("MONTHLY_MIN_BAL")) {
            } else if (calcType.equals("MONTH_END_BAL")) {
            } else if (calcType.equals("YR_END_BAL")) {
            }
        }
        return individualDetailsMap;
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
        System.out.println("#@$@#$@shareCreatedDt:" + shareCreatedDt + " :fromPeriod " + fromPeriod + " :toPeriod " + toPeriod);
        individualDetailsMap.put("CREATED_DT", shareCreatedDt);
        individualDetailsMap.put("FROM_PERIOD", fromPeriod);
        individualDetailsMap.put("TO_PERIOD", toPeriod);
        individualDetailsMap.put("SHARE_TYPE", objShareDividendCalcTO.getCboShareClass());
        if (fromPeriod.after(shareCreatedDt)) {
//            have to find the balance on that particular from Period
            System.out.println("$#%#$%#$%individualDetailsMap before execution:" + individualDetailsMap);
//            gets the details of the balance before the FromPeriod.
            List shareAmtAsOnFrmPeriod = sqlMap.executeQueryForList("getShareAmtAsonFrmPeriod", individualDetailsMap);
            HashMap shareAmtAsOnFrmPeriodMap = (HashMap) shareAmtAsOnFrmPeriod.get(0);
            individualDetailsMap.put("BALANCE_AS_ON_START", shareAmtAsOnFrmPeriodMap.get("BALANCE_AS_ON_START"));
            System.out.println("@#$shareAmtAsOnFrmPeriodMap:" + individualDetailsMap);
        }
        return individualDetailsMap;
    }

    private String getSharePaymentId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "SHARE_PAYMNET_ID");
        String sharePaymentId = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return sharePaymentId;
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
        
    private void insertShareDividendPaymentTransDetails(HashMap map) throws Exception, Exception {
        try {
            //System.out.println("map --------------- ="+map);
            //System.out.println("BRancjh code 1--------------- ="+_branchCode +" ber22--"+CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
            if (command != null && command.length() > 0) {
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    TransactionTO transactionTO = new TransactionTO();
                    List finalList = null;
                    if (map.containsKey("FINAL_LIST")) {
                        finalList = (List) map.get("FINAL_LIST");
                    }
                    HashMap txMap = new HashMap();
                    HashMap transMap = new HashMap();
                    HashMap debitMap = new HashMap();
                    String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
                    BRANCH_CODE = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
                    String paymentBranch = "";
                    if (map.containsKey("SHARE_PAYMENT_BRANCH")) {
                        paymentBranch = CommonUtil.convertObjToStr(map.get("SHARE_PAYMENT_BRANCH"));
                    }
                    //System.out.println("paymentBranch ==="+paymentBranch);
                    String generateSingleTransId = generateLinkID();
                    String generateSingleCashId = generateLinkID();
                    if (map.containsKey("TransactionTO")) {
                        sharePaymentID = "";
                        sharePaymentID = getSharePaymentId();
                        System.out.println("##### sharePaymentID" + sharePaymentID);
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
                        if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductType().equals("INV")) {
                            debitMap.put("prodId", transactionTO.getProductId());
                            List lst = sqlMap.executeQueryForList("TermLoan.getProdHead", debitMap);
                            if (lst != null && lst.size() > 0) {
                                debitMap = (HashMap) lst.get(0);
                            }
                        }
                        //added by sreekrishnan 
                        if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && transactionTO.getProductType().equals("TL")) {
                            debitMap.put("prodId", transactionTO.getProductId());
                            List lst = sqlMap.executeQueryForList("TermLoan.getProdHead", debitMap);
                            if (lst != null && lst.size() > 0) {
                                debitMap = (HashMap) lst.get(0);
                            }
                        }
                        if (transactionTO.getTransType().equals("TRANSFER")) {
                            HashMap dataMap = new HashMap();
                            double dividenAmount1 = 0.0;
                            TxTransferTO transferTo = new TxTransferTO();
                            ArrayList TxTransferTO = new ArrayList();
                            TransferTrans transferTrans = new TransferTrans();
                            String memberNo = "";
                            for (int i = 0; i < finalList.size(); i++) {
                                String divPaymentID = "";
                                invTransBatchID = "";
                                transferTrans.setInitiatedBranch(BRANCH_ID);
                                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                                transactionDAO.setInitiatedBranch(_branchCode);
                                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                HashMap tansactionMap = new HashMap();
                                //HashMap dataMap = new HashMap();
                                double dividenAmount = 0.0;
                                double interestAmt = 0.0;
                                double penalAmt = 0.0;
                                String payableGL = "";
                                dataMap = (HashMap) finalList.get(i);
                                divPaymentID = CommonUtil.convertObjToStr(dataMap.get("DIVIDEND_CALC_ID"));
                                dividenAmount = CommonUtil.convertObjToDouble(dataMap.get("DIV_AMOUNT")).doubleValue();
                                dividenAmount1 = dividenAmount1 + dividenAmount;
                                memberNo = CommonUtil.convertObjToStr(dataMap.get("MEMBER_NO"));
                                payableGL = CommonUtil.convertObjToStr(dataMap.get("PAYABLE_GL"));
                                //transferTo.setInstrumentNo2("APPL_GL_TRANS"); // Commented by nithya on 19-03-2020 for KD-2750
                               // transferTo.setInstrumentNo1(divPaymentID);
                                transferTo.setSingleTransId(generateSingleTransId);
                                //Added By Suresh
                                if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("INV") && dividenAmount > 0) {
                                    System.out.println("####Investment Transaction");
                                    HashMap invDataMap = new HashMap();
                                    LinkedHashMap invTransMap = new LinkedHashMap();
                                    LinkedHashMap notDelMap = new LinkedHashMap();
                                    invDataMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    invDataMap.put(CommonConstants.BRANCH_ID, BRANCH_ID);
                                    dataMap.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                                    invDataMap.put("InvestmentsTransTO", getInvestmentsTransTO(CommonConstants.TOSTATUS_INSERT, dividenAmount, dataMap, map));
                                    TransactionTO transfer = new TransactionTO();
                                    transfer.setTransType("TRANSFER");
                                    transfer.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    transfer.setTransAmt(new Double(dividenAmount));
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
                                    System.out.println("####### TransMap : " + transDetMap);
                                    invTransBatchID = CommonUtil.convertObjToStr(transDetMap.get("BATCH_ID"));
                                    transactionTO.setBatchId(invTransBatchID);
                                } else {
                                    txMap = new HashMap();
                                    if (dividenAmount > 0.0) {
                                        transferTo = new TxTransferTO();
                                        txMap = new HashMap();
                                        //transferTo.setInstrumentNo2("APPL_GL_TRANS"); // Commented by nithya 0n 19-03-2021 for KD-2750
                                        transferTo.setInstrumentNo1(divPaymentID);
                                        txMap.put(TransferTrans.DR_AC_HD, payableGL);
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                                        if (dataMap.containsKey("HIERARCHY_VALUE")) {
                                            txMap.put("HIERARCHY", dataMap.get("HIERARCHY_VALUE"));
                                        }
                                        txMap.put(TransferTrans.PARTICULARS, divPaymentID + "-" + ":DIVIDEND_PAYMENT");
                                        txMap.put(TransferTrans.DR_INSTRUMENT_1, divPaymentID);
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, memberNo); // Added by nithya 0n 19-03-2021 for KD-2750
                                        System.out.println("txMap  : " + txMap + "dividenAmount :" + dividenAmount);
                                        txMap.put("TRANS_MOD_TYPE", "SH");
                                        txMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                        transferTo = transactionDAO.addTransferDebitLocal(txMap, dividenAmount);
                                        transferTo.setTransId("-");
                                        transferTo.setBatchId("-");
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferTo.setTransDt(currDt);
                                        transferTo.setInitiatedBranch(BRANCH_ID);
                                        transferTo.setBranchId(paymentBranch);//transactionTO.getBranchId());
                                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        transferTo.setLinkBatchId(sharePaymentID);
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setInstrumentNo2(memberNo);// Added by nithya 0n 19-03-2021 for KD-2750
                                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(sharePaymentID));
                                        transactionTO.setChequeNo("SERVICE_TAX");
                                        TxTransferTO.add(transferTo);
                                    }
//                                    transferDAO = new TransferDAO();
//                                    tansactionMap.put("TxTransferTO", TxTransferTO);
//                                    tansactionMap.put("MODE", map.get("COMMAND"));
//                                    tansactionMap.put("COMMAND", map.get("COMMAND"));
//                                    tansactionMap.put("LINK_BATCH_ID", sharePaymentID);
//                                    tansactionMap.put(CommonConstants.BRANCH_ID, BRANCH_ID);
//                                    System.out.println("################ tansactionMap Debit :" + tansactionMap);
//                                    transMap = transferDAO.execute(tansactionMap, false);
//                                    transactionTO.setBatchId(sharePaymentID);
                                }
                            }
                            //adde for loan transaction
                            //TransferTrans transfer = new TransferTrans();
                            //TxTransferTO transferTo1 = new TxTransferTO();
                            //ArrayList TxTransferTO1 = new ArrayList();
                            if (dividenAmount1 > 0) {           // CREDIT DIVIDEND AMOUNT TO THE ACCOUNT NO
                                txMap = new HashMap();
                                System.out.println("Transfer Started debit : ");
                                //transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                transferTo.setInstrumentNo2(memberNo); // Added by nithya 0n 19-03-2021 for KD-2750
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                System.out.println("$#$$$#$#$# creditMap " + debitMap);
                                if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL") && transactionTO.getProductId().equals("")) {
                                    System.out.println("$#$$$#$#$# Prod Type GL " + transactionTO.getDebitAcctNo());
                                    txMap.put(TransferTrans.CR_AC_HD, (String) transactionTO.getDebitAcctNo());
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, linkBatchId + "-" + ":DIVIDEND_PAYMENT");
                                    if (dataMap.containsKey("HIERARCHY_VALUE")) {
                                        txMap.put("HIERARCHY", dataMap.get("HIERARCHY_VALUE"));
                                    }
                                } else if (!transactionTO.getProductType().equals("")) {
                                    if (transactionTO.getProductType().equals("TL")) {
                                        HashMap loanMap = new HashMap();
                                        HashMap where = new HashMap();
                                        where.put("PROD_ID", CommonUtil.convertObjToStr(transactionTO.getProductId()));
                                        loanMap = interestCalculationTLAD(CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()),
                                                CommonUtil.convertObjToStr(transactionTO.getProductId()));
                                        List waiveList = sqlMap.executeQueryForList("getwaiveoffDetails", where);
                                        if (waiveList != null && waiveList.size() > 0) {
                                            HashMap waiveMap = (HashMap) waiveList.get(0);
                                            loanMap.put("PENAL_WAIVE_OFF", waiveMap.get("PENAL_WAIVER"));
                                        }
                                        transferTrans.setAllAmountMap(loanMap);
                                        System.out.println("@@#@#@#@#loanMap" + loanMap);
                                        System.out.println("@@#@#@#@#trans" + transferTrans);
                                    }
                                    System.out.println("$#$$$#$#$# Prod Type Not GL " + transactionTO.getDebitAcctNo());
                                    txMap.put(TransferTrans.CR_AC_HD, (String) debitMap.get("AC_HEAD"));
                                    txMap.put(TransferTrans.CR_ACT_NUM, transactionTO.getDebitAcctNo());
                                    txMap.put(TransferTrans.CR_PROD_ID, transactionTO.getProductId());
                                    txMap.put(TransferTrans.CR_PROD_TYPE, transactionTO.getProductType());
                                    txMap.put(TransferTrans.DR_INSTRUMENT_2, memberNo); // Added by nithya 0n 19-03-2021 for KD-2750
                                    if (dataMap.containsKey("HIERARCHY_VALUE")) {
                                        txMap.put("HIERARCHY", dataMap.get("HIERARCHY_VALUE"));
                                    }
                                    txMap.put(TransferTrans.PARTICULARS, linkBatchId + "-" + ":DIVIDEND_PAYMENT");
                                }
                                if (transactionTO.getProductType().equals("OA")) {
                                    txMap.put("TRANS_MOD_TYPE", "OA");
                                } else if (transactionTO.getProductType().equals("AB")) {
                                    txMap.put("TRANS_MOD_TYPE", "AB");
                                } else if (transactionTO.getProductType().equals("SA")) {
                                    txMap.put("TRANS_MOD_TYPE", "SA");
                                } else if (transactionTO.getProductType().equals("TL")) {
                                    txMap.put("TRANS_MOD_TYPE", "TL");
                                } else if (transactionTO.getProductType().equals("AD")) {
                                    txMap.put("TRANS_MOD_TYPE", "AD");
                                } else {
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                }
                                txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                                System.out.println("txMap Debit : " + txMap + "dividenAmount :" + dividenAmount1);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, dividenAmount1);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setTransDt(currDt);
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                //transferTo.setBranchId(BRANCH_ID);
                                transferTo.setBranchId(transactionTO.getBranchId());
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setAuthorizeRemarks("DP");
                                transferTo.setSingleTransId(generateSingleTransId);
                                transactionDAO.setLinkBatchID(sharePaymentID);
                                TxTransferTO.add(transferTo);

                            }
                            transferDAO = new TransferDAO();
                            HashMap tansactionMap = new HashMap();
                            tansactionMap.put("TxTransferTO", TxTransferTO);
                            tansactionMap.put("SCREEN", (String) map.get(CommonConstants.SCREEN));
                            tansactionMap.put("MODE", map.get("COMMAND"));
                            tansactionMap.put("COMMAND", map.get("COMMAND"));
                            tansactionMap.put("LINK_BATCH_ID", sharePaymentID);
                            tansactionMap.put(CommonConstants.BRANCH_ID, BRANCH_ID);
                            tansactionMap.put("ALL_AMOUNT", transferTrans.getAllAmountMap());
                            System.out.println("################ tansactionMap Credit :" + tansactionMap);
                            transMap = transferDAO.execute(tansactionMap, false);
                            transactionTO.setBatchId(sharePaymentID);

                            transactionTO.setTransId(sharePaymentID);
                            transactionTO.setBatchDt(currDt);
                            transactionTO.setStatus(CommonConstants.STATUS_CREATED);
                            transactionTO.setBranchId(BRANCH_ID);
                            System.out.println("transactionTO------------------->" + transactionTO);
                            sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", transactionTO);
                        } else {
                            double transAmt;
                            TransactionTO transTO = new TransactionTO();
                            ArrayList cashList = new ArrayList();
                            if (map.containsKey("FINAL_LIST")) {
                                finalList = (List) map.get("FINAL_LIST");
                            }
                            if (finalList != null && finalList.size() > 0) {
                                if (CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue() > 0) {
                                    for (int i = 0; i < finalList.size(); i++) {
                                        HashMap finalListMap = (HashMap) finalList.get(i);
                                        System.out.println("@#%$#$%#$%finalListMap:" + finalListMap);
                                        String dividendAmt = "";
                                        dividendAmt = CommonUtil.convertObjToStr(finalListMap.get(""));
                                        String divPaymentID = CommonUtil.convertObjToStr(finalListMap.get("DIVIDEND_CALC_ID"));
//                                        changed by nikhil
                                        double dividenAmount = CommonUtil.convertObjToDouble((CommonUtil.convertObjToStr(finalListMap.get("DIV_AMOUNT"))).replaceAll(",", "")).doubleValue();
                                        System.out.println("@#$@#$@#$@#dividenAmount" + dividenAmount);
                                        String memberNo = CommonUtil.convertObjToStr(finalListMap.get("MEMBER_NO"));
                                        String payableGL = CommonUtil.convertObjToStr(finalListMap.get("PAYABLE_GL"));
                                        System.out.println("line no 465^^^^^^^");
                                        txMap = new HashMap();
                                        //txMap.put(CommonConstants.BRANCH_ID, _branchCode);
                                        txMap.put(CommonConstants.BRANCH_ID, paymentBranch);
                                        txMap.put(CommonConstants.PRODUCT_ID, TransactionFactory.GL);
                                        txMap.put(CommonConstants.USER_ID,map.get("USER_ID"));
                                        txMap.put("TRANS_TYPE", CommonConstants.DEBIT);
                                        txMap.put("LINK_BATCH_ID", sharePaymentID);
                                        txMap.put("HIERARCHY_VALUE", finalListMap.get("HIERARCHY_VALUE"));
                                        txMap.put("INSTRUMENT_1", divPaymentID);
                                        txMap.put("INSTRUMENT_2", memberNo); // Added by nithya on 19-03-2021 for KD-2750
                                        txMap.put(CommonConstants.SCREEN, CommonUtil.convertObjToStr(map.get("SCREEN")));
                                        if (dividenAmount > 0.0) {
                                            txMap.put(CommonConstants.AC_HD_ID, payableGL);
                                            txMap.put("AUTHORIZEREMARKS", "LIABILITY_HEAD");
                                            txMap.put("AMOUNT", new Double(dividenAmount));
                                            txMap.put("TRANS_MOD_TYPE", "SH");
                                            cashList.add(setCashTransaction(txMap, generateSingleCashId, paymentBranch));
                                            System.out.println("@#$@#$@#$txMap:" + txMap);
                                            System.out.println("@#$@#$@#dividenAmount:" + dividenAmount);
                                        }
                                        transactionTO.setBatchId(sharePaymentID);
                                        transactionTO.setBatchDt(currDt);
                                        transactionTO.setTransId(sharePaymentID);
                                    }
                                    System.out.println("cashList---------------->" + cashList);
                                    HashMap tranMap = new HashMap();
                                    tranMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                                    tranMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    tranMap.put(CommonConstants.IP_ADDR, CommonUtil.convertObjToStr(map.get("IP_ADDR")));
                                    tranMap.put(CommonConstants.MODULE, CommonUtil.convertObjToStr(map.get("MODULE")));
                                    tranMap.put(CommonConstants.SCREEN, CommonUtil.convertObjToStr(map.get("SCREEN")));
                                    tranMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                                    tranMap.put("DAILYDEPOSITTRANSTO", cashList);

                                    CashTransactionDAO cashDao;
                                    cashDao = new CashTransactionDAO();
                                    tranMap = cashDao.execute(tranMap, false);
                                    cashDao = null;
                                    tranMap = null;
                                    transactionTO.setStatus(CommonConstants.STATUS_CREATED);
                                    transactionTO.setBranchId(BRANCH_ID);//bbb11
                                    // transactionTO.setBranchId(_branchCode);
                                    System.out.println("transactionTO------------------->" + transactionTO);
                                    sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", transactionTO);
                                }
                            }
                        }
                        for (int k = 0; k < finalList.size(); k++) {
                            HashMap dataMap = new HashMap();
                            dataMap = (HashMap) finalList.get(k);
                            dataMap.put("PAYMENT_ID", sharePaymentID);
                            dataMap.put("PAYMENT_BRANCH_ID", BRANCH_ID);
                            System.out.println("############## dataMap " + dataMap);
                            sqlMap.executeUpdate("updateDividendDetails", dataMap);
                        }
                        //Added By Suresh
                        if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("INV")) {
                            getTransDetails(generateSingleTransId, invTransBatchID);
                        } else {
                            //getTransDetails(sharePaymentID);
                            //changed by sreekrishnan...
                            getTransDetails(generateSingleTransId, sharePaymentID);
                        }
                        //Added by sreekrishnan
                        if(map.containsKey("SHARE_DIVIDEND_TRANSFER")){
                            ArrayList arrList = new ArrayList();
                            HashMap authorizeMap = new HashMap();
                            HashMap singleAuthorizeMap = new HashMap();
                            singleAuthorizeMap.put("STATUS", CommonConstants.STATUS_AUTHORIZED);
                            singleAuthorizeMap.put("AUTH_BY", CommonUtil.convertObjToStr(map.get("USER_ID")));
                            singleAuthorizeMap.put("AUTH_DT", currDt.clone());
                            singleAuthorizeMap.put("PAYMENT_ID", sharePaymentID);
                            singleAuthorizeMap.put("SINGLE_TRANS_ID", generateSingleTransId);
                            singleAuthorizeMap.put(CommonConstants.USER_ID,CommonUtil.convertObjToStr(map.get("USER_ID")));
                            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, BRANCH_ID);
                            arrList.add(singleAuthorizeMap);
                            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                            authorizeMap.put(CommonConstants.BRANCH_ID, BRANCH_ID);
                            authorize(authorizeMap, map);
                        }
                    }
                    txMap = null;
                    // Code End
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            //                throw new TransRollbackException(e);
            throw e;
        }
    }
    //added by sreekrishnan for credit loan transfer
    private HashMap interestCalculationTLAD(String accountNo, String prodID){
        HashMap map=new HashMap();
        HashMap insertPenal=new HashMap();
        HashMap hash=null;
        try{
            map.put("ACT_NUM",accountNo);
            map.put("PROD_ID",prodID);
            List lst=sqlMap.executeQueryForList("IntCalculationDetail", map);
            if(lst !=null && lst.size()>0){
                hash=(HashMap)lst.get(0);
                insertPenal.put("AS_CUSTOMER_COMES",hash.get("AS_CUSTOMER_COMES"));
                if(hash.get("AS_CUSTOMER_COMES")!=null  && hash.get("AS_CUSTOMER_COMES").equals("N")){
                    hash=new HashMap();
                    return hash;
                }
                map.put("BRANCH_ID", BRANCH_CODE);
                map.put(CommonConstants.BRANCH_ID, BRANCH_CODE);
                map.putAll(hash);
                map.put("LOAN_ACCOUNT_CLOSING","LOAN_ACCOUNT_CLOSING");
                map.put("CURR_DATE", currDt);
                System.out.println("map before intereest###"+map);
                TaskHeader header=new TaskHeader();
                header.setBranchID(_branchCode);
                InterestCalculationTask interestcalTask=new InterestCalculationTask(header);
                hash =interestcalTask.interestCalcTermLoanAD(map);
                if(hash==null)
                    hash=new HashMap();
                else if (hash!=null && hash.size()>0) {
                    double interest=CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                    double penal =CommonUtil.convertObjToDouble(hash.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                    hash.put("ACT_NUM",accountNo);
                    hash.put("FROM_DT",hash.get("LAST_INT_CALC_DT"));
                    hash.put("FROM_DT",DateUtil.addDays(((Date)hash.get("FROM_DT")),2));
                    hash.put("TO_DATE",map.get("CURR_DATE"));
                    List facilitylst=sqlMap.executeQueryForList("getPaidPrinciple",hash);
                    if(facilitylst!=null && facilitylst.size()>0){
                        hash=(HashMap)facilitylst.get(0);
                        interest-=CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                        penal-=CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                    }
                    if(interest>0)
                        insertPenal.put("CURR_MONTH_INT",new Double(interest));
                    else
                        insertPenal.put("CURR_MONTH_INT",new Double(0));
                    if(penal>0)
                        insertPenal.put("PENAL_INT",new Double(penal));
                    else
                        insertPenal.put("PENAL_INT",new Double(0));
                    List chargeList=sqlMap.executeQueryForList("getChargeDetails",map);
                    if(chargeList !=null && chargeList.size()>0){
                        for(int i=0;i<chargeList.size();i++){
                            HashMap chargeMap=(HashMap)chargeList.get(i);
                            double chargeAmt=CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                            if(chargeMap.get("CHARGE_TYPE").equals("POSTAGE CHARGES") && chargeAmt>0 ){
                                insertPenal.put("POSTAGE CHARGES",chargeMap.get("CHARGE_AMT"));
                            }
                            if(chargeMap.get("CHARGE_TYPE").equals("MISCELLANEOUS CHARGES")&& chargeAmt>0){
                                insertPenal.put("MISCELLANEOUS CHARGES",chargeMap.get("CHARGE_AMT"));
                            }
                            if(chargeMap.get("CHARGE_TYPE").equals("LEGAL CHARGES") && chargeAmt>0){
                                insertPenal.put("LEGAL CHARGES",chargeMap.get("CHARGE_AMT"));
                            }
                            if(chargeMap.get("CHARGE_TYPE").equals("INSURANCE CHARGES") && chargeAmt>0){
                                insertPenal.put("INSURANCE CHARGES",chargeMap.get("CHARGE_AMT"));
                            }
                            if(chargeMap.get("CHARGE_TYPE").equals("EXECUTION DECREE CHARGES") && chargeAmt>0){
                                insertPenal.put("EXECUTION DECREE CHARGES",chargeMap.get("CHARGE_AMT"));
                            }
                            if(chargeMap.get("CHARGE_TYPE").equals("ARBITRARY CHARGES") && chargeAmt>0){
                                insertPenal.put("ARBITRARY CHARGES",chargeMap.get("CHARGE_AMT"));
                            }
                            chargeMap = null;
                        }
                    }
                    chargeList = null;
                }
                interestcalTask = null;
                System.out.println("hashinterestoutput###"+insertPenal);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        map=null;
        hash=null;
        
        return insertPenal;
    }
    //Added By Suresh
    public InvestmentsTransTO getInvestmentsTransTO(String cmd, double intTrfAm, HashMap acctDtlMap, HashMap map) {
        HashMap whereMap = new HashMap();
        InvestmentsTransTO objgetInvestmentsTransTO = new InvestmentsTransTO();
        acctDtlMap.put("INT_PAY_ACC_NO", CommonUtil.convertObjToStr(acctDtlMap.get("INVESTMENT_ACC_NO")));
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
            objgetInvestmentsTransTO.setPurchaseMode("SHARE_PAYMENT");
            if (acctDtlMap.containsKey("BATCH_ID")) {
                objgetInvestmentsTransTO.setBatchID(CommonUtil.convertObjToStr(acctDtlMap.get("BATCH_ID")));
            }
        }
        return objgetInvestmentsTransTO;
    }

    private void getTransDetails(String generateSingleTransId,String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put("SINGLE_TRANS_ID", generateSingleTransId);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        returnMap = new HashMap();
        List transList = null;
        if (!invTransBatchID.equals("") && invTransBatchID.length() > 0) {
            transList = (List) sqlMap.executeQueryForList("getTransferDetailsInvestment", getTransMap);
        } else {
            //transList = (List) sqlMap.executeQueryForList("getTransferDetails", getTransMap);
            transList = (List) sqlMap.executeQueryForList("getTransferDetailsWithLoan", getTransMap);
        }
        if (transList != null && transList.size() > 0) {
            returnMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetails", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            returnMap.put("CASH_TRANS_LIST", cashList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    private void insertData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();
            objLogDAO.addToLog(objLogTO);
            System.out.println("!@#!@#@#$inside Insert:" + map);
            insertShareDividendPaymentTransDetails(map);
            final String USERID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            System.out.println("e : " + e);
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
        shareProductDetails = null;
    }
}
