/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * IncrementsPromotionsDAO.java
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
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.common.IncrementTO;
import com.see.truetransact.transferobject.common.PromotionTO;
import com.see.truetransact.transferobject.common.SalaryMasterTO;
import com.see.truetransact.transferobject.common.SalaryMasterDetailsTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Date;
import java.util.LinkedHashMap;
import java.lang.Math;
import java.util.GregorianCalendar;

/**
 * IncrementsPromotionsDAO.
 *
 * @author Sathiya
 *
 */
public class IncrementsPromotionsDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private IncrementTO objDTTO;
    private PromotionTO objEDTO;
    private SalaryMasterTO objSalaryMasterTO;
    private SalaryMasterDetailsTO objSalaryMasterDetailsTO;
    HashMap salaryReportMap = new HashMap();
    private String salaryID = "";
    private String _userId = "";
    private ArrayList IncrementTOs, PromotionTOs, SalaryCalcList;
    private ArrayList deleteIncrementList, deletePromotionList;
    HashMap resultMap = new HashMap();
    double grossAmt = 0.0;
    Date currDt = null;
    //List which holds the details of a single or a group of employees
    List EmployeeLst = null;
    //List which holds all the salary details of a employee or a list of employees
    List salaryDetailsLst = null;
    //    Hashmap which stores all the details of all the employees
    HashMap EmployeeMap = new HashMap();

    /**
     * Creates a new instance of DeductionDAO
     */
    public IncrementsPromotionsDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String mapStr = (String) map.get(CommonConstants.MAP_NAME);
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        map = null;
        List list = (List) sqlMap.executeQueryForList(mapStr, where);
        returnMap.put(CommonConstants.MAP_NAME, list);
        return returnMap;
    }

    private HashMap insertData(HashMap map) throws Exception {
        System.out.println("@!#!@#!@#map" + map);
        HashMap resultMap = new HashMap();
        if (IncrementTOs != null && IncrementTOs.size() > 0) {
            int size = IncrementTOs.size();
            for (int i = 0; i < size; i++) {
                objDTTO = (IncrementTO) IncrementTOs.get(i);
                Date fromDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objDTTO.getCreatedDate()));
                Date toDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objDTTO.getEffectiveDate()));
                if (fromDt != null) {
                    Date insDate = (Date) currDt.clone();
                    insDate.setDate(fromDt.getDate());
                    insDate.setMonth(fromDt.getMonth());
                    insDate.setYear(fromDt.getYear());
                    objDTTO.setCreatedDate(insDate);
                    objDTTO.setStatusDt(insDate);
                }
                if (toDt != null) {
                    Date insDate = (Date) currDt.clone();
                    insDate.setDate(toDt.getDate());
                    insDate.setMonth(toDt.getMonth());
                    insDate.setYear(toDt.getYear());
                    objDTTO.setEffectiveDate(insDate);
                }

                processIncrementDetails(map, objDTTO);
                resultMap = null;
            }
        } else if (PromotionTOs != null && PromotionTOs.size() > 0) {
            int size = PromotionTOs.size();
            for (int i = 0; i < size; i++) {
                objEDTO = (PromotionTO) PromotionTOs.get(i);
                Date fromDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objEDTO.getCreatedDate()));
                Date toDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objEDTO.getEffectiveDate()));
                if (fromDt != null) {
                    Date insDate = (Date) currDt.clone();
                    insDate.setDate(fromDt.getDate());
                    insDate.setMonth(fromDt.getMonth());
                    insDate.setYear(fromDt.getYear());
                    objEDTO.setCreatedDate(insDate);
                    objEDTO.setStatusDt(insDate);
                }
                if (toDt != null) {
                    Date insDate = (Date) currDt.clone();
                    insDate.setDate(toDt.getDate());
                    insDate.setMonth(toDt.getMonth());
                    insDate.setYear(toDt.getYear());
                    objEDTO.setEffectiveDate(insDate);
                }
                processPromotionDetails(map, objEDTO);
                resultMap = null;
            }
        } else if (SalaryCalcList != null && SalaryCalcList.size() > 0) {

            resultMap = processSalaryDetails(map);
            System.out.println("#@$% #$resultMap" + resultMap);
            System.out.println("@#$@#$@#$before makin grossAmt null:" + grossAmt);
            //        grossAmt is nullified here
            grossAmt = 0.0;
        }
        return resultMap;
    }
    //to Process the Salary Details of the employee

    private HashMap processSalaryDetails(HashMap map) throws Exception {
        HashMap employeeSalaryMap = null;
        try {
            sqlMap.startTransaction();
            salaryID = null;
            ArrayList EmployeeSalaryDetails = null;
            ArrayList empSalDetails = null;
            SalaryMasterTO objSalaryMasterTO = new SalaryMasterTO();
            currDt = ServerUtil.getCurrentDate(_branchCode);
            System.out.println("########insert salaryDetails :" + SalaryCalcList);
            HashMap ArrearCalcMap = new HashMap();
            ArrearCalcMap = (HashMap) (SalaryCalcList.get(0));
            System.out.println("@#$@$#@ ArrearCalcMap:" + ArrearCalcMap);
            Date salaryForDate = (Date) currDt.clone();

            if (ArrearCalcMap.containsKey("FROM_MONTH") && ArrearCalcMap.containsKey("FROM_YEAR")) {
                int month = CommonUtil.convertObjToInt(ArrearCalcMap.get("FROM_MONTH"));
                int year = CommonUtil.convertObjToInt(ArrearCalcMap.get("FROM_YEAR"));
                salaryForDate.setDate(1);
                salaryForDate.setMonth(month - 1);
                salaryForDate.setYear(year - 1900);
                salaryForDate = setProperDtFormat(salaryForDate);
            }
            System.out.println("@#$@$#@ salaryForDate:" + salaryForDate);
            objSalaryMasterTO.setSalaryDt(salaryForDate);
            objSalaryMasterTO.setCreatedDate(currDt);
            salaryID = CommonUtil.convertObjToStr(ArrearCalcMap.get("SALARY_ID"));
            String status = CommonUtil.convertObjToStr(ArrearCalcMap.get("STATUS"));
            objSalaryMasterTO.setSalaryStatus(CommonUtil.convertObjToStr(ArrearCalcMap.get("STATUS")));
            //            TO INSERT INTO THE SALARY MASTER TABLE
            if (status.equals("ENQUIRY")) {
                if (ArrearCalcMap.containsKey("BASED_ON") && !ArrearCalcMap.get("BASED_ON").equals("")) {
                    String basedOn = CommonUtil.convertObjToStr(ArrearCalcMap.get("BASED_ON"));
                    List employeeFinalLst = null;
                    System.out.println("@#$@#$@#$basedOn" + basedOn);
                    if (basedOn.equals("ZONAL")) {
                        HashMap zonalMap = new HashMap();
                        String Zone = (String) CommonUtil.convertObjToStr(ArrearCalcMap.get("REGIONAL_CODE"));
                        List salIdList = sqlMap.executeQueryForList("getSalIdForEnquiry", salaryForDate);
                        if (salIdList != null && salIdList.size() > 0) {
                            zonalMap = (HashMap) salIdList.get(0);
                            String salary_id = (String) CommonUtil.convertObjToStr(zonalMap.get("SALARY_ID"));
                            zonalMap.put("SALARY_FROM_DT", salaryForDate);
                            zonalMap.put("ZONAL_CODE", Zone);
                            System.out.println("!@#@!#zonalMap:" + zonalMap);
                            List employeeZonalLst = sqlMap.executeQueryForList("getEmployeeForZonal", zonalMap);

                            //                        employeeZonalLst CONTAINS ALL THE EMPLOYEES PERTAINING TO SPECIFIED ZONE
                            System.out.println("@#$@#$employeeZonalLst" + employeeZonalLst);
                            if (employeeZonalLst != null && employeeZonalLst.size() > 0) {
                                for (int i = 0; i < employeeZonalLst.size(); i++) {
                                    HashMap employeeZonalLstMap = new HashMap();
                                    //                                FOR THE FIRST EMPLOYEE
                                    employeeZonalLstMap = (HashMap) employeeZonalLst.get(i);
                                    employeeZonalLstMap.put("SALARY_ID", salary_id);
                                    List employeeindiList = sqlMap.executeQueryForList("getEmpDetailsForEnquiry", employeeZonalLstMap);
                                    HashMap salaryTypeMap = new HashMap();
                                    if (employeeindiList != null && employeeindiList.size() > 0) {
                                        salaryTypeMap = (HashMap) employeeindiList.get(0);
                                        ArrayList empSalaryTypeList = (ArrayList) sqlMap.executeQueryForList("getsalaryTypeforEnquiry", employeeZonalLstMap);
                                        salaryTypeMap.put("SALARY_TYPE", empSalaryTypeList);
                                        System.out.println("@!#!@#employeeindiList:" + salaryTypeMap);
                                        if (empSalDetails == null) {
                                            empSalDetails = new ArrayList();
                                        }
                                        empSalDetails.add(salaryTypeMap);
                                    }
                                }
                            }
                            if (employeeSalaryMap == null) {
                                employeeSalaryMap = new HashMap();
                            }

                            employeeSalaryMap.put("SALARY_DETAILS", empSalDetails);
                            System.out.println("@!#!@#!@#employeeSalaryMap:0" + employeeSalaryMap);
                        }
                    } else if (basedOn.equals("BRANCH")) {
                        HashMap branchMap = new HashMap();
                        String branch = (String) CommonUtil.convertObjToStr(ArrearCalcMap.get("BRANCH_CODE"));
                        List salIdList = sqlMap.executeQueryForList("getSalIdForEnquiry", salaryForDate);
                        if (salIdList != null && salIdList.size() > 0) {
                            branchMap = (HashMap) salIdList.get(0);
                            String salary_id = (String) CommonUtil.convertObjToStr(branchMap.get("SALARY_ID"));
                            branchMap.put("SALARY_FROM_DT", salaryForDate);
                            branchMap.put("BRANCH_CODE", branch);
                            System.out.println("!@#@!#branchMap:" + branchMap);
                            List employeeBranchLst = sqlMap.executeQueryForList("getEmployeeForBranch", branchMap);
                            //                        employeeBranchLst CONTAINS ALL THE EMPLOYEES PERTAINING TO SPECIFIED Branch
                            System.out.println("@#$@#$employeeBranchLst" + employeeBranchLst);
                            if (employeeBranchLst != null && employeeBranchLst.size() > 0) {
                                for (int i = 0; i < employeeBranchLst.size(); i++) {
                                    HashMap employeeBranchLstMap = new HashMap();
                                    //                                FOR THE FIRST EMPLOYEE
                                    employeeBranchLstMap = (HashMap) employeeBranchLst.get(i);
                                    employeeBranchLstMap.put("SALARY_ID", salary_id);
                                    List employeeindiList = sqlMap.executeQueryForList("getEmpDetailsForEnquiry", employeeBranchLstMap);
                                    HashMap salaryTypeMap = new HashMap();
                                    if (employeeindiList != null && employeeindiList.size() > 0) {
                                        salaryTypeMap = (HashMap) employeeindiList.get(0);
                                        ArrayList empSalaryTypeList = (ArrayList) sqlMap.executeQueryForList("getsalaryTypeforEnquiry", employeeBranchLstMap);
                                        salaryTypeMap.put("SALARY_TYPE", empSalaryTypeList);
                                        System.out.println("@!#!@#employeeindiList:" + salaryTypeMap);
                                        if (empSalDetails == null) {
                                            empSalDetails = new ArrayList();
                                        }
                                        empSalDetails.add(salaryTypeMap);
                                    }
                                }
                            }
                            if (employeeSalaryMap == null) {
                                employeeSalaryMap = new HashMap();
                            }
                            employeeSalaryMap.put("SALARY_DETAILS", empSalDetails);
                            System.out.println("@!#!@#!@#employeeSalaryMap:0" + employeeSalaryMap);
                        }
                    } else if (basedOn.equals("EMPLOYEE")) {
                        if (ArrearCalcMap.containsKey("TO_EMPLOYEE_CODE") && !ArrearCalcMap.get("TO_EMPLOYEE_CODE").equals("")) {
                            HashMap employeeLstMap = new HashMap();
                            String toEmp = (String) CommonUtil.convertObjToStr(ArrearCalcMap.get("TO_EMPLOYEE_CODE"));
                            String fromEmp = (String) CommonUtil.convertObjToStr(ArrearCalcMap.get("FROM_EMPLOYEE_CODE"));
                            List salIdList = sqlMap.executeQueryForList("getSalIdForEnquiry", salaryForDate);
                            if (salIdList != null && salIdList.size() > 0) {
                                employeeLstMap = (HashMap) salIdList.get(0);
                                String salary_id = (String) CommonUtil.convertObjToStr(employeeLstMap.get("SALARY_ID"));
                                employeeLstMap.put("SALARY_FROM_DT", salaryForDate);
                                employeeLstMap.put("TO_EMPLOYEE_CODE", toEmp);
                                employeeLstMap.put("FROM_EMPLOYEE_CODE", fromEmp);
                                System.out.println("!@#@!#employeeLstMap:" + employeeLstMap);
                                List employeebetweenLst = sqlMap.executeQueryForList("getEmployeeListBtw", employeeLstMap);
                                //                        employeebetweenLst CONTAINS ALL THE EMPLOYEES PERTAINING TO SPECIFIED Branch
                                System.out.println("@#$@#$employeebetweenLst" + employeebetweenLst);
                                if (employeebetweenLst != null && employeebetweenLst.size() > 0) {
                                    for (int i = 0; i < employeebetweenLst.size(); i++) {
                                        HashMap employeebetweenLstMap = new HashMap();
                                        //                                FOR THE FIRST EMPLOYEE
                                        employeebetweenLstMap = (HashMap) employeebetweenLst.get(i);
                                        employeebetweenLstMap.put("SALARY_ID", salary_id);
                                        List employeeindiList = sqlMap.executeQueryForList("getEmpDetailsForEnquiry", employeebetweenLstMap);
                                        HashMap salaryTypeMap = new HashMap();
                                        if (employeeindiList != null && employeeindiList.size() > 0) {
                                            salaryTypeMap = (HashMap) employeeindiList.get(0);
                                            ArrayList empSalaryTypeList = (ArrayList) sqlMap.executeQueryForList("getsalaryTypeforEnquiry", employeebetweenLstMap);
                                            salaryTypeMap.put("SALARY_TYPE", empSalaryTypeList);
                                            System.out.println("@!#!@#employeeindiList:" + salaryTypeMap);
                                            if (empSalDetails == null) {
                                                empSalDetails = new ArrayList();
                                            }
                                            empSalDetails.add(salaryTypeMap);
                                        }
                                    }
                                }
                                if (employeeSalaryMap == null) {
                                    employeeSalaryMap = new HashMap();
                                }
                                employeeSalaryMap.put("SALARY_DETAILS", empSalDetails);
                                System.out.println("@!#!@#!@#employeeSalaryMap:0" + employeeSalaryMap);
                            }
                        } else {
                            HashMap employeeLstMap = new HashMap();
                            List salIdList = sqlMap.executeQueryForList("getSalIdForEnquiry", salaryForDate);
                            if (salIdList != null && salIdList.size() > 0) {
                                employeeLstMap = (HashMap) salIdList.get(0);
                                String salary_id = (String) CommonUtil.convertObjToStr(employeeLstMap.get("SALARY_ID"));
                                employeeLstMap.put("EMPLOYEE_ID", (String) CommonUtil.convertObjToStr(ArrearCalcMap.get("FROM_EMPLOYEE_CODE")));
                                List employeeindiList = sqlMap.executeQueryForList("getEmpDetailsForEnquiry", employeeLstMap);
                                HashMap salaryTypeMap = new HashMap();
                                if (employeeindiList != null && employeeindiList.size() > 0) {
                                    salaryTypeMap = (HashMap) employeeindiList.get(0);
                                    ArrayList empSalaryTypeList = (ArrayList) sqlMap.executeQueryForList("getsalaryTypeforEnquiry", employeeLstMap);
                                    salaryTypeMap.put("SALARY_TYPE", empSalaryTypeList);
                                    System.out.println("@!#!@#employeeindiList:" + salaryTypeMap);
                                    if (empSalDetails == null) {
                                        empSalDetails = new ArrayList();
                                    }
                                    empSalDetails.add(salaryTypeMap);
                                }
                            }
                            if (employeeSalaryMap == null) {
                                employeeSalaryMap = new HashMap();
                            }
                            employeeSalaryMap.put("SALARY_DETAILS", empSalDetails);
                            System.out.println("@!#!@#!@#employeeSalaryMap:0" + employeeSalaryMap);
                        }
                    } else if (basedOn.equals("ALL")) {

                        HashMap allMap = new HashMap();
                        //                        String branch = (String) CommonUtil.convertObjToStr(ArrearCalcMap.get("BRANCH_CODE"));
                        List salIdList = sqlMap.executeQueryForList("getSalIdForEnquiry", salaryForDate);
                        if (salIdList != null && salIdList.size() > 0) {
                            allMap = (HashMap) salIdList.get(0);
                            String salary_id = (String) CommonUtil.convertObjToStr(allMap.get("SALARY_ID"));
                            allMap.put("SALARY_FROM_DT", salaryForDate);
                            System.out.println("!@#@!#allMap:" + allMap);
                            List allemployeeLst = sqlMap.executeQueryForList("getAllEmployeeForEnquiry", allMap);
                            //                        allemployeeLst CONTAINS ALL THE EMPLOYEES specific of the date
                            System.out.println("@#$@#$allemployeeLst" + allemployeeLst);
                            if (allemployeeLst != null && allemployeeLst.size() > 0) {
                                for (int i = 0; i < allemployeeLst.size(); i++) {
                                    HashMap allemployeeLstMap = new HashMap();
                                    //                                FOR THE FIRST EMPLOYEE
                                    allemployeeLstMap = (HashMap) allemployeeLst.get(i);
                                    allemployeeLstMap.put("SALARY_ID", salary_id);
                                    List employeeindiList = sqlMap.executeQueryForList("getEmpDetailsForEnquiry", allemployeeLstMap);
                                    HashMap salaryTypeMap = new HashMap();
                                    if (employeeindiList != null && employeeindiList.size() > 0) {
                                        salaryTypeMap = (HashMap) employeeindiList.get(0);
                                        ArrayList empSalaryTypeList = (ArrayList) sqlMap.executeQueryForList("getsalaryTypeforEnquiry", allemployeeLstMap);
                                        salaryTypeMap.put("SALARY_TYPE", empSalaryTypeList);
                                        System.out.println("@!#!@#employeeindiList:" + salaryTypeMap);
                                        if (empSalDetails == null) {
                                            empSalDetails = new ArrayList();
                                        }
                                        empSalDetails.add(salaryTypeMap);
                                    }
                                }
                            }
                            if (employeeSalaryMap == null) {
                                employeeSalaryMap = new HashMap();
                            }
                            employeeSalaryMap.put("SALARY_DETAILS", empSalDetails);
                            System.out.println("@!#!@#!@#employeeSalaryMap:0" + employeeSalaryMap);
                        }
                    }
                }
            } else {

                //                the actual calculation part
                if (status.equals("TRIAL_PROCESS") || status.equals("FINAL")) {
                    //                TAKE THE SALARY FOR THE MONTH AS THE CRITERIA TO CHECK ON THE FINAL PROCESS
                    if (salaryID.equals("")) {
                        salaryID = getSalaryIDGeneration();
                        objSalaryMasterTO.setSalaryId(salaryID);
                        objSalaryMasterTO.setPayMentDt(currDt);
                        HashMap salaryDtMap = new HashMap();
                        salaryDtMap.put("SALARY_FROM_DT", salaryForDate);
                        List getMaxDateList = sqlMap.executeQueryForList("getMaxSalCreatedDate", salaryDtMap);
                        if (getMaxDateList != null && getMaxDateList.size() > 0) {
                            HashMap maxSalDateMap = (HashMap) getMaxDateList.get(0);
                            if (maxSalDateMap.containsKey("CREATED_DATE")) {
                                Date maxPaymentDt = (Date) currDt.clone();
                                maxPaymentDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(maxSalDateMap.get("CREATED_DATE")));
                                maxPaymentDt.setDate(maxPaymentDt.getDate());
                                maxPaymentDt.setMonth(maxPaymentDt.getMonth());
                                maxPaymentDt.setYear(maxPaymentDt.getYear());
                                maxPaymentDt = setProperDtFormat(maxPaymentDt);
                                objSalaryMasterTO.setPayMentDt(maxPaymentDt);
                                System.out.println("$@#$@#$@#$#@$objSalaryMasterTO:" + objSalaryMasterTO);
                            }
                        }
                        if (status.equals("FINAL")) {
                            //                        perform delete to all the other trial process for dat particular month

                            List allSalIdList = sqlMap.executeQueryForList("getSalaryIdForDate", salaryDtMap);
                            sqlMap.executeUpdate("deleteSalaryMaster", salaryDtMap);
                            if (allSalIdList != null && allSalIdList.size() > 0) {
                                for (int i = 0; i < allSalIdList.size(); i++) {
                                    HashMap allSalIdListMap = new HashMap();
                                    allSalIdListMap = (HashMap) allSalIdList.get(i);
                                    sqlMap.executeUpdate("deleteSalaryMasterDetails", allSalIdListMap);
                                }
                            }
                        }
                        //                 TO Insert a new salary record into the SALARY_MASTER table
                        sqlMap.executeUpdate("insertSalaryMasterTO", objSalaryMasterTO);
                    } else {
                        objSalaryMasterTO.setSalaryId(CommonUtil.convertObjToStr(ArrearCalcMap.get("SALARY_ID")));
                        sqlMap.executeUpdate("UpdateSalaryMaster", objSalaryMasterTO);
                    }
                }
                if (ArrearCalcMap.containsKey("BASED_ON") && !ArrearCalcMap.get("BASED_ON").equals("")) {
                    String basedOn = CommonUtil.convertObjToStr(ArrearCalcMap.get("BASED_ON"));
                    System.out.println("@#$@#$@#$basedOn" + basedOn);
                    if (basedOn.equals("ZONAL")) {
                        //                    USING THE ZONAL CODE
                        String Zone = (String) CommonUtil.convertObjToStr(ArrearCalcMap.get("REGIONAL_CODE"));
                        //                query to get all the branches having the specified regional code
                        List allBranchList = sqlMap.executeQueryForList("getBranchUsingRegionalCode", ArrearCalcMap);
                        if (allBranchList != null && allBranchList.size() > 0) {
                            for (int i = 0; i < allBranchList.size(); i++) {
                                HashMap allBranchListMap = new HashMap();
                                allBranchListMap = (HashMap) allBranchList.get(i);
                                List employeelst = sqlMap.executeQueryForList("getEmployeesForBranch", allBranchListMap);
                                if (employeelst != null && employeelst.size() > 0) {
                                    HashMap employeelstMap = new HashMap();
                                    for (int j = 0; j < employeelst.size(); j++) {
                                        HashMap empMap = new HashMap();
                                        empMap = (HashMap) employeelst.get(j);
                                        employeelstMap.put("FROM_EMPLOYEE_CODE", empMap.get("EMPLOYEE_ID"));
                                        employeelstMap.put("CURRENT_DT", currDt);
                                        System.out.println("@#$@#$employeelstMap" + employeelstMap);
                                        EmployeeLst = sqlMap.executeQueryForList("getSingleEmployeeDetail", employeelstMap);
                                        System.out.println("!!@#!@#!#EmployeeLst:" + EmployeeLst);
                                        EmployeeSalaryDetails = new ArrayList();
                                        EmployeeSalaryDetails = calculateCommonAllowances(allBranchListMap, EmployeeLst, ArrearCalcMap);
                                        if (empSalDetails == null) {
                                            empSalDetails = new ArrayList();
                                        }
                                        empSalDetails.add(EmployeeSalaryDetails);
                                    }
                                }
                            }
                            if (employeeSalaryMap == null) {
                                employeeSalaryMap = new HashMap();
                            }
                            employeeSalaryMap.put("SALARY_DETAILS", empSalDetails);
                        }
                    } else if (basedOn.equals("BRANCH")) {
                        //               Using the Branch Id we can Find out the Employee list
                        EmployeeLst = null;
                        HashMap branchIDMap = new HashMap();
                        //                    branchIDMap.put("REGIONAL_CODE",ArrearCalcMap.get("REGIONAL_CODE"));
                        branchIDMap.put("BRANCH_CODE", ArrearCalcMap.get("BRANCH_CODE"));
                        List employeelst = sqlMap.executeQueryForList("getEmployeesForBranch", branchIDMap);
                        if (employeelst != null && employeelst.size() > 0) {
                            HashMap employeelstMap = new HashMap();
                            for (int j = 0; j < employeelst.size(); j++) {
                                HashMap empMap = new HashMap();
                                empMap = (HashMap) employeelst.get(j);
                                employeelstMap.put("FROM_EMPLOYEE_CODE", empMap.get("EMPLOYEE_ID"));
                                employeelstMap.put("CURRENT_DT", currDt);
                                System.out.println("@#$@#$employeelstMap" + employeelstMap);
                                EmployeeLst = sqlMap.executeQueryForList("getSingleEmployeeDetail", employeelstMap);
                                System.out.println("!!@#!@#!#EmployeeLst:" + EmployeeLst);
                                EmployeeSalaryDetails = new ArrayList();
                                EmployeeSalaryDetails = (ArrayList) calculateCommonAllowances(branchIDMap, EmployeeLst, ArrearCalcMap);
                                System.out.println("!!@#!@#!#EmployeeSalaryDetails :" + EmployeeSalaryDetails);
                                if (empSalDetails == null) {
                                    empSalDetails = new ArrayList();
                                }
                                empSalDetails.add(EmployeeSalaryDetails);
                                System.out.println("@#$@#empSalDetails" + empSalDetails);
                            }
                            if (employeeSalaryMap == null) {
                                employeeSalaryMap = new HashMap();
                            }
                            employeeSalaryMap.put("SALARY_DETAILS", empSalDetails);
                            System.out.println("#$%@#$%#employeeSalaryMap:" + employeeSalaryMap);

                        }
                        //get the common allowances for all the employees in a specific Branch and belonging to Each grade
                        //                EmployeeSalaryDetails = calculateCommonAllowances(branchIDMap,EmployeeLst,ArrearCalcMap);
                        //                EmployeeMap.put("SALARY_DETAILS",EmployeeSalaryDetails);
                    } else if (basedOn.equals("EMPLOYEE")) {
                        //               calculate salary details for Individual employee
                        if (ArrearCalcMap.containsKey("TO_EMPLOYEE_CODE") && !ArrearCalcMap.get("TO_EMPLOYEE_CODE").equals("")) {
                            EmployeeLst = null;
                            HashMap branchIDMap = new HashMap();
                            //                        branchIDMap.put("REGIONAL_CODE",ArrearCalcMap.get("REGIONAL_CODE"));
                            //                        branchIDMap.put("BRANCH_CODE", ArrearCalcMap.get("BRANCH_CODE"));
                            ArrearCalcMap.put("CURRENT_DT", currDt);
                            List employeeList = sqlMap.executeQueryForList("getEmployeesList", ArrearCalcMap);
                            System.out.println("@#@#@#getEmployeeList" + employeeList);
                            if (employeeList != null && employeeList.size() > 0) {
                                HashMap employeeListMap = new HashMap();
                                for (int i = 0; i < employeeList.size(); i++) {
                                    HashMap empMap = new HashMap();
                                    empMap = (HashMap) employeeList.get(i);
                                    employeeListMap.put("FROM_EMPLOYEE_CODE", empMap.get("EMPLOYEE_ID"));
                                    employeeListMap.put("BRANCH_CODE", ArrearCalcMap.get("BRANCH_CODE"));
                                    employeeListMap.put("CURRENT_DT", currDt);
                                    System.out.println("@#$@#$employeeListMap" + employeeListMap);
                                    EmployeeLst = sqlMap.executeQueryForList("getSingleEmployeeDetail", employeeListMap);
                                    System.out.println("!!@#!@#!#EmployeeLst:" + EmployeeLst);
                                    EmployeeSalaryDetails = calculateCommonAllowances(branchIDMap, EmployeeLst, ArrearCalcMap);
                                    if (empSalDetails == null) {
                                        empSalDetails = new ArrayList();
                                    }
                                    empSalDetails.add(EmployeeSalaryDetails);
                                }
                                System.out.println("@$@#$@#$EmployeeSalaryDetailssfdcv:" + EmployeeSalaryDetails);
                                System.out.println("@$@#$@#$empSalDetailsaadasdcv:" + empSalDetails);
                                if (employeeSalaryMap == null) {
                                    employeeSalaryMap = new HashMap();
                                }
                                employeeSalaryMap.put("SALARY_DETAILS", empSalDetails);
                            }
                        } else {
                            EmployeeLst = null;
                            HashMap branchIDMap = new HashMap();
                            //                        branchIDMap.put("REGIONAL_CODE",ArrearCalcMap.get("REGIONAL_CODE"));
                            //                        branchIDMap.put("BRANCH_CODE", ArrearCalcMap.get("BRANCH_CODE"));
                            ArrearCalcMap.put("CURRENT_DT", currDt);
                            System.out.println("#@$@#$@#$ArrearCalcMap:" + ArrearCalcMap);
                            EmployeeLst = sqlMap.executeQueryForList("getSingleEmployeeDetail", ArrearCalcMap);
                            System.out.println("!!@#!@#!#EmployeeLst:" + EmployeeLst);
                            EmployeeSalaryDetails = calculateCommonAllowances(branchIDMap, EmployeeLst, ArrearCalcMap);
                            System.out.println("!!@#!@#!#EmployeeSalaryDetails :" + EmployeeSalaryDetails);
                            if (empSalDetails == null) {
                                empSalDetails = new ArrayList();
                            }
                            empSalDetails.add(EmployeeSalaryDetails);
                            if (employeeSalaryMap == null) {
                                employeeSalaryMap = new HashMap();
                            }
                            employeeSalaryMap.put("SALARY_DETAILS", empSalDetails);
                            System.out.println("@#$@#$#$%^$%$^employeeSalaryMap:" + employeeSalaryMap);
                        }
                    } else if (basedOn.equals("ALL") || status.equals("FINAL")) {
                        System.out.println("$@#$@#$@#based FINAL:" + status);
                        EmployeeSalaryDetails = new ArrayList();
                        EmployeeSalaryDetails = getAllEmployeesSalary(ArrearCalcMap);
                        System.out.println("@#$@$#@$inside regional code and final :" + EmployeeSalaryDetails);
                        if (employeeSalaryMap == null) {
                            employeeSalaryMap = new HashMap();
                        }
                        employeeSalaryMap.put("SALARY_DETAILS", EmployeeSalaryDetails);
                    }
                }
                if (status.equals("TRIAL_PROCESS") || status.equals("FINAL")) {
                    if (salaryReportMap != null && salaryReportMap.size() > 0) {
                        System.out.println("@!#!@#!@#adsadfsfsalaryReportMap:" + salaryReportMap);
                        sqlMap.executeUpdate("deleteSalaryReport", null);
                        Object[] objKeys = salaryReportMap.keySet().toArray();
                        for (int i = 0; i < salaryReportMap.size(); i++) {
                            HashMap insertSalReportMap = new HashMap();
                            insertSalReportMap.put("EARNING_OR_DEDUCTION", CommonUtil.convertObjToStr(objKeys[i]));
                            insertSalReportMap.put("ORDER_NO", CommonUtil.convertObjToStr(salaryReportMap.get(objKeys[i])));
                            sqlMap.executeUpdate("insertSalaryReport", insertSalReportMap);
                        }
                    }
                }
            }
            ArrearCalcMap = null;
            salaryID = "";
            System.out.println("#@$@#$@#$employeeSalaryMap:" + employeeSalaryMap);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
        return employeeSalaryMap;
    }

    private List getEmployeesForBranch(HashMap branchIDMap) throws Exception {
        try {
            EmployeeLst = null;
            System.out.println("@#$@#$inside getEmployeesForBranch" + branchIDMap);
            //        Gives a List of employees based on the Branch and Zone ID
            EmployeeLst = sqlMap.executeQueryForList("getEmployeeLstForBranch", branchIDMap);
            System.out.println("@#$@#$inside EmployeeLst" + EmployeeLst);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EmployeeLst;
    }

    private ArrayList calculateCommonAllowances(HashMap branchIDMap, List EmployeeLst, HashMap ArrearCalcMap) throws Exception {
        /*this Method is used to calculate the common and individual allowances based on different grades in a branch
         *1st Execute a qyery to find the list of grades in a particular branch.
         *then calculate the different allowances based on the grade and branch ID.
         *subsequently call functions to calculate the individual allowances for each Employee.
         */
        //        Query to find out the different grades wrt the BranchID
        HashMap returnMap = new HashMap();
        ArrayList EmployeeSalaryDetails = null;
        try {
            //            if(EmployeeSalaryDetails == null){
            EmployeeSalaryDetails = new ArrayList();
            //            }
            HashMap branchGradeMap = new HashMap();
            LinkedHashMap calculateMap = new LinkedHashMap();
            //            query return null if the specific branch_ID does not hav salary values in salary structure table
            //            List gradeLst= sqlMap.executeQueryForList("getGradesFromBranch",branchIDMap);
            List gradeLst = sqlMap.executeQueryForList("getGradesFromBranch", null);
            System.out.println("different gradeLst" + gradeLst);
            if (gradeLst != null && gradeLst.size() > 0) {
                for (int i = 0; i < gradeLst.size(); i++) {
                    if (calculateMap == null || calculateMap.size() <= 0) {
                        calculateMap = new LinkedHashMap();
                    }
                    branchGradeMap = (HashMap) gradeLst.get(i);
                    System.out.println("!@#!@#!@#ArrearCalcMap:" + ArrearCalcMap);
                    int month = CommonUtil.convertObjToInt(ArrearCalcMap.get("FROM_MONTH"));
                    int year = CommonUtil.convertObjToInt(ArrearCalcMap.get("FROM_YEAR"));
                    System.out.println("@#@$@#$@#month:" + month);
                    System.out.println("@#@$@#$@#year:" + year);
                    Date firstDateOfMonth = (Date) currDt.clone();
                    firstDateOfMonth.setDate(1);
                    firstDateOfMonth.setMonth(month - 1);
                    firstDateOfMonth.setYear(year - 1900);
                    //        first day of the salary calculaton month
                    firstDateOfMonth = setProperDtFormat(firstDateOfMonth);
                    branchGradeMap.put("SALARY_DT", firstDateOfMonth);
                    System.out.println("#$%#$%#%$branchGradeMap" + branchGradeMap);
                    //        FUNCTION TO CALCULATE Allowances from the tables
                    if (!branchGradeMap.get("PRESENT_GRADE").equals("") || !branchGradeMap.get("BRANCH_CODE").equals("")) {
                        salaryReportMap.put("BASIC", "1");
                        calculateMap.put("DA", getAllowanceParameters("getDADetails", branchGradeMap));
                        salaryReportMap.put("DA", "2");
                        calculateMap.put("HRA", getAllowanceParameters("getHraDetails", branchGradeMap));
                        salaryReportMap.put("HRA", "3");
                        calculateMap.put("CCA", getAllowanceParameters("getCCAllowanceDetails", branchGradeMap));
                        salaryReportMap.put("CCA", "4");
                        calculateMap.put("MA", getAllowanceParameters("getMedAllowanceDetails", branchGradeMap));
                        salaryReportMap.put("MA", "5");
                        calculateMap.put("WASHING ALLOWANCE", getAllowanceParameters("getWashingAllowanceDetails", branchGradeMap));
                        salaryReportMap.put("WASHING ALLOWANCE", "6");
                        System.out.println("$#%#$%$#calculateMap:" + calculateMap);
                        if (EmployeeLst.size() > 0 && EmployeeLst != null) {
                            for (int j = 0; j < EmployeeLst.size(); j++) {
                                //calculate the common allowance for employee wrt to
                                HashMap earnDeductMap = new HashMap();
                                HashMap empLstMap = (HashMap) EmployeeLst.get(j);
                                System.out.println("the empoloyee is @#$#@$$%^$:" + empLstMap);
                                if (EmployeeMap == null || EmployeeMap.size() <= 0) {
                                    EmployeeMap = new HashMap();
                                }
                                if (empLstMap.get("PRESENT_GRADE").equals(branchGradeMap.get("PRESENT_GRADE"))) {
                                    //                                EmployeeMap stores the details of all the employees keepin employee_code as key
                                    EmployeeMap = calculateCommonAllowanceForEmp(empLstMap, calculateMap, ArrearCalcMap);
                                    EmployeeMap = calculateIndividualAllowances(EmployeeMap, ArrearCalcMap);
                                    System.out.println("#@$@#after calculateIndividualAllowances:" + EmployeeMap);
                                    if (!ArrearCalcMap.get("STATUS").equals("TRIAL")) {
                                        earnDeductMap = EmployeeMap;
                                        System.out.println("@#$@#$@#earnDeductMap:" + earnDeductMap);
                                        System.out.println("@#$@#$@#ArrearCalcMap:" + ArrearCalcMap);
                                    }
                                    EmployeeMap = calculateEmployeeSalaryDetails(EmployeeMap, ArrearCalcMap);
                                    System.out.println("#@$@#after calculateEmployeeSalaryDetails@#$:" + EmployeeMap);
                                    if (!ArrearCalcMap.get("STATUS").equals("TRIAL")) {
                                        System.out.println("#@#$@#inside here:");
                                        earnDeductMap = calculateIndividualEarnings(earnDeductMap, ArrearCalcMap);
                                        System.out.println("#@$@#$@#calculateIndividualEarnings#$%#$" + earnDeductMap);
                                        //                                        changed here to see if there is nay effect on the calculation id the earnings are done b4 misc deduction
                                        earnDeductMap = calculateMiscellaneousDeductions(earnDeductMap, ArrearCalcMap);
                                        System.out.println("#@$@#$@#calculateMiscellaneousDeductions#$%#$" + earnDeductMap);
                                        earnDeductMap = calculateIndividualDeductions(earnDeductMap, ArrearCalcMap);
                                        System.out.println("#@$@#$@#calculateIndividualDeductions#$%#$" + earnDeductMap);
                                        System.out.println("salaryREportMap after everything:" + salaryReportMap);
                                    } else {
                                        EmployeeMap = calculateIndividualEarnings(EmployeeMap, ArrearCalcMap);
                                        System.out.println("#@$@#$@#calculateIndividualEarnings" + EmployeeMap);
                                        EmployeeMap = calculateMiscellaneousDeductions(EmployeeMap, ArrearCalcMap);
                                        System.out.println("#@$@#$@#calculateMiscellaneousDeductions#$%#$" + EmployeeMap);
                                        EmployeeMap = calculateIndividualDeductions(EmployeeMap, ArrearCalcMap);
                                        System.out.println("#@$@#$@#calculateIndividualDeductions" + EmployeeMap);
                                    }
                                    EmployeeSalaryDetails.add(EmployeeMap);
                                }
                            }
                        }
                        calculateMap = null;
                    }
                    System.out.println("#@$@#after everything EmployeeSalaryDetails:" + EmployeeSalaryDetails);
                }
            }
            System.out.println("@$@#$@#$grossSal:" + grossAmt);
            grossAmt = 0.0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return EmployeeSalaryDetails;
    }

    private HashMap calculateCommonAllowanceForEmp(HashMap empLstMap, LinkedHashMap calculateMap, HashMap ArrearCalcMap) throws Exception {
        /*This function is used to calculate the Common Allowances of an employee.
         *
         */
        //        Basic for particular Employee.
        HashMap eachListMap = new HashMap();
        try {
            double basicAmt = CommonUtil.convertObjToDouble(empLstMap.get("PRESENT_BASIC")).doubleValue();
            double basicAmtPerMonth = CommonUtil.convertObjToDouble(empLstMap.get("PRESENT_BASIC")).doubleValue();
            Object keys[] = calculateMap.keySet().toArray();
            Map tempMap = null;

            //      salTypeMap is used to store the salary type along with the salary type amount.
            LinkedHashMap salTypeMap = new LinkedHashMap();
            System.out.println("@#$@#$@$empLstMap: " + empLstMap);
            eachListMap.put("FIRST_NAME", empLstMap.get("FIRST_NAME"));
            eachListMap.put("EMPLOYEE_CODE", empLstMap.get("SYS_EMPID"));
            eachListMap.put("PRESENT_GRADE", empLstMap.get("PRESENT_GRADE"));
            eachListMap.put("PRESENT_DISGNATION", empLstMap.get("PRESENT_DISGNATION"));
            eachListMap.put("DATE_OF_JOIN", empLstMap.get("DATE_OF_JOIN"));
            eachListMap.put("DATE_OF_RETIREMENT", empLstMap.get("DATE_OF_RETIREMENT"));
            eachListMap.put("BRANCH_CODE", empLstMap.get("BRANCH_CODE"));
            eachListMap.put("ZONAL_CODE", empLstMap.get("ZONAL_CODE"));
            Date dateOfJoin = (Date) eachListMap.get("DATE_OF_JOIN");
            Date dateOfRet = (Date) eachListMap.get("DATE_OF_RETIREMENT");
            dateOfJoin = setProperDtFormat(dateOfJoin);
            dateOfRet = setProperDtFormat(dateOfRet);
            int month = CommonUtil.convertObjToInt(ArrearCalcMap.get("FROM_MONTH"));
            int year = CommonUtil.convertObjToInt(ArrearCalcMap.get("FROM_YEAR"));
            System.out.println("#@$#%#$%#$ArrearCalcMap:" + ArrearCalcMap);
            System.out.println("@#@$@#$@#month:" + month);
            System.out.println("@#@$@#$@#year:" + year);
            GregorianCalendar firstdaymonth = new GregorianCalendar(year, month - 1, 1);
            int noOfDays = firstdaymonth.getActualMaximum(firstdaymonth.DAY_OF_MONTH);
            int daysInMonth = noOfDays;
            System.out.println("#@$@$@$noOfDays:" + noOfDays);
            Date firstDateOfMonth = (Date) currDt.clone();
            firstDateOfMonth.setDate(1);
            firstDateOfMonth.setMonth(month - 1);
            firstDateOfMonth.setYear(year - 1900);
            //        first day of the salary calculaton month
            firstDateOfMonth = setProperDtFormat(firstDateOfMonth);
            Date lastDateOfMonth = (Date) currDt.clone();
            lastDateOfMonth.setDate(noOfDays);
            lastDateOfMonth.setMonth(month - 1);
            lastDateOfMonth.setYear(year - 1900);
            //        last day of the salary calculation month
            lastDateOfMonth = setProperDtFormat(lastDateOfMonth);
            System.out.println("@!#$@#$@#lastDateOfMonth :" + lastDateOfMonth);
            System.out.println("@!#$@#$@#firstDateOfMonth :" + firstDateOfMonth);
            int dateOfJoinDiff = (int) dateOfJoin.getDate();
            int dateOfRetDiff = (int) dateOfRet.getDate();
            if (dateOfJoin.after(firstDateOfMonth) && dateOfJoin.before(lastDateOfMonth)) {
                basicAmt = basicAmt / daysInMonth;
                dateOfJoinDiff = noOfDays - dateOfJoinDiff;
                noOfDays = dateOfJoinDiff;
                basicAmt = basicAmt * dateOfJoinDiff;
            } else if (dateOfRet.after(firstDateOfMonth) && dateOfRet.before(lastDateOfMonth)) {
                basicAmt = basicAmt / daysInMonth;
                basicAmt = basicAmt * dateOfRetDiff;
                noOfDays = dateOfRetDiff;
            }
            System.out.println("@#$@#$final no of days for which basic is eligible!!:" + noOfDays);

            basicAmt = (double) getNearest((long) (basicAmt * 100), 100) / 100;
            eachListMap.put("PRESENT_BASIC", new Double(basicAmt));
            //            salTypeMap.put("BASIC",new Double(basicAmt));
            System.out.println("#$%#$%#$%#$% calculate Map : " + calculateMap);
            eachListMap.put("FIRST_DATE", firstDateOfMonth);
            eachListMap.put("LAST_DATE", lastDateOfMonth);
            double lossOfPay = calculateLossOfPay(eachListMap, "LOSSOFPAY");
            double additionalPay = calculateLossOfPay(eachListMap, "ADDITIONALPAY");
            //            double additionalPay = calculateAdditionalPayDays(eachListMap, "");
            System.out.println("#$%#$%#$%additional Pay after comin back:" + additionalPay);
            System.out.println("#$%#$%#$%lossOfPay after comin back:" + lossOfPay);
            int lossOfPayDays = 0;
            lossOfPayDays = (int) lossOfPay;

            int additionalPayDays = (int) additionalPay;
            int remainingDays = lossOfPayDays - additionalPayDays;
            String lossOrAddFlag = "";
            //            FLAG IS USED TO IDENTIFY WHETHER IT IS LOSS OF PAY OR ADDITIONAL PAY
            if (remainingDays >= 0) {
                System.out.println("LOSS OF PAY IS GREATER!!!");
                lossOrAddFlag = "LOSSOFPAY";
                lossOfPayDays = remainingDays;
            } else {
                System.out.println("ADDITIONAL PAY IS GREATER!!!");
                lossOrAddFlag = "ADDITIONALPAY";
                lossOfPayDays = additionalPayDays - lossOfPayDays;
            }
            System.out.println("#@$@#$@#$lossOfPayDays:" + lossOfPayDays);
            if (lossOfPay > 0 || additionalPay > 0) {
                basicAmt = basicAmt / daysInMonth;
                if (lossOrAddFlag.equals("LOSSOFPAY")) {
                    //                    subtracting the no of days if the loss of pay is more than the additional pay
                    noOfDays = noOfDays - lossOfPayDays;
                } else if (lossOrAddFlag.equals("ADDITIONALPAY")) {
                    //                    adding the no of days if the additional pay is more than the loss of pay
                    noOfDays = noOfDays + lossOfPayDays;
                }
                System.out.println("@#$@#$@#$#@$noOfDays" + noOfDays);
                basicAmt = basicAmt * noOfDays;
                basicAmt = (double) getNearest((long) (basicAmt * 100), 100) / 100;
            }
            salTypeMap.put("BASIC", new Double(basicAmt));
            //hav to start from here
            grossAmt += basicAmt;
            System.out.println("@#$@#$!@#grossAmt:" + grossAmt);
            for (int i = 0; i < keys.length; i++) {
                tempMap = (HashMap) calculateMap.get(keys[i]);
                tempMap.put("BASIC", new Double(basicAmt));
                tempMap.put("PRESENT_GRADE", empLstMap.get("PRESENT_GRADE"));
                tempMap.put("BRANCH_CODE", empLstMap.get("BRANCH_CODE"));
                tempMap.put("NO_OF_DAYS", new Double(noOfDays));
                tempMap.put("DAYS_IN_MONTH", new Double(daysInMonth));
                tempMap.put("BASIC_PER_MONTH", new Double(basicAmtPerMonth));
                tempMap.put("SALARY_DT", firstDateOfMonth);
                salTypeMap.put(keys[i], new Double(calculateAllowance(keys[i], tempMap, basicAmt, lossOfPayDays, lossOrAddFlag)));
            }
            System.out.println("@#$@#$@#$@#grossAmt:" + grossAmt);
            eachListMap.put("SALARY_TYPE", salTypeMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return eachListMap;
    }

    private double calculateLossOfPay(HashMap lossOfPayMap, String lossOrAddFlag) throws Exception {
        double lossOfPay = 0.0;
        try {
            Date firstDateOfMonth = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lossOfPayMap.get("FIRST_DATE")));
            Date lastDateOfMonth = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lossOfPayMap.get("LAST_DATE")));

            int lOPDateDiff = 0;
            List lossOfPayList = null;
            if (lossOrAddFlag.equals("LOSSOFPAY")) {
                lossOfPayList = sqlMap.executeQueryForList("getLossOfPay", lossOfPayMap);
            } else if (lossOrAddFlag.equals("ADDITIONALPAY")) {
                lossOfPayList = sqlMap.executeQueryForList("getAdditionalPay", lossOfPayMap);
            }
            if (lossOfPayList != null && lossOfPayList.size() > 0) {
                for (int i = 0; i < lossOfPayList.size(); i++) {
                    HashMap lOPMap = (HashMap) lossOfPayList.get(i);
                    Date lossOPFrmDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lOPMap.get("FROM_DATE")));
                    Date lossOPToDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lOPMap.get("TO_DATE")));
                    System.out.println("#$%#$%#$%lossOPFrmDt:" + lossOPFrmDt);
                    System.out.println("#$%#$%#$%lossOPToDt:" + lossOPToDt);
                    System.out.println("#$%#$%#$%firstDateOfMonth:" + firstDateOfMonth);
                    System.out.println("#$%#$%#$%lastDateOfMonth:" + lastDateOfMonth);

                    if (lossOPToDt.before(lastDateOfMonth)) {
                        //                                if LOP from date is before the 1st day of the month
                        if (lossOPFrmDt.before(firstDateOfMonth) || lossOPFrmDt.equals(firstDateOfMonth)) {
                            lOPDateDiff = (int) DateUtil.dateDiff(firstDateOfMonth, lossOPToDt);
                            lOPDateDiff = lOPDateDiff + 1;
                        } //                                if LOP from date is after the 1st day of the month
                        else if (lossOPFrmDt.after(firstDateOfMonth)) {
                            lOPDateDiff = (int) DateUtil.dateDiff(lossOPFrmDt, lossOPToDt);
                            lOPDateDiff = lOPDateDiff + 1;
                        }
                        lossOfPay += lOPDateDiff;
                    } //                            If LOP till date comes after the last date of the salary month
                    else if (lossOPToDt.equals(lastDateOfMonth) || lossOPToDt.after(lastDateOfMonth)) {
                        System.out.println("#@$@#$@#$inside dis:to date:" + lossOPToDt + ":lastDate:" + lastDateOfMonth + ":lossOPFrmDt:" + lossOPFrmDt);
                        if (lossOPFrmDt.before(firstDateOfMonth) || lossOPFrmDt.equals(firstDateOfMonth)) {
                            System.out.println("@#$@#$1stone:");
                            lOPDateDiff = (int) DateUtil.dateDiff(firstDateOfMonth, lastDateOfMonth);
                            lOPDateDiff = lOPDateDiff + 1;
                        } else if (lossOPFrmDt.after(firstDateOfMonth)) {
                            System.out.println("@#$@#$2ndone:");
                            lOPDateDiff = (int) DateUtil.dateDiff(lossOPFrmDt, lastDateOfMonth);
                            lOPDateDiff = lOPDateDiff + 1;
                        }
                        lossOfPay += lOPDateDiff;
                    }
                }
                System.out.println("#$%#$%#$%lossOfPay inside loop:" + lossOfPay);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        return lossOfPay;
    }

    //    private double calculateAdditionalPayDays(HashMap lossOfPayMap) throws Exception{
    //        double additionalPay = 0.0;
    //        try{
    //            Date firstDateOfMonth = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lossOfPayMap.get("FIRST_DATE")));
    //            Date lastDateOfMonth = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lossOfPayMap.get("LAST_DATE")));
    //            int lOPDateDiff = 0;
    //            List lossOfPayList = sqlMap.executeQueryForList("getAdditionalPay",lossOfPayMap);
    //            if(lossOfPayList != null && lossOfPayList.size()>0){
    //                for(int i=0;i<lossOfPayList.size();i++){
    //                    HashMap lOPMap = (HashMap) lossOfPayList.get(i);
    //                    Date lossOPFrmDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lOPMap.get("FROM_DATE")));
    //                    Date  lossOPToDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lOPMap.get("TO_DATE")));
    //                    if(lossOPToDt.before(lastDateOfMonth)){
    //                        //                                if LOP from date is before the 1st day of the month
    //                        if(lossOPFrmDt.before(firstDateOfMonth) || lossOPFrmDt.equals(firstDateOfMonth)){
    //                            lOPDateDiff = (int)DateUtil.dateDiff(firstDateOfMonth,lossOPToDt);
    //                            lOPDateDiff = lOPDateDiff + 1;
    //                        }
    //                        //                                if LOP from date is after the 1st day of the month
    //                        else if(lossOPFrmDt.after(firstDateOfMonth)){
    //                            lOPDateDiff = (int)DateUtil.dateDiff(lossOPFrmDt,lossOPToDt);
    //                            lOPDateDiff = lOPDateDiff +  1;
    //                        }
    //                        additionalPay += lOPDateDiff;
    //                    }
    //                    //                            If LOP till date comes after the last date of the salary month
    //                    else if(lossOPToDt.equals(lastDateOfMonth) || lossOPToDt.after(lastDateOfMonth)){
    //                        System.out.println("#@$@#$@#$inside dis:to date:"+lossOPToDt+ ":lastDate:"+lastDateOfMonth+ ":lossOPFrmDt:"+lossOPFrmDt);
    //                        if(lossOPFrmDt.before(firstDateOfMonth) || lossOPFrmDt.equals(firstDateOfMonth)){
    //                            System.out.println("@#$@#$1stone:");
    //                            lOPDateDiff = (int)DateUtil.dateDiff(firstDateOfMonth,lastDateOfMonth);
    //                            lOPDateDiff = lOPDateDiff + 1;
    //                        }
    //                        else if(lossOPFrmDt.after(firstDateOfMonth)){
    //                            System.out.println("@#$@#$2ndone:");
    //                            lOPDateDiff = (int)DateUtil.dateDiff(lossOPFrmDt,lastDateOfMonth);
    //                            lOPDateDiff = lOPDateDiff + 1;
    //                        }
    //                        additionalPay += lOPDateDiff;
    //                    }
    //                    System.out.println("#$%#$%#$%lossOfPay inside loop:"+additionalPay);
    //                }
    //
    //            }
    //
    //        }catch(Exception e){
    //            e.printStackTrace();
    //        }
    //        return additionalPay;
    //    }
    private double calculateAllowance(Object salType, Map dataMap, double basicAmt, int lossOfPay, String lossOrAddFlag) throws Exception {
        double totAmt = 0.0;
        try {
            System.out.println("@#$#$@#dataMap:" + dataMap);
            System.out.println("@#$#$@#salType:" + salType);
            System.out.println("@@#$@$@#$lossOrAddFlag:" + lossOrAddFlag);
            int noOfDays = CommonUtil.convertObjToInt(dataMap.get("NO_OF_DAYS"));
            int daysInMonth = CommonUtil.convertObjToInt(dataMap.get("DAYS_IN_MONTH"));
            double percentage = CommonUtil.convertObjToDouble(dataMap.get("PERCENT")).doubleValue();
            double maxAmt = CommonUtil.convertObjToDouble(dataMap.get("MAX")).doubleValue();
            Date allowanceFrmDt = (Date) dataMap.get("FROM_DATE");
            allowanceFrmDt = setProperDtFormat(allowanceFrmDt);
            totAmt = basicAmt * percentage / 100;
            if (totAmt > maxAmt && maxAmt != 0) {
                totAmt = maxAmt;
            }
            if (CommonUtil.convertObjToStr(salType).equals("MA")) {
                if (lossOrAddFlag.equals("ADDITIONALPAY")) {
                    totAmt = percentage;
                    System.out.println("@#$@#$with MA additional pay:" + totAmt);
                } else if (lossOrAddFlag.equals("LOSSOFPAY")) {
                    percentage = percentage / daysInMonth;
                    percentage = percentage * noOfDays;
                    totAmt = percentage;
                    System.out.println("@#$@#$with MA loss of pay:" + totAmt);
                }
            } else if (CommonUtil.convertObjToStr(salType).equals("CCA")) {
                totAmt = 0;
                List salIdList = sqlMap.executeQueryForList("getCCAForCalculation", dataMap);
                System.out.println("@$#$@#$salIdList:" + salIdList);
                dataMap = new HashMap();
                if (salIdList != null && salIdList.size() > 0) {
                    dataMap = (HashMap) salIdList.get(0);
                    //                percentage conains the percent or fixed price based on the parameter
                    percentage = CommonUtil.convertObjToDouble(dataMap.get("PERCENT")).doubleValue();
                    maxAmt = CommonUtil.convertObjToDouble(dataMap.get("MAX")).doubleValue();
                    String percentOrFixed = CommonUtil.convertObjToStr(dataMap.get("PERCENT_OR_FIXED"));
                    totAmt = percentage;
                    System.out.println("@#$@#$@#percentOrFxcvxixed:" + percentOrFixed);
                    if (percentOrFixed.equals("FIXED")) {
                        percentage = percentage / daysInMonth;
                        percentage = percentage * noOfDays;
                        System.out.println("@#$@#$@#$percentage:" + percentage);
                        totAmt = percentage;
                    } else if (percentOrFixed.equals("PERCENT")) {
                        totAmt = basicAmt * percentage / 100;
                        System.out.println("@#$@#$@#percentOrFixed:" + percentOrFixed);
                    }

                }
                if (totAmt > maxAmt && maxAmt != 0) {
                    totAmt = maxAmt;
                }
            } else if (CommonUtil.convertObjToStr(salType).equals("WASHING ALLOWANCE")) {
                //            percentage = percentage/daysInMonth;
                //            percentage = percentage * noOfDays;
                //            totAmt = percentage;
                //            this allowance is not affected when additional pay is effected and effects wen loss of pay is there
                if (lossOrAddFlag.equals("ADDITIONALPAY")) {
                    totAmt = percentage;
                    System.out.println("@#$@#$with MA additional pay:" + totAmt);
                } else if (lossOrAddFlag.equals("LOSSOFPAY")) {
                    percentage = percentage / daysInMonth;
                    percentage = percentage * noOfDays;
                    totAmt = percentage;
                    System.out.println("@#$@#$with MA loss of pay:" + totAmt);
                }
            }
            totAmt = (double) getNearest((long) (totAmt * 100), 100) / 100;
        } catch (Exception e) {
            e.printStackTrace();
        }
        grossAmt += totAmt;
        return totAmt;
    }

    private HashMap getAllowanceParameters(String mapName, HashMap paramMap) throws Exception {
        //        Function used to calculate CCA.
        List resultList = null;
        HashMap resultMap = new HashMap();
        try {
            resultList = sqlMap.executeQueryForList(mapName, paramMap);
            if (resultList != null && resultList.size() > 0) {
                resultMap = (HashMap) resultList.get(0);
            } else {
                resultMap.put("MAX", "0.00");
                resultMap.put("FROM_DATE", paramMap.get("SALARY_DT"));
                resultMap.put("PERCENT", "0.0");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    private HashMap calculateMiscellaneousDeductions(HashMap EmployeeMap, HashMap ArrearCalcMap) throws Exception {
        /*This method is used to calculate the induvidual earnings of employees
         *based on Employee ID
         */
        try {
            HashMap earningOrDeductionMap = new HashMap();
            HashMap salaryTypeMap = new HashMap();
            HashMap salaryTypeForProcessMap = new HashMap();
            System.out.println("!#@$@#$@#$Miscellaneous deduction EmployeeMap" + EmployeeMap);
            System.out.println("!#@$@#$@#$Miscellaneous deduction ArrearCalcMap" + ArrearCalcMap);
            if (ArrearCalcMap.get("STATUS").equals("TRIAL")) {
                System.out.println("@#$@#$@#$in here buddy:");
                earningOrDeductionMap = (HashMap) EmployeeMap.get("EARNING_OR_DEDUCTION");
                salaryTypeMap = (HashMap) EmployeeMap.get("SALARY_TYPE");
            } else {
                salaryTypeForProcessMap = (HashMap) EmployeeMap.get("SALARY_TYPE");
            }
            int month = CommonUtil.convertObjToInt(ArrearCalcMap.get("FROM_MONTH"));
            int year = CommonUtil.convertObjToInt(ArrearCalcMap.get("FROM_YEAR"));
            System.out.println("#@$#%#$%#$mscArrearCalcMap:" + ArrearCalcMap);
            System.out.println("@#@$@#$@#mscmonth:" + month);
            System.out.println("@#@$@#$@#mscyear:" + year);
            GregorianCalendar firstdaymonth = new GregorianCalendar(year, month - 1, 1);
            int noOfDays = firstdaymonth.getActualMaximum(firstdaymonth.DAY_OF_MONTH);
            System.out.println("#@$@$@$noOfmscDays:" + noOfDays);
            Date firstDateOfMonth = (Date) currDt.clone();
            firstDateOfMonth.setDate(1);
            firstDateOfMonth.setMonth(month - 1);
            firstDateOfMonth.setYear(year - 1900);
            //        first day of the salary calculaton month
            firstDateOfMonth = setProperDtFormat(firstDateOfMonth);
            Date lastDateOfMonth = (Date) currDt.clone();
            lastDateOfMonth.setDate(noOfDays);
            lastDateOfMonth.setMonth(month - 1);
            lastDateOfMonth.setYear(year - 1900);
            //        last day of the salary calculation month
            lastDateOfMonth = setProperDtFormat(lastDateOfMonth);
            System.out.println("@!$@#$@#$EmployeeMap before individual earning:" + EmployeeMap);
            if (EmployeeMap.containsKey("PRESENT_GRADE") && EmployeeMap != null) {
                //                this query is used to get the different miscellanious deduction wrt to the specific grade
                ArrayList empMsckDeductionList = (ArrayList) sqlMap.executeQueryForList("getMisscellaneousDeductionForSalCalc", EmployeeMap);
                if (empMsckDeductionList != null && empMsckDeductionList.size() > 0) {
                    System.out.println("@!#!@#empMsckDeductionList:" + empMsckDeductionList);
                    for (int i = 0; i < empMsckDeductionList.size(); i++) {
                        HashMap miscDeductionMap = (HashMap) empMsckDeductionList.get(i);
                        String allowance_type = CommonUtil.convertObjToStr(miscDeductionMap.get("MD_DEDUCTION_TYPE"));
                        String allowance_amount = "";
                        miscDeductionMap.put("MD_GRADE", EmployeeMap.get("PRESENT_GRADE"));

                        //                         mite have to change based on if the actual basic or the basic after the LOP shud be calculated
                        miscDeductionMap.put("BASIC", EmployeeMap.get("PRESENT_BASIC"));
                        System.out.println("#$%#$%#$%grossAmt:" + grossAmt);
                        miscDeductionMap.put("GROSS", String.valueOf(grossAmt));
                        System.out.println("miscDeductionMap@#$@#$@#:" + miscDeductionMap);
                        System.out.println("@#$@#$@#$@#$salaryTypeMap:" + salaryTypeMap);
                        ArrayList individualDeductionDetailsList = new ArrayList();
                        if (CommonUtil.convertObjToStr(miscDeductionMap.get("USING_BASIC")).equals("B")) {
                            individualDeductionDetailsList = (ArrayList) sqlMap.executeQueryForList("getIndMscDeducDetUsingBasic", miscDeductionMap);
                        } else if (CommonUtil.convertObjToStr(miscDeductionMap.get("USING_BASIC")).equals("O")) {
                            individualDeductionDetailsList = (ArrayList) sqlMap.executeQueryForList("getIndividualMscDeductionDetails", miscDeductionMap);
                        } else if (CommonUtil.convertObjToStr(miscDeductionMap.get("USING_BASIC")).equals("G")) {
                            individualDeductionDetailsList = (ArrayList) sqlMap.executeQueryForList("getIndMscDeducDetUsingGross", miscDeductionMap);
                        }
                        if (individualDeductionDetailsList != null && individualDeductionDetailsList.size() > 0) {
                            double maxAmount = 0.0;
                            double finalAmount = 0.0;

                            System.out.println("@#$@#$individualDeductionDetailsList:" + individualDeductionDetailsList);
                            for (int j = 0; j < individualDeductionDetailsList.size(); j++) {
                                HashMap individualDeductionDetails = (HashMap) individualDeductionDetailsList.get(j);
                                System.out.println("$#%#$%#$%#individualDeductionDetails:" + individualDeductionDetails);
                                String max_deduction_amount = "";
                                String mdFixedOrPercentage = CommonUtil.convertObjToStr(individualDeductionDetails.get("MD_FIXED_OR_PERCENT"));
                                Date mscDeductionFromDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(individualDeductionDetails.get("MD_FROM_DATE")));
                                Date mscDeductionToDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(individualDeductionDetails.get("MD_TO_DATE")));
                                System.out.println("QW#@$@@#$mscDeductionFromDt:" + mscDeductionFromDt + ":mscDeductionToDt:" + mscDeductionToDt);
                                System.out.println("QW#@$@@#lastDateOfMonth:" + lastDateOfMonth + ":firstDateOfMonth:" + firstDateOfMonth);
                                if (mscDeductionFromDt.before(lastDateOfMonth) && (mscDeductionToDt == null || mscDeductionToDt.after(firstDateOfMonth))) {

                                    System.out.println("@#$@#$@#$inside Deduction" + noOfDays);
                                    if (mscDeductionToDt == null) {
                                        mscDeductionToDt = lastDateOfMonth;
                                    }
                                    System.out.println("@#$@#$@#$lastDateOfMonth:" + mscDeductionToDt);
                                    int deductionDateDiff = 0;
                                    //                            If deduction till date comes b4 the last date of the salary month
                                    if (mscDeductionToDt.before(lastDateOfMonth)) {
                                        //                                if deduction from date is before the 1st day of the month
                                        if (mscDeductionFromDt.before(firstDateOfMonth) || mscDeductionFromDt.equals(firstDateOfMonth)) {
                                            deductionDateDiff = (int) DateUtil.dateDiff(firstDateOfMonth, mscDeductionToDt);
                                            if (mdFixedOrPercentage.equals("PERCENTAGE")) {
                                                max_deduction_amount = String.valueOf((individualDeductionDetails.get("MAXIMUM_AMT")));
                                            } else if (mdFixedOrPercentage.equals("FIXED")) {
                                                max_deduction_amount = String.valueOf((individualDeductionDetails.get("MD_FIXED_AMT")));
                                            }

                                        } //                                if decuction from date is after the 1st day of the month
                                        else if (mscDeductionFromDt.after(firstDateOfMonth)) {
                                            deductionDateDiff = (int) DateUtil.dateDiff(mscDeductionFromDt, mscDeductionToDt);

                                            if (mdFixedOrPercentage.equals("PERCENTAGE")) {
                                                max_deduction_amount = String.valueOf((individualDeductionDetails.get("MAXIMUM_AMT")));
                                            } else if (mdFixedOrPercentage.equals("FIXED")) {
                                                max_deduction_amount = String.valueOf((individualDeductionDetails.get("MD_FIXED_AMT")));
                                            }
                                            //                                            max_deduction_amount = String.valueOf((individualDeductionDetails.get("MAXIMUM_AMT")));
                                        }

                                    } //                            If deduction till date comes after the last date of the salary month
                                    else if (mscDeductionToDt.equals(lastDateOfMonth) || mscDeductionToDt.after(lastDateOfMonth)) {
                                        if (mscDeductionFromDt.before(firstDateOfMonth) || mscDeductionFromDt.equals(firstDateOfMonth)) {
                                            deductionDateDiff = (int) DateUtil.dateDiff(firstDateOfMonth, lastDateOfMonth);
                                            if (mdFixedOrPercentage.equals("PERCENTAGE")) {
                                                max_deduction_amount = String.valueOf((individualDeductionDetails.get("MAXIMUM_AMT")));
                                            } else if (mdFixedOrPercentage.equals("FIXED")) {
                                                max_deduction_amount = String.valueOf((individualDeductionDetails.get("MD_FIXED_AMT")));
                                            }
                                            //                                            max_deduction_amount = String.valueOf((individualDeductionDetails.get("MAXIMUM_AMT")));
                                        } else if (mscDeductionFromDt.after(firstDateOfMonth)) {
                                            deductionDateDiff = (int) DateUtil.dateDiff(mscDeductionFromDt, lastDateOfMonth);
                                            if (mdFixedOrPercentage.equals("PERCENTAGE")) {
                                                max_deduction_amount = String.valueOf((individualDeductionDetails.get("MAXIMUM_AMT")));
                                            } else if (mdFixedOrPercentage.equals("FIXED")) {
                                                max_deduction_amount = String.valueOf((individualDeductionDetails.get("MD_FIXED_AMT")));
                                            }
                                            //                                            max_deduction_amount = String.valueOf((individualDeductionDetails.get("MAXIMUM_AMT")));
                                        }
                                    }
                                    if (!max_deduction_amount.equals("") && mdFixedOrPercentage.equals("PERCENTAGE")) {
                                        double eligibleAllowance = 0.0;
                                        System.out.println("@#$@#$deductionDateDiff:" + deductionDateDiff);
                                        maxAmount = CommonUtil.convertObjToDouble(max_deduction_amount).doubleValue();
                                        if (ArrearCalcMap.get("STATUS").equals("TRIAL")) {
                                            eligibleAllowance = CommonUtil.convertObjToDouble(salaryTypeMap.get(CommonUtil.convertObjToStr(individualDeductionDetails.get("ELIGIBLE_ALLOWANCE")))).doubleValue();
                                        } else {
                                            eligibleAllowance = CommonUtil.convertObjToDouble(salaryTypeForProcessMap.get(CommonUtil.convertObjToStr(individualDeductionDetails.get("ELIGIBLE_ALLOWANCE")))).doubleValue();

                                        }
                                        System.out.println("@#$@#$@#$eligibleAllowance:" + individualDeductionDetails.get("ELIGIBLE_ALLOWANCE") + ":" + eligibleAllowance);
                                        double percentValue = CommonUtil.convertObjToDouble(individualDeductionDetails.get("PERCENTAGE")).doubleValue();
                                        double eligiblePercentage = CommonUtil.convertObjToDouble(individualDeductionDetails.get("ELIGIBLE_PERCENTAGE")).doubleValue();

                                        eligibleAllowance = (eligibleAllowance * eligiblePercentage) / 100;
                                        System.out.println("#@$@#$@#$@#$eligibleAllowance after percent:" + eligibleAllowance);
                                        eligibleAllowance = eligibleAllowance * percentValue / 100;
                                        System.out.println("#@$@#$@#$@#$eligibleAllowance after final percent:" + eligibleAllowance);
                                        //                                        finalAmount +=eligibleAllowance;
                                        double amountPerDay = eligibleAllowance / noOfDays;
                                        deductionDateDiff = deductionDateDiff + 1;
                                        finalAmount += amountPerDay * deductionDateDiff;
//                                        finalAmount = (double)getNearest((long)(finalAmount*100),100)/100;
                                        System.out.println("@#$@#$@#$finalAmount:" + j + ":" + finalAmount);
                                        if (j == individualDeductionDetailsList.size() - 1) {
                                            finalAmount = (double) getNearest((long) (finalAmount * 100), 100) / 100;
                                            if (maxAmount < finalAmount) {
                                                finalAmount = maxAmount;
                                                allowance_amount = String.valueOf(finalAmount);
                                                System.out.println("@#$@#$amount:" + finalAmount + "#@$@#amountPerDay:" + amountPerDay);
                                            } else {
                                                allowance_amount = String.valueOf(finalAmount);
                                                System.out.println("@#$@#$am#$%ount:" + finalAmount + "#@$@#amou@#4ntPerDay:" + amountPerDay);
                                            }
                                        }

                                    } else if (mdFixedOrPercentage.equals("FIXED")) {
                                        maxAmount = CommonUtil.convertObjToDouble(max_deduction_amount).doubleValue();
                                        finalAmount = maxAmount;
                                        allowance_amount = String.valueOf(finalAmount);
                                        System.out.println("@#$@#$amount:" + finalAmount);
                                    }
                                }
                                if (!allowance_amount.equals("")) {
                                    salaryTypeMap.put(allowance_type, allowance_amount);
                                    if (!salaryReportMap.containsKey(allowance_type)) {
                                        salaryReportMap.put(allowance_type, String.valueOf(salaryReportMap.size() + 1));
                                    }
                                    earningOrDeductionMap.put(allowance_type, "DEDUCTION");
                                }

                            }
                        }
                    }
                }
                EmployeeMap.put("SALARY_TYPE", salaryTypeMap);
                EmployeeMap.put("EARNING_OR_DEDUCTION", earningOrDeductionMap);
                System.out.println("@#$@#$salary ReportMap after miscellaneousDeduction:" + salaryReportMap);
                System.out.println("@!$@#$@#$EmployeeMap after misc deduction:" + EmployeeMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EmployeeMap;
    }

    private HashMap calculateIndividualAllowances(HashMap EmployeeMap, HashMap ArrearCalcMap) throws Exception {
        /*This method is used to calculate the induvidual deductions and increments of employees
         *based on their basic,grade and Employee ID
         */
        try {
            //            allowanceList is used to list the various allowances applicable for a particular Employee
            ArrayList allowanceList = new ArrayList();
            LinkedHashMap salTypeMap = new LinkedHashMap();
            //            EmployeeMap contains the grade,branch id and the salary details from the previous function
            System.out.println("#$#$#$$EmployeeMap :" + EmployeeMap);
            HashMap allowanceListMap = new HashMap();
            ArrayList allowanceArrayList = new ArrayList();
            //            this query is used to generate different allowances applicable for a employee based on grade
            allowanceList = (ArrayList) sqlMap.executeQueryForList("getAllowanceID", EmployeeMap);
            System.out.println("!@#!#!@#allowanceList:" + allowanceList);
            //            it contains the details of salary along with the salary type
            salTypeMap = (LinkedHashMap) EmployeeMap.get("SALARY_TYPE");
            double basicAmt = CommonUtil.convertObjToDouble(salTypeMap.get("BASIC")).doubleValue();
            if (allowanceList.size() > 0) {
                for (int i = 0; i < allowanceList.size(); i++) {
                    if (allowanceListMap == null) {
                        allowanceListMap = new HashMap();
                    }
                    allowanceListMap = (HashMap) allowanceList.get(i);
                    System.out.println("@#$#@$@#$@#$@#$allowanceListMap" + allowanceListMap);
                    if (allowanceListMap != null) {
                        HashMap allowanceMap = new HashMap();
                        double allowanceAmt = 0.0;
                        allowanceMap = (HashMap) allowanceList.get(i);
                        if (allowanceMap.get("PERCENT_OR_FIXED").equals("PERCENT")) {
                            if (allowanceMap.get("USING_BASIC").equals("Y")) {
                                //                                get Percent and Max amount using Basic as the criteria
                                allowanceListMap.put("BASIC", salTypeMap.get("BASIC"));
                                List percentBasiclist = sqlMap.executeQueryForList("getPercentUsingBasic", allowanceListMap);
                                System.out.println("@#$@#$@#percentBasiclist: " + percentBasiclist);
                                HashMap percentBasicMap = new HashMap();
                                percentBasicMap = (HashMap) percentBasiclist.get(0);
                                double percent = CommonUtil.convertObjToDouble(percentBasicMap.get("AMT_OR_PER_ALLOWANCE")).doubleValue();
                                double maxAmount = CommonUtil.convertObjToDouble(percentBasicMap.get("MAX_AMOUNT")).doubleValue();
                                allowanceAmt = (percent * basicAmt) / 100;
                                if (allowanceAmt >= maxAmount) {
                                    allowanceAmt = maxAmount;
                                }
                                allowanceAmt = (double) getNearest((long) allowanceAmt, 100);
                                salTypeMap.put(allowanceMap.get("ALLOWANCE_ID"), new Double(allowanceAmt));
                                maxAmount = 0.0;
                                percent = 0.0;
                                allowanceAmt = 0.0;
                            } else {
                                //                                get Percent and Max amount without using basic as the criteria
                                List percentBasiclist = sqlMap.executeQueryForList("getPercentWithoutUsingBasic", allowanceListMap);
                                HashMap percentBasicMap = new HashMap();
                                percentBasicMap = (HashMap) percentBasiclist.get(0);
                                double percent = CommonUtil.convertObjToDouble(percentBasicMap.get("AMT_OR_PER_ALLOWANCE")).doubleValue();
                                double maxAmount = CommonUtil.convertObjToDouble(percentBasicMap.get("MAX_AMOUNT")).doubleValue();
                                allowanceAmt = (percent * basicAmt) / 100;
                                if (allowanceAmt >= maxAmount) {
                                    allowanceAmt = maxAmount;
                                }
                                allowanceAmt = (double) getNearest((long) allowanceAmt, 100);
                                salTypeMap.put(allowanceMap.get("ALLOWANCE_ID"), new Double(allowanceAmt));
                                maxAmount = 0.0;
                                percent = 0.0;
                                allowanceAmt = 0.0;
                            }
                        } else if (allowanceMap.get("PERCENT_OR_FIXED").equals("FIXED")) {
                            //                            get Fixed amount and Max amount using Basic as the Criteria
                            if (allowanceMap.get("USING_BASIC").equals("Y")) {
                                allowanceListMap.put("BASIC", salTypeMap.get("BASIC"));
                                List percentBasiclist = sqlMap.executeQueryForList("getPercentUsingBasic", allowanceListMap);
                                HashMap fixedBasicMap = new HashMap();
                                fixedBasicMap = (HashMap) percentBasiclist.get(0);
                                double fixed = CommonUtil.convertObjToDouble(fixedBasicMap.get("AMT_OR_PER_ALLOWANCE")).doubleValue();
                                double maxAmount = CommonUtil.convertObjToDouble(fixedBasicMap.get("MAX_AMOUNT")).doubleValue();
                                allowanceAmt = fixed;
                                if (allowanceAmt >= maxAmount) {
                                    allowanceAmt = maxAmount;
                                }
                                allowanceAmt = (double) getNearest((long) allowanceAmt, 100);
                                salTypeMap.put(allowanceMap.get("ALLOWANCE_ID"), new Double(allowanceAmt));
                                maxAmount = 0.0;
                                fixed = 0.0;
                                allowanceAmt = 0.0;
                            } else {
                                //                                 get Fixed Amount and max amount without using basic as the criteria
                                List percentBasiclist = sqlMap.executeQueryForList("getPercentWithoutUsingBasic", allowanceListMap);
                                HashMap fixedBasicMap = new HashMap();
                                fixedBasicMap = (HashMap) percentBasiclist.get(0);
                                double fixed = CommonUtil.convertObjToDouble(fixedBasicMap.get("AMT_OR_PER_ALLOWANCE")).doubleValue();
                                double maxAmount = CommonUtil.convertObjToDouble(fixedBasicMap.get("MAX_AMOUNT")).doubleValue();
                                allowanceAmt = fixed;
                                if (allowanceAmt >= maxAmount) {
                                    allowanceAmt = maxAmount;
                                }
                                allowanceAmt = (double) getNearest((long) allowanceAmt, 100);
                                salTypeMap.put(allowanceMap.get("ALLOWANCE_ID"), new Double(allowanceAmt));
                                maxAmount = 0.0;
                                fixed = 0.0;
                                allowanceAmt = 0.0;
                            }
                        }
                        allowanceMap = null;
                    }
                    allowanceListMap = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EmployeeMap;
    }

    private HashMap calculateEmployeeSalaryDetails(HashMap EmployeeMap, HashMap ArrearCalcMap) throws Exception {
        try {
            SalaryMasterDetailsTO objSalaryMasterDetailsTO = new SalaryMasterDetailsTO();
            //            TO calculate Employee Salary from
            EmployeeMap.put("SALARY_ID", salaryID);
            System.out.println("SADFSDFSDFSDFEmployeeMap" + EmployeeMap);
            //            if(ArrearCalcMap.get("STATUS").equals("FINAL")){
            //            sqlMap.executeUpdate("deleteSalaryMasterDetails",EmployeeMap);
            //            }
            LinkedHashMap salTypeMap = new LinkedHashMap();
            HashMap salEarnOrDeduction = new HashMap();
            salTypeMap = (LinkedHashMap) EmployeeMap.get("SALARY_TYPE");
            System.out.println("#@$@#$#@$salTypeMap" + salTypeMap);
            Object[] objKeys = salTypeMap.keySet().toArray();
            for (int i = 0; i < objKeys.length; i++) {
                objSalaryMasterDetailsTO.setSalaryId(salaryID);
                objSalaryMasterDetailsTO.setEmployeeId(CommonUtil.convertObjToStr(EmployeeMap.get("EMPLOYEE_CODE")));
                objSalaryMasterDetailsTO.setSalaryType(CommonUtil.convertObjToStr(objKeys[i]));
                objSalaryMasterDetailsTO.setAmount(CommonUtil.convertObjToStr(salTypeMap.get(objKeys[i])));
                objSalaryMasterDetailsTO.setZonalCode(CommonUtil.convertObjToStr(EmployeeMap.get("ZONAL_CODE")));
                objSalaryMasterDetailsTO.setBranchCode(CommonUtil.convertObjToStr(EmployeeMap.get("BRANCH_CODE")));
                System.out.println("@#@#@#SALARYTYPE:" + objKeys[i]);
                String salaryType = String.valueOf(CommonUtil.convertObjToStr(objKeys[i]));
                List earnOrDed = sqlMap.executeQueryForList("getEarningOrDeduction", salaryType);
                if (earnOrDed != null && earnOrDed.size() > 0) {
                    HashMap earnOrDedMap = new HashMap();
                    earnOrDedMap = (HashMap) earnOrDed.get(0);
                    if (earnOrDedMap.get("EARNING_OR_DEDUCTION").equals("EARNING")) {
                        if (ArrearCalcMap.get("STATUS").equals("TRIAL")) {
                            salEarnOrDeduction.put(objKeys[i], "EARNING");
                        } else {
                            objSalaryMasterDetailsTO.setEarningOrDeduction("EARNING");
                        }
                    } else {
                        if (ArrearCalcMap.get("STATUS").equals("TRIAL")) {
                            salEarnOrDeduction.put(objKeys[i], "DEDUCTION");
                        } else {
                            objSalaryMasterDetailsTO.setEarningOrDeduction("DEDUCTION");
                        }
                    }
                } else {
                    if (ArrearCalcMap.get("STATUS").equals("TRIAL")) {
                        salEarnOrDeduction.put(objKeys[i], "EARNING");
                    } else {
                        objSalaryMasterDetailsTO.setEarningOrDeduction("EARNING");
                    }
                }
                if (ArrearCalcMap.get("STATUS").equals("TRIAL")) {
                    System.out.println("@#$@#$@435#EmployeeMap:" + EmployeeMap);
                } else {
                    sqlMap.executeUpdate("insertSalaryMasterDetails", objSalaryMasterDetailsTO);
                }
                earnOrDed = null;
            }
            if (ArrearCalcMap.get("STATUS").equals("TRIAL")) {
                EmployeeMap.put("EARNING_OR_DEDUCTION", salEarnOrDeduction);
                System.out.println("!@#$@#@#$inside the last stmt:" + EmployeeMap);

            } else {
                EmployeeMap = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return EmployeeMap;
    }

    private List getBranchsGrades() {
        return null;
    }

    private HashMap calculateIndividualEarnings(HashMap EmployeeMap, HashMap ArrearCalcMap) throws Exception {
        /*This method is used to calculate the induvidual earnings of employees
         *based on Employee ID
         */
        try {
            HashMap earningOrDeductionMap = new HashMap();
            HashMap salaryTypeMap = new HashMap();
            System.out.println("!#@$@#$@#$Individual earnings EmployeeMap" + EmployeeMap);
            System.out.println("!#@$@#$@#$Individual earnings ArrearCalcMap" + ArrearCalcMap);
            //            if(ArrearCalcMap.get("STATUS").equals("TRIAL")){
            System.out.println("@#$@#$@#$in here buddy:");
            earningOrDeductionMap = (HashMap) EmployeeMap.get("EARNING_OR_DEDUCTION");
            salaryTypeMap = (HashMap) EmployeeMap.get("SALARY_TYPE");
            //            }
            int month = CommonUtil.convertObjToInt(ArrearCalcMap.get("FROM_MONTH"));
            int year = CommonUtil.convertObjToInt(ArrearCalcMap.get("FROM_YEAR"));
            System.out.println("#@$#%#$%#$ArrearCalcMap:" + ArrearCalcMap);
            System.out.println("@#@$@#$@#month:" + month);
            System.out.println("@#@$@#$@#year:" + year);
            GregorianCalendar firstdaymonth = new GregorianCalendar(year, month - 1, 1);
            int noOfDays = firstdaymonth.getActualMaximum(firstdaymonth.DAY_OF_MONTH);
            System.out.println("#@$@$@$noOfDays:" + noOfDays);
            Date firstDateOfMonth = (Date) currDt.clone();
            firstDateOfMonth.setDate(1);
            firstDateOfMonth.setMonth(month - 1);
            firstDateOfMonth.setYear(year - 1900);
            //        first day of the salary calculaton month
            firstDateOfMonth = setProperDtFormat(firstDateOfMonth);
            Date lastDateOfMonth = (Date) currDt.clone();
            lastDateOfMonth.setDate(noOfDays);
            lastDateOfMonth.setMonth(month - 1);
            lastDateOfMonth.setYear(year - 1900);
            //        last day of the salary calculation month
            lastDateOfMonth = setProperDtFormat(lastDateOfMonth);
            System.out.println("@!$@#$@#$EmployeeMap before individual earning:" + EmployeeMap);
            if (EmployeeMap.containsKey("EMPLOYEE_CODE") && EmployeeMap != null) {
                ArrayList empEarningDetailsList = (ArrayList) sqlMap.executeQueryForList("getEarningDetailsForSalCalc", EmployeeMap);
                if (empEarningDetailsList != null && empEarningDetailsList.size() > 0) {
                    System.out.println("@!#!@#empEarningDetailsList:" + empEarningDetailsList);
                    for (int i = 0; i < empEarningDetailsList.size(); i++) {

                        HashMap earningDetailsMap = (HashMap) empEarningDetailsList.get(i);
                        String allowance_amount = "";
                        String allowance_type = CommonUtil.convertObjToStr(earningDetailsMap.get("ALLOWANCE_TYPE"));
                        Date earningFromDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(earningDetailsMap.get("FROM_DATE")));
                        Date earningToDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(earningDetailsMap.get("TO_DATE")));
                        System.out.println("QW#@$@@#$earningFromDt:" + earningFromDt + ":earningToDt:" + earningToDt);
                        System.out.println("QW#@$@@#lastDateOfMonth:" + lastDateOfMonth + ":firstDateOfMonth:" + firstDateOfMonth);
                        if (earningFromDt.before(lastDateOfMonth) && (earningToDt == null || earningToDt.after(firstDateOfMonth))) {
                            System.out.println("@#$@#$@#$noOfDays inside earning" + noOfDays);
                            int earningDateDiff = 0;
                            if (earningToDt == null) {
                                earningToDt = lastDateOfMonth;
                            }
                            System.out.println("@#$@#$earningToDt@@#$@#$:" + earningToDt);
                            //                            If earning till date comes b4 the last date of the salary month
                            if (earningToDt.before(lastDateOfMonth)) {
                                //                                if earning from date is before the 1st day of the month
                                if (earningFromDt.before(firstDateOfMonth) || earningFromDt.equals(firstDateOfMonth)) {
                                    earningDateDiff = (int) DateUtil.dateDiff(firstDateOfMonth, earningToDt);
                                    allowance_amount = String.valueOf((earningDetailsMap.get("AMOUNT")));
                                } //                                if earning from date is after the 1st day of the month
                                else if (earningFromDt.after(firstDateOfMonth)) {
                                    earningDateDiff = (int) DateUtil.dateDiff(earningFromDt, earningToDt);
                                    allowance_amount = String.valueOf((earningDetailsMap.get("AMOUNT")));
                                }

                            } //                            If earning till date comes after the last date of the salary month
                            else if (earningToDt.equals(lastDateOfMonth) || earningToDt.after(lastDateOfMonth)) {
                                System.out.println("#@$@#$@#$inside dis:to date:" + earningToDt + ":lastDate:" + lastDateOfMonth + ":earningFromDt:" + earningFromDt);
                                if (earningFromDt.before(firstDateOfMonth) || earningFromDt.equals(firstDateOfMonth)) {
                                    System.out.println("@#$@#$1stone:");
                                    earningDateDiff = (int) DateUtil.dateDiff(firstDateOfMonth, lastDateOfMonth);
                                    allowance_amount = String.valueOf((earningDetailsMap.get("AMOUNT")));
                                } else if (earningFromDt.after(firstDateOfMonth)) {
                                    System.out.println("@#$@#$2ndone:");
                                    earningDateDiff = (int) DateUtil.dateDiff(earningFromDt, lastDateOfMonth);
                                    allowance_amount = String.valueOf((earningDetailsMap.get("AMOUNT")));
                                }
                            }
                            if (!allowance_amount.equals("")) {
                                System.out.println("@#$@#$earningDateDiff:" + earningDateDiff);
                                double amount = CommonUtil.convertObjToDouble(allowance_amount).doubleValue();
                                double amountPerDay = amount / noOfDays;
                                earningDateDiff = earningDateDiff + 1;
                                amount = amountPerDay * earningDateDiff;
                                amount = (double) getNearest((long) (amount * 100), 100) / 100;
                                //                                int allowance_amt = amount;
                                allowance_amount = String.valueOf(amount);
                                //                                added here to include individual earnings as a part of the gross amt
                                grossAmt += amount;
                                System.out.println("@#$@#$@#$@#$grossAmt:" + grossAmt);
                                System.out.println("@#$@#$amount:" + amount + "#@$@#amountPerDay:" + amountPerDay);
                            }
                        }
                        if (!allowance_amount.equals("")) {
                            if (salaryTypeMap.containsKey(allowance_type)) {
                                double amount = CommonUtil.convertObjToDouble(salaryTypeMap.get(allowance_type)).doubleValue();
                                System.out.println("234@@#$@#$ 2nd additional pay:" + amount);
                                amount += CommonUtil.convertObjToDouble(allowance_amount).doubleValue();
                                System.out.println("@#$@#$@#$new additional pay:" + amount);
                                allowance_amount = String.valueOf(amount);
                                salaryTypeMap.put(allowance_type, allowance_amount);
                                earningOrDeductionMap.put(allowance_type, "EARNING");
                            } else {
                                salaryTypeMap.put(allowance_type, allowance_amount);
                                earningOrDeductionMap.put(allowance_type, "EARNING");
                            }
                            if (!salaryReportMap.containsKey(allowance_type)) {
                                salaryReportMap.put(allowance_type, String.valueOf(salaryReportMap.size() + 1));
                            }
                        }
                    }
                }
                EmployeeMap.put("SALARY_TYPE", salaryTypeMap);
                EmployeeMap.put("EARNING_OR_DEDUCTION", earningOrDeductionMap);
                System.out.println("salaryReportMap after individual allowance:" + salaryReportMap);
                System.out.println("@!$@#$@#$EmployeeMap after individual earning:" + EmployeeMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EmployeeMap;
    }

    private HashMap calculateIndividualDeductions(HashMap EmployeeMap, HashMap ArrearCalcMap) throws Exception {
        /*This method is used to calculate the induvidual deductions of employees
         *based on their Employee ID
         */
        try {
            System.out.println("!#@$@#$@#$Individual deduction EmployeeMap" + EmployeeMap);
            System.out.println("!#@$@#$@#$Individual deduction ArrearCalcMap" + ArrearCalcMap);
            HashMap earningOrDeductionMap = (HashMap) EmployeeMap.get("EARNING_OR_DEDUCTION");
            HashMap salaryTypeMap = (HashMap) EmployeeMap.get("SALARY_TYPE");
            int month = CommonUtil.convertObjToInt(ArrearCalcMap.get("FROM_MONTH"));
            int year = CommonUtil.convertObjToInt(ArrearCalcMap.get("FROM_YEAR"));
            System.out.println("#@$#%#$%#$ArrearCalcMap:" + ArrearCalcMap);
            System.out.println("@#@$@#$@#month:" + month);
            System.out.println("@#@$@#$@#year:" + year);
            GregorianCalendar firstdaymonth = new GregorianCalendar(year, month - 1, 1);
            int noOfDays = firstdaymonth.getActualMaximum(firstdaymonth.DAY_OF_MONTH);
            System.out.println("#@$@$@$noOfDays deduction:" + noOfDays);
            Date firstDateOfMonth = (Date) currDt.clone();
            firstDateOfMonth.setDate(1);
            firstDateOfMonth.setMonth(month - 1);
            firstDateOfMonth.setYear(year - 1900);
            //        first day of the salary calculaton month
            firstDateOfMonth = setProperDtFormat(firstDateOfMonth);
            Date lastDateOfMonth = (Date) currDt.clone();
            lastDateOfMonth.setDate(noOfDays);
            lastDateOfMonth.setMonth(month - 1);
            lastDateOfMonth.setYear(year - 1900);
            //        last day of the salary calculation month
            lastDateOfMonth = setProperDtFormat(lastDateOfMonth);
            System.out.println("@!$@#$@#$EmployeeMap before individual deduction:" + EmployeeMap);
            if (EmployeeMap.containsKey("EMPLOYEE_CODE") && EmployeeMap != null) {
                ArrayList empDeductionDetailsList = (ArrayList) sqlMap.executeQueryForList("getDeductionDetailsForSalCalc", EmployeeMap);
                if (empDeductionDetailsList != null && empDeductionDetailsList.size() > 0) {
                    System.out.println("@!#!@#empDeductionDetailsList:" + empDeductionDetailsList);
                    for (int i = 0; i < empDeductionDetailsList.size(); i++) {
                        HashMap deductionDetailsMap = (HashMap) empDeductionDetailsList.get(i);
                        System.out.println("@#$@#$@#$deductionDetailsMap:" + deductionDetailsMap);
                        String deduction_type = CommonUtil.convertObjToStr(deductionDetailsMap.get("DEDUCTIONTYPE"));
                        String allowance_amount = "";
                        if (deductionDetailsMap.get("FIXED").equals("N")) {
                            //                            put the loan codin here
                            System.out.println("@#$@#$@$deduction starts here:");
                            Date deductionLoanFromDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(deductionDetailsMap.get("LOAN_FROMDATE")));
                            Date deductionLoanToDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(deductionDetailsMap.get("LOAN_TODATE")));
                            //                            allowance_amount = String.valueOf((deductionDetailsMap.get("INSTALLMENT_AMT")));
                            System.out.println("#@!$@#$@#deductionLoanToDt :" + deductionLoanToDt + ":deductionLoanFromDt : " + deductionLoanFromDt);
                            if (deductionLoanFromDt.before(lastDateOfMonth) && (deductionLoanToDt == null || deductionLoanToDt.after(firstDateOfMonth))) {

                                System.out.println("@#$@#$@#$inside Deduction" + noOfDays);
                                if (deductionLoanToDt == null) {
                                    deductionLoanToDt = lastDateOfMonth;
                                }
                                System.out.println("@#$@#$@#$lastDateOfMonth:" + deductionLoanToDt);
                                int deductionDateDiff = 0;
                                //                            If deduction till date comes b4 the last date of the salary month
                                if (deductionLoanToDt.before(lastDateOfMonth)) {
                                    //                                if deduction from date is before the 1st day of the month
                                    if (deductionLoanFromDt.before(firstDateOfMonth) || deductionLoanFromDt.equals(firstDateOfMonth)) {
                                        deductionDateDiff = (int) DateUtil.dateDiff(firstDateOfMonth, deductionLoanToDt);
                                        allowance_amount = String.valueOf((deductionDetailsMap.get("INSTALLMENT_AMT")));
                                    } //                                if decuction from date is after the 1st day of the month
                                    else if (deductionLoanFromDt.after(firstDateOfMonth)) {
                                        deductionDateDiff = (int) DateUtil.dateDiff(deductionLoanFromDt, deductionLoanToDt);
                                        allowance_amount = String.valueOf((deductionDetailsMap.get("INSTALLMENT_AMT")));
                                    }

                                } //                            If deduction till date comes after the last date of the salary month
                                else if (deductionLoanToDt.equals(lastDateOfMonth) || deductionLoanToDt.after(lastDateOfMonth)) {
                                    if (deductionLoanFromDt.before(firstDateOfMonth) || deductionLoanFromDt.equals(firstDateOfMonth)) {
                                        deductionDateDiff = (int) DateUtil.dateDiff(firstDateOfMonth, lastDateOfMonth);
                                        allowance_amount = String.valueOf((deductionDetailsMap.get("INSTALLMENT_AMT")));
                                    } else if (deductionLoanFromDt.after(firstDateOfMonth)) {
                                        deductionDateDiff = (int) DateUtil.dateDiff(deductionLoanFromDt, lastDateOfMonth);
                                        allowance_amount = String.valueOf((deductionDetailsMap.get("INSTALLMENT_AMT")));
                                    }
                                }
                                if (!allowance_amount.equals("")) {
                                    System.out.println("@#$@#$deductionDateDiff:" + deductionDateDiff);
                                    double amount = CommonUtil.convertObjToDouble(allowance_amount).doubleValue();
                                    double amountPerDay = amount / noOfDays;
                                    deductionDateDiff = deductionDateDiff + 1;
                                    amount = amountPerDay * deductionDateDiff;
                                    amount = (double) getNearest((long) (amount * 100), 100) / 100;
                                    allowance_amount = String.valueOf(amount);
                                    System.out.println("@#$@#$amount:" + amount + "#@$@#amountPerDay:" + amountPerDay);
                                }
                            }
                            if (!allowance_amount.equals("")) {
                                salaryTypeMap.put(deduction_type, allowance_amount);
                                earningOrDeductionMap.put(deduction_type, "DEDUCTION");
                                if (!salaryReportMap.containsKey(deduction_type)) {
                                    salaryReportMap.put(deduction_type, String.valueOf(salaryReportMap.size() + 1));
                                }
                            }
                        } else if (deductionDetailsMap.get("FIXED").equals("Y")) {
                            //                            put the other deduction here
                            System.out.println("inside fixed deduction:");
                            int fromMonth = CommonUtil.convertObjToInt(deductionDetailsMap.get("FROM_MM"));
                            int FromYear = CommonUtil.convertObjToInt(deductionDetailsMap.get("FROM_YYYY"));
                            int toMonth = CommonUtil.convertObjToInt(deductionDetailsMap.get("TO_MM"));
                            int toYear = CommonUtil.convertObjToInt(deductionDetailsMap.get("TO_YYYY"));
                            System.out.println("@#$@#$toYear:" + toYear + "FromYear:" + FromYear);
                            Date deductionFromDt = (Date) currDt.clone();
                            deductionFromDt.setDate(1);
                            deductionFromDt.setMonth(fromMonth - 1);
                            deductionFromDt.setYear(FromYear - 1900);
                            deductionFromDt = setProperDtFormat(deductionFromDt);
                            Date deductionToDt = (Date) currDt.clone();
                            deductionToDt.setDate(1);
                            //                            here toMonth value itself is used instead of toMonth - 1 as the next month has to be the end date
                            if (toMonth != 0 || toYear != 0) {
                                deductionToDt.setMonth(toMonth);
                                if (toMonth == 12) {
                                    deductionToDt.setYear(toYear - 1900 + 1);
                                } else {
                                    deductionToDt.setYear(toYear - 1900);
                                }
                                deductionToDt = setProperDtFormat(deductionToDt);
                            } else {
                                deductionToDt = null;
                            }
                            System.out.println("#@$@#$deductionToDt:" + deductionToDt + "!@#$deductionFromDt:" + deductionFromDt);
                            if (deductionFromDt.before(lastDateOfMonth) && (deductionToDt == null || deductionToDt.after(firstDateOfMonth))) {
                                allowance_amount = String.valueOf((deductionDetailsMap.get("AMOUNT")));
                                double amount = CommonUtil.convertObjToDouble(allowance_amount).doubleValue();
                                amount = (double) getNearest((long) (amount * 100), 100) / 100;
                                allowance_amount = String.valueOf(amount);
                            }
                            if (!allowance_amount.equals("")) {
                                salaryTypeMap.put(deduction_type, allowance_amount);
                                earningOrDeductionMap.put(deduction_type, "DEDUCTION");
                                if (!salaryReportMap.containsKey(deduction_type)) {
                                    salaryReportMap.put(deduction_type, String.valueOf(salaryReportMap.size() + 1));
                                }
                            }
                        }
                    }
                    EmployeeMap.put("SALARY_TYPE", salaryTypeMap);
                    EmployeeMap.put("EARNING_OR_DEDUCTION", earningOrDeductionMap);
                    System.out.println("after earning and deduction:" + EmployeeMap);
                    System.out.println("salaryREportMap after everything:" + salaryReportMap);
                }
                if (!ArrearCalcMap.get("STATUS").equals("TRIAL")) {
                    SalaryMasterDetailsTO objSalaryMasterDetailsTO = new SalaryMasterDetailsTO();
                    System.out.println("!@#$@#$@#$salaryID" + salaryID);
                    Object[] objKeys = salaryTypeMap.keySet().toArray();
                    for (int i = 0; i < objKeys.length; i++) {
                        objSalaryMasterDetailsTO.setSalaryId(salaryID);
                        objSalaryMasterDetailsTO.setEmployeeId(CommonUtil.convertObjToStr(EmployeeMap.get("EMPLOYEE_CODE")));
                        objSalaryMasterDetailsTO.setSalaryType(CommonUtil.convertObjToStr(objKeys[i]));
                        objSalaryMasterDetailsTO.setAmount(CommonUtil.convertObjToStr(salaryTypeMap.get(objKeys[i])));
                        objSalaryMasterDetailsTO.setZonalCode(CommonUtil.convertObjToStr(EmployeeMap.get("ZONAL_CODE")));
                        objSalaryMasterDetailsTO.setBranchCode(CommonUtil.convertObjToStr(EmployeeMap.get("BRANCH_CODE")));
                        objSalaryMasterDetailsTO.setEarningOrDeduction(CommonUtil.convertObjToStr(earningOrDeductionMap.get(objKeys[i])));
                        System.out.println("!#@$@#$@#$objSalaryMasterDetailsTO" + objSalaryMasterDetailsTO);
                        sqlMap.executeUpdate("insertSalaryMasterDetails", objSalaryMasterDetailsTO);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EmployeeMap;
    }

    private ArrayList getAllEmployeesSalary(HashMap ArrearCalcMap) throws Exception {
        System.out.println("@#$@#$inside getZonalOffices");
        HashMap branchListMap = new HashMap();
        ArrayList empSalDetails = null;
        List allBranchList = sqlMap.executeQueryForList("getBranchFromBranchMaster", null);
        List allEmployeeList = sqlMap.executeQueryForList("getAllEmployeeFrmEmplBasic", null);
        System.out.println("allaasda@#$@#$@#$BranchList:" + allBranchList);
        System.out.println("allaasda@#$@#$@#allEmployeeList:" + allEmployeeList);
        //        if(allBranchList != null && allBranchList.size() >0){
        //            for(int j=0 ; j < allBranchList.size();j++){
        branchListMap = new HashMap();
        //                branchListMap = (HashMap) allBranchList.get(j);
        if (allEmployeeList != null && allEmployeeList.size() > 0) {
            HashMap allEmployeeListMap = new HashMap();
            for (int i = 0; i < allEmployeeList.size(); i++) {
                HashMap empMap = new HashMap();
                empMap = (HashMap) allEmployeeList.get(i);
                allEmployeeListMap.put("FROM_EMPLOYEE_CODE", empMap.get("EMPLOYEE_ID"));
                allEmployeeListMap.put("CURRENT_DT", currDt);
                System.out.println("@#$@#$allEmployeeListMap" + allEmployeeListMap);
                EmployeeLst = sqlMap.executeQueryForList("getSingleEmployeeDetail", allEmployeeListMap);
                System.out.println("!!@#!@#!#EmployeeLst:" + EmployeeLst);
                ArrayList EmployeeSalaryDetails = calculateCommonAllowances(branchListMap, EmployeeLst, ArrearCalcMap);
                System.out.println("!!@#!@#!#EmployeeSalaryDetails:" + EmployeeSalaryDetails);
                if (empSalDetails == null) {
                    empSalDetails = new ArrayList();
                }
                empSalDetails.add(EmployeeSalaryDetails);
            }
        }
        System.out.println("@#$@#$empSalDetails:" + empSalDetails);
        return empSalDetails;
    }

    private String getSalaryIDGeneration() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "SALARY_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private String getIncrementIDGeneration() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "INCREMENT_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private String getPromotionIDGeneration() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "PROMOTION_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void processIncrementDetails(HashMap map, IncrementTO objDTTO) throws Exception {
        try {
            sqlMap.startTransaction();
            String command = (String) map.get("COMMAND");
            if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                if (objDTTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                    System.out.println("@#$@#$#@insertIncrementTO" + objDTTO);
                    sqlMap.executeUpdate("insertIncrementTO", objDTTO);
                } else {
                    System.out.println("@#$@#$#@updateIncrementTO" + objDTTO);
                    sqlMap.executeUpdate("updateIncrementTO", objDTTO);
                }
            } else if (command.equals(CommonConstants.TOSTATUS_INSERT)) {

                String incrementID = getIncrementIDGeneration();
                objDTTO.setIncrementID(incrementID);
                System.out.println("########increment objDTTO :" + objDTTO);
                sqlMap.executeUpdate("insertIncrementTO", objDTTO);
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {

                System.out.println("########delete objDTTO :" + objDTTO);
                objDTTO.setStatus(CommonConstants.STATUS_DELETED);
                sqlMap.executeUpdate("deleteIncrementTO", objDTTO);
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void processPromotionDetails(HashMap map, PromotionTO objEDTO) throws Exception {
        try {
            sqlMap.startTransaction();
            if (CommonUtil.convertObjToStr(objEDTO.getStatus()).equals(CommonConstants.STATUS_MODIFIED)) {
                System.out.println("########update objEDTO :" + objEDTO);
                sqlMap.executeUpdate("updatePromotionTO", objEDTO);
            } else if (CommonUtil.convertObjToStr(objEDTO.getStatus()).equals(CommonConstants.STATUS_CREATED)) {
                String promotionID = getPromotionIDGeneration();
                objEDTO.setPromotionID(promotionID);
                System.out.println("########insert objEDTO :" + objEDTO);
                sqlMap.executeUpdate("insertPromotionTO", objEDTO);
            } else if (CommonUtil.convertObjToStr(objEDTO.getStatus()).equals(CommonConstants.STATUS_DELETED)) {
                objEDTO.setStatus(CommonConstants.STATUS_DELETED);
                System.out.println("########delete Records objEDTO :" + objEDTO);
                sqlMap.executeUpdate("deletePromotionTO", objEDTO);
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateData(HashMap map) throws Exception {

        if (IncrementTOs != null && IncrementTOs.size() > 0) {
            int size = IncrementTOs.size();
            System.out.println("######## :" + size + "IncrementTOs :" + IncrementTOs);
            System.out.println("########updateData :" + map + "size :" + size);
            for (int i = 0; i < size; i++) {
                objDTTO = (IncrementTO) IncrementTOs.get(i);
                Date fromDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objDTTO.getCreatedDate()));
                Date toDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objDTTO.getEffectiveDate()));
                Date statusDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objDTTO.getStatusDt()));
                if (fromDt != null) {
                    Date insDate = (Date) currDt.clone();
                    insDate.setDate(fromDt.getDate());
                    insDate.setMonth(fromDt.getMonth());
                    insDate.setYear(fromDt.getYear());
                    objDTTO.setCreatedDate(insDate);
                }
                if (toDt != null) {
                    Date insDate = (Date) currDt.clone();
                    insDate.setDate(toDt.getDate());
                    insDate.setMonth(toDt.getMonth());
                    insDate.setYear(toDt.getYear());
                    objDTTO.setEffectiveDate(insDate);
                }
                if (statusDt != null) {
                    Date insDate = (Date) currDt.clone();
                    insDate.setDate(statusDt.getDate());
                    insDate.setMonth(statusDt.getMonth());
                    insDate.setYear(statusDt.getYear());
                    objDTTO.setStatusDt(insDate);
                }
                processIncrementDetails(map, objDTTO);
            }
        }
        if (PromotionTOs != null && PromotionTOs.size() > 0) {
            int size = PromotionTOs.size();
            for (int i = 0; i < size; i++) {
                objEDTO = (PromotionTO) PromotionTOs.get(i);
                processPromotionDetails(map, objEDTO);
            }
        } else if (SalaryCalcList != null && SalaryCalcList.size() > 0) {
            processSalaryDetails(map);
        }
    }

    private void deleteData(HashMap map) throws Exception {

        if (IncrementTOs != null && IncrementTOs.size() > 0) {
            int size = IncrementTOs.size();
            System.out.println("######## :" + size + "IncrementTOs :" + IncrementTOs);
            System.out.println("########delete :" + map + "size :" + size);
            for (int i = 0; i < size; i++) {
                objDTTO = (IncrementTO) IncrementTOs.get(i);
                Date fromDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objDTTO.getCreatedDate()));
                Date toDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objDTTO.getEffectiveDate()));
                Date statusDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objDTTO.getStatusDt()));
                if (fromDt != null) {
                    Date insDate = (Date) currDt.clone();
                    insDate.setDate(fromDt.getDate());
                    insDate.setMonth(fromDt.getMonth());
                    insDate.setYear(fromDt.getYear());
                    objDTTO.setCreatedDate(insDate);
                }
                if (toDt != null) {
                    Date insDate = (Date) currDt.clone();
                    insDate.setDate(toDt.getDate());
                    insDate.setMonth(toDt.getMonth());
                    insDate.setYear(toDt.getYear());
                    objDTTO.setEffectiveDate(insDate);
                }
                if (statusDt != null) {
                    Date insDate = (Date) currDt.clone();
                    insDate.setDate(statusDt.getDate());
                    insDate.setMonth(statusDt.getMonth());
                    insDate.setYear(statusDt.getYear());
                    objDTTO.setStatusDt(insDate);
                }
                processIncrementDetails(map, objDTTO);
            }
        } else if (SalaryCalcList != null && SalaryCalcList.size() > 0) {
            processSalaryDetails(map);
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
        IncrementTOs = (ArrayList) map.get("IncrementTOs");
        PromotionTOs = (ArrayList) map.get("PromotionTOs");
        SalaryCalcList = (ArrayList) map.get("SalaryCalcList");
        deleteIncrementList = (ArrayList) map.get("deleteIncrementList");
        deletePromotionList = (ArrayList) map.get("deletePromotionList");
        System.out.println("deleteIncrementList" + deleteIncrementList);
        final String command = (String) map.get("COMMAND");
        System.out.println("Command@##$$##% :" + command);
        if ((IncrementTOs != null && IncrementTOs.size() > 0) || (PromotionTOs != null && PromotionTOs.size() > 0) || (SalaryCalcList != null && SalaryCalcList.size() > 0)) {
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                if (map.containsKey("SalaryCalcList")) {
                    return insertData(map);
                } else {
                    insertData(map);
                }
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                System.out.println("Inside Update!!!");
                updateData(map);
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                System.out.println("Inside delete!!!");
                deleteData(map);
            }
        } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            //        else if (map.containsKey("INCREMENT_SCREEN")) {
            System.out.println("Inside authorize!!!");
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            //              HashMap authMap = (HashMap) map.get("INCREMENT_SCREEN");
            if (authMap != null) {
                System.out.println("authMap#@$@#$!!!" + authMap);
                authorize(authMap);
            }
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            //deleteData();
        } else {
            throw new NoCommandException();
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
            if (AuthMap.containsKey("INCREMENT_REASON")) {
                System.out.println("insertEmployeeBasic#$#@$#@" + AuthMap);
                sqlMap.executeUpdate("updateAuthorizeStatusIncrement", AuthMap);
                sqlMap.executeUpdate("insertEmployeeBasic", AuthMap);
                sqlMap.executeUpdate("updateEmployeePresentDIncrements", AuthMap);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
        if ((mod < (roundingFactor / 2)) || (mod < (roundingFactorOdd / 2))) {
            return lower(number, roundingFactor);
        } else {
            return higher(number, roundingFactor);
        }
    }

    public long lower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    public long higher(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        if (mod == 0) {
            return number;
        }
        return (number - mod) + roundingFactor;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        System.out.println("#@$@#$@#$$#%obj:" + obj);
        if (obj.containsKey("SalaryCalcList")) {
            SalaryCalcList = (ArrayList) obj.get("SalaryCalcList");
            return insertData(obj);
        } else {
            return getData(obj);
        }
    }

    private void destroyObjects() {
        objDTTO = null;
        objEDTO = null;
        IncrementTOs = null;
        PromotionTOs = null;
        SalaryCalcList = null;
    }
}
