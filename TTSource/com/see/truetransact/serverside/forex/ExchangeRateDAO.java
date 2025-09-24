/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ExchangeRateDAO.java
 * 
 * Created on Mon Jan 12 17:16:01 GMT+05:30 2004
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
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.transferobject.forex.ExchangeRateTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;

/**
 * ExchangeRate DAO.
 *
 */
public class ExchangeRateDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private ExchangeRateTO objTO;

    /**
     * Creates a new instance of ExchangeRateDAO
     */
    public ExchangeRateDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        //String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectExchangeRateTO", map);
        returnMap.put("ExchangeRateDetails", list);
        return returnMap;
    }

    /**
     * To get auto generated Exchange Rate ID from table
     */
    private void getExchangeRateID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "EXCHANGE_RATE");
        String rMap = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        objTO.setExchangeId(rMap);
    }

    private void insertData() throws Exception {
        getExchangeRateID();
        objTO.setStatus(CommonConstants.STATUS_CREATED);
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("insertExchangeRateTO", objTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateData() throws Exception {
        objTO.setStatus(CommonConstants.STATUS_MODIFIED);
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("updateExchangeRateTO", objTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData() throws Exception {
        objTO.setStatus(CommonConstants.STATUS_DELETED);
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("deleteExchangeRateTO", objTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        objTO = (ExchangeRateTO) map.get("EXCHANGE_RATE");
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
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
    }

    public static void main(String str[]) {
        try {
            ExchangeRateDAO dao = new ExchangeRateDAO();
            ExchangeRateTO objExchangeRateTO = new ExchangeRateTO();
            TOHeader toHeader = new TOHeader();
            toHeader.setCommand(CommonConstants.TOSTATUS_INSERT);
            objExchangeRateTO.setValueDate(DateUtil.getDateMMDDYYYY(null));
            objExchangeRateTO.setTransCurrency("1000");
            objExchangeRateTO.setConversionCurrency("convCur");
            objExchangeRateTO.setCustomerType("New");
            objExchangeRateTO.setMiddleRate(new Double("1000"));
            objExchangeRateTO.setSellingPer(new Double("10"));
            objExchangeRateTO.setSellingPrice(new Double("25"));
            objExchangeRateTO.setBuyingPer(new Double("5"));
            objExchangeRateTO.setBuyingPrice(new Double("500"));
            objExchangeRateTO.setPreferred("y");
            objExchangeRateTO.setCommission(new Double("14"));
            objExchangeRateTO.setRemarks("");
            objExchangeRateTO.setCreatedBy("malai");
            objExchangeRateTO.setCreatedDt(DateUtil.getDateMMDDYYYY(null));
            objExchangeRateTO.setAuthorizedBy("anna");
            objExchangeRateTO.setAuthorizedDt(DateUtil.getDateMMDDYYYY(null));
            objExchangeRateTO.setStatus(CommonConstants.STATUS_CREATED);
            objExchangeRateTO.setTOHeader(toHeader);

            HashMap hash = new HashMap();
            hash.put("EXCHANGE_RATE", objExchangeRateTO);

            dao.execute(hash);

            dao = null;
            objExchangeRateTO = null;
            toHeader = null;
            hash = null;
            /*
             * -------------------------------------------------------
             */
            /*
             * dao = new ExchangeRateDAO(); objExchangeRateTO = new
             * ExchangeRateTO(); toHeader = new TOHeader();
             *
             * toHeader.setCommand(CommonConstants.TOSTATUS_UPDATE);
             * //objExchangeRateTO.setExchangeId ("1001");
             * objExchangeRateTO.setValueDate (DateUtil.getDateMMDDYYYY (null));
             * objExchangeRateTO.setTransCurrency ("2000");
             * objExchangeRateTO.setConversionCurrency ("cconvCur");
             * objExchangeRateTO.setCustomerType ("Neww");
             * objExchangeRateTO.setMiddleRate (new Double ("3000"));
             * objExchangeRateTO.setSellingPer (new Double ("40"));
             * objExchangeRateTO.setSellingPrice IDGenerateMap(new Double
             * ("65")); objExchangeRateTO.setBuyingPer (new Double ("35"));
             * objExchangeRateTO.setBuyingPrice (new Double ("7500"));
             * objExchangeRateTO.setPreferred ("n");
             * objExchangeRateTO.setCommission (new Double("17"));
             * objExchangeRateTO.setRemarks ("wer");
             * objExchangeRateTO.setCreatedBy ("anna");
             * objExchangeRateTO.setCreatedDt (DateUtil.getDateMMDDYYYY (null));
             * objExchangeRateTO.setAuthorizedBy ("chk");
             * objExchangeRateTO.setAuthorizedDt (DateUtil.getDateMMDDYYYY
             * (null)); objExchangeRateTO.setStatus
             * (CommonConstants.STATUS_MODIFIED);
             * objExchangeRateTO.setTOHeader(toHeader);
             *
             * hash = new HashMap();
             * hash.put("EXCHANGE_RATE",objExchangeRateTO);
             *
             * dao.execute(hash);
             *
             *
             * dao = null; objExchangeRateTO = null; toHeader = null; hash = null;
             */
            /*
             * -------------------------------------------------------
             */

            /*
             * dao = new ExchangeRateDAO(); objExchangeRateTO = new
             * ExchangeRateTO(); toHeader = new TOHeader();
             *
             * toHeader.setCommand(CommonConstants.TOSTATUS_DELETE);
             * objExchangeRateTO.setExchangeId ("1001");
             * objExchangeRateTO.setValueDate (DateUtil.getDateMMDDYYYY (null));
             * objExchangeRateTO.setTransCurrency ("1000");
             * objExchangeRateTO.setConversionCurrency ("convCur");
             * objExchangeRateTO.setCustomerType ("New");
             * objExchangeRateTO.setMiddleRate (new Double ("1000"));
             * objExchangeRateTO.setSellingPer (new Double ("10"));
             * objExchangeRateTO.setSellingPrice (new Double ("25"));
             * objExchangeRateTO.setBuyingPer (new Double ("5"));
             * objExchangeRateTO.setBuyingPrice (IDGenerateMapnew Double
             * ("500")); objExchangeRateTO.setPreferred ("y");
             * objExchangeRateTO.setCommission (new Double("14"));
             * objExchangeRateTO.setRemarks (""); objExchangeRateTO.setCreatedBy
             * ("malai"); objExchangeRateTO.setCreatedDt
             * (DateUtil.getDateMMDDYYYY (null));
             * objExchangeRateTO.setAuthorizedBy ("mathan");
             * objExchangeRateTO.setAuthorizedDt (DateUtil.getDateMMDDYYYY
             * (null)); objExchangeRateTO.setStatus
             * (CommonConstants.STATUS_DELETED);
             * objExchangeRateTO.setTOHeader(toHeader);
             *
             * hash = new HashMap();
             * hash.put("EXCHANGE_RATE",objExchangeRateTO);
             *
             * dao.execute(hash);              *
             * dao = null; objExchangeRateTO = null; toHeader = null; hash = null;
             */

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
