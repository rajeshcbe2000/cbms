/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CashMovementDAO.java
 *
 * Created on Fri Jan 28 14:31:55 IST 2005
 */
package com.see.truetransact.serverside.transaction.cashmanagement;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.transaction.cashmanagement.CashMovementDetailsTO;
import com.see.truetransact.transferobject.transaction.cashmanagement.CashMovementTO;
// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import java.util.Date;
/**
 * CashMovement DAO.
 *
 */
public class CashMovementDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private CashMovementTO objCashMovementTO;
    private CashMovementDetailsTO objCashMovementDetailsTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private String cashMovementID = "";
    private LinkedHashMap cashMovementDetailsHash = null;
    private Date currDt = null;
    /**
     * Creates a new instance of CashMovementDAO
     */
    public CashMovementDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap where = new HashMap();
        List list = null;
        System.out.println("#### Map in CashMovementDAO : " + map);
        where = (HashMap) map.get(CommonConstants.MAP_WHERE);
        if (map.containsKey(CommonConstants.MAP_NAME)) {
            String mapName = CommonUtil.convertObjToStr(map.get(CommonConstants.MAP_NAME));
            list = (List) sqlMap.executeQueryForList(mapName, where);
            returnMap.put(mapName, list);
            where = null;
            list = null;
        } else {
            list = (List) sqlMap.executeQueryForList("getSelectCashMovementTO", where);
            returnMap.put("CashMovementTO", list);
            where = null;
            list = null;

            where = (HashMap) map.get(CommonConstants.MAP_WHERE);
            list = (List) sqlMap.executeQueryForList("getSelectTransactionDetailsTO", where);
            returnMap.put("TransactionDetailsTO", list);
            where = null;
            list = null;

            where = (HashMap) map.get(CommonConstants.MAP_WHERE);
            list = (List) sqlMap.executeQueryForList("getSelectCashBoxDetailsTO", where);
            returnMap.put("CashBoxDetailsTO", list);
            where = null;
            list = null;
        }
        System.out.println("returnMap@@@@" + returnMap);
        return returnMap;
    }

    /**
     * Generates CASH_MOVEMENT_ID
     */
    private String getCashMovementId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "CASH_MOVEMENT_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    /**
     * Sets the values for logTO Object
     */
    private void setInitialValuesForLogTO(HashMap map) throws Exception {
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
    }

    /**
     * To set User Id, Created By, Created Dt in the TO
     */
    private void setUserIdForTO(HashMap map) {
        objCashMovementTO.setCreatedBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        objCashMovementTO.setStatusBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
//        objCashMovementTO.setAuthorizeBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
    }

    /**
     * To insert the Cash Movement
     */
    private void insertData(HashMap map) throws Exception {

        try {
            sqlMap.startTransaction();
            cashMovementID = getCashMovementId();
            objCashMovementTO.setStatus(CommonConstants.STATUS_CREATED);
            objCashMovementTO.setCashMovementId(cashMovementID);
            objCashMovementTO.setBranchId(logTO.getBranchId());
            objCashMovementTO.setCashDt(currDt);
            objCashMovementTO.setCreatedDt(currDt);
            objCashMovementTO.setStatusDt(currDt);
            sqlMap.executeUpdate("insertCashMovementTO", objCashMovementTO);
            logTO.setData(objCashMovementTO.toString());
            logTO.setPrimaryKey(objCashMovementTO.getKeyData());
            logTO.setStatus(objCashMovementTO.getCommand());
            logDAO.addToLog(logTO);

            // insert cash movement details TO
            insertCashMovementDetails(map);


            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    /**
     * To insert the Cash Movement Details
     */
    private void insertCashMovementDetails(HashMap map) throws Exception {

        if (cashMovementDetailsHash != null && cashMovementDetailsHash.size() > 0) {
            Object keySets[] = cashMovementDetailsHash.keySet().toArray();
            for (int i = 0, j = cashMovementDetailsHash.size(); i < j; i++) {
                ArrayList denomList = (ArrayList) cashMovementDetailsHash.get(CommonUtil.convertObjToStr(keySets[i]));
                for (int k = 0, size = denomList.size(); k < size; k++) {
                    HashMap denomMap = new HashMap();
                    HashMap where = new HashMap();
                    denomMap = (HashMap) denomList.get(k);
                    Object keys[] = denomMap.keySet().toArray();
                    int hashSize = denomMap.size();
                    for (int l = 0; l < hashSize; l++) {
                        objCashMovementDetailsTO = (CashMovementDetailsTO) denomMap.get(CommonUtil.convertObjToStr(keys[l]));
                        objCashMovementDetailsTO.setCashMovementId(cashMovementID);
                        objCashMovementDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
                        objCashMovementDetailsTO.setBranchId(logTO.getBranchId());
                        sqlMap.executeUpdate("insertCashMovementDetailsTO", objCashMovementDetailsTO);
                        where.put("DENOMINATION_COUNT", objCashMovementDetailsTO.getDenominationCount());
                        where.put("DENOMINATION_NAME", objCashMovementDetailsTO.getDenominationName());
                        where.put("BRANCH_CODE", _branchCode);
                        where.put("DENOMINATION_TYPE", objCashMovementDetailsTO.getDenominationType());
                        //                        if(map.containsKey("VAULT_YES") && map.containsKey("RECIEPT_YES") && where.containsValue("VAULTREC")){
                        //                            where.put("SOURCE","VAULTREC");
                        //                            sqlMap.executeUpdate("updateForexOpeningCountReciept",where);
                        //                        }
                        //                        if(map.containsKey("VAULT_YES") && map.containsKey("RECIEPT_YES") && where.containsValue("CASH BOX")){
                        //                            where.put("SOURCE","CASH BOX");
                        //                            sqlMap.executeUpdate("updateForexOpeningCountCashBox",where);
                        //                        }
                        //                        if(map.containsKey("VAULT_YES") && (map.containsKey("PAYMENT_YES") || map.containsValue("CASH BOX"))){
                        //                            sqlMap.executeUpdate("updateForexOpeningCountPayment",where);
                        //                        }
                        logTO.setData(objCashMovementDetailsTO.toString());
                        logTO.setPrimaryKey(objCashMovementDetailsTO.getKeyData());
                        logTO.setStatus(objCashMovementDetailsTO.getCommand());
                        logDAO.addToLog(logTO);
                        objCashMovementDetailsTO = null;
                    }
                    denomMap = null;
                    keys = null;
                }
                denomList = null;
            }
            keySets = null;
        }
    }

    /**
     * To update the Cash Movement Details
     */
    private void updateCashMovementDetails() throws Exception {
        if (cashMovementDetailsHash != null && cashMovementDetailsHash.size() > 0) {
            Object keySets[] = cashMovementDetailsHash.keySet().toArray();
            for (int i = 0, j = cashMovementDetailsHash.size(); i < j; i++) {
                ArrayList denomList = (ArrayList) cashMovementDetailsHash.get(CommonUtil.convertObjToStr(keySets[i]));
                for (int k = 0, size = denomList.size(); k < size; k++) {
                    HashMap denomMap = new HashMap();
                    denomMap = (HashMap) denomList.get(k);
                    Object keys[] = denomMap.keySet().toArray();
                    int hashSize = denomMap.size();
                    for (int l = 0; l < hashSize; l++) {
                        objCashMovementDetailsTO = (CashMovementDetailsTO) denomMap.get(CommonUtil.convertObjToStr(keys[l]));
                        if (objCashMovementDetailsTO.getStatus().equals(CommonConstants.STATUS_MODIFIED)) {
                            sqlMap.executeUpdate("updateCashMovementDetailsTO", objCashMovementDetailsTO);
                        } else if (objCashMovementDetailsTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                            objCashMovementDetailsTO.setBranchId(logTO.getBranchId());
                            sqlMap.executeUpdate("insertCashMovementDetailsTO", objCashMovementDetailsTO);
                        } else if (objCashMovementDetailsTO.getStatus().equals(CommonConstants.STATUS_DELETED)) {
                            sqlMap.executeUpdate("updateCashMovementDetailsTO", objCashMovementDetailsTO);
                        }
                        logTO.setData(objCashMovementDetailsTO.toString());
                        logTO.setPrimaryKey(objCashMovementDetailsTO.getKeyData());
                        logTO.setStatus(objCashMovementDetailsTO.getCommand());
                        logDAO.addToLog(logTO);
                        objCashMovementDetailsTO = null;
                    }
                    denomMap = null;
                    keys = null;
                }
                denomList = null;
            }
            keySets = null;
        }

    }

    /**
     * To update the Cash Movement
     */
    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();

            objCashMovementTO.setStatus(CommonConstants.STATUS_MODIFIED);
            sqlMap.executeUpdate("updateCashMovementTO", objCashMovementTO);
            logTO.setData(objCashMovementTO.toString());
            logTO.setPrimaryKey(objCashMovementTO.getKeyData());
            logTO.setStatus(objCashMovementTO.getCommand());
            logDAO.addToLog(logTO);

            // update Cash Movement Details TO
            updateCashMovementDetails();

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    /**
     * To delete the Cash Movement
     */
    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();

            objCashMovementTO.setStatus(CommonConstants.STATUS_DELETED);
            objCashMovementTO.setStatusDt(currDt);
            sqlMap.executeUpdate("deleteCashMovementTO", objCashMovementTO);
            logTO.setData(objCashMovementTO.toString());
            logTO.setPrimaryKey(objCashMovementTO.getKeyData());
            logTO.setStatus(objCashMovementTO.getCommand());
            logDAO.addToLog(logTO);

            // delete Cash Movement Details TO
            deleteCashMovementDetails();

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    /**
     * To delete the Cash Movement Details
     */
    private void deleteCashMovementDetails() throws Exception {
        if (cashMovementDetailsHash != null && cashMovementDetailsHash.size() > 0) {
            System.out.println("cashMovementDetailsHash@@@" + cashMovementDetailsHash);
            Object keySets[] = cashMovementDetailsHash.keySet().toArray();
            for (int i = 0, j = cashMovementDetailsHash.size(); i < j; i++) {
                ArrayList denomList = (ArrayList) cashMovementDetailsHash.get(CommonUtil.convertObjToStr(keySets[i]));
                for (int k = 0, size = denomList.size(); k < size; k++) {
                    HashMap denomMap = new HashMap();
                    denomMap = (HashMap) denomList.get(k);
                    Object keys[] = denomMap.keySet().toArray();
                    int hashSize = denomMap.size();
                    for (int l = 0; l < hashSize; l++) {
                        objCashMovementDetailsTO = (CashMovementDetailsTO) denomMap.get(CommonUtil.convertObjToStr(keys[l]));
                        System.out.println("objCashMovementDetailsTO@@@" + objCashMovementDetailsTO);
//                        objCashMovementDetailsTO.setCashMovementId(cashMovementID);
                        objCashMovementDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
                        sqlMap.executeUpdate("deleteCashMovementDetailsTO", objCashMovementDetailsTO);
                        logTO.setData(objCashMovementDetailsTO.toString());
                        logTO.setPrimaryKey(objCashMovementDetailsTO.getKeyData());
                        logTO.setStatus(objCashMovementDetailsTO.getCommand());
                        logDAO.addToLog(logTO);
                        objCashMovementDetailsTO = null;
                    }
                    denomMap = null;
                    keys = null;
                }
                denomList = null;
            }
            keySets = null;
        }
    }

    public static void main(String str[]) {
        try {
            CashMovementDAO dao = new CashMovementDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        HashMap returnMap = null;
        logDAO = new LogDAO();
        setInitialValuesForLogTO(map);

        objCashMovementTO = (CashMovementTO) map.get("CashMovementTO");
        setUserIdForTO(map);

        cashMovementDetailsHash = (LinkedHashMap) map.get("CashMovementDetailsTO");

        final String command = objCashMovementTO.getCommand();

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
            returnMap = new HashMap();
            returnMap.put("CASH_MOVEMENT_ID", objCashMovementTO.getCashMovementId());
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData();
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        } else {
            throw new NoCommandException();
        }

//        destroyObjects();
        System.out.println("###RET" + returnMap);
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        System.out.println("OBJ $$$$$$$" + obj);
        HashMap retmap = null;
        HashMap where = null;
        if (obj.containsKey("AUTHORIZED")) {
            if (obj.containsKey("transDetailsList")) {
                if (obj.get("transDetailsList") != null) {
                    List list = (List) obj.get("transDetailsList");

                    for (int l = 0; l < list.size(); l++) {
                        where = new HashMap();
                        objCashMovementDetailsTO = (CashMovementDetailsTO) list.get(l);
                        System.out.println("objCashMovementDetailsTO@@@" + objCashMovementDetailsTO);
                        where.put("DENOMINATION_COUNT", objCashMovementDetailsTO.getDenominationCount());
                        where.put("DENOMINATION_NAME", objCashMovementDetailsTO.getDenominationName());
                        where.put("BRANCH_CODE", _branchCode);
                        where.put("DENOMINATION_TYPE", objCashMovementDetailsTO.getDenominationType());
                        where.put("DENOM_TYPE", objCashMovementDetailsTO.getDenomType());
                        List lst = null;
                        if (where.containsValue("VAULTREC")) {
                            where.put("SOURCE", "VAULTREC");
                            lst = sqlMap.executeQueryForList("getTotalValueDuringDay", where);
                            sqlMap.executeUpdate("updateForexOpeningCountReciept", where);
                            if (lst != null && lst.size() > 0) {
                                where = (HashMap) lst.get(0);
                            }
                            double count = CommonUtil.convertObjToDouble(where.get("COUNT")).doubleValue();
                            double amt = count - CommonUtil.convertObjToDouble(objCashMovementDetailsTO.getDenominationTotal()).doubleValue();

                            objCashMovementDetailsTO.setMovementDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj.get("MOVEMENT_DATE"))));
                            objCashMovementDetailsTO.setTotalValue(new Double(amt));
                            sqlMap.executeUpdate("updateCashMovementDetForTotalValue", objCashMovementDetailsTO);
                        }
                        if (where.containsValue("VAULTPAYMENT")) {
                            where.put("SOURCE", "VAULTREC");
                            lst = sqlMap.executeQueryForList("getTotalValueDuringDay", where);
                            sqlMap.executeUpdate("updateForexOpeningCountPayment", where);
                            if (lst != null && lst.size() > 0) {
                                where = (HashMap) lst.get(0);
                            }
                            double count = CommonUtil.convertObjToDouble(where.get("COUNT")).doubleValue();
                            double amt = count + CommonUtil.convertObjToDouble(objCashMovementDetailsTO.getDenominationTotal()).doubleValue();

                            objCashMovementDetailsTO.setMovementDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj.get("MOVEMENT_DATE"))));
                            objCashMovementDetailsTO.setTotalValue(new Double(amt));
                            sqlMap.executeUpdate("updateCashMovementDetForTotalValue", objCashMovementDetailsTO);
                        }



                        objCashMovementDetailsTO = null;
                    }

                    list = null;
                }


            }
            if (obj.containsKey("cashDetailsList")) {
                if (obj.get("cashDetailsList") != null) {
                    List list = (List) obj.get("cashDetailsList");
                    for (int l = 0; l < list.size(); l++) {
                        where = new HashMap();
                        objCashMovementDetailsTO = (CashMovementDetailsTO) list.get(l);
                        System.out.println("objCashMovementDetailsTO@@@" + objCashMovementDetailsTO);
                        where.put("DENOMINATION_COUNT", objCashMovementDetailsTO.getDenominationCount());
                        where.put("DENOMINATION_NAME", objCashMovementDetailsTO.getDenominationName());
                        where.put("BRANCH_CODE", _branchCode);
                        where.put("DENOMINATION_TYPE", objCashMovementDetailsTO.getDenominationType());
                        where.put("DENOM_TYPE", objCashMovementDetailsTO.getDenomType());
                        List lst = null;

                        if (where.containsValue("CASH BOX") && obj.containsKey("VAULT_YES") && obj.containsKey("RECEIPT_YES")) {
                            where.put("SOURCE", "CASH BOX");
                            lst = sqlMap.executeQueryForList("getTotalValueDuringDay", where);
                            sqlMap.executeUpdate("updateForexOpeningCountCashBoxRec", where);
                            if (lst != null && lst.size() > 0) {
                                where = (HashMap) lst.get(0);
                            }
                            double count = CommonUtil.convertObjToDouble(where.get("COUNT")).doubleValue();
                            double amt = count - CommonUtil.convertObjToDouble(objCashMovementDetailsTO.getDenominationTotal()).doubleValue();

                            objCashMovementDetailsTO.setMovementDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj.get("MOVEMENT_DATE"))));
                            objCashMovementDetailsTO.setTotalValue(new Double(amt));
                            sqlMap.executeUpdate("updateCashMovementDetForTotalValue", objCashMovementDetailsTO);
                        }

                        if (where.containsValue("CASH BOX") && obj.containsKey("VAULT_YES") && obj.containsKey("PAYMENT_YES")) {
                            where.put("SOURCE", "CASH BOX");
                            lst = sqlMap.executeQueryForList("getTotalValueDuringDay", where);
                            sqlMap.executeUpdate("updateForexOpeningCountCashBoxPayment", where);
                            if (lst != null && lst.size() > 0) {
                                where = (HashMap) lst.get(0);
                            }
                            double count = CommonUtil.convertObjToDouble(where.get("COUNT")).doubleValue();
                            double amt = count + CommonUtil.convertObjToDouble(objCashMovementDetailsTO.getDenominationTotal()).doubleValue();

                            objCashMovementDetailsTO.setMovementDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj.get("MOVEMENT_DATE"))));
                            objCashMovementDetailsTO.setTotalValue(new Double(amt));
                            sqlMap.executeUpdate("updateCashMovementDetForTotalValue", objCashMovementDetailsTO);
                        }



                        objCashMovementDetailsTO = null;

                    }
                    list = null;
                }
            }

        } else {
            _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
            retmap = getData(obj);
        }
        System.out.println("retmap@@@" + retmap);
        return retmap;
    }

    private void destroyObjects() {
        objCashMovementTO = null;
        objCashMovementDetailsTO = null;
        logDAO = null;
        logTO = null;
        cashMovementDetailsHash = null;
    }
}
