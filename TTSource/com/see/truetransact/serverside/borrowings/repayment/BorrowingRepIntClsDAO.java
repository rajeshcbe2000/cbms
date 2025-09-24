/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BorrowingRepIntClsDAO.java
 *
 * Created on Thu Jan 20 17:19:05 IST 2005
 */
package com.see.truetransact.serverside.borrowings.repayment;

import java.util.List;
import java.util.ArrayList;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverside.investments.InvestmentsTransDAO;
import com.see.truetransact.transferobject.investments.InvestmentsTransTO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.borrowings.repayment.BorrowingRepIntClsTO;
import java.util.HashMap;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * BorrowingRepIntClsDAO DAO.
 *
 */
public class BorrowingRepIntClsDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private BorrowingRepIntClsTO objTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private Date currDt;
    HashMap returnMap;
    private TransactionDAO transactionDAO = null;

    /**
     * Creates a new instance of TokenConfigDAO
     */
    public BorrowingRepIntClsDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        HashMap where = (HashMap) map.get(CommonConstants.MAP_WHERE);
        List list = null;
        if (where.containsKey("REP_INT_CLS_NO")) {
            list = (List) sqlMap.executeQueryForList("BorrowingRepIntCls.getSelectBorrowingRepIntCls", where);
        } else {
            list = (List) sqlMap.executeQueryForList("BorrowingRepIntCls.getSelectBorrowingsList", where);
        }
        returnMap.put("BorrowingRepIntClsTO", list);
        if (where.containsKey("REP_INT_CLS_NO")) {
            HashMap getRemitTransMap = new HashMap();
            getRemitTransMap.put("TRANS_ID", where.get("REP_INT_CLS_NO"));
            getRemitTransMap.put("TRANS_DT", currDt.clone());
            getRemitTransMap.put("BRANCH_CODE", _branchCode);
            System.out.println("@#%$#@%#$%getRemitTransMap:" + getRemitTransMap);
            list = sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", getRemitTransMap);
            if (list != null && list.size() > 0) {
                returnMap.put("TRANSACTION_LIST", list);
            }
        }
        System.out.println("returnMap 00========" + returnMap);
        return returnMap;
    }

    private String getRepintclsNo() throws Exception {
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "REP_INT_CLS_NO");
        HashMap map = generateID();
        return (String) map.get(CommonConstants.DATA);
    }

    public HashMap generateID() {
        HashMap hash = null, result = null;
        try {
            String mapName = "getCurrentID";
            HashMap where = new HashMap();
            where.put("ID_KEY", "BORROWING_REP_INT_CLS_ID"); //Here u have to pass BORROW_ID or something else
            List list = null;
            sqlMap.executeUpdate("updateIDGenerated", where);  // This update statement just updates curr_value=curr_value+1
            list = (List) sqlMap.executeQueryForList(mapName, where);  // This will get u the updated curr_value, prefix and length
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

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setRepintclsNo(getRepintclsNo());
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            objTO.setCreatedBy(logTO.getUserId());
            doRepaymentTransactions(map);
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            if(map.containsKey("ACT_CLOSING")){
                HashMap closeMap = new HashMap();
                closeMap.put("BORROWING_NO", objTO.getBorrowingNo());
                closeMap.put("CLOSED_DATE", currDt.clone());
                closeMap.put("ACT_STATUS", CommonConstants.CLOSED);
                sqlMap.executeUpdate("updateBorrowingMasterTO", closeMap);
            }
            sqlMap.executeUpdate("insertBorrwingRepIntClsTO", objTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            sqlMap.executeUpdate("updateBorrowingRepIntClsTO", objTO);
            System.out.println("AFTER EXECUTIONNNN====" + objTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_DELETED);
            sqlMap.executeUpdate("deleteBorrowingRepIntClsTO", objTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void doRepaymentTransactions(HashMap map) throws Exception, Exception {
        try {
            System.out.println("!@#$@# Repayment to :" + objTO);
            if (objTO.getCommand() != null) {
                if (objTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                    System.out.println("!@#!@#@# !ID GENERATED:" + objTO.getRepintclsNo());
                    double principalRepaid = CommonUtil.convertObjToDouble(objTO.getPrincipalRepaid()).doubleValue();
                    double intRepaid = CommonUtil.convertObjToDouble(objTO.getIntRepaid()).doubleValue();
                    double penalRepaid = CommonUtil.convertObjToDouble(objTO.getPenalRepaid()).doubleValue();
                    double chargesRepaid = CommonUtil.convertObjToDouble(objTO.getChargesRepaid()).doubleValue();
                    System.out.println("@#$ principalRepaid :" + principalRepaid);
                    System.out.println("@#$ intRepaid :" + intRepaid);
                    System.out.println("@#$ penalRepaid :" + penalRepaid);
                    System.out.println("@#$ chargesRepaid :" + chargesRepaid);
                    double totalAmt = chargesRepaid + penalRepaid + intRepaid + principalRepaid;
                    HashMap txMap;
                    HashMap whereMap = new HashMap();
                    whereMap.put("BORROWING_NO", objTO.getBorrowingNo());
                    HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("Borrowings.getAcHeads", whereMap);

                    if (acHeads == null || acHeads.size() == 0) {
                        throw new TTException("Account heads not set properly...");
                    }
                    TransferTrans objTransferTrans = new TransferTrans();
                    objTransferTrans.setInitiatedBranch(_branchCode);

                    objTransferTrans.setLinkBatchId(objTO.getRepintclsNo());
                    txMap = new HashMap();
                    ArrayList transferList = new ArrayList();

                    LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    //                            System.out.println("TransactionDetailsMap---->"+TransactionDetailsMap);
                    if (TransactionDetailsMap.size() > 0) {
                        if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                            LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                            TransactionTO objTransactionTO = null;
                            System.out.println("@#$@#$#$allowedTransDetailsTO" + allowedTransDetailsTO);
                            for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                double debitAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                    txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN)); //23-08-2016
                                    txMap.put(TransferTrans.PARTICULARS, "To " + objTO.getBorrowingNo() + " Repayment");
                                    txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                    if (objTransactionTO.getProductType().equals("GL")) {
                                        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                    } else {
                                        txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                    }
                                    txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                    //Added By Suresh
                                    if (!objTransactionTO.getProductType().equals("") && objTransactionTO.getProductType().equals("INV")) {
                                        HashMap achdMap = new HashMap();
                                        achdMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                        List achdLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", achdMap);
                                        if (achdLst != null && achdLst.size() > 0) {
                                            achdMap = new HashMap();
                                            achdMap = (HashMap) achdLst.get(0);
                                            System.out.println("achdMap--------------->" + achdMap);
                                            txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("IINVESTMENT_AC_HD"));
                                            txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_WITHDRAWAL");
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            //insert Investment Details in INVESTMENT_TRANS_DETAILS Table
                                            double dividendAmount = 0.0;
                                            whereMap = new HashMap();
                                            dividendAmount = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                            whereMap.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                            InvestmentsTransTO objTO = new InvestmentsTransTO();
                                            objTO = getInvestmentsTransTO(CommonConstants.TOSTATUS_INSERT, dividendAmount, whereMap, map);
                                            sqlMap.executeUpdate("insertInvestmentTransTO", objTO);
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
                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, debitAmt));
                                    if (principalRepaid > 0.0) {
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("PRINCIPAL_GRP_HEAD"));
                                        txMap.put("AUTHORIZEREMARKS", "PRINCIPAL_GRP_HEAD");
                                        txMap.put("TRANS_MOD_TYPE", "BR");
                                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, principalRepaid));
                                    }
                                    if (intRepaid > 0.0) {
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_GRP_HEAD"));
                                        txMap.put("AUTHORIZEREMARKS", "INT_GRP_HEAD");
                                        txMap.put("TRANS_MOD_TYPE", "BR");
                                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, intRepaid));
                                    }
                                    if (penalRepaid > 0.0) {
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("PENAL_GRP_HEAD"));
                                        txMap.put("AUTHORIZEREMARKS", "PENAL_GRP_HEAD");
                                        txMap.put("TRANS_MOD_TYPE", "BR");
                                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, penalRepaid));
                                    }
                                    if (chargesRepaid > 0.0) {
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("CHARGES_GRP_HEAD"));
                                        txMap.put("AUTHORIZEREMARKS", "CHARGES_GRP_HEAD");
                                        txMap.put("TRANS_MOD_TYPE", "BR");
                                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, chargesRepaid));
                                    }
                                    objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                } else {
                                    double transAmt;
                                    CashTransactionTO objCashTO = new CashTransactionTO();
                                    ArrayList cashList = new ArrayList();
                                    if (CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue() > 0) {                                        
                                        txMap = new HashMap();
                                        txMap.put(CommonConstants.BRANCH_ID, _branchCode);
                                        txMap.put(CommonConstants.PRODUCT_ID, TransactionFactory.GL);
                                        txMap.put("TRANS_TYPE", CommonConstants.CREDIT);
                                        if (debitAmt > 0.0) {
                                            txMap.put(CommonConstants.AC_HD_ID, (String) acHeads.get("PRINCIPAL_GRP_HEAD"));
                                            txMap.put("AUTHORIZEREMARKS", "PRINCIPAL_GRP_HEAD");
                                            txMap.put("AMOUNT", new Double(debitAmt));
                                            txMap.put("LINK_BATCH_ID", objTransferTrans.getLinkBatchId());
                                        }
                                        // Added by nithya
                                        if (principalRepaid > 0.0) {
                                            System.out.println("executing pricicipal");
                                            objCashTO = getCashTransactionTO(map);
                                            objCashTO.setAcHdId((String) acHeads.get("PRINCIPAL_GRP_HEAD"));
                                            objCashTO.setInpAmount(principalRepaid);
                                            objCashTO.setAmount(principalRepaid);
                                            cashList.add(objCashTO);
                                        }
                                        System.out.println("cashList after principal :: " + cashList);
                                        if (intRepaid > 0.0) {    
                                            System.out.println("executing intRepaid");
                                            objCashTO = getCashTransactionTO(map);
                                            objCashTO.setAcHdId((String) acHeads.get("INT_GRP_HEAD"));
                                            objCashTO.setInpAmount(intRepaid);
                                            objCashTO.setAmount(intRepaid);
                                            cashList.add(objCashTO);
                                        }
                                        System.out.println("cashList after penal :: " + cashList);
                                        if (penalRepaid > 0.0) {    
                                            objCashTO = getCashTransactionTO(map);
                                            objCashTO.setAcHdId((String) acHeads.get("PENAL_GRP_HEAD"));
                                            objCashTO.setInpAmount(penalRepaid);
                                            objCashTO.setAmount(penalRepaid);
                                            cashList.add(objCashTO);
                                        }
                                        if (chargesRepaid > 0.0) {  
                                            objCashTO = getCashTransactionTO(map);
                                            objCashTO.setAcHdId((String) acHeads.get("CHARGES_GRP_HEAD"));
                                            objCashTO.setInpAmount(chargesRepaid);
                                            objCashTO.setAmount(chargesRepaid);
                                            cashList.add(objCashTO);
                                        }
                                        // End
                                        //cashList.add(objCashTO); // Commented by nithya on 03-03-2017
                                        System.out.println("cashList---------------->" + cashList);
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
                                objTransactionTO.setStatus(CommonConstants.STATUS_CREATED);
                                objTransactionTO.setBatchId(objTO.getRepintclsNo());
                                objTransactionTO.setBatchDt(currDt);
                                objTransactionTO.setTransId(objTransferTrans.getLinkBatchId());
                                objTransactionTO.setBranchId(_branchCode);
                                System.out.println("objTransactionTO------------------->" + objTransactionTO);
                                sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);
                            }
                        }
                    }
                    principalRepaid = 0.0;
                    intRepaid = 0.0;
                    penalRepaid = 0.0;
                    chargesRepaid = 0.0;
                    objTransferTrans = null;
                    transferList = null;
                    txMap = null;
                    // Code End
                    getTransDetails(objTO.getRepintclsNo());
                } else {
                    HashMap shareAcctNoMap = new HashMap();
                    double amtBorrowed = CommonUtil.convertObjToDouble(objTO.getAmtBorrowed()).doubleValue();
                }
            }
        } catch (Exception e) {
            //                sqlMap.rollbackTransaction();
            e.printStackTrace();
            //                throw new TransRollbackException(e);
            throw e;
        }
    }
    
    private CashTransactionTO getCashTransactionTO(HashMap map) {
        CashTransactionTO objCashTO = new CashTransactionTO();
        objCashTO.setTransId("");
        objCashTO.setProdType(TransactionFactory.GL);
        objCashTO.setTransType(CommonConstants.DEBIT);
        objCashTO.setInitTransId(logTO.getUserId());
        objCashTO.setBranchId(_branchCode);
        objCashTO.setStatusBy(logTO.getUserId());
        objCashTO.setStatusDt(currDt);
        objCashTO.setInstrumentNo1("ENTERED_AMOUNT");
        objCashTO.setParticulars("By " + objTO.getBorrowingNo() + " Disbursement");
        objCashTO.setInitiatedBranch(_branchCode);
        objCashTO.setInitChannType(CommonConstants.CASHIER);
        objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
        // Commented by nithya on 03-03-2017
//       objCashTO.setAcHdId((String) acHeads.get("PRINCIPAL_GRP_HEAD"));
//       objCashTO.setInpAmount(objTransactionTO.getTransAmt());
//       objCashTO.setAmount(objTransactionTO.getTransAmt());                                          
        objCashTO.setLinkBatchId(objTO.getRepintclsNo());
        objCashTO.setTransModType("BR");
        objCashTO.setScreenName((String) map.get(CommonConstants.SCREEN)); // 23-08-2016
        System.out.println("objCashTO^^^^^^^" + objCashTO);
        return objCashTO;
    }
    

    private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        returnMap = new HashMap();
        List transList = (List) sqlMap.executeQueryForList("getTransferDetails", getTransMap);
        if (transList != null && transList.size() > 0) {
            returnMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetails", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            returnMap.put("CASH_TRANS_LIST", cashList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    public static void main(String str[]) {
        try {
            BorrowingRepIntClsDAO dao = new BorrowingRepIntClsDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        returnMap = null;
        logDAO = new LogDAO();
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
        System.out.println("*&$@#$@#$map:" + map);

        if (!map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            objTO = (BorrowingRepIntClsTO) map.get("BorrowingRepIntClsTO");
            final String command = objTO.getCommand();
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData(map);
//                returnMap = new HashMap();
                returnMap.put("REP_INT_CLS_NO", objTO.getRepintclsNo());
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData();
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData();
            } else {
                throw new NoCommandException();
            }
        } else {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            authMap.put(CommonConstants.BRANCH_ID, (String) map.get(CommonConstants.BRANCH_ID));
            authMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
            System.out.println("authMap:" + authMap);
            if (authMap != null) {
                authorize(authMap, map);
            }
        }
        destroyObjects();
        return returnMap;
    }

    private void authorize(HashMap map, HashMap borrowMap) throws Exception {
        String status = (String) map.get("STATUS");
        String linkBatchId = null;
        String repIntClsNo = null;
        HashMap cashAuthMap;
        TransactionTO objTransactionTO = null;
        try {
            sqlMap.startTransaction();
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            System.out.println("map:" + map);
            repIntClsNo = CommonUtil.convertObjToStr(map.get("REP_INT_CLS_NO"));
            map.put(CommonConstants.STATUS, status);
            map.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            map.put("CURR_DATE", currDt);
            sqlMap.executeUpdate("authorizeBorrowingRepIntCls", map);
            linkBatchId = CommonUtil.convertObjToStr(map.get("REP_INT_CLS_NO"));//Transaction Batch Id
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
                        investmentBatchId = CommonUtil.convertObjToStr(whereMap.get("BATCH_ID"));
                        double dividendAmount = 0.0;
                        whereMap = new HashMap();
                        dividendAmount = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                        System.out.println("####Investment Transaction");
                        //Authorization
                        whereMap.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                        whereMap.put("BATCH_ID", investmentBatchId);
                        borrowMap.put("FROM_INTEREST_TASK", "FROM_INTEREST_TASK");
                        ArrayList arrList = new ArrayList();
                        HashMap authDataMap = new HashMap();
                        HashMap singleAuthorizeMap = new HashMap();
                        authDataMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(borrowMap.get("USER_ID")));
                        authDataMap.put("BATCH_ID", investmentBatchId);
                        arrList.add(authDataMap);
                        singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, status);
                        singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                        singleAuthorizeMap.put("InvestmentsTransTO", getInvestmentsTransTO(status, dividendAmount, whereMap, map));
                        singleAuthorizeMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(borrowMap.get("USER_ID")));
                        borrowMap.put(CommonConstants.AUTHORIZEMAP, singleAuthorizeMap);
                        InvestmentsTransDAO investmentDAO = new InvestmentsTransDAO();
                        whereMap = investmentDAO.execute(borrowMap);
                    }
                } else {
                    cashAuthMap = new HashMap();
                    cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                    cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                    cashAuthMap.put("DAILY", "DAILY");
                    System.out.println("cashAuthMap:" + cashAuthMap);
                    TransactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
                }
                HashMap transMap = new HashMap();
                transMap.put("LINK_BATCH_ID", linkBatchId);
                sqlMap.executeUpdate("updateInstrumentNO1Transfer", transMap);
                sqlMap.executeUpdate("updateInstrumentNO1Cash", transMap);
                
                if(borrowMap.containsKey("ACT_CLOSING") && status != null && status.equals("REJECTED")){
                    HashMap closeMap = new HashMap();
                    closeMap.put("BORROWING_NO", borrowMap.get("BORROWING_NO"));
                    closeMap.put("CLOSE_DT", null);
                    closeMap.put("ACT_STATUS", CommonConstants.NEW);
                    sqlMap.executeUpdate("updateBorrowingMasterTO", closeMap);
                    closeMap = null;
                }
                transMap = null;
            }
            objTransactionTO = new TransactionTO();
            objTransactionTO.setBatchId(CommonUtil.convertObjToStr(repIntClsNo));
            objTransactionTO.setTransId(CommonUtil.convertObjToStr(linkBatchId));
            objTransactionTO.setBranchId(_branchCode);
            sqlMap.executeUpdate("deleteRemittanceIssueTransactionTO", objTransactionTO);
            map = null;
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
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
            objgetInvestmentsTransTO.setTransType("CREDIT");
            objgetInvestmentsTransTO.setTrnCode("Withdrawal");
            objgetInvestmentsTransTO.setAmount(new Double(0.0));
            objgetInvestmentsTransTO.setPurchaseDt(currDt);
            objgetInvestmentsTransTO.setInvestmentAmount(new Double(intTrfAm));
            objgetInvestmentsTransTO.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            objgetInvestmentsTransTO.setStatusDt(currDt);
            objgetInvestmentsTransTO.setDividendAmount(new Double(0));
            objgetInvestmentsTransTO.setLastIntPaidDate(currDt);
            objgetInvestmentsTransTO.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
            objgetInvestmentsTransTO.setPurchaseMode("SHARE_PAYMENT");
            if (acctDtlMap.containsKey("BATCH_ID")) {
                objgetInvestmentsTransTO.setBatchID(CommonUtil.convertObjToStr(acctDtlMap.get("BATCH_ID")));
            }
        }
        return objgetInvestmentsTransTO;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
    }
}
