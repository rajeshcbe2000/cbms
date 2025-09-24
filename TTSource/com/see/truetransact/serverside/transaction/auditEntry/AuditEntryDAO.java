/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CashTransactionDAO.java
 *
 * Created on Wed Feb 25 16:04:18 IST 2004
 */
package com.see.truetransact.serverside.transaction.auditEntry;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.termloan.TermLoanPenalWaiveOffTO;
import com.see.truetransact.transferobject.termloan.loanrebate.LoanRebateTO;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.transferobject.transaction.auditEntry.AuditEntryTO;
import com.see.truetransact.transferobject.transaction.chargesServiceTax.ChargesServiceTaxTO;
import com.see.truetransact.transferobject.transaction.reconciliation.ReconciliationTO;
import com.see.truetransact.serverside.transaction.common.MultiAuthorizeDAO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;

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
import com.see.truetransact.transferobject.batchprocess.interest.InterestBatchTO;
import com.see.truetransact.serverside.deposit.lien.DepositLienDAO;
import com.see.truetransact.transferobject.deposit.lien.DepositLienTO;

//interestcalculation TL AD
import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;

// for sending SMS alerts 
import com.see.truetransact.transferobject.sms.SMSParameterTO;
import com.see.truetransact.transferobject.common.mobile.SMSSubscriptionTO;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.see.truetransact.serverside.common.sms.SmsConfigDAO;
/**
 * CashTransaction DAO.
 *
 * @author bala
 *
 */
public class AuditEntryDAO extends TTDAO {

    private SqlMap sqlMap = null;
    private final Logger log = Logger.getLogger(AuditEntryDAO.class);
    private final String CASH_TO = "CashTransactionTO";
    private Transaction transModuleBased;
    private boolean isException = false;
    private boolean isRecord = true;
    private String depTransId = "";
    int firstRecord = 0;
    //    private HashMap calcMap = new HashMap();
    private boolean isDaily = false;
    private HashMap OAmap;
    //    private HashMap allTermLoanAmt;
    private HashMap interestMap = new HashMap();
    private HashMap depIntMap = new HashMap();
    private String errorList = null;
    private String loan_debit_type = null;
    private HashMap transIdStorageMap = new HashMap();
    private String transID;
    private Date currDt = null;
    private Date properFormatDate;
    private DepositLienDAO depositLienDAO = new DepositLienDAO();
    private DepositLienTO depositLienTO = new DepositLienTO();
    //    private boolean flexiFlag = false;
    private double flexiAmount = 0.0;
    private HashMap flexiDeletionMap = new HashMap();
    private String status = null;
    private String account_closing = "";
    ReconciliationTO reconciliationTO = new ReconciliationTO();
    private String act_closing_min_bal_check = null;
    private final String BATCH_ID = "BATCH_ID";
    private final String PRESENT_TRANS_ID = "PRESENT_TRANS_ID";
    private final String LINK_BATCH_ID = "LINK_BATCH_ID";
    private final String PROD_ID = "PROD_ID";
    private final String DEPOSIT_NO = "DEPOSIT_NO";
    private final String BEHAVES_LIKE = "BEHAVES_LIKE";
    private final String AUTHORIZE_STATUS = "AUTHORIZE_STATUS";
    private final String SHADOWLIEN = "SHADOWLIEN";
    private final String LIEN_AMOUNT = "LIEN_AMOUNT";
    private String user = "";
    HashMap otherChargesMap;
    private String shift = "";
    private String shifttime = "";
    private Map corpLoanMap = null; // For Corporate Loan purpose added by Rajesh
    private Map cache;                  //used to hold references to Resources for re-use
    SmsConfigDAO smsConfigDAO;
    /**
     * Creates a new instance of CashTransactionDAO
     */
    public AuditEntryDAO() throws ServiceLocatorException, Exception {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    /**
     * executeQuery
     */
    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("#### getData CashTransaction DAO :" + map);
        HashMap returnMap = new HashMap();
        currDt = (Date) ServerUtil.getCurrentDate(_branchCode);
        Date trans_Dt = (Date) currDt.clone();
        Date tempDt = (Date) map.get("TRANS_DT");
        if (tempDt != null) {
            trans_Dt.setDate(tempDt.getDate());
            trans_Dt.setMonth(tempDt.getMonth());
            trans_Dt.setYear(tempDt.getYear());
            map.put("TRANS_DT", trans_Dt);
        }
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        log.info("where:" + where);
        List list = null;
        if (map.containsKey(CommonConstants.MAP_NAME)) {
            String mapName = (String) map.get(CommonConstants.MAP_NAME);
            list = (List) sqlMap.executeQueryForList(mapName, where);
            returnMap.put("DOCUMENT_LIST", list);
        } else {
            if (map.containsKey("AGENT_ID")) {
                map.put("TRANS_DT", currDt.clone());
                map.put("INITIATED_BRANCH", _branchCode);
                list = (List) sqlMap.executeQueryForList("getSelectCashTransactionTODAILY", map);
            } else {
                list = (List) sqlMap.executeQueryForList("getSelectCashTransactionTO", map);
            }
            returnMap.put(CASH_TO, list);
            list = (List) sqlMap.executeQueryForList("getSelectCashDenominationTO", map);
            returnMap.put("DENOMINATION_LIST", list);
            list = (List) sqlMap.executeQueryForList("getSelectCashDenominationTO", map);
        }
        where = null;
        map = null;
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;
    }

    public HashMap execute(HashMap map) throws Exception {
        return execute(map, true);
    }

    public HashMap execute(HashMap map, boolean isTransaction) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = (Date) ServerUtil.getCurrentDate(_branchCode);
        if ((map.containsKey("ACCOUNT_CLOSING")) && (map.get("ACCOUNT_CLOSING") != null)) {
            account_closing = CommonUtil.convertObjToStr(map.get("ACCOUNT_CLOSING"));
        }
        if ((map.containsKey("ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER")) && (map.get("ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER") != null)) {
            act_closing_min_bal_check = CommonUtil.convertObjToStr(map.get("ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER"));
        }
        if (map.containsKey(CommonConstants.USER_ID)) {
            user = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
        } else {
            user = "";
        }

        System.out.println("execute Tds Map : " + map + currDt);
        ArrayList asAnwhenList = new ArrayList();
        ArrayList depositPenalList = new ArrayList();
        ArrayList chargesServiceTax = new ArrayList();
        interestMap = (HashMap) map.get("INTERESTMAP");
        //        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        HashMap execReturnMap = new HashMap();
        HashMap intTransMap = (HashMap) map.get("INTTRANSMAP");
        System.out.println("execute intTrans Map : " + intTransMap);

        HashMap whereMap = new HashMap();
        whereMap.put("BRANCH_CODE",_branchCode);
        List shftlst = sqlMap.executeQueryForList("getBranchShiftDetails",whereMap);
        if (shftlst != null && shftlst.size() > 0) {
            HashMap hmap = (HashMap) shftlst.get(0);
            shift = CommonUtil.convertObjToStr(hmap.get("TRANSAUTH_TIME"));
            shifttime = CommonUtil.convertObjToStr(hmap.get("SHIFT"));
        }
        // For Corporate Loan purpose added by Rajesh
        corpLoanMap = new HashMap();
        if (map.containsKey("CORP_LOAN_MAP")) {
            corpLoanMap = (HashMap) map.get("CORP_LOAN_MAP");
        }

        if (!map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            asAnwhenList = setTransactionDetailTLAD(map);
        }
        if (asAnwhenList != null && asAnwhenList.size() > 0) {
            //            map.put("AS_AN_CUSTOMER_LIST",asAnwhenList);
            map.put("DAILYDEPOSITTRANSTO", asAnwhenList);
            asAnwhenList = new ArrayList();
            System.out.println("asAnwhenList      ####" + asAnwhenList);
            map.remove(CASH_TO);
        }
        if (map.containsKey("DEPOSIT_PENAL_AMT") && !map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            double penalAmt = CommonUtil.convertObjToDouble(map.get("DEPOSIT_PENAL_AMT")).doubleValue();
            if (penalAmt > 0) {
                depositPenalList = penalReceived(map);
                map.put("DAILYDEPOSITTRANSTO", depositPenalList);
                depositPenalList = new ArrayList();
                System.out.println("depositPenalList      ####" + depositPenalList);
                map.remove(CASH_TO);
            }
        }
        if (map.containsKey("CHARGES_SERVICE_TAX_SCREEN")) {
            System.out.println("CHARGES_SERVICE_TAX_SCREEN map ####" + map);
            double tot_amt = CommonUtil.convertObjToDouble(map.get("CHARGES_SERVICE_TAX")).doubleValue();
            if (tot_amt > 0) {
                chargesServiceTax = chargesServiceTax(map);
                System.out.println("chargesServiceTax map ####" + chargesServiceTax);
                map.put("DAILYDEPOSITTRANSTO", chargesServiceTax);
                chargesServiceTax = new ArrayList();
            }
        }
        if (map.containsKey("RENEWAL_DEPOSIT_SCREEN")) {
            ArrayList renewalList = new ArrayList();
            renewalList = (ArrayList) map.get("RENEWAL_LIST");
            map.put("DAILYDEPOSITTRANSTO", renewalList);
            renewalList = new ArrayList();
        }
        if (map.containsKey("CHARGES_UPDATE")) {
            chargesServiceTax = chargesServiceTaxUpate(map);
            map.put("DAILYDEPOSITTRANSTO", chargesServiceTax);
            chargesServiceTax = new ArrayList();
        }
        if (map.containsKey("FLEXI_LIEN_CREATION")) {
            flexiDeletionMap = new HashMap();
            flexiAmount = CommonUtil.convertObjToDouble(map.get("FLEXI_AMOUNT")).doubleValue();
            flexiDeletionMap.put("FLEXI_LIEN_CREATION", map.get("FLEXI_LIEN_CREATION"));
        }
        if (map.containsKey("FLEXI_LIEN_DELETION")) {
            flexiDeletionMap = new HashMap();
            flexiDeletionMap.put("FLEXI_LIEN_DELETION", map.get("FLEXI_LIEN_DELETION"));
        }
        if (map.containsKey("DEPOSIT_INTEREST")) {
            depIntMap = new HashMap();
            depIntMap.put("DEPOSIT_INTEREST", map.get("DEPOSIT_INTEREST"));
            depIntMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
            System.out.println("depIntMap :" + depIntMap);
        }
        if (interestMap != null) {
            interestMap.put(CommonConstants.BRANCH_ID, map.get("SELECTED_BRANCH_ID"));
        }
        // initial Objects
        double oldAmount = 0.0;
        if (!map.containsKey("CHARGES_UPDATE")) {
            oldAmount = CommonUtil.convertObjToDouble(map.get(TransactionDAOConstants.OLDAMT)).doubleValue();
        }

        String productType = "";

        loan_debit_type = null;
        if (map.containsKey("DEBIT_LOAN_TYPE") && map.get("DEBIT_LOAN_TYPE") != null) {
            loan_debit_type = CommonUtil.convertObjToStr(map.get("DEBIT_LOAN_TYPE"));
        }
        if (map.containsKey("EXCEPTION")) {
            isException = true;
        } else {
            isException = false;
        }

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

        OAmap = new HashMap();
        if (map.containsKey("PROCCHARGEMAP")) {
            if (map.get("PROCCHARGEMAP") != null) {
                OAmap.put("PROCCHARGEMAP", map.get("PROCCHARGEMAP"));
            }
        }
        ArrayList dailyData = new ArrayList();
        int i = 0, j = 0, size = 0;
        if (map.containsKey("DAILYDEPOSITTRANSTO")) {
            dailyData = (ArrayList) map.get("DAILYDEPOSITTRANSTO");
            System.out.println("ArrayListdailyData" + dailyData);
            j = dailyData.size();
            size = dailyData.size();
            isDaily = true;
        } else {
            isDaily = false;
        }
        isRecord = true;
        firstRecord = 0;
        depTransId = "";
        while (isRecord) {
            System.out.println(i + " : i : and j : value : " + j);
            //            do{
            if (i + 1 <= j) {
                System.out.println(i + "inside if : i : and j : value : " + j);
                CashTransactionTO cashTransaction = (CashTransactionTO) dailyData.get(i);
                System.out.println(i + ":  iVaule : and jvalue   :" + j);
                map.put(CASH_TO, cashTransaction);
                System.out.println("cashTransactionDAO######" + cashTransaction);
                firstRecord = i;
                cashTransaction = null;
            } else {
                isRecord = false;
            }
            int listSize = 0;
            int k = 0;
            if (asAnwhenList != null && asAnwhenList.size() > 0) {
                listSize = asAnwhenList.size();
            }
            do {
                if (listSize > k) {
                    depTransId = "";
                    map.put(CASH_TO, asAnwhenList.get(k));
                }
                if (map.containsKey(CASH_TO)) {
                    System.out.println("##### map :" + map);
                    CashTransactionTO objCashTransactionTO = (CashTransactionTO) map.get(CASH_TO);

                    System.out.println("##### objCashTransactionTO :" + objCashTransactionTO);
                    if (map.containsKey("CHARGES_UPDATE")) {
                        HashMap amtMap = (HashMap) map.get("AMT_TRANSACTION");
                        HashMap serviceMap = (HashMap) map.get("SERVICE_TAX_AMT_TRANSACTION");
                        if (map.containsKey("AMT_TRANSACTION") && !objCashTransactionTO.getInstrumentNo2().equals("")
                                && objCashTransactionTO.getInstrumentNo2().equals("INPUT_AMOUNT")) {
                            oldAmount = CommonUtil.convertObjToDouble(amtMap.get("AMOUNT")).doubleValue();
                            System.out.println("INPUT_AMOUNT oldAmount :" + oldAmount);

                        }
                        if (map.containsKey("SERVICE_TAX_AMT_TRANSACTION") && !objCashTransactionTO.getInstrumentNo2().equals("")
                                && objCashTransactionTO.getInstrumentNo2().equals("SERVICE_TAX")) {
                            oldAmount = CommonUtil.convertObjToDouble(serviceMap.get("AMOUNT")).doubleValue();
                            System.out.println("SERVICE_TAX_AMT_TRANSACTION oldAmount :" + oldAmount);
                        }
                    }
                    // Module Based Updations
                    productType = objCashTransactionTO.getProdType();
                    if (productType != null && !productType.equals("")) {
                        transModuleBased = TransactionFactory.createTransaction(productType);
                    }
                    System.out.println("execute objCashTransactionTO" + objCashTransactionTO);
                    System.out.println("execute CASH_TO" + CASH_TO);
                    if (intTransMap != null) {
                        System.out.println("####intTransMap :" + intTransMap);
                        String command = CommonConstants.TOSTATUS_INSERT;
                        objCashTransactionTO.setCommand(command);
                        System.out.println("execute intMap objCashTransactionTO inside :" + objCashTransactionTO);
                    }

                    if (objCashTransactionTO.getCommand() != null) {
                        final String command = objCashTransactionTO.getCommand();

                        if (command != null) {

                            try {
                                if (isTransaction) {
                                    sqlMap.startTransaction();
                                }

                                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {

                                    insertData(objCashTransactionTO);
                                    if (map.containsKey("ORG_RESP_DETAILS")) {
                                        insertData(map);
                                    }
                                    insertDenomination(map, objCashTransactionTO);
                                    execReturnMap.put(CommonConstants.TRANS_ID, objCashTransactionTO.getTransId());
                                    if (map.containsKey("ReconciliationTO")) {
                                        reconciliationTO = new ReconciliationTO();// it will store into reconciliation_tarans table
                                        reconciliationTO = (ReconciliationTO) map.get("ReconciliationTO");
                                        reconciliationTO.setTransId(objCashTransactionTO.getTransId());
                                        reconciliationTO.setBatchId(objCashTransactionTO.getTransId());
                                        reconciliationTO.setTransDt(objCashTransactionTO.getTransDt());
                                        reconciliationTO.setStatus(objCashTransactionTO.getStatus());
                                        reconciliationTO.setParticulars(objCashTransactionTO.getParticulars());
                                        sqlMap.executeUpdate("insertReconciliationTO", reconciliationTO);//reconciliation table it will store
                                    }
                                    if (map.containsKey("LIST_OF_REDUCED") && map.get("LIST_OF_REDUCED") != null) {
                                        HashMap listMap = new HashMap();
                                        listMap = (HashMap) map.get("LIST_OF_REDUCED");
                                        ArrayList updateList = new ArrayList();
                                        updateList = (ArrayList) listMap.get("TOTAL_LIST");
                                        HashMap updateMap = new HashMap();
                                        if (updateList.size() > 0) {
                                            for (int l = 0; l < updateList.size(); l++) {
                                                reconciliationTO = new ReconciliationTO();// it will store into reconciliation_tarans table
                                                updateMap = new HashMap();
                                                String value = CommonUtil.convertObjToStr(((ArrayList) updateList.get(l)).get(0));
                                                if (!value.equals("") && value.equals("true")) {
                                                    double balance = CommonUtil.convertObjToDouble(((ArrayList) updateList.get(l)).get(7)).doubleValue();
                                                    String oldTransId = CommonUtil.convertObjToStr(((ArrayList) updateList.get(l)).get(8));
                                                    String oldBatchId = CommonUtil.convertObjToStr(((ArrayList) updateList.get(l)).get(1));
                                                    HashMap presentMap = new HashMap();
                                                    presentMap.put(PRESENT_TRANS_ID, objCashTransactionTO.getTransId());
                                                    presentMap.put("PRESENT_BATCH_ID", objCashTransactionTO.getTransId());
                                                    presentMap.put("PRESENT_AMOUNT", new Double(balance));
                                                    presentMap.put("BATCH_ID", oldBatchId);
                                                    presentMap.put("TRANS_ID", oldTransId);
                                                    presentMap.put("PRESENT_TRANS_DT", currDt.clone());
                                                    presentMap.put("TRANS_DT", CommonUtil.convertObjToStr(((ArrayList) updateList.get(k)).get(2)));
                                                    presentMap.put("INITIATED_BRANCH", _branchCode);
                                                    System.out.println("presentMap :" + presentMap);
                                                    sqlMap.executeUpdate("updatePresentTransaction", presentMap);
                                                }
                                            }
                                        }
                                    }
                                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                                    updateData(objCashTransactionTO, oldAmount);
                                    if (map.containsKey("ORG_RESP_DETAILS")) {
                                        updateData(map, objCashTransactionTO);
                                    }
                                    deleteDenomination(objCashTransactionTO);
                                    insertDenomination(map, objCashTransactionTO);
                                    HashMap reconcileMap = new HashMap();
                                    reconcileMap.put("BATCH_ID", objCashTransactionTO.getTransId());
                                    reconcileMap.put("TRANS_DT", currDt.clone());
                                    reconcileMap.put("INITIATED_BRANCH", _branchCode);
                                    List reconcile = sqlMap.executeQueryForList("getSelectReconciliation", reconcileMap);
                                    if (reconcile != null && reconcile.size() > 0) {//incase reconciliation size is greater than 0,
                                        reconciliationTO = new ReconciliationTO();// it will store into reconciliation_tarans table
                                        reconciliationTO = (ReconciliationTO) reconcile.get(0);
                                        reconciliationTO.setStatus(objCashTransactionTO.getStatus());
                                        reconciliationTO.setStatusBy(objCashTransactionTO.getStatusBy());
                                        reconciliationTO.setTransAmount(objCashTransactionTO.getAmount());
                                        reconciliationTO.setBalanceAmount(objCashTransactionTO.getAmount());
                                        reconciliationTO.setTransDt(objCashTransactionTO.getTransDt());
                                        reconciliationTO.setStatusDt(objCashTransactionTO.getStatusDt());
                                        reconciliationTO.setInitiatedBranch(objCashTransactionTO.getInitiatedBranch());
                                        sqlMap.executeUpdate("updateReconciliationTO", reconciliationTO);//reconciliation table it will store
                                    }
                                    if (map.containsKey("LIST_OF_REDUCED") && map.get("LIST_OF_REDUCED") != null) {
                                        HashMap listMap = new HashMap();
                                        listMap = (HashMap) map.get("LIST_OF_REDUCED");
                                        ArrayList updateList = new ArrayList();
                                        updateList = (ArrayList) listMap.get("TOTAL_LIST");
                                        HashMap updateMap = new HashMap();
                                        if (updateList.size() > 0) {
                                            for (int l = 0; l < updateList.size(); l++) {
                                                reconciliationTO = new ReconciliationTO();// it will store into reconciliation_tarans table
                                                updateMap = new HashMap();
                                                String value = CommonUtil.convertObjToStr(((ArrayList) updateList.get(l)).get(0));
                                                double balance = CommonUtil.convertObjToDouble(((ArrayList) updateList.get(l)).get(7)).doubleValue();
                                                if (!value.equals("") && value.equals("true")) {
                                                    String oldTransId = CommonUtil.convertObjToStr(((ArrayList) updateList.get(l)).get(8));
                                                    String oldBatchId = CommonUtil.convertObjToStr(((ArrayList) updateList.get(l)).get(1));
                                                    HashMap presentMap = new HashMap();
                                                    presentMap.put(PRESENT_TRANS_ID, objCashTransactionTO.getTransId());
                                                    presentMap.put("PRESENT_BATCH_ID", objCashTransactionTO.getTransId());
                                                    presentMap.put("PRESENT_AMOUNT", new Double(balance));
                                                    presentMap.put("BATCH_ID", oldBatchId);
                                                    presentMap.put("TRANS_ID", oldTransId);
                                                    presentMap.put("PRESENT_TRANS_DT", currDt.clone());
                                                    presentMap.put("TRANS_DT", CommonUtil.convertObjToStr(((ArrayList) updateList.get(k)).get(2)));
                                                    presentMap.put("INITIATED_BRANCH", _branchCode);
                                                    System.out.println("presentMap if part :" + presentMap);
                                                    sqlMap.executeUpdate("updatePresentTransaction", presentMap);
                                                } else if (!value.equals("") && value.equals("false") && balance == 0) {
                                                    String oldTransId = CommonUtil.convertObjToStr(((ArrayList) updateList.get(l)).get(8));
                                                    String oldBatchId = CommonUtil.convertObjToStr(((ArrayList) updateList.get(l)).get(1));
                                                    HashMap presentMap = new HashMap();
                                                    presentMap.put(PRESENT_TRANS_ID, "");
                                                    presentMap.put("PRESENT_BATCH_ID", "");
                                                    presentMap.put("PRESENT_AMOUNT", new Double(0));
                                                    presentMap.put("BATCH_ID", oldBatchId);
                                                    presentMap.put("TRANS_ID", oldTransId);
                                                    presentMap.put("PRESENT_TRANS_DT", currDt.clone());
                                                    presentMap.put("TRANS_DT", CommonUtil.convertObjToStr(((ArrayList) updateList.get(k)).get(2)));
                                                    presentMap.put("INITIATED_BRANCH", _branchCode);
                                                    System.out.println("presentMap else part :" + presentMap);
                                                    sqlMap.executeUpdate("updatePresentTransaction", presentMap);
                                                }
                                            }
                                        }
                                    }
                                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                    deleteData(objCashTransactionTO, oldAmount);
                                    if (map.containsKey("ORG_RESP_DETAILS")) {
                                        deleteData(map, objCashTransactionTO);
                                    }
                                    deleteDenomination(objCashTransactionTO);
                                    HashMap reconcileMap = new HashMap();
                                    reconcileMap.put("BATCH_ID", objCashTransactionTO.getTransId());
                                    reconcileMap.put("TRANS_DT", currDt.clone());
                                    reconcileMap.put("INITIATED_BRANCH", _branchCode);
                                    List reconcile = sqlMap.executeQueryForList("getSelectReconciliation", reconcileMap);
                                    if (reconcile != null && reconcile.size() > 0) {//incase reconciliation size is greater than 0,
                                        reconciliationTO = new ReconciliationTO();// it will store into reconciliation_tarans table
                                        reconciliationTO = (ReconciliationTO) reconcile.get(0);
                                        reconciliationTO.setStatus(objCashTransactionTO.getStatus());
                                        reconciliationTO.setStatusBy(objCashTransactionTO.getStatusBy());
                                        reconciliationTO.setTransAmount(objCashTransactionTO.getAmount());
                                        reconciliationTO.setBalanceAmount(objCashTransactionTO.getAmount());
                                        reconciliationTO.setTransDt(objCashTransactionTO.getTransDt());
                                        reconciliationTO.setStatusDt(objCashTransactionTO.getStatusDt());
                                        reconciliationTO.setInitiatedBranch(objCashTransactionTO.getInitiatedBranch());
                                        sqlMap.executeUpdate("updateReconciliationTO", reconciliationTO);//reconciliation table it will store
                                    }
                                    HashMap reconcileAuthMap = new HashMap();
                                    reconcileAuthMap.put("PRESENT_TRANS_ID", objCashTransactionTO.getTransId());
//                                    properFormatDate = objCashTransactionTO.getTransDt();
                                    System.out.println("txTransferTO.getTransDt()#### :" + objCashTransactionTO.getTransDt());
                                    Date trDt = (Date) currDt.clone();
                                    trDt.setDate(objCashTransactionTO.getTransDt().getDate());
                                    trDt.setMonth(objCashTransactionTO.getTransDt().getMonth());
                                    trDt.setYear(objCashTransactionTO.getTransDt().getYear());
//                                    reconcileAuthMap.put("TRANS_DT",trDt);
                                    reconcileAuthMap.put("PRESENT_TRANS_DT", trDt);
                                    System.out.println("reconcileAuthMapsDt()#### :" + reconcileAuthMap);
//                                    reconcileAuthMap.put("PRESENT_TRANS_DT",objCashTransactionTO.getTransDt());
                                    List lst = sqlMap.executeQueryForList("getSelectAllinDeleteMode", reconcileAuthMap);
                                    if (lst != null && lst.size() > 0) {
                                        for (int l = 0; l < lst.size(); l++) {
                                            reconcileAuthMap = (HashMap) lst.get(l);
                                            HashMap presentMap = new HashMap();
                                            presentMap.put("PRESENT_TRANS", "");
                                            presentMap.put("PRESENT_BATCH", "");
                                            presentMap.put("PRESENT_AMOUNT", new Double(0));
                                            presentMap.put("PRESENT_TRANS_ID", reconcileAuthMap.get("PRESENT_TRANS_ID"));
                                            presentMap.put("PRESENT_TRANS_DT", reconcileAuthMap.get("PRESENT_TRANS_DT"));
                                            System.out.println("presentMap :" + presentMap);
                                            sqlMap.executeUpdate("deleteOldReconcileAmt", presentMap);
                                        }
                                    }
                                } else {
                                    throw new NoCommandException();
                                }

                                objLogTO.setData(objCashTransactionTO.toString());
                                objLogTO.setPrimaryKey(objCashTransactionTO.getKeyData());
                                objLogTO.setStatus(command);

                                objLogDAO.addToLog(objLogTO);

                                if (isTransaction) {
                                    sqlMap.commitTransaction();
                                }
                                if (isTransaction) {
                                    sqlMap.startTransaction();
                                }
                                //                                    if(objCashTransactionTOIns.getProdType().equals(TransactionFactory.LOANS)){

                                if (OAmap != null) {
                                    if (OAmap.containsKey("PROCCHARGEMAP")) {
                                        if (OAmap.get("PROCCHARGEMAP") != null) {
                                            HashMap hash = new HashMap();
                                            hash = (HashMap) OAmap.get("PROCCHARGEMAP");
                                            boolean flag = new Boolean(CommonUtil.convertObjToStr(hash.get("FLAG_STATUS"))).booleanValue();

                                            if (flag) {
                                                updateProcessingChargesFromLoan(hash);
                                            } else {
                                                updateProcessingChargesFormOperative(hash);

                                            }
                                        }
                                    }
                                }
                                if (isTransaction) {
                                    sqlMap.commitTransaction();
                                }
                            } catch (Exception e) {

                                if (isTransaction) {
                                    sqlMap.rollbackTransaction();
                                }

                                e.printStackTrace();
                                throw e;
                            }

                            if (isTransaction && command.equals(CommonConstants.TOSTATUS_INSERT)) {
                                execReturnMap.put(CommonConstants.TRANS_ID, objCashTransactionTO.getTransId());
                                //System.out.println("execReturnMap:" + execReturnMap);
                            }

                        }

                        objCashTransactionTO = null;
                        map.remove(CASH_TO);
                    }
                }
                k++;
            } while (listSize > k);

            if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
                HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
                status = (String) authMap.get(CommonConstants.AUTHORIZESTATUS);
                if (map.containsKey("ORG_RESP_DETAILS")) {
                    ArrayList list = null;
                    list = (ArrayList) map.get("ORG_RESP_DETAILS");
                    if (list != null && list.size() > 0) {
                        HashMap hmap = (HashMap) list.get(0);
                        String type = CommonUtil.convertObjToStr(hmap.get("OrgOrRespTransType"));
                        if (type.equals("R")) {
                            hmap.put("RECONSILED", "RESPONDED");
                            hmap.put("DATE", currDt);
                            sqlMap.executeUpdate("updateReconsiledOrg", hmap);
                        }
                        hmap.put("STATUS", status);
                        hmap.put("STATUS_BY", objLogTO.getUserId());
                        hmap.put("STATUS_DT", currDt);
                        sqlMap.executeUpdate("authorizeOrgRespDetails", hmap);
                    }
                }
                if (map.containsKey("PRODUCT")) {
                    authMap.put("PRODUCT", "SHARE");
                }
                if (map.containsKey(LINK_BATCH_ID)) {
                    if (map.get(LINK_BATCH_ID) != null) {
                        authMap.put(LINK_BATCH_ID, map.get(LINK_BATCH_ID));
                        authMap.put(BATCH_ID, map.get(BATCH_ID));
                        authMap.put("REMARKS", map.get("REMARKS"));
                        authMap.put(CommonConstants.BRANCH_ID, _branchCode);
                        //authMap.putAll(map);
                    }
                }
                if (map.containsKey("DEPOSIT_PENAL_AMT")) {
                    authMap.put("DAILY", "DAILY");
                    authMap.put("DEPOSIT_PENAL_AMT", map.get("DEPOSIT_PENAL_AMT"));
                    authMap.put("DEPOSIT_PENAL_MONTH", map.get("DEPOSIT_PENAL_MONTH"));
                }
                if (authMap != null && isAuthorizeUserList(map)) {
                    System.out.println("######authMap :" + map);
                    authMap.put("INTERESTMAP", map.get("INTERESTMAP"));
                    try {
                        authorize(authMap, objLogDAO, objLogTO, isTransaction);
                    } catch (Exception e) {
                        throw e;
                    }
                }
                authMap = null;
            }
            i++;
        }
        //        isException=false;
//        oldAmount = null;
        objLogDAO = null;
        objLogTO = null;
        map = null;
        transModuleBased = null;
        flexiDeletionMap = null;
        //        execReturnMap.put("ERRORLIST", errorList);
        errorList = null;
        asAnwhenList = null;
        depositPenalList = null;
        interestMap = null;
        intTransMap = null;
        depIntMap = null;
        //        calcMap = null;
        dailyData = null;
        act_closing_min_bal_check = null;
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return execReturnMap;

    }

    private ArrayList setTransactionDetailTLAD(HashMap dataMap) throws Exception {
        ArrayList cashList = new ArrayList();
        List appList = null;
        String ots = "";
        double paidInterest = 0;
        double paidprincipal = 0;
        double transAmt = 0.0;
        double rebateInterest = 0;
        double waiveOffInterest = 0;
        boolean isTLAvailable = false;
        String penalWaiveOff = "";
        String rebateAllowed = "";
        long no_of_installment = 0;
        String asAnWhenCustomer = "";
        HashMap map = new HashMap();
        System.out.println("dataMap ####" + dataMap);
        HashMap ALL_LOAN_AMOUNT = new HashMap();
        CashTransactionTO objCashTransactionTO = (CashTransactionTO) dataMap.get(CASH_TO);
        if (objCashTransactionTO != null && objCashTransactionTO.getTransType().equals(CommonConstants.CREDIT)) {
            if (objCashTransactionTO.getLinkBatchId() != null && objCashTransactionTO.getLinkBatchId().length() > 0) {
                map.put(CommonConstants.ACT_NUM, objCashTransactionTO.getLinkBatchId());
                ALL_LOAN_AMOUNT = (HashMap) dataMap.get("ALL_AMOUNT");

                if (ALL_LOAN_AMOUNT != null && ALL_LOAN_AMOUNT.containsKey("REBATE_INTEREST") && CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("REBATE_INTEREST")).doubleValue() > 0) {
                    rebateInterest = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("REBATE_INTEREST")).doubleValue();
                }

                if (ALL_LOAN_AMOUNT != null && ALL_LOAN_AMOUNT.containsKey("WAIVE_OFF_INTEREST") && CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("WAIVE_OFF_INTEREST")).doubleValue() > 0) {
                    waiveOffInterest = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("WAIVE_OFF_INTEREST")).doubleValue();
                }

                if (ALL_LOAN_AMOUNT != null && ALL_LOAN_AMOUNT.containsKey("PENAL_WAIVE_OFF") && CommonUtil.convertObjToStr(ALL_LOAN_AMOUNT.get("PENAL_WAIVE_OFF")).equals("Y")) {
                    penalWaiveOff = "Y";
                } else {
                    penalWaiveOff = "N";
                }

                if (dataMap.containsKey("REBATE_INTEREST") && CommonUtil.convertObjToStr(dataMap.get("REBATE_INTEREST")).equals("Y")) {
                    rebateAllowed = "Y";
                } else {
                    rebateAllowed = "N";
                }
                map.put("TRANS_DT", currDt);
                map.put("INITIATED_BRANCH", _branchCode);
                List lst = sqlMap.executeQueryForList("IntCalculationDetail", map);
                if (lst == null || lst.isEmpty()) {
                    lst = sqlMap.executeQueryForList("IntCalculationDetailAD", map);
                }
                if (lst != null && lst.size() > 0) {
                    map = (HashMap) lst.get(0);
                    System.out.println("map ####" + map);
                    asAnWhenCustomer = CommonUtil.convertObjToStr(map.get("AS_CUSTOMER_COMES"));
                    ots = CommonUtil.convertObjToStr(map.get("OTS"));
                    map.put(CommonConstants.ACT_NUM, objCashTransactionTO.getLinkBatchId());
                    map.put("ACCT_NUM", objCashTransactionTO.getLinkBatchId());
                    map.put(PROD_ID, objCashTransactionTO.getProdId());
                }
                lst = null;
            }
        }
        if (asAnWhenCustomer != null && asAnWhenCustomer.length() > 0 && asAnWhenCustomer.equals("Y")) {// && actionType==ClientConstants.ACTIONTYPE_NEW){

            if (objCashTransactionTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                HashMap whereMap = new HashMap();
                //                    whereMap.put(CommonConstants.MAP_WHERE,map.get(CommonConstants.ACT_NUM));
                whereMap.put(CommonConstants.MAP_WHERE, objCashTransactionTO.getTransId());


                List list = sqlMap.executeQueryForList("getCashTransactionTOForAuthorzationTransId", whereMap.get(CommonConstants.MAP_WHERE));
                if (list != null && list.size() > 0) {
                    cashList = new ArrayList();
                    for (int i = 0; i < list.size(); i++) {
                        objCashTransactionTO = (CashTransactionTO) list.get(i);
                        objCashTransactionTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                        cashList.add(objCashTransactionTO);
                        if (i == 0) {
                            deleteRebateInterestTransaction(objCashTransactionTO.getLinkBatchId(), CommonConstants.TOSTATUS_DELETE);
                        }
                    }
                }
                System.out.println("cashListdelete ####" + cashList);
                whereMap = null;
                list = null;
                return cashList;
            }
            if (objCashTransactionTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                //                map.put(PROD_ID,CommonUtil.convertObjToStr(getCbmProdId().getKeyForSelected()));
                List lstachd = sqlMap.executeQueryForList("getInterestAndPenalIntActHead", map);
                if (lstachd != null && lstachd.size() > 0) {
                    map = (HashMap) lstachd.get(0);
                }
                transAmt = objCashTransactionTO.getAmount().doubleValue();
                if (ALL_LOAN_AMOUNT.containsKey("NO_OF_INSTALLMENT") && ALL_LOAN_AMOUNT.get("NO_OF_INSTALLMENT") != null) {
                    no_of_installment = CommonUtil.convertObjToLong(ALL_LOAN_AMOUNT.get("NO_OF_INSTALLMENT"));
                }
//                //account closing charges
//                CashTransactionTO objCashTO = setCashTransaction(objCashTransactionTO);
//                double account_closing_charge=CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("ACT_CLOSING_CHARGE")).doubleValue();
//                if(transAmt>0 && account_closing_charge>0 ){
//                    if( transAmt>=account_closing_charge){
//                        transAmt-=account_closing_charge;
//                        paidInterest=account_closing_charge;
//                    }else{
//                        paidInterest=transAmt;
//                        transAmt-=account_closing_charge;
//                        
//                    }
//                    objCashTO.setAmount(new Double(paidInterest));
//                    objCashTO.setActNum("");
//                    objCashTO.setProdId("");
//                    objCashTO.setProdType(TransactionFactory.GL);
//                    objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("AC_CLOSING_CHRG")));
//                    objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
//                    objCashTO.setAuthorizeRemarks("ACT CLOSING CHARGE");
//                    objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId()+":"+"ACT CLOSING CHARGE");
//                    cashList.add(objCashTO);
//                }
//                paidInterest=0;
                if (ots.equals("Y")) {
                    appList = sqlMap.executeQueryForList("selectAppropriatTransaction_OTS", map.get("PROD_ID"));
                } else {
                    appList = sqlMap.executeQueryForList("selectAppropriatTransaction", map.get("PROD_ID"));
                }
                HashMap appropriateMap = new HashMap();
                if (appList != null && appList.size() > 0) {
                    appropriateMap = (HashMap) appList.get(0);
                    appropriateMap.remove("PROD_ID");
                } else {
                    throw new TTException("Please Enter Hierachy of Transaction  in This Product ");
                }
                System.out.println("appropriateMap####" + appropriateMap);
                java.util.Collection collectedValues = appropriateMap.values();
                java.util.Iterator it = collectedValues.iterator();
                CashTransactionTO objCashTO = new CashTransactionTO();
                int appTranValue = 0;
                while (it.hasNext()) {
                    appTranValue++;
                    String hierachyValue = CommonUtil.convertObjToStr(it.next());
                    System.out.println("hierachyValue####" + hierachyValue);
                    objCashTO = setCashTransaction(objCashTransactionTO);
                    if (hierachyValue.equals("CHARGES")) {
                        //Account Closing Charges
//                CashTransactionTO objCashTO = setCashTransaction(objCashTransactionTO);
                        if (ALL_LOAN_AMOUNT.containsKey("ACT_CLOSING_CHARGES")) {
                            List chargeLst = (List) ALL_LOAN_AMOUNT.get("ACT_CLOSING_CHARGES");
                            if (chargeLst != null && chargeLst.size() > 0) {
                                System.out.println("@##$#$% chargeLst #### :" + chargeLst);
                                for (int i = 0; i < chargeLst.size(); i++) {
                                    objCashTO = setCashTransaction(objCashTransactionTO);
                                    HashMap chargeMap = new HashMap();
                                    String accHead = "";
                                    double chargeAmt = 0;
                                    chargeMap = (HashMap) chargeLst.get(i);
                                    accHead = CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"));
                                    chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")).doubleValue();
                                    System.out.println("$#@@$ accHead" + accHead);
                                    System.out.println("$#@@$ chargeAmt" + chargeAmt);
                                    if (transAmt > 0 && chargeAmt > 0) {
                                        if (transAmt >= chargeAmt) {
                                            transAmt -= chargeAmt;
                                            paidInterest = chargeAmt;
                                        } else {
                                            paidInterest = transAmt;
                                            transAmt -= chargeAmt;
                                        }
                                        objCashTO.setAmount(new Double(paidInterest));
                                        objCashTO.setActNum("");
                                        objCashTO.setProdId("");
                                        objCashTO.setProdType(TransactionFactory.GL);
                                        objCashTO.setAcHdId(CommonUtil.convertObjToStr(accHead));
                                        objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                                        objCashTO.setAuthorizeRemarks("ACT CLOSING CHARGE");
                                        objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId() + ":" + "ACT CLOSING CHARGE");
                                        objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
                                        objCashTO.setNarration(objCashTransactionTO.getNarration());
                                        objCashTO.setInstrumentNo2("LOAN_ACT_CLOSING_CHARGE");
                                        cashList.add(objCashTO);
                                    }
                                    paidInterest = 0;
                                    chargeAmt = 0;
                                }
                            }
                        }
                        //account clsoing misc charges
                        objCashTO = setCashTransaction(objCashTransactionTO);
                        double accountClosingMisc = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("ACT_CLOSING_MISC_CHARGE")).doubleValue();
                        if (transAmt > 0 && accountClosingMisc > 0) {
                            if (transAmt >= accountClosingMisc) {
                                transAmt -= accountClosingMisc;
                                paidInterest = accountClosingMisc;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= accountClosingMisc;
                            }
                            objCashTO.setAmount(new Double(paidInterest));
                            objCashTO.setActNum("");
                            objCashTO.setProdId("");
                            objCashTO.setProdType(TransactionFactory.GL);
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("MISC_SERV_CHRG")));
                            objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                            objCashTO.setAuthorizeRemarks("ACT CLOSING MISC CHARGE");
                            objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId() + ":" + "ACT CLOSING MISC CHARGE");
                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
                            objCashTO.setNarration(objCashTransactionTO.getNarration());
                            objCashTO.setInstrumentNo2("LOAN_ACT_CLOSING_MISC_CHARGE");
                            cashList.add(objCashTO);
                        }
                        paidInterest = 0;
                        //postage charges
                        objCashTO = setCashTransaction(objCashTransactionTO);
                        double postageCharges = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("POSTAGE CHARGES")).doubleValue();
                        if (transAmt > 0 && postageCharges > 0) {
                            if (transAmt >= postageCharges) {
                                transAmt -= postageCharges;
                                paidInterest = postageCharges;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= postageCharges;
                            }
                            objCashTO.setAmount(new Double(paidInterest));
                            objCashTO.setActNum("");
                            objCashTO.setProdId("");
                            objCashTO.setProdType(TransactionFactory.GL);
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("POSTAGE_CHARGES")));
                            objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                            objCashTO.setAuthorizeRemarks("POSTAGE CHARGES");
                            objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId() + ":" + "POSTAGE_CHARGES");
                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
                            objCashTO.setNarration(objCashTransactionTO.getNarration());
                            objCashTO.setInstrumentNo2("LOAN_POSTAGE_CHARGES");
                            cashList.add(objCashTO);
                        }
                        paidInterest = 0;

                        //advertisement charges
                        objCashTO = setCashTransaction(objCashTransactionTO);
                        double advertiseCharges = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("ADVERTISE CHARGES")).doubleValue();
                        if (transAmt > 0 && advertiseCharges > 0) {
                            if (transAmt >= advertiseCharges) {
                                transAmt -= advertiseCharges;
                                paidInterest = advertiseCharges;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= advertiseCharges;
                            }
                            objCashTO.setAmount(new Double(paidInterest));
                            objCashTO.setActNum("");
                            objCashTO.setProdId("");
                            objCashTO.setProdType(TransactionFactory.GL);
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("ADVERTISE_ACHEAD")));
                            objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                            objCashTO.setAuthorizeRemarks("ADVERTISE CHARGES");
                            objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId() + ":" + "ADVERTISE_CHARGES");
                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
                            objCashTO.setNarration(objCashTransactionTO.getNarration());
                            objCashTO.setInstrumentNo2("LOAN_ADVERTISE_CHARGES");
                            cashList.add(objCashTO);
                        }
                        paidInterest = 0;

                        //arbitary charges
                        objCashTO = setCashTransaction(objCashTransactionTO);
                        double orbitaryCharges = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("ARBITRARY CHARGES")).doubleValue();
                        if (transAmt > 0 && orbitaryCharges > 0) {
                            if (transAmt >= orbitaryCharges) {
                                transAmt -= orbitaryCharges;
                                paidInterest = orbitaryCharges;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= orbitaryCharges;
                            }
                            objCashTO.setAmount(new Double(paidInterest));
                            objCashTO.setActNum("");
                            objCashTO.setProdId("");
                            objCashTO.setProdType(TransactionFactory.GL);
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("ARBITRARY_CHARGES")));//MISC_SERV_CHRG
                            objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                            objCashTO.setAuthorizeRemarks("ARBITRARY CHARGES");
                            objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId() + ":" + "ARBITRARY CHARGES");
                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
                            objCashTO.setNarration(objCashTransactionTO.getNarration());
                            objCashTO.setInstrumentNo2("LOAN_ARBITRARY_CHARGES");
                            cashList.add(objCashTO);
                        }
                        paidInterest = 0;
                        //legal charges
                        objCashTO = setCashTransaction(objCashTransactionTO);
                        double legalCharges = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("LEGAL CHARGES")).doubleValue();
                        if (transAmt > 0 && legalCharges > 0) {
                            if (transAmt >= legalCharges) {
                                transAmt -= legalCharges;
                                paidInterest = legalCharges;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= legalCharges;
                            }
                            objCashTO.setAmount(new Double(paidInterest));
                            objCashTO.setActNum("");
                            objCashTO.setProdId("");
                            objCashTO.setProdType(TransactionFactory.GL);
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("LEGAL_CHARGES")));//NOTICE_CHARGES
                            objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                            objCashTO.setAuthorizeRemarks("LEGAL CHARGES");
                            objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId() + ":" + "LEGAL CHARGES");
                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
                            objCashTO.setNarration(objCashTransactionTO.getNarration());
                            objCashTO.setInstrumentNo2("LOAN_LEGAL_CHARGES");
                            cashList.add(objCashTO);
                        }
                        paidInterest = 0;
                        //insurance charges
                        objCashTO = setCashTransaction(objCashTransactionTO);
                        double insuranceCharge = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("INSURANCE CHARGES")).doubleValue();
                        if (transAmt > 0 && insuranceCharge > 0) {
                            if (transAmt >= insuranceCharge) {
                                transAmt -= insuranceCharge;
                                paidInterest = insuranceCharge;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= insuranceCharge;
                            }
                            objCashTO.setAmount(new Double(paidInterest));
                            objCashTO.setActNum("");
                            objCashTO.setProdId("");
                            objCashTO.setProdType(TransactionFactory.GL);
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("INSURANCE_CHARGES")));//MISC_SERV_CHRG
                            objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                            objCashTO.setAuthorizeRemarks("INSURANCE CHARGES");
                            objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId() + ":" + "INSURANCE CHARGES");
                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
                            objCashTO.setNarration(objCashTransactionTO.getNarration());
                            objCashTO.setInstrumentNo2("LOAN_INSURANCE_CHARGES");
                            cashList.add(objCashTO);
                        }
                        paidInterest = 0;
                        //missleneous
                        objCashTO = setCashTransaction(objCashTransactionTO);
                        double miscellous = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("MISCELLANEOUS CHARGES")).doubleValue();
                        if (transAmt > 0 && miscellous > 0) {
                            if (transAmt >= miscellous) {
                                transAmt -= miscellous;
                                paidInterest = miscellous;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= miscellous;
                            }
                            objCashTO.setAmount(new Double(paidInterest));
                            objCashTO.setActNum("");
                            objCashTO.setProdId("");
                            objCashTO.setProdType(TransactionFactory.GL);
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("MISC_SERV_CHRG")));
                            objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                            objCashTO.setAuthorizeRemarks("MISCELLANEOUS CHARGES");
                            objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId() + ":" + "MISCELLANEOUS CHARGES");
                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
                            objCashTO.setNarration(objCashTransactionTO.getNarration());
                            objCashTO.setInstrumentNo2("LOAN_MISC_SERV_CHRG");
                            cashList.add(objCashTO);
                        }
                        paidInterest = 0;
                        //execution degree
                        objCashTO = setCashTransaction(objCashTransactionTO);
                        double executionDegree = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("EXECUTION DECREE CHARGES")).doubleValue();
                        if (transAmt > 0 && executionDegree > 0) {
                            if (transAmt >= executionDegree) {
                                transAmt -= executionDegree;
                                paidInterest = executionDegree;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= executionDegree;
                            }
                            objCashTO.setAmount(new Double(paidInterest));
                            objCashTO.setActNum("");
                            objCashTO.setProdId("");
                            objCashTO.setProdType(TransactionFactory.GL);
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("EXECUTION_DECREE_CHARGES")));//MISC_SERV_CHRG
                            objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                            objCashTO.setAuthorizeRemarks("EXECUTION DECREE CHARGES");
                            objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId() + ":" + "EXECUTION DECREE CHARGES");
                            objCashTO.setLoanHierarchy("4"); // "4" For Charges
                            objCashTO.setNarration(objCashTransactionTO.getNarration());
                            objCashTO.setInstrumentNo2("LOAN_EXECUTION_DECREE_CHARGES");
                            cashList.add(objCashTO);
                        }
                        paidInterest = 0;

                        //CASE DETAILS Changed By Suresh
                        if (ALL_LOAN_AMOUNT.containsKey("OTHER_CHARGES")) {
                            if (otherChargesMap == null) {
                                otherChargesMap = new HashMap();
                            }
                            otherChargesMap = (HashMap) ALL_LOAN_AMOUNT.get("OTHER_CHARGES");
                            System.out.println("@#$@#otherChargesMap:" + otherChargesMap);
                            Object keys[] = otherChargesMap.keySet().toArray();
                            for (int i = 0; i < otherChargesMap.size(); i++) {
                                objCashTO = setCashTransaction(objCashTransactionTO);
                                double otherCharge = CommonUtil.convertObjToDouble(otherChargesMap.get(keys[i])).doubleValue();
                                System.out.println("$#@$#@$@otherCharge : " + otherCharge + ":" + keys[i]);
                                if (transAmt > 0 && otherCharge > 0) {
                                    if (transAmt >= otherCharge) {
                                        transAmt -= otherCharge;
                                        paidInterest = otherCharge;
                                    } else {
                                        paidInterest = transAmt;
                                        transAmt -= otherCharge;
                                    }
                                    objCashTO.setActNum(objCashTransactionTO.getLinkBatchId());
                                    objCashTO.setInpAmount(new Double(paidInterest));
                                    objCashTO.setAmount(new Double(paidInterest));
                                    objCashTO.setActNum("");
                                    objCashTO.setProdId("");
                                    objCashTO.setProdType(TransactionFactory.GL);
                                    if (CommonUtil.convertObjToStr(keys[i]).equals("NOTICE CHARGES")) {
                                        objCashTO.setAcHdId(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(map.get("NOTICE_CHARGES"))));
                                    } else {
                                        objCashTO.setAcHdId(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(keys[i])));
                                    }
                                    objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                                    objCashTO.setAuthorizeRemarks(CommonUtil.convertObjToStr(keys[i]));
                                    objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId() + ":" + keys[i]);
                                    objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
                                    objCashTO.setNarration(objCashTransactionTO.getNarration());
                                    System.out.println("@#$@#objCashTO:" + objCashTO);
                                    objCashTO.setInstrumentNo2("LOAN_OTHER_CHARGES");
                                    cashList.add(objCashTO);
                                }
                                paidInterest = 0;
                                otherCharge = 0;
                            }
                            continue;
                        }

                    }
                    if (hierachyValue.equals("PENALINTEREST")) {
                        //penal interest
                        double penalInterest = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("PENAL_INT")).doubleValue();
                        if (penalWaiveOff.equals("Y") || waiveOffInterest > 0) {
                            penalWaiveOff(objCashTransactionTO, penalInterest, waiveOffInterest);
                            continue;
                        }
                        objCashTO = setCashTransaction(objCashTransactionTO);
//                double penalInterest=CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("PENAL_INT")).doubleValue();
                        if (transAmt > 0 && penalInterest > 0) {
                            if (transAmt >= penalInterest) {
                                transAmt -= penalInterest;
                                paidInterest = penalInterest;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= penalInterest;

                            }
                            objCashTO.setAmount(new Double(paidInterest));
                            objCashTO.setActNum("");
                            objCashTO.setProdId("");
                            objCashTO.setProdType(TransactionFactory.GL);
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("PENAL_INT")));
                            objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                            objCashTO.setAuthorizeRemarks("PENAL_INT");
                            objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId() + ":" + "PENAL_INT");
                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "3" For Penal Interest
                            objCashTO.setNarration(objCashTransactionTO.getNarration());
                            objCashTO.setInstrumentNo2("LOAN_PENAL_INT");
                            cashList.add(objCashTO);
                            continue;
                        }
                    }
                    if (hierachyValue.equals("INTEREST")) {
                        //interest
                        objCashTO = setCashTransaction(objCashTransactionTO);
                        double interest = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("CURR_MONTH_INT")).doubleValue();
                        if (rebateInterest > 0 && interest > 0) {
                            if (rebateAllowed.equals("Y")) {
                                if (interest >= rebateInterest) {
                                    interest -= rebateInterest;
                                    rebateInterestTransaction(objCashTransactionTO, rebateInterest, 0.0);

                                } else {
                                    rebateInterestTransaction(objCashTransactionTO, rebateInterest, interest);
                                    interest = 0;
                                }

                            }

                        }
                        if (waiveOffInterest > 0 && interest > 0) {
                            if (interest >= waiveOffInterest) {
                                interest -= waiveOffInterest;
                                // penalWaiveOff(objCashTransactionTO,waiveOffInterest);
                                // continue;
                            }

                        }


                        if (transAmt > 0 && interest > 0) {
                            if (transAmt >= interest) {
                                transAmt -= interest;
                                paidInterest = interest;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= interest;

                            }
                            objCashTO.setAmount(new Double(paidInterest));
                            objCashTO.setActNum("");
                            objCashTO.setProdId("");
                            objCashTO.setProdType(TransactionFactory.GL);
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("AC_DEBIT_INT")));
                            objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                            objCashTO.setAuthorizeRemarks("INTEREST");
                            objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId() + ":" + "INTEREST");
                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "2" For Penal Interest
                            objCashTO.setNarration(objCashTransactionTO.getNarration());
                            objCashTO.setInstrumentNo2("LOAN_INTEREST");
                            cashList.add(objCashTO);
                            continue;
                        }
                    }
                    if (hierachyValue.equals("PRINCIPAL")) {

                        double principal = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("CURR_MONTH_PRINCEPLE")).doubleValue();
                        //principal
                        if (transAmt > 0.0 && principal > 0) {
                            if (transAmt >= principal) {
                                transAmt -= principal;
                                paidprincipal = principal;
                            } else {
                                paidprincipal = transAmt;
                                transAmt -= principal;

                            }
                            objCashTO = setCashTransaction(objCashTransactionTO);
                            objCashTO.setAmount(new Double(paidprincipal));
                            objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                            //                    if(no_of_installment>0)
                            objCashTO.setAuthorizeRemarks(String.valueOf(no_of_installment));
                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "2" For Principal
                            objCashTO.setNarration(objCashTransactionTO.getNarration());
                            objCashTO.setInstrumentNo2("LOAN_PRINCIPAL");
                            cashList.add(objCashTO);
                            continue;
                        }
                    }
                }
                if (transAmt > 0.0) {
                    for (int i = 0; i < cashList.size(); i++) {
                        CashTransactionTO obj = (CashTransactionTO) cashList.get(i);
                        if (obj.getProdType().equals("TL")) {
                            double principal = obj.getAmount().doubleValue();
                            principal += transAmt;
                            obj.setAmount(new Double(principal));
                            cashList.set(i, obj);
                            isTLAvailable = true;
                            break;
                        }

                    }
                    if (!isTLAvailable) {
                        objCashTO = setCashTransaction(objCashTransactionTO);
                        objCashTO.setAmount(new Double(transAmt));
                        objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                        //                    if(no_of_installment>0)
                        objCashTO.setAuthorizeRemarks(String.valueOf(no_of_installment));
                        objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "2" For Principal
                        objCashTO.setNarration(objCashTransactionTO.getNarration());
                        objCashTO.setInstrumentNo2("LOAN_PRINCIPAL");
                        cashList.add(objCashTO);
                    }
                }
                lstachd = null;
                objCashTO = null;
            }
        }
        //            else{
        //                final CashTransactionTO objCashTransactionTO = setCashTransaction();
        //                cashList.add(objCashTransactionTO);
        //            }
        map = null;
        ALL_LOAN_AMOUNT = null;
        objCashTransactionTO = null;
        System.out.println("cashlist####" + cashList);
        return cashList;
    }

    public CashTransactionTO setCashTransaction(CashTransactionTO cashTo) {
        log.info("In setCashTransaction()");
        Date curDate = (Date) currDt.clone();
        final CashTransactionTO objCashTransactionTO = new CashTransactionTO();
        try {
            objCashTransactionTO.setTransId(cashTo.getTransId());
            objCashTransactionTO.setAcHdId(cashTo.getAcHdId());
            objCashTransactionTO.setProdId(cashTo.getProdId());
            objCashTransactionTO.setProdType(cashTo.getProdType());
            objCashTransactionTO.setActNum(cashTo.getActNum());
            objCashTransactionTO.setInpAmount(cashTo.getInpAmount());
            objCashTransactionTO.setInpCurr(cashTo.getInpCurr());
            objCashTransactionTO.setAmount(cashTo.getAmount());
            objCashTransactionTO.setTransType(cashTo.getTransType());
            objCashTransactionTO.setInstType(cashTo.getInstType());
            objCashTransactionTO.setBranchId(cashTo.getBranchId());
            objCashTransactionTO.setStatusBy(cashTo.getStatusBy());
            objCashTransactionTO.setInstrumentNo1(cashTo.getInstrumentNo1());
            objCashTransactionTO.setInstrumentNo2(cashTo.getInstrumentNo2());
            Date InsDt = cashTo.getInstDt();
            if (InsDt != null) {
                Date insDate = (Date) curDate.clone();
                insDate.setDate(InsDt.getDate());
                insDate.setMonth(InsDt.getMonth());
                insDate.setYear(InsDt.getYear());
                //            objCashTransactionTO.setInstDt(DateUtil.getDateMMDDYYYY(getTdtInstrumentDate()));
                objCashTransactionTO.setInstDt(insDate);
            } else {
                objCashTransactionTO.setInstDt(cashTo.getInstDt());
            }
            objCashTransactionTO.setTokenNo(cashTo.getTokenNo());
            objCashTransactionTO.setInitTransId(cashTo.getInitTransId());
            objCashTransactionTO.setInitChannType(cashTo.getInitChannType());
            objCashTransactionTO.setParticulars(cashTo.getParticulars());
            objCashTransactionTO.setInitiatedBranch(cashTo.getInitiatedBranch());
            objCashTransactionTO.setCommand(cashTo.getCommand());
            objCashTransactionTO.setAuthorizeStatus_2(cashTo.getAuthorizeStatus_2());
            System.out.println("objCashTransactionTO:" + objCashTransactionTO);
        } catch (Exception e) {
            log.info("Error In setInwardClearing()");
            e.printStackTrace();
        }
        return objCashTransactionTO;
    }

    private void penalWaiveOff(CashTransactionTO obj, double waivePenalAmt, double waiveInterestAmt) throws Exception {
        TermLoanPenalWaiveOffTO objpenalWaive = new TermLoanPenalWaiveOffTO();
        interestWaiveoffTransaction(obj, waivePenalAmt, waiveInterestAmt);
        if (obj != null) {
            objpenalWaive.setAcctNum(obj.getLinkBatchId());
            objpenalWaive.setWaiveDt((Date) currDt.clone());
            objpenalWaive.setInterestAmt(new Double(waiveInterestAmt));
            objpenalWaive.setPenalAmt(new Double(waivePenalAmt));
            objpenalWaive.setStatus(CommonConstants.STATUS_CREATED);
            objpenalWaive.setStatusBy(obj.getStatusBy());
            objpenalWaive.setStatusDt((Date) currDt.clone());
            objpenalWaive.setWaiveOffId(CommonUtil.convertObjToStr(generateWaiveOffBatchID()));
            sqlMap.executeUpdate("insertTermLoanInterestWaiveOffTO", objpenalWaive);


        }

    }

    private String generateWaiveOffBatchID() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "LOAN.WAIVE_OFF_ID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//for trans_batch_id resetting, branch code
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        String batchID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        return batchID;
    }

    private boolean isAuthorizeUserList(HashMap daoMap) throws Exception {
        boolean authorize = false;
        HashMap map = (HashMap) daoMap.get(CommonConstants.AUTHORIZEMAP);

        // Multi User Authorization
        if (map.containsKey(CommonConstants.AUTHORIZEUSERLIST)) {
            MultiAuthorizeDAO objMultiAuthorizeDAO = new MultiAuthorizeDAO();
            map.put(CommonConstants.TRANSACTION_TYPE, String.valueOf(CommonConstants.CASH));
            //System.out.println ("MultiAuthorizieMap-cash" + daoMap);
            objMultiAuthorizeDAO.execute(daoMap);
        } else { // Single authorization
            authorize = true;
        }
        return authorize;
    }

    private void insertDenomination(HashMap map, CashTransactionTO objCashTransactionTOIns) throws Exception {
        if (map.containsKey("DENOMINATION_LIST") && map.get("DENOMINATION_LIST") != null) {
            ArrayList denoList = (ArrayList) map.get("DENOMINATION_LIST");
            AuditEntryTO objCashDenominationTO = null;
            HashMap denoMap = null;
            for (int i = 0, j = denoList.size(); i < j; i++) {
                denoMap = (HashMap) denoList.get(i);
                objCashDenominationTO = new AuditEntryTO();
                objCashDenominationTO.setTransId(objCashTransactionTOIns.getTransId());
//                objCashDenominationTO.setDenominationCount(CommonUtil.convertObjToDouble(denoMap.get("COUNT")));
//                objCashDenominationTO.setDenominationValue(CommonUtil.convertObjToDouble(denoMap.get("DENOMINATION")));
//                objCashDenominationTO.setTransType(objCashTransactionTOIns.getTransType());
//                objCashDenominationTO.setCurrency(CommonUtil.convertObjToStr(denoMap.get("CURRENCY")));
//                objCashDenominationTO.setDenominationType(CommonUtil.convertObjToStr(denoMap.get("DENOMINATION_TYPE")));
                objCashDenominationTO.setStatus(CommonConstants.STATUS_CREATED);
                sqlMap.executeUpdate("insertCashDenominationTO", objCashDenominationTO);
            }
        }
    }

    private void deleteDenomination(CashTransactionTO objCashTransactionTO) throws Exception {
        HashMap whereMap = new HashMap();
        whereMap.put("STATUS", CommonConstants.STATUS_DELETED);
        whereMap.put(CommonConstants.TRANS_ID, objCashTransactionTO.getTransId());
        sqlMap.executeUpdate("deleteCashDenominationTO", whereMap);
    }

    private String getAdviceNo() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "ADVICE_NO");
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void insertData(HashMap hmap) {
        ArrayList list = null;
        list = (ArrayList) hmap.get("ORG_RESP_DETAILS");
        if (list != null && list.size() > 0) {
            try {
                System.out.println("hmap" + hmap);

                hmap = (HashMap) list.get(0);
                if (CommonUtil.convertObjToStr(hmap.get("OrgOrRespTransType")).equals("O")) {
                    hmap.put("ADVICE_NO", getAdviceNo());
                }
                System.out.println("hmap" + hmap);
                hmap.put("TRAN_ID", transID);
                hmap.put("TRAN_DT", currDt);
                hmap.put("STATUS", "CREATED");
                hmap.put("STATUS_DT", currDt);
                hmap.put("STATUS_BY", user);
                hmap.put("BRANCH", _branchCode);
                System.out.println("hmap");
                sqlMap.executeUpdate("insertOrgOrResp", hmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void insertData(CashTransactionTO objCashTransactionTOIns) throws Exception {
        HashMap map = new HashMap();
        //        HashMap interestMap = (HashMap)map.get("INTEREST_MAP");
        System.out.println("####Insert Data interestMap :" + interestMap + "map :" + map);
        transID = "";
        System.out.print("#### firstRecord " + firstRecord);
        System.out.print("#### depTransId.length() " + depTransId.length());
        if (firstRecord == 0 && depTransId.length() < 1) {
            transID = getTransID();
            depTransId = transID;
        } else {
            transID = depTransId;
        }

        if (depIntMap != null && depIntMap.containsKey("DEPOSIT_INTEREST")) {
            depIntMap.put("AMOUNT", objCashTransactionTOIns.getAmount());
            depIntMap.put(PROD_ID, objCashTransactionTOIns.getProdId());
            depIntMap.put("TRANS_TYPE", objCashTransactionTOIns.getTransType());
            depIntMap.put(DEPOSIT_NO, objCashTransactionTOIns.getActNum());
            depIntMap = depositInterestTransfer(depIntMap);
            System.out.println("#####inside depIntMap :" + depIntMap);
            objCashTransactionTOIns.setAcHdId(CommonUtil.convertObjToStr(depIntMap.get("INT_PAY")));
            objCashTransactionTOIns.setActNum("");
            objCashTransactionTOIns.setProdId("");
            objCashTransactionTOIns.setProdType(TransactionFactory.GL);
            objCashTransactionTOIns.setAmount(CommonUtil.convertObjToDouble(depIntMap.get("BALANCE_AMT")));
            objCashTransactionTOIns.setLinkBatchId(CommonUtil.convertObjToStr(depIntMap.get(DEPOSIT_NO)));
            objCashTransactionTOIns.setBranchId(CommonUtil.convertObjToStr(depIntMap.get(CommonConstants.BRANCH_ID)));
            objCashTransactionTOIns.setInstrumentNo1("INTEREST_AMT");
            //            objCashTransactionTOIns.setInstrumentNo2("DEPOSIT_RENEWAL");
            System.out.println("#####inside depIntMap :" + objCashTransactionTOIns);
        }
        if (interestMap != null && objCashTransactionTOIns.getProdType().equals(TransactionFactory.DEPOSITS)) {
            depIntMap = interestTransferFD(interestMap);
            System.out.println("#####inside interestMap :" + depIntMap);
            objCashTransactionTOIns.setAcHdId(CommonUtil.convertObjToStr(depIntMap.get("INT_PAY")));
            objCashTransactionTOIns.setActNum("");
            objCashTransactionTOIns.setProdId("");
            objCashTransactionTOIns.setProdType(TransactionFactory.GL);
            objCashTransactionTOIns.setAmount(CommonUtil.convertObjToDouble(depIntMap.get("BALANCE_AMT")));
            objCashTransactionTOIns.setLinkBatchId(CommonUtil.convertObjToStr(depIntMap.get(DEPOSIT_NO)));
            objCashTransactionTOIns.setBranchId(CommonUtil.convertObjToStr(depIntMap.get(CommonConstants.BRANCH_ID)));
            System.out.println("#####inside interestMap :" + objCashTransactionTOIns);
        }
        if (!objCashTransactionTOIns.getProdType().equals("") && objCashTransactionTOIns.getProdType().equals(TransactionFactory.OPERATIVE)
                && objCashTransactionTOIns.getActNum() != null && flexiDeletionMap != null && flexiDeletionMap.containsKey("FLEXI_LIEN_CREATION")) {
            HashMap flexiMap = new HashMap();
            depositLienDAO = new DepositLienDAO();
            depositLienTO = new DepositLienTO();
            ArrayList lienTOs = new ArrayList();
            flexiMap.put("ACCOUNTNO", objCashTransactionTOIns.getActNum());
            List getList = sqlMap.executeQueryForList("getFlexiDetails", flexiMap);
            if (getList != null && getList.size() > 0) {
                System.out.println("getList : " + getList);
                flexiMap = (HashMap) getList.get(0);
                HashMap eachLienMap = new HashMap();
                double depAmt = 0.0;
                eachLienMap.put("FLEXI_ACT_NUM", objCashTransactionTOIns.getActNum());
                List lstEachLien = sqlMap.executeQueryForList("getSelectSumOfEachDepAmount", eachLienMap);
                if (lstEachLien != null && lstEachLien.size() > 0) {
                    for (int i = 0; i < lstEachLien.size(); i++) {
                        eachLienMap = (HashMap) lstEachLien.get(i);
                        depAmt = CommonUtil.convertObjToDouble(eachLienMap.get("TOTAL_BALANCE")).doubleValue();
                        System.out.println("depAmt :" + depAmt + "flexiAmount :" + flexiAmount);
                        if (flexiAmount >= depAmt) {
                            depAmt = depAmt;
                            System.out.println("if depAmt :" + depAmt + "if flexiAmount :" + flexiAmount);
                        } else {
                            depAmt = flexiAmount;
                            System.out.println("else depAmt :" + depAmt + "else flexiAmount :" + flexiAmount);
                        }
                        if (depAmt > 0) {
                            depositLienTO.setLienNo("-");
                            depositLienTO.setDepositNo(CommonUtil.convertObjToStr(eachLienMap.get(DEPOSIT_NO)));
                            depositLienTO.setDepositSubNo(CommonUtil.convertObjToInt("1"));
                            depositLienTO.setLienDt(currDt);
                            depositLienTO.setStatus(CommonConstants.STATUS_CREATED);
                            depositLienTO.setLienAcHd(CommonUtil.convertObjToStr(objCashTransactionTOIns.getAcHdId()));
                            depositLienTO.setLienAcNo(CommonUtil.convertObjToStr(objCashTransactionTOIns.getActNum()));
                            depositLienTO.setRemarks(transID);
                            depositLienTO.setStatusBy(CommonUtil.convertObjToStr(objCashTransactionTOIns.getStatusBy()));
                            depositLienTO.setLienAmount(new Double(depAmt));
                            depositLienTO.setLienProdId(CommonUtil.convertObjToStr(objCashTransactionTOIns.getProdId()));
                            depositLienTO.setStatusDt(currDt);
                            depositLienTO.setCreditLienAcct("NA");

                            depositLienTO.setUnlienRemarks("FLEXI_DEPOSITS");
                            lienTOs.add(depositLienTO);
                            System.out.println("lienTOs: " + lienTOs);
                            map.put(CommonConstants.BRANCH_ID, objCashTransactionTOIns.getBranchId());
                            map.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                            map.put("lienTOs", lienTOs);
                            map.put(SHADOWLIEN, new Double(depAmt));
                            map.put("LIENAMOUNT", String.valueOf(depAmt));
                            map.put("USER_ID", objCashTransactionTOIns.getStatusBy());
                            map.put(CommonConstants.BRANCH_ID, _branchCode);
                            System.out.println("map : " + map);
                            depositLienDAO.setCallFromOtherDAO(true);
                            HashMap lienCreatedMap = depositLienDAO.execute(map);
                            List selected = (ArrayList) lienCreatedMap.get("LIENNO");
                            System.out.println("selected : " + selected);
                            flexiAmount = flexiAmount - depAmt;
                            if (flexiAmount < 0) {
                                break;
                            }
                        }
                    }
                }
            }
            depositLienDAO = null;
            depositLienTO = null;
            lienTOs = null;
            flexiMap = null;
            flexiAmount = 0.0;
        }
        objCashTransactionTOIns.setTransId(transID);
        objCashTransactionTOIns.setStatus(CommonConstants.STATUS_CREATED);
        System.out.print("CASHTRANSACTIONDAO######insert" + objCashTransactionTOIns);
        transModuleBased = TransactionFactory.createTransaction(objCashTransactionTOIns.getProdType());
        transModuleBased.validateRules(getRuleMap(objCashTransactionTOIns, 0.0, true), isException);


        //        if (prodType.equals(TransactionFactory.OPERATIVE))
        //            errorList = transModuleBased.getErrorList();
        if (isException) {
            objCashTransactionTOIns.setAuthorizeStatus(CommonConstants.STATUS_EXCEPTION);
        }

        transModuleBased.performShadowAdd(getRuleMap(objCashTransactionTOIns, 0.0, true));
        //transModuleBased.performOtherBalanceAdd(getRuleMap(objCashTransactionTOIns, 0.0, true));

        objCashTransactionTOIns.setStatusDt(currDt);
        objCashTransactionTOIns.setTransDt(currDt);
        if (shift.equals("T")) {
            objCashTransactionTOIns.setShift(shifttime);
        } else {
            objCashTransactionTOIns.setShift("");
        }

        //        if (!(objCashTransactionTOIns.getParticulars()!=null && objCashTransactionTOIns.getParticulars().length()>0)) {
        //            if(objCashTransactionTOIns.getTransType().equals(TransactionDAOConstants.CREDIT))
        //                objCashTransactionTOIns.setParticulars("By Cash");
        //            else
        //                 objCashTransactionTOIns.setParticulars("To Self A/c Closure");
        //        }
        if (objCashTransactionTOIns.getAcHdId() == null || objCashTransactionTOIns.getAcHdId().length() <= 0) {
            throw new TTException(objCashTransactionTOIns.getTransType() + " Account Head not set...\nAmount : "
                    + objCashTransactionTOIns.getAmount().toString() + " (" + objCashTransactionTOIns.getParticulars() + ")");
        }
        if (objCashTransactionTOIns.getAmount() == null || objCashTransactionTOIns.getAmount().doubleValue() <= 0) {
            throw new TTException(objCashTransactionTOIns.getTransType() + " Transaction Amount not set...\nAmount : "
                    + objCashTransactionTOIns.getAmount().toString() + " (" + objCashTransactionTOIns.getParticulars() + ")");
        }
        objCashTransactionTOIns.setParticulars(objCashTransactionTOIns.getParticulars());
        sqlMap.executeUpdate("insertCashTransactionTO", objCashTransactionTOIns);
        if (isException) {
            HashMap exceptionmap = new HashMap();
            exceptionmap = exceptionMap(objCashTransactionTOIns);

            //            exceptionmap.put("")
            exceptionmap.put("BRANCH_ID", _branchCode);
            System.out.println("exceptionmap###" + exceptionmap);
            sqlMap.executeUpdate("insertExceptionTrans", exceptionmap);
            exceptionmap = new HashMap();

        }
        //FOR COLLECTING PROCESSING CHARGE
        //        if(objCashTransactionTOIns.getProdType().equals(TransactionFactory.LOANS)){
        //            if(OAmap.containsKey("PROCCHARGEMAP")) {
        //                HashMap hash=new HashMap();
        //                hash=(HashMap)OAmap.get("PROCCHARGEMAP");
        //                boolean flag=new Boolean(CommonUtil.convertObjToStr(hash.get("FLAG_STATUS"))).booleanValue();
        //
        //                if(flag){
        //                    updateProcessingChargesFromLoan(hash);
        //                }
        //                else{
        //                    updateProcessingChargesFormOperative(hash);
        //                }
        //            }
        //                }
        // To apply TDS

        if (objCashTransactionTOIns.getProdType().equals(TransactionFactory.DEPOSITS) && objCashTransactionTOIns.getActNum() != null) {
            HashMap dailyMap = new HashMap();
            dailyMap.put(PROD_ID, objCashTransactionTOIns.getProdId());
            List lst = sqlMap.executeQueryForList("getBehavesLikeForDeposit", dailyMap);
            if (lst.size() > 0) {
                dailyMap = (HashMap) lst.get(0);
                if (!dailyMap.get(BEHAVES_LIKE).equals("DAILY") || dailyMap.get(BEHAVES_LIKE).equals("RECURRING")) {
                    if (!dailyMap.get(BEHAVES_LIKE).equals("RECURRING")) {
                        System.out.println("deposit_NodataMap" + objCashTransactionTOIns.getActNum());
                        HashMap tdsCalc = new HashMap();
                        tdsCalc.put(DEPOSIT_NO, objCashTransactionTOIns.getActNum());
                        System.out.println("deposit_NotdsCalcMap" + tdsCalc.get(DEPOSIT_NO));
                        String deposit_No = CommonUtil.convertObjToStr(tdsCalc.get(DEPOSIT_NO));
                        deposit_No = deposit_No.substring(0, deposit_No.lastIndexOf("_"));
                        System.out.println("depositNo " + deposit_No);

                        tdsCalc.put(DEPOSIT_NO, deposit_No);
                        List list = sqlMap.executeQueryForList("getCustNoforTDS", tdsCalc);
                        tdsCalc = new HashMap();
                        if (list.size() > 0) {
                            System.out.println("depositNoforexecutequery " + deposit_No);
                        }
                        tdsCalc = (HashMap) list.get(0);
                        tdsCalc.put("CUST_ID", tdsCalc.get("CUST_ID"));
                        tdsCalc.put(PROD_ID, objCashTransactionTOIns.getProdId());
                        tdsCalc.put("PROD_TYPE", objCashTransactionTOIns.getProdType());
                        System.out.println("tdsCalcCUSTOMER " + tdsCalc);
                    }
                }
            }
        }
    }

    private String getTransID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "CASH_TRANS_ID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//for trans_batch_id resetting, branch code
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private HashMap exceptionMap(CashTransactionTO objCashTransactionTORuleMap) {
        HashMap exceptionmap = new HashMap();
        exceptionmap.put(BATCH_ID, "");
        exceptionmap.put(CommonConstants.TRANS_ID, objCashTransactionTORuleMap.getTransId());
        exceptionmap.put(CommonConstants.ACT_NUM, objCashTransactionTORuleMap.getActNum());
        return exceptionmap;

    }

    private HashMap getRuleMap(CashTransactionTO objCashTransactionTORuleMap, double prevAmount, boolean makeNegative) throws Exception {
        HashMap inputMap = new HashMap();

        System.out.println("objCashTransactionTORuleMap:" + objCashTransactionTORuleMap);

        double amount = objCashTransactionTORuleMap.getAmount().doubleValue();
        String acctNo = objCashTransactionTORuleMap.getActNum();

        if (objCashTransactionTORuleMap.getTransType().equals(TransactionDAOConstants.DEBIT) && makeNegative) {
            amount = -amount + prevAmount;
        } else {
            amount -= prevAmount;
        }

        if (objCashTransactionTORuleMap.getProdType().equals(TransactionFactory.GL)) {
            objCashTransactionTORuleMap.setActNum(null);  // This condition added to prevent actnum entries in GL
        }

        if ((acctNo == null || acctNo.equals("")) && (objCashTransactionTORuleMap.getAcHdId() != null)) {
            acctNo = objCashTransactionTORuleMap.getAcHdId();
        }

        inputMap.put(TransactionDAOConstants.ACCT_NO, acctNo);
        inputMap.put(TransactionDAOConstants.AMT, CommonUtil.convertObjToDouble(String.valueOf(amount)));
        inputMap.put(TransactionDAOConstants.INSTRUMENT_TYPE, objCashTransactionTORuleMap.getInstType());
        inputMap.put(TransactionDAOConstants.INSTRUMENT_1, CommonUtil.convertObjToStr(objCashTransactionTORuleMap.getInstrumentNo1()));
        inputMap.put(TransactionDAOConstants.INSTRUMENT_2, CommonUtil.convertObjToStr(objCashTransactionTORuleMap.getInstrumentNo2()));
        inputMap.put(TransactionDAOConstants.DATE, objCashTransactionTORuleMap.getInstDt());
        inputMap.put(TransactionDAOConstants.TRANS_TYPE, objCashTransactionTORuleMap.getTransType());
        inputMap.put(TransactionDAOConstants.BRANCH_CODE, objCashTransactionTORuleMap.getBranchId());
        inputMap.put(TransactionDAOConstants.INITIATED_BRANCH, objCashTransactionTORuleMap.getInitiatedBranch());
        inputMap.put(TransactionDAOConstants.TO_STATUS, objCashTransactionTORuleMap.getStatus());
        inputMap.put(TransactionDAOConstants.AUTHORIZE_BY, objCashTransactionTORuleMap.getAuthorizeBy());
        if (status != null) {
            inputMap.put(TransactionDAOConstants.AUTHORIZE_STATUS, status);
        } else {
            inputMap.put(TransactionDAOConstants.AUTHORIZE_STATUS, objCashTransactionTORuleMap.getAuthorizeStatus());
        }
        inputMap.put(TransactionDAOConstants.TRANS_ID, objCashTransactionTORuleMap.getTransId());
        inputMap.put(TransactionDAOConstants.TODAY_DT, currDt);
        inputMap.put(TransactionDAOConstants.TRANS_MODE, TransactionDAOConstants.CASH);
        inputMap.put("ACTUAL_AMT", objCashTransactionTORuleMap.getAmount()); // Added by Rajesh for LimitCheckingRule. No need to deduct the previous amount for Operative. Because the system should check the actual amount with Minimum Balance (on product level)
        if (loan_debit_type != null && loan_debit_type.length() > 0) {
            inputMap.put("DEBIT_LOAN_TYPE", loan_debit_type);
        }
        if (objCashTransactionTORuleMap.getProdType().equals(TransactionFactory.LOANS) || objCashTransactionTORuleMap.getProdType().equals(TransactionFactory.ADVANCES) || objCashTransactionTORuleMap.getProdType().equals("ATL")) {
            inputMap.put(PROD_ID, objCashTransactionTORuleMap.getProdId());
            inputMap.put("PROD_TYPE", objCashTransactionTORuleMap.getProdType());
        }

        if (objCashTransactionTORuleMap.getParticulars() != null) {
            inputMap.put(TransactionDAOConstants.PARTICULARS, objCashTransactionTORuleMap.getParticulars());
        }
        if (account_closing.equals("ACCOUNT_CLOSING")) {
            inputMap.put("ACCOUNT_CLOSING", "ACCOUNT_CLOSING");
        }
        if (act_closing_min_bal_check != null && act_closing_min_bal_check.equalsIgnoreCase("ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER")) {
            inputMap.put("ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER", "ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER");
        }
        // For Corporate Loan purpose added by Rajesh
        if (corpLoanMap != null && corpLoanMap.size() > 0) {
            inputMap.putAll(corpLoanMap);
        }
        //System.out.println ("inputMap:"+inputMap);
        return inputMap;
    }

    private void deleteData(HashMap map, CashTransactionTO objCashTransactionTO) {
        ArrayList list = (ArrayList) map.get("ORG_RESP_DETAILS");
        if (list != null && list.size() > 0) {
            try {
                map = (HashMap) list.get(0);
                map.put("STATUS", "DELETED");
                map.put("STATUS_DT", currDt);
                map.put("STATUS_BY", user);
                map.put("BRANCH_ID", _branchCode);
                map.put("TRANS_ID", objCashTransactionTO.getTransId());
                sqlMap.executeUpdate("deleteOrgRespDetails", map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateData(HashMap map, CashTransactionTO objCashTransactionTO) {
        ArrayList list = (ArrayList) map.get("ORG_RESP_DETAILS");
        if (list != null && list.size() > 0) {
            try {
                map = (HashMap) list.get(0);
                map.put("STATUS", "MODIFIED");
                map.put("STATUS_DT", currDt);
                map.put("STATUS_BY", user);
                map.put("BRANCH_ID", _branchCode);
                map.put("TRANS_ID", objCashTransactionTO.getTransId());
                sqlMap.executeUpdate("updateOrgRespDetails", map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateData(CashTransactionTO objCashTransactionTOUpd, double prevAmount) throws Exception {
        objCashTransactionTOUpd.setStatus(CommonConstants.STATUS_MODIFIED);

        // ******** important ****** Don't give prevAmount in getRuleMap, the amount should be 0
        transModuleBased.validateRules(getRuleMap(objCashTransactionTOUpd, 0, true), isException);
        //        if (objCashTransactionTOUpd.getProdType().equals(TransactionFactory.OPERATIVE))
        //            errorList = transModuleBased.getErrorList();

        if (isException) {
            objCashTransactionTOUpd.setAuthorizeStatus(CommonConstants.STATUS_EXCEPTION);
        }

        // ******** important ****** Don't give 0 in getRuleMap, the amount should be prevAmount
        transModuleBased.performShadowAdd(getRuleMap(objCashTransactionTOUpd, prevAmount, true));
        //transModuleBased.performOtherBalanceAdd(getRuleMap(objCashTransactionTOUpd, prevAmount, true));

        objCashTransactionTOUpd.setStatusDt(currDt);
        System.out.println("objCashTransactionTOUpd" + objCashTransactionTOUpd);
        HashMap dailyMap = new HashMap();
        // if(!objCashTransactionTOUpd.getProdId().equals("")){
        String prodType = (String) objCashTransactionTOUpd.getProdType();

        if (prodType.equals(TransactionFactory.DEPOSITS) && objCashTransactionTOUpd.getActNum() != null) {
            dailyMap.put(PROD_ID, objCashTransactionTOUpd.getProdId());
            List lst = sqlMap.executeQueryForList("getBehavesLikeForDeposit", dailyMap);
            if (lst.size() > 0) {
                dailyMap = (HashMap) lst.get(0);
                if (dailyMap.get(BEHAVES_LIKE).equals("DAILY")) {
                    isDaily = true;
                }
            }
            if (isDaily) {
                HashMap dailyUpdateMap = new HashMap();
                dailyUpdateMap.put(CommonConstants.TRANS_ID, objCashTransactionTOUpd.getTransId());
                dailyUpdateMap.put("AC_HD_ID", objCashTransactionTOUpd.getAcHdId());
                dailyUpdateMap.put(CommonConstants.ACT_NUM, objCashTransactionTOUpd.getActNum());
                dailyUpdateMap.put("INP_AMOUNT", objCashTransactionTOUpd.getInpAmount());
                dailyUpdateMap.put("INP_CURR", objCashTransactionTOUpd.getInpCurr());
                dailyUpdateMap.put("AMOUNT", objCashTransactionTOUpd.getAmount());
                dailyUpdateMap.put("TRANS_TYPE", objCashTransactionTOUpd.getTransType());
                dailyUpdateMap.put("INST_TYPE", objCashTransactionTOUpd.getInstType());
                dailyUpdateMap.put("INST_DT", objCashTransactionTOUpd.getInstDt());
                dailyUpdateMap.put("TOKEN_NO", objCashTransactionTOUpd.getTokenNo());
                dailyUpdateMap.put("INIT_TRANS_ID", objCashTransactionTOUpd.getInitTransId());
                dailyUpdateMap.put("INIT_CHANN_TYPE", objCashTransactionTOUpd.getInitChannType());
                dailyUpdateMap.put("PARTICULARS", objCashTransactionTOUpd.getParticulars());
                dailyUpdateMap.put("STATUS", objCashTransactionTOUpd.getStatus());
                dailyUpdateMap.put("INSTRUMENT_NO1", objCashTransactionTOUpd.getInstrumentNo1());
                dailyUpdateMap.put("INSTRUMENT_NO2", objCashTransactionTOUpd.getInstrumentNo2());
                dailyUpdateMap.put("AVAILABLE_BALANCE", objCashTransactionTOUpd.getAvailableBalance());
                dailyUpdateMap.put(PROD_ID, objCashTransactionTOUpd.getProdId());
                dailyUpdateMap.put("PROD_TYPE", objCashTransactionTOUpd.getProdType());
                dailyUpdateMap.put("STATUS_BY", objCashTransactionTOUpd.getStatusBy());
                dailyUpdateMap.put(AUTHORIZE_STATUS, objCashTransactionTOUpd.getAuthorizeStatus());
                dailyUpdateMap.put(LINK_BATCH_ID, objCashTransactionTOUpd.getLinkBatchId());
                dailyUpdateMap.put("LINK_BATCH_DT", objCashTransactionTOUpd.getLinkBatchDt());
                dailyUpdateMap.put("TRANS_DT", objCashTransactionTOUpd.getTransDt());
                dailyUpdateMap.put("INITIATED_BRANCH", objCashTransactionTOUpd.getInitiatedBranch());
                sqlMap.executeUpdate("updateCashTransactionTOForDaily", dailyUpdateMap);
                dailyUpdateMap = null;
                isDaily = false;
            }
        } else if (objCashTransactionTOUpd.getInstrumentNo2() != null && !objCashTransactionTOUpd.getInstrumentNo2().equals("")
                && objCashTransactionTOUpd.getInstrumentNo2().length() > 0
                && objCashTransactionTOUpd.getInstrumentNo2().equals("INPUT_AMOUNT")) {
            objCashTransactionTOUpd.setInitiatedBranch(_branchCode);
            objCashTransactionTOUpd.setTransDt(currDt);
            sqlMap.executeUpdate("updateAmountChargesCashTransactionTO", objCashTransactionTOUpd);
        } else if (objCashTransactionTOUpd.getInstrumentNo2() != null && !objCashTransactionTOUpd.getInstrumentNo2().equals("")
                && objCashTransactionTOUpd.getInstrumentNo2().length() > 0 && objCashTransactionTOUpd.getInstrumentNo2().equals("SERVICE_TAX")) {
            objCashTransactionTOUpd.setInitiatedBranch(_branchCode);
            objCashTransactionTOUpd.setTransDt(currDt);
            sqlMap.executeUpdate("updateAmountChargesCashTransactionTO", objCashTransactionTOUpd);
        } else {
            objCashTransactionTOUpd.setInitiatedBranch(_branchCode);
            objCashTransactionTOUpd.setTransDt(currDt);
            sqlMap.executeUpdate("updateCashTransactionTO", objCashTransactionTOUpd);
        }
    }

    private void deleteData(CashTransactionTO objCashTransactionTODel, double prevAmount) throws Exception {
        objCashTransactionTODel.setStatus(CommonConstants.STATUS_DELETED);

        transModuleBased.performShadowMinus(getRuleMap(objCashTransactionTODel, 0.0, false));
        //transModuleBased.performOtherBalanceMinus(getRuleMap(objCashTransactionTODel, 0.0, true));

        objCashTransactionTODel.setStatusDt(currDt);
        if (objCashTransactionTODel.getProdId() != null && objCashTransactionTODel.getProdId().length() > 0) {
            HashMap dailydel = new HashMap();
            dailydel.put(PROD_ID, objCashTransactionTODel.getProdId());
            List lstDel = sqlMap.executeQueryForList("getBehavesLikeForDeposit", dailydel);
            if (lstDel.size() > 0) {
                dailydel = (HashMap) lstDel.get(0);
                if (dailydel.get(BEHAVES_LIKE).equals("DAILY")) {
                    isDaily = true;
                }
            }
        }
        if (!objCashTransactionTODel.getTransId().equals("") && objCashTransactionTODel.getTransId().length() > 0
                && objCashTransactionTODel.getActNum() != null && objCashTransactionTODel.getTransType().equals(TransactionDAOConstants.DEBIT)) {
            HashMap cashMap = new HashMap();
            cashMap.put(BATCH_ID, objCashTransactionTODel.getTransId());
            System.out.println("cashMap " + cashMap);
            List lstLien = sqlMap.executeQueryForList("getSBLienTransferAccountNo", cashMap);
            if (lstLien != null && lstLien.size() > 0) {
                for (int i = 0; i < lstLien.size(); i++) {
                    cashMap = (HashMap) lstLien.get(i);
                    System.out.println("cashMap " + cashMap);
                    HashMap flexiMap = new HashMap();
                    String lienNo = CommonUtil.convertObjToStr(cashMap.get("LIEN_NO"));
                    flexiMap.put("DEPOSIT_ACT_NUM", cashMap.get(DEPOSIT_NO));
                    flexiMap.put(SHADOWLIEN, new Double(CommonUtil.convertObjToDouble(cashMap.get(LIEN_AMOUNT)).doubleValue()));
                    flexiMap.put("COMMAND", CommonConstants.STATUS_DELETED);
                    flexiMap.put("STATUS", CommonConstants.STATUS_DELETED);
                    flexiMap.put("USER_ID", objCashTransactionTODel.getStatusBy());
                    flexiMap.put("LIEN_AC_NO", objCashTransactionTODel.getActNum());
                    flexiMap.put(AUTHORIZE_STATUS, CommonConstants.STATUS_REJECTED);
                    flexiMap.put("AUTHORIZE_DATE", currDt);
                    flexiMap.put("LIENNO", lienNo);
                    flexiMap.put("LIEN_NO", lienNo);
                    flexiMap.put("SUBNO", CommonUtil.convertObjToInt("1"));
                    flexiMap.put("UNLIEN_DT", currDt);
                    flexiMap.put(CommonConstants.ACT_NUM, objCashTransactionTODel.getActNum());
                    flexiMap.put("LIENAMOUNT", new Double(0.0));//String.valueOf(0.0)
                    flexiMap.put(LIEN_AMOUNT, cashMap.get(LIEN_AMOUNT));
                    System.out.println("flexiMap : " + flexiMap);
                    sqlMap.executeUpdate("updateSubAcInfoBal", flexiMap);
                    sqlMap.executeUpdate("updateForSBLienMarking", flexiMap);
                    sqlMap.executeUpdate("updateReducingLienAmountDAO", flexiMap);
                    flexiMap = null;
                }
                cashMap = null;
            }
        }
        if (isDaily) {
            HashMap dailyUpdateMap = new HashMap();
            dailyUpdateMap.put(CommonConstants.TRANS_ID, objCashTransactionTODel.getTransId());
            dailyUpdateMap.put(CommonConstants.ACT_NUM, objCashTransactionTODel.getActNum());
            dailyUpdateMap.put("STATUS", objCashTransactionTODel.getStatus());
            dailyUpdateMap.put("STATUS_DT", objCashTransactionTODel.getStatusDt());
            dailyUpdateMap.put("STATUS_BY", objCashTransactionTODel.getStatusBy());
            dailyUpdateMap.put("TRANS_DT", objCashTransactionTODel.getTransDt());
            dailyUpdateMap.put("INITIATED_BRANCH", objCashTransactionTODel.getInitiatedBranch());
            sqlMap.executeUpdate("deleteCashTransactionTOForDaily", dailyUpdateMap);
            dailyUpdateMap = null;
            isDaily = false;
        } else {
            objCashTransactionTODel.setInitiatedBranch(_branchCode);
            objCashTransactionTODel.setTransDt((Date)currDt.clone());
            objCashTransactionTODel.setAuthorizeStatus(CommonConstants.STATUS_REJECTED);
            objCashTransactionTODel.setAuthorizeBy(CommonUtil.convertObjToStr(objCashTransactionTODel.getStatusBy()));
            objCashTransactionTODel.setAuthorizeDt((Date)currDt.clone());
            sqlMap.executeUpdate("deleteCashTransactionTO", objCashTransactionTODel);
        }
        if (interestMap != null && interestMap.get("PRODUCT_TYPE").equals(TransactionFactory.DEPOSITS)) {
            System.out.println("####### InterestMap" + interestMap);
            HashMap prodMap = new HashMap();
            prodMap.put(PROD_ID, interestMap.get("PRODUCT_ID"));
            List lstProd = sqlMap.executeQueryForList("getBehavesLikeForDeposit", prodMap);
            if (lstProd != null && lstProd.size() > 0) {
                prodMap = (HashMap) lstProd.get(0);
                if (prodMap.get(BEHAVES_LIKE).equals("FIXED")) {
                    String depositSubNo = CommonUtil.convertObjToStr(interestMap.get(CommonConstants.ACT_NUM));
                    if (depositSubNo.lastIndexOf("_") != -1) {
                        depositSubNo = depositSubNo.substring(0, depositSubNo.lastIndexOf("_"));
                    }
                    HashMap fdPaymentMap = new HashMap();
                    fdPaymentMap.put(DEPOSIT_NO, depositSubNo);
                    fdPaymentMap.put("FD_CASH_PAYMENT", "N");
                    sqlMap.executeUpdate("updateFDCashPayment", fdPaymentMap);
                }
            }
        }
    }

    private boolean checkInterBranchTransaction(List selectedList) throws Exception {
        int size = selectedList.size();
        CashTransactionTO objCashTransactionTO;
        boolean isInterBranch = false;
        for (int i = 0; i < size; i++) {
            objCashTransactionTO = (CashTransactionTO) selectedList.get(i);
            if (!objCashTransactionTO.getBranchId().equals(objCashTransactionTO.getInitiatedBranch())) {
                isInterBranch = true;
            }
        }
        String oldBranch = "";
        int cnt = 1;
        for (int i = 0; i < size; i++) {
            objCashTransactionTO = (CashTransactionTO) selectedList.get(i);
            if (objCashTransactionTO.getBranchId().equals(oldBranch)) {
                cnt++;
            }
            oldBranch = objCashTransactionTO.getBranchId();
        }
//         if (cnt == size) {
//             isInterBranch = false;
//         }
        return isInterBranch;

    }

    private void authorize(HashMap map, LogDAO objLogDAO, LogTO objLogTO, boolean isTransaction) throws Exception {
        boolean isInterBranch = false;
        status = (String) map.get(CommonConstants.AUTHORIZESTATUS);
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        String cashAcctHd = getCashAcctHead();
        System.out.println("cashAcctHd@@@" + cashAcctHd);
        HashMap interestMap = (HashMap) map.get("INTERESTMAP");
        System.out.println("####### Authorize InterestMap" + interestMap + "###Map :" + map);
        Double amount;
        HashMap dataMap;
        String prodType;
        String loanlinkBatchId = "";
        CashTransactionTO objCashTrans = null;
        //        boolean isTrans = true;
        int a = 0, b = 0;
        //        boolean isTransactionboolean=false;
        try {
            if (isTransaction) {
                sqlMap.startTransaction();
            }
            if (!map.containsKey("LOAN_TRANS_OUT")) {
                asAnCustomerCreditLoanAdvances(selectedList, status);
            }

            if (selectedList != null && selectedList.size() > 0) {
                System.out.println("####### selectedList###Map :" + selectedList + "SIZE" + selectedList.size());
                for (int i = 0, j = selectedList.size(); i < j; i++) {
                    boolean isTrans = true;
                    //               if(isTransaction && isTransactionboolean)
                    //                   sqlMap.startTransaction();
                    dataMap = (HashMap) selectedList.get(i);
                    System.out.println("$#$#$#$ transID : " + transID);
                    System.out.println("$#$#$#$ dataMap : " + dataMap);
                    if (dataMap.containsKey("ACCOUNT NO") && dataMap.get("ACCOUNT NO") != null) {
                        loanlinkBatchId = CommonUtil.convertObjToStr(dataMap.get("ACCOUNT NO"));
                    }
                    if (dataMap.containsKey(transID)) {
                        throw new TTException("This transaction already authorized...");
                    }
                    transID = (String) dataMap.get(TransactionDAOConstants.TRANS_ID);
                    dataMap.put(CommonConstants.STATUS, status);
                    dataMap.put(CommonConstants.USER_ID, objLogTO.getUserId());
                    dataMap.put(TransactionDAOConstants.TODAY_DT, currDt);
                    System.out.println("$#$#$#$ transIdStorageMap : " + transIdStorageMap);
                    dataMap.put("TRANS_DT", currDt);
                    dataMap.put("INITIATED_BRANCH", _branchCode);
                    List authLst = (List) sqlMap.executeQueryForList("getCashAuthorizeStatus", dataMap);
                    if (authLst != null && authLst.size() > 0) {
                        HashMap whereMap = (HashMap) authLst.get(0);
                        String authStatus = CommonUtil.convertObjToStr(whereMap.get(AUTHORIZE_STATUS));
                        String authBy = CommonUtil.convertObjToStr(whereMap.get("AUTHORIZE_BY"));
                        if (!authStatus.equals("") && !status.equals(CommonConstants.STATUS_EXCEPTION)) {
                            throw new TTException("This transaction already " + authStatus.toLowerCase() + " by " + authBy);
                        }
                    }
                    if (status.equals(CommonConstants.STATUS_EXCEPTION)) {
                        dataMap.put("BRANCH_ID", _branchCode);
                        sqlMap.executeUpdate("exceptionTransaction", dataMap);
                        //                    sqlMap.commitTransaction();
                        //                    continue;
                    }
                    //                    sqlMap.executeUpdate("authorizeCashTransaction", dataMap);

                    log.info("authorize ... dataMap : " + dataMap);
                    //                    sqlMap.executeUpdate("authorizePassBook", dataMap);
                    //System.out.println("DAO side DataMap: " + dataMap);
                    //System.out.println("DAO side TransModule: " + transModuleBased);

                    objCashTrans = getTransactionData(transID);
                    List list = null;
                    if (map.containsKey("DAILY")) {
                        //                    list = (List) sqlMap.executeQueryForList("getSelectCashTransactionTODAILY", transID);
                        HashMap param = new HashMap();
                        param.put("TRANS_ID", transID);
                        param.put("INITIATED_BRANCH", _branchCode);
                        param.put("TRANS_DT", currDt.clone());
                        list = (List) sqlMap.executeQueryForList("getCashTransactionTOBatchForAuthorzation", param);
                        b = list.size();

                    }
                    if (list == null) {
                        list = new ArrayList();
                        list.add(objCashTrans);
                    }
                    if (checkInterBranchTransaction(list)) {
                        map.put("INTER_BRANCH_TRANS", new Boolean(true));
                        isInterBranch = true;
                    } else {
                        isInterBranch = false;
                    }
                    while (isTrans) {

                        if (a + 1 <= b) {
                            objCashTrans = (CashTransactionTO) list.get(a);
                            a++;
                        } else {
                            isTrans = false;
                            if (map.containsKey("DAILY")) {
                                objCashTrans = null;
                            }
                        }
                        if (objCashTrans != null) {
                            prodType = objCashTrans.getProdType();
                            System.out.println("objCashTrans####" + objCashTrans);
                            transModuleBased = TransactionFactory.createTransaction(prodType);
                            objCashTrans.setAuthorizeBy(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.USER_ID)));

                            // AuthorizeStatus is Authorized
                            if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                                // Exisiting status is Created or Modified
                                if (objCashTrans.getStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED)
                                        || objCashTrans.getStatus().equalsIgnoreCase(CommonConstants.STATUS_MODIFIED)) {
                                    if (shift.equals("A")) {
                                        dataMap.put("SHIFT", shifttime);
                                    } else {
                                        dataMap.put("SHIFT", shift);
                                    }
                                    if (prodType.equals("TL") && map.containsKey("CORP_LOAN_MAP")) { //For Corporate Loan purpose added by Rajesh
                                        corpLoanMap = (HashMap) map.get("CORP_LOAN_MAP");
                                    }

                                    HashMap ruleMap = getRuleMap(objCashTrans, 0.0, true);
                                    String ACT_NUM = objCashTrans.getActNum();
                                    double limit = 0.0;
                                    double dp = 0.0;
                                    String prod = objCashTrans.getProdType();
                                    HashMap where = new HashMap();
                                    where.put(CommonConstants.ACT_NUM, ACT_NUM);
                                    where.put(CommonConstants.BRANCH_ID, objCashTrans.getBranchId());
                                    if (objCashTrans.getProdType().equals(TransactionFactory.OPERATIVE) || objCashTrans.getProdType().equals(TransactionFactory.ADVANCES) || objCashTrans.getProdType().equals(TransactionFactory.AGRIADVANCES)) {
                                        String type_of_tod = "";
                                        List lstBal = sqlMap.executeQueryForList("getBalance" + prod, where);
                                        where = null;
                                        if (lstBal != null && lstBal.size() > 0) {
                                            where = (HashMap) lstBal.get(0);
                                            double clBal = CommonUtil.convertObjToDouble(where.get("CLEAR_BALANCE")).doubleValue();
                                            if (objCashTrans.getProdType().equals(TransactionFactory.ADVANCES) || objCashTrans.getProdType().equals(TransactionFactory.AGRIADVANCES)) {
                                                clBal = CommonUtil.convertObjToDouble(where.get("LOAN_PAID_INT")).doubleValue();
                                                limit = CommonUtil.convertObjToDouble(where.get("LIMIT")).doubleValue();
                                                dp = CommonUtil.convertObjToDouble(where.get("DRAWING_POWER")).doubleValue();
                                                if (dp > 0 && dp < limit) {
                                                    limit = dp;
                                                }
                                            }
                                            ruleMap.put("LIMIT", String.valueOf(limit));
                                            double amt = CommonUtil.convertObjToDouble(objCashTrans.getAmount()).doubleValue();
                                            double tod_amt = CommonUtil.convertObjToDouble(where.get("TOD_AMOUNT")).doubleValue();
                                            double tod_util = CommonUtil.convertObjToDouble(where.get("TOD_UTILIZED")).doubleValue();
                                            double tod_left = tod_amt - tod_util;
                                            where.put("AMOUNT", objCashTrans.getAmount());
                                            Date toDt = (Date) currDt.clone();
                                            if (objCashTrans.getTransDt() != null) {
                                                toDt.setDate(objCashTrans.getTransDt().getDate());
                                                toDt.setMonth(objCashTrans.getTransDt().getMonth());
                                                toDt.setYear(objCashTrans.getTransDt().getYear());
                                                where.put("TODAY_DT", toDt);
                                            }
                                            //                                        where.put("TODAY_DT",objCashTrans.getTransDt());
                                            where.put(CommonConstants.ACT_NUM, ACT_NUM);
                                            System.out.println("For Updating Balances" + where);
                                            double posBal = Math.abs(clBal);
                                            String trans_type = CommonUtil.convertObjToStr(objCashTrans.getTransType());
                                            System.out.println("posBal" + posBal);
                                            List lst = sqlMap.executeQueryForList("getTypeOfTod", where);
                                            HashMap hash = new HashMap();
                                            if (lst != null && lst.size() > 0) {
                                                hash = (HashMap) lst.get(0);
                                                type_of_tod = CommonUtil.convertObjToStr(hash.get("TYPE_OF_TOD"));

                                                System.out.println(" list size " + lst.size());
                                                if (type_of_tod.equals("SINGLE")) {
                                                    System.out.println(" Inside SINGLE1 " + ruleMap);
                                                    if (objCashTrans.getProdType().equals(TransactionFactory.OPERATIVE)) {
                                                        if (clBal >= 0.0 && clBal <= amt && trans_type.equals(TransactionDAOConstants.DEBIT)) {
                                                            where.put("TODUTILIZED", "");
                                                        }
                                                    }
                                                    if (objCashTrans.getProdType().equals(TransactionFactory.ADVANCES) || objCashTrans.getProdType().equals(TransactionFactory.AGRIADVANCES)) {
                                                        if (clBal >= 0.0 && clBal <= amt && trans_type.equals(TransactionDAOConstants.DEBIT)) {
                                                            where.put("TODUTILIZEDAD", "");
                                                        }
                                                    }
                                                    if (clBal >= 0.0 && clBal <= amt && trans_type.equals(TransactionDAOConstants.CREDIT)) {
                                                        where.put("TODUTILIZEDCBMORE", "");
                                                    }
                                                    if (clBal < 0.0 && trans_type.equals(TransactionDAOConstants.DEBIT)) {
                                                        where.put("TODUTILIZEDCBLESS", "");
                                                    }
                                                    if (clBal < 0.0 && trans_type.equals(TransactionDAOConstants.CREDIT)) {
                                                        where.put("TODUTILIZEDCBMORE", "");
                                                    }
                                                    if (clBal > 0.0 && clBal > amt) {
                                                        where.put("TODUTILIZEDCBMORE", "");
                                                    }

                                                    if (trans_type.equals(TransactionDAOConstants.CREDIT)) {
                                                        ruleMap.put("TOD_LEFT", String.valueOf(tod_left));
                                                        System.out.println(" Inside CREDIT " + ruleMap);
                                                        if (clBal < 0.0 && amt >= posBal) {
                                                            ruleMap.put("GREATERAMTCREDIT", "");
                                                        } else if (clBal < 0.0 && amt < posBal) {
                                                            ruleMap.put("LESSERAMTCREDIT", "");
                                                        } else if (clBal >= 0.0) {
                                                            if (lst != null && lst.size() > 0) {
                                                                if (objCashTrans.getProdType().equals(TransactionFactory.ADVANCES) || objCashTrans.getProdType().equals(TransactionFactory.AGRIADVANCES)) {
                                                                    ruleMap.put("NORMALAD", "");
                                                                } else {
                                                                    ruleMap.put("NORMAL", null);
                                                                }
                                                            } else {
                                                                ruleMap.put("NORMAL", null);
                                                            }
                                                            System.out.println("ruleMap Inside CREDIT" + ruleMap);
                                                        }
                                                    }
                                                } else if (type_of_tod.equals("RUNNING")) {
                                                    System.out.println(" Inside Running " + ruleMap);
                                                    if (objCashTrans.getProdType().equals(TransactionFactory.OPERATIVE)) {
                                                        if (clBal >= 0.0 && clBal <= amt && trans_type.equals(TransactionDAOConstants.DEBIT)) {
                                                            where.put("TODUTILIZED", "");
                                                        }
                                                    } else if (objCashTrans.getProdType().equals(TransactionFactory.ADVANCES) || objCashTrans.getProdType().equals(TransactionFactory.AGRIADVANCES)) {
                                                        if (clBal >= 0.0 && clBal <= amt && trans_type.equals(TransactionDAOConstants.DEBIT)) {
                                                            where.put("TODUTILIZEDAD", "");
                                                        }
                                                    }
                                                    if (clBal >= 0.0 && clBal <= amt && trans_type.equals(TransactionDAOConstants.CREDIT)) {
                                                        where.put("TODUTILIZEDCBMORERUNNING", "");
                                                    }
                                                    if (clBal < 0.0 && trans_type.equals(TransactionDAOConstants.DEBIT)) {
                                                        where.put("TODUTILIZEDCBLESS", "");
                                                    }
                                                    if (clBal < 0.0 && trans_type.equals(TransactionDAOConstants.CREDIT)) {
                                                        where.put("TODUTILIZEDCBMORERUNNING", "");
                                                    }
                                                    if (clBal > 0.0 && clBal > amt) {
                                                        where.put("TODUTILIZEDCBMORE", "");
                                                    }
                                                    if (trans_type.equals(TransactionDAOConstants.CREDIT)) {
                                                        ruleMap.put("TOD_AMOUNT", String.valueOf(tod_amt));
                                                        ruleMap.put("TOD_UTILIZED", String.valueOf(tod_util));
                                                        ruleMap.put("TOD_LEFT", String.valueOf(tod_left));
                                                        ruleMap.put("LIMIT", String.valueOf(limit));
                                                        if (clBal < 0.0) {
                                                            ruleMap.put("GREATERAMTCREDITRUNNING", "");
                                                        } else if (clBal >= 0.0) {
                                                            if (lst != null && lst.size() > 0) {
                                                                if (objCashTrans.getProdType().equals(TransactionFactory.ADVANCES) || objCashTrans.getProdType().equals(TransactionFactory.AGRIADVANCES)) {
                                                                    ruleMap.put("NORMALAD", "");
                                                                } else {
                                                                    ruleMap.put("NORMAL", "");
                                                                }
                                                            } else {
                                                                ruleMap.put("NORMAL", "");
                                                            }
                                                        }
                                                        System.out.println("ruleMap Inside CREDIT" + ruleMap);
                                                    }
                                                }
                                                if (trans_type.equals(TransactionDAOConstants.DEBIT)) {
                                                    System.out.println(" Inside DEBIT " + ruleMap);
                                                    if (lst != null && lst.size() > 0) {
                                                        if (objCashTrans.getProdType().equals(TransactionFactory.ADVANCES) || objCashTrans.getProdType().equals(TransactionFactory.AGRIADVANCES)) {
                                                            ruleMap.put("NORMALDEBITAD", "");
                                                        } else {
                                                            ruleMap.put("NORMALDEBIT", "");
                                                        }
                                                    } else {
                                                        ruleMap.put("NORMALDEBIT", "");
                                                    }
                                                    System.out.println("ruleMap Inside DEBIT" + ruleMap);
                                                }
                                            } else {
                                                System.out.println(" Inside Common " + ruleMap);
                                                if (trans_type.equals(TransactionDAOConstants.CREDIT)) {
                                                    if (objCashTrans.getProdType().equals(TransactionFactory.ADVANCES) || objCashTrans.getProdType().equals(TransactionFactory.AGRIADVANCES)) {
                                                        ruleMap.put("LIMIT", String.valueOf(limit));
                                                        ruleMap.put("NORMALWOTOD", "");
                                                    } else {
                                                        ruleMap.put("NORMAL", "");
                                                    }
                                                } else {
                                                    ruleMap.put("NORMALDEBIT", "");
                                                }
                                            }
                                            System.out.println("updateTODUtilized" + where);
                                            if (lst != null && lst.size() > 0) {
                                                if (objCashTrans.getProdType().equals(TransactionFactory.OPERATIVE) || objCashTrans.getProdType().equals(TransactionFactory.ADVANCES) || objCashTrans.getProdType().equals(TransactionFactory.AGRIADVANCES)) {
                                                    sqlMap.executeUpdate("updateTODUtilized", where);
                                                }
                                            }
                                        }
                                    } else {
                                        System.out.println(" Inside CommonMain " + ruleMap);
                                        if (objCashTrans.getTransType().equals(TransactionDAOConstants.CREDIT)) {
                                            ruleMap.put("NORMAL", "");
                                        } else {
                                            ruleMap.put("NORMALDEBIT", "");
                                        }
                                    }
                                    if (map.containsKey("PRODUCT")) {
                                        ruleMap.put("PRODUCT", "SHARE");
                                    }
                                    if (!objCashTrans.getProdType().equals("") && objCashTrans.getProdType().equals(TransactionFactory.OPERATIVE)
                                            && objCashTrans.getActNum() != null && objCashTrans.getTransType().equals(TransactionDAOConstants.CREDIT)) {
                                        HashMap cashMap = new HashMap();
                                        HashMap flexiMap = new HashMap();
                                        cashMap.put("LIEN_AC_NO", objCashTrans.getActNum());
                                        double transAmt = CommonUtil.convertObjToDouble(objCashTrans.getAmount()).doubleValue();
                                        System.out.println("cashMap " + cashMap);
                                        List lstLien = sqlMap.executeQueryForList("getSelectReducingLienAmountDAO", cashMap);
                                        if (lstLien != null && lstLien.size() > 0) {
                                            for (int k = 0; k < lstLien.size(); k++) {
                                                cashMap = (HashMap) lstLien.get(k);
                                                System.out.println("cashMap " + cashMap + "SIZE" + lstLien.size());
                                                double lienAmt = CommonUtil.convertObjToDouble(cashMap.get(LIEN_AMOUNT)).doubleValue();
                                                flexiAmount = lienAmt;
                                                System.out.println("cashMap " + lienAmt + "SIZE" + lstLien.size());
                                                if (transAmt >= lienAmt) {
                                                    lienAmt = 0;
                                                    flexiMap.put("LIENAMOUNT", new Double(flexiAmount * -1));
                                                    flexiMap.put("AVAILABLE_BALANCE", new Double(lienAmt));
                                                    flexiMap.put("STATUS", "UNLIENED");
                                                    System.out.println("transAmt if " + transAmt);
                                                    System.out.println("lienAmt if " + lienAmt);
                                                } else {
                                                    flexiMap.put("LIENAMOUNT", new Double(transAmt * -1));
                                                    flexiMap.put("STATUS", CommonConstants.STATUS_MODIFIED);
                                                    flexiMap.put(LIEN_AMOUNT, new Double(lienAmt - transAmt));
                                                    flexiMap.put("AVAILABLE_BALANCE", new Double(lienAmt - transAmt));
                                                    System.out.println("transAmt else " + transAmt);
                                                    System.out.println("lienAmt else " + lienAmt);
                                                }
                                                System.out.println("cashMap " + cashMap);
                                                String lienNo = CommonUtil.convertObjToStr(cashMap.get("LIEN_NO"));
                                                flexiMap.put("LIEN_NO", lienNo);
                                                flexiMap.put("DEPOSIT_ACT_NUM", cashMap.get(DEPOSIT_NO));
                                                flexiMap.put(SHADOWLIEN, new Double(0.0));
                                                flexiMap.put("COMMAND", CommonConstants.TOSTATUS_UPDATE);
                                                flexiMap.put("USER_ID", objCashTrans.getStatusBy());
                                                flexiMap.put("LIEN_AC_NO", objCashTrans.getActNum());
                                                flexiMap.put(AUTHORIZE_STATUS, CommonConstants.STATUS_AUTHORIZED);
                                                flexiMap.put("AUTHORIZE_DATE", currDt);
                                                flexiMap.put("LIENNO", lienNo);
                                                flexiMap.put("SUBNO", CommonUtil.convertObjToInt("1"));
                                                flexiMap.put("UNLIEN_DT", currDt);
                                                flexiMap.put(CommonConstants.ACT_NUM, objCashTrans.getActNum());
                                                System.out.println("flexiMap : " + flexiMap);
                                                sqlMap.executeUpdate("updateSubAcInfoBal", flexiMap);
                                                sqlMap.executeUpdate("updateForSBLienMarking", flexiMap);
                                                if (lienAmt != 0) {
                                                    sqlMap.executeUpdate("updateReducingLienAmountDAO", flexiMap);
                                                }
                                                lienAmt = CommonUtil.convertObjToDouble(cashMap.get(LIEN_AMOUNT)).doubleValue();
                                                transAmt = transAmt - lienAmt;
                                                if (transAmt < 0) {
                                                    break;
                                                }
                                                System.out.println("Flexi Deposit Updation Completed...");
                                            }
                                        }
                                        cashMap = null;
                                        flexiMap = null;
                                        lstLien = null;
                                    }
                                    if (isException) {
                                        transModuleBased.validateRules(ruleMap, true);
                                    } else {
                                        transModuleBased.validateRules(ruleMap, false);
                                    }
                                    //                                errorList = transModuleBased.getErrorList();

                                    log.info("authorize ... objCashTrans : " + objCashTrans.toString());
                                    log.info("updateAvailableBalance ... after getRuleMap updateMap : " + getRuleMap(objCashTrans, 0.0, true));
                                    log.info("updateAvailableBalance : " + objCashTrans.toString());
                                    transModuleBased.authorizeUpdate(ruleMap, objCashTrans.getAmount());
                                    amount = getTransAmount(objCashTrans);
                                    System.out.println("objCashTrans.getAcHdId" + objCashTrans.getAcHdId());
                                    String ruleTransType = CommonUtil.convertObjToStr(ruleMap.get(TransactionDAOConstants.TRANS_TYPE));
//                                    if(CommonUtil.convertObjToStr(ruleMap.get(TransactionDAOConstants.PROD_TYPE)).equals("GL"))
                                    transModuleBased.updateGL(objCashTrans.getAcHdId(), amount, objLogTO, ruleMap);

                                    objLogTO.setBranchId(_branchCode);

                                    if (ruleTransType.equals(CommonConstants.CREDIT)) {
                                        ruleMap.put(TransactionDAOConstants.TRANS_TYPE, CommonConstants.DEBIT);
                                    } else {
                                        ruleMap.put(TransactionDAOConstants.TRANS_TYPE, CommonConstants.CREDIT);
                                    }
                                    ruleMap.put(TransactionDAOConstants.PROD_TYPE, TransactionFactory.GL);

                                    //                                transModuleBased.updateGL(cashAcctHd, amount, objLogTO, ruleMap, true);  //This line commented & the following if block added by Rajesh for Interbranch Transactions.
                                    if (map.containsKey("INTER_BRANCH_TRANS")
                                            && new Boolean(CommonUtil.convertObjToStr(map.get("INTER_BRANCH_TRANS"))).booleanValue() == true && isInterBranch) {
                                        ruleMap.put("INTER_BRANCH", new Boolean(isInterBranch));
                                        ruleMap.put("IS_INTER_BRANCH_TRANS", new Boolean(isInterBranch));
                                        transModuleBased.updateGL(cashAcctHd, amount, objLogTO, ruleMap);
                                    } else {
                                        ruleMap.put("INTER_BRANCH", new Boolean(isInterBranch));
                                        transModuleBased.updateGL(cashAcctHd, amount, objLogTO, ruleMap, true);
                                    }

                                    updatepassbook(objCashTrans);
                                }
                                if (interestMap != null && interestMap.get("PRODUCT_TYPE").equals(TransactionFactory.DEPOSITS)) {
                                    System.out.println("####### InterestMap" + interestMap);
                                    HashMap prodMap = new HashMap();
                                    String depositSubNo = CommonUtil.convertObjToStr(interestMap.get(CommonConstants.ACT_NUM));
                                    if (depositSubNo.lastIndexOf("_") != -1) {
                                        depositSubNo = depositSubNo.substring(0, depositSubNo.lastIndexOf("_"));
                                    }
                                    prodMap.put(CommonConstants.ACT_NUM, depositSubNo);
                                    List lstProd = sqlMap.executeQueryForList("getBehavesLikeForDepositNo", prodMap);
                                    if (lstProd != null && lstProd.size() > 0) {
                                        prodMap = (HashMap) lstProd.get(0);
                                        if (prodMap.get(BEHAVES_LIKE).equals("FIXED")) {
                                            HashMap creditMap = new HashMap();
                                            double totBalance = 0;
                                            creditMap.put(DEPOSIT_NO, depositSubNo);
                                            List lstCredit = sqlMap.executeQueryForList("getTotalIntBalanceForDeposit", creditMap);
                                            if (lstCredit != null && lstCredit.size() > 0) {
                                                creditMap = (HashMap) lstCredit.get(0);
                                                double credited = CommonUtil.convertObjToDouble(creditMap.get("TOTAL_INT_CREDIT")).doubleValue();
                                                double drawn = CommonUtil.convertObjToDouble(creditMap.get("TOTAL_INT_DRAWN")).doubleValue();
                                                totBalance = credited - drawn;
                                            }
                                            double intAmt = CommonUtil.convertObjToDouble(interestMap.get("INT_AMT")).doubleValue();
                                            System.out.println("intAmt :" + intAmt);
                                            InterestBatchTO interestBatchTO = new InterestBatchTO();
                                            interestBatchTO.setActNum(CommonUtil.convertObjToStr(interestMap.get(CommonConstants.ACT_NUM)));
                                            interestBatchTO.setAcHdId(CommonUtil.convertObjToStr(interestMap.get("AC_HD_ID")));
                                            interestBatchTO.setIntAmt(new Double(intAmt));
                                            interestBatchTO.setProductId(CommonUtil.convertObjToStr(prodMap.get(PROD_ID)));
                                            interestBatchTO.setProductType(CommonUtil.convertObjToStr(interestMap.get("PRODUCT_TYPE")));
                                            interestBatchTO.setIntType(CommonUtil.convertObjToStr(interestMap.get("INT_TYPE")));
                                            interestBatchTO.setIntDt((Date) interestMap.get("INT_DT"));
                                            interestBatchTO.setApplDt((Date) interestMap.get("APPL_DT"));
                                            interestBatchTO.setIntRate(CommonUtil.convertObjToDouble(interestMap.get("INT_RATE")));
                                            interestBatchTO.setPrincipleAmt(CommonUtil.convertObjToDouble(interestMap.get("PRINCIPLE_AMT")));
                                            interestBatchTO.setTransLogId(CommonUtil.convertObjToStr(interestMap.get("TRANS_LOG_ID")));
                                            interestBatchTO.setCustId(CommonUtil.convertObjToStr(interestMap.get("CUST_ID")));
                                            interestBatchTO.setDrCr(TransactionDAOConstants.DEBIT);
                                            interestBatchTO.setTrnDt((Date) interestMap.get("INT_PAID_DATE"));
                                            interestBatchTO.setTransLogId(CommonUtil.convertObjToStr(interestMap.get("IS_TDS_APPLIED")));
                                            interestBatchTO.setTot_int_amt(new Double(totBalance - intAmt));
                                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                                            sqlMap.executeUpdate("updateTotIndDrawnAmount", interestBatchTO);
                                            HashMap fdPaymentMap = new HashMap();
                                            fdPaymentMap.put(DEPOSIT_NO, depositSubNo);
                                            fdPaymentMap.put("FD_CASH_PAYMENT", "N");
                                            sqlMap.executeUpdate("updateFDCashPayment", fdPaymentMap);
                                            fdPaymentMap = null;
                                            creditMap = null;
                                        }
                                        HashMap updateMap = new HashMap();
                                        updateMap.put("DEPOSIT_NO", depositSubNo);
                                        if (CommonUtil.convertObjToStr(objCashTrans.getInstrumentNo1()).equals("INTEREST_AMT")) {
                                            updateMap.put("TEMP_NEXT_INT_APPL_DT", null);
                                            updateMap.put("TEMP_LAST_INT_APPL_DT", null);
                                            updateMap.put("INT_CREDIT", null);
                                            updateMap.put("INT_DRAWN", null);
                                            System.out.println("updateMap####" + updateMap);
                                            sqlMap.executeUpdate("updateDepositTempDate", updateMap);
                                        }


                                    }
                                    interestMap = null;
                                    prodMap = null;
                                }
                                if (objCashTrans.getProdType().equals(TransactionFactory.DEPOSITS) && map.containsKey("DEPOSIT_PENAL_AMT")) {
                                    System.out.println("####### InterestMap" + interestMap);
                                    HashMap prodMap = new HashMap();
                                    prodMap.put(PROD_ID, objCashTrans.getProdId());
                                    double penalMonth = CommonUtil.convertObjToDouble(map.get("DEPOSIT_PENAL_MONTH")).doubleValue();
                                    double penalAmt = CommonUtil.convertObjToDouble(map.get("DEPOSIT_PENAL_AMT")).doubleValue();
                                    List lstProd = sqlMap.executeQueryForList("getBehavesLikeForDeposit", prodMap);
                                    if (lstProd != null && lstProd.size() > 0) {
                                        prodMap = (HashMap) lstProd.get(0);
                                        if (prodMap.get(BEHAVES_LIKE).equals("RECURRING") && penalAmt > 0) {
                                            String act_Num = objCashTrans.getLinkBatchId();
                                            if (act_Num.lastIndexOf("_") != -1) {
                                                act_Num = act_Num.substring(0, act_Num.lastIndexOf("_"));
                                            }
                                            HashMap penalMap = new HashMap();
                                            penalMap.put(DEPOSIT_NO, act_Num);
//                                            penalMap.put("DELAYED_MONTH", String.valueOf(penalMonth));
//                                            penalMap.put("DELAYED_AMOUNT", String.valueOf(penalAmt));
                                            penalMap.put("DELAYED_MONTH", CommonUtil.convertObjToDouble(penalMonth));
                                            penalMap.put("DELAYED_AMOUNT", CommonUtil.convertObjToDouble(penalAmt));
                                            sqlMap.executeUpdate("updateDepositPenalAmount", penalMap);
                                            HashMap updateMap = new HashMap();
                                            updateMap.put("INSTRUMENT_NO2", objCashTrans.getInstrumentNo2());
                                            updateMap.put("TRANS_ID", objCashTrans.getTransId());
                                            updateMap.put("TRANS_DT", currDt);
                                            updateMap.put("INITIATED_BRANCH", _branchCode);
                                            sqlMap.executeUpdate("updateDepCashPenalMakingNull", updateMap);
                                            updateMap = null;
                                        }
                                    }
                                    prodMap = null;
                                }
                                // Checking for Rejected Status
                            } else if (status.equalsIgnoreCase(CommonConstants.STATUS_REJECTED)) {
                                //objCashTrans = getTransactionData(transID);
                                System.out.println("reject first status##" + objCashTrans);
                                dataMap.put("SHIFT", "");
                                // Exisiting status is Created or Modified
                                if (objCashTrans.getStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED)
                                        || objCashTrans.getStatus().equalsIgnoreCase(CommonConstants.STATUS_MODIFIED)) {
                                    transModuleBased.performShadowMinus(getRuleMap(objCashTrans, 0.0, false));
                                    //transModuleBased.performOtherBalanceMinus(getRuleMap(objCashTrans, 0.0, true));
                                } else {
                                    transModuleBased.performShadowAdd(getRuleMap(objCashTrans, 0.0, true));
                                    //transModuleBased.performOtherBalanceAdd(getRuleMap(objCashTrans, 0.0, true));

                                    dataMap.put(CommonConstants.STATUS, CommonConstants.STATUS_MODIFIED);
                                    dataMap.put("TRANS_DT", currDt);
                                    dataMap.put("INITIATED_BRANCH", _branchCode);
                                    sqlMap.executeUpdate("rejectCashTransaction", dataMap);
                                }
                                if (interestMap != null && interestMap.get("PRODUCT_TYPE").equals(TransactionFactory.DEPOSITS)) {
                                    System.out.println("####### InterestMap" + interestMap);
                                    HashMap prodMap = new HashMap();
                                    String depositSubNo = CommonUtil.convertObjToStr(interestMap.get(CommonConstants.ACT_NUM));
                                    if (depositSubNo.lastIndexOf("_") != -1) {
                                        depositSubNo = depositSubNo.substring(0, depositSubNo.lastIndexOf("_"));
                                    }
                                    prodMap.put(CommonConstants.ACT_NUM, depositSubNo);
                                    List lstProd = sqlMap.executeQueryForList("getBehavesLikeForDepositNo", prodMap);
                                    if (lstProd != null && lstProd.size() > 0) {
                                        prodMap = (HashMap) lstProd.get(0);
                                        if (prodMap.get(BEHAVES_LIKE).equals("FIXED")) {
                                            HashMap fdPaymentMap = new HashMap();
                                            fdPaymentMap.put(DEPOSIT_NO, depositSubNo);
                                            fdPaymentMap.put("FD_CASH_PAYMENT", "N");
                                            sqlMap.executeUpdate("updateFDCashPayment", fdPaymentMap);
                                            fdPaymentMap = null;
                                        }

                                        HashMap emptyMap = new HashMap();
                                        emptyMap.put("DEPOSIT_NO", depositSubNo);
                                        if (CommonUtil.convertObjToStr(objCashTrans.getInstrumentNo1()).equals("INTEREST_AMT")) {
                                            sqlMap.executeUpdate("updateDepositlastAppDt", emptyMap);

                                            emptyMap.put("TEMP_NEXT_INT_APPL_DT", null);
                                            emptyMap.put("TEMP_LAST_INT_APPL_DT", null);
                                            emptyMap.put("INT_CREDIT", null);
                                            emptyMap.put("INT_DRAWN", null);
                                            System.out.println("emptyMap####" + emptyMap);
                                            sqlMap.executeUpdate("updateDepositTempDate", emptyMap);
                                        }
                                    }
                                    interestMap = null;
                                    prodMap = null;
                                }
                            }
                            System.out.println("###cashTransactionDAOmap" + map);
                            if (map.containsKey(LINK_BATCH_ID)) {
                                if (map.get(LINK_BATCH_ID) != null) {
                                    String linkBatchId = CommonUtil.convertObjToStr(map.get(LINK_BATCH_ID)); //FOR CASH TRANSACTION
                                    System.out.println("@@@#####linkbatchiddao " + linkBatchId);
                                    HashMap cashAuthMap = new HashMap();
                                    cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                                    cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));

                                    cashAuthMap.put(LINK_BATCH_ID, map.get(LINK_BATCH_ID));
                                    ArrayList transferTransList = (ArrayList) sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", cashAuthMap);

                                    TransferTrans trans = new TransferTrans();
                                    trans.doTransferAuthorize(transferTransList, map);

                                }
                            }
                            // To apply TDS
                            if (!("DAILY").equals("DAILY") && prodType.equals(TransactionFactory.DEPOSITS)) {
                                //                    if(dataMap.containsKey(TransactionFactory.DEPOSITS)) {
                                System.out.println("deposit_NodataMap" + dataMap.get("ACCOUNT NO"));
                                HashMap tdsCalc = new HashMap();
                                tdsCalc.put(DEPOSIT_NO, dataMap.get("ACCOUNT NO"));
                                System.out.println("deposit_NotdsCalcMap" + tdsCalc.get(DEPOSIT_NO));
                                String deposit_No = CommonUtil.convertObjToStr(tdsCalc.get(DEPOSIT_NO));
                                deposit_No = deposit_No.substring(0, deposit_No.lastIndexOf("_"));
                                System.out.println("depositNo " + deposit_No);
                                tdsCalc.put(DEPOSIT_NO, deposit_No);
                                List lst = sqlMap.executeQueryForList("getCustNoforTDS", tdsCalc);
                                tdsCalc = new HashMap();
                                if (lst.size() > 0) {
                                    System.out.println("depositNoforexecutequery " + deposit_No);
                                    tdsCalc = (HashMap) lst.get(0);
                                }
                                //                            tdsCalc.put("CUST_ID", tdsCalc.get("CUST_ID"));
                                tdsCalc.put(PROD_ID, objCashTrans.getProdId());
                                tdsCalc.put("PROD_TYPE", objCashTrans.getProdType());
                                System.out.println("tdsCalcCUSTOMER " + tdsCalc);
                                TdsCalc tdsCalculator = new TdsCalc(_branchCode);
                                //                                tdsCalculator.setInsertData(CommonUtil.convertObjToStr(tdsCalc.get("CUST_ID")),null,null,0,0);
                            }
                            System.out.println("Flexi Deposit objCashTrans " + objCashTrans);
                            if (!objCashTrans.getTransId().equals("") && objCashTrans.getTransId().length() > 0
                                    && objCashTrans.getActNum() != null && objCashTrans.getTransType().equals(TransactionDAOConstants.DEBIT)) {
                                HashMap cashMap = new HashMap();
                                cashMap.put(BATCH_ID, objCashTrans.getTransId());
                                System.out.println("cashMap " + cashMap);
                                List lstLien = sqlMap.executeQueryForList("getSBLienTransferAccountNo", cashMap);
                                if (lstLien != null && lstLien.size() > 0) {
                                    for (int k = 0; k < lstLien.size(); k++) {
                                        cashMap = (HashMap) lstLien.get(k);
                                        System.out.println("cashMap " + cashMap);
                                        HashMap flexiMap = new HashMap();
                                        String lienNo = CommonUtil.convertObjToStr(cashMap.get("LIEN_NO"));
                                        flexiMap.put("DEPOSIT_ACT_NUM", cashMap.get(DEPOSIT_NO));
                                        flexiMap.put(SHADOWLIEN, new Double(CommonUtil.convertObjToDouble(cashMap.get(LIEN_AMOUNT)).doubleValue()));
                                        flexiMap.put("USER_ID", objCashTrans.getStatusBy());
                                        flexiMap.put("LIEN_AC_NO", objCashTrans.getActNum());
                                        flexiMap.put("AUTHORIZE_DATE", currDt);
                                        flexiMap.put("LIENNO", lienNo);
                                        flexiMap.put("SUBNO", CommonUtil.convertObjToInt("1"));
                                        flexiMap.put(CommonConstants.ACT_NUM, objCashTrans.getActNum());
                                        flexiMap.put(AUTHORIZE_STATUS, status);
                                        if (status.equals(CommonConstants.STATUS_AUTHORIZED)) {
                                            flexiMap.put("LIENAMOUNT", CommonUtil.convertObjToDouble(cashMap.get(LIEN_AMOUNT)));
                                            flexiMap.put("STATUS", objCashTrans.getStatus());
                                        } else if (status.equals(CommonConstants.STATUS_REJECTED)) {
                                            flexiMap.put("LIENAMOUNT", new Double(0.0));//String.valueOf(0.0));
                                            flexiMap.put("LIEN_NO", lienNo);
                                            flexiMap.put(LIEN_AMOUNT, String.valueOf(CommonUtil.convertObjToDouble(cashMap.get(LIEN_AMOUNT)).doubleValue()));
                                            flexiMap.put("UNLIEN_DT", currDt);
                                            flexiMap.put("STATUS", CommonConstants.STATUS_DELETED);
                                            sqlMap.executeUpdate("updateReducingLienAmountDAO", flexiMap);
                                        }
                                        System.out.println("flexiMap : " + flexiMap);
                                        sqlMap.executeUpdate("updateSubAcInfoBal", flexiMap);
                                        sqlMap.executeUpdate("updateForSBLienMarking", flexiMap);
                                        flexiMap = null;
                                    }
                                    cashMap = null;
                                }
                            }
                        }
                    }
                    dataMap.put("TRANS_DT", currDt);
                    dataMap.put("INITIATED_BRANCH", _branchCode);
                    sqlMap.executeUpdate("authorizeCashTransaction", dataMap);

                    // The following block added by Rajesh for sending SMS alerts
                    if (CommonUtil.convertObjToStr(CommonConstants.MOBILE_BANKING).equals("Y")
                            && CommonUtil.convertObjToStr(CommonConstants.MOBILE_BANKING_USERNAME).length() > 0
                            && CommonUtil.convertObjToStr(CommonConstants.MOBILE_BANKING_PWD).length() > 0
                            && CommonUtil.convertObjToStr(CommonConstants.MOBILE_BANKING_SENDERID).length() > 0) {
                        if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                            List lst = getTransactionDataForSMSAlerts(transID);
                            CashTransactionTO objCashTO = null;
                            String loanProdType = "";
                            String prodId = "";
                            String acctNum = "";
                            double crAmount = 0;
                            for (int s = 0; s < lst.size(); s++) {
                                objCashTO = (CashTransactionTO) lst.get(s);
                                if (objCashTO.getProdType().equals(TransactionFactory.LOANS)
                                        && objCashTO.getTransType().equals("CREDIT")) {//This line added because if TransType is CREDIT then only should set ProdType & AcctNum otherwise it's not inserting record into LOAN_TRANS_DETAILS table
                                    loanProdType = TransactionFactory.LOANS;
                                    acctNum = objCashTO.getActNum();
                                }
                                if (objCashTO.getProdType().equals(TransactionFactory.ADVANCES)
                                        && objCashTO.getTransType().equals("CREDIT")) {//This line added because if TransType is CREDIT then only should set ProdType & AcctNum otherwise it's not inserting record into ADV_TRANS_DETAILS table
                                    loanProdType = TransactionFactory.ADVANCES;
                                    acctNum = objCashTO.getActNum();
                                }
                                if (objCashTO.getProdType().equals(TransactionFactory.DEPOSITS)
                                        && objCashTO.getTransType().equals("CREDIT")) {//This line added because if TransType is CREDIT then only should set ProdType & AcctNum 
                                    loanProdType = TransactionFactory.DEPOSITS;
                                    acctNum = objCashTO.getActNum();
                                }
                                if (objCashTO.getTransType().equals("CREDIT")) {
                                    crAmount += objCashTO.getAmount().doubleValue();
                                }
                            }
                            for (int s = 0; s < lst.size(); s++) {
                                objCashTO = (CashTransactionTO) lst.get(s);
                                HashMap intMap = new HashMap();
                                HashMap getDateMap = new HashMap();
                                if (objCashTO.getProdType().equals(TransactionFactory.LOANS) || objCashTO.getProdType().equals(TransactionFactory.ADVANCES) || objCashTO.getProdType().equals(TransactionFactory.GL)) {
                                    List intList = null;
                                    if (acctNum != null && acctNum.length() > 0) {
                                        intMap.put(CommonConstants.ACT_NUM, acctNum);
                                    } else {
                                        intMap.put(CommonConstants.ACT_NUM, objCashTO.getLinkBatchId());
                                        acctNum = (String) objCashTO.getLinkBatchId();
                                    }
                                    if (intMap.get(CommonConstants.ACT_NUM) != null) {
                                        intList = sqlMap.executeQueryForList("getBehavesLikeTLAD", intMap);
                                    }
                                    if (intList != null && intList.size() > 0) {
                                        HashMap behaves = (HashMap) intList.get(0);
                                        loanProdType = CommonUtil.convertObjToStr(behaves.get("BEHAVES_LIKE"));
                                        loanProdType = loanProdType.equals("OD") ? "AD" : "TL";
                                        prodId = CommonUtil.convertObjToStr(behaves.get("PROD_ID"));
                                        break;
                                    }
                                }
                                if (objCashTO.getProdType().equals(TransactionFactory.DEPOSITS) || objCashTO.getProdType().equals(TransactionFactory.GL)) {
                                    List intList = null;
                                    if (acctNum != null && acctNum.length() > 0) {
                                        intMap.put(CommonConstants.ACT_NUM, acctNum);
                                    } else {
                                        intMap.put(CommonConstants.ACT_NUM, objCashTO.getLinkBatchId());
                                        acctNum = (String) objCashTO.getLinkBatchId();
                                    }
                                    if (intMap.get(CommonConstants.ACT_NUM) != null) {
                                        intList = sqlMap.executeQueryForList("getBehavesLikeForDepositNo", intMap);
                                    }
                                    if (intList != null && intList.size() > 0) {
                                        HashMap behaves = (HashMap) intList.get(0);
                                        loanProdType = TransactionFactory.DEPOSITS;
                                        prodId = CommonUtil.convertObjToStr(behaves.get("PROD_ID"));
                                        break;
                                    }
                                }
                            }

                            if ((loanProdType.equals(TransactionFactory.ADVANCES) || loanProdType.equals(TransactionFactory.LOANS)
                                    || loanProdType.equals(TransactionFactory.DEPOSITS)) && prodId.length() > 0 && crAmount > 0) {
                                List loanLst = new ArrayList();
                                boolean setCredit = false;
                                for (int s = 0; s < lst.size(); s++) {
                                    objCashTO = (CashTransactionTO) lst.get(s);
                                    if (objCashTO.getTransType().equals("CREDIT") && !setCredit) {
                                        objCashTO.setProdType(loanProdType);
                                        objCashTO.setProdId(prodId);
                                        objCashTO.setActNum(acctNum);
                                        objCashTO.setAmount(CommonUtil.convertObjToDouble(String.valueOf(crAmount)));
                                        loanLst.add(objCashTO);
                                        setCredit = true;
                                    }
                                    if (setCredit) {
                                        break;
                                    }
                                }
                                lst = new ArrayList();
                                lst.addAll(loanLst);
                                loanLst = null;
                            }
                            System.out.println("#$#$ lst:" + lst);
                            HashMap smsAlertMap = new HashMap();
                            if (lst != null && lst.size() > 0) {
                                for (int s = 0; s < lst.size(); s++) {
                                    objCashTO = (CashTransactionTO) lst.get(s);
                                    if ((!objCashTO.getProdType().equals(TransactionFactory.GL))&&(!objCashTO.getProdType().equals(TransactionFactory.OTHERBANKACTS))
                                            && CommonUtil.convertObjToStr(objCashTO.getProdId()).length() > 0) {
                                        smsAlertMap.put("PROD_TYPE", objCashTO.getProdType());
                                        smsAlertMap.put("PROD_ID", objCashTO.getProdId());
                                        List smsParamList = sqlMap.executeQueryForList("getSelectSMSParameterForAlerts", smsAlertMap);
                                        smsAlertMap.put(CommonConstants.ACT_NUM, objCashTO.getActNum().lastIndexOf("_") != -1
                                                ? objCashTO.getActNum().substring(0, objCashTO.getActNum().lastIndexOf("_")) : objCashTO.getActNum());
                                        List smsAccountList = sqlMap.executeQueryForList("getSelectSMSSubscriptionMap", smsAlertMap);
                                        smsAlertMap.put(CommonConstants.ACT_NUM, objCashTO.getActNum());
                                        if (smsAccountList != null && smsAccountList.size() > 0) {
                                            SMSSubscriptionTO objSMSSubscriptionTO = (SMSSubscriptionTO) smsAccountList.get(0);
                                            if (CommonUtil.convertObjToStr(objSMSSubscriptionTO.getMobileNo()) != null && CommonUtil.convertObjToStr(objSMSSubscriptionTO.getMobileNo()).length() == 10) {
                                                List actList = sqlMap.executeQueryForList("getBalance" + objCashTO.getProdType(), smsAlertMap);
                                                if (actList != null && actList.size() > 0) {
                                                    HashMap actMap = (HashMap) actList.get(0);
                                                    String actBal = CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(actMap.get("CLEAR_BALANCE")).doubleValue() > 0
                                                            ? actMap.get("CLEAR_BALANCE") : new Double(-1 * CommonUtil.convertObjToDouble(actMap.get("CLEAR_BALANCE")).doubleValue()))
                                                            + (CommonUtil.convertObjToDouble(actMap.get("CLEAR_BALANCE")).doubleValue() > 0 ? " CR." : " DR.");
                                                    String balString = objCashTO.getProdType().equals(TransactionFactory.LOANS) ? "Total Bal" : "Total Avail Bal";
                                                    //The following line added by Kannan AR advance accounts need to show available balance. (JIRA : KDSA - 301)
                                                    if (objCashTO.getProdType().equals(TransactionFactory.ADVANCES)) {
                                                        actBal = CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(actMap.get("AV_BALANCE")).doubleValue());
                                                    }
                                                    if (smsParamList != null && smsParamList.size() > 0) {
                                                        SMSParameterTO objSMSParameterTO = (SMSParameterTO) smsParamList.get(0);
                                                        String message = "";
                                                        if (objCashTO.getTransType().equals("CREDIT") && objSMSParameterTO.getCrCash().equals("Y")
                                                                && objCashTO.getAmount().doubleValue() >= objSMSParameterTO.getCrCashAmt().doubleValue()) {
                                                            message = "An amount of Rs." + CommonUtil.convertObjToStr(objCashTO.getAmount())
                                                                    + " has been CREDITED to your Account " + getAccountNo(objCashTO.getActNum()) + ". " + balString + ": Rs."
                                                                    + actBal + " (" + getFormattedFullDate() + ") - " + CommonConstants.BANK_SMS_DESCRIPTION + " - " + CommonConstants.MOBILE_BANKING_SENDERID;
                                                            System.out.println("#$#$ SMS:" + message);
                                                        } else if (objCashTO.getTransType().equals("DEBIT") && objSMSParameterTO.getDrCash().equals("Y")
                                                                && objCashTO.getAmount().doubleValue() >= objSMSParameterTO.getDrCashAmt().doubleValue()) {
                                                            message = "An amount of Rs." + CommonUtil.convertObjToStr(objCashTO.getAmount())
                                                                    + " has been DEBITED to your Account " + getAccountNo(objCashTO.getActNum()) + ". " + balString + ": Rs."
                                                                    + actBal + " (" + getFormattedFullDate() + ") - " + CommonConstants.BANK_SMS_DESCRIPTION + " - " + CommonConstants.MOBILE_BANKING_SENDERID;
                                                            System.out.println("#$#$ SMS:" + message);
//                                                            sendSMS(message, objSMSSubscriptionTO.getMobileNo());
                                                        }
                                                        if(message.length()>0){
                                                            this.smsConfigDAO = new SmsConfigDAO();
//                                                            smsConfigDAO.sendSMS(message, objSMSSubscriptionTO.getMobileNo());
                                                        }
                                                        smsParamList.clear();
                                                    }
                                                    actList.clear();
                                                }
                                                actList = null;
                                            }
                                            smsAccountList.clear();
                                        }
                                        smsAccountList = null;
                                        smsParamList = null;
                                        objCashTO = null;
                                    }
                                }
                                //                        lst.clear();
                            }
                            smsAlertMap.clear();
                            smsAlertMap = null;
                            lst = null;
                        }
                    }
                    // End Send SMS block

                    if (objCashTrans != null) {
                        if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {//reconciliation..
                            HashMap reconcileAuthMap = new HashMap();//transaction authorizing
                            reconcileAuthMap.put("PRESENT_TRANS_ID", objCashTrans.getTransId());
                            reconcileAuthMap.put("PRESENT_TRANS_DT", currDt);
                            List lst = sqlMap.executeQueryForList("getSelectEditModeReconciliationTransaction", reconcileAuthMap);
                            if (lst != null && lst.size() > 0) {
                                for (int k = 0; k < lst.size(); k++) {
                                    reconcileAuthMap = (HashMap) lst.get(k);
                                    double presentAmt = 0.0;
                                    double balance = 0.0;
                                    presentAmt = CommonUtil.convertObjToDouble(reconcileAuthMap.get("PRESENT_AMOUNT")).doubleValue();
                                    balance = CommonUtil.convertObjToDouble(reconcileAuthMap.get("BALANCE_AMOUNT")).doubleValue();
                                    double tempAmt = balance - presentAmt;
                                    reconciliationTO = new ReconciliationTO();// it will store into
                                    reconciliationTO.setTransId(CommonUtil.convertObjToStr(reconcileAuthMap.get(CommonConstants.TRANS_ID)));
                                    reconciliationTO.setBatchId(CommonUtil.convertObjToStr(reconcileAuthMap.get(BATCH_ID)));
                                    reconciliationTO.setAcHdId(CommonUtil.convertObjToStr(reconcileAuthMap.get("AC_HD_ID")));
                                    reconciliationTO.setTransAmount(new Double(presentAmt));
                                    reconciliationTO.setReconcileAmount(new Double(presentAmt));
                                    reconciliationTO.setBalanceAmount(new Double(tempAmt));
                                    reconciliationTO.setStatus(CommonConstants.STATUS_CREATED);
                                    reconciliationTO.setStatusBy(objCashTrans.getStatusBy());
                                    reconciliationTO.setStatusDt(currDt);
                                    reconciliationTO.setReconcileTransId(CommonUtil.convertObjToStr(reconcileAuthMap.get(PRESENT_TRANS_ID)));
                                    reconciliationTO.setReconcileBatchId(CommonUtil.convertObjToStr(reconcileAuthMap.get("PRESENT_BATCH_ID")));
                                    reconciliationTO.setTransMode(CommonUtil.convertObjToStr(reconcileAuthMap.get("TRANS_MODE")));
                                    reconciliationTO.setInitiatedBranch(objCashTrans.getInitiatedBranch());
                                    reconciliationTO.setBranchId(objCashTrans.getBranchId());
                                    reconciliationTO.setTransType(CommonUtil.convertObjToStr(reconcileAuthMap.get("TRANS_TYPE")));
                                    reconciliationTO.setTransDt(currDt);
                                    reconciliationTO.setAuthorizeStatus(String.valueOf(status));
                                    reconciliationTO.setAuthorizeDt(currDt);
                                    reconciliationTO.setAuthorizeBy(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.USER_ID)));
                                    reconciliationTO.setRecTranDt(currDt);
                                    System.out.println("reconciliationTO :" + reconciliationTO);
                                    sqlMap.executeUpdate("insertReconciliationTO", reconciliationTO);
                                    reconciliationTO = null;
                                    HashMap presentMap = new HashMap();
                                    presentMap.put(PRESENT_TRANS_ID, "");
                                    presentMap.put("PRESENT_BATCH_ID", "");
                                    presentMap.put("PRESENT_AMOUNT", new Double(0));
                                    presentMap.put(BATCH_ID, reconcileAuthMap.get(BATCH_ID));
                                    presentMap.put(CommonConstants.TRANS_ID, reconcileAuthMap.get(CommonConstants.TRANS_ID));
                                    presentMap.put("RECONCILE_AMOUNT", new Double(presentAmt));
                                    presentMap.put("BALANCE_AMOUNT", new Double(tempAmt));
                                    presentMap.put("TRANS_DT", currDt.clone());
                                    presentMap.put("INITIATED_BRANCH", _branchCode);
                                    System.out.println("presentMap :" + presentMap);
                                    sqlMap.executeUpdate("updateBalanceAmountTrans", presentMap);
                                    presentMap = null;
                                }
                            }
                            lst = null;
                            reconcileAuthMap = null;
                        } else if (status.equalsIgnoreCase(CommonConstants.STATUS_REJECTED) && objCashTrans.getTransId() != null) {
                            HashMap reconcileAuthMap = new HashMap();
                            reconcileAuthMap.put("PRESENT_TRANS_ID", objCashTrans.getTransId());
//                        reconcileAuthMap.put("PRESENT_TRANS_DT",objCashTrans.getTransDt());
//                        properFormatDate = objCashTrans.getTransDt();
                            System.out.println("txTransferTO.getTransDt()#### :" + objCashTrans.getTransDt());
                            Date trDt = (Date) currDt.clone();
                            trDt.setDate(objCashTrans.getTransDt().getDate());
                            trDt.setMonth(objCashTrans.getTransDt().getMonth());
                            trDt.setYear(objCashTrans.getTransDt().getYear());
//                                    reconcileAuthMap.put("TRANS_DT",trDt);
                            reconcileAuthMap.put("PRESENT_TRANS_DT", trDt);
                            System.out.println("reconcileAuthMapsDt()#### :" + reconcileAuthMap);
                            List lst = sqlMap.executeQueryForList("getSelectAllinDeleteMode", reconcileAuthMap);
                            if (lst != null && lst.size() > 0) {
                                for (int k = 0; k < lst.size(); k++) {
                                    reconcileAuthMap = (HashMap) lst.get(k);
                                    HashMap presentMap = new HashMap();
                                    presentMap.put("PRESENT_TRANS", "");
                                    presentMap.put("PRESENT_BATCH", "");
                                    presentMap.put("PRESENT_AMOUNT", new Double(0));
                                    presentMap.put("PRESENT_TRANS_ID", reconcileAuthMap.get("PRESENT_TRANS_ID"));
                                    presentMap.put("PRESENT_TRANS_DT", reconcileAuthMap.get("PRESENT_TRANS_DT"));
                                    System.out.println("presentMap :" + presentMap);
                                    sqlMap.executeUpdate("deleteOldReconcileAmt", presentMap);
                                    presentMap = null;
                                }
                            }
                            reconcileAuthMap = null;
                            lst = null;
                        }
                        if (objCashTrans.getTransId() != null) {
                            HashMap reconcileAuthMap = new HashMap();//transaction authorizing
                            reconcileAuthMap.put("BATCH_ID", objCashTrans.getTransId());
                            reconcileAuthMap.put("TRANS_DT", currDt.clone());
                            reconcileAuthMap.put("INITIATED_BRANCH", _branchCode);
                            List lst = sqlMap.executeQueryForList("getSelectAuthorizeListReconcile", reconcileAuthMap);
                            if (lst != null && lst.size() > 0) {
                                reconcileAuthMap.put(AUTHORIZE_STATUS, status);
                                reconcileAuthMap.put("AUTHORIZE_BY", objLogTO.getUserId());
                                reconcileAuthMap.put("AUTHORIZE_DT", currDt);
                                reconcileAuthMap.put("TRANS_DT", currDt.clone());
                                reconcileAuthMap.put("INITIATED_BRANCH", _branchCode);
                                sqlMap.executeUpdate("updateReconcileAuthorize", reconcileAuthMap);
                            }
                        }
                    }
                    objLogTO.setData(dataMap.toString());
                    objLogTO.setPrimaryKey((String) dataMap.get(TransactionDAOConstants.TRANS_ID));
                    objLogTO.setStatus(status);

                    objLogDAO.addToLog(objLogTO);
                }
                if (loanlinkBatchId.length() > 0) {
                    authorizeRebateInterestTransaction(loanlinkBatchId, status);
                }

            }
            if (isTransaction) {
                sqlMap.commitTransaction();
                //                isTransactionboolean=true;
            }

            status = null;
            selectedList = null;
            cashAcctHd = null;
            amount = null;
            dataMap = null;
            interestMap = null;
            //            transID= null;
        } catch (Exception e) {
            if (isTransaction) {
                sqlMap.rollbackTransaction();
            }
            e.printStackTrace();
            throw e;
        }
    }

    private String getFormattedFullDate() throws Exception {
        java.text.SimpleDateFormat DATE_FORMAT = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return DATE_FORMAT.format(new Date());
    }

    

//    private String sendSMS(String message, String phoneNo) {
//        String reply="";
//        BufferedReader in = null;
//        try {
////            String url = "http://easyhops.co.in/sendsms/rajeshsee/greshmaa/"+URLEncoder.encode("TMAGIC")+"/9916174195/"+
////                URLEncoder.encode("Dear Parent,  Rajesh  has  Boarded  the bus at Location at 04:00pm - Message Generated by SeE")+"/T";
//            String url = "http://easyhops.co.in/sendsms/"+CommonConstants.MOBILE_BANKING_USERNAME+
//                "/"+CommonConstants.MOBILE_BANKING_PWD+"/"+CommonConstants.MOBILE_BANKING_SENDERID+"/"+phoneNo+"/"+
//                URLEncoder.encode(message)+"/T";
//            System.out.println("#$#$ URL "+url);
//            // Uncomment the following block for sending SMS
//            URL myURL = new URL(url);
//            in = new BufferedReader(new InputStreamReader(myURL.openStream()));
//            String inputLine="";
//            while((inputLine = in.readLine()) != null) {
//                reply=inputLine;
//                System.out.println(reply);
//            }
//            if (in!=null) {
//                in.close();
//                myURL = null;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            try {
//                if (in!=null) {
//                    in.close();
//                }
//            } catch (Exception ee) {
//                ee.printStackTrace();
//            }
//        }
//        return reply;
//    }
    private String getAccountNo(String actNum) {
        String tempAcNo = CommonUtil.lpad(actNum.substring(7, 13), 13, '*');
        System.out.println("@#@ tempAcNo:" + tempAcNo);
        return tempAcNo;
    }

    private void asAnCustomerCreditLoanAdvances(ArrayList cashTos, String status) throws Exception {
        String prodType = "";
        String acctNum = "";
        long transSlNo = 0;
        HashMap insetMap = new HashMap();
        CashTransactionTO cashTo = null;
        if (status != null && status.length() > 0) {
            if (status.equals(CommonConstants.STATUS_AUTHORIZED)) {
                System.out.println(status + "cashTos######" + cashTos);
                HashMap transMap = (HashMap) cashTos.get(0);
                transMap.put("TRANS_DT", currDt);
                transMap.put("INITIATED_BRANCH", _branchCode);
                List cashList = sqlMap.executeQueryForList("getSelectCashTransactionTO", transMap);
                for (int j = 0; j < cashList.size(); j++) {
                    cashTo = new CashTransactionTO();
                    cashTo = (CashTransactionTO) cashList.get(j);
                    HashMap interestMap = new HashMap();
                    HashMap getDateMap = new HashMap();
                    System.out.println("cashTo####" + cashTo);
                    if (cashTo.getTransType().equals(CommonConstants.DEBIT)) {
                        return;
                    }
                    if (cashTo.getProdType().equals(TransactionFactory.LOANS) || cashTo.getProdType().equals(TransactionFactory.ADVANCES) || cashTo.getProdType().equals(TransactionFactory.GL)) {
                        List list = null;
                        if (acctNum != null && acctNum.length() > 0) {
                            interestMap.put(CommonConstants.ACT_NUM, acctNum);
                        } else {
                            interestMap.put(CommonConstants.ACT_NUM, cashTo.getLinkBatchId());
                            acctNum = (String) cashTo.getLinkBatchId();
                        }
                        HashMap lastCalDateMap = new HashMap();
                        lastCalDateMap.put(TransactionDAOConstants.ACCT_NO, interestMap.get(CommonConstants.ACT_NUM));
                        if (interestMap.get(CommonConstants.ACT_NUM) != null) {
                            list = sqlMap.executeQueryForList("getBehavesLikeTLAD", interestMap);
                        }
                        if (list != null && list.size() > 0) {
                            HashMap behaves = (HashMap) list.get(0);
                            prodType = CommonUtil.convertObjToStr(behaves.get(BEHAVES_LIKE));

                            interestMap.put(PROD_ID, behaves.get(PROD_ID));
                            prodType = prodType.equals("OD") ? TransactionFactory.ADVANCES : TransactionFactory.LOANS;

                            interestMap.put("TRANS_DT", currDt);
                            interestMap.put("INITIATED_BRANCH", _branchCode);
                            String mapNameForCalcInt = "IntCalculationDetail";
                            if (prodType.equals(TransactionFactory.ADVANCES)) {
                                mapNameForCalcInt = "IntCalculationDetailAD";
                            }
                            List intList = sqlMap.executeQueryForList(mapNameForCalcInt, interestMap);
                            if (intList != null && intList.size() > 0) {
                                getDateMap = (HashMap) intList.get(0);
                            }
                            getDateMap.put(CommonConstants.ACT_NUM, interestMap.get(CommonConstants.ACT_NUM));
                            getDateMap.put(PROD_ID, interestMap.get(PROD_ID));
                            getDateMap.put(BEHAVES_LIKE, behaves.get(BEHAVES_LIKE));

                        }
                        if (getDateMap != null && getDateMap.get("AS_CUSTOMER_COMES") != null && getDateMap.get("AS_CUSTOMER_COMES").equals("Y")) {

                            List lst = null;
                            if (list != null && list.size() > 0) {
                                if (prodType.equals(TransactionFactory.LOANS)) {
                                    lst = (List) sqlMap.executeQueryForList("getIntDetails", interestMap);
                                    //                            prodType=TransactionFactory.LOANS;
                                }
                                if (prodType.equals(TransactionFactory.ADVANCES)) {
                                    lst = sqlMap.executeQueryForList("getIntDetailsAD", interestMap);
                                    //                            prodType=TransactionFactory.ADVANCES;
                                }
                                if (lst != null && lst.size() > 0) {
                                    interestMap = (HashMap) lst.get(0);
                                    insetMap.put("ACCOUNTNO", acctNum);
                                    insetMap.put("TRANSTYPE", TransactionDAOConstants.CREDIT);
                                    insetMap.put(CommonConstants.BRANCH_ID, _branchCode);
                                    transSlNo = CommonUtil.convertObjToLong(interestMap.get("TRANS_SLNO"));
                                }
                                if (cashTo.getProdType().equals(TransactionFactory.LOANS)) {
                                    //EMI ONLY UPDATE STATUS
                                    if (cashTo.getAuthorizeRemarks() != null && (CommonUtil.convertObjToLong(cashTo.getAuthorizeRemarks())) > 0) {
                                        HashMap map = new HashMap();
                                        long count = CommonUtil.convertObjToLong(cashTo.getAuthorizeRemarks());
                                        map.put(CommonConstants.ACT_NUM, cashTo.getLinkBatchId());
                                        List installmentLst = sqlMap.executeQueryForList("getMinimaminstallmentTL", map);
                                        if (installmentLst != null && installmentLst.size() > 0) {
                                            map = (HashMap) installmentLst.get(0);
                                            int instalmentNo = CommonUtil.convertObjToInt(map.get("INSTALLMENT_SLNO"));
                                            HashMap updateMap = new HashMap();
                                            for (int i = 0; i < count; i++) {
                                                updateMap = new HashMap();
                                                updateMap.put("ACCT_NUM", cashTo.getLinkBatchId());
                                                updateMap.put("INSTALLMENT_NO", new Integer(instalmentNo));
                                                System.out.println("updateMap###" + updateMap);
                                                sqlMap.executeUpdate("updateloanInstallment", updateMap);
                                                instalmentNo++;
                                            }
                                            sqlMap.executeUpdate("updateEMIlastIntCalc", updateMap);
                                            updateMap = null;
                                        }
                                        map = null;
                                    }
                                    //END
                                }

                                if (cashTo.getProdType().equals(TransactionFactory.GL)) {
                                    System.out.println("interestMap###" + interestMap);
                                    double amount = cashTo.getAmount().doubleValue();
                                    transSlNo++;
                                    boolean otherCharges = true;
                                    if (cashTo.getAuthorizeRemarks() != null && (cashTo.getAuthorizeRemarks().equals("INTEREST") || cashTo.getAuthorizeRemarks().equals("INTEREST_WAIVEOFF"))) {
                                        if (cashTo.getAuthorizeRemarks().equals("INTEREST_WAIVEOFF")) {
                                            updateWaiveOffInterestPenalDetails(cashTo);
                                        }
                                        insetMap.put("IBAL", new Double(0));
                                        insetMap.put("INTEREST", amount);
                                        otherCharges = false;
                                    } else {
                                        insetMap.put("IBAL", new Double(0));
                                        insetMap.put("INTEREST", new Double(0));
                                    }
                                    if (cashTo.getAuthorizeRemarks() != null && (cashTo.getAuthorizeRemarks().equals("PENAL_INT") || cashTo.getAuthorizeRemarks().equals("PENAL_WAIVEOFF"))) {
                                        if (cashTo.getAuthorizeRemarks().equals("PENAL_WAIVEOFF")) {
                                            updateWaiveOffInterestPenalDetails(cashTo);
                                        }
                                        insetMap.put("PENAL", amount);
                                        insetMap.put("PIBAL", new Double(0));
                                        otherCharges = false;
                                    } else {
                                        insetMap.put("PENAL", new Double(0));
                                        insetMap.put("PIBAL", new Double(0));
                                    }
                                    //account closing charge account closing misc
                                    if (cashTo.getAuthorizeRemarks() != null && (cashTo.getAuthorizeRemarks().equals("ACT CLOSING CHARGE") || cashTo.getAuthorizeRemarks().equals("ACT CLOSING MISC CHARGE"))) {

                                        insetMap.put("EBAL", new Double(0));
                                        insetMap.put("EXPENSE", amount);
                                        otherCharges = false;
                                    } else {
                                        insetMap.put("EBAL", new Double(0));
                                        insetMap.put("EXPENSE", new Double(0));
                                    }
                                    //                                //account closing misc
                                    //                                 if(cashTo.getAuthorizeRemarks()!=null && cashTo.getAuthorizeRemarks().equals("INTEREST")){
                                    //
                                    //                                    insetMap.put("IBAL",new Double(0));
                                    //                                    insetMap.put("INTEREST",new Double(amount));
                                    //                                }else{
                                    //                                    insetMap.put("IBAL",new Double(0));
                                    //                                    insetMap.put("INTEREST",new Double(0));
                                    //                                }
                                    //postage
                                    if (cashTo.getAuthorizeRemarks() != null && cashTo.getAuthorizeRemarks().equals("POSTAGE CHARGES")) {
                                        insetMap.put("POSTAGE_CHARGE", amount);
                                        insetMap.put("POSTAGE_CHARGE_BAL", new Double(0));
                                        chargesCollected(cashTo.getLinkBatchId(), cashTo.getAuthorizeRemarks(), amount, null);
                                        otherCharges = false;
                                    } else {
                                        insetMap.put("POSTAGE_CHARGE", new Double(0));
                                        insetMap.put("POSTAGE_CHARGE_BAL", new Double(0));
                                    }
                                    //advertise
                                    if (cashTo.getAuthorizeRemarks() != null && cashTo.getAuthorizeRemarks().equals("ADVERTISE CHARGES")) {
                                        insetMap.put("ADVERTISE_CHARGES", amount);
                                        insetMap.put("ADVERTISE_CHARGES_BAL", new Double(0));
                                        chargesCollected(cashTo.getLinkBatchId(), cashTo.getAuthorizeRemarks(), amount, null);
                                        otherCharges = false;
                                    } else {
                                        insetMap.put("ADVERTISE_CHARGES", new Double(0));
                                        insetMap.put("ADVERTISE_CHARGES_BAL", new Double(0));
                                    }

                                    //orbitary
                                    if (cashTo.getAuthorizeRemarks() != null && cashTo.getAuthorizeRemarks().equals("ARBITRARY CHARGES")) {
                                        insetMap.put("ARBITARY_CHARGE", amount);
                                        insetMap.put("ARBITARY_CHARGE_BAL", new Double(0));
                                        chargesCollected(cashTo.getLinkBatchId(), cashTo.getAuthorizeRemarks(), amount, null);
                                        otherCharges = false;
                                    } else {
                                        insetMap.put("ARBITARY_CHARGE", new Double(0));
                                        insetMap.put("ARBITARY_CHARGE_BAL", new Double(0));
                                    }
                                    //insurance
                                    if (cashTo.getAuthorizeRemarks() != null && cashTo.getAuthorizeRemarks().equals("INSURANCE CHARGES")) {
                                        insetMap.put("INSURANCE_CHARGE", amount);
                                        insetMap.put("INSURANCE_CHARGE_BAL", new Double(0));
                                        chargesCollected(cashTo.getLinkBatchId(), cashTo.getAuthorizeRemarks(), amount, null);
                                        otherCharges = false;
                                    } else {
                                        insetMap.put("INSURANCE_CHARGE", new Double(0));
                                        insetMap.put("INSURANCE_CHARGE_BAL", new Double(0));
                                    }
                                    //execDegree
                                    if (cashTo.getAuthorizeRemarks() != null && cashTo.getAuthorizeRemarks().equals("EXECUTION DECREE CHARGES")) {
                                        insetMap.put("EXE_DEGREE", amount);
                                        insetMap.put("EXE_DEGREE_BAL", new Double(0));
                                        chargesCollected(cashTo.getLinkBatchId(), cashTo.getAuthorizeRemarks(), amount, null);
                                        otherCharges = false;
                                    } else {
                                        insetMap.put("EXE_DEGREE", new Double(0));
                                        insetMap.put("EXE_DEGREE_BAL", new Double(0));
                                    }
                                    //misc
                                    if (cashTo.getAuthorizeRemarks() != null && cashTo.getAuthorizeRemarks().equals("MISCELLANEOUS CHARGES")) {
                                        insetMap.put("MISC_CHARGES", amount);
                                        insetMap.put("MISC_CHARGES_BAL", new Double(0));
                                        chargesCollected(cashTo.getLinkBatchId(), cashTo.getAuthorizeRemarks(), amount, null);
                                        otherCharges = false;
                                    } else {
                                        insetMap.put("MISC_CHARGES", new Double(0));
                                        insetMap.put("MISC_CHARGES_BAL", new Double(0));
                                    }
                                    //legal
                                    if (cashTo.getAuthorizeRemarks() != null && cashTo.getAuthorizeRemarks().equals("LEGAL CHARGES")) {
                                        insetMap.put("LEGAL_CHARGE", amount);
                                        insetMap.put("LEGAL_CHARGE_BAL", new Double(0));
                                        chargesCollected(cashTo.getLinkBatchId(), cashTo.getAuthorizeRemarks(), amount, null);
                                        otherCharges = false;
                                    } else {
                                        insetMap.put("LEGAL_CHARGE", new Double(0));
                                        insetMap.put("LEGAL_CHARGE_BAL", new Double(0));
                                    }
                                    //legal
                                    if (cashTo.getAuthorizeRemarks() != null && cashTo.getAuthorizeRemarks().equals("EA")) {
                                        insetMap.put("EXPENSE", amount);
                                        insetMap.put("EBAL", new Double(0));
                                        chargesCollected(cashTo.getLinkBatchId(), cashTo.getAuthorizeRemarks(), amount, cashTo.getParticulars());
                                        otherCharges = false;
                                    } else if (cashTo.getAuthorizeRemarks() != null && cashTo.getAuthorizeRemarks().equals("EP")) {
                                        insetMap.put("EXPENSE", amount);
                                        insetMap.put("EBAL", new Double(0));
                                        chargesCollected(cashTo.getLinkBatchId(), cashTo.getAuthorizeRemarks(), amount, cashTo.getParticulars());
                                        otherCharges = false;
                                    } else if (cashTo.getAuthorizeRemarks() != null && cashTo.getAuthorizeRemarks().equals("ARC")) {
                                        insetMap.put("EXPENSE", amount);
                                        insetMap.put("EBAL", new Double(0));
                                        chargesCollected(cashTo.getLinkBatchId(), cashTo.getAuthorizeRemarks(), amount, cashTo.getParticulars());
                                        otherCharges = false;
                                    }



                                    System.out.println("insetMap#####" + insetMap);

                                    if (otherCharges) {
                                        chargesCollected(cashTo.getLinkBatchId(), cashTo.getAuthorizeRemarks(), amount, cashTo.getParticulars());
                                        insetMap.put("EXPENSE", amount);
                                    }

                                    amount = CommonUtil.convertObjToDouble(interestMap.get("PBAL")).doubleValue();
                                    insetMap.put("PBAL", amount);
                                    insetMap.put("PRINCIPAL", new Double(0));
                                    amount = CommonUtil.convertObjToDouble(insetMap.get("INTEREST")).doubleValue();
                                    insetMap.put("INTEREST", amount);
                                    amount = CommonUtil.convertObjToDouble(interestMap.get("EBAL")).doubleValue();
                                    insetMap.put("EBAL", amount);
                                    insetMap.put("TODAY_DT", currDt);
                                    insetMap.put("TRANS_SLNO", new Long(transSlNo));
                                    insetMap.put("PRINCIPAL", new Double(0));//new Double(0));
                                    insetMap.put("NPA_INTEREST", new Double(0));
                                    insetMap.put("NPA_INT_BAL", new Double(0));
                                    insetMap.put("NPA_PENAL", new Double(0));
                                    insetMap.put("NPA_PENAL_BAL", new Double(0));
                                    insetMap.put("EXCESS_AMT", new Double(0));
                                    insetMap.put(AUTHORIZE_STATUS, "AUTHORIZED");
                                    //                                insetMap.put("EXPENSE",new Double(0));
                                    //                                insetMap.put("EBAL",new Double(0));
//                                    insetMap.put(CommonConstants.TRANS_ID,String .valueOf("GL TRANS"));
//                                    insetMap.put("PARTICULARS",String .valueOf("GL TRANSACTION"));
                                    insetMap.put(CommonConstants.TRANS_ID, cashTo.getTransId());
                                    if (otherCharges) {
                                        insetMap.put("PARTICULARS", cashTo.getAuthorizeRemarks() == null
                                                ? cashTo.getParticulars() : cashTo.getAuthorizeRemarks());
                                    } else {
                                        insetMap.put("PARTICULARS", cashTo.getParticulars());
                                    }
                                    insetMap.put("TRANS_MODE", "CASH");
                                    insetMap.put("TRN_CODE", String.valueOf("C*"));
                                    String uptoDtYN = "N";
                                    //                                uptoDtYN=asAnWhenCustomer(getDateMap);
                                    insetMap.put("UPTO_DT_INT", String.valueOf(uptoDtYN));
                                    System.out.println("insetMap######" + insetMap);
                                    if (prodType.equals(TransactionFactory.LOANS)) {
                                        sqlMap.executeUpdate("insertLoansDisbursementDetailsCumLoan", insetMap);
                                    }
                                    if (prodType.equals(TransactionFactory.ADVANCES)) {
                                        sqlMap.executeUpdate("insertAuthorizeAdvTransDetails", insetMap);
                                    }

                                    //                                asAnWhenCustomer(getDateMap);
                                    uptoDtYN = asAnWhenCustomer(getDateMap);
                                    if (uptoDtYN.equals("Y")) {
                                        sqlMap.executeUpdate("updateinterestYes", insetMap);
                                    }
                                    interestMap = null;
                                }
                            }
                            lst = null;
                        }
                    }
                    interestMap = null;
                }
                cashList = null;
                transMap = null;
            } else if (status.equals(CommonConstants.STATUS_REJECTED)) {
                HashMap dataMap = new HashMap();
                String linkbatchId = "";
                dataMap = (HashMap) cashTos.get(0);
                if (!dataMap.containsKey("ACCOUNT NO")) {
                    linkbatchId = CommonUtil.convertObjToStr(dataMap.get("LINK_BATCH_ID"));
                } else {
                    linkbatchId = CommonUtil.convertObjToStr(dataMap.get("ACCOUNT NO"));
                }

                List waiveOffList = (List) sqlMap.executeQueryForList("getSelectTermLoanWaiveOffTO", linkbatchId);
                if (waiveOffList != null && waiveOffList.size() > 0) {
                    TermLoanPenalWaiveOffTO obj = new TermLoanPenalWaiveOffTO();
                    obj = (TermLoanPenalWaiveOffTO) waiveOffList.get(0);
                    obj.setAuthorizeStatus(status);
                    obj.setAuthorizedBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                    obj.setAuthorizedDt(currDt);
                    sqlMap.executeUpdate("updateTermLoanInterestWaiveOffTO", obj);
                }
//                authorizeRebateInterestTransaction(CommonUtil.convertObjToStr(dataMap.get("ACCOUNT NO")), status);


            }
        }
        insetMap = null;
        cashTo = null;
    }

    /*
     * method to get the batch id, will be called once for one batch
     */
    private String generateRebateBatchID() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "REBATE_ID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//for trans_batch_id resetting, branch code
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        String batchID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        return batchID;
    }

    private void interestWaiveoffTransaction(CashTransactionTO cashTO, double waivePenal, double waiveInterest) throws Exception {

        HashMap dataMap = new HashMap();
        HashMap transMap = new HashMap();
        int count = 0;
//        List waiveOffList=(List)sqlMap.executeQueryForList("getSelectTermLoanWaiveOffTO",txTransTO.getLinkBatchId());
//        if(waiveOffList !=null && waiveOffList.size()>0){
//            TermLoanPenalWaiveOffTO obj =new TermLoanPenalWaiveOffTO();
//            obj =(TermLoanPenalWaiveOffTO)waiveOffList.get(0);
        if (waivePenal > 0) {
            transMap.put("WAIVE_PENAL", new Double(waivePenal));
            count++;
        }
        if (waiveInterest > 0) {
            transMap.put("WAIVE_INTEREST", new Double(waiveInterest));
            count++;
        }
        dataMap.put("ACCT_NUM", cashTO.getActNum());
        List lst = (List) sqlMap.executeQueryForList("getInterestAndPenalIntActHead", dataMap);
        if (lst != null && lst.size() > 0) {
            HashMap acHeads = (HashMap) lst.get(0);
            //         System.out.println("#####collectfromloan"+waiveOffMap);
            ArrayList transferList = new ArrayList(); // for local transfer
            TransferTrans trans = new TransferTrans();
            HashMap txMap = new HashMap();
            double penal = 0;
            double interest = 0, transAmt = 0;
            for (int i = 0; i < count; i++) {
                penal = CommonUtil.convertObjToDouble(transMap.get("WAIVE_PENAL")).doubleValue();
                if (penal == 0) {
                    interest = CommonUtil.convertObjToDouble(transMap.get("WAIVE_INTEREST")).doubleValue();
                    transAmt = interest;
                } else {
                    transAmt = penal;
                }

                txMap = new HashMap();
                transferList = new ArrayList(); // for local transfer
                trans = new TransferTrans();
                trans.setTransMode(CommonConstants.TX_TRANSFER);

                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("DEBIT_DISCOUNT_ACHD"));
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                if (penal > 0) {
                    txMap.put(TransferTrans.PARTICULARS, "WaiveOff Penal for " + cashTO.getActNum());
                    txMap.put("AUTHORIZEREMARKS", "PENAL_WAIVEOFF");
                    txMap.put("DR_INST_TYPE", "PENAL_WAIVEOFF");
                    txMap.put("DR_INSTRUMENT_2", "PENAL_WAIVEOFF");
                } else {
                    txMap.put(TransferTrans.PARTICULARS, "WaiveOff Interest for " + cashTO.getActNum());
                    txMap.put("AUTHORIZEREMARKS", "INTEREST_WAIVEOFF");
                    txMap.put("DR_INST_TYPE", "INTEREST_WAIVEOFF");
                    txMap.put("DR_INSTRUMENT_2", "INTEREST_WAIVEOFF");
                }
                transferList.add(trans.getDebitTransferTO(txMap, transAmt));//CommonUtil.convertObjToDouble(obj.getPenalAmt()).doubleValue()));

                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("AC_DEBIT_INT"));
                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                txMap.put(TransferTrans.CURRENCY, "INR");
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                if (penal > 0) {
                    txMap.put(TransferTrans.PARTICULARS, "WaiveOff Penal upto" + CommonUtil.convertObjToStr(properFormatDate));
                } else {
                    txMap.put(TransferTrans.PARTICULARS, "WaiveOff Interest upto" + CommonUtil.convertObjToStr(properFormatDate));
                }
                System.out.println("####### insertAccHead txMap " + txMap);


                trans.setInitiatedBranch(_branchCode);
                trans.setLinkBatchId(CommonUtil.convertObjToStr(cashTO.getActNum()));
                trans.setBreakLoanHierachy("Y");
                transferList.add(trans.getCreditTransferTO(txMap, transAmt));//CommonUtil.convertObjToDouble(obj.getPenalAmt()).doubleValue()));
                trans.doDebitCredit(transferList, _branchCode, false);
//                    obj.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
//                    obj.setAuthorizedBy(user);
//                    obj.setAuthorizedDt(properFormatDate);
                transMap.remove("WAIVE_PENAL");
            }
//                dataMap.put("CURR_DT",properFormatDate);
//                dataMap.put("ACCT_NUM",obj.getAcctNum());
//                dataMap.put("INTEREST_TRANS_AMT",obj.getInterestAmt());
//                dataMap.put("PENAL_TRANS_AMT",obj.getPenalAmt());
//                System.out.println("dataMap####"+dataMap);
//                sqlMap.executeUpdate("updateTermLoanInterestWaiveOffTO",obj);
//                sqlMap.executeUpdate("updateTermLoanFacilityWaiveofDetailsTO",dataMap);




            transferList = null;
            trans = null;
            acHeads = null;
            lst = null;

            txMap = null;
        }
//        }
    }

    private void authorizeRebateInterestTransaction(String linkBatchId, String authorizeStatus) throws Exception {
        TransactionDAO objTransactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        HashMap cashAuthMap = new HashMap();
        HashMap interestMap = new HashMap();
        interestMap.put("ACT_NUM", linkBatchId);
        List list = sqlMap.executeQueryForList("getBehavesLikeTLAD", interestMap);
        if (list != null && list.size() > 0) {
            cashAuthMap.put(CommonConstants.BRANCH_ID, _branchCode);
            cashAuthMap.put(CommonConstants.USER_ID, user);
            cashAuthMap.put("DAILY", "DAILY");
            TransactionDAO.authorizeCashAndTransfer(linkBatchId, authorizeStatus, cashAuthMap);

//        cashAuthMap = null ;
        }
    }

    private void deleteRebateInterestTransaction(String linkBatchId, String status) throws Exception {
        TransactionDAO objTransactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        HashMap cashAuthMap = new HashMap();
        HashMap interestMap = new HashMap();
        ArrayList totList = new ArrayList();
        interestMap.put("ACT_NUM", linkBatchId);
        TransferTrans trans = new TransferTrans();
        trans.setTransMode(CommonConstants.TX_TRANSFER);
        trans.setInitiatedBranch(_branchCode);
        trans.setLinkBatchId(linkBatchId);
        trans.setInitiatedBranch(_branchCode);
        List list = sqlMap.executeQueryForList("getBehavesLikeTLAD", interestMap);
        if (list != null && list.size() > 0) {
            cashAuthMap.put("INITIATED_BRANCH", _branchCode);
            cashAuthMap.put("LINK_BATCH_ID", linkBatchId);
            cashAuthMap.put("TODAY_DT", currDt);
            List lst = sqlMap.executeQueryForList("getTransferTransdistinctBatchID", cashAuthMap);
            if (lst != null && lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    HashMap transMap = (HashMap) lst.get(i);
                    List txList = sqlMap.executeQueryForList("getBatchTxTransferTOs", transMap);
                    for (int j = 0; j < txList.size(); j++) {
                        TxTransferTO txTransTO = (TxTransferTO) txList.get(j);
                        txTransTO.setCommand(status);
                        txTransTO.setStatus("DELETED");
                        totList.add(txTransTO);
                    }
                }
            }
            trans.setTransMode(CommonConstants.TX_TRANSFER);
            trans.setInitiatedBranch(_branchCode);
            if (totList != null && totList.size() > 0) {
                trans.doDebitCredit(totList, _branchCode, false);
            }
            //        cashAuthMap = null ;
        }
    }

    private void updateWaiveOffInterestPenalDetails(CashTransactionTO cashTO) throws Exception {
        HashMap dataMap = new HashMap();
        List waiveOffList = (List) sqlMap.executeQueryForList("getSelectTermLoanWaiveOffTO", cashTO.getLinkBatchId());
        if (waiveOffList != null && waiveOffList.size() > 0) {
            TermLoanPenalWaiveOffTO obj = new TermLoanPenalWaiveOffTO();
            obj = (TermLoanPenalWaiveOffTO) waiveOffList.get(0);
            obj.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
            obj.setAuthorizedBy(user);
            obj.setAuthorizedDt(properFormatDate);
            dataMap.put("CURR_DT", properFormatDate);
            dataMap.put("ACCT_NUM", obj.getAcctNum());

            dataMap.put("INTEREST_TRANS_AMT", obj.getInterestAmt());
            dataMap.put("PENAL_TRANS_AMT", obj.getPenalAmt());

            System.out.println("dataMap####" + dataMap);
            sqlMap.executeUpdate("updateTermLoanInterestWaiveOffTO", obj);
            sqlMap.executeUpdate("updateTermLoanFacilityWaiveofDetailsTO", dataMap);

        }


    }

    private void rebateInterestTransaction(CashTransactionTO cashTO, double rebateInterest, double interest) throws Exception {
        LoanRebateTO rebateTo = new LoanRebateTO();
        HashMap dataMap = new HashMap();
//        List waiveOffList=(List)sqlMap.executeQueryForList("getSelectTermLoanWaiveOffTO",cashTO.getLinkBatchId());
//        if(waiveOffList !=null && waiveOffList.size()>0){
//            TermLoanPenalWaiveOffTO obj =new TermLoanPenalWaiveOffTO();
//            obj =(TermLoanPenalWaiveOffTO)waiveOffList.get(0);
        dataMap.put("ACCT_NUM", cashTO.getActNum());
        List lst = (List) sqlMap.executeQueryForList("getInterestAndPenalIntActHead", dataMap);
        if (lst != null && lst.size() > 0) {
            HashMap acHeads = (HashMap) lst.get(0);
            //         System.out.println("#####collectfromloan"+waiveOffMap);

            HashMap txMap = new HashMap();

            txMap = new HashMap();
            ArrayList transferList = new ArrayList(); // for local transfer
            TransferTrans trans = new TransferTrans();
            trans.setTransMode(CommonConstants.TX_TRANSFER);
            trans.setInitiatedBranch(_branchCode);
            trans.setLinkBatchId(CommonUtil.convertObjToStr(cashTO.getLinkBatchId()));
            trans.setInitiatedBranch(_branchCode);
            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("REBATE_INTEREST_ACHD"));
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
            txMap.put(TransferTrans.PARTICULARS, "Rebate interest  " + cashTO.getActNum());
            txMap.put("AUTHORIZEREMARKS", "REBATE_INTEREST");
            txMap.put("DR_INST_TYPE", "REBATE_INTEREST");
            txMap.put("DR_INSTRUMENT_2", "REBATE_INTEREST");
            transferList.add(trans.getDebitTransferTO(txMap, rebateInterest));

            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("AC_DEBIT_INT"));
            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
            txMap.put(TransferTrans.CURRENCY, "INR");
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.PARTICULARS, "Rebate interest upto " + CommonUtil.convertObjToStr(currDt));
            if (interest > 0) {
                transferList.add(trans.getCreditTransferTO(txMap, interest));
            } else {
                transferList.add(trans.getCreditTransferTO(txMap, rebateInterest));
            }
            if (interest > 0) {
                txMap.put(TransferTrans.CR_ACT_NUM, cashTO.getActNum());
                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("ACCT_HEAD"));
                txMap.put(TransferTrans.CR_PROD_ID, cashTO.getProdId());
                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                txMap.put(TransferTrans.CURRENCY, "INR");
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
                txMap.put("DR_INST_TYPE", "REBATE_PRINCIPAL");
                txMap.put("DR_INSTRUMENT_2", "REBATE_PRINCIPAL");
                txMap.put("AUTHORIZEREMARKS", "REBATE_PRINCIPAL");
                txMap.put(TransferTrans.PARTICULARS, "Rebate interest paid to principal upto " + CommonUtil.convertObjToStr(currDt));
                transferList.add(trans.getCreditTransferTO(txMap, rebateInterest - interest));
            }

            System.out.println("####### insertAccHead txMap " + txMap);

            trans.setInitiatedBranch(_branchCode);
            trans.setLinkBatchId(CommonUtil.convertObjToStr(cashTO.getActNum()));
//                transferList.add(trans.getCreditTransferTO(txMap,rebateInterest));
            trans.setBreakLoanHierachy("Y");
            trans.doDebitCredit(transferList, _branchCode, false);
            rebateTo.setAccNo(CommonUtil.convertObjToStr(cashTO.getActNum()));
            rebateTo.setBranchCode(_branchCode);

            rebateTo.setIntAmount(String.valueOf(rebateInterest));
            rebateTo.setStatus(CommonConstants.STATUS_CREATED);
            rebateTo.setStatusDt((Date) currDt.clone());
            rebateTo.setStatusBy(user);
            rebateTo.setRebateId(generateRebateBatchID());

            sqlMap.executeUpdate("insertLoanRebate", rebateTo);

//                #accNo:VARCHAR#, #accName:VARCHAR#, #rebateInt:VARCHAR#, #intAmount:VARCHAR#,#status:VARCHAR#,#statusBy:VARCHAR#,#statusDt:DATE#,
//                #authorizeStatus:VARCHAR#,#authorizeBy:VARCHAR#,#rebateId:VARCHAR#,#branchCode:VARCHAR#)

            transferList = null;
            trans = null;
            acHeads = null;
            lst = null;

            txMap = null;
        }
//        }
    }

    //as an whenc customer updation of charges
    private void chargesCollected(String act_num, String chargeType, double amount, String caseAccountParticulares) throws Exception {
        HashMap chargeMap = new HashMap();
        chargeMap.put(CommonConstants.ACT_NUM, act_num);
        chargeMap.put("CHARGE_TYPE", chargeType);
        double amt = amount;
        List lst = sqlMap.executeQueryForList("getChargeDetailsforUpdate", chargeMap);
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                chargeMap = (HashMap) lst.get(i);
                chargeMap.put("CHARGE_NO", CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_NO"))); //avoid class cast exception
                chargeMap.put("CHARGE_TYPE", CommonUtil.convertObjToStr(chargeMap.get("CHARGE_TYPE"))); //avoid class cast exception
                chargeMap.put("ACT_NUM", CommonUtil.convertObjToStr(chargeMap.get("ACT_NUM"))); //avoid class cast exception
                double paidAmt = CommonUtil.convertObjToDouble(chargeMap.get("PAID_AMT")).doubleValue();
                double payableAmt = CommonUtil.convertObjToDouble(chargeMap.get("PAYABLE_AMT")).doubleValue();
                if (amt > 0) {
                    if (amt > payableAmt) {
                        amt -= payableAmt;
                        chargeMap.put("PAID_AMT", new Double(paidAmt + payableAmt));
                    } else {
                        chargeMap.put("PAID_AMT", new Double(paidAmt + amt));
                        amt = 0;
                    }
                    sqlMap.executeUpdate("updateChargeDetails", chargeMap);
                }
            }
        }

//       lst= sqlMap.executeQueryForList("getSelectTermLoanCaseChargeDetails",chargeMap);
//        if(lst!=null && lst.size()>0){
//            boolean isExpence=false;
//             System.out.println("caseAccountParticularesbefore####"+caseAccountParticulares);
//             if(caseAccountParticulares !=null)
//             caseAccountParticulares=caseAccountParticulares.substring(caseAccountParticulares.length()-7 ,caseAccountParticulares.length());
//            if(caseAccountParticulares.equals("EXPENCE"))
//                  isExpence=true;
//            System.out.println("caseAccountParticulares####"+caseAccountParticulares);
//            for(int i=0;i<lst.size();i++){
//                chargeMap=(HashMap)lst.get(i);
//                chargeMap.put("CHARGE_TYPE",chargeType);
//                if(!chargeType.equals(CommonUtil.convertObjToStr(chargeMap.get("CASE_STATUS"))))
//                    continue;
//                double paidAmt=CommonUtil.convertObjToDouble(chargeMap.get("PAID_FILING_FEES")).doubleValue();
//                double payableAmt=CommonUtil.convertObjToDouble(chargeMap.get("FILING_FEES")).doubleValue();
//                if(caseAccountParticulares !=null && caseAccountParticulares.endsWith("EXPENCE")){
//                     paidAmt=CommonUtil.convertObjToDouble(chargeMap.get("PAID_MISC_FEES")).doubleValue();
//                     payableAmt=CommonUtil.convertObjToDouble(chargeMap.get("MISC_CHARGES")).doubleValue();
//                }
//                if(amt>0){
//                    if(amt>payableAmt){
//                        amt-=payableAmt;
//                        if(isExpence){
//                        chargeMap.put("PAID_MISC_FEES",new Double(paidAmt+payableAmt));
////                        chargeMap.put("PAID_FILING_FESS_AMT",new Double(0));
//                        }else{
//                             chargeMap.put("PAID_FILING_FESS_AMT",new Double(paidAmt+payableAmt));
////                              chargeMap.put("PAID_MISC_FEES",new Double(0));
//                        }
//                    }else {
//                        if(isExpence)
//                            chargeMap.put("PAID_MISC_FEES",new Double(paidAmt+amt));
//                        else
//                            chargeMap.put("PAID_FILING_FESS_AMT",new Double(paidAmt+amt));
////                        chargeMap.put("PAID_AMT",new Double(paidAmt+amt));
//                        amt=0;
//                    }
//                    System.out.println("chargeMap#####"+chargeMap);
//                    if(isExpence)
//                        sqlMap.executeUpdate("updatePaidCaseExpenceAmt", chargeMap);
//                    else
//                        sqlMap.executeUpdate("updatePaidCaseFileAmt", chargeMap);
//                }
//            }
//        }
        chargeMap = null;
    }

    private String asAnWhenCustomer(HashMap authorizeMap) throws Exception {
        System.out.println("authorizeMap#####" + authorizeMap);
        HashMap getDateMap = new HashMap();
        TaskHeader header = new TaskHeader();
        header.setBranchID(_branchCode);
        getDateMap.put("ACCT_NUM", authorizeMap.get(CommonConstants.ACT_NUM));
        getDateMap.put(PROD_ID, authorizeMap.get(TransactionDAOConstants.PROD_ID));
        getDateMap.put("BRANCH_ID", _branchCode);
        //        HashMap depositcummap=(HashMap)((List)sqlMap.executeQueryForList("getDepositBehavesforLoan",getDateMap));
        //        if(depositcummap !=null && depositcummap.size()>0 && depositcummap.containsKey(BEHAVES_LIKE) && (!depositcummap.get(BEHAVES_LIKE).equals("CUMMULATIVE"))){

        InterestCalculationTask interestCalTask = new InterestCalculationTask(header);
        authorizeMap.put("WHERE", authorizeMap.get(CommonConstants.ACT_NUM));
        String mapNameForLastIntCalcDt = "getLastIntCalDate";
        if (CommonUtil.convertObjToStr(authorizeMap.get(BEHAVES_LIKE)).equals("OD")) {
            mapNameForLastIntCalcDt = "getLastIntCalDateAD";
        }
        List lst = (List) sqlMap.executeQueryForList(mapNameForLastIntCalcDt, authorizeMap);
//        List lst=(List)sqlMap.executeQueryForList("getLastIntCalDate",authorizeMap);
        getDateMap = (HashMap) lst.get(0);
        insertInterestDetails(getDateMap);

        if (getDateMap != null && getDateMap.containsKey("INSTALL_TYPE") && getDateMap.get("INSTALL_TYPE").equals("EMI")) {
            return "N";
        }
        authorizeMap.put(PROD_ID, authorizeMap.get(PROD_ID));
        Date CURR_DATE = new Date();
        CURR_DATE = (Date) currDt.clone();
        System.out.println("curr_date###1" + CURR_DATE);
        getDateMap.put("CURR_DATE", CURR_DATE);
        getDateMap.put("BRANCH_ID", _branchCode);
        getDateMap.put(CommonConstants.BRANCH_ID, _branchCode);
        //            HashMap behaveLike=(HashMap)(sqlMap.executeQueryForList("getLoanBehaves",authorizeMap).get(0));
        getDateMap.put(CommonConstants.ACT_NUM, authorizeMap.get(CommonConstants.ACT_NUM));
        getDateMap.put(BEHAVES_LIKE, authorizeMap.get(BEHAVES_LIKE));
        getDateMap.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
        System.out.println("before od interest calculation####" + getDateMap);
        HashMap resultMap = new HashMap();
        resultMap = interestCalTask.interestCalcTermLoanAD(getDateMap); // we need same used for TL also
        double penalInt = 0;
        double interest = 0;
        if (resultMap != null && resultMap.containsKey("LOAN_CLOSING_PENAL_INT")) {
            penalInt = CommonUtil.convertObjToDouble(resultMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
        }
        if (resultMap != null && resultMap.containsKey("INTEREST")) {
            interest = CommonUtil.convertObjToDouble(resultMap.get("INTEREST")).doubleValue();
        }
        //calculated interest
        double totCalInterest = penalInt + interest;
        HashMap accDetailMap = new HashMap();
        if (totCalInterest > 0) {
            accDetailMap.put(CommonConstants.ACT_NUM, getDateMap.get(CommonConstants.ACT_NUM));
            accDetailMap.put("FROM_DT", getDateMap.get("LAST_INT_CALC_DT"));
            accDetailMap.put("FROM_DT", DateUtil.addDays(((Date) accDetailMap.get("FROM_DT")), 2));
            accDetailMap.put("TO_DATE", getDateMap.get("CURR_DATE"));
            //paid interest
            if (resultMap.containsKey("BEHAVES_LIKE") && CommonUtil.convertObjToStr(resultMap.get("BEHAVES_LIKE")).equals("OD")) {
                lst = sqlMap.executeQueryForList("getPaidPrincipleAD", accDetailMap);
            } else {
                lst = sqlMap.executeQueryForList("getPaidPrinciple", accDetailMap);
            }
            accDetailMap = (HashMap) lst.get(0);
            double paidInt = CommonUtil.convertObjToDouble(accDetailMap.get("INTEREST")).doubleValue();
            double paidPenalInt = CommonUtil.convertObjToDouble(accDetailMap.get("PENAL")).doubleValue();
            if (resultMap.containsKey("FUTURE_LAST_INT_CALC_DT")) {
                accDetailMap.put("LAST_CALC_DT", resultMap.get("FUTURE_LAST_INT_CALC_DT"));
            } else {
                accDetailMap.put("LAST_CALC_DT", DateUtil.addDaysProperFormat(CURR_DATE, -1));
            }
            accDetailMap.put(TransactionDAOConstants.ACCT_NO, getDateMap.get(CommonConstants.ACT_NUM));
            System.out.println("accDetailMap    " + accDetailMap);

            if (totCalInterest <= (paidInt + paidPenalInt)) {
                sqlMap.executeUpdate("updateclearBal", accDetailMap);
                accDetailMap = null;
                header = null;
                getDateMap = null;
                lst = null;
                interestCalTask = null;
                resultMap = null;
                return "Y";
            }
        }
        accDetailMap = null;
        header = null;
        getDateMap = null;
        resultMap = null;
        interestCalTask = null;
        lst = null;
        return "N";
    }

    /**
     * Generates BatchId
     */
    private String getBatchId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, BATCH_ID);
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void updateProcessingChargesFormOperative(HashMap procHash) throws Exception {
        System.out.println("#####collectfromoperative" + procHash);
        double procAmt = CommonUtil.convertObjToDouble(procHash.get("PROC_AMT")).doubleValue();
        HashMap txMap = new HashMap();
        if (procAmt > 0) {
            txMap = new HashMap();
            ArrayList transferList = new ArrayList(); // for local transfer
            txMap.put(TransferTrans.DR_PROD_ID, (String) procHash.get("OA_PROD_ID"));
            txMap.put(TransferTrans.DR_ACT_NUM, (String) procHash.get("OA_ACT_NUM"));
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
            List lst = (List) sqlMap.executeQueryForList("getProcChargeAcHd", procHash);
            HashMap acHeads = (HashMap) lst.get(0);
            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("PROC_CHRG"));
            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
            txMap.put(TransferTrans.CURRENCY, "INR");
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            System.out.println("####### insertAccHead txMap " + txMap);
            TransferTrans trans = new TransferTrans();
            trans.setTransMode(CommonConstants.TX_TRANSFER);
            trans.setInitiatedBranch(_branchCode);
            trans.setLinkBatchId(CommonUtil.convertObjToStr(procHash.get(LINK_BATCH_ID)));
            transferList.add(trans.getDebitTransferTO(txMap, procAmt));
            transferList.add(trans.getCreditTransferTO(txMap, procAmt));
            trans.doDebitCredit(transferList, _branchCode, false);
            trans = null;
            transferList = null;
            acHeads = null;
        }
        txMap = null;
    }

    private HashMap depositInterestTransfer(HashMap depIntMap) throws Exception {
        System.out.println("##### DEP_INTEREST_AMT :" + depIntMap);
        double balPay = 0.0;
        double balPayable = 0.0;
        double balInt = 0.0;

        double intAmt = CommonUtil.convertObjToDouble(depIntMap.get("AMOUNT")).doubleValue();
        String depositSubNo = CommonUtil.convertObjToStr(depIntMap.get(DEPOSIT_NO));
        String branchID = CommonUtil.convertObjToStr(depIntMap.get(CommonConstants.BRANCH_ID));
        if (depositSubNo.lastIndexOf("_") != -1) {
            depositSubNo = depositSubNo.substring(0, depositSubNo.lastIndexOf("_"));
        }
        HashMap balanceMap = new HashMap();
        balanceMap.put(DEPOSIT_NO, depositSubNo);
        List lst = sqlMap.executeQueryForList("getTotalIntBalanceForDeposit", balanceMap);
        if (lst.size() > 0) {
            balanceMap = (HashMap) lst.get(0);
            System.out.println("##### balanceMap :" + balanceMap);
            HashMap behavesMap = new HashMap();
            behavesMap.put(PROD_ID, depIntMap.get(PROD_ID));
            lst = sqlMap.executeQueryForList("getBehavesLikeForDeposit", behavesMap);
            if (lst.size() > 0) {
                behavesMap = (HashMap) lst.get(0);
            }
            if (behavesMap.get(BEHAVES_LIKE).equals("FIXED")) {
                double totIntAmt = CommonUtil.convertObjToDouble(balanceMap.get("TOT_INT_AMT")).doubleValue();
                double totIntCredit = CommonUtil.convertObjToDouble(balanceMap.get("TOTAL_INT_CREDIT")).doubleValue();
                double totIntDrawn = CommonUtil.convertObjToDouble(balanceMap.get("TOTAL_INT_DRAWN")).doubleValue();
                balInt = totIntAmt - totIntCredit;
                balPayable = totIntCredit - totIntDrawn;
                balPay = intAmt - balPayable;
            } else {
                balPay = intAmt;
            }
            lst = sqlMap.executeQueryForList("getDepositClosingHeads", depIntMap);
            if (lst.size() > 0) {
                depIntMap = (HashMap) lst.get(0);
                if (balPay > 0) {
                    HashMap txMap = new HashMap();
                    ArrayList transferList = new ArrayList();
                    txMap.put(TransferTrans.DR_AC_HD, (String) depIntMap.get("INT_PROV_ACHD"));//debiting to int paid a/c head
                    txMap.put(TransferTrans.PARTICULARS, depositSubNo + "_1");
                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);

                    txMap.put(TransferTrans.CR_AC_HD, (String) depIntMap.get("INT_PAY")); // Debited to interest payable account head......
                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                    txMap.put(TransferTrans.PARTICULARS, depositSubNo + "_1");
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.DR_INSTRUMENT_1, "INTEREST_AMT");
                    System.out.println("DEP_INTEREST_AMT txMap:" + txMap);

                    TransferTrans trans = new TransferTrans();
                    trans.setTransMode(CommonConstants.TX_TRANSFER);
                    trans.setLinkBatchId(CommonUtil.convertObjToStr(depositSubNo + "_1"));
                    trans.setInitiatedBranch(_branchCode);
                    transferList.add(trans.getDebitTransferTO(txMap, balPay));
                    transferList.add(trans.getCreditTransferTO(txMap, balPay));
                    trans.doDebitCredit(transferList, _branchCode, false);
                    txMap = null;
                    trans = null;
                    transferList = null;
                }
            }
            behavesMap = null;
        }
        lst = null;
        depIntMap.put(DEPOSIT_NO, depositSubNo + "_1");
        depIntMap.put(CommonConstants.BRANCH_ID, branchID);
        balPay = balPay + balPayable;
        depIntMap.put("BALANCE_AMT", new Double(balPay));
        return depIntMap;
    }

    private HashMap interestTransferFD(HashMap depIntMap) throws Exception {
        System.out.println("##### DEP_INTEREST_AMT :" + depIntMap);
        double balPay = 0.0;
        double balPayable = 0.0;
        double balInt = 0.0;

        double intAmt = CommonUtil.convertObjToDouble(depIntMap.get("AMOUNT")).doubleValue();
        String depositSubNo = CommonUtil.convertObjToStr(depIntMap.get(CommonConstants.ACT_NUM));
        String branchID = CommonUtil.convertObjToStr(depIntMap.get(CommonConstants.BRANCH_ID));
        if (depositSubNo.lastIndexOf("_") != -1) {
            depositSubNo = depositSubNo.substring(0, depositSubNo.lastIndexOf("_"));
        }
        HashMap balanceMap = new HashMap();
        balanceMap.put(DEPOSIT_NO, depositSubNo);
        List lst = sqlMap.executeQueryForList("getTotalIntBalanceForDeposit", balanceMap);
        if (lst.size() > 0) {
            balanceMap = (HashMap) lst.get(0);
            System.out.println("##### balanceMap :" + balanceMap);
            HashMap behavesMap = new HashMap();
            behavesMap.put(PROD_ID, depIntMap.get(PROD_ID));
            lst = sqlMap.executeQueryForList("getBehavesLikeForDeposit", behavesMap);
            if (lst.size() > 0) {
                behavesMap = (HashMap) lst.get(0);
            }
            if (behavesMap.get(BEHAVES_LIKE).equals("FIXED")) {
                double totIntAmt = CommonUtil.convertObjToDouble(balanceMap.get("TOT_INT_AMT")).doubleValue();
                double totIntCredit = CommonUtil.convertObjToDouble(balanceMap.get("TOTAL_INT_CREDIT")).doubleValue();
                double totIntDrawn = CommonUtil.convertObjToDouble(balanceMap.get("TOTAL_INT_DRAWN")).doubleValue();
                balInt = totIntAmt - totIntCredit;
                balPayable = totIntCredit - totIntDrawn;
                balPay = intAmt - balPayable;
            } else {
                balPay = intAmt;
            }
            if (behavesMap.get(BEHAVES_LIKE).equals("FIXED")) {
                lst = sqlMap.executeQueryForList("getDepositClosingHeads", depIntMap);
                if (lst.size() > 0) {
                    depIntMap = (HashMap) lst.get(0);
                    if (balPay > 0) {
                        HashMap txMap = new HashMap();
                        ArrayList transferList = new ArrayList();
                        txMap.put(TransferTrans.DR_AC_HD, (String) depIntMap.get("INT_PROV_ACHD"));//debiting to int paid a/c head
                        txMap.put(TransferTrans.PARTICULARS, depositSubNo + "_1");
                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);

                        txMap.put(TransferTrans.CR_AC_HD, (String) depIntMap.get("INT_PAY")); // Debited to interest payable account head......
                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                        txMap.put(TransferTrans.PARTICULARS, depositSubNo + "_1");
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        System.out.println("DEP_INTEREST_AMT txMap:" + txMap);

                        TransferTrans trans = new TransferTrans();
                        trans.setTransMode(CommonConstants.TX_TRANSFER);
                        trans.setLinkBatchId(CommonUtil.convertObjToStr(depositSubNo + "_1"));
                        trans.setInitiatedBranch(_branchCode);
                        transferList.add(trans.getDebitTransferTO(txMap, balPay));
                        transferList.add(trans.getCreditTransferTO(txMap, balPay));
                        trans.doDebitCredit(transferList, _branchCode, false);
                        txMap = null;
                        trans = null;
                        transferList = null;
                    }
                }
                depIntMap.put(DEPOSIT_NO, depositSubNo + "_1");
                depIntMap.put(CommonConstants.BRANCH_ID, branchID);
                balPay = balPay + balPayable;
                depIntMap.put("BALANCE_AMT", new Double(balPay));
                HashMap fdPaymentMap = new HashMap();
                fdPaymentMap.put(DEPOSIT_NO, depositSubNo);
                fdPaymentMap.put("FD_CASH_PAYMENT", "Y");
                sqlMap.executeUpdate("updateFDCashPayment", fdPaymentMap);
            }
            behavesMap = null;
        }
        lst = null;
        balanceMap = null;
        return depIntMap;
    }

    private void updateProcessingChargesFromLoan(HashMap procHash) throws Exception {
        System.out.println("#####collectfromloan" + procHash);
        double procAmt = CommonUtil.convertObjToDouble(procHash.get("PROC_AMT")).doubleValue();
        HashMap txMap = new HashMap();
        if (procAmt > 0) {
            txMap = new HashMap();
            ArrayList transferList = new ArrayList(); // for local transfer
            txMap.put(TransferTrans.DR_PROD_ID, (String) procHash.get("TL_PROD_ID"));
            txMap.put(TransferTrans.DR_ACT_NUM, (String) procHash.get("TL_ACT_NUM"));
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.LOANS);
            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
            List lst = (List) sqlMap.executeQueryForList("getProcChargeAcHd", procHash);
            //            List lst = (List) sqlMap.executeQueryForList("getProcChargeAcHd",procHash.get("LOAN_PROD_ID"));
            HashMap acHeads = (HashMap) lst.get(0);
            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("PROC_CHRG"));
            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
            txMap.put(TransferTrans.CURRENCY, "INR");
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            System.out.println("####### insertAccHead txMap " + txMap);
            TransferTrans trans = new TransferTrans();
            trans.setTransMode(CommonConstants.TX_TRANSFER);
            trans.setInitiatedBranch(_branchCode);
            trans.setLinkBatchId(CommonUtil.convertObjToStr(procHash.get(LINK_BATCH_ID)));
            transferList.add(trans.getDebitTransferTO(txMap, procAmt));
            transferList.add(trans.getCreditTransferTO(txMap, procAmt));
            trans.doDebitCredit(transferList, _branchCode, false);
            transferList = null;
            trans = null;
            acHeads = null;
            lst = null;
        }
        txMap = null;
    }

    private String getCashAcctHead() throws Exception {
        List list = (List) sqlMap.executeQueryForList("getSelectTransParams", null);
        return (String) ((HashMap) list.get(0)).get("CASH_AC_HD");
    }

    private CashTransactionTO getTransactionData(String transID) throws Exception {
        HashMap where = new HashMap();
        where.put(CommonConstants.TRANS_ID, transID);
        where.put("TRANS_DT", currDt);
        where.put("INITIATED_BRANCH", _branchCode);
        List list = (List) sqlMap.executeQueryForList("getSelectCashTransactionTO", where);
        return ((CashTransactionTO) list.get(0));
    }

    private List getTransactionDataForSMSAlerts(String transID) throws Exception {
        HashMap where = new HashMap();
        where.put(CommonConstants.TRANS_ID, transID);
        where.put("TRANS_DT", currDt);
        where.put("INITIATED_BRANCH", _branchCode);
        List list = (List) sqlMap.executeQueryForList("getSelectCashTransactionTOForSMSAlert", where);
        return list;
    }

    private Double getTransAmount(CashTransactionTO cashTO) throws Exception {

        String transType = cashTO.getTransType();
        Double amount = cashTO.getAmount();

        if (transType.equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
            amount = new Double(-1 * amount.doubleValue());
        }
        return amount;
    }

//    private void updateChequeCashAvailableBalance(HashMap inputMapParam)throws Exception {
//        transModuleBased.updateAvailableBalance(inputMapParam);
//    }
    public static void main(String str[]) {
        try {
            AuditEntryDAO dao = new AuditEntryDAO();
            CashTransactionTO objCashTransactionTO = new CashTransactionTO();

            TOHeader toHeader = new TOHeader();
            toHeader.setCommand(CommonConstants.TOSTATUS_INSERT);//To tell what to do... Insert, Update, Delete...

            //objCashTransactionTO.setTOHeader(toHeader);
            //objCashTransactionTO.setTransId("C0001001");
            objCashTransactionTO.setProdType(TransactionFactory.OPERATIVE);
            objCashTransactionTO.setProdId("SBGen");
            objCashTransactionTO.setBranchId("Bran");
            objCashTransactionTO.setAcHdId("CA");
            objCashTransactionTO.setActNum("OA060903");//OA001001
            objCashTransactionTO.setInpAmount(new Double(10));
            objCashTransactionTO.setInpCurr("INR");
            objCashTransactionTO.setAmount(new Double(10));
            objCashTransactionTO.setTransType(TransactionDAOConstants.CREDIT);
            //            objCashTransactionTO.setInstType("CHEQUE");
            //            objCashTransactionTO.setInstrumentNo1("ABC");
            //            objCashTransactionTO.setInstrumentNo2("10026");
            //            objCashTransactionTO.setInstDt(DateUtil.getDateMMDDYYYY("02/27/2004"));
            //            objCashTransactionTO.setTokenNo(new Double(11));
            //            objCashTransactionTO.setInitTransId("********");
            //            objCashTransactionTO.setInitChannType("ATM");
            objCashTransactionTO.setParticulars("Cash Deposit");

            HashMap hash = new HashMap();
            hash.put("CashTransactionTO", objCashTransactionTO);
            hash.put("PRODUCTTYPE", TransactionFactory.OPERATIVE);
            HashMap authMap = new HashMap();
            authMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
            hash.put(CommonConstants.AUTHORIZEMAP, authMap);
            dao.execute(hash);
            //System.out.println("Inserted");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updatepassbook(CashTransactionTO objCashTransactionTOUpd) throws Exception {
        HashMap map = new HashMap();
        if (objCashTransactionTOUpd.getProdType().equals(TransactionFactory.OPERATIVE) || objCashTransactionTOUpd.getProdType().equals(TransactionFactory.ADVANCES) || objCashTransactionTOUpd.getProdType().equals(TransactionFactory.AGRIADVANCES)) {
            double finamt = 0;
            HashMap where = new HashMap();
            if (objCashTransactionTOUpd.getTransType().equals(TransactionDAOConstants.CREDIT)) {
                where.put(TransactionDAOConstants.CREDIT, objCashTransactionTOUpd.getAmount());
            } else if (objCashTransactionTOUpd.getTransType().equals(TransactionDAOConstants.DEBIT)) {
                where.put(TransactionDAOConstants.DEBIT, objCashTransactionTOUpd.getAmount());
            }
            HashMap map1 = new HashMap();
            map1.put(CommonConstants.ACT_NUM, objCashTransactionTOUpd.getActNum());
            List lst1 = (List) sqlMap.executeQueryForList("getClearBalance" + objCashTransactionTOUpd.getProdType(), map1);
            System.out.println("@@@@@@@@@@@@$$%^^%listdebit" + lst1);
            map1 = null;
            map1 = (HashMap) lst1.get(0);
            finamt = CommonUtil.convertObjToDouble(map1.get("TOTAL_BALANCE")).doubleValue();
            System.out.println(objCashTransactionTOUpd.getParticulars() + "@@@@@@@@@@@@$$%^^%finamtdebit" + finamt);

            where.put("BALANCE", new Double(finamt));
            where.put(CommonConstants.TRANS_ID, objCashTransactionTOUpd.getTransId());
            where.put(CommonConstants.ACT_NUM, objCashTransactionTOUpd.getActNum());
            Date transdt = (Date) currDt.clone();
            transdt.setDate(objCashTransactionTOUpd.getTransDt().getDate());
            transdt.setMonth(objCashTransactionTOUpd.getTransDt().getMonth());
            transdt.setYear(objCashTransactionTOUpd.getTransDt().getYear());
            where.put("TRANS_DT", transdt);
            if (objCashTransactionTOUpd.getInstrumentNo1() != null && objCashTransactionTOUpd.getInstrumentNo1().length() > 0
                    && objCashTransactionTOUpd.getInstrumentNo1().equals("INTEREST_AMT")) {
                objCashTransactionTOUpd.setInstrumentNo1("");
            }
            if (objCashTransactionTOUpd.getInstrumentNo2() != null && objCashTransactionTOUpd.getInstrumentNo2().length() > 0
                    && objCashTransactionTOUpd.getInstrumentNo2().equals("DEPOSIT_RENEWAL")) {
                objCashTransactionTOUpd.setInstrumentNo2("");
            }
            String particulars = objCashTransactionTOUpd.getParticulars();
            if (particulars != null) {
                where.put("PARTICULARS", objCashTransactionTOUpd.getParticulars());
            } else {
                where.put("PARTICULARS", "SYS");
            }
            where.put("INSTRUMENT_NO1", objCashTransactionTOUpd.getInstrumentNo1());
            where.put("INSTRUMENT_NO2", objCashTransactionTOUpd.getInstrumentNo2());
            where.put("PBOOKFLAG", "0");
            //                    where.put("SLNO", "0");
            where.put("INST_TYPE", objCashTransactionTOUpd.getInstType());
            transdt = (Date) currDt.clone();
            if (objCashTransactionTOUpd.getInstDt() != null) {
                transdt.setDate(objCashTransactionTOUpd.getInstDt().getDate());
                transdt.setMonth(objCashTransactionTOUpd.getInstDt().getMonth());
                transdt.setYear(objCashTransactionTOUpd.getInstDt().getYear());
                where.put("INST_DT", transdt);
            } else {
                where.put("INST_DT", objCashTransactionTOUpd.getInstDt());
            }
            where.put("STATUS", objCashTransactionTOUpd.getStatus());
            where.put(AUTHORIZE_STATUS, CommonConstants.STATUS_AUTHORIZED);
            where.put("AUTHORIZE_DT", currDt);
            System.out.println("@@@@@@@@@@@@$$%^^%where" + where);
            sqlMap.executeUpdate("insertPassBook", where);
            where = null;
            map1 = null;
            lst1 = null;
            where = null;
        }
        map = null;

    }

    private ArrayList penalReceived(HashMap depositPenalMap) throws Exception {
        ArrayList cashList = new ArrayList();
        double penalAmt = CommonUtil.convertObjToDouble(depositPenalMap.get("DEPOSIT_PENAL_AMT")).doubleValue();
        CashTransactionTO objCashTransactionTO = (CashTransactionTO) depositPenalMap.get(CASH_TO);
        System.out.println("objCashTransactionTO :" + objCashTransactionTO);
        if (objCashTransactionTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
            CashTransactionTO objCashTO = setCashTransaction(objCashTransactionTO);
            double balanceAmt = 0.0;
            if (penalAmt > 0) {
                double totalAmt = CommonUtil.convertObjToDouble(objCashTransactionTO.getAmount()).doubleValue();
                balanceAmt = totalAmt - penalAmt;
                objCashTO.setAmount(new Double(balanceAmt));
            } else {
                balanceAmt = CommonUtil.convertObjToDouble(objCashTransactionTO.getAmount()).doubleValue();
            }
            objCashTO.setLinkBatchId(objCashTransactionTO.getActNum());
            objCashTO.setActNum(objCashTransactionTO.getActNum());
            objCashTO.setProdId(objCashTransactionTO.getProdId());
            objCashTO.setProdType(objCashTransactionTO.getProdType());
            objCashTO.setAmount(new Double(balanceAmt));
            objCashTO.setAcHdId(objCashTransactionTO.getAcHdId());
            objCashTO.setParticulars(objCashTransactionTO.getParticulars());
            objCashTO.setInstrumentNo2("DEPOSIT_PENAL");
            cashList.add(objCashTO);
            System.out.println("cashList RD a/c transaction :" + cashList);
            HashMap prodMap = new HashMap();
            prodMap.put(PROD_ID, objCashTransactionTO.getProdId());
            List lst = sqlMap.executeQueryForList("getBehavesLikeForDeposit", prodMap);
            if (lst != null && lst.size() > 0) {
                prodMap = (HashMap) lst.get(0);
            }
            if (prodMap.get(BEHAVES_LIKE).equals("RECURRING")) {
                prodMap.put(PROD_ID, objCashTransactionTO.getProdId());
                lst = sqlMap.executeQueryForList("getDepositClosingHeads", prodMap);
                if (lst != null && lst.size() > 0) {
                    prodMap = (HashMap) lst.get(0);
                    objCashTO = setCashTransaction(objCashTransactionTO);
                    objCashTO.setAcHdId(CommonUtil.convertObjToStr(prodMap.get("DELAYED_ACHD")));
                    objCashTO.setLinkBatchId(objCashTransactionTO.getActNum());
                    objCashTO.setAmount(new Double(penalAmt));
                    objCashTO.setParticulars("Penal Amount Received " + "" + objCashTransactionTO.getActNum());
                    objCashTO.setInstrumentNo2("DEPOSIT_PENAL");
                    objCashTO.setActNum("");
                    objCashTO.setProdId("");
                    objCashTO.setProdType(TransactionFactory.GL);
                    cashList.add(objCashTO);
                    System.out.println("cashList penal transaction :" + cashList);
                }
            }
            lst = null;
            prodMap = null;
            objCashTO = null;
        }
        objCashTransactionTO = null;
        return cashList;
    }

    private ArrayList chargesServiceTax(HashMap chargesMap) throws Exception {
        System.out.println("chargesMap :" + chargesMap);
        ArrayList cashList = new ArrayList();
        ChargesServiceTaxTO objChargesServiceTaxTO = new ChargesServiceTaxTO();
        objChargesServiceTaxTO = (ChargesServiceTaxTO) chargesMap.get("ChargesServiceTaxTO");
        System.out.println("objCashTransactionTO :" + objChargesServiceTaxTO);
        if (objChargesServiceTaxTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
            CashTransactionTO objCashTO = new CashTransactionTO();
            objCashTO.setTransId("");
            objCashTO.setAcHdId(objChargesServiceTaxTO.getAc_Head());
            objCashTO.setProdId(objChargesServiceTaxTO.getProd_id());
            objCashTO.setProdType(objChargesServiceTaxTO.getProd_type());
            objCashTO.setInpAmount(objChargesServiceTaxTO.getAmount());
            objCashTO.setAmount(objChargesServiceTaxTO.getAmount());
            objCashTO.setTransType(CommonConstants.CREDIT);
            objCashTO.setInitTransId(CommonConstants.TTSYSTEM);
            objCashTO.setBranchId(CommonUtil.convertObjToStr(chargesMap.get("SELECTED_BRANCH_ID")));
            objCashTO.setStatusBy(CommonConstants.TTSYSTEM);
            objCashTO.setStatusDt(objChargesServiceTaxTO.getCreated_dt());
            objCashTO.setInstrumentNo2("INPUT_AMOUNT");
            objCashTO.setParticulars(objChargesServiceTaxTO.getParticulars());
            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(chargesMap.get("SELECTED_BRANCH_ID")));
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            objCashTO.setCommand(objChargesServiceTaxTO.getCommand());
            System.out.println("objCashTO 1st one:" + objCashTO);
            cashList.add(objCashTO);
            System.out.println("cashList RD a/c transaction :" + cashList);
            HashMap serviceMap = new HashMap();
            List lst = sqlMap.executeQueryForList("getSelectServiceTaxHead", null);
            HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getLockerAccountHeads", objChargesServiceTaxTO.getProd_id());
            if (lst != null && lst.size() > 0) {
                serviceMap = (HashMap) lst.get(0);
                objCashTO = new CashTransactionTO();
                objCashTO.setTransId("");
                String n = CommonUtil.convertObjToStr(chargesMap.get("SCREEN"));
                String sur = "Locker Renew/Surrender";
                String issue = "LOCKER_ISSUE";
                if ((n.equals(sur)) || n.equals(issue)) {


                    objCashTO.setAcHdId(CommonUtil.convertObjToStr(acHeads.get("SERV_TAX_AC_HD")));
                } else {
                    objCashTO.setAcHdId(CommonUtil.convertObjToStr(serviceMap.get("OTHER_CHRG_HD")));
                }
                objCashTO.setProdId("");
                objCashTO.setActNum("");
                objCashTO.setProdType(TransactionFactory.GL);
                objCashTO.setInpAmount(objChargesServiceTaxTO.getService_tax_amt());
                objCashTO.setAmount(objChargesServiceTaxTO.getService_tax_amt());
                objCashTO.setTransType(CommonConstants.CREDIT);
                objCashTO.setBranchId(CommonUtil.convertObjToStr(chargesMap.get("SELECTED_BRANCH_ID")));
                objCashTO.setStatusBy(CommonConstants.TTSYSTEM);
                objCashTO.setStatusDt(objChargesServiceTaxTO.getCreated_dt());
                objCashTO.setInstrumentNo2("SERVICE_TAX");
                objCashTO.setParticulars(objChargesServiceTaxTO.getParticulars());
                objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(chargesMap.get("SELECTED_BRANCH_ID")));
                objCashTO.setCommand(objChargesServiceTaxTO.getCommand());
                objCashTO.setInitTransId(CommonConstants.TTSYSTEM);
                objCashTO.setInitChannType(CommonConstants.CASHIER);
                System.out.println("objCashTO 2nd one:" + objCashTO);
                cashList.add(objCashTO);
                System.out.println("cashList penal transaction :" + cashList);
            }
            lst = null;
            serviceMap = null;
            objCashTO = null;
        }
        objChargesServiceTaxTO = null;
        return cashList;
    }

    private ArrayList chargesServiceTaxUpate(HashMap chargesMap) throws Exception {//updating Transaction
        System.out.println("chargesMap :" + chargesMap);
        ArrayList cashList = new ArrayList();
        ChargesServiceTaxTO objChargesServiceTaxTO = new ChargesServiceTaxTO();
        objChargesServiceTaxTO = (ChargesServiceTaxTO) chargesMap.get("ChargesServiceTaxTO");
        System.out.println("objCashTransactionTO :" + objChargesServiceTaxTO);
        if (objChargesServiceTaxTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
            HashMap tempMap = (HashMap) chargesMap.get("SERVICE_TAX_AMT_TRANSACTION");
            String trans_id = CommonUtil.convertObjToStr(tempMap.get(CommonConstants.TRANS_ID));
            CashTransactionTO objCashTO = new CashTransactionTO();
            HashMap param = new HashMap();
            param.put("TRANS_ID", trans_id);
            param.put("INITIATED_BRANCH", _branchCode);
            param.put("TRANS_DT", currDt.clone());
            List lst = sqlMap.executeQueryForList("getSelectINPUT_AMOUNT", param);
            if (lst != null && lst.size() > 0) {
                objCashTO = new CashTransactionTO();
                objCashTO = (CashTransactionTO) lst.get(0);
                objCashTO.setInpAmount(objChargesServiceTaxTO.getAmount());
                objCashTO.setAmount(objChargesServiceTaxTO.getAmount());
                objCashTO.setCommand(objChargesServiceTaxTO.getCommand());
                cashList.add(objCashTO);
            }
            HashMap paramMap = new HashMap();
            paramMap.put("TRANS_ID", trans_id);
            paramMap.put("INITIATED_BRANCH", _branchCode);
            paramMap.put("TRANS_DT", currDt.clone());
            lst = sqlMap.executeQueryForList("getSelectSERVICE_TAX", paramMap);
            if (lst != null && lst.size() > 0) {
                objCashTO = (CashTransactionTO) lst.get(0);
                objCashTO.setInpAmount(objChargesServiceTaxTO.getService_tax_amt());
                objCashTO.setAmount(objChargesServiceTaxTO.getService_tax_amt());
                objCashTO.setCommand(objChargesServiceTaxTO.getCommand());
                cashList.add(objCashTO);
            }
            lst = null;
            objCashTO = null;
        }
        objChargesServiceTaxTO = null;
        return cashList;
    }
    //for interest details transfer from tmp to loans_interest

    void insertInterestDetails(HashMap Interest) throws Exception {
        HashMap singleRecord = new HashMap();
        Interest.put("ACT_NUM", Interest.get("ACCT_NUM"));
        List lst = sqlMap.executeQueryForList("selectLoanInterestTMP", Interest);
        if (lst != null && lst.size() > 0) {

            sqlMap.executeUpdate("deleteLoanInterest", Interest);
            for (int i = 0; i < lst.size(); i++) {
                HashMap insertRecord = new HashMap();
                singleRecord = (HashMap) lst.get(i);
                insertRecord.put("ACT_NUM", singleRecord.get("ACT_NUM"));
                String from_dt = CommonUtil.convertObjToStr(singleRecord.get("FROM_DT"));
                Date fromDate = DateUtil.getDateMMDDYYYY(from_dt);
                insertRecord.put("FROM_DT", fromDate);
                String to_dt = CommonUtil.convertObjToStr(singleRecord.get("TO_DATE"));
                Date toDate = DateUtil.getDateMMDDYYYY(to_dt);
                insertRecord.put("TO_DATE", toDate);
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
                insertRecord.put("PROD_ID", singleRecord.get("PROD_ID"));
                String intcalcdt = CommonUtil.convertObjToStr(singleRecord.get("INT_CALC_DT"));
                Date intcalcDt = DateUtil.getDateMMDDYYYY(intcalcdt);
                insertRecord.put("INT_CALC_DT", intcalcDt);
                insertRecord.put("CUST_ID", singleRecord.get("CUST_ID"));
                System.out.println("insertrecord####" + insertRecord);
                sqlMap.executeUpdate("insertLoanInterest", insertRecord);//singleRecord
            }
            sqlMap.executeUpdate("deleteLoanInterestTMP", Interest);
        }
    }
}
