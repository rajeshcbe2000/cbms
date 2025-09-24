/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OperativeTransaction.java
 *
 * Created on June 17, 2004, 3:07 PM
 */
package com.see.truetransact.serverside.transaction.common.product.otherbanksacct;

import com.see.truetransact.serverside.transaction.common.product.suspense.*;

import java.util.HashMap;

import com.ibatis.db.sqlmap.SqlMap;

import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;

// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

import org.apache.log4j.Logger;

import com.see.truetransact.serverside.transaction.common.Transaction;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;


import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;

import java.util.ArrayList;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.transaction.common.product.interbranch.InterBranchTransaction;

/**
 * OperativeTransaction.
 *
 * @author bala
 */
public class AccountswithOtherBanksTransaction extends Transaction {

    ArrayList errorList;

    public AccountswithOtherBanksTransaction() throws ServiceLocatorException {
        super();
    }

    /**
     * Updating Clear & Total Balance
     */
    public void performOtherBalanceAdd(HashMap addShadow) throws Exception {
        String act_num = (String) addShadow.get(TransactionDAOConstants.ACCT_NO);
        if (act_num != null && !act_num.equalsIgnoreCase("")) {
            if (!addShadow.containsKey(TransactionDAOConstants.UNCLEAR_AMT)) {
                addShadow.put(TransactionDAOConstants.UNCLEAR_AMT, new Double(0));
            }
            System.out.println("#### addShadow : " + addShadow);
            // Updates other Balances  (Clear balance and Total Balance)
            //  if ((CommonUtil.convertObjToStr(addShadow.get(TransactionDAOConstants.TRANS_TYPE))).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
            addShadow.put(TransactionDAOConstants.AMT,
                    new Double(-1 * Double.parseDouble(addShadow.get(TransactionDAOConstants.AMT).toString())));
            //  }
            sqlMap.executeUpdate("updateOtherBalancesAB", addShadow);

        }
        addShadow = null;
    }

    /**
     * Updating Clear & Total Balance
     */
    public void performOtherBalanceMinus(HashMap addShadow) throws Exception {
        printLog("performOtherBalanceMinus:before:" + addShadow);

        String act_num = (String) addShadow.get(TransactionDAOConstants.ACCT_NO);
        if (act_num != null && !act_num.equalsIgnoreCase("")) {

            if (addShadow.containsKey(TransactionDAOConstants.UNCLEAR_AMT)) {
                addShadow.put(TransactionDAOConstants.UNCLEAR_AMT,
                        new Double(-1 * CommonUtil.convertObjToDouble(addShadow.get(TransactionDAOConstants.UNCLEAR_AMT)).doubleValue()));
            }

            if (!addShadow.containsKey(TransactionDAOConstants.UNCLEAR_AMT)) {
                addShadow.put(TransactionDAOConstants.UNCLEAR_AMT, new Double(0));
            }


            addShadow.put(TransactionDAOConstants.AMT,
                    new Double(-1 * CommonUtil.convertObjToDouble(addShadow.get(TransactionDAOConstants.AMT)).doubleValue()));
            printLog("performOtherBalanceMinus:after:" + addShadow);
            // Updates other Balances  (Clear balance and Total Balance)
            sqlMap.executeUpdate("updateOtherBalancesAB", addShadow);

        }
        addShadow = null;
    }

    /**
     * Updating Shadow debit or shadow credit based on the parameter
     */
    public void performShadowAdd(HashMap addShadow) throws Exception {
        printLog("performShadowAdd:before:" + addShadow);
        printLog("performShadowAdd:before:" + addShadow.get("AMOUNT").getClass());
        String act_num = (String) addShadow.get(TransactionDAOConstants.ACCT_NO);
        if (act_num != null && !act_num.equalsIgnoreCase("")) {

            if (addShadow.containsKey("AUTHORIZE_BY") && addShadow.get("AUTHORIZE_BY") != null) {
                if (!CommonUtil.convertObjToStr(addShadow.get("AUTHORIZE_BY")).equalsIgnoreCase("TTSYSTEM")) // sqlMap.executeUpdate("updateLastTransDateAB", minusMap);
                {
                    printLog("performShadowAdd:after:" + addShadow);
                }
            }


            if ((CommonUtil.convertObjToStr(addShadow.get(TransactionDAOConstants.TRANS_TYPE))).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
                addShadow.put(TransactionDAOConstants.AMT,
                        new Double(-1 * Double.parseDouble(addShadow.get(TransactionDAOConstants.AMT).toString())));
                sqlMap.executeUpdate("updateShadowDebitAB", addShadow);//changed by jiv
            } else {
                addShadow.put(TransactionDAOConstants.AMT,
                        new Double(Double.parseDouble(addShadow.get(TransactionDAOConstants.AMT).toString())));//changed by jiv
                sqlMap.executeUpdate("updateShadowCreditAB", addShadow);
                //                 doIntTransaction(minusMap);   //for transfer int receivable to received
            }

        }
        addShadow = null;

    }

    /**
     * Updating Shadow debit or shadow credit based on the parameter
     */
    public void performShadowMinus(HashMap minusMap) throws Exception {
        printLog("performShadowMinus:before:" + minusMap);
        printLog("performShadowMinus:before:" + minusMap.get("AMOUNT").getClass());
        String act_num = (String) minusMap.get(TransactionDAOConstants.ACCT_NO);
        if (act_num != null && !act_num.equalsIgnoreCase("")) {

            if (minusMap.containsKey("AUTHORIZE_BY") && minusMap.get("AUTHORIZE_BY") != null) {
                if (!CommonUtil.convertObjToStr(minusMap.get("AUTHORIZE_BY")).equalsIgnoreCase("TTSYSTEM")) // sqlMap.executeUpdate("updateLastTransDateAB", minusMap);
                {
                    printLog("performShadowMinus:after:" + minusMap);
                }
            }

            minusMap.put(TransactionDAOConstants.AMT,
                    new Double(-1 * Double.parseDouble(minusMap.get(TransactionDAOConstants.AMT).toString())));
            if ((CommonUtil.convertObjToStr(minusMap.get(TransactionDAOConstants.TRANS_TYPE))).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
                sqlMap.executeUpdate("updateShadowDebitAB", minusMap);
            } else {
                sqlMap.executeUpdate("updateShadowCreditAB", minusMap);
                //                 doIntTransaction(minusMap);   //for transfer int recivable to received
            }

        }
        minusMap = null;
    }

    /**
     * Updating Available Balance
     */
    public void updateAvailableBalance(HashMap updateMap) throws Exception {
        printLog("updateAvailableBalance:" + updateMap);

        String act_num = (String) updateMap.get(TransactionDAOConstants.ACCT_NO);
        if (act_num != null && !act_num.equalsIgnoreCase("")) {

            if (!updateMap.containsKey(TransactionDAOConstants.UNCLEAR_AMT)) {
                updateMap.put(TransactionDAOConstants.UNCLEAR_AMT, new Double(0));
            }
          //  System.out.println("#### updateMap : " + updateMap);
            sqlMap.executeUpdate("updateAvailBalanceAB", updateMap);

        }
        updateMap = null;
    }

    public void validateRules(HashMap validateMap, boolean isException) throws Exception {
    }

    /**
     * Method is overridden to add IBR.
     */
    public void updateGL(String acctHead, Double amount, LogTO objLogTO, HashMap ruleMap) throws Exception {
        //super.updateGL(acctHead, amount, objLogTO, ruleMap);
        String transOwner = CommonUtil.convertObjToStr(ruleMap.get(TransactionDAOConstants.INITIATED_BRANCH));
        String acctOwner = CommonUtil.convertObjToStr(ruleMap.get(TransactionDAOConstants.BRANCH_CODE));
        System.out.println("transOwner##" + transOwner + "acctOwner$$$" + acctOwner);
        System.out.println("ruleMap operative" + ruleMap);
        if (ruleMap.containsKey("IS_INTER_BRANCH_TRANS")
                && new Boolean(CommonUtil.convertObjToStr(ruleMap.get("IS_INTER_BRANCH_TRANS"))).booleanValue()) {
            transOwner = "";
        } else {
            if (transOwner.length() == 0) {
                throw new TTException("Initiated Brach Should not null ");
            }
        }
        /**
         * Inter Branch Transaction
         */
        if (!transOwner.equals(acctOwner)) {
            InterBranchTransaction objInterBranch = new InterBranchTransaction(transOwner, acctOwner);
            objInterBranch.doInterBranchTransaction(acctHead, amount, objLogTO, ruleMap);
            ruleMap.put("INTER_BRANCH_TRANS", new Boolean(true));
        } else {
            super.updateGL(acctHead, amount, objLogTO, ruleMap);
        }
    }

    /**
     * Getter for property errorList.
     *
     * @return Value of property errorList.
     */
    public java.util.ArrayList getErrorList() {
        return errorList;
    }
    /*
     * private String getIBRAcctHead() throws Exception { HashMap transMap =
     * (HashMap) sqlMap.executeQueryForObject("getSelectTransParams", null);
     * return CommonUtil.convertObjToStr(transMap.get("IBR_AC_HD"));
    }
     */
}