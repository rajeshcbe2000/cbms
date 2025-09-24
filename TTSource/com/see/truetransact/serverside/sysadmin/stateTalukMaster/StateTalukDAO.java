/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OtherBankDAO.java
 *
 * Created on Thu Dec 30 17:43:45 IST 2004
 */
package com.see.truetransact.serverside.sysadmin.stateTalukMaster;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


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

import com.see.truetransact.transferobject.sysadmin.stateTalukMaster.StateTalukTO;

// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

/**
 * OtherBank DAO.
 *
 */
public class StateTalukDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private StateTalukTO objStateTalukTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private LinkedHashMap deletedTaluk = null;// Contains Other Bank Branch Details which the Status is DELETED
    private LinkedHashMap talukDetails = null;
    private LinkedHashMap disDetails = null;
    private LinkedHashMap deletedDistrict = null;// Contains Both Other Bank Details and Other Bank Branch Details
    private final String TO_DELETED_AT_UPDATE_MODE = "TO_DELETED_AT_UPDATE_MODE";
    private final String TO_NOT_DELETED_AT_UPDATE_MODE = "TO_NOT_DELETED_AT_UPDATE_MODE";

    /**
     * Creates a new instance of OtherBankDAO
     */
    public StateTalukDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        List list;
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        int status = CommonUtil.convertObjToInt(map.get("AUTHSTATUS"));
        list = (List) sqlMap.executeQueryForList("getSelectStateDisTO", where);
        if (list != null && list.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            System.out.println("@@@list" + list);
            for (int i = list.size(), j = 0; i > 0; i--, j++) {
                ParMap.put(((StateTalukTO) list.get(j)).getStateSlno(), list.get(j));
            }
            returnMap.put("StateDisTO", ParMap);
        }
        list = null;
//        if(status==8 || status==10){      
//        list = (List) sqlMap.executeQueryForList("getSelectTalukTOAuthorize", where); 
//        }
        list = (List) sqlMap.executeQueryForList("getSelectTaluk", where);
        if (list != null && list.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            System.out.println("@@@list" + list);
            for (int i = list.size(), j = 0; i > 0; i--, j++) {
                String st = ((StateTalukTO) list.get(j)).getStateCode() + "." + ((StateTalukTO) list.get(j)).getDisCode() + "." + ((StateTalukTO) list.get(j)).getTalukCode();
                ParMap.put(((StateTalukTO) list.get(j)).getSlno() + "." + st, list.get(j));
            }
            System.out.println("@@@ParMap" + ParMap);
            returnMap.put("TalukTO", ParMap);
        }
        list = null;

        return returnMap;
    }

    private void insertData(LogDAO logDAO, LogTO logTO, String command) throws Exception {

        try {
            sqlMap.startTransaction();

            if (objStateTalukTO != null) {
                // insert Other Bank
                objStateTalukTO.setStatus(CommonConstants.STATUS_CREATED);
                sqlMap.executeUpdate("insertStateTO", objStateTalukTO);
                logTO.setData(objStateTalukTO.toString());
                logTO.setPrimaryKey(objStateTalukTO.getKeyData());
                logTO.setStatus(command);
                logDAO.addToLog(logTO);
            }
            // insert Other Bank Branch
            insertDistrict(logDAO, logTO, command);
            insertTaluk(logDAO, logTO, command);

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    /**
     * To Insert OtherBankBranch
     */
    private void insertDistrict(LogDAO logDAO, LogTO logTO, String command) throws Exception {

        if (disDetails != null) {
            ArrayList addList = new ArrayList(disDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                StateTalukTO objDisTO = (StateTalukTO) disDetails.get(addList.get(i));
                objDisTO.setDisStatus(CommonConstants.STATUS_CREATED);
                sqlMap.executeUpdate("insertDistrictTO", objDisTO);
                logTO.setData(objDisTO.toString());
                logTO.setPrimaryKey(objDisTO.getKeyData());
                logTO.setStatus(objDisTO.getCommand());
                logDAO.addToLog(logTO);
                objDisTO = null;
            }
        }
    }

    private void insertTaluk(LogDAO logDAO, LogTO logTO, String command) throws Exception {

        if (talukDetails != null) {
            ArrayList addList = new ArrayList(talukDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                StateTalukTO objTalukTO = (StateTalukTO) talukDetails.get(addList.get(i));
                objTalukTO.setTalukStatus(CommonConstants.STATUS_CREATED);
                sqlMap.executeUpdate("insertTalukTO", objTalukTO);
                logTO.setData(objTalukTO.toString());
                logTO.setPrimaryKey(objTalukTO.getKeyData());
                logTO.setStatus(objTalukTO.getCommand());
                logDAO.addToLog(logTO);
                objTalukTO = null;
            }
        }
    }

    /**
     * To Delete OtherBankBranch
     */
    private void deleteOtherBankBranch(LogDAO logDAO, LogTO logTO, String command) throws Exception {
//        if (otherBankBranchTO != null) {
//            for (int i=1,j=otherBankBranchTO.size();i<=j;i++) {
//                OtherBankBranchTO objOtherBankBranchTO = (OtherBankBranchTO) otherBankBranchTO.get(String.valueOf(i));
//                objOtherBankBranchTO.setStatus(CommonConstants.STATUS_DELETED);
//                sqlMap.executeUpdate("deleteOtherBankBranchTO", objOtherBankBranchTO);
//                logTO.setData(objOtherBankBranchTO.toString());
//                logTO.setPrimaryKey(objOtherBankBranchTO.getKeyData());
//                logTO.setStatus(command);
//                logDAO.addToLog(logTO);
//                objOtherBankBranchTO = null;
//            }
//        }
    }

    /**
     * To Update OtherBankBranch
     */
    private void updateDistrict(LogDAO logDAO, LogTO logTO, String command) throws Exception {
        if (deletedDistrict != null) {
            ArrayList addList = new ArrayList(deletedDistrict.keySet());
            for (int i = 0; i < addList.size(); i++) {
                StateTalukTO objDeletedDistrictTO = (StateTalukTO) deletedDistrict.get(addList.get(i));
                objDeletedDistrictTO.setDisStatus(CommonConstants.STATUS_DELETED);
                sqlMap.executeUpdate("deleteDistrictTO", objDeletedDistrictTO);
                logTO.setData(objDeletedDistrictTO.toString());
                logTO.setPrimaryKey(objDeletedDistrictTO.getKeyData());
                logTO.setStatus(command);
                logDAO.addToLog(logTO);
                objDeletedDistrictTO = null;
            }
        }
        if (disDetails != null) {
            ArrayList addList = new ArrayList(disDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                StateTalukTO objDisTO = (StateTalukTO) disDetails.get(addList.get(i));
                System.out.println("@@@objDisTO" + objDisTO);
                logTO.setData(objDisTO.toString());
                logTO.setPrimaryKey(objDisTO.getKeyData());
                logTO.setStatus(command);
                List lst = sqlMap.executeQueryForList("countStateCode", objDisTO);
                int a = CommonUtil.convertObjToInt(lst.get(0));
                System.out.println("@@@coount" + a);
                if (a <= 0) {
                    sqlMap.executeUpdate("insertDistrictTO", objDisTO);
                } else {
                    sqlMap.executeUpdate("updateDistrictTO", objDisTO);
                }
                logDAO.addToLog(logTO);
            }


        }
    }

    private void updateTaluk(LogDAO logDAO, LogTO logTO, String command) throws Exception {
        if (deletedTaluk != null) {
            ArrayList addList = new ArrayList(deletedTaluk.keySet());
            for (int i = 0; i < addList.size(); i++) {
                StateTalukTO objDeletedTalukTO = (StateTalukTO) deletedTaluk.get(addList.get(i));
                objDeletedTalukTO.setTalukStatus(CommonConstants.STATUS_DELETED);
                sqlMap.executeUpdate("deleteTalukTO", objDeletedTalukTO);
                logTO.setData(objDeletedTalukTO.toString());
                logTO.setPrimaryKey(objDeletedTalukTO.getKeyData());
                logTO.setStatus(command);
                logDAO.addToLog(logTO);
                objDeletedTalukTO = null;
            }
        }
        if (talukDetails != null) {
            System.out.println("@@@talukDetails" + talukDetails);
            ArrayList addList = new ArrayList(talukDetails.keySet());
            System.out.println("@@@dtalukDetailsKeySET" + talukDetails);
            for (int i = 0; i < addList.size(); i++) {
                StateTalukTO objTalukTO = (StateTalukTO) talukDetails.get(addList.get(i));
                System.out.println("@@@objTalukTO" + objTalukTO);
                logTO.setData(objTalukTO.toString());
                logTO.setPrimaryKey(objTalukTO.getKeyData());
                logTO.setStatus(command);
                List lst = sqlMap.executeQueryForList("countTalukCode", objTalukTO);
                int a = CommonUtil.convertObjToInt(lst.get(0));
                System.out.println("@@@coount" + a);
                if (a <= 0) {
                    sqlMap.executeUpdate("insertTalukTO", objTalukTO);
                } else {
                    sqlMap.executeUpdate("updateTalukTO", objTalukTO);
                }
                logDAO.addToLog(logTO);
            }

        }
    }

    private void updateData(LogDAO logDAO, LogTO logTO, String command) throws Exception {
        try {

            sqlMap.startTransaction();

            if (objStateTalukTO != null) {
                // update Other Bank
                objStateTalukTO.setStatus(CommonConstants.STATUS_MODIFIED);
                sqlMap.executeUpdate("updateStateTO", objStateTalukTO);
                logTO.setData(objStateTalukTO.toString());
                logTO.setPrimaryKey(objStateTalukTO.getKeyData());
                logTO.setStatus(command);
                logDAO.addToLog(logTO);
            }

            // update Other Bank Branch
            updateDistrict(logDAO, logTO, command);
            updateTaluk(logDAO, logTO, command);


            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData(LogDAO logDAO, LogTO logTO, String command) throws Exception {
        try {
            sqlMap.startTransaction();

            if (objStateTalukTO != null) {
                // delete Other Bank
                objStateTalukTO.setStatus(CommonConstants.STATUS_DELETED);
                sqlMap.executeUpdate("deleteStateTO", objStateTalukTO);
                logTO.setData(objStateTalukTO.toString());
                logTO.setPrimaryKey(objStateTalukTO.getKeyData());
                logTO.setStatus(command);
                logDAO.addToLog(logTO);
            }

            // To delete Other Bank Branch
            deleteOtherBankBranch(logDAO, logTO, command);


            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    //    public static void main(String str[]) {
    //        try {
    //            OtherBankDAO dao = new OtherBankDAO();
    //        } catch (Exception ex) {
    //            ex.printStackTrace();
    //        }
    //    }
    /**
     * Sets the values for logTO Object
     */
    private void setInitialValuesForLogTO(HashMap map) throws Exception {
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        LogDAO logDAO = new LogDAO();
        setInitialValuesForLogTO(map);
        objStateTalukTO = (StateTalukTO) map.get("StateTalukTO");
        talukDetails = (LinkedHashMap) map.get("TalukDetailsTO");
        disDetails = (LinkedHashMap) map.get("DistrictDetailsTO");
        deletedTaluk = (LinkedHashMap) map.get("deletedTalukDetails");
        deletedDistrict = (LinkedHashMap) map.get("deletedDistrictDetails");
        System.out.println("@@@@@@StateTalukTO" + objStateTalukTO);
        System.out.println("@@@@@@TalukDetailsTO" + talukDetails);
        System.out.println("@@@@@@DistrictDetailsTO" + disDetails);
        final String command = (String) map.get("MODE");

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(logDAO, logTO, command);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(logDAO, logTO, command);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData(logDAO, logTO, command);
        } else {
            throw new NoCommandException();
        }
        destroyObjects();
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void destroyObjects() {
        objStateTalukTO = null;
        logTO = null;
        logDAO = null;
        deletedTaluk = null;
        talukDetails = null;
        disDetails = null;


    }
}
