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
package com.see.truetransact.serverside.accountswithotherbank;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import com.see.truetransact.commonutil.TTException;

import com.ibatis.db.sqlmap.SqlMap;
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
import com.see.truetransact.transferobject.accountswithotherbank.AccountswithOtherBanksOperativeTO;

/**
 * InvestmentsMaster DAO.
 *
 */
public class AccountswithOtherBanksDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private InvestmentsMasterTO objTO;
    private AccountswithOtherBanksOperativeTO objInvestmentsOperativeTO;
    private InvestmentsDepositTO objInvestmentsDepositTO;
    private InvestmentsShareTO objInvestmentsShareTO;
    private InvestmentsRFTO objInvestmentsRFTO;
    private InvestmentsChequeTO objInvestmentsChequeTO;
//    private ShareProductLoanTO objLoanTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private InvestmentsTransTO objInvestmentsTransTO;
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

    /**
     * Creates a new instance of ShareProductDAO
     */
    public AccountswithOtherBanksDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private String getInvestmentId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "ACT_ID");
        String investmentId = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return investmentId;
    }

    /**
     * This method is used to insertnew datat into the Table
     */
    private void insertData(HashMap map) throws Exception {
        investmentID = "";
        investmentID = getInvestmentId();
        System.out.println("objInvestmentsOperativeTO______" + objInvestmentsOperativeTO);
        if (objInvestmentsOperativeTO != null) {
            System.out.println("investmentID" + investmentID);
            logTO.setData(objInvestmentsOperativeTO.toString());
            logTO.setPrimaryKey(objInvestmentsOperativeTO.getKeyData());
            logTO.setStatus(objInvestmentsOperativeTO.getCommand());
            logDAO.addToLog(logTO);
            objInvestmentsOperativeTO.setInvestmentId(investmentID);
            objInvestmentsOperativeTO.setAmt(0.0);
            objInvestmentsOperativeTO.setLast_trans_dt(currDt);
            objInvestmentsOperativeTO.setAct_status("NEW");
            objInvestmentsOperativeTO.setBranchCode(_branchCode);
            //objInvestmentsTransTO.setInvestment_internal_Id(investmentID);
            System.out.println("objInvestmentsOperativeTO" + objInvestmentsOperativeTO);
            sqlMap.executeUpdate("insertotherBanksActOperativeTO", objInvestmentsOperativeTO);
            // sqlMap.executeUpdate("insertInvestmentsMasterTO", objInvestmentsOperativeTO);
            if (map.containsKey("chequeBookDetails")) {
                insertChequeDetails();
            }
            if (objInvestmentsOperativeTO.getDepositAmt() > 0.0) {
                objInvestmentsOperativeTO.setAmt(objInvestmentsOperativeTO.getDepositAmt());
            } else if (objInvestmentsOperativeTO.getSanctnAmt() > 0.0) {
                objInvestmentsOperativeTO.setAmt(objInvestmentsOperativeTO.getSanctnAmt());
            }
            System.out.println("amttttttttt" + objInvestmentsOperativeTO.getAmt());
        } else if (objInvestmentsDepositTO != null) {
            logTO.setData(objInvestmentsDepositTO.toString());
            logTO.setPrimaryKey(objInvestmentsDepositTO.getKeyData());
            logTO.setStatus(objInvestmentsDepositTO.getCommand());
            logDAO.addToLog(logTO);
            objInvestmentsDepositTO.setInvestmentId(investmentID);
            objInvestmentsTransTO.setInvestment_internal_Id(investmentID);
            sqlMap.executeUpdate("insertInvestmentsDepositTO", objInvestmentsDepositTO);
            sqlMap.executeUpdate("insertInvestmentsMasterTO", objInvestmentsDepositTO);
        } else if (objInvestmentsShareTO != null) {
            logTO.setData(objInvestmentsShareTO.toString());
            logTO.setPrimaryKey(objInvestmentsShareTO.getKeyData());
            logTO.setStatus(objInvestmentsShareTO.getCommand());
            logDAO.addToLog(logTO);
            objInvestmentsShareTO.setInvestmentId(investmentID);
            objInvestmentsTransTO.setInvestment_internal_Id(investmentID);
            sqlMap.executeUpdate("insertInvestmentsShareTO", objInvestmentsShareTO);
            sqlMap.executeUpdate("insertInvestmentsMasterTO", objInvestmentsShareTO);
        } else if (objInvestmentsRFTO != null) {
            logTO.setData(objInvestmentsRFTO.toString());
            logTO.setPrimaryKey(objInvestmentsRFTO.getKeyData());
            logTO.setStatus(objInvestmentsRFTO.getCommand());
            logDAO.addToLog(logTO);
            objInvestmentsRFTO.setInvestmentId(investmentID);
            objInvestmentsTransTO.setInvestment_internal_Id(investmentID);
            sqlMap.executeUpdate("insertInvestmentsRFTO", objInvestmentsRFTO);
            sqlMap.executeUpdate("insertInvestmentsMasterTO", objInvestmentsRFTO);
        }

        // Transaction Part
        System.out.println("##################### objInvestmentsTransTO : " + objInvestmentsTransTO);
        if(objInvestmentsOperativeTO.getInvestmentType().equalsIgnoreCase("BR")){ // Added by nithya on 23-09-2019 for KD-621 Borrowings master transaction issue 
          doTransactionForBorrowings(map);  
        }else{
        doTransaction(map);
        }
        System.out.println("##################### Map : " + map);
        if (loanDataMap != null && objInvestmentsTransTO != null) {
            objInvestmentsTransTO.setBatchID(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
            objInvestmentsTransTO.setInitiatedBranch(_branchCode);
            sqlMap.executeUpdate("insertInvestmentTransTO", objInvestmentsTransTO);
        }
    }

    private HashMap doTransaction(HashMap map) throws Exception {
        System.out.println("In Side The DoTransaction");
        if (map.containsKey("TransactionTO") && map.get("TransactionTO") != null) {
            System.out.println("TransactionTO");
            HashMap achdMap = new HashMap();
            String investMentType = "";
            investMentType = CommonUtil.convertObjToStr(objInvestmentsOperativeTO.getInvestmentType());
            achdMap.put("ACCOUNT_TYPE", CommonUtil.convertObjToStr(objInvestmentsOperativeTO.getInvestmentType()));
            achdMap.put("PROD_ID", CommonUtil.convertObjToStr(objInvestmentsOperativeTO.getInvestmentProdId()));
            System.out.println("achdMap before query ------------>" + achdMap);
            List achdLst = ServerUtil.executeQuery("getSelectOtherBankAccountHead", achdMap);
            if (achdLst != null && achdLst.size() > 0) {
                achdMap = new HashMap();
                ArrayList transferList = new ArrayList();
                ArrayList cashList = new ArrayList();
                achdMap = (HashMap) achdLst.get(0);
                System.out.println("achdMap--------------->" + achdMap);
                TransferTrans objTransferTrans = new TransferTrans();
                objTransferTrans.setInitiatedBranch(_branchCode);
                objTransferTrans.setLinkBatchId(objInvestmentsOperativeTO.getInvestmentId());
                HashMap txMap = new HashMap();
                //   if(!objInvestmentsTransTO.getTrnCode().equals("")&&map.containsKey("TransactionTO")){
                if (map.containsKey("TransactionTO")) {
                    LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    if (TransactionDetailsMap.size() > 0) {
//                    if(objInvestmentsTransTO.getTrnCode().equals("Deposit") ){      //DEPOSIT AMOUNT TRANSACTION
                        if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                            LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                            for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                double tranAmt = 0.0;
                                objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                if (investMentType.equals("OTHER_BANK_SB") || investMentType.equals("OTHER_BANK_CA") || investMentType.equals("CC") || investMentType.equals("OD")) {
                                    if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                        if(map.containsKey("ACCT_STATUS") && CommonUtil.convertObjToStr(map.get("ACCT_STATUS")).equalsIgnoreCase("CLOSED")){
                                            txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                            txMap.put(TransferTrans.DR_ACT_NUM,CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                            txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                            txMap.put(TransferTrans.PARTICULARS, objInvestmentsOperativeTO.getInvestmentType() + "_" + objInvestmentsOperativeTO.getInvestmentProdDesc()+ " - " +"Deposit");
                                            txMap.put("AUTHORIZEREMARKS","TRANSACTIONAMOUNT");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.DR_INST_TYPE,"VOUCHER");
                                            if(CommonUtil.convertObjToStr(objTransactionTO.getProductType()).equals("GL")){
                                                txMap.put(TransferTrans.DR_AC_HD,CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                            }
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                            tranAmt=CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                            if (objTransactionTO.getProductType().equals("OA")){
                                                    txMap.put("TRANS_MOD_TYPE", "OA");
                                            }else if(objTransactionTO.getProductType().equals("AB")){
                                                    txMap.put("TRANS_MOD_TYPE", "AB");
                                            }else if(objTransactionTO.getProductType().equals("SA")){
                                                    txMap.put("TRANS_MOD_TYPE", "SA");
                                            }else if(objTransactionTO.getProductType().equals("TL")){
                                                    txMap.put("TRANS_MOD_TYPE", "TL");
                                            }else if(objTransactionTO.getProductType().equals("AD")){
                                                    txMap.put("TRANS_MOD_TYPE", "AD");
                                            }else
                                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                            txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap,tranAmt));
                                            if(objInvestmentsOperativeTO.getDepositAmt().doubleValue()!=0.0){
                                                txMap.put(TransferTrans.CR_ACT_NUM,objInvestmentsOperativeTO.getInvestmentId());
                                                txMap.put(TransferTrans.CR_AC_HD, (String)achdMap.get("PRINCIPAL_AC_HD"));
                                                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);
                                                txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId())); // Added by nithya on 14-03-2017 for 5974
                                                txMap.put("AUTHORIZEREMARKS","AB_PRINCIPAL_AC_HD");
                                                txMap.put(TransferTrans.PARTICULARS, objInvestmentsOperativeTO.getInvestmentType() + "_" + objInvestmentsOperativeTO.getInvestmentProdDesc()+ " - " +"Deposit");
                                                txMap.put(CommonConstants.USER_ID, objInvestmentsOperativeTO.getStatusBy());
                                                txMap.put("TRANS_MOD_TYPE", "AB");
                                                txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                                transferList.add(objTransferTrans.getCreditTransferTO(txMap, objInvestmentsOperativeTO.getDepositAmt()));
                                            }
                                        }else{
                                            txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                            txMap.put(TransferTrans.CR_ACT_NUM,CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                            txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                            txMap.put(TransferTrans.PARTICULARS, objInvestmentsOperativeTO.getInvestmentType() + "_" + objInvestmentsOperativeTO.getInvestmentProdDesc()+ " - " +"Deposit");
                                            txMap.put("AUTHORIZEREMARKS","TRANSACTIONAMOUNT");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.DR_INST_TYPE,"VOUCHER");
                                            if(CommonUtil.convertObjToStr(objTransactionTO.getProductType()).equals("GL")){
                                                txMap.put(TransferTrans.CR_AC_HD,CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                            }
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            tranAmt=CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                            if (objTransactionTO.getProductType().equals("OA")){
                                                    txMap.put("TRANS_MOD_TYPE", "OA");
                                            }else if(objTransactionTO.getProductType().equals("AB")){
                                                    txMap.put("TRANS_MOD_TYPE", "AB");
                                            }else if(objTransactionTO.getProductType().equals("SA")){
                                                    txMap.put("TRANS_MOD_TYPE", "SA");
                                            }else if(objTransactionTO.getProductType().equals("TL")){
                                                    txMap.put("TRANS_MOD_TYPE", "TL");
                                            }else if(objTransactionTO.getProductType().equals("AD")){
                                                    txMap.put("TRANS_MOD_TYPE", "AD");
                                            }else
                                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                             txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap,tranAmt));
                                            if(objInvestmentsOperativeTO.getAmt().doubleValue()!=0.0){
                                                //txMap.put(TransferTrans.CR_ACT_NUM,objInvestmentsOperativeTO.getInvestmentId());
                                                txMap.put(TransferTrans.DR_AC_HD, (String)achdMap.get("PRINCIPAL_AC_HD"));
                                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);
                                                txMap.put("AUTHORIZEREMARKS","AB_PRINCIPAL_AC_HD");
                                                txMap.put(TransferTrans.PARTICULARS, objInvestmentsOperativeTO.getInvestmentType() + "_" + objInvestmentsOperativeTO.getInvestmentProdDesc()+ " - " +"Deposit");
                                                txMap.put(CommonConstants.USER_ID, objInvestmentsOperativeTO.getStatusBy());
                                                txMap.put("TRANS_MOD_TYPE", "AB");
                                                txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, objInvestmentsOperativeTO.getAmt()));
                                            }
                                        }                                        
                                        System.out.println("transferList  ------------->" + transferList);
                                    } else {
                                        if(map.containsKey("ACCT_STATUS") && CommonUtil.convertObjToStr(map.get("ACCT_STATUS")).equalsIgnoreCase("CLOSED")){
                                            System.out.println("objInvestmentsOperativeTO....Amt" + objInvestmentsOperativeTO.getDepositAmt());
                                            HashMap investmentTransMap = new HashMap();
                                            investmentTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                                            investmentTransMap.put("BRANCH_CODE", _branchCode);
                                            investmentTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                            investmentTransMap.put("USER_ID", objInvestmentsOperativeTO.getStatusBy());
                                            investmentTransMap.put("INVESTMENT_NO", objInvestmentsOperativeTO.getInvestmentType() + "_" + objInvestmentsOperativeTO.getInvestmentProdDesc());
                                            if (objInvestmentsOperativeTO.getDepositAmt().doubleValue() != 0.0) {
                                                investmentTransMap.put("ACT_NUM", objInvestmentsOperativeTO.getInvestmentId());
                                                investmentTransMap.put("ACCT_HEAD", achdMap.get("PRINCIPAL_AC_HD"));
                                                investmentTransMap.put("TRANS_AMOUNT", objInvestmentsOperativeTO.getDepositAmt());
                                                investmentTransMap.put("AUTHORIZEREMARKS", "OTHER_BANKS_AC_HD");
                                                investmentTransMap.put("TRN_CODE", "Deposit");
                                                investmentTransMap.put("TRANS_MOD_TYPE", "AB");
                                                investmentTransMap.put(CommonConstants.SCREEN,CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                                cashList.add(createCashDebitTransactionTO(investmentTransMap,CommonConstants.CREDIT));
                                            }
                                            System.out.print("###### cashList: in if" + cashList);
                                        }else{
                                            System.out.println("objInvestmentsOperativeTO....Amt" + objInvestmentsOperativeTO.getAmt());
                                            HashMap investmentTransMap = new HashMap();
                                            investmentTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                                            investmentTransMap.put("BRANCH_CODE", _branchCode);
                                            investmentTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                            investmentTransMap.put("USER_ID", objInvestmentsOperativeTO.getStatusBy());
                                            investmentTransMap.put("INVESTMENT_NO", objInvestmentsOperativeTO.getInvestmentType() + "_" + objInvestmentsOperativeTO.getInvestmentProdDesc());
                                            if (objInvestmentsOperativeTO.getAmt().doubleValue() != 0.0) {
                                                investmentTransMap.put("ACT_NUM", objInvestmentsOperativeTO.getInvestmentId());
                                                investmentTransMap.put("ACCT_HEAD", achdMap.get("PRINCIPAL_AC_HD"));
                                                investmentTransMap.put("TRANS_AMOUNT", objInvestmentsOperativeTO.getAmt());
                                                investmentTransMap.put("AUTHORIZEREMARKS", "OTHER_BANKS_AC_HD");
                                                investmentTransMap.put("TRN_CODE", "Deposit");
                                                investmentTransMap.put("TRANS_MOD_TYPE", "AB");
                                                investmentTransMap.put(CommonConstants.SCREEN,CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                                cashList.add(createCashDebitTransactionTO(investmentTransMap,CommonConstants.DEBIT));
                                            }
                                            System.out.print("###### cashList in else: " + cashList);
                                        }
                                    }
                                }
                            }
                        }

                        System.out.println("transferList total ------------->" + transferList);
//                    }
//                    else if(objInvestmentsTransTO.getTrnCode().equals("Purchase") ){      //SHARE TRANSACTION
//                        if(TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
//                            LinkedHashMap allowedTransDetailsTO = (LinkedHashMap)TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
//                            for (int J = 1;J <= allowedTransDetailsTO.size();J++){
//                                double tranAmt=0.0;
//                                objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
//                                if(investMentType.equals("SHARES_DCB") || investMentType.equals("SHARE_OTHER_INSTITUTIONS")){
//                                    if (objTransactionTO.getTransType().equals("TRANSFER")) {
//                                        txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
//                                        txMap.put(TransferTrans.CR_ACT_NUM,CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
//                                        txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
//                                        txMap.put(TransferTrans.PARTICULARS, objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName()+ " - " +"Purchase");
//                                        txMap.put("AUTHORIZEREMARKS","TRANSACTIONAMOUNT");
//                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
//                                        txMap.put(TransferTrans.DR_INST_TYPE,"VOUCHER");                                        
//                                        if(CommonUtil.convertObjToStr(objTransactionTO.getProductType()).equals("GL")){
//                                            txMap.put(TransferTrans.CR_AC_HD,CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
//                                        }
//                                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
//                                        tranAmt=CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
//                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap,tranAmt));
//                                        if(objInvestmentsTransTO.getInvestmentAmount().doubleValue()!=0.0){
//                                            double feesPaid =0.0;
//                                            double shareAmt =0.0;
//                                            shareAmt = objInvestmentsTransTO.getInvestmentAmount().doubleValue();
//                                            feesPaid = objInvestmentsTransTO.getPurchaseRate().doubleValue();
//                                            System.out.println("#### Total Share Amount : "+shareAmt);
//                                            System.out.println("#### feesPaid : "+feesPaid);
//                                            if(feesPaid>0){
//                                                txMap.put(TransferTrans.DR_AC_HD, (String)achdMap.get("CHARGE_PAID_AC_HD"));
//                                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
//                                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
//                                                txMap.put("AUTHORIZEREMARKS","SHARE_FEES_AC_HD");
//                                                txMap.put(TransferTrans.PARTICULARS, objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName()+ " - " +"Purchase");
//                                                txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
//                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, feesPaid));
//                                                shareAmt-=feesPaid;
//                                            }
//                                            if(shareAmt>0){
//                                                txMap.put(TransferTrans.DR_AC_HD, (String)achdMap.get("IINVESTMENT_AC_HD"));
//                                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
//                                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
//                                                txMap.put("AUTHORIZEREMARKS","SHARE_IINVESTMENT_AC_HD");
//                                                txMap.put(TransferTrans.PARTICULARS, objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName()+ " - " +"Purchase");
//                                                txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
//                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, shareAmt));
//                                            }
//                                            System.out.println("transferList  ------------->"+transferList);
//                                        }
//                                    }else {
//                                        HashMap investmentTransMap = new HashMap();
//                                        investmentTransMap.put("SELECTED_BRANCH_ID", _branchCode);
//                                        investmentTransMap.put("BRANCH_CODE", _branchCode);
//                                        investmentTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
//                                        investmentTransMap.put("USER_ID",  objInvestmentsTransTO.getStatusBy());
//                                        investmentTransMap.put("INVESTMENT_NO",  objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName());
//                                        if(objInvestmentsTransTO.getInvestmentAmount().doubleValue()!=0.0){
//                                            double feesPaid =0.0;
//                                            double shareAmt =0.0;
//                                            shareAmt = objInvestmentsTransTO.getInvestmentAmount().doubleValue();
//                                            feesPaid = objInvestmentsTransTO.getPurchaseRate().doubleValue();
//                                            System.out.println("#### Total Share Amount : "+shareAmt);
//                                            System.out.println("#### feesPaid : "+feesPaid);
//                                            if(feesPaid>0){
//                                                investmentTransMap.put("ACCT_HEAD", achdMap.get("CHARGE_PAID_AC_HD"));
//                                                investmentTransMap.put("TRANS_AMOUNT", String.valueOf(feesPaid));
//                                                investmentTransMap.put("AUTHORIZEREMARKS","SHARE_FEES_AC_HD");
//                                                investmentTransMap.put("TRN_CODE","Purchase");
//                                                cashList.add(createCashDebitTransactionTO(investmentTransMap));
//                                                shareAmt-=feesPaid;
//                                            }
//                                            System.out.println("####Share Amount : "+shareAmt);
//                                            if(shareAmt>0){
//                                                investmentTransMap.put("ACCT_HEAD", achdMap.get("IINVESTMENT_AC_HD"));
//                                                investmentTransMap.put("TRANS_AMOUNT", String.valueOf(shareAmt));
//                                                investmentTransMap.put("AUTHORIZEREMARKS","SHARE_IINVESTMENT_AC_HD");
//                                                investmentTransMap.put("TRN_CODE","Purchase");
//                                                cashList.add(createCashDebitTransactionTO(investmentTransMap));
//                                            }
//                                            System.out.print("###### cashList: "+cashList);
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }

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
        }
        returnDataMap.put("WITHOUT_TRANSACTION", "WITHOUT_TRANSACTION");
        System.out.println("returnDataMap" + returnDataMap);
        return returnDataMap;

    }

    // Added by nithya on 23-09-2019 for KD-621 Borrowings master transaction issue
    private HashMap doTransactionForBorrowings(HashMap map) throws Exception {
        System.out.println("In Side The doTransactionForBorrowings");
        if (map.containsKey("TransactionTO") && map.get("TransactionTO") != null) {
            System.out.println("TransactionTO");
            HashMap achdMap = new HashMap();
            String investMentType = "";
            investMentType = CommonUtil.convertObjToStr(objInvestmentsOperativeTO.getInvestmentType());
            achdMap.put("ACCOUNT_TYPE", CommonUtil.convertObjToStr(objInvestmentsOperativeTO.getInvestmentType()));
            achdMap.put("PROD_ID", CommonUtil.convertObjToStr(objInvestmentsOperativeTO.getInvestmentProdId()));            
            List achdLst = ServerUtil.executeQuery("getSelectOtherBankAccountHead", achdMap);
            if (achdLst != null && achdLst.size() > 0) {
                achdMap = new HashMap();
                ArrayList transferList = new ArrayList();
                ArrayList cashList = new ArrayList();
                achdMap = (HashMap) achdLst.get(0);                
                TransferTrans objTransferTrans = new TransferTrans();
                objTransferTrans.setInitiatedBranch(_branchCode);
                objTransferTrans.setLinkBatchId(objInvestmentsOperativeTO.getInvestmentId());
                HashMap txMap = new HashMap();
                if (map.containsKey("TransactionTO")) {
                    LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    if (TransactionDetailsMap.size() > 0) {
                        if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                            LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                            for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                double tranAmt = 0.0;
                                objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                    if (map.containsKey("ACCT_STATUS") && CommonUtil.convertObjToStr(map.get("ACCT_STATUS")).equalsIgnoreCase("CLOSED")) {
                                        txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                        txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                        //txMap.put(TransferTrans.PARTICULARS, objInvestmentsOperativeTO.getInvestmentType() + "_" + objInvestmentsOperativeTO.getInvestmentProdDesc() + " - " + "Deposit");
                                        txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()) + "-" + objTransactionTO.getProductType());
                                        txMap.put("AUTHORIZEREMARKS", "TRANSACTIONAMOUNT");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
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
                                        txMap.put("SCREEN_NAME", CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, tranAmt));
                                        if (objInvestmentsOperativeTO.getDepositAmt().doubleValue()!=0.0) {
                                            //txMap.put(TransferTrans.CR_ACT_NUM,objInvestmentsOperativeTO.getInvestmentId());
                                            txMap.put(TransferTrans.DR_AC_HD, (String) achdMap.get("PRINCIPAL_AC_HD"));
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);
                                            txMap.put("AUTHORIZEREMARKS", "AB_PRINCIPAL_AC_HD");
                                            txMap.put(TransferTrans.PARTICULARS, objInvestmentsOperativeTO.getInvestmentType() + "_" + objInvestmentsOperativeTO.getInvestmentProdDesc() + " - " + "Borrowings");
                                            txMap.put(CommonConstants.USER_ID, objInvestmentsOperativeTO.getStatusBy());
                                            txMap.put("TRANS_MOD_TYPE", "BR");
                                            txMap.put("SCREEN_NAME", CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objInvestmentsOperativeTO.getDepositAmt()));
                                        }
                                    } else {
                                        txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                        txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                        txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()) + "-" + objTransactionTO.getProductType());
                                        txMap.put("AUTHORIZEREMARKS", "TRANSACTIONAMOUNT");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
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
                                        txMap.put("SCREEN_NAME", CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, tranAmt));
                                        if (objInvestmentsOperativeTO.getDepositAmt().doubleValue() != 0.0) {
                                            txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("PRINCIPAL_AC_HD"));
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);
                                            txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId())); // Added by nithya on 14-03-2017 for 5974
                                            txMap.put("AUTHORIZEREMARKS", "AB_PRINCIPAL_AC_HD");
                                            txMap.put(TransferTrans.PARTICULARS, objInvestmentsOperativeTO.getInvestmentType() + "_" + objInvestmentsOperativeTO.getInvestmentProdDesc() + " - " + "Borrowings");
                                            txMap.put(CommonConstants.USER_ID, objInvestmentsOperativeTO.getStatusBy());
                                            txMap.put("TRANS_MOD_TYPE", "BR");
                                            txMap.put("SCREEN_NAME", CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objInvestmentsOperativeTO.getDepositAmt()));
                                        }
                                    }
                                    //System.out.println("transferList  ------------->" + transferList);
                                } else {
                                    if (map.containsKey("ACCT_STATUS") && CommonUtil.convertObjToStr(map.get("ACCT_STATUS")).equalsIgnoreCase("CLOSED")) {
                                        System.out.println("objInvestmentsOperativeTO....Amt" + objInvestmentsOperativeTO.getDepositAmt());
                                        HashMap investmentTransMap = new HashMap();
                                        investmentTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                                        investmentTransMap.put("BRANCH_CODE", _branchCode);
                                        investmentTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                        investmentTransMap.put("USER_ID", objInvestmentsOperativeTO.getStatusBy());
                                        investmentTransMap.put("INVESTMENT_NO", objInvestmentsOperativeTO.getInvestmentType() + "_" + objInvestmentsOperativeTO.getInvestmentProdDesc() +" - " + "Borrowings");
                                        if (objInvestmentsOperativeTO.getDepositAmt().doubleValue() != 0.0) {
                                            investmentTransMap.put("ACT_NUM", objInvestmentsOperativeTO.getInvestmentId());
                                            investmentTransMap.put("ACCT_HEAD", achdMap.get("PRINCIPAL_AC_HD"));
                                            investmentTransMap.put("TRANS_AMOUNT", objInvestmentsOperativeTO.getDepositAmt());
                                            investmentTransMap.put("AUTHORIZEREMARKS", "OTHER_BANKS_AC_HD");
                                            investmentTransMap.put("TRN_CODE", "Deposit");
                                            investmentTransMap.put("TRANS_MOD_TYPE", "BR");
                                            investmentTransMap.put(CommonConstants.SCREEN, CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                            cashList.add(createCashDebitTransactionTO(investmentTransMap, CommonConstants.DEBIT));
                                        }
                                        System.out.print("###### cashList in else: " + cashList);
                                    } else {
                                        System.out.println("objInvestmentsOperativeTO....Amt" + objInvestmentsOperativeTO.getDepositAmt());
                                            HashMap investmentTransMap = new HashMap();
                                            investmentTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                                            investmentTransMap.put("BRANCH_CODE", _branchCode);
                                            investmentTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                            investmentTransMap.put("USER_ID", objInvestmentsOperativeTO.getStatusBy());
                                            investmentTransMap.put("INVESTMENT_NO", objInvestmentsOperativeTO.getInvestmentType() + "_" + objInvestmentsOperativeTO.getInvestmentProdDesc() + " - " + "Borrowings");
                                            if (objInvestmentsOperativeTO.getDepositAmt().doubleValue() != 0.0) {
                                                investmentTransMap.put("ACT_NUM", objInvestmentsOperativeTO.getInvestmentId());
                                                investmentTransMap.put("ACCT_HEAD", achdMap.get("PRINCIPAL_AC_HD"));
                                                investmentTransMap.put("TRANS_AMOUNT", objInvestmentsOperativeTO.getDepositAmt());
                                                investmentTransMap.put("AUTHORIZEREMARKS", "OTHER_BANKS_AC_HD");
                                                investmentTransMap.put("TRN_CODE", "Deposit");
                                                investmentTransMap.put("TRANS_MOD_TYPE", "BR");
                                                investmentTransMap.put(CommonConstants.SCREEN,CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                                cashList.add(createCashDebitTransactionTO(investmentTransMap,CommonConstants.CREDIT));
                                            }
                                            System.out.print("###### cashList: in if" + cashList);
                                    }
                                }

                            }
                        }
                        //System.out.println("transferList total ------------->" + transferList);

                        if (cashList != null && cashList.size() > 0) {
                            doCashTrans(cashList, _branchCode, false);
                        }
                        if (transferList != null && transferList.size() > 0) {
                            doDebitCredit(transferList, _branchCode, false);
                        }
                        transferList = null;
                        cashList = null;
                        getTransDetails(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
                        returnDataMap.put("BATCH_ID", CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
                    } else {
                        throw new Exception("investment  Transaction is not proper");
                    }
                }

            } else {
                throw new Exception("investment  Config Date is Not set...");
            }
        }
        returnDataMap.put("WITHOUT_TRANSACTION", "WITHOUT_TRANSACTION");
        System.out.println("returnDataMap" + returnDataMap);
        return returnDataMap;

    }

    
    
    private CashTransactionTO createCashDebitTransactionTO(HashMap dataMap,String transType) throws Exception {
        CashTransactionTO objCashTO = new CashTransactionTO();
        if (dataMap.get("STATUS").equals(CommonConstants.TOSTATUS_INSERT)) {
            objCashTO.setActNum(CommonUtil.convertObjToStr(dataMap.get("ACT_NUM")));
            objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
            objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.OTHERBANKACTS));
            objCashTO.setTransType(transType);
            objCashTO.setParticulars("By Cash : "+dataMap.get("INVESTMENT_NO")+" - "+dataMap.get("TRN_CODE"));
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
            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
            objCashTO.setCommand("INSERT");
            objCashTO.setScreenName(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.SCREEN)));
            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(objInvestmentsOperativeTO.getInvestmentId()));
            System.out.println("objCashTO 1st one:" + objCashTO);
        }
        return objCashTO;
    }
    
    private void doCashTrans(ArrayList batchList, String branchCode, boolean isAutoAuthorize) throws Exception {
        CashTransactionDAO cashDAO = new CashTransactionDAO();
        HashMap data = new HashMap();
        data.put("DAILYDEPOSITTRANSTO", batchList);
        data.put("COMMAND", objInvestmentsOperativeTO.getCommand());
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
        if (isAutoAuthorize != true) {
            if (objTransactionTO == null) {
                objTransactionTO = new TransactionTO();
            }
            System.out.println("loanDataMap---------------->" + loanDataMap);
            objTransactionTO.setBatchId(CommonUtil.convertObjToStr(objInvestmentsOperativeTO.getInvestmentId()));
            objTransactionTO.setBatchDt(currDt);
            System.out.println("objInvestmentsTransTO.getInvestmentName()---------------->" + objInvestmentsOperativeTO.getInvestmentType());
            String val = (String) objInvestmentsOperativeTO.getInvestmentType();
            if (val.length() > 15) {
                val = (String) objInvestmentsTransTO.getInvestmentName().substring(0, 15);
            }
            objTransactionTO.setTransId(CommonUtil.convertObjToStr(objInvestmentsOperativeTO.getInvestmentId()));
            System.out.println("objTransactionTO.getTransId()---------------->" + val);
            objTransactionTO.setStatus(objInvestmentsOperativeTO.getStatus());
            objTransactionTO.setBranchId(_branchCode);
            System.out.println("objTransactionTO------------------->" + objTransactionTO);
            if (CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue() > 0) {
                sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);
            }
            objTransactionTO = null;
            HashMap linkBatchMap = new HashMap();                           //update LinkBatchId
            linkBatchMap.put("LINK_BATCH_ID", objInvestmentsOperativeTO.getInvestmentId());
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
        data.put("COMMAND", objInvestmentsOperativeTO.getCommand());
        data.put("INITIATED_BRANCH", branchCode);
        data.put(CommonConstants.BRANCH_ID, branchCode);
        data.put(CommonConstants.USER_ID, logTO.getUserId());
        data.put(CommonConstants.MODULE, "Transaction");
        data.put(CommonConstants.SCREEN, "");
        data.put("MODE", "MODE");
        data.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(objInvestmentsOperativeTO.getInvestmentId()));
        if (isAutoAuthorize) {
            HashMap authorizeMap = new HashMap();
            authorizeMap.put("BATCH_ID", null);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
            data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
        }
        loanDataMap = transferDAO.execute(data, false);
        if (isAutoAuthorize != true) {
            System.out.println("loanDataMap---------------->" + loanDataMap);
            if (objTransactionTO == null) {
                objTransactionTO = new TransactionTO();
            }
            objTransactionTO.setBatchId(CommonUtil.convertObjToStr(objInvestmentsOperativeTO.getInvestmentId()));
//            objTransactionTO.setBatchId(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
            objTransactionTO.setBatchDt(currDt);
            //   System.out.println("objInvestmentsTransTO.getInvestmentName()---------------->"+objInvestmentsTransTO.getInvestmentName());
            String val = (String) objInvestmentsOperativeTO.getInvestmentType();
            if (val.length() > 15) {
                val = (String) objInvestmentsOperativeTO.getInvestmentType().substring(0, 15);
            }
//            objTransactionTO.setTransId(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
            objTransactionTO.setTransId(CommonUtil.convertObjToStr(objInvestmentsOperativeTO.getInvestmentId()));
            System.out.println("objTransactionTO.getTransId()---------------->" + val);
            objTransactionTO.setStatus(objInvestmentsOperativeTO.getStatus());
            objTransactionTO.setBranchId(_branchCode);
            System.out.println("objTransactionTO------------------->" + objTransactionTO);
            if (CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue() > 0) {
                sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);
            }
            objTransactionTO = null;
        }
    }

    private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
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
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put("AUTHORIZE_STATUS", "AUTHORIZE_STATUS");
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        returnDataMap = new HashMap();
        List transList = (List) sqlMap.executeQueryForList("getTransferDetails", getTransMap);
        if (transList != null && transList.size() > 0) {
            returnDataMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetails", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            returnDataMap.put("CASH_TRANS_LIST", cashList);
        }
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
            objInvestmentsOperativeTO.setAct_status("MODIFIED");
            objInvestmentsOperativeTO.setLast_trans_dt(currDt);
            sqlMap.executeUpdate("updateABOperativeTO", objInvestmentsOperativeTO);
            if (map.containsKey("chequeBookDetails") || map.containsKey("deletedChequeBookDetails")) {
                updateChequeDetails();
            }
            if(map.containsKey("ACCT_STATUS") && CommonUtil.convertObjToStr(map.get("ACCT_STATUS")).equalsIgnoreCase("CLOSED")){              
                objInvestmentsOperativeTO.setClosedDt((Date)currDt.clone());
                objInvestmentsOperativeTO.setAct_status("CLOSED");
                sqlMap.executeUpdate("updateClosedABOperativeTO", objInvestmentsOperativeTO);     
                if (objInvestmentsOperativeTO.getInvestmentType().equalsIgnoreCase("BR")) {// Added by nithya on 23-09-2019 for KD-621 Borrowings master transaction issue
                    doTransactionForBorrowings(map);
                } else {
                    doTransaction(map);
                }
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
        System.out.println("######### Renewal Map : " + map);
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
                linkBatch_ID = CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestment_internal_Id());
                newPdodId = CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentID());
                newInvType = CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentBehaves());

                objInvestmentsTransTO.setTrnCode("Deposit");
                objInvestmentsTransTO.setTransType(CommonConstants.DEBIT);
                if (oldPrinciPalAmount > 0) {
                    objInvestmentsTransTO.setInvestmentAmount(CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")));
                    objInvestmentsTransTO.setBrokenPeriodInterest(new Double(0.0));
                    renewalDepositTransaction(map);
                    objInvestmentsTransTO.setBatchID(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
                    objInvestmentsTransTO.setInitiatedBranch(_branchCode);
                    if (loanDataMap != null) {
                        double debitAmount = 0.0;
                        if (map.containsKey("RENEWAL_INTEREST_TYPE") && (map.get("RENEWAL_INTEREST_TYPE").equals("WITH_INTEREST") || map.get("RENEWAL_INTEREST_TYPE").equals("WITHOUT_INTEREST"))) {
                            debitAmount = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue() + CommonUtil.convertObjToDouble(map.get("RENEWAL_INTEREST_AMOUNT")).doubleValue();
                        } else {
                            debitAmount = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue() + CommonUtil.convertObjToDouble(map.get("RENEWAL_INTEREST_AMOUNT")).doubleValue()
                                    + CommonUtil.convertObjToDouble(map.get("PARTIAL_INTEREST_AMOUNT")).doubleValue();
                        }
                        objInvestmentsTransTO.setInvestmentAmount(CommonUtil.convertObjToDouble(String.valueOf(debitAmount)));
                        sqlMap.executeUpdate("insertInvestmentTransTO", objInvestmentsTransTO);
                        //Credit
                        objInvestmentsTransTO.setInvestmentAmount(CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")));
                        objInvestmentsTransTO.setTrnCode("Withdrawal");
                        objInvestmentsTransTO.setTransType(CommonConstants.CREDIT);
                        objInvestmentsTransTO.setBrokenPeriodInterest(new Double(0.0));
                        objInvestmentsTransTO.setBrokenPeriodInterest(new Double(0.0));

                        //Insert Withdrawal
                        if (map.containsKey("RENEWAL_NUMBER_TYPE") && map.get("RENEWAL_NUMBER_TYPE").equals("DIFFERENT_PROD_ID")) {
                            objInvestmentsTransTO.setInvestmentID(CommonUtil.convertObjToStr(map.get("OLD_INVESTMENT_ID")));
                            objInvestmentsTransTO.setInvestmentBehaves(CommonUtil.convertObjToStr(map.get("OLD_INVESTMENT_TYPE")));
                            sqlMap.executeUpdate("insertInvestmentTransTO", objInvestmentsTransTO);
                            objInvestmentsTransTO.setInvestmentID(newPdodId);
                            objInvestmentsTransTO.setInvestmentBehaves(newInvType);
                        } else {
                            sqlMap.executeUpdate("insertInvestmentTransTO", objInvestmentsTransTO);
                        }



                    }
                }
                //Interest
                if (map.containsKey("RENEWAL_INTEREST_AMOUNT")) {
                    objInvestmentsTransTO.setTrnCode("Interest");
                    objInvestmentsTransTO.setTransType(CommonConstants.CREDIT);
                    objInvestmentsTransTO.setBrokenPeriodInterest(CommonUtil.convertObjToDouble(map.get("RENEWAL_INTEREST_AMOUNT")));
                    objInvestmentsTransTO.setInvestmentAmount(new Double(0.0));
                    renewalInterestTransaction(map);
                    objInvestmentsTransTO.setBatchID(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
                    objInvestmentsTransTO.setInitiatedBranch(_branchCode);
                    if (loanDataMap != null) {
                        if (map.containsKey("RENEWAL_NUMBER_TYPE") && map.get("RENEWAL_NUMBER_TYPE").equals("DIFFERENT_PROD_ID")) {
                            objInvestmentsTransTO.setInvestmentID(CommonUtil.convertObjToStr(map.get("OLD_INVESTMENT_ID")));
                            objInvestmentsTransTO.setInvestmentBehaves(CommonUtil.convertObjToStr(map.get("OLD_INVESTMENT_TYPE")));
                            sqlMap.executeUpdate("insertInvestmentTransTO", objInvestmentsTransTO);
                            objInvestmentsTransTO.setInvestmentID(newPdodId);
                            objInvestmentsTransTO.setInvestmentBehaves(newInvType);
                        } else {
                            sqlMap.executeUpdate("insertInvestmentTransTO", objInvestmentsTransTO);
                        }
                    }
                }
                //Partial Interest
                if (map.containsKey("RENEWAL_INTEREST_TYPE") && map.get("RENEWAL_INTEREST_TYPE").equals("PARTIAL_INTEREST")) {
                    double partialInt = 0.0;
                    partialInt = CommonUtil.convertObjToDouble(map.get("PARTIAL_INTEREST_AMOUNT")).doubleValue();
                    if (partialInt > 0) {
                        objInvestmentsTransTO.setTrnCode("Interest");
                        objInvestmentsTransTO.setTransType(CommonConstants.CREDIT);
                        objInvestmentsTransTO.setBrokenPeriodInterest(new Double(partialInt));
                        objInvestmentsTransTO.setInvestmentAmount(new Double(0.0));
                        partialInterestTransaction(map);
                        objInvestmentsTransTO.setBatchID(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
                        objInvestmentsTransTO.setInitiatedBranch(_branchCode);
                        if (loanDataMap != null) {
                            if (map.containsKey("RENEWAL_NUMBER_TYPE") && map.get("RENEWAL_NUMBER_TYPE").equals("DIFFERENT_PROD_ID")) {
                                objInvestmentsTransTO.setInvestmentID(CommonUtil.convertObjToStr(map.get("OLD_INVESTMENT_ID")));
                                objInvestmentsTransTO.setInvestmentBehaves(CommonUtil.convertObjToStr(map.get("OLD_INVESTMENT_TYPE")));
                                sqlMap.executeUpdate("insertInvestmentTransTO", objInvestmentsTransTO);
                                objInvestmentsTransTO.setInvestmentID(newPdodId);
                                objInvestmentsTransTO.setInvestmentBehaves(newInvType);
                            } else {
                                sqlMap.executeUpdate("insertInvestmentTransTO", objInvestmentsTransTO);
                            }
                        }
                    }
                }
                getRenewalTransDetails(linkBatch_ID);
            }
        }
    }

    private HashMap partialInterestTransaction(HashMap map) throws Exception {
        System.out.println("In Side The DoTransaction Renewal Interest");
        System.out.println("############# objInvestmentsTransTO For Interest : " + objInvestmentsTransTO);
        HashMap achdMap = new HashMap();
        String investMentType = "";
        investMentType = CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentBehaves());
        achdMap.put("INVESTMENT_TYPE", CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentBehaves()));
        achdMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentID()));
        System.out.println("achdMap before query ------------>" + achdMap);
        List achdLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", achdMap);
        if (achdLst != null && achdLst.size() > 0) {
            achdMap = new HashMap();
            ArrayList transferList = new ArrayList();
            ArrayList cashList = new ArrayList();
            achdMap = (HashMap) achdLst.get(0);
            System.out.println("achdMap--------------->" + achdMap);
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
                                txMap.put(TransferTrans.PARTICULARS, objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName() + " - " + "Partial_Interest");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put("AUTHORIZEREMARKS", "TRANSACTIONAMOUNT");
                                txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
                                if (CommonUtil.convertObjToStr(objTransactionTO.getProductType()).equals("GL")) {
                                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                }
                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                tranAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, tranAmt));

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
                                    txMap.put(TransferTrans.PARTICULARS, objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName() + " - " + "Partial_Interest");
                                    txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, objInvestmentsTransTO.getBrokenPeriodInterest().doubleValue()));
                                    System.out.println("transferList INTEREST_PAID_AC_HD ------------->" + transferList);
                                }

                                System.out.println("transferList total ------------->" + transferList);
                            } else {
                                HashMap investmentTransMap = new HashMap();
                                investmentTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                                investmentTransMap.put("BRANCH_CODE", _branchCode);
                                investmentTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                investmentTransMap.put("USER_ID", objInvestmentsTransTO.getStatusBy());
                                investmentTransMap.put("INVESTMENT_NO", objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName());
                                if (objInvestmentsTransTO.getBrokenPeriodInterest().doubleValue() != 0.0) {
                                    investmentTransMap.put("ACCT_HEAD", achdMap.get("INTEREST_RECIVED_AC_HD"));
                                    investmentTransMap.put("TRANS_AMOUNT", objInvestmentsTransTO.getBrokenPeriodInterest());
                                    investmentTransMap.put("AUTHORIZEREMARKS", "IINVESTMENT_INTEREST");
                                    investmentTransMap.put("INT_TYPE", "Partial_Interest");
                                    cashList.add(createCashTransactionTO(investmentTransMap));
                                }
                                System.out.print("###### cashList: " + cashList);
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
        System.out.println("############# objInvestmentsTransTO For Interest : " + objInvestmentsTransTO);
        HashMap achdMap = new HashMap();
        String investMentType = "";
        investMentType = CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentBehaves());
        achdMap.put("INVESTMENT_TYPE", CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentBehaves()));
        achdMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentID()));
        System.out.println("achdMap before query ------------>" + achdMap);
        List achdLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", achdMap);
        if (achdLst != null && achdLst.size() > 0) {
            achdMap = new HashMap();
            ArrayList transferList = new ArrayList();
            ArrayList cashList = new ArrayList();
            achdMap = (HashMap) achdLst.get(0);
            System.out.println("achdMap--------------->" + achdMap);
            TransferTrans objTransferTrans = new TransferTrans();
            objTransferTrans.setInitiatedBranch(_branchCode);
            objTransferTrans.setLinkBatchId(objInvestmentsTransTO.getInvestmentName());
            HashMap txMap = new HashMap();

            if (map.containsKey("TransactionTO") && !(map.containsKey("RENEWAL_INTEREST_TYPE") && map.get("RENEWAL_INTEREST_TYPE").equals("PARTIAL_INTEREST"))) {
                System.out.println("################ renewalInterestTransaction  Only INTEREST Amt Start");
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
                                txMap.put(TransferTrans.PARTICULARS, objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName() + " - " + "Interest");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put("AUTHORIZEREMARKS", "TRANSACTIONAMOUNT");
                                txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
                                if (CommonUtil.convertObjToStr(objTransactionTO.getProductType()).equals("GL")) {
                                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                }
                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                tranAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, tranAmt));
                                System.out.println("########## renewalInterestTransaction  DIFFERENT_PROD_ID: " + map.get("RENEWAL_NUMBER_TYPE"));
                                if (objInvestmentsTransTO.getBrokenPeriodInterest().doubleValue() != 0.0) {
                                    if (map.containsKey("RENEWAL_NUMBER_TYPE") && map.get("RENEWAL_NUMBER_TYPE").equals("DIFFERENT_PROD_ID")) {
                                        System.out.println("########## renewalInterestTransaction  DIFFERENT_PROD_ID: " + map.get("RENEWAL_NUMBER_TYPE"));
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
                                    txMap.put(TransferTrans.PARTICULARS, objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName() + " - " + "Interest");
                                    txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, objInvestmentsTransTO.getBrokenPeriodInterest().doubleValue()));
                                    System.out.println("transferList INTEREST_PAID_AC_HD ------------->" + transferList);
                                }

                                System.out.println("transferList total ------------->" + transferList);
                            } else {
                                HashMap investmentTransMap = new HashMap();
                                investmentTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                                investmentTransMap.put("BRANCH_CODE", _branchCode);
                                investmentTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                investmentTransMap.put("USER_ID", objInvestmentsTransTO.getStatusBy());
                                investmentTransMap.put("INVESTMENT_NO", objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName());
                                if (objInvestmentsTransTO.getBrokenPeriodInterest().doubleValue() != 0.0) {
                                    investmentTransMap.put("ACCT_HEAD", achdMap.get("INTEREST_RECIVED_AC_HD"));
                                    investmentTransMap.put("TRANS_AMOUNT", objInvestmentsTransTO.getBrokenPeriodInterest());
                                    investmentTransMap.put("AUTHORIZEREMARKS", "IINVESTMENT_INTEREST");
                                    investmentTransMap.put("INT_TYPE", "Interest");
                                    cashList.add(createCashTransactionTO(investmentTransMap));
                                }
                                System.out.print("###### cashList: " + cashList);
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
            } else {
                System.out.println("################ renewalInterestTransaction  PARTIAL INTEREST Amt Start");
                if (!objInvestmentsTransTO.getTrnCode().equals("") && objInvestmentsTransTO.getTrnCode().equals("Interest")) {
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.PARTICULARS, objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName() + " - " + "Interest");
                    txMap.put("AUTHORIZEREMARKS", "INV_RENEWAL_INTEREST");
                    txMap.put(TransferTrans.DR_AC_HD, achdMap.get("IINVESTMENT_AC_HD"));
                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, objInvestmentsTransTO.getBrokenPeriodInterest().doubleValue()));
                    System.out.println("transferList ------------->" + transferList);

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
                    txMap.put(TransferTrans.PARTICULARS, objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName() + " - " + "Interest");
                    txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, objInvestmentsTransTO.getBrokenPeriodInterest().doubleValue()));
                    System.out.println("transferList  ------------->" + transferList);
                    System.out.println("transferList total ------------->" + transferList);
                    if (transferList != null && transferList.size() > 0) {
                        doDebitCredit(transferList, _branchCode, false);
                    }

                    if (cashList != null && cashList.size() > 0) {
                        doCashTrans(cashList, _branchCode, false);
                    }
                    transferList = null;
                    cashList = null;
                    returnDataMap.put("BATCH_ID", CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
                    System.out.println("############# returnDataMap : " + returnDataMap);
                }
            }
        } else {
            throw new Exception("investment  AccountHead is Not set...");
        }
        return null;
    }

    private CashTransactionTO createCashTransactionTO(HashMap dataMap) throws Exception {
        CashTransactionTO objCashTO = new CashTransactionTO();
        if (dataMap.get("STATUS").equals(CommonConstants.TOSTATUS_INSERT)) {
            objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
            objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.GL));
            objCashTO.setTransType(CommonConstants.DEBIT);
            objCashTO.setParticulars("By Cash : "+dataMap.get("INVESTMENT_NO")+ " - " +dataMap.get("INT_TYPE"));
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
            System.out.println("objCashTO 1st one:" + objCashTO);
        }
        return objCashTO;
    }

    private HashMap renewalDepositTransaction(HashMap map) throws Exception {
        System.out.println("In Side The DoTransaction Renewal Deposit");
        System.out.println("############# objInvestmentsTransTO For Deposit : " + objInvestmentsTransTO);
        HashMap achdMap = new HashMap();
        String investMentType = "";
        investMentType = CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentBehaves());
        achdMap.put("INVESTMENT_TYPE", CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentBehaves()));
        achdMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentID()));
        System.out.println("achdMap before query ------------>" + achdMap);
        List achdLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", achdMap);
        if (achdLst != null && achdLst.size() > 0) {
            achdMap = new HashMap();
            ArrayList transferList = new ArrayList();
            ArrayList cashList = new ArrayList();
            achdMap = (HashMap) achdLst.get(0);
            System.out.println("achdMap--------------->" + achdMap);
            TransferTrans objTransferTrans = new TransferTrans();
            objTransferTrans.setInitiatedBranch(_branchCode);
            objTransferTrans.setLinkBatchId(objInvestmentsTransTO.getInvestmentName());
            HashMap txMap = new HashMap();

            if (!objInvestmentsTransTO.getTrnCode().equals("") && objInvestmentsTransTO.getTrnCode().equals("Deposit")) {
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.PARTICULARS, objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName() + " - " + "Deposit");
                txMap.put("AUTHORIZEREMARKS", "INV_RENEWAL_DEPOSIT");
                txMap.put(TransferTrans.DR_AC_HD, achdMap.get("IINVESTMENT_AC_HD"));
                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                transferList.add(objTransferTrans.getDebitTransferTO(txMap, objInvestmentsTransTO.getInvestmentAmount().doubleValue()));
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
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.PARTICULARS, objInvestmentsTransTO.getInvestmentBehaves() + "_" + objInvestmentsTransTO.getInvestmentName() + " - " + "Deposit");
                txMap.put(CommonConstants.USER_ID, objInvestmentsTransTO.getStatusBy());
                transferList.add(objTransferTrans.getCreditTransferTO(txMap, objInvestmentsTransTO.getInvestmentAmount().doubleValue()));
                System.out.println("transferList  ------------->" + transferList);
                System.out.println("transferList total ------------->" + transferList);
                if (transferList != null && transferList.size() > 0) {
                    doDebitCredit(transferList, _branchCode, false);
                }
                transferList = null;
                returnDataMap.put("BATCH_ID", CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
                System.out.println("############# returnDataMap : " + returnDataMap);
            } else {
                throw new Exception("investment  Transaction is not proper");
            }
        } else {
            throw new Exception("investment  AccountHead is Not set...");
        }
        return null;
    }

    /* This method is used to update the already existing data by making
     *its status to be deleted */
    private void deleteData() throws Exception {
        if (objInvestmentsOperativeTO != null) {
            logTO.setData(objInvestmentsOperativeTO.toString());
            logTO.setPrimaryKey(objInvestmentsOperativeTO.getKeyData());
            logTO.setStatus(objInvestmentsOperativeTO.getCommand());
            logDAO.addToLog(logTO);
            sqlMap.executeUpdate("deleteABOperativeTO", objInvestmentsOperativeTO);
//            sqlMap.executeUpdate("deleteInvestmentSMasterTO", objInvestmentsOperativeTO);
//            sqlMap.executeUpdate("deleteOldInvestmentsDetails", objInvestmentsOperativeTO);
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
            AccountswithOtherBanksDAO dao = new AccountswithOtherBanksDAO();
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
                objInvestmentsOperativeTO = (AccountswithOtherBanksOperativeTO) map.get("InvestmentsOperativeTO");
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
                        authorize(map);
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
        System.out.println("authMap ------------>" + authMap);
        String batchid = "";
        HashMap param = new HashMap();
        String status = (String) authMap.get(CommonConstants.AUTHORIZESTATUS);
        param.put("INITIATED_BRANCH", _branchCode);
        param.put("INVESTMENT_ID", authMap.get("INVESTMENT_ID"));
        param.put("TRANS_DT", currDt.clone());
        param.put("BRANCH_CODE", _branchCode);
        param.put("BATCH_ID", authMap.get("INVESTMENT_ID"));
        param.put("AUTHORIZE_STATUS", "AUTHORIZE_STATUS");
        param.put("TRANS_DT", currDt.clone());
        List cashList = (List) sqlMap.executeQueryForList("getCashDetails", param);
        List transList = (List) sqlMap.executeQueryForList("getTransferDetails", param);
        HashMap transMap = new HashMap();

        if (status.equals("AUTHORIZED")) {
            //Update Available Balance
            HashMap whereMap = new HashMap();
            whereMap.put("INITIATED_BRANCH", _branchCode);
            whereMap.put("INVESTMENT_ID", authMap.get("INVESTMENT_ID"));
            whereMap.put("TRANS_DT", currDt.clone());
            List investmentTransList = (List) sqlMap.executeQueryForList("getDataForRenewalInvestmentTrans", whereMap);
            if (investmentTransList != null && investmentTransList.size() > 0) {
                for (int i = 0; i < investmentTransList.size(); i++) {
                    whereMap = (HashMap) investmentTransList.get(i);
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
                                        invWhereMap.put("AVAILABLE_BALANCE", availableBalance);
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
                                invWhereMap.put("AVAILABLE_BALANCE", availableBalance);
                                invWhereMap.put("BATCH_ID", whereMap.get("BATCH_ID"));
                                System.out.println("##### After Auth availableBalance : " + availableBalance);
                                invWhereMap.put("TRAN_CODE", invType);
                                sqlMap.executeUpdate("updateInvestMasterAvailableBalance", invWhereMap);
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
                                invWhereMap.put("AVAILABLE_BALANCE", availableBalance);
                                invWhereMap.put("BATCH_ID", whereMap.get("BATCH_ID"));
                                System.out.println("##### After Auth availableBalance : " + availableBalance);
                                invWhereMap.put("TRAN_CODE", invType);
                                sqlMap.executeUpdate("updateInvestMasterAvailableBalance", invWhereMap);
                                sqlMap.executeUpdate("updateInvestTrnasAvailableBalance", invWhereMap);
                            }
                        }
                    }
                }
            }

            //Inserting & Deleting Deposit Details
            sqlMap.executeUpdate("deleteInvestmentDepositDetail", authMap);
            sqlMap.executeUpdate("deleteInvestmentRenewalDepositDetail", authMap);
            if (map.containsKey("RenewalInvestmentsDepositTO")) {
                InvestmentsDepositTO objInvestmentsDepositTO = new InvestmentsDepositTO();
                objInvestmentsDepositTO = (InvestmentsDepositTO) map.get("RenewalInvestmentsDepositTO");
                sqlMap.executeUpdate("insertInvestmentsDepositTO", objInvestmentsDepositTO);
                if (map.containsKey("AVAILABLE_BALANCE")) {
                    param.put("AVAILABLE_BALANCE", CommonUtil.convertObjToDouble(map.get("AVAILABLE_BALANCE")));
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

        if (cashList != null && cashList.size() > 0) {
            if (map.containsKey("TransactionTO")) {
                HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                TransactionTO transactionTO = new TransactionTO();
                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                }
                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                transMap = (HashMap) cashList.get(0);
                batchid = CommonUtil.convertObjToStr(transMap.get("TRANS_ID"));
                doCashAuthorize(authMap, map, transactionTO, batchid);
            }
        }

        if (transList != null && transList.size() > 0) {
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

//    private void authorize(HashMap authMap,HashMap map)throws Exception { 
//        System.out.println("authMap ------------>"+authMap);
//        String batchid=objInvestmentsTransTO.getBatchID();
//        String investMentType = "";
//        investMentType = CommonUtil.convertObjToStr(objInvestmentsOperativeTO.getInvestmentType());
//        String status = (String) authMap.get(CommonConstants.AUTHORIZESTATUS);
//        String user_id=(String)authMap.get(CommonConstants.USER_ID);
//        ArrayList selectedList = (ArrayList) authMap.get(CommonConstants.AUTHORIZEDATA);
//        HashMap dataMap=new HashMap();
//        String transType ="";
//        if(map.containsKey("TransactionTO")) {
//            for(int i=0; i<selectedList.size(); i++){
//                dataMap = (HashMap) selectedList.get(i);
//                batchid=CommonUtil.convertObjToStr(dataMap.get("BATCH_ID"));
//                HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
//                TransactionTO transactionTO = new TransactionTO();
//                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
//                if (transactionDetailsMap.size()>0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs"))
//                    allowedTransDetailsTO = (LinkedHashMap)transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
//                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
//                transType = CommonUtil.convertObjToStr(transactionTO.getTransType());
//                System.out.println("####### allowedTransDetailsTO : "+allowedTransDetailsTO);
//                if (transactionTO.getTransType().equals("TRANSFER")) {
//                    dotransferInvestment(batchid, status, authMap, map);
//                }else{
//                    doCashAuthorize(authMap,map,transactionTO,batchid);
//                }
//            }
////            if(!status.equals(CommonConstants.STATUS_REJECTED)){
////                //Added By Suresh
////                if(status.equals(CommonConstants.STATUS_AUTHORIZED)){
////                    if(objInvestmentsTransTO.getTrnCode().equals("Deposit") ||objInvestmentsTransTO.getTrnCode().equals("Purchase")){
////                        String creditAcNo = "";
////                        String debitAcNo = "";
////                        HashMap whereMap = new HashMap();
////                        creditAcNo = CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestment_internal_Id());
////                        debitAcNo = CommonUtil.convertObjToStr(objInvestmentsTransTO.getTxtInvestmentInternalNoTrans());
////                        System.out.println("##### creditAcNo : "+creditAcNo);
////                        System.out.println("##### debitAcNo : "+debitAcNo);
////                        //Update Available Balance In INVESTMENT_MASTER TABLE
////                        if(creditAcNo.length()>0){  
////                            double availableBalance =0.0;
////                            whereMap.put("INVESTMENT_ID", creditAcNo);
////                            List creditLst = sqlMap.executeQueryForList("getAvailableBalanceFromMaster",whereMap);
////                            if(creditLst!=null && creditLst.size()>0){
////                                whereMap = (HashMap)creditLst.get(0);
////                                availableBalance = CommonUtil.convertObjToDouble(whereMap.get("AVAILABLE_BALANCE")).doubleValue();
////                                System.out.println("##### availableBalance : "+availableBalance);
////                                if(objInvestmentsTransTO.getTrnCode().equals("Deposit")){
////                                    availableBalance+=CommonUtil.convertObjToDouble(objInvestmentsTransTO.getInvestmentAmount()).doubleValue();
////                                }else if(objInvestmentsTransTO.getTrnCode().equals("Purchase")){
////                                    availableBalance+=CommonUtil.convertObjToDouble(objInvestmentsTransTO.getInvestmentAmount()).doubleValue()-CommonUtil.convertObjToDouble(objInvestmentsTransTO.getPurchaseRate()).doubleValue(); // Total_Share Amt-Fees Paid
////                                }
////                                whereMap.put("AVAILABLE_BALANCE",String.valueOf(availableBalance));
////                                whereMap.put("BATCH_ID",objInvestmentsTransTO.getBatchID());
////                                System.out.println("##### After Auth availableBalance : "+availableBalance);
////                                sqlMap.executeUpdate("updateInvestMasterAvailableBalance", whereMap);
////                                sqlMap.executeUpdate("updateInvestTrnasAvailableBalance", whereMap);
////                            }
////                        }
////                        //Update Available Balance In INVESTMENT_MASTER TABLE
////                        if(debitAcNo.length()>0 && transType.equals("TRANSFER")){
////                            double availableBalance =0.0;
////                            whereMap.put("INVESTMENT_ID", debitAcNo);
////                            List debitLst = sqlMap.executeQueryForList("getAvailableBalanceFromMaster",whereMap);
////                            if(debitLst!=null && debitLst.size()>0){
////                                whereMap = (HashMap)debitLst.get(0);
////                                availableBalance = CommonUtil.convertObjToDouble(whereMap.get("AVAILABLE_BALANCE")).doubleValue();
////                                System.out.println("##### availableBalance : "+availableBalance);
////                                if(objInvestmentsTransTO.getTrnCode().equals("Deposit") ||objInvestmentsTransTO.getTrnCode().equals("Purchase")){
////                                    availableBalance-=CommonUtil.convertObjToDouble(objInvestmentsTransTO.getInvestmentAmount()).doubleValue();
////                                }
////                                whereMap.put("AVAILABLE_BALANCE",String.valueOf(availableBalance));
////                                whereMap.put("BATCH_ID",objInvestmentsTransTO.getBatchID());
////                                System.out.println("##### After Auth availableBalance : "+availableBalance);
////                                sqlMap.executeUpdate("updateInvestMasterAvailableBalance", whereMap);
////                                sqlMap.executeUpdate("updateInvestTrnasOtherBankAvailableBalance", whereMap);
////                            }
////                            String chequeNo ="";
////                            chequeNo = CommonUtil.convertObjToStr(objInvestmentsTransTO.getTxtChequeNo());
////                            if(!chequeNo.equals("") && chequeNo.length()>0){
////                                whereMap.put("CHEQUE_NO",objInvestmentsTransTO.getTxtChequeNo());
////                                sqlMap.executeUpdate("updateCheckNoStatusUsed", whereMap);
////                            }
////                        }
////                    }
////                }
////            }
//        }
//        dataMap=null;
//        selectedList=null;
//    }
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
            transferTransParam.put("TRANS_DT", currDt.clone());
            transferTransParam.put("INITIATED_BRANCH", _branchCode);
            transferTransList = (ArrayList) sqlMap.executeQueryForList("getBatchTxTransferTOsAuthorize", transferTransParam);
            if (transferTransList != null && (!transferTransList.isEmpty())) {
                TransferTrans objTrans = new TransferTrans();
                objTrans.doTransferAuthorize(transferTransList, transferTransParam);
                transferTransParam.put(CommonConstants.STATUS, status);
                if (map.containsKey("BILLS_LINK_BATCH_ID")) {
                    transferTransParam.put("BATCH_ID", map.get("BILLS_LINK_BATCH_ID"));
                } else {
                    transferTransParam.put("BATCH_ID", batchid);
                }
                transferTransParam.put(CommonConstants.AUTHORIZEDT, currDt.clone());
                transferTransParam.put("TRANS_DT", currDt.clone());
                transferTransParam.put("INITIATED_BRANCH", _branchCode);
                sqlMap.executeUpdate("authorizeInvestmentTrans", transferTransParam);
            } else {
                throw new TTException("Transfer List Is Empty");
            }
        }
        transferTransList = null;
        transferTransParam = null;
    }

    private void authorize(HashMap map) throws Exception {
        String status = "";
        HashMap authMap = (HashMap) map.get("AUTHORIZEMAP");
        status = (String) authMap.get("AUTHORIZESTATUS");
        String linkBatchId = null;
        String disbursalNo = null;
        HashMap cashAuthMap;
        TransactionTO objTransactionTO = null;
        try {
            //sqlMap.startTransaction();
            transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
            System.out.println("map:" + map);
            // disbursalNo = CommonUtil.convertObjToStr(map.get("RTID"));
            map.put(CommonConstants.STATUS, status);
            //map.put(CommonConstants.USER_ID, "0001");
            map.put("CURR_DATE", currDt);
            System.out.println("status------------>" + status);
            //sqlMap.executeUpdate("authorizeRentTrans", map);
            linkBatchId = objInvestmentsOperativeTO.getInvestmentId();
            //CommonUtil.convertObjToStr(map.get("RTID"));//Transaction Batch Id
            //Separation of Authorization for Cash and Transfer
            //Call this in all places that need Authorization for Transaction
            cashAuthMap = new HashMap();
            System.out.println("@#$@zvbvxcvzx#$map:" + map);
            cashAuthMap.put(CommonConstants.BRANCH_ID, _branchCode);
            cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            cashAuthMap.put("DAILY", "DAILY");

            System.out.println("map:" + map);
            System.out.println("cashAuthMap:" + cashAuthMap);
            System.out.println("#$%#$%#$%xcvlinkBatchId" + linkBatchId);
            TransactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
//            HashMap transMap = new HashMap();
//            transMap.put("LINK_BATCH_ID",linkBatchId);
//            System.out.println("transMap----------------->"+transMap);
//            sqlMap.executeUpdate("updateInstrumentNO1Transfer", transMap);
//            sqlMap.executeUpdate("updateInstrumentNO1Cash", transMap);
//            transMap=null;
//            System.out.println("disbursalNo----------------->"+disbursalNo);
//            objTransactionTO=new TransactionTO();
//            objTransactionTO.setBatchId(CommonUtil.convertObjToStr(disbursalNo));
//            objTransactionTO.setTransId(CommonUtil.convertObjToStr(linkBatchId));
//            objTransactionTO.setBranchId(_branchCode);
//            System.out.println("objTransactionTO----------------->"+objTransactionTO);
//            sqlMap.executeUpdate("deleteRemittanceIssueTransactionTO", objTransactionTO);
//            if(!status.equals("REJECTED")){
//            }
            map = null;
            // sqlMap.commitTransaction();
        } catch (Exception e) {
            // sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }

    }

    private void doCashAuthorize(HashMap authMap, HashMap map, TransactionTO transactionTO, String batchid) throws Exception {
        String status = CommonUtil.convertObjToStr(authMap.get("AUTHORIZESTATUS"));
        if (!authMap.containsKey("callexcessOrShortauthorize")) {
            CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
            ArrayList arrList = new ArrayList();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", status);
//            singleAuthorizeMap.put("TRANS_ID", CommonUtil.convertObjToStr(transactionTO.getBatchId()));
            singleAuthorizeMap.put("LINK_BATCH_ID", batchid);
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
            // sqlMap.executeUpdate("authorizeInvestmentTrans", whereMap);
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
            // sqlMap.executeUpdate("authorizeInvestmentTrans", whereMap);
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
        List OperativeList = (List) sqlMap.executeQueryForList("getSelectABOperativeTO", map);
        if (OperativeList != null && OperativeList.size() > 0) {
            returnMap.put("InvestmentsOperativeTO", OperativeList);
            map.put("INVESTMENT_ID", map.get("ACT_MASTER_ID"));
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
            map.remove("INVESTMENT_ID");
        }
//        List depositList= (List) sqlMap.executeQueryForList("getSelectInvestmentsDepositTO", map);
//        if(depositList!=null && depositList.size()>0){
//            returnMap.put("InvestmentsDepositTO", depositList);
//        }
//        if(map.get("ACTION_TYPE").equals("Closed_Details")){
//            depositList= (List) sqlMap.executeQueryForList("getSelectClosedInvestmentsDepositTO", map);
//            if(depositList!=null && depositList.size()>0){
//                returnMap.put("InvestmentsDepositTO", depositList);
//            }else{
//                depositList= (List) sqlMap.executeQueryForList("getSelectClosedRenewalInvestmentsTO", map);
//                if(depositList!=null && depositList.size()>0){
//                    returnMap.put("InvestmentsDepositTO", depositList);
//                }
//            }
//        }
//        List renewalDepositList= (List) sqlMap.executeQueryForList("getSelectRenewalInvestmentsDepositTO", map);
//        if(renewalDepositList!=null && renewalDepositList.size()>0){
//            returnMap.put("RenewalInvestmentsDepositTO", renewalDepositList);
//        }
//        List shareList= (List) sqlMap.executeQueryForList("getSelectInvestmentsShareTO", map);
//        if(shareList!=null && shareList.size()>0){
//            returnMap.put("InvestmentsShareTO", shareList);
//        }
//        List reserveFundList= (List) sqlMap.executeQueryForList("getSelectInvestmentsRFTO", map);
//        if(reserveFundList!=null && reserveFundList.size()>0){
//            returnMap.put("InvestmentsRFTO", reserveFundList);
//        }

        if (map.get("ACTION_TYPE").equals("Authorize") || map.get("ACTION_TYPE").equals("Edit") || map.get("ACTION_TYPE").equals("Delete")) {
            HashMap param = new HashMap();
            param.put("BRANCH_CODE", _branchCode);
            param.put("TRANS_ID", map.get("ACT_MASTER_ID"));
            param.put("TRANS_DT", currDt.clone());
            List transList = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", param);
            if (transList != null && transList.size() > 0) {
                returnMap.put("TransactionTO", transList);
                transList = null;
            }
        }
        System.out.println("###### returnMap : " + returnMap);
        return returnMap;
    }

    /* This is used to free up the memory used by SharePrductTO object */
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
}
