/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * IncrementDAO.java
 *
 * Created on Fri Nov 14 10:00:00 IST 2014
 */
package com.see.truetransact.serverside.payroll.increment;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.payroll.increment.IncrementTO;
import java.util.regex.Pattern;
import java.util.Date;
/**
 * Increment DAO.
 *
 */
public class IncrementDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private IncrementTO objIncrementTO;
    private String userID = "";
    private String remarks = "";
    private String incr = "";
    private HashMap agentDetailMap = new HashMap();
    private TransactionDAO transactionDAO;
    final int EDIT = 0, DELETE = 1, AUTHORIZE = 2, AGENTID = 3, ACCNO = 4, DEPOSITNO = 5, VIEW = 10, ACCTHDID = 6;
    private HashMap returnMap;
    private String incrId = "";
    private Date currDt = null;
    /**
     * Creates a new instance of AgentDAO
     */
    public IncrementDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        //		String where = (String) map.get(CommonConstants.MAP_WHERE);
        int type = CommonUtil.convertObjToInt(map.get("VIEWTYPE"));
        List list = null;
        if (type == EDIT) {
            list = (List) sqlMap.executeQueryForList("getHeadConsolidationEditTO", map);
        }
        if (type == AUTHORIZE) {
            list = (List) sqlMap.executeQueryForList("selectHeadConsolidationAuthTO", map);
        }
        returnMap.put("PaymentVoucherTO", list);
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        list = null;
        return returnMap;
    }

    private void insertData(LogDAO objLogDAO, LogTO objLogTO, HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            incrId = getIncrementId();
            objIncrementTO.setCreatedBy(userID);
            objIncrementTO.setCreatedDt(currDt);
            objIncrementTO.setStatus(CommonConstants.STATUS_CREATED);
            objIncrementTO.setStatusBy(userID);
            objIncrementTO.setStatusDt(ServerUtil.getCurrentDateWithTime(_branchCode));
            objIncrementTO.setBranchId(objLogTO.getBranchId());
            objIncrementTO.setIncrID(incrId);
            objIncrementTO.setActive("Y");
            objIncrementTO.setCalcUpto(currDt);
            List list = caclIncrement(map);
            HashMap empMap = new HashMap();
            empMap.put("EMPID", objIncrementTO.getEmployeeId());
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    int slno = 0;
                    HashMap incrMap = (HashMap) list.get(i);
                    System.out.println("incr map" + incrMap);
                    if (Math.round(CommonUtil.convertObjToDouble(incrMap.get("AMOUNT"))) > 0) {
                        empMap.put("PAY_CODE", incrMap.get("PAY_CODE"));
                        List serialList = ServerUtil.executeQuery("getMaxSerialNoPaymaster", empMap);
                        if (serialList != null && serialList.size() > 0) {
                            HashMap sno = (HashMap) serialList.get(0);
                            slno = CommonUtil.convertObjToInt(sno.get("SLNO")) + 1;
                        }
                        objIncrementTO.setSerialNo(slno);
                        objIncrementTO.setPayCode(CommonUtil.convertObjToStr(incrMap.get("PAY_CODE")));
                        objIncrementTO.setAmount(CommonUtil.convertObjToDouble(Math.round(CommonUtil.convertObjToDouble(incrMap.get("AMOUNT")))));
                        List actDataLst = ServerUtil.executeQuery("getAccountDataForEmplyeeByPayCode", empMap); // Added by nithya on 24-05-2021 for KD-2788
                        if(actDataLst != null && actDataLst.size() > 0){
                          HashMap actMap = (HashMap)actDataLst.get(0);
                          if(actMap.containsKey("PROD_TYPE") && actMap.get("PROD_TYPE") != null && !actMap.get("PROD_TYPE").equals("GL")){
                             objIncrementTO.setProdType(CommonUtil.convertObjToStr(actMap.get("PROD_TYPE"))); 
                             objIncrementTO.setProdId(CommonUtil.convertObjToStr(actMap.get("PROD_ID")));
                             objIncrementTO.setAccNo(CommonUtil.convertObjToStr(actMap.get("ACC_NO")));
                          }else{
                        objIncrementTO.setProdType(TransactionFactory.GL);
                          }
                        }else{
                          objIncrementTO.setProdType(TransactionFactory.GL);
                        }
                        sqlMap.executeUpdate("insertIntoPayMaster", objIncrementTO);
                    }
                }
            }
            sqlMap.executeUpdate("insertPayrollIncrement", objIncrementTO);
            int slno = 0;
            List serialList = ServerUtil.executeQuery("getMaxSerialEmpPresDetails", empMap);
            if (serialList != null && serialList.size() > 0) {
                HashMap sno = (HashMap) serialList.get(0);
                slno = CommonUtil.convertObjToInt(sno.get("SLNO")) + 1;
                objIncrementTO.setSerialNo(slno);
            }
            sqlMap.executeUpdate("insertEmployeePresentDetails", objIncrementTO);
            sqlMap.commitTransaction();
            objLogTO.setData(objIncrementTO.toString());
            objLogTO.setPrimaryKey(objIncrementTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private List caclIncrement(HashMap map) {
        System.out.println("map here " + map);
        double basicPay = 0.0;
        String basicCode = "";
        HashMap resultMap = new HashMap();
        List list = new ArrayList();
        Double basic = CommonUtil.convertObjToDouble(objIncrementTO.getNewBasicSal());
        HashMap paramMap = new HashMap();
        List basicPayCodeList = ServerUtil.executeQuery("getPayCodeBasic", paramMap);
        if (basicPayCodeList != null && basicPayCodeList.size() > 0) {
            HashMap basicCodeMap = (HashMap) basicPayCodeList.get(0);
            basicCode = CommonUtil.convertObjToStr(basicCodeMap.get("PAY_CODE"));
            resultMap.put("PAY_CODE", basicCode);
            resultMap.put("AMOUNT", basic);
            list.add(resultMap);
        }
        HashMap hashMap = new HashMap();
        List paycodes = ServerUtil.executeQuery("getPayCodesMaster", hashMap);
        String pay1 = "";
        Double payAmt1 = 0.0;
        String pay2 = "";
        Double payAmt2 = 0.0;
        if (paycodes != null && paycodes.size() > 0) {
            for (int i = 0; i < paycodes.size(); i++) {     
                Double amount = 0.0;
                HashMap payMap = (HashMap) paycodes.get(i);
                if (CommonUtil.convertObjToStr(payMap.get("PAY_CALC_ON")).equals(basicCode)) {
                    amount = amountCal(payMap,basic);
                    resultMap = new HashMap();
                    resultMap.put("PAY_CODE", CommonUtil.convertObjToStr(payMap.get("PAY_CODE")));
                    resultMap.put("AMOUNT", amount);
                    list.add(resultMap);
                } else {
                    amount = 0.0;
                    amount = calcPay(payMap, basicCode, list);
                    if (payMap.containsKey("PAY_CODE")) {
                        resultMap = new HashMap();
                        resultMap.put("PAY_CODE", payMap.get("PAY_CODE"));
                        resultMap.put("AMOUNT", amount);
                        list.add(resultMap);
                    }
                }
            }
            System.out.println("final list herte" + list);
        }
        return list;
    }

    private double calcPay(HashMap hashMap, String basic, List list) {
        double amount = 0.0;
        HashMap newhash = new HashMap();
        HashMap result = new HashMap();
        String[] splits = CommonUtil.convertObjToStr(hashMap.get("PAY_CALC_ON")).split(Pattern.quote("+"));
        for (int j = 0; j < splits.length; j++) {
            HashMap codeSetMap = new HashMap();
            codeSetMap.put("PAY_CODE", splits[j]);
            List paycodeSettings = ServerUtil.executeQuery("getPayCodeSetting", codeSetMap);
            if (paycodeSettings != null && paycodeSettings.size() > 0) {
                result = (HashMap) paycodeSettings.get(0);
                String payCalc = CommonUtil.convertObjToStr(result.get("PAY_CALC_ON"));
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        newhash = (HashMap) list.get(i);
                        if (CommonUtil.convertObjToStr(result.get("PAY_CODE")).equals(CommonUtil.convertObjToStr(newhash.get("PAY_CODE")))) {
                            amount = amount + CommonUtil.convertObjToDouble(newhash.get("AMOUNT"));
                            System.out.println("amount here" + amount);
                        }
                    }
                }

            }
        }
        amount = amountCal(hashMap, amount);
        return amount;
    }

    private double amountCal(HashMap map, Double amount) {
                double percent = CommonUtil.convertObjToDouble(map.get("PAY_PERCENT"));
                System.out.println("calcBAsed --percent" + percent);
                amount = percent * amount / 100.0;
                if (CommonUtil.convertObjToDouble(map.get("PAY_MIN_AMT")) <= amount) {
                    if (amount <= CommonUtil.convertObjToDouble(map.get("PAY_MAX_AMT"))) {
                        amount = amount;
                    } else {
                        amount = CommonUtil.convertObjToDouble(map.get("PAY_MAX_AMT"));
                    }
                } else {
                    amount = CommonUtil.convertObjToDouble(map.get("PAY_MIN_AMT"));
                }
//            }
//        }
        System.out.println("return amt here" + amount);
        return amount;
    }

    private double calcBasedBasic(double basic) {

        return 0;

    }

    private void updateData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
    }

    private void deleteData(LogDAO objLogDAO, LogTO objLogTO, HashMap map) throws Exception {
        System.out.println("obj incr to here"+objIncrementTO);
        HashMap paramMap = new HashMap();
        paramMap.put("INCRID", objIncrementTO.getIncrID());
        paramMap.put("EMPID", objIncrementTO.getEmployeeId());
        sqlMap.executeUpdate("deleteFromPayMasterIncr", paramMap);
        sqlMap.executeUpdate("deleteEmployeePresentDetailsIncr", paramMap);
        sqlMap.executeUpdate("deletePayrollIncrement", paramMap);      
    }

    public static void main(String str[]) {
        try {
            IncrementDAO dao = new IncrementDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("head Map Dao : " + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        returnMap = new HashMap();
        LogDAO objLogDAO = new LogDAO();
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        objLogTO.setSelectedBranchId((String) map.get(CommonConstants.SELECTED_BRANCH_ID));
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));
        userID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
        objIncrementTO = (IncrementTO) map.get("EmpIncrementTO");
        final String command = objIncrementTO.getCommand();
        System.out.println("objincremento shi" + objIncrementTO);
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(objLogDAO, objLogTO, map);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
//            insertData(objLogDAO, objLogTO, map);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData(objLogDAO, objLogTO, map);
        } else {
            throw new NoCommandException();
        }
        objLogDAO = null;
        objLogTO = null;
        destroyObjects();
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        returnMap.put("INCRID", incrId);
        incrId = "";
        return returnMap;
    }
    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objIncrementTO = null;
    }

    private String getIncrementId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "INCREMENT_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }
}
