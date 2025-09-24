/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InterestMaintenanceGroupDAO.java
 *
 * Created on Tue May 25 10:39:44 IST 2004
 */
package com.see.truetransact.serverside.deposit.interestmaintenance;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.LinkedHashMap;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.commonutil.CommonUtil;

import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;

import com.see.truetransact.transferobject.deposit.interestmaintenance.InterestMaintenanceGroupTO;
import com.see.truetransact.transferobject.deposit.interestmaintenance.InterestMaintenanceCategotyTO;
import com.see.truetransact.transferobject.deposit.interestmaintenance.InterestMaintenanceProdTO;
import com.see.truetransact.transferobject.deposit.interestmaintenance.InterestMaintenanceTypeTO;
import com.see.truetransact.transferobject.deposit.interestmaintenance.InterestMaintenanceRateTO;

import com.see.truetransact.serverside.product.deposits.DepositsProductDAO;
// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

/**
 * InterestMaintenanceGroup DAO.
 *
 */
public class InterestMaintenanceGroupDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private InterestMaintenanceGroupTO objInterestMaintenanceGroupTO;
    private InterestMaintenanceCategotyTO objInterestMaintenanceCategotyTO;
    private InterestMaintenanceProdTO objInterestMaintenanceProdTO;
    private InterestMaintenanceRateTO objInterestMaintenanceRateTO;
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
    private Date curr_dt = null;
    private String DUPLICATE= "";

    /**
     * Creates a new instance of InterestMaintenanceGroupDAO
     */
    public InterestMaintenanceGroupDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectInterestMaintenanceGroupTO", where);
        returnMap.put("InterestMaintenanceGroupTO", list);
        list = (List) sqlMap.executeQueryForList("getSelectInterestMaintenanceCategotyTO", where);
        returnMap.put("InterestMaintenanceCategotyTO", list);
        list = (List) sqlMap.executeQueryForList("getSelectInterestMaintenanceProdTO", where);
        returnMap.put("InterestMaintenanceProdTO", list);
        //        list = (List) sqlMap.executeQueryForList("getSelectInterestMaintenanceTypeTO", where);
        //        returnMap.put("InterestMaintenanceTypeTO", list);
        HashMap whereMap = new HashMap();
        whereMap.put("ROI_GROUP_ID", where);
        whereMap.put("INT_TYPE", "D");
        list = (List) sqlMap.executeQueryForList("getSelectInterestMaintenanceRateTO", whereMap);
        returnMap.put("InterestMaintenanceDebitRateTO", list);
        whereMap.put("INT_TYPE", "C");
        list = (List) sqlMap.executeQueryForList("getSelectInterestMaintenanceRateTO", whereMap);
        returnMap.put("InterestMaintenanceCreditRateTO", list);
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;
    }

    private void insertData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        boolean duplicate = false;
        try {
            /*
             * To Set the RoiGroupId in all The Tables...
             */
            sqlMap.startTransaction();
            if (agentsMap.containsKey("AGENTS_COMMISION")) {
                agentsCommisionRateInsert(CommonConstants.STATUS_CREATED);
            } else {
                final String ROIID = getROIID();
                objInterestMaintenanceGroupTO.setRoiGroupId(ROIID);
                //__ To Set the Status in Table as Created
                objInterestMaintenanceGroupTO.setStatusBy(userID);
                objInterestMaintenanceGroupTO.setStatus(CommonConstants.STATUS_CREATED);
                objInterestMaintenanceGroupTO.setStatusDt(curr_dt);

                objInterestMaintenanceGroupTO.setCreatedBy(userID);
                objInterestMaintenanceGroupTO.setCreatedDt(curr_dt);
                /*
                 * To Verify the Prod->Category group Data for the Objects which
                 * are passed as ArrayList(s) and/or HashMap from the OB...
                 */
                System.out.println("###### roiid : " + ROIID);
                getDuplicate(ROIID);
                if(DUPLICATE!=null && DUPLICATE.length()>0){
                    final HashMap where = new HashMap();
                    where.put("ID_KEY", "DEPOSIT.INT_GRP_ID");
                    sqlMap.executeUpdate("updateIdGenerate", where);
                }
                
                System.out.println("DUPLICATE++++"+duplicate);

                objLogTO.setStatus(command);
                objLogTO.setData(objInterestMaintenanceGroupTO.toString());
                objLogTO.setPrimaryKey(objInterestMaintenanceGroupTO.getKeyData());

                objLogDAO.addToLog(objLogTO);
                /*
                 * To Enter the Data for the Objects which are passed as
                 * ArrayList(s) and/or HashMap from the OB...
                 */
//                if(duplicate==false)
//                {
//                     System.out.println("start insertData");
//                    sqlMap.executeUpdate("insertInterestMaintenanceGroupTO", objInterestMaintenanceGroupTO);
//                    insertDataTab(ROIID, CommonConstants.STATUS_CREATED, objLogDAO, objLogTO);
//                    System.out.println("commit insertData");
//                }
                if(DUPLICATE!=null&&DUPLICATE.equals("DUPLICATE"))
                {}
                else
                {
                     System.out.println("start insertData");
                    sqlMap.executeUpdate("insertInterestMaintenanceGroupTO", objInterestMaintenanceGroupTO);
                    insertDataTab(ROIID, CommonConstants.STATUS_CREATED, objLogDAO, objLogTO);
                    System.out.println("commit insertData");
                }
//                if(DUPLICATE.equals("")){
//                    System.out.println("start insertData");
//                    sqlMap.executeUpdate("insertInterestMaintenanceGroupTO", objInterestMaintenanceGroupTO);
//                    insertDataTab(ROIID, CommonConstants.STATUS_CREATED, objLogDAO, objLogTO);
//                    System.out.println("commit insertData");
//                }
                
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
                    str.append(CommonUtil.convertObjToStr(((InterestMaintenanceProdTO) productData.get(length)).getProdId()));
                    str.append(CommonUtil.convertObjToStr(((InterestMaintenanceCategotyTO) categoryData.get(catLen)).getCategoryId()));
                    prodList.add(str.toString());
                }
            }
            HashMap map = new HashMap();
            map.put("prodList", prodList);
            if (!groupId.equalsIgnoreCase("")) {
                map.put("ROIGROUPID", groupId);
            }

            map.put("PRODUCT_TYPE", objInterestMaintenanceGroupTO.getProductType());
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
                throw new TTException("DUPLICATE GROUP");
                
            }
        } catch (Exception e) {
            if (e instanceof TTException) {
                System.out.println("INSIDEif" + e);
                DUPLICATE = "DUPLICATE";
                //throw new TTException(e);
            } else {
                //throw new TransRollbackException(e);
            }
        }
    }

    private void agentsCommisionRateInsert(String status) throws Exception {
        int interRate = agentsList.size();
        for (int k = 0; k < interRate; k++) {
            objInterestMaintenanceRateTO = (InterestMaintenanceRateTO) agentsList.get(k);
            objInterestMaintenanceRateTO.setStatus(status);
            System.out.println("######agentsCommisionRateInsert : " + objInterestMaintenanceRateTO);
            sqlMap.executeUpdate("insertInterestMaintenanceRateTO", objInterestMaintenanceRateTO);
        }
    }

    private void insertDataTab(String ID, String status, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        int prod = productData.size();
        int category = categoryData.size();
        int interRate = interRateData.size();

        try {
            for (int i = 0; i < prod; i++) {
                objInterestMaintenanceProdTO = (InterestMaintenanceProdTO) productData.get(i);
                objInterestMaintenanceProdTO.setRoiGroupId(ID);
                objInterestMaintenanceProdTO.setStatus(status);
                sqlMap.executeUpdate("insertInterestMaintenanceProdTO", objInterestMaintenanceProdTO);

                objLogTO.setStatus(command);
                objLogTO.setData(objInterestMaintenanceProdTO.toString());
                objLogTO.setPrimaryKey(objInterestMaintenanceProdTO.getKeyData());

                objLogDAO.addToLog(objLogTO);
            }

            for (int j = 0; j < category; j++) {
                objInterestMaintenanceCategotyTO = (InterestMaintenanceCategotyTO) categoryData.get(j);
                objInterestMaintenanceCategotyTO.setRoiGroupId(ID);
                objInterestMaintenanceCategotyTO.setStatus(status);
                sqlMap.executeUpdate("insertInterestMaintenanceCategotyTO", objInterestMaintenanceCategotyTO);

                objLogTO.setStatus(command);
                objLogTO.setData(objInterestMaintenanceCategotyTO.toString());
                objLogTO.setPrimaryKey(objInterestMaintenanceCategotyTO.getKeyData());

                objLogDAO.addToLog(objLogTO);
            }

            for (int k = 0; k < interRate; k++) {
                objInterestMaintenanceRateTO = (InterestMaintenanceRateTO) interRateData.get(k);
                objInterestMaintenanceRateTO.setRoiGroupId(ID);
                objInterestMaintenanceRateTO.setStatus(status);
                sqlMap.executeUpdate("insertInterestMaintenanceRateTO", objInterestMaintenanceRateTO);

                objLogTO.setStatus(command);
                objLogTO.setData(objInterestMaintenanceRateTO.toString());
                objLogTO.setPrimaryKey(objInterestMaintenanceRateTO.getKeyData());

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
        if (prodList != null && prodList.size() > 0) {
            where = (HashMap) prodList.get(0);
            if (where.get("BEHAVES_LIKE").equals("RECURRING") || where.get("BEHAVES_LIKE").equals("DAILY")) {
                where.put("ROI_GROUP_ID", prodId);
                List lst = sqlMap.executeQueryForList("getSelectInterestMaintenanceRatewhere", where);
                if (lst != null && lst.size() > 0) {
                    for (int i = 0; i < lst.size(); i++) {
                        objInterestMaintenanceRateTO = (InterestMaintenanceRateTO) lst.get(i);
                        objInterestMaintenanceRateTO.setRoiGroupId(prodId);
                        System.out.println("#####agentsCommisionRateUpdate status : " + status);
                        sqlMap.executeUpdate("deleteInterestMaintenanceRateTO", objInterestMaintenanceRateTO);
                        System.out.println("#####agentsCommisionRateUpdate status : " + objInterestMaintenanceRateTO);
                    }
                    objInterestMaintenanceRateTO = null;
                }
            }
        }
        objInterestMaintenanceRateTO = new InterestMaintenanceRateTO();
        System.out.println("#####agentsCommisionRateUpdate : " + objInterestMaintenanceRateTO.getStatus());
        //        if(status.equals(objInterestMaintenanceRateTO.getStatus()))
        //        objInterestMaintenanceRateTO.setRoiGroupId(ID);
        //        sqlMap.executeUpdate("deleteInterestMaintenanceRateTO", objInterestMaintenanceRateTO);

        int interRate = agentsList.size();
        System.out.println("#####interRate : " + interRate + "agentsList :" + agentsList);
        for (int k = 0; k < interRate; k++) {
            objInterestMaintenanceRateTO = (InterestMaintenanceRateTO) agentsList.get(k);
            Date roiStDate = (Date) curr_dt.clone();
            Date StartDate = objInterestMaintenanceRateTO.getRoiDate();
            if (StartDate != null && StartDate.getDate() > 0) {
                roiStDate.setDate(StartDate.getDate());
                roiStDate.setMonth(StartDate.getMonth());
                roiStDate.setYear(StartDate.getYear());
                objInterestMaintenanceRateTO.setRoiDate(roiStDate);
            }
            System.out.println("#####agentsCommisionRateUpdate Edit : " + objInterestMaintenanceRateTO);
            objInterestMaintenanceRateTO.setRoiGroupId(prodId);
            objInterestMaintenanceRateTO.setStatus(CommonConstants.STATUS_MODIFIED);
            sqlMap.executeUpdate("insertInterestMaintenanceRateTO", objInterestMaintenanceRateTO);
        }
    }

    private void deleteAgentsTableRow(ArrayList agentsList) throws Exception {
        int interTypeDelete = agentsList.size();
        for (int i = 0; i < interTypeDelete; i++) {
            objInterestMaintenanceRateTO = (InterestMaintenanceRateTO) agentsList.get(i);
            //            objInterestMaintenanceRateTO.setRoiGroupId(ID);
            objInterestMaintenanceRateTO.setStatus(CommonConstants.STATUS_DELETED);
            sqlMap.executeUpdate("deleteInterestMaintenanceRateTO", objInterestMaintenanceRateTO);
            //            sqlMap.executeUpdate("insertInterestMaintenanceRateTO", objInterestMaintenanceRateTO);
        }
    }

    private void updateData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();

            if (agentsMap.containsKey("AGENTS_COMMISION")) {
                agentsCommisionRateUpdate(CommonConstants.STATUS_MODIFIED);
                //                deleteAgentsTableRow(agentsList);
            } else {
                final String ROIID = CommonUtil.convertObjToStr(objInterestMaintenanceGroupTO.getRoiGroupId());
                /*
                 * To Verify the Prod->Category group Data for the Objects which
                 * are passed as ArrayList(s) and/or HashMap from the OB...
                 */
                if (!agentsMap.containsKey("AGENTS_COMMISION")) {
                    getDuplicate(ROIID);
                }

                System.out.println("start updateData");

                objInterestMaintenanceGroupTO.setStatusBy(userID);
                objInterestMaintenanceGroupTO.setStatus(CommonConstants.STATUS_MODIFIED);
                objInterestMaintenanceGroupTO.setStatusDt(curr_dt);

                sqlMap.executeUpdate("updateInterestMaintenanceGroupTO", objInterestMaintenanceGroupTO);

                objLogTO.setStatus(command);
                objLogTO.setData(objInterestMaintenanceGroupTO.toString());
                objLogTO.setPrimaryKey(objInterestMaintenanceGroupTO.getKeyData());

                objLogDAO.addToLog(objLogTO);

                updateDeleteDataTab(ROIID, CommonConstants.STATUS_MODIFIED, objLogDAO, objLogTO);
                /*
                 * To delete the Rows from the Interest table...
                 */
                //deleteInterTableRow(ROIID);  Jeffin
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
            objInterestMaintenanceRateTO = (InterestMaintenanceRateTO) agentsList.get(k);
            objInterestMaintenanceRateTO.setStatus(CommonConstants.STATUS_DELETED);
            System.out.println("######agentsCommisionRateDelete : " + objInterestMaintenanceRateTO);
            sqlMap.executeUpdate("insertInterestMaintenanceRateTO", objInterestMaintenanceRateTO);
        }
    }

    private void updateDeleteDataTab(String ID, String Status, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        int prod = productData.size();
        int category = categoryData.size();
        int interRate = interRateData.size();

        objInterestMaintenanceProdTO = new InterestMaintenanceProdTO();
        objInterestMaintenanceProdTO.setRoiGroupId(ID);
        sqlMap.executeUpdate("deleteInterestMaintenanceProdTO", objInterestMaintenanceProdTO);

        objInterestMaintenanceCategotyTO = new InterestMaintenanceCategotyTO();
        objInterestMaintenanceCategotyTO.setRoiGroupId(ID);
        sqlMap.executeUpdate("deleteInterestMaintenanceCategotyTO", objInterestMaintenanceCategotyTO);

        objInterestMaintenanceRateTO = new InterestMaintenanceRateTO();
        objInterestMaintenanceRateTO.setRoiGroupId(ID);
        sqlMap.executeUpdate("deleteInterestMaintenanceRateTO", objInterestMaintenanceRateTO);

        try {
            for (int i = 0; i < prod; i++) {
                objInterestMaintenanceProdTO = (InterestMaintenanceProdTO) productData.get(i);
                objInterestMaintenanceProdTO.setRoiGroupId(ID);
                objInterestMaintenanceProdTO.setStatus(Status);
                sqlMap.executeUpdate("insertInterestMaintenanceProdTO", objInterestMaintenanceProdTO);

                objLogTO.setStatus(command);
                objLogTO.setData(objInterestMaintenanceProdTO.toString());
                objLogTO.setPrimaryKey(objInterestMaintenanceProdTO.getKeyData());

                objLogDAO.addToLog(objLogTO);
            }

            for (int j = 0; j < category; j++) {
                objInterestMaintenanceCategotyTO = (InterestMaintenanceCategotyTO) categoryData.get(j);
                objInterestMaintenanceCategotyTO.setRoiGroupId(ID);
                objInterestMaintenanceCategotyTO.setStatus(Status);
                sqlMap.executeUpdate("insertInterestMaintenanceCategotyTO", objInterestMaintenanceCategotyTO);

                objLogTO.setStatus(command);
                objLogTO.setData(objInterestMaintenanceCategotyTO.toString());
                objLogTO.setPrimaryKey(objInterestMaintenanceCategotyTO.getKeyData());

                objLogDAO.addToLog(objLogTO);
            }

            for (int k = 0; k < interRate; k++) {
                objInterestMaintenanceRateTO = (InterestMaintenanceRateTO) interRateData.get(k);
                objInterestMaintenanceRateTO.setRoiGroupId(ID);
                objInterestMaintenanceRateTO.setStatus(Status);
                Date roiDate = (Date) objInterestMaintenanceRateTO.getRoiDate();
                Date roiEndDt = (Date) objInterestMaintenanceRateTO.getRoiEndDate();
                Date roiCreateDt = (Date) objInterestMaintenanceRateTO.getCreateDt();
                objInterestMaintenanceRateTO.setRoiDate(setproperDate(objInterestMaintenanceRateTO.getRoiDate()));
                objInterestMaintenanceRateTO.setRoiEndDate(setproperDate(objInterestMaintenanceRateTO.getRoiEndDate()));
                objInterestMaintenanceRateTO.setCreateDt(setproperDate(objInterestMaintenanceRateTO.getCreateDt()));
                System.out.println("objInterestMaintenanceRateTO="+objInterestMaintenanceRateTO);
                //sqlMap.executeUpdate("updateDepositRoiGroupTypeRate", objInterestMaintenanceRateTO);
                sqlMap.executeUpdate("insertInterestMaintenanceRateTO", objInterestMaintenanceRateTO);

                objLogTO.setStatus(command);
                objLogTO.setData(objInterestMaintenanceRateTO.toString());
                objLogTO.setPrimaryKey(objInterestMaintenanceRateTO.getKeyData());

                objLogDAO.addToLog(objLogTO);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private Date setproperDate(Date roiDt) {
        if (roiDt != null) {
            Date roiCurrDt = (Date) curr_dt.clone();
            roiCurrDt.setDate(roiDt.getDate());
            roiCurrDt.setMonth(roiDt.getMonth());
            roiCurrDt.setYear(roiDt.getYear());
            return (roiCurrDt);
        }
        return null;
    }

    /*
     * To delete the rows from the Interest rate table...
     */
    private void deleteInterTableRow(String ID) throws Exception {
        int interTypeDelete = interRateDeleteData.size();
        for (int i = 0; i < interTypeDelete; i++) {
            objInterestMaintenanceRateTO = (InterestMaintenanceRateTO) interRateDeleteData.get(i);
            objInterestMaintenanceRateTO.setRoiGroupId(ID);
            objInterestMaintenanceRateTO.setStatus(CommonConstants.STATUS_DELETED);
            objInterestMaintenanceRateTO.setRoiDate(setproperDate(objInterestMaintenanceRateTO.getRoiDate()));
            objInterestMaintenanceRateTO.setCreateDt(setproperDate(objInterestMaintenanceRateTO.getCreateDt()));
            objInterestMaintenanceRateTO.setRoiEndDate(setproperDate(objInterestMaintenanceRateTO.getRoiEndDate()));
            sqlMap.executeUpdate("insertInterestMaintenanceRateTO", objInterestMaintenanceRateTO);
        }
    }

    private void deleteData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();
            if (agentsMap.containsKey("AGENTS_COMMISION")) {
                //                agentsCommisionRateUpdate(CommonConstants.STATUS_DELETED);
                deleteAgentsTableRow(agentsList);
            } else {
                final String ROIID = CommonUtil.convertObjToStr(objInterestMaintenanceGroupTO.getRoiGroupId());

                System.out.println("start deleteData");
                objInterestMaintenanceGroupTO.setStatusBy(userID);
                objInterestMaintenanceGroupTO.setStatus(CommonConstants.STATUS_DELETED);
                objInterestMaintenanceGroupTO.setStatusDt(curr_dt);

                sqlMap.executeUpdate("deleteInterestMaintenanceGroupTO", objInterestMaintenanceGroupTO);

                objLogTO.setStatus(command);
                objLogTO.setData(objInterestMaintenanceGroupTO.toString());
                objLogTO.setPrimaryKey(objInterestMaintenanceGroupTO.getKeyData());

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
            InterestMaintenanceGroupDAO dao = new InterestMaintenanceGroupDAO();
            InterestMaintenanceGroupTO objInterestMaintenanceGroupTO = new InterestMaintenanceGroupTO();
            InterestMaintenanceCategotyTO objInterestMaintenanceCategotyTO = new InterestMaintenanceCategotyTO();
            InterestMaintenanceProdTO objInterestMaintenanceProdTO = new InterestMaintenanceProdTO();
            InterestMaintenanceTypeTO objInterestMaintenanceTypeTO = new InterestMaintenanceTypeTO();
            InterestMaintenanceRateTO objInterestMaintenanceRateTO = new InterestMaintenanceRateTO();

            /*
             * To tell what to do... Insert, Update, Delete...
             */
            TOHeader toHeader = new TOHeader();
            toHeader.setCommand(CommonConstants.TOSTATUS_INSERT);
            objInterestMaintenanceGroupTO.setTOHeader(toHeader);

            objInterestMaintenanceGroupTO.setRoiGroupName("SPECIAL");

            objInterestMaintenanceCategotyTO.setCategoryId("CAT");
            //objInterestMaintenanceCategotyTO.setStatus (getTxtStatus());

            objInterestMaintenanceProdTO.setProdId("PROD");
            //objInterestMaintenanceProdTO.setStatus ("");

            objInterestMaintenanceTypeTO.setRateTypeId("RATE");
            //objInterestMaintenanceTypeTO.setStatus ("");

            objInterestMaintenanceRateTO.setRateTypeId("RATE");
            objInterestMaintenanceRateTO.setRoiDate(DateUtil.getDateMMDDYYYY(null));
            objInterestMaintenanceRateTO.setFromAmount(new Double(100));
            objInterestMaintenanceRateTO.setToAmount(new Double(1000));
            objInterestMaintenanceRateTO.setFromPeriod(new Double(1));
            objInterestMaintenanceRateTO.setToPeriod(new Double(21));
            objInterestMaintenanceRateTO.setRoi(new Double(1.09));
            objInterestMaintenanceRateTO.setPenalInt(new Double(1.23));
            //objInterestMaintenanceRateTO.setStatus (getTxtStatus());

            HashMap hash = new HashMap();
            hash.put("InterestMaintenanceGroupTO", objInterestMaintenanceGroupTO);
            hash.put("InterestMaintenanceCategotyTO", objInterestMaintenanceCategotyTO);
            hash.put("InterestMaintenanceProdTO", objInterestMaintenanceProdTO);
            hash.put("InterestMaintenanceTypeTO", objInterestMaintenanceTypeTO);
            hash.put("InterestMaintenanceRateTO", objInterestMaintenanceRateTO);

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
        //boolean duplicate = false;
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        curr_dt = ServerUtil.getCurrentDateProperFormat(_branchCode);
        // Log DAO
        LogDAO objLogDAO = new LogDAO();
        HashMap resultMap = null;
        System.out.println("######Execute" + map);
        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));


        userID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));

        objInterestMaintenanceGroupTO = (InterestMaintenanceGroupTO) map.get("InterestMaintenanceGroupTO");

        productData = (ArrayList) map.get("InterestMaintenanceProdTO");
        categoryData = (ArrayList) map.get("InterestMaintenanceCategotyTO");
        interRateData = (ArrayList) map.get("InterestMaintenanceRateTO");
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
            command = objInterestMaintenanceGroupTO.getCommand();
        }
        try {
            //            sqlMap.startTransaction();

            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                resultMap = new HashMap();
                insertData(objLogDAO, objLogTO);
                if(DUPLICATE!=null){
                    resultMap.put(DUPLICATE, DUPLICATE);
                }
                DUPLICATE = null;
                if (objInterestMaintenanceGroupTO != null) {
                    resultMap.put("ROI GROUPID", objInterestMaintenanceGroupTO.getRoiGroupId());
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
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return resultMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        curr_dt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objInterestMaintenanceGroupTO = null;
        objInterestMaintenanceCategotyTO = null;
        objInterestMaintenanceProdTO = null;
        objInterestMaintenanceRateTO = null;

        productData = null;
        categoryData = null;
        interRateData = null;
        interRateDeleteData = null;
    }
}
