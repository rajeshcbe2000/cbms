/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PaymentVoucherDAO.java
 *
 * Created on Wed Feb 02 13:11:28 IST 2015
 */
package com.see.truetransact.serverside.payroll.voucherprocessing;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.payroll.PayrollDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.payroll.voucherprocessing.PaymentVoucherTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * PaymentVoucher DAO.
 *
 */
public class PaymentVoucherDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private PaymentVoucherTO objPaymentVoucherTO;
    private String userID = "";
    private String remarks = "";
    private TransactionDAO transactionDAO;
    private PayrollDAO payrollDAO;
    final int EDIT = 0, DELETE = 1, AUTHORIZE = 2, VIEW = 10;
    private HashMap returnMap = null;

    /**
     * Creates a new instance of AgentDAO
     */
    public PaymentVoucherDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        return returnMap;
    }

    private void insertData(LogDAO objLogDAO, LogTO objLogTO, HashMap map) throws Exception {
        try {
            System.out.println("map in insert data: " + map);
            returnMap = new HashMap();
            payrollDAO = new PayrollDAO();
            List headsTotList = null;
            headsTotList = (List) map.get("AccHeadsAmtList");
            HashMap monthYearMap = new HashMap();
            monthYearMap.put("MONTH_YEAR", objPaymentVoucherTO.getMonthYr());
            List resultList = sqlMap.executeQueryForList("getPaymentVoucherWithDetails", monthYearMap);
            if (resultList != null && resultList.size() > 0) {
                for (int i = 0; i < resultList.size(); i++) {
                    HashMap resMap = new HashMap();
                    resMap = (HashMap) resultList.get(i);
                    resMap.put("MONTH_YEAR", objPaymentVoucherTO.getMonthYr());
                    sqlMap.executeUpdate("insertIntoPayrollPaymentVoucher", resMap);
                }
            }
            if (headsTotList != null && headsTotList.size() > 0) {
                for (int i = 0; i < headsTotList.size(); i++) {
                    ArrayList headList = new ArrayList();
                    headList = (ArrayList) headsTotList.get(i);
                    HashMap glMap = new HashMap();
                    glMap.put("DEBIT_HD_ID", headList.get(0));  
                    glMap.put("PRODUCT_TYPE", "GL_VOUCHER");
                    glMap.put("AMOUNT", CommonUtil.convertObjToDouble(headList.get(1)));
                    glMap.put("EMPLOYEE_NAME", "PAYMENT VOUCHER FROM PAYROLL");
                    glMap.put("PAY_DESCRIPTION", "PAYMENT VOUCHER FROM PAYROLL");
                    glMap.put(CommonConstants.BRANCH_ID, objPaymentVoucherTO.getBranchId());
                    System.out.println("glMap.." + glMap);
                    HashMap parMap = new HashMap();
                    parMap = getDataMap(glMap);
                    System.out.println("para map" + parMap);
                    payrollDAO.execute(parMap);
                    System.out.println("retur Map " + returnMap);
                }
            }
            HashMap statusMap = new HashMap();
            statusMap.put("STATUS_DATE", ServerUtil.getCurrentDate(_branchCode));
            statusMap.put("MONTH_YEAR", objPaymentVoucherTO.getMonthYr());
            sqlMap.executeUpdate("updatePaymentVoucherStatus", statusMap);
            returnMap.put("POSTED", "POSTED");

        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public HashMap getDataMap(HashMap map) {
        System.out.println("inside datamap: " + map);
        HashMap dataMap = new HashMap();
        TransactionTO transactionTO = new TransactionTO();
        transactionTO.setDebitAcctNo(CommonUtil.convertObjToStr(map.get("DEBIT_HD_ID")));
        transactionTO.setProductType(CommonUtil.convertObjToStr(map.get("PRODUCT_TYPE")));
        transactionTO.setTransType("TRANSFER");
        transactionTO.setTransAmt(CommonUtil.convertObjToDouble(map.get("AMOUNT")));
        dataMap.put("PRODUCT_TYPE", map.get("PRODUCT_TYPE"));
        dataMap.put("BRANCH_CODE", map.get(CommonConstants.BRANCH_ID));
        dataMap.put("TransactionTO", transactionTO);
        if (map.get("CREDIT_HD_ID") != null) {
            dataMap.put("CREDIT_HD_ID", "" + map.get("CREDIT_HD_ID"));
        }
        System.out.println("Empplloyeee in getdATA map post to CBMS" + map.get("EMPLOYEE_NAME"));
        if (map.get("EMPLOYEE_NAME") != null) {
            dataMap.put("EMPLOYEE_NAME", "" + map.get("EMPLOYEE_NAME"));
        }
        System.out.println("Edescrin getdATA map post to CBMS" + map.get("PAY_DESCRIPTION"));
        if (map.get("PAY_DESCRIPTION") != null) {
            dataMap.put("PAY_DESCRIPTION", "" + map.get("PAY_DESCRIPTION"));
        }
        if (map.get("EMPLOYEEID") != null) {
            dataMap.put("AC_NO", map.get("EMPLOYEEID"));
        }
        if (map.get("AC_NUM") != null) {
            dataMap.put("ACT_NUM", map.get("AC_NUM"));
        }
        if (map.get("PRO_ID") != null) {
            dataMap.put("PRO", map.get("PRO_ID"));
        }
        dataMap.put(CommonConstants.USER_ID, objPaymentVoucherTO.getCreatedBy());
        return dataMap;
    }

    private void updateData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData(LogDAO objLogDAO, LogTO objLogTO, HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            PaymentVoucherDAO dao = new PaymentVoucherDAO();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("map in execute :" + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        LogDAO objLogDAO = new LogDAO();
        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        objLogTO.setSelectedBranchId((String) map.get(CommonConstants.SELECTED_BRANCH_ID));
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));

        userID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));

        if (map.containsKey("AccHeadsAmtList") && map.get("AccHeadsAmtList") != null) {
            objPaymentVoucherTO = (PaymentVoucherTO) map.get("AccHeadsTO");
            final String command = objPaymentVoucherTO.getCommand();
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                if (map.containsKey("AccHeadsAmtList")) {
                    insertData(objLogDAO, objLogTO, map);
                }
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            } else {
                throw new NoCommandException();
            }
        }
        objLogDAO = null;
        objLogTO = null;
        destroyObjects();
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void destroyObjects() {
        objPaymentVoucherTO = null;
    }
}
