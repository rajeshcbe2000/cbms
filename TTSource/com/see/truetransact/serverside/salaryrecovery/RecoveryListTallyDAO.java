/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RecoveryListTallyDAO.java
 *
 *
 */
package com.see.truetransact.serverside.salaryrecovery;

import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
//import com.see.truetransact.transferobject.salaryrecovery.SalaryRecoveryListMasterTO;
//import com.see.truetransact.transferobject.salaryrecovery.SalaryRecoveryListDetailTO;
import com.see.truetransact.serverside.mdsapplication.mdsreceiptentry.MDSReceiptEntryDAO;
import com.see.truetransact.transferobject.mdsapplication.mdsreceiptentry.MDSReceiptEntryTO;

/**
 * RecoveryListGenerationDAO DAO.
 *
 * @author Suresh
 */
public class RecoveryListTallyDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private Date currDt = null;
    private Map returnMap = null;
    private HashMap finalMap = null;
    private Date intUptoDt = null;
    private TransactionDAO transactionDAO = null;
    TransferDAO transferDAO = new TransferDAO();
    private MDSReceiptEntryDAO mdsReceiptEntryDAO = null;
    private MDSReceiptEntryTO mdsReceiptEntryTO = null;
    private Iterator processLstIterator;
    double recoveredAmt = 0.0;

    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public RecoveryListTallyDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public static void main(String str[]) {
        try {
            RecoveryListTallyDAO dao = new RecoveryListTallyDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private HashMap getRecoveryTallyList(HashMap map) throws Exception {
        try {
            HashMap whereMap = new HashMap();
            HashMap recoveryMap = new HashMap();
            finalMap = new HashMap();
            ArrayList singleRecoveryList = new ArrayList();
           // System.out.println("wheremap" + map);
            List recoveryList = sqlMap.executeQueryForList("getRecoveryTallyData", map);
            if (recoveryList != null && recoveryList.size() > 0) {
                for (int i = 0; i < recoveryList.size(); i++) {
                    singleRecoveryList = new ArrayList();
                    whereMap = (HashMap) recoveryList.get(i);
                    String emp_Ref_No = CommonUtil.convertObjToStr(whereMap.get("EMP_REF_NO"));
                    List empNoList = null;
                    if (map.containsKey("NEW_MODE")) {
                        empNoList = sqlMap.executeQueryForList("getEmpNoRecoveryTallyDetails", whereMap);
                    } else {
                        empNoList = sqlMap.executeQueryForList("getEmpNoRecoveryTallyDetailsEdit", whereMap);
                    }
                    if (empNoList != null && empNoList.size() > 0) {
                        for (int j = 0; j < empNoList.size(); j++) {
                            recoveryMap = new HashMap();
                            recoveryMap = (HashMap) empNoList.get(j);
                            singleRecoveryList.add(recoveryMap);
                        }
                        finalMap.put(emp_Ref_No, singleRecoveryList);
                    }
                }
             //   System.out.println("####### Final DAO  Map : " + finalMap);
            }
            return finalMap;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        System.out.println(" ###### Map in RecoveryListTally DAO : " + map);
        HashMap recoveryMap = new HashMap();
        HashMap returnMap = new HashMap();
        try {
            sqlMap.startTransaction();
            if (map.containsKey("IMPORT_RECOVERY_LIST")) {
                recoveryMap = getRecoveryTallyList(map);
                if (recoveryMap != null && recoveryMap.size() > 0) {
                    returnMap.putAll(recoveryMap);
                }
                recoveryMap = null;
            } else if (map.containsKey("RECOVERY_PROCESS_LIST")) {
                intUptoDt = (Date) map.get("CALC_INT_UPTO_DT");
//                System.out.println(" ###### RECOVERY_PROCESS_LIST DAO : "+map);
                processTransactionPart(map);
                returnMap.put("STATUS", "COMPLETED");
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
        destroyObjects();
     //   System.out.println("@#$@@$@@@$ returnMap : " + returnMap);
        return (HashMap) returnMap;
    }

    public void processTransactionPart(HashMap map) throws Exception {
        try {
            HashMap finalMap = new HashMap();
         //   System.out.println("mapppppppp"+map);
            finalMap = (HashMap) map.get("RECOVERY_PROCESS_LIST");
          //  System.out.println(" ###### finalMap : "+finalMap);
            processLstIterator = finalMap.keySet().iterator();
            String key1 = "";
            String actNum = "";
            String prodType = "";
            for (int i = 0; i < finalMap.size(); i++) {
                key1 = (String) processLstIterator.next();
               // System.out.println("###### key1 ###### : "+key1);
                ArrayList singleRecoveryList = new ArrayList();
                singleRecoveryList = (ArrayList) finalMap.get(key1);
              //  System.out.println("################# singleRecoveryList Size : "+"  "+i+" "+singleRecoveryList.size());
               // System.out.println("###### singleRecoveryList : "+"  "+i+" "+singleRecoveryList);
                for (int j = 0; j < singleRecoveryList.size(); j++) {
                    HashMap singleAccountMap = new HashMap();
                    singleAccountMap = (HashMap) singleRecoveryList.get(j);
                    prodType = CommonUtil.convertObjToStr(singleAccountMap.get("PROD_TYPE"));
                    actNum = CommonUtil.convertObjToStr(singleAccountMap.get("ACT_NUM"));
                  System.out.println("#######%%%%%%%%%% singleAccountMap : " + actNum + "" + singleAccountMap);
                    if (prodType.equals("MDS")) {
                        transactionPartMDS(singleAccountMap, map);
                    } else if (prodType.equals(TransactionFactory.DEPOSITS)) {
                        transactionPartDeposits(singleAccountMap, map);
                    } else if (prodType.equals(TransactionFactory.GL)) {
                        transactionPartGL(singleAccountMap, map);
                    } else if (prodType.equals(TransactionFactory.LOANS) || prodType.equals(TransactionFactory.ADVANCES)) {
                        transactionPartLoans(singleAccountMap, map);
                    } else if (prodType.equals(TransactionFactory.OPERATIVE) || prodType.equals(TransactionFactory.SUSPENSE)) {
                        transactionPartOA(singleAccountMap, map, prodType);
                    }

                    if (!prodType.equals(TransactionFactory.GL) && !prodType.equals(TransactionFactory.OPERATIVE) && !prodType.equals(TransactionFactory.SUSPENSE)) {  //Update LOCK_STATUS NO
                        HashMap updateMap = new HashMap();
                        if (!prodType.equals("MDS")) {
                            if (actNum.indexOf("_") != -1) {
                                actNum = actNum.substring(0, actNum.indexOf("_"));
                            }
                            updateMap.put("ACCT_NUM", actNum);
                        } else {
                            String chittalNo = "";
                            String subNo = "1";
                            if (actNum.indexOf("_") != -1) {
                                chittalNo = actNum.substring(0, actNum.indexOf("_"));
                                subNo = actNum.substring(actNum.indexOf("_") + 1, actNum.length());
                                if(subNo.length()<=0)
                                    subNo ="1";
                                updateMap.put("CHITTAL_NO", chittalNo);
                                updateMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                            }
                        }
                        updateMap.put("LOCK_STATUS", "N");
                        sqlMap.executeUpdate("updateLockStatus" + prodType, updateMap);
                    }
                    //Jeffin
                    if (prodType.equals(TransactionFactory.SUSPENSE)) {
                        HashMap numberMap = new HashMap();
                        numberMap.put("ACCT_NUM", actNum);
                        List suspenseAcctList = sqlMap.executeQueryForList("getSuspenseInstallmentPresent", numberMap);
                        if (suspenseAcctList != null && suspenseAcctList.size() > 0) {
                            HashMap acctMap = (HashMap) suspenseAcctList.get(0);
                            if (acctMap != null && acctMap.size() > 0) {
                                String acctNum = CommonUtil.convertObjToStr(acctMap.get("ACCT_NUM"));
                                if (acctNum != null && !acctNum.equals("")) {
                                    HashMap inputMap = new HashMap();
                                    double principalAmt = CommonUtil.convertObjToDouble(singleAccountMap.get("REC_PRINCIPAL"));
                                    double interest = CommonUtil.convertObjToDouble(singleAccountMap.get("REC_PENAL"));
                                    String clockNo = CommonUtil.convertObjToStr(singleAccountMap.get("EMP_REF_NO"));
                                    String prodId = CommonUtil.convertObjToStr(singleAccountMap.get("PROD_ID"));
                                    if (principalAmt > 0 || interest > 0) {
                                        inputMap.put("ACCT_NUM", actNum);
                                        inputMap.put("PAID_AMOUNT", principalAmt);
                                        inputMap.put("INTEREST", interest);
                                        inputMap.put("CLOCK_NO", clockNo);
                                        inputMap.put("TRANS_ALL_ID", "RP");
                                        inputMap.put("PRODUCT_ID", prodId);
                                        inputMap.put("PAID_DATE", currDt.clone());
                                        inputMap.put("TRANS_TYPE", CommonConstants.CREDIT);
                                        updateSABalance(inputMap);
                                        if (principalAmt > 0) {
                                            //Update suspense_last_int_calc_dt for particular suspense account if prinicipal amount has been paid
                                            HashMap lastIntCalcMap = new HashMap();
                                            lastIntCalcMap.put("SUSPENSE_LAST_INT_CALC_DT", CommonUtil.getProperDate(currDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleAccountMap.get("INT_CALC_UPTO_DT")))));
                                            lastIntCalcMap.put("SUSPENSE_ACCT_NUM", actNum);
                                            sqlMap.executeUpdate("updateSuspenseLastIntCalcDt", lastIntCalcMap);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void updateSABalance(HashMap whereMap) throws Exception {
        System.out.println("entered here .......updateSABalance" + whereMap);
        sqlMap.executeUpdate("insertAuthCreditSales", whereMap);
//        List installmentList = sqlMap.executeQueryForList("getSuspendCurrInstDetails1", whereMap);
//        if (installmentList != null && installmentList.size() > 0 && recoveredAmt > 0) {
//            for (int k = 0; k < installmentList.size(); k++) {
//                double currInstAmt = 0.0;
//                whereMap = (HashMap) installmentList.get(k);
//                currInstAmt = CommonUtil.convertObjToDouble(whereMap.get("BALANCE_AMOUNT")).doubleValue();
//                whereMap.put("AMOUNT", String.valueOf(currInstAmt));
//                whereMap.put("PAID_DATE", intUptoDt);
//                if (currInstAmt > 0 && recoveredAmt >= currInstAmt && recoveredAmt > 0) {
//                    recoveredAmt -= currInstAmt;
//                    sqlMap.executeUpdate("updateSABalanceAmount", whereMap);
//                } else if (recoveredAmt > 0 && currInstAmt > 0) {
//                    if (recoveredAmt < currInstAmt) {
//                        whereMap.put("AMOUNT", String.valueOf(recoveredAmt));
//                        recoveredAmt = 0;
//                    }
//                    recoveredAmt -= currInstAmt;
//                    if (recoveredAmt <= 0) {
//                        k = installmentList.size();
//                    }
//                    sqlMap.executeUpdate("updateSABalanceAmount", whereMap);
//                }
//            }
//        }
//        installmentList = null;
        recoveredAmt = 0;
    }

    private void transactionPartGL(HashMap GLMap, HashMap map) throws Exception {
        try {
            System.out.println("############################### GLMap : " + GLMap);
            HashMap txMap = new HashMap();
            HashMap GLDataMap = new HashMap();
            ArrayList TxTransferTO = new ArrayList();
            TxTransferTO transferTo = new TxTransferTO();
            TransactionTO transactionTO = new TransactionTO();

            double recoveredAmount = 0;
            recoveredAmount = CommonUtil.convertObjToDouble(GLMap.get("RECOVERED_AMOUNT")).doubleValue();
//            System.out.println("############################### recoveredAmount : "+recoveredAmount);
            if (map.containsKey("TransactionTO") && recoveredAmount > 0) {
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                transactionDAO.setInitiatedBranch(_branchCode);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                }
                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));

                HashMap debitMap = new HashMap();
                if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("OA")) {
                    debitMap.put("ACT_NUM", transactionTO.getDebitAcctNo());
                    List lst = sqlMap.executeQueryForList("getAccNoProdIdDet", debitMap);
                    if (lst != null && lst.size() > 0) {
                        debitMap = (HashMap) lst.get(0);
                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                    }
                }
                if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("AD")) {
                    debitMap.put("prodId", transactionTO.getProductId());
                    List lst = sqlMap.executeQueryForList("TermLoan.getProdHead", debitMap);
                    if (lst != null && lst.size() > 0) {
                        debitMap = (HashMap) lst.get(0);
                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                    }
                }
                //DEBIT
                txMap = new HashMap();
                if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL") && transactionTO.getProductId().equals("")) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) transactionTO.getDebitAcctNo());
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("OA")) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                    txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                    txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                }
                txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put("DR_INST_TYPE", "VOUCHER");
                txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo());
                transferTo = transactionDAO.addTransferDebitLocal(txMap, recoveredAmount);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setRec_mode("RP");
                if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                    transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                }
                TxTransferTO.add(transferTo);

                //CREDIT
                txMap = new HashMap();
                transferTo = new TxTransferTO();
                txMap.put(TransferTrans.CR_AC_HD, GLMap.get("ACT_NUM"));
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo());
                txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                transferTo = transactionDAO.addTransferCreditLocal(txMap, recoveredAmount);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setRec_mode("RP");
                if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                    transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                }
                TxTransferTO.add(transferTo);

                transferDAO = new TransferDAO();
                GLDataMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                GLDataMap.put("COMMAND", GLDataMap.get("MODE"));
                GLDataMap.put("TxTransferTO", TxTransferTO);
                GLDataMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                //AUTHORIZE_MAP
                HashMap authorizeMap = new HashMap();
                authorizeMap.put("BATCH_ID", null);
                authorizeMap.put("USER_ID", map.get("USER_ID"));
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                GLDataMap.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
//                System.out.println("######## Before Transfer Dao GLDataMap : " + GLDataMap);
                HashMap transMap = transferDAO.execute(GLDataMap, false);
                GLMap = null;
                GLDataMap = null;
                txMap = null;
            }

        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void transactionPartOA(HashMap operativeMap, HashMap map, String prodType) throws Exception {       // OA and SA
        try {
            System.out.println("############################### operativeMap : " + operativeMap);
            HashMap txMap = new HashMap();
            HashMap operativeDataMap = new HashMap();
            ArrayList TxTransferTO = new ArrayList();
            TxTransferTO transferTo = new TxTransferTO();
            TransactionTO transactionTO = new TransactionTO();

            double recoveredAmount = 0;
            double unrecovery = 0;
            recoveredAmount = CommonUtil.convertObjToDouble(operativeMap.get("RECOVERED_AMOUNT")).doubleValue();
            if (operativeMap.containsKey("UNRECOVERY") && (CommonUtil.convertObjToStr(operativeMap.get("UNRECOVERY")) != null || !CommonUtil.convertObjToStr(operativeMap.get("UNRECOVERY")).equals(""))) {
                unrecovery = CommonUtil.convertObjToDouble(operativeMap.get("UNRECOVERY")).doubleValue();
            }
//            System.out.println("############################### recoveredAmount : "+recoveredAmount);
            if (map.containsKey("TransactionTO") && recoveredAmount > 0) {
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                transactionDAO.setInitiatedBranch(_branchCode);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                }
                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));

                HashMap acHeadMap = new HashMap();
                acHeadMap.put("PROD_ID", operativeMap.get("PROD_ID"));
                List lst = sqlMap.executeQueryForList("getAccountHeadProd" + prodType, acHeadMap);
                if (lst != null && lst.size() > 0) {
                    acHeadMap = (HashMap) lst.get(0);
                }

                HashMap debitMap = new HashMap();
                if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("OA")) {
                    debitMap.put("ACT_NUM", transactionTO.getDebitAcctNo());
                    lst = sqlMap.executeQueryForList("getAccNoProdIdDet", debitMap);
                    if (lst != null && lst.size() > 0) {
                        debitMap = (HashMap) lst.get(0);
                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                    }
                }
                if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("AD")) {
                    debitMap.put("prodId", transactionTO.getProductId());
                    lst = sqlMap.executeQueryForList("TermLoan.getProdHead", debitMap);
                    if (lst != null && lst.size() > 0) {
                        debitMap = (HashMap) lst.get(0);
                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                    }
                }
                //DEBIT
                txMap = new HashMap();
                if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL") && transactionTO.getProductId().equals("")) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) transactionTO.getDebitAcctNo());
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("OA")) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                    txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                    txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                }
                txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put("DR_INST_TYPE", "VOUCHER");
                txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo());
                transferTo = transactionDAO.addTransferDebitLocal(txMap, recoveredAmount);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setRec_mode("RP");
                if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                    transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                }
                TxTransferTO.add(transferTo);
                if (!operativeMap.get("PROD_TYPE").toString().equals("SA")) {
                    System.out.println("in one creditttt");
                    //CREDIT
                    txMap = new HashMap();
                    transferTo = new TxTransferTO();
                    txMap.put(TransferTrans.CR_PROD_TYPE, operativeMap.get("PROD_TYPE"));
                    txMap.put(TransferTrans.CR_PROD_ID, operativeMap.get("PROD_ID"));
                    txMap.put(TransferTrans.CR_AC_HD, acHeadMap.get("AC_HEAD"));
                    txMap.put(TransferTrans.CR_ACT_NUM, operativeMap.get("ACT_NUM"));
                    txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo());
                    txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, recoveredAmount);
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                    transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setRec_mode("RP");
                    if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                        transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                    }
                    TxTransferTO.add(transferTo);
                } else {
                    System.out.println("in twoo creditttt 11");
                    //CREDIT
                    HashMap acHeadMapInterest = new HashMap();
                    // acHeadMap.put("PROD_ID",operativeMap.get("PROD_ID"));
                    List lstInterest = (List) sqlMap.executeQueryForList("getAccountHeadProdForSAInterest", acHeadMapInterest);
                    if (lstInterest != null && lstInterest.size() > 0) {
                        acHeadMapInterest = (HashMap) lstInterest.get(0);
                    }

                    /*txMap = new HashMap();
                    transferTo = new TxTransferTO();
                    txMap.put(TransferTrans.CR_PROD_TYPE, operativeMap.get("PROD_TYPE"));
                    txMap.put(TransferTrans.CR_PROD_ID, operativeMap.get("PROD_ID"));
                    txMap.put(TransferTrans.CR_AC_HD, acHeadMapInterest.get("ACCOUNTHEAD_ID"));
                    txMap.put(TransferTrans.CR_ACT_NUM, operativeMap.get("ACT_NUM"));
                    txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo());
                    txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    Double interest = 0.0;
                    interest = CommonUtil.convertObjToDouble(operativeMap.get("INTEREST")).doubleValue();
                    if (interest <= 0) {
                        interest = CommonUtil.convertObjToDouble(operativeMap.get("PENAL")).doubleValue();
                    }
                    if (interest > 0) {
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, interest);
                        //}
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                        transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setRec_mode("RP");
                        if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                            transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                        }
                        TxTransferTO.add(transferTo);
                    }*/
                    //CREDIT
                    txMap = new HashMap();
                    transferTo = new TxTransferTO();
                    txMap.put(TransferTrans.CR_PROD_TYPE, operativeMap.get("PROD_TYPE"));
                    txMap.put(TransferTrans.CR_PROD_ID, operativeMap.get("PROD_ID"));
                    txMap.put(TransferTrans.CR_AC_HD, acHeadMap.get("AC_HEAD"));
                    txMap.put(TransferTrans.CR_ACT_NUM, operativeMap.get("ACT_NUM"));
                    txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo());
                    txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    Double principle = CommonUtil.convertObjToDouble(operativeMap.get("PRINCIPAL")).doubleValue();
                    System.out.println("unrecovery : " + unrecovery + "recoveredAmount : " + recoveredAmount);
                    if (unrecovery > 0) {
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, (recoveredAmount));
                    } else {
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, recoveredAmount);
                    }
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                    transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setRec_mode("RP");
                    if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                        transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                    }
                    TxTransferTO.add(transferTo);
                    System.out.println("in one creditttt 2222");
                }
                transferDAO = new TransferDAO();
                operativeDataMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                operativeDataMap.put("COMMAND", operativeDataMap.get("MODE"));
                operativeDataMap.put("TxTransferTO", TxTransferTO);
                operativeDataMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                //AUTHORIZE_MAP
                HashMap authorizeMap = new HashMap();
                authorizeMap.put("BATCH_ID", null);
                authorizeMap.put("USER_ID", map.get("USER_ID"));
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                operativeDataMap.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
//                System.out.println("######## Before Transfer Dao operativeDataMap : " + operativeDataMap);
                HashMap transMap = transferDAO.execute(operativeDataMap, false);
                operativeDataMap = null;
                operativeMap = null;
                acHeadMap = null;
                txMap = null;
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void transactionPartLoans(HashMap loanMap, HashMap map) throws Exception {
        try {
            System.out.println("############################### loanMap : " + loanMap);
            HashMap txMap = new HashMap();
            HashMap loanDataMap = new HashMap();
            ArrayList TxTransferTO = new ArrayList();
            TxTransferTO transferTo = new TxTransferTO();
            HashMap ALL_LOAN_AMOUNT = new HashMap();
            TransactionTO transactionTO = new TransactionTO();
            double penalAmount = 0;
            double chargesAmount = 0;
            double interestAmount = 0;
            double principalAmount = 0;
            double recoveredAmount = 0;
            penalAmount = CommonUtil.convertObjToDouble(loanMap.get("PENAL")).doubleValue();
            chargesAmount = CommonUtil.convertObjToDouble(loanMap.get("CHARGES")).doubleValue();
            interestAmount = CommonUtil.convertObjToDouble(loanMap.get("INTEREST")).doubleValue();
            principalAmount = CommonUtil.convertObjToDouble(loanMap.get("PRINCIPAL")).doubleValue();
            recoveredAmount = CommonUtil.convertObjToDouble(loanMap.get("RECOVERED_AMOUNT")).doubleValue();
//            System.out.println("############################### recoveredAmount : "+recoveredAmount);
            if (map.containsKey("TransactionTO") && recoveredAmount > 0) {
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                transactionDAO.setInitiatedBranch(_branchCode);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                }
                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));

                HashMap acHeadMap = new HashMap();
                acHeadMap.put("PROD_ID", loanMap.get("PROD_ID"));
                List lst = sqlMap.executeQueryForList("getAccountHeadProd" + loanMap.get("PROD_TYPE"), acHeadMap);
                if (lst != null && lst.size() > 0) {
                    acHeadMap = (HashMap) lst.get(0);
                }
                HashMap debitMap = new HashMap();
                if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("OA")) {
                    debitMap.put("ACT_NUM", transactionTO.getDebitAcctNo());
                    lst = sqlMap.executeQueryForList("getAccNoProdIdDet", debitMap);
                    if (lst != null && lst.size() > 0) {
                        debitMap = (HashMap) lst.get(0);
                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                    }
                }
                if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("AD")) {
                    debitMap.put("prodId", transactionTO.getProductId());
                    lst = sqlMap.executeQueryForList("TermLoan.getProdHead", debitMap);
                    if (lst != null && lst.size() > 0) {
                        debitMap = (HashMap) lst.get(0);
                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                    }
                }
                //DEBIT
                txMap = new HashMap();
                if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL") && transactionTO.getProductId().equals("")) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) transactionTO.getDebitAcctNo());
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put("TRANS_MOD_TYPE",TransactionFactory.GL);
                } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("OA")) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                    txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                    txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                    txMap.put("TRANS_MOD_TYPE",TransactionFactory.OPERATIVE);
                }
                txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put("DR_INST_TYPE", "VOUCHER");
                txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo());
                transferTo = transactionDAO.addTransferDebitLocal(txMap, recoveredAmount);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setRec_mode("RP");
                if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                    transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                }
                TxTransferTO.add(transferTo);

                //CREDIT
                txMap = new HashMap();
                transferTo = new TxTransferTO();
                txMap.put(TransferTrans.CR_PROD_TYPE, loanMap.get("PROD_TYPE"));
                txMap.put(TransferTrans.CR_PROD_ID, loanMap.get("PROD_ID"));
                txMap.put(TransferTrans.CR_AC_HD, acHeadMap.get("AC_HEAD"));
                txMap.put(TransferTrans.CR_ACT_NUM, loanMap.get("ACT_NUM"));
                txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo());
                txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                if (loanMap.get("PROD_TYPE") != null && loanMap.get("PROD_TYPE").equals(TransactionFactory.ADVANCES)) {
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                } else {
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.LOANS);
                }
                transferTo = transactionDAO.addTransferCreditLocal(txMap, recoveredAmount);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setRec_mode("RP");
                if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                    transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                }
                TxTransferTO.add(transferTo);

                transferDAO = new TransferDAO();
                loanDataMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                loanDataMap.put("COMMAND", loanDataMap.get("MODE"));
                loanDataMap.put("TxTransferTO", TxTransferTO);
                loanDataMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));

                //ALL_LOAN_AMOUNT Map Start
                if (penalAmount > 0) {
                    ALL_LOAN_AMOUNT.put("PENAL_INT", String.valueOf(penalAmount));
                    ALL_LOAN_AMOUNT.put("LOAN_CLOSING_PENAL_INT", String.valueOf(penalAmount));
                }
                if (principalAmount > 0) {
                    ALL_LOAN_AMOUNT.put("INSTALL_DT", String.valueOf(penalAmount));
                    ALL_LOAN_AMOUNT.put("CURR_MONTH_PRINCEPLE", String.valueOf(principalAmount));
                    ALL_LOAN_AMOUNT.put("LOAN_BALANCE_PRINCIPAL", String.valueOf(principalAmount));
                }
                if (interestAmount > 0) {
                    ALL_LOAN_AMOUNT.put("CURR_MONTH_INT", String.valueOf(interestAmount));
                    ALL_LOAN_AMOUNT.put("INTEREST", String.valueOf(interestAmount));
                }
                // Charges Start
                if (chargesAmount > 0) {
                    Map otherChargesMap = new HashMap();
                    HashMap chargeMap = new HashMap();
                    chargeMap.put("INT_CALC_UPTO_DT", intUptoDt);
                    chargeMap.put("ACT_NUM", loanMap.get("ACT_NUM"));
                    List chargeList = sqlMap.executeQueryForList("getRecoveryChargeList", chargeMap);
                    if (chargeList != null && chargeList.size() > 0) {
                        for (int i = 0; i < chargeList.size(); i++) {
                            chargeMap = (HashMap) chargeList.get(i);
                            double chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("AMOUNT")).doubleValue();
                            if (chargeAmt > 0) {
                                if (chargeMap.get("CHARGE_TYPE").equals("POSTAGE CHARGES") || chargeMap.get("CHARGE_TYPE").equals("MISCELLANEOUS CHARGES")
                                        || chargeMap.get("CHARGE_TYPE").equals("LEGAL CHARGES") || chargeMap.get("CHARGE_TYPE").equals("INSURANCE CHARGES")
                                        || chargeMap.get("CHARGE_TYPE").equals("EXECUTION DECREE CHARGES") || chargeMap.get("CHARGE_TYPE").equals("ARBITRARY CHARGES")
                                        || chargeMap.get("CHARGE_TYPE").equals("ADVERTISE CHARGES")) {
                                    ALL_LOAN_AMOUNT.put(chargeMap.get("CHARGE_TYPE"), String.valueOf(chargeAmt));
                                } else {
                                    otherChargesMap.put(chargeMap.get("CHARGE_TYPE"), String.valueOf(chargeAmt));
                                }
                            }
                        }
                        if (otherChargesMap.size() > 0) {
                            ALL_LOAN_AMOUNT.put("OTHER_CHARGES", otherChargesMap);
                        }
                    }
                }
                if (ALL_LOAN_AMOUNT != null && ALL_LOAN_AMOUNT.size() > 0) {
                    loanDataMap.put("ALL_AMOUNT", ALL_LOAN_AMOUNT);
                }
                //AUTHORIZE_MAP
                HashMap authorizeMap = new HashMap();
                authorizeMap.put("BATCH_ID", null);
                authorizeMap.put("USER_ID", map.get("USER_ID"));
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                loanDataMap.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
                loanDataMap.put("REC_PRINCIPAL", CommonUtil.convertObjToDouble(loanMap.get("REC_PRINCIPAL")));
                loanDataMap.put("VOUCHER_RELEASE", CommonUtil.getProperDate(currDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(loanMap.get("INT_CALC_UPTO_DT")))));
              //  System.out.println("######## Before Transfer Dao loanDataMap : " + loanDataMap);
                HashMap transMap = transferDAO.execute(loanDataMap, false);
                System.out.println("transMap"+transMap);
                HashMap voucherMap = new HashMap();
                voucherMap.put("ACT_NUM", CommonUtil.convertObjToStr(loanMap.get("ACT_NUM")));
                voucherMap.put("VOUCHER_RELEASE_DATE", currDt.clone());
                voucherMap.put("VOUCHER_RELEASE_BATCH_ID", transMap.get("TRANS_ID"));
                voucherMap.put("INT_CALC_UPTO_DT", CommonUtil.getProperDate(currDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(loanMap.get("INT_CALC_UPTO_DT")))));
                sqlMap.executeUpdate("updateVoucherReleaseDetails", voucherMap);
                if(CommonUtil.convertObjToDouble(loanDataMap.get("REC_PRINCIPAL"))>0){
                    HashMap accDetailMap = new HashMap();
                    accDetailMap.put("LAST_CALC_DT",CommonUtil.getProperDate(currDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(loanMap.get("INT_CALC_UPTO_DT")))));
                    accDetailMap.put("ACCOUNTNO",  CommonUtil.convertObjToStr(loanMap.get("ACT_NUM")));
                //    System.out.println("accDetailMap --------- :"+accDetailMap);
                    sqlMap.executeUpdate("updateclearBal", accDetailMap);
                }
                String pTyp = CommonUtil.convertObjToStr(loanMap.get("PROD_TYPE"));
                if (pTyp != null && pTyp.equals("TL")) {
                    voucherMap = new HashMap();
                    voucherMap.put("ACCOUNTNO", CommonUtil.convertObjToStr(loanMap.get("ACT_NUM")));
                    voucherMap.put("AUTHORIZEDT", CommonUtil.getProperDate(currDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(loanMap.get("INT_CALC_UPTO_DT")))));
                    voucherMap.put("ACCOUNT_STATUS", "CLOSED");
                    sqlMap.executeUpdate("updateAcctStatusForSalaryRecovery", voucherMap);
                }
                loanDataMap = null;
                acHeadMap = null;
                loanMap = null;
                txMap = null;
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void transactionPartDeposits(HashMap depositMap, HashMap map) throws Exception {
        try {
            System.out.println("############################### depositMap : " + depositMap);
            HashMap txMap = new HashMap();
            HashMap depositDataMap = new HashMap();
            ArrayList TxTransferTO = new ArrayList();
            TxTransferTO transferTo = new TxTransferTO();
            TransactionTO transactionTO = new TransactionTO();

            double penalAmount = 0;
            double recoveredAmount = 0;
            recoveredAmount = CommonUtil.convertObjToDouble(depositMap.get("RECOVERED_AMOUNT")).doubleValue();
            penalAmount = CommonUtil.convertObjToDouble(depositMap.get("REC_PENAL")).doubleValue();
//            System.out.println("############################### recoveredAmount : "+recoveredAmount);
          //  System.out.println("############################### penalAmount : " + penalAmount);
            if (map.containsKey("TransactionTO") && recoveredAmount > 0) {
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                transactionDAO.setInitiatedBranch(_branchCode);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                }
                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                HashMap acHeadMap = new HashMap();
                acHeadMap.put("PROD_ID", depositMap.get("PROD_ID"));
                List lst = sqlMap.executeQueryForList("getAccountHeadProdTD", acHeadMap);
                if (lst != null && lst.size() > 0) {
                    acHeadMap = (HashMap) lst.get(0);
                }
                HashMap debitMap = new HashMap();
                if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("OA")) {
                    debitMap.put("ACT_NUM", transactionTO.getDebitAcctNo());
                    lst = sqlMap.executeQueryForList("getAccNoProdIdDet", debitMap);
                    if (lst != null && lst.size() > 0) {
                        debitMap = (HashMap) lst.get(0);
                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                    }
                }
                if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("AD")) {
                    debitMap.put("prodId", transactionTO.getProductId());
                    lst = sqlMap.executeQueryForList("TermLoan.getProdHead", debitMap);
                    if (lst != null && lst.size() > 0) {
                        debitMap = (HashMap) lst.get(0);
                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                    }
                }
                //DEBIT
                txMap = new HashMap();
                if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL") && transactionTO.getProductId().equals("")) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) transactionTO.getDebitAcctNo());
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("OA")) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                    txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                    txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                }
                txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put("DR_INST_TYPE", "VOUCHER");
                txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo());
                transferTo = transactionDAO.addTransferDebitLocal(txMap, recoveredAmount);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setRec_mode("RP");
                if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                    transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                }
                TxTransferTO.add(transferTo);

                //CREDIT
                txMap = new HashMap();
                transferTo = new TxTransferTO();
                txMap.put(TransferTrans.CR_PROD_TYPE, depositMap.get("PROD_TYPE"));
                txMap.put(TransferTrans.CR_PROD_ID, depositMap.get("PROD_ID"));
                txMap.put(TransferTrans.CR_AC_HD, acHeadMap.get("AC_HEAD"));
                txMap.put(TransferTrans.CR_ACT_NUM, depositMap.get("ACT_NUM"));
                txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo());
                txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                transferTo = transactionDAO.addTransferCreditLocal(txMap, recoveredAmount);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setRec_mode("RP");
                if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                    transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                }
                TxTransferTO.add(transferTo);

                transferDAO = new TransferDAO();
                depositDataMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                depositDataMap.put("COMMAND", depositDataMap.get("MODE"));
                depositDataMap.put("TxTransferTO", TxTransferTO);
                depositDataMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                if (penalAmount > 0) {
                    depositDataMap.put("DEPOSIT_PENAL_AMT", String.valueOf(penalAmount));
                    depositDataMap.put("DEPOSIT_PENAL_MONTH", CommonUtil.convertObjToDouble(depositMap.get("DEPOSIT_PENAL_MONTH")));
                }
                //AUTHORIZE_MAP
                HashMap authorizeMap = new HashMap();
                authorizeMap.put("BATCH_ID", null);
                authorizeMap.put("USER_ID", map.get("USER_ID"));
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                depositDataMap.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
            //    System.out.println("######## Before Transfer Dao depositDataMap : " + depositDataMap);
                HashMap transMap = transferDAO.execute(depositDataMap, false);
                depositDataMap = null;
                depositMap = null;
                acHeadMap = null;
                txMap = null;
            }

        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void transactionPartMDS(HashMap chittalMap, HashMap map) throws Exception {
        try {
            System.out.println("############################### chittalMap : " + chittalMap);
            double totalRecAmt = CommonUtil.convertObjToDouble(chittalMap.get("RECOVERED_AMOUNT"));
            HashMap MDSmap = new HashMap();
            HashMap bonusMap = new HashMap();
            HashMap whereMap = new HashMap();
            HashMap chittalDetailMap = new HashMap();
            HashMap installmentMap = new HashMap();
            String actNo = "";
            String subNo = "";
            String chittalNo = "";
            long count = 0;
            int curInsNo = 0;
            int noOfInsPaid = 0;
            long pendingInst = 0;
            double totBonusAmt = 0;
            double totPenalAmt = 0;
            double totNoticeAmt = 0;
            double totDiscountAmt = 0;
            double totArbitrationAmt = 0;
            int unrecoveredCount = 0;
            int paying = 0;
            actNo = CommonUtil.convertObjToStr(chittalMap.get("ACT_NUM"));
            if (actNo.indexOf("_") != -1) {
                chittalNo = actNo.substring(0, actNo.indexOf("_"));
                subNo = actNo.substring(actNo.indexOf("_") + 1, actNo.length());
            }
            whereMap.put("CHITTAL_NO", chittalNo);
            whereMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
            List chittalList = (List) sqlMap.executeQueryForList("getMDSChittalDetails", whereMap);
            if (chittalList != null && chittalList.size() > 0) {
                chittalDetailMap = (HashMap) chittalList.get(0);
                whereMap.put("INT_CALC_UPTO_DT", setProperDtFormat(intUptoDt));
                List bonusList = sqlMap.executeQueryForList("getRecoveryListInstallmentMapDetails", whereMap);
             //   System.out.println("bonusList"+bonusList);
                if (bonusList != null && bonusList.size() > 0) {
                    for (int i = 0; i < bonusList.size(); i++) {
                        HashMap bonusNewMap = (HashMap) bonusList.get(i);
                        double instAmt = (double) Math.round((CommonUtil.convertObjToDouble(chittalDetailMap.get("INST_AMT"))) * 100) / 100;
                        double bonusAmount =  (double) Math.round((CommonUtil.convertObjToDouble(bonusNewMap.get("BONUS_AMT"))) * 100) / 100;
                        double penalAmt = (double) Math.round((CommonUtil.convertObjToDouble(bonusNewMap.get("PENAL_AMT"))) * 100) / 100;
                        double currInstAmount = (double) Math.round(( instAmt-bonusAmount ) * 100) / 100;
                        double thisMonth = (double) Math.round(( currInstAmount+penalAmt ) * 100) / 100;
                        if (totalRecAmt > 0) {
                            totalRecAmt =  (double) Math.round(( totalRecAmt-thisMonth ) * 100) / 100;
                            unrecoveredCount += 1;
                        }
                    }
                }
                if (bonusList != null && bonusList.size() > 0) {
                    for (int i = 0; i < unrecoveredCount; i++) {
                        HashMap instMap = new HashMap();
                        bonusMap = (HashMap) bonusList.get(i);
                       // System.out.println("bonusMap"+bonusMap);
                        totBonusAmt += CommonUtil.convertObjToDouble(bonusMap.get("BONUS_AMT")).doubleValue();
                        totPenalAmt += CommonUtil.convertObjToDouble(bonusMap.get("PENAL_AMT")).doubleValue();
                        totDiscountAmt += CommonUtil.convertObjToDouble(bonusMap.get("DISCOUNT_AMT")).doubleValue();
                        if (i == 0) {
                            totNoticeAmt = CommonUtil.convertObjToDouble(bonusMap.get("NOTICE_AMT")).doubleValue();
                            totArbitrationAmt = CommonUtil.convertObjToDouble(bonusMap.get("ARBITRATION_AMT")).doubleValue();
                        }
                        instMap.put("BONUS", bonusMap.get("BONUS_AMT"));
                        instMap.put("DISCOUNT", bonusMap.get("DISCOUNT_AMT"));
                        instMap.put("PENAL", bonusMap.get("PENAL_AMT"));
                        instMap.put("INST_AMT", chittalDetailMap.get("INST_AMT"));
                        installmentMap.put(CommonUtil.convertObjToStr(bonusMap.get("INSTALLMENT_NO")), instMap);
                    }
                }
                paying = unrecoveredCount;
                List insList = sqlMap.executeQueryForList("getNoOfInstalmentsPaid", whereMap);
                if (insList != null && insList.size() > 0) {
                    whereMap = (HashMap) insList.get(0);
                    noOfInsPaid = CommonUtil.convertObjToInt(whereMap.get("NO_OF_INST"));
                    count = CommonUtil.convertObjToLong(whereMap.get("NO_OF_INST"));
                }
                curInsNo = noOfInsPaid + paying;
                pendingInst = curInsNo - (noOfInsPaid + paying);
                int insDay = 0;
                Date paidUpToDate = null;
                HashMap instDateMap = new HashMap();
                instDateMap.put("SCHEME_NAME", chittalDetailMap.get("SCHEME_NAME"));
                instDateMap.put("DIVISION_NO", CommonUtil.convertObjToInt(chittalDetailMap.get("DIVISION_NO")));
                instDateMap.put("INSTALLMENT_NO", CommonUtil.convertObjToInt(String.valueOf(count)));
                List insLst = sqlMap.executeQueryForList("getSelectInstUptoPaid", instDateMap);
                if (insLst != null && insLst.size() > 0) {
                    instDateMap = (HashMap) insLst.get(0);
                    paidUpToDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(instDateMap.get("NEXT_INSTALLMENT_DATE")));
                } else {
                    Date startedDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalDetailMap.get("SCHEME_START_DT")));
                    insDay = CommonUtil.convertObjToInt(chittalDetailMap.get("INSTALLMENT_DAY"));
                    startedDate.setDate(insDay);
                    int stMonth = startedDate.getMonth();
                    startedDate.setMonth(stMonth + (int) count - 1);
                    paidUpToDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(startedDate));
                }
                String narration = "";
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM/yyyy");
                int noInstPay = CommonUtil.convertObjToInt(String.valueOf(paying));
                if (noInstPay == 1) {
                    narration = "Inst#" + (noOfInsPaid + 1);
                    Date dt = DateUtil.addDays(paidUpToDate, 30);
                    narration += " " + sdf.format(dt);
                } else if (noInstPay > 1) {
                    narration = "Inst#" + (noOfInsPaid + 1);
                    narration += "-" + (noOfInsPaid + noInstPay);
                    Date dt = DateUtil.addDays(paidUpToDate, 30);
                    narration += " " + sdf.format(dt);
                    dt = DateUtil.addDays(paidUpToDate, 30 * noInstPay);
                    narration += " To " + sdf.format(dt);
                }
          //      System.out.println("#$#$# narration :" + narration);
                if (chittalMap.containsKey("UNRECOVERY")) {
                    if (CommonUtil.convertObjToStr(chittalMap.get("UNRECOVERY")) != null || !CommonUtil.convertObjToStr(chittalMap.get("UNRECOVERY")).equals("")) {
                        double amt = paying * (CommonUtil.convertObjToDouble(chittalDetailMap.get("INSTALLMENT_AMOUNT")));
                        chittalMap.put("PRINCIPAL", amt);
                    }
                }

                //SET RECEIPT_ENTRY_TO
                mdsReceiptEntryTO = new MDSReceiptEntryTO();
                mdsReceiptEntryTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                mdsReceiptEntryTO.setSchemeName(CommonUtil.convertObjToStr(chittalDetailMap.get("SCHEME_NAME")));
                mdsReceiptEntryTO.setChittalNo(chittalNo);
                mdsReceiptEntryTO.setSubNo(CommonUtil.convertObjToDouble(subNo));
                mdsReceiptEntryTO.setMemberName(CommonUtil.convertObjToStr(chittalDetailMap.get("MEMBER_NAME")));
                mdsReceiptEntryTO.setDivisionNo(CommonUtil.convertObjToDouble(chittalDetailMap.get("DIVISION_NO")));
                mdsReceiptEntryTO.setChitStartDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalDetailMap.get("CHIT_START_DT"))));
                mdsReceiptEntryTO.setChitEndDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalDetailMap.get("CHIT_END_DT"))));
                mdsReceiptEntryTO.setPaidDate(currDt);
                mdsReceiptEntryTO.setNoOfInst(CommonUtil.convertObjToInt(chittalDetailMap.get("NO_OF_INSTALLMENTS")));
                mdsReceiptEntryTO.setCurrInst(CommonUtil.convertObjToDouble(String.valueOf(curInsNo)));
                mdsReceiptEntryTO.setPendingInst(CommonUtil.convertObjToDouble(String.valueOf(pendingInst)));
                mdsReceiptEntryTO.setInstAmt(CommonUtil.convertObjToDouble(chittalDetailMap.get("INST_AMT")));
                mdsReceiptEntryTO.setNoOfInstPay(CommonUtil.convertObjToInt(String.valueOf(paying)));
                mdsReceiptEntryTO.setInstAmtPayable(CommonUtil.convertObjToDouble(chittalMap.get("PRINCIPAL")));
                mdsReceiptEntryTO.setPaidInst(CommonUtil.convertObjToDouble(String.valueOf(noOfInsPaid)));
                mdsReceiptEntryTO.setBonusAmtPayable(CommonUtil.convertObjToDouble(String.valueOf(totBonusAmt)));
                mdsReceiptEntryTO.setDiscountAmt(CommonUtil.convertObjToDouble(String.valueOf(totDiscountAmt)));
                mdsReceiptEntryTO.setPenalAmtPayable(CommonUtil.convertObjToDouble(String.valueOf(totPenalAmt)));
                mdsReceiptEntryTO.setNoticeAmt(CommonUtil.convertObjToDouble(String.valueOf(totNoticeAmt)));
                mdsReceiptEntryTO.setArbitrationAmt(CommonUtil.convertObjToDouble(String.valueOf(totArbitrationAmt)));
                mdsReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(chittalMap.get("RECOVERED_AMOUNT")));
                mdsReceiptEntryTO.setNarration(narration);
                mdsReceiptEntryTO.setBankPay("N");
                mdsReceiptEntryTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
                mdsReceiptEntryTO.setStatus(CommonConstants.STATUS_CREATED);
                mdsReceiptEntryTO.setStatusDt(currDt);
                mdsReceiptEntryTO.setBranchCode(CommonUtil.convertObjToStr(chittalDetailMap.get("BRANCH_CODE")));
                mdsReceiptEntryTO.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
            //    System.out.println("############################### mdsReceiptEntryTO : " + mdsReceiptEntryTO);
             //   System.out.println("############################### installmentMap : " + installmentMap);

                MDSmap.put("INSTALLMENT_MAP", installmentMap);
                MDSmap.put("mdsReceiptEntryTO", mdsReceiptEntryTO);
                List closureList = (List) sqlMap.executeQueryForList("checkSchemeClosureDetails", chittalDetailMap);
                if (closureList != null && closureList.size() > 0) {
                    MDSmap.put("MDS_CLOSURE", "MDS_CLOSURE");
                } else {
                    MDSmap.remove("MDS_CLOSURE");
                }
                MDSmap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                MDSmap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                MDSmap.put("TransactionTO", map.get("TransactionTO"));
                MDSmap.put("INT_CALC_UPTO_DT", intUptoDt);
                MDSmap.put("FROM_RECOVERY_TALLY", "FROM_RECOVERY_TALLY");
                MDSmap.put("REC_MODE", "RP");
               // System.out.println("########### MDSmap : " + MDSmap);
                if (CommonUtil.convertObjToDouble(chittalMap.get("PRINCIPAL")) > 0) {
                    mdsReceiptEntryDAO = new MDSReceiptEntryDAO();
                    HashMap transMap = new HashMap();
                    transMap = mdsReceiptEntryDAO.execute(MDSmap, false);                    // INSERT_TRANSACTION
                //    System.out.println("################# transMap : " + transMap);

                    //AUTHORIZE_START
                    HashMap authorizeMap = new HashMap();
                    authorizeMap.put("NET_TRANS_ID", transMap.get("BATCH_ID"));
                    authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                    authorizeMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                    authorizeMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                    mdsReceiptEntryTO.setCommand("");
                    MDSmap.put("AUTHORIZEMAP", authorizeMap);
                    MDSmap.put("mdsReceiptEntryTO", mdsReceiptEntryTO);
                    MDSmap.put("SCHEME_NAME", CommonUtil.convertObjToStr(chittalDetailMap.get("SCHEME_NAME")));
                    MDSmap.put("USER_ID", map.get(CommonConstants.USER_ID));
                 //   System.out.println("########### MDSmap : " + MDSmap);
                    mdsReceiptEntryDAO = new MDSReceiptEntryDAO();
                    mdsReceiptEntryDAO.execute(MDSmap, false); // AUTHORIZE_TRANSACTION    
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return null;
    }

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    private void destroyObjects() {
        returnMap = null;
    }
}