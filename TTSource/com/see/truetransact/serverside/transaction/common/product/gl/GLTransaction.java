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
package com.see.truetransact.serverside.transaction.common.product.gl;

import java.util.HashMap;
import java.util.ArrayList;
import com.see.truetransact.serverexception.ServiceLocatorException;

// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

import org.apache.log4j.Logger;

import com.see.truetransact.serverside.transaction.common.Transaction;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;

// For Shares
import com.see.truetransact.serverside.transaction.common.product.interbranch.InterBranchTransaction;
import com.see.truetransact.serverutil.ServerConstants;

// For the Business Rules...
import com.see.truetransact.businessrule.RuleContext;
import com.see.truetransact.businessrule.RuleEngine;

import com.see.truetransact.businessrule.transaction.DateCheckingRule;
import com.see.truetransact.businessrule.generalledger.GLLimitCheckingRule;
import com.see.truetransact.businessrule.generalledger.GLParamRule;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;

import com.see.truetransact.transferobject.transaction.common.product.gl.GLTO;

/**
 * GLTransaction.
 *
 * @author bala
 */
public class GLTransaction extends Transaction {

    public GLTransaction() throws ServiceLocatorException {
        super();
    }

    /**
     * Updating Clear & Total Balance
     */
    public void performOtherBalanceAdd(HashMap addShadow) throws Exception {
        String act_num = (String) addShadow.get(TransactionDAOConstants.ACCT_NO);
        if (act_num != null && !act_num.equalsIgnoreCase("")) {
            checkGL(act_num, (String) addShadow.get(TransactionDAOConstants.BRANCH_CODE),
                    (String) addShadow.get(TransactionDAOConstants.TRANS_TYPE));
            if (!addShadow.containsKey(TransactionDAOConstants.UNCLEAR_AMT)) {
                addShadow.put(TransactionDAOConstants.UNCLEAR_AMT, new Double(0));
            }

            // Updates other Balances  (Clear balance and Total Balance)
            sqlMap.executeUpdate("updateOtherBalancesGL", addShadow);

        }
        addShadow = null;
    }

    private void checkGL(String act_num, String branchCode, String transType) throws Exception {
        GLUpdateDAO objDAO = new GLUpdateDAO();
        GLTO objGLTO = new GLTO();
        objGLTO.setAcHdId(act_num);
        objGLTO.setBranchCode(branchCode);
        if (!objDAO.isGLEntryExists(objGLTO)) {
            objGLTO.setBalanceType(transType);
            sqlMap.executeUpdate("insertGLTO", objGLTO);
        }
    }

    /**
     * Updating Clear & Total Balance
     */
    public void performOtherBalanceMinus(HashMap addShadow) throws Exception {
        printLog("performOtherBalanceMinus:before:" + addShadow);

        String act_num = (String) addShadow.get(TransactionDAOConstants.ACCT_NO);
        if (act_num != null && !act_num.equalsIgnoreCase("")) {
            checkGL(act_num, (String) addShadow.get(TransactionDAOConstants.BRANCH_CODE),
                    (String) addShadow.get(TransactionDAOConstants.TRANS_TYPE));

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
            sqlMap.executeUpdate("updateOtherBalancesGL", addShadow);

        }
        addShadow = null;
    }

    /**
     * Updating Shadow debit or shadow credit based on the parameter
     */
    public void performShadowAdd(HashMap addShadow) throws Exception {
        printLog("addShadow:" + addShadow);
        String act_num = (String) addShadow.get(TransactionDAOConstants.ACCT_NO);
        if (act_num != null && !act_num.equalsIgnoreCase("")) {
            checkGL(act_num, (String) addShadow.get(TransactionDAOConstants.BRANCH_CODE),
                    (String) addShadow.get(TransactionDAOConstants.TRANS_TYPE));

            if (((String) addShadow.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
                addShadow.put(TransactionDAOConstants.AMT,
                        new Double(-1 * Double.parseDouble(addShadow.get(TransactionDAOConstants.AMT).toString())));
                sqlMap.executeUpdate("updateShadowDebitGL", addShadow);
            } else {
                sqlMap.executeUpdate("updateShadowCreditGL", addShadow);
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
            checkGL(act_num, (String) minusMap.get(TransactionDAOConstants.BRANCH_CODE),
                    (String) minusMap.get(TransactionDAOConstants.TRANS_TYPE));

            minusMap.put(TransactionDAOConstants.AMT,
                    new Double(-1 * Double.parseDouble(minusMap.get(TransactionDAOConstants.AMT).toString())));
            if (((String) minusMap.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
                sqlMap.executeUpdate("updateShadowDebitGL", minusMap);
            } else {
                sqlMap.executeUpdate("updateShadowCreditGL", minusMap);
            }
            printLog("performShadowMinus:after:" + minusMap);
        }
        minusMap = null;
    }

    /**
     * Updating Available Balance
     */
    public void updateAvailableBalance(HashMap updateMap) throws Exception {
//        printLog("updateAvailableBalance:" + updateMap);
//        
//        String act_num = (String)updateMap.get(TransactionDAOConstants.ACCT_NO);
//        if(act_num!=null && !act_num.equalsIgnoreCase("")){
//            if(!updateMap.containsKey(TransactionDAOConstants.UNCLEAR_AMT))
//                updateMap.put(TransactionDAOConstants.UNCLEAR_AMT,new Double(0));
//            
//            GLTO objGLTO = new GLTO();
//            objGLTO.setAcHdId(act_num);
//            objGLTO.setCurBal(CommonUtil.convertObjToDouble(updateMap.get(TransactionDAOConstants.AMT)));
//            objGLTO.setBranchCode(CommonUtil.convertObjToStr(updateMap.get(TransactionDAOConstants.BRANCH_CODE)));
//            
//            updateMap.put(TransactionDAOConstants.AMT,new Double(0));
//            
//            sqlMap.executeUpdate("updateAvailBalanceGL", updateMap);
//            
//            updateMap.put(TransactionDAOConstants.AMT, objGLTO.getCurBal());
//
//            GLUpdateDAO objGLUpdateDao = new GLUpdateDAO();
//            objGLUpdateDao.updateGL(objGLTO, updateMap);
//
//            objGLTO = null;
//            objGLUpdateDao = null;
//        }
//        updateMap = null;
    }

    public void updateGL(String acctHead, Double amount, LogTO objLogTO, HashMap updateMap) throws Exception {
        printLog("updateAvailableBalance (GLTransaction) :" + updateMap);
        if (updateMap.containsKey("INTERBRANCH_CREATION_SCREEN")) {
            String transOwner = CommonUtil.convertObjToStr(updateMap.get(TransactionDAOConstants.INITIATED_BRANCH));
            String acctOwner = CommonUtil.convertObjToStr(updateMap.get(TransactionDAOConstants.BRANCH_CODE));
            System.out.println("INTERBRANCH_CREATION_SCREEN transOwner##" + transOwner + "INTERBRANCH_CREATION_SCREEN acctOwner$$$" + acctOwner);
            System.out.println("INTERBRANCH_CREATION_SCREEN ruleMap operative" + updateMap);
            if (updateMap.containsKey("IS_INTER_BRANCH_TRANS")
                    && new Boolean(CommonUtil.convertObjToStr(updateMap.get("IS_INTER_BRANCH_TRANS"))).booleanValue()) {
                transOwner = "";
            } else {
                if (transOwner.length() == 0) {
                    throw new TTException("Initiated Brach Should not null ");
                }
            }

            if (!transOwner.equals(acctOwner)) {
                InterBranchTransaction objInterBranch = new InterBranchTransaction(transOwner, acctOwner);
                objInterBranch.doInterBranchTransaction(acctHead, amount, objLogTO, updateMap);
                updateMap.put("INTER_BRANCH_TRANS", new Boolean(true));
            } else {
                super.updateGL(acctHead, amount, objLogTO, updateMap);
            }
        } else {
            Boolean isInterBranch = new Boolean(false);
            System.out.println("GLRulemap" + updateMap);
            updateMap.put(TransactionDAOConstants.ACCT_NO, acctHead);
            if (updateMap.containsKey("PRODUCT")) {
                String transOwner = objLogTO.getBranchId();
                String acctOwner = ServerConstants.HO;
                InterBranchTransaction objInterBranch = new InterBranchTransaction(transOwner, acctOwner);
                objInterBranch.doInterBranchTransaction(acctHead, amount, objLogTO, updateMap);
                return;
            }

            String transOwner = CommonUtil.convertObjToStr(updateMap.get(TransactionDAOConstants.INITIATED_BRANCH));
            String acctOwner = CommonUtil.convertObjToStr(updateMap.get(TransactionDAOConstants.BRANCH_CODE));
            if (updateMap.containsKey("INTER_BRANCH") && updateMap.get("INTER_BRANCH") != null) {
                isInterBranch = (Boolean) updateMap.get("INTER_BRANCH");
            }

            if (updateMap.containsKey("IS_INTER_BRANCH_TRANS")
                    && new Boolean(CommonUtil.convertObjToStr(updateMap.get("IS_INTER_BRANCH_TRANS"))).booleanValue()) {
                transOwner = "";
            } else if (transOwner.length() == 0) {
                throw new TTException("Initiated Brach Should not null ");
            }

            System.out.println("GL tranaction transOwner : " + transOwner + "acctOwner : " + acctOwner);

            if ((!(transOwner.equals(acctOwner))) && isInterBranch.booleanValue()) {
//            String transOwner = objLogTO.getBranchId();
//            String acctOwner = ServerConstants.HO;

                InterBranchTransaction objInterBranch = new InterBranchTransaction(transOwner, acctOwner);
                objInterBranch.doInterBranchTransaction(acctHead, amount, objLogTO, updateMap);
                if (!updateMap.get("TRANSMODE").equals("TRANSFER")) {
                    String ruleTransType = CommonUtil.convertObjToStr(updateMap.get(TransactionDAOConstants.TRANS_TYPE));
                    if (ruleTransType.equals(CommonConstants.CREDIT)) {
                        updateMap.put(TransactionDAOConstants.TRANS_TYPE, CommonConstants.DEBIT);
                    } else {
                        updateMap.put(TransactionDAOConstants.TRANS_TYPE, CommonConstants.CREDIT);
                    }
                    updateMap.put(TransactionDAOConstants.BRANCH_CODE, acctOwner);//added abi for intiated branch should update in trans_ref gl
                    objLogTO.setBranchId(acctOwner);
                    System.out.println("GL tranaction transOwner" + transOwner + "acctOwner" + "acctOwner" + acctOwner + "updateMap" + updateMap);
                    super.updateGL(objInterBranch.getIBRAcctHead(), amount, objLogTO, updateMap, true);
                }
                updateMap.put("INTER_BRANCH_TRANS", new Boolean(true));
//                return;
            }

            String act_num = (String) updateMap.get(TransactionDAOConstants.ACCT_NO);
            if (act_num != null && !act_num.equalsIgnoreCase("")) {
                if (!updateMap.containsKey(TransactionDAOConstants.UNCLEAR_AMT)) {
                    updateMap.put(TransactionDAOConstants.UNCLEAR_AMT, new Double(0));
                }

                GLTO objGLTO = new GLTO();
                objGLTO.setAcHdId(acctHead);
                objGLTO.setCurBal(CommonUtil.convertObjToDouble(updateMap.get(TransactionDAOConstants.AMT)));
                objGLTO.setBranchCode(CommonUtil.convertObjToStr(updateMap.get(TransactionDAOConstants.BRANCH_CODE)));

                updateMap.put(TransactionDAOConstants.AMT, new Double(0));

                sqlMap.executeUpdate("updateAvailBalanceGL", updateMap);

                updateMap.put(TransactionDAOConstants.AMT, objGLTO.getCurBal());

                GLUpdateDAO objGLUpdateDao = new GLUpdateDAO();
                objGLUpdateDao.updateGL(objGLTO, updateMap, false);

                objGLTO = null;
                objGLUpdateDao = null;
            }
            updateMap = null;

//        String transOwner = objLogTO.getBranchId();
//        String acctOwner = objLogTO.getBranchId();
//        
//        if (ruleMap != null && !CommonUtil.convertObjToStr(ruleMap.get(TransactionDAOConstants.ACCT_NO)).equals("")) {
//            // get the branch for the a/c and override acctOwner variable
//            String acctNo = CommonUtil.convertObjToStr(ruleMap.get(TransactionDAOConstants.ACCT_NO));
//            acctOwner = CommonUtil.convertObjToStr(sqlMap.executeQueryForObject("getBranchGL", acctNo));
//            if (acctOwner.equals("")) {
//                acctOwner = objLogTO.getBranchId();
//            }
//        }
//
//        if (!transOwner.equalsIgnoreCase(acctOwner)) {
//            String ibrAcHd = getIBRAcctHead();
//            
//            objLogTO.setBranchId(acctOwner);
//            
//            super.updateGL(ibrAcHd, amount, objLogTO, ruleMap);
//            
//            objLogTO.setBranchId(transOwner);
//            
//            String ruleTransType = CommonUtil.convertObjToStr(ruleMap.get(TransactionDAOConstants.TRANS_TYPE));
//            if (ruleTransType.equals(CommonConstants.CREDIT)) {
//                ruleMap.put(TransactionDAOConstants.TRANS_TYPE, CommonConstants.DEBIT);
//            } else {
//                ruleMap.put(TransactionDAOConstants.TRANS_TYPE, CommonConstants.CREDIT);
//            }
//
//            super.updateGL(ibrAcHd, amount, objLogTO, ruleMap);
//        }   
        }
    }

    public void validateRules(HashMap validateMap, boolean isException) throws Exception {
        // Validation only for Debit
        String act_num = (String) validateMap.get(TransactionDAOConstants.ACCT_NO);
        RuleEngine engine = new RuleEngine();
        RuleContext context = new RuleContext();
        ArrayList list;

//        context.addRule(new GLParamRule());
        if (((String) validateMap.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
            if (act_num != null && !act_num.equalsIgnoreCase("") && !isException) {
                context.addRule(new GLLimitCheckingRule());
            }
            context.addRule(new GLParamRule());
            context.addRule(new DateCheckingRule());
        }
        list = (ArrayList) engine.validateAll(context, validateMap);

        context = null;
        engine = null;

        if (list != null) {
            HashMap exception = new HashMap();
            exception.put(CommonConstants.EXCEPTION_LIST, list);
            exception.put(CommonConstants.CONSTANT_CLASS, TransactionDAOConstants.TRANS_RULE_MAP);
            throw new TTException(exception);
        }
    }

    private String getIBRAcctHead() throws Exception {
        HashMap transMap = (HashMap) sqlMap.executeQueryForObject("getSelectTransParams", null);
        return CommonUtil.convertObjToStr(transMap.get("IBR_AC_HD"));
    }
}
