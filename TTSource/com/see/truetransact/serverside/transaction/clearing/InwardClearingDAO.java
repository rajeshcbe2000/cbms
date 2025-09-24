/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InwardClearingDAO.java
 *
 * Created on Tue Jan 06 18:05:48 IST 2004
 */
package com.see.truetransact.serverside.transaction.clearing;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import java.rmi.RemoteException;
import java.util.Date;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.transferobject.transaction.clearing.InwardClearingTO;
import com.see.truetransact.transferobject.clearing.banklevel.BankClearingParameterTO;

import com.see.truetransact.transferobject.product.operativeacct.OperativeAcctProductTO;

// For the Business Rules...
import com.see.truetransact.businessrule.RuleContext;
import com.see.truetransact.businessrule.RuleEngine;
import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.transaction.LimitCheckingRule;
import com.see.truetransact.businessrule.transaction.ChequeInstrumentRule;
import com.see.truetransact.businessrule.transaction.DraftInstrumentRule;
import com.see.truetransact.businessrule.transaction.DateCheckingRule;

// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

import org.apache.log4j.Logger;

// Transaction
import com.see.truetransact.commonutil.exceptionconstants.transaction.TransactionConstants;
//import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.Transaction;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;

// Bouncing
import com.see.truetransact.serverside.clearing.bouncing.*;
import com.see.truetransact.transferobject.clearing.bouncing.BouncingInstrumentwiseTO;

// Remittance
import com.see.truetransact.transferobject.remittance.RemittancePaymentTO;
import com.see.truetransact.serverside.remittance.RemittancePaymentDAO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;

/**
 * InwardClearingTO DAO.
 *
 */
public class InwardClearingDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private final String INWARD_TO = "InwardClearingTO";
    private final Logger log = Logger.getLogger(InwardClearingDAO.class);
    private Transaction transModuleBased;
    private HashMap execInputMap = null;
    private int taskSelected = 0;
    private final int NEWDATA = 500;
    private final int EXCEPTION = 100;
    private final int BOUNCE = 200;
    private final int CANCEL = 300;
    private final String OPERATIVE = "OA";
    private final String ADVANCES = "AD";
    private final String YES = "N";
    private final String STATUS_BOUNCED = "BOUNCED";
    private String BRANCHID = "";
    private String userID = "";
    private String bounceReason = "";
    private boolean isException = false;
    private String[] bounceClearingType;
    private HashMap execReturnMap;
    private String prodType = "";
    private String inwardAcctHd = ""; //getInwardAcctHead();
    private Date currDt = null;
    private String act_closing_min_bal_check = null;

    /**
     * Creates a new instance of InwardClearingTODAO
     */
    public InwardClearingDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        currDt = ServerUtil.getCurrentDate(_branchCode);
        Date trans_Dt = (Date) currDt.clone();
        Date tempDt = (Date) map.get("TRANS_DT");
        if (tempDt != null) {
            trans_Dt.setDate(tempDt.getDate());
            trans_Dt.setMonth(tempDt.getMonth());
            trans_Dt.setYear(tempDt.getYear());
            map.put("TRANS_DT", trans_Dt);
        }
        map.put("INITIATED_BRANCH", _branchCode);
        System.out.println("Inside getdata");
        List list = (List) sqlMap.executeQueryForList("getSelectInwardClearingTO", map);
        returnMap.put(INWARD_TO, list);
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;
    }

    public HashMap execute(HashMap map) throws Exception {
        execInputMap = map;
        act_closing_min_bal_check = null;
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        if (map.containsKey("ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER")) {
            act_closing_min_bal_check = "ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER";
        }
//        HashMap execReturnMap = new HashMap();
        System.out.println("Map in Dao " + map);
        System.out.println("@@@act_closing_min_bal_check" + act_closing_min_bal_check);
        // initial Objects
        Double oldAmount = (Double) map.get(TransactionDAOConstants.OLDAMT);

        // Log DAO
        LogDAO objLogDAO = new LogDAO();

        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));

        // Module Based Updations
//            transModuleBased = TransactionFactory.createTransaction(TransactionFactory.OPERATIVE);
        BRANCHID = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
        userID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));

        /*
         * If the To-object is present in the Map and its is not null, do the
         * basic operations.
         */

        if (map.containsKey(INWARD_TO)) {
            InwardClearingTO objTO = (InwardClearingTO) map.get(INWARD_TO);
            prodType = objTO.getProdType();
            if (!prodType.equals("RM")) {
                transModuleBased = TransactionFactory.createTransaction(objTO.getProdType());
            }

            /**
             * To select the Task Type
             */
            taskSelected = CommonUtil.convertObjToInt(map.get("TaskSelected"));
            if (taskSelected == BOUNCE) {
                bounceReason = CommonUtil.convertObjToStr(map.get("BounceRemarks"));
            }
            bounceClearingType = null;
            if (map.containsKey("BounceClearingType")) {
                bounceClearingType = CommonUtil.convertObjToStr(map.get("BounceClearingType")).split("~");
            }

            final String command = objTO.getCommand();
            if (objTO != null && command != null) {
                try {
                    sqlMap.startTransaction();

                    objTO.setSuserId(objLogTO.getUserId());
                    objTO.setBranchId(objLogTO.getBranchId());

                    if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                        insertData(objTO);
                    } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                        updateData(objTO, oldAmount.doubleValue());
                    } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                        deleteData(objTO, oldAmount.doubleValue());
                    } else {
                        throw new NoCommandException();
                    }

                    objLogTO.setData(objTO.toString());
                    objLogTO.setPrimaryKey(objTO.getKeyData());
                    objLogTO.setStatus(command);

                    objLogDAO.addToLog(objLogTO);

                    sqlMap.commitTransaction();

                } catch (Exception e) {
                    e.printStackTrace();
                    sqlMap.rollbackTransaction();
                    throw e;
                }
            }

            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                execReturnMap = new HashMap();
                execReturnMap.put(CommonConstants.TRANS_ID, objTO.getInwardId());
            }

            taskSelected = 0;
            objTO = null;
        }

        /*
         * Method Call to perform the desired Authorization.
         */
        if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            taskSelected = CommonUtil.convertObjToInt(map.get("TaskSelected"));
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap, objLogDAO, objLogTO);
            }
        }

        oldAmount = null;
        objLogDAO = null;
        objLogTO = null;
        map = null;
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return execReturnMap;
    }

    private void insertData(InwardClearingTO objTO) throws Exception {
        /**
         * if the Data is Entered for the first time... Validate all the Rules...
         */
        final String INWARD_ID = getInwardID();
        objTO.setInwardId(INWARD_ID);
        objTO.setStatus(CommonConstants.STATUS_CREATED);
        objTO.setSDate(currDt);
        objTO.setSuserId(userID);

        objTO.setInwardDt(currDt);
        objTO.setCreatedBy(userID);

        if (taskSelected == NEWDATA) {
            validateSchedule(objTO.getScheduleNo());

            if (!CommonUtil.convertObjToStr(objTO.getAcctNo()).equals("") && !prodType.equals("RM")) {
                /**
                 * To Apply all the Data Validating Rules
                 */
                transModuleBased.validateRules(getRuleMap(objTO, 0.0, true), isException);
            }
        }

        if (taskSelected == EXCEPTION) {
            objTO.setAuthorizeStatus(CommonConstants.STATUS_EXCEPTION);
        }


        if (taskSelected == BOUNCE) {
            objTO.setAuthorizeStatus(STATUS_BOUNCED);
//            objTO.setAuthorizeBy(userID);
//            objTO.setAuthorizeDt(currDt);
        } else {
            if (!prodType.equals("RM")) {
                transModuleBased.performShadowAdd(getRuleMap(objTO, 0.0, true));
            }
            //transModuleBased.performOtherBalanceAdd(getRuleMap(objTO, 0.0, true));
        }
        if (prodType.equals("RM") && taskSelected == NEWDATA) {
            HashMap ruleMap = getRuleMap(objTO, 0.0, true);
            ruleMap.put(TransactionDAOConstants.DATE, currDt);
            try {
                DraftInstrumentRule draftInstRuleObj = new DraftInstrumentRule();
                draftInstRuleObj.validate(ruleMap);
            } catch (com.see.truetransact.businessrule.validateexception.ValidationRuleException e) {
                System.out.println("@#$@#$ e.getExceptionHashMap() : " + e.getExceptionHashMap());
                System.out.println("@#$@#$ e.getMessage() : " + e.getMessage());
                ArrayList lst = new ArrayList();
                lst.add(e.getMessage());
                HashMap exception = new HashMap();
                exception.put(CommonConstants.EXCEPTION_LIST, lst);
                exception.put(CommonConstants.CONSTANT_CLASS, TransactionDAOConstants.TRANS_RULE_MAP);
                System.out.println("#$#$#$ Exception List : " + lst);
                throw new TTException(exception);
            }
//            callRemitPayDAO(objTO);
        }


        sqlMap.executeUpdate("insertInwardClearingTO", objTO);
//        if(objTO.getProdType().equals("OA") ||objTO.getProdType().equals("AD")) {
//            HashMap map = new HashMap();
//            map.put("ACT_NUM",objTO.getAcctNo());
//            
////            System.out.println("@@@@@@@@@@@@$$%^^%map"+map);
//            List lst = (List)sqlMap.executeQueryForList("chkForPassBook",map);
////            System.out.println("@@@@@@@@@@@@$$%^^%list"+lst);
////            System.out.println("@@@@@@@@@@@@$$%^^%list"+lst.size());
//            map=null;
//            if(lst.size()>0) {
//                map=(HashMap)lst.get(0);
////                System.out.println("@@@@@@@@@@@@$$%^^%maptochecky"+map);
//                if(map.containsValue("Y")){
//                    double finamt=0;
//                    HashMap where = new HashMap();
//                    where.put("DEBIT",objTO.getAmount());
//                    double amt = CommonUtil.convertObjToDouble(where.get("DEBIT")).doubleValue();
////                    System.out.println("@@@@@@@@@@@@$$%^^%amtdebit"+amt);
//                    HashMap map1 = new HashMap();
//                    map1.put("ACT_NUM", objTO.getAcctNo());
//
////                    List lst1 = (List)sqlMap.executeQueryForList("getClearBalance"+objTO.getProdType(),map1);
//////                    System.out.println("@@@@@@@@@@@@$$%^^%listdebit"+lst1);
//
//                    List lst1 = (List)sqlMap.executeQueryForList("getClearBalance"+objTO.getProdType(),map1);
//                    System.out.println("@@@@@@@@@@@@$$%^^%listdebit"+lst1);
//
//                    map1=null;
//                    map1=(HashMap)lst1.get(0);
//                    double amt1 = CommonUtil.convertObjToDouble(map1.get("CLEAR_BALANCE")).doubleValue();
//                    double amt2 = CommonUtil.convertObjToDouble(map1.get("SHADOW_DEBIT")).doubleValue();
//                    double amt3 = CommonUtil.convertObjToDouble(map1.get("SHADOW_CREDIT")).doubleValue();
//                    String st=CommonUtil.convertObjToStr(map1.get("SHADOW_DEBIT"));
//                    if(st.startsWith("-"))
//                        finamt=(amt1+amt2+amt3)-amt;
//                    else {
//                        finamt=(amt1-amt2+amt3)-amt;
//                    }
////                    System.out.println("@@@@@@@@@@@@$$%^^%finamtdebit"+finamt);
//                    where.put("BALANCE", new Double(finamt));
//                    where.put("TRANS_ID",objTO.getInwardId());
//                    where.put("ACT_NUM",objTO.getAcctNo());
//                    where.put("TRANS_DT", objTO.getClearingDt());
//                    where.put("PARTICULARS", objTO.getPayeeName());
//                    where.put("INSTRUMENT_NO1", objTO.getInstrumentNo1());
//                    where.put("INSTRUMENT_NO2", objTO.getInstrumentNo2());
//                    where.put("PBOOKFLAG", "0");
////                    where.put("SLNO", "0");
//                    where.put("INST_TYPE", objTO.getInstrumentType());
//                    where.put("INST_DT",objTO.getInstrumentDt());
//                    where.put("STATUS",objTO.getStatus());
////                    System.out.println("@@@@@@@@@@@@$$%^^%where"+where);
//                    sqlMap.executeUpdate("insertPassBook",where);
//                }
//            }
//        }
        /**
         * To Enter the Bounce Instrument(s) data in Bounce Table...
         */
        if (taskSelected == BOUNCE) {
            doInwardBouncing(objTO, false);
        }
    }

    private void callRemitPayDAO(InwardClearingTO objTO) throws Exception {
        final RemittancePaymentTO objRemittancePaymentTO = setRemittancePaymentData(objTO);
        objRemittancePaymentTO.setCommand("INSERT");
        LinkedHashMap transactionDetailsTO = new LinkedHashMap();
        transactionDetailsTO.put("NOT_DELETED_TRANS_TOs", setTransactionDetailsTO(objTO));
        HashMap data = new HashMap();
        data.put("MODE", "INSERT");
        data.put(CommonConstants.BRANCH_ID, _branchCode);
        data.put(CommonConstants.USER_ID, objTO.getSuserId());
        data.put("TransactionTO", transactionDetailsTO);
        data.put("RemittancePaymentTO", objRemittancePaymentTO);
//        data.put(CommonConstants.MODULE, objTO.getModule());
//        data.put(CommonConstants.SCREEN, getScreen());
        System.out.println("DATA## : " + data);
        RemittancePaymentDAO remitPayDAO = new RemittancePaymentDAO();
        remitPayDAO.setCallingFromOtherDAO(true);
        HashMap returnMap = remitPayDAO.execute(data);
        System.out.println("@#@# remitPayDAO returnMap : " + returnMap);
        ArrayList lst = new ArrayList();
        lst.add(returnMap);
        data = new HashMap();
        data.put(CommonConstants.USER_ID, objTO.getAuthorizeBy());
        data.put(CommonConstants.AUTHORIZEDATA, lst);
        data.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
        HashMap authMap = new HashMap();
        authMap.put(CommonConstants.AUTHORIZEMAP, data);
        authMap.put(CommonConstants.BRANCH_ID, _branchCode);
        authMap.put(CommonConstants.USER_ID, objTO.getAuthorizeBy());
        remitPayDAO = new RemittancePaymentDAO();
        remitPayDAO.setCallingFromOtherDAO(true);
        remitPayDAO.execute(authMap);
    }

    public RemittancePaymentTO setRemittancePaymentData(InwardClearingTO objTO) throws Exception {
        final RemittancePaymentTO objRemittancePaymentTO = new RemittancePaymentTO();
        objRemittancePaymentTO.setCreatedBy(objTO.getCreatedBy());
        objRemittancePaymentTO.setRemitPayId("");
        objRemittancePaymentTO.setInstrumentType(objTO.getInstrumentType());
        objRemittancePaymentTO.setPayStatus("PAID");
        objRemittancePaymentTO.setBranchId(_branchCode);
        objRemittancePaymentTO.setSerialNo(objTO.getAcctNo());
        objRemittancePaymentTO.setCharges(new Double(0));
        objRemittancePaymentTO.setPayAmt(objTO.getAmount());
        objRemittancePaymentTO.setRemarks("Through Inward Clearing");
        objRemittancePaymentTO.setAddress("");
        objRemittancePaymentTO.setInstrumentNo1(objTO.getInstrumentNo1());
        objRemittancePaymentTO.setInstrumentNo2(objTO.getInstrumentNo2());

        return objRemittancePaymentTO;
    }

    private LinkedHashMap setTransactionDetailsTO(InwardClearingTO objTO) throws Exception {
        LinkedHashMap setMap = new LinkedHashMap();
        TransactionTO transTO = new TransactionTO();
        transTO.setCommand("INSERT");
        transTO.setApplName(objTO.getPayeeName());
        transTO.setTransType(TransactionDAOConstants.TRANSFER);
        transTO.setTransAmt(objTO.getAmount());
        transTO.setProductId(null);
        transTO.setProductType("GL");
        transTO.setDebitAcctNo(inwardAcctHd);
        setMap.put("1", transTO);
        return setMap;
    }

    private void validateSchedule(String scheduleNo) throws Exception {
        /**
         * To Check if the No of instruments for the particular Schedule No in
         * Inward Clearing are equal to Physical Count in Inward Tally. if yes,
         * No more data/records for that Schedule No should be allowed...
         */
        HashMap dataMap = new HashMap();
        dataMap.put("SCHEDULE_NO", CommonUtil.convertObjToStr(scheduleNo));
        dataMap.put("BRANCH_ID", _branchCode);
        System.out.println("DataMap: " + dataMap);
        List resultList = (List) sqlMap.executeQueryForList("clearing.getTallyDetail", dataMap);
        if (resultList.size() > 0) {
            HashMap objMap = (HashMap) resultList.get(0);
            int totalCount = CommonUtil.convertObjToInt(objMap.get("PHY_INSTRUMENTS"));
            int totalAmount = CommonUtil.convertObjToInt(objMap.get("PHY_AMOUNT"));

            List dataList = (List) sqlMap.executeQueryForList("clearing.getClearingDetail", dataMap);

            if (dataList.size() > 0) {
                HashMap objDataMap = (HashMap) dataList.get(0);
                int count = CommonUtil.convertObjToInt(objDataMap.get("COUNT"));
                int sum = CommonUtil.convertObjToInt(objDataMap.get("SUM"));

                if ((totalCount <= count) || (totalAmount <= sum)) {
                    System.out.println("Check Tally Count/Amount");
                    HashMap exception = new HashMap();
                    ArrayList list = new ArrayList();
                    list.add(TransactionConstants.TALLY_COUNT); // The Key Value for the ExceptionHashMap...
                    exception.put(CommonConstants.EXCEPTION_LIST, list);
                    exception.put(CommonConstants.CONSTANT_CLASS, TransactionDAOConstants.TRANS_RULE_MAP);
                    throw new TTException(exception);
                }
            }
        }
    }

    private void doInwardBouncing(InwardClearingTO objTO, boolean authorize) throws Exception {
        BouncingInstrumentwiseDAO objBouncingDao = new BouncingInstrumentwiseDAO();

        BouncingInstrumentwiseTO objBouncing = new BouncingInstrumentwiseTO();
        objBouncing.setBouncingType("INSTRUMENTWISE");
        objBouncing.setInwardId(objTO.getInwardId());
        objBouncing.setInwardScheduleNo(CommonUtil.convertObjToStr(objTO.getScheduleNo()));
        objBouncing.setPresentAgain(YES);

        objBouncing.setClearingType(bounceClearingType[0]);

        HashMap whereMap = new HashMap();
        whereMap.put("CLEARING_TYPE", bounceClearingType[0]);   //objBouncing.getClearingType());
        whereMap.put("BRANCH_ID", _branchCode);
        if (bounceClearingType != null && bounceClearingType.length > 0) {
            whereMap.put("CLEARING_DATE", bounceClearingType[1]);
        }

        HashMap outwardClearingMap = (HashMap) sqlMap.executeQueryForObject("getScheduleNo", whereMap);

        if (outwardClearingMap != null) {
            objBouncing.setClearingDate((Date) outwardClearingMap.get("CLEARING_DT"));
            objBouncing.setScheduleNo(CommonUtil.convertObjToStr(outwardClearingMap.get("SCHEDULE_NO")));
        } else {
            throw new TTException("Inward Return Clearing is not configured.");
        }

        objBouncing.setStatus(CommonConstants.STATUS_CREATED);
        objBouncing.setBouncingReason(bounceReason);
        objBouncing.setBranchId(_branchCode);
        objBouncing.setAmount(objTO.getAmount());

        objBouncing.setCommand(CommonConstants.TOSTATUS_INSERT);

        HashMap bounceMap = new HashMap();
        bounceMap.put(CommonConstants.USER_ID, (String) execInputMap.get(CommonConstants.USER_ID));
        bounceMap.put(CommonConstants.BRANCH_ID, (String) execInputMap.get(CommonConstants.BRANCH_ID));
        bounceMap.put(CommonConstants.IP_ADDR, (String) execInputMap.get(CommonConstants.IP_ADDR));
        bounceMap.put("BouncingInstrumentwiseTO", objBouncing);

        // Authorize true or not
        if (authorize) {
            HashMap authMap = new HashMap();
            authMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
            bounceMap.put(CommonConstants.AUTHORIZEMAP, authMap);
            System.out.println("bounceMap : " + bounceMap);
        }
        execReturnMap = new HashMap();
        execReturnMap = objBouncingDao.execute(bounceMap, false);
    }

//    public void inwardBouncingCharge (InwardClearingTO inwardObjTO) throws Exception{
//        HashMap map = new HashMap();
//        map.put(TransferTrans.DR_PROD_ID, inwardObjTO.getProdId());
//        map.put(TransferTrans.DR_ACT_NUM, inwardObjTO.getAcctNo()) ;
//        map.put(TransferTrans.DR_BRANCH, inwardObjTO.getBranchId()) ;
//        map.put(TransferTrans.CR_BRANCH, inwardObjTO.getBranchId()) ;
//        map.put(TransferTrans.DR_AC_HD, inwardObjTO.getAcHdId()) ;
//        map.put(TransferTrans.DR_PROD_TYPE, inwardObjTO.getProdType());
//        map.put(TransferTrans.CR_AC_HD, "");
//        map.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
//        
//        TransferTrans trans = new TransferTrans();
//        ArrayList batchList = new ArrayList();
//        batchList.add(trans.getDebitTransferTO(map, 0)) ;
//        batchList.add(trans.getCreditTransferTO(map, 0)) ;
//        trans.doDebitCredit(batchList, BRANCHID);
//        batchList = null;
//    }    
    private String getInwardID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "INWARD_CLEARING");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//for trans_batch_id resetting, branch code
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private HashMap getRuleMap(InwardClearingTO objInwardClearingTO, double prevAmount, boolean makeNegative) throws Exception {
        HashMap inputMap = new HashMap();
        double amount = objInwardClearingTO.getAmount().doubleValue();

        if (makeNegative) {
            amount = -amount + prevAmount;
        } else {
            amount -= prevAmount;
        }
        inputMap.put(TransactionDAOConstants.PROD_TYPE, CommonUtil.convertObjToStr(objInwardClearingTO.getProdType()));
        inputMap.put(TransactionDAOConstants.PROD_ID, CommonUtil.convertObjToStr(objInwardClearingTO.getProdId()));
        inputMap.put(TransactionDAOConstants.ACCT_NO, CommonUtil.convertObjToStr(objInwardClearingTO.getAcctNo()));
        inputMap.put(TransactionDAOConstants.AMT, CommonUtil.convertObjToDouble(String.valueOf(amount)));
        inputMap.put(TransactionDAOConstants.INSTRUMENT_1, CommonUtil.convertObjToStr(objInwardClearingTO.getInstrumentNo1()));
        inputMap.put(TransactionDAOConstants.INSTRUMENT_2, CommonUtil.convertObjToStr(objInwardClearingTO.getInstrumentNo2()));
        inputMap.put(TransactionDAOConstants.INSTRUMENT_TYPE, objInwardClearingTO.getInstrumentType());
        inputMap.put(TransactionDAOConstants.DATE, objInwardClearingTO.getInstrumentDt());
        inputMap.put(TransactionDAOConstants.TRANS_TYPE, TransactionDAOConstants.DEBIT);
        inputMap.put(TransactionDAOConstants.BRANCH_CODE, objInwardClearingTO.getBranchId());
        inputMap.put(TransactionDAOConstants.TO_STATUS, objInwardClearingTO.getStatus());
        inputMap.put(TransactionDAOConstants.AUTHORIZE_BY, objInwardClearingTO.getAuthorizeBy());
        inputMap.put(TransactionDAOConstants.AUTHORIZE_STATUS, objInwardClearingTO.getAuthorizeStatus());
        inputMap.put(TransactionDAOConstants.TRANS_ID, objInwardClearingTO.getInwardId());
        inputMap.put(TransactionDAOConstants.TODAY_DT, currDt);
        inputMap.put(TransactionDAOConstants.TRANS_MODE, TransactionDAOConstants.CLEARING);
        if (act_closing_min_bal_check != null && act_closing_min_bal_check.equalsIgnoreCase("ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER")) {
            inputMap.put("ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER", "ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER");
        }
        System.out.println("Return Map: " + inputMap);

        return inputMap;
    }

    private void doInwardBouncingEdit(BouncingInstrumentwiseTO objBouncing, boolean authorize) throws Exception {
        BouncingInstrumentwiseDAO objBouncingDao = new BouncingInstrumentwiseDAO();

        HashMap bounceMap = new HashMap();
        bounceMap.put(CommonConstants.USER_ID, (String) execInputMap.get(CommonConstants.USER_ID));
        bounceMap.put(CommonConstants.BRANCH_ID, (String) execInputMap.get(CommonConstants.BRANCH_ID));
        bounceMap.put(CommonConstants.IP_ADDR, (String) execInputMap.get(CommonConstants.IP_ADDR));
        bounceMap.put("BouncingInstrumentwiseTO", objBouncing);

        // Authorize true or not
        if (authorize) {
            HashMap authMap = new HashMap();
            authMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
            bounceMap.put(CommonConstants.AUTHORIZEMAP, authMap);
            System.out.println("bounceMap : " + bounceMap);
            authMap = null;
        }
        execReturnMap = objBouncingDao.execute(bounceMap, false);
        bounceMap = null;
        objBouncingDao = null;
    }

    private void updateData(InwardClearingTO objTO, double prevAmt) throws Exception {
        objTO.setStatus(CommonConstants.STATUS_MODIFIED);
        objTO.setSDate(currDt);
        objTO.setSuserId(userID);

        String bounceID = CommonUtil.convertObjToStr(sqlMap.executeQueryForObject("getBouncingData", objTO.getInwardId()));

        if (taskSelected == BOUNCE) {
            objTO.setAuthorizeStatus(STATUS_BOUNCED);
            if (bounceID.length() > 0) {
                BouncingInstrumentwiseTO objBouncing = (BouncingInstrumentwiseTO) sqlMap.executeQueryForObject("getSelectBouncingInstrumentwiseTO", bounceID);
                objBouncing.setCommand(CommonConstants.TOSTATUS_UPDATE);
                objBouncing.setBouncingReason(bounceReason);
                doInwardBouncingEdit(objBouncing, false);

                objBouncing = null;
            } else {
                doInwardBouncing(objTO, false);
            }
            transModuleBased.performShadowMinus(getRuleMap(objTO, 0.0, false));
        } else {
            transModuleBased.validateRules(getRuleMap(objTO, 0.0, true), isException);
            if (bounceID.length() > 0) {
                BouncingInstrumentwiseTO objBouncing = (BouncingInstrumentwiseTO) sqlMap.executeQueryForObject("getSelectBouncingInstrumentwiseTO", bounceID);
                objBouncing.setCommand(CommonConstants.TOSTATUS_DELETE);
                objBouncing.setBouncingReason(bounceReason);
                doInwardBouncingEdit(objBouncing, false);
                objBouncing = null;
            }
        }
        if (taskSelected != BOUNCE) {
            transModuleBased.performShadowAdd(getRuleMap(objTO, prevAmt, true));
        }
        //transModuleBased.performOtherBalanceAdd(getRuleMap(objTO, prevAmt, true));
        objTO.setInitiatedBranch(_branchCode);
        objTO.setClearingDt(currDt);
        sqlMap.executeUpdate("updateInwardClearingTO", objTO);
//        if (objTO.getProdType().equals("OA") || objTO.getProdType().equals("AD")) {
//            double finamt=0;
//            HashMap where = new HashMap();
//            where.put("DEBIT",objTO.getAmount());
//            double amt = CommonUtil.convertObjToDouble(where.get("DEBIT")).doubleValue();
////            System.out.println("@@@@@@@@@@@@$$%^^%amtdebit"+amt);
//            HashMap map1 = new HashMap();
//            map1.put("ACT_NUM", objTO.getAcctNo());
//            List lst1 = (List)sqlMap.executeQueryForList("getClearBalance"+objTO.getProdType(),map1);
////            System.out.println("@@@@@@@@@@@@$$%^^%listdebit"+lst1);
//            map1=null;
//            map1=(HashMap)lst1.get(0);
//            double amt1 = CommonUtil.convertObjToDouble(map1.get("CLEAR_BALANCE")).doubleValue();
//            double amt2 = CommonUtil.convertObjToDouble(map1.get("SHADOW_DEBIT")).doubleValue();
//            double amt3 = CommonUtil.convertObjToDouble(map1.get("SHADOW_CREDIT")).doubleValue();
//            String st=CommonUtil.convertObjToStr(map1.get("SHADOW_DEBIT"));
//            if(st.startsWith("-"))
//                finamt=(amt1+amt2+amt3)-amt;
//            else {
//                finamt=(amt1-amt2+amt3)-amt;
//            }
////            System.out.println("@@@@@@@@@@@@$$%^^%finamtdebit"+finamt);
//            where.put("BALANCE", new Double(finamt));
//            where.put("TRANS_ID",objTO.getInwardId());
//            where.put("ACT_NUM",objTO.getAcctNo());
//            where.put("TRANS_DT", objTO.getClearingDt());
//            where.put("PARTICULARS", objTO.getPayeeName());
//            where.put("INSTRUMENT_NO1", objTO.getInstrumentNo1());
//            where.put("INSTRUMENT_NO2", objTO.getInstrumentNo2());
//            where.put("INST_TYPE", objTO.getInstrumentType());
//            where.put("INST_DT",objTO.getInstrumentDt());
//            where.put("STATUS",objTO.getStatus());
////            System.out.println("@@@@@@@@@@@@$$%^^%where"+where);
//            sqlMap.executeUpdate("updatePassBook",where);
//        }
    }

    private void deleteData(InwardClearingTO objTO, double prevAmt) throws Exception {
        objTO.setStatus(CommonConstants.STATUS_DELETED);
        objTO.setSDate(currDt);
        objTO.setSuserId(userID);

        transModuleBased.performShadowMinus(getRuleMap(objTO, 0.0, false));
        //transModuleBased.performOtherBalanceMinus(getRuleMap(objTO, 0.0, true));
        objTO.setInitiatedBranch(_branchCode);
        objTO.setClearingDt(currDt);
        sqlMap.executeUpdate("deleteInwardClearingTO", objTO);

        String bounceID = CommonUtil.convertObjToStr(sqlMap.executeQueryForObject("getBouncingData", objTO.getInwardId()));
        if (bounceID.length() > 0) {
            BouncingInstrumentwiseTO objBouncing = (BouncingInstrumentwiseTO) sqlMap.executeQueryForObject("getSelectBouncingInstrumentwiseTO", bounceID);
            objBouncing.setCommand(CommonConstants.TOSTATUS_DELETE);
            doInwardBouncingEdit(objBouncing, false);
            objBouncing = null;
        }
        HashMap map = new HashMap();
        map.put("STATUS", objTO.getStatus());
        map.put("TRANS_ID", objTO.getInwardId());
//        sqlMap.executeUpdate("deletePassBook",map);
    }

    public static void main(String str[]) {
        try {
            InwardClearingDAO dao = new InwardClearingDAO();
            InwardClearingTO objTO = new InwardClearingTO();

            TOHeader toHeader = new TOHeader();
            toHeader.setCommand(CommonConstants.TOSTATUS_INSERT);//To tell what to do... Insert, Update, Delete...
            objTO.setTOHeader(toHeader);
            objTO.setInwardId("I0001070");
            objTO.setProdId("SB001");
            objTO.setAcctNo("");//OA001001
            objTO.setClearingType("IB_CLEARING");
            objTO.setClearingDt(DateUtil.getDateMMDDYYYY(null));
            objTO.setScheduleNo("S001");
            objTO.setInstrumentNo1("ABC");
            objTO.setInstrumentNo2("1004");
            objTO.setInstrumentDt(DateUtil.getDateMMDDYYYY("02/20/2004"));
            objTO.setAmount(new Double(20.0));
            objTO.setPayeeName("RAHUL");
            objTO.setBankCode("B1");
            objTO.setBranchCode("B1001");
            //objTO.setStatus ("MODIFIED");

            HashMap hash = new HashMap();
            hash.put("InwardClearingTO", objTO);
            dao.execute(hash);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void authorize(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        String status = "";
        String remark = CommonUtil.convertObjToStr(map.get("REMARKS"));
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        inwardAcctHd = ""; //getInwardAcctHead();
        BankClearingParameterTO objClearingParam = null;
        Double amount;
        HashMap dataMap;
        String transID;
        HashMap rmMap = new HashMap();
//        rmMap=(HashMap)map.get("AUTHORIZEDATA");
        boolean bounce = false;
        InwardClearingTO objInwardClearingTO = null;

        for (int i = 0, j = selectedList.size(); i < j; i++) {
            try {
                sqlMap.startTransaction();

                status = CommonUtil.convertObjToStr(map.get(CommonConstants.AUTHORIZESTATUS));;

                dataMap = (HashMap) selectedList.get(i);
                transID = CommonUtil.convertObjToStr(dataMap.get("INWARD ID"));
                dataMap.put(CommonConstants.USER_ID, objLogTO.getUserId());
                dataMap.put("INWARD_ID", transID);
                dataMap.put("REMARKS", remark);
                dataMap.put("BRANCH_ID", BRANCHID);

                dataMap.put("TODAY_DT", currDt);
                objClearingParam = (BankClearingParameterTO) sqlMap.executeQueryForObject("getSelectBankClearingParameterTO",
                        CommonUtil.convertObjToStr(dataMap.get("CLEARING TYPE")));

                inwardAcctHd = objClearingParam.getClearingHd();

                //__ If the Map Cotains the Bounce Status... just reject the record...
                if (CommonUtil.convertObjToStr(dataMap.get("AUTHORIZE STATUS")).equalsIgnoreCase(STATUS_BOUNCED)) {
                    System.out.println("Bounce Occured...");
//                    status = CommonConstants.STATUS_REJECTED;


                    //__ To mark, this record is already entered in Bounce Table...
                    bounce = true;
                }

                /*
                 * Update the Authorization Fields and Update the Available
                 * Balance.
                 */
                System.out.println("Data MAp in Clearing DAO: " + dataMap);
                dataMap.put(CommonConstants.STATUS, status);
                dataMap.put("INITIATED_BRANCH", _branchCode);
                dataMap.put("TRANS_DT", currDt.clone());
                sqlMap.executeUpdate("authorizeInwardClearing", dataMap);
//                HashMap where = new HashMap();
//                where.put("TRANS_ID",dataMap.get("INWARD ID"));
//                where.put("STATUS",dataMap.get("STATUS"));
//                sqlMap.executeUpdate("authorizePassBook", where);


                // AuthorizeStatus is Authorized
                objInwardClearingTO = getTransactionData(dataMap);
                if (!objInwardClearingTO.getProdType().equals("RM")) {
                    transModuleBased = TransactionFactory.createTransaction(objInwardClearingTO.getProdType());
                }

                if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED) && taskSelected != BOUNCE && !bounce) {
                    // Exisiting status is Created or Modified
                    if (objInwardClearingTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED)
                            || objInwardClearingTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_MODIFIED)) {
                        System.out.println("authorize ... objInwardClearingTO : " + objInwardClearingTO.toString());
                        System.out.println("Amount : " + CommonUtil.convertObjToStr(objInwardClearingTO.getAmount()));

                        if (!objInwardClearingTO.getProdType().equals("RM")) {
                            HashMap ruleMap = getRuleMap(objInwardClearingTO, 0.0, true);
                            if (objInwardClearingTO.getProdType().equals("AD") || objInwardClearingTO.getProdType().equals("AAD")) {
                                ruleMap.put("DEBIT_LOAN_TYPE", "DP");
                            }
                            transModuleBased.validateRules(ruleMap, false);

                            String ACT_NUM = objInwardClearingTO.getAcctNo();
                            String prod = objInwardClearingTO.getProdType();
                            HashMap where = new HashMap();
                            where.put("ACT_NUM", ACT_NUM);
                            double limit = 0.0;
                            double dp = 0.0;
                            if (objInwardClearingTO.getProdType().equals("OA") || objInwardClearingTO.getProdType().equals("AD") || objInwardClearingTO.getProdType().equals("AAD")) {
                                String type_of_tod = "";
                                List lstBal = sqlMap.executeQueryForList("getBalance" + prod, where);
                                where = null;
                                if (lstBal != null && lstBal.size() > 0) {
                                    where = (HashMap) lstBal.get(0);
                                    double clBal = CommonUtil.convertObjToDouble(where.get("CLEAR_BALANCE")).doubleValue();
                                    if (objInwardClearingTO.getProdType().equals("AD") || objInwardClearingTO.getProdType().equals("AAD")) {
                                        clBal = CommonUtil.convertObjToDouble(where.get("LOAN_PAID_INT")).doubleValue();
                                        limit = CommonUtil.convertObjToDouble(where.get("LIMIT")).doubleValue();
                                        dp = CommonUtil.convertObjToDouble(where.get("DRAWING_POWER")).doubleValue();
                                        if (dp > 0 && dp < limit) {
                                            limit = dp;
                                        }
                                    }
                                    ruleMap.put("LIMIT", String.valueOf(limit));
                                    double avBal = CommonUtil.convertObjToDouble(where.get("AVAILABLE_BALANCE")).doubleValue();
                                    double totBal = CommonUtil.convertObjToDouble(where.get("TOTAL_BALANCE")).doubleValue();
                                    double amt = CommonUtil.convertObjToDouble(objInwardClearingTO.getAmount()).doubleValue();
                                    double tod_amt = CommonUtil.convertObjToDouble(where.get("TOD_AMOUNT")).doubleValue();
                                    double tod_util = CommonUtil.convertObjToDouble(where.get("TOD_UTILIZED")).doubleValue();
                                    double tod_left = tod_amt - tod_util;
                                    where.put("AMOUNT", objInwardClearingTO.getAmount());
                                    Date toDt = (Date) currDt.clone();
                                    if (objInwardClearingTO.getClearingDt() != null) {
                                        toDt.setDate(objInwardClearingTO.getClearingDt().getDate());
                                        toDt.setMonth(objInwardClearingTO.getClearingDt().getMonth());
                                        toDt.setYear(objInwardClearingTO.getClearingDt().getYear());
                                        where.put("TODAY_DT", toDt);
                                    }
//                                    where.put("TODAY_DT",objInwardClearingTO.getClearingDt());
                                    where.put("ACT_NUM", ACT_NUM);
                                    System.out.println("For Updating Balances" + where);
                                    double posBal = Math.abs(clBal);
                                    System.out.println("posBal" + posBal);
                                    List lst = sqlMap.executeQueryForList("getTypeOfTod", where);
                                    HashMap hash = new HashMap();
                                    if (lst != null && lst.size() > 0) {
                                        hash = (HashMap) lst.get(0);
                                        type_of_tod = CommonUtil.convertObjToStr(hash.get("TYPE_OF_TOD"));
                                    }
                                    if (clBal >= 0.0 && clBal <= amt) {
                                        if (objInwardClearingTO.getProdType().equals("OA")) {
                                            where.put("TODUTILIZED", "");
                                        } else if (objInwardClearingTO.getProdType().equals("AD") || objInwardClearingTO.getProdType().equals("AAD")) {
                                            where.put("TODUTILIZEDAD", "");
                                        }
                                    }
                                    if (clBal < 0.0) {
                                        where.put("TODUTILIZEDCBLESS", "");
                                    }
                                    if (clBal > 0.0 && clBal > amt) {
                                        where.put("TODUTILIZEDCBMORE", "");
                                    }
                                    if (lst != null && lst.size() > 0) {
                                        if (objInwardClearingTO.getProdType().equals("AD") || objInwardClearingTO.getProdType().equals("AAD")) {
                                            ruleMap.put("NORMALDEBITAD", "");
                                        } else {
                                            ruleMap.put("NORMALDEBIT", "");
                                        }
                                    } else {
                                        ruleMap.put("NORMALDEBIT", "");
                                    }
                                    System.out.println("ruleMap Inside DEBIT" + ruleMap);
                                    if (lst != null && lst.size() > 0) {
                                        if (objInwardClearingTO.getProdType().equals("OA") || objInwardClearingTO.getProdType().equals("AD") || objInwardClearingTO.getProdType().equals("AAD")) {
                                            sqlMap.executeUpdate("updateTODUtilized", where);
                                        }
                                    }
                                }
                            } else {
                                ruleMap.put("NORMALDEBIT", "");
                            }
                            //interbranch code
                            objLogTO.setInitiatedBranch(objInwardClearingTO.getInitiatedBranch());
                            //end
                            transModuleBased.authorizeUpdate(ruleMap, CommonUtil.convertObjToDouble(objInwardClearingTO.getAmount()));
                            amount = getTransAmount(objInwardClearingTO);
                            // Parties A/c Debitted
                            transModuleBased.updateGL(objInwardClearingTO.getAcHdId(), amount, objLogTO, ruleMap);

                            // GL Clearing Adj. A/c Credited
                            ruleMap.put(TransactionDAOConstants.TRANS_TYPE, CommonConstants.CREDIT);
//                            amount = getTransAmount(objInwardClearingTO);
                            amount = objInwardClearingTO.getAmount();
                            ruleMap.put(TransactionDAOConstants.ACCT_NO, inwardAcctHd);
                            ruleMap.put(TransactionDAOConstants.AMT, amount);
                            ruleMap.put(TransactionDAOConstants.UNCLEAR_AMT, CommonUtil.convertObjToDouble(String.valueOf(0.0)));
//                            sqlMap.executeUpdate("updateOtherBalancesGL", ruleMap);
                            transModuleBased.updateGL(inwardAcctHd, amount, objLogTO, ruleMap, true);
                            updatepassbook(objInwardClearingTO);
                        } else {
                            callRemitPayDAO(objInwardClearingTO);
                        }

                    } else {
                        transModuleBased.performShadowAdd(getRuleMap(objInwardClearingTO, 0.0, true));
                        dataMap.put(CommonConstants.STATUS, CommonConstants.STATUS_MODIFIED);
                        dataMap.put("INITIATED_BRANCH", _branchCode);
                        dataMap.put("TRANS_DT", currDt.clone());
                        sqlMap.executeUpdate("rejectInwardClearing", dataMap);
//                        HashMap wheres = new HashMap();
//                        wheres.put("TRANS_ID",dataMap.get("INWARD ID"));
//                        wheres.put("STATUS",dataMap.get("STATUS"));
//                        sqlMap.executeUpdate("rejectPassBook", wheres);
                    }
                    // Checking for Authorize Status and Bounced..
                } else if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED) && taskSelected == BOUNCE) {
                    // Exisiting status is Created or Modified
                    if (objInwardClearingTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED)
                            || objInwardClearingTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_MODIFIED)) {
                        transModuleBased.performShadowMinus(getRuleMap(objInwardClearingTO, 0.0, false));
                        //__ if Not already entered in the Bounce Table... add this record...
                        bounceReason = CommonUtil.convertObjToStr(map.get("BounceRemarks"));
                        doInwardBouncing(objInwardClearingTO, true);
                    } else {
                        transModuleBased.performShadowAdd(getRuleMap(objInwardClearingTO, 0.0, true));
                        dataMap.put(CommonConstants.STATUS, CommonConstants.STATUS_MODIFIED);
                        dataMap.put("INITIATED_BRANCH", _branchCode);
                        dataMap.put("TRANS_DT", currDt.clone());
                        sqlMap.executeUpdate("rejectInwardClearing", dataMap);
                    }
                    // Checking for Rejected Status                                        // For already bounced records - Authorization
                } else if (status.equalsIgnoreCase(CommonConstants.STATUS_REJECTED) || (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED) && bounce)) {
                    if (!bounce) {
                        // Exisiting status is Created or Modified
                        if (objInwardClearingTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED)
                                || objInwardClearingTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_MODIFIED)) {
                            transModuleBased.performShadowMinus(getRuleMap(objInwardClearingTO, 0.0, false));
                            //__ if Not already entered in the Bounce Table... add this record...
                            doInwardBouncing(objInwardClearingTO, true);
                        } else {
                            transModuleBased.performShadowAdd(getRuleMap(objInwardClearingTO, 0.0, true));
                            dataMap.put(CommonConstants.STATUS, CommonConstants.STATUS_MODIFIED);
                            dataMap.put("INITIATED_BRANCH", _branchCode);
                            dataMap.put("TRANS_DT", currDt.clone());
                            sqlMap.executeUpdate("rejectInwardClearing", dataMap);

                        }
                    } else {
                        BouncingInstrumentwiseDAO objBouncingDao = new BouncingInstrumentwiseDAO();

                        HashMap bounceMap = new HashMap();
                        bounceMap.put(CommonConstants.USER_ID, (String) execInputMap.get(CommonConstants.USER_ID));
                        bounceMap.put(CommonConstants.BRANCH_ID, (String) execInputMap.get(CommonConstants.BRANCH_ID));
                        bounceMap.put(CommonConstants.IP_ADDR, (String) execInputMap.get(CommonConstants.IP_ADDR));

                        HashMap authMap = new HashMap();
                        authMap.put(CommonConstants.AUTHORIZESTATUS, status);

                        ArrayList lstBounce = new ArrayList();
                        HashMap lstMap = new HashMap();
                        lstMap.put("INWARD ID", transID);

                        String bounceID = CommonUtil.convertObjToStr(sqlMap.executeQueryForObject("getBouncingData", transID));
                        lstMap.put("BOUNCING ID", bounceID);
                        lstMap.put("BRANCH_CODE", (String) execInputMap.get(CommonConstants.BRANCH_ID));
                        lstBounce.add(lstMap);
                        authMap.put(CommonConstants.AUTHORIZEDATA, lstBounce);

                        bounceMap.put(CommonConstants.AUTHORIZEMAP, authMap);
                        System.out.println("bounceMap : " + bounceMap);
                        execReturnMap = objBouncingDao.execute(bounceMap, false);
                    }
                }

                objLogTO.setData(dataMap.toString());
                objLogTO.setPrimaryKey(transID);
                objLogTO.setStatus(status);

                objLogDAO.addToLog(objLogTO);

                sqlMap.commitTransaction();
            } catch (Exception e) {
                e.printStackTrace();
                sqlMap.rollbackTransaction();
                throw e;
            }
        }
    }

    private InwardClearingTO getTransactionData(HashMap transMap) throws Exception {
        currDt = (Date) currDt.clone();
        Date trans_Dt = (Date) currDt.clone();
        Date tempDt = (Date) transMap.get("CLEARING_DATE");
        if (tempDt != null) {
            trans_Dt.setDate(tempDt.getDate());
            trans_Dt.setMonth(tempDt.getMonth());
            trans_Dt.setYear(tempDt.getYear());
            transMap.put("TRANS_DT", trans_Dt);
        }
        transMap.put("INITIATED_BRANCH", _branchCode);
        System.out.println("Inside getTransactionData");
        List list = (List) sqlMap.executeQueryForList("getSelectInwardClearingTO", transMap);
        return ((InwardClearingTO) list.get(0));
    }

    private Double getTransAmount(InwardClearingTO objInwardClearingTO) throws Exception {
        Double amount = objInwardClearingTO.getAmount();
        amount = new Double(amount.doubleValue() * -1);
        return amount;
    }

    public void updatepassbook(InwardClearingTO objTO) throws Exception {
        HashMap map = new HashMap();
        if (objTO.getProdType().equals("OA") || objTO.getProdType().equals("AD") || objTO.getProdType().equals("AAD")) {
            map.put("ACT_NUM", objTO.getAcctNo());
            double finamt = 0;
            HashMap where = new HashMap();
            where.put("DEBIT", objTO.getAmount());
            HashMap map1 = new HashMap();
            map1.put("ACT_NUM", objTO.getAcctNo());
            List lst1 = (List) sqlMap.executeQueryForList("getClearBalance" + objTO.getProdType(), map1);
            //                                System.out.println("@@@@@@@@@@@@$$%^^%listcredit"+lst1);
            map1 = null;
            map1 = (HashMap) lst1.get(0);
            finamt = CommonUtil.convertObjToDouble(map1.get("TOTAL_BALANCE")).doubleValue();
            where.put("BALANCE", new Double(finamt));
            where.put("TRANS_ID", objTO.getInwardId());
            where.put("ACT_NUM", objTO.getAcctNo());
            Date transdt = (Date) currDt.clone();
//            transdt.setDate(objTO.getClearingDt().getDate());
//            transdt.setMonth(objTO.getClearingDt().getMonth());
//            transdt.setYear(objTO.getClearingDt().getYear());
            where.put("TRANS_DT", transdt);
//            where.put("TRANS_DT", objTO.getClearingDt());
            where.put("PARTICULARS", objTO.getPayeeName());
            where.put("INSTRUMENT_NO1", objTO.getInstrumentNo1());
            where.put("INSTRUMENT_NO2", objTO.getInstrumentNo2());
            where.put("INST_TYPE", objTO.getInstrumentType());
            transdt = (Date) currDt.clone();
            if (objTO.getInstrumentDt() != null) {
                transdt.setDate(objTO.getInstrumentDt().getDate());
                transdt.setMonth(objTO.getInstrumentDt().getMonth());
                transdt.setYear(objTO.getInstrumentDt().getYear());
                where.put("INST_DT", transdt);
            } else {
                where.put("INST_DT", objTO.getInstrumentDt());
            }
            where.put("STATUS", objTO.getStatus());
            where.put("PBOOKFLAG", "0");
            where.put("AUTHORIZE_STATUS", CommonConstants.STATUS_AUTHORIZED);
            //                                System.out.println("@@@@@@@@@@@@$$%^^%where"+where);
            sqlMap.executeUpdate("insertPassBook", where);
            where = null;
            map1 = null;
            lst1 = null;
        }

    }
}
