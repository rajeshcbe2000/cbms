/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RTGSOtherBankBranch.java
 *
 * Created on June 18, 2003, 4:14 PM
 */

package com.see.truetransact.serverside.product.remittance;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.transferobject.product.operativeacct.*;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.product.mds.MDSProductSchemeTO;
import com.see.truetransact.transferobject.product.mds.MDSProductAcctHeadTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;

import com.see.truetransact.transferobject.product.remittance.RTGSOtherBankBranchTo;

/**
 * This is used for RTGSOtherBankBranchDAO Data Access.
 *
 * @author  Suresh R
 *
 */

public class RTGSOtherBankBranchDAO extends TTDAO {
    private final static Logger log = Logger.getLogger(RTGSOtherBankBranchDAO.class);
    private RTGSOtherBankBranchTo objRTGSOtherBankBranchTo;
    private LinkedHashMap deletedTableValues = null;
    private LinkedHashMap tableDetails = null;
    private MDSProductAcctHeadTO acctHeadTo;
    private MDSProductSchemeTO schemeTo;
    private static SqlMap sqlMap = null;
    private Date CurrDt = null;
    private LogDAO logDAO;
    private LogTO logTO;
    
    /** Creates a new instance of OperativeAcctProductDAO */
    public RTGSOtherBankBranchDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    
    private HashMap getData(HashMap map) throws Exception {
        System.out.println("##### RTGS Bank Branch Map in DAO getData() : "+map);
        HashMap returnMap = new HashMap();
        //Based on mode Query changes done by Kannan AR Ref. Suresh On 02-Feb-2017
        String mapName ="";
        if(map.containsKey("AUTH_MODE")){
            mapName = "getSelectRTGSBankBranchAuthTO";
        }else{
            mapName = "getSelectRTGSBankBranchTO";
        }                        
        List rtgsList = (List) sqlMap.executeQueryForList(mapName, map);
        if(rtgsList!=null && rtgsList.size() > 0){
            LinkedHashMap ParMap = new LinkedHashMap();
            for(int i = rtgsList.size(), j = 0; i > 0; i--,j++){
                ParMap.put(CommonUtil.convertObjToStr(((RTGSOtherBankBranchTo) rtgsList.get(j)).getTxtIFSCCode()), rtgsList.get(j));
            }
            System.out.println("@@@ParMap"+ParMap);
            returnMap.put("RTGSTO_DATA",ParMap);
        }
        return returnMap;
    }
    
    public static void main(String str[]) {
        try {
            RTGSOtherBankBranchDAO dao = new RTGSOtherBankBranchDAO();
            HashMap inputMap = new HashMap();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public HashMap execute(HashMap map)  throws Exception {
        
        System.out.println("##### RTGS Bank Branch Map in DAO : "+map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        CurrDt = ServerUtil.getCurrentDate(_branchCode);
        HashMap returnMap = new HashMap();
        logDAO = new LogDAO();
        logTO = new LogTO();
        if (map.containsKey("RTGSTableDetails")) {
            tableDetails = (LinkedHashMap) map.get("RTGSTableDetails");
        }
        if (map.containsKey("deletedRTGSTableDetails")) {
            deletedTableValues = (LinkedHashMap) map.get("deletedRTGSTableDetails");
        }
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (command.equals(CommonConstants.TOSTATUS_INSERT)){
            insertData(map);
        }else if (command.equals(CommonConstants.TOSTATUS_UPDATE)){
            updateData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)){
            deleteData(map);
        }
        else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if(authMap!=null)
                authorize(authMap);
        }
        map=null;
        destroyObjects();
        return returnMap;
    }
    
    private void insertData(HashMap map) throws Exception {
        try{
            sqlMap.startTransaction();
            if(tableDetails!=null){
                ArrayList addList =new ArrayList(tableDetails.keySet());
                for(int i=0;i<addList.size();i++){
                    RTGSOtherBankBranchTo objRTGSOtherBankBranchTo = (RTGSOtherBankBranchTo) tableDetails.get(addList.get(i));
                    sqlMap.executeUpdate("insertRTGSBankBranchDetailsTo", objRTGSOtherBankBranchTo);
                    objRTGSOtherBankBranchTo = null;
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map=null;
    }
    
    private void updateData(HashMap map) throws Exception {
        try{
            sqlMap.startTransaction();
            if (tableDetails != null && tableDetails.size() > 0) {
                ArrayList addList = new ArrayList(tableDetails.keySet());
                RTGSOtherBankBranchTo objRTGSOtherBankBranchTo = null;
                for (int i = 0; i < tableDetails.size(); i++) {
                    objRTGSOtherBankBranchTo = new RTGSOtherBankBranchTo();
                    objRTGSOtherBankBranchTo = (RTGSOtherBankBranchTo) tableDetails.get(addList.get(i));
                    objRTGSOtherBankBranchTo.setStatusDt(CurrDt);
                    System.out.println("####### objRTGSOtherBankBranchTo : "+objRTGSOtherBankBranchTo);
                    if (CommonUtil.convertObjToStr(objRTGSOtherBankBranchTo.getAuthorizedStatus()).length() <= 0) {
                        if (objRTGSOtherBankBranchTo.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                            sqlMap.executeUpdate("insertRTGSBankBranchDetailsTo", objRTGSOtherBankBranchTo);
                        } else {
                            sqlMap.executeUpdate("updateRTGSBankBranchDetailsTo", objRTGSOtherBankBranchTo);
                        }
                    }
                }
            }
            if(deletedTableValues!= null && deletedTableValues.size()>0){
                System.out.println("######## deletedTableValues :"+deletedTableValues);
                ArrayList addList =new ArrayList(deletedTableValues.keySet());
                RTGSOtherBankBranchTo objRTGSOtherBankBranchTo = null;
                for (int i=0; i<deletedTableValues.size(); i++) {
                    objRTGSOtherBankBranchTo = new RTGSOtherBankBranchTo();
                    objRTGSOtherBankBranchTo = (RTGSOtherBankBranchTo) deletedTableValues.get(addList.get(i));
                    sqlMap.executeUpdate("deleteRTGSBankBranchDetailsTo", objRTGSOtherBankBranchTo);
                }
            }

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map=null;
    }
    
   
    private void deleteData(HashMap map) throws Exception {
        try{
            sqlMap.startTransaction();
            map.put("STATUS",CommonConstants.STATUS_DELETED);
            map.put("STATUS_DT",CurrDt);
            sqlMap.executeUpdate("deleteRTGSOtherBankBranchTo",map);
            sqlMap.executeUpdate("deleteLookUpMasterOtherBankBranch",map);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map=null;
    }
    
    private void authorize(HashMap map) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap= new HashMap();
        AuthMap= (HashMap) selectedList.get(0);
        System.out.println("@@@@@@@AuthMap"+AuthMap);
        try {
            sqlMap.startTransaction();
            logTO.setData(map.toString());
            sqlMap.executeUpdate("authorizeRTGSBankBranchDetails", AuthMap);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
    
    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        CurrDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }
    
    private void destroyObjects() {
        objRTGSOtherBankBranchTo = null;
    }
}










