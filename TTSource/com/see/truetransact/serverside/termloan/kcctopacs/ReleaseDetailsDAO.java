/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ReleaseDetailsDAO.java
 * 
 * Created on Fri Apr 19 11:33:46 IST 2013
 */
package com.see.truetransact.serverside.termloan.kcctopacs;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.GregorianCalendar;
import java.util.Date;
import org.apache.log4j.Logger;
import java.util.LinkedHashMap;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.transferobject.product.operativeacct.*;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.termloan.kcctopacs.ReleaseDetailsTO;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.transferobject.termloan.kcctopacs.NclClassificationTO;
import com.see.truetransact.transferobject.termloan.kcctopacs.NclAmtSlabWiseDetTO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.commonutil.interestcalc.Rounding;

/**
 * This is used for ReleaseDetailsDAO Data Access.
 *
 * @author Suresh
 *
 */
public class ReleaseDetailsDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private ReleaseDetailsTO objReleaseDetailsTO;
    private TransactionDAO transactionDAO = null;
    TransferDAO transferDAO = new TransferDAO();
    private LogDAO logDAO;
    private LogTO logTO;
    private Date currDt = null;
    HashMap returnMap = new HashMap();
    private LinkedHashMap membertableDetails = null;
    private LinkedHashMap loantableDetails = null;
    private NclClassificationTO objClasficationDetailsTO;
    private NclAmtSlabWiseDetTO objLoanSlabDetailsTO;
    private final static Logger log = Logger.getLogger(ReleaseDetailsDAO.class);
    private int yearTobeAdded = 1900;

    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public ReleaseDetailsDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("#### GetData map : " + map);
        returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSelectReleaseDetailsTO", map);
        if (list != null && list.size() > 0) {
            returnMap.put("NCLReleaseData", list);
        }
        map.put("NCL_SANCTION_NO", map.get("RELEASE_NO"));
        List Memlist = (List) sqlMap.executeQueryForList("getClassificationMemberTO", map);
        if (Memlist != null && Memlist.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            for (int i = Memlist.size(), j = 0; i > 0; i--, j++) {
                String st = ((NclClassificationTO) Memlist.get(j)).getNclSanctionNo();
                ParMap.put(j + 1, Memlist.get(j));
            }
            returnMap.put("MEMBER_DATA", ParMap);
        }

        List loanlist = (List) sqlMap.executeQueryForList("getSelectNclAmtSlabWiseDetTO", map);
        if (loanlist != null && loanlist.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            for (int i = loanlist.size(), j = 0; i > 0; i--, j++) {
                String st = ((NclAmtSlabWiseDetTO) loanlist.get(j)).getNclSanctionNo();
                //Comment And Added Following Line By Kannan
//                ParMap.put(CommonUtil.convertObjToStr(((NclAmtSlabWiseDetTO) loanlist.get(j)).getFromAmt()), loanlist.get(j));
                ParMap.put(((NclAmtSlabWiseDetTO) loanlist.get(j)).getFromAmt(), loanlist.get(j));
            }
            returnMap.put("LOAN_DATA", ParMap);
        }

        if (map.containsKey("DISPLAY_TRANS_DETAILS")) {
            getTransDetails(CommonUtil.convertObjToStr(map.get("RELEASE_NO")));
        }
        System.out.println("########## returnMap : " + returnMap);
        return returnMap;
    }

    private String getReleaseNo() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "KCC_RELEASE_NO");
        String releaseNo = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return releaseNo;
    }

    public HashMap execute(HashMap map) throws Exception {

        System.out.println("######## Release Map DAO : " + map);
        returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        logDAO = new LogDAO();
        logTO = new LogTO();
        objReleaseDetailsTO = (ReleaseDetailsTO) map.get("NCLReleaseData");
        membertableDetails = (LinkedHashMap) map.get("MemberDetails");
        loantableDetails = (LinkedHashMap) map.get("LoanSlabDetails");
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData(map);
        } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap);
            }
        }
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            returnMap.put("RELEASE_NO", objReleaseDetailsTO.getReleaseNo());
        }
        map = null;
        destroyObjects();
        System.out.println("######## returnMap DAO : " + returnMap);
        return returnMap;
    }

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            String relNo = "";
            relNo = getReleaseNo();
            String year1 = DateUtil.getStringDate(currDt);
            year1 = year1.replace("/", "");
            year1 = year1.substring(6, 8);
            int end = CommonUtil.convertObjToInt(year1);
            end = end + 1;
            String releaseNo = "R";
            releaseNo = releaseNo + year1;
            releaseNo = releaseNo + end;
            releaseNo = releaseNo + relNo;
            System.out.println("########## Release Number : " + releaseNo);
            objReleaseDetailsTO.setReleaseNo(releaseNo);
            commonTransactionCashandTransfer(map);
            insertMemberData(releaseNo);
            insertLoanData(releaseNo);
            sqlMap.executeUpdate("insertReleaseDetailsTO", objReleaseDetailsTO);
            logTO.setData(objReleaseDetailsTO.toString());
            logTO.setPrimaryKey(objReleaseDetailsTO.getKeyData());
            logTO.setStatus(objReleaseDetailsTO.getStatus());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void insertMemberData(String releaseNo) throws Exception {
        try {
            if (membertableDetails != null) {
                ArrayList addList = new ArrayList(membertableDetails.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    NclClassificationTO objClasficationDetailsTO = (NclClassificationTO) membertableDetails.get(addList.get(i));
                    objClasficationDetailsTO.setNclSanctionNo(releaseNo);
                    sqlMap.executeUpdate("insertClassificationMemberTO", objClasficationDetailsTO);
                    logTO.setData(objClasficationDetailsTO.toString());
                    logTO.setPrimaryKey(objClasficationDetailsTO.getKeyData());
                    logDAO.addToLog(logTO);
                    objClasficationDetailsTO = null;
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }

    private void insertLoanData(String releaseNo) throws Exception {
        try {
            if (loantableDetails != null) {
                ArrayList addList = new ArrayList(loantableDetails.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    NclAmtSlabWiseDetTO objLoanSlabDetailsTO = (NclAmtSlabWiseDetTO) loantableDetails.get(addList.get(i));
                    objLoanSlabDetailsTO.setNclSanctionNo(releaseNo);
                    sqlMap.executeUpdate("insertNclAmtSlabWiseDetTO", objLoanSlabDetailsTO);
                    logTO.setData(objLoanSlabDetailsTO.toString());
                    logTO.setPrimaryKey(objLoanSlabDetailsTO.getKeyData());
                    logDAO.addToLog(logTO);
                    objLoanSlabDetailsTO = null;
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }

    public void commonTransactionCashandTransfer(HashMap map) throws Exception {
        String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        ArrayList transferList = new ArrayList();
        TransferTrans transferTrans = new TransferTrans();
        TxTransferTO transferTo = new TxTransferTO();
        ArrayList TxTransferTO = new ArrayList();
        TransactionTO transactionTO = new TransactionTO();
        HashMap txMap = new HashMap();
        HashMap transactionListMap = new HashMap();
        transferTrans.setInitiatedBranch(BRANCH_ID);
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        transactionDAO.setInitiatedBranch(_branchCode);
        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
        if (CommonUtil.convertObjToDouble(objReleaseDetailsTO.getAmountReleased()) > 0) {
            HashMap sanctionMap = new HashMap();
            sanctionMap.put("NCL_SANCTION_NO", CommonUtil.convertObjToStr(objReleaseDetailsTO.getNclSanctionNo()));
            List sanctionList = sqlMap.executeQueryForList("getNCLSanctionDetails", sanctionMap);
            if (sanctionList != null && sanctionList.size() > 0) {
                sanctionMap = (HashMap) sanctionList.get(0);
                //Credit A/c Head
                HashMap creditHeadMap = new HashMap();
                creditHeadMap.put("PROD_ID", sanctionMap.get("CA_PROD_ID"));
                List creditLst = sqlMap.executeQueryForList("getAccountHeadProd" + sanctionMap.get("CA_PROD_TYPE"), creditHeadMap);
                if (creditLst != null && creditLst.size() > 0) {
                    creditHeadMap = (HashMap) creditLst.get(0);
                }
                //Debit A/c Head
                HashMap debitHeadMap = new HashMap();
                debitHeadMap.put("PROD_ID", sanctionMap.get("KCC_PROD_ID"));
                List debitLst = sqlMap.executeQueryForList("getAccountHeadProdAD", debitHeadMap);
                if (debitLst != null && debitLst.size() > 0) {
                    debitHeadMap = (HashMap) debitLst.get(0);
                }
                //DEBIT
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                txMap.put(TransferTrans.DR_AC_HD, debitHeadMap.get("AC_HEAD"));
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.ADVANCES);
                txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(sanctionMap.get("KCC_ACT_NUM")));
                txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put(TransferTrans.PARTICULARS, " Release " + "- " + CommonUtil.convertObjToStr(objReleaseDetailsTO.getReleaseNo()));
                transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(objReleaseDetailsTO.getAmountReleased()));
                transferTo.setInstrumentNo1("");
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransType(CommonConstants.DEBIT);
                transferTo.setAcHdId(CommonUtil.convertObjToStr(debitHeadMap.get("AC_HEAD")));
                transferTo.setTransDt(currDt);
                transferTo.setBranchId(BRANCH_ID);
                transferTo.setInitiatedBranch(BRANCH_ID);
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(objReleaseDetailsTO.getReleaseNo()));
                transactionTO.setChequeNo("SERVICE_TAX");
                TxTransferTO.add(transferTo);
                //CREDIT
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                txMap.put(TransferTrans.CR_AC_HD, creditHeadMap.get("AC_HEAD"));
                txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(sanctionMap.get("CA_PROD_TYPE")));
                txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(sanctionMap.get("CA_ACT_NUM")));
                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put(TransferTrans.PARTICULARS, " Release " + "- " + CommonUtil.convertObjToStr(objReleaseDetailsTO.getReleaseNo()));
                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(objReleaseDetailsTO.getAmountReleased()));
                transferTo.setInstrumentNo1("");
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransType(CommonConstants.CREDIT);
                transferTo.setAcHdId(CommonUtil.convertObjToStr(creditHeadMap.get("AC_HEAD")));
                transferTo.setTransDt(currDt);
                transferTo.setBranchId(BRANCH_ID);
                transferTo.setInitiatedBranch(BRANCH_ID);
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(objReleaseDetailsTO.getReleaseNo()));
                transactionTO.setChequeNo("SERVICE_TAX");
                TxTransferTO.add(transferTo);
                HashMap applnMap = new HashMap();
                map.put("MODE", map.get("COMMAND"));
                map.put("COMMAND", map.get("MODE"));
                map.put("TxTransferTO", TxTransferTO);
                map.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(objReleaseDetailsTO.getReleaseNo()));
                System.out.println("####### Before Calling transferDAO List Data : " + map);
                HashMap transMap = transferDAO.execute(map, false);
                if (transMap != null && transMap.size() > 0) {
                    getTransDetails(CommonUtil.convertObjToStr(objReleaseDetailsTO.getReleaseNo()));
                }
                transMap = null;
            }
        }
    }

    private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        List transList = (List) sqlMap.executeQueryForList("getTransferDetailsWithAccHeadDesc", getTransMap);
        if (transList != null && transList.size() > 0) {
            returnMap.put("TRANSFER_TRANS_LIST", transList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
    }

    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            HashMap wheremap = new HashMap();
            wheremap.put("RELEASE_NO", objReleaseDetailsTO.getReleaseNo());
            List amtList = (List) sqlMap.executeQueryForList("NCLAmountReleaseDetails", wheremap);
            if (amtList != null && amtList.size() > 0) {
                wheremap = (HashMap) amtList.get(0);
                if (CommonUtil.convertObjToDouble(wheremap.get("AMOUNT_RELEASED")).doubleValue() != CommonUtil.convertObjToDouble(objReleaseDetailsTO.getAmountReleased()).doubleValue()) {
                    deleteTransactionData(map);
                    commonTransactionCashandTransfer(map);
                }
            }
            updateMemberData(map);
            updateLoanData(map);
            sqlMap.executeUpdate("updateReleaseDetailsTO", objReleaseDetailsTO);
            logTO.setData(objReleaseDetailsTO.toString());
            logTO.setPrimaryKey(objReleaseDetailsTO.getKeyData());
            logTO.setStatus(objReleaseDetailsTO.getStatus());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void updateMemberData(HashMap map) throws Exception {
        try {
            sqlMap.executeUpdate("deleteReleaseMemberData", objReleaseDetailsTO);
            if (membertableDetails != null) {
                ArrayList addList = new ArrayList(membertableDetails.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    NclClassificationTO objClasficationDetailsTO = (NclClassificationTO) membertableDetails.get(addList.get(i));
                    sqlMap.executeUpdate("insertClassificationMemberTO", objClasficationDetailsTO);
                    logTO.setData(objClasficationDetailsTO.toString());
                    logTO.setPrimaryKey(objClasficationDetailsTO.getKeyData());
                    logDAO.addToLog(logTO);
                    objClasficationDetailsTO = null;
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void updateLoanData(HashMap map) throws Exception {
        try {
            sqlMap.executeUpdate("deleteReleaseLoanData", objReleaseDetailsTO);
            if (loantableDetails != null) {
                ArrayList addList = new ArrayList(loantableDetails.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    NclAmtSlabWiseDetTO objLoanSlabDetailsTO = (NclAmtSlabWiseDetTO) loantableDetails.get(addList.get(i));
                    sqlMap.executeUpdate("insertNclAmtSlabWiseDetTO", objLoanSlabDetailsTO);
                    logTO.setData(objLoanSlabDetailsTO.toString());
                    logTO.setPrimaryKey(objLoanSlabDetailsTO.getKeyData());
                    logDAO.addToLog(logTO);
                    objLoanSlabDetailsTO = null;
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void deleteData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            deleteTransactionData(map);
            sqlMap.executeUpdate("deleteReleaseDetailsTO", objReleaseDetailsTO);
            logTO.setData(objReleaseDetailsTO.toString());
            logTO.setPrimaryKey(objReleaseDetailsTO.getKeyData());
            logTO.setStatus(objReleaseDetailsTO.getStatus());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void deleteTransactionData(HashMap map) throws Exception {
        TransferTrans transferTrans = new TransferTrans();
        String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        transferTrans.setInitiatedBranch(BRANCH_ID);
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        transactionDAO.setInitiatedBranch(_branchCode);
        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_DELETE);
        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
        HashMap tempMap = new HashMap();
        tempMap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(objReleaseDetailsTO.getReleaseNo()));
        tempMap.put("TRANS_DT", currDt);
        tempMap.put("INITIATED_BRANCH", BRANCH_ID);
        ArrayList batchList = new ArrayList();
        TxTransferTO txTransferTO = null;
        HashMap oldAmountMap = new HashMap();
        List lst = sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", tempMap);
        if (lst != null) {
            for (int i = 0; i < lst.size(); i++) {
                txTransferTO = (TxTransferTO) lst.get(i);
                txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                txTransferTO.setStatusDt(currDt);
                txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                oldAmountMap.put(txTransferTO.getTransId(), CommonUtil.convertObjToDouble(txTransferTO.getAmount()));
                batchList.add(txTransferTO);
            }
        }
        transferTrans = new TransferTrans();
        transferTrans.setOldAmount(oldAmountMap);
        transferTrans.setInitiatedBranch(BRANCH_ID);
        transferTrans.doDebitCredit(batchList, BRANCH_ID, false, CommonConstants.TOSTATUS_DELETE);
        lst = null;
        transferTrans = null;
        txTransferTO = null;
        batchList = null;
        oldAmountMap = null;
        tempMap = null;
    }

    private void authorize(HashMap map) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        System.out.println("@@@@@@@AuthMap : " + AuthMap);
        try {
            sqlMap.startTransaction();
            logTO.setData(map.toString());
            authorizeTransaction(AuthMap, map);
            AuthMap.put("LAST_INT_CALC_DT", currDt);
            AuthMap.put("LAST_TRANS_DT", currDt);
            sqlMap.executeUpdate("authorizeNCLReleaseDetails", AuthMap);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public void authorizeTransaction(HashMap AuthMap, HashMap map) throws Exception {
        try {
            if (AuthMap != null && AuthMap.get("RELEASE_NO") != null && !AuthMap.get("RELEASE_NO").equals("")) {
                String authorizeStatus = CommonUtil.convertObjToStr(AuthMap.get("STATUS"));
                String linkBatchId = CommonUtil.convertObjToStr(AuthMap.get("RELEASE_NO"));
                HashMap transAuthMap = new HashMap();
                transAuthMap.put(CommonConstants.BRANCH_ID, _branchCode);
                transAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, transAuthMap);
                transAuthMap = null;
                if (authorizeStatus.equals("AUTHORIZED")) {
                    System.out.println("########## INSTALLMENT ENTRY START : ");
                    HashMap whereMap = new HashMap();
                    HashMap installMap = new HashMap();
                    List releaseList = sqlMap.executeQueryForList("NCLAmountReleaseDetails", AuthMap);
                    if (releaseList != null && releaseList.size() > 0) {
                        whereMap = (HashMap) releaseList.get(0);
                        int no_of_Installment = 0;
                        String repayment_Period_Type = "";
                        no_of_Installment = CommonUtil.convertObjToInt(whereMap.get("NO_OF_INST"));
                        repayment_Period_Type = CommonUtil.convertObjToStr(whereMap.get("PRINCIPAL_FREQ_TYPE"));
                        System.out.println("########### repayment_Period_Type :" + repayment_Period_Type);
                        double totInstAmt = 0;
                        double releasedAmt = 0;
                        double installmentAmt = 0;
                        Date instDate = (Date) currDt.clone();
                        releasedAmt = CommonUtil.convertObjToDouble(whereMap.get("AMOUNT_RELEASED"));
                        installmentAmt = releasedAmt / no_of_Installment;
                        Rounding rod = new Rounding();
                        installmentAmt = (double) rod.getNearest((long) (installmentAmt * 100), 100) / 100;
                        System.out.println("########## Each Months/Years InstAmt: " + installmentAmt);
                        for (int i = 1; i <= no_of_Installment; i++) {
                            installMap = new HashMap();
                            installMap.put("RELEASE_NO", linkBatchId);
                            installMap.put("INSTALLMENT_SLNO", String.valueOf(i));
                            installMap.put("PRINCIPAL_AMT", String.valueOf(installmentAmt));
                            if (i == no_of_Installment) {
                                installMap.put("PRINCIPAL_AMT", String.valueOf(releasedAmt - totInstAmt));
                            }
                            //Installment Date Calculating
                            if (instDate != null) {
                                GregorianCalendar cal = new GregorianCalendar((instDate.getYear() + yearTobeAdded), instDate.getMonth(), instDate.getDate());
                                if ((!repayment_Period_Type.equals("")) && repayment_Period_Type.equals("Yearly")) {
                                    cal.add(GregorianCalendar.YEAR, 1);
                                }
                                if ((!repayment_Period_Type.equals("")) && repayment_Period_Type.equals("Monthly")) {
                                    cal.add(GregorianCalendar.MONTH, 1);
                                }
                                if ((!repayment_Period_Type.equals("")) && repayment_Period_Type.equals("Half Yearly")) {
                                    cal.add(GregorianCalendar.MONTH, 6);
                                }
                                if ((!repayment_Period_Type.equals("")) && repayment_Period_Type.equals("Quaterly")) {
                                    cal.add(GregorianCalendar.MONTH, 3);
                                }
                                if ((!repayment_Period_Type.equals("")) && repayment_Period_Type.equals("Days")) {
                                    cal.add(GregorianCalendar.DAY_OF_MONTH, 1);
                                }
                                instDate = (Date) DateUtil.getDateMMDDYYYY(DateUtil.getStringDate(cal.getTime()));
                            }
                            if ((!repayment_Period_Type.equals("")) && (repayment_Period_Type.equals("Lump Sum") || repayment_Period_Type.equals("On Maturity"))) {
                                installMap.put("INSTALLMENT_DT", setProperDtFormat((Date) (whereMap.get("DUE_DATE"))));
                            } else {
                                instDate = setProperDtFormat(instDate);
                                installMap.put("INSTALLMENT_DT", instDate);
                            }
                            totInstAmt += installmentAmt;
                            System.out.println("########## installMap : " + installMap);
                            sqlMap.executeUpdate("insertNCLReleaseInstallments", installMap);
                        }

                        //Insert Loan Trans Table
                        HashMap loanTransMap = new HashMap();
                        loanTransMap.put("ACCOUNTNO", linkBatchId);
//                        loanTransMap.put("PRINCIPLE", CommonUtil.convertObjToDouble(0));
                        loanTransMap.put("PRINCIPLE", String.valueOf(new Double(0)));
//                        loanTransMap.put("INTEREST", "");
                        loanTransMap.put("INTEREST", String.valueOf(new Double(0)));
//                        loanTransMap.put("PENAL", CommonUtil.convertObjToDouble(0));
                        loanTransMap.put("PENAL", String.valueOf(new Double(0)));
                        loanTransMap.put("PROD_ID", "");
                        loanTransMap.put("TRANSTYPE", "DEBIT");
                        loanTransMap.put("BRANCH_CODE", _branchCode);
                        loanTransMap.put("TRN_CODE", "DP");
                        loanTransMap.put("TODAY_DT", currDt);
                        loanTransMap.put("EFFECTIVE_DT", currDt);
                        loanTransMap.put("IBAL", "");
                        loanTransMap.put("PIBAL", "");
                        loanTransMap.put("PBAL", String.valueOf(releasedAmt));
                        loanTransMap.put("TRANS_ID", linkBatchId);
                        loanTransMap.put("AUTHORIZE_STATUS", authorizeStatus);
                        loanTransMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                        loanTransMap.put("TRANS_SLNO", new Long(1));
                        loanTransMap.put("EXPENSE", "");
                        loanTransMap.put("EBAL", "");
                        loanTransMap.put("NPA_INTEREST", "");
                        loanTransMap.put("NPA_INT_BAL", "");
                        loanTransMap.put("NPA_PENAL", "");
                        loanTransMap.put("NPA_PENAL_BAL", "");
                        loanTransMap.put("EXCESS_AMT", "");
                        loanTransMap.put("TRANS_MODE", "");
                        sqlMap.executeUpdate("insertintoloanTransAuthDetails", loanTransMap);
                    }
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    public static void main(String str[]) {
        try {
            ReleaseDetailsDAO dao = new ReleaseDetailsDAO();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void destroyObjects() {
        objReleaseDetailsTO = null;
        objClasficationDetailsTO = null;
        membertableDetails = null;
        loantableDetails = null;
    }
}