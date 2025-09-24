/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * AgentCommisionSlabRateDAO.java
 *
 * Created on Wed Jun 28 13:11:28 IST 2016
 */
package com.see.truetransact.serverside.agent.agentcommisionslabrate;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.agent.agentcommisionslabrate.AgentCommisionSlabRateTO;

/**
 * Agent DAO.
 *
 */
public class AgentCommisionSlabRateDAO extends TTDAO {

    private Date currDt = null;
    private static SqlMap sqlMap = null;
    private AgentCommisionSlabRateTO objAgentCommisionDetailsTO;
    private LinkedHashMap agentCommisionDetailsMap;
    private LinkedHashMap deletedMap;
    private String userID = "";
    HashMap returnMap = new HashMap();

    /**
     * Creates a new instance of AgentCommisionSlabRateDAO
     */
    public AgentCommisionSlabRateDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("map : " + map);
        HashMap returnMap = new HashMap();
        if (map.containsKey("PROD_TYPE")) {
            HashMap agentMap = new HashMap();
            agentMap.put("PROD_TYPE", map.get("PROD_TYPE"));
            agentMap.put("PROD_ID", map.get("PROD_ID"));
            System.out.println("agentMap : " + agentMap);
            List list = (List) sqlMap.executeQueryForList("getAgentCommisionSlabRateTO", agentMap);
            if (list != null && list.size() > 0) {
                LinkedHashMap ParMap = new LinkedHashMap();
                System.out.println("@@@list" + list);
                for (int i = list.size(), j = 0; i > 0; i--, j++) {
//                    (AgentCommisionSlabRateTO) list.get(j)).setStatus("MODIFIED");
//                    dataMap.put(((AgentLeaveDetailsTO) leaveList.get(j)).getSlNo(), leaveList.get(j));
                    AgentCommisionSlabRateTO agentCommisionSlabRateTO = (AgentCommisionSlabRateTO) list.get(j);
                    agentCommisionSlabRateTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    ParMap.put(agentCommisionSlabRateTO.getSlNo(), agentCommisionSlabRateTO);
                }
                System.out.println("@@@ParMap" + ParMap);
                returnMap.put("AGENT_COMMISSION_SLAB", ParMap);
            }
        }
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;
    }

    private void insertData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();
            if (agentCommisionDetailsMap != null) {
                String leaveID = getLeaveID();
                ArrayList commisionList = new ArrayList(agentCommisionDetailsMap.keySet());
                int length = commisionList.size();
                for (int i = 0; i < length; i++) {
                    AgentCommisionSlabRateTO objAgentCommisionDetailsTO = (AgentCommisionSlabRateTO) agentCommisionDetailsMap.get(commisionList.get(i));
                    objAgentCommisionDetailsTO.setSlabId(leaveID);
                    objAgentCommisionDetailsTO.setSlNo(i + 1);
                    objAgentCommisionDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
                    objAgentCommisionDetailsTO.setStatusDt((Date) currDt.clone());
                    objAgentCommisionDetailsTO.setStatusBy(userID);
                    sqlMap.executeUpdate("insertAgentCommisionSlabRateTO", objAgentCommisionDetailsTO);
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private String getAgentMachineId() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "AGENT_MAHINE_ID");
        String strBorrower_No = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        where = null;
        dao = null;
        return strBorrower_No;
    }

    private void updateData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();
            if (agentCommisionDetailsMap != null) {
                ArrayList commisionList = new ArrayList(agentCommisionDetailsMap.keySet());
                int length = commisionList.size();
                HashMap prodTypeExistsorNoMap = new HashMap();
                for (int i = 0; i < length; i++) {
                    AgentCommisionSlabRateTO objAgentCommisionDetailsTO = (AgentCommisionSlabRateTO) agentCommisionDetailsMap.get(commisionList.get(i));
                    objAgentCommisionDetailsTO.setStatusDt((Date) currDt.clone());
                    objAgentCommisionDetailsTO.setStatusBy(userID);
                    objAgentCommisionDetailsTO.setFromDate(getProperFormatDate(objAgentCommisionDetailsTO.getFromDate()));
                    if (objAgentCommisionDetailsTO.getToDate() != null) {
                        objAgentCommisionDetailsTO.setToDate(getProperFormatDate(objAgentCommisionDetailsTO.getToDate()));
                    }
                    if (objAgentCommisionDetailsTO.getSlabId() != null && objAgentCommisionDetailsTO.getSlabId().length() > 0) {
                        objAgentCommisionDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        sqlMap.executeUpdate("updateAgentCommisionSlabRateTO", objAgentCommisionDetailsTO);
                    } else if (objAgentCommisionDetailsTO.getStatus().equals(CommonConstants.STATUS_DELETED)) {
                        objAgentCommisionDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
                        objAgentCommisionDetailsTO.setFromDate(getProperFormatDate(objAgentCommisionDetailsTO.getFromDate()));
                        if (objAgentCommisionDetailsTO.getToDate() != null) {
                            objAgentCommisionDetailsTO.setToDate(getProperFormatDate(objAgentCommisionDetailsTO.getToDate()));
                        }
                        sqlMap.executeUpdate("updateAgentCommisionSlabRateTO", objAgentCommisionDetailsTO);
                    } else {
                        prodTypeExistsorNoMap.put("PROD_TYPE", objAgentCommisionDetailsTO.getProdType());
                        prodTypeExistsorNoMap.put("PROD_ID", objAgentCommisionDetailsTO.getProdId());
                        List prodTypeExistsorNoList = sqlMap.executeQueryForList("getSelectProdTypeExistorNot", prodTypeExistsorNoMap);
                        if (prodTypeExistsorNoList != null && prodTypeExistsorNoList.size() > 0) {
                            prodTypeExistsorNoMap = (HashMap) prodTypeExistsorNoList.get(0);
                            objAgentCommisionDetailsTO.setSlNo(CommonUtil.convertObjToInt(prodTypeExistsorNoMap.get("MAX_COUNT")));
                            objAgentCommisionDetailsTO.setSlabId(CommonUtil.convertObjToStr(prodTypeExistsorNoMap.get("SLAB_ID")));
                        }
                        objAgentCommisionDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
                        sqlMap.executeUpdate("insertAgentCommisionSlabRateTO", objAgentCommisionDetailsTO);
                    }
                }
            }
            if (deletedMap != null) {
                ArrayList commisionList = new ArrayList(deletedMap.keySet());
                int length = commisionList.size();
                for (int i = 0; i < length; i++) {
                    AgentCommisionSlabRateTO objAgentCommisionDetailsTO = (AgentCommisionSlabRateTO) deletedMap.get(commisionList.get(i));
                    objAgentCommisionDetailsTO.setStatusDt((Date) currDt.clone());
                    objAgentCommisionDetailsTO.setStatusBy(userID);
//                    if (objAgentCommisionDetailsTO.getStatus().equals(CommonConstants.STATUS_DELETED)) {
                    objAgentCommisionDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
                    sqlMap.executeUpdate("deleteAgentCommisionSlabRateTO", objAgentCommisionDetailsTO);
//                    }
                }
            }
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
            if (objAgentCommisionDetailsTO != null) {
                objAgentCommisionDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
                objAgentCommisionDetailsTO.setStatusDt((Date) currDt.clone());
                objAgentCommisionDetailsTO.setStatusBy(userID);
                sqlMap.executeUpdate("deleteAgentCommisionSlabRateTO", objAgentCommisionDetailsTO);
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
            AgentCommisionSlabRateDAO dao = new AgentCommisionSlabRateDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("Agent Map Dao : " + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        // Log DAO
        LogDAO objLogDAO = new LogDAO();

        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        objLogTO.setSelectedBranchId((String) map.get(CommonConstants.SELECTED_BRANCH_ID));
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));

        userID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));

        if (map.containsKey("AGENT_COMMISION_DETAILS")) {
            agentCommisionDetailsMap = (LinkedHashMap) map.get("AGENT_COMMISION_DETAILS");
        }
        if (map.containsKey("AGENT_COMMISION_DELETED_DETAILS")) {
            deletedMap = (LinkedHashMap) map.get("AGENT_COMMISION_DELETED_DETAILS");
        }

        String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (command != null && command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(objLogDAO, objLogTO);
//            if (objAgentTO != null && objAgentTO.getAgentId().length() > 0) {
//                returnMap.put("AGENT ID", objAgentTO.getAgentId());
//            }
        } else if (command != null && command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(objLogDAO, objLogTO);
        } else if (command != null && command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData(objLogDAO, objLogTO);
        } else if (map.containsKey("AGENT_AUTHORIZE_DETAILS")) {
            if (agentCommisionDetailsMap != null) {
                HashMap authorizeMap = new HashMap();
                authorizeMap = (HashMap) map.get("AGENT_AUTHORIZE_DETAILS");
                ArrayList commisionList = new ArrayList(agentCommisionDetailsMap.keySet());
                int length = commisionList.size();
                for (int i = 0; i < length; i++) {
                    AgentCommisionSlabRateTO objAgentCommisionDetailsTO = (AgentCommisionSlabRateTO) agentCommisionDetailsMap.get(commisionList.get(i));
                    objAgentCommisionDetailsTO.setAuthorizeBy(userID);
                    System.out.println("fromAmt : " + objAgentCommisionDetailsTO.getFromAmt() + "AuthStatus : " + objAgentCommisionDetailsTO.getAuthorizeStatus());
                    if (objAgentCommisionDetailsTO.getFromAmt() == 1 && objAgentCommisionDetailsTO.getAuthorizeStatus() == null) {
                        objAgentCommisionDetailsTO.setToDate(objAgentCommisionDetailsTO.getFromDate());
                        sqlMap.executeUpdate("updateAgentCommisionSlabRatePreviousTODate", objAgentCommisionDetailsTO);
                    }
                    objAgentCommisionDetailsTO.setAuthorizeStatus(CommonUtil.convertObjToStr(authorizeMap.get("STATUS")));
                    sqlMap.executeUpdate("updateAgentCommisionSlabRateTOAuthorize", objAgentCommisionDetailsTO);
                }
            }
        } else {
            throw new NoCommandException();
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
//        objAgentTO = null;
    }

    private String getLeaveID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "AGENT_SLAB_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    public Date getProperFormatDate(Object obj) {
        Date curDt = null;
        // currDt = properFormatDate;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            curDt = (Date) currDt.clone();
            curDt.setDate(tempDt.getDate());
            curDt.setMonth(tempDt.getMonth());
            curDt.setYear(tempDt.getYear());
        }
        return curDt;
    }
}
