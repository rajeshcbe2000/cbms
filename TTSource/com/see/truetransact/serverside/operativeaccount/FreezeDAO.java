/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LienMarkingDAO.java
 *
 * Created on September 08, 2003, 1:00 PM
 */
package com.see.truetransact.serverside.operativeaccount;

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
 * This is used for Accounts Data Access.
 *
 * @author Karthik
 */
public class FreezeDAO extends TTDAO {

    private SqlMap sqlMap;
    private FreezeTO freezeTO;
    private AccountTO accountTO;
    private ArrayList freezeList;
    private ArrayList freezeDeleteList;
    private String userID = "";
    private String advancesFreeze = "";
    HashMap resultMap = new HashMap();
    private Date currDt = null;
    /**
     * Creates a new instance of FreezeDAO
     */
    public FreezeDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    /*
     *
     * private void insertData() throws Exception{ try {
     * freezeTO.setFreezeId(getFreezeID());
     *
     * accountTO = new AccountTO(); accountTO.setActNum(freezeTO.getActNum());
     * accountTO.setActStatusId("PARTIALFREEZE");
     *
     * sqlMap.executeUpdate("insertFreezeTO", freezeTO);
     * sqlMap.executeUpdate("updateAccountMasterStatusId", accountTO); } catch
     * (Exception e) { // if something goes wrong, rollback the transaction
     * sqlMap.rollbackTransaction(); throw new TransRollbackException(e);
     * }finally{ accountTO = null; }
    }
     */
    /**
     * To insert Data
     */
    private void insertData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            ArrayList lst = new ArrayList();
            int length = freezeList.size();
            for (int i = 0; i < length; i++) {
                freezeTO = (FreezeTO) freezeList.get(i);
                String FREEZEID = getFreezeID();
                freezeTO.setFreezeId(FREEZEID);
                freezeTO.setCreatedBy(userID);
                freezeTO.setCreatedDt(currDt);
                freezeTO.setStatusBy(userID);
                freezeTO.setStatusDt(currDt);
                freezeTO.setStatus(CommonConstants.STATUS_CREATED);

                String ACTNO = CommonUtil.convertObjToStr(freezeTO.getActNum());
                String AMOUNT = CommonUtil.convertObjToStr(freezeTO.getFreezeAmt());
                checkRule(ACTNO, AMOUNT);

                sqlMap.executeUpdate("insertFreezeTO", freezeTO);
                lst.add(FREEZEID);
                resultMap.put("FREEZE_NO", lst);
                objLogTO.setData(freezeTO.toString());
                objLogTO.setPrimaryKey(freezeTO.getKeyData());
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

    //    private void checkRule(FreezeTO freezeTO)throws Exception{
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
            int length = freezeList.size();
            int deleteLength = freezeDeleteList.size();
            //__ ToInsert and/or Update the existing Records...
            for (int i = 0; i < length; i++) {
                freezeTO = (FreezeTO) freezeList.get(i);
                freezeTO.setStatusBy(userID);
                if (freezeTO.getFreezeId().equalsIgnoreCase("")) {
                    freezeTO.setFreezeId(getFreezeID());
                    freezeTO.setCreatedBy(userID);
                    freezeTO.setCreatedDt(currDt);
                    freezeTO.setStatusBy(userID);
                    freezeTO.setStatus(CommonConstants.STATUS_CREATED);
                    freezeTO.setStatusDt(currDt);

                    //__ Check for the Avaiable Balance...
                    String ACTNO = CommonUtil.convertObjToStr(freezeTO.getActNum());
                    String AMOUNT = CommonUtil.convertObjToStr(freezeTO.getFreezeAmt());
                    checkRule(ACTNO, AMOUNT);
                    sqlMap.executeUpdate("insertFreezeTO", freezeTO);
                } else {
                    freezeTO.setStatusBy(userID);
                    freezeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    freezeTO.setStatusDt(currDt);

                    sqlMap.executeUpdate("updateFreezeTO", freezeTO);
                }

                objLogTO.setData(freezeTO.toString());
                objLogTO.setPrimaryKey(freezeTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            }

            //__ To add the Deleted records(at the time of Edit) into the DataBase...
            for (int j = 0; j < deleteLength; j++) {
                freezeTO = (FreezeTO) freezeDeleteList.get(j);
                freezeTO.setStatusBy(userID);
                freezeTO.setStatus(CommonConstants.STATUS_DELETED);
                freezeTO.setStatusDt(currDt);
                sqlMap.executeUpdate("deleteFreezeTO", freezeTO);

                objLogTO.setData(freezeTO.toString());
                objLogTO.setPrimaryKey(freezeTO.getKeyData());
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
                freezeTO = (FreezeTO) freezeList.get(j);
                freezeTO.setStatusBy(userID);
                freezeTO.setStatus(CommonConstants.STATUS_DELETED);
                freezeTO.setStatusDt(currDt);

                sqlMap.executeUpdate("deleteFreezeTO", freezeTO);

                objLogTO.setData(freezeTO.toString());
                objLogTO.setPrimaryKey(freezeTO.getKeyData());
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
        currDt = ServerUtil.getCurrentDate(_branchCode);
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
        if (map.containsKey("FreezeTO")) {
            freezeList = (ArrayList) map.get("FreezeTO");

            String command = (String) map.get("COMMAND");

            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData(objLogDAO, objLogTO);
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                freezeDeleteList = (ArrayList) map.get("FreezeDelete");
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
        double amount = 0;
        HashMap dataMap;
        try {
            sqlMap.startTransaction();
            for (int i = 0, j = selectedList.size(); i < j; i++) {
                dataMap = (HashMap) selectedList.get(i);
                //__ To Check for the Avaiable Balance before Authorizing the Record...
                String ACTNO = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));
                String AMOUNT = CommonUtil.convertObjToStr(dataMap.get("FREEZE_AMT"));
                String FREEZETYPE = CommonUtil.convertObjToStr(dataMap.get("FREEZE_TYPE"));
                String FREEZESTATUS = CommonUtil.convertObjToStr(dataMap.get("FREEZE_STATUS"));

                if (FREEZESTATUS.equals("FREEZED")) {
                    checkRule(ACTNO, AMOUNT);
                }

                //__ If the Available Balance is enough for the LienAmount, Proceed...
                dataMap.put("STATUS", status);
                dataMap.put("USER_ID", objLogTO.getUserId());
                dataMap.put("AUTHORIZEDT", currDt.clone());
                amount = Double.parseDouble(dataMap.get("FREEZE_AMT").toString());

                //__ Authorize the Record in Act_freeze...
                if (status.equalsIgnoreCase(CommonConstants.STATUS_REJECTED)) {
                    if (FREEZESTATUS.equalsIgnoreCase("FREEZED")) {
                        dataMap.put("FREEZE_STATUS", CommonConstants.STATUS_FREEZE);
                    } else if (FREEZESTATUS.equalsIgnoreCase("UNFREEZED")) {
                        dataMap.put("FREEZE_STATUS", CommonConstants.STATUS_UNFREEZE);
                    }
                }
                sqlMap.executeUpdate("authorizeUpdateFreezeTO", dataMap);

                objLogTO.setData(dataMap.toString());
                objLogTO.setPrimaryKey("");
                objLogDAO.addToLog(objLogTO);

                /**
                 * If the Freeze Type is Comp_Freeze, do nothing... And if, its
                 * Part_freeze, Change the Available Balance and Freeze Amount
                 * in Account_Master...
                 */
                //                List list = (List) sqlMap.executeQueryForList("Freeze.getAccountStatus", CommonUtil.convertObjToStr(dataMap.get("ACT_NUM")));
                //                HashMap resultMap = (HashMap)list.get(0);
                //__ If the freeze-Type is Partilal Freeze...
                //                if(FREEZETYPE.equalsIgnoreCase("PARTIAL")){
                //                    System.out.println("In Part_freeze...");
                //__ If Freeze-Status is UNFREEZE, Subtract the freeze Amt from Freeze_Amt and Add it to
                //__ Available_balance in Act_Master
                if (FREEZESTATUS.equalsIgnoreCase("UNFREEZED")) {
                    amount = -amount;
                }
                //__ If the Freeze-Status is Freeze, Add the Freeze Amt to Freeze_amt and Subtract the Same from
                //__ Available_balance in Act_Master...
                if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                    updataAccountData(amount, dataMap);
                }
                //                }
                //                //__ If the Freeze-Type is Complete Freeze...
                //                else if(FREEZETYPE.equalsIgnoreCase("COMPLETE")){
                //                    //__ Update the Account Status Id in the Act_Master...
                //                    System.out.println("In Comp_freeze...");
                //                    updateActMaster(CommonUtil.convertObjToStr(dataMap.get("ACT_NUM")));
                //                }
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
        currDt = ServerUtil.getCurrentDate(_branchCode);
        HashMap freezeMap = new HashMap();
        if (status == ClientConstants.ACTIONTYPE_AUTHORIZE || status == ClientConstants.ACTIONTYPE_REJECT) {
            List list = (List) sqlMap.executeQueryForList("getFreezeAccountDetailsForAuthDisplay", obj);
            freezeMap.put("FreezeTO", list);
            return freezeMap;
        } else {
            List list = (List) sqlMap.executeQueryForList("getFreezeAccountDetails", obj);
            freezeMap.put("FreezeTO", list);
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
        accountTO.setActStatusDt(currDt);
        //sqlMap.executeUpdate("updateAccountMasterStatusId", accountTO);
        sqlMap.executeUpdate("updateFreezeAccountStatus", accountTO);

        //        } catch (Exception e) {
        //            // if something goes wrong, rollback the transaction
        //            sqlMap.rollbackTransaction();
        //            throw new TransRollbackException(e);
        //        }
    }

    public static void main(String str[]) {
        try {
            FreezeDAO dao = new FreezeDAO();
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
