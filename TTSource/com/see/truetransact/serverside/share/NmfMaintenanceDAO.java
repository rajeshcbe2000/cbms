/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareResolutionDAO.java
 *
 * Created on Thu Apr 28 12:59:32 IST 2005
 */
package com.see.truetransact.serverside.share;

import com.see.truetransact.serverside.share.shareresolution.*;
import java.util.List;
import java.util.ArrayList;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.iie.tools.workflow.util.ClientUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import java.util.HashMap;
import java.util.Date;
import org.apache.log4j.Logger;
import com.see.truetransact.transferobject.share.NmfMaintenanceTO;
import com.see.truetransact.transferobject.share.ShareAcctDetailsTO;
import com.see.truetransact.transferobject.share.shareresolution.ShareResolutionTO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.commonutil.LocaleConstants;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.transferobject.share.ShareAccInfoTO;
import java.util.*;

/**
 * ShareResolution DAO.
 *
 */
public class NmfMaintenanceDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private NmfMaintenanceTO objTO;
    private TransactionTO objTransactionTO;
    HashMap shareAcctDet;
    private Date currDt = null;
    private final String INT_GET_FROM = "INT_GET_FROM";
    private final String LIMIT = "LIMIT";
    private final String PROD_ID = "PROD_ID";
    private final String SECURITY_DETAILS = "SECURITY_DETAILS";
    private final static Logger log = Logger.getLogger(NmfMaintenanceDAO.class);
    ArrayList listShareAcctDet;
    String branchId = "";
    String returnApplNo = "";
    String ShareAcctNo = "";
    HashMap execReturnMap = new HashMap();
    String userid = "";
    private TransactionDAO transactionDAO = null;

    /**
     * Creates a new instance of ShareResolutionDAO
     */
    public NmfMaintenanceDAO() throws ServiceLocatorException {

        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        String where = CommonUtil.convertObjToStr(map.get("NOMINAL_MEM_NO"));
        map.put(CommonConstants.MAP_WHERE, where);
        List list = transactionDAO.getData(map);
        returnMap.put("TransactionTO", list);
        return returnMap;
    }

    private void executeTransactionPart(HashMap map) throws Exception {
        try {
            //transDetailMap.put("ACCT_NUM", acct_No);
            HashMap txMap;
            HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getNmfAcHead", objTO.getNominalMemNo());
            LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
            System.out.println("@@TransactionDetailsMap" + TransactionDetailsMap);
            if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                {
                    for (int J = 0; J < allowedTransDetailsTO.size(); J++) {
                        objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                        if (objTransactionTO.getTransType().equals("TRANSFER")) {
                            txMap = new HashMap();
                            ArrayList transferList = new ArrayList();
                            TransferTrans objTransferTrans = new TransferTrans();
                            objTransferTrans.setInitiatedBranch(_branchCode);
                            txMap.put(TransferTrans.PARTICULARS, objTO.getNominalMemNo());
                            txMap.put(CommonConstants.USER_ID, objTO.getCreatedBy());
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);//_branchCode
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            if (objTransactionTO.getProductType().equals("GL")) {
                                txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                            } else {
                                txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                            }
                            txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getNominalMemFee()));

                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("NMF_HD"));
                            txMap.put("AUTHORIZEREMARKS", "NMF_HD");
                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getNominalMemFee()));
                            objTransferTrans.setLinkBatchId(objTO.getNominalMemNo());
                            objTransferTrans.doDebitCredit(transferList, _branchCode, false);

                        } else if (objTransactionTO.getTransType().equals("CASH")) {
                            insertCashTransaction(map);
                        }
                        objTransactionTO.setBatchId(objTO.getNominalMemNo());
                        objTransactionTO.setBatchDt(currDt);
                        objTransactionTO.setTransId(objTO.getNominalMemNo());
                        objTransactionTO.setStatus("CREATED");
                        objTransactionTO.setBranchId(_branchCode);
                        sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);
                        getTransDetails(objTO.getNominalMemNo());
                    }
                }
            }

        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    public void insertCashTransaction(HashMap map) throws Exception, Exception {
        double transAmt;
        System.out.println("the map is" + map);
        TransactionTO transTO = new TransactionTO();
        HashMap tranMap = new HashMap();
        ArrayList cashList = new ArrayList();

        HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getNmfAcHead", objTO.getNominalMemNo());
        if (CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue() > 0) {
            transAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
            HashMap txMap = new HashMap();
            txMap.put(CommonConstants.BRANCH_ID, _branchCode);
            txMap.put(CommonConstants.PRODUCT_ID, TransactionFactory.GL);
            txMap.put(CommonConstants.USER_ID, objTO.getCreatedBy());
            System.out.println("the head is" + (String) acHeads.get("NMF_HD"));
            // System.out.println("the linkbatch id is"+objTransferTrans.getLinkBatchId());
            txMap.put(CommonConstants.AC_HD_ID, acHeads.get("NMF_HD"));
            txMap.put("AUTHORIZEREMARKS", "NMF_HD");
            txMap.put("AMOUNT", new Double(transAmt));
            txMap.put("LINK_BATCH_ID", objTO.getNominalMemNo());
            cashList.add(setCashTransaction(txMap, objTO));
            tranMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
            tranMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
            tranMap.put(CommonConstants.IP_ADDR, CommonUtil.convertObjToStr(map.get("IP_ADDR")));
            tranMap.put(CommonConstants.MODULE, CommonUtil.convertObjToStr(map.get("MODULE")));
            tranMap.put(CommonConstants.SCREEN, CommonUtil.convertObjToStr(map.get("SCREEN")));
            tranMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
            tranMap.put("DAILYDEPOSITTRANSTO", cashList);
            CashTransactionDAO cashDao;
            cashDao = new CashTransactionDAO();
            System.out.println("the map is" + tranMap);
            tranMap = cashDao.execute(tranMap, false);
            cashDao = null;
            tranMap = null;
        }
    }

    public CashTransactionTO setCashTransaction(HashMap cashMap, NmfMaintenanceTO objNmfMaintenanceTO) {
        log.info("In setCashTransaction()");
        Date curDate = (Date) currDt.clone();
        final CashTransactionTO objCashTransactionTO = new CashTransactionTO();
        try {
            objCashTransactionTO.setAcHdId(CommonUtil.convertObjToStr(cashMap.get(CommonConstants.AC_HD_ID)));
            objCashTransactionTO.setProdType(TransactionFactory.GL);
            objCashTransactionTO.setInpAmount(CommonUtil.convertObjToDouble(cashMap.get("AMOUNT")));
            objCashTransactionTO.setAmount(CommonUtil.convertObjToDouble(cashMap.get("AMOUNT")));
            objCashTransactionTO.setTransType(CommonConstants.CREDIT);
            objCashTransactionTO.setBranchId(objNmfMaintenanceTO.getBranchCode());
            objCashTransactionTO.setStatusBy(CommonUtil.convertObjToStr(cashMap.get(CommonConstants.USER_ID)));
            //            objCashTransactionTO.setInstrumentNo1(cashTo.getInstrumentNo1());
            objCashTransactionTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            objCashTransactionTO.setInitTransId(CommonUtil.convertObjToStr(cashMap.get(CommonConstants.USER_ID)));
            objCashTransactionTO.setInitChannType("CASHIER");
            objCashTransactionTO.setParticulars("By " + CommonUtil.convertObjToStr(cashMap.get("LINK_BATCH_ID")));
            objCashTransactionTO.setInitiatedBranch(_branchCode);
            objCashTransactionTO.setLinkBatchId(CommonUtil.convertObjToStr(cashMap.get("LINK_BATCH_ID")));
            objCashTransactionTO.setAuthorizeRemarks(CommonUtil.convertObjToStr(cashMap.get("AUTHORIZEREMARKS")));
            objCashTransactionTO.setCommand(CommonConstants.TOSTATUS_INSERT);
            objCashTransactionTO.setLoanHierarchy(CommonUtil.convertObjToStr(cashMap.get("HIERARCHY")));
            System.out.println("objCashTransactionTO:" + objCashTransactionTO);
        } catch (Exception e) {
            log.info("Error In setInwardClearing()");
            e.printStackTrace();
        }
        return objCashTransactionTO;
    }

    //--- Generate ShareNo.From and To.
    private void getNominal_Mem_No() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "NOMINAL_MEM_NO");
        String nominalMemNo = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        System.out.println("the share application no is" + nominalMemNo);
        objTO.setNominalMemNo(nominalMemNo);
        returnApplNo = nominalMemNo;
        System.out.println("the returnApplN is" + returnApplNo);
    }

    public static void main(String str[]) {
        try {
            NmfMaintenanceDAO dao = new NmfMaintenanceDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("map:" + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        LogDAO objLogDAO = new LogDAO();
        execReturnMap = new HashMap();
        System.out.println("map:" + map);
        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        branchId = objLogTO.getBranchId();
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));

        currDt = ServerUtil.getCurrentDate(_branchCode);

        userid = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));

        if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            System.out.println("map:" + map);

            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            authMap.put(CommonConstants.BRANCH_ID, (String) map.get(CommonConstants.BRANCH_ID));
            System.out.println("authMap:" + authMap);
            if (authMap != null) {
                authorize(authMap);
            }
        } else {
            objTO = (NmfMaintenanceTO) map.get("NmfMaintenanceTO");
            final String command = objTO.getCommand();
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                objTO.setStatus("CREATED");
                objTO.setBranchCode(_branchCode);
                getNominal_Mem_No();
                objTO.setNominalMemNo(returnApplNo);
                insertData();
                executeTransactionPart(map);
                execReturnMap.put(CommonConstants.TRANS_ID, returnApplNo);
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                objTO.setStatus("UPDATED");
                updateData();
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                objTO.setStatus("DELETED");
                deleteData();
            } else {
                throw new NoCommandException();
            }

        }
        destroyObjects();
        return execReturnMap;
    }

    private void authorize(HashMap authMap) {
        try {
            String status = (String) authMap.get(CommonConstants.AUTHORIZESTATUS);
            ArrayList selectedList = (ArrayList) authMap.get(CommonConstants.AUTHORIZEDATA);
            HashMap dataMap = new HashMap();
            for (int i = 0; i < selectedList.size(); i++) {
                System.out.println("selected list is" + selectedList.size());
                dataMap = (HashMap) selectedList.get(i);
                System.out.println("dataMap:" + dataMap);
                String linkBatchId = CommonUtil.convertObjToStr(dataMap.get("NOMINAL_MEM_NO"));
                String CUST_ID = CommonUtil.convertObjToStr(dataMap.get("CUST_ID"));
                System.out.println("linkBatchId" + linkBatchId);
                dataMap.put("AUTHORIZE_STATUS", status);
                dataMap.put("AUTHORIZE_DT", currDt);
                dataMap.put("AUTHORIZE_BY", userid);
                sqlMap.executeUpdate("authorizeNMF", dataMap);
                if (status.equals("AUTHORIZED")) {
                    dataMap.put("MEMBERSHIP_NO", linkBatchId);
                    dataMap.put("MEMBERSHIP_CLASS", "NOMINAL");
                    sqlMap.executeUpdate("getUpdateCustomer", dataMap);
                }
                HashMap cashAuthMap = new HashMap();
                cashAuthMap.put(CommonConstants.BRANCH_ID, branchId);
                cashAuthMap.put(CommonConstants.USER_ID, userid);
                cashAuthMap.put("DAILY", "DAILY");
                System.out.println("authMap:" + authMap);
                System.out.println("cashAuthMap:" + cashAuthMap);

                TransactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getTransDetails(String batchId) throws Exception {
        System.out.println("batch id is" + batchId);
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        getTransMap.put("AUTHORIZE_STATUS", "AUTHORIZE_STATUS");
        //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
        execReturnMap = new HashMap();
        List transList = (List) sqlMap.executeQueryForList("getTransferDetails", getTransMap);
        if (transList != null && transList.size() > 0) {
            execReturnMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetails", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            execReturnMap.put("CASH_TRANS_LIST", cashList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    private void insertData() {
        try {
            sqlMap.executeUpdate("insertNmfTO", objTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateData() {
        try {
            sqlMap.executeUpdate("updateNmfTO", objTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteData() {
        try {
            HashMap hmap = new HashMap();
            ArrayList transferList = new ArrayList();
            hmap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(objTO.getNominalMemNo()));
            List lst = sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", hmap);
            List cLst1 = sqlMap.executeQueryForList("getSelectShareCashTransactionTO", CommonUtil.convertObjToStr(objTO.getNominalMemNo()));
            TxTransferTO txTransferTO = null;
            if (lst != null && lst.size() > 0) {
                for (int j = 0; j < lst.size(); j++) {
                    txTransferTO = (TxTransferTO) lst.get(j);
                    txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                    txTransferTO.setStatusDt(currDt);
                }
                transferList.add(txTransferTO);
                TransferTrans transferTrans = new TransferTrans();
//                    transferTrans.setOldAmount(oldAmountMap);
                transferTrans.setInitiatedBranch(_branchCode);
                transferTrans.doDebitCredit(transferList, _branchCode, false, "DELETED");
                transferTrans = null;
            }
            if (cLst1 != null && cLst1.size() > 0) {
                for (int l = 0; l < cLst1.size(); l++) {
                    CashTransactionTO txTransferTO1 = null;
                    txTransferTO1 = (CashTransactionTO) cLst1.get(l);

                    txTransferTO1.setCommand("DELETE");
                    txTransferTO1.setStatus(CommonConstants.STATUS_DELETED);
                    txTransferTO1.setStatusDt(currDt);
                    transferList.add(txTransferTO1);
                }

                hmap.put(CommonConstants.PRODUCT_ID, TransactionFactory.GL);
                hmap.put("PRODUCTTYPE", TransactionFactory.GL);
                hmap.put("DAILYDEPOSITTRANSTO", transferList);
                hmap.put("BRANCH_CODE", _branchCode);
                System.out.println("@@@@transferList" + transferList);
                CashTransactionDAO cashTransDAO = new CashTransactionDAO();
                cashTransDAO.execute(hmap, false);
            }
            sqlMap.executeUpdate("deleteNmfTO", objTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        System.out.println("obj&&&" + obj);
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;

    }
}
