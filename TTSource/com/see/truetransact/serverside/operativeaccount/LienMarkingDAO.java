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
import java.rmi.RemoteException;
import java.lang.String;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.operativeaccount.*;


import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.businessrule.RuleContext;
import com.see.truetransact.businessrule.RuleEngine;
import com.see.truetransact.businessrule.operativeaccount.*;
//import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.AcctStatusConstants;
import com.see.truetransact.clientutil.ClientConstants;
//__ To log the Data
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import java.util.Date;
/**
 * This is used for Accounts Data Access.
 *
 * @author Rahul
 */
public class LienMarkingDAO extends TTDAO {

    private SqlMap sqlMap;
    private LienMarkingTO lienTO;
    private AccountTO accountTO;
    private ArrayList lienList;
    private ArrayList lienDeleteList;
    private String userID = "";
    HashMap resultMap = new HashMap();
    private Date currDt = null;
    /**
     * Creates a new instance of LienMarkingDAO
     */
    public LienMarkingDAO() throws ServiceLocatorException {
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    /**
     * To insert Data
     */
    private void insertData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            ArrayList lst = new ArrayList();
            int length = lienList.size();
            for (int i = 0; i < length; i++) {
                lienTO = (LienMarkingTO) lienList.get(i);
                final String LIENID = getLienID();
                lienTO.setLienId(LIENID);

                lienTO.setCreatedBy(userID);
                lienTO.setCreatedDt(currDt);

                lienTO.setStatus(CommonConstants.STATUS_CREATED);
                lienTO.setStatusBy(userID);
                lienTO.setStatusDt(currDt);

                final String ACTNO = CommonUtil.convertObjToStr(lienTO.getActNum());
                final String AMOUNT = CommonUtil.convertObjToStr(lienTO.getLienAmt());

                checkRule(ACTNO, AMOUNT);

                sqlMap.executeUpdate("insertLienMarkingTO", lienTO);
                lst.add(LIENID);
                resultMap.put("LIEN_NO", lst);
                objLogTO.setData(lienTO.toString());
                objLogTO.setPrimaryKey(lienTO.getKeyData());
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

    /**
     * To auto generate Lien ID from table
     */
    private String getLienID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "OP_AC_LIEN");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    //    private void checkRule(LienMarkingTO lienTO)throws Exception{
    private void checkRule(String actNum, String amount) throws Exception {
        RuleContext context = new RuleContext();
        RuleEngine engine = new RuleEngine();
        context.addRule(new LienApplicationRule());


        accountTO = new AccountTO();
        accountTO.setActNum(actNum);
        //        accountTO.setActStatusId("LIENMARKED");

        HashMap inputMap = new HashMap();
        inputMap.put("ACCOUNTTO", accountTO);
        inputMap.put("AMOUNT", amount);
        //        inputMap.put("Operation", "LIEN");
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
     * To update data
     */
    private void updateData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            int length = lienList.size();
            int deleteLength = lienDeleteList.size();

            for (int i = 0; i < length; i++) {
                lienTO = (LienMarkingTO) lienList.get(i);
                if (lienTO.getLienId().equalsIgnoreCase("")) {
                    lienTO.setLienId(getLienID());

                    lienTO.setCreatedBy(userID);
                    lienTO.setCreatedDt(currDt);

                    lienTO.setStatusBy(userID);
                    lienTO.setStatusDt(currDt);
                    lienTO.setStatus(CommonConstants.STATUS_CREATED);

                    final String ACTNO = CommonUtil.convertObjToStr(lienTO.getActNum());
                    final String AMOUNT = CommonUtil.convertObjToStr(lienTO.getLienAmt());

                    checkRule(ACTNO, AMOUNT);
                    sqlMap.executeUpdate("insertLienMarkingTO", lienTO);
                } else {
                    lienTO.setStatusBy(userID);
                    lienTO.setStatusDt(currDt);
                    lienTO.setStatus(CommonConstants.STATUS_MODIFIED);

                    sqlMap.executeUpdate("updateLienMarkingTO", lienTO);
                }

                objLogTO.setData(lienTO.toString());
                objLogTO.setPrimaryKey(lienTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            }

            if (deleteLength > 0) {
                for (int j = 0; j < deleteLength; j++) {
                    lienTO = (LienMarkingTO) lienDeleteList.get(j);

                    lienTO.setStatusBy(userID);
                    lienTO.setStatusDt(currDt);
                    lienTO.setStatus(CommonConstants.STATUS_DELETED);

                    sqlMap.executeUpdate("deleteLienMarkingTO", lienTO);

                    objLogTO.setData(lienTO.toString());
                    objLogTO.setPrimaryKey(lienTO.getKeyData());
                    objLogDAO.addToLog(objLogTO);
                }
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
        }
    }

    /**
     * To update data
     */
    private void deleteData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            int length = lienList.size();
            /*
             * Update the status to 'DELETED'
             */
            for (int j = 0; j < length; j++) {
                lienTO = (LienMarkingTO) lienList.get(j);

                lienTO.setStatusBy(userID);
                lienTO.setStatusDt(currDt);
                lienTO.setStatus(CommonConstants.STATUS_DELETED);

                sqlMap.executeUpdate("deleteLienMarkingTO", lienTO);

                objLogTO.setData(lienTO.toString());
                objLogTO.setPrimaryKey(lienTO.getKeyData());
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

        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        objLogTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        objLogTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        objLogTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        objLogTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

        userID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));

        if (map.containsKey("LienMarkingTO")) {
            lienList = (ArrayList) map.get("LienMarkingTO");

            final String command = (String) map.get("COMMAND");
            sqlMap.startTransaction();
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData(objLogDAO, objLogTO);
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                lienDeleteList = (ArrayList) map.get("LienMarkingDelete");
                updateData(objLogDAO, objLogTO);
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData(objLogDAO, objLogTO);
            } else {
                throw new NoCommandException();
            }
            sqlMap.commitTransaction();
            destroyObjects();
            return resultMap;

        }

        if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap, objLogTO);
            }
        }
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return null;
    }

    private void authorize(HashMap map, LogTO objLogTO) throws Exception {
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
                String AMOUNT = CommonUtil.convertObjToStr(dataMap.get("LIEN_AMT"));
                String LIENSTATUS = CommonUtil.convertObjToStr(dataMap.get("LIEN_STATUS"));

                if (LIENSTATUS.equals("LIENED")) {
                    checkRule(ACTNO, AMOUNT);
                }

                //__ If the Available Balance is enough for the LienAmount, Proceed...
                dataMap.put("STATUS", status);
                dataMap.put("USER_ID", objLogTO.getUserId());
                dataMap.put("AUTHORIZEDT", currDt.clone());
                /*
                 * Update the Authorization Fields and Update the Available
                 * Balance.
                 */
                if (status.equalsIgnoreCase(CommonConstants.STATUS_REJECTED)) {
                    if (LIENSTATUS.equalsIgnoreCase("LIENED")) {
                        dataMap.put("LIEN_STATUS", CommonConstants.STATUS_LIEN);
                    } else if (LIENSTATUS.equalsIgnoreCase("UNLIENED")) {
                        dataMap.put("LIEN_STATUS", CommonConstants.STATUS_UNLIEN);
                    }
                }
                sqlMap.executeUpdate("authorizeUpdateLienMarkingTO", dataMap);

                amount = Double.parseDouble(dataMap.get("LIEN_AMT").toString());

                if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                    if (LIENSTATUS.equalsIgnoreCase("UNLIENED")) {
                        amount = -amount;
                    }

                    dataMap.put("AMOUNT", amount);
                    sqlMap.executeUpdate("Lien.updateAvailBalance", dataMap);
                    updateActMaster(CommonUtil.convertObjToStr(dataMap.get("ACT_NUM")));
                }
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

    /*
     * #######################################################################################
     */
    public HashMap executeQuery(HashMap obj) throws Exception {
        System.out.println("@@@@OBJDISPLAY" + obj);
        int status = CommonUtil.convertObjToInt(obj.get(CommonConstants.STATUS));
        System.out.println("????status" + status);
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        final HashMap lienMap = new HashMap();
        if (status == ClientConstants.ACTIONTYPE_AUTHORIZE || status == ClientConstants.ACTIONTYPE_REJECT) {
            List list = (List) sqlMap.executeQueryForList("getLienAccountDetailsForAuth", obj);
            lienMap.put("LienMarkingTO", list);
            return lienMap;
        } else {
            List list = (List) sqlMap.executeQueryForList("getLienAccountDetails", obj);
            lienMap.put("LienMarkingTO", list);
            return lienMap;

        }
    }

    private void destroyObjects() {
        lienList = null;
        lienDeleteList = null;
    }

    /**
     * To update Account status in Account master if the entire lien is deleted
     */
    private void updateActMaster(String accountNumber) throws Exception {
        final HashMap condition = new HashMap();
        condition.put("ACCTNUMBER", accountNumber);
//        try {
        final List accountStatusList = (List) sqlMap.executeQueryForList("Freeze.getAccountStatus", accountNumber);
        HashMap resultMap = (HashMap) accountStatusList.get(0);
        final String ACCSTATUS = CommonUtil.convertObjToStr(resultMap.get("ACT_STATUS_ID"));

        if (!ACCSTATUS.equalsIgnoreCase("COMP_FREEZE")) {
            accountTO = new AccountTO();
            accountTO.setActNum(accountNumber);

            final List list = (List) sqlMap.executeQueryForList("getActiveAccountList", condition);

            final HashMap result1 = (HashMap) list.get(0);
            final int ROW1 = CommonUtil.convertObjToInt(result1.get("COUNT"));
            int ROW2 = 0;
            if (list.size() > 1) {
                final HashMap result2 = (HashMap) list.get(1);
                ROW2 = CommonUtil.convertObjToInt(result2.get("COUNT"));
            }

            final int count = ROW1 + ROW2;
            //If there is no lien data available for the account, change the account status
            if (0 == count) {
                accountTO.setActStatusId("OPERATIONAL");
            } else {
                HashMap map = new HashMap();
                map.put("ACT_NUM", accountTO.getActNum());
                List list1 = (List) sqlMap.executeQueryForList("clearBalanceToPutCompFreezeOrPartialFreeze", map);
                HashMap map1 = null;
                map1 = (HashMap) list1.get(0);
                double res = CommonUtil.convertObjToDouble(map1.get("AVAILABLE_BALANCE")).doubleValue();
                System.out.println("???res" + res);
                if (res == 0) {
                    accountTO.setActStatusId(AcctStatusConstants.COMP_FREEZE);
                } else {
                    accountTO.setActStatusId(AcctStatusConstants.PART_FREEZE);
                }
            }

            accountTO.setActStatusDt(currDt);
            sqlMap.executeUpdate("updateAccountMasterStatusId", accountTO);

        }
//        } catch (Exception e) {
//            // if something goes wrong, rollback the transaction
//            sqlMap.rollbackTransaction();
//            throw new TransRollbackException(e);
//        }
    }

    public static void main(String str[]) {
        try {
            LienMarkingDAO dao = new LienMarkingDAO();
            ArrayList testList = new ArrayList();
            HashMap testMap = new HashMap();
            HashMap hash = new HashMap();
            //            hash.put("AMOUNT","100.0");
            hash.put("ACT_NUM", "OA060888");
            //            hash.put("LIEN_STATUS","LIENED");
            //            hash.put("STATUS","AUTHORIZED");
            hash.put("LIEN_AMT", "2000");
            hash.put("LIEN_ID", "FL001030");
            hash.put("LIEN_TYPE", "OTHER BANK");
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
