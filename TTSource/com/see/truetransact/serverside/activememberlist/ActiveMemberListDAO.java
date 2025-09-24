/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * AgentDAO.java
 *
 * Created on Wed Feb 02 13:11:28 IST 2005
 */
package com.see.truetransact.serverside.activememberlist;

//import com.see.truetransact.serverside.ActiveMemberList.*;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
import java.lang.Integer;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;

import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.activememberlist.ActiveMemberListTo;

/**
 * Agent DAO.
 *
 */
public class ActiveMemberListDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LogTO logTO;
    private LogDAO logDAO;
    private ActiveMemberListTo objActiveMemberListTo;
    private String userID = "";
    private String remarks = "";
    private String branchCode = "";
    private HashMap agentDetailMap = new HashMap();
    //    private TransferDAO transferDAO;
    private TransactionDAO transactionDAO;
    private ArrayList selectedArrayList;
    private ArrayList deletedArrayList;
    private int totalCount = 0;

    /**
     * Creates a new instance of AgentDAO
     */
    public ActiveMemberListDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();
            int count = 0;
            totalCount = 0;
            HashMap selectMap = new HashMap();
            selectMap.put("BRANCH_CODE", _branchCode);
            List resultList = sqlMap.executeQueryForList("getSelectMaxRecordCount", selectMap);
            if (resultList != null && resultList.size() > 0) {
                selectMap = (HashMap) resultList.get(0);
                count = CommonUtil.convertObjToInt(selectMap.get("MAX_COUNT"));
                count = count + 1;
                if (selectedArrayList != null && selectedArrayList.size() != 0) {
                    for (int i = 0; i < selectedArrayList.size(); i++) {
                        ActiveMemberListTo objActiveMemberListTo = (ActiveMemberListTo) selectedArrayList.get(i);
                        if (objActiveMemberListTo.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                            objActiveMemberListTo.setRecordCount(count);
                            objActiveMemberListTo.setSlNo(i + 1);
                            System.out.println("reached inside : " + objActiveMemberListTo);
                            sqlMap.executeUpdate("insertActiveMemberListTO", objActiveMemberListTo);
                        }
                        totalCount = i + 1;
                    }
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            ActiveMemberListDAO dao = new ActiveMemberListDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        totalCount = 0;
        HashMap returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        System.out.println("ActiveMemberListDAO Execute Method : " + map);
        objActiveMemberListTo = (ActiveMemberListTo) map.get("getActiveMemberListDetails");
        //objTO.setBranCode(_branchCode);
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (map.containsKey("insertActiveMemberList")) {
            selectedArrayList = (ArrayList) map.get("insertActiveMemberList");
            System.out.println("insertActiveMemberList selectedArrayList : " + selectedArrayList);
        }
        if (map.containsKey("updateActiveMemberList")) {
            deletedArrayList = (ArrayList) map.get("updateActiveMemberList");
            System.out.println("updateActiveMemberList deletedArrayList : " + deletedArrayList);
        }
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            // System.out.println("enter inside the insert");
            insertData();
            //returnMap.put("INWARD_NO",  objActiveMemberListTo.getInwardNo());
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData();
        }
        returnMap.put("TOTAL_COUNT", totalCount);
        System.out.println("returnMap : " + returnMap);
        destroyObjects();
        return returnMap;
    }

    private void destroyObjects() {
        objActiveMemberListTo = null;
        deletedArrayList = null;
    }

    @Override
    public HashMap executeQuery(HashMap obj) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();
            if (deletedArrayList != null && deletedArrayList.size() != 0) {
                for (int i = 0; i < deletedArrayList.size(); i++) {
                    ActiveMemberListTo objActiveMemberListTo = (ActiveMemberListTo) deletedArrayList.get(i);
                    System.out.println("reached inside : " + objActiveMemberListTo);
                    sqlMap.executeUpdate("updateShareAccountTO", objActiveMemberListTo);
                    totalCount = i + 1;
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        //		String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectShareAccountTO", map);
        returnMap.put("SelectShareAccountTO", list);
        System.out.println("" + returnMap);
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;
    }
}
