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
package com.see.truetransact.serverside.transaction.common.product.suspense;

import java.util.HashMap;

import com.ibatis.db.sqlmap.SqlMap;

import com.see.truetransact.businessrule.RuleContext;
import com.see.truetransact.businessrule.RuleEngine;
import com.see.truetransact.businessrule.transaction.LimitCheckingRule;
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
public class SuspenseTransaction extends Transaction {

    ArrayList errorList;

    public SuspenseTransaction() throws ServiceLocatorException {
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
            sqlMap.executeUpdate("updateOtherBalancesSA", addShadow);

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
            sqlMap.executeUpdate("updateOtherBalancesSA", addShadow);

        }
        addShadow = null;
    }

    /**
     * Updating Shadow debit or shadow credit based on the parameter
     */
    public void performShadowAdd(HashMap addShadow) throws Exception {
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
                if (!CommonUtil.convertObjToStr(minusMap.get("AUTHORIZE_BY")).equalsIgnoreCase("TTSYSTEM")) {
                    sqlMap.executeUpdate("updateLastTransDateSA", minusMap);
                }
            }
            printLog("performShadowMinus:after:" + minusMap);
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
            System.out.println("#### updateMap : " + updateMap);
            sqlMap.executeUpdate("updateAvailBalanceSA", updateMap);

        }
        updateMap = null;
    }

    public void validateRules(HashMap validateMap, boolean isException) throws Exception {
                // Validation only for Debit
        String act_closing = "";
        String act_num = (String) validateMap.get(TransactionDAOConstants.ACCT_NO);
        validateMap.put(TransactionDAOConstants.PROD_TYPE, TransactionFactory.SUSPENSE);
        //AddressVerificationRule avr = new AddressVerificationRule();

        System.out.println("validateMap: SuspenseTransaction" + validateMap);

//        if ((validateMap.containsKey("ACCOUNT_CLOSING")) && (validateMap.get("ACCOUNT_CLOSING") != null)) {
//            act_closing = (CommonUtil.convertObjToStr(validateMap.get("ACCOUNT_CLOSING")));
//        }

        RuleEngine engine = new RuleEngine();
        RuleContext context = new RuleContext();

        errorList = new ArrayList();
        if (act_num != null && !act_num.equalsIgnoreCase("") && !isException) {
            if (!validateMap.containsKey("FORPROCCHARGE")) {
                context.addRule(new LimitCheckingRule());
            }
//            if (!act_closing.equals("ACCOUNT_CLOSING")) {
//                context.addRule(new DeathMarkedRule());
//            }
            //context.addRule(new SuspiciousConfigRule());
        }

        if (((String) validateMap.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
//            if (act_num != null && !act_num.equalsIgnoreCase("") && !isException) {
//                context.addRule(avr);
//                context.addRule(new ConfirmThanxRule());
//            }

            //context.addRule(new DateCheckingRule());
            //To apply business rule
//            if (validateMap.get(TransactionDAOConstants.INSTRUMENT_TYPE) != null) {
//                if (((String) validateMap.get(TransactionDAOConstants.INSTRUMENT_TYPE)).equalsIgnoreCase(TransactionDAOConstants.DD)) {
//                    context.addRule(new DraftInstrumentRule());
//                } else if (((String) validateMap.get(TransactionDAOConstants.INSTRUMENT_TYPE)).equalsIgnoreCase(TransactionDAOConstants.CHEQUE)) {
//                    context.addRule(new ChequeInstrumentRule());
//                } else if (((String) validateMap.get(TransactionDAOConstants.INSTRUMENT_TYPE)).equalsIgnoreCase(TransactionDAOConstants.WITHDRAWLSLIP)) {
//                    context.addRule(new WithdrawlSlipRule());
//                } else if (((String) validateMap.get(TransactionDAOConstants.INSTRUMENT_TYPE)).equalsIgnoreCase(TransactionDAOConstants.ECS)) {
//                    context.addRule(new EcsRule());
//                }
//            }
        }
        System.out.println(validateMap + ":  SuspenseTransaction TRANSACTION####" + context);
        ArrayList list = (ArrayList) engine.validateAll(context, validateMap);
        //errorList.add(avr.getErrorMessage());
        if (list != null) {
            TTException t = new TTException();
            HashMap exception = new HashMap();
            exception.put(CommonConstants.EXCEPTION_LIST, list);
            exception.put(CommonConstants.CONSTANT_CLASS, TransactionDAOConstants.TRANS_RULE_MAP);
            t.setExceptionHashMap(exception);
            System.out.println("Exception List : " + list);
            errorList = list;
            throw new TTException(exception);
        }

        context = null;
        engine = null;
    }

    /**
     * Method is overridden to add IBR.
     */
    public void updateGL(String acctHead, Double amount, LogTO objLogTO, HashMap ruleMap) throws Exception {
       // super.updateGL(acctHead, amount, objLogTO, ruleMap);
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