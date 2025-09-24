/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BorrwingDAO.java
 *
 * Created on Thu Jan 20 17:19:05 IST 2005
 */
package com.see.truetransact.serverside.borrowings.master;

import java.util.List;
import java.util.ArrayList;
//cheque details
import java.util.LinkedHashMap;
import com.see.truetransact.transferobject.borrowings.master.BorrowingsChequeTO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.investments.InvestmentsTransDAO;
import com.see.truetransact.transferobject.investments.InvestmentsTransTO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.borrowings.disbursal.BorrowingDisbursalTO; //trans details
import com.see.truetransact.serverside.borrowings.disbursal.BorrwingDisbursalDAO; //trans details
//end...
//trans details
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.cash.CashTransaction;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.borrowings.disbursal.BorrowingDisbursalTO;
//end...
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO; //insert into disbursal
//import com.see.truetransact.serverside.common.log.LogDAO;
//import com.see.truetransact.transferobject.common.log.LogTO;

import com.see.truetransact.transferobject.borrowings.master.BorrowingsTO;
import java.util.HashMap;
import java.util.Date;

/**
 * TokenConfig DAO.
 *
 */
public class BorrwingDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private BorrowingsTO objTO;
    BorrowingDisbursalTO objDisbursalTO = new BorrowingDisbursalTO(); //insert into disbursal
    //cheque details
    private LinkedHashMap deletedTableValues = null;
    private LinkedHashMap tableDetails = null;
    private String borrowingNo = "";
    private LogDAO logDAO;
    private LogTO logTO;
    private Date currDt = null;//trans details
    private TransactionDAO transactionDAO = null;//trans details
    HashMap returnMap; //trans details
    private String authBy = "";
    private InvestmentsTransTO objInv = null;
    private String invProdType = "";
    private String invAccNo = "";
    private Double dividendAmount = 0.0;
    //  private LogDAO logDAO;
    // private LogTO logTO;

    /**
     * Creates a new instance of TokenConfigDAO
     */
    public BorrwingDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap where = (HashMap) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("Borrowings.getSelectBorrowings", where);
        returnMap.put("BorrowingTO", list);

        //trans details
        if (where.containsKey("BORROWING_NO")) {
            HashMap getRemitTransMap = new HashMap();
            getRemitTransMap.put("TRANS_ID", where.get("BORROWING_NO"));
            getRemitTransMap.put("TRANS_DT", currDt.clone());
            getRemitTransMap.put("BRANCH_CODE", _branchCode);
            System.out.println("@#%$#@%#$%getRemitTransMap:" + getRemitTransMap);
            list = sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", getRemitTransMap);
//            list = sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", where.get("REP_INT_CLS_NO"));
            if (list != null && list.size() > 0) {
                returnMap.put("TRANSACTION_LIST", list);
            }
        }

        //chequedetails
        List chequeList = (List) sqlMap.executeQueryForList("getSelectBorrowingsChequeTO", map); // CHEQUE_DETAILS
        if (chequeList != null && chequeList.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            for (int i = chequeList.size(), j = 0; i > 0; i--, j++) {
                String st = ((BorrowingsChequeTO) chequeList.get(j)).getSlNo();
                ParMap.put(((BorrowingsChequeTO) chequeList.get(j)).getSlNo(), chequeList.get(j));
            }
            System.out.println("@@@ParMap" + ParMap);
            returnMap.put("chequeListTO", ParMap);
        }
        //end...


        return returnMap;
    }

    private String getBorrowingNo() throws Exception {
        //  final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "BORROWING_NO");
        HashMap map = generateID();
        return (String) map.get(CommonConstants.DATA);
    }

    public HashMap generateID() {
        HashMap hash = null, result = null;
        try {
            String mapName = "getCurrentID";
            HashMap where = new HashMap();
            where.put("ID_KEY", "BORROWING_ID"); //Here u have to pass BORROW_ID or something else
            List list = null;
            sqlMap.executeUpdate("updateIDGenerated", where);  // This update statement just updates curr_value=curr_value+1
            list = (List) sqlMap.executeQueryForList(mapName, where);  // This will get u the updated curr_value, prefix and length
            //sqlMap.commitTransaction();

            if (list.size() > 0) {
                hash = (HashMap) list.get(0);
                String strPrefix = "", strLen = "";

                // Prefix for the ID.
                if (hash.containsKey("PREFIX")) {
                    strPrefix = (String) hash.get("PREFIX");
                    if (strPrefix == null || strPrefix.trim().length() == 0) {
                        strPrefix = "";
                    }
                }

                // Maximum Length for the ID
                int len = 10;
                if (hash.containsKey("ID_LENGTH")) {
                    strLen = String.valueOf(hash.get("ID_LENGTH"));
                    if (strLen == null || strLen.trim().length() == 0) {
                        len = 10;
                    } else {
                        len = Integer.parseInt(strLen.trim());
                    }
                }

                int numFrom = strPrefix.trim().length();

                String newID = String.valueOf(hash.get("CURR_VALUE"));

                // Number Part of the String and incrementing 1 (ex. only 00085 from OGGOT00085)
                result = new HashMap();
                String genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
                result.put(CommonConstants.DATA, genID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //cheque details
    private void insertChequeDetails() throws Exception {
        if (tableDetails != null) {
            ArrayList addList = new ArrayList(tableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                BorrowingsChequeTO objBorrowingsTO = (BorrowingsChequeTO) tableDetails.get(addList.get(i));
                objBorrowingsTO.setBorrowingNo(borrowingNo);
                sqlMap.executeUpdate("insertBorrowingsChequeTO", objBorrowingsTO);
                int fromNo = 0;
                int toNo = 0;
                fromNo = CommonUtil.convertObjToInt(objBorrowingsTO.getFromNo());
                toNo = CommonUtil.convertObjToInt(objBorrowingsTO.getToNo());
                /* for(int j=fromNo; j<=toNo;j++){
                 objBorrowingsTO.setFromNo(String.valueOf(j));
                 objBorrowingsTO.setStatus("UN_USED");
                 sqlMap.executeUpdate("insertBorrowingsChequeDetails", objBorrowingsTO);
                 }
                 */
                // logTO.setData(objBorrowingsTO.toString());
                //  logTO.setPrimaryKey(objBorrowingsTO.getKeyData());
                //  logDAO.addToLog(logTO);
            }
        }
    }

    private void updateChequeDetails() throws Exception {
        if (tableDetails != null || deletedTableValues != null) {
            ArrayList addList = new ArrayList(tableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                BorrowingsChequeTO objBorrowingsTO = (BorrowingsChequeTO) tableDetails.get(addList.get(i));

                if (objBorrowingsTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                    sqlMap.executeUpdate("insertBorrowingsChequeTO", objBorrowingsTO);
                    System.out.println("chequeinsert>>>>>>>>>>>>>");
                } else {
                    sqlMap.executeUpdate("updateBorrowingsChequeTO", objBorrowingsTO);
                }
                if (i == 0) {
                    sqlMap.executeUpdate("deleteBorrowingChequeMasterStatus", objBorrowingsTO);
                }

                int fromNo = 0;
                int toNo = 0;
                fromNo = CommonUtil.convertObjToInt(objBorrowingsTO.getFromNo());
                toNo = CommonUtil.convertObjToInt(objBorrowingsTO.getToNo());
                for (int j = fromNo; j <= toNo; j++) {
                    objBorrowingsTO.setFromNo(String.valueOf(j));
                    objBorrowingsTO.setStatus("UN_USED");
                    sqlMap.executeUpdate("insertBorrowingsChequeDetails", objBorrowingsTO);
                }

                //  logTO.setData(objBorrowingsTO.toString());
                //   logTO.setPrimaryKey(objBorrowingsTO.getKeyData());
                //  logDAO.addToLog(logTO);
            }
        }
        if (deletedTableValues != null) {
            System.out.println("######## deletedTableValues :" + deletedTableValues);
            ArrayList addList = new ArrayList(deletedTableValues.keySet());
            BorrowingsTO objBorrowingsTO = null;
            for (int i = 0; i < deletedTableValues.size(); i++) {
                objBorrowingsTO = new BorrowingsTO();
                objBorrowingsTO = (BorrowingsTO) deletedTableValues.get(addList.get(i));
                sqlMap.executeUpdate("deleteBorrowingCheckMasterTO", objBorrowingsTO);
            }
        }
    }

    //end....
    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setBorrowingNo(getBorrowingNo());
            borrowingNo = objTO.getBorrowingNo();
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            insertDisbursal(map); //insert to disbursal
            if (objTO.getGovtLoan() != null && objTO.getGovtLoan().equals("N")) {
                doBorrowingTransactions(map); //trans details
            }
            sqlMap.executeUpdate("insertBorrowingTO", objTO);
            if (objTO.getGovtLoan() != null && objTO.getGovtLoan().equals("N")) {
                sqlMap.executeUpdate("insertBorrwingDisbursalTO", objDisbursalTO); //insert to disbursal
            }
            if (map.containsKey("chequeBookDetails")) {
                insertChequeDetails();
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);

        }
    }

    //insert into disbursal
    private String getDisbursalNo(HashMap map) throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "BORROWING_DISBURSAL_ID");
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//reseting branch code
        System.out.println("branch code>>>>>>>>>>" + _branchCode);
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void insertDisbursal(HashMap map) {

        try {
            objDisbursalTO.setDisbursalNo(getDisbursalNo(map));
            System.out.println("didbursal no is....." + objDisbursalTO.getDisbursalNo());
            objDisbursalTO.setBorrowingrefNo(objTO.getBorrowingrefNo());
            objDisbursalTO.setBorrowingNo(objTO.getBorrowingNo());
            objDisbursalTO.setAmtBorrowed(objTO.getAmount());
            objDisbursalTO.setAuthorizeStatus(objTO.getAuthorizeStatus());
            objDisbursalTO.setAuthorizeBy(objTO.getAuthorizeBy());
            objDisbursalTO.setAuthorizeDte(objTO.getAuthorizeDte());
            objDisbursalTO.setStatus(objTO.getStatus());
            System.out.println("objDisbursalTO>>>>@@111>>>>" + objDisbursalTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //end...
    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            sqlMap.executeUpdate("updateBorrowingTO", objTO);
            if (map.containsKey("chequeBookDetails") || map.containsKey("deletedChequeBookDetails")) {
                updateChequeDetails();
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    //trans details
    private void doBorrowingTransactions(HashMap map) throws Exception, Exception {
        try {
            String generateSingleTransId = generateLinkID();
            if (objTO.getCommand() != null) {
                if (objTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                    //  double amtBorrowed = CommonUtil.convertObjToDouble(objTO.getAmtBorrowed()).doubleValue() ;
                    //  System.out.println("@#$ amtBorrowed :"+amtBorrowed);
                    HashMap txMap;
                    HashMap whereMap = new HashMap();
                    whereMap.put("BORROWING_NO", objTO.getBorrowingNo());
                    //  HashMap acHeads = (HashMap)sqlMap.executeQueryForObject("Borrowings.getAcHeads", whereMap);
                    String ac_head = objTO.getPrinGrpHead();
                    if (ac_head == null || ac_head.equals("")) {
                        throw new TTException("Account heads not set properly...");
                    }
                    TransferTrans objTransferTrans = new TransferTrans();

                    objTransferTrans.setInitiatedBranch(_branchCode);

                    objTransferTrans.setLinkBatchId(objTO.getBorrowingNo());
                    txMap = new HashMap();
                    ArrayList transferList = new ArrayList();

                    LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    //                            System.out.println("TransactionDetailsMap---->"+TransactionDetailsMap);
                    if (TransactionDetailsMap.size() > 0) {
                        if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                            LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                            TransactionTO objTransactionTO = null;
                            for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                //                                   System.out.println("objTransactionTO---->"+objTransactionTO);
                                double debitAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                    //                                            txMap.put(TransferTrans.DR_AC_HD, (String)acHeads.get("SHARE_ACHD"));
                                    txMap.put(TransferTrans.PARTICULARS, "To " + objTO.getBorrowingNo() + " Disbursement");
                                    txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("DR_INST_TYPE", "VOUCHER");
                                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    if (objTransactionTO.getProductType().equals("GL")) {
                                        txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));

                                    } else {
                                        txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                    }
                                    txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                    //Added By Suresh
                                    System.out.println("");
                                    if (!objTransactionTO.getProductType().equals("") && objTransactionTO.getProductType().equals("INV")) {
                                        invProdType = objTransactionTO.getProductType();
                                        HashMap achdMap = new HashMap();
                                        achdMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                        List achdLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", achdMap);
                                        if (achdLst != null && achdLst.size() > 0) {
                                            achdMap = new HashMap();
                                            achdMap = (HashMap) achdLst.get(0);
                                            txMap.put(TransferTrans.DR_AC_HD, (String) achdMap.get("IINVESTMENT_AC_HD"));
                                            txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_WITHDRAWAL");
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            dividendAmount = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                            //insert Investment Details in INVESTMENT_TRANS_DETAILS Table
//                                            double dividendAmount=0.0;
//                                            whereMap=new HashMap();
//                                            dividendAmount = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                             invAccNo = CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo());
//                                            whereMap.put("INVESTMENT_ACC_NO",CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
//                                            System.out.println("objTransactionTO.getBatchId()123--------------->"+objTransactionTO.getBatchId());
//                                            System.out.println("objTO.getBorrowingNo()--------------->"+objTO.getBorrowingNo());
                                            //  whereMap.put("BATCH_ID",objTO.getBorrowingNo());
                                            // InvestmentsTransTO objInv= new InvestmentsTransTO();
                                            // objInv = getInvestmentsTransTO(CommonConstants.TOSTATUS_INSERT, dividendAmount, whereMap, map);
                                            //sqlMap.executeUpdate("insertInvestmentTransTO", objTO);
                                        } else {
                                            throw new TTException("Account heads not set properly...");
                                        }
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
                                    txMap.put("generateSingleTransId", generateSingleTransId);
                                    txMap.put("SCREEN_NAME", CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, debitAmt));
                                    //                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, shareAmt));
                                    //                                            objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                    if (debitAmt > 0.0) {
                                        txMap.put(TransferTrans.CR_AC_HD, ac_head);
                                        txMap.put("AUTHORIZEREMARKS", "PRINCIPAL_GRP_HEAD");
                                        //                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, shareAmt));
                                        txMap.put("TRANS_MOD_TYPE", "BR");
                                        txMap.put("generateSingleTransId", generateSingleTransId);
                                        txMap.put("SCREEN_NAME", CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, debitAmt));
                                        //                                                objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                    }
                                    //   System.out.println("amtjkdjcdjh1233343>>>>transferList>>>>>>"+transferList);
                                    objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                    //vivek
                                    if ((invProdType.equals("INV"))) {
                                        HashMap batchListMap = new HashMap();
                                        HashMap batchListMap1 = new HashMap();
                                        // double dividendAmount=0.0;
                                        String batchIdval = "";
                                        whereMap = new HashMap();
                                        // dividendAmount = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                        whereMap.put("INVESTMENT_ACC_NO", invAccNo);
                                        // System.out.println("objTransactionTO.getBatchId()123--------------->"+objTransactionTO.getBatchId());
                                        batchListMap.put("BORROW", objTO.getBorrowingNo());
                                        List batchIdList = sqlMap.executeQueryForList("getBatchIdInvForBorrow", batchListMap);
                                        for (int i = 0; i < batchIdList.size(); i++) {
                                            batchListMap1 = (HashMap) batchIdList.get(0);
                                        }
                                        batchIdval = batchListMap1.get("BATCH_ID").toString();
                                        whereMap.put("BATCH_ID", batchIdval);
                                        InvestmentsTransTO objInv = new InvestmentsTransTO();
                                        objInv = getInvestmentsTransTO(CommonConstants.TOSTATUS_INSERT, dividendAmount, whereMap, map);
                                        sqlMap.executeUpdate("insertInvestmentTransTO", objInv);
                                    }
                                    //end

                                } else {
                                    double transAmt;
                                    //  TransactionTO transTO = new TransactionTO();
                                    CashTransactionTO objCashTO = new CashTransactionTO();
                                    ArrayList cashList = new ArrayList();
                                    if (CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue() > 0) {
                                        objCashTO.setTransId("");
                                        objCashTO.setProdType(TransactionFactory.GL);
                                        objCashTO.setTransType(CommonConstants.CREDIT);
                                        objCashTO.setInitTransId(logTO.getUserId());
                                        objCashTO.setBranchId(_branchCode);
                                        objCashTO.setStatusBy(logTO.getUserId());
                                        objCashTO.setStatusDt(currDt);
//                                        objCashTO.setTokenNo(CommonUtil.convertObjToStr(paramMap.get("TOKEN_NO")));
                                        objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                        objCashTO.setParticulars("By " + objTO.getBorrowingNo() + " Disbursement");
                                        objCashTO.setInitiatedBranch(_branchCode);
                                        objCashTO.setInitChannType(CommonConstants.CASHIER);
                                        objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                        objCashTO.setAcHdId(ac_head);
                                        objCashTO.setInpAmount(objTransactionTO.getTransAmt());
                                        objCashTO.setAmount(objTransactionTO.getTransAmt());
                                        objCashTO.setLinkBatchId(objTO.getBorrowingNo());
                                        objCashTO.setTransModType("BR");
                                        objCashTO.setSingleTransId(generateSingleTransId);
                                        objCashTO.setScreenName(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                        cashList.add(objCashTO);
                                        HashMap tranMap = new HashMap();
                                        tranMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                                        tranMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        tranMap.put(CommonConstants.IP_ADDR, CommonUtil.convertObjToStr(map.get("IP_ADDR")));
                                        tranMap.put(CommonConstants.MODULE, CommonUtil.convertObjToStr(map.get("MODULE")));
                                        tranMap.put(CommonConstants.SCREEN, CommonUtil.convertObjToStr(map.get("SCREEN")));
                                        tranMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                                        tranMap.put("DAILYDEPOSITTRANSTO", cashList);
                                        CashTransactionDAO cashDao;
                                        cashDao = new CashTransactionDAO();
                                        tranMap = cashDao.execute(tranMap, false);
                                        cashDao = null;
                                        tranMap = null;
                                    }
                                }
                                /* else
                                 {
                                    
                                 //                                            txMap.put(TransferTrans.DR_AC_HD, (String)acHeads.get("SHARE_ACHD"));
                                 txMap.put(CashTransaction.PARTICULARS, "To "+objTO.getBorrowingNo()+" Disbursement");
                                 txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                 txMap.put("DR_INSTRUMENT_2","DEPOSIT_TRANS");
                                 txMap.put("DR_INST_TYPE","WITHDRAW_SLIP");
                                 txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                 txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                 txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                 if (objTransactionTO.getProductType().equals("GL")) {
                                 txMap.put(TransferTrans.DR_AC_HD,CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));

                                 } else {
                                 txMap.put(TransferTrans.DR_ACT_NUM,CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                 txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                 }
                                 txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                 transferList.add(objTransferTrans.getDebitTransferTO(txMap, debitAmt));
                                 //                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, shareAmt));
                                 //                                            objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                 if(amtBorrowed>0.0){
                                 txMap.put(TransferTrans.CR_AC_HD, (String)acHeads.get("PRINCIPAL_GRP_HEAD"));
                                 txMap.put("AUTHORIZEREMARKS","PRINCIPAL_GRP_HEAD");
                                 //                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, shareAmt));
                                 transferList.add(objTransferTrans.getCreditTransferTO(txMap, amtBorrowed));
                                 //                                                objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                 }
                                 objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                 }*/
                                //End cash
                                objTransactionTO.setStatus(CommonConstants.STATUS_CREATED);
                                objTransactionTO.setBatchId(objTO.getBorrowingNo());
                                objTransactionTO.setBatchDt(currDt);
                                objTransactionTO.setTransId(objTransferTrans.getLinkBatchId());
                                objTransactionTO.setBranchId(_branchCode);
                                sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);
                            }
                        }
                    }
                    //                 amtBorrowed=0.0;
                    objTransferTrans = null;
                    transferList = null;
                    txMap = null;
                    // Code End
                    getTransDetails(objTO.getBorrowingNo());
                } else {
                    HashMap shareAcctNoMap = new HashMap();
                    //                    shareAcctNoMap = (HashMap)sqlMap.executeQueryForObject("transferResolvedShare", shareAcctNoMap);
                    //                    sqlMap.executeUpdate("updateShareAcctDetailsTO", shareAcctDetailsTO);
                    double amtBorrowed = CommonUtil.convertObjToDouble(objTO.getAmtBorrowed()).doubleValue();
                    if (objTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                    }
                }
            }
        } catch (Exception e) {
            //                sqlMap.rollbackTransaction();
            e.printStackTrace();
            //                throw new TransRollbackException(e);
            throw e;
        }
    }

    //Added By Suresh
    public InvestmentsTransTO getInvestmentsTransTO(String cmd, double intTrfAm, HashMap acctDtlMap, HashMap map) {
        HashMap whereMap = new HashMap();
        InvestmentsTransTO objgetInvestmentsTransTO = new InvestmentsTransTO();
        acctDtlMap.put("INT_PAY_ACC_NO", CommonUtil.convertObjToStr(acctDtlMap.get("INVESTMENT_ACC_NO")));
        List invList = ServerUtil.executeQuery("getInvestmentDetails", acctDtlMap);
        if (invList != null && invList.size() > 0) {
            whereMap = (HashMap) invList.get(0);
            objgetInvestmentsTransTO.setCommand(cmd);
            objgetInvestmentsTransTO.setStatus(CommonConstants.STATUS_CREATED);
            objgetInvestmentsTransTO.setInvestmentBehaves(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_TYPE")));
            objgetInvestmentsTransTO.setInvestmentID(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_PROD_ID")));
            objgetInvestmentsTransTO.setInvestment_internal_Id(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_ID")));
            objgetInvestmentsTransTO.setInvestment_Ref_No(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_REF_NO")));
            objgetInvestmentsTransTO.setInvestmentName(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_PROD_DESC")));
            objgetInvestmentsTransTO.setTransDT(currDt);
            objgetInvestmentsTransTO.setTransType("DEBIT");
            objgetInvestmentsTransTO.setTrnCode("Deposit");
            objgetInvestmentsTransTO.setAmount(new Double(0.0));
            objgetInvestmentsTransTO.setPurchaseDt(currDt);
            objgetInvestmentsTransTO.setInvestmentAmount(new Double(intTrfAm));
            objgetInvestmentsTransTO.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            objgetInvestmentsTransTO.setStatusDt(currDt);
            objgetInvestmentsTransTO.setDividendAmount(new Double(0));
            objgetInvestmentsTransTO.setLastIntPaidDate(currDt);
            objgetInvestmentsTransTO.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
            objgetInvestmentsTransTO.setPurchaseMode("SHARE_PAYMENT");
            System.out.println("objTO.getAuthorizeBy(>>>>>>9999" + objTO.getAuthorizeBy());
            System.out.println("objTO.getAuthorizeStatus()>>>>>>9999" + objTO.getAuthorizeStatus());

            System.out.println("acctDtlMap111@@@sdfsd>>>>>>9999" + acctDtlMap);
            System.out.println("acctDtlMap.get(BATCH_ID)>>>>>>99999" + acctDtlMap.get("BATCH_ID"));
            System.out.println("cmd>>>>>>99999" + cmd);
            System.out.println("CommonConstants.STATUS_AUTHORIZED>>>>>>99999" + CommonConstants.STATUS_AUTHORIZED);
            System.out.println("CommonConstants.AUTHORIZEUSERLIST>>>>>>99999" + CommonConstants.AUTHORIZEUSERLIST + "jkhbjkdgf" + CommonConstants.AUTHORIZEDATA);
            if (objTO.getCommand().equals(CommonConstants.STATUS_AUTHORIZED)) {
                System.out.println("CommonConstants.USER_ID(>>>>>>4444" + CommonConstants.USER_ID);
                System.out.println("currDt>>>>>4444" + currDt);
                System.out.println("objTO.getAuthorizeBy(>>>>>>4444" + CommonConstants.USER_ID);
                objgetInvestmentsTransTO.setAuthorizeBy(authBy);
                // objgetInvestmentsTransTO.setInvAvailableBal(objTO.getAvailBalance());
                objgetInvestmentsTransTO.setAuthorizeDt(currDt);
                objgetInvestmentsTransTO.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
            }
            System.out.println("objTO.getAvailBalance()>>>>>>9999" + objTO.getAvailBalance());
            objgetInvestmentsTransTO.setInvAvailableBal(objTO.getAvailBalance());
            System.out.println("objgetInvestmentsTransTO.getInvAvailableBal()>>>hhfh@@" + objgetInvestmentsTransTO.getInvAvailableBal());
            System.out.println("objgetInvestmentsTransTO1111@@@sdfsd>>>>>>" + objgetInvestmentsTransTO);
            System.out.println("acctDtlMap111@@@sdfsd>>>>>>" + acctDtlMap);
            if (acctDtlMap.containsKey("BATCH_ID")) {
                System.out.println("acctDtlMap.get(BATCH_ID)>>>>>>" + acctDtlMap.get("BATCH_ID"));
                objgetInvestmentsTransTO.setBatchID(CommonUtil.convertObjToStr(acctDtlMap.get("BATCH_ID")));
            }
            //  if(cmd.containsKey("AUTHORIZESTATUS")){

            //   }
        }
        System.out.println("objgetInvestmentsTransTO2222@@@sdfsd>>>>>>" + objgetInvestmentsTransTO);
        return objgetInvestmentsTransTO;
    }

    private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
        returnMap = new HashMap();
        List transList = (List) sqlMap.executeQueryForList("getTransferDetails", getTransMap);
        if (transList != null && transList.size() > 0) {
            returnMap.put("TRANSFER_TRANS_LIST", transList);
            System.out.println("########transfrretrnmap>>>>>>>>>>>///" + returnMap);
            System.out.println("########transfrlist>>>>>>>>>>>///" + transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetails", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            returnMap.put("CASH_TRANS_LIST", cashList);
            System.out.println("########cashretrnmap>>>>>>>>>>>///" + returnMap);
            System.out.println("########cashlist>>>>>>>>>>>///" + cashList);

        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    //end...
    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_DELETED);
            sqlMap.executeUpdate("deleteBorrowingTO", objTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            BorrwingDAO dao = new BorrwingDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        returnMap = new HashMap();//trans details
        objTO = (BorrowingsTO) map.get("BorrowingsTO");
        System.out.println("objTO1111111111@@@@>>>>>>>>" + map);
        if (objTO.getAmount() != null) {
            objTO.setAvailBalance(objTO.getSanctionAmt() - objTO.getAmount());
        } else {
            objTO.setAvailBalance(objTO.getSanctionAmt() - objTO.getAmtBorrowed());//objTO.getAmount();
        }
        final String command = objTO.getCommand();
        //  HashMap returnMap = null;
        //cheque details
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

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
            // returnMap = new HashMap();
            returnMap.put("BORROWING_NO", objTO.getBorrowingNo());
          //  System.out.println("returnMap1111111111@@@@>>>>>>>>" + returnMap);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap, map);
            }
        } else {
            throw new NoCommandException();
        }

        destroyObjects();
        System.out.println("returnMapenddddd>>>>>>>>" + returnMap);
        return returnMap;
    }

    private void authorize(HashMap map, HashMap borrowMap) throws Exception {

        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        System.out.println("@@@@@@@selectedList" + selectedList);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        String status = (String) AuthMap.get("STATUS");
        String linkBatchId = null;
        String appNo = null;
        HashMap cashAuthMap;
        TransactionTO objTransactionTO = null;
        if (objTO.getGovtLoan() != null && objTO.getGovtLoan().equals("N")) {
            try {
                //  sqlMap.startTransaction();
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                appNo = CommonUtil.convertObjToStr(AuthMap.get("BORROWING_NO"));
                //   map.put(CommonConstants.STATUS, status);
                //  map.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                //  map.put("CURR_DATE", currDt);
                // System.out.println("status--56---------->"+CommonConstants.USER_ID);
                //  sqlMap.executeUpdate("authorizeBorrowingRepIntCls", map);
                linkBatchId = CommonUtil.convertObjToStr(AuthMap.get("BORROWING_NO"));//Transaction Batch Id

                //Separation of Authorization for Cash and Transfer
                //Added By Suresh
                if (borrowMap.containsKey("TransactionTO")) {
                    HashMap transactionDetailsMap = (LinkedHashMap) borrowMap.get("TransactionTO");
                    TransactionTO transactionTO = new TransactionTO();
                    LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                    if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                        allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                    }
                    transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                    if (!CommonUtil.convertObjToStr(transactionTO.getProductType()).equals("") && transactionTO.getProductType().equals("INV")) {
                        HashMap whereMap = new HashMap();
                        String investmentBatchId = "";
                        whereMap.put("BATCH_ID", CommonUtil.convertObjToStr(transactionTO.getBatchId()));
                        whereMap.put("TRANS_DT", currDt);
                        whereMap.put(CommonConstants.BRANCH_ID, _branchCode);
                        List transList = (List) sqlMap.executeQueryForList("getTransferDetails", whereMap);
                        if (transList != null && transList.size() > 0) {
                            whereMap = (HashMap) transList.get(0);
                            //  System.out.println("whereMap.get(BATCH_ID)>>>>"+whereMap.get("BATCH_ID"));
                            investmentBatchId = CommonUtil.convertObjToStr(whereMap.get("BATCH_ID"));
                            double dividendAmount = 0.0;
                            whereMap = new HashMap();
                            dividendAmount = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                            //Authorization
                            whereMap.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                            whereMap.put("BATCH_ID", investmentBatchId);
                            borrowMap.put("FROM_INTEREST_TASK", "FROM_INTEREST_TASK");
                            ArrayList arrList = new ArrayList();
                            HashMap authDataMap = new HashMap();
                            HashMap singleAuthorizeMap = new HashMap();
                            authDataMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(borrowMap.get("USER_ID")));
                            authBy = (CommonUtil.convertObjToStr(borrowMap.get("USER_ID")));
                            authDataMap.put("BATCH_ID", investmentBatchId);
                            arrList.add(authDataMap);
                            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, status);
                            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                            singleAuthorizeMap.put("InvestmentsTransTO", getInvestmentsTransTO(status, dividendAmount, whereMap, map));
                            singleAuthorizeMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(borrowMap.get("USER_ID")));
                            borrowMap.put(CommonConstants.AUTHORIZEMAP, singleAuthorizeMap);
                            borrowMap.put("BORROWING", "BORROW");
                            InvestmentsTransDAO investmentDAO = new InvestmentsTransDAO();
                            whereMap = investmentDAO.execute(borrowMap);
                        }
                    } else {
                        cashAuthMap = new HashMap();
                        cashAuthMap.put(CommonConstants.BRANCH_ID, borrowMap.get("BRANCH_CODE"));
                        cashAuthMap.put(CommonConstants.USER_ID, borrowMap.get("USER_ID"));
                        cashAuthMap.put("DAILY", "DAILY");
                        TransactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
                    }
                    HashMap transMap = new HashMap();
                    transMap.put("LINK_BATCH_ID", linkBatchId);
                    sqlMap.executeUpdate("updateInstrumentNO1Transfer", transMap);
                    sqlMap.executeUpdate("updateInstrumentNO1Cash", transMap);
                    transMap = null;
                }

                /*  //Call this in all places that need Authorization for Transaction
                 cashAuthMap = new HashMap();
                 System.out.println("@#$@zxcvzx#$map:"+map);
                 cashAuthMap.put(CommonConstants.BRANCH_ID, _branchCode);
                 cashAuthMap.put(CommonConstants.USER_ID, AuthMap.get("AUTH_BY"));
                 cashAuthMap.put("DAILY", "DAILY");
                 System.out.println("map:" + map);
                 System.out.println("cashAuthMap:" + cashAuthMap);
                 System.out.println("#$%#$%#$%linkBatchId"+linkBatchId);
                 System.out.println("#$%#$%#$%status"+status);
                 TransactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
                 HashMap transMap = new HashMap();
                 transMap.put("LINK_BATCH_ID",linkBatchId);
                 System.out.println("transMap----------------->"+transMap);
                 sqlMap.executeUpdate("updateInstrumentNO1Transfer", transMap);
                 sqlMap.executeUpdate("updateInstrumentNO1Cash", transMap);
                 transMap=null;
                 */
                objTransactionTO = new TransactionTO();
                objTransactionTO.setBatchId(CommonUtil.convertObjToStr(appNo));
                objTransactionTO.setTransId(CommonUtil.convertObjToStr(linkBatchId));
                objTransactionTO.setBranchId(_branchCode);
                sqlMap.executeUpdate("deleteRemittanceIssueTransactionTO", objTransactionTO);
                if (!status.equals("REJECTED")) {
                }
                map = null;
                //  sqlMap.commitTransaction();
            } catch (Exception e) {
                //  sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw e;
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

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
    }
}
