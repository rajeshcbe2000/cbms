/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * CurrencyExchangeDAO.java
 *
 * Created on Mon Jan 12 14:27:32 GMT+05:30 2004
 */
package com.see.truetransact.serverside.forex;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.transferobject.forex.CurrencyExchangeTO;

/**
 * CurrencyExchange DAO.
 *
 */
public class CurrencyExchangeDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private CurrencyExchangeTO objTO;

    /**
     * Creates a new instance of CurrencyExchangeDAO
     */
    public CurrencyExchangeDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectCurrencyExchangeTO", where);
        returnMap.put("CurrencyExchangeTO", list);
        return returnMap;
    }

    private void insertData() throws Exception {
        try {
            IDGenerateDAO dao = new IDGenerateDAO();
            HashMap where = new HashMap();
            where.put(CommonConstants.MAP_WHERE, "CURRENCY_EXCHANGE");
            String rMap = (String) dao.executeQuery(where).get(CommonConstants.DATA);
            objTO.setTransId(rMap);

            sqlMap.startTransaction();
            sqlMap.executeUpdate("insertCurrencyExchangeTO", objTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("updateCurrencyExchangeTO", objTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("deleteCurrencyExchangeTO", objTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            CurrencyExchangeDAO dao = new CurrencyExchangeDAO();

            /*
             * HashMap hash = new HashMap(); hash.put(CommonConstants.MAP_WHERE,
             * "T000001"); HashMap map = dao.executeQuery(hash);
             * System.out.println (map);
             */

            CurrencyExchangeTO objCurrencyExchangeTO = new CurrencyExchangeTO();
            objCurrencyExchangeTO.setCommand(CommonConstants.TOSTATUS_UPDATE);
            objCurrencyExchangeTO.setTransId("X000000000000003");
            objCurrencyExchangeTO.setTransDt(new java.util.Date());
            objCurrencyExchangeTO.setAcctNo("123123");
            objCurrencyExchangeTO.setTransType("Deposit");
            objCurrencyExchangeTO.setTransCurrency("USD");
            objCurrencyExchangeTO.setTransAmount(new Double(23454));
            objCurrencyExchangeTO.setConvCurrency("INR");
            objCurrencyExchangeTO.setValueDate(new java.util.Date());
            objCurrencyExchangeTO.setExchangeRate(new Double(353));
            objCurrencyExchangeTO.setCommission(new Double(4356));
            objCurrencyExchangeTO.setTotalAmount(new Double(3454));
            objCurrencyExchangeTO.setRemarks("getTxtReASDFASDFmarks()");
            //objCurrencyExchangeTO.setCreatedBy (getTxtCreatedBy());
            //objCurrencyExchangeTO.setCreatedDt (DateUtil.getDateMMDDYYYY (getTxtCreatedDt()));
            //objCurrencyExchangeTO.setAuthorizedBy (getTxtAuthorizedBy());
            //objCurrencyExchangeTO.setAuthorizedDt (DateUtil.getDateMMDDYYYY (getTxtAuthorizedDt()));

            objCurrencyExchangeTO.setStatus(CommonConstants.TOSTATUS_UPDATE);
            HashMap hash = new HashMap();
            hash.put("CurrencyExchangeTO", objCurrencyExchangeTO);
            dao.execute(hash);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        objTO = (CurrencyExchangeTO) map.get("CurrencyExchangeTO");

        final String command = objTO.getCommand();

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
        objTO = null;
    }
}
