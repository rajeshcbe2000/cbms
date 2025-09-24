/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ClearingDataDAO.java
 *
 * Created on Fri Aug 14 16:06:22 PST 2009
 */
package com.see.truetransact.serverside.clearing.clearingData;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Date;
import com.see.truetransact.commonutil.DateUtil;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.clearing.clearingData.ClearingDataImportDebitTO;
import com.see.truetransact.transferobject.clearing.clearingData.ClearingDataImportTO;

/**
 * ClearingData DAO. This is used for Parameter Data Access.
 *
 * @author Sathiya.V
 *
 */
public class ClearingDataImportDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private ClearingDataImportDebitTO clearingDataImportDebitTO;
    private ClearingDataImportTO clearingDataImportTO;

    /**
     * Creates a new instance of ParameterDAO
     */
    public ClearingDataImportDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    // To get the data from the database 
    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
//        String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectClearingImportDataTO", map);
        returnMap.put("CelaringDataTO", list);
        list = (List) sqlMap.executeQueryForList("getClearingImportDataDebitTO", map);
        returnMap.put("CelaringDataDebitTO", list);
        list = (List) sqlMap.executeQueryForList("getClearingImportDataCreditTO", map);
        returnMap.put("CelaringDataCreditTO", list);
        return returnMap;
    }

    // To insert the data from the database
    private void insertData(LinkedHashMap listofRecords) throws Exception {
        try {
            for (int i = 0; i < listofRecords.size(); i++) {
                if (i == 0) {
                    sqlMap.startTransaction();
                    HashMap records = new HashMap();
                    records = (HashMap) listofRecords.get(CommonUtil.convertObjToStr(listofRecords.keySet().toArray()[i]));
//                    System.out.println("records :" +records +"listofRecords.size()"+listofRecords.size());
                    clearingDataImportTO = new ClearingDataImportTO();
                    clearingDataImportTO.setTransCode(CommonUtil.convertObjToDouble(records.get("TRANS_CODE")));
                    clearingDataImportTO.setUserNumber(CommonUtil.convertObjToDouble(records.get("USER_NUMBER")));
                    clearingDataImportTO.setUserName(CommonUtil.convertObjToStr(records.get("USER_NAME")));
                    clearingDataImportTO.setUserCreditRef(CommonUtil.convertObjToStr(records.get("USER_CREDIT_REF")));
                    clearingDataImportTO.setTapeInputNo(CommonUtil.convertObjToDouble(records.get("TAPE_INPUT_NO")));
                    clearingDataImportTO.setSponsor(CommonUtil.convertObjToDouble(records.get("SPONSOR")));
                    clearingDataImportTO.setUsersAcNo(CommonUtil.convertObjToStr(records.get("USERS_AC_NO")));
                    clearingDataImportTO.setLedgerNo(CommonUtil.convertObjToStr(records.get("LEDGER_NO")));
                    clearingDataImportTO.setUserLimit(CommonUtil.convertObjToDouble(records.get("USER_LIMIT")));
                    clearingDataImportTO.setTotalAmt(CommonUtil.convertObjToDouble(records.get("TOTAL_AMT")));
                    clearingDataImportTO.setSetellementDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(records.get("SETLLEMENT_DT"))));
                    clearingDataImportTO.setSequenceNo(CommonUtil.convertObjToDouble(records.get("SEQUENCE_NO")));
                    clearingDataImportTO.setCheckSum(CommonUtil.convertObjToDouble(records.get("CHECK_SUM")));
                    clearingDataImportTO.setFilter(CommonUtil.convertObjToStr(records.get("FILTER")));
                    HashMap deleteMap = new HashMap();
                    deleteMap.put("SETLLEMENT_DT", records.get("SETLLEMENT_DT"));
                    List existingRecords = sqlMap.executeQueryForList("selectExistingRecords", deleteMap);
                    if (existingRecords != null && existingRecords.size() > 0) {
//                        System.out.println("deleteclearingDataImport :" +deleteMap);
                        sqlMap.executeUpdate("deleteClearingData", deleteMap);
//                        System.out.println("deleteclearingDataImportDebit :" +deleteMap);
                        sqlMap.executeUpdate("deleteClearingDataDebit", deleteMap);
//                        System.out.println("deleteclearingDataImportCredit :" +deleteMap);
                        sqlMap.executeUpdate("deleteClearingDataCredit", deleteMap);
//                        System.out.println("clearingDataImportTO :" +clearingDataImportTO);
                        sqlMap.executeUpdate("insertClearingDataTO", clearingDataImportTO);
                    } else {
//                        System.out.println("clearingDataImportTO :" +clearingDataImportTO);
                        sqlMap.executeUpdate("insertClearingDataTO", clearingDataImportTO);
                    }
                    sqlMap.commitTransaction();
                } else if (i != 0) {
                    sqlMap.startTransaction();
                    HashMap records = new HashMap();
                    records = (HashMap) listofRecords.get(CommonUtil.convertObjToStr(listofRecords.keySet().toArray()[i]));
//                    System.out.println("records :" +records +"listofRecords.size()"+listofRecords.size());
                    clearingDataImportDebitTO = new ClearingDataImportDebitTO();
                    clearingDataImportDebitTO.setTransCode(CommonUtil.convertObjToDouble(records.get("TRANS_CODE")));
                    clearingDataImportDebitTO.setDestinationSortCode(CommonUtil.convertObjToDouble(records.get("DESTINATION_SORT_CODE")));
                    clearingDataImportDebitTO.setDestinationACType(CommonUtil.convertObjToStr(records.get("DESTINATION_AC_TYPE")));
                    clearingDataImportDebitTO.setLedgerNo(CommonUtil.convertObjToStr(records.get("LEDGER_NO")));
                    clearingDataImportDebitTO.setDestinationACNo(CommonUtil.convertObjToDouble(records.get("DESTINATION_AC_NO")));
                    clearingDataImportDebitTO.setDestinationACName(CommonUtil.convertObjToStr(records.get("DESTINATION_AC_HOLDERS_NAME")));
                    clearingDataImportDebitTO.setSponsor(CommonUtil.convertObjToDouble(records.get("SPONSOR_BANK")));
                    clearingDataImportDebitTO.setUserNumber(CommonUtil.convertObjToDouble(records.get("USER_NO")));
                    clearingDataImportDebitTO.setUserName(CommonUtil.convertObjToStr(records.get("USER_NAME")));
                    clearingDataImportDebitTO.setUserCreditRef(CommonUtil.convertObjToStr(records.get("USER_CREDIT_REFERENCE")));
                    clearingDataImportDebitTO.setTotalAmt(CommonUtil.convertObjToDouble(records.get("TOTAL_AMT")));
                    clearingDataImportDebitTO.setSequenceNo(CommonUtil.convertObjToDouble(records.get("SEQUENCE_NO")));
                    clearingDataImportDebitTO.setCheckSum(CommonUtil.convertObjToDouble(records.get("CHECK_SUM")));
                    clearingDataImportDebitTO.setFilter(CommonUtil.convertObjToStr(records.get("FILTER")));
//                    System.out.println("clearingDataImportDebitTO :" +clearingDataImportDebitTO);
                    sqlMap.executeUpdate("insertClearingDataDebitTO", clearingDataImportDebitTO);
                    sqlMap.commitTransaction();
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    // To update the data from the database
    private void updateData() throws Exception {
        try {
//            parameterTO.setStatus(CommonConstants.STATUS_MODIFIED);
            sqlMap.startTransaction();
            sqlMap.executeUpdate("insertClearingDataTO", clearingDataImportDebitTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    // To delete the data from the database
    private void deleteData() throws Exception {
        try {
//            parameterTO.setStatus(CommonConstants.STATUS_DELETED);
            sqlMap.startTransaction();
            sqlMap.executeUpdate("insertClearingDataTO", clearingDataImportDebitTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    // To insert or update or delete the data in the database
    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        System.out.println("clearingDataImportDao :" + map);
        LinkedHashMap listofRecords = (LinkedHashMap) map.get("TOTALRECORDS");
//        final String command = clearingDataImportTO.getCommand();
        if (listofRecords != null && listofRecords.size() > 0 && map.containsKey("TOTALRECORDS")) {
            insertData(listofRecords);
//        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)){
//            updateData();
//        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)){
//            deleteData();
        } else {
            throw new NoCommandException();
        }
        destroyObjects();
        return null;
    }

    // To retrive data from the database
    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    // To nulify the objects 
    private void destroyObjects() {
        clearingDataImportDebitTO = null;
    }

    public static void main(String str[]) {
        try {
            ClearingDataImportDAO dao = new ClearingDataImportDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
