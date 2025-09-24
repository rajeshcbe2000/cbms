/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * RemittanceTransaction.java
 *
 * Created on Nov 13, 2005, 2:07 PM
 */
package com.see.truetransact.serverside.transaction.common.product.remittance;

import com.see.truetransact.serverside.transaction.common.product.loan.LoanTransaction;

import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;

import com.see.truetransact.serverside.transaction.common.Transaction;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;

import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;

// For the Business Rules...
import com.see.truetransact.businessrule.RuleContext;
import com.see.truetransact.businessrule.RuleEngine;

import com.see.truetransact.businessrule.termloan.LoanLimitCheckingRule;
import com.see.truetransact.businessrule.transaction.ChequeInstrumentRule;
import com.see.truetransact.businessrule.transaction.DraftInstrumentRule;
import com.see.truetransact.businessrule.transaction.DateCheckingRule;
import com.see.truetransact.businessrule.transaction.DeathMarkedRule;
import com.see.truetransact.businessrule.transaction.WithdrawlSlipRule;
import com.see.truetransact.businessrule.transaction.suspiciousconfig.SuspiciousConfigRule;
import com.see.truetransact.businessrule.transaction.AddressVerificationRule;
import com.see.truetransact.businessrule.transaction.ConfirmThanxRule;
import com.see.truetransact.businessrule.advances.LimitCheckingRule;


import com.see.truetransact.commonutil.TTException;

import java.util.HashMap;
import java.util.ArrayList;

/**
 *
 * @author 152721
 */
public class RemittanceTransaction extends Transaction {

    /**
     * Creates a new instance of AdvancesTransaction
     */
    public RemittanceTransaction() throws ServiceLocatorException {
        super();
    }

    /**
     * Updating Clear & Total Balance
     */
    public void performOtherBalanceAdd(HashMap addShadow) throws Exception {
        String act_num = CommonUtil.convertObjToStr(addShadow.get(TransactionDAOConstants.ACCT_NO));
        if (act_num != null && !act_num.equalsIgnoreCase("")) {
            if (!addShadow.containsKey(TransactionDAOConstants.UNCLEAR_AMT)) {
                addShadow.put(TransactionDAOConstants.UNCLEAR_AMT, new Double(0));
            }

            // Updates other Balances  (Clear balance and Total Balance)
            sqlMap.executeUpdate("updateOtherBalancesTL", addShadow);

        }
        addShadow = null;
    }

    /**
     * Updating Clear & Total Balance
     */
    public void performOtherBalanceMinus(HashMap addShadow) throws Exception {
        printLog("performOtherBalanceMinus:before:" + addShadow);

        String act_num = CommonUtil.convertObjToStr(addShadow.get(TransactionDAOConstants.ACCT_NO));
        if (act_num != null && !act_num.equalsIgnoreCase("")) {

            if (addShadow.containsKey(TransactionDAOConstants.UNCLEAR_AMT)) {
                addShadow.put(TransactionDAOConstants.UNCLEAR_AMT,
                        new Double(-1 * Double.parseDouble(addShadow.get(TransactionDAOConstants.UNCLEAR_AMT).toString())));
            }

            if (!addShadow.containsKey(TransactionDAOConstants.UNCLEAR_AMT)) {
                addShadow.put(TransactionDAOConstants.UNCLEAR_AMT, new Double(0));
            }


            addShadow.put(TransactionDAOConstants.AMT,
                    new Double(-1 * Double.parseDouble(addShadow.get(TransactionDAOConstants.AMT).toString())));
            printLog("performOtherBalanceMinus:after:" + addShadow);
            // Updates other Balances  (Clear balance and Total Balance)
            sqlMap.executeUpdate("updateOtherBalancesTL", addShadow);

        }
        addShadow = null;
    }

    /**
     * Updating Shadow debit or shadow credit based on the parameter
     */
    public void performShadowAdd(HashMap addShadow) throws Exception {
        //        printLog ("performShadowAdd:before:" + addShadow);
        String act_num = (String) addShadow.get(TransactionDAOConstants.ACCT_NO);
        if (act_num != null && !act_num.equalsIgnoreCase("")) {

            if ((CommonUtil.convertObjToStr(addShadow.get(TransactionDAOConstants.TRANS_TYPE))).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
                addShadow.put(TransactionDAOConstants.AMT,
                        new Double(-1 * Double.parseDouble(addShadow.get(TransactionDAOConstants.AMT).toString())));
                sqlMap.executeUpdate("updateShadowDebitTL", addShadow);
            } else {
                sqlMap.executeUpdate("updateShadowCreditTL", addShadow);
            }
        }
        addShadow = null;
    }

    /**
     * Updating Shadow debit or shadow credit based on the parameter
     */
    public void performShadowMinus(HashMap minusMap) throws Exception {
        printLog("performShadowMinus:before:" + minusMap);

        String act_num = (String) minusMap.get(TransactionDAOConstants.ACCT_NO);
        if (act_num != null && !act_num.equalsIgnoreCase("")) {

            minusMap.put(TransactionDAOConstants.AMT,
                    new Double(-1 * Double.parseDouble(minusMap.get(TransactionDAOConstants.AMT).toString())));
            if ((CommonUtil.convertObjToStr(minusMap.get(TransactionDAOConstants.TRANS_TYPE))).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
                sqlMap.executeUpdate("updateShadowDebitTL", minusMap);
            } else {
                sqlMap.executeUpdate("updateShadowCreditTL", minusMap);
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

            sqlMap.executeUpdate("updateAvailBalanceTL", updateMap);
        }
        updateMap = null;
    }

    public void validateRules(HashMap validateMap, boolean isException) throws Exception {
        String act_num = CommonUtil.convertObjToStr(validateMap.get(TransactionDAOConstants.ACCT_NO));

        validateMap.put(TransactionDAOConstants.PROD_TYPE, TransactionFactory.ADVANCES);

        RuleEngine engine = new RuleEngine();
        RuleContext context = new RuleContext();
        ArrayList list = new ArrayList();

        if (act_num != null && !act_num.equalsIgnoreCase("") && !isException) {
            context.addRule(new DeathMarkedRule());
            context.addRule(new SuspiciousConfigRule());
        }

        if (CommonUtil.convertObjToStr(validateMap.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
            // Validation only for Debit
            if (act_num != null && !act_num.equalsIgnoreCase("") && !isException) {
                context.addRule(new LimitCheckingRule());
                context.addRule(new AddressVerificationRule());
                context.addRule(new ConfirmThanxRule());
            }

            //To apply business rule
            if (((String) validateMap.get(TransactionDAOConstants.INSTRUMENT_TYPE)).equalsIgnoreCase(TransactionDAOConstants.DD)) {
                context.addRule(new DraftInstrumentRule());
                context.addRule(new DateCheckingRule());
            } else if (((String) validateMap.get(TransactionDAOConstants.INSTRUMENT_TYPE)).equalsIgnoreCase(TransactionDAOConstants.CHEQUE)) {
                context.addRule(new ChequeInstrumentRule());
                context.addRule(new DateCheckingRule());
            } else if (((String) validateMap.get(TransactionDAOConstants.INSTRUMENT_TYPE)).equalsIgnoreCase(TransactionDAOConstants.WITHDRAWLSLIP)) {
                context.addRule(new WithdrawlSlipRule());
            }
        }

        list = (ArrayList) engine.validateAll(context, validateMap);

//        else if(CommonUtil.convertObjToStr(validateMap.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.CREDIT)) {
//            // Validation only for Credit
//            if(act_num!=null && !act_num.equalsIgnoreCase("") && !isException){
//                context.addRule(new LoanLimitCheckingRule());
//                context.addRule(new LoanRepaymentRule());
//            }
//            // Validate the rules here
//            list = (ArrayList) engine.validateAll(context,validateMap);
//        }

        context = null;
        engine = null;

        if (list != null) {
            System.out.println("Exception List in AdvancesTransaction: " + list);
            HashMap exception = new HashMap();
            exception.put(CommonConstants.EXCEPTION_LIST, list);
            exception.put(CommonConstants.CONSTANT_CLASS, TransactionDAOConstants.TRANS_RULE_MAP);
            System.out.println("Exception List : " + list);

            throw new TTException(exception);
        }
    }
}
