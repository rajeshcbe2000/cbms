
/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AccountClosingDAO.java
 *
 * Created on September 08, 2003, 1:00 PM
 */
package com.see.truetransact.serverside.operativeaccount;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Date;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.*;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.transferobject.operativeaccount.*;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.transferobject.termloan.TermLoanInterestTO;
import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;
import com.see.truetransact.transferobject.termloan.TermLoanFacilityTO;  
import com.see.truetransact.serverside.batchprocess.task.operativeaccount.interest.InterestTask;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.serverside.deposit.lien.DepositLienDAO;
import com.see.truetransact.transferobject.deposit.lien.DepositLienTO;
import com.see.truetransact.serverside.remittance.RemittanceIssueDAO;
import com.see.truetransact.transferobject.remittance.RemittanceIssueTO;
import org.apache.log4j.Logger;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.transaction.common.product.suspense.SuspenseTransaction;
import com.see.truetransact.serverside.transaction.rollback.RollBackDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import com.see.truetransact.transferobject.suspenseaccount.SuspenseAccountMasterTO;
import com.see.truetransact.transferobject.termloan.chargesTo.TermLoanChargesTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * This is used for AccountClosing Data Access.
 *
 * @author Karthik @modified Sunil Changes done : Added Transaction Tab Account
 * Head transfer enabled
 */
public class AccountClosingDAO extends TTDAO {

    private final String PRODUCTTYPE = "PRODUCTTYPE";
    private final String TRANS_ID = "TRANS_ID";
    private final String CASH = "CASH";
    private String commandMode = CommonConstants.TOSTATUS_INSERT;
    private AccountClosingTO accountClosingTO;
    private RemittanceIssueDAO remittanceIssueDAO;
    private RemittanceIssueTO remittanceIssueTO;
    private AccountTO accountTO;
    private TermLoanFacilityTO termFacilityTo;
    private SqlMap sqlMap;
    private TransactionDAO transactionDAO = null;
    private final static Logger log = Logger.getLogger(AccountClosingDAO.class);
    private String prodType = "";
    private double lienAmount;
    private double freezeAmount;
    private HashMap acHeads = new HashMap();
    private HashMap loanMap = null;
    private TermLoanInterestTO termLoanTO;
    private String depType = null;
    private HashMap resultMap = null;
    private String acno = "";
    private String inst_type = "",chqno2="";
    private String transmode = "";
    private String asAnWhen = "";
    public List chargeLst = null;
    TransactionTO tto = null;
    LinkedHashMap transactionDetailsMap = new LinkedHashMap();
    TransactionTO transactionTODebit = new TransactionTO();
    Date currDt = null;
    private String productBehaveLike = "";
    private Double advancesCreditInterest = null;
    private String generateSingleTransId = "";
    private String isLoanCloseThroughGldLnAlwd = "";
    private byte[] photoByteArray;
    private String driverName;
    private Connection conn;
    private Statement stmt;
    private ResultSet rset;
    private String cmd;
    private String dataBaseURL;
    private String userName;
    private String passWord;
    private String SERVER_ADDRESS;
    private String tableName;
    private String tableCondition;
    private File photoFile;
    private FileOutputStream writer = null;
    private HashMap mapPhotoSign;

    /**
     * Creates a new instance of AccountClosingDAO
     */
    public AccountClosingDAO() throws ServiceLocatorException {
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    /**
     * To insert data for each account head
     */
    public void insertAccHead(AccountClosingTO accountClosingTO, HashMap obj) throws Exception {
        double inttaxamt = 0.0;
        String branchID = CommonUtil.convertObjToStr(obj.get(CommonConstants.BRANCH_ID));
        //        HashMap actCloseChargeTransMap = new HashMap();
        //        HashMap actChargeDetTransMap = new HashMap();
        //        if (obj.containsValue("ACT_CLOSE_CHRG_TRANS_DETAILS"))
        //            actCloseChargeTransMap = (HashMap) obj.get("ACT_CLOSE_CHRG_TRANS_DETAILS");
        //        if (obj.containsValue("CHRG_DETAILS_TRANS_DETAILS"))
        //            actChargeDetTransMap = (HashMap) obj.get("CHRG_DETAILS_TRANS_DETAILS");
        productBehaveLike = CommonUtil.convertObjToStr(obj.get("BEHAVE_LIKE"));
        HashMap totAmt = new HashMap();
        if (prodType.equals("TermLoan")) {
            acHeads = (HashMap) sqlMap.executeQueryForObject("getLoanAccountClosingHeads", accountClosingTO.getActNum());
        } else {
            acHeads = (HashMap) sqlMap.executeQueryForObject("getAccountClosingHeads", accountClosingTO.getActNum());
        }
        HashMap txMap = new HashMap();
        transactionDAO.setInitiatedBranch(_branchCode);

        transactionDAO.setLinkBatchID(accountClosingTO.getActNum());
        System.out.println("acHeads:" + acHeads);

        TxTransferTO transferTo = null;
       
        if (getLienAmount() > 0) {
            // System.out.println("In liean code .........");
            txMap = new HashMap();
            List lienList = sqlMap.executeQueryForList("getLienAccountHead", accountClosingTO.getActNum());
            int lienRecords = lienList.size();
            if (lienRecords > 0) {
                // //System.out.println("in lien if .........");
                ArrayList transferList = new ArrayList(); // for local transfer
                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("AC_HD_ID"));
                txMap.put(TransferTrans.DR_BRANCH, branchID);
                txMap.put(TransferTrans.CURRENCY, "INR");
                txMap.put(TransferTrans.DR_ACT_NUM, accountClosingTO.getActNum());
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put("TRANS_MOD_TYPE", "OA");
                txMap.put("generateSingleTransId", generateSingleTransId);
                 if (prodType.equals("TermLoan")) {
                        txMap.put("SCREEN_NAME", "Loan Closing");
                    } else {
                        txMap.put("SCREEN_NAME", "Account Closing");
                    }   
                transferTo = transactionDAO.addTransferDebitLocal(txMap, getLienAmount());// to be got....
//                if (prodType.equals("TermLoan")) {
//                    transferTo.setScreenName("Loan Closing");
//                } else {
//                    transferTo.setScreenName("Account Closing");
//                }
                transferList.add(transferTo);

                for (int i = 0; i < lienRecords; i++) {
                    //TODO : we dont know how much amount has been repayed. (getLienAccounts)
                    txMap.put(TransferTrans.CR_PROD_ID, (String) ((HashMap) lienList.get(i)).get("PROD_ID"));
                    txMap.put(TransferTrans.CR_AC_HD, (String) ((HashMap) lienList.get(i)).get("LIEN_AC_HD"));
                    txMap.put(TransferTrans.CR_ACT_NUM, (String) ((HashMap) lienList.get(i)).get("LIEN_ACT_NUM"));
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                    txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put("TRANS_MOD_TYPE", "OA");
                    txMap.put("generateSingleTransId", generateSingleTransId);
                    if (prodType.equals("TermLoan")) {
                        txMap.put("SCREEN_NAME", "Loan Closing");
                    } else {
                        txMap.put("SCREEN_NAME", "Account Closing");
                    }    
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(((HashMap) lienList.get(i)).get("LIEN_ACT_NUM")).doubleValue());
                    if (prodType.equals("TermLoan")) {
                        transferTo.setScreenName("Loan Closing");
                    } else {
                        transferTo.setScreenName("Account Closing");
                    }
                    transferList.add(transferTo);
                }
                transactionDAO.doTransferLocal(transferList, branchID);
            }
        }

        if (CommonUtil.convertObjToDouble(accountClosingTO.getIntPayable()).doubleValue() > 0 && !prodType.equals("TermLoan")) {
            acHeads.putAll(obj);
            acHeads.put("ACT_NUM", accountClosingTO.getActNum());
            acHeads.put("INT_PAYABLE_AMOUNT", accountClosingTO.getIntPayable());
            acHeads.put("generateSingleTransId", generateSingleTransId);                                      
            acHeads.put("SCREEN_NAME", "Account Closing");
            System.out.println("generateSingleTransId:acHeads " + generateSingleTransId+"hhhhh"+obj);
            ((InterestTask) getIntPayable(acHeads, obj)).insertData();
        }
        if (!prodType.equals("TermLoan")) {
            HashMap todCheckmap = new HashMap();
            todCheckmap.put("PROD_ID", accountClosingTO.getProdId());
            List todList = sqlMap.executeQueryForList("isTODSetForProduct", todCheckmap);
            if (todList != null && todList.size() > 0) {
                HashMap todMap = (HashMap) todList.get(0);
                if (todMap.containsKey("TEMP_OD_ALLOWED")) {
                    if (CommonUtil.convertObjToStr(todMap.get("TEMP_OD_ALLOWED")).equalsIgnoreCase("Y")) {
                        acHeads.putAll(obj);
                        acHeads.put("SB_OD", "SB_OD");
                        acHeads.put("ACT_NUM", accountClosingTO.getActNum());
                        acHeads.put("INT_PAYABLE_AMOUNT", accountClosingTO.getIntPayable());
                        acHeads.put("generateSingleTransId", generateSingleTransId);
                        System.out.println("generateSingleTransId:acHeads for sb od " + generateSingleTransId + "hhhhh" + obj);
                        ((InterestTask) getIntPayable(acHeads, obj)).insertData();

                    }
                }
            }
        }
        String behaves = "";
        Double loanTempAmt = new Double(0);
        //ArrayList transferListTemp = new ArrayList();
        if (CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue() > 0) {
            if (prodType.equals("TermLoan")) {
                totAmt = (HashMap) obj.get("TOTAL_AMOUNT");
            }
            //                HashMap hash=new HashMap();
            //
            //                totAmt=(HashMap)obj.get("TOTAL_AMOUNT");
            //                txMap = new HashMap();
            //                txMap.put(TransferTrans.CR_PROD_ID, (String)acHeads.get("PROD_ID"));
            //                txMap.put(TransferTrans.CR_AC_HD, (String)acHeads.get("ACCT_HEAD"));
            //                txMap.put(TransferTrans.CR_ACT_NUM, accountClosingTO.getActNum());
            //                txMap.put(TransferTrans.CR_BRANCH, branchID);
            //                txMap.put(TransferTrans.CURRENCY, "INR");
            ////                hash=(HashMap)obj.get("BEHAVE_LIKE");
            if (prodType.equals("TermLoan")) {
                behaves = CommonUtil.convertObjToStr(obj.get("BEHAVE_LIKE"));
            }
            productBehaveLike = CommonUtil.convertObjToStr(obj.get("BEHAVE_LIKE"));
            //                System.out.println(totAmt+"behaves####@@@"+behaves);
            //                if(behaves.equals("OD"))
            //                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
            //                else
            //                    txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.LOANS);
            //                transactionDAO.addTransferCredit(txMap, CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue()) ;
            ////                if(CommonUtil.convertObjToDouble(totAmt.get("CLEAR_BALANCE")).doubleValue()>0)
            ////                transactionDAO.addTransferCredit(txMap, CommonUtil.convertObjToDouble(totAmt.get("CLEAR_BALANCE")).doubleValue()) ;
            ////                if(CommonUtil.convertObjToDouble(totAmt.get("CURR_MONTH_PRINCEPLE")).doubleValue()>0)
            ////                     transactionDAO.addTransferCredit(txMap, CommonUtil.convertObjToDouble(totAmt.get("CURR_MONTH_PRINCEPLE")).doubleValue()) ;
            ////                 if(CommonUtil.convertObjToDouble(totAmt.get("OVER_DUE_PRINCIPAL")).doubleValue()>0)
            ////                     transactionDAO.addTransferCredit(txMap, CommonUtil.convertObjToDouble(totAmt.get("OVER_DUE_PRINCIPAL")).doubleValue()) ;
            //                } else {
            Double tempAmt = accountClosingTO.getPayableBal();
            loanTempAmt = accountClosingTO.getPayableBal();
            // If Edit mode
            if (obj.containsKey("CASH_TRANS_DETAILS") && CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue() > 0) {
                System.out.println("@@@@@@inside CASH_TRANS_DETAILS");
                HashMap tempMap = (HashMap) obj.get("CASH_TRANS_DETAILS");
                tempMap.put("TRANS_DT", currDt.clone());
                tempMap.put("INITIATED_BRANCH", _branchCode);
                double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                double newAmount = CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue();
                CashTransactionTO txTransferTO = null;
                HashMap oldAmountMap = new HashMap();
                if (oldAmount != newAmount || accountClosingTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                    List lst = sqlMap.executeQueryForList("getSelectCashTransactionTO", tempMap);
                    if (lst != null) {
                        txTransferTO = (CashTransactionTO) lst.get(0);
                        txTransferTO.setInpAmount(new Double(newAmount));
                        txTransferTO.setAmount(new Double(newAmount));
                        txTransferTO.setCommand(commandMode);
                        //akhil@
                        if (prodType.equals("TermLoan")) {
                            txTransferTO.setScreenName("Loan Closing");
                        } else {
                            txTransferTO.setScreenName("Account Closing");
                        }
                        if (accountClosingTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                            txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                        } else {
                            txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        }
                        txTransferTO.setStatusDt(currDt);

                        //                            txMap = new HashMap();
                        //                            txMap.put(TransferTrans.DR_PROD_ID, txTransferTO.getProdId());
                        //                            txMap.put(TransferTrans.DR_AC_HD, txTransferTO.getAcHdId());
                        //                            txMap.put(TransferTrans.DR_ACT_NUM, accountClosingTO.getActNum());
                        //                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                        //                            txMap.put(TransferTrans.CURRENCY, "INR");
                        //                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                        //                            System.out.println("OPERATIVE ACCOUNT####"+txMap);
                        //                            transactionDAO.addTransferDebit(txMap, CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue()) ;
                        //                            ArrayList cashList = new ArrayList();
                        //                            cashList.add(txTransferTO);
                        //                            transactionDAO.addCashList(cashList);
                        //                            transactionDAO.setCommandMode(commandMode);
                        //                            transactionDAO.setTransactionType(CommonConstants.TX_CASH);
                        //                            transactionDAO.doTransferLocal(null, branchID);
                        if (prodType.equals("TermLoan")) {
                            if (behaves.equals("OD")) {
                                obj.put("PRODUCTTYPE", TransactionFactory.ADVANCES);
                            } else {
                                obj.put("PRODUCTTYPE", TransactionFactory.LOANS);
                            }
                        } else {
                            obj.put("PRODUCTTYPE", TransactionFactory.OPERATIVE);
                        }
                        obj.put(TransactionDAOConstants.OLDAMT, new Double(oldAmount));
                        if (prodType.equals("TermLoan")) {
                            if (behaves.equals("OD")) {
                                txTransferTO.setTransModType("AD");
                            } else {
                                txTransferTO.setTransModType("TL");
                            }
                        } else {
                            txTransferTO.setTransModType("OA");
                        }

                        txTransferTO.setParticulars("By Cash");
                        if (prodType.equals("TermLoan")) {
                            txTransferTO.setScreenName("Loan Closing");
                        } else {
                            txTransferTO.setScreenName("Account Closing");
                        }
                        obj.put("CashTransactionTO", txTransferTO);
                        CashTransactionDAO cashTransDAO = new CashTransactionDAO();
                        cashTransDAO.execute(obj, false);
                        //                            cashList = null;
                        txMap = null;
                    }
                    lst = null;
                }
                txTransferTO = null;
                oldAmountMap = null;
                tempMap = null;
                accountClosingTO.setPayableBal(new Double(0));
            }

            if (obj.containsKey("TRANSFER_TRANS_DETAILS") && CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue() > 0) {
                System.out.println("@@@@@@inside TRANSFER_TRANS_DETAILS");
                HashMap tempMap = (HashMap) obj.get("TRANSFER_TRANS_DETAILS");
                tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                tempMap.put("TRANS_DT", currDt.clone());
                tempMap.put("INITIATED_BRANCH", _branchCode);
                tempMap.remove("TRANS_ID");
                double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                double newAmount = CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue();
                ArrayList batchList = new ArrayList();
                TxTransferTO txTransferTO = null;
                HashMap oldAmountMap = new HashMap();
                if (oldAmount != newAmount || accountClosingTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                    List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                    if (lst != null) {
                        for (int i = 0; i < lst.size(); i++) {
                            txTransferTO = (TxTransferTO) lst.get(i);
                            txTransferTO.setInpAmount(new Double(newAmount));
                            txTransferTO.setAmount(new Double(newAmount));
                            if (accountClosingTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                                txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                            } else {
                                txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                            }
                            txTransferTO.setStatusDt(currDt);
                            txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                            if (prodType.equals("TermLoan")) {
                                txTransferTO.setScreenName("Loan Closing");
                            } else {
                                txTransferTO.setScreenName("Account Closing");
                            }
                            oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                            batchList.add(txTransferTO);
                        }
                    }
                    TransferTrans transferTrans = new TransferTrans();
                    transferTrans.setOldAmount(oldAmountMap);
                    transferTrans.setInitiatedBranch(branchID);
                    transferTrans.doDebitCredit(batchList, branchID, false, commandMode);
                    lst = null;
                    transferTrans = null;
                }
                txTransferTO = null;
                batchList = null;
                oldAmountMap = null;
                tempMap = null;
                accountClosingTO.setPayableBal(new Double(0));
            }

            // New mode            
            if (CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue() > 0) {
                if (!prodType.equals("TermLoan")) {
                    txMap = new HashMap();
                    txMap.put(TransferTrans.DR_PROD_ID, (String) acHeads.get("PROD_ID"));
                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("AC_HD_ID"));
                    txMap.put(TransferTrans.DR_ACT_NUM, accountClosingTO.getActNum());
                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                    txMap.put(TransferTrans.PARTICULARS, "   A/C Closure-" + acno);
                    txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put("PRODUCT_ID", accountClosingTO.getProdId());
                    System.out.println("TransferTrans.PARTICULARS2" + txMap);
                    System.out.println("OPERATIVE ACCOUNT####" + txMap);
                    txMap.put("TRANS_MOD_TYPE", "OA");
                    txMap.put("generateSingleTransId", generateSingleTransId);
//                    String status = ((OperativeAcctProductTO)sqlMap.executeQueryForList("getOpAcctProductTOByProdId", txMap).get(0)).getSRemarks();
//                    if(status.equals("NRO")){
//                        HashMap map = new HashMap();
//                        map.put("PROD_ID",CommonUtil.convertObjToStr(txMap.get("PRODUCT_ID")));
//                        List lst=sqlMap.executeQueryForList("getTaxDetailsNRO", map);
//                        if(lst!=null && lst.size()>0){
//                            double taxAmt= CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue();
//                            System.out.println("taxAmtININSertACHAED"+taxAmt);
//                            taxAmt-=CommonUtil.convertObjToDouble(accountClosingTO.getTaxPayable()).doubleValue();
//                            accountClosingTO.setPayableBal(new Double(taxAmt));
//                            transactionDAO.addTransferDebit(txMap,taxAmt);
//                            tto.setTransAmt(new Double(taxAmt));
//                        }
//                        else{
//                        transactionDAO.addTransferDebit(txMap, CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue()) ;
//                        }
//                    }
//                    else
                    txMap.put("SCREEN_NAME","Account Closing");
                    if(inst_type!=null && inst_type.equals("CHEQUE")){
                        txMap.put(TransferTrans.DR_INST_TYPE,"AB_CHEQUE");
                    }
                     if (prodType.equals("TermLoan")) {
                        txMap.put("SCREEN_NAME", "Loan Closing");
                    } else {
                        txMap.put("SCREEN_NAME", "Account Closing");
                    }   
                     System.out.println("addTransferDebit : " + txMap);
                    transactionDAO.addTransferDebit(txMap, CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue());
                    //transferList.add(transferTo);
                    //transferListTemp.add(transferTo);
                }
            }
            if (!prodType.equals("TermLoan")) {
                accountClosingTO.setPayableBal(tempAmt);
            }
            //            }//
        }

        Double tempAmt = accountClosingTO.getActClosingChrg();
        // If Edit mode
        if (obj.containsKey("ACT_CLOSE_CHRG_TRANS_DETAILS") && CommonUtil.convertObjToDouble(accountClosingTO.getActClosingChrg()).doubleValue() > 0) {
            System.out.println("@@@@@@inside ACT_CLOSE_CHRG_TRANS_DETAILS");
            HashMap tempMap = (HashMap) obj.get("ACT_CLOSE_CHRG_TRANS_DETAILS");
            tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
            tempMap.put("TRANS_DT", currDt.clone());
            tempMap.put("INITIATED_BRANCH", _branchCode);
            tempMap.remove("TRANS_ID");
            double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
            double newAmount = CommonUtil.convertObjToDouble(accountClosingTO.getActClosingChrg()).doubleValue();
            ArrayList batchList = new ArrayList();
            TxTransferTO txTransferTO = null;
            HashMap oldAmountMap = new HashMap();
            if (oldAmount != newAmount || accountClosingTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                if (lst != null) {
                    for (int i = 0; i < lst.size(); i++) {
                        txTransferTO = (TxTransferTO) lst.get(i);
                        txTransferTO.setInpAmount(new Double(newAmount));
                        txTransferTO.setAmount(new Double(newAmount));
                        if (accountClosingTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                            txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                        } else {
                            txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        }
                        txTransferTO.setStatusDt(currDt);
                        txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                        if (prodType.equals("TermLoan")) {
                            txTransferTO.setScreenName("Loan Closing");
                        } else {
                            txTransferTO.setScreenName("Account Closing");
                        }
                        oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                        batchList.add(txTransferTO);
                    }
                }
                TransferTrans transferTrans = new TransferTrans();
                transferTrans.setOldAmount(oldAmountMap);
                transferTrans.setInitiatedBranch(branchID);
                transferTrans.doDebitCredit(batchList, branchID, false, commandMode);
                lst = null;
                transferTrans = null;
            }
            txTransferTO = null;
            batchList = null;
            oldAmountMap = null;
            tempMap = null;
            accountClosingTO.setActClosingChrg(new Double(0));
        }
        double debitAmount = 0;
        ArrayList transferListTemp = new ArrayList();
        //If New Mode
        if (CommonUtil.convertObjToDouble(accountClosingTO.getActClosingChrg()).doubleValue() > 0) {
            if (CommonUtil.convertObjToStr(acHeads.get("ACCLOSE_CHRG")).length() > 0) {
                System.out.println("@@@@@@inside new mode 363");
                txMap = new HashMap();
                ArrayList transferList = new ArrayList(); // for local transfer
                txMap.put(TransferTrans.DR_PROD_ID, (String) acHeads.get("PROD_ID"));
                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("AC_HD_ID"));
                txMap.put(TransferTrans.DR_ACT_NUM, accountClosingTO.getActNum());
                txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                if (prodType.equals("TermLoan")) {
                    if (behaves.equals("OD")) {
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.ADVANCES);
                        txMap.put("TRANS_MOD_TYPE", "AD");
                    } else {
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.LOANS);
                        txMap.put("TRANS_MOD_TYPE", "TL");
                    }
                    //                txMap.put(TransferTrans.PARTICULARS,"OTHERCHARGES"); change  transactionDAO.setLoanDebitInt("OTHERCHARGES");
                } else {
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                    //txMap.put("TRANS_MOD_TYPE", "OA");
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL); // Added by nithya on 01-02-2019 for KD-394 - Sb closing (integrity issue)
                }
                txMap.put(TransferTrans.DR_BRANCH, branchID);

                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("ACCLOSE_CHRG"));
                txMap.put(TransferTrans.CR_BRANCH, branchID);
                txMap.put(TransferTrans.CURRENCY, "INR");
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.PARTICULARS, "A/C No" + "" + accountClosingTO.getActNum());
                txMap.put("generateSingleTransId", generateSingleTransId);
                if (prodType.equals("TermLoan")) {
                    txMap.put("SCREEN_NAME", "Loan Closing");
                } else {
                    txMap.put("SCREEN_NAME", "Account Closing");
                }
                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(accountClosingTO.getActClosingChrg()).doubleValue());
                if (prodType.equals("TermLoan")) {
                    transferTo.setScreenName("Loan Closing");
                } else {
                    transferTo.setScreenName("Account Closing");
                }
                transferList.add(transferTo);
                transferListTemp.add(transferTo);
                txMap.put(TransferTrans.PARTICULARS, " Miscallenous Charges");
                txMap.put("generateSingleTransId", generateSingleTransId);
                debitAmount= debitAmount+CommonUtil.convertObjToDouble(accountClosingTO.getActClosingChrg());
               // transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(accountClosingTO.getActClosingChrg()).doubleValue());
               // transferList.add(transferTo);
                // //System.out.println("transferList:" + transferList);
              //  transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
               // transactionDAO.setLoanDebitInt("OTHERCHARGES");
               // transactionDAO.doTransferLocal(transferList, branchID);
              //  transactionDAO.setCommandMode(commandMode);
                if (obj.containsKey("stMap") && obj.get("stMap") != null
                        && CommonUtil.convertObjToStr(txMap.get(TransferTrans.DR_PROD_TYPE)).equals("OA")) {
                    HashMap stMap = new HashMap();
                    stMap = (HashMap) obj.get("stMap");

                    txMap.put("stMap", obj.get("stMap"));
                    if (CommonUtil.convertObjToDouble(stMap.get("SERVICE_TAX")).doubleValue() > 0) {
                        doServiceTaxDebitCredit(txMap);
                    }
                }
            } else {
                throw new TTException("Closing Charges A/c Head not set.");
            }
        }
        accountClosingTO.setActClosingChrg(tempAmt);

        //added for credit interst transaction
        //If New Mode FOR AD ONLY
        if (CommonUtil.convertObjToDouble(getAdvancesCreditInterest()).doubleValue() > 0) {
            if (CommonUtil.convertObjToStr(acHeads.get("AC_CREDIT_INT")).length() > 0) {
                System.out.println("@@@@@@inside getAdvancesCreditInterest mo");
                txMap = new HashMap();
                ArrayList transferList = new ArrayList(); // for local transfer
                txMap.put(TransferTrans.CR_PROD_ID, (String) acHeads.get("PROD_ID"));
                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("AC_HD_ID"));
                txMap.put(TransferTrans.CR_ACT_NUM, accountClosingTO.getActNum());
                txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
//                if(prodType.equals("TermLoan")){
//                    if(behaves.equals("OD"))
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                txMap.put("TRANS_MOD_TYPE", "AD");
//                    else
//                        txMap.put(TransferTrans.DR_PROD_TYPE,TransactionFactory.LOANS);
                //                txMap.put(TransferTrans.PARTICULARS,"OTHERCHARGES"); change  transactionDAO.setLoanDebitInt("OTHERCHARGES");
//                }
//                else
//                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                txMap.put(TransferTrans.CR_BRANCH, branchID);

                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("AC_CREDIT_INT"));
                txMap.put(TransferTrans.DR_BRANCH, branchID);
                txMap.put(TransferTrans.CURRENCY, "INR");
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.PARTICULARS, "A/C No" + "" + accountClosingTO.getActNum());
                txMap.put("generateSingleTransId", generateSingleTransId);
                if (prodType.equals("TermLoan")) {
                    txMap.put("SCREEN_NAME", "Loan Closing");
                } else {
                    txMap.put("SCREEN_NAME", "Account Closing");
                }
                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(getAdvancesCreditInterest()).doubleValue());//addTransferCreditLocal
                if (prodType.equals("TermLoan")) {
                    transferTo.setScreenName("Loan Closing");
                } else {
                    transferTo.setScreenName("Account Closing");
                }
                transferList.add(transferTo);
                txMap.put(TransferTrans.PARTICULARS, " Interest Debit Advances");
                txMap.put("generateSingleTransId", generateSingleTransId);
                 if (prodType.equals("TermLoan")) {
                        txMap.put("SCREEN_NAME", "Loan Closing");
                    } else {
                        txMap.put("SCREEN_NAME", "Account Closing");
                    }    
                transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(getAdvancesCreditInterest()).doubleValue());
                if (prodType.equals("TermLoan")) {
                    transferTo.setScreenName("Loan Closing");
                } else {
                    transferTo.setScreenName("Account Closing");
                }
                transferList.add(transferTo);
                // //System.out.println("transferList:" + transferList);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                transactionDAO.setLoanDebitInt("C*");
                transactionDAO.doTransferLocal(transferList, branchID);
                transactionDAO.setCommandMode(commandMode);
//                if(obj.containsKey("stMap") && obj.get("stMap")!=null && 
//                      CommonUtil.convertObjToStr(txMap.get(TransferTrans.DR_PROD_TYPE)).equals("OA")){
//                    HashMap stMap=new HashMap();
//                    stMap=(HashMap)obj.get("stMap");
//                    
//                    txMap.put("stMap", obj.get("stMap"));
//                   if(CommonUtil.convertObjToDouble(stMap.get("SERVICE_TAX")).doubleValue()>0){
//                    doServiceTaxDebitCredit(txMap);
//                   }
//                }
            } else {
                throw new TTException("Interest Debit GL A/c Head not set.");
            }
        }
        accountClosingTO.setActClosingChrg(tempAmt);
        //end credit interst transaction

        tempAmt = accountClosingTO.getChrgDetails();
        // If Edit mode
        if (obj.containsKey("CHRG_DETAILS_TRANS_DETAILS") && CommonUtil.convertObjToDouble(accountClosingTO.getChrgDetails()).doubleValue() > 0) {
            System.out.println("@@@@@@inside CHRG_DETAILS_TRANS_DETAILS");
            HashMap tempMap = (HashMap) obj.get("CHRG_DETAILS_TRANS_DETAILS");
            tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
            tempMap.put("TRANS_DT", currDt.clone());
            tempMap.put("INITIATED_BRANCH", _branchCode);
            tempMap.remove("TRANS_ID");
            double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
            double newAmount = CommonUtil.convertObjToDouble(accountClosingTO.getChrgDetails()).doubleValue();
            ArrayList batchList = new ArrayList();
            TxTransferTO txTransferTO = null;
            HashMap oldAmountMap = new HashMap();
            if (oldAmount != newAmount || accountClosingTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                if (lst != null) {
                    for (int i = 0; i < lst.size(); i++) {
                        txTransferTO = (TxTransferTO) lst.get(i);
                        txTransferTO.setInpAmount(new Double(newAmount));
                        txTransferTO.setAmount(new Double(newAmount));
                        if (accountClosingTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                            txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                        } else {
                            txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        }
                        txTransferTO.setStatusDt(currDt);
                        txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                        if (prodType.equals("TermLoan")) {
                            txTransferTO.setScreenName("Loan Closing");
                        } else {
                            txTransferTO.setScreenName("Account Closing");
                        }
                        oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                        batchList.add(txTransferTO);
                    }
                }
                TransferTrans transferTrans = new TransferTrans();
                transferTrans.setOldAmount(oldAmountMap);
                transferTrans.setInitiatedBranch(branchID);
                transferTrans.doDebitCredit(batchList, branchID, false, commandMode);
                lst = null;
                transferTrans = null;
            }
            txTransferTO = null;
            batchList = null;
            oldAmountMap = null;
            tempMap = null;
            accountClosingTO.setChrgDetails(new Double(0));
        }
        //If New Mode
        if (CommonUtil.convertObjToDouble(accountClosingTO.getChrgDetails()).doubleValue() > 0) {
            if (CommonUtil.convertObjToStr(acHeads.get("MISSER_CHRG")).length() > 0) {
                System.out.println("@@@@@@inside  443");
                txMap = new HashMap();
                ArrayList transferList = new ArrayList(); // for local transfer
                txMap.put(TransferTrans.DR_PROD_ID, (String) acHeads.get("PROD_ID"));
                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("AC_HD_ID"));
                txMap.put(TransferTrans.DR_ACT_NUM, accountClosingTO.getActNum());
                txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                if (prodType.equals("TermLoan")) {
                    if (behaves.equals("OD")) {
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.ADVANCES);
                        txMap.put("TRANS_MOD_TYPE", "AD");
                    } else {
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.LOANS);
                        txMap.put("TRANS_MOD_TYPE", "TL");
                    }
                    //                    txMap.put(TransferTrans.PARTICULARS,"OTHERCHARGES");alter transactionDAO.setLoanDebitInt("OTHERCHARGES");
                } else {
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                    txMap.put("TRANS_MOD_TYPE", "OA");
                }
                txMap.put(TransferTrans.DR_BRANCH, branchID);
                transactionDAO.setLoanDebitInt("OTHERCHARGES");
                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("MISSER_CHRG"));
                txMap.put(TransferTrans.CR_BRANCH, branchID);
                txMap.put(TransferTrans.CURRENCY, "INR");
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                if (!prodType.equals("TermLoan")) {
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                }
                txMap.put(TransferTrans.PARTICULARS, "A/C No" + "" + accountClosingTO.getActNum());
                txMap.put("generateSingleTransId", generateSingleTransId);
                if (prodType.equals("TermLoan")) {
                    txMap.put("SCREEN_NAME", "Loan Closing");
                } else {
                    txMap.put("SCREEN_NAME", "Account Closing");
                }
                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(accountClosingTO.getChrgDetails()).doubleValue());
                if (prodType.equals("TermLoan")) {
                    transferTo.setScreenName("Loan Closing");
                } else {
                    transferTo.setScreenName("Account Closing");
                }
                transferList.add(transferTo);
                transferListTemp.add(transferTo);
                txMap.put("generateSingleTransId", generateSingleTransId);
                debitAmount = debitAmount + CommonUtil.convertObjToDouble(accountClosingTO.getChrgDetails());
              //  transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(accountClosingTO.getChrgDetails()).doubleValue());
              //  transferList.add(transferTo);
               // // //System.out.println("transferList:" + transferList);
              //  transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
               // transactionDAO.doTransferLocal(transferList, branchID);
                //transactionDAO.setCommandMode(commandMode);
            } else {
                throw new TTException("Miscellaneous Charges A/c Head not set.");
            }
        }
        accountClosingTO.setChrgDetails(tempAmt);
		//added by chithra for service Tax
        if (!prodType.equals("TermLoan") && obj.containsKey("serviceTaxDetails")) {
            HashMap temp_map = (HashMap) obj.get("serviceTaxDetails");
            if (temp_map != null && temp_map.size() > 0) {
                ArrayList transferList = new ArrayList(); // for local transfer
                double swachhCess = 0.0;
                double krishikalyanCess = 0.0;
                double serTaxAmt = 0.0;
                double totalServiceTaxAmt = 0.0;
                double normalServiceTax = 0.0;
                if (temp_map.containsKey("TOT_TAX_AMT") && temp_map.get("TOT_TAX_AMT") != null) {
                    serTaxAmt = CommonUtil.convertObjToDouble(temp_map.get("TOT_TAX_AMT"));
                    totalServiceTaxAmt = CommonUtil.convertObjToDouble(temp_map.get("TOT_TAX_AMT"));
                }
                if (temp_map.containsKey(ServiceTaxCalculation.SWACHH_CESS) && temp_map.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                    swachhCess = CommonUtil.convertObjToDouble(temp_map.get(ServiceTaxCalculation.SWACHH_CESS));
                }
                if (temp_map.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && temp_map.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                    krishikalyanCess = CommonUtil.convertObjToDouble(temp_map.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                }
                if (temp_map.containsKey(ServiceTaxCalculation.SERVICE_TAX) && temp_map.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                    normalServiceTax = CommonUtil.convertObjToDouble(temp_map.get(ServiceTaxCalculation.SERVICE_TAX));
                }
                //serTaxAmt-=(swachhCess+krishikalyanCess);               
                if (swachhCess > 0) {
                    txMap = new HashMap();
                    txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    if (prodType.equals("TermLoan")) {
                        if (behaves.equals("OD")) {
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.ADVANCES);
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                        } else {
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.LOANS);
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.LOANS);
                        }
                    } else {
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                        //txMap.put("TRANS_MOD_TYPE", TransactionFactory.OPERATIVE);
                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL); // Added by nithya on 01-02-2019 for KD-394 - Sb closing (integrity issue)
                    }
                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                    transactionDAO.setLoanDebitInt("OTHERCHARGES");
                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(temp_map.get("SWACHH_HEAD_ID")));
                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.PARTICULARS, "CGST");
                    txMap.put("generateSingleTransId", generateSingleTransId);
                    if (prodType.equals("TermLoan")) {
                        txMap.put("SCREEN_NAME", "Loan Closing");
                    } else {
                        txMap.put("SCREEN_NAME", "Account Closing");
                    }
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, swachhCess);
                    if (prodType.equals("TermLoan")) {
                        transferTo.setScreenName("Loan Closing");
                    } else {
                        transferTo.setScreenName("Account Closing");
                    }
                    transferList.add(transferTo);
                    transferListTemp.add(transferTo);
                   // serTaxAmt -= swachhCess;
                }
                if (krishikalyanCess > 0) {
                    txMap = new HashMap();
                    txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    if (prodType.equals("TermLoan")) {
                        if (behaves.equals("OD")) {
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.ADVANCES);
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                        } else {
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.LOANS);
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.LOANS);
                        }
                    } else {
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                        //txMap.put("TRANS_MOD_TYPE", TransactionFactory.OPERATIVE);
                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL); // Added by nithya on 01-02-2019 for KD-394 - Sb closing (integrity issue)
                    }
                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                    transactionDAO.setLoanDebitInt("OTHERCHARGES");
                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(temp_map.get("KRISHIKALYAN_HEAD_ID")));
                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.PARTICULARS, "SGST");
                    txMap.put("generateSingleTransId", generateSingleTransId);
                    if (prodType.equals("TermLoan")) {
                        txMap.put("SCREEN_NAME", "Loan Closing");
                    } else {
                        txMap.put("SCREEN_NAME", "Account Closing");
                    }
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, krishikalyanCess);
                    if (prodType.equals("TermLoan")) {
                        transferTo.setScreenName("Loan Closing");
                    } else {
                        transferTo.setScreenName("Account Closing");
                    }
                    transferList.add(transferTo);
                    transferListTemp.add(transferTo);
                   // serTaxAmt -= krishikalyanCess;
                }
                if (normalServiceTax > 0){
                    txMap = new HashMap();
                    txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    if (prodType.equals("TermLoan")) {
                        if (behaves.equals("OD")) {
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.ADVANCES);
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                        } else {
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.LOANS);
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.LOANS);
                        }
                    } else {
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                        //txMap.put("TRANS_MOD_TYPE", TransactionFactory.OPERATIVE);
                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL); // Added by nithya on 01-02-2019 for KD-394 - Sb closing (integrity issue)
                    }
                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                    transactionDAO.setLoanDebitInt("OTHERCHARGES");
                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(temp_map.get("TAX_HEAD_ID")));
                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.PARTICULARS, "SERVICE_TAX");
                    txMap.put("generateSingleTransId", generateSingleTransId);
                    if (prodType.equals("TermLoan")) {
                        txMap.put("SCREEN_NAME", "Loan Closing");
                    } else {
                        txMap.put("SCREEN_NAME", "Account Closing");
                    }
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax);
                    if (prodType.equals("TermLoan")) {
                        transferTo.setScreenName("Loan Closing");
                    } else {
                        transferTo.setScreenName("Account Closing");
                    }
                    transferList.add(transferTo);
                    transferListTemp.add(transferTo);
                }                
                txMap.put("generateSingleTransId", generateSingleTransId);
                debitAmount = debitAmount + totalServiceTaxAmt;
            }
        }
        System.out.println("@@@@@@inside  transferListTemp"+transferListTemp);
        if (transferListTemp != null && transferListTemp.size() > 0) {
            txMap = new HashMap();
            txMap.put(TransferTrans.DR_PROD_ID, (String) acHeads.get("PROD_ID"));
            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("AC_HD_ID"));
            txMap.put(TransferTrans.DR_ACT_NUM, accountClosingTO.getActNum());
            txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
            if (prodType.equals("TermLoan")) {
                if (behaves.equals("OD")) {
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.ADVANCES);
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                } else {
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.LOANS);
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.LOANS);
                }
            } else {
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                txMap.put("TRANS_MOD_TYPE", TransactionFactory.OPERATIVE);
                //added by anjuanand for MantisId: 0010357
                txMap.put(TransferTrans.PARTICULARS, "Closing Charge Debit");
            }
            txMap.put(TransferTrans.DR_BRANCH, branchID);
            transactionDAO.setLoanDebitInt("OTHERCHARGES");
            if (prodType.equals("TermLoan")) {
                txMap.put("SCREEN_NAME", "Loan Closing");
            } else {
                txMap.put("SCREEN_NAME", "Account Closing");
            }
            txMap.put("generateSingleTransId", generateSingleTransId);
            transferTo = transactionDAO.addTransferDebitLocal(txMap, debitAmount);
            if (prodType.equals("TermLoan")) {
                transferTo.setScreenName("Loan Closing");
            } else {
                transferTo.setScreenName("Account Closing");
            }
            transferListTemp.add(transferTo);
            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
            transactionDAO.doTransferLocal(transferListTemp, branchID);
            transactionDAO.setCommandMode(commandMode);
        }
        
        if (CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue() > 0) {
            txMap = new HashMap();

            //If New Mode for termloan  DI
            if (prodType.equals("TermLoan") && totAmt != null && totAmt.containsKey("CALCULATE_INT") && totAmt.get("CALCULATE_INT") != null) {
                if (CommonUtil.convertObjToDouble(totAmt.get("CALCULATE_INT")).doubleValue() > 0 || obj.containsKey("DEPOSIT_PREMATURE_CLOSER")) {
                    if ((CommonUtil.convertObjToStr(acHeads.get("ACCT_HEAD")).length() > 0 && CommonUtil.convertObjToStr(acHeads.get("INT_PAYABLE_ACHD")).length() > 0)
                            || (obj.containsKey("DEPOSIT_PREMATURE_CLOSER")) && CommonUtil.convertObjToStr(acHeads.get("AC_DEBIT_INT")).length() > 0) {
                        double calculateInt = CommonUtil.convertObjToDouble(totAmt.get("CALCULATE_INT")).doubleValue();
                        txMap = new HashMap();
                        ArrayList transferList = new ArrayList(); // for local transfer
                        //for premature deposit closer
                        //debit int recived
                        if (obj.containsKey("DEPOSIT_PREMATURE_CLOSER") && calculateInt > 0) {
                            //---------------------------INT RECIVED TO RECIVABLE
                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("AC_DEBIT_INT"));
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                            txMap.put(TransferTrans.PARTICULARS, "A/C No" + "" + accountClosingTO.getActNum());
                            txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            //credit loan account
                            //                            txMap.put(TransferTrans.CR_PROD_ID, (String)acHeads.get("PROD_ID"));
                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PAYABLE_ACHD"));//INT_PAYABLE_ACHD
                            //                            txMap.put(TransferTrans.CR_ACT_NUM, accountClosingTO.getActNum());
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                            txMap.put("TRANS_MOD_TYPE","TL");
                            txMap.put("generateSingleTransId", generateSingleTransId);
                            if (prodType.equals("TermLoan")) {
                                txMap.put("SCREEN_NAME", "Loan Closing");
                            } else {
                                txMap.put("SCREEN_NAME", "Account Closing");
                            }
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, calculateInt);
                            if (prodType.equals("TermLoan")) {
                                transferTo.setScreenName("Loan Closing");
                            } else {
                                transferTo.setScreenName("Account Closing");
                            }
                            //                        transferTo.setAuthorizeRemarks("DI");
                            transferList.add(transferTo);
                            txMap.put("generateSingleTransId", generateSingleTransId);
                            if (prodType.equals("TermLoan")) {
                                txMap.put("SCREEN_NAME", "Loan Closing");
                            } else {
                                txMap.put("SCREEN_NAME", "Account Closing");
                            }
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, calculateInt);
                            if (prodType.equals("TermLoan")) {
                                transferTo.setScreenName("Loan Closing");
                            } else {
                                transferTo.setScreenName("Account Closing");
                            }
                            //                        transferTo.setAuthorizeRemarks("DI");
                            transferList.add(transferTo);
                            System.out.println("transferListcalculateint###:" + transferList);
                            transactionDAO.doTransferLocal(transferList, branchID);

                            //------------------------INT RECIVABLE TO LOAN CREDIT
                            txMap = new HashMap();
                            transferList = new ArrayList();
                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAYABLE_ACHD"));
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                            //credit loan account
                            txMap.put(TransferTrans.CR_PROD_ID, (String) acHeads.get("PROD_ID"));
                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("ACCT_HEAD"));
                            txMap.put(TransferTrans.CR_ACT_NUM, accountClosingTO.getActNum());
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                            txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.PARTICULARS, "Excess Interest");
                            txMap.put("TRANS_MOD_TYPE", "TL");
                            txMap.put("generateSingleTransId", generateSingleTransId);
                            if (prodType.equals("TermLoan")) {
                                txMap.put("SCREEN_NAME", "Loan Closing");
                            } else {
                                txMap.put("SCREEN_NAME", "Account Closing");
                            }
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, calculateInt);
                            if (prodType.equals("TermLoan")) {
                                transferTo.setScreenName("Loan Closing");
                            } else {
                                transferTo.setScreenName("Account Closing");
                            }
                            //                        transferTo.setAuthorizeRemarks("DI");
                            transferList.add(transferTo);
                            txMap.put(TransferTrans.PARTICULARS, "A/C No" + "" + accountClosingTO.getActNum());
                            txMap.put("TRANS_MOD_TYPE", "TL");
                            txMap.put("generateSingleTransId", generateSingleTransId);
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, calculateInt);
                            if (prodType.equals("TermLoan")) {
                                transferTo.setScreenName("Loan Closing");
                            } else {
                                transferTo.setScreenName("Account Closing");
                            }
                            //                        transferTo.setAuthorizeRemarks("DI");
                            transferList.add(transferTo);
                            System.out.println("transferListcalculateint###:" + transferList);
                            transactionDAO.doTransferLocal(transferList, branchID);
                            transactionDAO.setLoanDebitInt("");
                        } else {
                            //modified by rishad 19/10/2016 interest payable for od mantis:0004704:,0005953 
                            //added by rishad 09/03/2017 for mantis 5953 -- transaction interest credit
                            if (behaves.equals("OD")) {
                                if(obj.get("BAL_DR_CR")!=null&&obj.get("BAL_DR_CR").equals("Cr"))
                                {
                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAYABLE_ACHD"));
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                                    txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                                    txMap.put("TRANS_MOD_TYPE", "AD");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.CR_PROD_ID, (String) acHeads.get("PROD_ID"));
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("ACCT_HEAD"));
                                    txMap.put(TransferTrans.CR_ACT_NUM, accountClosingTO.getActNum());
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                                    txMap.put("AUTHORIZEREMARKS","DI");
                                }
                                else {
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PAYABLE_ACHD"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                                    txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                                    txMap.put(TransferTrans.PARTICULARS, "A/C No" + "" + accountClosingTO.getActNum());
                                    txMap.put("TRANS_MOD_TYPE", "AD");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_PROD_ID, (String) acHeads.get("PROD_ID"));
                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("ACCT_HEAD"));
                                    txMap.put(TransferTrans.DR_ACT_NUM, accountClosingTO.getActNum());
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.ADVANCES);
                                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                                    txMap.put("AUTHORIZEREMARKS","INTEREST");
                                }
                            }
                            else{
                            
                            //
                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PAYABLE_ACHD"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                            txMap.put(TransferTrans.PARTICULARS, "A/C No" + "" + accountClosingTO.getActNum());
                            txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                            
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_PROD_ID, (String) acHeads.get("PROD_ID"));
                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("ACCT_HEAD"));
                            txMap.put(TransferTrans.DR_ACT_NUM, accountClosingTO.getActNum());
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.LOANS);
                            txMap.put("TRANS_MOD_TYPE", "TL");
                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                            
                            //credit incase ltdpremature
                            //                            if(obj.containsKey("DEPOSIT_PREMATURE_CLOSER") || behaves.equals("LOANS_AGAINST_DEPOSITS"))
                            //                                txMap.put(TransferTrans.CR_AC_HD, (String)acHeads.get("AC_DEBIT_INT"));
                            //                            else
                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PAYABLE_ACHD"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                            txMap.put(TransferTrans.PARTICULARS, "A/C No" + "" + accountClosingTO.getActNum());
                            txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                             txMap.put("AUTHORIZEREMARKS","DI");
                            }
                            
                            if (calculateInt < 0) {
                                calculateInt *= -1;
                            }
                            txMap.put("generateSingleTransId", generateSingleTransId);
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, calculateInt); //CommonUtil.convertObjToDouble(totAmt.get("CALCULATE_INT")).doubleValue()
                            if (prodType.equals("TermLoan")) {
                                transferTo.setScreenName("Loan Closing");
                            } else {
                                transferTo.setScreenName("Account Closing");
                            }
                           // transferTo.setAuthorizeRemarks("DI");
                            transferList.add(transferTo);
                            txMap.put("generateSingleTransId", generateSingleTransId);
                            txMap.put(TransferTrans.PARTICULARS, "Interest Upto" + DateUtil.addDaysProperFormat(ServerUtil.getCurrentDateProperFormat(_branchCode), -1));
                            if (prodType.equals("TermLoan")) {
                                txMap.put("SCREEN_NAME", "Loan Closing");
                            } else {
                                txMap.put("SCREEN_NAME", "Account Closing");
                            }
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, calculateInt);
                            if (prodType.equals("TermLoan")) {
                                transferTo.setScreenName("Loan Closing");
                            } else {
                                transferTo.setScreenName("Account Closing");
                            }
                          //  transferTo.setAuthorizeRemarks("DI");
                            transferList.add(transferTo);
                            System.out.println("transferListcalculateint###:" + transferList);
                            transactionDAO.doTransferLocal(transferList, branchID);
                            //----------------------------------------------------
                            //                             txMap.put(TransferTrans.DR_PROD_ID, (String)acHeads.get("PROD_ID"));
                            //                            txMap.put(TransferTrans.DR_AC_HD, (String)acHeads.get("INT_PAYABLE_ACHD"));
                            //                            txMap.put(TransferTrans.DR_ACT_NUM, accountClosingTO.getActNum());
                            //                            if(behaves.equals("OD"))
                            //                                txMap.put(TransferTrans.DR_PROD_TYPE,TransactionFactory.ADVANCES);
                            //                            else
                            //                                txMap.put(TransferTrans.DR_PROD_TYPE,TransactionFactory.LOANS);
                            //                            txMap.put(TransferTrans.DR_BRANCH, branchID);

                            //credit incase ltdpremature
                            if (obj.containsKey("DEPOSIT_PREMATURE_CLOSER") || behaves.equals("LOANS_AGAINST_DEPOSITS")) {
                                //
                                //                            else
                                txMap = new HashMap();
                                transferList = new ArrayList();
                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAYABLE_ACHD"));
                                txMap.put(TransferTrans.DR_BRANCH, branchID);
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, "A/C No" + "" + accountClosingTO.getActNum());

                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("AC_DEBIT_INT"));
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.CR_BRANCH, branchID);
                                txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put("generateSingleTransId", generateSingleTransId);
                                if (calculateInt < 0) {
                                    calculateInt *= -1;
                                }
                                if (prodType.equals("TermLoan")) {
                                    txMap.put("SCREEN_NAME", "Loan Closing");
                                } else {
                                    txMap.put("SCREEN_NAME", "Account Closing");
                                }
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, calculateInt); //CommonUtil.convertObjToDouble(totAmt.get("CALCULATE_INT")).doubleValue()
                                if (prodType.equals("TermLoan")) {
                                    transferTo.setScreenName("Loan Closing");
                                } else {
                                    transferTo.setScreenName("Account Closing");
                                }
                                //                                transferTo.setAuthorizeRemarks("DI");
                                transferList.add(transferTo);
                                txMap.put("generateSingleTransId", generateSingleTransId);
                                if (prodType.equals("TermLoan")) {
                                    txMap.put("SCREEN_NAME", "Loan Closing");
                                } else {
                                    txMap.put("SCREEN_NAME", "Account Closing");
                                }
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, calculateInt);
                                if (prodType.equals("TermLoan")) {
                                    transferTo.setScreenName("Loan Closing");
                                } else {
                                    transferTo.setScreenName("Account Closing");
                                }
                                //                                transferTo.setAuthorizeRemarks("DI");
                                transferList.add(transferTo);
                                System.out.println("transferListcalculateint###:" + transferList);
                                transactionDAO.doTransferLocal(transferList, branchID);
                                transactionDAO.setLoanDebitInt("");
                            }
                        }



                        //                        txMap.put(TransferTrans.CR_AC_HD, (String)acHeads.get("INT_PAYABLE_ACHD"));
                        //                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                        //            txMap.put("PARTICULARS","DEBIT_INT"); alter  transactionDAO.setLoanDebitInt("OTHERCHARGES");
                        //                        transactionDAO.setLoanDebitInt("DI");
                        //                        txMap.put(TransferTrans.CURRENCY, "INR");
                        //                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        //                        txMap.put(TransferTrans.PARTICULARS,"A/C No"+ "" +accountClosingTO.getActNum());

                        //                        txMap.put(TransferTrans.PARTICULARS,"Interest Upto"+ " - " +currDt);
                        //
                        //                        transferTo =  transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(totAmt.get("CALCULATE_INT")).doubleValue()) ;
                        //                        transferTo.setAuthorizeRemarks("DI");
                        //                        transferList.add(transferTo);
                        //                        transferTo =  transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(totAmt.get("CALCULATE_INT")).doubleValue()) ;
                        //                        transferTo.setAuthorizeRemarks("DI");
                        //                        transferList.add(transferTo);
                        // //System.out.println("transferList:" + transferList);
                        System.out.println("transferListcalculateint###:" + transferList);

                        //                        transactionDAO.doTransferLocal(transferList, branchID);
                        //                        transactionDAO.setLoanDebitInt("");
                    } else {
                        throw new TTException("Closing Charges A/c Head not set.");
                    }
                }
            }

            //If New Mode for termloan  DPI
            if (prodType.equals("TermLoan") && totAmt != null && totAmt.containsKey("CALCULATE_PENAL_INT") && totAmt.get("CALCULATE_PENAL_INT") != null) {
                if (CommonUtil.convertObjToDouble(totAmt.get("CALCULATE_PENAL_INT")).doubleValue() > 0) {
                    if (CommonUtil.convertObjToStr(acHeads.get("ACCT_HEAD")).length() > 0 && CommonUtil.convertObjToStr(acHeads.get("INT_PAYABLE_ACHD")).length() > 0) {
                        txMap = new HashMap();
                        double calculate_penal_int = CommonUtil.convertObjToDouble(totAmt.get("CALCULATE_PENAL_INT")).doubleValue();
                        ArrayList transferList = new ArrayList(); // for local transfer
                        txMap.put(TransferTrans.DR_PROD_ID, (String) acHeads.get("PROD_ID"));
                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("ACCT_HEAD"));
                        txMap.put(TransferTrans.DR_ACT_NUM, accountClosingTO.getActNum());
                        txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        if (behaves.equals("OD")) {
                              txMap.put("TRANS_MOD_TYPE", "AD");
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.ADVANCES);
                        } else {
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.LOANS);
                            txMap.put("TRANS_MOD_TYPE", "TL");
                        }
                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                        System.out.println("(String)acHeads.ge=====" + (String) acHeads.get("PENAL_INT"));
                        System.out.println("calculate_penal_int===" + calculate_penal_int);
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PAYABLE_ACHD"));
                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                        //            txMap.put("PARTICULARS","DEBIT_INT"); alter  transactionDAO.setLoanDebitInt("OTHERCHARGES");
                        //                        transactionDAO.setLoanDebitInt("DPI");
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.PARTICULARS, "A/C No" + "" + accountClosingTO.getActNum());
                        txMap.put("generateSingleTransId", generateSingleTransId);
                        if (prodType.equals("TermLoan")) {
                            txMap.put("SCREEN_NAME", "Loan Closing");
                        } else {
                            txMap.put("SCREEN_NAME", "Account Closing");
                        }
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, calculate_penal_int);
                        if (prodType.equals("TermLoan")) {
                            transferTo.setScreenName("Loan Closing");
                        } else {
                            transferTo.setScreenName("Account Closing");
                        }
                        transferTo.setAuthorizeRemarks("DPI");
                        transferList.add(transferTo);
                        txMap.put(TransferTrans.PARTICULARS, "Penal Interest Upto" + DateUtil.addDaysProperFormat(ServerUtil.getCurrentDateProperFormat(_branchCode), -1));
                        txMap.put("generateSingleTransId", generateSingleTransId);
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, calculate_penal_int);
                        if (prodType.equals("TermLoan")) {
                            transferTo.setScreenName("Loan Closing");
                        } else {
                            transferTo.setScreenName("Account Closing");
                        }
                        transferTo.setAuthorizeRemarks("DPI");
                        transferList.add(transferTo);
                        // //System.out.println("transferList:" + transferList);
                        System.out.println("transferListcalculateint###:" + transferList);

                        transactionDAO.doTransferLocal(transferList, branchID);
                        transactionDAO.setLoanDebitInt("");
                        //FOR LTD
                        if (behaves.equals("LOANS_AGAINST_DEPOSITS")) {
                            //
                            //                            else
                            txMap = new HashMap();
                            transferList = new ArrayList();
                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAYABLE_ACHD"));
                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, "A/C No" + "" + accountClosingTO.getActNum());

                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("PENAL_INT"));
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                            txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put("TRANS_MOD_TYPE", "TL");
                            txMap.put("generateSingleTransId", generateSingleTransId);
                            if (prodType.equals("TermLoan")) {
                                txMap.put("SCREEN_NAME", "Loan Closing");
                            } else {
                                txMap.put("SCREEN_NAME", "Account Closing");
                            }
                            if (calculate_penal_int < 0) {
                                calculate_penal_int *= -1;
                            }
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, calculate_penal_int); //CommonUtil.convertObjToDouble(totAmt.get("CALCULATE_INT")).doubleValue()
                            //                                transferTo.setAuthorizeRemarks("DI");
                            if (prodType.equals("TermLoan")) {
                                transferTo.setScreenName("Loan Closing");
                            } else {
                                transferTo.setScreenName("Account Closing");
                            }
                            transferList.add(transferTo);
                            txMap.put("generateSingleTransId", generateSingleTransId);
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, calculate_penal_int);
                            if (prodType.equals("TermLoan")) {
                                transferTo.setScreenName("Loan Closing");
                            } else {
                                transferTo.setScreenName("Account Closing");
                            }
                            //                                transferTo.setAuthorizeRemarks("DI");
                            transferList.add(transferTo);
                            System.out.println("transferListcalculateint###:" + transferList);
                            transactionDAO.doTransferLocal(transferList, branchID);
                            transactionDAO.setLoanDebitInt("");
                        }
                    } else {
                        throw new TTException("Closing Charges A/c Head not set.");
                    }
                }
            }

            //            if(totAmt !=null)
            //                if(CommonUtil.convertObjToDouble(totAmt.get("CURR_MONTH_INT")).doubleValue()>0 || CommonUtil.convertObjToDouble(totAmt.get("CALCULATE_INT")).doubleValue()>0){  //already updated loan and advances transaction of updategl check update gl method
            //                    double tot_int=0;
            //                    tot_int= totAmt.get("CURR_MONTH_INT")==null? 0 :CommonUtil.convertObjToDouble(totAmt.get("CURR_MONTH_INT")).doubleValue();
            //                    tot_int+=CommonUtil.convertObjToDouble(totAmt.get("CALCULATE_INT")).doubleValue();
            //                    if(CommonUtil.convertObjToStr(acHeads.get("INT_PAYABLE_ACHD")).length()>0 && CommonUtil.convertObjToStr(acHeads.get("ACCT_HEAD")).length()>0){
            //                        txMap = new HashMap();
            //                        ArrayList transferList = new ArrayList(); // for local transfer
            //                        //            txMap.put(TransferTrans.DR_PROD_ID, (String)acHeads.get("PROD_ID"));
            //                        txMap.put(TransferTrans.DR_AC_HD, (String)acHeads.get("INT_PAYABLE_ACHD"));
            //                        //            txMap.put(TransferTrans.DR_ACT_NUM, accountClosingTO.getActNum());
            //                        txMap.put(TransferTrans.DR_PROD_TYPE,TransactionFactory.GL);
            //                        txMap.put(TransferTrans.DR_BRANCH, branchID);
            //                        txMap.put(TransferTrans.CR_AC_HD, (String)acHeads.get("AC_DEBIT_INT"));
            //                        txMap.put(TransferTrans.CR_BRANCH, branchID);
            //                        txMap.put(TransferTrans.CURRENCY, "INR");
            //                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            //                        txMap.put(TransferTrans.PARTICULARS,"A/C No"+ "" +accountClosingTO.getActNum());
            //                        transferTo =  transactionDAO.addTransferCreditLocal(txMap, tot_int) ;
            //                        transferList.add(transferTo);
            //                        transferTo =  transactionDAO.addTransferDebitLocal(txMap, tot_int) ;
            //                        transferList.add(transferTo);
            //                        // //System.out.println("transferList:" + transferList);
            //                        System.out.println("transferList:CUUMONTHINT###" + transferList);
            //
            //                        transactionDAO.doTransferLocal(transferList, branchID);
            //                    } else {
            //                        throw new TTException("int recivable or int received A/c Head not set.");
            //                    }
            //                }

        }

        // New mode
        if (CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue() > 0) {
            if (prodType.equals("TermLoan")) {
                if (getAdvancesCreditInterest() != null && CommonUtil.convertObjToDouble(getAdvancesCreditInterest()).doubleValue() > 0) {
                    HashMap hash = new HashMap();
                    if (behaves != null && behaves.equals("OD") && obj.get("BAL_DR_CR").equals("Dr")) {
                        totAmt = (HashMap) obj.get("TOTAL_AMOUNT");
                        txMap = new HashMap();
                        txMap.put(TransferTrans.CR_PROD_ID, (String) acHeads.get("PROD_ID"));
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("ACCT_HEAD"));
                        txMap.put(TransferTrans.CR_ACT_NUM, accountClosingTO.getActNum());
                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put("AUTHORIZEREMARKS", "ACT_CLOSE");
                        txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put("generateSingleTransId", generateSingleTransId);
                        //                hash=(HashMap)obj.get("BEHAVE_LIKE");
                        //                            String behaves=CommonUtil.convertObjToStr(obj.get("BEHAVE_LIKE"));
                        System.out.println(totAmt + "behaves####@@@ n" + behaves);
                        if (behaves.equals("OD")) {
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                            txMap.put("TRANS_MOD_TYPE", "AD");
                        } else {
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
                            txMap.put("TRANS_MOD_TYPE", "TL");
                        }

                        System.out.println("getPayablebal####termloan DEBIT" + txMap);

                        txMap.put(TransferTrans.PARTICULARS, acno);
                        if (transmode.equalsIgnoreCase("CASH")) {
                            txMap.put(TransferTrans.PARTICULARS, " CASH");
                        }
                        System.out.println("TransferTrans.PARTICULARS DEBIT" + txMap);
                        // transactionDAO.addTransferDebit(txMap, CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue()) ;
                        System.out.println("CRDR=================" + obj.get("BAL_DR_CR"));
                        System.out.println("addTransferDebit22222======" + txMap); //bbb
                        // if(obj.get("BAL_DR_CR")!=null && obj.get("BAL_DR_CR").equals("Dr"))
                        // transactionDAO.addTransferDebit(txMap, CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue()) ;
                        //  if(obj.get("BAL_DR_CR")!=null && obj.get("BAL_DR_CR").equals("Cr"))
                        if (prodType.equals("TermLoan")) {
                            txMap.put("SCREEN_NAME", "Loan Closing");
                        } else {
                            txMap.put("SCREEN_NAME", "Account Closing");
                        }
                        transactionDAO.addTransferCredit(txMap, CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue());///bbbb
                        ////end 
                    } else // if(behaves!=null && behaves.equals("OD") && obj.get("BAL_DR_CR").equals("Cr"))
                    {
                        totAmt = (HashMap) obj.get("TOTAL_AMOUNT");
                        txMap = new HashMap();
                        txMap.put(TransferTrans.DR_PROD_ID, (String) acHeads.get("PROD_ID"));
                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("ACCT_HEAD"));//Nidhin changes
                        txMap.put(TransferTrans.DR_ACT_NUM, accountClosingTO.getActNum());
                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put("AUTHORIZEREMARKS", "ACT_CLOSE");
                        txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put("generateSingleTransId", generateSingleTransId);
                        //                hash=(HashMap)obj.get("BEHAVE_LIKE");
                        //                            String behaves=CommonUtil.convertObjToStr(obj.get("BEHAVE_LIKE"));
                        System.out.println(totAmt + "behaves####@@@ " + behaves);
                        if (behaves.equals("OD")) {
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.ADVANCES);
                            txMap.put("TRANS_MOD_TYPE", "AD");
                        } else {
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.LOANS);
                            txMap.put("TRANS_MOD_TYPE", "TL");
                        }
                        System.out.println("getPayablebal####termloan DEBIT" + txMap);

                        txMap.put(TransferTrans.PARTICULARS, acno);
                        if (transmode.equalsIgnoreCase("CASH")) {
                            txMap.put(TransferTrans.PARTICULARS, " CASH");
                        }
                         if (prodType.equals("TermLoan")) {
                            txMap.put("SCREEN_NAME", "Loan Closing");
                        } else {
                            txMap.put("SCREEN_NAME", "Account Closing");
                        }
                        System.out.println("TransferTrans.PARTICULARS DEBIT" + txMap);
                        // transactionDAO.addTransferDebit(txMap, CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue()) ;
                        System.out.println("CRDR=================" + obj.get("BAL_DR_CR"));
                        System.out.println("addTransferDebit22222======" + txMap); //bbb
                        // if(obj.get("BAL_DR_CR")!=null && obj.get("BAL_DR_CR").equals("Dr"))
                        // transactionDAO.addTransferDebit(txMap, CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue()) ;
                        //  if(obj.get("BAL_DR_CR")!=null && obj.get("BAL_DR_CR").equals("Cr"))
                        transactionDAO.addTransferDebit(txMap, CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue());
                        ////end 
                    }
                } else {
                    System.out.println("$@#$@#$#@$#@$#@$#@"+obj.get("BAL_DR_CR"));
                      if (behaves != null && behaves.equals("OD") && obj.get("BAL_DR_CR").equals("Dr")) {
                        totAmt = (HashMap) obj.get("TOTAL_AMOUNT");
                        txMap = new HashMap();
                        txMap.put(TransferTrans.CR_PROD_ID, (String) acHeads.get("PROD_ID"));
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("ACCT_HEAD"));
                        txMap.put(TransferTrans.CR_ACT_NUM, accountClosingTO.getActNum());
                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put("AUTHORIZEREMARKS", "ACT_CLOSE");
                        txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put("generateSingleTransId", generateSingleTransId);
                        //                hash=(HashMap)obj.get("BEHAVE_LIKE");
                        //                            String behaves=CommonUtil.convertObjToStr(obj.get("BEHAVE_LIKE"));
                        System.out.println(totAmt + "behaves####@@@ n" + behaves);
                        if (behaves.equals("OD")) {
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                            txMap.put("TRANS_MOD_TYPE", "AD");
                        } else {
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
                            txMap.put("TRANS_MOD_TYPE", "TL");
                        }

                        System.out.println("getPayablebal####termloan DEBIT" + txMap);

                        txMap.put(TransferTrans.PARTICULARS, acno);
                        if (transmode.equalsIgnoreCase("CASH")) {
                            txMap.put(TransferTrans.PARTICULARS, " CASH");
                        }
                        System.out.println("TransferTrans.PARTICULARS DEBIT" + txMap);
                        // transactionDAO.addTransferDebit(txMap, CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue()) ;
                        System.out.println("CRDR=================sreee" + obj.get("BAL_DR_CR"));
                        System.out.println("addTransferDebit22222======sreee" + txMap); //bbb
                        // if(obj.get("BAL_DR_CR")!=null && obj.get("BAL_DR_CR").equals("Dr"))
                        // transactionDAO.addTransferDebit(txMap, CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue()) ;
                        //  if(obj.get("BAL_DR_CR")!=null && obj.get("BAL_DR_CR").equals("Cr"))
                        if (prodType.equals("TermLoan")) {
                            txMap.put("SCREEN_NAME", "Loan Closing");
                        } else {
                            txMap.put("SCREEN_NAME", "Account Closing");
                        }
                        transactionDAO.addTransferCredit(txMap, CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue());///bbbb
                        ////end 
                    } else // if(behaves!=null && behaves.equals("OD") && obj.get("BAL_DR_CR").equals("Cr"))
                    {
                        totAmt = (HashMap) obj.get("TOTAL_AMOUNT");
                        txMap = new HashMap();
                        txMap.put(TransferTrans.DR_PROD_ID, (String) acHeads.get("PROD_ID"));
                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("ACCT_HEAD"));//Nidhin changes
                        txMap.put(TransferTrans.DR_ACT_NUM, accountClosingTO.getActNum());
                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put("AUTHORIZEREMARKS", "DP");
                        txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put("generateSingleTransId", generateSingleTransId);
                        //                hash=(HashMap)obj.get("BEHAVE_LIKE");
                        //                            String behaves=CommonUtil.convertObjToStr(obj.get("BEHAVE_LIKE"));
                        System.out.println(totAmt + "behaves####@@@ " + behaves);
                        if (behaves.equals("OD")) {
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.ADVANCES);
                            txMap.put("TRANS_MOD_TYPE", "AD");
                        } else {
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.LOANS);
                            txMap.put("TRANS_MOD_TYPE", "TL");
                        }
                        System.out.println("getPayablebal####termloan DEBIT" + txMap);

                        txMap.put(TransferTrans.PARTICULARS, acno);
                        if (transmode.equalsIgnoreCase("CASH")) {
                            txMap.put(TransferTrans.PARTICULARS, " CASH");
                        }
                         if (prodType.equals("TermLoan")) {
                            txMap.put("SCREEN_NAME", "Loan Closing");
                        } else {
                            txMap.put("SCREEN_NAME", "Account Closing");
                        }
                        System.out.println("TransferTrans.PARTICULARS DEBIT" + txMap);
                        // transactionDAO.addTransferDebit(txMap, CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue()) ;
                        System.out.println("CRDR=================sree 222" + obj.get("BAL_DR_CR"));
                        System.out.println("addTransferDebit22222======sree 222" + txMap); //bbb
                        // if(obj.get("BAL_DR_CR")!=null && obj.get("BAL_DR_CR").equals("Dr"))
                        // transactionDAO.addTransferDebit(txMap, CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue()) ;
                        //  if(obj.get("BAL_DR_CR")!=null && obj.get("BAL_DR_CR").equals("Cr"))
                        transactionDAO.addTransferDebit(txMap, CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue());
                        ////end 
                    }
                    /*
                    HashMap hash = new HashMap();
                    totAmt = (HashMap) obj.get("TOTAL_AMOUNT");
                    txMap = new HashMap();
                    txMap.put(TransferTrans.CR_PROD_ID, (String) acHeads.get("PROD_ID"));
                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("ACCT_HEAD"));
                    txMap.put(TransferTrans.CR_ACT_NUM, accountClosingTO.getActNum());
                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    txMap.put("AUTHORIZEREMARKS", "ACT_CLOSE");
                    txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put("generateSingleTransId", generateSingleTransId);
                    System.out.println(totAmt + "behaves####@@@;[[[[" + behaves);
                    if (behaves.equals("OD")) {
                        txMap.put("TRANS_MOD_TYPE", "AD");
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                    } else {
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
                        txMap.put("TRANS_MOD_TYPE", "TL");
                    }
                    System.out.println("getPayablebal####termloan" + txMap);

                    txMap.put(TransferTrans.PARTICULARS, acno);
                    if (transmode.equalsIgnoreCase("CASH")) {
                        txMap.put(TransferTrans.PARTICULARS, " CASH");
                    }
                     if (prodType.equals("TermLoan")) {
                            txMap.put("SCREEN_NAME", "Loan Closing");
                        } else {
                            txMap.put("SCREEN_NAME", "Account Closing");
                        }
                    System.out.println("TransferTrans.PARTICULARS" + txMap);
                    System.out.println("addTransferCredit99999=====" + txMap);
                    transactionDAO.setLoanAmtMap(totAmt);
                    transactionDAO.addTransferCredit(txMap, CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue());
                    */
                }
                //
                accountClosingTO.setPayableBal(loanTempAmt);
            }
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

    private void doServiceTaxDebitCredit(HashMap txMap) throws Exception {
        System.out.println("INSIDE SERVICE TAX");
        HashMap serMap = new HashMap();
        serMap = (HashMap) txMap.get("stMap");
        TxTransferTO transferTo = null;
        ArrayList transferList = new ArrayList(); // for local transfer                 
        txMap.put(TransferTrans.CR_AC_HD, (String) serMap.get("SERVICE_TAX_HD_ID"));
        transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(serMap.get("SERVICE_TAX")).doubleValue());
        transferList.add(transferTo);
        txMap.put(TransferTrans.PARTICULARS, " Miscallenous Charges ServiceTax");
        transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(serMap.get("SERVICE_TAX")).doubleValue());
        transferList.add(transferTo);
        // //System.out.println("transferList:" + transferList);
        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
//        transactionDAO.setLoanDebitInt("OTHERCHARGES");
        transactionDAO.doTransferLocal(transferList, CommonUtil.convertObjToStr(txMap.get(TransferTrans.DR_BRANCH)));
        transactionDAO.setCommandMode(commandMode);
    }
    //LOAN ADVANCES AS AN CUSTOMER COMES

    public void insertAccHeadTLAD(AccountClosingTO accountClosingTO, HashMap obj) throws Exception {
        
        System.out.println("####generateSingleTransId:" + generateSingleTransId);
        ArrayList transList = null;
        HashMap notDeleted = new HashMap();
        String branchID = CommonUtil.convertObjToStr(obj.get(CommonConstants.BRANCH_ID));
        HashMap totAmt = new HashMap();
        acHeads = (HashMap) sqlMap.executeQueryForObject("getLoanAccountClosingHeads", accountClosingTO.getActNum());
        HashMap txMap = new HashMap();
        transactionDAO.setInitiatedBranch(_branchCode);
        transactionDAO.setLinkBatchID(accountClosingTO.getActNum());
        System.out.println("acHeads:" + acHeads);
        TxTransferTO transferTo = null;
        String behaves = "";
        Double loanTempAmt = new Double(0);
        // New mode
        if (CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue() > 0) {
            System.out.println("totAmt  ###" + totAmt);
            totAmt = (HashMap) obj.get("TOTAL_AMOUNT");
            behaves = CommonUtil.convertObjToStr(obj.get("BEHAVE_LIKE"));
            if (prodType.equals("TermLoan")) {
                HashMap hash = new HashMap();
                double reverseInt = 0.0;
                //                transAmt=transAmt-calculate_penal_int;
                totAmt = (HashMap) obj.get("TOTAL_AMOUNT");
                System.out.println("totAmt 1 ###" + totAmt);
                double negativeInt = CommonUtil.convertObjToDouble(totAmt.get("CURR_MONTH_INT")).doubleValue();
                System.out.println("negativeInt  ###" + negativeInt);
                if (negativeInt < 0.0  && !obj.containsKey("DEPOSITCLOSINGDAO")) {
                    reverseInt = CommonUtil.convertObjToDouble(totAmt.get("CURR_MONTH_INT")).doubleValue();
                    System.out.println("reverseInt  ###" + reverseInt);
                    reverseInt = reverseInt * (-1);
                    System.out.println("reverseInt  ###" + reverseInt);
                    txMap = new HashMap();
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("AC_DEBIT_INT"));
                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    txMap.put("AUTHORIZEREMARKS", "ACT_CLOSE");

                    txMap.put(TransferTrans.CR_PROD_ID, (String) acHeads.get("PROD_ID"));
                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("ACCT_HEAD"));
                    txMap.put(TransferTrans.CR_ACT_NUM, accountClosingTO.getActNum());
                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());

                    if (behaves.equals("OD")) {
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                        txMap.put("TRANS_MOD_TYPE", "AD");
                    } else {
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
                        txMap.put("TRANS_MOD_TYPE", "TL");
                    }
                    txMap.put("AUTHORIZEREMARKS", "ACT_CLOSE");
                    System.out.println("txmap  ####" + txMap);
                    txMap.put(TransferTrans.PARTICULARS, "Excess Interest");
                    txMap.put("generateSingleTransId", generateSingleTransId);
                    System.out.println("####generateSingleTransId:2" + generateSingleTransId);
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, reverseInt);
                    ArrayList transferList = new ArrayList();
                    transferList.add(transferTo);
                    txMap.put(TransferTrans.PARTICULARS, accountClosingTO.getActNum());
                    txMap.put("generateSingleTransId", generateSingleTransId);
                    System.out.println("####generateSingleTransId:3" + generateSingleTransId);
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, reverseInt);
                    if(prodType.equals("TermLoan")){
                    	transferTo.setScreenName("Loan Closing");   
                    }else{
                        transferTo.setScreenName("Account Closing");    
                    }
                    transferList.add(transferTo);
                    // //System.out.println("transferList:" + transferList);
                    transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                    if (transactionDAO.getLoanAmtMap() == null) {
                        transactionDAO.setLoanAmtMap(new HashMap());
                    }
                    transactionDAO.getLoanAmtMap().put("EXCESS_INTEREST", "EXCESS_INTEREST");
                    transactionDAO.doTransferLocal(transferList, branchID);
                    transactionDAO.setLoanAmtMap(new HashMap());

                }
                //SUBSIDY TRANSACTION STARTED

                if (totAmt != null && CommonUtil.convertObjToDouble(totAmt.get("AVAILABLE_SUBSIDY")).doubleValue() > 0) {
                    transferTo = new TxTransferTO();
                    HashMap dataMap = new HashMap();
                    dataMap.put("ACCT_NUM", accountClosingTO.getActNum());
                    transList = new ArrayList();
                    List list = (List) sqlMap.executeQueryForList("SelectTermLoanSubsidyAcctHeadDeatils", dataMap);
                    if (list != null && list.size() > 0) {
                        dataMap = (HashMap) list.get(0);
                    }
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.DR_AC_HD, dataMap.get("SUBSIDY_ADJUST_ACHD")); //(String)inputMap.get("AC_DEBIT_INT")
                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                    txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put("AUTHORIZEREMARKS", "LOAN_SUBSIDY");
                    txMap.put("DR_INST_TYPE", "LOAN_SUBSIDY");
                    txMap.put("DR_INSTRUMENT_2", "LOAN_SUBSIDY");
                    txMap.put("generateSingleTransId", generateSingleTransId);
                    System.out.println("####generateSingleTransId:4" + generateSingleTransId);
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(totAmt.get("AVAILABLE_SUBSIDY")).doubleValue());
                    if(prodType.equals("TermLoan")){
                    	transferTo.setScreenName("Loan Closing");   
                    }else{
                        transferTo.setScreenName("Account Closing");    
                    }
                    transList.add(transferTo);

                    if (behaves.equals("OD")) {
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                        txMap.put("TRANS_MOD_TYPE", "AD");
                    } else {
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
                        txMap.put("TRANS_MOD_TYPE", "TL");
                    }
                    //
                    txMap.put(TransferTrans.CR_AC_HD, dataMap.get("ACCT_HEAD"));
                    txMap.put(TransferTrans.CR_ACT_NUM, accountClosingTO.getActNum());
                    txMap.put(TransferTrans.CR_PROD_ID, dataMap.get("PROD_ID"));
                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    txMap.put("generateSingleTransId", generateSingleTransId);
                    System.out.println("####generateSingleTransId:5" + generateSingleTransId);
                    txMap.put(TransferTrans.PARTICULARS, "Subsidy Amt " + CommonUtil.convertObjToDouble(totAmt.get("AVAILABLE_SUBSIDY")).doubleValue());
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(totAmt.get("AVAILABLE_SUBSIDY")).doubleValue());
                    if(prodType.equals("TermLoan")){
                    	transferTo.setScreenName("Loan Closing");   
                    }else{
                        transferTo.setScreenName("Account Closing");    
                    }
                    transList.add(transferTo);
                    txMap = new HashMap();
                    System.out.println("transferList:" + transList);
                    transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                    transactionDAO.setLoanDebitInt("C*");
                    transactionDAO.setBreakLoanHierachy("Y");
                    transactionDAO.setLinkBatchID(accountClosingTO.getActNum());
                    transactionDAO.setInitiatedBranch(_branchCode);
                    transactionDAO.doTransferLocal(transList, _branchCode);
                }

                // END SUBSIDY TRANSACTION

                //REBATE TRANSACTION STARTED
                if (totAmt != null&&totAmt.containsKey("REBATE_MODE")&&totAmt.get("REBATE_MODE").equals("Transfer")  && CommonUtil.convertObjToDouble(totAmt.get("REBATE_INTEREST")).doubleValue() > 0) {
                    transferTo = new TxTransferTO();
                    HashMap dataMap = new HashMap();
                    dataMap.put("ACCT_NUM", accountClosingTO.getActNum());
                    transList = new ArrayList();
                    List list = (List) sqlMap.executeQueryForList("SelectTermLoanSubsidyAcctHeadDeatils", dataMap);
                    if (list != null && list.size() > 0) {
                        dataMap = (HashMap) list.get(0);
                    }
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.DR_AC_HD, acHeads.get("REBATE_INTEREST_ACHD")); //(String)inputMap.get("AC_DEBIT_INT")
                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                    txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put("AUTHORIZEREMARKS", "REBATE_INTEREST");
                    txMap.put("DR_INST_TYPE", "REBATE_INTEREST");
                    txMap.put("DR_INSTRUMENT_2", "REBATE_INTEREST");
                    txMap.put("generateSingleTransId", generateSingleTransId);
                    System.out.println("####generateSingleTransId:6" + generateSingleTransId);
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(totAmt.get("REBATE_INTEREST")).doubleValue());
                    if(prodType.equals("TermLoan")){
                    	transferTo.setScreenName("Loan Closing");   
                    }else{
                        transferTo.setScreenName("Account Closing");    
                    }
                    transList.add(transferTo);

                    if (behaves.equals("OD")) {
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                        txMap.put("TRANS_MOD_TYPE", "AD");
                    } else {
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
                        txMap.put("TRANS_MOD_TYPE", "TL");
                    }
                    //
                    txMap.put(TransferTrans.CR_AC_HD, dataMap.get("ACCT_HEAD"));
                    txMap.put(TransferTrans.CR_ACT_NUM, accountClosingTO.getActNum());
                    txMap.put(TransferTrans.CR_PROD_ID, dataMap.get("PROD_ID"));
                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    txMap.put("generateSingleTransId", generateSingleTransId);

                    txMap.put(TransferTrans.PARTICULARS, "Subsidy Amt " + CommonUtil.convertObjToDouble(totAmt.get("REBATE_INTEREST")).doubleValue());
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(totAmt.get("REBATE_INTEREST")).doubleValue());
                    if(prodType.equals("TermLoan")){
                    	transferTo.setScreenName("Loan Closing");   
                    }else{
                        transferTo.setScreenName("Account Closing");    
                    }
                    transList.add(transferTo);
                    txMap = new HashMap();
                    System.out.println("transferList:" + transList);
                    transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                    transactionDAO.setLoanDebitInt("C*");
                    transactionDAO.setBreakLoanHierachy("");
                    transactionDAO.setLinkBatchID(accountClosingTO.getActNum());
                    transactionDAO.setInitiatedBranch(_branchCode);
                    txMap.put("STRING_REBATE_INTEREST", "Y");
                    txMap.put("REBATE_INTEREST", totAmt.get("REBATE_INTEREST"));
                    transactionDAO.setLoanAmtMap(txMap);
                    transactionDAO.doTransferLocal(transList, _branchCode);

                }

                // END REBATE TRANSACTION




                txMap = new HashMap();

                if (CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue() > 0 && (!obj.containsKey("AUCTN_ACT"))) {
                    if (CommonUtil.convertObjToDouble(accountClosingTO.getActClosingChrg()).doubleValue() > 0) {
                        totAmt.put("ACT_CLOSING_CHARGE", accountClosingTO.getActClosingChrg());
                    }
                    if (CommonUtil.convertObjToDouble(accountClosingTO.getChrgDetails()).doubleValue() > 0) {
                        totAmt.put("ACT_CLOSING_MISC_CHARGE", accountClosingTO.getChrgDetails());
                    }
                    if (CommonUtil.convertObjToDouble(accountClosingTO.getInsuranceCharges()).doubleValue() > 0) {
                        totAmt.put("INSURANCE CHARGES", accountClosingTO.getInsuranceCharges());
                    }

                    txMap.put(TransferTrans.CR_PROD_ID, (String) acHeads.get("PROD_ID"));
                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("ACCT_HEAD"));
                    txMap.put(TransferTrans.CR_ACT_NUM, accountClosingTO.getActNum());
                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                    txMap.put("AUTHORIZEREMARKS", "ACT_CLOSE");
                    txMap.put("generateSingleTransId", generateSingleTransId);

                    System.out.println(totAmt + "behaves####@@@" + behaves);
                    if (behaves.equals("OD")) {
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                        txMap.put("TRANS_MOD_TYPE", "AD");
                    } else {
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
                        txMap.put("TRANS_MOD_TYPE", "TL");
                    }
                    System.out.println("getPayablebal####termloan" + txMap);

                    txMap.put(TransferTrans.PARTICULARS, acno);
                    if (transmode.equalsIgnoreCase("CASH")) {
                        txMap.put(TransferTrans.PARTICULARS, " CASH");
                    }
                    if (chargeLst != null && chargeLst.size() > 0) {   // Account Closing Charges
                        totAmt.put("ACT_CLOSING_CHARGES", chargeLst);
                    }
                    //added by chithra on 4-06-14
                    if (obj.containsKey("ADD_LOAN_INT_AMOUNT")) {
                        totAmt.put("ADD_LOAN_INT_AMOUNT", obj.get("ADD_LOAN_INT_AMOUNT"));
                    }
                    if (obj.containsKey("DEPOSIT_AMT")) {
                        totAmt.put("DEPOSIT_AMT", obj.get("DEPOSIT_AMT"));
                    }
                    if (obj.containsKey("DEPOSIT_INTEREST_AMT")) {
                        totAmt.put("DEPOSIT_INTEREST_AMT", obj.get("DEPOSIT_INTEREST_AMT"));
                    }
                    if (obj.containsKey("ADD_INT_AMOUNT")) {
                        totAmt.put("ADD_INT_AMOUNT", obj.get("ADD_INT_AMOUNT"));
                    }
                    System.out.println("TransferTrans.PARTICULARS" + txMap);
                    System.out.println("totAmt   @####" + totAmt);
                    if (totAmt != null && totAmt.size() > 0) {
                        transactionDAO.setLoanAmtMap(totAmt);
                    }
                    if(prodType.equals("TermLoan")){
                    	txMap.put("SCREEN_NAME","Loan Closing");   
                    }else{
                    	txMap.put("SCREEN_NAME","Account Closing");    
                    }
                    System.out.println("CommonUtil.convertObjToDouble(tto.getTransAmt()).doubleValue())" + CommonUtil.convertObjToDouble(tto.getTransAmt()).doubleValue());
                    if (obj.containsKey("DEPOSIT_TRANSACTION") && obj.get("DEPOSIT_TRANSACTION") != null) {
//                        reverseInt=CommonUtil.convertObjToDouble(totAmt.get("CURR_MONTH_INT")).doubleValue();
//                        if(reverseInt>0)
//                            transactionDAO.addTransferCredit(txMap, (CommonUtil.convertObjToDouble(tto.getTransAmt()).doubleValue()));
//                        else
                        System.out.println("addTransferCredit2222=====" + txMap);
                        transactionDAO.addTransferCredit(txMap, CommonUtil.convertObjToDouble(tto.getTransAmt()).doubleValue());
                        return;
                    }
                    System.out.println("addTransferCredit33333=====" + txMap);
                    if (reverseInt > 0) {
                        transactionDAO.addTransferCredit(txMap, (CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue() - reverseInt));
                    } else {
                        transactionDAO.addTransferCredit(txMap, CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue());
                    }

                }
                if (obj.containsKey("AUCTN_ACT")) {
                    boolean isInserted = false;
                    String aucType = CommonUtil.convertObjToStr(obj.get("AUCTION_TYPE"));
                    HashMap susMap = new HashMap();
                    HashMap goldLoanClosemap = new HashMap();
                    String suspenseHead ="";
                    String suspenseAcctNo = "";
                    SuspenseAccountMasterTO suspenseActmasterTo = null;
                    if(obj.containsKey("RETURN_MAP") && obj.get("RETURN_MAP") != null){
                        goldLoanClosemap = (HashMap)obj.get("RETURN_MAP");
                    }
                    if((isLoanCloseThroughGldLnAlwd != null && isLoanCloseThroughGldLnAlwd.equals("Y") && aucType != null && aucType.equals("Dr"))){
                       isInserted = false; 
                    }
                    if ((isLoanCloseThroughGldLnAlwd != null && isLoanCloseThroughGldLnAlwd.equals("Y") && aucType != null && aucType.equals("Cr"))) {
                        isInserted = true;
                    }
                    else if(isLoanCloseThroughGldLnAlwd != null && !isLoanCloseThroughGldLnAlwd.equals("Y") && aucType != null && aucType.equals("Cr")){
                       isInserted = true;  
                    }
                    else if(isLoanCloseThroughGldLnAlwd != null && !isLoanCloseThroughGldLnAlwd.equals("Y") && aucType != null && aucType.equals("Dr")){
                       isInserted = true;  
                    }else{
                       isInserted = false;  
                    }
                    if(isInserted){
                    if (goldLoanClosemap != null && goldLoanClosemap.containsKey("SuspenseAccountMasterTO") && goldLoanClosemap.get("SuspenseAccountMasterTO") != null) {
                        suspenseActmasterTo = (SuspenseAccountMasterTO) goldLoanClosemap.get("SuspenseAccountMasterTO");
                        sqlMap.executeUpdate("insertSuspenseAccountMaster", suspenseActmasterTo);
                        HashMap updateAmounts = new HashMap();
                        if (obj.containsKey("AUCTN_BALNC_AMT") && obj.get("AUCTN_BALNC_AMT") != null) {
                            updateAmounts.put("STATUS", CommonUtil.convertObjToStr(suspenseActmasterTo.getAuthorizeStatus()));
                            updateAmounts.put("USER_ID", CommonUtil.convertObjToStr(suspenseActmasterTo.getAuthorizeBy()));
                            updateAmounts.put("AUTHORIZEDT", DateUtil.getDateWithoutMinitues(suspenseActmasterTo.getAuthorizeDt()));
                            updateAmounts.put("SUSPENSE_ACCT_NUM", CommonUtil.convertObjToStr(suspenseActmasterTo.getTxtSuspenseActNum()));
                            sqlMap.executeUpdate("authorizeSuspenseAccountMaster", updateAmounts);
                            SuspenseTransaction sp = new SuspenseTransaction();//Added By Nidhin Amount Updation Problem in SuspenseMansrer table
                            if (aucType != null && aucType.equals("Dr")) {
                                updateAmounts.put(TransactionDAOConstants.UNCLEAR_AMT, 0);
                                updateAmounts.put(TransactionDAOConstants.ACCT_NO, CommonUtil.convertObjToStr(suspenseActmasterTo.getTxtSuspenseActNum()));
                                updateAmounts.put(TransactionDAOConstants.AMT, 1 * CommonUtil.convertObjToDouble(obj.get("AUCTN_BALNC_AMT")));
                            } else {
                                updateAmounts.put(TransactionDAOConstants.UNCLEAR_AMT, 0);
                                updateAmounts.put(TransactionDAOConstants.ACCT_NO, CommonUtil.convertObjToStr(suspenseActmasterTo.getTxtSuspenseActNum()));
                                updateAmounts.put(TransactionDAOConstants.AMT, -1 * CommonUtil.convertObjToDouble(obj.get("AUCTN_BALNC_AMT")));
                            }
                           // sp.performOtherBalanceMinus(updateAmounts);
                        }
//                        HashMap whereMap = new HashMap();
//                        whereMap.put("ACCT_NUM", CommonUtil.convertObjToStr(accountClosingTO.getActNum()));
//                        whereMap.put("STATUS", "Y");
//                        sqlMap.executeUpdate("updateSecurityReleaseDetails", whereMap);
                    }
                    }
                     if (!isInserted) {
                        HashMap whereMap = new HashMap();
                        whereMap.put("ACCT_NUM", CommonUtil.convertObjToStr(accountClosingTO.getActNum()));
                        whereMap.put("STATUS", "Y");
                        sqlMap.executeUpdate("updateSecurityReleaseDetails", whereMap);
                    }
                    if (goldLoanClosemap != null && goldLoanClosemap.containsKey("suspenseAcHd") && goldLoanClosemap.get("suspenseAcHd") != null
                            && goldLoanClosemap.containsKey("suspenseAcctNo") && goldLoanClosemap.get("suspenseAcctNo") != null) {
                        suspenseHead = CommonUtil.convertObjToStr(goldLoanClosemap.get("suspenseAcHd"));
                        suspenseAcctNo = CommonUtil.convertObjToStr(goldLoanClosemap.get("suspenseAcctNo"));
                    }
                    if (goldLoanClosemap != null && goldLoanClosemap.containsKey("SUS_PROD_ID") && goldLoanClosemap.get("SUS_PROD_ID") != null) {
                        susMap.put("prodId", CommonUtil.convertObjToStr(goldLoanClosemap.get("SUS_PROD_ID")));
                    }
                    List susList = sqlMap.executeQueryForList("getAccountHeadProdSAHead", susMap);
                    txMap = new HashMap();
                    if (susList != null && susList.size() > 0) {
                        susMap = (HashMap) susList.get(0);
                        String auctntype = CommonUtil.convertObjToStr(obj.get("AUCTION_TYPE"));
                        transList = new ArrayList();
                        if (transmode != null && transmode.equalsIgnoreCase(CommonConstants.TX_TRANSFER)) {
                            if (auctntype.equals("Cr")) {
                                if (CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue() > 0) {
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                    //txMap.put(TransferTrans.DR_AC_HD, acno); //(String)inputMap.get("AC_DEBIT_INT")
                                    //txMap.put(TransferTrans.DR_AC_HD, (String)acHeads.get("AC_DEBIT_INT"));
                                    txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(suspenseAcctNo));
                                    txMap.put("TRANS_MOD_TYPE", "SA");
                                    System.out.println("tto.getProductType() -----DDDDD-------- :"+tto.getProductType());
                                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(tto.getDebitAcctNo()));
                                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                    txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("AUTHORIZEREMARKS", "ACT_CLOSE");
                                    txMap.put("generateSingleTransId", generateSingleTransId);
                                    txMap.put(TransferTrans.PARTICULARS, "Loan closing amount");
                                    if (behaves.equals("OD")) {
                                        txMap.put("TRANS_MOD_TYPE", "AD");
                                    } else {
                                        txMap.put("TRANS_MOD_TYPE", "TL");
                                    }
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, (CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue() + CommonUtil.convertObjToDouble(obj.get("AUCTN_BALNC_AMT")).doubleValue()));
                                    if (prodType.equals("TermLoan")) {
                                        transferTo.setScreenName("Loan Closing");
                                    } else {
                                        transferTo.setScreenName("Account Closing");
                                    }
                                    transList.add(transferTo);
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.SUSPENSE);
                                    txMap.put(TransferTrans.CR_PROD_ID, (String) obj.get("SUS_PROD_ID"));
                                    txMap.put(TransferTrans.CR_AC_HD, (String) susMap.get("AC_HD_ID"));
                                    txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(suspenseAcctNo));
                                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                                    txMap.put("AUTHORIZEREMARKS", "ACT_CLOSE");
                                    txMap.put("TRANS_MOD_TYPE", "SA");
                                    txMap.put(TransferTrans.PARTICULARS, "Excess Auction amount to" + CommonUtil.convertObjToStr(suspenseAcctNo));
                                    txMap.put("generateSingleTransId", generateSingleTransId);


                                if (CommonUtil.convertObjToDouble(obj.get("AUCTN_BALNC_AMT")).doubleValue() > 0.0) {
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(obj.get("AUCTN_BALNC_AMT")).doubleValue());
                                    if (prodType.equals("TermLoan")) {
                                        transferTo.setScreenName("Loan Closing");
                                    } else {
                                        transferTo.setScreenName("Account Closing");
                                    }
                                    transList.add(transferTo);
                                }
                                if (CommonUtil.convertObjToDouble(accountClosingTO.getActClosingChrg()).doubleValue() > 0) {
                                    totAmt.put("ACT_CLOSING_CHARGE", accountClosingTO.getActClosingChrg());
                                }
                                if (CommonUtil.convertObjToDouble(accountClosingTO.getChrgDetails()).doubleValue() > 0) {
                                    totAmt.put("ACT_CLOSING_MISC_CHARGE", accountClosingTO.getChrgDetails());
                                }
                                if (CommonUtil.convertObjToDouble(accountClosingTO.getInsuranceCharges()).doubleValue() > 0) {
                                    totAmt.put("INSURANCE CHARGES", accountClosingTO.getInsuranceCharges());
                                }
                                txMap.put(TransferTrans.CR_PROD_ID, (String) acHeads.get("PROD_ID"));
                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("ACCT_HEAD"));
                                txMap.put(TransferTrans.CR_ACT_NUM, accountClosingTO.getActNum());
                                txMap.put(TransferTrans.CR_BRANCH, branchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                                txMap.put("AUTHORIZEREMARKS", "ACT_CLOSE");
                                txMap.put("generateSingleTransId", generateSingleTransId);

                                System.out.println(totAmt + "behaves####@@@" + behaves);
                                if (behaves.equals("OD")) {
                                    txMap.put("TRANS_MOD_TYPE", "AD");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                                } else {
                                    txMap.put("TRANS_MOD_TYPE", "TL");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
                                }
                                txMap.put(TransferTrans.PARTICULARS, "Auction amount "+(String) acHeads.get("ACCT_HEAD"));
                                if (chargeLst != null && chargeLst.size() > 0) {   // Account Closing Charges
                                    totAmt.put("ACT_CLOSING_CHARGES", chargeLst);
                                }
                                if (totAmt != null && totAmt.size() > 0) {
                                    transactionDAO.setLoanAmtMap(totAmt);
                                }
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue());
                                    if (prodType.equals("TermLoan")) {
                                        transferTo.setScreenName("Loan Closing");
                                    } else {
                                        transferTo.setScreenName("Account Closing");
                                    }
                                    transList.add(transferTo);
                                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                transactionDAO.setLoanDebitInt("C*");
                                transactionDAO.setBreakLoanHierachy("");
                                transactionDAO.setLinkBatchID(accountClosingTO.getActNum());
                                transactionDAO.setInitiatedBranch(_branchCode);
                                transactionDAO.doTransferLocal(transList, _branchCode);
                            }
                            //|Cr ends here

                            ///if auctn amt less than closing amt
                        } else if (auctntype.equals("Dr")) {
                            if (CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue() > 0) {
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                               // txMap.put("TRANS_MOD_TYPE", "SA");
                                txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(suspenseAcctNo));
                                //txMap.put(TransferTrans.DR_AC_HD, acno); //(String)inputMap.get("AC_DEBIT_INT")
                                System.out.println("acHeads --11-->"+acHeads);
                                //txMap.put(TransferTrans.DR_AC_HD, (String)acHeads.get("AC_DEBIT_INT"));
                                txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(tto.getDebitAcctNo()));
                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.PARTICULARS," Auction amount "+(String)acHeads.get("AC_DEBIT_INT"));
                                txMap.put("AUTHORIZEREMARKS", "ACT_CLOSE");
                                txMap.put("generateSingleTransId", generateSingleTransId);
                                if (tto.getProductType() != null && tto.getProductType().equals(TransactionFactory.GL)) {
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                }
                                if (tto.getProductType() != null && tto.getProductType().equals("SA")) {
                                    txMap.put("TRANS_MOD_TYPE", "SA");
                                }
                                if (tto.getProductType() != null && tto.getProductType().equals("OA")) {
                                    txMap.put("TRANS_MOD_TYPE", "OA");
                                }
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, (CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue() - CommonUtil.convertObjToDouble(obj.get("AUCTN_BALNC_AMT")).doubleValue()));
                                if (prodType.equals("TermLoan")) {
                                    transferTo.setScreenName("Loan Closing");
                                } else {
                                    transferTo.setScreenName("Account Closing");
                                }
                                transList.add(transferTo);

                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.SUSPENSE);
                               
                                txMap.put(TransferTrans.DR_PROD_ID, (String) obj.get("SUS_PROD_ID"));
                                txMap.put(TransferTrans.DR_AC_HD, (String) susMap.get("AC_HD_ID"));
                                txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(suspenseAcctNo));
                                txMap.put(TransferTrans.DR_BRANCH, branchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                                txMap.put("AUTHORIZEREMARKS", "ACT_CLOSE");
                                txMap.put("TRANS_MOD_TYPE", "SA");
                                txMap.put(TransferTrans.PARTICULARS, "Balance Auction amount From" + obj.get("SUS_ACT_NUM"));
                                txMap.put("generateSingleTransId", generateSingleTransId);
                                
                                if(isLoanCloseThroughGldLnAlwd != null && !isLoanCloseThroughGldLnAlwd.equals("Y")){
	                                if (CommonUtil.convertObjToDouble(obj.get("AUCTN_BALNC_AMT")).doubleValue() > 0.0) {
	                                    transferTo.setSingleTransId(generateSingleTransId);
	                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(obj.get("AUCTN_BALNC_AMT")).doubleValue());
                                            if (prodType.equals("TermLoan")) {
                                                transferTo.setScreenName("Loan Closing");
                                            } else {
                                                transferTo.setScreenName("Account Closing");
                                            }
                                            transList.add(transferTo);
	                                }
                                }
                                if (CommonUtil.convertObjToDouble(accountClosingTO.getActClosingChrg()).doubleValue() > 0) {
                                    totAmt.put("ACT_CLOSING_CHARGE", accountClosingTO.getActClosingChrg());
                                }
                                if (CommonUtil.convertObjToDouble(accountClosingTO.getChrgDetails()).doubleValue() > 0) {
                                    totAmt.put("ACT_CLOSING_MISC_CHARGE", accountClosingTO.getChrgDetails());
                                }
                                if (CommonUtil.convertObjToDouble(accountClosingTO.getInsuranceCharges()).doubleValue() > 0) {
                                    totAmt.put("INSURANCE CHARGES", accountClosingTO.getInsuranceCharges());
                                }
                                txMap.put(TransferTrans.CR_PROD_ID, (String) acHeads.get("PROD_ID"));
                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("ACCT_HEAD"));
                                txMap.put(TransferTrans.CR_ACT_NUM, accountClosingTO.getActNum());
                                txMap.put(TransferTrans.CR_BRANCH, branchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                                txMap.put("AUTHORIZEREMARKS", "ACT_CLOSE");
                                txMap.put("generateSingleTransId", generateSingleTransId);

                                System.out.println(totAmt + "behaves####@@@" + behaves);
                                if (behaves.equals("OD")) {
                                    txMap.put("TRANS_MOD_TYPE", "AD");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                                } else {
                                    txMap.put("TRANS_MOD_TYPE", "TL");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
                                }
                                txMap.put(TransferTrans.PARTICULARS, "Auction Amount ");
                                if (chargeLst != null && chargeLst.size() > 0) {   // Account Closing Charges
                                    totAmt.put("ACT_CLOSING_CHARGES", chargeLst);
                                }
                                if (totAmt != null && totAmt.size() > 0) {
                                    transactionDAO.setLoanAmtMap(totAmt);
                                }
                                double aucBalAmt = CommonUtil.convertObjToDouble(obj.get("AUCTN_BALNC_AMT"));
                                double totPayBalAmt = CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal());
                                if(isLoanCloseThroughGldLnAlwd != null && isLoanCloseThroughGldLnAlwd.equals("Y")){
                                    totPayBalAmt = totPayBalAmt - aucBalAmt;
                                }
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(totPayBalAmt));
                                if (prodType.equals("TermLoan")) {
                                    transferTo.setScreenName("Loan Closing");
                                } else {
                                    transferTo.setScreenName("Account Closing");
                                }
                                transList.add(transferTo);
                                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                transactionDAO.setLoanDebitInt("C*");
                                transactionDAO.setBreakLoanHierachy("");
                                transactionDAO.setLinkBatchID(accountClosingTO.getActNum());
                                transactionDAO.setInitiatedBranch(_branchCode);
                                transactionDAO.doTransferLocal(transList, _branchCode);
                            }
                        }
                    }else{
                                if (CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue() > 0) {
                                    
                                if (CommonUtil.convertObjToDouble(accountClosingTO.getActClosingChrg()).doubleValue() > 0) {
                                    totAmt.put("ACT_CLOSING_CHARGE", accountClosingTO.getActClosingChrg());
                                }
                                if (CommonUtil.convertObjToDouble(accountClosingTO.getChrgDetails()).doubleValue() > 0) {
                                    totAmt.put("ACT_CLOSING_MISC_CHARGE", accountClosingTO.getChrgDetails());
                                }
                                if (CommonUtil.convertObjToDouble(accountClosingTO.getInsuranceCharges()).doubleValue() > 0) {
                                    totAmt.put("INSURANCE CHARGES", accountClosingTO.getInsuranceCharges());
                                }
                                txMap.put(TransferTrans.CR_PROD_ID, (String) acHeads.get("PROD_ID"));
                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("ACCT_HEAD"));
                                txMap.put(TransferTrans.CR_ACT_NUM, accountClosingTO.getActNum());
                                txMap.put(TransferTrans.CR_BRANCH, branchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(CommonConstants.USER_ID, accountClosingTO.getStatusBy());
                                txMap.put("AUTHORIZEREMARKS", "ACT_CLOSE");
                                txMap.put("generateSingleTransId", generateSingleTransId);
                                txMap.put("SCREEN_NAME","Loan Closing");
                                if (behaves.equals("OD")) {
                                    txMap.put("TRANS_MOD_TYPE", "AD");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                                } else {
                                    txMap.put("TRANS_MOD_TYPE", "TL");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
                                }
                                txMap.put(TransferTrans.PARTICULARS, "Auction Amount ");
                                    if (prodType.equals("TermLoan")) {
                                    	txMap.put("SCREEN_NAME","Loan Closing");
                                    } else {
                                        txMap.put("SCREEN_NAME","Account Closing");
                                    }
                                if (chargeLst != null && chargeLst.size() > 0) {   // Account Closing Charges
                                    totAmt.put("ACT_CLOSING_CHARGES", chargeLst);
                                }
                                if (totAmt != null && totAmt.size() > 0) {
                                    transactionDAO.setLoanAmtMap(totAmt);
                                }
                                transactionDAO.addTransferCredit(txMap, CommonUtil.convertObjToDouble(CommonUtil.convertObjToDouble(0)));
                            }
                        }
                        ///dr ends
                        //Added by chithra 10077: Gold Loan Closing - Auctioned Account Closing Transaction Is Not Correct For Printing Purpose.
                        if (isInserted) {
                            HashMap where = new HashMap();
                            where.put("PROD_ID", CommonUtil.convertObjToStr(obj.get("SUSPANCE_PRODID")));
                            where.put("VALUE", CommonUtil.convertObjToStr(obj.get("NEWACCOUNT_NUM")));
                            where.put("BRANCH_CODE", _branchCode);
                            sqlMap.executeUpdate("updateCoreBankNextActNum", where);
                        }
                        TermLoanChargesTO termLoanChargeTO = null;
                        for (int i = 0; i < chargeLst.size(); i++) {
                            //To lOANS_ACCT_CHARGE_DETAILS
                            HashMap sing = (HashMap) chargeLst.get(i);
                            if (sing != null && sing.size() > 0) {
                                termLoanChargeTO = new TermLoanChargesTO();
                                termLoanChargeTO.setProd_Type(TransactionFactory.LOANS);
                                termLoanChargeTO.setProd_Id(accountClosingTO.getProdId());
                                termLoanChargeTO.setAct_num(accountClosingTO.getActNum());
                                termLoanChargeTO.setCharge_Type(CommonUtil.convertObjToStr(sing.get("CHARGE_DESC")));
                                termLoanChargeTO.setChargeDt(currDt);
                                termLoanChargeTO.setAmount(CommonUtil.convertObjToDouble(sing.get("CHARGE_AMOUNT")));
                                termLoanChargeTO.setStatus(CommonConstants.STATUS_CREATED);
                                termLoanChargeTO.setStatus_By(accountClosingTO.getStatusBy());
                                termLoanChargeTO.setStatus_Dt(currDt);
                                termLoanChargeTO.setAuthorize_Dt(null);
                                termLoanChargeTO.setAuthorize_by("");
                                termLoanChargeTO.setAuthorize_Status("");
                                termLoanChargeTO.setPaidAmount(CommonUtil.convertObjToDouble(sing.get("CHARGE_AMOUNT")));
                                termLoanChargeTO.setScreenName("Loan Closing");
                                System.out.println("termLoanChargeTO %$%$$$^$^$^$^$^$^" + termLoanChargeTO);
                                sqlMap.executeUpdate("insertTermLoanChargeTO", termLoanChargeTO);
                            }
                        }
                    } else {
                        throw new TTException("Auction A/c Head not set.");
                    }
                }
///auctn ends
            }
        }
    }

    /**
     * ******************************
     */
    private void deleteasAnwhenTransaction(HashMap obj) throws Exception {
        // If Edit mode
        String behaves = CommonUtil.convertObjToStr(obj.get("BEHAVE_LIKE"));
        if (obj.containsKey(CommonConstants.AUTHORIZEMAP)) {
            obj.remove(CommonConstants.AUTHORIZEMAP);
        }
        if (obj.containsKey("CASH_TRANS_DETAILS")) {
            System.out.println("@@@@@@inside CASH_TRANS_DETAILS");
            HashMap tempMap = (HashMap) obj.get("CASH_TRANS_DETAILS");
            CashTransactionTO txTransferTO = null;
            HashMap oldAmountMap = new HashMap();
            if (accountClosingTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                tempMap.put("TRANS_DT", currDt.clone());
                tempMap.put("INITIATED_BRANCH", _branchCode);
                List lst = sqlMap.executeQueryForList("getSelectCashTransactionTO", tempMap);
                if (lst != null) {
                    txTransferTO = (CashTransactionTO) lst.get(0);
                    txTransferTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                    txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                    txTransferTO.setStatusDt(ServerUtil.getCurrentDateProperFormat(_branchCode));
                    txTransferTO.setParticulars("By Cash");
                    obj.put("CashTransactionTO", txTransferTO);
                    obj.put(TransactionDAOConstants.OLDAMT, new Double(0));
                    CashTransactionDAO cashTransDAO = new CashTransactionDAO();
                    cashTransDAO.execute(obj, false);
                }
                lst = null;
            }
            txTransferTO = null;
            oldAmountMap = null;
            tempMap = null;
            accountClosingTO.setPayableBal(new Double(0));
        }

        if (obj.containsKey("TRANSFER_TRANS_DETAILS")) {
            System.out.println("@@@@@@inside TRANSFER_TRANS_DETAILS");
            HashMap tempMap = (HashMap) obj.get("TRANSFER_TRANS_DETAILS");
            tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
            tempMap.put("TRANS_DT", currDt.clone());
            tempMap.put("INITIATED_BRANCH", _branchCode);
            double oldAmount = 0;
            if (tempMap.containsKey("AMOUNT")) {
                oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                tempMap.remove("AMOUNT");
            }
            if (tempMap.containsKey("TRANS_ID")) {
                tempMap.remove("TRANS_ID");
            }


            ArrayList batchList = new ArrayList();
            TxTransferTO txTransferTO = null;
            HashMap oldAmountMap = new HashMap();
            if (accountClosingTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                if (lst != null) {
                    for (int i = 0; i < lst.size(); i++) {
                        txTransferTO = (TxTransferTO) lst.get(i);
                        txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                        txTransferTO.setStatusDt(ServerUtil.getCurrentDateProperFormat(_branchCode));
                        txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                        oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                        batchList.add(txTransferTO);
                    }
                }
                TransferTrans transferTrans = new TransferTrans();
                transferTrans.setOldAmount(oldAmountMap);
                transferTrans.setInitiatedBranch(_branchCode);
                transferTrans.doDebitCredit(batchList, _branchCode, false, commandMode);
                lst = null;
                transferTrans = null;
            }
            txTransferTO = null;
            batchList = null;
            oldAmountMap = null;
            tempMap = null;
            accountClosingTO.setPayableBal(new Double(0));
        }
    }

    /**
     * To insert Data
     */
    private void insertData(HashMap obj) throws Exception {
        // //System.out.println("obj in insert Data : " + obj);
        System.out.println("insertData HashMapobj" + obj);
        String odInCreditBalance = "N"; // Added by nithya on 15-04-2020 for KD 889
        accountTO = new AccountTO();
        HashMap retMap = new HashMap();
        if (obj.containsKey("RETURN_MAP") && obj.get("RETURN_MAP") != null) {
            retMap = (HashMap) obj.get("RETURN_MAP");
        }
        if (obj.containsKey("IS_AN_AUCTION_ACCT") && obj.get("IS_AN_AUCTION_ACCT") != null) {
            if (CommonUtil.convertObjToStr(obj.get("IS_AN_AUCTION_ACCT")).equalsIgnoreCase("Y")) {
                accountTO.setRemarks("Gold Acution");
            }
        }
        if (retMap.containsKey("isLoanCloseThroughGldLnAlwd") && retMap.get("isLoanCloseThroughGldLnAlwd") != null) {
            if (CommonUtil.convertObjToStr(retMap.get("isLoanCloseThroughGldLnAlwd")).equalsIgnoreCase("Y")) {
                isLoanCloseThroughGldLnAlwd = CommonUtil.convertObjToStr(retMap.get("isLoanCloseThroughGldLnAlwd"));
            }else{
                isLoanCloseThroughGldLnAlwd = "";
            }
        }
        accountTO.setActNum(accountClosingTO.getActNum());
        accountTO.setActStatusId(CommonConstants.CLOSED);
        accountTO.setClosedDt(currDt);
        accountTO.setActStatusDt(currDt);
        System.out.println("insertData accountTO" + accountTO);
        System.out.println("insertData accountClosingTO" + accountClosingTO);
        if (obj.containsKey("DEPOSIT_TRANSACTION")) {
            accountClosingTO.setRemarks(CommonUtil.convertObjToStr(obj.get("DEPOSIT_TRANSACTION")));
        } else if (obj.containsKey("AUCTN_ACT")) {
            accountClosingTO.setRemarks("AUCTIONED_ACCOUNT");
        } else {
            accountClosingTO.setRemarks("");
        }
        //Added By Kannan AR to avoid duplicate transactions (JIRA : KDSA-299)
        if (!CommonUtil.convertObjToStr(accountClosingTO.getActNum()).equals("")) {
            HashMap actMap = new HashMap();
            actMap.put("ACT_NUM", accountClosingTO.getActNum());            
            List actList = sqlMap.executeQueryForList("getClosedAccounts", actMap);
            if (actList != null && actList.size() > 0) {
                throw new TTException("Account Closure not allowed. Account already closed or authorization pending for " + CommonUtil.convertObjToStr(accountClosingTO.getActNum()));                
            }
        }
        sqlMap.executeUpdate("insertAccountClosingTO", accountClosingTO);
        if(prodType.equals("TermLoan")){
            if(obj.containsKey("LOAN_RENEWAL_MAP") && obj.get("LOAN_RENEWAL_MAP") != null){
                HashMap renewalMap = (HashMap)(obj.get("LOAN_RENEWAL_MAP"));
                if(renewalMap != null && renewalMap.containsKey("LOAN_OD_RENEWAL") && renewalMap.get("LOAN_OD_RENEWAL") != null && renewalMap.get("LOAN_OD_RENEWAL").equals("LOAN_OD_RENEWAL")){
                    renewalMap.put("CLOSE_DT",currDt.clone());
                    renewalMap.put("STATUS","PROGRESS");
                    renewalMap.put("CLOSING_AMOUNT",accountClosingTO.getPayableBal());
                    sqlMap.executeUpdate("insertLoanODRenewalDetails", renewalMap);
//                    (#CLOSING_LOAN_NO:VARCHAR#, #DEBIT_ACCT_NUM:VARCHAR#, #DEBIT_PROD_TYPE:VARCHAR#, #CLOSE_DT:DATE#, 
//                    #CLOSING_AMOUNT:NUMERIC# #STATUS:VARCHAR#, #NEW_LOAN_NO:VARCHAR#)
                }
            }
        }
        if(obj.containsKey("OD_CREDIT_BALANCE") && obj.get("OD_CREDIT_BALANCE") != null && CommonUtil.convertObjToStr(obj.get("OD_CREDIT_BALANCE")).equalsIgnoreCase("OD_CREDIT_BALANCE")){ // Added by nithya on 15-04-2020 for KD 889
            odInCreditBalance = "Y";
        }
            

        //Do ac/head insert here
        if (prodType.equals("TermLoan") && asAnWhen.equals("Y") && !odInCreditBalance.equals("Y")) { // Added by nithya on 15-04-2020 for KD 889
            if (obj.containsKey("AUTHORIZEMAP")) {
                obj.put("AUTHORIZEMAP", null);
            }
            insertAccHeadTLAD(accountClosingTO, obj);
        } else {
            insertAccHead(accountClosingTO, obj);
        }
//        if (prodType.equals("TermLoan")) {
//            if (obj.containsKey("LTD_CLOSING_STATUS") && CommonUtil.convertObjToStr(obj.get("LTD_CLOSING_STATUS")).equals("DONT_CLOSE")) {
//            } else {
//                if(isLoanCloseThroughGldLnAlwd != null && !isLoanCloseThroughGldLnAlwd.equals("") && isLoanCloseThroughGldLnAlwd.equals("Y")){
//                    if(obj.containsKey("AUCTION_TYPE") && obj.get("AUCTION_TYPE") != null && CommonUtil.convertObjToStr(obj.get("AUCTION_TYPE")).equals("Dr")){
//                    accountTO.setActStatusId("NEW");
//                    accountTO.setActStatusDt(null);
//                    accountTO.setClosedDt(null);
//                    }
//                }
//                sqlMap.executeUpdate("updateLoanActClosingDetail", accountTO);
//            }
//            if(obj.containsKey("SERVICETAXDETAILSTO_DEP")){
//                ServiceTaxDetailsTO objserviceTaxDetailsTO = (ServiceTaxDetailsTO)obj.get("SERVICETAXDETAILSTO_DEP");
//                objserviceTaxDetailsTO.setAcct_Num(accountClosingTO.getActNum());
//                insertServiceTaxDetails(objserviceTaxDetailsTO);
//            }
//
//        } else {
//            sqlMap.executeUpdate("updateAccountMasterStatusId", accountTO);
//        }
        if (obj.containsKey("serviceTaxDetailsTO")) {
            ServiceTaxDetailsTO objserviceTaxDetailsTO = (ServiceTaxDetailsTO) obj.get("serviceTaxDetailsTO");
            insertServiceTaxDetails(objserviceTaxDetailsTO);
        }
       
    }
    private void insertServiceTaxDetails(ServiceTaxDetailsTO objserviceTaxDetailsTO) {
        try {
            objserviceTaxDetailsTO.setServiceTaxDet_Id(getServiceTaxNo());
            if (!prodType.equals("TermLoan")) {
                objserviceTaxDetailsTO.setParticulars("Accout Closing");
            } else {
                objserviceTaxDetailsTO.setParticulars("Loan Closing");
            }
            sqlMap.executeUpdate("insertServiceTaxDetailsTO", objserviceTaxDetailsTO);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    private String getServiceTaxNo() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "SERVICETAX_DET_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }
    /**
     * To update data
     */
    private void updateData(HashMap obj) throws Exception {
        //Do ac/head insert here
        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_UPDATE);

        //        if(obj.containsKey("DEPOSIT_PREMATURE_CLOSER"))
        //            insertAccHeadPreMature(accountClosingTO, obj);
        //        else
        insertAccHead(accountClosingTO, obj);
        sqlMap.executeUpdate("updateAccountClosingTO", accountClosingTO);
    }

    /**
     * To delete data
     */
    private void deleteData(HashMap obj) throws Exception {
        accountTO = new AccountTO();
        accountTO.setActNum(accountClosingTO.getActNum());
        accountTO.setActStatusId("OPERATIONAL");
        accountTO.setActStatusDt(currDt);
        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_DELETE);
        //        if(obj.containsKey("DEPOSIT_PREMATURE_CLOSER"))
        //            insertAccHeadPreMature(accountClosingTO, obj);
        //        else
        if (prodType.equals("TermLoan") && asAnWhen.equals("Y")) {
            deleteasAnwhenTransaction(obj);
        } else {
            insertAccHead(accountClosingTO, obj);
        }
        sqlMap.executeUpdate("deleteAccountClosingTO", accountClosingTO);
        if (prodType.equals("TermLoan")) {
            sqlMap.executeUpdate("updateLoanActClosingDetail", accountTO);
        } else {
            HashMap where = new HashMap();
            where.put(CommonConstants.ACT_NUM, accountClosingTO.getActNum());
            where = (HashMap) sqlMap.executeQueryForObject("getStatusForAccount", where);
            if (where != null && where.size() > 0) {
                System.out.println("@@@Now Status " + where);
                java.sql.Timestamp timeStamp = (java.sql.Timestamp) where.get("CLOSED_DT");
                timeStamp.setTime(timeStamp.getTime() - 1000);
                System.out.println("@@@Status " + where);
                where = (HashMap) sqlMap.executeQueryForObject("getStatusForAccount", where);
                if (where != null && where.size() > 0) {
                    System.out.println("@@@Previous Status " + where);
                    sqlMap.executeUpdate("updateStatusForAccount", where);
                }
            }
            where = null;
            //            sqlMap.executeUpdate("updateAccountMasterStatusId", accountTO);
        }
        if (obj.containsKey("TransactionTO") && obj.get("TransactionTO") != null) {
            TransactionTO transactionTODebit = new TransactionTO();
            LinkedHashMap transactionDetailsMap = new LinkedHashMap();
            TransactionTO transactionTO;
            transactionDetailsMap = (LinkedHashMap) obj.get("TransactionTO");
            LinkedHashMap allowedTransDetailsTO;
            if (transactionDetailsMap.size() > 0) {
                if (transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs") && transactionDetailsMap.get("NOT_DELETED_TRANS_TOs") != null) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                    if (allowedTransDetailsTO != null && allowedTransDetailsTO.size() > 0) {
                        transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                        transactionTO.setStatus(CommonConstants.STATUS_DELETED);
                        transactionTO.setBranchId(_branchCode);
                        sqlMap.executeUpdate("deleteRemittanceIssueTransactionTO", transactionTO);
                    }
                }
            }
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        return execute(map, true);
    }

    /**
     * Standard method to execute necessary functionality
     */
    public HashMap execute(HashMap obj, boolean isTransaction) throws Exception {
        System.out.println(">>>>>>>>>> obj : " + obj);
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        generateSingleTransId = generateLinkID();
        System.out.println("single trans id in execute"+generateSingleTransId);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        HashMap notdeleted = null;
        tto = null;
        if (obj.containsKey("TransactionTO") && obj.get("TransactionTO") != null) {
            notdeleted = (HashMap) obj.get("TransactionTO");
            transactionDetailsMap = (LinkedHashMap) obj.get("TransactionTO");
            if (notdeleted.containsKey("NOT_DELETED_TRANS_TOs") && notdeleted.get("NOT_DELETED_TRANS_TOs") != null) {
                notdeleted = (HashMap) notdeleted.get("NOT_DELETED_TRANS_TOs");
                if (notdeleted.containsKey("1")) {
                    tto = new TransactionTO();
                    tto = (TransactionTO) notdeleted.get("1");
                }
            }
        }
        //loans and advances as an when customer comes
        if (obj.containsKey("AS_CUSTOMER_COMES") && obj.get("AS_CUSTOMER_COMES") != null) {
            asAnWhen = CommonUtil.convertObjToStr(obj.get("AS_CUSTOMER_COMES"));
        } else {
            asAnWhen = "";
        }
        System.out.println("tto " + tto);
        if (tto != null) {
            acno = CommonUtil.convertObjToStr(tto.getDebitAcctNo());
            if (tto.getProductType() != null && tto.getProductType().equals("AB") && tto.getInstType() != null && tto.getInstType().equals("CHEQUE")) {
                inst_type = CommonUtil.convertObjToStr(tto.getInstType());
                chqno2 = CommonUtil.convertObjToStr(tto.getChequeNo2());
            } else {
                inst_type = "";
                chqno2 = "";
            }
        }
        System.out.println(">>>>>>>>>> acno : " + acno);
        if (tto != null) {
            transmode = CommonUtil.convertObjToStr(tto.getTransType());
        }
        prodType = "";
        if (obj.containsKey("PROD_TYPE")) {
            prodType = CommonUtil.convertObjToStr(obj.get("PROD_TYPE"));
        }
        //Test Code, to be run once...
        /*
         * CommonUtil.serializeObjWrite("D:\\myfile1.txt", obj);
         */
        //End of Test Code
        chargeLst = null;
        if (obj.containsKey("Charge List Data")) {
            chargeLst = (List) obj.get("Charge List Data");
            System.out.println("@##$#$% chargeLst #### :" + chargeLst);
        }
        HashMap depMap = new HashMap();
        depMap = (HashMap) obj.get("DEPMAP");
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        if (prodType.equals("TermLoan")) {
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        } else {
            transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
        }
        System.out.println("ERTEYEETEG=====" + obj.get("BAL_DR_CR"));
        if (prodType.equals("TermLoan"))//babuv
        {
            if (obj.containsKey("ADVANCES_CREDIT_INT") && CommonUtil.convertObjToDouble(obj.get("ADVANCES_CREDIT_INT")).doubleValue() > 0
                    && (obj.get("BAL_DR_CR") != null && obj.get("BAL_DR_CR").equals("Dr"))) {
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);///bbb
                //  transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
            }
        }


        transactionDAO.setInitiatedBranch(_branchCode);
        if (obj.containsKey("MODE")) {
            commandMode = CommonUtil.convertObjToStr(obj.get("MODE"));
            transactionDAO.setCommandMode(CommonUtil.convertObjToStr(obj.get("MODE")));
        }
        accountClosingTO = (AccountClosingTO) obj.get("accountclosing");
        if (depMap != null) {
            transactionDAO.setTransType(CommonConstants.DEBIT);
        }
        //////////////////////////////////////////////////
        //  if(prodType.equals("TermLoan"))//babuv
        //    if(obj.containsKey("ADVANCES_CREDIT_INT") && CommonUtil.convertObjToDouble(obj.get("ADVANCES_CREDIT_INT")).doubleValue()>0
        //            && (obj.get("BAL_DR_CR")!=null && obj.get("BAL_DR_CR").equals("Dr"))) {
        //       transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
        //       System.out.println("35432432432====="+obj.get("BAL_DR_CR"));
        //    }
        //  else
        //   transactionDAO = new TransactionDAO(CommonConstants.CREDIT); 
        if (obj.get("BEHAVE_LIKE") != null && obj.get("BEHAVE_LIKE").equals("OD") && obj.get("BAL_DR_CR").equals("Dr")) {
            System.out.println("566666666666666666666666=====" + obj.get("BEHAVE_LIKE"));
            transactionDAO.setTransType(CommonConstants.DEBIT);///bbb
        } else if (obj.get("BEHAVE_LIKE") != null && obj.get("BEHAVE_LIKE").equals("OD") && obj.get("BAL_DR_CR").equals("Cr")) {
            transactionDAO.setTransType(CommonConstants.CREDIT);///bbb
        }

        // //System.out.println("accountClosingTO:" + accountClosingTO);
        if (obj.get("LIEN") != null) {
            setLienAmount(CommonUtil.convertObjToDouble(obj.get("LIEN")).doubleValue());
        } else {
            setLienAmount(0);
        }

        if (obj.get("FREEZE") != null) {
            setFreezeAmount(CommonUtil.convertObjToDouble(obj.get("FREEZE")).doubleValue());
        } else {
            setFreezeAmount(0);
        }

        if (obj.containsKey("ADVANCES_CREDIT_INT") && obj.get("ADVANCES_CREDIT_INT") != null) {
            setAdvancesCreditInterest(CommonUtil.convertObjToDouble(obj.get("ADVANCES_CREDIT_INT")));
        } else {
            setAdvancesCreditInterest(new Double(0));
        }


        //To perform corresponding actions
        try {
            if (isTransaction && !obj.containsKey("DEPOSIT_TRANSACTION")) {

                sqlMap.startTransaction();
            }



            //            if(obj!=null)
            //            return obj;
            actionPerform(obj);
            if (isTransaction && !obj.containsKey("DEPOSIT_TRANSACTION")) {
                sqlMap.commitTransaction();
            }
        } catch (Exception transError) {
            transError.printStackTrace();
            if (isTransaction) {
                sqlMap.rollbackTransaction();
            }
            throw transError;
        }

        obj = null;
        destroyObjects();
        System.out.println("@@@map" + resultMap);
        if (isTransaction) {
            ServiceLocator.flushCache(sqlMap);
        }
        System.gc();
        return resultMap;
    }

    private void actionPerform(HashMap obj) throws Exception {
        System.out.println("###actionPerformobj" + obj);
        transactionDAO.setInitiatedBranch(_branchCode);
        System.out.println("accountClosingTO.getCommand()" + accountClosingTO.getCommand());
        obj.put("ACCOUNT_CLOSING", "ACCOUNT_CLOSING");
        obj.put("generateSingleTransId",generateSingleTransId);
        if (obj.containsKey("AUTHORIZEMAP") && (obj.get("AUTHORIZEMAP") == null || ((HashMap) obj.get("AUTHORIZEMAP")).isEmpty())) {
            if (accountClosingTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                // //System.out.println("obj:" + obj);
                insertData(obj);
                transactionDAO.setBatchId(accountClosingTO.getActNum());
                transactionDAO.setBatchDate(currDt);
                resultMap = new HashMap();
                resultMap.put("ACT_NUM", accountClosingTO.getActNum());
            } else if (accountClosingTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData(obj);
            } else if (accountClosingTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData(obj);
            } else {
                throw new NoCommandException();
            }
            if (prodType.equals("TermLoan")) {
                lienCancel(obj);
                obj.put(TransferTrans.PARTICULARS, "A/c No " + "" + accountClosingTO.getActNum());
            }
        } else if (obj.containsKey("AUTHORIZEMAP") && obj.get("AUTHORIZEMAP") != null) {
            // //System.out.println("In Authorize .... of A/c Closing DAO ");
            if (prodType.equals("TermLoan")) {
                lienCancel(obj);
            }
            doAuthorize(obj);
        }
        System.out.println("accountClosingTO.getCommand()" + accountClosingTO.getCommand());
        if (obj.containsKey("AUTHORIZEMAP") && (obj.get("AUTHORIZEMAP") == null || ((HashMap) obj.get("AUTHORIZEMAP")).isEmpty())) {
            if (accountClosingTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)
                    || accountClosingTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)
                    || accountClosingTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                System.out.println("###FINAL TRANSACTION   :" + obj);
                /**
                 * **************************************************
                 */
                if ((!prodType.equals("TermLoan")) && (tto != null && tto.getProductType().equals("RM"))) {
//                    if(accountClosingTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
                    double closeAmt = CommonUtil.convertObjToDouble(tto.getTransAmt()).doubleValue();
//                     String instNo1 = "";
//                     String instNo2 = "";
                    obj.put("generateSingleTransId", generateSingleTransId);
                    obj.put("TransactionTO", transactionDetailsMap);
                    transactionDAO.execute(obj);
                    LinkedHashMap notDeleteMap = new LinkedHashMap();
                    LinkedHashMap transferMap = new LinkedHashMap();
                    HashMap remittanceMap = new HashMap();
                    remittanceIssueDAO = new RemittanceIssueDAO();
                    remittanceIssueTO = new RemittanceIssueTO();
//                                        String favouringName = tto.getDebitAcctNo();
                    transactionTODebit.setApplName(CommonUtil.convertObjToStr(tto.getDebitAcctNo()));
                    transactionTODebit.setTransAmt(new Double(closeAmt));
                    transactionTODebit.setProductId(accountClosingTO.getProdId());
                    transactionTODebit.setProductType("OA");
                    transactionTODebit.setDebitAcctNo(CommonUtil.convertObjToStr(accountClosingTO.getActNum()));
                    transactionTODebit.setTransType(CommonUtil.convertObjToStr(tto.getTransType()));
                    remittanceIssueDAO.setFromotherDAo(false);
                    notDeleteMap.put(String.valueOf(1), transactionTODebit);
                    transferMap.put("NOT_DELETED_TRANS_TOs", notDeleteMap);
                    remittanceMap.put("TransactionTO", transferMap);
                    remittanceMap.put(CommonConstants.BRANCH_ID, _branchCode);
                    remittanceMap.put("OPERATION_MODE", "ISSUE");
                    remittanceMap.put("AUTHORIZEMAP", null);
                    remittanceMap.put("USER_ID", obj.get("USER_ID"));
                    remittanceMap.put("MODULE", "Remittance");
                    remittanceMap.put("MODE", accountClosingTO.getCommand());
                    remittanceMap.put("SCREEN", "Issue");
                    HashMap behavesMap = new HashMap();
                    behavesMap.put("BEHAVES_LIKE", "PO");
                    List lstRemit = sqlMap.executeQueryForList("selectRemitProductId", behavesMap);
                    if (lstRemit != null && lstRemit.size() > 0) {
                        behavesMap = (HashMap) lstRemit.get(0);
                    }
                    HashMap draweeMap = new HashMap();
                    if (behavesMap.get("PAY_ISSUE_BRANCH").equals("ISSU_BRANCH")) {
                        lstRemit = sqlMap.executeQueryForList("getSelectBankTOList", null);
                        if (lstRemit != null && lstRemit.size() > 0) {
                            draweeMap = (HashMap) lstRemit.get(0);
                        }
                    }
                    remittanceIssueTO.setAmount(new Double(closeAmt));
                    remittanceIssueTO.setCategory("GENERAL_CATEGORY");
                    remittanceIssueTO.setDraweeBranchCode(_branchCode);
                    remittanceIssueTO.setDraweeBank(CommonUtil.convertObjToStr(draweeMap.get("BANK_CODE")));
                    remittanceIssueTO.setProdId(CommonUtil.convertObjToStr(behavesMap.get("PROD_ID")));
                    remittanceIssueTO.setCity("560");
                    remittanceIssueTO.setFavouring(CommonUtil.convertObjToStr(tto.getDebitAcctNo()));
                    remittanceIssueTO.setBranchId(_branchCode);
                    remittanceIssueTO.setRemarks(CommonUtil.convertObjToStr(accountClosingTO.getActNum()));
                    remittanceIssueTO.setCommand(accountClosingTO.getCommand());
                    remittanceIssueTO.setTotalAmt(new Double(closeAmt));
                    remittanceIssueTO.setExchange(new Double(0.0));
                    remittanceIssueTO.setPostage(new Double(0.0));
                    remittanceIssueTO.setOtherCharges(new Double(0.0));
                    remittanceIssueTO.setStatusDt(currDt);
                    remittanceIssueTO.setAuthorizeRemark("OP_ACT_PAY_ORDER");
                    remittanceIssueTO.setInstrumentNo1(CommonUtil.convertObjToStr(tto.getChequeNo()));
                    remittanceIssueTO.setInstrumentNo2(CommonUtil.convertObjToStr(tto.getChequeNo2()));
                    remittanceIssueTO.setStatusBy(CommonUtil.convertObjToStr(obj.get("USER_ID")));
                    remittanceIssueTO.setVariableNo(CommonUtil.convertObjToStr(accountClosingTO.getVariableNo()));
                    LinkedHashMap remitMap = new LinkedHashMap();
                    LinkedHashMap remMap = new LinkedHashMap();
                    remMap.put(String.valueOf(1), remittanceIssueTO);
                    remitMap.put("NOT_DELETED_ISSUE_TOs", remMap);
                    remittanceMap.put("RemittanceIssueTO", remitMap);
                    System.out.println(" remittanceMap :" + remittanceMap);
                    if (accountClosingTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
//                                            HashMap tempReturnMap = new HashMap();
//                                            HashMap tempMap = new HashMap();
//                                            if (obj.containsKey("TRANSFER_TRANS_DETAILS") && CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue()>0) {
//                                                System.out.println("@@@@@@inside TRANSFER_TRANS_DETAILS");
//                                                tempMap.put("TRANSFER_TRANS_DETAILS", obj.get("TRANSFER_TRANS_DETAILS"));
//                                                tempReturnMap.put(remittanceIssueTO.getVariableNo(), tempMap);
//                                                System.out.println("@@@@@@tempReturnMap tempReturnMap"+tempReturnMap);
//                                            }
//                                            remittanceMap.put("TransactionDetails", tempReturnMap);
                        HashMap tempReturnMap = new HashMap();
                        HashMap getTransMap = new HashMap();
                        HashMap tempMap = new HashMap();
                        if (obj.containsKey("TRANSFER_TRANS_DETAILS") && CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue() > 0) {
                            System.out.println("@@@@@@inside TRANSFER_TRANS_DETAILS");
                            getTransMap.put("TRANS_ID", "");
                            getTransMap.put("BATCH_ID", "");
                            getTransMap.put("AMOUNT", remittanceIssueTO.getTotalAmt());
                            tempMap.put("TRANSFER_TRANS_DETAILS", getTransMap);
                            tempReturnMap.put(remittanceIssueTO.getVariableNo(), tempMap);
                            System.out.println("@@@@@@tempReturnMap tempReturnMap" + tempReturnMap);
                        }
                        remittanceMap.put("TransactionDetails", tempReturnMap);
                    }
                    if (accountClosingTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                        HashMap tempReturnMap = new HashMap();
                        HashMap getTransMap = new HashMap();
                        HashMap tempMap = new HashMap();
                        if (obj.containsKey("TRANSFER_TRANS_DETAILS") && CommonUtil.convertObjToDouble(accountClosingTO.getPayableBal()).doubleValue() > 0) {
                            System.out.println("@@@@@@inside TRANSFER_TRANS_DETAILS");
                            getTransMap.put("TRANS_ID", "");
                            getTransMap.put("BATCH_ID", "");
                            getTransMap.put("AMOUNT", remittanceIssueTO.getTotalAmt());
                            tempMap.put("TRANSFER_TRANS_DETAILS", getTransMap);
                            tempReturnMap.put(remittanceIssueTO.getVariableNo(), tempMap);
                            System.out.println("@@@@@@tempReturnMap tempReturnMap" + tempReturnMap);
                        }
                        remittanceMap.put("TransactionDetails", tempReturnMap);
                    }
                    remittanceIssueDAO.execute(remittanceMap);
                } else {
                    System.out.println("Execute here....... nithya");
                    obj.put("generateSingleTransId", generateSingleTransId);
                    transactionDAO.setBreakLoanHierachy("");
                    transactionDAO.execute(obj);
                    transactionDAO.doTransfer();
                    transactionDAO.setLoanAmtMap(new HashMap());
                    getTransDetails(accountClosingTO.getActNum());
                }
                
                
                //Adding master close code changes here
                if (prodType.equals("TermLoan")) {
                    if (obj.containsKey("LTD_CLOSING_STATUS") && CommonUtil.convertObjToStr(obj.get("LTD_CLOSING_STATUS")).equals("DONT_CLOSE")) {
                    } else {
                        if (isLoanCloseThroughGldLnAlwd != null && !isLoanCloseThroughGldLnAlwd.equals("") && isLoanCloseThroughGldLnAlwd.equals("Y")) {
                            if (obj.containsKey("AUCTION_TYPE") && obj.get("AUCTION_TYPE") != null && CommonUtil.convertObjToStr(obj.get("AUCTION_TYPE")).equals("Dr")) {
                                accountTO.setActStatusId("NEW");
                                accountTO.setActStatusDt(null);
                                accountTO.setClosedDt(null);
                            }
                        }
                        sqlMap.executeUpdate("updateLoanActClosingDetail", accountTO);
                    }
                    if (obj.containsKey("SERVICETAXDETAILSTO_DEP")) {
                        ServiceTaxDetailsTO objserviceTaxDetailsTO = (ServiceTaxDetailsTO) obj.get("SERVICETAXDETAILSTO_DEP");
                        objserviceTaxDetailsTO.setAcct_Num(accountClosingTO.getActNum());
                        insertServiceTaxDetails(objserviceTaxDetailsTO);
                    }

                } else {
                    sqlMap.executeUpdate("updateAccountMasterStatusId", accountTO);
                }

                //End        
                
            }
        }
        String authBy2 = CommonUtil.convertObjToStr(obj.get("AUTHORIZE_BY_2"));
        if (prodType != null && !prodType.equals("TermLoan") && authBy2 != null && authBy2.length() > 0) {
            HashMap udtMap = new HashMap();
            udtMap.put("AUTHORIZE_BY_2", authBy2);
            udtMap.put("SINGLE_TRANS_ID", generateSingleTransId);
            udtMap.put("TRANS_DT", (Date) currDt.clone());
            sqlMap.executeUpdate("updateTranssferTransAuthorizedBy2", udtMap);
            sqlMap.executeUpdate("updateCashTransAuthorizedBy2", udtMap);
        }
    }

//    private void addGahanSecurityValue(HashMap acctMap)throws Exception{
//         List lst=sqlMap.executeQueryForList("getSelectTermLoanSecurityLandTO",acctMap.get("ACCOUNTNO"));
//         if(lst !=null && lst.size()>0){
//             int i=0;
//             while(i<lst.size()){
//                 TermLoanSecurityLandTO obj=(TermLoanSecurityLandTO)lst.get(i);
//                 acctMap=new HashMap();
//                 acctMap.put("DOC_NO",obj.getDocumentNo());
//                 acctMap.put("AMOUNT",obj.getPledgeAmount());
//                 sqlMap.executeUpdate("addSecurityDetails",acctMap);
//                 i++;
//             }
//         }
//    }
    private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
            //commented by anjuanand for MantisId: 0010357
//        getTransMap.put("AUTHORIZE_STATUS", "AUTHORIZE_STATUS");
        if (prodType.equals("TermLoan")){
            getTransMap.put("AUTHORIZE_STATUS", "AUTHORIZE_STATUS");
            getTransMap.put("SCREEN_NAME","Loan Closing");
        }
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:" + resultMap);
        List transList = (List) sqlMap.executeQueryForList("getTransferDetails", getTransMap);
        if (transList != null && transList.size() > 0) {
            resultMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetails", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            resultMap.put("CASH_TRANS_LIST", cashList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    private void lienCancel(HashMap lienCancel) throws Exception {
        HashMap hash = new HashMap();
        System.out.println("lienCancelmap#####" + lienCancel);
        if (CommonUtil.convertObjToStr(lienCancel.get("BEHAVE_LIKE")).equals("LOANS_AGAINST_DEPOSITS")
                && (accountClosingTO.getCommand() != null && accountClosingTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT))
                || (accountClosingTO.getCommand() != null
                && accountClosingTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE))) {
            ArrayList lienTOs = new ArrayList();
            HashMap lienCancelMap = new HashMap();
            AccountClosingTO accountClosingTO = (AccountClosingTO) lienCancel.get("accountclosing");
            if (accountClosingTO != null) {
                lienCancelMap.put("LOAN_NO", accountClosingTO.getActNum());
                List lst = sqlMap.executeQueryForList("getDepositLienTO", lienCancelMap);
                System.out.println("getdepositliento#####" + lst);
                if (lst != null && lst.size() > 0) {
                    //                lienCancelMap=(HashMap)lst.get(0);
                    //regarding more than one lien is there in one loan  example additional sanction
                    for (int i = 0; i < lst.size(); i++) {
                        DepositLienTO depLienTO = (DepositLienTO) lst.get(i);
                        depLienTO.setStatus("UNLIENED");
                        depLienTO.setStatusDt(currDt);
                        depLienTO.setRemarks("LOANCLOSING");
                        depLienTO.setUnlienDt(currDt);
                        depLienTO.setAuthorizeBy(null);
                        depLienTO.setAuthorizeDt(null);
                        depLienTO.setAuthorizeStatus(null);
                        lienTOs.add(depLienTO);
                        DepositLienDAO depositLiendao = new DepositLienDAO();
                        lienCancelMap.put(CommonConstants.BRANCH_ID, _branchCode);
                        lienCancelMap.put("COMMAND", CommonConstants.TOSTATUS_UPDATE);
                        lienCancelMap.put("lienTOs", lienTOs);
                        lienCancelMap.put("SHADOWLIEN", depLienTO.getLienAmount());
                        lienCancelMap.put("MULTIPLE_LIEN_CLOSING", "MULTIPLE_LIEN_CLOSING");
                        depositLiendao.setCallFromOtherDAO(true);
                        System.out.println("lienCancelbefore daoinsert ******" + lienCancelMap);
                        depositLiendao.execute(lienCancelMap);
                    }
                }
            }
        }
        if (CommonUtil.convertObjToStr(lienCancel.get("BEHAVE_LIKE")).equals("LOANS_AGAINST_DEPOSITS")
                && (accountClosingTO.getCommand() != null && accountClosingTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE))
                || lienCancel.containsKey("AUTHORIZEMAP") && lienCancel.get("AUTHORIZEMAP") != null) {
            HashMap lienCancelMap = new HashMap();
            AccountClosingTO accountClosingTO = (AccountClosingTO) lienCancel.get("accountclosing");
            String authStatus = "";
            if (lienCancel.containsKey("AUTHORIZEMAP") && lienCancel.get("AUTHORIZEMAP") != null) {
                HashMap authMap = (HashMap) lienCancel.get(CommonConstants.AUTHORIZEMAP);
                authStatus = CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS));
            }
            if (accountClosingTO != null) {
                lienCancelMap.put("LOAN_NO", accountClosingTO.getActNum());
                List lst = sqlMap.executeQueryForList("getDepositLienUnlienTO", lienCancelMap);
                System.out.println("getDepositLienUnlienTO##" + lst);
                if (lst != null && lst.size() > 0) {
                    //                lienCancelMap=(HashMap)lst.get(0);
                    //regarding more than one lien is there in one loan  example additional sanction
                    for (int i = 0; i < lst.size(); i++) {
                        DepositLienTO depLienTO = (DepositLienTO) lst.get(i);
                        lienCancelMap.put("DEPOSIT_ACT_NUM", depLienTO.getDepositNo());
                        lienCancelMap.put("SUBNO", CommonUtil.convertObjToInt(depLienTO.getDepositSubNo()));
                        lienCancelMap.put("SHADOWLIEN", depLienTO.getLienAmount());
                        lienCancelMap.put("BALANCE", depLienTO.getLienAmount());
                        lienCancelMap.put("AMOUNT", depLienTO.getLienAmount());
                        lienCancelMap.put("LIENNO", depLienTO.getLienNo());
                        lienCancelMap.put("AUTHORIZEDT", currDt);
                        lienCancelMap.put("AUTHORIZE_DATE", currDt);
                        lienCancelMap.put("COMMAND_STATUS", "CREATED");
                        lienCancelMap.put("STATUS", "CREATED");
                        DepositLienDAO depositLiendao = new DepositLienDAO();
                        lienCancelMap.put(CommonConstants.BRANCH_ID, _branchCode);
                        if ((accountClosingTO.getCommand() != null && accountClosingTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) || authStatus.equals(CommonConstants.STATUS_REJECTED)) {
                            lienCancelMap.put("ACTION", CommonConstants.STATUS_REJECTED);
                            lienCancelMap.put("AUTHORIZE_STATUS", CommonConstants.STATUS_AUTHORIZED);
                            lienCancelMap.put("LIENAMOUNT", new Double(0));

                        }
                        if (authStatus.equals(CommonConstants.STATUS_AUTHORIZED)) {
                            lienCancelMap.put("ACTION", CommonConstants.STATUS_AUTHORIZED);
                            lienCancelMap.put("AUTHORIZE_STATUS", CommonConstants.STATUS_AUTHORIZED);
                            lienCancelMap.put("LIENAMOUNT", depLienTO.getLienAmount());
                            lienCancelMap.put("STATUS", "UNLIENED");
                            //deposit sub acinfo status
                            //                        sqlMap.executeUpdate("updateDepositSubAccounts",lienCancelMap);
                        }
                        lienCancelMap.put("COMMAND", CommonConstants.AUTHORIZEDATA);
                        depositLiendao.setCallFromOtherDAO(true);
                        lienCancelMap.put("LOAN_CLOSING", "LOAN_CLOSING");
                        System.out.println("lienCancelbefore dao" + lienCancelMap);
                        depositLiendao.execute(lienCancelMap);
                    }
                }
            }
        }
    }

    private void doAuthorize(HashMap map) throws Exception {
        //Test Code....
        /*
         * FileOutputStream out; out = new FileOutputStream("D:\\myfile1.txt");
         * ObjectOutputStream dataOut = new ObjectOutputStream(out);
         * dataOut.writeObject(map); dataOut.flush(); dataOut.close();
         */
        //End of Test Code
        HashMap dataMap = null;
        HashMap singleAuthorizeMap;
        String linkBatchId = null;
        HashMap cashAuthMap;
        String authorizeStatus;
        HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
        HashMap mdsMap = (HashMap)authMap.get("MDS_LOANS");
        System.out.println("MDSLOANSSS"+map);
        List authData = (List) authMap.get(CommonConstants.AUTHORIZEDATA);
        HashMap acHeads = null;
        HashMap getTransMap = null;
        String intAcHd = null;
        double intAmt = 0;
        String behavesLike = null; //KD 889
        if(prodType.equals("TermLoan")){
            if(mdsMap != null && mdsMap.containsKey("MDS_PROD_ID") && mdsMap.containsKey("MDS_BEHAVES")){
                String mdsbehaves = CommonUtil.convertObjToStr(mdsMap.get("MDS_BEHAVES"));
                String mdsProdId = CommonUtil.convertObjToStr(mdsMap.get("MDS_PROD_ID"));
                String loanNo = CommonUtil.convertObjToStr(mdsMap.get("ACCOUNTNO"));
                if(mdsbehaves.equals("LOANS_AGAINST_DEPOSITS") && mdsProdId.equals("MDS_LOAN")){
                    HashMap mdsUpdateMap = new HashMap();
                    HashMap mdsChittalMap = new HashMap();
                    mdsChittalMap.put("LOAN_ACT_NUM", loanNo);
                        mdsUpdateMap.put("ACT_NUM", "");
                        mdsUpdateMap.put("LOAN_STATUS", "");
                        mdsUpdateMap.put("LOAN_GIVEN", "N");
                        String chittalNo = "";
                        String isLoanGiven = "";
                        String subNo = "1";
                        List lst = sqlMap.executeQueryForList("getchittalNo", mdsChittalMap);
                        if (lst != null && lst.size() > 0) {
                    HashMap updateMdsMap = (HashMap) lst.get(0);
                    if (updateMdsMap != null && updateMdsMap.containsKey("CHITTAL_NO") && updateMdsMap.containsKey("LOAN_GIVEN")) {
                        chittalNo = CommonUtil.convertObjToStr(updateMdsMap.get("CHITTAL_NO"));
                        isLoanGiven = CommonUtil.convertObjToStr(updateMdsMap.get("LOAN_GIVEN"));
                        mdsUpdateMap.put("CHITTAL_NO", chittalNo);
                        mdsUpdateMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                        if(isLoanGiven != null && isLoanGiven.equals("Y")){
                        int i = sqlMap.executeUpdate("updateMDSLoanStatus", mdsUpdateMap);
                            System.out.println("iiiiiiiiii"+i);
                        }
                        }
                        }
                }
            }
            
        }
        if (authMap.containsKey("SER_TAX_AUTH")) {
            HashMap serMapAuth = (HashMap) authMap.get("SER_TAX_AUTH");
            sqlMap.executeUpdate("authorizeServiceTaxDetails", serMapAuth);
        }
        //        if (prodType.equals("TermLoan")) {
        //            HashMap tempMap = (HashMap)map.get("TOTAL_AMOUNT");
        //            if (tempMap.containsKey("CALCULATE_INT")) {
        //                acHeads = (HashMap)sqlMap.executeQueryForObject("getLoanAccountClosingHeads", accountClosingTO.getActNum());
        //                if (acHeads!=null && acHeads.size()>0) {
        //                    getTransMap = new HashMap();
        //                                getTransMap.put("LINK_BATCH_ID",accountClosingTO.getActNum());
        //                                getTransMap.put("TODAY_DT",ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID))));
        //                    intAcHd = CommonUtil.convertObjToStr(tempMap.get("INT_PAYABLE_ACHD"));
        //                    intAmt = CommonUtil.convertObjToDouble(tempMap.get("CALCULATE_INT")).doubleValue();
        //                }
        //            }
        //        }
        for (int i = 0, j = authData.size(); i < j; i++) {
            linkBatchId = CommonUtil.convertObjToStr(((HashMap) authData.get(i)).get("ACCOUNTNO"));//Transaction Batch Id
            singleAuthorizeMap = new HashMap();
            dataMap = new HashMap();
            authorizeStatus = CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS));
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("ACCOUNTNO", linkBatchId);
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
            singleAuthorizeMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            singleAuthorizeMap.put("AUTHORIZEDT", currDt);
            if (prodType.equals("TermLoan")) {
                //                authorizeLoanTrans(singleAuthorizeMap); directly insert interest to loan or advances transaction
                if (authorizeStatus.equals("AUTHORIZED")) {
                    List intList = null;
                    HashMap prodMap = new HashMap();
                    prodMap.put("PROD_ID", CommonUtil.convertObjToStr(map.get("PROD_ID")));
                    List behavesLikeList = sqlMap.executeQueryForList("getLoanBehaves", prodMap);
                    HashMap behavesLikeMap = null;
                    //String behavesLike = null;
                    if(behavesLikeList != null && behavesLikeList.size()>0){
                        behavesLikeMap = (HashMap) behavesLikeList.get(0);
                        if (behavesLikeMap != null && behavesLikeMap.size() > 0 && behavesLikeMap.containsKey("BEHAVES_LIKE")) {
                            behavesLike = CommonUtil.convertObjToStr(behavesLikeMap.get("BEHAVES_LIKE"));
                            HashMap interestMap = new HashMap();
                            interestMap.put(CommonConstants.ACT_NUM, linkBatchId);
                            if (behavesLike.equals("OD")) {
                                intList = sqlMap.executeQueryForList("IntCalculationDetailAD", interestMap);
                            } else {
                                intList = sqlMap.executeQueryForList("IntCalculationDetail", interestMap);
                            }
                        }
                    }

                    HashMap intMap = null;
                    if(intList != null && intList.size()>0){
                        intMap = (HashMap) intList.get(0);
                    }
                    Date lstIntCalcDt = null;
                    if(intMap != null && intMap.size()>0 && intMap.containsKey("LAST_INT_CALC_DT")){
                        lstIntCalcDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(intMap.get("LAST_INT_CALC_DT")));
                    }
                    HashMap lastIntCalcDt = new HashMap();
                    lastIntCalcDt.put("LASTINTCALCDT", CommonUtil.getProperDate(currDt, lstIntCalcDt));
                    lastIntCalcDt.put("ACCOUNTNO", linkBatchId);System.out.println("value of lastIntCalcDt in Loan Closing: " + lastIntCalcDt);
                    sqlMap.executeUpdate("updateLoansFacilityDetailsTemp", lastIntCalcDt);
//                    singleAuthorizeMap.put("LAST_CALC_DT", currDt);
                    singleAuthorizeMap.put("LAST_CALC_DT", DateUtil.addDaysProperFormat(currDt, -1));
                    singleAuthorizeMap.put("END", currDt);
                    singleAuthorizeMap.put("CR_ACT_NUM", singleAuthorizeMap.get("ACCOUNTNO"));
                    sqlMap.executeUpdate("updateclearBal", singleAuthorizeMap);
                    //last int credit date updated for AD
                    sqlMap.executeUpdate("updateAdvancesAppDate", singleAuthorizeMap);
                    insertInterestDetails(singleAuthorizeMap);
                    dataMap.put("ACCT_NUM", singleAuthorizeMap.get("ACCOUNTNO"));
                    dataMap.put("CURR_DT", currDt);
                    dataMap.put("TRANS_AMT", CommonUtil.convertObjToDouble(((HashMap) authData.get(i)).get("SUBSIDY_AMT")));
                    sqlMap.executeUpdate("updateTermLoanFacilityDetailsTO", dataMap);
//                    addGahanSecurityValue(singleAuthorizeMap);
                    List lst = sqlMap.executeQueryForList("getSelectSecurityAmtClosingTL", singleAuthorizeMap);
                    if (lst != null && lst.size() > 0) {
                        for (int a = 0; a < lst.size(); a++) {
                            HashMap updateAvilSecurityMap = (HashMap) lst.get(a);
                            double security_no = CommonUtil.convertObjToDouble(updateAvilSecurityMap.get("SECURITY_NO")).doubleValue();
                            updateAvilSecurityMap.remove("SECURITY_NO");
                            updateAvilSecurityMap.put("SECURITY_NO", new Double(security_no));
                            sqlMap.executeUpdate("updateCustSecurityAvailableAmt", updateAvilSecurityMap);
                        }
                    }
                }
            }
            sqlMap.executeUpdate("authorizeUpdateAccountCloseTO", singleAuthorizeMap);
            System.out.println("@@@authorizestatus" + authorizeStatus);
            if (prodType.equals("TermLoan")) {
                if (authorizeStatus.equals("REJECTED")) {
                    HashMap where = new HashMap();
                    where.put("ACCT_NUM", linkBatchId);
                    where.put("ACCT_STATUS", "NEW");

                    sqlMap.executeUpdate("updateStatusForAccountTL", where);
                    where = (HashMap) sqlMap.executeQueryForObject("getStatusForAccountTL", where);
//                    if(where!=null && where.size()>0 && where.get("ACCT_CLOSE_DT") !=null) {
//                        System.out.println("@@@Now Status "+where);
//                        java.sql.Timestamp timeStamp = (java.sql.Timestamp)where.get("ACCT_CLOSE_DT");
//                        timeStamp.setTime(timeStamp.getTime()-1000);
//                        System.out.println("@@@Status "+where);
//                        where =(HashMap) sqlMap.executeQueryForObject("getStatusForAccountTL",where);
//                        if(where!=null && where.size()>0) {
//                            System.out.println("@@@Previous Status "+where);
//                            sqlMap.executeUpdate("updateStatusForAccountTL", where);
//                        }
//                    }
                    //                    TransactionTO tto=null;
                    //                    termFacilityTo =(TermLoanFacilityTO) sqlMap.executeQueryForObject("getSelectTermLoanFacilityTOMaterializedView",linkBatchId);
                    //                    Date curr_dt=ServerUtil.getCurrentDateProperFormat(_branchCode);
                    //                    curr_dt.setDate(termFacilityTo.getStatusDt().getDate());
                    //                    curr_dt.setMonth(termFacilityTo.getStatusDt().getMonth());
                    //                    curr_dt.setYear(termFacilityTo.getStatusDt().getYear());
                    //                    termFacilityTo.setStatusDt(curr_dt);
                    //                    System.out.println("@@@accountTO"+termFacilityTo);
                    //                    if(termFacilityTo !=null)
                    //                        sqlMap.executeUpdate("updateTermLoanFacilityTOMaterializedView", termFacilityTo);
                }
            }
            if (!prodType.equals("TermLoan")) {
                if (authorizeStatus.equals("REJECTED")) {
                    
                    HashMap where = new HashMap();
                    where.put(CommonConstants.ACT_NUM, linkBatchId);
                    where.put("ACCOUNTNO",linkBatchId);
                    where.put("LINK_BATCH","LINK_BATCH");
                    where.put("LINK_BATCH_ID",linkBatchId);
                    where.put("TRANS_DT",currDt);
                    where.put("INITIATED_BRANCH", map.get(CommonConstants.BRANCH_ID));
                    where.put("REVERSE_CLOSING_INT","REVERSE_CLOSING_INT");
                    List transList = sqlMap.executeQueryForList("getBatchTxTransferTOsForRollBack", where);
                    if (transList != null && transList.size() > 0) {
                        HashMap rollBackMap = new HashMap();
                        rollBackMap.put("ACT_NUM", linkBatchId);
                        rollBackMap.put("LINK_BATCH", "LINK_BATCH");
                        rollBackMap.put("LINK_BATCH_ID", linkBatchId);
                        rollBackMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                        rollBackMap.put("TANS_LIST_DATA",transList);
                        rollBackMap.put("BRANCH_ID",map.get(CommonConstants.BRANCH_ID));
                        rollBackMap.put("TRANS_DT",currDt);
                        RollBackDAO rollBackObj = new RollBackDAO();
                        rollBackObj.doTransferRollBack(rollBackMap);
                    }
                    sqlMap.executeUpdate("updateAcctPaymentLASTCRINTApplDt", where);
                    where.put("ACT_STATUS_ID", "NEW");
                    sqlMap.executeUpdate("updateStatusForAccount", where);
//                    where = (HashMap) sqlMap.executeQueryForObject("getStatusForAccount", where);
//                    if (where != null && where.size() > 0) {
//                        System.out.println("@@@Now Status " + where);
//                        java.sql.Timestamp timeStamp = (java.sql.Timestamp) where.get("CLOSED_DT");
//                        timeStamp.setTime(timeStamp.getTime() - 1000);
//                        System.out.println("@@@Status " + where);
//                        where = (HashMap) sqlMap.executeQueryForObject("getStatusForAccount", where);
//                        if (where != null && where.size() > 0) {
//                            System.out.println("@@@Previous Status " + where);
//                            sqlMap.executeUpdate("updateStatusForAccount", where);
//                        }
//                    }
                    where = null;
                    //                    accountTO =(AccountTO) sqlMap.executeQueryForObject("getSelectAccountTOForTempView",linkBatchId);
                    //                    System.out.println("@@@accountTO"+accountTO);
                    //                    if(accountTO !=null)
                    //                        sqlMap.executeUpdate("updateAccountTOMaterializedView", accountTO);
                }
            }

            if (!prodType.equals("TermLoan") && !authorizeStatus.equals("REJECTED")) {
                HashMap remitMap = new HashMap();
                remitMap.put("BATCH_ID", linkBatchId);
                List remitList = sqlMap.executeQueryForList("getSelectDepositTransMode", remitMap);
                if (remitList != null && remitList.size() > 0) {
                    remitMap = (HashMap) remitList.get(0);
                    sqlMap.executeUpdate("updateTokenNOFromOtherModule", remitMap);
                }
                //following code added by swaroop for destroying cheques
                HashMap chqDetMap = new HashMap();
                chqDetMap.put("ACCT_NO", linkBatchId);
                List chqDetList = sqlMap.executeQueryForList("getTableValues", chqDetMap);
                chqDetMap = null;
                if (chqDetList != null && chqDetList.size() > 0) {
                    for (int k = 0; k < chqDetList.size(); k++) {
                        chqDetMap = (HashMap) chqDetList.get(k);
                        System.out.println("@@@chqDetMap" + chqDetMap);
                        HashMap detailsMap = new HashMap();
                        detailsMap.put("FROM_CHQ_NO1", chqDetMap.get("START_CHQ_NO1"));
                        detailsMap.put("FROM_CHQ_NO2", chqDetMap.get("START_CHQ_NO2"));
                        detailsMap.put("TO_CHQ_NO1", chqDetMap.get("END_CHQ_NO1"));
                        detailsMap.put("TO_CHQ_NO2", chqDetMap.get("END_CHQ_NO2"));
                        detailsMap.put("ACCT_NO", CommonUtil.convertObjToStr(linkBatchId));
                        System.out.println("@@@detailsMap" + detailsMap);
                        List usedList = (ArrayList) sqlMap.executeQueryForList("getIssuedChequeDetails", detailsMap);
                        List stopPayList = sqlMap.executeQueryForList("getStopPaymentList", detailsMap);
                        long fromNo = CommonUtil.convertObjToLong(detailsMap.get("FROM_CHQ_NO2"));
                        long toNo = CommonUtil.convertObjToLong(detailsMap.get("TO_CHQ_NO2"));
                        int m = 0;
                        for (; fromNo <= toNo; fromNo++, m++) {
                            String chequeNo = CommonUtil.convertObjToStr(detailsMap.get("FROM_CHQ_NO1")) + " " + fromNo;
                            String status = chqNoExistsOrNot(chequeNo, usedList, fromNo, stopPayList);
                            detailsMap.put("STATUS", CommonUtil.convertObjToStr(status));
                            detailsMap.put("CHQ_NO", CommonUtil.convertObjToStr(chequeNo));
                            System.out.println("@@@status" + status);
                            if (status.equals("DESTROYED")) {
                                sqlMap.executeUpdate("insertChqDestroDetails", detailsMap);
                            }
                        }
                        chqDetMap = null;
                        detailsMap = null;
                    }
                }

            }
            if (map.containsKey("TransactionTO") && map.get("TransactionTO") != null) {
                TransactionTO transactionTODebit = new TransactionTO();
                LinkedHashMap transactionDetailsMap = new LinkedHashMap();
                TransactionTO transactionTO;
                transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                LinkedHashMap allowedTransDetailsTO;
                if (transactionDetailsMap != null && transactionDetailsMap.size() > 0) {
                    if (transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs") && transactionDetailsMap.get("NOT_DELETED_TRANS_TOs") != null) {
                        allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                        if (allowedTransDetailsTO != null && (!allowedTransDetailsTO.isEmpty())) {
                            transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                            transactionTO.setStatus(CommonConstants.STATUS_DELETED);
                            transactionTO.setBranchId(_branchCode);
                            sqlMap.executeUpdate("deleteRemittanceIssueTransactionTO", transactionTO);
                        }
                    }
                }
            }
            singleAuthorizeMap = null;
            //payorder coding...
            HashMap payOrderMap = new HashMap();
            payOrderMap.put("REMARKS", linkBatchId);
            remittanceIssueTO = new RemittanceIssueTO();
            List lstPay = sqlMap.executeQueryForList("getSelectDepositPayOrder", payOrderMap);
            if (lstPay != null && lstPay.size() > 0) {
                HashMap selectMap = (HashMap) lstPay.get(0);
                String variableNo = CommonUtil.convertObjToStr(selectMap.get("VARIABLE_NO"));
                LinkedHashMap notDeleteMap = new LinkedHashMap();
                LinkedHashMap transferMap = new LinkedHashMap();
                HashMap remittanceMap = new HashMap();
                remittanceIssueDAO = new RemittanceIssueDAO();
                remittanceIssueTO = new RemittanceIssueTO();
                LinkedHashMap remitMap = new LinkedHashMap();
                LinkedHashMap remMap = new LinkedHashMap();

                String favouringName = CommonUtil.convertObjToStr(selectMap.get("FAVOURING"));
                double amount = CommonUtil.convertObjToDouble(selectMap.get("AMOUNT")).doubleValue();
                transactionTODebit.setApplName(favouringName);
                transactionTODebit.setTransAmt(new Double(amount));
                transactionTODebit.setTransType("TRANSFER");
                transactionTODebit.setProductId(CommonUtil.convertObjToStr(selectMap.get("PROD_ID")));
                transactionTODebit.setProductType("RM");
                transactionTODebit.setDebitAcctNo(favouringName);
                remittanceIssueDAO.setFromotherDAo(false);
                notDeleteMap.put(String.valueOf(1), null);
                transferMap.put("NOT_DELETED_TRANS_TOs", notDeleteMap);
                remittanceMap.put("TransactionTO", transferMap);
                remittanceMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
                remittanceMap.put("OPERATION_MODE", "ISSUE");
                remittanceMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                remittanceMap.put("MODULE", null);
                remittanceMap.put("MODE", null);
                remittanceMap.put("SCREEN", null);
                remittanceIssueTO.setDraweeBranchCode(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
                remittanceIssueTO.setAmount(new Double(amount));
                remittanceIssueTO.setCategory("GENERAL_CATEGORY");
                remittanceIssueTO.setProdId(CommonUtil.convertObjToStr(selectMap.get("PROD_ID")));
                remittanceIssueTO.setFavouring(favouringName);
//                remittanceIssueTO.setDraweeBank(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
                remittanceIssueTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
                remittanceIssueTO.setRemarks(linkBatchId);
                remittanceIssueTO.setCommand(null);
                remittanceIssueTO.setTotalAmt(new Double(amount));
                remittanceIssueTO.setExchange(new Double(0.0));
                remittanceIssueTO.setPostage(new Double(0.0));
                remittanceIssueTO.setOtherCharges(new Double(0.0));
                remittanceIssueTO.setStatusDt(currDt);
                remittanceIssueTO.setStatusBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                remittanceIssueTO.setAuthorizeDt(currDt);
                remittanceIssueTO.setAuthorizeStatus(authorizeStatus);
                remittanceIssueTO.setAuthorizeUser(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                remMap.put(String.valueOf(1), remittanceIssueTO);
                remitMap.put("NOT_DELETED_ISSUE_TOs", remMap);
                remittanceMap.put("RemittanceIssueTO", remitMap);
                HashMap transferTransMap = new HashMap();
                transferTransMap.put("LINK_BATCH_ID", variableNo);
                transferTransMap.put("TODAY_DT", currDt);
                transferTransMap.put("AMOUNT", new Double(amount));
                transferTransMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(_branchCode));
                lstPay = sqlMap.executeQueryForList("getTransferTransBatchID", transferTransMap);
                if (lstPay != null && lstPay.size() > 0) {
                    transferTransMap = (HashMap) lstPay.get(0);
                }
                HashMap authorizeMap = new HashMap();
                HashMap authorizeData = new HashMap();
                ArrayList data = new ArrayList();
                String transBatchId = CommonUtil.convertObjToStr(selectMap.get("BATCH_ID"));
                authorizeData.put("STATUS", authorizeStatus);
                authorizeData.put("BATCH_ID", transBatchId);
                data.add(authorizeData);
                authorizeMap.put("AUTHORIZESTATUS", authorizeStatus);
                authorizeMap.put("AUTHORIZEDATA", data);
                remittanceMap.put("AUTHORIZEMAP", authorizeMap);
                System.out.println(" remittanceMap :" + remittanceMap);
                remittanceIssueDAO.execute(remittanceMap);
            }
            //Separation of Authorization for Cash and Transfer
            //Call this in all places that need Authorization for Transaction
            cashAuthMap = new HashMap();
            cashAuthMap.put("LOAN_CLOSING_SCREEN", "LOAN_CLOSING_SCREEN");
            cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
            cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            System.out.println("asAnwhen####" + asAnWhen);
            if (asAnWhen != null && asAnWhen.equals("Y")) {
                cashAuthMap.put("DAILY", "DAILY");
            }
            //Added by chithra 10077: Gold Loan Closing - Auctioned Account Closing Transaction Is Not Correct For Printing Purpose.
            boolean authFlag = true;
            HashMap WhereMap = new HashMap();
            WhereMap.put("ACCT_NUM", linkBatchId);
            List releaseList = ServerUtil.executeQuery("getSecurityReleaseDetails", WhereMap);

            if (releaseList != null && releaseList.size() > 0) {
                HashMap hmap = (HashMap) releaseList.get(0);
                if (hmap != null && hmap.containsKey("IS_RELEASE")) {
                    String val = CommonUtil.convertObjToStr(hmap.get("IS_RELEASE"));
                    if (val != null && val.equals("Y")) {
                        authFlag = false;
                    }
                }
            }
            if (authFlag) {
                if (authMap.containsKey("AUCTION_BAL_AMOUNT")) {
                    HashMap valMap = new HashMap();
                    valMap = (HashMap) authMap.get("AUCTION_BAL_AMOUNT");
                    WhereMap = new HashMap();
                    WhereMap.put("ACCT_NUM", linkBatchId);
                    releaseList = ServerUtil.executeQuery("getSuspeceIsAuction", WhereMap);
                    if (releaseList != null && releaseList.size() > 0) {
                        HashMap hmap = (HashMap) releaseList.get(0);
                        if (hmap != null && hmap.containsKey("ISAUCTION")) {
                            String val = CommonUtil.convertObjToStr(hmap.get("ISAUCTION"));
                            if (val != null && val.equals("Y")) {
                                authFlag = false;
                            }
                        }
                    }
                }
            }
            if (!authFlag) {
                HashMap authChargeMap = new HashMap();
                authChargeMap.put("AUTHORIZE_BY", map.get(CommonConstants.USER_ID));
                authChargeMap.put("AUTHORIZE_DATE", currDt);
                authChargeMap.put("AUTHORIZE_STATUS", authorizeStatus);
                authChargeMap.put("ACT_NUM", linkBatchId);
                sqlMap.executeUpdate("updateAuthorizeDetail", authChargeMap);
            }
            //End---------------
            //aaded by sreekrishnan for cashier auhtorization to avoid debit transaction from authorization.
            if (asAnWhen != null && asAnWhen.equals("Y") && !authorizeStatus.equals("REJECTED")) {
                List listData = ServerUtil.executeQuery("getCashierAuth", new HashMap());
                if (listData != null && listData.size() > 0) {
                    HashMap map1 = (HashMap) listData.get(0);
                    if (map1.get("CASHIER_AUTH_ALLOWED") != null && map1.get("CASHIER_AUTH_ALLOWED").toString().equals("Y")) {
                        if (authFlag) {
                            cashAuthMap.put("GOLD_LOAN_CLOSING_TRANSACTION", "GOLD_LOAN_CLOSING_TRANSACTION");
                            accountTO = new AccountTO();
                            accountTO.setActNum(linkBatchId);
                            accountTO.setActStatusId(CommonConstants.CLOSED);
                            accountTO.setClosedDt(currDt);
                            accountTO.setActStatusDt(currDt);
                            System.out.println("accountTO : " + accountTO);
                            sqlMap.executeUpdate("updateLoanActClosingDetail", accountTO);//for status updation
                        }
                        if(behavesLike.equals("OD") && cashAuthMap.containsKey("GOLD_LOAN_CLOSING_TRANSACTION")){ // KD 889
                            cashAuthMap.remove("GOLD_LOAN_CLOSING_TRANSACTION");
                        }
                    }
                }
            }
            if (!prodType.equals("TermLoan")) {
                cashAuthMap.put("ACCOUNT_CLOSING", "ACCOUNT_CLOSING");
            }
            TransactionDAO.authorizeCashAndTransfer(linkBatchId, authorizeStatus, cashAuthMap);
            
            //Added  by nithya on 07-06-2024 for Gold loan release - KD-3783
            if(prodType.equals("TermLoan") && authorizeStatus.equals(CommonConstants.STATUS_AUTHORIZED)){
                HashMap updatemap = new HashMap();                  
                updatemap.put("IS_RELEASE","Y");
                updatemap.put("RELEASE_DT",currDt.clone());
                updatemap.put("ACCT_NUM",linkBatchId);
                sqlMap.executeUpdate("updateGoldReleaseStatusForKCC", updatemap);
            }
            
            if (authorizeStatus.equals("REJECTED") && authMap.containsKey("AUCTION_BAL_AMOUNT")) {
                HashMap rejection = new HashMap();
                rejection = (HashMap) authMap.get("AUCTION_BAL_AMOUNT");
                if(map.containsKey(CommonConstants.USER_ID) && map.get(CommonConstants.USER_ID) != null){
                	rejection.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                }
                rejectSuspenseAccmaster(rejection);
            }else{
                // Add code for closing standing instruction by nithya
                HashMap termLoanSiMap = new HashMap();
                termLoanSiMap.put("ACT_NUM",linkBatchId);
                List termLoanSiLst = ServerUtil.executeQuery("getselectSIDetailsForTermLoan", termLoanSiMap);
                if (termLoanSiLst != null && termLoanSiLst.size() > 0) {
                    HashMap siMap = new HashMap();
                    siMap.put("ACT_NUM", linkBatchId);
                    HashMap siUpdateMap = new HashMap();
                    siMap = (HashMap) termLoanSiLst.get(0);
                    siUpdateMap.put("STATUS", "CLOSED");
                    siUpdateMap.put("SI_ID", siMap.get("SI_ID"));
                    sqlMap.executeUpdate("updateSIinTermLoanCredit", siUpdateMap);
                    sqlMap.executeUpdate("updateSIinTermLoanDebit", siUpdateMap);
                    sqlMap.executeUpdate("updateSIinTermLoan", siUpdateMap);
                    siUpdateMap = null;
                    siMap = null;
                    termLoanSiMap = null;                 
                }
            }
            cashAuthMap = null;
        }
    }
    protected void rejectSuspenseAccmaster(HashMap susTypeMap) throws Exception{
            susTypeMap.put("STATUS", "REJECTED");
            susTypeMap.put("AUTHORIZEDT", currDt);
            susTypeMap.put("USER_ID", CommonUtil.convertObjToStr(susTypeMap.get(CommonConstants.USER_ID)));
            if (susTypeMap.containsKey("ACT_NUM") && susTypeMap.get("ACT_NUM") != null) {
                susTypeMap.put("SUSPENSE_ACCT_NUM", CommonUtil.convertObjToStr(susTypeMap.get("ACT_NUM")));
                sqlMap.executeUpdate("authorizeSuspenseAccountMaster", susTypeMap);
            }

    }
    private void authorizeLoanTrans(HashMap authMap) throws Exception {
        authMap.put("ACT_NUM", authMap.get("ACCOUNTNO"));
        authMap.put("AUTHORIZE_STATUS", authMap.get("STATUS"));
        List lst = sqlMap.executeQueryForList("getIntDetails", authMap);
        if (lst != null && lst.size() > 0) {
            HashMap hash = (HashMap) lst.get(0);
            authMap.put("TODAY_DT", hash.get("TRANS_DT"));
            authMap.put("TRANS_DT", hash.get("TRANS_DT"));
            authMap.put("AUTHORIZE_BY", authMap.get(CommonConstants.USER_ID));
            System.out.println("authMap#####" + authMap);
            sqlMap.executeUpdate("authorizeLoansActClosingCumLoanDetails", authMap);
        }

    }

    /**
     * Standard method to retrieve data for a particular account
     */
    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        prodType = "";
        if (obj.containsKey("PROD_TYPE") && obj.get("PROD_TYPE") != null) {
            prodType = CommonUtil.convertObjToStr(obj.get("PROD_TYPE"));
        }
        System.out.println("obj : " + obj);
        if (prodType.equals("TermLoan")) {
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        } else {
            transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
        }
        if (obj.containsKey("MODE")) {
            this.commandMode = CommonUtil.convertObjToStr(obj.get("MODE"));
        }
        return getData(obj);
    }
    //for interest details transfer from tmp to loans_interest

    void insertInterestDetails(HashMap Interest) throws Exception {
        HashMap singleRecord = new HashMap();
        Interest.put("ACT_NUM", Interest.get("ACCOUNTNO"));
        List lst = sqlMap.executeQueryForList("selectLoanInterestTMP", Interest);
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                HashMap insertRecord = new HashMap();
                singleRecord = (HashMap) lst.get(i);
                insertRecord.put("ACT_NUM", singleRecord.get("ACT_NUM"));
                String from_dt = CommonUtil.convertObjToStr(singleRecord.get("FROM_DT"));
                Date fromDate = DateUtil.getDateMMDDYYYY(from_dt);
                insertRecord.put("FROM_DT",CommonUtil.getProperDate(currDt, fromDate));
                String to_dt = CommonUtil.convertObjToStr(singleRecord.get("TO_DATE"));
                Date toDate = DateUtil.getDateMMDDYYYY(to_dt);
                insertRecord.put("TO_DATE", CommonUtil.getProperDate(currDt,toDate));
                Double amt = CommonUtil.convertObjToDouble(singleRecord.get("AMT"));
                insertRecord.put("AMT", amt);
                int noOfdays = CommonUtil.convertObjToInt(singleRecord.get("NO_OF_DAYS"));
                insertRecord.put("NO_OF_DAYS", new Long(noOfdays));
                double totProduct = CommonUtil.convertObjToDouble(singleRecord.get("TOT_PRODUCT")).doubleValue();
                insertRecord.put("TOT_PRODUCT", new Double(totProduct));
                double intamt = CommonUtil.convertObjToDouble(singleRecord.get("INT_AMT")).doubleValue();
                insertRecord.put("INT_AMT", new Double(intamt));
                intamt = CommonUtil.convertObjToDouble(singleRecord.get("INT_RATE")).doubleValue();
                insertRecord.put("INT_RATE", new Double(intamt));
                insertRecord.put("ACT_NUM", singleRecord.get("PROD_ID"));
                String intcalcdt = CommonUtil.convertObjToStr(singleRecord.get("INT_CALC_DT"));
                Date intcalcDt = DateUtil.getDateMMDDYYYY(intcalcdt);
                insertRecord.put("INT_CALC_DT", CommonUtil.getProperDate(currDt,intcalcDt));
                insertRecord.put("CUST_ID", singleRecord.get("CUST_ID"));
                System.out.println("insertrecord####" + insertRecord);
                sqlMap.executeUpdate("insertLoanInterest", insertRecord);//singleRecord
            }
        }
    }

    private InterestTask getIntPayable(HashMap map, HashMap paramMap) throws Exception {
        TaskHeader header = new TaskHeader();
        header.setTaskClass("InterestTask");
        if (map.containsKey("PROD_TYPE")) {
            header.setProductType(TransactionFactory.LOANS);
            header.setTransactionType(CommonConstants.RECEIVABLE);
        } else {
            header.setTransactionType(CommonConstants.PAYABLE);
            header.setProductType(TransactionFactory.OPERATIVE);
            if (paramMap.containsKey("DB_DRIVER_NAME") && paramMap.get("DB_DRIVER_NAME") != null) {// Added by nithya on 20-02-2019 for KD 393 - Sb closing issue (interest pay at daily product)
                header.setDB_DRIVER_NAME(CommonUtil.convertObjToStr(paramMap.get("DB_DRIVER_NAME")));
            }
        }
        if (productBehaveLike.equals("OD")) {
            header.setTransactionType(CommonConstants.PAYABLE);
            header.setProductType(TransactionFactory.ADVANCES);
        }



        //        private InterestTask getIntPayable(HashMap map, HashMap paramMap) throws Exception {
        //            TaskHeader header = new TaskHeader();
        //            header.setTaskClass("InterestTask");
        //            header.setTransactionType(CommonConstants.PAYABLE);
        //            header.setProductType(TransactionFactory.OPERATIVE);
        //            if(map.containsKey("PROD_TYPE")){
        //                header.setProductType(TransactionFactory.LOANS);
        //                header.setTransactionType(CommonConstants.RECEIVABLE);
        //            }


        header.setBranchID((String) paramMap.get(CommonConstants.BRANCH_ID));
        header.setIpAddr((String) paramMap.get(CommonConstants.IP_ADDR));
        header.setUserID((String) paramMap.get(CommonConstants.USER_ID));
        

        ArrayList acctList = new ArrayList();
        if (map.containsKey("PROD_TYPE") || productBehaveLike.equals("OD")) {
            acctList.add(map.get("ACCT_NUM"));
        }
        acctList.add(map.get("ACT_NUM"));
        HashMap taskParam = new HashMap();
        //Added BY Suresh
        if (map.containsKey("INT_PAYABLE_AMOUNT")) {
            taskParam.put("INT_PAYABLE_AMOUNT", map.get("INT_PAYABLE_AMOUNT"));
        }
        taskParam.put("generateSingleTransId", generateSingleTransId);
        taskParam.put(CommonConstants.PRODUCT_ID, map.get("PROD_ID"));
        taskParam.put(CommonConstants.ACT_NUM, acctList);
        taskParam.put(CommonConstants.BRANCH, (String) paramMap.get(CommonConstants.BRANCH_ID));
        taskParam.put("ADD_MONTHS", new Integer(-1));
        if(map.containsKey("SB_OD")){
            String prodType = "OA";
            String prodId = CommonUtil.convertObjToStr(map.get("PROD_ID"));
            String acctNum = CommonUtil.convertObjToStr(map.get("ACT_NUM"));
            Date dt = setFromDate(prodType, prodId,acctNum);
            taskParam.put("DATE_FROM", dt);
        } 
        if(map.containsKey("SCREEN_NAME") && map.get("SCREEN_NAME") != null && CommonUtil.convertObjToStr(map.get("SCREEN_NAME")).equalsIgnoreCase("Account Closing")){
            taskParam.put("SCREEN_NAME",map.get("SCREEN_NAME"));
        }        
        System.out.println("taskParam: " + taskParam);
        header.setTaskParam(taskParam);
        // //System.out.println("header : " + header);

        InterestTask tsk = new InterestTask(header);
        System.out.println("Inttask: " + tsk);
        return tsk;
    }

     private Date setFromDate(String Prod_Type, String prod_ID, String acctNum) {
         System.out.println("inside setFromDate :: " + Prod_Type + "-" + prod_ID);
         Date fromDt = null;
         try {
             if (Prod_Type.length() > 0 && prod_ID.length() > 0) {
                 HashMap provMap = new HashMap();
                 provMap.put("PROD_ID", prod_ID);
                 provMap.put("PROD_TYPE", Prod_Type);
                 provMap.put("BRANCH_CODE", _branchCode);
                 provMap.put("ACCT_NUM", acctNum);
                 List provList = sqlMap.executeQueryForList("getSBODlastIntCalcDt", provMap);
                 if (provList != null && provList.size() > 0) {
                     String from_date = "";
                     provMap = (HashMap) provList.get(0);
                     if(provMap.get("LAST_CR_INT_APPLDT") != null){
                       from_date = CommonUtil.convertObjToStr(provMap.get("LAST_CR_INT_APPLDT"));   
                     }else{
                       from_date = CommonUtil.convertObjToStr(provMap.get("CREATE_DT"));  
                     }                     
                     fromDt = setProperDtFormat(DateUtil.addDays(DateUtil.getDateMMDDYYYY(from_date), 1));
                 }
             }
         } catch (Exception e) {
         }

         return fromDt;
    }
    
    private HashMap getData(HashMap map) throws Exception {
        System.out.println("Map in getData : " + map);
        HashMap returnMap = new HashMap();
        List list = null;
        HashMap where = new HashMap();
        HashMap clearBalMap = new HashMap();
        long noOfDays = 0;
        where.put("ACT_NUM", map.get(CommonConstants.MAP_WHERE));
        System.out.println("!!!!!!! where : " + where);
        String branchcode="";
        if (map.containsKey("PROD_TYPE") && prodType.equals("TermLoan")) {
            List branchList = sqlMap.executeQueryForList("getBranchTL", CommonUtil.convertObjToStr(map.get(CommonConstants.MAP_WHERE)));
            if (!branchList.isEmpty() && branchList != null) {
                branchcode = CommonUtil.convertObjToStr(branchList.get(0));
            } else {
                branchcode = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
            }
        } else {
            branchcode = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        }
        if (this.commandMode == null || this.commandMode.equals("") || this.commandMode.equals(CommonConstants.TOSTATUS_UPDATE) || this.commandMode.equals("AUTHORIZE") || this.commandMode.equals(CommonConstants.TOSTATUS_DELETE)) {
            //            if(map.containsKey("PROD_TYPE")){       
            if (prodType.equals("TermLoan") && map.containsKey("PROD_TYPE")) {
                where.put("BRANCH_CODE", branchcode);
                list = (List) sqlMap.executeQueryForList("getLoanAccClosingChargeInfoInt", where);
            } else {
                list = (List) sqlMap.executeQueryForList("getClosingAccountDetails", where);
            }
            if (this.commandMode.equals(CommonConstants.TOSTATUS_UPDATE) || this.commandMode.equals(CommonConstants.TOSTATUS_DELETE)) {
                if (list != null) {
                    if (list.size() > 0) {
                        String batchID = "";
                        HashMap hash = new HashMap();
                        hash = (HashMap) list.get(0);
                        System.out.println("###### hash : " + hash);
                        if (hash != null) {
                            if (hash.size() > 0) {
                                if (prodType.equals("TermLoan")) {
                                    hash.put("ACT_NUM", hash.get("ACCT_NUM"));
                                    acHeads = (HashMap) sqlMap.executeQueryForObject("getLoanAccountClosingHeads", hash.get("ACT_NUM"));
                                } else {
                                    acHeads = (HashMap) sqlMap.executeQueryForObject("getAccountClosingHeads", hash.get("ACT_NUM"));
                                }
                                HashMap getTransMap = new HashMap();
                                double actCloseCharge = CommonUtil.convertObjToDouble(hash.get("ACT_CLOSING_CHRG")).doubleValue();
                                getTransMap.put("LINK_BATCH_ID", hash.get("ACT_NUM"));
                                getTransMap.put("TODAY_DT", currDt);
                                getTransMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
                                if (actCloseCharge > 0) {
                                    getTransMap.put("AC_HD_ID", acHeads.get("ACCLOSE_CHRG"));
                                    getTransMap.put("AMOUNT", hash.get("ACT_CLOSING_CHRG"));
                                    List lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                                    if (lst != null) {
                                        if (lst.size() > 0) {
                                            batchID = CommonUtil.convertObjToStr(((HashMap) lst.get(0)).get("BATCH_ID"));
                                            returnMap.put("ACT_CLOSE_CHRG_TRANS_DETAILS", lst.get(0));
                                        }
                                    }
                                    lst = null;
                                }
                                double actChargeDet = CommonUtil.convertObjToDouble(hash.get("CHRG_DETAILS")).doubleValue();
                                if (actChargeDet > 0) {
                                    getTransMap.put("AC_HD_ID", acHeads.get("MISSER_CHRG"));
                                    getTransMap.put("AMOUNT", hash.get("CHRG_DETAILS"));
                                    if (batchID.length() > 0) {
                                        getTransMap.put("BATCH_ID", batchID);
                                    }
                                    List lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                                    if (lst != null) {
                                        if (lst.size() > 0) {
                                            //                                batchID = CommonUtil.convertObjToStr(((HashMap)lst.get(0)).get("BATCH_ID"));
                                            returnMap.put("CHRG_DETAILS_TRANS_DETAILS", lst.get(0));
                                        }
                                    }
                                    lst = null;
                                }
                                double advancesCreditInt = CommonUtil.convertObjToDouble(hash.get("AD_CREDIT_INT_AMT")).doubleValue();
                                if (advancesCreditInt > 0) {
                                    returnMap.put("ADVANCES_CREDIT_INT_TRANS_DETAILS", new Double(advancesCreditInt));
                                    getTransMap.put("AC_HD_ID", acHeads.get("AC_CREDIT_INT"));
                                    getTransMap.put("AMOUNT", hash.get("AD_CREDIT_INT_AMT"));
                                    if (batchID.length() > 0) {
                                        getTransMap.put("BATCH_ID", batchID);
                                    }
                                    System.out.println("getTransMap#####" + getTransMap);
                                    List lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                                    if (lst != null) {
                                        if (lst.size() > 0) {
                                            //                                batchID = CommonUtil.convertObjToStr(((HashMap)lst.get(0)).get("BATCH_ID"));
                                            returnMap.put("ADVANCES_CREDIT_INT_TRANS_DETAILS", lst.get(0));
                                        }
                                    }
                                    lst = null;
                                }

                                double availableSubsidy = CommonUtil.convertObjToDouble(hash.get("SUBSIDY_AMT")).doubleValue();
                                if (availableSubsidy > 0) {
                                    returnMap.put("AVAILABLE_SUBSIDY_TRANS_AMT", new Double(availableSubsidy));
                                    getTransMap.put("AC_HD_ID", hash.get("SUBSIDY_ADJUST_ACHD"));
                                    getTransMap.put("AMOUNT", hash.get("SUBSIDY_AMT"));
                                    if (batchID.length() > 0) {
                                        getTransMap.put("BATCH_ID", batchID);
                                    }
                                    System.out.println("getTransMap#####" + getTransMap);
                                    List lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                                    if (lst != null) {
                                        if (lst.size() > 0) {
                                            //                                batchID = CommonUtil.convertObjToStr(((HashMap)lst.get(0)).get("BATCH_ID"));
                                            returnMap.put("AVAILABLE_SUBSIDY_TRANS_AMT", lst.get(0));
                                        }
                                    }
                                    lst = null;
                                }

                            } else {
                                acHeads = (HashMap) sqlMap.executeQueryForObject("getAccountClosingHeads", hash.get("ACT_NUM"));
                            }
                        }
                        //                                HashMap getTransMap = new HashMap();
                        //                                double actCloseCharge = CommonUtil.convertObjToDouble(hash.get("ACT_CLOSING_CHRG")).doubleValue();
                        //                                getTransMap.put("LINK_BATCH_ID",hash.get("ACT_NUM"));
                        //                                getTransMap.put("TODAY_DT",ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID))));
                        //                                if (actCloseCharge > 0) {
                        //                                    getTransMap.put("AC_HD_ID",acHeads.get("ACCLOSE_CHRG"));
                        //                                    getTransMap.put("AMOUNT",hash.get("ACT_CLOSING_CHRG"));
                        //                                    List lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                        //                                    if (lst!=null)
                        //                                        if (lst.size()>0) {
                        //                                            batchID = CommonUtil.convertObjToStr(((HashMap)lst.get(0)).get("BATCH_ID"));
                        //                                            returnMap.put("ACT_CLOSE_CHRG_TRANS_DETAILS", lst.get(0));
                        //                                        }
                        //                                    lst = null;
                        //                                }
                        //                                double actChargeDet = CommonUtil.convertObjToDouble(hash.get("CHRG_DETAILS")).doubleValue();
                        //                                if (actChargeDet > 0) {
                        //                                    getTransMap.put("AC_HD_ID",acHeads.get("MISSER_CHRG"));
                        //                                    getTransMap.put("AMOUNT",hash.get("CHRG_DETAILS"));
                        //                                    if (batchID.length()>0)
                        //                                        getTransMap.put("BATCH_ID",batchID);
                        //                                    List lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                        //                                    if (lst!=null)
                        //                                        if (lst.size()>0) {
                        //                                            //                                batchID = CommonUtil.convertObjToStr(((HashMap)lst.get(0)).get("BATCH_ID"));
                        //                                            returnMap.put("CHRG_DETAILS_TRANS_DETAILS", lst.get(0));
                        //                                        }
                        //                                    lst = null;
                        //                                }
                    }
                }
            }
        } else if (this.commandMode.equals(CommonConstants.TOSTATUS_INSERT)) {
            if (map.containsKey("PROD_TYPE") && prodType.equals("TermLoan")) {
                where.put("BRANCH_CODE",branchcode);

                list = (List) sqlMap.executeQueryForList("getLoanAccClosingChargeInfo", where);
                if (list.size() > 0) {
                    clearBalMap = (HashMap) list.get(0);
                }
            } else {
                list = (List) sqlMap.executeQueryForList("getAccountClosingChargeInfo", where);
            }
            System.out.println("!!!!!!! this.commandMode : " + this.commandMode);
            // Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option
            String goldStockPhotoRequired = "N";
            HashMap photoMap = new HashMap();
            photoMap.put("PROD_ID", map.get("PROD_ID"));
            List photoLst = sqlMap.executeQueryForList("getSelectGoldStockPhotoRequired", photoMap);
            if (photoLst != null && photoLst.size() > 0) {
                photoMap = (HashMap) photoLst.get(0);
                goldStockPhotoRequired = CommonUtil.convertObjToStr(photoMap.get("GOLD_STOCK_PHOTO_REQUIRED"));
            }
            if (goldStockPhotoRequired.equalsIgnoreCase("Y")) {
                getStockPhoto(CommonUtil.convertObjToStr(map.get(CommonConstants.MAP_WHERE)));
                returnMap.put("STOCK_PHOTO_FILE", mapPhotoSign);
                mapPhotoSign = null;
            }
        }
        //        else if(this.commandMode.equals(CommonConstants.TOSTATUS_DELETE)){
        //            where.put("DELETED", "");
        //            if(prodType.equals("TermLoan"))
        //                list = (List) sqlMap.executeQueryForList("getClosingAccountDetailsTL", where);
        //            else
        //                list = (List) sqlMap.executeQueryForList("getClosingAccountDetails", where);
        //        }
        System.out.println("list : " + list);
        if(list!=null && list.size()>0)
        returnMap.put("AccountDetailsTO", (HashMap) list.get(0));
        System.out.println(">>> returnMap : " + returnMap);
        HashMap getDateMap = new HashMap();
        Date lastPayDate = new Date();
        List lst;
        //        if(prodType.equals("TermLoan") && (map.containsKey("PROD_TYPE") ||  this.commandMode.equals("AUTHORIZE") || commandMode.equals(CommonConstants.TOSTATUS_UPDATE) || commandMode.equals(CommonConstants.TOSTATUS_INSERT))){
        //            String prod_id=null;
        //            if(map.containsKey("PROD_ID"))
        //                  prod_id=CommonUtil.convertObjToStr(map.get("PROD_ID"));
        //            else{
        //                 lst=(List)sqlMap.executeQueryForList("getLastIntCalDate",map);
        //                getDateMap=(HashMap)lst.get(0);
        //               prod_id=  CommonUtil.convertObjToStr(getDateMap.get("PROD_ID"));
        //               map.put("PROD_ID",prod_id);
        //            }  }
        //            else if(this.commandMode.equals(CommonConstants.TOSTATUS_INSERT)){
        //                if(prodType.equals("TermLoan")){

        //                    where.put("BRANCH_CODE",map.get("BRANCH_CODE"));
        //
        //                    list=(List)sqlMap.executeQueryForList("getLoanAccClosingChargeInfo",where);
        //                    if(list.size()>0)
        //                        clearBalMap=(HashMap)list.get(0);
        //                }else
        //                    list = (List) sqlMap.executeQueryForList("getAccountClosingChargeInfo", where);
        //                System.out.println("!!!!!!! this.commandMode : " + this.commandMode);
        //            }
        //            else if(this.commandMode.equals(CommonConstants.TOSTATUS_DELETE)){
        //                where.put("DELETED", "");
        //                if(prodType.equals("TermLoan"))
        //                    list = (List) sqlMap.executeQueryForList("getClosingAccountDetailsTL", where);
        //                else
        //                    list = (List) sqlMap.executeQueryForList("getClosingAccountDetails", where);
        //            }
        //            System.out.println("list : " + list);
        //            returnMap.put("AccountDetailsTO", (HashMap)list.get(0));
        //            System.out.println(">>> returnMap : " + returnMap);
        //            getDateMap=new HashMap();
        //             lastPayDate=new Date();
        //             lst=null;
        //            if(map.containsKey("PROD_TYPE") ||  this.commandMode.equals("AUTHORIZE") || commandMode.equals(CommonConstants.TOSTATUS_UPDATE) || commandMode.equals(CommonConstants.TOSTATUS_INSERT)){
        if (map.containsKey("PROD_TYPE") || this.commandMode.equals("AUTHORIZE") || commandMode.equals(CommonConstants.TOSTATUS_UPDATE) || commandMode.equals(CommonConstants.TOSTATUS_INSERT)) {
            if (prodType.equals("TermLoan")) {
//                String prod_id=null;
                TaskHeader header = new TaskHeader();
                header.setBranchID(_branchCode);
                InterestCalculationTask interestCalTask = new InterestCalculationTask(header);
//                if(map.containsKey("PROD_ID"))
//                    prod_id=CommonUtil.convertObjToStr(map.get("PROD_ID"));
//                else{
//                    lst=(List)sqlMap.executeQueryForList("getLastIntCalDate",map);
//                    getDateMap=(HashMap)lst.get(0);
//                    prod_id=  CommonUtil.convertObjToStr(getDateMap.get("PROD_ID"));
//                    map.put("PROD_ID",prod_id);
//                }
              //  HashMap behaveLike = (HashMap) (sqlMap.executeQueryForList("getLoanBehaves", map).get(0));
                List allValList=sqlMap.executeQueryForList("getBehavesLikeForLoanAcc_No", map);
                HashMap behaveLike =new HashMap(0);
                if(allValList!=null && allValList.size()>0){
                 behaveLike = (HashMap) allValList.get(0);
                }
//                Date CURR_DATE=new Date();
//                CURR_DATE=currDt.clone();
//                System.out.println("curr_date###1"+currDt);
//                CURR_DATE=(Date)currDt.clone();
//                System.out.println("curr_date###2"+currDt);
                System.out.println("behaveLike=========== :"+behaveLike);
                productBehaveLike = CommonUtil.convertObjToStr(behaveLike.get("BEHAVES_LIKE"));
                if (CommonUtil.convertObjToStr(behaveLike.get("BEHAVES_LIKE")).equals("OD")) {
                    lst = (List) sqlMap.executeQueryForList("getLastIntCalDateAD", map);
                } else {
                    lst = (List) sqlMap.executeQueryForList("getLastIntCalDate", map);
                }
                //Below code added by Ajay Sharma for mantis ID 9086 dated 15 May 2014
                HashMap lastIntCalcDt = new HashMap();//Added by Ajay Sharma for mantis ID 9086
                HashMap lastIntCalc = new HashMap();
                if (lst != null && lst.size() > 0) {
                    lastIntCalc = (HashMap) lst.get(0);
                    lastIntCalcDt.put("LASTINTCALCDT", lastIntCalc.get("LAST_INT_CALC_DT"));
                    lastIntCalcDt.put("ACCOUNTNO", map.get("WHERE"));
                    System.out.println("value of lastIntCalcDt : " + lastIntCalcDt);
                    //sqlMap.executeUpdate("updateLoansFacilityDetailsTemp", lastIntCalcDt);//Commented for KD-3889
               }
                if(lst!=null && lst.size()>0){
                getDateMap = (HashMap) lst.get(0);
                }
                if (getDateMap != null && getDateMap.containsKey("LAST_INT_CALC_DT")) {
                    lastPayDate = (Date) getDateMap.get("LAST_INT_CALC_DT");
                }
                if (getDateMap != null && getDateMap.containsKey("PROD_ID")) {
                    map.put("PROD_ID", getDateMap.get("PROD_ID"));
                }
                if (CommonUtil.convertObjToStr(behaveLike.get("BEHAVES_LIKE")).equals("OD")) {
                    //                lst=(List)sqlMap.executeQueryForList("getLastIntCalDate",map);
                    //                getDateMap=(HashMap)lst.get(0);
                    //                lastPayDate=(Date)getDateMap.get("LAST_INTCALC_DTDEBIT");
                    //                noOfDays=DateUtil.dateDiff(lastPayDate, currDt);
                    //                Date CURR_DATE=currDt.clone();
                    //                    getDateMap.put("CURR_DATE",CURR_DATE);
                    getDateMap.put("BRANCH_ID", _branchCode);
                    getDateMap.put("BRANCH_CODE", _branchCode);
                    getDateMap.put("CURR_DATE", map.get("CURR_DATE"));
                    getDateMap.put("ACT_NUM", map.get(CommonConstants.MAP_WHERE));
                    getDateMap.put("BEHAVES_LIKE", behaveLike.get("BEHAVES_LIKE"));
                    getDateMap.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
                    getDateMap.put("PREMATURE_ONEMONTH_INT", "PREMATURE_ONEMONTH_INT");
                    getDateMap.put("INT_CALC_FROM_SCREEN","INT_CALC_FROM_SCREEN");

                    System.out.println("before od interest calculation####" + getDateMap);
                    getDateMap = interestCalTask.interestCalcTermLoanAD(getDateMap); // we need same used for TL also
                    double penalInt = 0;
                    double interest = 0;
                    System.out.println("after od interest calculation####" + getDateMap);
                    if (getDateMap != null && getDateMap.containsKey("LOAN_CLOSING_PENAL_INT")) {
                        penalInt = CommonUtil.convertObjToDouble(getDateMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                    }
                    if (getDateMap != null && getDateMap.containsKey("INTEREST")) {
                        interest = CommonUtil.convertObjToDouble(getDateMap.get("INTEREST")).doubleValue();
                    }
                    //                lst=sqlMap.executeQueryForList("getSumProductOD",getDateMap);
                    //                getDateMap=(HashMap)lst.get(0);
                    System.out.println(lastPayDate + "OD  #####!" + getDateMap);
                    //                    returnMap.put("AccountInterest",getDateMap.get("INTEREST"));
                    returnMap.put("AccountInterest", new Double(interest));
                    returnMap.put("AccountPenalInterest", new Double(penalInt));
                
                } else {

                    //                TaskHeader header=new TaskHeader();
                    //                header.setBranchID(_branchCode);
                    //                InterestCalculationTask interestCalTask = new InterestCalculationTask(header);
                    HashMap hash = new HashMap();
                    map.put("CURR_DATE", currDt);
                    map.put("BRANCH_ID", map.get("BRANCH_CODE"));
                    map.put("ACT_NUM", map.get("WHERE"));
                    map.put("BEHAVES_LIKE", behaveLike.get("BEHAVES_LIKE"));
                    map.put("LAST_INT_CALC_DT", lastPayDate);
                    map.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
                    //emiclosing identified purpose add
                    map.put("LOAN_EMI_CLOSE", "LOAN_EMI_CLOSE");
                    if (map.containsKey("NORMAL_CLOSER") || map.containsKey("DEPOSIT_PREMATURE_CLOSER")) {
                        map.put("SOURCE_SCREEN", "DEPOSIT_CLOSING");
                    } else {
                        map.put("SOURCE_SCREEN", "LOAN_CLOSING");
                    }
                    map.put("INT_CALC_FROM_SCREEN","INT_CALC_FROM_SCREEN");
                    HashMap  whereMAp=new HashMap();
                     HashMap  cumDepositMap=new HashMap();
                    whereMAp.put("ACCT_NUM", map.get("WHERE"));
                    List cumDepositlst = sqlMap.executeQueryForList("getDepositBehavesforLoan", whereMAp);
                        if (cumDepositlst != null && cumDepositlst.size() > 0) {
                            cumDepositMap = (HashMap) cumDepositlst.get(0);
                             String deposit_behaves ="";
                            if(cumDepositMap!=null && cumDepositMap.containsKey("BEHAVES_LIKE")){
                              deposit_behaves = CommonUtil.convertObjToStr(cumDepositMap.get("BEHAVES_LIKE"));}
                            if (deposit_behaves.equals("CUMMULATIVE")&& map.containsKey("NORMAL_CLOSER")) {
                                map.put("CALC_ON_MATURITY","Y");
                            }
                        }
                    System.out.println("before od interest calculation####" + map);
                    
                    //                hash=interestCalTask.calculateInterestNonBatchAndbatch(map);  //already calculating interest now using dayend balance comment
                    hash = interestCalTask.interestCalcTermLoanAD(map);
                    System.out.println("totInterest MAP#####" + map);
                    System.out.println("totInterest#####" + hash);
                    if (hash != null) {
                        //                    returnMap.put("AccountInterest",hash.get("TOT_INT"));
                        if (map.containsKey("DEPOSIT_PREMATURE_CLOSER")) {
                            returnMap.put("AccountInterest", hash.get("FINAL_INT"));
                            //                             returnMap.put("AccountPenalInterest",hash.get("LOAN_INT"));
                        } else {
                            returnMap.put("AccountInterest", hash.get("INTEREST"));
                            double penalInt = 0;
                            if (hash.containsKey("LOAN_CLOSING_PENAL_INT")) {
                                penalInt = CommonUtil.convertObjToDouble(hash.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                            }
                            double interest = CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();

                            returnMap.put("AccountInterest", new Double(interest));
                            returnMap.put("AccountPenalInterest", new Double(penalInt));
                            returnMap.put("LOANBALANCE", hash.get("PRINCIPAL_BAL"));
                        }
                    }
                    hash = null;
                    //                lst=(List)sqlMap.executeQueryForList("getNoOfDaysinLoan",where);
                    //                HashMap sanctionMap=new HashMap();
                    //                if(lst.size()>0)
                    //                    sanctionMap=(HashMap)lst.get(0);
                    //                Date fromDt=new Date();
                    //                fromDt =(Date)sanctionMap.get("FROM_DT");
                    //                noOfDays=DateUtil.dateDiff(currDt,fromDt);
                    //                System.out.println("tl  #####!"+noOfDays);

                }

                //            lst=(List)sqlMap.executeQueryForList("getInterestDetailsWhereConditions",where.get("ACT_NUM"));
                //            HashMap hash=new HashMap();
                //            if(lst.size()>0)
                //                hash=(HashMap)lst.get(0);
                //            hash.put("CATEGORY_ID",hash.get("CATEGORY"));
                //            hash.put("AMOUNT",hash.get("LIMIT"));
                //            List intList=sqlMap.executeQueryForList("getSelectProductTermLoanInterestTO",hash);
                //            termLoanTO=(TermLoanInterestTO)intList.get(0);
                //
                //            double intRate=termLoanTO.getInterest().doubleValue();
                //            System.out.println("rate@@@####"+termLoanTO.getAgainstClearingInt()+"###"+termLoanTO.getInterest()+"@@@@###"+termLoanTO.getToAmt());
                //            double clearBal=CommonUtil.convertObjToDouble(clearBalMap.get("CLEAR_BALANCE")).doubleValue();
                //            intRate=intRate/100;
                //            double product=CommonUtil.convertObjToDouble(getDateMap.get("PRODUCT")).doubleValue();
                //            double interestValue=(intRate*product)/365;
                //            System.out.println(product+"FINALINTEREST ###  "+interestValue+"INTRATE "+intRate+"clearbal"+clearBal+"noofday"+noOfDays);
                //            returnMap.put("AccountInterest",new Double(interestValue));
                //            return returnMap;
            }
            System.out.println(commandMode + "productBehaveLike##" + productBehaveLike);
            if (this.commandMode.equals("AUTHORIZE") || this.commandMode.equals("INSERT") || this.commandMode.equals("UPDATE")) {
                if (!prodType.equals("TermLoan")) {
                    System.out.print("interest CAlculationfor operative#####" + list.get(0));
                    System.out.println("" + getIntPayable((HashMap) list.get(0), map));
                    System.out.println("INTDSSSS" + ((InterestTask) getIntPayable((HashMap) list.get(0), map)).getInterestData());
                    returnMap.put("AccountInterest", ((InterestTask) getIntPayable((HashMap) list.get(0), map)).getInterestData());
                } else if (productBehaveLike.equals("OD")) {
                    System.out.print("interest CAlculationfor advances#####" + list.get(0));
                    HashMap resultmap = new HashMap();
                    resultmap = ((InterestTask) getIntPayable((HashMap) list.get(0), map)).getInterestData();
//                    returnMap.put("AccountInterest", ((InterestTask) getIntPayable((HashMap)list.get(0), map)).getInterestData());
                    returnMap.put("ADVANCES_CREDIT_INT", resultmap.get("ADVANCES_CREDIT_INT"));
                }
            }
        }
        System.out.println(">>> returnMapOPERATIVEACCOUNTINTEREST#### : " + returnMap);
        where = null;
        list = transactionDAO.getData(map);
        System.out.println("list in A DAO " + list);
        if (list != null) {
            if (list.size() > 0) {
                HashMap whereMap = new HashMap();
                TransactionTO transactionTO = null;
//                whereMap.put("LINK_BATCH_ID", map.get(CommonConstants.MAP_WHERE));
                List varList = (List) sqlMap.executeQueryForList("getVarNoActClose", map.get(CommonConstants.MAP_WHERE));
                if (varList != null) {
                    if (varList.size() > 0) {
                        HashMap varMap = new HashMap();
                        varMap = (HashMap) varList.get(0);
                        whereMap.put("LINK_BATCH_ID", varMap.get("VARIABLE_NO"));
                        returnMap.put("VARIABLE_NO", varMap.get("VARIABLE_NO"));
                        varMap = null;
                    } else {
                        whereMap.put("LINK_BATCH_ID", map.get(CommonConstants.MAP_WHERE));
                    }
                }
                whereMap.put("TODAY_DT", ServerUtil.getCurrentDateProperFormat(_branchCode));
                whereMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(_branchCode)));
                for (int i = 0; i < list.size(); i++) {
                    transactionTO = (TransactionTO) list.get(i);
                    List lstTrans = null;
                    if (transactionTO != null) {
                        if (!prodType.equals("TermLoan")) {
                            whereMap.put("AMOUNT", transactionTO.getTransAmt());
                        }
                        if (transactionTO.getTransType().equals(CommonConstants.TX_CASH)) {
                            lstTrans = sqlMap.executeQueryForList("getCashTransBatchID", whereMap);
                            if (lstTrans != null) {
                                if (lstTrans.size() > 0) {
                                    returnMap.put("CASH_TRANS_DETAILS", lstTrans.get(0));
                                }
                            }
                        } else {
                            whereMap.put("AMOUNT", transactionTO.getTransAmt());
                            lstTrans = sqlMap.executeQueryForList("getTransferTransBatchID", whereMap);
                            if (lstTrans != null) {
                                if (lstTrans.size() > 0) {
                                    returnMap.put("TRANSFER_TRANS_DETAILS", lstTrans.get(0));
                                }
                            }
                        }
                    }
                    lstTrans = null;
                    transactionTO = null;
                }
                whereMap = null;
            }
        }
        returnMap.put("TransactionTO", list);
        list = null;
        System.out.println(">>> returnMap : " + returnMap);
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;

    }

    private void destroyObjects() {
        transactionDAO = null;
        accountClosingTO = null;
        accountTO = null;
        productBehaveLike = "";
    }
    //        private void creditLoanAmount(HashMap obj)throws Exception {
    //            loanMap=new HashMap();
    //            HashMap finalMap=null;
    //            HashMap inputMap=new HashMap();
    //            loanMap.putAll(obj);
    //            finalMap=(HashMap)loanMap.get("TOTAL_AMOUNT");
    //            AccountClosingTO actTo=(AccountClosingTO)loanMap.get("accountclosing");
    //            try{
    //                double pInt=CommonUtil.convertObjToDouble(finalMap.get("CALCULATE_INT")).doubleValue();
    //                double Int=CommonUtil.convertObjToDouble(finalMap.get("CURR_MONTH_INT")).doubleValue();
    //                double principelBal=CommonUtil.convertObjToDouble(finalMap.get("PRINCIPLE_BAL")).doubleValue();
    //                double amount =actTo.getPayableBal().doubleValue();
    //                System.out.println("amount"+amount+"principal_bal"+principelBal+"finalMap"+finalMap);
    //                double totInt=pInt+Int;
    //                double principle=0;
    //                double paidPenal=pInt;
    //                double paidInt=Int;
    //                if(amount>=totInt){
    //                    amount-=pInt;
    //                    amount-=Int;
    //                    paidPenal=0;
    //                    paidInt=0;
    //                    if(amount>0.0){
    //                        principelBal-=amount;
    //                        principle=amount;
    //                    }
    //                }else if(amount>=pInt){
    //                    paidPenal=0;
    //                    amount-=pInt;
    //                    paidInt-=amount;
    //                }else{
    //                    //                    pInt-=amount;
    //                    paidPenal-=amount;
    //                }
    //                inputMap.put("ACCOUNTNO",actTo.getActNum());
    //                inputMap.put("BRANCH_CODE",_branchCode);
    //                inputMap.put("PROD_ID",loanMap.get("PROD_ID"));
    //                inputMap.put("TRANSTYPE",CommonConstants.CREDIT);
    //                inputMap.put("PRINCIPLE",new Double(principle));
    //                inputMap.put("PBAL",new Double(principelBal));
    //                inputMap.put("INTEREST",new Double(Int));
    //                inputMap.put("TODAY_DT",currDt);
    //                inputMap.put("IBAL",new Double(paidInt));
    //                inputMap.put("PENAL",new Double(pInt));
    //                inputMap.put("PIBAL",new Double(paidPenal));
    //                inputMap.put("TRN_CODE",new String("C*"));
    //                inputMap.put("EFFECTIVE_DT",loanMap.get("INSTALL_DT"));
    //                sqlMap.executeUpdate("insertintoloanTransDetails",inputMap);
    //                System.out.println("inputMap #####"+inputMap);
    //            }
    //            catch(Exception e){
    //                e.printStackTrace();
    //            }
    //            loanMap=null;
    //            inputMap=null;
    //        }

    /**
     * Getter for property lienAmount.
     *
     * @return Value of property lienAmount.
     */
    public double getLienAmount() {
        return lienAmount;
    }

    /**
     * Setter for property lienAmount.
     *
     * @param lienAmount New value of property lienAmount.
     */
    public void setLienAmount(double lienAmount) {
        this.lienAmount = lienAmount;
    }

    /**
     * Getter for property freezeAmount.
     *
     * @return Value of property freezeAmount.
     */
    public double getFreezeAmount() {
        return freezeAmount;
    }

    /**
     * Setter for property freezeAmount.
     *
     * @param freezeAmount New value of property freezeAmount.
     */
    public void setFreezeAmount(double freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    public static void main(String str[]) {
        try {
            HashMap inputMap = CommonUtil.serializeObjRead("D:\\myfile1.txt");
            //System.out.println("input Map : " + inputMap);
            AccountClosingDAO acobj = new AccountClosingDAO();
            //acobj.execute(inputMap);
            acobj.doAuthorize(inputMap);

            // //System.out.println("Start...");
            //            HashMap map = new HashMap();
            //            com.see.truetransact.transferobject.common.transaction.TransactionTO objTrans = new com.see.truetransact.transferobject.common.transaction.TransactionTO();
            //            objTrans.setApplName("test");
            //            objTrans.setTransType("TRANSFER");
            //            objTrans.setTransAmt(new Double("5175"));
            //            objTrans.setProductId("SBGEN");
            //            objTrans.setProductType("OA");
            //            objTrans.setDebitAcctNo("OA060901");
            //
            //            map.put("BRANCH_CODE", "Bran");
            //            map.put("FREEZE", "0.0");
            //            map.put("LIEN", "0.0");
            //            map.put("USER_ID", "sysadmin");
            //            map.put("IP_ADDR", "234");
            //
            //
            //            HashMap insMap = new HashMap();
            //            insMap.put("1", objTrans);
            //            map.put("MODE", "INSERT");
            //            map.put("TransactionTO", insMap);
            //
            //            AccountClosingTO objClose = new AccountClosingTO();
            //            objClose.setActNum("OA060892");
            //            objClose.setUnusedChk(new Double("7"));
            //            objClose.setActClosingChrg(new Double("25"));
            //            objClose.setIntPayable(new Double("0.0"));
            //            objClose.setPayableBal(new Double("5175"));
            //            objClose.setCommand(CommonConstants.TOSTATUS_INSERT);
            //
            //            map.put("accountclosing", objClose);

            //            AccountClosingDAO acobj = new AccountClosingDAO();
            //            acobj.execute(map);

            // //System.out.println("map:" + map);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String chqNoExistsOrNot(String chqNo, List chqList, long chqNos, List chqStopList) {
        System.out.println("chqNo:" + chqNo);
        System.out.println("chqList:" + chqList);
        System.out.println("chqNos:" + chqNos);
        System.out.println("chqStopList:" + chqStopList);
        String tempNo;
        String retStr = "";
        if (chqList!=null && chqList.size() > 0) {
            for (int i = 0; i < chqList.size(); i++) {
                HashMap map = new HashMap();
                map = (HashMap) chqList.get(i);
                if (CommonUtil.convertObjToStr(map.get("CHEQUE NO.")).equals(chqNo)) {
                    retStr = "USED";
                    break;
                }
            }
        }
        if (chqStopList != null && chqStopList.size() > 0) {
            long fromNo = 0;
            long toNo = 0;
            HashMap tempHash = new HashMap();
            tempHash = (HashMap) chqStopList.get(0);
            fromNo = CommonUtil.convertObjToLong(tempHash.get("START_CHQ_NO2"));
            toNo = CommonUtil.convertObjToLong(tempHash.get("END_CHQ_NO2"));
            if (toNo > 0) {
                if (chqNos >= fromNo && chqNos <= toNo) {
                    retStr = CommonUtil.convertObjToStr(tempHash.get("STOP_STATUS"));
                }
            } else {
                if (chqNos == fromNo) {
                    retStr = CommonUtil.convertObjToStr(tempHash.get("STOP_STATUS"));
                }
            }
        }

        if (retStr.length() == 0) {
            retStr = "DESTROYED";
        }
        return retStr;
    }

    /**
     * Getter for property advancesCreditInterest.
     *
     * @return Value of property advancesCreditInterest.
     */
    public java.lang.Double getAdvancesCreditInterest() {
        return advancesCreditInterest;
    }

    /**
     * Setter for property advancesCreditInterest.
     *
     * @param advancesCreditInterest New value of property
     * advancesCreditInterest.
     */
    public void setAdvancesCreditInterest(java.lang.Double advancesCreditInterest) {
        this.advancesCreditInterest = advancesCreditInterest;
    }    
    
    private Date setProperDtFormat(Date dt) {   //Added By Suresh
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }  
    
    // Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option
    private void setDriver() throws Exception {
        java.util.Properties serverProperties = new java.util.Properties();
        try {
            Dummy cons = new Dummy();
            serverProperties.load(cons.getClass().getResourceAsStream("/com/see/truetransact/serverutil/SqlMapConfig.properties"));
            dataBaseURL = serverProperties.getProperty("url");
            userName = serverProperties.getProperty("username");
            passWord = serverProperties.getProperty("password");
            driverName = serverProperties.getProperty("driver");
            Class.forName(serverProperties.getProperty("driver"));
            serverProperties = new java.util.Properties();
            serverProperties.load(cons.getClass().getResourceAsStream("/com/see/truetransact/commonutil/TT.properties"));
            SERVER_ADDRESS = "http://" + CommonConstants.SERVER_IP_ADDR + ":" + serverProperties.getProperty("HTTP_PORT") + "/" + serverProperties.getProperty("HTTP_CONTEXT");
            System.out.println("#### SERVER_ADDRESS : " + SERVER_ADDRESS);
            serverProperties = null;
            cons = null;
        } catch (Exception ex) {
        }
    }
    
    // Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option
    private void getStockPhoto(String actNo) throws Exception {
        try {
            mapPhotoSign = new HashMap();           
            setDriver();
            conn = DriverManager.getConnection(dataBaseURL, userName, passWord);            
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            String st = "select ACCT_NUM,STOCK_PHOTO_FILE from SECURITY_DETAILS where " + "ACCT_NUM = '" + actNo + "'";            
            //System.out.println("#### statement to be executed : " + st);
            rset = stmt.executeQuery(st);
            //System.out.println("#### rset.getRow() " + rset.getRow());
            if (rset != null) {
                //System.out.println("#### rset not null");
                while (rset.next()) {
                    //System.out.println("#### rset in while");
                    java.sql.Blob oracleBlob = null;
                    File f1 = null;
                    FileOutputStream out = null;
                    InputStream in = null;
                    java.net.URI serverURI = null;
                    int fileLength = 0;
                    byte[] blobBytes;
                    int len = 0;
                    if (rset.getBlob(2) != null) {
                        //System.out.println("#### Cust ID : " + rset.getString(1));   // Print col 1
                        oracleBlob = rset.getBlob(2);                       
                        long length = oracleBlob.length();                       
                        //System.out.println("photo blob length " + length);
                        if (length != 0) {
                            //System.out.println("#### " + SERVER_ADDRESS + "goldloan/" + actNo + "_photo.jpg");
                            serverURI = new java.net.URI(SERVER_ADDRESS + "goldloan/" + actNo + "_photo.jpg");
                            f1 = new File(ServerConstants.SERVER_PATH + "\\goldloan\\" + actNo + "_photo.jpg");
                            f1.createNewFile();
                            out = new FileOutputStream(f1);
                            in = oracleBlob.getBinaryStream();                          
                            //System.out.println("fileLength " + fileLength);
                            fileLength = (int) length;
                            blobBytes = new byte[fileLength];
                            //System.out.println("blobBytes.length " + blobBytes.length);
                            len = -1;
                            in.read(blobBytes);
                            //System.out.println("inside while len " + len);
                            out.write(blobBytes);
                            out.close();
                            in.close();                         
                            mapPhotoSign.put("PHOTO", blobBytes);
                        }
                    }                                     
                    f1 = null;
                    out = null;
                    in = null;
                    oracleBlob = null;
                }
            }           
            rset.close();
            stmt.close();
        } catch (Exception se) {
            se.printStackTrace();         
            rset.close();
            stmt.close();
            //System.out.println("SQL Exception : " + se);
        }
    }
    
}