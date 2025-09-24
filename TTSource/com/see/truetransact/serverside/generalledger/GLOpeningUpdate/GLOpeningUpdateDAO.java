/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * GLOpeningUpdateDAO.java
 */

package com.see.truetransact.serverside.generalledger.GLOpeningUpdate;
import com.see.truetransact.serverside.supporting.balanceupdate.*;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.supporting.balanceupdate.BalanceUpdateTO;
import com.see.truetransact.transferobject.generalledger.GLOpeningUpdateTO;
import java.util.Date;

public class GLOpeningUpdateDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LogTO logTO;
    private LogDAO logDAO;
    private BalanceUpdate objBalanceUpdateTO;
    private String userID = "";
    private String branchCode = "";
    private ArrayList selectedArrayList;
    private ArrayList deletedArrayList;
    private int totalCount = 0;
    private BalanceUpdateTO objTO;
    BalanceUpdateTO objTO1 = new BalanceUpdateTO();
    Date currDt = null;
    int key = 1; 
    
    public GLOpeningUpdateDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    private String getBalSheetId() throws Exception {
        //  final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "BAL_SHEET_ID");
        HashMap map = generateID();
        return (String) map.get(CommonConstants.DATA);
    }

    public HashMap generateID() {
        HashMap hash = null, result = null;
        try {
            String mapName = "getCurrentID";
            HashMap where = new HashMap();
            where.put("ID_KEY", "BAL_SHEET_ID"); //Here u have to pass BORROW_ID or something else
            List list = null;
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

    public Date getProperDateFormat(Object obj) {
        Date currDate = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDate = (Date) currDt.clone();
            currDate.setDate(tempDt.getDate());
            currDate.setMonth(tempDt.getMonth());
            currDate.setYear(tempDt.getYear());
        }
        return currDate;
    }

    private void insertData(HashMap map) throws Exception {
         try {
            HashMap updateMap = new HashMap();
            updateMap.put("BALANCE", CommonUtil.convertObjToDouble(map.get("BALANCE")));
            updateMap.put("AC_HD", CommonUtil.convertObjToStr(map.get("AC_HD")));
            updateMap.put("BRANCH_CODE",CommonUtil.convertObjToStr(map.get("BRANCH_ID")));
            updateMap.put("OPEN_DT",map.get("OPEN_DT"));
            updateMap.put("NEW_CLOS_BAL",map.get("NEW_CLOS_BAL"));
            updateMap.put("BALANCE_TYPE",map.get("BALANCE_TYPE"));
            System.out.println("updateMap####"+updateMap);
            sqlMap.executeUpdate("updateGLCLosingBalanceOnly", updateMap);
            key = sqlMap.executeUpdate("updateGLCLosingBalance", updateMap);
            insertUpdatedAccountHeads(map);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

     private void insertUpdatedAccountHeads(HashMap map) throws Exception {
        try {
            HashMap insertAcHeadMap = setUpdateAccountHeads(map);
            GLOpeningUpdateTO glUpdateTO = new GLOpeningUpdateTO();
            if (insertAcHeadMap.containsKey("GL_CLOSE_BAL")) {
            	glUpdateTO = (GLOpeningUpdateTO) insertAcHeadMap.get("GL_CLOSE_BAL");
            }
            sqlMap.executeUpdate("insertUpdatedAccountHeads", glUpdateTO);
        }catch (Exception e) {
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
     
    private HashMap setUpdateAccountHeads(HashMap map) throws Exception {
        HashMap dataMap = new HashMap();
        GLOpeningUpdateTO glUpdateTO = new GLOpeningUpdateTO();
        glUpdateTO.setAcHd(CommonUtil.convertObjToStr(map.get("AC_HD")));
        glUpdateTO.setBranch(CommonUtil.convertObjToStr(map.get("BRANCH_ID")));
        glUpdateTO.setClosBal(CommonUtil.convertObjToStr(map.get("OLD_CLOS_BAL")));
        glUpdateTO.setUserId(CommonUtil.convertObjToStr(map.get("USER_ID")));
        glUpdateTO.setFrmDt(getProperDateFormat(map.get("OPEN_DT")));
        glUpdateTO.setNewBal(CommonUtil.convertObjToStr(map.get("NEW_CLOS_BAL")));
        glUpdateTO.setOpenBal(CommonUtil.convertObjToStr(map.get("OPEN_BAL")));
        glUpdateTO.setStatus(CommonConstants.STATUS_CREATED);
        dataMap.put("GL_CLOSE_BAL", glUpdateTO);
        System.out.println("glUpdateTO####"+glUpdateTO);
        return dataMap;
        
    }
     
    public static void main(String str[]) {
        try {
            GLOpeningUpdateDAO dao = new GLOpeningUpdateDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        totalCount = 0;
        
        HashMap returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        System.out.println("BalanceUpdateDAO Execute Method : " + map);
        System.out.println("BalanceUpdateDAO branch: "+CommonUtil.convertObjToStr(map.get("BRANCH_ID")));
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        System.out.println("command -- " + command);
        System.out.println("insert : " + CommonConstants.TOSTATUS_INSERT);
        System.out.println("delete : " + CommonConstants.TOSTATUS_DELETE);
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
           insertData(map);
        }else if (command.equals(CommonConstants.TOSTATUS_DELETE)) { 
            System.out.println("ENTRY TO DELETE");
            deleteData(map);
        }
        returnMap.put("COUNT", key);
        System.out.println("returnMap : " + returnMap);
        //destroyObjects();
        return returnMap;
    }

    private void destroyObjects() {
        objBalanceUpdateTO = null;
    }

    @Override
    public HashMap executeQuery(HashMap obj) throws Exception {
        return getData(obj);
    }

    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();
            if (deletedArrayList != null && deletedArrayList.size() != 0) {
                for (int i = 0; i < deletedArrayList.size(); i++) {
                    BalanceUpdateTO objBalanceUpdateTo = (BalanceUpdateTO) deletedArrayList.get(i);
                    System.out.println("reached inside : " + objBalanceUpdateTo);
                    sqlMap.executeUpdate("updateShareAccountTO", objBalanceUpdateTo);
                    totalCount = i + 1;
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
    
    private void deleteData(HashMap deleteMap) throws Exception {
        try {
            sqlMap.startTransaction();
            System.out.println("deletedData reached!!!!"+deleteMap);
            sqlMap.executeUpdate("updateGLCLosingBalanceAsZero", deleteMap);
            key = sqlMap.executeUpdate("updateGLCLosingBalance", deleteMap);
            sqlMap.executeUpdate("deleteGLOpeningUpdate", deleteMap);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    
    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectBalanceSheetTO", where);
        returnMap.put("BalanceSheetTO", list);
        return returnMap;
    }
}
