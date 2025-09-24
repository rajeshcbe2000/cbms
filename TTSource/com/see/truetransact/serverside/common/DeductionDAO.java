/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * DeductionDAO.java
 *
 * Created on Wed May 26 10:59:57 GMT+05:30 2004
 */
package com.see.truetransact.serverside.common;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.common.DeductionTO;
import com.see.truetransact.transferobject.common.EarningTO;
import com.see.truetransact.transferobject.common.SalaryDeductionTO;
//import com.see.truetransact.transferobject.common.GratuityTO;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.Iterator;

/**
 * Deduction DAO.
 *
 * @author Sathiya
 *
 */
public class DeductionDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private DeductionTO objDeductionTO;
    private EarningTO objEDTO;
    private Iterator addressIterator;
    private SalaryDeductionTO objSLTO;
    private String _userId = "";
    private HashMap data;
    private LinkedHashMap earningMap;
    private LinkedHashMap deductionMap;
    private HashMap deletedEarningMap;
    private HashMap deletedDeductionMap;
    private String key;
    private LogDAO logDAO;
    private LogTO logTO;
    private String addressKey = new String();
    //    private MisecllaniousDeductionTO objMDTO;
    //    private GratuityTO objGATO;
    private ArrayList DeductionTOs, EarningTOs, SalaryListTOs;
    private ArrayList deleteDeductionList, deleteEarningList;
    HashMap resultMap = new HashMap();
    //    HashMap employeeMap = new HashMap();
    Date currDt = null;

    /**
     * Creates a new instance of DeductionDAO
     */
    public DeductionDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String mapStr = (String) map.get(CommonConstants.MAP_NAME);
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        //        map = null;
        System.out.println("@!#!@#@!!@#where" + where);
        if (map.containsKey("EARNING")) {
            List list = (List) sqlMap.executeQueryForList(mapStr, map);
            if (list.size() > 0) {
                earningMap = new LinkedHashMap();
                for (int i = list.size(), j = 0; i > 0; i--, j++) {
                    earningMap.put(((EarningTO) list.get(j)).getEarningID(), list.get(j));
                }
                if (data == null) {
                    data = new HashMap();
                }
                data.put("EARNING", earningMap);
                earningMap = null;
            }
        } else if (map.containsKey("DEDUCTION")) {
            System.out.println("!@#!@$@!$@#$#mapStr" + mapStr + "A@#$@#$map" + map);
            List list = (List) sqlMap.executeQueryForList(mapStr, map);
            if (list.size() > 0) {
                deductionMap = new LinkedHashMap();
                for (int i = list.size(), j = 0; i > 0; i--, j++) {
                    deductionMap.put(((DeductionTO) list.get(j)).getDtSlNo(), list.get(j));
                }
                if (data == null) {
                    data = new HashMap();
                }
                data.put("DEDUCTION", deductionMap);
                deductionMap = null;
            }
        }

        //        returnMap.put(CommonConstants.MAP_NAME,list);
        return data;
    }

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            double tempMaxSlNo = 0.0;
            System.out.println("@!@#!@#!@map" + map);
            if (map.containsKey("earningMap")) {
                earningMap = new LinkedHashMap();
                earningMap = (LinkedHashMap) map.get("earningMap");
                System.out.println("@#$@#$@#$#earningMap" + earningMap);
            }
            if (map.containsKey("deletedEarningMap")) {
                deletedEarningMap = new HashMap();
                deletedEarningMap = (HashMap) map.get("deletedEarningMap");
                System.out.println("@#$@#$@#$#deletedEarningMap" + deletedEarningMap);
            }
            if (map.containsKey("deductionMap")) {
                deductionMap = new LinkedHashMap();
                deductionMap = (LinkedHashMap) map.get("deductionMap");
                System.out.println("@#$@#$@#$#deductionMap" + deductionMap);
            }
            if (map.containsKey("deletedDeductionMap")) {
                deletedDeductionMap = new HashMap();
                deletedDeductionMap = (HashMap) map.get("deletedDeductionMap");
                System.out.println("@#$@#$@#$#deletedDeductionMap" + deletedDeductionMap);
            }
            final String command = (String) map.get("COMMAND");
            System.out.println("@#$@#$@#command :" + command);
            if (earningMap != null || deletedEarningMap != null) {
                processEarningData(command);
            } else if (deductionMap != null || deletedDeductionMap != null) {
                processDeductionData(command);
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void processEarningData(String command) throws Exception {
        if (deletedEarningMap != null) {
            System.out.println("@!@#$@#$inside deletedEarningMap:");
            addressIterator = deletedEarningMap.keySet().iterator();
            for (int i = deletedEarningMap.size(); i > 0; i--) {
                System.out.println("entering  deleted Techncal map!!!!");
                key = (String) addressIterator.next();
                objEDTO = (EarningTO) deletedEarningMap.get(key);
                //                logTO.setData(objEDTO.toString());
                //                logTO.setStatus(objEDTO.getCommand());
                objEDTO.setStatusBy(_userId);
                objEDTO.setStatusDt(currDt);
                sqlMap.executeUpdate("deleteEarningTypeTO", objEDTO);
                //                logDAO.addToLog(logTO);
            }
            deletedEarningMap = null;
        }
        if (earningMap != null) {
            addressIterator = earningMap.keySet().iterator();
            for (int i = earningMap.size(); i > 0; i--) {
                System.out.println("!@#!@#!@#inside for:" + earningMap);
                addressKey = (String) addressIterator.next();
                objEDTO = (EarningTO) earningMap.get(addressKey);
                if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    if (objEDTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                        objEDTO.setStatusBy(_userId);
                        objEDTO.setStatusDt(currDt);
                        System.out.println("@#$@#$#@insertEarningTypeTO" + objEDTO);
                        sqlMap.executeUpdate("insertEarningTypeTO", objEDTO);
                    } else {
                        objEDTO.setStatusBy(_userId);
                        objEDTO.setStatusDt(currDt);
                        System.out.println("@#$@#$#@updateEarningTypeTO" + objEDTO);
                        sqlMap.executeUpdate("updateEarningTypeTO", objEDTO);
                    }

                } else if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    objEDTO.setStatusBy(_userId);
                    objEDTO.setStatusDt(currDt);
                    System.out.println("inside insert!@#!@objEDTO" + objEDTO);
                    sqlMap.executeUpdate("insertEarningTypeTO", objEDTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    objEDTO.setStatus(CommonConstants.STATUS_DELETED);
                    objEDTO.setStatusBy(_userId);
                    objEDTO.setStatusDt(currDt);
                    sqlMap.executeUpdate("deleteEarningTypeTO", objEDTO);
                }
            }
            earningMap = null;
        }
    }

    private void processDeductionData(String command) throws Exception {
        if (deletedDeductionMap != null) {
            System.out.println("@!@#$@#$inside deletedDeductionMap:" + deletedDeductionMap);
            addressIterator = deletedDeductionMap.keySet().iterator();
            for (int i = deletedDeductionMap.size(); i > 0; i--) {
                System.out.println("entering  deleted Techncal map!!!!");
                key = (String) addressIterator.next();
                objDeductionTO = (DeductionTO) deletedDeductionMap.get(key);
                sqlMap.executeUpdate("deleteDeductionTypeTO", objDeductionTO);
            }
            deletedEarningMap = null;
        }
        if (deductionMap != null) {
            addressIterator = deductionMap.keySet().iterator();
            for (int i = deductionMap.size(); i > 0; i--) {
                System.out.println("!@#!@#!@#inside for:" + deductionMap);
                addressKey = (String) addressIterator.next();
                objDeductionTO = (DeductionTO) deductionMap.get(addressKey);
                if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    if (objDeductionTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                        System.out.println("@#$@#$#@insertDeductionTypeTO" + objDeductionTO);
                        sqlMap.executeUpdate("insertDeductionTypeTO", objDeductionTO);
                    } else {
                        System.out.println("@#$@#$#@updateDeductionTypeTO" + objDeductionTO);
                        sqlMap.executeUpdate("updateDeductionTypeTO", objDeductionTO);
                    }

                } else if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    System.out.println("inside insert!@#!@objDeductionTO" + objDeductionTO);
                    sqlMap.executeUpdate("insertDeductionTypeTO", objDeductionTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    objDeductionTO.setStatus(CommonConstants.STATUS_DELETED);
                    sqlMap.executeUpdate("deleteDeductionTypeTO", objDeductionTO);
                }
            }
            deductionMap = null;
        }
    }

    private void insertSalaryListData() throws Exception {
        try {
            if (SalaryListTOs != null && SalaryListTOs.size() > 0) {
                sqlMap.startTransaction();
                System.out.println("########SalaryListTOs :" + SalaryListTOs);
                int size = SalaryListTOs.size();
                HashMap eachMap = new HashMap();
                for (int i = 0; i < size; i++) {
                    objSLTO = new SalaryDeductionTO();
                    eachMap = (HashMap) SalaryListTOs.get(i);
                    System.out.println("########eachMap :" + eachMap);
                    //                        HashMap existingDateMap = new HashMap();
                    //                        Date fromDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objHATO.getHalting_from_date()));
                    //                        Date currDate = currDt;
                    //                        if(fromDate!=null && fromDate.getDate()>0){
                    //                            currDate.setDate(fromDate.getDate());
                    //                            currDate.setMonth(fromDate.getMonth());
                    //                            currDate.setYear(fromDate.getYear());
                    //                        }
                    //                        existingDateMap.put("GRADE",CommonUtil.convertObjToStr(objHATO.getHalting_grade()).toUpperCase());
                    //                        existingDateMap.put("BRANCH_CODE",objHATO.getBranchCode());
                    //                        existingDateMap.put("FROM_DATE", currDate);
                    //                        List existingLst = sqlMap.executeQueryForList("getSelectExistingRecords", existingDateMap);
                    //                        if(existingLst!=null && existingLst.size()>0){
                    //                            existingDateMap = (HashMap)existingLst.get(0);
                    //                            System.out.println("########insert existingLst :"+existingLst);
                    //                            fromDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(existingDateMap.get("FROM_DATE")));
                    //                            Date newRecDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objHATO.getHalting_from_date()));
                    //                            System.out.println("########insert newRecDate :"+newRecDate +"fromDate :"+fromDate);
                    //                            if(DateUtil.dateDiff(fromDate,newRecDate) == 0){
                    //                                existingDateMap = new HashMap();
                    //                                existingDateMap.put("FROM_DATE",currDate);
                    //                                existingDateMap.put("GRADE",CommonUtil.convertObjToStr(objHATO.getHalting_grade()).toUpperCase());
                    //                                existingDateMap.put("BRANCH_CODE",objHATO.getBranchCode());
                    //                                existingDateMap.put("STATUS","INACTIVE");
                    //                                existingDateMap.put("AUTHORIZE_STATUS","INACTIVE");
                    //                                sqlMap.executeUpdate("updateStatusDeletedasExistingRecSS", existingDateMap);
                    //                            }
                    //                        }
                    //                    }
                    boolean existingFlag = false;
                    long month = CommonUtil.convertObjToLong(eachMap.get("SAL_MONTH"));
                    long year = CommonUtil.convertObjToLong(eachMap.get("SAL_YEAR"));
                    HashMap existingMap = new HashMap();
                    existingMap.put("EMP_ID", eachMap.get("EMPLOYEE_CODE"));
                    List slList = sqlMap.executeQueryForList("getSelectAlreadyExistingRecord", existingMap);
                    if (slList != null && slList.size() > 0) {
                        existingMap = (HashMap) slList.get(0);
                        long existedRecMonth = CommonUtil.convertObjToLong(existingMap.get("SALARY_MONTH"));
                        long existedRecYear = CommonUtil.convertObjToLong(existingMap.get("SALARY_YEAR"));
                        System.out.println("month :" + month + "existedRecMonth :" + existedRecMonth + "year :" + year + "existedRecYear :" + existedRecYear);
                        if (existedRecMonth == month && existedRecYear == year) {
                            System.out.println("Already Existed Records");
                        } else {
                            existingFlag = true;
                        }
                    } else {
                        existingFlag = true;
                    }
                    if (existingFlag == true) {
                        objSLTO.setSdSlNo(new Double(1));
                        objSLTO.setTempSlNo(new Double(1));
                        objSLTO.setEmpId(CommonUtil.convertObjToStr(eachMap.get("EMPLOYEE_CODE")));
                        objSLTO.setEmpCustId(CommonUtil.convertObjToStr(eachMap.get("CUST_ID")));
                        objSLTO.setDesignation(CommonUtil.convertObjToStr(eachMap.get("DESIG_ID")));
                        objSLTO.setEmpName(CommonUtil.convertObjToStr(eachMap.get("FNAME")));
                        objSLTO.setBasic(CommonUtil.convertObjToDouble(eachMap.get("BASIC")));
                        objSLTO.setEmpBranch(CommonUtil.convertObjToStr(eachMap.get("EMP_BRANCH")));
                        objSLTO.setStatus(CommonConstants.STATUS_CREATED);
                        objSLTO.setStatusBy(_userId);
                        objSLTO.setStatusDate(currDt);
                        objSLTO.setBranchCode(_branchCode);
                        objSLTO.setSalaryMonth(CommonUtil.convertObjToDouble(eachMap.get("SAL_MONTH")));
                        objSLTO.setSalaryYear(CommonUtil.convertObjToDouble(eachMap.get("SAL_YEAR")));
                        if (eachMap.containsKey("DA") && CommonUtil.convertObjToDouble(eachMap.get("DA")).doubleValue() > 0) {
                            objSLTO.setTypeOfDeduction("DA");
                            objSLTO.setAmount(CommonUtil.convertObjToDouble(eachMap.get("DA")));
                            System.out.println("########insert DA :" + objSLTO);
                            sqlMap.executeUpdate("insertSalaryListTO", objSLTO);
                        }
                        if (eachMap.containsKey("HRA") && CommonUtil.convertObjToDouble(eachMap.get("HRA")).doubleValue() > 0) {
                            objSLTO.setTypeOfDeduction("HRA");
                            objSLTO.setAmount(CommonUtil.convertObjToDouble(eachMap.get("HRA")));
                            System.out.println("########insert HRA :" + objSLTO);
                            sqlMap.executeUpdate("insertSalaryListTO", objSLTO);
                        }
                        if (eachMap.containsKey("CCA") && CommonUtil.convertObjToDouble(eachMap.get("CCA")).doubleValue() > 0) {
                            objSLTO.setTypeOfDeduction("CCA");
                            objSLTO.setAmount(CommonUtil.convertObjToDouble(eachMap.get("CCA")));
                            System.out.println("########insert CCA :" + objSLTO);
                            sqlMap.executeUpdate("insertSalaryListTO", objSLTO);
                        }
                        if (eachMap.containsKey("SPLALLOWANCE") && CommonUtil.convertObjToDouble(eachMap.get("SPLALLOWANCE")).doubleValue() > 0) {
                            objSLTO.setTypeOfDeduction("SPLALLOWANCE");
                            objSLTO.setAmount(CommonUtil.convertObjToDouble(eachMap.get("SPLALLOWANCE")));
                            System.out.println("########insert SPLALLOWANCE :" + objSLTO);
                            sqlMap.executeUpdate("insertSalaryListTO", objSLTO);
                        }
                        if (eachMap.containsKey("DEDUCT") && CommonUtil.convertObjToDouble(eachMap.get("DEDUCT")).doubleValue() > 0) {
                            objSLTO.setTypeOfDeduction("DEDUCT");
                            objSLTO.setAmount(CommonUtil.convertObjToDouble(eachMap.get("DEDUCT")));
                            System.out.println("########insert DEDUCT :" + objSLTO);
                            sqlMap.executeUpdate("insertSalaryListTO", objSLTO);
                        }
                        if (eachMap.containsKey("LOSSOFPAY") && CommonUtil.convertObjToDouble(eachMap.get("LOSSOFPAY")).doubleValue() > 0) {
                            objSLTO.setTypeOfDeduction("LOSSOFPAY");
                            objSLTO.setAmount(CommonUtil.convertObjToDouble(eachMap.get("LOSSOFPAY")));
                            System.out.println("########insert LOSSOFPAY :" + objSLTO);
                           sqlMap.executeUpdate("insertSalaryListTO", objSLTO);
                        }
                    }
                    existingFlag = false;
                    //                    if(i == 0){
                    //                    if(CommonUtil.convertObjToStr(objEDTO.getStatus()).equals(CommonConstants.STATUS_CREATED)){
                    //                        System.out.println("else Part insert objEDTO :"+objEDTO+"Size :"+i);
                    //                        objEDTO.setTempSlNo(new Double((long)tempMaxSlNo));
                    //                        System.out.println("########insert objEDTO :"+objEDTO);
                    //                        sqlMap.executeUpdate("insertEarningTypeTO", objEDTO);
                    //                    }else if(CommonUtil.convertObjToStr(objEDTO.getStatus()).equals(CommonConstants.STATUS_DELETED)){
                    //                        objEDTO.setStatus(CommonConstants.STATUS_DELETED);
                    //                        System.out.println("########delete Records objEDTO :"+objEDTO);
                    //                        sqlMap.executeUpdate("deleteEarningTypeTO", objEDTO);
                    //                    }
                    //                }
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            if (map.containsKey("earningMap")) {
                earningMap = new LinkedHashMap();
                earningMap = (LinkedHashMap) map.get("earningMap");
                System.out.println("@#$@#$@#$#earningMap" + earningMap);
            }
            if (map.containsKey("deletedEarningMap")) {
                deletedEarningMap = new HashMap();
                deletedEarningMap = (HashMap) map.get("deletedEarningMap");
                System.out.println("@#$@#$@#$#deletedEarningMap" + deletedEarningMap);
            }
            if (map.containsKey("deductionMap")) {
                deductionMap = new LinkedHashMap();
                deductionMap = (LinkedHashMap) map.get("deductionMap");
                System.out.println("@#$@#$@#$#deductionMap" + deductionMap);
            }
            if (map.containsKey("deletedDeductionMap")) {
                deletedDeductionMap = new HashMap();
                deletedDeductionMap = (HashMap) map.get("deletedDeductionMap");
                System.out.println("@#$@#$@#$#deletedDeductionMap" + deletedDeductionMap);
            }
            final String command = (String) map.get("COMMAND");
            System.out.println("@#$@#$@#command :" + command);
            if (earningMap != null || deletedEarningMap != null) {
                processEarningData(command);
            } else if (deductionMap != null || deletedDeductionMap != null) {
                processDeductionData(command);
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    private void updateDeleteStatusData(HashMap map) throws Exception {
        try {
            //            int size = SalaryStructureTOs.size();
            //            System.out.println("########updatesize :"+size);
            //            sqlMap.startTransaction();
            ////            ArrayList lst = new ArrayList();
            //            for(int i=0;i<size;i++){
            //                objTO =(SalaryStructureTO)SalaryStructureTOs.get(0);
            //                objTO.setSlNo(String.valueOf(i+1));
            //                objTO.setToDate(null);
            //                sqlMap.executeUpdate("updateDeleteLienTO",objTO);
            //            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            SalaryStructureDAO dao = new SalaryStructureDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("SalaryStructure execute###" + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        _userId = (String) map.get(CommonConstants.USER_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        final String command = (String) map.get("COMMAND");
        System.out.println("@#$@#$@#$#command" + command + " : " + _userId + " : " + currDt);

        if (map != null && map.size() > 0) {
            if (!map.containsKey(CommonConstants.AUTHORIZEMAP)) {
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertData(map);
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData(map);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData(map);
                } else {
                    throw new NoCommandException();
                }
            } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
                //        else if (map.containsKey("INCREMENT_SCREEN")) {
                System.out.println("Inside authorize!!!");
                HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
                if (authMap != null) {
                    System.out.println("authMap#@$@#$!!!" + authMap);
                    authorize(authMap);
                }
            }
        }

        map = null;
        destroyObjects();
        _userId = "";
        return resultMap;
    }

    private void authorize(HashMap map) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        System.out.println("@@@@@@@AuthMap" + AuthMap);
        try {
            if (AuthMap.containsKey("EARNING_REASON")) {
                System.out.println("insert#$#@$#@" + AuthMap);
                sqlMap.executeUpdate("updateAuthorizeStatusEarning", AuthMap);
            } else if (AuthMap.containsKey("DEDUCTION_REASON")) {
                System.out.println("insert DEDUCTION_DETAILS#$#@$#@" + AuthMap);
                sqlMap.executeUpdate("updateAuthorizeStatusDeduction", AuthMap);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            if (map.containsKey("earningMap")) {
                earningMap = new LinkedHashMap();
                earningMap = (LinkedHashMap) map.get("earningMap");
                System.out.println("@#$@#$@#$#earningMap" + earningMap);
            }
            if (map.containsKey("deletedEarningMap")) {
                deletedEarningMap = new HashMap();
                deletedEarningMap = (HashMap) map.get("deletedEarningMap");
                System.out.println("@#$@#$@#$#deletedEarningMap" + deletedEarningMap);
            }
            if (map.containsKey("deductionMap")) {
                deductionMap = new LinkedHashMap();
                deductionMap = (LinkedHashMap) map.get("deductionMap");
                System.out.println("@#$@#$@#$#deductionMap" + deductionMap);
            }
            if (map.containsKey("deletedDeductionMap")) {
                deletedDeductionMap = new HashMap();
                deletedDeductionMap = (HashMap) map.get("deletedDeductionMap");
                System.out.println("@#$@#$@#$#deletedDeductionMap" + deletedDeductionMap);
            }
            final String command = (String) map.get("COMMAND");
            System.out.println("@#$@#$@#command :" + command);
            if (earningMap != null || deletedEarningMap != null) {
                processEarningData(command);
            } else if (deductionMap != null || deletedDeductionMap != null) {
                processDeductionData(command);
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        System.out.println("@!#!@#!@#obj:->" + obj.get(CommonConstants.MAP_NAME));
        System.out.println("@!#!@#!@#obj:->" + obj);
        return getData(obj);
    }

    private void destroyObjects() {
        objEDTO = null;
        objDeductionTO = null;
        earningMap = null;
        deductionMap = null;
        deletedDeductionMap = null;
        deletedEarningMap = null;
    }
}
