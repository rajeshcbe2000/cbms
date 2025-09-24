/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * daoEarningsDeductionDAO.java
 *
 * 
 */
package com.see.truetransact.serverside.payroll.earningsDeductions;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.payroll.earningsDeductions.EarnDeduAccTO;
import com.see.truetransact.transferobject.payroll.earningsDeductions.EarnDeduPaySettingsTO;
import com.see.truetransact.transferobject.payroll.earningsDeductions.EarnDeduPayTO;
import com.see.truetransact.transferobject.payroll.earningsDeductions.EarningsDeductionTO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * EarningsDeductionDAO.
 *
 * @author anjuanand
 *
 */
public class EarningsDeductionDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private EarningsDeductionTO objEarningsDeduction;
    private EarnDeduAccTO objEarnDeduAcc;
    private EarnDeduPayTO objEarnDeduPay;
    private EarnDeduPaySettingsTO objEarnDeduPaySettingsTO;
    private ArrayList accList;
    private ArrayList calcList;
    private Date curr_dt = null;
    HashMap returnMap;

    /**
     * Creates a new instance of EarningsDeductionDAO
     */
    public EarningsDeductionDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap resultMap = new HashMap();
        String where = CommonUtil.convertObjToStr(map.get(CommonConstants.MAP_WHERE));
        List list = null;
        if (map.containsKey("PAY_PROD_TYPE") && map.get("PAY_PROD_TYPE").equals("GL")) {
            list = (List) sqlMap.executeQueryForList("getSelectEarnDedu", where);
        } else {
            list = (List) sqlMap.executeQueryForList("getSelectEarnDeduNonGL", where);
        }
        resultMap.put("EarningsDeduction", list);
        list = (List) sqlMap.executeQueryForList("getSelectEarnDeduAcc", where);
        resultMap.put("EarnDeduAccount", list);
        return resultMap;
    }

    public HashMap execute(HashMap map) throws Exception {
        try {
            returnMap = new HashMap();
            _branchCode = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
            curr_dt = ServerUtil.getCurrentDate(_branchCode);
            objEarningsDeduction = (EarningsDeductionTO) map.get("EARNDEDU");
            objEarnDeduAcc = (EarnDeduAccTO) map.get("EARNDEDUACC");
            final String command = CommonUtil.convertObjToStr(objEarningsDeduction.getCommand());
            objEarnDeduPay = (EarnDeduPayTO) map.get("PAYDATA");
            objEarnDeduPaySettingsTO = (EarnDeduPaySettingsTO) map.get("SETTINGSDATA");
            if (objEarningsDeduction != null || objEarnDeduAcc != null) {
                if (objEarningsDeduction.getPay_Calc_Type().equals("Calculated")) {
                    objEarnDeduPaySettingsTO.setPayFixAmt(0.0);
                    objEarnDeduPaySettingsTO.setFromDate(setproperDate(objEarnDeduPaySettingsTO.getFromDate()));
                } else {
                    objEarnDeduPaySettingsTO.setPayFixAmt(objEarningsDeduction.getPay_Fix_Amt());
                    objEarnDeduPaySettingsTO.setFromDate(curr_dt);
                    objEarnDeduPaySettingsTO.setPayCalcOn("");
                }
                  sqlMap.startTransaction();
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    InsertEmployeeSalaryDetails(map, objEarnDeduPaySettingsTO, objEarnDeduPay);
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateEarnDeduData(map, objEarnDeduPaySettingsTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteEarnDeduData(objEarningsDeduction);
                } else {
                    throw new NoCommandException();
                }
                
                sqlMap.commitTransaction();
                
            }
        } catch (SQLException ex) {
            sqlMap.rollbackTransaction();
            ex.printStackTrace();
            throw new TransRollbackException(ex);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
        return returnMap;
    }

    public void InsertEmployeeSalaryDetails(HashMap map, EarnDeduPaySettingsTO objEarnDeduPaySettingsTO, EarnDeduPayTO objEarnDeduPay) throws Exception {
        try {
            if (map.containsKey("EARNDEDU") && map.get("EARNDEDU") != null) {
                objEarningsDeduction = (EarningsDeductionTO) map.get("EARNDEDU");
            }
            if (map.containsKey("ACCDATA") && map.get("ACCDATA") != null) {
                accList = (ArrayList) map.get("ACCDATA");
                if (accList.size() > 0) {
                    InsertPaycodesMaster(accList, map);
                } else {
                    InsertPaycodesMaster(null, map);
                }
            } else if (map.containsKey("CALCDATA") && map.get("CALCDATA") != null) {
                calcList = (ArrayList) map.get("CALCDATA");
                if (calcList.size() > 0) {
                    InsertPaycodesMaster(calcList, map);
                } else {
                    InsertPaycodesMaster(null, map);
                }
            }
            if (objEarnDeduPaySettingsTO != null && objEarningsDeduction.getPay_Prod_Type().equals("GL")) {
                objEarnDeduPaySettingsTO.setPaycode_Id(objEarningsDeduction.getPaycode_Id());
                objEarnDeduPaySettingsTO.setPayCode(objEarningsDeduction.getPay_Code());
                InsertPaySettings(map, objEarnDeduPaySettingsTO);
            }
            if (objEarnDeduPay != null && objEarningsDeduction.getIndividual_reqd().equals("N") && (accList != null && accList.size() > 0)) {
                InsertPayMaster(map, objEarnDeduPaySettingsTO, objEarnDeduPay);
            }
        } catch (SQLException ex) {
            sqlMap.rollbackTransaction();
            ex.printStackTrace();
            throw new TransRollbackException(ex);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = CommonUtil.convertObjToStr(obj.get(CommonConstants.BRANCH_ID));
        curr_dt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private String getPaycodeID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", "PAYCODE_ID");
        return CommonUtil.convertObjToStr((dao.executeQuery(where)).get(CommonConstants.DATA));
    }

    private String getEarnID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", "PAYCODE_EARN");
        return CommonUtil.convertObjToStr((dao.executeQuery(where)).get(CommonConstants.DATA));
    }

    private String getDeduID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", "PAYCODE_DEDU");
        return CommonUtil.convertObjToStr((dao.executeQuery(where)).get(CommonConstants.DATA));
    }

    private String getContraID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", "PAYCODE_CONTRA");
        return CommonUtil.convertObjToStr((dao.executeQuery(where)).get(CommonConstants.DATA));
    }

    private void InsertPaycodesMaster(ArrayList accList, HashMap map) throws Exception {
        objEarningsDeduction = (EarningsDeductionTO) map.get("EARNDEDU");
        String payCodeId = getPaycodeID();
        String paycode = "";
        objEarningsDeduction.setPaycode_Id(payCodeId);
        if (objEarningsDeduction.getPay_EarnDedu().equals("EARNINGS")) {
            paycode = getEarnID();
            objEarningsDeduction.setPay_Code(paycode);
        } else if (objEarningsDeduction.getPay_EarnDedu().equals("DEDUCTIONS")) {
            paycode = getDeduID();
            objEarningsDeduction.setPay_Code(paycode);
        } else if (objEarningsDeduction.getPay_EarnDedu().equals("CONTRA")) {
            paycode = getContraID();
            objEarningsDeduction.setPay_Code(paycode);
        }
        objEarningsDeduction.setStatus("CREATED");
        try {
            sqlMap.executeUpdate("insertPaycodes_Master", objEarningsDeduction);
            returnMap.put("PayCodeId", payCodeId);
            returnMap.put("PayCode", paycode);
        } catch (SQLException ex) {
            sqlMap.rollbackTransaction();
            ex.printStackTrace();
            throw new TransRollbackException(ex);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
        int accSize = 0;
        if (accList != null && accList.size() > 0) {
            accSize = accList.size();
        }
        if (accSize > 0) {

            for (int i = 0; i < accSize; i++) {
                objEarnDeduAcc = (EarnDeduAccTO) accList.get(i);
                objEarnDeduAcc.setPayCode_Id(payCodeId);
                objEarnDeduAcc.setAccHd(objEarnDeduAcc.getAccHd());
                objEarnDeduAcc.setAccType(objEarnDeduAcc.getAccType());
                try {
                    sqlMap.executeUpdate("insertPay_Account", objEarnDeduAcc);
                } catch (SQLException ex) {
                    sqlMap.rollbackTransaction();
                    ex.printStackTrace();
                    throw new TransRollbackException(ex);
                } catch (Exception e) {
                    sqlMap.rollbackTransaction();
                    e.printStackTrace();
                    throw new TransRollbackException(e);
                }
            }
        }
    }

    private void deleteEarnDeduData(EarningsDeductionTO objEarningsDeduction) throws Exception {
        try {
            String payCode = "";
            payCode = objEarningsDeduction.getPay_Code();
            HashMap accMap = new HashMap();
            accMap.put("payCode", payCode);
            sqlMap.executeUpdate("deletePayMaster", accMap);
            sqlMap.executeUpdate("deletePaySettings", accMap);
            HashMap data = new HashMap();
            data.put("PAYCODEID", objEarningsDeduction.getPaycode_Id());
            sqlMap.executeUpdate("deleteEarnDeduAccTO", data);
            sqlMap.executeUpdate("deleteEarnDeduTO", data);
        } catch (SQLException ex) {
            sqlMap.rollbackTransaction();
            ex.printStackTrace();
            throw new TransRollbackException(ex);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateEarnDeduData(HashMap map, EarnDeduPaySettingsTO objEarnDeduPaySettingsTO) throws Exception {
        try {
            if (map.containsKey("EARNDEDU") && map.get("EARNDEDU") != null) {
                objEarningsDeduction = (EarningsDeductionTO) map.get("EARNDEDU");
                sqlMap.executeUpdate("updatePayCodesMasterTO", objEarningsDeduction);
                if (objEarnDeduPaySettingsTO != null && objEarningsDeduction.getPay_Prod_Type().equals("GL")) {
                    objEarnDeduPaySettingsTO.setPayCode(objEarningsDeduction.getPay_Code());
                    InsertPaySettings(map, objEarnDeduPaySettingsTO);
                }
                if (map.containsKey("ACCDATA") && map.get("ACCDATA") != null) {
                    accList = (ArrayList) map.get("ACCDATA");
                    int accSize = accList.size();
                    if (accSize > 0) {
                        for (int i = 0; i < accSize; i++) {
                            objEarnDeduAcc = (EarnDeduAccTO) accList.get(i);
                            objEarnDeduAcc.setAccHd(objEarnDeduAcc.getAccHd());
                            objEarnDeduAcc.setAccType(objEarnDeduAcc.getAccType());
                            objEarnDeduAcc.setPayCode_Id(objEarningsDeduction.getPaycode_Id());
                            sqlMap.executeUpdate("updatePayCodesAccountTO", objEarnDeduAcc);
                        }
                    }
                    HashMap payCodeMap = new HashMap();
                    payCodeMap.put("PAYCODE", objEarningsDeduction.getPay_Code());
                    List payCodesList = null;
                    payCodesList = sqlMap.executeQueryForList("getPayMasterPayCode", payCodeMap);
                    String payCode = "";
                    if (payCodesList != null && payCodesList.size() > 0) {
                        HashMap payCodesMap = new HashMap();
                        for (int k = 0; k < payCodesList.size(); k++) {
                            payCodesMap = (HashMap) payCodesList.get(k);
                            payCode = CommonUtil.convertObjToStr(payCodesMap.get("PAY_CODE"));
                            if (objEarningsDeduction.getPay_Code().equals(payCode) && objEarningsDeduction.getIndividual_reqd().equals("N")) {
                                updatePayMaster(map, objEarnDeduPaySettingsTO, objEarnDeduPay);
                            }
                        }
                    } else {
                        if (objEarningsDeduction.getIndividual_reqd().equals("N")) {
                            InsertPayMaster(map, objEarnDeduPaySettingsTO, objEarnDeduPay);
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            sqlMap.rollbackTransaction();
            ex.printStackTrace();
            throw new TransRollbackException(ex);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void InsertPayMaster(HashMap map, EarnDeduPaySettingsTO objEarnDeduPaySettingsTO, EarnDeduPayTO objEarnDeduPay) throws Exception {
        try {
            if (map.containsKey("EMPID") && map.get("EMPID") != null) {
                List payList = (List) map.get("EMPID");
                int paySize = payList.size();
                if (payList != null && paySize > 0) {
                    for (int i = 0; i < paySize; i++) {
                        HashMap empMap = new HashMap();
                        empMap = (HashMap) payList.get(i);
                        String empId = CommonUtil.convertObjToStr(empMap.get("EMPLOYEEID"));
                        double basicAmt = CommonUtil.convertObjToDouble(empMap.get("PRESENT_BASIC"));
                        objEarnDeduPay.setEmployeeId(empId);
                        objEarnDeduPay.setPayCode(objEarningsDeduction.getPay_Code());
                        HashMap empIdMap = new HashMap();
                        empIdMap.put("EMPID", empId);
                        HashMap payCodeMap = new HashMap();
                        payCodeMap.put("PAYCODE", objEarningsDeduction.getPay_Code());
                        String basic = "";
                        List basicList = sqlMap.executeQueryForList("getBasicPayCode", null);
                        if (basicList != null && basicList.size() > 0) {
                            HashMap basicMap = new HashMap();
                            basicMap = (HashMap) basicList.get(0);
                            basic = CommonUtil.convertObjToStr(basicMap.get("PAY_CODE"));
                        }
                        objEarnDeduPay.setSrlNo(CommonUtil.convertObjToInt("1"));
                        if (objEarningsDeduction.getPay_Prod_Type().equals(CommonConstants.GL_TRANSMODE_TYPE)) {
                            objEarnDeduPay.setProdType(CommonConstants.GL_TRANSMODE_TYPE);
                        }
                        if (objEarningsDeduction.getPay_Calc_Type().equals("Fixed") && objEarningsDeduction.getPay_Mod_Type().equals("BASICPAY")) {
                            objEarnDeduPay.setAmount(basicAmt);
                        } else if (objEarningsDeduction.getPay_Calc_Type().equals("Fixed") && !objEarningsDeduction.getPay_Mod_Type().equals("BASICPAY")) {
                            objEarnDeduPay.setAmount(objEarningsDeduction.getPay_Fix_Amt());
                        } else if (objEarningsDeduction.getPay_Calc_Type().equals("Calculated")) {
                            double finalamount = 0;
                            double percent = 0.0;
                            percent = CommonUtil.convertObjToDouble(objEarningsDeduction.getPay_Percent());
                            double minAmt = 0.0;
                            double maxAmt = 0.0;
                            minAmt = objEarningsDeduction.getPay_Min_Amt();
                            maxAmt = objEarningsDeduction.getPay_Max_Amt();
                            double totalVal = 0.0;
                            String pay_calc_on = "";
                            pay_calc_on = objEarningsDeduction.getPay_Calc_On();
                            String paycode = "";
                            String[] payCalc = pay_calc_on.split(Pattern.quote("+"));
                            int k = 0;
                            for (String s : payCalc) {
                                if (k == 0) {
                                    paycode = "'" + s + "'";
                                } else {
                                    paycode = paycode + "," + "'" + s + "'";
                                }
                                k++;
                            }
                            HashMap payMap = new HashMap();
                            payMap.put("PAYCODE", paycode);
                            payMap.put("EMPID", empId);
                            List finalAmtList = sqlMap.executeQueryForList("getPaymasterCalcAmt", payMap);
                            if (finalAmtList != null && finalAmtList.size() > 0) {
                                HashMap finalAmtMap = new HashMap();
                                finalAmtMap = (HashMap) finalAmtList.get(0);
                                finalamount = finalamount + CommonUtil.convertObjToDouble(finalAmtMap.get("AMOUNT"));
                            }
                            totalVal = finalamount * percent / 100;
                            if (totalVal < minAmt) {
                                objEarnDeduPay.setAmount(CommonUtil.convertObjToDouble(Math.round(minAmt)));
                            } else if (totalVal > minAmt) {
                                objEarnDeduPay.setAmount(CommonUtil.convertObjToDouble(Math.round(totalVal)));
                            }
                            if (totalVal > maxAmt) {
                                objEarnDeduPay.setAmount(CommonUtil.convertObjToDouble(Math.round(maxAmt)));
                            } else if (totalVal < maxAmt) {
                                objEarnDeduPay.setAmount(CommonUtil.convertObjToDouble(Math.round(totalVal)));
                            }
                        }
                        sqlMap.executeUpdate("insertPayMaster", objEarnDeduPay);
                    }
                }
            }
        } catch (SQLException ex) {
            sqlMap.rollbackTransaction();
            ex.printStackTrace();
            throw new TransRollbackException(ex);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updatePayMaster(HashMap map, EarnDeduPaySettingsTO objEarnDeduPaySettingsTO, EarnDeduPayTO objEarnDeduPay) throws Exception {
        try {
            if (map.containsKey("EMPID") && map.get("EMPID") != null) {
                List empList = (List) map.get("EMPID");
                int global_Srl_N0=0;
                if (empList != null && empList.size() > 0) {
                    for (int i = 0; i < empList.size(); i++) {
                        HashMap empMap = new HashMap();
                        empMap = (HashMap) empList.get(i);
                        String empId = CommonUtil.convertObjToStr(empMap.get("EMPLOYEEID"));
                        double basicAmt = 0.0;
                        basicAmt = CommonUtil.convertObjToDouble(empMap.get("PRESENT_BASIC"));
                        if (map != null && map.containsKey("EARNDEDU")) {
                            EarningsDeductionTO objEarningsDeduction = (EarningsDeductionTO) map.get("EARNDEDU");
                            String paycode = "";
                            paycode = objEarningsDeduction.getPay_Code();
                            HashMap payCodeMap = new HashMap();
                            payCodeMap.put("PAYCODE", paycode);
                            payCodeMap.put("EMPID", empId);
                            objEarnDeduPay.setEmployeeId(empId);
                            objEarnDeduPay.setPayCode(paycode);
                            objEarnDeduAcc.setPayCode_Id(objEarningsDeduction.getPaycode_Id());
                            objEarnDeduPay.setStatus("UPDATED");
                            objEarnDeduPay.setProdType(objEarningsDeduction.getPay_Prod_Type());
                            objEarnDeduPay.setActive(objEarningsDeduction.getActive());
                            objEarnDeduPay.setStatusDate(curr_dt);
                            objEarnDeduPay.setSrlNo(CommonUtil.convertObjToInt(objEarnDeduPaySettingsTO.getSrlNo()));
                            List basicList = sqlMap.executeQueryForList("getBasicPayCode", null);
                            if (basicList != null && basicList.size() > 0) {
                                HashMap basicMap = new HashMap();
                                basicMap = (HashMap) basicList.get(0);
                                String basic = "";
                                basic = CommonUtil.convertObjToStr(basicMap.get("PAY_CODE"));
                                if (objEarningsDeduction.getPay_Calc_Type().equals("Fixed") && objEarningsDeduction.getPay_Mod_Type().equals("BASICPAY")) {
                                    objEarnDeduPay.setAmount(basicAmt);
                                } else if (objEarningsDeduction.getPay_Calc_Type().equals("Fixed") && !objEarningsDeduction.getPay_Mod_Type().equals("BASICPAY")) {
                                    objEarnDeduPay.setAmount(objEarningsDeduction.getPay_Fix_Amt());
                                } else if (objEarningsDeduction.getPay_Calc_Type().equals("Calculated")) {
                                    double finalamount = 0;
                                    double percent = 0.0;
                                    percent = CommonUtil.convertObjToDouble(objEarningsDeduction.getPay_Percent());
                                    double minAmt = 0.0;
                                    double maxAmt = 0.0;
                                    minAmt = objEarningsDeduction.getPay_Min_Amt();
                                    maxAmt = objEarningsDeduction.getPay_Max_Amt();
                                    String pay_calc_on = "";
                                    pay_calc_on = objEarningsDeduction.getPay_Calc_On();
                                    String[] payCalc = pay_calc_on.split(Pattern.quote("+"));
                                    double totalVal = 0.0;
                                    int k = 0;
                                    for (String s : payCalc) {
                                        if (k == 0) {
                                            paycode = "'" + s + "'";
                                        } else {
                                            paycode = paycode + "," + "'" + s + "'";
                                        }
                                        k++;
                                    }
                                    HashMap payMap = new HashMap();
                                    payMap.put("PAYCODE", paycode);
                                    payMap.put("EMPID", empId);
                                    List finalAmtList = sqlMap.executeQueryForList("getPaymasterCalcAmt", payMap);
                                    if (finalAmtList != null && finalAmtList.size() > 0) {
                                        HashMap finalAmtMap = new HashMap();
                                        finalAmtMap = (HashMap) finalAmtList.get(0);
                                        finalamount = finalamount + CommonUtil.convertObjToDouble(finalAmtMap.get("AMOUNT"));
                                    }
                                    totalVal = finalamount * percent / 100;
                                    if (totalVal < minAmt) {
                                        objEarnDeduPay.setAmount(CommonUtil.convertObjToDouble(Math.round(minAmt)));
                                    } else if (totalVal > minAmt) {
                                        objEarnDeduPay.setAmount(CommonUtil.convertObjToDouble(Math.round(totalVal)));
                                    }
                                    if (totalVal > maxAmt) {
                                        objEarnDeduPay.setAmount(CommonUtil.convertObjToDouble(Math.round(maxAmt)));
                                    } else if (totalVal < maxAmt) {
                                        objEarnDeduPay.setAmount(CommonUtil.convertObjToDouble(Math.round(totalVal)));
                                    }
                                }
                                Date frmDate = objEarnDeduPay.getFromDate();
                                List payFromDateList = sqlMap.executeQueryForList("getPaySettingsFromDate", payCodeMap);
                                if (payFromDateList != null && payFromDateList.size() > 0) {
                                    HashMap payFromDateMap = new HashMap();
                                    payFromDateMap = (HashMap) payFromDateList.get(0);
                                    Date fromDate = null;
                                    fromDate = setproperDate((Date) payFromDateMap.get("FROM_DATE"));
                                    if (fromDate.equals(frmDate)) {
                                        sqlMap.executeUpdate("updateGlobalPayMaster", objEarnDeduPay);
                                    } else {
                                        sqlMap.executeUpdate("insertPayMaster", objEarnDeduPay);
                                    }
                                    HashMap payCodesMap = new HashMap();
                                    payCodesMap.put("PAYCODE", objEarnDeduPay.getPayCode());
                                    List selPayList = sqlMap.executeQueryForList("selectPayCode", payCodesMap);
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
                                            String active = "";
                                            active = CommonUtil.convertObjToStr(selPayMap.get("ACTIVE"));
                                            int srlNo = 1;
                                            HashMap whereMap = new HashMap();
                                            whereMap.put("PAY_CODE", payCode);
                                            whereMap.put("EMP_ID", empId);
                                            List srlNoList = sqlMap.executeQueryForList("getMaxSrl_No", whereMap);
                                            if (srlNoList != null && srlNoList.size() > 0) {
                                                HashMap srlMap = new HashMap();
                                                srlMap = (HashMap) srlNoList.get(0);
                                                if (srlMap.containsKey("SRL_NO")) {
                                                    srlNo = CommonUtil.convertObjToInt(srlMap.get("SRL_NO"));
                                                }
                                            }
                                          //  srlNo = CommonUtil.convertObjToInt(selPayMap.get("SRL_NO"));
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
                                            objEarnDeduPay.setEmployeeId(empId);
                                            objEarnDeduPay.setPayCode(payCode);
                                            objEarnDeduPay.setActive(active);
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
                                            if (fromDate.equals(frmDate)) {
                                                objEarnDeduPay.setSrlNo(CommonUtil.convertObjToInt(srlNo));
                                                sqlMap.executeUpdate("updateGlobalPayMaster", objEarnDeduPay);
                                            } else {
                                                objEarnDeduPay.setSrlNo(CommonUtil.convertObjToInt(srlNo + 1));
                                                sqlMap.executeUpdate("insertPayMaster", objEarnDeduPay);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            sqlMap.rollbackTransaction();
            ex.printStackTrace();
            throw new TransRollbackException(ex);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void InsertPaySettings(HashMap map, EarnDeduPaySettingsTO objEarnDeduPaySettingsTO) throws Exception {
        try {
            int srlNo = 1;
            HashMap paySetMap = new HashMap();
            paySetMap.put("PAYCODE", objEarnDeduPaySettingsTO.getPayCode());
            HashMap payData = new HashMap();
            HashMap payMap = new HashMap();
            payMap.put("PAYCODE", objEarnDeduPaySettingsTO.getPayCode());
            List payCalcSetList = sqlMap.executeQueryForList("getPaySetDetails", paySetMap);
            if (payCalcSetList != null && payCalcSetList.size() > 0) {
                try {
                    HashMap payCalcFrmMap = new HashMap();
                    payCalcFrmMap = (HashMap) payCalcSetList.get(0);
                    srlNo = CommonUtil.convertObjToInt(payCalcFrmMap.get("SRL_NO"));
                    String fromDate = CommonUtil.convertObjToStr(payCalcFrmMap.get("FROM_DATE"));
                    paySetMap.put("SRLNO", srlNo);
                    paySetMap.put("TODATE", DateUtil.addDays(objEarnDeduPaySettingsTO.getFromDate(),-1));
                    sqlMap.executeUpdate("updatePaySetToDate", paySetMap);
                    if (CommonUtil.convertObjToStr(objEarnDeduPaySettingsTO.getFromDate()).equals(fromDate)) {
                        payData.put("SRLNO", srlNo);
                        objEarnDeduPaySettingsTO.setSrlNo(srlNo);
                        sqlMap.executeUpdate("updatePaySettings", objEarnDeduPaySettingsTO);
                    } else {
                        List srlNoList = sqlMap.executeQueryForList("getMaxPaySrlNo", payMap);
                        int srl_No = 1;
                        if (srlNoList != null && srlNoList.size() > 0) {
                            HashMap srlNoMap = new HashMap();
                            srlNoMap = (HashMap) srlNoList.get(0);
                            srl_No = CommonUtil.convertObjToInt(srlNoMap.get("SRL_NO"));
                            objEarnDeduPaySettingsTO.setSrlNo(srl_No + 1);
                        }
                        payData.put("SRLNO", objEarnDeduPaySettingsTO.getSrlNo());
                        sqlMap.executeUpdate("insertPaySettings", objEarnDeduPaySettingsTO);
                    }
                } catch (SQLException ex) {
                    sqlMap.rollbackTransaction();
                    ex.printStackTrace();
                    throw new TransRollbackException(ex);
                } catch (Exception e) {
                    sqlMap.rollbackTransaction();
                    e.printStackTrace();
                    throw new TransRollbackException(e);
                }
            } else {
                try {
                    objEarnDeduPaySettingsTO.setToDate(null);
                    List srlNoList = sqlMap.executeQueryForList("getMaxPaySrlNo", payMap);
                    int srl_No = 1;
                    if (srlNoList != null && srlNoList.size() > 0) {
                        HashMap srlNoMap = new HashMap();
                        srlNoMap = (HashMap) srlNoList.get(0);
                        srl_No = CommonUtil.convertObjToInt(srlNoMap.get("SRL_NO"));
                        objEarnDeduPaySettingsTO.setSrlNo(srl_No + 1);
                    }
                    payData.put("SRLNO", objEarnDeduPaySettingsTO.getSrlNo());
                    sqlMap.executeUpdate("insertPaySettings", objEarnDeduPaySettingsTO);
                } catch (SQLException ex) {
                    sqlMap.rollbackTransaction();
                    ex.printStackTrace();
                    throw new TransRollbackException(ex);
                } catch (Exception e) {
                    sqlMap.rollbackTransaction();
                    e.printStackTrace();
                    throw new TransRollbackException(e);
                }
            }
            payData.put("PAYCODE", objEarnDeduPaySettingsTO.getPayCode());
            payData.put("STATUSDATE", curr_dt);
            if (map.containsKey("PAYDATA") && map.get("PAYDATA") != null) {
                objEarnDeduPay = (EarnDeduPayTO) map.get("PAYDATA");
                objEarnDeduPay.setFromDate(objEarnDeduPaySettingsTO.getFromDate());
                if (objEarnDeduPaySettingsTO.getToDate() == null) {
                    objEarnDeduPay.setToDate(null);
                } else {
                    objEarnDeduPay.setToDate(objEarnDeduPaySettingsTO.getToDate());
                }
            }
        } catch (SQLException ex) {
            sqlMap.rollbackTransaction();
            ex.printStackTrace();
            throw new TransRollbackException(ex);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private Date setproperDate(Date roiDt) {
        if (roiDt != null) {
            Date roiCurrDt = (Date) curr_dt.clone();
            roiCurrDt.setDate(roiDt.getDate());
            roiCurrDt.setMonth(roiDt.getMonth());
            roiCurrDt.setYear(roiDt.getYear());
            return (roiCurrDt);
        }
        return null;
    }
}