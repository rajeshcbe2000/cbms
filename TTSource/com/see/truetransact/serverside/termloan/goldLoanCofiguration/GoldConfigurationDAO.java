/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * GoldConfigurationDAO.java
 *
 * Created on Wed Feb 02 13:11:28 IST 2005
 */
package com.see.truetransact.serverside.termloan.goldLoanCofiguration;

import java.util.List;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Date;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.termloan.goldLoanConfiguration.GoldConfigurationTO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.commonutil.TTException;

/**
 * GoldConfiguration DAO.
 *
 */
public class GoldConfigurationDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private GoldConfigurationTO objGoldConfigurationTO;
    private String userID = "";
    private String remarks = "";
    private long referenceNo = 0;
    private long setNo = 0;
    private Date currDt = null;
    private ArrayList overAllList = new ArrayList();
    private ArrayList deleteAllList = new ArrayList();

    /**
     * Creates a new instance of GoldConfigurationDAO
     */
    public GoldConfigurationDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("######map" + map);
        HashMap returnMap = new HashMap();
        String where = "";
        if (map.containsKey("SET_NO")) {
            where = ((java.math.BigDecimal) (map.get("SET_NO"))).toString();
        }
        List list = (List) sqlMap.executeQueryForList("getSelectGoldConfiguration", where);
        returnMap.put("GoldConfigurationTO", list);
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;
    }

    private void insertData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            if (overAllList != null && overAllList.size() > 0) {
                sqlMap.startTransaction();
                Date toDate = null;
                for (int i = 0; i < overAllList.size(); i++) {
                    objGoldConfigurationTO = (GoldConfigurationTO) overAllList.get(i);
                    if (i == 0) {
                        toDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objGoldConfigurationTO.getFromDate()));
                        if (toDate != null) {
                            Date curDate = (Date) currDt.clone();
                            curDate.setDate(toDate.getDate());
                            curDate.setMonth(toDate.getMonth());
                            curDate.setYear(toDate.getYear());
                            objGoldConfigurationTO.setFromDate(DateUtil.getDateWithoutMinitues(curDate));
                            System.out.println("Insert curDate : " + curDate + "TO Value : " + objGoldConfigurationTO.getFromDate());
                        }
                    }
                    objGoldConfigurationTO.setReferenceNo(new Long(referenceNo));
                    objGoldConfigurationTO.setSetNo(new Long(setNo));
                    sqlMap.executeUpdate("insertGoldConfigurationTO", objGoldConfigurationTO);
                    //            objLogTO.setData(objGoldConfigurationTO.toString());
                    //            objLogTO.setPrimaryKey(objGoldConfigurationTO.getKeyData());
                    objLogDAO.addToLog(objLogTO);
                }
                long updateNo = 0;
                if (referenceNo > 1) {
                    updateNo = referenceNo - 1;
                } else {
                    updateNo = referenceNo;
                }
                Date existingFromDt = null;
                HashMap existingRecords = new HashMap();
                existingRecords.put("SET_NO", new Long(updateNo));
                List lst = sqlMap.executeQueryForList("getSelectExistingFromDate", existingRecords);
                if (lst != null && lst.size() > 0) {
                    existingRecords = (HashMap) lst.get(0);
                    existingFromDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(existingRecords.get("FROM_DATE")));
                }
                if (referenceNo > 1 && existingFromDt != null) {
                    if (DateUtil.dateDiff(existingFromDt, toDate) > 0) {
                        toDate = DateUtil.addDays(toDate, -1);
                    } else {
                        toDate = existingFromDt;
                    }
                    java.util.Hashtable updateToDate = new java.util.Hashtable();
                    updateToDate.put("TO_DATE", toDate);
                    updateToDate.put("SET_NO", new Long(updateNo));
                    updateToDate.put("BRANCH_CODE", _branchCode);
                    sqlMap.executeUpdate("updateToDate", updateToDate);
                    updateToDate = null;
                }
                sqlMap.commitTransaction();
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            if (overAllList != null && overAllList.size() > 0) {
                sqlMap.startTransaction();
                Date toDate = null;
                for (int i = 0; i < overAllList.size(); i++) {
                    objGoldConfigurationTO = (GoldConfigurationTO) overAllList.get(i);
                    objGoldConfigurationTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objGoldConfigurationTO.setStatusBy(userID);
                    objGoldConfigurationTO.setStatusDt(currDt);
                    Date AppDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objGoldConfigurationTO.getFromDate()));
                    if (i == 0) {
                        toDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objGoldConfigurationTO.getFromDate()));
                        if (toDate != null) {
                            Date curDate = (Date) currDt.clone();
                            curDate.setDate(toDate.getDate());
                            curDate.setMonth(toDate.getMonth());
                            curDate.setYear(toDate.getYear());
                            objGoldConfigurationTO.setFromDate(DateUtil.getDateWithoutMinitues(curDate));
                            System.out.println("Update curDate : " + curDate + "TO Value : " + objGoldConfigurationTO.getFromDate());
                        }
                    }
                    System.out.println("####### objGoldConfigurationTO" + objGoldConfigurationTO);
                    sqlMap.executeUpdate("updateGoldConfigurationTO", objGoldConfigurationTO);
                    //            objLogTO.setData(objGoldConfigurationTO.toString());
                    //            objLogTO.setPrimaryKey(objGoldConfigurationTO.getKeyData());
                    objLogDAO.addToLog(objLogTO);
                }

                if (referenceNo > 1) {
                    long updateNo = 0;
                    if (referenceNo > 1) {
                        updateNo = referenceNo - 1;
                    } else {
                        updateNo = referenceNo;
                    }
                    if (updateNo > 0) {
                        toDate = DateUtil.addDays(toDate, -1);
                        Hashtable updateToDate = new Hashtable();
                        updateToDate.put("TO_DATE", toDate);
                        updateToDate.put("SET_NO", new Long(updateNo));
                        updateToDate.put("BRANCH_CODE", _branchCode);
                        sqlMap.executeUpdate("updateToDate", updateToDate);
                        updateToDate = null;
                    }
                }
                sqlMap.commitTransaction();
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            if (overAllList != null && overAllList.size() > 0) {
                sqlMap.startTransaction();
                for (int i = 0; i < overAllList.size(); i++) {
                    objGoldConfigurationTO = (GoldConfigurationTO) overAllList.get(i);
                    objGoldConfigurationTO.setStatus(CommonConstants.STATUS_DELETED);
                    objGoldConfigurationTO.setStatusBy(userID);
                    objGoldConfigurationTO.setStatusDt(currDt);
                    sqlMap.executeUpdate("deleteGoldConfigurationTO", objGoldConfigurationTO);
                    //            objLogTO.setData(objGoldConfigurationTO.toString());
                    //            objLogTO.setPrimaryKey(objGoldConfigurationTO.getKeyData());
                    objLogDAO.addToLog(objLogTO);
                }
                sqlMap.commitTransaction();
            }
            Hashtable deleteMap = new Hashtable();
            long oldvalue = 0;
            if (referenceNo > 1) {
                oldvalue = (referenceNo - 1);
            } else {
                oldvalue = referenceNo;
            }
            deleteMap.put("SET_NO", String.valueOf(oldvalue));
            deleteMap.put("BRANCH_CODE", _branchCode);
            sqlMap.executeUpdate("updateNullValueforToDate", deleteMap);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            GoldConfigurationDAO goldConfigurationDAO = new GoldConfigurationDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("GoldConfiguration Map Dao : " + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
//        currDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("CURR_DT")));
        currDt = ServerUtil.getCurrentDate(_branchCode);
        HashMap returnMap = new HashMap();
        LogDAO objLogDAO = new LogDAO();// Log DAO
        LogTO objLogTO = new LogTO();// Log Transfer Object
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        objLogTO.setSelectedBranchId((String) map.get(CommonConstants.SELECTED_BRANCH_ID));
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));

        userID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));

        if (map.containsKey("GoldConfigurationTO")) {
            overAllList = (ArrayList) map.get("GoldConfigurationTO");
            deleteAllList = (ArrayList) map.get("deleteGoldConfigurationTO");
            final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
            referenceNo = CommonUtil.convertObjToLong(map.get("REFERENCE_NO"));
            setNo = CommonUtil.convertObjToLong(map.get("SET_NO"));
            if (referenceNo == 0) {
                referenceNo = CommonUtil.convertObjToLong(map.get("SET_NO"));
                setNo = CommonUtil.convertObjToLong(map.get("SET_NO"));
            }
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData(objLogDAO, objLogTO);
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData(objLogDAO, objLogTO);
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData(objLogDAO, objLogTO);
            } else {
                throw new NoCommandException();
            }
        }
        objLogDAO = null;
        objLogTO = null;
        destroyObjects();
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objGoldConfigurationTO = null;
        referenceNo = 0;
        setNo = 0;
        overAllList = null;
    }
}
