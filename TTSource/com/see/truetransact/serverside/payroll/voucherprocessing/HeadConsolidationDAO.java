/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * HeadConsolidationDAO.java
 *
 * Created on Wed Feb 02 13:11:28 IST 2015
 */
package com.see.truetransact.serverside.payroll.voucherprocessing;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.payroll.voucherprocessing.PaymentVoucherTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * HeadConsolidation DAO.
 *
 */
public class HeadConsolidationDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private PaymentVoucherTO objPaymentVoucherTO;
    private String userID = "";
    private String remarks = "";
    private HashMap agentDetailMap = new HashMap();
    //    private TransferDAO transferDAO;
    private TransactionDAO transactionDAO;
    final int EDIT = 0, DELETE = 1, AUTHORIZE = 2, AGENTID = 3, ACCNO = 4, DEPOSITNO = 5, VIEW = 10, ACCTHDID = 6;

    /**
     * Creates a new instance of AgentDAO
     */
    public HeadConsolidationDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        //		String where = (String) map.get(CommonConstants.MAP_WHERE);
        int type = CommonUtil.convertObjToInt(map.get("VIEWTYPE"));
        List list = null;
        if (type == EDIT || type == DELETE) {
            list = (List) sqlMap.executeQueryForList("getHeadConsolidationEditTO", map);
        }
        returnMap.put("PaymentVoucherTO", list);
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        list = null;
        return returnMap;
    }

    private void insertData(LogDAO objLogDAO, LogTO objLogTO, HashMap map) throws Exception {
        try {
            List headsTotList = null;
            if (objPaymentVoucherTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                headsTotList = (List) map.get("mappingHeadsEdit");
            } else {
                headsTotList = (List) map.get("mappingHeads");
            }
            System.out.println("heads size " + headsTotList.size());
            if (headsTotList != null && headsTotList.size() > 0) {
                for (int i = 0; i < headsTotList.size(); i++) {
                    sqlMap.startTransaction();
                    ArrayList headList = new ArrayList();
                    headList = (ArrayList) headsTotList.get(i);
                    System.out.println("value here" + headsTotList.get(i));
                    for (int j = 0; j < headList.size(); j++) {
                        objPaymentVoucherTO.setAccHead(CommonUtil.convertObjToStr(headList.get(0)));
                        objPaymentVoucherTO.setPayCode(CommonUtil.convertObjToStr(headList.get(1)));
                    }
                    objPaymentVoucherTO.setStatus(CommonConstants.STATUS_CREATED);
                    System.out.println("array list here" + headList);              
                    sqlMap.executeUpdate("insertPayrollDeductionMapping", objPaymentVoucherTO);
                    sqlMap.commitTransaction();
                }
                objLogTO.setData(objPaymentVoucherTO.toString());
                objLogTO.setPrimaryKey(objPaymentVoucherTO.getKeyData());
                objLogDAO.addToLog(objLogTO);


            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private String getAgentMachineId() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "AGENT_MAHINE_ID");
        String strBorrower_No = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        where = null;
        dao = null;
        return strBorrower_No;
    }

    private void updateData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();
            objPaymentVoucherTO.setStatus(CommonConstants.STATUS_CREATED);
            sqlMap.executeUpdate("insertPayrollDeductionMapping", objPaymentVoucherTO);
            objLogTO.setData(objPaymentVoucherTO.toString());
            objLogTO.setPrimaryKey(objPaymentVoucherTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData(LogDAO objLogDAO, LogTO objLogTO, HashMap map) throws Exception {
        try {
            List headsTotList = (List) map.get("mappingHeadsDelete");
            System.out.println("heads size " + headsTotList.size());
            sqlMap.startTransaction();
            if (headsTotList != null && headsTotList.size() > 0) {
                for (int i = 0; i < headsTotList.size(); i++) {                  
                    ArrayList headList = new ArrayList();
                    headList = (ArrayList) headsTotList.get(i);
                    System.out.println("value here" + headsTotList.get(i));
                    for (int j = 0; j < headList.size(); j++) {
                        objPaymentVoucherTO.setAccHead(CommonUtil.convertObjToStr(headList.get(0)));
                        objPaymentVoucherTO.setPayCode(CommonUtil.convertObjToStr(headList.get(1)));
                    }
                    System.out.println("array list here" + headList);
                    sqlMap.executeUpdate("deletePayrollDeductionMappedHeads", objPaymentVoucherTO);
                }
                objLogTO.setData(objPaymentVoucherTO.toString());
                objLogTO.setPrimaryKey(objPaymentVoucherTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            HeadConsolidationDAO dao = new HeadConsolidationDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("head Map Dao : " + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        HashMap returnMap = new HashMap();
        // Log DAO
        LogDAO objLogDAO = new LogDAO();

        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        objLogTO.setSelectedBranchId((String) map.get(CommonConstants.SELECTED_BRANCH_ID));
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));

        userID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));

        if (map.containsKey("PayAccTO")) {
            objPaymentVoucherTO = (PaymentVoucherTO) map.get("PayAccTO");
            final String command = objPaymentVoucherTO.getCommand();
            System.out.println("command here" + command);

            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                if (map.containsKey("mappingHeads")) {
                    insertData(objLogDAO, objLogTO, map);
                }
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                if (map.containsKey("mappingHeadsEdit")) {
                    insertData(objLogDAO, objLogTO, map);
                }
                if (map.containsKey("mappingHeadsDelete")) {
                    deleteData(objLogDAO, objLogTO, map);
                }
            } 
            else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData(objLogDAO, objLogTO, map);
            } else {
                throw new NoCommandException();
            }
        }
        objLogDAO = null;
        objLogTO = null;
        destroyObjects();
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;
    }
    public void insertAgentsAccount(HashMap map, HashMap transactionMap, String branchID) throws Exception {
        System.out.println("###### map : " + map);

        ArrayList agentsList = new ArrayList();


    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void destroyObjects() {
        objPaymentVoucherTO = null;
    }
}
