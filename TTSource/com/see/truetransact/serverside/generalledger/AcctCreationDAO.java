/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AcctCreationDAO.java
 *
 * Created on August 18, 2003, 4:19 PM
 */
package com.see.truetransact.serverside.generalledger;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.transferobject.common.lookup.LookUpTO;
import com.see.truetransact.servicelocator.*;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.transferobject.generalledger.AccountCreationTO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

import com.see.truetransact.serverexception.ServiceLocatorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
/**
 *
 * @author Annamalai
 */
public class AcctCreationDAO extends TTDAO {

    private AccountCreationTO _objAccountCreationTO;
    static SqlMap _sqlMap;
    private LogTO logTO;
    private LogDAO logDAO;
    private String userID = "";
    private Date currDt = null;
    /**
     * Creates a new instance of AcctCreationDAO
     */
    public AcctCreationDAO() throws ServiceLocatorException {
        final ServiceLocator locate = ServiceLocator.getInstance();
        _sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public HashMap executeQuery(HashMap param) throws Exception {
        final HashMap acctCreationMap = new HashMap();
        final List list = (List) _sqlMap.executeQueryForList("getAccountCreationDetails", param);
        acctCreationMap.put("AccountCreationDetails", list);
        return acctCreationMap;
    }

    private void insertData() throws Exception {
        _objAccountCreationTO.setCreatedDt(currDt);
        _objAccountCreationTO.setStatus(CommonConstants.STATUS_CREATED);
        _objAccountCreationTO.setStatusBy(userID);
        _objAccountCreationTO.setStatusDt(currDt);
        try {
            _sqlMap.startTransaction();
            logTO.setData(_objAccountCreationTO.toString());
            logTO.setPrimaryKey(_objAccountCreationTO.getKeyData());
            logTO.setStatus(_objAccountCreationTO.getCommand());
            _sqlMap.executeUpdate("insertAccountCreation", _objAccountCreationTO);
            logDAO.addToLog(logTO);
            _sqlMap.commitTransaction();
        } catch (Exception se) {
            // if something goes wrong, rollback the transaction
            _sqlMap.rollbackTransaction();
            throw new TransRollbackException();
        } finally {
            _objAccountCreationTO = null;
        }
    }

    private void updateData() throws Exception {
        _objAccountCreationTO.setStatus(CommonConstants.STATUS_MODIFIED);
        _objAccountCreationTO.setStatusBy(userID);
        _objAccountCreationTO.setStatusDt(currDt);
        try {
            _sqlMap.startTransaction();
            logTO.setData(_objAccountCreationTO.toString());
            logTO.setPrimaryKey(_objAccountCreationTO.getKeyData());
            logTO.setStatus(_objAccountCreationTO.getCommand());
            _sqlMap.executeUpdate("updateAccountCreation", _objAccountCreationTO);
            logDAO.addToLog(logTO);
            _sqlMap.commitTransaction();

        } catch (Exception se) {
            // if something goes wrong, rollback the transaction
            _sqlMap.rollbackTransaction();
            throw new TransRollbackException();
        } finally {
            _objAccountCreationTO = null;
        }
    }

    private void deleteData() throws Exception {
        _objAccountCreationTO.setDeletedDt(currDt);
        _objAccountCreationTO.setStatus(CommonConstants.STATUS_DELETED);
        _objAccountCreationTO.setStatusBy(userID);
        _objAccountCreationTO.setStatusDt(currDt);
        try {
            _sqlMap.startTransaction();
            logTO.setData(_objAccountCreationTO.toString());
            logTO.setPrimaryKey(_objAccountCreationTO.getKeyData());
            logTO.setStatus(_objAccountCreationTO.getCommand());
            _sqlMap.executeUpdate("deleteAccountCreation", _objAccountCreationTO);
            logDAO.addToLog(logTO);
            _sqlMap.commitTransaction();
        } catch (Exception se) {
            // if something goes wrong, rollback the transaction
            _sqlMap.rollbackTransaction();
            throw new TransRollbackException();
        } finally {
            _objAccountCreationTO = null;
        }
    }

    public HashMap execute(HashMap param) throws Exception {
        _objAccountCreationTO = (AccountCreationTO) param.get("ACTCREATION");
        final TOHeader toHeader = _objAccountCreationTO.getTOHeader();
        final String command = _objAccountCreationTO.getCommand();
        logDAO = new LogDAO();
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(param.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(param.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(param.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(param.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(param.get(CommonConstants.SCREEN)));
        _branchCode = CommonUtil.convertObjToStr(param.get(CommonConstants.BRANCH_ID));
        currDt = ServerUtil.getCurrentDate(_branchCode);
        userID = CommonUtil.convertObjToStr(param.get(CommonConstants.USER_ID));

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData();
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData();
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        } else {
            _objAccountCreationTO = null;
            throw new NoCommandException();
        }
        return null;
    }
    /*
     * public static void main(String str[]) { try { AcctCreationDAO
     * accountCreationDAO = new AcctCreationDAO(); AccountCreationTO
     * accountCreationTO = new AccountCreationTO(); TOHeader toHeader = new
     * TOHeader(); toHeader.setCommand(CommonConstants.TOSTATUS_INSERT);
     * accountCreationTO.setAccountType("Income");
     * accountCreationTO.setMajorHead("1435");
     * accountCreationTO.setSubHead("1435");
     * accountCreationTO.setAccountHead("455");
     * accountCreationTO.setAccountHeadDesc("test455");
     *
     *
     * accountCreationTO.setCallingCode("test455");
     * accountCreationTO.setCreatedDate(DateUtil.getDateMMDDYYYY(null));
     * accountCreationTO.setStatus(CommonConstants.STATUS_CREATED);
     * accountCreationTO.setTOHeader(toHeader);
     *
     * HashMap hash = new HashMap(); hash.put("ACTCREATION",accountCreationTO);
     *
     * accountCreationDAO.execute(hash); } catch(Exception e) {
     * e.printStackTrace(); }
    }
     */
}