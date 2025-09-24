/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * BorrwingDAO.java
 *
 * Created on Thu Jan 20 17:19:05 IST 2005
 */
package com.see.truetransact.serverside.termloan.personalSuretyConfiguration.serviceWiseLoanIssue;

import java.util.List;
import java.util.ArrayList;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
//import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
//import com.see.truetransact.serverside.common.log.LogDAO;
//import com.see.truetransact.transferobject.common.log.LogTO;

import com.see.truetransact.transferobject.termloan.personalSuretyConfiguration.serviceWiseLoanIssue.ServiceWiseLoanIssueConfigurationTO;
import java.util.HashMap;

/**
 * TokenConfig DAO.
 *
 */
public class ServiceWiseLoanIssueConfigurationDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private ServiceWiseLoanIssueConfigurationTO objTO;

    //  private LogDAO logDAO;
    // private LogTO logTO;
    /**
     * Creates a new instance of TokenConfigDAO
     */
    public ServiceWiseLoanIssueConfigurationDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap where = (HashMap) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectESLI", where);
        returnMap.put("ServiceWiseLoanIssueConfigurationTO", list);
        List list1 = (List) sqlMap.executeQueryForList("getSelectESLIProdDesc", where);
        returnMap.put("list1", list1);
        return returnMap;
    }

    private String getIMBPID() throws Exception {
        //  final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "ESLI_ID");
        // return "";
        HashMap map = generateIMBPID();
        //System.out.println("MAP IN DAOOOOOO=========="+map);
        return (String) map.get(CommonConstants.DATA);
        //  return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private String getGeneralBoadyID() throws Exception {
        //  final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "PSCID");
        // return "";
        HashMap map = generateID();
        //System.out.println("MAP IN DAOOOOOO=========="+map);
        return (String) map.get(CommonConstants.DATA);
        //  return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    public HashMap generateIMBPID() {

        HashMap hash = null, result = null;
        try {
            String mapName = "getCurrentID";
            HashMap where = new HashMap();
            where.put("ID_KEY", "ELSI_ID"); //Here u have to pass BORROW_ID or something else
            List list = null;

            //sqlMap.startTransaction();
            sqlMap.executeUpdate("updateIDGenerated", where);  // This update statement just updates curr_value=curr_value+1
            list = (List) sqlMap.executeQueryForList(mapName, where);  // This will get u the updated curr_value, prefix and length
            //sqlMap.commitTransaction();

            if (list.size() > 0) {
                hash = (HashMap) list.get(0);
                String strPrefix = "", strLen = "";

                // Prefix for the ID.
                if (hash.containsKey("PREFIX")) {
                    strPrefix = (String) hash.get("PREFIX");
                    if (strPrefix == null || strPrefix.trim().length() == 0) {
                        strPrefix = "";
                    }
                }

                // Maximum Length for the ID
                int len = 10;
                if (hash.containsKey("ID_LENGTH")) {
                    strLen = String.valueOf(hash.get("ID_LENGTH"));
                    if (strLen == null || strLen.trim().length() == 0) {
                        len = 10;
                    } else {
                        len = Integer.parseInt(strLen.trim());
                    }
                }

                int numFrom = strPrefix.trim().length();

                String newID = String.valueOf(hash.get("CURR_VALUE"));

                // Number Part of the String and incrementing 1 (ex. only 00085 from OGGOT00085)
                result = new HashMap();
                String genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
                result.put(CommonConstants.DATA, genID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public HashMap generateID() {

        HashMap hash = null, result = null;
        try {
            String mapName = "getCurrentID";
            HashMap where = new HashMap();
            where.put("ID_KEY", "PSCID"); //Here u have to pass BORROW_ID or something else
            List list = null;

            //sqlMap.startTransaction();
            sqlMap.executeUpdate("updateIDGenerated", where);  // This update statement just updates curr_value=curr_value+1
            list = (List) sqlMap.executeQueryForList(mapName, where);  // This will get u the updated curr_value, prefix and length
            //sqlMap.commitTransaction();

            if (list.size() > 0) {
                hash = (HashMap) list.get(0);
                String strPrefix = "", strLen = "";

                // Prefix for the ID.
                if (hash.containsKey("PREFIX")) {
                    strPrefix = (String) hash.get("PREFIX");
                    if (strPrefix == null || strPrefix.trim().length() == 0) {
                        strPrefix = "";
                    }
                }

                // Maximum Length for the ID
                int len = 10;
                if (hash.containsKey("ID_LENGTH")) {
                    strLen = String.valueOf(hash.get("ID_LENGTH"));
                    if (strLen == null || strLen.trim().length() == 0) {
                        len = 10;
                    } else {
                        len = Integer.parseInt(strLen.trim());
                    }
                }

                int numFrom = strPrefix.trim().length();

                String newID = String.valueOf(hash.get("CURR_VALUE"));

                // Number Part of the String and incrementing 1 (ex. only 00085 from OGGOT00085)
                result = new HashMap();
                String genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
                result.put(CommonConstants.DATA, genID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();

            objTO.setStatus(CommonConstants.STATUS_CREATED);
            objTO.setElsi_id(getIMBPID());
            sqlMap.executeUpdate("insertELSI_MASTER", objTO);
            List Selected = objTO.getSelectedList();
            System.out.println("selected list in DAO..." + Selected.size());
            for (int i = 0; i < Selected.size(); i++) {
                HashMap getMap = new HashMap();
                getMap = (HashMap) Selected.get(i);
                System.out.println("MMMAAAPPPPP" + getMap);
                ServiceWiseLoanIssueConfigurationTO personalTO = new ServiceWiseLoanIssueConfigurationTO();

                personalTO.setStatus(CommonConstants.STATUS_CREATED);
                //  personalTO.setProdType(objTO.getProdType());
                System.out.println("getMap.get().toString()" + getMap.get("PROD_ID").toString());
                personalTO.setProdID(getMap.get("PROD_ID").toString());
                System.out.println("hhhhh" + personalTO.getProdID());
                personalTO.setElsi_id(objTO.getElsi_id());
                sqlMap.executeUpdate("insertELSI_ID", personalTO);
                //  personalTO.setProdDesc(ProdDesc

            }

            //  logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            //logTO.setData(objTO.toString());
            //  logTO.setPrimaryKey(objTO.getKeyData());
            // logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("deleteELSIIssue", objTO);
            List Selected = objTO.getSelectedList();
            System.out.println("selected list in DAO..." + Selected.size());
            for (int i = 0; i < Selected.size(); i++) {
                HashMap getMap = new HashMap();
                getMap = (HashMap) Selected.get(i);
                System.out.println("MMMAAAPPPPP" + getMap);
                ServiceWiseLoanIssueConfigurationTO personalTO = new ServiceWiseLoanIssueConfigurationTO();

                //personalTO.setStatus(CommonConstants.STATUS_CREATED);
                //  personalTO.setProdType(objTO.getProdType());
                System.out.println("getMap.get().toString()" + getMap.get("PROD_ID").toString());
                personalTO.setProdID(getMap.get("PROD_ID").toString());
                System.out.println("hhhhh" + personalTO.getProdID());
                personalTO.setElsi_id(objTO.getElsi_id());
                sqlMap.executeUpdate("insertELSI_ID", personalTO);
                //  personalTO.setProdDesc(ProdDesc

            }
            //sqlMap.executeUpdate("insertELSI_ID",objTO);
            sqlMap.executeUpdate("updateELSIprod", objTO);
            //  logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_DELETED);
            //objTO.setStatusDt(
            //logTO.setData(objTO.toString());
            // logTO.setPrimaryKey(objTO.getKeyData());
            // logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("deleteGeneralBodyTO", objTO);
            //   logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            ServiceWiseLoanIssueConfigurationDAO dao = new ServiceWiseLoanIssueConfigurationDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        objTO = (ServiceWiseLoanIssueConfigurationTO) map.get("ServiceWiseLoanIssueConfigurationTO");
        final String command = objTO.getCommand();
        HashMap returnMap = null;
        ////  logDAO = new LogDAO();
        //  logTO = new LogTO();

        // logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        // logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        // logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        // logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        // logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData();
            returnMap = new HashMap();
//            returnMap.put("BORROWING_NO",objTO.getBorrowingNo());
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData();
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        } else {
            throw new NoCommandException();
        }

        destroyObjects();
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
    }
}
