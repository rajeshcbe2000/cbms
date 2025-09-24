/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BorrwingDAO.java
 *
 * Created on Thu Jan 20 17:19:05 IST 2005
 */
package com.see.truetransact.serverside.transaction.cashierapproval;

import java.util.List;
import java.util.ArrayList;
//cheque details
import java.util.LinkedHashMap;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverside.transaction.cashierapproval.CashierApprovalDAO; //trans details
//end...

import com.see.truetransact.commonutil.TTException;
//end...
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
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO; //insert into disbursal
import java.util.HashMap;
import java.util.Date;

/**
 * TokenConfig DAO.
 *
 */
public class CashierApprovalDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LogDAO logDAO;
    private LogTO logTO;
    private Date currDt = null;//trans details
    HashMap returnMap;

    /**
     * Creates a new instance of TokenConfigDAO
     */
    public CashierApprovalDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();


        return returnMap;
    }

    private String getDenominationNo() throws Exception {
        //  final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "DENOMINATION_TRANS_ID");
        HashMap map = generateID();
        return (String) map.get(CommonConstants.DATA);
    }

    public HashMap generateID() {
        HashMap hash = null, result = null;
        try {
            String mapName = "getCurrentID";
            HashMap where = new HashMap();
            where.put("ID_KEY", "DENOMINATION_TRANS_ID"); //Here u have to pass BORROW_ID or something else
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

    /*
     * private void insertData(HashMap map) throws Exception { try {
     * System.out.println("MAP IN INSERT
     * CASHIER============================"+map); sqlMap.startTransaction();
     * //sqlMap.executeUpdate("insertBorrowingTO", objTO); String
     * deno_no=getDenominationNo() ; map.put(CommonConstants.TRANS_ID,deno_no );
     * ServerUtil.executeQuery("insertDenominationDetails", map); if(map!=null
     * && map.get("TRANS_SUB_ID")!=null) { String
     * transId=map.get("TRANS_SUB_ID").toString(); if(transId!=null &&
     * transId.contains(":")) { String[] parts = transId.split(":"); for(int
     * i=0;i<parts.length;i++) { HashMap singleAuthorizeMap11 = new HashMap();
     * singleAuthorizeMap11.put("TRANS_DT", currDt);
     * singleAuthorizeMap11.put("TRANS_ID", parts[i] );
     * singleAuthorizeMap11.put("DENO_TRANS_ID", deno_no);
     * ServerUtil.executeQuery("insertDenominationSubDetails", map); } } }
     * sqlMap.commitTransaction(); } catch (Exception e) {
     * sqlMap.rollbackTransaction(); e.printStackTrace(); throw new
     * TransRollbackException(e);
     *
     * }
     * }
     */
    public static void main(String str[]) {
        try {
            CashierApprovalDAO dao = new CashierApprovalDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);

        HashMap returnMap = null;

        if (map != null && map.get("MULTIPLE").equals("MULTIPLE")) {
            // insertData(map);
            returnMap = new HashMap();
            returnMap.put("DENO_TRANS_ID", getDenominationNo());

        } else {
            throw new NoCommandException();
        }
        destroyObjects();
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
//        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
    }
}
