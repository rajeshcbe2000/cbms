/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LevelControlDAO.java
 *
 * Created on Tue Mar 02 12:54:53 IST 2004
 */
package com.see.truetransact.serverside.sysadmin.levelcontrol;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.transferobject.sysadmin.levelcontrol.LevelControlTO;
import java.util.Date;
/**
 * LevelControl DAO.
 *
 */
public class LevelControlDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LevelControlTO objLevelControlTO;
    private String userID = "";
    private String transID = "";
    private Date currDt = null;
    /**
     * Creates a new instance of LevelControlDAO
     */
    public LevelControlDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectLevelControlTO", where);
        returnMap.put("LevelControlTO", list);
        return returnMap;
    }

    private void insertData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();

            final String levelID = getLevelID();
            transID = levelID;

            objLevelControlTO.setLevelId(levelID);
            objLevelControlTO.setCreatedBy(userID);
            objLevelControlTO.setCreatedDt(currDt);
            objLevelControlTO.setStatusBy(userID);
            objLevelControlTO.setStatusDt(currDt);
            objLevelControlTO.setStatus(CommonConstants.STATUS_CREATED);
            sqlMap.executeUpdate("insertLevelControlTO", objLevelControlTO);

            objLogTO.setData(objLevelControlTO.toString());
            objLogTO.setPrimaryKey(objLevelControlTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private String getLevelID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", "LEVEL_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void updateData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();

            objLevelControlTO.setStatusBy(userID);
            objLevelControlTO.setStatusDt(currDt);
            objLevelControlTO.setStatus(CommonConstants.STATUS_MODIFIED);
            sqlMap.executeUpdate("updateLevelControlTO", objLevelControlTO);

            objLogTO.setData(objLevelControlTO.toString());
            objLogTO.setPrimaryKey(objLevelControlTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();

            objLevelControlTO.setStatusBy(userID);
            objLevelControlTO.setStatusDt(currDt);
            objLevelControlTO.setStatus(CommonConstants.STATUS_DELETED);
            sqlMap.executeUpdate("deleteLevelControlTO", objLevelControlTO);

            objLogTO.setData(objLevelControlTO.toString());
            objLogTO.setPrimaryKey(objLevelControlTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            LevelControlDAO dao = new LevelControlDAO();
            LevelControlTO objLevelControlTO = new LevelControlTO();

            TOHeader toHeader = new TOHeader();
            toHeader.setCommand(CommonConstants.TOSTATUS_DELETE);//To tell what to do... Insert, Update, Delete...

            objLevelControlTO.setTOHeader(toHeader);
            objLevelControlTO.setLevelId("L001");
            objLevelControlTO.setLevelDesc("FIRST");
            objLevelControlTO.setCashCredit(new Double(10));
            objLevelControlTO.setCashDebit(new Double(10));
            objLevelControlTO.setTransCredit(new Double(10));
            objLevelControlTO.setTransDebit(new Double(10));
            objLevelControlTO.setClearingCredit(new Double(10));
            objLevelControlTO.setClearingDebit(new Double(10));
            objLevelControlTO.setLevelName("RAHUL");

            HashMap hash = new HashMap();
            hash.put("LevelControlTO", objLevelControlTO);
            dao.execute(hash);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        HashMap execReturnMap = new HashMap();

        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        // Log DAO
        LogDAO objLogDAO = new LogDAO();

        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));

        userID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));


        objLevelControlTO = (LevelControlTO) map.get("LevelControlTO");
        final String command = objLevelControlTO.getCommand();
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(objLogDAO, objLogTO);
            execReturnMap.put(CommonConstants.TRANS_ID, transID);

        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(objLogDAO, objLogTO);

        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData(objLogDAO, objLogTO);

        } else {
            throw new NoCommandException();
        }

        objLogDAO = null;
        objLogTO = null;
        destroyObjects();


        return execReturnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objLevelControlTO = null;
    }
}
