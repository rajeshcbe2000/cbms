/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BankDAO.java
 * 
 * Created on Thu Feb 05 18:32:23 IST 2004
 */
package com.see.truetransact.serverside.sysadmin.bank;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.sysadmin.bank.BankTO;

/**
 * Bank DAO.
 *
 */
public class BankDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private BankTO objBankTO;
    private LogDAO logDAO;
    private LogTO logTO;

    /**
     * Creates a new instance of BankDAO
     */
    public BankDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectBankTO", where);
        returnMap.put("BankTO", list);
        return returnMap;
    }

    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();
            logTO.setData(objBankTO.toString());
            logTO.setPrimaryKey(objBankTO.getKeyData());
            logTO.setStatus(objBankTO.getCommand());
            sqlMap.executeUpdate("insertBankTO", objBankTO);
            logDAO.addToLog(logTO);
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
            logTO.setData(objBankTO.toString());
            logTO.setPrimaryKey(objBankTO.getKeyData());
            logTO.setStatus(objBankTO.getCommand());
            sqlMap.executeUpdate("updateBankTO", objBankTO);
            logDAO.addToLog(logTO);
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
            logTO.setData(objBankTO.toString());
            logTO.setPrimaryKey(objBankTO.getKeyData());
            logTO.setStatus(objBankTO.getCommand());
            sqlMap.executeUpdate("deleteBankTO", objBankTO);
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
            BankDAO dao = new BankDAO();

            BankTO bto;
            HashMap data;
            /*
             * bto = new BankTO();
             * bto.setCommand(CommonConstants.TOSTATUS_UPDATE);
             * bto.setBankCode("CORP"); bto.setBankName("Corporation Bank,
             * India"); bto.setBranchTransAllowed("Y"); bto.setCashLimit(new
             * Double("1000000")); bto.setCreatedBy("Admin");
             * bto.setDataCenterIp("10.10.10.2");
             * bto.setDayEndProcessTime("17:15"); bto.setExchRateConv("Direct");
             * bto.setSupportEmail("support@Corporationbank.com");
             * bto.setLastModifiedBy("Admin"); bto.setTellerTransAllowed("Y");
             * bto.setTransPostingMethod("Individual");
             * bto.setWebSiteAddr("www.corporationbank.com");
             * bto.setWebSiteIp("64.280.150.20");
             *
             * data = new HashMap(); data.put("BankTO",bto); dao.execute(data);
                        /*
             */

            data = new HashMap();
            data.put(CommonConstants.MAP_WHERE, "CORP");
            data = dao.executeQuery(data);

            /*
             * bto = (BankTO)((List)data.get("BankTO")).get(0);
             *
             * System.out.println(bto.getBankCode());
             * System.out.println(bto.getBankName());
             * System.out.println(bto.getBranchTransAllowed());
             * System.out.println(bto.getCashLimit().toString());
             * System.out.println(bto.getCreatedBy());
             * System.out.println(bto.getDataCenterIp());
             * System.out.println(bto.getDayEndProcessTime());
             * System.out.println(bto.getExchRateConv());
             * System.out.println(bto.getSupportEmail());
             * System.out.println(bto.getLastModifiedBy());
             * System.out.println(bto.getTellerTransAllowed());
             * System.out.println(bto.getTransPostingMethod());
             * System.out.println(bto.getWebSiteAddr());
             * System.out.println(bto.getWebSiteIp());
                        /*
             */
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        objBankTO = (BankTO) map.get("BankTO");
        logDAO = new LogDAO();
        logTO = new LogTO();

        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

        final String command = objBankTO.getCommand();

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
        objBankTO = null;
        logDAO = null;
        logTO = null;
    }
}
