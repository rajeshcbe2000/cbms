/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSLetterGenerationDAO.java
 *
 * Created on Mon Jun 18 12:45:16 IST 2012
 */
package com.see.truetransact.serverside.mdsapplication.mdslettergeneration;

import java.util.List;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.product.operativeacct.*;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.mdsapplication.mdslettergeneration.MDSLetterGenerationTO;
// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

/**
 * This is used for OperativeAcctProductDAO Data Access.
 *
 * @author Suresh
 *
 *
 */
public class MDSLetterGenerationDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private MDSLetterGenerationTO objMDSLettorGenerationTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private final static Logger log = Logger.getLogger(MDSLetterGenerationDAO.class);

    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public MDSLetterGenerationDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        List list = null;
        // if(CommonConstants.SAL_REC_MODULE=="y"||CommonConstants.SAL_REC_MODULE=="Y"){
        //  list = (List) sqlMap.executeQueryForList("getSelectMDSLettorGenerationTo", map);
        // }else{
        list = (List) sqlMap.executeQueryForList("getSelectMDSLettorGenerationTo", map);
        //   }
        returnMap.put("MDSLetterGenerationTO", list);
        return returnMap;
    }

    public HashMap execute(HashMap map) throws Exception {

        System.out.println("####### MDS Lettor Map in DAO: " + map);
        HashMap returnMap = new HashMap();
        logDAO = new LogDAO();
        logTO = new LogTO();
        if (map.containsKey("MDSLetterGeneration")) {
            objMDSLettorGenerationTO = (MDSLetterGenerationTO) map.get("MDSLetterGeneration");
        }
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

    private String getMDSLettorNoId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "MDS_LETTER_ID");
        String mdsLettorId = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return mdsLettorId;
    }

    private String getMDSLetterNoTkId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "MDS_LETTER_TK_ID");
        String mdsLettorId = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return mdsLettorId;
    }

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            HashMap slMap = new HashMap();
            String mdsLettorId = "";
            mdsLettorId = getMDSLettorNoId();
            objMDSLettorGenerationTO.setLettorNo(mdsLettorId);

            //String mdsLettorId2 = "";
            //  mdsLettorId2 = getMDSLetterNoTkId();
            // objMDSLettorGenerationTO.setLettorNoVal1(mdsLettorId2);

            // if(CommonConstants.SAL_REC_MODULE=="y"||CommonConstants.SAL_REC_MODULE=="Y"){
            //  sqlMap.executeUpdate("insertMDSLetterData",objMDSLettorGenerationTO); 
            sqlMap.executeUpdate("insertMDSLetterTempData", objMDSLettorGenerationTO);
            //  }else{
            sqlMap.executeUpdate("insertMDSLettorGenerationTo", objMDSLettorGenerationTO);
            // }
            logTO.setData(objMDSLettorGenerationTO.toString());
            logTO.setPrimaryKey(objMDSLettorGenerationTO.getKeyData());
            logTO.setStatus(objMDSLettorGenerationTO.getStatus());
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
            //  if(CommonConstants.SAL_REC_MODULE=="y"||CommonConstants.SAL_REC_MODULE=="Y"){
            //   sqlMap.executeUpdate("updateMDSLetterDataTo",objMDSLettorGenerationTO);
            sqlMap.executeUpdate("insertMDSLetterTempData", objMDSLettorGenerationTO);
            //  }else{
            sqlMap.executeUpdate("updateMDSLettorGenerationTo", objMDSLettorGenerationTO);
            //  }
            logTO.setData(objMDSLettorGenerationTO.toString());
            logTO.setPrimaryKey(objMDSLettorGenerationTO.getKeyData());
            logTO.setStatus(objMDSLettorGenerationTO.getStatus());
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
            //  if(CommonConstants.SAL_REC_MODULE=="y"||CommonConstants.SAL_REC_MODULE=="Y"){
            //  sqlMap.executeUpdate("deleteMDSLetterDataTo",objMDSLettorGenerationTO);   
            // }else{
            sqlMap.executeUpdate("deleteMDSLettorGenerationTo", objMDSLettorGenerationTO);
            // }
            logTO.setData(objMDSLettorGenerationTO.toString());
            logTO.setPrimaryKey(objMDSLettorGenerationTO.getKeyData());
            logTO.setStatus(objMDSLettorGenerationTO.getStatus());
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
        //        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        //        HashMap AuthMap= new HashMap();
        //        AuthMap= (HashMap) selectedList.get(0);
        //        System.out.println("@@@@@@@AuthMap"+AuthMap);
        //        try {
        //            sqlMap.startTransaction();
        //            logTO.setData(map.toString());
        //            sqlMap.executeUpdate("authorizePrizedMoneyDetails", AuthMap);
        //            sqlMap.commitTransaction();
        //        } catch (Exception e) {
        //            sqlMap.rollbackTransaction();
        //            e.printStackTrace();
        //            throw new TransRollbackException(e);
        //        }
    }

    public static void main(String str[]) {
        try {
            MDSLetterGenerationDAO dao = new MDSLetterGenerationDAO();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void destroyObjects() {
        objMDSLettorGenerationTO = null;
    }
}
