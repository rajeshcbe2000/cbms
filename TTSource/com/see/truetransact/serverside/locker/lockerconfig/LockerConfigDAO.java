/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * TokenConfigDAO.java
 *
 * Created on Thu Jan 20 17:19:05 IST 2005
 */
package com.see.truetransact.serverside.locker.lockerconfig;

import java.util.List;
import java.util.ArrayList;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

import com.see.truetransact.transferobject.locker.lockerconfig.LockerConfigDetailsTO;
import com.see.truetransact.transferobject.locker.lockerconfig.LockerConfigTO;
import java.util.HashMap;
import java.util.Set;

/**
 * TokenConfig DAO.
 *
 */
public class LockerConfigDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LockerConfigTO objTO;
    private LockerConfigDetailsTO objLockerConfigDetailsTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private HashMap setMap;

    /**
     * Creates a new instance of TokenConfigDAO
     */
    public LockerConfigDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap where = (HashMap) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectLockerConfigTO", where);
        returnMap.put("LockerConfigTO", list);
        return returnMap;
    }

    private String getTokenConfigId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "TOKEN_CONFIG_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void insertData(String command) throws Exception {
        try {
            sqlMap.startTransaction();
//            objTO.setProdId();
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("insertLockerConfigTO", objTO);
            executeTabQuery(command);
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public void executeTabQuery(String command) throws Exception {

        LockerConfigDetailsTO objLockerConfigDetailsTO;
//        SettlementBankTO objSettlementBankTO;
        Set keySet;
        Object[] objKeySet;
        keySet = setMap.keySet();
        objKeySet = (Object[]) keySet.toArray();
        // To retrieve the TermLoanAuthorizedSignatoryTO from the authorizedMap
        for (int i = setMap.size() - 1, j = 0; i >= 0; --i, ++j) {
            objLockerConfigDetailsTO = (LockerConfigDetailsTO) setMap.get(objKeySet[j]);
            System.out.println("objTermLoanPowerAttorneyTO $$$$" + objLockerConfigDetailsTO);
            if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                objLockerConfigDetailsTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                objLockerConfigDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                objLockerConfigDetailsTO.setCommand(CommonConstants.TOSTATUS_UPDATE);
                objLockerConfigDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
                sqlMap.executeUpdate("updateLockerConfigDetailsTO", objLockerConfigDetailsTO);
            } else {
//            executeUpdate(getUpdateSetMap(), objSettlementTO, sqlMap);
                objLockerConfigDetailsTO.setBranchId(_branchCode);
                objLockerConfigDetailsTO.setLocStatus("AVAILABLE");
                objLockerConfigDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
                sqlMap.executeUpdate("insertLockerConfigDetailsTO", objLockerConfigDetailsTO);
            }
            objLockerConfigDetailsTO = null;
        }


//        System.out.println("setBankMap$$$$"+setBankMap);
//        objSettlementBankTO = (SettlementBankTO) setBankMap.get(String.valueOf(0));
//        System.out.println("objSettlementBankTO $$$$"+objSettlementBankTO);
//        if (getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
//            objSettlementBankTO.setCommand(CommonConstants.TOSTATUS_DELETE);
//        }
//        executeUpdate(getUpdateSetBnkMap(), objSettlementBankTO, sqlMap);

        objLockerConfigDetailsTO = null;
//        objSettlementBankTO = null;
//        setCommand("");
        keySet = null;
        objKeySet = null;
//        objSettlementTO = null;
    }

    private void updateData(String command) throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("updateLockerConfigTO", objTO);
            executeTabQuery(command);
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData(String command) throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_DELETED);
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("deleteLockerConfigTO", objTO);
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            LockerConfigDAO dao = new LockerConfigDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("######lockerconfig" + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        objTO = (LockerConfigTO) map.get("LockerConfigTO");
        setSetMap((HashMap) map.get("LockerConfigDetailsTO"));
        final String command = objTO.getCommand();
        HashMap returnMap = null;
        logDAO = new LogDAO();
        logTO = new LogTO();

        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(command);
//            returnMap = new HashMap();
//            returnMap.put("CONFIG_ID",objTO.getConfigId());
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(command);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData(command);
        } else {
            throw new NoCommandException();
        }

        destroyObjects();
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
    }

    /**
     * Getter for property setMap.
     *
     * @return Value of property setMap.
     */
    public java.util.HashMap getSetMap() {
        return setMap;
    }

    /**
     * Setter for property setMap.
     *
     * @param setMap New value of property setMap.
     */
    public void setSetMap(java.util.HashMap setMap) {
        this.setMap = setMap;
    }
}
