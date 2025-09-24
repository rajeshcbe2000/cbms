/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * Transaction.java
 *
 * Created on June 17, 2004, 3:07 PM
 */
package com.see.truetransact.serverside.transaction.common;

import com.see.truetransact.businessrule.RuleContext;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;

import java.util.HashMap;

//For GL
import com.see.truetransact.transferobject.transaction.common.product.gl.GLTO;
import com.see.truetransact.serverside.transaction.common.product.gl.GLUpdateDAO;

import com.see.truetransact.transferobject.common.log.LogTO;

import com.ibatis.db.sqlmap.SqlMap;
import org.apache.log4j.Logger;

import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.commonutil.CommonUtil;

/**
 *
 * @author bala
 */
public abstract class Transaction {

    /**
     * <PRE>
     *
     * -------------------------------------------------------------------------------------------
     *  Status    | Default Action    | Authorize             | Reject            | Exception
     * -------------------------------------------------------------------------------------------
     *
     *  New/        ShadowAdd &         updateAvailableBalance  ShadowMinus         Status Change
     *  Edit                             ShadowMinus
     *
     *  Status      CREATED/MODIFIED    AUTHORIZED              REJECTED            EXCEPTION
     *
     * -------------------------------------------------------------------------------------------
     *
     *  Delete      ShadowMinus                                 ShadowAdd               "
     *
     *
     *  Status      DELETED             AUTHORIZED              REJECTED            EXCEPTION
     *
     * -------------------------------------------------------------------------------------------
     * </PRE>
     */
    protected SqlMap sqlMap = null;
    protected final Logger log = Logger.getLogger(Transaction.class);

    /**
     * Creates a new instance of Transaction
     */
    public Transaction() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public void printLog(String str) {
        System.out.println(str);
        str = null;
    }

    /**
     * Adding Shadow Balanace
     *
     * This will be called in the time of Insert & Update
     */
    public abstract void performShadowAdd(HashMap addShadow) throws Exception;

    /**
     * Subtract Shadow Balanace
     *
     * This will be called in the time of Delete & Reject Operations
     */
    public abstract void performShadowMinus(HashMap minusMap) throws Exception;

    /**
     * Other Balance Updation
     *
     * This will be called in the time of Insert & Update
     */
    public abstract void performOtherBalanceAdd(HashMap addShadow) throws Exception;

    /**
     * Other Balance Updation
     *
     * This will be called in the time of Delete and Reject
     */
    public abstract void performOtherBalanceMinus(HashMap minusMap) throws Exception;

    /**
     * Other Balance Updation
     *
     * This will be called in the time of Authorize
     */
    public abstract void updateAvailableBalance(HashMap updateMap) throws Exception;

    /**
     * Specific Rules based on the Modulewise.
     *
     * This method will be called in Insert as well as update
     */
    public abstract void validateRules(HashMap validateMap, boolean isException) throws Exception;

//    public abstract java.util.ArrayList getErrorList() throws Exception;
    public void authorizeUpdate(HashMap authorizeMap, Double amount) throws Exception {
      //  log.info("updateAvailableBalance ... after getRuleMap updateMap : " + authorizeMap);
        authorizeMap.put(TransactionDAOConstants.AMT, amount);

        HashMap updateMap = mapAmount(authorizeMap, 0.0, true);
        log.info("updateAvailableBalance ... after getRuleMap updateMap : " + updateMap);

        String instType = (String) authorizeMap.get(TransactionDAOConstants.INSTRUMENT_TYPE);
        String actNum = (String) authorizeMap.get(TransactionDAOConstants.ACCT_NO);

        if (instType != null) {
            if (instType.equals(TransactionDAOConstants.DD)) {
                if (!actNum.equalsIgnoreCase("")
                        && !((String) authorizeMap.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
                    performOtherBalanceAdd(updateMap);
                    updateAvailableBalance(updateMap);
                    // To Update the Status in Remit_issue...
                    sqlMap.executeUpdate("getDraftCleared", updateMap);
                    performShadowMinus(mapAmount(authorizeMap, 0.0, false));
                } else {
                    sqlMap.rollbackTransaction();
                }
            } else if (instType.equals(TransactionDAOConstants.CHEQUE)) {
                performOtherBalanceAdd(updateMap);
                updateAvailableBalance(updateMap);
                performShadowMinus(mapAmount(authorizeMap, 0.0, false));
            } else {
                performOtherBalanceAdd(updateMap);
                updateAvailableBalance(updateMap);
                performShadowMinus(mapAmount(authorizeMap, 0.0, false));
            }
        } else if (actNum != null && !actNum.equalsIgnoreCase("")) {
            performOtherBalanceAdd(updateMap);
            updateAvailableBalance(updateMap);
            performShadowMinus(mapAmount(authorizeMap, 0.0, false));
        }
    }

    public void updateGL(String acctHead, Double amount, LogTO objLogTO, HashMap ruleMap) throws Exception {
        updateGL(acctHead, amount, objLogTO, ruleMap, false);
//        log.info("updateGL ... acctHead" + acctHead + "...amount " + amount);
//        GLTO objGLTO = new GLTO();
//        objGLTO.setAcHdId(acctHead);
//        //objGLTO.setOpnBal (new Double (getTxtOpnBal()));
//        objGLTO.setCurBal(amount);
//        objGLTO.setBranchCode(objLogTO.getBranchId());
//
//        GLUpdateDAO objGLUpdateDao = new GLUpdateDAO();
//        objGLUpdateDao.updateGL(objGLTO, ruleMap);
//        
//        objGLTO = null;
//        objGLUpdateDao = null;
    }

    public void updateGL(String acctHead, Double amount, LogTO objLogTO, HashMap ruleMap, boolean isReference) throws Exception {
        log.info("updateGL ... acctHead" + acctHead + "...amount " + amount + " initiatedBranch : " + objLogTO.getInitiatedBranch() + " ruleMap : " + ruleMap);
        GLTO objGLTO = new GLTO();
        //interbranch code
        objGLTO.setBranchCode(objLogTO.getBranchId());  //-- after this line just add below code
        objGLTO.setInitiatedBranch(objLogTO.getInitiatedBranch());
        //end
        objGLTO.setAcHdId(acctHead);
        //objGLTO.setOpnBal (new Double (getTxtOpnBal()));
        objGLTO.setCurBal(amount);
        objGLTO.setBranchCode(objLogTO.getBranchId());
        if(ruleMap.containsKey("IBR_HIERARCHY")){
            objGLTO.setIbrHierarchy(CommonUtil.convertObjToStr(ruleMap.get("IBR_HIERARCHY")));
        }
        GLUpdateDAO objGLUpdateDao = new GLUpdateDAO();
        objGLUpdateDao.updateGL(objGLTO, ruleMap, isReference);

        objGLTO = null;
        objGLUpdateDao = null;
    }

    private HashMap mapAmount(HashMap ruleMap, double prevAmount, boolean makeNegative) throws Exception {
        HashMap returnMap = new HashMap();
        returnMap.putAll(ruleMap);
        double amount = (CommonUtil.convertObjToDouble(ruleMap.get(TransactionDAOConstants.AMT))).doubleValue();

        if (((String) ruleMap.get(TransactionDAOConstants.TRANS_TYPE)).equals(TransactionDAOConstants.DEBIT) && makeNegative) {
            amount = -amount + prevAmount;
        } else {
            amount -= prevAmount;
        }
        returnMap.put(TransactionDAOConstants.AMT, CommonUtil.convertObjToDouble(String.valueOf(amount)));
        return returnMap;
    }
}
