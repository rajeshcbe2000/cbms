/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * UserDAO.java
 *
 * Created on September 08, 2003, 1:00 PM
 *
 * Modified on April 16, 2004, 10:42 AM
 *
 */
package com.see.truetransact.serverside.sysadmin.fixedassets;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.sysadmin.fixedassets.FixedAssetsDescriptionTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.commonutil.CommonUtil;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import java.util.Date;
/**
 * This is used for User Data Access.
 *
 * @author Karthik
 *
 * @modified Pinky
 */
public class FixedAssetsDescriptionDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private FixedAssetsDescriptionTO ObjEmpTO;
    private List list;
    // For Maintaining Logs...
    private LogDAO logDAO;
    private LogTO logTO;
    private LinkedHashMap deletedTableValues = null;
    private LinkedHashMap tableDetails = null;
    private String command = "";
    private final static Logger log = Logger.getLogger(FixedAssetsDescriptionDAO.class);
    private Date currDt = null;
    /**
     * Creates a new instance of roleDAO
     */
    public FixedAssetsDescriptionDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        //        List list = (List) sqlMap.executeQueryForList("getSelectEmpTrainingTO", map);
        //        returnMap.put("EmpTrainingTO", list);
        if (map.get("VIEW_TYPE").equals("Authorize")) {
            list = (List) sqlMap.executeQueryForList("getSelectFixedAssetDescAuthorize", map);
        } else {
            list = (List) sqlMap.executeQueryForList("getSelectFixedAssetDesc", map);
        }
        if (list != null && list.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            System.out.println("@@@list" + list);
            for (int i = list.size(), j = 0; i > 0; i--, j++) {
                String st = ((FixedAssetsDescriptionTO) list.get(j)).getSlNo();
                ParMap.put(((FixedAssetsDescriptionTO) list.get(j)).getSlNo(), list.get(j));
            }
            System.out.println("@@@ParMap" + ParMap);
            returnMap.put("FixedAssetsDescriptionTO", ParMap);
        }
        return returnMap;
    }

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            ObjEmpTO.setAssetDescID(getAssetDescID());
            logTO.setData(ObjEmpTO.toString());
            logTO.setPrimaryKey(ObjEmpTO.getKeyData());
            insertTableValues();
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private String getAssetDescID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "FIXED_ASSET_DESC");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            //            sqlMap.executeUpdate("updateFixedAssetDescTO",ObjEmpTO);
            logTO.setData(ObjEmpTO.toString());
            logTO.setPrimaryKey(ObjEmpTO.getKeyData());
            updateEmpTableValues();
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();
            ObjEmpTO.setAssetStatusDt(currDt);
            ObjEmpTO.setAssetStatus("DELETED");
            sqlMap.executeUpdate("deleteFixedAssetDescTO", ObjEmpTO);
            logTO.setData(ObjEmpTO.toString());
            logTO.setPrimaryKey(ObjEmpTO.getKeyData());
            //            deleteTableValues();
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("@@@@@@@ExecuteMap" + map);
        HashMap returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        logDAO = new LogDAO();
        logTO = new LogTO();
        deletedTableValues = (LinkedHashMap) map.get("deletedFixedAssetsDescription");
        tableDetails = (LinkedHashMap) map.get("FixedAssetsDescriptionTableDetails");
        ObjEmpTO = (FixedAssetsDescriptionTO) map.get("FixedAssetsDescription");
        command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
            returnMap.put("FIXED_ASSET_DESC", ObjEmpTO.getAssetDescID());
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap);
            }
        }
        map = null;
        destroyObjects();
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        ObjEmpTO = null;
        logTO = null;
        logDAO = null;
        command = "";
    }

    private void insertTableValues() throws Exception {

        if (tableDetails != null) {
            ArrayList addList = new ArrayList(tableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                FixedAssetsDescriptionTO objFixedAssetsDescriptionTO = (FixedAssetsDescriptionTO) tableDetails.get(addList.get(i));
                objFixedAssetsDescriptionTO.setAssetDescID(ObjEmpTO.getAssetDescID());
                objFixedAssetsDescriptionTO.setBranCode(ObjEmpTO.getBranCode());
                sqlMap.executeUpdate("insertFixedAssetDescDetailsTO", objFixedAssetsDescriptionTO);
                logTO.setData(objFixedAssetsDescriptionTO.toString());
                logTO.setPrimaryKey(objFixedAssetsDescriptionTO.getKeyData());
                logDAO.addToLog(logTO);
                objFixedAssetsDescriptionTO = null;
            }
        }
    }

    private void updateEmpTableValues() throws Exception {

        if (deletedTableValues != null) {
            ArrayList addList = new ArrayList(deletedTableValues.keySet());
            for (int i = 0; i < addList.size(); i++) {
                FixedAssetsDescriptionTO objFixedAssetsDescriptionTO = (FixedAssetsDescriptionTO) deletedTableValues.get(addList.get(i));
                objFixedAssetsDescriptionTO.setAssetStatus(CommonConstants.STATUS_DELETED);
                objFixedAssetsDescriptionTO.setAssetDescID(ObjEmpTO.getAssetDescID());
                sqlMap.executeUpdate("deleteFixedAssetDescTableValuesTO", objFixedAssetsDescriptionTO);
                logTO.setData(objFixedAssetsDescriptionTO.toString());
                logTO.setPrimaryKey(objFixedAssetsDescriptionTO.getKeyData());
                logDAO.addToLog(logTO);
                objFixedAssetsDescriptionTO = null;
            }
        }

        if (tableDetails != null) {
            List lst = null;
            ArrayList addList = new ArrayList(tableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                FixedAssetsDescriptionTO objFixedAssetsDescriptionTO = (FixedAssetsDescriptionTO) tableDetails.get(addList.get(i));
                logTO.setData(objFixedAssetsDescriptionTO.toString());
                logTO.setPrimaryKey(objFixedAssetsDescriptionTO.getKeyData());
                objFixedAssetsDescriptionTO.setAssetDescID(ObjEmpTO.getAssetDescID());
                objFixedAssetsDescriptionTO.setBranCode(ObjEmpTO.getBranCode());
                lst = sqlMap.executeQueryForList("countNoOfAssets", objFixedAssetsDescriptionTO);
                int a = CommonUtil.convertObjToInt(lst.get(0));
                if (a <= 0) {
                    sqlMap.executeUpdate("insertFixedAssetDescDetailsTO", objFixedAssetsDescriptionTO);
                } else {
                    sqlMap.executeUpdate("updateFixedAssetDescTO", objFixedAssetsDescriptionTO);
                }

                logDAO.addToLog(logTO);
            }
        }
    }

    private void deleteTableValues() throws Exception {

        if (tableDetails != null) {
            ArrayList addList = new ArrayList(tableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                FixedAssetsDescriptionTO objFixedAssetsDescriptionTO = (FixedAssetsDescriptionTO) tableDetails.get(addList.get(i));
                objFixedAssetsDescriptionTO.setAssetDescID(ObjEmpTO.getAssetDescID());
                objFixedAssetsDescriptionTO.setAssetStatus("DELETED");
                sqlMap.executeUpdate("deleteEmpDetailsTO", objFixedAssetsDescriptionTO);
                logTO.setData(objFixedAssetsDescriptionTO.toString());
                logTO.setPrimaryKey(objFixedAssetsDescriptionTO.getKeyData());
                logDAO.addToLog(logTO);
                objFixedAssetsDescriptionTO = null;
            }
        }
    }

    private void authorize(HashMap map) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        System.out.println("@@@@@@@AuthMap" + AuthMap);
        try {
            sqlMap.startTransaction();
            if (deletedTableValues != null) {
                System.out.println("Inside Authorize Delete");
                ArrayList addList = new ArrayList(deletedTableValues.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    FixedAssetsDescriptionTO objFixedAssetsDescriptionTO = (FixedAssetsDescriptionTO) deletedTableValues.get(addList.get(i));
                    AuthMap.put("SL_NO", objFixedAssetsDescriptionTO.getSlNo());
                    sqlMap.executeUpdate("authorizeFixedAssetDescDetails", AuthMap);
                    logTO.setData(objFixedAssetsDescriptionTO.toString());
                    logTO.setPrimaryKey(objFixedAssetsDescriptionTO.getKeyData());
                    logDAO.addToLog(logTO);
                }
            }
            if (tableDetails != null) {
                System.out.println("Inside Authorize NonDelete");
                List lst = null;
                ArrayList addList = new ArrayList(tableDetails.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    FixedAssetsDescriptionTO objFadTO = (FixedAssetsDescriptionTO) tableDetails.get(addList.get(i));
                    System.out.println("DATA CHECK" + objFadTO);
                    AuthMap.put("SL_NO", objFadTO.getSlNo());
                    sqlMap.executeUpdate("authorizeFixedAssetDescDetails", AuthMap);
                    logTO.setData(objFadTO.toString());
                    logTO.setPrimaryKey(objFadTO.getKeyData());
                    logDAO.addToLog(logTO);
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
}
