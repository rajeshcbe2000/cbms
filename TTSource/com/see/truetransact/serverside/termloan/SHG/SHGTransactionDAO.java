/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * SHGTransactionDAO.java
 *
 * Created on June 18, 2011, 4:14 PM
 */
package com.see.truetransact.serverside.termloan.SHG;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.common.nominee.NomineeDAO;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.operativeaccount.AccountTO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;


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
public class SHGTransactionDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private TransactionDAO transactionDAO = null;
    TransferDAO transferDAO = new TransferDAO();
    NomineeDAO objNomineeDAO = new NomineeDAO();
    private AccountTO accountTO;
    private Date currDt = null;
    final String SCREEN = "TD";
    public List shgList = null;
    private HashMap TermLoanCloseCharge = new HashMap();
    private HashMap execReturnMap = null;

    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public SHGTransactionDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("########" + map);
        HashMap transMap = new HashMap();
        String where = (String) map.get("SHG_TRANS_ID");
        List list = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTO", where);
        if (list != null && list.size() > 0) {
            transMap.put("TRANSACTION_LIST", list);
            list = null;
        }
        return transMap;
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        System.out.println("Map in DAO: " + map);
        // Log DAO
        LogDAO objLogDAO = new LogDAO();
        execReturnMap = new HashMap();
        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        System.out.println("$#$#$#$#$ : Command : " + command);
        if (map.containsKey("SHG_TABLE_DATA")) {
            try {
                sqlMap.startTransaction();
                objLogTO.setStatus(command);
                shgList = (List) map.get("SHG_TABLE_DATA");
                System.out.println("@##$#$% shgList #### :" + shgList);
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertData(map, objLogDAO, objLogTO);
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData(map, objLogDAO, objLogTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData(map, objLogDAO, objLogTO);
                } else if (map.containsKey("AUTHORIZEMAP")) {
                    authorize(map);
                } else {
                    throw new NoCommandException();
                }
                sqlMap.commitTransaction();
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw e;
            }
            objLogDAO = null;
            objLogTO = null;
            destroyObjects();

        }
        return execReturnMap;
    }

    private String getSHGTransId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "SHG_TRANS_ID");
        String shgTransId = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return shgTransId;
    }

    private void insertData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            if (shgList != null && shgList.size() > 0) {
                String shgTransID = "";
                shgTransID = getSHGTransId();
                execReturnMap.put("SHG_TRANS_ID", shgTransID);
                String group_id = "";
                group_id = CommonUtil.convertObjToStr(map.get("GROUP_ID"));
                TransactionTO transactionTO = new TransactionTO();
                HashMap txMap = new HashMap();
                HashMap transMap = new HashMap();
                HashMap debitMap = new HashMap();
                double totalExcessTransAmt = 0.0;
                String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
                if (map.containsKey("TransactionTO")) {
                    HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                    if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                        allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                    }
                    transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                    String linkBatchId = CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo());
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")) {
                        debitMap.put("ACT_NUM", linkBatchId);
                        List lst = sqlMap.executeQueryForList("getAccNoProdIdDet", debitMap);
                        if (lst != null && lst.size() > 0) {
                            debitMap = (HashMap) lst.get(0);
                        }
                    }
                    if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL")) {
                        debitMap.put("prodId", transactionTO.getProductId());
                        List lst = sqlMap.executeQueryForList("TermLoan.getProdHead", debitMap);
                        if (lst != null && lst.size() > 0) {
                            debitMap = (HashMap) lst.get(0);
                        }
                    }
                    totalExcessTransAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                    System.out.println("-------------------> totalExcessTransAmt : " + totalExcessTransAmt);
                    if (transactionTO.getTransType().equals("TRANSFER")) {
                        for (int i = 0; i < shgList.size(); i++) {
                            ArrayList transferList = new ArrayList();
                            TransferTrans transferTrans = new TransferTrans();
                            transferTrans.setInitiatedBranch(BRANCH_ID);
                            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                            transactionDAO.setInitiatedBranch(_branchCode);
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            HashMap tansactionMap = new HashMap();
                            TxTransferTO transferTo = new TxTransferTO();
                            ArrayList TxTransferTO = new ArrayList();
                            HashMap dataMap = new HashMap();
                            double paymentAmt = 0.0;
                            double interestAmt = 0.0;
                            double penalAmt = 0.0;
                            String loanAccNo = "";
                            dataMap = (HashMap) shgList.get(i);
                            paymentAmt = CommonUtil.convertObjToDouble(dataMap.get("PAYMENT")).doubleValue();
                            interestAmt = CommonUtil.convertObjToDouble(dataMap.get("INT_DUE")).doubleValue();
                            penalAmt = CommonUtil.convertObjToDouble(dataMap.get("PENAL")).doubleValue();
                            loanAccNo = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            if (paymentAmt > 0) {           // Debit Insert Start
                                totalExcessTransAmt -= paymentAmt;
                                txMap = new HashMap();
                                System.out.println("Transfer Started debit : ");
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                System.out.println("$#$$$#$#$# debitMap " + debitMap);
                                if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL") && transactionTO.getProductId().equals("")) {
                                    System.out.println("$#$$$#$#$# Prod Type GL " + transactionTO.getDebitAcctNo());
                                    txMap.put(TransferTrans.DR_AC_HD, (String) transactionTO.getDebitAcctNo());
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, linkBatchId + "-" + ":SHG_PAYMENT");
                                } else if (!transactionTO.getProductType().equals("")) {
                                    System.out.println("$#$$$#$#$# Prod Type Not GL " + transactionTO.getDebitAcctNo());
                                    txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HEAD"));
                                    txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                                    txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                                    txMap.put(TransferTrans.DR_PROD_TYPE, transactionTO.getProductType());
                                    txMap.put(TransferTrans.PARTICULARS, linkBatchId + "-" + ":SHG_PAYMENT");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));

                                }
                                txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                                System.out.println("txMap Debit : " + txMap + "paymentAmt :" + paymentAmt);
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, paymentAmt);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setTransDt(currDt);
                                transferTo.setBranchId(BRANCH_ID);
                                transferTo.setInitiatedBranch(BRANCH_ID);
//                                transferTo.setStatusBy(CommonConstants.TTSYSTEM);
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setAuthorizeRemarks("DP");
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                                TxTransferTO.add(transferTo);
                            }                                                       // Debit Acc INSERT End

                            HashMap applicationMap = new HashMap();                // Crdit Acc INSERT Start
                            applicationMap.put("PROD_ID", dataMap.get("PROD_ID"));
                            List lst = sqlMap.executeQueryForList("getAccountHeadProdTL", applicationMap);    // Acc Head
                            if (lst != null && lst.size() > 0) {
                                applicationMap = (HashMap) lst.get(0);
                            }
                            TermLoanCloseCharge = new HashMap();
                            TermLoanCloseCharge.put("BRANCH_CODE", BRANCH_ID);
                            List chargeList = sqlMap.executeQueryForList("getChargeDetails", dataMap);
                            if (chargeList != null && chargeList.size() > 0) {
                                HashMap otherChargesMap = new HashMap();
                                for (int j = 0; j < chargeList.size(); j++) {
                                    HashMap chargeMap = (HashMap) chargeList.get(j);
                                    double chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                                    if (chargeMap.get("CHARGE_TYPE").equals("POSTAGE CHARGES") && chargeAmt > 0) {
                                        TermLoanCloseCharge.put("POSTAGE CHARGES", chargeMap.get("CHARGE_AMT"));
                                    } else if (chargeMap.get("CHARGE_TYPE").equals("MISCELLANEOUS CHARGES") && chargeAmt > 0) {
                                        TermLoanCloseCharge.put("MISCELLANEOUS CHARGES", chargeMap.get("CHARGE_AMT"));
                                    } else if (chargeMap.get("CHARGE_TYPE").equals("LEGAL CHARGES") && chargeAmt > 0) {
                                        TermLoanCloseCharge.put("LEGAL CHARGES", chargeMap.get("CHARGE_AMT"));
                                    } else if (chargeMap.get("CHARGE_TYPE").equals("INSURANCE CHARGES") && chargeAmt > 0) {
                                        TermLoanCloseCharge.put("INSURANCE CHARGES", chargeMap.get("CHARGE_AMT"));
                                    } else if (chargeMap.get("CHARGE_TYPE").equals("EXECUTION DECREE CHARGES") && chargeAmt > 0) {
                                        TermLoanCloseCharge.put("EXECUTION DECREE CHARGES", chargeMap.get("CHARGE_AMT"));
                                    } else if (chargeMap.get("CHARGE_TYPE").equals("ARBITRARY CHARGES") && chargeAmt > 0) {
                                        TermLoanCloseCharge.put("ARBITRARY CHARGES", chargeMap.get("CHARGE_AMT"));
                                    } else if (chargeMap.get("CHARGE_TYPE").equals("ADVERTISE CHARGES") && chargeAmt > 0) {
                                        TermLoanCloseCharge.put("ADVERTISE CHARGES", chargeMap.get("CHARGE_AMT"));
                                    } else {
                                        otherChargesMap.put(chargeMap.get("CHARGE_TYPE"), chargeMap.get("CHARGE_AMT"));
                                    }
                                }
                                TermLoanCloseCharge.put("OTHER_CHARGES", otherChargesMap);
                            }
                            if (interestAmt > 0) {
                                TermLoanCloseCharge.put("CURR_MONTH_INT", String.valueOf(interestAmt));
                            }
                            if (penalAmt > 0) {
                                TermLoanCloseCharge.put("PENAL_INT", String.valueOf(penalAmt));
                            }

                            txMap = new HashMap();
                            if (paymentAmt > 0.0) {
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_ACT_NUM, loanAccNo);
                                txMap.put(TransferTrans.CR_PROD_ID, dataMap.get("PROD_ID"));
                                txMap.put(TransferTrans.CR_AC_HD, (String) applicationMap.get("AC_HEAD"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
                                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                txMap.put(TransferTrans.PARTICULARS, loanAccNo + "-" + ":SHG_PAYMENT");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                System.out.println("txMap  : " + txMap + "paymentAmt :" + paymentAmt);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, paymentAmt);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setTransDt(currDt);
                                transferTo.setInitiatedBranch(BRANCH_ID);
//                                transferTo.setStatusBy(CommonConstants.TTSYSTEM);
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setLinkBatchId(loanAccNo);
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(loanAccNo));
                                transactionTO.setChequeNo("SERVICE_TAX");
                                TxTransferTO.add(transferTo);
                            }
                            transferDAO = new TransferDAO();
                            tansactionMap.put("ALL_AMOUNT", TermLoanCloseCharge);
                            tansactionMap.put("TxTransferTO", TxTransferTO);
                            tansactionMap.put("MODE", map.get("COMMAND"));
                            tansactionMap.put("COMMAND", map.get("COMMAND"));
                            tansactionMap.put(CommonConstants.BRANCH_ID, BRANCH_ID);
                            System.out.println("################ tansactionMap :" + tansactionMap);
                            transMap = transferDAO.execute(tansactionMap, false);
                        }
                        System.out.println("-------------------> totalExcessTransAmt : " + totalExcessTransAmt);
                        if (totalExcessTransAmt > 0) {
                            ArrayList transferList = new ArrayList();
                            TransferTrans transferTrans = new TransferTrans();
                            transferTrans.setInitiatedBranch(BRANCH_ID);
                            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                            transactionDAO.setInitiatedBranch(_branchCode);
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            HashMap tansactionMap = new HashMap();
                            TxTransferTO transferTo = new TxTransferTO();
                            ArrayList TxTransferTO = new ArrayList();
                            //EXCESS_DEBIT
                            txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HEAD"));
                            txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                            txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                            txMap.put(TransferTrans.DR_PROD_TYPE, transactionTO.getProductType());
                            txMap.put(TransferTrans.PARTICULARS, "EXCESS_OF_CHEQUE_NO" + " - " + shgTransID);
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                            txMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, totalExcessTransAmt);
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt(currDt);
                            transferTo.setBranchId(BRANCH_ID);
                            transferTo.setInitiatedBranch(BRANCH_ID);
//                            transferTo.setStatusBy(CommonConstants.TTSYSTEM);
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setAuthorizeRemarks("DP");
                            transactionDAO.setLinkBatchID(transactionTO.getDebitAcctNo());
                            TxTransferTO.add(transferTo);

                            //EXCESS_CREDIT
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            txMap.put(TransferTrans.CR_ACT_NUM, transactionTO.getDebitAcctNo());
                            txMap.put(TransferTrans.CR_PROD_ID, transactionTO.getProductId());
                            txMap.put(TransferTrans.CR_AC_HD, (String) debitMap.get("AC_HEAD"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, transactionTO.getProductType());
                            txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                            txMap.put(TransferTrans.PARTICULARS, "EXCESS_OF_CHEQUE_NO" + " - " + shgTransID);
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, totalExcessTransAmt);
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt(currDt);
                            transferTo.setInitiatedBranch(BRANCH_ID);
//                            transferTo.setStatusBy(CommonConstants.TTSYSTEM);
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                            transactionDAO.setLinkBatchID(transactionTO.getDebitAcctNo());
                            transactionTO.setChequeNo("SERVICE_TAX");
                            TxTransferTO.add(transferTo);

                            transferDAO = new TransferDAO();
                            tansactionMap.put("TxTransferTO", TxTransferTO);
                            tansactionMap.put("MODE", map.get("COMMAND"));
                            tansactionMap.put("COMMAND", map.get("COMMAND"));
                            tansactionMap.put(CommonConstants.BRANCH_ID, BRANCH_ID);
                            System.out.println("################ tansactionMap :" + tansactionMap);
                            transMap = transferDAO.execute(tansactionMap, false);
                        }
                        transactionTO.setBatchId(shgTransID);
                        transactionTO.setBatchDt(currDt);
                        transactionTO.setStatus(CommonConstants.STATUS_CREATED);
                        transactionTO.setTransId(shgTransID);
                        System.out.println("transactionTO------------------->" + transactionTO);
                        sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", transactionTO);
                    }
                    for (int k = 0; k < shgList.size(); k++) {
                        HashMap dataMap = new HashMap();
                        dataMap = (HashMap) shgList.get(k);
                        dataMap.put("GROUP_ID", group_id);
                        dataMap.put("SHG_TRANS_ID", shgTransID);
                        dataMap.put("STATUS", CommonConstants.STATUS_CREATED);
                        dataMap.put("STATUS_DT", currDt);
                        dataMap.put("STATUS_BY", (String) map.get(CommonConstants.USER_ID));
                        System.out.println("############## dataMap " + dataMap);
                        sqlMap.executeUpdate("insertSHGTansaction", dataMap);
                    }
                }
                objLogDAO.addToLog(objLogTO);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateData(HashMap chargesMap, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
    }

    private void deleteData(HashMap chargesMap, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
    }

    private void authorize(HashMap map) throws Exception {
        System.out.println("######### map : " + map);
        String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        HashMap authMap = new HashMap();
        authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
        ArrayList selectedList = (ArrayList) authMap.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthorizeMap = new HashMap();
        AuthorizeMap = (HashMap) selectedList.get(0);
        String status = CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS));
        transferDAO = new TransferDAO();
        double totalExcessTransAmt = 0.0;
        if (map.containsKey("TransactionTO")) {
            TransactionTO transactionTO = new TransactionTO();
            HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
            LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
            if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
            }
            transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
            totalExcessTransAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
            System.out.println("-------------------> totalExcessTransAmt : " + totalExcessTransAmt);
            if (shgList != null) {
                for (int i = 0; i < shgList.size(); i++) {
                    HashMap dataMap = new HashMap();
                    dataMap = (HashMap) shgList.get(i);
                    String linkBatchId = "";
                    double paymentAmt = 0.0;
                    //                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                    paymentAmt = CommonUtil.convertObjToDouble(dataMap.get("PAYMENT")).doubleValue();
                    if (paymentAmt > 0) {
                        HashMap transferTransParam = new HashMap();
                        transferTransParam.put(CommonConstants.BRANCH_ID, BRANCH_ID);
                        transferTransParam.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                        transferTransParam.put("DEBIT_LOAN_TYPE", "DP");
                        linkBatchId = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));
                        transferTransParam.put("LINK_BATCH_ID", linkBatchId);
                        System.out.println(transferTransParam + "  linkBatchId####" + linkBatchId);
                        //                    TransactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
                        ArrayList transferTransList = (ArrayList) sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", transferTransParam);
                        if (transferTransList != null) {
                            String batchId = ((TxTransferTO) transferTransList.get(0)).getBatchId();
                            System.out.println("###@@# batchId : " + batchId);
                            HashMap transAuthMap = new HashMap();
                            transAuthMap.put("BATCH_ID", batchId);
                            transAuthMap.put(CommonConstants.AUTHORIZESTATUS, status);
                            transAuthMap.put(CommonConstants.AUTHORIZEDATA, transferTransList);
                            transferTransParam.put(CommonConstants.AUTHORIZEMAP, transAuthMap);
                            transferDAO.execute(transferTransParam, false);
                        }
                        totalExcessTransAmt -= paymentAmt;
                        //Update Closed Status
                        if (status.equals("AUTHORIZED")) {
                            double totPayableAmt = 0;
                            totPayableAmt = CommonUtil.convertObjToDouble(dataMap.get("BALANCE")).doubleValue()
                                    + CommonUtil.convertObjToDouble(dataMap.get("INTEREST")).doubleValue()
                                    + CommonUtil.convertObjToDouble(dataMap.get("PENAL")).doubleValue()
                                    + CommonUtil.convertObjToDouble(dataMap.get("CHARGE")).doubleValue();
                            if (totPayableAmt == paymentAmt) {
                                accountTO = new AccountTO();
                                accountTO.setActNum(linkBatchId);
                                accountTO.setActStatusId(CommonConstants.CLOSED);
                                accountTO.setClosedDt(currDt);
                                accountTO.setActStatusDt(currDt);
                                accountTO.setClosedBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                                System.out.println("insertData accountTO" + accountTO);
                                sqlMap.executeUpdate("updateLoanActClosingDetail", accountTO);
                            }
                        }
                    }
                }
                System.out.println("-------------------> totalExcessTransAmt : " + totalExcessTransAmt);
                if (totalExcessTransAmt > 0) {
                    HashMap transferTransParam = new HashMap();
                    transferDAO = new TransferDAO();
                    transferTransParam.put(CommonConstants.BRANCH_ID, BRANCH_ID);
                    transferTransParam.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                    transferTransParam.put("DEBIT_LOAN_TYPE", "DP");
                    String linkBatchId = CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo());
                    transferTransParam.put("LINK_BATCH_ID", linkBatchId);
                    ArrayList transferTransList = (ArrayList) sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", transferTransParam);
                    if (transferTransList != null) {
                        String batchId = ((TxTransferTO) transferTransList.get(0)).getBatchId();
                        System.out.println("###@@# batchId : " + batchId);
                        HashMap transAuthMap = new HashMap();
                        transAuthMap.put("BATCH_ID", batchId);
                        transAuthMap.put(CommonConstants.AUTHORIZESTATUS, status);
                        transAuthMap.put(CommonConstants.AUTHORIZEDATA, transferTransList);
                        transferTransParam.put(CommonConstants.AUTHORIZEMAP, transAuthMap);
                        transferDAO.execute(transferTransParam, false);
                    }
                }
                System.out.println("#$$$$$$$$$$$$$ AuthorizeMap : " + AuthorizeMap);
                sqlMap.executeUpdate("authorizeSHGTransactionDetails", AuthorizeMap);
            }
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        shgList = null;
    }

    public static void main(String str[]) {
        try {
            SHGTransactionDAO dao = new SHGTransactionDAO();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
