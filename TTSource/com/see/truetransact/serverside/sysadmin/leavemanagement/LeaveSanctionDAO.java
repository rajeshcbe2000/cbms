/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LeaveManagementDAO.java
 *
 * Created on Thu Jan 20 17:19:05 IST 2005
 */
package com.see.truetransact.serverside.sysadmin.leavemanagement;

import java.util.List;
import java.util.ArrayList;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;

import com.see.truetransact.transferobject.sysadmin.leavemanagement.LeaveSanctionTO;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Date;

/**
 * TokenConfig DAO.
 *
 */
public class LeaveSanctionDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LeaveSanctionTO objTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private LinkedHashMap tableDetails = null;
    private String processType = "";
    private HashMap encashmentMap = null;
    double proRateValue = 0;
    double leavesGranted = 0;
    private boolean entered = false;
    private LinkedHashMap deletedTableValues = null;
    private Date currDt = null;
    /**
     * Creates a new instance of TokenConfigDAO
     */
    public LeaveSanctionDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        if (map.containsKey("APPLICATION")) {
            List list = (List) sqlMap.executeQueryForList("getSelectLeaveApplTO", map);
            returnMap.put("LeaveSanctionApplicationTO", list);
            list = (List) sqlMap.executeQueryForList("getSelectLeaveApplTable", map);
            if (list != null && list.size() > 0) {
                LinkedHashMap ParMap = new LinkedHashMap();
                System.out.println("@@@list" + list);
                for (int i = list.size(), j = 0; i > 0; i--, j++) {
                    String st = ((LeaveSanctionTO) list.get(j)).getSlNo();
                    ParMap.put(((LeaveSanctionTO) list.get(j)).getSlNo(), list.get(j));
                }
                System.out.println("@@@ParMap" + ParMap);
                returnMap.put("APP_TABLE", ParMap);
            }
        }
        if (map.containsKey("SANCTION")) {
            List list = (List) sqlMap.executeQueryForList("getSelectLeaveSanTO", map);
            if (list != null && list.size() > 0) {
                returnMap.put("LeaveSanctionTO", list);
            }
            list = (List) sqlMap.executeQueryForList("getSelectLeaveSanTable", map);
            if (list != null && list.size() > 0) {
                LinkedHashMap ParMap = new LinkedHashMap();
                System.out.println("@@@list" + list);
                for (int i = list.size(), j = 0; i > 0; i--, j++) {
                    String st = ((LeaveSanctionTO) list.get(j)).getSlNo();
                    ParMap.put(((LeaveSanctionTO) list.get(j)).getSlNo(), list.get(j));
                }
                System.out.println("@@@ParMap" + ParMap);
                returnMap.put("SAN_TABLE", ParMap);
            }
        }
        if (map.containsKey("MODIFICATION_EDIT")) {
            List list = (List) sqlMap.executeQueryForList("getSelectLeaveSanTable", map);
            if (list != null && list.size() > 0) {
                LinkedHashMap ParMap = new LinkedHashMap();
                System.out.println("@@@list" + list);
                for (int i = list.size(), j = 0; i > 0; i--, j++) {
                    String st = ((LeaveSanctionTO) list.get(j)).getSlNo();
                    ParMap.put(((LeaveSanctionTO) list.get(j)).getSlNo(), list.get(j));
                }
                System.out.println("@@@ParMap" + ParMap);
                returnMap.put("SAN_TABLE", ParMap);
            }
        }

        return returnMap;
    }

    private String getLeaveId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "LEAVE_APPLN_SAN_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void insertData(String command) throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            logTO.setData(objTO.toString());
            logTO.setStatus(objTO.getCommand());
            String appl_dt = objTO.getApplSan();
            System.out.println("@@@APPL_SAN" + appl_dt);
            if (objTO.getApplSan().equalsIgnoreCase("APPLICATION")) {
                objTO.setleaveApplID(getLeaveId());
                sqlMap.executeUpdate("insertLeaveAppl", objTO);
                insertTableValues(command);
            } else {
                List lst = sqlMap.executeQueryForList("countInsertSan", objTO);
                int a = CommonUtil.convertObjToInt(lst.get(0));
                if (a <= 0) {
                    sqlMap.executeUpdate("insertLeaveSan", objTO);
                } else {
                    sqlMap.executeUpdate("updateLeaveSan", objTO);
                }
                insertTableValues(command);
            }
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateData(String command) throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            logTO.setData(objTO.toString());
            logTO.setStatus(objTO.getCommand());
            if (objTO.getApplSan().equalsIgnoreCase("APPLICATION")) {
                sqlMap.executeUpdate("updateLeaveAppl", objTO);
                updateTableValues(command);
            } else {
                sqlMap.executeUpdate("updateLeaveSan", objTO);
                updateTableValues(command);
            }
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData(String command) throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_DELETED);
            logTO.setData(objTO.toString());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("deleteLeaveApplMainDelete", objTO);
            sqlMap.executeUpdate("deleteLeaveSan", objTO);
            sqlMap.executeUpdate("deleteLeaveApplMain", objTO);
            sqlMap.executeUpdate("deleteLeaveSanMain", objTO);
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            LeaveSanctionDAO dao = new LeaveSanctionDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("@@@@@@@mapINEXECUTE" + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        HashMap returnMap = null;
        returnMap = new HashMap();
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
        processType = CommonUtil.convertObjToStr(map.get("PROCESS_TYPE"));

        deletedTableValues = (LinkedHashMap) map.get("deletedTableDetails");
        tableDetails = (LinkedHashMap) map.get("TableDetails");
        if (map.containsKey("ENCASHMENT_DETAILS")) {
            encashmentMap = (HashMap) map.get("ENCASHMENT_DETAILS");
            encashmentMap.put("BRANCH_CODE", map.get(CommonConstants.BRANCH_ID));
        }
        if (map.containsKey("LeaveSanctionTO")) {
            objTO = (LeaveSanctionTO) map.get("LeaveSanctionTO");
            final String command = objTO.getCommand();
            System.out.println("@@@command" + command);
            logDAO = new LogDAO();
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData(command);
                returnMap = new HashMap();
                returnMap.put("LEAVE_ID", objTO.getleaveApplID());
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData(command);
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData(command);
            } else {
                throw new NoCommandException();
            }

            destroyObjects();
        }
        if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap, CommonUtil.convertObjToStr(map.get("EMP_ID")));
            }
        }
        if (map.containsKey("PROCESS_EXECUTION")) {
            processExecution(CommonUtil.convertObjToStr(map.get("LEAVE_TYPE")), CommonUtil.convertObjToStr(map.get("EMP_ID")));
        }
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void authorize(HashMap map, String emp_id) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        System.out.println("@@@@@@@AuthMap" + AuthMap);
        try {
            sqlMap.startTransaction();
            logTO.setData(map.toString());
            sqlMap.executeUpdate("authorizeLeaveAppl", AuthMap);
            sqlMap.executeUpdate("authorizeLeaveApplTab", AuthMap);
            sqlMap.executeUpdate("authorizeSanAppl", AuthMap);
            sqlMap.executeUpdate("authorizeLeaveSanTab", AuthMap);
            if (AuthMap.containsKey("SANCTION") && (!AuthMap.containsKey("SAN_STATUS_REJECT"))) {
                if (tableDetails != null) {
                    ArrayList addList = new ArrayList(tableDetails.keySet());
                    for (int i = 0; i < addList.size(); i++) {
                        LeaveSanctionTO objAuthTO = (LeaveSanctionTO) tableDetails.get(addList.get(i));
                        objAuthTO.setEmpID(emp_id);
                        objAuthTO.setBranch(logTO.getBranchId());
                        objAuthTO.setStatusDt(currDt);
                        objAuthTO.setSanNo(CommonUtil.convertObjToStr(AuthMap.get("SANCTION_NO")));
                        objAuthTO.setOldSanNo(CommonUtil.convertObjToStr(AuthMap.get("OLD_SANCTION_NO")));

                        // check for payment type
                        HashMap payMap = new HashMap();
                        String paytype = "";
                        payMap.put("LEAVE_TYPE", CommonUtil.convertObjToStr(objAuthTO.getTabLeaveType()));
                        List payList = ServerUtil.executeQuery("getPaymentType", payMap);
                        if (payList != null && payList.size() > 0) {
                            payMap = (HashMap) payList.get(0);
                            paytype = CommonUtil.convertObjToStr(payMap.get("PAYMENT_TYPE"));
                        }

                        if (processType.equalsIgnoreCase("SANCTION") || processType.equalsIgnoreCase("EXTENSION")) {
                            if (!objAuthTO.getTabLeaveType().equalsIgnoreCase("LOSS OF PAY")) {
                                objAuthTO.setTransType("DEBIT");
                                // to double the number of days based on leave type
                                if (paytype.equalsIgnoreCase("HALF_PAY") && CommonUtil.convertObjToStr(objAuthTO.getPaymentType()).equalsIgnoreCase("FULL_PAY")) {
                                    int days = (CommonUtil.convertObjToInt(objAuthTO.getTableNoOfdays())) * 2;
                                    objAuthTO.setTableNoOfdays(String.valueOf(days));
                                }

                                sqlMap.executeUpdate("updateEmpLeaveAfterAuth", objAuthTO);
                            }
                            if (objAuthTO.getTabLeaveType().equalsIgnoreCase("LOSS OF PAY")) {
                                List lst = sqlMap.executeQueryForList("countLossOfPay", objAuthTO);
                                int cnt = CommonUtil.convertObjToInt(lst.get(0));
                                if (cnt <= 0) {
                                    sqlMap.executeUpdate("insertEmpLeaveWhileAuth", objAuthTO);
                                } else {
                                    sqlMap.executeUpdate("updateEmpLeaveAfterAuthModification", objAuthTO);
                                }
                            }
                        } else {
                            if (!objAuthTO.getTabLeaveType().equalsIgnoreCase("LOSS OF PAY")) {
                                objAuthTO.setTransType("CREDIT");
                                if (AuthMap.containsKey("LEAVE_CANCEL")) {
                                    if (paytype.equalsIgnoreCase("HALF_PAY") && CommonUtil.convertObjToStr(objAuthTO.getPaymentType()).equalsIgnoreCase("FULL_PAY")) {
                                        int days = (CommonUtil.convertObjToInt(objAuthTO.getTableNoOfdays())) * 2;
                                        objAuthTO.setTableNoOfdays(String.valueOf(days));
                                    }
                                    sqlMap.executeUpdate("updateEmpLeaveAfterAuthModification", objAuthTO);
                                } else {
                                    if (!entered) {
                                        List getprevDays = sqlMap.executeQueryForList("getPreviousNumberOfDays", AuthMap);
                                        if (getprevDays != null & getprevDays.size() > 0) {
                                            HashMap prevMap = new HashMap();
                                            for (int k = 0; k < getprevDays.size(); k++) {
                                                prevMap = (HashMap) getprevDays.get(k);
                                                System.out.println("insidePrevBalance" + prevMap);
                                                prevMap.put("EMP_ID", objAuthTO.getEmpID());
                                                sqlMap.executeUpdate("updateEmpLeaveForPartialModification", prevMap);
                                            }
                                            entered = true;
                                        }
                                        sqlMap.executeUpdate("updateEmpLeaveAfterAuth", objAuthTO);
                                    }
                                }
                            }

                            if (objAuthTO.getTabLeaveType().equalsIgnoreCase("LOSS OF PAY")) {
                                if (AuthMap.containsKey("LEAVE_CANCEL")) {
                                    sqlMap.executeUpdate("updateEmpLeaveAfterAuth", objAuthTO);
                                } else {
                                    List getprevDays = sqlMap.executeQueryForList("getPreviousNumberOfDays", AuthMap);
                                    if (getprevDays != null & getprevDays.size() > 0) {
                                        HashMap prevMap = new HashMap();
                                        for (int k = 0; k < getprevDays.size(); k++) {
                                            prevMap = (HashMap) getprevDays.get(k);
                                            System.out.println("insidePrevBalance" + prevMap);
                                            prevMap.put("EMP_ID", objAuthTO.getEmpID());
                                            sqlMap.executeUpdate("updateEmpLeavePartialModLoss", prevMap);
                                        }
                                        sqlMap.executeUpdate("updateEmpLeaveAfterAuthModification", objAuthTO);
                                    }
                                }
                            }
                        }
                        sqlMap.executeUpdate("insertEmpLeaveTranferWhileAuth", objAuthTO);
                    }
                }
                // code for leaveEncashment (PL)
                if (encashmentMap != null) {
                    encashmentMap.put("LEAVE_TYPE", encashmentMap.get("ENCASHMENT_LEAVE_TYPE"));
                    encashmentMap.put("EMP_ID", emp_id);
                    encashmentMap.put("TRANS_TYPE", "ENCASHMENT");
                    encashmentMap.put("TRANS_DATE", currDt.clone());
                    encashmentMap.put("SANCTION_NO", CommonUtil.convertObjToStr(AuthMap.get("SANCTION_NO")));
                    if (processType.equalsIgnoreCase("MODIFICATION")) {
                        if (AuthMap.containsKey("LEAVE_CANCEL")) {
                            sqlMap.executeUpdate("AddLeaveEncashment", encashmentMap);
                        } else {
                            List getprevDays = sqlMap.executeQueryForList("getPreviousNumberOfDaysForEncashMent", AuthMap);
                            if (getprevDays != null & getprevDays.size() > 0) {
                                HashMap prevMap = new HashMap();
                                for (int k = 0; k < getprevDays.size(); k++) {
                                    prevMap = (HashMap) getprevDays.get(k);
                                    System.out.println("insidePrevBalanceEncash" + prevMap);
                                    prevMap.put("EMP_ID", emp_id);
                                    double noOfDayAddition = CommonUtil.convertObjToDouble(prevMap.get("NO_OF_DAYS")).doubleValue();
                                    prevMap.put("NO_OF_DAYS_ADDITION", new Double(noOfDayAddition));
                                    sqlMap.executeUpdate("AddLeaveEncashment", prevMap);
                                }
                                sqlMap.executeUpdate("leaveEncashment", encashmentMap);
                            }

                        }

                    } else {
                        sqlMap.executeUpdate("leaveEncashment", encashmentMap);
                    }
                    sqlMap.executeUpdate("insertEmpLeaveTranfer", encashmentMap);
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void destroyObjects() {
        objTO = null;
        tableDetails = null;
        processType = "";
        proRateValue = 0;
        entered = false;
    }

    private void insertTableValues(String command) throws Exception {

        if (tableDetails != null) {
            ArrayList addList = new ArrayList(tableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                LeaveSanctionTO objDisTO = (LeaveSanctionTO) tableDetails.get(addList.get(i));
                //               objDisTO.setDisStatus(CommonConstants.STATUS_CREATED);
                objDisTO.setleaveApplID(objTO.getleaveApplID());
                if (objTO.getApplSan().equalsIgnoreCase("APPLICATION")) {
                    if (objDisTO.getAppTblStatus().length() <= 0) {
                        objDisTO.setAppTblStatus("CREATED");
                    }
                    List lst = sqlMap.executeQueryForList("countAppl", objDisTO);
                    int a = CommonUtil.convertObjToInt(lst.get(0));
                    if (a <= 0) {
                        sqlMap.executeUpdate("insertApplTableValues", objDisTO);
                    } else {
                        sqlMap.executeUpdate("updateApplTableValues", objDisTO);
                    }
                } else if (objTO.getApplSan().equalsIgnoreCase("SANCTION")) {
                    objDisTO.setAuthorizedStatus("");
                    objDisTO.setAuthorizedBy("");
                    objDisTO.setSanCreatedBy(objTO.getSanCreatedBy());
                    objDisTO.setSanStatusDt(objTO.getSanCreatedDt());
                    objDisTO.setSanStatusBy(objTO.getSanStatusBy());
                    objDisTO.setSanCreatedDt(objTO.getSanCreatedDt());
                    if (objDisTO.getSanTblStatus().length() <= 0) {
                        objDisTO.setSanTblStatus("CREATED");
                    }
                    List lst = sqlMap.executeQueryForList("countSan", objDisTO);
                    int a = CommonUtil.convertObjToInt(lst.get(0));
                    if (a <= 0) {
                        sqlMap.executeUpdate("insertSanTableValues", objDisTO);
                    } else {
                        sqlMap.executeUpdate("updateSanTableValues", objDisTO);
                    }
                }
                logTO.setData(objDisTO.toString());
                logTO.setPrimaryKey(objDisTO.getKeyData());
                logTO.setStatus(objDisTO.getCommand());
                logDAO.addToLog(logTO);
                objDisTO = null;
            }
        }
    }

    private void updateTableValues(String command) throws Exception {

        if (deletedTableValues != null) {
            ArrayList addList = new ArrayList(deletedTableValues.keySet());
            for (int i = 0; i < addList.size(); i++) {
                LeaveSanctionTO objLeaveSanctionTO = (LeaveSanctionTO) deletedTableValues.get(addList.get(i));
                if (objTO.getApplSan().equalsIgnoreCase("APPLICATION")) {
                    objLeaveSanctionTO.setAppTblStatus(CommonConstants.STATUS_DELETED);
                    sqlMap.executeUpdate("deleteLeaveAppl", objLeaveSanctionTO);
                } else {
                    objLeaveSanctionTO.setSanTblStatus(CommonConstants.STATUS_DELETED);
                    sqlMap.executeUpdate("deleteLeaveApplTableValues", objLeaveSanctionTO);
                }

                logTO.setData(objLeaveSanctionTO.toString());
                logTO.setPrimaryKey(objLeaveSanctionTO.getKeyData());
                logTO.setStatus(command);
                logDAO.addToLog(logTO);
                objLeaveSanctionTO = null;
            }
        }

        if (tableDetails != null) {
            List lst = null;
            ArrayList addList = new ArrayList(tableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                LeaveSanctionTO objDisTO = (LeaveSanctionTO) tableDetails.get(addList.get(i));
                logTO.setData(objDisTO.toString());
                logTO.setPrimaryKey(objDisTO.getKeyData());
                logTO.setStatus(command);
                objDisTO.setleaveApplID(objTO.getleaveApplID());
                if (objTO.getApplSan().equalsIgnoreCase("APPLICATION")) {
                    lst = sqlMap.executeQueryForList("countAppl", objDisTO);
                    int a = CommonUtil.convertObjToInt(lst.get(0));
                    if (a <= 0) {
                        sqlMap.executeUpdate("insertApplTableValues", objDisTO);
                    } else {
                        sqlMap.executeUpdate("updateApplTableValues", objDisTO);
                    }
                } else if (objTO.getApplSan().equalsIgnoreCase("SANCTION")) {
                    objDisTO.setSanStatusDt(objTO.getSanCreatedDt());
                    objDisTO.setSanStatusBy(objTO.getSanStatusBy());
                    lst = sqlMap.executeQueryForList("countSan", objDisTO);
                    int a = CommonUtil.convertObjToInt(lst.get(0));
                    System.out.println("@@@coount" + a);
                    if (a <= 0) {
                        sqlMap.executeUpdate("insertSanTableValues", objDisTO);
                    } else {
                        sqlMap.executeUpdate("updateSanTableValues", objDisTO);
                    }
                }

                logDAO.addToLog(logTO);
            }
        }
    }

    public void processExecution(String leaveType, String empID) throws Exception {
        try {
            sqlMap.startTransaction();
            List noOfEmp = new ArrayList();
            HashMap parMap = new HashMap();
            parMap.put("LEAVE_TYPE", leaveType);
            Date currDate = (Date) currDt.clone();
            //            to get the parameters for the particular Leave Type.
            List parList = sqlMap.executeQueryForList("getLeaveParameters", parMap);
            if (parList != null && parList.size() > 0) {
                parMap = null;
                parMap = (HashMap) parList.get(0);
                System.out.println("$##$%#$%#$%parMap:" + parMap);
                if (empID.length() > 0) {
                    parMap.put("EMP_ID", empID);
                    //                    Gets the last leave credited date of the employee along with the next date of credit
                    List lastCrDate = sqlMap.executeQueryForList("getLastCreditedDate", parMap);
                    System.out.println("%#$%#%$#%$lastCrDate:" + lastCrDate);
                    if (!parMap.get("CREDIT_TYPE").equals("ANY_TIME")) {
                        if (lastCrDate != null && lastCrDate.size() > 0) {
                            Date runningDate1 = null;
                            HashMap crDate = (HashMap) lastCrDate.get(0);
                            Date creditDate = (Date) crDate.get("LAST_CREDIT_DATE");
                            if (parMap.get("LEAVE_CREDITING_PARAMETERS").equals("FIXED")) {
                                if (DateUtil.dateDiff(creditDate, currDate) >= 365) {
                                    noOfEmp.add(empID);
                                }
                            } else {
                                noOfEmp.add(empID);
                            }
                            System.out.println("!@#!@#!@#@#noOfEmp:" + noOfEmp);
                        } else {
                            Date runningDate = null;
                            if (!(parMap.get("CREDIT_TYPE").equals("DATE_OF_JOINING"))) {
                                runningDate = (Date) parMap.get("DATE_OF_CREDITING");
                                System.out.println("$@#$@#$@#$runningDate :" + runningDate);
                            } else {
                                List doj = sqlMap.executeQueryForList("getJoinAndRetAge", parMap);
                                if (doj != null && doj.size() > 0) {
                                    HashMap DateOfJoining = new HashMap();
                                    DateOfJoining = (HashMap) doj.get(0);
                                    runningDate = (Date) DateOfJoining.get("DATE_OF_JOIN");
                                    Date retAge = (Date) DateOfJoining.get("DATE_OF_RETIREMENT");
                                }
                            }
                            if ((currDate.getDate() >= runningDate.getDate()) && (currDate.getMonth() >= runningDate.getMonth())) {
                                noOfEmp.add(empID);
                                System.out.println("#@$@#$@#$@#$noOfEmp" + noOfEmp);
                            }

                        }
                    } else {
                        System.out.println("#@$@#$@ inside maternity leave:");
                        noOfEmp.add(empID);
//                        changed here by nikhil for maternity leave enabling
                        System.out.println("#@$@#$@#$@#$noOfEmp" + noOfEmp);
                    }

                } else {
                    HashMap noOfEmpMap = new HashMap();
                    noOfEmpMap.put("BRANCH_CODE", _branchCode);
                    //                    if a single Employee is not entered while process then all the meployees are calculated
                    noOfEmp = sqlMap.executeQueryForList("getAllEmp", noOfEmpMap);

                }
                for (int i = 0; i < noOfEmp.size(); i++) {
                    if (empID.length() <= 0) {
                        HashMap balList = ((HashMap) noOfEmp.get(i));
                        parMap.put("EMP_ID", balList.get("EMPLOYEE_CODE"));
                    }
                    double leavesCredited = gettingNumerOfDays(parMap);
                    System.out.println("@#$@#$@#$leavesCredited:" + leavesCredited);
                    if (parMap.get("LEAVE_CREDITING_PARAMETERS").equals("FIXED")) {
                        List lastCrDate = sqlMap.executeQueryForList("getLastCreditedDate", parMap);
                        if (lastCrDate != null && lastCrDate.size() > 0) {
                            HashMap map = new HashMap();
                            map = (HashMap) lastCrDate.get(0);
                            Date crDate = (Date) map.get("LAST_CREDIT_DATE");
                            if (DateUtil.dateDiff(crDate, currDate) < 365) {
                                continue;
                            }
                        }
                        if (parMap.get("LEAVE_LAPSES").equals("N")) {
                            //                            if leave lapses are not there then the carryover is added to the
                            double leavesForParticularYr = (CommonUtil.convertObjToDouble(parMap.get("CARRY_OVER")).doubleValue()) * leavesCredited;
                            //                            gets the remaining leaves for the employee for that particular leave type
                            List lapsList = sqlMap.executeQueryForList("getRemainingLeaves", parMap);
                            if (lapsList != null && lapsList.size() > 0) {
                                double lapsedLeaves = (CommonUtil.convertObjToDouble(lapsList.get(0)).doubleValue()) + leavesCredited;
                                if (lapsedLeaves > leavesForParticularYr) {
                                    lapsedLeaves = leavesForParticularYr;
                                }
                                parMap.put("NO_OF_DAYS_ADDITION", new Double(lapsedLeaves));
                            } else {
                                parMap.put("NO_OF_DAYS_ADDITION", new Double(leavesCredited));
                            }
                        } else if (parMap.get("LEAVE_LAPSES").equals("Y")) {
                            parMap.put("NO_OF_DAYS_ADDITION", new Double(leavesCredited));
                        }
                    }
                    if (parMap.get("LEAVE_CREDITING_PARAMETERS").equals("PRORATE") && parMap.get("ACCUMALTION_ALLOWED").equals("Y")) {
                        double accDays = getAccumalatedDays(parMap);
                        System.out.println("leavesCredited@@@@" + leavesCredited);
                        System.out.println("accDays$$$$$" + accDays);
                        if (proRateValue > accDays) {
                            leavesCredited = (leavesCredited - (proRateValue - accDays));
                        }
                        List lapsList = sqlMap.executeQueryForList("getRemainingLeaves", parMap);
                        System.out.println("leavesCredited@@@@" + leavesCredited);
                        if (lapsList != null && lapsList.size() > 0) {
                            double remainingLeaves = CommonUtil.convertObjToDouble(lapsList.get(0)).doubleValue();
                            if ((remainingLeaves + leavesCredited) > accDays) {
                                parMap.put("NO_OF_DAYS_ADDITION", new Double(accDays));
                            } else {
                                parMap.put("NO_OF_DAYS_ADDITION", new Double(remainingLeaves + leavesCredited));
                            }
                        } else {
                            parMap.put("NO_OF_DAYS_ADDITION", new Double(leavesCredited));
                        }

                    } else if (parMap.get("LEAVE_CREDITING_PARAMETERS").equals("PRORATE") && parMap.get("ACCUMALTION_ALLOWED").equals("N")) {
                        List lapsList = sqlMap.executeQueryForList("getRemainingLeaves", parMap);
                        if (lapsList != null && lapsList.size() > 0) {
                            double remainingLeaves = CommonUtil.convertObjToDouble(lapsList.get(0)).doubleValue();
                            leavesCredited = leavesCredited + remainingLeaves;
                            parMap.put("NO_OF_DAYS_ADDITION", new Double(leavesCredited));
                        } else {
                            parMap.put("NO_OF_DAYS_ADDITION", new Double(leavesCredited));
                        }
                    } else if (parMap.get("LEAVE_CREDITING_PARAMETERS").equals("OTHERS") && parMap.get("LEAVE_TYPE").equals("ML")) {
                        List lapsList = sqlMap.executeQueryForList("getRemainingLeaves", parMap);
                        if (lapsList != null && lapsList.size() > 0) {
                            double remainingLeaves = CommonUtil.convertObjToDouble(lapsList.get(0)).doubleValue();
                            leavesCredited = leavesCredited + remainingLeaves;
                            parMap.put("NO_OF_DAYS_ADDITION", new Double(leavesCredited));
                        } else {
                            parMap.put("NO_OF_DAYS_ADDITION", new Double(leavesCredited));
                        }
                    }
                    parMap.put("TRANS_TYPE", "CREDIT");
                    parMap.put("TRANS_DATE", currDate);
                    parMap.put("LAST_CREDITED_DATE", currDate);
                    if (noOfEmp.size() > 1) {
                        parMap.put("NEXT_CREDIT_DATE", DateUtil.addDays(currDate, 365));
                    } else {
                        List crDate = sqlMap.executeQueryForList("getLastCreditedDate", parMap);
                        if (crDate != null && crDate.size() > 0) {
                            HashMap map = new HashMap();
                            map = (HashMap) crDate.get(0);
                            Date nextDt = (Date) map.get("NEXT_CREDIT_DATE");
                            parMap.put("NEXT_CREDIT_DATE", nextDt);
                        } else {
                            parMap.put("NEXT_CREDIT_DATE", DateUtil.addDays(currDate, 365));
                        }
                    }
                    parMap.put("LEAVES_CREDITED_PRORATE", new Double(proRateValue));
                    parMap.put("SANCTION_NO", "");
                    System.out.println("#$%#$%#$%parMap" + parMap);
                    List count = sqlMap.executeQueryForList("countEmpID", parMap);
                    int a = CommonUtil.convertObjToInt(count.get(0));
                    if (a <= 0) {
                        sqlMap.executeUpdate("insertEmpLeave", parMap);
                    } else {
                        sqlMap.executeUpdate("updateEmpLeave", parMap);
                    }
                    System.out.println("parMap@@@@" + parMap);
                    parMap.put("NO_OF_DAYS_ADDITION", new Double(leavesGranted));
                    sqlMap.executeUpdate("insertEmpLeaveTranfer", parMap);
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public double gettingNumerOfDays(HashMap parMap) throws Exception {
        System.out.println("PARMAP$$$$$" + parMap);
        double totalDays = 0;
        Date currDate = (Date) currDt.clone();
        Date retAge = null;
        String creditType = CommonUtil.convertObjToStr(parMap.get("LEAVE_CREDITING_PARAMETERS"));
        System.out.println("creditTypeInsideGETDAYS" + creditType);
        if (creditType.equalsIgnoreCase("FIXED")) {
            int key = 0;
            String period = CommonUtil.convertObjToStr(parMap.get("FIXED_PARAM_TYPE"));
            double keyType = CommonUtil.convertObjToDouble(parMap.get("FIXED_PARAMETER1")).doubleValue();
            double days = CommonUtil.convertObjToDouble(parMap.get("FIXED_PARAMETER2")).doubleValue();
            List doj = sqlMap.executeQueryForList("getJoinAndRetAge", parMap);
            if (doj != null && doj.size() > 0) {
                HashMap DateOfRet = new HashMap();
                DateOfRet = (HashMap) doj.get(0);
                retAge = (Date) DateOfRet.get("DATE_OF_RETIREMENT");
            }

            if (period.equalsIgnoreCase("DAYS")) {
                if (retAge != null && retAge.after(currDate) && retAge.before(DateUtil.addDays(currDate, 365))) {
                    key = (int) DateUtil.dateDiff(currDate, retAge);
                } else {
                    key = 365;
                }
                totalDays = Math.floor((key / keyType) * days);
                System.out.println("@#$@#$@#$totalDays:" + totalDays);
            } else if (period.equalsIgnoreCase("MONTHS")) {
                if (retAge != null && retAge.after(currDate) && retAge.before(DateUtil.addDays(currDate, 365))) {
                    key = (int) DateUtil.dateDiff(currDate, retAge);
                    totalDays = Math.floor((key / (30 * keyType)) * days);
                } else {
                    key = 12;
                    totalDays = Math.floor((key / keyType) * days);
                    System.out.println("totalDaysInsoideMONTHS" + totalDays);
                }
            } else if (period.equalsIgnoreCase("YEAR")) {
                if (retAge != null && retAge.after(currDate) && retAge.before(DateUtil.addDays(currDate, 365))) {
                    key = (int) DateUtil.dateDiff(currDate, retAge);
                    totalDays = Math.floor((key / (30 * keyType)));
                } else {
                    key = 1;
                    totalDays = Math.floor((key / keyType) * days);
                    System.out.println("totalDaysInsoideYEAR" + totalDays);
                }
            }

            leavesGranted = totalDays;
        } else if (creditType.equalsIgnoreCase("PRORATE")) {
            Date creditDate = null;
            double daysWorked = CommonUtil.convertObjToDouble(parMap.get("PRORATE_TYPE2")).doubleValue();
            double leavesGot = CommonUtil.convertObjToDouble(parMap.get("PRORATE_TYPE1")).doubleValue();
            System.out.println("#@$#@#@$$leavesGot: " + leavesGot + "#@$@#$@4daysWorked:" + daysWorked);
            List lastCrDate = sqlMap.executeQueryForList("getLastCreditedDate", parMap);
            System.out.println("#@$@#$@#$@#$lastCrDate" + lastCrDate);
            if (lastCrDate != null && lastCrDate.size() > 0) {
                HashMap crDate = (HashMap) lastCrDate.get(0);

                creditDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(crDate.get("NEXT_CREDIT_DATE")));
                long d = DateUtil.dateDiff(DateUtil.addDays(creditDate, -365), currDt);
                System.out.println("DATEVALUE###" + DateUtil.addDays(creditDate, -365));
                totalDays = Math.floor(((d + 1) * daysWorked) / leavesGot);
                System.out.println("totalDAysBEFOREr###" + totalDays);
                proRateValue = totalDays;
                totalDays = totalDays - CommonUtil.convertObjToDouble(crDate.get("LEAVES_CREDITED_PRORATE")).doubleValue();
                System.out.println("totalDAysAfter###" + totalDays);
            } else {
                Date confirmDate = null;
                Date joiningDate = null;
                List doj = sqlMap.executeQueryForList("getJoinAndRetAge", parMap);
                if (doj != null && doj.size() > 0) {
                    HashMap DateOfJoining = new HashMap();
                    DateOfJoining = (HashMap) doj.get(0);
                    joiningDate = (Date) DateOfJoining.get("DATE_OF_JOIN");
                    confirmDate = (Date) DateOfJoining.get("CONFIRMATION_DATE");
                    retAge = (Date) DateOfJoining.get("DATE_OF_RETIREMENT");

                    System.out.println("@#$@#$@#$confirmDate:" + confirmDate);
//                    if the date of confirmation is in between the last credited year and current date
//                    then we will have to find out the no of days worked by the employees
                    if (DateUtil.dateDiff(confirmDate, currDate) >= 365) {
//                        currently done using confirmation date but can be replaced with the joining date
                        totalDays = Math.floor((365 * daysWorked) / leavesGot);
                        System.out.println("$@#%@#$%#$%totalDays:" + totalDays);
                    } else {
                        System.out.println("@#$@#$@aSASD#currDate:" + currDate);
                        Date prevYearDt = currDate;
                        prevYearDt.setYear(prevYearDt.getYear() - 1);
                        System.out.println("@#$@#$@#$prevYearDt:" + prevYearDt);
                        System.out.println("@#$@#$@#currDate1:" + currDate);
                        parMap.put("PREV_DT", prevYearDt);
                        currDate = (Date) currDt.clone();
                        System.out.println("@#$@#$@#$#%$#%currDate2:" + currDate);
                        parMap.put("CURRENT_DT", currDate);
                        System.out.println("@#$%#$%#parMap:" + parMap);
                        List lop = sqlMap.executeQueryForList("getLossOfPayForLeave", parMap);
                        System.out.println("@#$@#$@#$lop:" + lop);
                        HashMap lopMap = (HashMap) lop.get(0);
                        System.out.println("@#$%#$%#$lopMap:" + lopMap);
                        double noOfdaysWorked = (double) DateUtil.dateDiff(confirmDate, currDate);
                        System.out.println("no of days before lop:" + noOfdaysWorked);
                        if (lopMap.containsKey("TOTAL_LOP") && lopMap.size() > 0) {
                            double lopDays = CommonUtil.convertObjToDouble(lopMap.get("TOTAL_LOP")).doubleValue();
                            System.out.println("@#$@#$@#$lopDays:" + lopDays);
                            noOfdaysWorked = noOfdaysWorked - lopDays;
                            System.out.println("no of days after lop:" + noOfdaysWorked);
                        }
                        System.out.println("@#$@#@$@#noOfdaysWorked :" + noOfdaysWorked);
                        totalDays = Math.floor((noOfdaysWorked * daysWorked) / leavesGot);
                        System.out.println("@#$@#$@#$totalDays:" + totalDays);

                    }

                }

            }
            double maxLeavesCredited = CommonUtil.convertObjToDouble(parMap.get("MAX_LEAVES_CREDITED")).doubleValue();
            if (totalDays > maxLeavesCredited) {
                totalDays = maxLeavesCredited;
            }
            leavesGranted = totalDays;
        } else if (creditType.equalsIgnoreCase("OTHERS") && parMap.get("MATERNITY_LEAVE").equals("Y")) {
            System.out.println("@#$%#$%inside others totalDays:" + totalDays);
            List maternityCount = sqlMap.executeQueryForList("getCountOfMaternityLeave", parMap);
            if (maternityCount != null && maternityCount.size() > 0) {
                HashMap maternityMap = new HashMap();
                maternityMap = (HashMap) maternityCount.get(0);
                int matCount = CommonUtil.convertObjToInt(maternityMap.get("MATERNITY_LEAVE_COUNT"));
                totalDays = Math.floor(matCount * 90);
                System.out.println("#@$@#$@#$matCount:" + matCount + " : " + totalDays);

            }
        }

        return totalDays;
    }

//    private double calculateLossOfPay(HashMap lossOfPayMap){
//        System.out.println("#@$@#$@#$lossOfPayMap:"+lossOfPayMap);
//        List lop=sqlMap.executeQueryForList("getLossOfPay", lossOfPayMap);
//        return 0.0;
//    }
    public double getAccumalatedDays(HashMap parMap) {
        System.out.println("parMap%%%%" + parMap);
        double totalAccDays = 0;
        double key = 0;
        double accAllowed = CommonUtil.convertObjToDouble(parMap.get("ACCUMALTION_TIME")).doubleValue();
        String period = CommonUtil.convertObjToStr(parMap.get("ACCUMALTION_TYPE"));
        System.out.println("period%%%%" + period);
        System.out.println("accAllowed%%%%" + accAllowed);
        if (period.equalsIgnoreCase("DAYS")) {
            key = 1;
        } else if (period.equalsIgnoreCase("MONTHS")) {
            key = 30;
        } else if (period.equalsIgnoreCase("YEAR")) {
            key = 365;
        }
        totalAccDays = key * accAllowed;
        return totalAccDays;
    }
}
