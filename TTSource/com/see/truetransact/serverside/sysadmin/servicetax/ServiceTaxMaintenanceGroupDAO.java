/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ServiceTaxMaintenanceGroupDAO.java
 *
 * Created on Tue May 25 10:39:44 IST 2004
 */
package com.see.truetransact.serverside.sysadmin.servicetax;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;

import java.util.LinkedHashMap;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.LocaleConstants;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.batchprocess.task.operativeaccount.interest.DepositIntTask;

import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;

import com.see.truetransact.transferobject.sysadmin.servicetax.ServiceTaxMaintenanceGroupTO;
import com.see.truetransact.transferobject.sysadmin.servicetax.ServiceTaxMaintenanceCategotyTO;
import com.see.truetransact.transferobject.sysadmin.servicetax.ServiceTaxMaintenanceProdTO;
import com.see.truetransact.transferobject.sysadmin.servicetax.ServiceTaxMaintenanceTypeTO;
import com.see.truetransact.transferobject.sysadmin.servicetax.ServiceTaxMaintenanceRateTO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.product.deposits.DepositsProductDAO;
// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

/**
 * ServiceTaxMaintenanceGroup DAO.
 *
 */
public class ServiceTaxMaintenanceGroupDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private ServiceTaxMaintenanceGroupTO objServiceTaxMaintenanceGroupTO;
    private ServiceTaxMaintenanceCategotyTO objServiceTaxMaintenanceCategotyTO;
    private ServiceTaxMaintenanceProdTO objServiceTaxMaintenanceProdTO;
    private ServiceTaxMaintenanceRateTO objServiceTaxMaintenanceRateTO;
    private ArrayList productData;
    private ArrayList categoryData;
    //agents Commision Interest Eate Maintanance
    private ArrayList agentsList = new ArrayList();
    private HashMap agentsMap = new HashMap();
    private ArrayList interRateData;
    private ArrayList interRateDeleteData;
    private String command = "";
    private String userID = "";
    private String prodId = "";
    private Date curDate = null;

    /**
     * Creates a new instance of ServiceTaxMaintenanceGroupDAO
     */
    public ServiceTaxMaintenanceGroupDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
//        List list = (List) sqlMap.executeQueryForList("getSelectServiceTaxMaintenanceGroupTO", where);
//        returnMap.put("ServiceTaxMaintenanceGroupTO", list);
        List list = (List) sqlMap.executeQueryForList("getSelectServiceTaxMaintenanceCategotyTO", where);
        returnMap.put("ServiceTaxMaintenanceCategotyTO", list);
        list = (List) sqlMap.executeQueryForList("getSelectServiceTaxMaintenanceProdTO", where);
        returnMap.put("ServiceTaxMaintenanceProdTO", list);
        //        list = (List) sqlMap.executeQueryForList("getSelectServiceTaxMaintenanceTypeTO", where);
        //        returnMap.put("ServiceTaxMaintenanceTypeTO", list);
        list = (List) sqlMap.executeQueryForList("getSelectServiceTaxMaintenanceRateTO", where);
        returnMap.put("ServiceTaxMaintenanceRateTO", list);
        return returnMap;
    }

    private void insertData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            /*
             * To Set the RoiGroupId in all The Tables...
             */
            sqlMap.startTransaction();

            if (agentsMap.containsKey("AGENTS_COMMISION")) {
                agentsCommisionRateInsert(CommonConstants.STATUS_CREATED);
            } else {
                final String ROIID = objServiceTaxMaintenanceGroupTO.getRoiGroupName();// getROIID();
                objServiceTaxMaintenanceGroupTO.setRoiGroupId(ROIID);
                //__ To Set the Status in Table as Created 
                objServiceTaxMaintenanceGroupTO.setStatusBy(userID);
                objServiceTaxMaintenanceGroupTO.setStatus(CommonConstants.STATUS_CREATED);
                objServiceTaxMaintenanceGroupTO.setStatusDt(curDate);

                objServiceTaxMaintenanceGroupTO.setCreatedBy(userID);
                objServiceTaxMaintenanceGroupTO.setCreatedDt(curDate);
                /*
                 * To Verify the Prod->Category group Data for the Objects which
                 * are passed as ArrayList(s) and/or HashMap from the OB...
                 */
                System.out.println("###### roiid : " + ROIID);
//                getDuplicate(ROIID);

                System.out.println("start insertData");
//                sqlMap.executeUpdate("insertServiceTaxMaintenanceGroupTO", objServiceTaxMaintenanceGroupTO);

                objLogTO.setStatus(command);
                objLogTO.setData(objServiceTaxMaintenanceGroupTO.toString());
                objLogTO.setPrimaryKey(objServiceTaxMaintenanceGroupTO.getKeyData());

                objLogDAO.addToLog(objLogTO);
                /*
                 * To Enter the Data for the Objects which are passed as
                 * ArrayList(s) and/or HashMap from the OB...
                 */
                insertDataTab(ROIID, CommonConstants.STATUS_CREATED, objLogDAO, objLogTO);
                System.out.println("commit insertData");
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            if (e instanceof TTException) {
                throw new TTException(e);
            } else {
                throw new TransRollbackException(e);
            }
        }
    }

    private String getROIID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", "DEPOSIT.INT_GRP_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void getDuplicate(String groupId) throws Exception {
        try {
            ArrayList prodList = new ArrayList();
            int prod = productData.size();
            int category = categoryData.size();
            for (int length = 0; length < prod; length++) {
                for (int catLen = 0; catLen < category; catLen++) {
                    StringBuffer str = new StringBuffer();
//                    str.append(CommonUtil.convertObjToStr(((ServiceTaxMaintenanceProdTO)productData.get(length)).getProdId()));
                    str.append(CommonUtil.convertObjToStr(((ServiceTaxMaintenanceCategotyTO) categoryData.get(catLen)).getCategoryId()));
                    prodList.add(str.toString());
                }
            }
            HashMap map = new HashMap();
            map.put("prodList", prodList);
            if (!groupId.equalsIgnoreCase("")) {
                map.put("ROIGROUPID", groupId);
            }

            map.put("PRODUCT_TYPE", objServiceTaxMaintenanceGroupTO.getProductType());
            List duplicate = sqlMap.executeQueryForList("getDuplicateGroupData", map);
            int size = duplicate.size();
            System.out.println("########duplicateData" + duplicate);
            if (size > 0) {
                HashMap exception = new HashMap();
                ArrayList list = new ArrayList();
                list.add("DUPLICATEGROUP"); // The Key Value for the ExceptionHashMap...
                System.out.println("insideif########list" + list);
                exception.put(CommonConstants.EXCEPTION_LIST, list);
                exception.put(CommonConstants.CONSTANT_CLASS,
                        "com.see.truetransact.clientutil.exceptionhashmap.deposit.DepositRuleHashMap");
                exception.put(CommonConstants.EXCEPTION_TYPE, CommonConstants.CONCAT_EXCEPTION);
                System.out.println("insideif########exception" + exception);
                StringBuffer str = new StringBuffer();

                HashMap duplicateMap = new HashMap();
                for (int i = 0; i < size; i++) {
                    HashMap interMap = (HashMap) duplicate.get(i);
                    final Object[] keyList = (interMap.keySet()).toArray();
                    final int keyListLength = keyList.length;
                    for (int j = 0; j < keyListLength; j++) {
                        duplicateMap.put(CommonUtil.convertObjToStr(keyList[j]), CommonUtil.convertObjToStr(interMap.get(keyList[j])));
                        System.out.println("insidefor########duplicateMap" + duplicateMap);
                    }

                    final Object[] duplicatekeyList = (duplicateMap.keySet()).toArray();
                    final int duplicateLength = duplicatekeyList.length;

                    for (int k = 0; k < duplicateLength; k++) {
                        str.append(CommonUtil.convertObjToStr(interMap.get(duplicatekeyList[k])) + "\n");
                        System.out.println("&&&&&&&&str" + str);
                    }
                    System.out.println("insideif########duplicateMap" + duplicateMap);
                }
                ArrayList duplicateList = new ArrayList();
                duplicateList.add(str.toString());
                exception.put(CommonConstants.SERVER_VALUE, duplicateList);
                System.out.println("insideif########duplicateList" + duplicateList);
                System.out.println("insideif########duplicateMap" + duplicateMap);

                list = null; // To Destroy the List obj...
                duplicateList = null; // To Destroy the List obj...
                throw new TTException("Duplicate Group");
            }
        } catch (Exception e) {
            if (e instanceof TTException) {
                System.out.println("INSIDEif" + e);
                throw new TTException(e);
            } else {
                throw new TransRollbackException(e);
            }
        }
    }

    private void agentsCommisionRateInsert(String status) throws Exception {
        int interRate = agentsList.size();
        for (int k = 0; k < interRate; k++) {
            objServiceTaxMaintenanceRateTO = (ServiceTaxMaintenanceRateTO) agentsList.get(k);
            objServiceTaxMaintenanceRateTO.setStatus(status);
            System.out.println("######agentsCommisionRateInsert : " + objServiceTaxMaintenanceRateTO);
            sqlMap.executeUpdate("insertServiceTaxMaintenanceRateTO", objServiceTaxMaintenanceRateTO);
        }
    }

    private void insertDataTab(String ID, String status, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        int prod = productData.size();
        int category = categoryData.size();
        int interRate = interRateData.size();

        try {
            for (int i = 0; i < prod; i++) {
                objServiceTaxMaintenanceProdTO = (ServiceTaxMaintenanceProdTO) productData.get(i);
                objServiceTaxMaintenanceProdTO.setRoiGroupId(ID);
                objServiceTaxMaintenanceProdTO.setStatus(status);
                sqlMap.executeUpdate("insertServiceTaxMaintenanceProdTO", objServiceTaxMaintenanceProdTO);

                objLogTO.setStatus(command);
                objLogTO.setData(objServiceTaxMaintenanceProdTO.toString());
                objLogTO.setPrimaryKey(objServiceTaxMaintenanceProdTO.getKeyData());

                objLogDAO.addToLog(objLogTO);
            }

            for (int j = 0; j < category; j++) {
                objServiceTaxMaintenanceCategotyTO = (ServiceTaxMaintenanceCategotyTO) categoryData.get(j);
                objServiceTaxMaintenanceCategotyTO.setRoiGroupId(ID);
                objServiceTaxMaintenanceCategotyTO.setStatus(status);
                objServiceTaxMaintenanceCategotyTO.setStatusBy(userID);
                sqlMap.executeUpdate("insertServiceTaxMaintenanceCategotyTO", objServiceTaxMaintenanceCategotyTO);

                objLogTO.setStatus(command);
                objLogTO.setData(objServiceTaxMaintenanceCategotyTO.toString());
                objLogTO.setPrimaryKey(objServiceTaxMaintenanceCategotyTO.getKeyData());

                objLogDAO.addToLog(objLogTO);
            }

            for (int k = 0; k < interRate; k++) {
                objServiceTaxMaintenanceRateTO = (ServiceTaxMaintenanceRateTO) interRateData.get(k);
                objServiceTaxMaintenanceRateTO.setRoiGroupId(ID);
                objServiceTaxMaintenanceRateTO.setStatus(status);
                Date dt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objServiceTaxMaintenanceRateTO.getRoiDate()));
                Date curDt = (Date) curDate.clone();
                if (dt != null) {
                    Date isidDate = (Date) curDt.clone();
                    isidDate.setDate(dt.getDate());
                    isidDate.setMonth(dt.getMonth());
                    isidDate.setYear(dt.getYear());
                    objServiceTaxMaintenanceRateTO.setRoiDate(isidDate);
                }

                Date endDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objServiceTaxMaintenanceRateTO.getRoiEndDate()));
                if (endDt != null) {
                    Date isidDate = (Date) curDt.clone();
                    isidDate.setDate(endDt.getDate());
                    isidDate.setMonth(endDt.getMonth());
                    isidDate.setYear(endDt.getYear());
                    objServiceTaxMaintenanceRateTO.setRoiEndDate(isidDate);

                }

                sqlMap.executeUpdate("insertServiceTaxMaintenanceRateTO", objServiceTaxMaintenanceRateTO);

                objLogTO.setStatus(command);
                objLogTO.setData(objServiceTaxMaintenanceRateTO.toString());
                objLogTO.setPrimaryKey(objServiceTaxMaintenanceRateTO.getKeyData());

                objLogDAO.addToLog(objLogTO);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
    //agentsCommision Rate updating

    private void agentsCommisionRateUpdate(String status) throws Exception {
        HashMap where = new HashMap();
        String delStatus = CommonConstants.STATUS_DELETED;
        where.put("PROD_ID", prodId);
        List prodList = sqlMap.executeQueryForList("getBehavesLikeForDeposit", where);
        if (prodList.size() > 0) {
            where = (HashMap) prodList.get(0);
            if (where.get("BEHAVES_LIKE").equals("RECURRING")) {
                where.put("ROI_GROUP_ID", prodId);
                List lst = sqlMap.executeQueryForList("getSelectServiceTaxMaintenanceRatewhere", where);
                if (lst != null) {
                    for (int i = 0; i < lst.size(); i++) {
                        objServiceTaxMaintenanceRateTO = (ServiceTaxMaintenanceRateTO) lst.get(i);
                        objServiceTaxMaintenanceRateTO.setStatus(CommonConstants.STATUS_DELETED);
                        System.out.println("#####agentsCommisionRateUpdate status : " + status);
                        sqlMap.executeUpdate("deleteServiceTaxMaintenanceRateTO", objServiceTaxMaintenanceRateTO);
                        System.out.println("#####agentsCommisionRateUpdate status : " + objServiceTaxMaintenanceRateTO);
                    }
                }
            }
        }
        objServiceTaxMaintenanceRateTO = new ServiceTaxMaintenanceRateTO();
        System.out.println("#####agentsCommisionRateUpdate : " + objServiceTaxMaintenanceRateTO.getStatus());
//        if(status.equals(objServiceTaxMaintenanceRateTO.getStatus()))
//        objServiceTaxMaintenanceRateTO.setRoiGroupId(ID);
//        sqlMap.executeUpdate("deleteServiceTaxMaintenanceRateTO", objServiceTaxMaintenanceRateTO);

        int interRate = agentsList.size();
        for (int k = 0; k < interRate; k++) {
            objServiceTaxMaintenanceRateTO = (ServiceTaxMaintenanceRateTO) agentsList.get(k);
            objServiceTaxMaintenanceRateTO.setStatus(status);
            System.out.println("#####agentsCommisionRateUpdate : " + objServiceTaxMaintenanceRateTO);
            sqlMap.executeUpdate("insertServiceTaxMaintenanceRateTO", objServiceTaxMaintenanceRateTO);
        }
    }

    private void deleteAgentsTableRow(ArrayList agentsList) throws Exception {
        int interTypeDelete = agentsList.size();
        for (int i = 0; i < interTypeDelete; i++) {
            objServiceTaxMaintenanceRateTO = (ServiceTaxMaintenanceRateTO) agentsList.get(i);
//            objServiceTaxMaintenanceRateTO.setRoiGroupId(ID);
            objServiceTaxMaintenanceRateTO.setStatus(CommonConstants.STATUS_DELETED);
            sqlMap.executeUpdate("deleteServiceTaxMaintenanceRateTO", objServiceTaxMaintenanceRateTO);
//            sqlMap.executeUpdate("insertServiceTaxMaintenanceRateTO", objServiceTaxMaintenanceRateTO);
        }
    }

    private void updateData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();

            if (agentsMap.containsKey("AGENTS_COMMISION")) {
                agentsCommisionRateUpdate(CommonConstants.STATUS_MODIFIED);
//                deleteAgentsTableRow(agentsList);
            } else {
                final String ROIID = CommonUtil.convertObjToStr(objServiceTaxMaintenanceGroupTO.getRoiGroupName());
                System.out.println("ROIID------------" + ROIID);
                /*
                 * To Verify the Prod->Category group Data for the Objects which
                 * are passed as ArrayList(s) and/or HashMap from the OB...
                 */
                getDuplicate(ROIID);

                System.out.println("start updateData");

                objServiceTaxMaintenanceGroupTO.setStatusBy(userID);
                objServiceTaxMaintenanceGroupTO.setStatus(CommonConstants.STATUS_MODIFIED);
                objServiceTaxMaintenanceGroupTO.setStatusDt(curDate);

//                sqlMap.executeUpdate("updateServiceTaxMaintenanceGroupTO", objServiceTaxMaintenanceGroupTO);

                objLogTO.setStatus(command);
                objLogTO.setData(objServiceTaxMaintenanceGroupTO.toString());
                objLogTO.setPrimaryKey(objServiceTaxMaintenanceGroupTO.getKeyData());

                objLogDAO.addToLog(objLogTO);

                updateDeleteDataTab(ROIID, CommonConstants.STATUS_MODIFIED, objLogDAO, objLogTO);
                /*
                 * To delete the Rows from the Interest table...
                 */
                deleteInterTableRow(ROIID);
                System.out.println("commit updateData");
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            if (e instanceof TTException) {
                throw new TTException(e);
            } else {
                throw new TransRollbackException(e);
            }
        }
    }

    private void agentsCommisionRateDelete(String status) throws Exception {
        int interRate = agentsList.size();
        for (int k = 0; k < interRate; k++) {
            objServiceTaxMaintenanceRateTO = (ServiceTaxMaintenanceRateTO) agentsList.get(k);
            objServiceTaxMaintenanceRateTO.setStatus(CommonConstants.STATUS_DELETED);
            System.out.println("######agentsCommisionRateDelete : " + objServiceTaxMaintenanceRateTO);
            sqlMap.executeUpdate("insertServiceTaxMaintenanceRateTO", objServiceTaxMaintenanceRateTO);
        }
    }

    private void updateDeleteDataTab(String ID, String Status, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        int prod = productData.size();
        int category = categoryData.size();
        int interRate = interRateData.size();
        curDate = (Date) curDate.clone();


        objServiceTaxMaintenanceProdTO = new ServiceTaxMaintenanceProdTO();
        objServiceTaxMaintenanceProdTO.setRoiGroupId(ID);
        sqlMap.executeUpdate("deleteServiceTaxMaintenanceProdTO", objServiceTaxMaintenanceProdTO);

        objServiceTaxMaintenanceCategotyTO = new ServiceTaxMaintenanceCategotyTO();
        objServiceTaxMaintenanceCategotyTO.setRoiGroupId(ID);
        sqlMap.executeUpdate("deleteServiceTaxMaintenanceCategotyTO", objServiceTaxMaintenanceCategotyTO);

        objServiceTaxMaintenanceRateTO = new ServiceTaxMaintenanceRateTO();
        objServiceTaxMaintenanceRateTO.setRoiGroupId(ID);
        sqlMap.executeUpdate("deleteServiceTaxMaintenanceRateTO", objServiceTaxMaintenanceRateTO);

        try {
            for (int i = 0; i < prod; i++) {
                objServiceTaxMaintenanceProdTO = (ServiceTaxMaintenanceProdTO) productData.get(i);
                objServiceTaxMaintenanceProdTO.setRoiGroupId(ID);
                objServiceTaxMaintenanceProdTO.setStatus(Status);
                sqlMap.executeUpdate("insertServiceTaxMaintenanceProdTO", objServiceTaxMaintenanceProdTO);

                objLogTO.setStatus(command);
                objLogTO.setData(objServiceTaxMaintenanceProdTO.toString());
                objLogTO.setPrimaryKey(objServiceTaxMaintenanceProdTO.getKeyData());

                objLogDAO.addToLog(objLogTO);
            }

            for (int j = 0; j < category; j++) {
                objServiceTaxMaintenanceCategotyTO = (ServiceTaxMaintenanceCategotyTO) categoryData.get(j);
                objServiceTaxMaintenanceCategotyTO.setRoiGroupId(ID);
                objServiceTaxMaintenanceCategotyTO.setStatus(Status);
                objServiceTaxMaintenanceCategotyTO.setStatusBy(userID);
                sqlMap.executeUpdate("insertServiceTaxMaintenanceCategotyTO", objServiceTaxMaintenanceCategotyTO);

                objLogTO.setStatus(command);
                objLogTO.setData(objServiceTaxMaintenanceCategotyTO.toString());
                objLogTO.setPrimaryKey(objServiceTaxMaintenanceCategotyTO.getKeyData());

                objLogDAO.addToLog(objLogTO);
            }

            for (int k = 0; k < interRate; k++) {
                Date Dt = (Date) curDate.clone();
                if (objServiceTaxMaintenanceRateTO.getRoiDate() != null && objServiceTaxMaintenanceRateTO.getRoiDate().getDate() > 0) {
                    Dt.setDate(objServiceTaxMaintenanceRateTO.getRoiDate().getDate());
                    Dt.setMonth(objServiceTaxMaintenanceRateTO.getRoiDate().getMonth());
                    Dt.setYear(objServiceTaxMaintenanceRateTO.getRoiDate().getYear());
                    objServiceTaxMaintenanceRateTO.setRoiDate(Dt);

                }
                if (objServiceTaxMaintenanceRateTO.getRoiEndDate() != null && objServiceTaxMaintenanceRateTO.getRoiEndDate().getDate() > 0) {
                    Dt = (Date) curDate.clone();
                    Dt.setDate(objServiceTaxMaintenanceRateTO.getRoiEndDate().getDate());
                    Dt.setMonth(objServiceTaxMaintenanceRateTO.getRoiEndDate().getMonth());
                    Dt.setYear(objServiceTaxMaintenanceRateTO.getRoiEndDate().getYear());
                    objServiceTaxMaintenanceRateTO.setRoiEndDate(Dt);
                }
//                Dt=null;
                objServiceTaxMaintenanceRateTO = (ServiceTaxMaintenanceRateTO) interRateData.get(k);
                objServiceTaxMaintenanceRateTO.setRoiGroupId(ID);
                objServiceTaxMaintenanceRateTO.setStatus(Status);
                sqlMap.executeUpdate("insertServiceTaxMaintenanceRateTO", objServiceTaxMaintenanceRateTO);

                objLogTO.setStatus(command);
                objLogTO.setData(objServiceTaxMaintenanceRateTO.toString());
                objLogTO.setPrimaryKey(objServiceTaxMaintenanceRateTO.getKeyData());

                objLogDAO.addToLog(objLogTO);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
    /*
     * To delete the rows from the Interest rate table...
     */

    private void deleteInterTableRow(String ID) throws Exception {
        int interTypeDelete = interRateDeleteData.size();
        for (int i = 0; i < interTypeDelete; i++) {
            objServiceTaxMaintenanceRateTO = (ServiceTaxMaintenanceRateTO) interRateDeleteData.get(i);
            objServiceTaxMaintenanceRateTO.setRoiGroupId(ID);
            objServiceTaxMaintenanceRateTO.setStatus(CommonConstants.STATUS_DELETED);
            sqlMap.executeUpdate("insertServiceTaxMaintenanceRateTO", objServiceTaxMaintenanceRateTO);
        }
    }

    private void deleteData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();
            if (agentsMap.containsKey("AGENTS_COMMISION")) {
//                agentsCommisionRateUpdate(CommonConstants.STATUS_DELETED);
                deleteAgentsTableRow(agentsList);
            } else {
                final String ROIID = CommonUtil.convertObjToStr(objServiceTaxMaintenanceGroupTO.getRoiGroupName());

                System.out.println("start deleteData");
                objServiceTaxMaintenanceGroupTO.setStatusBy(userID);
                objServiceTaxMaintenanceGroupTO.setStatus(CommonConstants.STATUS_DELETED);
                objServiceTaxMaintenanceGroupTO.setStatusDt(curDate);

                sqlMap.executeUpdate("deleteServiceTaxMaintenanceGroupTO", objServiceTaxMaintenanceGroupTO);

                objLogTO.setStatus(command);
                objLogTO.setData(objServiceTaxMaintenanceGroupTO.toString());
                objLogTO.setPrimaryKey(objServiceTaxMaintenanceGroupTO.getKeyData());

                objLogDAO.addToLog(objLogTO);

                updateDeleteDataTab(ROIID, CommonConstants.STATUS_DELETED, objLogDAO, objLogTO);
                System.out.println("commit deleteData");
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            ServiceTaxMaintenanceGroupDAO dao = new ServiceTaxMaintenanceGroupDAO();
            ServiceTaxMaintenanceGroupTO objServiceTaxMaintenanceGroupTO = new ServiceTaxMaintenanceGroupTO();
            ServiceTaxMaintenanceCategotyTO objServiceTaxMaintenanceCategotyTO = new ServiceTaxMaintenanceCategotyTO();
            ServiceTaxMaintenanceProdTO objServiceTaxMaintenanceProdTO = new ServiceTaxMaintenanceProdTO();
            ServiceTaxMaintenanceTypeTO objServiceTaxMaintenanceTypeTO = new ServiceTaxMaintenanceTypeTO();
            ServiceTaxMaintenanceRateTO objServiceTaxMaintenanceRateTO = new ServiceTaxMaintenanceRateTO();

            /*
             * To tell what to do... Insert, Update, Delete...
             */
            TOHeader toHeader = new TOHeader();
            toHeader.setCommand(CommonConstants.TOSTATUS_INSERT);
            objServiceTaxMaintenanceGroupTO.setTOHeader(toHeader);

            objServiceTaxMaintenanceGroupTO.setRoiGroupName("SPECIAL");

            objServiceTaxMaintenanceCategotyTO.setCategoryId("CAT");
            //objServiceTaxMaintenanceCategotyTO.setStatus (getTxtStatus());

//            objServiceTaxMaintenanceProdTO.setProdId("PROD");
            //objServiceTaxMaintenanceProdTO.setStatus ("");

            objServiceTaxMaintenanceTypeTO.setRateTypeId("RATE");
            //objServiceTaxMaintenanceTypeTO.setStatus ("");

            objServiceTaxMaintenanceRateTO.setRateTypeId("RATE");
            objServiceTaxMaintenanceRateTO.setRoiDate(DateUtil.getDateMMDDYYYY(null));
            objServiceTaxMaintenanceRateTO.setFromAmount(new Double(100));
            objServiceTaxMaintenanceRateTO.setToAmount(new Double(1000));
            objServiceTaxMaintenanceRateTO.setFromPeriod(new Double(1));
            objServiceTaxMaintenanceRateTO.setToPeriod(new Double(21));
            objServiceTaxMaintenanceRateTO.setServiceTax(new Double(1.09));
            objServiceTaxMaintenanceRateTO.setCess1Tax(new Double(1.23));
            //objServiceTaxMaintenanceRateTO.setStatus (getTxtStatus());

            HashMap hash = new HashMap();
            hash.put("ServiceTaxMaintenanceGroupTO", objServiceTaxMaintenanceGroupTO);
            hash.put("ServiceTaxMaintenanceCategotyTO", objServiceTaxMaintenanceCategotyTO);
            hash.put("ServiceTaxMaintenanceProdTO", objServiceTaxMaintenanceProdTO);
            hash.put("ServiceTaxMaintenanceTypeTO", objServiceTaxMaintenanceTypeTO);
            hash.put("ServiceTaxMaintenanceRateTO", objServiceTaxMaintenanceRateTO);

            dao.execute(hash);
            /*
             * Destroy the object after its use.
             */
            hash = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /*
     * final Object[] keyList = (interMap.keySet()).toArray(); final int
     * keyListLength = keyList.length; issueMap = (LinkedHashMap)
     * map.get("REMITISSUE"); objRemittanceIssueTO =
     * (RemittanceIssueTO)issueMap.get((String)(keyList[i]));
     */

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        // Log DAO
        LogDAO objLogDAO = new LogDAO();
        HashMap resultMap = null;
        curDate = ServerUtil.getCurrentDate(_branchCode);
//         resultMap=serviceTaxCalculation(null);
        System.out.println("######Execute" + map);
        if (map.containsKey("ST_CAL")) {
//              curDate=ServerUtil.getCurrentDate(_branchCode);
            resultMap = serviceTaxCalculation(map);
        } else {
            // Log Transfer Object
            LogTO objLogTO = new LogTO();
            objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
            objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
            objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
            objLogTO.setModule((String) map.get(CommonConstants.MODULE));
            objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));


            userID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));

            objServiceTaxMaintenanceGroupTO = (ServiceTaxMaintenanceGroupTO) map.get("ServiceTaxMaintenanceGroupTO");

            productData = (ArrayList) map.get("ServiceTaxMaintenanceProdTO");
            categoryData = (ArrayList) map.get("ServiceTaxMaintenanceCategotyTO");
            interRateData = (ArrayList) map.get("ServiceTaxMaintenanceRateTO");
            if (map.containsKey("InterestDeleteTabRow")) {
                interRateDeleteData = (ArrayList) map.get("InterestDeleteTabRow");
            }
            if (map.containsKey("AGENTS_COMMISION")) {
                agentsList = (ArrayList) map.get("AGENTS_COMMISION");
                command = CommonUtil.convertObjToStr(map.get("COMMAND"));
                agentsMap.put("AGENTS_COMMISION", map.get("AGENTS_COMMISION"));
                prodId = CommonUtil.convertObjToStr(map.get("PROD_MAP"));
                System.out.println("agentsCommision : " + command);
                System.out.println("interestMaintanaceAgentsCommision : " + agentsList);
            } else {
                command = objServiceTaxMaintenanceGroupTO.getCommand();
            }
            try {
                //            sqlMap.startTransaction();

                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertData(objLogDAO, objLogTO);
                    resultMap = new HashMap();
                    if (objServiceTaxMaintenanceGroupTO != null) {
                        resultMap.put("ROI GROUPID", objServiceTaxMaintenanceGroupTO.getRoiGroupId());
                    }
                    System.out.println("$$$$GROUP" + resultMap);
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData(objLogDAO, objLogTO);

                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData(objLogDAO, objLogTO);

                } else {
                    throw new NoCommandException();
                }

                //            sqlMap.commitTransaction();
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw e;
            }
            objLogDAO = null;
            objLogTO = null;
            destroyObjects();
        }
        return resultMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        curDate = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    public HashMap serviceTaxCalculation(HashMap stCalcMap) throws Exception {
        HashMap calMap = new HashMap();
        double calAmt = CommonUtil.convertObjToDouble(stCalcMap.get("CAL_AMT")).doubleValue();
//        calAmt=CommonUtil.convertObjToDouble(stCalcMap.get("CAL_AMT")).doubleValue();
//        String chgType =CommonUtil.convertObjToStr(stCalcMap.get("CHARGE_TYPE"));
        String chgType = CommonUtil.convertObjToStr(stCalcMap.get("CHARGE_TYPE"));
//        calAmt=15.0;
        Date curDt = (Date) curDate.clone();
        double cess1Tax = 0.0;
        double cess2Tax = 0.0;
        double serviceTax = 0.0;
        double stTax = 0.0;
//        HashMap calMap=new HashMap();
        calMap.put("CAL_AMT", new Double(calAmt));
        calMap.put("CURR_DT", curDt);
        calMap.put("CHARGE_TYPE", chgType);
        System.out.println("#$#$ calMap : " + calMap);
        List calLst = ServerUtil.executeQuery("getServiceTaxRate", calMap);
        if (calLst != null && calLst.size() > 0) {
            calMap = (HashMap) calLst.get(0);
            stTax = CommonUtil.convertObjToDouble(calMap.get("SERVICE_TAX")).doubleValue();
            System.out.println("calMap------------->" + calMap);
            if (CommonUtil.convertObjToStr(calMap.get("CESS1_TAX")).length() > 0 && !CommonUtil.convertObjToStr(calMap.get("CESS1_TAX")).equals("")) {
                if (CommonUtil.convertObjToStr(calMap.get("SERVICE_TAX_HD_ID")).equals(CommonUtil.convertObjToStr(calMap.get("CESS1_TAX_HD_ID")))) {
                    stTax = stTax + CommonUtil.convertObjToDouble(calMap.get("CESS1_TAX")).doubleValue();
                } else {
                    cess1Tax = (calAmt * CommonUtil.convertObjToDouble(calMap.get("CESS1_TAX")).doubleValue()) / 100;
                }
            }
            if (CommonUtil.convertObjToStr(calMap.get("CESS2_TAX")).length() > 0 && !CommonUtil.convertObjToStr(calMap.get("CESS2_TAX")).equals("")) {
                if (CommonUtil.convertObjToStr(calMap.get("SERVICE_TAX_HD_ID")).equals(CommonUtil.convertObjToStr(calMap.get("CESS2_TAX_HD_ID")))) {
                    stTax = stTax + CommonUtil.convertObjToDouble(calMap.get("CESS2_TAX")).doubleValue();
                } else {
                    cess2Tax = (calAmt * CommonUtil.convertObjToDouble(calMap.get("CESS2_TAX")).doubleValue()) / 100;
                }
            }
            if (CommonUtil.convertObjToStr(calMap.get("SERVICE_TAX_HD_ID")).length() > 0 && !CommonUtil.convertObjToStr(calMap.get("SERVICE_TAX_HD_ID")).equals("")) {

                serviceTax = (stTax * calAmt) / 100;
                serviceTax = (double) getNearest((long) (serviceTax * 100), 100) / 100;

            }


        }
        System.out.println("serviceTax---->" + serviceTax);
        System.out.println("cess2Tax---->" + cess2Tax);
        System.out.println("cess1Tax---->" + cess1Tax);
        System.out.println("stTax---->" + stTax);
//        System.out.println("serviceTax---->"+serviceTax);  



        if (stCalcMap.containsKey("CALCULATION_TYPE") && CommonUtil.convertObjToStr(stCalcMap.get("CALCULATION_TYPE")).equals("AFTER_AUTH")) {
            if (serviceTax > 0) {
                HashMap map = new HashMap();
                TransferTrans trans = new TransferTrans();
                map.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(stCalcMap.get("PROD_TYPE")));
                map.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(stCalcMap.get("PROD_ID")));
                map.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(stCalcMap.get("ACT_NUM")));
                map.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(stCalcMap.get("AC_HD_ID")));
                map.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(stCalcMap.get("BRANCH_ID")));
                map.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(stCalcMap.get("BRANCH_ID")));
                map.put(TransferTrans.CURRENCY, LocaleConstants.DEFAULT_CURRENCY);
                map.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(calMap.get("SERVICE_TAX_HD_ID")));
                map.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL); // gl
                map.put(TransferTrans.PARTICULARS, stCalcMap.get(TransferTrans.PARTICULARS));
                trans.setInitiatedBranch(CommonUtil.convertObjToStr(stCalcMap.get("BRANCH_ID")));
                trans.setLinkBatchId(CommonUtil.convertObjToStr(stCalcMap.get("ACT_NUM")));
                ArrayList batchList = new ArrayList();
                batchList.add(trans.getDebitTransferTO(map, serviceTax));
                batchList.add(trans.getCreditTransferTO(map, serviceTax));
                String branchID = CommonUtil.convertObjToStr(stCalcMap.get("BRANCH_ID"));
                trans.doDebitCredit(batchList, branchID);
                batchList = null;
            }
        }
        calMap.put("SERVICE_TAX", new Double(serviceTax));
        calMap.put("CESS1_TAX", new Double(cess1Tax));
        calMap.put("CESS2_TAX", new Double(cess2Tax));
        System.out.println("calMap-------->" + calMap);
        return calMap;
    }

    private void destroyObjects() {
        objServiceTaxMaintenanceGroupTO = null;
        objServiceTaxMaintenanceCategotyTO = null;
        objServiceTaxMaintenanceProdTO = null;
        objServiceTaxMaintenanceRateTO = null;

        productData = null;
        categoryData = null;
        interRateData = null;
        interRateDeleteData = null;
    }

    public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
        if ((mod <= (roundingFactor / 2)) || (mod <= (roundingFactorOdd / 2))) {
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
}
