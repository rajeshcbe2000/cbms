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
package com.see.truetransact.serverside.sysadmin.branchgroupscr;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.sysadmin.branchgroupscr.BranchGroupMasterTO;
import com.see.truetransact.transferobject.sysadmin.branchgroupscr.BranchGroupScreensTO;
import com.see.truetransact.transferobject.sysadmin.branchgroupscr.ScreenMasterTO;
import com.see.truetransact.commonutil.CommonUtil;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * GroupScreenDAO Local Interface
 *
 * @author Pinky
 */
public class BranchGroupScreenDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    BranchGroupMasterTO groupMasterObj;
    private final static Logger log = Logger.getLogger(BranchGroupScreenDAO.class);

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
            sqlMap.executeUpdate("insertBranchGroupMaster", groupMasterObj);
            insertGroupScreenTO(hashmap);
            sqlMap.commitTransaction();
//            System.out.println("sucessfully inserted");
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
            sqlMap.executeUpdate("updateBranchGroupMaster", groupMasterObj);
//            sqlMap.executeUpdate("deleteBranchGroupScreenTO", groupMasterObj.getGroupId());
            insertGroupScreenTO(hashmap);
            sqlMap.commitTransaction();
//            System.out.println("Sucessfully Updated");
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
            sqlMap.executeUpdate("updateBranchStatusGroupScreenTO", groupMasterObj.getGroupId());
            sqlMap.executeUpdate("deleteBranchGroupMaster", groupMasterObj);
            sqlMap.commitTransaction();
//            System.out.println("Deleted successfully");
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        groupMasterObj = (BranchGroupMasterTO) map.get("GroupMasterTO");
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
            BranchGroupScreensTO groupScreenTOObj = new BranchGroupScreensTO();
            ArrayList screenTO = (ArrayList) hashmap.get("ScreenTO");
            int size = screenTO.size();

            //Insert screens assigned to a particular group in GROUP_SCREEN TABLE.

            for (int i = 0; i < size; i++) {
                screenMasterTOObj = (ScreenMasterTO) screenTO.get(i);
                groupScreenTOObj.setScreenId(screenMasterTOObj.getScreenId());
                groupScreenTOObj.setGroupId(groupMasterObj.getGroupId());
                groupScreenTOObj.setStatus(CommonConstants.STATUS_CREATED);
                if (groupMasterObj.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                    sqlMap.executeUpdate("insertBranchGroupScreenTO", groupScreenTOObj);
                } else if (groupMasterObj.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                    List lst = sqlMap.executeQueryForList("countGroupExists", groupScreenTOObj);
                    int a = CommonUtil.convertObjToInt(lst.get(0));
                    if (a <= 0) {
                        sqlMap.executeUpdate("insertBranchGroupScreenTO", groupScreenTOObj);
                    } else {
                        sqlMap.executeUpdate("updateBranchGroupScreenTO", groupScreenTOObj);
                    }
                }
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
            BranchGroupScreenDAO test = new BranchGroupScreenDAO();
            HashMap hashMap = new HashMap();
            BranchGroupMasterTO grp = new BranchGroupMasterTO();
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
