/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LodgementBillsTODAO.java
 *
 * Created on Tue Mar 16 17:29:44 IST 2004
 */
package com.see.truetransact.serverside.bills;

import java.util.List;
import java.util.ArrayList;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.bills.LodgementBillsTO;
import com.see.truetransact.commonutil.CommonUtil;


import java.util.HashMap;

/**
 * LodgementBillsTO DAO.
 *
 */
public class LodgementBillsDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LodgementBillsTO objTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private List list;
    private String Bill_Lodgement_ID;
    private IDGenerateDAO dao;
    private HashMap where;

    /**
     * Creates a new instance of LodgementBillsTODAO
     */
    public LodgementBillsDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        list = (List) sqlMap.executeQueryForList("getSelectLodgementBillsTO", where);
        returnMap.put("LodgementBillsTO", list);
        map = null;
        where = null;
        list = null;
        return returnMap;
    }

    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            Bill_Lodgement_ID = getBillLodgementID();
            objTO.setLodgementId(Bill_Lodgement_ID);
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("insertLodgementBillsTO", objTO);
            Bill_Lodgement_ID = null;
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
        objTO.setLodgementId(Bill_Lodgement_ID);
    }

    private String getBillLodgementID() throws Exception {
        dao = new IDGenerateDAO();
        where = new HashMap();
        where.put("WHERE", "LODGEMENT_ID");
        String str = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        where = null;
        dao = null;
        return str;
    }

    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("updateLodgementBillsTO", objTO);
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
            objTO.setStatus(CommonConstants.STATUS_DELETED);
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("deleteLodgementBillsTO", objTO);
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    /**/ public static void main(String str[]) throws Exception {
        LodgementBillsDAO objLodgementBillsDAO = new LodgementBillsDAO();
        System.out.println(objLodgementBillsDAO.sqlMap.executeQueryForList("getAcctHeadIDs", ""));
    }
    /* 
     }
     try {
     LodgementBillsDAO dao = new LodgementBillsDAO();
     LodgementBillsTO objTO = new LodgementBillsTO();
     TOHeader toHeader = new TOHeader();
     toHeader.setCommand(CommonConstants.TOSTATUS_DELETE);
     objTO.setTOHeader(toHeader);
  
     objTO.setProdId("tt");
     objTO.setBillType("tt");
     objTO.setBankDetail("tt");
     objTO.setCustId("tt");
     objTO.setBorrowerNo("tt");
     objTO.setInstrumentDetails("tt");
     objTO.setPSbrOther("tt");
     objTO.setBillAmt(new Double(5));
     objTO.setCommission(new Double(5));
     objTO.setDiscount(new Double(5));
     objTO.setPostage(new Double(5));
     objTO.setMargin(new Double(5));
     objTO.setDdDrawee("as");
     objTO.setDdName("as");
     objTO.setDdStreet("as");
     objTO.setDdArea("as");
     objTO.setDdCity("as");
     objTO.setDdState("as");
     objTO.setDdCountry("as");
     objTO.setDdPincode("as");
     objTO.setFdDrawee("as");
     objTO.setFdName("as");
     objTO.setFdStreet("as");
     objTO.setFdArea("as");
     objTO.setFdCity("as");
     objTO.setFdState("as");
     objTO.setFdCountry("as");
     objTO.setFdPincode("as");
     objTO.setStatus("DELETED");
  
     HashMap hash = new HashMap();
     hash.put("LodgementBillsTO",objTO);
  
     dao.execute(hash);
  
  
     } catch (Exception ex) {
     ex.printStackTrace();
     }
     }
     /*/

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        logDAO = new LogDAO();
        logTO = new LogTO();
        objTO = (LodgementBillsTO) map.get("LodgementBillsTO");
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
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
        map = null;
        destroyObjects();
        return null;

    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
        logDAO = null;
        logTO = null;
    }
}
