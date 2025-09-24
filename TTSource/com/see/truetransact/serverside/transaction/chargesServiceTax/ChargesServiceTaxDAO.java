/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * chargesServiceTaxDAO.java
 *
 * Created on Wed Feb 25 16:04:18 IST 2004
 */
package com.see.truetransact.serverside.transaction.chargesServiceTax;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;

import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.transaction.chargesServiceTax.ChargesServiceTaxTO;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.transaction.common.MultiAuthorizeDAO;

// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

// Transaction
import com.see.truetransact.serverside.transaction.common.Transaction;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import java.util.Date;
import org.apache.log4j.Logger;
//Tds calc
import com.see.truetransact.serverside.tds.tdscalc.TdsCalc;
//
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.transferobject.batchprocess.interest.InterestBatchTO;
import java.math.BigDecimal;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
//interestcalculation TL AD
import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;

/**
 * chargesServiceTaxDAO.
 *
 * @author bala
 *
 */
public class ChargesServiceTaxDAO extends TTDAO {

    private SqlMap sqlMap = null;
    private final Logger log = Logger.getLogger(ChargesServiceTaxDAO.class);
    private final String CHARGES_SERVICE_TAX_TO = "ChargesServiceTaxTO";
    private Transaction transModuleBased;
    private boolean isException = false;
    private HashMap dailyDeposit = new HashMap();
    private TransactionDAO transactionDAO = null;
    private boolean isRecord = true;
    private String depTransId = "";
    int firstRecord = 0;
    //    private HashMap calcMap = new HashMap();
    private boolean isDaily = false;
    private HashMap OAmap;
    //    private HashMap allTermLoanAmt;
    private HashMap interestMap = new HashMap();
    private HashMap depIntMap = new HashMap();
    private String payActNum = null;
    private String payProdId = null;
    private String payProdType = null;
    private String errorList = null;
    private String loan_debit_type = null;
    private HashMap transIdStorageMap = new HashMap();
    private String transID;
    private Date currDt = null;
    private String single_trans_id = null;
    TransactionTO transactionTO = new TransactionTO();
    CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
    TransferDAO transferDAO = new TransferDAO();

    /**
     * Creates a new instance of CashTransactionDAO
     */
    public ChargesServiceTaxDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    /**
     * executeQuery
     */
    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = (Date) ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("#### getData CashTransaction DAO :" + map);
        HashMap returnMap = new HashMap();

        String where = (String) map.get(CommonConstants.MAP_WHERE);
        HashMap param = new HashMap();
        param.put("TRANS_ID", where);
        param.put("TRANS_DT", currDt.clone());
        param.put("INITIATED_BRANCH", _branchCode);
        log.info("where:" + where);
        List list = null;
        list = (List) sqlMap.executeQueryForList("getSelectChargesServiceTaxTO", param);
        returnMap.put(CHARGES_SERVICE_TAX_TO, list);

        List lst = (List) sqlMap.executeQueryForList("getSelectChargesServiceRemittanceIssue", where);
        returnMap.put("TransactionTO", lst);

        HashMap editRemitMap = new HashMap();
        HashMap editMap = new HashMap();
        editMap.put("TRANS_ID", map.get("WHERE"));
        editMap.put("TRANS_DT", currDt.clone());
        editMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(_branchCode));
        editRemitMap.put("TRANS_ID", map.get("WHERE"));
        List lstRemit = sqlMap.executeQueryForList("getSelectRemitTransactionAmt", editMap);
        if (lstRemit != null && lstRemit.size() > 0) {
            editRemitMap = (HashMap) lstRemit.get(0);
        }
        List lstEdit = sqlMap.executeQueryForList("getSelectTransactionAmt", editMap);
        if (lstEdit != null && lstEdit.size() > 0) {
            editMap = (HashMap) lstEdit.get(0);
            double amount = CommonUtil.convertObjToDouble(editMap.get("AMOUNT")).doubleValue();
            double serviceAmt = CommonUtil.convertObjToDouble(editMap.get("SERVICE_TAX_AMT")).doubleValue();
            double totAmt = CommonUtil.convertObjToDouble(editMap.get("TOTAL_AMT")).doubleValue();
            HashMap getTransMap = new HashMap();
            String debitAcNo = CommonUtil.convertObjToStr(editRemitMap.get("DEBIT_ACCT_NO"));
            String prodType = CommonUtil.convertObjToStr(editRemitMap.get("PRODUCT_TYPE"));
            String prodId = CommonUtil.convertObjToStr(editRemitMap.get("PROD_ID"));
            if (!editRemitMap.get("TRANS_TYPE").equals("") && editRemitMap.get("TRANS_TYPE").equals("TRANSFER")) {
                if (!prodType.equals("") && prodType.equals("GL") && prodId.equals("")) {
                    getTransMap.put("LINK_BATCH_ID", editRemitMap.get("BATCH_ID"));
                } else {
                    getTransMap.put("LINK_BATCH_ID", editRemitMap.get("DEBIT_ACCT_NO"));
                }
            } else {
                getTransMap.put("LINK_BATCH_ID", editRemitMap.get("BATCH_ID"));
            }
            getTransMap.put("TODAY_DT", currDt.clone());
            getTransMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(_branchCode));
            System.out.println("########getTransferTransBatchID returnMap depCloseAmt:" + returnMap);
            if (amount > 0) {
                getTransMap.put("AMOUNT", new Double(amount));
                lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                if (lst != null && lst.size() > 0) {
                    returnMap.put("AMT_TRANSACTION", lst.get(0));
                    System.out.println("AMT_TRANSACTION TRANSFER :" + returnMap);
                }
                lst = (List) sqlMap.executeQueryForList("getCashTransBatchID", getTransMap);
                if (lst != null && lst.size() > 0) {
                    returnMap.put("AMT_TRANSACTION", lst.get(0));
                    System.out.println("AMT_TRANSACTION CASH :" + returnMap);
                }
                lst = null;
            }
            if (serviceAmt > 0) {
                getTransMap.put("AMOUNT", new Double(serviceAmt));
                lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                if (lst != null && lst.size() > 0) {
                    returnMap.put("SERVICE_TAX_AMT_TRANSACTION", lst.get(0));
                    System.out.println("SERVICE_TAX_AMT_TRANSACTION TRANSFER:" + returnMap);
                }
                lst = (List) sqlMap.executeQueryForList("getCashTransBatchID", getTransMap);
                if (lst != null && lst.size() > 0) {
                    returnMap.put("SERVICE_TAX_AMT_TRANSACTION", lst.get(0));
                    System.out.println("SERVICE_TAX_AMT_TRANSACTION CASH:" + returnMap);
                }
                lst = null;
            }
            if (totAmt > 0) {
                getTransMap.put("AMOUNT", new Double(totAmt));
                lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                if (lst != null && lst.size() > 0) {
                    returnMap.put("TOTAL_AMT_TRANSACTION", lst.get(0));
                    System.out.println("TOTAL_AMT_TRANSACTION TRANSFER :" + returnMap);
                }
                lst = (List) sqlMap.executeQueryForList("getCashTransBatchID", getTransMap);
                if (lst != null && lst.size() > 0) {
                    returnMap.put("TOTAL_AMT_TRANSACTION", lst.get(0));
                    System.out.println("TOTAL_AMT_TRANSACTION CASH :" + returnMap);
                }
                lst = null;
            }
        }
        where = null;
        map = null;
        ServiceLocator.flushCache(sqlMap);
        return returnMap;
    }

    public HashMap execute(HashMap map) throws Exception {
        return execute(map, true);
    }

    public HashMap execute(HashMap map, boolean isTransaction) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = (Date) ServerUtil.getCurrentDate(_branchCode);
        System.out.println("ChargesServiceTax ExecuteMap :" + map);
        // Log DAO
        LogDAO objLogDAO = new LogDAO();
        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));
        objLogTO.setSelectedBranchId((String) map.get(CommonConstants.SELECTED_BRANCH_ID));
        HashMap execReturnMap = new HashMap();
        if (!map.containsKey(CommonConstants.AUTHORIZEMAP) && map.containsKey(CHARGES_SERVICE_TAX_TO)) {
            ChargesServiceTaxTO objChargesServiceTaxTO = (ChargesServiceTaxTO) map.get(CHARGES_SERVICE_TAX_TO);
            System.out.println("execute objChargesServiceTaxTO " + objChargesServiceTaxTO);
            System.out.println("execute CHARGES_SERVICE_TAX_TO " + CHARGES_SERVICE_TAX_TO);
            if (objChargesServiceTaxTO.getCommand() != null) {
                final String command = objChargesServiceTaxTO.getCommand();
                if (command != null) {
                    try {
                        if (!map.containsKey("LOCKER_SURRENDER_DAO")) {
                            sqlMap.startTransaction();
                        }
                        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                            insertData(map);
                            execReturnMap.put("TRANS_ID", objChargesServiceTaxTO.getTrans_id());
                            execReturnMap.put("SINGLE_TRANS_ID", single_trans_id);


                        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                            updateData(map);
                        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                            deleteData(map);
                        } else {
                            throw new NoCommandException();
                        }
                        if (!map.containsKey("LOCKER_SURRENDER_DAO")) {
                            sqlMap.commitTransaction();
                        }
                    } catch (Exception e) {

                        if (isTransaction) {
                            sqlMap.rollbackTransaction();
                        }

                        e.printStackTrace();
                        throw e;
                    }
                }
                objChargesServiceTaxTO = null;
                map.remove(CHARGES_SERVICE_TAX_TO);
            }
        }
        if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            authorize(map, objLogDAO, objLogTO);
        }
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return execReturnMap;
    }

    private void insertData(HashMap chargesMap) throws Exception {
        boolean fdIntoLocker = false;
        ArrayList transferList = new ArrayList();
        TransferTrans transferTrans = new TransferTrans();
        TxTransferTO transferTo = new TxTransferTO();
        ArrayList TxTransferTO = new ArrayList();
        HashMap txMap = new HashMap();
        double totalServiceTaxAmt = 0.0;
        HashMap serviceTaxMap = new HashMap();                  
        ChargesServiceTaxTO objChargesServiceTaxTO = new ChargesServiceTaxTO();


        //TransactionTO transactionTO=new TransactionTO();
        objChargesServiceTaxTO = (ChargesServiceTaxTO) chargesMap.get("ChargesServiceTaxTO");

        System.out.println("objChargesServiceTaxTO " + objChargesServiceTaxTO);
        String BRANCH_ID = CommonUtil.convertObjToStr(chargesMap.get("BRANCH_CODE"));
        double penalAmount = CommonUtil.convertObjToDouble(objChargesServiceTaxTO.getPenalAmount()).doubleValue();
        double totalAmt = CommonUtil.convertObjToDouble(objChargesServiceTaxTO.getTotal_amt()).doubleValue() + penalAmount;
        double serviceAmt = CommonUtil.convertObjToDouble(objChargesServiceTaxTO.getService_tax_amt()).doubleValue();
        double amount = CommonUtil.convertObjToDouble(objChargesServiceTaxTO.getAmount()).doubleValue();
        if (chargesMap.containsKey("serviceTaxDetails") && chargesMap.get("serviceTaxDetails") != null) {
            serviceTaxMap = (HashMap) chargesMap.get("serviceTaxDetails");
            if (serviceTaxMap.containsKey("TOT_TAX_AMT") && serviceTaxMap.get("TOT_TAX_AMT") != null) {
                totalServiceTaxAmt = CommonUtil.convertObjToDouble(serviceTaxMap.get("TOT_TAX_AMT"));
            }
        }
        totalAmt = totalAmt + totalServiceTaxAmt;
        String head = CommonUtil.convertObjToStr(objChargesServiceTaxTO.getAc_Head());
        transferTrans.setInitiatedBranch(BRANCH_ID);
        

        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);

        transactionDAO.setInitiatedBranch(_branchCode);
        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
        if (chargesMap.containsKey("TransactionTO")) {
            HashMap transactionDetailsMap = (LinkedHashMap) chargesMap.get("TransactionTO");
            System.out.println("*********" + transactionDetailsMap);
            LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
            if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
            }
            transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
            if (transactionTO.getTransType().equals("TRANSFER")) {
                transferTo.setInstrumentNo2("CHARGES_GL_TRANS");
                String linkBatchId = CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo());
                HashMap serviceMap = new HashMap();
                List lst = sqlMap.executeQueryForList("getSelectServiceTaxHead", null);
                if (lst != null && lst.size() > 0) {
                    serviceMap = (HashMap) lst.get(0);
                }
                HashMap debitMap = new HashMap();
                if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("OA")) {
                    debitMap.put("ACT_NUM", transactionTO.getDebitAcctNo());
                    System.out.println("the debit accno is" + transactionTO.getDebitAcctNo());
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
                   if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("TD")) {
                    debitMap.put("PROD_ID", transactionTO.getProductId());
                    lst = sqlMap.executeQueryForList("getDepositClosingHeads", debitMap);
                    if (lst != null && lst.size() > 0) {
                        debitMap = (HashMap) lst.get(0);
                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                    }
                }
 
                 if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("SA")) {
                    debitMap.put("PROD_ID", transactionTO.getProductId());
                    lst = sqlMap.executeQueryForList("getAccountHeadProdSA", debitMap);
                    if (lst != null && lst.size() > 0) {
                        debitMap = (HashMap) lst.get(0);
                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                    }
                }
                 //KD-3961
                if (chargesMap.containsKey("FROM_LOCKER_SI_SCREEN") && chargesMap.get("FROM_LOCKER_SI_SCREEN") != null && transactionTO.getProductType().equals("TD")) {
                    fdIntoLocker = true;
                }
                 
                if (totalAmt > 0) {
                    txMap = new HashMap();
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL") && transactionTO.getProductId().equals("")) {
                        txMap.put(TransferTrans.DR_AC_HD, (String) transactionTO.getDebitAcctNo());
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("OA")) {
                        txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                        txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                        txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                    } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("AD")) {
                        txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HEAD"));
                        txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                        txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.ADVANCES);
                    } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("SA")) {
                        txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HEAD"));
                        txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                        txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.SUSPENSE);
                    } 
                    else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("TD")) { // only for deposit interest as per requirement will modify
                        txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("INT_PAY"));
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    } 
                    txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.PARTICULARS, objChargesServiceTaxTO.getParticulars());
                    //System.out.println("product type here"+transactionTO.getProductType());
                    if (transactionTO.getProductType().equals("OA")) {
                        txMap.put("TRANS_MOD_TYPE", "OA");
                    } else if (transactionTO.getProductType().equals("AB")) {
                        txMap.put("TRANS_MOD_TYPE", "AB");
                    } else if (transactionTO.getProductType().equals("SA")) {
                        txMap.put("TRANS_MOD_TYPE", "SA");
                    } else if (transactionTO.getProductType().equals("TL")) {
                        txMap.put("TRANS_MOD_TYPE", "TL");
                    } else if (transactionTO.getProductType().equals("AD")) {
                        txMap.put("TRANS_MOD_TYPE", "AD");
                    } else {
                        txMap.put("TRANS_MOD_TYPE", "GL");
                    }
                    //txMap.put("TRANS_MOD_TYPE", CommonConstants.LOCKER_TRANSMODE_TYPE);
                    if (chargesMap.containsKey("generateSingleTransId")) {
                        txMap.put("generateSingleTransId", chargesMap.get("generateSingleTransId"));
                    }
                    System.out.println("txMap : " + txMap + "totalAmt :" + totalAmt);

                    transferTo = transactionDAO.addTransferDebitLocal(txMap, totalAmt);

                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setInitiatedBranch(BRANCH_ID);
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(chargesMap.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(chargesMap.get("USER_ID")));
                    if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                        transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                         if(fdIntoLocker){                            
                           transferTo.setGlTransActNum(transactionTO.getDebitAcctNo());
                        }
                    }
                    transferTo.setScreenName((String) chargesMap.get(CommonConstants.SCREEN)); // 23-08-2016
                    System.out.println("transferTo List 1 : " + transferTo);                    
                    TxTransferTO.add(transferTo);
                }
                if (serviceAmt > 0) {
                    txMap = new HashMap();
                    transferTo = new TxTransferTO();
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    if (!chargesMap.containsKey("LOCKER_SURRENDER_DAO")) {
                        txMap.put(TransferTrans.CR_AC_HD, serviceMap.get("OTHER_CHRG_HD"));
                    } else {
                        HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getLockerAccountHeads", objChargesServiceTaxTO.getProd_id());
                        txMap.put(TransferTrans.CR_AC_HD, acHeads.get("SERV_TAX_AC_HD"));
                    }
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.PARTICULARS, objChargesServiceTaxTO.getParticulars()+"-Service Tax");
                    txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
//                    if (transactionTO.getProductType().equals("OA")) {
//                        txMap.put("TRANS_MOD_TYPE", "OA");
//                    } else if (transactionTO.getProductType().equals("AB")) {
//                        txMap.put("TRANS_MOD_TYPE", "AB");
//                    } else if (transactionTO.getProductType().equals("SA")) {
//                        txMap.put("TRANS_MOD_TYPE", "SA");
//                    } else if (transactionTO.getProductType().equals("TL")) {
//                        txMap.put("TRANS_MOD_TYPE", "TL");
//                    } else if (transactionTO.getProductType().equals("AD")) {
//                        txMap.put("TRANS_MOD_TYPE", "AD");
//                    } else {
//                        txMap.put("TRANS_MOD_TYPE", "GL");
//                    }
                    // txMap.put("TRANS_MOD_TYPE", CommonConstants.LOCKER_TRANSMODE_TYPE);// Changed by nithya on 03-02-2017
                    txMap.put("TRANS_MOD_TYPE", CommonConstants.GL_TRANSMODE_TYPE);// Changed by nithya on 03-02-2017
                    if (chargesMap.containsKey("generateSingleTransId")) {
                        txMap.put("generateSingleTransId", chargesMap.get("generateSingleTransId"));
                    }
                    System.out.println("txMap : " + txMap + "serviceAmt :" + serviceAmt);

                    transferTo = transactionDAO.addTransferCreditLocal(txMap, serviceAmt);
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setInitiatedBranch(BRANCH_ID);
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(chargesMap.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(chargesMap.get("USER_ID")));
                    if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                        transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                        if(fdIntoLocker){
                           transferTo.setLinkBatchId(CommonUtil.convertObjToStr(chargesMap.get("LOCKER_SURRENDER_ID")));  
                           transferTo.setGlTransActNum(transactionTO.getDebitAcctNo());
                        }
                    }
                    transferTo.setScreenName((String) chargesMap.get(CommonConstants.SCREEN)); // 23-08-2016
                    System.out.println("transferTo List 2 : " + transferTo);
                    TxTransferTO.add(transferTo);
                }
                if (penalAmount > 0) {
                    txMap = new HashMap();
                    transferTo = new TxTransferTO();
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    if (!chargesMap.containsKey("LOCKER_SURRENDER_DAO")) {
                        txMap.put(TransferTrans.CR_AC_HD, objChargesServiceTaxTO.getPenalAcctHead());
                    } else {
                        HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getLockerAccountHeads", objChargesServiceTaxTO.getProd_id());
                        txMap.put(TransferTrans.CR_AC_HD, acHeads.get("PENAL_INTEREST_AC_HEAD"));
                    }
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.PARTICULARS, objChargesServiceTaxTO.getParticulars()+"-Penal");
                    txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
//                    if (transactionTO.getProductType().equals("OA")) {
//                        txMap.put("TRANS_MOD_TYPE", "OA");
//                    } else if (transactionTO.getProductType().equals("AB")) {
//                        txMap.put("TRANS_MOD_TYPE", "AB");
//                    } else if (transactionTO.getProductType().equals("SA")) {
//                        txMap.put("TRANS_MOD_TYPE", "SA");
//                    } else if (transactionTO.getProductType().equals("TL")) {
//                        txMap.put("TRANS_MOD_TYPE", "TL");
//                    } else if (transactionTO.getProductType().equals("AD")) {
//                        txMap.put("TRANS_MOD_TYPE", "AD");
//                    } else {
//                        txMap.put("TRANS_MOD_TYPE", "GL");
//                    }
                    txMap.put("TRANS_MOD_TYPE", CommonConstants.LOCKER_TRANSMODE_TYPE);
                    if (chargesMap.containsKey("generateSingleTransId")) {
                        txMap.put("generateSingleTransId", chargesMap.get("generateSingleTransId"));
                    }
                    System.out.println("txMap : " + txMap + "penalAmount :" + penalAmount);
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, penalAmount);
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setInitiatedBranch(BRANCH_ID);
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(chargesMap.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(chargesMap.get("USER_ID")));
                    if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                        transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                        if(fdIntoLocker){
                           transferTo.setLinkBatchId(CommonUtil.convertObjToStr(chargesMap.get("LOCKER_SURRENDER_ID")));  
                           transferTo.setGlTransActNum(transactionTO.getDebitAcctNo());
                        }
                    }
                    transferTo.setScreenName((String) chargesMap.get(CommonConstants.SCREEN)); // 23-08-2016
                    System.out.println("transferTo List 2 : " + transferTo);
                    TxTransferTO.add(transferTo);
                }
                if (amount > 0) {
                    txMap = new HashMap();
                    transferTo = new TxTransferTO();
                    if (!objChargesServiceTaxTO.getProd_type().equals("") && objChargesServiceTaxTO.getProd_type().equals("GL")) {
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                          // for Locker Print Purpose for understanding in Product Level( in cash mode already have)
                        txMap.put(TransferTrans.CR_PROD_ID, objChargesServiceTaxTO.getProd_id());
                    } else {
                        txMap.put(TransferTrans.CR_PROD_ID, objChargesServiceTaxTO.getProd_id());
                        txMap.put(TransferTrans.CR_ACT_NUM, objChargesServiceTaxTO.getAcct_num());
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                    }
                    txMap.put(TransferTrans.CR_AC_HD, (String) objChargesServiceTaxTO.getAc_Head());
                    txMap.put(TransferTrans.PARTICULARS, objChargesServiceTaxTO.getParticulars());
                    txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    
//                    if (transactionTO.getProductType().equals("OA")) {
//                        txMap.put("TRANS_MOD_TYPE", "OA");
//                    } else if (transactionTO.getProductType().equals("AB")) {
//                        txMap.put("TRANS_MOD_TYPE", "AB");
//                    } else if (transactionTO.getProductType().equals("SA")) {
//                        txMap.put("TRANS_MOD_TYPE", "SA");
//                    } else if (transactionTO.getProductType().equals("TL")) {
//                        txMap.put("TRANS_MOD_TYPE", "TL");
//                    } else if (transactionTO.getProductType().equals("AD")) {
//                        txMap.put("TRANS_MOD_TYPE", "AD");
//                    } else {
//                        txMap.put("TRANS_MOD_TYPE", "GL");
//                    }
                    txMap.put("TRANS_MOD_TYPE", CommonConstants.LOCKER_TRANSMODE_TYPE);
                    if (chargesMap.containsKey("generateSingleTransId")) {
                        txMap.put("generateSingleTransId", chargesMap.get("generateSingleTransId"));
                    }
                    System.out.println("txMap : " + txMap + "amount :" + amount);


                    transferTo = transactionDAO.addTransferCreditLocal(txMap, amount);

                   // transferTo = transactionDAO.addTransferCreditLocal(txMap, amount);
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setInitiatedBranch(BRANCH_ID);
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(chargesMap.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(chargesMap.get("USER_ID")));
                    if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                        transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                        if(fdIntoLocker){
                           transferTo.setLinkBatchId(CommonUtil.convertObjToStr(chargesMap.get("LOCKER_SURRENDER_ID")));  
                           transferTo.setGlTransActNum(transactionTO.getDebitAcctNo());
                        }
                    }
                    transferTo.setScreenName((String) chargesMap.get(CommonConstants.SCREEN)); // 23-08-2016
                    System.out.println("transferTo List 3 : " + transferTo);
                    TxTransferTO.add(transferTo);
                }
                if (serviceTaxMap != null && serviceTaxMap.size() > 0) {
                    double swachhCess = 0.0;
                    double krishikalyanCess = 0.0;
                    double normalServiceTax = 0.0;
                    double serTaxAmt = 0.0;
                    if (serviceTaxMap.containsKey("TOT_TAX_AMT") && serviceTaxMap.get("TOT_TAX_AMT") != null) {
                        serTaxAmt = CommonUtil.convertObjToDouble(serviceTaxMap.get("TOT_TAX_AMT"));
                    }
                    if (serviceTaxMap.containsKey(ServiceTaxCalculation.SWACHH_CESS) && serviceTaxMap.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                        swachhCess = CommonUtil.convertObjToDouble(serviceTaxMap.get(ServiceTaxCalculation.SWACHH_CESS));
                    }
                    if (serviceTaxMap.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && serviceTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                        krishikalyanCess = CommonUtil.convertObjToDouble(serviceTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                    }
                    if (serviceTaxMap.containsKey(ServiceTaxCalculation.SERVICE_TAX) && serviceTaxMap.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                        normalServiceTax = CommonUtil.convertObjToDouble(serviceTaxMap.get(ServiceTaxCalculation.SERVICE_TAX));
                    }
                   // serTaxAmt -= (swachhCess + krishikalyanCess);
                    // //AX_HEAD_ID=1006001004, SWACHH_HEAD_ID=1006001004, EDUCATION_CESS=0.0, KRISHIKALYAN_HEAD_ID=1006001006,
                    if (swachhCess > 0) {
                        txMap = new HashMap();
                        transferTo = new TxTransferTO();
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);                       
                        txMap.put(TransferTrans.CR_AC_HD, serviceTaxMap.get("SWACHH_HEAD_ID"));                       
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.PARTICULARS, "CGST");
                        txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");//                   
                        txMap.put("TRANS_MOD_TYPE", CommonConstants.GL_TRANSMODE_TYPE);// Changed by nithya on 03-02-2017
                        if (chargesMap.containsKey("generateSingleTransId")) {
                            txMap.put("generateSingleTransId", chargesMap.get("generateSingleTransId"));
                        }   
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, swachhCess);
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setInitiatedBranch(BRANCH_ID);
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(chargesMap.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(chargesMap.get("USER_ID")));
                        if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                            transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                            if (fdIntoLocker) {
                                transferTo.setLinkBatchId(CommonUtil.convertObjToStr(chargesMap.get("LOCKER_SURRENDER_ID")));
                                transferTo.setGlTransActNum(transactionTO.getDebitAcctNo());
                            }
                        }
                        transferTo.setScreenName((String) chargesMap.get(CommonConstants.SCREEN));                 
                        TxTransferTO.add(transferTo);
                        serTaxAmt -= swachhCess;
                    }
                    if (krishikalyanCess > 0) {
                        txMap = new HashMap();
                        transferTo = new TxTransferTO();
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);                       
                        txMap.put(TransferTrans.CR_AC_HD, serviceTaxMap.get("KRISHIKALYAN_HEAD_ID"));                       
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.PARTICULARS, "SGST");
                        txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");//                   
                        txMap.put("TRANS_MOD_TYPE", CommonConstants.GL_TRANSMODE_TYPE);// Changed by nithya on 03-02-2017
                        if (chargesMap.containsKey("generateSingleTransId")) {
                            txMap.put("generateSingleTransId", chargesMap.get("generateSingleTransId"));
                        }   
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, krishikalyanCess);
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setInitiatedBranch(BRANCH_ID);
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(chargesMap.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(chargesMap.get("USER_ID")));
                        if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                            transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                            if (fdIntoLocker) {
                                transferTo.setLinkBatchId(CommonUtil.convertObjToStr(chargesMap.get("LOCKER_SURRENDER_ID")));
                                transferTo.setGlTransActNum(transactionTO.getDebitAcctNo());
                            }
                        }
                        transferTo.setScreenName((String) chargesMap.get(CommonConstants.SCREEN));                 
                        TxTransferTO.add(transferTo);
                        serTaxAmt -= krishikalyanCess;
                    }
                    if (normalServiceTax > 0) {
                        txMap = new HashMap();
                        transferTo = new TxTransferTO();
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);                       
                        txMap.put(TransferTrans.CR_AC_HD, serviceTaxMap.get("TAX_HEAD_ID"));                       
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.PARTICULARS, "SGST");
                        txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");//                   
                        txMap.put("TRANS_MOD_TYPE", CommonConstants.GL_TRANSMODE_TYPE);// Changed by nithya on 03-02-2017
                        if (chargesMap.containsKey("generateSingleTransId")) {
                            txMap.put("generateSingleTransId", chargesMap.get("generateSingleTransId"));
                        }   
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax);
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setInitiatedBranch(BRANCH_ID);
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(chargesMap.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(chargesMap.get("USER_ID")));
                        if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                            transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                            if (fdIntoLocker) {
                                transferTo.setLinkBatchId(CommonUtil.convertObjToStr(chargesMap.get("LOCKER_SURRENDER_ID")));
                                transferTo.setGlTransActNum(transactionTO.getDebitAcctNo());
                            }
                        }
                        transferTo.setScreenName((String) chargesMap.get(CommonConstants.SCREEN));                 
                        TxTransferTO.add(transferTo);                       
                    }
                }
                transferDAO = new TransferDAO();
                chargesMap.put("COMMAND", chargesMap.get("MODE"));
                chargesMap.put("TxTransferTO", TxTransferTO);
                System.out.println("^^&&&&&&" + chargesMap);
                HashMap transMap = new HashMap();
//                if (transactionTO.getProductType().equals("OA")) {
//                    chargesMap.put("TRANS_MOD_TYPE", "OA");
//                } else if (transactionTO.getProductType().equals("AB")) {
//                    chargesMap.put("TRANS_MOD_TYPE", "AB");
//                } else if (transactionTO.getProductType().equals("SA")) {
//                    chargesMap.put("TRANS_MOD_TYPE", "SA");
//                } else if (transactionTO.getProductType().equals("TL")) {
//                    chargesMap.put("TRANS_MOD_TYPE", "TL");
//                } else if (transactionTO.getProductType().equals("AD")) {
//                    chargesMap.put("TRANS_MOD_TYPE", "AD");
//                } else {
//                    chargesMap.put("TRANS_MOD_TYPE", "GL");
//                }
//                txMap.put("TRANS_MOD_TYPE", CommonConstants.LOCKER_TRANSMODE_TYPE);
                transMap = transferDAO.execute(chargesMap, false);
                System.out.println("transMap AFTER : " + transMap + "totalAmt :" + totalAmt);
                objChargesServiceTaxTO.setTrans_id(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                single_trans_id = CommonUtil.convertObjToStr(transMap.get("SINGLE_TRANS_ID"));
                //System.out.println("in charge serviceDao transfer" + single_trans_id);
                transactionTO.setChequeNo("SERVICE_TAX");
                transactionDAO.setBatchId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                transactionDAO.setBatchDate(currDt);
                if (chargesMap.containsKey("LOCKER_SURRENDER_DAO")) {
                    transactionDAO.setBatchId(CommonUtil.convertObjToStr(chargesMap.get("LOCKER_SURRENDER_ID")));
                }
                transactionDAO.execute(chargesMap);
//                HashMap remitMap = new HashMap();
//                lst = sqlMap.executeQueryForList("getSelectMaxTransIdForCash",null);
//                if(lst!=null && lst.size()>0){
//                    remitMap = (HashMap)lst.get(0);
//                    
//                    if(!chargesMap.containsKey("LOCKER_SURRENDER_DAO")){
//                      remitMap.put("BATCH_ID", transMap.get("TRANS_ID"));
//                    }
//                    else{
//                      remitMap.put("BATCH_ID", chargesMap.get("LOCKER_SURRENDER_ID"));
//                    }
//                      remitMap.put("BATCH_DT", currDt);
//                    //                    remitMap.put("TRANS_DT",currDt);
//                    //                    remitMap.put("INITIATED_BRANCH",BRANCH_ID);
//                    System.out.println("remitMap : " + remitMap);
//                    sqlMap.executeUpdate("updateRemitIssueTableCashBatchId", remitMap);
//                }
                HashMap linkBatchMap = new HashMap();
                if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")
                        && transactionTO.getProductId().equals("") && !chargesMap.containsKey("LOCKER_SURRENDER_DAO")) {
                    linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
                } else if (!chargesMap.containsKey("LOCKER_SURRENDER_DAO")) {
                    linkBatchMap.put("LINK_BATCH_ID", transactionTO.getDebitAcctNo());
                } else {
                    linkBatchMap.put("LINK_BATCH_ID", chargesMap.get("LOCKER_SURRENDER_ID"));
                }
                linkBatchMap.put("BATCH_ID", transMap.get("TRANS_ID"));
                linkBatchMap.put("TRANS_DT", currDt.clone());
                linkBatchMap.put("INITIATED_BRANCH", _branchCode);
                if(!fdIntoLocker){
                  sqlMap.executeUpdate("updateLinkBatchIdTransfer", linkBatchMap);
                }
                linkBatchMap = null;
                transMap = null;
            } else if (transactionTO.getTransType().equals("CASH")) {
                transactionTO.setChequeNo("SERVICE_TAX");
                chargesMap.put("CHARGES_SERVICE_TAX", new Double(totalAmt));
                chargesMap.put("CHARGES_SERVICE_TAX_SCREEN", "CHARGES_SERVICE_TAX_SCREEN");
                chargesMap.put("SELECTED_BRANCH_ID", BRANCH_ID);
//                if (transactionTO.getProductType().equals("OA")) {
//                    chargesMap.put("TRANS_MOD_TYPE", "OA");
//                } else if (transactionTO.getProductType().equals("AB")) {
//                    chargesMap.put("TRANS_MOD_TYPE", "AB");
//                } else if (transactionTO.getProductType().equals("SA")) {
//                    chargesMap.put("TRANS_MOD_TYPE", "SA");
//                } else if (transactionTO.getProductType().equals("TL")) {
//                    chargesMap.put("TRANS_MOD_TYPE", "TL");
//                } else if (transactionTO.getProductType().equals("AD")) {
//                    chargesMap.put("TRANS_MOD_TYPE", "AD");
//                } else {
//                    chargesMap.put("TRANS_MOD_TYPE", "GL");
//                }
                //chargesMap.put("TRANS_MOD_TYPE", CommonConstants.LOCKER_TRANSMODE_TYPE);
                if (chargesMap.containsKey("generateSingleTransId")) {
                    chargesMap.put("SINGLE_TRANS_ID", chargesMap.get("generateSingleTransId"));
                }
                //System.out.println("chargesmap here ha ha ha"+chargesMap);
                CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                HashMap cashMap = cashTransactionDAO.execute(chargesMap, false);
                System.out.println("cashMap :" + cashMap);
                String command = CommonUtil.convertObjToStr(objChargesServiceTaxTO.getCommand());
                transactionTO.setBatchId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                objChargesServiceTaxTO.setTrans_id(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                single_trans_id = CommonUtil.convertObjToStr(cashMap.get("SINGLE_TRANS_ID"));
                //System.out.println("single transs id in cash" + cashMap.get("SINGLE_TRANS_ID"));
                transactionTO.setBatchDt(objChargesServiceTaxTO.getCreated_dt());
                transactionDAO.setBatchId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                transactionDAO.setBatchDate(currDt);
                if (chargesMap.containsKey("LOCKER_SURRENDER_DAO")) {
                    transactionDAO.setBatchId(CommonUtil.convertObjToStr(chargesMap.get("LOCKER_SURRENDER_ID")));
                }                
                transactionDAO.execute(chargesMap);
//                HashMap debitMap = new HashMap();
//                List lst = sqlMap.executeQueryForList("getSelectMaxTransIdForCash",null);
//                if(lst!=null && lst.size()>0){
//                    debitMap = (HashMap)lst.get(0);
//                    if(!chargesMap.containsKey("LOCKER_SURRENDER_DAO"))
//                        debitMap.put("BATCH_ID", cashMap.get("TRANS_ID"));
//                    else
//                        debitMap.put("BATCH_ID", chargesMap.get("LOCKER_SURRENDER_ID"));
//                    debitMap.put("BATCH_DT", currDt);
//                    debitMap.put("TRANS_DT",currDt);
//                    debitMap.put("INITIATED_BRANCH",BRANCH_ID);
//                    sqlMap.executeUpdate("updateRemitIssueTableCashBatchId", debitMap);
//                    debitMap = null;
//                }
                HashMap linkBatchMap = new HashMap();
                //                linkBatchMap.put("LINK_BATCH_ID",cashMap.get("TRANS_ID"));
                if (!chargesMap.containsKey("LOCKER_SURRENDER_DAO")) {
                    linkBatchMap.put("LINK_BATCH_ID", cashMap.get("TRANS_ID"));
                } else {
                    linkBatchMap.put("LINK_BATCH_ID", chargesMap.get("LOCKER_SURRENDER_ID"));
                }
                linkBatchMap.put("TRANS_ID", cashMap.get("TRANS_ID"));
                linkBatchMap.put("TRANS_DT", currDt);
                linkBatchMap.put("INITIATED_BRANCH", BRANCH_ID);
                sqlMap.executeUpdate("updateLinkBatchIdCash", linkBatchMap);
                linkBatchMap = null;
            }
            objChargesServiceTaxTO.setCreated_dt(currDt);
            objChargesServiceTaxTO.setTrans_dt(currDt);
            objChargesServiceTaxTO.setParticulars(objChargesServiceTaxTO.getParticulars());
            objChargesServiceTaxTO.setBranchCode(BRANCH_ID);
            sqlMap.executeUpdate("insertChargesServiceTaxTO", objChargesServiceTaxTO);
             if (chargesMap.containsKey("serviceTaxDetailsTO")) {
                ServiceTaxDetailsTO objserTax = (ServiceTaxDetailsTO) chargesMap.get("serviceTaxDetailsTO");
                //objserTax.setAcct_Num(objTO.getRmNumber());
                insertServiceTaxDetails(objserTax);
               }
        }
    }

    private void updateData(HashMap chargesMap) throws Exception {
        ArrayList transferList = new ArrayList();
        TransferTrans transferTrans = new TransferTrans();
        TxTransferTO transferTo = new TxTransferTO();
        ArrayList transferEachList = new ArrayList();
        ChargesServiceTaxTO objChargesServiceTaxTO = new ChargesServiceTaxTO();
        objChargesServiceTaxTO = (ChargesServiceTaxTO) chargesMap.get("ChargesServiceTaxTO");
        System.out.println("objChargesServiceTaxTO " + objChargesServiceTaxTO);
        String BRANCH_ID = CommonUtil.convertObjToStr(chargesMap.get("BRANCH_CODE"));
        double totalAmt = CommonUtil.convertObjToDouble(objChargesServiceTaxTO.getTotal_amt()).doubleValue();
        double serviceAmt = CommonUtil.convertObjToDouble(objChargesServiceTaxTO.getService_tax_amt()).doubleValue();
        double amount = CommonUtil.convertObjToDouble(objChargesServiceTaxTO.getAmount()).doubleValue();
        transferTrans.setInitiatedBranch(BRANCH_ID);
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        transactionDAO.setInitiatedBranch(_branchCode);
        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_UPDATE);
        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
        if (chargesMap.containsKey("TransactionTO")) {
            HashMap transactionDetailsMap = (LinkedHashMap) chargesMap.get("TransactionTO");
            LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
            if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
            }
            transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
            objChargesServiceTaxTO.setStatus(CommonConstants.STATUS_MODIFIED);
            if (transactionTO.getTransType().equals("TRANSFER")) {
                if (chargesMap.containsKey("TOTAL_AMT_TRANSACTION") && CommonUtil.convertObjToDouble(objChargesServiceTaxTO.getTotal_amt()).doubleValue() > 0) {
                    HashMap tempMap = (HashMap) chargesMap.get("TOTAL_AMT_TRANSACTION");
                    tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                    tempMap.put("TRANS_DT", currDt);
                    tempMap.put("INITIATED_BRANCH", objChargesServiceTaxTO.getBranchCode());
                    double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    double newAmount = CommonUtil.convertObjToDouble(objChargesServiceTaxTO.getTotal_amt()).doubleValue();
                    ArrayList batchList = new ArrayList();
                    TxTransferTO txTransferTO = null;
                    HashMap oldAmountMap = new HashMap();
                    String commandMode = CommonUtil.convertObjToStr(objChargesServiceTaxTO.getCommand());
                    if (oldAmount != newAmount || objChargesServiceTaxTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                        List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                        if (lst != null) {
                            for (int i = 0; i < lst.size(); i++) {
                                txTransferTO = (TxTransferTO) lst.get(i);
                                txTransferTO.setInpAmount(new Double(newAmount));
                                txTransferTO.setAmount(new Double(newAmount));
                                if (objChargesServiceTaxTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                                    txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                } else {
                                    txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                                }
                                txTransferTO.setStatusDt(currDt);
                                txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                                oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                                batchList.add(txTransferTO);
                            }
                        }
                        transferTrans = new TransferTrans();
                        transferTrans.setOldAmount(oldAmountMap);
                        transferTrans.setInitiatedBranch(BRANCH_ID);
                        transferTrans.doDebitCredit(batchList, BRANCH_ID, false, commandMode);
                        lst = null;
                        transferTrans = null;
                    }
                    txTransferTO = null;
                    batchList = null;
                    oldAmountMap = null;
                    tempMap = null;
                }
                if (chargesMap.containsKey("AMT_TRANSACTION") && CommonUtil.convertObjToDouble(objChargesServiceTaxTO.getAmount()).doubleValue() > 0) {
                    HashMap tempMap = (HashMap) chargesMap.get("AMT_TRANSACTION");
                    tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                    tempMap.put("TRANS_DT", currDt);
                    tempMap.put("INITIATED_BRANCH", objChargesServiceTaxTO.getBranchCode());
                    double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    double newAmount = CommonUtil.convertObjToDouble(objChargesServiceTaxTO.getAmount()).doubleValue();
                    ArrayList batchList = new ArrayList();
                    TxTransferTO txTransferTO = null;
                    HashMap oldAmountMap = new HashMap();
                    String commandMode = CommonUtil.convertObjToStr(objChargesServiceTaxTO.getCommand());
                    if (oldAmount != newAmount || objChargesServiceTaxTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                        List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                        if (lst != null) {
                            for (int i = 0; i < lst.size(); i++) {
                                txTransferTO = (TxTransferTO) lst.get(i);
                                txTransferTO.setInpAmount(new Double(newAmount));
                                txTransferTO.setAmount(new Double(newAmount));
                                if (objChargesServiceTaxTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                                    txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                } else {
                                    txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                                }
                                txTransferTO.setStatusDt(currDt);
                                txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                                oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                                batchList.add(txTransferTO);
                            }
                        }
                        transferTrans = new TransferTrans();
                        transferTrans.setOldAmount(oldAmountMap);
                        transferTrans.setInitiatedBranch(BRANCH_ID);
                        transferTrans.doDebitCredit(batchList, BRANCH_ID, false, commandMode);
                        lst = null;
                        transferTrans = null;
                    }
                    txTransferTO = null;
                    batchList = null;
                    oldAmountMap = null;
                    tempMap = null;
                }
                if (chargesMap.containsKey("SERVICE_TAX_AMT_TRANSACTION") && CommonUtil.convertObjToDouble(objChargesServiceTaxTO.getService_tax_amt()).doubleValue() > 0) {
                    HashMap tempMap = (HashMap) chargesMap.get("SERVICE_TAX_AMT_TRANSACTION");
                    tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                    tempMap.put("TRANS_DT", currDt);
                    tempMap.put("INITIATED_BRANCH", objChargesServiceTaxTO.getBranchCode());
                    double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    double newAmount = CommonUtil.convertObjToDouble(objChargesServiceTaxTO.getService_tax_amt()).doubleValue();
                    ArrayList batchList = new ArrayList();
                    TxTransferTO txTransferTO = null;
                    HashMap oldAmountMap = new HashMap();
                    String commandMode = CommonUtil.convertObjToStr(objChargesServiceTaxTO.getCommand());
                    if (oldAmount != newAmount || objChargesServiceTaxTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                        List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                        if (lst != null) {
                            for (int i = 0; i < lst.size(); i++) {
                                txTransferTO = (TxTransferTO) lst.get(i);
                                txTransferTO.setInpAmount(new Double(newAmount));
                                txTransferTO.setAmount(new Double(newAmount));
                                if (objChargesServiceTaxTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                                    txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                } else {
                                    txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                                }
                                txTransferTO.setStatusDt(currDt);
                                txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                                oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                                batchList.add(txTransferTO);
                            }
                        }
                        transferTrans = new TransferTrans();
                        transferTrans.setOldAmount(oldAmountMap);
                        transferTrans.setInitiatedBranch(BRANCH_ID);
                        transferTrans.doDebitCredit(batchList, BRANCH_ID, false, commandMode);
                        lst = null;
                        transferTrans = null;
                    }
                    txTransferTO = null;
                    batchList = null;
                    oldAmountMap = null;
                    tempMap = null;
                }
            } else if (transactionTO.getTransType().equals("CASH")) {
                HashMap tempMap = (HashMap) chargesMap.get("AMT_TRANSACTION");
                HashMap serviceMap = (HashMap) chargesMap.get("SERVICE_TAX_AMT_TRANSACTION");
                double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                double serviceAmount = CommonUtil.convertObjToDouble(serviceMap.get("AMOUNT")).doubleValue();

                CashTransactionDAO cashDAO = new CashTransactionDAO();
                CashTransactionTO cashTO = new CashTransactionTO();
                cashTO.setCommand("UPDATE");
                chargesMap.put("BRANCH_CODE", BRANCH_ID);
                chargesMap.put("USER_ID", chargesMap.get("USER_ID"));
                chargesMap.put("OLDAMOUNT", new Double(oldAmount));
                chargesMap.put("SELECTED_BRANCH_ID", BRANCH_ID);
                chargesMap.put("CHARGES_UPDATE", "CHARGES_UPDATE");
                System.out.println("chargesMap :" + chargesMap);
                cashDAO.execute(chargesMap, false);
                oldAmount = serviceAmount;
                amount = serviceAmt;
                cashTO = null;
                cashDAO = null;
            }
            HashMap remitMap = new HashMap();
            remitMap.put("TRANS_AMT", objChargesServiceTaxTO.getTotal_amt());
            remitMap.put("BATCH_ID", transactionTO.getBatchId());
            sqlMap.executeUpdate("updateAmountForRemitIssueTransTable", remitMap);
            HashMap updateMap = new HashMap();
            updateMap.put("PARTICULARS", objChargesServiceTaxTO.getParticulars());
            updateMap.put("AMOUNT", objChargesServiceTaxTO.getAmount());
            updateMap.put("SERVICE_TAX_AMT", objChargesServiceTaxTO.getService_tax_amt());
            updateMap.put("TOTAL_AMT", objChargesServiceTaxTO.getTotal_amt());
            updateMap.put("STATUS", objChargesServiceTaxTO.getStatus());
            updateMap.put("TRANS_ID", transactionTO.getBatchId());
            updateMap.put("INITIATED_BRANCH", BRANCH_ID);
            updateMap.put("TRANS_DT", ServerUtil.getCurrentDate(BRANCH_ID));
            System.out.println("UpdateMap :" + updateMap);
            sqlMap.executeUpdate("updateChargesServiceTaxTO", updateMap);
            remitMap = null;
            updateMap = null;
        }
    }

    private void deleteData(HashMap chargesMap) throws Exception {
        ArrayList transferList = new ArrayList();
        TransferTrans transferTrans = new TransferTrans();
        TxTransferTO transferTo = new TxTransferTO();
        ArrayList transferEachList = new ArrayList();
        ChargesServiceTaxTO objChargesServiceTaxTO = new ChargesServiceTaxTO();
        objChargesServiceTaxTO = (ChargesServiceTaxTO) chargesMap.get("ChargesServiceTaxTO");
        System.out.println("objChargesServiceTaxTO " + objChargesServiceTaxTO);
        String BRANCH_ID = CommonUtil.convertObjToStr(chargesMap.get("BRANCH_CODE"));
        double totalAmt = CommonUtil.convertObjToDouble(objChargesServiceTaxTO.getTotal_amt()).doubleValue();
        double serviceAmt = CommonUtil.convertObjToDouble(objChargesServiceTaxTO.getService_tax_amt()).doubleValue();
        double amount = CommonUtil.convertObjToDouble(objChargesServiceTaxTO.getAmount()).doubleValue();
        transferTrans.setInitiatedBranch(BRANCH_ID);
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        transactionDAO.setInitiatedBranch(_branchCode);
        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_UPDATE);
        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
        HashMap tempMap = new HashMap();
        HashMap deleteMap = new HashMap();
        double oldAmount = 0.0;
        if (chargesMap.containsKey("TransactionTO")) {
            HashMap transactionDetailsMap = (LinkedHashMap) chargesMap.get("TransactionTO");
            System.out.println("Inside deleteData chargesMap containskey :" + chargesMap);
            LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
            if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
            }
            transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
            System.out.println("Inside deleteData transactionTO :" + transactionTO);
            objChargesServiceTaxTO.setStatus(CommonConstants.STATUS_DELETED);
            if (transactionTO.getTransType().equals("TRANSFER")) {
                System.out.println("Inside TRANSFER ");
                if (chargesMap.containsKey("TOTAL_AMT_TRANSACTION") && CommonUtil.convertObjToDouble(objChargesServiceTaxTO.getTotal_amt()).doubleValue() > 0) {
                    tempMap = (HashMap) chargesMap.get("TOTAL_AMT_TRANSACTION");
                    System.out.println("#### tempMap : " + tempMap);
                    oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                    tempMap.put("TRANS_DT", currDt);
                    tempMap.put("INITIATED_BRANCH", BRANCH_ID);
                    tempMap.remove("TRANS_ID");
                    tempMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(BRANCH_ID));
                    double newAmount = CommonUtil.convertObjToDouble(objChargesServiceTaxTO.getTotal_amt()).doubleValue();
                    ArrayList batchList = new ArrayList();
                    TxTransferTO txTransferTO = null;
                    HashMap oldAmountMap = new HashMap();
                    String commandMode = CommonUtil.convertObjToStr(objChargesServiceTaxTO.getCommand());
                    if (objChargesServiceTaxTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                        List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                        if (lst != null) {
                            for (int i = 0; i < lst.size(); i++) {
                                txTransferTO = (TxTransferTO) lst.get(i);
                                if (objChargesServiceTaxTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                                    txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                }
                                txTransferTO.setStatusDt(currDt);
                                txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                                oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                                batchList.add(txTransferTO);
                            }
                        }
                        transferTrans = new TransferTrans();
                        transferTrans.setOldAmount(oldAmountMap);
                        transferTrans.setInitiatedBranch(BRANCH_ID);
                        transferTrans.doDebitCredit(batchList, BRANCH_ID, false, commandMode);
                        lst = null;
                        transferTrans = null;
                    }
                    txTransferTO = null;
                    batchList = null;
                    oldAmountMap = null;
                }
                deleteMap.put("TRANS_ID", tempMap.get("BATCH_ID"));
            } else if (transactionTO.getTransType().equals("CASH")) {
                tempMap = (HashMap) chargesMap.get("AMT_TRANSACTION");
                System.out.println("#### inside CASH tempMap : " + tempMap);
                oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                CashTransactionDAO cashDAO = new CashTransactionDAO();
                CashTransactionTO cashTO = new CashTransactionTO();
                HashMap cashMap = new HashMap();
                String transId = CommonUtil.convertObjToStr(tempMap.get("TRANS_ID"));
                cashMap.put("TRANS_ID", tempMap.get("TRANS_ID"));
                cashMap.put("TRANS_DT", currDt);
                cashMap.put("INITIATED_BRANCH", BRANCH_ID);
                System.out.println("cashMap 1st :" + cashMap);
                List lstCash = (List) sqlMap.executeQueryForList("getSelectCashTransactionTO", cashMap);
                if (lstCash != null && lstCash.size() > 0) {
                    for (int i = 0; i < lstCash.size(); i++) {
                        cashTO = (CashTransactionTO) lstCash.get(i);
                        cashTO.setCommand("DELETE");
                        cashTO.setStatus(CommonConstants.STATUS_DELETED);
                        cashMap.put("CashTransactionTO", cashTO);
                        cashMap.put("BRANCH_CODE", BRANCH_ID);
                        cashMap.put("USER_ID", chargesMap.get("USER_ID"));
                        cashMap.put("OLDAMOUNT", new Double(oldAmount));
                        cashMap.put("SELECTED_BRANCH_ID", BRANCH_ID);
                        System.out.println("cashMap :" + cashMap);
                        cashDAO.execute(cashMap, false);
                    }
                }
                deleteMap.put("TRANS_ID", tempMap.get("TRANS_ID"));
                cashMap = null;
                cashTO = null;
                cashDAO = null;
            }
            deleteMap.put("PARTICULARS", objChargesServiceTaxTO.getParticulars());
            deleteMap.put("AMOUNT", objChargesServiceTaxTO.getAmount());
            deleteMap.put("SERVICE_TAX_AMT", objChargesServiceTaxTO.getService_tax_amt());
            deleteMap.put("TOTAL_AMT", objChargesServiceTaxTO.getTotal_amt());
            deleteMap.put("STATUS", objChargesServiceTaxTO.getStatus());
            deleteMap.put("INITIATED_BRANCH", BRANCH_ID);
            deleteMap.put("TRANS_DT", ServerUtil.getCurrentDate(BRANCH_ID));
            System.out.println("UpdateMap :" + deleteMap);
            sqlMap.executeUpdate("updateChargesServiceTaxTO", deleteMap);
            deleteMap = null;
            tempMap = null;
        }
    }

    private void authorize(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        System.out.println("AuthorizeMap :" + map);
        if (map.containsKey("TransactionTO")) {
            HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
            LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
            if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
            }
            transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
            HashMap serviceAuthMap = new HashMap();
            if (transactionTO.getTransType().equals("TRANSFER")) {
                System.out.println("transactionTO.getTransType() :" + transactionTO.getTransType());
                HashMap authorizeMap = new HashMap();
                authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
                System.out.println("map :" + map);
                HashMap cashAuthMap = new HashMap();
                String authorizeStatus = CommonUtil.convertObjToStr(authorizeMap.get("AUTHORIZESTATUS"));
                serviceAuthMap.put("AUTHORIZE_STATUS", authorizeStatus);
                serviceAuthMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                serviceAuthMap.put("TRANS_ID", transactionTO.getBatchId());
                String linkBatchId = "";
                if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL")) {
                    linkBatchId = CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo());
                } else {
                    linkBatchId = CommonUtil.convertObjToStr(transactionTO.getBatchId());
                }
                cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                System.out.println(" cashAuthMap :" + cashAuthMap + "linkBatchId :" + linkBatchId + "authorizeStatus :" + authorizeStatus);
                TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
                System.out.println("transactionTO.getTransType() :" + transactionTO.getTransType());
                authorizeMap = null;
                cashAuthMap = null;
            } else {
                System.out.println("transactionTO.getTransType() :" + transactionTO.getTransType());
                cashTransactionDAO = new CashTransactionDAO();
                ArrayList arrList = new ArrayList();
                HashMap authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
                HashMap singleAuthorizeMap = new HashMap();
                String status = CommonUtil.convertObjToStr(authorizeMap.get("AUTHORIZESTATUS"));
                serviceAuthMap.put("AUTHORIZE_STATUS", status);
                singleAuthorizeMap.put("STATUS", status);
                singleAuthorizeMap.put("TRANS_ID", CommonUtil.convertObjToStr(transactionTO.getBatchId()));
                singleAuthorizeMap.put("USER_ID", map.get("USER_ID"));
                serviceAuthMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                serviceAuthMap.put("TRANS_ID", transactionTO.getBatchId());
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
                cashTransactionDAO = null;
                map = null;
                dataMap = null;
            }
            serviceAuthMap.put("AUTHORIZE_DT", currDt);
            serviceAuthMap.put("TRANS_DT", currDt);
            serviceAuthMap.put("INITIATED_BRANCH", _branchCode);
            sqlMap.executeUpdate("updateAuthorizeTransaction", serviceAuthMap);
            serviceAuthMap = null;
        }
    }
    
     private void insertServiceTaxDetails(ServiceTaxDetailsTO objserviceTaxDetailsTO) {
        try {
            objserviceTaxDetailsTO.setServiceTaxDet_Id(getServiceTaxNo());
            objserviceTaxDetailsTO.setParticulars("Romm Rent");
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

    public static void main(String str[]) {
        try {
            ChargesServiceTaxDAO dao = new ChargesServiceTaxDAO();
            ChargesServiceTaxTO objChargesServiceTaxTO = new ChargesServiceTaxTO();

            TOHeader toHeader = new TOHeader();
            toHeader.setCommand(CommonConstants.TOSTATUS_INSERT);//To tell what to do... Insert, Update, Delete...

            System.out.println("Inserted");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
