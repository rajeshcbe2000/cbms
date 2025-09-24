/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * InterestMaintenanceGroupDAO.java
 *
 * Created on Tue May 25 10:39:44 IST 2004
 */
package com.see.truetransact.serverside.termloan.kcctopacs;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.LinkedHashMap;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.commonutil.CommonUtil;

import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;

import com.see.truetransact.transferobject.termloan.kcctopacs.InterestSubsidyRateMaintenanceGroupTO;
import com.see.truetransact.transferobject.termloan.kcctopacs.InterestSubsidyRateMaintenanceProdTO;
import com.see.truetransact.transferobject.termloan.kcctopacs.InterestSubsidyRateMaintenanceTypeTO;
import com.see.truetransact.transferobject.termloan.kcctopacs.InterestSubsidyRateMaintenanceCategotyTO;

import com.see.truetransact.serverside.termloan.kcctopacs.InterestSubsidyRateMaintenanceDAO;

// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.deposit.interestmaintenance.InterestMaintenanceRateTO;
import org.apache.log4j.Logger;

/**
 * InterestMaintenanceGroup DAO.
 *
 */
public class InterestSubsidyRateMaintenanceDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private InterestSubsidyRateMaintenanceGroupTO objInterestSubsidyRateMaintenanceGroupTO;
    private InterestSubsidyRateMaintenanceCategotyTO objInterestSubsidyRateMaintenanceCategotyTO;
    private InterestSubsidyRateMaintenanceProdTO objInterestSubsidyRateMaintenanceProdTO;
    private InterestSubsidyRateMaintenanceTypeTO objInterestSubsidyRateMaintenanceTypeTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private LinkedHashMap subsidytableDetails = null;
    private LinkedHashMap categoryTablDetails = null;
    private LinkedHashMap prodIdTablDetails = null;
    //agents Commision Interest Eate Maintanance    
    private Date curr_dt = null;
    private final static Logger log = Logger.getLogger(InterestSubsidyRateMaintenanceDAO.class);

    /**
     * Creates a new instance of InterestMaintenanceGroupDAO
     */
    public InterestSubsidyRateMaintenanceDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSubsidyRoiDetailsTO", map);
        if (list != null && list.size() > 0) {
            returnMap.put("objSubsidyRoiDetailsTO", list);
        }
        List subsidyIntlist = (List) sqlMap.executeQueryForList("getSubsidyRoiTypeRateDetailsTO", map);
        if (subsidyIntlist != null && subsidyIntlist.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            System.out.println("@@@subsidyIntlist" + subsidyIntlist);
            for (int i = subsidyIntlist.size(), j = 0; i > 0; i--, j++) {
                String st = ((InterestSubsidyRateMaintenanceTypeTO) subsidyIntlist.get(j)).getRoiGroupId();
                ParMap.put(CommonUtil.convertObjToStr(((InterestSubsidyRateMaintenanceTypeTO) subsidyIntlist.get(j)).getInstituteName() + ((InterestSubsidyRateMaintenanceTypeTO) subsidyIntlist.get(j)).getRefno()), subsidyIntlist.get(j));
            }
            System.out.println("@@@ParMap" + ParMap);
            returnMap.put("SUBSIDY_INT_LIST", ParMap);
        }
        List categorylist = (List) sqlMap.executeQueryForList("getSubsidycategoryDetailsTO", map);
        if (categorylist != null && categorylist.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            System.out.println("@@@categorylist" + categorylist);
            for (int i = categorylist.size(), j = 0; i > 0; i--, j++) {
                String st = ((InterestSubsidyRateMaintenanceCategotyTO) categorylist.get(j)).getRoiGroupId();
                ParMap.put(CommonUtil.convertObjToStr(((InterestSubsidyRateMaintenanceCategotyTO) categorylist.get(j)).getCategoryId()), categorylist.get(j));
            }
            System.out.println("@@@ParMap" + ParMap);
            returnMap.put("CATEGORY_LIST", ParMap);
        }
        List productlist = (List) sqlMap.executeQueryForList("getSubsidyProductDetailsTO", map);
        if (productlist != null && productlist.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            System.out.println("@@@productlist" + productlist);
            for (int i = productlist.size(), j = 0; i > 0; i--, j++) {
                String st = ((InterestSubsidyRateMaintenanceProdTO) productlist.get(j)).getRoiGroupId();
                ParMap.put(CommonUtil.convertObjToStr(((InterestSubsidyRateMaintenanceProdTO) productlist.get(j)).getProdId()), productlist.get(j));
            }
            System.out.println("@@@ParMap" + ParMap);
            returnMap.put("PRODUCT_LIST", ParMap);
        }
        return returnMap;
    }

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            String roiId = "";
            roiId = getROIID();
            getDuplicate(roiId);
            objInterestSubsidyRateMaintenanceGroupTO.setRoiGroupId(roiId);
            sqlMap.executeUpdate("insertSubsidyGroupDetailsTO", objInterestSubsidyRateMaintenanceGroupTO);
            insertSubsidyinterestData(roiId);
            insertSusidyCategoryData(roiId);
            insertSusidyProductData(roiId);
            logTO.setData(objInterestSubsidyRateMaintenanceGroupTO.toString());
            logTO.setPrimaryKey(objInterestSubsidyRateMaintenanceGroupTO.getKeyData());
            logTO.setStatus(objInterestSubsidyRateMaintenanceGroupTO.getStatus());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            if (e instanceof TTException) {
                throw new TTException(e);
            } else {
                throw new TransRollbackException(e);
            }
        }
        map = null;
    }

    private void insertSubsidyinterestData(String roiId) throws Exception {
        try {
            if (subsidytableDetails != null) {
                ArrayList addList = new ArrayList(subsidytableDetails.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    InterestSubsidyRateMaintenanceTypeTO objInterestSubsidyRateMaintenanceTypeTO = (InterestSubsidyRateMaintenanceTypeTO) subsidytableDetails.get(addList.get(i));
                    objInterestSubsidyRateMaintenanceTypeTO.setRoiGroupId(roiId);
                    sqlMap.executeUpdate("insertSubsidyInterestDetailsTO", objInterestSubsidyRateMaintenanceTypeTO);
                    logTO.setData(objInterestSubsidyRateMaintenanceTypeTO.toString());
                    logTO.setPrimaryKey(objInterestSubsidyRateMaintenanceTypeTO.getKeyData());
                    logDAO.addToLog(logTO);
                    objInterestSubsidyRateMaintenanceTypeTO = null;
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        roiId = null;
    }

    private void insertSusidyCategoryData(String roiId) throws Exception {
        try {
            if (categoryTablDetails != null) {
                ArrayList addList = new ArrayList(categoryTablDetails.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    InterestSubsidyRateMaintenanceCategotyTO objInterestSubsidyRateMaintenanceCategotyTO = (InterestSubsidyRateMaintenanceCategotyTO) categoryTablDetails.get(addList.get(i));
                    objInterestSubsidyRateMaintenanceCategotyTO.setRoiGroupId(roiId);
                    sqlMap.executeUpdate("insertSubsidyCategoryDetailsTO", objInterestSubsidyRateMaintenanceCategotyTO);
                    logTO.setData(objInterestSubsidyRateMaintenanceCategotyTO.toString());
                    logTO.setPrimaryKey(objInterestSubsidyRateMaintenanceCategotyTO.getKeyData());
                    logDAO.addToLog(logTO);
                    objInterestSubsidyRateMaintenanceCategotyTO = null;
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        roiId = null;
    }

    private void insertSusidyProductData(String roiId) throws Exception {
        try {
            if (prodIdTablDetails != null) {
                ArrayList addList = new ArrayList(prodIdTablDetails.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    InterestSubsidyRateMaintenanceProdTO objInterestSubsidyRateMaintenanceProdTO = (InterestSubsidyRateMaintenanceProdTO) prodIdTablDetails.get(addList.get(i));
                    objInterestSubsidyRateMaintenanceProdTO.setRoiGroupId(roiId);
                    sqlMap.executeUpdate("insertSubsidyProductIdDetailsTO", objInterestSubsidyRateMaintenanceProdTO);
                    logTO.setData(objInterestSubsidyRateMaintenanceProdTO.toString());
                    logTO.setPrimaryKey(objInterestSubsidyRateMaintenanceProdTO.getKeyData());
                    logDAO.addToLog(logTO);
                    objInterestSubsidyRateMaintenanceProdTO = null;
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        roiId = null;
    }

    private String getROIID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", "SUBSIDY.PRODID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void getDuplicate(String groupId) throws Exception {
        try {
            ArrayList prodList = new ArrayList();
            ArrayList pIdList = new ArrayList(prodIdTablDetails.keySet());
            ArrayList cIdList = new ArrayList(categoryTablDetails.keySet());
            for (int i = 0; i < pIdList.size(); i++) {
                for (int j = 0; j < cIdList.size(); j++) {
                    objInterestSubsidyRateMaintenanceProdTO = (InterestSubsidyRateMaintenanceProdTO) prodIdTablDetails.get(pIdList.get(i));
                    String prod = objInterestSubsidyRateMaintenanceProdTO.getProdId();
                    objInterestSubsidyRateMaintenanceCategotyTO = (InterestSubsidyRateMaintenanceCategotyTO) categoryTablDetails.get(cIdList.get(j));
                    String cate = objInterestSubsidyRateMaintenanceCategotyTO.getCategoryId();
                    StringBuffer strg = new StringBuffer();
                    strg.append(CommonUtil.convertObjToStr(prod));
                    strg.append(CommonUtil.convertObjToStr(cate));
                    prodList.add(strg.toString());
                    System.out.println("@@@@@@prodList" + prodList);
                }
            }
            HashMap map = new HashMap();
            map.put("prodList", prodList);
            if (!groupId.equalsIgnoreCase("")) {
                map.put("ROIGROUPID", groupId);
            }
            map.put("PRODUCT_TYPE", objInterestSubsidyRateMaintenanceGroupTO.getProductType());
            List duplicate = sqlMap.executeQueryForList("getDuplicateGroupData", map);
            int size = duplicate.size();
            System.out.println("########duplicateData" + duplicate);
            if (size > 0) {
                HashMap exception = new HashMap();
                ArrayList list = new ArrayList();
                list.add("DUPLICATEGROUP"); // The Key Value for the ExceptionHashMap...
                System.out.println("insideif########list" + list);
                exception.put(CommonConstants.EXCEPTION_LIST, list);
                exception.put(CommonConstants.CONSTANT_CLASS,
                        "com.see.truetransact.clientutil.exceptionhashmap.deposit.DepositRuleHashMap");
                exception.put(CommonConstants.EXCEPTION_TYPE, CommonConstants.CONCAT_EXCEPTION);
                System.out.println("insideif########exception" + exception);
                StringBuffer str = new StringBuffer();

                HashMap duplicateMap = new HashMap();
                for (int i = 0; i < size; i++) {
                    HashMap interMap = (HashMap) duplicate.get(i);
                    final Object[] keyList = (interMap.keySet()).toArray();
                    final int keyListLength = keyList.length;
                    for (int j = 0; j < keyListLength; j++) {
                        duplicateMap.put(CommonUtil.convertObjToStr(keyList[j]), CommonUtil.convertObjToStr(interMap.get(keyList[j])));
                        System.out.println("insidefor########duplicateMap" + duplicateMap);
                    }

                    final Object[] duplicatekeyList = (duplicateMap.keySet()).toArray();
                    final int duplicateLength = duplicatekeyList.length;

                    for (int k = 0; k < duplicateLength; k++) {
                        str.append(CommonUtil.convertObjToStr(interMap.get(duplicatekeyList[k])) + "\n");
                        System.out.println("&&&&&&&&str" + str);
                    }
                    System.out.println("insideif########duplicateMap" + duplicateMap);
                }
                ArrayList duplicateList = new ArrayList();
                duplicateList.add(str.toString());
                exception.put(CommonConstants.SERVER_VALUE, duplicateList);
                System.out.println("insideif########duplicateList" + duplicateList);
                System.out.println("insideif########duplicateMap" + duplicateMap);

                list = null; // To Destroy the List obj...
                duplicateList = null; // To Destroy the List obj...
                throw new TTException("Duplicate Group");
            }
        } catch (Exception e) {
            if (e instanceof TTException) {
                System.out.println("INSIDEif" + e);
                throw new TTException(e);
            } else {
                throw new TransRollbackException(e);
            }
        }
    }

    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            final String roiId = CommonUtil.convertObjToStr(objInterestSubsidyRateMaintenanceGroupTO.getRoiGroupId());
            getDuplicate(roiId);
            sqlMap.executeUpdate("updateSubsidyGroupDetailsTO", objInterestSubsidyRateMaintenanceGroupTO);
            updateSubsidyinterestData(map);
            updateCategoryData(map);
            updateProductData(map);
            logTO.setData(objInterestSubsidyRateMaintenanceGroupTO.toString());
            logTO.setPrimaryKey(objInterestSubsidyRateMaintenanceGroupTO.getKeyData());
            logTO.setStatus(objInterestSubsidyRateMaintenanceGroupTO.getStatus());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            if (e instanceof TTException) {
                throw new TTException(e);
            } else {
                throw new TransRollbackException(e);
            }
        }
        map = null;
    }

    private void updateSubsidyinterestData(HashMap map) throws Exception {
        try {
            sqlMap.executeUpdate("deleteSubsidyIntTableData", objInterestSubsidyRateMaintenanceGroupTO);
            if (subsidytableDetails != null) {
                ArrayList addList = new ArrayList(subsidytableDetails.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    InterestSubsidyRateMaintenanceTypeTO objInterestSubsidyRateMaintenanceTypeTO = (InterestSubsidyRateMaintenanceTypeTO) subsidytableDetails.get(addList.get(i));
                    sqlMap.executeUpdate("insertSubsidyInterestDetailsTO", objInterestSubsidyRateMaintenanceTypeTO);
                    logTO.setData(objInterestSubsidyRateMaintenanceTypeTO.toString());
                    logTO.setPrimaryKey(objInterestSubsidyRateMaintenanceTypeTO.getKeyData());
                    logTO.setStatus(objInterestSubsidyRateMaintenanceGroupTO.getStatus());
                    logDAO.addToLog(logTO);
                    objInterestSubsidyRateMaintenanceTypeTO = null;
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void updateCategoryData(HashMap map) throws Exception {
        try {
            sqlMap.executeUpdate("deleteCatTableData", objInterestSubsidyRateMaintenanceGroupTO);
            if (categoryTablDetails != null) {
                ArrayList addList = new ArrayList(categoryTablDetails.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    InterestSubsidyRateMaintenanceCategotyTO objInterestSubsidyRateMaintenanceCategotyTO = (InterestSubsidyRateMaintenanceCategotyTO) categoryTablDetails.get(addList.get(i));
                    sqlMap.executeUpdate("insertSubsidyCategoryDetailsTO", objInterestSubsidyRateMaintenanceCategotyTO);
                    logTO.setData(objInterestSubsidyRateMaintenanceCategotyTO.toString());
                    logTO.setPrimaryKey(objInterestSubsidyRateMaintenanceCategotyTO.getKeyData());
                    logTO.setStatus(objInterestSubsidyRateMaintenanceCategotyTO.getStatus());
                    logDAO.addToLog(logTO);
                    objInterestSubsidyRateMaintenanceCategotyTO = null;
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void updateProductData(HashMap map) throws Exception {
        try {
            sqlMap.executeUpdate("deleteProductTableData", objInterestSubsidyRateMaintenanceGroupTO);
            if (prodIdTablDetails != null) {
                ArrayList addList = new ArrayList(prodIdTablDetails.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    InterestSubsidyRateMaintenanceProdTO objInterestSubsidyRateMaintenanceProdTO = (InterestSubsidyRateMaintenanceProdTO) prodIdTablDetails.get(addList.get(i));
                    sqlMap.executeUpdate("insertSubsidyProductIdDetailsTO", objInterestSubsidyRateMaintenanceProdTO);
                    logTO.setData(objInterestSubsidyRateMaintenanceProdTO.toString());
                    logTO.setPrimaryKey(objInterestSubsidyRateMaintenanceProdTO.getKeyData());
                    logTO.setStatus(objInterestSubsidyRateMaintenanceProdTO.getStatus());
                    logDAO.addToLog(logTO);
                    objInterestSubsidyRateMaintenanceProdTO = null;
                }
            }
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
            sqlMap.executeUpdate("deleteSubsidyGroupDetailsTO", objInterestSubsidyRateMaintenanceGroupTO);
            sqlMap.executeUpdate("deleteIntTableData", objInterestSubsidyRateMaintenanceGroupTO);
            sqlMap.executeUpdate("deleteCategoryTableData", objInterestSubsidyRateMaintenanceGroupTO);
            sqlMap.executeUpdate("deleteProdData", objInterestSubsidyRateMaintenanceGroupTO);
            logTO.setData(objInterestSubsidyRateMaintenanceGroupTO.toString());
            logTO.setPrimaryKey(objInterestSubsidyRateMaintenanceGroupTO.getKeyData());
            logTO.setStatus(objInterestSubsidyRateMaintenanceGroupTO.getStatus());
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
            sqlMap.executeUpdate("authorizeSubsidyIntTabelDetails", AuthMap);
            if (AuthMap.get("STATUS").equals("REJECTED")) {
                List lst = sqlMap.executeQueryForList("getAuthListForSubsidyRate", AuthMap);
                if (lst != null && lst.size() > 0) {
                    AuthMap.put("STATUS", "AUTHORIZED");
                }
            }
            sqlMap.executeUpdate("authorizeSubsidyGroupDetails", AuthMap);
            String status = CommonUtil.convertObjToStr(AuthMap.get("STATUS"));
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            InterestSubsidyRateMaintenanceDAO dao = new InterestSubsidyRateMaintenanceDAO();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*
     * final Object[] keyList = (interMap.keySet()).toArray(); final int
     * keyListLength = keyList.length; issueMap = (LinkedHashMap)
     * map.get("REMITISSUE"); objRemittanceIssueTO =
     * (RemittanceIssueTO)issueMap.get((String)(keyList[i]));
     */
    public HashMap execute(HashMap map) throws Exception {
        System.out.println("##################### InterestSubsidyRateMaintenanceGroup  in DAO: " + map);
        HashMap returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        curr_dt = ServerUtil.getCurrentDate(_branchCode);
        logDAO = new LogDAO();
        logTO = new LogTO();
        objInterestSubsidyRateMaintenanceGroupTO = (InterestSubsidyRateMaintenanceGroupTO) map.get("SubsidyGroupDetailsData");
        subsidytableDetails = (LinkedHashMap) map.get("SubsidyInterestDetails");
        categoryTablDetails = (LinkedHashMap) map.get("SubsidyCategoryDetails");
        prodIdTablDetails = (LinkedHashMap) map.get("SubsidyProductDetails");

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

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        curr_dt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objInterestSubsidyRateMaintenanceGroupTO = null;
        objInterestSubsidyRateMaintenanceCategotyTO = null;
        objInterestSubsidyRateMaintenanceProdTO = null;
        objInterestSubsidyRateMaintenanceTypeTO = null;
        subsidytableDetails = null;
        categoryTablDetails = null;
        prodIdTablDetails = null;
    }
}
