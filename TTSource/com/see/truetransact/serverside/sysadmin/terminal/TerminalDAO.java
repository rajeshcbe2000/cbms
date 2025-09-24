/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TerminalDAO.java
 *
 * Created on Mon Feb 02 17:15:01 GMT+05:30 2004
 */

package com.see.truetransact.serverside.sysadmin.terminal;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.ParseException;
import org.apache.log4j.Logger;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.sysadmin.terminal.TerminalTO;

import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import java.util.Date;
/**
 * This is used for Terminal Data Access.
 *
 * @author  Ashok
 */
public class TerminalDAO extends TTDAO {
    private static SqlMap sqlMap = null;
    private TerminalTO objTO;
    private final static Logger _log = Logger.getLogger(TerminalDAO.class);
    private ArrayList terminalMasterTabTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private String userId = "";
    private Date currDt = null;
    static {
        try {
            ServiceLocator locate = ServiceLocator.getInstance();
            sqlMap = (SqlMap) locate.getDAOSqlMap();
        } catch(ServiceLocatorException se) {
            _log.error(se);
        }
    }
    
    /** Creates a new instance of TerminalDAO */
    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap whereMap = (HashMap)map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("SelectTerminalMaster", whereMap);
        returnMap.put("TerminalTO", list);
        list =  null;
        whereMap = null;
        return returnMap;
    }
    /** To insert data */
    private void insertData(TerminalTO obj) throws Exception {
        try {
            sqlMap.startTransaction();
            
            String terminalId = getTerminalId();
            obj.setTerminalId(terminalId);
            obj.setStatus(CommonConstants.STATUS_CREATED);
            obj.setStatusBy(userId);
            obj.setStatusDt(currDt);
            
            terminalId = null;
            logTO.setData(obj.toString());
            logTO.setPrimaryKey(obj.getKeyData());
            logTO.setStatus(obj.getCommand());
            
            sqlMap.executeUpdate("insertTerminalTO", obj);
            logDAO.addToLog(logTO);
            
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            _log.error(e);
            throw e;
        }
    }
    
    /** To Update Terminal data */
    private void updateData(TerminalTO objTO) throws Exception {
        try {
            sqlMap.startTransaction();
            
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            objTO.setStatusBy(userId);
            objTO.setStatusDt(currDt);
            
            sqlMap.executeUpdate("updateTerminalMaster", objTO);
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            _log.error(e);
            throw e;
        }
    }
    
    /** To get auto generated TerminalID from table */
    private String getTerminalId() throws Exception{
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "TERMINAL_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }
    
    /** To delete Terminal data */
    private void deleteData(TerminalTO objTO) throws Exception {
        try {
            sqlMap.startTransaction();
            
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            
            objTO.setStatus(CommonConstants.STATUS_DELETED);
            objTO.setStatusBy(userId);
            objTO.setStatusDt(currDt);
            
            sqlMap.executeUpdate("DeleteTerminalMaster", objTO);
            logDAO.addToLog(logTO);
            
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            _log.error(e);
            throw e;
        }
    }
    
    
    /** Standard method for insert, update & delete operations */
    public HashMap execute(HashMap map)  throws Exception {
		_branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        terminalMasterTabTO = (ArrayList) map.get("TerminalTO");
        String command;
        for(int i=0;i<terminalMasterTabTO.size();i++){
            objTO=(TerminalTO)terminalMasterTabTO.get(i);
            logDAO = new LogDAO();
            logTO = new LogTO();
            
            logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
            logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
            logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
            logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
            logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
            
            userId = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
            
            command=objTO.getCommand();
            if (command.equals(CommonConstants.TOSTATUS_INSERT)){
                insertData(objTO);
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)){
                updateData(objTO);
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)){
                deleteData(objTO);
            } else {
                throw new NoCommandException();
            }
        }
        destroyObjects();return null;
    }
    
    /** Standard method to get data for a particular Branch based on Terminal id */
    public HashMap executeQuery(HashMap obj) throws Exception {
		_branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }
    
    /** To make TerminalTO object null */
    private void destroyObjects() {
        TerminalTO objTO = null;
    }
}
