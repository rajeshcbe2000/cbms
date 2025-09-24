/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemittanceProductDAO.java
 *
 * Created on Wed Jan 07 19:24:17 IST 2004
 */
package com.see.truetransact.serverside.product.remittance;

import java.util.List;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.TOHeader;

import com.see.truetransact.transferobject.product.remittance.RemittanceProductBranchTO;
import com.see.truetransact.transferobject.product.remittance.RemittanceProductTO;
import com.see.truetransact.transferobject.product.remittance.RemittanceProductChargeTO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import java.util.Date;
/**
 * RemittanceProduct DAO.
 *
 */
public class RemittanceProductDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private RemittanceProductTO objRemittanceProductTO;
    private RemittanceProductBranchTO objRemittanceProductBranchTO;
    private RemittanceProductChargeTO objRemittanceProductChargeTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private List list;
    private ArrayList remittanceProductBranchTab;
    private ArrayList remittanceProductChargeTab;
    private LinkedHashMap remittanceProductChargeTO = null;
    private LinkedHashMap deletedRemittanceProductChargeTO = null;
    private String command = null;
    private String userID = "";
    private Date currDt = null;
    /**
     * Creates a new instance of RemittanceProductDAO
     */
    public RemittanceProductDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        HashMap descmap=new HashMap();
        ArrayList descList=new ArrayList();
         List lst=null;
        list = (List) sqlMap.executeQueryForList("getSelectRemittanceProductTO", where);
        returnMap.put("RemittanceProductTO", list);
        if (list != null && list.size() > 0) {
            HashMap hmap = new HashMap();
            RemittanceProductTO remittanceProductTO = (RemittanceProductTO) list.get(0);
            if (!remittanceProductTO.getIssueHd().equals("")) {
                hmap.put("IBR_AC_HD", remittanceProductTO.getIssueHd());
                lst = sqlMap.executeQueryForList("getAcHeadDesc", hmap);
                if (lst != null && lst.size() > 0) {
                    hmap = (HashMap) lst.get(0);
                    descmap = new HashMap();
                    descmap = new HashMap();
                    descmap.put(remittanceProductTO.getIssueHd(), hmap.get("AC_HD_DESC"));
                    descList.add(descmap);
                }
            }
            if (!remittanceProductTO.getExchangeHd().equals("")) {
                hmap.put("IBR_AC_HD", remittanceProductTO.getExchangeHd());
                lst = sqlMap.executeQueryForList("getAcHeadDesc", hmap);
                if (lst != null && lst.size() > 0) {
                    hmap = (HashMap) lst.get(0);
                    descmap = new HashMap();
                    descmap.put(remittanceProductTO.getExchangeHd(), hmap.get("AC_HD_DESC"));
                    descList.add(descmap);
                }
            }
            if (remittanceProductTO.getTelegramChrgHd()!=null && !remittanceProductTO.getTelegramChrgHd().equals("")) {
                hmap.put("IBR_AC_HD", remittanceProductTO.getTelegramChrgHd());
                lst = sqlMap.executeQueryForList("getAcHeadDesc", hmap);
                if (lst != null && lst.size() > 0) {
                    hmap = (HashMap) lst.get(0);
                    descmap = new HashMap();
                    descmap.put(remittanceProductTO.getTelegramChrgHd(), hmap.get("AC_HD_DESC"));
                    descList.add(descmap);
                }
            }
            if (remittanceProductTO.getRevalChrgHd()!=null && !remittanceProductTO.getRevalChrgHd().equals("")) {
                hmap.put("IBR_AC_HD", remittanceProductTO.getRevalChrgHd());
                lst = sqlMap.executeQueryForList("getAcHeadDesc", hmap);
                if (lst != null && lst.size() > 0) {
                    hmap = (HashMap) lst.get(0);
                    descmap = new HashMap();
                    descmap.put(remittanceProductTO.getRevalChrgHd(), hmap.get("AC_HD_DESC"));
                    descList.add(descmap);
                }
            }
            if (remittanceProductTO.getOtherChrgHd()!=null && !remittanceProductTO.getOtherChrgHd().equals("")) {
                hmap.put("IBR_AC_HD", remittanceProductTO.getOtherChrgHd());
                lst = sqlMap.executeQueryForList("getAcHeadDesc", hmap);
                if (lst != null && lst.size() > 0) {
                    hmap = (HashMap) lst.get(0);
                    descmap = new HashMap();
                    descmap.put(remittanceProductTO.getOtherChrgHd(), hmap.get("AC_HD_DESC"));
                    descList.add(descmap);
                }
            }
            if (remittanceProductTO.getPayHd()!=null && !remittanceProductTO.getPayHd().equals("")) {
                hmap.put("IBR_AC_HD", remittanceProductTO.getPayHd());
                lst = sqlMap.executeQueryForList("getAcHeadDesc", hmap);
                if (lst != null && lst.size() > 0) {
                    hmap = (HashMap) lst.get(0);
                    descmap = new HashMap();
                    descmap.put(remittanceProductTO.getPayHd(), hmap.get("AC_HD_DESC"));
                    descList.add(descmap);
                }
            }
            if (remittanceProductTO.getPostageHd()!=null && !remittanceProductTO.getPostageHd().equals("")) {
                hmap.put("IBR_AC_HD", remittanceProductTO.getPostageHd());
                lst = sqlMap.executeQueryForList("getAcHeadDesc", hmap);
                if (lst != null && lst.size() > 0) {
                    hmap = (HashMap) lst.get(0);
                    descmap = new HashMap();
                    descmap.put(remittanceProductTO.getPostageHd(), hmap.get("AC_HD_DESC"));
                    descList.add(descmap);
                }
            }
            if (remittanceProductTO.getDuplChrgHd()!=null && !remittanceProductTO.getDuplChrgHd().equals("")) {
                hmap.put("IBR_AC_HD", remittanceProductTO.getDuplChrgHd());
                lst = sqlMap.executeQueryForList("getAcHeadDesc", hmap);
                if (lst != null && lst.size() > 0) {
                    hmap = (HashMap) lst.get(0);
                    descmap = new HashMap();
                    descmap.put(remittanceProductTO.getDuplChrgHd(), hmap.get("AC_HD_DESC"));
                    descList.add(descmap);
                }
            }
            if (remittanceProductTO.getCancellChrgHd()!=null && !remittanceProductTO.getCancellChrgHd().equals("")) {
                hmap.put("IBR_AC_HD", remittanceProductTO.getCancellChrgHd());
                lst = sqlMap.executeQueryForList("getAcHeadDesc", hmap);
                if (lst != null && lst.size() > 0) {
                    hmap = (HashMap) lst.get(0);
                    descmap = new HashMap();
                    descmap.put(remittanceProductTO.getCancellChrgHd(), hmap.get("AC_HD_DESC"));
                    descList.add(descmap);
                }
            }
            
            //Added by kannan for new procedure achead
            if (remittanceProductTO.getNewProcIssueAcHd()!=null && !remittanceProductTO.getNewProcIssueAcHd().equals("")) {
                hmap.put("IBR_AC_HD", remittanceProductTO.getNewProcIssueAcHd());
                lst = sqlMap.executeQueryForList("getAcHeadDesc", hmap);
                if (lst != null && lst.size() > 0) {
                    hmap = (HashMap) lst.get(0);
                    descmap = new HashMap();
                    descmap.put(remittanceProductTO.getNewProcIssueAcHd(), hmap.get("AC_HD_DESC"));
                    descList.add(descmap);
                }
            }
        }
        //System.out.println("descList##"+descList);
        returnMap.put("RemittanceDesc", descList);
        list = (List) sqlMap.executeQueryForList("ViewRemittance_Product_Branch", where);
        returnMap.put("RemittanceProductBranchTO", list);
        list = (List) sqlMap.executeQueryForList("getSelectRemittanceProductChargeTO", where);
        returnMap.put("RemittanceProductChargeTO", list);
        map = null;
        where = null;
        list = null;
        return returnMap;
    }

    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();
            objRemittanceProductTO.setCreatedBy(userID);
            objRemittanceProductTO.setCreatedDt(currDt);

            objRemittanceProductTO.setStatus(CommonConstants.STATUS_CREATED);
            objRemittanceProductTO.setStatusBy(userID);
            objRemittanceProductTO.setStatusDt(currDt);

            logTO.setData(objRemittanceProductTO.toString());
            logTO.setPrimaryKey(objRemittanceProductTO.getKeyData());
            logTO.setStatus(objRemittanceProductTO.getCommand());
            sqlMap.executeUpdate("insertRemittanceProductTO", objRemittanceProductTO);
            logDAO.addToLog(logTO);
            executeRemittanceProductBranchTO();
            insertRemitProdChargeTO();
            //            executeRemittanceProductChargeTO();
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
            objRemittanceProductTO.setStatus(CommonConstants.STATUS_MODIFIED);
            objRemittanceProductTO.setStatusBy(userID);
            objRemittanceProductTO.setStatusDt(currDt);

            logTO.setData(objRemittanceProductTO.toString());
            logTO.setPrimaryKey(objRemittanceProductTO.getKeyData());
            logTO.setStatus(objRemittanceProductTO.getCommand());
            sqlMap.executeUpdate("updateRemittanceProductTO", objRemittanceProductTO);
            logDAO.addToLog(logTO);
            executeRemittanceProductBranchTO();
            updateRemitProdChargeTO();
            deletedRemitProdChargeTO();
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
            objRemittanceProductTO.setStatus(CommonConstants.STATUS_DELETED);
            objRemittanceProductTO.setStatusBy(userID);
            objRemittanceProductTO.setStatusDt(currDt);

            logTO.setData(objRemittanceProductTO.toString());
            logTO.setPrimaryKey(objRemittanceProductTO.getKeyData());
            logTO.setStatus(objRemittanceProductTO.getCommand());
            sqlMap.executeUpdate("deleteRemittanceProductTO", objRemittanceProductTO);
            logDAO.addToLog(logTO);
            executeRemittanceProductBranchTO();
            deleteRemitProdChargeTO();
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void executeRemittanceProductBranchTO() throws Exception {
        try {
            RemittanceProductBranchTO objRemittanceProductBranchTO;
            // To retrieve the RemittanceProductBranchTO from the remittanceProductBranchTab (ArrayList)
            if (remittanceProductBranchTab != null) {
                int remittanceProductBranchTabSize = remittanceProductBranchTab.size();
                for (int i = 0; i < remittanceProductBranchTabSize; i++) {
                    objRemittanceProductBranchTO = (RemittanceProductBranchTO) remittanceProductBranchTab.get(i);
                    if (objRemittanceProductBranchTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                        objRemittanceProductBranchTO.setStatus(CommonConstants.STATUS_CREATED);
                        logTO.setData(objRemittanceProductBranchTO.toString());
                        logTO.setPrimaryKey(objRemittanceProductBranchTO.getKeyData());
                        logTO.setStatus(objRemittanceProductBranchTO.getCommand());
                        remittanceProductBranchTOQuery("insertRemittanceProductBranchTO", objRemittanceProductBranchTO);
                        logDAO.addToLog(logTO);
                    } else if (objRemittanceProductBranchTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                        objRemittanceProductBranchTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        logTO.setData(objRemittanceProductBranchTO.toString());
                        logTO.setPrimaryKey(objRemittanceProductBranchTO.getKeyData());
                        logTO.setStatus(objRemittanceProductBranchTO.getCommand());
                        remittanceProductBranchTOQuery("updateRemittanceProductBranchTO", objRemittanceProductBranchTO);
                        logDAO.addToLog(logTO);
                    } else if (objRemittanceProductBranchTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                        objRemittanceProductBranchTO.setStatus(CommonConstants.STATUS_DELETED);
                        logTO.setData(objRemittanceProductBranchTO.toString());
                        logTO.setPrimaryKey(objRemittanceProductBranchTO.getKeyData());
                        logTO.setStatus(objRemittanceProductBranchTO.getCommand());
                        remittanceProductBranchTOQuery("deleteRemittanceProductBranchTO", objRemittanceProductBranchTO);
                        logDAO.addToLog(logTO);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * To Insert Remittance Product Charges TO
     */
    private void insertRemitProdChargeTO() throws Exception {
        if (remittanceProductChargeTO != null) {
            Object objArray[] = remittanceProductChargeTO.keySet().toArray();
            int size = remittanceProductChargeTO.keySet().size();
            for (int i = 0; i < size; i++) {
                LinkedHashMap remitProdChargeTOs = (LinkedHashMap) remittanceProductChargeTO.get(CommonUtil.convertObjToStr(objArray[i]));
                Object objArr[] = remitProdChargeTOs.keySet().toArray();
                for (int index = 0, j = remitProdChargeTOs.keySet().size(); index < j; index++) {
                    objRemittanceProductChargeTO = (RemittanceProductChargeTO) remitProdChargeTOs.get(CommonUtil.convertObjToStr(objArr[index]));

                    objRemittanceProductChargeTO.setStatus(CommonConstants.STATUS_CREATED);
                    logTO.setData(objRemittanceProductChargeTO.toString());
                    logTO.setPrimaryKey(objRemittanceProductChargeTO.getKeyData());
                    logTO.setStatus(command);
                    remittanceProductChargeTOQuery("insertRemittanceProductChargeTO", objRemittanceProductChargeTO);
                    logDAO.addToLog(logTO);
                    objRemittanceProductChargeTO = null;
                }
                remitProdChargeTOs = null;
                objArr = null;
            }
            remittanceProductChargeTO = null;
            objArray = null;
        }
        remittanceProductChargeTO = null;
    }

    /**
     * To Update Remittance Product Charges TO
     */
    private void updateRemitProdChargeTO() throws Exception {
        if (remittanceProductChargeTO != null) {
            Object objArray[] = remittanceProductChargeTO.keySet().toArray();
            int size = remittanceProductChargeTO.keySet().size();
            for (int i = 0; i < size; i++) {
                LinkedHashMap remitProdChargeTOs = (LinkedHashMap) remittanceProductChargeTO.get(CommonUtil.convertObjToStr(objArray[i]));
                Object objArr[] = remitProdChargeTOs.keySet().toArray();
                for (int index = 0, j = remitProdChargeTOs.keySet().size(); index < j; index++) {
                    objRemittanceProductChargeTO = (RemittanceProductChargeTO) remitProdChargeTOs.get(CommonUtil.convertObjToStr(objArr[index]));
                    if ((objRemittanceProductChargeTO.getStatus().length() > 0) && (objRemittanceProductChargeTO.getStatus() != null)) {
                        objRemittanceProductChargeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        logTO.setData(objRemittanceProductChargeTO.toString());
                        logTO.setPrimaryKey(objRemittanceProductChargeTO.getKeyData());
                        logTO.setStatus(command);
                        remittanceProductChargeTOQuery("updateRemittanceProductChargeTO", objRemittanceProductChargeTO);
                        logDAO.addToLog(logTO);
                        objRemittanceProductChargeTO = null;
                    } else {
                        objRemittanceProductChargeTO.setStatus(CommonConstants.STATUS_CREATED);
                        logTO.setData(objRemittanceProductChargeTO.toString());
                        logTO.setPrimaryKey(objRemittanceProductChargeTO.getKeyData());
                        logTO.setStatus(command);
                        remittanceProductChargeTOQuery("insertRemittanceProductChargeTO", objRemittanceProductChargeTO);
                        logDAO.addToLog(logTO);
                        objRemittanceProductChargeTO = null;
                    }

                }
                remitProdChargeTOs = null;
                objArr = null;
            }
            remittanceProductChargeTO = null;
            objArray = null;

        }
        remittanceProductChargeTO = null;
    }

    /**
     * To Change Remittance Product Charges TO as Deleted
     */
    private void deletedRemitProdChargeTO() throws Exception {
        if (deletedRemittanceProductChargeTO != null) {
            for (int i = 0, j = deletedRemittanceProductChargeTO.size(); i < j; i++) {
                objRemittanceProductChargeTO = (RemittanceProductChargeTO) deletedRemittanceProductChargeTO.get(String.valueOf(i + 1));

                objRemittanceProductChargeTO.setStatus(CommonConstants.STATUS_DELETED);
                logTO.setData(objRemittanceProductChargeTO.toString());
                logTO.setPrimaryKey(objRemittanceProductChargeTO.getKeyData());
                logTO.setStatus(command);
                remittanceProductChargeTOQuery("deletedRemittanceProductChargeTO", objRemittanceProductChargeTO);
                logDAO.addToLog(logTO);
                objRemittanceProductChargeTO = null;
            }
        }
        deletedRemittanceProductChargeTO = null;
    }

    /**
     * To Delete Remittance Product Charges TOs
     */
    private void deleteRemitProdChargeTO() throws Exception {
        if (remittanceProductChargeTO != null) {
            Object objArray[] = remittanceProductChargeTO.keySet().toArray();
            int size = remittanceProductChargeTO.keySet().size();
            for (int i = 0; i < size; i++) {
                LinkedHashMap remitProdChargeTOs = (LinkedHashMap) remittanceProductChargeTO.get(CommonUtil.convertObjToStr(objArray[i]));
                Object objArr[] = remitProdChargeTOs.keySet().toArray();
                for (int index = 0, j = remitProdChargeTOs.keySet().size(); index < j; index++) {
                    objRemittanceProductChargeTO = (RemittanceProductChargeTO) remitProdChargeTOs.get(CommonUtil.convertObjToStr(objArr[index]));

                    objRemittanceProductChargeTO.setStatus(CommonConstants.STATUS_DELETED);
                    logTO.setData(objRemittanceProductChargeTO.toString());
                    logTO.setPrimaryKey(objRemittanceProductChargeTO.getKeyData());
                    logTO.setStatus(command);
                    remittanceProductChargeTOQuery("deleteRemittanceProductChargeTO", objRemittanceProductChargeTO);
                    logDAO.addToLog(logTO);
                    objRemittanceProductChargeTO = null;
                }
                remitProdChargeTOs = null;
                objArr = null;
            }
            remittanceProductChargeTO = null;
            objArray = null;

        }
        remittanceProductChargeTO = null;
    }

    private void remittanceProductBranchTOQuery(String map, RemittanceProductBranchTO objRemittanceProductBranchTO) throws Exception {
        try {
            sqlMap.executeUpdate(map, objRemittanceProductBranchTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void remittanceProductChargeTOQuery(String map, RemittanceProductChargeTO objRemittanceProductChargeTO) throws Exception {
        try {
            sqlMap.executeUpdate(map, objRemittanceProductChargeTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String str[]) {
        try {
            RemittanceProductDAO dao = new RemittanceProductDAO();
            //            RemittanceProductChargeTO rpcTO = new RemittanceProductChargeTO();
            TOHeader toHeader = new TOHeader();
            toHeader.setCommand(CommonConstants.TOSTATUS_DELETE);
            //            rpcTO.setTOHeader(toHeader);


            /**
             * **********************
             */
            HashMap hash = new HashMap();
            //            hash.put("DataDaseTO",objTO);
            //            hash.put("PROD_ID",  "as");
            ArrayList a = new ArrayList();
            a.add("INF");
            a.add("B001");

            hash.put("DRAWEE_BRANCH_CODE", a);

            System.out.println("resultList--> beore ");
            final List resultList = ClientUtil.executeQuery("deleteremittissue", hash);
            System.out.println("resultList--> after -->  " + resultList);

            dao.executeQuery(hash);

            /**
             * ***********************
             */
            //
            //            rpcTO.setProdId("tt");
            //            rpcTO.setChargeType("tt");
            //            rpcTO.setCategory("tt");
            //            rpcTO.setAmtRangeFrom(new Double(6));
            //            rpcTO.setAmtRangeTo(new Double(6));
            //            rpcTO.setCharge(new Double(6));
            //            rpcTO.setStatus("DELETED");
            //            rpTO.setStatus("CREATED");
            //            rpTO.setProdId("112233");
            //            rpTO.setProdDesc("Remittance Product 1");
            //            rpTO.setCancellChrgHd("COMIB");
            //            rpTO.setCashLimit(Double.valueOf("10"));
            //            rpTO.setDuplChrgHd("COMIB");
            //            rpTO.setEftProduct("E");
            //            rpTO.setExchangeHd("COMIB");
            //            rpTO.setIssueHd("COMIB");
            //            rpTO.setLapseAppl("Y");
            //            rpTO.setLapsePeriod(Double.valueOf("10"));
            //            rpTO.setLapsedHd("COMIB");
            //            rpTO.setNumberPattern("Decimal");
            //            rpTO.setOtherChrgHd("COMIB");
            //            rpTO.setPayHd("COMIB");
            //            rpTO.setPayIssueBranch("Y");
            //            rpTO.setPostageHd("COMIB");
            //            rpTO.setPrintServices("Y");
            //            rpTO.setSeriesMaintained("1");
            //            rpTO.setTelegramChrgHd("COMIB");
            //            rpTO.setRemarks("Remarks");
            //            rpTO.setRevalChrgHd("COMIB");
            //            rpbTO.setProdId("112233");
            //            rpbTO.setBranchCode("B007");
            //            rpbTO.setBranchName("Mg Road");
            //            rpbTO.setRemittanceLimit(Double.valueOf("10"));
            //            rpbTO.setRemittanceType("Post");
            //            rpbTO.setInwardVariableNo("Mg Road");
            //            rpbTO.setStatus("CREATED");
            //            rpbTO.setMaxAmt(Double.valueOf("10"));
            //            rpbTO.setMinAmt(Double.valueOf("10"));
            //            rpbTO.setOutwardVariableNo("B007");
            //            HashMap hash = new HashMap();
            //            hash.put("RemittanceProductChargeTO",rpcTO);
            //            hash.put("RemittanceProductBranchTO",rpbTO);
            //            dao.execute(hash);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        HashMap returnMap = null;
        remittanceProductBranchTab = new ArrayList();
        remittanceProductChargeTab = new ArrayList();
        logDAO = new LogDAO();
        logTO = new LogTO();
        objRemittanceProductTO = (RemittanceProductTO) map.get("RemittanceProductTO");
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

        userID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));

        if (map.get("RemittanceProductBranchTO") != null) {
            remittanceProductBranchTab = (ArrayList) map.get("RemittanceProductBranchTO");
        }
        if (map.get("RemittanceProductChargeTO") != null) {
            remittanceProductChargeTO = (LinkedHashMap) map.get("RemittanceProductChargeTO");
        }
        if (map.get("DELETED_CHARGETO") != null) {
            deletedRemittanceProductChargeTO = (LinkedHashMap) map.get("DELETED_CHARGETO");
        }
        command = (String) map.get("COMMAND");
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData();
            returnMap = new HashMap();
            returnMap.put("PRODUCT ID", objRemittanceProductTO.getProdId());
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData();
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        } else {
            throw new NoCommandException();
        }
        map = null;
        destroyObjects();
        System.out.println("#######returnMap" + returnMap);
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objRemittanceProductTO = null;
        objRemittanceProductBranchTO = null;
        objRemittanceProductChargeTO = null;
        logDAO = null;
        logTO = null;
    }
}