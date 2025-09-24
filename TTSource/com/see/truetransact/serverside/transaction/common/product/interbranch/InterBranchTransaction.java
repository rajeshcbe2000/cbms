/*
* Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * InterBranchTransaction.java
 *
 * Created on June 17, 2004, 3:07 PM
 */
package com.see.truetransact.serverside.transaction.common.product.interbranch;

import java.util.HashMap;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.transaction.common.Transaction;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;

/**
 *
 * @author Bala
 */
public class InterBranchTransaction extends Transaction {

    private String transOwner;
    private String acctOwner;

    /**
     * Creates a new instance of InterBranchTransaction
     */
    public InterBranchTransaction(String transOwner, String acctOwner) throws ServiceLocatorException {
        super();
        this.transOwner = transOwner;
        this.acctOwner = acctOwner;
    }

    public void doInterBranchTransaction(String acctHead, Double amount, LogTO objLogTO, HashMap ruleMap) throws Exception {
        String ibrAcHd = getIBRAcctHead();
        System.out.println("doInterBranchTransaction ruleMap : " + ruleMap);
        boolean isGL = CommonUtil.convertObjToStr(ruleMap.get(TransactionDAOConstants.PROD_TYPE)).equals(TransactionFactory.GL);
        System.out.println("acctOwner" + acctOwner);
        if (!isGL && !ruleMap.containsKey("ACTUAL_TRANSFER_TRANS")) {
            objLogTO.setBranchId(acctOwner);
        }
//        System.out.println("ruleMap inside doInterBranchTransaction before acctHead:"+ruleMap);
        if(!ruleMap.containsKey("ACTUAL_TRANSFER_TRANS")){
            if (!isGL) {
                super.updateGL(acctHead, amount, objLogTO, ruleMap);
            } else {
                super.updateGL(acctHead, amount, objLogTO, ruleMap, true);
            }
        }else{
            if(ruleMap.containsKey("ACTUAL_TRANSFER_TRANS") && ruleMap.get("ACTUAL_TRANSFER_TRANS").equals("TRANSFER")){
                objLogTO.setBranchId(objLogTO.getInitiatedBranch());
                System.out.println("doInterBranchTransaction ruleMap TRANSMODE TRANSFER" + objLogTO);
                super.updateGL(ibrAcHd, amount, objLogTO, ruleMap, true);
            }
        }
//        System.out.println("ruleMap inside doInterBranchTransaction after acctHead:"+ruleMap);
        if (ruleMap.get(TransactionDAOConstants.TRANS_MODE).equals(TransactionDAOConstants.CASH)) {
//             if (isGL) {
//            objLogTO.setBranchId(acctOwner);
//            super.updateGL(ibrAcHd, amount, objLogTO, ruleMap, true); 
//            objLogTO.setBranchId(CommonUtil.convertObjToStr(ruleMap.get("INITIATED_BRANCH")));//COMMENT BY ABI
//        } 
        }
        /*
         * if (isGL) { objLogTO.setBranchId(acctOwner); super.updateGL(ibrAcHd,
         * amount, objLogTO, ruleMap, true);
         * objLogTO.setBranchId(CommonUtil.convertObjToStr(ruleMap.get("INITIATED_BRANCH")));//COMMENT
         * BY ABI } if (!isGL) { objLogTO.setBranchId(acctOwner); }
         */

        String ruleTransType = CommonUtil.convertObjToStr(ruleMap.get(TransactionDAOConstants.TRANS_TYPE));
        if (ruleTransType.equals(CommonConstants.CREDIT)) {
            ruleMap.put(TransactionDAOConstants.TRANS_TYPE, CommonConstants.DEBIT);
        } else {
            ruleMap.put(TransactionDAOConstants.TRANS_TYPE, CommonConstants.CREDIT);
        }
        if(ruleMap.containsKey("ACTUAL_TRANSFER_TRANS") && ruleMap.get("ACTUAL_TRANSFER_TRANS").equals("TRANSFER")){
            objLogTO.setBranchId(acctOwner);
            System.out.println("doInterBranchTransaction ruleMap TRANSMODE TRANSFER" + objLogTO);
        }
//        System.out.println("ruleMap inside doInterBranchTransaction before ibrAcHd:"+ruleMap);
        super.updateGL(ibrAcHd, amount, objLogTO, ruleMap, true);
        if (ruleMap.containsKey("ACTUAL_TRANS_TYPE")) {
            ruleMap.put("TRANSMODE", TransactionDAOConstants.CASH);
            ruleMap.put(TransactionDAOConstants.PARTICULARS, "BY CASH");
        }
//        System.out.println("ruleMap inside doInterBranchTransaction after ibrAcHd:"+ruleMap);
    }

    public String getIBRAcctHead() throws Exception {
        HashMap transMap = (HashMap) sqlMap.executeQueryForObject("getSelectTransParams", null);
        return CommonUtil.convertObjToStr(transMap.get("IBR_AC_HD"));
    }

    public void performOtherBalanceAdd(java.util.HashMap addShadow) throws Exception {
    }

    public void performOtherBalanceMinus(java.util.HashMap minusMap) throws Exception {
    }

    public void performShadowAdd(java.util.HashMap addShadow) throws Exception {
    }

    public void performShadowMinus(java.util.HashMap minusMap) throws Exception {
    }

    public void updateAvailableBalance(java.util.HashMap updateMap) throws Exception {
    }

    public void validateRules(java.util.HashMap validateMap, boolean isException) throws Exception {
    }
}
