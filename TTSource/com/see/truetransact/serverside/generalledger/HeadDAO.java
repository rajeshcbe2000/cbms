/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * HeadDAO.java
 *
 * Created on August 25, 2003, 12:35 PM
 */
package com.see.truetransact.serverside.generalledger;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.transferobject.common.lookup.LookUpTO;
import com.see.truetransact.servicelocator.*;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.transferobject.generalledger.HeadTO;
import com.see.truetransact.transferobject.generalledger.SubHeadTO;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;



import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Date;
/**
 *
 * @author Annamalai
 */
public class HeadDAO extends TTDAO {

    HeadTO _headTO;
    SubHeadTO _subHeadTO;
    HashMap _subHeadList;
    private LogDAO logDAO;
    private LogTO logTO;
    static SqlMap _sqlMap;
    private String userID = "";
    private Date currDt = null;
    /**
     * Creates a new instance of HeadDAO
     */
    public HeadDAO() throws ServiceLocatorException {
        final ServiceLocator locate = ServiceLocator.getInstance();
        _sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public HashMap executeQuery(HashMap param) throws Exception {
        _branchCode = (String) param.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        final HashMap returnMap = new HashMap();
        List list = (List) _sqlMap.executeQueryForList("getSelectMajorHeadTO", (String) param.get("WHERE"));
        returnMap.put("MajorHeadTO", list);
        list = (List) _sqlMap.executeQueryForList("getSelectSubHeadTO", (String) param.get("WHERE"));
        returnMap.put("SubHeadTO", list);
        return returnMap;
    }

    private void insertData() throws Exception {
        _headTO.setStatus(CommonConstants.STATUS_CREATED);
        _headTO.setStatusBy(userID);
        _headTO.setStatusDt(currDt);

        _subHeadList = _headTO.getSubHeadList();
        try {
            _sqlMap.startTransaction();
            logTO.setData(_headTO.toString());
            logTO.setPrimaryKey(_headTO.getKeyData());
            logTO.setStatus(_headTO.getCommand());
            _sqlMap.executeUpdate("insertMajorHead", _headTO);
            logDAO.addToLog(logTO);

            if (_subHeadList != null) {
                operateSubHeadList();
            }
            _sqlMap.commitTransaction();
        } catch (Exception se) {
            // if something goes wrong, rollback the transaction
            _sqlMap.rollbackTransaction();
            throw new TransRollbackException();
        } finally {
            _headTO = null;
        }
    }

    private void updateData() throws Exception {
        _headTO.setStatus(CommonConstants.STATUS_MODIFIED);
        _headTO.setStatusBy(userID);
        _headTO.setStatusDt(currDt);

        _subHeadList = _headTO.getSubHeadList();
        try {
            _sqlMap.startTransaction();
            logTO.setData(_headTO.toString());
            logTO.setPrimaryKey(_headTO.getKeyData());
            logTO.setStatus(_headTO.getCommand());
            _sqlMap.executeUpdate("updateMajorHead", _headTO);
            logDAO.addToLog(logTO);
            if (_subHeadList != null) {
                operateSubHeadList();
            }
            _sqlMap.commitTransaction();
        } catch (Exception se) {
            // if something goes wrong, rollback the transaction
            _sqlMap.rollbackTransaction();
            throw new TransRollbackException();
        } finally {
            _headTO = null;
        }
    }

    private void deleteData() throws Exception {
        _headTO.setStatus(CommonConstants.STATUS_DELETED);
        _headTO.setStatusBy(userID);
        _headTO.setStatusDt(currDt);

        try {
            _sqlMap.startTransaction();
            logTO.setData(_headTO.toString());
            logTO.setPrimaryKey(_headTO.getKeyData());
            logTO.setStatus(_headTO.getCommand());
            _sqlMap.executeUpdate("deleteMajorHead", _headTO);
            logDAO.addToLog(logTO);
            logTO.setData(_headTO.toString());
            logTO.setPrimaryKey(_headTO.getKeyData());
            logTO.setStatus(_headTO.getCommand());
            _sqlMap.executeUpdate("deleteManySubHead", _headTO);
            logDAO.addToLog(logTO);
            _sqlMap.commitTransaction();
        } catch (Exception se) {
            // if something goes wrong, rollback the transaction
            _sqlMap.rollbackTransaction();
            throw new TransRollbackException();
        } finally {
            _headTO = null;
        }
    }

    private void operateSubHeadList() throws Exception {
        final Object[] keyList = (_subHeadList.keySet()).toArray();
        final int keyListLength = keyList.length;
        for (int i = 0; i < keyListLength; i++) {
            _subHeadTO = (SubHeadTO) _subHeadList.get((String) (keyList[i]));
            operationSubHead();
            _subHeadTO = null;
        }
        _subHeadList = null;
    }

    private void operationSubHead() throws Exception {
        final String command = _subHeadTO.getCommand();
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            _subHeadTO.setStatus(CommonConstants.STATUS_CREATED);
            logTO.setData(_subHeadTO.toString());
            logTO.setPrimaryKey(_subHeadTO.getKeyData());
            logTO.setStatus(_subHeadTO.getCommand());
            _sqlMap.executeUpdate("insertSubHead", _subHeadTO);
            logDAO.addToLog(logTO);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            _subHeadTO.setStatus(CommonConstants.STATUS_MODIFIED);
            logTO.setData(_subHeadTO.toString());
            logTO.setPrimaryKey(_subHeadTO.getKeyData());
            logTO.setStatus(_subHeadTO.getCommand());
            _sqlMap.executeUpdate("updateSubHead", _subHeadTO);
            logDAO.addToLog(logTO);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            _subHeadTO.setStatus(CommonConstants.STATUS_DELETED);
            logTO.setData(_subHeadTO.toString());
            logTO.setPrimaryKey(_subHeadTO.getKeyData());
            logTO.setStatus(_subHeadTO.getCommand());
            _sqlMap.executeUpdate("deleteSubHead", _subHeadTO);
            logDAO.addToLog(logTO);
        }
    }

    public HashMap execute(HashMap param) throws Exception {
        logDAO = new LogDAO();
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(param.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(param.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(param.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(param.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(param.get(CommonConstants.SCREEN)));

        userID = CommonUtil.convertObjToStr(param.get(CommonConstants.USER_ID));
        _branchCode = (String) param.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        _headTO = (HeadTO) param.get("HEAD");
        final TOHeader toHeader = _headTO.getTOHeader();
        final String command = _headTO.getCommand();
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData();
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData();
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        } else {
            _headTO = null;
            throw new NoCommandException();
        }
        return null;
    }
    /*
     * public static void main(String str[]) { try { HeadDAO head = new
     * HeadDAO(); HeadTO headTO = new HeadTO(); headTO.setAccountType("Income");
     * headTO.setMajorHead("1539"); headTO.setMajorHeadDesc("test1539");
     * headTO.setStatus("CREATED");
     *
     * SubHeadTO subHeadTO = new SubHeadTO(); subHeadTO.setMajorHead("1539");
     * subHeadTO.setSubHead("1539"); subHeadTO.setSubHeadDesc("Test1539");
     * subHeadTO.setStatus("CREATED");
     *
     * TOHeader toHeader = new TOHeader(); toHeader.setCommand("INSERT");
     * headTO.setTOHeader(toHeader);
     *
     * //toHeader.setCommand("INSERT"); //subHeadTO.setTOHeader(toHeader);
     *
     * HashMap hash = new HashMap(); hash.put("1539", subHeadTO);
     *
     * headTO.addSubHead(subHeadTO, "INSERT"); HashMap headHash = new HashMap();
     * headHash.put("HEAD",headTO); head.execute(headHash);
     *
     * //--------------------------------------------------- headTO = new
     * HeadTO(); headTO.setAccountType("Income"); headTO.setMajorHead("1539");
     * headTO.setMajorHeadDesc("test01539"); headTO.setStatus("MODIFIED");
     *
     * subHeadTO = new SubHeadTO(); subHeadTO.setMajorHead("1539");
     * subHeadTO.setSubHead("1539"); subHeadTO.setSubHeadDesc("Test001539");
     * subHeadTO.setStatus("MODIFIED");
     *
     * toHeader = new TOHeader(); toHeader.setCommand("UPDATE");
     * headTO.setTOHeader(toHeader);
     *
     * hash = new HashMap(); hash.put("1539", subHeadTO);
     *
     * headTO.addSubHead(subHeadTO, "UPDATE"); headHash = new HashMap();
     * headHash.put("HEAD",headTO); head.execute(headHash);
     *
     * //-------------------------------------------------- headTO = new
     * HeadTO(); headTO.setAccountType("Income"); headTO.setMajorHead("1539");
     * headTO.setMajorHeadDesc("test01489"); headTO.setStatus("DELETED");
     *
     * subHeadTO = new SubHeadTO(); subHeadTO.setMajorHead("1539");
     * subHeadTO.setSubHead("1539"); subHeadTO.setSubHeadDesc("Test0489");
     * subHeadTO.setStatus("DELETED");
     *
     * toHeader = new TOHeader(); toHeader.setCommand("DELETE");
     * headTO.setTOHeader(toHeader);
     *
     * headHash = new HashMap(); headHash.put("HEAD",headTO);
     * head.execute(headHash);
     *
     *
     * } catch(Exception ex) { ex.printStackTrace(); }
    }
     */
}
