/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductDAO.java
 *
 * Created on Wed Nov 24 16:51:38 GMT+05:30 2004
 */
package com.see.truetransact.serverside.investments;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import com.see.truetransact.commonutil.TTException;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.TTDAO;
import java.util.Date;
//import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.investments.InvestmentsMasterTO;
import com.see.truetransact.transferobject.investments.InvestmentsOperativeTO;
import com.see.truetransact.transferobject.investments.InvestmentsDepositTO;
import com.see.truetransact.transferobject.investments.InvestmentsShareTO;
import com.see.truetransact.transferobject.investments.InvestmentsRFTO;
import com.see.truetransact.transferobject.product.share.ShareProductLoanTO;
import com.see.truetransact.transferobject.investments.InvestmentsChequeTO;
import com.see.truetransact.transferobject.investments.InvestmentsTransTO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
//import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import java.text.SimpleDateFormat;

/**
 * InvestmentsMaster DAO.
 *
 */
public class InvestmentsMasterDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private InvestmentsMasterTO objTO;
    private InvestmentsOperativeTO objInvestmentsOperativeTO;
    private InvestmentsDepositTO objInvestmentsDepositTO;
    private InvestmentsShareTO objInvestmentsShareTO;
    private InvestmentsRFTO objInvestmentsRFTO;
    private InvestmentsChequeTO objInvestmentsChequeTO;
//    private ShareProductLoanTO objLoanTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private InvestmentsTransTO objInvestmentsTransTO;
    private ArrayList objInvestmentsTransTOList = new ArrayList();
    private HashMap loanDataMap = new HashMap();
    private HashMap deletedLoanDataMap;
    private String where = "";
    private LinkedHashMap deletedTableValues = null;
    private LinkedHashMap tableDetails = null;
    private String investmentID = "";
    private TransactionTO objTransactionTO;
    private TransactionDAO transactionDAO = null;
    private Date currDt;
    private HashMap returnDataMap = null;
    final String SCREEN = "TD";
    private HashMap pickSingleTransId = new HashMap();
    private String generateSingleTransId = "";
    private String cashSingleTransId = "";
    private HashMap commonBatchId = new HashMap();
    private ArrayList transferList = null;
    HashMap reMap = new HashMap();
    double tdsAmt = 0;
    Date backDate = null;

    /**
     * Creates a new instance of ShareProductDAO
     */
    public InvestmentsMasterDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private String getInvestmentId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "INVESTMENT_ID");
        String investmentId = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return investmentId;
    }

    /**
     * This method is used to insertnew datat into the Table
     */
    private void insertData(HashMap map) throws Exception {
        investmentID = "";
        investmentID = getInvestmentId();
        generateSingleTransId = generateLinkID();
        cashSingleTransId = generateLinkID();
        if (objInvestmentsOperativeTO != null) {
            logTO.setData(objInvestmentsOperativeTO.toString());
            logTO.setPrimaryKey(objInvestmentsOperativeTO.getKeyData());
            logTO.setStatus(objInvestmentsOperativeTO.getCommand());
            logDAO.addToLog(logTO);
            objInvestmentsOperativeTO.setInvestmentId(investmentID);
            objInvestmentsTransTO.setInvestment_internal_Id(investmentID);
            objInvestmentsOperativeTO.setTxtBranchCode(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
            sqlMap.executeUpdate("insertInvestmentsOperativeTO", objInvestmentsOperativeTO);
            sqlMap.executeUpdate("insertInvestmentsMasterTO", objInvestmentsOperativeTO);
            if (map.containsKey("chequeBookDetails")) {
                insertChequeDetails();
            }
        } else if (objInvestmentsDepositTO != null) {
            logTO.setData(objInvestmentsDepositTO.toString());
            logTO.setPrimaryKey(objInvestmentsDepositTO.getKeyData());
            logTO.setStatus(objInvestmentsDepositTO.getCommand());
            logDAO.addToLog(logTO);
            objInvestmentsDepositTO.setInvestmentId(investmentID);
            objInvestmentsTransTO.setInvestment_internal_Id(investmentID);
            objInvestmentsDepositTO.setTxtBranchCode(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
            objInvestmentsDepositTO.setRenewalDt(currDt);
            sqlMap.executeUpdate("insertInvestmentsDepositTO", objInvestmentsDepositTO);
            sqlMap.executeUpdate("insertInvestmentsMasterTO", objInvestmentsDepositTO);
        } else if (objInvestmentsShareTO != null) {
            logTO.setData(objInvestmentsShareTO.toString());
            logTO.setPrimaryKey(objInvestmentsShareTO.getKeyData());
            logTO.setStatus(objInvestmentsShareTO.getCommand());
            logDAO.addToLog(logTO);
            objInvestmentsShareTO.setInvestmentId(investmentID);
            objInvestmentsTransTO.setInvestment_internal_Id(investmentID);
            objInvestmentsShareTO.setTxtBranchCode(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
            sqlMap.executeUpdate("insertInvestmentsShareTO", objInvestmentsShareTO);
            sqlMap.executeUpdate("insertInvestmentsMasterTO", objInvestmentsShareTO);
        } else if (objInvestmentsRFTO != null) {
            logTO.setData(objInvestmentsRFTO.toString());
            logTO.setPrimaryKey(objInvestmentsRFTO.getKeyData());
            logTO.setStatus(objInvestmentsRFTO.getCommand());
            logDAO.addToLog(logTO);
            objInvestmentsRFTO.setInvestmentId(investmentID);
            objInvestmentsTransTO.setInvestment_internal_Id(investmentID);
            objInvestmentsRFTO.setTxtBranchCode(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
            sqlMap.executeUpdate("insertInvestmentsRFTO", objInvestmentsRFTO);
            sqlMap.executeUpdate("insertInvestmentsMasterTO", objInvestmentsRFTO);
        }

        // Transaction Part
//        System.out.println("##################### objInvestmentsTransTO : " + objInvestmentsTransTO);
//        System.out.println("##################### Map : " + map);
        doTransaction(map);
        objInvestmentsTransTO.setBatchID(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
        objInvestmentsTransTO.setInitiatedBranch(_branchCode);
        if (loanDataMap != null) {
            sqlMap.executeUpdate("insertInvestmentTransTO", objInvestmentsTransTO);
        }
    }

    private HashMap doTransaction(HashMap map) throws Exception {
        System.out.println("In Side The DoTransaction");
        HashMap achdMap = new HashMap();
        String investMentType = "";
        investMentType = CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentBehaves());
        achdMap.put("INVESTMENT_TYPE", CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentBehaves()));
        achdMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentID()));
        //  System.out.println("achdMap before query ------------>" + achdMap);
        List achdLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", achdMap);
        if (achdLst != null && achdLst.size() > 0) {
            achdMap = new HashMap();
            ArrayList transferList = new ArrayList();
            ArrayList cashList = new ArrayList();
            achdMap = (HashMap) achdLst.get(0);
            //  System.out.println("achdMap--------------->" + achdMap);
            TransferTrans objTransferTrans = new TransferTrans();
            objTransferTrans.setInitiatedBranch(_branchCode);
            objTransferTrans.setLinkBatchId(objInvestmentsTransTO.getInvestmentName());
            HashMap txMap = new HashMap();
            if (!objInvestmentsTransTO.getTrnCode().equals("") && map.containsKey("TransactionTO")) {
                LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                if (TransactionDetailsMap.size() > 0) {
                    if (objInvestmentsTransTO.getTrnCode().equals("Deposit")) {      //DEPOSIT AMOUNT TRANSACTION
                        if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                            LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                            for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                double tranAmt = 0.0;
                                objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                if (investMentType.equals("OTHER_BANK_SB") || investMentType.equals("OTHER_BANK_CA") || investMentType.equals("OTHER_BANK_SPD") || investMentType.equals("RESERVE_FUND_DCB")
                                        || investMentType.equals("OTHER_BANK_FD") || investMentType.equals("OTHER_BANK_SSD") || investMentType.equals("OTHER_BANK_RD") || investMentType.equals("OTHER_BANK_CCD")) {
                                    if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                        txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                        txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                        System.out.println("777----" + objInvestmentsTransTO.getInvestment_Ref_No());
                                        txMap.put(TransferTrans.PARTICULARS, objInvestmentsTransTO.getInvestment_Ref_No() + " " + objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName() + " - " + "Deposit");
                                        txMap.put("AUTHORIZEREMARKS", "TRANSACTIONAMOUNT");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                        txMap.put("generateSingleTransId", generateSingleTransId);
                                        if (CommonUtil.convertObjToStr(objTransactionTO.getProductType()).equals("GL")) {
                                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        }
                                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                        tranAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                        if (objTransactionTO.getProductType().equals("OA")) {
                                            txMap.put("TRANS_MOD_TYPE", "OA");
                                        } else if (objTransactionTO.getProductType().equals("AB")) {
                                            txMap.put("TRANS_MOD_TYPE", "AB");
                                        } else if (objTransactionTO.getProductType().equals("SA")) {
                                            txMap.put("TRANS_MOD_TYPE", "SA");
                                        } else if (objTransactionTO.getProductType().equals("TL")) {
                                            txMap.put("TRANS_MOD_TYPE", "TL");
                                        } else if (objTransactionTO.getProductType().equals("AD")) {
                                            txMap.put("TRANS_MOD_TYPE", "AD");
                                        } else {
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                        }
                                        txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());//KD-4023
                                        txMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, tranAmt));
                                        if (objInvestmentsTransTO.getInvestmentAmount().doubleValue() != 0.0) {
                                            txMap.put(TransferTrans.DR_AC_HD, (String) achdMap.get("IINVESTMENT_AC_HD"));
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_AC_HD");
                                            //System.out.println("888----" + objInvestmentsTransTO.getInvestment_Ref_No());
                                            txMap.put(TransferTrans.PARTICULARS, objInvestmentsTransTO.getInvestment_Ref_No() + " " + objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName() + " - " + "Deposit");
                                            txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
                                            txMap.put("generateSingleTransId", generateSingleTransId);
                                            txMap.put("TRANS_MOD_TYPE", "INV");
                                            txMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objInvestmentsTransTO.getInvestmentAmount().doubleValue()));
                                        }
                                        //  System.out.println("transferList  ------------->" + transferList);
                                    } else {
                                        HashMap investmentTransMap = new HashMap();
                                        investmentTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                                        investmentTransMap.put("BRANCH_CODE", _branchCode);
                                        investmentTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                        investmentTransMap.put("USER_ID", objInvestmentsTransTO.getStatusBy());
                                        investmentTransMap.put("INVESTMENT_NO", objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName());
                                        if (objInvestmentsTransTO.getInvestmentAmount().doubleValue() != 0.0) {
                                            investmentTransMap.put("ACCT_HEAD", achdMap.get("IINVESTMENT_AC_HD"));
                                            investmentTransMap.put("TRANS_AMOUNT", objInvestmentsTransTO.getInvestmentAmount());
                                            investmentTransMap.put("AUTHORIZEREMARKS", "IINVESTMENT_AC_HD");
                                            investmentTransMap.put("TRN_CODE", "Deposit");
                                            investmentTransMap.put("TRANS_MOD_TYPE", "INV");
                                            investmentTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                            cashList.add(createCashDebitTransactionTO(investmentTransMap));
                                        }
                                        //   System.out.print("###### cashList: " + cashList);
                                    }
                                }
                            }
                        }

                        // System.out.println("transferList total ------------->" + transferList);
                    } else if (objInvestmentsTransTO.getTrnCode().equals("Purchase")) {      //SHARE TRANSACTION
                        if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                            LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                            for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                double tranAmt = 0.0;
                                objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                if (investMentType.equals("SHARES_DCB") || investMentType.equals("SHARE_OTHER_INSTITUTIONS")) {
                                    if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                        txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                        txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                        //   System.out.println("999----" + objInvestmentsTransTO.getInvestment_Ref_No());
                                        txMap.put(TransferTrans.PARTICULARS, objInvestmentsTransTO.getInvestment_Ref_No() + " " + objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName() + " - " + "Purchase");
                                        txMap.put("AUTHORIZEREMARKS", "TRANSACTIONAMOUNT");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                        txMap.put("generateSingleTransId", generateSingleTransId);
                                        if (CommonUtil.convertObjToStr(objTransactionTO.getProductType()).equals("GL")) {
                                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        }
                                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                        tranAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                        if (objTransactionTO.getProductType().equals("OA")) {
                                            txMap.put("TRANS_MOD_TYPE", "OA");
                                        } else if (objTransactionTO.getProductType().equals("AB")) {
                                            txMap.put("TRANS_MOD_TYPE", "AB");
                                        } else if (objTransactionTO.getProductType().equals("SA")) {
                                            txMap.put("TRANS_MOD_TYPE", "SA");
                                        } else if (objTransactionTO.getProductType().equals("TL")) {
                                            txMap.put("TRANS_MOD_TYPE", "TL");
                                        } else if (objTransactionTO.getProductType().equals("AD")) {
                                            txMap.put("TRANS_MOD_TYPE", "AD");
                                        } else {
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                        }
                                        txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());//KD-4023
                                        txMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, tranAmt));
                                        if (objInvestmentsTransTO.getInvestmentAmount().doubleValue() != 0.0) {
                                            double feesPaid = 0.0;
                                            double shareAmt = 0.0;
                                            shareAmt = objInvestmentsTransTO.getInvestmentAmount().doubleValue();
                                            feesPaid = objInvestmentsTransTO.getPurchaseRate().doubleValue();
//                                            System.out.println("#### Total Share Amount : " + shareAmt);
//                                            System.out.println("#### feesPaid : " + feesPaid);
                                            if (feesPaid > 0) {
                                                txMap.put(TransferTrans.DR_AC_HD, (String) achdMap.get("CHARGE_PAID_AC_HD"));
                                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put("AUTHORIZEREMARKS", "SHARE_FEES_AC_HD");
                                                //  System.out.println("qqq----" + objInvestmentsTransTO.getInvestment_Ref_No());
                                                txMap.put(TransferTrans.PARTICULARS, objInvestmentsTransTO.getInvestment_Ref_No() + " " + objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName() + " - " + "Purchase");
                                                txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
                                                txMap.put("generateSingleTransId", generateSingleTransId);
                                                txMap.put("TRANS_MOD_TYPE", "INV");
                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, feesPaid));
                                                //shareAmt -= feesPaid; //Commented by nithya on 06-05-2021 for KD-2856
                                            }
                                            if (shareAmt > 0) {
                                                txMap.put(TransferTrans.DR_AC_HD, (String) achdMap.get("IINVESTMENT_AC_HD"));
                                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put("AUTHORIZEREMARKS", "SHARE_IINVESTMENT_AC_HD");
                                                //  System.out.println("www----" + objInvestmentsTransTO.getInvestment_Ref_No());
                                                txMap.put(TransferTrans.PARTICULARS, objInvestmentsTransTO.getInvestment_Ref_No() + " " + objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName() + " - " + "Purchase");
                                                txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
                                                txMap.put("generateSingleTransId", generateSingleTransId);
                                                txMap.put("TRANS_MOD_TYPE", "INV");
                                                txMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, shareAmt));
                                            }
                                            //  System.out.println("transferList  ------------->" + transferList);
                                        }
                                    } else {
                                        HashMap investmentTransMap = new HashMap();
                                        investmentTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                                        investmentTransMap.put("BRANCH_CODE", _branchCode);
                                        investmentTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                        investmentTransMap.put("USER_ID", objInvestmentsTransTO.getStatusBy());
                                        investmentTransMap.put("INVESTMENT_NO", objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName());
                                        if (objInvestmentsTransTO.getInvestmentAmount().doubleValue() != 0.0) {
                                            double feesPaid = 0.0;
                                            double shareAmt = 0.0;
                                            shareAmt = objInvestmentsTransTO.getInvestmentAmount().doubleValue();
                                            feesPaid = objInvestmentsTransTO.getPurchaseRate().doubleValue();
//                                            System.out.println("#### Total Share Amount : " + shareAmt);
//                                            System.out.println("#### feesPaid : " + feesPaid);
                                            if (feesPaid > 0) {
                                                investmentTransMap.put("ACCT_HEAD", achdMap.get("CHARGE_PAID_AC_HD"));
                                                investmentTransMap.put("TRANS_AMOUNT", String.valueOf(feesPaid));
                                                investmentTransMap.put("AUTHORIZEREMARKS", "SHARE_FEES_AC_HD");
                                                investmentTransMap.put("TRN_CODE", "Purchase");
                                                investmentTransMap.put("TRANS_MOD_TYPE", "INV");
                                                investmentTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                                cashList.add(createCashDebitTransactionTO(investmentTransMap));
                                                //shareAmt -= feesPaid; //Commented by nithya on 06-05-2021 for KD-2856
                                            }
                                            //     System.out.println("####Share Amount : " + shareAmt);
                                            if (shareAmt > 0) {
                                                investmentTransMap.put("ACCT_HEAD", achdMap.get("IINVESTMENT_AC_HD"));
                                                investmentTransMap.put("TRANS_AMOUNT", String.valueOf(shareAmt));
                                                investmentTransMap.put("AUTHORIZEREMARKS", "SHARE_IINVESTMENT_AC_HD");
                                                investmentTransMap.put("TRN_CODE", "Purchase");
                                                investmentTransMap.put("TRANS_MOD_TYPE", "INV");
                                                investmentTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                                cashList.add(createCashDebitTransactionTO(investmentTransMap));
                                            }
                                            //   System.out.print("###### cashList: " + cashList);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (cashList != null && cashList.size() > 0) {
                        doCashTrans(cashList, _branchCode, false);
                    }
                    if (transferList != null && transferList.size() > 0) {
                        doDebitCredit(transferList, _branchCode, false);
                    }
                    transferList = null;
                    cashList = null;
//                    changed here 
                    getTransDetails(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
                    returnDataMap.put("BATCH_ID", CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
                } else {
                    throw new Exception("investment  Transaction is not proper");
                }
            }

        } else {
            throw new Exception("investment  Config Date is Not set...");
        }
        return null;
    }

    private CashTransactionTO createCashDebitTransactionTO(HashMap dataMap) throws Exception {
        CashTransactionTO objCashTO = new CashTransactionTO();
        if (dataMap.get("STATUS").equals(CommonConstants.TOSTATUS_INSERT)) {
            objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
            objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.GL));
            objCashTO.setTransType(CommonConstants.DEBIT);
            objCashTO.setParticulars("By Cash : " + dataMap.get("INVESTMENT_NO") + " - " + dataMap.get("TRN_CODE"));
            objCashTO.setAuthorizeRemarks(CommonUtil.convertObjToStr(dataMap.get("AUTHORIZEREMARKS")));
            objCashTO.setInpAmount(CommonUtil.convertObjToDouble(dataMap.get("TRANS_AMOUNT")));
            objCashTO.setAmount(CommonUtil.convertObjToDouble(dataMap.get("TRANS_AMOUNT")));
            objCashTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            objCashTO.setBranchId(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
            objCashTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            objCashTO.setStatusDt(currDt);
            objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            objCashTO.setCommand("INSERT");
            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestment_internal_Id()));
            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
            objCashTO.setSingleTransId(cashSingleTransId);
            if (dataMap.containsKey("SCREEN_NAME") && dataMap.get("SCREEN_NAME") != null) {
                objCashTO.setScreenName((String) dataMap.get("SCREEN_NAME"));
            }
            System.out.println("objCashTO 1st one:" + objCashTO);
        }
        return objCashTO;
    }

    private void doCashTrans(ArrayList batchList, String branchCode, boolean isAutoAuthorize) throws Exception {
        CashTransactionDAO cashDAO = new CashTransactionDAO();
        HashMap data = new HashMap();
        data.put("DAILYDEPOSITTRANSTO", batchList);
        data.put("COMMAND", objInvestmentsTransTO.getCommand());
        data.put("INITIATED_BRANCH", branchCode);
        data.put(CommonConstants.BRANCH_ID, branchCode);
        data.put(CommonConstants.USER_ID, logTO.getUserId());
        data.put(CommonConstants.MODULE, "Transaction");
        data.put(CommonConstants.SCREEN, "");
        data.put("MODE", "MODE");
        if (isAutoAuthorize) {
            HashMap authorizeMap = new HashMap();
            authorizeMap.put("BATCH_ID", null);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
            data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
        }
        loanDataMap = cashDAO.execute(data, false);
        System.out.println("loanDataMap---------------cash->" + loanDataMap);
        //    System.out.println("cashs ID" + generateSingleTransId);
        if (isAutoAuthorize != true) {
            if (objTransactionTO == null) {
                objTransactionTO = new TransactionTO();
            }

            objTransactionTO.setBatchId(CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestment_internal_Id()));
            objTransactionTO.setBatchDt(currDt);
            //      System.out.println("objInvestmentsTransTO.getInvestmentName()---------------->" + objInvestmentsTransTO.getInvestmentName());
            String val = (String) objInvestmentsTransTO.getInvestmentName();
            if (val.length() > 15) {
                val = (String) objInvestmentsTransTO.getInvestmentName().substring(0, 15);
            }
            objTransactionTO.setTransId(CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestment_internal_Id()));
            //    System.out.println("objTransactionTO.getTransId()---------------->" + val);
            objTransactionTO.setStatus(objInvestmentsTransTO.getStatus());
            objTransactionTO.setBranchId(_branchCode);
            //  System.out.println("objTransactionTO------------------->" + objTransactionTO);
            if (CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue() > 0) {
                sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);
            }
            objTransactionTO = null;
            HashMap linkBatchMap = new HashMap();                           //update LinkBatchId
            linkBatchMap.put("LINK_BATCH_ID", objInvestmentsTransTO.getInvestment_internal_Id());
            linkBatchMap.put("TRANS_ID", loanDataMap.get("TRANS_ID"));
            linkBatchMap.put("TRANS_DT", currDt);
            linkBatchMap.put("INITIATED_BRANCH", _branchCode);
            sqlMap.executeUpdate("updateLinkBatchIdCash", linkBatchMap);
            linkBatchMap = null;
        }
    }

    private void doDebitCredit(ArrayList batchList, String branchCode, boolean isAutoAuthorize) throws Exception {
        TransferDAO transferDAO = new TransferDAO();
        HashMap data = new HashMap();
        data.put("TxTransferTO", batchList);
        data.put("COMMAND", objInvestmentsTransTO.getCommand());
        data.put("INITIATED_BRANCH", branchCode);
        data.put(CommonConstants.BRANCH_ID, branchCode);
        data.put(CommonConstants.USER_ID, logTO.getUserId());
        data.put(CommonConstants.MODULE, "Transaction");
        data.put("MODE", "MODE");
        data.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestment_internal_Id()));
        if (isAutoAuthorize) {
            HashMap authorizeMap = new HashMap();
            authorizeMap.put("BATCH_ID", null);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
            data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
        }
//        System.out.println("doDebitCredit : "+objInvestmentsTransTOList);
//        System.out.println("backDate^$^$^#"+backDate);
        if (backDate != null && !backDate.equals("")) {
            data.put("TRANS_DATE", getProperDateFormat(backDate));
            data.put("BACK_DATED_TRANSACTION", "BACK_DATED_TRANSACTION");
            data.put("INV_BACK_DATED_TRANSACTION", "INV_BACK_DATED_TRANSACTION");
            data.put(CommonConstants.SCREEN, "INV_BACK_DATED_TRANSACTION");
        }
        loanDataMap = transferDAO.execute(data, false);
        if (isAutoAuthorize != true) {
            if (loanDataMap != null && loanDataMap.containsKey("TRANS_ID")) {
                commonBatchId.put("BATCH_ID", CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
            }
//            System.out.println("loanDataMap-------------Transfer--->" + loanDataMap);
//            System.out.println("transfer ID" + generateSingleTransId);
            if (generateSingleTransId != null && (!generateSingleTransId.equals(""))) {
                pickSingleTransId.put("SINGLE_TRANSFER_TRANSID", generateSingleTransId);
            }
            if (objTransactionTO == null) {
                objTransactionTO = new TransactionTO();
            }
            objTransactionTO.setBatchId(CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestment_internal_Id()));
//            objTransactionTO.setBatchId(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
            objTransactionTO.setBatchDt(currDt);
            //System.out.println("objInvestmentsTransTO.getInvestmentName()---------------->" + objInvestmentsTransTO.getInvestmentName());
            String val = (String) objInvestmentsTransTO.getInvestmentName();
            if (val.length() > 15) {
                val = (String) objInvestmentsTransTO.getInvestmentName().substring(0, 15);
            }
//            objTransactionTO.setTransId(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
            objTransactionTO.setTransId(CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestment_internal_Id()));
            //System.out.println("objTransactionTO.getTransId()---------------->" + val);
            objTransactionTO.setStatus(objInvestmentsTransTO.getStatus());
            objTransactionTO.setBranchId(_branchCode);
            //System.out.println("objTransactionTO------------------->" + objTransactionTO);
            if (CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue() > 0) {
                sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);
            }
            objTransactionTO = null;
        }
    }

    public Date getProperDateFormat(Object obj) {
        Date properDt = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            properDt = (Date) currDt.clone();
            properDt.setDate(tempDt.getDate());
            properDt.setMonth(tempDt.getMonth());
            properDt.setYear(tempDt.getYear());
        }
        return properDt;
    }

    private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        System.out.println("batchId$$$$$$$$$$$" + batchId);
        getTransMap.put("BATCH_ID", batchId);
        if (backDate != null && !backDate.equals("")) {
            getTransMap.put("TRANS_DT", backDate);
        } else {
            getTransMap.put("TRANS_DT", currDt.clone());
        }
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        returnDataMap = new HashMap();
        List transList = (List) sqlMap.executeQueryForList("getTransferDetailsInvestment", getTransMap);
        if (transList != null && transList.size() > 0) {
            returnDataMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetailsInvestment", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            returnDataMap.put("CASH_TRANS_LIST", cashList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    private void getRenewalTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        String sCashId = "";
        String sTransId = "";
        if (pickSingleTransId != null) {
            if (pickSingleTransId.containsKey("SINGLE_CASH_TRANSID") && pickSingleTransId.get("SINGLE_CASH_TRANSID") != null) {
                sCashId = CommonUtil.convertObjToStr(pickSingleTransId.get("SINGLE_CASH_TRANSID"));
                getTransMap.put("S_CASH_ID", sCashId);
            }
            if (pickSingleTransId.containsKey("SINGLE_TRANSFER_TRANSID") && pickSingleTransId.get("SINGLE_TRANSFER_TRANSID") != null) {
                sTransId = CommonUtil.convertObjToStr(pickSingleTransId.get("SINGLE_TRANSFER_TRANSID"));
                getTransMap.put("S_TRANS_ID", sTransId);
            }
        }
        getTransMap.put("BATCH_ID", batchId);
        if (backDate != null && !backDate.equals("")) {
            getTransMap.put("TRANS_DT", backDate);
        } else {
            getTransMap.put("TRANS_DT", currDt.clone());
        }
        getTransMap.put("AUTHORIZE_STATUS", "AUTHORIZE_STATUS");
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        System.out.println("Gettrans Map" + getTransMap);
        returnDataMap = new HashMap();
        List transList = (List) sqlMap.executeQueryForList("getTransferDetailsWithTransId", getTransMap);
        if (transList != null && transList.size() > 0) {
            returnDataMap.put("TRANSFER_TRANS_LIST", transList);
        }

        List cashList = (List) sqlMap.executeQueryForList("getCashDetailsWithTransId", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            if (getTransMap.containsKey("S_CASH_ID")) {
                returnDataMap.put("CASH_TRANS_LIST", cashList);
            }
        }
        System.out.println("TransLlst" + transList);
        System.out.println("CashList" + cashList);
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    private void insertChequeDetails() throws Exception {
        if (tableDetails != null) {
            ArrayList addList = new ArrayList(tableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                InvestmentsChequeTO objInvestmentsChequeTO = (InvestmentsChequeTO) tableDetails.get(addList.get(i));
                objInvestmentsChequeTO.setInvestmentId(investmentID);
                sqlMap.executeUpdate("insertInvestmentsChequeTO", objInvestmentsChequeTO);
                int fromNo = 0;
                int toNo = 0;
                fromNo = CommonUtil.convertObjToInt(objInvestmentsChequeTO.getFromNo());
                toNo = CommonUtil.convertObjToInt(objInvestmentsChequeTO.getToNo());
                for (int j = fromNo; j <= toNo; j++) {
                    objInvestmentsChequeTO.setFromNo(String.valueOf(j));
                    objInvestmentsChequeTO.setStatus("UN_USED");
                    sqlMap.executeUpdate("insertInvestmentsChequeDetails", objInvestmentsChequeTO);
                }
                logTO.setData(objInvestmentsChequeTO.toString());
                logTO.setPrimaryKey(objInvestmentsChequeTO.getKeyData());
                logDAO.addToLog(logTO);
            }
        }
    }

    private void updateChequeDetails() throws Exception {
        if (tableDetails != null || deletedTableValues != null) {
            ArrayList addList = new ArrayList(tableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                InvestmentsChequeTO objInvestmentsChequeTO = (InvestmentsChequeTO) tableDetails.get(addList.get(i));

                if (objInvestmentsChequeTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                    sqlMap.executeUpdate("insertInvestmentsChequeTO", objInvestmentsChequeTO);
                } else {
                    sqlMap.executeUpdate("updateInvestmentsChequeTO", objInvestmentsChequeTO);
                }
                if (i == 0) {
                    sqlMap.executeUpdate("deleteOldInvestmentsDetails", objInvestmentsChequeTO);
                }
                int fromNo = 0;
                int toNo = 0;
                fromNo = CommonUtil.convertObjToInt(objInvestmentsChequeTO.getFromNo());
                toNo = CommonUtil.convertObjToInt(objInvestmentsChequeTO.getToNo());
                for (int j = fromNo; j <= toNo; j++) {
                    objInvestmentsChequeTO.setFromNo(String.valueOf(j));
                    objInvestmentsChequeTO.setStatus("UN_USED");
                    sqlMap.executeUpdate("insertInvestmentsChequeDetails", objInvestmentsChequeTO);
                }
                logTO.setData(objInvestmentsChequeTO.toString());
                logTO.setPrimaryKey(objInvestmentsChequeTO.getKeyData());
                logDAO.addToLog(logTO);
            }
        }
        if (deletedTableValues != null) {
            System.out.println("######## deletedTableValues :" + deletedTableValues);
            ArrayList addList = new ArrayList(deletedTableValues.keySet());
            InvestmentsChequeTO objInvestmentsChequeTO = null;
            for (int i = 0; i < deletedTableValues.size(); i++) {
                objInvestmentsChequeTO = new InvestmentsChequeTO();
                objInvestmentsChequeTO = (InvestmentsChequeTO) deletedTableValues.get(addList.get(i));
                sqlMap.executeUpdate("deleteCheckMasterTO", objInvestmentsChequeTO);
            }
        }
    }

    /**
     * This method is used to Edit the already existing data in the table
     */
    private void updateData(HashMap map) throws Exception {
        if (objInvestmentsOperativeTO != null) {
            logTO.setData(objInvestmentsOperativeTO.toString());
            logTO.setPrimaryKey(objInvestmentsOperativeTO.getKeyData());
            logTO.setStatus(objInvestmentsOperativeTO.getCommand());
            logDAO.addToLog(logTO);
            sqlMap.executeUpdate("updateInvestmentsOperativeTO", objInvestmentsOperativeTO);
            if (map.containsKey("chequeBookDetails") || map.containsKey("deletedChequeBookDetails")) {
                updateChequeDetails();
            }
        } else if (objInvestmentsDepositTO != null) {
            logTO.setData(objInvestmentsDepositTO.toString());
            logTO.setPrimaryKey(objInvestmentsDepositTO.getKeyData());
            logTO.setStatus(objInvestmentsDepositTO.getCommand());
            logDAO.addToLog(logTO);
            if (map.containsKey("RENEWAL")) {
                System.out.println("@@@@@@@@@@ RENEWAL IS OK");
                renewalData(map);
            }
            if (map.containsKey("InvestmentsDepositTO")) {
                objInvestmentsDepositTO = (InvestmentsDepositTO) map.get("InvestmentsDepositTO");
            }
            if (map.containsKey("RENEWAL")) {
                objInvestmentsDepositTO.setRenewalDt(currDt);
            }
            sqlMap.executeUpdate("updateInvestmentsDepositTO", objInvestmentsDepositTO);
            sqlMap.executeUpdate("updateInvestmentsMasterStatus", objInvestmentsDepositTO);
        } else if (objInvestmentsShareTO != null) {
            logTO.setData(objInvestmentsShareTO.toString());
            logTO.setPrimaryKey(objInvestmentsShareTO.getKeyData());
            logTO.setStatus(objInvestmentsShareTO.getCommand());
            logDAO.addToLog(logTO);
            sqlMap.executeUpdate("updateInvestmentsShareTO", objInvestmentsShareTO);
        } else if (objInvestmentsRFTO != null) {
            logTO.setData(objInvestmentsRFTO.toString());
            logTO.setPrimaryKey(objInvestmentsRFTO.getKeyData());
            logTO.setStatus(objInvestmentsRFTO.getCommand());
            logDAO.addToLog(logTO);
            sqlMap.executeUpdate("updateInvestmentsRFTO", objInvestmentsRFTO);
        }
    }

    private void renewalData(HashMap map) throws Exception {
        generateSingleTransId = generateLinkID();
        cashSingleTransId = generateLinkID();
        transferList = new ArrayList();
        //   System.out.println("renewalData loanDataMap : "+loanDataMap);
        System.out.println("######### Renewal Map : " + map);
        //Added by nithya on 04-02-2022 for KD-3287
         String renewWithOutTransaction = "N";
        if(map.containsKey("RENEWAL_WITHOUT_TRANSACTION") && map.get("RENEWAL_WITHOUT_TRANSACTION") != null){
            renewWithOutTransaction = CommonUtil.convertObjToStr(map.get("RENEWAL_WITHOUT_TRANSACTION"));
        }
        //   System.out.println("single" + generateSingleTransId + "" + cashSingleTransId);
        if (map.containsKey("RenewalInvestmentsDepositTO")) {
            InvestmentsDepositTO objInvestmentsDepositTO = new InvestmentsDepositTO();
            objInvestmentsDepositTO = (InvestmentsDepositTO) map.get("RenewalInvestmentsDepositTO");
            sqlMap.executeUpdate("insertInvestmentsRenewalDepositTO", objInvestmentsDepositTO);
            String linkBatch_ID = "";
            double oldPrinciPalAmount = 0.0;
            oldPrinciPalAmount = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue();
            String newPdodId = "";
            String newInvType = "";

            //Principal
            if (map.containsKey("RenewalInvestmentsTransTO")) {
                objInvestmentsTransTO = (InvestmentsTransTO) map.get("RenewalInvestmentsTransTO");
                tdsAmt = CommonUtil.convertObjToDouble(objInvestmentsTransTO.getInvestTDS());
                objInvestmentsTransTO.setInvestTDS(0.0);
                System.out.println("objInvestmentsTransTO  111 : " + objInvestmentsTransTO);
                linkBatch_ID = CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestment_internal_Id());
                newPdodId = CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentID());
                newInvType = CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentBehaves());

                objInvestmentsTransTO.setTrnCode("Deposit");
                //KD-3679 INVESTMENT RENEWAL TRANSACTION
                if(renewWithOutTransaction.equals("Y")){
                    objInvestmentsTransTO.setPrincipalEntryWithoutTransaction("Y");
                }
                objInvestmentsTransTO.setTransType(CommonConstants.DEBIT);
                if (oldPrinciPalAmount > 0) {
                    objInvestmentsTransTO.setInvestmentAmount(CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")));
                    objInvestmentsTransTO.setBrokenPeriodInterest(new Double(0.0));
                    //             System.out.println("loanDataMaploanDataMap renewal ddd"+loanDataMap);
                    renewalDepositTransaction(map);
                    //           System.out.println("loanDataMaploanDataMap renewal last"+loanDataMap);
//                    objInvestmentsTransTO.setBatchID(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
                    objInvestmentsTransTO.setInitiatedBranch(_branchCode);
//                    if (loanDataMap != null) {
                    double debitAmount = 0.0;
                    if (map.containsKey("RENEWAL_INTEREST_TYPE") && (map.get("RENEWAL_INTEREST_TYPE").equals("WITH_INTEREST") || map.get("RENEWAL_INTEREST_TYPE").equals("WITHOUT_INTEREST"))) {
                        if (map.get("RENEWAL_INTEREST_TYPE").equals("WITHOUT_INTEREST")) {
                            debitAmount = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue();
                        } else {
                            debitAmount = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue() + CommonUtil.convertObjToDouble(map.get("RENEWAL_INTEREST_AMOUNT")).doubleValue() - tdsAmt;
                        }
                    } else {
                          //  debitAmount = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue() + CommonUtil.convertObjToDouble(map.get("RENEWAL_INTEREST_AMOUNT")).doubleValue()
                        //          + CommonUtil.convertObjToDouble(map.get("PARTIAL_INTEREST_AMOUNT")).doubleValue()-tdsAmt;
                        // System.out.println("PP AMT====="+map.get("PRINCIPAL_AMOUNT")+" P="+objInvestmentsDepositTO.getInterestReceivable() +"V="+objInvestmentsDepositTO.getRenewalPartialAmt());
                        debitAmount = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue() + CommonUtil.convertObjToDouble(map.get("RENEWAL_INTEREST_AMOUNT")).doubleValue();
                    }
                    objInvestmentsTransTO.setInvestmentAmount(CommonUtil.convertObjToDouble(String.valueOf(debitAmount)));
                    sqlMap.executeUpdate("insertInvestmentTransTO", objInvestmentsTransTO);
                    objInvestmentsTransTOList.add(objInvestmentsTransTO);
                    //Credit
                    objInvestmentsTransTO.setInvestmentAmount(CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")));
                    objInvestmentsTransTO.setTrnCode("Withdrawal");
                    //KD-3679 INVESTMENT RENEWAL TRANSACTION
                    if(renewWithOutTransaction.equals("Y")){
                     objInvestmentsTransTO.setPrincipalEntryWithoutTransaction("Y");
                    }
                    objInvestmentsTransTO.setInvestTDS(0.0);
                    objInvestmentsTransTO.setTransType(CommonConstants.CREDIT);
                    objInvestmentsTransTO.setBrokenPeriodInterest(new Double(0.0));
                    objInvestmentsTransTO.setBrokenPeriodInterest(new Double(0.0));

                    //Insert Withdrawal
                    if (map.containsKey("RENEWAL_NUMBER_TYPE") && map.get("RENEWAL_NUMBER_TYPE").equals("DIFFERENT_PROD_ID")) {
                        objInvestmentsTransTO.setInvestmentID(CommonUtil.convertObjToStr(map.get("OLD_INVESTMENT_ID")));
                        objInvestmentsTransTO.setInvestmentBehaves(CommonUtil.convertObjToStr(map.get("OLD_INVESTMENT_TYPE")));
                        sqlMap.executeUpdate("insertInvestmentTransTO", objInvestmentsTransTO);
                        objInvestmentsTransTOList.add(objInvestmentsTransTO);
                        objInvestmentsTransTO.setInvestmentID(newPdodId);
                        objInvestmentsTransTO.setInvestmentBehaves(newInvType);
                    } else {
                        sqlMap.executeUpdate("insertInvestmentTransTO", objInvestmentsTransTO);
                        objInvestmentsTransTOList.add(objInvestmentsTransTO);
                    }

//                    }
                }
                //Interest
                if (map.containsKey("RENEWAL_INTEREST_AMOUNT")) {
                    objInvestmentsTransTO.setTrnCode("Interest");
                    //KD-3679 INVESTMENT RENEWAL TRANSACTION
                     if(renewWithOutTransaction.equals("Y") && CommonUtil.convertObjToDouble(map.get("RENEWAL_INTEREST_AMOUNT")) > 0){
                     objInvestmentsTransTO.setPrincipalEntryWithoutTransaction("");
                    }
                    objInvestmentsTransTO.setTransType(CommonConstants.CREDIT);
                    objInvestmentsTransTO.setBrokenPeriodInterest(CommonUtil.convertObjToDouble(map.get("RENEWAL_INTEREST_AMOUNT")));
                    objInvestmentsTransTO.setInvestmentAmount(new Double(0.0));
                    renewalInterestTransaction(map);
                    //         System.out.println("tdsAmt ----->"+tdsAmt);
                    objInvestmentsTransTO.setInvestTDS(tdsAmt);
                    //       System.out.println("loanDataMaploanDataMap renewal Interest last"+loanDataMap);
                    if(loanDataMap!=null && loanDataMap.containsKey("TRANS_ID") && loanDataMap.get("TRANS_ID") != null)
                    objInvestmentsTransTO.setBatchID(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
                    objInvestmentsTransTO.setInitiatedBranch(_branchCode);
//                    if (loanDataMap != null) {
                    if (map.containsKey("RENEWAL_NUMBER_TYPE") && map.get("RENEWAL_NUMBER_TYPE").equals("DIFFERENT_PROD_ID")) {
                        objInvestmentsTransTO.setInvestmentID(CommonUtil.convertObjToStr(map.get("OLD_INVESTMENT_ID")));
                        objInvestmentsTransTO.setInvestmentBehaves(CommonUtil.convertObjToStr(map.get("OLD_INVESTMENT_TYPE")));
                        sqlMap.executeUpdate("insertInvestmentTransTO", objInvestmentsTransTO);
                        objInvestmentsTransTOList.add(objInvestmentsTransTO);
                        objInvestmentsTransTO.setInvestmentID(newPdodId);
                        objInvestmentsTransTO.setInvestmentBehaves(newInvType);
                    } else {
                        sqlMap.executeUpdate("insertInvestmentTransTO", objInvestmentsTransTO);
                        objInvestmentsTransTOList.add(objInvestmentsTransTO);
                    }
//                    }
//                        HashMap investTrans = new HashMap();
//                        investTrans.put("batch_id", objInvestmentsTransTO.getBatchID());
//                        investTrans.put("INVESTMENT_ID", objInvestmentsTransTO.getInvestment_internal_Id());
//                        investTrans.put("INVESTMENT_REF_NO", objInvestmentsTransTO.getInvestment_Ref_No());
//                        investTrans.put("TRANS_DT", currDt);
//                        System.out.println("REMAPSSSSSS"+reMap.size());
//                        for (int i = 0; i < reMap.size(); i++) {
//                        sqlMap.executeUpdate("updateInvestTransDetails",investTrans);
//                        }
//                       InvestmentsTransTO s =  (InvestmentsTransTO)reMap.get("Interest");
//                       sqlMap.executeUpdate("insertInvestmentTransTO", s);
//                       InvestmentsTransTO w =  (InvestmentsTransTO)reMap.get("Withdrawal");
//                       sqlMap.executeUpdate("insertInvestmentTransTO", w);
//                       InvestmentsTransTO d =  (InvestmentsTransTO)reMap.get("Deposit");
//                       sqlMap.executeUpdate("insertInvestmentTransTO", d);
//                        System.out.println("REEEMAP"+reMap);

                }
                //Partial Interest
                if (map.containsKey("RENEWAL_INTEREST_TYPE") && map.get("RENEWAL_INTEREST_TYPE").equals("PARTIAL_INTEREST")) {
                    double partialInt = 0.0;
                    System.out.println("else updateInvestMasterStatusClosed111");
                    partialInt = CommonUtil.convertObjToDouble(map.get("PARTIAL_INTEREST_AMOUNT")).doubleValue();
                    System.out.println("partialInt updateInvestMasterStatusClosed111" + partialInt);
                    if (partialInt > 0) {
                        objInvestmentsTransTO.setTrnCode("Interest");
                        //KD-3679 INVESTMENT RENEWAL TRANSACTION
                        if (renewWithOutTransaction.equals("Y") && partialInt > 0) {
                            objInvestmentsTransTO.setPrincipalEntryWithoutTransaction("");
                        }
                        objInvestmentsTransTO.setTransType(CommonConstants.CREDIT);
                        objInvestmentsTransTO.setBrokenPeriodInterest(new Double(partialInt));
                        objInvestmentsTransTO.setInvestmentAmount(new Double(0.0));
                        partialInterestTransaction(map);
                        objInvestmentsTransTO.setInvestTDS(0.0);
                        objInvestmentsTransTO.setBatchID(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
                        objInvestmentsTransTO.setInitiatedBranch(_branchCode);
                        if (loanDataMap != null) {
                            if (map.containsKey("RENEWAL_NUMBER_TYPE") && map.get("RENEWAL_NUMBER_TYPE").equals("DIFFERENT_PROD_ID")) {
                                objInvestmentsTransTO.setInvestmentID(CommonUtil.convertObjToStr(map.get("OLD_INVESTMENT_ID")));
                                objInvestmentsTransTO.setInvestmentBehaves(CommonUtil.convertObjToStr(map.get("OLD_INVESTMENT_TYPE")));
                               // sqlMap.executeUpdate("updateInvestMasterStatusClosed", objInvestmentsTransTO); // Commented by nithya on 12-03-2020 for KD-1643
                                objInvestmentsTransTOList.add(objInvestmentsTransTO);
                                objInvestmentsTransTO.setInvestmentID(newPdodId);
                                objInvestmentsTransTO.setInvestmentBehaves(newInvType);
                                System.out.println("inside updateInvestMasterStatusClosed" + objInvestmentsTransTO);
                            } else {
                              //  sqlMap.executeUpdate("updateInvestMasterStatusClosed", objInvestmentsTransTO);// Commented by nithya on 12-03-2020 for KD-1643
                                objInvestmentsTransTOList.add(objInvestmentsTransTO);
                                System.out.println("else updateInvestMasterStatusClosed" + objInvestmentsTransTO);
                            }
                        }
                    }
                }
                HashMap investTrans = new HashMap();
                if (loanDataMap != null && loanDataMap.get("TRANS_ID") != null) {
                    investTrans.put("batch_id", CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
                }
                investTrans.put("INVESTMENT_ID", objInvestmentsTransTO.getInvestment_internal_Id());
                investTrans.put("INVESTMENT_REF_NO", objInvestmentsTransTO.getInvestment_Ref_No());
                investTrans.put("TRANS_DT", map.get("TRANS_DATE"));
                System.out.println("partial REMAPSSSSSS" + reMap.size());
                //  for (int i = 0; i < 3; i++) {
                sqlMap.executeUpdate("updateInvestTransDetails", investTrans);
                // }
                if (map.containsKey("RENEWAL_WITH_NEW_NUMBER") && map.get("RENEWAL_WITH_NEW_NUMBER") != null) {
                    if (CommonUtil.convertObjToStr(map.get("RENEWAL_WITH_NEW_NUMBER")).equals("RENEWAL_WITH_NEW_NUMBER")) {
                        List lists = sqlMap.executeQueryForList("getInvestTransDetails", investTrans);
                        System.out.println("liststs" + lists);
                        if (lists.size() > 0 && lists != null) {
                            for (int i = 0; i < lists.size(); i++) {
                                HashMap invMap = (HashMap) lists.get(i);
                                System.out.println("partial invMap" + invMap.size());
                                if (invMap.size() > 0 && invMap != null) {
                                    if (CommonUtil.convertObjToStr(invMap.get("TRANS_TYPE")).contains(CommonConstants.CREDIT)) {
                                        if (map.containsKey("OLD_REF_ID") && map.get("OLD_REF_ID") != null) {
                                            investTrans.put("INVESTMENT_REF_NO", CommonUtil.convertObjToStr(map.get("OLD_REF_ID")));
                                            sqlMap.executeUpdate("updateInvestmentID", investTrans);
                                            //sqlMap.executeUpdate("updateInvestMasterStatusClosed", investTrans);// Commented by nithya on 12-03-2020 for KD-1643
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
                getRenewalTransDetails(linkBatch_ID);
            }
            //Added by nithya on 04-02-2022 for KD-3287
            if(renewWithOutTransaction.equalsIgnoreCase("Y")){
                returnDataMap.put("RENEWAL_COMPLETED","RENEWAL_COMPLETED");
            }
        }
    }

    private HashMap partialInterestTransaction(HashMap map) throws Exception {
        System.out.println("In Side The DoTransaction Renewal partial Interest");
//        System.out.println("############# objInvestmentsTransTO For Interest : " + objInvestmentsTransTO);
        HashMap achdMap = new HashMap();
        String investMentType = "";
        investMentType = CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentBehaves());
        achdMap.put("INVESTMENT_TYPE", CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentBehaves()));
        achdMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentID()));
        //System.out.println("achdMap before query ------------>" + achdMap);
        List achdLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", achdMap);
        if (achdLst != null && achdLst.size() > 0) {
            achdMap = new HashMap();
            //ArrayList transferList = new ArrayList();
            ArrayList cashList = new ArrayList();
            achdMap = (HashMap) achdLst.get(0);
            //  System.out.println("achdMap--------------->" + achdMap);
            TransferTrans objTransferTrans = new TransferTrans();
            objTransferTrans.setInitiatedBranch(_branchCode);
            objTransferTrans.setLinkBatchId(objInvestmentsTransTO.getInvestmentName());
            HashMap txMap = new HashMap();
            if (map.containsKey("TransactionTO")) {
                if (!objInvestmentsTransTO.getTrnCode().equals("") && objInvestmentsTransTO.getTrnCode().equals("Interest")) {
                    LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) { //FD  Only
                        LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                        for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                            double tranAmt = 0.0;
                            objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                            if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                //                    System.out.println("eee----" + objInvestmentsTransTO.getInvestment_Ref_No());
                                txMap.put(TransferTrans.PARTICULARS, " Partial_Interest " + objInvestmentsTransTO.getInvestment_Ref_No() + " From " + getDateFor(objInvestmentsDepositTO.getIntRecTillDt()) + " To " + getDateFor(objInvestmentsDepositTO.getMaturityDt()));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put("AUTHORIZEREMARKS", "TRANSACTIONAMOUNT");
                                txMap.put("generateSingleTransId", generateSingleTransId);
                                txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
                                txMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                if (CommonUtil.convertObjToStr(objTransactionTO.getProductType()).equals("GL")) {
                                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                }
                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                tranAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue() - tdsAmt;
                                if (tranAmt > 0) {
                                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, tranAmt));
                                }

                                if (objInvestmentsTransTO.getBrokenPeriodInterest().doubleValue() != 0.0) {
                                    if (map.containsKey("RENEWAL_NUMBER_TYPE") && map.get("RENEWAL_NUMBER_TYPE").equals("DIFFERENT_PROD_ID")) {
                                        HashMap oldDepositHeadMap = new HashMap();
                                        oldDepositHeadMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(map.get("OLD_INVESTMENT_ID")));
                                        List oldDepositLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", oldDepositHeadMap);
                                        if (oldDepositLst != null && oldDepositLst.size() > 0) {
                                            oldDepositHeadMap = (HashMap) oldDepositLst.get(0);
                                            txMap.put(TransferTrans.CR_AC_HD, (String) oldDepositHeadMap.get("INTEREST_RECIVED_AC_HD"));
                                        }
                                    } else {
                                        txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("INTEREST_RECIVED_AC_HD"));
                                    }
                                    txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_INTEREST");
                                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put("generateSingleTransId", generateSingleTransId);
                                    // System.out.println("rrr----" + objInvestmentsTransTO.getInvestment_Ref_No());
                                    txMap.put(TransferTrans.PARTICULARS, " Partial_Interest " + objInvestmentsTransTO.getInvestment_Ref_No() + " From " + getDateFor(objInvestmentsDepositTO.getIntRecTillDt()) + " To " + getDateFor(objInvestmentsDepositTO.getMaturityDt()));
                                    txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
                                    double newdbAmt = CommonUtil.convertObjToDouble(objInvestmentsDepositTO.getInterestReceivable());
                                    txMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, newdbAmt));
//                                    System.out.println("transferList INTEREST_PAID_AC_HD 11111------------->" + transferList +"newdbAmt-->"+newdbAmt +" BR AMT-->"+
//                                            objInvestmentsTransTO.getBrokenPeriodInterest());
                                }
                                double investTDS = CommonUtil.convertObjToDouble(tdsAmt);
                                if (investTDS > 0) {
                                    txMap = new HashMap();
                   //     txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                    //     txMap.put(TransferTrans.PARTICULARS, " TDS " + objInvestmentsTransTO.getInvestment_Ref_No() + " From " + getDateFor(objInvestmentsDepositTO.getIntRecTillDt()) + " To " + getDateFor(objInvestmentsDepositTO.getMaturityDt()));
                                    //    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    //    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                    //    txMap.put(TransferTrans.DR_BRANCH,_branchCode );
                                    //     txMap.put("AUTHORIZEREMARKS", "TDSAMOUNT");
                                    //      txMap.put("generateSingleTransId", generateSingleTransId);
                                    //    txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
                                    //    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(achdMap.get("INTEREST_RECIVED_AC_HD")));
                                    //    txMap.put("TRANS_MOD_TYPE", "GL");
                                    //    transferList.add(objTransferTrans.getDebitTransferTO(txMap, investTDS));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_AC_HD, (String) achdMap.get("TDS_AC_HD"));
                                    txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_TDS");
                                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, " TDS " + objInvestmentsTransTO.getInvestment_Ref_No() + " From " + getDateFor(objInvestmentsDepositTO.getIntRecTillDt()) + " To " + getDateFor(objInvestmentsDepositTO.getMaturityDt()));
                                    txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
                                    txMap.put("generateSingleTransId", generateSingleTransId);
                                    txMap.put("TRANS_MOD_TYPE", "INV");
                                    txMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, investTDS));
                        //System.out.println("transferList TDS--5555555555555--55555-------->" + transferList);

                                }

                                //   System.out.println("transferList total ------------->" + transferList);
                            } else {
                                HashMap investmentTransMap = new HashMap();
                                investmentTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                                investmentTransMap.put("PARTICULARS", objInvestmentsTransTO.getInvestment_Ref_No());
                                investmentTransMap.put("BRANCH_CODE", _branchCode);
                                investmentTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                investmentTransMap.put("USER_ID", objInvestmentsTransTO.getStatusBy());
                                investmentTransMap.put("INVESTMENT_NO", objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName());
                                //  System.out.println("tPPPPPPPPPPPPP----------->" + objInvestmentsTransTO.getBrokenPeriodInterest());
                                if (objInvestmentsTransTO.getBrokenPeriodInterest().doubleValue() != 0.0) {
                                    investmentTransMap.put("ACCT_HEAD", achdMap.get("INTEREST_RECIVED_AC_HD"));
                                    investmentTransMap.put("TRANS_AMOUNT", objInvestmentsTransTO.getBrokenPeriodInterest() - tdsAmt);
                                    investmentTransMap.put("AUTHORIZEREMARKS", "IINVESTMENT_INTEREST");
                                    investmentTransMap.put("INT_TYPE", "Partial_Interest");
                                    investmentTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                    CashTransactionTO cashObj = createCashTransactionTO(investmentTransMap);
                                    cashList.add(cashObj);
                                    if (cashObj != null && cashObj.getSingleTransId() != null) {
                                        cashSingleTransId = CommonUtil.convertObjToStr(cashObj.getSingleTransId());
                                        pickSingleTransId.put("SINGLE_CASH_TRANSID", cashSingleTransId);
                                    }
                                }
                                //TDS
                                double newdbAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToDouble(map.get("RENEWAL_INTEREST_AMOUNT")).doubleValue());
                                if (newdbAmt > 0) {
                                    txMap = new HashMap();
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, " TDS " + objInvestmentsTransTO.getInvestment_Ref_No() + " From " + getDateFor(objInvestmentsDepositTO.getIntRecTillDt()) + " To " + getDateFor(objInvestmentsDepositTO.getMaturityDt()));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    //txMap.put(TransferTrans.CR_INST_TYPE, "VOUCHER");
                                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                    txMap.put("AUTHORIZEREMARKS", "TDSAMOUNT");
                                    txMap.put("generateSingleTransId", generateSingleTransId);
                                    txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(achdMap.get("INTEREST_RECIVED_AC_HD")));
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, newdbAmt));
                                }
                                double investTDS = CommonUtil.convertObjToDouble(tdsAmt);
                                if (investTDS > 0) {
                                    txMap = new HashMap();
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, " TDS " + objInvestmentsTransTO.getInvestment_Ref_No() + " From " + getDateFor(objInvestmentsDepositTO.getIntRecTillDt()) + " To " + getDateFor(objInvestmentsDepositTO.getMaturityDt()));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    //txMap.put(TransferTrans.CR_INST_TYPE, "VOUCHER");
                                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                    txMap.put("AUTHORIZEREMARKS", "TDSAMOUNT");
                                    txMap.put("generateSingleTransId", generateSingleTransId);
                                    txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(achdMap.get("INTEREST_RECIVED_AC_HD")));
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, investTDS));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_AC_HD, (String) achdMap.get("TDS_AC_HD"));
                                    txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_TDS");
                                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, " TDS " + objInvestmentsTransTO.getInvestment_Ref_No() + " From " + getDateFor(objInvestmentsDepositTO.getIntRecTillDt()) + " To " + getDateFor(objInvestmentsDepositTO.getMaturityDt()));
                                    txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
                                    txMap.put("generateSingleTransId", generateSingleTransId);
                                    txMap.put("TRANS_MOD_TYPE", "INV");
                                    txMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, investTDS));
                        //System.out.println("transferList TDS------------>" + transferList);

                                }
                                //      System.out.print("###### cashList:111111111111111111 " + cashList);
                            }
                            if (cashList != null && cashList.size() > 0) {
                                doCashTrans(cashList, _branchCode, false);
                            }
                            if (transferList != null && transferList.size() > 0) {
                                doDebitCredit(transferList, _branchCode, false);
                            }
                            transferList = null;
                            cashList = null;
                        }
                    }
                }
            }
        } else {
            throw new Exception("investment  AccountHead is Not set...");
        }
        return null;
    }

    private HashMap renewalInterestTransaction(HashMap map) throws Exception {
        System.out.println("In Side The DoTransaction Renewal Interest");
        //Added by nithya on 04-02-2022 for KD-3287
        String renewWithOutTransaction = "N";
        if(map.containsKey("RENEWAL_WITHOUT_TRANSACTION") && map.get("RENEWAL_WITHOUT_TRANSACTION") != null){
            renewWithOutTransaction = CommonUtil.convertObjToStr(map.get("RENEWAL_WITHOUT_TRANSACTION"));
        }
        // System.out.println("############# objInvestmentsTransTO For Interest : " + objInvestmentsTransTO);
        HashMap achdMap = new HashMap();
        String investMentType = "";
        investMentType = CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentBehaves());
        achdMap.put("INVESTMENT_TYPE", CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentBehaves()));
        achdMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentID()));
        //  System.out.println("achdMap before query ------------>" + achdMap);
        List achdLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", achdMap);
        if (achdLst != null && achdLst.size() > 0) {
            achdMap = new HashMap();
//            ArrayList transferList = new ArrayList();
            ArrayList cashList = new ArrayList();
            achdMap = (HashMap) achdLst.get(0);
            //  System.out.println("achdMap--------------->" + achdMap);
            TransferTrans objTransferTrans = new TransferTrans();
            objTransferTrans.setInitiatedBranch(_branchCode);
            objTransferTrans.setLinkBatchId(objInvestmentsTransTO.getInvestmentName());
            HashMap txMap = new HashMap();
            if (map.containsKey("TransactionTO") && !(map.containsKey("RENEWAL_INTEREST_TYPE") && map.get("RENEWAL_INTEREST_TYPE").equals("PARTIAL_INTEREST"))) {
                //        System.out.println("################ renewalInterestTransaction  Only INTEREST Amt Start");
                if (!objInvestmentsTransTO.getTrnCode().equals("") && objInvestmentsTransTO.getTrnCode().equals("Interest")) {
                    LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) { //FD  Only
                        LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                        for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                            double tranAmt = 0.0;
                            objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                            //                System.out.println("objTransactionTO.getTransType() ------->"+objTransactionTO.getTransType());
                            if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                System.out.println("111----" + objInvestmentsTransTO.getInvestment_Ref_No());
                                //   txMap.put(TransferTrans.PARTICULARS,objInvestmentsTransTO.getInvestment_Ref_No()+" "+ objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName()+ " - " +"Interest");
                                txMap.put(TransferTrans.PARTICULARS, " Interest on" + objInvestmentsTransTO.getInvestment_Ref_No() + " From " + getDateFor(objInvestmentsDepositTO.getIntRecTillDt()) + " To " + getDateFor(objInvestmentsDepositTO.getMaturityDt()));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put("AUTHORIZEREMARKS", "TRANSACTIONAMOUNT");
                                txMap.put("generateSingleTransId", generateSingleTransId);
                                txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
                                if (CommonUtil.convertObjToStr(objTransactionTO.getProductType()).equals("GL")) {
                                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                }
                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                tranAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                if (objTransactionTO.getProductType().equals("OA")) {
                                    txMap.put("TRANS_MOD_TYPE", "OA");
                                } else if (objTransactionTO.getProductType().equals("AB")) {
                                    txMap.put("TRANS_MOD_TYPE", "AB");
                                } else if (objTransactionTO.getProductType().equals("SA")) {
                                    txMap.put("TRANS_MOD_TYPE", "SA");
                                } else if (objTransactionTO.getProductType().equals("TL")) {
                                    txMap.put("TRANS_MOD_TYPE", "TL");
                                } else if (objTransactionTO.getProductType().equals("AD")) {
                                    txMap.put("TRANS_MOD_TYPE", "AD");
                                } else {
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                }
                                txMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                if (tranAmt > 0) {
                                    if (map.get("RENEWAL_INTEREST_TYPE").equals("WITHOUT_INTEREST")) {
                                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, tranAmt - tdsAmt));
                                    } else {
                                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, tranAmt));
                                    }
                                }
                                //                  System.out.println("########## renewalInterestTransaction  DIFFERENT_PROD_ID: " + map.get("RENEWAL_NUMBER_TYPE"));
                                if (objInvestmentsTransTO.getBrokenPeriodInterest().doubleValue() != 0.0) {
                                    if (map.containsKey("RENEWAL_NUMBER_TYPE") && map.get("RENEWAL_NUMBER_TYPE").equals("DIFFERENT_PROD_ID")) {
                                        //System.out.println("########## renewalInterestTransaction  DIFFERENT_PROD_ID: " + map.get("RENEWAL_NUMBER_TYPE"));
                                        HashMap oldDepositHeadMap = new HashMap();
                                        oldDepositHeadMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(map.get("OLD_INVESTMENT_ID")));
                                        List oldDepositLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", oldDepositHeadMap);
                                        if (oldDepositLst != null && oldDepositLst.size() > 0) {
                                            oldDepositHeadMap = (HashMap) oldDepositLst.get(0);
                                            txMap.put(TransferTrans.CR_AC_HD, (String) oldDepositHeadMap.get("INTEREST_RECIVED_AC_HD"));
                                        }
                                    } else {
                                        txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("INTEREST_RECIVED_AC_HD"));
                                    }
                                    txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_INTEREST");
                                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    //  System.out.println("222----" + objInvestmentsTransTO.getInvestment_Ref_No());
                                    // txMap.put(TransferTrans.PARTICULARS,objInvestmentsTransTO.getInvestment_Ref_No()+" "+ objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName()+ " - " +"Interest");
                                    txMap.put(TransferTrans.PARTICULARS, " Interest on " + objInvestmentsTransTO.getInvestment_Ref_No() + " From " + getDateFor(objInvestmentsDepositTO.getIntRecTillDt()) + " To " + getDateFor(objInvestmentsDepositTO.getMaturityDt()));
                                    txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
                                    txMap.put("generateSingleTransId", generateSingleTransId);
                                    txMap.put("TRANS_MOD_TYPE", "INV");
                                    txMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
//                                     System.out.println("objInvestmentsTransTO.getBrokenPeriodInterest()---------->" + 
//                                             objInvestmentsTransTO.getBrokenPeriodInterest() +"tdsAmt--------->"+tdsAmt);
                                    double newdbAmt = CommonUtil.convertObjToDouble(objInvestmentsTransTO.getBrokenPeriodInterest());
                                    if (map.get("RENEWAL_INTEREST_TYPE").equals("WITHOUT_INTEREST")) {
                                        newdbAmt = CommonUtil.convertObjToDouble(objInvestmentsTransTO.getBrokenPeriodInterest());
                                    }

                                    //System.out.println("newdbAmt ------>" + newdbAmt);
                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, newdbAmt));

                                    //System.out.println("transferList INTEREST_PAID_AC_HD ------------->" + transferList);
                                }
                                //This part for TDS Investment 
                                double investTDS = CommonUtil.convertObjToDouble(tdsAmt);
                                if (investTDS > 0) {
                                    txMap = new HashMap();
                   //     txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                    //     txMap.put(TransferTrans.PARTICULARS, " TDS " + objInvestmentsTransTO.getInvestment_Ref_No() + " From " + getDateFor(objInvestmentsDepositTO.getIntRecTillDt()) + " To " + getDateFor(objInvestmentsDepositTO.getMaturityDt()));
                                    //    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    //    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                    //    txMap.put(TransferTrans.DR_BRANCH,_branchCode );
                                    //     txMap.put("AUTHORIZEREMARKS", "TDSAMOUNT");
                                    //      txMap.put("generateSingleTransId", generateSingleTransId);
                                    //    txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
                                    //    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(achdMap.get("INTEREST_RECIVED_AC_HD")));
                                    //    txMap.put("TRANS_MOD_TYPE", "GL");
                                    //    transferList.add(objTransferTrans.getDebitTransferTO(txMap, investTDS));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_AC_HD, (String) achdMap.get("TDS_AC_HD"));
                                    txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_TDS");
                                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, " TDS " + objInvestmentsTransTO.getInvestment_Ref_No() + " From " + getDateFor(objInvestmentsDepositTO.getIntRecTillDt()) + " To " + getDateFor(objInvestmentsDepositTO.getMaturityDt()));
                                    txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
                                    txMap.put("generateSingleTransId", generateSingleTransId);
                                    txMap.put("TRANS_MOD_TYPE", "INV");
                                    txMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, investTDS));
                       // System.out.println("transferList TDS------------>" + transferList);

                                }
                //end TDS Investment
                                //       System.out.println("transferList total ------------->" + transferList);
                            } else {
                                System.out.println("IN CASH ---------->");
                                HashMap investmentTransMap = new HashMap();
                                investmentTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                                investmentTransMap.put("PARTICULARS", objInvestmentsTransTO.getInvestment_Ref_No());
                                investmentTransMap.put("BRANCH_CODE", _branchCode);
                                investmentTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                investmentTransMap.put("USER_ID", objInvestmentsTransTO.getStatusBy());
                                investmentTransMap.put("INVESTMENT_NO", objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName());
                                //  System.out.println("VVVVVVVVVVVV--------->"+objInvestmentsTransTO.getBrokenPeriodInterest()); 
                                if (objInvestmentsTransTO.getBrokenPeriodInterest().doubleValue() != 0.0) {
                                    investmentTransMap.put("ACCT_HEAD", achdMap.get("INTEREST_RECIVED_AC_HD"));
                                    investmentTransMap.put("TRANS_AMOUNT", objInvestmentsTransTO.getBrokenPeriodInterest() - CommonUtil.convertObjToDouble(tdsAmt));
                                    investmentTransMap.put("AUTHORIZEREMARKS", "IINVESTMENT_INTEREST");
                                    investmentTransMap.put("INT_TYPE", "Interest");
                                    investmentTransMap.put("TRANS_MOD_TYPE", "INV");
                                    investmentTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                    CashTransactionTO cashObj = createCashTransactionTO(investmentTransMap);
                                    cashList.add(cashObj);
                                    if (cashObj != null && cashObj.getSingleTransId() != null) {
                                        cashSingleTransId = CommonUtil.convertObjToStr(cashObj.getSingleTransId());
                                        pickSingleTransId.put("SINGLE_CASH_TRANSID", cashSingleTransId);
                                    }
                                }
                                //TDS
                                double investTDS = CommonUtil.convertObjToDouble(tdsAmt);
                                if (investTDS > 0) {
                                    txMap = new HashMap();
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, " TDS " + objInvestmentsTransTO.getInvestment_Ref_No() + " From " + getDateFor(objInvestmentsDepositTO.getIntRecTillDt()) + " To " + getDateFor(objInvestmentsDepositTO.getMaturityDt()));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    //txMap.put(TransferTrans.CR_INST_TYPE, "VOUCHER");
                                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                    txMap.put("AUTHORIZEREMARKS", "TDSAMOUNT");
                                    txMap.put("generateSingleTransId", generateSingleTransId);
                                    txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(achdMap.get("INTEREST_RECIVED_AC_HD")));
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, investTDS));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_AC_HD, (String) achdMap.get("TDS_AC_HD"));
                                    txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_TDS");
                                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, " TDS " + objInvestmentsTransTO.getInvestment_Ref_No() + " From " + getDateFor(objInvestmentsDepositTO.getIntRecTillDt()) + " To " + getDateFor(objInvestmentsDepositTO.getMaturityDt()));
                                    txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
                                    txMap.put("generateSingleTransId", generateSingleTransId);
                                    txMap.put("TRANS_MOD_TYPE", "INV");
                                    txMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, investTDS));
                                    //System.out.println("transferList TDS------------>" + transferList);
                                }
                                //END
                                //System.out.print("###### cashList: " + cashList);
                            }
                            if (cashList != null && cashList.size() > 0) {
                                //   doCashTrans(cashList, _branchCode, false);
                            }
                            if (transferList != null && transferList.size() > 0) {
                                // doDebitCredit(transferList, _branchCode, false);
                            }
                            //System.out.println("pickSingleTransId" + pickSingleTransId);
                            //transferList = null;
                            // cashList = null;
                        }
                    }
                }//(map.get("RENEWAL_INTEREST_TYPE").equals("WITH_INTEREST") || map.get("RENEWAL_INTEREST_TYPE").equals("WITHOUT_INTEREST")
            } else if (map.containsKey("RENEWAL_INTEREST_TYPE") && (map.get("RENEWAL_INTEREST_TYPE").equals("WITH_INTEREST"))) {//want to edit 
                //  System.out.println("################ renewalInterestTransaction  PARTIAL INTEREST Amt Start");
                if (!objInvestmentsTransTO.getTrnCode().equals("") && objInvestmentsTransTO.getTrnCode().equals("Interest")) {
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
//                    System.out.println("333----" + objInvestmentsTransTO.getInvestment_Ref_No());
//                    System.out.println("Ntxt" + objInvestmentsTransTO);
//                    System.out.println("get tr----" + objInvestmentsTransTO.getTransType());
//                    System.out.println("SB or CA" + objInvestmentsTransTO.getTxtInvestmentIDTransSBorCA());
                    // txMap.put(TransferTrans.PARTICULARS,objInvestmentsTransTO.getInvestment_Ref_No()+" "+ objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName()+ " - " +"Interest");
                    txMap.put(TransferTrans.PARTICULARS, " Interest on" + objInvestmentsTransTO.getInvestment_Ref_No() + " From " + getDateFor(objInvestmentsDepositTO.getIntRecTillDt()) + " To " + getDateFor(objInvestmentsDepositTO.getMaturityDt()));
                    txMap.put("AUTHORIZEREMARKS", "INV_RENEWAL_INTEREST");
                    txMap.put(TransferTrans.DR_AC_HD, achdMap.get("IINVESTMENT_AC_HD"));
                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                    txMap.put("generateSingleTransId", generateSingleTransId);
                    txMap.put("TRANS_MOD_TYPE", "INV");
                    txMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                    txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());//KD-4023
                    //  System.out.println("objInvestmentTrans...."+reMap.get("DEP_AMOUNT"));
                    Double totAmount = CommonUtil.convertObjToDouble(reMap.get("DEP_AMOUNT")) + CommonUtil.convertObjToDouble(objInvestmentsTransTO.getBrokenPeriodInterest());
                    //Added by nithya on 04-02-2022 for KD-3287
                    if(renewWithOutTransaction.equals("Y")){
                       totAmount =  CommonUtil.convertObjToDouble(objInvestmentsTransTO.getBrokenPeriodInterest());
                       if(totAmount > 0){
                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, totAmount));
                       }
                    }else{
                       transferList.add(objTransferTrans.getDebitTransferTO(txMap, totAmount));
                    }
                    // System.out.println("transferList ------------->" + transferList);

                    if (map.containsKey("RENEWAL_NUMBER_TYPE") && map.get("RENEWAL_NUMBER_TYPE").equals("DIFFERENT_PROD_ID")) {
                        HashMap oldDepositHeadMap = new HashMap();
                        oldDepositHeadMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(map.get("OLD_INVESTMENT_ID")));
                        List oldDepositLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", oldDepositHeadMap);
                        if (oldDepositLst != null && oldDepositLst.size() > 0) {
                            oldDepositHeadMap = (HashMap) oldDepositLst.get(0);
                            txMap.put(TransferTrans.CR_AC_HD, (String) oldDepositHeadMap.get("INTEREST_RECIVED_AC_HD"));
                        }
                    } else {
                        txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("INTEREST_RECIVED_AC_HD"));
                    }
                    txMap.put("AUTHORIZEREMARKS", "INV_RENEWAL_INTEREST");
                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put("generateSingleTransId", generateSingleTransId);
                    //  System.out.println("444----" + objInvestmentsTransTO.getInvestment_Ref_No());
                    //txMap.put(TransferTrans.PARTICULARS,objInvestmentsTransTO.getInvestment_Ref_No()+" "+ objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName()+ " - " +"Interest");
                    txMap.put(TransferTrans.PARTICULARS, " Interest on" + objInvestmentsTransTO.getInvestment_Ref_No() + " From " + getDateFor(objInvestmentsDepositTO.getIntRecTillDt()) + " To " + getDateFor(objInvestmentsDepositTO.getMaturityDt()));
                    if (map.containsKey("OLD_REF_ID") && map.get("OLD_REF_ID") != null) {
                        txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(map.get("OLD_REF_ID")) + " " + objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName() + " - " + "Deposit");
                    }
                    txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
                    txMap.put("TRANS_MOD_TYPE", "INV");
                    txMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                    //Added by nithya on 04-02-2022 for KD-3287
                    if(renewWithOutTransaction.equals("Y")){                        
                        if(objInvestmentsTransTO.getBrokenPeriodInterest().doubleValue() > 0){
                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, objInvestmentsTransTO.getBrokenPeriodInterest().doubleValue()));
                        }
                    }else{                        
                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, objInvestmentsTransTO.getBrokenPeriodInterest().doubleValue()));
                    }
//                    System.out.println("transferList  ------------->" + transferList);
//                    System.out.println("transferList total ------------->" + transferList);
                    if (transferList != null && transferList.size() > 0) {
                        //doDebitCredit(transferList, _branchCode, false);
                    }
                    //This part for TDS Investment 
                    double investTDS = CommonUtil.convertObjToDouble(tdsAmt);
                    if (investTDS > 0) {
                        txMap = new HashMap();
                   //     txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        //     txMap.put(TransferTrans.PARTICULARS, " TDS " + objInvestmentsTransTO.getInvestment_Ref_No() + " From " + getDateFor(objInvestmentsDepositTO.getIntRecTillDt()) + " To " + getDateFor(objInvestmentsDepositTO.getMaturityDt()));
                        //    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        //    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                        //    txMap.put(TransferTrans.DR_BRANCH,_branchCode );
                        //     txMap.put("AUTHORIZEREMARKS", "TDSAMOUNT");
                        //      txMap.put("generateSingleTransId", generateSingleTransId);
                        //    txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
                        //    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(achdMap.get("INTEREST_RECIVED_AC_HD")));
                        //    txMap.put("TRANS_MOD_TYPE", "GL");
                        //    transferList.add(objTransferTrans.getDebitTransferTO(txMap, investTDS));
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put(TransferTrans.DR_AC_HD, (String) achdMap.get("TDS_AC_HD"));
                        txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_TDS");
                        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.PARTICULARS, " TDS " + objInvestmentsTransTO.getInvestment_Ref_No() + " From " + getDateFor(objInvestmentsDepositTO.getIntRecTillDt()) + " To " + getDateFor(objInvestmentsDepositTO.getMaturityDt()));
                        txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
                        txMap.put("generateSingleTransId", generateSingleTransId);
                        txMap.put("TRANS_MOD_TYPE", "INV");
                        txMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, investTDS));
                    //    System.out.println("transferList TDS------------>" + transferList);

                    }
                    //end TDS Investment
                    if (cashList != null && cashList.size() > 0) {
                        doCashTrans(cashList, _branchCode, false);
                    }
//                    System.out.println("Nidhin LOAN MAP" + loanDataMap);
//                    System.out.println("pickSingleTransId partial" + pickSingleTransId);
//                    transferList = null;
                    // cashList = null;

                }
            } else if (map.containsKey("RENEWAL_INTEREST_TYPE") && (map.get("RENEWAL_INTEREST_TYPE").equals("WITHOUT_INTEREST"))) {
                HashMap investmentTransMap = new HashMap();
                investmentTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                investmentTransMap.put("PARTICULARS", objInvestmentsTransTO.getInvestment_Ref_No());
                investmentTransMap.put("BRANCH_CODE", _branchCode);
                investmentTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                investmentTransMap.put("USER_ID", objInvestmentsTransTO.getStatusBy());
                investmentTransMap.put("INVESTMENT_NO", objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName());
                // System.out.println("BR CASH -->"+objInvestmentsTransTO.getBrokenPeriodInterest() +"tdsAmt--->"+tdsAmt);
                if (objInvestmentsTransTO.getBrokenPeriodInterest().doubleValue() != 0.0) {
                    investmentTransMap.put("ACCT_HEAD", achdMap.get("INTEREST_RECIVED_AC_HD"));
                    investmentTransMap.put("TRANS_AMOUNT", objInvestmentsTransTO.getBrokenPeriodInterest());
                    investmentTransMap.put("AUTHORIZEREMARKS", "IINVESTMENT_INTEREST");
                    investmentTransMap.put("INT_TYPE", "Interest");
                    investmentTransMap.put("TRANS_MOD_TYPE", "INV");
                    investmentTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                    CashTransactionTO cashObj = createCashTransactionTO(investmentTransMap);
                    cashList.add(cashObj);
                    if (cashObj != null && cashObj.getSingleTransId() != null) {
                        cashSingleTransId = CommonUtil.convertObjToStr(cashObj.getSingleTransId());
                        pickSingleTransId.put("SINGLE_CASH_TRANSID", cashSingleTransId);
                    }
                }
                //TDS
                double investTDS = CommonUtil.convertObjToDouble(tdsAmt);
                if (investTDS > 0) {
                    investmentTransMap = new HashMap();
                    investmentTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                    investmentTransMap.put("PARTICULARS", objInvestmentsTransTO.getInvestment_Ref_No());
                    investmentTransMap.put("BRANCH_CODE", _branchCode);
                    investmentTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                    investmentTransMap.put("USER_ID", objInvestmentsTransTO.getStatusBy());
                    investmentTransMap.put("INVESTMENT_NO", objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName());
                    investmentTransMap.put("ACCT_HEAD", achdMap.get("TDS_AC_HD"));
                    investmentTransMap.put("TRANS_AMOUNT", investTDS);
                    investmentTransMap.put("AUTHORIZEREMARKS", "IINVESTMENT_TDS");
                    investmentTransMap.put("INT_TYPE", "TDS");
                    investmentTransMap.put("TRANS_MOD_TYPE", "INV");
                    investmentTransMap.put("TDS_DEBIT", "TDS_DEBIT");
                    investmentTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                    CashTransactionTO cashObj = createCashTransactionTO(investmentTransMap);
                    cashList.add(cashObj);
                    if (cashObj != null && cashObj.getSingleTransId() != null) {
                        cashSingleTransId = CommonUtil.convertObjToStr(cashObj.getSingleTransId());
                        pickSingleTransId.put("SINGLE_CASH_TRANSID", cashSingleTransId);
                    }

                }
                //END
                //     System.out.print("###### cashList: nds " + cashList);
            }            
            
            //KD-3944 - by nithya on 22 may 2025
            if (map.containsKey("RENEWAL_INTEREST_TYPE") && !map.get("RENEWAL_INTEREST_TYPE").equals("PARTIAL_INTEREST")) {
                if (cashList != null && cashList.size() > 0) {
                    doCashTrans(cashList, _branchCode, false);

                }
                // System.out.println("NDSTransfer List"+transferList);
                if (transferList != null && transferList.size() > 0) {
                    doDebitCredit(transferList, _branchCode, false);
                }
                if (loanDataMap != null && loanDataMap.containsKey("TRANS_ID") && loanDataMap.get("TRANS_ID") != null) {
                    returnDataMap.put("BATCH_ID", CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
                }
//            System.out.println("############# returnDataMap : " + returnDataMap);
//            System.out.println("interest loandatamap" + loanDataMap);
                transferList = null;
            }
        } else {
            throw new Exception("investment  AccountHead is Not set...");
        }
        return null;
    }

    private String getDateFor(Date dd) {
        if (dd != null) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            System.out.println("New Date--->" + format.format(dd));
            return CommonUtil.convertObjToStr(format.format(dd));
        } else {
            return "";
        }
    }

    private CashTransactionTO createCashTransactionTO(HashMap dataMap) throws Exception {
        CashTransactionTO objCashTO = new CashTransactionTO();
        if (dataMap.get("STATUS").equals(CommonConstants.TOSTATUS_INSERT)) {
            objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
            objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.GL));
            if (dataMap.containsKey("TDS_DEBIT")) {
                objCashTO.setTransType(CommonConstants.DEBIT);
            } else {
                objCashTO.setTransType(CommonConstants.CREDIT);
            }
            if (dataMap.get("INT_TYPE") != null && (dataMap.get("INT_TYPE").equals("Partial_Interest")
                    || dataMap.get("INT_TYPE").equals("Interest"))) {
                objCashTO.setParticulars("By Cash : " + dataMap.get("INT_TYPE") + " " + CommonUtil.convertObjToStr(dataMap.get("PARTICULARS"))
                        + " From " + getDateFor(objInvestmentsDepositTO.getIntRecTillDt()) + " To " + getDateFor(objInvestmentsDepositTO.getMaturityDt()));
            } else {
                objCashTO.setParticulars(CommonUtil.convertObjToStr(dataMap.get("PARTICULARS")) + "By Cash : " + dataMap.get("INVESTMENT_NO") + " - " + dataMap.get("INT_TYPE"));
            }
            objCashTO.setAuthorizeRemarks(CommonUtil.convertObjToStr(dataMap.get("AUTHORIZEREMARKS")));
            objCashTO.setInpAmount(CommonUtil.convertObjToDouble(dataMap.get("TRANS_AMOUNT")));
            objCashTO.setAmount(CommonUtil.convertObjToDouble(dataMap.get("TRANS_AMOUNT")));
            objCashTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            objCashTO.setBranchId(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
            objCashTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            objCashTO.setStatusDt(currDt);
            objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            objCashTO.setCommand("INSERT");
            objCashTO.setSingleTransId(cashSingleTransId);
            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestment_internal_Id()));
            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
            if (dataMap.containsKey("SCREEN_NAME")) {
                objCashTO.setScreenName(CommonUtil.convertObjToStr(dataMap.get("SCREEN_NAME")));
            }
            System.out.println("objCashTO 1st oness:" + objCashTO);
        }
        return objCashTO;
    }
//edited by Nidhin

    private HashMap renewalDepositTransaction(HashMap map) throws Exception {
        System.out.println("In Side The DoTransaction Renewal Deposit");
        System.out.println("############# objInvestmentsTransTO For Deposit : " + objInvestmentsTransTO);
        HashMap achdMap = new HashMap();
        String investMentType = "";
        //Added by nithya on 04-02-2022 for KD-3287
        String renewWithOutTransaction = "N";
        if(map.containsKey("RENEWAL_WITHOUT_TRANSACTION") && map.get("RENEWAL_WITHOUT_TRANSACTION") != null){
            renewWithOutTransaction = CommonUtil.convertObjToStr(map.get("RENEWAL_WITHOUT_TRANSACTION"));
        }
        investMentType = CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentBehaves());
        achdMap.put("INVESTMENT_TYPE", CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentBehaves()));
        achdMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentID()));
        System.out.println("achdMap before query ------------>" + achdMap);
        List achdLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", achdMap);
        if (achdLst != null && achdLst.size() > 0) {
            achdMap = new HashMap();
//            ArrayList transferList = new ArrayList();
            ArrayList cashList = new ArrayList();
            achdMap = (HashMap) achdLst.get(0);
            System.out.println("achdMap--------------->" + achdMap);
            TransferTrans objTransferTrans = new TransferTrans();
            objTransferTrans.setInitiatedBranch(_branchCode);
            objTransferTrans.setLinkBatchId(objInvestmentsTransTO.getInvestmentName());
            HashMap txMap = new HashMap();
            if (!objInvestmentsTransTO.getTrnCode().equals("") && objInvestmentsTransTO.getTrnCode().equals("Deposit")) {
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                System.out.println("555----" + objInvestmentsTransTO.getInvestment_Ref_No());
                txMap.put(TransferTrans.PARTICULARS, objInvestmentsTransTO.getInvestment_Ref_No() + " " + objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName() + " - " + "Deposit");
                txMap.put("AUTHORIZEREMARKS", "INV_RENEWAL_DEPOSIT");
                txMap.put(TransferTrans.DR_AC_HD, achdMap.get("IINVESTMENT_AC_HD"));
                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                txMap.put("generateSingleTransId", generateSingleTransId);
                txMap.put("TRANS_MOD_TYPE", "INV");
                txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());//KD-4023
                if (map.containsKey("RENEWAL_INTEREST_TYPE")) {
                    if (map.get("RENEWAL_INTEREST_TYPE") != null && !map.get("RENEWAL_INTEREST_TYPE").equals("WITH_INTEREST")) {
                        double newAmt = CommonUtil.convertObjToDouble(objInvestmentsTransTO.getInvestmentAmount() - tdsAmt);
                        if (map.get("RENEWAL_INTEREST_TYPE").equals("WITHOUT_INTEREST")) {
                            //Added by nithya on 04-02-2022 for KD-3287
                            if(renewWithOutTransaction.equals("N"))
                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objInvestmentsTransTO.getInvestmentAmount()));
                        } else {
                            if (map.get("RENEWAL_INTEREST_TYPE").equals("PARTIAL_INTEREST")) {
                                double invAmt = objInvestmentsTransTO.getInvestmentAmount() + objInvestmentsDepositTO.getInterestReceivable() - objInvestmentsDepositTO.getRenewalPartialAmt();
                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, invAmt));
                            } else {
                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, newAmt));
                            }
                        }
                    }
                }
                reMap.put("DEP_AMOUNT", objInvestmentsTransTO.getInvestmentAmount().doubleValue() - tdsAmt);
                System.out.println("transferList ------------->" + transferList);

                if (map.containsKey("RENEWAL_NUMBER_TYPE") && map.get("RENEWAL_NUMBER_TYPE").equals("DIFFERENT_PROD_ID")) {
                    HashMap oldDepositHeadMap = new HashMap();
                    oldDepositHeadMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(map.get("OLD_INVESTMENT_ID")));
                    List oldDepositLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", oldDepositHeadMap);
                    if (oldDepositLst != null && oldDepositLst.size() > 0) {
                        oldDepositHeadMap = (HashMap) oldDepositLst.get(0);
                        txMap.put(TransferTrans.CR_AC_HD, (String) oldDepositHeadMap.get("IINVESTMENT_AC_HD"));
                    }
                } else {
                    txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("IINVESTMENT_AC_HD"));
                }
                txMap.put("AUTHORIZEREMARKS", "INV_RENEWAL_DEPOSIT");
                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                txMap.put("generateSingleTransId", generateSingleTransId);
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                System.out.println("666----" + objInvestmentsTransTO.getInvestment_Ref_No());
                txMap.put(TransferTrans.PARTICULARS, objInvestmentsTransTO.getInvestment_Ref_No() + " " + objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName() + " - " + "Deposit");
                txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
                txMap.put("TRANS_MOD_TYPE", "INV");
                if (map.containsKey("OLD_REF_ID") && map.get("OLD_REF_ID") != null) {
                    txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(map.get("OLD_REF_ID")) + " " + objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName() + " - " + "Deposit");
                }
                
                //Added by nithya on 04-02-2022 for KD-3287
                if(renewWithOutTransaction.equals("N"))
                transferList.add(objTransferTrans.getCreditTransferTO(txMap, objInvestmentsTransTO.getInvestmentAmount().doubleValue()));

                //recently edited by Nidhin 15-03-2014
                //debit transaction
                if (map.containsKey("IS_AMOUNT_ADDED") && map.get("IS_AMOUNT_ADDED") != null && map.get("IS_AMOUNT_ADDED").equals("Y")) {
                    if (map.containsKey("RENEWAL_DEP_TRANSMODE") && map.get("RENEWAL_DEP_TRANSMODE") != null
                            && map.get("RENEWAL_DEP_TRANSMODE").equals("Transfer")) {
                        System.out.println("Cash Transfer mode working");
                        if (map.containsKey("ADD_AMOUNT_TO_DEPOSIT") && map.get("ADD_AMOUNT_TO_DEPOSIT") != null) {
                            HashMap operativeDepMap = new HashMap();
                            HashMap otherBankAccHeadMap = new HashMap();
                            if (map.containsKey("RENEWAL_DEP_ID") && map.get("RENEWAL_DEP_ID") != null) {
                                operativeDepMap.put("ACT_NUM", map.get("RENEWAL_DEP_ID"));
                                List prodList = sqlMap.executeQueryForList("getAccNoProdIdDet", operativeDepMap);
                                if (prodList != null && prodList.size() > 0) {
                                    operativeDepMap = (HashMap) prodList.get(0);
                                }
                                otherBankAccHeadMap.put("ACT_MASTER_ID", map.get("RENEWAL_DEP_ID"));
                                List headList = sqlMap.executeQueryForList("getOtherAccNoHeaddDet", otherBankAccHeadMap);
                                if (headList != null && headList.size() > 0) {
                                    otherBankAccHeadMap = (HashMap) headList.get(0);
                                }
                            }
                            System.out.println("Mapssssssss()" + map);
                            System.out.println("operativeDepMap() " + operativeDepMap);
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            System.out.println("Recent Transaction" + objInvestmentsTransTO.getInvestment_Ref_No());
                            System.out.println(" " + map.get("RENEWAL_DEP_ID"));
                            txMap.put(TransferTrans.PARTICULARS, objInvestmentsTransTO.getInvestment_Ref_No() + " " + objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName() + " - " + "Add Deposit");
                            txMap.put("AUTHORIZEREMARKS", "INV_RENEWAL_DEPOSIT");
                            txMap.put(TransferTrans.DR_AC_HD, achdMap.get("IINVESTMENT_AC_HD"));
                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            txMap.put("generateSingleTransId", generateSingleTransId);
                            txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());//KD-4023
                            System.out.println("Credit TXMAP" + txMap);
                            txMap.put("TRANS_MOD_TYPE", "INV");
                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, CommonUtil.convertObjToDouble(map.get("ADD_AMOUNT_TO_DEPOSIT"))));
                            //credit transaction
                            if (map.containsKey("RENEWAL_DEP_TRANSPROD") && map.get("RENEWAL_DEP_TRANSPROD").equals("OA")) {
                                txMap.put(TransferTrans.CR_AC_HD, (String) operativeDepMap.get("AC_HD_ID"));
                                System.out.println("INT HEAD104 ===" + (String) operativeDepMap.get("AC_HD_ID"));
                                txMap.put(TransferTrans.CR_ACT_NUM, map.get("RENEWAL_DEP_ID"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                                txMap.put(TransferTrans.CR_PROD_ID, achdMap.get("INVESTMENT_PROD_ID"));
                                txMap.put("TRANS_MOD_TYPE", "OA");
                            } else if (map.containsKey("RENEWAL_DEP_TRANSPROD") && map.get("RENEWAL_DEP_TRANSPROD").equals("GL")) {
                                txMap.put(TransferTrans.CR_AC_HD, map.get("RENEWAL_DEP_ID"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put("TRANS_MOD_TYPE", "GL");
                            } else if (map.containsKey("RENEWAL_DEP_TRANSPROD") && map.get("RENEWAL_DEP_TRANSPROD").equals("AD")) {
                                txMap.put(TransferTrans.CR_AC_HD, map.get("RENEWAL_DEP_ID"));
                                txMap.put(TransferTrans.CR_ACT_NUM, map.get("RENEWAL_DEP_ID"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                                txMap.put(TransferTrans.CR_PROD_ID, achdMap.get("INVESTMENT_PROD_ID"));
                                txMap.put("TRANS_MOD_TYPE", "AD");
                            } else if (map.containsKey("RENEWAL_DEP_TRANSPROD") && map.get("RENEWAL_DEP_TRANSPROD").equals("AB")) {
                                txMap.put(TransferTrans.CR_ACT_NUM, map.get("RENEWAL_DEP_ID"));
                                txMap.put(TransferTrans.CR_AC_HD, (String) otherBankAccHeadMap.get("PRINCIPAL_AC_HD"));
                                txMap.put(TransferTrans.CR_PROD_ID, achdMap.get("INVESTMENT_PROD_ID"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);
                                txMap.put("TRANS_MOD_TYPE", "AB");
                            }
                            txMap.put("AUTHORIZEREMARKS", "INV_RENEWAL_DEPOSIT");
                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                            System.out.println("666----" + objInvestmentsTransTO.getInvestment_Ref_No());
                            txMap.put("generateSingleTransId", generateSingleTransId);
                            txMap.put(TransferTrans.PARTICULARS, objInvestmentsTransTO.getInvestment_Ref_No() + " " + objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName() + " - " + "Add Deposit");
                            txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
                            System.out.println("Credit TXMAP" + txMap);
                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, CommonUtil.convertObjToDouble(map.get("ADD_AMOUNT_TO_DEPOSIT"))));
                        }
                    } else if (map.containsKey("RENEWAL_DEP_TRANSMODE") && map.get("RENEWAL_DEP_TRANSMODE") != null
                            && map.get("RENEWAL_DEP_TRANSMODE").equals("Cash")) {
                        System.out.println("Cash else mode working");
                        if (map.containsKey("ADD_AMOUNT_TO_DEPOSIT") && map.get("ADD_AMOUNT_TO_DEPOSIT") != null) {
                            HashMap investmentTransMap = new HashMap();
                            investmentTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                            investmentTransMap.put("BRANCH_CODE", _branchCode);
                            investmentTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                            investmentTransMap.put("USER_ID", objInvestmentsTransTO.getStatusBy());
                            if (map.containsKey("FD_INTERNAL_ACC_NO") && map.get("FD_INTERNAL_ACC_NO") != null) {
                                investmentTransMap.put("FD_INTERNAL_ACC_NO", map.get("FD_INTERNAL_ACC_NO"));
                            }
                            investmentTransMap.put("INVESTMENT_NO", objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName());
                            if (objInvestmentsTransTO.getInvestmentAmount().doubleValue() != 0.0) {
                                investmentTransMap.put("ACCT_HEAD", achdMap.get("IINVESTMENT_AC_HD"));
                                investmentTransMap.put("TRANS_AMOUNT", CommonUtil.convertObjToDouble(map.get("ADD_AMOUNT_TO_DEPOSIT")));
                                investmentTransMap.put("AUTHORIZEREMARKS", "IINVESTMENT_AC_HD");
                                investmentTransMap.put("PROD_ID", achdMap.get("INVESTMENT_PROD_ID"));
                                investmentTransMap.put("TRN_CODE", "Deposit");
                                investmentTransMap.put("INT_TYPE", "Add_Deposit");
                                //cashList.add(createCashDebitTransactionTO(investmentTransMap));
                                investmentTransMap.put("TRANS_MOD_TYPE", "INV");
                                CashTransactionTO cashObj = createCashDebitTransactionTO(investmentTransMap);
                                cashList.add(cashObj);
                                if (cashObj != null && cashObj.getSingleTransId() != null) {
                                    cashSingleTransId = CommonUtil.convertObjToStr(cashObj.getSingleTransId());
                                    pickSingleTransId.put("SINGLE_CASH_TRANSID", cashSingleTransId);
                                }
                            }
                            System.out.println(" cashlist" + cashList);
                            if (cashList != null && cashList.size() > 0) {
                                doCashTrans(cashList, _branchCode, false);
                            }
                        }
                    }
                }
                //ended
                System.out.println("transferList  ------------->" + transferList);
                System.out.println("transferList total ------------->" + transferList);
                if (transferList != null && transferList.size() > 0) {
                    //doDebitCredit(transferList, _branchCode, false);
                }
                //transferList = null;
//                if(loanDataMap != null && loanDataMap.containsKey("TRANS_ID") && loanDataMap.get("TRANS_ID") != null){
//                returnDataMap.put("BATCH_ID", CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
//                }
                System.out.println("############# returnDataMap : " + returnDataMap);
                System.out.println("Deposit loandatamap" + loanDataMap);
            } else {
                throw new Exception("investment  Transaction is not proper");
            }
        } else {
            throw new Exception("investment  AccountHead is Not set...");
        }
        return null;
    }

    /*
     * This method is used to update the already existing data by making its
     * status to be deleted
     */
    private void deleteData() throws Exception {
        if (objInvestmentsOperativeTO != null) {
            logTO.setData(objInvestmentsOperativeTO.toString());
            logTO.setPrimaryKey(objInvestmentsOperativeTO.getKeyData());
            logTO.setStatus(objInvestmentsOperativeTO.getCommand());
            logDAO.addToLog(logTO);
            sqlMap.executeUpdate("deleteInvestmentsOperativeTO", objInvestmentsOperativeTO);
            sqlMap.executeUpdate("deleteInvestmentSMasterTO", objInvestmentsOperativeTO);
            sqlMap.executeUpdate("deleteOldInvestmentsDetails", objInvestmentsOperativeTO);
            sqlMap.executeUpdate("deleteChequeMasterStatus", objInvestmentsOperativeTO);
        } else if (objInvestmentsDepositTO != null) {
            logTO.setData(objInvestmentsDepositTO.toString());
            logTO.setPrimaryKey(objInvestmentsDepositTO.getKeyData());
            logTO.setStatus(objInvestmentsDepositTO.getCommand());
            logDAO.addToLog(logTO);
            sqlMap.executeUpdate("deleteInvestmentsDepositTO", objInvestmentsDepositTO);
            sqlMap.executeUpdate("deleteInvestmentSMasterTO", objInvestmentsDepositTO);
        } else if (objInvestmentsShareTO != null) {
            logTO.setData(objInvestmentsShareTO.toString());
            logTO.setPrimaryKey(objInvestmentsShareTO.getKeyData());
            logTO.setStatus(objInvestmentsShareTO.getCommand());
            logDAO.addToLog(logTO);
            sqlMap.executeUpdate("deleteInvestmentsShareTO", objInvestmentsShareTO);
            sqlMap.executeUpdate("deleteInvestmentSMasterTO", objInvestmentsShareTO);
        } else if (objInvestmentsRFTO != null) {
            logTO.setData(objInvestmentsRFTO.toString());
            logTO.setPrimaryKey(objInvestmentsRFTO.getKeyData());
            logTO.setStatus(objInvestmentsRFTO.getCommand());
            logDAO.addToLog(logTO);
            sqlMap.executeUpdate("deleteInvestmentsRFTO", objInvestmentsRFTO);
            sqlMap.executeUpdate("deleteInvestmentSMasterTO", objInvestmentsRFTO);
        }
    }

    public static void main(String str[]) {
        try {
            InvestmentsMasterDAO dao = new InvestmentsMasterDAO();
        } catch (Exception ex) {
        }
    }

    /**
     * This method is called to do desired operations in the Table
     */
    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        try {
            sqlMap.startTransaction();
            returnDataMap = new HashMap();
            System.out.println("Map in Dao ------------>" + map);
            final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
//            objTO = (InvestmentsMasterTO) map.get("InvestmentsMasterTO");
            if (map.containsKey("InvestmentsOperativeTO")) {
                objInvestmentsOperativeTO = (InvestmentsOperativeTO) map.get("InvestmentsOperativeTO");
            } else if (map.containsKey("InvestmentsDepositTO")) {
                objInvestmentsDepositTO = (InvestmentsDepositTO) map.get("InvestmentsDepositTO");
            } else if (map.containsKey("InvestmentsShareTO")) {
                objInvestmentsShareTO = (InvestmentsShareTO) map.get("InvestmentsShareTO");
            } else if (map.containsKey("InvestmentsReserveFundTO")) {
                objInvestmentsRFTO = (InvestmentsRFTO) map.get("InvestmentsReserveFundTO");
            }
            if (map.containsKey("InvestmentsTransTO")) {
                objInvestmentsTransTO = (InvestmentsTransTO) map.get("InvestmentsTransTO");
            }

            if (map.containsKey("chequeBookDetails")) {
                tableDetails = (LinkedHashMap) map.get("chequeBookDetails");
            }
            if (map.containsKey("deletedChequeBookDetails")) {
                deletedTableValues = (LinkedHashMap) map.get("deletedChequeBookDetails");
            }
            //    System.out.println("back date&^*^*&(&(&("+map.get("TRANS_DATE"));
            System.out.println("back BACK_DATED_TRANSACTION&^*^*&(&(&(" + map.get("BACK_DATED_TRANSACTION"));
            if (map.containsKey("BACK_DATED_TRANSACTION")) {
                backDate = (Date) map.get("TRANS_DATE");
            }
            logDAO = new LogDAO();
            logTO = new LogTO();
            logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
            logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
            logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
            logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
            logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
            if (!map.containsKey(CommonConstants.AUTHORIZEMAP)) {
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertData(map);
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData(map);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData();
                }
            } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
                System.out.println("map:" + map);
                HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
                authMap.put(CommonConstants.BRANCH_ID, (String) map.get(CommonConstants.BRANCH_ID));
                System.out.println("authMap:" + authMap);
                if (authMap != null) {
                    if (authMap.containsKey("RENEWAL")) {
                        renewalAuthorize(authMap, map);
                    } else {
                        authorize(authMap, map);
                    }
                    returnDataMap.put("COMPLETED", "COMPLETED");
                }
                authMap = null;
            } else {
                throw new NoCommandException();
            }
            sqlMap.commitTransaction();
            destroyObjects();
            System.out.println("########### returnDataMap : " + returnDataMap);
            return returnDataMap;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw new TransRollbackException(e);
        }
    }

    private void renewalAuthorize(HashMap authMap, HashMap map) throws Exception {
        System.out.println("authMap ------------>" + authMap + " MAAAASSPP" + map);
        String batchid = "";
        HashMap param = new HashMap();
        String status = (String) authMap.get(CommonConstants.AUTHORIZESTATUS);
        param.put("INITIATED_BRANCH", _branchCode);
        param.put("INVESTMENT_ID", authMap.get("INVESTMENT_ID"));
        param.put("TRANS_DT", currDt.clone());
        param.put("BRANCH_CODE", _branchCode);
        param.put("BATCH_ID", authMap.get("INVESTMENT_ID"));
        param.put("AUTHORIZE_STATUS",  authMap.get("AUTHORIZE_STATUS"));
        param.put("TRANS_DT", map.get("TRANS_DATE"));       
        List cashList = (List) sqlMap.executeQueryForList("getCashDetails", param);
        //List transList = (List) sqlMap.executeQueryForList("getTransferDetails", param);
        List transList = (List) sqlMap.executeQueryForList("getTransferDetailsForInvestmentRenewal", param); // Added by nithya on 11-03-2020 for KD-1532
        //Added by nithya on 04-02-2022 for KD-3287
        String renewWithOutTransaction = "N";
        if(map.containsKey("RENEWAL_WITHOUT_TRANSACTION") && map.get("RENEWAL_WITHOUT_TRANSACTION") != null){
            renewWithOutTransaction = CommonUtil.convertObjToStr(map.get("RENEWAL_WITHOUT_TRANSACTION"));
        }
        HashMap transMap = new HashMap();

        if (status.equals("AUTHORIZED")) {
            //Update Available Balance
            System.out.println("parammmmmms" + param);
            HashMap whereMap = new HashMap();
            whereMap.put("INITIATED_BRANCH", _branchCode);
            whereMap.put("INVESTMENT_ID", authMap.get("INVESTMENT_ID"));
            whereMap.put("TRANS_DT", map.get("TRANS_DATE"));
            List investmentTransList = (List) sqlMap.executeQueryForList("getDataForRenewalInvestmentTrans", whereMap);
            System.out.println("investmentTransList >>>" + investmentTransList);
            if (investmentTransList != null && investmentTransList.size() > 0) {
                for (int i = 0; i < investmentTransList.size(); i++) {
                    whereMap = (HashMap) investmentTransList.get(i);
                    HashMap dataMap = new HashMap();
                    dataMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(whereMap.get("INITIATED_BRANCH")));
                    dataMap.put("INVEST_ID", CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_ID")));
                    dataMap.put("BATCH_ID", CommonUtil.convertObjToStr(whereMap.get("BATCH_ID")));
                    List dataLst = ServerUtil.executeQuery("getTDSInvestmentRenewal", dataMap);
                    double tdsAmt = 0;
                    if (dataLst != null && dataLst.size() > 0) {
                        dataMap = (HashMap) dataLst.get(0);
                        tdsAmt = CommonUtil.convertObjToDouble(dataMap.get("TDS_AMOUNT"));
                    }
                    System.out.println("tdsAmt -------------------->" + tdsAmt);
                    String invType = "";
                    invType = CommonUtil.convertObjToStr(whereMap.get("TRAN_CODE"));
                    System.out.println("##### investmentTransMap : " + whereMap);
                    System.out.println("##### invType : " + invType);
                    String debitAcNo = "";
                    HashMap invWhereMap = new HashMap();
                    debitAcNo = CommonUtil.convertObjToStr(whereMap.get("OTHER_BANK_INTERNAL_NO"));
                    if (debitAcNo.length() > 0 && i == 0) {
                        if (map.containsKey("TransactionTO")) {
                            HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                            TransactionTO transactionTO = new TransactionTO();
                            LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                            if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                                allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                                System.out.println("####### allowedTransDetailsTO : " + allowedTransDetailsTO);
                                if (transactionTO.getTransType().equals("TRANSFER")) {
                                    //Update Available Balance In INVESTMENT_MASTER TABLE
                                    double availableBalance = 0.0;
                                    invWhereMap.put("INVESTMENT_ID", debitAcNo);
                                    List debitLst = sqlMap.executeQueryForList("getAvailableBalanceFromMaster", invWhereMap);
                                    if (debitLst != null && debitLst.size() > 0) {
                                        invWhereMap = (HashMap) debitLst.get(0);
                                        availableBalance = CommonUtil.convertObjToDouble(invWhereMap.get("AVAILABLE_BALANCE")).doubleValue();
                                        System.out.println("##### availableBalance : " + availableBalance);
                                        availableBalance += CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                                        invWhereMap.put("AVAILABLE_BALANCE", availableBalance - tdsAmt);
                                        invWhereMap.put("BATCH_ID", whereMap.get("BATCH_ID"));
                                        System.out.println("##### After Auth availableBalance : " + availableBalance);
                                        sqlMap.executeUpdate("updateInvestMasterAvailableBalance", invWhereMap);
                                        sqlMap.executeUpdate("updateInvestTrnasOtherBankAvailableBalance", invWhereMap);
                                    }
                                    String chequeNo = "";
                                    chequeNo = CommonUtil.convertObjToStr(whereMap.get("CHEQUE_NO"));
                                    if (!chequeNo.equals("") && chequeNo.length() > 0) {
                                        invWhereMap.put("CHEQUE_NO", chequeNo);
                                        sqlMap.executeUpdate("updateCheckNoStatusUsed", invWhereMap);
                                    }
                                }
                            }
                        }
                    }
                    if (invType.equals("Deposit")) {
                        String creditAcNo = "";
                        invWhereMap = new HashMap();
                        creditAcNo = CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_ID"));
                        System.out.println("##### creditAcNo : " + creditAcNo);
                        System.out.println("##### debitAcNo : " + debitAcNo);

                        if (creditAcNo.length() > 0) {  //Update Available Balance In INVESTMENT_MASTER TABLE
                            double availableBalance = 0.0;
                            invWhereMap.put("INVESTMENT_ID", creditAcNo);
                            List creditLst = sqlMap.executeQueryForList("getAvailableBalanceFromMaster", invWhereMap);
                            if (creditLst != null && creditLst.size() > 0) {
                                invWhereMap = (HashMap) creditLst.get(0);
                                availableBalance = CommonUtil.convertObjToDouble(invWhereMap.get("AVAILABLE_BALANCE")).doubleValue();
                                System.out.println("##### availableBalance : " + availableBalance);
                                if (invType.equals("Deposit")) {
                                    availableBalance += CommonUtil.convertObjToDouble(whereMap.get("INVESTMENT_AMOUNT")).doubleValue();
                                } else if (invType.equals("Interest")) {
                                    availableBalance += CommonUtil.convertObjToDouble(whereMap.get("BRKOEN_PERIOD_INTEREST")).doubleValue();
                                }
                                invWhereMap.put("AVAILABLE_BALANCE",availableBalance - tdsAmt);
                                invWhereMap.put("BATCH_ID", whereMap.get("BATCH_ID"));
                                System.out.println("##### After Auth availableBalance : " + availableBalance);
                                invWhereMap.put("TRAN_CODE", invType);
                                sqlMap.executeUpdate("updateInvestMasterAvailableBalance", invWhereMap);
                                double amt = CommonUtil.convertObjToDouble(map.get("AVAILABLE_BALANCE")) - tdsAmt;
                                invWhereMap.remove("AVAILABLE_BALANCE");
                                invWhereMap.put("AVAILABLE_BALANCE", String.valueOf(amt));
                                sqlMap.executeUpdate("updateInvestTrnasAvailableBalance", invWhereMap);
                            }
                        }
                    } else if (invType.equals("Withdrawal")) {
                        String creditAcNo = "";
                        invWhereMap = new HashMap();
                        creditAcNo = CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_ID"));
                        System.out.println("##### creditAcNo : " + creditAcNo);
                        double availableBalance = 0.0;
                        if (creditAcNo.length() > 0) {  //Update Available Balance In INVESTMENT_MASTER TABLE
                            availableBalance = 0.0;
                            invWhereMap.put("INVESTMENT_ID", creditAcNo);
                            List creditLst = sqlMap.executeQueryForList("getAvailableBalanceFromMaster", invWhereMap);
                            if (creditLst != null && creditLst.size() > 0) {
                                invWhereMap = (HashMap) creditLst.get(0);
                                availableBalance = CommonUtil.convertObjToDouble(invWhereMap.get("AVAILABLE_BALANCE")).doubleValue();
                                System.out.println("##### availableBalance : " + availableBalance);
                                availableBalance -= CommonUtil.convertObjToDouble(whereMap.get("INVESTMENT_AMOUNT")).doubleValue();
                                invWhereMap.put("AVAILABLE_BALANCE", availableBalance - tdsAmt);
                                invWhereMap.put("BATCH_ID", whereMap.get("BATCH_ID"));
                                System.out.println("##### After Auth availableBalance : " + availableBalance);
                                invWhereMap.put("TRAN_CODE", invType);
                                sqlMap.executeUpdate("updateInvestMasterAvailableBalance", invWhereMap);
                                invWhereMap.remove("AVAILABLE_BALANCE");
                                invWhereMap.put("AVAILABLE_BALANCE", String.valueOf(availableBalance));
                                sqlMap.executeUpdate("updateInvestTrnasAvailableBalance", invWhereMap);
                            }
                        }
                    }
                }
            }
            if ((map.containsKey("NEW_NUM") ||map.containsKey("DIFF_NUM"))) {
                param.put("cur_dt", currDt.clone());
                //sqlMap.executeUpdate("authorizeTimeMasterDetails", param); // Commented by nithya on 12-03-2020 for KD-1643
            }

        
        //Inserting & Deleting Deposit Details
        sqlMap.executeUpdate("deleteInvestmentDepositDetail", authMap);
        sqlMap.executeUpdate("deleteInvestmentRenewalDepositDetail", authMap);
        if (map.containsKey("RenewalInvestmentsDepositTO")) {
            InvestmentsDepositTO objInvestmentsDepositTO = new InvestmentsDepositTO();
            objInvestmentsDepositTO = (InvestmentsDepositTO) map.get("RenewalInvestmentsDepositTO");
            System.out.println("map.get(AVAILABLE_BALANCE)%T#%#" + map.get("AVAILABLE_BALANCE"));
            System.out.println("tdsAmt)%T#%#" + tdsAmt);
            if (map.get("RENEWAL_INTEREST_TYPE").equals("WITH_INTEREST")) {
                objInvestmentsDepositTO.setMaturityAmount(CommonUtil.convertObjToDouble(map.get("AVAILABLE_BALANCE")) - tdsAmt);
                objInvestmentsDepositTO.setPrincipalAmount(CommonUtil.convertObjToDouble(map.get("AVAILABLE_BALANCE")) - tdsAmt);
            } else {
                objInvestmentsDepositTO.setMaturityAmount(CommonUtil.convertObjToDouble(map.get("AVAILABLE_BALANCE")));
                objInvestmentsDepositTO.setPrincipalAmount(CommonUtil.convertObjToDouble(map.get("AVAILABLE_BALANCE")));
            }
            // objInvestmentsDepositTO.setInterestReceivable(objInvestmentsDepositTO.getInterestReceivable()- tdsAmt);
            objInvestmentsDepositTO.setRenewalDt(currDt);
            sqlMap.executeUpdate("insertInvestmentsDepositTO", objInvestmentsDepositTO);
            if (map.containsKey("AVAILABLE_BALANCE")) {
                double amt = CommonUtil.convertObjToDouble(map.get("AVAILABLE_BALANCE")) - tdsAmt;
                param.put("AVAILABLE_BALANCE", CommonUtil.convertObjToDouble(amt));
//                    param.put("AVAILABLE_BALANCE",CommonUtil.convertObjToStr(objInvestmentsDepositTO.getPrincipalAmount()));
                sqlMap.executeUpdate("updateInvestMasterAvailableBalance", param);
            }
        }
        if (map.containsKey("InvestmentsDepositTO")) {
            InvestmentsDepositTO objInvestmentsDepositTO = new InvestmentsDepositTO();
            objInvestmentsDepositTO = (InvestmentsDepositTO) map.get("InvestmentsDepositTO");
            objInvestmentsDepositTO.setStatus(CommonConstants.CLOSED);
            sqlMap.executeUpdate("insertInvestmentsRenewalDepositTO", objInvestmentsDepositTO);
        }
    }
    //Added by nithya on 04-02-2022 for KD-3287
    if(renewWithOutTransaction.equals("Y")){
        if(((cashList == null || (cashList!= null && cashList.size() == 0)))&& ((transferList == null || (transferList!= null && transferList.size() == 0)))){
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.STATUS, status);
            whereMap.put("INVESTMENT_ID", authMap.get("INVESTMENT_ID"));
            whereMap.put("USER_ID", map.get("USER_ID"));
            whereMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            whereMap.put("TRANS_DT", currDt.clone());
            whereMap.put("INITIATED_BRANCH", _branchCode);
            sqlMap.executeUpdate("authorizeInvestmentTransWithoutTransaction", whereMap);
        }
    }    

    System.out.println (
    "CASHLISTSSSS" + cashList);
        if (cashList

    != null && cashList.size () 
        > 0) {//11
                if (map.containsKey("TransactionTO") && map.get("TransactionTO") != null) {
            HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
            TransactionTO transactionTO = new TransactionTO();
            LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
            if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
            }
            transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
            String oldBatchId = "";
            if (cashList != null && cashList.size() > 0) {
                for (int i = 0; i < cashList.size(); i++) {
                    transMap = (HashMap) cashList.get(i);
                    batchid = CommonUtil.convertObjToStr(transMap.get("TRANS_ID"));
                    if (!oldBatchId.equals(batchid)) {
                        doCashAuthorize(authMap, map, transactionTO, batchid);
                    }
                    oldBatchId = batchid;
                }
            }
        } else {
            HashMap whereMap = new HashMap();
            HashMap cashMap = (HashMap) cashList.get(0);
            whereMap.put("STATUS", "AUTHORIZED");
            whereMap.put("INITIATED_BRANCH", _branchCode);
            whereMap.put("SHIFT", "");
            whereMap.put("TRANS_DT", currDt.clone());
            whereMap.put("TRANS_ID", CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
            whereMap.put("USER_ID", map.get("USER_ID"));
            whereMap.put("SINGLE_TRANS_ID", CommonUtil.convertObjToStr(cashMap.get("SINGLE_TRANS_ID")));
            whereMap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(cashMap.get("LINK_BATCH_ID")));
            sqlMap.executeUpdate("authorizeCashTransaction", whereMap);
        }
    }

    if (transList

    != null && transList.size () 
        > 0) {
            String oldBatchId = "";
        for (int b = 0; b < transList.size(); b++) {
            transMap = (HashMap) transList.get(b);
            batchid = CommonUtil.convertObjToStr(transMap.get("BATCH_ID"));
            if (!oldBatchId.equals(batchid)) {
                dotransferInvestment(batchid, status, authMap, map);
            }
            oldBatchId = batchid;
        }
    }

//        List transList = (List) sqlMap.executeQueryForList("getDataForInvestmentTransRenewalTime", param);
//        if(transList!=null && transList.size()>0){
//            for(int i=0; i<transList.size();i++){
//                HashMap renewalHashMap = new HashMap();
//                renewalHashMap =(HashMap)transList.get(i);
//                batchid = CommonUtil.convertObjToStr(renewalHashMap.get("BATCH_ID"));
//                dotransferInvestment(batchid, status, authMap, map);
//            }
}

private void authorize(HashMap authMap, HashMap map) throws Exception {
        System.out.println("authMap ------------>authorize" + authMap);
        String batchid = objInvestmentsTransTO.getBatchID();
        String investMentType = "";
        investMentType = CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentBehaves());
        String status = (String) authMap.get(CommonConstants.AUTHORIZESTATUS);
        String user_id = (String) authMap.get(CommonConstants.USER_ID);
        ArrayList selectedList = (ArrayList) authMap.get(CommonConstants.AUTHORIZEDATA);
        HashMap dataMap = new HashMap();
        String transType = "";
        if (map.containsKey("TransactionTO")) {
            for (int i = 0; i < selectedList.size(); i++) {
                dataMap = (HashMap) selectedList.get(i);
                batchid = CommonUtil.convertObjToStr(dataMap.get("BATCH_ID"));
                HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                TransactionTO transactionTO = new TransactionTO();
                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                }
                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                transType = CommonUtil.convertObjToStr(transactionTO.getTransType());
                System.out.println("####### allowedTransDetailsTO : " + allowedTransDetailsTO);
                if (transactionTO.getTransType().equals("TRANSFER")) {
                    dotransferInvestment(batchid, status, authMap, map);
                } else {
                    doCashAuthorize(authMap, map, transactionTO, batchid);
                }
            }
            if (!status.equals(CommonConstants.STATUS_REJECTED)) {
                //Added By Suresh
                if (status.equals(CommonConstants.STATUS_AUTHORIZED)) {
                    if (objInvestmentsTransTO.getTrnCode().equals("Deposit") || objInvestmentsTransTO.getTrnCode().equals("Purchase")) {
                        String creditAcNo = "";
                        String debitAcNo = "";
                        HashMap whereMap = new HashMap();
                        creditAcNo = CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestment_internal_Id());
                        debitAcNo = CommonUtil.convertObjToStr(objInvestmentsTransTO.getTxtInvestmentInternalNoTrans());
                        System.out.println("##### creditAcNo : " + creditAcNo);
                        System.out.println("##### debitAcNo : " + debitAcNo);
                        //Update Available Balance In INVESTMENT_MASTER TABLE
                        if (creditAcNo.length() > 0) {
                            double availableBalance = 0.0;
                            whereMap.put("INVESTMENT_ID", creditAcNo);
                            List creditLst = sqlMap.executeQueryForList("getAvailableBalanceFromMaster", whereMap);
                            if (creditLst != null && creditLst.size() > 0) {
                                whereMap = (HashMap) creditLst.get(0);
                                availableBalance = CommonUtil.convertObjToDouble(whereMap.get("AVAILABLE_BALANCE")).doubleValue();
                                System.out.println("##### availableBalance : " + availableBalance);
                                if (objInvestmentsTransTO.getTrnCode().equals("Deposit")) {
                                    availableBalance += CommonUtil.convertObjToDouble(objInvestmentsTransTO.getInvestmentAmount()).doubleValue();
                                } else if (objInvestmentsTransTO.getTrnCode().equals("Purchase")) {
                                    availableBalance += CommonUtil.convertObjToDouble(objInvestmentsTransTO.getInvestmentAmount()).doubleValue() - CommonUtil.convertObjToDouble(objInvestmentsTransTO.getPurchaseRate()).doubleValue(); // Total_Share Amt-Fees Paid
                                }
                                whereMap.put("AVAILABLE_BALANCE", availableBalance);
                                whereMap.put("BATCH_ID", objInvestmentsTransTO.getBatchID());
                                System.out.println("##### After Auth availableBalance : " + availableBalance);
                                sqlMap.executeUpdate("updateInvestMasterAvailableBalance", whereMap);
                                sqlMap.executeUpdate("updateInvestTrnasAvailableBalance", whereMap);
                                
//                                
//                                if(diffprod || new number){
//                                    whereMap.put("cur+dt", currDt);
//                                    whereMap.put("ive_id", objInvestmentsTransTO.getBatchID());
//                                    System.out.println("##### After Auth availableBalance : " + availableBalance);
//                                    sqlMap.executeUpdate("updateInvestMasterAvailableBalance", whereMap);
//                                }
                            }
                        }
                        //Update Available Balance In INVESTMENT_MASTER TABLE
                        if (debitAcNo.length() > 0 && transType.equals("TRANSFER")) {
                            double availableBalance = 0.0;
                            whereMap.put("INVESTMENT_ID", debitAcNo);
                            List debitLst = sqlMap.executeQueryForList("getAvailableBalanceFromMaster", whereMap);
                            if (debitLst != null && debitLst.size() > 0) {
                                whereMap = (HashMap) debitLst.get(0);
                                availableBalance = CommonUtil.convertObjToDouble(whereMap.get("AVAILABLE_BALANCE")).doubleValue();
                                System.out.println("##### availableBalance : " + availableBalance);
                                if (objInvestmentsTransTO.getTrnCode().equals("Deposit") || objInvestmentsTransTO.getTrnCode().equals("Purchase")) {
                                    availableBalance -= CommonUtil.convertObjToDouble(objInvestmentsTransTO.getInvestmentAmount()).doubleValue();
                                }
                                whereMap.put("AVAILABLE_BALANCE", availableBalance);
                                whereMap.put("BATCH_ID", objInvestmentsTransTO.getBatchID());
                                System.out.println("##### After Auth availableBalance : " + availableBalance);
                                sqlMap.executeUpdate("updateInvestMasterAvailableBalance", whereMap);
                                sqlMap.executeUpdate("updateInvestTrnasOtherBankAvailableBalance", whereMap);
                            }
                            String chequeNo = "";
                            chequeNo = CommonUtil.convertObjToStr(objInvestmentsTransTO.getTxtChequeNo());
                            if (!chequeNo.equals("") && chequeNo.length() > 0) {
                                whereMap.put("CHEQUE_NO", objInvestmentsTransTO.getTxtChequeNo());
                                sqlMap.executeUpdate("updateCheckNoStatusUsed", whereMap);
                            }
                        }
                    }
                }
            }
        }
        dataMap = null;
        selectedList = null;
    }

    private void dotransferInvestment(String batchid, String status, HashMap authMap, HashMap map) throws Exception {
        TransactionTO transactionTO;
        HashMap cashAuthMap, tempMap, transIdMap, transferTransParam;
        ArrayList cashTransList, transferTransList, transactionList;
        transactionList = new ArrayList();
        transferTransParam = new HashMap();
        transferTransParam.put(CommonConstants.BRANCH_ID, authMap.get(CommonConstants.BRANCH_ID));
        transferTransParam.put(CommonConstants.USER_ID, authMap.get(CommonConstants.USER_ID));
        transferTransParam.put("BATCHID", batchid);
        transferTransParam.put("BATCH_ID", batchid);
        transferTransParam.put(CommonConstants.AUTHORIZESTATUS, status);
        if (!authMap.containsKey("callexcessOrShortauthorize")) {
            transferTransParam.put("TRANS_DT", map.get("TRANS_DATE"));
            transferTransParam.put("TRANS_DATE", map.get("TRANS_DATE"));
            transferTransParam.put("INITIATED_BRANCH", _branchCode);
            transferTransList = (ArrayList) sqlMap.executeQueryForList("getBatchTxTransferTOsAuthorize", transferTransParam);
            if (transferTransList != null && (!transferTransList.isEmpty())) {
                TransferTrans objTrans = new TransferTrans();
                if (map.containsKey("BACK_DATED_TRANSACTION")) {
                     transferTransParam.put("BACK_DATED_TRANSACTION", "BACK_DATED_TRANSACTION");
                }
                objTrans.doTransferAuthorize(transferTransList, transferTransParam);
                transferTransParam.put(CommonConstants.STATUS, status);
                if (map.containsKey("BILLS_LINK_BATCH_ID")) {
                    transferTransParam.put("BATCH_ID", map.get("BILLS_LINK_BATCH_ID"));
                } else {
                    transferTransParam.put("BATCH_ID", batchid);
                }
                transferTransParam.put(CommonConstants.AUTHORIZEDT, currDt.clone());
                transferTransParam.put("TRANS_DT", map.get("TRANS_DATE"));
                transferTransParam.put("INITIATED_BRANCH", _branchCode);
                sqlMap.executeUpdate("authorizeInvestmentTrans", transferTransParam);
            } else {
                throw new TTException("Transfer List Is Empty");
            }
        }
        transferTransList = null;
        transferTransParam = null;
    }

    private void doCashAuthorize(HashMap authMap, HashMap map, TransactionTO transactionTO, String batchid) throws Exception {
        String status = CommonUtil.convertObjToStr(authMap.get("AUTHORIZESTATUS"));
        if (!authMap.containsKey("callexcessOrShortauthorize")) {
            CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
            ArrayList arrList = new ArrayList();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", status);
//            singleAuthorizeMap.put("TRANS_ID", CommonUtil.convertObjToStr(transactionTO.getBatchId()));
            singleAuthorizeMap.put("TRANS_ID", batchid);
            singleAuthorizeMap.put("USER_ID", map.get("USER_ID"));
            arrList.add(singleAuthorizeMap);
            String branchCode = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
            String userId = CommonUtil.convertObjToStr(map.get("USER_ID"));
            System.out.println("before making new DAO map :" + map);
            map = new HashMap();
            map.put("SCREEN", "Cash");
            map.put("USER_ID", userId);
            map.put("SELECTED_BRANCH_ID", branchCode);
            map.put("BRANCH_CODE", branchCode);
            map.put("MODULE", "Transaction");
            HashMap dataMap = new HashMap();
            dataMap.put(CommonConstants.AUTHORIZESTATUS, status);
            dataMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            dataMap.put("DAILY", "DAILY");
            map.put(CommonConstants.AUTHORIZEMAP, dataMap);
            System.out.println("before entering DAO map :" + map);
            cashTransactionDAO.execute(map, false);
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.STATUS, status);
            whereMap.put("BATCH_ID", batchid);
            whereMap.put("USER_ID", map.get("USER_ID"));
            whereMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            whereMap.put("TRANS_DT", currDt.clone());
            whereMap.put("INITIATED_BRANCH", _branchCode);
            sqlMap.executeUpdate("authorizeInvestmentTrans", whereMap);
            cashTransactionDAO = null;
            dataMap = null;
        } else if (authMap.containsKey("callexcessOrShortauthorize")) {
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.STATUS, status);
            whereMap.put("BATCH_ID", batchid);
            whereMap.put("USER_ID", map.get("USER_ID"));
            whereMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            whereMap.put("TRANS_DT", currDt.clone());
            whereMap.put("INITIATED_BRANCH", _branchCode);
            sqlMap.executeUpdate("authorizeInvestmentTrans", whereMap);
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        List OperativeList = (List) sqlMap.executeQueryForList("getSelectInvestmentsOperativeTO", map);
        if (OperativeList != null && OperativeList.size() > 0) {
            returnMap.put("InvestmentsOperativeTO", OperativeList);

            List chequeList = (List) sqlMap.executeQueryForList("getSelectInvestmentsChequeTO", map); // CHEQUE_DETAILS
            if (chequeList != null && chequeList.size() > 0) {
                LinkedHashMap ParMap = new LinkedHashMap();
                for (int i = chequeList.size(), j = 0; i > 0; i--, j++) {
                    String st = ((InvestmentsChequeTO) chequeList.get(j)).getSlNo();
                    ParMap.put(((InvestmentsChequeTO) chequeList.get(j)).getSlNo(), chequeList.get(j));
                }
                System.out.println("@@@ParMap" + ParMap);
                returnMap.put("chequeListTO", ParMap);
            }
        }
        List depositList = (List) sqlMap.executeQueryForList("getSelectInvestmentsDepositTO", map);
        if (depositList != null && depositList.size() > 0) {
            returnMap.put("InvestmentsDepositTO", depositList);
        }
        if (map.get("ACTION_TYPE").equals("Closed_Details")) {
            depositList = (List) sqlMap.executeQueryForList("getSelectClosedInvestmentsDepositTO", map);
            if (depositList != null && depositList.size() > 0) {
                returnMap.put("InvestmentsDepositTO", depositList);
            } else {
                depositList = (List) sqlMap.executeQueryForList("getSelectClosedRenewalInvestmentsTO", map);
                if (depositList != null && depositList.size() > 0) {
                    returnMap.put("InvestmentsDepositTO", depositList);
                }
            }
        }
        List renewalDepositList = (List) sqlMap.executeQueryForList("getSelectRenewalInvestmentsDepositTO", map);
        if (renewalDepositList != null && renewalDepositList.size() > 0) {
            returnMap.put("RenewalInvestmentsDepositTO", renewalDepositList);
        }
        List shareList = (List) sqlMap.executeQueryForList("getSelectInvestmentsShareTO", map);
        if (shareList != null && shareList.size() > 0) {
            returnMap.put("InvestmentsShareTO", shareList);
        }
        List reserveFundList = (List) sqlMap.executeQueryForList("getSelectInvestmentsRFTO", map);
        if (reserveFundList != null && reserveFundList.size() > 0) {
            returnMap.put("InvestmentsRFTO", reserveFundList);
        }

        if (map.get("ACTION_TYPE").equals("Authorize")) {
            HashMap param = new HashMap();
            param.put("INITIATED_BRANCH", _branchCode);
            param.put("INVESTMENT_ID", map.get("INVESTMENT_ID"));
            param.put("TRANS_DT", getProperDateFormat(map.get("TRANS_DT")));
            List transList = (List) sqlMap.executeQueryForList("getDataForInvestmentTransTO", param);
            if (transList != null && transList.size() > 0) {
                returnMap.put("InvestmentsTransTO", transList);
                // Get Trans_Details
                HashMap getRemitTransMap = new HashMap();
                getRemitTransMap.put("TRANS_ID", map.get("INVESTMENT_ID"));
                getRemitTransMap.put("TRANS_DT", currDt.clone());
                getRemitTransMap.put("BRANCH_CODE", _branchCode);
                List remitList = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", getRemitTransMap);
                returnMap.put("TransactionTO", remitList);
                remitList = null;
            }
            transList = null;
        }
        System.out.println("###### returnMap : " + returnMap);
        return returnMap;
    }

    /*
     * This is used to free up the memory used by SharePrductTO object
     */
    private void destroyObjects() {
        objTO = null;
        objInvestmentsOperativeTO = null;
        objInvestmentsDepositTO = null;
        objInvestmentsShareTO = null;
        objInvestmentsRFTO = null;
        objInvestmentsChequeTO = null;
        loanDataMap = null;
        tableDetails = null;
        deletedTableValues = null;
        deletedLoanDataMap = null;
        objInvestmentsTransTO = null;
        backDate = null;
    }
//    /* This method is used to execute a query to get all the inserted datas in
//     *the table and retrun the resultset as a HashMap **/
//    public HashMap executeQuery(HashMap obj) throws Exception {
//        System.out.println("obj------------------->"+obj);
//        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
//        where = (String) obj.get(CommonConstants.MAP_WHERE);
//        HashMap investmentMap =  getInvestmentMasterData();
//        HashMap data = new HashMap();
//        data.put("InvestmentsMasterTO", investmentMap);
//        
//        return data;
//    }
//    /** This method execute a Query and returns the resultset in HashMap object */
//    private HashMap getInvestmentMasterData() throws Exception {
//        HashMap returnMap = new HashMap();
////        List list = (List) sqlMap.executeQueryForList("getSelectInvestmentMasterTO", where);
////        returnMap.put("InvestmentsMasterTO", list);
//        List OperativeList = (List) sqlMap.executeQueryForList("getSelectInvestmentsOperativeTO", where);
//        returnMap.put("InvestmentsOperativeTO", OperativeList);
//
//        OperativeList = null;
//        
//        return returnMap;
//    }

    /**
     * This is used to Insert,Update,Delete the loan related detials in the
     * table *
     */
//    private void addLoanData()throws Exception{
//        if(loanDataMap!=null){
//            ArrayList list = new ArrayList(loanDataMap.keySet());
//            if(list.size() != 0){
//                for(int i=0; i<list.size();i++){
//                    objLoanTO = (ShareProductLoanTO)loanDataMap.get(CommonUtil.convertObjToStr(list.get(i)));
//                    if(objLoanTO.getStatus().equals(CommonConstants.STATUS_CREATED)){
//                        sqlMap.executeUpdate("insertShareProductLoanTO", objLoanTO);
//                    }else if(objLoanTO.getStatus().equals(CommonConstants.STATUS_MODIFIED)){
//                        sqlMap.executeUpdate("updateShareProductLoanTO", objLoanTO);
//                    }
//                }
//            }
//        }
//        if(deletedLoanDataMap != null){
//            ArrayList deletedList = new ArrayList(deletedLoanDataMap.keySet());
//            if(deletedList.size() != 0){
//                for(int j=0;j<deletedList.size();j++){
//                    objLoanTO = (ShareProductLoanTO)deletedLoanDataMap.get(CommonUtil.convertObjToStr(deletedList.get(j)));
//                    sqlMap.executeUpdate("deleteShareProductLoanTO", objLoanTO);
//                }
//            }
//        }
//    }
//    
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
}
