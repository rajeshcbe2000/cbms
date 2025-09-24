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
package com.see.truetransact.serverside.gdsapplication.gdsprizedmoneypayment;

import com.see.truetransact.serverside.mdsapplication.mdsprizedmoneypayment.*;
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
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.commonutil.servicetax.ServiceTaxCalculation;
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
import com.see.truetransact.transferobject.gdsapplication.GDSApplicationTO;
import com.see.truetransact.transferobject.gdsapplication.gdsprizedmoneypayment.GDSPrizedMoneyPaymentTO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;

/**
 * This is used for OperativeAcctProductDAO Data Access.
 *
 * @author Balachandar
 *
 * @modified Pinky @modified Rahul
 */
public class GDSPrizedMoneyPaymentDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private GDSPrizedMoneyPaymentTO prizedMoneyPaymentTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private TransactionDAO transactionDAO = null;
    TransferDAO transferDAO = new TransferDAO();
    private final static Logger log = Logger.getLogger(GDSPrizedMoneyPaymentDAO.class);
    private Date currDt = null;
    Map transDetMap = null;
    boolean ExecptionDeftler = false;
    private String generateSingleTransId = "";
    private int ibrHierarchy = 0;
    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public GDSPrizedMoneyPaymentDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getDataNew(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSelectGDSPrizedMoneyPaymentTO", map);
        returnMap.put("GDSPrizedMoneyPaymentTO", list);

        // Get Trans_Details
        HashMap getRemitTransMap = new HashMap();
        getRemitTransMap.put("TRANS_ID", CommonUtil.convertObjToStr(map.get("TRANS_ID")));
        getRemitTransMap.put("TRANS_DT", currDt.clone());
        getRemitTransMap.put("BRANCH_CODE", _branchCode);
        List remitList = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", getRemitTransMap);
        if (remitList != null && remitList.size() > 0) {
            returnMap.put("TransactionTO", remitList);
        } else {
            GDSPrizedMoneyPaymentTO objMDSPrizedMoneyPaymentTO = (GDSPrizedMoneyPaymentTO) list.get(0);
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
        returnMap.put("GDSPrizedMoneyPaymentTO", list);

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
            prizedMoneyPaymentTO = (GDSPrizedMoneyPaymentTO) map.get("MDSPrizedMoneyPaymentData");
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
            map.put("GDS_NO", prizedMoneyPaymentTO.getGdsNo());
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
//            List allApplnDetails = sqlMap.executeQueryForList("getAllApplcationDetailsForGDS", map);
//                    int schemeCount = allApplnDetails.size();
//            if (allApplnDetails != null && allApplnDetails.size() > 0) {
//                            for (int i = 0; i < allApplnDetails.size(); i++) {
//                                GDSApplicationTO objGDSApplicationTO = (GDSApplicationTO)allApplnDetails.get(i);
//                                applicationMap = (HashMap) actheadlst.get(i);
            
            //commented by shany
                        
           // prizedMoneyPaymentTO.setPaidStatus("N");
//            sqlMap.executeUpdate("insertMDSPrizedMoneyPaymentTO", prizedMoneyPaymentTO);
//            logTO.setData(prizedMoneyPaymentTO.toString());
//            logTO.setPrimaryKey(prizedMoneyPaymentTO.getKeyData());
//            logTO.setStatus(prizedMoneyPaymentTO.getStatus());
//            logDAO.addToLog(logTO);
//            System.out.println("ExecptionDeftler..." + ExecptionDeftler);
//            if (ExecptionDeftler) {
//                sqlMap.rollbackTransaction();
//
//            } else {
//                sqlMap.commitTransaction();
//                //ExecptionDeftler=true;
//            }
                                
//                            }
//            }
//                    
            
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
            applicationMap.put("GROUP_NO", prizedMoneyPaymentTO.getGroupName());
            List lst = sqlMap.executeQueryForList("getSelectAllSchemeAcctHead", applicationMap);
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
            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
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
                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo()+ "_" + prizedMoneyPaymentTO.getSubNo() + " PAYMENT");
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
                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
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
                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " BONUS");
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
                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
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
                account_map.put("CHITTAL_NO", prizedMoneyPaymentTO.getGdsNo());
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
                    defaulter_bonus_recoverd = prizedMoneyPaymentTO.getDefaulter_bonus_recoverd();
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
                    defaulter_comm = prizedMoneyPaymentTO.getDefaulter_comm();
              //      System.out.println("commission.." + defaulter_comm);
                    transferTo = new TxTransferTO();
                    txMap = new HashMap();
                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.CR_AC_HD, Misclens);
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " OVERDUE");
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
                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                    transferTo.setSingleTransId(generateSingleTransId);
                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                    TxTransferTO.add(transferTo);
                    transactionTO.setChequeNo("SERVICE_TAX");
                }
                if (prizedMoneyPaymentTO.getDefaulter_interst() != null && prizedMoneyPaymentTO.getDefaulter_interst() > 0.0) {
                    defaulter_Inteset = prizedMoneyPaymentTO.getDefaulter_interst();
                    transferTo = new TxTransferTO();
                    txMap = new HashMap();
                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.CR_AC_HD, interstAccountHead);
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " OVERDUE");
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
                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                    transferTo.setSingleTransId(generateSingleTransId);
                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                    TxTransferTO.add(transferTo);
                    transactionTO.setChequeNo("SERVICE_TAX");
                }


                // System.out.print("###### cashList: "+cashList);
                double paidamount = 0.0;
                HashMap mdsMap = new HashMap();
                mdsMap.put("CHITTAL_NO", prizedMoneyPaymentTO.getGdsNo());
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
                mdsbonusMap.put("CHITTAL_NO", prizedMoneyPaymentTO.getGdsNo());
                mdsbonusMap.put("SUB_NO", CommonUtil.convertObjToInt(prizedMoneyPaymentTO.getSubNo()));
                mdsbonusMap.put("SCHME_NAME", prizedMoneyPaymentTO.getGroupName());

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
                    txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " OVERDUE");
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
                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
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
                    txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " OVERDUE");
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
                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
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
                    txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " OVERDUE");
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
                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
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
                double NtMislns = TotDet - (amountToReciept + paidamount + defaulter_Inteset + defaulter_comm + prizedMoneyPaymentTO.getCommisionAmount() + prizedMoneyPaymentTO.getBonusAmount());
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
                    txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " OVERDUE");
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
                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
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
                        txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " COMMISION");
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
                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                        TxTransferTO.add(transferTo);
                        transactionTO.setChequeNo("SERVICE_TAX");
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                        TxTransferTO.add(transferTo);
						//added by chithra for service Tax
                        if (map.containsKey("serviceTaxDetails")) {
                                HashMap serTaxMap = (HashMap) map.get("serviceTaxDetails");
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_AC_HD, serTaxMap.get("TAX_HEAD_ID"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " SERVICE TAX");
                                txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT")));
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
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
                        txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " COMMISION");
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
                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                        TxTransferTO.add(transferTo);
                        transactionTO.setChequeNo("SERVICE_TAX");
                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                        TxTransferTO.add(transferTo);
						//added by chithra for service Tax
                        if (map.containsKey("serviceTaxDetails")) {
                                HashMap serTaxMap = (HashMap) map.get("serviceTaxDetails");
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_AC_HD, serTaxMap.get("TAX_HEAD_ID"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " SERVICE TAX");
                                txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT")));
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
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
            System.out.println("inside insertTransactionData : " + map);
            String thalayalChittal = "N";
            String munnalChittal = "N";
            List prizedMoneyPaymentTOLsit = new ArrayList();
            String glTransActNum = "";
            String isBonusTrans = "";
            String generateSingleTransId = generateLinkID();
            prizedMoneyPaymentTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
            prizedMoneyPaymentTO.setStatus(CommonConstants.STATUS_CREATED);            
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
             HashMap groupMap = new HashMap();
            groupMap.put("GROUP_NO", map.get("GROUP_NO"));
            List commonSchemeDetailsLst = sqlMap.executeQueryForList("getDetailsForOneSchemeInGroup", groupMap);           
            HashMap commonSchemeMap = (HashMap) commonSchemeDetailsLst.get(0);
            String commonScheme = CommonUtil.convertObjToStr(commonSchemeMap.get("SCHEME_NAME"));
            HashMap checkMap = new HashMap();
            checkMap.put("SCHEME_NAME", commonScheme);
            List list = sqlMap.executeQueryForList("getifBonusTransactionRequired", checkMap);
            if (list != null && list.size() > 0) {
                checkMap = (HashMap) list.get(0);
                if (checkMap.containsKey("IS_BONUS_TRANSFER") && checkMap.get("IS_BONUS_TRANSFER") != null && !"".equalsIgnoreCase(CommonUtil.convertObjToStr(checkMap.get("IS_BONUS_TRANSFER")))) {
                    isBonusTrans = CommonUtil.convertObjToStr(checkMap.get("IS_BONUS_TRANSFER"));
                }
            }
            map.put("IS_BONUS_TRANSFER",isBonusTrans);
            if (map.containsKey("TransactionTO")) {
                HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                }
                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                HashMap applicationMap = new HashMap();
                applicationMap.put("SCHEME_NAME", prizedMoneyPaymentTO.getGroupName());
                applicationMap.put("GDS_NO", prizedMoneyPaymentTO.getGdsNo());
                List lst;
                List actheadlst = sqlMap.executeQueryForList("getSelectGDSGroupAcctHead", applicationMap);             
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
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("SA")) {
                        debitMap.put("prodId", transactionTO.getProductId());
                        lst = sqlMap.executeQueryForList("getAccountHeadProdSAHead", debitMap);
                        if (lst != null && lst.size() > 0) {
                            debitMap = (HashMap) lst.get(0);
                        }
                    }
                    if(map.containsKey("THALAYAL_CHITTAL") && map.get("THALAYAL_CHITTAL") != null && map.get("THALAYAL_CHITTAL").equals("Y")){
                        thalayalChittal = "Y";
                    }
                    if(map.containsKey("MUNNAL_CHITTAL") && map.get("MUNNAL_CHITTAL") != null && map.get("MUNNAL_CHITTAL").equals("Y")){
                        munnalChittal = "Y";
                    }
                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                    List allApplnDetails = sqlMap.executeQueryForList("getAllApplcationDetailsForGDS", applicationMap);
                    int schemeCount = allApplnDetails.size();
                    if (transactionTO.getTransType().equals("TRANSFER")) {    
                        GDSPrizedMoneyPaymentTO objprizedMoneyPaymentTO;
                        // Iterating for getting scheme deatails [ schemename & heads ]
                        if (allApplnDetails != null && allApplnDetails.size() > 0) {
                            for (int i = 0; i < allApplnDetails.size(); i++) {
                                GDSApplicationTO objGDSApplicationTO = (GDSApplicationTO)allApplnDetails.get(i);  
                                HashMap schemeMap = new HashMap();
                                schemeMap.put("SCHEME_NAME",objGDSApplicationTO.getSchemeName());
                                List headlst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", schemeMap);
                                if (headlst != null && headlst.size() > 0) {
                                applicationMap = (HashMap) headlst.get(0);
                              } 
                                System.out.println("objGDSApplicationTO"+objGDSApplicationTO);
                                prizedMoneyPaymentTO.setSchemeName(CommonUtil.convertObjToStr(objGDSApplicationTO.getSchemeName()));
                                prizedMoneyPaymentTO.setChittalNo(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));
                                //applicationMap = (HashMap) actheadlst.get(i);
                                if (map.containsKey("GL_TRANS_ACT_NUM") && CommonUtil.convertObjToStr(map.get("GL_TRANS_ACT_NUM")).length() > 0) {
                                    glTransActNum = CommonUtil.convertObjToStr(map.get("GL_TRANS_ACT_NUM"));
                                }
                                if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue()
                                        + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue() > 0) {
                                    txMap = new HashMap();                                  
                                    if (map.containsKey("MDS_CLOSURE")) {
                                        txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("MDS_PAYABLE_HEAD"));
                                    } else {
                                        txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("PAYMENT_HEAD"));
                                    }
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " PAYMENT");
                                    txMap.put(TransferTrans.DR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                    txMap.put("TRANS_MOD_TYPE", "MDS");
                                    if (!(map.containsKey("AUCTION_TRANS") && map.get("AUCTION_TRANS").equals("Y"))) {
                                        if (isBonusTrans.equalsIgnoreCase("N")) {
                                            transferTo = transactionDAO.addTransferDebitLocal(txMap, (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue() / schemeCount                                                    
                                                    + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue() / schemeCount));
                                        } else {
                                            transferTo = transactionDAO.addTransferDebitLocal(txMap, (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue() / schemeCount
                                                    + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue() / schemeCount
                                                    + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue() / schemeCount));
                                        }

                                    } else {
                                        transferTo = transactionDAO.addTransferDebitLocal(txMap, (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue()/schemeCount));
                                    }                                  
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setInitiatedBranch(BRANCH_ID);
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setGlTransActNum(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                    TxTransferTO.add(transferTo);
                                }
                                // Credit transaction of commission - start
                                if (!(map.containsKey("AUCTION_TRANS") && map.get("AUCTION_TRANS").equals("Y"))) {
                                    if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue() > 0) {
                                        transferTo = new TxTransferTO();
                                        txMap = new HashMap();
                                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("COMMISION_HEAD"));
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.PARTICULARS, objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " COMMISION");
                                        txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                        txMap.put("TRANS_MOD_TYPE", "MDS");                                      
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue()/schemeCount);
                                        transferTo.setTransId("-");
                                        transferTo.setBatchId("-");
                                        transferTo.setInitiatedBranch(BRANCH_ID);
                                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        TxTransferTO.add(transferTo);
                                        transactionTO.setChequeNo("SERVICE_TAX");
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setGlTransActNum(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        TxTransferTO.add(transferTo);
                                        //added by chithra for service Tax
//                                        if (map.containsKey("serviceTaxDetails")) {
//                                            HashMap serTaxMap = (HashMap) map.get("serviceTaxDetails");
//
//                                            transferTo = new TxTransferTO();
//                                            txMap = new HashMap();
//                                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
//                                            txMap.put(TransferTrans.CR_AC_HD, serTaxMap.get("TAX_HEAD_ID"));
//                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
//                                            txMap.put(TransferTrans.PARTICULARS, objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " SERVICE TAX");
//                                            txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
//                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
//                                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
//                                            txMap.put("TRANS_MOD_TYPE", "MDS");
//                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT")));
//                                            transferTo.setTransId("-");
//                                            transferTo.setBatchId("-");
//                                            transferTo.setInitiatedBranch(BRANCH_ID);
//                                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                                            transferTo.setSingleTransId(generateSingleTransId);
//                                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
//                                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
//                                            TxTransferTO.add(transferTo);
//                                            transactionTO.setChequeNo("SERVICE_TAX");
//                                            transferTo.setSingleTransId(generateSingleTransId);
//                                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));
//                                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
//                                            TxTransferTO.add(transferTo);
//                                        }
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
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, swachhCess/schemeCount);
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
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, krishikalyanCess/schemeCount);
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
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax/schemeCount);
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
                                
                                
                                
                                // Credit transaction of commission - end
                                // Credit transaction of bonus - start
                                if (!(map.containsKey("AUCTION_TRANS") && map.get("AUCTION_TRANS").equals("Y"))) {
                                    if (!isBonusTrans.equalsIgnoreCase("N")) {
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
                                                } else {
                                                    txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("BONUS_RECEIVABLE_HEAD"));
                                                }
                                            }
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " BONUS");
                                            txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                            txMap.put("TRANS_MOD_TYPE", "MDS");
                                            System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue());
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue() / schemeCount);
                                            transferTo.setTransId("-");
                                            transferTo.setBatchId("-");
                                            transferTo.setInitiatedBranch(BRANCH_ID);
                                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));

                                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                            // System.out.println("transferTo List 2 : " + transferTo);
                                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                            TxTransferTO.add(transferTo);
                                            transactionTO.setChequeNo("SERVICE_TAX");
                                            transferTo.setSingleTransId(generateSingleTransId);
                                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));
                                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                            TxTransferTO.add(transferTo);
                                        }
                                    }

                                }
                                // Credit transaction of bonus - end
                                // Credit transaction of Notice amount - start
                                if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNoticeAmount()).doubleValue() > 0) {
                                    transferTo = new TxTransferTO();
                                    txMap = new HashMap();
                                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("NOTICE_CHARGES_HEAD"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " NOTICE");
                                    txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                    //     System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNoticeAmount()).doubleValue());
                                    txMap.put("TRANS_MOD_TYPE", "MDS");
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNoticeAmount()).doubleValue()/schemeCount);
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setInitiatedBranch(BRANCH_ID);
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setGlTransActNum(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    TxTransferTO.add(transferTo);
                                }
                                // Credit transaction of Notice amount - end
                                // Credit transaction of Charge amount - start
                                if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getChargeAmount()).doubleValue() > 0) {
                                    transferTo = new TxTransferTO();
                                    txMap = new HashMap();
                                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("CHARGE_PAYMENT_HEAD"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " CHARGE");
                                    txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                    //System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getChargeAmount()).doubleValue());
                                    txMap.put("TRANS_MOD_TYPE", "MDS");
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getChargeAmount()).doubleValue()/schemeCount);
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setInitiatedBranch(BRANCH_ID);
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setGlTransActNum(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    TxTransferTO.add(transferTo);
                                }
                                // Credit transaction of Charge amount - end
                                // Credit transaction of arbitration amount - start
                                if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getAribitrationAmount()).doubleValue() > 0) {
                                    HashMap whereMap = new HashMap();
                                    List chargeList = null;
                                    whereMap.put("ACT_NUM", CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()) + "_" + CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getSubNo()));
                                    chargeList = (List) sqlMap.executeQueryForList("getMDSCaseChargeDetails", whereMap);
                                    if (chargeList != null && chargeList.size() > 0) {
                                        for (int j = 0; j < chargeList.size(); j++) {
                                            double chargeAmount = 0.0;
                                            String chargeType = "";
                                            whereMap = (HashMap) chargeList.get(j);
                                            chargeAmount = CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue()/schemeCount;
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
                                                transferTo.setInstrumentNo2("APPL_GL_TRANS");//                                   
                                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get(chargeType));
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put(TransferTrans.CR_BRANCH, prizedMoneyPaymentTO.getBranchCode()); //BRANCH_ID
                                                txMap.put(TransferTrans.PARTICULARS, chargeType + applicationMap.get(" MP_MDS_CODE") + "-" + objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo());
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                                transferTo = transactionDAO.addTransferCreditLocal(txMap, chargeAmount/schemeCount);
                                                transferTo.setTransId("-");
                                                transferTo.setBatchId("-");
                                                transferTo.setTransDt(currDt);
                                                transferTo.setInitiatedBranch(BRANCH_ID);
                                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                                transactionTO.setChequeNo("SERVICE_TAX");
                                                transferTo.setSingleTransId(generateSingleTransId);
                                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));
                                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                TxTransferTO.add(transferTo);
                                            }
                                        }
                                    }
                                }
                                // Credit transaction of arbitration amount - end
                                // Credit transaction of discount amount - start
                                if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getDiscountAmount()).doubleValue() > 0) {
                                    transferTo = new TxTransferTO();
                                    txMap = new HashMap();
                                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("DISCOUNT_HEAD"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " DISCOUNT");
                                    txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                    txMap.put("TRANS_MOD_TYPE", "MDS");
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getDiscountAmount()).doubleValue()/schemeCount);
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setInitiatedBranch(BRANCH_ID);
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setGlTransActNum(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    TxTransferTO.add(transferTo);
                                    transactionTO.setChequeNo("SERVICE_TAX");
                                }
                                
                                if ((thalayalChittal.equals("Y") || munnalChittal.equals("Y")) && CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()).doubleValue() > 0) {                             
                                    System.out.println("Execute inside thalayal chittal");
                                    if(thalayalChittal.equals("Y")){
                                        System.out.println("Thalayal chittal head :: " + applicationMap.get("THALAYAL_REP_PAY_HEAD"));
                                         txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("THALAYAL_REP_PAY_HEAD"));
                                    }
                                    if(munnalChittal.equals("Y")){
                                         System.out.println("Munnal chittal head :: " + applicationMap.get("MUNNAL_REP_PAY_HEAD"));
                                         txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("MUNNAL_REP_PAY_HEAD"));
                                    }
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put(TransferTrans.PARTICULARS,objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo());
                                    txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                    //Added By Suresh
                                    if (CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getDefaulters()).equals("Y") && CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue() > 0) {
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, ((CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()).doubleValue()/schemeCount)
                                                - (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue()/schemeCount)));
                                    } else {
                                        if (!(map.containsKey("AUCTION_TRANS") && map.get("AUCTION_TRANS").equals("Y"))) {//Added by sreekrishnan
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()).doubleValue()/schemeCount);
                                        } else {
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount())/schemeCount);
                                        }
                                    }
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setInitiatedBranch(BRANCH_ID);
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    TxTransferTO.add(transferTo);
                                    transactionTO.setChequeNo("SERVICE_TAX");
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    TxTransferTO.add(transferTo);
                                }

                                // Credit transaction of discount amount - end
                                   objprizedMoneyPaymentTO = new GDSPrizedMoneyPaymentTO();
                                   objprizedMoneyPaymentTO = prizedMoneyPaymentTO;
                                   objprizedMoneyPaymentTO.setChittalNo(prizedMoneyPaymentTO.getChittalNo());
                                   prizedMoneyPaymentTOLsit.add(objprizedMoneyPaymentTO);
                            }
                            // Credit transaction transfer - start - Dont divide
                            if (!prizedMoneyPaymentTO.getDefaulter_marked().equals("Y")) {
                                if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()).doubleValue() > 0 && !thalayalChittal.equals("Y") && !munnalChittal.equals("Y")) {
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
                                    } else if (!transactionTO.getProductType().equals("") && (transactionTO.getProductType().equals("TD"))) {
                                        txMap.put(TransferTrans.CR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                                        txMap.put(TransferTrans.CR_ACT_NUM, transactionTO.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_PROD_ID, transactionTO.getProductId());
                                        if ((transactionTO.getProductType().equals("TD"))) {
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                        }
                                    }
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
                                    } else if (transactionTO.getProductType().equals("TD")) {
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                    } else {
                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                    }
                                    txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo());
                                    txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                    //Added By Suresh
                                    if (CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getDefaulters()).equals("Y") && CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue() > 0) {
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()).doubleValue()
                                                - CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue()));
                                    } else {
                                        if (!(map.containsKey("AUCTION_TRANS") && map.get("AUCTION_TRANS").equals("Y"))) {//Added by sreekrishnan
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()).doubleValue());
                                        } else {
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()));
                                        }
                                    }
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setInitiatedBranch(BRANCH_ID);
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                    //                            if(!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")){
                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                    //                            }
                                    // System.out.println("transferTo List 2 : " + transferTo);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    TxTransferTO.add(transferTo);
                                    transactionTO.setChequeNo("SERVICE_TAX");
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    TxTransferTO.add(transferTo);
                                }
                            }
                            // Credit transaction transfer - end     
                        }
                 
                        if (CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getDefaulters()).equals("Y")) {
                            if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPenalAmount()).doubleValue() > 0) {
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("PENAL_INTEREST_HEAD"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " PENAL");
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
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
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
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " OVERDUE");
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
                                
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
                            }
                        }
                        // Defaulter selected - code starts here
                        if (prizedMoneyPaymentTO.getDefaulter_marked().equals("Y")) {
                            HashMap account_map = new HashMap();
                            account_map.put("CHITTAL_NO", prizedMoneyPaymentTO.getGdsNo());
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
                                defaulter_bonus_recoverd = prizedMoneyPaymentTO.getDefaulter_bonus_recoverd();
                            }
                            if (prizedMoneyPaymentTO.getDefaulter_comm() != null && prizedMoneyPaymentTO.getDefaulter_comm() > 0.0) {
                                defaulter_comm = prizedMoneyPaymentTO.getDefaulter_comm();                  
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_AC_HD, Misclens);
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " OVERDUE");
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
                                
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
                            }
                            if (prizedMoneyPaymentTO.getDefaulter_interst() != null && prizedMoneyPaymentTO.getDefaulter_interst() > 0.0) {
                                defaulter_Inteset = prizedMoneyPaymentTO.getDefaulter_interst();
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_AC_HD, interstAccountHead);
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " OVERDUE");
                                txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                System.out.println("txMap iii: " + txMap + "serviceAmtiiii :" +prizedMoneyPaymentTO.getOverdueAmount());
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, defaulter_Inteset);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));                                
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
                            }
                            double paidamount = 0.0;
                            HashMap mdsMap = new HashMap();
                            mdsMap.put("CHITTAL_NO", prizedMoneyPaymentTO.getGdsNo());
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
                                    + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue();                        ;
                            double amountToReciept = TotDet - paidamount;                        
                            double bonusTogive = 0.0;
                            HashMap mdsbonusMap = new HashMap();
                            mdsbonusMap.put("CHITTAL_NO", prizedMoneyPaymentTO.getGdsNo());
                            mdsbonusMap.put("SUB_NO", CommonUtil.convertObjToInt(prizedMoneyPaymentTO.getSubNo()));
                            mdsbonusMap.put("SCHME_NAME", prizedMoneyPaymentTO.getGroupName());
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
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " OVERDUE");
                                txMap.put(TransferTrans.DR_BRANCH,BRANCH_ID);
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");                         
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, bonusTogive);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));                                
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
                            }
                            if (amountToReciept > 0.0) {                             
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_AC_HD, RecieptAccountHead);
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " OVERDUE");
                                txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");                          
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, amountToReciept);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
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
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " OVERDUE");
                                txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");                            
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, paidamount);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));                                
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
                            }

                            TotDet = TotDet + bonusTogive;
                            double NtMislns = TotDet - (amountToReciept + paidamount + defaulter_Inteset + defaulter_comm + prizedMoneyPaymentTO.getCommisionAmount() + prizedMoneyPaymentTO.getBonusAmount());                           
                            if (NtMislns > 0.0) {
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_AC_HD, Misclens);
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " OVERDUE");
                                txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put("TRANS_MOD_TYPE", "MDS");                             
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, NtMislns);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));                                
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
                            } else if (NtMislns < 0.0) {
                                ExecptionDeftler = true;
                                throw new TTException("Cannot make Defaulter Payment !!!");
                            }
                        }
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getRefundAmount()).doubleValue() > 0) {   //Cr Refund_Amount                           
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
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " REFUND");
                            txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");                         
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getRefundAmount()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));                            
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            TxTransferTO.add(transferTo);
                            transactionTO.setChequeNo("SERVICE_TAX");
                        }                      
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getRefundAmount()).doubleValue() > 0 || map.containsKey("REFUND_AMOUNT")) {  //Cr Reverse_Bonus_Amount
                            double earnedBonus = 0.0;
                            if (applicationMap.get("BONUS_EXISTING_CHITTAL") != null && !applicationMap.get("BONUS_EXISTING_CHITTAL").equals("") && (applicationMap.get("BONUS_EXISTING_CHITTAL").equals("Y"))) {
                                HashMap hashMap = new HashMap();
                                hashMap.put("CHITTAL_NO", prizedMoneyPaymentTO.getGdsNo());
                                List paidList = sqlMap.executeQueryForList("getTotalInstAmount", hashMap);
                                if (paidList != null && paidList.size() > 0) {
                                    hashMap = (HashMap) paidList.get(0);
                                    earnedBonus = CommonUtil.convertObjToDouble(hashMap.get("PAID_AMT")).doubleValue() - CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getRefundAmount()).doubleValue();
                                }
                                if (earnedBonus > 0) {                     
                                    transferTo = new TxTransferTO();
                                    txMap = new HashMap();
                                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);                                
                                    if (map.containsKey("MDS_CLOSURE")) {
                                        txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("SUNDRY_RECEIPT_HEAD"));
                                    } else {
                                        txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("BONUS_PAYABLE_HEAD"));
                                    }
                                    txMap.put("TRANS_MOD_TYPE", "MDS");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " REVERSE_BONUS");
                                    txMap.put(TransferTrans.CR_BRANCH,map.get("SELECTED_BRANCH_ID"));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, earnedBonus);
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setInitiatedBranch(BRANCH_ID);
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    
                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    TxTransferTO.add(transferTo);
                                    transactionTO.setChequeNo("SERVICE_TAX");
                                }
                            }
                        }
                       // Defaulter selected - ends starts here
                        HashMap applnMap = new HashMap();
                        transferDAO = new TransferDAO();
                        map.put("MODE", map.get("COMMAND"));
                        map.put("COMMAND", map.get("MODE"));
                        map.put("TxTransferTO", TxTransferTO);
                      //  System.out.println("reckjnjfgnfrtdd hererer" + map);
                        transMap = transferDAO.execute(map, false);
                        prizedMoneyPaymentTO.setTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                        System.out.println("transMap : " + transMap);
                        transactionDAO.setBatchId(prizedMoneyPaymentTO.getTransId());
                        transactionDAO.setBatchDate(currDt);
                        
                        // Updating trans id
                         HashMap transIdMap=new HashMap();
                        transIdMap.put("TRANS_ID",transMap.get("TRANS_ID"));
                        transIdMap.put("gds_No", prizedMoneyPaymentTO.getGdsNo());
                        transIdMap.put("GDS_PRIZED_MONEY_PAYMENT","GDS_PRIZED_MONEY_PAYMENT");
                        sqlMap.executeUpdate("updateTransIdGDSApplication", transIdMap);
                        //End
                     
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
                        System.out.println("Cash transaction started");
                        transactionTO.setChequeNo("SERVICE_TAX");
                        HashMap cashTransMap = new HashMap();
                        //Total Amount (Prized Amount)
                        cashTransMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                        cashTransMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                        cashTransMap.put("PENAL_DETAILS", "PENAL_DETAILS");
                        cashTransMap.put("LINK_BATCH_ID", prizedMoneyPaymentTO.getGdsNo());
                        cashTransMap.put("PAYMENT_HEAD", applicationMap.get("PAYMENT_HEAD"));
                        cashTransMap.put("MDS_PAYABLE_HEAD", applicationMap.get("MDS_PAYABLE_HEAD"));
                        cashTransMap.put("SCHEME_NAME", prizedMoneyPaymentTO.getGroupName());
                        cashTransMap.put("USER_ID", map.get("USER_ID"));
                        cashTransMap.put("TRANS_MOD_TYPE", "MDS");
                        //  System.out.println("prizeddd%#$%#$%"+CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()));
                        if ((map.containsKey("TRASFER_BONUS_COMMISION") && map.get("TRASFER_BONUS_COMMISION").equals("Y"))) {
                            cashTransMap.put("TOTAL_AMOUNT", String.valueOf(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue()/schemeCount));                            
                        } else {
                            System.out.println("AUCTION_TRANS^$$^$^$" + map.get("AUCTION_TRANS"));
                            if (!(map.containsKey("AUCTION_TRANS") && map.get("AUCTION_TRANS").equals("Y"))) {
                                cashTransMap.put("TOTAL_AMOUNT", String.valueOf(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue()/schemeCount
                                        + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue()/schemeCount
                                        + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue()/schemeCount));
                            } else {                                
                                cashTransMap.put("TOTAL_AMOUNT", String.valueOf(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue()));
                            }
                        }
                        //Changed By Suresh
                        if (map.containsKey("MDS_CLOSURE")) {
                            cashTransMap.put("MDS_CLOSURE", "MDS_CLOSURE");
                        }
                        CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                        ArrayList applncashList = newApplicationList(map, generateSingleTransId, allApplnDetails);
                        System.out.println("applncashList :: 1 ::"+ applncashList);
                        cashTransMap.put("DAILYDEPOSITTRANSTO", applncashList);
                        cashTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                        cashTransMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                        HashMap cashMap = cashTransactionDAO.execute(cashTransMap, false);
                        prizedMoneyPaymentTO.setCashId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));

                        //Insert Remit _Issue_Trans
                        transactionTO.setBatchId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                        transactionTO.setBatchDt(currDt);
                        transactionTO.setTransId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                        transactionTO.setStatus(prizedMoneyPaymentTO.getStatus());
                        transactionTO.setBranchId(_branchCode);
                        
                        sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", transactionTO);
                        //Update LinkBatchId as Trans_ID
                        HashMap linkBatchMap = new HashMap();
                        linkBatchMap.put("LINK_BATCH_ID", cashMap.get("TRANS_ID"));
                        linkBatchMap.put("TRANS_ID", cashMap.get("TRANS_ID"));
                        linkBatchMap.put("TRANS_DT", currDt);
                        linkBatchMap.put("INITIATED_BRANCH", BRANCH_ID);
                        sqlMap.executeUpdate("updateLinkBatchIdCash", linkBatchMap);
                        linkBatchMap = null;
                        
                        ArrayList cashList = getCashListForOtherCashTransactions(map, generateSingleTransId, allApplnDetails);
                        //Added by sreekrishnan for kottayam
                        
                        if (cashList != null && cashList.size() > 0) {
                            doCashTrans(cashList, _branchCode, false);
                        }
                    }                  
                    
                    if (allApplnDetails != null && allApplnDetails.size() > 0) {
                        int rowCount = allApplnDetails.size();
                        if(isBonusTrans.equalsIgnoreCase("N")){
                          prizedMoneyPaymentTO.setBonusAmount(0.0);      
                        }else{
                          prizedMoneyPaymentTO.setBonusAmount(prizedMoneyPaymentTO.getBonusAmount().doubleValue()/rowCount);    
                        }                                            
                        prizedMoneyPaymentTO.setCommisionAmount(prizedMoneyPaymentTO.getCommisionAmount().doubleValue()/rowCount);
                        prizedMoneyPaymentTO.setDiscountAmount(prizedMoneyPaymentTO.getDiscountAmount().doubleValue()/rowCount);
                        prizedMoneyPaymentTO.setNoticeAmount(prizedMoneyPaymentTO.getNoticeAmount().doubleValue()/rowCount);
                        prizedMoneyPaymentTO.setAribitrationAmount(prizedMoneyPaymentTO.getAribitrationAmount().doubleValue()/rowCount);
                        prizedMoneyPaymentTO.setPrizedAmount(prizedMoneyPaymentTO.getPrizedAmount().doubleValue()/rowCount);
                        prizedMoneyPaymentTO.setNetAmount(prizedMoneyPaymentTO.getNetAmount().doubleValue()/rowCount);
                            for (int i = 0; i < allApplnDetails.size(); i++) {
                                GDSApplicationTO objGDSApplicationTO = (GDSApplicationTO)allApplnDetails.get(i);
                                prizedMoneyPaymentTO.setSchemeName(objGDSApplicationTO.getSchemeName()); 
                                prizedMoneyPaymentTO.setChittalNo(objGDSApplicationTO.getChittalNo());  
                                prizedMoneyPaymentTO.setPaidStatus("N");
                                sqlMap.executeUpdate("insertGDSPrizedMoneyPaymentTO", prizedMoneyPaymentTO);
                           }
                    }       
          
            if (ExecptionDeftler) {
                sqlMap.rollbackTransaction();

            } else {
                sqlMap.commitTransaction();
                //ExecptionDeftler=true;
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
        }
    }
    
    private void transferBonusCommision(HashMap map, HashMap applnMap, GDSPrizedMoneyPaymentTO prizedTo, String singleTransId, int schemeCnt, List allApplnDetails) throws Exception {
        try { 
            System.out.println("Inside transferBonusCommision");
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
            System.out.println("bonusAmt#%#%%#%# trans " + prizedTo.getBonusAmount());
            String chittalNo = CommonUtil.convertObjToStr(prizedTo.getGdsNo());
            String subNo = CommonUtil.convertObjToStr(prizedTo.getSubNo());
            String isBonusTransfer = "";
             if(map.containsKey("IS_BONUS_TRANSFER") && map.get("IS_BONUS_TRANSFER") != null){
                isBonusTransfer = CommonUtil.convertObjToStr(map.get("IS_BONUS_TRANSFER"));
            }
            if (allApplnDetails != null && allApplnDetails.size() > 0) {
                int schemeCount = allApplnDetails.size();
                HashMap applicationMap = new HashMap();
                for (int i = 0; i < schemeCount; i++) {
                    GDSApplicationTO objGDSApplicationTO = (GDSApplicationTO) allApplnDetails.get(i);
                    HashMap schemeMap = new HashMap();
                    schemeMap.put("SCHEME_NAME", objGDSApplicationTO.getSchemeName());
                    List headlst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", schemeMap);
                    if (headlst != null && headlst.size() > 0) {
                        applicationMap = (HashMap) headlst.get(0);
                    }
                    if (CommonUtil.convertObjToDouble(prizedTo.getBonusAmount()) + CommonUtil.convertObjToDouble(prizedTo.getCommisionAmount()) > 0) {
                        double serviceTax = 0.0;
                        if(!isBonusTransfer.equalsIgnoreCase("N")){
                            if (CommonUtil.convertObjToDouble(prizedTo.getBonusAmount()) > 0) {
                                System.out.println("Bonus Started");
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("BONUS_PAYABLE_HEAD"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                txMap.put(TransferTrans.PARTICULARS, "Bonus " + applicationMap.get("MP_MDS_CODE") + "-" + objGDSApplicationTO.getChittalNo() + "_" + subNo);
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                txMap.put("SCREEN_NAME", "GDS Prized Money Payment");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedTo.getBonusAmount()) / schemeCount);
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
                                transferTo.setGlTransActNum(objGDSApplicationTO.getChittalNo());
                                transferTo.setSingleTransId(CommonUtil.convertObjToStr(singleTransId));
                                transactionTO.setChequeNo("SERVICE_TAX");
                                TxTransferTO.add(transferTo);
                            }
                            System.out.println("TxTransferTO List 4 : " + TxTransferTO);
                        }                        
                        if (CommonUtil.convertObjToDouble(prizedTo.getCommisionAmount()) > 0) {
                            System.out.println("commission Transaction Started");
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("COMMISION_HEAD"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                            txMap.put(TransferTrans.PARTICULARS, "Commission " + applicationMap.get("MP_MDS_CODE") + "-" + objGDSApplicationTO.getChittalNo() + "_" + subNo);
                            txMap.put("TRANS_MOD_TYPE", "MDS");
                            txMap.put("SCREEN_NAME", "GDS Prized Money Payment");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedTo.getCommisionAmount()) / schemeCount);
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
                            transferTo.setGlTransActNum(objGDSApplicationTO.getChittalNo());
                            transferTo.setSingleTransId(CommonUtil.convertObjToStr(singleTransId));
                            transactionTO.setChequeNo("SERVICE_TAX");
                            TxTransferTO.add(transferTo);
                            System.out.println("transferTo List 6 : commission" + TxTransferTO);
                            
                        }
                        System.out.println("debit Started");
                        transferTo = new TxTransferTO();
                        txMap = new HashMap();
                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
                        txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("PAYMENT_HEAD"));
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                        if(isBonusTransfer.equalsIgnoreCase("N")){
                           txMap.put(TransferTrans.PARTICULARS, "Commission " + applicationMap.get("MP_MDS_CODE") + "-" + objGDSApplicationTO.getChittalNo() + "_" + subNo);
                        }else{
                           txMap.put(TransferTrans.PARTICULARS, "Bonus And Commission " + applicationMap.get("MP_MDS_CODE") + "-" + objGDSApplicationTO.getChittalNo() + "_" + subNo); 
                        }                        
                        txMap.put("TRANS_MOD_TYPE", "MDS");
                        txMap.put("SCREEN_NAME", "GDS Prized Money Payment");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        if(isBonusTransfer.equalsIgnoreCase("N")){
                           transferTo = transactionDAO.addTransferDebitLocal(txMap, (CommonUtil.convertObjToDouble(prizedTo.getCommisionAmount()) + serviceTax) / schemeCount); 
                        }else{
                           transferTo = transactionDAO.addTransferDebitLocal(txMap, (CommonUtil.convertObjToDouble(prizedTo.getBonusAmount()) + CommonUtil.convertObjToDouble(prizedTo.getCommisionAmount()) + serviceTax) / schemeCount); 
                        }                        
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
                        transferTo.setGlTransActNum(objGDSApplicationTO.getChittalNo());
                        transferTo.setSingleTransId(CommonUtil.convertObjToStr(singleTransId));
                        transactionTO.setChequeNo("SERVICE_TAX");
                        TxTransferTO.add(transferTo);
                    }                    
                }     
                map.put("MODE", map.get("COMMAND"));
                map.put("COMMAND", map.get("MODE"));
                map.put("TxTransferTO", TxTransferTO);
                System.out.println("transferDAO List Last : " + map);
                HashMap transMap = transferDAO.execute(map, false);
                System.out.println("transferDAO List transMap : " + transMap);                
                HashMap linkBatchMap = new HashMap();                
                linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
                linkBatchMap.put("BATCH_ID", transMap.get("TRANS_ID"));
                linkBatchMap.put("TRANS_DT", currDt);
                linkBatchMap.put("INITIATED_BRANCH", _branchCode);
                sqlMap.executeUpdate("updateLinkBatchIdTransfer", linkBatchMap);
                linkBatchMap = null;
                transMap = null;                
            }          
                        
        } catch (Exception e) {
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
        data.put("SCREEN_NAME", "GDS Prized Money Payment");
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
            if(dataMap.containsKey("GL_TRANS_ACT_NUM") && dataMap.get("GL_TRANS_ACT_NUM") != null && !dataMap.get("GL_TRANS_ACT_NUM").equals("")){
                objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(dataMap.get("GL_TRANS_ACT_NUM")));
            }else{
                objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
            }            
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
                    objCashTO.setAuthorizeStatus_2("");
                }else
                    objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            }else{
                objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            }
            objCashTO.setSingleTransId(generateSingleTransId);
            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
            objCashTO.setCommand("INSERT");
            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
            if (dataMap.containsKey(CommonConstants.SCREEN) && dataMap.get(CommonConstants.SCREEN) != null) {
                objCashTO.setScreenName((String) dataMap.get(CommonConstants.SCREEN));
            }
            objCashTO.setIbrHierarchy(CommonUtil.convertObjToStr(ibrHierarchy++));
            System.out.println("objCashTO 1st one:" + objCashTO);
        }
        return objCashTO;
    }

    private CashTransactionTO createCashTransactionTODefulter(HashMap dataMap, String generateSingleTransId) throws Exception {
        CashTransactionTO objCashTO = new CashTransactionTO();
        if (dataMap.get("STATUS").equals(CommonConstants.TOSTATUS_INSERT)) {
            objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
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
            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
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
                applicationMap.put("GROUP_NO", prizedMoneyPaymentTO.getGroupName());
                List lst = sqlMap.executeQueryForList("getSelectAllSchemeAcctHead", applicationMap);
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
                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
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
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " PAYMENT");
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
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
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
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo());
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
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
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
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " COMMISION");
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
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                            TxTransferTO.add(transferTo);
                            transactionTO.setChequeNo("SERVICE_TAX");
                            TxTransferTO.add(transferTo);
						//added by chithra for service Tax
                            if (map.containsKey("serviceTaxDetails")) {
                                HashMap serTaxMap = (HashMap) map.get("serviceTaxDetails");

                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_AC_HD, serTaxMap.get("TAX_HEAD_ID"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " SERVICE TAX");
                                txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT")));
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                TxTransferTO.add(transferTo);
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
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " BONUS");
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
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
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
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " NOTICE");
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
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
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
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " CASE EXPENSE");
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
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
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
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " DISCOUNT");
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
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
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
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " REFUND");
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
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                            TxTransferTO.add(transferTo);
                            transactionTO.setChequeNo("SERVICE_TAX");
                        }
                        System.out.println("####### $$$$$$ " + map.get("REFUND_AMOUNT"));
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getRefundAmount()).doubleValue() > 0 || map.containsKey("REFUND_AMOUNT")) {  //Cr Reverse_Bonus_Amount
                            double earnedBonus = 0.0;
                            if (applicationMap.get("BONUS_EXISTING_CHITTAL") != null && !applicationMap.get("BONUS_EXISTING_CHITTAL").equals("") && (applicationMap.get("BONUS_EXISTING_CHITTAL").equals("Y"))) {
                                HashMap hashMap = new HashMap();
                                hashMap.put("CHITTAL_NO", prizedMoneyPaymentTO.getGdsNo());
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
                                    txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " REVERSE_BONUS");
                                    txMap.put(TransferTrans.CR_BRANCH,map.get("SELECTED_BRANCH_ID"));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, earnedBonus);
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setInitiatedBranch(BRANCH_ID);
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
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
                        cashTransMap.put("LINK_BATCH_ID", prizedMoneyPaymentTO.getGdsNo());
                        cashTransMap.put("PAYMENT_HEAD", applicationMap.get("PAYMENT_HEAD"));
                        cashTransMap.put("MDS_PAYABLE_HEAD", applicationMap.get("MDS_PAYABLE_HEAD"));
                        cashTransMap.put("SCHEME_NAME", prizedMoneyPaymentTO.getGroupName());
                        cashTransMap.put("USER_ID", map.get("USER_ID"));
                        //Changed By Suresh
                        if (map.containsKey("MDS_CLOSURE")) {
                            cashTransMap.put("MDS_CLOSURE", "MDS_CLOSURE");
                        }
                        CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                        ArrayList applncashList = applicationList(cashTransMap);
                        cashTransMap.put("DAILYDEPOSITTRANSTO", applncashList);
                        cashTransMap.put("SCREEN_NAME", "GDS Prized Money Payment");
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
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " PAYMENT");
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
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                            
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
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " COMMISION");
                            txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                            System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue());
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                            TxTransferTO.add(transferTo);
                            transactionTO.setChequeNo("SERVICE_TAX");
                            
                            TxTransferTO.add(transferTo);
						//added by chithra for service Tax
                            if (map.containsKey("serviceTaxDetails")) {
                                HashMap serTaxMap = (HashMap) map.get("serviceTaxDetails");

                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_AC_HD, serTaxMap.get("TAX_HEAD_ID"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " SERVICE TAX");
                                txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT")));
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                TxTransferTO.add(transferTo);
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
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " BONUS");
                            txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                            System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue());
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
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
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " NOTICE");
                            txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                            System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNoticeAmount()).doubleValue() + " NOTICE CHARGE");
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNoticeAmount()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                            
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
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " CASE EXPENSE");
                            txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                            System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getAribitrationAmount()).doubleValue());
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getAribitrationAmount()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                            
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
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " DISCOUNT");
                            txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                            System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getDiscountAmount()).doubleValue());
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getDiscountAmount()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                            
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
                            txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " PENAL");
                            txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                            System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPenalAmount()).doubleValue());
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPenalAmount()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                            
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
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " REFUND_PAYMENT");
                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, refundAmount);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
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
                                txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " REFUND");
                                txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                System.out.println("txMap : " + txMap + "serviceAmt :" + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getRefundAmount()).doubleValue());
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getRefundAmount()).doubleValue());
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                TxTransferTO.add(transferTo);
                                transactionTO.setChequeNo("SERVICE_TAX");
                            }

                            if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getRefundAmount()).doubleValue() > 0 || map.containsKey("REFUND_AMOUNT")) {  //Cr Reverse_Bonus_Amount
                                double earnedBonus = 0.0;
                                if (applicationMap.get("BONUS_EXISTING_CHITTAL") != null && !applicationMap.get("BONUS_EXISTING_CHITTAL").equals("") && (applicationMap.get("BONUS_EXISTING_CHITTAL").equals("Y"))) {
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("CHITTAL_NO", prizedMoneyPaymentTO.getGdsNo());
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
                                        txMap.put(TransferTrans.PARTICULARS, prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " REVERSE_BONUS");
                                        txMap.put(TransferTrans.CR_BRANCH, map.get("SELECTED_BRANCH_ID"));
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, earnedBonus);
                                        transferTo.setTransId("-");
                                        transferTo.setBatchId("-");
                                        transferTo.setInitiatedBranch(BRANCH_ID);
                                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
                                        
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

   // Start code for making transaction cashlist
    private ArrayList newApplicationList(HashMap map, String generateSingleTransId, List allApplnDetails) throws Exception {    
        System.out.println("allApplnDetails :" + allApplnDetails);
        System.out.println("objCashTransactionTO :" + prizedMoneyPaymentTO);
        String isBonusTransfer = "";
        if (map.containsKey("IS_BONUS_TRANSFER") && map.get("IS_BONUS_TRANSFER") != null) {
            isBonusTransfer = CommonUtil.convertObjToStr(map.get("IS_BONUS_TRANSFER"));
        }
        ArrayList cashList = new ArrayList();
        if (allApplnDetails != null && allApplnDetails.size() > 0) {
            int schemeCount = allApplnDetails.size();
            HashMap applnMap = new HashMap();
            for (int i = 0; i < schemeCount; i++) {
                GDSApplicationTO objGDSApplicationTO = (GDSApplicationTO) allApplnDetails.get(i);
                HashMap schemeMap = new HashMap();
                schemeMap.put("SCHEME_NAME", objGDSApplicationTO.getSchemeName());
                List headlst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", schemeMap);
                if (headlst != null && headlst.size() > 0) {
                    applnMap = (HashMap) headlst.get(0);
                }
             
                applnMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                applnMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                applnMap.put("PENAL_DETAILS", "PENAL_DETAILS");
                applnMap.put("LINK_BATCH_ID", objGDSApplicationTO.getChittalNo());              
                applnMap.put("SCHEME_NAME", objGDSApplicationTO.getSchemeName());
                applnMap.put("USER_ID", map.get("USER_ID"));
                applnMap.put("TRANS_MOD_TYPE", "MDS");
                if ((map.containsKey("TRASFER_BONUS_COMMISION") && map.get("TRASFER_BONUS_COMMISION").equals("Y"))) {
                    applnMap.put("TOTAL_AMOUNT", String.valueOf(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue()/schemeCount));                    
                } else {
                    System.out.println("AUCTION_TRANS^$$^$^$" + map.get("AUCTION_TRANS"));
                    if (!(map.containsKey("AUCTION_TRANS") && map.get("AUCTION_TRANS").equals("Y"))) {
                        if(isBonusTransfer.equalsIgnoreCase("N")){
                           applnMap.put("TOTAL_AMOUNT", String.valueOf(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue()/schemeCount                                
                                + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue()/schemeCount)); 
                        }else{
                           applnMap.put("TOTAL_AMOUNT", String.valueOf(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue()/schemeCount
                                + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue()/schemeCount
                                + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue()/schemeCount)); 
                        }                        
                    } else {                        
                        applnMap.put("TOTAL_AMOUNT", String.valueOf(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue()/schemeCount));
                    }
                }             
                if (map.containsKey("MDS_CLOSURE")) {
                    applnMap.put("MDS_CLOSURE", "MDS_CLOSURE");
                }              
                CashTransactionTO objCashTO = new CashTransactionTO();
                if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()).doubleValue() > 0) {
                    objCashTO.setTransId("");
                    objCashTO.setProdType("GL");
                    if (applnMap.containsKey("MDS_CLOSURE")) {
                        objCashTO.setAcHdId(CommonUtil.convertObjToStr(applnMap.get("MDS_PAYABLE_HEAD")));
                    } else {
                        objCashTO.setAcHdId(CommonUtil.convertObjToStr(applnMap.get("PAYMENT_HEAD")));
                    }
                    objCashTO.setInpAmount(CommonUtil.convertObjToDouble(applnMap.get("TOTAL_AMOUNT")));
                    objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));
                    objCashTO.setAmount(CommonUtil.convertObjToDouble(applnMap.get("TOTAL_AMOUNT")));
                    objCashTO.setTransType(CommonConstants.DEBIT);
                    objCashTO.setInitTransId(CommonUtil.convertObjToStr(applnMap.get("USER_ID")));
                    objCashTO.setBranchId(CommonUtil.convertObjToStr(applnMap.get("SELECTED_BRANCH_ID")));
                    objCashTO.setStatusBy(CommonUtil.convertObjToStr(applnMap.get("USER_ID")));
                    objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(applnMap.get("LINK_BATCH_ID")));
                    objCashTO.setStatusDt(prizedMoneyPaymentTO.getStatusDt());
                    List listData = ServerUtil.executeQuery("getCashierAuth", new HashMap());
                    if (listData != null && listData.size() > 0) {
                        HashMap map1 = (HashMap) listData.get(0);
                        if (map1.get("CASHIER_AUTH_ALLOWED") != null && map1.get("CASHIER_AUTH_ALLOWED").toString().equals("Y") && objCashTO.getTransType().equals(CommonConstants.CREDIT)) {
                            objCashTO.setAuthorizeStatus_2("");
                        } else {
                            objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                        }
                    } else {
                        objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    }
                    objCashTO.setParticulars(objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo());
                    objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
                    objCashTO.setInitChannType(CommonConstants.CASHIER);
                    objCashTO.setSingleTransId(generateSingleTransId);
                    objCashTO.setTransModType(CommonUtil.convertObjToStr(applnMap.get("TRANS_MOD_TYPE")));
                    objCashTO.setCommand(prizedMoneyPaymentTO.getCommand());
                    objCashTO.setScreenName("GDS Prized Money Payment");
                    System.out.println("objCashTO 1st one:" + objCashTO);
                    cashList.add(objCashTO);
                }
                objCashTO = null;
            }
        }
        return cashList;
    }

    // Start code for making transaction cashlist
    private ArrayList getCashListForOtherCashTransactions(HashMap map, String generateSingleTransId, List allApplnDetails) throws Exception {
        System.out.println("allApplnDetails : inside getCashListForOtherCashTransactions :: " + allApplnDetails);
        System.out.println("objCashTransactionTO : inside getCashListForOtherCashTransactions :: " + prizedMoneyPaymentTO);
        ArrayList cashList = new ArrayList();
        String isBonusTransfer = "";
        if (map.containsKey("IS_BONUS_TRANSFER") && map.get("IS_BONUS_TRANSFER") != null) {
            isBonusTransfer = CommonUtil.convertObjToStr(map.get("IS_BONUS_TRANSFER"));
        }
        if (allApplnDetails != null && allApplnDetails.size() > 0) {
            int schemeCount = allApplnDetails.size();
            HashMap applnMap = new HashMap();
            for (int i = 0; i < schemeCount; i++) {
                GDSApplicationTO objGDSApplicationTO = (GDSApplicationTO) allApplnDetails.get(i);
                HashMap schemeMap = new HashMap();
                schemeMap.put("SCHEME_NAME", objGDSApplicationTO.getSchemeName());
                List headlst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", schemeMap);
                if (headlst != null && headlst.size() > 0) {
                    applnMap = (HashMap) headlst.get(0);
                }
                applnMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                applnMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                applnMap.put("PENAL_DETAILS", "PENAL_DETAILS");
                applnMap.put("LINK_BATCH_ID", objGDSApplicationTO.getChitNo());
                applnMap.put("SCHEME_NAME", objGDSApplicationTO.getSchemeName());
                applnMap.put("USER_ID", map.get("USER_ID"));
                applnMap.put("TRANS_MOD_TYPE", "MDS");
                if ((map.containsKey("TRASFER_BONUS_COMMISION") && map.get("TRASFER_BONUS_COMMISION").equals("Y"))) {
                    applnMap.put("TOTAL_AMOUNT", String.valueOf(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue() / schemeCount));
                } else {
                    System.out.println("AUCTION_TRANS^$$^$^$" + map.get("AUCTION_TRANS"));
                    if (!(map.containsKey("AUCTION_TRANS") && map.get("AUCTION_TRANS").equals("Y"))) {
                        applnMap.put("TOTAL_AMOUNT", String.valueOf(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue() / schemeCount
                                + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue() / schemeCount
                                + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue() / schemeCount));
                    } else {
                        applnMap.put("TOTAL_AMOUNT", String.valueOf(CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue() / schemeCount));
                    }
                }
                if (map.containsKey("MDS_CLOSURE")) {
                    applnMap.put("MDS_CLOSURE", "MDS_CLOSURE");
                }
                if (!(map.containsKey("TRASFER_BONUS_COMMISION") && map.get("TRASFER_BONUS_COMMISION").equals("Y"))) {
                    if (!(map.containsKey("AUCTION_TRANS") && map.get("AUCTION_TRANS").equals("Y"))) {//Added by sreekrishnan
                        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue() > 0) {
                            HashMap MDSMap = new HashMap();
                            MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                            MDSMap.put("BRANCH_CODE", _branchCode);
                            MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                            MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                            MDSMap.put("PARTICULARS", objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " COMMISION");
                            MDSMap.put("ACCT_HEAD", applnMap.get("COMMISION_HEAD"));
                            MDSMap.put("TRANS_AMOUNT", CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()) / schemeCount);
                            MDSMap.put("AUTHORIZEREMARKS", "COMMISION");
                            MDSMap.put("TRANS_MOD_TYPE", "MDS");
                            MDSMap.put("GL_TRANS_ACT_NUM",objGDSApplicationTO.getChittalNo());
                            MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                            cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
//                            if (map.containsKey("serviceTaxDetails")) {
//                                HashMap serTaxMap = (HashMap) map.get("serviceTaxDetails");
//                                MDSMap = new HashMap();
//                                MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
//                                MDSMap.put("BRANCH_CODE", _branchCode);
//                                MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
//                                MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
//                                MDSMap.put("PARTICULARS", objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " SERVICE TAX");
//                                MDSMap.put("ACCT_HEAD", serTaxMap.get("TAX_HEAD_ID"));
//                                MDSMap.put("TRANS_AMOUNT", CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT")) / schemeCount);
//                                MDSMap.put("AUTHORIZEREMARKS", "SERVICE TAX");
//                                MDSMap.put("TRANS_MOD_TYPE", "MDS");
//                                MDSMap.put("GL_TRANS_ACT_NUM",objGDSApplicationTO.getChittalNo());
//                                MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
//                                cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
//                            }


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
                                    MDSMap.put("PARTICULARS", objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " CGST");
                                    MDSMap.put("ACCT_HEAD", serTaxMap.get("SWACHH_HEAD_ID"));
                                    MDSMap.put("TRANS_AMOUNT", swachhCess/schemeCount);
                                    MDSMap.put("AUTHORIZEREMARKS", "SERVICE TAX");
                                    MDSMap.put("TRANS_MOD_TYPE", "MDS");
                                    MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                                    MDSMap.put("INSTRUMENT_NO2", "CGST");// Added by nithya on 31-10-2019 for KD-680
                                    cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                                    //serTaxAmt -= swachhCess;
                                }
                                if (krishikalyanCess > 0) {
                                    MDSMap = new HashMap();
                                    MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                                    MDSMap.put("BRANCH_CODE", _branchCode);
                                    MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                    MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                                    MDSMap.put("PARTICULARS", objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " SGST");
                                    MDSMap.put("ACCT_HEAD", serTaxMap.get("KRISHIKALYAN_HEAD_ID"));
                                    MDSMap.put("TRANS_AMOUNT", krishikalyanCess/schemeCount);
                                    MDSMap.put("AUTHORIZEREMARKS", "SERVICE TAX");
                                    MDSMap.put("TRANS_MOD_TYPE", "MDS");
                                    MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                                    MDSMap.put("INSTRUMENT_NO2", "SGST");// Added by nithya on 31-10-2019 for KD-680
                                    cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                                    //serTaxAmt -= krishikalyanCess;
                                }
                                if (normalServiceTax > 0) {
                                    MDSMap = new HashMap();
                                    MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                                    MDSMap.put("BRANCH_CODE", _branchCode);
                                    MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                    MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                                    MDSMap.put("PARTICULARS", objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " SERVICE TAX");
                                    MDSMap.put("ACCT_HEAD", serTaxMap.get("TAX_HEAD_ID"));
                                    MDSMap.put("TRANS_AMOUNT", normalServiceTax/schemeCount);
                                    MDSMap.put("AUTHORIZEREMARKS", "SERVICE TAX");
                                    MDSMap.put("TRANS_MOD_TYPE", "MDS");
                                    MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                                    MDSMap.put("INSTRUMENT_NO2", "SERVICE_TAX");// Added by nithya on 31-10-2019 for KD-680
                                    cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                                }
                            }
                        }
                    }else{
                                if (map.containsKey("serviceTaxDetails")) {
                                HashMap MDSMap = new HashMap();
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
                                    MDSMap.put("PARTICULARS", objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " CGST");
                                    MDSMap.put("ACCT_HEAD", serTaxMap.get("SWACHH_HEAD_ID"));
                                    MDSMap.put("TRANS_AMOUNT", swachhCess/schemeCount);
                                    MDSMap.put("AUTHORIZEREMARKS", "SERVICE TAX");
                                    MDSMap.put("TRANS_MOD_TYPE", "MDS");
                                    MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                                    MDSMap.put("INSTRUMENT_NO2", "CGST");// Added by nithya on 31-10-2019 for KD-680
                                    cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                                    //serTaxAmt -= swachhCess;
                                }
                                if (krishikalyanCess > 0) {
                                    MDSMap = new HashMap();
                                    MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                                    MDSMap.put("BRANCH_CODE", _branchCode);
                                    MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                    MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                                    MDSMap.put("PARTICULARS", objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " SGST");
                                    MDSMap.put("ACCT_HEAD", serTaxMap.get("KRISHIKALYAN_HEAD_ID"));
                                    MDSMap.put("TRANS_AMOUNT", krishikalyanCess/schemeCount);
                                    MDSMap.put("AUTHORIZEREMARKS", "SERVICE TAX");
                                    MDSMap.put("TRANS_MOD_TYPE", "MDS");
                                    MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                                    MDSMap.put("INSTRUMENT_NO2", "SGST");// Added by nithya on 31-10-2019 for KD-680
                                    cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                                    //serTaxAmt -= krishikalyanCess;
                                }
                                if (normalServiceTax > 0) {
                                    MDSMap = new HashMap();
                                    MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                                    MDSMap.put("BRANCH_CODE", _branchCode);
                                    MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                    MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                                    MDSMap.put("PARTICULARS", objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " SERVICE TAX");
                                    MDSMap.put("ACCT_HEAD", serTaxMap.get("TAX_HEAD_ID"));
                                    MDSMap.put("TRANS_AMOUNT", normalServiceTax/schemeCount);
                                    MDSMap.put("AUTHORIZEREMARKS", "SERVICE TAX");
                                    MDSMap.put("TRANS_MOD_TYPE", "MDS");
                                    MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                                    MDSMap.put("INSTRUMENT_NO2", "SERVICE_TAX");// Added by nithya on 31-10-2019 for KD-680
                                    cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                                }
                            }
                    }
                } else {
                    System.out.println("Else part executing....");
                    HashMap MDSMap = new HashMap();
//                    if (map.containsKey("serviceTaxDetails")) {
//                        HashMap serTaxMap = (HashMap) map.get("serviceTaxDetails");
//                        HashMap MDSMap = new HashMap();
//                        MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
//                        MDSMap.put("BRANCH_CODE", _branchCode);
//                        MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
//                        MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
//                        MDSMap.put("PARTICULARS", objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " SERVICE TAX");
//                        MDSMap.put("ACCT_HEAD", serTaxMap.get("TAX_HEAD_ID"));
//                        MDSMap.put("TRANS_AMOUNT", CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT")) / schemeCount);
//                        MDSMap.put("AUTHORIZEREMARKS", "SERVICE TAX");
//                        MDSMap.put("TRANS_MOD_TYPE", "MDS");
//                        MDSMap.put("GL_TRANS_ACT_NUM",objGDSApplicationTO.getChittalNo());
//                        MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
//                        cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
//                    }



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
                                    MDSMap.put("PARTICULARS", objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " CGST");
                                    MDSMap.put("ACCT_HEAD", serTaxMap.get("SWACHH_HEAD_ID"));
                                    MDSMap.put("TRANS_AMOUNT", swachhCess/schemeCount);
                                    MDSMap.put("AUTHORIZEREMARKS", "SERVICE TAX");
                                    MDSMap.put("TRANS_MOD_TYPE", "MDS");
                                    MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                                    MDSMap.put("INSTRUMENT_NO2", "CGST");// Added by nithya on 31-10-2019 for KD-680
                                    cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                                    //serTaxAmt -= swachhCess;
                                }
                                if (krishikalyanCess > 0) {
                                    MDSMap = new HashMap();
                                    MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                                    MDSMap.put("BRANCH_CODE", _branchCode);
                                    MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                    MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                                    MDSMap.put("PARTICULARS", objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " SGST");
                                    MDSMap.put("ACCT_HEAD", serTaxMap.get("KRISHIKALYAN_HEAD_ID"));
                                    MDSMap.put("TRANS_AMOUNT", krishikalyanCess/schemeCount);
                                    MDSMap.put("AUTHORIZEREMARKS", "SERVICE TAX");
                                    MDSMap.put("TRANS_MOD_TYPE", "MDS");
                                    MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                                    MDSMap.put("INSTRUMENT_NO2", "SGST");// Added by nithya on 31-10-2019 for KD-680
                                    cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                                    //serTaxAmt -= krishikalyanCess;
                                }
                                if (normalServiceTax > 0) {
                                    MDSMap = new HashMap();
                                    MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                                    MDSMap.put("BRANCH_CODE", _branchCode);
                                    MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                    MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                                    MDSMap.put("PARTICULARS", objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " SERVICE TAX");
                                    MDSMap.put("ACCT_HEAD", serTaxMap.get("TAX_HEAD_ID"));
                                    MDSMap.put("TRANS_AMOUNT", normalServiceTax/schemeCount);
                                    MDSMap.put("AUTHORIZEREMARKS", "SERVICE TAX");
                                    MDSMap.put("TRANS_MOD_TYPE", "MDS");
                                    MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                                    MDSMap.put("INSTRUMENT_NO2", "SERVICE_TAX");// Added by nithya on 31-10-2019 for KD-680
                                    cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                                }
                            }
                }

                if (prizedMoneyPaymentTO.getDefaulter_marked().equals("Y")) {
                    HashMap account_map = new HashMap();
                    account_map.put("CHITTAL_NO", prizedMoneyPaymentTO.getGdsNo());
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
                        defaulter_bonus_recoverd = prizedMoneyPaymentTO.getDefaulter_bonus_recoverd() / schemeCount;
                    }
                    if (prizedMoneyPaymentTO.getDefaulter_comm() > 0.0) {
                        defaulter_comm = prizedMoneyPaymentTO.getDefaulter_comm() / schemeCount;
                        HashMap MDSMap = new HashMap();
                        MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                        MDSMap.put("BRANCH_CODE", _branchCode);
                        MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                        MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                        MDSMap.put("PARTICULARS", objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " RECOVER BONUS");
                        MDSMap.put("ACCT_HEAD", Misclens);
                        MDSMap.put("TRANS_AMOUNT", defaulter_comm);
                        MDSMap.put("AUTHORIZEREMARKS", "RECOVERD_BONUS");
                        MDSMap.put("TRANS_MOD_TYPE", "MDS");
                        MDSMap.put("GL_TRANS_ACT_NUM",objGDSApplicationTO.getChittalNo());
                        MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                        cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                    }
                    if (prizedMoneyPaymentTO.getDefaulter_interst() > 0.0) {
                        defaulter_Inteset = prizedMoneyPaymentTO.getDefaulter_interst() / schemeCount;
                        HashMap MDSMap = new HashMap();
                        MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                        MDSMap.put("BRANCH_CODE", _branchCode);
                        MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                        MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                        MDSMap.put("PARTICULARS", objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " RECOVER BONUS");
                        MDSMap.put("ACCT_HEAD", interstAccountHead);
                        MDSMap.put("TRANS_AMOUNT", defaulter_Inteset);
                        MDSMap.put("AUTHORIZEREMARKS", "RECOVERD_BONUS");
                        MDSMap.put("TRANS_MOD_TYPE", "MDS");
                        MDSMap.put("GL_TRANS_ACT_NUM",objGDSApplicationTO.getChittalNo());
                        MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                        cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                    }
                    double paidamount = 0.0;
                    HashMap mdsMap = new HashMap();
                    mdsMap.put("CHITTAL_NO", prizedMoneyPaymentTO.getGdsNo());
                    mdsMap.put("SUB_NO", CommonUtil.convertObjToInt(prizedMoneyPaymentTO.getSubNo()));

                    List getpaidAmounts = sqlMap.executeQueryForList("getMdsPaidAmount", mdsMap);
                    if (getpaidAmounts != null && getpaidAmounts.size() > 0) {
                        mdsMap = new HashMap();
                        mdsMap = (HashMap) getpaidAmounts.get(0);
                        if (mdsMap != null && mdsMap.containsKey("TOTAL") && mdsMap.get("TOTAL") != null) {

                            paidamount = Double.parseDouble(mdsMap.get("TOTAL").toString()) / schemeCount;
                        } else {
                            paidamount = 0.0;
                        }
                    } else {
                        paidamount = 0.0;
                    }
                    double TotDet = CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPrizedAmount()).doubleValue() / schemeCount
                            + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue() / schemeCount
                            + CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getCommisionAmount()).doubleValue() / schemeCount;
                    double amountToReciept = TotDet - paidamount;
                    double bonusTogive = 0.0;
                    HashMap mdsbonusMap = new HashMap();
                    mdsbonusMap.put("CHITTAL_NO", prizedMoneyPaymentTO.getGdsNo());
                    mdsbonusMap.put("SUB_NO", CommonUtil.convertObjToInt(prizedMoneyPaymentTO.getSubNo()));
                    mdsbonusMap.put("SCHME_NAME", prizedMoneyPaymentTO.getGroupName());
                    List payableBons = sqlMap.executeQueryForList("getPayableBonus", mdsbonusMap);
                    if (payableBons != null && payableBons.size() > 0) {
                        HashMap mapDts = new HashMap();
                        mapDts = (HashMap) payableBons.get(0);
                        if (mapDts != null) {
                            bonusTogive = Double.parseDouble(mapDts.get("BONUS_PAYABLE").toString()) / schemeCount;

                        } else {
                            bonusTogive = 0.0;
                        }
                    } else {
                        bonusTogive = 0.0;
                    }
                    if (bonusTogive > 0.0) {
                        HashMap MDSMap = new HashMap();
                        MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                        MDSMap.put("BRANCH_CODE", _branchCode);
                        MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                        MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                        MDSMap.put("PARTICULARS", objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " AMOUNT TO RECIPT:DEFAULTER");
                        MDSMap.put("ACCT_HEAD", bonusAcountHead);
                        MDSMap.put("TRANS_AMOUNT", bonusTogive);
                        MDSMap.put("AUTHORIZEREMARKS", "RECOVERD_MDSAMOUNT");
                        MDSMap.put("TRANS_MOD_TYPE", "MDS");
                        MDSMap.put("GL_TRANS_ACT_NUM",objGDSApplicationTO.getChittalNo());
                        MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                        cashList.add(createCashTransactionTODefulter(MDSMap, generateSingleTransId));
                    }
                    double chittalPaid = paidamount;
                    paidamount = paidamount - (defaulter_Inteset + defaulter_bonus_recoverd + defaulter_comm);
                    if (paidamount > 0.0) {
                        HashMap MDSMap = new HashMap();
                        MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                        MDSMap.put("BRANCH_CODE", _branchCode);
                        MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                        MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                        MDSMap.put("PARTICULARS", objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " AMOUNT TO RECIPT:DEFAULTER");
                        MDSMap.put("ACCT_HEAD", SuspensetAccountHead);
                        MDSMap.put("TRANS_AMOUNT", paidamount);
                        MDSMap.put("AUTHORIZEREMARKS", "RECOVERD_MDSAMOUNT");
                        MDSMap.put("TRANS_MOD_TYPE", "MDS");
                        MDSMap.put("GL_TRANS_ACT_NUM",objGDSApplicationTO.getChittalNo());
                        MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                        cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                    }
                    if (amountToReciept > 0.0) {
                        HashMap MDSMap = new HashMap();
                        MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                        MDSMap.put("BRANCH_CODE", _branchCode);
                        MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                        MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                        MDSMap.put("PARTICULARS", objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " AMOUNT TO RECIPT:DEFAULTER");
                        MDSMap.put("ACCT_HEAD", RecieptAccountHead);
                        MDSMap.put("TRANS_AMOUNT", amountToReciept);
                        MDSMap.put("AUTHORIZEREMARKS", "RECOVERD_MDSAMOUNT");
                        MDSMap.put("TRANS_MOD_TYPE", "MDS");
                        MDSMap.put("GL_TRANS_ACT_NUM",objGDSApplicationTO.getChittalNo());
                        MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                        cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                    }
                    TotDet = TotDet + bonusTogive;
                    double NtMislns = TotDet - (amountToReciept + paidamount + defaulter_Inteset + defaulter_comm + prizedMoneyPaymentTO.getCommisionAmount() / schemeCount + prizedMoneyPaymentTO.getBonusAmount() / schemeCount);
                    if (NtMislns > 0.0) {
                        System.out.println("NtMislns" + NtMislns);
                        HashMap MDSMap = new HashMap();
                        MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                        MDSMap.put("BRANCH_CODE", _branchCode);
                        MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                        MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                        MDSMap.put("PARTICULARS", objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " AMOUNT TO RECIPT:DEFAULTER");
                        MDSMap.put("ACCT_HEAD", Misclens);
                        MDSMap.put("TRANS_AMOUNT", NtMislns);
                        MDSMap.put("AUTHORIZEREMARKS", "RECOVERD_MDSAMOUNT");
                        MDSMap.put("TRANS_MOD_TYPE", "MDS");
                        MDSMap.put("GL_TRANS_ACT_NUM",objGDSApplicationTO.getChittalNo());
                        MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                        cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                    } else if (NtMislns < 0.0) {
                        ExecptionDeftler = true;
                        throw new TTException("Cannot make Defaulter Payment !!!");
                    }
                }
                if (!(map.containsKey("TRASFER_BONUS_COMMISION") && map.get("TRASFER_BONUS_COMMISION").equals("Y"))) {
                    if (!(map.containsKey("AUCTION_TRANS") && map.get("AUCTION_TRANS").equals("Y"))) {
                        if (!isBonusTransfer.equalsIgnoreCase("N")) {
                            if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()).doubleValue() > 0) {
                                HashMap MDSMap = new HashMap();
                                MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                                MDSMap.put("BRANCH_CODE", _branchCode);
                                MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                                MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                                MDSMap.put("PARTICULARS", objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " BONUS");
                                if (map.containsKey("MDS_CLOSURE")) {
                                    MDSMap.put("ACCT_HEAD", applnMap.get("SUNDRY_RECEIPT_HEAD"));
                                } else {
                                    if (!(map.containsKey("PART_PAY") && map.get("PART_PAY").equals("Y"))) {
                                        MDSMap.put("ACCT_HEAD", applnMap.get("BONUS_PAYABLE_HEAD"));
                                    } else {
                                        MDSMap.put("ACCT_HEAD", applnMap.get("BONUS_RECEIVABLE_HEAD"));
                                    }
                                }
                                MDSMap.put("TRANS_AMOUNT", CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getBonusAmount()) / schemeCount);
                                MDSMap.put("AUTHORIZEREMARKS", "BONUS");
                                MDSMap.put("TRANS_MOD_TYPE", "MDS");
                                MDSMap.put("GL_TRANS_ACT_NUM", objGDSApplicationTO.getChittalNo());
                                MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                                cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                            }
                        }
                    }
                }    
                if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNoticeAmount()).doubleValue() > 0) {
                    HashMap MDSMap = new HashMap();
                    MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                    MDSMap.put("BRANCH_CODE", _branchCode);
                    MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                    MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                    MDSMap.put("PARTICULARS", objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " NOTICE");
                    MDSMap.put("ACCT_HEAD", applnMap.get("NOTICE_CHARGES_HEAD"));
                    MDSMap.put("TRANS_AMOUNT", CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNoticeAmount()) / schemeCount);
                    MDSMap.put("AUTHORIZEREMARKS", "NOTICE");
                    MDSMap.put("TRANS_MOD_TYPE", "MDS");
                    MDSMap.put("GL_TRANS_ACT_NUM",objGDSApplicationTO.getChittalNo());
                    MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                    cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                }

                if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getChargeAmount()).doubleValue() > 0) {
                    HashMap MDSMap = new HashMap();
                    MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                    MDSMap.put("BRANCH_CODE", _branchCode);
                    MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                    MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                    MDSMap.put("PARTICULARS", objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " CHARGE");
                    MDSMap.put("ACCT_HEAD", applnMap.get("CHARGE_PAYMENT_HEAD"));
                    MDSMap.put("TRANS_AMOUNT", CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getChargeAmount()) / schemeCount);
                    MDSMap.put("AUTHORIZEREMARKS", "CHARGE");
                    MDSMap.put("TRANS_MOD_TYPE", "MDS");
                    MDSMap.put("GL_TRANS_ACT_NUM",objGDSApplicationTO.getChittalNo());
                    MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                    cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                }
                if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getAribitrationAmount()).doubleValue() > 0) {
                    System.out.println("Arbitration Started");
                    HashMap whereMap = new HashMap();
                    whereMap.put("ACT_NUM", CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()) + "_" + CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getSubNo()));
                    List chargeList = (List) sqlMap.executeQueryForList("getMDSCaseChargeDetails", whereMap);
                    if (chargeList != null && chargeList.size() > 0) {
                        List applncashList = new ArrayList();
                        for (int k = 0; k < chargeList.size(); k++) {
                            double chargeAmount = 0.0;
                            String chargeType = "";
                            whereMap = (HashMap) chargeList.get(k);
                            chargeAmount = CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue() / schemeCount;
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
                                MDSMap.put("PARTICULARS", objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " " + chargeType);
                                MDSMap.put("ACCT_HEAD", applnMap.get(chargeType));
                                //MDSMap.put("ACCT_HEAD", applicationMap.get("CASE_EXPENSE_HEAD"));
                                MDSMap.put("TRANS_AMOUNT", String.valueOf(chargeAmount));
                                MDSMap.put("AUTHORIZEREMARKS", chargeType);
                                MDSMap.put("TRANS_MOD_TYPE", "MDS");
                                MDSMap.put("GL_TRANS_ACT_NUM",objGDSApplicationTO.getChittalNo());
                                MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                                cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                            }
                        }
                    }
                }
                if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getDiscountAmount()).doubleValue() > 0) {
                    HashMap MDSMap = new HashMap();
                    MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                    MDSMap.put("BRANCH_CODE", _branchCode);
                    MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                    MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                    MDSMap.put("PARTICULARS", objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " DISCOUNT");
                    MDSMap.put("ACCT_HEAD", applnMap.get("DISCOUNT_HEAD"));
                    MDSMap.put("TRANS_AMOUNT", CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getDiscountAmount()) / schemeCount);
                    MDSMap.put("AUTHORIZEREMARKS", "DISCOUNT");
                    MDSMap.put("TRANS_MOD_TYPE", "MDS");
                    MDSMap.put("GL_TRANS_ACT_NUM",objGDSApplicationTO.getChittalNo());
                    MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                    cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                }
                if (CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getDefaulters()).equals("Y")) {
                    if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPenalAmount()).doubleValue() > 0) {
                        HashMap MDSMap = new HashMap();
                        MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                        MDSMap.put("BRANCH_CODE", _branchCode);
                        MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                        MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                        MDSMap.put("PARTICULARS", objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " PENAL");
                        MDSMap.put("ACCT_HEAD", applnMap.get("PENAL_INTEREST_HEAD"));
                        MDSMap.put("TRANS_AMOUNT", CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getPenalAmount()) / schemeCount);
                        MDSMap.put("AUTHORIZEREMARKS", "PENAL");
                        MDSMap.put("TRANS_MOD_TYPE", "MDS");
                        MDSMap.put("GL_TRANS_ACT_NUM",objGDSApplicationTO.getChittalNo());
                        MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                        cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                    }
                    if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()).doubleValue() > 0) {
                        HashMap MDSMap = new HashMap();
                        MDSMap.put("SELECTED_BRANCH_ID", map.get("SELECTED_BRANCH_ID"));
                        MDSMap.put("BRANCH_CODE", _branchCode);
                        MDSMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                        MDSMap.put("USER_ID", prizedMoneyPaymentTO.getStatusBy());
                        MDSMap.put("PARTICULARS", objGDSApplicationTO.getChittalNo() + "_" + prizedMoneyPaymentTO.getSubNo() + " OVER_DUE");
                        MDSMap.put("ACCT_HEAD", applnMap.get("RECEIPT_HEAD"));
                        MDSMap.put("TRANS_AMOUNT", CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getOverdueAmount()) / schemeCount);
                        MDSMap.put("AUTHORIZEREMARKS", "OVER_DUE");
                        MDSMap.put("TRANS_MOD_TYPE", "MDS");
                        MDSMap.put("GL_TRANS_ACT_NUM",objGDSApplicationTO.getChittalNo());
                        MDSMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                        cashList.add(createCashTransactionTO(MDSMap, generateSingleTransId));
                    }
                }
            }
            if ((map.containsKey("TRASFER_BONUS_COMMISION") && map.get("TRASFER_BONUS_COMMISION").equals("Y"))) {
                transferBonusCommision(map, applnMap, prizedMoneyPaymentTO, generateSingleTransId, schemeCount,allApplnDetails);
            }
        }
        return cashList;
    }

    

   // End         
            
            
            
            
//    private ArrayList newApplicationList(HashMap applnMap,String generateSingleTransId) throws Exception {
//        System.out.println("applnMap :" + applnMap);
//        ArrayList cashList = new ArrayList();
//        System.out.println("objCashTransactionTO :" + prizedMoneyPaymentTO);      
//        CashTransactionTO objCashTO = new CashTransactionTO();
//        if (CommonUtil.convertObjToDouble(prizedMoneyPaymentTO.getNetAmount()).doubleValue() > 0) {
//            objCashTO.setTransId("");
//            objCashTO.setProdType("GL");            
//            if (applnMap.containsKey("MDS_CLOSURE")) {
//                objCashTO.setAcHdId(CommonUtil.convertObjToStr(applnMap.get("MDS_PAYABLE_HEAD")));
//            } else {
//                objCashTO.setAcHdId(CommonUtil.convertObjToStr(applnMap.get("PAYMENT_HEAD")));
//            }
//            objCashTO.setInpAmount(CommonUtil.convertObjToDouble(applnMap.get("TOTAL_AMOUNT")));
//            objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
//            objCashTO.setAmount(CommonUtil.convertObjToDouble(applnMap.get("TOTAL_AMOUNT")));
//            objCashTO.setTransType(CommonConstants.DEBIT);
//            objCashTO.setInitTransId(CommonUtil.convertObjToStr(applnMap.get("USER_ID")));
//            objCashTO.setBranchId(CommonUtil.convertObjToStr(applnMap.get("SELECTED_BRANCH_ID")));
//            objCashTO.setStatusBy(CommonUtil.convertObjToStr(applnMap.get("USER_ID")));
//            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(applnMap.get("LINK_BATCH_ID")));
//            objCashTO.setStatusDt(prizedMoneyPaymentTO.getStatusDt());           
//            List listData = ServerUtil.executeQuery("getCashierAuth", new HashMap());
//            if (listData != null && listData.size() > 0) {
//                HashMap map1 = (HashMap) listData.get(0);
//                if(map1.get("CASHIER_AUTH_ALLOWED") != null && map1.get("CASHIER_AUTH_ALLOWED").toString().equals("Y") && objCashTO.getTransType().equals(CommonConstants.CREDIT)){
//                    objCashTO.setAuthorizeStatus_2("");
//                }else
//                    objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
//            }else{
//                objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
//            }
//            objCashTO.setParticulars(prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo());
//            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
//            objCashTO.setInitChannType(CommonConstants.CASHIER);
//            objCashTO.setSingleTransId(generateSingleTransId);
//            objCashTO.setTransModType(CommonUtil.convertObjToStr(applnMap.get("TRANS_MOD_TYPE")));
//            objCashTO.setCommand(prizedMoneyPaymentTO.getCommand());
//            objCashTO.setScreenName("MDS Payment");
//            System.out.println("objCashTO 1st one:" + objCashTO);
//            cashList.add(objCashTO);
//        }
//        objCashTO = null;
//        //        }
//        return cashList;
//    }

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
            objCashTO.setParticulars(prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo());
            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            objCashTO.setCommand(prizedMoneyPaymentTO.getCommand());
            objCashTO.setScreenName("GDS Prized Money Payment");
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
            objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()));
            objCashTO.setTransType(CommonConstants.DEBIT);
            objCashTO.setInitTransId(CommonUtil.convertObjToStr(applnMap.get("USER_ID")));
            objCashTO.setBranchId(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
            objCashTO.setStatusBy(CommonUtil.convertObjToStr(applnMap.get("USER_ID")));
            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(applnMap.get("LINK_BATCH_ID")));
            objCashTO.setStatusDt(prizedMoneyPaymentTO.getStatusDt());
            objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            objCashTO.setParticulars(prizedMoneyPaymentTO.getGdsNo() + "_" + prizedMoneyPaymentTO.getSubNo());
            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            objCashTO.setCommand(prizedMoneyPaymentTO.getCommand());
            objCashTO.setScreenName("GDS Prized Money Payment");
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
                            cashMap.put("SCREEN_NAME", "GDS Prized Money Payment");
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
        List lstCash = (List) sqlMap.executeQueryForList("getGDSTransBatchIdForCashPayment", cashAuthMap);
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
        map.put("SCREEN_NAME", "GDS Prized Money Payment");
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
            if (map.containsKey("SERVICE_TAX_AUTH")) {
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


            sqlMap.executeUpdate("authorizeGDSPrizedMoneyPayment", AuthMap);
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
                    String actNo = CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getGdsNo()) + "_" + CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getSubNo());
                    chargesCollected(actNo);
                } 
                //Added by sreekrishnan for sms alerts..
                if (authorizeStatus.equals(CommonConstants.STATUS_AUTHORIZED)) {
                HashMap smsAlertMap = new HashMap();
                SmsConfigDAO smsDAO = new SmsConfigDAO(); 
                smsAlertMap.put("SCHEME_NAME", prizedMoneyPaymentTO.getGroupName());
                smsAlertMap.put("CHITTAL_NO", prizedMoneyPaymentTO.getGdsNo());
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
                            MdsTransMap.put("SCHEME_NAME", prizedMoneyPaymentTO.getGroupName());
                            MdsTransMap.put("CHITTAL_NO", prizedMoneyPaymentTO.getGdsNo());
                            MdsTransMap.put("INSTALLMENT", prizedMoneyPaymentTO.getPrizedAmount());
                            MdsTransMap.put("MDS_PRIZED_SMS", prizedMoneyPaymentTO.getPrizedAmount());
                            System.out.println("MdsTransMap %#%#%#^#^#^#^" + MdsTransMap);
                            smsDAO.MdsReceiptsSmsConfiguration(MdsTransMap);
                        //}
                    }
                    MdsSmsList = null;
                }
                }
            }

        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

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
            GDSPrizedMoneyPaymentDAO dao = new GDSPrizedMoneyPaymentDAO();

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
