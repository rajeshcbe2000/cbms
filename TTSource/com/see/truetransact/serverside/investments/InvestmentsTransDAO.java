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
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.TTDAO;
//import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.transferobject.investments.InvestmentsTransTO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
//import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.transferobject.borrowings.repayment.BorrowingRepIntClsTO;
import java.util.LinkedHashMap;
import java.util.Date;
import com.see.truetransact.commonutil.DateUtil;

import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import java.text.SimpleDateFormat;
import java.util.Iterator;

/**
 * ShareProduct DAO.
 *
 */
public class InvestmentsTransDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private InvestmentsTransTO objTO;
    private LogDAO logDAO;
    private Date currDt;
    private LogTO logTO;
    private HashMap loanDataMap = new HashMap();
    private HashMap deletedLoanDataMap;
    private String where = "";
    private TransactionTO objTransactionTO;
    private TransactionDAO transactionDAO = null;
    private HashMap returnDataMap = null;
    private BorrowingRepIntClsTO objBorrowingRepIntClsTO;
    private boolean getTransDetails = true;
    private Iterator processLstIterator;
    String batchId = "";
    private String narration1 = "";
    private String debitParticular = "";
    private String creditParticular = "";
    private HashMap investmentIdMap = new HashMap();
    private String generateSingleTransId ="";
    private Date backDate = null;
    private String cashId = "";
    private String transferId = "";
    /**
     * Creates a new instance of ShareProductDAO
     */
    public InvestmentsTransDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    /**
     * This method execute a Query and returns the resultset in HashMap object
     */
    private HashMap getInvestmentMasterData() throws Exception {
        HashMap returnMap = new HashMap();
        return returnMap;
    }

    private HashMap getInvestmentTransData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap param = new HashMap();
        param.put("INITIATED_BRANCH", _branchCode);
        param.put("BATCH_ID", where);
        param.put("TRANS_DT", map.get("TRANS_DT"));
        List list = (List) sqlMap.executeQueryForList("getSelectInvestmentTransTOwithoutShareKey", param);
        returnMap.put("InvestmentsTransTO", list);
        list = null;
        return returnMap;
    }

    private String getInvestmentBatchId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "INV_ID");
        String investmentId = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return investmentId;
    }

    /**
     * This method is used to insertnew datat into the Table
     */
    private void insertData(HashMap map) throws Exception {
        System.out.println("map====" + map);
        doTransaction(map);
        HashMap finalMap = (HashMap) map.get("finalMap");
        processLstIterator = finalMap.keySet().iterator();
        String key1 = "";
    //    System.out.println("fmap...." + finalMap.size());
        for (int i = 0; i < finalMap.size(); i++) {
            key1 = (String) processLstIterator.next();
            objTO = (InvestmentsTransTO) finalMap.get(key1);
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            logDAO.addToLog(logTO);
      //      System.out.println("loanDataMap1111@@@>>>>>>" + loanDataMap);
            //System.out.println("loanDataMap.get(TRANS_ID)>>>"+loanDataMap.get("TRANS_ID"));
            // if()   
            objTO.setBatchID(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
            objTO.setInitiatedBranch(_branchCode);
            System.out.println("objTO1111>>>>" + objTO);
            if (loanDataMap != null) {
                System.out.println("objTO2222>>>>" + objTO);
                sqlMap.executeUpdate("insertInvestmentTransTO", objTO);
                //Insert Premature Interest at Closing Time Only
                String investMentType = "";
                investMentType = CommonUtil.convertObjToStr(objTO.getInvestmentBehaves());
                if (objTO.getTrnCode().equals("Closure") && CommonUtil.convertObjToDouble(objTO.getPrematureInt()).doubleValue() > 0 && (investMentType.equals("OTHER_BANK_FD")
                        || investMentType.equals("OTHER_BANK_RD") || investMentType.equals("OTHER_BANK_CCD") || investMentType.equals("OTHER_BANK_SSD"))) {
                    objTO.setTrnCode("Interest");
                    objTO.setTransType(CommonConstants.CREDIT);
                    objTO.setInvestmentAmount(new Double(0.0));
                    objTO.setPurchaseMode("SHARE_PAYMENT");
                    sqlMap.executeUpdate("insertInvestmentTransTO", objTO);
                }
            }
        }
    }
//editf
    private HashMap doTransaction(HashMap map) throws Exception {
        batchId = getInvestmentBatchId();
        generateSingleTransId = generateLinkID();
        ArrayList transferList = new ArrayList();
        ArrayList cashList = new ArrayList();
    //    System.out.println("In Side The DoTransaction");
        HashMap finalMap = (HashMap) map.get("finalMap");
        processLstIterator = finalMap.keySet().iterator();
        String key1 = "";
      //  System.out.println("fmap...." + finalMap.size());
        for (int i = 0; i < finalMap.size(); i++) {
            key1 = (String) processLstIterator.next();
            objTO = (InvestmentsTransTO) finalMap.get(key1);
            //System.out.println("oooooooooo"+objTO.getInvestment_internal_Id());
            investmentIdMap.put("FINAL_MAP", finalMap);
            HashMap achdMap = new HashMap();
            String investMentType = "";
            investMentType = CommonUtil.convertObjToStr(objTO.getInvestmentBehaves());
            achdMap.put("INVESTMENT_TYPE", CommonUtil.convertObjToStr(objTO.getInvestmentBehaves()));
            achdMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objTO.getInvestmentID()));
        //    System.out.println("achdMap before query ------------>" + achdMap);
            List achdLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", achdMap);
            if (achdLst != null && achdLst.size() > 0) {
                achdMap = new HashMap();

                achdMap = (HashMap) achdLst.get(0);
          //      System.out.println("achdMap--------------->" + achdMap);
                TransferTrans objTransferTrans = new TransferTrans();
                objTransferTrans.setInitiatedBranch(_branchCode);
                objTransferTrans.setLinkBatchId(objTO.getInvestmentName());
                HashMap txMap = new HashMap();
            //    System.out.println("pppp11111");
                if (!objTO.getTrnCode().equals("") && map.containsKey("TransactionTO")) {
                    LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    if (TransactionDetailsMap.size() > 0) {
              //          System.out.println("pppp22222");
                        if (objTO.getTrnCode().equals("Deposit")) {      //DEPOSIT AMOUNT TRANSACTION
                            if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                                LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                    double tranAmt = 0.0;
                                    objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));

                                    if (investMentType.equals("OTHER_BANK_SB") || investMentType.equals("OTHER_BANK_CA") || investMentType.equals("OTHER_BANK_SPD") || investMentType.equals("RESERVE_FUND_DCB")
                                            || investMentType.equals("OTHER_BANK_FD") || investMentType.equals("OTHER_BANK_SSD") || investMentType.equals("OTHER_BANK_RD") || investMentType.equals("OTHER_BANK_CCD")) {
                                        if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                            txMap.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                            txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                            txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                            txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                //                            System.out.println("creditParticular111>>>" + creditParticular);

                                            txMap.put("AUTHORIZEREMARKS", "TRANSACTIONAMOUNT");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                            if (CommonUtil.convertObjToStr(objTransactionTO.getProductType()).equals("GL")) {
                                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                            }
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.NARRATION, objTO.getNarration());
                                            txMap.put(TransferTrans.PARTICULARS, objTO.getCrParticulars());
                                            txMap.put("generateSingleTransId", generateSingleTransId);
                                            //  tranAmt=CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                            tranAmt = objTO.getInvestmentAmount().doubleValue();
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
                                            txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, tranAmt));

                                            if (objTO.getInvestmentAmount().doubleValue() != 0.0) {
                                                txMap.put(TransferTrans.DR_AC_HD, (String) achdMap.get("IINVESTMENT_AC_HD"));
                                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_AC_HD");
                                                //System.out.println("debitParticular111>>>" + debitParticular);

                                                txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                                txMap.put(TransferTrans.NARRATION, objTO.getNarration());
                                                txMap.put(TransferTrans.PARTICULARS, objTO.getDrParticulars());
                                               // System.out.println("txmap@@20@@>>>" + txMap);
                                                txMap.put("TRANS_MOD_TYPE", "INV");
                                                txMap.put("generateSingleTransId", generateSingleTransId);
                                                txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getInvestmentAmount().doubleValue()));
                                               // System.out.println("transferList 5555 ------------->" + transferList);
                                            }

                                        } else {
                                            HashMap investmentTransMap = new HashMap();
                                            investmentTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                                            investmentTransMap.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                            investmentTransMap.put("BRANCH_CODE", _branchCode);
                                            investmentTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                            investmentTransMap.put("USER_ID", objTO.getStatusBy());
                                            investmentTransMap.put("INVESTMENT_NO", objTO.getInvestmentBehaves() + "_" + objTO.getInvestmentName());
                                            if (objTO.getInvestmentAmount().doubleValue() != 0.0) {
                                                investmentTransMap.put("ACCT_HEAD", achdMap.get("IINVESTMENT_AC_HD"));
                                                investmentTransMap.put("TRANS_AMOUNT", objTO.getInvestmentAmount());
                                                investmentTransMap.put("AUTHORIZEREMARKS", "IINVESTMENT_AC_HD");
                                                investmentTransMap.put("NARRATION", objTO.getNarration());
                                                investmentTransMap.put("PARTICULARS", objTO.getDrParticulars());
                                                investmentTransMap.put("TRANS_MOD_TYPE", "INV");
                                                investmentTransMap.put("generateSingleTransId", generateSingleTransId);
                                                investmentTransMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                cashList.add(createCashDebitTransactionTO(investmentTransMap));
                                            }
                                            //System.out.print("###### cashList@@@5555: " + cashList);
                                        }
                                    }
                                }
                            }

                            //System.out.println("transferList total222 ------------->" + transferList);
                        } else if (objTO.getTrnCode().equals("Interest")) {
                            //INTEREST AMOUNT TRANSACTION
//                            System.out.println("objTOjjjj" + objTO);
//                            System.out.println("####### investMentType : " + investMentType);
                            HashMap invMap = new HashMap();
                            //KD-3491 : Incorrect Particulars
                            String lastIntPaidDt = "";
                            invMap.put("INVESTMENT_ID", objTO.getInvestment_internal_Id());
                            List depIntList = sqlMap.executeQueryForList("getPeriodicInterestAmt", invMap);
                            if (depIntList != null && depIntList.size() > 0) {
                                invMap = (HashMap) depIntList.get(0);
                                if(invMap.containsKey("INT_REC_TILL_DT") && invMap.get("INT_REC_TILL_DT") != null){
                                  lastIntPaidDt = CommonUtil.convertObjToStr(invMap.get("INT_REC_TILL_DT"));
                                }
                            }
                            if (!investMentType.equals("")) {
                                if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs") && (investMentType.equals("OTHER_BANK_FD") || investMentType.equals("OTHER_BANK_SSD"))) { //FD  Only
                                    LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                    for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                        
                                        double tranAmt = 0.0;
                                        objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                        if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                            txMap.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                            txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                            txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                            txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                            txMap.put("AUTHORIZEREMARKS", "TRANSACTIONAMOUNT");
                                            txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                         //   System.out.println("objTO.getNarration() in transfer====" + objTO.getNarration());
                                            txMap.put(TransferTrans.NARRATION, objTO.getNarration());
                                            //KD-3491 : Incorrect Particulars
                                            //txMap.put(TransferTrans.PARTICULARS, objTransactionTO.getParticulars());
                                             txMap.put(TransferTrans.PARTICULARS, "Interest on " + objTO.getInvestment_Ref_No() + " From " + lastIntPaidDt + " To " + getDateFor(objTO.getLastIntPaidDate()));                   
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
                                            if (CommonUtil.convertObjToStr(objTransactionTO.getProductType()).equals("GL")) {
                                                txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                            }
                                            //Added By Vivek 23-July-2013
                                          //  System.out.println("objTransactionTO>>>>1111>>>>>" + objTransactionTO);
                                            if (!objTransactionTO.getProductType().equals("") && objTransactionTO.getProductType().equals("AB")) {
                                                HashMap acheadMap = new HashMap();
                                                acheadMap.put("ACT_MASTER_ID", objTransactionTO.getDebitAcctNo());
                                                List borrowingAchdLst = ServerUtil.executeQuery("getSelectAcHdForOthrBank", acheadMap);
                                                if (borrowingAchdLst != null && borrowingAchdLst.size() > 0) {
                                                    acheadMap = new HashMap();
                                                    acheadMap = (HashMap) borrowingAchdLst.get(0);
                                            //        System.out.println("acheadMap othr bank3333--------------->" + acheadMap);
                                                    txMap.put(TransferTrans.DR_AC_HD, (String) acheadMap.get("PRINCIPAL_AC_HD"));
                                                    txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_INTEREST");
                                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);
                                                } else {
                                                    throw new TTException("Account heads not set properly...");
                                                }
                                            }
                                            //Added By Suresh 06-Sep-2012
                                            if (!objTransactionTO.getProductType().equals("") && objTransactionTO.getProductType().equals("BRW")) {
                                                HashMap acheadMap = new HashMap();
                                                acheadMap.put("BORROWING_NO", objTransactionTO.getDebitAcctNo());
                                                List borrowingAchdLst = ServerUtil.executeQuery("Borrowings.getAcHeads", acheadMap);
                                                if (borrowingAchdLst != null && borrowingAchdLst.size() > 0) {
                                                    acheadMap = new HashMap();
                                                    acheadMap = (HashMap) borrowingAchdLst.get(0);
                                              //      System.out.println("acheadMap--------------->" + acheadMap);
                                                    txMap.put(TransferTrans.DR_AC_HD, (String) acheadMap.get("PRINCIPAL_GRP_HEAD"));
                                                    txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_INTEREST");
                                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                                } else {
                                                    throw new TTException("Account heads not set properly...");
                                                }
                                            }
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                            txMap.put("generateSingleTransId", generateSingleTransId);
                                            //tranAmt=CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                            tranAmt = (objTO.getBrokenPeriodInterest().doubleValue()-objTO.getInvestTDS());//objTO.getBrokenPeriodInterest().doubleValue()
                                            ///System.out.println("txmap@@21@@>>>" + txMap);
                                            txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, tranAmt));
                                            if (tranAmt == 0.0 && investMentType.equals("OTHER_BANK_SSD")) {
                                                tranAmt = (objTO.getInvestmentAmount().doubleValue());//objTO.getBrokenPeriodInterest().doubleValue()
                                                //System.out.println("txmap@@111>>>" + txMap);
                                                txMap.put("generateSingleTransId", generateSingleTransId);
                                                txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, tranAmt));
                                            }
                                            //TDS debit transaction
                                            if(objTO.getInvestTDS()>0){
                                                    txMap = new HashMap();
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put(TransferTrans.DR_AC_HD, (String) achdMap.get("TDS_AC_HD"));
                                                    txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_TDS");
                                                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, " TDS " + objTO.getInvestment_Ref_No() + " From ");
                                                    txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                                    txMap.put("generateSingleTransId", generateSingleTransId);
                                                    txMap.put("TRANS_MOD_TYPE", "INV");
                                                    txMap.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                                    txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getInvestTDS()));
                                                    //System.out.println("transferList TDS------------>" + transferList);

                                            }
                                            
                                            if (objTO.getBrokenPeriodInterest().doubleValue() != 0.0) {
                                                txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("INTEREST_RECIVED_AC_HD"));
                                                txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_INTEREST");
                                                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                //edited by Nithin
                                                txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                                //System.out.println("objTO.getNarration()222 in transfer===" + objTO.getNarration());
                                                txMap.put(TransferTrans.NARRATION, objTO.getNarration());
                                                //txMap.put(TransferTrans.PARTICULARS, "Interest on Investment "+objTO.getInvestment_Ref_No());
                                                //KD-3491 : Incorrect Particulars
                                                txMap.put(TransferTrans.PARTICULARS, " Interest on " + objTO.getInvestment_Ref_No() + " From " + lastIntPaidDt + " To " + getDateFor(objTO.getLastIntPaidDate()));                  
                                           
                                                txMap.put("TRANS_MOD_TYPE", "INV");
//                                              //  transferList.add(objTO.getInvestment_internal_Id());
                                                txMap.put("generateSingleTransId", generateSingleTransId);
                                                txMap.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                                txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getBrokenPeriodInterest().doubleValue()));
                                                //System.out.println("transferList INTEREST_PAID_AC_HD ------------->" + transferList);
                                            } else if (objTO.getBrokenPeriodInterest().doubleValue() == 0.0 && investMentType.equals("OTHER_BANK_SSD")) { //vv
                                                txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("INTEREST_RECIVED_AC_HD"));
                                                txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_INTEREST");
                                                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                                txMap.put(TransferTrans.NARRATION, objTO.getNarration());
                                                txMap.put(TransferTrans.PARTICULARS, "Interest on Investment "+objTO.getInvestment_Ref_No());
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                                txMap.put("TRANS_MOD_TYPE", "INV");
                                                txMap.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                                txMap.put("generateSingleTransId", generateSingleTransId);
                                                txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getInvestmentAmount().doubleValue()));
                                            //    System.out.println("transferList INTEREST_PAID_AC_HD ------------->" + transferList);
                                            }

                                           // System.out.println("transferList total333 ------------->" + transferList);
                                        } else {
                                            HashMap investmentTransMap = new HashMap();
                                            investmentTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                                            investmentTransMap.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                            investmentTransMap.put("BRANCH_CODE", _branchCode);
                                            investmentTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                            investmentTransMap.put("USER_ID", objTO.getStatusBy());
                                            //System.out.println("objTO.getNarration() in cash===" + objTO.getNarration());
                                            investmentTransMap.put("NARRATION", objTO.getNarration());
                                            investmentTransMap.put("PARTICULARS", objTransactionTO.getParticulars());
                                            investmentTransMap.put("INVESTMENT_NO", objTO.getInvestmentBehaves() + "_" + objTO.getInvestmentName());
                                            investmentTransMap.put("TRANS_MOD_TYPE", "INV");
                                            if (objTO.getBrokenPeriodInterest().doubleValue() != 0.0) {
                                                investmentTransMap.put("ACCT_HEAD", achdMap.get("INTEREST_RECIVED_AC_HD"));
                                                investmentTransMap.put("TRANS_AMOUNT", objTO.getBrokenPeriodInterest());
                                                investmentTransMap.put("AUTHORIZEREMARKS", "IINVESTMENT_INTEREST");
                                                investmentTransMap.put("generateSingleTransId", generateSingleTransId);
                                                investmentTransMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                cashList.add(createCashTransactionTO(investmentTransMap));
                                            } else if (objTO.getBrokenPeriodInterest().doubleValue() == 0.0 && investMentType.equals("OTHER_BANK_SSD")) { //vv
                                                investmentTransMap.put("ACCT_HEAD", achdMap.get("INTEREST_RECIVED_AC_HD"));
                                                investmentTransMap.put("TRANS_AMOUNT", objTO.getInvestmentAmount()-objTO.getInvestTDS());
                                                investmentTransMap.put("AUTHORIZEREMARKS", "IINVESTMENT_INTEREST");
                                                investmentTransMap.put("generateSingleTransId", generateSingleTransId);
                                                investmentTransMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                cashList.add(createCashTransactionTO(investmentTransMap));
                                            }
                                       //     System.out.print("###### cashList@@@6666: " + cashList);
                                             if(objTO.getInvestTDS()>0){
                                                    txMap = new HashMap();
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, " TDS " + objTO.getInvestment_Ref_No() + " From ");
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    //txMap.put(TransferTrans.CR_INST_TYPE, "VOUCHER");
                                                    txMap.put(TransferTrans.CR_BRANCH,_branchCode );
                                                    txMap.put("AUTHORIZEREMARKS", "TDSAMOUNT");
                                                    txMap.put("generateSingleTransId", generateSingleTransId);
                                                    txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(achdMap.get("INTEREST_RECIVED_AC_HD")));
                                                    txMap.put("TRANS_MOD_TYPE", "INV");
                                                    txMap.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                                    txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getInvestTDS()));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put(TransferTrans.DR_AC_HD, (String) achdMap.get("TDS_AC_HD"));
                                                    txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_TDS");
                                                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS," TDS " + objTO.getInvestment_Ref_No() + " From ");
                                                    txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                                    txMap.put("generateSingleTransId", generateSingleTransId);
                                                    txMap.put("TRANS_MOD_TYPE", "INV");
                                                    txMap.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                                    txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getInvestTDS()));
                                                   // System.out.println("transferList TDS------------>" + transferList);

                                            }
                                        }
                                    }
                                } else if (investMentType.equals("OTHER_BANK_CCD")) {             //Only CCD Interest
                                    if (objTO.getBrokenPeriodInterest().doubleValue() != 0.0) {
                                        HashMap whereMap = new HashMap();
                                        String intWithPrincipal = "";
                                        whereMap.put("INVESTMENT_TYPE", CommonUtil.convertObjToStr(objTO.getInvestmentBehaves()));
                                        whereMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objTO.getInvestmentID()));
                                        whereMap.put("INVESTMENT_ID", CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                        List depMasterList = ServerUtil.executeQuery("getIntWithPrincipalFromDepMaster", whereMap);
                                        if (depMasterList != null && depMasterList.size() > 0) {
                                            whereMap = (HashMap) depMasterList.get(0);
                                            intWithPrincipal = CommonUtil.convertObjToStr(whereMap.get("INT_WITH_PRINCIPAL"));
                                            if (!intWithPrincipal.equals("") && intWithPrincipal.equals("Y")) {
                                                txMap.put(TransferTrans.DR_AC_HD, achdMap.get("IINVESTMENT_AC_HD")); //DEBIT TRANSACTION
                                                txMap.put("AUTHORIZEREMARKS", "INTEREST_IINVESTMENT_AC_HD");
                                            } else {
                                                txMap.put(TransferTrans.DR_AC_HD, achdMap.get("INTEREST_RECEIVABLE_AC_HD"));
                                                txMap.put("AUTHORIZEREMARKS", "INTEREST_RECEIVABLE_AC_HD");
                                            }
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS,objTransactionTO.getParticulars());

                                            txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_INTEREST");
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.NARRATION, objTO.getNarration());
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
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
                                            //System.out.println("txmap@@222>>>" + txMap);
                                            txMap.put("generateSingleTransId", generateSingleTransId);
                                            txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getBrokenPeriodInterest().doubleValue()));
                                            //System.out.println("transferList ------------->" + transferList);

                                            txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("INTEREST_RECIVED_AC_HD"));   //CREDIT TRANSACTION
                                            txMap.put("AUTHORIZEREMARKS", "INTEREST_RECIVED_AC_HD");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.NARRATION, objTO.getNarration());
                                            txMap.put(TransferTrans.PARTICULARS,"Interest on Investment "+objTO.getInvestment_Ref_No());
                                            txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                            txMap.put("TRANS_MOD_TYPE", "INV");
                                            txMap.put("generateSingleTransId", generateSingleTransId);
                                            txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getBrokenPeriodInterest().doubleValue()));
                                            //System.out.println("transferList ------------->" + transferList);
                                        }
                                    }
                                } else if (investMentType.equals("OTHER_BANK_RD") || investMentType.equals("OTHER_BANK_SB") || investMentType.equals("OTHER_BANK_CA") || investMentType.equals("OTHER_BANK_SPD")) {
                                    if (objTO.getBrokenPeriodInterest().doubleValue() != 0.0) {
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.PARTICULARS, objTransactionTO.getParticulars());
                                        txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_INTEREST");
                                        txMap.put(TransferTrans.DR_AC_HD, achdMap.get("IINVESTMENT_AC_HD"));
                                        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                        txMap.put(TransferTrans.NARRATION, objTO.getNarration());
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
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
                                        txMap.put("generateSingleTransId", generateSingleTransId);
                                        txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                       // System.out.println("txmap@@333>>>" + txMap);
                                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getBrokenPeriodInterest().doubleValue()));
                                        //System.out.println("transferList ------------->" + transferList);

                                        txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("INTEREST_RECIVED_AC_HD"));
                                        txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_INTEREST");
                                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.NARRATION, objTO.getNarration());
                                        txMap.put(TransferTrans.PARTICULARS, "Interest on Investment "+objTO.getInvestment_Ref_No());
                                        txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                        txMap.put("TRANS_MOD_TYPE", "INV");
                                        txMap.put("generateSingleTransId", generateSingleTransId);
                                        txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getBrokenPeriodInterest().doubleValue()));
                                        //System.out.println("transferList1111  ------------->" + transferList);
                                    }
                                } else if (investMentType.equals("RESERVE_FUND_DCB")) {
                                    //System.out.println("Rdo SB/CA Yes OR No ------------->" + objTO.getRdoSBorCA());
                                    if (objTO.getRdoSBorCA().equals("Y") && !objTO.getRdoSBorCA().equals("")) {
                                        LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                        for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                            
                                            double tranAmt = 0.0;
                                            objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                            if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                                txMap.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                                txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                                txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                                txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                                txMap.put(TransferTrans.NARRATION, objTO.getNarration());
                                                txMap.put(TransferTrans.PARTICULARS, objTransactionTO.getParticulars());
                                                txMap.put("AUTHORIZEREMARKS", "TRANSACTIONAMOUNT");
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
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
                                                if (CommonUtil.convertObjToStr(objTransactionTO.getProductType()).equals("GL")) {
                                                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                                }
                                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                                txMap.put("generateSingleTransId", generateSingleTransId);
                                                //   tranAmt=CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                                tranAmt = objTO.getBrokenPeriodInterest().doubleValue();
                                      //          System.out.println("txmap@@444>>>" + txMap);
                                                txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, tranAmt));

                                                if (objTO.getBrokenPeriodInterest().doubleValue() != 0.0) {
                                                    txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("INTEREST_RECIVED_AC_HD"));
                                                    txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_INTEREST");
                                                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.NARRATION, objTO.getNarration());
                                                    txMap.put(TransferTrans.PARTICULARS, "Interest on Investment "+objTO.getInvestment_Ref_No());
                                                    txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                                    txMap.put("TRANS_MOD_TYPE", "INV");
                                                    txMap.put("generateSingleTransId", generateSingleTransId);
                                                    txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getBrokenPeriodInterest().doubleValue()));
                                                    //System.out.println("transferList INTEREST_PAID_AC_HD ------------->" + transferList);
                                                }

                                                //System.out.println("transferList total444 ------------->" + transferList);
                                            }
                                        }
                                    } else {
                                        if (objTO.getBrokenPeriodInterest().doubleValue() != 0.0) {
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.NARRATION, objTO.getNarration());
                                            txMap.put(TransferTrans.PARTICULARS, objTransactionTO.getParticulars());
                                            txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_INTEREST");
                                            txMap.put(TransferTrans.DR_AC_HD, achdMap.get("IINVESTMENT_AC_HD"));
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                            //System.out.println("txmap@@555>>>" + txMap);
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
                                            txMap.put("generateSingleTransId", generateSingleTransId);
                                            txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getBrokenPeriodInterest().doubleValue()));
                                           // System.out.println("transferList ------------->" + transferList);

                                            txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("INTEREST_RECIVED_AC_HD"));
                                            txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_INTEREST");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.NARRATION, objTO.getNarration());
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS,"Interest on Investment "+objTO.getInvestment_Ref_No());
                                            txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                            txMap.put("TRANS_MOD_TYPE", "INV");
                                            txMap.put("generateSingleTransId", generateSingleTransId);
                                            txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getBrokenPeriodInterest().doubleValue()));
                                           // System.out.println("transferList2222  ------------->" + transferList);
                                        }
                                    }
                                }
                            }
                        } else if (objTO.getTrnCode().equals("Withdrawal")) {       //WITHDRAWAL AMOUNT TRANSACTION
//                            System.out.println("####### objTO : " + objTO);
                 //           System.out.println("####### investMentType : " + investMentType);
                            if (!investMentType.equals("")) {
                                double pstAmt = CommonUtil.convertObjToDouble(map.get("POSTAGE_AMT")).doubleValue();
                                String achd = CommonUtil.convertObjToStr(map.get("POSTAGE_ACHD"));
                                double renewPstAmt = CommonUtil.convertObjToDouble(map.get("RENEW_POSTAGE_AMT")).doubleValue();


                                if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs") && (investMentType.equals("OTHER_BANK_SB") || investMentType.equals("OTHER_BANK_CA")
                                        || investMentType.equals("RESERVE_FUND_DCB") || investMentType.equals("SHARES_DCB") || investMentType.equals("SHARE_OTHER_INSTITUTIONS") 
                                        || investMentType.equals("OTHER_BANK_SPD"))) {
                                    LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                    for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                        txMap.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                        double tranAmt = 0.0;
                                        objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                        if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                            txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                            txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                            txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                            txMap.put(TransferTrans.PARTICULARS, objTO.getDrParticulars());
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                            txMap.put("AUTHORIZEREMARKS", "TRANSACTIONAMOUNT");
                                            txMap.put(TransferTrans.NARRATION, CommonUtil.convertObjToStr(objTO.getNarration()));
                                            if (CommonUtil.convertObjToStr(objTransactionTO.getProductType()).equals("GL")) {
                                                txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                            }
                                         //                                        //Added By Suresh 06-Sep-2012
//                                        if(!objTransactionTO.getProductType().equals("")&& objTransactionTO.getProductType().equals("BRW") && (investMentType.equals("OTHER_BANK_SB") || investMentType.equals("OTHER_BANK_CA"))){
//                                            HashMap acheadMap=new HashMap();
//                                            acheadMap.put("BORROWING_NO", objTransactionTO.getDebitAcctNo());
//                                            List borrowingAchdLst=ServerUtil.executeQuery("Borrowings.getAcHeads", acheadMap);
//                                            if(borrowingAchdLst!=null && borrowingAchdLst.size()>0){
//                                                acheadMap = new HashMap();
//                                                acheadMap=(HashMap)borrowingAchdLst.get(0);
//                                                System.out.println("acheadMap--------------->"+acheadMap);
//                                                txMap.put(TransferTrans.DR_AC_HD, (String)acheadMap.get("PRINCIPAL_GRP_HEAD"));
//                                                txMap.put("AUTHORIZEREMARKS","IINVESTMENT_WITHDRAWAL");
//                                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
//                                            }else{
//                                                throw new TTException("Account heads not set properly...");
//                                            }
//                                        }
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);                                           
                                            tranAmt = objTO.getInvestmentAmount().doubleValue();                                          
                   //                         System.out.println("txmap@@666>>>" + txMap);
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
                                            txMap.put("generateSingleTransId", generateSingleTransId);
                                            txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, tranAmt));

                                            if (objTO.getInvestmentAmount().doubleValue() != 0.0) {
                                                txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("IINVESTMENT_AC_HD"));
                                                txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_WITHDRAWAL");
                                                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                                txMap.put(TransferTrans.NARRATION, CommonUtil.convertObjToStr(objTO.getNarration()));
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put(TransferTrans.PARTICULARS, objTO.getCrParticulars());
                                                txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                                txMap.put("TRANS_MOD_TYPE", "INV");
                                                txMap.put("generateSingleTransId", generateSingleTransId);
                                                txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                if (pstAmt > 0.0 || renewPstAmt > 0.0) {
                                                    if (pstAmt > 0.0) {
                                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getInvestmentAmount().doubleValue() - pstAmt));
                                                    }
                                                    if (renewPstAmt > 0.0) {
                                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getInvestmentAmount().doubleValue() - renewPstAmt));

                                                    }
                                                } else {
                                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getInvestmentAmount().doubleValue()));
                                                }
                                                if (pstAmt > 0.0 || renewPstAmt > 0.0) {
                                                    if (pstAmt > 0.0) {
                                                        txMap.put(TransferTrans.CR_AC_HD, achd);
                                                        txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_POSTAGE");
                                                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                        txMap.put(TransferTrans.PARTICULARS, objTO.getCrParticulars());
                                                        txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                                        txMap.put("TRANS_MOD_TYPE", "INV");
                                                        txMap.put("generateSingleTransId", generateSingleTransId);
                                                        txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, pstAmt));
                                                    }
                                                    if (renewPstAmt > 0.0) {
                                                        txMap.put(TransferTrans.CR_AC_HD, achd);
                                                        txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_POSTAGE");
                                                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                        txMap.put(TransferTrans.PARTICULARS, objTO.getCrParticulars());
                                                        txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                                        txMap.put("TRANS_MOD_TYPE", "INV");
                                                        txMap.put("generateSingleTransId", generateSingleTransId);
                                                        txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, renewPstAmt));
                                                    }
                                                }
                                            }

                                          //  System.out.println("transferList total555 ------------->" + transferList);
                                        } else {
                                            HashMap investmentTransMap = new HashMap();
                                            investmentTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                                            investmentTransMap.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                            investmentTransMap.put("BRANCH_CODE", _branchCode);
                                            investmentTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                            investmentTransMap.put("USER_ID", objTO.getStatusBy());
                                            investmentTransMap.put("INVESTMENT_NO", objTO.getInvestmentBehaves() + "_" + objTO.getInvestmentName());
                                            investmentTransMap.put("TRANS_MOD_TYPE", "INV");
                                            if (objTO.getInvestmentAmount().doubleValue() != 0.0) {
                                                investmentTransMap.put("ACCT_HEAD", achdMap.get("IINVESTMENT_AC_HD"));
                                                investmentTransMap.put("TRANS_AMOUNT", objTO.getInvestmentAmount());
                                                investmentTransMap.put("AUTHORIZEREMARKS", "IINVESTMENT_WITHDRAWAL");
                                                investmentTransMap.put("NARRATION", objTO.getNarration());
                                                investmentTransMap.put("generateSingleTransId", generateSingleTransId);
                                                investmentTransMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                cashList.add(createCashTransactionTO(investmentTransMap));
                                            }
                                            //System.out.print("###### cashList@@@7777: " + cashList);
                                        }
                                    }
                                }
                            }
                        } else if (objTO.getTrnCode().equals("Charges") && (investMentType.equals("OTHER_BANK_SB") || investMentType.equals("OTHER_BANK_CA") || investMentType.equals("OTHER_BANK_SPD"))) {
                            if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                                LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                    
                                    double tranAmt = 0.0;
                                    objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                    if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                        txMap.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                        txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                        txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                        txMap.put(TransferTrans.PARTICULARS, objTO.getDrParticulars());
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                        txMap.put("AUTHORIZEREMARKS", "TRANSACTIONAMOUNT");
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
                                         txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                        if (CommonUtil.convertObjToStr(objTransactionTO.getProductType()).equals("GL")) {
                                            txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        }
                                        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                        txMap.put("generateSingleTransId", generateSingleTransId);
                                        //  tranAmt=CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                        tranAmt = objTO.getBrokenPeriodInterest().doubleValue();
                                      //  System.out.println("txmap@@777>>>" + txMap);
                                        txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, tranAmt));
                                        if (objTO.getBrokenPeriodInterest().doubleValue() != 0.0) {
                                            txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("IINVESTMENT_AC_HD"));
                                            txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_CHARGE_AMOUNT");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, objTO.getCrParticulars());
                                            txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                            txMap.put("TRANS_MOD_TYPE", "INV");
                                            txMap.put("generateSingleTransId", generateSingleTransId);
                                            txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getBrokenPeriodInterest().doubleValue()));
                                            //System.out.println("transferList INTEREST_PAID_AC_HD ------------->" + transferList);
                                        }
                                       // System.out.println("transferList total666 ------------->" + transferList);
                                    }
                                }
                            }
                        } else if (objTO.getTrnCode().equals("Renewal") && investMentType.equals("OTHER_BANK_CCD")) {
                            HashMap renewalMap = new HashMap();
                            String intWithPrincipal = "";
                            String withOrWithoutInterest = "";
                            if (map.containsKey("RENEWAL_DATA")) {
                                renewalMap = (HashMap) map.get("RENEWAL_DATA");
                                //System.out.println("##### renewalMap : " + renewalMap);
                                intWithPrincipal = CommonUtil.convertObjToStr(renewalMap.get("INT_WITH_PRINCIPAL"));
                                withOrWithoutInterest = CommonUtil.convertObjToStr(renewalMap.get("WITH_INTEREST"));
                                if (intWithPrincipal.length() > 0 && withOrWithoutInterest.length() > 0) {
                                    if (intWithPrincipal.equals("N") && withOrWithoutInterest.equals("Y")) {
                                        if (objTO.getBrokenPeriodInterest().doubleValue() != 0.0) {
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, objTO.getDrParticulars());
                                            txMap.put("AUTHORIZEREMARKS", "RENEWAL_INTEREST");
                                            txMap.put(TransferTrans.DR_AC_HD, achdMap.get("IINVESTMENT_AC_HD"));
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
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
                                            txMap.put("generateSingleTransId", generateSingleTransId);
                                            txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                           // System.out.println("txmap@@888>>>" + txMap);
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getBrokenPeriodInterest().doubleValue()));
                                            //System.out.println("transferList ------------->" + transferList);

                                            txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("INTEREST_RECEIVABLE_AC_HD"));
                                            txMap.put("AUTHORIZEREMARKS", "RENEWAL_INTEREST");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, objTO.getCrParticulars());
                                            txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                            txMap.put("TRANS_MOD_TYPE", "INV");
                                            txMap.put("generateSingleTransId", generateSingleTransId);
                                            txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getBrokenPeriodInterest().doubleValue()));
                                            //System.out.println("transferList 3333 ------------->" + transferList);
                                        }
                                    } else {
                                        LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                        for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                            
                                            double tranAmt = 0.0;
                                            objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                            if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                                txMap.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                                txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                                txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                                txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                                txMap.put(TransferTrans.PARTICULARS, objTO.getCrParticulars());
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                                txMap.put("AUTHORIZEREMARKS", "TRANSACTIONAMOUNT");
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
                                                if (CommonUtil.convertObjToStr(objTransactionTO.getProductType()).equals("GL")) {
                                                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                                }
                                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                                txMap.put("generateSingleTransId", generateSingleTransId);
                                                // tranAmt=CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                                tranAmt = objTO.getBrokenPeriodInterest().doubleValue();
                                                txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                              //  System.out.println("txmap@@999>>>" + txMap);
                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, tranAmt));
                                                if (objTO.getBrokenPeriodInterest().doubleValue() != 0.0) {
                                                    if (intWithPrincipal.equals("Y") && withOrWithoutInterest.equals("N")) {
                                                        txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("IINVESTMENT_AC_HD"));
                                                    } else if (intWithPrincipal.equals("N") && withOrWithoutInterest.equals("N")) {
                                                        txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("INTEREST_RECEIVABLE_AC_HD"));
                                                    }
                                                    txMap.put("AUTHORIZEREMARKS", "RENEWAL_INTEREST");
                                                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, objTO.getCrParticulars());
                                                    txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                                    txMap.put("TRANS_MOD_TYPE", "INV");
                                                    txMap.put("generateSingleTransId", generateSingleTransId);
                                                    txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getBrokenPeriodInterest().doubleValue()));
                                                }
                                            } else {
                                                HashMap investmentTransMap = new HashMap();
                                                investmentTransMap.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                                investmentTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                                                investmentTransMap.put("BRANCH_CODE", _branchCode);
                                                investmentTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                                investmentTransMap.put("USER_ID", objTO.getStatusBy());
                                                investmentTransMap.put("INVESTMENT_NO", objTO.getInvestmentBehaves() + "_" + objTO.getInvestmentName());
                                                investmentTransMap.put("TRANS_MOD_TYPE", "INV");
                                                if (objTO.getBrokenPeriodInterest().doubleValue() != 0.0) {
                                                    if (intWithPrincipal.equals("Y") && withOrWithoutInterest.equals("N")) {
                                                        investmentTransMap.put("ACCT_HEAD", achdMap.get("IINVESTMENT_AC_HD"));
                                                    } else if (intWithPrincipal.equals("N") && withOrWithoutInterest.equals("N")) {
                                                        investmentTransMap.put("ACCT_HEAD", achdMap.get("INTEREST_RECEIVABLE_AC_HD"));
                                                    }
                                                    investmentTransMap.put("TRANS_AMOUNT", objTO.getBrokenPeriodInterest());
                                                    investmentTransMap.put("AUTHORIZEREMARKS", "RENEWAL_INTEREST");
                                                    investmentTransMap.put("generateSingleTransId", generateSingleTransId);
                                                    investmentTransMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                    cashList.add(createCashTransactionTO(investmentTransMap));
                                                }
                                             //   System.out.print("###### cashList@@@8888: " + cashList);
                                            }
                                        }
                                    }

                                }
                            }
                        } else if (objTO.getTrnCode().equals("Closure") && (investMentType.equals("OTHER_BANK_FD") || investMentType.equals("OTHER_BANK_RD")
                                || investMentType.equals("OTHER_BANK_CCD") || investMentType.equals("OTHER_BANK_SSD"))) {

                            if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs") && (investMentType.equals("OTHER_BANK_FD")
                                    || investMentType.equals("OTHER_BANK_SSD") || investMentType.equals("OTHER_BANK_RD"))) {
                                // Transaction Only "INTEREST_RECEIVABLE"
//                            if(map.containsKey("CLOSING_TYPE") && (map.get("CLOSING_TYPE").equals("Normal Closure") || map.get("CLOSING_TYPE").equals("Premature Closure"))
//                            && map.get("INTEREST_TYPE").equals("Interest Receivable")){
                                //System.out.println("objTO.getClosingType()" + objTO.getClosingType() + "&&" + "objTO.getInterestType()" + objTO.getInterestType());
                                if ((objTO.getClosingType().equals("Normal Closure") || objTO.getClosingType().equals("Premature Closure"))
                                        && objTO.getInterestType().equals("Interest Receivable")) {

                                    LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                    for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                        
                                        double tranAmt = 0.0;
                                        objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                        if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                            txMap.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                            txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                            txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                            txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                            //txMap.put(TransferTrans.PARTICULARS, objTO.getDrParticulars());
                                            txMap.put(TransferTrans.PARTICULARS, "Closure Investment Amount "+objTO.getInvestment_Ref_No()); //KD-3373
                                            txMap.put("AUTHORIZEREMARKS", "TRANSACTIONAMOUNT");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
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
                                            if (CommonUtil.convertObjToStr(objTransactionTO.getProductType()).equals("GL")) {
                                                txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                            }
                                            //Added By Vivek 23-July-2013
                                  //          System.out.println("objTransactionTO>>>>1111>>>>>" + objTransactionTO);
                                            if (!objTransactionTO.getProductType().equals("") && objTransactionTO.getProductType().equals("AB")) {
                                                HashMap acheadMap = new HashMap();
                                                acheadMap.put("ACT_MASTER_ID", objTransactionTO.getDebitAcctNo());
                                                List borrowingAchdLst = ServerUtil.executeQuery("getSelectAcHdForOthrBank", acheadMap);
                                                if (borrowingAchdLst != null && borrowingAchdLst.size() > 0) {
                                                    acheadMap = new HashMap();
                                                    acheadMap = (HashMap) borrowingAchdLst.get(0);
                                    //                System.out.println("acheadMap othr bank1111--------------->" + acheadMap);
                                                    txMap.put(TransferTrans.DR_AC_HD, (String) acheadMap.get("PRINCIPAL_AC_HD"));
                                                    txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_INTEREST");
                                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);
                                                } else {
                                                    throw new TTException("Account heads not set properly...");
                                                }
                                            }
                                            //Added By Suresh 06-Sep-2012
                                            if (!objTransactionTO.getProductType().equals("") && objTransactionTO.getProductType().equals("BRW")) {
                                                HashMap acheadMap = new HashMap();
                                                acheadMap.put("BORROWING_NO", objTransactionTO.getDebitAcctNo());
                                                List borrowingAchdLst = ServerUtil.executeQuery("Borrowings.getAcHeads", acheadMap);
                                                if (borrowingAchdLst != null && borrowingAchdLst.size() > 0) {
                                                    acheadMap = new HashMap();
                                                    acheadMap = (HashMap) borrowingAchdLst.get(0);
                                      //              System.out.println("acheadMap--------------->" + acheadMap);
                                                    txMap.put(TransferTrans.DR_AC_HD, (String) acheadMap.get("PRINCIPAL_GRP_HEAD"));
                                                    txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_INTEREST");
                                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                                    
                                                } else {
                                                    throw new TTException("Account heads not set properly...");
                                                }
                                            }
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                            //tranAmt=CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                            tranAmt = objTO.getInvestmentAmount().doubleValue();
//                                            System.out.println("tranAmt>>>>11>>>" + tranAmt);
//                                            System.out.println("txmap@@10>>>" + txMap);
                                            txMap.put("generateSingleTransId", generateSingleTransId);
                                            txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                            txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, tranAmt));

                                            if (objTO.getInvestmentAmount().doubleValue() != 0.0) {
                                                txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("IINVESTMENT_AC_HD"));
                                                txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_CLOSER");
                                                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                //txMap.put(TransferTrans.PARTICULARS, objTO.getCrParticulars());
                                                txMap.put(TransferTrans.PARTICULARS, "Closure Investment Amount "+objTO.getInvestment_Ref_No()); //KD-3373
                                                txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                                txMap.put("TRANS_MOD_TYPE", "INV");
//                                                System.out.println("ppp33333333333");
//                                                System.out.println("objTO.getInvestmentAmount()>>>" + objTO.getInvestmentAmount());
                                                txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                txMap.put("generateSingleTransId", generateSingleTransId);
                                                transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getInvestmentAmount().doubleValue()));
                                            }

                                            //Premature Closure Interest
                                            if (CommonUtil.convertObjToDouble(objTO.getPrematureInt()).doubleValue() > 0) {
                                                txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("INTEREST_RECIVED_AC_HD"));
                                                txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_CLOSER");
                                                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                //txMap.put(TransferTrans.PARTICULARS, objTO.getCrParticulars());
                                                txMap.put(TransferTrans.PARTICULARS, "Closure Interest Amount "+objTO.getInvestment_Ref_No()); //KD-3373
                                                txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                                txMap.put("TRANS_MOD_TYPE", "INV");
                                                System.out.println("objTO.getPrematureInt()>>>" + objTO.getPrematureInt());
                                                txMap.put("generateSingleTransId", generateSingleTransId);
                                                txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                transferList.add(objTransferTrans.getCreditTransferTO(txMap, CommonUtil.convertObjToDouble(objTO.getPrematureInt()).doubleValue()));
                                                //System.out.println("objTransactionTO>>>>1111@@@@acved>>>>>" + objTransactionTO);
                                                if (!objTransactionTO.getProductType().equals("") && objTransactionTO.getProductType().equals("AB")) {
                                                    HashMap acheadMap = new HashMap();
                                                    acheadMap.put("ACT_MASTER_ID", objTransactionTO.getDebitAcctNo());
                                                    List borrowingAchdLst = ServerUtil.executeQuery("getSelectAcHdForOthrBank", acheadMap);
                                                    if (borrowingAchdLst != null && borrowingAchdLst.size() > 0) {
                                                        acheadMap = new HashMap();
                                                        acheadMap = (HashMap) borrowingAchdLst.get(0);
                                                  //      System.out.println("acheadMap othr bank1111@@@@--------------->" + acheadMap);
                                                        txMap.put(TransferTrans.DR_AC_HD, (String) acheadMap.get("PRINCIPAL_AC_HD"));
                                                        txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_INTEREST");
                                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);

                                                    }
                                                } else {
                                                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                                }
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
                                                //txMap.put(TransferTrans.PARTICULARS, objTO.getDrParticulars());
                                                txMap.put(TransferTrans.PARTICULARS, "Closure Interest Amount "+objTO.getInvestment_Ref_No()); //KD-3373
                                                //System.out.println("txMap@@11@@>>>>>" + txMap);
                                                txMap.put("generateSingleTransId", generateSingleTransId);
                                                txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                                txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, CommonUtil.convertObjToDouble(objTO.getPrematureInt()).doubleValue()));
                                            }

                                           // System.out.println("transferList total777 ------------->" + transferList);
                                        } else {
                                            HashMap investmentTransMap = new HashMap();
                                            investmentTransMap.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                            investmentTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                                            investmentTransMap.put("BRANCH_CODE", _branchCode);
                                            investmentTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                            investmentTransMap.put("USER_ID", objTO.getStatusBy());
                                            investmentTransMap.put("INVESTMENT_NO", objTO.getInvestmentBehaves() + "_" + objTO.getInvestmentName());
                                            investmentTransMap.put("TRANS_MOD_TYPE", "INV");
                                            if (objTO.getInvestmentAmount().doubleValue() != 0.0) {
                                                investmentTransMap.put("ACCT_HEAD", achdMap.get("IINVESTMENT_AC_HD"));
                                                investmentTransMap.put("TRANS_AMOUNT", objTO.getInvestmentAmount());
                                                investmentTransMap.put("AUTHORIZEREMARKS", "IINVESTMENT_CLOSER_IntAmt");
                                                investmentTransMap.put("generateSingleTransId", generateSingleTransId);
                                                investmentTransMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                               // System.out.println("ppppp66666");
                                                cashList.add(createCashTransactionTO(investmentTransMap));
                                            }

                                            //Premature Closure Interest
                                            if (CommonUtil.convertObjToDouble(objTO.getPrematureInt()).doubleValue() > 0) {
                                                investmentTransMap.put("ACCT_HEAD", achdMap.get("INTEREST_RECIVED_AC_HD"));
                                                investmentTransMap.put("TRANS_AMOUNT", objTO.getPrematureInt());
                                                investmentTransMap.put("AUTHORIZEREMARKS", "IINVESTMENT_CLOSER_IntAmt");
                                                investmentTransMap.put("generateSingleTransId", generateSingleTransId);
                                                investmentTransMap.put("USER_ID", objTO.getStatusBy());
                                                investmentTransMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                cashList.add(createCashTransactionTO(investmentTransMap));
                                               // System.out.println("pppp7777");
                                            }
//                                            System.out.println("ppp888888");
//                                            System.out.print("###### cashList@@@@1111: " + cashList);
                                        }
                                    }
                                } else {
                                    getTransDetails = false;
                                   // System.out.println("#################### Start Interest Payable ");
                                    LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                    for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                        
                                        double tranAmt = 0.0;
                                        objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                        if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                            txMap.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                            txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                            txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                            txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                            txMap.put(TransferTrans.PARTICULARS, objTO.getDrParticulars());
                                            txMap.put("AUTHORIZEREMARKS", "TRANSACTIONAMOUNT");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                            if (CommonUtil.convertObjToStr(objTransactionTO.getProductType()).equals("GL")) {
                                                txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                            }
                                            //Added By Vivek 23-July-2013
                                     //       System.out.println("objTransactionTO>>>>1111>>>>>" + objTransactionTO);
                                            if (!objTransactionTO.getProductType().equals("") && objTransactionTO.getProductType().equals("AB")) {
                                                HashMap acheadMap = new HashMap();
                                                acheadMap.put("ACT_MASTER_ID", objTransactionTO.getDebitAcctNo());
                                                List borrowingAchdLst = ServerUtil.executeQuery("getSelectAcHdForOthrBank", acheadMap);
                                                if (borrowingAchdLst != null && borrowingAchdLst.size() > 0) {
                                                    acheadMap = new HashMap();
                                                    acheadMap = (HashMap) borrowingAchdLst.get(0);
                                       //             System.out.println("acheadMap othr bank22222--------------->" + acheadMap);
                                                    txMap.put(TransferTrans.DR_AC_HD, (String) acheadMap.get("PRINCIPAL_AC_HD"));

                                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);
                                                } else {
                                                    throw new TTException("Account heads not set properly...");
                                                }
                                            }
                                              //Added By Suresh 06-Sep-2012
                                            if (!objTransactionTO.getProductType().equals("") && objTransactionTO.getProductType().equals("BRW")) {
                                                HashMap acheadMap = new HashMap();
                                                acheadMap.put("BORROWING_NO", objTransactionTO.getDebitAcctNo());
                                                List borrowingAchdLst = ServerUtil.executeQuery("Borrowings.getAcHeads", acheadMap);
                                                if (borrowingAchdLst != null && borrowingAchdLst.size() > 0) {
                                                    acheadMap = new HashMap();
                                                    acheadMap = (HashMap) borrowingAchdLst.get(0);
                                                    System.out.println("acheadMap--------------->" + acheadMap);
                                                    txMap.put(TransferTrans.DR_AC_HD, (String) acheadMap.get("PRINCIPAL_GRP_HEAD"));
                                                    txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_INTEREST");
                                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                                } else {
                                                    throw new TTException("Account heads not set properly...");
                                                }
                                            }
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
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
                                            // tranAmt=CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                            tranAmt = CommonUtil.convertObjToDouble(objTO.getInvestmentAmount()).doubleValue();
                                            //-CommonUtil.convertObjToDouble(objTO.getPrematureInt()).doubleValue();
                                         //   System.out.println("txmap@@12@@>>>" + txMap);
                                            txMap.put("generateSingleTransId", generateSingleTransId);
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
                                            txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                            txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, tranAmt));

                                            //------------------------------------Below Code rewritten by nithya  -----------------------------------------//
                                            //excess intrest Credit 

//                                            tranAmt = CommonUtil.convertObjToDouble(objTO.getPrematureInt()).doubleValue();
//                                            if (tranAmt > 0) {
//                                                System.out.println("executing here ...debit " + tranAmt);
//                                                txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
//                                                txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
//                                                txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
//                                                txMap.put(TransferTrans.PARTICULARS, objTO.getDrParticulars());
//                                                txMap.put("AUTHORIZEREMARKS", "TRANSACTIONAMOUNT");
//                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
//                                                // txMap.put(TransferTrans.CR_INST_TYPE,"VOUCHER");
//                                                if (CommonUtil.convertObjToStr(objTransactionTO.getProductType()).equals("GL")) {
//                                                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
//                                                }
//                                                //Added By Vivek 23-July-2013
//                                             //   System.out.println("objTransactionTO>>>>1111>>>>>" + objTransactionTO);
//                                                if (!objTransactionTO.getProductType().equals("") && objTransactionTO.getProductType().equals("AB")) {
//                                                    HashMap acheadMap = new HashMap();
//                                                    acheadMap.put("ACT_MASTER_ID", objTransactionTO.getDebitAcctNo());
//                                                    List borrowingAchdLst = ServerUtil.executeQuery("getSelectAcHdForOthrBank", acheadMap);
//                                                    if (borrowingAchdLst != null && borrowingAchdLst.size() > 0) {
//                                                        acheadMap = new HashMap();
//                                                        acheadMap = (HashMap) borrowingAchdLst.get(0);
//                                                    //    System.out.println("acheadMap othr bank22222--------------->" + acheadMap);
//                                                        txMap.put(TransferTrans.DR_AC_HD, (String) acheadMap.get("PRINCIPAL_AC_HD"));
//
//                                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);
//                                                    } else {
//                                                        throw new TTException("Account heads not set properly...");
//                                                    }
//                                                }
//                                                //Added By Suresh 06-Sep-2012
//                                                if (!objTransactionTO.getProductType().equals("") && objTransactionTO.getProductType().equals("BRW")) {
//                                                    HashMap acheadMap = new HashMap();
//                                                    acheadMap.put("BORROWING_NO", objTransactionTO.getDebitAcctNo());
//                                                    List borrowingAchdLst = ServerUtil.executeQuery("Borrowings.getAcHeads", acheadMap);
//                                                    if (borrowingAchdLst != null && borrowingAchdLst.size() > 0) {
//                                                        acheadMap = new HashMap();
//                                                        acheadMap = (HashMap) borrowingAchdLst.get(0);
//                                                      //  System.out.println("acheadMap--------------->" + acheadMap);
//                                                        txMap.put(TransferTrans.DR_AC_HD, (String) acheadMap.get("PRINCIPAL_GRP_HEAD"));
//                                                        txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_INTEREST");
//                                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
//                                                    } else {
//                                                        throw new TTException("Account heads not set properly...");
//                                                    }
//                                                }
//                                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
//                                                if (objTransactionTO.getProductType().equals("OA")){
//                                                    txMap.put("TRANS_MOD_TYPE", "OA");
//                                                }else if(objTransactionTO.getProductType().equals("AB")){
//                                                    txMap.put("TRANS_MOD_TYPE", "AB");
//                                                }else if(objTransactionTO.getProductType().equals("SA")){
//                                                    txMap.put("TRANS_MOD_TYPE", "SA");
//                                                }else if(objTransactionTO.getProductType().equals("TL")){
//                                                    txMap.put("TRANS_MOD_TYPE", "TL");
//                                                }else if(objTransactionTO.getProductType().equals("AD")){
//                                                    txMap.put("TRANS_MOD_TYPE", "AD");
//                                                }else
//                                                    txMap.put("TRANS_MOD_TYPE", "GL");
//                                                txMap.put("generateSingleTransId", generateSingleTransId);
//                                                txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
//                                                txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
//                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, tranAmt));
//
//                                            }
//
//                                            //DEBIT
//                                            if (CommonUtil.convertObjToDouble(objTO.getPrematureInt()).doubleValue() > 0) {
//                                                System.out.println("executing here ...credit " + CommonUtil.convertObjToDouble(objTO.getPrematureInt()).doubleValue());
//                                                txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("INTEREST_RECIVED_AC_HD"));
//                                                txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_CLOSER");
//                                                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
//                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
//                                                txMap.put(TransferTrans.PARTICULARS, objTO.getDrParticulars());
//                                                txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
//                                                txMap.put("TRANS_MOD_TYPE", "INV");
//                                                //System.out.println("txmap@@13@@>>>" + txMap);
//                                                txMap.put("generateSingleTransId", generateSingleTransId);
//                                                txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
//                                                transferList.add(objTransferTrans.getCreditTransferTO(txMap, CommonUtil.convertObjToDouble(objTO.getPrematureInt()).doubleValue()));
//                                            }
//                                            
                                            //----------------------------------------------------------------------------------------------//
                                            
                                            // --------------------Excess interest transaction debit from interest on investment , credit to other bank account Jira ID KD3----------------------------------------------------------------------//
                                            
                                            tranAmt = CommonUtil.convertObjToDouble(objTO.getPrematureInt()).doubleValue();
                                            if (tranAmt > 0) {
                                                System.out.println("Excess interest transaction ... credit  " + tranAmt);
                                                txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                                txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                                txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                                txMap.put(TransferTrans.PARTICULARS, objTO.getDrParticulars());
                                                txMap.put("AUTHORIZEREMARKS", "TRANSACTIONAMOUNT");
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                // txMap.put(TransferTrans.CR_INST_TYPE,"VOUCHER");
                                                if (CommonUtil.convertObjToStr(objTransactionTO.getProductType()).equals("GL")) {
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                                }
                                                //Added By Vivek 23-July-2013
                                             //   System.out.println("objTransactionTO>>>>1111>>>>>" + objTransactionTO);
                                                if (!objTransactionTO.getProductType().equals("") && objTransactionTO.getProductType().equals("AB")) {
                                                    HashMap acheadMap = new HashMap();
                                                    acheadMap.put("ACT_MASTER_ID", objTransactionTO.getDebitAcctNo());
                                                    List borrowingAchdLst = ServerUtil.executeQuery("getSelectAcHdForOthrBank", acheadMap);
                                                    if (borrowingAchdLst != null && borrowingAchdLst.size() > 0) {
                                                        acheadMap = new HashMap();
                                                        acheadMap = (HashMap) borrowingAchdLst.get(0);
                                                    //    System.out.println("acheadMap othr bank22222--------------->" + acheadMap);
                                                        txMap.put(TransferTrans.CR_AC_HD, (String) acheadMap.get("PRINCIPAL_AC_HD"));

                                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);
                                                    } else {
                                                        throw new TTException("Account heads not set properly...");
                                                    }
                                                }
                                                //Added By Suresh 06-Sep-2012
                                                if (!objTransactionTO.getProductType().equals("") && objTransactionTO.getProductType().equals("BRW")) {
                                                    HashMap acheadMap = new HashMap();
                                                    acheadMap.put("BORROWING_NO", objTransactionTO.getDebitAcctNo());
                                                    List borrowingAchdLst = ServerUtil.executeQuery("Borrowings.getAcHeads", acheadMap);
                                                    if (borrowingAchdLst != null && borrowingAchdLst.size() > 0) {
                                                        acheadMap = new HashMap();
                                                        acheadMap = (HashMap) borrowingAchdLst.get(0);
                                                      //  System.out.println("acheadMap--------------->" + acheadMap);
                                                        txMap.put(TransferTrans.CR_AC_HD, (String) acheadMap.get("PRINCIPAL_GRP_HEAD"));
                                                        txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_INTEREST");
                                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    } else {
                                                        throw new TTException("Account heads not set properly...");
                                                    }
                                                }
                                                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
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
                                                txMap.put("generateSingleTransId", generateSingleTransId);
                                                txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                                txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));                                               
                                                transferList.add(objTransferTrans.getCreditTransferTO(txMap, tranAmt));
                                            }

                                            //DEBIT
                                            if (CommonUtil.convertObjToDouble(objTO.getPrematureInt()).doubleValue() > 0) {
                                                System.out.println("Excess interest transaction ... debit " + CommonUtil.convertObjToDouble(objTO.getPrematureInt()).doubleValue());
                                                txMap.put(TransferTrans.DR_AC_HD, (String) achdMap.get("INTEREST_RECIVED_AC_HD"));
                                                txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_CLOSER");
                                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put(TransferTrans.PARTICULARS, objTO.getDrParticulars());
                                                txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                                txMap.put("TRANS_MOD_TYPE", "INV");
                                                //System.out.println("txmap@@13@@>>>" + txMap);
                                                txMap.put("generateSingleTransId", generateSingleTransId);
                                                txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));                                                
                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, CommonUtil.convertObjToDouble(objTO.getPrematureInt()).doubleValue()));
                                            }
                                            
                                            
                                            //--------------------End of Excess interest transaction-----------------------------------------------------------------//
                                            
                                            //Premature Closure Interest
                                            if (CommonUtil.convertObjToDouble(objTO.getBrokenPeriodInterest()).doubleValue() > 0) {
                                                txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("INTEREST_RECIVED_AC_HD"));
                                                txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_CLOSER");
                                                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put(TransferTrans.PARTICULARS, objTO.getCrParticulars());
                                                txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                                txMap.put("TRANS_MOD_TYPE", "INV");
                                                //System.out.println("objTO.getPrematureInt()>>>" + objTO.getPrematureInt());
                                                txMap.put("generateSingleTransId", generateSingleTransId);
                                                txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                transferList.add(objTransferTrans.getCreditTransferTO(txMap, CommonUtil.convertObjToDouble(objTO.getBrokenPeriodInterest()).doubleValue()));
                                                //System.out.println("objTransactionTO>>>>1111@@@@acved>>>>>" + objTransactionTO);
                                                if (!objTransactionTO.getProductType().equals("") && objTransactionTO.getProductType().equals("AB")) {
                                                    HashMap acheadMap = new HashMap();
                                                    acheadMap.put("ACT_MASTER_ID", objTransactionTO.getDebitAcctNo());
                                                    List borrowingAchdLst = ServerUtil.executeQuery("getSelectAcHdForOthrBank", acheadMap);
                                                    if (borrowingAchdLst != null && borrowingAchdLst.size() > 0) {
                                                        acheadMap = new HashMap();
                                                        acheadMap = (HashMap) borrowingAchdLst.get(0);
                                                        System.out.println("acheadMap othr bank1111@@@@--------------->" + acheadMap);
                                                        txMap.put(TransferTrans.DR_AC_HD, (String) acheadMap.get("PRINCIPAL_AC_HD"));
                                                        txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_INTEREST");
                                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);

                                                    }
                                                } else {
                                                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                                }
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
                                                txMap.put(TransferTrans.PARTICULARS, objTO.getDrParticulars());
                                               // System.out.println("txMap@@11@@>>>>>" + txMap);
                                                txMap.put("generateSingleTransId", generateSingleTransId);
                                                txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                                txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, CommonUtil.convertObjToDouble(objTO.getBrokenPeriodInterest()).doubleValue()));
                                            }
                                            //CREDIT
                                            if ((objTO.getInvestmentAmount().doubleValue()) > 0) {
                                                txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("IINVESTMENT_AC_HD"));
                                                txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_CLOSER");
                                                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put(TransferTrans.PARTICULARS, objTO.getCrParticulars());
                                                txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                                txMap.put("TRANS_MOD_TYPE", "INV");
                                                txMap.put("generateSingleTransId", generateSingleTransId);
                                                txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getInvestmentAmount().doubleValue()));
                                            }

                                            //System.out.println("transferList total888 ------------->" + transferList);
                                        } else {
                                            //CREDIT
                                            //INTEREST PAYABLE CASH --> Transaction_To Amount Credit to FD GL_Account
                                            //tranAmt=CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();

                                            //System.out.println("objto in payable case   " + objTO);
                                            tranAmt = CommonUtil.convertObjToDouble(objTO.getInvestmentAmount()).doubleValue();
                                            if (investMentType.equals("OTHER_BANK_SSD") && tranAmt == 0.0) { //vv
                                                tranAmt = CommonUtil.convertObjToDouble(objTO.getInvestmentAmount());
                                            }
                                            HashMap investmentTransMap = new HashMap();
                                            //System.out.println("pppp444444");
                                            investmentTransMap.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                            investmentTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                                            investmentTransMap.put("BRANCH_CODE", _branchCode);
                                            investmentTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                            investmentTransMap.put("USER_ID", objTO.getStatusBy());
                                            investmentTransMap.put("INVESTMENT_NO", objTO.getInvestmentBehaves() + "_" + objTO.getInvestmentName());
                                            investmentTransMap.put("TRANS_MOD_TYPE", "INV");
                                            //System.out.println("tranAmt ??>>>>" + tranAmt + "objTO.getinvestmentamt>>>>" + objTO.getInvestmentAmount());
                                            if (tranAmt > 0) {
                                                investmentTransMap.put("ACCT_HEAD", achdMap.get("IINVESTMENT_AC_HD"));
                                                investmentTransMap.put("TRANS_AMOUNT", String.valueOf(tranAmt));
                                                investmentTransMap.put("AUTHORIZEREMARKS", "IINVESTMENT_CLOSER_IntAmt");
                                                investmentTransMap.put("generateSingleTransId", generateSingleTransId);
                                                investmentTransMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                              //  System.out.println("ppp9999999");
                                                cashList.add(createCashTransactionTO(investmentTransMap));
                                            }
                                            tranAmt = CommonUtil.convertObjToDouble(objTO.getPrematureInt()).doubleValue();
                                            if (tranAmt > 0) {
                                                investmentTransMap.put("ACCT_HEAD", (String) achdMap.get("INTEREST_RECIVED_AC_HD"));
                                                investmentTransMap.put("TRANS_AMOUNT", String.valueOf(tranAmt));
                                                investmentTransMap.put("AUTHORIZEREMARKS", "IINVESTMENT_CLOSER_IntAmt");
                                                investmentTransMap.put("generateSingleTransId", generateSingleTransId);
                                                investmentTransMap.put("USER_ID", objTO.getStatusBy());
                                                investmentTransMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                cashList.add(createCashDebitTransactionTO(investmentTransMap));
                                            }
                                            tranAmt = CommonUtil.convertObjToDouble(objTO.getBrokenPeriodInterest()).doubleValue();
                                            if (tranAmt > 0) {
                                                investmentTransMap.put("ACCT_HEAD", (String) achdMap.get("INTEREST_RECIVED_AC_HD"));
                                                investmentTransMap.put("TRANS_AMOUNT", String.valueOf(tranAmt));
                                                investmentTransMap.put("AUTHORIZEREMARKS", "IINVESTMENT_CLOSER_IntAmt");
                                                investmentTransMap.put("generateSingleTransId", generateSingleTransId);
                                                investmentTransMap.put("USER_ID", objTO.getStatusBy());
                                                investmentTransMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                cashList.add(createCashDebitTransactionTO(investmentTransMap));
                                            }
                                            //DEBIT
//                                        if(CommonUtil.convertObjToDouble(objTO.getPrematureInt()).doubleValue()>0){
//                                            txMap.put(TransferTrans.DR_AC_HD, (String)achdMap.get("INTEREST_RECIVED_AC_HD"));
//                                            txMap.put("AUTHORIZEREMARKS","IINVESTMENT_CLOSER");
//                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
//                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
//                                            txMap.put(TransferTrans.PARTICULARS, debitParticular);
//                                            txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
//                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
//                                            System.out.println("txmap@@14@@>>>"+txMap);
//                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, CommonUtil.convertObjToDouble(objTO.getPrematureInt()).doubleValue()));
//                                        }
//                                        //CREDIT
//                                        if(CommonUtil.convertObjToDouble(objTO.getPrematureInt()).doubleValue()>0){
//                                            txMap.put(TransferTrans.CR_AC_HD, (String)achdMap.get("IINVESTMENT_AC_HD"));
//                                            txMap.put("AUTHORIZEREMARKS","IINVESTMENT_CLOSER");
//                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
//                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
//                                            txMap.put(TransferTrans.PARTICULARS, creditParticular);
//                                            txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
//                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
//                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, CommonUtil.convertObjToDouble(objTO.getPrematureInt()).doubleValue()));
//                                        }
                                        }
                                    }
                                }
                            } else if (investMentType.equals("OTHER_BANK_CCD")) {
                                LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                    
                                    double tranAmt = 0.0;
                                    objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                    if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                        txMap.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                        txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                        txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                        txMap.put(TransferTrans.PARTICULARS, objTO.getDrParticulars());
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                        txMap.put("AUTHORIZEREMARKS", "TRANSACTIONAMOUNT");
                                        if (CommonUtil.convertObjToStr(objTransactionTO.getProductType()).equals("GL")) {
                                            txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        }
                                        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                        tranAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
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
                                       // System.out.println("txmap@@15@@>>>" + txMap);
                                        txMap.put("generateSingleTransId", generateSingleTransId);
                                        txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                        txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, tranAmt));

                                        HashMap whereMap = new HashMap();   //Credit Transactions
                                        String intWithPrincipal = "";
                                        whereMap.put("INVESTMENT_TYPE", CommonUtil.convertObjToStr(objTO.getInvestmentBehaves()));
                                        whereMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objTO.getInvestmentID()));
                                        whereMap.put("INVESTMENT_ID", CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                        List depMasterList = ServerUtil.executeQuery("getIntWithPrincipalFromDepMaster", whereMap);
                                        if (depMasterList != null && depMasterList.size() > 0) {
                                            whereMap = (HashMap) depMasterList.get(0);
                                            intWithPrincipal = CommonUtil.convertObjToStr(whereMap.get("INT_WITH_PRINCIPAL"));
                                            if (!intWithPrincipal.equals("") && intWithPrincipal.equals("Y")) {
                                                if (objTO.getInvestmentAmount().doubleValue() != 0.0) {
                                                    txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("IINVESTMENT_AC_HD"));
                                                    txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_CLOSER");
                                                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, objTO.getCrParticulars());
                                                    txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                                    txMap.put("TRANS_MOD_TYPE", "INV");
                                                    txMap.put("generateSingleTransId", generateSingleTransId);
                                                    txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getInvestmentAmount().doubleValue()));
                                                }
                                            } else {
                                                if (objTO.getInvestmentAmount().doubleValue() != 0.0) {
                                                    txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("IINVESTMENT_AC_HD"));
                                                    txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_CLOSER");
                                                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, objTO.getCrParticulars());
                                                    txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                                    txMap.put("TRANS_MOD_TYPE", "INV");
                                                    txMap.put("generateSingleTransId", generateSingleTransId);
                                                    txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getInvestmentAmount().doubleValue()));
                                                }
                                                whereMap = new HashMap();
                                                double intReceivable = 0.0;
                                                whereMap.put("INVESTMENT_ID", CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                                List intReceivableList = ServerUtil.executeQuery("gerInvestmentInterestReceivable", whereMap);
                                                if (intReceivableList != null && intReceivableList.size() > 0) {
                                                    whereMap = (HashMap) intReceivableList.get(0);
                                                    intReceivable = CommonUtil.convertObjToDouble(whereMap.get("INTEREST_RECEIVABLE")).doubleValue();
                                                    if (intReceivable > 0) {
                                                        txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("INTEREST_RECEIVABLE_AC_HD"));
                                                        txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_CLOSER");
                                                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                        txMap.put(TransferTrans.PARTICULARS, objTO.getCrParticulars());
                                                        txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                                        txMap.put("TRANS_MOD_TYPE", "INV");
                                                        txMap.put("generateSingleTransId", generateSingleTransId);
                                                        txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, intReceivable));
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        HashMap whereMap = new HashMap();
                                        String intWithPrincipal = "";
                                        whereMap.put("INVESTMENT_TYPE", CommonUtil.convertObjToStr(objTO.getInvestmentBehaves()));
                                        whereMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objTO.getInvestmentID()));
                                        whereMap.put("INVESTMENT_ID", CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                        List depMasterList = ServerUtil.executeQuery("getIntWithPrincipalFromDepMaster", whereMap);
                                        if (depMasterList != null && depMasterList.size() > 0) {
                                            whereMap = (HashMap) depMasterList.get(0);
                                            intWithPrincipal = CommonUtil.convertObjToStr(whereMap.get("INT_WITH_PRINCIPAL"));
                                            if (!intWithPrincipal.equals("") && intWithPrincipal.equals("Y")) {
                                                HashMap investmentTransMap = new HashMap();
                                                investmentTransMap.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                                investmentTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                                                investmentTransMap.put("BRANCH_CODE", _branchCode);
                                                investmentTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                                investmentTransMap.put("USER_ID", objTO.getStatusBy());
                                                investmentTransMap.put("INVESTMENT_NO", objTO.getInvestmentBehaves() + "_" + objTO.getInvestmentName());
                                                if (objTO.getInvestmentAmount().doubleValue() != 0.0) {
                                                    investmentTransMap.put("ACCT_HEAD", achdMap.get("IINVESTMENT_AC_HD"));
                                                    investmentTransMap.put("TRANS_AMOUNT", objTO.getInvestmentAmount());
                                                    investmentTransMap.put("AUTHORIZEREMARKS", "IINVESTMENT_CLOSER");
                                                    investmentTransMap.put("generateSingleTransId", generateSingleTransId);
                                                    investmentTransMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                    cashList.add(createCashTransactionTO(investmentTransMap));
                                                }
                                               // System.out.print("###### cashList@@@2222: " + cashList);
                                            } else {
                                                HashMap investmentTransMap = new HashMap();
                                                investmentTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                                                investmentTransMap.put("BRANCH_CODE", _branchCode);
                                                investmentTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                                investmentTransMap.put("USER_ID", objTO.getStatusBy());
                                                investmentTransMap.put("INVESTMENT_NO", objTO.getInvestmentBehaves() + "_" + objTO.getInvestmentName());
                                                if (objTO.getInvestmentAmount().doubleValue() != 0.0) {
                                                    investmentTransMap.put("ACCT_HEAD", achdMap.get("IINVESTMENT_AC_HD"));
                                                    investmentTransMap.put("TRANS_AMOUNT", objTO.getInvestmentAmount());
                                                    investmentTransMap.put("AUTHORIZEREMARKS", "IINVESTMENT_CLOSER");
                                                    investmentTransMap.put("TRANS_MOD_TYPE", "INV");
                                                    investmentTransMap.put("generateSingleTransId", generateSingleTransId);
                                                    investmentTransMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                    cashList.add(createCashTransactionTO(investmentTransMap));
                                                }
                                                whereMap = new HashMap();
                                                double intReceivable = 0.0;
                                                whereMap.put("INVESTMENT_ID", CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                                List intReceivableList = ServerUtil.executeQuery("gerInvestmentInterestReceivable", whereMap);
                                                if (intReceivableList != null && intReceivableList.size() > 0) {
                                                    whereMap = (HashMap) intReceivableList.get(0);
                                                    intReceivable = CommonUtil.convertObjToDouble(whereMap.get("INTEREST_RECEIVABLE")).doubleValue();
                                                    if (intReceivable > 0) {
                                                        investmentTransMap = new HashMap();
                                                        investmentTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                                                        investmentTransMap.put("BRANCH_CODE", _branchCode);
                                                        investmentTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                                        investmentTransMap.put("USER_ID", objTO.getStatusBy());
                                                        investmentTransMap.put("INVESTMENT_NO", objTO.getInvestmentBehaves() + "_" + objTO.getInvestmentName());
                                                        investmentTransMap.put("ACCT_HEAD", achdMap.get("INTEREST_RECEIVABLE_AC_HD"));
                                                        investmentTransMap.put("TRANS_AMOUNT", String.valueOf(intReceivable));
                                                        investmentTransMap.put("AUTHORIZEREMARKS", "IINVESTMENT_CLOSER");
                                                        investmentTransMap.put("TRANS_MOD_TYPE", "INV");
                                                        investmentTransMap.put("generateSingleTransId", generateSingleTransId);
                                                        investmentTransMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                        cashList.add(createCashTransactionTO(investmentTransMap));
                                                       // System.out.print("###### cashList@@@3333: " + cashList);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (objTO.getTrnCode().equals("Purchase")) {      //SHARE TRANSACTION
                            if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                                LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                    
                                    double tranAmt = 0.0;
                                    objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                    if (investMentType.equals("SHARES_DCB") || investMentType.equals("SHARE_OTHER_INSTITUTIONS")) {

                                        if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                            txMap.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                            txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                            txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                            txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                            txMap.put(TransferTrans.PARTICULARS, objTO.getCrParticulars());
                                            txMap.put("AUTHORIZEREMARKS", "TRANSACTIONAMOUNT");
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                            if (CommonUtil.convertObjToStr(objTransactionTO.getProductType()).equals("GL")) {
                                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                            }
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
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
                                            //   tranAmt=CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                            tranAmt = objTO.getInvestmentAmount().doubleValue();
                                            txMap.put("generateSingleTransId", generateSingleTransId);
                                            txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, tranAmt));
                                            if (objTO.getInvestmentAmount().doubleValue() != 0.0) {
                                                double feesPaid = 0.0;
                                                double shareAmt = 0.0;
                                                shareAmt = objTO.getInvestmentAmount().doubleValue();
                                                feesPaid = objTO.getPurchaseRate().doubleValue();
//                                                System.out.println("#### Total Share Amount : " + shareAmt);
//                                                System.out.println("#### feesPaid : " + feesPaid);
                                                if (feesPaid > 0) {
                                                    txMap.put(TransferTrans.DR_AC_HD, (String) achdMap.get("CHARGE_PAID_AC_HD"));
                                                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put("AUTHORIZEREMARKS", "SHARE_FEES_AC_HD");
                                                    txMap.put(TransferTrans.PARTICULARS, objTO.getDrParticulars());
                                                    txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                                    txMap.put("TRANS_MOD_TYPE", "INV");
                                                    txMap.put("generateSingleTransId", generateSingleTransId);
                                                   // System.out.println("txmap@@16@@>>>" + txMap);
                                                    txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, feesPaid));
                                                    shareAmt -= feesPaid;
                                                }
                                                if (shareAmt > 0) {
                                                    txMap.put(TransferTrans.DR_AC_HD, (String) achdMap.get("IINVESTMENT_AC_HD"));
                                                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put("AUTHORIZEREMARKS", "SHARE_IINVESTMENT_AC_HD");
                                                    txMap.put(TransferTrans.PARTICULARS, objTO.getDrParticulars());
                                                    txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                                   // System.out.println("txmap@@17@@>>>" + txMap);
                                                    txMap.put("TRANS_MOD_TYPE", "INV");
                                                    txMap.put("generateSingleTransId", generateSingleTransId);
                                                    txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, shareAmt));
                                                }
                                             //   System.out.println("transferList 4444 ------------->" + transferList);
                                            }
                                        } else {
                                            HashMap investmentTransMap = new HashMap();
                                            investmentTransMap.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                            investmentTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                                            investmentTransMap.put("BRANCH_CODE", _branchCode);
                                            investmentTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                            investmentTransMap.put("USER_ID", objTO.getStatusBy());
                                            investmentTransMap.put("INVESTMENT_NO", objTO.getInvestmentBehaves() + "_" + objTO.getInvestmentName());
                                            investmentTransMap.put("TRANS_MOD_TYPE", "INV");
                                            if (objTO.getInvestmentAmount().doubleValue() != 0.0) {
                                                double feesPaid = 0.0;
                                                double shareAmt = 0.0;
                                                shareAmt = objTO.getInvestmentAmount().doubleValue();
                                                feesPaid = objTO.getPurchaseRate().doubleValue();
//                                                System.out.println("#### Total Share Amount : " + shareAmt);
//                                                System.out.println("#### feesPaid : " + feesPaid);
                                                if (feesPaid > 0) {
                                                    investmentTransMap.put("ACCT_HEAD", achdMap.get("CHARGE_PAID_AC_HD"));
                                                    investmentTransMap.put("TRANS_AMOUNT", String.valueOf(feesPaid));
                                                    investmentTransMap.put("AUTHORIZEREMARKS", "SHARE_FEES_AC_HD");
                                                    investmentTransMap.put("generateSingleTransId", generateSingleTransId);
                                                    investmentTransMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                    cashList.add(createCashDebitTransactionTO(investmentTransMap));
                                                    shareAmt -= feesPaid;
                                                }
                                              //  System.out.println("####Share Amount : " + shareAmt);
                                                if (shareAmt > 0) {
                                                    investmentTransMap.put("ACCT_HEAD", achdMap.get("IINVESTMENT_AC_HD"));
                                                    investmentTransMap.put("TRANS_AMOUNT", String.valueOf(shareAmt));
                                                    investmentTransMap.put("AUTHORIZEREMARKS", "SHARE_IINVESTMENT_AC_HD");
                                                    investmentTransMap.put("generateSingleTransId", generateSingleTransId);
                                                    investmentTransMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                                    cashList.add(createCashDebitTransactionTO(investmentTransMap));
                                                }
                                          //     System.out.print("###### cashList@@@4444: " + cashList);
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (objTO.getTrnCode().equals("Divident")) {   //Share Divident
                            if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs") && (investMentType.equals("SHARES_DCB") || investMentType.equals("SHARE_OTHER_INSTITUTIONS"))) {
                                LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                    
                                    double tranAmt = 0.0;
                                    objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                    if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                        txMap.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                        txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                        txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                        txMap.put(TransferTrans.PARTICULARS, objTO.getDrParticulars());
                                        txMap.put("AUTHORIZEREMARKS", "TRANSACTIONAMOUNT");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
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
                                        if (CommonUtil.convertObjToStr(objTransactionTO.getProductType()).equals("GL")) {
                                            txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        }
                                        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                        txMap.put("generateSingleTransId", generateSingleTransId);
                                        //tranAmt=CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                        tranAmt = objTO.getBrokenPeriodInterest().doubleValue();
                                        //System.out.println("txmap@@18@@>>>" + txMap);
                                        txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, tranAmt));

                                        if (objTO.getBrokenPeriodInterest().doubleValue() != 0.0) {
                                            txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("DIVIDENT_RECIVED_AC_HD"));
                                            txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_SHARE_DIVIDENT");
                                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, objTO.getCrParticulars());
                                            txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                                            txMap.put("TRANS_MOD_TYPE", "INV");
                                            txMap.put("generateSingleTransId", generateSingleTransId);
                                            txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getBrokenPeriodInterest().doubleValue()));
                                        }
                                    } else {
                                        HashMap investmentTransMap = new HashMap();
                                        investmentTransMap.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                        investmentTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                                        investmentTransMap.put("BRANCH_CODE", _branchCode);
                                        investmentTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                        investmentTransMap.put("USER_ID", objTO.getStatusBy());
                                        investmentTransMap.put("INVESTMENT_NO", objTO.getInvestmentBehaves() + "_" + objTO.getInvestmentName());
                                        investmentTransMap.put("TRANS_MOD_TYPE", "INV");
                                        if (objTO.getBrokenPeriodInterest().doubleValue() != 0.0) {
                                            investmentTransMap.put("ACCT_HEAD", achdMap.get("DIVIDENT_RECIVED_AC_HD"));
                                            investmentTransMap.put("TRANS_AMOUNT", objTO.getBrokenPeriodInterest());
                                            investmentTransMap.put("AUTHORIZEREMARKS", "IINVESTMENT_SHARE_DIVIDENT");
                                            investmentTransMap.put("generateSingleTransId", generateSingleTransId);
                                            investmentTransMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                                            cashList.add(createCashTransactionTO(investmentTransMap));
                                        }
                                    }
                                }
                            }
                        }

//                    if (transferList!=null && transferList.size()>0) {
//                        doDebitCredit(transferList, _branchCode, false);
//                    }
//                     if(cashList!=null && cashList.size()>0) {
//                        doCashTrans(cashList, _branchCode, false);
//                    }
//                    transferList = null;
//                    
////                    changed here 
//                    if(getTransDetails){
//                    getTransDetails(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
//                    returnDataMap.put("BATCH_ID",CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
//                }else{
//                        getCashTransDetails(CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
//                    }

                    }///ddddddddddddd
                    else {
                        throw new Exception("investment  Transaction is not proper");
                    }
                }

            } else {
                throw new Exception("investment  Config Date is Not set...");
            }

        }
        if (transferList != null && transferList.size() > 0) {
            doDebitCredit(transferList, _branchCode, false);
        }
       // System.out.println("cashList<<<<11111@@@>>>>>>" + cashList);
        if (cashList != null && cashList.size() > 0) {
            doCashTrans(cashList, _branchCode, false);
        }
        transferList = null;
     //   System.out.println("ppppp00001111");
//                    changed here 
        if (getTransDetails) {
       //     System.out.println("ppp0002222");
            getTransDetails(cashId,transferId);
            returnDataMap.put("BATCH_ID", CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
        } else {
         //   System.out.println("pppp000044444");
            getCashTransDetails(CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
        }
        return null;
    }
    
    private HashMap closureInterestTransaction(HashMap map) throws Exception {
        return null;
    }

    private void getCashTransDetails(String batchId) throws Exception {
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
        System.out.println("returnDataMap>>>>" + returnDataMap);
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    private CashTransactionTO createCashTransactionTO(HashMap dataMap) throws Exception {
        CashTransactionTO objCashTO = new CashTransactionTO();
        if (dataMap.get("STATUS").equals(CommonConstants.TOSTATUS_INSERT)) {
            objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
            objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.GL));
            objCashTO.setTransType(CommonConstants.CREDIT);
            objCashTO.setParticulars("By Cash : " + CommonUtil.convertObjToStr(dataMap.get("PARTICULARS")));
            objCashTO.setNarration(CommonUtil.convertObjToStr(dataMap.get("NARRATION")));
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
            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("LINK_BATCH_ID")));
            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
            objCashTO.setSingleTransId(CommonUtil.convertObjToStr(dataMap.get("generateSingleTransId")));
            if (dataMap.containsKey("SCREEN_NAME")) {
                objCashTO.setScreenName(CommonUtil.convertObjToStr(dataMap.get("SCREEN_NAME")));
            }
            System.out.println("objCashTO 1st one:" + objCashTO);
        }
        return objCashTO;
    }

    private CashTransactionTO createCashDebitTransactionTO(HashMap dataMap) throws Exception {
        CashTransactionTO objCashTO = new CashTransactionTO();
        if (dataMap.get("STATUS").equals(CommonConstants.TOSTATUS_INSERT)) {
            objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
            objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.GL));
            objCashTO.setTransType(CommonConstants.DEBIT);
            //System.out.println("creditParticular222>>" + creditParticular + "narration1>>>>3434>>>" + narration1);
            objCashTO.setParticulars("By Cash : " + CommonUtil.convertObjToStr(dataMap.get("PARTICULARS")));
            objCashTO.setNarration(CommonUtil.convertObjToStr(dataMap.get("NARRATION")));
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
            objCashTO.setSingleTransId(CommonUtil.convertObjToStr(dataMap.get("generateSingleTransId")));
            objCashTO.setCommand("INSERT");
            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
            if (dataMap.containsKey("SCREEN_NAME")) {
                objCashTO.setScreenName(CommonUtil.convertObjToStr(dataMap.get("SCREEN_NAME")));
            }
            //objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(batchId));
            System.out.println("objCashTO 1st one:" + objCashTO);
        }
        return objCashTO;
    }

    private void getTransDetails(String cashId,String transferId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", transferId);
        if(backDate!=null && !backDate.equals("")){
            getTransMap.put("TRANS_DT", backDate);
        }else{
            getTransMap.put("TRANS_DT", currDt.clone());
        }
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
        returnDataMap = new HashMap();
        List transList = (List) sqlMap.executeQueryForList("getTransferDetailsInvestment", getTransMap);
        if (transList != null && transList.size() > 0) {
            returnDataMap.put("TRANSFER_TRANS_LIST", transList);
        }
        getTransMap.put("BATCH_ID", cashId);
        List cashList = (List) sqlMap.executeQueryForList("getCashDetailsInvestment", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            returnDataMap.put("CASH_TRANS_LIST", cashList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    private void doUpdateTransaction(HashMap map) throws Exception {
        System.out.println("In Side The doUpdateTransaction");
        map.put("BATCHID", objTO.getBatchID());
        map.put("TRANS_DT", currDt.clone());
        map.put("INITIATED_BRANCH", _branchCode);
        TransactionTO transactionTO = new TransactionTO();
        if (map.containsKey("TransactionTO")) {
            HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
            System.out.println("Inside deleteData map containskey :" + map);
            LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
            if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
            }
            transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
            System.out.println("Inside deleteData transactionTO :" + transactionTO);
            if (transactionTO.getTransType().equals("TRANSFER")) {
                List lst = (List) sqlMap.executeQueryForList("getBatchTxTransferTOsAuthorize", map);
                if (!lst.isEmpty()) {
                    TxTransferTO txTransferTO = null;
                    double oldAmount = 0, total = 0;
                    HashMap oldAmountMap = new HashMap();
                    ArrayList transferList = new ArrayList();
                    if (lst != null && lst.size() > 0) {
                        for (int j = 0; j < lst.size(); j++) {
                            total = 0;
                            txTransferTO = (TxTransferTO) lst.get(j);
                            System.out.println("txTransferTO.getAuthorizeRemarks()---------->" + txTransferTO.getAuthorizeRemarks());
                            if (CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("IINVESTMENT_AC_HD")) {
                                total = CommonUtil.convertObjToDouble(objTO.getInvestmentAmount()).doubleValue();
                                System.out.println("Inssid Investment" + total);
                            }

                            if (CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("PREMIUM_PAID_AC_HD")) {
                                total = CommonUtil.convertObjToDouble(objTO.getPremiumAmount()).doubleValue();
                                System.out.println("Inssid Premium" + total);
                            }

                            if (CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("INTEREST_PAID_AC_HD")) {
                                total = CommonUtil.convertObjToDouble(objTO.getBrokenPeriodInterest()).doubleValue();
                                System.out.println("Inssid Interest" + total);
                            }


                            if (CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("TRANSACTIONAMOUNT")) {
                                allowedTransDetailsTO = new LinkedHashMap();
                                System.out.println("transferList total111 ------------->" + transferList);
                                LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                                if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                                    allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                    for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                        double tranAmt = 0.0;
                                        objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                        total = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                        System.out.println("Inssid Transaction" + total);
                                    }
                                }
                            }
                            oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));


                            oldAmount = txTransferTO.getAmount().doubleValue();
                            txTransferTO.setInpAmount(new Double(total));
                            txTransferTO.setAmount(new Double(total));
                            if (objTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                                txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                            } else {
                                txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                            }
                            txTransferTO.setStatusDt(currDt);
                            txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                            oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                            transferList.add(txTransferTO);
                        }
                        TransferTrans transferTrans = new TransferTrans();
                        transferTrans.setOldAmount(oldAmountMap);
                        transferTrans.setInitiatedBranch(_branchCode);
                        transferTrans.doDebitCredit(transferList, _branchCode, false, objTO.getCommand());
                        transferTrans = null;
                    }
                }
            } else if (transactionTO.getTransType().equals("CASH")) {
                CashTransactionDAO cashDAO = new CashTransactionDAO();
                CashTransactionTO cashTO = new CashTransactionTO();
                HashMap cashMap = new HashMap();
                String transId = CommonUtil.convertObjToStr(map.get("BATCHID"));
                cashMap.put("TRANS_ID", transId);
                cashMap.put("TRANS_DT", currDt);
                cashMap.put("INITIATED_BRANCH", _branchCode);
                System.out.println("cashMap 1st :" + cashMap);
                List lstCash = (List) sqlMap.executeQueryForList("getSelectCashTransactionTO", cashMap);
                if (lstCash != null && lstCash.size() > 0) {
                    for (int i = 0; i < lstCash.size(); i++) {
                        cashTO = (CashTransactionTO) lstCash.get(i);
                        cashTO.setCommand("DELETE");
                        cashTO.setStatus(CommonConstants.STATUS_DELETED);
                        cashMap.put("CashTransactionTO", cashTO);
                        cashMap.put("BRANCH_CODE", _branchCode);
                        cashMap.put("USER_ID", map.get("USER_ID"));
                        cashMap.put("OLDAMOUNT", transactionTO.getTransAmt());
                        cashMap.put("SELECTED_BRANCH_ID", _branchCode);
                        System.out.println("cashMap :" + cashMap);
                        cashDAO.execute(cashMap, false);
                    }
                }
                cashMap = null;
                cashTO = null;
                cashDAO = null;
            }
        }
    }

    private void doCashTrans(ArrayList batchList, String branchCode, boolean isAutoAuthorize) throws Exception {
        CashTransactionDAO cashDAO = new CashTransactionDAO();
        HashMap data = new HashMap();
        data.put("DAILYDEPOSITTRANSTO", batchList);
        data.put("COMMAND", objTO.getCommand());
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
        cashId = CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID"));
        if (isAutoAuthorize != true) {
            System.out.println("loanDataMap---------------->" + loanDataMap);
            objTransactionTO.setBatchId(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
            objTransactionTO.setBatchDt(currDt);
            System.out.println("objTO.getInvestmentName()---------------->" + objTO.getInvestmentName());
            String val = (String) objTO.getInvestmentName();
            if (val.length() > 15) {
                val = (String) objTO.getInvestmentName().substring(0, 15);
            }
            //        objTransactionTO.setTransId(String.valueOf(objTO.getInvestmentName().split(objTO.getInvestmentName(),16)));
//            objTransactionTO.setTransId(val);
            objTransactionTO.setTransId(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
            System.out.println("objTransactionTO.getTransId()---------------->" + val);
            objTransactionTO.setStatus(objTO.getStatus());
            objTransactionTO.setBranchId(_branchCode);
            System.out.println("objTransactionTO------------------->" + objTransactionTO);
            sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);

//            HashMap linkBatchMap = new HashMap();                           //update LinkBatchId
////            linkBatchMap.put("LINK_BATCH_ID", loanDataMap.get("TRANS_ID"));
//            linkBatchMap.put("LINK_BATCH_ID", objTO.getInvestment_internal_Id());
//            linkBatchMap.put("TRANS_ID", loanDataMap.get("TRANS_ID"));
//            linkBatchMap.put("TRANS_DT", currDt);
//            linkBatchMap.put("INITIATED_BRANCH", _branchCode);
//            sqlMap.executeUpdate("updateLinkBatchIdCash", linkBatchMap);
//            linkBatchMap = null;


        }
    }
//Nidhin
    private void doDebitCredit(ArrayList batchList, String branchCode, boolean isAutoAuthorize) throws Exception {
        TransferDAO transferDAO = new TransferDAO();
        HashMap finalMap = (HashMap) investmentIdMap.get("FINAL_MAP");
        System.out.println("fmapiii...." + finalMap.size());

        HashMap data = new HashMap();
        data.put("TxTransferTO", batchList);
        data.put("COMMAND", objTO.getCommand());
        data.put("INITIATED_BRANCH", branchCode);
        data.put(CommonConstants.BRANCH_ID, branchCode);
        data.put(CommonConstants.USER_ID, logTO.getUserId());
        data.put(CommonConstants.MODULE, "Transaction");
        data.put(CommonConstants.SCREEN, "");
        data.put("MODE", "MODE");
       // System.out.println("objTO.getInvestment_internal_Id() :"+objTO.getInvestment_internal_Id());
       // System.out.println("batch List :"+batchList +"Sicce of "+batchList.size());
       // System.out.println("investmentIdMap"+investmentIdMap);
//         data.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(batchId));
        if (isAutoAuthorize) {
            HashMap authorizeMap = new HashMap();
            authorizeMap.put("BATCH_ID", null);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
            data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
        }
        System.out.println("backDate^$^$^#"+backDate);
        if(backDate!=null && !backDate.equals("")){
            data.put("TRANS_DATE", getProperDateFormat(backDate));
            data.put("BACK_DATED_TRANSACTION", "BACK_DATED_TRANSACTION");
            data.put("INV_BACK_DATED_TRANSACTION", "INV_BACK_DATED_TRANSACTION");            
            data.put(CommonConstants.SCREEN, "INV_BACK_DATED_TRANSACTION");
        } 
        loanDataMap = transferDAO.execute(data, false);
        transferId = CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID"));
//        }
        if (isAutoAuthorize != true) {
            System.out.println("loanDataMap---------------->" + loanDataMap);
            if (objTransactionTO == null) {
                objTransactionTO = new TransactionTO();
            }
            objTransactionTO.setBatchId(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
            if(backDate!=null && !backDate.equals("")){
                objTransactionTO.setBatchDt(backDate);
            }else{
                objTransactionTO.setBatchDt((Date)currDt.clone());
            }
            System.out.println("objTO.getInvestmentName()---------------->" + objTO.getInvestmentName());
            String val = (String) objTO.getInvestmentName();
            if (val.length() > 15) {
                val = (String) objTO.getInvestmentName().substring(0, 15);
            }
            //        objTransactionTO.setTransId(String.valueOf(objTO.getInvestmentName().split(objTO.getInvestmentName(),16)));
//            objTransactionTO.setTransId(val);
            objTransactionTO.setTransId(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
            System.out.println("objTransactionTO.getTransId()---------------->" + val);
            objTransactionTO.setStatus(objTO.getStatus());
            objTransactionTO.setBranchId(_branchCode);
            System.out.println("objTransactionTO------------------->" + objTransactionTO);
            sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);
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
    
    /**
     * This method is used to Edit the already existing data in the table
     */
    private void updateData(HashMap map) throws Exception {
        logTO.setData(objTO.toString());
        logTO.setPrimaryKey(objTO.getKeyData());
        logTO.setStatus(objTO.getCommand());
        logDAO.addToLog(logTO);
        objTransactionTO.setStatus(objTO.getCommand());
        doUpdateTransaction(map);
        objTransactionTO.setBranchId(_branchCode);
        sqlMap.executeUpdate("updateRemittanceIssueTransactionTO", objTransactionTO);
        objTO.setTransDT(currDt);
        objTO.setInitiatedBranch(_branchCode);
        sqlMap.executeUpdate("updateInvestmentTransTO", objTO);
        //        addLoanData();
    }
    /*
     * This method is used to update the already existing data by making its
     * status to be deleted
     */

    private void deleteData(HashMap map) throws Exception {
        logTO.setData(objTO.toString());
        logTO.setPrimaryKey(objTO.getKeyData());
        logTO.setStatus(objTO.getCommand());
        logDAO.addToLog(logTO);
        doUpdateTransaction(map);
        objTransactionTO = new TransactionTO();
        objTransactionTO.setBatchId(objTO.getBatchID());
        objTransactionTO.setTransId(objTO.getBatchID());
        objTransactionTO.setBranchId(_branchCode);
        System.out.println("objTransactionTO----------------->" + objTransactionTO);
        sqlMap.executeUpdate("deleteRemittanceIssueTransactionTO", objTransactionTO);
//        objTransactionTO.setStatus(objTO.getCommand());
//        objTransactionTO.setStatus("DELETED");
//        objTransactionTO.setBranchId(_branchCode);
//        sqlMap.executeUpdate("updateRemittanceIssueTransactionTO", objTransactionTO);
        objTO.setTransDT(currDt);
        objTO.setInitiatedBranch(_branchCode);
        sqlMap.executeUpdate("deleteInvestmentTransTO", objTO);

        //        addLoanData();
    }

    public static void main(String str[]) {
        try {
            InvestmentsTransDAO dao = new InvestmentsTransDAO();
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
            if (!map.containsKey("FROM_INTEREST_TASK")) {
                sqlMap.startTransaction();
            }
            System.out.println("Map in Dao ------------>" + map);
            if (map.containsKey("NARRATION_DETAILS")) {
                narration1 = map.get("NARRATION_DETAILS").toString();
            }
            if (map.containsKey("DEBIT_PARTICULARS")) {
                debitParticular = map.get("DEBIT_PARTICULARS").toString();
            }
            if (map.containsKey("CREDIT_PARTICULARS")) {
                creditParticular = map.get("CREDIT_PARTICULARS").toString();
            }
            if (map.containsKey("BACK_DATED_TRANSACTION")) {
                backDate =  (Date) map.get("TRANS_DATE");
            } 
            getTransDetails = true;
            logDAO = new LogDAO();
            logTO = new LogTO();
            logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
            logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
            logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
            logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
            logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
            if (!map.containsKey(CommonConstants.AUTHORIZEMAP)) {
                // objTO = (InvestmentsTransTO) map.get("InvestmentsTransTO");//by jiv
                final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertData(map);
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData(map);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData(map);
                }
            } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
               // System.out.println("map:" + map);
                HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
                authMap.put(CommonConstants.BRANCH_ID, (String) map.get(CommonConstants.BRANCH_ID));
                System.out.println("authMap:" + authMap);
                if (authMap != null) {
                    objTO = (InvestmentsTransTO) authMap.get("InvestmentsTransTO");
                    authorize(authMap, map);
                    if (map.containsKey("FROM_INTEREST_TASK")) {
                        returnDataMap = new HashMap();
                        returnDataMap.put("FROM_INTEREST_TASK_COMPLETED", "FROM_INTEREST_TASK_COMPLETED");
                    }
                }
                authMap = null;
            } else {
                throw new NoCommandException();
            }
            if (!map.containsKey("FROM_INTEREST_TASK")) {
                sqlMap.commitTransaction();
            }
            destroyObjects();
            return returnDataMap;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw new TransRollbackException(e);
        }
    }

    private void authorize(HashMap authMap, HashMap map) throws Exception {
        System.out.println("Do Authorization");
        System.out.println("Map in Dao ------------>" + authMap);
        boolean flag = true;
        String batchid = objTO.getBatchID();
        HashMap paramMap = new HashMap();
        paramMap.put("INITIATED_BRANCH", _branchCode);
        paramMap.put("BATCH_ID", batchid);
        paramMap.put("TRANS_DT", map.get("TRANS_DATE"));
        List list = (List) sqlMap.executeQueryForList("getSelectInvestmentTransTOwithoutShareKey", paramMap);
        for (int j = 0; j < list.size(); j++) {
            objTO = (InvestmentsTransTO) list.get(j);
            String investMentType = "";
            investMentType = CommonUtil.convertObjToStr(objTO.getInvestmentBehaves());
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
                    //Update Borrowing Balance
                    if (!CommonUtil.convertObjToStr(transactionTO.getProductType()).equals("") && transactionTO.getProductType().equals("BRW") && status.equals(CommonConstants.STATUS_AUTHORIZED)) {
                        String borrowRefNo = "";
                        double borrowAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                        borrowRefNo = CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo());
                        updateBorrowingBalance(borrowRefNo, borrowAmt, objTO.getInvestment_internal_Id(), authMap);
                    }
                    if (objTO.getTrnCode().equals("Interest") && (investMentType.equals("OTHER_BANK_RD") || investMentType.equals("OTHER_BANK_CCD")
                            || investMentType.equals("OTHER_BANK_SB") || investMentType.equals("OTHER_BANK_CA") || investMentType.equals("OTHER_BANK_SPD"))) {
                        if (flag == true) {
                            dotransferInvestment(batchid, status, authMap, map);
                            flag = false;
                        }
                    } else if (objTO.getTrnCode().equals("Renewal") && investMentType.equals("OTHER_BANK_CCD")) {
                        HashMap whereMap = new HashMap();
                        String intWithPrincipal = "";
                        String withOrWithoutInterest = "";
                        whereMap.put("INVESTMENT_ID", CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                        List renewalList = ServerUtil.executeQuery("gerInvestmentRenewalIntWithPrincipal", whereMap);
                        if (renewalList != null && renewalList.size() > 0) {
                            whereMap = (HashMap) renewalList.get(0);
                            intWithPrincipal = CommonUtil.convertObjToStr(whereMap.get("INT_WITH_PRINCIPAL"));
                            System.out.println("####intWithPrincipal : " + intWithPrincipal);
                            whereMap = new HashMap();
                            whereMap.put("INVESTMENT_ID", CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                            List depList = ServerUtil.executeQuery("gerInvestmentWithOrWithoutInterest", whereMap);
                            if (depList != null && depList.size() > 0) {
                                double availableBalance = 0.0;
                                whereMap = (HashMap) depList.get(0);
                                withOrWithoutInterest = CommonUtil.convertObjToStr(whereMap.get("WITH_INTEREST"));
                                System.out.println("####withOrWithoutInterest : " + withOrWithoutInterest);
                                whereMap.put("INVESTMENT_ID", CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                List renewalBalLst = sqlMap.executeQueryForList("getAvailableBalanceFromRenewal", whereMap);
                                if (renewalBalLst != null && renewalBalLst.size() > 0) {
                                    whereMap = (HashMap) renewalBalLst.get(0);
                                    availableBalance = CommonUtil.convertObjToDouble(whereMap.get("AVAILABLE_BALANCE")).doubleValue();
                                    System.out.println("##### Renewal AvailableBalance : " + availableBalance);
                                }
                                if (intWithPrincipal.equals("N") && withOrWithoutInterest.equals("Y")) {
                                    if (flag == true) {
                                        dotransferInvestment(batchid, status, authMap, map);
                                        flag = false;
                                    }
                                    availableBalance += CommonUtil.convertObjToDouble(objTO.getBrokenPeriodInterest()).doubleValue();
                                    whereMap.put("AVAILABLE_BALANCE", availableBalance);
                                    whereMap.put("INVESTMENT_ID", CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                    sqlMap.executeUpdate("updateInvestMasterAvailableBalance", whereMap);
                                } else {
                                    if (transactionTO.getTransType().equals("TRANSFER")) {
                                        if (flag == true) {
                                            dotransferInvestment(batchid, status, authMap, map);
                                            flag = false;
                                        }
                                    } else {
                                        if (flag == true) {
                                            doCashAuthorize(authMap, map, transactionTO, batchid);
                                            flag = false;
                                        }
                                    }
                                    whereMap.put("AVAILABLE_BALANCE", availableBalance);
                                    whereMap.put("INVESTMENT_ID", CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id()));
                                    sqlMap.executeUpdate("updateInvestMasterAvailableBalance", whereMap);
                                }
                            }
                        }
                    } else if (objTO.getTrnCode().equals("Interest") && investMentType.equals("RESERVE_FUND_DCB")) {
                        if (flag == true) {
                            dotransferInvestment(batchid, status, authMap, map);
                            flag = false;
                        }
                    } else if (transactionTO.getTransType().equals("TRANSFER")) {
                        if (flag == true) {
                            dotransferInvestment(batchid, status, authMap, map);
                            flag = false;
                        }
                    } else {
                        if (flag == true) {
                            doCashAuthorize(authMap, map, transactionTO, batchid);
                            flag = false;
                        }
                    }

                    //Closure Interest Payable
                    if (objTO.getTrnCode().equals("Closure") && (investMentType.equals("OTHER_BANK_FD") || investMentType.equals("OTHER_BANK_SSD") || investMentType.equals("OTHER_BANK_RD")
                            || investMentType.equals("OTHER_BANK_CCD")) && transactionTO.getTransType().equals("CASH")) {
                        HashMap param = new HashMap();
                        param.put("INITIATED_BRANCH", _branchCode);
                        param.put("TRANS_DT", currDt.clone());
                        param.put("BRANCH_CODE", _branchCode);
                        param.put("BATCH_ID", objTO.getInvestment_internal_Id());
                        param.put("AUTHORIZE_STATUS", "AUTHORIZE_STATUS");
                        param.put("TRANS_DT", currDt.clone());
                        List transClosureList = (List) sqlMap.executeQueryForList("getTransferDetails", param);
                        if (transClosureList != null && transClosureList.size() > 0) {
                            String oldBatchId = "";
                            String transBatchId = "";
                            HashMap transMap = new HashMap();
                            for (int b = 0; b < transClosureList.size(); b++) {
                                transMap = (HashMap) transClosureList.get(b);
                                transBatchId = CommonUtil.convertObjToStr(transMap.get("BATCH_ID"));
                                if (!oldBatchId.equals(transBatchId)) {
                                    dotransferInvestment(transBatchId, status, authMap, map);
                                }
                                oldBatchId = transBatchId;
                            }
                        }
                    }

                }
                if (!status.equals(CommonConstants.STATUS_REJECTED)) {
                    System.out.println("objTO----------------------->" + objTO);
                    dataMap.put("NO_OF_UNITS", objTO.getNoOfUnits());
                    dataMap.put("PREMIUM_PAID", objTO.getPremiumAmount());
                    dataMap.put("INTEREST_PAID", objTO.getBrokenPeriodInterest());
                    dataMap.put("INVESTMENT_TYPE", objTO.getInvestmentBehaves());
                    dataMap.put("INVESTMENT_PROD_ID", objTO.getInvestmentID());
                    dataMap.put("INVESTMENT_NAME", objTO.getInvestmentName());
                    dataMap.put("INVESTMENT_OUTSTANDING_AMOUNT", objTO.getInvestmentAmount());

                    if (objTO.getTrnCode().equals("Purchase") || objTO.getTrnCode().equals("Deposit")) {
                        //Changed By Suresh
//                    sqlMap.executeUpdate("updateInvestmentMastePurchaValues", dataMap);
                    } else if (objTO.getTrnCode().equals("Sale") || objTO.getTrnCode().equals("Interest") || objTO.getTrnCode().equals("Withdrawal")) {
//                    sqlMap.executeUpdate("updateInvestmentMasteSaleValues", dataMap);
                        if (objTO.getAuthorizeBy() != null) {
                            if (objTO.getAuthorizeBy().equals("CLOSED")) {

                                Date currentDate1 = (Date) currDt.clone();
                                Date closeDt = (Date) currentDate1.clone();
                                Date closeDt1 = objTO.getPurchaseDt();
                                if (closeDt1 != null && closeDt1.getDate() > 0) {
                                    closeDt.setDate(closeDt1.getDate());
                                    closeDt.setMonth(closeDt1.getMonth());
                                    closeDt.setYear(closeDt1.getYear());
                                }
                                dataMap.put("CLOSEDDT", closeDt);
                                dataMap.put("CLOSER_TYPE", "NORMAL");
                                dataMap.put("PRECLOSER_RATE", "0");


                                if (objTO.getTrnCode().equals("Withdrawal") && CommonUtil.convertObjToStr(objTO.getPurchaseSaleBy()).length() > 0) {
                                    dataMap.put("CLOSER_TYPE", "PRECLOSER");
                                    dataMap.put("PRECLOSER_RATE", CommonUtil.convertObjToStr(objTO.getPurchaseSaleBy()));

                                }
//                        sqlMap.executeUpdate("updateInvestmentMasterCLosedStatus", dataMap);
                            }
                        }
                    }
                    //            if(objTO.getTrnCode().equals("Interest")){
                    Date currentDate = (Date) currDt.clone();
                    Date applnDt = (Date) currentDate.clone();
                    Date applDt = objTO.getLastIntPaidDate();
                    if (applDt != null && applDt.getDate() > 0) {
                        applnDt.setDate(applDt.getDate());
                        applnDt.setMonth(applDt.getMonth());
                        applnDt.setYear(applDt.getYear());
                        dataMap.put("LAST_INT_COLLECTED_DATE", applnDt);
//                    sqlMap.executeUpdate("updateInvestmentMasterLastIntApplDt", dataMap);
                    }
                    if (objTO.getTrnCode().equals("Sale") && objTO.getAuthorizeBy().equals("CLOSED")) {
                        callexcessOrShortauthorize(map, batchid, status, authMap);
                    }
                    //                sqlMap.executeUpdate("updateInvestmentMasteSaleValues", dataMap);
                    //            }

                    //Added By Suresh
                    if (status.equals(CommonConstants.STATUS_AUTHORIZED)) {
                        if (objTO.getTrnCode().equals("Deposit") || objTO.getTrnCode().equals("Purchase")) {
                            String creditAcNo = "";
                            String debitAcNo = "";
                            HashMap whereMap = new HashMap();
                            creditAcNo = CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id());
                            debitAcNo = CommonUtil.convertObjToStr(objTO.getTxtInvestmentInternalNoTrans());
                            System.out.println("##### creditAcNo : " + creditAcNo);
                            System.out.println("##### debitAcNo : " + debitAcNo);

                            if (creditAcNo.length() > 0) {  //Update Available Balance In INVESTMENT_MASTER TABLE
                                double availableBalance = 0.0;
                                whereMap.put("INVESTMENT_ID", creditAcNo);
                                List creditLst = sqlMap.executeQueryForList("getAvailableBalanceFromMaster", whereMap);
                                if (creditLst != null && creditLst.size() > 0) {
                                    whereMap = (HashMap) creditLst.get(0);
                                    availableBalance = CommonUtil.convertObjToDouble(whereMap.get("AVAILABLE_BALANCE")).doubleValue();
                                    System.out.println("##### availableBalance : " + availableBalance);
                                    if (objTO.getTrnCode().equals("Deposit")) {
                                        availableBalance += CommonUtil.convertObjToDouble(objTO.getInvestmentAmount()).doubleValue();
                                    } else if (objTO.getTrnCode().equals("Interest")) {
                                        availableBalance += CommonUtil.convertObjToDouble(objTO.getBrokenPeriodInterest()).doubleValue();
                                    } else if (objTO.getTrnCode().equals("Purchase")) {
                                        availableBalance += CommonUtil.convertObjToDouble(objTO.getInvestmentAmount()).doubleValue() - CommonUtil.convertObjToDouble(objTO.getPurchaseRate()).doubleValue(); // Total_Share Amt-Fees Paid
                                    }
                                    whereMap.put("AVAILABLE_BALANCE", CommonUtil.convertObjToDouble(availableBalance));
                                    whereMap.put("BATCH_ID", objTO.getBatchID());
                                    if (map.containsKey("BILLS_LINK_BATCH_ID")) {
                                        whereMap.put("AMOUNT", objTO.getInvestmentAmount());
                                    }
                                    System.out.println("##### After Auth availableBalance : " + availableBalance);
                                    sqlMap.executeUpdate("updateInvestMasterAvailableBalance", whereMap);
                                    sqlMap.executeUpdate("updateInvestTrnasAvailableBalance", whereMap);
                                }
                            }

                            if (debitAcNo.length() > 0 && transType.equals("TRANSFER")) { //Update Available Balance In INVESTMENT_MASTER TABLE
                                double availableBalance = 0.0;
                                whereMap.put("INVESTMENT_ID", debitAcNo);
                                List debitLst = sqlMap.executeQueryForList("getAvailableBalanceFromMaster", whereMap);
                                if (debitLst != null && debitLst.size() > 0) {
                                    whereMap = (HashMap) debitLst.get(0);
                                    availableBalance = CommonUtil.convertObjToDouble(whereMap.get("AVAILABLE_BALANCE")).doubleValue();
                                    System.out.println("##### availableBalance : " + availableBalance);
                                    if (objTO.getTrnCode().equals("Deposit") || objTO.getTrnCode().equals("Purchase") || objTO.getTrnCode().equals("Withdrawal")) {
                                        availableBalance -= CommonUtil.convertObjToDouble(objTO.getInvestmentAmount()).doubleValue();
                                    } else if (objTO.getTrnCode().equals("Interest")) {
                                        availableBalance -= CommonUtil.convertObjToDouble(objTO.getBrokenPeriodInterest()).doubleValue();
                                    }
                                    whereMap.put("AVAILABLE_BALANCE", availableBalance);
                                    whereMap.put("BATCH_ID", objTO.getBatchID());
                                    System.out.println("##### After Auth availableBalance : " + availableBalance);
                                    sqlMap.executeUpdate("updateInvestMasterAvailableBalance", whereMap);
                                    sqlMap.executeUpdate("updateInvestTrnasOtherBankAvailableBalance", whereMap);
                                }
                                String chequeNo = "";
                                chequeNo = CommonUtil.convertObjToStr(objTO.getTxtChequeNo());
                                if (!chequeNo.equals("") && chequeNo.length() > 0) {
                                    whereMap.put("CHEQUE_NO", objTO.getTxtChequeNo());
                                    sqlMap.executeUpdate("updateCheckNoStatusUsed", whereMap);
                                }
                            }

                            //Share Details Updating
                            if (creditAcNo.length() > 0 && objTO.getTrnCode().equals("Purchase")
                                    && (investMentType.equals("SHARES_DCB") || investMentType.equals("SHARE_OTHER_INSTITUTIONS"))) {  //Update No Of Shares,Share Value And Fees Paid In INVESTMENT_SHARE TABLE
                                int noOfShare = 0;
                                double shareValue = 0.0;
                                double feesPaid = 0.0;
                                noOfShare = CommonUtil.convertObjToInt(objTO.getNoOfUnits());
                                shareValue = CommonUtil.convertObjToDouble(objTO.getInvestmentAmount()).doubleValue() - CommonUtil.convertObjToDouble(objTO.getPurchaseRate()).doubleValue(); // Share_Amt = Total_Amt-Fees Paid
                                feesPaid = CommonUtil.convertObjToDouble(objTO.getPurchaseRate()).doubleValue();
                                whereMap.put("INVESTMENT_ID", creditAcNo);
                                whereMap.put("NO_OF_SHARES", String.valueOf(noOfShare));
                                whereMap.put("SHARE_VALUE", String.valueOf(shareValue));
                                whereMap.put("FEES_PAID", String.valueOf(feesPaid));
                                System.out.println("###### NoOfShareDetailsMap: " + whereMap);
                                sqlMap.executeUpdate("updateInvestNoOfShareDetails", whereMap);
                            }
                        } else if (objTO.getTrnCode().equals("Interest")) {
                            String debitAcNo = "";
                            HashMap whereMap = new HashMap();
                            debitAcNo = CommonUtil.convertObjToStr(objTO.getTxtInvestmentInternalNoTrans());
                            System.out.println("##### debitAcNo : " + debitAcNo);
                            //OTHER_BANK_INVESTMENT ACCOUNTS
                            if (debitAcNo.length() > 0 && transType.equals("TRANSFER")) { //Update Available Balance In INVESTMENT_MASTER TABLE
                                double availableBalance = 0.0;
                                whereMap.put("INVESTMENT_ID", debitAcNo);
                                List debitLst = sqlMap.executeQueryForList("getAvailableBalanceFromMaster", whereMap);
                                if (debitLst != null && debitLst.size() > 0) {
                                    whereMap = (HashMap) debitLst.get(0);
                                    availableBalance = CommonUtil.convertObjToDouble(whereMap.get("AVAILABLE_BALANCE")).doubleValue();
                                    System.out.println("##### availableBalance : " + availableBalance);
                                    availableBalance += CommonUtil.convertObjToDouble(objTO.getBrokenPeriodInterest()).doubleValue();
                                    whereMap.put("AVAILABLE_BALANCE", availableBalance);
                                    whereMap.put("BATCH_ID", objTO.getBatchID());
                                    System.out.println("##### After Auth availableBalance : " + availableBalance);
                                    sqlMap.executeUpdate("updateInvestMasterAvailableBalance", whereMap);
                                    sqlMap.executeUpdate("updateInvestTrnasOtherBankAvailableBalance", whereMap);
                                }
                                String chequeNo = "";
                                chequeNo = CommonUtil.convertObjToStr(objTO.getTxtChequeNo());
                                if (!chequeNo.equals("") && chequeNo.length() > 0) {
                                    whereMap.put("CHEQUE_NO", objTO.getTxtChequeNo());
                                    sqlMap.executeUpdate("updateCheckNoStatusUsed", whereMap);
                                }
                            }
                        } else if ((objTO.getTrnCode().equals("Withdrawal") || objTO.getTrnCode().equals("Charges")) && (investMentType.equals("OTHER_BANK_SB")
                                || investMentType.equals("OTHER_BANK_CA") || investMentType.equals("OTHER_BANK_SPD") || investMentType.equals("RESERVE_FUND_DCB") || investMentType.equals("SHARES_DCB")
                                || investMentType.equals("SHARE_OTHER_INSTITUTIONS"))) {
                            String creditAcNo = "";
                            String debitAcNo = "";
                            HashMap whereMap = new HashMap();
                            creditAcNo = CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id());
                            debitAcNo = CommonUtil.convertObjToStr(objTO.getTxtInvestmentInternalNoTrans());
                            System.out.println("##### creditAcNo : " + creditAcNo);
                            System.out.println("##### debitAcNo : " + debitAcNo);
                            double availableBalance = 0.0;
                            if (creditAcNo.length() > 0) {  //Update Available Balance In INVESTMENT_MASTER TABLE
                                availableBalance = 0.0;
                                whereMap.put("INVESTMENT_ID", creditAcNo);
                                List creditLst = sqlMap.executeQueryForList("getAvailableBalanceFromMaster", whereMap);
                                if (creditLst != null && creditLst.size() > 0) {
                                    whereMap = (HashMap) creditLst.get(0);
                                    availableBalance = CommonUtil.convertObjToDouble(whereMap.get("AVAILABLE_BALANCE")).doubleValue();
                                    System.out.println("##### availableBalance : " + availableBalance);
                                    if (objTO.getTrnCode().equals("Withdrawal")) {
                                        availableBalance -= CommonUtil.convertObjToDouble(objTO.getInvestmentAmount()).doubleValue();
                                    } else if (objTO.getTrnCode().equals("Charges")) {
                                        availableBalance -= CommonUtil.convertObjToDouble(objTO.getBrokenPeriodInterest()).doubleValue();
                                    }
                                    whereMap.put("AVAILABLE_BALANCE", availableBalance);
                                    whereMap.put("BATCH_ID", objTO.getBatchID());
                                    if (map.containsKey("BILLS_LINK_BATCH_ID")) {
                                        whereMap.put("AMOUNT", objTO.getInvestmentAmount());
                                    }
                                    System.out.println("##### After Auth availableBalance : " + availableBalance);
                                    sqlMap.executeUpdate("updateInvestMasterAvailableBalance", whereMap);
                                    sqlMap.executeUpdate("updateInvestTrnasAvailableBalance", whereMap);
                                }
                                if (investMentType.equals("OTHER_BANK_SB") || investMentType.equals("OTHER_BANK_CA") || investMentType.equals("OTHER_BANK_SPD")) {
                                    String chequeNo = "";
                                    chequeNo = CommonUtil.convertObjToStr(objTO.getTxtChequeNo());
                                    if (!chequeNo.equals("") && chequeNo.length() > 0) {
                                        whereMap.put("CHEQUE_NO", objTO.getTxtChequeNo());
                                        sqlMap.executeUpdate("updateCheckNoStatusUsed", whereMap);
                                    }
                                }
                            }

                            if (debitAcNo.length() > 0) { //Update Available Balance In INVESTMENT_MASTER TABLE
                                availableBalance = 0.0;
                                whereMap.put("INVESTMENT_ID", debitAcNo);
                                List debitLst = sqlMap.executeQueryForList("getAvailableBalanceFromMaster", whereMap);
                                if (debitLst != null && debitLst.size() > 0) {
                                    whereMap = (HashMap) debitLst.get(0);
                                    availableBalance = CommonUtil.convertObjToDouble(whereMap.get("AVAILABLE_BALANCE")).doubleValue();
                                    System.out.println("##### availableBalance : " + availableBalance);
                                    if (objTO.getTrnCode().equals("Deposit") || objTO.getTrnCode().equals("Purchase") || objTO.getTrnCode().equals("Withdrawal")) {
                                        availableBalance += CommonUtil.convertObjToDouble(objTO.getInvestmentAmount()).doubleValue();
                                    } else if (objTO.getTrnCode().equals("Interest")) {
                                        availableBalance += CommonUtil.convertObjToDouble(objTO.getBrokenPeriodInterest()).doubleValue();
                                    }
                                    whereMap.put("AVAILABLE_BALANCE", availableBalance);
                                    whereMap.put("BATCH_ID", objTO.getBatchID());
                                    System.out.println("##### After Auth availableBalance : " + availableBalance);
                                    sqlMap.executeUpdate("updateInvestMasterAvailableBalance", whereMap);
                                    sqlMap.executeUpdate("updateInvestTrnasOtherBankAvailableBalance", whereMap);
                                }
                                if (!investMentType.equals("OTHER_BANK_SB") || !investMentType.equals("OTHER_BANK_CA") || !investMentType.equals("OTHER_BANK_SPD")) {
                                    String chequeNo = "";
                                    chequeNo = CommonUtil.convertObjToStr(objTO.getTxtChequeNo());
                                    if (!chequeNo.equals("") && chequeNo.length() > 0) {
                                        whereMap.put("CHEQUE_NO", objTO.getTxtChequeNo());
                                        sqlMap.executeUpdate("updateCheckNoStatusUsed", whereMap);
                                    }
                                }
                            }

                            //Share Details Updating
                            if (creditAcNo.length() > 0 && objTO.getTrnCode().equals("Withdrawal")
                                    && (investMentType.equals("SHARES_DCB") || investMentType.equals("SHARE_OTHER_INSTITUTIONS"))) {  //Update No Of Shares,Share Value And Fees Paid In INVESTMENT_SHARE TABLE
                                int noOfShare = 0;
                                double shareValue = 0.0;
                                double feesPaid = 0.0;
                                noOfShare = CommonUtil.convertObjToInt(objTO.getNoOfUnits());
                                shareValue = CommonUtil.convertObjToDouble(objTO.getInvestmentAmount()).doubleValue();
                                noOfShare = -1 * noOfShare;
                                shareValue = -1 * shareValue;
                                whereMap.put("INVESTMENT_ID", creditAcNo);
                                whereMap.put("NO_OF_SHARES", String.valueOf(noOfShare));
                                whereMap.put("SHARE_VALUE", String.valueOf(shareValue));
                                whereMap.put("FEES_PAID", String.valueOf(feesPaid));
                                System.out.println("###### NoOfShareDetailsMap: " + whereMap);
                                sqlMap.executeUpdate("updateInvestNoOfShareDetails", whereMap);
                            }
                        } else if (objTO.getTrnCode().equals("Closure") && (investMentType.equals("OTHER_BANK_FD") || investMentType.equals("OTHER_BANK_SSD") || investMentType.equals("OTHER_BANK_RD")
                                || investMentType.equals("OTHER_BANK_CCD"))) {
                            String creditAcNo = "";
                            String debitAcNo = "";
                            HashMap whereMap = new HashMap();
                            creditAcNo = CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id());
                            debitAcNo = CommonUtil.convertObjToStr(objTO.getTxtInvestmentInternalNoTrans());
                            System.out.println("##### creditAcNo : " + creditAcNo);
                            System.out.println("##### debitAcNo : " + debitAcNo);
                            double availableBalance = 0.0;
                            if (creditAcNo.length() > 0) {  //Update Available Balance In INVESTMENT_MASTER TABLE
                                availableBalance = 0.0;
                                whereMap.put("INVESTMENT_ID", creditAcNo);
                                List creditLst = sqlMap.executeQueryForList("getAvailableBalanceFromMaster", whereMap);
                                if (creditLst != null && creditLst.size() > 0) {
                                    whereMap = (HashMap) creditLst.get(0);
                                    availableBalance = CommonUtil.convertObjToDouble(whereMap.get("AVAILABLE_BALANCE")).doubleValue();
                                    System.out.println("##### availableBalance : " + availableBalance);
                                    availableBalance -= CommonUtil.convertObjToDouble(objTO.getInvestmentAmount()).doubleValue();
                                    whereMap.put("AVAILABLE_BALANCE", availableBalance);
                                    whereMap.put("BATCH_ID", objTO.getBatchID());
                                    System.out.println("##### After Auth availableBalance : " + availableBalance);
                                    sqlMap.executeUpdate("updateInvestMasterAvailableBalance", whereMap);
                                    sqlMap.executeUpdate("updateInvestTrnasAvailableBalance", whereMap);
                                    sqlMap.executeUpdate("updateInvestMasterStatusClosed", whereMap);
                                    sqlMap.executeUpdate("updateInvestDepStatusClosed", whereMap);
                                }
                            }

                            if (debitAcNo.length() > 0 && transType.equals("TRANSFER")) { //Update Available Balance In INVESTMENT_MASTER TABLE
                                availableBalance = 0.0;
                                whereMap.put("INVESTMENT_ID", debitAcNo);
                                List debitLst = sqlMap.executeQueryForList("getAvailableBalanceFromMaster", whereMap);
                                if (debitLst != null && debitLst.size() > 0) {
                                    whereMap = (HashMap) debitLst.get(0);
                                    availableBalance = CommonUtil.convertObjToDouble(whereMap.get("AVAILABLE_BALANCE")).doubleValue();
                                    System.out.println("##### availableBalance : " + availableBalance);
                                    System.out.println("########## objTransactionTO.getTransAmt()" + CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue());
//                                availableBalance+=CommonUtil.convertObjToDouble(objTO.getInvestmentAmount()).doubleValue();
                                    availableBalance += CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                    whereMap.put("AVAILABLE_BALANCE", availableBalance);
                                    whereMap.put("BATCH_ID", objTO.getBatchID());
                                    System.out.println("##### After Auth availableBalance : " + availableBalance);
                                    sqlMap.executeUpdate("updateInvestMasterAvailableBalance", whereMap);
                                    sqlMap.executeUpdate("updateInvestTrnasOtherBankAvailableBalance", whereMap);
                                }
                                String chequeNo = "";
                                chequeNo = CommonUtil.convertObjToStr(objTO.getTxtChequeNo());
                                if (!chequeNo.equals("") && chequeNo.length() > 0) {
                                    whereMap.put("CHEQUE_NO", objTO.getTxtChequeNo());
                                    sqlMap.executeUpdate("updateCheckNoStatusUsed", whereMap);
                                }
                            }
                        }
                        if (objTO.getTrnCode().equals("Interest") && (investMentType.equals("OTHER_BANK_FD") || investMentType.equals("OTHER_BANK_SSD") || investMentType.equals("OTHER_BANK_RD") || investMentType.equals("OTHER_BANK_CCD"))) {
                            String creditAcNo = "";
                            HashMap whereMap = new HashMap();
                            creditAcNo = CommonUtil.convertObjToStr(objTO.getInvestment_internal_Id());
                            System.out.println("##### creditAcNo : " + creditAcNo);

                            if (creditAcNo.length() > 0) {  //Update INTEREST_RECEIVED and LAST_INT_DATE In INVESTMENT_DEPOSIT TABLE
                                double interestReceived = 0.0;
                                whereMap.put("INVESTMENT_ID", creditAcNo);
                                List creditLst = sqlMap.executeQueryForList("getInterestReceivedFromDepositMaster", whereMap);
                                if (creditLst != null && creditLst.size() > 0) {
                                    whereMap = (HashMap) creditLst.get(0);
                                    currentDate = (Date) currDt.clone();
                                    applnDt = (Date) currentDate.clone();
                                    interestReceived = CommonUtil.convertObjToDouble(whereMap.get("INTEREST_RECEIVED")).doubleValue();
                                    System.out.println("##### interestReceived : " + interestReceived);
                                    interestReceived += CommonUtil.convertObjToDouble(objTO.getBrokenPeriodInterest()).doubleValue();
                                    whereMap.put("INTEREST_RECEIVED", interestReceived);
                                    //whereMap.put("INT_REC_TILL_DT", applnDt);
                                    whereMap.put("INT_REC_TILL_DT", getProperDateFormat(objTO.getLastIntPaidDate()));// Added by nithya on 04-04-2018 for 0009723: Investment trans screen - while doing interest transaction using this screen needed to update the interest upto dt properly.
                                    System.out.println("##### After Auth interestReceived : " + interestReceived);
                                    sqlMap.executeUpdate("updateInterestReceivedAndLastIntDate", whereMap);
                                }
                            }
                        }
                        if (objTO.getTrnCode().equals("Renewal") && investMentType.equals("OTHER_BANK_CCD")) {
                            HashMap whereMap = new HashMap();
                            whereMap.put("INVESTMENT_ID", objTO.getInvestment_internal_Id());
                            sqlMap.executeUpdate("updateClosedStatusInInvestRenewal", whereMap);
                        }
                        System.out.println("###### authMap : " + authMap);
                        if (objTO.getTrnCode().equals("Withdrawal") && authMap.containsKey("CLOSED_STATUS")
                                && (investMentType.equals("SHARES_DCB") || investMentType.equals("SHARE_OTHER_INSTITUTIONS"))) {
                            if (CommonUtil.convertObjToStr(authMap.get("CLOSED_STATUS")).equals("YES")) {
                                HashMap whereMap = new HashMap();
                                System.out.println("Closed Status Update");
                                whereMap.put("INVESTMENT_ID", objTO.getInvestment_internal_Id());
                                sqlMap.executeUpdate("updateInvestMasterStatusClosed", whereMap);
                                sqlMap.executeUpdate("updateInvestShareStatusClosed", whereMap);
                            }
                        }
                    }
                }
            }
            dataMap = null;
            selectedList = null;
        }
    }

    private void updateBorrowingBalance(String borrowRefNo, double borrowAmt, String investmentInternalID, HashMap authMap) throws Exception {
        String borrowType = "";
        HashMap borrowMap = new HashMap();
        borrowMap.put("BORRWING_REFNO", borrowRefNo);
        List borrowingList = sqlMap.executeQueryForList("getBorrowingList", borrowMap);
        if (borrowingList != null && borrowingList.size() > 0) {
            borrowMap = (HashMap) borrowingList.get(0);
            borrowMap.put("AMOUNT", String.valueOf(borrowAmt));
            borrowType = CommonUtil.convertObjToStr(borrowMap.get("TYPE"));
            sqlMap.executeUpdate("updateClearBalanceForInvBorrowing", borrowMap);
            BorrowingRepIntClsTO objBorrowingRepIntClsTO = new BorrowingRepIntClsTO();
            objBorrowingRepIntClsTO.setRepintclsNo(investmentInternalID);
            objBorrowingRepIntClsTO.setBorrowingrefNo(borrowRefNo);
            objBorrowingRepIntClsTO.setBorrowingNo(CommonUtil.convertObjToStr(borrowMap.get("BORROWING_NO")));
            objBorrowingRepIntClsTO.setPrincipalRepaid(CommonUtil.convertObjToDouble(String.valueOf(borrowAmt)));
            objBorrowingRepIntClsTO.setStatus(CommonConstants.STATUS_CREATED);
            objBorrowingRepIntClsTO.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
            objBorrowingRepIntClsTO.setAuthorizeBy(CommonUtil.convertObjToStr(authMap.get(CommonConstants.USER_ID)));
            objBorrowingRepIntClsTO.setAuthorizeDte(currDt);
            objBorrowingRepIntClsTO.setIntPayable(new Double(0));
            objBorrowingRepIntClsTO.setPenalPayable(new Double(0));
            objBorrowingRepIntClsTO.setChargesRepaid(new Double(0));
            objBorrowingRepIntClsTO.setPenalRepaid(new Double(0));
            objBorrowingRepIntClsTO.setIntRepaid(new Double(0));
            objBorrowingRepIntClsTO.setPrincipalBal(new Double(0));
            objBorrowingRepIntClsTO.setInterestBal(new Double(0));
            objBorrowingRepIntClsTO.setPenalBal(new Double(0));
            objBorrowingRepIntClsTO.setChargesBal(new Double(0));
            System.out.println("###### objBorrowingRepIntClsTO : " + objBorrowingRepIntClsTO);
            sqlMap.executeUpdate("insertBorrwingRepIntClsTO", objBorrowingRepIntClsTO);
        }
    }

    private void doCashAuthorize(HashMap authMap, HashMap map, TransactionTO transactionTO, String batchid) throws Exception {
        String status = CommonUtil.convertObjToStr(authMap.get("AUTHORIZESTATUS"));
        if (!authMap.containsKey("callexcessOrShortauthorize")) {
            CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
            ArrayList arrList = new ArrayList();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", status);
            singleAuthorizeMap.put("TRANS_ID", CommonUtil.convertObjToStr(transactionTO.getBatchId()));
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
        if (!authMap.containsKey("callexcessOrShortauthorize") && !authMap.containsKey("NOT_ALLOW_TRANS")) {
            transferTransParam.put("TRANS_DT", map.get("TRANS_DATE"));
            transferTransParam.put("TRANS_DATE", map.get("TRANS_DATE"));
            transferTransParam.put("INITIATED_BRANCH", _branchCode);
            System.out.println("transferTransParam>>>>>" + transferTransParam);
            transferTransList = (ArrayList) sqlMap.executeQueryForList("getBatchTxTransferTOsAuthorize", transferTransParam);
            System.out.println("@@@transferTransList" + transferTransList);
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
        } else if (authMap.containsKey("callexcessOrShortauthorize")) {
            transferTransParam.put(CommonConstants.STATUS, status);
            transferTransParam.put("BATCH_ID", batchid);
            transferTransParam.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            transferTransParam.put("TRANS_DT", map.get("TRANS_DATE"));
            transferTransParam.put("INITIATED_BRANCH", _branchCode);
            sqlMap.executeUpdate("authorizeInvestmentTrans", transferTransParam);
        }
        transferTransList = null;
        transferTransParam = null;

    }

    private void callexcessOrShortauthorize(HashMap map, String batchid, String status, HashMap authMap) throws Exception {
        TxTransferTO objTxTransferTO = null;
        HashMap txMap = new HashMap();
        HashMap achdMap = new HashMap();
        System.out.println("Map in excess or Short " + authMap);
        ArrayList transferList = new ArrayList();
        achdMap.put("INVESTMENT_TYPE", CommonUtil.convertObjToStr(objTO.getInvestmentBehaves()));
        achdMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objTO.getInvestmentID()));
        System.out.println("achdMap before query ------------>" + achdMap);
        List achdLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", achdMap);
        if (achdLst != null && achdLst.size() > 0) {
            achdMap = new HashMap();
            achdMap = (HashMap) achdLst.get(0);
            TransferTrans objTransferTrans = new TransferTrans();
            System.out.println("CommonUtil.convertObjToDouble(map.getdoubleValue();" + authMap.get("EXCESS_SHORT"));
            if (authMap.containsKey("EXCESS_SHORT") && CommonUtil.convertObjToDouble(authMap.get("EXCESS_SHORT")).doubleValue() != 0) {
                System.out.println("IN Side THE Excess OR Short");
                double excessShort = CommonUtil.convertObjToDouble(authMap.get("EXCESS_SHORT")).doubleValue();
                double excessShort1 = CommonUtil.convertObjToDouble(authMap.get("EXCESS_SHORT")).doubleValue();
                if (excessShort < 0.0) {
                    excessShort = excessShort * -1;
                    txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("PREMIUM_PAID_AC_HD"));
                    objTO.setTransType("CREDIT");
                } else {
                    txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("PREMIUM_DEPR_AC_HD"));
                    objTO.setTransType("DEBIT");
                }
                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put("AUTHORIZEREMARKS", "EXCESS_SHORT");
                txMap.put(TransferTrans.PARTICULARS, creditParticular);
                txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");

                transferList.add(objTransferTrans.getCreditTransferTO(txMap, excessShort));
                System.out.println("transferList Premium ------------->" + transferList);

                if (excessShort < 0.0) {
                    excessShort = excessShort * -1;
                    txMap.put(TransferTrans.DR_AC_HD, (String) achdMap.get("PREMIUM_DEPR_AC_HD"));
                } else {
                    txMap.put(TransferTrans.DR_AC_HD, (String) achdMap.get("PREMIUM_PAID_AC_HD"));
                }
                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put("AUTHORIZEREMARKS", "EXCESS_SHORT");
                txMap.put(TransferTrans.PARTICULARS, debitParticular);
                txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                System.out.println("txmap@@19@@>>>" + txMap);
                transferList.add(objTransferTrans.getDebitTransferTO(txMap, excessShort));
                doDebitCredit(transferList, _branchCode, true);
                objTO.setBatchID(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
                objTO.setInvestmentAmount(new Double(0.0));
                objTO.setBrokerCommession(new Double(0.0));
                objTO.setDiscountAmount(new Double(0.0));
                objTO.setDividendAmount(new Double(0.0));
                objTO.setPremiumAmount(new Double(excessShort1));
                objTO.setBrokenPeriodInterest(new Double(0.0));
                objTO.setStatus(CommonConstants.STATUS_CREATED);
                objTO.setInitiatedBranch(_branchCode);
                sqlMap.executeUpdate("insertInvestmentTransTO", objTO);
                authMap.put("callexcessOrShortauthorize", "callexcessOrShortauthorize");
                String batchid1 = CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID"));
                System.out.println("batchid1---------------->" + batchid1);
                dotransferInvestment(batchid1, status, authMap, map);
                HashMap dataMap = new HashMap();
                dataMap.put("NO_OF_UNITS", objTO.getNoOfUnits());
                dataMap.put("PREMIUM_PAID", objTO.getPremiumAmount());
                dataMap.put("INTEREST_PAID", objTO.getBrokenPeriodInterest());
                dataMap.put("INVESTMENT_TYPE", objTO.getInvestmentBehaves());
                dataMap.put("INVESTMENT_PROD_ID", objTO.getInvestmentID());
                dataMap.put("INVESTMENT_NAME", objTO.getInvestmentName());
                dataMap.put("INVESTMENT_OUTSTANDING_AMOUNT", objTO.getInvestmentAmount());
                System.out.println("dataMap---------------->" + dataMap);
                if (excessShort1 < 0.0) {
//                    sqlMap.executeUpdate("updateInvestmentMastePurchaValues", dataMap);
                } else if (excessShort1 > 0.0) {
//                    sqlMap.executeUpdate("updateInvestmentMasteSaleValues", dataMap);
                }
                dataMap = null;
                txMap = null;
                transferList = null;
                achdMap = null;
                achdLst = null;
                objTransferTrans = null;
                System.out.println("Completed");
            }
        }

    }
    /*
     * This method is used to execute a query to get all the inserted datas in
     * the table and retrun the resultset as a HashMap *
     */

    public HashMap executeQuery(HashMap obj) throws Exception {
        System.out.println("obj------------------->" + obj);
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        where = (String) obj.get(CommonConstants.MAP_WHERE);
        HashMap data = new HashMap();
        if (!obj.containsKey("TRANSACTION") && !obj.containsKey("UNAUTHORIZEDEXISTS")) {
//        HashMap investmentMasterMap =  getInvestmentMasterData();
//        data.put("InvestmentsMasterTO", investmentMasterMap);
        }
        if (obj.containsKey("TRANSACTION") && !obj.containsKey("UNAUTHORIZEDEXISTS")) {
            Date fromDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj.get("FROM_DATE")));
            Date toDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj.get("TO_DATE")));
            Date curDt = (Date) currDt.clone();
            Date prFromDt = (Date) curDt.clone();
            if (fromDate.getDate() > 0) {
                prFromDt.setDate(fromDate.getDate());
                prFromDt.setMonth(fromDate.getMonth());
                prFromDt.setYear(fromDate.getYear());
            }
            Date prToDt = (Date) curDt.clone();
            if (toDate.getDate() > 0) {
                prToDt.setDate(toDate.getDate());
                prToDt.setMonth(toDate.getMonth());
                prToDt.setYear(toDate.getYear());
            }
            data.put("FROM_DATE", prFromDt);
            data.put("TO_DATE", prToDt);
            System.out.println("data-------------------->" + data);

            List transList = (List) sqlMap.executeQueryForList("getSelectInvestmentTransDetailsTO", obj);
            data.put("getSelectInvestmentTransDetailsTO", transList);
            HashMap investmentMasterMap = getInvestmentMasterData();
            data.put("InvestmentsMasterTO", investmentMasterMap);
        }
        if (!obj.containsKey("MASTER") && !obj.containsKey("TRANSACTION") && !obj.containsKey("UNAUTHORIZEDEXISTS")) {
            where = (String) obj.get("BATCH_ID");
            HashMap investmenttransMap = getInvestmentTransData(obj);
            data.put("InvestmentsTransTO", investmenttransMap);
            obj.put(CommonConstants.MAP_WHERE, where);
            HashMap getRemitTransMap = new HashMap();
            getRemitTransMap.put("TRANS_ID", obj.get("BATCH_ID"));
            getRemitTransMap.put("TRANS_DT", obj.get("TRANS_DT"));
            getRemitTransMap.put("BRANCH_CODE", _branchCode);
            System.out.println("@#%$#@%#$%getRemitTransMap:" + getRemitTransMap);
            List list = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", getRemitTransMap);
            data.put("TransactionTO", list);
            list = null;
            obj.put("BATCHID", obj.get("BATCH_ID"));
            obj.put("TRANS_DT", currDt.clone());
            obj.put("INITIATED_BRANCH", _branchCode);
            list = (List) sqlMap.executeQueryForList("getBatchTxTransferTOsAuthorize", obj);
            data.put("transferTrans", list);
            list = null;
        }
        if (obj.containsKey("UNAUTHORIZEDEXISTS")) {
            List list = (List) sqlMap.executeQueryForList("getSelectInvestmentTransDetailsUnauthorizedTO", where);
            if (list.size() > 0) {
                HashMap exMap = new HashMap();
                exMap = (HashMap) list.get(0);
                data.put("EXISTS", list);
            }


        }

        return data;
    }

    /*
     * This is used to free up the memory used by SharePrductTO object
     */
    private void destroyObjects() {
        objTO = null;
        loanDataMap = null;
        deletedLoanDataMap = null;
        backDate = null;
        cashId = "";
        transferId = "";
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
    
        private String getDateFor(Date dd) {
        if (dd != null) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            System.out.println("New Date--->" + format.format(dd));
            return CommonUtil.convertObjToStr(format.format(dd));
        } else {
            return "";
        }
    }
    
}
