/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * GroupMDSDepositDAO.java
 *
 * Created on 31 January, 2013, 3:29 PM
 */
package com.see.truetransact.serverside.product.groupmdsdeposit;

import com.see.truetransact.serverside.product.loan.loaneligibilitymaintenance.*;
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
import com.see.truetransact.transferobject.product.groupmdsdeposit.GroupMDSDepositTO;
import java.sql.SQLException;

/**
 *
 * @author Admin
 */
public class GroupMDSDepositDAO extends TTDAO {

    private SqlMap sqlMap;
    private LoanEligibilityTO loanEligibilityTO;
    private AccountTO accountTO;
    private ArrayList freezeList;
    private ArrayList freezeDeleteList;
    private String userID = "";
    private String advancesFreeze = "";
    Date currDate = null;
    HashMap resultMap = new HashMap();
    private GroupMDSDepositTO groupMDSDepositTO;
    private String groupNo = "";

    /**

     * Creates a new instance of LoanEligibilityMaintenanceDAO
     */
    public GroupMDSDepositDAO() throws ServiceLocatorException {

        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    
    
    private String getGDSMDSProductGroupId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "GDS_MDS_GROUPID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");
        System.out.println("testing branch id :: " + _branchCode);
        where.put(CommonConstants.BRANCH_ID, _branchCode);// Added by nithya        
        String standingId = CommonUtil.convertObjToStr((dao.executeQuery(where)).get(CommonConstants.DATA));
        return standingId;
    }
    
    private boolean autoIdCreation() throws SQLException {
        boolean flag = true;
        if (groupMDSDepositTO.getGroupType().equalsIgnoreCase("Deposit")) {
            List idList = sqlMap.executeQueryForList("getGroupDepositAutoGenerateCount", null);
            if (idList != null && idList.size() > 0) {
                HashMap idMap = (HashMap) idList.get(0);
                if (idMap.containsKey("CNT") && idMap.get("CNT") != null && CommonUtil.convertObjToInt(idMap.get("CNT")) == 0) {
                   flag = false;
                }
            }
        }
        return flag;
    }

    private void insertData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();
            //groupNo = getGDSMDSProductGroupId();
            if (!autoIdCreation()) {
                groupNo = groupMDSDepositTO.getGroupName();
            } else {
                groupNo = getGDSMDSProductGroupId();
            }
            groupMDSDepositTO.setGroupNo(groupNo);
            groupMDSDepositTO.setCreatedBy(userID);
            groupMDSDepositTO.setCreatedDate(currDate);
            groupMDSDepositTO.setBranchId(_branchCode);
            groupMDSDepositTO.setStatus("CREATED");
            sqlMap.executeUpdate("insertGroupMDSDepositTO", groupMDSDepositTO);
            sqlMap.commitTransaction();
            resultMap.put("GROUP_ID",groupNo);
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
            sqlMap.startTransaction();                    
            groupMDSDepositTO.setStatusBy(userID);
            groupMDSDepositTO.setStatusDt(currDate);
            groupMDSDepositTO.setStatus("MODIFIED");
            sqlMap.executeUpdate("updateGroupMDSDepositTO", groupMDSDepositTO);
            sqlMap.commitTransaction();

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
                objLogDAO.addToLog(objLogTO);
            }
        } catch (Exception e) {
            // if something goes wrong, rollback the transaction
            sqlMap.rollbackTransaction();
            throw new TransRollbackException(e);
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("inside GroupMDSDepositDAO :: execute :: map :: " + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDate = ServerUtil.getCurrentDate(_branchCode);
      
        LogDAO objLogDAO = new LogDAO();      
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        objLogTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        objLogTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        objLogTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        objLogTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

        userID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
        if (map.containsKey("groupMDSDepositTO")) {
            groupMDSDepositTO = (GroupMDSDepositTO)map.get("groupMDSDepositTO");
            String command = (String) map.get("COMMAND");
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData(objLogDAO, objLogTO);
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
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
        String groupNo=CommonUtil.convertObjToStr(map.get("GROUP_NO"));
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        GroupMDSDepositTO groupMDSDepositTO = new GroupMDSDepositTO();
        double amount = 0;
        HashMap dataMap;
        try {
            sqlMap.startTransaction();
           
                groupMDSDepositTO.setAuthorizedStatus(status);
                groupMDSDepositTO.setAuthorizedBy(userID);
                groupMDSDepositTO.setAuthorizedDate(currDate);
                groupMDSDepositTO.setGroupNo(groupNo);                
                sqlMap.executeUpdate("updateAuthorizeGroupMDSTO", groupMDSDepositTO);
                objLogDAO.addToLog(objLogTO);
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
            List list = (List) sqlMap.executeQueryForList("getAllSelectGroupMDSDepositTO", obj);
            freezeMap.put("GroupMDSDepositTO", list.get(0));
            return freezeMap;
        } else {
            List list = (List) sqlMap.executeQueryForList("getAllSelectGroupMDSDepositTO", obj);
            freezeMap.put("GroupMDSDepositTO", list.get(0));
            return freezeMap;
        }
    }

    private void destroyObjects() {
        freezeList = null;
        freezeDeleteList = null;
        groupNo = null;
    }

    /**
     * To update Account status in Account master
     */
    private void updateActMaster(String accountNumber) throws Exception {
        HashMap condition = new HashMap();
        condition.put("ACCTNUMBER", accountNumber);
        condition.put("BRANCH_CODE", _branchCode);
        List list = null;
        if (advancesFreeze.equals("ADVANCES")) {
            list = (List) sqlMap.executeQueryForList("getActiveAccountListAD", condition);
        } else {
            list = (List) sqlMap.executeQueryForList("getActiveAccountList", condition);
        }
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

            }

    public static void main(String str[]) {
        try {
            GroupMDSDepositDAO dao = new GroupMDSDepositDAO();
            ArrayList testList = new ArrayList();
            HashMap testMap = new HashMap();
            HashMap hash = new HashMap();
            hash.put("ACT_NUM", "OA001016");
            hash.put("FREEZE_STATUS", "FREEZED");
            hash.put("FREEZE_AMT", "100");
            hash.put("FREEZE_ID", "FA001024");
            hash.put("FREEZE_TYPE", "COMPLETE");
            hash.put("USER_ID", "admin");

            testList.add(hash);
            testMap.put(CommonConstants.AUTHORIZEDATA, testList);
            testMap.put(CommonConstants.AUTHORIZESTATUS, "AUTHORIZED");
        } catch (Exception ex) {
            System.out.println("");
            ex.printStackTrace();
        }
    }
}
