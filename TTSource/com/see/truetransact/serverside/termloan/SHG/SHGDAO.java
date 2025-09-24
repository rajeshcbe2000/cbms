/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * OperativeAcctProductDAO.java
 *
 * Created on June 18, 2003, 4:14 PM
 */
package com.see.truetransact.serverside.termloan.SHG;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.transferobject.product.operativeacct.*;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.product.mds.MDSProductSchemeTO;
import com.see.truetransact.transferobject.product.mds.MDSProductAcctHeadTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;

import com.see.truetransact.transferobject.termloan.SHG.SHGTO;

/**
 * This is used for OperativeAcctProductDAO Data Access.
 *
 * @author Balachandar
 *
 * @modified Pinky @modified Rahul
 */
public class SHGDAO extends TTDAO {

    private final static Logger log = Logger.getLogger(SHGDAO.class);
    private SHGTO objSHGTO;
    private LinkedHashMap deletedTableValues = null;
    private LinkedHashMap tableDetails = null;
    private MDSProductAcctHeadTO acctHeadTo;
    private MDSProductSchemeTO schemeTo;
    private static SqlMap sqlMap = null;
    private Date CurrDt = null;
    private LogDAO logDAO;
    private LogTO logTO;

    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public SHGDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private String getSHGId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "SHG_ID");
        String shgId = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return shgId;
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        List shgList = (List) sqlMap.executeQueryForList("getSelectSHGTO", map);
        if (shgList != null && shgList.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
           // System.out.println("@@@shgList" + shgList);
            for (int i = shgList.size(), j = 0; i > 0; i--, j++) {
                String st = ((SHGTO) shgList.get(j)).getMemberNo();
                ParMap.put(((SHGTO) shgList.get(j)).getMemberNo(), shgList.get(j));
            }
            System.out.println("@@@ParMap" + ParMap);
            returnMap.put("SHGTO_DATA", ParMap);
        }
        return returnMap;
    }

    public static void main(String str[]) {
        try {
            SHGDAO dao = new SHGDAO();
            HashMap inputMap = new HashMap();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {

        System.out.println("Map in DAO: " + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        CurrDt = ServerUtil.getCurrentDate(_branchCode);
        HashMap returnMap = new HashMap();
        logDAO = new LogDAO();
        logTO = new LogTO();
        tableDetails = (LinkedHashMap) map.get("SHGTableDetails");
        deletedTableValues = (LinkedHashMap) map.get("deletedSHGTableDetails");
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData(map);
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

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            if (tableDetails != null) {
                String shgID = "";
                shgID = getSHGId();
                ArrayList addList = new ArrayList(tableDetails.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    SHGTO objSHGTO = (SHGTO) tableDetails.get(addList.get(i));
                    objSHGTO.setShgId(shgID);
                    sqlMap.executeUpdate("insertSHG", objSHGTO);
                    logTO.setData(objSHGTO.toString());
                    logTO.setPrimaryKey(objSHGTO.getKeyData());
                    logDAO.addToLog(logTO);
                    objSHGTO = null;
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            if (tableDetails != null) {
                System.out.println("######## tableDetails :" + tableDetails);
                ArrayList addList = new ArrayList(tableDetails.keySet());
                SHGTO objSHGTO = null;
                for (int i = 0; i < tableDetails.size(); i++) {
                    objSHGTO = new SHGTO();
                    objSHGTO = (SHGTO) tableDetails.get(addList.get(i));
                    objSHGTO.setStatusDt(CurrDt);
                    System.out.println("objSHGTO" + objSHGTO);
                    if (objSHGTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                        sqlMap.executeUpdate("insertSHG", objSHGTO);
                    } else {
                        sqlMap.executeUpdate("updateSHGTO", objSHGTO);
                    }
                }
            }
            if (deletedTableValues != null) {
                System.out.println("######## deletedTableValues :" + deletedTableValues);
                ArrayList addList = new ArrayList(deletedTableValues.keySet());
                SHGTO objSHGTO = null;
                for (int i = 0; i < deletedTableValues.size(); i++) {
                    objSHGTO = new SHGTO();
                    objSHGTO = (SHGTO) deletedTableValues.get(addList.get(i));
                    System.out.println("objSHGTO" + objSHGTO);
                    sqlMap.executeUpdate("deleteSHGMemberDetails", objSHGTO);
                }
            }

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void deleteData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            map.put("STATUS", CommonConstants.STATUS_DELETED);
            map.put("STATUS_DT", CurrDt);
            sqlMap.executeUpdate("deleteSHGTO", map);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void authorize(HashMap map) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        System.out.println("@@@@@@@AuthMap" + AuthMap);
        try {
            sqlMap.startTransaction();
            logTO.setData(map.toString());
            sqlMap.executeUpdate("authorizeSHGDetails", AuthMap);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        CurrDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objSHGTO = null;
    }
}
