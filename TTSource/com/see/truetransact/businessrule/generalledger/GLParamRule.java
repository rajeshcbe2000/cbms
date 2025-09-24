/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * GLUpdateDAO.java
 *
 * Created on April 2, 2004, 11:00 AM
 */

package com.see.truetransact.businessrule.generalledger;

import java.util.List;
import java.util.HashMap;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;

import com.see.truetransact.transferobject.transaction.common.product.gl.GLTO;
import com.see.truetransact.transferobject.generalledger.AccountMaintenanceTO;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
/**
 *
 * @author  bala
 */
public class GLParamRule  extends ValidationRule{
    private SqlMap sqlMap;
    private final String NEGATIVE = "Balance is not available. Available Amount is ";
    
    /** Creates a new instance of GLUpdateDAO */
    public GLParamRule() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    /** 
     * To Validate the Condition depending on the Value Passed in the HashMap from the DAO 
     */
    public void validate(HashMap inputMap) throws Exception {
        GLTO objGL = new GLTO();
        objGL.setAcHdId(CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.ACCT_NO)));
        objGL.setBranchCode(CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.BRANCH_CODE)));
        objGL.setCurBal(CommonUtil.convertObjToDouble(inputMap.get(TransactionDAOConstants.AMT)));
        
        validateBranchGL(objGL, inputMap);
    }
    
    public GLTO validateBranchGL (GLTO objGLTO, HashMap ruleMap) throws Exception {
        String acHd = objGLTO.getAcHdId();
        String trans = "";
        AccountMaintenanceTO objAccountMaintenanceTO = null;
        boolean isFloat = false;
        boolean isNegativeAllowed = false;
        double glAmt = 0;
        double branchGLBalance = 0;
        
        // Checking for Account Head passed or not
        if (CommonUtil.convertObjToStr(acHd).equals("")) {
            throw new TTException ("GLUpdate:Account Head is not passed or set...") ;
        }
        
        // Checking for Branch Id passed or not
        if (CommonUtil.convertObjToStr(objGLTO.getBranchCode()).equals("")) {
            throw new TTException ("GLUpdate:Branch Code is not passed...") ;
        }
        
        // Checking for Amount is passed or not
        if (CommonUtil.convertObjToDouble(objGLTO.getCurBal()).doubleValue() == 0) {
            throw new TTException ("GLUpdate:Amount is not passed") ;
        }
        
        // Check for Rule Map & Transaction type are passed or not
        if (ruleMap == null || !ruleMap.containsKey(TransactionDAOConstants.TRANS_TYPE)) {
            throw new TTException ("GLUpdate:Rule Map is not passed or set...") ;
        }
        
        // Retrival from Account Maintenance table (Bank Level configuration)
        HashMap whereMap = new HashMap();
        whereMap.put("AC_HD_ID", acHd);
        objAccountMaintenanceTO = (AccountMaintenanceTO) sqlMap.executeQueryForObject("getSelectAccountMaintenanceTO", whereMap);

        // Check for the Account Head maintenance is configured or not
        if (objAccountMaintenanceTO == null) {
            throw new TTException ("GLUpdate:Account Head Maintenance is not set...") ;
        }
        
        // if configured getting floatable and negative allowed properties.
        isFloat = objAccountMaintenanceTO.getFloatAct().equalsIgnoreCase("Y");
        isNegativeAllowed = objAccountMaintenanceTO.getNegativeAllowed().equalsIgnoreCase("Y");

        // Branch Level GL existing or not checking
        HashMap map = new HashMap();
        map.put("ACCT_HEAD", objGLTO.getAcHdId());
        map.put("BRANCH_ID", objGLTO.getBranchCode());
        GLTO objBranchGLTO = (GLTO) sqlMap.executeQueryForObject ("getSelectGLTO", map);
        
        // Check the Balance TYPE
        trans = CommonUtil.convertObjToStr(ruleMap.get(TransactionDAOConstants.TRANS_TYPE));
        String acHdTrans = "";
        if (objBranchGLTO != null) { // If Branch GL entry exists (Update Case)
            acHdTrans = objBranchGLTO.getBalanceType(); 
        } else if (objBranchGLTO == null && isFloat) { // If it is not exist and It is floating one (Insert Case)
            acHdTrans = trans;
        } else { // GL is not existing and if it is not floating take from A/c Maintenance (Insert Case)
            acHdTrans = objAccountMaintenanceTO.getBalancetype();
        }

        // Checking the transactions are equal or not and changing balances
        System.out.println("acHdTrans:" + acHdTrans +":"+ trans +":"+ objGLTO.getCurBal());
        if (acHdTrans.equals(trans) && objGLTO.getCurBal().doubleValue() < 0) {
            objGLTO.setCurBal(new Double(-1 * objGLTO.getCurBal().doubleValue()));
        } else if (!acHdTrans.equals(trans) && objGLTO.getCurBal().doubleValue() > 0) {
            objGLTO.setCurBal(new Double(-1 * objGLTO.getCurBal().doubleValue()));
        }
        
        // Checking for Amount and Negative allowed (Insert Case)
        glAmt = CommonUtil.convertObjToDouble(objGLTO.getCurBal()).doubleValue();
        if (objBranchGLTO == null && !isFloat && !isNegativeAllowed && glAmt < 0) {
            System.out.println("Branch Level : ");
            throw new TTException (NEGATIVE + String.valueOf(glAmt)) ;
        }
        
        if (objBranchGLTO != null) { // Update Case
            branchGLBalance = CommonUtil.convertObjToDouble(objBranchGLTO.getCurBal()).doubleValue() + 
                    glAmt;

            /* If the Branchwise GL Balance Becomes less than Zero then 
             * Check for Floatable or not .. if it is floatable
             * Change the balance type.
             */
            if (branchGLBalance < 0) { // Checking the amount If it is less than zero
                if (isFloat) { // Checking for Floating and changing the minus symbol and setting trans type
                    objGLTO.setCurBal(new Double(-1 * objGLTO.getCurBal().doubleValue()));
                    if (objBranchGLTO.getBalanceType().equalsIgnoreCase(CommonConstants.DEBIT)) {
                        objGLTO.setBalanceType(CommonConstants.CREDIT);
                    } else {
                        objGLTO.setBalanceType(CommonConstants.DEBIT);
                    }
                } else if (!isNegativeAllowed) { // If it is not floating checking for Negative Allowed or not
                    System.out.println("Branch Level : ");
                    throw new TTException (NEGATIVE + branchGLBalance);
                } else { // If Negative allowed take Balance type from Account head maintenance.
                    objGLTO.setBalanceType(objAccountMaintenanceTO.getBalancetype());
                }
            } else { // If the amt is not negative take from Branch GL itself
                objGLTO.setBalanceType(objBranchGLTO.getBalanceType());
            }
        } else { // Insert Case (if GL doesn't have ac head)
            if (isFloat) { // Checking for floating)
                objGLTO.setBalanceType(acHdTrans);
            } else { // if it is non-floating take it from a/c head maintenance
                objGLTO.setBalanceType(objAccountMaintenanceTO.getBalancetype());
            } 
        }

        objGLTO.setLastTransDt(ServerUtil.getCurrentDate(objGLTO.getBranchCode()));
        System.out.println("GLTO:" + objGLTO);

        return objGLTO;
    }
    
    
//    public GLTO validateBankGL (GLTO objGLTO, HashMap ruleMap) throws Exception {        
//        System.out.println("GLTO:" + objGLTO);
////        if (isGLEntryExists(objGLTO)) { // Checking existness and updating the TO (Update Case)
////            sqlMap.executeUpdate("updateGLTO", objGLTO);
////        } else { // Insert case
////            objGLTO.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
////            sqlMap.executeUpdate("insertGLTO", objGLTO);
////        }
//
//        /**
//         * Bank Level Checking for GL A/c Head
//         */
//        double bankGLBalance = CommonUtil.convertObjToDouble(objAccountMaintenanceTO.getGlbalance()).doubleValue() + 
//                glAmt;
//
//        /* If the Banklevel GL Balance Becomes less than Zero then 
//         * Check for Floatable or not .. if it is floatable
//         * Change the balance type.
//         */
//        if (bankGLBalance < 0) { // Checking the amount If it is less than zero
//            if (isFloat) { // Checking for Floating and changing the minus symbol and setting trans type
//                if (branchGLBalance > 0) { // If TO is not modified in Branch level
//                    objGLTO.setCurBal(new Double(-1 * objGLTO.getCurBal().doubleValue()));
//                    if (objAccountMaintenanceTO.getBalancetype().equalsIgnoreCase(CommonConstants.DEBIT)) {
//                        objGLTO.setBalanceType(CommonConstants.CREDIT);
//                    } else {
//                        objGLTO.setBalanceType(CommonConstants.DEBIT);
//                    }
//                }
//            } else if (!isNegativeAllowed) { // If it is not floating checking for Negative Allowed or not
//                System.out.println("Bank Level : ");
//                throw new TTException ("Negative Balance is not allowed." ) ;
//            } else { // If Negative allowed take Balance type from Account head maintenance.
//                objGLTO.setBalanceType(objAccountMaintenanceTO.getBalancetype());
//            }
//        } else { // If the amt is not negative take from Branch GL itself
//            objGLTO.setBalanceType(objAccountMaintenanceTO.getBalancetype());
//        }
//        sqlMap.executeUpdate("updateBankGL", objGLTO);
//    }
    
    public boolean isGLEntryExists(GLTO objGLTO) throws Exception {
        boolean result = false;
        HashMap map = new HashMap();
        map.put("ACCT_HEAD", objGLTO.getAcHdId());
        map.put("BRANCH_ID", objGLTO.getBranchCode());

        GLTO objBranchGLTO = (GLTO) sqlMap.executeQueryForObject ("getSelectGLTO", map);
        
        if (objBranchGLTO != null) {
            result = true;
        }
        return result;
    }
    
    public static void main (String str[]) {
        try {
            GLParamRule dao = new GLParamRule();
            HashMap rMap = new HashMap();
            rMap.put(TransactionDAOConstants.TRANS_TYPE, CommonConstants.DEBIT);
            GLTO objTO = new GLTO();
            objTO.setAcHdId("CSHAND");
            objTO.setBranchCode("Bran");
            objTO.setCurBal(new Double(4000));

//            dao.validateGL(objTO, rMap);
            System.out.println("Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
