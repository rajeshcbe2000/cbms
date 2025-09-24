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
public class TodAllowedDAO extends TTDAO {

    private SqlMap sqlMap;
    private FreezeTO freezeTO;
    private AccountTO accountTO;
    private TodAllowedTO todAllowedTO;
    private ArrayList freezeList;
    private ArrayList freezeDeleteList;
    private String userID = "";
    HashMap resultMap = new HashMap();
    Date currDt = null;
    boolean isFromOtherDAO = false;
    boolean isAutoAuthorize = false;

    /**
     * Creates a new instance of FreezeDAO
     */
    public TodAllowedDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    /**
     * To insert Data
     */
    private void insertData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        System.out.println("Insert Method in Tod DAO : " + map);
        TodAllowedTO todAllowedTO = (TodAllowedTO) map.get("TodAllowed");
        String TRANS_ID = getTransID();
        todAllowedTO.setCreatedBy(userID);
        todAllowedTO.setCreatedDt(currDt);
        todAllowedTO.setStatusBy(userID);
        todAllowedTO.setStatusDt(currDt);
        todAllowedTO.setStatus(CommonConstants.STATUS_CREATED);
        todAllowedTO.setTrans_id(TRANS_ID);
        System.out.println("Insert Method in Tod todAllowedTO : " + todAllowedTO);
        sqlMap.executeUpdate("insertTodAllowedTO", todAllowedTO);
        resultMap.put("TRANS_ID", TRANS_ID);
        objLogTO.setData(todAllowedTO.toString());
        objLogTO.setPrimaryKey(todAllowedTO.getKeyData());
        objLogDAO.addToLog(objLogTO);
    }

    /**
     * To auto generate Lien ID from table
     */
    private String getTransID() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        HashMap where = new HashMap();
        where.put("WHERE", "TOD_ALLOWED");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    /**
     * To update data
     */
    private void updateData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        System.out.println("Update Method in Tod DAO : " + map);
        TodAllowedTO todAllowedTO = (TodAllowedTO) map.get("TodAllowed");
        //__ ToInsert and/or Update the existing Records...
        todAllowedTO.setStatusBy(userID);
        if (todAllowedTO.getTrans_id().equalsIgnoreCase("")) {
            todAllowedTO.setTrans_id(getTransID());
            todAllowedTO.setCreatedBy(userID);
            todAllowedTO.setCreatedDt(currDt);
            todAllowedTO.setStatusBy(userID);
            todAllowedTO.setStatus(CommonConstants.STATUS_CREATED);
            todAllowedTO.setStatusDt(currDt);
            sqlMap.executeUpdate("insertTodAllowedTO", todAllowedTO);
        } else {
            todAllowedTO.setStatusBy(userID);
            todAllowedTO.setStatus(CommonConstants.STATUS_MODIFIED);
            todAllowedTO.setStatusDt(currDt);

            sqlMap.executeUpdate("updateTodTO", todAllowedTO);
        }

        objLogTO.setData(todAllowedTO.toString());
        objLogTO.setPrimaryKey(todAllowedTO.getKeyData());
        objLogDAO.addToLog(objLogTO);
    }

    /**
     * To delete data
     */
    private void deleteData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        /*
         * Update the status to 'DELETED'
         */
        System.out.println("Update Method in Tod DAO : " + map);
        TodAllowedTO todAllowedTO = (TodAllowedTO) map.get("TodAllowed");
        todAllowedTO.setStatusBy(userID);
        todAllowedTO.setStatus(CommonConstants.STATUS_DELETED);
        todAllowedTO.setStatusDt(currDt);

        sqlMap.executeUpdate("deleteTodAllowedTO", todAllowedTO);

        objLogTO.setData(todAllowedTO.toString());
        objLogTO.setPrimaryKey(todAllowedTO.getKeyData());
        objLogDAO.addToLog(objLogTO);
    }

    public HashMap execute(HashMap map, boolean otherDAO, boolean autoAuthorize) throws Exception {
        isFromOtherDAO = otherDAO;
        isAutoAuthorize = autoAuthorize;
        return execute(map);
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("Execute Method in Tod DAO : " + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        try {
            // Log DAO
            LogDAO objLogDAO = new LogDAO();

            // Log Transfer Object
            LogTO objLogTO = new LogTO();
            objLogTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
            objLogTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
            objLogTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
            objLogTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
            objLogTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

            userID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));

            if (!isFromOtherDAO) {
                sqlMap.startTransaction();
            }
            if (map.containsKey("TodAllowed")) {
                TodAllowedTO todAllowedTO = (TodAllowedTO) map.get("TodAllowed");

                String command = (String) map.get("MODE");
                System.out.println("Execute Method in Tod DAO : " + objLogTO);
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertData(map, objLogDAO, objLogTO);
                    if (isAutoAuthorize) {
                        HashMap authMap = new HashMap();
                        HashMap dataMap = new HashMap();
                        dataMap.put("TOD_AMOUNT", todAllowedTO.getTodAllowed());
                        dataMap.put("PROD_TYPE", todAllowedTO.getProductType());
                        dataMap.put("ACT_NUM", todAllowedTO.getAccountNumber());
                        dataMap.put("TRANS_ID", todAllowedTO.getTrans_id());
                        if(map.containsKey("AUTO_POSTING_EXCESS_AMOUNT")){// Added by nithya on 03-11-2017 for 0008180
                           dataMap.put("AUTO_POSTING_EXCESS_AMOUNT","AUTO_POSTING_EXCESS_AMOUNT");
                        }
                        if (map.containsKey("DRAWING_POWER")) {
                            dataMap.put("DRAWING_POWER", "DRAWING_POWER");
                        }
                        ArrayList dataList = new ArrayList();
                        dataList.add(dataMap);
                        authMap.put(CommonConstants.AUTHORIZEDATA, dataList);
                        authMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                        map.put(CommonConstants.AUTHORIZEMAP, authMap);
                    }
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    freezeDeleteList = (ArrayList) map.get("FreezeDelete");
                    updateData(map, objLogDAO, objLogTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData(map, objLogDAO, objLogTO);
                } else {
                    throw new NoCommandException();
                }

                destroyObjects();
            }

            if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
                HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
                if (authMap != null) {
                    authorize(authMap, objLogDAO, objLogTO);
                }
            }
            if (!isFromOtherDAO) {
                sqlMap.commitTransaction();
            }
        } catch (Exception e) {
            if (!isFromOtherDAO) {
                sqlMap.rollbackTransaction();
            }
            if (e instanceof TTException) {
                TTException tte = (TTException) e;
                HashMap exceptionMap = tte.getExceptionHashMap();
                throw new TTException(exceptionMap);

            } else {
                e.printStackTrace();
                throw new TransRollbackException(e);
            }
        }
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return resultMap;
    }

    private void authorize(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        String status = (String) map.get(CommonConstants.AUTHORIZESTATUS);
        String isTermLoanUI = "";
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        double amount = 0;
        HashMap dataMap;
        System.out.println("Authorize Method in Tod DAO : " + map);
        if (map.containsKey("TermLoanUI")) {
            isTermLoanUI = CommonUtil.convertObjToStr(map.get("TermLoanUI"));
        }
        for (int i = 0, j = selectedList.size(); i < j; i++) {
            dataMap = map;
            dataMap = (HashMap) selectedList.get(i);
            System.out.println("dataMap with selectedList : " + dataMap);
            //__ To Check for the Avaiable Balance before Authorizing the Record...
            //__ If the Available Balance is enough for the LienAmount, Proceed...
            dataMap.put("STATUS", status);
            dataMap.put("USER_ID", objLogTO.getUserId());
            dataMap.put("AUTHORIZED_DT", currDt);
            amount = Double.parseDouble(dataMap.get("TOD_AMOUNT").toString());
            System.out.println("Authorize Method in Tod DAO : " + dataMap);

            List list = sqlMap.executeQueryForList("getStopStatus", dataMap);
            System.out.println(" list size " + list.size());
            HashMap map1 = new HashMap();
            map1 = (HashMap) list.get(0);
            String stop_status = CommonUtil.convertObjToStr(map1.get("STOP_STATUS"));
            if (stop_status.equals("STOPPED")) {
                System.out.println("status" + status);
                if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                    sqlMap.executeUpdate("authorizeUpdateTodTO", dataMap);
                    sqlMap.executeUpdate("reverseDate1", dataMap);
                    objLogTO.setData(dataMap.toString());
                    objLogTO.setPrimaryKey("");
                    objLogDAO.addToLog(objLogTO);
                } else {
                    sqlMap.executeUpdate("reverseDate", dataMap);
                }
            } else {
                System.out.println("inside else");
                sqlMap.executeUpdate("authorizeUpdateTodTO", dataMap);

                objLogTO.setData(dataMap.toString());
                objLogTO.setPrimaryKey("");
                objLogDAO.addToLog(objLogTO);

                //
                //__ Update Available_balance in Act_Master...
                if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                    updataAccountData(amount, dataMap, isTermLoanUI);
                }

                //            sqlMap.commitTransaction();
            }
        }

    }

    private void updataAccountData(double amount, HashMap dataMap, String isTermLoanUI) throws Exception {
        //dataMap.put("AMOUNT", String.valueOf(amount));
        dataMap.put("AMOUNT", amount);
        Date tod_dt = null;
        String prod_type = CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE"));
        Date dt = ServerUtil.getCurrentDateProperFormat(_branchCode);
        System.out.println("@@@dt" + dt);
        System.out.println("dataMap" + dataMap);
        dataMap.put("TODAY_DT", dt);
        List lst = sqlMap.executeQueryForList("getTODUtilizedDetails", dataMap);
        if (lst != null && lst.size() > 0) {
            HashMap where = new HashMap();
            where = (HashMap) lst.get(0);
            System.out.println("Inside List" + where);
            double tod_utilized = 0.0;
            String tod_util = CommonUtil.convertObjToStr(where.get("TOD_UTILIZED"));
            if (tod_util != null) {
                if (dataMap.containsKey("DRAWING_POWER")) {
                    tod_utilized = CommonUtil.convertObjToDouble(dataMap.get("TOD_AMOUNT")).doubleValue();
                    double already_utilized = CommonUtil.convertObjToDouble(where.get("TOD_UTILIZED")).doubleValue();
                    tod_utilized = tod_utilized + already_utilized;
                } else {
                    tod_utilized = CommonUtil.convertObjToDouble(where.get("TOD_UTILIZED")).doubleValue();
                }
                dataMap.put("TOD_UTILIZED", new Double(tod_utilized));
                dataMap.put("TOD", "");
                System.out.println("Inside IF" + dataMap);
                sqlMap.executeUpdate("updateTODUtilizedWhileAuth", dataMap);
            }
        }
        if (prod_type != null && prod_type.equals("OA")) {
            //__ Update the Available Balance  in Act_Master...
            //            Date dt=ServerUtil.getCurrentDateProperFormat(_branchCode);
            //            System.out.println("@@@dt"+dt);
            //            System.out.println("dataMap"+dataMap);
            //            dataMap.put("TODAY_DT",dt);
            //            List lst = sqlMap.executeQueryForList("getTODUtilizedDetails",dataMap);
            //            if(lst!=null && lst.size()>0){
            //                HashMap where = new HashMap();
            //                where=(HashMap)lst.get(0);
            //                System.out.println("Inside List"+where);
            //                String tod_util=CommonUtil.convertObjToStr(where.get("TOD_UTILIZED"));
            //                if(tod_util!=null){
            //                    double tod_utilized = CommonUtil.convertObjToDouble(where.get("TOD_UTILIZED")).doubleValue();
            //                    dataMap.put("TOD_UTILIZED",new Double(tod_utilized));
            //                    dataMap.put("TOD","");
            //                    System.out.println("Inside IF"+dataMap);
            //                    sqlMap.executeUpdate("updateTODUtilizedWhileAuth", dataMap);
            //                }
            //            }
            List list = sqlMap.executeQueryForList("getTODUtilizedDetailsForAvBalUpdate", dataMap);
            HashMap map = new HashMap();
            if (list != null && list.size() > 0) {
                map = (HashMap) list.get(0);
                tod_dt = (Date) map.get("FROM_DT");
                System.out.println("@@@tod_dt" + tod_dt);
            }
            if (dt.equals(tod_dt)) {
                if (prod_type.equals("OA")) {
                    sqlMap.executeUpdate("Tod.updateAvailBalance", dataMap);
                    map.put("OA_CLEAR_BAL", "");
                } else {
                    if (!isTermLoanUI.equals("TermLoanUI")) {
                        sqlMap.executeUpdate("Tod.updateAvailBalanceAD", dataMap);
                    }
                    map.put("AD_CLEAR_BAL", "");
                }
                sqlMap.executeUpdate("updateCBTOD", map);
            }
        }
        if (prod_type != null && prod_type.equals("AD")) {
            double todUtilizedAmt = 0.0;
            double todAllowedAmt = 0.0;
            double dpAmt = 0.0;
            HashMap balanceMap = new HashMap();
            balanceMap.put("ACT_NUM", dataMap.get("ACT_NUM"));
            lst = sqlMap.executeQueryForList("getBalanceAD", balanceMap);
            if (lst != null && lst.size() > 0) {
                balanceMap = (HashMap) lst.get(0);
                double limit = CommonUtil.convertObjToDouble(balanceMap.get("LIMIT")).doubleValue();
                double availBal = CommonUtil.convertObjToDouble(balanceMap.get("AVAILABLE_BALANCE")).doubleValue();
                double totBal = CommonUtil.convertObjToDouble(balanceMap.get("TOTAL_BALANCE")).doubleValue();
                double clearBal = CommonUtil.convertObjToDouble(balanceMap.get("CLEAR_BALANCE")).doubleValue();
                HashMap todAllowedMap = new HashMap();
                todAllowedMap.put("ACT_NUM", dataMap.get("ACT_NUM"));
                todAllowedMap.put("TODAY_DATE", currDt);
                lst = sqlMap.executeQueryForList("getSelectSumOfTODAmount", todAllowedMap);
                if (lst != null && lst.size() > 0) {
                    todAllowedMap = (HashMap) lst.get(0);
                    todUtilizedAmt = CommonUtil.convertObjToDouble(todAllowedMap.get("TOD_UTILIZED")).doubleValue();
                    todAllowedAmt = CommonUtil.convertObjToDouble(todAllowedMap.get("TOD_AMOUNT")).doubleValue();
                }
                HashMap drawingPowerAmtMap = new HashMap();
                drawingPowerAmtMap.put("ACT_NUM", dataMap.get("ACT_NUM"));
                lst = sqlMap.executeQueryForList("getdrawingPowerAmtAD", drawingPowerAmtMap);
                if (lst != null && lst.size() > 0) {
                    drawingPowerAmtMap = (HashMap) lst.get(0);
                    dpAmt = CommonUtil.convertObjToDouble(drawingPowerAmtMap.get("DRAWING_POWER")).doubleValue();
                }
                double balance = todAllowedAmt - todUtilizedAmt;
                if (dpAmt > 0 && limit > dpAmt) {
                    if ((clearBal * -1) > dpAmt) {
                        System.out.println("if condition part :");
                        dataMap.put("AMOUNT", String.valueOf(balance));
                        dataMap.put("LOAN_PAID_INT", String.valueOf(balance * -1));
                    } else {
                        System.out.println("else part :");
                        double balanceAmt = dpAmt + clearBal;
                        dataMap.put("AMOUNT", String.valueOf(balanceAmt + balance));
                        dataMap.put("LOAN_PAID_INT", String.valueOf(balanceAmt));
                    }
                    if(!dataMap.containsKey("AUTO_POSTING_EXCESS_AMOUNT")){ // Added by nithya for 0008180
                        sqlMap.executeUpdate("Tod.updateAvailableBalanceAD", dataMap);
                    }                    
                } else {
                    double balanceAmt = limit + clearBal;
                    double balancePaid = limit + clearBal;
                    balanceAmt = (balanceAmt + todAllowedAmt) - todUtilizedAmt;
                    dataMap.put("AMOUNT", String.valueOf(balanceAmt));
                    dataMap.put("LOAN_PAID_INT", String.valueOf(balancePaid));
                    if(!dataMap.containsKey("AUTO_POSTING_EXCESS_AMOUNT")){ // Added by nithya for 0008180
                       sqlMap.executeUpdate("Tod.updateAvailableBalanceAD", dataMap); 
                    }                   
                }
            }
        }
        //__ Update the Account Status Id in the Act_Master...
        //        updateActMaster(CommonUtil.convertObjToStr(dataMap.get("ACT_NUM")));
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
            List list = (List) sqlMap.executeQueryForList("getTodForAuthDisplay", obj);
            freezeMap.put("TodAllowedTO", list);
            return freezeMap;
        } else {
            List list = (List) sqlMap.executeQueryForList("getSelectAccountListForEdit", obj);
            freezeMap.put("TodAllowedTO", list);
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
        //        try {
        List list = (List) sqlMap.executeQueryForList("getActiveAccountList", condition);
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
            List resultList = (List) sqlMap.executeQueryForList("Freeze.getAccountStatusId", condition);
            HashMap resultMap = (HashMap) resultList.get(0);
            int completeCount = CommonUtil.convertObjToInt(resultMap.get("COUNT"));

            if (completeCount > 0) {
                accountTO.setActStatusId(AcctStatusConstants.COMP_FREEZE);
            } else {
                accountTO.setActStatusId(AcctStatusConstants.PART_FREEZE);
            }
        }
        accountTO.setActStatusDt(currDt);
        sqlMap.executeUpdate("updateAccountMasterStatusId", accountTO);

        //        } catch (Exception e) {
        //            // if something goes wrong, rollback the transaction
        //            sqlMap.rollbackTransaction();
        //            throw new TransRollbackException(e);
        //        }
    }

    public static void main(String str[]) {
        try {
            TodAllowedDAO dao = new TodAllowedDAO();
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
