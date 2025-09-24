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
package com.see.truetransact.serverside.termloan.personalSuretyConfiguration;

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

import com.see.truetransact.transferobject.termloan.personalSuretyConfiguration.PersonalSuretyConfigurationTO;
import java.util.HashMap;

/**
 * TokenConfig DAO.
 *
 */
public class PersonalSuretyConfigurationDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private PersonalSuretyConfigurationTO objTO;

    //  private LogDAO logDAO;
    // private LogTO logTO;
    /**
     * Creates a new instance of TokenConfigDAO
     */
    public PersonalSuretyConfigurationDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap where = (HashMap) map.get(CommonConstants.MAP_WHERE);
        if (where.containsKey("PSCID")) {
            List list = (List) sqlMap.executeQueryForList("getSelectPersonal", where);
            returnMap.put("PersonalSuretyConfigurationTO", list);

        }
        if (where.containsKey("IMBPID")) {
            List list = (List) sqlMap.executeQueryForList("getSelectIMBP", where);
            returnMap.put("PersonalSuretyConfigurationTO", list);
            List prodList = (List) sqlMap.executeQueryForList("getSelectIMBPProd", where);
            System.out.println("ProdList..." + prodList.size());
            List selectedList = new ArrayList();
            for (int k = 0; k < prodList.size(); k++) {
                HashMap newMap = new HashMap();
                HashMap prodMap = new HashMap();
                prodMap = (HashMap) prodList.get(k);
                newMap.put("PROD_ID", prodMap.get("PROD_ID"));
                newMap.put("PROD_DESC", prodMap.get("PROD_DESC"));
                System.out.println("prodMap.get()" + prodMap.get("PROD_ID"));
                selectedList.add(newMap);

            }
            System.out.println("selectctrehke" + selectedList.size());
            if (!selectedList.isEmpty()) {
                returnMap.put("selectedList", selectedList);
            }
        }
        return returnMap;
    }

    private String getIMBPID() throws Exception {
        //  final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "IMBP_ID");
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
            where.put("ID_KEY", "IMBP_ID"); //Here u have to pass BORROW_ID or something else
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
            if (objTO.getPan() == 1) {
                System.out.println("MYYYY IIIDDDD" + getGeneralBoadyID());
                objTO.setGbid(getGeneralBoadyID());
                objTO.setStatus(CommonConstants.STATUS_CREATED);
                //  logTO.setData(objTO.toString());
                //  logTO.setPrimaryKey(objTO.getKeyData());
                //   logTO.setStatus(objTO.getCommand());
                sqlMap.executeUpdate("insertPersonalTO", objTO);
            }
            if (objTO.getPan() == 2) {
                objTO.setStatus(CommonConstants.STATUS_CREATED);
                objTO.setGbid(getIMBPID());
                sqlMap.executeUpdate("insertIMBPMaster", objTO);
                List Selected = objTO.getSelectedList();
                System.out.println("selected list in DAO..." + Selected.size());
                for (int i = 0; i < Selected.size(); i++) {
                    HashMap getMap = new HashMap();
                    getMap = (HashMap) Selected.get(i);
                    System.out.println("MMMAAAPPPPP" + getMap);
                    PersonalSuretyConfigurationTO personalTO = new PersonalSuretyConfigurationTO();

                    personalTO.setStatus(CommonConstants.STATUS_CREATED);
                    //  personalTO.setProdType(objTO.getProdType());
                    System.out.println("getMap.get().toString()" + getMap.get("PROD_ID").toString());
                    personalTO.setProdID(getMap.get("PROD_ID").toString());
                    System.out.println("hhhhh" + personalTO.getProdID());
                    personalTO.setGbid(objTO.getGbid());
                    sqlMap.executeUpdate("insertIMBPProd", personalTO);
                    //  personalTO.setProdDesc(ProdDesc

                }
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
//            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            //logTO.setData(objTO.toString());
            //  logTO.setPrimaryKey(objTO.getKeyData());
            // logTO.setStatus(objTO.getCommand());
//          sqlMap.executeUpdate("updatePersonalTO", objTO);


            if (objTO.getPan() == 1) {
                System.out.println("MYYYY IIIDDDD" + getGeneralBoadyID());
                // objTO.setGbid(getGeneralBoadyID());
                objTO.setStatus(CommonConstants.STATUS_MODIFIED);
                //  logTO.setData(objTO.toString());
                //  logTO.setPrimaryKey(objTO.getKeyData());
                //   logTO.setStatus(objTO.getCommand());
                sqlMap.executeUpdate("updatePersonalTO", objTO);
            }
            if (objTO.getPan() == 2) {
                System.out.println("MYYYY IIIDDDD in Edit Save " + objTO.getGbid());
                objTO.setStatus(CommonConstants.STATUS_MODIFIED);
                // objTO.setGbid(getIMBPID());
                sqlMap.executeUpdate("updateIMBPMaster", objTO);
                sqlMap.executeUpdate("deleteIMBPSettingsProd", objTO);
                List Selected = objTO.getSelectedList();
                System.out.println("selected list in DAO..." + Selected.size());
                for (int i = 0; i < Selected.size(); i++) {
                    HashMap getMap = new HashMap();
                    getMap = (HashMap) Selected.get(i);
                    System.out.println("MMMAAAPPPPP" + getMap);
                    PersonalSuretyConfigurationTO personalTO = new PersonalSuretyConfigurationTO();

                    personalTO.setStatus(CommonConstants.STATUS_CREATED);
                    //  personalTO.setProdType(objTO.getProdType());
                    System.out.println("getMap.get().toString()" + getMap.get("PROD_ID").toString());
                    personalTO.setProdID(getMap.get("PROD_ID").toString());
                    System.out.println("hhhhh" + personalTO.getProdID());
                    personalTO.setGbid(objTO.getGbid());
                    sqlMap.executeUpdate("insertIMBPProd", personalTO);
                    //  personalTO.setProdDesc(ProdDesc

                }
            }



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
            PersonalSuretyConfigurationDAO dao = new PersonalSuretyConfigurationDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        objTO = (PersonalSuretyConfigurationTO) map.get("PersonalSuretyConfigurationTO");
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
