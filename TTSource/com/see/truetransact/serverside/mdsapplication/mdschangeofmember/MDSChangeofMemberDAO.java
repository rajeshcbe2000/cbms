/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OperativeAcctProductDAO.java
 *
 * Created on June 18, 2003, 4:14 PM
 */
package com.see.truetransact.serverside.mdsapplication.mdschangeofmember;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import java.util.LinkedHashMap;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
//import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.transferobject.product.operativeacct.*;
import com.see.truetransact.transferobject.mdsapplication.mdschangeofmember.MDSChangeofMemberTO;
import com.see.truetransact.serverside.TTDAO;
//import com.see.truetransact.serverutil.ServerConstants;
//import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.commonutil.CommonUtil;
//import com.see.truetransact.commonutil.TTException;
// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

/**
 * This is used for OperativeAcctProductDAO Data Access.
 *
 * @author Balachandar
 *
 * @modified Pinky @modified Rahul
 */
public class MDSChangeofMemberDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private MDSChangeofMemberTO changeOfMemberTo;
    private LogDAO logDAO;
    private LogTO logTO;
    private final static Logger log = Logger.getLogger(MDSChangeofMemberDAO.class);

    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public MDSChangeofMemberDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSelectChangeOfMemberTO", map);
        returnMap.put("ChangeOfMemberTO", list);
        return returnMap;
    }

    public HashMap execute(HashMap map) throws Exception {

        System.out.println("Map in DAO: " + map);
        HashMap returnMap = new HashMap();
//        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        logDAO = new LogDAO();
        logTO = new LogTO();
        changeOfMemberTo = (MDSChangeofMemberTO) map.get("ChangeOfMember");
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData(map);
        } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap);
            }
        }
        map = null;
        destroyObjects();
        return returnMap;
    }

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("insertChangeOfMemberTO", changeOfMemberTo);
            logTO.setData(changeOfMemberTo.toString());
            logTO.setPrimaryKey(changeOfMemberTo.getKeyData());
            logTO.setStatus(changeOfMemberTo.getStatus());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("updateMDSChangeofMemberTo", changeOfMemberTo);
            logTO.setData(changeOfMemberTo.toString());
            logTO.setPrimaryKey(changeOfMemberTo.getKeyData());
            logTO.setStatus(changeOfMemberTo.getStatus());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void deleteData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("deleteMDSChangeofMemberTo", changeOfMemberTo);
            logTO.setData(changeOfMemberTo.toString());
            logTO.setPrimaryKey(changeOfMemberTo.getKeyData());
            logTO.setStatus(changeOfMemberTo.getStatus());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void authorize(HashMap map) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        System.out.println("@@@@@@@AuthMap" + AuthMap);
        try {
            sqlMap.startTransaction();
            logTO.setData(map.toString());
            sqlMap.executeUpdate("authorizeChangeOfMember", AuthMap);
            String status = CommonUtil.convertObjToStr(AuthMap.get("STATUS"));
            updateMemberDetails(AuthMap);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateMemberDetails(HashMap map) throws Exception {
        System.out.println("@@@@@@@AuthMap" + map);
        logTO.setData(map.toString());
        HashMap whereMap = new HashMap();
        LinkedHashMap custMap = new LinkedHashMap();
        whereMap.put("CHITTAL_NO", CommonUtil.convertObjToStr(map.get("CHITTAL_NO")));
//        whereMap.put("SUB_NO",map.get("SUB_NO")); //Changed by Rajesh
        whereMap.put("SUB_NO", CommonUtil.convertObjToInt(map.get("SUB_NO")));  //AJITH
        
        // Added by nithya on 23 Jun 2025 for KD-4275
        if(map.containsKey("MUNNAL_CHITTAL") && map.get("MUNNAL_CHITTAL") != null && CommonUtil.convertObjToStr(map.get("MUNNAL_CHITTAL")).equals("Y")){
          sqlMap.executeUpdate("updateMunnalChittalOnMemberChange", whereMap);                    
        }
        
        List list = ServerUtil.executeQuery("getMDSChangeOfMemDetails", whereMap);
        if (list != null && list.size() > 0) {
            whereMap = (HashMap) list.get(0);
            String memNo = CommonUtil.convertObjToStr(whereMap.get("NEW_MEMBER_NO"));
            custMap.put("MEMBERSHIP_NO", memNo);
            List custList = ServerUtil.executeQuery("getMemeberShipDetails", custMap);
            if (custList != null && custList.size() > 0) {
                custMap = (LinkedHashMap) custList.get(0);
                custMap.put("SCHEME_NAME", map.get("SCHEME_NAME"));
                custMap.put("CHITTAL_NO", map.get("CHITTAL_NO"));
                custMap.put("SUB_NO", CommonUtil.convertObjToInt(map.get("SUB_NO")));   //AJITH
                sqlMap.executeUpdate("updateMemberDetailsInMDSApplication", custMap);       // UPDATE MEMBER DETAILS IN MDS APPLICATION
                whereMap.put("SCHEME_NAME", map.get("SCHEME_NAME"));
                whereMap.put("CHITTAL_NO", map.get("CHITTAL_NO"));
                whereMap.put("SUB_NO", CommonUtil.convertObjToInt(map.get("SUB_NO")));  //AJITH
                System.out.println("$#@$#@$#@$%@whereMap :" + whereMap);
                sqlMap.executeUpdate("updateMemberDetailsInReceiptEntry", whereMap);
                HashMap custAddressMap = new HashMap();
                custAddressMap.put("CUST_ID", custMap.get("CUST_ID"));
                List addressLst = ServerUtil.executeQuery("getCustomerAddressDetails", custAddressMap);
                System.out.println("$#@$#@$#@$%@addressLst :" + addressLst);
                if (addressLst != null && addressLst.size() > 0) {
                    custAddressMap = (HashMap) addressLst.get(0);
                    custAddressMap.put("CHITTAL_NO", map.get("CHITTAL_NO"));
                    custAddressMap.put("SUB_NO", CommonUtil.convertObjToInt(map.get("SUB_NO")));    //AJITH
                    custAddressMap.put("SCHEME_NAME", map.get("SCHEME_NAME"));
                    System.out.println("$#@$#@$#@$%@custAddressMap :" + custAddressMap);
                    sqlMap.executeUpdate("updateMemAddressDetailsMDSApplication", custAddressMap);   // UPDATE MEMBER ADDRESS DETAILS IN MDS APPLICATION
                }
                List MasterList = ServerUtil.executeQuery("getSelectBondDetails", map);
                if (MasterList != null && MasterList.size() > 0) {
                    sqlMap.executeUpdate("updateMemberDetailsMasterMaintanance", custMap);       // UPDATE MEMBER DETAILS IN MDS MASTER MAINTANANCE
                }
                whereMap.put("CHITTAL_NO", CommonUtil.convertObjToStr(map.get("CHITTAL_NO")));
                whereMap.put("SUB_NO", CommonUtil.convertObjToInt(map.get("SUB_NO")));  //AJITH
                List totalBonusList = ServerUtil.executeQuery("getTotalInstAmount", whereMap);
                if (totalBonusList != null && totalBonusList.size() > 0) {
                    whereMap = (HashMap) totalBonusList.get(0);
                    whereMap.put("CHITTAL_NO", CommonUtil.convertObjToStr(map.get("CHITTAL_NO")));
                    whereMap.put("SUB_NO", CommonUtil.convertObjToInt(map.get("SUB_NO")));  //AJITH
                    sqlMap.executeUpdate("updateChangedMemberBonusEarned", whereMap);
                }

            }

        }
    }

    public static void main(String str[]) {
        try {
            MDSChangeofMemberDAO dao = new MDSChangeofMemberDAO();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void destroyObjects() {
        changeOfMemberTo = null;
    }
}
