/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OperativeAcctProductDAO.java
 *
 * Created on June 18, 2003, 4:14 PM
 */
package com.see.truetransact.serverside.mdsapplication.mdsconmmencement;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.rep.util.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.transferobject.mdsapplication.MDSApplicationTO;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;

// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.common.sms.SmsConfigDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import java.text.DateFormatSymbols;

/**
 * This is used for OperativeAcctProductDAO Data Access.
 *
 * @author Balachandar
 *
 * @modified Pinky @modified Rahul
 */
public class MDSCommencementDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private TransactionDAO transactionDAO = null;
    private MDSApplicationTO mdsApplicationTO = null;
    TransferDAO transferDAO = new TransferDAO();
    private Date currDt = null;
    private HashMap MDSClosureMap = new HashMap();
    private Map returnMap = null;

    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public MDSCommencementDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = CommonUtil.convertObjToStr(map.get("COMMENCEMENT_TRANS_ID"));
        List list = (List) sqlMap.executeQueryForList("getSelectGLTransNotAuthMDSApplicationTO", map);
        returnMap.put("mdsApplicationTO", list);
        HashMap whereMap = new HashMap();
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        String transId = CommonUtil.convertObjToStr(map.get("COMMENCEMENT_TRANS_ID"));
        HashMap getTransMap = new HashMap();
        getTransMap.put("TRANS_ID", transId);
        getTransMap.put("TRANS_DT", currDt.clone());
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        whereMap.put(CommonConstants.MAP_WHERE, getTransMap);
        list = transactionDAO.getData(whereMap);
        returnMap.put("TransactionTO", list);

        HashMap editRemitMap = new HashMap();
        HashMap editMap = new HashMap();
        editMap.put("TRANS_ID", CommonUtil.convertObjToStr(map.get("TRANS_ID")));
        editMap.put("TRANS_DT", currDt.clone());
        editMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(_branchCode));
        editRemitMap.put("TRANS_ID", CommonUtil.convertObjToStr(map.get("TRANS_ID")));
        List lstRemit = sqlMap.executeQueryForList("getSelectRemitTransactionAmt", editMap);
        if (lstRemit != null && lstRemit.size() > 0) {
            editRemitMap = (HashMap) lstRemit.get(0);
        }
        List lstEdit = sqlMap.executeQueryForList("getSelectApplnAmt", editMap);
        if (lstEdit != null && lstEdit.size() > 0) {
            editMap = (HashMap) lstEdit.get(0);
            double amount = CommonUtil.convertObjToDouble(editMap.get("INST_AMT")).doubleValue();
            getTransMap = new HashMap();
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
                List lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
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
        }
        System.out.println("returnMap : " + returnMap);
        return returnMap;
    }

    private void insertDataWithBonus(HashMap map, LogDAO objLogDAO, LogTO objLogTO, double auction_maxamt, HashMap productMap) throws Exception {
        try {
            System.out.println("###### :" + map);
            ArrayList transferList = new ArrayList();
            TransferTrans transferTrans = new TransferTrans();
            TxTransferTO transferTo = new TxTransferTO();
            ArrayList TxTransferTO = new ArrayList();
            TransactionTO transactionTO = new TransactionTO();
            HashMap txMap = new HashMap();
            String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
            double totalAmt = CommonUtil.convertObjToDouble(map.get("TOTAL_AMOUNT")).doubleValue();
            transferTrans.setInitiatedBranch(BRANCH_ID);
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            transactionDAO.setInitiatedBranch(_branchCode);
            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
            if (map.containsKey("TransactionTO")) {
                HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                }
                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                List totalList = sqlMap.executeQueryForList("getSelectGLTransMDSApplicationTO", map);
                if (totalList != null && totalList.size() > 0) {
                    System.out.println("###### totalListsize() : " + totalList.size());
                    double totalTransAmt = 0.0;
                    double totalSuspendsAmt = 0.0;
                    double totalBonusAmt = 0.0;
                    totalTransAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                    double commisionRateAmt = 0.0;
                    commisionRateAmt = CommonUtil.convertObjToDouble(productMap.get("COMMISION_RATE_AMT")).doubleValue();
                    String commisionType = CommonUtil.convertObjToStr(productMap.get("COMMISION_RATE_TYPE"));
                    if (commisionType != null && !commisionType.equals("") && commisionType.equals("Percent")) {
                        totalBonusAmt = totalTransAmt * (auction_maxamt - commisionRateAmt) / 100;
                    } else {
                        totalBonusAmt = totalTransAmt * auction_maxamt / 100;
                        totalBonusAmt -= commisionRateAmt;
                    }
//                    totalBonusAmt = totalTransAmt*auction_maxamt/100;
                    totalSuspendsAmt = totalTransAmt - totalBonusAmt;
                    System.out.println("######     totalBonusAmt : " + totalBonusAmt);
                    System.out.println("######  totalSuspendsAmt : " + totalSuspendsAmt);
                    String linkBatchId = CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo());
                    HashMap applicationMap = new HashMap();
                    HashMap transMap = new HashMap();
                    applicationMap.put("SCHEME_NAME", map.get("SCHEME_NAME"));
                    List lst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", applicationMap);
                    if (lst != null && lst.size() > 0) {
                        applicationMap = (HashMap) lst.get(0);
                    }
                    // Debit Suspends Account
                    if (totalSuspendsAmt > 0) {
                        txMap = new HashMap();
                        if (CommonUtil.convertObjToStr(applicationMap.get("SUSPENSE_GL_ACCNO")).equals("GL")) {
                            txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("SUSPENSE_HEAD"));
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        } else {
                            HashMap suspenseMap = new HashMap();                // Suspense Acc Head
                            suspenseMap.put("PROD_ID", applicationMap.get("SUSPENSE_PROD_ID"));
                            List suspenseLst = sqlMap.executeQueryForList("getAccountHeadProdSA", suspenseMap);
                            if (suspenseLst != null && suspenseLst.size() > 0) {
                                suspenseMap = (HashMap) suspenseLst.get(0);
                                txMap.put(TransferTrans.DR_AC_HD, suspenseMap.get("AC_HEAD"));
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.SUSPENSE);
                                txMap.put(TransferTrans.DR_PROD_ID, applicationMap.get("SUSPENSE_PROD_ID"));
                                txMap.put(TransferTrans.DR_ACT_NUM, applicationMap.get("SUSPENSE_ACC_NO"));
                            }
                        }
                        txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                        txMap.put(TransferTrans.PARTICULARS, map.get("SCHEME_NAME"));
                        txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                        System.out.println("txMap : " + txMap + "amt :" + totalSuspendsAmt);
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, totalSuspendsAmt);
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setInitiatedBranch(BRANCH_ID);
                        if (CommonUtil.convertObjToStr(applicationMap.get("SUSPENSE_GL_ACCNO")).equals("GL")) {
                            transferTo.setTransModType(TransactionFactory.GL);
                        } else {
                            transferTo.setTransModType(TransactionFactory.SUSPENSE);
                        }
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                            transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                        }
                        System.out.println("transferTo List 1 : " + transferTo);
                        TxTransferTO.add(transferTo);
                    }
                    // Debit Bonus Account
                    if (totalBonusAmt > 0) {
                        txMap = new HashMap();
                        txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("BONUS_RECEIVABLE_HEAD"));
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                        txMap.put(TransferTrans.PARTICULARS, map.get("SCHEME_NAME"));
                        txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                        System.out.println("txMap : " + txMap + "amt :" + totalBonusAmt);
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, totalBonusAmt);
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setInitiatedBranch(BRANCH_ID);
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        if (CommonUtil.convertObjToStr(applicationMap.get("SUSPENSE_GL_ACCNO")).equals("GL")) {
                            transferTo.setTransModType(TransactionFactory.GL);
                        } else {
                            transferTo.setTransModType(TransactionFactory.SUSPENSE);
                        }
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                            transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                        }
                        transferTo.setInstrumentNo2("BONUS");// Added by nithya on 31-10-2019 for KD-680
                        System.out.println("transferTo List 1 : " + transferTo);
                        TxTransferTO.add(transferTo);
                    }
                    //Credit Receipt Account
                    for (int i = 0; i < totalList.size(); i++) {
                        System.out.println("###### inside for loop : " + totalList.size());
                        MDSApplicationTO mdsApplicationTO = new MDSApplicationTO();
                        mdsApplicationTO = (MDSApplicationTO) totalList.get(i);
                        if (transactionTO.getTransType().equals("TRANSFER")) {
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            if (CommonUtil.convertObjToDouble(mdsApplicationTO.getInstAmt()).doubleValue() > 0) {
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, mdsApplicationTO.getSchemeName() + "-" + CommonUtil.convertObjToStr(mdsApplicationTO.getChitNo()));
                                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(mdsApplicationTO.getInstAmt()).doubleValue());
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(mdsApplicationTO.getInstAmt()).doubleValue());
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setTransModType("MDS");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setGlTransActNum(mdsApplicationTO.getChittalNo());// Added by nithya on 07-03-2018 for 0009203: MDS Commencement - Transfer Print Credit Side not coming in Report.
                                if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                                    transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                                }
                                transferTo.setInstrumentNo2("INSTALMENT_AMT");// Added by nithya on 31-10-2019 for KD-680
                                System.out.println("transferTo List 2 : " + transferTo);
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
                            }
                            HashMap commencementMap = new HashMap();
                            commencementMap.put("COMMENCEMENT_STATUS", "Y");
                            commencementMap.put("COMMENCEMENT_DATE", map.get("COMMENCEMENT_DATE"));
                            commencementMap.put("TRANS_ID", mdsApplicationTO.getTransId());
                            sqlMap.executeUpdate("updateCommencementStatus", commencementMap);
                        }
                    }
                    HashMap applnMap = new HashMap();
                    transferDAO = new TransferDAO();
                    map.put("MODE", map.get("COMMAND"));
                    map.put("COMMAND", map.get("MODE"));
                    map.put("TxTransferTO", TxTransferTO);
                    transMap = transferDAO.execute(map, false);
                    System.out.println("transactionDAO map : " + map);
                    transactionDAO.setBatchId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                    transactionDAO.setBatchDate(currDt);
                    transactionDAO.execute(map);
                    HashMap linkBatchMap = new HashMap();
                    HashMap remitMap = new HashMap();
//                    lst = sqlMap.executeQueryForList("getSelectMaxTransIdForCash",null);
//                    if(lst!=null && lst.size()>0){
//                        remitMap = (HashMap)lst.get(0);
//                        if(!map.containsKey("LOCKER_SURRENDER_DAO")){
//                            remitMap.put("BATCH_ID", transMap.get("TRANS_ID"));
//                        }
//                        else{
//                            remitMap.put("BATCH_ID", map.get("LOCKER_SURRENDER_ID"));
//                        }
//                        remitMap.put("BATCH_DT",currDt);
//                        sqlMap.executeUpdate("updateRemitIssueTableCashBatchId", remitMap);
//                        linkBatchMap.put("BATCH_ID",transMap.get("TRANS_ID"));
//                        linkBatchMap.put("LINK_BATCH_ID",transMap.get("TRANS_ID"));
//                        linkBatchMap.put("TRANS_DT", currDt);
//                        linkBatchMap.put("INITIATED_BRANCH",_branchCode);
//                        sqlMap.executeUpdate("updateLinkBatchIdTransfer", linkBatchMap);
//                        transMap.put("SCHEME_NAME",map.get("SCHEME_NAME"));
//                        System.out.println("####### transMap :"+transMap);
//                        sqlMap.executeUpdate("updateCommencementTransId", transMap);
//                        linkBatchMap = null;
//                    }
                    linkBatchMap.put("BATCH_ID", transMap.get("TRANS_ID"));
                    linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
                    linkBatchMap.put("TRANS_DT", currDt);
                    linkBatchMap.put("INITIATED_BRANCH", _branchCode);
                    if(transactionTO.getProductType().equals("GL")){
                    sqlMap.executeUpdate("updateLinkBatchIdTransfer", linkBatchMap);
                    }
                    transMap.put("SCHEME_NAME", map.get("SCHEME_NAME"));
                    System.out.println("####### transMap :" + transMap);
                    sqlMap.executeUpdate("updateCommencementTransId", transMap);
                    linkBatchMap = null;
                }
            }
            objLogDAO.addToLog(objLogTO);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void insertDataWithBonusPredifined(HashMap map, LogDAO objLogDAO, LogTO objLogTO, double auction_maxamt, HashMap productMap) throws Exception {
        try {
            System.out.println("###### :" + map);
            ArrayList transferList = new ArrayList();
            TransferTrans transferTrans = new TransferTrans();
            TxTransferTO transferTo = new TxTransferTO();
            ArrayList TxTransferTO = new ArrayList();
            TransactionTO transactionTO = new TransactionTO();
            HashMap txMap = new HashMap();
            String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
            double totalAmt = CommonUtil.convertObjToDouble(map.get("TOTAL_AMOUNT")).doubleValue();
            transferTrans.setInitiatedBranch(BRANCH_ID);
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            transactionDAO.setInitiatedBranch(_branchCode);
            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
            if (map.containsKey("TransactionTO")) {
                HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                }
                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                List totalList = sqlMap.executeQueryForList("getSelectGLTransMDSApplicationTO", map);
                if (totalList != null && totalList.size() > 0) {
                    System.out.println("###### totalListsize() : " + totalList.size());
                    double totalTransAmt = 0.0;
                    double totalSuspendsAmt = 0.0;
                    double totalBonusAmt = 0.0;
                    totalTransAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();                     
                    HashMap bonusMap = new HashMap();
                    bonusMap.put("SCHEME_NAME", map.get("SCHEME_NAME"));
                    List bonusList = sqlMap.executeQueryForList("getTotalBonusPredifinedCommencement", bonusMap);
                    if (bonusList != null && bonusList.size() > 0) {
                        bonusMap = (HashMap) bonusList.get(0);
                        totalBonusAmt = CommonUtil.convertObjToDouble(bonusMap.get("TOTAL_BONUS"));
                    }
//                    totalBonusAmt = totalTransAmt*auction_maxamt/100; 
                    totalSuspendsAmt = totalTransAmt - totalBonusAmt;
      //              System.out.println("######     totalBonusAmt : " + totalBonusAmt);
    //                System.out.println("######  totalSuspendsAmt : " + totalSuspendsAmt);
                    String linkBatchId = CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo());
                    HashMap applicationMap = new HashMap();
                    HashMap transMap = new HashMap();
                    applicationMap.put("SCHEME_NAME", map.get("SCHEME_NAME"));
                    List lst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", applicationMap);
                    if (lst != null && lst.size() > 0) {
                        applicationMap = (HashMap) lst.get(0);
                    }
                    // Debit Suspends Account
                    if (totalSuspendsAmt > 0) {
                        txMap = new HashMap();
                        if (CommonUtil.convertObjToStr(applicationMap.get("SUSPENSE_GL_ACCNO")).equals("GL")) {
                            txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("SUSPENSE_HEAD"));
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        } else {
                            HashMap suspenseMap = new HashMap();                // Suspense Acc Head
                            suspenseMap.put("PROD_ID", applicationMap.get("SUSPENSE_PROD_ID"));
                            List suspenseLst = sqlMap.executeQueryForList("getAccountHeadProdSA", suspenseMap);
                            if (suspenseLst != null && suspenseLst.size() > 0) {
                                suspenseMap = (HashMap) suspenseLst.get(0);
                                txMap.put(TransferTrans.DR_AC_HD, suspenseMap.get("AC_HEAD"));
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.SUSPENSE);
                                txMap.put(TransferTrans.DR_PROD_ID, applicationMap.get("SUSPENSE_PROD_ID"));
                                txMap.put(TransferTrans.DR_ACT_NUM, applicationMap.get("SUSPENSE_ACC_NO"));
                            }
                        }
                        txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                        txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                        txMap.put(TransferTrans.PARTICULARS, map.get("SCHEME_NAME"));
                        System.out.println("txMap : " + txMap + "amt :" + totalSuspendsAmt);
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, totalSuspendsAmt);
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setInitiatedBranch(BRANCH_ID);
                        if (CommonUtil.convertObjToStr(applicationMap.get("SUSPENSE_GL_ACCNO")).equals("GL")) {
                            transferTo.setTransModType(TransactionFactory.GL);
                        } else {
                            transferTo.setTransModType(TransactionFactory.SUSPENSE);
                        }
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                            transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                        }
                        System.out.println("transferTo List 1 : " + transferTo);
                        TxTransferTO.add(transferTo);
                    }
                    // Debit Bonus Account
                    if (totalBonusAmt > 0) {
                        txMap = new HashMap();
                        txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("BONUS_RECEIVABLE_HEAD"));
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                        txMap.put(TransferTrans.PARTICULARS, map.get("SCHEME_NAME"));
                        txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                        System.out.println("txMap : " + txMap + "amt :" + totalBonusAmt);
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, totalBonusAmt);
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setInitiatedBranch(BRANCH_ID);
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        if (CommonUtil.convertObjToStr(applicationMap.get("SUSPENSE_GL_ACCNO")).equals("GL")) {
                            transferTo.setTransModType(TransactionFactory.GL);
                        } else {
                            transferTo.setTransModType(TransactionFactory.SUSPENSE);
                        }
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                            transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                        }
                        transferTo.setInstrumentNo2("BONUS");// Added by nithya on 31-10-2019 for KD-680
                        System.out.println("transferTo List 1 : " + transferTo);
                        TxTransferTO.add(transferTo);
                    }
                    //Credit Receipt Account
                    for (int i = 0; i < totalList.size(); i++) {
                        System.out.println("###### inside for loop : " + totalList.size());
                        MDSApplicationTO mdsApplicationTO = new MDSApplicationTO();
                        mdsApplicationTO = (MDSApplicationTO) totalList.get(i);
                        if (transactionTO.getTransType().equals("TRANSFER")) {
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            if (CommonUtil.convertObjToDouble(mdsApplicationTO.getInstAmt()).doubleValue() > 0) {
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, mdsApplicationTO.getSchemeName() + "-" +CommonUtil.convertObjToStr(mdsApplicationTO.getChitNo()));
                                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(mdsApplicationTO.getInstAmt()).doubleValue());
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(mdsApplicationTO.getInstAmt()).doubleValue());
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setTransModType("MDS");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setGlTransActNum(mdsApplicationTO.getChittalNo());// Added by nithya on 07-03-2018 for 0009203: MDS Commencement - Transfer Print Credit Side not coming in Report.
                                if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                                    transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                                }
                                System.out.println("transferTo List 2 : " + transferTo);
                                transferTo.setInstrumentNo2("INSTALMENT_AMT");// Added by nithya on 31-10-2019 for KD-680
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
                            }
                            HashMap commencementMap = new HashMap();
                            commencementMap.put("COMMENCEMENT_STATUS", "Y");
                            commencementMap.put("COMMENCEMENT_DATE", map.get("COMMENCEMENT_DATE"));
                            commencementMap.put("TRANS_ID", mdsApplicationTO.getTransId());
                            sqlMap.executeUpdate("updateCommencementStatus", commencementMap);
                        }
                    }
                    HashMap applnMap = new HashMap();
                    transferDAO = new TransferDAO();
                    map.put("MODE", map.get("COMMAND"));
                    map.put("COMMAND", map.get("MODE"));
                    map.put("TxTransferTO", TxTransferTO);
                    transMap = transferDAO.execute(map, false);
                    System.out.println("transactionDAO map : " + map);
                    transactionDAO.setBatchId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                    transactionDAO.setBatchDate(currDt);
                    transactionDAO.execute(map);
                    HashMap linkBatchMap = new HashMap();
                    HashMap remitMap = new HashMap();
//                    lst = sqlMap.executeQueryForList("getSelectMaxTransIdForCash",null);
//                    if(lst!=null && lst.size()>0){
//                        remitMap = (HashMap)lst.get(0);
//                        if(!map.containsKey("LOCKER_SURRENDER_DAO")){
//                            remitMap.put("BATCH_ID", transMap.get("TRANS_ID"));
//                        }
//                        else{
//                            remitMap.put("BATCH_ID", map.get("LOCKER_SURRENDER_ID"));
//                        }
//                        remitMap.put("BATCH_DT",currDt);
//                        sqlMap.executeUpdate("updateRemitIssueTableCashBatchId", remitMap);
//                        linkBatchMap.put("BATCH_ID",transMap.get("TRANS_ID"));
//                        linkBatchMap.put("LINK_BATCH_ID",transMap.get("TRANS_ID"));
//                        linkBatchMap.put("TRANS_DT", currDt);
//                        linkBatchMap.put("INITIATED_BRANCH",_branchCode);
//                        sqlMap.executeUpdate("updateLinkBatchIdTransfer", linkBatchMap);
//                        transMap.put("SCHEME_NAME",map.get("SCHEME_NAME"));
//                        System.out.println("####### transMap :"+transMap);
//                        sqlMap.executeUpdate("updateCommencementTransId", transMap);
//                        linkBatchMap = null;
//                    }
                    linkBatchMap.put("BATCH_ID", transMap.get("TRANS_ID"));
                    linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
                    linkBatchMap.put("TRANS_DT", currDt);
                    linkBatchMap.put("INITIATED_BRANCH", _branchCode);
                    if(transactionTO.getProductType().equals("GL")){
                    sqlMap.executeUpdate("updateLinkBatchIdTransfer", linkBatchMap);
                    }
                    transMap.put("SCHEME_NAME", map.get("SCHEME_NAME"));
                    System.out.println("####### transMap :" + transMap);
                    sqlMap.executeUpdate("updateCommencementTransId", transMap);
                    linkBatchMap = null;
                }
            }
            objLogDAO.addToLog(objLogTO);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
    
    private void insertData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            System.out.println("###### :" + map);
            ArrayList transferList = new ArrayList();
            TransferTrans transferTrans = new TransferTrans();
            TxTransferTO transferTo = new TxTransferTO();
            ArrayList TxTransferTO = new ArrayList();
            TransactionTO transactionTO = new TransactionTO();
            HashMap txMap = new HashMap();
            String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
            double totalAmt = CommonUtil.convertObjToDouble(map.get("TOTAL_AMOUNT")).doubleValue();
            transferTrans.setInitiatedBranch(BRANCH_ID);
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            transactionDAO.setInitiatedBranch(_branchCode);
            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
            if (map.containsKey("TransactionTO")) {
                HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                }
                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                List totalList = sqlMap.executeQueryForList("getSelectGLTransMDSApplicationTO", map);
                if (totalList != null && totalList.size() > 0) {
                    System.out.println("###### : " + totalList.size());
                    String linkBatchId = CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo());
                    HashMap applicationMap = new HashMap();
                    HashMap transMap = new HashMap();
                    applicationMap.put("SCHEME_NAME", map.get("SCHEME_NAME"));
                    List lst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", applicationMap);
                    if (lst != null && lst.size() > 0) {
                        applicationMap = (HashMap) lst.get(0);
                    }
                    txMap = new HashMap();
                    if (CommonUtil.convertObjToStr(applicationMap.get("SUSPENSE_GL_ACCNO")).equals("GL")) {
                        txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("SUSPENSE_HEAD"));
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    } else {
                        HashMap suspenseMap = new HashMap();                // Suspense Acc Head
                        suspenseMap.put("PROD_ID", applicationMap.get("SUSPENSE_PROD_ID"));
                        List suspenseLst = sqlMap.executeQueryForList("getAccountHeadProdSA", suspenseMap);
                        if (suspenseLst != null && suspenseLst.size() > 0) {
                            suspenseMap = (HashMap) suspenseLst.get(0);
                            txMap.put(TransferTrans.DR_AC_HD, suspenseMap.get("AC_HEAD"));
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.SUSPENSE);
                            txMap.put(TransferTrans.DR_PROD_ID, applicationMap.get("SUSPENSE_PROD_ID"));
                            txMap.put(TransferTrans.DR_ACT_NUM, applicationMap.get("SUSPENSE_ACC_NO"));
                        }
                    }
                    txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                    txMap.put(TransferTrans.PARTICULARS, map.get("SCHEME_NAME"));
                    txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                    System.out.println("txMap : " + txMap + "amt :" + CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue());
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue());
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setInitiatedBranch(BRANCH_ID);
                    if (CommonUtil.convertObjToStr(applicationMap.get("SUSPENSE_GL_ACCNO")).equals("GL")) {
                        transferTo.setTransModType(TransactionFactory.GL);
                    } else {
                        transferTo.setTransModType(TransactionFactory.SUSPENSE);
                    }
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                        transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                    }
                    System.out.println("transferTo List 1 : " + transferTo);
                    TxTransferTO.add(transferTo);
                    for (int i = 0; i < totalList.size(); i++) {
                        System.out.println("###### inside for loop : " + totalList.size());
                        MDSApplicationTO mdsApplicationTO = new MDSApplicationTO();
                        mdsApplicationTO = (MDSApplicationTO) totalList.get(i);
                        if (transactionTO.getTransType().equals("TRANSFER")) {
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            if (CommonUtil.convertObjToDouble(mdsApplicationTO.getInstAmt()).doubleValue() > 0) {
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, mdsApplicationTO.getSchemeName() + "-" +CommonUtil.convertObjToStr(mdsApplicationTO.getChitNo()));
                                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(mdsApplicationTO.getInstAmt()).doubleValue());
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(mdsApplicationTO.getInstAmt()).doubleValue());
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setTransModType("MDS");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setGlTransActNum(mdsApplicationTO.getChittalNo());// Added by nithya on 07-03-2018 for 0009203: MDS Commencement - Transfer Print Credit Side not coming in Report.
                                if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                                    transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                                }
                                transferTo.setInstrumentNo2("INSTALMENT_AMT");// Added by nithya on 31-10-2019 for KD-680
                                System.out.println("transferTo List 2 : " + transferTo);
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
                            }
                            HashMap commencementMap = new HashMap();
                            commencementMap.put("COMMENCEMENT_STATUS", "Y");
                            commencementMap.put("COMMENCEMENT_DATE", map.get("COMMENCEMENT_DATE"));
                            commencementMap.put("TRANS_ID", mdsApplicationTO.getTransId());
                            sqlMap.executeUpdate("updateCommencementStatus", commencementMap);
                        }
                    }
                    HashMap applnMap = new HashMap();
                    transferDAO = new TransferDAO();
                    map.put("MODE", map.get("COMMAND"));
                    map.put("COMMAND", map.get("MODE"));
                    map.put("TxTransferTO", TxTransferTO);
                    transMap = transferDAO.execute(map, false);
                    System.out.println("transactionDAO map : " + map);
                    transactionDAO.setBatchId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                    transactionDAO.setBatchDate(currDt);
                    transactionDAO.execute(map);
                    HashMap linkBatchMap = new HashMap();
                    HashMap remitMap = new HashMap();
//                    lst = sqlMap.executeQueryForList("getSelectMaxTransIdForCash",null);
//                    if(lst!=null && lst.size()>0){
//                        remitMap = (HashMap)lst.get(0);
//                        if(!map.containsKey("LOCKER_SURRENDER_DAO")){
//                            remitMap.put("BATCH_ID", transMap.get("TRANS_ID"));
//                        }
//                        else{
//                            remitMap.put("BATCH_ID", map.get("LOCKER_SURRENDER_ID"));
//                        }
//                        remitMap.put("BATCH_DT",currDt);
//                        sqlMap.executeUpdate("updateRemitIssueTableCashBatchId", remitMap);
//                        linkBatchMap.put("BATCH_ID",transMap.get("TRANS_ID"));
//                        linkBatchMap.put("LINK_BATCH_ID",transMap.get("TRANS_ID"));
//                        linkBatchMap.put("TRANS_DT", currDt);
//                        linkBatchMap.put("INITIATED_BRANCH",_branchCode);
//                        sqlMap.executeUpdate("updateLinkBatchIdTransfer", linkBatchMap);
//                        transMap.put("SCHEME_NAME",map.get("SCHEME_NAME"));
//                        System.out.println("####### transMap :"+transMap);
//                        sqlMap.executeUpdate("updateCommencementTransId", transMap);
//                        linkBatchMap = null;
//                    }
                    linkBatchMap.put("BATCH_ID", transMap.get("TRANS_ID"));
                    linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
                    linkBatchMap.put("TRANS_DT", currDt);
                    linkBatchMap.put("INITIATED_BRANCH", _branchCode);
                    if(transactionTO.getProductType().equals("GL")){
                    sqlMap.executeUpdate("updateLinkBatchIdTransfer", linkBatchMap);
                    }
                    transMap.put("SCHEME_NAME", map.get("SCHEME_NAME"));
                    System.out.println("####### transMap :" + transMap);
                    sqlMap.executeUpdate("updateCommencementTransId", transMap);
                    linkBatchMap = null;
                }
            }
            objLogDAO.addToLog(objLogTO);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        System.out.println("map :" + map);
        ArrayList transferList = new ArrayList();
        TransferTrans transferTrans = new TransferTrans();
        TxTransferTO transferTo = new TxTransferTO();
        ArrayList TxTransferTO = new ArrayList();
        TransactionTO transactionTO = new TransactionTO();
        HashMap txMap = new HashMap();
        String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        transferTrans.setInitiatedBranch(BRANCH_ID);
        HashMap tempMap = new HashMap();
        HashMap deleteMap = new HashMap();
        double oldAmount = 0.0;
        if (map.containsKey("TransactionTO")) {
            HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
            LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
            if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                HashMap applnAuthMap = new HashMap();
                HashMap applicationMap = new HashMap();
                MDSApplicationTO mdsApplicationTO = new MDSApplicationTO();
                List totalList = sqlMap.executeQueryForList("getSelectGLTransNotAuthMDSApplicationTO", map);
                if (totalList != null && totalList.size() > 0) {
                    System.out.println("###### : " + totalList.size());
                    for (int i = 0; i < totalList.size(); i++) {
                        System.out.println("###### inside for loop : " + totalList.size());
                        mdsApplicationTO = new MDSApplicationTO();
                        mdsApplicationTO = (MDSApplicationTO) totalList.get(i);
                        if (map.get("COMMAND").equals("DELETE")) {
                            mdsApplicationTO.setShadowCredit(mdsApplicationTO.getInstAmt());
                            sqlMap.executeUpdate("updateMDSShadowCreditCrAmt", mdsApplicationTO);
                            mdsApplicationTO.setTotalBalance(mdsApplicationTO.getInstAmt());
                            mdsApplicationTO.setClearBalance(mdsApplicationTO.getInstAmt());
                            mdsApplicationTO.setAvailableBalance(mdsApplicationTO.getInstAmt());
//                            mdsApplicationTO.setStatus("DELETED");
                            System.out.println("########" + mdsApplicationTO);
                            sqlMap.executeUpdate("updateMDSShadowCreditDrAmt", mdsApplicationTO);
                            HashMap commencementMap = new HashMap();
                            commencementMap.put("TRANS_ID", mdsApplicationTO.getTransId());
                            sqlMap.executeUpdate("updateCommencementStatusNull", commencementMap);
                            mdsApplicationTO.setShadowCredit(mdsApplicationTO.getInstAmt());
                            sqlMap.executeUpdate("updateMDSShadowCreditCrAmt", mdsApplicationTO);
                            mdsApplicationTO.setTotalBalance(mdsApplicationTO.getInstAmt());
                            mdsApplicationTO.setClearBalance(mdsApplicationTO.getInstAmt());
                            mdsApplicationTO.setAvailableBalance(mdsApplicationTO.getInstAmt());
                            System.out.println("########" + mdsApplicationTO);
                            sqlMap.executeUpdate("updateMDSShadowCreditDrAmt", mdsApplicationTO);
                            System.out.println("mdsApplicationTO :" + mdsApplicationTO);
                            sqlMap.executeUpdate("updateApplicationTO", mdsApplicationTO);

                        }
                    }
                    TxTransferTO txTransferTO = null;
                    ArrayList batchList = new ArrayList();
                    HashMap oldAmountMap = new HashMap();
                    String commandMode = CommonUtil.convertObjToStr(map.get("COMMAND"));
                    tempMap.put("BATCHID", mdsApplicationTO.getCommencementTransId());
                    tempMap.put("TRANS_DT", currDt.clone());
                    tempMap.put("INITIATED_BRANCH", BRANCH_ID);
                    if (transactionTO.getTransType().equals("TRANSFER")) {
                        List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                        if (lst != null) {
                            for (int i = 0; i < lst.size(); i++) {
                                txTransferTO = (TxTransferTO) lst.get(i);
                                if (map.get("COMMAND").equals("DELETE")) {
                                    txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                    txTransferTO.setStatusDt(currDt);
                                    txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                                    oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                                    batchList.add(txTransferTO);
                                }
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
            }
        }
    }

   private void authorizePredefined(HashMap map) throws Exception {
        System.out.println("AuthorizeMap :" + map);
        HashMap authorizeMap = new HashMap();
        authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
        System.out.println("map :" + map);
        String authorizeStatus = CommonUtil.convertObjToStr(authorizeMap.get("AUTHORIZESTATUS"));
        if (map.containsKey("TransactionTO")) {
            HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
            TransactionTO transactionTO = new TransactionTO();
            LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
            if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                HashMap applnAuthMap = new HashMap();
                HashMap applicationMap = new HashMap();
                HashMap predefinedMap = new HashMap();
                List totalList = sqlMap.executeQueryForList("getSelectGLTransNotAuthMDSApplicationTO", map);
                if (totalList != null && totalList.size() > 0) {
                    System.out.println("###### : " + totalList.size());
                    List predefinedList = sqlMap.executeQueryForList("getSelectMdsPredefinedChittalDetails", map);
                    if (predefinedList != null && predefinedList.size() > 0) {
                        predefinedMap = (HashMap) predefinedList.get(0);
                    }
                    for (int i = 0; i < totalList.size(); i++) {
                        System.out.println("###### inside for loop : " + totalList.size());
                        MDSApplicationTO mdsApplicationTO = new MDSApplicationTO();
                        mdsApplicationTO = (MDSApplicationTO) totalList.get(i);
                        if (authorizeStatus.equals("AUTHORIZED")) {
                            mdsApplicationTO.setShadowCredit(mdsApplicationTO.getInstAmt());
                            sqlMap.executeUpdate("updateMDSShadowCreditCrAmt", mdsApplicationTO);
                            mdsApplicationTO.setTotalBalance(mdsApplicationTO.getInstAmt());
                            mdsApplicationTO.setClearBalance(mdsApplicationTO.getInstAmt());
                            mdsApplicationTO.setAvailableBalance(mdsApplicationTO.getInstAmt());
                            System.out.println("########" + mdsApplicationTO);
                            sqlMap.executeUpdate("updateMDSShadowCreditDrAmt", mdsApplicationTO);
                            sqlMap.executeUpdate("updateMDSAvailBalance", mdsApplicationTO);
                            sqlMap.executeUpdate("updateMDSClearBalance", mdsApplicationTO);
                            sqlMap.executeUpdate("updateMDSTotalBalance", mdsApplicationTO);
                            applnAuthMap.put("COMMENCEMENT_AUTHORIZE_STATUS", authorizeStatus);
                            applnAuthMap.put("LAST_TRANS_DT", currDt.clone());
                            applnAuthMap.put("TRANS_ID", mdsApplicationTO.getTransId());
                            applnAuthMap.put("SCHEME_NAME", map.get("SCHEME_NAME"));
                            applnAuthMap.put("INST_COUNT", new Double(1));
                            System.out.println("$#@$#@#@$#@applnAuthMap :" + applnAuthMap);
                            sqlMap.executeUpdate("updateMDSCommencementAuthStatus", applnAuthMap);

                            HashMap mdsTransMap = new HashMap();
                            mdsTransMap.put("SCHEME_NAME", mdsApplicationTO.getSchemeName());
                            mdsTransMap.put("CHITTAL_NO", mdsApplicationTO.getChittalNo());
                            mdsTransMap.put("INST_AMT", mdsApplicationTO.getInstAmt());
                            mdsTransMap.put("NO_OF_INST", new Integer(1));
                            mdsTransMap.put("PENAL_AMT", new Double(0));
                            mdsTransMap.put("BONUS_AMT", CommonUtil.convertObjToDouble(predefinedMap.get("NEXT_BONUS_AMOUNT")));
                            mdsTransMap.put("DISCOUNT_AMT", new Double(0));
                            mdsTransMap.put("MDS_INTEREST", new Double(0));
                            mdsTransMap.put("FORFEITED_AMT", new Double(0)); //Added by nithya for KD-2093
                            mdsTransMap.put("BANK_ADV_AMTCR",new Double(0));
                            mdsTransMap.put("NET_AMT", mdsApplicationTO.getInstAmt()-CommonUtil.convertObjToDouble(predefinedMap.get("NEXT_BONUS_AMOUNT")));
                            //mdsTransMap.put("SUBSCRIPTION_AMT", mdsApplicationTO.getInstAmt());//01-11-2019
                            mdsTransMap.put("STATUS", CommonConstants.STATUS_CREATED);
                            mdsTransMap.put("STATUS_DT", currDt);
                            mdsTransMap.put("STATUS_BY", map.get("USER_ID"));
                            mdsTransMap.put("AUTHORIZE_STATUS", authorizeStatus);
                            mdsTransMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                            mdsTransMap.put("AUTHORIZE_DT", currDt.clone());
                            mdsTransMap.put("TRANS_DT", currDt.clone());
                            mdsTransMap.put("NET_TRANS_ID", mdsApplicationTO.getCommencementTransId());
                            mdsTransMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsApplicationTO.getSubNo()));
                            Date cmDt=mdsApplicationTO.getCommencementDate();
                            String mnthNdYr ="";
                            if (cmDt != null) {
                                int m = DateUtil.getMonth(cmDt)-1;
                                DateFormatSymbols dfs = new DateFormatSymbols();
                                String mnths[] = dfs.getMonths();
                                if (mnths.length > 0) {
                                    if (m >= 0 && m <= 11) {
                                        mnthNdYr = mnths[m];
                                        mnthNdYr = mnthNdYr.substring(0, 3);
                                    }
                                }
                                mnthNdYr = mnthNdYr +"/"+DateUtil.getYear(cmDt);
                            }
                            mdsTransMap.put("NARRATION", "Inst#1 "+mnthNdYr);
                            sqlMap.executeUpdate("updateMDSTransDetailsEachRec", mdsTransMap);
                            //Added by sreekrishnan for sms alert..
                                HashMap smsAlertMap = new HashMap();
                                SmsConfigDAO smsDAO = new SmsConfigDAO();
                                smsAlertMap.put("SCHEME_NAME", mdsApplicationTO.getSchemeName());
                                smsAlertMap.put("CHITTAL_NO", mdsApplicationTO.getChittalNo());
                                System.out.println("smsAlertMap%#%#%#%#%#%  " + smsAlertMap);
                                List MdsSmsList = sqlMap.executeQueryForList("getMDsDetailsForSMS", smsAlertMap);
                                if (MdsSmsList != null && MdsSmsList.size() > 0) {
                                    HashMap MdsSmsMap = (HashMap) MdsSmsList.get(0);
                                    System.out.println("MdsSmsMap%#%#%#^#^#^#^" + MdsSmsMap);
                                    if (MdsSmsMap != null && !MdsSmsMap.equals("")) {
                                        MdsSmsMap.put(CommonConstants.TRANS_ID, mdsApplicationTO.getCommencementTransId());
                                        MdsSmsMap.put("TRANS_DT", currDt.clone());
                                        List MdsTransList = sqlMap.executeQueryForList("getMdsCommensmentForSms", MdsSmsMap);
                                        if (MdsTransList != null && MdsTransList.size() > 0) {
                                            HashMap MdsTransMap = (HashMap) MdsTransList.get(0);
                                            System.out.println("MdsTransMap %#%#%#^#^#^#^" + MdsTransMap);
                                            MdsTransMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                                            smsDAO.MdsReceiptsSmsConfiguration(MdsTransMap);
                                            MdsTransList = null;
                                        }
                                    }
                                    MdsSmsList = null;
                                }                           
                        } else if (authorizeStatus.equals("REJECTED")) {
                            mdsApplicationTO.setShadowCredit(mdsApplicationTO.getInstAmt());
                            sqlMap.executeUpdate("updateMDSShadowCreditCrAmt", mdsApplicationTO);
                            mdsApplicationTO.setTotalBalance(mdsApplicationTO.getInstAmt());
                            mdsApplicationTO.setClearBalance(mdsApplicationTO.getInstAmt());
                            mdsApplicationTO.setAvailableBalance(mdsApplicationTO.getInstAmt());
                            System.out.println("########" + mdsApplicationTO);
                            sqlMap.executeUpdate("updateMDSShadowCreditDrAmt", mdsApplicationTO);

                            HashMap commencementMap = new HashMap();
                            //                            commencementMap.put("COMMENCEMENT_STATUS","");
                            //                            commencementMap.put("COMMENCEMENT_DATE",null);
                            commencementMap.put("TRANS_ID", mdsApplicationTO.getTransId());
                            sqlMap.executeUpdate("updateCommencementStatusNull", commencementMap);
                        }
                    }
                    if (transactionTO.getTransType().equals("TRANSFER")) {
                        System.out.println("transactionTO.getTransType() :" + transactionTO.getTransType());
                        HashMap cashAuthMap = new HashMap();
                        applnAuthMap.put("AUTHORIZE_STATUS", authorizeStatus);
                        applnAuthMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                        applnAuthMap.put("TRANS_ID", transactionTO.getBatchId());
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
                        applnAuthMap = null;
                    }
                }
            }
        }
    }
      
    private void authorize(HashMap map) throws Exception {
        System.out.println("AuthorizeMap :" + map);
        double discountAmt = 0.0;
        String discountFirstScheme = "N"; // Added by nithya on 23-12-2020 for KD-2521
        HashMap authorizeMap = new HashMap();
        authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
        System.out.println("map :" + map);
        String authorizeStatus = CommonUtil.convertObjToStr(authorizeMap.get("AUTHORIZESTATUS"));
        if (map.containsKey("TransactionTO")) {
            HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
            TransactionTO transactionTO = new TransactionTO();
            LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
            if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                if(map.containsKey("DISCOUNT_FIRST_INSTALLMENT") && map.get("DISCOUNT_FIRST_INSTALLMENT") != null && map.get("DISCOUNT_FIRST_INSTALLMENT").equals("Y")){ // Added by nithya on 23-12-2020 for KD-2521
                    discountAmt = CommonUtil.convertObjToDouble(map.get("DISCOUNT_AMOUNT"));
                    discountFirstScheme = "Y";
                }
                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                HashMap applnAuthMap = new HashMap();
                HashMap applicationMap = new HashMap();
                List totalList = sqlMap.executeQueryForList("getSelectGLTransNotAuthMDSApplicationTO", map);
                if (totalList != null && totalList.size() > 0) {
                    System.out.println("###### : " + totalList.size());
                    for (int i = 0; i < totalList.size(); i++) {
                        System.out.println("###### inside for loop : " + totalList.size());
                        MDSApplicationTO mdsApplicationTO = new MDSApplicationTO();
                        mdsApplicationTO = (MDSApplicationTO) totalList.get(i);
                        if (authorizeStatus.equals("AUTHORIZED")) {
                            mdsApplicationTO.setShadowCredit(mdsApplicationTO.getInstAmt());
                            sqlMap.executeUpdate("updateMDSShadowCreditCrAmt", mdsApplicationTO);
                            mdsApplicationTO.setTotalBalance(mdsApplicationTO.getInstAmt());
                            mdsApplicationTO.setClearBalance(mdsApplicationTO.getInstAmt());
                            mdsApplicationTO.setAvailableBalance(mdsApplicationTO.getInstAmt());
                            System.out.println("########" + mdsApplicationTO);
                            sqlMap.executeUpdate("updateMDSShadowCreditDrAmt", mdsApplicationTO);
                            sqlMap.executeUpdate("updateMDSAvailBalance", mdsApplicationTO);
                            sqlMap.executeUpdate("updateMDSClearBalance", mdsApplicationTO);
                            sqlMap.executeUpdate("updateMDSTotalBalance", mdsApplicationTO);
                            applnAuthMap.put("COMMENCEMENT_AUTHORIZE_STATUS", authorizeStatus);
                            applnAuthMap.put("LAST_TRANS_DT", currDt);
                            applnAuthMap.put("TRANS_ID", mdsApplicationTO.getTransId());
                            applnAuthMap.put("SCHEME_NAME", map.get("SCHEME_NAME"));
                            applnAuthMap.put("INST_COUNT", new Double(1));
                            System.out.println("$#@$#@#@$#@applnAuthMap :" + applnAuthMap);
                            sqlMap.executeUpdate("updateMDSCommencementAuthStatus", applnAuthMap);

                            HashMap mdsTransMap = new HashMap();
                            mdsTransMap.put("SCHEME_NAME", mdsApplicationTO.getSchemeName());
                            mdsTransMap.put("CHITTAL_NO", mdsApplicationTO.getChittalNo());
                            mdsTransMap.put("INST_AMT", mdsApplicationTO.getInstAmt());
                            mdsTransMap.put("NO_OF_INST", new Integer(1));
                            mdsTransMap.put("PENAL_AMT", new Double(0));
                            mdsTransMap.put("BONUS_AMT", new Double(0));
                            mdsTransMap.put("FORFEITED_AMT", new Double(0)); //Added by nithya for KD-2093
                            mdsTransMap.put("BANK_ADV_AMTCR", new Double(0));
                            if(discountFirstScheme.equalsIgnoreCase("Y")){// Added by nithya on 23-12-2020 for KD-2521
                               mdsTransMap.put("DISCOUNT_AMT", discountAmt); 
                            }else{
                               mdsTransMap.put("DISCOUNT_AMT", new Double(0));
                            }
                            mdsTransMap.put("MDS_INTEREST", new Double(0));
                            mdsTransMap.put("NET_AMT", mdsApplicationTO.getInstAmt());
                            //mdsTransMap.put("SUBSCRIPTION_AMT", mdsApplicationTO.getInstAmt());//01-11-2019
                            mdsTransMap.put("STATUS", CommonConstants.STATUS_CREATED);
                            mdsTransMap.put("STATUS_DT", currDt);
                            mdsTransMap.put("STATUS_BY", map.get("USER_ID"));
                            mdsTransMap.put("AUTHORIZE_STATUS", authorizeStatus);
                            mdsTransMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                            mdsTransMap.put("AUTHORIZE_DT", currDt);
                            mdsTransMap.put("TRANS_DT", currDt);
                            mdsTransMap.put("NET_TRANS_ID", mdsApplicationTO.getCommencementTransId());
                            mdsTransMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsApplicationTO.getSubNo()));
                            Date cmDt=mdsApplicationTO.getCommencementDate();
                            String mnthNdYr ="";
                            if (cmDt != null) {
                                int m = DateUtil.getMonth(cmDt)-1;
                                DateFormatSymbols dfs = new DateFormatSymbols();
                                String mnths[] = dfs.getMonths();
                                if (mnths.length > 0) {
                                    if (m >= 0 && m <= 11) {
                                        mnthNdYr = mnths[m];
                                        mnthNdYr = mnthNdYr.substring(0, 3);
                                    }
                                }
                                mnthNdYr = mnthNdYr +"/"+DateUtil.getYear(cmDt);
                            }
                            mdsTransMap.put("NARRATION", "Inst#1 "+mnthNdYr);
                            sqlMap.executeUpdate("updateMDSTransDetailsEachRec", mdsTransMap);
                            //Added by sreekrishnan for sms alert..
                                HashMap smsAlertMap = new HashMap();
                                SmsConfigDAO smsDAO = new SmsConfigDAO();
                                smsAlertMap.put("SCHEME_NAME", mdsApplicationTO.getSchemeName());
                                smsAlertMap.put("CHITTAL_NO", mdsApplicationTO.getChittalNo());
                                System.out.println("smsAlertMap%#%#%#%#%#%  " + smsAlertMap);
                                List MdsSmsList = sqlMap.executeQueryForList("getMDsDetailsForSMS", smsAlertMap);
                                if (MdsSmsList != null && MdsSmsList.size() > 0) {
                                    HashMap MdsSmsMap = (HashMap) MdsSmsList.get(0);
                                    System.out.println("MdsSmsMap%#%#%#^#^#^#^" + MdsSmsMap);
                                    if (MdsSmsMap != null && !MdsSmsMap.equals("")) {
                                        MdsSmsMap.put(CommonConstants.TRANS_ID, mdsApplicationTO.getCommencementTransId());
                                        MdsSmsMap.put("TRANS_DT", currDt);
                                        List MdsTransList = sqlMap.executeQueryForList("getMdsCommensmentForSms", MdsSmsMap);
                                        if (MdsTransList != null && MdsTransList.size() > 0) {
                                            HashMap MdsTransMap = (HashMap) MdsTransList.get(0);
                                            System.out.println("MdsTransMap %#%#%#^#^#^#^" + MdsTransMap);
                                            MdsTransMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                                            smsDAO.MdsReceiptsSmsConfiguration(MdsTransMap);
                                            MdsTransList = null;
                                        }
                                    }
                                    MdsSmsList = null;
                                }                           
                        } else if (authorizeStatus.equals("REJECTED")) {
                            mdsApplicationTO.setShadowCredit(mdsApplicationTO.getInstAmt());
                            sqlMap.executeUpdate("updateMDSShadowCreditCrAmt", mdsApplicationTO);
                            mdsApplicationTO.setTotalBalance(mdsApplicationTO.getInstAmt());
                            mdsApplicationTO.setClearBalance(mdsApplicationTO.getInstAmt());
                            mdsApplicationTO.setAvailableBalance(mdsApplicationTO.getInstAmt());
                            System.out.println("########" + mdsApplicationTO);
                            sqlMap.executeUpdate("updateMDSShadowCreditDrAmt", mdsApplicationTO);

                            HashMap commencementMap = new HashMap();
                            //                            commencementMap.put("COMMENCEMENT_STATUS","");
                            //                            commencementMap.put("COMMENCEMENT_DATE",null);
                            commencementMap.put("TRANS_ID", mdsApplicationTO.getTransId());
                            sqlMap.executeUpdate("updateCommencementStatusNull", commencementMap);
                        }
                    }
                    if (transactionTO.getTransType().equals("TRANSFER")) {
                        System.out.println("transactionTO.getTransType() :" + transactionTO.getTransType());
                        HashMap cashAuthMap = new HashMap();
                        applnAuthMap.put("AUTHORIZE_STATUS", authorizeStatus);
                        applnAuthMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                        applnAuthMap.put("TRANS_ID", transactionTO.getBatchId());
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
                        applnAuthMap = null;
                    }
                }
            }
        }
    }

    public static void main(String str[]) {
        try {
            MDSCommencementDAO dao = new MDSCommencementDAO();
            HashMap inputMap = new HashMap();
            //inputMap.put("ACCT_HD", "CCS_D");

            System.out.println(sqlMap.executeQueryForList("OperativeAcctProduct.getSelectAcctHeadTOList", null));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        /*
         * To Verify The Account Head data...
         */
        System.out.println("Map in DAO: " + map);
        if (map.containsKey("ACCT_HD")) {
            ServerUtil.verifyAccountHead(map);
        }
        /*
         * data fot the Normal operations like Insert, Update, and/or Delete...
         */
        // Log DAO
        LogDAO objLogDAO = new LogDAO();

        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));

        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));

        if (map.containsKey("MDS_CLOSURE")) {
            returnMap = new HashMap();
            closureActionPerform(map);
        } else {
            //            operativeAcctProductTO.setSuserId((String) map.get(CommonConstants.USER_ID));
            /*
             * No User id is supplied from the OB, so it cannot be obtained from
             * the HashMap map...
             */
            //            operativeAcctProductTO.setSuserId((String) CommonConstants.USER_ID);
            try {
                sqlMap.startTransaction();
                objLogTO.setStatus(command);

                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    HashMap productMap = new HashMap();
                    List productLst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", map);
                    if (productLst != null && productLst.size() > 0) {
                        productMap = (HashMap) productLst.get(0);
                        double auction_maxamt = 0.0;
                        if (productMap.get("BONUS_FIRST_INSTALLMENT").equals("Y")) {
                            auction_maxamt = CommonUtil.convertObjToDouble(productMap.get("AUCTION_MAXAMT")).doubleValue();
                            if(productMap.get("PREDEFINITION_INSTALLMENT")!=null && !productMap.get("PREDEFINITION_INSTALLMENT").equals("")
                                && productMap.get("PREDEFINITION_INSTALLMENT").equals("Y")){
                                insertDataWithBonusPredifined(map, objLogDAO, objLogTO, auction_maxamt, productMap);
                            }else{
                                insertDataWithBonus(map, objLogDAO, objLogTO, auction_maxamt, productMap);
                            }
                        }else if(productMap.containsKey("DISCOUNT_FIRST_INSTALLMENT") && productMap.get("DISCOUNT_FIRST_INSTALLMENT") != null && productMap.get("DISCOUNT_FIRST_INSTALLMENT").equals("Y")){
                           insertDataWithDiscount(map, objLogDAO, objLogTO, auction_maxamt, productMap); // Added by nithya on 23-12-2020 for KD-2521
                        }else {
                            insertData(map, objLogDAO, objLogTO);
                        }
                    }
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData(map, objLogDAO, objLogTO);
                } else if (map.containsKey("AUTHORIZEMAP")) {
                    if(map.get("PREDEFINITION_INSTALLMENT")!=null && !map.get("PREDEFINITION_INSTALLMENT").equals("")
                        && map.get("PREDEFINITION_INSTALLMENT").equals("Y")){
                        authorizePredefined(map);
                    }else{
                        authorize(map);
                    }
                } else {
                    throw new NoCommandException();
                }
                sqlMap.commitTransaction();
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw e;
            }
        }
        objLogDAO = null;
        objLogTO = null;
        destroyObjects();
        //
        //        }
        System.out.println("@#$@@$@@@$ returnMap : " + returnMap);
        return (HashMap) returnMap;
    }

    private void closureActionPerform(HashMap map) throws Exception {
        String cmd = CommonUtil.convertObjToStr(map.get("COMMAND"));
        try {
            sqlMap.startTransaction();
            if (cmd.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertMDSClosureTransaction(map);
            } else if (cmd.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteMDSClosureTransaction(map);
            } else if (map.containsKey("AUTHORIZEMAP")) {
                authorizeMDSClosure(map);
            } else {
                throw new NoCommandException();
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void insertMDSClosureTransaction(HashMap map) throws Exception {
        try {

            MDSClosureMap = (HashMap) map.get("MDS_CLOSURE");
            System.out.print(" ###### MDSClosureMap : " + MDSClosureMap);
            String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
            TxTransferTO transferTo = new TxTransferTO();
            ArrayList TxTransferTO = new ArrayList();
            HashMap txMap = new HashMap();
            TransferTrans transferTrans = new TransferTrans();
            transferTrans.setInitiatedBranch(BRANCH_ID);
            transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
            transactionDAO.setInitiatedBranch(_branchCode);
            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
            double totalDebit = 0.0;
            double totalCredit = 0.0;
            double debitMDSLiability = 0.0;
            double debitMDSReceivable = 0.0;
            double creditMDSAssets = 0.0;
            double creditMDSPayable = 0.0;
            //Bonus
            double totalDebitBonus = 0.0;
            double totalCreditBonus = 0.0;
            double debitBonusPayable = 0.0;
            double debitSundryReceipts = 0.0;
            double creditBonusReceivable = 0.0;
            double creditSundryPayment = 0.0;
            totalDebit = CommonUtil.convertObjToDouble(MDSClosureMap.get("TOTAL_DEBIT")).doubleValue();
            totalCredit = CommonUtil.convertObjToDouble(MDSClosureMap.get("TOTAL_CREDIT")).doubleValue();
            debitMDSLiability = CommonUtil.convertObjToDouble(MDSClosureMap.get("DEBIT_MDS_LIABILITY")).doubleValue();
            debitMDSReceivable = CommonUtil.convertObjToDouble(MDSClosureMap.get("DEBIT_MDS_RECEIVABLE")).doubleValue();
            creditMDSAssets = CommonUtil.convertObjToDouble(MDSClosureMap.get("CREDIT_MDS_ASSETS")).doubleValue();
            creditMDSPayable = CommonUtil.convertObjToDouble(MDSClosureMap.get("CREDIT_MDS_PAYABLE")).doubleValue();
            totalDebitBonus = CommonUtil.convertObjToDouble(MDSClosureMap.get("TOTAL_DEBIT_BONUS")).doubleValue();
            totalCreditBonus = CommonUtil.convertObjToDouble(MDSClosureMap.get("TOTAL_CREDIT_BONUS")).doubleValue();
            debitBonusPayable = CommonUtil.convertObjToDouble(MDSClosureMap.get("DEBIT_BONUS_PAYABLE")).doubleValue();
            debitSundryReceipts = CommonUtil.convertObjToDouble(MDSClosureMap.get("DEBIT_SUNDRY_RECEIPTS")).doubleValue();
            creditBonusReceivable = CommonUtil.convertObjToDouble(MDSClosureMap.get("CREDIT_BONUS_RECEIVABLE")).doubleValue();
            creditSundryPayment = CommonUtil.convertObjToDouble(MDSClosureMap.get("CREDIT_SUNDRY_PAYMENT")).doubleValue();
//            System.out.println("##### totalDebit            : " + totalDebit);
//            System.out.println("##### totalCredit           : " + totalCredit);
//            System.out.println("##### debitMDSLiability     : " + debitMDSLiability);
//            System.out.println("##### debitMDSReceivable    : " + debitMDSReceivable);
//            System.out.println("##### creditMDSAssets       : " + creditMDSAssets);
//            System.out.println("##### creditMDSPayable      : " + creditMDSPayable);
//            System.out.println("##### totalDebitBonus       : " + totalDebitBonus);
//            System.out.println("##### totalCreditBonus      : " + totalCreditBonus);
//            System.out.println("##### debitBonusPayable     : " + debitBonusPayable);
//            System.out.println("##### debitSundryReceipts   : " + debitSundryReceipts);
//            System.out.println("##### creditBonusReceivable : " + creditBonusReceivable);
//            System.out.println("##### creditSundryPayment   : " + creditSundryPayment);

            HashMap applicationMap = new HashMap();
            HashMap transMap = new HashMap();
            applicationMap.put("SCHEME_NAME", map.get("SCHEME_NAME"));
            List lst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", applicationMap);
            if (lst != null && lst.size() > 0) {
                applicationMap = (HashMap) lst.get(0);
            }

            // Debit MDS Liability A/c Head
            if (totalDebit > 0) {
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.PARTICULARS, "MDS_CLOSURE : SCHEME - " + map.get("SCHEME_NAME"));
                txMap.put(TransferTrans.DR_BRANCH, map.get("BRANCH_CODE"));
                txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                transferTo = transactionDAO.addTransferDebitLocal(txMap, totalDebit);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransDt(currDt);
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setAuthorizeRemarks("MDS_CLOSURE");
                transferTo.setLinkBatchId(CommonUtil.convertObjToStr(map.get("SCHEME_NAME")));
                transferTo.setTransModType(TransactionFactory.GL); //KD-3346
                TxTransferTO.add(transferTo);
            }

            // Credit MDS Asset A/c Head
            if (totalCredit > 0) {
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("PAYMENT_HEAD"));
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.PARTICULARS, "MDS_CLOSURE : SCHEME - " + map.get("SCHEME_NAME"));
                txMap.put(TransferTrans.CR_BRANCH, map.get("BRANCH_CODE"));
                txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                transferTo = transactionDAO.addTransferCreditLocal(txMap, totalCredit);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransDt(currDt);
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setAuthorizeRemarks("MDS_CLOSURE");
                transferTo.setLinkBatchId(CommonUtil.convertObjToStr(map.get("SCHEME_NAME")));
                transferTo.setTransModType(TransactionFactory.GL); //KD-3346
                TxTransferTO.add(transferTo);
            }

            // Debit MDS Liability A/c Head
            if (debitMDSLiability > 0) {
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.PARTICULARS, "MDS_CLOSURE : SCHEME - " + map.get("SCHEME_NAME"));
                txMap.put(TransferTrans.DR_BRANCH, map.get("BRANCH_CODE"));
                txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                transferTo = transactionDAO.addTransferDebitLocal(txMap, debitMDSLiability);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransDt(currDt);
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setAuthorizeRemarks("MDS_CLOSURE");
                transferTo.setLinkBatchId(CommonUtil.convertObjToStr(map.get("SCHEME_NAME")));
                transferTo.setTransModType(TransactionFactory.GL); //KD-3346
                TxTransferTO.add(transferTo);
            }

            // Debit MDS receivable A/c Head
////            if(debitMDSReceivable>0){
////                transferTo = new TxTransferTO();
////                txMap = new HashMap();
////                transferTo.setInstrumentNo2("APPL_GL_TRANS");
////                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("MDS_RECEIVABLE_HEAD"));
////                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
////                txMap.put(TransferTrans.PARTICULARS, "MDS_CLOSURE : SCHEME - " + map.get("SCHEME_NAME"));
////                txMap.put(TransferTrans.DR_BRANCH, map.get("BRANCH_CODE"));
////                transferTo =  transactionDAO.addTransferDebitLocal(txMap, debitMDSReceivable);
////                transferTo.setTransId("-");
////                transferTo.setBatchId("-");
////                transferTo.setTransDt(currDt);
////                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
////                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
////                transferTo.setAuthorizeRemarks("MDS_CLOSURE");
////                transferTo.setLinkBatchId(CommonUtil.convertObjToStr(map.get("SCHEME_NAME")));
////                TxTransferTO.add(transferTo);
////            }

            // Credit MDS Asset A/c Head
            if (creditMDSAssets > 0) {
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("PAYMENT_HEAD"));
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.PARTICULARS, "MDS_CLOSURE : SCHEME - " + map.get("SCHEME_NAME"));
                txMap.put(TransferTrans.CR_BRANCH, map.get("BRANCH_CODE"));
                txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                transferTo = transactionDAO.addTransferCreditLocal(txMap, creditMDSAssets);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransDt(currDt);
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setAuthorizeRemarks("MDS_CLOSURE");
                transferTo.setLinkBatchId(CommonUtil.convertObjToStr(map.get("SCHEME_NAME")));
                transferTo.setTransModType(TransactionFactory.GL); //KD-3346
                TxTransferTO.add(transferTo);
            }

            // Credit MDS Payable A/c Head
////            if(creditMDSPayable>0){
////                transferTo = new TxTransferTO();
////                txMap = new HashMap();
////                transferTo.setInstrumentNo2("APPL_GL_TRANS");
////                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("MDS_PAYABLE_HEAD"));
////                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
////                txMap.put(TransferTrans.PARTICULARS, "MDS_CLOSURE : SCHEME - " + map.get("SCHEME_NAME"));
////                txMap.put(TransferTrans.CR_BRANCH, map.get("BRANCH_CODE"));
////                transferTo =  transactionDAO.addTransferCreditLocal(txMap, creditMDSPayable);
////                transferTo.setTransId("-");
////                transferTo.setBatchId("-");
////                transferTo.setTransDt(currDt);
////                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
////                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
////                transferTo.setAuthorizeRemarks("MDS_CLOSURE");
////                transferTo.setLinkBatchId(CommonUtil.convertObjToStr(map.get("SCHEME_NAME")));
////                TxTransferTO.add(transferTo);
////            }

            //Bonus Transaction
            // Debit MDS Bonus Payable A/c Head
            if (totalDebitBonus > 0) {
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("BONUS_PAYABLE_HEAD"));
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.PARTICULARS, "MDS_CLOSURE : SCHEME - " + map.get("SCHEME_NAME"));
                txMap.put(TransferTrans.DR_BRANCH, map.get("BRANCH_CODE"));
                txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                transferTo = transactionDAO.addTransferDebitLocal(txMap, totalDebitBonus);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransDt(currDt);
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setAuthorizeRemarks("MDS_CLOSURE");
                transferTo.setLinkBatchId(CommonUtil.convertObjToStr(map.get("SCHEME_NAME")));
                transferTo.setTransModType(TransactionFactory.GL); //KD-3346
                TxTransferTO.add(transferTo);
            }

            // Credit MDS Bonus Receivable A/c Head
            if (totalCreditBonus > 0) {
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("BONUS_RECEIVABLE_HEAD"));
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.PARTICULARS, "MDS_CLOSURE : SCHEME - " + map.get("SCHEME_NAME"));
                txMap.put(TransferTrans.CR_BRANCH, map.get("BRANCH_CODE"));
                txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                transferTo = transactionDAO.addTransferCreditLocal(txMap, totalCreditBonus);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransDt(currDt);
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setAuthorizeRemarks("MDS_CLOSURE");
                transferTo.setLinkBatchId(CommonUtil.convertObjToStr(map.get("SCHEME_NAME")));
                transferTo.setTransModType(TransactionFactory.GL); //KD-3346
                TxTransferTO.add(transferTo);
            }
            // Debit MDS Bonus Payable A/c Head
            if (debitBonusPayable > 0) {
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("BONUS_PAYABLE_HEAD"));
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.PARTICULARS, "MDS_CLOSURE : SCHEME - " + map.get("SCHEME_NAME"));
                txMap.put(TransferTrans.DR_BRANCH, map.get("BRANCH_CODE"));
                txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                transferTo = transactionDAO.addTransferDebitLocal(txMap, debitBonusPayable);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransDt(currDt);
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setAuthorizeRemarks("MDS_CLOSURE");
                transferTo.setLinkBatchId(CommonUtil.convertObjToStr(map.get("SCHEME_NAME")));
                transferTo.setTransModType(TransactionFactory.GL); //KD-3346
                TxTransferTO.add(transferTo);
            }
            // Debit MDS Sundry Receipts A/c Head
            if (debitSundryReceipts > 0) {
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("SUNDRY_RECEIPT_HEAD"));
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.PARTICULARS, "MDS_CLOSURE : SCHEME - " + map.get("SCHEME_NAME"));
                txMap.put(TransferTrans.DR_BRANCH, map.get("BRANCH_CODE"));
                txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                transferTo = transactionDAO.addTransferDebitLocal(txMap, debitSundryReceipts);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransDt(currDt);
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setAuthorizeRemarks("MDS_CLOSURE");
                transferTo.setLinkBatchId(CommonUtil.convertObjToStr(map.get("SCHEME_NAME")));
                transferTo.setTransModType(TransactionFactory.GL); //KD-3346
                TxTransferTO.add(transferTo);
            }
            // Credit MDS Bonus Receivable A/c Head
            if (creditBonusReceivable > 0) {
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("BONUS_RECEIVABLE_HEAD"));
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.PARTICULARS, "MDS_CLOSURE : SCHEME - " + map.get("SCHEME_NAME"));
                txMap.put(TransferTrans.CR_BRANCH, map.get("BRANCH_CODE"));
                txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                transferTo = transactionDAO.addTransferCreditLocal(txMap, creditBonusReceivable);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransDt(currDt);
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setAuthorizeRemarks("MDS_CLOSURE");
                transferTo.setLinkBatchId(CommonUtil.convertObjToStr(map.get("SCHEME_NAME")));
                transferTo.setTransModType(TransactionFactory.GL); //KD-3346
                TxTransferTO.add(transferTo);
            }
            // Credit MDS Sundry Payment A/c Head
            if (creditSundryPayment > 0) {
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                //txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("SUNDRY_PAYMENT_HEAD"));
                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("SUNDRY_RECEIPT_HEAD")); //KD-3437
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.PARTICULARS, "MDS_CLOSURE : SCHEME - " + map.get("SCHEME_NAME"));
                txMap.put(TransferTrans.CR_BRANCH, map.get("BRANCH_CODE"));
                txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                transferTo = transactionDAO.addTransferCreditLocal(txMap, creditSundryPayment);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransDt(currDt);
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setAuthorizeRemarks("MDS_CLOSURE");
                transferTo.setLinkBatchId(CommonUtil.convertObjToStr(map.get("SCHEME_NAME")));
                transferTo.setTransModType(TransactionFactory.GL); //KD-3346
                TxTransferTO.add(transferTo);
            }
            String method = "1";
            HashMap amap = new HashMap();
            amap.put("SCHEME_NAME", map.get("SCHEME_NAME").toString());
            //amap.put("SCHEME_NAME",map.get("SCHEME_NAME"));
            List getMethod = sqlMap.executeQueryForList("getClosureMethod", amap);
            if (getMethod != null && getMethod.size() > 0) {
                amap = new HashMap();
                amap = (HashMap) getMethod.get(0);
                if (amap != null) {
                    if (amap.containsKey("CLOSURE_METHOD2") && amap.get("CLOSURE_METHOD2") != null && amap.get("CLOSURE_METHOD2").equals("Y")) {
                        method = "2";
                    } else {
                        method = "1";
                    }
                } else {
                    method = "1";
                }
            } else {
                method = "1";
            }
            if (method.equals("2")) {
                double net = 0.0;
                double paid = 0.0;
                amap = new HashMap();
                amap.put("SCHEME_NAME", map.get("SCHEME_NAME").toString());
                List getmdsMonyPyent = sqlMap.executeQueryForList("getMdsNetfrom", amap);
                if (getmdsMonyPyent != null && getmdsMonyPyent.size() > 0) {
                    HashMap netMap = new HashMap();
                    netMap = (HashMap) getmdsMonyPyent.get(0);
                    if (netMap != null && netMap.size() > 0 && netMap.containsKey("TOTAL_MDSPAYMENT") && netMap.get("TOTAL_MDSPAYMENT") != null) {
                        net = Double.parseDouble(netMap.get("TOTAL_MDSPAYMENT").toString());

                    } else {
                        net = 0.0;
                    }
                } else {
                    net = 0.0;
                }
                amap = new HashMap();
                amap.put("SCHEME_NAME", map.get("SCHEME_NAME").toString());
                List getmdsTransDts = sqlMap.executeQueryForList("getTotalReceivedAmount", amap);
                if (getmdsTransDts != null && getmdsTransDts.size() > 0) {
                    HashMap mdstransMap = new HashMap();
                    mdstransMap = (HashMap) getmdsTransDts.get(0);
                    if (mdstransMap != null && mdstransMap.containsKey("TOTAL_RECEIVED") && mdstransMap.get("TOTAL_RECEIVED") != null) {
                        paid = Double.parseDouble(mdstransMap.get("TOTAL_RECEIVED").toString());
                    } else {
                        paid = 0.0;
                    }
                } else {
                    paid = 0.0;
                }
                double diff = net - paid;
                System.out.println("diff" + diff);
                if (diff > 0) {
                    amap = new HashMap();
                    amap.put("SCHEME_NAME", map.get("SCHEME_NAME").toString());
                    List pendingChits = sqlMap.executeQueryForList("getPendingChits", amap);
                    List installmntAmt = sqlMap.executeQueryForList("getInstallmntAmount", amap);
                    amap = new HashMap();
                    amap = (HashMap) installmntAmt.get(0);
                    double installment = 0.0;
                    if (amap != null && amap.containsKey("INSTALLMENT_AMOUNT") && amap.get("INSTALLMENT_AMOUNT") != null) {
                        installment = Double.parseDouble(amap.get("INSTALLMENT_AMOUNT").toString());
                    } else {
                        installment = 0.0;
                    }
                    System.out.println("installment..." + installment);
                    if (pendingChits != null && pendingChits.size() > 0) {
                        HashMap chitMap = new HashMap();
                        String chit_no = "";
                        String subno = "";
                        int due = 0;
                        for (int i = 0; i < pendingChits.size(); i++) {
                            chitMap = new HashMap();
                            chitMap = (HashMap) pendingChits.get(i);
                            chit_no = chitMap.get("CHITTAL_NO").toString();
                            subno = chitMap.get("SUB_NO").toString();
                            due = Integer.parseInt(chitMap.get("DUE").toString());
                            double pendAmount = installment * due;
                            System.out.println("Pendingamount by chit" + pendAmount + "mmm" + chit_no);
                            if (pendAmount > 0) {
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("MDS_RECEIVABLE_HEAD"));
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, "MDS_CLOSURE : SCHEME - " + map.get("SCHEME_NAME"));
                                txMap.put(TransferTrans.DR_BRANCH, map.get("BRANCH_CODE"));
                                txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, pendAmount);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setTransDt(currDt);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setAuthorizeRemarks("MDS_CLOSURE");
                                transferTo.setLinkBatchId(chit_no + "_" + subno);
                                transferTo.setTransModType(TransactionFactory.GL); //KD-3346
                                TxTransferTO.add(transferTo);
                                System.out.println("transferToddd" + transferTo);
                            }
                        }
                    }
                }
            } else {
                if (debitMDSReceivable > 0) {
                    transferTo = new TxTransferTO();
                    txMap = new HashMap();
                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                    txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("MDS_RECEIVABLE_HEAD"));
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.PARTICULARS, "MDS_CLOSURE : SCHEME - " + map.get("SCHEME_NAME"));
                    txMap.put(TransferTrans.DR_BRANCH, map.get("BRANCH_CODE"));
                    txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, debitMDSReceivable);
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setTransDt(currDt);
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setAuthorizeRemarks("MDS_CLOSURE");
                    transferTo.setLinkBatchId(CommonUtil.convertObjToStr(map.get("SCHEME_NAME")));
                    transferTo.setTransModType(TransactionFactory.GL); //KD-3346
                    TxTransferTO.add(transferTo);
                }


                if (creditMDSPayable > 0) {
                    transferTo = new TxTransferTO();
                    txMap = new HashMap();
                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                    txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("MDS_PAYABLE_HEAD"));
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.PARTICULARS, "MDS_CLOSURE : SCHEME - " + map.get("SCHEME_NAME"));
                    txMap.put(TransferTrans.CR_BRANCH, map.get("BRANCH_CODE"));
                    txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, creditMDSPayable);
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setTransDt(currDt);
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setAuthorizeRemarks("MDS_CLOSURE");
                    transferTo.setLinkBatchId(CommonUtil.convertObjToStr(map.get("SCHEME_NAME")));
                    transferTo.setTransModType(TransactionFactory.GL);//KD-3346
                    TxTransferTO.add(transferTo);
                }

            }
            transferDAO = new TransferDAO();
            HashMap transactionMap = new HashMap();
            transactionMap.put("TxTransferTO", TxTransferTO);
            transactionMap.put("MODE", map.get("COMMAND"));
            transactionMap.put("COMMAND", map.get("COMMAND"));
            transactionMap.put(CommonConstants.BRANCH_ID, BRANCH_ID);
            // transactionMap.put("LINK_BATCH_ID", map.get("SCHEME_NAME"));
            transactionMap.put("INITIATED_BRANCH", _branchCode);
            System.out.println("################ tansactionMap :" + transactionMap);
            transMap = transferDAO.execute(transactionMap, false);
            getTransDetails(CommonUtil.convertObjToStr(map.get("SCHEME_NAME")));

        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }

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
        getTransMap.clear();
        getTransMap = null;
        transList = null;
    }

    private void deleteMDSClosureTransaction(HashMap map) throws Exception {
        try {
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void authorizeMDSClosure(HashMap map) throws Exception {
        try {
            System.out.println("####### MDS CLOSURE AUTHORIZE : " + map);
            HashMap authorizeMap = new HashMap();
            authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
            String authorizeStatus = CommonUtil.convertObjToStr(authorizeMap.get("AUTHORIZESTATUS"));
            String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
    //        System.out.println("###### authorizeStatus :" + authorizeStatus);
            transferDAO = new TransferDAO();
            String linkBatchId = "";
            HashMap transferTransParam = new HashMap();
            transferTransParam.put(CommonConstants.BRANCH_ID, BRANCH_ID);
            transferTransParam.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            transferTransParam.put("DEBIT_LOAN_TYPE", "DP");
            linkBatchId = CommonUtil.convertObjToStr(map.get("SCHEME_NAME"));
            transferTransParam.put("LINK_BATCH_ID", linkBatchId);
            ArrayList transferTransList = (ArrayList) sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", transferTransParam);
            if (transferTransList != null) {
                String batchId = ((TxTransferTO) transferTransList.get(0)).getBatchId();
                HashMap transAuthMap = new HashMap();
                transAuthMap.put("BATCH_ID", batchId);
                transAuthMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                transAuthMap.put(CommonConstants.AUTHORIZEDATA, transferTransList);
                transferTransParam.put(CommonConstants.AUTHORIZEMAP, transAuthMap);
                transferDAO.execute(transferTransParam, false);
      //          System.out.println("#$$$$$$$$$$$$$ AuthorizeMap : " + authorizeMap);
                sqlMap.executeUpdate("authorizeMDSClosureDetails", authorizeMap);

                if (authorizeStatus.equals("AUTHORIZED") && authorizeMap.containsKey("SCHEME_CLOSED")) {
                    authorizeMap.put("STATUS", "CLOSED");
                    sqlMap.executeUpdate("updateMDSProductCloseStatus", authorizeMap);
                    List lst = sqlMap.executeQueryForList("getMDSLienDetailsForClosing", authorizeMap);
                    if (lst != null && lst.size() > 0) {
                        for (int i = 0; i < lst.size(); i++) {
                            HashMap hmap = (HashMap) lst.get(i);
                            List lst1 = sqlMap.executeQueryForList("getLienDEtailsForDelete", hmap);
                            if (lst1 != null && lst1.size() > 0) {
                                hmap = (HashMap) lst1.get(0);
                                double lienamt = CommonUtil.convertObjToDouble(hmap.get("LIEN_AMOUNT")).doubleValue();
                                hmap.put("STATUS", "UNLIENED");
                                hmap.put("UNLIEN_DT", currDt);
                                hmap.put("DEPOSIT_ACT_NUM", hmap.get("DEPOSIT_NO"));
                                hmap.put("CHITTAL_NO", hmap.get("LIEN_AC_NO"));
                                hmap.put("LIENAMOUNT", CommonUtil.convertObjToDouble(hmap.get("LIEN_AMOUNT")));
                                hmap.put("SUBNO", CommonUtil.convertObjToInt("1"));
                                hmap.put("SHADOWLIEN", new Double(0.0));
                                sqlMap.executeUpdate("updateSubAcInfoAvlBal", hmap);
                                sqlMap.executeUpdate("updateUnlienForMDS", hmap);
                            }

                        }
                    }

                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        mdsApplicationTO = null;
    }
    
    // Added by nithya on 23-12-2020 for KD-2521
    private void insertDataWithDiscount(HashMap map, LogDAO objLogDAO, LogTO objLogTO, double auction_maxamt, HashMap productMap) throws Exception {
        try {
            System.out.println("###### :" + map);
            ArrayList transferList = new ArrayList();
            TransferTrans transferTrans = new TransferTrans();
            TxTransferTO transferTo = new TxTransferTO();
            ArrayList TxTransferTO = new ArrayList();
            TransactionTO transactionTO = new TransactionTO();
            HashMap txMap = new HashMap();
            String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
            double totalAmt = CommonUtil.convertObjToDouble(map.get("TOTAL_AMOUNT")).doubleValue();
            transferTrans.setInitiatedBranch(BRANCH_ID);
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            transactionDAO.setInitiatedBranch(_branchCode);
            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
            if (map.containsKey("TransactionTO")) {
                HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                }
                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                List totalList = sqlMap.executeQueryForList("getSelectGLTransMDSApplicationTO", map);
                if (totalList != null && totalList.size() > 0) {
                    System.out.println("###### totalListsize() : " + totalList.size());
                    double totalTransAmt = 0.0;
                    double totalSuspendsAmt = 0.0;                    
                    totalTransAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                    double totalDiscountAmt = CommonUtil.convertObjToDouble(productMap.get("FIRST_DISCOUNT_AMT")) * totalList.size();
                    totalSuspendsAmt = totalTransAmt - totalDiscountAmt;
                    System.out.println("######     totalTransAmt : " + totalTransAmt);
                    System.out.println("######     totalDiscountAmt : " + totalDiscountAmt);
                    System.out.println("######  totalSuspendsAmt : " + totalSuspendsAmt);
                    String linkBatchId = CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo());
                    HashMap applicationMap = new HashMap();
                    HashMap transMap = new HashMap();
                    applicationMap.put("SCHEME_NAME", map.get("SCHEME_NAME"));
                    List lst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", applicationMap);
                    if (lst != null && lst.size() > 0) {
                        applicationMap = (HashMap) lst.get(0);
                    }
                    // Debit Suspends Account
                    if (totalSuspendsAmt > 0) {
                        txMap = new HashMap();
                        if (CommonUtil.convertObjToStr(applicationMap.get("SUSPENSE_GL_ACCNO")).equals("GL")) {
                            txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("SUSPENSE_HEAD"));
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        } else {
                            HashMap suspenseMap = new HashMap();                // Suspense Acc Head
                            suspenseMap.put("PROD_ID", applicationMap.get("SUSPENSE_PROD_ID"));
                            List suspenseLst = sqlMap.executeQueryForList("getAccountHeadProdSA", suspenseMap);
                            if (suspenseLst != null && suspenseLst.size() > 0) {
                                suspenseMap = (HashMap) suspenseLst.get(0);
                                txMap.put(TransferTrans.DR_AC_HD, suspenseMap.get("AC_HEAD"));
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.SUSPENSE);
                                txMap.put(TransferTrans.DR_PROD_ID, applicationMap.get("SUSPENSE_PROD_ID"));
                                txMap.put(TransferTrans.DR_ACT_NUM, applicationMap.get("SUSPENSE_ACC_NO"));
                            }
                        }
                        txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                        txMap.put(TransferTrans.PARTICULARS, map.get("SCHEME_NAME"));
                        txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                        System.out.println("txMap : " + txMap + "amt :" + totalSuspendsAmt);
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, totalSuspendsAmt);
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setInitiatedBranch(BRANCH_ID);
                        if (CommonUtil.convertObjToStr(applicationMap.get("SUSPENSE_GL_ACCNO")).equals("GL")) {
                            transferTo.setTransModType(TransactionFactory.GL);
                        } else {
                            transferTo.setTransModType(TransactionFactory.SUSPENSE);
                        }
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                            transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                        }
                        System.out.println("transferTo List 1 : " + transferTo);
                        TxTransferTO.add(transferTo);
                    }
                    // Debit discount Account
                    if (totalDiscountAmt > 0) {
                        txMap = new HashMap();
                        txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("DISCOUNT_HEAD"));
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                        txMap.put(TransferTrans.PARTICULARS, map.get("SCHEME_NAME"));
                        txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                        System.out.println("txMap : " + txMap + "amt :" + totalDiscountAmt);
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, totalDiscountAmt);
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setInitiatedBranch(BRANCH_ID);
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        if (CommonUtil.convertObjToStr(applicationMap.get("SUSPENSE_GL_ACCNO")).equals("GL")) {
                            transferTo.setTransModType(TransactionFactory.GL);
                        } else {
                            transferTo.setTransModType(TransactionFactory.SUSPENSE);
                        }
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                            transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                        }
                        transferTo.setInstrumentNo2("DISCOUNT");// Added by nithya on 31-10-2019 for KD-680
                        System.out.println("transferTo List 1 : " + transferTo);
                        TxTransferTO.add(transferTo);
                    }
                    //Credit Receipt Account
                    for (int i = 0; i < totalList.size(); i++) {
                        System.out.println("###### inside for loop : " + totalList.size());
                        MDSApplicationTO mdsApplicationTO = new MDSApplicationTO();
                        mdsApplicationTO = (MDSApplicationTO) totalList.get(i);
                        if (transactionTO.getTransType().equals("TRANSFER")) {
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            if (CommonUtil.convertObjToDouble(mdsApplicationTO.getInstAmt()).doubleValue() > 0) {
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, mdsApplicationTO.getSchemeName() + "-" + CommonUtil.convertObjToStr(mdsApplicationTO.getChitNo()));
                                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(mdsApplicationTO.getInstAmt()).doubleValue());
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(mdsApplicationTO.getInstAmt()).doubleValue());
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setTransModType("MDS");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setGlTransActNum(mdsApplicationTO.getChittalNo());// Added by nithya on 07-03-2018 for 0009203: MDS Commencement - Transfer Print Credit Side not coming in Report.
                                if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                                    transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                                }
                                transferTo.setInstrumentNo2("INSTALMENT_AMT");// Added by nithya on 31-10-2019 for KD-680
                                System.out.println("transferTo List 2 : " + transferTo);
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
                            }
                            HashMap commencementMap = new HashMap();
                            commencementMap.put("COMMENCEMENT_STATUS", "Y");
                            commencementMap.put("COMMENCEMENT_DATE", map.get("COMMENCEMENT_DATE"));
                            commencementMap.put("TRANS_ID", mdsApplicationTO.getTransId());
                            sqlMap.executeUpdate("updateCommencementStatus", commencementMap);
                        }
                    }
                    HashMap applnMap = new HashMap();
                    transferDAO = new TransferDAO();
                    map.put("MODE", map.get("COMMAND"));
                    map.put("COMMAND", map.get("MODE"));
                    map.put("TxTransferTO", TxTransferTO);
                    transMap = transferDAO.execute(map, false);
                    System.out.println("transactionDAO map : " + map);
                    transactionDAO.setBatchId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                    transactionDAO.setBatchDate(currDt);
                    transactionDAO.execute(map);
                    HashMap linkBatchMap = new HashMap();
                    HashMap remitMap = new HashMap();
                    linkBatchMap.put("BATCH_ID", transMap.get("TRANS_ID"));
                    linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
                    linkBatchMap.put("TRANS_DT", currDt);
                    linkBatchMap.put("INITIATED_BRANCH", _branchCode);
                    if(transactionTO.getProductType().equals("GL")){
                    sqlMap.executeUpdate("updateLinkBatchIdTransfer", linkBatchMap);
                    }
                    transMap.put("SCHEME_NAME", map.get("SCHEME_NAME"));
                    System.out.println("####### transMap :" + transMap);
                    sqlMap.executeUpdate("updateCommencementTransId", transMap);
                    linkBatchMap = null;
                }
            }
            objLogDAO.addToLog(objLogTO);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
    
}
