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
package com.see.truetransact.serverside.agent;

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
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.transferobject.agent.AgentColProdTO;
import com.see.truetransact.transferobject.product.deposits.AgentCommisonSlabTO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.transferobject.agent.AgentLeaveDetailsTO;
import java.util.LinkedHashMap;

/**
 * Agent DAO.
 *
 */
public class AgentDAO extends TTDAO {
    private Date currDt = null;
    private static SqlMap sqlMap = null;
    private AgentTO objAgentTO;
    private AgentLeaveDetailsTO objAgentLeaveDetailsTO;
    private LinkedHashMap agentLeaveDetailsMap,agentDeletedLeaveMap;
    private String userID = "";
    private String remarks = "";
    private HashMap agentDetailMap = new HashMap();
    //    private TransferDAO transferDAO;
    private TransactionDAO transactionDAO;
    TransferDAO transferDAO = new TransferDAO();
    private String generateSingleTransId="";
    HashMap returnMap = new HashMap();
    /**
     * Creates a new instance of AgentDAO
     */
    public AgentDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        //		String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectAgentTO", map);
        if(list != null && list.size()>0){
            returnMap.put("AgentTO", list);
        }
        List prodList = (List) sqlMap.executeQueryForList("getSelectAgentProductTo", map);
        if (prodList != null && prodList.size() > 0) {
            LinkedHashMap dataMap = new LinkedHashMap();
            System.out.println("@@@prodList" + prodList);
            for (int i = prodList.size(), j = 0; i > 0; i--, j++) {
                dataMap.put(((AgentColProdTO) prodList.get(j)).getProdId(), prodList.get(j));
            }
            System.out.println("@@@dataMap" + dataMap);
            returnMap.put("AGENT_PROD_LIST", dataMap);
        }
        
        List leaveList = (List) sqlMap.executeQueryForList("getSelectAgentLeaveDetailsTo", map);
        if (leaveList != null && leaveList.size() > 0) {
            LinkedHashMap dataMap = new LinkedHashMap();
            System.out.println("@@@leaveList : " + leaveList);
            for (int i = leaveList.size(), j = 0; i > 0; i--, j++) {
                dataMap.put(((AgentLeaveDetailsTO) leaveList.get(j)).getSlNo(), leaveList.get(j));
            }
            System.out.println("@@@dataMap : " + dataMap);
            returnMap.put("AGENT_LEAVE_DETAILS", dataMap);
        }
        
        //added by srekrishnan
        if(map.containsKey("AGENT_COMMISSION_SLAB")){
            HashMap agentMap = new HashMap();
            agentMap.put("PROD_ID", map);
            list = (List) sqlMap.executeQueryForList("getSelectAgentCommissionSlab", agentMap);        
            if (list != null && list.size() > 0) {
                LinkedHashMap ParMap = new LinkedHashMap();
                System.out.println("@@@list" + list);
                for (int i = list.size(), j = 0; i > 0; i--, j++) {
                    ParMap.put(((AgentCommisonSlabTO) list.get(j)).getFromDt(), list.get(j));
                }
                System.out.println("@@@ParMap" + ParMap);
                returnMap.put("AGENT_COMMISSION_SLAB", ParMap);
            }    
        }
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;
    }

    private void insertData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();
            if(objAgentTO != null){
                objAgentTO.setCreatedBy(userID);
                objAgentTO.setCreatedDt(currDt);
                objAgentTO.setStatus(CommonConstants.STATUS_CREATED);
                objAgentTO.setStatusBy(userID);
                objAgentTO.setStatusDt(currDt);
                objAgentTO.setAgentMachineId(getAgentMachineId());
                sqlMap.executeUpdate("insertAgentTO", objAgentTO);

                objLogTO.setData(objAgentTO.toString());
                objLogTO.setPrimaryKey(objAgentTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            }
            if(agentLeaveDetailsMap != null){
                String leaveID = getLeaveID();
                ArrayList leaveList = new ArrayList(agentLeaveDetailsMap.keySet());
                int length = leaveList.size();
                for (int i = 0; i < length; i++) {
                    AgentLeaveDetailsTO objAgentLeaveDetailsTO = (AgentLeaveDetailsTO) agentLeaveDetailsMap.get(leaveList.get(i));
                    objAgentLeaveDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
                    objAgentLeaveDetailsTO.setStatusDt((Date)currDt.clone());
                    objAgentLeaveDetailsTO.setStatusBy(userID);
                    objAgentLeaveDetailsTO.setAgentLeaveId(leaveID);
                    sqlMap.executeUpdate("insertAgentLeaveDetailsTO", objAgentLeaveDetailsTO);
                }
            }

            sqlMap.commitTransaction();
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
//        setBorrower_No(strBorrower_No);
        return strBorrower_No;
    }

    private void updateData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();
            if(objAgentTO != null){
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
            }
            if(agentLeaveDetailsMap != null){
                String leaveID = getLeaveID();
                ArrayList leaveList = new ArrayList(agentLeaveDetailsMap.keySet());
                int length = leaveList.size();
                for (int i = 0; i < length; i++) {
                    AgentLeaveDetailsTO objAgentLeaveDetailsTO = (AgentLeaveDetailsTO) agentLeaveDetailsMap.get(leaveList.get(i));
                    objAgentLeaveDetailsTO.setStatusDt((Date)currDt.clone());
                    objAgentLeaveDetailsTO.setStatusBy(userID);
                    if(objAgentLeaveDetailsTO.getStatus().equals(CommonConstants.STATUS_CREATED)){
                        if(objAgentLeaveDetailsTO.getAgentLeaveId()!=null && objAgentLeaveDetailsTO.getAgentLeaveId().length()>0){
                            objAgentLeaveDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
                            sqlMap.executeUpdate("updateAgentLeaveDetailsTO", objAgentLeaveDetailsTO);
                        }else{
                            objAgentLeaveDetailsTO.setAgentLeaveId(leaveID);
                            objAgentLeaveDetailsTO.setStatus(CommonConstants.STATUS_CREATED);                        
                            sqlMap.executeUpdate("insertAgentLeaveDetailsTO", objAgentLeaveDetailsTO);                            
                        }
                    }else if(objAgentLeaveDetailsTO.getStatus().equals(CommonConstants.STATUS_MODIFIED)){
                        objAgentLeaveDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        sqlMap.executeUpdate("updateAgentLeaveDetailsTO", objAgentLeaveDetailsTO);
//                    }else if(objAgentLeaveDetailsTO.getStatus().equals(CommonConstants.STATUS_DELETED)){
//                        objAgentLeaveDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
//                        sqlMap.executeUpdate("updateAgentLeaveDetailsTO", objAgentLeaveDetailsTO);
                    }
                }
            }
            
            if(agentDeletedLeaveMap != null){
                ArrayList leaveList = new ArrayList(agentDeletedLeaveMap.keySet());
                int length = leaveList.size();
                for (int i = 0; i < length; i++) {
                    AgentLeaveDetailsTO objAgentLeaveDetailsTO = (AgentLeaveDetailsTO) agentDeletedLeaveMap.get(leaveList.get(i));
                    objAgentLeaveDetailsTO.setStatusDt((Date)currDt.clone());
                    objAgentLeaveDetailsTO.setStatusBy(userID);
                    objAgentLeaveDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
                    sqlMap.executeUpdate("deleteAgentLeaveDetailsTO", objAgentLeaveDetailsTO);
                }
            }            
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
            if(objAgentTO != null){
                objAgentTO.setStatus(CommonConstants.STATUS_DELETED);
                objAgentTO.setStatusBy(userID);
                objAgentTO.setStatusDt(currDt);

                sqlMap.executeUpdate("deleteAgentTO", objAgentTO);

                objLogTO.setData(objAgentTO.toString());
                objLogTO.setPrimaryKey(objAgentTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            }
            if(objAgentLeaveDetailsTO != null){
                objAgentLeaveDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
                objAgentLeaveDetailsTO.setStatusDt((Date)currDt.clone());
                objAgentLeaveDetailsTO.setStatusBy(userID);
                sqlMap.executeUpdate("deleteAgentLeaveDetailsTO", objAgentTO);
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
            AgentDAO dao = new AgentDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("Agent Map Dao : " + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
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
             if (map.containsKey("AGENT_COMMISSION_SLAB")) {  //Added by Sreekrishnan               
                LinkedHashMap agentCommissionTableDetails = new LinkedHashMap();
                agentCommissionTableDetails = (LinkedHashMap) map.get("AGENT_COMMISSION_SLAB");
                ArrayList addList = new ArrayList(agentCommissionTableDetails.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    AgentCommisonSlabTO agentSlabTO = (AgentCommisonSlabTO) agentCommissionTableDetails.get(addList.get(i));  
                    if(!agentSlabTO.getStatus().equals(CommonConstants.STATUS_CREATED)){
                        HashMap updateMap = new HashMap();
                        System.out.println("agentSlabTO.getFromDt()$@$@$@$"+agentSlabTO.getFromDt());
                        System.out.println("DateUtil.addDays(agentSlabTO.getFromDt(),-1).getFromDt()$@$@$@$"+DateUtil.addDays(agentSlabTO.getFromDt(),-1));
                        updateMap.put("TO_DT",DateUtil.addDays(agentSlabTO.getFromDt(),-1));
                        sqlMap.executeUpdate("updateAgentCommissionSlabTo", updateMap);
                    }else
                        sqlMap.executeUpdate("insertAgentCommissionSlabTo", agentSlabTO); 
                }            
             }else{                 
                objAgentTO = (AgentTO) map.get("AgentTO");
                if(map.containsKey("AGENT_LEAVE_DETAILS")){
                    agentLeaveDetailsMap = (LinkedHashMap) map.get("AGENT_LEAVE_DETAILS");
                }
                if(map.containsKey("AGENT_DELETED_LEAVE_DETAILS")){
                    agentDeletedLeaveMap = (LinkedHashMap) map.get("AGENT_DELETED_LEAVE_DETAILS");
                }
                
                if(map.containsKey("AGENT_PROD_DETAILS")){
                    HashMap agentProdMap = (HashMap) map.get("AGENT_PROD_DETAILS");
                    ArrayList prodList = new ArrayList(agentProdMap.keySet());
                    sqlMap.executeUpdate("deleteAgentColProducts", objAgentTO);
                    int length = prodList.size();
                    for (int i = 0; i < length; i++) {
                        AgentColProdTO objAgentColProdTO = (AgentColProdTO) agentProdMap.get(prodList.get(i));
                        sqlMap.executeUpdate("insertAgentColProducts", objAgentColProdTO);
                    }
                }
                String command = "";
                if(objAgentTO != null){
                    command = objAgentTO.getCommand();
                }else{
                    command = CommonUtil.convertObjToStr(map.get("COMMAND"));
                }
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertData(objLogDAO, objLogTO);
                    if(objAgentTO != null && objAgentTO.getAgentId().length()>0){
                        returnMap.put("AGENT ID", objAgentTO.getAgentId());
                    }
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData(objLogDAO, objLogTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData(objLogDAO, objLogTO);
                } else if(map.containsKey("AUTHORIZE_LEAVE_MAP")) {
                    HashMap authorizeMap = new HashMap();
                    authorizeMap = (HashMap)map.get("AUTHORIZE_LEAVE_MAP");
                    System.out.println("authorizeMap : "+authorizeMap);
                    if(agentLeaveDetailsMap != null){
                        ArrayList leaveList = new ArrayList(agentLeaveDetailsMap.keySet());
                        int length = leaveList.size();
                        for (int i = 0; i < length; i++) {
                            AgentLeaveDetailsTO objAgentLeaveDetailsTO = (AgentLeaveDetailsTO) agentLeaveDetailsMap.get(leaveList.get(i));
                            HashMap leaveMap = new HashMap();
                            leaveMap.put("STATUS",authorizeMap.get("STATUS"));
                            leaveMap.put("USER_ID",userID);
                            leaveMap.put("AGENT ID",objAgentLeaveDetailsTO.getlAgentId());
                            leaveMap.put("SL_NO",objAgentLeaveDetailsTO.getSlNo());
                            sqlMap.executeUpdate("authorizeAgentLeaveDetails", leaveMap);
                        }
                    }                                
                }else{
                    throw new NoCommandException();
                }
            }
        }else {
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
            agComTo = (AgentCommisonTO) map.get("AgentCommisionTO");
            if (agComTo.getTransType().equals(CommonConstants.TX_TRANSFER)) {                
                generateSingleTransId = generateLinkID();
                HashMap TransMap;
                transferDAO = new TransferDAO();

                ArrayList transferList = new ArrayList();
                AgentTO agTo = new AgentTO();
                agTo = (AgentTO) map.get("AGENT_COMMISION_DETAILS");
                TxTransferTO txtTo = new TxTransferTO();
                if(map.containsKey("AGENT_AMOUNT_BASED_DETAILS")){
                opMap.put("ACT_NUM", agTo.getOperativeAcNo());
                opMap.put("AGENT_ID", agTo.getAgentId());
                List lst = ServerUtil.executeQuery("getACheadANDPRODID", opMap);//FOR 90% COM TO  OPRATIVE  ACCOUNT
                if (lst != null && lst.size() > 0) {
                    opMap = new HashMap();
                    txtTo = new TxTransferTO();
                    opMap = (HashMap) lst.get(0);
                    txtTo.setAcHdId(CommonUtil.convertObjToStr(opMap.get("AC_HD_ID")));
                    txtTo.setActNum(agTo.getOperativeAcNo());
                    txtTo.setProdId(CommonUtil.convertObjToStr(opMap.get("PROD_ID")));
                    txtTo.setAmount(agComTo.getComToOAacc()-agComTo.getTdsAmt());
                    txtTo.setInpCurr("INR");
                    txtTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    txtTo.setStatus("CREATED");
                    txtTo.setBranchId(_branchCode);
                    txtTo.setInitiatedBranch(_branchCode);
                    txtTo.setSingleTransId(generateSingleTransId);
                    txtTo.setInpAmount(agComTo.getComToOAacc()-agComTo.getTdsAmt());
                    txtTo.setParticulars("Agent Comm UpTo" + DateUtil.getStringDate(agComTo.getToDate()));
                    txtTo.setProdType("OA");
                    txtTo.setTransModType("OA");
                    txtTo.setStatusDt(currDt);
                    txtTo.setTransDt(ServerUtil.getCurrentDateWithTime(_branchCode));
                    txtTo.setTransMode("TRANSFER");
                    txtTo.setTransType(CommonConstants.CREDIT);
                    txtTo.setInitiatedBranch(_branchCode);
                    txtTo.setScreenName("AGENT_COMMISION_PRODUCT");
                    transferList.add(txtTo);
                    System.out.println("transferList&&&&&&&&& FIRST " + transferList);
                } else {
                    throw new TTException("Operative Account is not set for this Agent");
                }
                //TDS Transaction
                if(agComTo.getTdsAmt()>0){
                    HashMap tdsMap = new HashMap();
                    tdsMap.put("AGENT_ID", agTo.getAgentId());
                    tdsMap.put("PROD_ID",  CommonUtil.convertObjToStr(map.get("COMMISION_PROD_ID")));
                    List tdslst = ServerUtil.executeQuery("getTdsAcHd", tdsMap);//FOR 90% COM TO  OPRATIVE  ACCOUNT
                    if (tdslst != null && tdslst.size() > 0) {
                        tdsMap = new HashMap();
                        txtTo = new TxTransferTO();
                        tdsMap = (HashMap) tdslst.get(0);
                        txtTo.setAcHdId(CommonUtil.convertObjToStr(tdsMap.get("TDS_AC_HD")));                    
                        txtTo.setAmount(agComTo.getTdsAmt());
                        txtTo.setInpCurr("INR");
                        txtTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        txtTo.setStatus("CREATED");
                        txtTo.setBranchId(_branchCode);
                        txtTo.setInitiatedBranch(_branchCode);
                        txtTo.setSingleTransId(generateSingleTransId);
                        txtTo.setInpAmount(agComTo.getTdsAmt());
                        txtTo.setParticulars("By Agent commission Tds UpTo" + DateUtil.getStringDate(agComTo.getToDate()));
                        txtTo.setProdType(TransactionFactory.GL);
                        txtTo.setTransModType(TransactionFactory.GL);
                        txtTo.setStatusDt((Date)currDt.clone());
                        txtTo.setTransDt(ServerUtil.getCurrentDateWithTime(_branchCode));
                        txtTo.setTransMode("TRANSFER");
                        txtTo.setTransType(CommonConstants.CREDIT);
                        txtTo.setInitiatedBranch(_branchCode);
                        txtTo.setScreenName("AGENT_COMMISION_PRODUCT");
                        transferList.add(txtTo);
                        System.out.println("transferList&&&&&&&&& FIRST " + transferList);
                    } else {
                        throw new TTException("TDS Ac HEAD is not set for this Agent");
                    }
                }
//            opMap.put("AGENT_ID", agTo.getAgentId());
//            opMap.put("ACT_NUM", agTo.getDpacnum());
//            List tDlst = ServerUtil.executeQuery("getACheadANDPRODID", opMap); //FOR 10% COM TO  OPRATIVE  ACCOUNT
//            if (tDlst != null && tDlst.size() > 0) {
//                opMap = new HashMap();
//                opMap = (HashMap) tDlst.get(0);
//                txtTo = new TxTransferTO();
//                txtTo.setProdType(CommonUtil.convertObjToStr(opMap.get("DP_PROD_TYPE")));
//                txtTo.setProdId(CommonUtil.convertObjToStr(opMap.get("DP_PROD_ID")));
//                txtTo.setAcHdId(CommonUtil.convertObjToStr(opMap.get("AC_HD_ID")));
//                txtTo.setInpCurr("INR");
//                txtTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                txtTo.setInitiatedBranch(_branchCode);
//                txtTo.setStatus("CREATED");
//                txtTo.setBranchId(_branchCode);
//                txtTo.setInitiatedBranch(_branchCode);
//                txtTo.setParticulars("Agent Comm UpTo" + DateUtil.getStringDate(agComTo.getToDate()));
//                txtTo.setStatusDt(currDt);
//                txtTo.setTransDt(ServerUtil.getCurrentDateWithTime(_branchCode));
//                txtTo.setTransMode("TRANSFER");
//                txtTo.setTransType(CommonConstants.CREDIT);
//                txtTo.setActNum(agTo.getDpacnum());
//                txtTo.setAmount(agComTo.getComToTDacc());
//                txtTo.setInpAmount(agComTo.getComToTDacc());
//                transferList.add(txtTo);
//                System.out.println("transferList&&&&&&&&& SECOND " + transferList);
//            } else {
//                throw new TTException("InOperative Account is not set for this Agent");
//            }
                opMap.put("DAILY", "DAILY");
                opMap.put("AGENT_ID", agTo.getAgentId());
                opMap.put("PROD_ID",  CommonUtil.convertObjToStr(map.get("COMMISION_PROD_ID")));
                List comDRHdlst = sqlMap.executeQueryForList("getAgentCommAcHd", opMap);
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
                    txtTo.setTransModType("GL");
                    txtTo.setProdType("GL");
                    txtTo.setAcHdId(CommonUtil.convertObjToStr(opMap.get("COMM_COL_AC_HD_ID")));
                    txtTo.setAmount(CommonUtil.convertObjToDouble(agComTo.getCommisionForThePeriod()));
                    txtTo.setInpAmount(CommonUtil.convertObjToDouble(agComTo.getCommisionForThePeriod()));
                    txtTo.setTransType(CommonConstants.DEBIT);
                    txtTo.setInitiatedBranch(_branchCode);
                    txtTo.setSingleTransId(generateSingleTransId);
                    txtTo.setScreenName("AGENT_COMMISION_PRODUCT");
                    System.out.println("transferList&&&&&&&&&" + transferList);
                    transferList.add(txtTo);
                    System.out.println("transferList&&&&&&&&&THIRD" + transferList);
                } else {
                    throw new TTException("Commission  Account Head  is not set for this Agent");
                }
                }else if(map.containsKey("AGENT_NEW_ACCOUNT_BASED_DETAILS")){
                    opMap.put("ACT_NUM",agTo.getOperativeAcNo());
                    opMap.put("AGENT_ID",agTo.getAgentId());
                    List lst=ServerUtil.executeQuery("getACheadANDPRODID",opMap);//FOR 90% COM TO  OPRATIVE  ACCOUNT
                    if(lst != null && opMap.size()>0){
                        opMap = new HashMap();
                        txtTo = new TxTransferTO();
                        opMap = (HashMap)lst.get(0);
                        txtTo.setAcHdId(CommonUtil.convertObjToStr(opMap.get("AC_HD_ID")));
                        txtTo.setActNum(agTo.getOperativeAcNo());
                        txtTo.setProdId(CommonUtil.convertObjToStr(opMap.get("PROD_ID")));
                        txtTo.setAmount(CommonUtil.convertObjToDouble(map.get("TOTAL_NEW_ACCOUNT_AMT")));
                        txtTo.setInpCurr("INR");
                        txtTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        txtTo.setStatus(CommonConstants.STATUS_CREATED);
                        txtTo.setBranchId(_branchCode);
                        txtTo.setInitiatedBranch(_branchCode);
                        txtTo.setInpAmount(CommonUtil.convertObjToDouble(map.get("TOTAL_NEW_ACCOUNT_AMT")));
                        txtTo.setParticulars("Agent Comm UpTo" +  DateUtil.getStringDate(agComTo.getToDate()));
                        txtTo.setProdType(TransactionFactory.OPERATIVE);
                        txtTo.setStatusDt((Date)currDt.clone());
                        txtTo.setTransDt(ServerUtil.getCurrentDateWithTime(_branchCode));
                        txtTo.setTransMode("TRANSFER");
                        txtTo.setTransType(CommonConstants.CREDIT);
                        txtTo.setInitiatedBranch(_branchCode);
                        transferList.add(txtTo);
                        System.out.println("transferList&&&&&&&&& FIRST "+transferList);
                    }else{
                        throw new TTException("Operative Account is not set for this Agent");
                    }
                    opMap.put("DAILY","DAILY");
                    opMap.put("PROD_ID",  CommonUtil.convertObjToStr(map.get("COMMISION_PROD_ID")));
                    List comDRHdlst = sqlMap.executeQueryForList("getAgentCommAcHd",opMap);
                    if(comDRHdlst!=null && comDRHdlst.size()>0){
                        opMap = new HashMap();
                        opMap =(HashMap)comDRHdlst.get(0);
                        txtTo = new TxTransferTO();
                        txtTo.setInpCurr("INR");
                        txtTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        txtTo.setStatus(CommonConstants.STATUS_CREATED);
                        txtTo.setBranchId(_branchCode);
                        txtTo.setInitiatedBranch(_branchCode);
                        txtTo.setParticulars("Agent Comm UpTo" +  DateUtil.getStringDate(agComTo.getToDate()));
                        txtTo.setStatusDt((Date)currDt.clone());
                        txtTo.setTransDt(ServerUtil.getCurrentDateWithTime(_branchCode));
                        txtTo.setTransMode("TRANSFER");
                        txtTo.setProdType(TransactionFactory.GL);
                        txtTo.setAcHdId((String)opMap.get("COMM_COL_AC_HD_ID"));
                        txtTo.setAmount(CommonUtil.convertObjToDouble(map.get("TOTAL_NEW_ACCOUNT_AMT")));
                        txtTo.setInpAmount(CommonUtil.convertObjToDouble(map.get("TOTAL_NEW_ACCOUNT_AMT")));
                        txtTo.setTransType(CommonConstants.DEBIT);
                        txtTo.setInitiatedBranch(_branchCode);
                        System.out.println("transferList&&&&&&&&&"+transferList);
                        transferList.add(txtTo);
                        System.out.println("transferList&&&&&&&&&THIRD"+transferList);
                    }else{
                        throw new TTException("Commission  Account Head  is not set for this Agent");
                    }
                }else if(map.containsKey("FROM_AGENT_COMM_SCREEN")){
                    opMap.put("ACT_NUM",agTo.getOperativeAcNo());
                    opMap.put("AGENT_ID",agTo.getAgentId());
                    List lst=ServerUtil.executeQuery("getACheadANDPRODID",opMap);//FOR 90% COM TO  OPRATIVE  ACCOUNT
                    if(lst != null && opMap.size()>0){
                        opMap = new HashMap();
                        txtTo = new TxTransferTO();
                        opMap = (HashMap)lst.get(0);
                        txtTo.setAcHdId(CommonUtil.convertObjToStr(opMap.get("AC_HD_ID")));
                        txtTo.setActNum(agTo.getOperativeAcNo());
                        txtTo.setProdId(CommonUtil.convertObjToStr(opMap.get("PROD_ID")));                        
                        txtTo.setInpCurr("INR");
                        txtTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        txtTo.setStatus(CommonConstants.STATUS_CREATED);
                        txtTo.setBranchId(_branchCode);
                        txtTo.setInitiatedBranch(_branchCode);                       
                        txtTo.setParticulars("Agent Comm UpTo" +  DateUtil.getStringDate(agComTo.getToDate()));
                        txtTo.setProdType(TransactionFactory.OPERATIVE);
                        txtTo.setStatusDt((Date)currDt.clone());
                        txtTo.setTransDt(ServerUtil.getCurrentDateWithTime(_branchCode));
                        txtTo.setTransMode("TRANSFER");
                        txtTo.setTransType(CommonConstants.CREDIT);
                        txtTo.setInitiatedBranch(_branchCode);
                        txtTo.setInpAmount(CommonUtil.convertObjToDouble(agComTo.getCommisionForThePeriod()));
                        txtTo.setAmount(CommonUtil.convertObjToDouble(agComTo.getCommisionForThePeriod()));
                        txtTo.setTransModType("OA");
                        transferList.add(txtTo);
                        System.out.println("transferList&&&&&&&&& FROM_AGENT_COMM_SCREEN "+transferList);
                    }else{
                        throw new TTException("Operative Account is not set for this Agent");
                    }
                    opMap.put("DAILY","DAILY");
                    opMap.put("PROD_ID",  CommonUtil.convertObjToStr(map.get("COMMISION_PROD_ID")));
                    List comDRHdlst = sqlMap.executeQueryForList("getAgentCommAcHd",opMap);
                    if(comDRHdlst!=null && comDRHdlst.size()>0){
                        opMap = new HashMap();
                        opMap =(HashMap)comDRHdlst.get(0);
                        txtTo = new TxTransferTO();
                        txtTo.setInpCurr("INR");
                        txtTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        txtTo.setStatus(CommonConstants.STATUS_CREATED);
                        txtTo.setBranchId(_branchCode);
                        txtTo.setInitiatedBranch(_branchCode);
                        txtTo.setParticulars("Agent Comm UpTo" +  DateUtil.getStringDate(agComTo.getToDate()));
                        txtTo.setStatusDt((Date)currDt.clone());
                        txtTo.setTransDt(ServerUtil.getCurrentDateWithTime(_branchCode));
                        txtTo.setTransMode("TRANSFER");
                        txtTo.setProdType(TransactionFactory.GL);
                        txtTo.setAcHdId((String)opMap.get("COMM_COL_AC_HD_ID"));
                        txtTo.setInpAmount(CommonUtil.convertObjToDouble(agComTo.getCommisionForThePeriod()));
                        txtTo.setAmount(CommonUtil.convertObjToDouble(agComTo.getCommisionForThePeriod()));
                        txtTo.setTransType(CommonConstants.DEBIT);
                        txtTo.setInitiatedBranch(_branchCode);
                        txtTo.setTransModType("OA");
                        System.out.println("transferList&&&&&&&&&"+transferList);
                        transferList.add(txtTo);
                        System.out.println("transferList&&&&&&&&&THIRD"+transferList);
                    }else{
                        throw new TTException("Commission  Account Head  is not set for this Agent");
                    }
                }
                System.out.println("transferList&&&&&&&&&FROM_AGENT_COMM_SCREEN" + transferList);
                TransferTrans trans = new TransferTrans();
                //trans.doDebitCredit(transferList, _branchCode, false);
                trans.setInitiatedBranch(_branchCode);
                trans.doDebitCredit(transferList, _branchCode, true);
                returnMap = new HashMap();
                returnMap.put("SINGLE_TANS_ID", generateSingleTransId);
                returnMap.put("TRANSFER", "TRANSFER");
                //transferDAO
            } else if (agComTo.getTransType().equals(CommonConstants.TX_CASH)) {                
                CashTransactionTO objCashTO = new CashTransactionTO();
                HashMap cashMap;
                CashTransactionDAO cashDao;
                CashTransactionTO cashTO;
                cashDao = new CashTransactionDAO();
                cashTO = new CashTransactionTO();
                cashMap = new HashMap();
                ArrayList cashList = new ArrayList();
                AgentTO agTo = new AgentTO();
                agTo = (AgentTO) map.get("AGENT_COMMISION_DETAILS");
                TxTransferTO txtTo = new TxTransferTO();
                opMap.put("ACT_NUM", agTo.getOperativeAcNo());
                opMap.put("AGENT_ID", agTo.getAgentId());
                opMap.put("DAILY", "DAILY");
                opMap.put("PROD_ID",  CommonUtil.convertObjToStr(map.get("COMMISION_PROD_ID")));
                List comDRHdlst = sqlMap.executeQueryForList("getAgentCommAcHd", opMap);
                if (comDRHdlst != null && comDRHdlst.size() > 0) {
                    opMap = new HashMap();
                    opMap = (HashMap) comDRHdlst.get(0);
                    cashTO = new CashTransactionTO();
                    if(map.containsKey("AGENT_NEW_ACCOUNT_BASED_DETAILS")){
                        cashTO.setAmount(CommonUtil.convertObjToDouble(map.get("TOTAL_NEW_ACCOUNT_AMT")));
                        cashTO.setInpAmount(CommonUtil.convertObjToDouble(map.get("TOTAL_NEW_ACCOUNT_AMT")));
                        cashTO.setAmount(CommonUtil.convertObjToDouble(map.get("TOTAL_NEW_ACCOUNT_AMT")));
                    }else{                        
                        cashTO.setAmount(CommonUtil.convertObjToDouble(agComTo.getCommisionForThePeriod())-agComTo.getTdsAmt());
                        cashTO.setInpAmount(CommonUtil.convertObjToDouble(agComTo.getCommisionForThePeriod()));
                        cashTO.setAmount(CommonUtil.convertObjToDouble(agComTo.getCommisionForThePeriod()));
                    }
                    System.out.println("agComTo :: " + agComTo);
                    cashTO.setAcHdId(CommonUtil.convertObjToStr(opMap.get("COMM_COL_AC_HD_ID")));
                    cashTO.setProdType("GL");
                    cashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    cashTO.setStatus("CREATED");
                    cashTO.setStatusDt(currDt);
                    cashTO.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    cashTO.setTransType(CommonConstants.DEBIT);
                    cashTO.setBranchId(_branchCode);
                    cashTO.setInitChannType(CommonConstants.CASHIER);
                    cashTO.setInitTransId("CASHIER");
                    cashTO.setInpCurr("INR");
                    cashTO.setLinkBatchId(CommonUtil.convertObjToStr(agComTo.getAgentId()));
                    cashTO.setInitiatedBranch(_branchCode);
                    cashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                    cashTO.setTransModType("GL");
                    cashTO.setParticulars("Agent Comm UpTo" + DateUtil.getStringDate(agComTo.getToDate()));
                    cashTO.setTransDt(ServerUtil.getCurrentDateWithTime(_branchCode));
                    cashList.add(cashTO);
                    //Tds transaction
                    if(agComTo.getTdsAmt()>0){
                        HashMap tdsMap = new HashMap();
                        tdsMap.put("AGENT_ID", agTo.getAgentId());
                        tdsMap.put("PROD_ID",  CommonUtil.convertObjToStr(map.get("COMMISION_PROD_ID")));
                        List tdslst = ServerUtil.executeQuery("getTdsAcHd", tdsMap);//FOR 90% COM TO  OPRATIVE  ACCOUNT
                        if (tdslst != null && tdslst.size() > 0) {
                            tdsMap = new HashMap();
                            tdsMap = (HashMap) tdslst.get(0);
                            cashTO = new CashTransactionTO();
                            cashTO.setAmount(agComTo.getTdsAmt());
                            cashTO.setAcHdId(CommonUtil.convertObjToStr(tdsMap.get("TDS_AC_HD")));
                            cashTO.setProdType(TransactionFactory.GL);
                            cashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                            cashTO.setStatus("CREATED");
                            cashTO.setStatusDt((Date)currDt.clone());
                            cashTO.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            cashTO.setTransType(CommonConstants.CREDIT);
                            cashTO.setBranchId(_branchCode);
                            cashTO.setInitChannType(CommonConstants.CASHIER);
                            cashTO.setInitTransId("CASHIER");
                            cashTO.setInpAmount(agComTo.getTdsAmt());
                            cashTO.setAmount(agComTo.getTdsAmt());
                            cashTO.setInpCurr("INR");
                            cashTO.setLinkBatchId(CommonUtil.convertObjToStr(agComTo.getAgentId()));
                            cashTO.setInitiatedBranch(_branchCode);
                            cashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                            cashTO.setTransModType(TransactionFactory.GL);
                            cashTO.setParticulars("Agent Comm  Tds UpTo" + DateUtil.getStringDate(agComTo.getToDate()));
                            cashTO.setTransDt(ServerUtil.getCurrentDateWithTime(_branchCode));
                            cashTO.setScreenName("AGENT_COMMISION_PRODUCT");
                            cashList.add(cashTO);
                        }
                    }
                    cashMap.put("DAILYDEPOSITTRANSTO", cashList);
                    cashMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                    cashMap.put("INITIATED_BRANCH", _branchCode);
                    cashMap.put(CommonConstants.BRANCH_ID, _branchCode);
                    cashMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                    cashMap.put(CommonConstants.MODULE, "Transaction");
                    cashMap.put("MODE", "MODE");
                    HashMap cashTransMap = cashDao.execute(cashMap, false);
                    returnMap = new HashMap();
                    returnMap.put("SINGLE_TANS_ID", cashTransMap.get(("SINGLE_TRANS_ID")));
                    returnMap.put("CASH", "CASH");
                    HashMap map1 = new HashMap();
                    String status = CommonConstants.STATUS_AUTHORIZED;
                    CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                    ArrayList arrList = new ArrayList();
                    HashMap singleAuthorizeMap = new HashMap();
                    singleAuthorizeMap.put("STATUS", status);
                    singleAuthorizeMap.put("TRANS_ID", cashTransMap.get("TRANS_ID"));
                    singleAuthorizeMap.put("USER_ID", map.get("USER_ID"));
                    arrList.add(singleAuthorizeMap);
                    map1.put("USER_ID", CommonUtil.convertObjToStr(map.get("USER_ID")));
                    map1.put("SELECTED_BRANCH_ID", _branchCode);
                    map1.put("BRANCH_CODE", _branchCode);
                    map1.put("MODULE", "Transaction");
                    HashMap dataMap = new HashMap();
                    dataMap.put(CommonConstants.AUTHORIZESTATUS, status);
                    dataMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                    dataMap.put("DAILY", "DAILY");
                    map1.put(CommonConstants.AUTHORIZEMAP, dataMap);
                    returnMap = cashTransactionDAO.execute(map1, false);
                    returnMap = new HashMap();
                    returnMap.put("SINGLE_TANS_ID", generateSingleTransId);
                    returnMap.put("CASH", "CASH");
                    cashTO = null;
                    cashDao = null;
                    cashMap = null;
                }
            }
            opMap = new HashMap();
            Date agCompaidDt = agComTo.getToDate();
//            agCompaidDt = DateUtil.addDays(agCompaidDt, 1);
            opMap.put("LAST_COM_PAID_DT", agCompaidDt);
            opMap.put("AGENT_ID", agComTo.getAgentId());
            opMap.put("PROD_ID",  CommonUtil.convertObjToStr(map.get("COMMISION_PROD_ID")));
            HashMap monthlyScheduleMap = new HashMap();
            if(map.containsKey("AGENT_NEW_ACCOUNT_BASED_DETAILS")){
                sqlMap.executeUpdate("updateLastIntroComPaidDate", opMap);
                sqlMap.executeUpdate("updateLastIntroComPaidDateForAgentProducts", opMap);
                monthlyScheduleMap.put("TYPE_OF_COMMISION", "NEW_ACCT_BASED");
                monthlyScheduleMap.put("COMMISION_TO_NEW_AC",map.get("TOTAL_NEW_ACCOUNT_AMT"));
            }else{
                sqlMap.executeUpdate("updateLastComPaidDate", opMap);
                //Added by sreekrishnan
                sqlMap.executeUpdate("updateLastComPaidDateForAgentProducts", opMap);                
                monthlyScheduleMap.put("TYPE_OF_COMMISION", "AMOUNT_BASED");
                monthlyScheduleMap.put("COMMISION_TO_NEW_AC",new Double(0));
            }
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
     private String generateLinkID() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "GENERATE_LINK_ID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//for trans_batch_id resetting, branch code
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        String batchID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        return batchID;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objAgentTO = null;
    }
    
    private String getLeaveID() throws Exception {
        //  final IDGenerateDAO dao = new IDGenerateDAO();
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "AGENT_LEAVE_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }
}
