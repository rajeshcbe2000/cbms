/*
 * Copyright 2012 Fincuro Solutions (P) Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * LoanRecoveryDAO.java
 *
 * Created on Fri Jan 11 13:11:28 IST 2019
 */
package com.see.truetransact.serverside.termloan.loanrecovery;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.common.viewall.SelectAllDAO;
import com.see.truetransact.serverside.mdsapplication.MDSStandingInstructionDAO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.deposit.interestmaintenance.InterestMaintenanceRateTO;
import com.see.truetransact.transferobject.termloan.loanrecovery.LoanSalaryRecoveryListCustDataTO;
import com.see.truetransact.transferobject.termloan.loanrecovery.LoanSalaryRecoveryListDetailTO;
import com.see.truetransact.transferobject.termloan.loanrecovery.LoanSalaryRecoveryListMasterTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * LoanRecovery DAO.
 *
 */
public class LoanRecoveryDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private Date currDt = null;
    private HashMap returnMap = null;
    private HashMap finalMap = null;
    private List recoveryList = null;
    private List recoveryRowList = null;
    private Date intUptoDt = null;
    private Date interestUptoDt = null;
    DecimalFormat df = new DecimalFormat("##.00");
    private final static double AVERAGE_MILLIS_PER_MONTH = 365.24 * 24 * 60 * 60 * 1000 / 12;
    Rounding rd = new Rounding();
    private String recoveryId = "";
    TransferDAO transferDAO = new TransferDAO();
    private Iterator processLstIterator;
    private TransactionDAO transactionDAO = null;

    /**
     * Creates a new instance of LoanRecoveryDAO
     */
    public LoanRecoveryDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public static void main(String str[]) {
        try {
            LoanRecoveryDAO dao = new LoanRecoveryDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        returnMap = new HashMap();
        System.out.println(" ###### Map in RecoveryListGenerationDAO DAO : " + map);
        try {
            HashMap transDetailMap = new HashMap();
            if (map.containsKey("RECOVERY_PROCESS_LIST") && map.get("RECOVERY_PROCESS_LIST") != null
                    && map.containsKey("TRANSACTION_DETAILS_DATA")) {
                intUptoDt = (Date) map.get("CALC_INT_UPTO_DT");
                transDetailMap = (HashMap) map.get("TRANSACTION_DETAILS_DATA");
                processTransactionPart(map, transDetailMap);
                returnMap.put("STATUS", "COMPLETED");
            }else if (map.containsKey("RECOVERY_MDS_PROCESS_LIST") && map.get("RECOVERY_MDS_PROCESS_LIST") != null
                    && map.containsKey("TRANSACTION_DETAILS_DATA")) {
                intUptoDt = (Date) map.get("CALC_INT_UPTO_DT");
                transDetailMap = (HashMap) map.get("TRANSACTION_DETAILS_DATA");
                processMDSTransactionPart(map, transDetailMap);
                returnMap.put("STATUS", "COMPLETED");
            }else if (map.containsKey("RECOVERY_RD_PROCESS_LIST") && map.get("RECOVERY_RD_PROCESS_LIST") != null
                    && map.containsKey("TRANSACTION_DETAILS_DATA")) {
                intUptoDt = (Date) map.get("CALC_INT_UPTO_DT");
                transDetailMap = (HashMap) map.get("TRANSACTION_DETAILS_DATA");
                processRDTransactionPart(map, transDetailMap);
                returnMap.put("STATUS", "COMPLETED");
            }else if (map.containsKey("RECOVERY_AMT_UPDATE_LIST") && map.get("RECOVERY_AMT_UPDATE_LIST") != null){                 
                intUptoDt = (Date) map.get("CALC_INT_UPTO_DT");
                recoveryAmountUpdationProcess(map);
                returnMap.put("STATUS", "COMPLETED");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMap.put("STATUS", "FAILED");
            throw e;
        }
        destroyObjects();
        System.out.println("@#$@@$@@@$ returnMap : " + returnMap);
        return returnMap;
    }

    public void processTransactionPart(HashMap map, HashMap transactionMap) throws Exception {
        try {
            HashMap finalMap = new HashMap();
            finalMap = (HashMap) map.get("RECOVERY_PROCESS_LIST");
            ArrayList recoveryList;
            recoveryList = (ArrayList) finalMap.get("SALARYRECOVERY_POST_LIST");
            processLstIterator = finalMap.keySet().iterator();
            String key1 = "";
            String actNum = "";
            String prodType = "";

            if (recoveryList.size() > 0) {
                for (int i = 0; i < recoveryList.size(); i++) {
                    ArrayList newList = (ArrayList) recoveryList.get(i);
                    HashMap singleAccountMap = new HashMap();
                    prodType = CommonUtil.convertObjToStr(newList.get(2));
                    actNum = CommonUtil.convertObjToStr(newList.get(3));
                    singleAccountMap.put("PRINCIPAL", CommonUtil.convertObjToStr(newList.get(4)));
                    singleAccountMap.put("INTEREST", CommonUtil.convertObjToStr(newList.get(5)));
                    singleAccountMap.put("PENAL", CommonUtil.convertObjToStr(newList.get(6)));
                    singleAccountMap.put("CHARGES", CommonUtil.convertObjToStr(newList.get(7)));
                    singleAccountMap.put("RECOVERED_AMOUNT", CommonUtil.convertObjToStr(newList.get(8)));
                    singleAccountMap.put("ACT_NUM", actNum);
                    singleAccountMap.put("PROD_TYPE", prodType);
                    singleAccountMap.put("PROD_ID", CommonUtil.convertObjToStr(newList.get(1)));
                    if(CommonUtil.convertObjToStr(newList.get(9)).length() == 0){
                     singleAccountMap.put("TOTAL_BAL_AMOUNT", null);   
                    }else{
                     singleAccountMap.put("TOTAL_BAL_AMOUNT", CommonUtil.convertObjToStr(newList.get(9)));
                    }
                    singleAccountMap.put("CUST_ID", CommonUtil.convertObjToStr(newList.get(10)));
                    if (prodType.equals(TransactionFactory.LOANS) || prodType.equals(TransactionFactory.ADVANCES)) {
                        transactionPartLoans(singleAccountMap, map, transactionMap);
                    }
                }
                HashMap listGenerationMap = new HashMap();
                listGenerationMap.put("RECOVERY_ID", map.get("RECOVERY_ID"));
                List listGeneration = sqlMap.executeQueryForList("getLoanRecoveryListGenerationProcessed", listGenerationMap);
                if (!(listGeneration != null && listGeneration.size() > 0)) {
                    sqlMap.executeUpdate("updateLoanRecoveryListGenerationMaster", listGenerationMap);
                }

            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    public Date getProperDateFormat(Object obj) {
        Date currDate = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDate = (Date) currDt.clone();
            currDate.setDate(tempDt.getDate());
            currDate.setMonth(tempDt.getMonth());
            currDate.setYear(tempDt.getYear());
        }
        return currDate;
    }

    private void transactionPartLoans(HashMap loanMap, HashMap map, HashMap transactionMap) throws Exception {
        try {
            HashMap txMap = new HashMap();
            HashMap loanDataMap = new HashMap();
            ArrayList TxTransferTO = new ArrayList();
            TxTransferTO transferTo = new TxTransferTO();
            HashMap ALL_LOAN_AMOUNT = new HashMap();
            double penalAmount = 0;
            double chargesAmount = 0;
            double interestAmount = 0;
            double principalAmount = 0;
            double recoveredAmount = 0;
            double totalBalanceAmount = 0.0;
            String recoveryId = "";
            penalAmount = CommonUtil.convertObjToDouble(loanMap.get("PENAL")).doubleValue();
            chargesAmount = CommonUtil.convertObjToDouble(loanMap.get("CHARGES")).doubleValue();
            interestAmount = CommonUtil.convertObjToDouble(loanMap.get("INTEREST")).doubleValue();
            principalAmount = CommonUtil.convertObjToDouble(loanMap.get("PRINCIPAL")).doubleValue();
            recoveredAmount = CommonUtil.convertObjToDouble(loanMap.get("RECOVERED_AMOUNT")).doubleValue();
            if(loanMap.containsKey("TOTAL_BAL_AMOUNT") && loanMap.get("TOTAL_BAL_AMOUNT") != null){
               totalBalanceAmount = CommonUtil.convertObjToDouble(loanMap.get("TOTAL_BAL_AMOUNT"));
            }else{
               totalBalanceAmount = 0; 
            }
            recoveryId = CommonUtil.convertObjToStr(map.get("RECOVERY_ID"));
            if (transactionMap.containsKey("TRANSACTION_PART") && recoveredAmount > 0) {
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                transactionDAO.setInitiatedBranch(_branchCode);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                HashMap acHeadMap = new HashMap();
                acHeadMap.put("PROD_ID", loanMap.get("PROD_ID"));
                List lst = sqlMap.executeQueryForList("getAccountHeadProd" + loanMap.get("PROD_TYPE"), acHeadMap);
                if (lst != null && lst.size() > 0) {
                    acHeadMap = (HashMap) lst.get(0);
                }
                HashMap debitMap = new HashMap();
                if (transactionMap != null && transactionMap.get("DR_PROD_TYPE").equals("OA")) {
                    debitMap.put("ACT_NUM", CommonUtil.convertObjToStr(transactionMap.get("DR_ACT_NUM")));
                    lst = sqlMap.executeQueryForList("getAccNoProdIdDet", debitMap);
                    if (lst != null && lst.size() > 0) {
                        debitMap = (HashMap) lst.get(0);
                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(transactionMap.get("DR_ACT_NUM")));
                    }
                }
                if (transactionMap != null && transactionMap.get("DR_PROD_TYPE").equals("SA")) {
                    HashMap susMap = new HashMap();                // Suspense Acc Head
                    susMap.put("PROD_ID", CommonUtil.convertObjToStr(transactionMap.get("DR_ACT_NUM")).substring(4, 7));
                    List susLst = sqlMap.executeQueryForList("getAccountHeadProdSA", susMap);
                    if (susLst != null && susLst.size() > 0) {
                        debitMap = (HashMap) susLst.get(0);
                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(transactionMap.get("DR_ACT_NUM")));
                    }
                }
                if (transactionMap != null && transactionMap.get("DR_PROD_TYPE").equals("AD")) {
                    debitMap.put("prodId", CommonUtil.convertObjToStr(transactionMap.get("DR_ACT_NUM")).substring(4, 7));
                    lst = sqlMap.executeQueryForList("TermLoan.getProdHead", debitMap);
                    if (lst != null && lst.size() > 0) {
                        debitMap = (HashMap) lst.get(0);
                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(transactionMap.get("DR_ACT_NUM"))));
                    }
                }
                //DEBIT
                txMap = new HashMap();
                if (transactionMap != null && transactionMap.get("DR_PROD_TYPE").equals("GL")) {
                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(transactionMap.get("DR_ACT_NUM")));
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                } else if (transactionMap != null && transactionMap.get("DR_PROD_TYPE").equals("OA")) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                    txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(transactionMap.get("DR_ACT_NUM")));
                    txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(transactionMap.get("DR_ACT_NUM")).substring(4, 7));
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.OPERATIVE);
                } else if (transactionMap != null && transactionMap.get("DR_PROD_TYPE").equals("SA")) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HEAD"));
                    txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(transactionMap.get("DR_ACT_NUM")));
                    txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(transactionMap.get("DR_ACT_NUM")).substring(4, 7));
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.SUSPENSE);
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.SUSPENSE);
                }
                txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put("DR_INST_TYPE", "VOUCHER");
                txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(transactionMap.get("DR_ACT_NUM")));
                transferTo = transactionDAO.addTransferDebitLocal(txMap, recoveredAmount);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setRec_mode("RP");
                if (transactionMap != null && !transactionMap.get("DR_PROD_TYPE").equals("GL")) {
                    transferTo.setLinkBatchId(transactionDAO.getLinkBatchID());
                }
                TxTransferTO.add(transferTo);

                //CREDIT
                txMap = new HashMap();
                transferTo = new TxTransferTO();
                txMap.put(TransferTrans.CR_PROD_TYPE, loanMap.get("PROD_TYPE"));
                txMap.put(TransferTrans.CR_PROD_ID, loanMap.get("PROD_ID"));
                txMap.put(TransferTrans.CR_AC_HD, acHeadMap.get("AC_HEAD"));
                txMap.put(TransferTrans.CR_ACT_NUM, loanMap.get("ACT_NUM"));
                txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(transactionMap.get("DR_ACT_NUM")));
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
                if (transactionMap != null && !transactionMap.get("DR_PROD_TYPE").equals("GL")) {
                    transferTo.setLinkBatchId(CommonUtil.convertObjToStr(transactionMap.get("DR_ACT_NUM")));
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
                if (chargesAmount > 0) {
                    List chargeList = sqlMap.executeQueryForList("getChargeDetails", loanMap);
                    if (chargeList != null && chargeList.size() > 0) {
                        double totCharge = 0.0;
                        for (int i = 0; i < chargeList.size(); i++) {
                            HashMap chargeMap = (HashMap) chargeList.get(i);
                            double chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                            totCharge = totCharge + chargeAmt;
                            if (chargeMap.get("CHARGE_TYPE").equals("POSTAGE CHARGES") && chargeAmt > 0) {
                                ALL_LOAN_AMOUNT.put("POSTAGE CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("MISCELLANEOUS CHARGES") && chargeAmt > 0) {
                                ALL_LOAN_AMOUNT.put("MISCELLANEOUS CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("LEGAL CHARGES") && chargeAmt > 0) {
                                ALL_LOAN_AMOUNT.put("LEGAL CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("INSURANCE CHARGES") && chargeAmt > 0) {
                                ALL_LOAN_AMOUNT.put("INSURANCE CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("EXECUTION DECREE CHARGES") && chargeAmt > 0) {
                                ALL_LOAN_AMOUNT.put("EXECUTION DECREE CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("ARBITRARY CHARGES") && chargeAmt > 0) {
                                ALL_LOAN_AMOUNT.put("ARBITRARY CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.containsKey("CHARGE_TYPE") && chargeMap.get("CHARGE_TYPE") != null && chargeMap.get("CHARGE_TYPE").equals("NOTICE CHARGES") && chargeAmt > 0) {
                                ALL_LOAN_AMOUNT.put("NOTICE CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.containsKey("CHARGE_TYPE") && chargeMap.get("CHARGE_TYPE") != null && chargeMap.get("CHARGE_TYPE").equals("OTHER CHARGES") && chargeAmt > 0) {
                                ALL_LOAN_AMOUNT.put("OTHER CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.containsKey("CHARGE_TYPE") && chargeMap.get("CHARGE_TYPE") != null && chargeMap.get("CHARGE_TYPE").equals("EP_COST") && chargeAmt > 0) {
                                ALL_LOAN_AMOUNT.put("EP_COST", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.containsKey("CHARGE_TYPE") && chargeMap.get("CHARGE_TYPE") != null && chargeMap.get("CHARGE_TYPE").equals("ARC_COST") && chargeAmt > 0) {
                                ALL_LOAN_AMOUNT.put("ARC_COST", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.containsKey("CHARGE_TYPE") && chargeMap.get("CHARGE_TYPE") != null && chargeMap.get("CHARGE_TYPE").equals("EXECUTION DECREE CHARGES") && chargeAmt > 0) {
                                ALL_LOAN_AMOUNT.put("EP_COST", chargeMap.get("CHARGE_AMT"));
                            }
                            chargeMap = null;
                        }
                        if (totCharge > 0) {
                            ALL_LOAN_AMOUNT.put("TOTAL_CHARGE", totCharge);
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
                loanDataMap.put("REC_PRINCIPAL", principalAmount);
                loanDataMap.put("VOUCHER_RELEASE", CommonUtil.getProperDate(currDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("CALC_INT_UPTO_DT")))));
                HashMap transMap = transferDAO.execute(loanDataMap, false);
                if (transMap != null && transMap.containsKey("TRANS_ID") && transMap.get("TRANS_ID") != null) {
                    HashMap voucherMap = new HashMap();
                    voucherMap.put("ACT_NUM", CommonUtil.convertObjToStr(loanMap.get("ACT_NUM")));
                    voucherMap.put("VOUCHER_RELEASE_DATE", currDt.clone());
                    voucherMap.put("VOUCHER_RELEASE_BATCH_ID", transMap.get("TRANS_ID"));
                    voucherMap.put("INT_CALC_UPTO_DT", CommonUtil.getProperDate(currDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("CALC_INT_UPTO_DT")))));
                    voucherMap.put("RECOVERED_AMOUNT", recoveredAmount);
                    voucherMap.put("RECOVERY_ID", recoveryId);
                    sqlMap.executeUpdate("updateLoanVoucherReleaseDetails", voucherMap);
                    if(totalBalanceAmount == 0){
                         voucherMap.put("BALANCE_RECOVERED_AMOUNT",new Double(0.0));
                    }else{
                         voucherMap.put("BALANCE_RECOVERED_AMOUNT",totalBalanceAmount - recoveredAmount);
                         sqlMap.executeUpdate("updateCustomerTotalRecoveryAmount", voucherMap);
                    }                    
                    String pTyp = CommonUtil.convertObjToStr(loanMap.get("PROD_TYPE"));
                    if (pTyp != null && pTyp.equals("TL")) {
                        voucherMap = new HashMap();
                        voucherMap.put("ACCOUNTNO", CommonUtil.convertObjToStr(loanMap.get("ACT_NUM")));
                        voucherMap.put("AUTHORIZEDT", CommonUtil.getProperDate(currDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(loanMap.get("INT_CALC_UPTO_DT")))));
                        voucherMap.put("ACCOUNT_STATUS", "CLOSED");
                        sqlMap.executeUpdate("updateAcctStatusForSalaryRecovery", voucherMap);
                    }
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

    public HashMap executeQuery(HashMap obj) throws Exception {
        System.out.println("@#$@@$@@@$ Map in Dao : " + obj);
        HashMap returnMap = new HashMap();
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        intUptoDt = CommonUtil.getProperDate(currDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj.get("CALC_INT_UPTO_DT"))));
        interestUptoDt = (Date) intUptoDt.clone();
        interestUptoDt.setDate(intUptoDt.getDate() + 1);
        interestUptoDt = CommonUtil.getProperDate(currDt, interestUptoDt);
        if (obj.containsKey("LOAN_RECOVERY")) {
            returnMap = getSalaryRecoveryList(obj);
        }
       System.out.println("returnMap" + returnMap);
        return returnMap;
    }

    private HashMap getSalaryRecoveryList(HashMap obj) throws Exception {
        HashMap singleRowMap = new HashMap();
        LinkedHashMap mdsRecoveryMap = new LinkedHashMap();
        HashMap rdRecoveryMap = new HashMap();
        HashMap recoveryListMap = new HashMap();
        HashMap custDataMap =  new HashMap();
        double totalCustAmt = 0.0;
        List listForRecovery = null;
        HashMap chkMap = new HashMap();
        chkMap.put("INTEREST_UP_TO_DT", interestUptoDt);
        chkMap.put("INST_ID", CommonUtil.convertObjToStr(obj.get("INST_ID")));
        listForRecovery = sqlMap.executeQueryForList("getDueDetailsForLoanRecoveryList", chkMap);
        if (listForRecovery != null && listForRecovery.size() > 0) {
            try {
                sqlMap.startTransaction();
                recoveryId = getRecoveryId();
                finalMap = new HashMap();
                HashMap recoveryMap = null;
                recoveryList = new ArrayList();
                String prodType = "";
                String prodID = "";
                String actNum = "";
                String recoveryYesNo = "";
                String empType = "";
                int sno = 1;
                obj.put("RECOVERY_ID", recoveryId);
                HashMap existMap = new HashMap();
                existMap.put("INT_CALC_UPTO_DT", intUptoDt);
                existMap.put("INST_ID", CommonUtil.convertObjToStr(obj.get("INST_ID")));
                List existList = sqlMap.executeQueryForList("checkingSameDateLoanRecord", existMap);
                if (existList != null && existList.size() > 0) {
                    HashMap resultMap = (HashMap) existList.get(0);
                    existMap.put("RECOVERY_ID", resultMap.get("RECOVERY_ID"));
                    sqlMap.executeUpdate("deleteLoanRecoveryDetailsData", existMap);
                }
                for (int i = 0; i < listForRecovery.size(); i++) {
                    boolean retired = false;
                    recoveryMap = (HashMap) listForRecovery.get(i);
                    singleRowMap = new HashMap();
                    double glAmount = 0.0;
                    glAmount = CommonUtil.convertObjToDouble(recoveryMap.get("AMOUNT")).doubleValue();
                    prodType = CommonUtil.convertObjToStr(recoveryMap.get("PROD_TYPE"));
                    prodID = CommonUtil.convertObjToStr(recoveryMap.get("PROD_ID"));
                    actNum = CommonUtil.convertObjToStr(recoveryMap.get("ACT_NUM"));
                    recoveryYesNo = CommonUtil.convertObjToStr(recoveryMap.get("SALARY_RECOVERY"));
                    empType = CommonUtil.convertObjToStr(recoveryMap.get("CUSTOMERGROUP"));
                    if ((prodType.equals(TransactionFactory.LOANS) || prodType.equals(TransactionFactory.ADVANCES)) && glAmount == 0) {
                        singleRowMap = calcLoanPayments(actNum, prodID, prodType, "N");
                   // }          
                    if (singleRowMap != null && singleRowMap.size() > 0) {
                        double totaldemand = CommonUtil.convertObjToDouble(singleRowMap.get("TOTAL_DEMAND"));
                        if (totaldemand > 0) {
                            recoveryRowList = new ArrayList();
                            recoveryRowList.add(String.valueOf(sno++));
                            recoveryRowList.add(CommonUtil.convertObjToStr(recoveryMap.get("EMP_REFNO")));
                            recoveryRowList.add(CommonUtil.convertObjToStr(recoveryMap.get("MEMBER_NO")));
                            recoveryRowList.add(CommonUtil.convertObjToStr(recoveryMap.get("MEM_NAME")));
                            recoveryRowList.add(CommonUtil.convertObjToStr(recoveryMap.get("PROD_DESC")));
                            recoveryRowList.add(CommonUtil.convertObjToStr(recoveryMap.get("ACT_NUM")));
                            recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("TOTAL_DEMAND")));
                            recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("PRINCIPAL")));
                            recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("INTEREST")));
                            recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("PENAL")));
                            recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("CHARGES")));
                            recoveryRowList.add("0.0");
                            recoveryRowList.add("0.0");
                            recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("CLEAR_BALANCE")));
                            recoveryRowList.add(CommonUtil.convertObjToStr(recoveryMap.get("INST_BRANCH_NAME")));                           
                            recoveryList.add(recoveryRowList);
                            //Insert Details Data
                            LoanSalaryRecoveryListDetailTO objSalaryRecoveryListDetailTO = new LoanSalaryRecoveryListDetailTO();
                            objSalaryRecoveryListDetailTO.setIntCalcUptoDt(intUptoDt);
                            objSalaryRecoveryListDetailTO.setEmpRefNo(CommonUtil.convertObjToStr(recoveryMap.get("EMP_REFNO")));
                            objSalaryRecoveryListDetailTO.setMemberName(CommonUtil.convertObjToStr(recoveryMap.get("MEM_NAME")));
                            objSalaryRecoveryListDetailTO.setSchemeName(CommonUtil.convertObjToStr(recoveryMap.get("PROD_DESC")));
                            objSalaryRecoveryListDetailTO.setActNum(CommonUtil.convertObjToStr(recoveryMap.get("ACT_NUM")));
                            String tot_demand = String.valueOf(df.format(CommonUtil.convertObjToDouble(singleRowMap.get("TOTAL_DEMAND"))));
                            objSalaryRecoveryListDetailTO.setTotalDemand(Double.parseDouble(tot_demand));
                            String principal = String.valueOf(df.format(CommonUtil.convertObjToDouble(singleRowMap.get("PRINCIPAL"))));
                            objSalaryRecoveryListDetailTO.setPrincipal(Double.parseDouble(principal));
                            String intrest = String.valueOf(df.format(CommonUtil.convertObjToDouble(singleRowMap.get("INTEREST"))));
                            objSalaryRecoveryListDetailTO.setInterest(Double.parseDouble(intrest));
                            String penal = String.valueOf(df.format(CommonUtil.convertObjToDouble(singleRowMap.get("PENAL"))));
                            objSalaryRecoveryListDetailTO.setPenal(Double.parseDouble(penal));
                            String charge = String.valueOf(df.format(CommonUtil.convertObjToDouble(singleRowMap.get("CHARGES"))));
                            objSalaryRecoveryListDetailTO.setCharges(Double.parseDouble(charge));
                            objSalaryRecoveryListDetailTO.setMdsBonus(new Double(0.0));
                            objSalaryRecoveryListDetailTO.setInstNo(0);
                            objSalaryRecoveryListDetailTO.setForfeitBonus(new Double(0.0));
                            objSalaryRecoveryListDetailTO.setProd_ID(prodID);
                            objSalaryRecoveryListDetailTO.setProd_Type(prodType);
                            objSalaryRecoveryListDetailTO.setStatus(CommonConstants.STATUS_CREATED);
                            objSalaryRecoveryListDetailTO.setRecoveryId(recoveryId);
                            // End
                            sqlMap.executeUpdate("insertOtherSalaryRecoveryListDetailTO", objSalaryRecoveryListDetailTO);                      
                            if (custDataMap.containsKey(recoveryMap.get("EMP_REFNO"))) {
                                totalCustAmt = CommonUtil.convertObjToDouble(custDataMap.get(recoveryMap.get("EMP_REFNO")));                                
                                custDataMap.put(recoveryMap.get("EMP_REFNO"), totalCustAmt + totaldemand);
                            } else {
                                custDataMap.put(recoveryMap.get("EMP_REFNO"), totaldemand);
                            }
                        }
                    }
                } 
                    //Added by nithya on 24-06-2022 for MDS/RD Recovery
                    if (prodType.equals("MDS")) {
                        Date checkDt = CommonUtil.getProperDate(currDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj.get("CALC_INT_UPTO_DT"))));                
                        mdsRecoveryMap = calcMDSPayments(actNum, prodID, prodType,checkDt);
                        System.out.println("singleRowMap :: chittal :: " + actNum +" --->" + mdsRecoveryMap);
                    //}
                    if (mdsRecoveryMap != null && mdsRecoveryMap.size() > 0) {
                        Iterator iterate = mdsRecoveryMap.entrySet().iterator();
                        if (null != mdsRecoveryMap && mdsRecoveryMap.size() > 0) {
                            ArrayList list = new ArrayList();
                            while (iterate.hasNext()) {
                                Map.Entry entry = (Map.Entry) iterate.next();
                                Object key1 = (Object) entry.getKey();
                                Object value = (Object) entry.getValue();
                                singleRowMap = (HashMap) value;
                                double totaldemand = CommonUtil.convertObjToDouble(singleRowMap.get("TOTAL_DEMAND"));
                                if (totaldemand > 0) {
                                    recoveryRowList = new ArrayList();
                                    recoveryRowList.add(String.valueOf(sno++));
                                    recoveryRowList.add(CommonUtil.convertObjToStr(recoveryMap.get("EMP_REFNO")));
                                    recoveryRowList.add(CommonUtil.convertObjToStr(recoveryMap.get("MEMBER_NO")));
                                    recoveryRowList.add(CommonUtil.convertObjToStr(recoveryMap.get("MEM_NAME")));
                                    recoveryRowList.add(CommonUtil.convertObjToStr(recoveryMap.get("PROD_DESC")));
                                    recoveryRowList.add(CommonUtil.convertObjToStr(recoveryMap.get("ACT_NUM")));
                                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("TOTAL_DEMAND")));
                                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("PRINCIPAL")));
                                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("INTEREST")));
                                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("PENAL")));
                                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("CHARGES")));
                                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("BONUS")));
                                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("FORFEIT")));
                                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("CLEAR_BALANCE")));
                                    recoveryRowList.add(CommonUtil.convertObjToStr(recoveryMap.get("INST_BRANCH_NAME")));
                                    recoveryList.add(recoveryRowList);
                                    //Insert Details Data
                                    LoanSalaryRecoveryListDetailTO objSalaryRecoveryListDetailTO = new LoanSalaryRecoveryListDetailTO();
                                    objSalaryRecoveryListDetailTO.setIntCalcUptoDt(intUptoDt);
                                    objSalaryRecoveryListDetailTO.setEmpRefNo(CommonUtil.convertObjToStr(recoveryMap.get("EMP_REFNO")));
                                    objSalaryRecoveryListDetailTO.setMemberName(CommonUtil.convertObjToStr(recoveryMap.get("MEM_NAME")));
                                    objSalaryRecoveryListDetailTO.setSchemeName(CommonUtil.convertObjToStr(recoveryMap.get("PROD_DESC")));
                                    objSalaryRecoveryListDetailTO.setActNum(CommonUtil.convertObjToStr(recoveryMap.get("ACT_NUM")));
                                    String tot_demand = String.valueOf(df.format(CommonUtil.convertObjToDouble(singleRowMap.get("TOTAL_DEMAND"))));
                                    objSalaryRecoveryListDetailTO.setTotalDemand(Double.parseDouble(tot_demand));
                                    String principal = String.valueOf(df.format(CommonUtil.convertObjToDouble(singleRowMap.get("PRINCIPAL"))));
                                    objSalaryRecoveryListDetailTO.setPrincipal(Double.parseDouble(principal));
                                    String intrest = String.valueOf(df.format(CommonUtil.convertObjToDouble(singleRowMap.get("INTEREST"))));
                                    objSalaryRecoveryListDetailTO.setInterest(Double.parseDouble(intrest));
                                    String penal = String.valueOf(df.format(CommonUtil.convertObjToDouble(singleRowMap.get("PENAL"))));
                                    objSalaryRecoveryListDetailTO.setPenal(Double.parseDouble(penal));
                                    String charge = String.valueOf(df.format(CommonUtil.convertObjToDouble(singleRowMap.get("CHARGES"))));
                                    objSalaryRecoveryListDetailTO.setCharges(Double.parseDouble(charge));
                                    String mdsBonus = String.valueOf(df.format(CommonUtil.convertObjToDouble(singleRowMap.get("BONUS"))));
                                    objSalaryRecoveryListDetailTO.setMdsBonus(Double.parseDouble(mdsBonus));
                                    String forfeit = String.valueOf(df.format(CommonUtil.convertObjToDouble(singleRowMap.get("FORFEIT"))));
                                    objSalaryRecoveryListDetailTO.setForfeitBonus(Double.parseDouble(forfeit));
                                    objSalaryRecoveryListDetailTO.setInstNo(CommonUtil.convertObjToInt(key1));
                                    objSalaryRecoveryListDetailTO.setProd_ID(prodID);
                                    objSalaryRecoveryListDetailTO.setProd_Type(prodType);
                                    objSalaryRecoveryListDetailTO.setStatus(CommonConstants.STATUS_CREATED);
                                    objSalaryRecoveryListDetailTO.setRecoveryId(recoveryId);
                                    // End
                                    sqlMap.executeUpdate("insertOtherSalaryRecoveryListDetailTO", objSalaryRecoveryListDetailTO);
                                    //System.out.println("custDataMap here :: acctnum :: "+ actNum +"--::"+ custDataMap);
                                    if (custDataMap.containsKey(recoveryMap.get("EMP_REFNO"))) {
                                        //System.out.println("exist record !!!");
                                        totalCustAmt = CommonUtil.convertObjToDouble(custDataMap.get(recoveryMap.get("EMP_REFNO")));
                                        //System.out.println("totalCustAmt inside exists ::" + totalCustAmt);
                                        custDataMap.put(recoveryMap.get("EMP_REFNO"), totalCustAmt + totaldemand);
                                        //System.out.println("custDataMap after inside exists :: acctnum :: "+ actNum +"--::"+ custDataMap);
                                    } else {
                                        custDataMap.put(recoveryMap.get("EMP_REFNO"), totaldemand);
                                    }
                                }
                            }
                        }

                    }
                    
                }
                    // End
               System.out.println("recoveryList after loan and mds :: " + recoveryList);
               if (prodType.equals("TD")) {
                 System.out.println("Execute here for RD");
                 Date checkDt = CommonUtil.getProperDate(currDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj.get("CALC_INT_UPTO_DT"))));
                 rdRecoveryMap = calcRDPayments(actNum, prodID, prodType,checkDt);
                 System.out.println("datamap RD :: " + rdRecoveryMap);
                 if (rdRecoveryMap != null && rdRecoveryMap.size() > 0) {
                        Iterator iterate = rdRecoveryMap.entrySet().iterator();
                        if (null != rdRecoveryMap && rdRecoveryMap.size() > 0) {
                            ArrayList list = new ArrayList();
                            while (iterate.hasNext()) {
                                Map.Entry entry = (Map.Entry) iterate.next();
                                Object key1 = (Object) entry.getKey();
                                Object value = (Object) entry.getValue();
                                singleRowMap = (HashMap) value;
                                double totaldemand = CommonUtil.convertObjToDouble(singleRowMap.get("TOTAL_DEMAND"));
                                if (totaldemand > 0) {
                                    recoveryRowList = new ArrayList();
                                    recoveryRowList.add(String.valueOf(sno++));
                                    recoveryRowList.add(CommonUtil.convertObjToStr(recoveryMap.get("EMP_REFNO")));
                                    recoveryRowList.add(CommonUtil.convertObjToStr(recoveryMap.get("MEMBER_NO")));
                                    recoveryRowList.add(CommonUtil.convertObjToStr(recoveryMap.get("MEM_NAME")));
                                    recoveryRowList.add(CommonUtil.convertObjToStr(recoveryMap.get("PROD_DESC")));
                                    recoveryRowList.add(CommonUtil.convertObjToStr(recoveryMap.get("ACT_NUM")));
                                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("TOTAL_DEMAND")));
                                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("PRINCIPAL")));
                                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("INTEREST")));
                                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("PENAL")));
                                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("CHARGES")));
                                    recoveryRowList.add("0.0");
                                    recoveryRowList.add("0.0");
                                    recoveryRowList.add(CommonUtil.convertObjToStr(singleRowMap.get("CLEAR_BALANCE")));
                                    recoveryRowList.add(CommonUtil.convertObjToStr(recoveryMap.get("INST_BRANCH_NAME")));
                                    recoveryList.add(recoveryRowList);
                                    //Insert Details Data
                                    LoanSalaryRecoveryListDetailTO objSalaryRecoveryListDetailTO = new LoanSalaryRecoveryListDetailTO();
                                    objSalaryRecoveryListDetailTO.setIntCalcUptoDt(intUptoDt);
                                    objSalaryRecoveryListDetailTO.setEmpRefNo(CommonUtil.convertObjToStr(recoveryMap.get("EMP_REFNO")));
                                    objSalaryRecoveryListDetailTO.setMemberName(CommonUtil.convertObjToStr(recoveryMap.get("MEM_NAME")));
                                    objSalaryRecoveryListDetailTO.setSchemeName(CommonUtil.convertObjToStr(recoveryMap.get("PROD_DESC")));
                                    objSalaryRecoveryListDetailTO.setActNum(CommonUtil.convertObjToStr(recoveryMap.get("ACT_NUM")));
                                    String tot_demand = String.valueOf(df.format(CommonUtil.convertObjToDouble(singleRowMap.get("TOTAL_DEMAND"))));
                                    objSalaryRecoveryListDetailTO.setTotalDemand(Double.parseDouble(tot_demand));
                                    String principal = String.valueOf(df.format(CommonUtil.convertObjToDouble(singleRowMap.get("PRINCIPAL"))));
                                    objSalaryRecoveryListDetailTO.setPrincipal(Double.parseDouble(principal));
                                    String intrest = String.valueOf(df.format(CommonUtil.convertObjToDouble(singleRowMap.get("INTEREST"))));
                                    objSalaryRecoveryListDetailTO.setInterest(Double.parseDouble(intrest));
                                    String penal = String.valueOf(df.format(CommonUtil.convertObjToDouble(singleRowMap.get("PENAL"))));
                                    objSalaryRecoveryListDetailTO.setPenal(Double.parseDouble(penal));
                                    String charge = String.valueOf(df.format(CommonUtil.convertObjToDouble(singleRowMap.get("CHARGES"))));
                                    objSalaryRecoveryListDetailTO.setCharges(Double.parseDouble(charge));                                    
                                    objSalaryRecoveryListDetailTO.setMdsBonus(new Double(0.0));
                                    objSalaryRecoveryListDetailTO.setForfeitBonus(new Double(0.0));
                                    objSalaryRecoveryListDetailTO.setInstNo(CommonUtil.convertObjToInt(key1));
                                    objSalaryRecoveryListDetailTO.setProd_ID(prodID);
                                    objSalaryRecoveryListDetailTO.setProd_Type(prodType);
                                    objSalaryRecoveryListDetailTO.setStatus(CommonConstants.STATUS_CREATED);
                                    objSalaryRecoveryListDetailTO.setRecoveryId(recoveryId);
                                    // End
                                    sqlMap.executeUpdate("insertOtherSalaryRecoveryListDetailTO", objSalaryRecoveryListDetailTO);                                 
                                    if (custDataMap.containsKey(recoveryMap.get("EMP_REFNO"))) {
                                        //System.out.println("exist record !!!");
                                        totalCustAmt = CommonUtil.convertObjToDouble(custDataMap.get(recoveryMap.get("EMP_REFNO")));
                                        //System.out.println("totalCustAmt inside exists ::" + totalCustAmt);
                                        custDataMap.put(recoveryMap.get("EMP_REFNO"), totalCustAmt + totaldemand);
                                        //System.out.println("custDataMap after inside exists :: acctnum :: "+ actNum +"--::"+ custDataMap);
                                    } else {
                                        custDataMap.put(recoveryMap.get("EMP_REFNO"), totaldemand);
                                    }
                                }
                            }
                        }
                    }
                 }

                     System.out.println("recoveryList after RD :: " + recoveryList);
                }
                insertRecoveryMasterData(obj);
                insertRecoveryCustomerData(obj, custDataMap);
                sqlMap.commitTransaction();
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw e;
            }
            recoveryListMap.put("RECOVERY_LIST_TABLE_DATA", recoveryList);
        }
        return recoveryListMap;
    }

    public HashMap calcLoanPayments(String actNum, String prodId, String prodType, String recoveryYesNo) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put("ACT_NUM", actNum);
        try {
            List parameterList = sqlMap.executeQueryForList("getRecoveryParameter", whereMap);
            String clear_balance = "";
            boolean flag = false;
            double chargeAmt = 0.0;
            if (parameterList != null && parameterList.size() > 0) {
                int firstDay = 0;
                double clearBal = 0.0;
                java.util.Date repayDate = null;
                java.util.Date inst_dt = null;
                java.util.Date checkDate = (java.util.Date) intUptoDt.clone();
                Date checkingDate = (Date) intUptoDt.clone();
                java.util.Date sanctionDt = null;
                String EMIINSIMPLEINTREST = "";
                String MORATORIUMGIVEN = "";
                String INSTALLTYPE = "0";
                whereMap = (HashMap) parameterList.get(0);
                firstDay = CommonUtil.convertObjToInt(whereMap.get("FIRST_DAY"));
                checkingDate.setDate(firstDay);
                repayDate = (java.util.Date) whereMap.get("REPAYMENT_DT");
                sanctionDt = (java.util.Date) whereMap.get("FROM_DT");
                long dtDiff = DateUtil.dateDiff(sanctionDt, checkingDate);
                if ((double) dtDiff < 0.0D) {
                    dataMap = null;
                } else {
                    EMIINSIMPLEINTREST = CommonUtil.convertObjToStr(whereMap.get("EMI_IN_SIMPLEINTREST"));
                    MORATORIUMGIVEN = CommonUtil.convertObjToStr(whereMap.get("MORATORIUM_GIVEN"));
                    INSTALLTYPE = CommonUtil.convertObjToStr(whereMap.get("INSTALL_TYPE"));
                    long diffDay = DateUtil.dateDiff(sanctionDt, checkDate);
                    long diffRepayDay = DateUtil.dateDiff(repayDate, checkDate);
                    String behavesLike = CommonUtil.convertObjToStr(whereMap.get("FACILITY_TYPE"));
                    if (/*
                             * INSTALLTYPE.equals("UNIFORM_PRINCIPLE_EMI") &&
                             */behavesLike != null && !behavesLike.equals("") && behavesLike.length() > 0 && !behavesLike.equals("OD") && diffRepayDay < 0 && MORATORIUMGIVEN.equals("N")) {
                        dataMap = null;
                    } else {
                        if ((double) diffDay >= 0.0D) {
                            if (recoveryYesNo.equals("N")) {
                                HashMap asAndWhenMap = interestCalculationTLAD(actNum, prodId, prodType);
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
                                        clearBal = CommonUtil.convertObjToDouble(mapop.get("CLEAR_BALANCE")) * -1;
                                    }
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
                                        totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
                                        paidAmount = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
                                        if (asAndWhenMap == null || asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && asAndWhenMap.get("AS_CUSTOMER_COMES") != null && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("N")) {
                                            paidAmt = sqlMap.executeQueryForList("getIntDetails", loanInstall);
                                            if (paidAmt != null && paidAmt.size() > 0) {
                                                allInstallmentMap = (HashMap) paidAmt.get(0);
                                            }
                                            double totExcessAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
                                            totPrinciple += totExcessAmt;
                                        }
                                        List lst = sqlMap.executeQueryForList("getAllLoanInstallment", loanInstall);
                                        int i = 0;
                                        do {
                                            if (i >= lst.size()) {
                                                break;
                                            }
                                            allInstallmentMap = (HashMap) lst.get(i);
                                            instalAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                                            if (instalAmt <= totPrinciple) {
                                                totPrinciple -= instalAmt;
                                                inst_dt = new java.util.Date();
                                                String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                                                inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                                            } else {
                                                inst_dt = new java.util.Date();
                                                String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                                                inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                                                if (DateUtil.dateDiff(checkDate, intUptoDt) >= 0L && moratorium != null && moratorium.equals("Y")) {
                                                    if (DateUtil.dateDiff(repayDate, intUptoDt) < 0) {
                                                        totPrinciple = 0.0D;
                                                        flag = true;
                                                    } else {
                                                        totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue() - totPrinciple;
                                                        if (totPrinciple > clearBal) {
                                                            totPrinciple = clearBal;
                                                        }
                                                    }
                                                } else {
                                                    totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue() - totPrinciple;
                                                }
                                                break;
                                            }
                                            i++;
                                        } while (true);
                                    } else {
                                        List paidAmtemi = sqlMap.executeQueryForList("getPaidPrincipleEMI1", loanInstall);
                                        allInstallmentMap = (HashMap) paidAmtemi.get(0);
                                        totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
                                        paidAmount = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
                                        if (asAndWhenMap == null || asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && asAndWhenMap.get("AS_CUSTOMER_COMES") != null && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("N")) {
                                            paidAmtemi = sqlMap.executeQueryForList("getIntDetails", loanInstall);
                                            if (paidAmtemi != null && paidAmtemi.size() > 0) {
                                                allInstallmentMap = (HashMap) paidAmtemi.get(0);
                                            }
                                            double totExcessAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
                                            totPrinciple += totExcessAmt;
                                        }
                                        List lst = sqlMap.executeQueryForList("getAllLoanInstallment", loanInstall);
                                        int i = 0;
                                        do {
                                            if (i >= lst.size()) {
                                                break;
                                            }
                                            allInstallmentMap = (HashMap) lst.get(i);
                                            instalAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                                            if (instalAmt <= totPrinciple) {
                                                totPrinciple -= instalAmt;
                                                inst_dt = new java.util.Date();
                                                String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                                                inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                                            } else {
                                                inst_dt = new java.util.Date();
                                                String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                                                inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                                                totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("TOTAL_AMT")).doubleValue() - totPrinciple;
                                                break;
                                            }
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
                                    if (DateUtil.dateDiff(inst_dt, intUptoDt) <= 0L && moret != null && moret.equals("Y")) {
                                        dataMap.put("INSTMT_AMT", Integer.valueOf(0));
                                        dataMap.put("PRINCIPAL", String.valueOf("0"));
                                    }
                                    java.util.Date addDt = (java.util.Date) intUptoDt.clone();
                                    java.util.Date instDt = DateUtil.addDays(inst_dt, 1);
                                    addDt.setDate(instDt.getDate());
                                    addDt.setMonth(instDt.getMonth());
                                    addDt.setYear(instDt.getYear());
                                    loanInstall.put("FROM_DATE", addDt);
                                    loanInstall.put("TO_DATE", interestUptoDt);
                                    List lst1 = null;
                                    if (inst_dt != null && totPrinciple > 0.0D) {
                                        lst1 = sqlMap.executeQueryForList("getTotalAmountOverDue", loanInstall);
                                    }
                                    double principle = 0.0D;
                                    if (!installtype.equals("UNIFORM_PRINCIPLE_EMI") || !emi_uniform.equals("Y")) {
                                        if (lst1 != null && lst1.size() > 0) {
                                            HashMap map = (HashMap) lst1.get(0);
                                            principle = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue();
                                        }
                                        totPrinciple += principle;
                                    } else if (lst1 != null && lst1.size() > 0) {
                                        HashMap map = (HashMap) lst1.get(0);
                                        principle = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue() /*
                                                 * + CommonUtil.convertObjToDouble(map.get("INTEREST_AMOUNT")).doubleValue()
                                                 */;
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
                                            totPrinciple += principle;
                                        }
                                    } else {
                                        totPrinciple -= CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
                                    }
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
                                            HashMap hash1 = (HashMap) facilitylst.get(0);
                                            if (hash.get("CLEAR_BALANCE") != null) {
                                                clear_balance = hash.get("CLEAR_BALANCE").toString();
                                            }
                                            hash.put("ACT_NUM", loanInstall.get("ACT_NUM"));
                                            hash1.put("ACT_NUM", loanInstall.get("ACT_NUM"));
                                            if (asAndWhenMap.containsKey("PREMATURE")) {
                                                insertPenal.put("PREMATURE", asAndWhenMap.get("PREMATURE"));
                                            }
                                            if (asAndWhenMap.containsKey("PREMATURE") && asAndWhenMap.containsKey("PREMATURE_INT_CALC_AMT") && CommonUtil.convertObjToStr(asAndWhenMap.get("PREMATURE_INT_CALC_AMT")).equals("LOANSANCTIONAMT")) {
                                                hash.put("FROM_DT", hash.get("ACCT_OPEN_DT"));
                                                hash1.put("FROM_DT", hash1.get("ACCT_OPEN_DT"));
                                            } else {
                                                hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                                                hash.put("FROM_DT", DateUtil.addDays((java.util.Date) hash.get("FROM_DT"), 2));
                                                hash1.put("FROM_DT", hash1.get("LAST_INT_CALC_DT"));
                                                Date dtt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hash1.get("LAST_INT_CALC_DT")));
                                                if (moret != null && moret.equals("Y")) {
                                                    hash1.put("FROM_DT", CommonUtil.getProperDate(currDt, DateUtil.addDays(dtt, 2)));
                                                } else {
                                                    hash1.put("FROM_DT", CommonUtil.getProperDate(currDt, DateUtil.addDays(dtt, -1)));
                                                }
                                            }
                                            hash.put("TO_DATE", interestUptoDt.clone());
                                            hash.put("SALARY_RECOVERY", "SALARY_RECOVERY");
                                            hash1.put("TO_DATE", interestUptoDt.clone());
                                            hash1.put("SALARY_RECOVERY", "SALARY_RECOVERY");
                                            if (asAndWhenMap == null || !asAndWhenMap.containsKey("INSTALL_TYPE") || asAndWhenMap.get("INSTALL_TYPE") == null || !asAndWhenMap.get("INSTALL_TYPE").equals("EMI")) {
                                                facilitylst = sqlMap.executeQueryForList("getPaidPrinciple", hash1);
                                                hash.remove("SALARY_RECOVERY");
                                                hash1.remove("SALARY_RECOVERY");
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
                                        insertPenal.put("INTEREST", rd.getNearestHigher(CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue(), 1));
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
                                                pBal -= excess;
                                                if (pBal < totPrinciple) {
                                                    insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(pBal));
                                                }
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
                                            }
                                        }
                                        getIntDetails = sqlMap.executeQueryForList("getPenalIntDetails", loanInstall);
                                        hash = (HashMap) getIntDetails.get(0);
                                        insertPenal.put("PENAL_INT", hash.get("PIBAL"));
                                    }
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
                                        insertPenal.put("INTEREST", rd.getNearestHigher(CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue(), 1));
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
                                        if (diffDayPending > 0L && pBalance > 0.0D) {
                                            insertPenal.put("PRINCIPLE_BAL", new Double(pBalance));
                                        }
                                    }
                                }
                                chargeAmt = 0.0D;
                                whereMap = new HashMap();
                                whereMap.put("ACT_NUM", actNum);
                                chargeAmt = getChargeAmount(whereMap, prodType);
                                if (chargeAmt > 0.0D) {
                                    dataMap.put("CHARGES", String.valueOf(chargeAmt));
                                } else {
                                    dataMap.put("CHARGES", "0");
                                }
                                double totalDemand = 0.0D;
                                double principalAmount = 0.0D;
                                if (insertPenal.containsKey("CURR_MONTH_PRINCEPLE")) {
                                    principalAmount = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_PRINCEPLE")).doubleValue();
                                    totalDemand = rd.getNearestHigher(CommonUtil.convertObjToDouble(insertPenal.get("INTEREST")).doubleValue(), 1) + CommonUtil.convertObjToDouble(insertPenal.get("LOAN_CLOSING_PENAL_INT")).doubleValue() + chargeAmt;
                                } else {
                                    principalAmount = CommonUtil.convertObjToDouble(insertPenal.get("PRINCIPLE_BAL")).doubleValue();
                                    totalDemand = rd.getNearestHigher(CommonUtil.convertObjToDouble(insertPenal.get("INTEREST")).doubleValue(), 1) + CommonUtil.convertObjToDouble(insertPenal.get("LOAN_CLOSING_PENAL_INT")).doubleValue() + chargeAmt;
                                }
                                if (inst_dt != null && prodType.equals("TL")) {
                                    if (DateUtil.dateDiff(intUptoDt, inst_dt) <= 0L) {
                                        if (installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y") && moratorium.equals("N")) {
                                            principalAmount -= CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue();
                                            if (principalAmount > 0) {
                                                dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
                                            } else {
                                                dataMap.put("PRINCIPAL", "0");
                                            }
                                        } else {
                                            if (principalAmount > 0) {
                                                dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
                                            } else {
                                                dataMap.put("PRINCIPAL", "0");
                                            }
                                        }
                                    } else {
                                        dataMap.put("PRINCIPAL", "0");
                                        principalAmount = 0.0;
                                    }
                                    if (principalAmount == 0.0 && (!installtype.equals("UNIFORM_PRINCIPLE_EMI") || !emi_uniform.equals("Y")) || installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y")) {
                                        HashMap balanceMap = new HashMap();
                                        double balanceLoanAmt = 0.0D;
                                        double finalDemandAmt = 0.0D;
                                        balanceMap.put("ACCOUNTNO", actNum);
                                        List balannceAmtLst = sqlMap.executeQueryForList("getBalanceLoanPrincipalAmt", balanceMap);
                                        if (balannceAmtLst != null && balannceAmtLst.size() > 0) {
                                            balanceMap = (HashMap) balannceAmtLst.get(0);
                                            balanceLoanAmt = CommonUtil.convertObjToDouble(balanceMap.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();
                                            double checkAmt = 0.0D;
                                            double totalPrincAmount = 0.0D;
                                            checkAmt = balanceLoanAmt - instalAmt;
                                            if (checkAmt > 0.0D) {
                                                if (!installtype.equals("UNIFORM_PRINCIPLE_EMI") || !emi_uniform.equals("Y")) {
                                                    balanceMap.put("ACCT_NUM", actNum);
                                                    //balanceMap.put("BALANCE_AMT", String.valueOf(checkAmt));
                                                    balanceMap.put("BALANCE_AMT", checkAmt);
                                                    List sumInstLst = sqlMap.executeQueryForList("getPrincipalAmtGreaterThanBalAmt", balanceMap);
                                                    if (sumInstLst != null && sumInstLst.size() > 0) {
                                                        balanceMap = (HashMap) sumInstLst.get(0);
                                                        totalPrincAmount = CommonUtil.convertObjToDouble(balanceMap.get("PRINCIPAL_AMOUNT")).doubleValue();
                                                        totalPrincAmount += instalAmt;
                                                        finalDemandAmt = totalPrincAmount - paidAmount;
                                                        if (balanceLoanAmt > finalDemandAmt) {
                                                            if (finalDemandAmt > 0) {
                                                                dataMap.put("PRINCIPAL", String.valueOf(finalDemandAmt));
                                                            } else {
                                                                dataMap.put("PRINCIPAL", "0");
                                                            }
                                                            principalAmount = finalDemandAmt;
                                                        } else {
                                                            if (balanceLoanAmt > 0) {
                                                                dataMap.put("PRINCIPAL", String.valueOf(balanceLoanAmt));
                                                            } else {
                                                                dataMap.put("PRINCIPAL", "0");
                                                            }
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
                                                    if (DateUtil.dateDiff(inst_dt, intUptoDt) <= 0L && moret != null && moret.equals("Y")) {
                                                        finalDemandAmt = 0.0D;
                                                        principalAmount = 0.0D;
                                                    }
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
                                                        if (DateUtil.dateDiff(intUptoDt, expiry_dt) >= 0L) {
                                                            principalAmount = 0.0D;
                                                            dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
                                                        } else {
                                                            if (balanceLoanAmt > 0) {
                                                                dataMap.put("PRINCIPAL", String.valueOf(balanceLoanAmt));
                                                            } else {
                                                                dataMap.put("PRINCIPAL", "0");
                                                            }
                                                            principalAmount = balanceLoanAmt;
                                                        }
                                                    } else {
                                                        if (balanceLoanAmt > 0) {
                                                            dataMap.put("PRINCIPAL", String.valueOf(balanceLoanAmt));
                                                        } else {
                                                            dataMap.put("PRINCIPAL", "0");
                                                        }
                                                        principalAmount = balanceLoanAmt;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if (prodType.equals("AD")) {
                                    if (principalAmount > 0.0D) {
                                        dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
                                    } else {
                                        dataMap.put("PRINCIPAL", "0");
                                    }
                                }
                                if (installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y")) {
                                    double dueamount = 0.0D;
                                    double penal = 0.0D;
                                    double totEmi = 0.0D;
                                    double paidEmi = 0.0D;
                                    double principle = 0.0D;
                                    double interst = 0.0D;
                                    if (insertPenal.containsKey("CURR_MONTH_INT")) {
                                        interst = Double.parseDouble(insertPenal.get("CURR_MONTH_INT").toString());
                                    }
                                    HashMap emi = new HashMap();
                                    java.util.Date upto = (java.util.Date) intUptoDt.clone();
                                    emi.put("ACC_NUM", actNum);
                                    emi.put("UP_TO", upto);
                                    List totalEmiList = sqlMap.executeQueryForList("TotalEmi", emi);
                                    if (totalEmiList != null && totalEmiList.size() > 0 && ((HashMap) totalEmiList.get(0)).get("TOTAL_AMOUNT") != null) {
                                        HashMap aMap = new HashMap();
                                        aMap = (HashMap) totalEmiList.get(0);
                                        totEmi = Double.parseDouble(aMap.get("TOTAL_AMOUNT").toString());
                                    } else {
                                        totEmi = 0.0D;
                                    }
                                    HashMap paid = new HashMap();
                                    paid.put("ACT_NUM", actNum);
                                    paid.put("BRANCH_CODE", _branchCode);
                                    List paidAmtemi = sqlMap.executeQueryForList("getPaidPrincipleEMI", loanInstall);
                                    if (paidAmtemi != null && paidAmtemi.size() > 0 && ((HashMap) paidAmtemi.get(0)).get("PRINCIPLE") != null) {
                                        paid = (HashMap) paidAmtemi.get(0);
                                        paidEmi = CommonUtil.convertObjToDouble(paid.get("PRINCIPLE")).doubleValue();
                                    } else {
                                        paidEmi = 0.0D;
                                    }
                                    dueamount = totEmi - paidEmi;
                                    double paidamount = paidEmi;
                                    if (dueamount <= 0.0D) {
                                        dueamount = CommonUtil.convertObjToDouble(dataMap.get("PRINCIPAL")).doubleValue() + interst;
                                    }
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
                                            double scheduledEmi = Double.parseDouble(eachInstall.get("TOTAL_AMT").toString());
                                            if (paidamount >= scheduledEmi) {
                                                paidamount -= scheduledEmi;
                                            } else {
                                                String in_date = CommonUtil.convertObjToStr(eachInstall.get("INSTALLMENT_DT"));
                                                penalStrats = DateUtil.getDateMMDDYYYY(in_date);
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
                                        if (getPenalData != null && getPenalData.size() > 0) {
                                            for (k = 0; k < getPenalData.size(); k++) {
                                                HashMap amap = new HashMap();
                                                amap = (HashMap) getPenalData.get(k);
                                                String in_date = CommonUtil.convertObjToStr(amap.get("INSTALLMENT_DT"));
                                                java.util.Date currntDate = DateUtil.getDateMMDDYYYY(in_date);
                                                currntDate = DateUtil.addDays(currntDate, graceint);
                                                HashMap holidayMap = new HashMap();
                                                holidayMap.put("CURR_DATE", currntDate);
                                                holidayMap.put("BRANCH_CODE", _branchCode);
                                                currntDate = CommonUtil.getProperDate(currDt, currntDate);
                                                holidayMap = new HashMap();
                                                boolean checkHoliday = true;
                                                String str = "any next working day";
                                                currntDate = CommonUtil.getProperDate(currDt, currntDate);
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
                                                    } else {
                                                        checkHoliday = false;
                                                    }
                                                }
                                                long difference = DateUtil.dateDiff(currntDate, upto) - 1L;
                                                if (difference < 0L) {
                                                    difference = 0L;
                                                }
                                                double installment = Double.parseDouble(amap.get("TOTAL_AMT").toString());
                                                penal += (installment * (double) difference * interstPenal) / 36500D;
                                            }
                                        }
                                    }
                                    penal = rd.getNearestHigher(penal, 1);
                                    interst = rd.getNearestHigher(interst, 1);
                                    principle = dueamount - interst;
                                    if (principle > 0) {
                                        totalDemand = principle + penal + interst;
                                    } else {
                                        totalDemand = penal + interst;
                                    }
                                    dataMap.put("INTEREST", interst);
                                    dataMap.put("PENAL", penal);
                                    if (principle > 0) {
                                        dataMap.put("PRINCIPAL", principle);
                                    }
                                    HashMap detailedMap = new HashMap();
                                    detailedMap.put("ACCT_NUM", actNum);
                                    detailedMap.put("ASONDT", (Date) intUptoDt.clone());
                                    detailedMap.put("INSTALL_TYPE", installtype);
                                    detailedMap.put("EMI_IN_SIMPLE_INTEREST", emi_uniform);
                                    List advanceEmiList = sqlMap.executeQueryForList("getAdvAmtEmi", detailedMap);
                                    if (advanceEmiList != null && advanceEmiList.size() > 0) {
                                        HashMap advMap = (HashMap) advanceEmiList.get(0);
                                        if (advMap != null && advMap.size() > 0 && advMap.containsKey("BALANCE")) {
                                            double balance = CommonUtil.convertObjToDouble(advMap.get("BALANCE"));
                                            if (balance <= 0) {
                                                double interest = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT"));
                                                double insAmt = CommonUtil.convertObjToDouble(dataMap.get("INSTMT_AMT"));
                                                double princi = insAmt - interest;
                                                double princip = CommonUtil.convertObjToDouble(dataMap.get("PRINCIPAL"));
                                                if (princi > 0) {
                                                    if (princip > 0) {
                                                        totalDemand -= princip;
                                                    }
                                                    totalDemand += princi;
                                                    dataMap.put("PRINCIPAL", princi);
                                                }
                                            }
                                        }
                                    }

                                    double insttAmt = CommonUtil.convertObjToDouble(dataMap.get("INSTMT_AMT"));
                                    double tempPrin = CommonUtil.convertObjToDouble(dataMap.get("PRINCIPAL"));
                                    double tempInt = CommonUtil.convertObjToDouble(dataMap.get("INTEREST"));

                                    dataMap.put("CLEAR_BALANCE", clear_balance);
                                    double temmmpPrinc = insttAmt - tempInt;
                                    if (tempPrin < temmmpPrinc) {
                                        totalDemand -= tempPrin;
                                        totalDemand += temmmpPrinc;
                                        dataMap.put("PRINCIPAL", temmmpPrinc);
                                    } else {
                                    }
                                    double princHere = CommonUtil.convertObjToDouble(dataMap.get("PRINCIPAL"));
                                    if (princHere > clearBal) {
                                        totalDemand -= princHere;
                                        totalDemand += clearBal;
                                        dataMap.put("PRINCIPAL", clearBal);
                                    }
                                    if (totalDemand > 0) {
                                        dataMap.put("TOTAL_DEMAND", totalDemand);
                                    } else {
                                        dataMap.put("TOTAL_DEMAND", "0");
                                        if (prodType != null && !prodType.equals("TL")) {
                                            dataMap = null;
                                            return dataMap;
                                        }
                                    }
                                } else {
                                    dataMap.put("CLEAR_BALANCE", clear_balance);
                                    totalDemand += principalAmount;
                                    double princHere = CommonUtil.convertObjToDouble(dataMap.get("PRINCIPAL"));
                                    if (princHere > clearBal) {
                                        totalDemand -= princHere;
                                        totalDemand += clearBal;
                                        dataMap.put("PRINCIPAL", clearBal);
                                    }
                                    dataMap.put("INTEREST", rd.getNearestHigher(Double.parseDouble(insertPenal.get("INTEREST").toString()), 1));
                                    if (insertPenal.get("LOAN_CLOSING_PENAL_INT") != null) {
                                        dataMap.put("PENAL", insertPenal.get("LOAN_CLOSING_PENAL_INT"));
                                    } else {
                                        dataMap.put("PENAL", 0);
                                    }
                                    if (totalDemand > 0) {
                                        dataMap.put("TOTAL_DEMAND", totalDemand);
                                    } else {
                                        dataMap.put("TOTAL_DEMAND", "0");
                                        if (prodType != null && !prodType.equals("TL")) {
                                            dataMap = null;
                                            return dataMap;
                                        }
                                    }
                                }
                                if (flag) {
                                    dataMap.put("PRINCIPAL", Integer.valueOf(0));
                                    totalDemand -= principalAmount;
                                    if (totalDemand > 0) {
                                        dataMap.put("TOTAL_DEMAND", totalDemand);
                                    } else {
                                        if (prodType != null && !prodType.equals("TL")) {
                                            dataMap.put("TOTAL_DEMAND", "0");
                                            dataMap = null;
                                            return dataMap;
                                        }
                                    }
                                    flag = false;
                                }
                            }
                            if (prodType != null && !prodType.equals("") && prodType.equals("TL")) {
                                HashMap hmap = new HashMap();
                                hmap.put("ACT_NUM", CommonUtil.convertObjToStr(actNum));
                                hmap.put("TRANS_DT", getProperDateFormat(intUptoDt.clone()));
                                hmap.put("REC_TYPE", "Recovery");
                                List resltList = sqlMap.executeQueryForList("getPenalIntDetailsTL", hmap);
                                double intVal = 0, princVal = 0;
                                if (resltList != null && resltList.size() > 0) {
                                    HashMap ansMap = (HashMap) resltList.get(0);
                                    if (ansMap != null && ansMap.containsKey("RESULT")) {
                                        String ans = CommonUtil.convertObjToStr(ansMap.get("RESULT"));
                                        if (ans != null && ans.length() > 0) {
                                            String[] ansArr = ans.split(":");
                                            String intStr = "0", princStr = "0";
                                            if (ansArr.length > 5) {
                                                if (ansArr[5].contains("=")) {
                                                    String[] splArr = ansArr[5].split("=");
                                                    if (splArr.length > 1) {
                                                        intStr = splArr[1].trim();
                                                    }
                                                }
                                            }
                                            if (ansArr.length > 4) {
                                                if (ansArr[4].contains("=")) {
                                                    String[] splArr = ansArr[4].split("=");
                                                    if (splArr.length > 1) {
                                                        princStr = splArr[1].trim();
                                                    }
                                                }
                                            }
                                            intVal = CommonUtil.convertObjToDouble(intStr);
                                            princVal = CommonUtil.convertObjToDouble(princStr);
                                            if (resltList != null && resltList.size() > 0) {
                                                double penalVal = CommonUtil.convertObjToDouble(dataMap.get("PENAL"));
                                                double demandVal = 0;
                                                if (princVal >= 0) {
                                                    demandVal = princVal + penalVal + intVal + chargeAmt;
                                                } else {
                                                    demandVal = penalVal + intVal + chargeAmt;
                                                }
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
                                            }
                                        }
                                    }
                                }
                            }

                        } else {
                            dataMap = null;
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
//        System.out.println("DATA MAP FINAL ####:" + dataMap);
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
            if (lst != null && lst.size() > 0) {
                hash = (HashMap) lst.get(0);
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
                hash = new SelectAllDAO().executeQuery(dataMap);
                if (hash == null) {
                    hash = new HashMap();
                }
                if (hash.containsKey("DATA") && hash.get("DATA") != null) {
                    hash.putAll((HashMap) ((List) hash.get("DATA")).get(0));
                }
                hash.putAll((HashMap) dataMap.get(CommonConstants.MAP_WHERE));
//                System.out.println("hashinterestoutput###" + hash);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }
 private void insertRecoveryMasterData(HashMap map) throws Exception {
        try {
            if (map.containsKey("CALC_INT_UPTO_DT")) {
                intUptoDt = (Date) map.get("CALC_INT_UPTO_DT");
                LoanSalaryRecoveryListMasterTO objSalaryRecoveryListMasterTO = new LoanSalaryRecoveryListMasterTO();
                objSalaryRecoveryListMasterTO.setIntCalcUptoDt(intUptoDt);
                objSalaryRecoveryListMasterTO.setCreatedBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                objSalaryRecoveryListMasterTO.setInstId(CommonUtil.convertObjToStr(map.get("INST_ID")));
                HashMap listGenerationMap = new HashMap();
                listGenerationMap.put("RECOVERY_ID", recoveryId);
                listGenerationMap.put("CALC_INT_UPTO_DT", map.get("CALC_INT_UPTO_DT"));
                listGenerationMap.put("INST_ID", map.get("INST_ID"));
                List listGeneration = sqlMap.executeQueryForList("getSelectLoanRecoveryListGenerationProcessed", listGenerationMap);
                if (listGeneration != null && listGeneration.size() > 0) {
                    objSalaryRecoveryListMasterTO.setRecoveryId(CommonUtil.convertObjToStr(map.get("RECOVERY_ID")));
                    sqlMap.executeUpdate("updateLoanRecoveryListGeneration", objSalaryRecoveryListMasterTO);
                }else{
                    objSalaryRecoveryListMasterTO.setCreatedDt((Date)currDt.clone());
                    objSalaryRecoveryListMasterTO.setStatus("PROCESSED");
                    if(map.containsKey("RECOVERY_ID")&&map.get("RECOVERY_ID")!=null){
                    objSalaryRecoveryListMasterTO.setRecoveryId(CommonUtil.convertObjToStr(map.get("RECOVERY_ID")));}
                    sqlMap.executeUpdate("insertLoanRecoveryListGeneration", objSalaryRecoveryListMasterTO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // sqlMap.rollbackTransaction();
        }
    }
    
    private void insertRecoveryCustomerData(HashMap map, HashMap custDataMap) throws Exception {
        try {
            System.out.println("custDataMap inside insertRecoveryCustomerData :: " + custDataMap);
            if (map.containsKey("CALC_INT_UPTO_DT")) {
                intUptoDt = (Date) map.get("CALC_INT_UPTO_DT");
                LoanSalaryRecoveryListCustDataTO objLoanSalaryRecoveryListCustDataTO = new LoanSalaryRecoveryListCustDataTO();
                objLoanSalaryRecoveryListCustDataTO.setIntCalcUptoDt(intUptoDt);
                objLoanSalaryRecoveryListCustDataTO.setCreatedBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                objLoanSalaryRecoveryListCustDataTO.setInstId(CommonUtil.convertObjToStr(map.get("INST_ID")));
                objLoanSalaryRecoveryListCustDataTO.setCreatedDt((Date) currDt.clone());
                objLoanSalaryRecoveryListCustDataTO.setStatus("PROCESSED");
                if (map.containsKey("RECOVERY_ID") && map.get("RECOVERY_ID") != null) {
                    objLoanSalaryRecoveryListCustDataTO.setRecoveryId(CommonUtil.convertObjToStr(map.get("RECOVERY_ID")));
                }                
                Iterator iterate = custDataMap.entrySet().iterator();
                    if (null != custDataMap && custDataMap.size() > 0) {
                        ArrayList list = new ArrayList();
                        while (iterate.hasNext()) {
                            Map.Entry entry = (Map.Entry) iterate.next();
                            Object key1 = (Object) entry.getKey();
                            Object value = (Object) entry.getValue();
                            objLoanSalaryRecoveryListCustDataTO.setCustId(CommonUtil.convertObjToStr(key1));
                            objLoanSalaryRecoveryListCustDataTO.setTotalRecoveryAmt(CommonUtil.convertObjToDouble(value));
                            sqlMap.executeUpdate("insertLoanSalaryRecoveryListCustDataTO", objLoanSalaryRecoveryListCustDataTO);
                        }
                    }

            }
        } catch (Exception e) {
            e.printStackTrace();
            // sqlMap.rollbackTransaction();
        }
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

    private String getRecoveryId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "RECOVERY_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void destroyObjects() {
        if(transactionDAO != null)
        transactionDAO.setLoanAmtMap(new HashMap());
    }
    
    private LinkedHashMap calcMDSPayments(String chittalNo,  String schemeName, String prodType,Date checkDt) throws SQLException {
        HashMap dataMap = new HashMap();
        LinkedHashMap dataListMap = new LinkedHashMap();
        long currentInst = 0;
        int pendingInst = 0;
        long noOfInstPay = 0;
        double insAmt = 0;
        String isWeeklyOrMonthlyScheme = "M";
        double totalPenalAmt = 0.0;
        ArrayList bonusAmountList = new ArrayList();
        LinkedHashMap penalMap = new LinkedHashMap();
        LinkedHashMap bonusMap = new LinkedHashMap();
        LinkedHashMap instAmtMap = new LinkedHashMap();
        LinkedHashMap forfeitBonusMap = new LinkedHashMap();
        ArrayList penalList = new ArrayList();
        ArrayList noticeList = new ArrayList();
        ArrayList instAmountList = new ArrayList();
        Iterator bonusInstIterator = null;
        ArrayList narrationList = new ArrayList();
        ArrayList ForfeitebonusAmountList = new ArrayList();
        ArrayList bonusReversalLsit = new ArrayList(); //16-07-2020
        String isSplitMDSTransaction = "";
        HashMap installmentAmountList = new HashMap();
        String calculateIntOn = "";
        String chittal_No = "";  
        String subNo = "";
        if (chittalNo.contains("_")) {
            System.out.println("chittal_No.indexOf(_)" + chittalNo.indexOf("_"));
            if (chittal_No.indexOf("_") != -1) {
                chittal_No = chittalNo.substring(0, chittalNo.indexOf("_"));
                subNo = chittal_No.substring(chittalNo.indexOf("_") + 1, chittalNo.length());
            }
        } else {
            chittal_No = chittalNo;
            subNo = "1";
        }
        HashMap chittalMap = new HashMap();
        int divNo = 1;
        chittalMap.put("CHITTAL_NO", chittal_No);
        chittalMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
        List chitLst = sqlMap.executeQueryForList("getSelctApplnReceiptDetails", chittalMap);
        if (chitLst != null && chitLst.size() > 0) {
            chittalMap = (HashMap) chitLst.get(0);
            divNo = CommonUtil.convertObjToInt(chittalMap.get("DIVISION_NO"));
        }
        
        HashMap productMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put("SCHEME_NAME", schemeName);
        whereMap.put("CHITTAL_NO", chittal_No);
        whereMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
        List lst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", whereMap);
//        System.out.println("Aneez List::---->" + lst);
        String bonusFirstInst = "";
        int interestDay = 0;
        int totIns = 0;
        Date startDate = null;
        Date insDate = null;
        int startMonth = 0;
        int penalGracePeriod;
        String penalGracePeriodType = "";
        if (lst != null && lst.size() > 0) {
            productMap = (HashMap) lst.get(0);
            insAmt = CommonUtil.convertObjToDouble(productMap.get("INSTALLMENT_AMOUNT")).doubleValue();
            interestDay = CommonUtil.convertObjToInt(productMap.get("INSTALLMENT_DAY"));
            totIns = CommonUtil.convertObjToInt(productMap.get("NO_OF_INSTALLMENTS"));
            startDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_START_DT")));
            insDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_START_DT")));
            startMonth = insDate.getMonth();
            bonusFirstInst = CommonUtil.convertObjToStr(productMap.get("ADVANCE_COLLECTION"));
            penalGracePeriod = CommonUtil.convertObjToInt(productMap.get("PENAL_GRACE_PERIOD"));
            penalGracePeriodType = CommonUtil.convertObjToStr(productMap.get("PENAL_GRACE_PERIOD_TYPE"));
        }
        System.out.println("productMap=======" + productMap);
        String forfeitChk = CommonUtil.convertObjToStr(productMap.get("FORFEITE_HD_Y_N"));
        isSplitMDSTransaction = CommonUtil.convertObjToStr(productMap.get("IS_SPLIT_MDS_TRANSACTION"));
        String isAdvanceCollection = CommonUtil.convertObjToStr(productMap.get("ADVANCE_COLLECTION"));//change bonusFirstInst to isAdvanceCollection have some confution
        Date endDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_END_DT")));
        int startNoForPenal = 0;
        int addNo = 1;
        int firstInst_No = -1;
        System.out.println("bbbbonus firstt" + isAdvanceCollection);
        if (isAdvanceCollection.equals("Y") || CommonUtil.convertObjToStr(productMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")) {
            startNoForPenal = 1;
            addNo = 0;
            firstInst_No = 0;
        }
        long diffDayPending = 0;
        int noOfInsPaid = 0;
        Date currDate = checkDt;
        Date instDate = null;
        boolean bonusAvailabe = true;
        int count = 0;
        HashMap instMap = new HashMap();
        instMap.put("CHITTAL_NO", chittal_No);
        instMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
        List instLst = sqlMap.executeQueryForList("getNoOfInstalmentsPaid", instMap);
        if (instLst != null && instLst.size() > 0) {
            instMap = (HashMap) instLst.get(0);
            count = CommonUtil.convertObjToInt(instMap.get("NO_OF_INST"));
        }
        HashMap insDateMap = new HashMap();
        List insDateLst;
        insDateMap.put("DIVISION_NO", divNo);
        insDateMap.put("SCHEME_NAME", schemeName);
        insDateMap.put("CURR_DATE", currDate.clone());
        insDateMap.put("ADD_MONTHS", "-1");
        insDateLst = sqlMap.executeQueryForList("getMDSCurrentInsDate", insDateMap);
        if (insDateLst != null && insDateLst.size() > 0) {
            insDateMap = (HashMap) insDateLst.get(0);
            Date InstalDt = (Date) insDateMap.get("INSTALLMENT_DT");
            currentInst = CommonUtil.convertObjToLong(insDateMap.get("INSTALLMENT_NO"));
            String advance_Collection = bonusFirstInst;
            if (advance_Collection.equals("Y") /* || CommonUtil.convertObjToStr(prodMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")*/) {
                pendingInst = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO")) - (int) count - 1;
                System.out.println("pendingInst-0----" + pendingInst);
            } else {
                pendingInst = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO")) - (int) count;
            }
            System.out.println("count-----" + count);
            System.out.println("pendingInst-----" + pendingInst);
            noOfInstPay = pendingInst + 1;
        }
        whereMap = new HashMap();
        whereMap.put("CHITTAL_NO", chittal_No);
        whereMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
        List insList = sqlMap.executeQueryForList("getNoOfInstalmentsPaid", whereMap);
        if (insList != null && insList.size() > 0) {
            whereMap = (HashMap) insList.get(0);
            noOfInsPaid = CommonUtil.convertObjToInt(whereMap.get("NO_OF_INST"));
        }

        int balanceIns = totIns - noOfInsPaid;
        long totDiscAmt = 0;
        double totBonusAmt = 0;
        double totReversalBonusAmt = 0.0; 
        double bonusAmt = 0;
        double forfeitBonusAmt = 0.0;
        LinkedHashMap installmentMap = new LinkedHashMap();
        ListIterator amountIterator = null;
        if (balanceIns >= noOfInstPay) {
            String penalIntType = "";
            double penalValue = 0;
            String penalGraceType = "";
            long penalGraceValue = 0;
            String penalCalcBaseOn = "";
            /*Commenting below block - Bank advance not required*/
//                int bankAd = 0;
//                HashMap bankAdv = new HashMap();
//                bankAdv.put("CHITTAL", chittal_No);
//                bankAdv.put("SUB_NO", subNo);
//                bankAdv.put("NEXT_INSTALL", DateUtil.getDateMMDDYYYY(tdtInsUptoPaid.getDateValue()));
//                bankAdv.put("CURR_DATE", DateUtil.getDateMMDDYYYY(tdtTrnDate.getDateValue()));
//                List bankList = ClientUtil.executeQuery("getMdsBAnkAdvForReciept", bankAdv);
//                if (bankList != null && bankList.size() > 0) {
//                    bankAdv = new HashMap();
//                    bankAdv = (HashMap) bankList.get(0);
//                    if (bankAdv != null && bankAdv.containsKey("NUM") && bankAdv.get("NUM") != null) {
//                        bankAd = Integer.parseInt(bankAdv.get("NUM").toString());
//                        //  startNoForPenal=bankAd+startNoForPenal;
//                        //pendingInst = pendingInst - bankAd;
//                    }
//                }
            // Checking prized or not
            boolean prized = false;
            int totPendingInst = 0;
            HashMap prizedMap = new HashMap();
            prizedMap.put("SCHEME_NAME", schemeName);
            prizedMap.put("DIVISION_NO", divNo); // To be changed
            prizedMap.put("CHITTAL_NO", chittal_No);
            prizedMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
            List prizeList = sqlMap.executeQueryForList("getSelectPrizedDetailsEntryRecords", prizedMap);
            if (prizeList != null && prizeList.size() > 0) {
                prizedMap = (HashMap) prizeList.get(0);
                if (prizedMap.get("DRAW") != null && !prizedMap.get("DRAW").equals("") && prizedMap.get("DRAW").equals("Y")) {
                    prized = true;
                }
                if (prizedMap.get("AUCTION") != null && !prizedMap.get("AUCTION").equals("") && prizedMap.get("AUCTION").equals("Y")) {
                    prized = true;
                }
            } else {
                prized = false;
            }
            ArrayList interestList = new ArrayList();
            if (pendingInst >= 0) {//pending installment calculation starts... && pendingInst>=noOfInstPay
                totPendingInst = (int) pendingInst;
                System.out.println("totalPending Istallment" + totPendingInst);
                double calc = 0;
                long totInst = pendingInst;
                penalCalcBaseOn = CommonUtil.convertObjToStr(productMap.get("PENAL_CALC"));
                if (prized) {
                    if (productMap.containsKey("PENEL_PZ_INT_FULL_AMT_INST_AMT") && productMap.get("PENEL_PZ_INT_FULL_AMT_INST_AMT") != null) {
                        calculateIntOn = CommonUtil.convertObjToStr(productMap.get("PENEL_PZ_INT_FULL_AMT_INST_AMT"));
                    }
                    penalIntType = CommonUtil.convertObjToStr(productMap.get("PENAL_PRIZED_INT_TYPE"));
                    penalValue = CommonUtil.convertObjToDouble(productMap.get("PENAL_PRIZED_INT_AMT"));
                    penalGraceType = CommonUtil.convertObjToStr(productMap.get("PENAL_PRIZED_GRACE_PERIOD_TYPE"));
                    penalGraceValue = CommonUtil.convertObjToLong(productMap.get("PENAL_PRIZED_GRACE_PERIOD"));
                    if (penalGraceType.equals("Installments") && (penalIntType != null && penalIntType.equals("Percent"))) {
                        pendingInst -= penalGraceValue;
                    }
                } else if (!prized) {
                    if (productMap.containsKey("PENEL_INT_FULL_AMT_INST_AMT") && productMap.get("PENEL_INT_FULL_AMT_INST_AMT") != null) {
                        calculateIntOn = CommonUtil.convertObjToStr(productMap.get("PENEL_INT_FULL_AMT_INST_AMT"));
                    }
                    penalIntType = CommonUtil.convertObjToStr(productMap.get("PENAL_INT_TYPE"));
                    penalValue = CommonUtil.convertObjToDouble(productMap.get("PENAL_INT_AMT"));
                    penalGraceType = CommonUtil.convertObjToStr(productMap.get("PENAL_GRACE_PERIOD_TYPE"));
                    penalGraceValue = CommonUtil.convertObjToLong(productMap.get("PENAL_GRACE_PERIOD"));
                    if (penalGraceType.equals("Installments") && (penalIntType != null && penalIntType.equals("Percent"))) {
                        pendingInst -= penalGraceValue;
                    }
                }
                List bonusAmout = new ArrayList();
                if (calculateIntOn != null && calculateIntOn.equals("Installment Amount")) {
                    HashMap nextInstMaps = null;
                    for (int i = startNoForPenal; i <= noOfInstPay - addNo; i++) {
                        nextInstMaps = new HashMap();
                        nextInstMaps.put("SCHEME_NAME", schemeName);
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
                Rounding rod = new Rounding();
                int installmentFreq = 30;
                for (int i = startNoForPenal; i < noOfInstPay + startNoForPenal; i++) {
                    if (calculateIntOn != null && calculateIntOn.equals("Installment Amount")) {
                        int predefinedIterator = i - 1;
                        insAmt = 0.0;
                        double instAmount = insAmt;
                        if (bonusAmout != null && bonusAmout.size() > 0) {
                            if (isAdvanceCollection.equals("Y") || CommonUtil.convertObjToStr(productMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")) {
                                instAmount -= CommonUtil.convertObjToDouble(bonusAmout.get(predefinedIterator));
                            } else {
                                instAmount -= CommonUtil.convertObjToDouble(bonusAmout.get(i));
                            }
                        }
                        insAmt = instAmount;
                    }
                    HashMap nextInstMap = new HashMap();
                    nextInstMap.put("SCHEME_NAME", schemeName);
                    nextInstMap.put("SL_NO", new Double(i + noOfInsPaid));
                    int tempInstNo = i + noOfInsPaid + 1;
                    List listRec = sqlMap.executeQueryForList("getSelectNextInstDate", nextInstMap);
                    if (listRec != null && listRec.size() > 0) {
                        double penal = 0;
                        nextInstMap = (HashMap) listRec.get(0);
                        nextInstMap.put("INST_AMT", insAmt);
                        //Changed by sreekrishnan
                        if (CommonUtil.convertObjToStr(productMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")) {
                            //  instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                            instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("DRAW_AUCTION_DATE")));
                        } else {
                            instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                        }
                        diffDayPending = DateUtil.dateDiff(instDate, currDate);
                        System.out.println("First #########diffDay : Inst day " + diffDayPending);
                        //Holiday Checking - Added By Suresh
                        HashMap holidayMap = new HashMap();
                        boolean checkHoliday = true;
                        System.out.println("instDate   " + instDate);
                        instDate = setProperDtFormat(instDate);
                        System.out.println("instDate   " + instDate);
                        holidayMap.put("NEXT_DATE", instDate);
                        holidayMap.put("BRANCH_CODE", _branchCode);
                        while (checkHoliday) {
                            boolean tholiday = false;
                            System.out.println("enterytothecheckholiday" + checkHoliday);
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
                        // System.out.println("#### diffDayPending Final Days : " + diffDayPending);
                        if (penalCalcBaseOn != null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Days")) {
                            System.out.println("based on Dayssss ");
                            if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Days")) {
                                if (diffDayPending > penalGraceValue) {
//                                        if(graceCalculated == pendingInst){
//                                            diffDayPending=penalGraceValue;
//                                        }
                                    if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                        System.out.println("CalcBefore" + calc);
                                        System.out.println("diffDayPending...." + diffDayPending + "penalValue" + penalValue + "instAMy" + insAmt);
                                        calc += (diffDayPending * penalValue * insAmt) / 36500;
                                        penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                        System.out.println("CalcAfter" + calc);
                                    } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                        calc += penalValue;
                                        penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                    }
                                }
                            } else if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Installments")) {
                                // To be written
                                if (diffDayPending > penalGraceValue * installmentFreq) {
                                    if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
//                                                calc = insAmt*(((pendingInst+1)*pendingInst/2)*penalValue)/1200;
                                        if (isWeeklyOrMonthlyScheme != null && !isWeeklyOrMonthlyScheme.equals("") && isWeeklyOrMonthlyScheme.equals("W")) {
                                            calc += (double) ((insAmt * penalValue) * 7 / 36500.0) * pendingInst--;
                                        } else {
                                            calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                        }
                                        penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                    } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                        calc += penalValue;
                                        penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                    }
                                }
                            }
                        } else if (penalCalcBaseOn != null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Installments")) {
                            System.out.println("based on installments " + "penalGraceType" + penalGraceType);
                            if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Days")) {
                                System.out.println("penal grace days" + pendingInst);
                                if (diffDayPending > penalGraceValue) {
                                    if (pendingInst == 0) {
                                        pendingInst = 1;
                                    }
                                    System.out.println("Inside calculation procedure 1 " + calc);
                                    if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                        System.out.println("Inside calculation procedure pendingInst " + pendingInst + "mm" + insAmt + " " + penalValue);
                                        if (isWeeklyOrMonthlyScheme != null && !isWeeklyOrMonthlyScheme.equals("") && isWeeklyOrMonthlyScheme.equals("W")) {
                                            calc += (double) ((insAmt * penalValue) * 7 / 36500.0) * pendingInst--;
                                        } else {
                                            if (CommonUtil.convertObjToStr(productMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")) {
                                                //calc += (double) ((insAmt * penalValue) / 1200.0) * prependingInst--;
                                            } else {
                                                calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                            }
                                            System.out.println("cal####" + calc);
                                        }
                                        penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                    } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute") && totInst <= noOfInstPay) {
                                        calc += penalValue;
                                        penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                    }
                                    System.out.println("nitz calc " + calc);
                                }
                            } else if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Installments")) {
                                // To be written
                                System.out.println("diffDayPending" + diffDayPending);
                                System.out.println("penalGraceValue===" + penalGraceValue + "calc====" + calc + "penalIntType==" + penalIntType);
                                // int instFreq = CommonUtil.convertObjToInt(observable.getInstalmntFreq());
                                int instFreq = 30;
                                System.out.println("instFreq===" + instFreq + "insAmt" + insAmt + "penalValue" + penalValue + "pendingInst" + pendingInst);
                                if ((diffDayPending > (penalGraceValue * instFreq)) && (pendingInst >= penalGraceValue)) {
                                    if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                        //calc = insAmt*(((pendingInst+1)*pendingInst/2)*penalValue)/1200;
                                        if (prized == true || calculateIntOn.equals("Installment Amount")) {
                                            if (isWeeklyOrMonthlyScheme != null && !isWeeklyOrMonthlyScheme.equals("") && isWeeklyOrMonthlyScheme.equals("W")) {
                                                calc += (double) ((insAmt * penalValue) * 7 / 36500.0) * pendingInst--;
                                            } else {
                                                //modified by rishad for predefinition installment
                                                if (CommonUtil.convertObjToStr(productMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")) {
                                                    //calc += (double) ((insAmt * penalValue) / 1200.0) * prependingInst--;
                                                } else {
                                                    calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                                }
                                                //  calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                            }
                                            penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                        } else {
                                            if (isWeeklyOrMonthlyScheme != null && !isWeeklyOrMonthlyScheme.equals("") && isWeeklyOrMonthlyScheme.equals("W")) {
                                                calc = (double) ((pendingInst * (pendingInst + 1) / 2) * insAmt * penalValue) * 7 / 36500;
                                            } else {
                                                calc = (double) ((pendingInst * (pendingInst + 1) / 2) * insAmt * penalValue) / 1200;
                                            }
                                            penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                        }
                                    } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                        calc += penalValue;
                                        penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                    }
                                }
                                System.out.println("calcfffff===" + calc);
                            }
                        }

                        //After Scheme End Date Penal Calculating
                        HashMap c_hash = new HashMap();
                        c_hash.put("SCHEME_NAME", schemeName);
                        List closureList = sqlMap.executeQueryForList("checkSchemeClosureDetails", c_hash);
                        if (closureList != null && closureList.size() > 0) {
                            c_hash = (HashMap) closureList.get(0);
                            System.out.println(" Value of Scheme Closed Date " + (Date) c_hash.get("SCHEME_CLOSE_DT"));
                            //   Date clsDate  = (Date)c_hash.get("SCHEME_CLOSE_DT");
                            Date clsDate = (Date) c_hash.get("SCHEME_END_DT");

                            System.out.println("annnn" + clsDate);
                            Calendar cal = null;
                            cal = Calendar.getInstance();
                            cal.setTime(clsDate);
                            cal.add(Calendar.DATE, 1);
                            clsDate = cal.getTime();
                            System.out.println("clsDate.." + clsDate + "currrdddaaattteeeee" + currDate);

                            if (currDate.after(clsDate)) {
                                if ((i + 1 == noOfInstPay + startNoForPenal) && !(penalCalcBaseOn.equals("Days") && penalIntType.equals("Percent")) && (DateUtil.dateDiff(endDate, currDate) > 0)) {
                                    System.out.println("#### endDate : " + endDate + "currDate" + currDate);
                                    if (penalIntType.equals("Percent")) {
                                        diffDayPending = DateUtil.dateDiff(endDate, currDate);
                                        System.out.println("#### endDate_diffDayPending : " + diffDayPending);
                                        System.out.println("noOfInstPay" + noOfInstPay + "insAmt" + insAmt + "penalValue" + penalValue);
                                        calc += (double) ((((insAmt * noOfInstPay * penalValue) / 100.0) * diffDayPending) / 365);
                                    }
                                    // Absolute Not Required...
                                }
                            }
                        }
                        //comented by rish
//                            nextInstMap.put("PENAL", String.valueOf(penal));
//                            installmentMap.put(String.valueOf(i + noOfInsPaid + addNo), nextInstMap);
//                            penal = calc + 0.5;
                    }
                    penalMap.put(tempInstNo,calc); 
                    System.out.println("penalMap :: nithya :: " + penalMap +" Chittal No :: " + chittal_No);
                }
//                    graceCalculated = 1;
//                    pendingInst=pendingInst = CommonUtil.convertObjToLong(txtPendingInst.getText());
//                    if (penalCalcBaseOn != null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Installments"))
//                    {
//                        System.out.println("hiiiii"+"insAmt==="+insAmt+"penalValue===="+penalValue+"pendingInst===="+pendingInst);
//                        if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Days")) {
//                            calc = (double) ((insAmt * penalValue) / 1200.0) * (((pendingInst-1)*pendingInst)/2);
//                        }
//                        else if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Installments")) {
//                            calc = (double) ((insAmt * penalValue) / 1200.0) * (((pendingInst-1)*pendingInst)/2);
//                        }
//                    }
                System.out.println("ccccc" + calc);
                if (calc > 0) {
                    //txtPenalAmtPayable.setText(String.valueOf((long) (calc + 0.5)));
                    totalPenalAmt = CommonUtil.convertObjToDouble(String.valueOf((long) (calc + 0.5)));
                }
                double penalAmtWithoutEdit = calc;
                ArrayList penalRealList = new ArrayList();
//                   al.add(CommonUtil.convertObjToDouble(penalList.get(0)));
                double d = 0;
                if (penalList != null && penalList.size() > 0) {
                    double firVal = CommonUtil.convertObjToDouble(penalList.get(0));
                    Collections.reverse(penalList);
                    for (int i = 0; i <= penalList.size(); i++) {
                        if (i + 1 < penalList.size()) {
                            d = CommonUtil.convertObjToDouble(penalList.get(i)) - CommonUtil.convertObjToDouble(penalList.get(i + 1));
                            penalRealList.add(d);
                        }
                    }
                    penalRealList.add(firVal);
                }
                Collections.reverse(penalRealList);
            }
            //pending installment calculation ends...
            //Bonus calculation details Starts...               

            for (int i = startNoForPenal; i <= noOfInstPay - addNo; i++) {
                HashMap nextInstMap = new HashMap();
                nextInstMap.put("SCHEME_NAME", schemeName);
                nextInstMap.put("DIVISION_NO", divNo);
                nextInstMap.put("SL_NO", new Double(i + noOfInsPaid + addNo + firstInst_No));
                int tempInstNo = i + noOfInsPaid + addNo + firstInst_No + 1;
                List listRec = sqlMap.executeQueryForList("getSelectBonusAndNextInstDateWithoutDivision", nextInstMap);
                if (listRec == null || listRec.size() == 0) {
                    int instDay = interestDay;
                    System.out.println("#@$@#@#@ instDay : " + instDay);
                    Date curDate = currDate;
                    int curMonth = curDate.getMonth();
                    System.out.println("@#$$#$#instDay" + instDay);
                    curDate.setMonth(curMonth + i + 1);
                    curDate.setDate(instDay);
                    listRec = new ArrayList();
                    nextInstMap.put("NEXT_INSTALLMENT_DATE", curDate);
                    listRec.add(nextInstMap);
                    bonusAvailabe = false;
                    System.out.println("y000000" + bonusAvailabe);
                }
                if (listRec != null && listRec.size() > 0) {
                    nextInstMap = (HashMap) listRec.get(0);
                    instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                    bonusAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_BONUS_AMOUNT"))).doubleValue();
                    if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                            && bonusAmt > 0) {
                        Rounding rod = new Rounding();
                        if (productMap.get("BONUS_ROUNDOFF") != null && productMap.get("BONUS_ROUNDOFF").equals("NEAREST_VALUE")) {
                            bonusAmt = (double) rod.getNearest((long) (bonusAmt * 100), 100) / 100;
                        } else if (productMap.get("BONUS_ROUNDOFF") != null && productMap.get("BONUS_ROUNDOFF").equals("LOWER_VALUE")) {
                            bonusAmt = (double) rod.lower((long) (bonusAmt * 100), 100) / 100;
                        } else {
                            bonusAmt = Math.round(bonusAmt * 100.0) / 100.0;
                            System.out.println("bonusAmt>>> " + bonusAmt);
                            Double d = bonusAmt;
                            String s = d.toString();
                            String s2 = s.substring(s.indexOf('.') + 1);
                            if (s2.length() >= 2) {
                                String s1 = "" + s2.charAt(1);
                                String s3 = "" + s2.charAt(0);
                                System.out.println("d " + d + "   s" + s + "   s2" + s2 + " s1" + s1 + " s3" + s3);
                                int num1 = Integer.parseInt(s1);
                                int num = Integer.parseInt(s3);
                                if (num1 >= 5) {
                                    num += 1;
                                }
                                s = s.substring(0, s.indexOf('.')) + "." + num + "0";
                            } else {
                                s = s + "0";
                            }

                            System.out.println("sss###" + s);
                            bonusAmt = CommonUtil.convertObjToDouble(s);
                        }
                    }

                    System.out.println("bonusAmt------>" + bonusAmt);
                    long diffDay = DateUtil.dateDiff(instDate, currDate);
                    System.out.println("INSTAL DATE " + instDate);
                    System.out.println("CURRNT DATE" + currDate);
                    System.out.println("First #########diffDay :----> " + diffDay);
                    HashMap holidayMap = new HashMap();
                    boolean checkHoliday = true;
                    System.out.println("instDate   " + instDate);
                    instDate = setProperDtFormat(instDate);
                    System.out.println("instDate   " + instDate);
                    holidayMap.put("NEXT_DATE", instDate);
                    holidayMap.put("BRANCH_CODE", _branchCode);
                    while (checkHoliday) {
                        boolean tholiday = false;
                        System.out.println("enterytothecheckholiday" + checkHoliday);
                        List Holiday = sqlMap.executeQueryForList("checkHolidayDateOD", holidayMap);
                        List weeklyOf = sqlMap.executeQueryForList("checkWeeklyOffOD", holidayMap);
                        boolean isHoliday = Holiday.size() > 0 ? true : false;
                        boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
                        if (isHoliday || isWeekOff) {
                            System.out.println("#### diffDay Holiday True : Bonus" + diffDay);
                            if (CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")) {
                                diffDay -= 1;
                                instDate.setDate(instDate.getDate() + 1);
                            } else {
                                diffDay += 1;
                                instDate.setDate(instDate.getDate() - 1);
                            }
                            holidayMap.put("NEXT_DATE", instDate);
                            checkHoliday = true;
                            System.out.println("#### holidayMap : " + holidayMap);
                        } else {
                            System.out.println("#### diffDay Holiday False : " + diffDay);
                            checkHoliday = false;
                        }
                    }
                    System.out.println("#### diffDay Final Days : " + diffDay);
                    if (productMap.get("BONUS_ALLOWED") != null && !productMap.get("BONUS_ALLOWED").equals("") && (productMap.get("BONUS_ALLOWED").equals("Y"))) {
                        //bonus calculation details...
                        String prizedDefaultYesN = "";
                        if (productMap.containsKey("PRIZED_DEFAULTERS") && productMap.get("PRIZED_DEFAULTERS") != null) {
                            prizedDefaultYesN = CommonUtil.convertObjToStr(productMap.get("PRIZED_DEFAULTERS"));
                        }
                        //bonus calculation details...
                        System.out.println("Bonus Available ddd" + bonusAvailabe + "prizedDefaultYesN  " + prizedDefaultYesN + "diffDay :" + diffDay);
                        if (bonusAvailabe == true) {//edit Here PRIZED_DEFAULTERS
                            long bonusPrizedValue = CommonUtil.convertObjToLong(productMap.get("BONUS_PRIZED_GRACE_PERIOD"));
                            String bonusPrzInMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_MNTH"));
                            if (prized) {
                                HashMap nextActMap = new HashMap();
                                nextActMap.put("SCHEME_NAME", schemeName);
                                nextActMap.put("DIVISION_NO", divNo);
                                nextActMap.put("SL_NO", CommonUtil.convertObjToDouble(i + noOfInsPaid));
                                List listAuc = sqlMap.executeQueryForList("getSelectNextAuctDate", nextActMap);
                                if (listAuc != null && listAuc.size() > 0) {
                                    nextActMap = (HashMap) listAuc.get(0);
                                }
                                Date drawAuctionDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextActMap.get("DRAW_AUCTION_DATE")));
                                System.out.println("Date acution d" + drawAuctionDate);
                                Calendar cal = null;
                                Date newDate = null;
                                if (bonusPrzInMonth != null && bonusPrzInMonth.equalsIgnoreCase("M")) {
                                    cal = Calendar.getInstance();
                                    cal.setTime(drawAuctionDate);
                                    cal.add(Calendar.MONTH, CommonUtil.convertObjToInt(bonusPrizedValue));
                                    newDate = cal.getTime();
                                } else {
                                    newDate = DateUtil.addDays(drawAuctionDate, CommonUtil.convertObjToInt(bonusPrizedValue));
                                }
                                long dateDiff = CommonUtil.convertObjToLong(DateUtil.dateDiff(currDate, newDate));
//                                   diffDay = CommonUtil.convertObjToLong(DateUtil.dateDiff(curr_dt, newDate));
                                diffDay = DateUtil.dateDiff(drawAuctionDate, currDate);
                                System.out.println("Date acution c" + newDate);
                                System.out.println("Date acution c" + dateDiff);
                                //Holiday checking Added  By Nidhin 12/11/2014
                                HashMap holidayCheckMap = new HashMap();
                                boolean checkForHoliday = true;
                                newDate = setProperDtFormat(newDate);
                                holidayCheckMap.put("NEXT_DATE", newDate);
                                holidayCheckMap.put("BRANCH_CODE", _branchCode);
                                while (checkForHoliday) {
                                    List Holiday = sqlMap.executeQueryForList("checkHolidayDateOD", holidayCheckMap);
                                    List weeklyOf = sqlMap.executeQueryForList("checkWeeklyOffOD", holidayCheckMap);
                                    boolean isHoliday = Holiday.size() > 0 ? true : false;
                                    boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
                                    if (isHoliday || isWeekOff) {
                                        System.out.println("#### diffDay Holiday True : Bonus" + dateDiff);
                                        if (CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")) {
                                            dateDiff += 1;
                                            diffDay -= 1; // Added by nithya on 14-08-2020 for KD-2163 
                                            newDate.setDate(newDate.getDate() + 1);
                                        } else {
                                            dateDiff -= 1;
                                            diffDay += 1;  // Added by nithya on 14-08-2020 for KD-2163 
                                            newDate.setDate(newDate.getDate() - 1);
                                        }
                                        holidayCheckMap.put("NEXT_DATE", newDate);
                                        checkForHoliday = true;
                                    } else {
                                        checkForHoliday = false;
                                    }
                                }
                                //End for HoliDay Checking
                                whereMap = new HashMap();
                                whereMap.put("CHITTAL_NO", chittal_No);
                                whereMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                                whereMap.put("SCHEME_NAME", schemeName);
                                List paymentList = sqlMap.executeQueryForList("getSelectMDSPaymentDetails", whereMap);
                                if (paymentList != null && paymentList.size() > 0 && productMap.get("PRIZED_OWNER_BONUS").equals("N")) {
                                    System.out.println("###### NO BONUS FOR PRODUCT PARAMETER");
                                } else if (paymentList != null && paymentList.size() > 0 && productMap.get("PRIZED_OWNER_BONUS").equals("Y")) {
                                    String bonusPrizedDays = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_DAYS"));
                                    String bonusPrizedMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_MNTH"));
                                    String bonusPrizedAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_AFT"));
                                    String bonusPrizedEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_END"));
                                    //long bonusPrizedValue = CommonUtil.convertObjToLong(productMap.get("BONUS_PRIZED_GRACE_PERIOD"));
                                    if (bonusPrizedDays != null && !bonusPrizedDays.equals("") && bonusPrizedDays.equals("D") && diffDay <= bonusPrizedValue) {
//                                            if(prizedDefaultYesN.equals("N") || diffDay <= bonusPrizedValue){
                                        System.out.println("Total Bonus before" + totBonusAmt);
                                        if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                            totBonusAmt = totBonusAmt + bonusAmt;
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                        } else {
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(0));
                                        }
                                        System.out.println("Total Bonus after" + totBonusAmt);
                                    } else if (bonusPrizedMonth != null && !bonusPrizedMonth.equals("") && bonusPrizedMonth.equals("M") && diffDay <= (bonusPrizedValue * 30)) {
                                        if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                            totBonusAmt = totBonusAmt + bonusAmt;
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                        } else {
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(0));
                                        }
                                    } else if (bonusPrizedAfter != null && !bonusPrizedAfter.equals("") && bonusPrizedAfter.equals("A") && currDate.getDate() <= bonusPrizedValue) {
                                        if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                            totBonusAmt = totBonusAmt + bonusAmt;
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                        } else {
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(0));
                                        }
                                    } else if (bonusPrizedEnd != null && !bonusPrizedEnd.equals("") && bonusPrizedEnd.equals("E")) {
                                    } else {
                                        if (productMap.containsKey("FORFEITE_HD_Y_N") && productMap.get("FORFEITE_HD_Y_N").equals("Y")) {
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(0));
                                            ForfeitebonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                            forfeitBonusAmt = bonusAmt;
                                            System.out.println("BonusAmount Yes" + bonusAmt);
                                        }
                                    }
                                    System.out.println("Bonus Available->" + bonusAvailabe + "prizedDefaultYesN  " + prizedDefaultYesN + "diffDay :" + diffDay + "bonusPrizedValue :" + bonusPrizedValue);
                                    System.out.println("111ss" + totBonusAmt);
                                } else if (productMap.get("PRIZED_OWNER_BONUS").equals("Y") || productMap.get("PRIZED_OWNER_BONUS").equals("N")) {
                                    String bonusGraceDays = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_DAYS"));
                                    String bonusGraceMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_MONTHS"));
                                    String bonusGraceOnAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_AFTER"));
                                    String bonusGraceEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_END"));
                                    long bonusGraceValue = CommonUtil.convertObjToLong(productMap.get("BONUS_GRACE_PERIOD"));
                                    System.out.println("bonusGraceDays---In :" + bonusGraceDays + " : bonusGraceMonth " + bonusGraceMonth + "bonusGraceOnAfter" + bonusGraceOnAfter + " " + bonusGraceEnd + " bonusGraceValue" + bonusGraceValue + "diffDay :" + diffDay);
                                    if (bonusGraceDays != null && !bonusGraceDays.equals("") && bonusGraceDays.equals("D") && diffDay <= bonusGraceValue /*
                                             * && pendingInst<noOfInstPay
                                             */) {
                                        //                                        txtBonusAmt.setText(txtBonusAmtAvail.getText());

                                        if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                            totBonusAmt = totBonusAmt + bonusAmt;
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                        } else {
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(0));
                                        }
                                        System.out.println("diffDay : " + diffDay + "bonusPrizedValue :" + bonusPrizedValue + "bonusAmt :" + bonusAmt + "totBonusAmt :" + totBonusAmt);
//                                                                                  }
                                    } else if (bonusGraceMonth != null && !bonusGraceMonth.equals("") && bonusGraceMonth.equals("M") && diffDay <= (bonusGraceValue * 30) /*
                                             * && pendingInst<noOfInstPay
                                             */) {
                                        //                                        txtBonusAmt.setText(txtBonusAmtAvail.getText());
                                        if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                            totBonusAmt = totBonusAmt + bonusAmt;
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                        } else {
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(0));
                                        }
                                        System.out.println("dayDiff in Bomnusss this" + diffDay + " " + (bonusGraceValue * 30) + " " + totBonusAmt + " " + bonusAmt);
                                    } else if (bonusGraceOnAfter != null && !bonusGraceOnAfter.equals("") && bonusGraceOnAfter.equals("A") && currDate.getDate() <= bonusGraceValue /*
                                             * && pendingInst<noOfInstPay
                                             */) {
                                        //                                        txtBonusAmt.setText(txtBonusAmtAvail.getText());
                                        if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                            totBonusAmt = totBonusAmt + bonusAmt;
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                        } else {
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(0));
                                        }
                                    } else if (bonusGraceEnd != null && !bonusGraceEnd.equals("") && bonusGraceEnd.equals("E") /*
                                             * && pendingInst<noOfInstPay
                                             */) {
                                    } else {
                                        if (productMap.get("FORFEITE_HD_Y_N") != null && productMap.get("FORFEITE_HD_Y_N").equals("Y")) {
                                            ForfeitebonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(0));
                                            forfeitBonusAmt = bonusAmt;
                                            System.out.println("BonusAmount Yes23" + bonusAmt);
                                        }

                                    }
                                    System.out.println("222ss" + totBonusAmt);
                                } else {
                                    System.out.println("Prized chittal with no bomus eligibility");
                                }

                            } else if (!prized) {
                                String bonusGraceDays = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_DAYS"));
                                String bonusGraceMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_MONTHS"));
                                String bonusGraceOnAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_AFTER"));
                                String bonusGraceEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_END"));
                                long bonusGraceValue = CommonUtil.convertObjToLong(productMap.get("BONUS_GRACE_PERIOD"));
                                System.out.println("bonusGraceDays " + bonusGraceDays + "bonusGraceMonth" + bonusGraceMonth + "bonusGraceOnAfter" + bonusGraceOnAfter + " " + bonusGraceEnd + " bonusGraceValue" + bonusGraceValue);
                                if (bonusGraceDays != null && !bonusGraceDays.equals("") && bonusGraceDays.equals("D") && diffDay <= bonusGraceValue /*
                                         * && pendingInst<noOfInstPay
                                         */) {
                                    //                                        txtBonusAmt.setText(txtBonusAmtAvail.getText());
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                    bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                } else if (bonusGraceMonth != null && !bonusGraceMonth.equals("") && bonusGraceMonth.equals("M") && diffDay <= (bonusGraceValue * 30) /*
                                         * && pendingInst<noOfInstPay
                                         */) {
                                    //                                        txtBonusAmt.setText(txtBonusAmtAvail.getText());
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                    bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                    System.out.println("dayDiff in Bomnusss****" + diffDay + " " + (bonusGraceValue * 30) + " " + totBonusAmt + " " + bonusAmt);
                                } else if (bonusGraceOnAfter != null && !bonusGraceOnAfter.equals("") && bonusGraceOnAfter.equals("A") && currDate.getDate() <= bonusGraceValue /*
                                         * && pendingInst<noOfInstPay
                                         */) {
                                    //                                        txtBonusAmt.setText(txtBonusAmtAvail.getText());
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                    bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                } else if (bonusGraceEnd != null && !bonusGraceEnd.equals("") && bonusGraceEnd.equals("E") /*
                                         * && pendingInst<noOfInstPay
                                         */) {
                                } else {
                                    //                                        txtBonusAmt.setText(String.valueOf("0"));
                                    bonusAmountList.add(CommonUtil.convertObjToDouble(0));
                                    if (productMap.get("FORFEITE_HD_Y_N") != null && productMap.get("FORFEITE_HD_Y_N").equals("Y")) {
                                        ForfeitebonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                        forfeitBonusAmt = bonusAmt;
                                    }
                                }
                            }
                        }

                        System.out.println("totBonusAmtmmmm>>" + totBonusAmt);
                        System.out.println("bbb" + (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                                && totBonusAmt > 0));
                        System.out.println("productMap.get()" + productMap.get("BONUS_ROUNDOFF"));
                        // +addNo added by Rajesh
                        HashMap instMaps = new HashMap();
                        if (installmentMap.containsKey(String.valueOf(i + noOfInsPaid + addNo))) {
                            Rounding rod = new Rounding();
                            instMaps = (HashMap) installmentMap.get(String.valueOf(i + noOfInsPaid + addNo));
                            //Added By Suresh
////                                if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
////                                        && bonusAmt > 0) {
////                                    if (productMap.get("BONUS_ROUNDOFF") != null && productMap.get("BONUS_ROUNDOFF").equals("NEAREST_VALUE")) {
////                                        bonusAmt = (double) rod.getNearest((long) (bonusAmt * 100), 100) / 100;
////                                    } else {
////                                        bonusAmt = (double) rod.lower((long) (bonusAmt * 100), 100) / 100;
////                                    }
////                                }
//                                bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));

                            instMaps.put("BONUS", String.valueOf(bonusAmt));
                            installmentMap.put(String.valueOf(i + noOfInsPaid + addNo), instMaps);
                            System.out.println("installmentMap nithya :: " + installmentMap +" Chittal No :: " + chittal_No);
                        }
                    }
                }
                bonusMap.put(tempInstNo,bonusAmt);
                forfeitBonusMap.put(tempInstNo, forfeitBonusAmt);
                instAmtMap.put(tempInstNo,insAmt-bonusAmt);
                System.out.println("bonusMap nithya :: " + bonusMap +" Chittal No :: " + chittal_No);
                System.out.println("instAmtMap nithya :: " + instAmtMap +" Chittal No :: " + chittal_No);
                System.out.println("");
                bonusAmt = 0;
            }
            System.out.println("totBonusAmtmmmm" + totBonusAmt + bonusAmountList.size());
            if (isSplitMDSTransaction != null && isSplitMDSTransaction.equals("Y")) {
                if (totBonusAmt == 0 && bonusAmountList != null && bonusAmountList.size() <= 0) {
                    for (int i = 0; i < noOfInstPay; i++) {
                        bonusAmountList.add(0.0);
                    }
                }
//                    if (penalRealList.isEmpty()) {
//                        for (int i = 0; i < noOfInstPay; i++) {
//                            penalRealList.add(0.0);
//                        }
//                    }
            }
            System.out.println("bbb" + (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                    && totBonusAmt > 0));
            System.out.println("productMap.get()" + productMap.get("BONUS_ROUNDOFF"));
            if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                    && totBonusAmt > 0) {
                Rounding rod = new Rounding();
                if (productMap.get("BONUS_ROUNDOFF") != null && productMap.get("BONUS_ROUNDOFF").equals("NEAREST_VALUE")) {
                    totBonusAmt = (double) rod.getNearest((long) (totBonusAmt * 100), 100) / 100;
                } else if (productMap.get("BONUS_ROUNDOFF") != null && productMap.get("BONUS_ROUNDOFF").equals("LOWER_VALUE")) {
                    totBonusAmt = (double) rod.lower((long) (totBonusAmt * 100), 100) / 100;
                } else {
                    totBonusAmt = Math.round(totBonusAmt * 100.0) / 100.0;
                    System.out.println("totBonusAmt " + totBonusAmt);
                    Double d = totBonusAmt;
                    String s = d.toString();
                    String s2 = s.substring(s.indexOf('.') + 1);
                    if (s2.length() >= 2) {
                        String s1 = "" + s2.charAt(1);
                        String s3 = "" + s2.charAt(0);
                        System.out.println("d " + d + "   s" + s + "   s2" + s2 + " s1" + s1 + " s3" + s3);
                        int num1 = Integer.parseInt(s1);
                        int num = Integer.parseInt(s3);
                        if (num1 >= 5) {
                            num += 1;
                        }
                        s = s.substring(0, s.indexOf('.')) + "." + num + "0";
                    } else {
                        s = s + "0";
                    }

                    System.out.println("sss" + s);
                    totBonusAmt = CommonUtil.convertObjToDouble(s);
                }
            }
            int calc = 0;
            double instalAmt = 0;
            double chargeAmt = 0.0;
            whereMap.put("ACT_NUM", chittal_No);
            chargeAmt = getChargeAmount(whereMap, prodType);
            double instPayable = (insAmt * noOfInstPay) - totBonusAmt - totDiscAmt;
            double netAmount = instPayable + totalPenalAmt;
            
            Iterator iterate = instAmtMap.entrySet().iterator();
            if (null != instAmtMap && instAmtMap.size() > 0) {
                ArrayList list = new ArrayList();
                while (iterate.hasNext()) {
                    
                    Map.Entry entry = (Map.Entry) iterate.next();
                    Object key1 = (Object) entry.getKey();
                    Object value = (Object) entry.getValue();                    
                    ////
                    double principal = CommonUtil.convertObjToDouble(value);
                    double penal = CommonUtil.convertObjToDouble(penalMap.get(key1));
                    dataMap = new HashMap();
                    dataMap.put("CHITTAL_NO", chittal_No);
                    dataMap.put("SCHEME_NAME", schemeName);
                    if (chargeAmt > 0) {
                        dataMap.put("CHARGES", String.valueOf(chargeAmt));
                    } else {
                        dataMap.put("CHARGES", "0");
                    }
                    dataMap.put("DIVISION_NO", String.valueOf(divNo));
                    dataMap.put("CHIT_START_DT", startDate);
                    dataMap.put("INSTALLMENT_DATE", insDate);
                    dataMap.put("NO_OF_INSTALLMENTS", String.valueOf(totIns));
                    dataMap.put("CURR_INST", String.valueOf(key1));
                    dataMap.put("PENDING_INST", String.valueOf(totPendingInst));
                    dataMap.put("PENDING_DUE_AMT", String.valueOf(principal));
                    dataMap.put("NO_OF_INST_PAY", String.valueOf(1));
                    dataMap.put("PRINCIPAL", String.valueOf(principal)); // Principal Amount
                    dataMap.put("PAID_INST", String.valueOf(noOfInsPaid));
                    dataMap.put("PAID_DATE", currDt);
                    dataMap.put("INTEREST", "0");
                    if (prized == true) {
                        dataMap.put("PRIZED_MEMBER", "Y");
                    } else {
                        dataMap.put("PRIZED_MEMBER", "N");
                    }
                    dataMap.put("BONUS", bonusMap.get(key1));
                    dataMap.put("FORFEIT",forfeitBonusMap.get(key1));
                    dataMap.put("DISCOUNT", new Double(0.0));
                    dataMap.put("PENAL", penalMap.get(key1));  
                    dataMap.put("TOTAL_DEMAND", new Double(principal + CommonUtil.convertObjToDouble(penalMap.get(key1))));
                    dataMap.put("CLEAR_BALANCE", new Double(0));
                    ////
                    dataListMap.put(key1, dataMap);
                }
            }
            System.out.println("Heloooo  Checking by nithya .... dataListMap:: " + dataListMap);
            
            System.out.println("bonus amount list :: " + bonusAmountList);
        } else {
            dataListMap = null;
        }
        return dataListMap;

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
   
    
      public void processMDSTransactionPart(HashMap map, HashMap transactionMap) throws Exception {
          try{
              
              HashMap data = null ;
              HashMap dataMap = null;              
              LinkedHashMap instBonusMap = null;
              HashMap mdsBonusMap = null;
              String schemeName = "";
              String chittalNo = "";
              List singleRowList =  null;
              HashMap finalMap = new HashMap();
              finalMap = (HashMap) map.get("RECOVERY_MDS_PROCESS_LIST");
              List recoveryList;
              sqlMap.startTransaction();
              // TRANSACTION_DETAILS_DATA={DR_ACT_NUM=0001703000007, TRANS_TYPE=TRANSFER, TRANSACTION_PART=TRANSACTION_PART, 
              //USER_ID=admin, DR_PROD_ID=703, DR_PROD_TYPE=SA}
              
              String debitProdType = CommonUtil.convertObjToStr(transactionMap.get("DR_PROD_TYPE"));
              String debitProdId = CommonUtil.convertObjToStr(transactionMap.get("DR_PROD_ID"));
              String debitAcctNo = CommonUtil.convertObjToStr(transactionMap.get("DR_ACT_NUM"));
              String divisionNo = "";
              double instAmt = 0.0;
              String name= "";
              int instPaid = 0;
              String narration = "";
              int subNo = 1;
              int currInst = 0;
              String recoveryId = "";
              double totalRecoveryBal = 0.0;
              if(finalMap != null && finalMap.containsKey("SALARYRECOVERY_MDS_POST_LIST") && finalMap.get("SALARYRECOVERY_MDS_POST_LIST") != null){
                  recoveryList = (ArrayList) finalMap.get("SALARYRECOVERY_MDS_POST_LIST");
                  if(recoveryList != null && recoveryList.size() > 0){
                      for(int i = 0; i<recoveryList.size(); i++){
                          List transDetailsList = new ArrayList();
                          instBonusMap =  new LinkedHashMap();
                          mdsBonusMap = new HashMap();
                          dataMap = new HashMap();
                          singleRowList = (ArrayList) recoveryList.get(i);
                          System.out.println("singleRowList checking by nithya :: " + singleRowList);
                          //[SAL00080, 406, MDS, 0001406000016, 30, 1710, 1700, 0, 10, 0, 300, 0, 1710, , C010001393]   
                          //13,14 - cust 
                          schemeName = CommonUtil.convertObjToStr(singleRowList.get(1));
                          chittalNo = CommonUtil.convertObjToStr(singleRowList.get(3));
                          currInst = CommonUtil.convertObjToInt(singleRowList.get(4));
                          recoveryId = CommonUtil.convertObjToStr(singleRowList.get(0)); 
                          HashMap recoveryMap = new HashMap();
                          recoveryMap.put("CUST_ID",singleRowList.get(14));
                          recoveryMap.put("RECOVERY_ID", recoveryId);
                          recoveryMap.put("INT_CALC_UPTO_DT", CommonUtil.getProperDate(currDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("CALC_INT_UPTO_DT")))));
                          List list = null;
                          list = sqlMap.executeQueryForList("getCustomerRecoveryBalance", recoveryMap);
                          if (list != null && list.size() > 0) {
                              recoveryMap = (HashMap) list.get(0);
                              totalRecoveryBal = CommonUtil.convertObjToDouble(recoveryMap.get("TOTAL_RECOVERY_AMT"));
                              if(recoveryMap.containsKey("TOTAL_RECOVERY_AMT") && recoveryMap.get("TOTAL_RECOVERY_AMT") != null){
                                  totalRecoveryBal = CommonUtil.convertObjToDouble(recoveryMap.get("TOTAL_RECOVERY_AMT"));
                                  System.out.println("inside if ... further execution :: " + totalRecoveryBal);
                              }else{
                                  totalRecoveryBal = CommonUtil.convertObjToDouble(singleRowList.get(13));
                                  System.out.println("inside if ... first execution :: " + singleRowList.get(13));
                              }
                          }     
                          //[SAL00084, 406, MDS, 0001406000016, 30, 1710, 1700, 0, 0, 0, 300, 0, 1700.0, 3000, C010001393]
                          if(totalRecoveryBal >= CommonUtil.convertObjToDouble(singleRowList.get(12))){                          
                          HashMap chittalMap = new HashMap();
                          chittalMap.put("CHITTAL_NO", chittalNo);
                          chittalMap.put("CURRDT", currDt.clone());
                          List chittalDataList = sqlMap.executeQueryForList("getChittalRecoveryDetails", chittalMap);
                          if (chittalDataList != null && chittalDataList.size() > 0) {
                              chittalMap = (HashMap) chittalDataList.get(0);
                              divisionNo = CommonUtil.convertObjToStr(chittalMap.get("DIVISION_NO"));
                              instAmt = CommonUtil.convertObjToDouble(chittalMap.get("INST_AMT"));
                              subNo = CommonUtil.convertObjToInt(chittalMap.get("SUB_NO"));
                              instPaid = CommonUtil.convertObjToInt(chittalMap.get("INST_PAID"));
                              narration = CommonUtil.convertObjToStr(chittalMap.get("NARRATION"));
                              name = CommonUtil.convertObjToStr(chittalMap.get("MEMBER_NAME"));
                          }
                          List standingList = new ArrayList();
                          data = new HashMap();
                          data.put("DIVISION_NO",divisionNo);
                          data.put("NARRATION",narration);
                          data.put("SUB_NO",CommonUtil.convertObjToInt(subNo));
                          data.put("INITIATED_BRANCH",_branchCode);
                          data.put("FORFEIT_BONUS",new Double(0.0));
                          data.put("INST_AMT",instAmt);
                          data.put("PAID_INST",instPaid);
                          data.put("PROD_ID",debitProdId);/////
                          data.put("MEMBER_NAME",name);                          
                          data.put("CURR_INST",singleRowList.get(4));
                          data.put("NO_OF_INST_PAY",1);
                          data.put("MIN_BALANCE",new Double(0.0));
                          data.put("DISCOUNT",new Double(0.0));
                          data.put("PRIZED_MEMBER","");  
                          data.put("PENDING_DUE_AMT",singleRowList.get(5));
                          data.put("INSTALLMENT_DATE","");
                          data.put("CHITTAL_NO",chittalNo);
                          double netAmt = CommonUtil.convertObjToDouble(singleRowList.get(12));
                          data.put("NET_AMOUNT",netAmt);
                          data.put("DR_ACT_NO",debitAcctNo);                          
                          data.put("NO_OF_INSTALLMENTS",1);
                          data.put("BONUS_NEW",new Double(0.0));
                          data.put("PENAL",singleRowList.get(8));
                          //data.put("PENAL",10.0);
                          double payable = CommonUtil.convertObjToDouble(singleRowList.get(5));
                          data.put("INST_AMT_PAYABLE",singleRowList.get(5));
                          data.put("PAID_DATE",currDt.clone());                          
                          data.put("PENDING_INST",1);                          
                          data.put("PROD_TYPE",debitProdType);
                          data.put("CHIT_START_DT","");
                          data.put("AVAILABLE_BALANCE",new Double(0.0));
                          data.put("BONUS",singleRowList.get(10));    
                          data.put("FORFEIT_RECOVERY",singleRowList.get(11)); 
                          standingList.add(data);
                          dataMap = new HashMap();
                          dataMap.put("SCHEME_NAME", schemeName);
                          dataMap.put("MDS_STANDING_INSTRUCTION",standingList);
                          dataMap.put("BRANCH_CODE",_branchCode);
                          //dataMap.put("MDS_BONUS_MAP",null);
                          dataMap.put("USER_ID",map.get("USER_ID"));
                          dataMap.put("FROM_MDS_RECOVERY_SCREEN","FROM_MDS_RECOVERY_SCREEN");
                          
                          //KD-3656 - Implementing advance collection in MDS recovery
                          instBonusMap.put(instPaid+1,singleRowList.get(10));
                          mdsBonusMap.put(chittalNo,instBonusMap);
                          dataMap.put("MDS_BONUS_MAP",mdsBonusMap);
                          
                          MDSStandingInstructionDAO dao =  new MDSStandingInstructionDAO();
                          HashMap returnMDSMap = dao.execute(dataMap);
                          System.out.println("returnMDSMAp :: " + returnMDSMap);
                          if (returnMDSMap != null && returnMDSMap.containsKey("TRANSACTION_DETAILS") && returnMDSMap.get("TRANSACTION_DETAILS") != null) {
                              transDetailsList = (ArrayList) returnMDSMap.get("TRANSACTION_DETAILS");
                              if (transDetailsList != null && transDetailsList.size() > 0) {
                                  String transId = CommonUtil.convertObjToStr(transDetailsList.get(0));
                                  if (transId != null && transId.length() > 0) {
                                      HashMap voucherMap = new HashMap();
                                      voucherMap.put("ACT_NUM", chittalNo);
                                      voucherMap.put("VOUCHER_RELEASE_DATE", currDt.clone());
                                      voucherMap.put("VOUCHER_RELEASE_BATCH_ID", transId);
                                      voucherMap.put("INT_CALC_UPTO_DT", CommonUtil.getProperDate(currDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("CALC_INT_UPTO_DT")))));
                                      System.out.println("totalRecoveryBal :: " + totalRecoveryBal +" netamount :: "+ netAmt);
                                      voucherMap.put("BALANCE_RECOVERED_AMOUNT", totalRecoveryBal - netAmt);
                                      voucherMap.put("RECOVERED_AMOUNT", netAmt);
                                      voucherMap.put("INST_NO",currInst);
                                      voucherMap.put("RECOVERY_ID", recoveryId);
                                      sqlMap.executeUpdate("updateMDSVoucherReleaseDetails", voucherMap);
                                      sqlMap.executeUpdate("updateCustomerTotalRecoveryAmount", voucherMap);
                                  }
                              }
                          }
                        }  
                      }
                      HashMap listGenerationMap = new HashMap();
                      listGenerationMap.put("RECOVERY_ID", map.get("RECOVERY_ID"));
                      List listGeneration = sqlMap.executeQueryForList("getLoanRecoveryListGenerationProcessed", listGenerationMap);
                      if (!(listGeneration != null && listGeneration.size() > 0)) {
                          sqlMap.executeUpdate("updateLoanRecoveryListGenerationMaster", listGenerationMap);
                      }
                  } 
              }
              sqlMap.commitTransaction();
          }catch(Exception e){
              sqlMap.rollbackTransaction();
              e.printStackTrace();
              throw e;
          }
      }
      
      
     private LinkedHashMap calcRDPayments(String depositNo,String prodId,String prodType, Date checkDt) throws SQLException {
         int pendingInst,pendingInstl;
         double ROI = 0,chargeAmt = 0,calcAmt = 0;
         double PENAL = 0.0;
         int delayMonth = 0;
         LinkedHashMap dataListMap = new LinkedHashMap();
         HashMap dataMap;
         HashMap calcMap;
         HashMap lastMap = new HashMap();
         if (depositNo.lastIndexOf("_") != -1) {
             depositNo = depositNo.substring(0, depositNo.lastIndexOf("_"));
         }
         HashMap recurringMap = new HashMap();
         recurringMap.put("PROD_ID", prodId);
         List recurringLst = sqlMap.executeQueryForList("getRecurringDepositDetails", recurringMap);
         if (recurringLst != null && recurringLst.size() > 0) {
             recurringMap = (HashMap) recurringLst.get(0);
         }
         HashMap accountMap = new HashMap();
         accountMap.put("DEPOSIT_NO", depositNo);
         accountMap.put("BRANCH_ID", _branchCode);
         List depositAccLst = sqlMap.executeQueryForList("getProductIdForDeposits", accountMap);
         if (depositAccLst != null && depositAccLst.size() > 0) {
             accountMap = (HashMap) depositAccLst.get(0);
         }
        double depAmt = CommonUtil.convertObjToDouble(accountMap.get("DEPOSIT_AMT")).doubleValue();
        chargeAmt = depAmt/100;
        lastMap.put("DEPOSIT_NO", depositNo);
//        List lst = sqlMap.executeQueryForList("getInterestDeptIntTable", lastMap);
//        if (lst != null && lst.size() > 0) {
//            lastMap = (HashMap) lst.get(0);
//            double tot_Inst_paid = CommonUtil.convertObjToDouble(lastMap.get("TOTAL_INSTALL_PAID")).doubleValue();
//            Date depDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lastMap.get("DEPOSIT_DT")));
//            int gracePeriod = CommonUtil.convertObjToInt(recurringMap.get("GRACE_PERIOD"));
//            depDate = DateUtil.addDays(depDate, gracePeriod);
//            HashMap penaltyMap = new HashMap();
//            HashMap penalInstMap = new HashMap();
//            penaltyMap.put("DEPOSIT_DT", setProperDtFormat(depDate));
//            penaltyMap.put("CURR_DT", setProperDtFormat((Date) currDt.clone()));
//            penaltyMap.put("FREQ", 1);
//            penaltyMap.put("PAID_INST", tot_Inst_paid); // Penal Calculation for RD
//            List penaltyList = sqlMap.executeQueryForList("getPenaltyForDeposit", penaltyMap);     //Based On Query Penaty Calculation
//            if (penaltyList != null && penaltyList.size() > 0) {
//                penalInstMap = (HashMap) penaltyList.get(0);
//                int penalInstallments = CommonUtil.convertObjToInt(penalInstMap.get("NO_OF_PENDING_INST"));
//                penalInstMap = new HashMap();
//                for (int p = 1; p <= penalInstallments; p++) {// Added by nithya on 07-05-2020 for KD-1724
//                    penalInstMap.put(p, p);
//                }
//            }
//            System.out.println("penal Inst Map ::" + penalInstMap);
//            depDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lastMap.get("DEPOSIT_DT")));
//            penaltyMap.put("DEPOSIT_DT", setProperDtFormat(depDate));
//            penaltyList = sqlMap.executeQueryForList("getPenaltyForDeposit", penaltyMap);
//            if (penaltyList != null && penaltyList.size() > 0) {
//                penaltyMap = (HashMap) penaltyList.get(0);
//                if (CommonUtil.convertObjToInt(penaltyMap.get("NO_OF_PENDING_INST")) > 0) {
//                    pendingInst = CommonUtil.convertObjToInt(penaltyMap.get("NO_OF_PENDING_INST"));
//                    pendingInstl = pendingInst;
//                    System.out.println("############ NO_OF_PENDING_INST : " + pendingInst);
//                    calcMap = new HashMap();
//                    for (int j = 1; j <= pendingInst; j++) {
//                        calcMap.put(j, pendingInstl);
//                        pendingInstl = pendingInstl - 1;
//                    }
//                    HashMap delayMap = new HashMap();
//                    delayMap.put("PROD_ID", accountMap.get("PROD_ID"));
//                    delayMap.put("DEPOSIT_AMT", accountMap.get("DEPOSIT_AMT"));
//                    lst = sqlMap.executeQueryForList("getSelectDelayedRate", delayMap);
//                    if (lst != null && lst.size() > 0) {
//                        delayMap = (HashMap) lst.get(0);
//                        System.out.println("####### delayMap : " + delayMap);
//                        ROI = CommonUtil.convertObjToDouble(delayMap.get("ROI")).doubleValue();
//                        calcAmt = ROI * chargeAmt;
//                        System.out.println("######## calcAmt " + calcAmt);
//                    }
//                    System.out.println("######## CALC_Map " + calcMap);
//                    if (calcMap != null && calcMap.size() > 0) {
//                        delayMonth = 0;
//                        for (int z = 1; z <= calcMap.size(); z++) {
//                            PENAL = 0.0;   
//                            delayMonth = CommonUtil.convertObjToInt(calcMap.get(z));
//                            System.out.println("############ DELAY_MONTH : " + delayMonth);
//                            if (delayMonth > 0) {
//                                if (penalInstMap.containsKey(z)) { // Added by nithya on 07-05-2020 for KD-1724
//                                    PENAL = calcAmt * CommonUtil.convertObjToDouble(delayMonth);
//                                    PENAL = (double) getNearest((long) (PENAL * 100), 100) / 100;
//                                } else {
//                                    PENAL = 0.0;
//                                }
//                            }
//                            System.out.println("PENAL ::" + PENAL +" Delayed Month :: " + delayMonth);
//                        }
//                    }
//                }
//            }
//        }
//        
        int gracePeriod = CommonUtil.convertObjToInt(recurringMap.get("GRACE_PERIOD"));
         /// Calculte RD installments and penal
         HashMap delayMap = new HashMap();
         delayMap.put("PROD_ID", accountMap.get("PROD_ID"));
         delayMap.put("DEPOSIT_AMT", accountMap.get("DEPOSIT_AMT"));
         List lst = sqlMap.executeQueryForList("getSelectDelayedRate", delayMap);
         if (lst != null && lst.size() > 0) {
             delayMap = (HashMap) lst.get(0);
             System.out.println("####### delayMap : " + delayMap);
             ROI = CommonUtil.convertObjToDouble(delayMap.get("ROI")).doubleValue();
             calcAmt = ROI * chargeAmt;
             System.out.println("######## calcAmt " + calcAmt);
         }
        int monthsDelay = 0;
        double penalAmt = 0.0;
        int instNo = 0;
         HashMap rdDetailsMap = new HashMap();
         rdDetailsMap.put("ASON", checkDt);
         rdDetailsMap.put("DEPOSIT_NO", depositNo);
         rdDetailsMap.put("GRACE_PERIOD",gracePeriod);
         List rdList = sqlMap.executeQueryForList("getRDRecoveryDueDetails", rdDetailsMap);
         if (rdList != null && rdList.size() > 0) {
             for (int i = 0; i < rdList.size(); i++) {
                 rdDetailsMap = (HashMap) rdList.get(i);
                 monthsDelay = CommonUtil.convertObjToInt(rdDetailsMap.get("DELAYED_MONTHS"));
                 instNo = CommonUtil.convertObjToInt(rdDetailsMap.get("SL_NO"));
                 penalAmt = ROI * chargeAmt * monthsDelay;
                 System.out.println("penalAmt :: " + penalAmt + " instNo :: " + instNo);
                 if(penalAmt < 0){
                     penalAmt = 0.0;
                 }
                 dataMap = new HashMap();
                 dataMap.put("DEPOSIT_NO", depositNo);
                 dataMap.put("PROD_ID", prodId);
                 dataMap.put("CHARGES", "0");
                 dataMap.put("INSTALLMENT_DATE", rdDetailsMap.get("DUE_DATE"));
                 dataMap.put("NO_OF_INSTALLMENTS", 1);
                 dataMap.put("CURR_INST", instNo);
                 dataMap.put("PENDING_INST", instNo);
                 dataMap.put("PENDING_DUE_AMT", depAmt + penalAmt);
                 dataMap.put("NO_OF_INST_PAY", String.valueOf(1));
                 dataMap.put("PRINCIPAL", String.valueOf(depAmt));
                 dataMap.put("INTEREST", "0");
                 dataMap.put("PENAL", penalAmt);
                 dataMap.put("TOTAL_DEMAND", new Double(depAmt + penalAmt));
                 dataMap.put("CLEAR_BALANCE", new Double(0));
                 dataListMap.put(instNo, dataMap);
             }      
         }

         // End
        return dataListMap;
    }

    
    public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
        if ((mod <= (roundingFactor / 2)) || (mod <= (roundingFactorOdd / 2))) {
            return lower(number, roundingFactor);
        } else {
            return higher(number, roundingFactor);
        }
    }
         
       public long lower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
       }

    public long higher(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        if (mod == 0) {
            return number;
        }
        return (number - mod) + roundingFactor;
    }

     private HashMap doRDPayment(HashMap dataMap) throws Exception {
         HashMap transMap =  new HashMap();
        try {
            System.out.println("inside doRDPayment :: " + dataMap);
            String ACCOUNTNO = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));
            if (ACCOUNTNO.lastIndexOf("_") != -1) {
                ACCOUNTNO = ACCOUNTNO;
            } else {
                ACCOUNTNO = ACCOUNTNO + "_1";
            }
            TransferTrans objTransferTrans = new TransferTrans();
            objTransferTrans.setInitiatedBranch(_branchCode);
            HashMap txMap = new HashMap();
            ArrayList transferList = new ArrayList();
            txMap.put(TransferTrans.PARTICULARS, "To " + ACCOUNTNO + " ");
            txMap.put(CommonConstants.USER_ID, dataMap.get("USER_ID"));
            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
            // In case the Debit Account type is GL
            txMap.put(TransferTrans.DR_AC_HD, dataMap.get(TransferTrans.DR_AC_HD));  // Give the Debit GL Head here
            txMap.put(TransferTrans.DR_PROD_TYPE, dataMap.get(TransferTrans.DR_PROD_TYPE));
            txMap.put(TransferTrans.DR_ACT_NUM, dataMap.get(TransferTrans.DR_ACT_NUM));
            txMap.put("TRANS_MOD_TYPE", dataMap.get(TransferTrans.DR_PROD_TYPE));
            txMap.put(TransferTrans.DR_INSTRUMENT_1, "RECOVERY");
            txMap.put("GL_TRANS_ACT_NUM", dataMap.get("AC_NO"));
            txMap.put("LINK_BATCH_ID", ACCOUNTNO);
            txMap.put("generateSingleTransId", dataMap.get("generateSingleTransId"));
            txMap.put(TransferTrans.NARRATION, "RD Recovery");
            
            double debitAmt = CommonUtil.convertObjToDouble(dataMap.get("DEPOSIT_AMT"));
            double penalAmt = 0.0;
            if (dataMap.get("DEPOSIT_PENAL_AMT") != null) {
                penalAmt = CommonUtil.convertObjToDouble(dataMap.get("DEPOSIT_PENAL_AMT"));
            }
            debitAmt += penalAmt;
            TxTransferTO objTxTransferTO = objTransferTrans.getDebitTransferTO(txMap, debitAmt); 
            objTxTransferTO.setRec_mode("RP");
            transferList.add(objTxTransferTO);
            //Added by nithya on 13-01-2021 for Kd-3260
            HashMap interBranchCodeMap = new HashMap();
            interBranchCodeMap.put("ACT_NUM", ACCOUNTNO);
            List interBranchCodeList = sqlMap.executeQueryForList("getSelectInterBranchCode", interBranchCodeMap);
            String acctBranchId = "";
            if (interBranchCodeList != null && interBranchCodeList.size() > 0) {
                interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                //System.out.println("interBranchCodeMap : " + interBranchCodeMap);
                if(interBranchCodeMap.containsKey("BRANCH_CODE") && interBranchCodeMap.get("BRANCH_CODE") != null){
                  acctBranchId = CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"));
                }
            }
            if(!acctBranchId.equals(_branchCode)){
              txMap.put(TransferTrans.CR_BRANCH, acctBranchId);  
            }else{
              txMap.put(TransferTrans.CR_BRANCH, _branchCode);
            }
            if (!(acctBranchId.equalsIgnoreCase(_branchCode))) {// Added by nithya on 21-03-2019
                txMap.put("SALARY_PROCESS_RD_INTER_BRANCH_TRANS", "SALARY_PROCESS_RD_INTER_BRANCH_TRANS");
                txMap.put("INITIATED_BRANCH", _branchCode);
            }

            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
            txMap.put(TransferTrans.CR_AC_HD,dataMap.get(TransferTrans.CR_AC_HD));
            txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
            txMap.put(TransferTrans.CR_PROD_ID, dataMap.get("PROD_ID"));
            txMap.put(TransferTrans.CR_ACT_NUM, ACCOUNTNO);
            txMap.put("AUTHORIZEREMARKS", "");  // Any remarks
            txMap.put("GL_TRANS_ACT_NUM", dataMap.get("AC_NO"));
            txMap.put("LINK_BATCH_ID", ACCOUNTNO);
            txMap.put("generateSingleTransId", dataMap.get("generateSingleTransId"));
            // double creditAmt = debitAmt;// Give whichever amount wanted to Credit Ex: 5 * 500 (5 months due) + 50 (penal amount)
            objTxTransferTO = objTransferTrans.getCreditTransferTO(txMap, debitAmt);
            objTxTransferTO.setRec_mode("RP");
            transferList.add(objTxTransferTO);
            HashMap tansactionMap = new HashMap();
            tansactionMap.put("SCREEN", "PAYROLL");
            tansactionMap.put("MODULE", "Transaction");
            tansactionMap.put(CommonConstants.BRANCH_ID, _branchCode);
            tansactionMap.put(CommonConstants.USER_ID, dataMap.get("USER_ID"));
            tansactionMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
            tansactionMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
            tansactionMap.put("TxTransferTO", transferList);
            if (dataMap.get("DEPOSIT_PENAL_AMT") != null) {
                // At the time of authorization pass the following values
                tansactionMap.put("DEPOSIT_PENAL_AMT", dataMap.get("DEPOSIT_PENAL_AMT"));
                tansactionMap.put("DEPOSIT_PENAL_MONTH", dataMap.get("SL_NO"));
            }
            tansactionMap.put("FROM_RD_RECOVERY_SCREEN","FROM_RD_RECOVERY_SCREEN");
            // This map should be set if authorization should happen immediately
            HashMap authorizeMap = new HashMap();
            authorizeMap.put("USER_ID", dataMap.get("USER_ID"));
            authorizeMap.put("BATCH_ID", null);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
            tansactionMap.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
            System.out.println("tansactionMap here :: " + tansactionMap);
            TransferDAO objTransferDAO = new TransferDAO();
            transMap = objTransferDAO.execute(tansactionMap, false);
            System.out.println("transmap after RD process:: " + transMap);
            
            objTransferTrans = null;
            objTransferDAO = null;
            tansactionMap.clear();
            tansactionMap = null;
            transferList.clear();
            transferList = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
        return transMap;
    }
 
    public void processRDTransactionPart(HashMap map, HashMap transactionMap) throws Exception {
        try {
  
            sqlMap.startTransaction();
            System.out.println("inside processRDTransactionPart :: " + map);
            List singleRowList = null;
            String recoveryId = "";
            int currInst = 0;
            double totalRecoveryBal = 0.0;
            HashMap dataMap;
            /*
             inside processRDTransactionPart :: {DB_DRIVER_NAME=oracle.jdbc.driver.OracleDriver, CALC_INT_UPTO_DT=Thu Jun 30 00:00:00 IST 2022, IP_ADDR=192.168.0.103, USER_ID=admin,
             TRANSACTION_DETAILS_DATA={DR_ACT_NUM=0001703000007, TRANS_TYPE=TRANSFER, TRANSACTION_PART=TRANSACTION_PART, USER_ID=admin, DR_PROD_ID=703, DR_PROD_TYPE=SA},
             RECOVERY_ID=SAL00115, SESSION_ID=SI83544, BANK_CODE=0001, BRANCH_CODE=0001, RECOVERY_RD_PROCESS_LIST={SALARYRECOVERY_RD_POST_LIST=[[SAL00115, 108, TD, 0001108004052_1, 4, 520, 500, 0, 20, 0, 520, , C010002600], 
             [SAL00115, 108, TD, 0001108004052_1, 5, 510, 500, 0, 10, 0, 510, , C010002600]]}}
             */
            HashMap finalMap = new HashMap();
            finalMap = (HashMap) map.get("RECOVERY_RD_PROCESS_LIST");
            List recoveryList;
            //[SAL00115, 108, TD, 0001108004052_1, 4, 520, 500, 0, 20, 0, 520, , C010002600]
            // TRANSACTION_DETAILS_DATA={DR_ACT_NUM=0001703000007, TRANS_TYPE=TRANSFER, TRANSACTION_PART=TRANSACTION_PART, 
            //USER_ID=admin, DR_PROD_ID=703, DR_PROD_TYPE=SA}

            String debitProdType = CommonUtil.convertObjToStr(transactionMap.get("DR_PROD_TYPE"));
            String debitProdId = CommonUtil.convertObjToStr(transactionMap.get("DR_PROD_ID"));
            String debitAcctNo = CommonUtil.convertObjToStr(transactionMap.get("DR_ACT_NUM"));
            String divisionNo = "";

            //Fetch Account heads            
            HashMap debitMap = new HashMap();
            if (transactionMap != null && transactionMap.get("DR_PROD_TYPE").equals("OA")) {
                debitMap.put("ACT_NUM", CommonUtil.convertObjToStr(transactionMap.get("DR_ACT_NUM")));
                List headList = sqlMap.executeQueryForList("getAccNoProdIdDet", debitMap);
                if (headList != null && headList.size() > 0) {
                    debitMap = (HashMap) headList.get(0);
                    //transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(transactionMap.get("DR_ACT_NUM")));
                }
            }
            if (transactionMap != null && transactionMap.get("DR_PROD_TYPE").equals("SA")) {
                HashMap susMap = new HashMap();                // Suspense Acc Head
                susMap.put("PROD_ID", CommonUtil.convertObjToStr(transactionMap.get("DR_ACT_NUM")).substring(4, 7));
                List susLst = sqlMap.executeQueryForList("getAccountHeadProdSA", susMap);
                if (susLst != null && susLst.size() > 0) {
                    debitMap = (HashMap) susLst.get(0);
                    //transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(transactionMap.get("DR_ACT_NUM")));
                }
            }

            // end
            if (finalMap != null && finalMap.containsKey("SALARYRECOVERY_RD_POST_LIST") && finalMap.get("SALARYRECOVERY_RD_POST_LIST") != null) {
                recoveryList = (ArrayList) finalMap.get("SALARYRECOVERY_RD_POST_LIST");
                if (recoveryList != null && recoveryList.size() > 0) {
                    for (int i = 0; i < recoveryList.size(); i++) {
                        singleRowList = (ArrayList) recoveryList.get(i);
                        System.out.println("single RD row : " + singleRowList);
                        recoveryId = CommonUtil.convertObjToStr(singleRowList.get(0)); 
                        currInst =  CommonUtil.convertObjToInt(singleRowList.get(4));
                        HashMap recoveryMap = new HashMap();
                        recoveryMap.put("CUST_ID", singleRowList.get(12));
                        recoveryMap.put("RECOVERY_ID", singleRowList.get(0));
                        recoveryMap.put("INT_CALC_UPTO_DT", CommonUtil.getProperDate(currDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("CALC_INT_UPTO_DT")))));
                        List list = null;
                        list = sqlMap.executeQueryForList("getCustomerRecoveryBalance", recoveryMap);
                        if (list != null && list.size() > 0) {
                            recoveryMap = (HashMap) list.get(0);
                            totalRecoveryBal = CommonUtil.convertObjToDouble(recoveryMap.get("TOTAL_RECOVERY_AMT"));
                            if (recoveryMap.containsKey("TOTAL_RECOVERY_AMT") && recoveryMap.get("TOTAL_RECOVERY_AMT") != null) {
                                totalRecoveryBal = CommonUtil.convertObjToDouble(recoveryMap.get("TOTAL_RECOVERY_AMT"));
                                System.out.println("inside if ... further execution :: " + totalRecoveryBal);
                            } else {
                                totalRecoveryBal = CommonUtil.convertObjToDouble(singleRowList.get(11));
                                System.out.println("inside if ... first execution :: " + singleRowList.get(11));
                            }
                        }
                        if (totalRecoveryBal >= CommonUtil.convertObjToDouble(singleRowList.get(10))) {
                            dataMap = new HashMap();
                            //[SAL00115, 108, TD, 0001108004052_1, 4, 520, 500, 0, 20, 0, 520, , C010002600]
                            HashMap acHeadMap = new HashMap();
                            acHeadMap.put("PROD_ID", singleRowList.get(1));
                            List lst = sqlMap.executeQueryForList("getAccountHeadProdTD", acHeadMap);
                            if (lst != null && lst.size() > 0) {
                                acHeadMap = (HashMap) lst.get(0);
                            }
                            dataMap.put("ACT_NUM", singleRowList.get(3));
                            dataMap.put("USER_ID", map.get("USER_ID"));
                            dataMap.put(TransferTrans.DR_ACT_NUM,debitAcctNo);
                            dataMap.put(TransferTrans.DR_PROD_TYPE,debitProdType);
                            dataMap.put(TransferTrans.DR_PROD_ID,debitProdId);
                            dataMap.put(TransferTrans.DR_AC_HD, debitMap.get("AC_HEAD"));
                            dataMap.put(TransferTrans.CR_AC_HD, acHeadMap.get("AC_HEAD"));
                            dataMap.put("AC_NO", singleRowList.get(3));
                            dataMap.put("DEPOSIT_AMT", singleRowList.get(6));
                            dataMap.put("DEPOSIT_PENAL_AMT", singleRowList.get(8));
                            dataMap.put("PROD_ID", singleRowList.get(1));
                            dataMap.put("SL_NO", singleRowList.get(4));
                            dataMap.put("generateSingleTransId", generateLinkID());
                            HashMap transMap = doRDPayment(dataMap);
                            if (transMap != null && transMap.containsKey("TRANS_ID") && transMap.get("TRANS_ID") != null) {
                                HashMap voucherMap = new HashMap();
                                voucherMap.put("ACT_NUM", singleRowList.get(3));
                                voucherMap.put("VOUCHER_RELEASE_DATE", currDt.clone());
                                voucherMap.put("VOUCHER_RELEASE_BATCH_ID", transMap.get("TRANS_ID"));
                                voucherMap.put("INT_CALC_UPTO_DT", CommonUtil.getProperDate(currDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("CALC_INT_UPTO_DT")))));
                                //System.out.println("totalRecoveryBal :: " + totalRecoveryBal + " netamount :: " + netAmt);
                                voucherMap.put("BALANCE_RECOVERED_AMOUNT", totalRecoveryBal - CommonUtil.convertObjToDouble(singleRowList.get(10)));
                                voucherMap.put("RECOVERED_AMOUNT", CommonUtil.convertObjToDouble(singleRowList.get(10)));
                                voucherMap.put("INST_NO",currInst);
                                voucherMap.put("RECOVERY_ID", recoveryId);
                                sqlMap.executeUpdate("updateMDSVoucherReleaseDetails", voucherMap);
                                sqlMap.executeUpdate("updateCustomerTotalRecoveryAmount", voucherMap);
                            }
                        }
                    }
                    HashMap listGenerationMap = new HashMap();
                    listGenerationMap.put("RECOVERY_ID", map.get("RECOVERY_ID"));
                    List listGeneration = sqlMap.executeQueryForList("getLoanRecoveryListGenerationProcessed", listGenerationMap);
                    if (!(listGeneration != null && listGeneration.size() > 0)) {
                        sqlMap.executeUpdate("updateLoanRecoveryListGenerationMaster", listGenerationMap);
                    }
                }
            }
            sqlMap.commitTransaction();

        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }
     
    private void recoveryAmountUpdationProcess(HashMap map) throws SQLException{
        try{
            sqlMap.startTransaction();
            System.out.println("inside recoveryAmountUpdationProcess :: " + map);
            List singleRowList = null;
            List recoveryList;
            String recoveryId = "";
            int currInst = 0;
            String custId = "";
            double totalRecoveryAmt = 0.0;
            HashMap dataMap;
          
            HashMap finalMap = new HashMap();
            finalMap = (HashMap) map.get("RECOVERY_AMT_UPDATE_LIST");
            if (finalMap != null && finalMap.containsKey("SALARYRECOVERY_AMT_UPDATE_LIST") && finalMap.get("SALARYRECOVERY_AMT_UPDATE_LIST") != null) {
                recoveryList = (ArrayList) finalMap.get("SALARYRECOVERY_AMT_UPDATE_LIST");
                if (recoveryList != null && recoveryList.size() > 0) {
                    for (int i = 0; i < recoveryList.size(); i++) {
                        singleRowList = (ArrayList) recoveryList.get(i);
                        System.out.println("single RD row : " + singleRowList);
                        //single RD row : [SAL00150, C010002771, AA494, RAMESHA K, 25000]
                        recoveryId = CommonUtil.convertObjToStr(singleRowList.get(0)); 
                        custId = CommonUtil.convertObjToStr(singleRowList.get(1)); 
                        totalRecoveryAmt = CommonUtil.convertObjToDouble(singleRowList.get(4));
                     
                        HashMap updateMap = new HashMap();
                        updateMap.put("RECOVERY_AMOUNT",totalRecoveryAmt);
                        updateMap.put("CUST_ID",custId);
                        updateMap.put("RECOVERY_ID",recoveryId);
                        updateMap.put("INT_CALC_UPTO_DT", CommonUtil.getProperDate(currDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("CALC_INT_UPTO_DT")))));
                                     
                        sqlMap.executeUpdate("updateAllCustomerTotalRecoveryAmount", updateMap);
                        //updateAllCustomerTotalRecoveryAmount
                    }
                }
            }
            sqlMap.commitTransaction();
            System.out.println("Execute after commit ");
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
            
    }
    
    
}
