/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmployeeDAO.java
 *
 * Created on Tue Feb 17 12:06:18 IST 2004
 * 
 * Modified by anjuanand on 21-07-2014
 */
package com.see.truetransact.serverside.payroll.employee;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.payroll.earningsDeductions.EarnDeduPayTO;
import com.see.truetransact.transferobject.payroll.employee.EmployeeAddrTO;
import com.see.truetransact.transferobject.payroll.employee.EmployeeDetailsTO;
import com.see.truetransact.transferobject.payroll.employee.SalaryStructTO;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Employee DAO.
 *
 */
public class EmployeeDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private EmployeeAddrTO objEmployeeAddrTO;
    private EmployeeDetailsTO objEmployeeDetailsTO;
    private SalaryStructTO objsalSalaryStructTO;
    private EarnDeduPayTO objEarnDeduPay;
    private byte[] photoByteArray;
    final String EMPLOYEE_PHOTO = "employee\\";
    private Date currDt;
    HashMap returnMap;

    /**
     * Creates a new instance of EmployeeDAO
     */
    public EmployeeDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap resultMap = new HashMap();
        String where = CommonUtil.convertObjToStr(map.get(CommonConstants.MAP_WHERE));
        List list = (List) sqlMap.executeQueryForList("getSelectEmployeeMasterDetailsTO", where);
        if (list != null && list.size() > 0) {
            resultMap.put("EmployeeMastrDetailsTO", list);
        }
        list = (List) sqlMap.executeQueryForList("getSelectEmployeeSalStructTO", where);
        if (list != null && list.size() > 0) {
            resultMap.put("EmployeeSalStructTO", list);
        }
        list = (List) sqlMap.executeQueryForList("getSelectEmployeeOtherDetailsTO", where);
        if (list != null && list.size() > 0) {
            resultMap.put("EmployeeOtherDetailsTO", list);
        }
        list = (List) sqlMap.executeQueryForList("getSelectEmplPresentDetailsTO", where);
        if (list != null && list.size() > 0) {
            resultMap.put("EmployeePresentDetailsTO", list);
        }
        return resultMap;
    }

    private void insertData(HashMap map) throws Exception {
        String employeeId = getEmployeeID();
        returnMap.put("EmployeeId", employeeId);
        objEmployeeDetailsTO.setEmployeeCode(employeeId);
        objsalSalaryStructTO.setEmployeeCode(employeeId);
        objEmployeeDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
        objsalSalaryStructTO.setStatus(CommonConstants.STATUS_CREATED);
        objsalSalaryStructTO.setSrlNo(1);
        objEmployeeAddrTO.setEmployeeId(employeeId);
//        if (objEmployeeDetailsTO.getPhotoFile() != null) {
//            objEmployeeDetailsTO.setPhotoFile(employeeId + CommonUtil.convertObjToStr(objEmployeeDetailsTO.getPhotoFile()));
//            //Method to Store the Picture...
//            storePicture();
//        }
        sqlMap.executeUpdate("insertEmplMasterTo", objEmployeeDetailsTO);
        sqlMap.executeUpdate("insertEmplOtherDetailsTo", objEmployeeDetailsTO);
        sqlMap.executeUpdate("insertEmplPresentDetailsTo", objsalSalaryStructTO);
        sqlMap.executeUpdate("updateCustTo", objEmployeeDetailsTO);
        sqlMap.executeUpdate("updateEmplSalStructureTo", objsalSalaryStructTO);
    }

    private void insertPayCodesData(EmployeeDetailsTO objEmployeeDetailsTO, SalaryStructTO objsalSalaryStructTO, EarnDeduPayTO objEarnDeduPay) throws Exception {
        try {
            String empId = "";
            empId = objEmployeeDetailsTO.getEmployeeCode();
            List employeeDetails = sqlMap.executeQueryForList("chkEmployeeDetails", empId);
            if (employeeDetails.size() > 0 && employeeDetails != null) {
                HashMap resultMap = (HashMap) employeeDetails.get(0);
                if (resultMap.containsKey("COUNT") && CommonUtil.convertObjToInt(resultMap.get("COUNT")) == 0) {
                    double basicSalary = 0;
                    basicSalary = objsalSalaryStructTO.getPresentBasicSalary();
                    Date effDate = null;
                    effDate = setproperDate(objsalSalaryStructTO.getEffectiveDate());
                    objEarnDeduPay.setEmployeeId(empId);
                    objEarnDeduPay.setProdType(CommonConstants.GL_TRANSMODE_TYPE);
                    objEarnDeduPay.setSrlNo(CommonUtil.convertObjToInt("1"));
                    String basic = "";
                    List basicList = sqlMap.executeQueryForList("getBasicPayCode", null);
                    if (basicList != null && basicList.size() > 0) {
                        HashMap basicMap = new HashMap();
                        basicMap = (HashMap) basicList.get(0);
                        basic = CommonUtil.convertObjToStr(basicMap.get("PAY_CODE"));
                        objEarnDeduPay.setPayCode(basic);
                        objEarnDeduPay.setAmount(basicSalary);
                        objEarnDeduPay.setFromDate(effDate);
                        objEarnDeduPay.setActive("Y");
                        sqlMap.executeUpdate("insertPayMaster", objEarnDeduPay);
                    }
                    List fixedCodesList = sqlMap.executeQueryForList("getAllFixedPayCodes", null);
                    if (fixedCodesList != null && fixedCodesList.size() > 0) {
                        for (int i = 0; i < fixedCodesList.size(); i++) {
                            HashMap fixedMap = new HashMap();
                            fixedMap = (HashMap) fixedCodesList.get(i);
                            String fixedCode = "";
                            fixedCode = CommonUtil.convertObjToStr(fixedMap.get("PAY_CODE"));
                            double fixedAmount = 0;
                            fixedAmount = CommonUtil.convertObjToDouble(fixedMap.get("PAY_FIX_AMT"));
                            Date fromDate = null;
                            fromDate = setproperDate((Date) fixedMap.get("FROM_DATE"));
                            String active = "";
                            active = CommonUtil.convertObjToStr(fixedMap.get("ACTIVE"));
                            String srlNo = null;
                            srlNo = CommonUtil.convertObjToStr(fixedMap.get("SRL_NO"));
                            objEarnDeduPay.setPayCode(fixedCode);
                            objEarnDeduPay.setAmount(fixedAmount);
                            objEarnDeduPay.setFromDate(fromDate);
                            objEarnDeduPay.setActive(active);
                            objEarnDeduPay.setSrlNo(CommonUtil.convertObjToInt(srlNo));
                            sqlMap.executeUpdate("insertPayMaster", objEarnDeduPay);
                        }
                    }
                    List selPayList = sqlMap.executeQueryForList("getAllCalcPayCodes", null);
                    String paycode = "";
                    if (selPayList != null && selPayList.size() > 0) {
                        HashMap selPayMap = new HashMap();
                        for (int k = 0; k < selPayList.size(); k++) {
                            selPayMap = (HashMap) selPayList.get(k);
                            double percent = 0;
                            percent = CommonUtil.convertObjToDouble(selPayMap.get("PAY_PERCENT"));
                            String payCode = "";
                            payCode = CommonUtil.convertObjToStr(selPayMap.get("PAY_CODE"));
                            String payCalc = "";
                            payCalc = CommonUtil.convertObjToStr(selPayMap.get("PAY_CALC_ON"));
                            double minAmt = 0;
                            minAmt = CommonUtil.convertObjToDouble(selPayMap.get("PAY_MIN_AMT"));
                            double maxAmt = 0;
                            maxAmt = CommonUtil.convertObjToDouble(selPayMap.get("PAY_MAX_AMT"));
                            Date fromDate = null;
                            fromDate = setproperDate((Date) selPayMap.get("FROM_DATE"));
                            String active = "";
                            active = CommonUtil.convertObjToStr(selPayMap.get("ACTIVE"));
                            String srlNo = null;
                            srlNo = CommonUtil.convertObjToStr(selPayMap.get("SRL_NO"));
                            String[] payCalcOn = payCalc.split(Pattern.quote("+"));
                            double finalAmt = 0;
                            double totAmt = 0;
                            int d = 0;
                            for (String s : payCalcOn) {
                                if (d == 0) {
                                    paycode = "'" + s + "'";
                                } else {
                                    paycode = paycode + "," + "'" + s + "'";
                                }
                                d++;
                            }
                            HashMap payMap = new HashMap();
                            payMap.put("PAYCODE", paycode);
                            payMap.put("EMPID", empId);
                            List finalAmtList = sqlMap.executeQueryForList("getPaymasterCalcAmt", payMap);
                            if (finalAmtList != null && finalAmtList.size() > 0) {
                                HashMap finalAmtMap = new HashMap();
                                finalAmtMap = (HashMap) finalAmtList.get(0);
                                totAmt += CommonUtil.convertObjToDouble(finalAmtMap.get("AMOUNT"));
                            }
                            finalAmt = totAmt * percent / 100;

                            if (finalAmt < minAmt) {
                                objEarnDeduPay.setAmount(CommonUtil.convertObjToDouble(Math.round(minAmt)));
                            } else if (finalAmt > minAmt) {
                                objEarnDeduPay.setAmount(CommonUtil.convertObjToDouble(Math.round(finalAmt)));
                            }
                            if (finalAmt > maxAmt) {
                                objEarnDeduPay.setAmount(CommonUtil.convertObjToDouble(Math.round(maxAmt)));
                            } else if (finalAmt < maxAmt) {
                                objEarnDeduPay.setAmount(CommonUtil.convertObjToDouble(Math.round(finalAmt)));
                            }
                            objEarnDeduPay.setPayCode(payCode);
                            objEarnDeduPay.setFromDate(fromDate);
                            objEarnDeduPay.setActive(active);
                            objEarnDeduPay.setSrlNo(CommonUtil.convertObjToInt(srlNo));
                            sqlMap.executeUpdate("insertPayMaster", objEarnDeduPay);
                        }
                    }
                } else {
                    HashMap where = new HashMap();
                    where.put("EMPID", empId);
                    if (objsalSalaryStructTO.getStopPayt().equals("NO")) {
                        where.put("ACTIVE", "Y");
                        sqlMap.executeUpdate("UpdateEmployeeDetails", where);
                    } else if (objsalSalaryStructTO.getStopPayt().equals("YES")) {
                        where.put("ACTIVE", "N");
                        sqlMap.executeUpdate("UpdateEmployeeDetails", where);
                    }
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private String getEmployeeID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", "EMPLOYEE_ID");
        return CommonUtil.convertObjToStr((dao.executeQuery(where)).get(CommonConstants.DATA));
    }

    private void storePicture() throws Exception {
        //StringBuffer targetFileName = new StringBuffer("E:\\jboss-3.2.1_tomcat-4.1.24\\server\\default\\deploy\\truetransact.war\\employee\\");
        StringBuffer targetFileName = new StringBuffer(ServerConstants.SERVER_PATH).append(EMPLOYEE_PHOTO);
        targetFileName.append(objEmployeeDetailsTO.getPhotoFile());
        FileOutputStream writer = new FileOutputStream(targetFileName.toString());
        writer.write(photoByteArray);
        writer.flush();
        writer.close();
    }

    private void updateData() throws Exception {
        objEmployeeDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
        objsalSalaryStructTO.setStatus(CommonConstants.STATUS_MODIFIED);
        objsalSalaryStructTO.setSrlNo(1);
//        if (objEmployeeDetailsTO.getPhotoFile() != null && photoByteArray != null) {
//            objEmployeeDetailsTO.setPhotoFile(objEmployeeDetailsTO.getEmployeeCode() + CommonUtil.convertObjToStr(objEmployeeDetailsTO.getPhotoFile()));
//            //Method to Store the Picture...
//            storePicture();
//        }

        String empCode = "";
        empCode = objEmployeeDetailsTO.getEmployeeCode();
        List empIdList = null;
        empIdList = sqlMap.executeQueryForList("getEmpOtherDetailsId", empCode);
        if (empIdList != null && empIdList.size() > 0) {
            sqlMap.executeUpdate("updateEmplOtherDetailsTo", objEmployeeDetailsTO);
        } else {
            sqlMap.executeUpdate("insertEmplOtherDetailsTo", objEmployeeDetailsTO);
        }
        sqlMap.executeUpdate("updateEmployeeMastrDetailsTO", objEmployeeDetailsTO);
        sqlMap.executeUpdate("updateEmplSalStructureTo", objsalSalaryStructTO);
        sqlMap.executeUpdate("updateEmplPresentDetailsTo", objsalSalaryStructTO);
    }

    private void deleteData() throws Exception {
        objEmployeeDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
        sqlMap.startTransaction();
        sqlMap.executeUpdate("deleteEmployeeDetailsTO", objEmployeeDetailsTO);
        sqlMap.commitTransaction();
    }

    public static void main(String str[]) {
    }

    public HashMap execute(HashMap map) throws Exception {
        if (map.containsKey("AuthMap") && map.get("AuthMap") != null && map.containsKey("PayCodesList") && map.get("PayCodesList") != null) {
            if (map.containsKey("EarnDeduPayTO") && map.get("EarnDeduPayTO") != null) {
                objEarnDeduPay = (EarnDeduPayTO) map.get("EarnDeduPayTO");
                String payCommand = objEarnDeduPay.getCommand();
                if (payCommand.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertPayDetails(map, objEarnDeduPay);
                }
            }
        } else {
            returnMap = new HashMap();
            _branchCode = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
            currDt = ServerUtil.getCurrentDate(_branchCode);
            objEmployeeDetailsTO = (EmployeeDetailsTO) map.get("EmployeeDetailsTO");
            objEmployeeAddrTO = (EmployeeAddrTO) map.get("EmployeeAddrTO");
            objsalSalaryStructTO = (SalaryStructTO) map.get("SalaryDetailsTO");
            photoByteArray = (byte[]) map.get("photo");
            String command = objEmployeeDetailsTO.getCommand();
            try {
                sqlMap.startTransaction();
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertData(map);
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData();
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData();
                } else {
                    throw new NoCommandException();
                }
                sqlMap.commitTransaction();
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw new TransRollbackException(e);
            }
            destroyObjects();
            return returnMap;
        }
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = CommonUtil.convertObjToStr(obj.get(CommonConstants.BRANCH_ID));
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objEmployeeDetailsTO = null;
        objEmployeeAddrTO = null;
        objsalSalaryStructTO = null;
    }

    private Date setproperDate(Date roiDt) {
        if (roiDt != null) {
            Date roiCurrDt = (Date) currDt.clone();
            roiCurrDt.setDate(roiDt.getDate());
            roiCurrDt.setMonth(roiDt.getMonth());
            roiCurrDt.setYear(roiDt.getYear());
            return (roiCurrDt);
        }
        return null;
    }

    private void insertPayDetails(HashMap map, EarnDeduPayTO objEarnDeduPay) throws Exception {
        List payCodesList = null;
        payCodesList = (List) map.get("PayCodesList");
        EmployeeDetailsTO objEmpDetTo = (EmployeeDetailsTO) map.get("AuthMap");
        SalaryStructTO objsalSalaryStructTO = (SalaryStructTO) map.get("SalaryDetailsTO");
        if (objEmpDetTo != null && payCodesList != null && payCodesList.size() > 0 && objsalSalaryStructTO.getStatusOfEmp().equals("Service")) {
            insertPayCodesData(objEmpDetTo, objsalSalaryStructTO, objEarnDeduPay);
        }
    }
}
