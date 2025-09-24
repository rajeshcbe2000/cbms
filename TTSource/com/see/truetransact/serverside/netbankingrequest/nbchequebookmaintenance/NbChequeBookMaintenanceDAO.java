/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * NbChequeBookMaintenanceDAO.java
 */

package com.see.truetransact.serverside.netbankingrequest.nbchequebookmaintenance;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.see.truetransact.transferobject.netbankingrequest.nbchequebookmaintenance.NbChequeBookMaintenanceTO;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
/**
 *
 * @author Abhishek
 */
public class NbChequeBookMaintenanceDAO extends TTDAO {
    
    private Date currDt;
    private static SqlMap sqlMap = null;
    private LinkedHashMap netbankingDetails = null; 
    private LinkedHashMap netbankingTableDetails = null; 
    private NbChequeBookMaintenanceTO objNbChequeBookMaintenanceTO;
    private List selOutputList;
    private List deselOutputList;
    
    public NbChequeBookMaintenanceDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    
    public HashMap generateID() {

        HashMap hash = null, result = null;
        try {
            String mapName = "getCurrentID";
            HashMap where = new HashMap();
            where.put("ID_KEY", "NB_TRANS_ID"); //Here u have to pass BORROW_ID or something else
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
    
    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        System.out.println("executeQuery obj : "+obj);
        HashMap returnMap = new HashMap(); 
        HashMap transMap = new HashMap();
        NbChequeBookMaintenanceTO objNbChequeBookMaintenanceTO;
        List transList = (List) sqlMap.executeQueryForList("getDataToUpdateChequeBook", obj);
        if (transList != null && transList.size() > 0) {
            for (int i = 0; i < transList.size(); i++) {
                objNbChequeBookMaintenanceTO = new NbChequeBookMaintenanceTO();
                objNbChequeBookMaintenanceTO = (NbChequeBookMaintenanceTO) transList.get(i);
                transMap.put(i, objNbChequeBookMaintenanceTO);
            }
            returnMap.put("chequeBookMaintenanceList", transMap);
        }
        transList = null;
        transMap = null;
        objNbChequeBookMaintenanceTO = null;
        return returnMap;                
    }
     
    
    public HashMap execute(HashMap map) throws Exception {
        System.out.println("INSIDE EXECEUTE :" + map);
        HashMap returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        if (map.containsKey("SelChequeBookDetails")) {
             selOutputList = (ArrayList) map.get("SelChequeBookDetails");
        }
        if (map.containsKey("DeselChequeBookDetails")) {
             deselOutputList = (ArrayList) map.get("DeselChequeBookDetails");
        }
        
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
        }else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map);
        }else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData(map);
        }
        if(map.containsKey(CommonConstants.AUTHORIZEMAP)){
            if (map.containsKey("SelAuthChequeBookDetails")) {
                selOutputList = (ArrayList) map.get("SelAuthChequeBookDetails");
            }
            if (map.containsKey("DeselAuthChequeBookDetails")) {
                deselOutputList = (ArrayList) map.get("DeselAuthChequeBookDetails");
            }
         
                authorize(map);
        }
        
        map = null;
        destroyObjects();
        return returnMap;
    }
    
    private void insertData(HashMap map) throws Exception {
        HashMap nbTransId = generateID();
            
        try {
            for (int i = 0; i < selOutputList.size(); i++) {
                Map resultMap = (HashMap) selOutputList.get(i);
                NbChequeBookMaintenanceTO objNbChequeBookMaintenanceTO = new NbChequeBookMaintenanceTO();
                objNbChequeBookMaintenanceTO.setCustId(CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
                objNbChequeBookMaintenanceTO.setActNum(CommonUtil.convertObjToStr(resultMap.get("ACCOUNT_NUM")));
                objNbChequeBookMaintenanceTO.setStatus(CommonConstants.STATUS_CREATED);
                objNbChequeBookMaintenanceTO.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                objNbChequeBookMaintenanceTO.setNbTransId(CommonUtil.convertObjToStr(nbTransId.get("DATA")));
                sqlMap.executeUpdate("insertChequeBookDetailsTO", objNbChequeBookMaintenanceTO);
                objNbChequeBookMaintenanceTO = null;
            }
                 
        } catch (Exception e) {
            sqlMap.rollbackTransaction();       
            throw new TransRollbackException(e);
        }
    }
    
    private void updateData(HashMap map) throws Exception {
            
        try {
            for (int i = 0; i < selOutputList.size(); i++) {
                Map resultMap = (HashMap) selOutputList.get(i);
                NbChequeBookMaintenanceTO objNbChequeBookMaintenanceTO = new NbChequeBookMaintenanceTO();
                objNbChequeBookMaintenanceTO.setCustId(CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
                objNbChequeBookMaintenanceTO.setActNum(CommonUtil.convertObjToStr(resultMap.get("ACCOUNT_NUM")));
                objNbChequeBookMaintenanceTO.setStatus(CommonConstants.STATUS_MODIFIED);
                objNbChequeBookMaintenanceTO.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                System.out.println("NbChequeBookMaintenanceTO update  "+objNbChequeBookMaintenanceTO);
                sqlMap.executeUpdate("selUpdateChequeBookDetailsTO", objNbChequeBookMaintenanceTO);
                objNbChequeBookMaintenanceTO = null;
            }
            
            for (int i = 0; i < deselOutputList.size(); i++) {
                Map resultMap = (HashMap) deselOutputList.get(i);
                NbChequeBookMaintenanceTO objNbChequeBookMaintenanceTO = new NbChequeBookMaintenanceTO();
                objNbChequeBookMaintenanceTO.setCustId(CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
                objNbChequeBookMaintenanceTO.setActNum(CommonUtil.convertObjToStr(resultMap.get("ACCOUNT_NUM")));
                //objNbChequeBookMaintenanceTO.setStatus(CommonConstants.STATUS_MODIFIED);
                //objNbChequeBookMaintenanceTO.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                sqlMap.executeUpdate("deselUpdateChequeBookDetailsTO", objNbChequeBookMaintenanceTO);
                objNbChequeBookMaintenanceTO = null;
            }
                 
        } catch (Exception e) {
            sqlMap.rollbackTransaction();       
            throw new TransRollbackException(e);
        }
    }
    
    private void deleteData(HashMap map) throws Exception {
            
        try {
            for (int i = 0; i < selOutputList.size(); i++) {
                Map resultMap = (HashMap) selOutputList.get(i);
                NbChequeBookMaintenanceTO objNbChequeBookMaintenanceTO = new NbChequeBookMaintenanceTO();
                objNbChequeBookMaintenanceTO.setCustId(CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
                objNbChequeBookMaintenanceTO.setActNum(CommonUtil.convertObjToStr(resultMap.get("ACCOUNT_NUM")));
                objNbChequeBookMaintenanceTO.setStatus(CommonConstants.STATUS_DELETED);
                objNbChequeBookMaintenanceTO.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                //System.out.println("NbChequeBookMaintenanceTO update  "+objNbChequeBookMaintenanceTO);
                sqlMap.executeUpdate("DeleteChequeBookDetailsTO", objNbChequeBookMaintenanceTO);
                objNbChequeBookMaintenanceTO = null;
            }
          } catch (Exception e) {
            sqlMap.rollbackTransaction();       
            throw new TransRollbackException(e);
        }
    }
    
    private void authorize(HashMap map) throws Exception {
            
        try {
//            HashMap authMap = (HashMap)map.get(CommonConstants.AUTHORIZEMAP);
            System.out.println("Mapppppppppp  "+map);
            for (int i = 0; i < selOutputList.size(); i++) {
                Map resultMap = (HashMap) selOutputList.get(i);
                NbChequeBookMaintenanceTO objNbChequeBookMaintenanceTO = new NbChequeBookMaintenanceTO();
                objNbChequeBookMaintenanceTO.setCustId(CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
                objNbChequeBookMaintenanceTO.setActNum(CommonUtil.convertObjToStr(resultMap.get("ACCOUNT_NUM")));
                objNbChequeBookMaintenanceTO.setStatus(CommonUtil.convertObjToStr(map.get("COMMAND")));
                objNbChequeBookMaintenanceTO.setAuthBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                //System.out.println("NbChequeBookMaintenanceTO update  "+objNbChequeBookMaintenanceTO);
                sqlMap.executeUpdate("AuthorizeChequeBookDetailsTO", objNbChequeBookMaintenanceTO);
                objNbChequeBookMaintenanceTO = null;
            }
          } catch (Exception e) {
            sqlMap.rollbackTransaction();       
            throw new TransRollbackException(e);
        }
    }
    
    private void destroyObjects() {
        objNbChequeBookMaintenanceTO = null;
    }
}
