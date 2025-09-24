/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * TransExceptionDAO.java
 *
 *
 */
package com.see.truetransact.serverside.transexception;

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
import com.see.truetransact.serverside.common.viewall.SelectAllDAO;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.transferobject.transexception.TransExceptionTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.deposit.interestmaintenance.InterestMaintenanceRateTO;
import java.text.*;
import java.util.Calendar;

/**
 * DAO.
 *
 * @author
 */
public class TransExceptionDAO extends TTDAO {

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
    private Iterator proceesMpIterator;
    double recoveredAmt = 0.0;
    private List recoveryList = null;
    private List recoveryRowList = null;
    private Date interestUptoDt = null;
    private Double total = 0.0;
    DecimalFormat df = new DecimalFormat("#.##");
    private final static double AVERAGE_MILLIS_PER_MONTH = 365.24 * 24 * 60 * 60 * 1000 / 12;
    TransExceptionTO objTO = null;
    ArrayList clockList = new ArrayList();
    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public TransExceptionDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        intUptoDt = ServerUtil.getCurrentDate("0001");
        currDt = ServerUtil.getCurrentDate("0001");
        System.out.println("@#$@@$intUptoDt1 : " + intUptoDt);
        interestUptoDt = intUptoDt;
        // interestUptoDt.setDate(interestUptoDt.getDate()+1);
    }

    public static void main(String str[]) {
        try {
            //  RecoveryListTallyDAO dao = new RecoveryListTallyDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        clockList = new ArrayList();
        System.out.println(" ###### Map in RecoveryListTally DAO : " + map);
        HashMap recoveryMap = new HashMap();
        HashMap returnMap = new HashMap();
        try {
            sqlMap.startTransaction();
            objTO = new TransExceptionTO();
            objTO.setTransId(getTransID());
            if (map.containsKey("RECOVERY_PROCESS_LIST")) {
                intUptoDt = currDt;
                processTransactionPart(map);

            }
            if (map.containsKey("INSERT")) {
                insertTableData(map);
            }
            returnMap.put("STATUS", "COMPLETED");
            returnMap.put("TransId", objTO.getTransId());
            returnMap.put("CLOACK_LIST", clockList);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
        destroyObjects();
        System.out.println("@#$@@$@@@$ returnMap : " + returnMap);
        return (HashMap) returnMap;
    }

    public void insertTableData(HashMap map) {
        try {
            ArrayList newList = new ArrayList();
            // List mapList=new ArrayList();
            newList = (ArrayList) (map.get("INSERT"));
            intUptoDt = (Date) currDt;
            // objTO.setTransId(getTransID());
            objTO.setInstDate(intUptoDt);
            for (int i = 0; i < newList.size(); i++) {
                List mapList = null;
                mapList = (List) newList.get(i);
                objTO.setSlno(i + 1);
                objTO.setCust_id(mapList.get(1).toString());
                objTO.setEmpRefNo(mapList.get(2).toString());
                objTO.setMemberName(mapList.get(3).toString());
                objTO.setProd_Type(mapList.get(4).toString());
                objTO.setProd_ID(mapList.get(5).toString());
                objTO.setActNum(mapList.get(6).toString());
                objTO.setTotalDemand(Double.parseDouble(mapList.get(7).toString()));
                objTO.setPenalMonth(Double.parseDouble(mapList.get(8).toString()));
                objTO.setPrincipal(Double.parseDouble(mapList.get(9).toString()));
                objTO.setInterest(Double.parseDouble(mapList.get(10).toString()));
                objTO.setPenal(Double.parseDouble(mapList.get(11).toString()));
                objTO.setBonus(Double.parseDouble(mapList.get(12).toString()));
                objTO.setCharges(Double.parseDouble(mapList.get(13).toString()));
                objTO.setActualDemand(Double.parseDouble(mapList.get(14).toString()));
                sqlMap.executeUpdate("insertTransException", objTO);
                if(!clockList.contains(objTO.getEmpRefNo())){    
                	clockList.add(objTO.getEmpRefNo());
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getTransID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "TRANS_EXCEPTION");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    public void processTransactionPart(HashMap map) throws Exception {
        try {
            HashMap finalMap = new HashMap();
            finalMap = (HashMap) map.get("RECOVERY_PROCESS_LIST");
            System.out.println(" ###### finalMap : " + finalMap);
            processLstIterator = finalMap.keySet().iterator();
            String key1 = "";
            String key2 = "";
            String actNum = "";
            String prodType = "";
            for (int i = 0; i < finalMap.size(); i++) {
                key1 = (String) processLstIterator.next();
                System.out.println("###### key1 ###### : " + key1);
                HashMap singleRecoveryList = new HashMap();
                singleRecoveryList = (HashMap) finalMap.get(key1);
                proceesMpIterator = singleRecoveryList.keySet().iterator();
                System.out.println("################# singleRecoveryList Size : " + "  " + i + " " + singleRecoveryList.size());
                System.out.println("###### singleRecoveryList : " + "  " + i + " " + singleRecoveryList);
                for (int j = 0; j < singleRecoveryList.size(); j++) {
                    key2 = (String) proceesMpIterator.next();
                    HashMap singleAccountMap = new HashMap();
                    singleAccountMap = (HashMap) singleRecoveryList.get(key2);
                    System.out.println("#######%%%%%%%%%% singleAccountMap : " + j + "" + singleAccountMap);
                    prodType = CommonUtil.convertObjToStr(singleAccountMap.get("PROD_TYPE"));
                    actNum = CommonUtil.convertObjToStr(singleAccountMap.get("ACT_NUM"));
                    System.out.println("prod type "+prodType);
                    if (prodType.equals("MDS")) {
                        transactionPartMDS(singleAccountMap, map);
                    } else if (prodType.equals("TD")) {
                        transactionPartDeposits(singleAccountMap, map);
                    } //                    else if (prodType.equals("SB")){
                    //                        transactionPartGL(singleRecoveryList,map);
                    //                    }
                    else if (prodType.equals("TL")) {
                        transactionPartLoans(singleAccountMap, map);
                    } 
                    //added by rishad 23/04/2014
                     else if (prodType.equals("AD")) {
                        transactionPartLoansAD(singleAccountMap, map);
                    } 
                    
                    else if (prodType.equals("SA")) {
                        prodType = "SA";
                        transactionPartOA(singleAccountMap, map, prodType);
                    }

                    if (!prodType.equals(TransactionFactory.GL) && !prodType.equals(TransactionFactory.OPERATIVE) && !prodType.equals("SA")) {  //Update LOCK_STATUS NO
                        HashMap updateMap = new HashMap();
                        if (!prodType.equals("MDS")) {
                            if (actNum.indexOf("_") != -1) {
                                actNum = actNum.substring(0, actNum.indexOf("_"));
                            }
                            updateMap.put("ACCT_NUM", actNum);
                        } else {
                            String chittalNo = "";
                            String subNo = "";
                            if (actNum.indexOf("_") != -1) {
                                chittalNo = actNum.substring(0, actNum.indexOf("_"));
                                subNo = actNum.substring(actNum.indexOf("_") + 1, actNum.length());
                                updateMap.put("CHITTAL_NO", chittalNo);
                                updateMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                            }
                        }
                        updateMap.put("LOCK_STATUS", "N");
                        sqlMap.executeUpdate("updateLockStatus" + prodType, updateMap);
                    }

                    if (prodType.equals("SA")) {  // UPDATE PAID Amd BALANCE AMOUNT
                        HashMap inputMap = new HashMap();
                        double principalAmt = CommonUtil.convertObjToDouble(singleAccountMap.get("PRINCIPAL"));
                        double interest = CommonUtil.convertObjToDouble(singleAccountMap.get("INTEREST"));
                        recoveredAmt = CommonUtil.convertObjToDouble(singleAccountMap.get("PRINCIPAL"));
                        String clockNo = CommonUtil.convertObjToStr(singleAccountMap.get("CLOCK_NO"));
                        String transAllId = CommonUtil.convertObjToStr(objTO.getTransId());
                        String accounttNum = CommonUtil.convertObjToStr(singleAccountMap.get("ACT_NUM"));
                        String prodId = CommonUtil.convertObjToStr(singleAccountMap.get("PROD_ID"));
                        recoveredAmt = 0.0;
                        if (principalAmt > 0 || interest > 0) {
                            inputMap.put("ACCT_NUM", actNum);
                            inputMap.put("PAID_AMOUNT", principalAmt);
                            inputMap.put("INTEREST", interest);
                            inputMap.put("CLOCK_NO", clockNo);
                            inputMap.put("TRANS_ALL_ID", transAllId);
                            inputMap.put("PRODUCT_ID", prodId);
                            inputMap.put("PAID_DATE",currDt.clone());
                            inputMap.put("TRANS_TYPE", CommonConstants.CREDIT);
                            updateSABalance(inputMap);
                            if(principalAmt > 0){
                                //Update suspense_last_int_calc_dt for particular suspense account if prinicipal amount has been paid
                                HashMap lastIntCalcMap = new HashMap();
                                lastIntCalcMap.put("SUSPENSE_LAST_INT_CALC_DT", CommonUtil.getProperDate(currDt, DateUtil.addDays(currDt, -1)));
                                lastIntCalcMap.put("SUSPENSE_ACCT_NUM", actNum);
                                sqlMap.executeUpdate("updateSuspenseLastIntCalcDt", lastIntCalcMap);
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
        // List installmentList = sqlMap.executeQueryForList("getSuspendCurrInstDetails", whereMap);
//        List installmentList = sqlMap.executeQueryForList("getSuspendCurrInstDetails1", whereMap);
//        if (installmentList != null && installmentList.size() > 0 && recoveredAmt > 0) {
//            System.out.println("updatesabalance in fn..." + installmentList);
//            for (int k = 0; k < installmentList.size(); k++) {
//                double currInstAmt = 0.0;
//                whereMap = (HashMap) installmentList.get(k);
//                //currInstAmt = CommonUtil.convertObjToDouble(whereMap.get("BALANCE_AMOUNT")).doubleValue();
//                currInstAmt = CommonUtil.convertObjToDouble(whereMap.get("BALANCE_AMOUNT")).doubleValue();
//                whereMap.put("AMOUNT", String.valueOf(currInstAmt));
//                whereMap.put("PAID_DATE", setProperDtFormat(intUptoDt));
//                if (currInstAmt > 0 && recoveredAmt >= currInstAmt && recoveredAmt > 0) {
//                    recoveredAmt -= currInstAmt;
//                    sqlMap.executeUpdate("updateSABalanceAmount1", whereMap);
//                } else if (recoveredAmt > 0 && currInstAmt > 0) {
//                    if (recoveredAmt < currInstAmt) {
//                        whereMap.put("AMOUNT", String.valueOf(recoveredAmt));
//                        recoveredAmt = 0;
//                    }
//                    recoveredAmt -= currInstAmt;
//                    if (recoveredAmt <= 0) {
//                        k = installmentList.size();
//                    }
//                    sqlMap.executeUpdate("updateSABalanceAmount1", whereMap);
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
            String dbactnum = null;
            String dbprodid = null;
            dbactnum = CommonUtil.convertObjToStr(GLMap.get("DEBIT_ACT_NUM").toString());
            dbprodid = CommonUtil.convertObjToStr(GLMap.get("DEBIT_PROD_ID").toString());
            recoveredAmount = CommonUtil.convertObjToDouble(GLMap.get("RECOVERED_AMOUNT")).doubleValue();
            //            System.out.println("############################### recoveredAmount : "+recoveredAmount);
            if (recoveredAmount > 0) {
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                transactionDAO.setInitiatedBranch(_branchCode);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);

                //                HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                //                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                //                if (transactionDetailsMap.size()>0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs"))
                //                    allowedTransDetailsTO = (LinkedHashMap)transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                //                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));


                transactionTO.setDebitAcctNo(dbactnum);
                transactionTO.setProductId(dbprodid);
                transactionTO.setProductType("OA");


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
            String dbactnum = null;
            String dbprodid = null;
            dbactnum = CommonUtil.convertObjToStr(operativeMap.get("DEBIT_ACT_NUM").toString());
            dbprodid = CommonUtil.convertObjToStr(operativeMap.get("DEBIT_PROD_ID").toString());
            recoveredAmount = CommonUtil.convertObjToDouble(operativeMap.get("RECOVERED_AMOUNT")).doubleValue();
            //            System.out.println("############################### recoveredAmount : "+recoveredAmount);
            // if(map.containsKey(recoveredAmount>0)) {
            if (recoveredAmount > 0) {
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                transactionDAO.setInitiatedBranch(_branchCode);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);

                //               HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                //                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                //                if (transactionDetailsMap.size()>0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs"))
                //                    allowedTransDetailsTO = (LinkedHashMap)transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                //                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));

                transactionTO.setDebitAcctNo(dbactnum);
                transactionTO.setProductId(dbprodid);
                transactionTO.setProductType("OA");
                transactionTO.setBranchId(_branchCode);
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
                txMap.put(TransferTrans.PARTICULARS, operativeMap.get("ACT_NUM")+":"+(String) acHeadMap.get("AC_HEAD_DESC"));
                transferTo = transactionDAO.addTransferDebitLocal(txMap, recoveredAmount);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setRec_mode("SI");
                transferTo.setTransAllId(objTO.getTransId());
                if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                    transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                }
                TxTransferTO.add(transferTo);



                if (!prodType.equals("SA")) {
                    
                    System.out.println("in one creditttt");
                    //CREDIT
                    txMap = new HashMap();
                    transferTo = new TxTransferTO();
                    txMap.put(TransferTrans.CR_PROD_TYPE, operativeMap.get("PROD_TYPE"));
                    txMap.put(TransferTrans.CR_PROD_ID, operativeMap.get("PROD_ID"));
                    txMap.put(TransferTrans.CR_AC_HD, acHeadMap.get("AC_HEAD"));
                    txMap.put(TransferTrans.CR_ACT_NUM, operativeMap.get("ACT_NUM"));
                    txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo()+":"+(String) debitMap.get("AC_HD_DESC"));
                    txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, recoveredAmount);
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                    transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setRec_mode("SI");
                    transferTo.setTransAllId(objTO.getTransId());
                    if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                        transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                    }
                    TxTransferTO.add(transferTo);
                } else {
                    System.out.println("in twoo creditttt 11");
                    //CREDIT
                    txMap = new HashMap();
                    transferTo = new TxTransferTO();
                    txMap.put(TransferTrans.CR_PROD_TYPE, prodType);
                    txMap.put(TransferTrans.CR_PROD_ID, operativeMap.get("PROD_ID"));
                    txMap.put(TransferTrans.CR_AC_HD, acHeadMap.get("AC_HEAD"));
                    txMap.put(TransferTrans.CR_ACT_NUM, operativeMap.get("ACT_NUM"));
                    txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo()+":"+(String) debitMap.get("AC_HD_DESC"));
                    txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    Double principle = CommonUtil.convertObjToDouble(operativeMap.get("PRINCIPAL")).doubleValue();
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, principle);
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                    transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setRec_mode("SI");
                    transferTo.setTransAllId(objTO.getTransId());
                    if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                        transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                    }
                    TxTransferTO.add(transferTo);



                    System.out.println("in one creditttt 2222");
                    //CREDIT
                    HashMap acHeadMapInterest = new HashMap();
                    // acHeadMap.put("PROD_ID",operativeMap.get("PROD_ID"));
                    List lstInterest = (List) sqlMap.executeQueryForList("getAccountHeadProdForSAInterest", acHeadMapInterest);
                    if (lstInterest != null && lstInterest.size() > 0) {
                        acHeadMapInterest = (HashMap) lstInterest.get(0);
                    }




                    txMap = new HashMap();
                    transferTo = new TxTransferTO();
                    txMap.put(TransferTrans.CR_PROD_TYPE, prodType);
                    txMap.put(TransferTrans.CR_PROD_ID, operativeMap.get("PROD_ID"));
                    txMap.put(TransferTrans.CR_AC_HD, acHeadMapInterest.get("ACCOUNTHEAD_ID"));
                    txMap.put(TransferTrans.CR_ACT_NUM, operativeMap.get("ACT_NUM"));
                    txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo()+":"+(String) debitMap.get("AC_HD_DESC"));
                    txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    Double interest = CommonUtil.convertObjToDouble(operativeMap.get("INTEREST")).doubleValue();
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, interest);
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                    transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setRec_mode("SI");
                    transferTo.setTransAllId(objTO.getTransId());
                    if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                        transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                    }
                    TxTransferTO.add(transferTo);




                }







////                //CREDIT
////                txMap = new HashMap();
////                transferTo = new TxTransferTO();
////                txMap.put(TransferTrans.CR_PROD_TYPE, operativeMap.get("PROD_TYPE"));
////                txMap.put(TransferTrans.CR_PROD_ID, operativeMap.get("PROD_ID"));
////                txMap.put(TransferTrans.CR_AC_HD, acHeadMap.get("AC_HEAD"));
////                txMap.put(TransferTrans.CR_ACT_NUM, operativeMap.get("ACT_NUM"));
////                txMap.put(TransferTrans.PARTICULARS,transactionTO.getDebitAcctNo());
////                txMap.put(TransferTrans.CR_BRANCH,CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
////                txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
////                transferTo =  transactionDAO.addTransferCreditLocal(txMap, recoveredAmount) ;
////                transferTo.setTransId("-");
////                transferTo.setBatchId("-");
////                transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
////                transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
////                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
////                if(!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals(""))
////                    transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
////                TxTransferTO.add(transferTo);

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
                debitMap = null;
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
            String dbactnum = null;
            String dbprodid = null;
            dbactnum = CommonUtil.convertObjToStr(loanMap.get("DEBIT_ACT_NUM").toString());
            dbprodid = CommonUtil.convertObjToStr(loanMap.get("DEBIT_PROD_ID").toString());
            penalAmount = CommonUtil.convertObjToDouble(loanMap.get("PENAL")).doubleValue();
            chargesAmount = CommonUtil.convertObjToDouble(loanMap.get("CHARGES")).doubleValue();
            interestAmount = CommonUtil.convertObjToDouble(loanMap.get("INTEREST")).doubleValue();
            principalAmount = CommonUtil.convertObjToDouble(loanMap.get("PRINCIPAL")).doubleValue();
            recoveredAmount = CommonUtil.convertObjToDouble(loanMap.get("RECOVERED_AMOUNT")).doubleValue();

            //            System.out.println("############################### recoveredAmount : "+recoveredAmount);
            if (recoveredAmount > 0) {
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                transactionDAO.setInitiatedBranch(_branchCode);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);


                //                HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                //                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                //                if (transactionDetailsMap.size()>0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs"))
                //                    allowedTransDetailsTO = (LinkedHashMap)transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                //                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                transactionTO.setDebitAcctNo(dbactnum);
                transactionTO.setProductId(dbprodid);
                transactionTO.setProductType("OA");
                transactionTO.setBranchId(_branchCode);
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
                } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("OA")) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                    txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                    txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                }
                txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put("DR_INST_TYPE", "VOUCHER");
                txMap.put(TransferTrans.PARTICULARS, loanMap.get("ACT_NUM")+":"+(String) acHeadMap.get("AC_HEAD_DESC"));
                transferTo = transactionDAO.addTransferDebitLocal(txMap, recoveredAmount);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setRec_mode("SI");
                transferTo.setTransAllId(objTO.getTransId());
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
                txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo()+":"+(String) debitMap.get("AC_HD_DESC"));
                txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                transferTo = transactionDAO.addTransferCreditLocal(txMap, recoveredAmount);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setRec_mode("SI");
                transferTo.setTransAllId(objTO.getTransId());
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
                                        || chargeMap.get("CHARGE_TYPE").equals("EXECUTION DECREE CHARGES") || chargeMap.get("CHARGE_TYPE").equals("ARBITRARY CHARGES")) {
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
                authorizeMap.put("DEDUCTION_SI","DEDUCTION_SI");
                loanDataMap.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
                System.out.println("######## Before Transfer Dao loanDataMap : " + loanDataMap);
                HashMap transMap = transferDAO.execute(loanDataMap, false);
                loanDataMap = null;
                acHeadMap = null;
                loanMap = null;
                txMap = null;
                debitMap = null;
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }
    //added by rishad 14/05/2014
      private void transactionPartLoansAD(HashMap loanMap, HashMap map) throws Exception {
        try {
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
            String dbactnum = null;
            String dbprodid = null;
            dbactnum = CommonUtil.convertObjToStr(loanMap.get("DEBIT_ACT_NUM").toString());
            dbprodid = CommonUtil.convertObjToStr(loanMap.get("DEBIT_PROD_ID").toString());
            penalAmount = CommonUtil.convertObjToDouble(loanMap.get("PENAL")).doubleValue();
            chargesAmount = CommonUtil.convertObjToDouble(loanMap.get("CHARGES")).doubleValue();
            interestAmount = CommonUtil.convertObjToDouble(loanMap.get("INTEREST")).doubleValue();
            principalAmount = CommonUtil.convertObjToDouble(loanMap.get("PRINCIPAL")).doubleValue();
            recoveredAmount = CommonUtil.convertObjToDouble(loanMap.get("RECOVERED_AMOUNT")).doubleValue();

            //            System.out.println("############################### recoveredAmount : "+recoveredAmount);
            if (recoveredAmount > 0) {
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                transactionDAO.setInitiatedBranch(_branchCode);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);


                //                HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                //                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                //                if (transactionDetailsMap.size()>0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs"))
                //                    allowedTransDetailsTO = (LinkedHashMap)transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                //                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                transactionTO.setDebitAcctNo(dbactnum);
                transactionTO.setProductId(dbprodid);
                transactionTO.setProductType("OA");
                transactionTO.setBranchId(_branchCode);
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
                } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("OA")) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                    txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                    txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                }
                txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                //commented by rishad
               // txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put("DR_INST_TYPE", "VOUCHER");
                System.out.println("AD debit particular "+(String) acHeadMap.get("AC_HEAD_DESC"));
                txMap.put(TransferTrans.PARTICULARS, loanMap.get("ACT_NUM")+":"+(String) acHeadMap.get("AC_HEAD_DESC"));
                transferTo = transactionDAO.addTransferDebitLocal(txMap, recoveredAmount);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setRec_mode("SI");
                transferTo.setTransAllId(objTO.getTransId());
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
                txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo()+":"+(String) debitMap.get("AC_HD_DESC"));
                txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                //commented by rishad
                //txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                 System.out.println("AD debit particular "+(String) debitMap.get("AC_HD_DESC"));
                transferTo = transactionDAO.addTransferCreditLocal(txMap, recoveredAmount);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setRec_mode("SI");
                transferTo.setTransAllId(objTO.getTransId());
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
                                        || chargeMap.get("CHARGE_TYPE").equals("EXECUTION DECREE CHARGES") || chargeMap.get("CHARGE_TYPE").equals("ARBITRARY CHARGES")) {
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
                //commeneteed by rishad 14/042014
                HashMap authorizeMap = new HashMap();
                authorizeMap.put("BATCH_ID", null);
                authorizeMap.put("USER_ID", map.get("USER_ID"));
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                loanDataMap.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
           System.out.println("######## Before Transfer Dao loanDataMap : " + loanDataMap);
                HashMap transMap = transferDAO.execute(loanDataMap, false);
                loanDataMap = null;
                acHeadMap = null;
                loanMap = null;
                txMap = null;
                debitMap = null;
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
            String dbactnum = null;
            String dbprodid = null;
            dbactnum = CommonUtil.convertObjToStr(depositMap.get("DEBIT_ACT_NUM").toString());
            dbprodid = CommonUtil.convertObjToStr(depositMap.get("DEBIT_PROD_ID").toString());
            recoveredAmount = CommonUtil.convertObjToDouble(depositMap.get("RECOVERED_AMOUNT")).doubleValue();
            penalAmount = CommonUtil.convertObjToDouble(depositMap.get("PENAL")).doubleValue();
            //            System.out.println("############################### recoveredAmount : "+recoveredAmount);
            System.out.println("############################### penalAmount : " + penalAmount);
            if (recoveredAmount > 0) {
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                transactionDAO.setInitiatedBranch(_branchCode);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);

                //
                //                HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                //                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                //                if (transactionDetailsMap.size()>0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs"))
                //                    allowedTransDetailsTO = (LinkedHashMap)transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                //                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                //

                transactionTO.setDebitAcctNo(dbactnum);
                transactionTO.setProductId(dbprodid);
                transactionTO.setProductType("OA");

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
                txMap.put(TransferTrans.PARTICULARS, depositMap.get("ACT_NUM")+":"+(String) acHeadMap.get("AC_HEAD_DESC"));
                transferTo = transactionDAO.addTransferDebitLocal(txMap, recoveredAmount);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setRec_mode("SI");
                transferTo.setTransAllId(objTO.getTransId());
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
                txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo()+":"+(String) debitMap.get("AC_HD_DESC"));
                txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                transferTo = transactionDAO.addTransferCreditLocal(txMap, recoveredAmount);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setRec_mode("SI");
                transferTo.setTransAllId(objTO.getTransId());
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
                System.out.println("######## Before Transfer Dao depositDataMap : " + depositDataMap);
                HashMap transMap = transferDAO.execute(depositDataMap, false);
                depositDataMap = null;
                depositMap = null;
                acHeadMap = null;
                txMap = null;
                debitMap = null;
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
            System.out.println("inside mds ");
            HashMap MDSmap = new HashMap();
            HashMap bonusMap = new HashMap();
            HashMap whereMap = new HashMap();
            HashMap chittalDetailMap = new HashMap();
            HashMap installmentMap = new HashMap();
            TransactionTO transactionTO = new TransactionTO();
            LinkedHashMap allowedTransactionDetailsTO = null;
            LinkedHashMap transactionDetailsTO = null;
            LinkedHashMap deletedTransactionDetailsTO = null;
            String DELETED_TRANS_TOs = "DELETED_TRANS_TOs";
            String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
            String actNo = "";
            String subNo = "";
            String chittalNo = "";
            long count = 0;
            int curInsNo = 0;
            int noOfInsPaid = 0;
            long pendingInst = 0;
            long noOfInstPay = 0;
            double totBonusAmt = 0;
            double totPenalAmt = 0;
            double totNoticeAmt = 0;
            double totDiscountAmt = 0;
            double totArbitrationAmt = 0;
            String dbactnum = null;
            String dbprodid = null;
            dbactnum = CommonUtil.convertObjToStr(chittalMap.get("DEBIT_ACT_NUM").toString());
            dbprodid = CommonUtil.convertObjToStr(chittalMap.get("DEBIT_PROD_ID").toString());
            transactionTO.setDebitAcctNo(dbactnum);
            transactionTO.setProductId(dbprodid);
            transactionTO.setProductType("OA");
            transactionTO.setTransType("TRANSFER");
            transactionTO.setBranchId(_branchCode);
            System.out.println("transaction TO in mds" + transactionTO);
            allowedTransactionDetailsTO = new LinkedHashMap();
            allowedTransactionDetailsTO.put(String.valueOf(1), transactionTO);
            transactionDetailsTO = new LinkedHashMap();
            transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
            map.put("TransactionTO", transactionDetailsTO);
            allowedTransactionDetailsTO = null;

            //  }
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
                //
                whereMap.put("INT_CALC_UPTO_DT", setProperDtFormat(intUptoDt));
//                List bonusList = sqlMap.executeQueryForList("getRecoveryListInstallmentMapDetails", whereMap);
//                if (bonusList != null && bonusList.size() > 0) {
//                    for(int i=0; i<bonusList.size();i++){
//                        HashMap instMap = new HashMap();
//                        bonusMap = (HashMap) bonusList.get(i);
//                        totBonusAmt+=CommonUtil.convertObjToDouble(bonusMap.get("BONUS_AMT")).doubleValue();
//                        totPenalAmt+=CommonUtil.convertObjToDouble(bonusMap.get("PENAL_AMT")).doubleValue();
//                        totDiscountAmt+=CommonUtil.convertObjToDouble(bonusMap.get("DISCOUNT_AMT")).doubleValue();
//                        if(i==0){
//                            totNoticeAmt = CommonUtil.convertObjToDouble(bonusMap.get("NOTICE_AMT")).doubleValue();
//                            totArbitrationAmt = CommonUtil.convertObjToDouble(bonusMap.get("ARBITRATION_AMT")).doubleValue();
//                        }
//                        instMap.put("BONUS",bonusMap.get("BONUS_AMT"));
//                        instMap.put("DISCOUNT",bonusMap.get("DISCOUNT_AMT"));
//                        instMap.put("PENAL",bonusMap.get("PENAL_AMT"));
//                        instMap.put("INST_AMT",chittalDetailMap.get("INST_AMT"));
//                        installmentMap.put(CommonUtil.convertObjToStr(bonusMap.get("INSTALLMENT_NO")),instMap);
//                    }
//                }
                List insList = sqlMap.executeQueryForList("getNoOfInstalmentsPaid", whereMap);
                if (insList != null && insList.size() > 0) {
                    whereMap = (HashMap) insList.get(0);
                    noOfInsPaid = CommonUtil.convertObjToInt(whereMap.get("NO_OF_INST"));
                    count = CommonUtil.convertObjToLong(whereMap.get("NO_OF_INST"));
                }
                HashMap insDateMap = new HashMap();
                insDateMap.put("DIVISION_NO", CommonUtil.convertObjToStr(chittalDetailMap.get("DIVISION_NO")));
                insDateMap.put("SCHEME_NAME", chittalDetailMap.get("SCHEME_NAME"));
                insDateMap.put("CURR_DATE", setProperDtFormat(intUptoDt));
                insDateMap.put("ADD_MONTHS", "-1");
                List insDateLst = sqlMap.executeQueryForList("getTransAllMDSCurrentInsDate", insDateMap);
                if (insDateLst != null && insDateLst.size() > 0) {
                    insDateMap = (HashMap) insDateLst.get(0);
                    curInsNo = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO"));
                    pendingInst = curInsNo - noOfInsPaid;
                    if (pendingInst < 0) {
                        pendingInst = 0;
                    }
                    if (pendingInst > 0) {
                        noOfInstPay = pendingInst;
                    }
                    System.out.println("######## NoOfInstToPay *** : " + noOfInstPay);
                }
                //Narration
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
                int noInstPay = CommonUtil.convertObjToInt(String.valueOf(noOfInstPay));
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
                System.out.println("#$#$# narration :" + narration);
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
                mdsReceiptEntryTO.setPaidDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalMap.get("INT_CALC_UPTO_DT"))));
                mdsReceiptEntryTO.setNoOfInst(CommonUtil.convertObjToInt(chittalDetailMap.get("NO_OF_INSTALLMENTS")));
                mdsReceiptEntryTO.setCurrInst(CommonUtil.convertObjToDouble(String.valueOf(curInsNo)));
                mdsReceiptEntryTO.setPendingInst(CommonUtil.convertObjToDouble(String.valueOf(pendingInst)));
                mdsReceiptEntryTO.setInstAmt(CommonUtil.convertObjToDouble(chittalDetailMap.get("INST_AMT")));
                mdsReceiptEntryTO.setNoOfInstPay(CommonUtil.convertObjToInt(String.valueOf(noOfInstPay)));
                mdsReceiptEntryTO.setInstAmtPayable(CommonUtil.convertObjToDouble(chittalMap.get("PRINCIPAL")));
                mdsReceiptEntryTO.setPaidInst(CommonUtil.convertObjToDouble(String.valueOf(noOfInsPaid)));
                mdsReceiptEntryTO.setBonusAmtPayable(CommonUtil.convertObjToDouble(chittalMap.get("BONUS")));
                mdsReceiptEntryTO.setDiscountAmt(CommonUtil.convertObjToDouble(String.valueOf(totDiscountAmt)));
                // mdsReceiptEntryTO.setPenalAmtPayable(CommonUtil.convertObjToDouble(String.valueOf(totPenalAmt)));
                mdsReceiptEntryTO.setPenalAmtPayable(CommonUtil.convertObjToDouble(chittalMap.get("PENAL")));
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
                mdsReceiptEntryTO.setThalayal("N");
                mdsReceiptEntryTO.setMunnal("N");
                mdsReceiptEntryTO.setMemberChanged("N");
                mdsReceiptEntryTO.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                mdsReceiptEntryTO.setPrizedMember(CommonUtil.convertObjToStr(chittalMap.get("PRIZED_MEMBER")));
                mdsReceiptEntryTO.setBonusAmtAvail(CommonUtil.convertObjToDouble(chittalMap.get("BONUS")));
                mdsReceiptEntryTO.setTotalInstDue(CommonUtil.convertObjToDouble(chittalMap.get("INST_AMT_PAYABLE")));
                mdsReceiptEntryTO.setChangedInstNo(CommonUtil.convertObjToDouble(String.valueOf("0")));
                System.out.println("############################### mdsReceiptEntryTO : " + mdsReceiptEntryTO);
                System.out.println("############################### installmentMap : " + installmentMap);
                MDSmap.put("INSTALLMENT_MAP", chittalMap.get("INSTALLMENT_MAP"));
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
                MDSmap.put("REC_MODE", "SI");
                MDSmap.put("TRANS_ALL_ID", objTO.getTransId());
                System.out.println("########### MDSmap : " + MDSmap);
                mdsReceiptEntryDAO = new MDSReceiptEntryDAO();
                HashMap transMap = new HashMap();
                transMap = mdsReceiptEntryDAO.execute(MDSmap, false);                    // INSERT_TRANSACTION
                System.out.println("################# transMap : " + transMap);

                //AUTHORIZE_START
                HashMap authorizeMap = new HashMap();
                authorizeMap.put("NET_TRANS_ID", transMap.get("BATCH_ID"));
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                authorizeMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                authorizeMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                mdsReceiptEntryTO.setCommand("");
                System.out.println("authorizeMap" + authorizeMap);
                MDSmap.put("AUTHORIZEMAP", authorizeMap);
                MDSmap.put("mdsReceiptEntryTO", mdsReceiptEntryTO);
                MDSmap.put("SCHEME_NAME", CommonUtil.convertObjToStr(chittalDetailMap.get("SCHEME_NAME")));
                MDSmap.put("USER_ID", map.get(CommonConstants.USER_ID));
                System.out.println("########### MDSmap : " + MDSmap);
                mdsReceiptEntryDAO = new MDSReceiptEntryDAO();
                mdsReceiptEntryDAO.execute(MDSmap, false);                                     // AUTHORIZE_TRANSACTION
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        System.out.println("obj -----------------=========" + obj);
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        intUptoDt = currDt;
        interestUptoDt = intUptoDt;
        return getData(obj);
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("map ((((9999999======" + map);
        System.out.println("branchCode......." + _branchCode);
        HashMap singleRowMap = new HashMap();
        HashMap returnMap = new HashMap();
        HashMap where = (HashMap) map.get(CommonConstants.MAP_WHERE);
        // HashMap data=(HashMap) map.get("DATA");
        System.out.println("where 00========" + where);
        System.out.println("wmapmapmap9888777==" + map.get("MEMBER_NO"));
        List list = null;
        String prodtype = map.get("PRODTYPE").toString();
        if (prodtype.equalsIgnoreCase("TL") || prodtype.equalsIgnoreCase("AD")) {
            try {
                sqlMap.startTransaction();

                String prodType = "";
                String prodID = "";
                String actNum = "";

                prodType = CommonUtil.convertObjToStr(map.get("PRODTYPE"));
                prodID = CommonUtil.convertObjToStr(map.get("PROD_ID"));
                actNum = CommonUtil.convertObjToStr(map.get("ACT_NUM"));
                singleRowMap = calcLoanPayments(actNum, prodID, prodType, "N");
                recoveryList = new ArrayList();
                if (singleRowMap != null && singleRowMap.size() > 0) {
                    recoveryRowList = new ArrayList();
                    recoveryRowList.add(CommonUtil.convertObjToStr(map.get("PRODTYPE")));
                    recoveryRowList.add(CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                    recoveryRowList.add("");
                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("PRINCIPAL")));
                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("PENAL")));
                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("INTEREST")));
                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("CHARGES")));
                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("CLEAR_BALANCE")));
                    recoveryList.add(recoveryRowList);
                    total = Double.parseDouble(singleRowMap.get("PRINCIPAL").toString()) + Double.parseDouble(singleRowMap.get("PENAL").toString()) + Double.parseDouble(singleRowMap.get("INTEREST").toString()) + Double.parseDouble(singleRowMap.get("CHARGES").toString());
                    returnMap.put("TOTAL", total);
                    System.out.println("TOTAL IN TL...######" + total);
                    returnMap.put("RECOVERY_LIST_TABLE_DATA", recoveryList);
                    returnMap.put("DATAMAP", singleRowMap);
                }
                sqlMap.commitTransaction();
            } catch (Exception e) {
                try {
                    sqlMap.rollbackTransaction();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                e.printStackTrace();
                // throw e;
            }

        } else if (prodtype.equalsIgnoreCase("TD")) {
            try {
                sqlMap.startTransaction();
                String prodType = "";
                String prodID = "";
                String actNum = "";
                singleRowMap = new HashMap();
                prodType = CommonUtil.convertObjToStr(map.get("PRODTYPE"));
                prodID = CommonUtil.convertObjToStr(map.get("PROD_ID"));
                actNum = CommonUtil.convertObjToStr(map.get("ACT_NUM"));
                singleRowMap = calcTermDeposits(actNum, prodID, prodType);
                recoveryList = new ArrayList();
                if (singleRowMap != null && singleRowMap.size() > 0) {
                    recoveryRowList = new ArrayList();
                    recoveryRowList.add(CommonUtil.convertObjToStr(map.get("PRODTYPE")));
                    recoveryRowList.add(CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                    recoveryRowList.add("");
                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("PRINCIPAL")));
                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("PENAL")));
                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("INTEREST")));
                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("CHARGES")));
                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("CLEAR_BALANCE")));
                    // recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("DELAY")));//total
                    recoveryList.add(recoveryRowList);
                    total = Double.parseDouble(singleRowMap.get("PRINCIPAL").toString()) + Double.parseDouble(singleRowMap.get("PENAL").toString()) + Double.parseDouble(singleRowMap.get("INTEREST").toString()) + Double.parseDouble(singleRowMap.get("CHARGES").toString());
                    returnMap.put("TOTAL", total);
                    System.out.println("TOTAL IN TD...######" + total);
                    returnMap.put("RECOVERY_LIST_TABLE_DATA", recoveryList);
                    returnMap.put("DATAMAP", singleRowMap);
                }
                sqlMap.commitTransaction();
            } catch (Exception e) {
                try {
                    sqlMap.rollbackTransaction();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                e.printStackTrace();
                // throw e;
            }

        } else if (prodtype.equalsIgnoreCase("MDS")) {
            try {
                sqlMap.startTransaction();
                System.out.println("inside mds asd");
                String prodType = "";
                String prodID = "";
                String actNum = "";
                singleRowMap = new HashMap();
                prodType = CommonUtil.convertObjToStr(map.get("PRODTYPE"));
                prodID = CommonUtil.convertObjToStr(map.get("PROD_ID"));
                actNum = CommonUtil.convertObjToStr(map.get("ACT_NUM"));
                System.out.println("actNum==" + actNum + " prodID =" + prodID + " prodType==" + prodType);
                recoveryList = new ArrayList();
                singleRowMap = calcMDS(actNum, prodID, prodType);
                System.out.println("singleRowMap IN MSDA ====================" + singleRowMap);
                if (singleRowMap != null && singleRowMap.size() > 0) {
                    recoveryRowList = new ArrayList();
                    recoveryRowList.add(CommonUtil.convertObjToStr(map.get("PRODTYPE")));
                    recoveryRowList.add(CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                    recoveryRowList.add("");
                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("PRINCIPAL")));
                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("PENAL")));
                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("INTEREST")));
                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("CHARGES")));
                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("CLEAR_BALANCE")));
                    recoveryList.add(recoveryRowList);
                    total = Double.parseDouble(singleRowMap.get("PRINCIPAL").toString()) + Double.parseDouble(singleRowMap.get("PENAL").toString()) + Double.parseDouble(singleRowMap.get("INTEREST").toString()) + Double.parseDouble(singleRowMap.get("CHARGES").toString());
                    returnMap.put("TOTAL", total);
                    System.out.println("TOTAL IN MDS...######" + total);
                    returnMap.put("RECOVERY_LIST_TABLE_DATA", recoveryList);
                    returnMap.put("DATAMAP", singleRowMap);
                }
                sqlMap.commitTransaction();
            } catch (Exception e) {
                try {
                    sqlMap.rollbackTransaction();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                e.printStackTrace();
                // throw e;
            }

        } else if (prodtype.equalsIgnoreCase("SA")) {
            try {
                sqlMap.startTransaction();
                Double penal = 0.0;
                String penaln = "";
                String prodType = "";
                String prodID = "";
                String actNum = "";
                System.out.println("in sb....map is =" + map);
                singleRowMap = new HashMap();
                HashMap newMap = new HashMap();
                HashMap lstDueMap = new HashMap();
                prodType = CommonUtil.convertObjToStr(map.get("PRODTYPE"));
                prodID = CommonUtil.convertObjToStr(map.get("PROD_ID"));
                actNum = CommonUtil.convertObjToStr(map.get("ACT_NUM"));
                HashMap where1 = new HashMap();
                HashMap map1 = new HashMap();
                //map1.put("BRANCH_CODE",);


//                Calendar cal = null;
//
//                cal = Calendar.getInstance();
//                cal.setTime(currDt);
//                cal.add(Calendar.MONTH, 0);
//                cal.set(Calendar.DATE, 1);
//                Date currDt1 = cal.getTime();
//                System.out.println("currDt1..." + currDt1);
//                //  java.sql.Date curDt = new java.sql.Date(currDt1.getTime());
//
//                System.out.println("curDt");
//
//
//

                map1.put("CLOCK_NO", actNum);
//                map1.put("AUTHORIZED_DT", setProperDtFormat(currDt1));
                map1.put("AUTHORIZED_DT", currDt.clone());
                System.out.println("map1......." + map1);
                List aList = sqlMap.executeQueryForList("GetSuspenseDuedetails3", map1);
                System.out.println("in sb....list is is =/" + aList + "size is" + aList.size());
                double glAmount = 0.0;
                int billid = 0;
                if (aList != null && aList.size() > 0) {
                    for (int i = 0; i < aList.size(); i++) {
                        newMap = (HashMap) aList.get(i);
                        System.out.println("in sb....newmap is =" + newMap + "alist size is" + aList.size());
                        glAmount += CommonUtil.convertObjToDouble(newMap.get("AMT"));
//                        billid = Integer.parseInt(newMap.get("BILL_ID").toString());
                    }
                    singleRowMap = calcGL(actNum, prodID, prodType, glAmount);
                    System.out.println("getData singleRowMap : "+singleRowMap +"SA actNum : "+actNum);
                    recoveryList = new ArrayList();
                    if (singleRowMap != null && singleRowMap.size() > 0) {
                        recoveryRowList = new ArrayList();
                        recoveryRowList.add(CommonUtil.convertObjToStr(map.get("PRODTYPE")));
                        recoveryRowList.add(CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                        recoveryRowList.add("");
                        recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("PRINCIPAL")));
                        recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("PENAL")));
                        recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("INTEREST")));
                        recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("CHARGES")));
                        recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("CLEAR_BALANCE")));
                        recoveryList.add(recoveryRowList);
                        total = Double.parseDouble(singleRowMap.get("PRINCIPAL").toString()) + Double.parseDouble(singleRowMap.get("PENAL").toString()) + Double.parseDouble(singleRowMap.get("INTEREST").toString()) + Double.parseDouble(singleRowMap.get("CHARGES").toString());
                        returnMap.put("TOTAL", total);
                        returnMap.put("BILL_ID", billid);
                        System.out.println("TOTAL IN sb...######" + total);
                        returnMap.put("RECOVERY_LIST_TABLE_DATA", recoveryList);
                        returnMap.put("DATAMAP", singleRowMap);
                    }

                }
                sqlMap.commitTransaction();
            } catch (Exception e) {
                try {
                    sqlMap.rollbackTransaction();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                e.printStackTrace();
                // throw e;
            }

        }
        System.out.println("getData returnMap : "+returnMap );
        return returnMap;
    }

    public static double monthsBetween(Date date1, Date date2) {
        Rounding rod = new Rounding();
        System.out.println("(date2.getTime() - date1.getTime())" + (date2.getTime() - date1.getTime()));
        System.out.println("AVERAGE_MILLIS_PER_MONTH" + AVERAGE_MILLIS_PER_MONTH);
        System.out.println("bbb" + (int) ((date2.getTime() - date1.getTime()) / AVERAGE_MILLIS_PER_MONTH));
        double d = ((date2.getTime() - date1.getTime()) / AVERAGE_MILLIS_PER_MONTH);
        System.out.println("dateeee.." + (int) rod.getNearest((long) (d * 100), 100) / 100);
        return (int) rod.getNearest((long) (d * 100), 100) / 100;
    }

    public static double daysBetween(Date date1, Date date2) {
        double diffDays = 0.0;
        try {
//			d1 = format.parse(dateStart);
//			d2 = format.parse(dateStop);

            //in milliseconds

            double diff = date2.getTime() - date1.getTime();
            diffDays = diff / (24 * 60 * 60 * 1000);

            System.out.print(diffDays + " days, ");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return diffDays;
    }

    public HashMap calcMDS(String chittal_No, String schemeName, String prodType) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap whereMap = new HashMap();
        HashMap productMap = new HashMap();
        HashMap installmentMap = new HashMap();
        String calculateIntOn = "";
        String chittalNo = "";
        String subNo = "";
        if (chittal_No.indexOf("_") != -1) {
            chittalNo = chittal_No.substring(0, chittal_No.indexOf("_"));
            subNo = chittal_No.substring(chittal_No.indexOf("_") + 1, chittal_No.length());
        }
        dataMap.put("CHITTAL_NO", chittal_No);
        dataMap.put("SCHEME_NAME", schemeName);
        System.out.println("ANEEZ********chittal_No" + chittal_No + " SubNo:" + subNo);
        System.out.println("ANEEZ********SCHEMENAME" + dataMap.get("SCHEME_NAME"));
        double insAmt = 0.0;
        long pendingInst = 0;
        int divNo = 0;
        long insDueAmt = 0;
        int noOfInsPaid = 0;
        int instDay = 1;
        boolean prized = false;
        int curInsNo = 0;
        Date instDate = null;
        boolean bonusAvailabe = true;
        ArrayList penalList = new ArrayList();
        ArrayList bonusList = new ArrayList();
        ArrayList discountList = new ArrayList();

        long noOfInstPay = 0;
        int totIns = 0;
        Date startDate = null;
        Date insDate = null;
        int startMonth = 0;
        int insMonth = 0;
        whereMap.put("SCHEME_NAME", dataMap.get("SCHEME_NAME"));
        whereMap.put("CHITTAL_NO", chittalNo);
        whereMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
        System.out.println("  ********chittal_No:" + chittal_No + " Sun BNo:" + subNo + "SCH NAME=====" + dataMap.get("SCHEME_NAME"));
        List lst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", whereMap);
        System.out.println("Aneez List::---->" + lst);
        String bonusFirstInst = "";
        if (lst != null && lst.size() > 0) {
            productMap = (HashMap) lst.get(0);
            insAmt = CommonUtil.convertObjToDouble(productMap.get("INSTALLMENT_AMOUNT")).doubleValue();
            totIns = CommonUtil.convertObjToInt(productMap.get("NO_OF_INSTALLMENTS"));
            startDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_START_DT")));
            insDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_START_DT")));
            startMonth = insDate.getMonth();
            //            bonusFirstInst = CommonUtil.convertObjToStr(productMap.get("BONUS_FIRST_INSTALLMENT"));
            bonusFirstInst = CommonUtil.convertObjToStr(productMap.get("ADVANCE_COLLECTION"));
        }
        Date endDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_END_DT")));
        int startNoForPenal = 0;
        int addNo = 1;
        int firstInst_No = -1;
        if (bonusFirstInst.equals("Y")) {
            startNoForPenal = 1;
            addNo = 0;
            firstInst_No = 0;
        }
        List insList = sqlMap.executeQueryForList("getNoOfInstalmentsPaid", whereMap);
        if (insList != null && insList.size() > 0) {
            whereMap = (HashMap) insList.get(0);
            noOfInsPaid = CommonUtil.convertObjToInt(whereMap.get("NO_OF_INST"));
        }
        HashMap chittalMap = new HashMap();
        chittalMap.put("CHITTAL_NO", chittalNo);
        chittalMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
        List chitLst = sqlMap.executeQueryForList("getSelctApplnReceiptDetails", chittalMap);
        if (chitLst != null && chitLst.size() > 0) {
            chittalMap = (HashMap) chitLst.get(0);
            instDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
            divNo = CommonUtil.convertObjToInt(chittalMap.get("DIVISION_NO"));
        }
        boolean notPaidThisMonth = false;
        HashMap mdsMap = new HashMap();
        mdsMap.put("CHITTAL_NO", chittalNo);
        List depositMaxList = sqlMap.executeQueryForList("getSelectMDSMaxtransDt", mdsMap);
        if (depositMaxList != null && depositMaxList.size() > 0) {
            mdsMap = (HashMap) depositMaxList.get(0);
            Date date = (Date) currDt.clone();
            Date maxDate = getProperDateFormat(CommonUtil.convertObjToStr(mdsMap.get("TRANS_DT")));
            int currentMonth = (int)date.getMonth()+1;
            int maxTxnMonth = (int)maxDate.getMonth()+1;
            int currentYear = (int)date.getYear()+1900;
            int maxTxnYear = (int)maxDate.getYear()+1900;
            System.out.println("curr date month : " + currentMonth + " last transaction maxDate : " + maxTxnMonth +" curr year : " + currentYear + " last transaction year : " + maxTxnYear);
            if ((maxTxnMonth != currentMonth) && (maxTxnYear == currentYear)) {
                notPaidThisMonth = true;
                System.out.println("Please Continue make payment nextmonth : " + chittalNo);
            }else{
                notPaidThisMonth = false;
            }
        //}else{
        //    notPaidThisMonth = true;
        }
        HashMap insDateMap = new HashMap();
        System.out.println("DIVISION_NO In===" + CommonUtil.convertObjToStr(String.valueOf(divNo)));
        System.out.println("SCE NAME In===" + dataMap.get("SCHEME_NAME"));
        System.out.println("PROPER BDATE In===" + setProperDtFormat(intUptoDt));
        insDateMap.put("DIVISION_NO", CommonUtil.convertObjToStr(String.valueOf(divNo)));
        insDateMap.put("SCHEME_NAME", dataMap.get("SCHEME_NAME"));
        insDateMap.put("CURR_DATE", setProperDtFormat(intUptoDt));
        insDateMap.put("ADD_MONTHS", "-1");
        List insDateLst = sqlMap.executeQueryForList("getTransAllMDSCurrentInsDate", insDateMap);
        if (insDateLst != null && insDateLst.size() > 0) {
            insDateMap = (HashMap) insDateLst.get(0);
            curInsNo = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO"));
            pendingInst = curInsNo - noOfInsPaid;
            if (pendingInst < 0) {
                pendingInst = 0;
            }
            insMonth = startMonth + curInsNo;
            insDate.setMonth(insMonth);
            if (pendingInst > 0) {
                noOfInstPay = pendingInst;
            }
            System.out.println("######## NoOfInstToPay *** : " + noOfInstPay);
        }
        if(noOfInstPay>0){
            notPaidThisMonth = true;
        }
        if (noOfInstPay > 0 && notPaidThisMonth) {        
            HashMap prizedMap = new HashMap();
            prizedMap.put("SCHEME_NAME", dataMap.get("SCHEME_NAME"));
            prizedMap.put("DIVISION_NO", String.valueOf(divNo));
            prizedMap.put("CHITTAL_NO", dataMap.get("CHITTAL_NO"));
            prizedMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
            lst = sqlMap.executeQueryForList("getSelectPrizedDetailsEntryRecords", prizedMap);
            if (lst != null && lst.size() > 0) {
                prizedMap = (HashMap) lst.get(0);
                if (prizedMap.get("DRAW") != null && !prizedMap.get("DRAW").equals("") && prizedMap.get("DRAW").equals("Y")) {
                    prized = true;
                }
                if (prizedMap.get("AUCTION") != null && !prizedMap.get("AUCTION").equals("") && prizedMap.get("AUCTION").equals("Y")) {
                    prized = true;
                }
            } else {
                prized = false;
            }
            long totDiscAmt = 0;
            long penalAmt = 0;
            double netAmt = 0;
            double insAmtPayable = 0;
            double totBonusAmt = 0;
            double bonusAmt = 0;
            String penalIntType = "";
            long penalValue = 0;
            String penalGraceType = "";
            long penalGraceValue = 0;
            String penalCalcBaseOn = "";
            long diffDayPending = 0;

            if (pendingInst > 0) {              //pending installment calculation starts...
                insDueAmt = (long) insAmt * pendingInst;
                double calc = 0;
                long totInst = pendingInst;
                penalCalcBaseOn = CommonUtil.convertObjToStr(productMap.get("PENAL_CALC"));
                if (prized == true) {
                    if (productMap.containsKey("PENEL_PZ_INT_FULL_AMT_INST_AMT") && productMap.get("PENEL_PZ_INT_FULL_AMT_INST_AMT") != null) {
                        calculateIntOn = CommonUtil.convertObjToStr(productMap.get("PENEL_PZ_INT_FULL_AMT_INST_AMT"));
                    }
                    penalIntType = CommonUtil.convertObjToStr(productMap.get("PENAL_PRIZED_INT_TYPE"));
                    penalValue = CommonUtil.convertObjToLong(productMap.get("PENAL_PRIZED_INT_AMT"));
                    penalGraceType = CommonUtil.convertObjToStr(productMap.get("PENAL_PRIZED_GRACE_PERIOD_TYPE"));
                    penalGraceValue = CommonUtil.convertObjToLong(productMap.get("PENAL_PRIZED_GRACE_PERIOD"));
                } else if (prized == false) {
                    if (productMap.containsKey("PENEL_INT_FULL_AMT_INST_AMT") && productMap.get("PENEL_INT_FULL_AMT_INST_AMT") != null) {
                        calculateIntOn = CommonUtil.convertObjToStr(productMap.get("PENEL_INT_FULL_AMT_INST_AMT"));
                    }
                    penalIntType = CommonUtil.convertObjToStr(productMap.get("PENAL_INT_TYPE"));
                    penalValue = CommonUtil.convertObjToLong(productMap.get("PENAL_INT_AMT"));
                    penalGraceType = CommonUtil.convertObjToStr(productMap.get("PENAL_GRACE_PERIOD_TYPE"));
                    penalGraceValue = CommonUtil.convertObjToLong(productMap.get("PENAL_GRACE_PERIOD"));
                }
                List bonusAmout = new ArrayList();
                if (calculateIntOn != null && calculateIntOn.equals("Installment Amount")) {
                    //double instAmount = 0.0;
                    HashMap nextInstMaps = null;
                    for (int i = startNoForPenal; i <= noOfInstPay - addNo; i++) {
                        nextInstMaps = new HashMap();
                        nextInstMaps.put("SCHEME_NAME", dataMap.get("SCHEME_NAME"));
                        nextInstMaps.put("DIVISION_NO", divNo);
                        nextInstMaps.put("SL_NO", new Double(i + noOfInsPaid + addNo + firstInst_No));
                        List listRec = sqlMap.executeQueryForList("getSelectBonusAndNextInstDateWithoutDivision", nextInstMaps);
                        if (listRec != null && listRec.size() > 0) {
                            nextInstMaps = (HashMap) listRec.get(0);
                        }
                        if (nextInstMaps != null && nextInstMaps.containsKey("NEXT_BONUS_AMOUNT")) {
                            bonusAmout.add(CommonUtil.convertObjToDouble(nextInstMaps.get("NEXT_BONUS_AMOUNT")));
                        } else {
                            bonusAmout.add(CommonUtil.convertObjToDouble(0));
                        }
                    }
                }
                System.out.println("Nidhin List:" + bonusAmout);
                for (int j = startNoForPenal; j < noOfInstPay + startNoForPenal; j++) {
                    if (calculateIntOn != null && calculateIntOn.equals("Installment Amount")) {
                        insAmt = 0.0;
                        double instAmount = CommonUtil.convertObjToDouble(productMap.get("INSTALLMENT_AMOUNT"));
                        if (bonusAmout != null && bonusAmout.size() > 0) {
                            instAmount -= CommonUtil.convertObjToDouble(bonusAmout.get(j - 1));
                        }
                        insAmt = instAmount;
                    }
                    HashMap nextInstMap = new HashMap();
                    nextInstMap.put("SCHEME_NAME", dataMap.get("SCHEME_NAME"));
                    nextInstMap.put("DIVISION_NO", String.valueOf(divNo));
                    nextInstMap.put("SL_NO", new Double(j + noOfInsPaid));
                    List listRec = sqlMap.executeQueryForList("getSelectNextInstDate", nextInstMap);
                    if (listRec != null && listRec.size() > 0) {
                        double penal = 0;
                        nextInstMap = (HashMap) listRec.get(0);
                        instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                        //                    diffDayPending = DateUtil.dateDiff(instDate, currDt);
                        if (instDay > 0) {
                            instDate.setDate(instDate.getDate() + instDay - 1);
                        }
                        diffDayPending = DateUtil.dateDiff(instDate, intUptoDt);
                        System.out.println("First #########diffDay : " + diffDayPending + "### instDate : " + instDate + "####  intUptoDt : " + intUptoDt);
                        //Holiday Checking - Added By Suresh
                        HashMap holidayMap = new HashMap();
                        boolean checkHoliday = true;
                        System.out.println("instDate   " + instDate);
                        instDate = setProperDtFormat(instDate);
                        holidayMap.put("NEXT_DATE", instDate);
                        holidayMap.put("BRANCH_CODE", _branchCode);
                        while (checkHoliday) {
                            boolean tholiday = false;
                            List Holiday = sqlMap.executeQueryForList("checkHolidayDateOD", holidayMap);
                            List weeklyOf = sqlMap.executeQueryForList("checkWeeklyOffOD", holidayMap);
                            boolean isHoliday = Holiday.size() > 0 ? true : false;
                            boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
                            if (isHoliday || isWeekOff) {
                                System.out.println("#### diffDayPending Holiday True : " + diffDayPending);
                                if (CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")) {
                                    diffDayPending -= 1;
                                    instDate.setDate(instDate.getDate() + 1);
                                } else {
                                    diffDayPending += 1;
                                    instDate.setDate(instDate.getDate() - 1);
                                }
                                holidayMap.put("NEXT_DATE", instDate);
                                checkHoliday = true;
                                System.out.println("#### holidayMap : " + holidayMap);
                            } else {
                                System.out.println("#### diffDay Holiday False : " + diffDayPending);
                                checkHoliday = false;
                            }
                        }
                        System.out.println("#### diffDayPending Final : " + diffDayPending);




                        if (penalCalcBaseOn != null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Days")) {
                            if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Days")) {
                                if (diffDayPending > penalGraceValue) {
                                    if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                        calc += (diffDayPending * penalValue * insAmt) / 36500;
                                    } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                        calc += penalValue;
                                    }
                                }
                            } else if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Installments")) {
                                // To be written
                                if (diffDayPending > penalGraceValue) {
                                    if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                        //                                                calc = insAmt*(((pendingInst+1)*pendingInst/2)*penalValue)/1200;
                                        calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                    } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                        calc += penalValue;
                                    }
                                }
                            }
                        } else if (penalCalcBaseOn != null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Installments")) {
                            if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Days")) {
                                if (diffDayPending > penalGraceValue) {
                                    if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                        calc += ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                    } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute") && totInst <= noOfInstPay) {
                                        calc += penalValue;
                                    }
                                }
                            } else if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Installments")) {
                                // To be written
                                if (diffDayPending > penalGraceValue) {
                                    if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                        //                                                calc = insAmt*(((pendingInst+1)*pendingInst/2)*penalValue)/1200;
                                        calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                    } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                        calc += penalValue;
                                    }
                                }
                            }
                        }
                        //After Scheme End Date Penal Calculating
                        if ((j + 1 == noOfInstPay + startNoForPenal) && !(penalCalcBaseOn.equals("Days") && penalIntType.equals("Percent")) && (DateUtil.dateDiff(endDate, intUptoDt) > 0)) {
                            System.out.println("#### endDate : " + endDate);
                            if (penalIntType.equals("Percent")) {
                                diffDayPending = DateUtil.dateDiff(endDate, intUptoDt);
                                System.out.println("#### endDate_diffDayPending : " + diffDayPending);
                                calc += (double) ((((insAmt * noOfInstPay * penalValue) / 100.0) * diffDayPending) / 365);
                            }
                            // Absolute Not Required...
                        }
                        penal = (long) (calc + 0.5) - penal;
                        penalList.add(String.valueOf(penal));
                        installmentMap.put("PENAL", penalList);
                        penal = calc + 0.5;

                    }
                }
                if (calc > 0) {
                    penalAmt = (long) (calc + 0.5);
                }
            }//pending installment calculation ends...
            //Discount calculation details Starts...
            for (int k = 0; k < noOfInstPay; k++) {
                HashMap nextInstMap = new HashMap();
                nextInstMap.put("SCHEME_NAME", dataMap.get("SCHEME_NAME"));
                nextInstMap.put("DIVISION_NO", String.valueOf(divNo));
                nextInstMap.put("SL_NO", new Double(k + noOfInsPaid));
                List listRec = sqlMap.executeQueryForList("getSelectNextInstDate", nextInstMap);
                if (listRec == null || listRec.isEmpty()) {
                    Date curDate = (Date) currDt.clone();
                    int curMonth = curDate.getMonth();
                    System.out.println("@#$$#$#instDay" + instDay);
                    curDate.setMonth(curMonth + 1);
                    curDate.setDate(instDay);
                    listRec = new ArrayList();
                    nextInstMap.put("NEXT_INSTALLMENT_DATE", curDate);
                    listRec.add(nextInstMap);
                }
                if (listRec != null && listRec.size() > 0) {
                    nextInstMap = (HashMap) listRec.get(0);
                    instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                    //                long diffDay = DateUtil.dateDiff(instDate, currDt);
                    long diffDay = DateUtil.dateDiff(instDate, intUptoDt);
                    //                System.out.println("First #########diffDay : " + diffDay);
                    String hoildayInt = CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT"));
                    if (productMap.get("BONUS_ALLOWED") != null && !productMap.get("BONUS_ALLOWED").equals("") && (productMap.get("BONUS_ALLOWED").equals("Y")
                            || productMap.get("BONUS_ALLOWED").equals("N"))) {


                        String discount = CommonUtil.convertObjToStr(productMap.get("DISCOUNT_ALLOWED"));
                        if (discount != null && !discount.equals("") && discount.equals("Y")) {
                            String discountType = CommonUtil.convertObjToStr(productMap.get("DISCOUNT_RATE_TYPE"));
                            long discountValue = CommonUtil.convertObjToLong(productMap.get("DISCOUNT_RATE_AMT"));
                            if (prized == true) {//discount calculation for prized prerson...
                                String discountPrizedDays = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_DAYS"));
                                String discountPrizedMonth = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_MONTHS"));
                                String discountPrizedAfter = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_AFTER"));
                                String discountPrizedEnd = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_END"));
                                long discountPrizedValue = CommonUtil.convertObjToLong(productMap.get("DIS_PRIZED_GRACE_PERIOD"));
                                if (discountPrizedDays != null && !discountPrizedDays.equals("") && discountPrizedDays.equals("D") && diffDay <= discountPrizedValue) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        if (diffDay <= discountPrizedValue) {
                                            totDiscAmt = totDiscAmt + calc;
                                        }
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        if (diffDay <= discountPrizedValue) {
                                            totDiscAmt = totDiscAmt + discountValue;
                                        }
                                    }
                                } else if (discountPrizedMonth != null && !discountPrizedMonth.equals("") && discountPrizedMonth.equals("M") && diffDay <= (discountPrizedValue * 30)) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        totDiscAmt = totDiscAmt + calc;
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        totDiscAmt = totDiscAmt + discountValue;
                                    }
                                } else if (discountPrizedAfter != null && !discountPrizedAfter.equals("") && discountPrizedAfter.equals("A") && intUptoDt.getDate() <= discountPrizedValue) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        totDiscAmt = totDiscAmt + calc;
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        totDiscAmt = totDiscAmt + discountValue;
                                    }
                                } else if (discountPrizedEnd != null && !discountPrizedEnd.equals("") && discountPrizedEnd.equals("E") && pendingInst < noOfInstPay) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        totDiscAmt = totDiscAmt + calc;
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        totDiscAmt = totDiscAmt + discountValue;
                                    }
                                } else {
                                    totDiscAmt = 0;
                                }
                            } else if (prized == false) {//discount calculation non prized person...
                                String discountGraceDays = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_DAYS"));
                                String discountGraceMonth = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_MONTHS"));
                                String discountGraceAfter = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_AFTER"));
                                String discountGraceEnd = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_END"));
                                long discountGraceValue = CommonUtil.convertObjToLong(productMap.get("DIS_GRACE_PERIOD"));
                                if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("D")) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        if (diffDay <= discountGraceValue) {
                                            totDiscAmt = totDiscAmt + calc;
                                        }
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        if (diffDay <= discountGraceValue) {
                                            totDiscAmt = totDiscAmt + discountValue;
                                        }
                                    } else {
                                        totDiscAmt = 0;
                                    }
                                } else if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("M") && diffDay <= discountGraceValue * 30 && pendingInst < noOfInstPay) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        totDiscAmt = totDiscAmt + calc;
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        totDiscAmt = totDiscAmt + discountValue;
                                    }
                                } else if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("A") && intUptoDt.getDate() <= discountGraceValue && pendingInst < noOfInstPay) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        totDiscAmt = totDiscAmt + calc;
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        totDiscAmt = totDiscAmt + discountValue;
                                    }
                                } else if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("E") && pendingInst < noOfInstPay) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        totDiscAmt = totDiscAmt + calc;
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        totDiscAmt = totDiscAmt + discountValue;
                                    }
                                } else {
                                    totDiscAmt = 0;
                                }
                            }
                        } else if (discount != null && !discount.equals("") && discount.equals("N")) {
                            totDiscAmt = 0;
                        }
                    }

                }
            }

            //Bonus calculation details Starts...
            for (int l = startNoForPenal; l <= noOfInstPay - addNo; l++) {
                HashMap nextInstMap = new HashMap();
                nextInstMap.put("SCHEME_NAME", dataMap.get("SCHEME_NAME"));
                nextInstMap.put("DIVISION_NO", divNo);
                nextInstMap.put("SL_NO", new Double(l + noOfInsPaid + addNo + firstInst_No));
                List listRec = sqlMap.executeQueryForList("getSelectBonusAndNextInstDateWithoutDivision", nextInstMap);
                if (listRec == null || listRec.isEmpty()) {
                    //                Date curDate = (Date)currDt.clone();
                    Date curDate = (Date) intUptoDt.clone();
                    int curMonth = curDate.getMonth();
                    System.out.println("@#$$#$#instDay" + instDay);
                    curDate.setMonth(curMonth + 1);
                    curDate.setDate(instDay);
                    listRec = new ArrayList();
                    nextInstMap.put("NEXT_INSTALLMENT_DATE", curDate);
                    listRec.add(nextInstMap);
                    bonusAvailabe = false;
                }
                if (listRec != null && listRec.size() > 0) {
                    nextInstMap = (HashMap) listRec.get(0);
                    instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                    bonusAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_BONUS_AMOUNT"))).doubleValue();
                    //                long diffDay = DateUtil.dateDiff(instDate, currDt);
                    if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                            && bonusAmt > 0) {
                        Rounding rod = new Rounding();
                        if (productMap.get("BONUS_ROUNDOFF") != null && productMap.get("BONUS_ROUNDOFF").equals("NEAREST_VALUE")) {
                            bonusAmt = (double) rod.getNearest((long) (bonusAmt * 100), 100) / 100;
                        } else {
                            bonusAmt = (double) rod.lower((long) (bonusAmt * 100), 100) / 100;
                        }
                    }
                    long diffDay = DateUtil.dateDiff(instDate, intUptoDt);
                    //                System.out.println("First #########diffDay : " + diffDay);
                    String hoildayInt = CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT"));
                    if (productMap.get("BONUS_ALLOWED") != null && !productMap.get("BONUS_ALLOWED").equals("") && (productMap.get("BONUS_ALLOWED").equals("Y")
                            || productMap.get("BONUS_ALLOWED").equals("N"))) {
                        if (bonusAvailabe == true) {
                            if (prized == true) {
                                String bonusPrizedDays = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_DAYS"));
                                String bonusPrizedMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_MNTH"));
                                String bonusPrizedAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_AFT"));
                                String bonusPrizedEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_END"));
                                long bonusPrizedValue = CommonUtil.convertObjToLong(productMap.get("BONUS_PRIZED_GRACE_PERIOD"));
                                if (bonusPrizedDays != null && !bonusPrizedDays.equals("") && bonusPrizedDays.equals("D") && diffDay <= bonusPrizedValue) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                } else if (bonusPrizedMonth != null && !bonusPrizedMonth.equals("") && bonusPrizedMonth.equals("M") && diffDay <= (bonusPrizedValue * 30)) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                } else if (bonusPrizedAfter != null && !bonusPrizedAfter.equals("") && bonusPrizedAfter.equals("A") && intUptoDt.getDate() <= bonusPrizedValue) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                } else if (bonusPrizedEnd != null && !bonusPrizedEnd.equals("") && bonusPrizedEnd.equals("E")) {
                                } else {
                                }
                            } else if (prized == false) {
                                String bonusGraceDays = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_DAYS"));
                                String bonusGraceMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_MONTHS"));
                                String bonusGraceOnAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_AFTER"));
                                String bonusGraceEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_END"));
                                long bonusGraceValue = CommonUtil.convertObjToLong(productMap.get("BONUS_GRACE_PERIOD"));
                                if (bonusGraceDays != null && !bonusGraceDays.equals("") && bonusGraceDays.equals("D") && diffDay <= bonusGraceValue) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                } else if (bonusGraceMonth != null && !bonusGraceMonth.equals("") && bonusGraceMonth.equals("M") && diffDay <= (bonusGraceValue * 30)) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                } else if (bonusGraceOnAfter != null && !bonusGraceOnAfter.equals("") && bonusGraceOnAfter.equals("A") && intUptoDt.getDate() <= bonusGraceValue) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                } else if (bonusGraceEnd != null && !bonusGraceEnd.equals("") && bonusGraceEnd.equals("E")) {
                                } else {
                                }
                            }
                        }
                        //Added By Suresh
                        Rounding rod = new Rounding();
                        if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                                && bonusAmt > 0) {
                            if (productMap.get("BONUS_ROUNDOFF") != null && productMap.get("BONUS_ROUNDOFF").equals("NEAREST_VALUE")) {
                                bonusAmt = (double) rod.getNearest((long) (bonusAmt * 100), 100) / 100;
                            } else {
                                bonusAmt = (double) rod.lower((long) (bonusAmt * 100), 100) / 100;
                            }
                        }
                        bonusList.add(String.valueOf(bonusAmt));
                        installmentMap.put("BONUS", bonusList);
                    }
                }
                bonusAmt = 0;
            }
            //Arbitration Amount
            double arbitrationAmount = 0.0;
            whereMap.put("ACT_NUM", dataMap.get("CHITTAL_NO"));
            List caseChargeList = sqlMap.executeQueryForList("getMDSCaseChargeDetails", whereMap);
            if (caseChargeList != null && caseChargeList.size() > 0) {
                for (int i = 0; i < caseChargeList.size(); i++) {
                    whereMap = (HashMap) caseChargeList.get(i);
                    arbitrationAmount += CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
                }
            }
            //Notice Amount
            double noticeAmount = 0.0;
            whereMap.put("ACT_NUM", dataMap.get("CHITTAL_NO"));
            List noticeList = sqlMap.executeQueryForList("getMDSNoticeChargeDetails", whereMap);
            if (noticeList != null && noticeList.size() > 0) {
                for (int i = 0; i < noticeList.size(); i++) {
                    whereMap = (HashMap) noticeList.get(i);
                    noticeAmount += CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
                }
            }
            System.out.println("###### installmentMap" + installmentMap);
            System.out.println("###### maxLength" + noOfInstPay);
            System.out.println("###### bonusList" + bonusList);
            System.out.println("###### penalList" + penalList);
            for (int s = 0; s < noOfInstPay; s++) {
                HashMap insertMap = new HashMap();
                insertMap.put("INT_CALC_DT", setProperDtFormat(intUptoDt));
                insertMap.put("CHITTAL_NO", chittalNo);
                insertMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                insertMap.put("DIVISION_NO", String.valueOf(divNo));
                if (bonusList.size() > s) {
                    insertMap.put("BONUS", CommonUtil.convertObjToDouble(bonusList.get(s)));
                } else {
                    insertMap.put("BONUS", new Double(0));
                }
                if (penalList.size() > s) {
                    insertMap.put("PENAL", CommonUtil.convertObjToDouble(penalList.get(s)));
                } else {
                    insertMap.put("PENAL", new Double(0));
                }
                if (discountList.size() > s) {
                    insertMap.put("DISCOUNT", CommonUtil.convertObjToDouble(discountList.get(s)));
                } else {
                    insertMap.put("DISCOUNT", new Double(0));
                }
                insertMap.put("ARBITRATION_AMT", String.valueOf(arbitrationAmount));
                insertMap.put("NOTICE_AMT", String.valueOf(noticeAmount));
                noOfInsPaid += 1;
                insertMap.put("INSTALLMENT_NO", String.valueOf(noOfInsPaid));
                System.out.println("###### insertMap" + insertMap);
//                sqlMap.executeUpdate("insertSalaryRecoveryMDSDetails", insertMap);
            }

            //Charges
            double chargeAmt = 0.0;
            whereMap.put("ACT_NUM", dataMap.get("CHITTAL_NO"));
            chargeAmt = getChargeAmount(whereMap, prodType);
            if (chargeAmt > 0) {
                dataMap.put("CHARGES", String.valueOf(chargeAmt));
            } else {
                dataMap.put("CHARGES", "0");
            }
            if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                    && totBonusAmt > 0) {
                Rounding rod = new Rounding();
                if (productMap.get("BONUS_ROUNDOFF") != null && productMap.get("BONUS_ROUNDOFF").equals("NEAREST_VALUE")) {
                    totBonusAmt = (double) rod.getNearest((long) (totBonusAmt * 100), 100) / 100;
                } else {
                    totBonusAmt = (double) rod.lower((long) (totBonusAmt * 100), 100) / 100;
                }
            }
            if (calculateIntOn != null && calculateIntOn.equals("Installment Amount")) {
                insAmt = CommonUtil.convertObjToDouble(productMap.get("INSTALLMENT_AMOUNT"));
            }
            netAmt = (insAmt * noOfInstPay) + penalAmt - (totBonusAmt + totDiscAmt) + chargeAmt;
            insAmtPayable = (insAmt * noOfInstPay) - (totBonusAmt + totDiscAmt);
            dataMap.put("DIVISION_NO", String.valueOf(divNo));
            dataMap.put("CHIT_START_DT", startDate);
            dataMap.put("INSTALLMENT_DATE", insDate);
            dataMap.put("NO_OF_INSTALLMENTS", String.valueOf(totIns));
            dataMap.put("CURR_INST", String.valueOf(curInsNo));
            dataMap.put("PENDING_INST", String.valueOf(pendingInst));
            dataMap.put("PENDING_DUE_AMT", String.valueOf(insDueAmt));
            dataMap.put("NO_OF_INST_PAY", String.valueOf(noOfInstPay));
            dataMap.put("PRINCIPAL", String.valueOf(insAmtPayable)); // Principal Amount
            dataMap.put("PAID_INST", String.valueOf(noOfInsPaid));
            dataMap.put("PAID_DATE", currDt);
            dataMap.put("INTEREST", "0");

            if (prized == true) {
                dataMap.put("PRIZED_MEMBER", "Y");
            } else {
                dataMap.put("PRIZED_MEMBER", "N");
            }
            dataMap.put("BONUS", new Double(totBonusAmt));
            dataMap.put("DISCOUNT", new Double(totDiscAmt));
            dataMap.put("PENAL", new Double(penalAmt));                 // Penal Amount
            dataMap.put("TOTAL_DEMAND", new Double(netAmt));
            dataMap.put("INSTALLMENT_MAP", installmentMap);
            dataMap.put("CLEAR_BALANCE", new Double(0));
        } else {
            dataMap = null;
        }
        System.out.println("####### Single Row DataMap : " + dataMap);
        return dataMap;
    }

    public HashMap calcGL(String actNo, String schemeName, String prodType, double glAmount) throws Exception {

//        Calendar cal = null;
//        Double penal = 0.0;
//        String penaln = "";
////                String prodType = "";
//        String prodID = "";
//        String actNum = "";
//        //  System.out.println("in sb....map is ="+map);
//        HashMap singleRowMap = new HashMap();
//        HashMap newMap = new HashMap();
//        HashMap lstDueMap = new HashMap();
//        // prodType = CommonUtil.convertObjToStr(map.get("PRODTYPE"));
//        prodID = "";
//        actNum = actNo;
//        HashMap where1 = new HashMap();
//        HashMap map1 = new HashMap();
//        cal = null;
//
//        cal = Calendar.getInstance();
//        System.out.println("intUptoDt" + currDt);
//        cal.setTime(currDt);
//        int days = cal.get(Calendar.DATE) - 1;
//        System.out.println("daysss" + days);
//        if (days > 7) {
//            days = days - 7;
//        } else {
//            days = 0;
//        }
//
//        cal = null;
//
//        cal = Calendar.getInstance();
//        cal.setTime(currDt);
//        cal.add(Calendar.MONTH, -1);
//        cal.set(Calendar.DATE, 1);
//        Date currDt1 = cal.getTime();
//
//
//        //map1.put("BRANCH_CODE",);
//        map1.put("ACT_NUM", actNum);
//        map1.put("INSTALLMENT_DATE", setProperDtFormat(currDt1));
//        System.out.println("in sb....before passing is is =" + map1);
//        List lstDue = sqlMap.executeQueryForList("getDueDetails2", map1);
//        if (lstDue.size() > 0) {
//            penal = 0.0;
//            for (int i = 0; i < lstDue.size(); i++) {
//                lstDueMap = (HashMap) lstDue.get(i);
//                Date date2 = DateUtil.getDateMMDDYYYY(lstDueMap.get("INSTALLMENT_DATE").toString());
//                //Date date2= DateUtil.getDateMMDDYYYY(lstDueMap.get("INSTALLMENT_DATE").toString());
//                Double instAmt = CommonUtil.convertObjToDouble(lstDueMap.get("INSTALLMENT_AMOUNT"));
//                Date tempDate = (Date) currDt1.clone();
//                System.out.println("tempDate1.." + tempDate);
//
////                        cal = Calendar.getInstance();
////                        cal.setTime(tempDate);
////                        int m1=cal.get(Calendar.MONTH);
////                        cal.setTime(date2);
////                        int m2=cal.get(Calendar.MONTH);
////                        System.out.println("mmmm.."+m1+"jjjj"+m2);
////                       if((m1-1)!=m2)
////                        {
//                Double mm = monthsBetween(date2, tempDate) - 1;
//                HashMap shpMastMode = new HashMap();
//                List intRtList = sqlMap.executeQueryForList("getSelIntRt", shpMastMode);
//                HashMap intRtMap = new HashMap();
//                intRtMap = (HashMap) intRtList.get(0);
//                int intRate = CommonUtil.convertObjToInt(intRtMap.get("INTEREST_RATE").toString());
//                penal = penal + (mm * instAmt * intRate / 1200);
//                System.out.println("penal111" + penal);
//                penal = penal + (days * instAmt * intRate / 36500);
//                penaln = String.valueOf(df.format(penal));
//                penal = Double.parseDouble(penaln);
////                    }
//            }
//            System.out.println("penal==========" + penal);
//
//
//
//
//
//            penal = Math.round(penal * 100.0) / 100.0;
//            System.out.println("totBonusAmt " + penal);
//            Double d = penal;
//            String s = d.toString();
//            String s2 = s.substring(s.indexOf('.') + 1);
//            if (s2.length() >= 2) {
//                String s1 = "" + s2.charAt(1);
//                String s3 = "" + s2.charAt(0);
//                System.out.println("d " + d + "   s" + s + "   s2" + s2 + " s1" + s1 + " s3" + s3);
//                int num1 = Integer.parseInt(s1);
//                int num = Integer.parseInt(s3);
//                if (num >= 5) {
//                    penal += 1;
//                    d = penal;
//                    s = d.toString();
//                }
//                s = s.substring(0, s.indexOf('.')) + "." + "0" + "0";
//            } else {
//                s = s + "0";
//            }
//
//
//            System.out.println("sss" + s);
//            penal = Double.parseDouble(s);
//
//
//
//
//        }
//
//
//
//
//
//
//
//        HashMap dataMap = new HashMap();
        //        HashMap where=new HashMap();
        //        where.put("ACT_NO",actNo);
        //        where.put("")
        Double penal = 0.0;
        if (prodType.equals(TransactionFactory.SUSPENSE)) {
            HashMap map1 = new HashMap();
            map1.put("ACT_NUM", actNo);
            map1.put("ASONDATE", currDt.clone());
            HashMap lstDueMap = new HashMap();
            List lstDue = sqlMap.executeQueryForList("getSuspenseCalculationinAccountsTrans", map1);
            if (lstDue != null && lstDue.size() > 0) {
                penal = 0.0;
                lstDueMap = (HashMap) lstDue.get(0);
                if (lstDueMap != null && lstDueMap.size() > 0 && lstDueMap.containsKey("INTEREST")) {
                    penal = CommonUtil.convertObjToDouble(lstDueMap.get("INTEREST"));
                }
            }
        }
        HashMap dataMap = new HashMap();
        dataMap.put("PRINCIPAL", new Double((glAmount)));
        dataMap.put("TOTAL_DEMAND", new Double((glAmount + penal)));
        dataMap.put("INTEREST", new Double(penal));
        dataMap.put("PENAL", new Double(0));
        dataMap.put("CHARGES", new Double(0));
        dataMap.put("CLEAR_BALANCE", new Double(0));
        return dataMap;
    }

//    public HashMap calcTermDeposits(String actNum, String prodId, String prodType) throws Exception {
//        HashMap dataMap = new HashMap();
//        HashMap whereMap = new HashMap();
//        String behavesLike = "";
//        System.out.println("###### actNum" + actNum);
//        if (actNum.indexOf("_") != -1) {
//            actNum = actNum.substring(0, actNum.indexOf("_"));
//        }
//        whereMap.put("ACT_NUM", actNum);
//        List behavesLikeList = sqlMap.executeQueryForList("getBehavesLikeForDepositNo", whereMap);
//        if (behavesLikeList != null && behavesLikeList.size() > 0) {
//            whereMap = (HashMap) behavesLikeList.get(0);
//            behavesLike = CommonUtil.convertObjToStr(whereMap.get("BEHAVES_LIKE"));
//            if (!behavesLike.equals("") && behavesLike != null) {
//                System.out.println("###### behavesLike" + behavesLike + "###### actNum" + actNum);
//
//                if (behavesLike.equals("RECURRING")) {            //Recurring Deposit
//                    HashMap accountMap = new HashMap();
//                    HashMap lastMap = new HashMap();
//                    HashMap rdDataMap = new HashMap();
//                    rdDataMap.put("DEPOSIT_NO", actNum);
//                    accountMap.put("DEPOSIT_NO", actNum);
//                    accountMap.put("BRANCH_ID", _branchCode);
//                    List lst = sqlMap.executeQueryForList("getProductIdForDeposits", accountMap);
//                    if (lst != null && lst.size() > 0) {
//                        accountMap = (HashMap) lst.get(0);
//                        Date currDate = (Date) intUptoDt.clone();
//                        //                        Date currDate = (Date) currDt.clone();
//                        String insBeyondMaturityDat = "";
//                        List recurringLst = sqlMap.executeQueryForList("getRecurringDepositDetails", accountMap);
//                        if (recurringLst != null && recurringLst.size() > 0) {
//                            HashMap recurringMap = new HashMap();
//                            recurringMap = (HashMap) recurringLst.get(0);
//                            insBeyondMaturityDat = CommonUtil.convertObjToStr(recurringMap.get("INST_BEYOND_MATURITY_DATE"));
//                        }
//                        long totalDelay = 0;
//                        long actualDelay = 0;
//                        double delayAmt = 0.0;
//                        double tot_Inst_paid = 0.0;
//                        double depAmt = CommonUtil.convertObjToDouble(accountMap.get("DEPOSIT_AMT")).doubleValue();
//                        Date matDt = new Date();
//                        matDt.setTime(currDate.getTime());
//                        Date depDt = new Date();
//                        depDt.setTime(currDate.getTime());
//                        System.out.println("&&&&&&&&&&&& CurrentDate11111" + currDate);
//                        lastMap.put("DEPOSIT_NO", actNum);
//                        lst = sqlMap.executeQueryForList("getInterestDeptIntTable", lastMap);
//                        if (lst != null && lst.size() > 0) {
//                            lastMap = (HashMap) lst.get(0);
//                            System.out.println("###### lastMap--->" + lastMap);
//                            rdDataMap.put("DEPOSIT_AMT", lastMap.get("DEPOSIT_AMT"));
//                            rdDataMap.put("MATURITY_DT", lastMap.get("MATURITY_DT"));
//                            tot_Inst_paid = CommonUtil.convertObjToDouble(lastMap.get("TOTAL_INSTALL_PAID")).doubleValue();
//                            HashMap prematureDateMap = new HashMap();
//                            double monthPeriod = 0.0;
//                            Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lastMap.get("MATURITY_DT")));
//                            System.out.println("############# MATURITY_DT" + matDate);
//                            System.out.println("############# CurrentDate" + currDate);
//                            if (matDate.getDate() > 0) {
//                                matDt.setDate(matDate.getDate());
//                                matDt.setMonth(matDate.getMonth());
//                                matDt.setYear(matDate.getYear());
//                            }
//                            Date depDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lastMap.get("DEPOSIT_DT")));
//                            if (depDate.getDate() > 0) {
//                                depDt.setDate(depDate.getDate());
//                                depDt.setMonth(depDate.getMonth());
//                                depDt.setYear(depDate.getYear());
//                            }
//                            System.out.println("############# MATURITY_DT" + matDate);
//                            System.out.println("############# CurrentDate" + currDate);
//                            if (DateUtil.dateDiff((Date) matDt, (Date) currDate) > 0) {
//                                matDt = setProperDtFormat(matDt);
//                                prematureDateMap.put("TO_DATE", matDt);
//                                prematureDateMap.put("FROM_DATE", lastMap.get("DEPOSIT_DT"));
//                                lst = sqlMap.executeQueryForList("periodRunMap", prematureDateMap);
//                                if (lst != null && lst.size() > 0) {
//                                    prematureDateMap = (HashMap) lst.get(0);
//                                    System.out.println("############# prematureDateMap" + prematureDateMap);
//                                    monthPeriod = CommonUtil.convertObjToDouble(prematureDateMap.get("NO_OF_MONTHS")).doubleValue();
//                                    actualDelay = (long) monthPeriod - (long) tot_Inst_paid;
//                                    System.out.println("############# actualDelay" + actualDelay + "############# monthPeriod" + monthPeriod + "############# tot_Inst_paid" + tot_Inst_paid);
//                                }
//                                lst = null;
//                            } else {
//                                int dep = depDt.getMonth() + 1;
//                                int curr = currDate.getMonth() + 1;
//                                int depYear = depDt.getYear() + 1900;
//                                int currYear = currDate.getYear() + 1900;
//                                if (depYear == currYear) {
//                                    monthPeriod = curr - dep;
//                                    actualDelay = (long) monthPeriod - (long) tot_Inst_paid;
//                                } else {
//                                    int diffYear = currYear - depYear;
//                                    monthPeriod = (diffYear * 12 - dep) + curr;
//                                    actualDelay = (long) monthPeriod - (long) tot_Inst_paid;
//                                    
//                                    
//                                }
//                                System.out.println("############# shi else actualDelay" + actualDelay + "############# monthPeriod" + monthPeriod + "############# tot_Inst_paid" + tot_Inst_paid);
//                            }
//                        }
//                        lst = null;
//
//                        if ((DateUtil.dateDiff((Date) matDt, (Date) currDt) > 0) && !insBeyondMaturityDat.equals("") && insBeyondMaturityDat.equals("N")) {
//                            dataMap = new HashMap();
//                            return dataMap;
//                        }
//                        System.out.println("#############%%%%% MATURITY_DT" + matDt);
//                        System.out.println("############# %%%% CurrentDate" + currDate);
//                        //delayed installment calculation...
//                        if (DateUtil.dateDiff((Date) matDt, (Date) currDate) < 0 || insBeyondMaturityDat.equals("Y")) {
//                            depAmt = CommonUtil.convertObjToDouble(accountMap.get("DEPOSIT_AMT")).doubleValue();
//                            double chargeAmt = depAmt / 100;
//                            HashMap delayMap = new HashMap();
//                            delayMap.put("PROD_ID", accountMap.get("PROD_ID"));
//                            delayMap.put("DEPOSIT_AMT", accountMap.get("DEPOSIT_AMT"));
//                            lst = sqlMap.executeQueryForList("getSelectDelayedRate", delayMap);
//                            if (lst != null && lst.size() > 0) {
//                                delayMap = (HashMap) lst.get(0);
//                                delayAmt = CommonUtil.convertObjToDouble(delayMap.get("ROI")).doubleValue();
//                                delayAmt = delayAmt * chargeAmt;
//                                System.out.println("######recurring delayAmt : " + delayAmt);
//                            }
//                            lst = null;
//                            HashMap depRecMap = new HashMap();
//                            depRecMap.put("DEPOSIT_NO", actNum + "_1");
//                            List lstRec = sqlMap.executeQueryForList("getDepTransactionRecurring", depRecMap);
//                            if (lstRec != null && lstRec.size() > 0) {//how many unCompleted quarter is there upto (one by one) we have to calculate simple interest formula....
//                                for (int i = 0; i < lstRec.size(); i++) {//when the customer is paid from that dt to today dt....
//                                    depRecMap = (HashMap) lstRec.get(i);
//                                    Date transDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("TRANS_DT")));
//                                    Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("DUE_DATE")));
//                                    int transMonth = transDt.getMonth() + 1;
//                                    int dueMonth = dueDate.getMonth() + 1;
//                                    int dueYear = dueDate.getYear() + 1900;
//                                    int transYear = transDt.getYear() + 1900;
//                                    int delayedInstallment;// = transMonth - dueMonth;
//                                    if (dueYear == transYear) {
//                                        delayedInstallment = transMonth - dueMonth;
//                                    } else {
//                                        int diffYear = transYear - dueYear;
//                                        delayedInstallment = (diffYear * 12 - dueMonth) + transMonth;
//                                    }
//                                    if (delayedInstallment < 0) {
//                                        delayedInstallment = 0;
//                                    }
//                                    totalDelay = totalDelay + delayedInstallment;
//                                }
//                            }
//                            lstRec = null;
//                            depRecMap = new HashMap();
//                            depRecMap.put("DEPOSIT_NO", actNum + "_1");
//                            depRecMap.put("DEPOSIT_DT", lastMap.get("DEPOSIT_DT"));
//                            depRecMap.put("CURR_DT", currDate);
//                            depRecMap.put("SL_NO", String.valueOf(tot_Inst_paid));
//                            lstRec = sqlMap.executeQueryForList("getDepTransRecurr", depRecMap);
//                            if (lstRec != null && lstRec.size() > 0) {//how many unCompleted quarter is there upto (one by one) we have to calculate simple interest formula....
//                                for (int i = 0; i < lstRec.size(); i++) {//when the customer is paid from that dt to today dt....
//                                    depRecMap = (HashMap) lstRec.get(i);
//                                    Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("DUE_DATE")));
//                                    int transMonth = currDate.getMonth() + 1;
//                                    int dueMonth = dueDate.getMonth() + 1;
//                                    int dueYear = dueDate.getYear() + 1900;
//                                    int transYear = currDate.getYear() + 1900;
//                                    int delayedInstallment;// = transMonth - dueMonth;
//                                    if (dueYear == transYear) {
//                                        delayedInstallment = transMonth - dueMonth;
//                                    } else {
//                                        int diffYear = transYear - dueYear;
//                                        delayedInstallment = (diffYear * 12 - dueMonth) + transMonth;
//                                    }
//                                    if (delayedInstallment < 0) {
//                                        delayedInstallment = 0;
//                                    }
//                                    totalDelay = totalDelay + delayedInstallment;
//                                }
//                            }
//                            lstRec = null;
//                            System.out.println("#### totalDelay--->" + totalDelay);
//                            delayAmt = delayAmt * totalDelay;
//                            //delayAmt = (double) delayAmt * 100), 100) / 100;
//                            double oldPenalAmt = CommonUtil.convertObjToDouble(accountMap.get("DELAYED_AMOUNT")).doubleValue();
//                            long oldPenalMonth = CommonUtil.convertObjToLong(accountMap.get("DELAYED_MONTH"));
//                            double balanceAmt = 0.0;
//                            if (oldPenalAmt > 0) {
//                                balanceAmt = delayAmt - oldPenalAmt;
//                                totalDelay = totalDelay - oldPenalMonth;
//                            } else {
//                                balanceAmt = delayAmt;
//                            }
//                            double principal = actualDelay * CommonUtil.convertObjToDouble(rdDataMap.get("DEPOSIT_AMT")).doubleValue();
//                            double totalDemand = principal + balanceAmt;
//                            rdDataMap.put("DELAYED_MONTH", accountMap.get("DELAYED_MONTH"));
//                            rdDataMap.put("DEPOSIT_PENAL_AMT", String.valueOf(balanceAmt));
//                            rdDataMap.put("PRINCIPAL", String.valueOf(principal));
//                            rdDataMap.put("TOTAL_DEMAND", String.valueOf(totalDemand));
//                            rdDataMap.put("DEPOSIT_PENAL_MONTH", String.valueOf(totalDelay));
//                            System.out.println("#### balanceAmt--->" + balanceAmt + "##### totalDelay" + totalDelay);
//                        }
//                    }
//                    System.out.println("#### rdDataMap--->" + rdDataMap);
//                    dataMap.put("PRINCIPAL", rdDataMap.get("PRINCIPAL"));
//                    dataMap.put("PENAL", rdDataMap.get("DEPOSIT_PENAL_AMT"));
//                    dataMap.put("TOTAL_DEMAND", rdDataMap.get("TOTAL_DEMAND"));
//                    dataMap.put("DEPOSIT_PENAL_MONTH", rdDataMap.get("DEPOSIT_PENAL_MONTH"));
//                    dataMap.put("INTEREST", new Double(0));
//                    dataMap.put("CHARGES", new Double(0));
//                    if (CommonUtil.convertObjToDouble(rdDataMap.get("TOTAL_DEMAND")).doubleValue() <= 0.0) {
//                        dataMap = null;
//                    }
//                    System.out.println("######## dataMap" + dataMap);
//                }
//            }
//        }
//        return dataMap;
//    }
    
    
    public HashMap calcTermDeposits(String actNum, String prodId, String prodType) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap whereMap = new HashMap();
        String behavesLike = "";
        long actualDelay = 0;
        String acNoSubNo = "";
        //System.out.println(" actNum" + actNum);
        acNoSubNo = actNum;
        if (actNum.indexOf("_") != -1) {
            actNum = actNum.substring(0, actNum.indexOf("_"));
        }
        whereMap.put("ACT_NUM", actNum);
        List behavesLikeList = sqlMap.executeQueryForList("getBehavesLikeForDepositNo", whereMap);
        if (behavesLikeList != null && behavesLikeList.size() > 0) {
            whereMap = (HashMap) behavesLikeList.get(0);
            behavesLike = CommonUtil.convertObjToStr(whereMap.get("BEHAVES_LIKE"));
            if (!behavesLike.equals("") && behavesLike != null && behavesLike.equals("RECURRING")) {
                System.out.println(" behavesLike" + behavesLike + " actNum" + actNum);
                HashMap depositMaxMap = new HashMap();
                depositMaxMap.put("DEPOSIT_NO", acNoSubNo);
                System.out.println("depositMaxMap : " + depositMaxMap);
                List depositMaxList = sqlMap.executeQueryForList("getSelectRDDepositMaxtransDt", depositMaxMap);
                if (depositMaxList != null && depositMaxList.size() > 0) {
                    depositMaxMap = (HashMap) depositMaxList.get(0);
                    Date date = (Date) currDt.clone();
                    Date maxDate = getProperDateFormat(CommonUtil.convertObjToStr(depositMaxMap.get("TRANS_DT")));
                    Date transDueDate = getProperDateFormat(CommonUtil.convertObjToStr(depositMaxMap.get("DUE_DATE")));
                    System.out.println("curr date : " + date + " last transaction date : " + maxDate);
                    int transDueMonth = (int) transDueDate.getMonth() + 1;
                    int currentMonth = (int) date.getMonth() + 1;
                    int maxTxnMonth = (int) maxDate.getMonth() + 1;
                    int currentYear = (int) date.getYear() + 1900;
                    int maxTxnYear = (int) maxDate.getYear() + 1900;
                    System.out.println("curr date month : " + currentMonth + " last transaction maxDate : " + maxTxnMonth + "curr year : " + currentYear + " last transaction year : " + maxTxnYear);
                    HashMap accountMap = new HashMap();
                    HashMap lastMap = new HashMap();
                    HashMap rdDataMap = new HashMap();
                    rdDataMap.put("DEPOSIT_NO", actNum);
                    accountMap.put("DEPOSIT_NO", actNum);
                    accountMap.put("BRANCH_ID", _branchCode);
                    List lst = sqlMap.executeQueryForList("getProductIdForDeposits", accountMap);
                    if (lst != null && lst.size() > 0) {
                        accountMap = (HashMap) lst.get(0);
                        Date currDate = (Date) intUptoDt.clone();
                        //  Date currDate = (Date) currDt.clone();
                        String insBeyondMaturityDat = "";
                        List recurringLst = sqlMap.executeQueryForList("getRecurringDepositDetails", accountMap);
                        if (recurringLst != null && recurringLst.size() > 0) {
                            HashMap recurringMap = new HashMap();
                            recurringMap = (HashMap) recurringLst.get(0);
                            insBeyondMaturityDat = CommonUtil.convertObjToStr(recurringMap.get("INST_BEYOND_MATURITY_DATE"));
                        }
                        long totalDelay = 0;
                        double delayAmt = 0.0;
                        double tot_Inst_paid = 0.0;
                        double depAmt = CommonUtil.convertObjToDouble(accountMap.get("DEPOSIT_AMT"));
                        dataMap.put("INSTMT_AMT", depAmt);
                        Date matDt = new Date();
                        matDt.setTime(currDate.getTime());
                        Date depDt = new Date();
                        depDt.setTime(currDate.getTime());
                        //System.out.println("&&&&&&&&&&&& CurrentDate11111" + currDate);
                        lastMap.put("DEPOSIT_NO", actNum);
                        lst = sqlMap.executeQueryForList("getInterestDeptIntTable", lastMap);
                        if (lst != null && lst.size() > 0) {
                            lastMap = (HashMap) lst.get(0);
                            System.out.println(" lastMap>" + lastMap);
                            rdDataMap.put("DEPOSIT_AMT", lastMap.get("DEPOSIT_AMT"));
                            rdDataMap.put("MATURITY_DT", lastMap.get("MATURITY_DT"));
                            tot_Inst_paid = CommonUtil.convertObjToDouble(lastMap.get("TOTAL_INSTALL_PAID"));
                            HashMap prematureDateMap = new HashMap();
                            double monthPeriod = 0.0;
                            Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lastMap.get("MATURITY_DT")));
                            //System.out.println(" MATURITY_DT" + matDate);
                            //System.out.println(" CurrentDate" + currDate);
                            if (matDate.getDate() > 0) {
                                matDt.setDate(matDate.getDate());
                                matDt.setMonth(matDate.getMonth());
                                matDt.setYear(matDate.getYear());
                            }
                            Date depDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lastMap.get("DEPOSIT_DT")));
                            if (depDate.getDate() > 0) {
                                depDt.setDate(depDate.getDate());
                                depDt.setMonth(depDate.getMonth());
                                depDt.setYear(depDate.getYear());
                            }
                            if (DateUtil.dateDiff((Date) matDt, (Date) currDate) > 0) {
                                matDt = setProperDtFormat(matDt);
                                prematureDateMap.put("TO_DATE", matDt);
                                prematureDateMap.put("FROM_DATE", lastMap.get("DEPOSIT_DT"));
                                lst = sqlMap.executeQueryForList("periodRunMap", prematureDateMap);
                                if (lst != null && lst.size() > 0) {
                                    prematureDateMap = (HashMap) lst.get(0);
                                    System.out.println(" prematureDateMap" + prematureDateMap);
                                    monthPeriod = CommonUtil.convertObjToDouble(prematureDateMap.get("NO_OF_MONTHS"));
                                    actualDelay = (long) monthPeriod - (long) tot_Inst_paid;
                                    System.out.println(" actualDelay" + actualDelay + " monthPeriod" + monthPeriod + " tot_Inst_paid" + tot_Inst_paid);
                                }
                                lst = null;
                            } else {
//                                int dep = depDt.getMonth() + 1;
//                                int curr = currDate.getMonth() + 1;
//                                int depYear = depDt.getYear() + 1900;
//                                int currYear = currDate.getYear() + 1900;
//                                if (depYear == currYear) {
//                                    monthPeriod = curr - dep;
//                                    actualDelay = (long) monthPeriod - (long) tot_Inst_paid;
//                                } else {
//                                    int diffYear = currYear - depYear;
//                                    monthPeriod = (diffYear * 12 - dep) + curr;
//                                    actualDelay = (long) monthPeriod - (long) tot_Inst_paid;
//                                }
//                                System.out.println(" else actualDelay" + actualDelay + " monthPeriod" + monthPeriod + " tot_Inst_paid" + tot_Inst_paid);
                                System.out.println("currDate" + currDate);
                                java.util.GregorianCalendar gactualCurrDateCalendar = new java.util.GregorianCalendar();
                                gactualCurrDateCalendar.setGregorianChange(currDate);
                                gactualCurrDateCalendar.setTime(currDate);
                                int curDay = gactualCurrDateCalendar.get(gactualCurrDateCalendar.DAY_OF_MONTH);
                                System.out.println("curDaycurDay" + curDay);
                                List recoveryList = sqlMap.executeQueryForList("getRecoveryParameters", whereMap);
                                int gracePeriod = 0;
                                if (recoveryList != null && recoveryList.size() > 0) {
                                    HashMap recoveryDetailsMap = (HashMap) recoveryList.get(0);
                                    if (recoveryDetailsMap != null && recoveryDetailsMap.size() > 0 && recoveryDetailsMap.containsKey("GRACE_PERIOD")) {
                                        gracePeriod = CommonUtil.convertObjToInt(recoveryDetailsMap.get("GRACE_PERIOD"));
                                    }
                                }
                                System.out.println("curDay : " + curDay + " gracePeriod : " + gracePeriod);
                                if (curDay > gracePeriod && transDueMonth > currentMonth && transDueMonth != currentMonth) {
                                    gactualCurrDateCalendar.add(gactualCurrDateCalendar.MONTH, 1);
                                    gactualCurrDateCalendar.set(gactualCurrDateCalendar.DAY_OF_MONTH, gracePeriod);
                                } else {
                                    gactualCurrDateCalendar.set(gactualCurrDateCalendar.DAY_OF_MONTH, gracePeriod);
                                }
                                Date tempcurrDate = new Date();
                                tempcurrDate = CommonUtil.getProperDate(currDt, gactualCurrDateCalendar.getTime());
                                Date dueDate = new Date();

                                HashMap detailedMap = new HashMap();
                                detailedMap.put("DEPOSIT_NO", actNum + "_1");
                                List newList = sqlMap.executeQueryForList("getBalnceDepositDetails", detailedMap);
                                if (newList != null && newList.size() > 0) {
                                    for (int i = 0; i < newList.size(); i++) {
                                        HashMap newMap = (HashMap) newList.get(i);
                                        if (newMap != null && newMap.size() > 0) {
                                            dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(newMap.get("DUE_DATE")));
                                            System.out.println("tempcurrDate" + tempcurrDate + "dueDate" + dueDate);
                                            if (dueDate.compareTo(tempcurrDate) == 0 || dueDate.compareTo(tempcurrDate) < 0) {
                                                actualDelay = actualDelay + 1;
                                            }
                                        }
                                    }
                                }
                                System.out.println(" else actualDelay" + actualDelay + " monthPeriod" + monthPeriod + " tot_Inst_paid" + tot_Inst_paid);
                            }
                        }
                        lst = null;
                        if ((DateUtil.dateDiff((Date) matDt, (Date) currDt) > 0) && !insBeyondMaturityDat.equals("") && insBeyondMaturityDat.equals("N")) {
                            dataMap = new HashMap();
                            return dataMap;
                        }
                        //System.out.println("MATURITY_DT" + matDt);
                        //System.out.println("CurrentDate" + currDate);
                        //delayed installment calculation...
                        String penalCalcType = "DAYS";//INSTALMENT
                        HashMap dailyMap = new HashMap();
                        dailyMap.put("ROI_GROUP_ID", prodId);
                        List list = (List) sqlMap.executeQueryForList("getSelectDepositsCommision", dailyMap);
                        if (list != null && list.size() > 0) {
                            System.out.println("list list list" + list);
                            InterestMaintenanceRateTO objInterestMaintenanceRateTO = (InterestMaintenanceRateTO) list.get(0);
                            if (objInterestMaintenanceRateTO != null) {
                                penalCalcType = CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getInstType());
                            }
                        }
                        if (DateUtil.dateDiff((Date) matDt, (Date) currDate) < 0 || insBeyondMaturityDat.equals("Y")) {
                            if (penalCalcType != null && !penalCalcType.equals("") && penalCalcType.equals("Installments")) {
                                depAmt = CommonUtil.convertObjToDouble(accountMap.get("DEPOSIT_AMT"));
                                dataMap.put("INSTMT_AMT", depAmt);
                                double chargeAmt = depAmt / 100;//100
                                HashMap delayMap = new HashMap();
                                delayMap.put("PROD_ID", accountMap.get("PROD_ID"));
                                delayMap.put("DEPOSIT_AMT", accountMap.get("DEPOSIT_AMT"));
                                lst = sqlMap.executeQueryForList("getSelectDelayedRate", delayMap);
                                if (lst != null && lst.size() > 0) {
                                    delayMap = (HashMap) lst.get(0);
                                    System.out.println("delayMapdelayMapdelayMap" + delayMap);
                                    delayAmt = CommonUtil.convertObjToDouble(delayMap.get("PENAL_INT"));
                                    delayAmt = delayAmt * chargeAmt;
                                    System.out.println("recurring delayAmt : " + delayAmt);
                                }
                                lst = null;
                                HashMap depRecMap = new HashMap();
                                depRecMap.put("DEPOSIT_NO", actNum + "_1");
                                List lstRec = sqlMap.executeQueryForList("getDepTransactionRecurring", depRecMap);
                                if (lstRec != null && lstRec.size() > 0) {//how many unCompleted quarter is there upto (one by one) we have to calculate simple interest formula....
                                    for (int i = 0; i < lstRec.size(); i++) {//when the customer is paid from that dt to today dt....
                                        depRecMap = (HashMap) lstRec.get(i);
                                        Date transDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("TRANS_DT")));
                                        Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("DUE_DATE")));
                                        int transMonth = transDt.getMonth() + 1;
                                        int dueMonth = dueDate.getMonth() + 1;
                                        int dueYear = dueDate.getYear() + 1900;
                                        int transYear = transDt.getYear() + 1900;
                                        int delayedInstallment;// = transMonth - dueMonth;
                                        System.out.println("JEFFFFFFFFFFFFF" + "transDt" + transDt + "dueDate" + dueDate + "transMonth" + transMonth + "dueMonth" + dueMonth + "transYear" + transYear + "transMonth" + transMonth);
                                        if (dueYear == transYear) {
                                            delayedInstallment = transMonth - dueMonth;
                                        } else {
                                            int diffYear = transYear - dueYear;
                                            delayedInstallment = (diffYear * 12 - dueMonth) + transMonth;
                                        }
                                        if (delayedInstallment < 0) {
                                            delayedInstallment = 0;
                                        }
                                        totalDelay = totalDelay + delayedInstallment;
                                        System.out.println("here totalDelay is now:" + totalDelay);
                                    }
                                }
                                lstRec = null;
                                depRecMap = new HashMap();
                                depRecMap.put("DEPOSIT_NO", actNum + "_1");
                                depRecMap.put("DEPOSIT_DT", lastMap.get("DEPOSIT_DT"));
                                depRecMap.put("CURR_DT", currDate);
                                depRecMap.put("SL_NO", String.valueOf(tot_Inst_paid));
                                lstRec = sqlMap.executeQueryForList("getDepTransRecurr", depRecMap);
                                if (lstRec != null && lstRec.size() > 0) {//how many unCompleted quarter is there upto (one by one) we have to calculate simple interest formula....
                                    for (int i = 0; i < lstRec.size(); i++) {//when the customer is paid from that dt to today dt....
                                        depRecMap = (HashMap) lstRec.get(i);
                                        Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("DUE_DATE")));
                                        int transMonth = currDate.getMonth() + 1;//2
                                        int dueMonth = dueDate.getMonth() + 1;//2
                                        int dueYear = dueDate.getYear() + 1900;//2015
                                        int transYear = currDate.getYear() + 1900;//2015
                                        int delayedInstallment;// = transMonth - dueMonth;
                                        System.out.println("JEFFFFFFFFFFFFF" + "dueDate" + dueDate + "transMonth" + transMonth + "dueMonth" + dueMonth + "transYear" + transYear + "transMonth" + transMonth);
                                        if (dueYear == transYear) {
                                            delayedInstallment = transMonth - dueMonth;
                                        } else {
                                            int diffYear = transYear - dueYear;
                                            delayedInstallment = (diffYear * 12 - dueMonth) + transMonth;
                                        }
                                        if (delayedInstallment < 0) {
                                            delayedInstallment = 0;
                                        }
                                        totalDelay = totalDelay + delayedInstallment;
                                        System.out.println("there totalDelay is now:" + totalDelay);
                                    }
                                }
                                lstRec = null;
                                //System.out.println(" totalDelay>" + totalDelay);
                                delayAmt = delayAmt * totalDelay;
                                System.out.println("delayAmt calculated = :" + delayAmt);
                                //delayAmt = (double) delayAmt * 100), 100) / 100;
                                double oldPenalAmt = CommonUtil.convertObjToDouble(accountMap.get("DELAYED_AMOUNT"));
                                long oldPenalMonth = CommonUtil.convertObjToLong(accountMap.get("DELAYED_MONTH"));
                                System.out.println("oldPenalAmt" + oldPenalAmt + "oldPenalMonth" + oldPenalMonth);
                                double balanceAmt = 0.0;
                                if (oldPenalAmt > 0) {
                                    balanceAmt = delayAmt - oldPenalAmt;
                                    totalDelay = totalDelay - oldPenalMonth;
                                    System.out.println("here now is :" + "balanceAmt" + balanceAmt + "totalDelay" + totalDelay);
                                } else {
                                    balanceAmt = delayAmt;
                                    System.out.println("balanceAmt = delayAmt" + balanceAmt);
                                }
                                System.out.println("calculation actualDelay" + actualDelay);
                                System.out.println("calculating CommonUtil.convertObjToDouble(rdDataMap.get(DEPOSIT_AMT)" + CommonUtil.convertObjToDouble(rdDataMap.get("DEPOSIT_AMT")));
                                double principal = actualDelay * CommonUtil.convertObjToDouble(rdDataMap.get("DEPOSIT_AMT"));
                                System.out.println("calculating balanceAmt" + balanceAmt);
                                double totalDemand = principal + balanceAmt;
                                System.out.println("calculated totalDemand" + totalDemand);
                                rdDataMap.put("DELAYED_MONTH", accountMap.get("DELAYED_MONTH"));
                                rdDataMap.put("DEPOSIT_PENAL_AMT", String.valueOf(balanceAmt));
                                rdDataMap.put("PRINCIPAL", String.valueOf(principal));
                                rdDataMap.put("TOTAL_DEMAND", String.valueOf(totalDemand));
                                rdDataMap.put("DEPOSIT_PENAL_MONTH", String.valueOf(totalDelay));
                                System.out.println(" balanceAmt>" + balanceAmt + " totalDelay" + totalDelay);
                            }
                            //Set of code added by Jeffin John on 28-12-2014 fro Mantis : 9969
                            if (penalCalcType != null && !penalCalcType.equals("") && penalCalcType.equals("Days")) {
                                depAmt = CommonUtil.convertObjToDouble(accountMap.get("DEPOSIT_AMT"));
                                dataMap.put("INSTMT_AMT", depAmt);
                                HashMap delayMap = new HashMap();
                                double roi = 0.0;
                                delayMap.put("PROD_ID", accountMap.get("PROD_ID"));
                                delayMap.put("DEPOSIT_AMT", accountMap.get("DEPOSIT_AMT"));
                                lst = sqlMap.executeQueryForList("getSelectDelayedRate", delayMap);
                                if (lst != null && lst.size() > 0) {
                                    delayMap = (HashMap) lst.get(0);
                                    roi = CommonUtil.convertObjToDouble(delayMap.get("PENAL_INT"));
                                }
                                List lstRec = null;
                                HashMap depRecMap = new HashMap();
                                depRecMap = new HashMap();
                                double penal = 0.0;
                                depRecMap.put("DEPOSIT_NO", actNum + "_1");
                                depRecMap.put("DEPOSIT_DT", lastMap.get("DEPOSIT_DT"));
                                depRecMap.put("CURR_DT", currDate);
                                depRecMap.put("SL_NO", String.valueOf(tot_Inst_paid));
                                lstRec = sqlMap.executeQueryForList("getDepTransRecurr", depRecMap);
                                int size = CommonUtil.convertObjToInt(lstRec.size());
                                if (lstRec != null && lstRec.size() > 0) {
                                    for (int i = 0; i < size; i++) {
                                        depRecMap = (HashMap) lstRec.get(i);
                                        double amount = depAmt * (i + 1);
                                        Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("DUE_DATE")));
                                        if (DateUtil.dateDiff(dueDate, currDt) > 0) {
                                            double diff = DateUtil.dateDiff(dueDate, currDt) - 1;
                                            if (diff > 0) {
                                                penal = penal + ((amount * roi * diff) / 36500);
                                            }
                                        }
                                    }
                                }
                                penal = Math.round(penal);
                                double principal = actualDelay * CommonUtil.convertObjToDouble(rdDataMap.get("DEPOSIT_AMT"));
                                double totalDemand = principal + penal;
                                rdDataMap.put("DELAYED_MONTH", accountMap.get("DELAYED_MONTH"));
                                rdDataMap.put("DEPOSIT_PENAL_AMT", String.valueOf(penal));
                                rdDataMap.put("PRINCIPAL", String.valueOf(principal));
                                rdDataMap.put("TOTAL_DEMAND", String.valueOf(totalDemand));
                                rdDataMap.put("DEPOSIT_PENAL_MONTH", String.valueOf(totalDelay));
                            }
                        }
                    }
                    System.out.println(" rdDataMap>" + rdDataMap);
                    dataMap.put("PRINCIPAL", rdDataMap.get("PRINCIPAL"));
                    dataMap.put("TOTAL", actualDelay);
                    dataMap.put("PRINCIPAL_OLD", rdDataMap.get("PRINCIPAL"));
                    dataMap.put("PENAL", rdDataMap.get("DEPOSIT_PENAL_AMT"));
                    dataMap.put("TOTAL_DEMAND", rdDataMap.get("TOTAL_DEMAND"));
                    dataMap.put("DEPOSIT_PENAL_MONTH", rdDataMap.get("DEPOSIT_PENAL_MONTH"));
                    dataMap.put("INTEREST", new Double(0));
                    dataMap.put("CHARGES", new Double(0));
                    dataMap.put("CLEAR_BALANCE", new Double(0));
                    if (CommonUtil.convertObjToDouble(rdDataMap.get("TOTAL_DEMAND")) <= 0.0) {
                        dataMap = null;
                    }
                }
                System.out.println(" dataMap" + dataMap);
            }
        }
        return dataMap;
    }
    

//    public HashMap calcLoanPayments(String actNum, String prodId, String prodType, String recoveryYesNo) throws Exception {
//        HashMap dataMap = new HashMap();
//        HashMap whereMap = new HashMap();
//        whereMap.put("ACT_NUM", actNum);
//        List parameterList = sqlMap.executeQueryForList("getRecoveryParameter", whereMap);
//        String clear_balance = "";
//        boolean flag = false;
//        if (parameterList != null && parameterList.size() > 0) {
//            int firstDay = 0;
//            Date inst_dt = null;
//            Date checkDate = (Date) currDt.clone();
//            Date sanctionDt = null;
//            whereMap = (HashMap) parameterList.get(0);
//            firstDay = CommonUtil.convertObjToInt(whereMap.get("FIRST_DAY"));
//            sanctionDt = (Date) whereMap.get("FROM_DT");
//            System.out.println("### First Day : " + firstDay);
//            System.out.println("### checkDat : " + checkDate);
//            System.out.println("### sanctionDt : " + sanctionDt);
////            checkDate.setDate(firstDay);
//            System.out.println("### checkDat : " + checkDate);
//            long diffDay;
//            diffDay = DateUtil.dateDiff((Date) sanctionDt, (Date) checkDate);
//            System.out.println("### diffDay : " + diffDay);
//            if (diffDay >= 0.0) {
//                System.out.println("### Allow : " + checkDate);
//
//                // IF SALARY_RECOVERY = 'NO' ONLY
//                if (recoveryYesNo.equals("N")) {
////                    Date recoveryCheckDt = null;
////                    Date maxInstDt = null;
////                    Date lastIntCalcDt = null;
////                    long diffDayPending = 0;
////                    recoveryCheckDt = (Date)interestUptoDt.clone();
////                    recoveryCheckDt.setMonth(recoveryCheckDt.getMonth()-1);
////                    recoveryCheckDt = setProperDtFormat(recoveryCheckDt);
////                    System.out.println("########### After interestUptoDt : "+interestUptoDt);
////                    System.out.println("########### After recoveryCheckDt : "+recoveryCheckDt);
////                    HashMap instMap = new HashMap();
////                    instMap.put("ACT_NUM",actNum);
////                    instMap.put("CHECK_DT",recoveryCheckDt);
////                    List instList = sqlMap.executeQueryForList("getLoanMaxInstDt", instMap);
////                    if(instList!=null && instList.size()>0){
////                        instMap=(HashMap) instList.get(0);
////                        maxInstDt = (Date)instMap.get("INSTALLMENT_DT");
////                        HashMap intCalcMap = new HashMap();
////                        intCalcMap.put("WHERE",actNum);
////                        List intCalcList = sqlMap.executeQueryForList("getLastIntCalDateAD", intCalcMap);
////                        if(intCalcList!=null && intCalcList.size()>0 ){
////                            intCalcMap=(HashMap) intCalcList.get(0);
////                            if (intCalcMap.get("LAST_INT_CALC_DT")!=null) {
////                                lastIntCalcDt = (Date)intCalcMap.get("LAST_INT_CALC_DT");
////                                System.out.println("########### lastIntCalcDt : "+lastIntCalcDt);
////                                System.out.println("###########     maxInstDt : "+maxInstDt);
////                                System.out.println("###########interestUptoDt : "+interestUptoDt);
////                                diffDayPending= DateUtil.dateDiff((Date) lastIntCalcDt, (Date) maxInstDt);
////                                System.out.println("### diffDayPending : "+diffDayPending);
////                                if(diffDayPending<=0){
////                                    System.out.println("########### NOT ALLOWED : ");
////                                   dataMap = null; 
////                                   return dataMap;
////                                }else{
////                                    System.out.println("########### ALLOWED : ");
////                                }
////                            } else {
////                                dataMap = null; 
////                                return dataMap;
////                            }
////                        }
////                    }
////                }
//
//                    HashMap asAndWhenMap = interestCalculationTLAD(actNum, prodId, prodType);
//                    System.out.println("@#@ asAndWhenMap is >>>>" + asAndWhenMap);
//                    System.out.println("transDetail" + actNum + _branchCode);
//                    HashMap insertPenal = new HashMap();
//                    List chargeList = null;
//                    HashMap loanInstall = new HashMap();
//                    loanInstall.put("ACT_NUM", actNum);
//                    loanInstall.put("BRANCH_CODE", _branchCode);
//                    double instalAmt = 0.0;
//                    double paidAmount = 0.0;
//                    HashMap emiMap = new HashMap();
//                    String installtype = "";
//                    String emi_uniform = "";
//                    if (prodType != null && prodType.equals("TL")) {      //Only TL
//
//                        double totPrinciple = 0.0;
//                        List emiList = sqlMap.executeQueryForList("getEmiTypeDetail", loanInstall);
//                        if (emiList.size() > 0) {
//                            emiMap = (HashMap) emiList.get(0);
//                            installtype = emiMap.get("INSTALL_TYPE").toString();
//                            emi_uniform = emiMap.get("EMI_IN_SIMPLEINTREST").toString();
//                        }
//                        //HashMap allInstallmentMap=null;
//
//
//
//
//
//                        HashMap allInstallmentMap = null;
//                        if (!(installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y"))) {
//                            List paidAmt = sqlMap.executeQueryForList("getPaidPrinciple", loanInstall);
//                            allInstallmentMap = (HashMap) paidAmt.get(0);
//                            System.out.println("allInstallmentMap..." + allInstallmentMap);
//                            System.out.println("!!!!asAndWhenMap:" + asAndWhenMap);
//                            totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
//                            paidAmount = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
//                            System.out.println("totPrinciple11:" + totPrinciple + " ::" + paidAmount);
//                            if (asAndWhenMap == null || (asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && (asAndWhenMap.get("AS_CUSTOMER_COMES") != null) && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("N"))) {
//                                paidAmt = sqlMap.executeQueryForList("getIntDetails", loanInstall);
//                                if (paidAmt != null && paidAmt.size() > 0) {
//                                    allInstallmentMap = (HashMap) paidAmt.get(0);
//                                }
//                                double totExcessAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
//                                totPrinciple += totExcessAmt;
//                                System.out.println("in as cust comexx" + totPrinciple);
//                            }
//                            List lst = sqlMap.executeQueryForList("getAllLoanInstallment", loanInstall);
//
//                            for (int i = 0; i < lst.size(); i++) {
//                                allInstallmentMap = (HashMap) lst.get(i);
//                                System.out.println("allInstallmentMap...,mmm>>" + allInstallmentMap);
//                                instalAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
//                                System.out.println("instalAmtt..." + instalAmt + "toooottt" + totPrinciple);
//                                if (instalAmt <= totPrinciple) {
//                                    totPrinciple -= instalAmt;
//                                    System.out.println("totPrinciple-=instalAmt==" + totPrinciple);
//                                    inst_dt = new Date();
//                                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
//                                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
//                                } else {
//
//                                    inst_dt = new Date();
//                                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
//                                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
//                                    //bbau22
//                                    List aList = ServerUtil.executeQuery("getMorotorium", loanInstall);
//                                    String moret = "";
//                                    if (aList.size() > 0 && aList.get(0) != null) {
//                                        HashMap mapop = (HashMap) aList.get(0);
//                                        if (mapop.get("MORATORIUM_GIVEN") != null) {
//                                            moret = mapop.get("MORATORIUM_GIVEN").toString();
//                                        }
//
//
//                                    }
//                                    System.out.println("inst_dt=22====" + inst_dt + "currDt=22======" + currDt);
//                                    System.out.println("totPrrr22rrrrrrrrrrrrrr=" + DateUtil.dateDiff(inst_dt, currDt) + "ggggg=" + moret);
//                                    System.out.println("totPrrrrrrrrrrrrrrrrr=" + DateUtil.dateDiff(inst_dt, currDt) + "ggggg=" + moret);
//                                    if (DateUtil.dateDiff(inst_dt, currDt) <= 0 && (moret != null && moret.equals("Y"))) {
//                                        totPrinciple = 0;
//                                        flag = true;
//                                        break;
//                                    } else {
//                                        //
//                                        totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue() - totPrinciple;
//                                        System.out.println("before Breakkkk" + totPrinciple);
//                                        break;
//                                    }
//                                }
//                            }
//                        } else {
//                            List paidAmtemi = sqlMap.executeQueryForList("getPaidPrincipleEMI1", loanInstall);
//                            allInstallmentMap = (HashMap) paidAmtemi.get(0);
//                            System.out.println("!!!!asAndWhenMap:" + asAndWhenMap);
//                            System.out.println("allInstallmentMap..." + allInstallmentMap);
//                            totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
//                            paidAmount = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
//                            System.out.println("totPrinciple22:" + totPrinciple + " ::" + paidAmount);
//                            if (asAndWhenMap == null || (asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && (asAndWhenMap.get("AS_CUSTOMER_COMES") != null) && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("N"))) {
//                                paidAmtemi = sqlMap.executeQueryForList("getIntDetails", loanInstall);
//                                if (paidAmtemi != null && paidAmtemi.size() > 0) {
//                                    allInstallmentMap = (HashMap) paidAmtemi.get(0);
//                                }
//                                System.out.println("allInstallmentMap444" + allInstallmentMap);
//                                double totExcessAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
//                                totPrinciple += totExcessAmt;
//                                System.out.println("totPrinciple" + totPrinciple);
//                            }
//                            List lst = sqlMap.executeQueryForList("getAllLoanInstallment", loanInstall);
//                            for (int i = 0; i < lst.size(); i++) {
//                                allInstallmentMap = (HashMap) lst.get(i);
//                                System.out.println("allInstallmentMap34243" + allInstallmentMap);
//                                instalAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
//                                System.out.println("111222instalAmt" + instalAmt + ":::" + totPrinciple);
//                                if (instalAmt <= totPrinciple) {
//                                    totPrinciple -= instalAmt;
//                                    System.out.println("chhhhnnn" + totPrinciple);
//                                    inst_dt = new Date();
//                                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
//                                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
//                                } else {
//                                    inst_dt = new Date();
//                                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
//                                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
//                                    totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("TOTAL_AMT")).doubleValue() - totPrinciple;
//                                    System.out.println("bbbkkk" + totPrinciple);
//                                    break;
//                                }
//                                System.out.println("totPrinciple@@@@@@@@@@@@" + totPrinciple);
//                            }
//                        }
//                        if (!(installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y"))) {
//                            //bb
//                            dataMap.put("INSTMT_AMT", instalAmt);
//                        } else {
//                            dataMap.put("INSTMT_AMT", (allInstallmentMap.get("TOTAL_AMT")));
//                        }
//                        //bbau33
//                        List aList = ServerUtil.executeQuery("getMorotorium", loanInstall);
//                        String moret = "";
//                        if (aList.size() > 0 && aList.get(0) != null) {
//                            HashMap mapop = (HashMap) aList.get(0);
//                            if (mapop.get("MORATORIUM_GIVEN") != null) {
//                                moret = mapop.get("MORATORIUM_GIVEN").toString();
//                            }
//
//
//                        }
//                        System.out.println("inst_dt=33====" + inst_dt + "currDt===33====" + currDt);
//                        System.out.println("totPrrrrr333rrrrrrrrrrrr=" + DateUtil.dateDiff(inst_dt, currDt) + "ggggg=" + moret);
//                        System.out.println("totPrrrrrrrrrrrrrrrrr=" + DateUtil.dateDiff(inst_dt, currDt) + "ggggg=" + moret);
//                        if (DateUtil.dateDiff(inst_dt, currDt) <= 0 && (moret != null && moret.equals("Y"))) {
//                            dataMap.put("INSTMT_AMT", 0);
//                            dataMap.put("PRINCIPAL", String.valueOf("0"));
//                        }
//
//
//
//                        Date addDt = (Date) currDt.clone();
//                        Date instDt = DateUtil.addDays(inst_dt, 1);
//                        addDt.setDate(instDt.getDate());
//                        addDt.setMonth(instDt.getMonth());
//                        addDt.setYear(instDt.getYear());
//                        loanInstall.put("FROM_DATE", addDt);//DateUtil.addDays(inst_dt,1));
//                        loanInstall.put("TO_DATE", interestUptoDt);
//                        System.out.println("!! getTotalamount#####" + loanInstall);
//                        List lst1 = null;
//                        if (inst_dt != null && (totPrinciple > 0)) {
//                            lst1 = sqlMap.executeQueryForList("getTotalAmountOverDue", loanInstall);
//                            System.out.println("listsize####" + lst1);
//                        }
//                        double principle = 0;
//                        if (lst1 != null && lst1.size() > 0) {
//                            HashMap map = (HashMap) lst1.get(0);
//                            principle = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue();
//                        }
//                        totPrinciple += principle;
//                        System.out.println("totPrinciple 1111####" + totPrinciple);
//
//                        if (!(installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y"))) {
//
//                            if (lst1 != null && lst1.size() > 0) {
//                                HashMap map = (HashMap) lst1.get(0);
//                                principle = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue();
//                            }
//                            totPrinciple += principle;
//                        } else {
//                            if (lst1 != null && lst1.size() > 0) {
//                                HashMap map = (HashMap) lst1.get(0);
//                                System.out.println("snnnnnn" + map);
//                                principle = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue() + CommonUtil.convertObjToDouble(map.get("INTEREST_AMOUNT")).doubleValue();
//                                System.out.println("sdasdsd" + principle);
//                                if (principle == 0) {
//                                    List advList = sqlMap.executeQueryForList("getAdvAmt", loanInstall);
//                                    if (advList.size() > 0 && advList != null) {
//                                        map = (HashMap) advList.get(0);
//                                        if (map.get("TOTAL_AMT") != null) {
//                                            principle = CommonUtil.convertObjToDouble(map.get("TOTAL_AMT"));
//                                        }
//                                        totPrinciple = principle;
//                                    }
//                                } else {
//// totPrinciple += principle;
//                                    totPrinciple = principle;
//                                }
//                                System.out.println("totPrinciple66666" + totPrinciple);
////                             if(totPrinciple<0)
////                             {
////                                 totPrinciple=0;
////                             }
//
//                            } else {
//                                System.out.println("innn eeelllsss333" + totPrinciple);
//                                totPrinciple -= CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
//                                System.out.println("innn eeelllsss333" + totPrinciple);
////                            if(totPrinciple<0)
////                             {
////                                 totPrinciple=0;
////                             }
//                            }
//
//                        }
//
//
//
//
//                        insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(totPrinciple));
//                        insertPenal.put("INSTALL_DT", inst_dt);
//                        if (asAndWhenMap.containsKey("MORATORIUM_INT_FOR_EMI")) {
//                            insertPenal.put("MORATORIUM_INT_FOR_EMI", asAndWhenMap.get("MORATORIUM_INT_FOR_EMI"));
//                        }
//                        if (asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && (asAndWhenMap.get("AS_CUSTOMER_COMES") != null) && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")) {
//                            double interest = CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
//                            double penal = CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
//                            List facilitylst = sqlMap.executeQueryForList("LoneFacilityDetailAD", loanInstall);
//                            if (facilitylst != null && facilitylst.size() > 0) {
//                                HashMap hash = (HashMap) facilitylst.get(0);
//                                if (hash.get("CLEAR_BALANCE") != null) {
//                                    clear_balance = hash.get("CLEAR_BALANCE").toString();
//                                }
//                                System.out.println("clear_balance =" + clear_balance + " aaa---" + loanInstall.get("ACT_NUM"));
//                                hash.put("ACT_NUM", loanInstall.get("ACT_NUM"));
//                                if (asAndWhenMap.containsKey("PREMATURE")) {
//                                    insertPenal.put("PREMATURE", asAndWhenMap.get("PREMATURE"));
//                                }
//                                if (asAndWhenMap.containsKey("PREMATURE") && asAndWhenMap.containsKey("PREMATURE_INT_CALC_AMT")
//                                        && CommonUtil.convertObjToStr(asAndWhenMap.get("PREMATURE_INT_CALC_AMT")).equals("LOANSANCTIONAMT")) {
//                                    hash.put("FROM_DT", hash.get("ACCT_OPEN_DT"));
//                                } else {
//                                    hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
//                                    hash.put("FROM_DT", DateUtil.addDays(((Date) hash.get("FROM_DT")), 2));
//                                }
//                                hash.put("TO_DATE", interestUptoDt.clone());
//                                if (!(asAndWhenMap != null && asAndWhenMap.containsKey("INSTALL_TYPE") && asAndWhenMap.get("INSTALL_TYPE") != null && asAndWhenMap.get("INSTALL_TYPE").equals("EMI"))) {
//                                    facilitylst = sqlMap.executeQueryForList("getPaidPrinciple", hash);
//                                } else {
//                                    facilitylst = null;
//                                    if (asAndWhenMap.containsKey("PRINCIPAL_DUE") && asAndWhenMap.get("PRINCIPAL_DUE") != null) {
//                                        insertPenal.put("CURR_MONTH_PRINCEPLE", asAndWhenMap.get("PRINCIPAL_DUE"));
//                                    }
//                                }
//                                if (facilitylst != null && facilitylst.size() > 0) {
//                                    hash = (HashMap) facilitylst.get(0);
//                                    interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
//                                    penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
//
//                                    insertPenal.put("PAID_INTEREST", hash.get("INTEREST"));
//                                }
//                            }
//                            System.out.println("####interest:" + interest);
//                            if (interest > 0) {
//                                insertPenal.put("CURR_MONTH_INT", new Double(interest));
//                            } else {
//                                insertPenal.put("CURR_MONTH_INT", new Double(0));
//                            }
//                            if (penal > 0) {
//                                insertPenal.put("PENAL_INT", new Double(penal));
//                            } else {
//                                insertPenal.put("PENAL_INT", new Double(0));
//                            }
//                            insertPenal.put("INTEREST", asAndWhenMap.get("INTEREST"));
//                            insertPenal.put("LOAN_CLOSING_PENAL_INT", asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
//
//                            insertPenal.put("LAST_INT_CALC_DT", asAndWhenMap.get("LAST_INT_CALC_DT"));
//                            insertPenal.put("ROI", asAndWhenMap.get("ROI"));
//                            chargeList = sqlMap.executeQueryForList("getChargeDetails", loanInstall);
//                        } else {
//                            List getIntDetails = sqlMap.executeQueryForList("getIntDetails", loanInstall);
//                            HashMap hash = null;
//                            if (getIntDetails != null) {
//                                for (int i = 0; i < getIntDetails.size(); i++) {
//                                    hash = (HashMap) getIntDetails.get(i);
//                                    String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
//                                    double pBal = CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue();
//                                    double iBal = CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
//                                    double pibal = CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
//                                    double excess = CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT")).doubleValue();
//                                    System.out.println("pBalcc99oogytf" + pBal + "::" + iBal + "::" + pibal + "::" + excess);
//                                    pBal -= excess;
//                                    System.out.println("pBalrrr" + pBal);
//                                    if (pBal < totPrinciple) {
//                                        insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(pBal));
//                                    }
//                                    System.out.println("insertPenal5555" + insertPenal);
//                                    if (trn_mode.equals("C*")) {
//                                        insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
//                                        insertPenal.put("PRINCIPLE_BAL", new Double(pBal));
//                                        insertPenal.put("EBAL", hash.get("EBAL"));
//                                        break;
//                                    } else {
//                                        if (!trn_mode.equals("DP")) {
//                                            insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
//                                        }
//                                        insertPenal.put("EBAL", hash.get("EBAL"));
//                                        insertPenal.put("PRINCIPLE_BAL", new Double(pBal));
//                                    }
//                                    System.out.println("int principel detailsINSIDE LOAN##" + insertPenal);
//                                }
//                            }
//                            getIntDetails = sqlMap.executeQueryForList("getPenalIntDetails", loanInstall);
//                            hash = (HashMap) getIntDetails.get(0);
//                            insertPenal.put("PENAL_INT", hash.get("PIBAL"));
//                        }
//                        System.out.println("insertPenalnnnnnnshdcgasdg" + insertPenal);//abbbbb
//                    }
//
//                    if (prodType != null && prodType.equals("AD")) // Only  AD
//                    {
//                        if (asAndWhenMap != null && asAndWhenMap.size() > 0) {
//                            if (asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")) {
//                                List facilitylst = sqlMap.executeQueryForList("LoneFacilityDetailAD", loanInstall);
//                                double interest = CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
//                                double penal = CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
//                                if (facilitylst != null && facilitylst.size() > 0) {
//                                    HashMap hash = (HashMap) facilitylst.get(0);
//                                    if (hash.get("CLEAR_BALANCE") != null) {
//                                        clear_balance = hash.get("CLEAR_BALANCE").toString();
//                                    }
//                                    hash.put("ACT_NUM", loanInstall.get("ACT_NUM"));
//                                    hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
//                                    hash.put("FROM_DT", DateUtil.addDays(((Date) hash.get("FROM_DT")), 2));
//                                    hash.put("TO_DATE", DateUtil.addDaysProperFormat(interestUptoDt, -1));
//                                    facilitylst = sqlMap.executeQueryForList("getPaidPrincipleAD", hash);
//                                    if (facilitylst != null && facilitylst.size() > 0) {
//                                        hash = (HashMap) facilitylst.get(0);
//                                        interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
//                                        penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
//                                    }
//                                }
//                                if (interest > 0) {
//                                    insertPenal.put("CURR_MONTH_INT", new Double(interest));
//                                } else {
//                                    insertPenal.put("CURR_MONTH_INT", new Double(0));
//                                }
//                                if (penal > 0) {
//                                    insertPenal.put("PENAL_INT", new Double(penal));
//                                } else {
//                                    insertPenal.put("PENAL_INT", new Double(0));
//                                }
//                                insertPenal.put("INTEREST", asAndWhenMap.get("INTEREST"));
//                                insertPenal.put("LOAN_CLOSING_PENAL_INT", asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
//                                chargeList = sqlMap.executeQueryForList("getChargeDetails", loanInstall);
//                            } else {
//                                if (prodType != null && prodType.equals("AD")) {
//                                    List getIntDetails = sqlMap.executeQueryForList("getIntDetailsAD", loanInstall);
//                                    HashMap hash = null;
//
//                                    for (int i = 0; i < getIntDetails.size(); i++) {
//                                        hash = (HashMap) getIntDetails.get(i);
//                                        String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
//                                        double iBal = CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
//                                        double pibal = CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
//                                        double excess = CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT")).doubleValue();
//                                        if (trn_mode.equals("C*")) {
//                                            insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
//                                            insertPenal.put("PRINCIPLE_BAL", new Double(CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue() - excess));
//                                            insertPenal.put("EBAL", hash.get("EBAL"));
//                                            break;
//                                        } else {
//                                            insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
//                                            insertPenal.put("PRINCIPLE_BAL", new Double(CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue() - excess));
//                                            insertPenal.put("EBAL", hash.get("EBAL"));
//                                        }
//                                        System.out.println("int principel detailsINSIDE OD" + insertPenal);
//                                    }
//                                    getIntDetails = sqlMap.executeQueryForList("getPenalIntDetailsAD", loanInstall);
//                                    if (getIntDetails.size() > 0) {
//                                        hash = (HashMap) getIntDetails.get(0);
//                                        insertPenal.put("PENAL_INT", hash.get("PIBAL"));
//                                    }
//                                    insertPenal.remove("PRINCIPLE_BAL");
//
//                                }
//                            }
//                        }
//                    }
//                    //Added By Suresh  (Current Dt > To Date AND PBAL >0 in ADV_TRANS_DETAILS, Add Principle_Balance)
//                    if (prodType != null && prodType.equals("AD")) {
//                        double pBalance = 0.0;
//                        Date expDt = null;
//                        List expDtList = sqlMap.executeQueryForList("getLoanExpDate", loanInstall);
//                        if (expDtList != null && expDtList.size() > 0) {
//                            whereMap = new HashMap();
//                            whereMap = (HashMap) expDtList.get(0);
//                            pBalance = CommonUtil.convertObjToDouble(whereMap.get("PBAL")).doubleValue();
//                            expDt = (Date) whereMap.get("TO_DT");
//                            long diffDayPending = DateUtil.dateDiff(expDt, intUptoDt);
//                            System.out.println("############# Insert PBalance" + pBalance + "######diffDayPending :" + diffDayPending);
//                            if (diffDayPending > 0 && pBalance > 0) {
//                                insertPenal.put("PRINCIPLE_BAL", new Double(pBalance));
//                            }
//                        }
//                    }
//                    System.out.println("####### insertPenal : " + insertPenal);
//                    //Charges
//                    double chargeAmt = 0.0;
//                    whereMap = new HashMap();
//                    whereMap.put("ACT_NUM", actNum);
//                    chargeAmt = getChargeAmount(whereMap, prodType);
//                    if (chargeAmt > 0) {
//                        dataMap.put("CHARGES", String.valueOf(chargeAmt));
//                    } else {
//                        dataMap.put("CHARGES", "0");
//                    }
//                    System.out.println("####### Single Row insertPenal : " + insertPenal);
//                    double totalDemand = 0.0;
//                    double principalAmount = 0.0;
//                    if (insertPenal.containsKey("CURR_MONTH_PRINCEPLE")) {
//                        principalAmount = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_PRINCEPLE")).doubleValue();     // Principal Amount
//                        totalDemand = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue()
//                                + CommonUtil.convertObjToDouble(insertPenal.get("PENAL_INT")).doubleValue() + chargeAmt;
//                        System.out.println("totalDemand 111====" + totalDemand);
//                    } else {
//                        principalAmount = CommonUtil.convertObjToDouble(insertPenal.get("PRINCIPLE_BAL")).doubleValue();     // Principal Amount
//                        totalDemand = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue()
//                                + CommonUtil.convertObjToDouble(insertPenal.get("PENAL_INT")).doubleValue() + chargeAmt;
//                        System.out.println("totalDemand 222====" + totalDemand);
//                    }
//
//
//                    if (inst_dt != null && prodType.equals("TL")) {
//                        // if (retired != null && retired.equals("YES")) {
//                        //   dataMap.put("PRINCIPAL",String.valueOf(Math.abs(Integer.parseInt(clear_balance))));
//                        // } else {
//                        System.out.println("intUptoDt" + intUptoDt + "inst_dt" + inst_dt);
//                        if (DateUtil.dateDiff(intUptoDt, inst_dt) <= 0) {
//                            if (installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y")) {
//                                principalAmount = principalAmount - CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT"));
//                                dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
//                            } else {
//                                dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
//                            }
//                        } else {
//                            if (installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y")) {
//                                principalAmount = principalAmount - CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT"));
//                                dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
//                            } else {
//                                dataMap.put("PRINCIPAL", "0");
//                                principalAmount = 0.0;
//                            }
//                        }
//                        // }
//
//
////                     if(installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y")){
////                    
////                     HashMap balanceMap = new HashMap();
////                      double totalPrincAmount=0.0;
////                      double balanceLoanAmt =0.0;
////                        double finalDemandAmt =0.0;
////                    List sumInstLstemi = sqlMap.executeQueryForList("getPrincipalAmtGreaterThanBalAmtforemi", balanceMap);
////                                if(sumInstLstemi!=null && sumInstLstemi.size()>0){
////                                    balanceMap =(HashMap) sumInstLstemi.get(0);
////                                    totalPrincAmount = CommonUtil.convertObjToDouble(balanceMap.get("TOTAL_AMT")).doubleValue();
////                                    //totalPrincAmount+=instalAmt;
////                                    finalDemandAmt = totalPrincAmount - paidAmount;
////                                    if(balanceLoanAmt>finalDemandAmt){
////                                        dataMap.put("PRINCIPAL", String.valueOf(instalAmt));
////                                        principalAmount = instalAmt;
////                                    }else{
////                                        dataMap.put("PRINCIPAL", String.valueOf(balanceLoanAmt));
////                                        principalAmount = balanceLoanAmt;
////                                    }
////                                }
////                    
////                    
////                    
////                    
////                    
////                     }
//
//                        // IF SALARY_RECOVERY = 'YES' ONLY
//                        if ((principalAmount == 0 && (!(installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y")))) || (installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y"))) {
//                            System.out.println("############ instalAmt : " + instalAmt);
//                            HashMap balanceMap = new HashMap();
//                            double balanceLoanAmt = 0.0;
//                            double finalDemandAmt = 0.0;
//                            System.out.println("############ actNum : " + actNum);
//                            balanceMap.put("ACCOUNTNO", actNum);
//                            List balannceAmtLst = sqlMap.executeQueryForList("getBalanceLoanPrincipalAmt", balanceMap);
//                            if (balannceAmtLst != null && balannceAmtLst.size() > 0) {
//                                balanceMap = (HashMap) balannceAmtLst.get(0);
//                                balanceLoanAmt = CommonUtil.convertObjToDouble(balanceMap.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();
//                                System.out.println("############ instalAmt : " + instalAmt);
//                                System.out.println("############ LoanBalancePrincAmt : " + balanceLoanAmt);
//                                double checkAmt = 0.0;
//                                double totalPrincAmount = 0.0;
//                                checkAmt = balanceLoanAmt - instalAmt;
//                                if (checkAmt > 0) {
//                                    if (!(installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y"))) {
//                                        balanceMap.put("ACCT_NUM", actNum);
//                                        balanceMap.put("BALANCE_AMT", String.valueOf(checkAmt));
//                                        List sumInstLst = sqlMap.executeQueryForList("getPrincipalAmtGreaterThanBalAmt", balanceMap);
//                                        if (sumInstLst != null && sumInstLst.size() > 0) {
//                                            balanceMap = (HashMap) sumInstLst.get(0);
//                                            totalPrincAmount = CommonUtil.convertObjToDouble(balanceMap.get("PRINCIPAL_AMOUNT")).doubleValue();
//                                            totalPrincAmount += instalAmt;
//                                            finalDemandAmt = totalPrincAmount - paidAmount;
//                                            if (balanceLoanAmt > finalDemandAmt) {
//                                                dataMap.put("PRINCIPAL", String.valueOf(finalDemandAmt));
//                                                principalAmount = finalDemandAmt;
//                                            } else {
//                                                dataMap.put("PRINCIPAL", String.valueOf(balanceLoanAmt));
//                                                principalAmount = balanceLoanAmt;
//                                            }
//                                        }
//                                        //bbau11
//                                        List aList = ServerUtil.executeQuery("getMorotorium", loanInstall);
//                                        String moret = "";
//                                        if (aList.size() > 0 && aList.get(0) != null) {
//                                            HashMap mapop = (HashMap) aList.get(0);
//                                            if (mapop.get("MORATORIUM_GIVEN") != null) {
//                                                moret = mapop.get("MORATORIUM_GIVEN").toString();
//                                            }
//
//
//                                        }
//                                        System.out.println("inst_dt==11===" + inst_dt + "currDt=11======" + currDt);
//                                        System.out.println("totPrrrrrrr11rrrrrrrrrr=" + DateUtil.dateDiff(inst_dt, currDt) + "ggggg=" + moret);
//                                        System.out.println("totPrrrrrrrrrrrrrrrrr=" + DateUtil.dateDiff(inst_dt, currDt) + "ggggg=" + moret);
//                                        if (DateUtil.dateDiff(inst_dt, currDt) <= 0 && (moret != null && moret.equals("Y"))) {
//                                            finalDemandAmt = 0;
//                                            principalAmount = 0;
//                                            //break;
//                                        }
//                                        System.out.println("############ finalDemandAmt : " + finalDemandAmt);
//                                    }
//                                } else {
//                                    HashMap transMap = new HashMap();
//                                    transMap.put("ACT_NUM", actNum);
//                                    transMap.put("BRANCH_CODE", _branchCode);
//                                    List sanctionLst = sqlMap.executeQueryForList("getNoOfDaysinLoan", transMap);
//                                    if (sanctionLst != null && sanctionLst.size() > 0) {
//                                        HashMap recordMap = (HashMap) sanctionLst.get(0);
//                                        int repayFreq = 0;
//                                        repayFreq = CommonUtil.convertObjToInt(recordMap.get("REPAYMENT_FREQUENCY"));
//                                        if (repayFreq == 1) {
//                                            Date expiry_dt = null;
//                                            expiry_dt = (Date) recordMap.get("TO_DT");
//                                            expiry_dt = (Date) expiry_dt.clone();
//                                            System.out.println("########## expiry_dt : " + expiry_dt);
//                                            if (DateUtil.dateDiff(intUptoDt, expiry_dt) >= 0) {
//                                                principalAmount = 0.0;
//                                                dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
//                                            } else {
//                                                dataMap.put("PRINCIPAL", String.valueOf(balanceLoanAmt));
//                                                principalAmount = balanceLoanAmt;
//                                            }
//                                        } else {
//                                            dataMap.put("PRINCIPAL", String.valueOf(balanceLoanAmt));
//                                            principalAmount = balanceLoanAmt;
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    if (prodType.equals("AD")) {
//                        // if (retired != null && retired.equals("YES")) {
//                        // dataMap.put("PRINCIPAL",String.valueOf(Math.abs(Integer.parseInt(clear_balance))));
//                        //  } else {
//                        if (principalAmount > 0) {
//                            dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
//                        } else {
//
//                            dataMap.put("PRINCIPAL", "0");
//
//                        }
//                        // }    
//                    }
//
//                    if (installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y")) {
//                        double dueamount = 0;
//                        double penal = 0;
//                        double totEmi = 0;
//                        double paidEmi = 0;
//                        double principle = 0;
//                        double interst = Double.parseDouble(insertPenal.get("CURR_MONTH_INT").toString());
//
//                        System.out.println("interst" + interst);
//                        HashMap emi = new HashMap();
//                        Date upto = (Date) currDt.clone();
//                        emi.put("ACC_NUM", actNum);
//                        emi.put("UP_TO", upto);
//
//                        List totalEmiList = sqlMap.executeQueryForList("TotalEmi", emi);
//                        if (totalEmiList != null && totalEmiList.size() > 0 && ((HashMap) totalEmiList.get(0)).get("TOTAL_AMOUNT") != null) {
//                            HashMap aMap = new HashMap();
//                            aMap = (HashMap) totalEmiList.get(0);
//                            totEmi = Double.parseDouble(aMap.get("TOTAL_AMOUNT").toString());
//                            System.out.println("TOTAL_AMOUNTv" + totEmi);
//                        } else {
//                            totEmi = 0;
//
//                        }
//                        HashMap paid = new HashMap();
//                        paid.put("ACT_NUM", actNum);
//                        paid.put("BRANCH_CODE", _branchCode);
//                        List paidAmtemi = sqlMap.executeQueryForList("getPaidPrincipleEMI", loanInstall);
//                        if (paidAmtemi != null && paidAmtemi.size() > 0 && ((HashMap) paidAmtemi.get(0)).get("PRINCIPLE") != null) {
//                            paid = (HashMap) paidAmtemi.get(0);
//                            System.out.println("!!!!asAndWhenMap:" + paid);
//                            // System.out.println("allInstallmentMap..."+allInstallmentMap);
//                            paidEmi = CommonUtil.convertObjToDouble(paid.get("PRINCIPLE")).doubleValue();
//                            System.out.println("paidEmi" + paidEmi);
//                        } else {
//                            paidEmi = 0;
//                        }
//                        System.out.println("totEmi" + totEmi + "paidEmi" + paidEmi);
//                        dueamount = totEmi - paidEmi;
//                        double paidamount = paidEmi;
//                        if (dueamount <= 0) {
//                            dueamount = CommonUtil.convertObjToDouble(dataMap.get("PRINCIPAL")) + interst;
//                        }
//                        System.out.println("totalDemandsdas" + dueamount);
//
//
//                        System.out.println("+==========PENAL STARTS==============================+");
//                        List scheduleList = sqlMap.executeQueryForList("getSchedules", emi);
//                        //  List penalInterstRate=
//
//
//                        Date penalStrats = new Date();
//                        if (scheduleList != null && scheduleList.size() > 0) {
//                            for (int k = 0; k < scheduleList.size(); k++) {
//                                HashMap eachInstall = new HashMap();
//                                eachInstall = (HashMap) scheduleList.get(k);
//                                System.out.println("eachInstall" + eachInstall);
//                                double scheduledEmi = Double.parseDouble(eachInstall.get("TOTAL_AMT").toString());
//                                if (paidamount >= scheduledEmi) {
//                                    System.out.println("111paidamount" + paidamount + "scheduledEmi" + scheduledEmi);
//                                    paidamount = paidamount - scheduledEmi;
//                                    System.out.println("paidamount" + paidamount);
//                                } else {
//                                    String in_date = CommonUtil.convertObjToStr(eachInstall.get("INSTALLMENT_DT"));
//                                    penalStrats = DateUtil.getDateMMDDYYYY(in_date);
//                                    //   penalStrats=DateUtil.getDateMMDDYYYY(eachInstall.get("INSTALLMENT_DT").toString());
//                                    System.out.println("penalStrats....." + penalStrats);
//                                    break;
//                                }
//
//
//                            }
//                            emi.put("FROM_DATE", penalStrats);
//                            List getPenalData = sqlMap.executeQueryForList("getPenalData", emi);
//                            List penalInterstRate = sqlMap.executeQueryForList("getPenalIntestRatefromMaintenance", emi);
//                            double interstPenal = 0;
//                            double garce = 0;
//                            List graceDays = sqlMap.executeQueryForList("getGracePeriodDays", emi);
//                            if (graceDays != null && graceDays.size() > 0) {
//                                HashMap map = new HashMap();
//                                map = (HashMap) graceDays.get(0);
//                                if (map != null && map.containsKey("GRACE_PERIOD_DAYS") && map.get("GRACE_PERIOD_DAYS") != null) {
//                                    garce = Double.parseDouble(map.get("GRACE_PERIOD_DAYS").toString());
//                                } else {
//                                    garce = 0;
//                                }
//                            } else {
//                                garce = 0;
//                            }
//                            long gracedy = (long) garce;
//                            int graceint = (int) garce;
//                            if (penalInterstRate != null && penalInterstRate.size() > 0) {
//                                HashMap test = new HashMap();
//                                test = (HashMap) penalInterstRate.get(0);
//                                if (test != null && test.containsKey("PENAL_INTEREST") && test.get("PENAL_INTEREST") != null) {
//                                    interstPenal = Double.parseDouble(test.get("PENAL_INTEREST").toString());
//                                } else {
//                                    List limitList = sqlMap.executeQueryForList("getLimitFromLoanSanc", emi);
//                                    double limit = Double.parseDouble(((HashMap) limitList.get(0)).get("LIMIT").toString());
//                                    emi.put("LIMIT", limit);
//                                    List penalFromROI = sqlMap.executeQueryForList("getPenalIntestRatefromROI", emi);
//                                    if (penalFromROI != null && penalFromROI.size() > 0) {
//                                        test = new HashMap();
//                                        test = (HashMap) penalFromROI.get(0);
//                                        System.out.println("testttt" + test);
//                                        interstPenal = Double.parseDouble(test.get("PENAL_INT").toString());
//                                    }
//                                }
//                            } else {
//                                List limitList = sqlMap.executeQueryForList("getLimitFromLoanSanc", emi);
//                                double limit = Double.parseDouble(((HashMap) limitList.get(0)).get("LIMIT").toString());
//                                emi.put("LIMIT", limit);
//                                List penalFromROI = sqlMap.executeQueryForList("getPenalIntestRatefromROI", emi);
//                                if (penalFromROI != null && penalFromROI.size() > 0) {
//                                    HashMap test = new HashMap();
//                                    test = (HashMap) penalFromROI.get(0);
//                                    interstPenal = Double.parseDouble(test.get("PENAL_INT").toString());
//                                }
//                            }
//                            System.out.println("interstPenal...." + interstPenal);
//                            if (getPenalData != null && getPenalData.size() > 0) {
//                                for (int k = 0; k < getPenalData.size(); k++) {
//                                    HashMap amap = new HashMap();
//                                    amap = (HashMap) getPenalData.get(k);
//
//
//
//                                    String in_date = CommonUtil.convertObjToStr(amap.get("INSTALLMENT_DT"));
//                                    Date currntDate = DateUtil.getDateMMDDYYYY(in_date);
//                                    currntDate = DateUtil.addDays(currntDate, graceint);
//                                    System.out.println("5555currntDate.." + currntDate);
//                                    // InterestCalculationTask incalc=new InterestCalculationTask(); 
//                                    HashMap holidayMap = new HashMap();
//                                    holidayMap.put("CURR_DATE", currntDate);
//                                    holidayMap.put("BRANCH_CODE", _branchCode);
////                                             currntDate=incalc.holiydaychecking(holidayMap);
//
//
//
//
//                                    currntDate = setProperDtFormat(currntDate);
////                            System.out.println("instDate   "+currntDate);
////                            holidayMap.put("NEXT_DATE",currntDate);
////                            holidayMap.put("BRANCH_CODE",_branchCode);
//                                    holidayMap = new HashMap();
//                                    boolean checkHoliday = true;
//                                    String str = "any next working day";
//                                    System.out.println("instDate   " + currntDate);
//                                    currntDate = setProperDtFormat(currntDate);
//                                    holidayMap.put("NEXT_DATE", currntDate);
//                                    holidayMap.put("BRANCH_CODE", _branchCode);
//                                    while (checkHoliday) {
//                                        boolean tholiday = false;
//                                        List Holiday = sqlMap.executeQueryForList("checkHolidayDateOD", holidayMap);
//                                        List weeklyOf = sqlMap.executeQueryForList("checkWeeklyOffOD", holidayMap);
//                                        boolean isHoliday = Holiday.size() > 0 ? true : false;
//                                        boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
//                                        if (isHoliday || isWeekOff) {
//                                            // System.out.println("#### diffDayPending Holiday True : "+diffDayPending);
//                                            if (str.equals("any next working day")) {
//                                                // diffDayPending-=1;
//                                                currntDate.setDate(currntDate.getDate() + 1);
//                                            } else {
//                                                //diffDayPending+=1;
//                                                currntDate.setDate(currntDate.getDate() - 1);
//                                            }
//                                            holidayMap.put("NEXT_DATE", currntDate);
//                                            checkHoliday = true;
//                                            System.out.println("#### holidayMap : " + holidayMap);
//                                        } else {
//                                            //System.out.println("#### diffDay Holiday False : "+diffDayPending);
//                                            checkHoliday = false;
//                                        }
//                                    }
//                                    System.out.println("currntDatemmm" + currntDate);
//                                    //  Date currntDate=DateUtil.getDateMMDDYYYY(amap.get("INSTALLMENT_DT").toString());
//                                    System.out.println("DateUtil.dateDiff(currntDate,upto)" + DateUtil.dateDiff(currntDate, upto));
//
//                                    long difference = DateUtil.dateDiff(currntDate, upto) - 1;
//                                    if (difference < 0) {
//                                        difference = 0;
//                                    }
//                                    System.out.println("difference..." + difference);
//                                    double installment = Double.parseDouble(amap.get("TOTAL_AMT").toString());
//                                    System.out.println("installmentsadeasdasd" + installment);
//                                    penal = penal + ((installment * difference * interstPenal) / 36500);
//                                    System.out.println("penallcalcuuu" + penal);
//                                }
//
//                            }
//
//                        }
//                        principle = dueamount - interst;
//                        System.out.println("mmmprinciple" + principle + "::penal" + penal + "::interst" + interst);
//                        totalDemand = principle + penal + interst;
//                        totalDemand = Math.round(totalDemand);
//                        principle = Math.round(principle);
//                        penal = Math.round(penal);
//                        interst = Math.round(interst);
//                        System.out.println("tttttoooo" + totalDemand);
//                        dataMap.put("INTEREST", Math.round(Double.parseDouble(insertPenal.get("CURR_MONTH_INT").toString())));
//                        dataMap.put("PENAL", penal);
//                        dataMap.put("TOTAL_DEMAND", new Double(totalDemand));
//                        dataMap.put("CLEAR_BALANCE", clear_balance);
//                        System.out.println("mmmmmmiiinnneee" + dataMap);
//                    } else {
//                        totalDemand += principalAmount;
//                        System.out.println("totalDemand 333====" + totalDemand);
//                        dataMap.put("INTEREST", insertPenal.get("CURR_MONTH_INT"));
//                        dataMap.put("PENAL", insertPenal.get("PENAL_INT"));
//                        dataMap.put("TOTAL_DEMAND", new Double(totalDemand));
//                        dataMap.put("CLEAR_BALANCE", clear_balance);
//                    }
//
//                    if (flag) {
//                        dataMap.put("PRINCIPAL", 0);
//                        flag = false;
//                    }
//
//                    //if(totalDemand<=0){
//                    //    dataMap = null;
//                    // }
//                }
//            } else {
//                System.out.println("### Not Allow : " + checkDate);
//                dataMap = null;
//            }
//        }
//
//
//        System.out.println("####### Single Row DataMap : " + dataMap);
//        return dataMap;
//
//        /*
//         * HashMap dataMap=new HashMap(); HashMap whereMap=new HashMap();
//         * whereMap.put("ACT_NUM",actNum); List parameterList =
//         * sqlMap.executeQueryForList("getRecoveryParameter", whereMap);
//         * if(parameterList!=null && parameterList.size()>0){ int firstDay=0;
//         * Date inst_dt=null; Date checkDate = (Date)currDt.clone(); Date
//         * sanctionDt = null; whereMap=(HashMap) parameterList.get(0); firstDay
//         * =CommonUtil.convertObjToInt(whereMap.get("FIRST_DAY")); sanctionDt =
//         * (Date) whereMap.get("FROM_DT"); System.out.println("### First Day :
//         * "+firstDay); System.out.println("### checkDat : "+checkDate);
//         * System.out.println("### sanctionDt : "+sanctionDt);
//         * checkDate.setDate(firstDay); System.out.println("### checkDat :
//         * "+checkDate); long diffDay; diffDay= DateUtil.dateDiff((Date)
//         * sanctionDt, (Date) checkDate); System.out.println("### diffDay :
//         * "+diffDay); if (diffDay >= 0.0) { System.out.println("### Allow :
//         * "+checkDate);
//         *
//         * // IF SALARY_RECOVERY = 'NO' ONLY if(recoveryYesNo.equals("N")){ Date
//         * recoveryCheckDt = null; Date maxInstDt = null; Date lastIntCalcDt =
//         * null; long diffDayPending = 0; recoveryCheckDt =
//         * (Date)interestUptoDt.clone();
//         * recoveryCheckDt.setMonth(recoveryCheckDt.getMonth()); recoveryCheckDt
//         * = setProperDtFormat(recoveryCheckDt); System.out.println("###########
//         * After interestUptoDt : "+interestUptoDt);
//         * System.out.println("########### After recoveryCheckDt :
//         * "+recoveryCheckDt); HashMap instMap = new HashMap();
//         * instMap.put("ACT_NUM",actNum);
//         * instMap.put("CHECK_DT",recoveryCheckDt); List instList =
//         * sqlMap.executeQueryForList("getLoanMaxInstDt", instMap);
//         * if(instList!=null && instList.size()>0){ instMap=(HashMap)
//         * instList.get(0); maxInstDt = (Date)instMap.get("INSTALLMENT_DT");
//         * HashMap intCalcMap = new HashMap(); intCalcMap.put("WHERE",actNum);
//         * List intCalcList = sqlMap.executeQueryForList("getLastIntCalDateAD",
//         * intCalcMap); if(intCalcList!=null && intCalcList.size()>0 ){
//         * intCalcMap=(HashMap) intCalcList.get(0); if
//         * (intCalcMap.get("LAST_INT_CALC_DT")!=null) { lastIntCalcDt =
//         * (Date)intCalcMap.get("LAST_INT_CALC_DT");
//         * System.out.println("########### lastIntCalcDt : "+lastIntCalcDt);
//         * System.out.println("########### maxInstDt : "+maxInstDt);
//         * System.out.println("###########interestUptoDt : "+interestUptoDt);
//         * diffDayPending= DateUtil.dateDiff((Date) lastIntCalcDt, (Date)
//         * maxInstDt); System.out.println("### diffDayPending :
//         * "+diffDayPending); if(diffDayPending<=0){
//         * System.out.println("########### NOT ALLOWED : "); dataMap = null;
//         * return dataMap; }else{ System.out.println("########### ALLOWED : ");
//         * } } else { dataMap = null; return dataMap; } } } }
//         *
//         * HashMap asAndWhenMap = interestCalculationTLAD(actNum, prodId,
//         * prodType); System.out.println("@#@ asAndWhenMap is
//         * >>>>"+asAndWhenMap);
//         * System.out.println("transDetail"+actNum+_branchCode); HashMap
//         * insertPenal=new HashMap(); List chargeList=null; HashMap loanInstall
//         * = new HashMap(); loanInstall.put("ACT_NUM",actNum);
//         * loanInstall.put("BRANCH_CODE", _branchCode); double instalAmt=0.0;
//         * double paidAmount=0.0; HashMap emiMap=new HashMap(); String
//         * installtype=""; String emi_uniform=""; if(prodType !=null &&
//         * prodType.equals("TL")) { //Only TL double totPrinciple=0.0; List
//         * emiList=sqlMap.executeQueryForList("getEmiTypeDetail", loanInstall);
//         * if(emiList.size()>0){ emiMap=(HashMap)emiList.get(0);
//         * installtype=emiMap.get("INSTALL_TYPE").toString();
//         * emi_uniform=emiMap.get("EMI_IN_SIMPLEINTREST").toString(); } HashMap
//         * allInstallmentMap=null;
//         * if(!(installtype.equals("UNIFORM_PRINCIPLE_EMI") &&
//         * emi_uniform.equals("Y") )){ List
//         * paidAmt=sqlMap.executeQueryForList("getPaidPrinciple", loanInstall);
//         * allInstallmentMap=(HashMap)paidAmt.get(0);
//         * System.out.println("!!!!asAndWhenMap:"+asAndWhenMap);
//         * totPrinciple=CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
//         * paidAmount =
//         * CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
//         * if(asAndWhenMap ==null || (asAndWhenMap !=null &&
//         * asAndWhenMap.containsKey("AS_CUSTOMER_COMES") &&
//         * (asAndWhenMap.get("AS_CUSTOMER_COMES")!=null) &&
//         * asAndWhenMap.get("AS_CUSTOMER_COMES").equals("N"))){
//         * paidAmt=sqlMap.executeQueryForList("getIntDetails", loanInstall); if
//         * (paidAmt != null && paidAmt.size() > 0) {
//         * allInstallmentMap=(HashMap)paidAmt.get(0); } double
//         * totExcessAmt=CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
//         * totPrinciple+=totExcessAmt; } List
//         * lst=sqlMap.executeQueryForList("getAllLoanInstallment", loanInstall);
//         * for(int i=0;i<lst.size();i++) {
//         * allInstallmentMap=(HashMap)lst.get(i);
//         * instalAmt=CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
//         * if(instalAmt<=totPrinciple) { totPrinciple-=instalAmt; inst_dt=new
//         * Date(); String
//         * in_date=CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
//         * inst_dt=DateUtil.getDateMMDDYYYY(in_date); } else { inst_dt=new
//         * Date(); String
//         * in_date=CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
//         * inst_dt=DateUtil.getDateMMDDYYYY(in_date);
//         * totPrinciple=CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue()-totPrinciple;
//         * break; } } } else { List
//         * paidAmtemi=sqlMap.executeQueryForList("getPaidPrincipleEMI1",
//         * loanInstall); allInstallmentMap=(HashMap)paidAmtemi.get(0);
//         * System.out.println("!!!!asAndWhenMap:"+asAndWhenMap);
//         * System.out.println("allInstallmentMap..."+allInstallmentMap);
//         * totPrinciple=CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
//         * paidAmount =
//         * CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
//         * System.out.println("totPrinciple22:"+totPrinciple+" ::"+paidAmount);
//         * if(asAndWhenMap ==null || (asAndWhenMap !=null &&
//         * asAndWhenMap.containsKey("AS_CUSTOMER_COMES") &&
//         * (asAndWhenMap.get("AS_CUSTOMER_COMES")!=null) &&
//         * asAndWhenMap.get("AS_CUSTOMER_COMES").equals("N"))){
//         * paidAmtemi=sqlMap.executeQueryForList("getIntDetails", loanInstall);
//         * if (paidAmtemi != null && paidAmtemi.size() > 0) {
//         * allInstallmentMap=(HashMap)paidAmtemi.get(0); }
//         * System.out.println("allInstallmentMap444"+allInstallmentMap); double
//         * totExcessAmt=CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
//         * totPrinciple+=totExcessAmt;
//         * System.out.println("totPrinciple"+totPrinciple); } List
//         * lst=sqlMap.executeQueryForList("getAllLoanInstallment", loanInstall);
//         * for(int i=0;i<lst.size();i++) {
//         * allInstallmentMap=(HashMap)lst.get(i);
//         * System.out.println("allInstallmentMap34243"+allInstallmentMap);
//         * instalAmt=CommonUtil.convertObjToDouble(allInstallmentMap.get("TOTAL_AMT")).doubleValue();
//         * System.out.println("111222instalAmt"+instalAmt+":::"+totPrinciple);
//         * if(instalAmt<=totPrinciple) { totPrinciple-=instalAmt;
//         * System.out.println("chhhhnnn"+totPrinciple); inst_dt=new Date();
//         * String
//         * in_date=CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
//         * inst_dt=DateUtil.getDateMMDDYYYY(in_date); } else { inst_dt=new
//         * Date(); String
//         * in_date=CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
//         * inst_dt=DateUtil.getDateMMDDYYYY(in_date);
//         * totPrinciple=CommonUtil.convertObjToDouble(allInstallmentMap.get("TOTAL_AMT")).doubleValue()-totPrinciple;
//         * System.out.println("bbbkkk"+totPrinciple); break; }
//         * System.out.println("totPrinciple@@@@@@@@@@@@"+totPrinciple); } }
//         *
//         *
//         * Date addDt=(Date)currDt.clone(); Date
//         * instDt=DateUtil.addDays(inst_dt,1); addDt.setDate(instDt.getDate());
//         * addDt.setMonth(instDt.getMonth()); addDt.setYear(instDt.getYear());
//         * loanInstall.put("FROM_DATE",addDt);//DateUtil.addDays(inst_dt,1));
//         * loanInstall.put("TO_DATE",interestUptoDt); System.out.println("!!
//         * getTotalamount#####"+loanInstall); List lst1=null; if(inst_dt !=null
//         * &&(totPrinciple>0)) {
//         * lst1=sqlMap.executeQueryForList("getTotalAmountOverDue",loanInstall);
//         * System.out.println("listsize####"+lst1); } double principle=0;
//         * if(!(installtype.equals("UNIFORM_PRINCIPLE_EMI") &&
//         * emi_uniform.equals("Y"))){ if(lst1 !=null && lst1.size()>0){ HashMap
//         * map=(HashMap)lst1.get(0);
//         * principle=CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue();
//         * } totPrinciple+=principle; } else { if(lst1 !=null && lst1.size()>0
//         * ){ HashMap map=(HashMap)lst1.get(0);
//         * System.out.println("snnnnnn"+map);
//         * principle=CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue()+CommonUtil.convertObjToDouble(map.get("INTEREST_AMOUNT")).doubleValue();
//         * System.out.println("sdasdsd"+principle); if(principle==0){
//         * if(principle==0){ List
//         * advList=sqlMap.executeQueryForList("getAdvAmt", loanInstall);
//         * if(advList.size()>0 && advList!=null){ map=(HashMap)advList.get(0);
//         * if(map.get("TOTAL_AMT")!=null){
//         * principle=CommonUtil.convertObjToDouble(map.get("TOTAL_AMT")); }
//         * totPrinciple=principle ; } } }else{ // totPrinciple += principle;
//         * totPrinciple = principle; }
//         * System.out.println("totPrinciple66666"+totPrinciple); //
//         * if(totPrinciple<0) // { // totPrinciple=0; // }
//         *
//         * } else { System.out.println("innn eeelllsss333"+totPrinciple);
//         * totPrinciple-=CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
//         * System.out.println("innn eeelllsss333"+totPrinciple); //
//         * if(totPrinciple<0) // { // totPrinciple=0; // } }
//         *
//         * }
//         * insertPenal.put("CURR_MONTH_PRINCEPLE",new Double(totPrinciple));
//         * insertPenal.put("INSTALL_DT",inst_dt); if
//         * (asAndWhenMap.containsKey("MORATORIUM_INT_FOR_EMI")) {
//         * insertPenal.put("MORATORIUM_INT_FOR_EMI",asAndWhenMap.get("MORATORIUM_INT_FOR_EMI"));
//         * } if(asAndWhenMap !=null &&
//         * asAndWhenMap.containsKey("AS_CUSTOMER_COMES") &&
//         * (asAndWhenMap.get("AS_CUSTOMER_COMES")!=null) &&
//         * asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")){ double
//         * interest=CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
//         * double penal
//         * =CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
//         * List
//         * facilitylst=sqlMap.executeQueryForList("LoneFacilityDetailAD",loanInstall);
//         * if(facilitylst!=null && facilitylst.size()>0){ HashMap
//         * hash=(HashMap)facilitylst.get(0);
//         * hash.put("ACT_NUM",loanInstall.get("ACT_NUM")); if
//         * (asAndWhenMap.containsKey("PREMATURE")) {
//         * insertPenal.put("PREMATURE",asAndWhenMap.get("PREMATURE")); } if
//         * (asAndWhenMap.containsKey("PREMATURE") &&
//         * asAndWhenMap.containsKey("PREMATURE_INT_CALC_AMT") &&
//         * CommonUtil.convertObjToStr(asAndWhenMap.get("PREMATURE_INT_CALC_AMT")).equals("LOANSANCTIONAMT"))
//         * { hash.put("FROM_DT",hash.get("ACCT_OPEN_DT")); } else {
//         * hash.put("FROM_DT",hash.get("LAST_INT_CALC_DT"));
//         * hash.put("FROM_DT",DateUtil.addDays(((Date)hash.get("FROM_DT")),2));
//         * } hash.put("TO_DATE", interestUptoDt.clone()); if (!(asAndWhenMap !=
//         * null && asAndWhenMap.containsKey("INSTALL_TYPE") &&
//         * asAndWhenMap.get("INSTALL_TYPE") != null &&
//         * asAndWhenMap.get("INSTALL_TYPE").equals("EMI"))) {
//         * facilitylst=sqlMap.executeQueryForList("getPaidPrinciple",hash); }
//         * else { facilitylst=null; if(
//         * asAndWhenMap.containsKey("PRINCIPAL_DUE") &&
//         * asAndWhenMap.get("PRINCIPAL_DUE")!=null){
//         * insertPenal.put("CURR_MONTH_PRINCEPLE",asAndWhenMap.get("PRINCIPAL_DUE"));
//         * } } if(facilitylst!=null && facilitylst.size()>0){
//         * hash=(HashMap)facilitylst.get(0);
//         * interest-=CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
//         * penal-=CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
//         *
//         * insertPenal.put("PAID_INTEREST",hash.get("INTEREST")); } }
//         * System.out.println("####interest:"+interest); if (interest > 0) {
//         * insertPenal.put("CURR_MONTH_INT",new Double(interest)); } else {
//         * insertPenal.put("CURR_MONTH_INT",new Double(0)); } if (penal > 0) {
//         * insertPenal.put("PENAL_INT",new Double(penal)); } else {
//         * insertPenal.put("PENAL_INT",new Double(0)); }
//         * insertPenal.put("INTEREST",asAndWhenMap.get("INTEREST"));
//         * insertPenal.put("LOAN_CLOSING_PENAL_INT",asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
//         *
//         * insertPenal.put("LAST_INT_CALC_DT",asAndWhenMap.get("LAST_INT_CALC_DT"));
//         * insertPenal.put("ROI",asAndWhenMap.get("ROI"));
//         * chargeList=sqlMap.executeQueryForList("getChargeDetails",loanInstall);
//         * }else{ List getIntDetails=sqlMap.executeQueryForList("getIntDetails",
//         * loanInstall); HashMap hash=null; if (getIntDetails != null) { for(int
//         * i=0;i<getIntDetails.size();i++){ hash=(HashMap)getIntDetails.get(i);
//         * String trn_mode=CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
//         * double
//         * pBal=CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue();
//         * double
//         * iBal=CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
//         * double pibal=
//         * CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
//         * double excess=
//         * CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT")).doubleValue();
//         * pBal-=excess; if (pBal < totPrinciple) {
//         * insertPenal.put("CURR_MONTH_PRINCEPLE",new Double(pBal)); }
//         * if(trn_mode.equals("C*")){ insertPenal.put("CURR_MONTH_INT",
//         * String.valueOf(iBal + pibal)); insertPenal.put("PRINCIPLE_BAL",new
//         * Double(pBal)); insertPenal.put("EBAL",hash.get("EBAL")); break; }
//         * else { if (!trn_mode.equals("DP")) {
//         * insertPenal.put("CURR_MONTH_INT",String.valueOf(iBal + pibal)); }
//         * insertPenal.put("EBAL",hash.get("EBAL"));
//         * insertPenal.put("PRINCIPLE_BAL",new Double(pBal)); }
//         * System.out.println("int principel detailsINSIDE LOAN##"+insertPenal);
//         * } } getIntDetails=sqlMap.executeQueryForList("getPenalIntDetails",
//         * loanInstall); hash=(HashMap)getIntDetails.get(0);
//         * insertPenal.put("PENAL_INT",hash.get("PIBAL")); } }
//         *
//         * if(prodType !=null && prodType.equals("AD")) // Only AD { if
//         * (asAndWhenMap != null && asAndWhenMap.size() > 0) {
//         * if(asAndWhenMap.containsKey("AS_CUSTOMER_COMES") &&
//         * asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")){ List
//         * facilitylst=sqlMap.executeQueryForList("LoneFacilityDetailAD",loanInstall);
//         * double
//         * interest=CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
//         * double penal
//         * =CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
//         * if(facilitylst!=null && facilitylst.size()>0){ HashMap
//         * hash=(HashMap)facilitylst.get(0);
//         * hash.put("ACT_NUM",loanInstall.get("ACT_NUM"));
//         * hash.put("FROM_DT",hash.get("LAST_INT_CALC_DT"));
//         * hash.put("FROM_DT",DateUtil.addDays(((Date)hash.get("FROM_DT")),2));
//         * hash.put("TO_DATE",DateUtil.addDaysProperFormat(interestUptoDt,-1));
//         * facilitylst=sqlMap.executeQueryForList("getPaidPrincipleAD",hash);
//         * if(facilitylst!=null && facilitylst.size()>0){
//         * hash=(HashMap)facilitylst.get(0);
//         * interest-=CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
//         * penal-=CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
//         * } } if (interest > 0) { insertPenal.put("CURR_MONTH_INT",new
//         * Double(interest)); } else { insertPenal.put("CURR_MONTH_INT",new
//         * Double(0)); } if (penal > 0) { insertPenal.put("PENAL_INT",new
//         * Double(penal)); } else { insertPenal.put("PENAL_INT",new Double(0));
//         * } insertPenal.put("INTEREST",asAndWhenMap.get("INTEREST"));
//         * insertPenal.put("LOAN_CLOSING_PENAL_INT",asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
//         * chargeList=sqlMap.executeQueryForList("getChargeDetails",loanInstall);
//         * }else{ if(prodType !=null && prodType.equals("AD")) { List
//         * getIntDetails=sqlMap.executeQueryForList("getIntDetailsAD",
//         * loanInstall); HashMap hash=null;
//         *
//         * for(int i=0;i<getIntDetails.size();i++){
//         * hash=(HashMap)getIntDetails.get(i); String
//         * trn_mode=CommonUtil.convertObjToStr(hash.get("TRN_CODE")); double
//         * iBal=CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
//         * double pibal=
//         * CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
//         * double excess=
//         * CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT")).doubleValue();
//         * if(trn_mode.equals("C*")){
//         * insertPenal.put("CURR_MONTH_INT",String.valueOf(iBal + pibal));
//         * insertPenal.put("PRINCIPLE_BAL",new
//         * Double(CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue()-excess));
//         * insertPenal.put("EBAL",hash.get("EBAL")); break; }else{
//         * insertPenal.put("CURR_MONTH_INT",String.valueOf(iBal + pibal));
//         * insertPenal.put("PRINCIPLE_BAL",new
//         * Double(CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue()-excess));
//         * insertPenal.put("EBAL",hash.get("EBAL")); } System.out.println("int
//         * principel detailsINSIDE OD"+insertPenal); }
//         * getIntDetails=sqlMap.executeQueryForList("getPenalIntDetailsAD",
//         * loanInstall); if(getIntDetails.size()>0){
//         * hash=(HashMap)getIntDetails.get(0);
//         * insertPenal.put("PENAL_INT",hash.get("PIBAL")); }
//         * insertPenal.remove("PRINCIPLE_BAL");
//         *
//         * }
//         * }
//         * }
//         * }
//         * //Added By Suresh (Current Dt > To Date AND PBAL >0 in
//         * ADV_TRANS_DETAILS, Add Principle_Balance) if(prodType !=null &&
//         * prodType.equals("AD")){ double pBalance=0.0; Date expDt = null; List
//         * expDtList=sqlMap.executeQueryForList("getLoanExpDate", loanInstall);
//         * if(expDtList!=null && expDtList.size()>0){ whereMap = new HashMap();
//         * whereMap=(HashMap) expDtList.get(0); pBalance =
//         * CommonUtil.convertObjToDouble(whereMap.get("PBAL")).doubleValue();
//         * expDt = (Date)whereMap.get("TO_DT"); long diffDayPending =
//         * DateUtil.dateDiff(expDt,intUptoDt); System.out.println("#############
//         * Insert PBalance"+pBalance+"######diffDayPending :"+diffDayPending);
//         * if(diffDayPending>0 && pBalance>0){
//         * insertPenal.put("PRINCIPLE_BAL",new Double(pBalance)); } } }
//         * System.out.println("####### insertPenal : "+insertPenal); //Charges
//         * double chargeAmt =0.0; whereMap =new HashMap();
//         * whereMap.put("ACT_NUM",actNum); chargeAmt = getChargeAmount(whereMap,
//         * prodType); if(chargeAmt>0){ dataMap.put("CHARGES",
//         * String.valueOf(chargeAmt)); }else{ dataMap.put("CHARGES", "0"); }
//         * System.out.println("####### Single Row insertPenal : "+insertPenal);
//         * double totalDemand = 0.0; double principalAmount = 0.0;
//         * if(insertPenal.containsKey("CURR_MONTH_PRINCEPLE")){ principalAmount
//         * =
//         * CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_PRINCEPLE")).doubleValue();
//         * // Principal Amount totalDemand =
//         * CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue()
//         * +
//         * CommonUtil.convertObjToDouble(insertPenal.get("PENAL_INT")).doubleValue()
//         * + chargeAmt; }else{ principalAmount =
//         * CommonUtil.convertObjToDouble(insertPenal.get("PRINCIPLE_BAL")).doubleValue();
//         * // Principal Amount totalDemand =
//         * CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue()
//         * +
//         * CommonUtil.convertObjToDouble(insertPenal.get("PENAL_INT")).doubleValue()
//         * + chargeAmt; }
//         *
//         *
//         * if (inst_dt!=null && prodType.equals("TL")){
//         * if(DateUtil.dateDiff(intUptoDt, inst_dt)<=0 ){ if
//         * (installtype.equals("UNIFORM_PRINCIPLE_EMI") &&
//         * emi_uniform.equals("Y")) { principalAmount = principalAmount -
//         * CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT"));
//         * dataMap.put("PRINCIPAL", String.valueOf(principalAmount)); }else{
//         * dataMap.put("PRINCIPAL", String.valueOf(principalAmount)); } } else {
//         * if (installtype.equals("UNIFORM_PRINCIPLE_EMI") &&
//         * emi_uniform.equals("Y")) { principalAmount = principalAmount -
//         * CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT"));
//         * dataMap.put("PRINCIPAL", String.valueOf(principalAmount)); }else{
//         * dataMap.put("PRINCIPAL", "0"); principalAmount =0.0; } } // IF
//         * SALARY_RECOVERY = 'YES' ONLY if((principalAmount==0
//         * &&(!(installtype.equals("UNIFORM_PRINCIPLE_EMI") &&
//         * emi_uniform.equals("Y"))))
//         * ||(installtype.equals("UNIFORM_PRINCIPLE_EMI") &&
//         * emi_uniform.equals("Y"))){ System.out.println("############ instalAmt
//         * : "+instalAmt); HashMap balanceMap = new HashMap(); double
//         * balanceLoanAmt =0.0; double finalDemandAmt =0.0;
//         * System.out.println("############ actNum : "+actNum);
//         * balanceMap.put("ACCOUNTNO",actNum); List balannceAmtLst =
//         * sqlMap.executeQueryForList("getBalanceLoanPrincipalAmt", balanceMap);
//         * if(balannceAmtLst!=null && balannceAmtLst.size()>0){
//         * balanceMap=(HashMap) balannceAmtLst.get(0); balanceLoanAmt =
//         * CommonUtil.convertObjToDouble(balanceMap.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();
//         * System.out.println("############ instalAmt : "+instalAmt);
//         * System.out.println("############ LoanBalancePrincAmt :
//         * "+balanceLoanAmt); double checkAmt =0.0; double totalPrincAmount=0.0;
//         * checkAmt = balanceLoanAmt - instalAmt; if(checkAmt>0){
//         * if(!(installtype.equals("UNIFORM_PRINCIPLE_EMI") &&
//         * emi_uniform.equals("Y"))){ balanceMap.put("ACCT_NUM",actNum);
//         * balanceMap.put("BALANCE_AMT",String.valueOf(checkAmt)); List
//         * sumInstLst =
//         * sqlMap.executeQueryForList("getPrincipalAmtGreaterThanBalAmt",
//         * balanceMap); if(sumInstLst!=null && sumInstLst.size()>0){ balanceMap
//         * =(HashMap) sumInstLst.get(0); totalPrincAmount =
//         * CommonUtil.convertObjToDouble(balanceMap.get("PRINCIPAL_AMOUNT")).doubleValue();
//         * totalPrincAmount+=instalAmt; finalDemandAmt = totalPrincAmount -
//         * paidAmount; if(balanceLoanAmt>finalDemandAmt){
//         * dataMap.put("PRINCIPAL", String.valueOf(finalDemandAmt));
//         * principalAmount = finalDemandAmt; }else{ dataMap.put("PRINCIPAL",
//         * String.valueOf(balanceLoanAmt)); principalAmount = balanceLoanAmt; }
//         * } System.out.println("############ finalDemandAmt :
//         * "+finalDemandAmt); } }else{ HashMap transMap=new HashMap();
//         * transMap.put("ACT_NUM",actNum);
//         * transMap.put("BRANCH_CODE",_branchCode); List sanctionLst =
//         * sqlMap.executeQueryForList("getNoOfDaysinLoan", transMap);
//         * if(sanctionLst !=null && sanctionLst.size()>0){ HashMap
//         * recordMap=(HashMap)sanctionLst.get(0); int repayFreq = 0; repayFreq =
//         * CommonUtil.convertObjToInt(recordMap.get("REPAYMENT_FREQUENCY"));
//         * if(repayFreq==1){ Date expiry_dt=null; expiry_dt =
//         * (Date)recordMap.get("TO_DT"); expiry_dt = (Date)expiry_dt.clone();
//         * System.out.println("########## expiry_dt : "+expiry_dt);
//         * if(DateUtil.dateDiff(intUptoDt, expiry_dt)>=0){ principalAmount =0.0;
//         * dataMap.put("PRINCIPAL", String.valueOf(principalAmount)); }else{
//         * dataMap.put("PRINCIPAL", String.valueOf(balanceLoanAmt));
//         * principalAmount = balanceLoanAmt; } }else{ dataMap.put("PRINCIPAL",
//         * String.valueOf(balanceLoanAmt)); principalAmount = balanceLoanAmt; }
//         * } } } } } if (prodType.equals("AD")){ if(principalAmount>0){
//         * dataMap.put("PRINCIPAL", String.valueOf(principalAmount)); }else{ if
//         * (installtype.equals("UNIFORM_PRINCIPLE_EMI") &&
//         * emi_uniform.equals("Y")) { dataMap.put("PRINCIPAL",
//         * String.valueOf(principalAmount)); } else { dataMap.put("PRINCIPAL",
//         * "0"); principalAmount = 0.0; } } }
//         *
//         * if(installtype.equals("UNIFORM_PRINCIPLE_EMI") &&
//         * emi_uniform.equals("Y")){ double dueamount=0; double penal=0; double
//         * totEmi=0; double paidEmi=0; double principle=0; double
//         * interst=Double.parseDouble(insertPenal.get("CURR_MONTH_INT").toString());
//         *
//         * System.out.println("interst"+interst); HashMap emi=new HashMap();
//         * Date upto=(Date)currDt.clone(); emi.put("ACC_NUM",actNum);
//         * emi.put("UP_TO",upto);
//         *
//         * List totalEmiList=sqlMap.executeQueryForList("TotalEmi",emi); if
//         * (totalEmiList != null && totalEmiList.size() > 0 && ((HashMap)
//         * totalEmiList.get(0)).get("TOTAL_AMOUNT") != null) { HashMap aMap=new
//         * HashMap(); aMap=(HashMap)totalEmiList.get(0);
//         * totEmi=Double.parseDouble(aMap.get("TOTAL_AMOUNT").toString());
//         * System.out.println("TOTAL_AMOUNTv"+totEmi); } else { totEmi=0;
//         *
//         * }
//         * HashMap paid = new HashMap(); paid.put("ACT_NUM",actNum);
//         * paid.put("BRANCH_CODE", _branchCode); List
//         * paidAmtemi=sqlMap.executeQueryForList("getPaidPrincipleEMI",
//         * loanInstall); if (paidAmtemi != null && paidAmtemi.size() > 0 &&
//         * ((HashMap) paidAmtemi.get(0)).get("PRINCIPLE") != null) { paid =
//         * (HashMap) paidAmtemi.get(0);
//         * System.out.println("!!!!asAndWhenMap:"+paid); //
//         * System.out.println("allInstallmentMap..."+allInstallmentMap);
//         * paidEmi=CommonUtil.convertObjToDouble(paid.get("PRINCIPLE")).doubleValue();
//         * System.out.println("paidEmi"+paidEmi); } else { paidEmi=0; }
//         * System.out.println("totEmi"+totEmi+"paidEmi"+paidEmi);
//         * dueamount=totEmi-paidEmi; double paidamount=paidEmi; if (dueamount <=
//         * 0) { dueamount =
//         * CommonUtil.convertObjToDouble(dataMap.get("PRINCIPAL")) + interst; }
//         * System.out.println("totalDemandsdas"+dueamount);
//         *
//         *
//         * System.out.println("+==========PENAL
//         * STARTS==============================+"); List
//         * scheduleList=sqlMap.executeQueryForList("getSchedules", emi); // List
//         * penalInterstRate=
//         *
//         *
//         * Date penalStrats=new Date(); if (scheduleList != null &&
//         * scheduleList.size() > 0) { for (int k = 0; k < scheduleList.size();
//         * k++) { HashMap eachInstall=new HashMap();
//         *
//         * eachInstall=(HashMap)scheduleList.get(k);
//         * System.out.println("eachInstall"+eachInstall); double scheduledEmi=
//         * Double.parseDouble(eachInstall.get("TOTAL_AMT").toString()); if
//         * (paidamount >= scheduledEmi) {
//         * System.out.println("111paidamount"+paidamount+"scheduledEmi"+scheduledEmi);
//         * paidamount=paidamount-scheduledEmi;
//         * System.out.println("paidamount"+paidamount); } else { String
//         * in_date=CommonUtil.convertObjToStr(eachInstall.get("INSTALLMENT_DT"));
//         * penalStrats=DateUtil.getDateMMDDYYYY(in_date); //
//         * penalStrats=DateUtil.getDateMMDDYYYY(eachInstall.get("INSTALLMENT_DT").toString());
//         * System.out.println("penalStrats....."+penalStrats); break; }
//         *
//         *
//         * }
//         * emi.put("FROM_DATE",penalStrats); List
//         * getPenalData=sqlMap.executeQueryForList("getPenalData", emi); List
//         * penalInterstRate=sqlMap.executeQueryForList("getPenalIntestRatefromMaintenance",
//         * emi); double interstPenal=0; double garce=0;
//         *
//         * List graceDays=sqlMap.executeQueryForList("getGracePeriodDays", emi);
//         * if (graceDays != null && graceDays.size() > 0) { HashMap map=new
//         * HashMap(); map=(HashMap)graceDays.get(0); if (map != null &&
//         * map.containsKey("GRACE_PERIOD_DAYS") && map.get("GRACE_PERIOD_DAYS")
//         * != null) { garce=
//         * Double.parseDouble(map.get("GRACE_PERIOD_DAYS").toString()); } else {
//         * garce=0; } } else { garce=0; }
//         *
//         *
//         * long gracedy=(long) garce; int graceint=(int) garce; if
//         * (penalInterstRate != null && penalInterstRate.size() > 0) { HashMap
//         * test=new HashMap(); test=(HashMap)penalInterstRate.get(0); if (test
//         * != null && test.containsKey("PENAL_INTEREST") &&
//         * test.get("PENAL_INTEREST") != null) { interstPenal=
//         * Double.parseDouble(test.get("PENAL_INTEREST").toString()); } else {
//         * List limitList=sqlMap.executeQueryForList("getLimitFromLoanSanc",
//         * emi); double
//         * limit=Double.parseDouble(((HashMap)limitList.get(0)).get("LIMIT").toString());
//         * emi.put("LIMIT",limit); List
//         * penalFromROI=sqlMap.executeQueryForList("getPenalIntestRatefromROI",
//         * emi); if (penalFromROI != null && penalFromROI.size() > 0) { test=new
//         * HashMap(); test=(HashMap)penalFromROI.get(0); interstPenal=
//         * Double.parseDouble(test.get("PENAL_INT").toString()); } } } else {
//         * List limitList=sqlMap.executeQueryForList("getLimitFromLoanSanc",
//         * emi); double
//         * limit=Double.parseDouble(((HashMap)limitList.get(0)).get("LIMIT").toString());
//         * emi.put("LIMIT",limit); List
//         * penalFromROI=sqlMap.executeQueryForList("getPenalIntestRatefromROI",
//         * emi); if (penalFromROI != null && penalFromROI.size() > 0) { HashMap
//         * test=new HashMap(); test=(HashMap)penalFromROI.get(0); interstPenal=
//         * Double.parseDouble(test.get("PENAL_INT").toString()); } }
//         * System.out.println("interstPenal...."+interstPenal); if (getPenalData
//         * != null && getPenalData.size() > 0) { for (int k = 0; k <
//         * getPenalData.size(); k++) { HashMap amap=new HashMap();
//         * amap=(HashMap)getPenalData.get(k);
//         *
//         *
//         *
//         * String
//         * in_date=CommonUtil.convertObjToStr(amap.get("INSTALLMENT_DT")); Date
//         * currntDate=DateUtil.getDateMMDDYYYY(in_date); currntDate=
//         * DateUtil.addDays(currntDate, graceint);
//         * System.out.println("5555currntDate.."+currntDate); //
//         * InterestCalculationTask incalc=new InterestCalculationTask(); HashMap
//         * holidayMap=new HashMap(); holidayMap.put("CURR_DATE", currntDate);
//         * holidayMap.put("BRANCH_CODE", _branchCode); //
//         * currntDate=incalc.holiydaychecking(holidayMap);
//         *
//         *
//         *
//         *
//         * currntDate = setProperDtFormat(currntDate); //
//         * System.out.println("instDate "+currntDate); //
//         * holidayMap.put("NEXT_DATE",currntDate); //
//         * holidayMap.put("BRANCH_CODE",_branchCode); holidayMap = new
//         * HashMap(); boolean checkHoliday=true; String str="any next working
//         * day"; System.out.println("instDate "+currntDate); currntDate =
//         * setProperDtFormat(currntDate);
//         * holidayMap.put("NEXT_DATE",currntDate);
//         * holidayMap.put("BRANCH_CODE",_branchCode); while(checkHoliday){
//         * boolean tholiday = false; List
//         * Holiday=sqlMap.executeQueryForList("checkHolidayDateOD",holidayMap);
//         * List
//         * weeklyOf=sqlMap.executeQueryForList("checkWeeklyOffOD",holidayMap);
//         * boolean isHoliday = Holiday.size()>0 ? true : false; boolean
//         * isWeekOff = weeklyOf.size()>0 ? true : false; if (isHoliday ||
//         * isWeekOff) { // System.out.println("#### diffDayPending Holiday True
//         * : "+diffDayPending); if(str.equals("any next working day")){ //
//         * diffDayPending-=1; currntDate.setDate(currntDate.getDate()+1); }else{
//         * //diffDayPending+=1; currntDate.setDate(currntDate.getDate()-1); }
//         * holidayMap.put("NEXT_DATE",currntDate); checkHoliday=true;
//         * System.out.println("#### holidayMap : "+holidayMap); }else{
//         * //System.out.println("#### diffDay Holiday False : "+diffDayPending);
//         * checkHoliday=false; } }
//         *
//         * System.out.println("currntDatemmm"+currntDate); // Date
//         * currntDate=DateUtil.getDateMMDDYYYY(amap.get("INSTALLMENT_DT").toString());
//         * System.out.println("DateUtil.dateDiff(currntDate,upto)"+DateUtil.dateDiff(currntDate,upto));
//         *
//         * long difference=DateUtil.dateDiff(currntDate,upto)-1; if (difference
//         * < 0) { difference=0; }
//         * System.out.println("difference..."+difference); double
//         * installment=Double.parseDouble(amap.get("TOTAL_AMT").toString());
//         * System.out.println("installmentsadeasdasd"+installment);
//         * penal=penal+((installment*difference*interstPenal)/36500);
//         * System.out.println("penallcalcuuu"+penal); }
//         *
//         * }
//         *
//         *
//         * }
//         * principle=dueamount-interst;
//         * System.out.println("mmmprinciple"+principle+"::penal"+penal+"::interst"+interst);
//         * totalDemand=principle+penal+interst;
//         * totalDemand=Math.round(totalDemand); principle=Math.round(principle);
//         * penal=Math.round(penal); interst=Math.round(interst);
//         * System.out.println("tttttoooo"+totalDemand); dataMap.put("INTEREST",
//         * Math.round(Double.parseDouble(insertPenal.get("CURR_MONTH_INT").toString())));
//         * dataMap.put("PENAL", penal); dataMap.put("TOTAL_DEMAND", new
//         * Double(totalDemand)); // dataMap.put("CLEAR_BALANCE", clear_balance);
//         * System.out.println("mmmmmmiiinnneee"+dataMap); } else {
//         * totalDemand+=principalAmount; System.out.println("totalDemand
//         * 333===="+totalDemand); dataMap.put("INTEREST",
//         * insertPenal.get("CURR_MONTH_INT")); dataMap.put("PENAL",
//         * insertPenal.get("PENAL_INT")); dataMap.put("TOTAL_DEMAND", new
//         * Double(totalDemand)); } if(totalDemand<=0){ dataMap = null; } }else{
//         * System.out.println("### Not Allow : "+checkDate); dataMap = null; } }
//         *
//         *
//         * System.out.println("####### Single Row DataMap : "+dataMap); return
//         * dataMap;
//         */
//    }
    
    
    public HashMap calcLoanPayments(String actNum, String prodId, String prodType, String recoveryYesNo) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put("ACT_NUM", actNum);
        whereMap.put("CURR_DATE",currDt.clone());
        System.out.println("processMap : "+whereMap);
        double intVal = 0, princVal =0;
        String remittedStatus = "";
         List resltList = null;
        if (prodType != null && prodType.equals("TL")) {
            System.out.println("whereMap :" + whereMap);
            HashMap hmap = new HashMap();
            hmap.put("ACT_NUM", CommonUtil.convertObjToStr(actNum));
            hmap.put("TRANS_DT", getProperDateFormat(intUptoDt.clone()));
            hmap.put("REC_TYPE", "Direct");
            System.out.println("intUptoDt ----------------------- :" + intUptoDt);
            resltList = sqlMap.executeQueryForList("getPenalIntDetailsTL", hmap);
            if (resltList != null && resltList.size() > 0) {
                HashMap ansMap = (HashMap) resltList.get(0);
                if (ansMap != null && ansMap.containsKey("RESULT")) {
                    String ans = CommonUtil.convertObjToStr(ansMap.get("RESULT"));
                    if (ans != null && ans.length() > 0) {
                        String[] ansArr = ans.split(":");
                        String intStr = "0", princStr = "0";
                        if (ansArr.length > 5) {
                            System.out.println("ansArr[4] :" + ansArr[5]);
                            if (ansArr[5].contains("=")) {
                                String[] splArr = ansArr[5].split("=");
                                if (splArr.length > 1) {
                                    intStr = splArr[1].trim();
                                }
                            }
                        }
                        if (ansArr.length > 4) {
                            System.out.println("ansArr[4] :" + ansArr[4]);
                            if (ansArr[4].contains("=")) {
                                String[] splArr = ansArr[4].split("=");
                                if (splArr.length > 1) {
                                    princStr = splArr[1].trim();
                                }
                            }
                        }
                         if (ansArr.length > 7) {
                          //  System.out.println("ansArr[6] :" + ansArr[7]);
                            if (ansArr[7].contains("=")) {
                                String[] splArr = ansArr[7].split("=");
                                if (splArr.length > 1) {
                                    remittedStatus = splArr[1].trim();
                                }
                            }
                        }
                        intVal = CommonUtil.convertObjToDouble(intStr);
                        princVal = CommonUtil.convertObjToDouble(princStr);
                    }
                }
            }
        }
        boolean isChkFlag = false;
        if (prodType != null && prodType.equals("TL")) {
            if (princVal > 0) {
                isChkFlag = true;
            } else {
                if (princVal == 0) {
                    List pList = sqlMap.executeQueryForList("getNotProcessedCurrentMonthRecords", whereMap);
                    if (pList.isEmpty()) {
                        isChkFlag = true;
                    } else {
                        isChkFlag = false;
                    }
                }
            }
        } else {
            List notProceesedList = sqlMap.executeQueryForList("getSelectNotProcessRecordcurrMonth", whereMap);
            if (notProceesedList.isEmpty()) {
                isChkFlag = true;
            }
            if (prodType != null && prodType.equals("AD")) {
                List pList = sqlMap.executeQueryForList("getNotProcessedCurrentMonthRecords", whereMap);
                if (pList.isEmpty()) {
                    isChkFlag = true;
                } else {
                    isChkFlag = false;
                }
            }
        }
        if (isChkFlag) {
        List parameterList = sqlMap.executeQueryForList("getRecoveryParameter", whereMap);
        String clear_balance = "";
        boolean flag = false;
        boolean alreadyPaidThisMonth = false;
        if (parameterList != null && parameterList.size() > 0) {
            int firstDay = 0;
            java.util.Date repayDate = null;
            java.util.Date inst_dt = null;
            java.util.Date checkDate = (java.util.Date) currDt.clone();
            java.util.Date sanctionDt = null;
            String EMIINSIMPLEINTREST = "";
            String MORATORIUMGIVEN = "";
            String INSTALLTYPE = "0";
            whereMap = (HashMap) parameterList.get(0);
            firstDay = CommonUtil.convertObjToInt(whereMap.get("FIRST_DAY"));
            repayDate = (java.util.Date) whereMap.get("REPAYMENT_DT");
            sanctionDt = (java.util.Date) whereMap.get("FROM_DT");
            EMIINSIMPLEINTREST = CommonUtil.convertObjToStr(whereMap.get("EMI_IN_SIMPLEINTREST"));
            MORATORIUMGIVEN = CommonUtil.convertObjToStr(whereMap.get("MORATORIUM_GIVEN"));
            INSTALLTYPE = CommonUtil.convertObjToStr(whereMap.get("INSTALL_TYPE"));
            long diffDay = DateUtil.dateDiff(sanctionDt, checkDate);
            long diffRepayDay = DateUtil.dateDiff(repayDate, checkDate);
            System.out.println("kusdkjvbsdv" + diffRepayDay);
            System.out.println("### diffDay : "+diffDay);
            if (INSTALLTYPE.equals("UNIFORM_PRINCIPLE_EMI") && diffRepayDay < 0 && MORATORIUMGIVEN.equals("N")) {
                System.out.println("inside something");
                dataMap = null;
            } else {
                if ((double) diffDay >= 0.0D) {
                    System.out.println("### Allow : "+checkDate);
                    if (recoveryYesNo.equals("N")) {
                        HashMap asAndWhenMap = interestCalculationTLAD(actNum, prodId, prodType);
                        System.out.println((new StringBuilder()).append("@#@ asAndWhenMap is >>>>").append(asAndWhenMap).toString());
                        System.out.println((new StringBuilder()).append("transDetail").append(actNum).append(_branchCode).toString());
                        HashMap insertPenal = new HashMap();
                        List chargeList = null;
                        HashMap loanInstall = new HashMap();
                        loanInstall.put("ACT_NUM", actNum);
                        loanInstall.put("BRANCH_CODE", _branchCode);
                        String moratorium = "";
                        List MoraList = ServerUtil.executeQuery("getMorotorium", loanInstall);
                        if (MoraList.size() > 0 && MoraList.get(0) != null) {
                            HashMap mapop = (HashMap) MoraList.get(0);
                            if (mapop.get("MORATORIUM_GIVEN") != null) {
                                moratorium = CommonUtil.convertObjToStr(mapop.get("MORATORIUM_GIVEN"));
                            }
                            System.out.println("soutMort" + moratorium);
                        }
                        double instalAmt = 0.0D;
                        double paidAmount = 0.0D;
                        HashMap emiMap = new HashMap();
                        String installtype = "";
                        String emi_uniform = "";
                        if (prodType != null && prodType.equals("TL")) {
                            double totPrinciple = 0.0D;
                            List emiList = sqlMap.executeQueryForList("getEmiTypeDetail", loanInstall);
                            if (emiList.size() > 0) {
                                emiMap = (HashMap) emiList.get(0);
                                installtype = emiMap.get("INSTALL_TYPE").toString();
                                emi_uniform = emiMap.get("EMI_IN_SIMPLEINTREST").toString();
                            }
                            HashMap allInstallmentMap = null;
                            if (!installtype.equals("UNIFORM_PRINCIPLE_EMI") || !emi_uniform.equals("Y")) {
                                List paidAmt = sqlMap.executeQueryForList("getPaidPrinciple", loanInstall);
                                allInstallmentMap = (HashMap) paidAmt.get(0);
                                System.out.println("allInstallmentMap..."+allInstallmentMap);
                                System.out.println("!!!!asAndWhenMap:"+asAndWhenMap);
                                totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
                                paidAmount = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
                                System.out.println("totPrinciple11:"+totPrinciple+" ::"+paidAmount);
                                if (asAndWhenMap == null || asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && asAndWhenMap.get("AS_CUSTOMER_COMES") != null && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("N")) {
                                    paidAmt = sqlMap.executeQueryForList("getIntDetails", loanInstall);
                                    if (paidAmt != null && paidAmt.size() > 0) {
                                        allInstallmentMap = (HashMap) paidAmt.get(0);
                                    }
                                    double totExcessAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
                                    totPrinciple += totExcessAmt;
                                    System.out.println("in as cust comexx"+totPrinciple);
                                }
                                List lst = sqlMap.executeQueryForList("getAllLoanInstallment", loanInstall);
                                int i = 0;
                                do {
                                    if (i >= lst.size()) {
                                        break;
                                    }
                                    allInstallmentMap = (HashMap) lst.get(i);
                                    System.out.println("allInstallmentMap...,mmm>>"+allInstallmentMap);
                                    instalAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                                    System.out.println("instalAmtt..."+instalAmt+"toooottt"+totPrinciple);
                                    if (instalAmt <= totPrinciple) {
                                        totPrinciple -= instalAmt;
                                        System.out.println("totPrinciple-=instalAmt=="+totPrinciple);
                                        inst_dt = new java.util.Date();
                                        String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                                        inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                                    } else {
                                        inst_dt = new java.util.Date();
                                        String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                                        inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                                        List aList = ServerUtil.executeQuery("getMorotorium", loanInstall);
                                        String moret = "";
                                        double clearBal = 0.0;
                                        if (aList.size() > 0 && aList.get(0) != null) {
                                            HashMap mapop = (HashMap) aList.get(0);
                                            if (mapop.get("MORATORIUM_GIVEN") != null) {
                                                moret = mapop.get("MORATORIUM_GIVEN").toString();
                                                clearBal = CommonUtil.convertObjToDouble(mapop.get("CLEAR_BALANCE"))*-1;
                                            }
                                        }
                                        System.out.println("inst_dt=22===="+inst_dt+"currDt=22======"+currDt);
                                        System.out.println("totPrrr22rrrrrrrrrrrrrr="+DateUtil.dateDiff(inst_dt, currDt)+"ggggg="+moret);
                                        if (DateUtil.dateDiff(inst_dt, currDt) <= 0L && moret != null && moret.equals("Y")) {
//                                            totPrinciple = 0.0D;
//                                            flag = true;
                                            if (DateUtil.dateDiff(repayDate, currDt) < 0) {
                                                totPrinciple = 0.0D;
                                                flag = true;
                                                System.out.println("not here");
                                            } else {
                                                totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                                                if (totPrinciple > clearBal) {
                                                    totPrinciple = clearBal;
                                                }
                                            }
                                        } else {
                                            totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue() - totPrinciple;
                                            System.out.println("before Breakkkk"+totPrinciple);
                                        }
                                        break;
                                    }
                                    i++;
                                } while (true);
                            } else {
                                List paidAmtemi = sqlMap.executeQueryForList("getPaidPrincipleEMI1", loanInstall);
                                allInstallmentMap = (HashMap) paidAmtemi.get(0);
                                System.out.println("!!!!asAndWhenMap:"+asAndWhenMap);
                                System.out.println("allInstallmentMap..."+allInstallmentMap);
                                totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
                                paidAmount = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
                                System.out.println("totPrinciple22:"+totPrinciple+" ::"+paidAmount);
                                if (asAndWhenMap == null || asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && asAndWhenMap.get("AS_CUSTOMER_COMES") != null && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("N")) {
                                    paidAmtemi = sqlMap.executeQueryForList("getIntDetails", loanInstall);
                                    if (paidAmtemi != null && paidAmtemi.size() > 0) {
                                        allInstallmentMap = (HashMap) paidAmtemi.get(0);
                                    }
                                    System.out.println("allInstallmentMap444"+allInstallmentMap);
                                    double totExcessAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
                                    totPrinciple += totExcessAmt;
                                    System.out.println("totPrinciple"+totPrinciple);
                                }
                                List lst = sqlMap.executeQueryForList("getAllLoanInstallment", loanInstall);
                                int i = 0;
                                do {
                                    if (i >= lst.size()) {
                                        break;
                                    }
                                    allInstallmentMap = (HashMap) lst.get(i);
                                    System.out.println("allInstallmentMap34243"+allInstallmentMap);
                                    instalAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                                    System.out.println("111222instalAmt"+instalAmt+":::"+totPrinciple);
                                    if (instalAmt <= totPrinciple) {
                                        totPrinciple -= instalAmt;
                                        System.out.println("chhhhnnn"+totPrinciple);
                                        inst_dt = new java.util.Date();
                                        String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                                        inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                                    } else {
                                        inst_dt = new java.util.Date();
                                        String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                                        inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                                        totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("TOTAL_AMT")).doubleValue() - totPrinciple;
                                        System.out.println("bbbkkk"+totPrinciple);
                                        break;
                                    }
                                    System.out.println("totPrinciple@@@@@@@@@@@@"+totPrinciple);
                                    i++;
                                } while (true);
                            }
                            if (!installtype.equals("UNIFORM_PRINCIPLE_EMI") || !emi_uniform.equals("Y")) {
                                dataMap.put("INSTMT_AMT", Double.valueOf(instalAmt));
                            } else {
                                dataMap.put("INSTMT_AMT", allInstallmentMap.get("TOTAL_AMT"));
                            }
                            List aList = ServerUtil.executeQuery("getMorotorium", loanInstall);
                            String moret = "";
                            if (aList.size() > 0 && aList.get(0) != null) {
                                HashMap mapop = (HashMap) aList.get(0);
                                if (mapop.get("MORATORIUM_GIVEN") != null) {
                                    moret = mapop.get("MORATORIUM_GIVEN").toString();
                                }
                            }
                            System.out.println("inst_dt=33===="+inst_dt+"currDt===33===="+currDt);
                            System.out.println("totPrrrrr333rrrrrrrrrrrr="+DateUtil.dateDiff(inst_dt, currDt)+"ggggg="+moret);
                            System.out.println("totPrrrrrrrrrrrrrrrrr="+DateUtil.dateDiff(inst_dt, currDt)+"ggggg="+moret);
                            if (DateUtil.dateDiff(inst_dt, currDt) <= 0L && moret != null && moret.equals("Y")) {
                                dataMap.put("INSTMT_AMT", Integer.valueOf(0));
                                dataMap.put("PRINCIPAL", String.valueOf("0"));
                            }
                            java.util.Date addDt = (java.util.Date) currDt.clone();
                            java.util.Date instDt = DateUtil.addDays(inst_dt, 1);
                            addDt.setDate(instDt.getDate());
                            addDt.setMonth(instDt.getMonth());
                            addDt.setYear(instDt.getYear());
                            loanInstall.put("FROM_DATE", addDt);
                            loanInstall.put("TO_DATE", interestUptoDt);
                            System.out.println("!! getTotalamount#####"+loanInstall);
                            List lst1 = null;
                            if (inst_dt != null && totPrinciple > 0.0D) {
                                lst1 = sqlMap.executeQueryForList("getTotalAmountOverDue", loanInstall);
                                System.out.println("listsize####"+lst1);
                            }
                            double principle = 0.0D;
//                            if (lst1 != null && lst1.size() > 0) {
//                                HashMap map = (HashMap) lst1.get(0);
//                                principle = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue();
//                            }
//                            totPrinciple += principle;
                            System.out.println("totPrinciple 1111####"+totPrinciple);
                            if (!installtype.equals("UNIFORM_PRINCIPLE_EMI") || !emi_uniform.equals("Y")) {
                                if (lst1 != null && lst1.size() > 0) {
                                    HashMap map = (HashMap) lst1.get(0);
                                    principle = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue();
                                    System.out.println("principleprinciple"+principle);
                                }
                                totPrinciple += principle;
                            } else if (lst1 != null && lst1.size() > 0) {
                                HashMap map = (HashMap) lst1.get(0);
                                System.out.println("snnnnnn"+map);
                                principle = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue() + CommonUtil.convertObjToDouble(map.get("INTEREST_AMOUNT")).doubleValue();
                                System.out.println("sdasdsd"+principle);
                                if (principle == 0.0D) {
                                    List advList = sqlMap.executeQueryForList("getAdvAmt", loanInstall);
                                    if (advList.size() > 0 && advList != null) {
                                        map = (HashMap) advList.get(0);
                                        if (map.get("TOTAL_AMT") != null) {
                                            principle = CommonUtil.convertObjToDouble(map.get("TOTAL_AMT")).doubleValue();
                                        }
                                        totPrinciple = principle;
                                    }
                                } else {
                                    totPrinciple = principle;
                                }
                                System.out.println("totPrinciple66666"+totPrinciple);
                            } else {
                                System.out.println("innn eeelllsss333"+totPrinciple);
                                totPrinciple -= CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
                                System.out.println("innn eeelllsss333"+totPrinciple);
                            }
                            System.out.println("herererererererererreerererere"+totPrinciple);
                            insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(totPrinciple));
                            insertPenal.put("INSTALL_DT", inst_dt);
                            if (asAndWhenMap.containsKey("MORATORIUM_INT_FOR_EMI")) {
                                insertPenal.put("MORATORIUM_INT_FOR_EMI", asAndWhenMap.get("MORATORIUM_INT_FOR_EMI"));
                            }
                            if (asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && asAndWhenMap.get("AS_CUSTOMER_COMES") != null && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")) {
                                double interest = CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
                                double penal = CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                                List facilitylst = sqlMap.executeQueryForList("LoneFacilityDetailAD", loanInstall);
                                if (facilitylst != null && facilitylst.size() > 0) {
                                    HashMap hash = (HashMap) facilitylst.get(0);
                                    if (hash.get("CLEAR_BALANCE") != null) {
                                        clear_balance = hash.get("CLEAR_BALANCE").toString();
                                    }
                                    System.out.println("clear_balance ="+clear_balance+" aaa---"+CommonUtil.convertObjToStr(loanInstall.get("ACT_NUM")));
                                    hash.put("ACT_NUM", loanInstall.get("ACT_NUM"));
                                    if (asAndWhenMap.containsKey("PREMATURE")) {
                                        insertPenal.put("PREMATURE", asAndWhenMap.get("PREMATURE"));
                                    }
                                    if (asAndWhenMap.containsKey("PREMATURE") && asAndWhenMap.containsKey("PREMATURE_INT_CALC_AMT") && CommonUtil.convertObjToStr(asAndWhenMap.get("PREMATURE_INT_CALC_AMT")).equals("LOANSANCTIONAMT")) {
                                        hash.put("FROM_DT", hash.get("ACCT_OPEN_DT"));
                                    } else {
                                        hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                                        hash.put("FROM_DT", DateUtil.addDays((java.util.Date) hash.get("FROM_DT"), 2));
                                    }
                                    hash.put("TO_DATE", interestUptoDt.clone());
                                    if (asAndWhenMap == null || !asAndWhenMap.containsKey("INSTALL_TYPE") || asAndWhenMap.get("INSTALL_TYPE") == null || !asAndWhenMap.get("INSTALL_TYPE").equals("EMI")) {
                                        facilitylst = sqlMap.executeQueryForList("getPaidPrinciple", hash);
                                    } else {
                                        facilitylst = null;
                                        if (asAndWhenMap.containsKey("PRINCIPAL_DUE") && asAndWhenMap.get("PRINCIPAL_DUE") != null) {
                                            insertPenal.put("CURR_MONTH_PRINCEPLE", asAndWhenMap.get("PRINCIPAL_DUE"));
                                        }
                                    }
                                    if (facilitylst != null && facilitylst.size() > 0) {
                                        hash = (HashMap) facilitylst.get(0);
                                        interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                                        penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                                        insertPenal.put("PAID_INTEREST", hash.get("INTEREST"));
                                    }
                                }
                                System.out.println("####interest:"+interest);
                                if (interest > 0.0D) {
                                    insertPenal.put("CURR_MONTH_INT", new Double(interest));
                                } else {
                                    insertPenal.put("CURR_MONTH_INT", new Double(0.0D));
                                }
                                if (penal > 0.0D) {
                                    insertPenal.put("PENAL_INT", new Double(penal));
                                } else {
                                    insertPenal.put("PENAL_INT", new Double(0.0D));
                                }
                                insertPenal.put("INTEREST", asAndWhenMap.get("INTEREST"));
                                insertPenal.put("LOAN_CLOSING_PENAL_INT", asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
                                insertPenal.put("LAST_INT_CALC_DT", asAndWhenMap.get("LAST_INT_CALC_DT"));
                                insertPenal.put("ROI", asAndWhenMap.get("ROI"));
                                chargeList = sqlMap.executeQueryForList("getChargeDetails", loanInstall);
                            } else {
                                List getIntDetails = sqlMap.executeQueryForList("getIntDetails", loanInstall);
                                HashMap hash = null;
                                if (getIntDetails != null) {
                                    for (int i = 0; i < getIntDetails.size(); i++) {
                                        hash = (HashMap) getIntDetails.get(i);
                                        String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
                                        double pBal = CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue();
                                        double iBal = CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
                                        double pibal = CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
                                        double excess = CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT")).doubleValue();
                                        System.out.println("pBalcc99oogytf"+pBal+"::"+iBal+"::"+pibal+"::"+excess);
                                        pBal -= excess;
                                        System.out.println("pBalrrr"+pBal);
                                        if (pBal < totPrinciple) {
                                            insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(pBal));
                                        }
                                        System.out.println("insertPenal5555"+insertPenal);
                                        if (trn_mode.equals("C*")) {
                                            insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                                            insertPenal.put("PRINCIPLE_BAL", new Double(pBal));
                                            insertPenal.put("EBAL", hash.get("EBAL"));
                                            break;
                                        }
                                        if (!trn_mode.equals("DP")) {
                                            insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                                        }
                                        insertPenal.put("EBAL", hash.get("EBAL"));
                                        insertPenal.put("PRINCIPLE_BAL", new Double(pBal));
                                        System.out.println("int principel detailsINSIDE LOAN##"+insertPenal);
                                    }

                                }
                                getIntDetails = sqlMap.executeQueryForList("getPenalIntDetails", loanInstall);
                                hash = (HashMap) getIntDetails.get(0);
                                insertPenal.put("PENAL_INT", hash.get("PIBAL"));
                            }
                           if (prodType != null && prodType.equals("TL")&& CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT"))==0 &&
                                   intVal>0) {
                              insertPenal.put("CURR_MONTH_INT",intVal);
                           }
                            System.out.println("insertPenalnnnnnnshdcgasdg"+insertPenal);
                        }
                        if (prodType != null && prodType.equals("AD") && asAndWhenMap != null && asAndWhenMap.size() > 0) {
                            if (asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")) {
                                List facilitylst = sqlMap.executeQueryForList("LoneFacilityDetailAD", loanInstall);
                                double interest = CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
                                double penal = CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                                if (facilitylst != null && facilitylst.size() > 0) {
                                    HashMap hash = (HashMap) facilitylst.get(0);
                                    if (hash.get("CLEAR_BALANCE") != null) {
                                        clear_balance = hash.get("CLEAR_BALANCE").toString();
                                    }
                                    hash.put("ACT_NUM", loanInstall.get("ACT_NUM"));
                                    hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                                    hash.put("FROM_DT", DateUtil.addDays((java.util.Date) hash.get("FROM_DT"), 2));
                                    hash.put("TO_DATE", DateUtil.addDaysProperFormat(interestUptoDt, -1));
                                    facilitylst = sqlMap.executeQueryForList("getPaidPrincipleAD", hash);
                                    if (facilitylst != null && facilitylst.size() > 0) {
                                        hash = (HashMap) facilitylst.get(0);
                                        interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                                        penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                                    }
                                }
                                if (interest > 0.0D) {
                                    insertPenal.put("CURR_MONTH_INT", new Double(interest));
                                } else {
                                    insertPenal.put("CURR_MONTH_INT", new Double(0.0D));
                                }
                                if (penal > 0.0D) {
                                    insertPenal.put("PENAL_INT", new Double(penal));
                                } else {
                                    insertPenal.put("PENAL_INT", new Double(0.0D));
                                }
                                insertPenal.put("INTEREST", asAndWhenMap.get("INTEREST"));
                                insertPenal.put("LOAN_CLOSING_PENAL_INT", asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
                                chargeList = sqlMap.executeQueryForList("getChargeDetails", loanInstall);
                            } else if (prodType != null && prodType.equals("AD")) {
                                List getIntDetails = sqlMap.executeQueryForList("getIntDetailsAD", loanInstall);
                                HashMap hash = null;
                                int i = 0;
                                do {
                                    if (i >= getIntDetails.size()) {
                                        break;
                                    }
                                    hash = (HashMap) getIntDetails.get(i);
                                    String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
                                    double iBal = CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
                                    double pibal = CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
                                    double excess = CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT")).doubleValue();
                                    if (trn_mode.equals("C*")) {
                                        insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                                        insertPenal.put("PRINCIPLE_BAL", new Double(CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue() - excess));
                                        insertPenal.put("EBAL", hash.get("EBAL"));
                                        break;
                                    }
                                    insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                                    insertPenal.put("PRINCIPLE_BAL", new Double(CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue() - excess));
                                    insertPenal.put("EBAL", hash.get("EBAL"));
                                    System.out.println("int principel detailsINSIDE OD"+insertPenal);
                                    i++;
                                } while (true);
                                getIntDetails = sqlMap.executeQueryForList("getPenalIntDetailsAD", loanInstall);
                                if (getIntDetails.size() > 0) {
                                    hash = (HashMap) getIntDetails.get(0);
                                    insertPenal.put("PENAL_INT", hash.get("PIBAL"));
                                }
                                insertPenal.remove("PRINCIPLE_BAL");
                            }
                        }
                        if (prodType != null && prodType.equals("AD")) {
                            double pBalance = 0.0D;
                            java.util.Date expDt = null;
                            List expDtList = sqlMap.executeQueryForList("getLoanExpDate", loanInstall);
                            if (expDtList != null && expDtList.size() > 0) {
                                whereMap = new HashMap();
                                whereMap = (HashMap) expDtList.get(0);
                                pBalance = CommonUtil.convertObjToDouble(whereMap.get("PBAL")).doubleValue();
                                expDt = (java.util.Date) whereMap.get("TO_DT");
                                long diffDayPending = DateUtil.dateDiff(expDt, intUptoDt);
                                System.out.println("############# Insert PBalance"+pBalance+"######diffDayPending :"+diffDayPending);
                                if (diffDayPending > 0L && pBalance > 0.0D) {
                                    insertPenal.put("PRINCIPLE_BAL", new Double(pBalance));
                                }
                            }
                        }
                        System.out.println("####### insertPenal : "+insertPenal);
                        double chargeAmt = 0.0D;
                        whereMap = new HashMap();
                        whereMap.put("ACT_NUM", actNum);
                        chargeAmt = getChargeAmount(whereMap, prodType);
                        if (chargeAmt > 0.0D) {
                            dataMap.put("CHARGES", String.valueOf(chargeAmt));
                        } else {
                            dataMap.put("CHARGES", "0");
                        }
                        System.out.println("####### Single Row insertPenal : "+insertPenal);
                        double totalDemand = 0.0D;
                        double principalAmount = 0.0D;
                        if (insertPenal.containsKey("CURR_MONTH_PRINCEPLE")) {
                            principalAmount = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_PRINCEPLE")).doubleValue();
                            totalDemand = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue() + CommonUtil.convertObjToDouble(insertPenal.get("PENAL_INT")).doubleValue() + chargeAmt;
                            System.out.println("totalDemand 111===="+totalDemand);
                        } else {
                            principalAmount = CommonUtil.convertObjToDouble(insertPenal.get("PRINCIPLE_BAL")).doubleValue();
                            totalDemand = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue() + CommonUtil.convertObjToDouble(insertPenal.get("PENAL_INT")).doubleValue() + chargeAmt;
                            System.out.println("totalDemand 222===="+totalDemand);
                        }
                        if (inst_dt != null && prodType.equals("TL")) {
//                            if (retired != null && retired.equals("YES")) {
//                                System.out.println("11111111clear_balance" + clear_balance);
//                                dataMap.put("PRINCIPAL", String.valueOf(Math.abs(Integer.parseInt(clear_balance))));
//                            } else {
                                System.out.println("intUptoDt"+intUptoDt+"inst_dt"+inst_dt);
                                System.out.println("jeffin----"+DateUtil.dateDiff(intUptoDt, inst_dt));
                                if (DateUtil.dateDiff(intUptoDt, inst_dt) <= 0L) {
                                    if (installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y") && moratorium.equals("N")) {
                                        principalAmount -= CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue();
                                        System.out.println("222222222222222principalAmount" + principalAmount);
                                        dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
                                    } else {
                                        System.out.println("33333333333principalAmount" + principalAmount);
                                        dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
                                    }
                                } 
//                                else if (installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y") && moratorium.equals("N")) {
//                                    principalAmount -= CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue();
//                                    System.out.println("44444444principalAmount" + principalAmount);
//                                    dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
//                                }
                                else {
                                    System.out.println("5555555");
                                    dataMap.put("PRINCIPAL", "0");
                                    principalAmount = 0.0;
                                }
//                            }
                            if (principalAmount == 0.0 && (!installtype.equals("UNIFORM_PRINCIPLE_EMI") || !emi_uniform.equals("Y")) || installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y")) {
                                System.out.println("############ instalAmt : "+instalAmt);
                                HashMap balanceMap = new HashMap();
                                double balanceLoanAmt = 0.0D;
                                double finalDemandAmt = 0.0D;
                                System.out.println("############ actNum : "+actNum);
                                balanceMap.put("ACCOUNTNO", actNum);
                                List balannceAmtLst = sqlMap.executeQueryForList("getBalanceLoanPrincipalAmt", balanceMap);
                                if (balannceAmtLst != null && balannceAmtLst.size() > 0) {
                                    balanceMap = (HashMap) balannceAmtLst.get(0);
                                    balanceLoanAmt = CommonUtil.convertObjToDouble(balanceMap.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();
                                    System.out.println("############ instalAmt : "+instalAmt);
                                    System.out.println("############ LoanBalancePrincAmt : "+balanceLoanAmt);
                                    System.out.println("######## paidAmount"+paidAmount);
                                    double checkAmt = 0.0D;
                                    double totalPrincAmount = 0.0D;
                                    checkAmt = balanceLoanAmt - instalAmt;
                                    System.out.println("checkAmtcheckAmtcheckAmt"+checkAmt);
                                    if (checkAmt > 0.0D) {
                                        if (!installtype.equals("UNIFORM_PRINCIPLE_EMI") || !emi_uniform.equals("Y")) {
                                            balanceMap.put("ACCT_NUM", actNum);
                                            //balanceMap.put("BALANCE_AMT", String.valueOf(checkAmt));
                                            balanceMap.put("BALANCE_AMT", checkAmt);
                                            List sumInstLst = sqlMap.executeQueryForList("getPrincipalAmtGreaterThanBalAmt", balanceMap);
                                            if (sumInstLst != null && sumInstLst.size() > 0) {
                                                balanceMap = (HashMap) sumInstLst.get(0);
                                                totalPrincAmount = CommonUtil.convertObjToDouble(balanceMap.get("PRINCIPAL_AMOUNT")).doubleValue();
                                                System.out.println("totalPrincAmounttotalPrincAmount"+totalPrincAmount);
                                                totalPrincAmount += instalAmt;
                                                System.out.println("here totalPrincAmount"+totalPrincAmount);
                                                finalDemandAmt = totalPrincAmount - paidAmount;
                                                if (balanceLoanAmt > finalDemandAmt) {
                                                    System.out.println("66666666finalDemandAmt" + finalDemandAmt);
                                                    dataMap.put("PRINCIPAL", String.valueOf(finalDemandAmt));
                                                    principalAmount = finalDemandAmt;
                                                } else {
                                                    System.out.println("7777777777balanceLoanAmt" + balanceLoanAmt);
                                                    dataMap.put("PRINCIPAL", String.valueOf(balanceLoanAmt));
                                                    principalAmount = balanceLoanAmt;
                                                }
                                            }
                                            List aList = ServerUtil.executeQuery("getMorotorium", loanInstall);
                                            String moret = "";
                                            if (aList.size() > 0 && aList.get(0) != null) {
                                                HashMap mapop = (HashMap) aList.get(0);
                                                if (mapop.get("MORATORIUM_GIVEN") != null) {
                                                    moret = mapop.get("MORATORIUM_GIVEN").toString();
                                                }
                                            }
                                            System.out.println("inst_dt==11==="+inst_dt+"currDt=11======"+currDt);
                                            System.out.println("totPrrrrrrr11rrrrrrrrrr="+DateUtil.dateDiff(inst_dt, currDt)+"ggggg="+moret);
                                            if (DateUtil.dateDiff(inst_dt, currDt) <= 0L && moret != null && moret.equals("Y")) {
                                                finalDemandAmt = 0.0D;
                                                principalAmount = 0.0D;
                                            }
                                            System.out.println("############ finalDemandAmt : "+finalDemandAmt);
                                        }
                                    } else {
                                        HashMap transMap = new HashMap();
                                        transMap.put("ACT_NUM", actNum);
                                        transMap.put("BRANCH_CODE", _branchCode);
                                        List sanctionLst = sqlMap.executeQueryForList("getNoOfDaysinLoan", transMap);
                                        if (sanctionLst != null && sanctionLst.size() > 0) {
                                            HashMap recordMap = (HashMap) sanctionLst.get(0);
                                            int repayFreq = 0;
                                            repayFreq = CommonUtil.convertObjToInt(recordMap.get("REPAYMENT_FREQUENCY"));
                                            if (repayFreq == 1) {
                                                java.util.Date expiry_dt = null;
                                                expiry_dt = (java.util.Date) recordMap.get("TO_DT");
                                                expiry_dt = (java.util.Date) expiry_dt.clone();
                                                System.out.println("########## expiry_dt : "+expiry_dt);
                                                if (DateUtil.dateDiff(intUptoDt, expiry_dt) >= 0L) {
                                                    principalAmount = 0.0D;
                                                    System.out.println("888888principalAmount" + principalAmount);
                                                    dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
                                                } else {
                                                    System.out.println("999999balanceLoanAmt" + balanceLoanAmt);
                                                    dataMap.put("PRINCIPAL", String.valueOf(balanceLoanAmt));
                                                    principalAmount = balanceLoanAmt;
                                                }
                                            } else {
                                                if (repayDate.compareTo((Date) currDt.clone()) < 1) {
                                                    dataMap.put("PRINCIPAL", String.valueOf(balanceLoanAmt));
                                                    principalAmount = balanceLoanAmt;
                                                } else {
                                                    dataMap.put("PRINCIPAL", String.valueOf("0"));
                                                    principalAmount = 0.0;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (prodType.equals("AD")) {
//                            if (retired != null && retired.equals("YES")) {
//                                System.out.println("111222333444clear_balance" + clear_balance);
//                                dataMap.put("PRINCIPAL", String.valueOf(Math.abs(Integer.parseInt(clear_balance))));
//                            } else {
                                if (principalAmount > 0.0D) {
                                    System.out.println("222333444555principalAmount" + principalAmount);
                                    dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
                                } else {
                                    System.out.println("333444555666");
                                    dataMap.put("PRINCIPAL", "0");
                                }
//                            }
                        }
                        if (installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y")) {
                            double dueamount = 0.0D;
                            double penal = 0.0D;
                            double totEmi = 0.0D;
                            double paidEmi = 0.0D;
                            double principle = 0.0D;
                            double interst = Double.parseDouble(insertPenal.get("CURR_MONTH_INT").toString());
                            System.out.println("interst"+interst);
                            HashMap emi = new HashMap();
                            java.util.Date upto = (java.util.Date) currDt.clone();
                            emi.put("ACC_NUM", actNum);
                            emi.put("UP_TO", upto);
                            List totalEmiList = sqlMap.executeQueryForList("TotalEmi", emi);
                            if (totalEmiList != null && totalEmiList.size() > 0 && ((HashMap) totalEmiList.get(0)).get("TOTAL_AMOUNT") != null) {
                                HashMap aMap = new HashMap();
                                aMap = (HashMap) totalEmiList.get(0);
                                totEmi = Double.parseDouble(aMap.get("TOTAL_AMOUNT").toString());
                                System.out.println("TOTAL_AMOUNTv"+totEmi);
                            } else {
                                totEmi = 0.0D;
                            }
                            HashMap paid = new HashMap();
                            paid.put("ACT_NUM", actNum);
                            paid.put("BRANCH_CODE", _branchCode);
                            List paidAmtemi = sqlMap.executeQueryForList("getPaidPrincipleEMI", loanInstall);
                            if (paidAmtemi != null && paidAmtemi.size() > 0 && ((HashMap) paidAmtemi.get(0)).get("PRINCIPLE") != null) {
                                paid = (HashMap) paidAmtemi.get(0);
                                System.out.println("!!!!asAndWhenMap:"+paid);
                                paidEmi = CommonUtil.convertObjToDouble(paid.get("PRINCIPLE")).doubleValue();
                                System.out.println("paidEmi"+paidEmi);
                            } else {
                                paidEmi = 0.0D;
                            }
                            System.out.println("totEmi"+totEmi+"paidEmi"+paidEmi);
                            dueamount = totEmi - paidEmi;
                            double paidamount = paidEmi;
                            if (dueamount <= 0.0D) {
                                dueamount = CommonUtil.convertObjToDouble(dataMap.get("PRINCIPAL")).doubleValue() + interst;
                            }
                            System.out.println("totalDemandsdas"+dueamount);
                            System.out.println("+==========PENAL STARTS==============================+");
                            List scheduleList = sqlMap.executeQueryForList("getSchedules", emi);
                            java.util.Date penalStrats = new java.util.Date();
                            if (scheduleList != null && scheduleList.size() > 0) {
                                int k = 0;
                                do {
                                    if (k >= scheduleList.size()) {
                                        break;
                                    }
                                    HashMap eachInstall = new HashMap();
                                    eachInstall = (HashMap) scheduleList.get(k);
                                    System.out.println("eachInstall"+eachInstall);
                                    double scheduledEmi = Double.parseDouble(eachInstall.get("TOTAL_AMT").toString());
                                    if (paidamount >= scheduledEmi) {
                                        System.out.println("111paidamount"+paidamount+"scheduledEmi"+scheduledEmi);
                                        paidamount -= scheduledEmi;
                                        System.out.println("paidamount"+paidamount);
                                    } else {
                                        String in_date = CommonUtil.convertObjToStr(eachInstall.get("INSTALLMENT_DT"));
                                        penalStrats = DateUtil.getDateMMDDYYYY(in_date);
                                        System.out.println("penalStrats....."+penalStrats);
                                        break;
                                    }
                                    k++;
                                } while (true);
                                emi.put("FROM_DATE", penalStrats);
                                List getPenalData = sqlMap.executeQueryForList("getPenalData", emi);
                                List penalInterstRate = sqlMap.executeQueryForList("getPenalIntestRatefromMaintenance", emi);
                                double interstPenal = 0.0D;
                                double garce = 0.0D;
                                List graceDays = sqlMap.executeQueryForList("getGracePeriodDays", emi);
                                if (graceDays != null && graceDays.size() > 0) {
                                    HashMap map = new HashMap();
                                    map = (HashMap) graceDays.get(0);
                                    if (map != null && map.containsKey("GRACE_PERIOD_DAYS") && map.get("GRACE_PERIOD_DAYS") != null) {
                                        garce = Double.parseDouble(map.get("GRACE_PERIOD_DAYS").toString());
                                    } else {
                                        garce = 0.0D;
                                    }
                                } else {
                                    garce = 0.0D;
                                }
                                long gracedy = (long) garce;
                                int graceint = (int) garce;
                                if (penalInterstRate != null && penalInterstRate.size() > 0) {
                                    HashMap test = new HashMap();
                                    test = (HashMap) penalInterstRate.get(0);
                                    if (test != null && test.containsKey("PENAL_INTEREST") && test.get("PENAL_INTEREST") != null) {
                                        interstPenal = Double.parseDouble(test.get("PENAL_INTEREST").toString());
                                    } else {
                                        List limitList = sqlMap.executeQueryForList("getLimitFromLoanSanc", emi);
                                        double limit = Double.parseDouble(((HashMap) limitList.get(0)).get("LIMIT").toString());
                                        emi.put("LIMIT", Double.valueOf(limit));
                                        List penalFromROI = sqlMap.executeQueryForList("getPenalIntestRatefromROI", emi);
                                        if (penalFromROI != null && penalFromROI.size() > 0) {
                                            test = new HashMap();
                                            test = (HashMap) penalFromROI.get(0);
                                            System.out.println("testttt"+test);
                                            interstPenal = Double.parseDouble(test.get("PENAL_INT").toString());
                                        }
                                    }
                                } else {
                                    List limitList = sqlMap.executeQueryForList("getLimitFromLoanSanc", emi);
                                    double limit = Double.parseDouble(((HashMap) limitList.get(0)).get("LIMIT").toString());
                                    emi.put("LIMIT", Double.valueOf(limit));
                                    List penalFromROI = sqlMap.executeQueryForList("getPenalIntestRatefromROI", emi);
                                    if (penalFromROI != null && penalFromROI.size() > 0) {
                                        HashMap test = new HashMap();
                                        test = (HashMap) penalFromROI.get(0);
                                        interstPenal = Double.parseDouble(test.get("PENAL_INT").toString());
                                    }
                                }
                                System.out.println("interstPenal...."+interstPenal);
                                if (getPenalData != null && getPenalData.size() > 0) {
                                    for (k = 0; k < getPenalData.size(); k++) {
                                        HashMap amap = new HashMap();
                                        amap = (HashMap) getPenalData.get(k);
                                        String in_date = CommonUtil.convertObjToStr(amap.get("INSTALLMENT_DT"));
                                        java.util.Date currntDate = DateUtil.getDateMMDDYYYY(in_date);
                                        currntDate = DateUtil.addDays(currntDate, graceint);
                                        System.out.println("5555currntDate.."+currntDate);
                                        HashMap holidayMap = new HashMap();
                                        holidayMap.put("CURR_DATE", currntDate);
                                        holidayMap.put("BRANCH_CODE", _branchCode);
                                        holidayMap = new HashMap();
                                        boolean checkHoliday = true;
                                        String str = "any next working day";
                                        System.out.println("instDate   "+currntDate);
                                        currntDate = CommonUtil.getProperDate(currDt,currntDate);
                                        holidayMap.put("NEXT_DATE", currntDate);
                                        holidayMap.put("BRANCH_CODE", _branchCode);
                                        while (checkHoliday) {
                                            boolean tholiday = false;
                                            List Holiday = sqlMap.executeQueryForList("checkHolidayDateOD", holidayMap);
                                            List weeklyOf = sqlMap.executeQueryForList("checkWeeklyOffOD", holidayMap);
                                            boolean isHoliday = Holiday.size() > 0;
                                            boolean isWeekOff = weeklyOf.size() > 0;
                                            if (isHoliday || isWeekOff) {
                                                if (str.equals("any next working day")) {
                                                    currntDate.setDate(currntDate.getDate() + 1);
                                                } else {
                                                    currntDate.setDate(currntDate.getDate() - 1);
                                                }
                                                holidayMap.put("NEXT_DATE", currntDate);
                                                checkHoliday = true;
                                                System.out.println("#### holidayMap : "+holidayMap);
                                            } else {
                                                checkHoliday = false;
                                            }
                                        }
                                        System.out.println("currntDatemmm"+currntDate);
                                        System.out.println("DateUtil.dateDiff(currntDate,upto)"+DateUtil.dateDiff(currntDate, upto));
                                        long difference = DateUtil.dateDiff(currntDate, upto) - 1L;
                                        if (difference < 0L) {
                                            difference = 0L;
                                        }
                                        System.out.println("difference..."+difference);
                                        double installment = Double.parseDouble(amap.get("TOTAL_AMT").toString());
                                        System.out.println("installmentsadeasdasd"+installment);
                                        penal += (installment * (double) difference * interstPenal) / 36500D;
                                        System.out.println("penallcalcuuu"+penal);
                                    }

                                }
                            }
                            principle = dueamount - interst;
                            System.out.println("mmmprinciple"+principle+"::penal"+penal+"::interst"+interst);
                            totalDemand = principle + penal + interst;
                            totalDemand = Math.round(totalDemand);
                            principle = Math.round(principle);
                            penal = Math.round(penal);
                            interst = Math.round(interst);
                            System.out.println("tttttoooo"+totalDemand);
                            dataMap.put("INTEREST", Long.valueOf(Math.round(Double.parseDouble(insertPenal.get("CURR_MONTH_INT").toString()))));
                            dataMap.put("PENAL", Double.valueOf(penal));
//                            if (totalDemand > 0) {
//                                dataMap.put("TOTAL_DEMAND", Math.round(CommonUtil.convertObjToDouble(totalDemand)));
//                            } else {
//                                dataMap.put("TOTAL_DEMAND", "0");
//                                dataMap = null;
//                                return dataMap;
//                            }
//                            dataMap.put("CLEAR_BALANCE", clear_balance);
//                            System.out.println("mmmmmmiiinnneee"+dataMap);
                            if(principle > 0){
                                dataMap.put("PRINCIPAL", principle);
                            }
                            HashMap detailedMap = new HashMap();
                            detailedMap.put("ACCT_NUM", actNum);
                            detailedMap.put("ASONDT", (Date)intUptoDt.clone());
                            detailedMap.put("INSTALL_TYPE", installtype);
                            detailedMap.put("EMI_IN_SIMPLE_INTEREST", emi_uniform);
                            List advanceEmiList = sqlMap.executeQueryForList("getAdvAmtEmi", detailedMap);
                            if(advanceEmiList != null && advanceEmiList.size()>0){
                                HashMap advMap = (HashMap) advanceEmiList.get(0);
                                if(advMap != null && advMap.size()>0 && advMap.containsKey("BALANCE")){
                                    double balance = CommonUtil.convertObjToDouble(advMap.get("BALANCE"));
                                    System.out.println("balance"+balance);
                                    if(balance <= 0){
                                        double interest = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT"));
                                        double insAmt = CommonUtil.convertObjToDouble(dataMap.get("INSTMT_AMT"));
                                        double princi = insAmt - interest;
                                        double princip = CommonUtil.convertObjToDouble(dataMap.get("PRINCIPAL"));
                                        
                                        System.out.println("interest"+interest+"insAmt"+insAmt+"princi"+princi+"princip"+princip);
                                        
                                        if (princi > 0) {
                                            if (princip > 0) {
                                                totalDemand -= princip;
                                                System.out.println("jefff");
                                            }
                                            totalDemand += princi;
                                            dataMap.put("PRINCIPAL", princi);
                                            System.out.println("prasnth"+totalDemand);
                                        }
                                    }
                                }
                            }
                            
                            double insttAmt = CommonUtil.convertObjToDouble(dataMap.get("INSTMT_AMT"));
                            double tempPrin = CommonUtil.convertObjToDouble(dataMap.get("PRINCIPAL"));
                            double tempInt = CommonUtil.convertObjToDouble(dataMap.get("INTEREST"));
                            
                            System.out.println("insttAmt"+insttAmt+"tempPrin"+tempPrin+"tempInt"+tempInt);
                            
                            double temmmpPrinc = insttAmt - tempInt;
                            if(tempPrin < temmmpPrinc){
                                totalDemand -= tempPrin;
                                totalDemand += temmmpPrinc;
                                dataMap.put("PRINCIPAL", temmmpPrinc);
                                System.out.println("prasnth" + totalDemand);
                            }else{
                                HashMap balanceMap = new HashMap(); 
                                balanceMap.put("ACCOUNTNO",actNum);
                                double outstandingAmt = 0;
                                List balannceAmtLst = sqlMap.executeQueryForList("getBalanceLoanPrincipalAmt", balanceMap);
                                if (balannceAmtLst != null && balannceAmtLst.size() > 0) {
                                    balanceMap = (HashMap) balannceAmtLst.get(0);
                                    outstandingAmt = CommonUtil.convertObjToDouble(balanceMap.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();
                                }
                                if(insttAmt<tempInt){
                                    double count = tempInt / insttAmt;
                                    System.out.println("count : "+count);
                                    if(count<0){
                                        temmmpPrinc = insttAmt - tempInt;
                                    }else{
                                        int count1 = (int) count + 1;
                                        System.out.println("else count : "+count1);
                                        temmmpPrinc = (insttAmt * (count1+1)) - tempInt;
                                    }
                                    dataMap.put("PRINCIPAL", temmmpPrinc);
                                    System.out.println("condition temmmpPrinc : "+temmmpPrinc);
                                }else{
                                    System.out.println("outstandingAmt : "+outstandingAmt);
                                    if(outstandingAmt<insttAmt){
                                        temmmpPrinc = outstandingAmt;
                                        totalDemand = outstandingAmt + penal+tempInt;
                                        dataMap.put("PRINCIPAL", temmmpPrinc);
                                        System.out.println("outstandingAmt condition temmmpPrinc : "+temmmpPrinc);
                                    }else{
                                        HashMap principlePaidMap = new HashMap();
                                        Date fromDate = (Date) currDt.clone();
                                        fromDate.setDate(1);
                                        principlePaidMap.put("LAST_INT_CALC_DT",getProperDateFormat(fromDate));
                                        principlePaidMap.put("CURR_DATE",currDt.clone());
                                        principlePaidMap.put("ACT_NUM",actNum);
                                        List principlePaidList = sqlMap.executeQueryForList("getSelectLoanPaidPrincipleMaxtransDt", principlePaidMap);
                                        if (principlePaidList != null && principlePaidList.size() > 0) {
                                            principlePaidMap = (HashMap) principlePaidList.get(0);
                                            outstandingAmt = CommonUtil.convertObjToDouble(principlePaidMap.get("PRINCIPLE_PAID")).doubleValue();                                        
                                            if(outstandingAmt >= insttAmt){
                                                alreadyPaidThisMonth = true;
                                                System.out.println("Complete amount already paid for this account No : "+actNum);
                                            }else{
                                                if(outstandingAmt>0){
                                                    temmmpPrinc = insttAmt - outstandingAmt;
                                                    totalDemand = insttAmt+penal;
                                                    dataMap.put("PRINCIPAL", temmmpPrinc);                                                
                                                }
                                            }
                                        }else{
                                            temmmpPrinc = insttAmt - tempInt;
                                            totalDemand = insttAmt+penal;
                                            dataMap.put("PRINCIPAL", temmmpPrinc);
                                        }
                                        System.out.println("else outstandingAmt temmmpPrinc : "+temmmpPrinc);
                                    }
                                }
                            }
                            
                            if (totalDemand > 0) {
                                dataMap.put("TOTAL_DEMAND", Math.round(CommonUtil.convertObjToDouble(totalDemand)));
                            } else {
                                dataMap.put("TOTAL_DEMAND", "0");
                                dataMap = null;
                                return dataMap;
                            }
                            dataMap.put("CLEAR_BALANCE", clear_balance);
                        } else {
                            totalDemand += principalAmount;
                            System.out.println("totalDemand 333===="+totalDemand);
                            dataMap.put("INTEREST", insertPenal.get("CURR_MONTH_INT"));
                            dataMap.put("PENAL", insertPenal.get("PENAL_INT"));
                            if (totalDemand > 0) {
                                dataMap.put("TOTAL_DEMAND", Math.round(CommonUtil.convertObjToDouble(totalDemand)));
                            } else {
                                dataMap.put("TOTAL_DEMAND", "0");
                                dataMap = null;
                                return dataMap;
                            }
                            dataMap.put("CLEAR_BALANCE", clear_balance);
                        }
                        if (flag) {
                            System.out.println("444555666777");
                            dataMap.put("PRINCIPAL", Integer.valueOf(0));
                            flag = false;
                        }
                    }
                    if (prodType != null && !prodType.equals("") && prodType.equals("AD")) {
                        HashMap intMap = new HashMap();
                        intMap.put("AS_ON_DATE", currDt.clone());
                        intMap.put("ACCT_NUM", CommonUtil.convertObjToStr(actNum));
                        List penalADList = sqlMap.executeQueryForList("TransAll.getPenalAmountForAD", intMap);
                        if (penalADList != null && penalADList.size() > 0) {
                            HashMap intDetailMap = (HashMap) penalADList.get(0);
                            System.out.println("intDetailMap" + intDetailMap);
                            if (intDetailMap != null && intDetailMap.size() > 0 && intDetailMap.containsKey("PENAL")) {
                                double penal = CommonUtil.convertObjToDouble(intDetailMap.get("PENAL"));
                                if (penal > 0) {
                                    dataMap.put("PENAL", penal);
                                } else {
                                    dataMap.put("PENAL", "0");
                                }
                            }
                        }
                    }
                    //Added by chithra for report correction-----------------------------
                    if (dataMap != null && prodType != null && prodType.equals("TL")) {
                        if (resltList != null && resltList.size() > 0) {
                            System.out.println("dataMap ********** ZZZ :" + dataMap);
                            double penalVal = CommonUtil.convertObjToDouble(dataMap.get("PENAL"));
                            double demandVal = 0;
                            if (princVal >= 0) {
                                demandVal = princVal + penalVal + intVal;
                            } else {
                                demandVal = penalVal + intVal;
                            }
                            System.out.println("totalDemand : " + demandVal);
                            dataMap.put("INTEREST", intVal);
                            if (princVal >= 0) {
                                dataMap.put("PRINCIPAL", princVal);
                            }
                            if (demandVal > 0) {
                                dataMap.put("TOTAL_DEMAND", demandVal);
                            } else {
                                dataMap.put("TOTAL_DEMAND", "0");
                                dataMap = null;
                                return dataMap;
                            }
                            System.out.println("dataMap ********** aaa :" + dataMap);
                        }
                    }
                     if(prodType != null && prodType.equals("TL") && remittedStatus.equals("PAID")){
                        dataMap = null;
                    }
                     if (prodType != null && prodType.equals("AD")) {
                        HashMap hamap = new HashMap();
                        hamap.put("ACT_NUM", CommonUtil.convertObjToStr(actNum));
                        hamap.put("TRANS_DT", getProperDateFormat(intUptoDt.clone()));
                       // System.out.println("intUptoDt ---ZQ-----------------EXC--- :" + intUptoDt);
                        List reList = sqlMap.executeQueryForList("getRemittedStatus", hamap);
                        if (reList != null && reList.size() > 0) {
                            HashMap newMap = (HashMap) reList.get(0);
                            String stat = CommonUtil.convertObjToStr(newMap.get("STATUS"));
                            if (stat != null && stat.equals("ADVANCE")) {
                                List rList = sqlMap.executeQueryForList("getRemittedRecordCount", hamap);
                                if (rList != null && rList.size() > 0) {
                                    HashMap pMap = (HashMap) rList.get(0);
                                    int countN = CommonUtil.convertObjToInt(pMap.get("SAT_NUM"));
                                    if (countN > 0) {
                                        dataMap = null;
                                    }
                                }
                            }
                        }

                    }
                } else {
                    System.out.println("### Not Allow : " + checkDate);
                    dataMap = null;
                }
            }
        }
        }
        System.out.println("####### Single Row DataMap : "+dataMap);
        System.out.println("dataMapdataMapdataMapdataMap" + dataMap);
        return dataMap;
    }

    private double getChargeAmount(HashMap whereMap, String prodType) {   //Charges
        double chargeAmount = 0.0;
        try {
            List chargeList = null;
            String actNo = "";
            HashMap recoverChrgMap = new HashMap();
            actNo = CommonUtil.convertObjToStr(whereMap.get("ACT_NUM"));
            chargeList = sqlMap.executeQueryForList("getChargeDetails", whereMap);
            if (chargeList != null && chargeList.size() > 0) {
                for (int i = 0; i < chargeList.size(); i++) {
                    whereMap = (HashMap) chargeList.get(i);
                    chargeAmount += CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
                    double chrgAmt = 0.0;
                    chrgAmt = CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
                    if (chrgAmt > 0) {
                        recoverChrgMap = new HashMap();
                        recoverChrgMap.put("INT_CALC_UPTO_DT", intUptoDt);
                        recoverChrgMap.put("ACT_NUM", actNo);
                        recoverChrgMap.put("CHARGE_TYPE", CommonUtil.convertObjToStr(whereMap.get("CHARGE_TYPE")));
                        recoverChrgMap.put("AMOUNT", CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")));
                        System.out.println("####### recoverChrgMap:" + recoverChrgMap);
                        sqlMap.executeUpdate("insertRecoveryChargesList", recoverChrgMap);
                    }
                }
            }
            chargeList = null;
            recoverChrgMap = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chargeAmount;
    }

//    public HashMap interestCalculationTLAD(Object accountNo, Object prod_id, String prodType) throws Exception {
//        HashMap dataMap = new HashMap();
//        HashMap hash = new HashMap();
//        try {
//            hash.put("ACT_NUM", accountNo);
//            hash.put("PRODUCT_TYPE", prodType);
//            hash.put("PROD_ID", prod_id);
//            hash.put("TRANS_DT", interestUptoDt);
//            hash.put("INITIATED_BRANCH", _branchCode);
//            String mapNameForCalcInt = "IntCalculationDetail";
//            if (prodType.equals("AD")) {
//                mapNameForCalcInt = "IntCalculationDetailAD";
//            }
//            List lst = sqlMap.executeQueryForList(mapNameForCalcInt, hash);
//            System.out.println(accountNo + "," + prod_id + "," + "LIST   1>>>>>>" + lst);
//            if (lst != null && lst.size() > 0) {
//                hash = (HashMap) lst.get(0);
//                java.util.Iterator iterator = hash.keySet().iterator();
//              /*  while (iterator.hasNext()) {
//                    String key = iterator.next().toString();
//                    String value = hash.get(key).toString();
//                    System.out.println(key + " " + value);
//                }*/
//                if (hash.get("AS_CUSTOMER_COMES") != null && hash.get("AS_CUSTOMER_COMES").equals("N")) {
//                    hash = new HashMap();
//                    return hash;
//                }
//                hash.put("ACT_NUM", accountNo);
//                hash.put("PRODUCT_TYPE", prodType);
//                hash.put("PROD_ID", prod_id);
//                hash.put("TRANS_DT", intUptoDt);
//                hash.put("INITIATED_BRANCH", _branchCode);
//                hash.put("ACT_NUM", accountNo);
//                hash.put("BRANCH_ID", _branchCode);
//                hash.put("BRANCH_CODE", _branchCode);
//                hash.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
//                hash.put("CURR_DATE", interestUptoDt);
//                dataMap.put(CommonConstants.MAP_WHERE, hash);
//                System.out.println("map before intereest###" + dataMap);
//                hash = new SelectAllDAO().executeQuery(dataMap);
//                if (hash == null) {
//                    hash = new HashMap();
//                }
//                if (hash.containsKey("DATA") && hash.get("DATA") != null) {
//                    hash.putAll((HashMap) ((List) hash.get("DATA")).get(0));
//                }
//                hash.putAll((HashMap) dataMap.get(CommonConstants.MAP_WHERE));
//                System.out.println("hashinterestoutput###" + hash);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return hash;
//    }
    
    public HashMap interestCalculationTLAD(Object accountNo, Object prod_id, String prodType) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap hash = new HashMap();
        try {
            hash.put("ACT_NUM", accountNo);
            hash.put("PRODUCT_TYPE", prodType);
            hash.put("PROD_ID", prod_id);
            hash.put("TRANS_DT", interestUptoDt);
            hash.put("INITIATED_BRANCH", _branchCode);
            String mapNameForCalcInt = "IntCalculationDetail";
            if (prodType.equals("AD")) {
                mapNameForCalcInt = "IntCalculationDetailAD";
            }
            List lst = sqlMap.executeQueryForList(mapNameForCalcInt, hash);
            System.out.println(accountNo + "," + prod_id + "," + "LIST 1" + lst);
            if (lst != null && lst.size() > 0) {
                hash = (HashMap) lst.get(0);
                java.util.Iterator iterator = hash.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next().toString();
                    String value = CommonUtil.convertObjToStr(hash.get(key));
                    System.out.println(key + " " + value);
                }
                if (hash.get("AS_CUSTOMER_COMES") != null && hash.get("AS_CUSTOMER_COMES").equals("N")) {
                    hash = new HashMap();
                    return hash;
                }
                hash.put("ACT_NUM", accountNo);
                hash.put("PRODUCT_TYPE", prodType);
                hash.put("PROD_ID", prod_id);
                hash.put("TRANS_DT", intUptoDt);
                hash.put("INITIATED_BRANCH", _branchCode);
                hash.put("ACT_NUM", accountNo);
                hash.put("BRANCH_ID", _branchCode);
                hash.put("BRANCH_CODE", _branchCode);
                hash.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
                hash.put("CURR_DATE", interestUptoDt);
                dataMap.put(CommonConstants.MAP_WHERE, hash);
                System.out.println("map before intereest" + dataMap);
                hash = new SelectAllDAO().executeQuery(dataMap);
                if (hash == null) {
                    hash = new HashMap();
                }
                if (hash.containsKey("DATA") && hash.get("DATA") != null) {
                    hash.putAll((HashMap) ((List) hash.get("DATA")).get(0));
                }
                hash.putAll((HashMap) dataMap.get(CommonConstants.MAP_WHERE));
                System.out.println("hashinterestoutput" + hash);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
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
    
    public Date getProperDateFormat(Object obj) {
        Date currDate1 = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDate1 = (Date) currDt.clone();
            currDate1.setDate(tempDt.getDate());
            currDate1.setMonth(tempDt.getMonth());
            currDate1.setYear(tempDt.getYear());
        }
        return currDate1;
    }
}