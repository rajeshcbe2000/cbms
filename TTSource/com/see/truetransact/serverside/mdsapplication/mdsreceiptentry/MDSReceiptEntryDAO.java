/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSReceiptEntryDAO.java
 *
 * Created on June 18, 2003, 4:14 PM
 */
package com.see.truetransact.serverside.mdsapplication.mdsreceiptentry;

import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.*;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.transferobject.mdsapplication.mdsreceiptentry.MDSReceiptEntryTO;
// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.common.sms.SmsConfigDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.mdsapplication.mdsmastermaintenance.MDSDepositTypeTO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.ListIterator;

/**
 * This is used for MDSReceiptEntryDAO Data Access.
 *
 * @author Sathiya
 *
 */
public class MDSReceiptEntryDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private TransactionDAO transactionDAO = null;
    private MDSReceiptEntryTO mdsReceiptEntryTO = null;
    TransferDAO transferDAO = new TransferDAO();
    private Date currDt = null;
    private Map returnMap = null;
    private double bankAdvInsAmt = 0.0;
    private double futureAdvbonusAmt = 0.0;
    private double instalmentPayAmt = 0.0;
    private double bankBonusAmt = 0.0, bonusFirst = 0.0;
    private double bankBonusReverseAmt = 0.0;
    private Iterator addressIterator1;
    private Iterator addressIterator2;
    private Iterator bonusIterator;
    private boolean penalTransHappened = false;
    private boolean waiveTransHappened = false;
    private boolean bonusreversal = false;
    private HashMap transIdMap = null;
    private String generateSingleTransId = "";
    private ArrayList TxTransferTO = new ArrayList();
    private int ibrHierarchy = 0;
    private String isSplitMDSTransaction = "";

    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public MDSReceiptEntryDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private void clearAllFileds() {//Added By Nidhin 
        bankAdvInsAmt = 0.0;
        futureAdvbonusAmt = 0.0;
        instalmentPayAmt = 0.0;
        bankBonusAmt = 0.0;
        bonusFirst = 0.0;
        bankBonusReverseAmt = 0.0;
        generateSingleTransId = "";
        isSplitMDSTransaction = "";
        currDt = ServerUtil.getCurrentDate(_branchCode);
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("#######getDataMap :" + map);
        isSplitMDSTransaction = "";
        if (map != null && map.containsKey("isSplitMDSTransaction") && map.get("isSplitMDSTransaction") != null) {
            isSplitMDSTransaction = CommonUtil.convertObjToStr(map.get("isSplitMDSTransaction"));
        }
        HashMap getReturnMap = new HashMap();
        String where = CommonUtil.convertObjToStr(map.get("NET_TRANS_ID"));
        HashMap editMap = new HashMap();
        editMap.put("TRANS_ID", CommonUtil.convertObjToStr(map.get("NET_TRANS_ID")));
        editMap.put("TRANS_DT", setProperDtFormat(currDt));
        editMap.put("INITIATED_BRANCH", _branchCode);
        List list = (List) sqlMap.executeQueryForList("getMDSReceiptEntryTO", editMap);
        if(list == null || list.size()<=0){
            editMap.put("SINGLE_TRANS_ID", CommonUtil.convertObjToStr(map.get("SINGLE_TRANS_ID")));
             list = (List) sqlMap.executeQueryForList("getMDSReceiptEntryTOForAdv", editMap);
        }
        getReturnMap.put("MDSReceiptEntryTO", list);
        mdsReceiptEntryTO = new MDSReceiptEntryTO();
        if (map.containsKey("VOUCHER_DETAILS")) {
//            penalTransHappened=false;
            returnMap = new HashMap();
            mdsReceiptEntryTO = (MDSReceiptEntryTO) ((List) getReturnMap.get("MDSReceiptEntryTO")).get(0);
            String netTransID = CommonUtil.convertObjToStr(map.get("NET_TRANS_ID"));
            String bonusTransID = mdsReceiptEntryTO.getBonusTransId();
            if (netTransID != null && netTransID.indexOf(",") != -1) {
                mdsReceiptEntryTO.setNetTransId(netTransID.substring(0, netTransID.indexOf(",")));
                if (bonusTransID != null && bonusTransID.indexOf(",") != -1) {
                    mdsReceiptEntryTO.setBonusTransId(bonusTransID.substring(0, bonusTransID.indexOf(",")));
                }
                setTransDetails(mdsReceiptEntryTO);
                mdsReceiptEntryTO.setNetTransId(netTransID.substring(netTransID.indexOf(",") + 1, netTransID.length()));
                if (bonusTransID != null && bonusTransID.indexOf(",") != -1) {
                    mdsReceiptEntryTO.setBonusTransId(bonusTransID.substring(bonusTransID.indexOf(",") + 1, bonusTransID.length()));
                }
            }
            setTransDetails(mdsReceiptEntryTO);
            getReturnMap.put("VOUCHER_DATA", returnMap);
            mdsReceiptEntryTO.setNetTransId(netTransID);
            mdsReceiptEntryTO.setBonusTransId(bonusTransID);
        }

        List lst = (List) sqlMap.executeQueryForList("getSelectParticularTransList", editMap);
        HashMap transactionMap = new HashMap();
        if (lst != null && lst.size() > 0) {
            transactionMap = (HashMap) lst.get(0);
        }
        HashMap whereMap = new HashMap();
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        //        String transId = CommonUtil.convertObjToStr(map.get("NET_TRANS_ID"));
        //        whereMap.put(CommonConstants.MAP_WHERE, transId) ;
        //        list = transactionDAO.getData(whereMap);
        HashMap totalMap = new HashMap();
        String transId = CommonUtil.convertObjToStr(map.get("NET_TRANS_ID"));
        int dot = transId.indexOf(",");
        System.out.println("transId.indexOf()+1"+transId.indexOf(",")+1);
        System.out.println(" transId.length() "+ transId.length());
        System.out.println(" transId.length() dot "+dot);
        if (dot >= 0) {
          //  transId = transId.substring(0, dot);
            transId= transId.substring(transId.indexOf(",")+1, transId.length());
        }
        System.out.println("###### transID : " + transId);
        whereMap.put(CommonConstants.TRANS_ID, transId);
        whereMap.put(CommonConstants.BRANCH_ID, _branchCode);
        whereMap.put("TRANS_DT", currDt);
        totalMap.put(CommonConstants.MAP_WHERE, whereMap);
        list = transactionDAO.getData(totalMap);
        getReturnMap.put("TransactionTO", list);

        HashMap editRemitMap = new HashMap();
//        HashMap editMap = new HashMap();
//        editMap.put("TRANS_ID",CommonUtil.convertObjToStr(map.get("NET_TRANS_ID")));
//        editMap.put("TRANS_DT",currDt.clone());
//        editMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(_branchCode));
//        editRemitMap.put("TRANS_ID",CommonUtil.convertObjToStr(map.get("NET_TRANS_ID")));
//        List lstRemit = sqlMap.executeQueryForList("getSelectRemitTransactionAmt",editMap);
//        if(lstRemit!=null && lstRemit.size()>0){
//            editRemitMap = (HashMap)lstRemit.get(0);
//        }
        //        List lstEdit = sqlMap.executeQueryForList("getSelectApplnAmt",editMap);
        //        if(lstEdit!=null && lstEdit.size()>0){
        //            editMap = (HashMap)lstEdit.get(0);
        //            double amount = CommonUtil.convertObjToDouble(editMap.get("INST_AMT")).doubleValue();
        HashMap getTransMap = new HashMap();
//        String debitAcNo = CommonUtil.convertObjToStr(editRemitMap.get("DEBIT_ACCT_NO"));
//        String prodType = CommonUtil.convertObjToStr(editRemitMap.get("PRODUCT_TYPE"));
//        String prodId = CommonUtil.convertObjToStr(editRemitMap.get("PROD_ID"));
        getTransMap.put("TODAY_DT", setProperDtFormat(currDt));
        getTransMap.put("INITIATED_BRANCH", _branchCode);

        if (transactionMap != null && transactionMap.get("NET_TRANS_ID") != null && !transactionMap.get("NET_TRANS_ID").equals("")) {
            getTransMap.put("LINK_BATCH_ID", transactionMap.get("NET_TRANS_ID"));
            lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
            if (lst != null && lst.size() > 0) {
                getReturnMap.put("NET_TRANSACTION_TRANSFER", lst.get(0));
//                System.out.println("NET_TRANSACTION_TRANSFER :"+getReturnMap);
            }
            lst = (List) sqlMap.executeQueryForList("getCashTransBatchID", getTransMap);
            if (lst != null && lst.size() > 0) {
                getReturnMap.put("NET_TRANSACTION_CASH", lst.get(0));
//                System.out.println("NET_TRANSACTION_CASH :"+getReturnMap);
            }
        }
        if (transactionMap != null && transactionMap.get("PENAL_TRANS_ID") != null && !transactionMap.get("PENAL_TRANS_ID").equals("")) {
            getTransMap.put("LINK_BATCH_ID", transactionMap.get("PENAL_TRANS_ID"));
            lst = (List) sqlMap.executeQueryForList("getCashTransBatchID", getTransMap);
            if (lst != null && lst.size() > 0) {
                getReturnMap.put("PENAL_TRANSACTION_CASH", lst.get(0));
//                System.out.println("PENAL_TRANSACTION_CASH :"+getReturnMap);
            }
        }
        if (transactionMap != null && transactionMap.get("ARBITRATION_ID") != null && !transactionMap.get("ARBITRATION_ID").equals("")) {
            getTransMap.put("LINK_BATCH_ID", transactionMap.get("ARBITRATION_ID"));
            lst = (List) sqlMap.executeQueryForList("getCashTransBatchID", getTransMap);
            if (lst != null && lst.size() > 0) {
                getReturnMap.put("ARBITRATION_TRANSACTION_CASH", lst.get(0));
//                System.out.println("ARBITRATION_TRANSACTION_CASH :"+getReturnMap);
            }
        }
        if (transactionMap != null && transactionMap.get("NOTICE_ID") != null && !transactionMap.get("NOTICE_ID").equals("")) {
            getTransMap.put("LINK_BATCH_ID", transactionMap.get("NOTICE_ID"));
            lst = (List) sqlMap.executeQueryForList("getCashTransBatchID", getTransMap);
            if (lst != null && lst.size() > 0) {
                getReturnMap.put("NOTICE_TRANSACTION_CASH", lst.get(0));
//                System.out.println("NOTICE_TRANSACTION_CASH :"+getReturnMap);
            }
        }
        if (transactionMap != null && transactionMap.get("BONUS_TRANS_ID") != null && !transactionMap.get("BONUS_TRANS_ID").equals("")) {
            getTransMap.put("LINK_BATCH_ID", transactionMap.get("BONUS_TRANS_ID"));
            lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
            if (lst != null && lst.size() > 0) {
                getReturnMap.put("BONUS_TRANSACTION_TRANSFER", lst.get(0));
//                System.out.println("BONUS_TRANSACTION_TRANSFER :"+getReturnMap);
            }
        }
        if (transactionMap != null && transactionMap.get("DISCOUNT_TRANS_ID") != null && !transactionMap.get("DISCOUNT_TRANS_ID").equals("")) {
            getTransMap.put("LINK_BATCH_ID", transactionMap.get("DISCOUNT_TRANS_ID"));
            lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
            if (lst != null && lst.size() > 0) {
                getReturnMap.put("DISCOUNT_TRANSACTION_TRANSFER", lst.get(0));
//                System.out.println("DISCOUNT_TRANSACTION_TRANSFER :"+getReturnMap);
            }
        }
        System.out.println("getReturnMap : " + getReturnMap);
        return getReturnMap;
    }

    private void insertData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            LinkedHashMap bankAdvMap = new LinkedHashMap();
            int bankAdvCnt = 0;
            int instNo = 0;
            double bonusNew = 0.0;
            bankAdvInsAmt = 0.0;
            instalmentPayAmt = 0.0;
            futureAdvbonusAmt = 0.0;
            penalTransHappened = false;
            waiveTransHappened = false;
            double penalWaiveAmt = 0.0;
            double arcWaiveAmt = 0.0;
            double noticeWaiveAmt = 0.0;
            double totalWaiveAmt = 0.0;
            HashMap waiveMap = null;
            TxTransferTO = new ArrayList();
            HashMap splitTransMap = new HashMap();
            instNo = CommonUtil.convertObjToInt(mdsReceiptEntryTO.getCurrInst()) - CommonUtil.convertObjToInt(mdsReceiptEntryTO.getPendingInst());
            bankAdvMap.put("SCHEME_NAME", mdsReceiptEntryTO.getSchemeName());
            bankAdvMap.put("CHITTAL_NO", mdsReceiptEntryTO.getChittalNo());
            bankAdvMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getSubNo()));
            bankAdvMap.put("INSTALLMENT_NO", instNo);
            MDSReceiptEntryTO mdsBankAdvReceiptEntryTO = null;
            MDSReceiptEntryTO mdsCurrentReceiptEntryTO = null;
            String cash_unique_id = getCashUniqueId();
            if(map.containsKey("WAIVE_MAP") && map.get("WAIVE_MAP") != null){
                waiveMap =(HashMap) map.get("WAIVE_MAP");
                penalWaiveAmt = CommonUtil.convertObjToDouble(waiveMap.get("PENAL_AMOUNT"));
                arcWaiveAmt = CommonUtil.convertObjToDouble(waiveMap.get("ARC_AMOUNT"));
                noticeWaiveAmt = CommonUtil.convertObjToDouble(waiveMap.get("NOTICE_AMOUNT"));
                totalWaiveAmt = CommonUtil.convertObjToDouble(waiveMap.get("Total_WaiveAmt"));
            }
            List bankAdvanceLst = sqlMap.executeQueryForList("getSelectBankAdvanceDetailsData", bankAdvMap);
            if (bankAdvanceLst != null && bankAdvanceLst.size() > 0) {
//                System.out.println("###### MDS BANK ADVANCE ###### : ");
                LinkedHashMap bankMap = new LinkedHashMap();
                bankAdvMap = new LinkedHashMap();
                LinkedHashMap installmentMap = new LinkedHashMap();
                if (map.containsKey("INSTALLMENT_MAP")) {
                    // System.out.println("INSTAMMLEMT MAP IN DAO BY AKhi :  "+);
                    installmentMap = (LinkedHashMap) map.get("INSTALLMENT_MAP");
                }
                if (bankAdvanceLst != null && bankAdvanceLst.size() > 0) {
                    for (int i = 0; i < bankAdvanceLst.size(); i++) {
                        bankMap = (LinkedHashMap) bankAdvanceLst.get(i);
                        bankAdvMap.put(String.valueOf(bankMap.get("INSTALLMENT_NO")), bankMap);
                    }
                }
                if (map.containsKey("BONUS_LIST_NEW") && map.get("BONUS_LIST_NEW") != null) {

                    // ArrayList bonusListNew=(ArrayList)map.get("BONUS_LIST_NEW");
                    // for(int k=0;k<bonusListNew.size();k++)
                    // {
                    bonusNew = CommonUtil.convertObjToDouble(map.get("BONUS_LIST_NEW"));
                    // }
                }
//                System.out.println("########## bankAdvMap : "+bankAdvMap);
                addressIterator1 = installmentMap.keySet().iterator();
                String key1 = "";

                bankBonusAmt = 0.0;
                bankBonusReverseAmt = 0;
                for (int i = 0; i < installmentMap.size(); i++) {
                    key1 = (String) addressIterator1.next();
                    System.out.println("###### key1 ###### : " + key1);
                    if (bankAdvMap.containsKey(key1)) {
                        HashMap installMap = new HashMap();
                        installMap = (HashMap) installmentMap.get(key1);
//                        System.out.println("########## bankAdvInsAmt : "+bankAdvInsAmt);
                        bankAdvInsAmt += CommonUtil.convertObjToDouble(((HashMap) bankAdvMap.get(key1)).get("INST_AMT")).doubleValue();
                        bankBonusAmt += CommonUtil.convertObjToDouble(((HashMap) bankAdvMap.get(key1)).get("BONUS_AMT")).doubleValue();
                        System.out.println("bankAdvInsAmt :: " + bankAdvInsAmt +" :: bankBonusAmt :: " + bankBonusAmt);
                        if (CommonUtil.convertObjToDouble(installMap.get("BONUS")).doubleValue() == 0) {
                            bankBonusReverseAmt += CommonUtil.convertObjToDouble(((HashMap) bankAdvMap.get(key1)).get("BONUS_AMT")).doubleValue();
                            System.out.println("bankBonusReverseAmt :: " + bankBonusReverseAmt);
                        }
                        //System.out.println("111=====" + i + " pending====  " + mdsReceiptEntryTO.getPendingInst() + "Noof insta=== " + mdsReceiptEntryTO.getNoOfInstPay());//bbbb
                        if (i == 1 && (mdsReceiptEntryTO.getPendingInst() + 1 == mdsReceiptEntryTO.getNoOfInstPay())) {
                            //int pay=CommonUtil.convertObjToInt(mdsReceiptEntryTO.getNoOfInstPay())
                            //        -CommonUtil.convertObjToInt(mdsReceiptEntryTO.getPendingInst());
                            bonusFirst = bonusNew;//*pay;
                        }
//                        System.out.println("bonusFirst 88=====" + bonusFirst);
//                        System.out.println("mdsReceiptEntryTO   " + mdsReceiptEntryTO);
//                        System.out.println("mdsReceiptEntryTO.getBonusAmtPayable()" + mdsReceiptEntryTO.getBonusAmtPayable());
                        //if (mdsReceiptEntryTO.getBonusAmtPayable() == 0.0) {
                        if(mdsReceiptEntryTO.getForfeitBonusAmtPayable() > 0){ // Changes the condition by nithya on 16-07-2020
                            bonusreversal = true;
                            System.out.println("flag Settinggggggggggggggggg>>>" + bonusreversal);
                        } else {
                            bonusreversal = false;
                            System.out.println("flag Settinggggggggggggggggg>>>" + bonusreversal);
                        }
                       // System.out.println("########## AFETER bankBonusAmt : " + bankBonusAmt + "   " + bankBonusAmt);
                    } else {
                        HashMap installMap = new HashMap();
                        installMap = (HashMap) installmentMap.get(key1);
                    //    System.out.println("########## installMap : " + installMap);
                        instalmentPayAmt += CommonUtil.convertObjToDouble(installMap.get("INST_AMT")).doubleValue();
                        futureAdvbonusAmt += CommonUtil.convertObjToDouble(installMap.get("BONUS")).doubleValue();
//                        System.out.println("########## futureAdvbonusAmt : " + futureAdvbonusAmt);
//                        System.out.println("########## instalmentPayAmt : " + instalmentPayAmt);
                    }
                }

                mdsBankAdvReceiptEntryTO = getMDSReceiptEntryTO(bankBonusAmt, bankAdvInsAmt);
                mdsCurrentReceiptEntryTO = (MDSReceiptEntryTO) map.get("mdsReceiptEntryTO");
                mdsBankAdvReceiptEntryTO.setBranchCode(mdsCurrentReceiptEntryTO.getBranchCode()); //added by abi
                mdsBankAdvReceiptEntryTO.setInitiatedBranch(_branchCode);
                mdsCurrentReceiptEntryTO = null;

           //     System.out.println("###### mdsBankAdvReceiptEntryTO ###### : " + mdsBankAdvReceiptEntryTO);

                insertBankAdvData(map, mdsBankAdvReceiptEntryTO, objLogDAO, objLogTO, cash_unique_id);
            }
//            System.out.println("###### bankAdvanceLst.size() ###### : " + bankAdvanceLst.size());
//            System.out.println("###### bankAdvanceLst ###### : " + bankAdvanceLst);
//            System.out.println("###### instalmentPayAmt.size() ###### : " + instalmentPayAmt);
//            System.out.println("###### futureAdvbonusAmt : " + futureAdvbonusAmt);
//            System.out.println("###### bankAdvInsAmt ###### : " + bankAdvInsAmt);
            if (instalmentPayAmt > 0 || (bankAdvanceLst == null || bankAdvanceLst.size() <= 0)) {
            //    System.out.println("###### MDS_RECEIPT_ENTRY_START ###### : ");

                if (bankAdvInsAmt > 0) {
              //      System.out.println("###### BOTH(bank_advance && receipt) MDS_RECEIPT_ENTRY_START ###### : ");
                    if (futureAdvbonusAmt <= 0) {
                        futureAdvbonusAmt = bonusFirst;
                    }
                    mdsCurrentReceiptEntryTO = getMDSReceiptEntryTO(futureAdvbonusAmt, instalmentPayAmt);
                    mdsCurrentReceiptEntryTO.setNetAmt(new Double(mdsCurrentReceiptEntryTO.getNetAmt().doubleValue()
                            + mdsCurrentReceiptEntryTO.getArbitrationAmt().doubleValue()
                            + mdsCurrentReceiptEntryTO.getNoticeAmt().doubleValue()
                            + mdsCurrentReceiptEntryTO.getPenalAmtPayable().doubleValue()));
                } else {
                 //   System.out.println("###### ONLY MDS_RECEIPT_ENTRY_START ###### : ");
                    mdsCurrentReceiptEntryTO = (MDSReceiptEntryTO) map.get("mdsReceiptEntryTO");
                }
               // System.out.println("###### mdsBankAdvReceiptEntryTO ###### : " + mdsCurrentReceiptEntryTO);                
                insertReceiptData(map, mdsCurrentReceiptEntryTO, objLogDAO, objLogTO, cash_unique_id);
            } 
            mdsReceiptEntryTO.setBankAdvanceAmt(bankAdvInsAmt-bankBonusAmt); //2093
            
            if (instalmentPayAmt == 0 && bankAdvanceLst != null && bankAdvanceLst.size() > 0) {
                String tran_type = "";
                TransactionTO transactionTO = new TransactionTO();
                if (map.containsKey("TransactionTO")) {
                    HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                    if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                        allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                    }
                    transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                    tran_type = transactionTO.getTransType();
                }
                  mdsCurrentReceiptEntryTO = (MDSReceiptEntryTO) map.get("mdsReceiptEntryTO");
                  
               // System.out.println("tran_type:::" + tran_type);
                if (tran_type != null && tran_type.equals("TRANSFER")) {
                    ArrayList templist = new ArrayList() ;
                    for(int n=0;n<TxTransferTO.size();n++){
                         TxTransferTO transferTo = (TxTransferTO)TxTransferTO.get(n);
                        // System.out.println("mdsCurrentReceiptEntryTO  VVVVVVVVV "+mdsCurrentReceiptEntryTO.getNetAmt());
                         if(transferTo.getTransType()!=null && transferTo.getTransType().equals("DEBIT")){
                             System.out.println("transferTo.setAmount(m------------------ :"+transferTo.getAmount());
                             transferTo.setInpAmount(mdsCurrentReceiptEntryTO.getNetAmt() - totalWaiveAmt);
                             transferTo.setAmount(mdsCurrentReceiptEntryTO.getNetAmt() - totalWaiveAmt);
                             transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                         }
                         templist.add (transferTo);
                    }
                   
                 //   TxTransferTO = templist;
                    
                    map.put("MODE", map.get("COMMAND"));
                    map.put("COMMAND", map.get("MODE"));
                    map.put("TxTransferTO", templist);
                    map.put("TRANS_MOD_TYPE_MMBS", CommonConstants.MMBS);
                    HashMap transMap = transferDAO.execute(map, false);
              //      System.out.println("transMap X:"+transMap);
                    mdsBankAdvReceiptEntryTO.setNetTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                    mdsBankAdvReceiptEntryTO.setPenalTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                    map.put("BANK_ADVANCE_NET_TRANS_ID", mdsBankAdvReceiptEntryTO.getNetTransId());
                    map.put("BANK_ADVANCE_BONUS_TRANS_ID", mdsBankAdvReceiptEntryTO.getBonusTransId());
                    map.put("BANK_ADVANCE_PENAL_TRANS_ID", mdsBankAdvReceiptEntryTO.getPenalTransId());
                    map.put("BANK_ADVANCE_NOTICE_TRANS_ID", mdsBankAdvReceiptEntryTO.getNoticeId());
                    map.put("BANK_ADVANCE_ARBITRATION_TRANS_ID", mdsBankAdvReceiptEntryTO.getArbitrationId());
                    map.put("BANK_ADVANCE_PENAL_WAIVE_TRANS_ID", mdsBankAdvReceiptEntryTO.getPenalWaiveId());
                    map.put("BANK_ADVANCE_NOTICE_WAIVE_TRANS_ID", mdsBankAdvReceiptEntryTO.getNoticeWaiveId());
                    map.put("BANK_ADVANCE_ARC_WAIVE_TRANS_ID", mdsBankAdvReceiptEntryTO.getArcWaiveId());
                    map.put("BANK_ADVANCE_PENAL_WAIVE_AMT", mdsBankAdvReceiptEntryTO.getPenalWaiveAmt());
                    map.put("BANK_ADVANCE_NOTICE_WAIVE_AMT", mdsBankAdvReceiptEntryTO.getNoticeWaiveAmt());
                    map.put("BANK_ADVANCE_ARC_WAIVE_AMT", mdsBankAdvReceiptEntryTO.getArcWaiveAmt());
                    System.out.println("transactionDAO map : " + map);
                    transactionDAO.setBatchId(mdsBankAdvReceiptEntryTO.getNetTransId());
                    transactionDAO.setBatchDate(currDt);
                    transactionDAO.execute(map);
                    HashMap debitMap = new HashMap();
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("OA")) {
                        debitMap.put("ACT_NUM", transactionTO.getDebitAcctNo());
                        List lst = sqlMap.executeQueryForList("getAccNoProdIdDet", debitMap);
                        if (lst != null && lst.size() > 0) {
                            debitMap = (HashMap) lst.get(0);
                        }
                    }
                    //babu added by transfer sus case
                //    System.out.println("transactionTO.getProductType() == " + transactionTO.getProductType());
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("SA")) {
                        debitMap.put("prodId", transactionTO.getProductId());
                        List lst = sqlMap.executeQueryForList("getAccountHeadProdSAHead", debitMap);
                        if (lst != null && lst.size() > 0) {
                            debitMap = (HashMap) lst.get(0);
                            //transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                        }

                    }
                    HashMap applicationMap = new HashMap();
                    applicationMap.put("SCHEME_NAME", mdsCurrentReceiptEntryTO.getSchemeName());
                    List lst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", applicationMap);
                    if (lst != null && lst.size() > 0) {
                        applicationMap = (HashMap) lst.get(0);
                    }
                    HashMap linkBatchMap  = new HashMap();
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
                        
                   // commonTransactionCashandTransfer(map, mdsCurrentReceiptEntryTO, debitMap, applicationMap);
                }
            }
            if (mdsReceiptEntryTO != null) { //INSERT RECIEPT TABLE
                if (bankAdvInsAmt > 0) {
                    if (bankAdvInsAmt > 0 && instalmentPayAmt > 0) {
                        System.out.println("###### INSERT BOTH MDS_RECEIPT TRANS_ID : ");
                         if (map.containsKey("BANK_ADVANCE_NET_TRANS_ID") && map.get("BANK_ADVANCE_NET_TRANS_ID") != null && CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_NET_TRANS_ID")).length() > 0) {
                            mdsReceiptEntryTO.setNetTransId(CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_NET_TRANS_ID")) + "," + CommonUtil.convertObjToStr(map.get("CURRENT_NET_TRANS_ID")));
                        } else {
                            mdsReceiptEntryTO.setNetTransId(CommonUtil.convertObjToStr(map.get("CURRENT_NET_TRANS_ID")));
                        }
                        if (map.containsKey("BANK_ADVANCE_BONUS_TRANS_ID") && map.get("BANK_ADVANCE_BONUS_TRANS_ID") != null && CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_BONUS_TRANS_ID")).length() > 0) {
                            mdsReceiptEntryTO.setBonusTransId(CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_BONUS_TRANS_ID")) + "," + CommonUtil.convertObjToStr(map.get("CURRENT_BONUS_TRANS_ID")));
                        } else {
                            mdsReceiptEntryTO.setBonusTransId(CommonUtil.convertObjToStr(map.get("CURRENT_BONUS_TRANS_ID")));
                        }
                        mdsReceiptEntryTO.setPenalTransId(CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_PENAL_TRANS_ID")));
                        mdsReceiptEntryTO.setNoticeId(CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_NOTICE_TRANS_ID")));
                        mdsReceiptEntryTO.setArbitrationId(CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_ARBITRATION_TRANS_ID")));
                        mdsReceiptEntryTO.setDiscountTransId(CommonUtil.convertObjToStr(map.get("CURRENT_DISCOUNT_TRANS_ID")));
                        if(map.containsKey("BANK_ADVANCE_FORFEITBONUS_TRANS_ID") && map.get("BANK_ADVANCE_FORFEITBONUS_TRANS_ID") != null){ //2093
                         mdsReceiptEntryTO.setForfeitBonusTransId(CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_FORFEITBONUS_TRANS_ID")));
                        }else{
                         mdsReceiptEntryTO.setForfeitBonusTransId("");   
                        }
                        if (map.containsKey("BANK_ADVANCE_PENAL_WAIVE_TRANS_ID") && map.get("BANK_ADVANCE_PENAL_WAIVE_TRANS_ID") != null) { //2093
                            mdsReceiptEntryTO.setPenalWaiveId(CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_PENAL_WAIVE_TRANS_ID")));
                    } else {
                            mdsReceiptEntryTO.setPenalWaiveId("");
                        }
                        if (map.containsKey("BANK_ADVANCE_NOTICE_WAIVE_TRANS_ID") && map.get("BANK_ADVANCE_NOTICE_WAIVE_TRANS_ID") != null) { //2093
                            mdsReceiptEntryTO.setNoticeWaiveId(CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_NOTICE_WAIVE_TRANS_ID")));
                        } else {
                            mdsReceiptEntryTO.setNoticeWaiveId("");
                        }
                        if (map.containsKey("BANK_ADVANCE_ARC_WAIVE_TRANS_ID") && map.get("BANK_ADVANCE_ARC_WAIVE_TRANS_ID") != null) { //2093
                            mdsReceiptEntryTO.setArcWaiveId(CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_ARC_WAIVE_TRANS_ID")));
                        } else {
                            mdsReceiptEntryTO.setArcWaiveId("");
                        }
                        
                        if (map.containsKey("BANK_ADVANCE_PENAL_WAIVE_AMT") && map.get("BANK_ADVANCE_PENAL_WAIVE_AMT") != null) { 
                            mdsReceiptEntryTO.setPenalWaiveAmt(CommonUtil.convertObjToDouble(map.get("BANK_ADVANCE_PENAL_WAIVE_AMT")));
                        }if (map.containsKey("BANK_ADVANCE_NOTICE_WAIVE_AMT") && map.get("BANK_ADVANCE_NOTICE_WAIVE_AMT") != null) { 
                            mdsReceiptEntryTO.setNoticeWaiveAmt(CommonUtil.convertObjToDouble(map.get("BANK_ADVANCE_NOTICE_WAIVE_AMT")));
                        }if (map.containsKey("BANK_ADVANCE_ARC_WAIVE_AMT") && map.get("BANK_ADVANCE_ARC_WAIVE_AMT") != null) { 
                            mdsReceiptEntryTO.setArcWaiveAmt(CommonUtil.convertObjToDouble(map.get("BANK_ADVANCE_ARC_WAIVE_AMT")));
                        }
                        
                    } else {
                        System.out.println("###### INSERT ONLY BANKA_ADVANCE MDS_RECEIPT TRANS_ID : ");
                        mdsReceiptEntryTO.setNetTransId(CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_NET_TRANS_ID")));
                        mdsReceiptEntryTO.setBonusTransId(CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_BONUS_TRANS_ID")));
                        mdsReceiptEntryTO.setPenalTransId(CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_PENAL_TRANS_ID")));
                        mdsReceiptEntryTO.setNoticeId(CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_NOTICE_TRANS_ID")));
                        mdsReceiptEntryTO.setArbitrationId(CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_ARBITRATION_TRANS_ID")));
                        if(map.containsKey("BANK_ADVANCE_FORFEITBONUS_TRANS_ID") && map.get("BANK_ADVANCE_FORFEITBONUS_TRANS_ID") != null){//2093
                         mdsReceiptEntryTO.setForfeitBonusTransId(CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_FORFEITBONUS_TRANS_ID")));
                        }else{
                         mdsReceiptEntryTO.setForfeitBonusTransId("");   
                        }
                        if (map.containsKey("BANK_ADVANCE_PENAL_WAIVE_TRANS_ID") && map.get("BANK_ADVANCE_PENAL_WAIVE_TRANS_ID") != null) { //2093
                            mdsReceiptEntryTO.setPenalWaiveId(CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_PENAL_WAIVE_TRANS_ID")));
                        } else {
                            mdsReceiptEntryTO.setPenalWaiveId("");
                        }
                        if (map.containsKey("BANK_ADVANCE_NOTICE_WAIVE_TRANS_ID") && map.get("BANK_ADVANCE_NOTICE_WAIVE_TRANS_ID") != null) { //2093
                            mdsReceiptEntryTO.setNoticeWaiveId(CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_NOTICE_WAIVE_TRANS_ID")));
                        } else {
                            mdsReceiptEntryTO.setNoticeWaiveId("");
                        }
                        if (map.containsKey("BANK_ADVANCE_ARC_WAIVE_TRANS_ID") && map.get("BANK_ADVANCE_ARC_WAIVE_TRANS_ID") != null) { //2093
                            mdsReceiptEntryTO.setArcWaiveId(CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_ARC_WAIVE_TRANS_ID")));
                        } else {
                            mdsReceiptEntryTO.setArcWaiveId("");
                        }
                        
                        if (map.containsKey("BANK_ADVANCE_PENAL_WAIVE_AMT") && map.get("BANK_ADVANCE_PENAL_WAIVE_AMT") != null) { 
                            mdsReceiptEntryTO.setPenalWaiveAmt(CommonUtil.convertObjToDouble(map.get("BANK_ADVANCE_PENAL_WAIVE_AMT")));
                        }if (map.containsKey("BANK_ADVANCE_NOTICE_WAIVE_AMT") && map.get("BANK_ADVANCE_NOTICE_WAIVE_AMT") != null) { 
                            mdsReceiptEntryTO.setNoticeWaiveAmt(CommonUtil.convertObjToDouble(map.get("BANK_ADVANCE_NOTICE_WAIVE_AMT")));
                        }if (map.containsKey("BANK_ADVANCE_ARC_WAIVE_AMT") && map.get("BANK_ADVANCE_ARC_WAIVE_AMT") != null) { 
                            mdsReceiptEntryTO.setArcWaiveAmt(CommonUtil.convertObjToDouble(map.get("BANK_ADVANCE_ARC_WAIVE_AMT")));
                        }
                        
                        
                    }
                } else {
                    System.out.println("###### INSERT ONLY MDS_RECEIPT TRANS_ID : ");
                    mdsReceiptEntryTO.setNetTransId(CommonUtil.convertObjToStr(map.get("CURRENT_NET_TRANS_ID")));
                    if (mdsReceiptEntryTO.getBonusAmtPayable() > 0) {
                        mdsReceiptEntryTO.setBonusTransId(CommonUtil.convertObjToStr(map.get("CURRENT_BONUS_TRANS_ID")));
                    } else if (mdsReceiptEntryTO.getPenalAmtPayable() > 0)//bbbbb
                    {
                        mdsReceiptEntryTO.setPenalTransId(CommonUtil.convertObjToStr(map.get("CURRENT_PENAL_TRANS_ID")));
                    } else {
                        mdsReceiptEntryTO.setPenalTransId("");
                    }
                    if (mdsReceiptEntryTO.getDiscountAmt() > 0) {
                        mdsReceiptEntryTO.setDiscountTransId(CommonUtil.convertObjToStr(map.get("CURRENT_DISCOUNT_TRANS_ID")));
                    } else {
                        mdsReceiptEntryTO.setDiscountTransId("");
                    }

                    mdsReceiptEntryTO.setNoticeId(CommonUtil.convertObjToStr(map.get("CURRENT_NOTICE_TRANS_ID")));
                    mdsReceiptEntryTO.setArbitrationId(CommonUtil.convertObjToStr(map.get("CURRENT_ARBITRATION_TRANS_ID")));
                    mdsReceiptEntryTO.setForfeitBonusTransId(""); //2093
                }
                mdsReceiptEntryTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
                mdsReceiptEntryTO.setStatus(CommonConstants.STATUS_CREATED);
                mdsReceiptEntryTO.setStatusDt((Date)currDt.clone());
                mdsReceiptEntryTO.setSingleTransId(generateSingleTransId);
                double noticeAmount = CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNoticeAmt());//9535
                mdsReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNetAmt()) - noticeAmount);
               // System.out.println("###### mdsReceiptEntryTO ###### : " + mdsReceiptEntryTO);
                //Added by Nidhin mantis Id :9624
                if (isSplitMDSTransaction != null && isSplitMDSTransaction.equals("Y")) {
                    if (map.containsKey("mdsReceiptEntryTOList") && map.get("mdsReceiptEntryTOList") != null) {
                        List mdssReceiptList = (List) map.get("mdsReceiptEntryTOList");
                     //   System.out.println("Going to Inserting ...."+mdssReceiptList);
                        if (mdssReceiptList != null && mdssReceiptList.size() > 0) {
                            for (int i = 0; i < mdssReceiptList.size(); i++) {
                                MDSReceiptEntryTO MDSTo = (MDSReceiptEntryTO) mdssReceiptList.get(i);
                                MDSTo.setNetTransId(CommonUtil.convertObjToStr(map.get("CURRENT_NET_TRANS_ID")));
                                if (MDSTo.getBonusAmtPayable() > 0) {
                                    MDSTo.setBonusTransId(CommonUtil.convertObjToStr(map.get("CURRENT_BONUS_TRANS_ID")));
                                } else {
                                    if (map.containsKey("CURRENT_BONUS_TRANS_ID") && map.get("CURRENT_BONUS_TRANS_ID") != null) {
                                        MDSTo.setBonusTransId(CommonUtil.convertObjToStr(map.get("CURRENT_BONUS_TRANS_ID")));
                                    }
                                }
                                if (MDSTo.getPenalAmtPayable() > 0)//bbbbb
                                {
                                    MDSTo.setPenalTransId(CommonUtil.convertObjToStr(map.get("CURRENT_PENAL_TRANS_ID")));
                                } else {
                                    MDSTo.setPenalTransId("");
                                }
                                if (mdsReceiptEntryTO.getDiscountAmt() > 0) {
                                    MDSTo.setDiscountTransId(CommonUtil.convertObjToStr(map.get("CURRENT_DISCOUNT_TRANS_ID")));
                                } else {
                                    MDSTo.setDiscountTransId("");
                                }
                                MDSTo.setBranchCode(mdsReceiptEntryTO.getBranchCode());//Added by nithya on 28-11-2017 for 6559 IN MDS MEMBER RECEIPT ENTRY, BRANCH CODE IS NOT UPDATING IN MDS_RECEIPT_ENTRY TABLE
                                MDSTo.setNoticeId(CommonUtil.convertObjToStr(map.get("CURRENT_NOTICE_TRANS_ID")));
                                MDSTo.setArbitrationId(CommonUtil.convertObjToStr(map.get("CURRENT_ARBITRATION_TRANS_ID")));
                                MDSTo.setForfeitBonusTransId(""); //Added by nithya for KD-2093
                                MDSTo.setForfeitBonusAmtPayable(mdsReceiptEntryTO.getForfeitBonusAmtPayable());
                                MDSTo.setBankAdvanceAmt(mdsReceiptEntryTO.getBankAdvanceAmt());
                                MDSTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                                MDSTo.setStatus(CommonConstants.STATUS_CREATED);
                                MDSTo.setStatusDt(setProperDtFormat(currDt));
                                MDSTo.setSingleTransId(generateSingleTransId);                               
                                sqlMap.executeUpdate("insertReceiptEntryTO", MDSTo);
                            }
                        }
                    }
                } else { 
                    System.out.println("mdsReceiptEntryTO  checking ::" + mdsReceiptEntryTO);
                    if (mdsReceiptEntryTO.getNetTransId().length() > 0) {
                        sqlMap.executeUpdate("insertReceiptEntryTO", mdsReceiptEntryTO);
                    }else{
                        System.out.println("MDS transaction not happened");
                    }
                }
                // Commented by nithya on 22-10-2018 for KD 293 - Mds receipt entry last installment reject/roll back issue
                // Reason : The same operation will be done while authorization. 
                //updateChitCloseDate(mdsReceiptEntryTO,null);
                //System.out.println("$#@$#$@# mdsReceiptEntryTO :" + mdsReceiptEntryTO);
                penalTransHappened = false;
                waiveTransHappened = false;
                //From Recover Tally List
                if (map.containsKey("FROM_RECOVERY_TALLY") || map.containsKey("FROM_MDS_MEMBER_RECEIPT")) {
                    returnMap.put("BATCH_ID", CommonUtil.convertObjToStr(mdsReceiptEntryTO.getNetTransId()));
                }
                //From Mds_Memeber_Receipt_Entry
                if (map.containsKey("FROM_MDS_MEMBER_RECEIPT")) {
                    returnMap.put("BONUS_TRANS_ID", CommonUtil.convertObjToStr(map.get("CURRENT_BONUS_TRANS_ID")));
                }


              //  System.out.println("mdsCurrentReceiptEntryTO " + mdsCurrentReceiptEntryTO + "   " + mdsBankAdvReceiptEntryTO);
                if (mdsCurrentReceiptEntryTO != null) {
                    setTransDetails(mdsCurrentReceiptEntryTO);
                }
                if (mdsBankAdvReceiptEntryTO != null) {
                    setTransDetails(mdsBankAdvReceiptEntryTO);
                }
//                penalTransHappened = false;
//                setTransDetails(mdsReceiptEntryTO);
            }
            if (map.containsKey("serviceTaxDetailsTO")) {
                ServiceTaxDetailsTO objserTax = (ServiceTaxDetailsTO) map.get("serviceTaxDetailsTO");
                objserTax.setAcct_Num(mdsReceiptEntryTO.getNetTransId());
                insertServiceTaxDetails(objserTax);
            }           

        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private MDSReceiptEntryTO getMDSReceiptEntryTO(double bonusAmt, double instAmt) {
        double netAmt = 0.0;
        netAmt = instAmt - bonusAmt;
        MDSReceiptEntryTO mdsBankAdvReceiptEntryTO = new MDSReceiptEntryTO();
        mdsBankAdvReceiptEntryTO.setSchemeName(mdsReceiptEntryTO.getSchemeName());
        mdsBankAdvReceiptEntryTO.setChittalNo(mdsReceiptEntryTO.getChittalNo());
        mdsBankAdvReceiptEntryTO.setSubNo(mdsReceiptEntryTO.getSubNo());
        mdsBankAdvReceiptEntryTO.setDivisionNo(mdsReceiptEntryTO.getDivisionNo());
        mdsBankAdvReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(String.valueOf(netAmt)));
        mdsBankAdvReceiptEntryTO.setInstAmt(CommonUtil.convertObjToDouble(String.valueOf(instAmt)));
        mdsBankAdvReceiptEntryTO.setBonusAmtPayable(CommonUtil.convertObjToDouble(String.valueOf(bonusAmt)));
        mdsBankAdvReceiptEntryTO.setNoticeAmt(mdsReceiptEntryTO.getNoticeAmt());
        mdsBankAdvReceiptEntryTO.setArbitrationAmt(mdsReceiptEntryTO.getArbitrationAmt());
        mdsBankAdvReceiptEntryTO.setPenalAmtPayable(mdsReceiptEntryTO.getPenalAmtPayable());
        mdsBankAdvReceiptEntryTO.setDiscountAmt(mdsReceiptEntryTO.getDiscountAmt());
        mdsBankAdvReceiptEntryTO.setBranchCode(mdsReceiptEntryTO.getBranchCode());
        mdsBankAdvReceiptEntryTO.setForfeitBonusAmtPayable(mdsReceiptEntryTO.getForfeitBonusAmtPayable());// 16-07-2020
        return mdsBankAdvReceiptEntryTO;
    }

    private String getCashUniqueId() throws Exception {
        //  final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "CASH_UNIQUE_ID");
        HashMap map = generateID();
        return (String) map.get(CommonConstants.DATA);
    }

    public HashMap generateID() {
        HashMap hash = null, result = null;
        try {
            String mapName = "getCurrentID";
            HashMap where = new HashMap();
            where.put("ID_KEY", "CASH_UNIQUE_ID"); //Here u have to pass BORROW_ID or something else
            List list = null;
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

    private void insertReceiptData(HashMap map, MDSReceiptEntryTO mdsCurrentReceiptEntryTO, LogDAO objLogDAO, LogTO objLogTO, String cash_unique_id) throws Exception {
        try {
//            System.out.println("###### MDS REPAYMENT ###### : ");
            HashMap waiveMap = null;
            double penalWaiveAmt = 0.0;
            double arcWaiveAmt = 0.0;
            double noticeWaiveAmt = 0.0;
            double totalWaiveAmt = 0.0;
            String arcHead = "";
            String generateSingleCashId = "";
            String thalayalTransCategory = "FULLAMT_TO_FULLAMT";
            String munnalTransCategory = "FULLAMT_TO_FULLAMT";
            double postageAmt = 0.0;
            double renewPostageAmt = 0.0;
            String postageAcHd = "";
            System.out.println("map****" + map);
            if(map.containsKey("WAIVE_MAP") && map.get("WAIVE_MAP") != null){
                waiveMap =(HashMap) map.get("WAIVE_MAP");
                penalWaiveAmt = CommonUtil.convertObjToDouble(waiveMap.get("PENAL_AMOUNT"));
                arcWaiveAmt = CommonUtil.convertObjToDouble(waiveMap.get("ARC_AMOUNT"));
                noticeWaiveAmt = CommonUtil.convertObjToDouble(waiveMap.get("NOTICE_AMOUNT"));
                totalWaiveAmt = CommonUtil.convertObjToDouble(waiveMap.get("Total_WaiveAmt"));
            }
            if (map.containsKey("POSTAGE_AMT_FOR_INT") || map.containsKey("RENEW_POSTAGE_AMT")) {
                postageAmt = CommonUtil.convertObjToDouble(map.get("POSTAGE_AMT_FOR_INT")).doubleValue();
                renewPostageAmt = CommonUtil.convertObjToDouble(map.get("RENEW_POSTAGE_AMT")).doubleValue();
                postageAcHd = CommonUtil.convertObjToStr(map.get("POSTAGE_ACHD"));
              //  System.out.println("postageAmt****" + postageAmt);
            }
            HashMap serviceTaxMap = new HashMap();
            double service_taxAmt = 0;
            if (map.containsKey("serviceTaxDetails")) {
                serviceTaxMap = (HashMap) map.get("serviceTaxDetails");
                if (serviceTaxMap.containsKey("TOT_TAX_AMT") && serviceTaxMap.get("TOT_TAX_AMT") != null) {
                    service_taxAmt = CommonUtil.convertObjToDouble(serviceTaxMap.get("TOT_TAX_AMT"));
                }
            }
            mdsCurrentReceiptEntryTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
            mdsCurrentReceiptEntryTO.setStatus(CommonConstants.STATUS_CREATED);
            mdsCurrentReceiptEntryTO.setStatusDt(currDt);
            System.out.println("###### insertTransactionData 11: " + map);
            ArrayList transferList = new ArrayList();
            TransferTrans transferTrans = new TransferTrans();
            TxTransferTO transferTo = new TxTransferTO();
            //ArrayList TxTransferTO = new ArrayList();
            TransactionTO transactionTO = new TransactionTO();
            HashMap txMap = new HashMap();
            HashMap transactionListMap = new HashMap();
            String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
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
                applicationMap.put("SCHEME_NAME", mdsCurrentReceiptEntryTO.getSchemeName());
                List lst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", applicationMap);
                if (lst != null && lst.size() > 0) {
                    applicationMap = (HashMap) lst.get(0);
                    if(applicationMap != null && applicationMap.containsKey("BANK_MDS_TRNF_CAT_THALAYAL") && applicationMap.get("BANK_MDS_TRNF_CAT_THALAYAL") != null){
                        thalayalTransCategory = CommonUtil.convertObjToStr(applicationMap.get("BANK_MDS_TRNF_CAT_THALAYAL"));
                    }
                    if(applicationMap != null && applicationMap.containsKey("BANK_MDS_TRNF_CAT_MUNNAL") && applicationMap.get("BANK_MDS_TRNF_CAT_MUNNAL") != null){
                        munnalTransCategory = CommonUtil.convertObjToStr(applicationMap.get("BANK_MDS_TRNF_CAT_MUNNAL"));
                    }
                }
                if (transactionTO.getTransType().equals("TRANSFER") || transactionTO.getTransType().equals("CASH")) {
                    String linkBatchId = "";
                    String glTransActNum = "";
                    if (transactionTO.getDebitAcctNo() != null && transactionTO.getDebitAcctNo().length() > 0) {
                        linkBatchId = CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo());
                    }
                    if (map.containsKey("GL_TRANS_ACT_NUM") && CommonUtil.convertObjToStr(map.get("GL_TRANS_ACT_NUM")).length() > 0) {
                        glTransActNum = CommonUtil.convertObjToStr(map.get("GL_TRANS_ACT_NUM"));
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
                    //babu added by transfer sus case
              //      System.out.println("transactionTO.getProductType() == " + transactionTO.getProductType());
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("SA")) {
                        debitMap.put("prodId", transactionTO.getProductId());
                        lst = sqlMap.executeQueryForList("getAccountHeadProdSAHead", debitMap);
                        if (lst != null && lst.size() > 0) {
                            debitMap = (HashMap) lst.get(0);
                            //transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                        }

                    }
                    //added by sreekrishnan for advances case
//                    System.out.println("transactionTO#####getProductId" + transactionTO.getProductId());
//                    System.out.println("transactionTO#####getProductType" + transactionTO.getProductType());
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("AD")) {
                        debitMap.put("prodId", transactionTO.getProductId());
                        lst = sqlMap.executeQueryForList("getAccountHeadProdADHead", debitMap);
                        if (lst != null && lst.size() > 0) {
                            debitMap = (HashMap) lst.get(0);
                           // System.out.println("transactionTO#####debitMap" + debitMap);
                            //transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                        }

                    }
                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
               //     System.out.println("###### insertTransactionData mdsCurrentReceiptEntryTO : " + mdsCurrentReceiptEntryTO);
                    if (transactionTO.getTransType().equals("TRANSFER")) {

                        if (CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNetAmt()).doubleValue() > 0) {
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL") && transactionTO.getProductId().equals("")) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("OA")) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                                txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);

                            } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("TD")) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                                txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);

                            }//babu suspence transfer
                            else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("SA")) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                                txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.SUSPENSE);
                            } //babu 26-May-2013
                            //added by sreekrishnan for advance transfer
                            else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("AD")) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("ACCT_HEAD"));
                                txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.ADVANCES);
                            } ////Nidhin 11-Nov-2014 - Other Bank Transfer
                            else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("AB")) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("ACCT_HEAD"));
                                txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);
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
                            if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
                             //   System.out.println("map.get======" + map.get("FROM_PARTICULARS_TRANSALL"));
                                txMap.put(TransferTrans.PARTICULARS, map.get("FROM_PARTICULARS_TRANSALL"));
                            } else {
                               // txMap.put(TransferTrans.PARTICULARS, "Installment Amt :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsCurrentReceiptEntryTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
                                // Removed string "Installment Amt :" and added installment number at the end of particulars for 8366 on 29-11-2017
                                txMap.put(TransferTrans.PARTICULARS,mdsCurrentReceiptEntryTO.getNarration()+":"+applicationMap.get("MP_MDS_CODE") + "-" + mdsCurrentReceiptEntryTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
                            }
                            if (!transactionTO.getProductType().equals("") && (transactionTO.getProductType().equals("OA") || transactionTO.getProductType().equals("AB"))) {
                                txMap.put(TransferTrans.DR_BRANCH, transactionTO.getBranchId());
                            } else {
                                txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                            }
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            // Commenting the following block by nithya on 06-10-2020 for thalayal - munnal changes
//                            if(map.containsKey("THALAYAL_CHITTAL") && !map.get("THALAYAL_CHITTAL").equals("") && map.get("THALAYAL_CHITTAL").equals("Y")){
//                             //   System.out.println("thalayal#$%#$%#"+CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNetAmt()).doubleValue()+CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getBonusAmtPayable()).doubleValue());
//                                transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNetAmt()).doubleValue()+CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getBonusAmtPayable()).doubleValue());    
//                            }else{
//                                transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNetAmt()).doubleValue());
//                            }
                            
                            //---- Thalayal - Munnal Changes Started
                            
                            if (map.containsKey("THALAYAL_CHITTAL") && !map.get("THALAYAL_CHITTAL").equals("") && map.get("THALAYAL_CHITTAL").equals("Y")) {
                                if (thalayalTransCategory.equalsIgnoreCase("FULLAMT_TO_FULLAMT")) {
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNetAmt()).doubleValue() + CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getBonusAmtPayable()).doubleValue());
                                } else if (thalayalTransCategory.equalsIgnoreCase("FULLAMT_TO_SUBAMT")) {
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNetAmt()).doubleValue() + CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getBonusAmtPayable()).doubleValue());
                               
                                } else if (thalayalTransCategory.equalsIgnoreCase("SUBAMT_TO_SUBAMT")) {
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNetAmt()).doubleValue());
                                } else {
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNetAmt()).doubleValue() + CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getBonusAmtPayable()).doubleValue());
                                }
                            } else if (map.containsKey("MUNNAL_CHITTAL") && !map.get("MUNNAL_CHITTAL").equals("") && map.get("MUNNAL_CHITTAL").equals("Y")) {
                                if (munnalTransCategory.equalsIgnoreCase("FULLAMT_TO_FULLAMT")) {
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNetAmt()).doubleValue() + CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getBonusAmtPayable()).doubleValue());
                                } else if (munnalTransCategory.equalsIgnoreCase("FULLAMT_TO_SUBAMT")) {
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNetAmt()).doubleValue() + CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getBonusAmtPayable()).doubleValue());                               
                                } else if (munnalTransCategory.equalsIgnoreCase("SUBAMT_TO_SUBAMT")) {
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNetAmt()).doubleValue());
                                } else {
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNetAmt()).doubleValue() + CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getBonusAmtPayable()).doubleValue());
                                }
                            } else {
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNetAmt()).doubleValue() - totalWaiveAmt);
                            }
                            //-- End
                            
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt(currDt);
                            if (!transactionTO.getProductType().equals("") && (transactionTO.getProductType().equals("OA")|| transactionTO.getProductType().equals("AB"))) {
                                transferTo.setBranchId(transactionTO.getBranchId());
                            } else {
                                transferTo.setBranchId(BRANCH_ID);
                            }
                            transferTo.setNarration(transactionTO.getParticulars());//KD-3393
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                            transferTo.setRec_mode(CommonUtil.convertObjToStr(map.get("REC_MODE")));
                            if (!glTransActNum.equals("") && glTransActNum.length() > 0) {
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(glTransActNum));
                            } else {
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
                            }
                            transferTo.setGlTransActNum(mdsCurrentReceiptEntryTO.getChittalNo());
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            TxTransferTO.add(transferTo);
                        }
                     //   System.out.println("TxTransferTO List 1 : " + TxTransferTO);
                        if (!penalTransHappened) {
                            //if (CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getPenalAmtPayable()).doubleValue() > 0) {
                              if (CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getPenalAmtPayable()).doubleValue() - penalWaiveAmt > 0) {
                                System.out.println("penal Started");
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_AC_HD, (String) applicationMap.get("PENAL_INTEREST_HEAD"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_BRANCH, mdsCurrentReceiptEntryTO.getBranchCode()); //BRANCH_ID
                                //babu 26-May-2013  

                                if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
                                  //  System.out.println("map.get====11==" + map.get("FROM_PARTICULARS_TRANSALL"));
                                    txMap.put(TransferTrans.PARTICULARS, "Penal" + map.get("FROM_PARTICULARS_TRANSALL"));
                                } else {
                                    txMap.put(TransferTrans.PARTICULARS, "Penal :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsCurrentReceiptEntryTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
                                }
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getPenalAmtPayable()).doubleValue() - penalWaiveAmt);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setTransDt(currDt);
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setBranchId(mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                transferTo.setRec_mode(CommonUtil.convertObjToStr(map.get("REC_MODE")));

                                if (!glTransActNum.equals("") && glTransActNum.length() > 0) {
                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(glTransActNum));
                                } else {
                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
                                }
                                transactionTO.setChequeNo("SERVICE_TAX");
                                transferTo.setGlTransActNum(mdsCurrentReceiptEntryTO.getChittalNo());
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                transferTo.setInstrumentNo2("PENAL_AMT");// Added by nithya on 31-10-2019 for KD-680
                                TxTransferTO.add(transferTo);
                             //   System.out.println("transferTo List 2 : " + transferTo);
                            }
                            //Notice Charge Amount Transaction Started
                            //if (CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNoticeAmt()).doubleValue() > 0) {
                              if (CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNoticeAmt()).doubleValue() - noticeWaiveAmt > 0) {
                                System.out.println("Notice Charge Started !!! ");
                                HashMap whereMap = new HashMap();
                                List chargeList = null;
                                whereMap.put("ACT_NUM", CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()) + "_" + CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getSubNo()));
                                if (map.containsKey("INT_CALC_UPTO_DT")) {        // FROM RECOVERY LIST
                                    whereMap.put("INT_CALC_UPTO_DT", map.get("INT_CALC_UPTO_DT"));
                                    chargeList = (List) sqlMap.executeQueryForList("getMDSRecoveryNoticeChargeDetails", whereMap);
                                } else {
                                    chargeList = (List) sqlMap.executeQueryForList("getMDSNoticeChargeDetails", whereMap);
                                }
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
                                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                            transferTo.setTransModType(CommonConstants.MMBS);
                                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("NOTICE_CHARGES_HEAD"));
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.CR_BRANCH, mdsCurrentReceiptEntryTO.getBranchCode()); //BRANCH_ID
                                            //babu 26-May-2013  

                                            if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
                                             //   System.out.println("map.get==22====" + map.get("FROM_PARTICULARS_TRANSALL"));
                                                txMap.put(TransferTrans.PARTICULARS, chargeType + ": " + map.get("FROM_PARTICULARS_TRANSALL"));
                                            } else {
                                                txMap.put(TransferTrans.PARTICULARS, chargeType + applicationMap.get(" MP_MDS_CODE") + "-" + mdsCurrentReceiptEntryTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
                                            }
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                            txMap.put("TRANS_MOD_TYPE", "MDS");
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, chargeAmount - noticeWaiveAmt);
                                            transferTo.setTransId("-");
                                            transferTo.setBatchId("-");
                                            transferTo.setTransDt(currDt);
                                            transferTo.setInitiatedBranch(BRANCH_ID);
                                            transferTo.setBranchId(mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);
                                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                            transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                            transferTo.setRec_mode(CommonUtil.convertObjToStr(map.get("REC_MODE")));

                                            if (!glTransActNum.equals("") && glTransActNum.length() > 0) {
                                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(glTransActNum));
                                            } else {
                                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
                                            }
                                            transactionTO.setChequeNo("SERVICE_TAX");
                                            transferTo.setGlTransActNum(mdsCurrentReceiptEntryTO.getChittalNo());
                                            transferTo.setSingleTransId(generateSingleTransId);
                                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                            transferTo.setInstrumentNo2("NOTICE_CHARGE"); // Added by nithya on 31-10-2019 for KD-680
                                            TxTransferTO.add(transferTo);
                                         //   System.out.println("Notice Charge transferTo List  : " + i + " " + transferTo);
                                        }
                                    }
                                }
                            }
                         //   System.out.println("TxTransferTO List 3 : " + transferTo);
                            if(arcWaiveAmt > 0){
                                arcHead = getARCHead(mdsCurrentReceiptEntryTO);
                            }
                            //Case Expense Amount Transaction
                            //if (CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getArbitrationAmt()).doubleValue() > 0) {
                              if (CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getArbitrationAmt()).doubleValue() - arcWaiveAmt > 0) {
                                System.out.println("Arbitration Started");
                                HashMap whereMap = new HashMap();
                                List chargeList = null;
                                whereMap.put("ACT_NUM", CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()) + "_" + CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getSubNo()));
                                if (map.containsKey("INT_CALC_UPTO_DT")) {        // FROM RECOVERY LIST
                                    whereMap.put("INT_CALC_UPTO_DT", map.get("INT_CALC_UPTO_DT"));
                                    chargeList = (List) sqlMap.executeQueryForList("getMDSRecoveryCaseChargeDetails", whereMap);
                                } else {
                                    chargeList = (List) sqlMap.executeQueryForList("getMDSCaseChargeDetails", whereMap);
                                }
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
                                            if (chargeType.equals("ARC_COST")) {
                                                chargeType = "ARC_COST";
                                            } else if (chargeType.equals("ARC Expense")) {
                                                chargeType = "ARC_EXPENSE";
                                            } else if (chargeType.equals("EA Cost")) {
                                                chargeType = "EA_COST";
                                            } else if (chargeType.equals("EA Expense")) {
                                                chargeType = "EA_EXPENSE";
                                            } else if (chargeType.equals("EP_COST")) {
                                                chargeType = "EP_COST";
                                            } else if (chargeType.equals("EP Expense")) {
                                                chargeType = "EP_EXPENSE";
                                            } else if (chargeType.equals("ARBITRARY CHARGES")) {
                                                chargeType = "ARC_COST";
                                            } else if (chargeType.equals("EXECUTION DECREE CHARGES")) {
                                                chargeType = "EP_COST";
                                            } else if (chargeType.equals("MISCELLANEOUS CHARGES")) {
                                                chargeType = "MISCELLANEOUS_CHARGES";
                                            } else if (chargeType.equals("OTHER CHARGES")) {
                                                chargeType = "OTHER_CHARGES";
                                            } else if (chargeType.equals("LEGAL CHARGES")) {
                                                chargeType = "LEGAL_CHARGES";
                                            } else if (chargeType.equals("INSURANCE CHARGES")) {
                                                chargeType = "INSURANCE_CHARGES";
                                            }
                                            arcHead = chargeType;
                                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                                            System.out.println("###### chargeType Head : " + chargeType);
//                                            System.out.println("###### chargeAmount : " + chargeAmount);
//                                            System.out.println("###### chargeAmount Head2222: " + applicationMap.get(chargeType));
//                                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("CASE_EXPENSE_HEAD"));
                                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get(chargeType));
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.CR_BRANCH, mdsCurrentReceiptEntryTO.getBranchCode()); //BRANCH_ID
                                            //babu 26-May-2013  

                                            if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
                                                System.out.println("map.get=33====" + map.get("FROM_PARTICULARS_TRANSALL"));
                                                txMap.put(TransferTrans.PARTICULARS, chargeType + ": " + map.get("FROM_PARTICULARS_TRANSALL"));
                                            } else {
                                                txMap.put(TransferTrans.PARTICULARS, chargeType + applicationMap.get(" MP_MDS_CODE") + "-" + mdsCurrentReceiptEntryTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
                                            }
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                            txMap.put("TRANS_MOD_TYPE", "MDS");
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, chargeAmount - arcWaiveAmt);
                                            transferTo.setTransId("-");
                                            transferTo.setBatchId("-");
                                            transferTo.setTransDt(currDt);
                                            transferTo.setInitiatedBranch(BRANCH_ID);
                                            transferTo.setBranchId(mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);
                                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                            transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                            transferTo.setRec_mode(CommonUtil.convertObjToStr(map.get("REC_MODE")));

                                            if (!glTransActNum.equals("") && glTransActNum.length() > 0) {
                                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(glTransActNum));
                                            } else {
                                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
                                            }
                                            transactionTO.setChequeNo("SERVICE_TAX");
                                            transferTo.setGlTransActNum(mdsCurrentReceiptEntryTO.getChittalNo());
                                            transferTo.setSingleTransId(generateSingleTransId);
                                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                            transferTo.setInstrumentNo2(chargeType);// Added by nithya on 31-10-2019 for KD-680
                                            TxTransferTO.add(transferTo);
                                         //   System.out.println("Arbitration transferTo List  : " + i + " " + transferTo);
                                        }
                                    }
                                }
                            }
                            //added by chithra for service Tax
                            if (serviceTaxMap != null && serviceTaxMap.size() > 0) {
                                transferTo = new TxTransferTO();
                                double swachhCess = 0.0;
                                double krishikalyanCess = 0.0;
                                double serTaxAmt = 0.0;
                                double totalServiceTaxAmt = 0.0;
                                double normalServiceTax = 0.0;
                                if (serviceTaxMap.containsKey("TOT_TAX_AMT") && serviceTaxMap.get("TOT_TAX_AMT") != null) {
                                    serTaxAmt = CommonUtil.convertObjToDouble(serviceTaxMap.get("TOT_TAX_AMT"));
                                    totalServiceTaxAmt = CommonUtil.convertObjToDouble(serviceTaxMap.get("TOT_TAX_AMT"));
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
                                if(swachhCess > 0){
                                    txMap = new HashMap();
                                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                    txMap.put(TransferTrans.CR_AC_HD, serviceTaxMap.get("SWACHH_HEAD_ID"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.CR_BRANCH, mdsCurrentReceiptEntryTO.getBranchCode()); //BRANCH_ID
                                    if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
                                        txMap.put(TransferTrans.PARTICULARS, "CGST" + map.get("FROM_PARTICULARS_TRANSALL"));
                                    } else {
                                        txMap.put(TransferTrans.PARTICULARS, "SGST:" + applicationMap.get("MP_MDS_CODE") + "-" + mdsCurrentReceiptEntryTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
                                    }
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                    txMap.put("TRANS_MOD_TYPE", "MDS");
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, swachhCess);
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setTransDt(currDt);
                                    transferTo.setInitiatedBranch(BRANCH_ID);
                                    transferTo.setBranchId(mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                    transferTo.setRec_mode(CommonUtil.convertObjToStr(map.get("REC_MODE")));
                                    if (!glTransActNum.equals("") && glTransActNum.length() > 0) {
                                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(glTransActNum));
                                    } else {
                                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
                                    }
                                    transactionTO.setChequeNo("CGST");
                                    transferTo.setGlTransActNum(mdsCurrentReceiptEntryTO.getChittalNo());
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    transferTo.setInstrumentNo2("CGST");// Added by nithya on 31-10-2019 for KD-680
                                    TxTransferTO.add(transferTo);
                                  //  serTaxAmt -= swachhCess;
                                }
                                if(krishikalyanCess > 0){
                                    txMap = new HashMap();
                                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                    txMap.put(TransferTrans.CR_AC_HD, serviceTaxMap.get("KRISHIKALYAN_HEAD_ID"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.CR_BRANCH, mdsCurrentReceiptEntryTO.getBranchCode()); //BRANCH_ID
                                    if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
                                        txMap.put(TransferTrans.PARTICULARS, "SGST" + map.get("FROM_PARTICULARS_TRANSALL"));
                                    } else {
                                        txMap.put(TransferTrans.PARTICULARS, "SGST :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsCurrentReceiptEntryTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
                                    }
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                    txMap.put("TRANS_MOD_TYPE", "MDS");
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap,krishikalyanCess);
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setTransDt(currDt);
                                    transferTo.setInitiatedBranch(BRANCH_ID);
                                    transferTo.setBranchId(mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                    transferTo.setRec_mode(CommonUtil.convertObjToStr(map.get("REC_MODE")));
                                    if (!glTransActNum.equals("") && glTransActNum.length() > 0) {
                                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(glTransActNum));
                                    } else {
                                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
                                    }
                                    transactionTO.setChequeNo("SGST");
                                    transferTo.setGlTransActNum(mdsCurrentReceiptEntryTO.getChittalNo());
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    transferTo.setInstrumentNo2("SGST");// Added by nithya on 31-10-2019 for KD-680
                                    TxTransferTO.add(transferTo);
                                   // serTaxAmt -= krishikalyanCess;
                                }
                                if(normalServiceTax > 0){
                                    txMap = new HashMap();
                                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                    txMap.put(TransferTrans.CR_AC_HD, serviceTaxMap.get("TAX_HEAD_ID"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.CR_BRANCH, mdsCurrentReceiptEntryTO.getBranchCode()); //BRANCH_ID
                                    if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
                                        txMap.put(TransferTrans.PARTICULARS, "Service Tax" + map.get("FROM_PARTICULARS_TRANSALL"));
                                    } else {
                                        txMap.put(TransferTrans.PARTICULARS, "Service Tax :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsCurrentReceiptEntryTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
                                    }
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                    txMap.put("TRANS_MOD_TYPE", "MDS");
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax);
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setTransDt(currDt);
                                    transferTo.setInitiatedBranch(BRANCH_ID);
                                    transferTo.setBranchId(mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                    transferTo.setRec_mode(CommonUtil.convertObjToStr(map.get("REC_MODE")));
                                    if (!glTransActNum.equals("") && glTransActNum.length() > 0) {
                                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(glTransActNum));
                                    } else {
                                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
                                    }
                                    transactionTO.setChequeNo("SERVICE_TAX");
                                    transferTo.setGlTransActNum(mdsCurrentReceiptEntryTO.getChittalNo());
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    transferTo.setInstrumentNo2("SERVICE_TAX");// Added by nithya on 31-10-2019 for KD-680
                                    TxTransferTO.add(transferTo);                                   
                                }                                
                            }
                            penalTransHappened = true;
                        }
                      //  System.out.println("transferTo List 4 : " + TxTransferTO);
                        double finalReceivingAmt = CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNetAmt()).doubleValue()
                                - (CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getPenalAmtPayable()).doubleValue()
                                + CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNoticeAmt()).doubleValue()
                                + CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getArbitrationAmt()).doubleValue() + service_taxAmt);
                        if(map.containsKey("THALAYAL_CHITTAL") && !map.get("THALAYAL_CHITTAL").equals("") && map.get("THALAYAL_CHITTAL").equals("Y")){
                            //finalReceivingAmt = finalReceivingAmt + CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getBonusAmtPayable()).doubleValue();
                            if(thalayalTransCategory.equalsIgnoreCase("FULLAMT_TO_FULLAMT")){
                                finalReceivingAmt = finalReceivingAmt + CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getBonusAmtPayable()).doubleValue();
                            }else if(thalayalTransCategory.equalsIgnoreCase("FULLAMT_TO_SUBAMT")){
                                finalReceivingAmt = finalReceivingAmt;
                            }else if(thalayalTransCategory.equalsIgnoreCase("SUBAMT_TO_SUBAMT")){
                                finalReceivingAmt = finalReceivingAmt;
                            }else{
                                finalReceivingAmt = finalReceivingAmt + CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getBonusAmtPayable()).doubleValue();                           
                            }
                        }if(map.containsKey("MUNNAL_CHITTAL") && !map.get("MUNNAL_CHITTAL").equals("") && map.get("MUNNAL_CHITTAL").equals("Y")){
                            //finalReceivingAmt = finalReceivingAmt + CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getBonusAmtPayable()).doubleValue();
                            if(munnalTransCategory.equalsIgnoreCase("FULLAMT_TO_FULLAMT")){
                                finalReceivingAmt = finalReceivingAmt + CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getBonusAmtPayable()).doubleValue();
                            }else if(munnalTransCategory.equalsIgnoreCase("FULLAMT_TO_SUBAMT")){
                                finalReceivingAmt = finalReceivingAmt;
                            }else if(munnalTransCategory.equalsIgnoreCase("SUBAMT_TO_SUBAMT")){
                                finalReceivingAmt = finalReceivingAmt;
                            }else{
                                finalReceivingAmt = finalReceivingAmt + CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getBonusAmtPayable()).doubleValue();                           
                            }
                        }
                        if (finalReceivingAmt > 0) {
                            System.out.println("Final Amount Transaction Started");
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            //Changed By Suresh
                            if (map.containsKey("MDS_CLOSURE")) {
                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("MDS_RECEIVABLE_HEAD"));
                            } else {
                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
                            }
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, mdsCurrentReceiptEntryTO.getBranchCode()); //BRANCH_ID
                            //babu 26-May-2013  

                            if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
                             //   System.out.println("map.get=33====" + map.get("FROM_PARTICULARS_TRANSALL"));
                                txMap.put(TransferTrans.PARTICULARS, map.get("FROM_PARTICULARS_TRANSALL"));
                            } else {
                                txMap.put(TransferTrans.PARTICULARS, "Installment Amt :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsCurrentReceiptEntryTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo()+ " - From : " +transactionTO.getDebitAcctNo());
                            }
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
//                            System.out.println("txMap5 : " + txMap+"serviceAmt :"+finalReceivingAmt);
                            txMap.put("TRANS_MOD_TYPE", "MDS");
                            if (postageAmt > 0.0 || renewPostageAmt > 0.0) {
                                if (postageAmt > 0.0) {
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, finalReceivingAmt - postageAmt);
                                }
                                if (renewPostageAmt > 0.0) {
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, finalReceivingAmt - renewPostageAmt);
                                }

                            } else {
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, finalReceivingAmt);
                            }
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            transferTo.setInstrumentNo2("INSTALMENT_AMT"); // Added by nithya on 31-10-2019 for KD-680
                            TxTransferTO.add(transferTo);
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt(currDt);
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setBranchId(mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                            transferTo.setRec_mode(CommonUtil.convertObjToStr(map.get("REC_MODE")));
                            transferTo.setGlTransActNum(mdsCurrentReceiptEntryTO.getChittalNo());
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            if (!glTransActNum.equals("") && glTransActNum.length() > 0) {
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(glTransActNum));
                            } else {
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
                            }

                            if (map.containsKey("POSTAGE_AMT_FOR_INT") || map.containsKey("RENEW_POSTAGE_AMT")) {
                                if (postageAmt > 0.0) {
                                    txMap.put(TransferTrans.CR_AC_HD, postageAcHd);
                                    txMap.put("AUTHORIZEREMARKS", "MDS_POSTAGE");
                                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    //babu 26-May-2013  

                                    if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
                                        System.out.println("map.get=55====" + map.get("FROM_PARTICULARS_TRANSALL"));
                                        txMap.put(TransferTrans.PARTICULARS, map.get("FROM_PARTICULARS_TRANSALL"));
                                    } else {
                                        txMap.put(TransferTrans.PARTICULARS, "Installment Amt :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsCurrentReceiptEntryTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
                                    }
                                    txMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    txMap.put("TRANS_MOD_TYPE", "MDS");
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, postageAmt);
                                    transferTo.setInitiatedBranch(BRANCH_ID);
                                    transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                    transferTo.setRec_mode(CommonUtil.convertObjToStr(map.get("REC_MODE")));
                                    transferTo.setGlTransActNum(mdsCurrentReceiptEntryTO.getChittalNo());
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    transferTo.setInstrumentNo2("POSTAGE_AMT"); // Added by nithya on 31-10-2019 for KD-680
                                    TxTransferTO.add(transferTo);
                                }
                                if (renewPostageAmt > 0.0) {
                                    txMap.put(TransferTrans.CR_AC_HD, postageAcHd);
                                    txMap.put("AUTHORIZEREMARKS", "MDS_POSTAGE");
                                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    //babu 26-May-2013  

                                    if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
                                   //     System.out.println("map.get=66====" + map.get("FROM_PARTICULARS_TRANSALL"));
                                        txMap.put(TransferTrans.PARTICULARS, map.get("FROM_PARTICULARS_TRANSALL"));
                                    } else {
                                        txMap.put(TransferTrans.PARTICULARS, "Installment Amt :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsCurrentReceiptEntryTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
                                    }
                                    txMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    txMap.put("TRANS_MOD_TYPE", "MDS");
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, renewPostageAmt);
                                    transferTo.setInitiatedBranch(BRANCH_ID);
                                    transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                    transferTo.setRec_mode(CommonUtil.convertObjToStr(map.get("REC_MODE")));
                                    transferTo.setGlTransActNum(mdsCurrentReceiptEntryTO.getChittalNo());
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    transferTo.setInstrumentNo2("RENEW_POSTAGE_AMT"); // Added by nithya on 31-10-2019 for KD-680
                                    TxTransferTO.add(transferTo);
                                }

                            }


                          //  System.out.println("transferTo List 5 : " + transferTo);
                        }
                        
                        // Thalayal - munnal - Full amount to subscription amount bonus transaction - Start
                        double thalayalMunnalBonusAmt = 0.0;
                        String thalayalMunnalBonusHead = "";
                        if(map.containsKey("THALAYAL_CHITTAL") && !map.get("THALAYAL_CHITTAL").equals("") && map.get("THALAYAL_CHITTAL").equals("Y")){
                         if(thalayalTransCategory.equalsIgnoreCase("FULLAMT_TO_SUBAMT")){
                               thalayalMunnalBonusAmt = mdsReceiptEntryTO.getBonusAmtPayable().doubleValue();
                               thalayalMunnalBonusHead = CommonUtil.convertObjToStr(applicationMap.get("THALAYAL_BONUS_HEAD"));
                         }
                        }else if(map.containsKey("MUNNAL_CHITTAL") && !map.get("MUNNAL_CHITTAL").equals("") && map.get("MUNNAL_CHITTAL").equals("Y")){
                         if(munnalTransCategory.equalsIgnoreCase("FULLAMT_TO_SUBAMT")){
                               thalayalMunnalBonusAmt = mdsReceiptEntryTO.getBonusAmtPayable().doubleValue();
                               thalayalMunnalBonusHead = CommonUtil.convertObjToStr(applicationMap.get("MUNNAL_BONUS_HEAD"));
                         }
                        }
                        thalayalMunnalBonusAmt = thalayalMunnalBonusAmt - bankBonusAmt;
                        if (thalayalMunnalBonusAmt > 0.0) {
                            txMap.put(TransferTrans.CR_AC_HD,thalayalMunnalBonusHead);
                            txMap.put("AUTHORIZEREMARKS", "THALAYAL_MUNNAL_BONUS");
                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
                                txMap.put(TransferTrans.PARTICULARS, map.get("FROM_PARTICULARS_TRANSALL"));
                            } else {
                                txMap.put(TransferTrans.PARTICULARS, "Installment Amt :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsCurrentReceiptEntryTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
                            }
                            txMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                            txMap.put("TRANS_MOD_TYPE", "MDS");
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, thalayalMunnalBonusAmt);
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                            transferTo.setRec_mode(CommonUtil.convertObjToStr(map.get("REC_MODE")));
                            transferTo.setGlTransActNum(mdsCurrentReceiptEntryTO.getChittalNo());
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            transferTo.setInstrumentNo2("THALAYAL_MUNNAL_BONUS"); // Added by nithya on 31-10-2019 for KD-680
                            TxTransferTO.add(transferTo);
                        }

                        // Thalayal - munnal - Full amount to subscription amount bonus transaction - End
                        
                        
                       // System.out.println("TxTransferTO List 5 : " + TxTransferTO);
                        HashMap applnMap = new HashMap();
                        transferDAO = new TransferDAO();
                        map.put("MODE", map.get("COMMAND"));
                        map.put("COMMAND", map.get("MODE"));
                        map.put("TxTransferTO", TxTransferTO);
                        transMap = transferDAO.execute(map, false);
                        mdsCurrentReceiptEntryTO.setNetTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                        mdsCurrentReceiptEntryTO.setPenalTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                        mdsCurrentReceiptEntryTO.setNoticeId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                        mdsCurrentReceiptEntryTO.setArbitrationId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                       // System.out.println("transactionDAO map : " + map);
                        transactionDAO.setBatchId(mdsCurrentReceiptEntryTO.getNetTransId());
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
                        commonTransactionCashandTransfer(map, mdsCurrentReceiptEntryTO, debitMap, applicationMap);
                        if(!waiveTransHappened){
                            if(penalWaiveAmt > 0){
                                commonTransactionWaiveOff(map, mdsCurrentReceiptEntryTO, debitMap, applicationMap, penalWaiveAmt, "PENAL",arcHead);
                            }if(noticeWaiveAmt > 0){
                                commonTransactionWaiveOff(map, mdsCurrentReceiptEntryTO, debitMap, applicationMap, noticeWaiveAmt, "NOTICE",arcHead);   
                            }if(arcWaiveAmt > 0){
                                commonTransactionWaiveOff(map, mdsCurrentReceiptEntryTO, debitMap, applicationMap, arcWaiveAmt, "ARC",arcHead);   
                            }
                            waiveTransHappened = true;
                        }
                        TxTransferTO = new ArrayList();
                    } else if (transactionTO.getTransType().equals("CASH")) {
                        transactionTO.setChequeNo("SERVICE_TAX");
                        HashMap cashTransMap = new HashMap();
                        cashTransMap.put("ACCT_FROM_BRANCH", mdsCurrentReceiptEntryTO.getBranchCode());
                        cashTransMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                        cashTransMap.put("MDS_GL_DETAILS", "MDS_GL_DETAILS");
                        cashTransMap.put("MDS_PENAL_DETAILS", "MDS_PENAL_DETAILS");
                        cashTransMap.put("MDS_NOTICE_DETAILS", "MDS_NOTICE_DETAILS");
                        cashTransMap.put("MDS_ARBITRATION_DETAILS", "MDS_ARBITRATION_DETAILS");
                        cashTransMap.put("LINK_BATCH_ID", mdsCurrentReceiptEntryTO.getChittalNo());
                        cashTransMap.put("RECEIPT_HEAD", applicationMap.get("RECEIPT_HEAD"));
                        cashTransMap.put("MDS_RECEIVABLE_HEAD", applicationMap.get("MDS_RECEIVABLE_HEAD"));
                        cashTransMap.put("PENAL_INTEREST_HEAD", applicationMap.get("PENAL_INTEREST_HEAD"));
                        cashTransMap.put("NOTICE_CHARGES_HEAD", applicationMap.get("NOTICE_CHARGES_HEAD"));
                        cashTransMap.put("CASE_EXPENSE_HEAD", applicationMap.get("CASE_EXPENSE_HEAD"));
                        cashTransMap.put("SCHEME_NAME", mdsCurrentReceiptEntryTO.getSchemeName());
                        cashTransMap.put("USER_ID", CommonUtil.convertObjToStr(map.get("USER_ID")));
                        cashTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                        cashTransMap.put("AUTHORIZE_REMARKS", cash_unique_id);
                        cashTransMap.put("TRANS_ALL_ID", CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                        cashTransMap.put("TRANS_MOD_TYPE", "MDS");
                        CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                        double finalReceivingAmt = CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNetAmt()).doubleValue()
                                - (CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getPenalAmtPayable()).doubleValue()
                                + CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNoticeAmt()).doubleValue()
                                + CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getArbitrationAmt()).doubleValue() + service_taxAmt);
//                        System.out.println("mdsCurrentReceiptEntryTOmdsCurrentReceiptEntryTO"+mdsCurrentReceiptEntryTO.getBonusAmtAvail()+":"+mdsCurrentReceiptEntryTO.getBonusAmtPayable());
//                        System.out.println("finalReceivingAmt +++ :"+finalReceivingAmt);
                        if(map.containsKey("BONUS_LIST_NEW") && map.get("BONUS_LIST_NEW") != null){
                            if(bankAdvInsAmt > 0){
                           //     finalReceivingAmt = finalReceivingAmt - CommonUtil.convertObjToDouble(map.get("BONUS_LIST_NEW"));
                            }
                        }
                        //Changed By Suresh
                        if (map.containsKey("MDS_CLOSURE")) {
                            cashTransMap.put("ACC_HEAD", applicationMap.get("MDS_RECEIVABLE_HEAD"));
                        } else {
                            cashTransMap.put("ACC_HEAD", applicationMap.get("RECEIPT_HEAD"));
                        }
                        //babu 26-May-2013  

                        if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
                           // System.out.println("map.get=999===" + map.get("FROM_PARTICULARS_TRANSALL"));
                            cashTransMap.put(TransferTrans.PARTICULARS, map.get("FROM_PARTICULARS_TRANSALL"));
                        } else {
                            cashTransMap.put("PARTICULARS", "Installment Amt ");
                        }
                      //  System.out.println("finalReceivingAmt-------------- :"+finalReceivingAmt);
                        cashTransMap.put("AMOUNT", String.valueOf(finalReceivingAmt));
                        cashTransMap.put("MP_MDS_CODE", applicationMap.get("MP_MDS_CODE"));
                        cashTransMap.put("TRANS_MOD_TYPE", "MDS");
                        cashTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                        cashTransMap.put("INSTRUMENT_NO2","INSTALMENT_AMT");// Added by nithya on 31-10-2019 for KD-680
                        ArrayList applncashList = commonApplicationList(cashTransMap);
                        //                        applncashList = applicationListMDS(cashTransMap);
                        cashTransMap.put("DAILYDEPOSITTRANSTO", applncashList);
                        cashTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                        cashTransMap.put("TRANS_ALL_ID", CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                        cashTransMap.put("TRANS_MOD_TYPE", "MDS");
                        cashTransMap.put("SINGLE_TRANS_ID", generateSingleTransId);
                        HashMap cashMap = cashTransactionDAO.execute(cashTransMap, false);
                        mdsCurrentReceiptEntryTO.setNetTransId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                        //System.out.println("cashMap :" + cashMap);
                        String command = CommonUtil.convertObjToStr(mdsReceiptEntryTO.getCommand());
                        transactionDAO.setBatchId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                        transactionDAO.setBatchDate(currDt);
                        map.put("MODE", "INSERT");
                        map.put("COMMAND", map.get("MODE"));
                        transactionDAO.execute(map);
                        if (CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNetAmt()).doubleValue() > 0) {
                            HashMap linkBatchMap = new HashMap();
                            if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")
                                    && transactionTO.getProductId().equals("") && !map.containsKey("LOCKER_SURRENDER_DAO")) {
                                linkBatchMap.put("LINK_BATCH_ID", cashMap.get("TRANS_ID"));
                            } else if (!map.containsKey("LOCKER_SURRENDER_DAO")) {
                                linkBatchMap.put("LINK_BATCH_ID", transactionTO.getDebitAcctNo());
                            } else {
                                linkBatchMap.put("LINK_BATCH_ID", map.get("LOCKER_SURRENDER_ID"));
                            }
                            linkBatchMap.put("LINK_BATCH_ID", cashMap.get("TRANS_ID"));
                            linkBatchMap.put("TRANS_ID", cashMap.get("TRANS_ID"));
                            linkBatchMap.put("TRANS_DT", currDt);
                            linkBatchMap.put("INITIATED_BRANCH", _branchCode);
                            sqlMap.executeUpdate("updateLinkBatchIdCash", linkBatchMap);
                        }

                        if (!penalTransHappened) {
                            if (CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getPenalAmtPayable()).doubleValue() - penalWaiveAmt > 0) {
                                cashTransMap.put("ACC_HEAD", applicationMap.get("PENAL_INTEREST_HEAD"));
                                //babu 26-May-2013  

                                if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
                                  //  System.out.println("map.get=12121====" + map.get("FROM_PARTICULARS_TRANSALL"));
                                    cashTransMap.put(TransferTrans.PARTICULARS, "Penal " + map.get("FROM_PARTICULARS_TRANSALL"));
                                } else {
                                    cashTransMap.put("PARTICULARS", "Penal ");
                                }
                                cashTransMap.put("AMOUNT", mdsCurrentReceiptEntryTO.getPenalAmtPayable() - penalWaiveAmt);
                                cashTransMap.put("MP_MDS_CODE", applicationMap.get("MP_MDS_CODE"));
                                cashTransMap.put("TRANS_MOD_TYPE", "MDS");
                                cashTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                cashTransMap.put("INSTRUMENT_NO2","PENAL_AMT");// Added by nithya on 31-10-2019 for KD-680
                                applncashList = commonApplicationList(cashTransMap);
                                cashTransMap.put("DAILYDEPOSITTRANSTO", applncashList);
                                cashTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                cashTransMap.put("AUTHORIZE_REMARKS", cash_unique_id);
                                cashMap = cashTransactionDAO.execute(cashTransMap, false);
                                mdsCurrentReceiptEntryTO.setPenalTransId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                                HashMap linkBatchMap = new HashMap();
                                if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")
                                        && transactionTO.getProductId().equals("") && !map.containsKey("LOCKER_SURRENDER_DAO")) {
                                    linkBatchMap.put("LINK_BATCH_ID", cashMap.get("TRANS_ID"));
                                } else if (!map.containsKey("LOCKER_SURRENDER_DAO")) {
                                    linkBatchMap.put("LINK_BATCH_ID", transactionTO.getDebitAcctNo());
                                } else {
                                    linkBatchMap.put("LINK_BATCH_ID", map.get("LOCKER_SURRENDER_ID"));
                                }
                                linkBatchMap.put("LINK_BATCH_ID", cashMap.get("TRANS_ID"));
                                linkBatchMap.put("TRANS_ID", cashMap.get("TRANS_ID"));
                                linkBatchMap.put("TRANS_DT", currDt);
                                linkBatchMap.put("INITIATED_BRANCH", _branchCode);
                                sqlMap.executeUpdate("updateLinkBatchIdCash", linkBatchMap);
                            }
                            //Notice Charge Amount Transaction Started
                            if (CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNoticeAmt()).doubleValue() - noticeWaiveAmt > 0) {
//                                cashTransMap.put("ACC_HEAD",applicationMap.get("NOTICE_CHARGES_HEAD"));
//                                cashTransMap.put("PARTICULARS","Notice ");
//                                cashTransMap.put("AMOUNT",mdsCurrentReceiptEntryTO.getNoticeAmt());
//                                cashTransMap.put("MP_MDS_CODE", applicationMap.get("MP_MDS_CODE"));
//                                applncashList = commonApplicationList(cashTransMap);
//                                cashTransMap.put("DAILYDEPOSITTRANSTO",applncashList);
//                                cashMap = cashTransactionDAO.execute(cashTransMap,false);
//                                mdsCurrentReceiptEntryTO.setNoticeId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
//                                HashMap linkBatchMap = new HashMap();
//                                if(!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")
//                                && transactionTO.getProductId().equals("") && !map.containsKey("LOCKER_SURRENDER_DAO")){
//                                    linkBatchMap.put("LINK_BATCH_ID",cashMap.get("TRANS_ID"));
//                                }else if(!map.containsKey("LOCKER_SURRENDER_DAO")){
//                                    linkBatchMap.put("LINK_BATCH_ID",transactionTO.getDebitAcctNo());
//                                }else{
//                                    linkBatchMap.put("LINK_BATCH_ID",map.get("LOCKER_SURRENDER_ID"));
//                                }
//                                linkBatchMap.put("LINK_BATCH_ID",cashMap.get("TRANS_ID"));
//                                linkBatchMap.put("TRANS_ID",cashMap.get("TRANS_ID"));
//                                linkBatchMap.put("TRANS_DT", currDt);
//                                linkBatchMap.put("INITIATED_BRANCH",_branchCode);
//                                sqlMap.executeUpdate("updateLinkBatchIdCash", linkBatchMap);
                                System.out.println("Notice Charge Started !!!! ");
                                HashMap whereMap = new HashMap();
                                whereMap.put("ACT_NUM", CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()) + "_" + CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getSubNo()));
                                List chargeList = (List) sqlMap.executeQueryForList("getMDSNoticeChargeDetails", whereMap);
                                if (chargeList != null && chargeList.size() > 0) {
                                    applncashList = new ArrayList();
                                    for (int i = 0; i < chargeList.size(); i++) {
                                        double chargeAmount = 0.0;
                                        String chargeType = "";
                                        whereMap = (HashMap) chargeList.get(i);
                                        chargeAmount = CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
                                        Rounding rod = new Rounding();
                                        chargeAmount = (double) rod.getNearest((long) (chargeAmount * 100), 100) / 100;
                                        chargeType = CommonUtil.convertObjToStr(whereMap.get("CHARGE_TYPE"));
                                        if (chargeAmount > 0) {
                                            if (chargeType.equals("NOTICE CHARGES")) {
                                                cashTransMap.put("ACC_HEAD", applicationMap.get("NOTICE_CHARGES_HEAD"));
                                            }
                                            if (chargeType.equals("POSTAGE CHARGES")) {
                                                cashTransMap.put("ACC_HEAD", applicationMap.get("POSTAGE_HEAD"));
                                            }
                                            //babu 26-May-2013  

                                            if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
                                               // System.out.println("map.get=2222222222222====" + map.get("FROM_PARTICULARS_TRANSALL"));
                                                cashTransMap.put(TransferTrans.PARTICULARS, chargeType + " " + map.get("FROM_PARTICULARS_TRANSALL"));
                                            } else {
                                                cashTransMap.put("PARTICULARS", chargeType);
                                            }
                                            cashTransMap.put("AMOUNT", String.valueOf(chargeAmount - noticeWaiveAmt));
                                            cashTransMap.put("MP_MDS_CODE", applicationMap.get("MP_MDS_CODE"));
                                            cashTransMap.put("AUTHORIZE_REMARKS", cash_unique_id);
                                            cashTransMap.put("TRANS_MOD_TYPE", "MDS");
                                            cashTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                            cashTransMap.put("GL_TRANS_ACT_NUM",mdsCurrentReceiptEntryTO.getChittalNo());
                                            cashTransMap.put("INSTRUMENT_NO2","NOTICE_CHARGE");// Added by nithya on 31-10-2019 for KD-680
                                            applncashList.add(createCashTransactionTO(cashTransMap, generateSingleTransId));
                                        }
                                    }
                                    cashTransMap.put("DAILYDEPOSITTRANSTO", applncashList);
                                    cashTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                    cashTransMap.put("TRANS_ALL_ID", CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                    cashTransMap.put("AUTHORIZE_REMARKS", cash_unique_id);
                                    cashTransMap.put("SINGLE_TRANS_ID", generateSingleTransId);
                                    cashMap = cashTransactionDAO.execute(cashTransMap, false);
                                    mdsCurrentReceiptEntryTO.setNoticeId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                                    HashMap linkBatchMap = new HashMap();
                                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")
                                            && transactionTO.getProductId().equals("") && !map.containsKey("LOCKER_SURRENDER_DAO")) {
                                        linkBatchMap.put("LINK_BATCH_ID", cashMap.get("TRANS_ID"));
                                    } else if (!map.containsKey("LOCKER_SURRENDER_DAO")) {
                                        linkBatchMap.put("LINK_BATCH_ID", transactionTO.getDebitAcctNo());
                                    } else {
                                        linkBatchMap.put("LINK_BATCH_ID", map.get("LOCKER_SURRENDER_ID"));
                                    }
                                    linkBatchMap.put("LINK_BATCH_ID", cashMap.get("TRANS_ID"));
                                    linkBatchMap.put("TRANS_ID", cashMap.get("TRANS_ID"));
                                    linkBatchMap.put("TRANS_DT", currDt);
                                    linkBatchMap.put("INITIATED_BRANCH", _branchCode);
                                    sqlMap.executeUpdate("updateLinkBatchIdCash", linkBatchMap);
                                }
                            }

                            //Case Expense Amount Transaction
                            if(arcWaiveAmt > 0){
                                arcHead = getARCHead(mdsCurrentReceiptEntryTO);
                            }
                            if (CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getArbitrationAmt()).doubleValue() - arcWaiveAmt > 0) {
                                System.out.println("Arbitration Started");
                                HashMap whereMap = new HashMap();
                                whereMap.put("ACT_NUM", CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()) + "_" + CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getSubNo()));
                                List chargeList = (List) sqlMap.executeQueryForList("getMDSCaseChargeDetails", whereMap);
                                if (chargeList != null && chargeList.size() > 0) {
                                    applncashList = new ArrayList();
                                    for (int i = 0; i < chargeList.size(); i++) {
                                        double chargeAmount = 0.0;
                                        String chargeType = "";
                                        whereMap = (HashMap) chargeList.get(i);
                                        chargeAmount = CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
                                        Rounding rod = new Rounding();
                                        chargeAmount = (double) rod.getNearest((long) (chargeAmount * 100), 100) / 100;
                                        chargeType = CommonUtil.convertObjToStr(whereMap.get("CHARGE_TYPE"));
                                        if (chargeAmount > 0) {
                                            if (chargeType.equals("ARC_COST")) {
                                                chargeType = "ARC_COST";
                                            } else if (chargeType.equals("ARC Expense")) {
                                                chargeType = "ARC_EXPENSE";
                                            } else if (chargeType.equals("EA Cost")) {
                                                chargeType = "EA_COST";
                                            } else if (chargeType.equals("EA Expense")) {
                                                chargeType = "EA_EXPENSE";
                                            } else if (chargeType.equals("EP_COST")) {
                                                chargeType = "EP_COST";
                                            } else if (chargeType.equals("EP Expense")) {
                                                chargeType = "EP_EXPENSE";
                                            } else if (chargeType.equals("ARBITRARY CHARGES")) {
                                                chargeType = "ARC_COST";
                                            } else if (chargeType.equals("EXECUTION DECREE CHARGES")) {
                                                chargeType = "EP_COST";
                                            } else if (chargeType.equals("MISCELLANEOUS CHARGES")) {
                                                chargeType = "MISCELLANEOUS_CHARGES";
                                            } else if (chargeType.equals("OTHER CHARGES")) {
                                                chargeType = "OTHER_CHARGES";
                                            } else if (chargeType.equals("LEGAL CHARGES")) {
                                                chargeType = "LEGAL_CHARGES";
                                            } else if (chargeType.equals("INSURANCE CHARGES")) {
                                                chargeType = "INSURANCE_CHARGES";
                                            }
                                            arcHead = chargeType;
                                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                                            System.out.println("###### chargeType Head : " + chargeType);
//                                            System.out.println("###### chargeAmount : " + chargeAmount);
//                                            System.out.println("###### chargeAmount Head2222: " + applicationMap.get(chargeType));
//                                            cashTransMap.put("ACC_HEAD",applicationMap.get("CASE_EXPENSE_HEAD"));
                                            cashTransMap.put("ACC_HEAD", applicationMap.get(chargeType));
                                            //babu 26-May-2013  

                                            if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
                                              //  System.out.println("map.get=3333333333333====" + map.get("FROM_PARTICULARS_TRANSALL"));
                                                cashTransMap.put(TransferTrans.PARTICULARS, chargeType + " " + map.get("FROM_PARTICULARS_TRANSALL"));
                                            } else {
                                                cashTransMap.put("PARTICULARS", chargeType);
                                            }
                                            cashTransMap.put("AMOUNT", String.valueOf(chargeAmount - arcWaiveAmt));
                                            cashTransMap.put("MP_MDS_CODE", applicationMap.get("MP_MDS_CODE"));
                                            cashTransMap.put("TRANS_ALL_ID", CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                            cashTransMap.put("AUTHORIZE_REMARKS", cash_unique_id);
                                            cashTransMap.put("TRANS_MOD_TYPE", "MDS");
                                            cashTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                            cashTransMap.put("GL_TRANS_ACT_NUM",mdsCurrentReceiptEntryTO.getChittalNo());
                                            cashTransMap.put("INSTRUMENT_NO2",chargeType);// Added by nithya on 31-10-2019 for KD-680
                                            applncashList.add(createCashTransactionTO(cashTransMap, generateSingleTransId));
                                        }
                                    }
                                    //added by chithra for service Tax
                                    if (serviceTaxMap != null && serviceTaxMap.size() > 0) {
                                        double swachhCess = 0.0;
                                        double krishikalyanCess = 0.0;
                                        double serTaxAmt = 0.0;    
                                        double normalServiceTax = 0.0;
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
                                      //  serTaxAmt -= (swachhCess + krishikalyanCess);
                                        if (swachhCess > 0) {
                                            cashTransMap.put("ACC_HEAD", serviceTaxMap.get("SWACHH_HEAD_ID"));
                                            if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
                                                cashTransMap.put(TransferTrans.PARTICULARS, "CGST " + map.get("FROM_PARTICULARS_TRANSALL"));
                                            } else {
                                                cashTransMap.put("PARTICULARS", "CGST");
                                            }
                                            cashTransMap.put("AMOUNT", swachhCess);
                                            cashTransMap.put("MP_MDS_CODE", applicationMap.get("MP_MDS_CODE"));
                                            cashTransMap.put("TRANS_ALL_ID", CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                            cashTransMap.put("AUTHORIZE_REMARKS", cash_unique_id);
                                            cashTransMap.put("TRANS_MOD_TYPE", "MDS");
                                            cashTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                            cashTransMap.put("GL_TRANS_ACT_NUM",mdsCurrentReceiptEntryTO.getChittalNo());
                                            cashTransMap.put("INSTRUMENT_NO2","CGST");// Added by nithya on 31-10-2019 for KD-680
                                            applncashList.add(createCashTransactionTO(cashTransMap, generateSingleTransId));
                                          //  serTaxAmt -= swachhCess;
                                        }
                                        if (krishikalyanCess > 0) {
                                            cashTransMap.put("ACC_HEAD", serviceTaxMap.get("KRISHIKALYAN_HEAD_ID"));
                                            if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
                                                cashTransMap.put(TransferTrans.PARTICULARS, "SGST " + map.get("FROM_PARTICULARS_TRANSALL"));
                                            } else {
                                                cashTransMap.put("PARTICULARS", "SGST");
                                            }
                                            cashTransMap.put("AMOUNT", krishikalyanCess);
                                            cashTransMap.put("MP_MDS_CODE", applicationMap.get("MP_MDS_CODE"));
                                            cashTransMap.put("TRANS_ALL_ID", CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                            cashTransMap.put("AUTHORIZE_REMARKS", cash_unique_id);
                                            cashTransMap.put("TRANS_MOD_TYPE", "MDS");
                                            cashTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                            cashTransMap.put("GL_TRANS_ACT_NUM",mdsCurrentReceiptEntryTO.getChittalNo());
                                            cashTransMap.put("INSTRUMENT_NO2","SGST");// Added by nithya on 31-10-2019 for KD-680
                                            applncashList.add(createCashTransactionTO(cashTransMap, generateSingleTransId));
                                           // serTaxAmt -= krishikalyanCess;
                                        }
                                        if (normalServiceTax > 0) {
                                            cashTransMap.put("ACC_HEAD", serviceTaxMap.get("TAX_HEAD_ID"));
                                            if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
                                                cashTransMap.put(TransferTrans.PARTICULARS, "Service Tax " + map.get("FROM_PARTICULARS_TRANSALL"));
                                            } else {
                                                cashTransMap.put("PARTICULARS", "Service_Tax");
                                            }
                                            cashTransMap.put("AMOUNT", normalServiceTax);
                                            cashTransMap.put("MP_MDS_CODE", applicationMap.get("MP_MDS_CODE"));
                                            cashTransMap.put("TRANS_ALL_ID", CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                            cashTransMap.put("AUTHORIZE_REMARKS", cash_unique_id);
                                            cashTransMap.put("TRANS_MOD_TYPE", "MDS");
                                            cashTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                            cashTransMap.put("GL_TRANS_ACT_NUM",mdsCurrentReceiptEntryTO.getChittalNo());
                                            cashTransMap.put("INSTRUMENT_NO2","SERVICE_TAX");// Added by nithya on 31-10-2019 for KD-680
                                            applncashList.add(createCashTransactionTO(cashTransMap, generateSingleTransId));
                                        }
                                    }
                                    cashTransMap.put("DAILYDEPOSITTRANSTO", applncashList);
                                    cashTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                    cashTransMap.put("TRANS_ALL_ID", CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                    cashTransMap.put("SINGLE_TRANS_ID", generateSingleTransId);
                                    cashMap = cashTransactionDAO.execute(cashTransMap, false);
                                    mdsCurrentReceiptEntryTO.setArbitrationId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                                    HashMap linkBatchMap = new HashMap();
                                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")
                                            && transactionTO.getProductId().equals("") && !map.containsKey("LOCKER_SURRENDER_DAO")) {
                                        linkBatchMap.put("LINK_BATCH_ID", cashMap.get("TRANS_ID"));
                                    } else if (!map.containsKey("LOCKER_SURRENDER_DAO")) {
                                        linkBatchMap.put("LINK_BATCH_ID", transactionTO.getDebitAcctNo());
                                    } else {
                                        linkBatchMap.put("LINK_BATCH_ID", map.get("LOCKER_SURRENDER_ID"));
                                    }
                                    linkBatchMap.put("LINK_BATCH_ID", cashMap.get("TRANS_ID"));
                                    linkBatchMap.put("TRANS_ID", cashMap.get("TRANS_ID"));
                                    linkBatchMap.put("TRANS_DT", currDt);
                                    linkBatchMap.put("INITIATED_BRANCH", _branchCode);
                                    sqlMap.executeUpdate("updateLinkBatchIdCash", linkBatchMap);
                                }

                            }


//                            if(CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getArbitrationAmt()).doubleValue()>0){
//                                cashTransMap.put("ACC_HEAD",applicationMap.get("CASE_EXPENSE_HEAD"));
//                                cashTransMap.put("PARTICULARS","Arbitration ");
//                                cashTransMap.put("AMOUNT",mdsCurrentReceiptEntryTO.getArbitrationAmt());
//                                cashTransMap.put("MP_MDS_CODE", applicationMap.get("MP_MDS_CODE"));
//                                applncashList = commonApplicationList(cashTransMap);
//                                cashTransMap.put("DAILYDEPOSITTRANSTO",applncashList);
//                                cashMap = cashTransactionDAO.execute(cashTransMap,false);
//                                mdsCurrentReceiptEntryTO.setArbitrationId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
//                                HashMap linkBatchMap = new HashMap();
//                                if(!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")
//                                && transactionTO.getProductId().equals("") && !map.containsKey("LOCKER_SURRENDER_DAO")){
//                                    linkBatchMap.put("LINK_BATCH_ID",cashMap.get("TRANS_ID"));
//                                }else if(!map.containsKey("LOCKER_SURRENDER_DAO")){
//                                    linkBatchMap.put("LINK_BATCH_ID",transactionTO.getDebitAcctNo());
//                                }else{
//                                    linkBatchMap.put("LINK_BATCH_ID",map.get("LOCKER_SURRENDER_ID"));
//                                }
//                                linkBatchMap.put("LINK_BATCH_ID",cashMap.get("TRANS_ID"));
//                                linkBatchMap.put("TRANS_ID",cashMap.get("TRANS_ID"));
//                                linkBatchMap.put("TRANS_DT", currDt);
//                                linkBatchMap.put("INITIATED_BRANCH",_branchCode);
//                                sqlMap.executeUpdate("updateLinkBatchIdCash", linkBatchMap);
//                            }
                            penalTransHappened = true;
                        }
                        commonTransactionCashandTransfer(map, mdsCurrentReceiptEntryTO, debitMap, applicationMap);
                        if(!waiveTransHappened){
                            if(penalWaiveAmt > 0){
                                commonTransactionWaiveOff(map, mdsCurrentReceiptEntryTO, debitMap, applicationMap, penalWaiveAmt, "PENAL",arcHead);
                            }if(noticeWaiveAmt > 0){
                                commonTransactionWaiveOff(map, mdsCurrentReceiptEntryTO, debitMap, applicationMap, noticeWaiveAmt, "NOTICE",arcHead);   
                            }if(arcWaiveAmt > 0){
                                commonTransactionWaiveOff(map, mdsCurrentReceiptEntryTO, debitMap, applicationMap, arcWaiveAmt, "ARC",arcHead);   
                            }
                            waiveTransHappened = true;
                        }
                    }
                    map.put("CURRENT_NET_TRANS_ID", mdsCurrentReceiptEntryTO.getNetTransId());
                    map.put("CURRENT_BONUS_TRANS_ID", mdsCurrentReceiptEntryTO.getBonusTransId());
                    map.put("CURRENT_PENAL_TRANS_ID", mdsCurrentReceiptEntryTO.getPenalTransId());
                    map.put("CURRENT_DISCOUNT_TRANS_ID", mdsCurrentReceiptEntryTO.getDiscountTransId());
                    map.put("CURRENT_NOTICE_TRANS_ID", mdsCurrentReceiptEntryTO.getNoticeId());
                    map.put("CURRENT_ARBITRATION_TRANS_ID", mdsCurrentReceiptEntryTO.getArbitrationId());
                    System.out.println("###### map : " + map);
                }
            }
//            String USERID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
//            sqlMap.executeUpdate("insertReceiptEntryTO", mdsReceiptEntryTO);
//            System.out.println("$#@$#$@# mdsReceiptEntryTO :"+mdsReceiptEntryTO);
//            setTransDetails(mdsReceiptEntryTO);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
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

    private void insertBankAdvData(HashMap map, MDSReceiptEntryTO mdsBankAdvReceiptEntryTO, LogDAO objLogDAO, LogTO objLogTO, String cash_unique_id) throws Exception {
        try {


            System.out.println("###### MDS BANK ADVANCE ###### : "+map);
            HashMap waiveMap = null;
            double penalWaiveAmt = 0.0;
            double arcWaiveAmt = 0.0;
            double noticeWaiveAmt = 0.0;
            double totalWaiveAmt = 0.0;
            String arcHead = "";
            mdsBankAdvReceiptEntryTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
            mdsBankAdvReceiptEntryTO.setStatus(CommonConstants.STATUS_CREATED);
            mdsBankAdvReceiptEntryTO.setStatusDt(currDt);
           // System.out.println("###### insertTransactionData 22: " + map);
            ArrayList transferList = new ArrayList();
            TransferTrans transferTrans = new TransferTrans();
            TxTransferTO transferTo = new TxTransferTO();
            //ArrayList TxTransferTO = new ArrayList();
            TransactionTO transactionTO = new TransactionTO();
            HashMap txMap = new HashMap();
            HashMap transactionListMap = new HashMap();
            String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
            transferTrans.setInitiatedBranch(BRANCH_ID);
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            transactionDAO.setInitiatedBranch(_branchCode);
            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
            if(map.containsKey("WAIVE_MAP") && map.get("WAIVE_MAP") != null){
                waiveMap =(HashMap) map.get("WAIVE_MAP");
                penalWaiveAmt = CommonUtil.convertObjToDouble(waiveMap.get("PENAL_AMOUNT"));
                arcWaiveAmt = CommonUtil.convertObjToDouble(waiveMap.get("ARC_AMOUNT"));
                noticeWaiveAmt = CommonUtil.convertObjToDouble(waiveMap.get("NOTICE_AMOUNT"));
                totalWaiveAmt = CommonUtil.convertObjToDouble(waiveMap.get("Total_WaiveAmt"));
            }
            if (map.containsKey("TransactionTO")) {
                HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                }
                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                HashMap applicationMap = new HashMap();
                applicationMap.put("SCHEME_NAME", mdsBankAdvReceiptEntryTO.getSchemeName());
                List lst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", applicationMap);
                if (lst != null && lst.size() > 0) {
                    applicationMap = (HashMap) lst.get(0);
                }
                if (transactionTO.getTransType().equals("TRANSFER") || transactionTO.getTransType().equals("CASH")) {
                    String linkBatchId = "";
                    double postageAmt = 0.0;
                    double renewPostageAmt = 0.0;
                    String postageAcHd = "";
                    if (map.containsKey("POSTAGE_AMT_FOR_INT") || map.containsKey("RENEW_POSTAGE_AMT")) {
                        postageAmt = CommonUtil.convertObjToDouble(map.get("POSTAGE_AMT_FOR_INT")).doubleValue();
                        renewPostageAmt = CommonUtil.convertObjToDouble(map.get("RENEW_POSTAGE_AMT")).doubleValue();
                        postageAcHd = CommonUtil.convertObjToStr(map.get("POSTAGE_ACHD"));
                    }
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
                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));
                 //   System.out.println("###### insertTransactionData mdsBankAdvReceiptEntryTO : " + mdsBankAdvReceiptEntryTO);
                    if (transactionTO.getTransType().equals("TRANSFER")) {
                        if (CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getNetAmt()).doubleValue() > 0) {
                         //   System.out.println("Transfer Started debit : ");
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL") && transactionTO.getProductId().equals("")) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("OA")) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                                txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                            } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("AB")) { // Added by nithya on 23-10-2019 for KD-679
                                txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("ACCT_HEAD"));
                                txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);
                            }else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("AD")) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("ACCT_HEAD"));
                                txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.ADVANCES);
                            }else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("SA")) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                                txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.SUSPENSE);
                            }
                            if (transactionTO.getProductType().equals("OA")) {
                                txMap.put("TRANS_MOD_TYPE", "OA");
                            } else if (transactionTO.getProductType().equals("AB")) {
                                txMap.put("TRANS_MOD_TYPE", "AB");
                            } else if (transactionTO.getProductType().equals("TD")) {
                                txMap.put("TRANS_MOD_TYPE", "TD");
                            } else if (transactionTO.getProductType().equals("SA")) {
                                txMap.put("TRANS_MOD_TYPE", "SA");
                            } else if (transactionTO.getProductType().equals("TL")) {
                                txMap.put("TRANS_MOD_TYPE", "TL");
                            } else if (transactionTO.getProductType().equals("AD")) {
                                txMap.put("TRANS_MOD_TYPE", "AD");
                            } else {
                                txMap.put("TRANS_MOD_TYPE", "GL");
                            }
                            txMap.put(TransferTrans.PARTICULARS, "Installment Amt :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsBankAdvReceiptEntryTO.getChittalNo() + "_" + mdsBankAdvReceiptEntryTO.getSubNo());
                            txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getNetAmt()).doubleValue() + CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getForfeitBonusAmtPayable()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt(currDt);
                            transferTo.setBranchId(BRANCH_ID);
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setGlTransActNum(mdsBankAdvReceiptEntryTO.getChittalNo());
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            transferTo.setNarration(transactionTO.getParticulars());//KD-3393
                            if (map.containsKey("REC_MODE")) {
                                transferTo.setRec_mode(CommonUtil.convertObjToStr(map.get("REC_MODE")));
                            }
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));

                            TxTransferTO.add(transferTo);
                            //System.out.println("transferTo List 1 : " + transferTo);
                        }

//                        double finalReceivingAmt = bankAdvInsAmt;
                        if (CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getNetAmt()).doubleValue() > 0) {
                            System.out.println("Final Amount Transaction Started");
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("BANKING_REP_PAY_HEAD"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, mdsBankAdvReceiptEntryTO.getBranchCode());//BRANCH_ID);
                            txMap.put(TransferTrans.PARTICULARS, "Installment Amt :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsBankAdvReceiptEntryTO.getChittalNo() + "_" + mdsBankAdvReceiptEntryTO.getSubNo() + ":BANK ADVANCE");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            txMap.put("TRANS_MOD_TYPE_MMBS", "MDS");
                            if (map.containsKey("POSTAGE_AMT_FOR_INT") || map.containsKey("RENEW_POSTAGE_AMT")) {
                                if (map.containsKey("POSTAGE_AMT_FOR_INT")) {
                                    if (postageAmt > 0.0) {
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getNetAmt()).doubleValue() - postageAmt);
                                    }
                                }
                                if (map.containsKey("RENEW_POSTAGE_AMT")) {
                                    if (renewPostageAmt > 0.0) {
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getNetAmt()).doubleValue() - renewPostageAmt);
                                    }
                                }
                            } else {
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getNetAmt()).doubleValue());
                            }
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            transferTo.setInstrumentNo2("INSTALMENT_AMT");// Added by nithya on 31-10-2019 for KD-680
                            TxTransferTO.add(transferTo);
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt(currDt);
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setGlTransActNum(mdsBankAdvReceiptEntryTO.getChittalNo());

                            if (map.containsKey("REC_MODE")) {
                                transferTo.setRec_mode(CommonUtil.convertObjToStr(map.get("REC_MODE")));
                            }
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));

                            if (map.containsKey("POSTAGE_AMT_FOR_INT") || map.containsKey("RENEW_POSTAGE_AMT")) {
                                if (postageAmt > 0.0) {
                                    txMap.put(TransferTrans.CR_AC_HD, postageAcHd);
                                    txMap.put("AUTHORIZEREMARKS", "MDS_POSTAGE");
                                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, "Installment Amt :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsBankAdvReceiptEntryTO.getChittalNo() + "_" + mdsBankAdvReceiptEntryTO.getSubNo() + ":BANK ADVANCE");
                                    txMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    txMap.put("TRANS_MOD_TYPE_MMBS", "MDS");

                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, postageAmt);
                                    transferTo.setInitiatedBranch(BRANCH_ID);
                                    transferTo.setInstrumentNo2("POSTAGE_AMT");// Added by nithya on 31-10-2019 for KD-680
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                }
                                if (renewPostageAmt > 0.0) {
                                    txMap.put(TransferTrans.CR_AC_HD, postageAcHd);
                                    txMap.put("AUTHORIZEREMARKS", "MDS_POSTAGE");
                                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, "Installment Amt :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsBankAdvReceiptEntryTO.getChittalNo() + "_" + mdsBankAdvReceiptEntryTO.getSubNo() + ":BANK ADVANCE");
                                    txMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    txMap.put("TRANS_MOD_TYPE_MMBS", "MDS");

                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, renewPostageAmt);
                                    transferTo.setInstrumentNo2("RENEW_POSTAGE_AMT");// Added by nithya on 31-10-2019 for KD-680
                                    transferTo.setInitiatedBranch(BRANCH_ID);
                                }
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setGlTransActNum(mdsBankAdvReceiptEntryTO.getChittalNo());
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                            }


                          //  System.out.println("transferTo List 5 : " + transferTo);
                        }
                        if (!penalTransHappened) {
                            if (CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getPenalAmtPayable()).doubleValue() - penalWaiveAmt > 0) {
                                System.out.println("penal Started");
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_AC_HD, (String) applicationMap.get("PENAL_INTEREST_HEAD"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_BRANCH, mdsBankAdvReceiptEntryTO.getBranchCode());//BRANCH_ID);
                                txMap.put(TransferTrans.PARTICULARS, "Penal :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsBankAdvReceiptEntryTO.getChittalNo() + "_" + mdsBankAdvReceiptEntryTO.getSubNo());
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put("TRANS_MOD_TYPE_MMBS", "MDS");
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getPenalAmtPayable()).doubleValue() - penalWaiveAmt);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setTransDt(currDt);
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));

                                if (map.containsKey("REC_MODE")) {
                                    transferTo.setRec_mode(CommonUtil.convertObjToStr(map.get("REC_MODE")));
                                }
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));
                                transactionTO.setChequeNo("SERVICE_TAX");
                                transferTo.setGlTransActNum(mdsBankAdvReceiptEntryTO.getChittalNo());
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                transferTo.setInstrumentNo2("PENAL_AMT");// Added by nithya on 31-10-2019 for KD-680
                                TxTransferTO.add(transferTo);
                              //  System.out.println("transferTo List 2 : " + transferTo);
                            }
                            //Notice Charge Amount Transaction Started
                            if (CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getNoticeAmt()).doubleValue() - noticeWaiveAmt > 0) {
                                System.out.println("Notice Charge Started !!!");
                                HashMap whereMap = new HashMap();
                                List chargeList = null;
                                whereMap.put("ACT_NUM", CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()) + "_" + CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getSubNo()));
                                if (map.containsKey("INT_CALC_UPTO_DT")) {        // FROM RECOVERY LIST
                                    whereMap.put("INT_CALC_UPTO_DT", map.get("INT_CALC_UPTO_DT"));
                                    chargeList = (List) sqlMap.executeQueryForList("getMDSRecoveryNoticeChargeDetails", whereMap);
                                } else {
                                    chargeList = (List) sqlMap.executeQueryForList("getMDSNoticeChargeDetails", whereMap);
                                }
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
                                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                            if (chargeType.equals("POSTAGE CHARGES")) {
                                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("POSTAGE_HEAD"));
                                            } else {
                                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("NOTICE_CHARGES_HEAD"));
                                            }
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.CR_BRANCH, mdsBankAdvReceiptEntryTO.getBranchCode());//BRANCH_ID);
                                            txMap.put(TransferTrans.PARTICULARS, chargeType + applicationMap.get(" MP_MDS_CODE") + "-" + mdsBankAdvReceiptEntryTO.getChittalNo() + "_" + mdsBankAdvReceiptEntryTO.getSubNo());
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                            txMap.put("TRANS_MOD_TYPE_MMBS", "MDS");
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, chargeAmount - noticeWaiveAmt);
                                            transferTo.setTransId("-");
                                            transferTo.setBatchId("-");
                                            transferTo.setTransDt(currDt);
                                            transferTo.setInitiatedBranch(BRANCH_ID);
                                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                            transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                            transferTo.setRec_mode(CommonUtil.convertObjToStr(map.get("REC_MODE")));

                                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));
                                            transactionTO.setChequeNo("SERVICE_TAX");
                                            transferTo.setGlTransActNum(mdsBankAdvReceiptEntryTO.getChittalNo());
                                            transferTo.setSingleTransId(generateSingleTransId);
                                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                            transferTo.setInstrumentNo2("NOTICE_CHARGE");// Added by nithya on 31-10-2019 for KD-680
                                            TxTransferTO.add(transferTo);
                                            System.out.println("Notice Charge transferTo List  : " + i + " " + transferTo);
                                        }
                                    }
                                }
                            }
                            //Case Expense Amount Transaction
                            if(arcWaiveAmt > 0){
                                arcHead = getARCHead(mdsBankAdvReceiptEntryTO);
                            }
                            if (CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getArbitrationAmt()).doubleValue() - arcWaiveAmt > 0) {
                                HashMap whereMap = new HashMap();
                                List chargeList = null;
                                whereMap.put("ACT_NUM", CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()) + "_" + CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getSubNo()));
                                if (map.containsKey("INT_CALC_UPTO_DT")) {        // FROM RECOVERY LIST
                                    whereMap.put("INT_CALC_UPTO_DT", map.get("INT_CALC_UPTO_DT"));
                                    chargeList = (List) sqlMap.executeQueryForList("getMDSRecoveryCaseChargeDetails", whereMap);
                                } else {
                                    chargeList = (List) sqlMap.executeQueryForList("getMDSCaseChargeDetails", whereMap);
                                }
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
                                            if (chargeType.equals("ARC_COST")) {
                                                chargeType = "ARC_COST";
                                            } else if (chargeType.equals("ARC Expense")) {
                                                chargeType = "ARC_EXPENSE";
                                            } else if (chargeType.equals("EA Cost")) {
                                                chargeType = "EA_COST";
                                            } else if (chargeType.equals("EA Expense")) {
                                                chargeType = "EA_EXPENSE";
                                            } else if (chargeType.equals("EP_COST")) {
                                                chargeType = "EP_COST";
                                            } else if (chargeType.equals("EP Expense")) {
                                                chargeType = "EP_EXPENSE";
                                            } else if (chargeType.equals("ARBITRARY CHARGES")) {
                                                chargeType = "ARC_COST";
                                            } else if (chargeType.equals("EXECUTION DECREE CHARGES")) {
                                                chargeType = "EP_COST";
                                            } else if (chargeType.equals("MISCELLANEOUS CHARGES")) {
                                                chargeType = "MISCELLANEOUS_CHARGES";
                                            } else if (chargeType.equals("OTHER CHARGES")) {
                                                chargeType = "OTHER_CHARGES";
                                            } else if (chargeType.equals("LEGAL CHARGES")) {
                                                chargeType = "LEGAL_CHARGES";
                                            } else if (chargeType.equals("INSURANCE CHARGES")) {
                                                chargeType = "INSURANCE_CHARGES";
                                            }
                                            arcHead = chargeType;
                                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                                            System.out.println("###### chargeType Head : " + chargeType);
//                                            System.out.println("###### chargeAmount : " + chargeAmount);
//                                            System.out.println("###### chargeAmount Head2222: " + applicationMap.get(chargeType));
//                                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("CASE_EXPENSE_HEAD"));
                                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get(chargeType));
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.CR_BRANCH, mdsBankAdvReceiptEntryTO.getBranchCode());//BRANCH_ID);
                                            txMap.put(TransferTrans.PARTICULARS, chargeType + applicationMap.get(" MP_MDS_CODE") + "-" + mdsBankAdvReceiptEntryTO.getChittalNo() + "_" + mdsBankAdvReceiptEntryTO.getSubNo());
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                            txMap.put("TRANS_MOD_TYPE_MMBS", "MDS");
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, chargeAmount - arcWaiveAmt);
                                            transferTo.setTransId("-");
                                            transferTo.setBatchId("-");
                                            transferTo.setTransDt(currDt);
                                            transferTo.setInitiatedBranch(BRANCH_ID);
                                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                            transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                            transferTo.setRec_mode(CommonUtil.convertObjToStr(map.get("REC_MODE")));

                                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));
                                            transactionTO.setChequeNo("SERVICE_TAX");
                                            transferTo.setGlTransActNum(mdsBankAdvReceiptEntryTO.getChittalNo());
                                            transferTo.setSingleTransId(generateSingleTransId);
                                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                            transferTo.setInstrumentNo2(chargeType);// Added by nithya on 31-10-2019 for KD-680
                                            TxTransferTO.add(transferTo);
                                           // System.out.println("Arbitration transferTo List  : " + i + " " + transferTo);
                                        }
                                    }
                                }
                            }
                            penalTransHappened = true;
                        }
                        if(!waiveTransHappened){
                            if(penalWaiveAmt > 0){
                                commonTransactionWaiveOff(map, mdsBankAdvReceiptEntryTO, debitMap, applicationMap, penalWaiveAmt, "PENAL",arcHead);
                            }if(noticeWaiveAmt > 0){
                                commonTransactionWaiveOff(map, mdsBankAdvReceiptEntryTO, debitMap, applicationMap, noticeWaiveAmt, "NOTICE",arcHead);   
                            }if(arcWaiveAmt > 0){
                                commonTransactionWaiveOff(map, mdsBankAdvReceiptEntryTO, debitMap, applicationMap, arcWaiveAmt, "ARC",arcHead);   
                            }
                            waiveTransHappened = true;
                        }
//                        if(bankBonusAmt>0){
                        // The following block is for already transaction happened for Bank Advance Bonus and now not eligible
                        
                        System.out.println("bonusreversal-- :"+bonusreversal);
                        if (bonusreversal) {
                            System.out.println("Final Amount Transaction Started");
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            //Added By Suresh
                            if (map.containsKey("MDS_CLOSURE")) {
                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("SUNDRY_RECEIPT_HEAD"));
                            } else {
                               // txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("BONUS_PAYABLE_HEAD"));
                                 txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("MDS_FORFEITED_ACCT_HEAD")); // Head change done by nithya on 16-07-2020 for KD-2093
                            }
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, mdsBankAdvReceiptEntryTO.getBranchCode());//BRANCH_ID);
                            txMap.put(TransferTrans.PARTICULARS, "Bonus Reversal :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsBankAdvReceiptEntryTO.getChittalNo() + "_" + mdsBankAdvReceiptEntryTO.getSubNo() + ":BANK ADVANCE");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            txMap.put("TRANS_MOD_TYPE_MMBS", "MDS");
//                            System.out.println("txMap5 : " + txMap + "bankBonusAmt :" + CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getBonusAmtPayable()).doubleValue());
//                            System.out.println("bankBonusReverseAmt" + bankBonusReverseAmt);
                            // transferTo =  transactionDAO.addTransferCreditLocal(txMap, bankBonusReverseAmt);
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getForfeitBonusAmtPayable()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt(currDt);
                            transferTo.setInstrumentNo2("BONUS_REVERSAL");// Added by nithya on 31-10-2019 for KD-680
                            transferTo.setGlTransActNum(mdsBankAdvReceiptEntryTO.getChittalNo());
                            transferTo.setSingleTransId(generateSingleTransId);
                           // transferTo.setInitiatedBranch(mdsBankAdvReceiptEntryTO.getBranchCode());//BRANCH_ID);//bb1
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                            transferTo.setRec_mode(CommonUtil.convertObjToStr(map.get("REC_MODE")));

                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            TxTransferTO.add(transferTo);
//                            System.out.println("transferTo List 5 : " + transferTo);
                        }
                        System.out.println("TxTransferTO List 5 : " + TxTransferTO);
                        HashMap applnMap = new HashMap();
                        /*    transferDAO = new TransferDAO();
                        map.put("MODE", map.get("COMMAND"));
                        map.put("COMMAND", map.get("MODE"));
                        map.put("TxTransferTO", TxTransferTO);
                        map.put("TRANS_MOD_TYPE_MMBS", CommonConstants.MMBS);
                        transMap = transferDAO.execute(map, false);
                        mdsBankAdvReceiptEntryTO.setNetTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                        System.out.println("transactionDAO map : " + map);
                        transactionDAO.setBatchId(mdsBankAdvReceiptEntryTO.getNetTransId());
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
                        sqlMap.executeUpdate("updateLinkBatchIdTransfer", linkBatchMap);*/
                    //    commonTransactionTransferBankAdvBonus(map, mdsBankAdvReceiptEntryTO, debitMap,applicationMap);//added on 27/7/2015 Nidhin
                    } else if (transactionTO.getTransType().equals("CASH")) {
                        if (map.containsKey("MEMBER_RECEIPT_SINGLE_ID") && map.get("MEMBER_RECEIPT_SINGLE_ID") != null) {
                            generateSingleTransId = CommonUtil.convertObjToStr(map.get("MEMBER_RECEIPT_SINGLE_ID"));
                        } else {
                            generateSingleTransId = generateLinkID();
                        }
                        transactionTO.setChequeNo("SERVICE_TAX");
                        HashMap cashTransMap = new HashMap();
                        cashTransMap.put("ACCT_FROM_BRANCH", mdsBankAdvReceiptEntryTO.getBranchCode());
                        cashTransMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                        cashTransMap.put("MDS_GL_DETAILS", "MDS_GL_DETAILS");
                        cashTransMap.put("MDS_PENAL_DETAILS", "MDS_PENAL_DETAILS");
                        cashTransMap.put("MDS_NOTICE_DETAILS", "MDS_NOTICE_DETAILS");
                        cashTransMap.put("MDS_ARBITRATION_DETAILS", "MDS_ARBITRATION_DETAILS");
                        cashTransMap.put("LINK_BATCH_ID", mdsBankAdvReceiptEntryTO.getChittalNo());
                        cashTransMap.put("RECEIPT_HEAD", applicationMap.get("RECEIPT_HEAD"));
                        cashTransMap.put("PENAL_INTEREST_HEAD", applicationMap.get("PENAL_INTEREST_HEAD"));
                        cashTransMap.put("NOTICE_CHARGES_HEAD", applicationMap.get("NOTICE_CHARGES_HEAD"));
                        cashTransMap.put("CASE_EXPENSE_HEAD", applicationMap.get("CASE_EXPENSE_HEAD"));
                        cashTransMap.put("BANKING_REP_PAY_HEAD", applicationMap.get("BANKING_REP_PAY_HEAD"));    // BANK ADVANCE ACC HEAD
                        cashTransMap.put("SCHEME_NAME", mdsBankAdvReceiptEntryTO.getSchemeName());
                        cashTransMap.put("USER_ID", CommonUtil.convertObjToStr(map.get("USER_ID")));
                        cashTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                        cashTransMap.put("AUTHORIZE_REMARKS", cash_unique_id);
                        cashTransMap.put("TRANS_ALL_ID", CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                        cashTransMap.put("TRANS_MOD_TYPE", "MDS");
                        CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                        double finalReceivingAmt = CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getNetAmt()).doubleValue();
                        cashTransMap.put("ACC_HEAD", applicationMap.get("BANKING_REP_PAY_HEAD"));
                        cashTransMap.put("PARTICULARS", "Installment Amt ");
                        cashTransMap.put("AMOUNT", String.valueOf(finalReceivingAmt));
                        cashTransMap.put("INSTRUMENT_NO2","INSTALMENT_AMT");
                        ArrayList applncashList = commonApplicationList(cashTransMap);
                        cashTransMap.put("DAILYDEPOSITTRANSTO", applncashList);
                        cashTransMap.put("SINGLE_TRANS_ID", generateSingleTransId);
                        cashTransMap.put("TRANS_MOD_TYPE_MMBS", CommonConstants.MMBS);
                        HashMap cashMap = cashTransactionDAO.execute(cashTransMap, false);
                        mdsBankAdvReceiptEntryTO.setNetTransId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                      //  System.out.println("cashMap :" + cashMap);
                        String command = CommonUtil.convertObjToStr(mdsReceiptEntryTO.getCommand());
                        transactionDAO.setBatchId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                        transactionDAO.setBatchDate(currDt);
                        map.put("MODE", "INSERT");
                        map.put("COMMAND", map.get("MODE"));
                        transactionDAO.execute(map);
                     //   System.out.println("afeter insatall pay" + (mdsBankAdvReceiptEntryTO.getNetAmt()).doubleValue());;
                        if (CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getNetAmt()).doubleValue() > 0) {
                            HashMap linkBatchMap = new HashMap();
                            if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")
                                    && transactionTO.getProductId().equals("") && !map.containsKey("LOCKER_SURRENDER_DAO")) {
                                linkBatchMap.put("LINK_BATCH_ID", cashMap.get("TRANS_ID"));
                            } else if (!map.containsKey("LOCKER_SURRENDER_DAO")) {
                                linkBatchMap.put("LINK_BATCH_ID", transactionTO.getDebitAcctNo());
                            } else {
                                linkBatchMap.put("LINK_BATCH_ID", map.get("LOCKER_SURRENDER_ID"));
                            }
                            linkBatchMap.put("LINK_BATCH_ID", cashMap.get("TRANS_ID"));
                            linkBatchMap.put("TRANS_ID", cashMap.get("TRANS_ID"));
                            linkBatchMap.put("TRANS_DT", currDt);
                            linkBatchMap.put("INITIATED_BRANCH", _branchCode);
                            sqlMap.executeUpdate("updateLinkBatchIdCash", linkBatchMap);
                        }
                     //   System.out.println("Trans ID 1 :: " + cashMap.get("TRANS_ID"));
//                        double finalBankAdvInsAmt = bankAdvInsAmt;
//                        if(finalBankAdvInsAmt>0){
//                            if(CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getNetAmt()).doubleValue()>0){
//                                cashTransMap.put("ACC_HEAD",applicationMap.get("BANKING_REP_PAY_HEAD"));
//                                cashTransMap.put("PARTICULARS","");
//                                cashTransMap.put("AMOUNT",String.valueOf(CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getNetAmt()).doubleValue()));
//                                System.out.println("####### cashTransMap : "+cashTransMap);
//                                applncashList = commonApplicationList(cashTransMap);
//                                cashTransMap.put("DAILYDEPOSITTRANSTO",applncashList);
//                                cashMap =cashTransactionDAO.execute(cashTransMap,false);
//                                mdsBankAdvReceiptEntryTO.setNetTransId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
//                                HashMap linkBatchMap = new HashMap();
//                                if(!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")
//                                && transactionTO.getProductId().equals("") && !map.containsKey("LOCKER_SURRENDER_DAO")){
//                                    linkBatchMap.put("LINK_BATCH_ID",cashMap.get("TRANS_ID"));
//                                }else if(!map.containsKey("LOCKER_SURRENDER_DAO")){
//                                    linkBatchMap.put("LINK_BATCH_ID",transactionTO.getDebitAcctNo());
//                                }else{
//                                    linkBatchMap.put("LINK_BATCH_ID",map.get("LOCKER_SURRENDER_ID"));
//                                }
//                                linkBatchMap.put("LINK_BATCH_ID",cashMap.get("TRANS_ID"));
//                                linkBatchMap.put("TRANS_ID",cashMap.get("TRANS_ID"));
//                                linkBatchMap.put("TRANS_DT", currDt);
//                                linkBatchMap.put("INITIATED_BRANCH",_branchCode);
//                                sqlMap.executeUpdate("updateLinkBatchIdCash", linkBatchMap);
//                            }
                        if (!penalTransHappened) {
                            if (CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getPenalAmtPayable()).doubleValue() - penalWaiveAmt > 0) {
                                cashTransMap.put("ACC_HEAD", applicationMap.get("PENAL_INTEREST_HEAD"));
                                cashTransMap.put("PARTICULARS", "Penal ");
                                cashTransMap.put("AMOUNT", mdsBankAdvReceiptEntryTO.getPenalAmtPayable() - penalWaiveAmt);
                                cashTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                cashTransMap.put("INSTRUMENT_NO2","PENAL_AMT");// Added by nithya on 31-10-2019 for KD-680
                                applncashList = commonApplicationList(cashTransMap);
                                cashTransMap.put("DAILYDEPOSITTRANSTO", applncashList);
                                cashTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                cashTransMap.put("AUTHORIZE_REMARKS", cash_unique_id);
                                cashTransMap.put("TRANS_ALL_ID", CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                cashTransMap.put("SINGLE_TRANS_ID", generateSingleTransId);
                                cashTransMap.put("TRANS_MOD_TYPE", "MDS");
                                cashMap = cashTransactionDAO.execute(cashTransMap, false);
                                mdsBankAdvReceiptEntryTO.setPenalTransId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                                HashMap linkBatchMap = new HashMap();
                                if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")
                                        && transactionTO.getProductId().equals("") && !map.containsKey("LOCKER_SURRENDER_DAO")) {
                                    linkBatchMap.put("LINK_BATCH_ID", cashMap.get("TRANS_ID"));
                                } else if (!map.containsKey("LOCKER_SURRENDER_DAO")) {
                                    linkBatchMap.put("LINK_BATCH_ID", transactionTO.getDebitAcctNo());
                                } else {
                                    linkBatchMap.put("LINK_BATCH_ID", map.get("LOCKER_SURRENDER_ID"));
                                }
                                linkBatchMap.put("LINK_BATCH_ID", cashMap.get("TRANS_ID"));
                                linkBatchMap.put("TRANS_ID", cashMap.get("TRANS_ID"));
                                System.out.println("Trans ID 2 :: " + cashMap.get("TRANS_ID"));
                                linkBatchMap.put("TRANS_DT", currDt);
                                linkBatchMap.put("INITIATED_BRANCH", _branchCode);
                                sqlMap.executeUpdate("updateLinkBatchIdCash", linkBatchMap);
                            }
                            //Notice Charge Amount Transaction Started
                            if (CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getNoticeAmt()).doubleValue() - noticeWaiveAmt > 0) {
//                                cashTransMap.put("ACC_HEAD",applicationMap.get("NOTICE_CHARGES_HEAD"));
//                                cashTransMap.put("PARTICULARS","Notice ");
//                                cashTransMap.put("AMOUNT",mdsBankAdvReceiptEntryTO.getNoticeAmt());
//                                applncashList = commonApplicationList(cashTransMap);
//                                cashTransMap.put("DAILYDEPOSITTRANSTO",applncashList);
//                                cashMap = cashTransactionDAO.execute(cashTransMap,false);
//                                mdsBankAdvReceiptEntryTO.setNoticeId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
//                                HashMap linkBatchMap = new HashMap();
//                                if(!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")
//                                && transactionTO.getProductId().equals("") && !map.containsKey("LOCKER_SURRENDER_DAO")){
//                                    linkBatchMap.put("LINK_BATCH_ID",cashMap.get("TRANS_ID"));
//                                }else if(!map.containsKey("LOCKER_SURRENDER_DAO")){
//                                    linkBatchMap.put("LINK_BATCH_ID",transactionTO.getDebitAcctNo());
//                                }else{
//                                    linkBatchMap.put("LINK_BATCH_ID",map.get("LOCKER_SURRENDER_ID"));
//                                }
//                                linkBatchMap.put("LINK_BATCH_ID",cashMap.get("TRANS_ID"));
//                                linkBatchMap.put("TRANS_ID",cashMap.get("TRANS_ID"));
//                                linkBatchMap.put("TRANS_DT", currDt);
//                                linkBatchMap.put("INITIATED_BRANCH",_branchCode);
//                                sqlMap.executeUpdate("updateLinkBatchIdCash", linkBatchMap);

                                System.out.println("Notice Charge Started !!!");
                                HashMap whereMap = new HashMap();
                                whereMap.put("ACT_NUM", CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()) + "_" + CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getSubNo()));
                                List chargeList = (List) sqlMap.executeQueryForList("getMDSNoticeChargeDetails", whereMap);
                                if (chargeList != null && chargeList.size() > 0) {
                                    applncashList = new ArrayList();
                                    for (int i = 0; i < chargeList.size(); i++) {
                                        double chargeAmount = 0.0;
                                        String chargeType = "";
                                        whereMap = (HashMap) chargeList.get(i);
                                        chargeAmount = CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
                                        Rounding rod = new Rounding();
                                        chargeAmount = (double) rod.getNearest((long) (chargeAmount * 100), 100) / 100;
                                        chargeType = CommonUtil.convertObjToStr(whereMap.get("CHARGE_TYPE"));
                                        if (chargeAmount > 0) {
                                            cashTransMap.put("ACC_HEAD", applicationMap.get("NOTICE_CHARGES_HEAD"));
                                            cashTransMap.put("PARTICULARS", chargeType);
                                            cashTransMap.put("AMOUNT", String.valueOf(chargeAmount - noticeWaiveAmt));
                                            cashTransMap.put("MP_MDS_CODE", applicationMap.get("MP_MDS_CODE"));
                                            cashTransMap.put("TRANS_MOD_TYPE", "MDS");
                                            cashTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                            cashTransMap.put("INSTRUMENT_NO2","NOTICE_CHARGE");// Added by nithya on 31-10-2019 for KD-680
                                            //applncashList.add(commonApplicationList(cashTransMap)); // Commented by nithya on 22-07-2020 for KD-2093
                                            ArrayList lstNoticedata = commonApplicationList(cashTransMap);
                                            if (lstNoticedata.size() > 0) {
                                                applncashList.add(lstNoticedata.get(0));
                                            }
                                        }
                                    }
                                    cashTransMap.put("DAILYDEPOSITTRANSTO", applncashList);
                                    cashTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                    cashTransMap.put("TRANS_ALL_ID", CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                    cashTransMap.put("AUTHORIZE_REMARKS", cash_unique_id);
                                    cashTransMap.put("SINGLE_TRANS_ID", generateSingleTransId);
                                    cashTransMap.put("TRANS_MOD_TYPE", "MDS");
                                    cashMap = cashTransactionDAO.execute(cashTransMap, false);
                                    mdsBankAdvReceiptEntryTO.setNoticeId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                                    HashMap linkBatchMap = new HashMap();
                                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")
                                            && transactionTO.getProductId().equals("") && !map.containsKey("LOCKER_SURRENDER_DAO")) {
                                        linkBatchMap.put("LINK_BATCH_ID", cashMap.get("TRANS_ID"));
                                    } else if (!map.containsKey("LOCKER_SURRENDER_DAO")) {
                                        linkBatchMap.put("LINK_BATCH_ID", transactionTO.getDebitAcctNo());
                                    } else {
                                        linkBatchMap.put("LINK_BATCH_ID", map.get("LOCKER_SURRENDER_ID"));
                                    }
                                    linkBatchMap.put("LINK_BATCH_ID", cashMap.get("TRANS_ID"));
                                    linkBatchMap.put("TRANS_ID", cashMap.get("TRANS_ID"));
                                //    System.out.println("Trans ID 3 :: " + cashMap.get("TRANS_ID"));
                                    linkBatchMap.put("TRANS_DT", currDt);
                                    linkBatchMap.put("INITIATED_BRANCH", _branchCode);
                                    sqlMap.executeUpdate("updateLinkBatchIdCash", linkBatchMap);
                                }
                            }
                            //Case Expense Amount Transaction
                            if(arcWaiveAmt > 0){
                                arcHead = getARCHead(mdsBankAdvReceiptEntryTO);
                            }
                            if (CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getArbitrationAmt()).doubleValue() - arcWaiveAmt > 0) {
                                System.out.println("Arbitration Started");
                                HashMap whereMap = new HashMap();
                                whereMap.put("ACT_NUM", CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()) + "_" + CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getSubNo()));
                                List chargeList = (List) sqlMap.executeQueryForList("getMDSCaseChargeDetails", whereMap);
                                if (chargeList != null && chargeList.size() > 0) {
                                    applncashList = new ArrayList();
                                    for (int i = 0; i < chargeList.size(); i++) {
                                        double chargeAmount = 0.0;
                                        String chargeType = "";
                                        whereMap = (HashMap) chargeList.get(i);
                                        chargeAmount = CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
                                        Rounding rod = new Rounding();
                                        chargeAmount = (double) rod.getNearest((long) (chargeAmount * 100), 100) / 100;
                                        chargeType = CommonUtil.convertObjToStr(whereMap.get("CHARGE_TYPE"));
                                        if (chargeAmount > 0) {
                                            if (chargeType.equals("ARC_COST")) {
                                                chargeType = "ARC_COST";
                                            } else if (chargeType.equals("ARC Expense")) {
                                                chargeType = "ARC_EXPENSE";
                                            } else if (chargeType.equals("EA Cost")) {
                                                chargeType = "EA_COST";
                                            } else if (chargeType.equals("EA Expense")) {
                                                chargeType = "EA_EXPENSE";
                                            } else if (chargeType.equals("EP_COST")) {
                                                chargeType = "EP_COST";
                                            } else if (chargeType.equals("EP Expense")) {
                                                chargeType = "EP_EXPENSE";
                                            } else if (chargeType.equals("ARBITRARY CHARGES")) {
                                                chargeType = "ARC_COST";
                                            } else if (chargeType.equals("EXECUTION DECREE CHARGES")) {
                                                chargeType = "EP_COST";
                                            } else if (chargeType.equals("MISCELLANEOUS CHARGES")) {
                                                chargeType = "MISCELLANEOUS_CHARGES";
                                            } else if (chargeType.equals("OTHER CHARGES")) {
                                                chargeType = "OTHER_CHARGES";
                                            } else if (chargeType.equals("LEGAL CHARGES")) {
                                                chargeType = "LEGAL_CHARGES";
                                            } else if (chargeType.equals("INSURANCE CHARGES")) {
                                                chargeType = "INSURANCE_CHARGES";
                                            }
                                            arcHead = chargeType;
//                                            System.out.println("###### chargeType Head : " + chargeType);
//                                            System.out.println("###### chargeAmount : " + chargeAmount);
//                                            System.out.println("###### chargeAmount Head2222: " + applicationMap.get(chargeType));
//                                            cashTransMap.put("ACC_HEAD",applicationMap.get("CASE_EXPENSE_HEAD"));
                                            cashTransMap.put("ACC_HEAD", applicationMap.get(chargeType));
                                            cashTransMap.put("PARTICULARS", chargeType);
                                            cashTransMap.put("AMOUNT", String.valueOf(chargeAmount - arcWaiveAmt));
                                            cashTransMap.put("MP_MDS_CODE", applicationMap.get("MP_MDS_CODE"));
                                            cashTransMap.put("TRANS_MOD_TYPE", "MDS");
                                            cashTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                            cashTransMap.put("INSTRUMENT_NO2",chargeType);// Added by nithya on 31-10-2019 for KD-680
                                            //applncashList.add(commonApplicationList(cashTransMap)); //Commented by nithya on 22-07-2020 for KD-2093
                                            ArrayList lstNoticedata = commonApplicationList(cashTransMap);
                                            if (lstNoticedata.size() > 0) {
                                                applncashList.add(lstNoticedata.get(0));
                                            }
                                        }
                                    }
                                    cashTransMap.put("DAILYDEPOSITTRANSTO", applncashList);
                                    cashTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                    cashTransMap.put("TRANS_ALL_ID", CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                    cashTransMap.put("AUTHORIZE_REMARKS", cash_unique_id);
                                    cashTransMap.put("SINGLE_TRANS_ID", generateSingleTransId);
                                    cashTransMap.put("TRANS_MOD_TYPE", "MDS");
                                    cashMap = cashTransactionDAO.execute(cashTransMap, false);
                                    mdsBankAdvReceiptEntryTO.setArbitrationId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                                    HashMap linkBatchMap = new HashMap();
                                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")
                                            && transactionTO.getProductId().equals("") && !map.containsKey("LOCKER_SURRENDER_DAO")) {
                                        linkBatchMap.put("LINK_BATCH_ID", cashMap.get("TRANS_ID"));
                                    } else if (!map.containsKey("LOCKER_SURRENDER_DAO")) {
                                        linkBatchMap.put("LINK_BATCH_ID", transactionTO.getDebitAcctNo());
                                    } else {
                                        linkBatchMap.put("LINK_BATCH_ID", map.get("LOCKER_SURRENDER_ID"));
                                    }
                                    linkBatchMap.put("LINK_BATCH_ID", cashMap.get("TRANS_ID"));
                                    linkBatchMap.put("TRANS_ID", cashMap.get("TRANS_ID"));
                                    System.out.println("Trans ID 4 :: " + cashMap.get("TRANS_ID"));
                                    linkBatchMap.put("TRANS_DT", currDt);
                                    linkBatchMap.put("INITIATED_BRANCH", _branchCode);
                                    sqlMap.executeUpdate("updateLinkBatchIdCash", linkBatchMap);
                                }

                            }
                            penalTransHappened = true;
                        }
                        
                        if(!waiveTransHappened){
                            if(penalWaiveAmt > 0){
                                commonTransactionWaiveOff(map, mdsBankAdvReceiptEntryTO, debitMap, applicationMap, penalWaiveAmt, "PENAL",arcHead);
                            }if(noticeWaiveAmt > 0){
                                commonTransactionWaiveOff(map, mdsBankAdvReceiptEntryTO, debitMap, applicationMap, noticeWaiveAmt, "NOTICE",arcHead);   
                            }if(arcWaiveAmt > 0){
                                commonTransactionWaiveOff(map, mdsBankAdvReceiptEntryTO, debitMap, applicationMap, arcWaiveAmt, "ARC",arcHead);   
                            }
                            waiveTransHappened = true;
                        }
                        
                        
                        //Changed By Suresh

//                        double futureInsAmt =  instalmentPayAmt;
//                        if(futureInsAmt>0){
//                            cashTransMap.put("ACC_HEAD",applicationMap.get("RECEIPT_HEAD"));
//                            cashTransMap.put("PARTICULARS","");
//                            cashTransMap.put("AMOUNT",String.valueOf(futureInsAmt));
//                            applncashList = commonApplicationList(cashTransMap);
//                            cashTransMap.put("DAILYDEPOSITTRANSTO",applncashList);
//                            cashMap = cashTransactionDAO.execute(cashTransMap,false);
//                            mdsReceiptEntryTO.setArbitrationId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
//                            HashMap linkBatchMap = new HashMap();
//                            if(!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")
//                            && transactionTO.getProductId().equals("") && !map.containsKey("LOCKER_SURRENDER_DAO")){
//                                linkBatchMap.put("LINK_BATCH_ID",cashMap.get("TRANS_ID"));
//                            }else if(!map.containsKey("LOCKER_SURRENDER_DAO")){
//                                linkBatchMap.put("LINK_BATCH_ID",transactionTO.getDebitAcctNo());
//                            }else{
//                                linkBatchMap.put("LINK_BATCH_ID",map.get("LOCKER_SURRENDER_ID"));
//                            }
//                            linkBatchMap.put("LINK_BATCH_ID",cashMap.get("TRANS_ID"));
//                            linkBatchMap.put("TRANS_ID",cashMap.get("TRANS_ID"));
//                            linkBatchMap.put("TRANS_DT", currDt);
//                            linkBatchMap.put("INITIATED_BRANCH",_branchCode);
//                            sqlMap.executeUpdate("updateLinkBatchIdCash", linkBatchMap);
//                        }

////                        if(bankBonusAmt>0){
//                        if(CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getBonusAmtPayable()).doubleValue()>0){






//////                         if(bonusreversal){ //changed by akhila for bonus reversal of ineligible chittal
//////                            System.out.println("Final Amount Transaction Started");
//////                            transferTo = new TxTransferTO();
//////                            txMap = new HashMap();
//////                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
//////                            //Added By Suresh
//////                            if(map.containsKey("MDS_CLOSURE")){
//////                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("SUNDRY_RECEIPT_HEAD"));
//////                            }else{
//////                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("BONUS_PAYABLE_HEAD"));
//////                            }
//////                            txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
//////                            txMap.put(TransferTrans.CR_BRANCH,mdsBankAdvReceiptEntryTO.getBranchCode());//BRANCH_ID);
//////                            txMap.put(TransferTrans.PARTICULARS,"Bonus Reversal :"+applicationMap.get("MP_MDS_CODE")+"-" + mdsBankAdvReceiptEntryTO.getChittalNo()+"_"+mdsBankAdvReceiptEntryTO.getSubNo()+":BANK ADVANCE");
//////                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
//////                            txMap.put(TransferTrans.DR_INST_TYPE,"VOUCHER");
//////                           System.out.println("txMap5 : " + txMap+"bankBonusAmt :"+CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getBonusAmtPayable()).doubleValue());
//////                             System.out.println("BankbonusREeeeversssss  "+bankBonusReverseAmt);
//////                          // transferTo =  transactionDAO.addTransferCreditLocal(txMap, bankBonusReverseAmt);
//////                            transferTo =  transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getBonusAmtPayable()).doubleValue());
//////                            transferTo.setTransId("-");
//////                            transferTo.setBatchId("-");
//////                            transferTo.setTransDt(currDt);
//////                            transferTo.setInitiatedBranch(BRANCH_ID);
//////                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
//////                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
//////                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));
//////                            TxTransferTO.add(transferTo);
////////                            System.out.println("transferTo List 5 : " + transferTo);
//////                        
//////                        System.out.println("TxTransferTO List 5 : " + TxTransferTO);
//////                        HashMap applnMap = new HashMap();
//////                        transferDAO = new TransferDAO();
//////                        map.put("MODE",map.get("COMMAND"));
//////                        map.put("COMMAND",map.get("MODE"));
//////                        map.put("TxTransferTO",TxTransferTO);
//////                        transMap = transferDAO.execute(map,false);
//////                        mdsBankAdvReceiptEntryTO.setBonusTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
//////                        System.out.println("transactionDAO map : " + map);
//////                        transactionDAO.setBatchId(mdsBankAdvReceiptEntryTO.getNetTransId());
//////                        transactionDAO.setBatchDate(currDt);
//////                        transactionDAO.execute(map);
//////                        HashMap linkBatchMap = new HashMap();
//////                        if(!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")
//////                        && transactionTO.getProductId().equals("") && !map.containsKey("LOCKER_SURRENDER_DAO")){
//////                            linkBatchMap.put("LINK_BATCH_ID",transMap.get("TRANS_ID"));
//////                        }else if(!map.containsKey("LOCKER_SURRENDER_DAO")){
//////                            linkBatchMap.put("LINK_BATCH_ID",transactionTO.getDebitAcctNo());
//////                        }else{
//////                            linkBatchMap.put("LINK_BATCH_ID",map.get("LOCKER_SURRENDER_ID"));
//////                        }
//////                        linkBatchMap.put("LINK_BATCH_ID",transMap.get("TRANS_ID"));
//////                        linkBatchMap.put("BATCH_ID",transMap.get("TRANS_ID"));
//////                        linkBatchMap.put("TRANS_DT", currDt);
//////                        linkBatchMap.put("INITIATED_BRANCH",_branchCode);
//////                        sqlMap.executeUpdate("updateLinkBatchIdTransfer", linkBatchMap);
//////                        bonusreversal=false;
//////                         }
//////                        
//////                        









                        if (mdsBankAdvReceiptEntryTO.getForfeitBonusAmtPayable() != null && CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getForfeitBonusAmtPayable()).doubleValue() > 0.0) {
                            if (bonusreversal) {
                                //Added By Suresh
                                if (map.containsKey("MDS_CLOSURE")) {
                                    cashTransMap.put("ACC_HEAD", applicationMap.get("SUNDRY_RECEIPT_HEAD"));
                                } else {
                                    //cashTransMap.put("ACC_HEAD", applicationMap.get("BONUS_PAYABLE_HEAD"));
                                    cashTransMap.put("ACC_HEAD", applicationMap.get("MDS_FORFEITED_ACCT_HEAD")); // Heas change done by nithya on 16-07-2020 for KD-2093
                                }
                                cashTransMap.put("PARTICULARS", "Bonus");
                                cashTransMap.put("AMOUNT", CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getForfeitBonusAmtPayable()).doubleValue());
                             //   System.out.println("####### cashTransMap : " + cashTransMap);
                                cashTransMap.put("TRANS_MOD_TYPE", "MDS");
                                cashTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                cashTransMap.put("INSTRUMENT_NO2","BONUS_REVERSAL");
                                applncashList = commonApplicationList(cashTransMap);
                                cashTransMap.put("DAILYDEPOSITTRANSTO", applncashList);
                                cashTransMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                                cashTransMap.put("AUTHORIZE_REMARKS", cash_unique_id);
                                cashTransMap.put("TRANS_ALL_ID", CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                cashTransMap.put("SINGLE_TRANS_ID", generateSingleTransId);
                                cashTransMap.put("TRANS_MOD_TYPE", "MDS");
                                cashMap = cashTransactionDAO.execute(cashTransMap, false);
                                //mdsBankAdvReceiptEntryTO.setBonusTransId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                                mdsBankAdvReceiptEntryTO.setForfeitBonusTransId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                                HashMap linkBatchMap = new HashMap();
                                if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")
                                        && transactionTO.getProductId().equals("") && !map.containsKey("LOCKER_SURRENDER_DAO")) {
                                    linkBatchMap.put("LINK_BATCH_ID", cashMap.get("TRANS_ID"));
                                } else if (!map.containsKey("LOCKER_SURRENDER_DAO")) {
                                    linkBatchMap.put("LINK_BATCH_ID", transactionTO.getDebitAcctNo());
                                } else {
                                    linkBatchMap.put("LINK_BATCH_ID", map.get("LOCKER_SURRENDER_ID"));
                                }
                                linkBatchMap.put("LINK_BATCH_ID", cashMap.get("TRANS_ID"));
                                linkBatchMap.put("TRANS_ID", cashMap.get("TRANS_ID"));
                                linkBatchMap.put("TRANS_DT", currDt);
                                linkBatchMap.put("INITIATED_BRANCH", _branchCode);
                                sqlMap.executeUpdate("updateLinkBatchIdCash", linkBatchMap);
                            }
                        }
                      //      commonTransactionTransferBankAdvBonus(map, mdsBankAdvReceiptEntryTO, debitMap,applicationMap);//added on 24/7/2015 by nidhin
//                        commonTransactionCashandTransferBankAdv(map, mdsBankAdvReceiptEntryTO, debitMap,applicationMap);
                    }
                    map.put("BANK_ADVANCE_NET_TRANS_ID", mdsBankAdvReceiptEntryTO.getNetTransId());
                    map.put("BANK_ADVANCE_BONUS_TRANS_ID", mdsBankAdvReceiptEntryTO.getBonusTransId());
                    map.put("BANK_ADVANCE_PENAL_TRANS_ID", mdsBankAdvReceiptEntryTO.getPenalTransId());
                    map.put("BANK_ADVANCE_NOTICE_TRANS_ID", mdsBankAdvReceiptEntryTO.getNoticeId());
                    map.put("BANK_ADVANCE_ARBITRATION_TRANS_ID", mdsBankAdvReceiptEntryTO.getArbitrationId());
                    map.put("BANK_ADVANCE_FORFEITBONUS_TRANS_ID", mdsBankAdvReceiptEntryTO.getForfeitBonusTransId()); //2093
                    map.put("BANK_ADVANCE_PENAL_WAIVE_TRANS_ID", mdsBankAdvReceiptEntryTO.getPenalWaiveId());
                    map.put("BANK_ADVANCE_NOTICE_WAIVE_TRANS_ID", mdsBankAdvReceiptEntryTO.getNoticeWaiveId());
                    map.put("BANK_ADVANCE_ARC_WAIVE_TRANS_ID", mdsBankAdvReceiptEntryTO.getArcWaiveId());
                    map.put("BANK_ADVANCE_PENAL_WAIVE_AMT", mdsBankAdvReceiptEntryTO.getPenalWaiveAmt());
                    map.put("BANK_ADVANCE_NOTICE_WAIVE_AMT", mdsBankAdvReceiptEntryTO.getNoticeWaiveAmt());
                    map.put("BANK_ADVANCE_ARC_WAIVE_AMT", mdsBankAdvReceiptEntryTO.getArcWaiveAmt());
                    System.out.println("###### masdadasdaswewp : " + map);
                }
            }

            String USERID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
//            sqlMap.executeUpdate("insertReceiptEntryTO", mdsReceiptEntryTO);
//            System.out.println("$#@$#$@# mdsReceiptEntryTO :"+mdsReceiptEntryTO);
//            setTransDetails(mdsReceiptEntryTO);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void setTransDetails(MDSReceiptEntryTO mdsReceiptEntryTO) throws Exception {
//        if(!penalTransHappened){

        transIdMap = new HashMap();
        if (mdsReceiptEntryTO.getArbitrationAmt().doubleValue() > 0) {
            getTransDetails(mdsReceiptEntryTO.getArbitrationId(), "ARBITRATION");

            transIdMap.put(mdsReceiptEntryTO.getArbitrationId(), null);
            System.out.println("transIdMap 1" + transIdMap);
        }
        if (mdsReceiptEntryTO.getNoticeAmt().doubleValue() > 0) {
            getTransDetails(mdsReceiptEntryTO.getNoticeId(), "NOTICE");
            transIdMap.put(mdsReceiptEntryTO.getNoticeId(), null);
            System.out.println("transIdMap 2" + transIdMap);
        }
        if (mdsReceiptEntryTO.getPenalAmtPayable().doubleValue() > 0) {
            getTransDetails(mdsReceiptEntryTO.getPenalTransId(), "PENAL");
            transIdMap.put(mdsReceiptEntryTO.getPenalTransId(), null);
            System.out.println("transIdMap 3" + transIdMap);
        }
//            penalTransHappened = true;
//        }
        if (mdsReceiptEntryTO.getBonusAmtPayable().doubleValue() > 0) {
            getTransDetails(mdsReceiptEntryTO.getBonusTransId(), "BONUS");
            transIdMap.put(mdsReceiptEntryTO.getBonusTransId(), null);
            System.out.println("transIdMap 4" + transIdMap);
        } else {
            getTransDetails(mdsReceiptEntryTO.getBonusTransId(), "BONUS");
            transIdMap.put(mdsReceiptEntryTO.getBonusTransId(), null);
        }
        if (mdsReceiptEntryTO.getDiscountAmt().doubleValue() > 0) {
            getTransDetails(mdsReceiptEntryTO.getDiscountTransId(), "DISCOUNT");
            transIdMap.put(mdsReceiptEntryTO.getDiscountTransId(), null);
            System.out.println("transIdMap 5" + transIdMap);
        }
        if (mdsReceiptEntryTO.getNetAmt().doubleValue() > 0) {
            getTransDetails(mdsReceiptEntryTO.getNetTransId(), "NET");
            transIdMap.put(mdsReceiptEntryTO.getNetTransId(), null);
            System.out.println("transIdMap 6" + transIdMap);
        }
    }

    private void getTransDetails(String batchId, String mapKey) throws Exception {
        System.out.println("@@@ transIdMap:" + transIdMap + " / @@@ batchId:" + batchId + " sinle " + mdsReceiptEntryTO.getSingleTransId());
        if (batchId != null && batchId.length() > 0 && !transIdMap.containsKey(batchId)) {
            HashMap whereMap = new HashMap();
            whereMap.put("BATCH_ID", batchId);
            whereMap.put("TRANS_DT", currDt);
            whereMap.put(CommonConstants.BRANCH_ID, _branchCode);
            if (returnMap == null) {
                returnMap = new HashMap();
            }
            System.out.println("returnMap 111           " + returnMap);
            List transList = (List) sqlMap.executeQueryForList("getTransferDetails", whereMap);
            System.out.println("problem is here>>> " + transList);
            if (transList != null && transList.size() > 0) {
                if ((mapKey.equals("NET") || mapKey.equals("BONUS")) && returnMap.containsKey(mapKey + "_TRANSFER_DATA")) {
                    ArrayList oldTransDetails = (ArrayList) returnMap.get(mapKey + "_TRANSFER_DATA");
                    transList.addAll(oldTransDetails);
                    returnMap.put(mapKey + "_TRANSFER_DATA", transList);
                    System.out.println("returnMap 22           " + returnMap);
                } else {
                    returnMap.put(mapKey + "_TRANSFER_DATA", transList);
                    System.out.println("returnMap 333           " + returnMap);
                }
            }
            List cashList = (List) sqlMap.executeQueryForList("getCashDetails", whereMap);
            System.out.println("problem is here2222>>> " + cashList);
            System.out.println("returnMap 444          " + returnMap);
            List tempList = cashList;
            if (cashList != null && cashList.size() > 0) {
                if ((mapKey.equals("NET") || mapKey.equals("BONUS")) && returnMap.containsKey(mapKey + "_CASH_DATA")) {
                    ArrayList oldTransDetails = (ArrayList) returnMap.get(mapKey + "_CASH_DATA");
                    if (oldTransDetails != null && oldTransDetails.size() > 0) {
                        for (int i = 0; i < oldTransDetails.size(); i++) {
                            HashMap hmap = (HashMap) oldTransDetails.get(i);
                            for (int j = 0; j < cashList.size(); j++) {
                                HashMap temphmap = (HashMap) cashList.get(j);
                                if (CommonUtil.convertObjToStr(hmap.get("TRANS_ID")).equals(CommonUtil.convertObjToStr(temphmap.get("TRANS_ID")))
                                        && CommonUtil.convertObjToStr(hmap.get("TRANS_TYPE")).equals(CommonUtil.convertObjToStr(temphmap.get("TRANS_TYPE")))
                                        && CommonUtil.convertObjToStr(hmap.get("AMOUNT")).equals(CommonUtil.convertObjToStr(temphmap.get("AMOUNT")))) {
                                    System.out.println("fash :"+temphmap);
                                } else {
                                    tempList.add(hmap);
                                }

                            }
                        }
                        cashList = tempList;
                    }
                 //   cashList.addAll(oldTransDetails);
                    returnMap.put(mapKey + "_CASH_DATA", cashList);
                    System.out.println("returnMap 555           " + returnMap);
                } else {
                    returnMap.put(mapKey + "_CASH_DATA", cashList);
                    System.out.println("returnMap 666           " + returnMap);
                }
            }
            whereMap.clear();
            whereMap = null;
        }
    }

    //    private ArrayList applicationListPenal(HashMap applnMap)throws Exception{
    //        System.out.println("applnMap :"+applnMap);
    //        ArrayList cashList = new ArrayList();
    //        System.out.println("objCashTransactionTO :"+mdsReceiptEntryTO);
    //        double finalReceivingAmt = CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNetAmt()).doubleValue() -
    //        (CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getPenalAmtPayable()).doubleValue() +
    //        CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNoticeAmt()).doubleValue() +
    //        CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getArbitrationAmt()).doubleValue());
    //
    //        if(mdsReceiptEntryTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
    //            CashTransactionTO objCashTO = new CashTransactionTO();
    //            objCashTO.setTransId("");
    //            objCashTO.setProdType("GL");
    //            objCashTO.setTransType(CommonConstants.CREDIT);
    //            objCashTO.setInitTransId(CommonConstants.TTSYSTEM);
    //            objCashTO.setBranchId(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
    //            objCashTO.setStatusBy(CommonConstants.TTSYSTEM);
    //            objCashTO.setStatusDt(mdsReceiptEntryTO.getStatusDt());
    //            objCashTO.setInstrumentNo2("ENTERED_AMOUNT");
    //            objCashTO.setParticulars(mdsReceiptEntryTO.getSchemeName()+"-" + mdsReceiptEntryTO.getChittalNo()+":Penal");
    //            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
    //            objCashTO.setInitChannType(CommonConstants.CASHIER);
    //            objCashTO.setCommand(mdsReceiptEntryTO.getCommand());
    //            System.out.println("objCashTO 1st one:" + objCashTO);
    //            objCashTO.setAcHdId(CommonUtil.convertObjToStr(applnMap.get("PENAL_INTEREST_HEAD")));
    //            objCashTO.setInpAmount(new Double(CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getPenalAmtPayable()).doubleValue()));
    //            objCashTO.setAmount(new Double(CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getPenalAmtPayable()).doubleValue()));
    //            cashList.add(objCashTO);
    //            objCashTO = null;
    //        }
    //        return cashList;
    //    }
    //
    //    private ArrayList applicationListNotice(HashMap applnMap)throws Exception{
    //        System.out.println("applnMap :"+applnMap);
    //        ArrayList cashList = new ArrayList();
    //        System.out.println("objCashTransactionTO :"+mdsReceiptEntryTO);
    //        double finalReceivingAmt = CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNetAmt()).doubleValue() -
    //        (CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getPenalAmtPayable()).doubleValue() +
    //        CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNoticeAmt()).doubleValue() +
    //        CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getArbitrationAmt()).doubleValue());
    //
    //        if(mdsReceiptEntryTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
    //            CashTransactionTO objCashTO = new CashTransactionTO();
    //            objCashTO.setTransId("");
    //            objCashTO.setProdType("GL");
    //            objCashTO.setTransType(CommonConstants.CREDIT);
    //            objCashTO.setInitTransId(CommonConstants.TTSYSTEM);
    //            objCashTO.setBranchId(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
    //            objCashTO.setStatusBy(CommonConstants.TTSYSTEM);
    //            objCashTO.setStatusDt(mdsReceiptEntryTO.getStatusDt());
    //            objCashTO.setInstrumentNo2("ENTERED_AMOUNT");
    //            objCashTO.setParticulars(mdsReceiptEntryTO.getSchemeName()+"-" + mdsReceiptEntryTO.getChittalNo()+":Notice");
    //            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
    //            objCashTO.setInitChannType(CommonConstants.CASHIER);
    //            objCashTO.setCommand(mdsReceiptEntryTO.getCommand());
    //            System.out.println("objCashTO 1st one:" + objCashTO);
    //            objCashTO.setAcHdId(CommonUtil.convertObjToStr(applnMap.get("NOTICE_CHARGES_HEAD")));
    //            objCashTO.setInpAmount(new Double(CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNoticeAmt()).doubleValue()));
    //            objCashTO.setAmount(new Double(CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNoticeAmt()).doubleValue()));
    //            cashList.add(objCashTO);
    //            objCashTO = null;
    //        }
    //        return cashList;
    //    }
    //
    //    private ArrayList applicationListArbitration(HashMap applnMap)throws Exception{
    //        System.out.println("applnMap :"+applnMap);
    //        ArrayList cashList = new ArrayList();
    //        System.out.println("objCashTransactionTO :"+mdsReceiptEntryTO);
    //        double finalReceivingAmt = CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNetAmt()).doubleValue() -
    //        (CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getPenalAmtPayable()).doubleValue() +
    //        CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNoticeAmt()).doubleValue() +
    //        CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getArbitrationAmt()).doubleValue());
    //        if(mdsReceiptEntryTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
    //            CashTransactionTO objCashTO = new CashTransactionTO();
    //            objCashTO.setTransId("");
    //            objCashTO.setProdType("GL");
    //            objCashTO.setTransType(CommonConstants.CREDIT);
    //            objCashTO.setInitTransId(CommonConstants.TTSYSTEM);
    //            objCashTO.setBranchId(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
    //            objCashTO.setStatusBy(CommonConstants.TTSYSTEM);
    //            objCashTO.setStatusDt(mdsReceiptEntryTO.getStatusDt());
    //            objCashTO.setInstrumentNo2("ENTERED_AMOUNT");
    //            objCashTO.setParticulars(mdsReceiptEntryTO.getSchemeName()+"-" + mdsReceiptEntryTO.getChittalNo()+":Arbitration");
    //            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
    //            objCashTO.setInitChannType(CommonConstants.CASHIER);
    //            objCashTO.setCommand(mdsReceiptEntryTO.getCommand());
    //            System.out.println("objCashTO 1st one:" + objCashTO);
    //            objCashTO.setAcHdId(CommonUtil.convertObjToStr(applnMap.get("CASE_EXPENSE_HEAD")));
    //            objCashTO.setInpAmount(new Double(CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getArbitrationAmt()).doubleValue()));
    //            objCashTO.setAmount(new Double(CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getArbitrationAmt()).doubleValue()));
    //            cashList.add(objCashTO);
    //            objCashTO = null;
    //        }
    //        return cashList;
    //    }
    //
    //    private ArrayList applicationListMDS(HashMap applnMap)throws Exception{
    //        System.out.println("applnMap :"+applnMap);
    //        ArrayList cashList = new ArrayList();
    //        System.out.println("objCashTransactionTO :"+mdsReceiptEntryTO);
    //        double finalReceivingAmt = CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNetAmt()).doubleValue() -
    //        (CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getPenalAmtPayable()).doubleValue() +
    //        CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNoticeAmt()).doubleValue() +
    //        CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getArbitrationAmt()).doubleValue());
    //        if(mdsReceiptEntryTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
    //            CashTransactionTO objCashTO = new CashTransactionTO();
    //            objCashTO.setTransId("");
    //            objCashTO.setProdType("GL");
    //            objCashTO.setTransType(CommonConstants.CREDIT);
    //            objCashTO.setInitTransId(CommonConstants.TTSYSTEM);
    //            objCashTO.setBranchId(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
    //            objCashTO.setStatusBy(CommonConstants.TTSYSTEM);
    //            objCashTO.setStatusDt(mdsReceiptEntryTO.getStatusDt());
    //            objCashTO.setInstrumentNo2("ENTERED_AMOUNT");
    //            objCashTO.setParticulars(mdsReceiptEntryTO.getSchemeName()+"-" + mdsReceiptEntryTO.getChittalNo());
    //            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
    //            objCashTO.setInitChannType(CommonConstants.CASHIER);
    //            objCashTO.setCommand(mdsReceiptEntryTO.getCommand());
    //            System.out.println("objCashTO 1st one:" + objCashTO);
    //            if(applnMap.containsKey("BANKING_REP_PAY_HEAD")){       // BANK ADVANCE
    //                objCashTO.setAcHdId(CommonUtil.convertObjToStr(applnMap.get("BANKING_REP_PAY_HEAD")));
    //            }else{
    //                objCashTO.setAcHdId(CommonUtil.convertObjToStr(applnMap.get("RECEIPT_HEAD")));
    //            }
    //            objCashTO.setInpAmount(new Double(finalReceivingAmt));
    //            objCashTO.setAmount(new Double(finalReceivingAmt));
    //            cashList.add(objCashTO);
    //            objCashTO = null;
    //        }
    //        return cashList;
    //    }
    private ArrayList commonApplicationList(HashMap applnMap) throws Exception {
        System.out.println("applnMap :" + applnMap);
        ArrayList cashList = new ArrayList();
        if (mdsReceiptEntryTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
            CashTransactionTO objCashTO = new CashTransactionTO();
            objCashTO.setTransId("");
            objCashTO.setProdType("GL");
            System.out.println("applnMap.get(AUTHORIZE_REMARKS)==" + applnMap.get("AUTHORIZE_REMARKS"));
            objCashTO.setAuthorizeRemarks(CommonUtil.convertObjToStr(applnMap.get("AUTHORIZE_REMARKS")));
            objCashTO.setTransType(CommonConstants.CREDIT);
            objCashTO.setInitTransId(CommonUtil.convertObjToStr(applnMap.get("USER_ID")));
            if (applnMap.containsKey("ACCT_FROM_BRANCH") && CommonUtil.convertObjToStr(applnMap.get("ACCT_FROM_BRANCH")).length() > 0) {
                objCashTO.setBranchId(CommonUtil.convertObjToStr(applnMap.get("ACCT_FROM_BRANCH")));
            } else {
                objCashTO.setBranchId(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
            }
            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
            objCashTO.setStatusBy(CommonUtil.convertObjToStr(applnMap.get("USER_ID")));
            objCashTO.setStatusDt(mdsReceiptEntryTO.getStatusDt());
            objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            // added installment number at the end of particulars for 8366 on 29-11-2017
           // objCashTO.setParticulars(applnMap.get("PARTICULARS") + ":" + applnMap.get("MP_MDS_CODE") + "-" + mdsReceiptEntryTO.getChittalNo() + "_" + mdsReceiptEntryTO.getSubNo());
            objCashTO.setParticulars(mdsReceiptEntryTO.getNarration()+":"+applnMap.get("MP_MDS_CODE") + "-" + mdsReceiptEntryTO.getChittalNo() + "_" + mdsReceiptEntryTO.getSubNo());
//            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            objCashTO.setCommand(mdsReceiptEntryTO.getCommand());
            objCashTO.setTransAllId(CommonUtil.convertObjToStr(applnMap.get("TRANS_ALL_ID")));
            objCashTO.setAcHdId(CommonUtil.convertObjToStr(applnMap.get("ACC_HEAD")));
            objCashTO.setInpAmount(new Double(CommonUtil.convertObjToDouble(applnMap.get("AMOUNT")).doubleValue()));
            objCashTO.setAmount(new Double(CommonUtil.convertObjToDouble(applnMap.get("AMOUNT")).doubleValue()));
            objCashTO.setTransModType(CommonUtil.convertObjToStr(applnMap.get("TRANS_MOD_TYPE")));
            objCashTO.setSingleTransId(generateSingleTransId);
            objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(applnMap.get("LINK_BATCH_ID")));
            objCashTO.setIbrHierarchy(CommonUtil.convertObjToStr(ibrHierarchy++));
            if (applnMap.containsKey("SCREEN_NAME") && applnMap.get("SCREEN_NAME") != null) {
                objCashTO.setScreenName(CommonUtil.convertObjToStr(applnMap.get("SCREEN_NAME")));
            }
            if(applnMap.containsKey("INSTRUMENT_NO2") && applnMap.get("INSTRUMENT_NO2") != null){// Added by nithya on 31-10-2019 for KD-680
                objCashTO.setInstrumentNo2(CommonUtil.convertObjToStr(applnMap.get("INSTRUMENT_NO2")));
            }
            System.out.println("objCashTO 1st one:" + objCashTO);
            cashList.add(objCashTO);
            objCashTO = null;
        }
        return cashList;
    }

    private CashTransactionTO createCashTransactionTO(HashMap applnMap, String generateSingleTransId) throws Exception { //Case Expense Transaction
        CashTransactionTO objCashTO = new CashTransactionTO();
        if (mdsReceiptEntryTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
            objCashTO.setTransId("");
            objCashTO.setProdType("GL");
            objCashTO.setTransType(CommonConstants.CREDIT);
            objCashTO.setInitTransId(CommonUtil.convertObjToStr(applnMap.get("USER_ID")));
            if (applnMap.containsKey("ACCT_FROM_BRANCH") && CommonUtil.convertObjToStr(applnMap.get("ACCT_FROM_BRANCH")).length() > 0) {// Added by nithya on 31-10-2018 for KD 302 - 0015072: mds- postage charge
                objCashTO.setBranchId(CommonUtil.convertObjToStr(applnMap.get("ACCT_FROM_BRANCH")));
            } else {
                objCashTO.setBranchId(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
            }
            objCashTO.setStatusBy(CommonUtil.convertObjToStr(applnMap.get("USER_ID")));
            objCashTO.setStatusDt(mdsReceiptEntryTO.getStatusDt());
            objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            objCashTO.setAuthorizeRemarks(CommonUtil.convertObjToStr(applnMap.get("AUTHORIZE_REMARKS")));
            if (applnMap.get("PARTICULARS") != null && (applnMap.get("PARTICULARS").toString().startsWith("Penal Chittal No")
                    || applnMap.get("PARTICULARS").toString().startsWith("Chittal No"))) {
                objCashTO.setParticulars(applnMap.get("PARTICULARS").toString());
            } else {
                objCashTO.setParticulars(applnMap.get("PARTICULARS") + ":" + applnMap.get("MP_MDS_CODE") + "-" + mdsReceiptEntryTO.getChittalNo() + "_" + mdsReceiptEntryTO.getSubNo());
            }
            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            objCashTO.setCommand(mdsReceiptEntryTO.getCommand());
            objCashTO.setTransAllId(CommonUtil.convertObjToStr(applnMap.get("TRANS_ALL_ID")));
            objCashTO.setAcHdId(CommonUtil.convertObjToStr(applnMap.get("ACC_HEAD")));
            objCashTO.setInpAmount(new Double(CommonUtil.convertObjToDouble(applnMap.get("AMOUNT")).doubleValue()));
            objCashTO.setAmount(new Double(CommonUtil.convertObjToDouble(applnMap.get("AMOUNT")).doubleValue()));
            objCashTO.setTransModType(CommonUtil.convertObjToStr(applnMap.get("TRANS_MOD_TYPE")));
            objCashTO.setSingleTransId(generateSingleTransId);
            if(applnMap.containsKey("GL_TRANS_ACT_NUM") && applnMap.get("GL_TRANS_ACT_NUM")!= null){// Added by nithya on KD 434 -MDS RECEIPT ENTRY ISSUE
                 objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(applnMap.get("GL_TRANS_ACT_NUM")));
            }            
            if (applnMap.containsKey("SCREEN_NAME") && applnMap.get("SCREEN_NAME") != null) {
                objCashTO.setScreenName((String)applnMap.get("SCREEN_NAME"));
            }
            if(applnMap.containsKey("INSTRUMENT_NO2") && applnMap.get("INSTRUMENT_NO2") != null){// Added by nithya on 31-10-2019 for KD-680
                objCashTO.setInstrumentNo2(CommonUtil.convertObjToStr(applnMap.get("INSTRUMENT_NO2")));
            }
            System.out.println("objCashTO 1st one:" + objCashTO);
        }
        return objCashTO;
    }

    public void commonTransactionCashandTransfer(HashMap map, MDSReceiptEntryTO mdsCurrentReceiptEntryTO, HashMap debitMap, HashMap applicationMap) throws Exception {
        String thalayalTransCategory = "FULLAMT_TO_FULLAMT";
        String munnalTransCategory = "FULLAMT_TO_FULLAMT";
        String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        System.out.println("mdsCurrentReceiptEntryTO####" + mdsCurrentReceiptEntryTO + map);
        ArrayList bonusList = null;
        ArrayList forfeiteBonusList = null;
        double forFeitedAmt = 0.0;
        HashMap graceForFeiteMap = null;
        ListIterator bonus_Iterator = null;
        double forfeiteBonusAmount = 0.0;
        if (map != null) {
            graceForFeiteMap = (HashMap) map.get("GRACE_MAP");
            //  bonusList = (ArrayList) graceForFeiteMap.get("BONUS_AMOUNT_LIST");
            if (graceForFeiteMap != null && graceForFeiteMap.get("FORFEITE_BONUS_AMOUNT_LIST") != null) {
                forfeiteBonusList = (ArrayList) graceForFeiteMap.get("FORFEITE_BONUS_AMOUNT_LIST");
            }
        }
        if (forfeiteBonusList != null && !forfeiteBonusList.isEmpty()) {
            for (int i = 0; i < forfeiteBonusList.size(); i++) {
                forfeiteBonusAmount += CommonUtil.convertObjToDouble(forfeiteBonusList.get(i));
            }
        }
//        System.out.println("BonusList" + bonusList);
//        System.out.println("forfeiteBonusList" + forfeiteBonusList);
        // Date nextInstDate = (Date)graceForFeiteMap.get("NEXT_INSTALLMENT_DATE");
        //  int noOfPendings = (Integer)graceForFeiteMap.get("No_OF_PENDING");
        //  System.out.println("DateUtil.dateDiff(currDt, nextInstDate)"+DateUtil.dateDiff(currDt, nextInstDate));
        double customerBonus = 0.0;
//        System.out.println("ForFeiteAmount" + forFeitedAmt + "CustBonus " + customerBonus);
//        System.out.println("AppLICATION MAp" + applicationMap);
        ArrayList transferList = new ArrayList();
        TransferTrans transferTrans = new TransferTrans();
        TxTransferTO transferTo = new TxTransferTO();
        ArrayList TxTransferTO = new ArrayList();
        TransactionTO transactionTO = new TransactionTO();
        HashMap txMap = new HashMap();
        HashMap transactionListMap = new HashMap();
        transferTrans.setInitiatedBranch(BRANCH_ID);
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        transactionDAO.setInitiatedBranch(mdsCurrentReceiptEntryTO.getBranchCode());
        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
        
        //Thalayal munnal changes
        if (applicationMap != null && applicationMap.containsKey("BANK_MDS_TRNF_CAT_THALAYAL") && applicationMap.get("BANK_MDS_TRNF_CAT_THALAYAL") != null) {
            thalayalTransCategory = CommonUtil.convertObjToStr(applicationMap.get("BANK_MDS_TRNF_CAT_THALAYAL"));
        }
        if (applicationMap != null && applicationMap.containsKey("BANK_MDS_TRNF_CAT_MUNNAL") && applicationMap.get("BANK_MDS_TRNF_CAT_MUNNAL") != null) {
            munnalTransCategory = CommonUtil.convertObjToStr(applicationMap.get("BANK_MDS_TRNF_CAT_MUNNAL"));
        }
        // End
        
        
        //System.out.println("mdsCurrentReceiptEntryTO.getBonusAmtPayable()" + mdsCurrentReceiptEntryTO.getBonusAmtPayable());
        if (CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getBonusAmtPayable()).doubleValue() > 0) {
            System.out.println("Bonus Started");
            transferTo = new TxTransferTO();
            txMap = new HashMap();
            transferTo.setInstrumentNo2("APPL_GL_TRANS");
            //Changed By Suresh
            if (map.containsKey("MDS_CLOSURE")) {
                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("SUNDRY_PAYMENT_HEAD"));
            } else {
                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("BONUS_RECEIVABLE_HEAD"));
            }
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.DR_BRANCH, mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);

            if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
              //  System.out.println("map.get=0000====" + map.get("FROM_PARTICULARS_TRANSALL"));
                txMap.put(TransferTrans.PARTICULARS, "Bonus : " + map.get("FROM_PARTICULARS_TRANSALL"));
            } else {
                txMap.put(TransferTrans.PARTICULARS, "Bonus :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsCurrentReceiptEntryTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
            }
            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
            txMap.put("TRANS_MOD_TYPE", "MDS");
//            System.out.println("txMap : " + txMap+"serviceAmt :"+mdsCurrentReceiptEntryTO.getBonusAmtPayable());
          //  System.out.println("First TXTMAP" + txMap);
            double totalAmount = mdsCurrentReceiptEntryTO.getBonusAmtPayable().doubleValue() + forfeiteBonusAmount;
            transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(totalAmount));
            transferTo.setInstrumentNo2("BONUS");// Added by nithya on 31-10-2019 for KD-680
            transferTo.setTransId("-");
            transferTo.setBatchId("-");
            transferTo.setTransType(CommonConstants.DEBIT);
            transferTo.setProdType(TransactionFactory.GL);
            transferTo.setTransDt(currDt);
            transferTo.setBranchId(mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);
            transferTo.setInitiatedBranch(mdsCurrentReceiptEntryTO.getBranchCode());//bb1 BRANCH_ID);
            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
            transferTo.setRec_mode(CommonUtil.convertObjToStr(map.get("REC_MODE")));

            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
            transactionTO.setChequeNo("SERVICE_TAX");
            transferTo.setGlTransActNum(mdsCurrentReceiptEntryTO.getChittalNo());
            transferTo.setSingleTransId(generateSingleTransId);
            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
            TxTransferTO.add(transferTo);
            //System.out.println("transferTo List 6 : " + transferTo);
        }
        if (forfeiteBonusAmount > 0 && mdsCurrentReceiptEntryTO.getBonusAmtPayable().doubleValue() <= 0) {
//By Nidhin
            transferTo = new TxTransferTO();
            txMap = new HashMap();
            transferTo.setInstrumentNo2("APPL_GL_TRANS");
            if (map.containsKey("MDS_CLOSURE")) {
                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("SUNDRY_PAYMENT_HEAD"));
            } else {
                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("BONUS_RECEIVABLE_HEAD"));
            }
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.DR_BRANCH, mdsCurrentReceiptEntryTO.getBranchCode());
            if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
               // System.out.println("map.get=0000====" + map.get("FROM_PARTICULARS_TRANSALL"));
                txMap.put(TransferTrans.PARTICULARS, "Bonus : " + map.get("FROM_PARTICULARS_TRANSALL"));
            } else {
                txMap.put(TransferTrans.PARTICULARS, "Bonus :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsCurrentReceiptEntryTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
            }
            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
         //   System.out.println("First TXTMAP" + txMap);
            txMap.put("TRANS_MOD_TYPE", "MDS");
            transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(forfeiteBonusAmount));
            transferTo.setInstrumentNo2("FORFEITE_BONUS");// Added by nithya on 31-10-2019 for KD-680
            transferTo.setTransId("-");
            transferTo.setBatchId("-");
            transferTo.setTransType(CommonConstants.DEBIT);
            transferTo.setProdType(TransactionFactory.GL);
            transferTo.setTransDt(currDt);
            transferTo.setBranchId(mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);
            transferTo.setInitiatedBranch(mdsCurrentReceiptEntryTO.getBranchCode());//bb1 BRANCH_ID);
            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
            transferTo.setRec_mode(CommonUtil.convertObjToStr(map.get("REC_MODE")));
            transferTo.setGlTransActNum(mdsCurrentReceiptEntryTO.getChittalNo());
            transferTo.setSingleTransId(generateSingleTransId);
            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
            transactionTO.setChequeNo("SERVICE_TAX");
            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
            TxTransferTO.add(transferTo);
        }
//        System.out.println("TxTransferTO List 6 : " + TxTransferTO);
        if (CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getDiscountAmt()).doubleValue() > 0) {
            System.out.println("Discount Started");
            transferTo = new TxTransferTO();
            txMap = new HashMap();
            transferTo.setInstrumentNo2("APPL_GL_TRANS");
            txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("DISCOUNT_HEAD"));
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.DR_BRANCH, mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);

            if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
             //   System.out.println("map.get=eeeee====" + map.get("FROM_PARTICULARS_TRANSALL"));
                txMap.put(TransferTrans.PARTICULARS, "Discount : " + map.get("FROM_PARTICULARS_TRANSALL"));
            } else {
                txMap.put(TransferTrans.PARTICULARS, "Discount :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsCurrentReceiptEntryTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
            }
            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
//            System.out.println("txMap : " + txMap+"serviceAmt :"+mdsCurrentReceiptEntryTO.getDiscountAmt());
         //   System.out.println("Second TXTMAP" + txMap);
            txMap.put("TRANS_MOD_TYPE", "MDS");
            transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getDiscountAmt()).doubleValue());
            transferTo.setInstrumentNo2("DISCOUNT");// Added by nithya on 31-10-2019 for KD-680
            transferTo.setTransId("-");
            transferTo.setBatchId("-");
            transferTo.setTransType(CommonConstants.DEBIT);
            transferTo.setProdType(TransactionFactory.GL);
            transferTo.setTransDt(currDt);
            transferTo.setBranchId(mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);
            transferTo.setInitiatedBranch(mdsCurrentReceiptEntryTO.getBranchCode());
            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
            transferTo.setRec_mode(CommonUtil.convertObjToStr(map.get("REC_MODE")));
            transferTo.setGlTransActNum(mdsCurrentReceiptEntryTO.getChittalNo());
            transferTo.setSingleTransId(generateSingleTransId);

            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
            transactionTO.setChequeNo("SERVICE_TAX");
            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
            TxTransferTO.add(transferTo);
         //   System.out.println("transferTo List 7 : " + transferTo);
        }
        double receivingGLAmt = CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getDiscountAmt()).doubleValue()
                + CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getBonusAmtPayable()).doubleValue();
     //   System.out.println("transferTo List 7 : " + TxTransferTO + "receivingGLAmt : " + receivingGLAmt);
        HashMap installMap = (HashMap) map.get("INSTALLMENT_MAP");
        double bonusAmt = 0.0;
        HashMap prdtMap = (HashMap) map.get("PRODUCT_MAP");
        double bonusAmount = CommonUtil.convertObjToDouble(installMap.get("BONUS"));
      //  System.out.println("mdsReceiptEntryTO.getBonusAmtAvail() " + bonusAmount + "" + "Install Map :" + installMap);
        double crntBonusAmt = 0.0;
        if (receivingGLAmt > 0 || receivingGLAmt == 0) {
//            if (applicationMap != null && !applicationMap.isEmpty()){
//                if(applicationMap.get("FORFEITE_HD_Y_N")!=null && applicationMap.get("FORFEITE_HD_Y_N").equals("Y")) {
//                if (installMap != null && (!installMap.isEmpty())) {
//                    bonusIterator = installMap.keySet().iterator();
//                    String key = "";
//                    for (int i = 0; i < installMap.size(); i++) {
//                        key = (String) bonusIterator.next();
//                        System.out.println("KEY "+key);
//                        HashMap bonusMap = (HashMap) installMap.get(key);
//                        GregorianCalendar source = new GregorianCalendar();
//                        if(bonusMap != null && !bonusMap.isEmpty()){
//                        if(bonusMap.get("NEXT_INSTALLMENT_DATE") != null && DateUtil.dateDiff(source.getTime(), DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(bonusMap.get("NEXT_INSTALLMENT_DATE"))))< 0){
//                        forFeitedAmt += CommonUtil.convertObjToDouble(bonusMap.get("BONUS"));
//                        }else{
//                         crntBonusAmt += CommonUtil.convertObjToDouble(bonusMap.get("BONUS"));   
//                        }
//                    }
//                    }
//                    System.out.println("forFeitedAmt :"+forFeitedAmt+"crntBonusAmt  :"+crntBonusAmt);
//                }
//        }

          //  System.out.println("GL Final Transaction Started");
            transferTo = new TxTransferTO();
            txMap = new HashMap();
            transferTo.setInstrumentNo2("APPL_GL_TRANS");
            //Changed By Suresh
            HashMap productMap = (HashMap) map.get("PRODUCT_MAP");
            if (map.containsKey("MDS_CLOSURE")) {
                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("MDS_RECEIVABLE_HEAD"));
            } else {
//                if (applicationMap.get("FORFEITE_HD_Y_N") != null && applicationMap.get("FORFEITE_HD_Y_N").equals("Y")) {                                                                                                                                                                                                                                                                                              
//                    System.out.println("111122222222NITHUS" + graceForFeiteMap);
//                    if (graceForFeiteMap != null && !graceForFeiteMap.isEmpty()) {
//                        int noOfDaysPending = CommonUtil.convertObjToInt(graceForFeiteMap.get("NO_OF_DAYS_PENDING"));
//                        System.out.println("FORFOFOFO" + CommonUtil.convertObjToInt(graceForFeiteMap.get("DATE_DIFF")) + noOfDaysPending);
//                        if ( graceForFeiteMap.get("ISPENDING").equals("N")) {
//                            if(bonusList.size() >1){
//                                forfeiteBonusAmount = CommonUtil.convertObjToDouble(bonusList.get(bonusList.size()-1));
//                                receivingGLAmt-=forfeiteBonusAmount;
//                                System.out.println("receivingGLAmt----"+receivingGLAmt);
//                            }
//                            if(CommonUtil.convertObjToInt(graceForFeiteMap.get("DATE_DIFF")) + noOfDaysPending < 0){
//                            applicationMap.put("RECEIPT_HEAD", CommonUtil.convertObjToStr(graceForFeiteMap.get("FORFEITED_HEAD")));
//                            }else{
//                                applicationMap.put("RECEIPT_HEAD", applicationMap.get("RECEIPT_HEAD"));
//                            }
//                        } else {
//                            applicationMap.put("RECEIPT_HEAD",CommonUtil.convertObjToStr(graceForFeiteMap.get("FORFEITED_HEAD")));
//                        }
//                    }
//                } else {
//                    applicationMap.put("RECEIPT_HEAD", applicationMap.get("RECEIPT_HEAD"));
//                }
                if(map.containsKey("THALAYAL_CHITTAL") && !map.get("THALAYAL_CHITTAL").equals("") && map.get("THALAYAL_CHITTAL").equals("Y")){
                    if(thalayalTransCategory.equalsIgnoreCase("FULLAMT_TO_FULLAMT")){
                      txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("THALAYAL_BONUS_HEAD"));
                    }else{
                      txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));  
                    }
                }else if(map.containsKey("MUNNAL_CHITTAL") && !map.get("MUNNAL_CHITTAL").equals("") && map.get("MUNNAL_CHITTAL").equals("Y")){
                    if(munnalTransCategory.equalsIgnoreCase("FULLAMT_TO_FULLAMT")){
                      txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("MUNNAL_BONUS_HEAD"));
                    }else{
                      txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));    
                    }
                }else{
                    System.out.println("RECEIPT_HEAD : " + applicationMap.get("RECEIPT_HEAD") + "Diff :" + "F Head:");
                    txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
                }
            }
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.CR_BRANCH, mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);

            if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
                System.out.println("map.get=ddddd====" + map.get("FROM_PARTICULARS_TRANSALL"));
                txMap.put(TransferTrans.PARTICULARS, map.get("FROM_PARTICULARS_TRANSALL"));
            } else {
                txMap.put(TransferTrans.PARTICULARS, applicationMap.get("MP_MDS_CODE") + "-" + mdsCurrentReceiptEntryTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
            }
            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
            txMap.put("TRANS_MOD_TYPE", "MDS");
          //  System.out.println("txMap : ==== " + txMap + "serviceAmt : ===== " + receivingGLAmt + " productMap====" + productMap);
            //   if(productMap != null && !productMap.isEmpty()){
         //   System.out.println("Third TXTMAP" + txMap);
            transferTo = transactionDAO.addTransferCreditLocal(txMap, receivingGLAmt);
            //   }
            transferTo.setTransId("-");
            transferTo.setBatchId("-");
            transferTo.setTransType(CommonConstants.CREDIT);
            transferTo.setProdType(TransactionFactory.GL);
            transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
            transferTo.setRec_mode(CommonUtil.convertObjToStr(map.get("REC_MODE")));
            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
            transferTo.setInstrumentNo2("BONUS");// Added by nithya on 31-10-2019 for KD-680
            if (map.containsKey("MDS_CLOSURE")) {
                transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("MDS_RECEIVABLE_HEAD")));
            } else {
                if(map.containsKey("THALAYAL_CHITTAL") && !map.get("THALAYAL_CHITTAL").equals("") && map.get("THALAYAL_CHITTAL").equals("Y")){
                    if(thalayalTransCategory.equalsIgnoreCase("FULLAMT_TO_FULLAMT")){
                    transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("THALAYAL_BONUS_HEAD")));
                    }else{
                     transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("RECEIPT_HEAD")));   
                    }
                }else if(map.containsKey("MUNNAL_CHITTAL") && !map.get("MUNNAL_CHITTAL").equals("") && map.get("MUNNAL_CHITTAL").equals("Y")){
                    if(munnalTransCategory.equalsIgnoreCase("FULLAMT_TO_FULLAMT")){
                      transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("MUNNAL_BONUS_HEAD")));
                    }else{
                      transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("RECEIPT_HEAD")));    
                    }
                }else{
                    transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("RECEIPT_HEAD")));
                }
            }
            transferTo.setTransDt(currDt);
            transferTo.setBranchId(mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);
            transferTo.setInitiatedBranch(mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);//bb1
            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));

            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
            transactionTO.setChequeNo("SERVICE_TAX");
            transferTo.setGlTransActNum(mdsCurrentReceiptEntryTO.getChittalNo());
            transferTo.setSingleTransId(generateSingleTransId);
            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
            TxTransferTO.add(transferTo);
      //      System.out.println("ENTER  " + applicationMap + "forFeitedAmt   :" + forFeitedAmt + " TxTransferTO==" + TxTransferTO);
//            System.out.println("transferTo List 8 : " + transferTo);
//            if(applicationMap.get("FORFEITE_HD_Y_N") != null && applicationMap.get("FORFEITE_HD_Y_N").equals("Y")){
            if (forfeiteBonusList != null && forfeiteBonusList.size() > 0) {
                System.out.println("forfeited case");
                if (applicationMap.get("FORFEITE_HD_Y_N") != null && applicationMap.get("FORFEITE_HD_Y_N").equals("Y")) {
                    transferTo = new TxTransferTO();
                    txMap = new HashMap();
                    transferTo.setInstrumentNo2("APPL_GL_TRANS");

                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(graceForFeiteMap.get("FORFEITED_HEAD")));
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.CR_BRANCH, mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);
                //    System.out.println("ist");
                    if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
                      //  System.out.println("map.get=ddddd====" + map.get("FROM_PARTICULARS_TRANSALL"));
                        txMap.put(TransferTrans.PARTICULARS, map.get("FROM_PARTICULARS_TRANSALL"));
                    } else {
                        txMap.put(TransferTrans.PARTICULARS, applicationMap.get("MP_MDS_CODE") + "-" + mdsCurrentReceiptEntryTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
                    }
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
//                    System.out.println("Fifth TXTMAP" + txMap + "   " + forfeiteBonusAmount);
//                    System.out.println("Before " + transferTo);
                    txMap.put("TRANS_MOD_TYPE", "MDS");
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, forfeiteBonusAmount);
                 //   System.out.println("Before " + transferTo);
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setTransType(CommonConstants.CREDIT);
                    transferTo.setProdType(TransactionFactory.GL);
                    transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                    transferTo.setRec_mode(CommonUtil.convertObjToStr(map.get("REC_MODE")));

                    transferTo.setAcHdId(CommonUtil.convertObjToStr(graceForFeiteMap.get("FORFEITED_HEAD")));
                    transferTo.setTransDt(currDt);
                    transferTo.setBranchId(mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);
                    transferTo.setInitiatedBranch(mdsCurrentReceiptEntryTO.getBranchCode());
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));

                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
                    transactionTO.setChequeNo("SERVICE_TAX");
                    transferTo.setGlTransActNum(mdsCurrentReceiptEntryTO.getChittalNo());
                    transferTo.setSingleTransId(generateSingleTransId);
                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                    transferTo.setInstrumentNo2("FORFEITE_BONUS");// Added by nithya on 31-10-2019 for KD-680
                    TxTransferTO.add(transferTo);
                }
            }
          //  System.out.println("transferTo List 8 111: " + TxTransferTO);
            HashMap applnMap = new HashMap();
            map.put("MODE", map.get("COMMAND"));
            map.put("COMMAND", map.get("MODE"));
            map.put("TxTransferTO", TxTransferTO);
            //System.out.println("transferDAO List Last : " + map);
            map.put(CommonConstants.SELECTED_BRANCH_ID,mdsCurrentReceiptEntryTO.getBranchCode());
            if(map.containsKey(CommonConstants.BRANCH_ID) && map.get(CommonConstants.BRANCH_ID) != null && 
            map.containsKey(CommonConstants.SELECTED_BRANCH_ID) && map.get(CommonConstants.SELECTED_BRANCH_ID) != null && 
            !CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)).equals(map.get(CommonConstants.SELECTED_BRANCH_ID))){
                map.put("MDS_BONUS_ENTRY","MDS_BONUS_ENTRY");
                map.put("LOGGED_BRANCH_ID", map.get("BRANCH_CODE"));
            }
            HashMap transMap = transferDAO.execute(map, false);
            System.out.println("transferDAO List transMap : " + transMap);
            mdsCurrentReceiptEntryTO.setBonusTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
            mdsCurrentReceiptEntryTO.setDiscountTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
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
            // linkBatchMap.put("INITIATED_BRANCH", _branchCode);
            sqlMap.executeUpdate("updateLinkBatchIdTransferMDS", linkBatchMap);
            linkBatchMap = null;
            transMap = null;
        }
    }

    public void commonTransactionCashandTransferBankAdv(HashMap map, MDSReceiptEntryTO mdsBankAdvReceiptEntryTO, HashMap debitMap, HashMap applicationMap) throws Exception {
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
        if (futureAdvbonusAmt > 0) {
//            System.out.println("Future Bonus Started");
            transferTo = new TxTransferTO();
            txMap = new HashMap();
            transferTo.setInstrumentNo2("APPL_GL_TRANS");
            //Added By Suresh
            if (map.containsKey("MDS_CLOSURE")) {
                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("SUNDRY_PAYMENT_HEAD"));
            } else {
                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("BONUS_RECEIVABLE_HEAD"));
            }
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.DR_BRANCH, mdsBankAdvReceiptEntryTO.getBranchCode());//BRANCH_ID);;
            txMap.put(TransferTrans.PARTICULARS, "Bonus :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsBankAdvReceiptEntryTO.getChittalNo() + "_" + mdsBankAdvReceiptEntryTO.getSubNo());
            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
            txMap.put("TRANS_MOD_TYPE", "MDS");
//            System.out.println("txMap : " + txMap+"serviceAmt :"+futureAdvbonusAmt);
            transferTo = transactionDAO.addTransferDebitLocal(txMap, futureAdvbonusAmt);
            transferTo.setTransId("-");
            transferTo.setBatchId("-");
            transferTo.setTransType(CommonConstants.DEBIT);
            transferTo.setProdType(TransactionFactory.GL);
            transferTo.setTransDt(currDt);
            transferTo.setBranchId(mdsBankAdvReceiptEntryTO.getBranchCode());//BRANCH_ID);;
            transferTo.setInitiatedBranch(BRANCH_ID);
            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
            transferTo.setRec_mode(CommonUtil.convertObjToStr(map.get("REC_MODE")));
            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));
            transactionTO.setChequeNo("SERVICE_TAX");
            transferTo.setGlTransActNum(mdsBankAdvReceiptEntryTO.getChittalNo());
            transferTo.setSingleTransId(generateSingleTransId);
            TxTransferTO.add(transferTo);
//            System.out.println("transferTo List 6 : " + transferTo);
        }
//        System.out.println("TxTransferTO List 6 : " + TxTransferTO);

        double receivingGLAmt = futureAdvbonusAmt;
//        System.out.println("transferTo List 7 : " + TxTransferTO+ "receivingGLAmt : "+receivingGLAmt);
        if (receivingGLAmt > 0) {
//            System.out.println("GL Final Transaction Started");
            transferTo = new TxTransferTO();
            txMap = new HashMap();
            transferTo.setInstrumentNo2("APPL_GL_TRANS");
            //Added By Suresh
            if (map.containsKey("MDS_CLOSURE")) {
                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("MDS_RECEIVABLE_HEAD"));
            } else {
                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
            }
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.CR_BRANCH, mdsBankAdvReceiptEntryTO.getBranchCode());//BRANCH_ID);;
            txMap.put(TransferTrans.PARTICULARS, applicationMap.get("MP_MDS_CODE") + "-" + mdsBankAdvReceiptEntryTO.getChittalNo() + "_" + mdsBankAdvReceiptEntryTO.getSubNo());
            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
            txMap.put("TRANS_MOD_TYPE", "MDS");
//            System.out.println("txMap : " + txMap+"serviceAmt :"+receivingGLAmt);
            transferTo = transactionDAO.addTransferDebitLocal(txMap, receivingGLAmt);
            transferTo.setTransId("-");
            transferTo.setBatchId("-");
            transferTo.setTransType(CommonConstants.CREDIT);
            transferTo.setProdType(TransactionFactory.GL);
            transferTo.setTransDt(currDt);
            transferTo.setBranchId(mdsBankAdvReceiptEntryTO.getBranchCode());//BRANCH_ID);;
            transferTo.setInitiatedBranch(BRANCH_ID);
            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
            transferTo.setRec_mode(CommonUtil.convertObjToStr(map.get("REC_MODE")));

            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));
            transactionTO.setChequeNo("SERVICE_TAX");
            transferTo.setGlTransActNum(mdsBankAdvReceiptEntryTO.getChittalNo());
            transferTo.setSingleTransId(generateSingleTransId);
            TxTransferTO.add(transferTo);

//            System.out.println("transferTo List 8 : " + TxTransferTO);
            HashMap applnMap = new HashMap();
            map.put("MODE", map.get("COMMAND"));
            map.put("COMMAND", map.get("MODE"));
            map.put("TxTransferTO", TxTransferTO);
//            System.out.println("transferDAO List Last : " + map);
            map.put("TRANS_MOD_TYPE_MMBS", CommonConstants.MMBS);
            HashMap transMap = transferDAO.execute(map, false);
//            System.out.println("transferDAO List transMap : " + transMap);
            mdsBankAdvReceiptEntryTO.setBonusTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
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
    }

    public void commonTransactionTransferBankAdvBonus(HashMap map, MDSReceiptEntryTO mdsBankAdvReceiptEntryTO, HashMap debitMap, HashMap applicationMap) throws Exception {
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
        System.out.println("applicationMap================" + applicationMap);
//        if (CommonUtil.convertObjToDouble(map.get("BONUS_LIST_NEW")) > 0) {
////            System.out.println("Future Bonus Started");
//            transferTo = new TxTransferTO();
//            txMap = new HashMap();
//            transferTo.setInstrumentNo2("APPL_GL_TRANS");
//            //Added By Suresh
//            if (map.containsKey("MDS_CLOSURE")) {
//                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("SUNDRY_PAYMENT_HEAD"));
//            } else {
//                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("BONUS_RECEIVABLE_HEAD"));
//            }
//            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
//            txMap.put(TransferTrans.DR_BRANCH, mdsBankAdvReceiptEntryTO.getBranchCode());//BRANCH_ID);;
//            txMap.put(TransferTrans.PARTICULARS, "Bonus :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsBankAdvReceiptEntryTO.getChittalNo() + "_" + mdsBankAdvReceiptEntryTO.getSubNo());
//            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
//            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
//            txMap.put("TRANS_MOD_TYPE", "MDS");
////            System.out.println("txMap : " + txMap+"serviceAmt :"+futureAdvbonusAmt);
//            transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(map.get("BONUS_LIST_NEW")));
//            transferTo.setTransId("-");
//            transferTo.setBatchId("-");
//            transferTo.setTransType(CommonConstants.DEBIT);
//            transferTo.setProdType(TransactionFactory.GL);
//            transferTo.setTransDt(currDt);
//            transferTo.setBranchId(mdsBankAdvReceiptEntryTO.getBranchCode());//BRANCH_ID);;
//            transferTo.setInitiatedBranch(BRANCH_ID);
//            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
//            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
//            transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
//            transferTo.setRec_mode(CommonUtil.convertObjToStr(map.get("REC_MODE")));
//
//            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));
//            transactionTO.setChequeNo("SERVICE_TAX");
//            transferTo.setGlTransActNum(mdsBankAdvReceiptEntryTO.getChittalNo());
//            transferTo.setSingleTransId(generateSingleTransId);
//            TxTransferTO.add(transferTo);
//        }
        double receivingGLAmt = CommonUtil.convertObjToDouble(map.get("BONUS_LIST_NEW"));
        if (receivingGLAmt > 0) {
            transferTo = new TxTransferTO();
            txMap = new HashMap();
            transferTo.setInstrumentNo2("APPL_GL_TRANS");
            //Added By Suresh
            if (map.containsKey("MDS_CLOSURE")) {
                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("MDS_RECEIVABLE_HEAD"));
            } else {
                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
            }
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.CR_BRANCH, mdsBankAdvReceiptEntryTO.getBranchCode());//BRANCH_ID);;
            txMap.put(TransferTrans.PARTICULARS, applicationMap.get("MP_MDS_CODE") + "-" + mdsBankAdvReceiptEntryTO.getChittalNo() + "_" + mdsBankAdvReceiptEntryTO.getSubNo());
            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
//            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
            txMap.put("TRANS_MOD_TYPE", "MDS");
//            System.out.println("txMap : " + txMap+"serviceAmt :"+receivingGLAmt);
            transferTo = transactionDAO.addTransferDebitLocal(txMap, receivingGLAmt);
            transferTo.setTransId("-");
            transferTo.setBatchId("-");
            transferTo.setTransType(CommonConstants.CREDIT);
            transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("RECEIPT_HEAD")));
            transferTo.setProdType(TransactionFactory.GL);
            transferTo.setTransDt(currDt);
            transferTo.setBranchId(mdsBankAdvReceiptEntryTO.getBranchCode());//BRANCH_ID);;
            transferTo.setInitiatedBranch(BRANCH_ID);
            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
            transferTo.setRec_mode(CommonUtil.convertObjToStr(map.get("REC_MODE")));
            transferTo.setGlTransActNum(mdsBankAdvReceiptEntryTO.getChittalNo());
            transferTo.setSingleTransId(generateSingleTransId);
            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));
            transactionTO.setChequeNo("SERVICE_TAX");
            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
            TxTransferTO.add(transferTo);

            //System.out.println("transferTo List 8888 : " + TxTransferTO);
            HashMap applnMap = new HashMap();
            map.put("MODE", map.get("COMMAND"));
            map.put("COMMAND", map.get("MODE"));
            map.put("TxTransferTO", TxTransferTO);
        //    System.out.println("transferDAO List Last 8888: " + map);
            HashMap transMap = transferDAO.execute(map, false);
//            System.out.println("transferDAO List transMap : " + transMap);
            mdsBankAdvReceiptEntryTO.setBonusTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
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
    }

    private void updateData(HashMap chargesMap, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        deleteData(chargesMap, objLogDAO, objLogTO);
        System.out.println("mdsReceiptEntryTO :" + mdsReceiptEntryTO);
        sqlMap.executeUpdate("updateReceiptEntryTO", mdsReceiptEntryTO);
    }

    private ArrayList applicationUpdate(HashMap chargesMap) throws Exception {//updating Transaction
        System.out.println("chargesMap :" + chargesMap);
        ArrayList cashList = new ArrayList();
        if (mdsReceiptEntryTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
            HashMap tempMap = (HashMap) chargesMap.get("SERVICE_TAX_AMT_TRANSACTION");
            String trans_id = CommonUtil.convertObjToStr(mdsReceiptEntryTO.getNetTransId());
            CashTransactionTO objCashTO = new CashTransactionTO();
            objCashTO = new CashTransactionTO();
            objCashTO.setInpAmount(mdsReceiptEntryTO.getNetAmt());
            objCashTO.setAmount(mdsReceiptEntryTO.getNetAmt());
            objCashTO.setCommand(mdsReceiptEntryTO.getCommand());
            cashList.add(objCashTO);
            objCashTO = null;
        }
        return cashList;
    }

    private void deleteData(HashMap chargesMap, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        ArrayList transferList = new ArrayList();
        TransferTrans transferTrans = new TransferTrans();
        TxTransferTO transferTo = new TxTransferTO();
        ArrayList TxTransferTO = new ArrayList();
        TransactionTO transactionTO = new TransactionTO();
        HashMap txMap = new HashMap();
        String BRANCH_ID = CommonUtil.convertObjToStr(chargesMap.get("BRANCH_CODE"));
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
            mdsReceiptEntryTO.setStatus(CommonConstants.STATUS_DELETED);
            chargesMap.put(CommonConstants.TRANS_ID, chargesMap.get("NET_TRANS_ID"));
            chargesMap.put("TRANS_DT", currDt);
            chargesMap.put("INITIATED_BRANCH", _branchCode);
            List lst = (List) sqlMap.executeQueryForList("getSelectParticularTransList", chargesMap);
            HashMap transactionMap = new HashMap();
            if (lst != null && lst.size() > 0) {
                transactionMap = (HashMap) lst.get(0);
            }
            if (transactionTO.getTransType().equals("TRANSFER") && transactionMap != null && transactionMap.get("NET_TRANS_ID") != null && !transactionMap.get("NET_TRANS_ID").equals("")
                    && chargesMap.containsKey("NET_TRANSACTION_TRANSFER") && CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNetAmt()).doubleValue() > 0) {
                tempMap = (HashMap) chargesMap.get("NET_TRANSACTION_TRANSFER");
                System.out.println("#### tempMap : " + tempMap);
                oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                tempMap.put("TRANS_DT", currDt);
                tempMap.put("INITIATED_BRANCH", BRANCH_ID);
                tempMap.remove("TRANS_ID");
                tempMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(BRANCH_ID));
                mdsReceiptEntryTO.setNetTransId(CommonUtil.convertObjToStr(tempMap.get("BATCH_ID")));
                double newAmount = CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNetAmt()).doubleValue();
                ArrayList batchList = new ArrayList();
                TxTransferTO txTransferTO = null;
                HashMap oldAmountMap = new HashMap();
                String commandMode = CommonUtil.convertObjToStr(mdsReceiptEntryTO.getCommand());
                if (mdsReceiptEntryTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                    lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                    if (lst != null) {
                        for (int i = 0; i < lst.size(); i++) {
                            txTransferTO = (TxTransferTO) lst.get(i);
                            if (mdsReceiptEntryTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
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
            if (transactionMap != null && transactionMap.get("BONUS_TRANS_ID") != null && !transactionMap.get("BONUS_TRANS_ID").equals("")
                    && chargesMap.containsKey("BONUS_TRANSACTION_TRANSFER") && CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getBonusAmtPayable()).doubleValue() > 0) {
                tempMap = (HashMap) chargesMap.get("BONUS_TRANSACTION_TRANSFER");
                System.out.println("#### tempMap : " + tempMap);
                oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                tempMap.put("TRANS_DT", currDt);
                tempMap.put("INITIATED_BRANCH", BRANCH_ID);
                tempMap.remove("TRANS_ID");
                tempMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(BRANCH_ID));
                double newAmount = CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNetAmt()).doubleValue();
                ArrayList batchList = new ArrayList();
                TxTransferTO txTransferTO = null;
                HashMap oldAmountMap = new HashMap();
                String commandMode = CommonUtil.convertObjToStr(mdsReceiptEntryTO.getCommand());
                if (mdsReceiptEntryTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                    lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                    if (lst != null) {
                        for (int i = 0; i < lst.size(); i++) {
                            txTransferTO = (TxTransferTO) lst.get(i);
                            if (mdsReceiptEntryTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
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
            if (transactionMap != null && transactionMap.get("DISCOUNT_TRANS_ID") != null && !transactionMap.get("DISCOUNT_TRANS_ID").equals("")
                    && chargesMap.containsKey("DISCOUNT_TRANSACTION_TRANSFER") && CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getDiscountAmt()).doubleValue() > 0) {
                tempMap = (HashMap) chargesMap.get("DISCOUNT_TRANSACTION_TRANSFER");
                System.out.println("#### tempMap : " + tempMap);
                oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                tempMap.put("TRANS_DT", currDt);
                tempMap.put("INITIATED_BRANCH", BRANCH_ID);
                tempMap.remove("TRANS_ID");
                tempMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(BRANCH_ID));
                double newAmount = CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNetAmt()).doubleValue();
                ArrayList batchList = new ArrayList();
                TxTransferTO txTransferTO = null;
                HashMap oldAmountMap = new HashMap();
                String commandMode = CommonUtil.convertObjToStr(mdsReceiptEntryTO.getCommand());
                if (mdsReceiptEntryTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                    lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                    if (lst != null) {
                        for (int i = 0; i < lst.size(); i++) {
                            txTransferTO = (TxTransferTO) lst.get(i);
                            if (mdsReceiptEntryTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
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
            if (transactionTO.getTransType().equals("CASH") && transactionMap != null && transactionMap.get("NET_TRANS_ID") != null && !transactionMap.get("NET_TRANS_ID").equals("")
                    && chargesMap.containsKey("NET_TRANSACTION_CASH") && CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNetAmt()).doubleValue() > 0) {
                tempMap = (HashMap) chargesMap.get("NET_TRANSACTION_CASH");
                System.out.println("#### inside CASH tempMap : " + tempMap);
                oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                CashTransactionDAO cashDAO = new CashTransactionDAO();
                CashTransactionTO cashTO = new CashTransactionTO();
                HashMap cashMap = new HashMap();
                String transId = CommonUtil.convertObjToStr(tempMap.get("TRANS_ID"));
                cashMap.put("TRANS_ID", tempMap.get("TRANS_ID"));
                cashMap.put("TRANS_DT", currDt);
                cashMap.put("INITIATED_BRANCH", BRANCH_ID);
                cashMap.put("SCREEN_NAME", "MDS Receipts");

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
            if (transactionTO.getTransType().equals("CASH") && transactionMap != null && transactionMap.get("PENAL_TRANS_ID") != null && !transactionMap.get("PENAL_TRANS_ID").equals("")
                    && chargesMap.containsKey("PENAL_TRANSACTION_CASH") && CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getPenalAmtPayable()).doubleValue() > 0) {
                tempMap = (HashMap) chargesMap.get("PENAL_TRANSACTION_CASH");
                System.out.println("#### inside CASH tempMap : " + tempMap);
                oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                CashTransactionDAO cashDAO = new CashTransactionDAO();
                CashTransactionTO cashTO = new CashTransactionTO();
                HashMap cashMap = new HashMap();
                String transId = CommonUtil.convertObjToStr(tempMap.get("TRANS_ID"));
                cashMap.put("TRANS_ID", tempMap.get("TRANS_ID"));
                cashMap.put("TRANS_DT", currDt);
                cashMap.put("INITIATED_BRANCH", BRANCH_ID);
                cashMap.put("SCREEN_NAME", "MDS Receipts");
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
            if (transactionTO.getTransType().equals("CASH") && transactionMap != null && transactionMap.get("ARBITRATION_ID") != null && !transactionMap.get("ARBITRATION_ID").equals("")
                    && chargesMap.containsKey("ARBITRATION_TRANSACTION_CASH") && CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getArbitrationAmt()).doubleValue() > 0) {
                tempMap = (HashMap) chargesMap.get("ARBITRATION_TRANSACTION_CASH");
                mdsReceiptEntryTO.setNetTransId(CommonUtil.convertObjToStr(tempMap.get("BATCH_ID")));
                System.out.println("#### inside CASH tempMap : " + tempMap);
                oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                CashTransactionDAO cashDAO = new CashTransactionDAO();
                CashTransactionTO cashTO = new CashTransactionTO();
                HashMap cashMap = new HashMap();
                String transId = CommonUtil.convertObjToStr(tempMap.get("TRANS_ID"));
                cashMap.put("TRANS_ID", tempMap.get("TRANS_ID"));
                cashMap.put("TRANS_DT", currDt);
                cashMap.put("INITIATED_BRANCH", BRANCH_ID);
                cashMap.put("SCREEN_NAME", "MDS Receipts");
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
            if (transactionTO.getTransType().equals("CASH") && transactionMap != null && transactionMap.get("NOTICE_ID") != null && !transactionMap.get("NOTICE_ID").equals("")
                    && chargesMap.containsKey("NOTICE_TRANSACTION_CASH") && CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNoticeAmt()).doubleValue() > 0) {
                tempMap = (HashMap) chargesMap.get("NOTICE_TRANSACTION_CASH");
                mdsReceiptEntryTO.setNetTransId(CommonUtil.convertObjToStr(tempMap.get("BATCH_ID")));
                System.out.println("#### inside CASH tempMap : " + tempMap);
                oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                CashTransactionDAO cashDAO = new CashTransactionDAO();
                CashTransactionTO cashTO = new CashTransactionTO();
                HashMap cashMap = new HashMap();
                String transId = CommonUtil.convertObjToStr(tempMap.get("TRANS_ID"));
                cashMap.put("TRANS_ID", tempMap.get("TRANS_ID"));
                cashMap.put("TRANS_DT", currDt);
                cashMap.put("INITIATED_BRANCH", BRANCH_ID);
                cashMap.put("SCREEN_NAME", "MDS Receipts");
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
            deleteMap = null;
            tempMap = null;
            mdsReceiptEntryTO.setNetTransId(CommonUtil.convertObjToStr(transactionMap.get("NET_TRANS_ID")));
            mdsReceiptEntryTO.setStatusDt((Date)currDt.clone());
            System.out.println("mdsReceiptEntryTO :" + mdsReceiptEntryTO);
            sqlMap.executeUpdate("deleteReceiptEntryTO", mdsReceiptEntryTO);
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

    private void authorize(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        System.out.println("AuthorizeMap :" + map);
        try {
            HashMap authorizeMap = new HashMap();
            HashMap authMap = new HashMap();
            HashMap instMap = new HashMap();
            authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
            authMap = (HashMap) map.get("AUTHORIZEMAP");
            String transId = CommonUtil.convertObjToStr(authorizeMap.get("NET_TRANS_ID"));
            String authorizeStatus = CommonUtil.convertObjToStr(authorizeMap.get("AUTHORIZESTATUS"));
            if(mdsReceiptEntryTO.getChittalNo().length() == 0 || mdsReceiptEntryTO.getChittalNo().equals("") || mdsReceiptEntryTO.getChittalNo() == null){
                throw new TTException("Authorization data null.. Try again..");
            }
            TransactionTO transactionTO = new TransactionTO();
            if (map.containsKey("TransactionTO")) {
                if (map.containsKey("MDS_TRANS_ALL_AUTHORIZATION")) {
                    transactionTO = (TransactionTO) map.get("TransactionTO");
                } else {
                    HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                    if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                        allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                    }
                    transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                }
                HashMap serviceAuthMap = new HashMap();
                if (transactionTO.getTransType().equals("TRANSFER") || transactionTO.getTransType().equals("CASH")) {
                    authorizeMap.put(CommonConstants.TRANS_ID, authorizeMap.get("NET_TRANS_ID"));
                    authorizeMap.put("TRANS_DT", setProperDtFormat(currDt));
                    authorizeMap.put("INITIATED_BRANCH", _branchCode);
                    List lst = (List) sqlMap.executeQueryForList("getSelectParticularTransList", authorizeMap);
                    HashMap transactionMap = new HashMap();
                    if (lst != null && lst.size() > 0) {
                        transactionMap = (HashMap) lst.get(0);
                    }
                    if (transactionMap != null && transactionMap.get("NET_TRANS_ID") != null && !transactionMap.get("NET_TRANS_ID").equals("")) {
                        String linkBatchId = CommonUtil.convertObjToStr(transactionMap.get("NET_TRANS_ID"));
                        HashMap cashAuthMap = new HashMap();
                        cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                        cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                        cashAuthMap.put("DEBIT_LOAN_TYPE", "DP");
                        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                        serviceAuthMap.put("AUTHORIZE_STATUS", authorizeStatus);
                        serviceAuthMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                        ArrayList transList = new ArrayList();
                        if (linkBatchId.indexOf(",") != -1) {
                            transList.add(linkBatchId.substring(0, linkBatchId.indexOf(",")));
                            transList.add(linkBatchId.substring(linkBatchId.indexOf(",") + 1, linkBatchId.length()));
                        } else {
                            transList.add(linkBatchId);
                        }
                        for (int i = 0; i < transList.size(); i++) {
                            linkBatchId = CommonUtil.convertObjToStr(transList.get(i));
                            serviceAuthMap.put("TRANS_ID", linkBatchId);
                            TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
                        }
                        cashAuthMap = null;
                    }
                    if (transactionTO.getTransType().equals("CASH")) {
                        if (transactionMap != null && transactionMap.get("ARBITRATION_ID") != null && !transactionMap.get("ARBITRATION_ID").equals("")) {
                            //Changed By Suresh
                            CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                            ArrayList arrList = new ArrayList();
                            authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
                            HashMap singleAuthorizeMap = new HashMap();
                            singleAuthorizeMap.put("STATUS", authorizeStatus);
                            singleAuthorizeMap.put("TRANS_ID", transactionMap.get("ARBITRATION_ID"));
                            singleAuthorizeMap.put("USER_ID", map.get("USER_ID"));
                            arrList.add(singleAuthorizeMap);
                            String branchCode = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
                            String userId = CommonUtil.convertObjToStr(map.get("USER_ID"));
                            map.put("SCREEN", "Cash");
                            map.put("USER_ID", userId);
                            map.put("SELECTED_BRANCH_ID", branchCode);
                            map.put("BRANCH_CODE", branchCode);
                            map.put("MODULE", "Transaction");
                            map.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                            HashMap dataMap = new HashMap();
                            dataMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                            dataMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                            dataMap.put("DAILY", "DAILY");
                            map.put(CommonConstants.AUTHORIZEMAP, dataMap);
                            cashTransactionDAO.execute(map, false);
                            cashTransactionDAO = null;
                            dataMap = null;
                        }
                        if (transactionMap != null && transactionMap.get("NOTICE_ID") != null && !transactionMap.get("NOTICE_ID").equals("")) {
                            System.out.println("TRANSFER NOTICE_ID :" + transactionMap.get("NOTICE_ID"));
                            CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                            ArrayList arrList = new ArrayList();
                            authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
                            HashMap singleAuthorizeMap = new HashMap();
                            singleAuthorizeMap.put("STATUS", authorizeStatus);
                            singleAuthorizeMap.put("TRANS_ID", transactionMap.get("NOTICE_ID"));
                            singleAuthorizeMap.put("USER_ID", map.get("USER_ID"));
                            arrList.add(singleAuthorizeMap);
                            String branchCode = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
                            String userId = CommonUtil.convertObjToStr(map.get("USER_ID"));
                            map.put("SCREEN", "Cash");
                            map.put("USER_ID", userId);
                            map.put("SELECTED_BRANCH_ID", branchCode);
                            map.put("BRANCH_CODE", branchCode);
                            map.put("MODULE", "Transaction");
                            map.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                            HashMap dataMap = new HashMap();
                            dataMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                            dataMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                            dataMap.put("DAILY", "DAILY");
                            map.put(CommonConstants.AUTHORIZEMAP, dataMap);
                            cashTransactionDAO.execute(map, false);
                            cashTransactionDAO = null;
                            dataMap = null;
                        }
                    }
                    //                if (transactionTO.getTransType().equals("TRANSFER")){
                    if (transactionMap != null && transactionMap.containsKey("BONUS_TRANS_ID") && transactionMap.get("BONUS_TRANS_ID") != null && !transactionMap.get("BONUS_TRANS_ID").equals("")) {
                        String branchCode = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
                        serviceAuthMap = new HashMap();
                        String linkBatchId = CommonUtil.convertObjToStr(transactionMap.get("BONUS_TRANS_ID"));
                        HashMap cashAuthMap = new HashMap();
                        cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                        cashAuthMap.put("DEBIT_LOAN_TYPE", "DP");
                        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                        serviceAuthMap.put("AUTHORIZE_STATUS", authorizeStatus);
                        serviceAuthMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                        ArrayList transList = new ArrayList();
                        if (linkBatchId.indexOf(",") != -1) {
                            transList.add(linkBatchId.substring(0, linkBatchId.indexOf(",")));
                            transList.add(linkBatchId.substring(linkBatchId.indexOf(",") + 1, linkBatchId.length()));
                        } else {
                            transList.add(linkBatchId);
                        }
                        String linkId = "";
                        for (int i = 0; i < transList.size(); i++) {
                            linkId = CommonUtil.convertObjToStr(transList.get(i));
                        }
                        HashMap map1 = new HashMap();
                        map1.put("LINK_BATCH_ID", linkId);
                        map1.put("TRANS_DT", currDt);
                        map1.put("SINGLE_TRANS_ID", CommonUtil.convertObjToStr(transactionMap.get("SINGLE_TRANS_ID")));//Bonus cant authrotized beacause INITIATED_BRANCH have more than one rows so cant authorize
                        List dataList = ServerUtil.executeQuery("getInitBrId", map1);
                        String initBrId = "";
                        if (dataList.size() > 0 && dataList.get(0) != null) {
                            HashMap mapop = (HashMap) dataList.get(0);
                            initBrId = CommonUtil.convertObjToStr(mapop.get("INITIATED_BRANCH"));
                        }
                        map.put(CommonConstants.SELECTED_BRANCH_ID, initBrId);
                        if (map.containsKey(CommonConstants.BRANCH_ID) && map.get(CommonConstants.BRANCH_ID) != null
                                && map.containsKey(CommonConstants.SELECTED_BRANCH_ID) && map.get(CommonConstants.SELECTED_BRANCH_ID) != null
                                && !CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)).equals(map.get(CommonConstants.SELECTED_BRANCH_ID))) {
                            cashAuthMap.put("LOGGED_BRANCH_ID", map.get("BRANCH_CODE"));
                            cashAuthMap.put("MDS_BONUS_ENTRY", "MDS_BONUS_ENTRY");
                        }
                        cashAuthMap.put(CommonConstants.BRANCH_ID, initBrId);// map.get(CommonConstants.BRANCH_ID));
                        if (initBrId != null && !initBrId.equals("")) {
                            for (int i = 0; i < transList.size(); i++) {
                                linkBatchId = CommonUtil.convertObjToStr(transList.get(i));
                                serviceAuthMap.put("TRANS_ID", linkBatchId);
                                TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
                            }
                        }
                        cashAuthMap = null;
                    }
                    if (transactionMap != null && transactionMap.get("DISCOUNT_TRANS_ID") != null && !transactionMap.get("DISCOUNT_TRANS_ID").equals("")) {
                        //                        serviceAuthMap = new HashMap();
                        serviceAuthMap.put("AUTHORIZE_STATUS", authorizeStatus);
                        serviceAuthMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                        serviceAuthMap.put("TRANS_ID", transactionMap.get("DISCOUNT_TRANS_ID"));
                        String linkBatchId = CommonUtil.convertObjToStr(transactionMap.get("DISCOUNT_TRANS_ID"));
                        HashMap cashAuthMap = new HashMap();
                        cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                        cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                        cashAuthMap.put("DEBIT_LOAN_TYPE", "DP");
                        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                        TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
                        cashAuthMap = null;
                    }

                    if (transactionMap != null && transactionMap.get("PENAL_TRANS_ID") != null && !transactionMap.get("PENAL_TRANS_ID").equals("")) {
                        serviceAuthMap = new HashMap();
                        serviceAuthMap.put("AUTHORIZE_STATUS", authorizeStatus);
                        serviceAuthMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                        serviceAuthMap.put("TRANS_ID", transactionMap.get("PENAL_TRANS_ID"));
                        String linkBatchId = CommonUtil.convertObjToStr(transactionMap.get("PENAL_TRANS_ID"));
                        HashMap cashAuthMap = new HashMap();
                        cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                        cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                        cashAuthMap.put("DEBIT_LOAN_TYPE", "DP");
                        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                        TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
                        cashAuthMap = null;
                    }
                    
                    //2093
                    if (transactionMap != null && transactionMap.get("FORFEITED_TRANS_ID") != null && !transactionMap.get("FORFEITED_TRANS_ID").equals("")) {
                        serviceAuthMap = new HashMap();
                        serviceAuthMap.put("AUTHORIZE_STATUS", authorizeStatus);
                        serviceAuthMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                        serviceAuthMap.put("TRANS_ID", transactionMap.get("FORFEITED_TRANS_ID"));
                        String linkBatchId = CommonUtil.convertObjToStr(transactionMap.get("FORFEITED_TRANS_ID"));
                        HashMap cashAuthMap = new HashMap();
                        cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                        cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                        cashAuthMap.put("DEBIT_LOAN_TYPE", "DP");
                        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                        TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
                        cashAuthMap = null;
                    }
                    // End
                    
                    // For Waive
                    
                       if (transactionMap != null && transactionMap.containsKey("PENAL_WAIVE_TRANS_ID") && transactionMap.get("PENAL_WAIVE_TRANS_ID") != null && !transactionMap.get("PENAL_WAIVE_TRANS_ID").equals("")) {
                        String branchCode = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
                        serviceAuthMap = new HashMap();
                        String linkBatchId = CommonUtil.convertObjToStr(transactionMap.get("PENAL_WAIVE_TRANS_ID"));
                        HashMap cashAuthMap = new HashMap();
                        cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                        cashAuthMap.put("DEBIT_LOAN_TYPE", "DP");
                        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                        serviceAuthMap.put("AUTHORIZE_STATUS", authorizeStatus);
                        serviceAuthMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                        ArrayList transList = new ArrayList();
                        if (linkBatchId.indexOf(",") != -1) {
                            transList.add(linkBatchId.substring(0, linkBatchId.indexOf(",")));
                            transList.add(linkBatchId.substring(linkBatchId.indexOf(",") + 1, linkBatchId.length()));
                        } else {
                            transList.add(linkBatchId);
                        }
                        String linkId = "";
                        for (int i = 0; i < transList.size(); i++) {
                            linkId = CommonUtil.convertObjToStr(transList.get(i));
                        }
                        HashMap map1 = new HashMap();
                        map1.put("LINK_BATCH_ID", linkId);
                        map1.put("TRANS_DT", currDt);
                        map1.put("SINGLE_TRANS_ID", CommonUtil.convertObjToStr(transactionMap.get("SINGLE_TRANS_ID")));//Bonus cant authrotized beacause INITIATED_BRANCH have more than one rows so cant authorize
                        List dataList = ServerUtil.executeQuery("getInitBrId", map1);
                        String initBrId = "";
                        if (dataList.size() > 0 && dataList.get(0) != null) {
                            HashMap mapop = (HashMap) dataList.get(0);
                            initBrId = CommonUtil.convertObjToStr(mapop.get("INITIATED_BRANCH"));
                        }
                        map.put(CommonConstants.SELECTED_BRANCH_ID, initBrId);
                        cashAuthMap.put(CommonConstants.BRANCH_ID, initBrId);// map.get(CommonConstants.BRANCH_ID));
                        if (initBrId != null && !initBrId.equals("")) {
                            for (int i = 0; i < transList.size(); i++) {
                                linkBatchId = CommonUtil.convertObjToStr(transList.get(i));
                                serviceAuthMap.put("TRANS_ID", linkBatchId);
                                TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
                            }
                        }
                        cashAuthMap = null;
                    }
                     if (transactionMap != null && transactionMap.containsKey("ARC_WAIVE_TRANS_ID") && transactionMap.get("ARC_WAIVE_TRANS_ID") != null && !transactionMap.get("ARC_WAIVE_TRANS_ID").equals("")) {
                        String branchCode = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
                        serviceAuthMap = new HashMap();
                        String linkBatchId = CommonUtil.convertObjToStr(transactionMap.get("ARC_WAIVE_TRANS_ID"));
                        HashMap cashAuthMap = new HashMap();
                        cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                        cashAuthMap.put("DEBIT_LOAN_TYPE", "DP");
                        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                        serviceAuthMap.put("AUTHORIZE_STATUS", authorizeStatus);
                        serviceAuthMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                        ArrayList transList = new ArrayList();
                        if (linkBatchId.indexOf(",") != -1) {
                            transList.add(linkBatchId.substring(0, linkBatchId.indexOf(",")));
                            transList.add(linkBatchId.substring(linkBatchId.indexOf(",") + 1, linkBatchId.length()));
                        } else {
                            transList.add(linkBatchId);
                        }
                        String linkId = "";
                        for (int i = 0; i < transList.size(); i++) {
                            linkId = CommonUtil.convertObjToStr(transList.get(i));
                        }
                        HashMap map1 = new HashMap();
                        map1.put("LINK_BATCH_ID", linkId);
                        map1.put("TRANS_DT", currDt);
                        map1.put("SINGLE_TRANS_ID", CommonUtil.convertObjToStr(transactionMap.get("SINGLE_TRANS_ID")));//Bonus cant authrotized beacause INITIATED_BRANCH have more than one rows so cant authorize
                        List dataList = ServerUtil.executeQuery("getInitBrId", map1);
                        String initBrId = "";
                        if (dataList.size() > 0 && dataList.get(0) != null) {
                            HashMap mapop = (HashMap) dataList.get(0);
                            initBrId = CommonUtil.convertObjToStr(mapop.get("INITIATED_BRANCH"));
                        }
                        map.put(CommonConstants.SELECTED_BRANCH_ID, initBrId);
                        cashAuthMap.put(CommonConstants.BRANCH_ID, initBrId);// map.get(CommonConstants.BRANCH_ID));
                        if (initBrId != null && !initBrId.equals("")) {
                            for (int i = 0; i < transList.size(); i++) {
                                linkBatchId = CommonUtil.convertObjToStr(transList.get(i));
                                serviceAuthMap.put("TRANS_ID", linkBatchId);
                                TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
                            }
                        }
                        cashAuthMap = null;
                    }
                      if (transactionMap != null && transactionMap.containsKey("NOTICE_WAIVE_TRANS_ID") && transactionMap.get("NOTICE_WAIVE_TRANS_ID") != null && !transactionMap.get("NOTICE_WAIVE_TRANS_ID").equals("")) {
                        String branchCode = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
                        serviceAuthMap = new HashMap();
                        String linkBatchId = CommonUtil.convertObjToStr(transactionMap.get("NOTICE_WAIVE_TRANS_ID"));
                        HashMap cashAuthMap = new HashMap();
                        cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                        cashAuthMap.put("DEBIT_LOAN_TYPE", "DP");
                        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                        serviceAuthMap.put("AUTHORIZE_STATUS", authorizeStatus);
                        serviceAuthMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                        ArrayList transList = new ArrayList();
                        if (linkBatchId.indexOf(",") != -1) {
                            transList.add(linkBatchId.substring(0, linkBatchId.indexOf(",")));
                            transList.add(linkBatchId.substring(linkBatchId.indexOf(",") + 1, linkBatchId.length()));
                        } else {
                            transList.add(linkBatchId);
                        }
                        String linkId = "";
                        for (int i = 0; i < transList.size(); i++) {
                            linkId = CommonUtil.convertObjToStr(transList.get(i));
                        }
                        HashMap map1 = new HashMap();
                        map1.put("LINK_BATCH_ID", linkId);
                        map1.put("TRANS_DT", currDt);
                        map1.put("SINGLE_TRANS_ID", CommonUtil.convertObjToStr(transactionMap.get("SINGLE_TRANS_ID")));//Bonus cant authrotized beacause INITIATED_BRANCH have more than one rows so cant authorize
                        List dataList = ServerUtil.executeQuery("getInitBrId", map1);
                        String initBrId = "";
                        if (dataList.size() > 0 && dataList.get(0) != null) {
                            HashMap mapop = (HashMap) dataList.get(0);
                            initBrId = CommonUtil.convertObjToStr(mapop.get("INITIATED_BRANCH"));
                        }
                        map.put(CommonConstants.SELECTED_BRANCH_ID, initBrId);
                        cashAuthMap.put(CommonConstants.BRANCH_ID, initBrId);// map.get(CommonConstants.BRANCH_ID));
                        if (initBrId != null && !initBrId.equals("")) {
                            for (int i = 0; i < transList.size(); i++) {
                                linkBatchId = CommonUtil.convertObjToStr(transList.get(i));
                                serviceAuthMap.put("TRANS_ID", linkBatchId);
                                TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
                            }
                        }
                        cashAuthMap = null;
                    }
                    // End
                    
                    if (map != null && map.containsKey("IS_SPLIT_MDS_TRANSACTION") && map.get("IS_SPLIT_MDS_TRANSACTION") != null) {
                        isSplitMDSTransaction = CommonUtil.convertObjToStr(map.get("IS_SPLIT_MDS_TRANSACTION"));
                    }
                    if (mdsReceiptEntryTO.getBankPay().equals("N")) {
                        HashMap transactionaApplnMap = new HashMap();
                        transactionaApplnMap.put("TOTAL_BALANCE", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getInstAmt()));
                        transactionaApplnMap.put("CLEAR_BALANCE", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getInstAmt()));
                        transactionaApplnMap.put("AVAILABLE_BALANCE", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getInstAmt()));
                        transactionaApplnMap.put("SHADOW_CREDIT", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getInstAmt()));
                        transactionaApplnMap.put("SHADOW_DEBIT", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getInstAmt()));
                        transactionaApplnMap.put("SCHEME_NAME", mdsReceiptEntryTO.getSchemeName());
                        transactionaApplnMap.put("BRANCH_CODE", authMap.get("BRANCH_CODE"));
                        transactionaApplnMap.put("TRANS_ID", transactionMap.get("NET_TRANS_ID"));
                        sqlMap.executeUpdate("updateMDSShadowCreditCrAmtMap", transactionaApplnMap);
                        sqlMap.executeUpdate("updateMDSShadowCreditDrAmtMap", transactionaApplnMap);
                        sqlMap.executeUpdate("updateMDSAvailBalanceMap", transactionaApplnMap);
                        sqlMap.executeUpdate("updateMDSClearBalanceMap", transactionaApplnMap);
                        sqlMap.executeUpdate("updateMDSTotalBalanceMap", transactionaApplnMap);
                        if (isSplitMDSTransaction != null && isSplitMDSTransaction.equals("Y")) {
                            List lsts = null;
                            if (map != null && map.containsKey("WHOLE_SPLIT_MAP") && map.get("WHOLE_SPLIT_MAP") != null) {
                                HashMap maps = (HashMap) map.get("WHOLE_SPLIT_MAP");
                                if (maps.containsKey("MDSReceiptEntryTO") && maps.get("MDSReceiptEntryTO") != null) {
                                    lsts = (List) maps.get("MDSReceiptEntryTO");
                                }
                            }
                            if (lsts != null && lsts.size() > 0) {
                                for (int i = 0; i < lsts.size(); i++) {
                                    MDSReceiptEntryTO MDSTo = (MDSReceiptEntryTO) lsts.get(i);
                                    HashMap mdsTransMap = new HashMap();
                                    mdsTransMap.put("SCHEME_NAME", mdsReceiptEntryTO.getSchemeName());
                                    mdsTransMap.put("CHITTAL_NO", mdsReceiptEntryTO.getChittalNo());
                                    mdsTransMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getSubNo()));
                                    mdsTransMap.put("NO_OF_INST", CommonUtil.convertObjToInt(1));
                                    mdsTransMap.put("INST_AMT", MDSTo.getInstAmt());
                                    mdsTransMap.put("PENAL_AMT", MDSTo.getPenalAmtPayable());
                                    mdsTransMap.put("BONUS_AMT", MDSTo.getBonusAmtPayable());
                                    mdsTransMap.put("FORFEITED_AMT",MDSTo.getForfeitBonusAmtPayable()); //Added by nithya for KD-2093
                                    mdsTransMap.put("BANK_ADV_AMTCR",MDSTo.getBankAdvanceAmt());
                                    mdsTransMap.put("DISCOUNT_AMT", MDSTo.getDiscountAmt());
                                    mdsTransMap.put("MDS_INTEREST", mdsReceiptEntryTO.getMdsInterset());
                                    double noticeAmount = CommonUtil.convertObjToDouble(MDSTo.getNoticeAmt());//9535
                                    double arbitrationAmount = CommonUtil.convertObjToDouble(MDSTo.getArbitrationAmt());//9535
                                    double setvideTaxAmount = CommonUtil.convertObjToDouble(MDSTo.getServiceTaxAmt());//9535
                                    mdsTransMap.put("NOTICE_AMT", CommonUtil.convertObjToDouble(MDSTo.getNoticeAmt()));
                                    mdsTransMap.put("ARBITRATION_AMT", CommonUtil.convertObjToDouble(MDSTo.getArbitrationAmt()));
                                    mdsTransMap.put("SERVICE_TAX_AMT", CommonUtil.convertObjToDouble(MDSTo.getServiceTaxAmt()));
                                    mdsTransMap.put("NARRATION", CommonUtil.convertObjToStr(MDSTo.getNarration()));
                                    mdsReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(MDSTo.getNetAmt()) - noticeAmount - arbitrationAmount - setvideTaxAmount);
                                    mdsTransMap.put("NET_AMT", CommonUtil.convertObjToDouble(MDSTo.getNetAmt()));
                                    //mdsTransMap.put("SUBSCRIPTION_AMT", CommonUtil.convertObjToDouble(MDSTo.getInstAmtPayable()));//01-11-2019

                                    mdsTransMap.put("STATUS", CommonConstants.STATUS_CREATED);
                                    mdsTransMap.put("STATUS_DT", setProperDtFormat(currDt));
                                    mdsTransMap.put("STATUS_BY", map.get("USER_ID"));
                                    mdsTransMap.put("AUTHORIZE_STATUS", authorizeStatus);
                                    mdsTransMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                                    mdsTransMap.put("AUTHORIZE_DT", setProperDtFormat(currDt));
                                    mdsTransMap.put("TRANS_DT", setProperDtFormat(currDt));
                                    mdsTransMap.put("NET_TRANS_ID", transactionMap.get("NET_TRANS_ID"));
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
                            }
                        } else {
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
                            //  Added by nithya on 21-11-2019 for KD-783 
                            double arbitrationAmount = CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getArbitrationAmt()); 
                            double setviceTaxAmount = CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getServiceTaxAmt());                           
                            System.out.println("setNetAmtsetNetAmt" + mdsReceiptEntryTO.getNetAmt());
                            //mdsReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNetAmt()) - noticeAmount); //  Commented by nithya on 21-11-2019 for KD-783
                            mdsReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNetAmt()) - noticeAmount - arbitrationAmount - setviceTaxAmount); //  Added by nithya on 21-11-2019 for KD-783
                            System.out.println("noticeAmountnoticeAmount" + noticeAmount + "  " + mdsReceiptEntryTO.getNetAmt());
                            mdsTransMap.put("NET_AMT", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNetAmt()));
                            //mdsTransMap.put("SUBSCRIPTION_AMT", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getSubscriptionAmt()));//01-11-2019

                            mdsTransMap.put("STATUS", CommonConstants.STATUS_CREATED);
                            mdsTransMap.put("STATUS_DT", setProperDtFormat(currDt));
                            mdsTransMap.put("STATUS_BY", map.get("USER_ID"));
                            mdsTransMap.put("AUTHORIZE_STATUS", authorizeStatus);
                            mdsTransMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                            mdsTransMap.put("AUTHORIZE_DT", setProperDtFormat(currDt));
                            mdsTransMap.put("TRANS_DT", setProperDtFormat(currDt));
                            mdsTransMap.put("NET_TRANS_ID", transactionMap.get("NET_TRANS_ID"));
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
                            if (transactionMap != null && transactionMap.get("NET_TRANS_ID") != null && !transactionMap.get("NET_TRANS_ID").equals("")) {
                            if (!authorizeStatus.equals("REJECTED")) {
                                sqlMap.executeUpdate("updateMDSTransDetailsEachRec", mdsTransMap);
                                }
                            }
                        }
                    }
                    if (authorizeStatus != null && authorizeStatus.equals(CommonConstants.STATUS_REJECTED)) {
                        //updateChitCloseDate(mdsReceiptEntryTO, CommonConstants.STATUS_REJECTED);
                    }
                    serviceAuthMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                    serviceAuthMap.put("AUTHORIZE_DT", setProperDtFormat(currDt));
                    serviceAuthMap.put("TRANS_DT", setProperDtFormat(currDt));
                    serviceAuthMap.put("INITIATED_BRANCH", _branchCode);
                    sqlMap.executeUpdate("updateAuthorizeTransaction", serviceAuthMap);
                    serviceAuthMap = null;
                    HashMap authorizeReceiptMap = new HashMap();
                    authorizeReceiptMap.put("TRANS_DT", setProperDtFormat(currDt));
                    authorizeReceiptMap.put("AUTHORIZE_STATUS", authorizeStatus);
                    authorizeReceiptMap.put("AUTHORIZE_BY", authMap.get("USER_ID"));
//                authorizeReceiptMap.put("NET_TRANS_ID",transactionTO.getBatchId());
                    authorizeReceiptMap.put("NET_TRANS_ID", authMap.get("NET_TRANS_ID"));
                    authorizeReceiptMap.put(CommonConstants.BRANCH_ID, _branchCode);
                    sqlMap.executeUpdate("updateAuthorizeReceiptEntry", authorizeReceiptMap);
                    if (map.containsKey("SERVICE_TAX_AUTH")) {
                        HashMap serMapAuth = new HashMap();
                        serMapAuth.put("STATUS", authorizeStatus);
                        serMapAuth.put("USER_ID", authMap.get("USER_ID"));
                        serMapAuth.put("AUTHORIZEDT", currDt);
                        serMapAuth.put("ACCT_NUM", authMap.get("NET_TRANS_ID"));
                        sqlMap.executeUpdate("authorizeServiceTaxDetails", serMapAuth);
                    }
                    HashMap whereMap = new HashMap();
                    double instNo = CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getCurrInst()).doubleValue()
                            - CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getPendingInst()).doubleValue()
                            + CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNoOfInstPay()).doubleValue();
                    whereMap.put("INSTALLMENT_NO", instNo);
                    whereMap.put("CHITTAL_NO", mdsReceiptEntryTO.getChittalNo());
                    whereMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getSubNo()));
                    whereMap.put("AUTH_STATUS", authorizeStatus);
                    if (authorizeStatus.equals("REJECTED")) {
                        whereMap.put("REPAID", "N");
                    } else {
                        whereMap.put("REPAID", "Y");
                    }
                    whereMap.put("REPAID_DT",currDt.clone());
                    double number = CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNoOfInstPay()).doubleValue();
                    List getBankAd = sqlMap.executeQueryForList("getPendingBankAdv", whereMap);
                    if (getBankAd != null && getBankAd.size() > 0) {
                        int countLength = getBankAd.size();
                        if (number >= countLength) {
                            sqlMap.executeUpdate("updateMDSBankAdvanceRepaidStatus", whereMap);
                        } else {
                            int k = 0;
                            for (int i = 0; i < number; i++) {
                                HashMap aMap = new HashMap();
                                aMap = (HashMap) getBankAd.get(i);
                                whereMap.put("BANK_ADV_ID", aMap.get("BANK_ADV_ID").toString());
                                sqlMap.executeUpdate("updateMDSBankAdvanceRepaidStatusSelected", whereMap);
                            }
                        }
                    }
                    //Changed By Suresh  ** Update CLOSED Status in Product Level
                    if (authorizeStatus.equals("AUTHORIZED")) {
                        if (transactionMap != null && transactionMap.get("ARBITRATION_ID") != null && !transactionMap.get("ARBITRATION_ID").equals("")
                                && CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getArbitrationAmt()).doubleValue() > 0) {
                            //Update Paid Charge Amount
                            HashMap chargeMap = new HashMap();
                            chargeMap.put("CASE_CHARGE_DETAILS", "CASE_CHARGE_DETAILS");
                            String actNo = CommonUtil.convertObjToStr(mdsReceiptEntryTO.getChittalNo()) + "_" + CommonUtil.convertObjToStr(mdsReceiptEntryTO.getSubNo());
                            chargesCollected(actNo, chargeMap);
                        }
                        if (transactionMap != null && transactionMap.get("NOTICE_ID") != null && !transactionMap.get("NOTICE_ID").equals("")
                                && CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNoticeAmt()).doubleValue() > 0) {
                            //Update Paid Charge Amount
                            HashMap chargeMap = new HashMap();
                            chargeMap.put("NOTICE_CHARGE_DETAILS", "NOTICE_CHARGE_DETAILS");
                            String actNo = CommonUtil.convertObjToStr(mdsReceiptEntryTO.getChittalNo()) + "_" + CommonUtil.convertObjToStr(mdsReceiptEntryTO.getSubNo());
                            chargesCollected(actNo, chargeMap);
                        }
                        
                         //Authorization in case of waive transaction
                    
                    if (transactionMap != null && transactionMap.containsKey("ARC_WAIVE_TRANS_ID") && transactionMap.get("ARC_WAIVE_TRANS_ID") != null && !transactionMap.get("ARC_WAIVE_TRANS_ID").equals("")
                            && CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getArbitrationAmt()).doubleValue() > 0) {
                        //Update Paid Charge Amount
                        HashMap chargeMap = new HashMap();
                        chargeMap.put("CASE_CHARGE_DETAILS", "CASE_CHARGE_DETAILS");
                        String actNo = CommonUtil.convertObjToStr(mdsReceiptEntryTO.getChittalNo()) + "_" + CommonUtil.convertObjToStr(mdsReceiptEntryTO.getSubNo());
                        chargesCollected(actNo, chargeMap);
                    }
                    if (transactionMap != null && transactionMap.containsKey("NOTICE_WAIVE_TRANS_ID")&& transactionMap.get("NOTICE_WAIVE_TRANS_ID") != null && !transactionMap.get("NOTICE_WAIVE_TRANS_ID").equals("")
                            && CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNoticeAmt()).doubleValue() > 0) {
                        //Update Paid Charge Amount
                        HashMap chargeMap = new HashMap();
                        chargeMap.put("NOTICE_CHARGE_DETAILS", "NOTICE_CHARGE_DETAILS");
                        String actNo = CommonUtil.convertObjToStr(mdsReceiptEntryTO.getChittalNo()) + "_" + CommonUtil.convertObjToStr(mdsReceiptEntryTO.getSubNo());
                        chargesCollected(actNo, chargeMap);
                    }
                    
                    // End
                        
                        List closureList = (List) sqlMap.executeQueryForList("checkSchemeClosureDetails", map);
                        if (closureList != null && closureList.size() > 0) {
                            double totalSchemeAmount = 0.0;
                            double totalAmtReceived = 0.0;
                            double totalAmtPaid = 0.0;
                            whereMap = new HashMap();
                            List totalSchemeAmtLst = (List) sqlMap.executeQueryForList("getTotalAmountPerScheme", map);
                            if (totalSchemeAmtLst != null && totalSchemeAmtLst.size() > 0) {
                                whereMap = (HashMap) totalSchemeAmtLst.get(0);
                                totalSchemeAmount = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(whereMap.get("TOTAL_SCHEME_AMOUNT"))).doubleValue();
                            }
                            List totalReceived = (List) sqlMap.executeQueryForList("getTotalReceivedAmount", map);
                            if (totalReceived != null && totalReceived.size() > 0) {
                                whereMap = (HashMap) totalReceived.get(0);
                                totalAmtReceived = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(whereMap.get("TOTAL_RECEIVED"))).doubleValue();
                            }
                            List totalPaid = (List) sqlMap.executeQueryForList("getTotalPaidAmount", map);
                            if (totalPaid != null && totalPaid.size() > 0) {
                                whereMap = (HashMap) totalPaid.get(0);
                                totalAmtPaid = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(whereMap.get("TOTAL_PAID"))).doubleValue();
                            }
                            if (totalSchemeAmount == totalAmtReceived && totalSchemeAmount == totalAmtPaid) {
                                map.put("STATUS", "CLOSED");
                                sqlMap.executeUpdate("updateMDSProductCloseStatus", map);

                                List lst1 = sqlMap.executeQueryForList("getMDSLienDetailsForClosing", map);
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
                        if (map.containsKey("CURRENT_INST_NO") && map.containsKey("PAID_INST_NO") && map.get("CURRENT_INST_NO") != null && map.get("PAID_INST_NO") != null) {
                            int currntInstNo = CommonUtil.convertObjToInt(map.get("CURRENT_INST_NO"));
                            int paidInstNo = CommonUtil.convertObjToInt(map.get("PAID_INST_NO"));
                            if (paidInstNo == currntInstNo) {
                                System.out.println("Do LienUnmarking");
                                doUnmarkLean();
                            }

                        }
                    }
                    authorizeMap = null;
                    authMap = null;
                    map = null;
                }
                //Added by sreekrishnan for sms alert..
                if (authorizeStatus.equals(CommonConstants.STATUS_AUTHORIZED)) {
                    HashMap smsAlertMap = new HashMap();
                    SmsConfigDAO smsDAO = new SmsConfigDAO();
                    smsAlertMap.put("SCHEME_NAME", mdsReceiptEntryTO.getSchemeName());
                    smsAlertMap.put("CHITTAL_NO", mdsReceiptEntryTO.getChittalNo());
                    List MdsSmsList = sqlMap.executeQueryForList("getMDsDetailsForSMS", smsAlertMap);
                    if (MdsSmsList != null && MdsSmsList.size() > 0) {
                        HashMap MdsSmsMap = (HashMap) MdsSmsList.get(0);
                        if (MdsSmsMap != null && !MdsSmsMap.equals("")) {
                            MdsSmsMap.put(CommonConstants.TRANS_ID, transId);
                            MdsSmsMap.put("TRANS_DT", currDt);
                            List MdsTransList = sqlMap.executeQueryForList("getMdsTransDetailsForSms", MdsSmsMap);
                            if (MdsTransList != null && MdsTransList.size() > 0) {
                                HashMap MdsTransMap = (HashMap) MdsTransList.get(0);
                                MdsTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
                                smsDAO.MdsReceiptsSmsConfiguration(MdsTransMap);
                                MdsTransList = null;
                            }
                        }
                        MdsSmsList = null;
                    }
                    //Added for Mantis 10732
                    HashMap lastInstMap = new HashMap();
                    List LastInstList = sqlMap.executeQueryForList("getChittalLastInstDetails", smsAlertMap);
                    if (LastInstList != null && LastInstList.size() > 0) {
                        lastInstMap.put("LAST_INST_DT", currDt.clone());
                        lastInstMap.putAll(smsAlertMap);
                        sqlMap.executeUpdate("updateMasterMaintenceCloseDt", lastInstMap);
                    }

                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
}
//Update Paid_Charge Amount in LOANS_ACCT_CHARGE_DETAILS
private void chargesCollected(String act_num, HashMap inputMap) throws Exception {
        HashMap chargeMap = new HashMap();
        List chargeLst = null;
        chargeMap.put(CommonConstants.ACT_NUM, act_num);
        if (inputMap.containsKey("CASE_CHARGE_DETAILS")) {
            chargeLst = sqlMap.executeQueryForList("getMDSCaseChargeDetails", chargeMap);
        } else if (inputMap.containsKey("NOTICE_CHARGE_DETAILS")) {
            chargeLst = sqlMap.executeQueryForList("getMDSNoticeChargeDetails", chargeMap);
        }
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

    private void doUnmarkLean() throws Exception {
        String chittalNo = CommonUtil.convertObjToStr(mdsReceiptEntryTO.getChittalNo());
        String subNo = CommonUtil.convertObjToStr(mdsReceiptEntryTO.getSubNo());
        HashMap unMarkleanMap = new HashMap();
        HashMap leanFlagMap = new HashMap();
        boolean leanFlag = false;
        unMarkleanMap.put("CHITTAL_NO", chittalNo);
        unMarkleanMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
        List depositTypeList = sqlMap.executeQueryForList("getSelectMDSDepositTypeTO", unMarkleanMap);
        if (depositTypeList != null && depositTypeList.size() > 0) {
            for (int i = 0; i < depositTypeList.size(); i++) {
                String depositNo = CommonUtil.convertObjToStr(((MDSDepositTypeTO) depositTypeList.get(i)).getDepositNo());
                unMarkleanMap.put("DEPOSIT_NO", depositNo);
                List noOfDepositList = sqlMap.executeQueryForList("getNoOfLienDeposit", unMarkleanMap);
                if (noOfDepositList != null && noOfDepositList.size() > 0) {
                    leanFlagMap = (HashMap) noOfDepositList.get(0);
                    if (leanFlagMap.get("STATUS") != null && leanFlagMap.containsKey("STATUS")) {
                        if (leanFlagMap.get("STATUS").equals("LIEN")) {
                            leanFlag = true;
                        }
                    }
                }
            }
            if (leanFlag) {
                HashMap availClearMap = new HashMap();
                double clearBal = 0;
                double availBal = 0;
                List clrAvailBalList = sqlMap.executeQueryForList("getClearAvailbalance", unMarkleanMap);
                if (clrAvailBalList != null && clrAvailBalList.size() > 0) {
                    availClearMap = (HashMap) clrAvailBalList.get(0);
                    if(availClearMap.containsKey("CLEAR_BALANCE") && availClearMap.get("CLEAR_BALANCE") != null && availClearMap.containsKey("AVAILABLE_BALANCE") && availClearMap.get("AVAILABLE_BALANCE") != null){
                    clearBal = CommonUtil.convertObjToDouble(availClearMap.get("CLEAR_BALANCE"));
                    availBal = CommonUtil.convertObjToDouble(availClearMap.get("AVAILABLE_BALANCE"));
                    }
                }
                unMarkleanMap.put("LIEN_ACCT_NO", chittalNo);
                List lienAmountList = sqlMap.executeQueryForList("selectLienAmountInDepositLien", unMarkleanMap);
                if (lienAmountList != null && lienAmountList.size() > 0) {
                    double lienAmount = 0;
                    HashMap lienAmountMap = (HashMap) lienAmountList.get(0);
                    if(lienAmountMap.containsKey("LIENAMOUNT") && lienAmountMap.get("LIENAMOUNT") != null){
                    lienAmount = CommonUtil.convertObjToDouble(lienAmountMap.get("LIENAMOUNT"));
                    }
                    lienAmount += availBal;
                    availBal = lienAmount;
                    unMarkleanMap.put("LIENAMOUNT", lienAmount);
                    sqlMap.executeUpdate("UpdateLienAmountInDeposit", unMarkleanMap);
                    unMarkleanMap.put("REMARK_STATUS", "Closed MDS");//
                    unMarkleanMap.put("STATUS", "UNLIENED");//
                    sqlMap.executeUpdate("UpdateRemarksInDepositLien", unMarkleanMap);
                    if (availBal >= clearBal) {
                        unMarkleanMap.put("LIEN_STATUS", "CREATED");
                        sqlMap.executeUpdate("UpadteLienForMDSDepositType", unMarkleanMap);
                    }
                }
            }
            leanFlag = false;
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        return execute(map, true);
    }

    public HashMap execute(HashMap map, boolean isTransaction) throws Exception {
        //    public HashMap execute(HashMap map)  throws Exception {
        _branchCode = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
        currDt = ServerUtil.getCurrentDate(_branchCode);
        clearAllFileds();
        if(map.containsKey("MEMBER_RECEIPT_SINGLE_ID") && map.get("MEMBER_RECEIPT_SINGLE_ID")!=null){
           generateSingleTransId =CommonUtil.convertObjToStr(map.get("MEMBER_RECEIPT_SINGLE_ID")); 
        }
        else{
            generateSingleTransId = generateLinkID();
        }
        if(map.containsKey("IS_SPLIT_MDS_TRANSACTION") && map.get("IS_SPLIT_MDS_TRANSACTION")!=null){
            isSplitMDSTransaction = CommonUtil.convertObjToStr(map.get("IS_SPLIT_MDS_TRANSACTION"));
        }
        /*
         * To Verify The Account Head data...
         */
        
        System.out.println("Map in Receipt Entry DAO : " + map);
        if (map.containsKey("ACCT_HD")) {
            ServerUtil.verifyAccountHead(map);
        }

        LogDAO objLogDAO = new LogDAO();
        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));
        ibrHierarchy = 1;
        if (map.containsKey("mdsReceiptEntryTO") || map.containsKey("MDSReceiptEntryTO")) {
            mdsReceiptEntryTO = new MDSReceiptEntryTO();
            if(map.containsKey("mdsReceiptEntryTO")){
                mdsReceiptEntryTO = (MDSReceiptEntryTO) map.get("mdsReceiptEntryTO");
            }else if (map.containsKey("MDS_TRANS_ALL_AUTHORIZATION") && map.containsKey("MDSReceiptEntryTO")){
                mdsReceiptEntryTO = (MDSReceiptEntryTO) ((List) map.get("MDSReceiptEntryTO")).get(0);
                TransactionTO objTransactionTO = (TransactionTO) ((List) map.get("TransactionTO")).get(0);
                map.remove("TransactionTO");
                map.put("TransactionTO", objTransactionTO);
            }
            final String command = mdsReceiptEntryTO.getCommand();
            try {
                if (!map.containsKey("MDS_TRANS_ALL_AUTHORIZATION") && isTransaction) {
                    sqlMap.startTransaction();
                }
                objLogTO.setStatus(command);
                returnMap = new HashMap();
                if (command != null && command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertData(map, objLogDAO, objLogTO);
                } else if (command != null && command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData(map, objLogDAO, objLogTO);
                } else if (command != null && command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData(map, objLogDAO, objLogTO);
                } else if (map.containsKey("AUTHORIZEMAP")) {
                    authorize(map, objLogDAO, objLogTO);
                } else {
                    throw new NoCommandException();
                }
                if (!map.containsKey("MDS_TRANS_ALL_AUTHORIZATION") && isTransaction) {
                    sqlMap.commitTransaction();
                }
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw e;
            }
            objLogDAO = null;
            objLogTO = null;
            destroyObjects();

        }
        System.out.println("@#$@@$@@@$ returnMap : " + returnMap);
        return (HashMap) returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        mdsReceiptEntryTO = null;
        TxTransferTO = null;
    }
		//added by chithra for service Tax
 private void insertServiceTaxDetails(ServiceTaxDetailsTO objserviceTaxDetailsTO) {
        try {
            objserviceTaxDetailsTO.setServiceTaxDet_Id(getServiceTaxNo());
            objserviceTaxDetailsTO.setParticulars("MDS Receipt Entry");
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
            MDSReceiptEntryDAO dao = new MDSReceiptEntryDAO();
            HashMap inputMap = new HashMap();
            //inputMap.put("ACCT_HD", "CCS_D");

            System.out.println(sqlMap.executeQueryForList("OperativeAcctProduct.getSelectAcctHeadTOList", null));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateChitCloseDate(MDSReceiptEntryTO mDSReceiptEntryTO, String status) throws Exception {
        int chkNoOfInst = 0, noOfInstToPay = 0;
        HashMap chitCloseMap = new HashMap();
        HashMap installMap = new HashMap();
        HashMap noOfInstMap = new HashMap();
        chitCloseMap.put("CHIT_CLOSE_DT", (Date) currDt.clone());
        chitCloseMap.put("SCHEME_NAME", CommonUtil.convertObjToStr(mDSReceiptEntryTO.getSchemeName()));
        chitCloseMap.put("CHITTAL_NO", CommonUtil.convertObjToStr(mDSReceiptEntryTO.getChittalNo()));
        chitCloseMap.put("SUB_NO", CommonUtil.convertObjToInt(mDSReceiptEntryTO.getSubNo()));
        chitCloseMap.put("DIV_NO", CommonUtil.convertObjToStr(mDSReceiptEntryTO.getDivisionNo()));
        List instLst = sqlMap.executeQueryForList("getNoOfInstalmentsPaid", chitCloseMap);
        if (instLst != null && instLst.size() > 0) {
            installMap = (HashMap) instLst.get(0);
        }
        int noOfInstPaid = CommonUtil.convertObjToInt(installMap.get("NO_OF_INST"));
        List noOfInstList = sqlMap.executeQueryForList("getMDSNoOfInstpay", chitCloseMap);
        if (noOfInstList != null && noOfInstList.size() > 0) {
            noOfInstMap = (HashMap) noOfInstList.get(0);
        }
        int noOfInst = CommonUtil.convertObjToInt(mDSReceiptEntryTO.getNoOfInst());
        if (status != null && !status.equals("") && status.equals(CommonConstants.STATUS_REJECTED)) {
            if (noOfInstMap != null && noOfInstMap.containsKey("NO_OF_INST_PAY") && noOfInstMap.get("NO_OF_INST_PAY") != null) {
                noOfInstToPay = CommonUtil.convertObjToInt(noOfInstMap.get("NO_OF_INST_PAY"));
                chkNoOfInst = noOfInstPaid + noOfInstToPay;
                chitCloseMap.put("CHIT_CLOSE_DT", null);
            }
        } else {
            noOfInstToPay = CommonUtil.convertObjToInt(mDSReceiptEntryTO.getNoOfInstPay());
            chkNoOfInst = noOfInstPaid + noOfInstToPay;
        }
        if (chkNoOfInst == noOfInst) {
            sqlMap.executeUpdate("updateChittalCloseDate", chitCloseMap);
        }
        chitCloseMap = null;
        status = null;
        installMap = null;
        noOfInstMap = null;
    }
    
    
     public void commonTransactionWaiveOff(HashMap map, MDSReceiptEntryTO mdsCurrentReceiptEntryTO, HashMap debitMap, HashMap applicationMap,double waiveAmount, String waiveParam, String arcHead) throws Exception {
     
        String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        System.out.println("mdsCurrentReceiptEntryTO####" + mdsCurrentReceiptEntryTO + map);
        TransferTrans transferTrans = new TransferTrans();
        TxTransferTO transferTo = new TxTransferTO();
        ArrayList TxTransferTO = new ArrayList();
        TransactionTO transactionTO = new TransactionTO();
        HashMap txMap = new HashMap();
        HashMap transactionListMap = new HashMap();
        transferTrans.setInitiatedBranch(BRANCH_ID);
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        transactionDAO.setInitiatedBranch(mdsCurrentReceiptEntryTO.getBranchCode());
        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
        transactionDAO.setTransactionType(transactionDAO.TRANSFER);     
            System.out.println("Waive Started :: " + waiveParam);
            transferTo = new TxTransferTO();
            txMap = new HashMap();
            transferTo.setInstrumentNo2("APPL_GL_TRANS");  
            if(waiveParam.equals("PENAL")){
                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("PENAL_WAIVE_HEAD"));  
            }else if(waiveParam.equals("ARC")){
                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("ARC_WAIVE_HEAD"));  
            }else if(waiveParam.equals("NOTICE")){
                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("NOTICE_WAIVE_HEAD"));  
            }     
            //PENAL_WAIVE_HEAD   ARC_WAIVE_HEAD   NOTICE_WAIVE_HEAD 
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.DR_BRANCH, mdsCurrentReceiptEntryTO.getBranchCode());
            txMap.put(TransferTrans.PARTICULARS, waiveParam + " Waive off :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsCurrentReceiptEntryTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());           
            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
            txMap.put("TRANS_MOD_TYPE", "MDS");     
            transferTo = transactionDAO.addTransferDebitLocal(txMap, waiveAmount);
             if(waiveParam.equals("PENAL")){
                transferTo.setInstrumentNo2("PENAL_WAIVE_OFF");
            }else if(waiveParam.equals("ARC")){
                transferTo.setInstrumentNo2("ARC_WAIVE_OFF");
            }else if(waiveParam.equals("NOTICE")){
                transferTo.setInstrumentNo2("NOTICE_WAIVE_OFF");
            } 
            transferTo.setTransId("-");
            transferTo.setBatchId("-");
            transferTo.setTransType(CommonConstants.DEBIT);
            transferTo.setProdType(TransactionFactory.GL);
            transferTo.setTransDt(currDt);
            transferTo.setBranchId(mdsCurrentReceiptEntryTO.getBranchCode());
            transferTo.setInitiatedBranch(mdsCurrentReceiptEntryTO.getBranchCode());
            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
            transferTo.setRec_mode(CommonUtil.convertObjToStr(map.get("REC_MODE")));
            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
            transactionTO.setChequeNo("SERVICE_TAX");
            transferTo.setGlTransActNum(mdsCurrentReceiptEntryTO.getChittalNo());
            transferTo.setSingleTransId(generateSingleTransId);
            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
            TxTransferTO.add(transferTo);
            
            // Credit side
            // PENAL_INTEREST_HEAD NOTICE_CHARGES_HEAD  
            transferTo = new TxTransferTO();
            txMap = new HashMap();
            transferTo.setInstrumentNo2("APPL_GL_TRANS");   
             if(waiveParam.equals("PENAL")){
                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("PENAL_INTEREST_HEAD"));  
            }else if(waiveParam.equals("ARC")){
                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get(arcHead));  
            }else if(waiveParam.equals("NOTICE")){
                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("NOTICE_CHARGES_HEAD"));  
            }            
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.CR_BRANCH, mdsCurrentReceiptEntryTO.getBranchCode());
            txMap.put(TransferTrans.PARTICULARS, waiveParam + " Waive Off :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsCurrentReceiptEntryTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());           
            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
            //txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
            txMap.put("TRANS_MOD_TYPE", "MDS");     
            transferTo = transactionDAO.addTransferCreditLocal(txMap, waiveAmount);
            //transferTo.setInstrumentNo2("BONUS");
            if(waiveParam.equals("PENAL")){
                transferTo.setInstrumentNo2("PENAL_WAIVE_OFF");
            }else if(waiveParam.equals("ARC")){
                transferTo.setInstrumentNo2("ARC_WAIVE_OFF");
            }else if(waiveParam.equals("NOTICE")){
                transferTo.setInstrumentNo2("NOTICE_WAIVE_OFF");
            } 
            transferTo.setTransId("-");
            transferTo.setBatchId("-");
            transferTo.setTransType(CommonConstants.CREDIT);
            transferTo.setProdType(TransactionFactory.GL);
            transferTo.setTransDt(currDt);
            transferTo.setBranchId(mdsCurrentReceiptEntryTO.getBranchCode());
            transferTo.setInitiatedBranch(mdsCurrentReceiptEntryTO.getBranchCode());
            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
            transferTo.setRec_mode(CommonUtil.convertObjToStr(map.get("REC_MODE")));
            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
            transactionTO.setChequeNo("SERVICE_TAX");
            transferTo.setGlTransActNum(mdsCurrentReceiptEntryTO.getChittalNo());
            transferTo.setSingleTransId(generateSingleTransId);
            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
            TxTransferTO.add(transferTo);
            // End
            System.out.println("Waive off TxTransferTO :: " + TxTransferTO);
            HashMap applnMap = new HashMap();
            map.put("MODE", map.get("COMMAND"));
            map.put("COMMAND", map.get("MODE"));
            map.put("TxTransferTO", TxTransferTO);
            map.put(CommonConstants.SELECTED_BRANCH_ID,mdsCurrentReceiptEntryTO.getBranchCode());
            HashMap transMap = transferDAO.execute(map, false);
            System.out.println("transferDAO List transMap : " + transMap);
            if(waiveParam.equals("PENAL")){
                mdsCurrentReceiptEntryTO.setPenalWaiveId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                mdsCurrentReceiptEntryTO.setPenalWaiveAmt(waiveAmount);
            }else if(waiveParam.equals("ARC")){
                mdsCurrentReceiptEntryTO.setArcWaiveId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID"))); 
                mdsCurrentReceiptEntryTO.setArcWaiveAmt(waiveAmount);
            }else if(waiveParam.equals("NOTICE")){
                mdsCurrentReceiptEntryTO.setNoticeWaiveId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                mdsCurrentReceiptEntryTO.setNoticeWaiveAmt(waiveAmount);
            } 
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
            sqlMap.executeUpdate("updateLinkBatchIdTransferMDS", linkBatchMap);
            linkBatchMap = null;
            transMap = null;
        
    }

       private String getARCHead(MDSReceiptEntryTO mdsCurrentReceiptEntryTO) throws SQLException {
        String arcHead = "";
        HashMap whereMap = new HashMap();
        whereMap.put("ACT_NUM", CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()) + "_" + CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getSubNo()));
        List chargeList = (List) sqlMap.executeQueryForList("getMDSCaseChargeDetails", whereMap);
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
                    if (chargeType.equals("ARC_COST")) {
                        chargeType = "ARC_COST";
                    } else if (chargeType.equals("ARC Expense")) {
                        chargeType = "ARC_EXPENSE";
                    } else if (chargeType.equals("EA Cost")) {
                        chargeType = "EA_COST";
                    } else if (chargeType.equals("EA Expense")) {
                        chargeType = "EA_EXPENSE";
                    } else if (chargeType.equals("EP_COST")) {
                        chargeType = "EP_COST";
                    } else if (chargeType.equals("EP Expense")) {
                        chargeType = "EP_EXPENSE";
                    } else if (chargeType.equals("ARBITRARY CHARGES")) {
                        chargeType = "ARC_COST";
                    } else if (chargeType.equals("EXECUTION DECREE CHARGES")) {
                        chargeType = "EP_COST";
                    } else if (chargeType.equals("POSTAGE CHARGES")) {
                        chargeType = "POSTAGE_HEAD";
                    } else if (chargeType.equals("NOTICE CHARGES")) {
                        chargeType = "NOTICE_CHARGES_HEAD";
                    } else if (chargeType.equals("MISCELLANEOUS CHARGES")) {
                        chargeType = "MISCELLANEOUS_CHARGES";
                    } else if (chargeType.equals("OTHER CHARGES")) {
                        chargeType = "OTHER_CHARGES";
                    } else if (chargeType.equals("LEGAL CHARGES")) {
                        chargeType = "LEGAL_CHARGES";
                    } else if (chargeType.equals("INSURANCE CHARGES")) {
                        chargeType = "INSURANCE_CHARGES";
                    }
                    arcHead = chargeType;
                }
            }
        }
        return arcHead;
    }
    
    
}