/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GroupRoleDAO.java
 * 
 * Created on Mon Apr 12 15:49:21 GMT+05:30 2004
 */
package com.see.truetransact.serverside.sysadmin.role;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.sysadmin.role.GroupMasterTO;
import com.see.truetransact.transferobject.sysadmin.role.RoleMasterTO;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

import org.apache.log4j.Logger;
import java.util.Date;
/**
 * GroupRole DAO.
 *
 * @author Pinky
 *
 */
public class GroupRoleDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    GroupMasterTO groupMasterObj;
    private String userID = "";
    private final static Logger log = Logger.getLogger(GroupRoleDAO.class);
    private Date currDt = null;
    /**
     * Creates a new instance of roleDAO
     */
    public GroupRoleDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String mapStr = (String) map.get(CommonConstants.MAP_NAME);
//        String where = (String) map.get(CommonConstants.MAP_WHERE);
        final HashMap dataMap = (HashMap) map.get(CommonConstants.MAP_WHERE);
        map = null;
//        List list = (List) sqlMap.executeQueryForList(mapStr, where);
        System.out.println("dataMap in RoleMaster DAO: " + dataMap);
        List list = (List) sqlMap.executeQueryForList(mapStr, dataMap);
        returnMap.put(CommonConstants.MAP_NAME, list);
        return returnMap;
    }

    private void insertData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        System.out.println("map===========" + map);
        RoleMasterTO roleMasterObj;
        ArrayList roleTOs = (ArrayList) map.get("RoleTOs");
        System.out.println("roleTOs=====" + roleTOs);
        int size = roleTOs.size();
        try {
            if (map.containsKey("RoleDeleted")) {
//                System.out.println("CommonUtil.convertObjToStr(map.get(RoleDeleted))"+CommonUtil.convertObjToStr(map.get("RoleDeleted")));

                System.out.println("RoleDeletedRoleDeletedRoleDeletedRoleDeleted");
                ArrayList deletedRoles = (ArrayList) map.get("RoleDeleted");
                int listSize = deletedRoles.size();
                for (int i = 0; i < listSize; i++) {
                    RoleMasterTO objTO = (RoleMasterTO) deletedRoles.get(i);
                    sqlMap.executeUpdate("deleteRole_Master", objTO);

                }
            }
            //Insert roles created for a particular group.
            for (int i = 0; i < size; i++) {
                roleMasterObj = (RoleMasterTO) roleTOs.get(i);
//                roleMasterObj.setStatus(CommonConstants.STATUS_CREATED);
                roleMasterObj.setStatusBy(userID);
                roleMasterObj.setStatusDt(currDt);
                sqlMap.executeUpdate("insertRoleMasterTO", roleMasterObj);
            }

            if (map.containsKey("EditRoleDeleted")) {
//                System.out.println("CommonUtil.convertObjToStr(map.get(RoleDeleted))"+CommonUtil.convertObjToStr(map.get("RoleDeleted")));

                System.out.println("RoleDeletedRoleDeletedRoleDeletedRoleDeleted");
                ArrayList deletedRoles = (ArrayList) map.get("EditRoleDeleted");
                int listSize = deletedRoles.size();
                for (int i = 0; i < listSize; i++) {
                    RoleMasterTO objTO = (RoleMasterTO) deletedRoles.get(i);
                    sqlMap.executeUpdate("deleteRole_Master", objTO);

                }
            }

        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
        roleMasterObj = null;
        roleTOs = null;
    }

    private void updateData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();

            sqlMap.executeUpdate("deleteRoleMasterTO", groupMasterObj.getGroupId());
            insertData(map, objLogDAO, objLogTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void deleteData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();
            RoleMasterTO roleMasterObj = new RoleMasterTO();
            roleMasterObj.setGroupId(CommonUtil.convertObjToStr(groupMasterObj.getGroupId()));
            roleMasterObj.setStatus(CommonConstants.STATUS_DELETED);
            roleMasterObj.setStatusBy(userID);
            roleMasterObj.setStatusDt(currDt);

            sqlMap.executeUpdate("updateStatusRoleMasterTO", roleMasterObj);

//            sqlMap.executeUpdate("updateStatusRoleMasterTO",groupMasterObj.getGroupId());
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            GroupRoleDAO dao = new GroupRoleDAO();
            HashMap map = new HashMap();
            System.out.println(dao.executeQuery(map));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        // Log DAO
        LogDAO objLogDAO = new LogDAO();

        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        objLogTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        objLogTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        objLogTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        objLogTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

        userID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));

        groupMasterObj = (GroupMasterTO) map.get("GroupMasterTO");
        final String command = groupMasterObj.getCommand();
        System.out.println("command====" + command);
        if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map, objLogDAO, objLogTO);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData(objLogDAO, objLogTO);
        } else {
            throw new NoCommandException();
        }
        destroyObjects();
        map = null;
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        groupMasterObj = null;
    }
}