/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MultiLevelDAO.java
 * 
 * Created on Thu Sep 09 15:32:52 GMT+05:30 2004
 */
package com.see.truetransact.serverside.sysadmin.levelcontrol.multilevel;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.sysadmin.levelcontrol.multilevel.MultiLevelMasterTO;
import com.see.truetransact.transferobject.sysadmin.levelcontrol.multilevel.MultiLevelTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
/**
 * MultiLevel DAO.
 *
 * @author Pinky
 *
 */
public class MultiLevelDAO extends TTDAO {
    private Date currDt = null;
    private static SqlMap sqlMap = null;

    /**
     * Creates a new instance of MultiLevelDAO
     */
    public MultiLevelDAO() throws ServiceLocatorException {
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
        return returnMap;
    }

    private void insertData(HashMap map) throws Exception {
        MultiLevelTO objLevelTO;
        MultiLevelMasterTO objTO = (MultiLevelMasterTO) map.get("LevelMasterTO");

        String multiLevelID = getLevelMasteID();
        String userID = (String) map.get(CommonConstants.USER_ID);

        objTO.setLevelMultiId(multiLevelID);
        objTO.setCreatedBy(userID);
        objTO.setStatusBy(userID);
        objTO.setStatus(CommonConstants.STATUS_CREATED);
        objTO.setStatusDt(currDt);
        objTO.setCreatedDt(currDt);
        sqlMap.executeUpdate("insertMultiLevelMasterTO", objTO);

        ArrayList listTOs = (ArrayList) map.get("LevelDetailTOs");
        int size = listTOs.size();

        for (int i = 0; i < size; i++) {
            objLevelTO = (MultiLevelTO) listTOs.get(i);

            objLevelTO.setLevelMultiId(multiLevelID);
            objLevelTO.setLevelOrder(String.valueOf(i + 1));
            //objLevelTO.setStatus(CommonConstants.STATUS_CREATED);
            objLevelTO.setStatusBy(userID);
            objLevelTO.setStatusDt(currDt);
            sqlMap.executeUpdate("insertMultiLevelTO", objLevelTO);
        }
        objLevelTO = null;
        objTO = null;
        listTOs = null;

        map = null;
    }

    private void updateData(HashMap map) throws Exception {
        MultiLevelTO objLevelTO;

        String userID = (String) map.get(CommonConstants.USER_ID);
        MultiLevelMasterTO objTO = (MultiLevelMasterTO) map.get("LevelMasterTO");

        objTO.setStatusBy(userID);
        objTO.setStatus(CommonConstants.STATUS_MODIFIED);
        objTO.setStatusDt(currDt);
        sqlMap.executeUpdate("updateMultiLevelMasterTO", objTO);

        int maxLevelOrder = ((Integer) sqlMap.executeQueryForObject("getMaxLevelOrder", objTO.getLevelMultiId())).intValue();
        ArrayList listTOs = (ArrayList) map.get("LevelDetailTOs");
        int size = listTOs.size();

        for (int i = 0; i < size; i++) {
            objLevelTO = (MultiLevelTO) listTOs.get(i);

            objLevelTO.setStatusBy(userID);

            if (objLevelTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED)
                    && (objLevelTO.getLevelOrder() == null || objLevelTO.getLevelOrder().length() == 0)) {

                objLevelTO.setLevelOrder(String.valueOf(maxLevelOrder + 1));
                maxLevelOrder += 1;
                sqlMap.executeUpdate("insertMultiLevelTO", objLevelTO);
            } else if (objLevelTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_DELETED)) {
                objLevelTO.setStatusDt(currDt);
                sqlMap.executeUpdate("deleteMultiLevelTO", objLevelTO);
            } else if (objLevelTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_MODIFIED)) {
                objLevelTO.setStatusDt(currDt);
                sqlMap.executeUpdate("updateMultiLevelTO", objLevelTO);
            }
        }
        objLevelTO = null;
        objTO = null;
        listTOs = null;
        map = null;
    }

    private void deleteData(HashMap map) throws Exception {
        String userID = (String) map.get(CommonConstants.USER_ID);

        MultiLevelMasterTO objTO = (MultiLevelMasterTO) map.get("LevelMasterTO");

        objTO.setStatusBy(userID);
        objTO.setStatus(CommonConstants.STATUS_DELETED);
        objTO.setStatusDt(currDt);
        sqlMap.executeUpdate("deleteStatusMultiLevelMasterTO", objTO);

        //sqlMap.executeUpdate ("deleteStatusMultiLevelTO",objTO);

        objTO = null;
        map = null;
    }

    private String getLevelMasteID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "MULTI_LEVEL_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    public static void main(String str[]) {
        try {
            //   MultiLevelDAO dao = new MultiLevelDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
            currDt = ServerUtil.getCurrentDate(_branchCode);
            if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
                authorize((HashMap) map.get(CommonConstants.AUTHORIZEMAP));
                map = null;
            } else {
                final String command = (String) map.get("COMMAND");

                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertData(map);
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData(map);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData(map);
                } else {
                    throw new NoCommandException();
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
        return null;
    }

    private void authorize(HashMap map) throws Exception {
        String status = (String) map.get(CommonConstants.AUTHORIZESTATUS);
        ArrayList data = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);

        int size = data.size();

        HashMap hash;
        String statusTO;

        for (int i = 0; i < size; i++) {
            hash = (HashMap) data.get(i);
            statusTO = (String) hash.get("STATUS");

            hash.put("AUTHO_STATUS", status);
            hash.put("USER_ID", (String) map.get(CommonConstants.USER_ID));
            hash.put("AUTHO_DATE", currDt.clone());

            if (status.equalsIgnoreCase(CommonConstants.STATUS_REJECTED) && statusTO.equalsIgnoreCase(CommonConstants.STATUS_DELETED)) {
                hash.put("STATUS", CommonConstants.STATUS_MODIFIED);
                hash.put("AUTHO_STATUS", "");
                hash.put("USER_ID", "");
                hash.put("AUTHO_DATE", null);
            }
            sqlMap.executeUpdate("authorizeMultiLevelMasterTO", hash);
        }
        map = null;
        data = null;
        hash = null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }
}
