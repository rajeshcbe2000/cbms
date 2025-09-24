/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * GLUpdateDAO.java
 *
 * Created on April 2, 2004, 11:00 AM
 */
package com.see.truetransact.serverside.transaction.common.product.gl;

import java.util.List;
import java.util.HashMap;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.transferobject.transaction.common.product.gl.GLTO;
import com.see.truetransact.transferobject.generalledger.AccountMaintenanceTO;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.serverutil.ServerConstants;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;

/**
 *
 * @author bala
 */
public class GLUpdateDAO {

    private SqlMap sqlMap;
    private final String NEGATIVE = "Balance is not available. Available Amount is ";
    private final String DC = ServerConstants.HO == null ? "" : ServerConstants.HO;

    /**
     * Creates a new instance of GLUpdateDAO
     */
    public GLUpdateDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public void updateGL(GLTO objGLTO) throws Exception {
        updateGL(objGLTO, null, false);
    }

    public void updateGL(GLTO objGLTO, HashMap ruleMap, boolean isReference) throws Exception {
        String acHd = objGLTO.getAcHdId();
        String trans = "";
        AccountMaintenanceTO objAccountMaintenanceTO = null;
        boolean isFloat = false;
        boolean isNegativeAllowed = false;
        double glAmt = 0;
        double branchGLBalance = 0;

        // Checking for Account Head passed or not
        if (CommonUtil.convertObjToStr(acHd).equals("")) {
            throw new TTException("GLUpdate:Account Head is not passed or set...");
        }

        // Checking for Branch Id passed or not
        if (CommonUtil.convertObjToStr(objGLTO.getBranchCode()).equals("")) {
            throw new TTException("GLUpdate:Branch Code is not passed...");
        }

        // Checking for Amount is passed or not
        if (CommonUtil.convertObjToDouble(objGLTO.getCurBal()).doubleValue() == 0) {
            throw new TTException("GLUpdate:Amount is not passed");
        }

        // Check for Rule Map & Transaction type are passed or not
        if (ruleMap == null || !ruleMap.containsKey(TransactionDAOConstants.TRANS_TYPE)) {
            throw new TTException("GLUpdate:Rule Map is not passed or set...");
        }

        // Retrival from Account Maintenance table (Bank Level configuration)
        HashMap whereMap = new HashMap();
        whereMap.put("AC_HD_ID", acHd);
        objAccountMaintenanceTO = (AccountMaintenanceTO) sqlMap.executeQueryForObject("getSelectAccountMaintenanceTO", whereMap);

        // Check for the Account Head maintenance is configured or not
        if (objAccountMaintenanceTO == null) {
            throw new TTException("GLUpdate:Account Head Maintenance is not set...");
        }
//        System.out.println("ruleMap inside updateGL before Head office a/c handling:"+ruleMap);
        // This is for Head office a/c handling
        if (CommonUtil.convertObjToStr(objAccountMaintenanceTO.getHoAcct()).equalsIgnoreCase("Y") && !objGLTO.getBranchCode().equals(DC)) {
            GLTO objGLTOHO = new GLTO();
            objGLTOHO.setAcHdId(objAccountMaintenanceTO.getReconsAcHdId());
            objGLTOHO.setCurBal(objGLTO.getCurBal());
            objGLTOHO.setBranchCode(objGLTO.getBranchCode());

            HashMap ruleMapHO = new HashMap();
            ruleMapHO.putAll(ruleMap);

            // Putting Branch Credit
            updateGL(objGLTOHO, ruleMapHO, true);

            if (CommonUtil.convertObjToStr(ruleMapHO.get(TransactionDAOConstants.TRANS_TYPE)).equals(CommonConstants.DEBIT)) {
                ruleMapHO.put(TransactionDAOConstants.TRANS_TYPE, CommonConstants.CREDIT);
            } else {
                ruleMapHO.put(TransactionDAOConstants.TRANS_TYPE, CommonConstants.DEBIT);
            }
            objGLTOHO.setBranchCode(DC);
            updateGL(objGLTOHO, ruleMapHO, true);

            objGLTO.setBranchCode(DC);
        }
//        System.out.println("ruleMap inside updateGL after Head office a/c handling:"+ruleMap);
        // if configured getting floatable and negative allowed properties.
        isFloat = objAccountMaintenanceTO.getFloatAct().equalsIgnoreCase("Y");
        isNegativeAllowed = objAccountMaintenanceTO.getNegativeAllowed().equalsIgnoreCase("Y");

        // Branch Level GL existing or not checking
        HashMap map = new HashMap();
        map.put("ACCT_HEAD", objGLTO.getAcHdId());
        map.put("BRANCH_ID", objGLTO.getBranchCode());
        GLTO objBranchGLTO = (GLTO) sqlMap.executeQueryForObject("getSelectGLTO", map);

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

        if (acHdTrans == null) {
            acHdTrans = objAccountMaintenanceTO.getBalancetype();
        }

        // Checking the transactions are equal or not and changing balances
        System.out.println("acHdTrans:" + acHdTrans + ":" + trans + ":" + objGLTO.getCurBal());
        if (acHdTrans.equals(trans) && objGLTO.getCurBal().doubleValue() < 0) {
            objGLTO.setCurBal(new Double(-1 * objGLTO.getCurBal().doubleValue()));
        } else if (!acHdTrans.equals(trans) && objGLTO.getCurBal().doubleValue() > 0) {
            objGLTO.setCurBal(new Double(-1 * objGLTO.getCurBal().doubleValue()));
        }

        // Checking for Amount and Negative allowed (Insert Case)
        glAmt = CommonUtil.convertObjToDouble(objGLTO.getCurBal()).doubleValue();
        if (objBranchGLTO == null && !isFloat && !isNegativeAllowed && glAmt < 0) {
            System.out.println("Branch Level : ");
            throw new TTException(NEGATIVE + String.valueOf(glAmt));
        }

        if (objBranchGLTO != null) { // Update Case
            branchGLBalance = CommonUtil.convertObjToDouble(objBranchGLTO.getCurBal()).doubleValue()
                    + glAmt;

            /*
             * If the Branchwise GL Balance Becomes less than Zero then Check
             * for Floatable or not .. if it is floatable Change the balance
             * type.
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
                    throw new TTException(NEGATIVE + branchGLBalance);
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
        Double prvAmt = objGLTO.getCurBal();
        if (isGLEntryExists(objGLTO)) { // Checking existness and updating the TO (Update Case)
            if (branchGLBalance < 0 && isFloat) {
                branchGLBalance = branchGLBalance * -1;
                objGLTO.setCurBal(new Double(branchGLBalance));
                sqlMap.executeUpdate("updateglTOminus", objGLTO);
                objGLTO.setCurBal(prvAmt);

            } else {
                sqlMap.executeUpdate("updateGLTO", objGLTO);
            }
        } else { // Insert case
            objGLTO.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
            sqlMap.executeUpdate("insertGLTO", objGLTO);
        }


        /**
         * Bank Level Checking for GL A/c Head
         */
        acHdTrans = objAccountMaintenanceTO.getBalancetype();
        // Checking the transactions are equal or not and changing balances
        if (acHdTrans.equals(trans) && objGLTO.getCurBal().doubleValue() < 0) {
            objGLTO.setCurBal(new Double(-1 * objGLTO.getCurBal().doubleValue()));
        } else if (!acHdTrans.equals(trans) && objGLTO.getCurBal().doubleValue() > 0) {
            objGLTO.setCurBal(new Double(-1 * objGLTO.getCurBal().doubleValue()));
        }

        // Checking for Amount and Negative allowed (Insert Case)
        glAmt = CommonUtil.convertObjToDouble(objGLTO.getCurBal()).doubleValue();

        double bankGLBalance = CommonUtil.convertObjToDouble(objAccountMaintenanceTO.getGlbalance()).doubleValue()
                + glAmt;

        /*
         * If the Banklevel GL Balance Becomes less than Zero then Check for
         * Floatable or not .. if it is floatable Change the balance type.
         */
        if (bankGLBalance < 0) { // Checking the amount If it is less than zero
            if (isFloat) { // Checking for Floating and changing the minus symbol and setting trans type
                if (branchGLBalance > 0) { // If TO is not modified in Branch level
                    objGLTO.setCurBal(new Double(-1 * objGLTO.getCurBal().doubleValue()));
                    if (objAccountMaintenanceTO.getBalancetype().equalsIgnoreCase(CommonConstants.DEBIT)) {
                        objGLTO.setBalanceType(CommonConstants.CREDIT);
                    } else {
                        objGLTO.setBalanceType(CommonConstants.DEBIT);
                    }
                }
            } else if (!isNegativeAllowed) { // If it is not floating checking for Negative Allowed or not
                System.out.println("Bank Level : ");
                throw new TTException("Negative Balance is not allowed.");
            } else { // If Negative allowed take Balance type from Account head maintenance.
                objGLTO.setBalanceType(objAccountMaintenanceTO.getBalancetype());
            }
        } else { // If the amt is not negative take from Branch GL itself
            objGLTO.setBalanceType(objAccountMaintenanceTO.getBalancetype());
        }
        if (bankGLBalance < 0 && branchGLBalance > 0 && isFloat) {
            bankGLBalance = bankGLBalance * -1;
            objGLTO.setCurBal(new Double(bankGLBalance));
            sqlMap.executeUpdate("updateBankMinusGL", objGLTO);
        } else {
            sqlMap.executeUpdate("updateBankGL", objGLTO);
        }
//        sqlMap.executeUpdate("updateBankGL", objGLTO);
//        System.out.println("ruleMap inside updateGL before objTransRefGLDAO.insertRefGL:"+ruleMap);
        if (isReference) {
            objGLTO.setCurBal(prvAmt);
            TransRefGLDAO objTransRefGLDAO = new TransRefGLDAO();
            objTransRefGLDAO.insertRefGL(objGLTO, ruleMap);
        }
//        System.out.println("ruleMap inside updateGL after objTransRefGLDAO.insertRefGL:"+ruleMap);
    }

    public boolean isGLEntryExists(GLTO objGLTO) throws Exception {
        boolean result = false;
        HashMap map = new HashMap();
        map.put("ACCT_HEAD", objGLTO.getAcHdId());
        map.put("BRANCH_ID", objGLTO.getBranchCode());

        GLTO objBranchGLTO = (GLTO) sqlMap.executeQueryForObject("getSelectGLTO", map);

        if (objBranchGLTO != null) {
            result = true;
        }
        return result;
    }

    public static void main(String str[]) {
        try {
            GLUpdateDAO dao = new GLUpdateDAO();
            HashMap rMap = new HashMap();
            rMap.put(TransactionDAOConstants.TRANS_TYPE, CommonConstants.DEBIT);
            GLTO objTO = new GLTO();
            objTO.setAcHdId("CSHAND");
            objTO.setBranchCode("Bran");
            objTO.setCurBal(new Double(4000));

            dao.updateGL(objTO, rMap, false);
            System.out.println("Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
