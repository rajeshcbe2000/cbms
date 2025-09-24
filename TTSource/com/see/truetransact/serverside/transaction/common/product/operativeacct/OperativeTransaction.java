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
package com.see.truetransact.serverside.transaction.common.product.operativeacct;

import com.see.truetransact.businessrule.RuleContext;
import com.see.truetransact.businessrule.RuleEngine;
import com.see.truetransact.businessrule.transaction.*;
import com.see.truetransact.businessrule.transaction.suspiciousconfig.SuspiciousConfigRule;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.transaction.common.Transaction;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.transaction.common.product.interbranch.InterBranchTransaction;
import com.see.truetransact.transferobject.common.log.LogTO;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * OperativeTransaction.
 *
 * @author bala
 */
public class OperativeTransaction extends Transaction {

    ArrayList errorList;

    public OperativeTransaction() throws ServiceLocatorException {
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
            sqlMap.executeUpdate("updateOtherBalancesOA", addShadow);

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
            sqlMap.executeUpdate("updateOtherBalancesOA", addShadow);

        }
        addShadow = null;
    }

    /**
     * Updating Shadow debit or shadow credit based on the parameter
     */
    public void performShadowAdd(HashMap addShadow) throws Exception {
        String act_num = (String) addShadow.get(TransactionDAOConstants.ACCT_NO);
        if (act_num != null && !act_num.equalsIgnoreCase("")) {

            if (((String) addShadow.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
                addShadow.put(TransactionDAOConstants.AMT,
                        new Double(-1 * CommonUtil.convertObjToDouble(addShadow.get(TransactionDAOConstants.AMT)).doubleValue()));
                sqlMap.executeUpdate("updateShadowDebitOA", addShadow);
            } else {
                try {
                    printLog("addShadow:" + addShadow);
                    sqlMap.executeUpdate("updateShadowCreditOA", addShadow);
                } catch (Exception e) {
                    printLog("Exception from performShadowAdd:" + addShadow);
                    e.printStackTrace();
                }
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

            minusMap.put(TransactionDAOConstants.AMT,
                    new Double(-1 * Double.parseDouble(minusMap.get(TransactionDAOConstants.AMT).toString())));
            if (((String) minusMap.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
                sqlMap.executeUpdate("updateShadowDebitOA", minusMap);
            } else {
                try {
                    printLog("minusMap:" + minusMap);
                    sqlMap.executeUpdate("updateShadowCreditOA", minusMap);
                } catch (Exception e) {
                    printLog("Exception from performShadowMinus:" + minusMap);
                    printLog("Exception from performShadowMinus:Class:" + minusMap.get("AMOUNT").getClass());
                    e.printStackTrace();
                }
            }
            if (minusMap.containsKey("AUTHORIZE_BY") && minusMap.get("AUTHORIZE_BY") != null) {
                if (!CommonUtil.convertObjToStr(minusMap.get("AUTHORIZE_BY")).equalsIgnoreCase("TTSYSTEM")) {
                    sqlMap.executeUpdate("updateLastTransDateOA", minusMap);
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
            //System.out.println("#### updateMap : " + updateMap);
            sqlMap.executeUpdate("updateAvailBalanceOA", updateMap);

        }
        updateMap = null;
    }

    public void validateRules(HashMap validateMap, boolean isException) throws Exception {
        // Validation only for Debit
        String act_closing = "";
        String act_num = (String) validateMap.get(TransactionDAOConstants.ACCT_NO);
        validateMap.put(TransactionDAOConstants.PROD_TYPE, TransactionFactory.OPERATIVE);
        AddressVerificationRule avr = new AddressVerificationRule();

        System.out.println("validateMap:" + validateMap);

        if ((validateMap.containsKey("ACCOUNT_CLOSING")) && (validateMap.get("ACCOUNT_CLOSING") != null)) {
            act_closing = (CommonUtil.convertObjToStr(validateMap.get("ACCOUNT_CLOSING")));
        }

        RuleEngine engine = new RuleEngine();
        RuleContext context = new RuleContext();

        errorList = new ArrayList();
        if (act_num != null && !act_num.equalsIgnoreCase("") && !isException) {
            if (!validateMap.containsKey("FORPROCCHARGE")) {
                context.addRule(new LimitCheckingRule());
            }
           //added by Rishad For Account Validate 10/12/2019
            context.addRule(new AccountCheckingRule());
            if (!act_closing.equals("ACCOUNT_CLOSING")) {
                context.addRule(new DeathMarkedRule());
            }
            context.addRule(new SuspiciousConfigRule());
            
        }

        if (((String) validateMap.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
            if (act_num != null && !act_num.equalsIgnoreCase("") && !isException) {
                context.addRule(avr);
                context.addRule(new ConfirmThanxRule());
            }

            context.addRule(new DateCheckingRule());
            //To apply business rule
            if (validateMap.get(TransactionDAOConstants.INSTRUMENT_TYPE) != null) {
                if (((String) validateMap.get(TransactionDAOConstants.INSTRUMENT_TYPE)).equalsIgnoreCase(TransactionDAOConstants.DD)) {
                    context.addRule(new DraftInstrumentRule());
                } else if (((String) validateMap.get(TransactionDAOConstants.INSTRUMENT_TYPE)).equalsIgnoreCase(TransactionDAOConstants.CHEQUE)) {
                    context.addRule(new ChequeInstrumentRule());
                } else if (((String) validateMap.get(TransactionDAOConstants.INSTRUMENT_TYPE)).equalsIgnoreCase(TransactionDAOConstants.WITHDRAWLSLIP)) {
                    context.addRule(new WithdrawlSlipRule());
                } else if (((String) validateMap.get(TransactionDAOConstants.INSTRUMENT_TYPE)).equalsIgnoreCase(TransactionDAOConstants.ECS)) {
                    context.addRule(new EcsRule());
                }
            }
        }
        System.out.println(validateMap + ":  OPERATIVE TRANSACTION####" + context);
        ArrayList list = (ArrayList) engine.validateAll(context, validateMap);
        errorList.add(avr.getErrorMessage());
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
//        String transOwner = objLogTO.getBranchId();
//        String acctOwner = objLogTO.getBranchId();
        String transOwner = CommonUtil.convertObjToStr(ruleMap.get(TransactionDAOConstants.INITIATED_BRANCH));
        String acctOwner = CommonUtil.convertObjToStr(ruleMap.get(TransactionDAOConstants.BRANCH_CODE));
        System.out.println("transOwner##" + transOwner + "acctOwner$$$" + acctOwner);
        System.out.println("ruleMap operative" + ruleMap);
//        if (ruleMap != null && !CommonUtil.convertObjToStr(ruleMap.get(TransactionDAOConstants.ACCT_NO)).equals("")) {
//            // get the branch for the a/c and override acctOwner variable
//            String acctNo = CommonUtil.convertObjToStr(ruleMap.get(TransactionDAOConstants.ACCT_NO));
//            acctOwner = CommonUtil.convertObjToStr(sqlMap.executeQueryForObject("getBranchOA", acctNo));
//        }


        if (ruleMap.containsKey("IS_INTER_BRANCH_TRANS")
                && new Boolean(CommonUtil.convertObjToStr(ruleMap.get("IS_INTER_BRANCH_TRANS"))).booleanValue()) {
            transOwner = "";
        } else {
            if (transOwner.length() == 0) {
                throw new TTException("Initiated Brach Should not null ");
            }
        }
//      else      
//        System.out.println("#$#$#$ transOwner : "+transOwner+"  /   acctOwner : "+acctOwner);
        /**
         * Inter Branch Transaction
         */
        if (!transOwner.equals(acctOwner)) {
//            String transOwner = objLogTO.getBranchId();
//            String acctOwner = ServerConstants.HO;

            InterBranchTransaction objInterBranch = new InterBranchTransaction(transOwner, acctOwner);
            objInterBranch.doInterBranchTransaction(acctHead, amount, objLogTO, ruleMap);
            ruleMap.put("INTER_BRANCH_TRANS", new Boolean(true));

//        if (!transOwner.equalsIgnoreCase(acctOwner)) {
//            InterBranchTransaction objInterBranch = new InterBranchTransaction(transOwner, acctOwner);
//            objInterBranch.doInterBranchTransaction (acctHead, amount, objLogTO, ruleMap);
//            ruleMap.put("INTER_BRANCH_TRANS",new Boolean(true)); // This line added by Rajesh

            /*
             * String ibrAcHd = getIBRAcctHeasd(); boolean isGL =
             * CommonUtil.convertObjToStr(ruleMap.get(TransactionDAOConstants.PROD_TYPE)).equals(TransactionFactory.GL);
             * if (!isGL) { objLogTO.setBranchId(acctOwner); }
             *
             * super.updateGL(acctHead, amount, objLogTO, ruleMap);
             *
             * if (!isGL) { objLogTO.setBranchId(acctOwner); }              *
             * String ruleTransType =
             * CommonUtil.convertObjToStr(ruleMap.get(TransactionDAOConstants.TRANS_TYPE));
             * if (ruleTransType.equals(CommonConstants.CREDIT)) {
             * ruleMap.put(TransactionDAOConstants.TRANS_TYPE,
             * CommonConstants.DEBIT); } else {
             * ruleMap.put(TransactionDAOConstants.TRANS_TYPE,
             * CommonConstants.CREDIT); }
             *
             * super.updateGL(ibrAcHd, amount, objLogTO, ruleMap, true);
             */

//        } 

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