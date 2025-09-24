/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GroupScreenDAO.java
 * 
 * Created on Tue Mar 02 17:01:53 GMT+05:30 2004
 */
package com.see.truetransact.serverside.sysadmin.group;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.sysadmin.group.GroupMasterTO;
import com.see.truetransact.transferobject.sysadmin.group.GroupScreensTO;
import com.see.truetransact.transferobject.sysadmin.group.ScreenMasterTO;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * GroupScreenDAO Local Interface
 *
 * @author Pinky
 */
public class GroupScreenDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    GroupMasterTO groupMasterObj;
    private final static Logger log = Logger.getLogger(GroupScreenDAO.class);

    static {
        try {
            ServiceLocator locate = ServiceLocator.getInstance();
            sqlMap = (SqlMap) locate.getDAOSqlMap();
        } catch (ServiceLocatorException se) {
            System.err.println(se);
            //se.printStackTrace(System.err);
            log.error(se);
        }
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        List list = null;
        String mapStr = (String) map.get(CommonConstants.MAP_NAME);

        if (map.get(CommonConstants.MAP_WHERE) instanceof String) {
            String where = (String) map.get(CommonConstants.MAP_WHERE);
            list = (List) sqlMap.executeQueryForList(mapStr, where);
        } else if (map.get(CommonConstants.MAP_WHERE) instanceof HashMap) {
            HashMap whereMap = (HashMap) map.get(CommonConstants.MAP_WHERE);
            list = (List) sqlMap.executeQueryForList(mapStr, whereMap);
        }

        map = null;
        returnMap.put(CommonConstants.MAP_NAME, list);
        return returnMap;
    }

    /**
     * To insert data
     */
    private void insertData(HashMap hashmap) throws Exception {
        try {
            String groupID = getGroupID();
            groupMasterObj.setGroupId(groupID);
            sqlMap.startTransaction();
            sqlMap.executeUpdate("insertGroupMaster", groupMasterObj);
            insertGroupScreenTO(hashmap);
            sqlMap.commitTransaction();
            System.out.println("sucessfully inserted");
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        hashmap = null;
    }

    /**
     * To Update data
     */
    private void updateData(HashMap hashmap) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("updateGroupMaster", groupMasterObj);
            sqlMap.executeUpdate("deleteGroupScreenTO", groupMasterObj.getGroupId());
            insertGroupScreenTO(hashmap);
            sqlMap.commitTransaction();
            System.out.println("Sucessfully Updated");
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        hashmap = null;
    }

    /**
     * To get auto generated GroupID from table
     */
    private String getGroupID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "GROUP_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    /**
     * To delete data
     */
    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("updateStatusGroupScreenTO", groupMasterObj.getGroupId());
            sqlMap.executeUpdate("deleteGroupMaster", groupMasterObj);
            sqlMap.commitTransaction();
            System.out.println("Deleted successfully");
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        groupMasterObj = (GroupMasterTO) map.get("GroupMasterTO");
        final String command = groupMasterObj.getCommand();

        //To find which operation is requested.

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        } else {
            throw new NoCommandException();
        }
        destroyObjects();
        map = null;
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void destroyObjects() {
        groupMasterObj = null;
    }

    private void insertGroupScreenTO(HashMap hashmap) throws Exception {
        try {
            ScreenMasterTO screenMasterTOObj;
//            GroupScreensTO groupScreenTOObj = new GroupScreensTO();
            GroupScreensTO groupScreenTOObj;
            ArrayList screenTO = (ArrayList) hashmap.get("ScreenTO");
//            System.out.println("screenTO:"+screenTO);
            int size = screenTO.size();

            //Insert screens assigned to a particular group in GROUP_SCREEN TABLE.

            for (int i = 0; i < size; i++) {
//                screenMasterTOObj = (ScreenMasterTO)screenTO.get(i);
                groupScreenTOObj = (GroupScreensTO) screenTO.get(i);
//                System.out.println("groupScreenTOObj:"+groupScreenTOObj);
//                groupScreenTOObj.setScreenId(screenMasterTOObj.getScreenId());
                groupScreenTOObj.setGroupId(groupMasterObj.getGroupId());
//                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//                System.out.println("DAO groupScreenTOObj.getScreenId():"+groupScreenTOObj.getScreenId());
//                System.out.println("DAO groupScreenTOObj.getGroupId():"+groupScreenTOObj.getGroupId());
//                System.out.println("DAO groupScreenTOObj.getNewAllowed():"+groupScreenTOObj.getNewAllowed());
//                System.out.println("DAO groupScreenTOObj.getEditAllowed():"+groupScreenTOObj.getEditAllowed());
//                System.out.println("DAO groupScreenTOObj.getDeleteAllowed():"+groupScreenTOObj.getDeleteAllowed());
//                System.out.println("DAO groupScreenTOObj.getAuthRejAllowed():"+groupScreenTOObj.getAuthRejAllowed());
//                System.out.println("DAO groupScreenTOObj.getExceptionAllowed():"+groupScreenTOObj.getExceptionAllowed());
//                System.out.println("DAO groupScreenTOObj.getPrintAllowed():"+groupScreenTOObj.getPrintAllowed());
//                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//                groupScreenTOObj.setNewAllowed(groupScreenTOObj.getNewAllowed());

                groupScreenTOObj.setStatus(CommonConstants.STATUS_CREATED);
                sqlMap.executeUpdate("insertGroupScreenTO", groupScreenTOObj);
            }
            screenMasterTOObj = null;
            groupScreenTOObj = null;
            screenTO = null;
        } catch (Exception e) {
            log.error(e);
            throw new Exception(e);
        }
        hashmap = null;
    }

    public static void main(String str[]) {
        try {
            GroupScreenDAO test = new GroupScreenDAO();
            HashMap hashMap = new HashMap();
            GroupMasterTO grp = new GroupMasterTO();
            grp.setGroupId("GRP00002");
            grp.setGroupName("TESTxyz");
            grp.setCommand(CommonConstants.TOSTATUS_UPDATE);
            hashMap.put("GroupMasterTO", grp);
            test.execute(hashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
