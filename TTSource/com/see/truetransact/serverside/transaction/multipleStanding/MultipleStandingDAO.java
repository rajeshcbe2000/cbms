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
package com.see.truetransact.serverside.transaction.multipleStanding;

import com.see.truetransact.serverside.payroll.payMaster.*;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.product.mds.MDSProductSchemeTO;
import com.see.truetransact.transferobject.product.mds.MDSProductAcctHeadTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.payroll.PayRollTo;
import com.see.truetransact.transferobject.payroll.earningsDeductions.EarnDeduPayTO;

import com.see.truetransact.transferobject.payroll.pfMaster.PFMasterTO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.transferobject.transaction.multipleStanding.MultiStandingMasterTO;
import com.see.truetransact.transferobject.transaction.multipleStanding.MultipleStandingActDetailsTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This is used for OperativeAcctProductDAO Data Access.
 *
 * @author SreeKrishnan
 *
 *
 */
public class MultipleStandingDAO extends TTDAO {

    private final static Logger log = Logger.getLogger(MultipleStandingDAO.class);
    private MultipleStandingActDetailsTO standingTO = null;
    private LinkedHashMap deletedTableValues = null;
    private LinkedHashMap tableDetails = null;
    private LinkedHashMap payrollMap = null;
    private MDSProductAcctHeadTO acctHeadTo;
    private MDSProductSchemeTO schemeTo;
    private static SqlMap sqlMap = null;
    private Date CurrDt = null;
    private LogDAO logDAO;
    private LogTO logTO;
    private HashMap returnDataMap = null;
    private HashMap returnPfCashMap = null;
    private HashMap returnPfTransMap = null;
    private TransactionDAO transactionDAO = null;
    TransferDAO transferDAO = new TransferDAO();
    public List standingList = null;
    private String standingId = "";

    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public MultipleStandingDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private String getStandingId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "MULTIPLE_STAND_ID");
        System.out.println("testing branch id :: " + _branchCode);
        where.put("BRANCH_CODE", _branchCode);// Added by nithya
        String standingId = CommonUtil.convertObjToStr((dao.executeQuery(where)).get(CommonConstants.DATA));
        return standingId;
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        if (map.containsKey("REMITT_ISSUE_TRANS")) {
            List list = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", map);
            if (list != null && list.size() > 0) {
                returnMap.put("TRANSACTION_LIST", list);
                list = null;
            }
        }
        List shgList = (List) sqlMap.executeQueryForList("getStandingMasterTO", map);
        if (shgList != null && shgList.size() > 0) {
            HashMap ParMap = new HashMap();
            System.out.println("@@@shgList" + shgList);
//            for (int i = shgList.size(), j = 0; i > 0; i--, j++) {
//                ParMap.put(((MultiStandingMasterTO) shgList.get(j)).getMasterActNo(), shgList.get(j));
//            }
           // ParMap = (HashMap)shgList.get(0);           
            System.out.println("@@@ParMap" + shgList.get(0));
            returnMap.put("StandingMasterTO_DATA", shgList.get(0));
        }
        
        List standActLst = (List) sqlMap.executeQueryForList("getMultipleStandingActDetailsTO", map);
        if(standActLst != null && standActLst.size() > 0){
          LinkedHashMap ParMap = new LinkedHashMap();
            System.out.println("@@@shgList" + standActLst);
            for (int i = standActLst.size(), j = 0; i > 0; i--, j++) {
                ParMap.put(((MultipleStandingActDetailsTO) standActLst.get(j)).getActAccNo(), standActLst.get(j));
            }
            System.out.println("@@@ParMap" + ParMap);
            returnMap.put("StandingMasterTO_ACT_DATA", ParMap);
        }
        
        return returnMap;

    }

    public static void main(String str[]) {
        try {
            MultipleStandingDAO dao = new MultipleStandingDAO();
            HashMap inputMap = new HashMap();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {

        System.out.println("Map in MultipleStandingDAO  : " + map);
        try {
            _branchCode = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
            CurrDt = ServerUtil.getCurrentDate(_branchCode);
            HashMap returnMap = new HashMap();
            logDAO = new LogDAO();
            logTO = new LogTO();
            returnDataMap = new HashMap();
            returnPfTransMap = new HashMap();
            returnPfCashMap = new HashMap();
            tableDetails = (LinkedHashMap) map.get("StandingMasterDetails");
            deletedTableValues = (LinkedHashMap) map.get("DeleteStandingMasterDetails");
            final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                if (map.containsKey("STANDING_TABLE_DATA")) {
                    standingList = (List) map.get("STANDING_TABLE_DATA");
                }
                insertData(map);
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData(map);
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData(map);
            } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
                if (map != null) {
                    authorize(map);
                    //To PayRoll ....
                    HashMap statusMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
                    String status = CommonUtil.convertObjToStr(statusMap.get(CommonConstants.AUTHORIZESTATUS));
                    if (status.equals(CommonConstants.STATUS_AUTHORIZED)) {
                        if (map.containsKey("PayRollMap")) {
                            payrollMap = (LinkedHashMap) map.get("PayRollMap");
                            PayRollTo payRollTo = (PayRollTo) payrollMap.get("PayRollTo");
                            payRollTo.setPayrollId(getPayRollID());
                            sqlMap.executeUpdate("insertPayRollTo", payRollTo);
                            HashMap updateMap = new HashMap();
                            updateMap.put("PAYROLLID", payRollTo.getPayrollId());
                            updateMap.put("generateSingleTransId", "INDIVIDUAL");
                            sqlMap.executeUpdate("updatePayrollData", updateMap);
                        }
                    }
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
        destroyObjects();
        returnDataMap.put("MSID",standingId);
        return returnDataMap;
    }

    private String getPayRollID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "PAYROLL_ENTRY_ID");
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            if (map.containsKey("TAB_DATA")) {                
                if (map.containsKey("TAB_DATA") && map.get("TAB_DATA").equals("TRANSACTION_DATA")) {
                    //To Tansaction part....
                    doTransactionNew(map, logDAO, logTO);
                } else {
                    standingId = getStandingId();
                    //SI_MASTER_DETAILS
                    if(map.containsKey("SI_MASTER_DETAILS") && map.get("SI_MASTER_DETAILS") != null){ // Added by nithya
                        MultiStandingMasterTO objMultiStandingMasterTO = (MultiStandingMasterTO)map.get("SI_MASTER_DETAILS");
                        objMultiStandingMasterTO.setStandingId(standingId);
                        objMultiStandingMasterTO.setAuthorizedStatus(CommonConstants.STATUS_AUTHORIZED);
                        objMultiStandingMasterTO.setAuthorizeBy(CommonUtil.convertObjToStr(objMultiStandingMasterTO.getStatusBy()));
                        objMultiStandingMasterTO.setAuthorizeDt((Date) CurrDt.clone());
                        objMultiStandingMasterTO.setBranchId(_branchCode);
                        sqlMap.executeUpdate("insertMultipleStandingTO", objMultiStandingMasterTO);
                        objMultiStandingMasterTO = null;
                    }
                    ArrayList addList = new ArrayList(tableDetails.keySet());                   
                    for (int i = 0; i < addList.size(); i++) {
                        MultipleStandingActDetailsTO multipleStandingTO = (MultipleStandingActDetailsTO) tableDetails.get(addList.get(i));
                        //if (earnDeduPayTO.getPayTrans() != null && earnDeduPayTO.getPayTrans().equals("Y")) {                    
                        multipleStandingTO.setStandingId(standingId);
                        multipleStandingTO.setAuthorizedStatus(CommonConstants.STATUS_AUTHORIZED);
                        multipleStandingTO.setAuthorizeBy(CommonUtil.convertObjToStr(multipleStandingTO.getStatusBy()));
                        multipleStandingTO.setAuthorizedDt((Date) CurrDt.clone());
                        multipleStandingTO.setBranchId(_branchCode);
                        multipleStandingTO.setIsActive("Y");
                        sqlMap.executeUpdate("insertMultipleStandingActDetailsTO", multipleStandingTO);
                        logTO.setData(multipleStandingTO.toString());
                        logTO.setPrimaryKey(multipleStandingTO.getKeyData());
                        logDAO.addToLog(logTO);
                        multipleStandingTO = null;
                    }
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void doTransaction(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            if (standingList != null && standingList.size() > 0) {
                TransactionTO transactionTO = new TransactionTO();
                HashMap txMap = new HashMap();
                HashMap transMap = new HashMap();
                HashMap headMap = new HashMap();
                HashMap prodMap = new HashMap();
                ArrayList TxTransferTO = new ArrayList();
                double transAmt = 0.0;
                List lst = null;
                System.out.println("standingList map####" + map);
                HashMap acHdmap = new HashMap();
                HashMap tansactionMap = new HashMap();
                String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_ID"));
                String initiatedBranch = CommonUtil.convertObjToStr(map.get("INITIATED_BRANCH"));
                if (map.get("TRANS_TYPE").equals("CREDIT")) {
                    for (int k = 0; k < standingList.size(); k++) {
                        HashMap dataMap = new HashMap();
                        dataMap = (HashMap) standingList.get(k);
                        ArrayList transferList = new ArrayList();
                        TransferTrans transferTrans = new TransferTrans();
                        transferTrans.setInitiatedBranch(BRANCH_ID);
                        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                        transactionDAO.setInitiatedBranch(initiatedBranch);
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        TxTransferTO transferTo = new TxTransferTO();
                        String loanAccNo = "";
                        loanAccNo = CommonUtil.convertObjToStr(map.get("ACT_NUM"));
                        transAmt = CommonUtil.convertObjToDouble(dataMap.get("TRANS_AMOUNT"));
                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
                        if (!dataMap.get("TRANS_PROD_TYPE").equals("") && dataMap.get("TRANS_PROD_TYPE").equals("OA")) {
                            headMap.put("ACT_NUM",CommonUtil.convertObjToStr(dataMap.get("TRANS_ACC_NO")));
                            lst = sqlMap.executeQueryForList("getAccNoProdIdDet", headMap);
                            if (lst != null && lst.size() > 0) {
                                headMap = (HashMap) lst.get(0);
                            }
                        }
                        //babu added by transfer sus case
                        System.out.println("transactionTO.getProductType() == " + transactionTO.getProductType());
                        if (!dataMap.get("TRANS_PROD_TYPE").equals("") && dataMap.get("TRANS_PROD_TYPE").equals("SA")) {
                            headMap.put("prodId", CommonUtil.convertObjToStr(dataMap.get("TRANS_PROD_ID")));
                            lst = sqlMap.executeQueryForList("getAccountHeadProdSAHead", headMap);
                            if (lst != null && lst.size() > 0) {
                                headMap = (HashMap) lst.get(0);
                                //transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                            }

                        }
                        if (transAmt > 0) {
                            //DEBIT
                            transferTrans.setInitiatedBranch(BRANCH_ID);
                            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                            transactionDAO.setInitiatedBranch(initiatedBranch);
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            if (!dataMap.get("TRANS_PROD_TYPE").equals("") && dataMap.get("TRANS_PROD_TYPE").equals("GL")) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) (String) dataMap.get("TRANS_ACC_NO"));
                                txMap.put(TransferTrans.DR_PROD_TYPE, (String) (String) dataMap.get("TRANS_PROD_TYPE"));
                            }else{
                                txMap.put(TransferTrans.DR_ACT_NUM, (String) (String) dataMap.get("TRANS_ACC_NO"));
                                txMap.put(TransferTrans.DR_AC_HD, (String) (String) headMap.get("AC_HD_ID"));
                                txMap.put(TransferTrans.DR_PROD_TYPE, (String) (String) dataMap.get("TRANS_PROD_TYPE"));
                                txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(dataMap.get("TRANS_PROD_ID")));
                            }
                            txMap.put(TransferTrans.PARTICULARS,CommonUtil.convertObjToStr(dataMap.get("PARTICULARS")));
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                            txMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
                            txMap.put("TRANS_MOD_TYPE", (String) (String) dataMap.get("TRANS_PROD_TYPE"));
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt((Date) CurrDt.clone());
                            transferTo.setBranchId(BRANCH_ID);
                            transferTo.setInitiatedBranch(initiatedBranch);
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setAuthorizeRemarks("DP");
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setStatusDt((Date) CurrDt.clone());
                            //transferTo.setLinkBatchId(loanAccNo);
                            TxTransferTO.add(transferTo);
                        }
                    }
                    //CREDIT
                    TxTransferTO transferTo = new TxTransferTO();
                    txMap.put(TransferTrans.CR_AC_HD, (String) (String) map.get("ACCOUNT_HEAD_ID"));
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.PARTICULARS,"Total "+map.get("TRANS_TYPE")+" From Multiple Standing");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                    txMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
                    txMap.put("TRANS_MOD_TYPE", CommonConstants.ADVANCE_TRANSMODE_TYPE);
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(map.get("TOTAL_AMOUNT")));
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setTransDt((Date) CurrDt.clone());
                    transferTo.setBranchId(BRANCH_ID);
                    transferTo.setInitiatedBranch(initiatedBranch);
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    //transferTo.setAuthorizeRemarks("DP");
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setStatusDt((Date) CurrDt.clone());
                    transferTo.setAuthorizeRemarks("OTHER_CHARGES");
                    transferTo.setHierarchyLevel("1");
                    transferTo.setInstrumentNo2("LOAN_OTHER_CHARGES");
                    TxTransferTO.add(transferTo);                    
                } else {
                    for (int k = 0; k < standingList.size(); k++) {
                        HashMap dataMap = new HashMap();
                        dataMap = (HashMap) standingList.get(k);
                        ArrayList transferList = new ArrayList();
                        TransferTrans transferTrans = new TransferTrans();
                        transferTrans.setInitiatedBranch(BRANCH_ID);
                        transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
                        transactionDAO.setInitiatedBranch(initiatedBranch);
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        TxTransferTO transferTo = new TxTransferTO();
                        String loanAccNo = "";
                        loanAccNo = CommonUtil.convertObjToStr(map.get("ACT_NUM"));
                        transAmt = CommonUtil.convertObjToDouble(dataMap.get("TRANS_AMOUNT"));
                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
                        if (!dataMap.get("TRANS_PROD_TYPE").equals("") && dataMap.get("TRANS_PROD_TYPE").equals("OA")) {
                            headMap.put("ACT_NUM",CommonUtil.convertObjToStr(dataMap.get("TRANS_ACC_NO")));
                            lst = sqlMap.executeQueryForList("getAccNoProdIdDet", headMap);
                            if (lst != null && lst.size() > 0) {
                                headMap = (HashMap) lst.get(0);
                            }
                        }
                        //babu added by transfer sus case
                        System.out.println("transactionTO.getProductType() == " + transactionTO.getProductType());
                        if (!dataMap.get("TRANS_PROD_TYPE").equals("") && dataMap.get("TRANS_PROD_TYPE").equals("OA")) {
                            headMap.put("prodId", CommonUtil.convertObjToStr(dataMap.get("TRANS_PROD_ID")));
                            lst = sqlMap.executeQueryForList("getAccountHeadProdSAHead", headMap);
                            if (lst != null && lst.size() > 0) {
                                headMap = (HashMap) lst.get(0);
                                //transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                            }

                        }
                        if (transAmt > 0) {
                            //DEBIT
                            transferTrans.setInitiatedBranch(BRANCH_ID);
                            transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
                            transactionDAO.setInitiatedBranch(initiatedBranch);
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            if (!dataMap.get("TRANS_PROD_TYPE").equals("") && dataMap.get("TRANS_PROD_TYPE").equals("GL")) {
                                txMap.put(TransferTrans.CR_AC_HD, (String) (String) dataMap.get("TRANS_ACC_NO"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, (String) (String) dataMap.get("TRANS_PROD_TYPE"));
                            }else{
                                txMap.put(TransferTrans.CR_ACT_NUM, (String) (String) dataMap.get("TRANS_ACC_NO"));
                                txMap.put(TransferTrans.CR_AC_HD, (String) (String) headMap.get("AC_HD_ID"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, (String) (String) dataMap.get("TRANS_PROD_TYPE"));
                                txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(dataMap.get("TRANS_PROD_ID")));
                            }
                            txMap.put(TransferTrans.PARTICULARS,CommonUtil.convertObjToStr(dataMap.get("PARTICULARS")));
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                            txMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
                            txMap.put("TRANS_MOD_TYPE", (String) (String) dataMap.get("TRANS_PROD_TYPE"));
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt((Date) CurrDt.clone());
                            transferTo.setBranchId(BRANCH_ID);
                            transferTo.setInitiatedBranch(initiatedBranch);
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setAuthorizeRemarks("DP");
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setStatusDt((Date) CurrDt.clone());
                            //transferTo.setLinkBatchId(loanAccNo);
                            TxTransferTO.add(transferTo);
                        }
                    }
                    //CREDIT
                    TxTransferTO transferTo = new TxTransferTO();
                    txMap.put(TransferTrans.DR_AC_HD, (String) (String) map.get("ACCOUNT_HEAD_ID"));
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.PARTICULARS,"Total "+map.get("TRANS_TYPE")+" From Multiple Standing");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                    txMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
                    txMap.put("TRANS_MOD_TYPE", CommonConstants.ADVANCE_TRANSMODE_TYPE);
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(map.get("TOTAL_AMOUNT")));
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setTransDt((Date) CurrDt.clone());
                    transferTo.setBranchId(BRANCH_ID);
                    transferTo.setInitiatedBranch(initiatedBranch);
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    //transferTo.setAuthorizeRemarks("DP");
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setStatusDt((Date) CurrDt.clone());
                    transferTo.setAuthorizeRemarks("OTHER_CHARGES");
                    transferTo.setHierarchyLevel("1");
                    transferTo.setInstrumentNo2("LOAN_OTHER_CHARGES");
                    TxTransferTO.add(transferTo); 
                }
                if(TxTransferTO!=null && TxTransferTO.size()>0){
                    transferDAO = new TransferDAO();
                    tansactionMap.put("TxTransferTO", TxTransferTO);
                    tansactionMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                    tansactionMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                    tansactionMap.put(CommonConstants.BRANCH_ID, initiatedBranch);
                    tansactionMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                    System.out.println("################ tansactionMap :" + tansactionMap);
                    transMap = transferDAO.execute(tansactionMap, false);                    
                    getTransDetails(CommonUtil.convertObjToStr(transMap.get("SINGLE_TRANS_ID")));
                    dotransferAuthorize(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")),CommonConstants.STATUS_AUTHORIZED, tansactionMap);
                }
                standingList = null;
                objLogDAO.addToLog(objLogTO);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
    
    private void doTransactionNew(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            System.out.println("Inside doTransaction new :: standingList :: " + standingList);
            if (standingList != null && standingList.size() > 0) {
                HashMap trasTypeMap = new HashMap();
                trasTypeMap = (HashMap) standingList.get(0);
                String masterTransType = CommonUtil.convertObjToStr(trasTypeMap.get("TRANS_TYPE"));
                String masterProdType = CommonUtil.convertObjToStr(trasTypeMap.get("MASTER_PROD_TYPE"));
                String masterProdId = CommonUtil.convertObjToStr(trasTypeMap.get("MASTER_PROD_ID"));
                String masterAcctNo = CommonUtil.convertObjToStr(trasTypeMap.get("MASTER_ACC_NO"));
                String masterParticulars = CommonUtil.convertObjToStr(trasTypeMap.get("MASTER_PARTICULARS"));
                TransactionTO transactionTO = new TransactionTO();
                HashMap txMap = new HashMap();
                HashMap transMap = new HashMap();
                HashMap headMap = new HashMap();
                HashMap prodMap = new HashMap();
                ArrayList TxTransferTO = new ArrayList();
                double transAmt = 0.0;
                List lst = null;
                System.out.println("standingList map####" + map);
                HashMap acHdmap = new HashMap();
                HashMap tansactionMap = new HashMap();
                String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_ID"));
                String initiatedBranch = CommonUtil.convertObjToStr(map.get("INITIATED_BRANCH"));
                if (masterTransType.equalsIgnoreCase("CREDIT")) {
                    for (int k = 0; k < standingList.size(); k++) {
                        HashMap dataMap = new HashMap();
                        dataMap = (HashMap) standingList.get(k);
                        ArrayList transferList = new ArrayList();
                        TransferTrans transferTrans = new TransferTrans();
                        transferTrans.setInitiatedBranch(BRANCH_ID);
                        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                        transactionDAO.setInitiatedBranch(initiatedBranch);
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        TxTransferTO transferTo = new TxTransferTO();
                        String loanAccNo = "";
                        loanAccNo = CommonUtil.convertObjToStr(map.get("ACT_NUM"));
                        transAmt = CommonUtil.convertObjToDouble(dataMap.get("TRANS_AMOUNT"));
                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
                        if (!dataMap.get("ACT_PROD_TYPE").equals("") && dataMap.get("ACT_PROD_TYPE").equals("OA")) {
                            headMap.put("ACT_NUM", CommonUtil.convertObjToStr(dataMap.get("ACT_ACC_NO")));
                            lst = sqlMap.executeQueryForList("getAccNoProdIdDet", headMap);
                            if (lst != null && lst.size() > 0) {
                                headMap = (HashMap) lst.get(0);
                            }
                        }                        
                        if (!dataMap.get("ACT_PROD_TYPE").equals("") && dataMap.get("ACT_PROD_TYPE").equals("SA")) {
                            headMap.put("prodId", CommonUtil.convertObjToStr(dataMap.get("ACT_PROD_ID")));
                            lst = sqlMap.executeQueryForList("getAccountHeadProdSAHead", headMap);
                            if (lst != null && lst.size() > 0) {
                                headMap = (HashMap) lst.get(0);
                            }
                        }
                        if (transAmt > 0) {
                            //DEBIT
                            transferTrans.setInitiatedBranch(BRANCH_ID);
                            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                            transactionDAO.setInitiatedBranch(initiatedBranch);
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            if (!dataMap.get("ACT_PROD_TYPE").equals("") && dataMap.get("ACT_PROD_TYPE").equals("GL")) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) (String) dataMap.get("ACT_ACC_NO"));
                                txMap.put(TransferTrans.DR_PROD_TYPE, (String) (String) dataMap.get("ACT_PROD_TYPE"));
                            } else {
                                txMap.put(TransferTrans.DR_ACT_NUM, (String) (String) dataMap.get("ACT_ACC_NO"));
                                txMap.put(TransferTrans.DR_AC_HD, (String) (String) headMap.get("AC_HD_ID"));
                                txMap.put(TransferTrans.DR_PROD_TYPE, (String) (String) dataMap.get("ACT_PROD_TYPE"));
                                txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(dataMap.get("ACT_PROD_ID")));
                            }
                            if(dataMap.containsKey("ACT_PARTICULARS") && dataMap.get("ACT_PARTICULARS")!= null && !dataMap.get("ACT_PARTICULARS").equals("")){
                                txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(dataMap.get("ACT_PARTICULARS")));
                            }else{
                                txMap.put(TransferTrans.PARTICULARS, "By " + (String) dataMap.get("ACT_ACC_NO"));
                            }       
                            txMap.put("SCREEN_NAME","MULTIPLE STANDING");                            
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                            txMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
                            txMap.put("TRANS_MOD_TYPE", (String) (String) dataMap.get("ACT_PROD_TYPE"));
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                            transferTo.setTransId("-");
                            if(map.containsKey("MULTI_STANDING_ID") && map.get("MULTI_STANDING_ID") != null && !map.get("MULTI_STANDING_ID").equals(""))
                              transferTo.setLinkBatchId(CommonUtil.convertObjToStr(map.get("MULTI_STANDING_ID")));
                            transferTo.setBatchId("-");
                            transferTo.setTransDt((Date) CurrDt.clone());
                            transferTo.setBranchId(BRANCH_ID);
                            transferTo.setInitiatedBranch(initiatedBranch);
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setAuthorizeRemarks("DP");
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setStatusDt((Date) CurrDt.clone());
                            //transferTo.setLinkBatchId(loanAccNo);
                            TxTransferTO.add(transferTo);
                        }
                    }
                    // Code for credit to master account
                    if (masterProdType.equalsIgnoreCase("OA")) {
                        headMap.put("ACT_NUM", masterAcctNo);
                        lst = sqlMap.executeQueryForList("getAccNoProdIdDet", headMap);
                        if (lst != null && lst.size() > 0) {
                            headMap = (HashMap) lst.get(0);
                        }
                    }
                    if (masterProdType.equalsIgnoreCase("SA")) {
                        headMap.put("prodId", masterProdId);
                        lst = sqlMap.executeQueryForList("getAccountHeadProdSAHead", headMap);
                        if (lst != null && lst.size() > 0) {
                            headMap = (HashMap) lst.get(0);
                        }
                    }               
                    TxTransferTO transferTo = new TxTransferTO();
                    if(masterProdType.equalsIgnoreCase(TransactionFactory.GL)){
                        txMap.put(TransferTrans.CR_AC_HD, masterAcctNo);
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);    
                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                    }else{
                        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(headMap.get("AC_HD_ID")));
                        txMap.put("TRANS_MOD_TYPE",masterProdType);
                        txMap.put(TransferTrans.CR_PROD_TYPE, masterProdType);  
                    }
                    txMap.put(TransferTrans.CR_ACT_NUM,masterAcctNo);
                    txMap.put(TransferTrans.CR_PROD_ID,masterProdId);                     
                    if(masterParticulars.length() == 0){
                        txMap.put(TransferTrans.PARTICULARS,"Total "+map.get("TRANS_TYPE")+" From Multiple Standing");
                    }else{
                        txMap.put(TransferTrans.PARTICULARS, masterParticulars);
                    }   
                    txMap.put("SCREEN_NAME","MULTIPLE STANDING");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                    txMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));                    
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(map.get("TOTAL_AMOUNT")));
                    transferTo.setTransId("-");
                    if(map.containsKey("MULTI_STANDING_ID") && map.get("MULTI_STANDING_ID") != null && !map.get("MULTI_STANDING_ID").equals(""))
                      transferTo.setLinkBatchId(CommonUtil.convertObjToStr(map.get("MULTI_STANDING_ID")));
                    transferTo.setBatchId("-");
                    transferTo.setTransDt((Date) CurrDt.clone());
                    transferTo.setBranchId(BRANCH_ID);
                    transferTo.setInitiatedBranch(initiatedBranch);
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    //transferTo.setAuthorizeRemarks("DP");
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setStatusDt((Date) CurrDt.clone());
                    transferTo.setAuthorizeRemarks("OTHER_CHARGES");
                    transferTo.setHierarchyLevel("1");
                    transferTo.setInstrumentNo2("LOAN_OTHER_CHARGES");
                    TxTransferTO.add(transferTo);     
                    
                    // End
                }else{                   
                    for (int k = 0; k < standingList.size(); k++) {
                        HashMap dataMap = new HashMap();
                        dataMap = (HashMap) standingList.get(k);
                        ArrayList transferList = new ArrayList();
                        TransferTrans transferTrans = new TransferTrans();
                        transferTrans.setInitiatedBranch(BRANCH_ID);
                        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                        transactionDAO.setInitiatedBranch(initiatedBranch);
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        TxTransferTO transferTo = new TxTransferTO();
                        String loanAccNo = "";
                        loanAccNo = CommonUtil.convertObjToStr(map.get("ACT_NUM"));
                        transAmt = CommonUtil.convertObjToDouble(dataMap.get("TRANS_AMOUNT"));
                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
                        if (!dataMap.get("ACT_PROD_TYPE").equals("") && dataMap.get("ACT_PROD_TYPE").equals("OA")) {
                            headMap.put("ACT_NUM", CommonUtil.convertObjToStr(dataMap.get("ACT_ACC_NO")));
                            lst = sqlMap.executeQueryForList("getAccNoProdIdDet", headMap);
                            if (lst != null && lst.size() > 0) {
                                headMap = (HashMap) lst.get(0);
                            }
                        }                        
                        if (!dataMap.get("ACT_PROD_TYPE").equals("") && dataMap.get("ACT_PROD_TYPE").equals("SA")) {
                            headMap.put("prodId", CommonUtil.convertObjToStr(dataMap.get("ACT_PROD_ID")));
                            lst = sqlMap.executeQueryForList("getAccountHeadProdSAHead", headMap);
                            if (lst != null && lst.size() > 0) {
                                headMap = (HashMap) lst.get(0);
                            }
                        }
                        if (transAmt > 0) {                            
                            transferTrans.setInitiatedBranch(BRANCH_ID);
                            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                            transactionDAO.setInitiatedBranch(initiatedBranch);
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            if (!dataMap.get("ACT_PROD_TYPE").equals("") && dataMap.get("ACT_PROD_TYPE").equals("GL")) {
                                txMap.put(TransferTrans.CR_AC_HD, (String) dataMap.get("ACT_ACC_NO"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);    
                                txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                            } else {
                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(headMap.get("AC_HD_ID")));
                                txMap.put("TRANS_MOD_TYPE",(String) dataMap.get("ACT_PROD_TYPE"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, (String) dataMap.get("ACT_PROD_TYPE"));
                                txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(dataMap.get("ACT_PROD_ID"))); 
                                txMap.put(TransferTrans.CR_ACT_NUM, (String) dataMap.get("ACT_ACC_NO"));                                
                            }
                            if(dataMap.containsKey("ACT_PARTICULARS") && !dataMap.get("ACT_PARTICULARS").equals("") && !dataMap.get("ACT_PARTICULARS").equals(null)){
                                txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(dataMap.get("ACT_PARTICULARS")));
                            }else{
                                txMap.put(TransferTrans.PARTICULARS, "By " + (String) dataMap.get("ACT_ACC_NO"));
                            } 
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                            txMap.put("SCREEN_NAME","MULTIPLE STANDING");
                            txMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));                            
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt((Date) CurrDt.clone());
                            transferTo.setBranchId(BRANCH_ID);
                            transferTo.setInitiatedBranch(initiatedBranch);
                            if(map.containsKey("MULTI_STANDING_ID") && map.get("MULTI_STANDING_ID") != null && !map.get("MULTI_STANDING_ID").equals(""))
                              transferTo.setLinkBatchId(CommonUtil.convertObjToStr(map.get("MULTI_STANDING_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setAuthorizeRemarks("DP");
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setStatusDt((Date) CurrDt.clone());
                            //transferTo.setLinkBatchId(loanAccNo);
                            TxTransferTO.add(transferTo);
                        }
                    }
                    if (masterProdType.equalsIgnoreCase("OA")) {
                        headMap.put("ACT_NUM", masterAcctNo);
                        lst = sqlMap.executeQueryForList("getAccNoProdIdDet", headMap);
                        if (lst != null && lst.size() > 0) {
                            headMap = (HashMap) lst.get(0);
                        }
                    }
                    if (masterProdType.equalsIgnoreCase("SA")) {
                        headMap.put("prodId", masterProdId);
                        lst = sqlMap.executeQueryForList("getAccountHeadProdSAHead", headMap);
                        if (lst != null && lst.size() > 0) {
                            headMap = (HashMap) lst.get(0);
                        }
                    } 
                    TxTransferTO transferTo = new TxTransferTO();
                    if(masterProdType.equalsIgnoreCase(TransactionFactory.GL)){
                        txMap.put(TransferTrans.DR_AC_HD, masterAcctNo);
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);    
                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                    }else{
                        txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(headMap.get("AC_HD_ID")));
                        txMap.put("TRANS_MOD_TYPE",masterProdType);
                        txMap.put(TransferTrans.DR_PROD_TYPE, masterProdType);  
                    }
                    txMap.put(TransferTrans.DR_ACT_NUM,masterAcctNo);
                    txMap.put(TransferTrans.DR_PROD_ID,masterProdId); 
                    if(masterParticulars.length() == 0){
                        txMap.put(TransferTrans.PARTICULARS,"Total "+map.get("TRANS_TYPE")+" From Multiple Standing");
                    }else{
                        txMap.put(TransferTrans.PARTICULARS, masterParticulars);
                    }  
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                    txMap.put("SCREEN_NAME","MULTIPLE STANDING");
                    txMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));                    
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(map.get("TOTAL_AMOUNT")));
                    transferTo.setTransId("-");
                    if(map.containsKey("MULTI_STANDING_ID") && map.get("MULTI_STANDING_ID") != null && !map.get("MULTI_STANDING_ID").equals(""))
                      transferTo.setLinkBatchId(CommonUtil.convertObjToStr(map.get("MULTI_STANDING_ID")));
                    transferTo.setBatchId("-");
                    transferTo.setTransDt((Date) CurrDt.clone());
                    transferTo.setBranchId(BRANCH_ID);
                    transferTo.setInitiatedBranch(initiatedBranch);
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    //transferTo.setAuthorizeRemarks("DP");
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setStatusDt((Date) CurrDt.clone());
                    transferTo.setAuthorizeRemarks("OTHER_CHARGES");
                    transferTo.setHierarchyLevel("1");
                    transferTo.setInstrumentNo2("LOAN_OTHER_CHARGES");
                    TxTransferTO.add(transferTo);     
                }
                System.out.println("Nithya final TxTransferTO :: "+ TxTransferTO);
                if(TxTransferTO!=null && TxTransferTO.size()>0){
                    transferDAO = new TransferDAO();
                    tansactionMap.put("TxTransferTO", TxTransferTO);
                    tansactionMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                    tansactionMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                    tansactionMap.put(CommonConstants.BRANCH_ID, initiatedBranch);
                    tansactionMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                    System.out.println("################ tansactionMap :" + tansactionMap);
                    transMap = transferDAO.execute(tansactionMap, false);                    
                    getTransDetails(CommonUtil.convertObjToStr(transMap.get("SINGLE_TRANS_ID")));
                    dotransferAuthorize(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")),CommonConstants.STATUS_AUTHORIZED, tansactionMap);
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }     

    private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", CurrDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        returnDataMap = new HashMap();
        List transList = (List) sqlMap.executeQueryForList("getTransferDetailsPayRoll", getTransMap);
        if (transList != null && transList.size() > 0) {
            returnDataMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetailsPayRoll", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            returnDataMap.put("CASH_TRANS_LIST", cashList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    private CashTransactionTO createCashDebitTransactionTO(HashMap dataMap) throws Exception {
        CashTransactionTO objCashTO = new CashTransactionTO();
        if (dataMap.get("STATUS").equals(CommonConstants.TOSTATUS_INSERT)) {
            objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
            objCashTO.setActNum(CommonUtil.convertObjToStr(dataMap.get("ACCT_NO")));
            objCashTO.setProdType(CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")));
            objCashTO.setProdId(CommonUtil.convertObjToStr(dataMap.get("PROD_ID")));
            objCashTO.setTransType(CommonUtil.convertObjToStr(dataMap.get("TRANS_TYPE")));
            objCashTO.setParticulars(CommonUtil.convertObjToStr(dataMap.get("PARTICULARS")));
            objCashTO.setAuthorizeRemarks(CommonUtil.convertObjToStr(dataMap.get("AUTHORIZEREMARKS")));
            objCashTO.setInpAmount(CommonUtil.convertObjToDouble(dataMap.get("TRANS_AMOUNT")));
            objCashTO.setAmount(CommonUtil.convertObjToDouble(dataMap.get("TRANS_AMOUNT")));
            objCashTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.USER_ID)));
            objCashTO.setBranchId(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
            objCashTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.USER_ID)));
            objCashTO.setStatusDt(CurrDt);
            objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
            if (objCashTO.getTransType().equals(CommonConstants.DEBIT)) {
                objCashTO.setInstType("VOUCHER");
            }
            objCashTO.setCommand("INSERT");
            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("LINK_BATCH_ID")));
            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
            System.out.println("objCashTO 1st one:" + objCashTO);
        }
        return objCashTO;
    }

    private void doCashTrans(TransactionTO transactionTO, ArrayList batchList, String branchCode, boolean isAutoAuthorize) throws Exception {
        CashTransactionDAO cashDAO = new CashTransactionDAO();
        HashMap data = new HashMap();
        data.put("DAILYDEPOSITTRANSTO", batchList);
        data.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
        data.put("INITIATED_BRANCH", branchCode);
        data.put(CommonConstants.BRANCH_ID, branchCode);
        data.put(CommonConstants.USER_ID, logTO.getUserId());
        data.put(CommonConstants.MODULE, "Transaction");
        data.put(CommonConstants.SCREEN, "");
        data.put("MODE", "MODE");
        returnDataMap = cashDAO.execute(data, false);
        returnPfCashMap = new HashMap();
        returnPfCashMap.putAll(returnDataMap);
        authorizeTransaction(returnDataMap, data);
        transactionTO.setBatchId(CommonUtil.convertObjToStr(returnDataMap.get("TRANS_ID")));
        transactionTO.setTransId(CommonUtil.convertObjToStr(returnDataMap.get("TRANS_ID")));
        transactionTO.setBatchDt(CurrDt);
        transactionTO.setStatus(CommonConstants.TOSTATUS_INSERT);
        transactionTO.setBranchId(_branchCode);
        if (CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue() > 0) {
            sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", transactionTO);
        }
        transactionTO = null;

    }

    private void doDebitCredit(TransactionTO transactionTO, ArrayList batchList, String branchCode, boolean isAutoAuthorize) throws Exception {
        TransferDAO transferDAO = new TransferDAO();
        HashMap data = new HashMap();
        data.put("TxTransferTO", batchList);
        data.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
        data.put("INITIATED_BRANCH", branchCode);
        data.put(CommonConstants.BRANCH_ID, branchCode);
        data.put(CommonConstants.USER_ID, logTO.getUserId());
        data.put(CommonConstants.MODULE, "Transaction");
        data.put(CommonConstants.SCREEN, "");
        data.put("MODE", "MODE");
        //For auto authorization        
        returnDataMap = transferDAO.execute(data, false);
        returnPfTransMap = new HashMap();
        returnPfTransMap.putAll(returnDataMap);
        authorizeTransaction(returnDataMap, data);
        transactionTO.setBatchId(CommonUtil.convertObjToStr(returnDataMap.get("TRANS_ID")));
        transactionTO.setTransId(CommonUtil.convertObjToStr(returnDataMap.get("TRANS_ID")));
        transactionTO.setBatchDt(CurrDt);
        transactionTO.setStatus(CommonConstants.TOSTATUS_INSERT);
        transactionTO.setBranchId(_branchCode);
        if (CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue() > 0) {
            sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", transactionTO);
        }
        transactionTO = null;
    }

    public void authorizeTransaction(HashMap transMap, HashMap map) throws Exception {
        try {
            if (transMap != null && transMap.get("TRANS_ID") != null && !transMap.get("TRANS_ID").equals("")) {
                System.out.println("TRANSFER TRANS_ID :" + transMap.get("TRANS_ID"));
                String authorizeStatus = CommonUtil.convertObjToStr(CommonConstants.STATUS_AUTHORIZED);
                String linkBatchId = transactionDAO.getLinkBatchID();
                HashMap cashAuthMap = new HashMap();
                cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                cashAuthMap.put("BATCH_ID", CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                TransactionDAO.authorizeCashAndTransfer(linkBatchId, authorizeStatus, cashAuthMap);
                cashAuthMap = null;
                transMap = null;
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void dotransferAuthorize(String batchid, String status, HashMap authMap) throws Exception {
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
        transferTransParam.put("TRANS_DT", CurrDt.clone());
        transferTransParam.put("TRANS_DATE", CurrDt.clone());
        transferTransParam.put("INITIATED_BRANCH", _branchCode);
        transferTransList = (ArrayList) sqlMap.executeQueryForList("getBatchTxTransferTOsAuthorize", transferTransParam);
        if (transferTransList != null && (!transferTransList.isEmpty())) {
            TransferTrans objTrans = new TransferTrans();            
            objTrans.doTransferAuthorize(transferTransList, transferTransParam);
            transferTransParam.put(CommonConstants.STATUS, status);                          
        } else {
            throw new TTException("Transfer List Is Empty");
        }
        transferTransList = null;
        transferTransParam = null;
    }
        
    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            if (tableDetails != null) {
                ArrayList addList = new ArrayList(tableDetails.keySet());
                MultipleStandingActDetailsTO objMultipleStandingActDetailsTO = null;                
                for (int i = 0; i < tableDetails.size(); i++) {
                    objMultipleStandingActDetailsTO = new MultipleStandingActDetailsTO();
                    objMultipleStandingActDetailsTO = (MultipleStandingActDetailsTO) tableDetails.get(addList.get(i));
                    objMultipleStandingActDetailsTO.setStatusDt((Date)CurrDt.clone());
                    objMultipleStandingActDetailsTO.setAuthorizedStatus(CommonConstants.STATUS_AUTHORIZED);
                    objMultipleStandingActDetailsTO.setAuthorizeBy(CommonUtil.convertObjToStr(objMultipleStandingActDetailsTO.getStatusBy()));
                    objMultipleStandingActDetailsTO.setAuthorizedDt((Date) CurrDt.clone());
                    objMultipleStandingActDetailsTO.setBranchId(_branchCode);
                    System.out.println("multipleStandingTO####" + objMultipleStandingActDetailsTO);
                    if (objMultipleStandingActDetailsTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {                        
                        sqlMap.executeUpdate("insertMultipleStandingActDetailsTO", objMultipleStandingActDetailsTO);
                    } else {
                        sqlMap.executeUpdate("updateMultipleStandingActDetailsTO", objMultipleStandingActDetailsTO);
                    }
                }
            }
            if(map.containsKey("SI_MASTER_DETAILS") && map.get("SI_MASTER_DETAILS") != null){ // Added by nithya
               MultiStandingMasterTO objMultiStandingMasterTO = (MultiStandingMasterTO)map.get("SI_MASTER_DETAILS");
               objMultiStandingMasterTO.setAuthorizedStatus(CommonConstants.STATUS_AUTHORIZED);
               objMultiStandingMasterTO.setAuthorizeBy(CommonUtil.convertObjToStr(objMultiStandingMasterTO.getStatusBy()));
               objMultiStandingMasterTO.setAuthorizeDt((Date) CurrDt.clone());
               objMultiStandingMasterTO.setBranchId(_branchCode);
               sqlMap.executeUpdate("updateMultipleStandingTO", objMultiStandingMasterTO);
               objMultiStandingMasterTO = null;
            }
            if (deletedTableValues != null) {
                System.out.println("deletedTableValues :: " + deletedTableValues);
                ArrayList addList = new ArrayList(deletedTableValues.keySet());
                MultipleStandingActDetailsTO multipleStandingTO = null;
                for (int i = 0; i < deletedTableValues.size(); i++) {
                    multipleStandingTO = new MultipleStandingActDetailsTO();
                    multipleStandingTO = (MultipleStandingActDetailsTO) deletedTableValues.get(addList.get(i));
                    sqlMap.executeUpdate("deleteMultipleStandingTO", multipleStandingTO);
                }
            }

            sqlMap.commitTransaction();
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
            map.put("STATUS", CommonConstants.STATUS_DELETED);
            map.put("STATUS_DT", CurrDt);
            sqlMap.executeUpdate("deleteMultipleStandingTO", map);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void authorize(HashMap map) throws Exception {
        System.out.println("######### map : " + map);
        String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        HashMap authMap = new HashMap();
        authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
        ArrayList selectedList = (ArrayList) authMap.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthorizeMap = new HashMap();
        AuthorizeMap = (HashMap) selectedList.get(0);
        String status = CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS));
        transferDAO = new TransferDAO();
        double totalExcessTransAmt = 0.0;
        if (map.containsKey("TransactionTO")) {
            TransactionTO transactionTO = new TransactionTO();
            HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
            LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
            if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
            }
            transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
            totalExcessTransAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt());
            String linkBatchId = "";
            if (transactionTO.getTransType().equals(CommonConstants.TX_TRANSFER)) {
                if (totalExcessTransAmt > 0) {
                    HashMap transferTransParam = new HashMap();
                    transferDAO = new TransferDAO();
                    transferTransParam.put(CommonConstants.BRANCH_ID, BRANCH_ID);
                    transferTransParam.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                    transferTransParam.put("DEBIT_LOAN_TYPE", "DP");
                    transferTransParam.put("LINK_BATCH_ID", AuthorizeMap.get("ACT_NUM"));
                    ArrayList transferTransList = (ArrayList) sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", transferTransParam);
                    if (transferTransList != null) {
                        String batchId = ((TxTransferTO) transferTransList.get(0)).getBatchId();
                        HashMap transAuthMap = new HashMap();
                        transAuthMap.put("BATCH_ID", batchId);
                        transAuthMap.put(CommonConstants.AUTHORIZESTATUS, status);
                        transAuthMap.put(CommonConstants.AUTHORIZEDATA, transferTransList);
                        transferTransParam.put(CommonConstants.AUTHORIZEMAP, transAuthMap);
                        transferDAO.execute(transferTransParam, false);
                    }
                }
            } else if (transactionTO.getTransType().equals(CommonConstants.TX_CASH)) {
                CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                ArrayList arrList = new ArrayList();
                HashMap authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
                HashMap singleAuthorizeMap = new HashMap();
                authorizeMap.put("LINK_BATCH_ID", AuthorizeMap.get("ACT_NUM"));
                authorizeMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                authorizeMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                authorizeMap.put("TRANS_DT", CurrDt);
                ArrayList cashTransList = (ArrayList) sqlMap.executeQueryForList("getCashTransactionTOForAuthorzation", authorizeMap);
                if (cashTransList != null) {
                    String transId = ((CashTransactionTO) cashTransList.get(0)).getTransId();
                    singleAuthorizeMap.put("AUTHORIZE_STATUS", status);
                    singleAuthorizeMap.put("STATUS", status);
                    singleAuthorizeMap.put("TRANS_ID", CommonUtil.convertObjToStr(transactionTO.getTransId()));
                    singleAuthorizeMap.put("USER_ID", map.get("USER_ID"));
                    singleAuthorizeMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                    singleAuthorizeMap.put("TRANS_ID", transId);
                    arrList.add(singleAuthorizeMap);
                    String branchCode = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
                    String userId = CommonUtil.convertObjToStr(map.get("USER_ID"));
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
                    map.put("DEBIT_LOAN_TYPE", "DP");
                    System.out.println("before entering DAO map :" + map);
                    cashTransactionDAO.execute(map, false);
                    cashTransactionDAO = null;
                    dataMap = null;
                }
            }

        }

    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = CommonUtil.convertObjToStr(obj.get(CommonConstants.BRANCH_ID));
        CurrDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private String getMaxSlNo(PayRollTo payRollTo) {
        HashMap slMap = new HashMap();
        String slNo = "";
        try {
            List slList = (List) sqlMap.executeQueryForList("getMaxSlNoFromPayRoll", payRollTo);
            if (slList != null && slList.size() > 0) {
                slMap = (HashMap) slList.get(0);
                slNo = CommonUtil.convertObjToStr(slMap.get("SL_NO"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return slNo;
    }

    private void destroyObjects() {
        standingTO = null;
        payrollMap = null;
        transactionDAO = null;
        //returnDataMap.clear();

    }
}
