/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CashTransactionDAO.java
 *
 * Created on Wed Feb 25 16:04:18 IST 2004
 */
package com.see.truetransact.serverside.transaction.common;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;

// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

import com.see.truetransact.transferobject.transaction.common.MultiAuthorizeTO;

import org.apache.log4j.Logger;
import java.util.Date;
/**
 * CashTransaction DAO.
 *
 * @author bala
 *
 */
public class MultiAuthorizeDAO extends TTDAO {

    private SqlMap sqlMap = null;
    private final Logger log = Logger.getLogger(MultiAuthorizeDAO.class);
    private Date currDt = null;
    /**
     * Creates a new instance of CashTransactionDAO
     */
    public MultiAuthorizeDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    /**
     * executeQuery
     */
    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        HashMap returnMap = new HashMap();
        returnMap.put("AUTHORIZE", new Boolean(true));
        return returnMap;
    }

    public HashMap execute(HashMap daoMap) throws Exception {
        String userId = (String) daoMap.get(CommonConstants.USER_ID);
        String branchId = (String) daoMap.get(CommonConstants.BRANCH_ID);
         _branchCode = (String) daoMap.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        HashMap map = (HashMap) daoMap.get(CommonConstants.AUTHORIZEMAP);
        System.out.println("multiautho" + map);
        String status = (String) map.get(CommonConstants.AUTHORIZESTATUS);
        Double transType = CommonUtil.convertObjToDouble(map.get(CommonConstants.TRANSACTION_TYPE));
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        ArrayList selectedUserList = (ArrayList) map.get(CommonConstants.AUTHORIZEUSERLIST);

        HashMap dataMap;
        String transID, authUserId;
        MultiAuthorizeTO objMultiAuthorizeTO;
        List lstExist;
        for (int i = 0, icnt = selectedList.size(); i < icnt; i++) {
            dataMap = (HashMap) selectedList.get(i);
            transID = (String) dataMap.get(TransactionDAOConstants.TRANS_ID);

            objMultiAuthorizeTO = new MultiAuthorizeTO();
            objMultiAuthorizeTO.setTransId(transID);
            objMultiAuthorizeTO.setTransType(transType);
            objMultiAuthorizeTO.setTransDt(currDt);
            objMultiAuthorizeTO.setInitiatedBranch(_branchCode);
            lstExist = sqlMap.executeQueryForList("getSelectMultiAuthorize", objMultiAuthorizeTO);

            boolean currUser = false;

            for (int j = 0, jcnt = selectedUserList.size(); j < jcnt; j++) {
                authUserId = selectedUserList.get(j).toString();

                sqlMap.startTransaction();

//                if (authUserId.equalsIgnoreCase(userId)) currUser = true;

                if (!isUserExist(lstExist, authUserId)) {
                    objMultiAuthorizeTO = new MultiAuthorizeTO();
                    objMultiAuthorizeTO.setTransId(transID);
                    objMultiAuthorizeTO.setTransType(transType);
                    objMultiAuthorizeTO.setAuthUserId(authUserId);

                    System.out.println("objMultiAuthorizeTO:" + objMultiAuthorizeTO.toString());
                    objMultiAuthorizeTO.setTransDt(currDt);
                    objMultiAuthorizeTO.setInitiatedBranch(_branchCode);
                    sqlMap.executeUpdate("insertMultiAuthorize", objMultiAuthorizeTO);
                } else {
                    objMultiAuthorizeTO = new MultiAuthorizeTO();
                    objMultiAuthorizeTO.setTransId(transID);
                    objMultiAuthorizeTO.setTransType(transType);
                    objMultiAuthorizeTO.setAuthUserId(authUserId);
                    objMultiAuthorizeTO.setStatus(status);

                    System.out.println("objMultiAuthorizeTO:" + objMultiAuthorizeTO.toString());
                    objMultiAuthorizeTO.setTransDt(currDt);
                    objMultiAuthorizeTO.setInitiatedBranch(_branchCode);
                    sqlMap.executeUpdate("updateMultiAuthorize", objMultiAuthorizeTO);
                }
                sqlMap.commitTransaction();
            }

//            sqlMap.startTransaction();
//            if (!currUser) {
//                objMultiAuthorizeTO = new MultiAuthorizeTO();
//                objMultiAuthorizeTO.setTransId(transID);
//                objMultiAuthorizeTO.setTransType(transType);
//                objMultiAuthorizeTO.setAuthUserId(userId);
//                objMultiAuthorizeTO.setStatus(status);
//                
//                System.out.println ("objMultiAuthorizeTO:" + objMultiAuthorizeTO.toString());
//                sqlMap.executeUpdate("insertMultiAuthorize", objMultiAuthorizeTO);
//            }
//            sqlMap.commitTransaction();
        }
        map = null;
        return null;
    }

    private boolean isUserExist(List existList, String authUserId) {
        boolean exist = false;
        int len = existList.size();
        if (existList != null && len > 0) {
            MultiAuthorizeTO objMultiAuthorizeTO;
            for (int i = 0; i < len; i++) {
                objMultiAuthorizeTO = (MultiAuthorizeTO) existList.get(i);
                if (objMultiAuthorizeTO.getAuthUserId().equalsIgnoreCase(authUserId)) {
                    exist = true;
                    break;
                }
                objMultiAuthorizeTO = null;
            }
        }
        return exist;
    }
}