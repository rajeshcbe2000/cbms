/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * TradingGroupDAO.java
 * @author Revathi L
 */
package com.see.truetransact.serverside.trading.tradinggroup;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.sysadmin.otherbank.OtherBankTO;
import com.see.truetransact.transferobject.tds.tdsconfig.TDSConfigTO;
import com.see.truetransact.transferobject.trading.tradinggroup.TradingGroupTO;
import com.see.truetransact.transferobject.trading.tradinggroup.TradingSubGroupTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.servicelocator.ServiceLocator;
import org.apache.log4j.Logger;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverutil.ServerUtil;
import java.util.Date;

public class TradingGroupDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private TDSConfigTO objTO;
    private LogTO logTO;
    private LogDAO logDAO;
    private TradingGroupTO objTradingGroupTO;
    private TradingSubGroupTO objTradingSubGroupTO;
    private LinkedHashMap subGroupMap = null;
    private LinkedHashMap deletedsubGroupMap = null;
    private LinkedHashMap totalsubGroupMap = null;
    private final String TO_DELETED_AT_UPDATE_MODE = "TO_DELETED_AT_UPDATE_MODE";
    private final String TO_NOT_DELETED_AT_UPDATE_MODE = "TO_NOT_DELETED_AT_UPDATE_MODE";
    private final static Logger log = Logger.getLogger(TradingGroupDAO.class);
    String groupID = "";
    private Date CurrDt = null;

    public TradingGroupDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private void setInitialValuesForLogTO(HashMap map) throws Exception {
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("################# TradingGroup DAO :" + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        CurrDt = ServerUtil.getCurrentDate(_branchCode);
        LogDAO logDAO = new LogDAO();
        logTO = new LogTO();
        setInitialValuesForLogTO(map);
        HashMap returnMap = new HashMap();
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (map.containsKey("objTradingGroupTO")) {
            objTradingGroupTO = (TradingGroupTO) map.get("objTradingGroupTO");
        }
        if (map.containsKey("objSubGroupTO")) {
            totalsubGroupMap = (LinkedHashMap) map.get("objSubGroupTO");
        }
        if (map.containsKey("DELETE_DATA")) {
            deletedsubGroupMap = (LinkedHashMap) map.get("DELETE_DATA");
        }
        subGroupMap = (LinkedHashMap) totalsubGroupMap.get("TO_NOT_DELETED_AT_UPDATE_MODE");
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(logDAO, logTO, command);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(logDAO, logTO, command);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData(map);
        } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap);
            }
        }
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            returnMap.put("GROUP_ID", objTradingGroupTO.getGroupID());
        }
        destroyObjects();
        map = null;
        return returnMap;
    }

    private void deleteData(HashMap map) throws Exception {
        try {
            objTradingGroupTO.setStatus(CommonUtil.convertObjToStr("DELETED"));
            sqlMap.executeUpdate("deleteTradingGroupDetailsTO", objTradingGroupTO);
            sqlMap.executeUpdate("deleteTradingSubGroupTO", objTradingGroupTO);
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
        HashMap authLstMap =  new HashMap();
        List authLst = null;
        AuthMap = (HashMap) selectedList.get(0);
        try {
            sqlMap.executeUpdate("authorizeTradingSubGroup", AuthMap);
            authLst = (List) sqlMap.executeQueryForList("getAuthDt", AuthMap);
            if (authLst != null && authLst.size() > 0) {
                authLstMap = (HashMap) authLst.get(0);
                if (CommonUtil.convertObjToStr(authLstMap.get("AUTHORIZED_DT")).length() > 0) {
                    AuthMap.put("STATUS", "AUTHORIZED");
                    AuthMap.put("AUTHORIZED_BY", CommonUtil.convertObjToStr(AuthMap.get("AUTHORIZED_BY")));
                }
            }
            sqlMap.executeUpdate("authorizeTradingGroup", AuthMap);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateData(LogDAO logDAO, LogTO logTO, String command) throws Exception {
        try {
            if (objTradingGroupTO != null) {
                objTradingGroupTO.setStatus(CommonConstants.STATUS_MODIFIED);
                sqlMap.executeUpdate("updateTradingGroupTO", objTradingGroupTO);
                logTO.setData(objTradingGroupTO.toString());
                logTO.setPrimaryKey(objTradingGroupTO.getKeyData());
                logTO.setStatus(command);
                logDAO.addToLog(logTO);
            }
            updateSubGroup(logDAO, logTO, command, objTradingGroupTO);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateSubGroup(LogDAO logDAO, LogTO logTO, String command, TradingGroupTO objTradingGroupTO) throws Exception {
        try {
            sqlMap.startTransaction();
            if (subGroupMap != null && subGroupMap.size()>0) {
                ArrayList addList = new ArrayList(subGroupMap.keySet());
                TradingSubGroupTO objTradingSubGroupTO = null;
                for (int i = 0; i < subGroupMap.size(); i++) {
                    objTradingSubGroupTO = new TradingSubGroupTO();
                    objTradingSubGroupTO = (TradingSubGroupTO) subGroupMap.get(addList.get(i));
                    if (objTradingSubGroupTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                        String subGroupID = "";
                        subGroupID = getSubGroupID();
                        objTradingSubGroupTO.setSubGroupID(subGroupID);
                        objTradingSubGroupTO.setCr_Dt(CurrDt);
                        sqlMap.executeUpdate("insertTradingSubGroupTO", objTradingSubGroupTO);
                    } else {
                        objTradingSubGroupTO.setCr_Dt(CurrDt);
                        sqlMap.executeUpdate("updateSubGroupTO", objTradingSubGroupTO);
                    }
                }
            }
            if (deletedsubGroupMap != null && deletedsubGroupMap.size()>0) {
                ArrayList addList = new ArrayList(deletedsubGroupMap.keySet());
                TradingSubGroupTO objTradingSubGroupTO = null;
                for (int i = 0; i < deletedsubGroupMap.size(); i++) {
                    objTradingSubGroupTO = new TradingSubGroupTO();
                    objTradingSubGroupTO = (TradingSubGroupTO) deletedsubGroupMap.get(addList.get(i));
                    sqlMap.executeUpdate("deleteSubGroupTO", objTradingSubGroupTO);
                }
            }

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }

        //map = null;
    }

    private void insertData(LogDAO logDAO, LogTO logTO, String command) throws Exception {

        try {
            sqlMap.startTransaction();

            if (objTradingGroupTO != null) {
                // insert Main Group
                objTradingGroupTO.setStatus(CommonConstants.STATUS_CREATED);
                String groupID = "";
                groupID = getGroupID();
                objTradingGroupTO.setGroupID(groupID);
                sqlMap.executeUpdate("insertTradingGroupTO", objTradingGroupTO);
                logTO.setData(objTradingGroupTO.toString());
                logTO.setPrimaryKey(objTradingGroupTO.getKeyData());
                logTO.setStatus(command);
                logDAO.addToLog(logTO);
                insertSubGroup(logDAO, logTO, command, groupID);
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private String getGroupID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "TR_GROUP_ID");
        String releaseNo = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return releaseNo;
    }

    private String getSubGroupID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "TR_SUB_GROUP_ID");
        String releaseNo = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return releaseNo;
    }

    private void insertSubGroup(LogDAO logDAO, LogTO logTO, String command, String groupID) throws Exception {
        try {
            if (subGroupMap != null && subGroupMap.size()>0) {
                ArrayList addList = new ArrayList(subGroupMap.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    TradingSubGroupTO objTradingSubGroupTO = (TradingSubGroupTO) subGroupMap.get(addList.get(i));
                    String subGroupID = "";
                    subGroupID = getSubGroupID();
                    objTradingSubGroupTO.setGroupID(groupID);
                    objTradingSubGroupTO.setSubGroupID(subGroupID);
                    objTradingSubGroupTO.setCr_Dt(CurrDt);
                    sqlMap.executeUpdate("insertTradingSubGroupTO", objTradingSubGroupTO);
                    logTO.setData(objTradingSubGroupTO.toString());
                    logTO.setPrimaryKey(objTradingSubGroupTO.getKeyData());
                    logTO.setStatus(command);
                    logDAO.addToLog(logTO);
                    objTradingSubGroupTO = null;
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        System.out.println("#### Trading Group Dao GetData map : " + map);
        returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSelectTradingGroupMasterTO", map);
        if (list != null && list.size() > 0) {
            returnMap.put("objTradingGroupTO", list);
        }
        map.put("GROUP_ID", map.get("GROUP_ID"));
        List subGroupList = (List) sqlMap.executeQueryForList("getTradingSubGroupTO", map);
        if (subGroupList != null && subGroupList.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            for (int i = subGroupList.size(), j = 0; i > 0; i--, j++) {
                String st = ((TradingSubGroupTO) subGroupList.get(j)).getGroupID();
                ParMap.put(j + 1, subGroupList.get(j));
            }
            returnMap.put("SUB_GROUP_DATA", ParMap);
        }
        return returnMap;
    }

    private void destroyObjects() {
        objTradingGroupTO = null;
        objTradingSubGroupTO = null;
        logTO = null;
        logDAO = null;
        subGroupMap = null;
        deletedsubGroupMap = null;
        totalsubGroupMap = null;
    }
}
