/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * AgentDAO.java
 *
 * Created on Wed Feb 02 13:11:28 IST 2005
 */
package com.see.truetransact.serverside.termloan.appraiserCommision;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.transferobject.agent.AgentTO;

import com.see.truetransact.transferobject.agent.AgentCommisonTO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
//import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;

import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.commonutil.TTException;

/**
 * Agent DAO.
 *
 */
public class AppraiserCommisionDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private AgentTO objAgentTO;
    private String userID = "";
    private String remarks = "";
    private HashMap agentDetailMap = new HashMap();
    //    private TransferDAO transferDAO;
    private TransactionDAO transactionDAO;
    private Date currDt = null;
    /**
     * Creates a new instance of AgentDAO
     */
    public AppraiserCommisionDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        //		String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectAgentTO", map);
        returnMap.put("AgentTO", list);
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;
    }

    private void insertData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();

            objAgentTO.setCreatedBy(userID);
            objAgentTO.setCreatedDt(currDt);
            objAgentTO.setStatus(CommonConstants.STATUS_CREATED);
            objAgentTO.setStatusBy(userID);
            objAgentTO.setStatusDt(currDt);
            sqlMap.executeUpdate("insertAgentTO", objAgentTO);

            objLogTO.setData(objAgentTO.toString());
            objLogTO.setPrimaryKey(objAgentTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();

            objAgentTO.setStatus(CommonConstants.STATUS_MODIFIED);
            objAgentTO.setStatusBy(userID);
            objAgentTO.setStatusDt(currDt);
            objAgentTO.setAuthorizedBy(null);
            objAgentTO.setAuthorizedDt(null);
            objAgentTO.setAuthorizedStatus(null);
            //            objAgentTO.setAppointedDt(currDt);
            //            objAgentTO.setRemarks(remarks);

            sqlMap.executeUpdate("updateAgentTO", objAgentTO);

            objLogTO.setData(objAgentTO.toString());
            objLogTO.setPrimaryKey(objAgentTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();

            objAgentTO.setStatus(CommonConstants.STATUS_DELETED);
            objAgentTO.setStatusBy(userID);
            objAgentTO.setStatusDt(currDt);

            sqlMap.executeUpdate("deleteAgentTO", objAgentTO);

            objLogTO.setData(objAgentTO.toString());
            objLogTO.setPrimaryKey(objAgentTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            AppraiserCommisionDAO dao = new AppraiserCommisionDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("Agent Map Dao : " + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
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

        if (!map.containsKey("AGENT_COMMISION_DETAILS")) {
            objAgentTO = (AgentTO) map.get("AgentTO");
            final String command = objAgentTO.getCommand();

            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData(objLogDAO, objLogTO);
                returnMap.put("AGENT ID", objAgentTO.getAgentId());
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData(objLogDAO, objLogTO);
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData(objLogDAO, objLogTO);
            } else {
                throw new NoCommandException();
            }
        } else {
            //            agentDetailMap=(HashMap)map.get("AGENT_COMMISION_DETAILS");
            //            agentCommisionDebitCreditAccount(agentDetailMap);
            setOprativeTrans(map);
        }
        objLogDAO = null;
        objLogTO = null;
        destroyObjects();
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;
    }

    public void agentCommisionDebitCreditAccount(HashMap agentDetailMap) throws Exception {
        System.out.println("agentCommisionDebitCreditAccount :" + agentDetailMap);
        double operativeAmt = CommonUtil.convertObjToDouble(agentDetailMap.get("OP_AMT")).doubleValue();
        HashMap txMap = new HashMap();
        txMap = new HashMap();
        ArrayList transferList = new ArrayList(); // for local transfer
        HashMap depositAcHd = new HashMap();
        depositAcHd.put("DAILY", "DAILY");
        List lst = sqlMap.executeQueryForList("getAgentCommisionAcHd", depositAcHd);
        if (lst.size() > 0) {
            HashMap acHdMap = new HashMap();
            acHdMap = (HashMap) lst.get(0);
            txMap.put(TransferTrans.DR_AC_HD, (String) acHdMap.get("COMMISION_HEAD"));
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
            System.out.println("lst.size()>0 :" + acHdMap);
            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(agentDetailMap.get("OP_AMT")));
            txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(agentDetailMap.get("OA_ACT_NUM")));
            txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(agentDetailMap.get("PROD_ID")));
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
            txMap.put(TransferTrans.CURRENCY, "INR");

            System.out.println("####### insertAccHead txMap " + txMap);
            TransferTrans trans = new TransferTrans();
            trans.setTransMode(CommonConstants.TX_TRANSFER);
            trans.setInitiatedBranch(_branchCode);
            trans.setLinkBatchId(CommonUtil.convertObjToStr(agentDetailMap.get("LINK_BATCH_COMMISION_ID")));

            System.out.println("######### Trans :" + trans);
            transferList.add(trans.getDebitTransferTO(txMap, operativeAmt));
            transferList.add(trans.getCreditTransferTO(txMap, operativeAmt));
            trans.doDebitCredit(transferList, _branchCode, false);
        }
    }

    public void setOprativeTrans(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();

            HashMap opMap = new HashMap();
            AgentCommisonTO agComTo = new AgentCommisonTO();
            ArrayList transferList = new ArrayList();
            AgentTO agTo = new AgentTO();
            agComTo = (AgentCommisonTO) map.get("AgentCommisionTO");
            agTo = (AgentTO) map.get("AGENT_COMMISION_DETAILS");
            TxTransferTO txtTo = new TxTransferTO();
            opMap.put("ACT_NUM", agTo.getOperativeAcNo());
            opMap.put("AGENT_ID", agTo.getAgentId());
            List lst = ServerUtil.executeQuery("getACheadANDPRODID", opMap);//FOR 90% COM TO  OPRATIVE  ACCOUNT
            if (lst != null && opMap.size() > 0) {
                opMap = new HashMap();
                txtTo = new TxTransferTO();
                opMap = (HashMap) lst.get(0);
                txtTo.setAcHdId(CommonUtil.convertObjToStr(opMap.get("AC_HD_ID")));
                txtTo.setActNum(agTo.getOperativeAcNo());
                txtTo.setProdId(CommonUtil.convertObjToStr(opMap.get("PROD_ID")));
                txtTo.setAmount(agComTo.getComToOAacc());
                txtTo.setInpCurr("INR");
                txtTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                txtTo.setStatus("CREATED");
                txtTo.setBranchId(_branchCode);
                txtTo.setInitiatedBranch(_branchCode);
                txtTo.setInpAmount(agComTo.getComToOAacc());
                txtTo.setParticulars("Agent Comm UpTo" + DateUtil.getStringDate(agComTo.getToDate()));
                txtTo.setProdType("OA");
                txtTo.setStatusDt(currDt);
                txtTo.setTransDt(ServerUtil.getCurrentDateWithTime(_branchCode));
                txtTo.setTransMode("TRANSFER");
                txtTo.setTransType(CommonConstants.CREDIT);
                txtTo.setInitiatedBranch(_branchCode);
                transferList.add(txtTo);
                System.out.println("transferList&&&&&&&&& FIRST " + transferList);
            } else {
                throw new TTException("Operative Account is not set for this Agent");
            }
            opMap.put("AGENT_ID", agTo.getAgentId());
            opMap.put("ACT_NUM", agTo.getDpacnum());
            List tDlst = ServerUtil.executeQuery("getACheadANDPRODID", opMap); //FOR 10% COM TO  OPRATIVE  ACCOUNT
            if (tDlst != null && tDlst.size() > 0) {
                opMap = new HashMap();
                opMap = (HashMap) tDlst.get(0);
                txtTo = new TxTransferTO();
                txtTo.setProdType(CommonUtil.convertObjToStr(opMap.get("DP_PROD_TYPE")));
                txtTo.setProdId(CommonUtil.convertObjToStr(opMap.get("DP_PROD_ID")));
                txtTo.setAcHdId(CommonUtil.convertObjToStr(opMap.get("AC_HD_ID")));
                txtTo.setInpCurr("INR");
                txtTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                txtTo.setInitiatedBranch(_branchCode);
                txtTo.setStatus("CREATED");
                txtTo.setBranchId(_branchCode);
                txtTo.setInitiatedBranch(_branchCode);
                txtTo.setParticulars("Agent Comm UpTo" + DateUtil.getStringDate(agComTo.getToDate()));
                txtTo.setStatusDt(currDt);
                txtTo.setTransDt(ServerUtil.getCurrentDateWithTime(_branchCode));
                txtTo.setTransMode("TRANSFER");
                txtTo.setTransType(CommonConstants.CREDIT);
                txtTo.setActNum(agTo.getDpacnum());
                txtTo.setAmount(agComTo.getComToTDacc());
                txtTo.setInpAmount(agComTo.getComToTDacc());
                transferList.add(txtTo);
                System.out.println("transferList&&&&&&&&& SECOND " + transferList);
            } else {
                throw new TTException("InOperative Account is not set for this Agent");
            }
            opMap.put("DAILY", "DAILY");
            List comDRHdlst = sqlMap.executeQueryForList("getAgentCommisionAcHd", opMap);
            if (comDRHdlst != null && comDRHdlst.size() > 0) {
                opMap = new HashMap();
                opMap = (HashMap) comDRHdlst.get(0);
                txtTo = new TxTransferTO();
                txtTo.setInpCurr("INR");
                txtTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                txtTo.setStatus("CREATED");
                txtTo.setBranchId(_branchCode);
                txtTo.setInitiatedBranch(_branchCode);
                txtTo.setParticulars("Agent Comm UpTo" + DateUtil.getStringDate(agComTo.getToDate()));
                txtTo.setStatusDt(currDt);
                txtTo.setTransDt(ServerUtil.getCurrentDateWithTime(_branchCode));
                txtTo.setTransMode("TRANSFER");
                txtTo.setProdType("GL");
                txtTo.setAcHdId((String) opMap.get("COMMISION_HEAD"));
                txtTo.setAmount(CommonUtil.convertObjToDouble(agComTo.getCommisionForThePeriod()));
                txtTo.setInpAmount(CommonUtil.convertObjToDouble(agComTo.getCommisionForThePeriod()));
                txtTo.setTransType(CommonConstants.DEBIT);
                txtTo.setInitiatedBranch(_branchCode);
                System.out.println("transferList&&&&&&&&&" + transferList);
                transferList.add(txtTo);
                System.out.println("transferList&&&&&&&&&THIRD" + transferList);
            } else {
                throw new TTException("Commission  Account Head  is not set for this Agent");
            }
            System.out.println("transferList&&&&&&&&&THIRD" + transferList);
            TransferTrans trans = new TransferTrans();
            //               trans.doDebitCredit(transferList, _branchCode, false);
            trans.setInitiatedBranch(_branchCode);
            trans.doDebitCredit(transferList, _branchCode, true);
            opMap = new HashMap();
            Date agCompaidDt = agComTo.getToDate();
            agCompaidDt = DateUtil.addDays(agCompaidDt, 1);
            opMap.put("LAST_COM_PAID_DT", agCompaidDt);
            opMap.put("AGENT_ID", agComTo.getAgentId());
            sqlMap.executeUpdate("updateLastComPaidDate", opMap);
            HashMap monthlyScheduleMap = new HashMap();
            monthlyScheduleMap.put("AGENT_ID", agComTo.getAgentId());
            monthlyScheduleMap.put("FROM_DATE", agComTo.getFromDate());
            monthlyScheduleMap.put("TO_DATE", agComTo.getToDate());
            monthlyScheduleMap.put("COLLECTED_AMT", CommonUtil.convertObjToDouble(agComTo.getCommision()));
            monthlyScheduleMap.put("COMMISION_AMT", CommonUtil.convertObjToDouble(agComTo.getCommisionForThePeriod()));
            monthlyScheduleMap.put("COMMISION_TO_OA", agComTo.getComToOAacc());
            monthlyScheduleMap.put("COMMISION_TO_TD", agComTo.getComToTDacc());
            monthlyScheduleMap.put("COMMISION_TO_TDS", agComTo.getTdsAmt());
            monthlyScheduleMap.put("COMMISION_GIVEN_DT", currDt.clone());
            monthlyScheduleMap.put("COMMISION_GIVEN_BY", CommonUtil.convertObjToStr(map.get("USER_ID")));
            System.out.println("###### monthlyScheduleMap : " + monthlyScheduleMap);
            sqlMap.executeUpdate("insertMonthlyScheduleList", monthlyScheduleMap);
            monthlyScheduleMap = null;
            sqlMap.commitTransaction();

        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TTException(e);
        }
    }

    public void insertAgentsAccount(HashMap map, HashMap transactionMap, String branchID) throws Exception {
        System.out.println("###### map : " + map);

        ArrayList agentsList = new ArrayList();


    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objAgentTO = null;
    }
}
