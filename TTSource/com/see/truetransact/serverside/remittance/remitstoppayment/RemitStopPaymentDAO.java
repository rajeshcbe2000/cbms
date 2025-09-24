/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemitStopPaymentDAO.java
 *
 * Created on Tue Jan 25 10:29:17 IST 2005
 */
package com.see.truetransact.serverside.remittance.remitstoppayment;

import java.util.List;
import java.util.ArrayList;

import java.util.HashMap;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;

import com.see.truetransact.transferobject.remittance.remitstoppayment.RemitStopPaymentTO;

import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import java.util.Date;
/**
 * RemitStopPayment DAO.
 *
 */
public class RemitStopPaymentDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private RemitStopPaymentTO objRemitStopPaymentTO;
    private String userID = "";
    private String branchID = "";
    private Date currDt = null;
    /**
     * Creates a new instance of RemitStopPaymentDAO
     */
    public RemitStopPaymentDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectRemitStopPaymentTO", where);
        returnMap.put("RemitStopPaymentTO", list);
        return returnMap;
    }

    private void insertData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            final String STOP_ID = getStopDDId();
            objRemitStopPaymentTO.setBranchId(branchID);
            objRemitStopPaymentTO.setStopPaymentId(STOP_ID);
            objRemitStopPaymentTO.setCreatedBy(userID);
            objRemitStopPaymentTO.setCreatedDt(currDt);
            objRemitStopPaymentTO.setStatusBy(userID);
            objRemitStopPaymentTO.setStatus(CommonConstants.STATUS_CREATED);
            objRemitStopPaymentTO.setStatusDt(currDt);
            objRemitStopPaymentTO.setStopStatusDt(currDt);
            sqlMap.startTransaction();
            sqlMap.executeUpdate("insertRemitStopPaymentTO", objRemitStopPaymentTO);

            objLogTO.setData(objRemitStopPaymentTO.toString());
            objLogTO.setPrimaryKey(objRemitStopPaymentTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private String getStopDDId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", "DD_STOP_ID");

        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void updateData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            objRemitStopPaymentTO.setStatusBy(userID);
            objRemitStopPaymentTO.setStatus(CommonConstants.STATUS_MODIFIED);
            objRemitStopPaymentTO.setStatusDt(currDt);

            sqlMap.startTransaction();
            sqlMap.executeUpdate("updateRemitStopPaymentTO", objRemitStopPaymentTO);

            objLogTO.setData(objRemitStopPaymentTO.toString());
            objLogTO.setPrimaryKey(objRemitStopPaymentTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            objRemitStopPaymentTO.setStatusBy(userID);
            objRemitStopPaymentTO.setStatus(CommonConstants.STATUS_DELETED);
            objRemitStopPaymentTO.setStatusDt(currDt);

            sqlMap.startTransaction();
            sqlMap.executeUpdate("deleteRemitStopPaymentTO", objRemitStopPaymentTO);

            objLogTO.setData(objRemitStopPaymentTO.toString());
            objLogTO.setPrimaryKey(objRemitStopPaymentTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            RemitStopPaymentDAO dao = new RemitStopPaymentDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        // Log DAO
        LogDAO objLogDAO = new LogDAO();
        HashMap returnData = null;
        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        objLogTO.setSelectedBranchId((String) map.get(CommonConstants.SELECTED_BRANCH_ID));
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));

        userID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
        branchID = CommonUtil.convertObjToStr(map.get(CommonConstants.SELECTED_BRANCH_ID));

        objRemitStopPaymentTO = (RemitStopPaymentTO) map.get("RemitStopPaymentTO");
        final String command = objRemitStopPaymentTO.getCommand();

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(objLogDAO, objLogTO);
            returnData = new HashMap();
            returnData.put("STOP PAYMENT ID", objRemitStopPaymentTO.getStopPaymentId());
            System.out.println("@@@@@@@@returnData" + returnData);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(objLogDAO, objLogTO);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData(objLogDAO, objLogTO);
        } else {
            throw new NoCommandException();
        }


        objLogDAO = null;
        objLogTO = null;
        destroyObjects();
        return returnData;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objRemitStopPaymentTO = null;
    }
}
