/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * TradingProductDAO.java
 *
 * @author Revathi.L
 */
package com.see.truetransact.serverside.trading.tradingproduct;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.tds.tdsconfig.TDSConfigTO;
import com.see.truetransact.transferobject.trading.tradingproduct.TradingProductTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;

public class TradingProductDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private TDSConfigTO objTO;
    private LogTO logTO;
    private LogDAO logDAO;
    private TradingProductTO objTradingProductTO;
    private final static Logger log = Logger.getLogger(TradingProductDAO.class);

    public TradingProductDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("#### TradingProductDAO : " + map);
        HashMap returnMap = new HashMap();
        logDAO = new LogDAO();
        logTO = new LogTO();
        objTradingProductTO = (TradingProductTO) map.get("objTradingProductTO");
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
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            returnMap.put("TR_PROD_ID", objTradingProductTO.getProductID());
        }
        map = null;
        destroyObjects();
        return returnMap;
    }

    private void authorize(HashMap map) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        System.out.println("@@@@@@@AuthMap : " + AuthMap);
        try {
            sqlMap.executeUpdate("authorizeTradingProduct", AuthMap);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public void insertData(HashMap map) throws Exception {
        try {
            String productID = "";
            productID = getTradingProductID();
            objTradingProductTO.setProductID(productID);
            sqlMap.executeUpdate("insertTradingProductTO", objTradingProductTO);
            logTO.setData(objTradingProductTO.toString());
            logTO.setPrimaryKey(objTradingProductTO.getKeyData());
            logTO.setStatus(objTradingProductTO.getStatus());
            logDAO.addToLog(logTO);
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
            HashMap wheremap = new HashMap();
            wheremap.put("PRODUCT_ID", objTradingProductTO.getProductID());
            sqlMap.executeUpdate("updateTradingProductTO", objTradingProductTO);
            logTO.setData(objTradingProductTO.toString());
            logTO.setPrimaryKey(objTradingProductTO.getKeyData());
            logTO.setStatus(objTradingProductTO.getStatus());
            logDAO.addToLog(logTO);
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
            objTradingProductTO.setStatus(CommonUtil.convertObjToStr("DELETED"));
            sqlMap.executeUpdate("deleteTradingProductTO", objTradingProductTO);
            logTO.setData(objTradingProductTO.toString());
            logTO.setPrimaryKey(objTradingProductTO.getKeyData());
            logTO.setStatus(objTradingProductTO.getStatus());
            logDAO.addToLog(logTO);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private String getTradingProductID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "TR_PROD_ID");
        String releaseNo = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return releaseNo;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        System.out.println("#### GetData map : " + map);
        returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSelectTradingProductTO", map);
        if (list != null && list.size() > 0) {
            returnMap.put("objTradingProductTO", list);
        }
        return returnMap;
    }

    private void destroyObjects() {
        objTradingProductTO = null;
    }
}
