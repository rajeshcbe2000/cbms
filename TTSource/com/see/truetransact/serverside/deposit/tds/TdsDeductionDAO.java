/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TdsDeductionTODAO.java
 *
 * Created on Mon Mar 22 15:25:59 IST 2004
 */
package com.see.truetransact.serverside.deposit.tds;

import java.util.List;
import java.util.ArrayList;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.deposit.tds.TdsDeductionTO;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.tds.tdscalc.TdsCalc;


import java.util.HashMap;

import java.util.Date;

/**
 * TdsDeductionTO DAO.
 *
 */
public class TdsDeductionDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private TdsDeductionTO objTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private List list;
    private String Deposit_Tds_ID;
    private IDGenerateDAO dao;
    private HashMap where;
    private Date currentDate;

    /**
     * Creates a new instance of TdsDeductionTODAO
     */
    public TdsDeductionDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String actBranch = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        currentDate = ServerUtil.getCurrentDate(actBranch);
        TdsCalc tds = new TdsCalc(actBranch);
        System.out.println("map----------->" + map);
        String all = CommonUtil.convertObjToStr(map.get("ALLTDSACCOUNT"));

        if (all.length() > 0 && map.containsKey("ALLTDSACCOUNT") && map.get("ALLTDSACCOUNT").equals("ALLTDSACCOUNT")) {
            list = (List) sqlMap.executeQueryForList("getAllAccountFinIntForCustomer", map);
            double totIntAmt = tds.totalIntrestCalculationforTds(map, null, null);
            map.put("END1", map.get("LASTFIN"));
            double finIntAmt = tds.totalIntrestCalculationforTds(map, null, null);
            returnMap.put("TOTALINTAMT", new Double(totIntAmt));
            returnMap.put("FININTAMT", new Double(finIntAmt));
            System.out.println("returnMap-------------> before" + returnMap);

            returnMap.put("INTERESTALL", list);
            System.out.println("returnMap-------------> After" + returnMap);
            list = (List) sqlMap.executeQueryForList("getTDSConfigData", map);
            returnMap.put("TDSPARAMETER", list);


        } else {
            System.out.println("map----------->" + map);
            String where = (String) map.get(CommonConstants.MAP_WHERE);
            list = (List) sqlMap.executeQueryForList("getSelectTdsDeductionTO", where);
            returnMap.put("TdsDeductionTO", list);


        }
        map = null;
        where = null;
        list = null;
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;
    }

    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            Deposit_Tds_ID = getDepositTdsID();
            objTO.setTdsId(Deposit_Tds_ID);
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("insertTdsDeductionTO", objTO);
            Deposit_Tds_ID = null;
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private String getDepositTdsID() throws Exception {
        dao = new IDGenerateDAO();
        where = new HashMap();
        where.put("WHERE", "DEPOSIT.TSD_ID");
        String str = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        where = null;
        dao = null;
        return str;
    }

    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            System.out.println("objTO.getModified()------------->" + objTO.getModified());
            System.out.println("objTO.getModified()------------->" + objTO.getModified());

            if (objTO.getAuthorizeStatus().length() > 0 && objTO.getAuthorizeStatus().equals("AUTHORIZED")) {
                String where = CommonUtil.convertObjToStr(objTO.getTdsId());
                System.out.println("where------------->" + where);
                list = (List) sqlMap.executeQueryForList("getSelectTdsDeductionTO", where);
                TdsDeductionTO objModified = new TdsDeductionTO();
                if (list != null && list.size() > 0) {
                    objModified = (TdsDeductionTO) list.get(0);
                    sqlMap.executeUpdate("insertTdsDeductionTOTemp", objModified);
                }

            }
            sqlMap.executeUpdate("updateTdsDeductionTO", objTO);
            logDAO.addToLog(logTO);
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
            objTO.setStatus(CommonConstants.STATUS_DELETED);
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("deleteTdsDeductionTO", objTO);
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    /*
     * public static void main(String str[]) { try { TdsDeductionDAO dao = new
     * TdsDeductionDAO(); TdsDeductionTO objTO = new TdsDeductionTO(); TOHeader
     * toHeader = new TOHeader();
     * toHeader.setCommand(CommonConstants.TOSTATUS_DELETE);
     * objTO.setTOHeader(toHeader); objTO.setTdsId("LOHITH");
     * objTO.setProdId("tt"); objTO.setDepositNo("tt");
     * objTO.setDepositDt(DateUtil.getDateMMDDYYYY(null));
     * objTO.setDepositAmt(new Double(20)); objTO.setInterestPayable(new
     * Double(20)); objTO.setTdsStartDt(DateUtil.getDateMMDDYYYY(null));
     * objTO.setCollectionType("asdasd"); objTO.setDebitAcHd("asdasd");
     * objTO.setRemarks("asdasd"); objTO.setDepositSubNo("asdasd");
     * objTO.setMaturityDt(DateUtil.getDateMMDDYYYY(null));
     * objTO.setInterestAccrued(new Double(20)); objTO.setInterestPaid(new
     * Double(5)); objTO.setTdsEndDt(DateUtil.getDateMMDDYYYY(null));
     * objTO.setTdsAmt(new Double(5)); objTO.setDebitAcctNo("tt");
     * objTO.setStatus("DELETED"); HashMap hash = new HashMap();
     * hash.put("TdsDeductionTO",objTO); dao.execute(hash); } catch (Exception
     * ex) { ex.printStackTrace(); } }
     */
    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        logDAO = new LogDAO();
        logTO = new LogTO();
        System.out.println("map-------------->" + map);

        currentDate = ServerUtil.getCurrentDate(_branchCode);

        HashMap tdsDeductId = new HashMap();
        if (!map.containsKey("REJECTMAP")) {
            objTO = (TdsDeductionTO) map.get("TdsDeductionTO");
            logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
            logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
            logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
            logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
            logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

            final String command = objTO.getCommand();
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData();
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData();
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData();
            } else {
                throw new NoCommandException();
            }
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                tdsDeductId.put(CommonConstants.TRANS_ID, objTO.getTdsId());
            }
        } else {
            HashMap rejMap = new HashMap();

            rejMap = (HashMap) map.get("REJECTMAP");
            System.out.println("rejMap------------------>" + rejMap);

            afterAuthorizeReject(rejMap);
        }
        map = null;
        destroyObjects();
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return tdsDeductId;
    }

    private void afterAuthorizeReject(HashMap authDataMap) throws Exception {
        try {
            sqlMap.startTransaction();
            String where = (String) authDataMap.get("TDS_ID");
            list = (List) sqlMap.executeQueryForList("getSelectTdsDeductionTOTemp", where);
            if (list != null && list.size() > 0) {
                TdsDeductionTO objTO1 = new TdsDeductionTO();
                objTO1 = (TdsDeductionTO) list.get(0);
                Date applnDt = (Date) currentDate.clone();
                Date dpDt = DateUtil.getDateMMDDYYYY(DateUtil.getStringDate(objTO1.getDepositDt()));
                if (dpDt.getDate() > 0) {
                    applnDt.setDate(dpDt.getDate());
                    applnDt.setMonth(dpDt.getMonth());
                    applnDt.setYear(dpDt.getYear());
                }
                objTO1.setDepositDt(applnDt);
                applnDt = (Date) currentDate.clone();
                Date mtDt = DateUtil.getDateMMDDYYYY(DateUtil.getStringDate(objTO1.getMaturityDt()));
                if (dpDt.getDate() > 0) {
                    applnDt.setDate(mtDt.getDate());
                    applnDt.setMonth(mtDt.getMonth());
                    applnDt.setYear(dpDt.getYear());
                }
                objTO1.setMaturityDt(applnDt);
                sqlMap.executeUpdate("rejectionModifiedTdsDeductionDeletion", authDataMap);
                sqlMap.executeUpdate("insertTdsDeductionTO", objTO1);
                sqlMap.executeUpdate("authorizeTdsDeductionTempDeletion", authDataMap);

            }

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }

    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
        logDAO = null;
        logTO = null;
    }
}
