/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * TradingAcHeadDAO.java
 */

package com.see.truetransact.serverside.trading.tradingachead;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.businessrule.RuleContext;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.trading.tradingachead.TradingacheadTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import org.apache.log4j.Logger;
import java.util.Date;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.businessrule.RuleEngine;
import com.see.truetransact.businessrule.generalledger.AccountMaintenanceRule;
import com.see.truetransact.commonutil.TTException;

//import com.transversalnet.db.DatabaseConnection;
//import com.transversalnet.error.TENException;
//import com.transversalnet.services.Group;
//import com.transversalnet.services.ShopMaster;
//import com.transversalnet.services.Supplier;

/**
 *
 * @author Revathi L
 */

public class TradingAcHeadDAO extends TTDAO {
    private static SqlMap sqlMap = null;
    private TradingacheadTO objTradingacheadTO;
    private LogTO logTO;
    private LogDAO logDAO;
    private final static Logger log = Logger.getLogger(TradingAcHeadDAO.class);
    private Date currDt = null;
    RuleContext context;
    RuleEngine engine;
    
    public TradingAcHeadDAO()throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("#################### Trading Account Head :"+ map);
         HashMap returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
         if(map.containsKey("ACCT_HD")){ //Added By Revathi
            verifyAccountHead(map);
        }
        if (map.containsKey("objTradingAcHdTO")) {
            objTradingacheadTO = (TradingacheadTO) map.get("objTradingAcHdTO");
        }
       // final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
         final String command = (String)map.get("TOSTATUS");
        logDAO = new LogDAO();
        logTO = new LogTO();

        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
        if (map.containsKey("objTradingAcHdTO")) {
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData(map);
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData(map);
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData(map);
            }
        }
        if(map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if(authMap!=null)
               authorize(authMap);
        } 
//        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
//            returnMap.put("AC_HD_PID",objTradingacheadTO.getAccountHeadPid());
//        }
        destroyObjects();
        return null;
    }
    
    private void destroyObjects() {
        objTradingacheadTO = null;
        
    }
    
    private void deleteData(HashMap map) throws Exception {
        try {
            objTradingacheadTO.setStatus(CommonUtil.convertObjToStr("DELETED"));
            sqlMap.executeUpdate("deleteTradingAcHeadTO", objTradingacheadTO);
            logTO.setData(objTradingacheadTO.toString());
            logTO.setStatus(objTradingacheadTO.getStatus());
            logDAO.addToLog(logTO);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }
    
    private void authorize(HashMap authMap) throws Exception {
        sqlMap.executeUpdate("authorizeTradingAcHd", authMap);
    }
    
    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            HashMap wheremap = new HashMap();
            objTradingacheadTO.setStatus("MODIFIED");
            sqlMap.executeUpdate("updateTradingAcHdTO", objTradingacheadTO);
            logTO.setData(objTradingacheadTO.toString());
            logTO.setStatus(objTradingacheadTO.getStatus());
            logTO.setPrimaryKey(objTradingacheadTO.getKeyData());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }
    
    private void verifyAccountHead(HashMap inputMap)throws Exception{  //Added By Revathi
        engine = new RuleEngine();
        context = new RuleContext();
        context.addRule(new AccountMaintenanceRule());
        
        ArrayList list = (ArrayList)engine.validateAll(context, inputMap);
        if(list!=null ){
            HashMap exception = new HashMap();
            exception.put(CommonConstants.EXCEPTION_LIST, list);
            exception.put(CommonConstants.CONSTANT_CLASS,
            "com.see.truetransact.clientutil.exceptionhashmap.generalledger.GeneralLedgerRuleHashMap");
            throw new TTException(exception);
            //sqlMap.rollbackTransaction();
        }
        
    }
    
    public void insertData(HashMap map) throws Exception {
         try{
            String accountHeadPid ="";
            accountHeadPid = getAcHdPID();
            objTradingacheadTO.setAccountHeadPid(accountHeadPid);
            objTradingacheadTO.setStatus("CREATED");
            sqlMap.executeUpdate("insertTradingAccountHeadTO",objTradingacheadTO);
            logTO.setData(objTradingacheadTO.toString());
            logTO.setPrimaryKey(objTradingacheadTO.getKeyData());
            logTO.setStatus(objTradingacheadTO.getStatus());
            logDAO.addToLog(logTO);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map=null;
     }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }
    
    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        System.out.println("#### GetData map : " + map);
        returnMap = new HashMap();
        HashMap whereMap = (HashMap)map.get(CommonConstants.MAP_WHERE);
        List acHdLst = (List) sqlMap.executeQueryForList("getSelectTradingAcHd", whereMap);
        if (acHdLst != null && acHdLst.size() > 0) {
            returnMap.put("objTradingAcHdTO", acHdLst);
        }
        return returnMap;
    }
    
    private String getAcHdPID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "AC_HD_PID");
        String releaseNo = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return releaseNo;
    }
}
