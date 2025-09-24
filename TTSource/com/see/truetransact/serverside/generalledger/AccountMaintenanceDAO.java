/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AccountMaintenanceDAO.java
 *
 * Created on September 2, 2003, 4:14 PM
 */
package com.see.truetransact.serverside.generalledger;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import java.rmi.RemoteException;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.transferobject.generalledger.*;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import java.util.Date;
/**
 * This is used for AccountMaintenance Data Access.
 *
 * @author Balachandar
 * @author Annamalai
 */
public class AccountMaintenanceDAO extends TTDAO {

    static SqlMap _sqlMap = null;
    private AccountMaintenanceTO _objAccountMaintenanceTO;
    private LogTO logTO;
    private LogDAO logDAO;
    private String userID = "";
    private Date currDt = null;
    /**
     * Creates a new instance of AcctHeadDAO
     */
    public AccountMaintenanceDAO() throws ServiceLocatorException {
        final ServiceLocator locate = ServiceLocator.getInstance();
        _sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private void insertData() throws Exception {
        try {
            _objAccountMaintenanceTO.setStatus(CommonConstants.STATUS_CREATED);
            _objAccountMaintenanceTO.setStatusBy(userID);
            _objAccountMaintenanceTO.setStatusDt(currDt);
            _sqlMap.startTransaction();
            contraAspects();
            logTO.setData(_objAccountMaintenanceTO.toString());
            logTO.setPrimaryKey(_objAccountMaintenanceTO.getKeyData());
            logTO.setStatus(_objAccountMaintenanceTO.getCommand());
            _sqlMap.executeUpdate("insertAccountMaintenanceTO", _objAccountMaintenanceTO);
            logDAO.addToLog(logTO);

            _sqlMap.commitTransaction();
        } catch (Exception se) {
            // if something goes wrong, rollback the transaction
            _sqlMap.rollbackTransaction();
            throw new TransRollbackException();
        } finally {
            _objAccountMaintenanceTO = null;
        }
    }

    private void updateData() throws Exception {
        try {
            _objAccountMaintenanceTO.setStatus(CommonConstants.STATUS_MODIFIED);
            _objAccountMaintenanceTO.setStatusBy(userID);
            _objAccountMaintenanceTO.setStatusDt(currDt);
            _sqlMap.startTransaction();
            contraAspects();
            logTO.setData(_objAccountMaintenanceTO.toString());
            logTO.setPrimaryKey(_objAccountMaintenanceTO.getKeyData());
            logTO.setStatus(_objAccountMaintenanceTO.getCommand());
            _sqlMap.executeUpdate("updateAccountMaintenanceTO", _objAccountMaintenanceTO);
            logDAO.addToLog(logTO);
            _sqlMap.commitTransaction();
        } catch (Exception se) {
            // if something goes wrong, rollback the transaction
            _sqlMap.rollbackTransaction();
            throw new TransRollbackException();
        } finally {
            _objAccountMaintenanceTO = null;
        }
    }

    /**
     * Insert or update the row which contains contra head of this row as
     * account head Also reset contra heads appropriately
     */
    private void contraAspects() throws Exception {
        if (_objAccountMaintenanceTO.getContraAct().length() > 0) {
            logTO.setData(_objAccountMaintenanceTO.toString());
            logTO.setPrimaryKey(_objAccountMaintenanceTO.getKeyData());
            logTO.setStatus(_objAccountMaintenanceTO.getCommand());
            _sqlMap.executeUpdate("accMaintenance.resetContraAcHd", _objAccountMaintenanceTO);
            logDAO.addToLog(logTO);
            final Integer count = (Integer) _sqlMap.executeQueryForList("accMaintenance.getCountContraAcHd", _objAccountMaintenanceTO).get(0);
            if (count.intValue() > 0) {
                logTO.setData(_objAccountMaintenanceTO.toString());
                logTO.setPrimaryKey(_objAccountMaintenanceTO.getKeyData());
                logTO.setStatus(_objAccountMaintenanceTO.getCommand());
                _sqlMap.executeUpdate("accMaintenance.updateContraHdAlone", _objAccountMaintenanceTO);
                logDAO.addToLog(logTO);
            } else {
                logTO.setData(_objAccountMaintenanceTO.toString());
                logTO.setPrimaryKey(_objAccountMaintenanceTO.getKeyData());
                logTO.setStatus(_objAccountMaintenanceTO.getCommand());

                _objAccountMaintenanceTO.setStatus(CommonConstants.STATUS_CREATED);
                _sqlMap.executeUpdate("accMaintenance.insertContraHdAlone", _objAccountMaintenanceTO);
                logDAO.addToLog(logTO);
            }
        }
    }

    private void deleteData() throws Exception {
        try {
            _objAccountMaintenanceTO.setStatus(CommonConstants.STATUS_DELETED);
            _objAccountMaintenanceTO.setStatusBy(userID);
            _objAccountMaintenanceTO.setStatusDt(currDt);
            _sqlMap.startTransaction();
            logTO.setData(_objAccountMaintenanceTO.toString());
            logTO.setPrimaryKey(_objAccountMaintenanceTO.getKeyData());
            logTO.setStatus(_objAccountMaintenanceTO.getCommand());
            _sqlMap.executeUpdate("deleteAccountMaintenanceTO", _objAccountMaintenanceTO);
            logDAO.addToLog(logTO);
            _sqlMap.commitTransaction();
        } catch (Exception se) {
            // if something goes wrong, rollback the transaction
            _sqlMap.rollbackTransaction();
            throw new TransRollbackException();
        } finally {
            _objAccountMaintenanceTO = null;
        }
    }

    public HashMap execute(HashMap param) throws Exception {
        _objAccountMaintenanceTO = (AccountMaintenanceTO) param.get("AccountMaintenanceTO");
        final TOHeader toHeader = _objAccountMaintenanceTO.getTOHeader();
        final String command = _objAccountMaintenanceTO.getCommand();
        logDAO = new LogDAO();
        logTO = new LogTO();
        _branchCode = CommonUtil.convertObjToStr(param.get(CommonConstants.BRANCH_ID));
        currDt = ServerUtil.getCurrentDate(_branchCode);
        logTO.setUserId(CommonUtil.convertObjToStr(param.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(param.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(param.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(param.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(param.get(CommonConstants.SCREEN)));

        userID = CommonUtil.convertObjToStr(param.get(CommonConstants.USER_ID));

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData();
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData();
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        } else {
            _objAccountMaintenanceTO = null;
            throw new NoCommandException();
        }
        return null;
    }

    public HashMap executeQuery(HashMap param) throws Exception {
        final HashMap returnMap = new HashMap();
        final List list = (List) _sqlMap.executeQueryForList("getSelectAccountMaintenanceTO", param);
        returnMap.put("AccountMaintenanceTO", list);
        return returnMap;
    }
    /*
     * public static void main(String str[]) { AccountMaintenanceDAO dao = new
     * AccountMaintenanceDAO(); TOHeader toh = new TOHeader();
     * toh.setCommand("INSERT");
     *
     * AccountMaintenanceTO objAccountMaintenanceTO = new
     * AccountMaintenanceTO(); objAccountMaintenanceTO.setTOHeader(toh);
     * objAccountMaintenanceTO.setAcHdId("62");
     * objAccountMaintenanceTO.setAcHdStatusId("Behavior");
     * objAccountMaintenanceTO.setBalancetype("DR");
     * objAccountMaintenanceTO.setContraAct("51");
     * objAccountMaintenanceTO.setCrCash("Y");
     * objAccountMaintenanceTO.setCrClr("Y");
     * objAccountMaintenanceTO.setCrTrans("Y");
     * objAccountMaintenanceTO.setDrCash("Y");
     * objAccountMaintenanceTO.setDrClr("Y");
     * objAccountMaintenanceTO.setDrTrans("Y");
     * objAccountMaintenanceTO.setFTransDt(new java.util.Date());
     * objAccountMaintenanceTO.setFloatAct("Y");
     * objAccountMaintenanceTO.setGlbalance(new Double("10.0"));
     * objAccountMaintenanceTO.setLTransDt(new java.util.Date());
     * objAccountMaintenanceTO.setPostmode("t");
     * objAccountMaintenanceTO.setRecons("Y");
     * objAccountMaintenanceTO.setTranspost("Tran");
     * objAccountMaintenanceTO.setAcOpenDt(new java.util.Date());
     * objAccountMaintenanceTO.setAcCloseDt(new java.util.Date());
     *
     * HashMap map = new HashMap(); //map.put("WHERE", "P001"); //HashMap map1 =
     * (HashMap) dao.getData(map); //List lst = (List)
     * map1.get("OperativeAcctProductTO");
     *
     * map.put("AccountMaintenanceTO", objAccountMaintenanceTO); try {
     * //dao.insertData(map); dao.execute(map); } catch (Exception e) {
     * e.printStackTrace(); }
    }
     */
}
