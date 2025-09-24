/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LookupMasterTODAO.java
 *
 * Created on Fri Feb 27 14:45:44 IST 2004
 */
package com.see.truetransact.serverside.sysadmin.lookup;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.sysadmin.lookup.LookupMasterTO;

// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

/**
 * LookupMasterTO DAO.
 *
 */
public class LookupMasterDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LookupMasterTO objTO;
    private ArrayList objLookupMasterTab;
    private List list;
    private String AUTH_KEY = "AUTH_KEY";
    private final String AUTHORIZED = "Y";
    private final String REJECTED = "N";

    /**
     * Creates a new instance of LookupMasterTODAO
     */
    public LookupMasterDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    /**
     * Method for the Selection
     *
     * @throws Exception
     */
    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        //        list = (List) sqlMap.executeQueryForList("getSelectLookupMasterTO", where);
        //        returnMap.put("LookupMasterTO", list);
        list = (List) sqlMap.executeQueryForList("ViewLookupMaster", where);
        returnMap.put("LookupMasterTO", list);
        //Added By Kannan AR
        list = (List) sqlMap.executeQueryForList("ViewLookupMasterDesc", where);
        if(list!=null && list.size()>0){
            returnMap.put("LookupMasterDesc", list);  
        }
        map = null;
        where = null;
        list = null;
        return returnMap;
    }

    /**
     * Method for the Insertion
     *
     * @throws Exception
     */
    private void insertData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            sqlMap.executeUpdate("insertLookupMasterTO", objTO);

            objLogTO.setData(objTO.toString());
            objLogTO.setPrimaryKey(objTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    /**
     * Method for the Updation
     *
     * @throws Exception
     */
    private void updateData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            sqlMap.executeUpdate("updateLookupMasterTO", objTO);

            objLogTO.setData(objTO.toString());
            objLogTO.setPrimaryKey(objTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    /**
     * Method for the Deletion
     *
     * @throws Exception
     */
    private void deleteData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_DELETED);
            sqlMap.executeUpdate("deleteLookupMasterTO", objTO);

            objLogTO.setData(objTO.toString());
            objLogTO.setPrimaryKey(objTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    /**
     * This method does the insertion, updatation or deletion from the data base
     *
     * @param map the argument is passed as selected LOOK_ID
     */
    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);

        System.out.println("In execute()"+map);

        // Log DAO
        LogDAO objLogDAO = new LogDAO();

        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        objLogTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        objLogTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        objLogTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        objLogTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));


        if (map.containsKey("LookupMasterTO")) {
            objLookupMasterTab = (ArrayList) map.get("LookupMasterTO");
            String command;
            for (int i = 0; i < objLookupMasterTab.size(); i++) {
                objTO = (LookupMasterTO) objLookupMasterTab.get(i);
                command = objTO.getCommand();
                if (i == 0) {
                    HashMap lookDescMap = new HashMap();
                    lookDescMap.put("LOOKUP_ID", CommonUtil.convertObjToStr(objTO.getLookupId()));
                    List cntList = sqlMap.executeQueryForList("getLookUpDescRecordCnt", lookDescMap);
                    if (cntList != null && cntList.size() > 0) {
                        lookDescMap = (HashMap) cntList.get(0);
                        if (CommonUtil.convertObjToInt(lookDescMap.get("RECORD_COUNT")) == 0) {
                            lookDescMap.put("LOOKUP_ID", CommonUtil.convertObjToStr(objTO.getLookupId()));
                            lookDescMap.put("LOOKUP_DESC", CommonUtil.convertObjToStr(objTO.getLookupDesc()));
                            lookDescMap.put("EDITABLE", "N");
                            lookDescMap.put("STATUS_BY", CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                            lookDescMap.put("STATUS_DT", ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID))));
                            lookDescMap.put("AUTHORIZE_STATUS", "AUTHORIZED");                            
                            sqlMap.executeUpdate("insertLookupMasterDesc", lookDescMap);
                        }
                    }
                }
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertData(objLogDAO, objLogTO);
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData(objLogDAO, objLogTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData(objLogDAO, objLogTO);
                } else {
                    throw new NoCommandException();
                }
            }
        }

        if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                System.out.println("Auth map != null");
                authorize(authMap, objLogDAO, objLogTO);
            }
        }


        map = null;
        objLogDAO = null;
        objLogTO = null;
        destroyObjects();
        return null;

    }

    /**
     * This method does the selection from the data base
     *
     * @param obj the argument is passed as selected LOOK_ID
     */
    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
        objLookupMasterTab = null;
    }

    private void authorize(HashMap authMap, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        if (authMap != null) {
            System.out.println("authorize map is not null");
            HashMap dataMap;
            final String status = CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS));
            final String USER_ID = CommonUtil.convertObjToStr(objLogTO.getUserId());

            ArrayList selectedList = (ArrayList) authMap.get(CommonConstants.AUTHORIZEDATA);
            System.out.println("list Size: " + selectedList.size());
            for (int i = 0, j = selectedList.size(); i < j; i++) {
                dataMap = (HashMap) selectedList.get(i);

                dataMap.put(CommonConstants.AUTHORIZESTATUS, status);
                dataMap.put(CommonConstants.USER_ID, USER_ID);

                //__ To Authorize the Data in the Lookup_Master_Desc...
                //                sqlMap.executeUpdate("authInventoryDetails", dataMap);

                if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                    dataMap.put(AUTH_KEY, AUTHORIZED);

                } else if (status.equalsIgnoreCase(CommonConstants.STATUS_REJECTED)) {
                    dataMap.put(AUTH_KEY, REJECTED);

                }

                System.out.println("Before Auth dataMap: " + dataMap);
                //__ To Authorize the Data in the Lookup_Master...
                sqlMap.executeUpdate("authorizeLookUpMasterData", dataMap);


                //__ To Add the data in the Log...
                objLogTO.setStatus(status);
                objLogTO.setData(dataMap.toString());
                objLogTO.setPrimaryKey(CommonUtil.convertObjToStr(dataMap.get("LOOKUP ID")));
                objLogDAO.addToLog(objLogTO);
            }

        }
    }
}
