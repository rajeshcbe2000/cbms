/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GLEntryDAO.java
 *
 * Created on Tue Jan 04 11:04:14 IST 2005
 */
package com.see.truetransact.serverside.generalledger.glentry;

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
import com.see.truetransact.transferobject.transaction.common.product.gl.GLTO;
import com.see.truetransact.serverside.transaction.common.product.gl.GLUpdateDAO;

import java.util.HashMap;

/**
 * GLEntry DAO.
 *
 */
public class GLEntryDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private GLTO objTO;
    private final String DEBIT = "DEBIT";
    private final String CREDIT = "CREDIT";

    /**
     * Creates a new instance of GLEntryDAO
     */
    public GLEntryDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public static void main(String str[]) {
        try {
            GLEntryDAO dao = new GLEntryDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Returns all the Data of the DeathMarking table
     */
    private GLTO getGLData(String acHdId, String branchCode) throws Exception {
        HashMap where = new HashMap();
        where.put("AC_HD_ID", acHdId);
        where.put(CommonConstants.BRANCH_ID, branchCode);
        List list = (List) sqlMap.executeQueryForList("getGLData", where);
        return ((GLTO) list.get(0));
    }

    /**
     * This is used to Update the customer table's Customer_Status as
     * 'DeathMarked'
     */
    private void authorizeUpdate(GLTO objGLTO) throws Exception {
        try {
            HashMap updateMap = new HashMap();
            updateMap.put("AC_HD_ID", objGLTO.getAcHdId());
            updateMap.put("BRANCH_ID", objGLTO.getBranchCode());
            if (getBalanceType(objGLTO.getAcHdId()).equals(DEBIT)) {
                updateMap.put("AMOUNT", objGLTO.getShadowDebit());
                updateMap.put("SHADOW_DEBIT", new Double(0));
                System.out.println("Update Map " + updateMap);
                sqlMap.executeUpdate("updateGLDebit", updateMap);
            } else {
                updateMap.put("AMOUNT", objGLTO.getShadowCredit());
                updateMap.put("SHADOW_CREDIT", new Double(0));
                sqlMap.executeUpdate("updateGLCredit", updateMap);
            }

        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw e;
        }
    }

    /**
     * This method is used to do Authoirization
     */
    private void authorize(HashMap map) throws Exception {
        String status = (String) map.get(CommonConstants.AUTHORIZESTATUS);
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap dataMap;
        String acHdId;
        String branchCode;
        GLTO objGLTO = null;
        try {
            sqlMap.startTransaction();
            for (int i = 0; i < selectedList.size(); i++) {
                dataMap = (HashMap) selectedList.get(i);
                acHdId = CommonUtil.convertObjToStr(dataMap.get("AC_HD_ID"));
                branchCode = CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE"));
                dataMap.put(CommonConstants.STATUS, status);
                dataMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                sqlMap.executeUpdate("authorizeGL", dataMap);
                if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                    objGLTO = getGLData(acHdId, branchCode);
                    if (objGLTO.getStatus() == null || objGLTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED) || objGLTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_MODIFIED)) {
                        authorizeUpdate(objGLTO);
                    } else if (status.equalsIgnoreCase(CommonConstants.STATUS_REJECTED)) {
                        objGLTO = getGLData(acHdId, branchCode);
                        if (objGLTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED)
                                || objGLTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_MODIFIED)) {
                            sqlMap.executeUpdate("rejectGL", dataMap);
                        }
                    }
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }

    }

    public HashMap execute(HashMap map) throws Exception {
        if (map.containsKey("GLTO")) {
            objTO = (GLTO) map.get("GLTO");
            final String command = objTO.getCommand();
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                objTO.setStatus(CommonConstants.STATUS_CREATED);
                sqlMap.executeUpdate("insertGLTO", objTO);
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                objTO.setStatus(CommonConstants.STATUS_MODIFIED);
                String balanceType = getBalanceType(objTO.getAcHdId());
                if (balanceType.equals(DEBIT)) {
                    sqlMap.executeUpdate("updateGLShadowDebit", objTO);
                } else if (balanceType.equals(CREDIT)) {
                    sqlMap.executeUpdate("updateGLShadowCredit", objTO);
                }
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                objTO.setStatus(CommonConstants.STATUS_DELETED);
                sqlMap.executeUpdate("deleteGL", objTO);
            }
        } else {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap);
            }
        }
        destroyObjects();
        return null;
    }

    private String getBalanceType(String acHdId) throws Exception {
        HashMap map = new HashMap();
        map.put("AC_HD_ID", acHdId);
        ArrayList list = (ArrayList) sqlMap.executeQueryForList("getSelectBalanceType", map);
        String balanceType = CommonUtil.convertObjToStr(((HashMap) list.get(0)).get("BALANCETYPE"));
        return balanceType;
    }

    public void destroyObjects() {
        objTO = null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return null;
    }
}
