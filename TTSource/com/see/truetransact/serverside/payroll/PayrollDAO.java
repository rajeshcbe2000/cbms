/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PayrollDAO.java
 *
 * Created on Thu Jan 20 17:19:05 IST 2005
 */
package com.see.truetransact.serverside.payroll;

import java.util.List;
import java.util.ArrayList;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.serverutil.ServerUtil;
import java.util.LinkedHashMap;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.interest.InterestTaskRunner;
import com.see.truetransact.serverside.mdsapplication.mdsreceiptentry.MDSReceiptEntryDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.cash.CashTransaction;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
//import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
//import com.see.truetransact.serverside.common.log.LogDAO;
//import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.transferobject.indend.IndendTO;
import com.see.truetransact.transferobject.mdsapplication.mdsreceiptentry.MDSReceiptEntryTO;
import com.see.truetransact.transferobject.payroll.PFTransferTO;
import java.util.HashMap;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Payroll DAO.
 *
 */
public class PayrollDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private IndendTO objTO;
    private TransactionDAO transactionDAO = null;
    private String userId = "";
    private LogDAO logDAO;
    private LogTO logTO;
    private Date currDt;
    HashMap returnMap;
    HashMap tansactionMap = new HashMap();
    HashMap authorizeMap = new HashMap();
    private Date currentDate;
    private LinkedHashMap transactionDetailsTO;
    private HashMap paramMap;
    private MDSReceiptEntryDAO mdsReceiptEntryDAO = null;
    private MDSReceiptEntryTO mdsReceiptEntryTO = null;
    private LinkedHashMap deletedTransactionDetailsTO;
    private LinkedHashMap allowedTransactionDetailsTO;
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    LinkedHashMap transactionDetailsMap = new LinkedHashMap();
    TransactionTO tto = null;
    private HashMap loanDataMap = new HashMap();
    PFTransferTO objPFTransTo = null;
    private String payMode = "";
    private String userID = "";
    HashMap execReturnMap;

    /**
     * Creates a new instance of PayrollDAO
     */
    public PayrollDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        currDt = ServerUtil.getCurrentDate(_branchCode);
        List list;
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        HashMap totalMap = new HashMap();
        String transId = CommonUtil.convertObjToStr(map.get("BATCH_ID"));
        int dot = transId.indexOf(",");
        if (dot > 0) {
            transId = transId.substring(0, dot);
        }
        System.out.println("###### transID : " + transId);
        HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.TRANS_ID, transId);
        whereMap.put(CommonConstants.BRANCH_ID, _branchCode);
        whereMap.put("TRANS_DT", currDt);
        totalMap.put(CommonConstants.MAP_WHERE, whereMap);
        list = transactionDAO.getData(totalMap);
        returnMap.put("TransactionTO", list);
        return returnMap;
    }

    private String getIRNo() throws Exception {
        //  final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "IRNO");
        // return "";
        HashMap map = generateID();
        //System.out.println("MAP IN DAOOOOOO=========="+map);
        return (String) map.get(CommonConstants.DATA);
        //  return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    public HashMap generateID() {
        HashMap hash = null, result = null;
        try {
            String mapName = "getCurrentID";
            HashMap where = new HashMap();
            where.put("ID_KEY", "IRNO"); //Here u have to pass BORROW_ID or something else
            List list = null;

            //sqlMap.startTransaction();
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

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void doGLTransactions(HashMap map, LogTO objLogTO) throws Exception, Exception {
        try {
            double transAmt = CommonUtil.convertObjToDouble(map.get("AMOUNT")).doubleValue();
            HashMap txMap;
            TransferTrans objTransferTrans = new TransferTrans();
            HashMap pfMap = new HashMap();
            _branchCode = "" + map.get("BRANCH_ID");
            objTransferTrans.setInitiatedBranch(_branchCode);
            txMap = new HashMap();
            ArrayList transferList = new ArrayList();
            TransactionTO objTransactionTO = null;
            objTransactionTO = (TransactionTO) map.get("TransactionTO");

            double debitAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
            if (objTransactionTO.getTransType().equals("TRANSFER")) {
                String transType = objTransactionTO.getTransType();
                if (map.get("EMPLOYEE_NAME") != null) {
                    txMap.put(TransferTrans.PARTICULARS, map.get("EMPLOYEE_NAME"));
                } else {
                    txMap.put(TransferTrans.PARTICULARS, "");
                }
                txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.DR_INSTRUMENT_1, "PAYROLL");
                txMap.put("GL_TRANS_ACT_NUM", map.get("AC_NO"));
                if (map.get("PAY_DESCRIPTION") != null) {
                    txMap.put(TransferTrans.NARRATION, map.get("PAY_DESCRIPTION"));
                } else {
                    txMap.put(TransferTrans.NARRATION, "PAYROLLGL Transactions");
                }
                if (objTransactionTO.getProductType().equals("GL")) {
                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                }
                txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                transferList.add(objTransferTrans.getDebitTransferTO(txMap, debitAmt));
                if (debitAmt > 0.0) {
                    txMap.put(TransferTrans.CR_AC_HD, (String) map.get("CREDIT_HD_ID"));
                    txMap.put("AUTHORIZEREMARKS", "GL PAYMENT :" + (String) map.get("CREDIT_HD_ID"));
                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, debitAmt));
                }
                System.out.println("@kkkkkiiii>>>>doGLTransactionsdoGLTransactionsdoGLTransactionsdoGLTransactionsdoGLTransactionsdoGLTransactionsdoGLTransactionsdoGLTransactionsdoGLTransactions" + debitAmt);
                objTransferTrans.doDebitCredit(transferList, _branchCode, true);
               
                //added by anju anand 26/07/2014
                if (map != null && map.containsKey("PAY_MODULE_TYPE") && map.get("PAY_MODULE_TYPE") != null && map.get("PAY_MODULE_TYPE").equals("PF")) {
                    pfMap.put("PF_NO", map.get(("PF_ACT_NO")));
                    pfMap.put("TRAN_DT", currDt);
                    pfMap.put("TRANS_ID", "");
                    pfMap.put("BATCH_ID", "");
                    pfMap.put("TRANS_MODE", transType);
                    pfMap.put("AMOUNT", debitAmt);
                    pfMap.put("PROD_TYPE", "C");
                    pfMap.put("CREATED_BY", objLogTO.getUserId());
                    pfMap.put("CREATED_DATE", currDt);
                    pfMap.put("AUTHORIZED_BY", "");
                    pfMap.put("AUTHORIZE_STATUS", "AUTHORIZED");
                    pfMap.put("BRANCHID", _branchCode);
                    pfMap.put("STATUS", "posted");
                    pfMap.put("REMARK", "");
                    pfMap.put("PF_TRANS_TYPE", map.get("PF_TRANS_TYPE"));
                    pfMap.put("TRANS_TYPE", "CREDIT");
                    sqlMap.executeUpdate("InsertPfTransTo", pfMap);
                    sqlMap.executeUpdate("updatepfmasterbalance", pfMap);
                }
            }
            transAmt = 0.0;
            objTransferTrans = null;
            transferList = null;
            txMap = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw new TransRollbackException(e);
        }
    }

    private void doMDSPayment(HashMap dataMap) throws Exception {
        currentDate = ServerUtil.getCurrentDateProperFormat(_branchCode);
        HashMap tempMap = null;
        //mds start
        HashMap map = new HashMap();
        TransactionTO transactionTO = new TransactionTO();
        transactionDetailsTO = new LinkedHashMap();
        LinkedHashMap transMap = new LinkedHashMap();
        transactionTO.setTransType("TRANSFER");
        transactionTO.setTransAmt(CommonUtil.convertObjToDouble(dataMap.get("TOTAL_DEMAND")));
        transactionTO.setProductType("GL");
        transactionTO.setDebitAcctNo(CommonUtil.convertObjToStr(dataMap.get("DEBIT_ACC_NO")));
        // transactionTO.setApplName(CommonUtil.convertObjToStr(paramMap.get(InterestTaskRunner.CREDIT_INT)));
        transactionTO.setInstType("VOUCHER");

        HashMap ChittalMap = new HashMap();
        map.put(CommonConstants.USER_ID, "admin");
        // map.put(CommonConstants.USER_ID,CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
        map.put("BRANCH_CODE", _branchCode);

        transMap.put("1", transactionTO);
        transactionDetailsTO.put("NOT_DELETED_TRANS_TOs", transMap);

        map.put("TransactionTO", transactionDetailsTO);
        System.out.println("map is$$$" + map);
        ChittalMap.put("ACT_NUM", dataMap.get("CHITTAL_NO"));
        ChittalMap.put("ACT_NUM", dataMap.get("CHITTAL_NO"));

        ChittalMap.put("PRINCIPAL", dataMap.get("PRINCIPAL"));
        ChittalMap.put("TOTAL_DEMAND", dataMap.get("TOTAL_DEMAND"));
        ChittalMap.put("INT_CALC_UPTO_DT", currentDate);

        ChittalMap.put("PENAL_AMT", dataMap.get("INTEREST"));//PENAL
        ChittalMap.put("BONUS_AMT", dataMap.get("BONUS"));
        ChittalMap.put("DISCOUNT_AMT", dataMap.get("DISCOUNT"));
        ChittalMap.put("INTEREST", dataMap.get("INTEREST"));
        ChittalMap.put("CHARGES", dataMap.get("CHARGES"));
        //ChittalMap.put("ARBITRATION_AMT",dataMap.get("ARBITRATION_AMT")) ;
        //ChittalMap.put("NOTICE_AMT",tempMap.get("NOTICE_AMT")) ;
        System.out.println("###ChittalMap" + ChittalMap);
//                                                         if(postageAmt>0){
//                                                             
//                                                             ChittalMap.put("POSTAGE_AMT_FOR_INT",CommonUtil.convertObjToStr(new Double(postageAmt)));
//                                                             ChittalMap.put("POSTAGE_ACHD", postageAcHd);
//                                                         }
//                                                        if(postageRenewAmt>0){
//                                                            ChittalMap.put("POSTAGE_ACHD", postageAcHd);
//                                                            ChittalMap.put("RENEW_POSTAGE_AMT",CommonUtil.convertObjToStr(new Double(postageRenewAmt)));
//                                                        }
        transactionPartMDS(ChittalMap, map);

        //ending

//        
//         //transaction part starting
//          try{
//          //  System.out.println("############################### chittalMap : "+chittalMap);
//              //transactionto
//              currDt=ServerUtil.getCurrentDate("0001");
//              currentDate=ServerUtil.getCurrentDate("0001");
//              System.out.println("rishuuuuuuuuuuu"+ currDt);
//                          String linkBatchId = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));
//                            TransferTrans transferTrans = new TransferTrans();
//                            transferTrans.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
//                            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
//                            transactionDAO.setInitiatedBranch(_branchCode);
//                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
//                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
//                          
//                            HashMap tansactionMap = new HashMap();
//                            TxTransferTO transferTo = new TxTransferTO();
//                            ArrayList TxTransferTO = new ArrayList();
//                            double paymentAmt = 0.0;
//                            double interestAmt = 0.0;
//                            double penalAmt = 0.0;
//                            String  mdsAccNo ="";
//                            //dataMap = (HashMap)shgList.get(i);
//                            paymentAmt = CommonUtil.convertObjToDouble(dataMap.get("TOTAL_DEMAND")).doubleValue();
//                            interestAmt = CommonUtil.convertObjToDouble(dataMap.get("INT_DUE")).doubleValue();
//                            penalAmt = CommonUtil.convertObjToDouble(dataMap.get("PENAL")).doubleValue();
//                            mdsAccNo = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));
//                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                            if(paymentAmt>0){//Debit Insert Start
//                                HashMap txMap = new HashMap();
//                                System.out.println("Transfer Started debit : ");
//                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                                //System.out.println("$#$$$#$#$# debitMap " +debitMap);
////                                if(!CommonUtil.convertObjToStr(dataMap.get("PRODUCT_TYPE")).equals("") && CommonUtil.convertObjToStr(dataMap.get("PRODUCT_TYPE")).equals("GL") && CommonUtil.convertObjToStr(dataMap.get("PRODUCT_TYPE")).equals("")){
//                                    //System.out.println("$#$$$#$#$# Prod Type GL " +transactionTO.getDebitAcctNo());
//                                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(dataMap.get("DEBIT_ACC_NO")));
//                                    txMap.put(TransferTrans.DR_PROD_TYPE,TransactionFactory.GL);
//                                    txMap.put(TransferTrans.PARTICULARS, linkBatchId + "-" + ":PAYMENT");
////                                }else if(!CommonUtil.convertObjToStr(dataMap.get("PRODUCT_TYPE")).equals("")){
////                                    System.out.println("$#$$$#$#$# Prod Type Not GL " +CommonUtil.convertObjToStr(dataMap.get("DEBIT_ACC_NO")));
////                                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(dataMap.get("AC_HEAD")));
////                                    txMap.put(TransferTrans.DR_ACT_NUM,CommonUtil.convertObjToStr(dataMap.get("DEBIT_ACC_NO")));
////                                    txMap.put(TransferTrans.DR_PROD_ID,CommonUtil.convertObjToStr(dataMap.get("PROD_ID")));
////                                    txMap.put(TransferTrans.DR_PROD_TYPE,CommonUtil.convertObjToStr(dataMap.get("PRODUCT_TYPE")));
////                                    txMap.put(TransferTrans.PARTICULARS, linkBatchId + "-" + ":PAYMENT");
////                                }
//                                txMap.put(TransferTrans.DR_BRANCH,CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
//                                txMap.put(TransferTrans.DR_INSTRUMENT_1,"PAYROLL");
//                           
//                                 if(dataMap.get("PAY_DESCRI")!=null)
//                                    {
//                                       txMap.put(TransferTrans.NARRATION,dataMap.get("PAY_DESCRI") ); 
//                                    }
//                                    else
//                                    {
//                                        
//                                  txMap.put(TransferTrans.NARRATION, "PAYROLLMDS Transactions");
//                                    }
//                                
//                                 
//                                  
//                                  
//                                  
//                                  
//                                System.out.println("txMap Debit : " + txMap+"paymentAmt :" + paymentAmt);
//                                 System.out.println("doMDSPayment"+paymentAmt);
//                                transferTo =  transactionDAO.addTransferDebitLocal(txMap, paymentAmt);
//                                transferTo.setTransId("-");
//                                transferTo.setBatchId("-");
//                                transferTo.setTransDt(currDt);
//                                transferTo.setBranchId(CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
//                                transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
//                                transferTo.setStatusBy(CommonConstants.TTSYSTEM);
//                                transferTo.setInitTransId(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(dataMap.get("USER_ID"))));
//                                transferTo.setLinkBatchId(mdsAccNo);
//                                transferTo.setInstrumentNo1("PAYROLL");
//                                TxTransferTO.add(transferTo);
//                            } 
//                            System.out.println("rish.............reached herer***********************************");
//      HashMap applicationMap = new HashMap();                // Crdit Acc INSERT Start
//                            applicationMap.put("PROD_ID",dataMap.get("PROD_ID"));
//                            List lst = sqlMap.executeQueryForList("getAccountHeadProdMDS",applicationMap);    // Acc Head
//                            if(lst!=null && lst.size()>0){
//                                applicationMap = (HashMap)lst.get(0);
//                            }
//                        HashMap MDSCharge = new HashMap();
//                           System.out.println("appplication map.........."+applicationMap);
//                           if(interestAmt>0){
//                                MDSCharge.put("CURR_MONTH_INT",String.valueOf(interestAmt));
//                            }
//                            if(penalAmt>0){
//                                MDSCharge.put("PENAL_INT",String.valueOf(penalAmt));
//                            }
//                            HashMap txMap = new HashMap();
//                            if(paymentAmt>0.0){
//                                transferTo = new TxTransferTO();
//                                txMap = new HashMap();
//                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                                txMap.put(TransferTrans.CR_ACT_NUM,  mdsAccNo);
//                                txMap.put(TransferTrans.CR_PROD_ID, dataMap.get("PROD_ID"));
//                                txMap.put(TransferTrans.CR_AC_HD, (String)applicationMap.get("AC_HEAD"));
//                               txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//                                txMap.put(TransferTrans.CR_BRANCH,CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
//                                txMap.put(TransferTrans.PARTICULARS, mdsAccNo+"-" +":PAYMENT");
//                                  // txMap.put(TransferTrans.DR_INSTRUMENT_1,"PAYROLL");
//                                   txMap.put(TransferTrans.DR_INSTRUMENT_1,"PAYROLL");
//                                    txMap.put(TransferTrans.NARRATION, "PAYROLMDS Transactions");
//                                System.out.println("txMap  : " + txMap+"paymentAmt :"+paymentAmt);
//                                // System.out.println("@kkkkkiiii>>>>doLoanPaymentdoLoanPayment"+paymentAmt);
//                                transferTo =  transactionDAO.addTransferCreditLocal(txMap, paymentAmt) ;
//                                transferTo.setTransId("-");
//                                transferTo.setBatchId("-");
//                                transferTo.setTransDt(currDt);
//                                transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
//                                transferTo.setStatusBy(CommonConstants.TTSYSTEM);
//                                transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
//                                transferTo.setLinkBatchId(mdsAccNo);
//                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsAccNo));
//                                transferTo.setInstrumentNo1("PAYROLL");
//                                //transactionTO.setChequeNo("SERVICE_TAX");
//                                TxTransferTO.add(transferTo);
//                            }
//              //end
//            HashMap MDSmap=new HashMap();
//            HashMap bonusMap=new HashMap();
//            HashMap whereMap=new HashMap();
//            HashMap chittalDetailMap=new HashMap();
//            HashMap installmentMap = new HashMap();
//            HashMap paramMap=new HashMap();
//       MDSReceiptEntryDAO mdsReceiptEntryDAO;
//              MDSReceiptEntryTO mdsReceiptEntryTO;
//            String actNo = "";
//            String subNo = "";
//            String chittalNo = "";
//            long count=0;
//            int curInsNo=0;
//            int noOfInsPaid=0;
//            long pendingInst = 0;
//            long noOfInstPay = 0;
//            double totBonusAmt = 0;
//            double totPenalAmt = 0;
//            double totNoticeAmt = 0;
//            double totDiscountAmt = 0;
//            double totArbitrationAmt = 0;
//            actNo = CommonUtil.convertObjToStr(dataMap.get("CHITTAL_NO"));
//            if (actNo.indexOf("_")!=-1) {
//                chittalNo = actNo.substring(0,actNo.indexOf("_"));
//                subNo = actNo.substring(actNo.indexOf("_")+1, actNo.length());
//            }
//            whereMap.put("CHITTAL_NO",chittalNo);
//            whereMap.put("SUB_NO",subNo);
//            List chittalList = (List) sqlMap.executeQueryForList("getMDSChittalDetails", whereMap);
//            if(chittalList !=null && chittalList.size()>0){
//                chittalDetailMap = (HashMap) chittalList.get(0);
//                //
//                whereMap.put("INT_CALC_UPTO_DT",currDt);
//                //                List bonusList = sqlMap.executeQueryForList("getRecoveryListInstallmentMapDetails", whereMap);
//                //                if (bonusList != null && bonusList.size() > 0) {
//                //                    for(int i=0; i<bonusList.size();i++){
//                HashMap instMap = new HashMap();
//                //                        bonusMap = (HashMap) bonusList.get(i);
//                totBonusAmt+=CommonUtil.convertObjToDouble(dataMap.get("BONUS")).doubleValue();
//                totPenalAmt+=CommonUtil.convertObjToDouble(dataMap.get("TOTAL_DEMAND")).doubleValue();
//                totDiscountAmt+=CommonUtil.convertObjToDouble(dataMap.get("DISCOUNT")).doubleValue();
//                //                        if(i==0){
//             //   totNoticeAmt = CommonUtil.convertObjToDouble(dataMap.get("DISCOUNT")).doubleValue();
//              //  totNoticeAmt = CommonUtil.convertObjToDouble(dataMap.get("DISCOUNT")).doubleValue();
//              //  totArbitrationAmt = CommonUtil.convertObjToDouble(dataMap.get("DISCOUNT")).doubleValue();
//                //                        }
//                instMap.put("BONUS",dataMap.get("DISCOUNT"));
//                instMap.put("DISCOUNT",dataMap.get("DISCOUNT"));
//                instMap.put("PENAL",dataMap.get("DISCOUNT"));
//                instMap.put("INST_AMT",dataMap.get("DISCOUNT"));
//                
//                //                    }
//                //                }
//                List insList = sqlMap.executeQueryForList("getNoOfInstalmentsPaid", whereMap);
//                if (insList != null && insList.size() > 0) {
//                    whereMap = (HashMap) insList.get(0);
//                    noOfInsPaid = CommonUtil.convertObjToInt(whereMap.get("NO_OF_INST"));
//                    count = CommonUtil.convertObjToLong(whereMap.get("NO_OF_INST"));
//                }
//                HashMap insDateMap = new HashMap();
//                insDateMap.put("DIVISION_NO", CommonUtil.convertObjToStr(chittalDetailMap.get("DIVISION_NO")));
//                insDateMap.put("SCHEME_NAME", chittalDetailMap.get("SCHEME_NAME"));
//                insDateMap.put("CURR_DATE",currDt);
//                insDateMap.put("ADD_MONTHS",  "-1");
//                List insDateLst = sqlMap.executeQueryForList("getMDSCurrentInsDate", insDateMap);
//                if (insDateLst != null && insDateLst.size() > 0) {
//                    insDateMap = (HashMap) insDateLst.get(0);
//                    curInsNo = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO"));
//                    pendingInst = curInsNo - noOfInsPaid;
//                    if (pendingInst < 0) {
//                        pendingInst = 0;
//                    }
//                    if(pendingInst>0){
//                        noOfInstPay = pendingInst;
//                    }
//                    
//                    System.out.println("######## NoOfInstToPay *** : "+noOfInstPay);
//                }
//                installmentMap.put(CommonUtil.convertObjToStr(insDateMap.get("INSTALLMENT_NO")),instMap);
//                //Narration
//                int insDay = 0;
//                Date paidUpToDate = null;
//                HashMap instDateMap = new HashMap();
//                instDateMap.put("SCHEME_NAME",chittalDetailMap.get("SCHEME_NAME"));
//                instDateMap.put("DIVISION_NO", CommonUtil.convertObjToStr(chittalDetailMap.get("DIVISION_NO")));
//                instDateMap.put("INSTALLMENT_NO",CommonUtil.convertObjToStr(String.valueOf(count)));
//                List insLst = sqlMap.executeQueryForList("getSelectInstUptoPaid", instDateMap);
//                if(insLst!=null && insLst.size()>0){
//                    instDateMap = (HashMap)insLst.get(0);
//                    paidUpToDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(instDateMap.get("NEXT_INSTALLMENT_DATE")));
//                }else{
//                    Date startedDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalDetailMap.get("SCHEME_START_DT")));
//                    insDay = CommonUtil.convertObjToInt(chittalDetailMap.get("INSTALLMENT_DAY"));
//                    startedDate.setDate(insDay);
//                    int stMonth = startedDate.getMonth();
//                    startedDate.setMonth(stMonth+(int)count-1);
//                    paidUpToDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(startedDate));
//                }
//                String narration = "";
//                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM/yyyy");
//                int noInstPay = CommonUtil.convertObjToInt(String.valueOf(noOfInstPay));
//                if (noInstPay == 1) {
//                    narration = "Inst#"+(noOfInsPaid+1);
//                    Date dt = DateUtil.addDays(paidUpToDate, 30);
//                    narration+=" "+sdf.format(dt);
//                } else if (noInstPay > 1) {
//                    narration = "Inst#"+(noOfInsPaid+1);
//                    narration+="-"+(noOfInsPaid+noInstPay);
//                    Date dt = DateUtil.addDays(paidUpToDate, 30);
//                    narration+=" "+sdf.format(dt);
//                    dt = DateUtil.addDays(paidUpToDate, 30*noInstPay);
//                    narration+=" To "+sdf.format(dt);
//                }
//                System.out.println("#$#$# narration :"+narration);
//                //SET RECEIPT_ENTRY_TO
//                mdsReceiptEntryTO = new MDSReceiptEntryTO();
//                mdsReceiptEntryTO.setCommand(CommonConstants.TOSTATUS_INSERT);
//                mdsReceiptEntryTO.setSchemeName(CommonUtil.convertObjToStr(chittalDetailMap.get("SCHEME_NAME")));
//                mdsReceiptEntryTO.setChittalNo(chittalNo);
//                mdsReceiptEntryTO.setSubNo(CommonUtil.convertObjToDouble(subNo));
//                mdsReceiptEntryTO.setMemberName(CommonUtil.convertObjToStr(chittalDetailMap.get("MEMBER_NAME")));
//                mdsReceiptEntryTO.setDivisionNo(CommonUtil.convertObjToDouble(chittalDetailMap.get("DIVISION_NO")));
//                mdsReceiptEntryTO.setChitStartDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalDetailMap.get("CHIT_START_DT"))));
//                mdsReceiptEntryTO.setChitEndDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalDetailMap.get("CHIT_END_DT"))));
//                mdsReceiptEntryTO.setPaidDate(currentDate);
//                mdsReceiptEntryTO.setNoOfInst(CommonUtil.convertObjToDouble(chittalDetailMap.get("NO_OF_INSTALLMENTS")));
//                mdsReceiptEntryTO.setCurrInst(CommonUtil.convertObjToDouble(String.valueOf(curInsNo)));
//                mdsReceiptEntryTO.setPendingInst(CommonUtil.convertObjToDouble(String.valueOf(pendingInst)));
//                mdsReceiptEntryTO.setInstAmt(CommonUtil.convertObjToDouble(chittalDetailMap.get("INST_AMT")));
//                mdsReceiptEntryTO.setNoOfInstPay(CommonUtil.convertObjToDouble(String.valueOf(noOfInstPay)));
//                mdsReceiptEntryTO.setInstAmtPayable(CommonUtil.convertObjToDouble(dataMap.get("PRINCIPAL")));
//                mdsReceiptEntryTO.setPaidInst(CommonUtil.convertObjToDouble(String.valueOf(noOfInsPaid)));
//                mdsReceiptEntryTO.setBonusAmtPayable(CommonUtil.convertObjToDouble(String.valueOf(totBonusAmt)));
//                mdsReceiptEntryTO.setDiscountAmt(CommonUtil.convertObjToDouble(String.valueOf(totDiscountAmt)));
//                mdsReceiptEntryTO.setPenalAmtPayable(CommonUtil.convertObjToDouble(String.valueOf(totPenalAmt)));
//                mdsReceiptEntryTO.setNoticeAmt(CommonUtil.convertObjToDouble(String.valueOf(totNoticeAmt)));
//                mdsReceiptEntryTO.setArbitrationAmt(CommonUtil.convertObjToDouble(String.valueOf(totArbitrationAmt)));
//                mdsReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(dataMap.get("TOTAL_DEMAND")));
//                mdsReceiptEntryTO.setNarration(narration);
//                mdsReceiptEntryTO.setBankPay("N");
//                mdsReceiptEntryTO.setStatusBy("admin");
//                mdsReceiptEntryTO.setStatus(CommonConstants.STATUS_CREATED);
//                mdsReceiptEntryTO.setStatusDt(currentDate);
//                mdsReceiptEntryTO.setBranchCode(CommonUtil.convertObjToStr(chittalDetailMap.get("BRANCH_CODE")));
//                mdsReceiptEntryTO.setInitiatedBranch("0001");
//                System.out.println("############################### mdsReceiptEntryTO : "+mdsReceiptEntryTO);
//                System.out.println("############################### installmentMap : "+installmentMap);
//                
//                MDSmap.put("INSTALLMENT_MAP",installmentMap);
//                MDSmap.put("mdsReceiptEntryTO",mdsReceiptEntryTO);
//                List closureList = (List) sqlMap.executeQueryForList("checkSchemeClosureDetails", chittalDetailMap);
//                if(closureList!=null && closureList.size()>0){
//                    MDSmap.put("MDS_CLOSURE","MDS_CLOSURE");
//                }else{
//                    MDSmap.remove("MDS_CLOSURE");
//                }
//                MDSmap.put("COMMAND",CommonConstants.TOSTATUS_INSERT);
//                MDSmap.put("BRANCH_CODE","0001");
//                MDSmap.put("TransactionTO",TxTransferTO);
//                MDSmap.put("INT_CALC_UPTO_DT",currentDate);
//                MDSmap.put("FROM_RECOVERY_TALLY","FROM_RECOVERY_TALLY");
//               // MDSmap.put("POSTAGE_AMT_FOR_INT",chittalMap.get("POSTAGE_AMT_FOR_INT"));
//               // MDSmap.put("RENEW_POSTAGE_AMT",chittalMap.get("RENEW_POSTAGE_AMT"));
//                //MDSmap.put("POSTAGE_ACHD",chittalMap.get("POSTAGE_ACHD"));
//                System.out.println("########### MDSmap : "+MDSmap);
//                mdsReceiptEntryDAO = new MDSReceiptEntryDAO();
//                HashMap transMap =new HashMap();
//                transMap = mdsReceiptEntryDAO.execute(MDSmap,false);                    // INSERT_TRANSACTION
//                System.out.println("################# transMap : "+transMap);
//                
//                //AUTHORIZE_START
//                HashMap authorizeMap = new HashMap();
//                authorizeMap.put("NET_TRANS_ID",transMap.get("BATCH_ID"));
//                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
//                authorizeMap.put("USER_ID", "admin");
//                authorizeMap.put("BRANCH_CODE","0001");
//                mdsReceiptEntryTO.setCommand("");
//                MDSmap.put("AUTHORIZEMAP",authorizeMap);
//                MDSmap.put("mdsReceiptEntryTO",mdsReceiptEntryTO);
//                MDSmap.put("SCHEME_NAME",CommonUtil.convertObjToStr(chittalDetailMap.get("SCHEME_NAME")));
//                MDSmap.put("USER_ID","admin");
//                System.out.println("########### MDSmap : "+MDSmap);
//                mdsReceiptEntryDAO = new MDSReceiptEntryDAO();
//                HashMap transDetMap=mdsReceiptEntryDAO.execute(MDSmap,false);                                     // AUTHORIZE_TRANSACTION
//                System.out.println("########### transDetMap : "+transDetMap);
//                if(transMap!= null && transMap.size() > 0){
//                    paramMap.put(paramMap.get("ACT_NUM"), transMap.get("BATCH_ID"));
//                }
//            }
//        }catch (Exception e) {
//            sqlMap.rollbackTransaction();
//            e.printStackTrace();
//            throw e;
//        }
//         


        //end transaction 
//         String chittalNo="";
//     MDSReceiptEntryTO mdsReceiptEntryTO = new MDSReceiptEntryTO();
//     String chittal_No=CommonUtil.convertObjToStr(dataMap.get("CHITTAL_NO"));
//      if (chittal_No.indexOf("_")!=-1) {
//            chittalNo = chittal_No.substring(0,chittal_No.indexOf("_"));
//          
//        }
////     
//    mdsReceiptEntryTO.setSchemeName(CommonUtil.convertObjToStr(dataMap.get("SCHEME_NAME")));
//       mdsReceiptEntryTO.setDivisionNo(CommonUtil.convertObjToDouble(dataMap.get("DIVISION_NO")));
//        mdsReceiptEntryTO.setChittalNo(CommonUtil.convertObjToStr(chittalNo));
//        mdsReceiptEntryTO.setChitStartDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dataMap.get("CHIT_START_DT"))));
//        //mdsReceiptEntryTO.setChitEndDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(datamap.get("SCHEME_NAME"))));
//        mdsReceiptEntryTO.setNoOfInst(CommonUtil.convertObjToDouble(dataMap.get("NO_OF_INSTALLMENTS")));
//        mdsReceiptEntryTO.setSubNo(CommonUtil.convertObjToDouble(dataMap.get("SUB_NO")));
//        mdsReceiptEntryTO.setCurrInst(CommonUtil.convertObjToDouble(dataMap.get("CURR_INST")));
//        mdsReceiptEntryTO.setInstAmt(CommonUtil.convertObjToDouble(dataMap.get("INSTMT_AMT")));
//        mdsReceiptEntryTO.setPendingInst(CommonUtil.convertObjToDouble(dataMap.get("PENDING_INST")));
//      //  mdsReceiptEntryTO.setTotalInstDue(CommonUtil.convertObjToDouble(datamap.get("SCHEME_NAME")));
//        mdsReceiptEntryTO.setBonusAmtAvail(CommonUtil.convertObjToDouble(dataMap.get("BONUS")));
//        //mdsReceiptEntryTO.setMemberName(CommonUtil.convertObjToStr(datamap.get("SCHEME_NAME")));
//    
//     mdsReceiptEntryTO.setPrizedMember(CommonUtil.convertObjToStr(dataMap.get("PRIZED_MEMBER")));
//       mdsReceiptEntryTO.setNoticeAmt(CommonUtil.convertObjToDouble(dataMap.get("PRIZED_MEMBER")));
//        mdsReceiptEntryTO.setArbitrationAmt(CommonUtil.convertObjToDouble(dataMap.get("PRIZED_MEMBER")));
//        mdsReceiptEntryTO.setNoOfInstPay(CommonUtil.convertObjToDouble(dataMap.get("NO_OF_INST_PAY")));
//        mdsReceiptEntryTO.setInstAmtPayable(CommonUtil.convertObjToDouble(dataMap.get("PENDING_DUE_AMT")));
//        mdsReceiptEntryTO.setPenalAmtPayable(CommonUtil.convertObjToDouble(dataMap.get("INTEREST")));
//        mdsReceiptEntryTO.setBonusAmtPayable(CommonUtil.convertObjToDouble(dataMap.get("BONUS")));
//        mdsReceiptEntryTO.setDiscountAmt(CommonUtil.convertObjToDouble(dataMap.get("DISCOUNT")));
//        mdsReceiptEntryTO.setMdsInterset(CommonUtil.convertObjToDouble(dataMap.get("INTEREST")));
//        mdsReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(dataMap.get("TOTAL_DEMAND")));
//        mdsReceiptEntryTO.setPaidDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(currDt)));
//        mdsReceiptEntryTO.setPaidInst(CommonUtil.convertObjToDouble(dataMap.get("NO_OF_INST_PAY")));
//        mdsReceiptEntryTO.setPaidAmt(CommonUtil.convertObjToDouble(dataMap.get("TOTAL_DEMAND")));
//
//
//        //        mdsReceiptEntryTO.setStatus(ClientConstants.)
//        //        mdsReceiptEntryTO.setStatusDt
//     
//        mdsReceiptEntryTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
//        
        //ended

//            String linkBatchId = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));
//                            TransferTrans transferTrans = new TransferTrans();
//                            transferTrans.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
//                            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
//                            transactionDAO.setInitiatedBranch(_branchCode);
//                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
//                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
//                          
//                            HashMap tansactionMap = new HashMap();
//                            TxTransferTO transferTo = new TxTransferTO();
//                            ArrayList TxTransferTO = new ArrayList();
//                            double paymentAmt = 0.0;
//                            double interestAmt = 0.0;
//                            double penalAmt = 0.0;
//                            String  mdsAccNo ="";
//                            //dataMap = (HashMap)shgList.get(i);
//                            paymentAmt = CommonUtil.convertObjToDouble(dataMap.get("PRINCIPAL")).doubleValue();
//                            interestAmt = CommonUtil.convertObjToDouble(dataMap.get("INT_DUE")).doubleValue();
//                            penalAmt = CommonUtil.convertObjToDouble(dataMap.get("PENAL")).doubleValue();
//                            mdsAccNo = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));
//                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                            if(paymentAmt>0){//Debit Insert Start
//                                HashMap txMap = new HashMap();
//                                System.out.println("Transfer Started debit : ");
//                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                                //System.out.println("$#$$$#$#$# debitMap " +debitMap);
////                                if(!CommonUtil.convertObjToStr(dataMap.get("PRODUCT_TYPE")).equals("") && CommonUtil.convertObjToStr(dataMap.get("PRODUCT_TYPE")).equals("GL") && CommonUtil.convertObjToStr(dataMap.get("PRODUCT_TYPE")).equals("")){
//                                    //System.out.println("$#$$$#$#$# Prod Type GL " +transactionTO.getDebitAcctNo());
//                                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(dataMap.get("DEBIT_ACC_NO")));
//                                    txMap.put(TransferTrans.DR_PROD_TYPE,TransactionFactory.GL);
//                                    txMap.put(TransferTrans.PARTICULARS, linkBatchId + "-" + ":PAYMENT");
////                                }else if(!CommonUtil.convertObjToStr(dataMap.get("PRODUCT_TYPE")).equals("")){
////                                    System.out.println("$#$$$#$#$# Prod Type Not GL " +CommonUtil.convertObjToStr(dataMap.get("DEBIT_ACC_NO")));
////                                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(dataMap.get("AC_HEAD")));
////                                    txMap.put(TransferTrans.DR_ACT_NUM,CommonUtil.convertObjToStr(dataMap.get("DEBIT_ACC_NO")));
////                                    txMap.put(TransferTrans.DR_PROD_ID,CommonUtil.convertObjToStr(dataMap.get("PROD_ID")));
////                                    txMap.put(TransferTrans.DR_PROD_TYPE,CommonUtil.convertObjToStr(dataMap.get("PRODUCT_TYPE")));
////                                    txMap.put(TransferTrans.PARTICULARS, linkBatchId + "-" + ":PAYMENT");
////                                }
//                                txMap.put(TransferTrans.DR_BRANCH,CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
//                                txMap.put(TransferTrans.DR_INSTRUMENT_1,"PAYROLL");
//                           
//                                 if(dataMap.get("PAY_DESCRI")!=null)
//                                    {
//                                       txMap.put(TransferTrans.NARRATION,dataMap.get("PAY_DESCRI") ); 
//                                    }
//                                    else
//                                    {
//                                        
//                                  txMap.put(TransferTrans.NARRATION, "PAYROLLMDS Transactions");
//                                    }
//                                
//                                 
//                                  
//                                  
//                                  
//                                  
//                                System.out.println("txMap Debit : " + txMap+"paymentAmt :" + paymentAmt);
//                                 System.out.println("doMDSPayment"+paymentAmt);
//                                transferTo =  transactionDAO.addTransferDebitLocal(txMap, paymentAmt);
//                                transferTo.setTransId("-");
//                                transferTo.setBatchId("-");
//                                transferTo.setTransDt(currDt);
//                                transferTo.setBranchId(CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
//                                transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
//                                transferTo.setStatusBy(CommonConstants.TTSYSTEM);
//                                transferTo.setInitTransId(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(dataMap.get("USER_ID"))));
//                                transferTo.setLinkBatchId(mdsAccNo);
//                                transferTo.setInstrumentNo1("PAYROLL");
//                                TxTransferTO.add(transferTo);
//                            } 
//                            System.out.println("rish.............reached herer***********************************");
//      HashMap applicationMap = new HashMap();                // Crdit Acc INSERT Start
//                            applicationMap.put("PROD_ID",dataMap.get("PROD_ID"));
//                            List lst = sqlMap.executeQueryForList("getAccountHeadProdMDS",applicationMap);    // Acc Head
//                            if(lst!=null && lst.size()>0){
//                                applicationMap = (HashMap)lst.get(0);
//                            }
//                        HashMap MDSCharge = new HashMap();
//                           System.out.println("appplication map.........."+applicationMap);
//                           if(interestAmt>0){
//                                MDSCharge.put("CURR_MONTH_INT",String.valueOf(interestAmt));
//                            }
//                            if(penalAmt>0){
//                                MDSCharge.put("PENAL_INT",String.valueOf(penalAmt));
//                            }
//                            HashMap txMap = new HashMap();
//                            if(paymentAmt>0.0){
//                                transferTo = new TxTransferTO();
//                                txMap = new HashMap();
//                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                                txMap.put(TransferTrans.CR_ACT_NUM,  mdsAccNo);
//                                txMap.put(TransferTrans.CR_PROD_ID, dataMap.get("PROD_ID"));
//                                txMap.put(TransferTrans.CR_AC_HD, (String)applicationMap.get("AC_HEAD"));
//                               txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//                                txMap.put(TransferTrans.CR_BRANCH,CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
//                                txMap.put(TransferTrans.PARTICULARS, mdsAccNo+"-" +":PAYMENT");
//                                  // txMap.put(TransferTrans.DR_INSTRUMENT_1,"PAYROLL");
//                                   txMap.put(TransferTrans.DR_INSTRUMENT_1,"PAYROLL");
//                                    txMap.put(TransferTrans.NARRATION, "PAYROLMDS Transactions");
//                                System.out.println("txMap  : " + txMap+"paymentAmt :"+paymentAmt);
//                                // System.out.println("@kkkkkiiii>>>>doLoanPaymentdoLoanPayment"+paymentAmt);
//                                transferTo =  transactionDAO.addTransferCreditLocal(txMap, paymentAmt) ;
//                                transferTo.setTransId("-");
//                                transferTo.setBatchId("-");
//                                transferTo.setTransDt(currDt);
//                                transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
//                                transferTo.setStatusBy(CommonConstants.TTSYSTEM);
//                                transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
//                                transferTo.setLinkBatchId(mdsAccNo);
//                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsAccNo));
//                                transferTo.setInstrumentNo1("PAYROLL");
//                                //transactionTO.setChequeNo("SERVICE_TAX");
//                                TxTransferTO.add(transferTo);
//                            }
//                            TransferDAO transferDAO = new TransferDAO();
//                            tansactionMap.put("ALL_AMOUNT",MDSCharge);
//                            tansactionMap.put("TxTransferTO",TxTransferTO);
//                            tansactionMap.put("MODE",dataMap.get("COMMAND"));
//                            tansactionMap.put("COMMAND",dataMap.get("COMMAND"));
//                            tansactionMap.put(CommonConstants.BRANCH_ID,_branchCode);
//                            tansactionMap.put("LINK_BATCH_ID", mdsAccNo);
//
//                            // This map should be set if authorization should happen immediately
//                            HashMap authorizeMap = new HashMap();
//                            authorizeMap.put("BATCH_ID", null);
//                            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
//                            tansactionMap.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
//                            
//                            System.out.println("################ tansactionMap :"+tansactionMap);
//                            HashMap transMap = transferDAO.execute(tansactionMap,false);
//         

        //end
        //  dataMap.put("mdsReceiptEntryTO", mdsReceiptEntryTO);
//      MDSReceiptEntryDAO objMDSReceiptEntryDAO = new MDSReceiptEntryDAO();
//       HashMap returnMap = objMDSReceiptEntryDAO.execute(dataMap);

    }

//     private Date setProperDtFormat(Date dt) {
//        Date tempDt=(Date)currentDate.clone();
//        if(dt!=null) {
//            tempDt.setDate(dt.getDate());
//            tempDt.setMonth(dt.getMonth());
//            tempDt.setYear(dt.getYear());
//            return tempDt;
//        }
//        return null;
//    }
    private void transactionPartMDS(HashMap chittalMap, HashMap map) throws Exception {
        try {
            System.out.println("############################### chittalMap : " + chittalMap);
            currentDate = ServerUtil.getCurrentDateProperFormat(_branchCode);
            HashMap MDSmap = new HashMap();
            HashMap bonusMap = new HashMap();
            HashMap whereMap = new HashMap();
            HashMap chittalDetailMap = new HashMap();
            HashMap installmentMap = new HashMap();
            String actNo = "";
            String subNo = "";
            String chittalNo = "";
            long count = 0;
            int curInsNo = 0;
            int noOfInsPaid = 0;
            long pendingInst = 0;
            long noOfInstPay = 0;
            double totBonusAmt = 0;
            double totPenalAmt = 0;
            double totNoticeAmt = 0;
            double totDiscountAmt = 0;
            double totArbitrationAmt = 0;
            actNo = CommonUtil.convertObjToStr(chittalMap.get("ACT_NUM"));
            if (actNo.indexOf("_") != -1) {
                chittalNo = actNo.substring(0, actNo.indexOf("_"));
                subNo = actNo.substring(actNo.indexOf("_") + 1, actNo.length());
            }
            whereMap.put("CHITTAL_NO", chittalNo);
            whereMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
            List chittalList = (List) sqlMap.executeQueryForList("getMDSChittalDetails", whereMap);
            if (chittalList != null && chittalList.size() > 0) {
                chittalDetailMap = (HashMap) chittalList.get(0);
                //
                whereMap.put("INT_CALC_UPTO_DT", currentDate);
                //                List bonusList = sqlMap.executeQueryForList("getRecoveryListInstallmentMapDetails", whereMap);
                //                if (bonusList != null && bonusList.size() > 0) {
                //                    for(int i=0; i<bonusList.size();i++){
                HashMap instMap = new HashMap();
                //                        bonusMap = (HashMap) bonusList.get(i);
                totBonusAmt += CommonUtil.convertObjToDouble(chittalMap.get("BONUS_AMT")).doubleValue();
                totPenalAmt += CommonUtil.convertObjToDouble(chittalMap.get("PENAL_AMT")).doubleValue();
                totDiscountAmt += CommonUtil.convertObjToDouble(chittalMap.get("DISCOUNT_AMT")).doubleValue();
                //                        if(i==0){
                totNoticeAmt = CommonUtil.convertObjToDouble(chittalMap.get("NOTICE_AMT")).doubleValue();
                totNoticeAmt = CommonUtil.convertObjToDouble(chittalMap.get("CHARGES")).doubleValue();
                totArbitrationAmt = CommonUtil.convertObjToDouble(chittalMap.get("ARBITRATION_AMT")).doubleValue();
                //                        }
                instMap.put("BONUS", chittalMap.get("BONUS_AMT"));
                instMap.put("DISCOUNT", chittalMap.get("DISCOUNT_AMT"));
                instMap.put("PENAL", chittalMap.get("PENAL_AMT"));
                instMap.put("INST_AMT", chittalDetailMap.get("INST_AMT"));

                //                    }
                //                }
                List insList = sqlMap.executeQueryForList("getNoOfInstalmentsPaid", whereMap);
                if (insList != null && insList.size() > 0) {
                    whereMap = (HashMap) insList.get(0);
                    noOfInsPaid = CommonUtil.convertObjToInt(whereMap.get("NO_OF_INST"));
                    count = CommonUtil.convertObjToLong(whereMap.get("NO_OF_INST"));
                }
                HashMap insDateMap = new HashMap();
                insDateMap.put("DIVISION_NO", CommonUtil.convertObjToInt(chittalDetailMap.get("DIVISION_NO")));
                insDateMap.put("SCHEME_NAME", chittalDetailMap.get("SCHEME_NAME"));
                insDateMap.put("CURR_DATE", currentDate);
                insDateMap.put("ADD_MONTHS", CommonUtil.convertObjToInt("-1"));
                List insDateLst = sqlMap.executeQueryForList("getMDSCurrentInsDate", insDateMap);
                if (insDateLst != null && insDateLst.size() > 0) {
                    insDateMap = (HashMap) insDateLst.get(0);
                    curInsNo = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO"));
                    pendingInst = curInsNo - noOfInsPaid;
                    if (pendingInst < 0) {
                        pendingInst = 0;
                    }
                    if (pendingInst > 0) {
                        noOfInstPay = pendingInst;
                    }

                    System.out.println("######## NoOfInstToPay *** : " + noOfInstPay);
                }
                installmentMap.put(CommonUtil.convertObjToStr(insDateMap.get("INSTALLMENT_NO")), instMap);
                //Narration
                int insDay = 0;
                Date paidUpToDate = null;
                HashMap instDateMap = new HashMap();
                instDateMap.put("SCHEME_NAME", chittalDetailMap.get("SCHEME_NAME"));
                instDateMap.put("DIVISION_NO", CommonUtil.convertObjToInt(chittalDetailMap.get("DIVISION_NO")));
                instDateMap.put("INSTALLMENT_NO", CommonUtil.convertObjToInt(String.valueOf(count)));
                List insLst = sqlMap.executeQueryForList("getSelectInstUptoPaid", instDateMap);
                if (insLst != null && insLst.size() > 0) {
                    instDateMap = (HashMap) insLst.get(0);
                    paidUpToDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(instDateMap.get("NEXT_INSTALLMENT_DATE")));
                } else {
                    Date startedDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalDetailMap.get("SCHEME_START_DT")));
                    insDay = CommonUtil.convertObjToInt(chittalDetailMap.get("INSTALLMENT_DAY"));
                    startedDate.setDate(insDay);
                    int stMonth = startedDate.getMonth();
                    startedDate.setMonth(stMonth + (int) count - 1);
                    paidUpToDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(startedDate));
                }
                String narration = "";
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM/yyyy");
                int noInstPay = CommonUtil.convertObjToInt(String.valueOf(noOfInstPay));
                if (noInstPay == 1) {
                    narration = "Inst#" + (noOfInsPaid + 1);
                    Date dt = DateUtil.addDays(paidUpToDate, 30);
                    narration += " " + sdf.format(dt);
                } else if (noInstPay > 1) {
                    narration = "Inst#" + (noOfInsPaid + 1);
                    narration += "-" + (noOfInsPaid + noInstPay);
                    Date dt = DateUtil.addDays(paidUpToDate, 30);
                    narration += " " + sdf.format(dt);
                    dt = DateUtil.addDays(paidUpToDate, 30 * noInstPay);
                    narration += " To " + sdf.format(dt);
                }
                System.out.println("#$#$# narration :" + narration);
                //SET RECEIPT_ENTRY_TO
                mdsReceiptEntryTO = new MDSReceiptEntryTO();
                mdsReceiptEntryTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                mdsReceiptEntryTO.setSchemeName(CommonUtil.convertObjToStr(chittalDetailMap.get("SCHEME_NAME")));
                mdsReceiptEntryTO.setChittalNo(chittalNo);
                mdsReceiptEntryTO.setSubNo(CommonUtil.convertObjToDouble(subNo));
                mdsReceiptEntryTO.setMemberName(CommonUtil.convertObjToStr(chittalDetailMap.get("MEMBER_NAME")));
                mdsReceiptEntryTO.setDivisionNo(CommonUtil.convertObjToDouble(chittalDetailMap.get("DIVISION_NO")));
                mdsReceiptEntryTO.setChitStartDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalDetailMap.get("CHIT_START_DT"))));
                mdsReceiptEntryTO.setChitEndDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalDetailMap.get("CHIT_END_DT"))));
                mdsReceiptEntryTO.setPaidDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalMap.get("INT_CALC_UPTO_DT"))));
                mdsReceiptEntryTO.setNoOfInst(CommonUtil.convertObjToInt(chittalDetailMap.get("NO_OF_INSTALLMENTS")));
                mdsReceiptEntryTO.setCurrInst(CommonUtil.convertObjToDouble(String.valueOf(curInsNo)));
                mdsReceiptEntryTO.setPendingInst(CommonUtil.convertObjToDouble(String.valueOf(pendingInst)));
                mdsReceiptEntryTO.setInstAmt(CommonUtil.convertObjToDouble(chittalDetailMap.get("INST_AMT")));
                mdsReceiptEntryTO.setNoOfInstPay(CommonUtil.convertObjToInt(String.valueOf(noOfInstPay)));
                mdsReceiptEntryTO.setInstAmtPayable(CommonUtil.convertObjToDouble(chittalMap.get("PRINCIPAL")));
                mdsReceiptEntryTO.setPaidInst(CommonUtil.convertObjToDouble(String.valueOf(noOfInsPaid)));
                mdsReceiptEntryTO.setBonusAmtPayable(CommonUtil.convertObjToDouble(String.valueOf(totBonusAmt)));
                mdsReceiptEntryTO.setDiscountAmt(CommonUtil.convertObjToDouble(String.valueOf(totDiscountAmt)));
                mdsReceiptEntryTO.setPenalAmtPayable(CommonUtil.convertObjToDouble(String.valueOf(totPenalAmt)));
                mdsReceiptEntryTO.setNoticeAmt(CommonUtil.convertObjToDouble(String.valueOf(totNoticeAmt)));
                mdsReceiptEntryTO.setArbitrationAmt(CommonUtil.convertObjToDouble(String.valueOf(totArbitrationAmt)));
                mdsReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(chittalMap.get("TOTAL_DEMAND")));
                mdsReceiptEntryTO.setNarration(narration);
                mdsReceiptEntryTO.setBankPay("N");
                mdsReceiptEntryTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
                mdsReceiptEntryTO.setStatus(CommonConstants.STATUS_CREATED);
                mdsReceiptEntryTO.setStatusDt(currentDate);
                mdsReceiptEntryTO.setBranchCode(CommonUtil.convertObjToStr(chittalDetailMap.get("BRANCH_CODE")));
                mdsReceiptEntryTO.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                System.out.println("############################### mdsReceiptEntryTO : " + mdsReceiptEntryTO);
                System.out.println("############################### installmentMap : " + installmentMap);

                MDSmap.put("INSTALLMENT_MAP", installmentMap);
                MDSmap.put("mdsReceiptEntryTO", mdsReceiptEntryTO);
                List closureList = (List) sqlMap.executeQueryForList("checkSchemeClosureDetails", chittalDetailMap);
                if (closureList != null && closureList.size() > 0) {
                    MDSmap.put("MDS_CLOSURE", "MDS_CLOSURE");
                } else {
                    MDSmap.remove("MDS_CLOSURE");
                }
                MDSmap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                MDSmap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                MDSmap.put("TransactionTO", map.get("TransactionTO"));
                MDSmap.put("INT_CALC_UPTO_DT", currentDate);
                MDSmap.put("FROM_RECOVERY_TALLY", "FROM_RECOVERY_TALLY");
                MDSmap.put("POSTAGE_AMT_FOR_INT", chittalMap.get("POSTAGE_AMT_FOR_INT"));
                MDSmap.put("RENEW_POSTAGE_AMT", chittalMap.get("RENEW_POSTAGE_AMT"));
                MDSmap.put("POSTAGE_ACHD", chittalMap.get("POSTAGE_ACHD"));
                System.out.println("########### MDSmap : " + MDSmap);
                mdsReceiptEntryDAO = new MDSReceiptEntryDAO();
                HashMap transMap = new HashMap();
                transMap = mdsReceiptEntryDAO.execute(MDSmap, false);                    // INSERT_TRANSACTION
                System.out.println("################# transMap : " + transMap);

                //AUTHORIZE_START
                HashMap authorizeMap = new HashMap();
                authorizeMap.put("NET_TRANS_ID", transMap.get("BATCH_ID"));
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                authorizeMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                authorizeMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                mdsReceiptEntryTO.setCommand("");
                MDSmap.put("AUTHORIZEMAP", authorizeMap);
                MDSmap.put("mdsReceiptEntryTO", mdsReceiptEntryTO);
                MDSmap.put("SCHEME_NAME", CommonUtil.convertObjToStr(chittalDetailMap.get("SCHEME_NAME")));
                MDSmap.put("USER_ID", map.get(CommonConstants.USER_ID));
                System.out.println("########### MDSmap : " + MDSmap);
                mdsReceiptEntryDAO = new MDSReceiptEntryDAO();
                HashMap transDetMap = mdsReceiptEntryDAO.execute(MDSmap, false);                                     // AUTHORIZE_TRANSACTION
                System.out.println("########### transDetMap : " + transDetMap);
                if (transMap != null && transMap.size() > 0) {
                    paramMap.put(paramMap.get("ACT_NUM"), transMap.get("BATCH_ID"));
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void doRDPayment(HashMap dataMap) throws Exception {
        HashMap accountMap = new HashMap();
        //        ArrayList row;
//                accountMap.put("DEPOSIT_NO",dataMap.get("ACT_NUM"));
//                accountMap.put("BRANCH_ID",dataMap.get("BRANCH_ID"));
//                List lst = sqlMap.executeQueryForList("getProductIdForDeposits", accountMap);
//                if(lst != null && lst.size()>0){
//                    accountMap = (HashMap)lst.get(0);
//                    HashMap prodMap = new HashMap();
//                    prodMap.put("PROD_ID",accountMap.get("PROD_ID"));
//                    List lstBehave = sqlMap.executeQueryForList("getBehavesLikeForDeposit", prodMap);
//                    if(lstBehave != null && lstBehave.size()>0){
//                        prodMap = (HashMap)lstBehave.get(0);
//                        if(prodMap.get("BEHAVES_LIKE").equals("RECURRING")){
//                            Date currDt = currDt.clone();
//                            double balanceAmt = 0.0;
//                            long totalDelay =0;
//                            long actualDelay =0;
//                            double delayAmt = 0.0;
//                            double tot_Inst_paid = 0.0;
//                            double depAmt = CommonUtil.convertObjToDouble(accountMap.get("DEPOSIT_AMT")).doubleValue();
//                            Date matDt = currDt.clone();
//                            Date depDt = currDt.clone();
//                            HashMap lastMap = new HashMap();
//                            lastMap.put("DEPOSIT_NO",dataMap.get("ACC_NO"));
//                            lst = sqlMap.executeQueryForList("getInterestDeptIntTable", lastMap);
//                            System.out.println("#######getLastApplDt for balanceMap:"+lst);
//                            if(lst!=null && lst.size()>0) {
//                                lastMap = (HashMap)lst.get(0);
//                                tot_Inst_paid = CommonUtil.convertObjToDouble(lastMap.get("TOTAL_INSTALL_PAID")).doubleValue();
//                                HashMap prematureDateMap = new HashMap();
//                                double monthPeriod = 0.0;
//                                Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lastMap.get("MATURITY_DT")));
//                                if(matDate.getDate()>0){
//                                    matDt.setDate(matDate.getDate());
//                                    matDt.setMonth(matDate.getMonth());
//                                    matDt.setYear(matDate.getYear());
//                                }
//                                Date depDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lastMap.get("DEPOSIT_DT")));
//                                if(depDate.getDate()>0){
//                                    depDt.setDate(depDate.getDate());
//                                    depDt.setMonth(depDate.getMonth());
//                                    depDt.setYear(depDate.getYear());
//                                }
//                                if(DateUtil.dateDiff((Date)matDt,(Date)currDt)>0){
//                                    prematureDateMap.put("TO_DATE",matDt);
//                                    prematureDateMap.put("FROM_DATE",lastMap.get("DEPOSIT_DT"));
//                                    lst = sqlMap.executeQueryForList("periodRunMap", prematureDateMap);
//                                    if(lst!=null && lst.size()>0){
//                                        prematureDateMap = (HashMap)lst.get(0);
//                                        monthPeriod = CommonUtil.convertObjToDouble(prematureDateMap.get("NO_OF_MONTHS")).doubleValue();
//                                        actualDelay = (long) monthPeriod - (long)tot_Inst_paid;
//                                    }
//                                    lst = null;
//                                }else{
//                                    int dep = depDt.getMonth()+1;
//                                    int curr = currDt.getMonth()+1;
//                                    int depYear = depDt.getYear()+1900;
//                                    int currYear = currDt.getYear()+1900;
//                                    if(depYear == currYear){
//                                        monthPeriod = curr - dep;
//                                        actualDelay = (long) monthPeriod - (long)tot_Inst_paid;
//                                    }else{
//                                        int diffYear = currYear - depYear;
//                                        monthPeriod = (diffYear * 12 - dep) + curr;
//                                        actualDelay = (long) monthPeriod - (long)tot_Inst_paid;
//                                    }
//                                }
//                            }
//                            lst = null;
//                            //delayed installment calculation...
//                            if(DateUtil.dateDiff((Date)matDt,(Date)currDt)<0){
//                           //     setPenalAmount(String.valueOf(0.0));
//                           //     setPenalMonth(String.valueOf(0.0));
//                                depAmt = CommonUtil.convertObjToDouble(accountMap.get("DEPOSIT_AMT")).doubleValue();
//                                double chargeAmt = depAmt /100;
//                                HashMap delayMap = new HashMap();
//                                delayMap.put("PROD_ID",accountMap.get("PROD_ID"));
//                                delayMap.put("DEPOSIT_AMT",accountMap.get("DEPOSIT_AMT"));
//                                lst = sqlMap.executeQueryForList("getSelectDelayedRate",delayMap);
//                                if(lst != null && lst.size()>0){
//                                    delayMap = (HashMap)lst.get(0);
//                                    delayAmt = CommonUtil.convertObjToDouble(delayMap.get("ROI")).doubleValue();
//                                    delayAmt = delayAmt * chargeAmt;
//                                    System.out.println("######recurring delayAmt : "+delayAmt);
//                                }
//                                lst = null;
//                                HashMap depRecMap = new HashMap();
//                                depRecMap.put("DEPOSIT_NO",dataMap.get("ACC_NO") +"_1");
//                                List lstRec = sqlMap.executeQueryForList("getDepTransactionRecurring", depRecMap);
//                                if(lstRec !=null && lstRec.size()>0){//how many unCompleted quarter is there upto (one by one) we have to calculate simple interest formula....
//                                    for(int i = 0; i<lstRec.size();i++){//when the customer is paid from that dt to today dt....
//                                        depRecMap = (HashMap)lstRec.get(i);
//                                        Date transDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("TRANS_DT")));
//                                        Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("DUE_DATE")));
//                                        int transMonth = transDt.getMonth()+1;
//                                        int dueMonth = dueDate.getMonth()+1;
//                                        int dueYear = dueDate.getYear()+1900;
//                                        int transYear = transDt.getYear()+1900;
//                                        int delayedInstallment;// = transMonth - dueMonth;
//                                        if(dueYear == transYear){
//                                            delayedInstallment = transMonth - dueMonth;
//                                        }else{
//                                            int diffYear = transYear - dueYear;
//                                            delayedInstallment = (diffYear * 12 - dueMonth) + transMonth;
//                                        }
//                                        if(delayedInstallment<0)
//                                            delayedInstallment = 0;
//                                        totalDelay = totalDelay + delayedInstallment;
//                                    }
//                                }
//                                lstRec = null;
//                                depRecMap = new HashMap();
//                                depRecMap.put("DEPOSIT_NO",dataMap.get("ACC_NO") +"_1");
//                                depRecMap.put("DEPOSIT_DT",lastMap.get("DEPOSIT_DT"));
//                                depRecMap.put("CURR_DT", currDt);
//                                depRecMap.put("SL_NO", new Double(tot_Inst_paid));
//                                lstRec = sqlMap.executeQueryForList("getDepTransRecurr", depRecMap);
//                                if(lstRec !=null && lstRec.size()>0){//how many unCompleted quarter is there upto (one by one) we have to calculate simple interest formula....
//                                    for(int i = 0; i<lstRec.size();i++){//when the customer is paid from that dt to today dt....
//                                        depRecMap = (HashMap)lstRec.get(i);
//                                        Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("DUE_DATE")));
//                                        int transMonth = currDt.getMonth()+1;
//                                        int dueMonth = dueDate.getMonth()+1;
//                                        int dueYear = dueDate.getYear()+1900;
//                                        int transYear = currDt.getYear()+1900;
//                                        int delayedInstallment;// = transMonth - dueMonth;
//                                        if(dueYear == transYear){
//                                            delayedInstallment = transMonth - dueMonth;
//                                        }else{
//                                            int diffYear = transYear - dueYear;
//                                            delayedInstallment = (diffYear * 12 - dueMonth) + transMonth;
//                                        }
//                                        if(delayedInstallment<0)
//                                            delayedInstallment = 0;
//                                        totalDelay = totalDelay + delayedInstallment;
//                                    }
//                                }
//                                lstRec = null;
//                                delayAmt = delayAmt * totalDelay;
//                              //  delayAmt = (double)getNearest((long)(delayAmt *100),100)/100;
//                                double oldPenalAmt = CommonUtil.convertObjToDouble(accountMap.get("DELAYED_AMOUNT")).doubleValue();
//                                long oldPenalMonth = CommonUtil.convertObjToLong(accountMap.get("DELAYED_MONTH"));
//                                balanceAmt = 0.0;
//                                if(oldPenalAmt >0){
//                                    balanceAmt = delayAmt - oldPenalAmt;
//                                    totalDelay = totalDelay - oldPenalMonth;
//                                }else
//                                    balanceAmt = delayAmt;
//                            }
//                            

        // Take "balanceAmt" variable for Penal Amount
        System.out.println("RD PAYMENT MAP server :" + dataMap);

        TransferTrans objTransferTrans = new TransferTrans();
        objTransferTrans.setInitiatedBranch(_branchCode);
        HashMap txMap = new HashMap();
        ArrayList transferList = new ArrayList();
        txMap.put(TransferTrans.PARTICULARS, "To " + dataMap.get("ACT_NUM") + " ");
        txMap.put(CommonConstants.USER_ID, dataMap.get("USER_ID"));
        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
//                            txMap.put(CommonConstants.AUTHORIZESTATUS,"ENTERED_AMOUNT");
        txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
        // In case the Debit Account type is GL
        txMap.put(TransferTrans.DR_AC_HD, dataMap.get("DEBIT_ACC_HD"));  // Give the Debit GL Head here
        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
        txMap.put(TransferTrans.DR_INSTRUMENT_1, "PAYROLL");
        txMap.put("GL_TRANS_ACT_NUM", dataMap.get("AC_NO"));

        if (dataMap.get("PAY_DESCRI") != null) {
            txMap.put(TransferTrans.NARRATION, dataMap.get("PAY_DESCRI"));
        } else {

            txMap.put(TransferTrans.NARRATION, "PAYROLLRD Payment");
        }





//                            // In case the Debit Account type is Operative
//                                txMap.put(TransferTrans.DR_ACT_NUM, dataMap.get("ACT_NUM")); // Give the Debit A/c no here
//                               // Give the Debit A/c Product ID here
//                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);

        double debitAmt = Double.parseDouble("" + dataMap.get("DEPOSIT_AMT"));
        double penalAmt = 0.0;
        if (dataMap.get("DEPOSIT_PENAL_AMT") != null) {
            penalAmt = Double.parseDouble("" + dataMap.get("DEPOSIT_PENAL_AMT"));
        }
        debitAmt += penalAmt;
        System.out.println("@kkkkkiiii>>>>doRDPaymentdoRDPayment" + debitAmt + "   " + penalAmt);
        transferList.add(objTransferTrans.getDebitTransferTO(txMap, debitAmt));
        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
        txMap.put(TransferTrans.CR_PROD_ID, dataMap.get("PROD_ID"));
        txMap.put(TransferTrans.CR_ACT_NUM, dataMap.get("ACT_NUM"));
        txMap.put("AUTHORIZEREMARKS", "");  // Any remarks
        txMap.put("GL_TRANS_ACT_NUM", dataMap.get("AC_NO"));

        // double creditAmt = debitAmt;// Give whichever amount wanted to Credit Ex: 5 * 500 (5 months due) + 50 (penal amount)
        transferList.add(objTransferTrans.getCreditTransferTO(txMap, debitAmt));
        tansactionMap = new HashMap();
        tansactionMap.put("SCREEN", "PAYROLL");
        tansactionMap.put("MODULE", "Transaction");
        tansactionMap.put(CommonConstants.BRANCH_ID, _branchCode);
        tansactionMap.put(CommonConstants.USER_ID, dataMap.get("USER_ID"));
        tansactionMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);

        tansactionMap.put("TxTransferTO", transferList);
        // While calling Transfer Transactions along with other required values pass the following value also.
//                            tansactionMap.put("DEPOSIT_PENAL_AMT",String.valueOf(dataMap.get("DEPOSIT_AMT")));

        if (dataMap.get("DEPOSIT_PENAL_AMT") != null) {
            // At the time of authorization pass the following values
            tansactionMap.put("DEPOSIT_PENAL_AMT", dataMap.get("DEPOSIT_PENAL_AMT"));
            tansactionMap.put("DEPOSIT_PENAL_MONTH", dataMap.get("SL_NO"));
        }
        // This map should be set if authorization should happen immediately
        HashMap authorizeMap = new HashMap();
        authorizeMap.put("BATCH_ID", null);
        authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
        tansactionMap.put(CommonConstants.AUTHORIZEMAP, authorizeMap);

        TransferDAO objTransferDAO = new TransferDAO();
        objTransferDAO.execute(tansactionMap);
        objTransferTrans = null;
        objTransferDAO = null;
        tansactionMap.clear();
        tansactionMap = null;
        transferList.clear();
        transferList = null;
    }

    private void doAdvancesPayment(HashMap dataMap) {
    }

    private void doOAPayment(HashMap dataMap) throws Exception {

        String accNo = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));
        ArrayList transferList = new ArrayList();
        TransferTrans transferTrans = new TransferTrans();
        transferTrans.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        transactionDAO.setInitiatedBranch(_branchCode);
        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
        HashMap tansactionMap = new HashMap();
        TxTransferTO transferTo = new TxTransferTO();
        ArrayList TxTransferTO = new ArrayList();
        double paymentAmt = 0.0;

        //dataMap = (HashMap)shgList.get(i);
        paymentAmt = CommonUtil.convertObjToDouble(dataMap.get("PAYMENT")).doubleValue();

        transferTo.setInstrumentNo2("APPL_GL_TRANS");
        if (paymentAmt > 0) {//Debit Insert Start
            HashMap txMap = new HashMap();
            System.out.println("Transfer Started debit : ");
            transferTo.setInstrumentNo2("APPL_GL_TRANS");
            //System.out.println("$#$$$#$#$# debitMap " +debitMap);
//                                if(!CommonUtil.convertObjToStr(dataMap.get("PRODUCT_TYPE")).equals("") && CommonUtil.convertObjToStr(dataMap.get("PRODUCT_TYPE")).equals("GL") && CommonUtil.convertObjToStr(dataMap.get("PRODUCT_TYPE")).equals("")){
            //System.out.println("$#$$$#$#$# Prod Type GL " +transactionTO.getDebitAcctNo());
            txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(dataMap.get("DEBIT_ACC_NO")));
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.PARTICULARS, accNo + "-" + ":PAYMENT");
//                                }else if(!CommonUtil.convertObjToStr(dataMap.get("PRODUCT_TYPE")).equals("")){
//                                    System.out.println("$#$$$#$#$# Prod Type Not GL " +CommonUtil.convertObjToStr(dataMap.get("DEBIT_ACC_NO")));
//                                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(dataMap.get("AC_HEAD")));
//                                    txMap.put(TransferTrans.DR_ACT_NUM,CommonUtil.convertObjToStr(dataMap.get("DEBIT_ACC_NO")));
//                                    txMap.put(TransferTrans.DR_PROD_ID,CommonUtil.convertObjToStr(dataMap.get("PROD_ID")));
//                                    txMap.put(TransferTrans.DR_PROD_TYPE,CommonUtil.convertObjToStr(dataMap.get("PRODUCT_TYPE")));
//                                    txMap.put(TransferTrans.PARTICULARS, linkBatchId + "-" + ":PAYMENT");
//                                }
            txMap.put(TransferTrans.DR_INSTRUMENT_1, "PAYROLL");

            if (dataMap.get("PAY_DESCRI") != null) {
                txMap.put(TransferTrans.NARRATION, dataMap.get("PAY_DESCRI"));
            } else {

                txMap.put(TransferTrans.NARRATION, "PAYROLLOA Payment");
            }





            txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
            System.out.println("CBMS OA PAYMENT ACCOUNT NUMBER=======" + accNo);
            System.out.println("txMap Debit : " + txMap + "paymentAmt :" + paymentAmt);
            System.out.println("@kkkkkiiii>>>>doOAPaymentdoOAPaymentdoOAPayment" + paymentAmt);

            txMap.put("GL_TRANS_ACT_NUM", dataMap.get("AC_NO"));
            //System.out.println("txmap in..............................oa" + txMap);
            transferTo = transactionDAO.addTransferDebitLocal(txMap, paymentAmt);
            transferTo.setTransId("-");
            transferTo.setBatchId("-");
            transferTo.setTransDt(currDt);
            transferTo.setBranchId(CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
            transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
            transferTo.setStatusBy(CommonConstants.TTSYSTEM);
            transferTo.setInitTransId(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(dataMap.get("USER_ID"))));
            transferTo.setLinkBatchId(accNo);
            transferTo.setInstrumentNo1("PAYROLL");
            TxTransferTO.add(transferTo);
        }                                                       // Debit Acc INSERT End

        HashMap applicationMap = new HashMap();                // Crdit Acc INSERT Start
        applicationMap.put("PROD_ID", dataMap.get("PROD_ID"));


        HashMap txMap = new HashMap();
        if (paymentAmt > 0.0) {
            transferTo = new TxTransferTO();
            txMap = new HashMap();
            transferTo.setInstrumentNo2("APPL_GL_TRANS");
            txMap.put(TransferTrans.CR_ACT_NUM, accNo);
            txMap.put(TransferTrans.CR_PROD_ID, dataMap.get("PROD_ID"));
//                                txMap.put(TransferTrans.CR_AC_HD, (String)applicationMap.get("AC_HEAD"));
            if (dataMap.get("PRODUCT_TYPE").equals("OA")) {
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
            } else if (dataMap.get("PRODUCT_TYPE").equals("AD")) {
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
            }
            txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
            txMap.put(TransferTrans.PARTICULARS, accNo + "-" + ":PAYMENT");
            txMap.put(TransferTrans.DR_INSTRUMENT_1, "PAYROLL");

            if (dataMap.get("PAY_DESCRI") != null) {
                txMap.put(TransferTrans.NARRATION, dataMap.get("PAY_DESCRI"));
            } else {

                txMap.put(TransferTrans.NARRATION, "PAYROLLOA Payment");
            }



            // txMap.put(TransferTrans.NARRATION, "PAYROLLOA Transactions");
            System.out.println("txMap  : " + txMap + "paymentAmt :" + paymentAmt);
            System.out.println("@kkkkkiiii>>>>doOAPaymentdoOAPaymentdoOAPayment" + paymentAmt);
            txMap.put("GL_TRANS_ACT_NUM", dataMap.get("AC_NO"));
            transferTo = transactionDAO.addTransferCreditLocal(txMap, paymentAmt);
            transferTo.setTransId("-");
            transferTo.setBatchId("-");
            transferTo.setTransDt(currDt);
            transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
            transferTo.setStatusBy(CommonConstants.TTSYSTEM);
            transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            transferTo.setLinkBatchId(accNo);
            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(accNo));
            //transactionTO.setChequeNo("SERVICE_TAX");
            transferTo.setInstrumentNo1("PAYROLL");
            TxTransferTO.add(transferTo);
        }
        TransferDAO transferDAO = new TransferDAO();
        tansactionMap.put("TxTransferTO", TxTransferTO);
        tansactionMap.put("MODE", dataMap.get("COMMAND"));
        tansactionMap.put("COMMAND", dataMap.get("COMMAND"));
        tansactionMap.put(CommonConstants.BRANCH_ID, _branchCode);
        tansactionMap.put("LINK_BATCH_ID", accNo);

        // This map should be set if authorization should happen immediately
        HashMap authorizeMap = new HashMap();
        authorizeMap.put("BATCH_ID", null);
        authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
        tansactionMap.put(CommonConstants.AUTHORIZEMAP, authorizeMap);

        System.out.println("################ tansactionMap :" + tansactionMap);
        HashMap transMap = transferDAO.execute(tansactionMap, false);

    }

    private void doLoanPayment(HashMap dataMap) throws Exception {
        System.out.println("\n doLoanPayment\n");
        String linkBatchId = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));
        TransferTrans transferTrans = new TransferTrans();
        transferTrans.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
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
        //dataMap = (HashMap)shgList.get(i);
        paymentAmt = CommonUtil.convertObjToDouble(dataMap.get("PAYMENT")).doubleValue();
        interestAmt = CommonUtil.convertObjToDouble(dataMap.get("INT_DUE")).doubleValue();
        penalAmt = CommonUtil.convertObjToDouble(dataMap.get("PENAL")).doubleValue();
        loanAccNo = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));
        transferTo.setInstrumentNo2("APPL_GL_TRANS");
        if (paymentAmt > 0) {//Debit Insert Start
            HashMap txMap = new HashMap();
            System.out.println("Transfer Started debit : ");
            transferTo.setInstrumentNo2("APPL_GL_TRANS");
            //System.out.println("$#$$$#$#$# debitMap " +debitMap);
//                                if(!CommonUtil.convertObjToStr(dataMap.get("PRODUCT_TYPE")).equals("") && CommonUtil.convertObjToStr(dataMap.get("PRODUCT_TYPE")).equals("GL") && CommonUtil.convertObjToStr(dataMap.get("PRODUCT_TYPE")).equals("")){
            //System.out.println("$#$$$#$#$# Prod Type GL " +transactionTO.getDebitAcctNo());
            txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(dataMap.get("DEBIT_ACC_NO")));
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.PARTICULARS, linkBatchId + "-" + ":PAYMENT");
//                                }else if(!CommonUtil.convertObjToStr(dataMap.get("PRODUCT_TYPE")).equals("")){
//                                    System.out.println("$#$$$#$#$# Prod Type Not GL " +CommonUtil.convertObjToStr(dataMap.get("DEBIT_ACC_NO")));
//                                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(dataMap.get("AC_HEAD")));
//                                    txMap.put(TransferTrans.DR_ACT_NUM,CommonUtil.convertObjToStr(dataMap.get("DEBIT_ACC_NO")));
//                                    txMap.put(TransferTrans.DR_PROD_ID,CommonUtil.convertObjToStr(dataMap.get("PROD_ID")));
//                                    txMap.put(TransferTrans.DR_PROD_TYPE,CommonUtil.convertObjToStr(dataMap.get("PRODUCT_TYPE")));
//                                    txMap.put(TransferTrans.PARTICULARS, linkBatchId + "-" + ":PAYMENT");
//                                }
            txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
            txMap.put(TransferTrans.DR_INSTRUMENT_1, "PAYROLL");
            txMap.put("GL_TRANS_ACT_NUM", dataMap.get("AC_NO"));
            if (dataMap.get("PAY_DESCRI") != null) {
                txMap.put(TransferTrans.NARRATION, dataMap.get("PAY_DESCRI"));
            } else {

                txMap.put(TransferTrans.NARRATION, "PAYROLLLOAN Transactions");
            }

            System.out.println("txMap Debit : " + txMap + "paymentAmt :" + paymentAmt);
            System.out.println("@kkkkkiiii>>>>doLoanPaymentdoLoanPayment" + paymentAmt);
            transferTo = transactionDAO.addTransferDebitLocal(txMap, paymentAmt);
            transferTo.setTransId("-");
            transferTo.setBatchId("-");
            transferTo.setTransDt(currDt);
            transferTo.setBranchId(CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
            transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
            transferTo.setStatusBy(CommonConstants.TTSYSTEM);
            transferTo.setInitTransId(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(dataMap.get("USER_ID"))));
            transferTo.setLinkBatchId(loanAccNo);
            transferTo.setInstrumentNo1("PAYROLL");
            TxTransferTO.add(transferTo);
        }                                                       // Debit Acc INSERT End

        HashMap applicationMap = new HashMap();                // Crdit Acc INSERT Start
        applicationMap.put("PROD_ID", dataMap.get("PROD_ID"));
        List lst = sqlMap.executeQueryForList("getAccountHeadProdTL", applicationMap);    // Acc Head
        if (lst != null && lst.size() > 0) {
            applicationMap = (HashMap) lst.get(0);
        }
        HashMap TermLoanCloseCharge = new HashMap();
        TermLoanCloseCharge.put("BRANCH_CODE", CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
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

        HashMap txMap = new HashMap();
        if (paymentAmt > 0.0) {
            transferTo = new TxTransferTO();
            txMap = new HashMap();
            transferTo.setInstrumentNo2("APPL_GL_TRANS");
            txMap.put(TransferTrans.CR_ACT_NUM, loanAccNo);
            txMap.put(TransferTrans.CR_PROD_ID, dataMap.get("PROD_ID"));
            txMap.put(TransferTrans.CR_AC_HD, (String) applicationMap.get("AC_HEAD"));
            if (dataMap.get("PRODUCT_TYPE").equals("SA")) {
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.SUSPENSE);
            } else {
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
            }
            txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
            txMap.put(TransferTrans.PARTICULARS, loanAccNo + "-" + ":PAYMENT");
            // txMap.put(TransferTrans.DR_INSTRUMENT_1,"PAYROLL");
            txMap.put(TransferTrans.DR_INSTRUMENT_1, "PAYROLL");
            txMap.put(TransferTrans.NARRATION, "PAYROLLLOAN Transactions");
            txMap.put("GL_TRANS_ACT_NUM", dataMap.get("AC_NO"));
            System.out.println("txMap  : " + txMap + "paymentAmt :" + paymentAmt);
            System.out.println("@kkkkkiiii>>>>doLoanPaymentdoLoanPayment" + paymentAmt);
            transferTo = transactionDAO.addTransferCreditLocal(txMap, paymentAmt);
            transferTo.setTransId("-");
            transferTo.setBatchId("-");
            transferTo.setTransDt(currDt);
            transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
            transferTo.setStatusBy(CommonConstants.TTSYSTEM);
            transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            transferTo.setLinkBatchId(loanAccNo);
            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(loanAccNo));
            transferTo.setInstrumentNo1("PAYROLL");
            //transactionTO.setChequeNo("SERVICE_TAX");
            TxTransferTO.add(transferTo);
        }
        TransferDAO transferDAO = new TransferDAO();
        tansactionMap.put("ALL_AMOUNT", TermLoanCloseCharge);
        tansactionMap.put("TxTransferTO", TxTransferTO);
        tansactionMap.put("MODE", dataMap.get("COMMAND"));
        tansactionMap.put("COMMAND", dataMap.get("COMMAND"));
        tansactionMap.put(CommonConstants.BRANCH_ID, _branchCode);
        tansactionMap.put("LINK_BATCH_ID", loanAccNo);

        // This map should be set if authorization should happen immediately
        HashMap authorizeMap = new HashMap();
        authorizeMap.put("BATCH_ID", null);
        authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
        tansactionMap.put(CommonConstants.AUTHORIZEMAP, authorizeMap);

        System.out.println("################ tansactionMap :" + tansactionMap);
        HashMap transMap = transferDAO.execute(tansactionMap, false);
    }

    private void getTransDetails(String batchId) throws Exception {
        System.out.println("\n getTransDetails \n");
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
        execReturnMap = new HashMap();
        List transList = (List) sqlMap.executeQueryForList("getTransferDetails", getTransMap);
        if (transList != null && transList.size() > 0) {
            execReturnMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetails", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            execReturnMap.put("CASH_TRANS_LIST", cashList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            //logTO.setData(objTO.toString());
            //  logTO.setPrimaryKey(objTO.getKeyData());
            // logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("updateIndendTO", objTO);
            //  logDAO.addToLog(logTO);
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
            //logTO.setData(objTO.toString());
            // logTO.setPrimaryKey(objTO.getKeyData());
            // logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("deleteIndendTO", objTO);
            //   logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            PayrollDAO dao = new PayrollDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap whereMap) throws Exception {
        try {
            HashMap map = new HashMap();
            map = whereMap;
            _branchCode = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
            currDt = ServerUtil.getCurrentDate(_branchCode);
            if (map.containsKey("PRODUCT_TYPE") && map.get("PRODUCT_TYPE") != null) {
                LogTO objLogTO = new LogTO();
                objLogTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));

                if (map.get("PRODUCT_TYPE").equals("GL")) {
                    doGLTransactions(map, objLogTO);
                } else if (map.get("PRODUCT_TYPE").equals("MDS")) {
                    doMDSPayment(map);
                } else if (map.get("PRODUCT_TYPE").equals("TL") || map.get("PRODUCT_TYPE").equals("SA")) {
                    doLoanPayment(map);
                } else if (map.get("PRODUCT_TYPE").equals("TD")) {
                    doRDPayment(map);
                } else if (map.get("PRODUCT_TYPE").equals("OA") || map.get("PRODUCT_TYPE").equals("AD")) {
                    doOAPayment(map);
                } else if (map.get("PRODUCT_TYPE").equals("GL_VOUCHER")) {
                    doGLDebit(map);
                }
            } //added by anju anand 26/07/2014
            else if (map != null && map.containsKey("SCREEN_NAME") && CommonUtil.convertObjToStr(map.get("SCREEN_NAME")).equals("PF_TRANSFER")) {
                LogTO objLogTO = new LogTO();
                objLogTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                objLogTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
                objLogTO.setSelectedBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.SELECTED_BRANCH_ID)));
                objLogTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
                objLogTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
                objLogTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                HashMap acHeads = new HashMap();
                List lstacHeads = (List) sqlMap.executeQueryForList("getPFAccHead", acHeads);
                if (lstacHeads != null && lstacHeads.size() > 0) {
                    acHeads = new HashMap();
                    acHeads = (HashMap) lstacHeads.get(0);
                    doPFTransfer(map, objLogTO);
                }
                if (map.containsKey("AUTHORIZEMAP") && !CommonUtil.convertObjToStr(map.get("AUTHORIZEMAP")).equals("")) {
                    authorization(map);
                }
            }
            map = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
        destroyObjects();
        return execReturnMap;
    }

    //added by anju anand 26/07/2014
    private void doPFTransfer(HashMap map, LogTO objLogTO) throws Exception {
        execReturnMap = new HashMap();
        try {
            TransactionTO transactionTO = new TransactionTO();
            HashMap acHeads = new HashMap();
            List lstacHeads = (List) sqlMap.executeQueryForList("getPFAccHead", acHeads);
            if (lstacHeads != null && lstacHeads.size() > 0) {
                acHeads = new HashMap();
                acHeads = (HashMap) lstacHeads.get(0);
                HashMap notdeleted = null;
                PFTransferTO pftransferto = (PFTransferTO) map.get("pftransfer");
                if (map.containsKey("TransactionTO") && map.get("TransactionTO") != null) {
                    notdeleted = (HashMap) map.get("TransactionTO");
                    transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    if (notdeleted.containsKey("NOT_DELETED_TRANS_TOs") && notdeleted.get("NOT_DELETED_TRANS_TOs") != null) {
                        notdeleted = (HashMap) notdeleted.get("NOT_DELETED_TRANS_TOs");
                        if (notdeleted.containsKey("1")) {
                            tto = new TransactionTO();
                            tto = (TransactionTO) notdeleted.get("1");
                        }
                        if (tto.getTransType().equals(CommonConstants.TX_CASH)) {
                            transactionTO.setTransType(CommonConstants.TX_CASH);
                            if (pftransferto.getTransType().equals(CommonConstants.DEBIT)) {
                                for (int J = 1; J <= notdeleted.size(); J++) {
                                    tto = (TransactionTO) notdeleted.get(CommonUtil.convertObjToStr(J));
                                    if (CommonUtil.convertObjToDouble(tto.getTransAmt()) > 0) {
                                        doCashPayment(tto, objLogTO, pftransferto.getPfNo(), acHeads, pftransferto.getEmpId());
                                        tto.setBatchId(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
                                        tto.setTransId(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
                                        tto.setBranchId(_branchCode);
                                    }
                                }
                            } else if (pftransferto.getTransType().equals(CommonConstants.CREDIT)) {
                                for (int J = 1; J <= notdeleted.size(); J++) {
                                    tto = (TransactionTO) notdeleted.get(CommonUtil.convertObjToStr(J));
                                    if (CommonUtil.convertObjToDouble(tto.getTransAmt()) > 0) {
                                        doCashReceipt(tto, objLogTO, pftransferto.getPfNo(), acHeads, pftransferto.getEmpId());
                                        tto.setBatchId(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
                                        tto.setTransId(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
                                        tto.setBranchId(_branchCode);
                                    }
                                }
                            }
                            HashMap newMap = new HashMap();
                            newMap.put("PF_ID", pftransferto.getPfId());
                            newMap.put("PF_NO", pftransferto.getPfNo());
                            newMap.put("TRAN_DT", currDt);
                            newMap.put("TRANS_ID", tto.getTransId());
                            newMap.put("BATCH_ID", tto.getBatchId());
                            newMap.put("TRANS_MODE", CommonConstants.TX_CASH);
                            newMap.put("AMOUNT", pftransferto.getAmount());
                            newMap.put("STATUS", "CREATED");
                            newMap.put("REMARK", "");
                            newMap.put("PROD_TYPE", tto.getProductType());
                            newMap.put("CREATED_BY", objLogTO.getUserId());
                            newMap.put("CREATED_DATE", currDt);
                            newMap.put("AUTHORIZED_BY", "");
                            newMap.put("AUTHORIZE_STATUS", "");
                            newMap.put("BRANCHID", objLogTO.getBranchId());
                            newMap.put("PF_TRANS_TYPE", "PF");
                            newMap.put("TRANS_TYPE", pftransferto.getTransType());
                            sqlMap.executeUpdate("InsertPfTransTo", newMap);
                        } else {
                            transactionTO.setTransType(CommonConstants.TX_TRANSFER);
                            if (pftransferto.getTransType().equals(CommonConstants.DEBIT)) {
                                doTransferPayment(map, objLogTO, acHeads);
                            } else if (pftransferto.getTransType().equals(CommonConstants.CREDIT)) {
                                doTransferReceipt(map, objLogTO, acHeads);
                            }
                            HashMap newMap = new HashMap();
                            newMap.put("PF_ID", pftransferto.getPfId());
                            newMap.put("PF_NO", pftransferto.getPfNo());
                            newMap.put("TRAN_DT", currDt);
                            newMap.put("TRANS_ID", tto.getTransId());
                            newMap.put("BATCH_ID", tto.getBatchId());
                            newMap.put("TRANS_MODE", CommonConstants.TX_TRANSFER);
                            newMap.put("AMOUNT", pftransferto.getAmount());
                            newMap.put("STATUS", "CREATED");
                            newMap.put("REMARK", "");
                            newMap.put("PROD_TYPE", tto.getProductType());
                            newMap.put("CREATED_BY", objLogTO.getUserId());
                            newMap.put("CREATED_DATE", currDt);
                            newMap.put("AUTHORIZED_BY", "");
                            newMap.put("AUTHORIZE_STATUS", "");
                            newMap.put("BRANCHID", objLogTO.getBranchId());
                            newMap.put("PF_TRANS_TYPE", "PF");
                            newMap.put("TRANS_TYPE", pftransferto.getTransType());
                            newMap.put("PROD_ID", tto.getProductId());
                            sqlMap.executeUpdate("InsertPfTransTo", newMap);
                        }
                        TransactionTO objTransactionTO = null;
                        TransferTrans objTransferTrans = new TransferTrans();
                        objTransferTrans.setInitiatedBranch(_branchCode);
                        objTransferTrans.setLinkBatchId(pftransferto.getEmpId());
                        for (int J = 1; J <= notdeleted.size(); J++) {
                            objTransactionTO = (TransactionTO) notdeleted.get(String.valueOf(J));
                            objTransactionTO.setStatus(CommonConstants.STATUS_CREATED);
                            objTransactionTO.setBatchId(tto.getBatchId());
                            objTransactionTO.setBatchDt(currDt);
                            objTransactionTO.setTransId(objTransferTrans.getLinkBatchId());
                            objTransactionTO.setBranchId(_branchCode);
                            sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);
                        }
                    }
                }
                getTransDetails(pftransferto.getEmpId());
            }
        } catch (Exception e) {
            execReturnMap = null;
            sqlMap.rollbackTransaction();
            throw e;
        }
    }

    //added by anju anand 26/07/2014
    private void doTransferPayment(HashMap map, LogTO objLogTO, HashMap acHeads) throws Exception {
        try {
            LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
            TransferTrans objTransferTrans = new TransferTrans();
            if (TransactionDetailsMap.size() > 0) {
                if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    ArrayList transferList = new ArrayList();
                    LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                    HashMap txMap = new HashMap();
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.PARTICULARS, "PF");
                    txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(acHeads.get("PAY_ACC_NO")));
                    txMap.put(TransferTrans.DR_PROD_TYPE, "GL");
                    txMap.put("TRANS_MOD_TYPE", CommonConstants.GL_TRANSMODE_TYPE);
                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                    txMap.put(CommonConstants.USER_ID, objLogTO.getUserId());
                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, CommonUtil.convertObjToDouble(tto.getTransAmt())));
                    for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                        tto = (TransactionTO) allowedTransDetailsTO.get(CommonUtil.convertObjToStr(J));
                        payMode = CommonUtil.convertObjToStr(tto.getTransType());
                        txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(tto.getProductType()));
                        txMap.put("TRANS_MOD_TYPE", CommonUtil.convertObjToStr(tto.getProductType()));
                        txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(tto.getDebitAcctNo()));
                        txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(tto.getProductId()));
                        if (CommonUtil.convertObjToStr(tto.getProductType()).equals("GL")) {
                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(tto.getDebitAcctNo()));
                        }
                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, CommonUtil.convertObjToDouble(tto.getTransAmt())));
                        doTransferTrans(map, transferList, _branchCode);
                        tto.setBatchDt(currDt);
                        tto.setBatchId(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
                        tto.setTransId(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
                        tto.setBranchId(_branchCode);
                    }
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    //added by anju anand 26/07/2014
    private void doTransferReceipt(HashMap map, LogTO objLogTO, HashMap acHeads) throws Exception {
        try {
            LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
            TransferTrans objTransferTrans = new TransferTrans();
            if (TransactionDetailsMap.size() > 0) {
                if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    ArrayList transferList = new ArrayList();
                    LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                    HashMap txMap = new HashMap();
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.PARTICULARS, "PF");
                    txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                    txMap.put(CommonConstants.USER_ID, objLogTO.getUserId());
                    txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(tto.getProductType()));
                    txMap.put("TRANS_MOD_TYPE", CommonUtil.convertObjToStr(tto.getProductType()));
                    txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(tto.getDebitAcctNo()));
                    txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(tto.getProductId()));
                    if (CommonUtil.convertObjToStr(tto.getProductType()).equals("GL")) {
                        txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(tto.getDebitAcctNo()));
                    }
                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, CommonUtil.convertObjToDouble(tto.getTransAmt()).doubleValue()));
                    for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                        tto = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                        payMode = CommonUtil.convertObjToStr(tto.getTransType());
                        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(acHeads.get("PAY_ACC_NO")));
                        txMap.put(TransferTrans.CR_PROD_TYPE, "GL");
                        txMap.put("TRANS_MOD_TYPE", CommonConstants.GL_TRANSMODE_TYPE);
                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, CommonUtil.convertObjToDouble(tto.getTransAmt()).doubleValue()));
                        doTransferTrans(map, transferList, _branchCode);
                        tto.setBatchDt(currDt);
                        tto.setBatchId(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
                        tto.setTransId(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
                        tto.setBranchId(_branchCode);
                    }
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    //added by anju anand 26/07/2014
    private void doTransferTrans(HashMap map, ArrayList batchList, String branchCode) throws Exception {
        try {
            PFTransferTO pftransferto = (PFTransferTO) map.get("pftransfer");
            TransferDAO transferDAO = new TransferDAO();
            HashMap data = new HashMap();
            data.put("TxTransferTO", batchList);
            data.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
            data.put("INITIATED_BRANCH", _branchCode);
            data.put("LINK_BATCH_ID", pftransferto.getEmpId());
            data.put(CommonConstants.BRANCH_ID, branchCode);
            data.put(CommonConstants.USER_ID, userID);
            data.put(CommonConstants.MODULE, "Transaction");
            data.put(CommonConstants.SCREEN, "");
            data.put("MODE", "MODE");
            loanDataMap = transferDAO.execute(data, false);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    //added by anju anand 26/07/2014
    private void doCashPayment(TransactionTO objTxTransferTO, LogTO objLogTO, String pfNo, HashMap acHeads, String empId) throws Exception {
        try {
            HashMap cashMap;
            CashTransactionDAO cashDao;
            CashTransactionTO cashTO;
            cashDao = new CashTransactionDAO();
            cashTO = new CashTransactionTO();
            cashMap = new HashMap();
            cashMap.put(CommonConstants.PRODUCT_TYPE, TransactionFactory.GL);
            cashMap.put(CommonConstants.USER_ID, objLogTO.getUserId());
            cashMap.put(CommonConstants.BRANCH_ID, objLogTO.getBranchId());
            cashMap.put(CommonConstants.IP_ADDR, objLogTO.getIpAddr());
            cashMap.put(CommonConstants.MODULE, objLogTO.getModule());
            cashMap.put(CommonConstants.SCREEN, objLogTO.getScreen());
            cashTO.setAmount(objTxTransferTO.getTransAmt());
            cashTO.setAcHdId(CommonUtil.convertObjToStr(acHeads.get("PAY_ACC_NO")));
            // cashTO.setAcHdId(CommonUtil.convertObjToStr(acHeads.get("CONTRA_DB_ACC_NO")));
            cashTO.setProdType(TransactionFactory.GL);
            cashTO.setInstType(objTxTransferTO.getInstType());
            cashTO.setInstrumentNo1(objTxTransferTO.getChequeNo());
            cashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            cashTO.setInstDt(objTxTransferTO.getChequeDt());
            cashTO.setParticulars("PF");
            cashTO.setStatus(objTxTransferTO.getStatus());
            cashTO.setStatusBy(objLogTO.getUserId());
            cashTO.setTransDt(currDt);
            cashTO.setTransId(objTxTransferTO.getTransId());
            cashTO.setTransType(CommonConstants.DEBIT);
            cashTO.setBranchId(_branchCode);
            cashTO.setInitChannType(CommonConstants.CASHIER);
            cashTO.setInitTransId("CASHIER");
            cashTO.setInpAmount(objTxTransferTO.getTransAmt());
            cashTO.setInpCurr("INR");
            cashTO.setLinkBatchId(empId);
            cashTO.setInitiatedBranch(_branchCode);
            cashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
            cashTO.setTransModType(TransactionFactory.GL);
            cashMap.put("CashTransactionTO", cashTO);
            loanDataMap = cashDao.execute(cashMap, false);
            cashTO = null;
            cashDao = null;
            cashMap = null;
            objTxTransferTO = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    //added by anju anand 26/07/2014
    private void doCashReceipt(TransactionTO objTxTransferTO, LogTO objLogTO, String pfNo, HashMap acHeads, String empId) throws Exception {
        try {
            HashMap cashMap;
            CashTransactionDAO cashDao;
            CashTransactionTO cashTO;
            cashDao = new CashTransactionDAO();
            cashTO = new CashTransactionTO();
            cashMap = new HashMap();
            cashMap.put(CommonConstants.PRODUCT_TYPE, TransactionFactory.GL);
            cashMap.put(CommonConstants.USER_ID, objLogTO.getUserId());
            cashMap.put(CommonConstants.BRANCH_ID, objLogTO.getBranchId());
            cashMap.put(CommonConstants.IP_ADDR, objLogTO.getIpAddr());
            cashMap.put(CommonConstants.MODULE, objLogTO.getModule());
            cashMap.put(CommonConstants.SCREEN, objLogTO.getScreen());
            cashTO.setAmount(objTxTransferTO.getTransAmt());
            cashTO.setAcHdId(CommonUtil.convertObjToStr(acHeads.get("PAY_ACC_NO")));
            cashTO.setProdType(TransactionFactory.GL);
            cashTO.setInstType(objTxTransferTO.getInstType());
            cashTO.setInstrumentNo1(objTxTransferTO.getChequeNo());
            cashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            cashTO.setInstDt(objTxTransferTO.getChequeDt());
            cashTO.setParticulars("PF");
            cashTO.setStatus(objTxTransferTO.getStatus());
            cashTO.setStatusBy(objLogTO.getUserId());
            cashTO.setTransDt(currDt);
            cashTO.setTransId(objTxTransferTO.getTransId());
            cashTO.setTransType(CommonConstants.CREDIT);
            cashTO.setBranchId(_branchCode);
            cashTO.setInitChannType(CommonConstants.CASHIER);
            cashTO.setInitTransId("CASHIER");
            cashTO.setInpAmount(objTxTransferTO.getTransAmt());
            cashTO.setInpCurr("INR");
            cashTO.setLinkBatchId(empId);
            cashTO.setInitiatedBranch(_branchCode);
            cashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
            cashTO.setTransModType(TransactionFactory.GL);
            cashMap.put("CashTransactionTO", cashTO);
            loanDataMap = cashDao.execute(cashMap, false);
            cashTO = null;
            cashDao = null;
            cashMap = null;
            objTxTransferTO = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    
    private void doGLDebitTrans(HashMap map) throws Exception {
        try {

            System.out.println("###### insertTransactionData : " + map);
            ArrayList transferList = new ArrayList();
            TransferTrans transferTrans = new TransferTrans();
            TxTransferTO transferTo = new TxTransferTO();
            ArrayList TxTransferTO = new ArrayList();
            TransactionTO transactionTO = new TransactionTO();
            HashMap txMap = new HashMap();
            HashMap transactionListMap = new HashMap();
            //String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
            // double totalAmt = CommonUtil.convertObjToDouble(map.get("TOTAL_AMOUNT")).doubleValue();
            transferTrans.setInitiatedBranch(_branchCode);
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            transactionDAO.setInitiatedBranch(_branchCode);
            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
            transactionDAO.setTransactionType(transactionDAO.TRANSFER);

            HashMap transMap = new HashMap();
            TransactionTO objTransactionTO = null;
            objTransactionTO = (TransactionTO) map.get("TransactionTO");
            System.out.println("TOOOOO" + map.get("TransactionTO"));
            // HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
            // System.out.println("transactionDetailsMap>>>>>>>>>"+transactionDetailsMap);
            double debitAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
            // transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));

//                    if (transactionTO.getTransType().equals("TRANSFER")){
            if (debitAmt > 0.0) {
                txMap = new HashMap();
                //Changed By Suresh


                txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));

                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);

                txMap.put(TransferTrans.DR_BRANCH, _branchCode);

                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");

                txMap.put(TransferTrans.DR_INSTRUMENT_1, "PAYROLL");
                txMap.put(TransferTrans.DR_INSTRUMENT_2, "PAYMENT_VOUCHER");




                if (map.get("PAY_DESCRIPTION") != null) {
                    txMap.put(TransferTrans.NARRATION, map.get("PAY_DESCRIPTION"));
                } else {

                    txMap.put(TransferTrans.NARRATION, "PAYROLLGL Transactions");
                }

                if (map.get("EMPLOYEE_NAME") != null) {
                    txMap.put(TransferTrans.PARTICULARS, map.get("EMPLOYEE_NAME"));
                } else {
                    txMap.put(TransferTrans.PARTICULARS, "");
                }
                txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                txMap.put(TransferTrans.DR_BRANCH, _branchCode);


                transferTo = transactionDAO.addTransferDebitLocal(txMap, debitAmt);

//                        }
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setInitiatedBranch(_branchCode);
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                //                            if(!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")){
                // transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(prizedMoneyPaymentTO.getChittalNo()));
                //                            }
                TxTransferTO.add(transferTo);
            }

            TransferDAO transferDAO = new TransferDAO();

            System.out.println("dddd");
            HashMap applnMap = new HashMap();
            transferDAO = new TransferDAO();
            map.put("MODE", map.get("COMMAND"));
            map.put("COMMAND", map.get("MODE"));
            map.put("TxTransferTO", TxTransferTO);
            System.out.println("reckjnjfgnfrtdd hererer" + map);


//                         HashMap authorizeMap = new HashMap();
//                          authorizeMap.put("BATCH_ID", null);
//                          authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
//                          map.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
////                        
            HashMap authorizeMap = new HashMap();
            authorizeMap.put("BATCH_ID", null);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
            map.put(CommonConstants.AUTHORIZEMAP, authorizeMap);

            transMap = transferDAO.execute(map, false);
            // prizedMoneyPaymentTO.setTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
            System.out.println("transactionDAO map : " + map);
            transactionDAO.setBatchId("");
            transactionDAO.setBatchDate(currDt);
            System.out.println("SuccessFull Posting of PaymentVoucher Done !!!");
            // transactionDAO.execute(map);


            //  }



            //  }
            // }

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

    private void doGLDebit(HashMap map) throws Exception {
        try {
            TransactionTO objTransactionTO = null;
            objTransactionTO = (TransactionTO) map.get("TransactionTO");
            System.out.println("TOOOOO" + map.get("TransactionTO"));
            double debitAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
            ArrayList cashList = new ArrayList();
            //  sqlMap.startTransaction();


            CashTransactionTO objCashTO = new CashTransactionTO();
            // if(dataMap.get("STATUS").equals(CommonConstants.TOSTATUS_INSERT)){
            objCashTO.setAcHdId(CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
            objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.GL));
            objCashTO.setTransType(CommonConstants.DEBIT);
            objCashTO.setParticulars("PAYROLL PAYMNET VOUCHER");
            //objCashTO.setAuthorizeRemarks(CommonUtil.convertObjToStr(dataMap.get("AUTHORIZEREMARKS")));
            objCashTO.setInpAmount(debitAmt);
            objCashTO.setAmount(debitAmt);
            objCashTO.setInitTransId("" + map.get(CommonConstants.USER_ID));
            objCashTO.setBranchId(_branchCode);
            objCashTO.setStatusBy("" + map.get(CommonConstants.USER_ID));
            userId = "" + map.get(CommonConstants.USER_ID);
            objCashTO.setStatusDt(currDt);
            objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            objCashTO.setAuthorizeRemarks("PAYROLL PAYMENT VOUCHER");
            objCashTO.setInitiatedBranch(_branchCode);
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            objCashTO.setCommand("INSERT");
            objCashTO.setInstrumentNo1("PAYROLL");
            objCashTO.setInstrumentNo2("PAYMENT_VOUCHER");

            if (map.get("PAY_DESCRIPTION") != null) {
                objCashTO.setNarration(CommonUtil.convertObjToStr(map.get("PAY_DESCRIPTION")));

            } else {

                objCashTO.setNarration("PAYROLLGL Transactions");
            }



            objCashTO.setInstType("WITHDRAW_SLIP");
            objCashTO.setInstDt(currDt);
            objCashTO.setLinkBatchId("");
            System.out.println("objCashTO 1st one:" + objCashTO);
            cashList.add(objCashTO);
            if (cashList != null && cashList.size() > 0) {
                doCashTransfer(cashList, _branchCode, false);

            }


            // sqlMap.commitTransaction();        



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

    private void doCashTransfer(ArrayList batchList, String branchCode, boolean isAutoAuthorize) throws Exception {
        CashTransactionDAO cashDAO = new CashTransactionDAO();
        HashMap data = new HashMap();
        data.put("DAILYDEPOSITTRANSTO", batchList);
        data.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
        data.put("INITIATED_BRANCH", branchCode);
        data.put(CommonConstants.BRANCH_ID, branchCode);
        data.put(CommonConstants.USER_ID, userId);
        data.put(CommonConstants.MODULE, "Transaction");
        data.put(CommonConstants.SCREEN, "");
        data.put("MODE", "MODE");
        data.put("SCREEN_NAME", "PAYROLL");
        if (isAutoAuthorize) {
            HashMap authorizeMap = new HashMap();
            authorizeMap.put("BATCH_ID", null);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
            data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
            //  data.put(CommonConstants.AUTHORIZEDATA, batchList);
        }
        System.out.println("data" + data);
        //  cashDAO.

        HashMap cashTransMap = cashDAO.execute(data, true);
        // cashDAO.execute(data, isAutoAuthorize)
        //   prizedMoneyPaymentTO.setTransId(CommonUtil.convertObjToStr(cashTransMap.get("TRANS_ID")));
        System.out.println("########### cashTransMap" + cashTransMap);
        //currDt = currDt.clone();


        HashMap map = new HashMap();

        String status = CommonConstants.STATUS_AUTHORIZED;
        CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
        ArrayList arrList = new ArrayList();
        HashMap singleAuthorizeMap = new HashMap();
        singleAuthorizeMap.put("STATUS", status);
        singleAuthorizeMap.put("TRANS_ID", cashTransMap.get("TRANS_ID"));
        singleAuthorizeMap.put("USER_ID", map.get("USER_ID"));
        arrList.add(singleAuthorizeMap);
        //String branchCode = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        // String userId = userId;
        System.out.println("before making new DAO map :" + map);
        map = new HashMap();
        map.put("SCREEN", "Cash");
        map.put("USER_ID", userId);
        map.put("SELECTED_BRANCH_ID", branchCode);
        map.put("BRANCH_CODE", branchCode);
        map.put("MODULE", "Transaction");
        map.put("SCREEN_NAME", "PAYROLL");
        HashMap dataMap = new HashMap();
        dataMap.put(CommonConstants.AUTHORIZESTATUS, status);
        dataMap.put(CommonConstants.AUTHORIZEDATA, arrList);
        dataMap.put("DAILY", "DAILY");
        map.put(CommonConstants.AUTHORIZEMAP, dataMap);
        System.out.println("before entering DAO map :" + map);
        cashTransactionDAO.execute(map, false);

    }

    private void authorize(HashMap map) throws Exception {
        String status = (String) map.get("STATUS");
        String linkBatchId = null;
        String disbursalNo = null;
        HashMap cashAuthMap;
        TransactionTO objTransactionTO = null;
        try {
            sqlMap.startTransaction();
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            System.out.println("map:" + map);
            disbursalNo = CommonUtil.convertObjToStr(map.get("IRNO"));
            map.put(CommonConstants.STATUS, status);
            map.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            map.put("CURR_DATE", currDt);
            System.out.println("status------------>" + status);
            sqlMap.executeUpdate("authorizeIndendDisbursal", map);
            linkBatchId = CommonUtil.convertObjToStr(map.get("IRNO"));//Transaction Batch Id
            //Separation of Authorization for Cash and Transfer
            //Call this in all places that need Authorization for Transaction
            cashAuthMap = new HashMap();
            System.out.println("@#$@zvbvxcvzx#$map:" + map);
            cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
            cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            cashAuthMap.put("DAILY", "DAILY");
            System.out.println("map:" + map);
            System.out.println("cashAuthMap:" + cashAuthMap);
            System.out.println("#$%#$%#$%xcvlinkBatchId" + linkBatchId);
            TransactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
            HashMap transMap = new HashMap();
            transMap.put("LINK_BATCH_ID", linkBatchId);
            System.out.println("transMap----------------->" + transMap);
            sqlMap.executeUpdate("updateInstrumentNO1Transfer", transMap);
            sqlMap.executeUpdate("updateInstrumentNO1Cash", transMap);
            transMap = null;
            System.out.println("disbursalNo----------------->" + disbursalNo);
            objTransactionTO = new TransactionTO();
            objTransactionTO.setBatchId(CommonUtil.convertObjToStr(disbursalNo));
            objTransactionTO.setTransId(CommonUtil.convertObjToStr(linkBatchId));
            objTransactionTO.setBranchId(_branchCode);
            System.out.println("objTransactionTO----------------->" + objTransactionTO);
            sqlMap.executeUpdate("deleteRemittanceIssueTransactionTO", objTransactionTO);
            if (!status.equals("REJECTED")) {
            }
            map = null;
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }

    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
    }
    
    //added by anju anand 26/07/2014
    private void authorization(HashMap map) throws Exception {
        System.out.println("map in authorize: "+map);
        try {
            String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
            HashMap authMap = new HashMap();
            authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            ArrayList selectedList = (ArrayList) authMap.get(CommonConstants.AUTHORIZEDATA);
            HashMap AuthorizeMap = new HashMap();
            AuthorizeMap = (HashMap) selectedList.get(0);
            String status = CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS));
            String linkBatchId = CommonUtil.convertObjToStr(AuthorizeMap.get("EMP_ID"));
            HashMap cashAuthMap = new HashMap();
            cashAuthMap.put(CommonConstants.BRANCH_ID, BRANCH_ID);
            cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
            transactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
            sqlMap.executeUpdate("updatePFTransTo", AuthorizeMap);
            if (AuthorizeMap != null && AuthorizeMap.containsKey("TRANS_MODE") && AuthorizeMap.get("TRANS_MODE").equals(CommonConstants.TX_CASH)) {
                HashMap whereMap = new HashMap();
                whereMap.put("AMOUNT", AuthorizeMap.get("AMOUNT"));
                whereMap.put("TRANS_TYPE", AuthorizeMap.get("TRANS_TYPE"));
                whereMap.put("PF_TRANS_TYPE", AuthorizeMap.get("PF_TRANS_TYPE"));
                whereMap.put("LINK_BATCH_ID", AuthorizeMap.get("PF_NO"));
                whereMap.put("BALANCE", AuthorizeMap.get("BALANCE"));
                sqlMap.executeUpdate("updateprnbalcash", whereMap);
            } else if (AuthorizeMap != null && AuthorizeMap.containsKey("TRANS_MODE") && AuthorizeMap.get("TRANS_MODE").equals(CommonConstants.TX_TRANSFER)) {
                HashMap whereMap = new HashMap();
                whereMap.put("AMOUNT", AuthorizeMap.get("AMOUNT"));
                whereMap.put("TRANS_TYPE", AuthorizeMap.get("TRANS_TYPE"));
                whereMap.put("PF_TRANS_TYPE", AuthorizeMap.get("PF_TRANS_TYPE"));
                whereMap.put("LINK_BATCH_ID", AuthorizeMap.get("PF_NO"));
                whereMap.put("BALANCE", AuthorizeMap.get("BALANCE"));
                sqlMap.executeUpdate("updateprnbaltransfer", whereMap);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
       
    }
}
