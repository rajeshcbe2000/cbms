/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositFreezeDAO.java
 * 
 * Created on Wed Jun 02 18:39:20 GMT+05:30 2004
 */
package com.see.truetransact.serverside.deposit.freeze;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.deposit.freeze.DepositFreezeTO;
import com.see.truetransact.commonutil.CommonUtil;
import java.util.Date;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * DepositFreeze DAO.
 *
 * @author Pinky
 *
 */
public class DepositFreezeDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private DepositFreezeTO objTO;
    private ArrayList freezeTOs;
    HashMap resultMap = new HashMap();
    private Date currDt = null;
    /**
     * Creates a new instance of DepositFreezeDAO
     */
    public DepositFreezeDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String mapStr = (String) map.get(CommonConstants.MAP_NAME);
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        map = null;
        List list = (List) sqlMap.executeQueryForList(mapStr, where);
        returnMap.put(CommonConstants.MAP_NAME, list);
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;
    }

    private void insertData() throws Exception {
        try {
            sqlMap.executeUpdate("insertDepositFreezeTO", objTO);

        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateData(HashMap map) throws Exception {
        try {
            int size = freezeTOs.size();
            sqlMap.startTransaction();
            ArrayList lst = new ArrayList();
            for (int i = 0; i < size; i++) {
                objTO = (DepositFreezeTO) freezeTOs.get(i);
                if (objTO.getFslNo().compareToIgnoreCase("-") == 0) {
                    objTO.setFslNo(this.getFSLNo());
                    System.out.println("###objto" + objTO);
                    insertData();
                    lst.add(objTO.getFslNo());
                    resultMap.put("FREEZE_NO", lst);
                } else {
                    sqlMap.executeUpdate("updateDepositFreezeTO", objTO);
                }
            }
            System.out.println("here 111");
            updateShadowFreeze(map);
            System.out.println("here 222");
            sqlMap.commitTransaction();

        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
    /*
     * private void deleteData() throws Exception { try {
     * sqlMap.executeUpdate("",objTO); } catch (Exception e) {
     * sqlMap.rollbackTransaction(); e.printStackTrace(); throw new
     * TransRollbackException(e); } }
     */

    private void updateDeleteStatusData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            objTO = (DepositFreezeTO) freezeTOs.get(0);
            sqlMap.executeUpdate("updateDeleteFreezeTO", objTO);
            updateShadowFreeze(map);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateShadowFreeze(HashMap map) throws Exception {
        try {
            System.out.println("Execute hereeee");
            objTO = (DepositFreezeTO) freezeTOs.get(0);
            HashMap where = new HashMap();
            where.put("DEPOSITNO", objTO.getDepositNo());
            where.put("SUBDEPOSITNO", objTO.getDepositSubNo());
            where.put("SHADOWFREEZE", map.get("SHADOWFREEZE"));
            sqlMap.executeUpdate("updateShadowFreeze", where);
            where = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private String getFSLNo() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "DEPOSIT_FSLNO");
        String freezeNo = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        where = null;
        return freezeNo;
    }

    private void updateAuthorizeData(HashMap map) throws Exception {
        try {
            String action = CommonUtil.convertObjToStr(map.get("ACTION"));
            String status = CommonUtil.convertObjToStr(map.get("COMMAND_STATUS"));            
            sqlMap.startTransaction();
            if (action.compareToIgnoreCase(CommonConstants.STATUS_EXCEPTION) != 0) {
                sqlMap.executeUpdate("Freeze.updateSubAcInfoBal", map);
            }
            sqlMap.executeUpdate("authorizeFreezeTO", map);
            if (action.compareToIgnoreCase(CommonConstants.STATUS_REJECTED) == 0 && status.compareToIgnoreCase(CommonConstants.STATUS_UNFREEZE) == 0) {
                sqlMap.executeUpdate("rejectUnFreezeUpdate", map);
            }
            //Changed By Kanan AR for JIRA:343
            if(status.equals("UNFREEZED") && action.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)){
                sqlMap.executeUpdate("updateSubACInfoStatusToUnFreeze", map);
            }
            if(status.equalsIgnoreCase(CommonConstants.STATUS_CREATED) && action.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)){
                sqlMap.executeUpdate("updateSubACInfoStatusToFreeze", map);
            }
           //sqlMap.executeUpdate("updateSubACInfoStatusToUnFreeze", map);
           // sqlMap.executeUpdate("updateSubACInfoStatusToFreeze", map);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            DepositFreezeDAO dao = new DepositFreezeDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        map.put("TODAY_DT", ServerUtil.getCurrentDate(_branchCode));
        freezeTOs = (ArrayList) map.get("freezeTOs");
        System.out.println("####FREEZETO" + freezeTOs);
        final String command = (String) map.get("COMMAND");
        if (command.equals(CommonConstants.AUTHORIZEDATA)) {
            updateAuthorizeData(map);
        } else if (freezeTOs != null && freezeTOs.size() > 0) {
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                updateData(map);
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData(map);
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                updateDeleteStatusData(map);
            } else {
                throw new NoCommandException();
            }
        }
        map = null;
        destroyObjects();
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return resultMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
        freezeTOs = null;
    }
}
