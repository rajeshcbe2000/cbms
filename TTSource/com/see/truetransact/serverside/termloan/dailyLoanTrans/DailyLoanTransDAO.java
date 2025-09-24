/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * DailyLoanTransDAO.java
 *
 * Created on June 18, 2011, 4:14 PM
 */
package com.see.truetransact.serverside.termloan.dailyLoanTrans;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.common.nominee.NomineeDAO;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.operativeaccount.AccountTO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;

// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

/**
 * This is used for OperativeAcctProductDAO Data Access.
 *
 * @author Suresh
 *
 *
 */
public class DailyLoanTransDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private TransactionDAO transactionDAO = null;
    TransferDAO transferDAO = new TransferDAO();
    NomineeDAO objNomineeDAO = new NomineeDAO();
    private AccountTO accountTO;
    private Date currDt = null;
    final String SCREEN = "TD";
    public List loanList = null;
    public List adjustmentLoanList = null;
    private HashMap TermLoanCloseCharge = new HashMap();
    private HashMap execReturnMap = null;
    private String generateSingleTransId = null;
    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public DailyLoanTransDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap transMap = new HashMap();
        return transMap;
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        generateSingleTransId = generateLinkID();
        currDt = ServerUtil.getCurrentDate(_branchCode);
        System.out.println("DailyLoanMap in DAO: " + map);
        // Log DAO
        LogDAO objLogDAO = new LogDAO();
        execReturnMap = new HashMap();
        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
//        System.out.println("$#$#$#$#$ : Command : " + command);
        if (map.containsKey("DAILY_LOAN_TABLE_DATA") && map.containsKey("DAILY_LOAN_COLLECTION")) {
            try {
                sqlMap.startTransaction();
                objLogTO.setStatus(command);
                loanList = (List) map.get("DAILY_LOAN_TABLE_DATA");
                System.out.println("@##$#$% loanList #### :" + loanList);
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertData(map, objLogDAO, objLogTO);
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData(map, objLogDAO, objLogTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData(map, objLogDAO, objLogTO);
                } else if (map.containsKey("AUTHORIZEMAP")) {
                    authorize(map);
                } else {
                    throw new NoCommandException();
                }
                sqlMap.commitTransaction();
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw e;
            }
            objLogDAO = null;
            objLogTO = null;
            destroyObjects();

        } else if (map.containsKey("LOAN_ADJUSTMENT") && map.containsKey("ADJUSTMENT_LOAN_TABLE_DATA")) {
            try {
                sqlMap.startTransaction();
                objLogTO.setStatus(command);
                adjustmentLoanList = (List) map.get("ADJUSTMENT_LOAN_TABLE_DATA");
//                System.out.println("########## adjustmentLoanList :" + adjustmentLoanList);
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertAdjustmentData(map, objLogDAO, objLogTO);
                } else if (map.containsKey("AUTHORIZEMAP")) {
                    authorizeLoanAdjustment(map);
                } else {
                    throw new NoCommandException();
                }
                sqlMap.commitTransaction();
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw e;
            }
            objLogDAO = null;
            objLogTO = null;
            destroyObjects();

        }
        return execReturnMap;
    }

    private void insertAdjustmentData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            if (adjustmentLoanList != null && adjustmentLoanList.size() > 0) {
                String loanAdjustmentId = "";
                loanAdjustmentId = getLoanAdjustmentTransId();
                execReturnMap.put("LOAN_TRANS_ID", loanAdjustmentId);
                String agent_id = "";
                String penalwaiveoff = "";
                double penalWaiveAmt=0;
                agent_id = CommonUtil.convertObjToStr(map.get("AGENT_ID"));
                penalwaiveoff = CommonUtil.convertObjToStr(map.get("PENAL_WAIVE_OFF"));
                HashMap txMap = new HashMap();
                HashMap transMap = new HashMap();
                String initiatedBranch = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
                //Daily Collection List
                for (int i = 0; i < adjustmentLoanList.size(); i++) {

                    HashMap dataMap = new HashMap();
                    dataMap = (HashMap) adjustmentLoanList.get(i);
                    ArrayList transferList = new ArrayList();
                    TransferTrans transferTrans = new TransferTrans();
                    transferTrans.setInitiatedBranch(initiatedBranch);
                    transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                    transactionDAO.setInitiatedBranch(_branchCode);
                    transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                    HashMap tansactionMap = new HashMap();
                    TxTransferTO transferTo = new TxTransferTO();
                    ArrayList TxTransferTO = new ArrayList();

                    double paymentAmt = 0.0;
                    double interestAmt = 0.0;
                    double penalAmt = 0.0;
                    String loanAccNo = "";
                    paymentAmt = CommonUtil.convertObjToDouble(dataMap.get("PAYMENT")).doubleValue();
                    interestAmt = CommonUtil.convertObjToDouble(dataMap.get("INT_DUE")).doubleValue();
                    penalAmt = CommonUtil.convertObjToDouble(dataMap.get("PENAL")).doubleValue();
                    loanAccNo = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));
                    transferTo.setInstrumentNo2("APPL_GL_TRANS");

                    //Debit Map 
                    String debitProdType = "";
                    HashMap debitMap = new HashMap();
                    HashMap debitHeadMap = new HashMap();
                    HashMap waioffMap = new HashMap();
                    debitMap.put("ACCT_NUM", loanAccNo);
                    List debitList = sqlMap.executeQueryForList("getDailyLoanSanctionDetails", debitMap);
                    if (debitList != null && debitList.size() > 0) {
                        debitMap = (HashMap) debitList.get(0);
                        debitProdType = CommonUtil.convertObjToStr(debitMap.get("PROD_TYPE"));
                    }
//                    System.out.println("########  Debit ProdType : " + debitProdType);

                    if (!debitProdType.equals("") && !debitProdType.equals("GL")) {
                        debitHeadMap.put("PROD_ID", debitMap.get("PROD_ID"));
                        List creditHeadList = sqlMap.executeQueryForList("getAccountHeadProd" + debitProdType, debitHeadMap);
                        if (creditHeadList != null && creditHeadList.size() > 0) {
                            debitHeadMap = (HashMap) creditHeadList.get(0);
                        }
                    }
                    
                    //Added by nithya on 29-06-2019 for KD-543	loan will be clossed with balance (daily to loan adjustment)
                    double totalCharge = 0.0;
                    TermLoanCloseCharge = new HashMap();
                    TermLoanCloseCharge.put("BRANCH_CODE", initiatedBranch);
                    List chargeList = sqlMap.executeQueryForList("getChargeDetails", dataMap);
                    if (chargeList != null && chargeList.size() > 0) {
                        HashMap otherChargesMap = new HashMap();
                        for (int j = 0; j < chargeList.size(); j++) {
                            HashMap chargeMap = (HashMap) chargeList.get(j);
                            double chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                            totalCharge += chargeAmt;
                            if (chargeMap.get("CHARGE_TYPE").equals("POSTAGE CHARGES") && chargeAmt > 0) {
                                TermLoanCloseCharge.put("POSTAGE CHARGES", chargeMap.get("CHARGE_AMT"));
                            } else if (chargeMap.get("CHARGE_TYPE").equals("MISCELLANEOUS CHARGES") && chargeAmt > 0) {
                                TermLoanCloseCharge.put("MISCELLANEOUS CHARGES", chargeMap.get("CHARGE_AMT"));
                            } else if (chargeMap.get("CHARGE_TYPE").equals("LEGAL CHARGES") && chargeAmt > 0) {
                                TermLoanCloseCharge.put("LEGAL CHARGES", chargeMap.get("CHARGE_AMT"));
                            } else if (chargeMap.get("CHARGE_TYPE").equals("INSURANCE CHARGES") && chargeAmt > 0) {
                                TermLoanCloseCharge.put("INSURANCE CHARGES", chargeMap.get("CHARGE_AMT"));
                            } else if (chargeMap.get("CHARGE_TYPE").equals("EXECUTION DECREE CHARGES") && chargeAmt > 0) {
                                TermLoanCloseCharge.put("EXECUTION DECREE CHARGES", chargeMap.get("CHARGE_AMT"));
                            } else if (chargeMap.get("CHARGE_TYPE").equals("ARBITRARY CHARGES") && chargeAmt > 0) {
                                TermLoanCloseCharge.put("ARBITRARY CHARGES", chargeMap.get("CHARGE_AMT"));
                            } else if (chargeMap.get("CHARGE_TYPE").equals("ADVERTISE CHARGES") && chargeAmt > 0) {
                                TermLoanCloseCharge.put("ADVERTISE CHARGES", chargeMap.get("CHARGE_AMT"));
                            } else if (chargeMap.get("CHARGE_TYPE").equals("ARC_COST") && chargeAmt > 0) {// Added by nithya on 20-03-2018 for 0009486: Daily collection - Authorisation Error in ARC EP Cases
                                TermLoanCloseCharge.put("ARC_COST", chargeMap.get("CHARGE_AMT"));
                            } else {
                                otherChargesMap.put(chargeMap.get("CHARGE_TYPE"), chargeMap.get("CHARGE_AMT"));
                            }
                        }
                        TermLoanCloseCharge.put("OTHER_CHARGES", otherChargesMap);
                    }
                    if (interestAmt > 0) {
                        TermLoanCloseCharge.put("CURR_MONTH_INT", String.valueOf(interestAmt));
                    }
                    if (penalAmt > 0) {
                        TermLoanCloseCharge.put("PENAL_INT", String.valueOf(penalAmt));
                    }
                    if (!penalwaiveoff.equals("") && penalwaiveoff.equals("Y")) {
                        List waiveoffList = sqlMap.executeQueryForList("getwaiveoffDetails", dataMap);
                        waioffMap = (HashMap) waiveoffList.get(0);
                        if (waioffMap.get("PENAL_WAIVER").toString().equals("Y")) {
                            penalwaiveoff = "Y";
                            penalWaiveAmt = penalAmt;
                        } else {
                            penalwaiveoff = "N";
                            penalWaiveAmt = 0;
                        }
                    } else {
                        penalwaiveoff = "N";
                        penalWaiveAmt = 0;
                    }
                    if (penalWaiveAmt > 0) {
                        TermLoanCloseCharge.put("PENAL_WAIVE_AMT", penalWaiveAmt);
                    }
                    TermLoanCloseCharge.put("PENAL_WAIVE_OFF", penalwaiveoff);
                    HashMap totBalMap = new HashMap();
                    double totalLoanBalAmount = CommonUtil.convertObjToDouble(dataMap.get("BALANCE")).doubleValue() + interestAmt + penalAmt + totalCharge;                                                    
                    double totalDueForLoan = totalLoanBalAmount - penalWaiveAmt;
                    System.out.println("totalDueForLoan :: " + totalDueForLoan + " paymentAmt :: " + paymentAmt);
                    if (totalDueForLoan <= paymentAmt) {
                        paymentAmt = totalDueForLoan;
                    }
                    System.out.println("paymentAmt after :: " + paymentAmt);
                    // END

                    if (paymentAmt > 0.0) {
                        //CREDIT
                        transferTo = new TxTransferTO();
                        txMap = new HashMap();
                        if (!debitProdType.equals("") && debitProdType.equals("GL")) {
                            txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(debitMap.get("ACCT_HEAD")));
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, "DAILY_LOAN_PAYMENT");
                            txMap.put("TRANS_MOD_TYPE", "GL");
                        } else if (!debitProdType.equals("")) {
                            txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(debitHeadMap.get("AC_HEAD")));
                            txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(debitMap.get("ACCOUNTNO")));
                            txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(debitMap.get("PROD_ID")));
                            txMap.put(TransferTrans.DR_PROD_TYPE, debitProdType);
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(debitMap.get("ACCOUNTNO")) + "-" + ":DAILY_LOAN_PAYMENT");
                            txMap.put("TRANS_MOD_TYPE", debitProdType);
                        }
                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
                        txMap.put(TransferTrans.DR_BRANCH, initiatedBranch);
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
//                        System.out.println("txMap  : " + txMap + "paymentAmt :" + paymentAmt);
                        
                        txMap.put("generateSingleTransId", generateSingleTransId);
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, paymentAmt);
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setTransDt(currDt);
                        transferTo.setInitiatedBranch(initiatedBranch);
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setLinkBatchId(loanAccNo);
                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(loanAccNo));
                        TxTransferTO.add(transferTo);


                        //DEBIT

                        HashMap applicationMap = new HashMap();
                        HashMap prodmap = new HashMap();
                        List lst = null;
                        applicationMap.put("PROD_ID", dataMap.get("PROD_ID"));
                        applicationMap.put("ACCT_NUM", dataMap.get("ACT_NUM"));
                        List prodtype = sqlMap.executeQueryForList("getProdType", applicationMap);
                        if (prodtype != null && prodtype.size() > 0) {
                            prodmap = (HashMap) prodtype.get(0);
                        }
                        if (prodmap.get("PROD_TYPE").toString().equals("TL")) {
                            lst = sqlMap.executeQueryForList("getAccountHeadProdTL", applicationMap);    // Acc Head
                        } else {
                            lst = sqlMap.executeQueryForList("getAccountHeadProdAD", applicationMap);
                        }
                        if (lst != null && lst.size() > 0) {
                            applicationMap = (HashMap) lst.get(0);
                        }
                        
                        //Commented by nithya on 29-06-2019 for KD-543	loan will be clossed with balance (daily to loan adjustment)
//                        TermLoanCloseCharge = new HashMap();
//                        TermLoanCloseCharge.put("BRANCH_CODE", initiatedBranch);
//                        List chargeList = sqlMap.executeQueryForList("getChargeDetails", dataMap);
//                        if (chargeList != null && chargeList.size() > 0) {
//                            HashMap otherChargesMap = new HashMap();
//                            for (int j = 0; j < chargeList.size(); j++) {
//                                HashMap chargeMap = (HashMap) chargeList.get(j);
//                                double chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
//                                if (chargeMap.get("CHARGE_TYPE").equals("POSTAGE CHARGES") && chargeAmt > 0) {
//                                    TermLoanCloseCharge.put("POSTAGE CHARGES", chargeMap.get("CHARGE_AMT"));
//                                } else if (chargeMap.get("CHARGE_TYPE").equals("MISCELLANEOUS CHARGES") && chargeAmt > 0) {
//                                    TermLoanCloseCharge.put("MISCELLANEOUS CHARGES", chargeMap.get("CHARGE_AMT"));
//                                } else if (chargeMap.get("CHARGE_TYPE").equals("LEGAL CHARGES") && chargeAmt > 0) {
//                                    TermLoanCloseCharge.put("LEGAL CHARGES", chargeMap.get("CHARGE_AMT"));
//                                } else if (chargeMap.get("CHARGE_TYPE").equals("INSURANCE CHARGES") && chargeAmt > 0) {
//                                    TermLoanCloseCharge.put("INSURANCE CHARGES", chargeMap.get("CHARGE_AMT"));
//                                } else if (chargeMap.get("CHARGE_TYPE").equals("EXECUTION DECREE CHARGES") && chargeAmt > 0) {
//                                    TermLoanCloseCharge.put("EXECUTION DECREE CHARGES", chargeMap.get("CHARGE_AMT"));
//                                } else if (chargeMap.get("CHARGE_TYPE").equals("ARBITRARY CHARGES") && chargeAmt > 0) {
//                                    TermLoanCloseCharge.put("ARBITRARY CHARGES", chargeMap.get("CHARGE_AMT"));
//                                } else if (chargeMap.get("CHARGE_TYPE").equals("ADVERTISE CHARGES") && chargeAmt > 0) {
//                                    TermLoanCloseCharge.put("ADVERTISE CHARGES", chargeMap.get("CHARGE_AMT"));
//                                } else if (chargeMap.get("CHARGE_TYPE").equals("ARC_COST") && chargeAmt > 0) {// Added by nithya on 20-03-2018 for 0009486: Daily collection - Authorisation Error in ARC EP Cases
//                                    TermLoanCloseCharge.put("ARC_COST", chargeMap.get("CHARGE_AMT"));
//                                } else {
//                                    otherChargesMap.put(chargeMap.get("CHARGE_TYPE"), chargeMap.get("CHARGE_AMT"));
//                                }
//                            }
//                            TermLoanCloseCharge.put("OTHER_CHARGES", otherChargesMap);
//                        }
//                        if (interestAmt > 0) {
//                            TermLoanCloseCharge.put("CURR_MONTH_INT", String.valueOf(interestAmt));
//                        }
//                        if (penalAmt > 0) {
//                            TermLoanCloseCharge.put("PENAL_INT", String.valueOf(penalAmt));
//                        }

                        txMap = new HashMap();
                        transferTo = new TxTransferTO();
                        txMap = new HashMap();
                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
                        txMap.put(TransferTrans.CR_ACT_NUM, loanAccNo);
                        txMap.put(TransferTrans.CR_PROD_ID, dataMap.get("PROD_ID"));
                        txMap.put(TransferTrans.CR_AC_HD, (String) applicationMap.get("AC_HEAD"));
                        txMap.put(TransferTrans.CR_PROD_TYPE, (String) prodmap.get("PROD_TYPE"));
                        txMap.put(TransferTrans.CR_BRANCH, initiatedBranch);
                        txMap.put(TransferTrans.PARTICULARS, loanAccNo + "-" + ":DAILY_LOAN_PAYMENT");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
//                        System.out.println("txMap  : " + txMap + "paymentAmt :" + paymentAmt);
                        txMap.put("TRANS_MOD_TYPE",(String) prodmap.get("PROD_TYPE"));
                        txMap.put("generateSingleTransId", generateSingleTransId);
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, paymentAmt);
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setTransDt(currDt);
                        transferTo.setInitiatedBranch(initiatedBranch);
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setLinkBatchId(loanAccNo);
                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(loanAccNo));
                        TxTransferTO.add(transferTo);
                        //Calling Transfer DAO
                        transferDAO = new TransferDAO();
                        //Commented by nithya on 29-06-2019 for KD-543	loan will be clossed with balance (daily to loan adjustment)
//                        if (!penalwaiveoff.equals("") && penalwaiveoff.equals("Y")) {
//                            List waiveoffList = sqlMap.executeQueryForList("getwaiveoffDetails", dataMap);
//                            waioffMap = (HashMap) waiveoffList.get(0);
//                            if (waioffMap.get("PENAL_WAIVER").toString().equals("Y")) {
//                                penalwaiveoff = "Y";
//                                penalWaiveAmt=penalAmt;
//                            } else {
//                                penalwaiveoff = "N";
//                                penalWaiveAmt=0;
//                            }
//                        } else {
//                            penalwaiveoff = "N";
//                            penalWaiveAmt=0;
//                        }
//                        if(penalWaiveAmt>0)
//                        {
//                            TermLoanCloseCharge.put("PENAL_WAIVE_AMT", penalWaiveAmt);
//                        }
//                        TermLoanCloseCharge.put("PENAL_WAIVE_OFF", penalwaiveoff);
                        tansactionMap.put("ALL_AMOUNT", TermLoanCloseCharge);
                        tansactionMap.put("TxTransferTO", TxTransferTO);
                        tansactionMap.put("MODE", map.get("COMMAND"));
                        tansactionMap.put("COMMAND", map.get("COMMAND"));
                        tansactionMap.put(CommonConstants.BRANCH_ID, initiatedBranch);
                        tansactionMap.put("LINK_BATCH_ID", loanAccNo);
                        System.out.println("################ tansactionMap :" + tansactionMap);
                        execReturnMap = transferDAO.execute(tansactionMap, false);
                    }
                }
                //Insert DAILY_LOAN_ADJUSTMENT_DETAILS Table
                for (int k = 0; k < adjustmentLoanList.size(); k++) {
                    HashMap dataMap = new HashMap();
                    dataMap = (HashMap) adjustmentLoanList.get(k);
//                    System.out.println("############## dataMap " + dataMap);
                    if (CommonUtil.convertObjToDouble(dataMap.get("PAYMENT")).doubleValue() > 0) {
                        dataMap.put("AGENT_ID", agent_id);
                        dataMap.put("LOAN_ADJUSTMENT_NO", loanAdjustmentId);
                        dataMap.put("STATUS", CommonConstants.STATUS_CREATED);
                        dataMap.put("STATUS_DT", currDt);
                        dataMap.put("STATUS_BY", (String) map.get(CommonConstants.USER_ID));
                        dataMap.put("BRANCH_ID",initiatedBranch);
                        sqlMap.executeUpdate("insertDailyLoanAdjustmentTansaction", dataMap);
                    }
                }
                objLogDAO.addToLog(objLogTO);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private String getLoanTransId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "LOAN_COLLECTION_ID");
        String loanCollectionId = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return loanCollectionId;
    }

    private String getLoanAdjustmentTransId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "LOAN_ADJUSTMENT_ID");
        String loanAdjustmentId = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return loanAdjustmentId;
    }

    private void insertData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            if (loanList != null && loanList.size() > 0) {
                String loanCollectionId = "";
                loanCollectionId = getLoanTransId();
                execReturnMap.put("LOAN_TRANS_ID", loanCollectionId);
                String agent_id = "";
                agent_id = CommonUtil.convertObjToStr(map.get("AGENT_ID"));
                HashMap txMap = new HashMap();
                HashMap transMap = new HashMap();
                HashMap tansactionMap = new HashMap();
                ArrayList TxTransferTO = new ArrayList();
                TxTransferTO transferTo = new TxTransferTO();
                String initiatedBranch = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
                String debitProdType = "";
                //Debit Map OR Agent Suspense Account Details
                HashMap debitMap = new HashMap();
                HashMap debitHeadMap = new HashMap();
                debitMap.put("AGENT_ID", agent_id);
                List debitList = sqlMap.executeQueryForList("getAgentAccountdetails", debitMap);
                if (debitList != null && debitList.size() > 0) {
                    debitMap = (HashMap) debitList.get(0);
                    debitProdType = CommonUtil.convertObjToStr(debitMap.get("PROD_TYPE"));
                }
                //For commision and total collection AMOUNT TRANSACTION
                double totCollectionAmount = CommonUtil.convertObjToDouble(map.get("TOTAL_PAYMENT_AMOUNT")).doubleValue();
                double commisionAmount  = CommonUtil.convertObjToDouble(map.get("TOTAL_COMMISION_AMOUNT")).doubleValue();
                System.out.println("##############  TOTAL_PAYMENT_AMOUNT : "+totCollectionAmount);
                System.out.println("##############  TOTAL_COMMISION_AMOUNT : "+commisionAmount);
                if (!debitProdType.equals("") && !debitProdType.equals("GL")) {
                    debitHeadMap.put("PROD_ID", debitMap.get("PROD_ID"));
                    List debitHeadList = sqlMap.executeQueryForList("getAccountHeadProd" + debitProdType, debitHeadMap);
                    if (debitHeadList != null && debitHeadList.size() > 0) {
                        debitHeadMap = (HashMap) debitHeadList.get(0);
                        if (debitHeadList != null && debitHeadList.size() > 0) {
                            debitHeadMap = (HashMap) debitHeadList.get(0);
                        }
                    }
                }
                //Daily Collection List
                for (int i = 0; i < loanList.size(); i++) {
                    HashMap dataMap = new HashMap();
                    dataMap = (HashMap) loanList.get(i);
                    ArrayList transferList = new ArrayList();
                    TransferTrans transferTrans = new TransferTrans();
                    transferTrans.setInitiatedBranch(initiatedBranch);
                    transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                    transactionDAO.setInitiatedBranch(_branchCode);
                    transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                    //HashMap tansactionMap = new HashMap();
                    //TxTransferTO transferTo = new TxTransferTO();
                    //ArrayList TxTransferTO = new ArrayList();

                    double paymentAmt = 0.0;
                    double interestAmt = 0.0;
                    double penalAmt = 0.0;
                    double commission = 0.0;
                    String loanAccNo = "";
                    commission = CommonUtil.convertObjToDouble(dataMap.get("COMMISSION")).doubleValue();
                    paymentAmt = CommonUtil.convertObjToDouble(dataMap.get("PAYMENT")).doubleValue();
                    interestAmt = CommonUtil.convertObjToDouble(dataMap.get("INT_DUE")).doubleValue();
                    penalAmt = CommonUtil.convertObjToDouble(dataMap.get("PENAL")).doubleValue();
                    loanAccNo = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));
                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                    //Debit
                    if (paymentAmt > 0) {           // Debit Insert Start
                        txMap = new HashMap();
//                        System.out.println("Transfer Started debit : ");
                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
                        if (!debitProdType.equals("") && debitProdType.equals("GL")) {
                            txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(debitMap.get("ACCT_NUM")));
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put("TRANS_MOD_TYPE", "GL");
                        } else if (!debitProdType.equals("")) {
                            txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(debitHeadMap.get("AC_HEAD")));
                            txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(debitMap.get("ACCT_NUM")));
                            txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(debitMap.get("PROD_ID")));
                            txMap.put(TransferTrans.DR_PROD_TYPE, debitProdType); 
                            txMap.put("TRANS_MOD_TYPE", debitProdType);
                        }
                        txMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put(TransferTrans.PARTICULARS, loanCollectionId + "-" + ":DAILY_LOAN_PAYMENT");
                        txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(loanAccNo.substring(0, 4) ));
//                        System.out.println("txMap Debit : " + txMap+"paymentAmt :" + paymentAmt);
                        //txMap.put("TRANS_MOD_TYPE", "GL");
                        txMap.put("generateSingleTransId", generateSingleTransId);
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, paymentAmt+commission);
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setTransDt(currDt);
                        transferTo.setBranchId(initiatedBranch);
                        transferTo.setInitiatedBranch(initiatedBranch);
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setAuthorizeRemarks("DP");
                        transactionDAO.setLinkBatchID(loanCollectionId);
                        TxTransferTO.add(transferTo);
                    }


                    //Credit
                    String creditProdType = "";
                    String direct_Repayment = "";
                    HashMap creditMap = new HashMap();
                    HashMap creditHeadMap = new HashMap();
                    creditMap.put("ACCT_NUM", loanAccNo);
                    List creditList = sqlMap.executeQueryForList("getDailyLoanSanctionDetails", creditMap);
                    if (creditList != null && creditList.size() > 0) {
                        creditMap = (HashMap) creditList.get(0);
                        direct_Repayment = CommonUtil.convertObjToStr(creditMap.get("DIRECT_REPAYMENT"));
                        creditProdType = CommonUtil.convertObjToStr(creditMap.get("PROD_TYPE"));
                        if (!direct_Repayment.equals("") && direct_Repayment.equals("N")) {
                            dataMap.put("ADJUSTED_LOAN", "N");
                        } else {
                            dataMap.put("ADJUSTED_LOAN", "Y");
                        }
                    }
//                    System.out.println("########  direct_Repayment : "+direct_Repayment);
//                    System.out.println("########  creditProdType : "+creditProdType);

                    if (!creditProdType.equals("") && !creditProdType.equals("GL") && direct_Repayment.equals("N")) {
                        creditHeadMap.put("PROD_ID", creditMap.get("PROD_ID"));
                        List creditHeadList = sqlMap.executeQueryForList("getAccountHeadProd" + creditProdType, creditHeadMap);
                        if (creditHeadList != null && creditHeadList.size() > 0) {
                            creditHeadMap = (HashMap) creditHeadList.get(0);
                            if (creditHeadList != null && creditHeadList.size() > 0) {
                                creditHeadMap = (HashMap) creditHeadList.get(0);
                            }
                        }
                    }

                    if (!direct_Repayment.equals("") && direct_Repayment.equals("Y")) {
                        HashMap applicationMap = new HashMap();
                        applicationMap.put("PROD_ID", dataMap.get("PROD_ID"));
                        List lst = sqlMap.executeQueryForList("getAccountHeadProdTL", applicationMap);    // Acc Head
                        if (lst != null && lst.size() > 0) {
                            applicationMap = (HashMap) lst.get(0);
                        }
                        TermLoanCloseCharge = new HashMap();
                        TermLoanCloseCharge.put("BRANCH_CODE", initiatedBranch);
                        List chargeList = sqlMap.executeQueryForList("getChargeDetails", dataMap);
                        if (chargeList != null && chargeList.size() > 0) {
                            HashMap otherChargesMap = new HashMap();
                            for (int j = 0; j < chargeList.size(); j++) {
                                HashMap chargeMap = (HashMap) chargeList.get(j);
                                double chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                                if (chargeMap.get("CHARGE_TYPE").equals("POSTAGE CHARGES") && chargeAmt > 0) {
                                    TermLoanCloseCharge.put("POSTAGE CHARGES", chargeMap.get("CHARGE_AMT"));
                                } else if (chargeMap.get("CHARGE_TYPE").equals("MISCELLANEOUS CHARGES") && chargeAmt > 0) {
                                    TermLoanCloseCharge.put("MISCELLANEOUS CHARGES", chargeMap.get("CHARGE_AMT"));
                                } else if (chargeMap.get("CHARGE_TYPE").equals("LEGAL CHARGES") && chargeAmt > 0) {
                                    TermLoanCloseCharge.put("LEGAL CHARGES", chargeMap.get("CHARGE_AMT"));
                                } else if (chargeMap.get("CHARGE_TYPE").equals("INSURANCE CHARGES") && chargeAmt > 0) {
                                    TermLoanCloseCharge.put("INSURANCE CHARGES", chargeMap.get("CHARGE_AMT"));
                                } else if (chargeMap.get("CHARGE_TYPE").equals("EXECUTION DECREE CHARGES") && chargeAmt > 0) {
                                    TermLoanCloseCharge.put("EXECUTION DECREE CHARGES", chargeMap.get("CHARGE_AMT"));
                                } else if (chargeMap.get("CHARGE_TYPE").equals("ARBITRARY CHARGES") && chargeAmt > 0) {
                                    TermLoanCloseCharge.put("ARBITRARY CHARGES", chargeMap.get("CHARGE_AMT"));
                                } else if (chargeMap.get("CHARGE_TYPE").equals("ADVERTISE CHARGES") && chargeAmt > 0) {
                                    TermLoanCloseCharge.put("ADVERTISE CHARGES", chargeMap.get("CHARGE_AMT"));
                                } else if (chargeMap.get("CHARGE_TYPE").equals("ARC_COST") && chargeAmt > 0) {// Added by nithya on 20-03-2018 for 0009486: Daily collection - Authorisation Error in ARC EP Cases
                                    TermLoanCloseCharge.put("ARC_COST", chargeMap.get("CHARGE_AMT"));
                                } else {
                                    otherChargesMap.put(chargeMap.get("CHARGE_TYPE"), chargeMap.get("CHARGE_AMT"));
                                }
                            }
                            TermLoanCloseCharge.put("OTHER_CHARGES", otherChargesMap);
                        }
                        if (interestAmt > 0) {
                            TermLoanCloseCharge.put("CURR_MONTH_INT", String.valueOf(interestAmt));
                        }
                        if (penalAmt > 0) {
                            TermLoanCloseCharge.put("PENAL_INT", String.valueOf(penalAmt));
                        }
                        System.out.println("sub%#%#%#%#"+CommonUtil.convertObjToStr(loanAccNo.substring(0, 4) ));
                        txMap = new HashMap();
                        if (paymentAmt > 0.0) {
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            txMap.put(TransferTrans.CR_ACT_NUM, loanAccNo);
                            txMap.put(TransferTrans.CR_PROD_ID, dataMap.get("PROD_ID"));
                            txMap.put(TransferTrans.CR_AC_HD, (String) applicationMap.get("AC_HEAD"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
                            txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(loanAccNo.substring(0, 4) ));
                            txMap.put(TransferTrans.PARTICULARS, loanAccNo + "-" + ":DAILY_LOAN_PAYMENT");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
//                            System.out.println("txMap  : " + txMap+"paymentAmt :"+paymentAmt);
                            txMap.put("TRANS_MOD_TYPE", "GL");
                            txMap.put("generateSingleTransId", generateSingleTransId);
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, paymentAmt);
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt(currDt);
                            transferTo.setInitiatedBranch(initiatedBranch);
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setLinkBatchId(loanAccNo);                            
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(loanAccNo));
                            TxTransferTO.add(transferTo);
                        }
                       if(commission>0){
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            HashMap commissionMap = new HashMap();
                            commissionMap.put("AGENT_ID", agent_id);
                            commissionMap.put("PROD_ID", CommonUtil.convertObjToStr(dataMap.get("PROD_ID")));
                            List commissionList = sqlMap.executeQueryForList("getAgentCommAcHd", commissionMap);
                            if (commissionList != null && commissionList.size() > 0) {
                                commissionMap = (HashMap) commissionList.get(0);
                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(commissionMap.get("COMM_COL_AC_HD_ID")));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, "DAILY_LOAN_PAYMENT_COMMISSION");
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(loanAccNo.substring(0, 4) ));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                txMap.put(TransferTrans.DR_INSTRUMENT_2, "CHARGESUI");
                                txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                txMap.put("generateSingleTransId", generateSingleTransId);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, commission);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setTransDt((Date)currDt.clone());
                                transferTo.setInitiatedBranch(initiatedBranch);
                                transferTo.setLinkBatchId("");
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                TxTransferTO.add(transferTo);
                            }
                        }  
                    } else if (!direct_Repayment.equals("") && direct_Repayment.equals("N")) {
                        if (paymentAmt > 0.0) {
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            if (!creditProdType.equals("") && creditProdType.equals("GL")) {
                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(creditMap.get("ACCT_HEAD")));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, "DAILY_LOAN_PAYMENT");
                            } else if (!creditProdType.equals("")) {
                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(creditHeadMap.get("AC_HEAD")));
                                txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(creditMap.get("ACCOUNTNO")));
                                txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(creditMap.get("PROD_ID")));
                                txMap.put(TransferTrans.CR_PROD_TYPE, creditProdType);
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(creditMap.get("ACCOUNTNO")) + "-" + ":DAILY_LOAN_PAYMENT");
                            }
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(loanAccNo.substring(0, 4) ));
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                            txMap.put("TRANS_MOD_TYPE", creditProdType);
                            txMap.put("generateSingleTransId", generateSingleTransId);
//                            System.out.println("txMap  : " + txMap+"paymentAmt :"+paymentAmt);
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, paymentAmt);
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt(currDt);
                            transferTo.setInitiatedBranch(initiatedBranch);
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setLinkBatchId(loanAccNo);
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(loanAccNo));
                            TxTransferTO.add(transferTo);
                        }
                        if(commission>0){
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            HashMap commissionMap = new HashMap();
                            commissionMap.put("AGENT_ID", agent_id);
                            commissionMap.put("PROD_ID", CommonUtil.convertObjToStr(creditMap.get("PROD_ID")));
                            List commissionList = sqlMap.executeQueryForList("getAgentCommAcHd", commissionMap);
                            if (commissionList != null && commissionList.size() > 0) {
                                commissionMap = (HashMap) commissionList.get(0);
                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(commissionMap.get("COMM_COL_AC_HD_ID")));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, "DAILY_LOAN_PAYMENT_COMMISSION");
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(loanAccNo.substring(0, 4) ));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INSTRUMENT_2, "CHARGESUI");
                                txMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                txMap.put("generateSingleTransId", generateSingleTransId);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, commission);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setTransDt((Date)currDt.clone());
                                transferTo.setInitiatedBranch(initiatedBranch);
                                transferTo.setLinkBatchId("");
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                TxTransferTO.add(transferTo);
                            }
                        }  
                    }                  
                    transferDAO = new TransferDAO();
                    tansactionMap.put("ALL_AMOUNT", TermLoanCloseCharge);
                    tansactionMap.put("TxTransferTO", TxTransferTO);
                    tansactionMap.put("MODE", map.get("COMMAND"));
                    tansactionMap.put("COMMAND", map.get("COMMAND"));
                    tansactionMap.put(CommonConstants.BRANCH_ID, initiatedBranch);
                    tansactionMap.put("LINK_BATCH_ID", loanAccNo);
//                  System.out.println("################ tansactionMap :"+tansactionMap);
                    execReturnMap = transferDAO.execute(tansactionMap, false);

                }   
                //Insert DAILY_LOAN_SANCTION_DETAILS Table
                for (int k = 0; k < loanList.size(); k++) {
                    HashMap dataMap = new HashMap();
                    dataMap = (HashMap) loanList.get(k);
//                    System.out.println("############## dataMap " + dataMap);
                    if (CommonUtil.convertObjToDouble(dataMap.get("PAYMENT")).doubleValue() > 0) {
                        dataMap.put("AGENT_ID", agent_id);
                        dataMap.put("LOAN_COLLECTION_NO", loanCollectionId);
                        dataMap.put("STATUS", CommonConstants.STATUS_CREATED);
                        dataMap.put("STATUS_DT", currDt);
                        dataMap.put("STATUS_BY", (String) map.get(CommonConstants.USER_ID));
                        dataMap.put("BRANCH_ID",initiatedBranch);
                        System.out.println("############## dataMap ------- " + dataMap);
                        sqlMap.executeUpdate("insertDailyLoanTansaction", dataMap);
                    }
                }
                //Commision and collection
                objLogDAO.addToLog(objLogTO);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateData(HashMap chargesMap, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
    }

    private void deleteData(HashMap chargesMap, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
    }

    private void authorize(HashMap map) throws Exception {
        System.out.println("######### map : " + map);
        String initiatedBranch = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        HashMap authMap = new HashMap();
        authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
        ArrayList selectedList = (ArrayList) authMap.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthorizeMap = new HashMap();
        AuthorizeMap = (HashMap) selectedList.get(0);
        String status = CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS));
        transferDAO = new TransferDAO();
        if (loanList != null) {
            for (int i = 0; i < loanList.size(); i++) {
                HashMap dataMap = new HashMap();
                dataMap = (HashMap) loanList.get(i);
                String linkBatchId = "";
                double paymentAmt = 0.0;
                paymentAmt = CommonUtil.convertObjToDouble(dataMap.get("PAYMENT")).doubleValue();
                if (paymentAmt > 0) {
                    HashMap transferTransParam = new HashMap();
                    transferTransParam.put(CommonConstants.BRANCH_ID, initiatedBranch);
                    transferTransParam.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                    transferTransParam.put("DEBIT_LOAN_TYPE", "DP");
                    linkBatchId = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));
                    transferTransParam.put("LINK_BATCH_ID", linkBatchId);
                    System.out.println(transferTransParam + "  linkBatchId####" + linkBatchId);
                    ArrayList transferTransList = (ArrayList) sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", transferTransParam);
                    HashMap cashAuthMap = new HashMap();
                    cashAuthMap.put(CommonConstants.BRANCH_ID, initiatedBranch);
                    cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                    cashAuthMap.put("DEBIT_LOAN_TYPE", "DP");
                    transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
                    transactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
                    //commented by rishad 23/05/2014
                   // System.out.println("transferTransList 22222=====" + transferTransList);
//                    if (transferTransList != null && transferTransList.size() > 0) {
//                        //modified by rishad 12/09/2014
//                     for(int k=0;k<transferTransList.size();k++){
//                    // String batchId = ((TxTransferTO) transferTransList.get(0)).getBatchId();
//                        String batchId = ((TxTransferTO) transferTransList.get(k)).getBatchId();
//                        System.out.println("###@@# batchId : " + batchId);
//                        HashMap transAuthMap = new HashMap();
//                        transAuthMap.put("BATCH_ID", batchId);
//                        transAuthMap.put(CommonConstants.AUTHORIZESTATUS, status);
//                        transAuthMap.put(CommonConstants.AUTHORIZEDATA, transferTransList);
//                        transferTransParam.put(CommonConstants.AUTHORIZEMAP, transAuthMap);
//                        transferDAO.execute(transferTransParam, false);
//                         
//                   }
//                    }
                   // end
                    //Update Closed Status
//                    if(status.equals("AUTHORIZED")){
//                        double totPayableAmt = 0;
//                        totPayableAmt = CommonUtil.convertObjToDouble(dataMap.get("BALANCE")).doubleValue()+
//                        CommonUtil.convertObjToDouble(dataMap.get("INTEREST")).doubleValue()+
//                        CommonUtil.convertObjToDouble(dataMap.get("PENAL")).doubleValue()+
//                        CommonUtil.convertObjToDouble(dataMap.get("CHARGE")).doubleValue();
//                        if(totPayableAmt==paymentAmt){
//                            accountTO = new AccountTO();
//                            accountTO.setActNum(linkBatchId);
//                            accountTO.setActStatusId(CommonConstants.CLOSED);
//                            accountTO.setClosedDt(currDt);
//                            accountTO.setActStatusDt(currDt);
//                            accountTO.setClosedBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
//                            System.out.println("insertData accountTO"+accountTO);
//                            sqlMap.executeUpdate("updateLoanActClosingDetail", accountTO);
//                        }
//                    }
                }
            }
            System.out.println("#$$$$$$$$$$$$$ AuthorizeMap : " + AuthorizeMap);
            sqlMap.executeUpdate("authorizeDailyLoanTransactionDetails", AuthorizeMap);
        }
    }

    private void authorizeLoanAdjustment(HashMap map) throws Exception {
        System.out.println("######### map : " + map);
        String initiatedBranch = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        HashMap authMap = new HashMap();
        authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
        ArrayList selectedList = (ArrayList) authMap.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthorizeMap = new HashMap();
        AuthorizeMap = (HashMap) selectedList.get(0);
        String status = CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS));
        transferDAO = new TransferDAO();
        if (adjustmentLoanList != null) {
            for (int i = 0; i < adjustmentLoanList.size(); i++) {
                HashMap dataMap = new HashMap();
                dataMap = (HashMap) adjustmentLoanList.get(i);
                String linkBatchId = "";
                double paymentAmt = 0.0;
                paymentAmt = CommonUtil.convertObjToDouble(dataMap.get("PAYMENT")).doubleValue();
                if (paymentAmt > 0) {
                    HashMap transferTransParam = new HashMap();
                    transferTransParam.put(CommonConstants.BRANCH_ID, initiatedBranch);
                    transferTransParam.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                    transferTransParam.put("DEBIT_LOAN_TYPE", "DP");
                    linkBatchId = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));
                    transferTransParam.put("LINK_BATCH_ID", linkBatchId);
                    transferTransParam.put("ACCOUNT_CLOSING", "ACCOUNT_CLOSING");
                    System.out.println(transferTransParam + "  linkBatchId####" + linkBatchId);
                    //commeneted by rishad 24/05/2014
//                    ArrayList transferTransList = (ArrayList) sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", transferTransParam);
//                    System.out.println("transferTransList IN=====" + transferTransList);
//                      if (transferTransList != null && transferTransList.size() > 0) {
//                        //modified by rishad 12/09/2014
//                     for(int k=0;k<transferTransList.size();k++){
//                    // String batchId = ((TxTransferTO) transferTransList.get(0)).getBatchId();
//                          String batchId = ((TxTransferTO) transferTransList.get(k)).getBatchId();
//                        HashMap transAuthMap = new HashMap();
//                        transAuthMap.put("BATCH_ID", batchId);
//                        transAuthMap.put(CommonConstants.AUTHORIZESTATUS, status);
//                        transAuthMap.put(CommonConstants.AUTHORIZEDATA, transferTransList);
//                        transferTransParam.put(CommonConstants.AUTHORIZEMAP, transAuthMap);
//                        transferDAO.execute(transferTransParam, false);   
//                   }
//                    }
                    //added by rishad 24/05/2014
                    HashMap cashAuthMap = new HashMap();
                    cashAuthMap.put(CommonConstants.BRANCH_ID, initiatedBranch);
                    cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                    cashAuthMap.put("DEBIT_LOAN_TYPE", "DP");
                    transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
                    transactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
                    //end
                    //Update Closed Status
                    if (status.equals("AUTHORIZED")) {
                        sqlMap.executeUpdate("updateDailyLoanAdjustmentStatus", dataMap);
                    }
                    if (status.equals("AUTHORIZED")) {
                        double totPayableAmt = 0;
                        totPayableAmt = CommonUtil.convertObjToDouble(dataMap.get("BALANCE")).doubleValue()
                                + CommonUtil.convertObjToDouble(dataMap.get("INTEREST")).doubleValue()
                                + CommonUtil.convertObjToDouble(dataMap.get("PENAL")).doubleValue()
                                + CommonUtil.convertObjToDouble(dataMap.get("CHARGE")).doubleValue();
                        List waiveoffList = ServerUtil.executeQuery("getPenalwaiveOff", dataMap);
                        if (waiveoffList != null && waiveoffList.size() > 0) {
                            HashMap waMap = (HashMap) waiveoffList.get(0);
                            String waive_off = "";
                            if (waMap.containsKey("PENAL_WAIVE_OFF") && waMap.get("PENAL_WAIVE_OFF") != null) {
                                waive_off = waMap.get("PENAL_WAIVE_OFF").toString();
                            }
                            if (waive_off != null && waive_off.equals("Y")) {
                                totPayableAmt = totPayableAmt - CommonUtil.convertObjToDouble(dataMap.get("PENAL")).doubleValue();
                            }
                        }
                        //System.out.println("totPayableAmt :: "+ totPayableAmt);
                        //System.out.println("paymentAmt :: "+ paymentAmt);
                        if (totPayableAmt <= paymentAmt) { //Added by nithya on 29-06-2019 for KD-543	loan will be clossed with balance (daily to loan adjustment)
                        //if (totPayableAmt == paymentAmt) {
                            accountTO = new AccountTO();
                            accountTO.setActNum(linkBatchId);
                            accountTO.setActStatusId(CommonConstants.CLOSED);
                            accountTO.setClosedDt(currDt);
                            accountTO.setActStatusDt(currDt);
                            accountTO.setClosedBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                            System.out.println("insertData accountTO" + accountTO);
                            sqlMap.executeUpdate("updateLoanActClosingDetail", accountTO);
                        }
                    }
                }
            }
            System.out.println("#$$$$$$$$$$$$$ AuthorizeMap : " + AuthorizeMap);
            sqlMap.executeUpdate("authorizeDailyLoanAdjustmentDetails", AuthorizeMap);
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
//        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        loanList = null;
        adjustmentLoanList = null;
    }

    public static void main(String str[]) {
        try {
            DailyLoanTransDAO dao = new DailyLoanTransDAO();

        } catch (Exception ex) {
            ex.printStackTrace();
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
   
}
