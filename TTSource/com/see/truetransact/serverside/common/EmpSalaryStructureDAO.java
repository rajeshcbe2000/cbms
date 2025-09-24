/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * SalaryStructureDAO.java
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
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.common.EmpSalaryStructureTO;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Date;

/**
 * EmpSalaryStructure DAO.
 *
 * @author
 *
 */
public class EmpSalaryStructureDAO extends TTDAO {

    private HashMap whereConditions;
    private String whereCondition;
    private static SqlMap sqlMap = null;
    HashMap resultMap = new HashMap();
    HashMap deletedsalary = new HashMap();
    Date currDt = null;
    private String addressKey = new String();
    private HashMap data;
    private LogDAO logDAO;
    private List list;
    private String tableName;
    private String tableCondition;
    public ArrayList zonalValueList = new ArrayList();
    public ArrayList branchValueList = new ArrayList();
    public ArrayList gradeValueList = new ArrayList();
    public ArrayList populationValueList = new ArrayList();
    private String cmd;
    private LogTO logTO;
    private Iterator addressIterator;
    LinkedHashMap empSalaryDetilsMap = new LinkedHashMap();

    /**
     * Creates a new instance of SalaryStructureDAO
     */
    public EmpSalaryStructureDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
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

    public static void main(String str[]) {
        try {
            SalaryStructureDAO dao = new SalaryStructureDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        return returnMap;
    }

    public void getAllData() throws Exception {
        if (data.containsKey("ZONAL_LIST")) {
            zonalValueList = (ArrayList) data.get("ZONAL_LIST");
        }
        if (data.containsKey("GRADE_LIST")) {
            gradeValueList = (ArrayList) data.get("GRADE_LIST");
        }
        if (data.containsKey("POPULATION_LIST")) {
            populationValueList = (ArrayList) data.get("POPULATION_LIST");
        }
        if (data.containsKey("BRANCH_LIST")) {
            branchValueList = (ArrayList) data.get("BRANCH_LIST");
        }
        resultMap = (HashMap) data.get("SALARY_DETAILS");
        if (data.containsKey("SALARY_DETAILS_DELETED")) {
            deletedsalary = (HashMap) data.get("SALARY_DETAILS_DELETED");
        }
    }

    /**
     * Standard method to get data for a particular customer based on customer
     * id
     */
    public HashMap executeQuery(HashMap condition) throws Exception {
        System.out.println("###condition" + condition);
        _branchCode = (String) condition.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        whereConditions = condition;
        if (condition.containsKey(CommonConstants.MAP_WHERE)) {
            whereCondition = (String) condition.get(CommonConstants.MAP_WHERE);
        }
        System.out.println("###whereconditions" + whereConditions);
        getEmpSalaryDetails();
        makeNull();
        makeQueryNull();
        System.out.println("#@$@#$@#$ data :" + data);
        return data;
    }

    private void getAllowanceDetails() throws Exception {
        list = (List) sqlMap.executeQueryForList("getSelectSalDetailsViewList", whereConditions);
        HashMap allowanceDetails = new HashMap();
        allowanceDetails = (HashMap) list.get(0);
        data.put("ALLOWANCE_DETAILS", allowanceDetails);

    }

    public void getEmpSalaryDetails() throws Exception {
        data = new HashMap();
        list = (List) sqlMap.executeQueryForList("getSelectSalaryDetails", whereConditions);
        if (list.size() > 0) {
            LinkedHashMap salDetailsMap = new LinkedHashMap();
            for (int i = list.size(), j = 0; i > 0; i--, j++) {
                salDetailsMap.put(((EmpSalaryStructureTO) list.get(j)).getSlNo(), list.get(j));
            }
            data.put("SALARY_DETAILS", salDetailsMap);
        }
        getAllowanceDetails();
        System.out.println("#$#$#$#$#$SALARY_DETAILS :" + data);
        branchValueList = (ArrayList) sqlMap.executeQueryForList("getSelectSalaryBranch", whereConditions);
        if (branchValueList.size() > 0) {
            HashMap branchMap = new HashMap();
            ArrayList branchList = new ArrayList();
            for (int i = 0; i < branchValueList.size(); i++) {
                branchMap = (HashMap) branchValueList.get(i);
                branchList.add(branchMap.get("BRANCH_ID"));
            }
            data.put("BRANCH_LIST", branchList);
        }
        zonalValueList = (ArrayList) sqlMap.executeQueryForList("getSelectSalaryZonal", whereConditions);
        if (zonalValueList.size() > 0) {
            HashMap zonalMap = new HashMap();
            ArrayList zonalList = new ArrayList();
            for (int i = 0; i < zonalValueList.size(); i++) {
                zonalMap = (HashMap) zonalValueList.get(i);
                zonalList.add(zonalMap.get("ZONAL_ID"));
            }
            System.out.println("#$$#$#zonalList" + zonalList);
            data.put("ZONAL_LIST", zonalList);
        }
        populationValueList = (ArrayList) sqlMap.executeQueryForList("getSelectSalaryPopulation", whereConditions);
        if (populationValueList.size() > 0) {
            HashMap populationMap = new HashMap();
            ArrayList populationList = new ArrayList();
            for (int i = 0; i < populationValueList.size(); i++) {
                populationMap = (HashMap) populationValueList.get(i);
                populationList.add(populationMap.get("POPULATION_TYPE"));
            }
            System.out.println("#$$#$#populationValueList" + populationList);
            data.put("POPULATION_LIST", populationList);
        }
        gradeValueList = (ArrayList) sqlMap.executeQueryForList("getSelectSalaryGrade", whereConditions);
        if (gradeValueList.size() > 0) {
            HashMap gradeMap = new HashMap();
            ArrayList gradeList = new ArrayList();
            for (int i = 0; i < gradeValueList.size(); i++) {
                gradeMap = (HashMap) gradeValueList.get(i);
                gradeList.add(gradeMap.get("GRADE_ID"));
            }
            data.put("GRADE_LIST", gradeList);
        }
    }

    private void makeNull() {
        resultMap = null;
        zonalValueList = null;
        branchValueList = null;
        gradeValueList = null;
        populationValueList = null;
        addressIterator = null;
        deletedsalary = null;
    }

    /**
     * To make used object in executeQuery method as null
     */
    private void makeQueryNull() {
        whereCondition = null;
        list = null;
    }

    /**
     * To make data object null
     */
    private void makeDataNull() {
        data = null;
    }

    private void processSalaryData(String command) throws Exception {

        if (deletedsalary != null) {
            String key = "";
            addressIterator = deletedsalary.keySet().iterator();
            for (int i = deletedsalary.size(); i > 0; i--) {
                key = (String) addressIterator.next();
                System.out.println("entering deleted SALARY DATA map!!" + deletedsalary);
                EmpSalaryStructureTO objEmpSalaryStructureTO = (EmpSalaryStructureTO) deletedsalary.get(key);
                logTO.setData(objEmpSalaryStructureTO.toString());
                logTO.setPrimaryKey(objEmpSalaryStructureTO.getKeyData());
                logTO.setStatus(objEmpSalaryStructureTO.getCommand());
                sqlMap.executeUpdate("deleteEmpSalaryDetailsTO", objEmpSalaryStructureTO);
                logDAO.addToLog(logTO);
            }
        }
        System.out.println("@#$@#$#@$ inside Process salarydata: " + command);
        if (resultMap != null) {
            addressIterator = resultMap.keySet().iterator();
            System.out.println("@#@#SALARY_ALLOWANCE_DETAILS:" + data.get("SALARY_ALLOWANCE_DETAILS"));
            EmpSalaryStructureTO objEmpSalaryStructureTO = (EmpSalaryStructureTO) data.get("SALARY_ALLOWANCE_DETAILS");
            String statusBy = CommonUtil.convertObjToStr(objEmpSalaryStructureTO.getStatusBy());
            String status = CommonUtil.convertObjToStr(objEmpSalaryStructureTO.getStatus());
            for (int i = resultMap.size(); i > 0; i--) {
                addressKey = (String) addressIterator.next();
                System.out.println("#$#$#$resultMap:" + resultMap);
                objEmpSalaryStructureTO = (EmpSalaryStructureTO) resultMap.get(addressKey);
                logTO.setData(objEmpSalaryStructureTO.toString());
                logTO.setPrimaryKey(objEmpSalaryStructureTO.getKeyData());
                logTO.setStatus(objEmpSalaryStructureTO.getCommand());
                System.out.println("salaryDetailsTO" + objEmpSalaryStructureTO);
                objEmpSalaryStructureTO.setStatusBy(statusBy);
                Date fromDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objEmpSalaryStructureTO.getFromDate()));
                if (fromDt != null) {
                    Date insDate = (Date) currDt.clone();
                    insDate.setDate(fromDt.getDate());
                    insDate.setMonth(fromDt.getMonth());
                    insDate.setYear(fromDt.getYear());
                    objEmpSalaryStructureTO.setFromDate(insDate);
                }
                if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    System.out.println("@#@#@# STATUS:" + objEmpSalaryStructureTO.getStatus());
                    if (objEmpSalaryStructureTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                        sqlMap.executeUpdate("insertEmpSalaryDetailsTO", objEmpSalaryStructureTO);
                    } else {
                        sqlMap.executeUpdate("updateEmpSalaryDetailsTO", objEmpSalaryStructureTO);
                    }
                    System.out.println("@#$@#$@#$ updateEmpsal :" + objEmpSalaryStructureTO);
                    sqlMap.executeUpdate("updateSalaryAllowanceType", objEmpSalaryStructureTO);

                } else if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    System.out.println("$%^$%^$%^%$^objEmpSalaryStructureTO" + objEmpSalaryStructureTO);
                    sqlMap.executeUpdate("insertEmpSalaryDetailsTO", objEmpSalaryStructureTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    objEmpSalaryStructureTO.setStatusBy(statusBy);
                    objEmpSalaryStructureTO.setStatus(CommonConstants.STATUS_DELETED);
                    System.out.println("#$#$#inside the delete:" + objEmpSalaryStructureTO);
                    sqlMap.executeUpdate("deleteEmpSalaryDetailsTO", objEmpSalaryStructureTO);
                    sqlMap.executeUpdate("deleteEmpSalPopulationType", objEmpSalaryStructureTO);
                    sqlMap.executeUpdate("deleteEmpSalaryGrade", objEmpSalaryStructureTO);
                    sqlMap.executeUpdate("deleteEmpSalaryBranch", objEmpSalaryStructureTO);
                    sqlMap.executeUpdate("deleteEmpSalaryZonal", objEmpSalaryStructureTO);
                    sqlMap.executeUpdate("deleteEmpSalaryAllowanceType", objEmpSalaryStructureTO);
                }
                logDAO.addToLog(logTO);
            }
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        System.out.println("#### DAO execute obj map : " + map);
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        System.out.println("#### DAO command : " + command);
        HashMap resultMap = new HashMap();
        try {
            sqlMap.startTransaction();
            if (map.containsKey("SALARY_ALLOWANCE_DETAILS")) {
                EmpSalaryStructureTO objEmpSalaryStructureTO = (EmpSalaryStructureTO) map.get("SALARY_ALLOWANCE_DETAILS");
                System.out.println("#$#$#$#$objEmpSalaryStructureTO" + objEmpSalaryStructureTO);
                int length = 0;
                length = resultMap.size();
                System.out.println("@#@#@#@#@#SALARY_DETAILS" + resultMap);
                logDAO = new LogDAO();
                logTO = new LogTO();
                logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.SELECTED_BRANCH_ID)));
                logTO.setInitiatedBranch(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
                logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
                logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
                logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

                data = map;
                //Start the transaction
                if (command.equals(CommonConstants.TOSTATUS_INSERT) || command.equals(CommonConstants.TOSTATUS_UPDATE)
                        || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    if (objEmpSalaryStructureTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                        insertData();
                    } else if (objEmpSalaryStructureTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                        updateData();
                    } else if (objEmpSalaryStructureTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                        deleteData();
                    }
                }
            } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
                HashMap _authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
                if (_authMap != null) {
                    authorize(_authMap);
                }
            } else {
                sqlMap.rollbackTransaction();
                throw new NoCommandException();
            }
            //Commit the transaction
            sqlMap.commitTransaction();
        } catch (Exception ex) {
            sqlMap.rollbackTransaction();
            throw ex;
        }
        return resultMap;
    }

    private void authorize(HashMap _authMap) throws Exception {
        System.out.println("#@$@$@#$_authMap" + _authMap);
        ArrayList authList = (ArrayList) _authMap.get(CommonConstants.AUTHORIZEDATA);
        HashMap authMap = new HashMap();
        authMap = (HashMap) authList.get(0);
        sqlMap.executeUpdate("authSalaryAllowanceType", authMap);

    }

    private void deleteData() throws Exception {
        getAllData();
        EmpSalaryStructureTO objEmpSalaryStructureTO = (EmpSalaryStructureTO) data.get("SALARY_ALLOWANCE_DETAILS");
        processSalaryData(CommonUtil.convertObjToStr(objEmpSalaryStructureTO.getCommand()));
        System.out.println("@#@!#@Inside the delete" + objEmpSalaryStructureTO);
        makeDataNull();
        makeNull();
    }

    private void updateData() throws Exception {
        getAllData();
        EmpSalaryStructureTO objEmpSalaryStructureTO = (EmpSalaryStructureTO) data.get("SALARY_ALLOWANCE_DETAILS");
        processSalaryData(CommonUtil.convertObjToStr(objEmpSalaryStructureTO.getCommand()));
        sqlMap.executeUpdate("deleteSalaryZonal", objEmpSalaryStructureTO);
        sqlMap.executeUpdate("deleteSalaryBranch", objEmpSalaryStructureTO);
        sqlMap.executeUpdate("deleteSalaryGrade", objEmpSalaryStructureTO);
        sqlMap.executeUpdate("deleteSalaryPopulation", objEmpSalaryStructureTO);
        if (zonalValueList != null && zonalValueList.size() > 0) {
            for (int i = 0; i < zonalValueList.size(); i++) {
                HashMap whereMap = new HashMap();
                whereMap.put("ALLOWANCE_ID", objEmpSalaryStructureTO.getAllowanceId());
                whereMap.put("ZONAL_ID", zonalValueList.get(i));
                whereMap.put("STATUS", objEmpSalaryStructureTO.getStatus());
                System.out.println("#$%#$%$#%#$%whereMap" + whereMap);
                sqlMap.executeUpdate("insertSalaryZonal", whereMap);
                if (branchValueList != null && branchValueList.size() > 0) {
                    for (int j = 0; j < branchValueList.size(); j++) {
                        whereMap.put("BRANCH_ID", branchValueList.get(j));
                        sqlMap.executeUpdate("insertSalaryBranch", whereMap);
                        if (gradeValueList != null && gradeValueList.size() > 0) {
                            doInsert("insertSalaryGrade", "GRADE_ID", gradeValueList, whereMap);
                        }
                        if (populationValueList != null && populationValueList.size() > 0) {
                            doInsert("insertSalaryPopulationType", "POPULATION_TYPE", populationValueList, whereMap);
                        }
                    }
                }
            }
        }
        System.out.println("@#@!#@Insdie the update" + objEmpSalaryStructureTO);
        makeDataNull();
        makeNull();
    }

    public void insertData() throws Exception {
        if (empSalaryDetilsMap != null) {
            getAllData();
            //          insert into SALARY_ALLOWANCE_TYPE
            EmpSalaryStructureTO objEmpSalaryStructureTO = (EmpSalaryStructureTO) data.get("SALARY_ALLOWANCE_DETAILS");
            sqlMap.executeUpdate("insertEmpSalaryStructAllowance", objEmpSalaryStructureTO);
            System.out.println("#%#$%#$%$#objEmpSalaryStructureTO :" + objEmpSalaryStructureTO);
            processSalaryData(CommonUtil.convertObjToStr(objEmpSalaryStructureTO.getCommand()));
            if (zonalValueList != null && zonalValueList.size() > 0) {
                for (int i = 0; i < zonalValueList.size(); i++) {
                    HashMap whereMap = new HashMap();
                    whereMap.put("ALLOWANCE_ID", objEmpSalaryStructureTO.getAllowanceId());
                    whereMap.put("ZONAL_ID", zonalValueList.get(i));
                    whereMap.put("STATUS", objEmpSalaryStructureTO.getStatus());
                    System.out.println("#$%#$%$#%#$%whereMap" + whereMap);
                    sqlMap.executeUpdate("insertSalaryZonal", whereMap);
                    if (branchValueList != null && branchValueList.size() > 0) {
                        for (int j = 0; j < branchValueList.size(); j++) {
                            whereMap.put("BRANCH_ID", branchValueList.get(j));
                            sqlMap.executeUpdate("insertSalaryBranch", whereMap);
                            if (gradeValueList != null && gradeValueList.size() > 0) {
                                doInsert("insertSalaryGrade", "GRADE_ID", gradeValueList, whereMap);
                            }
                            if (populationValueList != null && populationValueList.size() > 0) {
                                doInsert("insertSalaryPopulationType", "POPULATION_TYPE", populationValueList, whereMap);
                            }
                        }
                    }
                }
            }
        }
        makeDataNull();
        makeNull();
    }

    private void doInsert(String mapName, String tableType, ArrayList valueList, HashMap whereMap) throws Exception {

        for (int m = 0; m < valueList.size(); m++) {
            whereMap.put(tableType, valueList.get(m));
            sqlMap.executeUpdate(mapName, whereMap);
        }
    }

    private void destroyObjects() {
        branchValueList = new ArrayList();
        gradeValueList = new ArrayList();
        populationValueList = new ArrayList();
        zonalValueList = new ArrayList();
        resultMap = null;
        data = null;
    }
}
