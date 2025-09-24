/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ForexExchangeDAO.java
 *
 * Created on Tue May 04 18:27:02 IST 2004
 */
package com.see.truetransact.serverside.forex;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;

import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.transferobject.forex.ForexExchangeTO;

/**
 * ForexExchange DAO.
 *
 */
public class ForexExchangeDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private ForexExchangeTO objForexExchangeTO;

    /**
     * Creates a new instance of ForexExchangeDAO
     */
    public ForexExchangeDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        System.out.println("Where: " + where);
        List list = (List) sqlMap.executeQueryForList("getSelectForexExchangeTO", where);
        returnMap.put("ForexExchangeTO", list);
        return returnMap;
    }

    private void insertData() throws Exception {
        try {
            final String exchangeID = getExchangeID();
            objForexExchangeTO.setExchangeId(exchangeID);
            objForexExchangeTO.setStatus(CommonConstants.STATUS_CREATED);
            sqlMap.startTransaction();
            sqlMap.executeUpdate("insertForexExchangeTO", objForexExchangeTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private String getExchangeID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", "FOREXEXCHANGE");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void updateData() throws Exception {
        try {
            objForexExchangeTO.setStatus(CommonConstants.STATUS_MODIFIED);
            sqlMap.startTransaction();
            sqlMap.executeUpdate("updateForexExchangeTO", objForexExchangeTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData() throws Exception {
        try {
            objForexExchangeTO.setStatus(CommonConstants.STATUS_DELETED);
            sqlMap.startTransaction();
            sqlMap.executeUpdate("deleteForexExchangeTO", objForexExchangeTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            ForexExchangeDAO dao = new ForexExchangeDAO();
            ForexExchangeTO objForexExchangeTO = new ForexExchangeTO();

            TOHeader toHeader = new TOHeader();
            toHeader.setCommand(CommonConstants.TOSTATUS_INSERT);  //To tell what to do... Insert, Update, Delete...

            objForexExchangeTO.setTOHeader(toHeader);
            objForexExchangeTO.setExchangeId("E001");
            objForexExchangeTO.setValueDate(DateUtil.getDateMMDDYYYY(null));
            objForexExchangeTO.setTransCurrency("USD");
            objForexExchangeTO.setMiddleRate(new Double(44));
            objForexExchangeTO.setBaseCurrency("INR");

            HashMap hash = new HashMap();
            hash.put("ForexExchangeTO", objForexExchangeTO);
            dao.execute(hash);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        objForexExchangeTO = (ForexExchangeTO) map.get("ForexExchangeTO");
        final String command = objForexExchangeTO.getCommand();

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData();
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData();
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        } else {
            throw new NoCommandException();
        }

        destroyObjects();
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void destroyObjects() {
        objForexExchangeTO = null;
    }
}
