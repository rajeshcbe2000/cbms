/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LoanEligibilityMaintenanceDAO.java
 *
 * Created on 31 January, 2013, 3:29 PM
 */
package com.see.truetransact.serverside.product.loan.loaneligibilitymaintenance;

import com.see.truetransact.serverside.TTDAO;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import java.util.Date;
import java.lang.String;
import java.rmi.RemoteException;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.AcctStatusConstants;

import com.see.truetransact.transferobject.operativeaccount.*;
import com.see.truetransact.transferobject.product.loan.loaneligibilitymaintenance.LoanEligibilityTO;

import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.businessrule.RuleContext;
import com.see.truetransact.businessrule.RuleEngine;
import com.see.truetransact.businessrule.operativeaccount.*;
//import com.see.truetransact.businessrule.ValidationRule;

//__ To Log the Data
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.clientutil.ClientConstants;

/**
 *
 * @author Admin
 */
public class LoanEligibilityMaintenanceDAO extends TTDAO {

    private SqlMap sqlMap;
    private LoanEligibilityTO loanEligibilityTO;
    private AccountTO accountTO;
    private ArrayList freezeList;
    private ArrayList freezeDeleteList;
    private String userID = "";
    private String advancesFreeze = "";
    Date currDate = null;
    HashMap resultMap = new HashMap();

    /**
     * Creates a new instance of LoanEligibilityMaintenanceDAO
     */
    public LoanEligibilityMaintenanceDAO() throws ServiceLocatorException {

        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private void insertData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            ArrayList lst = new ArrayList();
            int length = freezeList.size();
            for (int i = 0; i < length; i++) {
                loanEligibilityTO = (LoanEligibilityTO) freezeList.get(i);
//                String FREEZEID = getFreezeID();
//                loanEligibilityTO.setFreezeId(FREEZEID);
//                loanEligibilityTO.setCreatedBy(userID);
//                loanEligibilityTO.setCreatedDt(currDate);
                loanEligibilityTO.setStatusBy(userID);
                loanEligibilityTO.setStatusDate(currDate);
                loanEligibilityTO.setStatus(CommonConstants.STATUS_CREATED);

//                String ACTNO = CommonUtil.convertObjToStr(loanEligibilityTO.getActNum());
//                String AMOUNT = CommonUtil.convertObjToStr(loanEligibilityTO.getFreezeAmt());
//                checkRule(ACTNO, AMOUNT);

                sqlMap.executeUpdate("insertLoanEligibilityTO", loanEligibilityTO);
//                lst.add(FREEZEID);
                resultMap.put("FREEZE_NO", lst);
//                objLogTO.setData(loanEligibilityTO.toString());
//                objLogTO.setPrimaryKey(loanEligibilityTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
                //                sqlMap.executeUpdate("updateAccountMasterStatusId", accountTO);
            }
        } catch (Exception e) {
            if (e instanceof TTException) {
                TTException tte = (TTException) e;
                HashMap exceptionMap = tte.getExceptionHashMap();
                throw new TTException(exceptionMap);
            } else {
                // if something goes wrong, rollback the transaction
                sqlMap.rollbackTransaction();
                throw new TransRollbackException(e);
            }
        } finally {
            accountTO = null;
        }
    }

    //    private void checkRule(loanEligibilityTO loanEligibilityTO)throws Exception{
    private void checkRule(String actNum, String amount) throws Exception {
        RuleContext context = new RuleContext();
        RuleEngine engine = new RuleEngine();
        context.addRule(new LienApplicationRule());


        accountTO = new AccountTO();
        accountTO.setActNum(actNum);

        HashMap inputMap = new HashMap();
        inputMap.put("ACCOUNTTO", accountTO);
        inputMap.put("AMOUNT", amount);
        if (advancesFreeze.equals("ADVANCES")) {
            inputMap.put("ADVANCES", "ADVANCES");
            inputMap.put("BRANCH_CODE", _branchCode);
            inputMap.put("ACCT_NUM", actNum);
        }
        //        inputMap.put("Operation", "FREEZE");
        ArrayList list = (ArrayList) engine.validateAll(context, inputMap);
        if (list != null) {
            if (list.size() > 0) {
                HashMap exception = new HashMap();
                exception.put(CommonConstants.EXCEPTION_LIST, list);
                exception.put(CommonConstants.CONSTANT_CLASS, "com.see.truetransact.clientutil.exceptionhashmap.operativeaccount.OperativeAccountRuleHashMap");
                sqlMap.rollbackTransaction();
                throw new TTException(exception);
            }
        }
    }

    /**
     * To auto generate Lien ID from table
     */
    private String getFreezeID() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        HashMap where = new HashMap();
        where.put("WHERE", "OP_AC_FREEZE");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    /**
     * To update data
     */
    private void updateData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            int deleteLength = 0;
            int length = freezeList.size();
            if (freezeDeleteList != null) {
                deleteLength = freezeDeleteList.size();
            }
            //__ ToInsert and/or Update the existing Records...
            for (int i = 0; i < length; i++) {
                loanEligibilityTO = (LoanEligibilityTO) freezeList.get(i);
                loanEligibilityTO.setStatusBy(userID);
                if (loanEligibilityTO.getCropType().equalsIgnoreCase("")) {
//                    loanEligibilityTO.setFreezeId(getFreezeID());
//                    loanEligibilityTO.setCreatedBy(userID);
//                    loanEligibilityTO.setCreatedDt(currDate);
                    loanEligibilityTO.setStatusBy(userID);
                    loanEligibilityTO.setStatus(CommonConstants.STATUS_CREATED);
                    loanEligibilityTO.setStatusDate(currDate);

                    //__ Check for the Avaiable Balance...
//                    String ACTNO = CommonUtil.convertObjToStr(loanEligibilityTO.getActNum());
//                    String AMOUNT = CommonUtil.convertObjToStr(loanEligibilityTO.getFreezeAmt());
//                    checkRule(ACTNO, AMOUNT);
                    sqlMap.executeUpdate("insertLoanEligibilityTO", loanEligibilityTO);
                } else {
                    loanEligibilityTO.setStatusBy(userID);
                    loanEligibilityTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    loanEligibilityTO.setStatusDate(currDate);

                    sqlMap.executeUpdate("updateLoanEligibilityTO", loanEligibilityTO);
                }

                objLogTO.setData(loanEligibilityTO.toString());
//                objLogTO.setPrimaryKey(loanEligibilityTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            }

            //__ To add the Deleted records(at the time of Edit) into the DataBase...
            for (int j = 0; j < deleteLength; j++) {
                loanEligibilityTO = (LoanEligibilityTO) freezeDeleteList.get(j);
                loanEligibilityTO.setStatusBy(userID);
                loanEligibilityTO.setStatus(CommonConstants.STATUS_DELETED);
                loanEligibilityTO.setStatusDate(currDate);
                sqlMap.executeUpdate("deleteLoanEligibilityTO", loanEligibilityTO);

                objLogTO.setData(loanEligibilityTO.toString());
//                objLogTO.setPrimaryKey(loanEligibilityTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            }
            //            }
        } catch (Exception e) {
            if (e instanceof TTException) {
                TTException tte = (TTException) e;
                HashMap exceptionMap = tte.getExceptionHashMap();
                throw new TTException(exceptionMap);

            } else {
                // if something goes wrong, rollback the transaction
                sqlMap.rollbackTransaction();
                throw new TransRollbackException(e);
            }
        }
    }

    /**
     * To update data
     */
    private void deleteData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            int length = freezeList.size();
            /*
             * Update the status to 'DELETED'
             */
            for (int j = 0; j < length; j++) {
                loanEligibilityTO = (LoanEligibilityTO) freezeList.get(j);
                loanEligibilityTO.setStatusBy(userID);
                loanEligibilityTO.setStatus(CommonConstants.STATUS_DELETED);
                loanEligibilityTO.setStatusDate(currDate);

                sqlMap.executeUpdate("deleteloanEligibilityTO", loanEligibilityTO);

                objLogTO.setData(loanEligibilityTO.toString());
//                objLogTO.setPrimaryKey(loanEligibilityTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            }
        } catch (Exception e) {
            // if something goes wrong, rollback the transaction
            sqlMap.rollbackTransaction();
            throw new TransRollbackException(e);
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDate = ServerUtil.getCurrentDate(_branchCode);
        // Log DAO
        LogDAO objLogDAO = new LogDAO();
        if (map.containsKey("ADVANCES")) {
            advancesFreeze = (String) map.get("ADVANCES");
        } else {
            advancesFreeze = "";
        }
        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        objLogTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        objLogTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        objLogTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        objLogTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

        userID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
        //        branchID = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
        if (map.containsKey("LoanEligibilityTO")) {
            freezeList = (ArrayList) map.get("LoanEligibilityTO");

            String command = (String) map.get("COMMAND");

            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData(objLogDAO, objLogTO);
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                freezeDeleteList = (ArrayList) map.get("LoanEligibilityTODelete");
                updateData(objLogDAO, objLogTO);
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData(objLogDAO, objLogTO);
            } else {
                throw new NoCommandException();
            }

            destroyObjects();
            ServiceLocator.flushCache(sqlMap);
            System.gc();
        }

        if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap, objLogDAO, objLogTO);
            }
        }
        return resultMap;
    }

    private void authorize(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        String status = (String) map.get(CommonConstants.AUTHORIZESTATUS);
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        LoanEligibilityTO objLoanEligibilityTO = null;
        double amount = 0;
        HashMap dataMap;
        try {
            sqlMap.startTransaction();
            for (int i = 0, j = selectedList.size(); i < j; i++) {
                objLoanEligibilityTO = (LoanEligibilityTO) selectedList.get(i);
                objLoanEligibilityTO.setAuthStatus(status);
                objLoanEligibilityTO.setAuthBy(userID);
                objLoanEligibilityTO.setAuthDate(currDate);

                sqlMap.executeUpdate("authorizeLoanEligibilityTO", objLoanEligibilityTO);
//                objLogTO.setData(dataMap.toString());
//                objLogTO.setPrimaryKey("");
                objLogDAO.addToLog(objLogTO);

            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            if (e instanceof TTException) {
                TTException tte = (TTException) e;
                HashMap exceptionMap = tte.getExceptionHashMap();
                throw new TTException(exceptionMap);

            } else {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw new TransRollbackException(e);
            }
        }
    }

    private void updataAccountData(double amount, HashMap dataMap) throws Exception {
        //dataMap.put("AMOUNT", String.valueOf(amount));
        dataMap.put("AMOUNT",amount);
        //__ Update the Available Balance and Freeze Amount in Act_Master...
        if (advancesFreeze.equals("ADVANCES")) {
            sqlMap.executeUpdate("Freeze.updateAvailBalanceAD", dataMap);
        } else {
            sqlMap.executeUpdate("Freeze.updateAvailBalance", dataMap);
        }
        //__ Update the Account Status Id in the Act_Master...
        updateActMaster(CommonUtil.convertObjToStr(dataMap.get("ACT_NUM")));
    }

    /*
     * #######################################################################################
     */
    //__ To get the Data for the Particular AccountNo and FreezeId...
    public HashMap executeQuery(HashMap obj) throws Exception {
        System.out.println("@@@@OBJDISPLAY" + obj);
        int status = CommonUtil.convertObjToInt(obj.get(CommonConstants.STATUS));
        System.out.println("????status" + status);
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDate = ServerUtil.getCurrentDate(_branchCode);
        HashMap freezeMap = new HashMap();
        if (status == ClientConstants.ACTIONTYPE_AUTHORIZE || status == ClientConstants.ACTIONTYPE_REJECT) {
            List list = (List) sqlMap.executeQueryForList("getSelectLoanEligibilityTO", obj);
            freezeMap.put("LoanEligibilityTO", list);
            return freezeMap;
        } else {
            List list = (List) sqlMap.executeQueryForList("getSelectLoanEligibilityTO", obj);
            freezeMap.put("LoanEligibilityTO", list);
            return freezeMap;
        }
    }

    private void destroyObjects() {
        freezeList = null;
        freezeDeleteList = null;
    }

    /**
     * To update Account status in Account master
     */
    private void updateActMaster(String accountNumber) throws Exception {
        HashMap condition = new HashMap();
        condition.put("ACCTNUMBER", accountNumber);
        condition.put("BRANCH_CODE", _branchCode);
        //        try {
        List list = null;
        if (advancesFreeze.equals("ADVANCES")) {
            list = (List) sqlMap.executeQueryForList("getActiveAccountListAD", condition);
        } else {
            list = (List) sqlMap.executeQueryForList("getActiveAccountList", condition);
        }
        //            HashMap result = (HashMap)list.get(0);
        accountTO = new AccountTO();
        accountTO.setActNum(accountNumber);

        HashMap result1 = (HashMap) list.get(0);
        int ROW1 = CommonUtil.convertObjToInt(result1.get("COUNT"));
        int ROW2 = 0;
        if (list.size() > 1) {
            HashMap result2 = (HashMap) list.get(1);
            ROW2 = CommonUtil.convertObjToInt(result2.get("COUNT"));
        }

        int count = ROW1 + ROW2;
        System.out.println("count: " + count);
        //If there is no freeze data available for the account, change the account status
        if (count <= 0) {
            accountTO.setActStatusId("OPERATIONAL");
        } else {
//            List resultList = (List) sqlMap.executeQueryForList("Freeze.getAccountStatusId", condition);
//            HashMap resultMap = (HashMap)resultList.get(0);
//            int completeCount = CommonUtil.convertObjToInt(resultMap.get("COUNT"));
//            
//            if(completeCount > 0){
//                accountTO.setActStatusId(AcctStatusConstants.COMP_FREEZE);
//            }else{
//                accountTO.setActStatusId(AcctStatusConstants.PART_FREEZE);
//            }
            List resultList = (List) sqlMap.executeQueryForList("Freeze.getAccountStatusIdNew", condition);
            String freezeType = "";
            HashMap resultMap = new HashMap();
            for (int i = 0; i < resultList.size(); i++) {
                resultMap = (HashMap) resultList.get(i);
                freezeType = CommonUtil.convertObjToStr(resultMap.get("FREEZE_TYPE"));
            }
            if (freezeType.equalsIgnoreCase("COMPLETE")) {
                accountTO.setActStatusId(AcctStatusConstants.COMP_FREEZE);
            } else if (freezeType.equalsIgnoreCase("PARTIAL")) {
                accountTO.setActStatusId(AcctStatusConstants.PART_FREEZE);
            } else {
                accountTO.setActStatusId(freezeType);
            }
        }
        accountTO.setActStatusDt(currDate);
        sqlMap.executeUpdate("updateAccountMasterStatusId", accountTO);

        //        } catch (Exception e) {
        //            // if something goes wrong, rollback the transaction
        //            sqlMap.rollbackTransaction();
        //            throw new TransRollbackException(e);
        //        }
    }

    public static void main(String str[]) {
        try {
            LoanEligibilityMaintenanceDAO dao = new LoanEligibilityMaintenanceDAO();
            ArrayList testList = new ArrayList();
            HashMap testMap = new HashMap();
            HashMap hash = new HashMap();
            //            hash.put("AMOUNT","100.0");
            hash.put("ACT_NUM", "OA001016");
            hash.put("FREEZE_STATUS", "FREEZED");
            //            hash.put("STATUS","AUTHORIZED");
            hash.put("FREEZE_AMT", "100");
            hash.put("FREEZE_ID", "FA001024");
            hash.put("FREEZE_TYPE", "COMPLETE");
            hash.put("USER_ID", "admin");

            testList.add(hash);
            testMap.put(CommonConstants.AUTHORIZEDATA, testList);
            testMap.put(CommonConstants.AUTHORIZESTATUS, "AUTHORIZED");
            //            dao.authorize(testMap);
        } catch (Exception ex) {
            System.out.println("");
            ex.printStackTrace();
        }
    }
}
