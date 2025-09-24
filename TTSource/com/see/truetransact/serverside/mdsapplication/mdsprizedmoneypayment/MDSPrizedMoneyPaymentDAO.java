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
package com.see.truetransact.serverside.mdsapplication.mdsprizedmoneypayment;

import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.transferobject.product.operativeacct.*;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.transferobject.mdsapplication.mdsprizedmoneypayment.MDSPrizedMoneyPaymentTO;

import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.common.sms.SmsConfigDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.mdsapplication.mdsreceiptentry.MDSReceiptEntryTO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;

/**
 * This is used for OperativeAcctProductDAO Data Access.
 *
 * @author Balachandar
 *
 * @modified Pinky @modified Rahul
 */
public class MDSPrizedMoneyPaymentDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private MDSPrizedMoneyPaymentTO prizedMoneyPaymentTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private TransactionDAO transactionDAO = null;
    TransferDAO transferDAO = new TransferDAO();
    private final static Logger log = Logger.getLogger(MDSPrizedMoneyPaymentDAO.class);
    private Date currDt = null;
    Map transDetMap = null;
    boolean ExecptionDeftler = false;
    private String generateSingleTransId = "";
    private int ibrHierarchy = 0;
    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public MDSPrizedMoneyPaymentDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getDataNew(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSelectMDSPrizedMoneyPaymentTO", map);
        returnMap.put("MDSPrizedMoneyPaymentTO", list);

        // Get Trans_Details
        HashMap getRemitTransMap = new HashMap();
        getRemitTransMap.put("TRANS_ID", CommonUtil.convertObjToStr(map.get("TRANS_ID")));
        getRemitTransMap.put("TRANS_DT", currDt.clone());
        getRemitTransMap.put("BRANCH_CODE", _branchCode);
        List remitList = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", getRemitTransMap);
        if (remitList != null && remitList.size() > 0) {
            returnMap.put("TransactionTO", remitList);
        } else {
            MDSPrizedMoneyPaymentTO objMDSPrizedMoneyPaymentTO = (MDSPrizedMoneyPaymentTO) list.get(0);
            System.out.println("####### objMDSPrizedMoneyPaymentTO : " + objMDSPrizedMoneyPaymentTO);
            getRemitTransMap.put("TRANS_ID", CommonUtil.convertObjToStr(objMDSPrizedMoneyPaymentTO.getCashId()));
            remitList = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", getRemitTransMap);
            if (remitList != null && remitList.size() > 0) {
                returnMap.put("TransactionTO", remitList);
            }
        }
        remitList = null;
        System.out.println("RETURNMAP :" + returnMap);
        return returnMap;
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSelectMDSPrizedMoneyPaymentTO", map);
        returnMap.put("MDSPrizedMoneyPaymentTO", list);

        HashMap whereMap = new HashMap();
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);

        System.out.println("returnMap : " + returnMap);
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
        List lstEdit = sqlMap.executeQueryForList("getSelectApplnPaymentAmt", editMap);
        if (lstEdit != null && lstEdit.size() > 0) {
            editMap = (HashMap) lstEdit.get(0);
            double bonusAmount = CommonUtil.convertObjToDouble(editMap.get("BONUS_AMOUNT")).doubleValue();
            double commisionAmount = CommonUtil.convertObjToDouble(editMap.get("COMMISION_AMOUNT")).doubleValue();
            double discountAmount = CommonUtil.convertObjToDouble(editMap.get("DISCOUNT_AMOUNT")).doubleValue();
            double noticeAmount = CommonUtil.convertObjToDouble(editMap.get("NOTICE_AMOUNT")).doubleValue();
            double arbitrationAmount = CommonUtil.convertObjToDouble(editMap.get("ARIBITRATION_AMOUNT")).doubleValue();
            HashMap getTransMap = new HashMap();
            String transferMapName = "getTransferTransBatchID";
            String cashMapName = "getCashTransBatchID";
            if (map.containsKey("GET_TRANS_DATE")) {                              // Enquiry Mode Only
                HashMap dtMap = new HashMap();
                List Datelist = (List) sqlMap.executeQueryForList("getMDSPrizedPaymentStatusDate", map);
                if (Datelist != null && Datelist.size() > 0) {
                    dtMap = (HashMap) Datelist.get(0);
                    getTransMap.put("TODAY_DT", dtMap.get("STATUS_DT"));
                    System.out.println("########getTransMap :" + getTransMap);
                    transferMapName = "getTransferTransBatchIDForEnquiry";
                    cashMapName = "getCashTransBatchIDForEnquiry";
                }
            } else {
                getTransMap.put("TODAY_DT", currDt.clone());
            }
            getTransMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(_branchCode));
            System.out.println("########getTransferTransBatchID returnMap depCloseAmt:" + returnMap);
//            if (bonusAmount>0){
            getTransMap.put("LINK_BATCH_ID", editMap.get("TRANS_ID"));
            List lst = (List) sqlMap.executeQueryForList(transferMapName, getTransMap);
            if (lst != null && lst.size() > 0) {
                returnMap.put("BONUS_AMT_TRANSACTION_TRANSFER", lst.get(0));
                System.out.println("BONUS_AMT_TRANSACTION TRANSFER :" + returnMap);
                String transId = CommonUtil.convertObjToStr(editMap.get("TRANS_ID"));
                HashMap totalMap = new HashMap();
                whereMap.put(CommonConstants.TRANS_ID, transId);
                whereMap.put(CommonConstants.BRANCH_ID, _branchCode);
                whereMap.put("TRANS_DT", currDt);
                totalMap.put(CommonConstants.MAP_WHERE, whereMap);
                list = transactionDAO.getData(totalMap);
                returnMap.put("TransactionTO", list);
            }
            if (editMap.get("CASH_ID") != null && !CommonUtil.convertObjToStr(editMap.get("CASH_ID")).equals("")) {
                getTransMap.put("LINK_BATCH_ID", editMap.get("CASH_ID"));
                lst = (List) sqlMap.executeQueryForList(cashMapName, getTransMap);
                if (lst != null && lst.size() > 0) {
                    returnMap.put("BONUS_AMT_TRANSACTION_CASH", lst.get(0));
                    System.out.println("BONUS_AMT_TRANSACTION CASH :" + returnMap);
                    String transId = CommonUtil.convertObjToStr(editMap.get("CASH_ID"));
                    HashMap totalMap = new HashMap();
                    whereMap.put(CommonConstants.TRANS_ID, transId);
                    whereMap.put(CommonConstants.BRANCH_ID, _branchCode);
                    whereMap.put("TRANS_DT", currDt);
                    totalMap.put(CommonConstants.MAP_WHERE, whereMap);
                    list = transactionDAO.getData(totalMap);
                    returnMap.put("TransactionTO", list);
                }
            }
            lst = null;
//            }
        }
        System.out.println("RETURNMAP :" + returnMap);
        return returnMap;
    }

    public HashMap execute(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        try {

            ExecptionDeftler = false;
            System.out.println("Map in DAO: " + map);

            _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
            currDt = ServerUtil.getCurrentDate(_branchCode);
            logDAO = new LogDAO();
            logTO = new LogTO();
            prizedMoneyPaymentTO = (MDSPrizedMoneyPaymentTO) map.get("MDSPrizedMoneyPaymentData");
            final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
            ibrHierarchy = 1;
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData(map);
               System.out.println("transDetMap...." + transDetMap);
                if (transDetMap != null && transDetMap.size() > 0) {
                    returnMap.putAll(transDetMap);
                }
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData(map);
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData(map);
            } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
                HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
                authMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                authMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                if (authMap != null) {
//                authorize(authMap);
                    authorizeNew(map, authMap);
                }
            }
            map = null;
            destroyObjects();
            if (ExecptionDeftler) {
                returnMap.put("EXECPTION_DEF", "Y");
            } else {
                returnMap.put("EXECPTION_DEF", "N");
            }

        } catch (Exception e) {
            throw e;
        }
        return returnMap;
    }
	//added by chithra for service Tax
    private void insertServiceTaxDetails(ServiceTaxDetailsTO objserviceTaxDetailsTO) {
        try {
            HashMap mapDel = new HashMap();
            mapDel.put("ACCT_NUM", objserviceTaxDetailsTO.getAcct_Num());
            mapDel.put("CREATED_DT", objserviceTaxDetailsTO.getCreatedDt());
            sqlMap.executeUpdate("deleteServiceTaxDetails", mapDel);

            objserviceTaxDetailsTO.setServiceTaxDet_Id(getServiceTaxNo());
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
    private void insertData(HashMap map) throws Exception {
        try {           
            sqlMap.startTransaction();
            if (!prizedMoneyPaymentTO.getDefaulter_marked().equals("Y")) {
                insertNewTransactionData(map);
            } else {
                insertNewTransactionDataDefaulter(map);
            }
//            insertTransactionData();
            if(map.containsKey("serviceTaxDetailsTO")){
                ServiceTaxDetailsTO objserTax = (ServiceTaxDetailsTO) map.get("serviceTaxDetailsTO");
               insertServiceTaxDetails(objserTax);
            }
            prizedMoneyPaymentTO.setPaidStatus("N");
            sqlMap.executeUpdate("insertMDSPrizedMoneyPaymentTO", prizedMoneyPaymentTO);
            logTO.setData(prizedMoneyPaymentTO.toString());
            logTO.setPrimaryKey(prizedMoneyPaymentTO.getKeyData());
            logTO.setStatus(prizedMoneyPaymentTO.getStatus());
            logDAO.addToLog(logTO);
            System.out.println("ExecptionDeftler..." + ExecptionDeftler);
            if (ExecptionDeftler) {
                sqlMap.rollbackTransaction();

            } else {
                sqlMap.commitTransaction();
                //ExecptionDeftler=true;
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw e;
            //throw new TransRollbackException(e);
        }
        map = null;
    }

    private void insertNewTransactionDataDefaulter(HashMap map) throws Exception {
        try {
            
            String generateSingleTransId = generateLinkID();
            prizedMoneyPaymentTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
            prizedMoneyPaymentTO.setStatus(CommonConstants.STATUS_CREATED);
            System.out.println("###### insertTransactionData : " + map);
            ArrayList transferList = new ArrayList();
            TransferTrans transferTrans = new TransferTrans();
            TxTransferTO transferTo = new TxTransferTO();
            ArrayList TxTransferTO = new ArrayList();
            TransactionTO transactionTO = new TransactionTO();
            HashMap txMap = new HashMap();
            HashMap transactionListMap = new HashMap();
            String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
            double totalAmt = CommonUtil.convertObjToDouble(map.get("TOTAL_AMOUNT")).doubleValue();
            transferTrans.setInitiatedBranch(BRANCH_ID);
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            transactionDAO.setInitiatedBranch(_branchCode);
            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
//            if(map.containsKey("TransactionTO")) {
//                HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
//                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
//                if (transactionDetailsMap.size()>0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs"))
//                    allowedTransDetailsTO = (LinkedHashMap)transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
//                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
            HashMap applicationMap = new HashMap();
            applicationMap.put("SCHEME_NAME", prizedMoneyPaymentTO.getSchemeName());
            List lst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", applicationMap);
            if (lst != null && lst.size() > 0) {
                applicationMap = (HashMap) lst.get(0);
            }
//                if (transactionTO.getTransType().equals("TRANSFER") || transactionTO.getTransType().equals("CASH") ) {
            String linkBatchId = "";
//                    if(transactionTO.getDebitAcctNo()!=null && transactionTO.getDebitAcctNo().length()>0){
//                        linkBatchId = CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo());
//                    }
            HashMap transMap = new HashMap();
//                    HashMap operativeMap = new HashMap();
//                    String value = CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo());
//                    List lstOA = sqlMap.executeQueryForList("getAccountClosingHeads",value);
//                    if(lstOA!=null && lstOA.size()>0){
//                        operativeMap = (HashMap)lstOA.get(0);
//                    }
//                    HashMap debitMap = new HashMap();
//                    if(!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("OA")){
//                        debitMap.put("ACT_NUM", transactionTO.getDebitAcctNo());
//                        lst = sqlMap.executeQueryForList("getAccNoProdIdDet",debitMap);
//                        if(lst!=null && lst.size()>0){
//                            debitMap = (HashMap)lst.get(0);
//                        }
//                    }
            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
         //   System.out.println("###### insertTransactionData prizedMoneyPaymentTO : " + prizedMoneyPaymentTO);
//                    if (transactionTO.getTransType().equals("TRANSFER")){
            if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue()
                    + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue() > 0) {
                txMap = new HashMap();
                //Changed By Suresh
                if (map.containsKey("MDS_CLOSURE")) {
                    txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("MDS_PAYABLE_HEAD"));
                } else {
                    txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("PAYMENT_HEAD"));
                }
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " PAYMENT");
                txMap.put(TransferTrans.DR_BRANCH, map.get("SELECTED_BRANCH_ID"));

                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
//                System.out.println("txMap : " + txMap + "amt :" + (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue()
//                        + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue()));
//                System.out.println("dddeeefaulterrfor netr amttt" + prizedMoneyPaymentTO.getDefaulter_marked());
//                        if(!prizedMoneyPaymentTO.getDefaulter_marked().equals("Y"))
//                        {
                txMap.put("TRANS_MOD_TYPE", "MDS");
                transferTo = transactionDAO.addTransferDebitLocal(txMap, (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue()
                        + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue()
                        + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue()));
//                System.out.println("amountttt " + (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue()
//                        + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue()
//                        + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue()));
//                        }
				transferTo.setSingleTransId(generateSingleTransId);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setInitiatedBranch(BRANCH_ID);
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setSingleTransId(generateSingleTransId);
                //                            if(!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")){
                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                //                            }
                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                TxTransferTO.add(transferTo);
            }
//            System.out.println("transferTo List 1 : " + transferTo);
//            System.out.println("dddeeefaulterrfor netr amttt" + prizedMoneyPaymentTO.getDefaulter_marked());
//                        if(!prizedMoneyPaymentTO.getDefaulter_marked().equals("Y"))
//                        {
//                        if(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()).doubleValue()>0){
//                            transferTo = new TxTransferTO();
//                            txMap = new HashMap();
//                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                            if(!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL") && transactionTO.getProductId().equals("")){
//                                txMap.put(TransferTrans.CR_AC_HD, (String)transactionTO.getDebitAcctNo());
//                                txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//                            }else if(!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("OA")){
//                                txMap.put(TransferTrans.CR_AC_HD, (String)debitMap.get("AC_HD_ID"));
//                                txMap.put(TransferTrans.CR_ACT_NUM,transactionTO.getDebitAcctNo());
//                                txMap.put(TransferTrans.CR_PROD_ID,transactionTO.getProductId());
//                                txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.OPERATIVE);
//                            }
//                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo()+"_"+prizedMoneyPaymentTO.getSubNo());
//                            txMap.put(TransferTrans.CR_BRANCH,BRANCH_ID);
//                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
//                            txMap.put(TransferTrans.DR_INST_TYPE,"VOUCHER");
//                            //Added By Suresh
//                            if(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getDefaulters()).equals("Y") && CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue()>0){
//                                transferTo =  transactionDAO.addTransferCreditLocal(txMap, (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()).doubleValue() -
//                                CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue()));
//                            }else{
//                                transferTo =  transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()).doubleValue()) ;
//                            }
//                            transferTo.setTransId("-");
//                            transferTo.setBatchId("-");
//                            transferTo.setInitiatedBranch(BRANCH_ID);
//                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                            //                            if(!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")){
//                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
//                            //                            }
//                            System.out.println("transferTo List 2 : " + transferTo);
//                            TxTransferTO.add(transferTo);
//                            transactionTO.setChequeNo("SERVICE_TAX");
//                            TxTransferTO.add(transferTo);
//                        }
//                    }
           // System.out.println("transferTo List 1 : " + transferTo);

         //   System.out.println("transferTo List 2 : " + transferTo);
            if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue() > 0) {
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                //Changed By Suresh
                if (map.containsKey("MDS_CLOSURE")) {
                    txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("SUNDRY_RECEIPT_HEAD"));
                } else {
                    txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("BONUS_PAYABLE_HEAD"));
                }
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " BONUS");
                txMap.put(TransferTrans.CR_BRANCH,map.get("SELECTED_BRANCH_ID"));
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                txMap.put("TRANS_MOD_TYPE", "MDS");
             //   System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue());
                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue());
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setInitiatedBranch(BRANCH_ID);
				transferTo.setSingleTransId(generateSingleTransId);
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
              //  System.out.println("transferTo List 2 : " + transferTo);
                TxTransferTO.add(transferTo);
                transactionTO.setChequeNo("SERVICE_TAX");
                transferTo.setSingleTransId(generateSingleTransId);
                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                TxTransferTO.add(transferTo);
            }
       //     System.out.println("transferTo List 3 : " + transferTo);
//                        if(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNoticeAmount()).doubleValue()>0){
//                            transferTo = new TxTransferTO();
//                            txMap = new HashMap();
//                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                            txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("NOTICE_CHARGES_HEAD"));
//                            txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo()+"_"+prizedMoneyPaymentTO.getSubNo()+" NOTICE");
//                            txMap.put(TransferTrans.CR_BRANCH,BRANCH_ID);
//                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
//                            txMap.put(TransferTrans.DR_INST_TYPE,"VOUCHER");
//                            System.out.println("txMap : " + txMap+"serviceAmt :"+CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNoticeAmount()).doubleValue());
//                            transferTo =  transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNoticeAmount()).doubleValue());
//                            transferTo.setTransId("-");
//                            transferTo.setBatchId("-");
//                            transferTo.setInitiatedBranch(BRANCH_ID);
//                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
//                            TxTransferTO.add(transferTo);
//                        }
//                        if(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getChargeAmount()).doubleValue()>0){
//                            transferTo = new TxTransferTO();
//                            txMap = new HashMap();
//                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                            txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("CHARGE_PAYMENT_HEAD"));
//                            txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo()+"_"+prizedMoneyPaymentTO.getSubNo()+" CHARGE");
//                            txMap.put(TransferTrans.CR_BRANCH,BRANCH_ID);
//                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
//                            txMap.put(TransferTrans.DR_INST_TYPE,"VOUCHER");
//                            System.out.println("txMap : " + txMap+"serviceAmt :"+CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getChargeAmount()).doubleValue());
//                            transferTo =  transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getChargeAmount()).doubleValue());
//                            transferTo.setTransId("-");
//                            transferTo.setBatchId("-");
//                            transferTo.setInitiatedBranch(BRANCH_ID);
//                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
//                            TxTransferTO.add(transferTo);
//                        }
         //   System.out.println("transferTo List 4 : " + transferTo);
//                        //Case Expense Amount Transaction
//                        if(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getAribitrationAmount()).doubleValue()>0){
//                            System.out.println("Arbitration Started");
//                            HashMap whereMap = new HashMap();
//                            List chargeList = null;
//                            whereMap.put("ACT_NUM",CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo())+"_"+CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getSubNo()));
//                            chargeList = (List) sqlMap.executeQueryForList("getMDSCaseChargeDetails", whereMap);
//                            if(chargeList!=null && chargeList.size()>0){
//                                for(int i=0;i<chargeList.size();i++){
//                                    double chargeAmount =0.0;
//                                    String chargeType ="";
//                                    whereMap=(HashMap) chargeList.get(i);
//                                    chargeAmount = CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
//                                    Rounding rod =new Rounding();
//                                    chargeAmount = (double)rod.getNearest((long)(chargeAmount *100),100)/100;
//                                    chargeType = CommonUtil.convertObjToStr(whereMap.get("CHARGE_TYPE"));
//                                    if(chargeAmount>0){
//                            transferTo = new TxTransferTO();
//                            txMap = new HashMap();
//                                        if(chargeType.equals("ARC Cost")){
//                                            chargeType = "ARC_COST";
//                                        }else if(chargeType.equals("ARC Expense")){
//                                            chargeType = "ARC_EXPENSE";
//                                        }else if(chargeType.equals("EA Cost")){
//                                            chargeType = "EA_COST";
//                                        }else if(chargeType.equals("EA Expense")){
//                                            chargeType = "EA_EXPENSE";
//                                        }else if(chargeType.equals("EP Cost")){
//                                            chargeType = "EP_COST";
//                                        }else if(chargeType.equals("EP Expense")){
//                                            chargeType = "EP_EXPENSE";
//                                        }
//                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                                        System.out.println("###### chargeType Head : "+chargeType);
//                                        System.out.println("###### chargeAmount : "+chargeAmount);
//                                        //txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("CASE_EXPENSE_HEAD"));
//                                        txMap.put(TransferTrans.CR_AC_HD, applicationMap.get(chargeType));
//                            txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//                                        txMap.put(TransferTrans.CR_BRANCH,prizedMoneyPaymentTO.getBranchCode()); //BRANCH_ID
//                                        txMap.put(TransferTrans.PARTICULARS,chargeType+applicationMap.get(" MP_MDS_CODE")+"-" + prizedMoneyPaymentTO.getChittalNo()+"_"+prizedMoneyPaymentTO.getSubNo());
//                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
//                            txMap.put(TransferTrans.DR_INST_TYPE,"VOUCHER");
//                                        transferTo =  transactionDAO.addTransferCreditLocal(txMap, chargeAmount) ;
//                            transferTo.setTransId("-");
//                            transferTo.setBatchId("-");
//                                        transferTo.setTransDt(currDt);
//                            transferTo.setInitiatedBranch(BRANCH_ID);
//                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
//                            transactionTO.setChequeNo("SERVICE_TAX");
//                                        TxTransferTO.add(transferTo);
//                                        System.out.println("Arbitration transferTo List  : "+i+" "+ transferTo);
//                                    }
//                                }
//                            }
//                            //                            transferTo = new TxTransferTO();
//                            //                            txMap = new HashMap();
//                            //                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                            //                            txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//                            //                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("CASE_EXPENSE_HEAD"));
//                            //                            txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//                            //                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo()+"_"+prizedMoneyPaymentTO.getSubNo()+" CASE EXPENSE");
//                            //                            txMap.put(TransferTrans.CR_BRANCH,BRANCH_ID);
//                            //                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
//                            //                            txMap.put(TransferTrans.DR_INST_TYPE,"VOUCHER");
//                            //                            System.out.println("txMap : " + txMap+"serviceAmt :"+CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getAribitrationAmount()).doubleValue());
//                            //                            transferTo =  transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getAribitrationAmount()).doubleValue()) ;
//                            //                            transferTo.setTransId("-");
//                            //                            transferTo.setBatchId("-");
//                            //                            transferTo.setInitiatedBranch(BRANCH_ID);
//                            //                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                            //                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                            //                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
//                            //                            TxTransferTO.add(transferTo);
//                            //                            transactionTO.setChequeNo("SERVICE_TAX");
//                        }
           // System.out.println("transferTo List 5 : " + transferTo);
//                        if(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getDiscountAmount()).doubleValue()>0){
//                            transferTo = new TxTransferTO();
//                            txMap = new HashMap();
//                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                            txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("DISCOUNT_HEAD"));
//                            txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo()+"_"+prizedMoneyPaymentTO.getSubNo()+" DISCOUNT");
//                            txMap.put(TransferTrans.CR_BRANCH,BRANCH_ID);
//                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
//                            txMap.put(TransferTrans.DR_INST_TYPE,"VOUCHER");
//                            System.out.println("txMap : " + txMap+"serviceAmt :"+CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getDiscountAmount()).doubleValue());
//                            transferTo =  transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getDiscountAmount()).doubleValue()) ;
//                            transferTo.setTransId("-");
//                            transferTo.setBatchId("-");
//                            transferTo.setInitiatedBranch(BRANCH_ID);
//                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
//                            TxTransferTO.add(transferTo);
//                            transactionTO.setChequeNo("SERVICE_TAX");
//                        }

            //System.out.println("transferTo List 5 : " + transferTo);
            //Added By Suresh
//                        if(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getDefaulters()).equals("Y")){
//                            if(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPenalAmount()).doubleValue()>0){
//                                transferTo = new TxTransferTO();
//                                txMap = new HashMap();
//                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                                txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("PENAL_INTEREST_HEAD"));
//                                txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo()+"_"+prizedMoneyPaymentTO.getSubNo()+" PENAL");
//                                txMap.put(TransferTrans.CR_BRANCH,BRANCH_ID);
//                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
//                                txMap.put(TransferTrans.DR_INST_TYPE,"VOUCHER");
//                                System.out.println("txMap : " + txMap+"serviceAmt :"+CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPenalAmount()).doubleValue());
//                                transferTo =  transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPenalAmount()).doubleValue()) ;
//                                transferTo.setTransId("-");
//                                transferTo.setBatchId("-");
//                                transferTo.setInitiatedBranch(BRANCH_ID);
//                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
//                                TxTransferTO.add(transferTo);
//                                transactionTO.setChequeNo("SERVICE_TAX");
//                            }
//                            
////                            if(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue()>0){
////                                transferTo = new TxTransferTO();
////                                txMap = new HashMap();
////                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
////                                txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
////                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
////                                txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
////                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo()+"_"+prizedMoneyPaymentTO.getSubNo()+" OVERDUE");
////                                txMap.put(TransferTrans.CR_BRANCH,BRANCH_ID);
////                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
////                                txMap.put(TransferTrans.DR_INST_TYPE,"VOUCHER");
////                                System.out.println("txMap : " + txMap+"serviceAmt :"+CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue());
////                                transferTo =  transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue()) ;
////                                transferTo.setTransId("-");
////                                transferTo.setBatchId("-");
////                                transferTo.setInitiatedBranch(BRANCH_ID);
////                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
////                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
////                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
////                                TxTransferTO.add(transferTo);
////                                transactionTO.setChequeNo("SERVICE_TAX");
////                            }
//                        }

//                       //DEFAULTER


            //System.out.println("dddeeefaulterrr" + prizedMoneyPaymentTO.getDefaulter_marked());
            if (prizedMoneyPaymentTO.getDefaulter_marked().equals("Y")) {
                HashMap account_map = new HashMap();
                account_map.put("CHITTAL_NO", prizedMoneyPaymentTO.getChittalNo());
                List defaulter_acc_head = sqlMap.executeQueryForList("getAccounthead", account_map);
                String bonusAcountHead = "";
                String commissionAcountHead = "";
                String interstAccountHead = "";
                String RecieptAccountHead = "";
                double defaulter_Inteset = 0.0;
                double defaulter_bonus_recoverd = 0.0;
                double defaulter_comm = 0.0;
                String SuspensetAccountHead = "";
                String Misclens = "";

                if (defaulter_acc_head != null && defaulter_acc_head.size() > 0) {
                    account_map = (HashMap) defaulter_acc_head.get(0);
                    bonusAcountHead = account_map.get("BONUS_RECEIVABLE_HEAD").toString();
                    commissionAcountHead = account_map.get("COMMISION_HEAD").toString();
                    interstAccountHead = account_map.get("PENAL_INTEREST_HEAD").toString();
                    RecieptAccountHead = account_map.get("RECEIPT_HEAD").toString();
                    SuspensetAccountHead = account_map.get("SUSPENSE_HEAD").toString();
                    Misclens = account_map.get("MISCELLANEOUS_HEAD").toString();
                }
                if (prizedMoneyPaymentTO.getDefaulter_bonus_recoverd() != null && prizedMoneyPaymentTO.getDefaulter_bonus_recoverd() > 0.0) {  //AJITH Removed Double.parseDouble()
                    defaulter_bonus_recoverd = prizedMoneyPaymentTO.getDefaulter_bonus_recoverd();  //AJITH Removed Double.parseDouble()
//                                transferTo = new TxTransferTO();
//                                txMap = new HashMap();
//                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                                txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//                                txMap.put(TransferTrans.CR_AC_HD, bonusAcountHead);
//                                txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo()+"_"+prizedMoneyPaymentTO.getSubNo()+" OVERDUE");
//                                txMap.put(TransferTrans.CR_BRANCH,BRANCH_ID);
//                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
//                                txMap.put(TransferTrans.DR_INST_TYPE,"VOUCHER");
//                                System.out.println("txMap : " + txMap+"serviceAmt :"+CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue());
//                                transferTo =  transactionDAO.addTransferCreditLocal(txMap, defaulter_bonus_recoverd) ;
//                                transferTo.setTransId("-");
//                                transferTo.setBatchId("-");
//                                transferTo.setInitiatedBranch(BRANCH_ID);
//                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
//                                TxTransferTO.add(transferTo);
//                                transactionTO.setChequeNo("SERVICE_TAX");
                }
                if (prizedMoneyPaymentTO.getDefaulter_comm() != null && prizedMoneyPaymentTO.getDefaulter_comm() > 0.0) {
                    defaulter_comm = prizedMoneyPaymentTO.getDefaulter_comm();  //AJITH Removed Double.parseDouble()
              //      System.out.println("commission.." + defaulter_comm);
                    transferTo = new TxTransferTO();
                    txMap = new HashMap();
                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.CR_AC_HD, Misclens);
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " OVERDUE");
                    txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                //    System.out.println("txMap ccc: " + txMap + "serviceAmtcccc :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue());
                    txMap.put("TRANS_MOD_TYPE", "MDS");
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, defaulter_comm);
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setInitiatedBranch(BRANCH_ID);
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                    transferTo.setSingleTransId(generateSingleTransId);
                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                    TxTransferTO.add(transferTo);
                    transactionTO.setChequeNo("SERVICE_TAX");
                }
                if (prizedMoneyPaymentTO.getDefaulter_interst() != null && prizedMoneyPaymentTO.getDefaulter_interst() > 0.0) {
                    defaulter_Inteset = prizedMoneyPaymentTO.getDefaulter_interst();  //AJITH Removed Double.parseDouble()
                    transferTo = new TxTransferTO();
                    txMap = new HashMap();
                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.CR_AC_HD, interstAccountHead);
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " OVERDUE");
                    txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                  //  System.out.println("txMap iii: " + txMap + "serviceAmtiiii :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue());
                    txMap.put("TRANS_MOD_TYPE", "MDS");
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, defaulter_Inteset);
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setInitiatedBranch(BRANCH_ID);
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                    transferTo.setSingleTransId(generateSingleTransId);
                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                    TxTransferTO.add(transferTo);
                    transactionTO.setChequeNo("SERVICE_TAX");
                }


                // System.out.print("###### cashList: "+cashList);
                double paidamount = 0.0;
                HashMap mdsMap = new HashMap();
                mdsMap.put("CHITTAL_NO", prizedMoneyPaymentTO.getChittalNo());
                mdsMap.put("SUB_NO", CommonUtil.convertObjToInt(prizedMoneyPaymentTO.getSubNo()));

                List getpaidAmounts = sqlMap.executeQueryForList("getMdsPaidAmount", mdsMap);
                if (getpaidAmounts != null && getpaidAmounts.size() > 0) {
                    mdsMap = new HashMap();
                    mdsMap = (HashMap) getpaidAmounts.get(0);
                    if (mdsMap != null && mdsMap.containsKey("TOTAL") && mdsMap.get("TOTAL") != null) {

                        paidamount = Double.parseDouble(mdsMap.get("TOTAL").toString());
                    } else {
                        paidamount = 0.0;
                    }
                } else {
                    paidamount = 0.0;
                }
//                        double d=Double.parseDouble(prizedMoneyPaymentTO.getPrizedAmount());
//                            System.out.println("d....."+d+"::"+paidamount);
//                        double amountToReciept=d-paidamount;
//                        

                double TotDet = CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue()
                        + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue()
                        + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue();
             //   System.out.println("d....." + TotDet + "::" + paidamount);
                double amountToReciept = TotDet - paidamount;
              //  System.out.println("TotDetawsad..." + TotDet);
                double bonusTogive = 0.0;
                HashMap mdsbonusMap = new HashMap();
                mdsbonusMap.put("CHITTAL_NO", prizedMoneyPaymentTO.getChittalNo());
                mdsbonusMap.put("SUB_NO", CommonUtil.convertObjToInt(prizedMoneyPaymentTO.getSubNo()));
                mdsbonusMap.put("SCHME_NAME", prizedMoneyPaymentTO.getSchemeName());

                List payableBons = sqlMap.executeQueryForList("getPayableBonus", mdsbonusMap);
                if (payableBons != null && payableBons.size() > 0) {
                    HashMap mapDts = new HashMap();
                    mapDts = (HashMap) payableBons.get(0);
                    if (mapDts != null) {
                        bonusTogive = Double.parseDouble(mapDts.get("BONUS_PAYABLE").toString());

                    } else {
                        bonusTogive = 0.0;
                    }
                } else {
                    bonusTogive = 0.0;
                }
                if (bonusTogive > 0.0) {
                    transferTo = new TxTransferTO();
                    txMap = new HashMap();
                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.DR_AC_HD, bonusAcountHead);
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " OVERDUE");
                    txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                 //   System.out.println("txMap bbfff: " + txMap + "serviceAmt bbtff:" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue());
                    txMap.put("TRANS_MOD_TYPE", "MDS");
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, bonusTogive);
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setInitiatedBranch(BRANCH_ID);
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                    transferTo.setSingleTransId(generateSingleTransId);
                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                    TxTransferTO.add(transferTo);
                    transactionTO.setChequeNo("SERVICE_TAX");
                }
                if (amountToReciept > 0.0) {
                    // defaulter_Inteset=Double.parseDouble(prizedMoneyPaymentTO.getDefaulter_interst());
                    transferTo = new TxTransferTO();
                    txMap = new HashMap();
                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.CR_AC_HD, RecieptAccountHead);
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " OVERDUE");
                    txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                 //   System.out.println("txMap iii: " + txMap + "serviceAmtiiii :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue());
                    txMap.put("TRANS_MOD_TYPE", "MDS");
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, amountToReciept);
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setInitiatedBranch(BRANCH_ID);
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                    transferTo.setSingleTransId(generateSingleTransId);
                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                    TxTransferTO.add(transferTo);
                    transactionTO.setChequeNo("SERVICE_TAX");
//                                transferTo = new TxTransferTO();
//                                txMap = new HashMap();
//                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                                txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//                                txMap.put(TransferTrans.CR_AC_HD, RecieptAccountHead);
//                                txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo()+"_"+prizedMoneyPaymentTO.getSubNo()+" OVERDUE");
//                                txMap.put(TransferTrans.CR_BRANCH,BRANCH_ID);
//                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
//                                txMap.put(TransferTrans.DR_INST_TYPE,"VOUCHER");
//                                System.out.println("txMap amount to aed : " + txMap+"serviceAmt amt :"+CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue());
//                                transferTo =  transactionDAO.addTransferCreditLocal(txMap, amountToReciept) ;
//                                transferTo.setTransId("-");
//                                transferTo.setBatchId("-");
//                                transferTo.setInitiatedBranch(BRANCH_ID);
//                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
//                                TxTransferTO.add(transferTo);
//                                transactionTO.setChequeNo("SERVICE_TAX");
                }
                double chittalPaid = paidamount;
                paidamount = paidamount - (defaulter_Inteset + defaulter_bonus_recoverd + defaulter_comm);
                //    paidamount=paidamount-( defaulter_Inteset+defaulter_comm);
//                              paidamount=paidamount-( defaulter_Inteset+defaulter_bonus_recoverd+defaulter_comm);
                if (paidamount > 0.0) {
                    transferTo = new TxTransferTO();
                    txMap = new HashMap();
                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.CR_AC_HD, SuspensetAccountHead);
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " OVERDUE");
                    txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
             //       System.out.println("txMap iii: " + txMap + "serviceAmtiiii :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue());
                    txMap.put("TRANS_MOD_TYPE", "MDS");
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, paidamount);
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setInitiatedBranch(BRANCH_ID);
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                    transferTo.setSingleTransId(generateSingleTransId);
                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                    TxTransferTO.add(transferTo);
                    transactionTO.setChequeNo("SERVICE_TAX");
//                                transferTo = new TxTransferTO();
//                                txMap = new HashMap();
//                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                                txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//                                txMap.put(TransferTrans.CR_AC_HD, SuspensetAccountHead);
//                                txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo()+"_"+prizedMoneyPaymentTO.getSubNo()+" OVERDUE");
//                                txMap.put(TransferTrans.CR_BRANCH,BRANCH_ID);
//                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
//                                txMap.put(TransferTrans.DR_INST_TYPE,"VOUCHER");
//                                System.out.println("txMap : " + txMap+"serviceAmt :"+CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue());
//                                transferTo =  transactionDAO.addTransferCreditLocal(txMap, paidamount) ;
//                                transferTo.setTransId("-");
//                                transferTo.setBatchId("-");
//                                transferTo.setInitiatedBranch(BRANCH_ID);
//                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
//                                TxTransferTO.add(transferTo);
//                                transactionTO.setChequeNo("SERVICE_TAX");
                }

                TotDet = TotDet + bonusTogive;
                double NtMislns = TotDet - (amountToReciept + paidamount + defaulter_Inteset + defaulter_comm + prizedMoneyPaymentTO.getCommisionAmount() + prizedMoneyPaymentTO.getBonusAmount());  //AJITH Removed Double.parseDouble()
                //Math.abs(TotDet);
                //  ddd
             //   System.out.println("NtMislns..." + NtMislns);
                boolean extraCreditFlag = false;
               // System.out.println("extraCreditFlag..." + extraCreditFlag);
                if (NtMislns > 0.0) {
                    transferTo = new TxTransferTO();
                    txMap = new HashMap();
                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.CR_AC_HD, Misclens);
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " OVERDUE");
                    txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                    System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue());
                    txMap.put("TRANS_MOD_TYPE", "MDS");
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, NtMislns);
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setInitiatedBranch(BRANCH_ID);
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                    transferTo.setSingleTransId(generateSingleTransId);
                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                    TxTransferTO.add(transferTo);
                    transactionTO.setChequeNo("SERVICE_TAX");

                } else if (NtMislns < 0.0) {
                    double extraCreditamt = Math.abs(NtMislns);
                //    System.out.println("ExtraCredit Amount=" + extraCreditamt);
                    double MdsComm = CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue();
                  //  System.out.println("MdsComm=" + MdsComm);
                    double mc = MdsComm;
                    mc = mc - extraCreditamt;
                  //  System.out.println("new Commission=" + mc);
                    extraCreditFlag = true;
                  //  System.out.println("extraCreditFlag..nnn." + extraCreditFlag);
                    if (mc > 0.0) {
                        // mc=Math.round(mc);
                        //  System.out.println("mc rounded");
                        transferTo = new TxTransferTO();
                        txMap = new HashMap();
                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("COMMISION_HEAD"));
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " COMMISION");
                        txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                    //    System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue());
                        txMap.put("TRANS_MOD_TYPE", "MDS");
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, mc);
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setInitiatedBranch(BRANCH_ID);
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                        TxTransferTO.add(transferTo);
                        transactionTO.setChequeNo("SERVICE_TAX");
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                        TxTransferTO.add(transferTo);
						//added by chithra for service Tax
                        if (map.containsKey("serviceTaxDetails")) {
                            System.out.println("serviceTaxDetails 1");
                            HashMap serTaxMap = (HashMap) map.get("serviceTaxDetails");
                            transferTo = new TxTransferTO();
                            double swachhCess = 0.0;
                            double krishikalyanCess = 0.0;
                            double serTaxAmt = 0.0;
                            double normalServiceTax = 0.0;
                            if (serTaxMap.containsKey("TOT_TAX_AMT") && serTaxMap.get("TOT_TAX_AMT") != null) {
                                serTaxAmt = CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT"));
                            }
                            if (serTaxMap.containsKey(ServiceTaxCalculation.SWACHH_CESS) && serTaxMap.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                swachhCess = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.SWACHH_CESS));
                            }
                            if (serTaxMap.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && serTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                krishikalyanCess = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                            }
                            if (serTaxMap.containsKey(ServiceTaxCalculation.SERVICE_TAX) && serTaxMap.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                normalServiceTax = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.SERVICE_TAX));
                            }
                           // serTaxAmt -= (swachhCess + krishikalyanCess);
                            if (swachhCess > 0) {
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_AC_HD, serTaxMap.get("SWACHH_HEAD_ID"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " CGST");
                                txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, swachhCess);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("CGST");
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                                //serTaxAmt -= swachhCess;
                            }
                            if (krishikalyanCess > 0) {
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_AC_HD, serTaxMap.get("KRISHIKALYAN_HEAD_ID"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " SGST");
                                txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, krishikalyanCess);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SGST");
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                               // serTaxAmt -= krishikalyanCess;
                            }
                            if (normalServiceTax > 0) {
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_AC_HD, serTaxMap.get("TAX_HEAD_ID"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " SERVICE TAX");
                                txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                            }
                        }
                    }
                    // ExecptionDeftler=true;
                    // throw new TTException("Cannot make Defaulter Payment !!!");
                }
                //System.out.println("extraCreditFlag..out." + extraCreditFlag);
                if (!extraCreditFlag) {
                    if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue() > 0) {
                        transferTo = new TxTransferTO();
                        txMap = new HashMap();
                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("COMMISION_HEAD"));
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " COMMISION");
                        txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                     //   System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue());
                        txMap.put("TRANS_MOD_TYPE", "MDS");
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue());
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setInitiatedBranch(BRANCH_ID);
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                        TxTransferTO.add(transferTo);
                        transactionTO.setChequeNo("SERVICE_TAX");
                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                        TxTransferTO.add(transferTo);
						//added by chithra for service Tax
                        if (map.containsKey("serviceTaxDetails")) {
                            System.out.println("serviceTaxDetails 2");
                            HashMap serTaxMap = (HashMap) map.get("serviceTaxDetails");
                            transferTo = new TxTransferTO();                            
                            double swachhCess = 0.0;
                            double krishikalyanCess = 0.0;
                            double serTaxAmt = 0.0;
                            double normalServiceTax = 0.0;
                            if (serTaxMap.containsKey("TOT_TAX_AMT") && serTaxMap.get("TOT_TAX_AMT") != null) {
                                serTaxAmt = CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT"));
                            }
                            if (serTaxMap.containsKey(ServiceTaxCalculation.SWACHH_CESS) && serTaxMap.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                swachhCess = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.SWACHH_CESS));
                            }
                            if (serTaxMap.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && serTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                krishikalyanCess = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                            }
                             if (serTaxMap.containsKey(ServiceTaxCalculation.SERVICE_TAX) && serTaxMap.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                normalServiceTax = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.SERVICE_TAX));
                            }
                          //  serTaxAmt -= (swachhCess + krishikalyanCess);
                            if (swachhCess > 0) {
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_AC_HD, serTaxMap.get("SWACHH_HEAD_ID"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " CGST");
                                txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, swachhCess);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("CGST");
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                               // serTaxAmt -= swachhCess;
                            }
                            if (krishikalyanCess > 0) {
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_AC_HD, serTaxMap.get("KRISHIKALYAN_HEAD_ID"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " SGST");
                                txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, krishikalyanCess);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SGST");
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                                //serTaxAmt -= krishikalyanCess;
                            }
                            if (normalServiceTax > 0) {
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_AC_HD, serTaxMap.get("TAX_HEAD_ID"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " SERVICE TAX");
                                txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                            }
                        }
                    }
                }
            }
            ////DEFAULTER
////                        // Changed By Suresh
////                        if(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getRefundAmount()).doubleValue()>0){   //Cr Refund_Amount
////                            System.out.print("####### Refund Amount : " +prizedMoneyPaymentTO.getRefundAmount());
////                            transferTo = new TxTransferTO();
////                            txMap = new HashMap();
////                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
////                            if(CommonUtil.convertObjToStr(applicationMap.get("SUSPENSE_GL_ACCNO")).equals("GL")){
////                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("SUSPENSE_HEAD"));
////                            txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
////                            }else{
////                                HashMap suspenseMap = new HashMap();                // Suspense Acc Head
////                                suspenseMap.put("PROD_ID",applicationMap.get("SUSPENSE_PROD_ID"));
////                                List suspenseLst = sqlMap.executeQueryForList("getAccountHeadProdSA",suspenseMap);
////                                if(suspenseLst!=null && suspenseLst.size()>0){
////                                    suspenseMap = (HashMap)suspenseLst.get(0);
////                                    txMap.put(TransferTrans.CR_AC_HD, suspenseMap.get("AC_HEAD"));
////                                    txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.SUSPENSE);
////                                    txMap.put(TransferTrans.CR_PROD_ID, applicationMap.get("SUSPENSE_PROD_ID"));
////                                    txMap.put(TransferTrans.CR_ACT_NUM, applicationMap.get("SUSPENSE_ACC_NO"));
////                                }
////                            }
////                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo()+"_"+prizedMoneyPaymentTO.getSubNo()+" REFUND");
////                            txMap.put(TransferTrans.CR_BRANCH,BRANCH_ID);
////                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
////                            txMap.put(TransferTrans.DR_INST_TYPE,"VOUCHER");
////                            System.out.println("txMap : " + txMap+"serviceAmt :"+CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getRefundAmount()).doubleValue());
////                            transferTo =  transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getRefundAmount()).doubleValue()) ;
////                            transferTo.setTransId("-");
////                            transferTo.setBatchId("-");
////                            transferTo.setInitiatedBranch(BRANCH_ID);
////                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
////                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
////                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
////                            TxTransferTO.add(transferTo);
////                            transactionTO.setChequeNo("SERVICE_TAX");
////                        }
//                        System.out.println("####### $$$$$$ "+map.get("REFUND_AMOUNT")+"dsfdfdsf"+map.containsKey("REFUND_AMOUNT"));
//                        if(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getRefundAmount()).doubleValue()>0 || map.containsKey("REFUND_AMOUNT")){  //Cr Reverse_Bonus_Amount
//                            double earnedBonus = 0.0;
//                            if(applicationMap.get("BONUS_EXISTING_CHITTAL")!=null &&!applicationMap.get("BONUS_EXISTING_CHITTAL").equals("") &&(applicationMap.get("BONUS_EXISTING_CHITTAL").equals("Y"))){
//                                HashMap hashMap = new HashMap();
//                                hashMap.put("CHITTAL_NO",prizedMoneyPaymentTO.getChittalNo());
//                                List paidList = sqlMap.executeQueryForList("getTotalInstAmount", hashMap);
//                                if(paidList!=null && paidList.size()>0){
//                                    hashMap = (HashMap)paidList.get(0);
//                                    earnedBonus = CommonUtil.convertObjToDouble(hashMap.get("PAID_AMT")).doubleValue() - CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getRefundAmount()).doubleValue();
//                                }
//                                if(earnedBonus>0){
//                                    System.out.print("####### Reverse Bonus Amount : " +earnedBonus);
//                                    transferTo = new TxTransferTO();
//                                    txMap = new HashMap();
//                                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                                    txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//                                    //Changed By Suresh
//                                    if(map.containsKey("MDS_CLOSURE")){
//                                        txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("SUNDRY_RECEIPT_HEAD"));
//                                    }else{
//                                        txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("BONUS_PAYABLE_HEAD"));
//                                    }
//                                    txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//                                    txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo()+"_"+prizedMoneyPaymentTO.getSubNo()+" REVERSE_BONUS");
//                                    txMap.put(TransferTrans.CR_BRANCH,BRANCH_ID);
//                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
//                                    txMap.put(TransferTrans.DR_INST_TYPE,"VOUCHER");
//                                    transferTo =  transactionDAO.addTransferCreditLocal(txMap, earnedBonus) ;
//                                    transferTo.setTransId("-");
//                                    transferTo.setBatchId("-");
//                                    transferTo.setInitiatedBranch(BRANCH_ID);
//                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
//                                    TxTransferTO.add(transferTo);
//                                    transactionTO.setChequeNo("SERVICE_TAX");
//                                }
//                            }
//                        }
            //System.out.println("dddd");
            HashMap applnMap = new HashMap();
            transferDAO = new TransferDAO();
            map.put("MODE", map.get("COMMAND"));
            map.put("COMMAND", map.get("MODE"));
            map.put("TxTransferTO", TxTransferTO);
          //  System.out.println("reckjnjfgnfrtdd hererer" + map);
//                         HashMap authorizeMap = new HashMap();
//                          authorizeMap.put("BATCH_ID", null);
//                          authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
//                          map.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
////                        
            transMap = transferDAO.execute(map, false);
            prizedMoneyPaymentTO.setTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
           // System.out.println("transactionDAO map : " + map);
            transactionDAO.setBatchId(prizedMoneyPaymentTO.getTransId());
            transactionDAO.setBatchDate(currDt);
            transactionDAO.execute(map);
            HashMap linkBatchMap = new HashMap();
            if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")
                    && transactionTO.getProductId().equals("") && !map.containsKey("LOCKER_SURRENDER_DAO")) {
                linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
            } else if (!map.containsKey("LOCKER_SURRENDER_DAO")) {
                linkBatchMap.put("LINK_BATCH_ID", transactionTO.getDebitAcctNo());
            } else {
                linkBatchMap.put("LINK_BATCH_ID", map.get("LOCKER_SURRENDER_ID"));
            }
            linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
            linkBatchMap.put("BATCH_ID", transMap.get("TRANS_ID"));
            linkBatchMap.put("TRANS_DT", currDt);
            linkBatchMap.put("INITIATED_BRANCH", _branchCode);
            sqlMap.executeUpdate("updateLinkBatchIdTransfer", linkBatchMap);
            linkBatchMap = null;
            transMap = null;
            //  }
            //  }
            // }
            String USERID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
            //    sqlMap.executeUpdate("insertReceiptEntryTO", prizedMoneyPaymentTO);
            //objLogDAO.addToLog(objLogTO);
            if (prizedMoneyPaymentTO.getTransId() != null && prizedMoneyPaymentTO.getTransId().length() > 0) {
                getTransNewDetails(prizedMoneyPaymentTO.getTransId(), prizedMoneyPaymentTO.getCashId(),map);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            if (e instanceof TTException) {
            } else {
                e.printStackTrace();
            }
            throw e;
            //  throw new TransRollbackException(e);
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

    //Added By Suresh
    private void insertNewTransactionData(HashMap map) throws Exception {
        try {
            //String generateSingleTransId = "";
            String defaulterBonusForfiet = "";            
            double auctionCommissionTaxAmount = 0.0;
            String glTransActNum = "";
            String generateSingleTransId = generateLinkID();
            prizedMoneyPaymentTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
            prizedMoneyPaymentTO.setStatus(CommonConstants.STATUS_CREATED);
            System.out.println("###### insertnewTransactionData : " + map);
            ArrayList transferList = new ArrayList();
            TransferTrans transferTrans = new TransferTrans();
            TxTransferTO transferTo = new TxTransferTO();
            ArrayList TxTransferTO = new ArrayList();
            TransactionTO transactionTO = new TransactionTO();
            HashMap txMap = new HashMap();
            HashMap transactionListMap = new HashMap();
            String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
            double totalAmt = CommonUtil.convertObjToDouble(map.get("TOTAL_AMOUNT")).doubleValue();
            if(map.containsKey("DEFAULTER_BONUS_FORFIET") && map.get("DEFAULTER_BONUS_FORFIET") != null){
               defaulterBonusForfiet = CommonUtil.convertObjToStr(map.get("DEFAULTER_BONUS_FORFIET")); 
            }           
            if(map.containsKey("AUCTION_COMMISSION_TAX") && map.get("AUCTION_COMMISSION_TAX") != null){
                auctionCommissionTaxAmount = CommonUtil.convertObjToDouble(map.get("AUCTION_COMMISSION_TAX"));
            }
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
                HashMap applicationMap = new HashMap();
                applicationMap.put("SCHEME_NAME", prizedMoneyPaymentTO.getSchemeName());
                List lst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", applicationMap);
                if (lst != null && lst.size() > 0) {
                    applicationMap = (HashMap) lst.get(0);
                }
                if (transactionTO.getTransType().equals("TRANSFER") || transactionTO.getTransType().equals("CASH")) {
                    String linkBatchId = "";
                    if (transactionTO.getDebitAcctNo() != null && transactionTO.getDebitAcctNo().length() > 0) {
                        linkBatchId = CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo());
                    }
                    HashMap transMap = new HashMap();
                    HashMap operativeMap = new HashMap();
                    String value = CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo());
                    List lstOA = sqlMap.executeQueryForList("getAccountClosingHeads", value);
                    if (lstOA != null && lstOA.size() > 0) {
                        operativeMap = (HashMap) lstOA.get(0);
                    }
                    HashMap debitMap = new HashMap();
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("OA")) {
                        debitMap.put("ACT_NUM", transactionTO.getDebitAcctNo());
                        lst = sqlMap.executeQueryForList("getAccNoProdIdDet", debitMap);
                        if (lst != null && lst.size() > 0) {
                            debitMap = (HashMap) lst.get(0);
                        }
                    }
        //            System.out.println("transactionTO.getPro" + transactionTO.getProductId());
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("SA")) {
                        debitMap.put("prodId", transactionTO.getProductId());
                        lst = sqlMap.executeQueryForList("getAccountHeadProdSAHead", debitMap);
                        if (lst != null && lst.size() > 0) {
                            debitMap = (HashMap) lst.get(0);
                        }
                    }
                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
          //          System.out.println("###### insertTransactionData prizedMoneyPaymentTO : " + prizedMoneyPaymentTO);
                    if (transactionTO.getTransType().equals("TRANSFER")) {
                        if (map.containsKey("GL_TRANS_ACT_NUM") && CommonUtil.convertObjToStr(map.get("GL_TRANS_ACT_NUM")).length() > 0) {
                            glTransActNum = CommonUtil.convertObjToStr(map.get("GL_TRANS_ACT_NUM"));
                        }
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue()
                                + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue() >= 0) {
                            txMap = new HashMap();
                            //Changed By Suresh
                            if (map.containsKey("MDS_CLOSURE")) {
                                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("MDS_PAYABLE_HEAD"));
                            } else {
                                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("PAYMENT_HEAD"));
                            }
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " PAYMENT");
                            txMap.put(TransferTrans.DR_BRANCH, map.get("SELECTED_BRANCH_ID"));

                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
//                            System.out.println("txMap : " + txMap + "amt :" + (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue()
//                                    + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue()));
//                            System.out.println("dddeeefaulterrfor netr amttt" + prizedMoneyPaymentTO.getDefaulter_marked());
//                        if(!prizedMoneyPaymentTO.getDefaulter_marked().equals("Y"))
//                        {
                            txMap.put("TRANS_MOD_TYPE", "MDS");                            
                            if (!(map.containsKey("AUCTION_TRANS") && map.get("AUCTION_TRANS").equals("Y"))) {//Added by sreekrishnan
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue()
                                + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue()
                                + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue()));
                            }else{
                                // Commission service tax to be substracted - change here  350000 - 5000 = 345000
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount())) - auctionCommissionTaxAmount);
                            }
                            System.out.println("amountttt " + (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue()
                                    + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue()
                                    + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue()));
//                        }
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                            transferTo.setSingleTransId(generateSingleTransId); 
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            transferTo.setInstrumentNo2("TOTAL_AMOUNT");// Added by nithya on 31-10-2019 for KD-680
                            
                            //                            if(!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")){
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                            //                            }
                            TxTransferTO.add(transferTo);
                        }
//                        System.out.println("transferTo List 1 : " + transferTo);
//                        System.out.println("dddeeefaulterrfor netr amttt" + prizedMoneyPaymentTO.getDefaulter_marked());
                        if (!prizedMoneyPaymentTO.getDefaulter_marked().equals("Y")) {                            
                            if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()).doubleValue() > 0) {
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL") && transactionTO.getProductId().equals("")) {
                                    txMap.put(TransferTrans.CR_AC_HD, (String) transactionTO.getDebitAcctNo());
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                } else if (!transactionTO.getProductType().equals("") && (transactionTO.getProductType().equals("OA") || transactionTO.getProductType().equals("SA"))) {
                                    txMap.put(TransferTrans.CR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                                    txMap.put(TransferTrans.CR_ACT_NUM, transactionTO.getDebitAcctNo());
                                    txMap.put(TransferTrans.CR_PROD_ID, transactionTO.getProductId());
                                    if ((transactionTO.getProductType().equals("OA"))) {
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                                    } else {
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.SUSPENSE);
                                    }
                                }
                                else if (!transactionTO.getProductType().equals("") && (transactionTO.getProductType().equals("TD"))) {
                                    txMap.put(TransferTrans.CR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                                    txMap.put(TransferTrans.CR_ACT_NUM, transactionTO.getDebitAcctNo());
                                    txMap.put(TransferTrans.CR_PROD_ID, transactionTO.getProductId());
                                    if ((transactionTO.getProductType().equals("TD"))) {
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                    }
                                }
                                if (transactionTO.getProductType().equals("OA")){
                                    txMap.put("TRANS_MOD_TYPE", "OA");
                                }else if(transactionTO.getProductType().equals("AB")){
                                    txMap.put("TRANS_MOD_TYPE", "AB");
                                }else if(transactionTO.getProductType().equals("SA")){
                                    txMap.put("TRANS_MOD_TYPE", "SA");
                                }else if(transactionTO.getProductType().equals("TL")){
                                    txMap.put("TRANS_MOD_TYPE", "TL");
                                }else if(transactionTO.getProductType().equals("AD")){
                                    txMap.put("TRANS_MOD_TYPE", "AD");
                                }else if(transactionTO.getProductType().equals("TD")){
                                    txMap.put("TRANS_MOD_TYPE", "TD");
                                }else{
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                }
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo());
                                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                //Added By Suresh
                                if (CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getDefaulters()).equals("Y") && CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue() > 0) {
//                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()).doubleValue()
//                                            - CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue()));
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()).doubleValue()));
                                } else {
                                    if (!(map.containsKey("AUCTION_TRANS") && map.get("AUCTION_TRANS").equals("Y"))) {//Added by sreekrishnan
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()).doubleValue());
                                    }else{
                                        //transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()));
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()).doubleValue() - auctionCommissionTaxAmount);// Added by nithya on 29-11-2017 for 8369
                                    }
                                    
                                }
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                //                            if(!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")){
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                //                            }
                               // System.out.println("transferTo List 2 : " + transferTo);
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                transferTo.setInstrumentNo2("PRIZED_AMOUNT");// Added by nithya on 31-10-2019 for KD-680
                                TxTransferTO.add(transferTo);
                            }
                        }
                      //  System.out.println("transferTo List 1 : " + transferTo);
                        if (!(map.containsKey("AUCTION_TRANS") && map.get("AUCTION_TRANS").equals("Y"))) {//Added by sreekrishnan
                            if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue() > 0) {
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("COMMISION_HEAD"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " COMMISION");
                                txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                               // System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue());
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue());
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInstrumentNo2("COMMISION");// Added by nithya on 31-10-2019 for KD-680
                                transferTo.setSingleTransId(generateSingleTransId);
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                                                    //added by chithra for service Tax
                            }
                        }
                        
                                if (map.containsKey("serviceTaxDetails")) {
                                    System.out.println("serviceTaxDetails 3");
                                    HashMap serTaxMap = (HashMap) map.get("serviceTaxDetails");
                                    transferTo = new TxTransferTO();
                                    double swachhCess = 0.0;
                                    double krishikalyanCess = 0.0;
                                    double serTaxAmt = 0.0;
                                    double normalServiceTax =0.0;
                                    if (serTaxMap.containsKey("TOT_TAX_AMT") && serTaxMap.get("TOT_TAX_AMT") != null) {
                                        serTaxAmt = CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT"));
                                    }
                                    if (serTaxMap.containsKey(ServiceTaxCalculation.SWACHH_CESS) && serTaxMap.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                        swachhCess = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.SWACHH_CESS));
                                    }
                                    if (serTaxMap.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && serTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                        krishikalyanCess = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                                    }
                                     if (serTaxMap.containsKey(ServiceTaxCalculation.SERVICE_TAX) && serTaxMap.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                normalServiceTax = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.SERVICE_TAX));
                            }
                                  //  serTaxAmt -= (swachhCess + krishikalyanCess);
                                    if (swachhCess > 0) {
                                        txMap = new HashMap();
                                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.CR_AC_HD, serTaxMap.get("SWACHH_HEAD_ID"));
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " CGST");
                                        txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                        txMap.put("TRANS_MOD_TYPE", "MDS");
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, swachhCess);
                                        transferTo.setTransId("-");
                                        transferTo.setBatchId("-");
                                        transferTo.setInitiatedBranch(BRANCH_ID);
                                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        TxTransferTO.add(transferTo);
                                        transactionTO.setChequeNo("CGST");
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferTo.setInstrumentNo2("CGST");// Added by nithya on 31-10-2019 for KD-680
                                        TxTransferTO.add(transferTo);
                                       // serTaxAmt -= swachhCess;
                                    }
                                    if (krishikalyanCess > 0) {
                                        txMap = new HashMap();
                                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.CR_AC_HD, serTaxMap.get("KRISHIKALYAN_HEAD_ID"));
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " SGST");
                                        txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                        txMap.put("TRANS_MOD_TYPE", "MDS");
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, krishikalyanCess);
                                        transferTo.setTransId("-");
                                        transferTo.setBatchId("-");
                                        transferTo.setInitiatedBranch(BRANCH_ID);
                                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        TxTransferTO.add(transferTo);
                                        transactionTO.setChequeNo("SGST");
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferTo.setInstrumentNo2("SGST");// Added by nithya on 31-10-2019 for KD-680
                                        TxTransferTO.add(transferTo);
                                       // serTaxAmt -= krishikalyanCess;
                                    }
                                    if (normalServiceTax > 0) {
                                        txMap = new HashMap();
                                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.CR_AC_HD, serTaxMap.get("TAX_HEAD_ID"));
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " SERVICE TAX");
                                        txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                        txMap.put("TRANS_MOD_TYPE", "MDS");
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax);
                                        transferTo.setTransId("-");
                                        transferTo.setBatchId("-");
                                        transferTo.setInitiatedBranch(BRANCH_ID);
                                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        TxTransferTO.add(transferTo);
                                        transactionTO.setChequeNo("SERVICE_TAX");
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferTo.setInstrumentNo2("SERVICE_TAX");// Added by nithya on 31-10-2019 for KD-680
                                        TxTransferTO.add(transferTo);
                                    }
                                }
                           // }
                        //}
                        //System.out.println("transferTo List 2 : " + transferTo);
                        if (!(map.containsKey("AUCTION_TRANS") && map.get("AUCTION_TRANS").equals("Y"))) {//Added by sreekrishnan
                            if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue() > 0) {
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                //Changed By Suresh
                                if (map.containsKey("MDS_CLOSURE")) {
                                    txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("SUNDRY_RECEIPT_HEAD"));
                                } else {
                                    if (!(map.containsKey("PART_PAY") && map.get("PART_PAY").equals("Y"))) {
                                        txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("BONUS_PAYABLE_HEAD"));
                                    }else{
                                        //txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("BONUS_RECEIVABLE_HEAD"));// Done by nithya on 27-08-2019 for KD 575 - MDS Money Payment With Bonus Recovery Head Option.
                                        txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("PART_PAY_BONUS_RECOVERY"));                                        
                                    } 
                                }
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " BONUS");
                                txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue());
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue());
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));

                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                               // System.out.println("transferTo List 2 : " + transferTo);
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                transferTo.setInstrumentNo2("BONUS");// Added by nithya on 31-10-2019 for KD-680
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                            }
                        }
                     //   System.out.println("transferTo List 3 : " + transferTo);
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNoticeAmount()).doubleValue() > 0) {
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("NOTICE_CHARGES_HEAD"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " NOTICE");
                            txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                       //     System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNoticeAmount()).doubleValue());
                            txMap.put("TRANS_MOD_TYPE", "MDS");
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNoticeAmount()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            transferTo.setInstrumentNo2("NOTICE_CHARGE");// Added by nithya on 31-10-2019 for KD-680
                            TxTransferTO.add(transferTo);
                        }
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getChargeAmount()).doubleValue() > 0) {
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("CHARGE_PAYMENT_HEAD"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " CHARGE");
                            txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            //System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getChargeAmount()).doubleValue());
                            txMap.put("TRANS_MOD_TYPE", "MDS");
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getChargeAmount()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            transferTo.setInstrumentNo2("CHARGE_AMT");// Added by nithya on 31-10-2019 for KD-680
                            TxTransferTO.add(transferTo);
                        }
                        //System.out.println("transferTo List 4 : " + transferTo);
                        //Case Expense Amount Transaction
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getAribitrationAmount()).doubleValue() > 0) {
                          //  System.out.println("Arbitration Started");
                            HashMap whereMap = new HashMap();
                            List chargeList = null;
                            whereMap.put("ACT_NUM", CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()) + "_" + CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getSubNo()));
                            chargeList = (List) sqlMap.executeQueryForList("getMDSCaseChargeDetails", whereMap);
                            if (chargeList != null && chargeList.size() > 0) {
                                for (int i = 0; i < chargeList.size(); i++) {
                                    double chargeAmount = 0.0;
                                    String chargeType = "";
                                    whereMap = (HashMap) chargeList.get(i);
                                    chargeAmount = CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
                                    Rounding rod = new Rounding();
                                    chargeAmount = (double) rod.getNearest((long) (chargeAmount * 100), 100) / 100;
                                    chargeType = CommonUtil.convertObjToStr(whereMap.get("CHARGE_TYPE"));
                                    if (chargeAmount > 0) {
                                        transferTo = new TxTransferTO();
                                        txMap = new HashMap();
                                        if (chargeType.equals("ARC Cost")) {
                                            chargeType = "ARC_COST";
                                        } else if (chargeType.equals("ARC Expense")) {
                                            chargeType = "ARC_EXPENSE";
                                        } else if (chargeType.equals("EA Cost")) {
                                            chargeType = "EA_COST";
                                        } else if (chargeType.equals("EA Expense")) {
                                            chargeType = "EA_EXPENSE";
                                        } else if (chargeType.equals("EP Cost")) {
                                            chargeType = "EP_COST";
                                        } else if (chargeType.equals("EP Expense")) {
                                            chargeType = "EP_EXPENSE";
                                        }
                                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                                        System.out.println("###### chargeType Head : " + chargeType);
//                                        System.out.println("###### chargeAmount : " + chargeAmount);
                                        //txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("CASE_EXPENSE_HEAD"));
                                        txMap.put(TransferTrans.CR_AC_HD, applicationMap.get(chargeType));
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.CR_BRANCH, prizedMoneyPaymentTO.getBranchCode()); //BRANCH_ID
                                        txMap.put(TransferTrans.PARTICULARS, chargeType + applicationMap.get(" MP_MDS_CODE") + "-" + prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo());
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                        txMap.put("TRANS_MOD_TYPE", "MDS");
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, chargeAmount);
                                        transferTo.setTransId("-");
                                        transferTo.setBatchId("-");
                                        transferTo.setTransDt(currDt);
                                        transferTo.setInitiatedBranch(BRANCH_ID);
                                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                        transactionTO.setChequeNo("SERVICE_TAX");
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferTo.setInstrumentNo2(chargeType);// Added by nithya on 31-10-2019 for KD-680
                                        TxTransferTO.add(transferTo);
                                        //System.out.println("Arbitration transferTo List  : " + i + " " + transferTo);
                                    }
                                }
                            }
                            //                            transferTo = new TxTransferTO();
                            //                            txMap = new HashMap();
                            //                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            //                            txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
                            //                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("CASE_EXPENSE_HEAD"));
                            //                            txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
                            //                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo()+"_"+prizedMoneyPaymentTO.getSubNo()+" CASE EXPENSE");
                            //                            txMap.put(TransferTrans.CR_BRANCH,BRANCH_ID);
                            //                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
                            //                            txMap.put(TransferTrans.DR_INST_TYPE,"VOUCHER");
                            //                            System.out.println("txMap : " + txMap+"serviceAmt :"+CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getAribitrationAmount()).doubleValue());
                            //                            transferTo =  transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getAribitrationAmount()).doubleValue()) ;
                            //                            transferTo.setTransId("-");
                            //                            transferTo.setBatchId("-");
                            //                            transferTo.setInitiatedBranch(BRANCH_ID);
                            //                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            //                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            //                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                            //                            TxTransferTO.add(transferTo);
                            //                            transactionTO.setChequeNo("SERVICE_TAX");
                        }
                      //  System.out.println("transferTo List 5 : " + transferTo);
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getDiscountAmount()).doubleValue() > 0) {
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("DISCOUNT_HEAD"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " DISCOUNT");
                            txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                        //    System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getDiscountAmount()).doubleValue());
                            txMap.put("TRANS_MOD_TYPE", "MDS");
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getDiscountAmount()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            transferTo.setInstrumentNo2("DISCOUNT");// Added by nithya on 31-10-2019 for KD-680
                            TxTransferTO.add(transferTo);
                            transactionTO.setChequeNo("SERVICE_TAX");
                        }

                  //      System.out.println("transferTo List 5 : " + transferTo);
                        //Added By Suresh
                        if (CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getDefaulters()).equals("Y")) {
                            if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPenalAmount()).doubleValue() > 0) {
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("PENAL_INTEREST_HEAD"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " PENAL");
                                txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                    //            System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPenalAmount()).doubleValue());
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPenalAmount()).doubleValue());
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                transferTo.setInstrumentNo2("PENAL_AMT");// Added by nithya on 31-10-2019 for KD-680
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
                            }

                            if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue() > 0) {
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " OVERDUE");
                                txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                               // System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue());
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue());
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                transferTo.setInstrumentNo2("OVERDUE_AMT");// Added by nithya on 31-10-2019 for KD-680
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
                            }
                        }
                        
                        // Added by nithya on 26-07-2018 for KD-179 Mds defaulters payment issue (deduction entries not inserting personal ledger )
                      
                            if (CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getDefaulters()).equals("Y")) {
                                if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getChitalBonusAmt()).doubleValue() > 0) {
                                    System.out.println("Bonus Started");
                                    transferTo = new TxTransferTO();
                                    txMap = new HashMap();
                                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                    if (defaulterBonusForfiet.equalsIgnoreCase("Y")) {
                                        txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("MDS_FORFEITED_ACCT_HEAD"));
                                    } else {
                                        txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
                                    }
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                    txMap.put(TransferTrans.PARTICULARS, "Chital Bonus " + applicationMap.get("MP_MDS_CODE") + "-" + prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo());
                                    txMap.put("TRANS_MOD_TYPE", "MDS");
                                    txMap.put("SCREEN_NAME", "MDS_MONEY_PAYMENT");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getChitalBonusAmt()));
                                    transferTo.setInstrumentNo1("SI");
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setTransType(CommonConstants.CREDIT);
                                    transferTo.setProdType(TransactionFactory.GL);
                                    //transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("BONUS_PAYABLE_HEAD")));
                                    transferTo.setTransDt(currDt);
                                    transferTo.setBranchId(BRANCH_ID);
                                    transferTo.setInitiatedBranch(BRANCH_ID);
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                    transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                    transferTo.setSingleTransId(CommonUtil.convertObjToStr(generateSingleTransId));
                                    transactionTO.setChequeNo("SERVICE_TAX");
                                    transferTo.setInstrumentNo2("CHITAL_BONUS");// Added by nithya on 31-10-2019 for KD-680
                                    TxTransferTO.add(transferTo);

                                    System.out.println("debit Started");
                                    transferTo = new TxTransferTO();
                                    txMap = new HashMap();
                                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                    //txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("PAYMENT_HEAD"));
                                    txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("BONUS_RECEIVABLE_HEAD"));
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                                    txMap.put(TransferTrans.PARTICULARS, "Chittal Bonus" + applicationMap.get("MP_MDS_CODE") + "-" + prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo());
                                    txMap.put("TRANS_MOD_TYPE", "MDS");
                                    txMap.put("SCREEN_NAME", "MDS_MONEY_PAYMENT");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getChitalBonusAmt()));
                                    transferTo.setInstrumentNo1("SI");
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setTransType(CommonConstants.DEBIT);
                                    transferTo.setProdType(TransactionFactory.GL);
                                    transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("PAYMENT_HEAD")));
                                    transferTo.setTransDt(currDt);
                                    transferTo.setBranchId(BRANCH_ID);
                                    transferTo.setInitiatedBranch(BRANCH_ID);
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                    transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                    transferTo.setSingleTransId(CommonUtil.convertObjToStr(generateSingleTransId));
                                    transactionTO.setChequeNo("SERVICE_TAX");
                                    transferTo.setInstrumentNo2("CHITAL_BONUS");// Added by nithya on 31-10-2019 for KD-680
                                    TxTransferTO.add(transferTo);
                                }
                            }
                        

//                       //DEFAULTER


                      //  System.out.println("dddeeefaulterrr" + prizedMoneyPaymentTO.getDefaulter_marked());
                        if (prizedMoneyPaymentTO.getDefaulter_marked().equals("Y")) {
                            HashMap account_map = new HashMap();
                            account_map.put("CHITTAL_NO", prizedMoneyPaymentTO.getChittalNo());
                            List defaulter_acc_head = sqlMap.executeQueryForList("getAccounthead", account_map);
                            String bonusAcountHead = "";
                            String commissionAcountHead = "";
                            String interstAccountHead = "";
                            String RecieptAccountHead = "";
                            double defaulter_Inteset = 0.0;
                            double defaulter_bonus_recoverd = 0.0;
                            double defaulter_comm = 0.0;
                            String SuspensetAccountHead = "";
                            String Misclens = "";

                            if (defaulter_acc_head != null && defaulter_acc_head.size() > 0) {
                                account_map = (HashMap) defaulter_acc_head.get(0);
                                bonusAcountHead = account_map.get("BONUS_RECEIVABLE_HEAD").toString();
                                commissionAcountHead = account_map.get("COMMISION_HEAD").toString();
                                interstAccountHead = account_map.get("PENAL_INTEREST_HEAD").toString();
                                RecieptAccountHead = account_map.get("RECEIPT_HEAD").toString();
                                SuspensetAccountHead = account_map.get("SUSPENSE_HEAD").toString();
                                Misclens = account_map.get("MISCELLANEOUS_HEAD").toString();



                            }
                            if (prizedMoneyPaymentTO.getDefaulter_bonus_recoverd() != null && prizedMoneyPaymentTO.getDefaulter_bonus_recoverd() > 0.0) {
                                defaulter_bonus_recoverd = prizedMoneyPaymentTO.getDefaulter_bonus_recoverd();  //AJITH Removed Double.parseDouble()
//                                transferTo = new TxTransferTO();
//                                txMap = new HashMap();
//                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                                txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//                                txMap.put(TransferTrans.CR_AC_HD, bonusAcountHead);
//                                txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo()+"_"+prizedMoneyPaymentTO.getSubNo()+" OVERDUE");
//                                txMap.put(TransferTrans.CR_BRANCH,BRANCH_ID);
//                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
//                                txMap.put(TransferTrans.DR_INST_TYPE,"VOUCHER");
//                                System.out.println("txMap : " + txMap+"serviceAmt :"+CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue());
//                                transferTo =  transactionDAO.addTransferCreditLocal(txMap, defaulter_bonus_recoverd) ;
//                                transferTo.setTransId("-");
//                                transferTo.setBatchId("-");
//                                transferTo.setInitiatedBranch(BRANCH_ID);
//                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
//                                TxTransferTO.add(transferTo);
//                                transactionTO.setChequeNo("SERVICE_TAX");
                            }


                            if (prizedMoneyPaymentTO.getDefaulter_comm() != null && prizedMoneyPaymentTO.getDefaulter_comm() > 0.0) {
                                defaulter_comm = prizedMoneyPaymentTO.getDefaulter_comm();  //AJITH Removed Double.parseDouble()
                        //        System.out.println("commission.." + defaulter_comm);
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_AC_HD, Misclens);
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " OVERDUE");
                                txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                System.out.println("txMap ccc: " + txMap + "serviceAmtcccc :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue());
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, defaulter_comm);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInstrumentNo2("DEFAULTER_COMMISION");// Added by nithya on 31-10-2019 for KD-680
                                
                                
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
                            }



                            if (prizedMoneyPaymentTO.getDefaulter_interst() != null && prizedMoneyPaymentTO.getDefaulter_interst() > 0.0) {
                                defaulter_Inteset = prizedMoneyPaymentTO.getDefaulter_interst();  //AJITH Removed Double.parseDouble()
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_AC_HD, interstAccountHead);
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " OVERDUE");
                                txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                System.out.println("txMap iii: " + txMap + "serviceAmtiiii :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue());
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, defaulter_Inteset);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInstrumentNo2("DEFAULTER_INTEREST");// Added by nithya on 31-10-2019 for KD-680
                                
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
                            }


                            // System.out.print("###### cashList: "+cashList);
                            double paidamount = 0.0;
                            HashMap mdsMap = new HashMap();
                            mdsMap.put("CHITTAL_NO", prizedMoneyPaymentTO.getChittalNo());
                            mdsMap.put("SUB_NO", CommonUtil.convertObjToInt(prizedMoneyPaymentTO.getSubNo()));

                            List getpaidAmounts = sqlMap.executeQueryForList("getMdsPaidAmount", mdsMap);
                            if (getpaidAmounts != null && getpaidAmounts.size() > 0) {
                                mdsMap = new HashMap();
                                mdsMap = (HashMap) getpaidAmounts.get(0);
                                if (mdsMap != null && mdsMap.containsKey("TOTAL") && mdsMap.get("TOTAL") != null) {

                                    paidamount = Double.parseDouble(mdsMap.get("TOTAL").toString());
                                } else {
                                    paidamount = 0.0;
                                }
                            } else {
                                paidamount = 0.0;
                            }
//                        double d=Double.parseDouble(prizedMoneyPaymentTO.getPrizedAmount());
//                            System.out.println("d....."+d+"::"+paidamount);
//                        double amountToReciept=d-paidamount;
//                        

                            double TotDet = CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue()
                                    + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue()
                                    + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue();
                           // System.out.println("d....." + TotDet + "::" + paidamount);
                            double amountToReciept = TotDet - paidamount;
                            //System.out.println("TotDetawsad..." + TotDet);
                            double bonusTogive = 0.0;
                            HashMap mdsbonusMap = new HashMap();
                            mdsbonusMap.put("CHITTAL_NO", prizedMoneyPaymentTO.getChittalNo());
                            mdsbonusMap.put("SUB_NO", CommonUtil.convertObjToInt(prizedMoneyPaymentTO.getSubNo()));
                            mdsbonusMap.put("SCHME_NAME", prizedMoneyPaymentTO.getSchemeName());

                            List payableBons = sqlMap.executeQueryForList("getPayableBonus", mdsbonusMap);
                            if (payableBons != null && payableBons.size() > 0) {
                                HashMap mapDts = new HashMap();
                                mapDts = (HashMap) payableBons.get(0);
                                if (mapDts != null) {
                                    bonusTogive = Double.parseDouble(mapDts.get("BONUS_PAYABLE").toString());

                                } else {
                                    bonusTogive = 0.0;
                                }
                            } else {
                                bonusTogive = 0.0;
                            }

                            if (bonusTogive > 0.0) {

                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.DR_AC_HD, bonusAcountHead);
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " OVERDUE");
                                txMap.put(TransferTrans.DR_BRANCH,BRANCH_ID);
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                               // System.out.println("txMap bbfff: " + txMap + "serviceAmt bbtff:" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue());
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, bonusTogive);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
                            }
                            if (amountToReciept > 0.0) {
                                // defaulter_Inteset=Double.parseDouble(prizedMoneyPaymentTO.getDefaulter_interst());
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_AC_HD, RecieptAccountHead);
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " OVERDUE");
                                txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                             //   System.out.println("txMap iii: " + txMap + "serviceAmtiiii :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue());
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, amountToReciept);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
//                                transferTo = new TxTransferTO();
//                                txMap = new HashMap();
//                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                                txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//                                txMap.put(TransferTrans.CR_AC_HD, RecieptAccountHead);
//                                txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo()+"_"+prizedMoneyPaymentTO.getSubNo()+" OVERDUE");
//                                txMap.put(TransferTrans.CR_BRANCH,BRANCH_ID);
//                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
//                                txMap.put(TransferTrans.DR_INST_TYPE,"VOUCHER");
//                                System.out.println("txMap amount to aed : " + txMap+"serviceAmt amt :"+CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue());
//                                transferTo =  transactionDAO.addTransferCreditLocal(txMap, amountToReciept) ;
//                                transferTo.setTransId("-");
//                                transferTo.setBatchId("-");
//                                transferTo.setInitiatedBranch(BRANCH_ID);
//                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
//                                TxTransferTO.add(transferTo);
//                                transactionTO.setChequeNo("SERVICE_TAX");
                            }

                            double chittalPaid = paidamount;
                            paidamount = paidamount - (defaulter_Inteset + defaulter_bonus_recoverd + defaulter_comm);
                            if (paidamount > 0.0) {
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_AC_HD, SuspensetAccountHead);
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " OVERDUE");
                                txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                             //   System.out.println("txMap iii: " + txMap + "serviceAmtiiii :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue());
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, paidamount);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");

//                                transferTo = new TxTransferTO();
//                                txMap = new HashMap();
//                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                                txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//                                txMap.put(TransferTrans.CR_AC_HD, SuspensetAccountHead);
//                                txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo()+"_"+prizedMoneyPaymentTO.getSubNo()+" OVERDUE");
//                                txMap.put(TransferTrans.CR_BRANCH,BRANCH_ID);
//                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
//                                txMap.put(TransferTrans.DR_INST_TYPE,"VOUCHER");
//                                System.out.println("txMap : " + txMap+"serviceAmt :"+CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue());
//                                transferTo =  transactionDAO.addTransferCreditLocal(txMap, paidamount) ;
//                                transferTo.setTransId("-");
//                                transferTo.setBatchId("-");
//                                transferTo.setInitiatedBranch(BRANCH_ID);
//                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
//                                TxTransferTO.add(transferTo);
//                                transactionTO.setChequeNo("SERVICE_TAX");
                            }

                            TotDet = TotDet + bonusTogive;
                            double NtMislns = TotDet - (amountToReciept + paidamount + defaulter_Inteset + defaulter_comm + prizedMoneyPaymentTO.getCommisionAmount() + prizedMoneyPaymentTO.getBonusAmount());  //AJITH Removed Double.parseDouble()
                            //Math.abs(TotDet);
                            //  ddd
                            if (NtMislns > 0.0) {
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_AC_HD, Misclens);
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " OVERDUE");
                                txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                //System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue());
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, NtMislns);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
                            } else if (NtMislns < 0.0) {
                                ExecptionDeftler = true;
                                throw new TTException("Cannot make Defaulter Payment !!!");
                            }
                        }

                        ////DEFAULTER
                        // Changed By Suresh
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getRefundAmount()).doubleValue() > 0) {   //Cr Refund_Amount
                            //System.out.print("####### Refund Amount : " + prizedMoneyPaymentTO.getRefundAmount());
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            if (CommonUtil.convertObjToStr(applicationMap.get("SUSPENSE_GL_ACCNO")).equals("GL")) {
                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("SUSPENSE_HEAD"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            } else {
                                HashMap suspenseMap = new HashMap();                // Suspense Acc Head
                                suspenseMap.put("PROD_ID", applicationMap.get("SUSPENSE_PROD_ID"));
                                List suspenseLst = sqlMap.executeQueryForList("getAccountHeadProdSA", suspenseMap);
                                if (suspenseLst != null && suspenseLst.size() > 0) {
                                    suspenseMap = (HashMap) suspenseLst.get(0);
                                    txMap.put(TransferTrans.CR_AC_HD, suspenseMap.get("AC_HEAD"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.SUSPENSE);
                                    txMap.put(TransferTrans.CR_PROD_ID, applicationMap.get("SUSPENSE_PROD_ID"));
                                    txMap.put(TransferTrans.CR_ACT_NUM, applicationMap.get("SUSPENSE_ACC_NO"));
                                }
                            }
                            txMap.put("TRANS_MOD_TYPE", "MDS");
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " REFUND");
                            txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            //System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getRefundAmount()).doubleValue());
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getRefundAmount()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            TxTransferTO.add(transferTo);
                            transactionTO.setChequeNo("SERVICE_TAX");
                        }
                       // System.out.println("####### $$$$$$ " + map.get("REFUND_AMOUNT") + "dsfdfdsf" + map.containsKey("REFUND_AMOUNT"));
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getRefundAmount()).doubleValue() > 0 || map.containsKey("REFUND_AMOUNT")) {  //Cr Reverse_Bonus_Amount
                            double earnedBonus = 0.0;
                            if (applicationMap.get("BONUS_EXISTING_CHITTAL") != null && !applicationMap.get("BONUS_EXISTING_CHITTAL").equals("") && (applicationMap.get("BONUS_EXISTING_CHITTAL").equals("Y"))) {
                                HashMap hashMap = new HashMap();
                                hashMap.put("CHITTAL_NO", prizedMoneyPaymentTO.getChittalNo());
                                List paidList = sqlMap.executeQueryForList("getTotalInstAmount", hashMap);
                                if (paidList != null && paidList.size() > 0) {
                                    hashMap = (HashMap) paidList.get(0);
                                    earnedBonus = CommonUtil.convertObjToDouble(hashMap.get("PAID_AMT")).doubleValue() - CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getRefundAmount()).doubleValue();
                                }
                                if (earnedBonus > 0) {
                         //           System.out.print("####### Reverse Bonus Amount : " + earnedBonus);
                                    transferTo = new TxTransferTO();
                                    txMap = new HashMap();
                                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    //Changed By Suresh
                                    if (map.containsKey("MDS_CLOSURE")) {
                                        txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("SUNDRY_RECEIPT_HEAD"));
                                    } else {
                                        txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("BONUS_PAYABLE_HEAD"));
                                    }
                                    txMap.put("TRANS_MOD_TYPE", "MDS");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " REVERSE_BONUS");
                                    txMap.put(TransferTrans.CR_BRANCH,map.get("SELECTED_BRANCH_ID"));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, earnedBonus);
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setInitiatedBranch(BRANCH_ID);
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    
                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    TxTransferTO.add(transferTo);
                                    transactionTO.setChequeNo("SERVICE_TAX");
                                }
                            }
                        }
                       // System.out.println("dddd");
                        HashMap applnMap = new HashMap();
                        transferDAO = new TransferDAO();
                        map.put("MODE", map.get("COMMAND"));
                        map.put("COMMAND", map.get("MODE"));
                        map.put("TxTransferTO", TxTransferTO);
                      //  System.out.println("reckjnjfgnfrtdd hererer" + map);
                        transMap = transferDAO.execute(map, false);
                        prizedMoneyPaymentTO.setTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                        //System.out.println("transactionDAO map : " + map);
                        transactionDAO.setBatchId(prizedMoneyPaymentTO.getTransId());
                        transactionDAO.setBatchDate(currDt);
                        transactionDAO.execute(map);
                        HashMap linkBatchMap = new HashMap();
                        if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")
                            && transactionTO.getProductId().equals("") && !map.containsKey("LOCKER_SURRENDER_DAO")) {
                            linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
                        } else if (!map.containsKey("LOCKER_SURRENDER_DAO")) {
                            linkBatchMap.put("LINK_BATCH_ID", transactionTO.getDebitAcctNo());
                        } else {
                            linkBatchMap.put("LINK_BATCH_ID", map.get("LOCKER_SURRENDER_ID"));
                        }
                        linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
                        linkBatchMap.put("BATCH_ID", transMap.get("TRANS_ID"));
                        linkBatchMap.put("TRANS_DT", currDt);
                        linkBatchMap.put("INITIATED_BRANCH", _branchCode);
                        sqlMap.executeUpdate("updateLinkBatchIdTransfer", linkBatchMap);
                        linkBatchMap = null;
                        transMap = null;
                    } else if (transactionTO.getTransType().equals("CASH")) {
                        //generateSingleTransId = generateLinkID();          
                            transactionTO.setChequeNo("SERVICE_TAX");
                            HashMap cashTransMap = new HashMap();
                            //Total Amount (Prized Amount)
                            cashTransMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                            cashTransMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                            cashTransMap.put("PENAL_DETAILS", "PENAL_DETAILS");
                            cashTransMap.put("LINK_BATCH_ID", prizedMoneyPaymentTO.getChittalNo());
                            cashTransMap.put("PAYMENT_HEAD", applicationMap.get("PAYMENT_HEAD"));
                            cashTransMap.put("MDS_PAYABLE_HEAD", applicationMap.get("MDS_PAYABLE_HEAD"));
                            cashTransMap.put("SCHEME_NAME", prizedMoneyPaymentTO.getSchemeName());
                            cashTransMap.put("USER_ID", map.get("USER_ID"));
                            cashTransMap.put("TRANS_MOD_TYPE", "MDS");
    //                      cashTransMap.put("PRIZED_AMOUNT",CommonUtil.convertObjToStr(transactionTO.getTransAmt()));
                            //Added by sreekrishnan   
                          //  System.out.println("prizeddd%#$%#$%"+CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()));
                           if ((map.containsKey("TRASFER_BONUS_COMMISION") && map.get("TRASFER_BONUS_COMMISION").equals("Y"))) {
                               cashTransMap.put("TOTAL_AMOUNT", String.valueOf(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue()));                               
                            }else{
                                System.out.println("AUCTION_TRANS^$$^$^$"+map.get("AUCTION_TRANS"));
                                if (!(map.containsKey("AUCTION_TRANS") && map.get("AUCTION_TRANS").equals("Y"))) {
                                    cashTransMap.put("TOTAL_AMOUNT", String.valueOf(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue()
                                    + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue()
                                    + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue()));
                                }else{                            
                                    cashTransMap.put("TOTAL_AMOUNT", String.valueOf(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue() - auctionCommissionTaxAmount));
                                }
                            }
                            //Changed By Suresh
                            if (map.containsKey("MDS_CLOSURE")) {
                                cashTransMap.put("MDS_CLOSURE", "MDS_CLOSURE");
                            }
                            CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                            cashTransMap.put("INSTRUMENT_NO2","TOTAL_AMOUNT");// Added by nithya on 31-10-2019 for KD-680
                            ArrayList applncashList = newApplicationList(cashTransMap,generateSingleTransId);
                            cashTransMap.put("DAILYDEPOSITTRANSTO",applncashList);
                            cashTransMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN));
                            cashTransMap.put(CommonConstants.SCREEN,(String) map.get(CommonConstants.SCREEN));
                            HashMap cashMap = cashTransactionDAO.execute(cashTransMap,false);
                            prizedMoneyPaymentTO.setCashId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                            //System.out.println("cashMap :" + cashMap);

                            //Insert Remit _Issue_Trans
                            transactionTO.setBatchId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                            transactionTO.setBatchDt(currDt);
                            transactionTO.setTransId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                            transactionTO.setStatus(prizedMoneyPaymentTO.getStatus());
                            transactionTO.setBranchId(_branchCode);
                           // System.out.println("transactionTO------------------->" + transactionTO);
                            sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", transactionTO);
                            //Update LinkBatchId as Trans_ID
                            HashMap linkBatchMap = new HashMap();
                            linkBatchMap.put("LINK_BATCH_ID", cashMap.get("TRANS_ID"));
                            linkBatchMap.put("TRANS_ID", cashMap.get("TRANS_ID"));
                            linkBatchMap.put("TRANS_DT", currDt);
                            linkBatchMap.put("INITIATED_BRANCH", BRANCH_ID);
                            sqlMap.executeUpdate("updateLinkBatchIdCash", linkBatchMap);
                            linkBatchMap = null;

                        ArrayList cashList = new ArrayList();
                        //Added by sreekrishnan for kottayam
                        if (!(map.containsKey("TRASFER_BONUS_COMMISION") && map.get("TRASFER_BONUS_COMMISION").equals("Y"))) {
                        //Commission
                            HashMap MDSMap = new HashMap();
                        if (!(map.containsKey("AUCTION_TRANS") && map.get("AUCTION_TRANS").equals("Y"))) {//Added by sreekrishnan
                            if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue() > 0) {
                                MDSMap = new HashMap();
                                MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                                MDSMap.put("BRANCH_CODE", _branchCode);
                                MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                                MDSMap.put("PARTICULARS", prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " COMMISION");
                                MDSMap.put("ACCT_HEAD", applicationMap.get("COMMISION_HEAD"));
                                MDSMap.put("TRANS_AMOUNT", prizedMoneyPaymentTO.getCommisionAmount());
                                MDSMap.put("AUTHORIZEREMARKS", "COMMISION");
                                MDSMap.put("TRANS_MOD_TYPE", "MDS");
                                MDSMap.put(CommonConstants.SCREEN,(String) map.get(CommonConstants.SCREEN));
                                MDSMap.put("INSTRUMENT_NO2","COMMISION");// Added by nithya on 31-10-2019 for KD-680
                                cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                                                    //added by chithra for service Tax
                            }
                        }
                                if (map.containsKey("serviceTaxDetails")) {
                                    System.out.println("serviceTaxDetails 4");
                                    HashMap serTaxMap = (HashMap) map.get("serviceTaxDetails");
                                    double swachhCess = 0.0;
                                    double krishikalyanCess = 0.0;
                                    double serTaxAmt = 0.0;
                                    double normalServiceTax = 0.0;
                                    if (serTaxMap.containsKey("TOT_TAX_AMT") && serTaxMap.get("TOT_TAX_AMT") != null) {
                                        serTaxAmt = CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT"));
                                    }
                                    if (serTaxMap.containsKey(ServiceTaxCalculation.SWACHH_CESS) && serTaxMap.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                        swachhCess = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.SWACHH_CESS));
                                    }
                                    if (serTaxMap.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && serTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                        krishikalyanCess = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                                    }
                                      if (serTaxMap.containsKey(ServiceTaxCalculation.SERVICE_TAX) && serTaxMap.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                normalServiceTax = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.SERVICE_TAX));
                            }
                                 //   serTaxAmt -= (swachhCess + krishikalyanCess);
                                    if (swachhCess > 0) {
                                        MDSMap = new HashMap();
                                        MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                                        MDSMap.put("BRANCH_CODE", _branchCode);
                                        MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                        MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                                        MDSMap.put("PARTICULARS", prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " CGST");
                                        MDSMap.put("ACCT_HEAD", serTaxMap.get("SWACHH_HEAD_ID"));
                                        MDSMap.put("TRANS_AMOUNT", swachhCess);
                                        MDSMap.put("AUTHORIZEREMARKS", "SERVICE TAX");
                                        MDSMap.put("TRANS_MOD_TYPE", "MDS");
                                        MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                                        MDSMap.put("INSTRUMENT_NO2","CGST");// Added by nithya on 31-10-2019 for KD-680
                                        cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                                        //serTaxAmt -= swachhCess;
                                    }
                                    if (krishikalyanCess > 0) {
                                        MDSMap = new HashMap();
                                        MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                                        MDSMap.put("BRANCH_CODE", _branchCode);
                                        MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                        MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                                        MDSMap.put("PARTICULARS", prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " SGST");
                                        MDSMap.put("ACCT_HEAD", serTaxMap.get("KRISHIKALYAN_HEAD_ID"));
                                        MDSMap.put("TRANS_AMOUNT", krishikalyanCess);
                                        MDSMap.put("AUTHORIZEREMARKS", "SERVICE TAX");
                                        MDSMap.put("TRANS_MOD_TYPE", "MDS");
                                        MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                                        MDSMap.put("INSTRUMENT_NO2","SGST");// Added by nithya on 31-10-2019 for KD-680
                                        cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                                        //serTaxAmt -= krishikalyanCess;
                                    }
                                    if (normalServiceTax > 0) {
                                        MDSMap = new HashMap();
                                        MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                                        MDSMap.put("BRANCH_CODE", _branchCode);
                                        MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                        MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                                        MDSMap.put("PARTICULARS", prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " SERVICE TAX");
                                        MDSMap.put("ACCT_HEAD", serTaxMap.get("TAX_HEAD_ID"));
                                        MDSMap.put("TRANS_AMOUNT", normalServiceTax);
                                        MDSMap.put("AUTHORIZEREMARKS", "SERVICE TAX");
                                        MDSMap.put("TRANS_MOD_TYPE", "MDS");
                                        MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                                        MDSMap.put("INSTRUMENT_NO2","SERVICE_TAX");// Added by nithya on 31-10-2019 for KD-680
                                        cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                                    }
                                }
                           // }
                        //}
                    }else{
                        if (map.containsKey("serviceTaxDetails")) {
                            System.out.println("serviceTaxDetails 5");
                                HashMap serTaxMap = (HashMap) map.get("serviceTaxDetails");
                                HashMap MDSMap ;
                                 double swachhCess = 0.0;
                                    double krishikalyanCess = 0.0;
                                    double serTaxAmt = 0.0;
                                    double normalServiceTax =0.0;
                                    if (serTaxMap.containsKey("TOT_TAX_AMT") && serTaxMap.get("TOT_TAX_AMT") != null) {
                                        serTaxAmt = CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT"));
                                    }
                                    if (serTaxMap.containsKey(ServiceTaxCalculation.SWACHH_CESS) && serTaxMap.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                        swachhCess = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.SWACHH_CESS));
                                    }
                                    if (serTaxMap.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && serTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                        krishikalyanCess = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                                    }
                                if (serTaxMap.containsKey(ServiceTaxCalculation.SERVICE_TAX) && serTaxMap.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                normalServiceTax = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.SERVICE_TAX));
                            }
                                   // serTaxAmt -= (swachhCess + krishikalyanCess);
                                    if (swachhCess > 0) {
                                        MDSMap = new HashMap();
                                        MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                                        MDSMap.put("BRANCH_CODE", _branchCode);
                                        MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                        MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                                        MDSMap.put("PARTICULARS", prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " CGST");
                                        MDSMap.put("ACCT_HEAD", serTaxMap.get("SWACHH_HEAD_ID"));
                                        MDSMap.put("TRANS_AMOUNT", swachhCess);
                                        MDSMap.put("AUTHORIZEREMARKS", "SERVICE TAX");
                                        MDSMap.put("TRANS_MOD_TYPE", "MDS");
                                        MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                                        MDSMap.put("INSTRUMENT_NO2","CGST");// Added by nithya on 31-10-2019 for KD-680
                                        cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                                       // serTaxAmt -= swachhCess;
                                    }
                                    if (krishikalyanCess > 0) {
                                        MDSMap = new HashMap();
                                        MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                                        MDSMap.put("BRANCH_CODE", _branchCode);
                                        MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                        MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                                        MDSMap.put("PARTICULARS", prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " SGST");
                                        MDSMap.put("ACCT_HEAD", serTaxMap.get("KRISHIKALYAN_HEAD_ID"));
                                        MDSMap.put("TRANS_AMOUNT", krishikalyanCess);
                                        MDSMap.put("AUTHORIZEREMARKS", "SERVICE TAX");
                                        MDSMap.put("TRANS_MOD_TYPE", "MDS");
                                        MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                                        MDSMap.put("INSTRUMENT_NO2","SGST");// Added by nithya on 31-10-2019 for KD-680
                                        cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                                        //serTaxAmt -= krishikalyanCess;
                                    }
                                    if (normalServiceTax > 0) {
                                        MDSMap = new HashMap();
                                        MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                                        MDSMap.put("BRANCH_CODE", _branchCode);
                                        MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                        MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                                        MDSMap.put("PARTICULARS", prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " SERVICE TAX");
                                        MDSMap.put("ACCT_HEAD", serTaxMap.get("TAX_HEAD_ID"));
                                        MDSMap.put("TRANS_AMOUNT", normalServiceTax);
                                        MDSMap.put("AUTHORIZEREMARKS", "SERVICE TAX");
                                        MDSMap.put("TRANS_MOD_TYPE", "MDS");
                                        MDSMap.put("INSTRUMENT_NO2","SERVICE_TAX");// Added by nithya on 31-10-2019 for KD-680
                                        MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                                        cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                                    }
                            }
                    }                        
                      //  System.out.print("###### cashList: " + cashList);
                        ///DEFAULTER
                        //System.out.println("dddeeefaulterrr" + prizedMoneyPaymentTO.getDefaulter_marked());
                        if (prizedMoneyPaymentTO.getDefaulter_marked().equals("Y")) {
                            HashMap account_map = new HashMap();
                            account_map.put("CHITTAL_NO", prizedMoneyPaymentTO.getChittalNo());
                            List defaulter_acc_head = sqlMap.executeQueryForList("getAccounthead", account_map);
                            String bonusAcountHead = "";
                            String commissionAcountHead = "";
                            String interstAccountHead = "";
                            String RecieptAccountHead = "";
                            double defaulter_Inteset = 0.0;
                            double defaulter_bonus_recoverd = 0.0;
                            double defaulter_comm = 0.0;
                            String SuspensetAccountHead = "";
                            String Misclens = "";

                            if (defaulter_acc_head != null && defaulter_acc_head.size() > 0) {
                                account_map = (HashMap) defaulter_acc_head.get(0);
                                bonusAcountHead = account_map.get("BONUS_RECEIVABLE_HEAD").toString();
                                commissionAcountHead = account_map.get("COMMISION_HEAD").toString();
                                interstAccountHead = account_map.get("PENAL_INTEREST_HEAD").toString();
                                RecieptAccountHead = account_map.get("RECEIPT_HEAD").toString();
                                SuspensetAccountHead = account_map.get("SUSPENSE_HEAD").toString();
                                Misclens = account_map.get("MISCELLANEOUS_HEAD").toString();



                            }


                            if (prizedMoneyPaymentTO.getDefaulter_bonus_recoverd() > 0.0) {
                                defaulter_bonus_recoverd = prizedMoneyPaymentTO.getDefaulter_bonus_recoverd();  //AJITH Removed Double.parseDouble()
//                        if(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue()>0){
//                            HashMap MDSMap = new HashMap();
//                            MDSMap.put("SELECTED_BRANCH_ID", _branchCode);
//                            MDSMap.put("BRANCH_CODE", _branchCode);
//                            MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
//                            MDSMap.put("USER_ID",  prizedMoneyPaymentTO.getStatusBy());
//                            MDSMap.put("PARTICULARS",  prizedMoneyPaymentTO.getChittalNo()+"_"+prizedMoneyPaymentTO.getSubNo()+" RECOVER BONUS");
//                            MDSMap.put("ACCT_HEAD", bonusAcountHead);
//                            MDSMap.put("TRANS_AMOUNT",defaulter_bonus_recoverd);
//                            MDSMap.put("AUTHORIZEREMARKS","RECOVERD_BONUS");
//                            cashList.add(createCashTransactionTO(MDSMap));
//                        }
                            }



                            if (prizedMoneyPaymentTO.getDefaulter_comm() > 0.0) {
                                defaulter_comm = prizedMoneyPaymentTO.getDefaulter_comm();  //AJITH Removed Double.parseDouble()
//                        if(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue()>0)
//                        {
                                HashMap MDSMap = new HashMap();
                                MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                                MDSMap.put("BRANCH_CODE", _branchCode);
                                MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                                MDSMap.put("PARTICULARS", prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " RECOVER BONUS");
                                MDSMap.put("ACCT_HEAD", Misclens);
                                MDSMap.put("TRANS_AMOUNT", defaulter_comm);
                                MDSMap.put("AUTHORIZEREMARKS", "RECOVERD_BONUS");
                                MDSMap.put("TRANS_MOD_TYPE", "MDS");
                                MDSMap.put(CommonConstants.SCREEN,(String) map.get(CommonConstants.SCREEN));
                                cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
//                        }
                            }



                            if (prizedMoneyPaymentTO.getDefaulter_interst() > 0.0) {
                                defaulter_Inteset = prizedMoneyPaymentTO.getDefaulter_interst();  //AJITH Removed Double.parseDouble()
//                       {
                                HashMap MDSMap = new HashMap();
                                MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                                MDSMap.put("BRANCH_CODE", _branchCode);
                                MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                                MDSMap.put("PARTICULARS", prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " RECOVER BONUS");
                                MDSMap.put("ACCT_HEAD", interstAccountHead);
                                MDSMap.put("TRANS_AMOUNT", defaulter_Inteset);
                                MDSMap.put("AUTHORIZEREMARKS", "RECOVERD_BONUS");
                                MDSMap.put("TRANS_MOD_TYPE", "MDS");
                                MDSMap.put(CommonConstants.SCREEN,(String) map.get(CommonConstants.SCREEN));
                                cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
//                        }
                            }
                         //   System.out.print("###### cashList: " + cashList);
                            double paidamount = 0.0;
                            HashMap mdsMap = new HashMap();
                            mdsMap.put("CHITTAL_NO", prizedMoneyPaymentTO.getChittalNo());
                            mdsMap.put("SUB_NO", CommonUtil.convertObjToInt(prizedMoneyPaymentTO.getSubNo()));

                            List getpaidAmounts = sqlMap.executeQueryForList("getMdsPaidAmount", mdsMap);
                            if (getpaidAmounts != null && getpaidAmounts.size() > 0) {
                                mdsMap = new HashMap();
                                mdsMap = (HashMap) getpaidAmounts.get(0);
                                if (mdsMap != null && mdsMap.containsKey("TOTAL") && mdsMap.get("TOTAL") != null) {

                                    paidamount = Double.parseDouble(mdsMap.get("TOTAL").toString());
                                } else {
                                    paidamount = 0.0;
                                }
                            } else {
                                paidamount = 0.0;
                            }
                            double TotDet = CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue()
                                    + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue()
                                    + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue();
                         //   System.out.println("d....." + TotDet + "::" + paidamount);
                            double amountToReciept = TotDet - paidamount;

                            double bonusTogive = 0.0;
                            HashMap mdsbonusMap = new HashMap();
                            mdsbonusMap.put("CHITTAL_NO", prizedMoneyPaymentTO.getChittalNo());
                            mdsbonusMap.put("SUB_NO", CommonUtil.convertObjToInt(prizedMoneyPaymentTO.getSubNo()));
                            mdsbonusMap.put("SCHME_NAME", prizedMoneyPaymentTO.getSchemeName());

                            List payableBons = sqlMap.executeQueryForList("getPayableBonus", mdsbonusMap);
                            if (payableBons != null && payableBons.size() > 0) {
                                HashMap mapDts = new HashMap();
                                mapDts = (HashMap) payableBons.get(0);
                                if (mapDts != null) {
                                    bonusTogive = Double.parseDouble(mapDts.get("BONUS_PAYABLE").toString());

                                } else {
                                    bonusTogive = 0.0;
                                }
                            } else {
                                bonusTogive = 0.0;
                            }




                            if (bonusTogive > 0.0) {
////                               //double defaulter_Inteset=Double.parseDouble(prizedMoneyPaymentTO.getDefaulter_interst());
//////                        if(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue()>0)
////                        {
////                            transactionTO.setChequeNo("SERVICE_TAX");
////                         cashTransMap = new HashMap();
////                        //Total Amount (Prized Amount)
////                        cashTransMap.put("BRANCH_CODE",map.get("BRANCH_CODE"));
////                        cashTransMap.put("PENAL_DETAILS","PENAL_DETAILS");
////                        cashTransMap.put("LINK_BATCH_ID",prizedMoneyPaymentTO.getChittalNo());
////                        cashTransMap.put("PAYMENT_HEAD", applicationMap.get("PAYMENT_HEAD"));
////                        cashTransMap.put("MDS_PAYABLE_HEAD", applicationMap.get("BONUS_PAYABLE_HEAD"));
////                        cashTransMap.put("SCHEME_NAME",prizedMoneyPaymentTO.getSchemeName());
////                        cashTransMap.put("USER_ID",map.get("USER_ID"));
//////                        cashTransMap.put("PRIZED_AMOUNT",CommonUtil.convertObjToStr(transactionTO.getTransAmt()));
////                        cashTransMap.put("TOTAL_AMOUNT",bonusTogive);
////                        //Changed By Suresh
//////                        if(map.containsKey("MDS_CLOSURE")){
//////                            cashTransMap.put("MDS_CLOSURE","MDS_CLOSURE");
//////                        }
////                         cashTransactionDAO = new CashTransactionDAO();
////                         applncashList = newApplicationListBonus(cashTransMap,bonusTogive);
////                        cashTransMap.put("DAILYDEPOSITTRANSTO",applncashList);
////                        cashTransMap.put("SCREEN_NAME","MDS Payments");
////                         cashMap = cashTransactionDAO.execute(cashTransMap,false);
////                        prizedMoneyPaymentTO.setCashId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
////                        System.out.println("cashMapuuuu :"+cashMap);
////                            System.out.println("vvvvv"+cashMap.get("TRANS_ID"));
////                        
////                        //Insert Remit _Issue_Trans
////                        transactionTO.setBatchId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
////                        transactionTO.setBatchDt(currDt);
////                        transactionTO.setTransId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
////                        transactionTO.setStatus(prizedMoneyPaymentTO.getStatus());
////                        transactionTO.setBranchId(_branchCode);
////                        System.out.println("transactionTO-lll------------------>"+transactionTO);
////                        sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", transactionTO);
////                        //Update LinkBatchId as Trans_ID
////                         linkBatchMap = new HashMap();
////                        linkBatchMap.put("LINK_BATCH_ID", cashMap.get("TRANS_ID"));
////                        linkBatchMap.put("TRANS_ID",cashMap.get("TRANS_ID"));
////                        linkBatchMap.put("TRANS_DT",currDt);
////                        linkBatchMap.put("INITIATED_BRANCH",BRANCH_ID);
////                        sqlMap.executeUpdate("updateLinkBatchIdCash", linkBatchMap);
////                        linkBatchMap = null;
////                        }


                                HashMap MDSMap = new HashMap();
                                MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                                MDSMap.put("BRANCH_CODE", _branchCode);
                                MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                                MDSMap.put("PARTICULARS", prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " AMOUNT TO RECIPT:DEFAULTER");
                                MDSMap.put("ACCT_HEAD", bonusAcountHead);
                                MDSMap.put("TRANS_AMOUNT", bonusTogive);
                                MDSMap.put("AUTHORIZEREMARKS", "RECOVERD_MDSAMOUNT");
                                MDSMap.put("TRANS_MOD_TYPE", "MDS");
                                MDSMap.put(CommonConstants.SCREEN,(String) map.get(CommonConstants.SCREEN));
                                cashList.add(createCashTransactionTODefulter(MDSMap, generateSingleTransId));
                            }
                            double chittalPaid = paidamount;
                            paidamount = paidamount - (defaulter_Inteset + defaulter_bonus_recoverd + defaulter_comm);
                            if (paidamount > 0.0) {
                                //double defaulter_Inteset=Double.parseDouble(prizedMoneyPaymentTO.getDefaulter_interst());
//                        if(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue()>0)
                                {
                                    HashMap MDSMap = new HashMap();
                                    MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                                    MDSMap.put("BRANCH_CODE", _branchCode);
                                    MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                    MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                                    MDSMap.put("PARTICULARS", prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " AMOUNT TO RECIPT:DEFAULTER");
                                    MDSMap.put("ACCT_HEAD", SuspensetAccountHead);
                                    MDSMap.put("TRANS_AMOUNT", paidamount);
                                    MDSMap.put("AUTHORIZEREMARKS", "RECOVERD_MDSAMOUNT");
                                    MDSMap.put("TRANS_MOD_TYPE", "MDS");
                                    MDSMap.put(CommonConstants.SCREEN,(String) map.get(CommonConstants.SCREEN));
                                    cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                                }
                            }
//                        double =chittalPaid;


                            if (amountToReciept > 0.0) {
                                //double defaulter_Inteset=Double.parseDouble(prizedMoneyPaymentTO.getDefaulter_interst());
//                        if(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue()>0)
//                        {
                                HashMap MDSMap = new HashMap();
                                MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                                MDSMap.put("BRANCH_CODE", _branchCode);
                                MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                                MDSMap.put("PARTICULARS", prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " AMOUNT TO RECIPT:DEFAULTER");
                                MDSMap.put("ACCT_HEAD", RecieptAccountHead);
                                MDSMap.put("TRANS_AMOUNT", amountToReciept);
                                MDSMap.put("AUTHORIZEREMARKS", "RECOVERD_MDSAMOUNT");
                                MDSMap.put("TRANS_MOD_TYPE", "MDS");
                                MDSMap.put(CommonConstants.SCREEN,(String) map.get(CommonConstants.SCREEN));
                                cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
//                        }
                            }
                            TotDet = TotDet + bonusTogive;
                            double NtMislns = TotDet - (amountToReciept + paidamount + defaulter_Inteset + defaulter_comm + prizedMoneyPaymentTO.getCommisionAmount() + prizedMoneyPaymentTO.getBonusAmount());  //AJITH Removed Double.parseDouble()
                            //   Math.abs(TotDet);
                            System.out.println("NtMislns" + NtMislns);
                            if (NtMislns > 0.0) {
                                //double defaulter_Inteset=Double.parseDouble(prizedMoneyPaymentTO.getDefaulter_interst());
//                        if(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue()>0)
//                        {
                                System.out.println("NtMislns" + NtMislns);
                                HashMap MDSMap = new HashMap();
                                MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                                MDSMap.put("BRANCH_CODE", _branchCode);
                                MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                                MDSMap.put("PARTICULARS", prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " AMOUNT TO RECIPT:DEFAULTER");
                                MDSMap.put("ACCT_HEAD", Misclens);
                                MDSMap.put("TRANS_AMOUNT", NtMislns);
                                MDSMap.put("AUTHORIZEREMARKS", "RECOVERD_MDSAMOUNT");
                                MDSMap.put("TRANS_MOD_TYPE", "MDS");
                                MDSMap.put(CommonConstants.SCREEN,(String) map.get(CommonConstants.SCREEN));
                                cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
//                        }
                            } else if (NtMislns < 0.0) {
                                ExecptionDeftler = true;
                                throw new TTException("Cannot make Defaulter Payment !!!");
                            }


                        }
                        ///DEFAULTER
                        //Added by sreekrishnan FOR KOTTAYAM
                        if (!(map.containsKey("TRASFER_BONUS_COMMISION") && map.get("TRASFER_BONUS_COMMISION").equals("Y"))) {   
                            //Bonus
                            if (!(map.containsKey("AUCTION_TRANS") && map.get("AUCTION_TRANS").equals("Y"))) {//Added by sreekrishnan
                                if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue() > 0) {
                                    HashMap MDSMap = new HashMap();
                                    MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                                    MDSMap.put("BRANCH_CODE", _branchCode);
                                    MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                    MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                                    MDSMap.put("PARTICULARS", prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " BONUS");
                                    //Changed By Suresh
                                    if (map.containsKey("MDS_CLOSURE")) {
                                        MDSMap.put("ACCT_HEAD", applicationMap.get("SUNDRY_RECEIPT_HEAD"));
                                    } else {
                                        if (!(map.containsKey("PART_PAY") && map.get("PART_PAY").equals("Y"))) {
                                            MDSMap.put("ACCT_HEAD", applicationMap.get("BONUS_PAYABLE_HEAD"));
                                        }else{
                                            //MDSMap.put("ACCT_HEAD", applicationMap.get("BONUS_RECEIVABLE_HEAD"));// Done by nithya on 27-08-2019 for KD 575 - MDS Money Payment With Bonus Recovery Head Option.
                                            MDSMap.put("ACCT_HEAD", applicationMap.get("PART_PAY_BONUS_RECOVERY"));                                         
                                        }
                                    }
                                    MDSMap.put("TRANS_AMOUNT", prizedMoneyPaymentTO.getBonusAmount());
                                    MDSMap.put("AUTHORIZEREMARKS", "BONUS");
                                    MDSMap.put("TRANS_MOD_TYPE", "MDS");
                                    MDSMap.put(CommonConstants.SCREEN,(String) map.get(CommonConstants.SCREEN));
                                    MDSMap.put("INSTRUMENT_NO2","BONUS");// Added by nithya on 31-10-2019 for KD-680
                                    cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                                }
                            }
                        }
                        //Added by sreekrishnan for kottayam
                        if ((map.containsKey("TRASFER_BONUS_COMMISION") && map.get("TRASFER_BONUS_COMMISION").equals("Y"))) {  
                            transferBonusCommision(map,applicationMap,prizedMoneyPaymentTO,generateSingleTransId);
                        }
                        //
                      //  System.out.print("###### cashList: " + cashList);

                        //Notice
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNoticeAmount()).doubleValue() > 0) {
                            HashMap MDSMap = new HashMap();
                            MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                            MDSMap.put("BRANCH_CODE", _branchCode);
                            MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                            MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                            MDSMap.put("PARTICULARS", prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " NOTICE");
                            MDSMap.put("ACCT_HEAD", applicationMap.get("NOTICE_CHARGES_HEAD"));
                            MDSMap.put("TRANS_AMOUNT", prizedMoneyPaymentTO.getNoticeAmount());
                            MDSMap.put("AUTHORIZEREMARKS", "NOTICE");
                            MDSMap.put("TRANS_MOD_TYPE", "MDS");
                            MDSMap.put(CommonConstants.SCREEN,(String) map.get(CommonConstants.SCREEN));
                            MDSMap.put("INSTRUMENT_NO2","NOTICE_CHARGE");// Added by nithya on 31-10-2019 for KD-680
                            cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                        }
                       // System.out.print("###### cashList: " + cashList);

                        //charge
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getChargeAmount()).doubleValue() > 0) {
                            HashMap MDSMap = new HashMap();
                            MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                            MDSMap.put("BRANCH_CODE", _branchCode);
                            MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                            MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                            MDSMap.put("PARTICULARS", prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " CHARGE");
                            MDSMap.put("ACCT_HEAD", applicationMap.get("CHARGE_PAYMENT_HEAD"));
                            MDSMap.put("TRANS_AMOUNT", prizedMoneyPaymentTO.getChargeAmount());
                            MDSMap.put("AUTHORIZEREMARKS", "CHARGE");
                            MDSMap.put("TRANS_MOD_TYPE", "MDS");
                            MDSMap.put(CommonConstants.SCREEN,(String) map.get(CommonConstants.SCREEN));
                            MDSMap.put("INSTRUMENT_NO2","CHARGE_AMT");// Added by nithya on 31-10-2019 for KD-680
                            cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                        }
                       // System.out.print("###### cashList: " + cashList);

//                        //Arbitration
//                        if(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getAribitrationAmount()).doubleValue()>0){
//                            HashMap MDSMap = new HashMap();
//                            MDSMap.put("SELECTED_BRANCH_ID", _branchCode);
//                            MDSMap.put("BRANCH_CODE", _branchCode);
//                            MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
//                            MDSMap.put("USER_ID",  prizedMoneyPaymentTO.getStatusBy());
//                            MDSMap.put("PARTICULARS",  prizedMoneyPaymentTO.getChittalNo()+"_"+prizedMoneyPaymentTO.getSubNo()+" CASE EXPENSE");
//                            MDSMap.put("ACCT_HEAD", applicationMap.get("CASE_EXPENSE_HEAD"));
//                            MDSMap.put("TRANS_AMOUNT", prizedMoneyPaymentTO.getAribitrationAmount());
//                            MDSMap.put("AUTHORIZEREMARKS","CASE EXPENSE");
//                            cashList.add(createCashTransactionTO(MDSMap));
//                        }

                        //Case Expense Amount Transaction
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getAribitrationAmount()).doubleValue() > 0) {
                            System.out.println("Arbitration Started");
                            HashMap whereMap = new HashMap();
                            whereMap.put("ACT_NUM", CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()) + "_" + CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getSubNo()));
                            List chargeList = (List) sqlMap.executeQueryForList("getMDSCaseChargeDetails", whereMap);
                            if (chargeList != null && chargeList.size() > 0) {
                                applncashList  = new ArrayList();
                                for (int i = 0; i < chargeList.size(); i++) {
                                    double chargeAmount = 0.0;
                                    String chargeType = "";
                                    whereMap = (HashMap) chargeList.get(i);
                                    chargeAmount = CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
                                    Rounding rod = new Rounding();
                                    chargeAmount = (double) rod.getNearest((long) (chargeAmount * 100), 100) / 100;
                                    chargeType = CommonUtil.convertObjToStr(whereMap.get("CHARGE_TYPE"));
                                    if (chargeAmount > 0) {
                                        if (chargeType.equals("ARC Cost")) {
                                            chargeType = "ARC_COST";
                                        } else if (chargeType.equals("ARC Expense")) {
                                            chargeType = "ARC_EXPENSE";
                                        } else if (chargeType.equals("EA Cost")) {
                                            chargeType = "EA_COST";
                                        } else if (chargeType.equals("EA Expense")) {
                                            chargeType = "EA_EXPENSE";
                                        } else if (chargeType.equals("EP Cost")) {
                                            chargeType = "EP_COST";
                                        } else if (chargeType.equals("EP Expense")) {
                                            chargeType = "EP_EXPENSE";
                                        }
                                        System.out.println("###### chargeType Head : " + chargeType);
                                        System.out.println("###### chargeAmount : " + chargeAmount);
                                        HashMap MDSMap = new HashMap();
                                        MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                                        MDSMap.put("BRANCH_CODE", _branchCode);
                                        MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                        MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                                        MDSMap.put("PARTICULARS", prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " " + chargeType);
                                        MDSMap.put("ACCT_HEAD", applicationMap.get(chargeType));
                                        //MDSMap.put("ACCT_HEAD", applicationMap.get("CASE_EXPENSE_HEAD"));
                                        MDSMap.put("TRANS_AMOUNT", String.valueOf(chargeAmount));
                                        MDSMap.put("AUTHORIZEREMARKS", chargeType);
                                        MDSMap.put("TRANS_MOD_TYPE", "MDS");
                                        MDSMap.put(CommonConstants.SCREEN,(String) map.get(CommonConstants.SCREEN));
                                        MDSMap.put("INSTRUMENT_NO2",chargeType);// Added by nithya on 31-10-2019 for KD-680
                                        cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                                    }
                                }
                            }
                        }

                       // System.out.print("###### cashList: " + cashList);

                        //Discount
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getDiscountAmount()).doubleValue() > 0) {
                            HashMap MDSMap = new HashMap();
                            MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                            MDSMap.put("BRANCH_CODE", _branchCode);
                            MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                            MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                            MDSMap.put("PARTICULARS", prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " DISCOUNT");
                            MDSMap.put("ACCT_HEAD", applicationMap.get("DISCOUNT_HEAD"));
                            MDSMap.put("TRANS_AMOUNT", prizedMoneyPaymentTO.getDiscountAmount());
                            MDSMap.put("AUTHORIZEREMARKS", "DISCOUNT");
                            MDSMap.put("TRANS_MOD_TYPE", "MDS");
                            MDSMap.put(CommonConstants.SCREEN,(String) map.get(CommonConstants.SCREEN));
                            MDSMap.put("INSTRUMENT_NO2","DISCOUNT");// Added by nithya on 31-10-2019 for KD-680
                            cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                        }
                       // System.out.print("###### cashList: " + cashList);

                        //Defaulters
                        if (CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getDefaulters()).equals("Y")) {
                            //Penal
                            if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPenalAmount()).doubleValue() > 0) {
                                HashMap MDSMap = new HashMap();
                                MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                                MDSMap.put("BRANCH_CODE", _branchCode);
                                MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                                MDSMap.put("PARTICULARS", prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " PENAL");
                                MDSMap.put("ACCT_HEAD", applicationMap.get("PENAL_INTEREST_HEAD"));
                                MDSMap.put("TRANS_AMOUNT", prizedMoneyPaymentTO.getPenalAmount());
                                MDSMap.put("AUTHORIZEREMARKS", "PENAL");
                                MDSMap.put("TRANS_MOD_TYPE", "MDS");
                                MDSMap.put(CommonConstants.SCREEN,(String) map.get(CommonConstants.SCREEN));
                                MDSMap.put("INSTRUMENT_NO2","PENAL_AMT");// Added by nithya on 31-10-2019 for KD-680
                                cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                            }
                            //Over_Due
                            if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue() > 0) {
                                HashMap MDSMap = new HashMap();
                                MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                                MDSMap.put("BRANCH_CODE", _branchCode);
                                MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                                MDSMap.put("PARTICULARS", prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " OVER_DUE");
                                MDSMap.put("ACCT_HEAD", applicationMap.get("RECEIPT_HEAD"));
                                MDSMap.put("TRANS_AMOUNT", prizedMoneyPaymentTO.getOverdueAmount());
                                MDSMap.put("AUTHORIZEREMARKS", "OVER_DUE");
                                MDSMap.put("TRANS_MOD_TYPE", "MDS");
                                MDSMap.put(CommonConstants.SCREEN,(String) map.get(CommonConstants.SCREEN));
                                MDSMap.put("INSTRUMENT_NO2","OVERDUE_AMT");// Added by nithya on 31-10-2019 for KD-680
                                cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                            }
                        }
                       // System.out.print("###### cashList: " + cashList);
                        if (cashList != null && cashList.size() > 0) {
                            doCashTrans(cashList, _branchCode, false);
                        }
                       
                        // Added by nithya on 26-07-2018 for KD-179 Mds defaulters payment issue (deduction entries not inserting personal ledger )
                        if (CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getDefaulters()).equals("Y")) {
                            if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getChitalBonusAmt()).doubleValue() > 0) {
                                if (!(map.containsKey("TRASFER_BONUS_COMMISION") && map.get("TRASFER_BONUS_COMMISION").equals("Y"))) {
                                   transferChittalBonus(map, applicationMap, prizedMoneyPaymentTO, generateSingleTransId);
                                }
                            }
                         } 
                        
                    }
                }
            }
            String USERID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
            //    sqlMap.executeUpdate("insertReceiptEntryTO", prizedMoneyPaymentTO);
            //objLogDAO.addToLog(objLogTO);
            if (prizedMoneyPaymentTO.getCashId() != null && prizedMoneyPaymentTO.getCashId().length() > 0 ||
                    prizedMoneyPaymentTO.getTransId() != null && prizedMoneyPaymentTO.getTransId().length() > 0) {
                getTransNewDetails(prizedMoneyPaymentTO.getTransId(), prizedMoneyPaymentTO.getCashId(),map);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
            //  throw new TransRollbackException(e);
        }
    }
    
    
    // Added by nithya on 26-07-2018 for KD-179 Mds defaulters payment issue (deduction entries not inserting personal ledger )
    private void transferChittalBonus(HashMap map,HashMap applicationMap,MDSPrizedMoneyPaymentTO prizedTo,String singleTransId) throws Exception {
        try{  
            String defaulterBonusForfiet = "";
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
            System.out.println("bonusAmt#%#%%#%# trans "+prizedTo.getChitalBonusAmt());
            String chittalNo = CommonUtil.convertObjToStr(prizedTo.getChittalNo());
            String subNo = CommonUtil.convertObjToStr(prizedTo.getSubNo());
            if(map.containsKey("DEFAULTER_BONUS_FORFIET") && map.get("DEFAULTER_BONUS_FORFIET") != null){
               defaulterBonusForfiet = CommonUtil.convertObjToStr(map.get("DEFAULTER_BONUS_FORFIET")); 
            }
            if(CommonUtil.convertObjToDouble(prizedTo.getChitalBonusAmt()) >0){
                double serviceTax = 0.0;
                System.out.println("Bonus Started");
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                //txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("BONUS_PAYABLE_HEAD"));
                //txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("MDS_RECEIVABLE_HEAD"));
                 if (defaulterBonusForfiet.equalsIgnoreCase("Y")) {
                    txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("MDS_FORFEITED_ACCT_HEAD"));
                } else {
                    txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
                }
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                txMap.put(TransferTrans.PARTICULARS, "Chital Bonus " + applicationMap.get("MP_MDS_CODE") + "-" + chittalNo + "_" + subNo);
                txMap.put("TRANS_MOD_TYPE", "MDS");
                txMap.put("SCREEN_NAME", "MDS_MONEY_PAYMENT");
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedTo.getChitalBonusAmt()));
                transferTo.setInstrumentNo1("SI");
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransType(CommonConstants.CREDIT);
                transferTo.setProdType(TransactionFactory.GL);
                //transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("BONUS_PAYABLE_HEAD")));
                transferTo.setTransDt(currDt);
                transferTo.setBranchId(BRANCH_ID);
                transferTo.setInitiatedBranch(BRANCH_ID);
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(chittalNo));
                transferTo.setSingleTransId(CommonUtil.convertObjToStr(singleTransId));
                transactionTO.setChequeNo("SERVICE_TAX");
                transferTo.setInstrumentNo2("CHITAL_BONUS");// Added by nithya on 31-10-2019 for KD-680
                TxTransferTO.add(transferTo);

                System.out.println("debit Started");
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                //txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("PAYMENT_HEAD"));
                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("BONUS_RECEIVABLE_HEAD"));
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                txMap.put(TransferTrans.PARTICULARS, "Chittal Bonus" + applicationMap.get("MP_MDS_CODE") + "-" + chittalNo + "_" + subNo);
                txMap.put("TRANS_MOD_TYPE", "MDS");
                txMap.put("SCREEN_NAME", "MDS_MONEY_PAYMENT");
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(prizedTo.getChitalBonusAmt()));
                transferTo.setInstrumentNo1("SI");
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransType(CommonConstants.DEBIT);
                transferTo.setProdType(TransactionFactory.GL);
                //transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("PAYMENT_HEAD")));
                transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("BONUS_RECEIVABLE_HEAD")));//KD-3445
                transferTo.setTransDt(currDt);
                transferTo.setBranchId(BRANCH_ID);
                transferTo.setInitiatedBranch(BRANCH_ID);
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(chittalNo));
                transferTo.setSingleTransId(CommonUtil.convertObjToStr(singleTransId));
                transactionTO.setChequeNo("SERVICE_TAX");
                transferTo.setInstrumentNo2("CHITAL_BONUS");// Added by nithya on 31-10-2019 for KD-680
                TxTransferTO.add(transferTo);
                //
                HashMap applnMap = new HashMap();
                map.put("MODE", map.get("COMMAND"));
                map.put("COMMAND", map.get("MODE"));
                map.put("TxTransferTO", TxTransferTO);
                System.out.println("transferDAO List Last : " + map);
                HashMap transMap = transferDAO.execute(map, false);
                System.out.println("transferDAO List transMap : " + transMap);
                HashMap linkBatchMap = new HashMap();
                if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")
                        && transactionTO.getProductId().equals("") && !map.containsKey("LOCKER_SURRENDER_DAO")) {
                    linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
                } else if (!map.containsKey("LOCKER_SURRENDER_DAO")) {
                    linkBatchMap.put("LINK_BATCH_ID", transactionTO.getDebitAcctNo());
                } else {
                    linkBatchMap.put("LINK_BATCH_ID", map.get("LOCKER_SURRENDER_ID"));
                }
                linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
                linkBatchMap.put("BATCH_ID", transMap.get("TRANS_ID"));
                linkBatchMap.put("TRANS_DT", currDt);
                linkBatchMap.put("INITIATED_BRANCH", _branchCode);
                sqlMap.executeUpdate("updateLinkBatchIdTransfer", linkBatchMap);
                linkBatchMap = null;
                transMap = null;
            }
            
        }catch(Exception e){
            e.printStackTrace();
            sqlMap.rollbackTransaction();
        }        
    }
    
    
    
    private void transferBonusCommision(HashMap map,HashMap applicationMap,MDSPrizedMoneyPaymentTO prizedTo,String singleTransId) throws Exception {
        try{             
            String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
            String defaulterBonusForfiet = "";            
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
            System.out.println("bonusAmt#%#%%#%# trans "+prizedTo.getBonusAmount());
            String chittalNo = CommonUtil.convertObjToStr(prizedTo.getChittalNo());
            String subNo = CommonUtil.convertObjToStr(prizedTo.getSubNo());
            if(map.containsKey("DEFAULTER_BONUS_FORFIET") && map.get("DEFAULTER_BONUS_FORFIET") != null){
               defaulterBonusForfiet = CommonUtil.convertObjToStr(map.get("DEFAULTER_BONUS_FORFIET")); 
            }
            if(CommonUtil.convertObjToDouble(prizedTo.getBonusAmount()) +CommonUtil.convertObjToDouble(prizedTo.getCommisionAmount())>0){
                double serviceTax = 0.0;
                if (CommonUtil.convertObjToDouble(prizedTo.getBonusAmount()) > 0) {
                    System.out.println("Bonus Started");
                    transferTo = new TxTransferTO();
                    txMap = new HashMap();
                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                    txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("BONUS_PAYABLE_HEAD"));
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                    txMap.put(TransferTrans.PARTICULARS, "Bonus " + applicationMap.get("MP_MDS_CODE") + "-" + chittalNo + "_" + subNo);
                    txMap.put("TRANS_MOD_TYPE", "MDS");
                    txMap.put("SCREEN_NAME", "MDS_MONEY_PAYMENT");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedTo.getBonusAmount()));
                    transferTo.setInstrumentNo1("SI");
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setTransType(CommonConstants.CREDIT);
                    transferTo.setProdType(TransactionFactory.GL);
                    transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("BONUS_PAYABLE_HEAD")));
                    transferTo.setTransDt(currDt);
                    transferTo.setBranchId(BRANCH_ID);
                    transferTo.setInitiatedBranch(BRANCH_ID);
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
                    transferTo.setGlTransActNum(CommonUtil.convertObjToStr(chittalNo));
                    transferTo.setSingleTransId(CommonUtil.convertObjToStr(singleTransId));
                    transactionTO.setChequeNo("SERVICE_TAX");
                    transferTo.setInstrumentNo2("BONUS");// Added by nithya on 31-10-2019 for KD-680
                    TxTransferTO.add(transferTo);
                }            
            System.out.println("TxTransferTO List 4 : " + TxTransferTO);
            
            if (CommonUtil.convertObjToDouble(prizedTo.getCommisionAmount()) > 0) {
                System.out.println("commission Transaction Started");
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("COMMISION_HEAD"));
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                txMap.put(TransferTrans.PARTICULARS, "Commission " + applicationMap.get("MP_MDS_CODE") + "-" + chittalNo + "_" + subNo);
                txMap.put("TRANS_MOD_TYPE", "MDS");
                txMap.put("SCREEN_NAME", "MDS_MONEY_PAYMENT");
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedTo.getCommisionAmount()));
                transferTo.setInstrumentNo1("SI");
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransType(CommonConstants.CREDIT);
                transferTo.setProdType(TransactionFactory.GL);
                transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("COMMISION_HEAD")));
                transferTo.setTransDt(currDt);
                transferTo.setBranchId(BRANCH_ID);
                transferTo.setInitiatedBranch(BRANCH_ID);
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(chittalNo));
                transferTo.setSingleTransId(CommonUtil.convertObjToStr(singleTransId));
                transactionTO.setChequeNo("SERVICE_TAX");
                transferTo.setInstrumentNo2("COMMISION");// Added by nithya on 31-10-2019 for KD-680
                TxTransferTO.add(transferTo);
                System.out.println("transferTo List 6 : commission" + TxTransferTO);
                
//                  if (map.containsKey("serviceTaxDetails")) {
//                    HashMap serTaxMap = (HashMap) map.get("serviceTaxDetails");
//                    transferTo = new TxTransferTO();
//                    txMap = new HashMap();
//                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
//                    txMap.put(TransferTrans.CR_AC_HD, serTaxMap.get("TAX_HEAD_ID"));
//                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
//                    txMap.put(TransferTrans.PARTICULARS, "Service Tax  "+prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo());
//                    txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
//                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                    
//                    txMap.put("TRANS_MOD_TYPE", "MDS");
//                    txMap.put("SCREEN_NAME", "MDS_MONEY_PAYMENT");
//                    serviceTax = CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT"));
//                    transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT")));
//                    transferTo.setTransId("-");
//                    transferTo.setBatchId("-");
//                    transferTo.setInitiatedBranch(BRANCH_ID);
//                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                    transferTo.setSingleTransId(generateSingleTransId);
//                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
//                    TxTransferTO.add(transferTo);
//                    transactionTO.setChequeNo("SERVICE_TAX");
//                    transferTo.setSingleTransId(generateSingleTransId);
//                    transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
//                    TxTransferTO.add(transferTo);                    
//                    System.out.println("transferDAO List serviceTaxDetails : " + TxTransferTO);  
//                } 
            }       
            //Debit
                System.out.println("debit Started");
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("PAYMENT_HEAD"));
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                txMap.put(TransferTrans.PARTICULARS, "Bonus And Commission " + applicationMap.get("MP_MDS_CODE") + "-" + chittalNo + "_" + subNo);
                txMap.put("TRANS_MOD_TYPE", "MDS");
                txMap.put("SCREEN_NAME", "MDS_MONEY_PAYMENT");
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(prizedTo.getBonusAmount()) +CommonUtil.convertObjToDouble(prizedTo.getCommisionAmount()) + serviceTax);
                transferTo.setInstrumentNo1("SI");
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransType(CommonConstants.DEBIT);
                transferTo.setProdType(TransactionFactory.GL);
                transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("PAYMENT_HEAD")));
                transferTo.setTransDt(currDt);
                transferTo.setBranchId(BRANCH_ID);
                transferTo.setInitiatedBranch(BRANCH_ID);
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(chittalNo));
                transferTo.setSingleTransId(CommonUtil.convertObjToStr(singleTransId));
                transactionTO.setChequeNo("SERVICE_TAX");
                transferTo.setInstrumentNo2("BONUS_COMMISION");// Added by nithya on 31-10-2019 for KD-680
                TxTransferTO.add(transferTo);
                //
               
                // Added by nithya on 26-07-2018 for KD-179 Mds defaulters payment issue (deduction entries not inserting personal ledger )               
                if (CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getDefaulters()).equals("Y")) {
                    if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getChitalBonusAmt()).doubleValue() > 0) {
                        System.out.println("Bonus Started");
                        transferTo = new TxTransferTO();
                        txMap = new HashMap();
                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
                        //txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("BONUS_PAYABLE_HEAD"));
                        //txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("MDS_RECEIVABLE_HEAD"));
                         if (defaulterBonusForfiet.equalsIgnoreCase("Y")) {
                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("MDS_FORFEITED_ACCT_HEAD"));
                        } else {
                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
                        }
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                        txMap.put(TransferTrans.PARTICULARS, "Chital Bonus " + applicationMap.get("MP_MDS_CODE") + "-" + chittalNo + "_" + subNo);
                        txMap.put("TRANS_MOD_TYPE", "MDS");
                        txMap.put("SCREEN_NAME", "MDS_MONEY_PAYMENT");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedTo.getChitalBonusAmt()));
                        transferTo.setInstrumentNo1("SI");
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setTransType(CommonConstants.CREDIT);
                        transferTo.setProdType(TransactionFactory.GL);
                        //transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("BONUS_PAYABLE_HEAD")));
                        transferTo.setTransDt(currDt);
                        transferTo.setBranchId(BRANCH_ID);
                        transferTo.setInitiatedBranch(BRANCH_ID);
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
                        transferTo.setGlTransActNum(CommonUtil.convertObjToStr(chittalNo));
                        transferTo.setSingleTransId(CommonUtil.convertObjToStr(singleTransId));
                        transactionTO.setChequeNo("SERVICE_TAX");
                        transferTo.setInstrumentNo2("CHITAL_BONUS");// Added by nithya on 31-10-2019 for KD-680
                        TxTransferTO.add(transferTo);

                        System.out.println("debit Started");
                        transferTo = new TxTransferTO();
                        txMap = new HashMap();
                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
                        //txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("PAYMENT_HEAD"));
                        txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("BONUS_RECEIVABLE_HEAD"));
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                        txMap.put(TransferTrans.PARTICULARS, "Chittal Bonus" + applicationMap.get("MP_MDS_CODE") + "-" + chittalNo + "_" + subNo);
                        txMap.put("TRANS_MOD_TYPE", "MDS");
                        txMap.put("SCREEN_NAME", "MDS_MONEY_PAYMENT");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(prizedTo.getChitalBonusAmt()));
                        transferTo.setInstrumentNo1("SI");
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setTransType(CommonConstants.DEBIT);
                        transferTo.setProdType(TransactionFactory.GL);
                        transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("PAYMENT_HEAD")));
                        transferTo.setTransDt(currDt);
                        transferTo.setBranchId(BRANCH_ID);
                        transferTo.setInitiatedBranch(BRANCH_ID);
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
                        transferTo.setGlTransActNum(CommonUtil.convertObjToStr(chittalNo));
                        transferTo.setSingleTransId(CommonUtil.convertObjToStr(singleTransId));
                        transactionTO.setChequeNo("SERVICE_TAX");
                        transferTo.setInstrumentNo2("CHITAL_BONUS");// Added by nithya on 31-10-2019 for KD-680
                        TxTransferTO.add(transferTo);
                    }
                }
            
                HashMap applnMap = new HashMap();
                map.put("MODE", map.get("COMMAND"));
                map.put("COMMAND", map.get("MODE"));
                map.put("TxTransferTO", TxTransferTO);
                System.out.println("transferDAO List Last : " + map);
                HashMap transMap = transferDAO.execute(map, false);
                System.out.println("transferDAO List transMap : " + transMap);    
                HashMap linkBatchMap = new HashMap();
                if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")
                    && transactionTO.getProductId().equals("") && !map.containsKey("LOCKER_SURRENDER_DAO")) {
                    linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
                } else if (!map.containsKey("LOCKER_SURRENDER_DAO")) {
                    linkBatchMap.put("LINK_BATCH_ID", transactionTO.getDebitAcctNo());
                } else {
                    linkBatchMap.put("LINK_BATCH_ID", map.get("LOCKER_SURRENDER_ID"));
                }
                linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
                linkBatchMap.put("BATCH_ID", transMap.get("TRANS_ID"));
                linkBatchMap.put("TRANS_DT", currDt);
                linkBatchMap.put("INITIATED_BRANCH", _branchCode);
                sqlMap.executeUpdate("updateLinkBatchIdTransfer", linkBatchMap);
                linkBatchMap = null;
                transMap = null;
            }
            
        }catch(Exception e){
            e.printStackTrace();
            sqlMap.rollbackTransaction();
        }
        
    }

    private void doCashTrans(ArrayList batchList, String branchCode, boolean isAutoAuthorize) throws Exception {
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
        data.put("SCREEN_NAME", "MDS Payments");
        if (isAutoAuthorize) {
            HashMap authorizeMap = new HashMap();
            authorizeMap.put("BATCH_ID", null);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
            data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
        }
        HashMap cashTransMap = cashDAO.execute(data, false);
        prizedMoneyPaymentTO.setTransId(CommonUtil.convertObjToStr(cashTransMap.get("TRANS_ID")));
        System.out.println("########### cashTransMap" + cashTransMap);

        //Update LinkBatchId as Trans_ID
        HashMap linkBatchMap = new HashMap();
        linkBatchMap.put("LINK_BATCH_ID", cashTransMap.get("TRANS_ID"));
        linkBatchMap.put("TRANS_ID", cashTransMap.get("TRANS_ID"));
        linkBatchMap.put("TRANS_DT", currDt);
        linkBatchMap.put("INITIATED_BRANCH", _branchCode);
        sqlMap.executeUpdate("updateLinkBatchIdCash", linkBatchMap);
        linkBatchMap = null;
    }

    private CashTransactionTO createCashTransactionTO(HashMap dataMap, String generateSingleTransId) throws Exception {
        CashTransactionTO objCashTO = new CashTransactionTO();
        if (dataMap.get("STATUS").equals(CommonConstants.TOSTATUS_INSERT)) {
            objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
            objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
            objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.GL));
            objCashTO.setTransType(CommonConstants.CREDIT);
            objCashTO.setParticulars("By Cash : " + dataMap.get("PARTICULARS"));
            objCashTO.setAuthorizeRemarks(CommonUtil.convertObjToStr(dataMap.get("AUTHORIZEREMARKS")));
            objCashTO.setInpAmount(CommonUtil.convertObjToDouble(dataMap.get("TRANS_AMOUNT")));
            objCashTO.setAmount(CommonUtil.convertObjToDouble(dataMap.get("TRANS_AMOUNT")));
            objCashTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            objCashTO.setBranchId(CommonUtil.convertObjToStr(dataMap.get("SELECTED_BRANCH_ID")));
            objCashTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            objCashTO.setStatusDt(currDt);
            objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
            //added by sreekrishnan for cashier
            List listData = ServerUtil.executeQuery("getCashierAuth", new HashMap());
            if (listData != null && listData.size() > 0) {
                HashMap map1 = (HashMap) listData.get(0);
                if(map1.get("CASHIER_AUTH_ALLOWED") != null && map1.get("CASHIER_AUTH_ALLOWED").toString().equals("Y") && objCashTO.getTransType().equals(CommonConstants.CREDIT)){
                    objCashTO.setAuthorizeStatus_2(null);
                }else
                    objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            }else{
                objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            }
            objCashTO.setSingleTransId(generateSingleTransId);
            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
            objCashTO.setCommand("INSERT");
            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
            if (dataMap.containsKey(CommonConstants.SCREEN) && dataMap.get(CommonConstants.SCREEN) != null) {
                objCashTO.setScreenName((String) dataMap.get(CommonConstants.SCREEN));
            }
            if(dataMap.containsKey("INSTRUMENT_NO2") && dataMap.get("INSTRUMENT_NO2") != null){// Added by nithya on 31-10-2019 for KD-680
                objCashTO.setInstrumentNo2(CommonUtil.convertObjToStr(dataMap.get("INSTRUMENT_NO2")));
            }
            objCashTO.setIbrHierarchy(CommonUtil.convertObjToStr(ibrHierarchy++));
            System.out.println("objCashTO 1st one:" + objCashTO);
        }
        return objCashTO;
    }

    private CashTransactionTO createCashTransactionTODefulter(HashMap dataMap, String generateSingleTransId) throws Exception {
        CashTransactionTO objCashTO = new CashTransactionTO();
        if (dataMap.get("STATUS").equals(CommonConstants.TOSTATUS_INSERT)) {
            objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
            objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
            objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.GL));
            objCashTO.setTransType(CommonConstants.DEBIT);
            objCashTO.setParticulars("By Cash : " + dataMap.get("PARTICULARS"));
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
            objCashTO.setSingleTransId(generateSingleTransId);
            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
            objCashTO.setCommand("INSERT");
            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
            objCashTO.setIbrHierarchy(CommonUtil.convertObjToStr(ibrHierarchy++));
            if (dataMap.containsKey(CommonConstants.SCREEN) && dataMap.get(CommonConstants.SCREEN) != null) {
                objCashTO.setScreenName((String) dataMap.get(CommonConstants.SCREEN));
            }
            System.out.println("objCashTO 1st one:" + objCashTO);
        }
        return objCashTO;
    }

    private void insertTransactionData(HashMap map) throws Exception {
        try {
            prizedMoneyPaymentTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
            prizedMoneyPaymentTO.setStatus(CommonConstants.STATUS_CREATED);
            System.out.println("###### insertTransactionData : " + map);
            ArrayList transferList = new ArrayList();
            TransferTrans transferTrans = new TransferTrans();
            TxTransferTO transferTo = new TxTransferTO();
            ArrayList TxTransferTO = new ArrayList();
            TransactionTO transactionTO = new TransactionTO();
            HashMap txMap = new HashMap();
            HashMap transactionListMap = new HashMap();
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
                HashMap applicationMap = new HashMap();
                applicationMap.put("SCHEME_NAME", prizedMoneyPaymentTO.getSchemeName());
                List lst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", applicationMap);
                if (lst != null && lst.size() > 0) {
                    applicationMap = (HashMap) lst.get(0);
                }
                if (transactionTO.getTransType().equals("TRANSFER") || transactionTO.getTransType().equals("CASH")) {
                    String linkBatchId = "";
                    if (transactionTO.getDebitAcctNo() != null && transactionTO.getDebitAcctNo().length() > 0) {
                        linkBatchId = CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo());
                    }
                    HashMap transMap = new HashMap();
                    HashMap operativeMap = new HashMap();
                    String value = CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo());
                    List lstOA = sqlMap.executeQueryForList("getAccountClosingHeads", value);
                    if (lstOA != null && lstOA.size() > 0) {
                        operativeMap = (HashMap) lstOA.get(0);
                    }
                    HashMap debitMap = new HashMap();
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("OA")) {
                        debitMap.put("ACT_NUM", transactionTO.getDebitAcctNo());
                        lst = sqlMap.executeQueryForList("getAccNoProdIdDet", debitMap);
                        if (lst != null && lst.size() > 0) {
                            debitMap = (HashMap) lst.get(0);
                        }
                    }
                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                    System.out.println("###### insertTransactionData prizedMoneyPaymentTO : " + prizedMoneyPaymentTO);
                    if (transactionTO.getTransType().equals("TRANSFER")) {
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue()
                                + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue() > 0) {
                            txMap = new HashMap();
                            //Changed By Suresh
                            if (map.containsKey("MDS_CLOSURE")) {
                                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("MDS_PAYABLE_HEAD"));
                            } else {
                                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("PAYMENT_HEAD"));
                            }
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " PAYMENT");
                            txMap.put(TransferTrans.DR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            System.out.println("txMap : " + txMap + "amt :" + (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue()
                                    + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue()));
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue()
                                    + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue()));
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            //                            if(!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")){
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                            //                            }
                            
                            TxTransferTO.add(transferTo);
                        }
                        System.out.println("transferTo List 1 : " + transferTo);
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()).doubleValue() > 0) {
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL") && transactionTO.getProductId().equals("")) {
                                txMap.put(TransferTrans.CR_AC_HD, (String) transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("OA")) {
                                txMap.put(TransferTrans.CR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                                txMap.put(TransferTrans.CR_ACT_NUM, transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.CR_PROD_ID, transactionTO.getProductId());
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                            }
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo());
                            txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()).doubleValue());
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            //                            if(!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")){
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                            //                            }
                            System.out.println("transferTo List 2 : " + transferTo);
                            TxTransferTO.add(transferTo);
                            transactionTO.setChequeNo("SERVICE_TAX");
                            TxTransferTO.add(transferTo);
                        }
                        System.out.println("transferTo List 1 : " + transferTo);
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue() > 0) {
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("COMMISION_HEAD"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " COMMISION");
                            txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue());
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                            TxTransferTO.add(transferTo);
                            transactionTO.setChequeNo("SERVICE_TAX");
                            TxTransferTO.add(transferTo);
						//added by chithra for service Tax
                            if (map.containsKey("serviceTaxDetails")) {
                                System.out.println("serviceTaxDetails 6");
                                HashMap serTaxMap = (HashMap) map.get("serviceTaxDetails");
                                transferTo = new TxTransferTO();
                                double swachhCess = 0.0;
                                double krishikalyanCess = 0.0;
                                double serTaxAmt = 0.0;
                                double normalServiceTax = 0.0;
                                if (serTaxMap.containsKey("TOT_TAX_AMT") && serTaxMap.get("TOT_TAX_AMT") != null) {
                                    serTaxAmt = CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT"));
                                }
                                if (serTaxMap.containsKey(ServiceTaxCalculation.SWACHH_CESS) && serTaxMap.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                    swachhCess = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.SWACHH_CESS));
                                }
                                if (serTaxMap.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && serTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                    krishikalyanCess = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                                }
                                if (serTaxMap.containsKey(ServiceTaxCalculation.SERVICE_TAX) && serTaxMap.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                normalServiceTax = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.SERVICE_TAX));
                            }
                              //  serTaxAmt -= (swachhCess + krishikalyanCess);
                                if (swachhCess > 0) {
                                    txMap = new HashMap();
                                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.CR_AC_HD, serTaxMap.get("SWACHH_HEAD_ID"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " CGST");
                                    txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                    txMap.put("TRANS_MOD_TYPE", "MDS");
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, swachhCess);
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setInitiatedBranch(BRANCH_ID);
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    TxTransferTO.add(transferTo);
                                    transactionTO.setChequeNo("CGST");
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    TxTransferTO.add(transferTo);
                                   // serTaxAmt -= swachhCess;
                                }
                                if (krishikalyanCess > 0) {
                                    txMap = new HashMap();
                                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.CR_AC_HD, serTaxMap.get("KRISHIKALYAN_HEAD_ID"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " SGST");
                                    txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                    txMap.put("TRANS_MOD_TYPE", "MDS");
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, krishikalyanCess);
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setInitiatedBranch(BRANCH_ID);
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    TxTransferTO.add(transferTo);
                                    transactionTO.setChequeNo("SGST");
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    TxTransferTO.add(transferTo);
                                  //  serTaxAmt -= krishikalyanCess;
                                }
                                if (normalServiceTax > 0) {
                                    txMap = new HashMap();
                                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.CR_AC_HD, serTaxMap.get("TAX_HEAD_ID"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " SERVICE TAX");
                                    txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                    txMap.put("TRANS_MOD_TYPE", "MDS");
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax);
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setInitiatedBranch(BRANCH_ID);
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    TxTransferTO.add(transferTo);
                                    transactionTO.setChequeNo("SERVICE_TAX");
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    TxTransferTO.add(transferTo);
                                }
                            }
                        }
                        System.out.println("transferTo List 2 : " + transferTo);
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue() > 0) {
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            //Changed By Suresh
                            if (map.containsKey("MDS_CLOSURE")) {
                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("SUNDRY_RECEIPT_HEAD"));
                            } else {
                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("BONUS_PAYABLE_HEAD"));
                            }
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " BONUS");
                            txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue());
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                            System.out.println("transferTo List 2 : " + transferTo);
                            TxTransferTO.add(transferTo);
                            transactionTO.setChequeNo("SERVICE_TAX");
                            TxTransferTO.add(transferTo);
                        }
                        System.out.println("transferTo List 3 : " + transferTo);
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNoticeAmount()).doubleValue() > 0) {
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("NOTICE_CHARGES_HEAD"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " NOTICE");
                            txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNoticeAmount()).doubleValue());
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNoticeAmount()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                            TxTransferTO.add(transferTo);
                        }
                        System.out.println("transferTo List 4 : " + transferTo);
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getAribitrationAmount()).doubleValue() > 0) {
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("CASE_EXPENSE_HEAD"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " CASE EXPENSE");
                            txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getAribitrationAmount()).doubleValue());
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getAribitrationAmount()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                            TxTransferTO.add(transferTo);
                            transactionTO.setChequeNo("SERVICE_TAX");
                        }
                        System.out.println("transferTo List 5 : " + transferTo);
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getDiscountAmount()).doubleValue() > 0) {
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("DISCOUNT_HEAD"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " DISCOUNT");
                            txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getDiscountAmount()).doubleValue());
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getDiscountAmount()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                            TxTransferTO.add(transferTo);
                            transactionTO.setChequeNo("SERVICE_TAX");
                        }
                        // Changed By Suresh
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getRefundAmount()).doubleValue() > 0) {   //Cr Refund_Amount
                            System.out.print("####### Refund Amount : " + prizedMoneyPaymentTO.getRefundAmount());
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            if (CommonUtil.convertObjToStr(applicationMap.get("SUSPENSE_GL_ACCNO")).equals("GL")) {
                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("SUSPENSE_HEAD"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            } else {
                                HashMap suspenseMap = new HashMap();                // Suspense Acc Head
                                suspenseMap.put("PROD_ID", applicationMap.get("SUSPENSE_PROD_ID"));
                                List suspenseLst = sqlMap.executeQueryForList("getAccountHeadProdSA", suspenseMap);
                                if (suspenseLst != null && suspenseLst.size() > 0) {
                                    suspenseMap = (HashMap) suspenseLst.get(0);
                                    txMap.put(TransferTrans.CR_AC_HD, suspenseMap.get("AC_HEAD"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.SUSPENSE);
                                    txMap.put(TransferTrans.CR_PROD_ID, applicationMap.get("SUSPENSE_PROD_ID"));
                                    txMap.put(TransferTrans.CR_ACT_NUM, applicationMap.get("SUSPENSE_ACC_NO"));
                                }
                            }
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " REFUND");
                            txMap.put(TransferTrans.CR_BRANCH, map.get(("SELECTED_BRANCH_ID")));
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getRefundAmount()).doubleValue());
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getRefundAmount()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                            TxTransferTO.add(transferTo);
                            transactionTO.setChequeNo("SERVICE_TAX");
                        }
                        System.out.println("####### $$$$$$ " + map.get("REFUND_AMOUNT"));
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getRefundAmount()).doubleValue() > 0 || map.containsKey("REFUND_AMOUNT")) {  //Cr Reverse_Bonus_Amount
                            double earnedBonus = 0.0;
                            if (applicationMap.get("BONUS_EXISTING_CHITTAL") != null && !applicationMap.get("BONUS_EXISTING_CHITTAL").equals("") && (applicationMap.get("BONUS_EXISTING_CHITTAL").equals("Y"))) {
                                HashMap hashMap = new HashMap();
                                hashMap.put("CHITTAL_NO", prizedMoneyPaymentTO.getChittalNo());
                                List paidList = sqlMap.executeQueryForList("getTotalInstAmount", hashMap);
                                if (paidList != null && paidList.size() > 0) {
                                    hashMap = (HashMap) paidList.get(0);
                                    earnedBonus = CommonUtil.convertObjToDouble(hashMap.get("PAID_AMT")).doubleValue() - CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getRefundAmount()).doubleValue();
                                }
                                if (earnedBonus > 0) {
                                    System.out.print("####### Reverse Bonus Amount : " + earnedBonus);
                                    transferTo = new TxTransferTO();
                                    txMap = new HashMap();
                                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    //Changed By Suresh
                                    if (map.containsKey("MDS_CLOSURE")) {
                                        txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("SUNDRY_RECEIPT_HEAD"));
                                    } else {
                                        txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("BONUS_PAYABLE_HEAD"));
                                    }
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " REVERSE_BONUS");
                                    txMap.put(TransferTrans.CR_BRANCH,map.get("SELECTED_BRANCH_ID"));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, earnedBonus);
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setInitiatedBranch(BRANCH_ID);
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                    TxTransferTO.add(transferTo);
                                    transactionTO.setChequeNo("SERVICE_TAX");
                                }
                            }
                        }

                        HashMap applnMap = new HashMap();
                        transferDAO = new TransferDAO();
                        map.put("MODE", map.get("COMMAND"));
                        map.put("COMMAND", map.get("MODE"));
                        map.put("TxTransferTO", TxTransferTO);
                        transMap = transferDAO.execute(map, false);
                        prizedMoneyPaymentTO.setTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                        System.out.println("transactionDAO map : " + map);
                        transactionDAO.setBatchId(prizedMoneyPaymentTO.getTransId());
                        transactionDAO.setBatchDate(currDt);
                        transactionDAO.execute(map);
                        HashMap linkBatchMap = new HashMap();
                        if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")
                                && transactionTO.getProductId().equals("") && !map.containsKey("LOCKER_SURRENDER_DAO")) {
                            linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
                        } else if (!map.containsKey("LOCKER_SURRENDER_DAO")) {
                            linkBatchMap.put("LINK_BATCH_ID", transactionTO.getDebitAcctNo());
                        } else {
                            linkBatchMap.put("LINK_BATCH_ID", map.get("LOCKER_SURRENDER_ID"));
                        }
                        linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
                        linkBatchMap.put("BATCH_ID", transMap.get("TRANS_ID"));
                        linkBatchMap.put("TRANS_DT", currDt);
                        linkBatchMap.put("INITIATED_BRANCH", _branchCode);
                        sqlMap.executeUpdate("updateLinkBatchIdTransfer", linkBatchMap);
                        linkBatchMap = null;
                        transMap = null;
                    } else if (transactionTO.getTransType().equals("CASH")) {
                        transactionTO.setChequeNo("SERVICE_TAX");
                        HashMap cashTransMap = new HashMap();
                        cashTransMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                        cashTransMap.put("PENAL_DETAILS", "PENAL_DETAILS");
                        cashTransMap.put("LINK_BATCH_ID", prizedMoneyPaymentTO.getChittalNo());
                        cashTransMap.put("PAYMENT_HEAD", applicationMap.get("PAYMENT_HEAD"));
                        cashTransMap.put("MDS_PAYABLE_HEAD", applicationMap.get("MDS_PAYABLE_HEAD"));
                        cashTransMap.put("SCHEME_NAME", prizedMoneyPaymentTO.getSchemeName());
                        cashTransMap.put("USER_ID", map.get("USER_ID"));
                        //Changed By Suresh
                        if (map.containsKey("MDS_CLOSURE")) {
                            cashTransMap.put("MDS_CLOSURE", "MDS_CLOSURE");
                        }
                        CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                        ArrayList applncashList = applicationList(cashTransMap);
                        cashTransMap.put("DAILYDEPOSITTRANSTO", applncashList);
                        cashTransMap.put("SCREEN_NAME", "MDS Payments");
                        HashMap cashMap = cashTransactionDAO.execute(cashTransMap, false);
                        prizedMoneyPaymentTO.setCashId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                        System.out.println("cashMap :" + cashMap);
                        String command = CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getCommand());
                        transactionTO.setBatchId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                        transactionTO.setBatchDt(prizedMoneyPaymentTO.getStatusDt());
                        map.put("MODE", "INSERT");
                        if (((CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue())
                                + (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue())
                                + (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNoticeAmount()).doubleValue())
                                + (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getAribitrationAmount()).doubleValue())
                                + (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getDiscountAmount()).doubleValue())) > 0) {
                            txMap = new HashMap();
                            //Changed By Suresh
                            if (map.containsKey("MDS_CLOSURE")) {
                                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("MDS_PAYABLE_HEAD"));
                            } else {
                                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("PAYMENT_HEAD"));
                            }
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " PAYMENT");
                            txMap.put(TransferTrans.DR_BRANCH,map.get("SELECTED_BRANCH_ID"));
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            System.out.println("txMap : " + txMap + "amt :" + (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue())
                                    + (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue())
                                    + (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNoticeAmount()).doubleValue())
                                    + (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getAribitrationAmount()).doubleValue())
                                    + (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getDiscountAmount()).doubleValue())
                                    + (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPenalAmount()).doubleValue()));
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue())
                                    + (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue())
                                    + (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNoticeAmount()).doubleValue())
                                    + (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getAribitrationAmount()).doubleValue())
                                    + (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getDiscountAmount()).doubleValue())
                                    + (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPenalAmount()).doubleValue()));
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                            
                            TxTransferTO.add(transferTo);
                        }
                        System.out.println("transferTo List 1 : " + transferTo);
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue() > 0) {
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("COMMISION_HEAD"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " COMMISION");
                            txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                            System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue());
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                            TxTransferTO.add(transferTo);
                            transactionTO.setChequeNo("SERVICE_TAX");
                            
                            TxTransferTO.add(transferTo);
						//added by chithra for service Tax
                            if (map.containsKey("serviceTaxDetails")) {
                                System.out.println("serviceTaxDetails 7");
                                HashMap serTaxMap = (HashMap) map.get("serviceTaxDetails");
                                transferTo = new TxTransferTO();
                                double swachhCess = 0.0;
                                double krishikalyanCess = 0.0;
                                double serTaxAmt = 0.0;
                                double normalServiceTax = 0.0;
                                if (serTaxMap.containsKey("TOT_TAX_AMT") && serTaxMap.get("TOT_TAX_AMT") != null) {
                                    serTaxAmt = CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT"));
                                }
                                if (serTaxMap.containsKey(ServiceTaxCalculation.SWACHH_CESS) && serTaxMap.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                    swachhCess = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.SWACHH_CESS));
                                }
                                if (serTaxMap.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && serTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                    krishikalyanCess = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                                }
                                 if (serTaxMap.containsKey(ServiceTaxCalculation.SERVICE_TAX) && serTaxMap.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                normalServiceTax = CommonUtil.convertObjToDouble(serTaxMap.get(ServiceTaxCalculation.SERVICE_TAX));
                            }
                              //  serTaxAmt -= (swachhCess + krishikalyanCess);
                                if (swachhCess > 0) {
                                    txMap = new HashMap();
                                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.CR_AC_HD, serTaxMap.get("SWACHH_HEAD_ID"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " CGST");
                                    txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                    txMap.put("TRANS_MOD_TYPE", "MDS");
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, swachhCess);
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setInitiatedBranch(BRANCH_ID);
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    TxTransferTO.add(transferTo);
                                    transactionTO.setChequeNo("CGST");
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    TxTransferTO.add(transferTo);
                                   // serTaxAmt -= swachhCess;
                                }
                                if (krishikalyanCess > 0) {
                                    txMap = new HashMap();
                                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.CR_AC_HD, serTaxMap.get("KRISHIKALYAN_HEAD_ID"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " SGST");
                                    txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                    txMap.put("TRANS_MOD_TYPE", "MDS");
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, krishikalyanCess);
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setInitiatedBranch(BRANCH_ID);
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    TxTransferTO.add(transferTo);
                                    transactionTO.setChequeNo("SGST");
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    TxTransferTO.add(transferTo);
                                   // serTaxAmt -= krishikalyanCess;
                                }
                                if (normalServiceTax > 0) {
                                    txMap = new HashMap();
                                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.CR_AC_HD, serTaxMap.get("TAX_HEAD_ID"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " SERVICE TAX");
                                    txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                    txMap.put("TRANS_MOD_TYPE", "MDS");
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax);
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setInitiatedBranch(BRANCH_ID);
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    TxTransferTO.add(transferTo);
                                    transactionTO.setChequeNo("SERVICE_TAX");
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    TxTransferTO.add(transferTo);
                                }
                            }
                        }
                        System.out.println("transferTo List 2 : " + transferTo);
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue() > 0) {
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            //Changed By Suresh
                            if (map.containsKey("MDS_CLOSURE")) {
                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("SUNDRY_RECEIPT_HEAD"));
                            } else {
                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("BONUS_PAYABLE_HEAD"));
                            }
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " BONUS");
                            txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                            System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue());
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                            TxTransferTO.add(transferTo);
                            transactionTO.setChequeNo("SERVICE_TAX");
                            
                            TxTransferTO.add(transferTo);
                        }
                        System.out.println("transferTo List 3 : " + transferTo);
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNoticeAmount()).doubleValue() > 0) {
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("NOTICE_CHARGES_HEAD"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " NOTICE");
                            txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                            System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNoticeAmount()).doubleValue() + " NOTICE CHARGE");
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNoticeAmount()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                            
                            TxTransferTO.add(transferTo);
                        }
                        System.out.println("transferTo List 4 : " + transferTo);
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getAribitrationAmount()).doubleValue() > 0) {
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("CASE_EXPENSE_HEAD"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " CASE EXPENSE");
                            txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                            System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getAribitrationAmount()).doubleValue());
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getAribitrationAmount()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                            
                            TxTransferTO.add(transferTo);
                            transactionTO.setChequeNo("SERVICE_TAX");
                        }
                        System.out.println("transferTo List 5 : " + transferTo);
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getDiscountAmount()).doubleValue() > 0) {
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("DISCOUNT_HEAD"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " DISCOUNT");
                            txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                            System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getDiscountAmount()).doubleValue());
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getDiscountAmount()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                            
                            TxTransferTO.add(transferTo);
                            transactionTO.setChequeNo("SERVICE_TAX");
                            TxTransferTO.add(transferTo);
                        }

                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPenalAmount()).doubleValue() > 0) {
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("PENAL_INTEREST_HEAD"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " PENAL");
                            txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                            System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPenalAmount()).doubleValue());
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPenalAmount()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                            
                            TxTransferTO.add(transferTo);
                            transactionTO.setChequeNo("SERVICE_TAX");
                            TxTransferTO.add(transferTo);
                        }

                        //Changed By Suresh
                        if (map.containsKey("REFUND_AMOUNT")) {       //Dr Payment (First_Party)
                            double refundAmount = 0.0;
                            refundAmount = CommonUtil.convertObjToDouble(map.get("REFUND_AMOUNT")).doubleValue();
                            System.out.println("####### REFUND_AMOUNT : " + refundAmount);
                            if (refundAmount > 0) {
                                txMap = new HashMap();
                                //Changed By Suresh
                                if (map.containsKey("MDS_CLOSURE")) {
                                    txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("MDS_PAYABLE_HEAD"));
                                } else {
                                    txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("PAYMENT_HEAD"));
                                }
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " REFUND_PAYMENT");
                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, refundAmount);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                TxTransferTO.add(transferTo);
                            }

                            if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getRefundAmount()).doubleValue() > 0) { //Cr Suspense (Full Amt Return)
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                if (CommonUtil.convertObjToStr(applicationMap.get("SUSPENSE_GL_ACCNO")).equals("GL")) {
                                    txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("SUSPENSE_HEAD"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                } else {
                                    HashMap suspenseMap = new HashMap();                // Suspense Acc Head
                                    suspenseMap.put("PROD_ID", applicationMap.get("SUSPENSE_PROD_ID"));
                                    List suspenseLst = sqlMap.executeQueryForList("getAccountHeadProdSA", suspenseMap);
                                    if (suspenseLst != null && suspenseLst.size() > 0) {
                                        suspenseMap = (HashMap) suspenseLst.get(0);
                                        txMap.put(TransferTrans.CR_AC_HD, suspenseMap.get("AC_HEAD"));
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.SUSPENSE);
                                        txMap.put(TransferTrans.CR_PROD_ID, applicationMap.get("SUSPENSE_PROD_ID"));
                                        txMap.put(TransferTrans.CR_ACT_NUM, applicationMap.get("SUSPENSE_ACC_NO"));
                                    }
                                }
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " REFUND");
                                txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getRefundAmount()).doubleValue());
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getRefundAmount()).doubleValue());
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
                            }

                            if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getRefundAmount()).doubleValue() > 0 || map.containsKey("REFUND_AMOUNT")) {  //Cr Reverse_Bonus_Amount
                                double earnedBonus = 0.0;
                                if (applicationMap.get("BONUS_EXISTING_CHITTAL") != null && !applicationMap.get("BONUS_EXISTING_CHITTAL").equals("") && (applicationMap.get("BONUS_EXISTING_CHITTAL").equals("Y"))) {
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("CHITTAL_NO", prizedMoneyPaymentTO.getChittalNo());
                                    List paidList = sqlMap.executeQueryForList("getTotalInstAmount", hashMap);
                                    if (paidList != null && paidList.size() > 0) {
                                        hashMap = (HashMap) paidList.get(0);
                                        earnedBonus = CommonUtil.convertObjToDouble(hashMap.get("PAID_AMT")).doubleValue() - CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getRefundAmount()).doubleValue();
                                    }
                                    System.out.print("####### Reverse Bonus Amount : " + earnedBonus);
                                    if (earnedBonus > 0) {
                                        transferTo = new TxTransferTO();
                                        txMap = new HashMap();
                                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        //Changed By Suresh
                                        if (map.containsKey("MDS_CLOSURE")) {
                                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("SUNDRY_RECEIPT_HEAD"));
                                        } else {
                                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("BONUS_PAYABLE_HEAD"));
                                        }
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " REVERSE_BONUS");
                                        txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, earnedBonus);
                                        transferTo.setTransId("-");
                                        transferTo.setBatchId("-");
                                        transferTo.setInitiatedBranch(BRANCH_ID);
                                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                                        
                                        TxTransferTO.add(transferTo);
                                        transactionTO.setChequeNo("SERVICE_TAX");
                                    }
                                }
                            }
                        }


                        System.out.println("transactionDAO map : " + map);
                        transactionDAO.setBatchId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                        transactionDAO.setBatchDate(currDt);
                        transactionDAO.execute(map);
                        System.out.println("transferTo List 5 : " + transferTo);
                        HashMap applnMap = new HashMap();
                        transferDAO = new TransferDAO();
                        map.put("MODE", map.get("COMMAND"));
                        map.put("COMMAND", map.get("MODE"));
                        map.put("TxTransferTO", TxTransferTO);
                        transMap = transferDAO.execute(map, false);
                        HashMap linkBatchMap = new HashMap();
                        if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")
                                && transactionTO.getProductId().equals("") && !map.containsKey("LOCKER_SURRENDER_DAO")) {
                            linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
                        } else if (!map.containsKey("LOCKER_SURRENDER_DAO")) {
                            linkBatchMap.put("LINK_BATCH_ID", transactionTO.getDebitAcctNo());
                        } else {
                            linkBatchMap.put("LINK_BATCH_ID", map.get("LOCKER_SURRENDER_ID"));
                        }
                        linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
                        linkBatchMap.put("BATCH_ID", transMap.get("TRANS_ID"));
                        linkBatchMap.put("TRANS_DT", currDt);
                        linkBatchMap.put("INITIATED_BRANCH", _branchCode);
                        sqlMap.executeUpdate("updateLinkBatchIdTransfer", linkBatchMap);

                        System.out.println("transactionDAO map : " + map);
                        prizedMoneyPaymentTO.setTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                        debitMap = new HashMap();
//                        lst = sqlMap.executeQueryForList("getSelectMaxTransIdForCash",null);
//                        if(lst!=null && lst.size()>0){
//                            debitMap = (HashMap)lst.get(0);
//                            if(!applicationMap.containsKey("LOCKER_SURRENDER_DAO"))
//                                debitMap.put("BATCH_ID", cashMap.get("TRANS_ID"));
//                            else
//                                debitMap.put("BATCH_ID", applicationMap.get("LOCKER_SURRENDER_ID"));
//                            debitMap.put("BATCH_DT", currDt);
//                            debitMap.put("TRANS_DT",currDt);
//                            debitMap.put("INITIATED_BRANCH",map.get("BRANCH_CODE"));
//                            sqlMap.executeUpdate("updateRemitIssueTableCashBatchId", debitMap);
//                            debitMap = null;

                        linkBatchMap = new HashMap();
                        if (!map.containsKey("LOCKER_SURRENDER_DAO")) {
                            linkBatchMap.put("LINK_BATCH_ID", cashMap.get("TRANS_ID"));
                        } else {
                            linkBatchMap.put("LINK_BATCH_ID", map.get("LOCKER_SURRENDER_ID"));
                        }
                        linkBatchMap.put("TRANS_ID", cashMap.get("TRANS_ID"));
                        linkBatchMap.put("TRANS_DT", currDt);
                        linkBatchMap.put("INITIATED_BRANCH", BRANCH_ID);
                        sqlMap.executeUpdate("updateLinkBatchIdCash", linkBatchMap);
                        linkBatchMap = null;
//                        }
                    }
                }
            }
            String USERID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
            //    sqlMap.executeUpdate("insertReceiptEntryTO", prizedMoneyPaymentTO);
            //objLogDAO.addToLog(objLogTO);
            if (prizedMoneyPaymentTO.getTransId() != null && prizedMoneyPaymentTO.getTransId().length() > 0) {
                getTransDetails(prizedMoneyPaymentTO.getTransId());
            }
            if (prizedMoneyPaymentTO.getCashId() != null && prizedMoneyPaymentTO.getCashId().length() > 0) {
                getTransDetails(prizedMoneyPaymentTO.getCashId());
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            // throw new TransRollbackException(e);
            throw e;
        }
    }

    private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        transDetMap = new HashMap();
        List transList = (List) sqlMap.executeQueryForList("getTransferDetails", getTransMap);
        if (transList != null && transList.size() > 0) {
            transDetMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetails", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            transDetMap.put("CASH_TRANS_LIST", cashList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    private void getTransNewDetails(String batchId, String batchId1,HashMap map) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("BATCH_ID1", batchId1);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        transDetMap = new HashMap();
        List transList;
        List cashList;
//        if(!prizedMoneyPaymentTO.getDefaulter_marked().equals("Y"))
//        {
        transList = (List) sqlMap.executeQueryForList("getTransferDetails", getTransMap);
        if (transList != null && transList.size() > 0) {
            transDetMap.put("TRANSFER_TRANS_LIST", transList);
        }
        cashList = (List) sqlMap.executeQueryForList("getMultipleCashTransDetails", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            transDetMap.put("CASH_TRANS_LIST", cashList);
            //Added by sreekrishnan for kottayam
            if ((map.containsKey("TRASFER_BONUS_COMMISION") && map.get("TRASFER_BONUS_COMMISION").equals("Y"))) {
                transList = (List) sqlMap.executeQueryForList("getTransferDetailsForCashPayment", getTransMap);
                if (transList != null && transList.size() > 0) {
                    transDetMap.put("TRANSFER_TRANS_LIST", transList);
                }
            }
        }
        System.out.println("transDetMap..." + transDetMap);
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    private ArrayList newApplicationList(HashMap applnMap,String generateSingleTransId) throws Exception {
        System.out.println("applnMap :" + applnMap);
        ArrayList cashList = new ArrayList();
        System.out.println("objCashTransactionTO :" + prizedMoneyPaymentTO);
        //        if(prizedMoneyPaymentTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
        CashTransactionTO objCashTO = new CashTransactionTO();
        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()).doubleValue() >= 0) {
            objCashTO.setTransId("");
            objCashTO.setProdType("GL");
            //Changed By Suresh
            if (applnMap.containsKey("MDS_CLOSURE")) {
                objCashTO.setAcHdId(CommonUtil.convertObjToStr(applnMap.get("MDS_PAYABLE_HEAD")));
            } else {
                objCashTO.setAcHdId(CommonUtil.convertObjToStr(applnMap.get("PAYMENT_HEAD")));
            }
//            objCashTO.setInpAmount(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()));
//            objCashTO.setAmount(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()));
            objCashTO.setInpAmount(CommonUtil.convertObjToDouble(applnMap.get("TOTAL_AMOUNT")));
            objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
            objCashTO.setAmount(CommonUtil.convertObjToDouble(applnMap.get("TOTAL_AMOUNT")));
            objCashTO.setTransType(CommonConstants.DEBIT);
            objCashTO.setInitTransId(CommonUtil.convertObjToStr(applnMap.get("USER_ID")));
            objCashTO.setBranchId(CommonUtil.convertObjToStr(applnMap.get("SELECTED_BRANCH_ID")));
            objCashTO.setStatusBy(CommonUtil.convertObjToStr(applnMap.get("USER_ID")));
            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(applnMap.get("LINK_BATCH_ID")));
            objCashTO.setStatusDt(prizedMoneyPaymentTO.getStatusDt());
            //added by sreekrishnan for cashier
            List listData = ServerUtil.executeQuery("getCashierAuth", new HashMap());
            if (listData != null && listData.size() > 0) {
                HashMap map1 = (HashMap) listData.get(0);
                if(map1.get("CASHIER_AUTH_ALLOWED") != null && map1.get("CASHIER_AUTH_ALLOWED").toString().equals("Y") && objCashTO.getTransType().equals(CommonConstants.CREDIT)){
                    objCashTO.setAuthorizeStatus_2(null);
                }else
                    objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            }else{
                objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            }
            objCashTO.setParticulars(prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo());
            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            objCashTO.setSingleTransId(generateSingleTransId);
            objCashTO.setTransModType(CommonUtil.convertObjToStr(applnMap.get("TRANS_MOD_TYPE")));
            objCashTO.setCommand(prizedMoneyPaymentTO.getCommand());
            objCashTO.setScreenName("MDS Payment");
            if(applnMap.containsKey("INSTRUMENT_NO2") && applnMap.get("INSTRUMENT_NO2") != null){// Added by nithya on 31-10-2019 for KD-680
                objCashTO.setInstrumentNo2(CommonUtil.convertObjToStr(applnMap.get("INSTRUMENT_NO2")));
            }
            System.out.println("objCashTO 1st one:" + objCashTO);
            cashList.add(objCashTO);
        }
        objCashTO = null;
        //        }
        return cashList;
    }

    private ArrayList newApplicationListBonus(HashMap applnMap, double dBonus) throws Exception {
        System.out.println("applnMap :" + applnMap);
        ArrayList cashList = new ArrayList();
        System.out.println("objCashTransactionTO :" + prizedMoneyPaymentTO);
        //        if(prizedMoneyPaymentTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
        CashTransactionTO objCashTO = new CashTransactionTO();
        if (dBonus > 0) {
            objCashTO.setTransId("");
            objCashTO.setProdType("GL");
            //Changed By Suresh
//            if(applnMap.containsKey("MDS_CLOSURE")){
//                objCashTO.setAcHdId(CommonUtil.convertObjToStr(applnMap.get("MDS_PAYABLE_HEAD")));
//            }else{
            objCashTO.setAcHdId(CommonUtil.convertObjToStr(applnMap.get("PAYMENT_HEAD")));
//            }
//            objCashTO.setInpAmount(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()));
//            objCashTO.setAmount(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()));
            objCashTO.setInpAmount(CommonUtil.convertObjToDouble(applnMap.get("TOTAL_AMOUNT")));
            objCashTO.setAmount(CommonUtil.convertObjToDouble(applnMap.get("TOTAL_AMOUNT")));
            objCashTO.setTransType(CommonConstants.DEBIT);
            objCashTO.setInitTransId(CommonUtil.convertObjToStr(applnMap.get("USER_ID")));
            objCashTO.setBranchId(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
            objCashTO.setStatusBy(CommonUtil.convertObjToStr(applnMap.get("USER_ID")));
            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(applnMap.get("LINK_BATCH_ID")));
            objCashTO.setStatusDt(prizedMoneyPaymentTO.getStatusDt());
            objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            objCashTO.setParticulars(prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo());
            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            objCashTO.setCommand(prizedMoneyPaymentTO.getCommand());
            objCashTO.setScreenName("MDS Payment");
            System.out.println("objCashTO 1st onemmnmm:" + objCashTO);
            cashList.add(objCashTO);
        }
        objCashTO = null;
        //        }
        return cashList;
    }

    private ArrayList applicationList(HashMap applnMap) throws Exception {
        System.out.println("applnMap :" + applnMap);
        ArrayList cashList = new ArrayList();
        System.out.println("objCashTransactionTO :" + prizedMoneyPaymentTO);
        //        if(prizedMoneyPaymentTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
        CashTransactionTO objCashTO = new CashTransactionTO();
        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()).doubleValue() > 0) {
            objCashTO.setTransId("");
            objCashTO.setProdType("GL");
            //Changed By Suresh
            if (applnMap.containsKey("MDS_CLOSURE")) {
                objCashTO.setAcHdId(CommonUtil.convertObjToStr(applnMap.get("MDS_PAYABLE_HEAD")));
            } else {
                objCashTO.setAcHdId(CommonUtil.convertObjToStr(applnMap.get("PAYMENT_HEAD")));
            }
            objCashTO.setInpAmount(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()));
        	objCashTO.setAmount(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()));
            objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
            objCashTO.setTransType(CommonConstants.DEBIT);
            objCashTO.setInitTransId(CommonUtil.convertObjToStr(applnMap.get("USER_ID")));
            objCashTO.setBranchId(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
            objCashTO.setStatusBy(CommonUtil.convertObjToStr(applnMap.get("USER_ID")));
            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(applnMap.get("LINK_BATCH_ID")));
            objCashTO.setStatusDt(prizedMoneyPaymentTO.getStatusDt());
            objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            objCashTO.setParticulars(prizedMoneyPaymentTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo());
            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            objCashTO.setCommand(prizedMoneyPaymentTO.getCommand());
            objCashTO.setScreenName("MDS Payment");
            System.out.println("objCashTO 1st one:" + objCashTO);
            cashList.add(objCashTO);
        }
        objCashTO = null;
        //        }
        return cashList;
    }

    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("updateMDSChangeofMemberTo", prizedMoneyPaymentTO);
            deleteTransactionData(map);
            insertNewTransactionData(map);
//            insertTransactionData();
            logTO.setData(prizedMoneyPaymentTO.toString());
            logTO.setPrimaryKey(prizedMoneyPaymentTO.getKeyData());
            logTO.setStatus(prizedMoneyPaymentTO.getStatus());
            logDAO.addToLog(logTO);
            if (ExecptionDeftler) {
                sqlMap.rollbackTransaction();

            } else {
                sqlMap.commitTransaction();
                //ExecptionDeftler=true;
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            // throw new TransRollbackException(e);
            throw e;
        }
        map = null;
    }

    private void deleteData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            prizedMoneyPaymentTO.setTransId(CommonUtil.convertObjToStr(map.get("TRANS_ID")));
            prizedMoneyPaymentTO.setPaidStatus("N");
            sqlMap.executeUpdate("deleteMDSPrizedMoneyPaymentTO", prizedMoneyPaymentTO);
            deleteTransactionData(map);
            logTO.setData(prizedMoneyPaymentTO.toString());
            logTO.setPrimaryKey(prizedMoneyPaymentTO.getKeyData());
            logTO.setStatus(prizedMoneyPaymentTO.getStatus());
            logDAO.addToLog(logTO);
     		//added by chithra for service Tax
            if (map.containsKey("serviceTaxDetailsTO")) {
                ServiceTaxDetailsTO objserTax = (ServiceTaxDetailsTO) map.get("serviceTaxDetailsTO");
                insertServiceTaxDetails(objserTax);
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void deleteTransactionData(HashMap map) throws Exception {
        try {
            System.out.println("########## deleteTransactionData map :" + map);
            TransferTrans transferTrans = new TransferTrans();
            ArrayList TxTransferTO = new ArrayList();
            TransactionTO transactionTO = new TransactionTO();
            String branchID = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
            double totalAmt = CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()).doubleValue();
            transferTrans.setInitiatedBranch(branchID);
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            transactionDAO.setInitiatedBranch(branchID);
            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_UPDATE);
            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
            HashMap tempMap = new HashMap();
            HashMap deleteMap = new HashMap();
            double oldAmount = 0.0;
            if (map.containsKey("TransactionTO")) {
                HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                System.out.println("deleteTransactionData Inside deleteData map containskey :" + map);
                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                }
                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                System.out.println("Inside deleteData transactionTO :" + transactionTO);
                //                if (transactionTO.getTransType().equals("TRANSFER")) {
                System.out.println("deleteTransactionData Inside TRANSFER ");
                if (map.containsKey("BONUS_AMT_TRANSACTION_TRANSFER") && CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()).doubleValue() > 0) {
                    tempMap = (HashMap) map.get("BONUS_AMT_TRANSACTION_TRANSFER");
                    System.out.println("deleteTransactionData #### tempMap : " + tempMap);
                    oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                    tempMap.put("TRANS_DT", currDt);
                    tempMap.put("INITIATED_BRANCH", branchID);
                    tempMap.remove("TRANS_ID");
                    double newAmount = CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()).doubleValue();
                    ArrayList batchList = new ArrayList();
                    TxTransferTO txTransferTO = null;
                    HashMap oldAmountMap = new HashMap();
                    String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
                    if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                        List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                        if (lst != null) {
                            for (int i = 0; i < lst.size(); i++) {
                                txTransferTO = (TxTransferTO) lst.get(i);
                                if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
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
                        transferTrans.setInitiatedBranch(branchID);
                        transferTrans.doDebitCredit(batchList, branchID, false, command);
                        lst = null;
                        transferTrans = null;
                    }
                    txTransferTO = null;
                    batchList = null;
                    oldAmountMap = null;
                }
                deleteMap.put("TRANS_ID", tempMap.get("BATCH_ID"));

                if (map.containsKey("BONUS_AMT_TRANSACTION_CASH") && CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()).doubleValue() > 0) {
                    tempMap = (HashMap) map.get("BONUS_AMT_TRANSACTION_CASH");
                    System.out.println("#### inside CASH tempMap : " + tempMap);
                    oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    CashTransactionDAO cashDAO = new CashTransactionDAO();
                    CashTransactionTO cashTO = new CashTransactionTO();
                    HashMap cashMap = new HashMap();
                    String transId = CommonUtil.convertObjToStr(tempMap.get("TRANS_ID"));
                    cashMap.put("TRANS_ID", tempMap.get("TRANS_ID"));
                    cashMap.put("TRANS_DT", currDt);
                    cashMap.put("INITIATED_BRANCH", branchID);
                    System.out.println("cashMap 1st :" + cashMap);
                    List lstCash = (List) sqlMap.executeQueryForList("getSelectCashTransactionTO", cashMap);
                    if (lstCash != null && lstCash.size() > 0) {
                        for (int i = 0; i < lstCash.size(); i++) {
                            cashTO = (CashTransactionTO) lstCash.get(i);
                            cashTO.setCommand("DELETE");
                            cashTO.setStatus(CommonConstants.STATUS_DELETED);
                            cashMap.put("CashTransactionTO", cashTO);
                            cashMap.put("BRANCH_CODE", branchID);
                            cashMap.put("USER_ID", map.get("USER_ID"));
                            cashMap.put("OLDAMOUNT", new Double(oldAmount));
                            cashMap.put("SELECTED_BRANCH_ID", branchID);
                            cashMap.put("SCREEN_NAME", "MDS Payments");
                            System.out.println("cashMap :" + cashMap);
                            cashDAO.execute(cashMap, false);
                        }
                    }
                    deleteMap.put("TRANS_ID", tempMap.get("TRANS_ID"));
                    cashMap = null;
                    cashTO = null;
                    cashDAO = null;
                }
                deleteMap = null;
                tempMap = null;
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void doTransAuthorize(HashMap map,String authorizeStatus) throws Exception {
        String linkBatchId = "";
        HashMap cashAuthMap = new HashMap();
        HashMap transMap = new HashMap();        
        cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
        cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
        cashAuthMap.put("TRANS_ID", map.get("TRANS_ID"));
        cashAuthMap.put("TRANS_DT", currDt.clone());
        List lstCash = (List) sqlMap.executeQueryForList("getTransBatchIdForCashPayment", cashAuthMap);
        if (lstCash != null && lstCash.size() > 0) {
            transMap = (HashMap) lstCash.get(0);
            System.out.println("transMap^$^$%^"+transMap);
            cashAuthMap.put("TRANS_ID", transMap.get("BATCH_ID"));
            linkBatchId = CommonUtil.convertObjToStr(transMap.get("BATCH_ID"));
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            System.out.println(" cashAuthMap :" + cashAuthMap + "linkBatchId :" + linkBatchId + "authorizeStatus :" + authorizeStatus);
            TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
        }
        
    }
    private void doCashAuthorize(HashMap authMap, HashMap map, String batchid) throws Exception {
        String status = CommonUtil.convertObjToStr(authMap.get("AUTHORIZESTATUS"));
        CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
        ArrayList arrList = new ArrayList();
        HashMap singleAuthorizeMap = new HashMap();
        singleAuthorizeMap.put("STATUS", status);
        singleAuthorizeMap.put("TRANS_ID", batchid);
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
        map.put("SCREEN_NAME", "MDS Payments");
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
    }

    private void authorizeNew(HashMap authMap, HashMap map) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        System.out.println("@@@@@@@AuthMap" + AuthMap);
        try {

            System.out.println("AuthorizeMap :" + map);
            HashMap authorizeMap = new HashMap();
            authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
            System.out.println("authorizeMap :" + authMap);
            String authorizeStatus = CommonUtil.convertObjToStr(map.get("AUTHORIZESTATUS"));
            if (authMap.containsKey("TransactionTO")) {
                sqlMap.startTransaction();
                logTO.setData(map.toString());
                HashMap cashAuthMap = new HashMap();
                HashMap applnAuthMap = new HashMap();
                String batchid = "";
                HashMap transactionDetailsMap = (LinkedHashMap) authMap.get("TransactionTO");
                TransactionTO transactionTO = new TransactionTO();
                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                }
                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                if (transactionTO.getTransType().equals("TRANSFER")) {
                    String linkBatchId = "";
                    linkBatchId = CommonUtil.convertObjToStr(map.get("TRANS_ID"));
                    cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                    cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                    cashAuthMap.put("TRANS_ID", map.get("TRANS_ID"));
                    transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                    System.out.println(" cashAuthMap :" + cashAuthMap + "linkBatchId :" + linkBatchId + "authorizeStatus :" + authorizeStatus);
                    TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
                } else {
                    //added by sreekrishnan for cashier authoriazation
                    List listData = ServerUtil.executeQuery("getCashierAuth", new HashMap());
                    if (listData != null && listData.size() > 0) {
                        HashMap map1 = (HashMap) listData.get(0);
                        if (map1.get("CASHIER_AUTH_ALLOWED") != null && map1.get("CASHIER_AUTH_ALLOWED").toString().equals("Y")){
                            batchid = CommonUtil.convertObjToStr(map.get("CASH_ID"));
                            doCashAuthorize(map, map, batchid);  
                        }else{
                            if(CommonUtil.convertObjToStr(map.get("TRANS_ID"))!=null && 
                                    !CommonUtil.convertObjToStr(map.get("TRANS_ID")).equals(CommonUtil.convertObjToStr(map.get("CASH_ID")))){
                                batchid = CommonUtil.convertObjToStr(map.get("TRANS_ID"));
                                doCashAuthorize(map, map, batchid);
                            }
                            batchid = CommonUtil.convertObjToStr(map.get("CASH_ID"));
                            doCashAuthorize(map, map, batchid);
                        }
                    }else{
                           if(CommonUtil.convertObjToStr(map.get("TRANS_ID"))!=null && 
                                    !CommonUtil.convertObjToStr(map.get("TRANS_ID")).equals(CommonUtil.convertObjToStr(map.get("CASH_ID")))){
                                batchid = CommonUtil.convertObjToStr(map.get("TRANS_ID"));
                                doCashAuthorize(map, map, batchid);
                            }
                            batchid = CommonUtil.convertObjToStr(map.get("CASH_ID"));
                            doCashAuthorize(map, map, batchid);
                    }
                    //Added by sreekrishnan for default bonus and commision transfer
                    doTransAuthorize(map,authorizeStatus);
                    
                }
                sqlMap.commitTransaction();
            } else {
                HashMap hashMap = new HashMap();
                hashMap.put("CHITTAL_NO", AuthMap.get("CHITTAL_NO"));
                hashMap.put("SCHEME_NAME", AuthMap.get("SCHEME_NAME"));

                List DefaulterList = sqlMap.executeQueryForList("getDefaulterDts", hashMap);
                if (DefaulterList != null && DefaulterList.size() > 0) {
                    HashMap aMpa = (HashMap) DefaulterList.get(0);
                    if (aMpa.get("DEFAULTER") != null && !aMpa.get("DEFAULTER").toString().equals("") && aMpa.get("DEFAULTER").toString().equals("Y")) {
                        sqlMap.startTransaction();
                        HashMap cashAuthMap = new HashMap();
                        //   if (transactionTO.getTransType().equals("TRANSFER")) {
                        String linkBatchId = "";
                        linkBatchId = CommonUtil.convertObjToStr(map.get("TRANS_ID"));
                        cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                        cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                        cashAuthMap.put("TRANS_ID", map.get("TRANS_ID"));
                        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                        authorizeStatus = CommonUtil.convertObjToStr(map.get("AUTHORIZESTATUS"));
                        System.out.println(" cashAuthMapDDDDD :" + cashAuthMap + "linkBatchId :" + linkBatchId + "authorizeStatus sdsadsad:" + authorizeStatus);
                        TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
                        //   }
                        sqlMap.commitTransaction();
                    }
                }

            }
			//added by chithra for service Tax
            if (authMap.containsKey("SERVICE_TAX_AUTH")) {
                HashMap serMapAuth = new HashMap();
                serMapAuth.put("STATUS", authorizeStatus);
                serMapAuth.put("USER_ID", map.get(CommonConstants.USER_ID));
                serMapAuth.put("AUTHORIZEDT", currDt);
                serMapAuth.put("ACCT_NUM", AuthMap.get("CHITTAL_NO"));
                sqlMap.executeUpdate("authorizeServiceTaxDetails", serMapAuth);
            }           
            if (authorizeStatus.equals("AUTHORIZED")) {
                AuthMap.put("PAID_STATUS", "Y");
            } else {
                AuthMap.put("PAID_STATUS", "N");
            }


            sqlMap.executeUpdate("authorizeMDSPrizedMoneyPayment", AuthMap);
            if (authorizeStatus.equals("AUTHORIZED")) {
                List closureList = (List) sqlMap.executeQueryForList("checkSchemeClosureDetails", AuthMap);
                if (closureList != null && closureList.size() > 0) {
                    double totalSchemeAmount = 0.0;
                    double totalAmtReceived = 0.0;
                    double totalAmtPaid = 0.0;
                    HashMap whereMap = new HashMap();
                    List totalSchemeAmtLst = (List) sqlMap.executeQueryForList("getTotalAmountPerScheme", AuthMap);
                    if (totalSchemeAmtLst != null && totalSchemeAmtLst.size() > 0) {
                        whereMap = (HashMap) totalSchemeAmtLst.get(0);
                        totalSchemeAmount = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(whereMap.get("TOTAL_SCHEME_AMOUNT"))).doubleValue();
                    }
                    List totalReceived = (List) sqlMap.executeQueryForList("getTotalReceivedAmount", AuthMap);
                    if (totalReceived != null && totalReceived.size() > 0) {
                        whereMap = (HashMap) totalReceived.get(0);
                        totalAmtReceived = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(whereMap.get("TOTAL_RECEIVED"))).doubleValue();
                    }
                    List totalPaid = (List) sqlMap.executeQueryForList("getTotalPaidAmount", AuthMap);
                    if (totalPaid != null && totalPaid.size() > 0) {
                        whereMap = (HashMap) totalPaid.get(0);
                        totalAmtPaid = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(whereMap.get("TOTAL_PAID"))).doubleValue();
                    }
                    if (totalSchemeAmount == totalAmtReceived && totalSchemeAmount == totalAmtPaid) {
                        AuthMap.put("STATUS", "CLOSED");
                        sqlMap.executeUpdate("updateMDSProductCloseStatus", AuthMap);
                        List lst1 = sqlMap.executeQueryForList("getMDSLienDetailsForClosing", AuthMap);
                        if (lst1 != null && lst1.size() > 0) {
                            for (int i = 0; i < lst1.size(); i++) {
                                HashMap hmap = (HashMap) lst1.get(i);
                                List lst2 = sqlMap.executeQueryForList("getLienDEtailsForDelete", hmap);
                                if (lst2 != null && lst2.size() > 0) {
                                    hmap = (HashMap) lst2.get(0);
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
                //Update Case_Detail Charges
                if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getAribitrationAmount()).doubleValue() > 0) {
                    String actNo = CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()) + "_" + CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getSubNo());
                    chargesCollected(actNo);
                } 
                
                // Update notice charge details
                // Added by nithya on 26-07-2018 for KD-179 Mds defaulters payment issue (deduction entries not inserting personal ledger )
                if(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNoticeAmount()).doubleValue() > 0){
                    String actNo = CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()) + "_" + CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getSubNo());
                    noticeChargesCollected(actNo);
                }
                // End
                
                
                //Added by sreekrishnan for sms alerts..
                if (authorizeStatus.equals(CommonConstants.STATUS_AUTHORIZED)) {
                HashMap smsAlertMap = new HashMap();
                SmsConfigDAO smsDAO = new SmsConfigDAO(); 
                smsAlertMap.put("SCHEME_NAME", prizedMoneyPaymentTO.getSchemeName());
                smsAlertMap.put("CHITTAL_NO", prizedMoneyPaymentTO.getChittalNo());
                System.out.println("smsAlertMap%#%#%#%#%#%  " + smsAlertMap);
                List MdsSmsList = sqlMap.executeQueryForList("getMDsDetailsForSMS", smsAlertMap);
                if (MdsSmsList != null && MdsSmsList.size() > 0) {
                    HashMap MdsSmsMap = (HashMap) MdsSmsList.get(0);
                    System.out.println("MdsSmsMap%#%#%#^#^#^#^" + MdsSmsMap);
                    if (MdsSmsMap != null && !MdsSmsMap.equals("")) {
                        MdsSmsMap.put(CommonConstants.TRANS_ID, prizedMoneyPaymentTO.getCashId());
                        MdsSmsMap.put("TRANS_DT", currDt);
                        //List MdsTransList = sqlMap.executeQueryForList("getMdsTransDetailsForSms", MdsSmsMap);
                        //if (MdsTransList != null && MdsTransList.size() > 0) {
                            HashMap MdsTransMap =  new HashMap();
                            MdsTransMap.put(CommonConstants.BRANCH_ID,_branchCode);
                            MdsTransMap.put("SCHEME_NAME", prizedMoneyPaymentTO.getSchemeName());
                            MdsTransMap.put("CHITTAL_NO", prizedMoneyPaymentTO.getChittalNo());
                            MdsTransMap.put("INSTALLMENT", prizedMoneyPaymentTO.getPrizedAmount());
                            MdsTransMap.put("MDS_PRIZED_SMS", prizedMoneyPaymentTO.getPrizedAmount());
                            System.out.println("MdsTransMap %#%#%#^#^#^#^" + MdsTransMap);
                            smsDAO.MdsReceiptsSmsConfiguration(MdsTransMap);
                        //}
                    }
                    MdsSmsList = null;
                }
                }
                
                 // Added by nithya on 26-07-2018 for KD-179 Mds defaulters payment issue (deduction entries not inserting personal ledger )
                 // Add code here to insert into MDS_RECEIPT_ENTRY for defaulters using a parameter 
                                
                if(prizedMoneyPaymentTO.getDefaulters().equalsIgnoreCase("Y") && ((CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount())) > 0)){
                    System.out.println("Insert into MDS_RECEIPT_ENTRY...");
                    MDSReceiptEntryTO mdsReceiptEntryTO = setMDSReceiptEntryTO(map);
                    sqlMap.executeUpdate("insertReceiptEntryTO", mdsReceiptEntryTO);
                    HashMap mdsTransMap = new HashMap();
                    mdsTransMap.put("SCHEME_NAME", mdsReceiptEntryTO.getSchemeName());
                    mdsTransMap.put("CHITTAL_NO", mdsReceiptEntryTO.getChittalNo());
                    mdsTransMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getSubNo()));
                    mdsTransMap.put("NO_OF_INST", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getNoOfInstPay()));
                    mdsTransMap.put("INST_AMT", mdsReceiptEntryTO.getInstAmt());
                    mdsTransMap.put("PENAL_AMT", mdsReceiptEntryTO.getPenalAmtPayable());
                    mdsTransMap.put("BONUS_AMT", mdsReceiptEntryTO.getBonusAmtPayable());
                    mdsTransMap.put("FORFEITED_AMT",mdsReceiptEntryTO.getForfeitBonusAmtPayable()); //Added by nithya for KD-2093
                    mdsTransMap.put("BANK_ADV_AMTCR",mdsReceiptEntryTO.getBankAdvanceAmt());
                    mdsTransMap.put("DISCOUNT_AMT", mdsReceiptEntryTO.getDiscountAmt());
                    mdsTransMap.put("MDS_INTEREST", mdsReceiptEntryTO.getMdsInterset());
                    mdsTransMap.put("NOTICE_AMT", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNoticeAmt()));
                    mdsTransMap.put("ARBITRATION_AMT", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getArbitrationAmt()));
                    mdsTransMap.put("SERVICE_TAX_AMT", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getServiceTaxAmt()));
                    mdsTransMap.put("NARRATION", CommonUtil.convertObjToStr(mdsReceiptEntryTO.getNarration()));
                    double noticeAmount = CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNoticeAmt());//9535
                    System.out.println("setNetAmtsetNetAmt" + mdsReceiptEntryTO.getNetAmt());
                    //mdsReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNetAmt()) - noticeAmount);
                    mdsReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNetAmt())); // Added by nithya on 22-11-2019 for KD-783
                    System.out.println("noticeAmountnoticeAmount" + noticeAmount + "  " + mdsReceiptEntryTO.getNetAmt());
                    mdsTransMap.put("NET_AMT", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNetAmt()));
                    //mdsTransMap.put("SUBSCRIPTION_AMT", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getInstAmtPayable())); // 01-11-2019

                    mdsTransMap.put("STATUS", CommonConstants.STATUS_CREATED);
                    mdsTransMap.put("STATUS_DT", setProperDtFormat(currDt));
                    mdsTransMap.put("STATUS_BY", map.get("USER_ID"));
                    mdsTransMap.put("AUTHORIZE_STATUS", authorizeStatus);
                    mdsTransMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                    mdsTransMap.put("AUTHORIZE_DT", setProperDtFormat(currDt));
                    mdsTransMap.put("TRANS_DT", setProperDtFormat(currDt));
                    mdsTransMap.put("NET_TRANS_ID", mdsReceiptEntryTO.getNetTransId());
                    HashMap maxListMap = new HashMap();
                    maxListMap.put("SCHEME_NAME", mdsReceiptEntryTO.getSchemeName());
                    maxListMap.put("CHITTAL_NO", mdsReceiptEntryTO.getChittalNo());
                    maxListMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getSubNo()));
                    List list = sqlMap.executeQueryForList("getSelectTransMaxRec", maxListMap);
                    if (list != null && list.size() > 0) {
                        maxListMap = (HashMap) list.get(0);
                        long instCount = CommonUtil.convertObjToLong(maxListMap.get("INST_COUNT")) + 1;
                        mdsTransMap.put("INST_COUNT", new Long(instCount));
                    } else {
                        mdsTransMap.put("INST_COUNT", new Long(1));
                    }
                    System.out.println("############ mdsTransMap : " + mdsTransMap);
                    if (!authorizeStatus.equals("REJECTED")) {
                        sqlMap.executeUpdate("updateMDSTransDetailsEachRec", mdsTransMap);
                    }
                }
                // End
            }

        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
    
    // Added by nithya on 26-07-2018 for KD-179 Mds defaulters payment issue (deduction entries not inserting personal ledger )
    
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
    
    private MDSReceiptEntryTO setMDSReceiptEntryTO(HashMap map) throws Exception  {
        MDSReceiptEntryTO mdsReceiptEntryTO = new MDSReceiptEntryTO();
        HashMap chittalMap = new HashMap();
        chittalMap.put("CHITTAL_NO", prizedMoneyPaymentTO.getChittalNo());
        chittalMap.put("SUB_NO", CommonUtil.convertObjToInt(prizedMoneyPaymentTO.getSubNo()));
        List lst = sqlMap.executeQueryForList("getSelctApplnReceiptDetails", chittalMap);
        if (lst != null && lst.size() > 0) {
            chittalMap = (HashMap) lst.get(0);
            Date chitStartDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalMap.get("CHIT_END_DT")));
            mdsReceiptEntryTO.setChitStartDt(setProperDtFormat(chitStartDt));
            mdsReceiptEntryTO.setNoOfInst(CommonUtil.convertObjToInt(chittalMap.get("NO_OF_INSTALLMENTS")));
            mdsReceiptEntryTO.setInstAmt(CommonUtil.convertObjToDouble(chittalMap.get("INST_AMT")));           
        }
        HashMap prizedMap = new HashMap();
        prizedMap.put("SCHEME_NAME", prizedMoneyPaymentTO.getSchemeName());
        prizedMap.put("DIVISION_NO", chittalMap.get("DIVISION_NO"));
        lst = sqlMap.executeQueryForList("getSelectPrizedDetailsEntryRecords", prizedMap);
        if (lst != null && lst.size() > 0) {
            prizedMap = (HashMap) lst.get(0);            
            Date chitEndDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(prizedMap.get("NEXT_INSTALLMENT_DATE")));
            mdsReceiptEntryTO.setChitEndDt(setProperDtFormat(chitEndDt));
        } 
        mdsReceiptEntryTO.setSchemeName(prizedMoneyPaymentTO.getSchemeName());
        mdsReceiptEntryTO.setDivisionNo(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getDivisionNo()));
        mdsReceiptEntryTO.setChittalNo(prizedMoneyPaymentTO.getChittalNo());        
        mdsReceiptEntryTO.setSubNo(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getSubNo()));
        HashMap insDateMap = new HashMap();
        insDateMap.put("DIVISION_NO", CommonUtil.convertObjToInt(prizedMoneyPaymentTO.getDivisionNo()));
        insDateMap.put("SCHEME_NAME", prizedMoneyPaymentTO.getSchemeName());
        insDateMap.put("CURR_DATE", currDt.clone());
        List insDateLst = sqlMap.executeQueryForList("getMDSNextInsDate", insDateMap);
        if (insDateLst != null && insDateLst.size() > 0) {
            insDateMap = (HashMap) insDateLst.get(0);           
            mdsReceiptEntryTO.setCurrInst(CommonUtil.convertObjToDouble(insDateMap.get("INST_NO")));
            mdsReceiptEntryTO.setPaidInst(CommonUtil.convertObjToDouble(insDateMap.get("INST_NO")));
        }           
        mdsReceiptEntryTO.setPendingInst(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNoOfOverdueInst()));
        mdsReceiptEntryTO.setTotalInstDue(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()));
        //mdsReceiptEntryTO.setBonusAmtAvail(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()));
        mdsReceiptEntryTO.setMemberName(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getMemberName()));        
        mdsReceiptEntryTO.setPrizedMember("Y");        
        mdsReceiptEntryTO.setNoticeAmt(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNoticeAmount()));
        mdsReceiptEntryTO.setArbitrationAmt(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getAribitrationAmount()));
        mdsReceiptEntryTO.setNoOfInstPay(CommonUtil.convertObjToInt(prizedMoneyPaymentTO.getNoOfOverdueInst()));
        mdsReceiptEntryTO.setInstAmtPayable(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()));
        mdsReceiptEntryTO.setPenalAmtPayable(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPenalAmount()));
        mdsReceiptEntryTO.setBonusAmtPayable(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getChitalBonusAmt()));
        mdsReceiptEntryTO.setDiscountAmt(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getDiscountAmount()));
        mdsReceiptEntryTO.setMdsInterset(0.0);
        mdsReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()) + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPenalAmount()));
        mdsReceiptEntryTO.setPaidDate(currDt);        
        mdsReceiptEntryTO.setPaidAmt(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()));        
        mdsReceiptEntryTO.setStatusBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));  
        mdsReceiptEntryTO.setStatus("CREATED");
        mdsReceiptEntryTO.setStatusDt(currDt);
        mdsReceiptEntryTO.setAuthorizeStatus("AUTHORIZED");
        mdsReceiptEntryTO.setAuthorizeBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        mdsReceiptEntryTO.setAuthorizeDt(currDt);
        mdsReceiptEntryTO.setBranchCode(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
        mdsReceiptEntryTO.setNarration("Inst#"+mdsReceiptEntryTO.getCurrInst()+"Defaulter");
        mdsReceiptEntryTO.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));       
        //mdsReceiptEntryTO.setServiceTaxAmt(0.0);
        mdsReceiptEntryTO.setServiceTaxAmt(prizedMoneyPaymentTO.getDefaulterReceiptTaxAmt()); // Added by nithya on 25-11-2019 for KD-910
        mdsReceiptEntryTO.setNetTransId(CommonUtil.convertObjToStr(map.get("TRANS_ID")));
        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPenalAmount()) > 0) {
            mdsReceiptEntryTO.setPenalTransId(CommonUtil.convertObjToStr(map.get("TRANS_ID")));
        }
        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getAribitrationAmount()) > 0) {
            mdsReceiptEntryTO.setArbitrationId(CommonUtil.convertObjToStr(map.get("TRANS_ID")));
        }
        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNoticeAmount()) > 0) {
            mdsReceiptEntryTO.setNoticeId(CommonUtil.convertObjToStr(map.get("TRANS_ID")));
        }
        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()) > 0) {
            mdsReceiptEntryTO.setBonusTransId(CommonUtil.convertObjToStr(map.get("TRANS_ID")));
        }
        mdsReceiptEntryTO.setForfeitBonusAmtPayable(0.0); //Added by nithya for KD-2093
        mdsReceiptEntryTO.setForfeitBonusTransId("");
        mdsReceiptEntryTO.setBankAdvanceAmt(0.0);
        System.out.println("ODDDDDDDDDDDDDDDDDDDDDDD");
        return mdsReceiptEntryTO;
    }

    // End
    
    // Added by nithya on 26-07-2018 for KD-179 Mds defaulters payment issue (deduction entries not inserting personal ledger )
    private void noticeChargesCollected(String act_num) throws Exception {
        HashMap chargeMap = new HashMap();
        List chargeLst = null;
        chargeMap.put(CommonConstants.ACT_NUM, act_num);
        chargeLst = sqlMap.executeQueryForList("getMDSNoticeChargeDetails", chargeMap);
        if (chargeLst != null && chargeLst.size() > 0) {
            for (int i = 0; i < chargeLst.size(); i++) {
                chargeMap = (HashMap) chargeLst.get(i);
                double chargeAmount = 0.0;
                double chargeNo = 0;
                String chargeType = "";
                HashMap whereMap = new HashMap();
                chargeAmount = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                chargeNo = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_NO")).doubleValue();
                chargeType = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_TYPE"));
                whereMap.put("ACT_NUM", act_num);
                whereMap.put("CHARGE_TYPE", chargeType);
                whereMap.put("PAID_AMT", new Double(chargeAmount));
                whereMap.put("CHARGE_NO", new Double(chargeNo));
                if (chargeAmount > 0) {
                    sqlMap.executeUpdate("updateChargeDetails", whereMap);
                }
            }
        }
    }
    
    // End


    //Update Paid_Charge Amount in LOANS_ACCT_CHARGE_DETAILS
    private void chargesCollected(String act_num) throws Exception {
        HashMap chargeMap = new HashMap();
        List chargeLst = null;
        chargeMap.put(CommonConstants.ACT_NUM, act_num);
        chargeLst = sqlMap.executeQueryForList("getMDSCaseChargeDetails", chargeMap);
        if (chargeLst != null && chargeLst.size() > 0) {
            for (int i = 0; i < chargeLst.size(); i++) {
                chargeMap = (HashMap) chargeLst.get(i);
                double chargeAmount = 0.0;
                double chargeNo = 0;
                String chargeType = "";
                HashMap whereMap = new HashMap();
                chargeAmount = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                chargeNo = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_NO")).doubleValue();
                chargeType = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_TYPE"));
                whereMap.put("ACT_NUM", act_num);
                whereMap.put("CHARGE_TYPE", chargeType);
                whereMap.put("PAID_AMT", new Double(chargeAmount));
                whereMap.put("CHARGE_NO", new Double(chargeNo));
                if (chargeAmount > 0) {
                    sqlMap.executeUpdate("updateChargeDetails", whereMap);
                }
            }
        }
    }

    private void authorize(HashMap map) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        System.out.println("@@@@@@@AuthMap" + AuthMap);
        try {
            sqlMap.startTransaction();
            logTO.setData(map.toString());
            System.out.println("AuthorizeMap :" + map);
            HashMap authorizeMap = new HashMap();
            authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
            System.out.println("authorizeMap :" + authorizeMap);
            String authorizeStatus = CommonUtil.convertObjToStr(map.get("AUTHORIZESTATUS"));
            HashMap cashAuthMap = new HashMap();
            HashMap applnAuthMap = new HashMap();
            String linkBatchId = "";
            linkBatchId = CommonUtil.convertObjToStr(map.get("TRANS_ID"));
            cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
            cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            cashAuthMap.put("TRANS_ID", map.get("TRANS_ID"));
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            System.out.println(" cashAuthMap :" + cashAuthMap + "linkBatchId :" + linkBatchId + "authorizeStatus :" + authorizeStatus);
            TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
            String cashLinkBatchId = "";
            cashAuthMap.put("TRANS_ID", map.get("CASH_ID"));
            cashLinkBatchId = CommonUtil.convertObjToStr(map.get("CASH_ID"));
            System.out.println(" cashAuthMap :" + cashAuthMap);
            TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(cashLinkBatchId, authorizeStatus, cashAuthMap);
            authorizeMap = null;
            cashAuthMap = null;
            applnAuthMap = null;
            if (authorizeStatus.equals("AUTHORIZED")) {
                AuthMap.put("PAID_STATUS", "Y");
            } else {
                AuthMap.put("PAID_STATUS", "N");
            }
            sqlMap.executeUpdate("authorizeMDSPrizedMoneyPayment", AuthMap);
            if (authorizeStatus.equals("AUTHORIZED")) {
                List closureList = (List) sqlMap.executeQueryForList("checkSchemeClosureDetails", AuthMap);
                if (closureList != null && closureList.size() > 0) {
                    double totalSchemeAmount = 0.0;
                    double totalAmtReceived = 0.0;
                    double totalAmtPaid = 0.0;
                    HashMap whereMap = new HashMap();
                    List totalSchemeAmtLst = (List) sqlMap.executeQueryForList("getTotalAmountPerScheme", AuthMap);
                    if (totalSchemeAmtLst != null && totalSchemeAmtLst.size() > 0) {
                        whereMap = (HashMap) totalSchemeAmtLst.get(0);
                        totalSchemeAmount = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(whereMap.get("TOTAL_SCHEME_AMOUNT"))).doubleValue();
                    }
                    List totalReceived = (List) sqlMap.executeQueryForList("getTotalReceivedAmount", AuthMap);
                    if (totalReceived != null && totalReceived.size() > 0) {
                        whereMap = (HashMap) totalReceived.get(0);
                        totalAmtReceived = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(whereMap.get("TOTAL_RECEIVED"))).doubleValue();
                    }
                    List totalPaid = (List) sqlMap.executeQueryForList("getTotalPaidAmount", AuthMap);
                    if (totalPaid != null && totalPaid.size() > 0) {
                        whereMap = (HashMap) totalPaid.get(0);
                        totalAmtPaid = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(whereMap.get("TOTAL_PAID"))).doubleValue();
                    }
                    if (totalSchemeAmount == totalAmtReceived && totalSchemeAmount == totalAmtPaid) {
                        AuthMap.put("STATUS", "CLOSED");
                        sqlMap.executeUpdate("updateMDSProductCloseStatus", AuthMap);

                        List lst1 = sqlMap.executeQueryForList("getMDSLienDetailsForClosing", AuthMap);
                        if (lst1 != null && lst1.size() > 0) {
                            for (int i = 0; i < lst1.size(); i++) {
                                HashMap hmap = (HashMap) lst1.get(i);
                                List lst2 = sqlMap.executeQueryForList("getLienDEtailsForDelete", hmap);
                                if (lst2 != null && lst2.size() > 0) {
                                    hmap = (HashMap) lst2.get(0);
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
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            MDSPrizedMoneyPaymentDAO dao = new MDSPrizedMoneyPaymentDAO();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getDataNew(obj);
    }

    private void destroyObjects() {
        prizedMoneyPaymentTO = null;
    }
}
