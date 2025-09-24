/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GDSReceiptEntryDAO.java
 *
 * Created on February 16, 2018, 4:14 PM
 */
package com.see.truetransact.serverside.gdsapplication.gdsclosedreceipt;

import com.see.truetransact.serverside.mdsapplication.mdsclosedreceipt.*;
import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.commonutil.NoCommandException;
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
import com.see.truetransact.transferobject.mdsapplication.mdsclosedreceipt.MDSClosedReciptTO;
// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.common.sms.SmsConfigDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.gdsapplication.GDSApplicationTO;
import com.see.truetransact.transferobject.gdsapplication.gdsclosedreceipt.GDSClosedReciptTO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;

/**
 * This is used for MDSReceiptEntryDAO Data Access.
 *
 * @author Nithya
 *
 */
public class GDSClosedReciptDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private TransactionDAO transactionDAO = null;
    private GDSClosedReciptTO mdsReceiptEntryTO = null;
    TransferDAO transferDAO = new TransferDAO();
    private Date currDt = null;
    private Map returnMap = null;
    private double bankAdvInsAmt = 0.0;
    private double futureAdvbonusAmt = 0.0;
    private double instalmentPayAmt = 0.0;
    private double bankBonusAmt = 0.0;
    private double bankBonusReverseAmt = 0.0;
    private Iterator addressIterator1;
    private Iterator addressIterator2;
    private boolean penalTransHappened = false;
    private boolean bonusreversal = false;
    private HashMap transIdMap = null;
    private String generateSingleTransId = "";
    private int ibrHierarchy = 0;
    private String isSplitMDSTransaction="";
    /**
     * Creates a new instance of GDSClosedReciptDAO
     */
    public GDSClosedReciptDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("Inside GDSClosedReciptDAO :: getdata ::" + map);
        isSplitMDSTransaction = "";
        if (map != null && map.containsKey("isSplitMDSTransaction") && map.get("isSplitMDSTransaction") != null) {
            isSplitMDSTransaction = CommonUtil.convertObjToStr(map.get("isSplitMDSTransaction"));
        }
        HashMap getReturnMap = new HashMap();
        String where = CommonUtil.convertObjToStr(map.get("NET_TRANS_ID"));
        HashMap editMap = new HashMap();
        editMap.put("TRANS_ID", CommonUtil.convertObjToStr(map.get("NET_TRANS_ID")));
        editMap.put("TRANS_DT", currDt);
        editMap.put("INITIATED_BRANCH", _branchCode);
        List list = (List) sqlMap.executeQueryForList("getGDSReceiptEntryTOClosed", editMap);
        getReturnMap.put("MDSReceiptEntryTO", list);
        mdsReceiptEntryTO = new GDSClosedReciptTO();
        if (map.containsKey("VOUCHER_DETAILS")) {
            returnMap = new HashMap();
            mdsReceiptEntryTO = (GDSClosedReciptTO) ((List) getReturnMap.get("MDSReceiptEntryTO")).get(0);
            String netTransID = CommonUtil.convertObjToStr(map.get("NET_TRANS_ID"));
            String bonusTransID = mdsReceiptEntryTO.getBonusTransId();
            if (netTransID.indexOf(",") != -1) {
                mdsReceiptEntryTO.setNetTransId(netTransID.substring(0, netTransID.indexOf(",")));
                if (bonusTransID.indexOf(",") != -1) {
                    mdsReceiptEntryTO.setBonusTransId(bonusTransID.substring(0, bonusTransID.indexOf(",")));
                }
                setTransDetails(mdsReceiptEntryTO);
                mdsReceiptEntryTO.setNetTransId(netTransID.substring(netTransID.indexOf(",") + 1, netTransID.length()));
                if (bonusTransID.indexOf(",") != -1) {
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
        HashMap totalMap = new HashMap();
        String transId = CommonUtil.convertObjToStr(map.get("NET_TRANS_ID"));
        int dot = transId.indexOf(",");
        if (dot > 0) {
            transId = transId.substring(0, dot);
        }
        System.out.println("###### transID : " + transId);
        whereMap.put(CommonConstants.TRANS_ID, transId);
        whereMap.put(CommonConstants.BRANCH_ID, _branchCode);
        whereMap.put("TRANS_DT", currDt);
        totalMap.put(CommonConstants.MAP_WHERE, whereMap);
        list = transactionDAO.getData(totalMap);
        getReturnMap.put("TransactionTO", list);

        HashMap editRemitMap = new HashMap();
        HashMap getTransMap = new HashMap();
        getTransMap.put("TODAY_DT", currDt);
        getTransMap.put("INITIATED_BRANCH", _branchCode);

        if (transactionMap != null && transactionMap.get("NET_TRANS_ID") != null && !transactionMap.get("NET_TRANS_ID").equals("")) {
            getTransMap.put("LINK_BATCH_ID", transactionMap.get("NET_TRANS_ID"));
            lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
            if (lst != null && lst.size() > 0) {
                getReturnMap.put("NET_TRANSACTION_TRANSFER", lst.get(0));
            }
            lst = (List) sqlMap.executeQueryForList("getCashTransBatchID", getTransMap);
            if (lst != null && lst.size() > 0) {
                getReturnMap.put("NET_TRANSACTION_CASH", lst.get(0));
            }
        }
        if (transactionMap != null && transactionMap.get("PENAL_TRANS_ID") != null && !transactionMap.get("PENAL_TRANS_ID").equals("")) {
            getTransMap.put("LINK_BATCH_ID", transactionMap.get("PENAL_TRANS_ID"));
            lst = (List) sqlMap.executeQueryForList("getCashTransBatchID", getTransMap);
            if (lst != null && lst.size() > 0) {
                getReturnMap.put("PENAL_TRANSACTION_CASH", lst.get(0));
            }
        }
        if (transactionMap != null && transactionMap.get("ARBITRATION_ID") != null && !transactionMap.get("ARBITRATION_ID").equals("")) {
            getTransMap.put("LINK_BATCH_ID", transactionMap.get("ARBITRATION_ID"));
            lst = (List) sqlMap.executeQueryForList("getCashTransBatchID", getTransMap);
            if (lst != null && lst.size() > 0) {
                getReturnMap.put("ARBITRATION_TRANSACTION_CASH", lst.get(0));
            }
        }
        if (transactionMap != null && transactionMap.get("NOTICE_ID") != null && !transactionMap.get("NOTICE_ID").equals("")) {
            getTransMap.put("LINK_BATCH_ID", transactionMap.get("NOTICE_ID"));
            lst = (List) sqlMap.executeQueryForList("getCashTransBatchID", getTransMap);
            if (lst != null && lst.size() > 0) {
                getReturnMap.put("NOTICE_TRANSACTION_CASH", lst.get(0));
            }
        }
        if (transactionMap != null && transactionMap.get("BONUS_TRANS_ID") != null && !transactionMap.get("BONUS_TRANS_ID").equals("")) {
            getTransMap.put("LINK_BATCH_ID", transactionMap.get("BONUS_TRANS_ID"));
            lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
            if (lst != null && lst.size() > 0) {
                getReturnMap.put("BONUS_TRANSACTION_TRANSFER", lst.get(0));
            }
        }
        if (transactionMap != null && transactionMap.get("DISCOUNT_TRANS_ID") != null && !transactionMap.get("DISCOUNT_TRANS_ID").equals("")) {
            getTransMap.put("LINK_BATCH_ID", transactionMap.get("DISCOUNT_TRANS_ID"));
            lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
            if (lst != null && lst.size() > 0) {
                getReturnMap.put("DISCOUNT_TRANSACTION_TRANSFER", lst.get(0));
            }
        }
        System.out.println("getReturnMap : " + getReturnMap);
        return getReturnMap;
    }

    private void insertData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            System.out.println("Inside GDSClosedReciptDAO :: insertData :: map :: "+ map);
            LinkedHashMap bankAdvMap = new LinkedHashMap();
            int bankAdvCnt = 0;
            int instNo = 0;
            bankAdvInsAmt = 0.0;
            instalmentPayAmt = 0.0;
            futureAdvbonusAmt = 0.0;
            penalTransHappened = false;   
            String isBonusTrans = "Y";
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
            instNo = CommonUtil.convertObjToInt(mdsReceiptEntryTO.getCurrInst()) - CommonUtil.convertObjToInt(mdsReceiptEntryTO.getPendingInst());
            bankAdvMap.put("SCHEME_NAME", commonScheme);
            bankAdvMap.put("GROUP_NO",map.get("GROUP_NO"));
            bankAdvMap.put("CHITTAL_NO", mdsReceiptEntryTO.getChittalNo());
            bankAdvMap.put("GDS_NO",map.get("GDS_NO"));
            bankAdvMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getSubNo()));
            bankAdvMap.put("INSTALLMENT_NO", instNo);
            GDSClosedReciptTO mdsBankAdvReceiptEntryTO = null;
            GDSClosedReciptTO mdsCurrentReceiptEntryTO = null;
            List bankAdvanceLst = sqlMap.executeQueryForList("getSelectGDSBankAdvanceDetailsData", bankAdvMap);
            if (bankAdvanceLst != null && bankAdvanceLst.size() > 0) {
                LinkedHashMap bankMap = new LinkedHashMap();
                bankAdvMap = new LinkedHashMap();
                HashMap installmentMap = new HashMap();
                if (map.containsKey("INSTALLMENT_MAP")) {             
                    installmentMap = (HashMap) map.get("INSTALLMENT_MAP");
                }    
                if (bankAdvanceLst != null && bankAdvanceLst.size() > 0) {
                    for (int i = 0; i < bankAdvanceLst.size(); i++) {
                        bankMap = (LinkedHashMap) bankAdvanceLst.get(i);
                        System.out.println("BANK MAP" + bankMap);
                        bankAdvMap.put(String.valueOf(bankMap.get("INSTALLMENT_NO")), bankMap);
                    }
                }
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
                        bankAdvInsAmt += CommonUtil.convertObjToDouble(((HashMap) bankAdvMap.get(key1)).get("INST_AMT")).doubleValue();
                        bankBonusAmt += CommonUtil.convertObjToDouble(((HashMap) bankAdvMap.get(key1)).get("BONUS_AMT")).doubleValue();
                        if (CommonUtil.convertObjToDouble(installMap.get("BONUS")).doubleValue() == 0) {
                            bankBonusReverseAmt += CommonUtil.convertObjToDouble(((HashMap) bankAdvMap.get(key1)).get("BONUS_AMT")).doubleValue();                            
                        }   
                        if (mdsReceiptEntryTO.getBonusAmtPayable() == 0.0) {
                            bonusreversal = true;                            
                        } else {
                            bonusreversal = false;                            
                        }                        
                    } else {
                        HashMap installMap = new HashMap();
                        installMap = (HashMap) installmentMap.get(key1);                       
                        instalmentPayAmt += CommonUtil.convertObjToDouble(installMap.get("INST_AMT")).doubleValue();
                        futureAdvbonusAmt += CommonUtil.convertObjToDouble(installMap.get("BONUS")).doubleValue();
                    }
                }
                bankBonusAmt = bankBonusAmt * bankAdvanceLst.size();
                bankAdvInsAmt = bankAdvInsAmt * bankAdvanceLst.size();
                System.out.println("bankBonusAmt :: "+ bankBonusAmt);
                System.out.println("bankAdvInsAmt :: "+ bankAdvInsAmt);
                mdsBankAdvReceiptEntryTO = getMDSReceiptEntryTO(bankBonusAmt, bankAdvInsAmt);
                mdsCurrentReceiptEntryTO = (GDSClosedReciptTO) map.get("mdsReceiptEntryTO");
                mdsBankAdvReceiptEntryTO.setBranchCode(mdsCurrentReceiptEntryTO.getBranchCode()); //added by abi
                mdsBankAdvReceiptEntryTO.setInitiatedBranch(_branchCode);
                mdsCurrentReceiptEntryTO = null;
               
                insertBankAdvData(map, mdsBankAdvReceiptEntryTO, objLogDAO, objLogTO);
            }

            if (instalmentPayAmt > 0 || (bankAdvanceLst == null || bankAdvanceLst.size() <= 0)) {                
                if (bankAdvInsAmt > 0) {                   
                    mdsCurrentReceiptEntryTO = getMDSReceiptEntryTO(futureAdvbonusAmt, instalmentPayAmt);
                    mdsCurrentReceiptEntryTO.setNetAmt(new Double(mdsCurrentReceiptEntryTO.getNetAmt().doubleValue()
                            + mdsCurrentReceiptEntryTO.getArbitrationAmt().doubleValue()
                            + mdsCurrentReceiptEntryTO.getNoticeAmt().doubleValue()
                            + mdsCurrentReceiptEntryTO.getPenalAmtPayable().doubleValue()));
                } else {                    
                    mdsCurrentReceiptEntryTO = (GDSClosedReciptTO) map.get("mdsReceiptEntryTO");
                }                
                insertReceiptData(map, mdsCurrentReceiptEntryTO, objLogDAO, objLogTO);
            }
            if (mdsReceiptEntryTO != null) { 
                if (bankAdvInsAmt > 0) {
                    if (bankAdvInsAmt > 0 && instalmentPayAmt > 0) {
                        System.out.println("###### INSERT BOTH MDS_RECEIPT TRANS_ID : ");
                        mdsReceiptEntryTO.setNetTransId(CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_NET_TRANS_ID")) + "," + CommonUtil.convertObjToStr(map.get("CURRENT_NET_TRANS_ID")));
                        mdsReceiptEntryTO.setBonusTransId(CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_BONUS_TRANS_ID")) + "," + CommonUtil.convertObjToStr(map.get("CURRENT_BONUS_TRANS_ID")));
                        mdsReceiptEntryTO.setPenalTransId(CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_PENAL_TRANS_ID")));
                        mdsReceiptEntryTO.setNoticeId(CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_NOTICE_TRANS_ID")));
                        mdsReceiptEntryTO.setArbitrationId(CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_ARBITRATION_TRANS_ID")));
                        mdsReceiptEntryTO.setDiscountTransId(CommonUtil.convertObjToStr(map.get("CURRENT_DISCOUNT_TRANS_ID")));
                    } else {
                        System.out.println("###### INSERT ONLY BANKA_ADVANCE MDS_RECEIPT TRANS_ID : ");
                        mdsReceiptEntryTO.setNetTransId(CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_NET_TRANS_ID")));
                        mdsReceiptEntryTO.setBonusTransId(CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_BONUS_TRANS_ID")));
                        mdsReceiptEntryTO.setPenalTransId(CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_PENAL_TRANS_ID")));
                        mdsReceiptEntryTO.setNoticeId(CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_NOTICE_TRANS_ID")));
                        mdsReceiptEntryTO.setArbitrationId(CommonUtil.convertObjToStr(map.get("BANK_ADVANCE_ARBITRATION_TRANS_ID")));
                    }
                } else {
                    System.out.println("###### INSERT ONLY MDS_RECEIPT TRANS_ID : ");
                    mdsReceiptEntryTO.setNetTransId(CommonUtil.convertObjToStr(map.get("CURRENT_NET_TRANS_ID")));
                    mdsReceiptEntryTO.setBonusTransId(CommonUtil.convertObjToStr(map.get("CURRENT_BONUS_TRANS_ID")));
                    mdsReceiptEntryTO.setPenalTransId(CommonUtil.convertObjToStr(map.get("CURRENT_PENAL_TRANS_ID")));
                    mdsReceiptEntryTO.setDiscountTransId(CommonUtil.convertObjToStr(map.get("CURRENT_DISCOUNT_TRANS_ID")));
                    mdsReceiptEntryTO.setNoticeId(CommonUtil.convertObjToStr(map.get("CURRENT_NOTICE_TRANS_ID")));
                    mdsReceiptEntryTO.setArbitrationId(CommonUtil.convertObjToStr(map.get("CURRENT_ARBITRATION_TRANS_ID")));
                }
                mdsReceiptEntryTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
                mdsReceiptEntryTO.setStatus(CommonConstants.STATUS_CREATED);
                mdsReceiptEntryTO.setSingleTransId(generateSingleTransId);
                mdsReceiptEntryTO.setStatusDt(currDt);
                double noticeAmount = CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNoticeAmt());
                mdsReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNetAmt()) - noticeAmount);//9535
               
          
                if (isSplitMDSTransaction != null && isSplitMDSTransaction.equals("Y")) {
                    if (map.containsKey("mdsClosedReceiptEntryTOList") && map.get("mdsClosedReceiptEntryTOList") != null) {
                        List mdssReceiptList = (List) map.get("mdsClosedReceiptEntryTOList");                        
                        for (int i = 0; i < mdssReceiptList.size(); i++) {
                            MDSClosedReciptTO MDSTo = (MDSClosedReciptTO) mdssReceiptList.get(i);
                            MDSTo.setNetTransId(CommonUtil.convertObjToStr(map.get("CURRENT_NET_TRANS_ID")));
                            if (MDSTo.getBonusAmtPayable() > 0) {
                                MDSTo.setBonusTransId(CommonUtil.convertObjToStr(map.get("CURRENT_BONUS_TRANS_ID")));
                            } else {
                                if (map.containsKey("CURRENT_BONUS_TRANS_ID") && map.get("CURRENT_BONUS_TRANS_ID") != null) {
                                    MDSTo.setBonusTransId(CommonUtil.convertObjToStr(map.get("CURRENT_BONUS_TRANS_ID")));
                                }
                            }
                            if (MDSTo.getPenalAmtPayable() > 0) {
                                MDSTo.setPenalTransId(CommonUtil.convertObjToStr(map.get("CURRENT_PENAL_TRANS_ID")));
                            } else {
                                MDSTo.setPenalTransId("");
                            }
                            if (mdsReceiptEntryTO.getDiscountAmt() > 0) {
                                MDSTo.setDiscountTransId(CommonUtil.convertObjToStr(map.get("CURRENT_DISCOUNT_TRANS_ID")));
                            } else {
                                MDSTo.setDiscountTransId("");
                            }

                            MDSTo.setNoticeId(CommonUtil.convertObjToStr(map.get("CURRENT_NOTICE_TRANS_ID")));
                            MDSTo.setArbitrationId(CommonUtil.convertObjToStr(map.get("CURRENT_ARBITRATION_TRANS_ID")));
                            MDSTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                            MDSTo.setStatus(CommonConstants.STATUS_CREATED);
                            MDSTo.setStatusDt(setProperDtFormat(currDt));
                            MDSTo.setSingleTransId(generateSingleTransId);

                            sqlMap.executeUpdate("insertReceiptEntryTO", MDSTo);
                        }
                    }
                } else {
                    List allApplnDetails = sqlMap.executeQueryForList("getAllApplcationDetailsForGDS", map);
                    int schemeCount = allApplnDetails.size();
                    if (allApplnDetails != null && allApplnDetails.size() > 0) {
                        mdsReceiptEntryTO.setBonusAmtAvail(CommonUtil.convertObjToDouble(CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getBonusAmtAvail()).doubleValue()/schemeCount));
                        mdsReceiptEntryTO.setBonusAmtPayable(CommonUtil.convertObjToDouble(CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getBonusAmtPayable()).doubleValue()/schemeCount));
                        mdsReceiptEntryTO.setCurrInst(CommonUtil.convertObjToDouble(CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getCurrInst()).doubleValue()));
                        mdsReceiptEntryTO.setDiscountAmt(CommonUtil.convertObjToDouble(CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getDiscountAmt()).doubleValue()/schemeCount));
                        mdsReceiptEntryTO.setInstAmt(CommonUtil.convertObjToDouble(CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getInstAmt()).doubleValue()/schemeCount));
                        mdsReceiptEntryTO.setInstAmtPayable(CommonUtil.convertObjToDouble(CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getInstAmtPayable()).doubleValue()/schemeCount));
                        mdsReceiptEntryTO.setMdsInterset(CommonUtil.convertObjToDouble(CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getMdsInterset()).doubleValue()/schemeCount));
                        mdsReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNetAmt()).doubleValue()/schemeCount));
                        mdsReceiptEntryTO.setPaidAmt(CommonUtil.convertObjToDouble(CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getPaidAmt()).doubleValue()/schemeCount));
                        mdsReceiptEntryTO.setPenalAmtPayable(CommonUtil.convertObjToDouble(CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getPenalAmtPayable()).doubleValue()/schemeCount));
                        mdsReceiptEntryTO.setPendingInst(CommonUtil.convertObjToDouble(CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getPendingInst()).doubleValue()));
                        mdsReceiptEntryTO.setServiceTaxAmt(CommonUtil.convertObjToDouble(CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getServiceTaxAmt()).doubleValue()/schemeCount));
                        mdsReceiptEntryTO.setTotalInstDue(CommonUtil.convertObjToDouble(CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getTotalInstDue()).doubleValue()));
                        if(isBonusTrans.equalsIgnoreCase("N")){
                            mdsReceiptEntryTO.setBonusAmtPayable(0.0);
                            mdsReceiptEntryTO.setDiscountAmt(0.0);
                        }   
                        for (int i = 0; i < allApplnDetails.size(); i++) {
                                GDSApplicationTO objGDSApplicationTO = (GDSApplicationTO)allApplnDetails.get(i);
                                mdsReceiptEntryTO.setSchemeName(CommonUtil.convertObjToStr(objGDSApplicationTO.getSchemeName()));
                                mdsReceiptEntryTO.setChittalNo(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));
                                sqlMap.executeUpdate("insertGDSReceiptEntryTO", mdsReceiptEntryTO);
                            }
                    }                   
                }
                
                penalTransHappened = false;

                //From Recover Tally List
                if (map.containsKey("FROM_RECOVERY_TALLY") || map.containsKey("FROM_MDS_MEMBER_RECEIPT")) {
                    returnMap.put("BATCH_ID", CommonUtil.convertObjToStr(mdsReceiptEntryTO.getNetTransId()));
                }
                //From Mds_Memeber_Receipt_Entry
                if (map.containsKey("FROM_MDS_MEMBER_RECEIPT")) {
                    returnMap.put("BONUS_TRANS_ID", CommonUtil.convertObjToStr(map.get("CURRENT_BONUS_TRANS_ID")));
                }
                
                if (mdsCurrentReceiptEntryTO != null) {
                    setTransDetails(mdsCurrentReceiptEntryTO);
                }
                if (mdsBankAdvReceiptEntryTO != null) {
                    setTransDetails(mdsBankAdvReceiptEntryTO);
                }
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

    private GDSClosedReciptTO getMDSReceiptEntryTO(double bonusAmt, double instAmt) {
        double netAmt = 0.0;
        netAmt = instAmt - bonusAmt;
        GDSClosedReciptTO mdsBankAdvReceiptEntryTO = new GDSClosedReciptTO();
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
        return mdsBankAdvReceiptEntryTO;
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
    private void insertReceiptData(HashMap map, GDSClosedReciptTO mdsCurrentReceiptEntryTO, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            System.out.println("Inside GDSClosedReciptDAO :: insertReceiptData :: mdsCurrentReceiptEntryTO :: "+mdsCurrentReceiptEntryTO);
            double postageAmt=0.0;
            double renewPostageAmt=0.0;
            String postageAcHd="";
            String cash_unique_id ="";
            String isBonusTransfer = "Y";
            System.out.println("Inside GDSClosedReciptDAO :: insertReceiptData ::map ::" + map);
            if(map.containsKey("POSTAGE_AMT_FOR_INT") || map.containsKey("RENEW_POSTAGE_AMT")){
                postageAmt=CommonUtil.convertObjToDouble(map.get("POSTAGE_AMT_FOR_INT")).doubleValue();
                renewPostageAmt=CommonUtil.convertObjToDouble(map.get("RENEW_POSTAGE_AMT")).doubleValue();
                postageAcHd=CommonUtil.convertObjToStr(map.get("POSTAGE_ACHD"));    
            }
		//added by chithra for service Tax
            HashMap serviceTaxMap = new HashMap();
            double service_taxAmt = 0;
            if (map.containsKey("serviceTaxDetails")) {
                serviceTaxMap = (HashMap) map.get("serviceTaxDetails");
                if (serviceTaxMap.containsKey("TOT_TAX_AMT")) {
                    service_taxAmt = CommonUtil.convertObjToDouble(serviceTaxMap.get("TOT_TAX_AMT"));
                }
            }
            mdsCurrentReceiptEntryTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
            mdsCurrentReceiptEntryTO.setStatus(CommonConstants.STATUS_CREATED);
            mdsCurrentReceiptEntryTO.setStatusDt(currDt);      
            ArrayList transferList = new ArrayList();
            TransferTrans transferTrans = new TransferTrans();
            TxTransferTO transferTo = new TxTransferTO();
            ArrayList TxTransferTO = new ArrayList();
            TransactionTO transactionTO = new TransactionTO();
            HashMap txMap = new HashMap();
            HashMap transactionListMap = new HashMap();
            String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
            transferTrans.setInitiatedBranch(BRANCH_ID);
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            transactionDAO.setInitiatedBranch(_branchCode);
            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
            if(map.containsKey("IS_BONUS_TRANSFER") && map.get("IS_BONUS_TRANSFER") != null){
                isBonusTransfer = CommonUtil.convertObjToStr(map.get("IS_BONUS_TRANSFER"));
            }
            if (map.containsKey("TransactionTO")) {
                HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                }
                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
       
                List lst;
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
                    }//Added by Jeffin John for DEBIT Transfer From Suspense And Advances
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.SUSPENSE)) {
                        debitMap.put("ACT_NUM", transactionTO.getDebitAcctNo());
                        lst = sqlMap.executeQueryForList("getSAAccNoProdIdDet", debitMap);
                        if (lst != null && lst.size() > 0) {
                            debitMap = (HashMap) lst.get(0);
                        }
                    }
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.ADVANCES)) {
                        debitMap.put("prodId", transactionTO.getProductId());
                        lst = sqlMap.executeQueryForList("getAccountHeadProdADHead", debitMap);
                        if (lst != null && lst.size() > 0) {
                            debitMap = (HashMap) lst.get(0);
                        }
                    }//End
                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
                    int schemeCount = 0;
                     HashMap applicationMap = new HashMap();
                     applicationMap.put("GDS_NO",mdsReceiptEntryTO.getGds_no());
                     List allApplnDetails = sqlMap.executeQueryForList("getAllApplcationDetailsForGDS", applicationMap);
                     if(allApplnDetails != null && allApplnDetails.size() > 0){
                         schemeCount = allApplnDetails.size();
                     }
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
                            } 
                            else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("TD")) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                                txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                            }                             
                            else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.SUSPENSE)){
                                txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                                txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.SUSPENSE);
                            } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.ADVANCES)) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("ACCT_HEAD"));
                                txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.ADVANCES);
                            }
                            if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {            
                                txMap.put(TransferTrans.PARTICULARS, map.get("FROM_PARTICULARS_TRANSALL"));
                            } else {
                                txMap.put(TransferTrans.PARTICULARS, "Installment Amt :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsCurrentReceiptEntryTO.getGds_no() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
                            }

                            HashMap interBranchCodeMap = new HashMap();
                            interBranchCodeMap.put("ACT_NUM", transactionTO.getDebitAcctNo());
                            List interBranchCodeList = sqlMap.executeQueryForList("getSelectInterBranchCode", interBranchCodeMap);
                            if (interBranchCodeList != null && interBranchCodeList.size() > 0) {
                                interBranchCodeMap = (HashMap) interBranchCodeList.get(0);             
                                txMap.put(TransferTrans.DR_BRANCH, interBranchCodeMap.get("BRANCH_CODE")); //BRANCH_ID
                                transferTo.setBranchId(CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE")));
                            }else{
                                txMap.put(TransferTrans.DR_BRANCH, mdsCurrentReceiptEntryTO.getBranchCode()); //BRANCH_ID
                                transferTo.setBranchId(BRANCH_ID);
                            }
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
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
                            }else
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNetAmt()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt(currDt);
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                            txMap.put("TRANS_MOD_TYPE", "MDS");
                            if (!glTransActNum.equals("") && glTransActNum.length() > 0) {
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(glTransActNum));
                            } else {
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
                            }
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getGds_no()));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            TxTransferTO.add(transferTo);
                        }
                       
                        for (int i = 0; i < allApplnDetails.size(); i++) {
                            GDSApplicationTO objGDSApplicationTO = (GDSApplicationTO) allApplnDetails.get(i);
                            HashMap schemeMap = new HashMap();
                            schemeMap.put("SCHEME_NAME", objGDSApplicationTO.getSchemeName());
                            List headlst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", schemeMap);
                            if (headlst != null && headlst.size() > 0) {
                                applicationMap = (HashMap) headlst.get(0);
                            }
                            if (!penalTransHappened) {
                                if (CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getPenalAmtPayable()).doubleValue() > 0) {
                                    System.out.println("penal Started");
                                    transferTo = new TxTransferTO();
                                    txMap = new HashMap();
                                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                    txMap.put(TransferTrans.CR_AC_HD, (String) applicationMap.get("PENAL_INTEREST_HEAD"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.CR_BRANCH, mdsCurrentReceiptEntryTO.getBranchCode()); 
                                    
                                    if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {                                        
                                        txMap.put(TransferTrans.PARTICULARS, "Penal" + map.get("FROM_PARTICULARS_TRANSALL"));
                                    } else {
                                        txMap.put(TransferTrans.PARTICULARS, "Penal :" + applicationMap.get("MP_MDS_CODE") + "-" + objGDSApplicationTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
                                    }
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                    txMap.put("TRANS_MOD_TYPE", "MDS");
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getPenalAmtPayable() / schemeCount).doubleValue());
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setTransDt(currDt);
                                    transferTo.setInitiatedBranch(BRANCH_ID);
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                    if (!glTransActNum.equals("") && glTransActNum.length() > 0) {
                                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(glTransActNum));
                                    } else {
                                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
                                    }
                                    transferTo.setGlTransActNum(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));
                                    transactionTO.setChequeNo("SERVICE_TAX");
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    TxTransferTO.add(transferTo);                                   
                                }
                                //Notice Charge Amount Transaction Started
                                if (CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNoticeAmt()).doubleValue() > 0) {
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
                                        for (int t = 0; t < chargeList.size(); t++) {
                                            double chargeAmount = 0.0;
                                            String chargeType = "";
                                            whereMap = (HashMap) chargeList.get(t);
                                            chargeAmount = CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
                                            Rounding rod = new Rounding();
                                            chargeAmount = (double) rod.getNearest((long) (chargeAmount * 100), 100) / 100;
                                            chargeType = CommonUtil.convertObjToStr(whereMap.get("CHARGE_TYPE"));
                                            if (chargeAmount > 0) {
                                                transferTo = new TxTransferTO();
                                                txMap = new HashMap();
                                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("NOTICE_CHARGES_HEAD"));
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put(TransferTrans.CR_BRANCH, mdsCurrentReceiptEntryTO.getBranchCode()); //BRANCH_ID
                                                //babu 26-May-2013  

                                                if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
                                                    //              System.out.println("map.get==22====" + map.get("FROM_PARTICULARS_TRANSALL"));
                                                    txMap.put(TransferTrans.PARTICULARS, chargeType + ": " + map.get("FROM_PARTICULARS_TRANSALL"));
                                                } else {
                                                    txMap.put(TransferTrans.PARTICULARS, chargeType + applicationMap.get(" MP_MDS_CODE") + "-" + objGDSApplicationTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
                                                }
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                                transferTo = transactionDAO.addTransferCreditLocal(txMap, chargeAmount / schemeCount);
                                                transferTo.setTransId("-");
                                                transferTo.setBatchId("-");
                                                transferTo.setTransDt(currDt);
                                                transferTo.setInitiatedBranch(BRANCH_ID);
                                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                                transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                                if (!glTransActNum.equals("") && glTransActNum.length() > 0) {
                                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(glTransActNum));
                                                } else {
                                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
                                                }
                                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));

                                                transactionTO.setChequeNo("SERVICE_TAX");
                                                transferTo.setSingleTransId(generateSingleTransId);
                                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                TxTransferTO.add(transferTo);
                                                //System.out.println("Notice Charge transferTo List  : " + i + " " + transferTo);
                                            }
                                        }
                                    }
                                }                               
                                //Case Expense Amount Transaction
                                if (CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getArbitrationAmt()).doubleValue() > 0) {
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
                                        for (int t = 0; t < chargeList.size(); t++) {
                                            double chargeAmount = 0.0;
                                            String chargeType = "";
                                            whereMap = (HashMap) chargeList.get(t);
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
                                                    //    System.out.println("map.get=33====" + map.get("FROM_PARTICULARS_TRANSALL"));
                                                    txMap.put(TransferTrans.PARTICULARS, chargeType + ": " + map.get("FROM_PARTICULARS_TRANSALL"));
                                                } else {
                                                    txMap.put(TransferTrans.PARTICULARS, chargeType + applicationMap.get(" MP_MDS_CODE") + "-" + objGDSApplicationTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
                                                }
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                                transferTo = transactionDAO.addTransferCreditLocal(txMap, chargeAmount / schemeCount);
                                                transferTo.setTransId("-");
                                                transferTo.setBatchId("-");
                                                transferTo.setTransDt(currDt);
                                                transferTo.setInitiatedBranch(BRANCH_ID);
                                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                                transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                                if (!glTransActNum.equals("") && glTransActNum.length() > 0) {
                                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(glTransActNum));
                                                } else {
                                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
                                                }
                                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));

                                                transferTo.setSingleTransId(generateSingleTransId);
                                                transactionTO.setChequeNo("SERVICE_TAX");
                                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                TxTransferTO.add(transferTo);
                                                // System.out.println("Arbitration transferTo List  : " + i + " " + transferTo);
                                            }
                                        }
                                    }
                                }
                                //added by chithra for service Tax
                                if (serviceTaxMap != null && serviceTaxMap.size() > 0) {
                                    transferTo = new TxTransferTO();
                                    txMap = new HashMap();
                                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxMap.get("TAX_HEAD_ID")));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.CR_BRANCH, mdsCurrentReceiptEntryTO.getBranchCode()); //BRANCH_ID
                                    if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
                                        txMap.put(TransferTrans.PARTICULARS, "Service Tax" + map.get("FROM_PARTICULARS_TRANSALL"));
                                    } else {
                                        txMap.put(TransferTrans.PARTICULARS, "Service Tax :" + applicationMap.get("MP_MDS_CODE") + "-" + objGDSApplicationTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
                                    }
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                    txMap.put("TRANS_MOD_TYPE", "MDS");
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, service_taxAmt / schemeCount);
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setTransDt(currDt);
                                    transferTo.setInitiatedBranch(BRANCH_ID);
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));

                                    if (!glTransActNum.equals("") && glTransActNum.length() > 0) {
                                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(glTransActNum));
                                    } else {
                                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
                                    }
                                    transferTo.setGlTransActNum(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));
                                    transactionTO.setChequeNo("SERVICE_TAX");
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    TxTransferTO.add(transferTo);
                                }
                                if (i == (allApplnDetails.size() - 1)) {
                                    penalTransHappened = true;
                                }

                            }
                            double finalReceivingAmt = CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNetAmt()).doubleValue()
                                    - (CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getPenalAmtPayable()).doubleValue()
                                    + CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNoticeAmt()).doubleValue()
                                    + CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getArbitrationAmt()).doubleValue() + service_taxAmt);

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
                                    //        System.out.println("map.get=33====" + map.get("FROM_PARTICULARS_TRANSALL"));
                                    txMap.put(TransferTrans.PARTICULARS, map.get("FROM_PARTICULARS_TRANSALL"));
                                } else {
                                    txMap.put(TransferTrans.PARTICULARS, "Installment Amt :" + applicationMap.get("MP_MDS_CODE") + "-" + objGDSApplicationTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
                                }
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
//                            System.out.println("txMap5 : " + txMap+"serviceAmt :"+finalReceivingAmt);
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                if (postageAmt > 0.0 || renewPostageAmt > 0.0) {
                                    if (postageAmt > 0.0) {
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, (finalReceivingAmt - postageAmt)/ schemeCount);
                                    }
                                    if (renewPostageAmt > 0.0) {
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, (finalReceivingAmt - renewPostageAmt)/ schemeCount);
                                    }

                                } else {
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, finalReceivingAmt/ schemeCount);
                                }
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));
                                transferTo.setSingleTransId(generateSingleTransId);
                                TxTransferTO.add(transferTo);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setTransDt(currDt);
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
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
                                            txMap.put(TransferTrans.PARTICULARS, "Installment Amt :" + applicationMap.get("MP_MDS_CODE") + "-" + objGDSApplicationTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
                                        }
                                        txMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        txMap.put("TRANS_MOD_TYPE", "MDS");
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, postageAmt);
                                        transferTo.setInitiatedBranch(BRANCH_ID);
                                        transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setGlTransActNum(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        TxTransferTO.add(transferTo);
                                    }
                                    if (renewPostageAmt > 0.0) {
                                        txMap.put(TransferTrans.CR_AC_HD, postageAcHd);
                                        txMap.put("AUTHORIZEREMARKS", "MDS_POSTAGE");
                                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        //babu 26-May-2013  

                                        if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
                                            //  System.out.println("map.get=66====" + map.get("FROM_PARTICULARS_TRANSALL"));
                                            txMap.put(TransferTrans.PARTICULARS, map.get("FROM_PARTICULARS_TRANSALL"));
                                        } else {
                                            txMap.put(TransferTrans.PARTICULARS, "Installment Amt :" + applicationMap.get("MP_MDS_CODE") + "-" + objGDSApplicationTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
                                        }
                                        txMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        txMap.put("TRANS_MOD_TYPE", "MDS");
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, renewPostageAmt);
                                        transferTo.setInitiatedBranch(BRANCH_ID);
                                        transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setGlTransActNum(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        TxTransferTO.add(transferTo);
                                    }
                                }
                            }

                        }
                        
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
                      //  System.out.println("transactionDAO map : " + map);
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
                        if(isBonusTransfer.equalsIgnoreCase("Y"))
                        commonTransactionCashandTransfer(map, mdsCurrentReceiptEntryTO, debitMap, applicationMap, schemeCount,allApplnDetails);
                        //commonTransactionCashandTransfer(map, mdsCurrentReceiptEntryTO, debitMap, applicationMap);
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
                        cashTransMap.put("SCREEN_NAME", "MDS Receipts");
                        cashTransMap.put("TRANS_ALL_ID", CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                        cashTransMap.put("TRANS_MOD_TYPE", "MDS");
                        CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                        double finalReceivingAmt = CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNetAmt()).doubleValue()
                                - (CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getPenalAmtPayable()).doubleValue()
                                + CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNoticeAmt()).doubleValue()
                                + CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getArbitrationAmt()).doubleValue()+service_taxAmt);
                        //Changed By Suresh
                        if (map.containsKey("MDS_CLOSURE")) {
                            cashTransMap.put("ACC_HEAD", applicationMap.get("MDS_RECEIVABLE_HEAD"));
                        } else {
                            cashTransMap.put("ACC_HEAD", applicationMap.get("RECEIPT_HEAD"));
                        }
                        //babu 26-May-2013  

                        if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
                         //   System.out.println("map.get=999===" + map.get("FROM_PARTICULARS_TRANSALL"));
                            cashTransMap.put(TransferTrans.PARTICULARS, map.get("FROM_PARTICULARS_TRANSALL"));
                        } else {
                            cashTransMap.put("PARTICULARS", "Installment Amt ");
                        }
                        cashTransMap.put("AMOUNT", String.valueOf(finalReceivingAmt));
                        cashTransMap.put("MP_MDS_CODE", applicationMap.get("MP_MDS_CODE"));
                        cashTransMap.put(CommonConstants.SCREEN,(String) map.get(CommonConstants.SCREEN));
                        cashTransMap.put("GDS_TRANS_FOR","INSTALLMENT_NORMAL");
                        double transactionAmount = finalReceivingAmt;
                        ArrayList applncashList = commonApplicationListForCashTrans(map,allApplnDetails,mdsCurrentReceiptEntryTO,cash_unique_id,transactionAmount,cashTransMap);//nithya----     
                        //ArrayList applncashList = commonApplicationList(cashTransMap);
                        //                        applncashList = applicationListMDS(cashTransMap);
                        cashTransMap.put("DAILYDEPOSITTRANSTO", applncashList);
                        cashTransMap.put("SCREEN_NAME", "MDS Receipts");
                        cashTransMap.put("TRANS_ALL_ID", CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                        cashTransMap.put("SINGLE_TRANS_ID", generateSingleTransId);
                        HashMap cashMap = cashTransactionDAO.execute(cashTransMap, false);
                        mdsCurrentReceiptEntryTO.setNetTransId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                     //   System.out.println("cashMap :" + cashMap);
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
                            if (CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getPenalAmtPayable()).doubleValue() > 0) {
                                cashTransMap.put("ACC_HEAD", applicationMap.get("PENAL_INTEREST_HEAD"));
                                //babu 26-May-2013  
                                if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
                                    System.out.println("map.get=12121====" + map.get("FROM_PARTICULARS_TRANSALL"));
                                    cashTransMap.put(TransferTrans.PARTICULARS, "Penal " + map.get("FROM_PARTICULARS_TRANSALL"));
                                } else {
                                    cashTransMap.put("PARTICULARS", "Penal ");
                                }
                                cashTransMap.put("AMOUNT", mdsCurrentReceiptEntryTO.getPenalAmtPayable());
                                cashTransMap.put("MP_MDS_CODE", applicationMap.get("MP_MDS_CODE"));
                                cashTransMap.put("GDS_TRANS_FOR","PENAL");
                                //applncashList = commonApplicationList(cashTransMap);
                                double penalAmount = mdsCurrentReceiptEntryTO.getPenalAmtPayable();
                                applncashList = commonApplicationListForCashTrans(cashTransMap,allApplnDetails,mdsCurrentReceiptEntryTO,cash_unique_id,penalAmount,cashTransMap);
                                cashTransMap.put("DAILYDEPOSITTRANSTO",applncashList);
                                cashTransMap.put("SCREEN_NAME","MDS Receipts");
                                cashTransMap.put("TRANS_MOD_TYPE", "MDS");
                                cashMap = cashTransactionDAO.execute(cashTransMap,false);
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
                            if (CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNoticeAmt()).doubleValue() > 0) {
                                System.out.println("Notice Charge Started !!!! ");
                                HashMap whereMap = new HashMap();
                                HashMap headMap = new HashMap();
                                whereMap.put("ACT_NUM", CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()) + "_" + CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getSubNo()));
                                List chargeList = (List) sqlMap.executeQueryForList("getMDSNoticeChargeDetails", whereMap);
                                if (allApplnDetails != null && allApplnDetails.size() > 0) {
                                    if (chargeList != null && chargeList.size() > 0) {
                                        applncashList = new ArrayList();
                                        for (int k = 0; k < allApplnDetails.size(); k++) {
                                            GDSApplicationTO objGDSApplicationTO = (GDSApplicationTO) allApplnDetails.get(k);
                                            HashMap schemeMap = new HashMap();
                                            schemeMap.put("SCHEME_NAME", objGDSApplicationTO.getSchemeName());
                                            List headlst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", schemeMap);
                                            if (headlst != null && headlst.size() > 0) {
                                                headMap = (HashMap) headlst.get(0);
                                            }
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
                                                        cashTransMap.put("ACC_HEAD", headMap.get("NOTICE_CHARGES_HEAD"));
                                                    }
                                                    if (chargeType.equals("POSTAGE CHARGES")) {
                                                        cashTransMap.put("ACC_HEAD", headMap.get("POSTAGE_HEAD"));
                                                    }
                                                    if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {                                                     
                                                        cashTransMap.put(TransferTrans.PARTICULARS, chargeType + " " + map.get("FROM_PARTICULARS_TRANSALL"));
                                                    } else {
                                                        cashTransMap.put("PARTICULARS", chargeType);
                                                    }
                                                    cashTransMap.put("AMOUNT", String.valueOf(chargeAmount));
                                                    cashTransMap.put("MP_MDS_CODE", headMap.get("MP_MDS_CODE"));
                                                    cashTransMap.put("TRANS_MOD_TYPE", "MDS");
                                                    cashTransMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                                                    cashTransMap.put("GL_TRANS_ACT_NUM",objGDSApplicationTO.getChittalNo());
                                                    applncashList.add(createCashTransactionTO(cashTransMap));
                                                }
                                            }
                                        }
                                    }
                                    cashTransMap.put("DAILYDEPOSITTRANSTO", applncashList);
                                    cashTransMap.put("SCREEN_NAME", "MDS Receipts");
                                    cashTransMap.put("TRANS_ALL_ID", CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
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
                            if (CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getArbitrationAmt()).doubleValue() > 0) {
                                System.out.println("Arbitration Started");
                                HashMap whereMap = new HashMap();
                                HashMap headMap = new HashMap();
                                whereMap.put("ACT_NUM", CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()) + "_" + CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getSubNo()));
                                List chargeList = (List) sqlMap.executeQueryForList("getMDSCaseChargeDetails", whereMap);
                                if (allApplnDetails != null && allApplnDetails.size() > 0) {
                                    applncashList = new ArrayList();
                                    for (int k = 0; k < applncashList.size(); k++) {
                                        GDSApplicationTO objGDSApplicationTO = (GDSApplicationTO) allApplnDetails.get(k);
                                        HashMap schemeMap = new HashMap();
                                        schemeMap.put("SCHEME_NAME", objGDSApplicationTO.getSchemeName());
                                        List headlst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", schemeMap);
                                        if (headlst != null && headlst.size() > 0) {
                                            headMap = (HashMap) headlst.get(0);
                                        }
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
                                                    cashTransMap.put("ACC_HEAD", headMap.get(chargeType));                                                   
                                                    if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {                                                       
                                                        cashTransMap.put(TransferTrans.PARTICULARS, chargeType + " " + map.get("FROM_PARTICULARS_TRANSALL"));
                                                    } else {
                                                        cashTransMap.put("PARTICULARS", chargeType);
                                                    }
                                                    cashTransMap.put("AMOUNT", String.valueOf(chargeAmount));
                                                    cashTransMap.put("MP_MDS_CODE", headMap.get("MP_MDS_CODE"));
                                                    cashTransMap.put("TRANS_ALL_ID", CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                                    cashTransMap.put("TRANS_MOD_TYPE", "MDS");
                                                    cashTransMap.put("GL_TRANS_ACT_NUM",objGDSApplicationTO.getChittalNo());
                                                    cashTransMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                                                    applncashList.add(createCashTransactionTO(cashTransMap));
                                                }
                                            }
                                        }
                                    }
                                    cashTransMap.put("DAILYDEPOSITTRANSTO", applncashList);
                                    cashTransMap.put("SCREEN_NAME", "MDS Receipts");
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
                            if (service_taxAmt > 0) {
                                cashTransMap.put("ACC_HEAD", serviceTaxMap.get("TAX_HEAD_ID"));
                                if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
                                    cashTransMap.put(TransferTrans.PARTICULARS, "Service Tax" + map.get("FROM_PARTICULARS_TRANSALL"));
                                } else {
                                    cashTransMap.put("PARTICULARS", "Service Tax");
                                }
                                cashTransMap.put("AMOUNT", service_taxAmt);
                                cashTransMap.put("MP_MDS_CODE", applicationMap.get("MP_MDS_CODE"));
                                cashTransMap.put("TRANS_ALL_ID", CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                cashTransMap.put("TRANS_MOD_TYPE", "MDS");                                
                                cashTransMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                                applncashList.add(createCashTransactionTO(cashTransMap));
                            }
                            cashTransMap.put("DAILYDEPOSITTRANSTO", applncashList);
                            cashTransMap.put("SCREEN_NAME", "MDS Receipts");
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
                            penalTransHappened = true;
                        }
                      //  commonTransactionCashandTransfer(map, mdsCurrentReceiptEntryTO, debitMap, applicationMap);
                        if(isBonusTransfer.equalsIgnoreCase("Y"))
                        commonTransactionCashandTransfer(map, mdsCurrentReceiptEntryTO, debitMap, applicationMap, schemeCount,allApplnDetails);
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
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void insertBankAdvData(HashMap map, GDSClosedReciptTO mdsBankAdvReceiptEntryTO, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            System.out.println("###### MDS BANK ADVANCE ###### : "+map);
            mdsBankAdvReceiptEntryTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
            mdsBankAdvReceiptEntryTO.setStatus(CommonConstants.STATUS_CREATED);
            mdsBankAdvReceiptEntryTO.setStatusDt(currDt);
          //  System.out.println("###### insertTransactionData : " + map);
            ArrayList transferList = new ArrayList();
            TransferTrans transferTrans = new TransferTrans();
            TxTransferTO transferTo = new TxTransferTO();
            ArrayList TxTransferTO = new ArrayList();
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
                List lst;
//                HashMap applicationMap = new HashMap();
//                applicationMap.put("SCHEME_NAME", mdsBankAdvReceiptEntryTO.getSchemeName());
//                List lst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", applicationMap);
//                if (lst != null && lst.size() > 0) {
//                    applicationMap = (HashMap) lst.get(0);
//                }
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
            //        System.out.println("###### insertTransactionData mdsBankAdvReceiptEntryTO : " + mdsBankAdvReceiptEntryTO);
                    int schemeCount = 0;
                     HashMap applicationMap = new HashMap();
                     applicationMap.put("GDS_NO",mdsReceiptEntryTO.getGds_no() );
                     List allApplnDetails = sqlMap.executeQueryForList("getAllApplcationDetailsForGDS", applicationMap);
                     if(allApplnDetails != null && allApplnDetails.size() > 0){
                         schemeCount = allApplnDetails.size();
                     }
                    if (transactionTO.getTransType().equals("TRANSFER")) {
                        if (CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getNetAmt()).doubleValue() > 0) {              
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL") && transactionTO.getProductId().equals("")) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("OA")) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                                txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                            }else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("TD")) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                                txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                            }
                            txMap.put(TransferTrans.PARTICULARS, "Installment Amt :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsBankAdvReceiptEntryTO.getChittalNo() + "_" + mdsBankAdvReceiptEntryTO.getSubNo());
                            txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
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
                            }else
                                txMap.put("TRANS_MOD_TYPE", "GL");
                            GDSClosedReciptTO mdsCurrentReceiptEntryTO = (GDSClosedReciptTO) map.get("mdsReceiptEntryTO");
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getNetAmt()).doubleValue());
                            //transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getNetAmt()).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt(currDt);
                            transferTo.setBranchId(BRANCH_ID);
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            TxTransferTO.add(transferTo);                          
                        }
                        
                        for (int i = 0; i < allApplnDetails.size(); i++) {
                            GDSApplicationTO objGDSApplicationTO = (GDSApplicationTO) allApplnDetails.get(i);
                            HashMap schemeMap = new HashMap();
                            schemeMap.put("SCHEME_NAME", objGDSApplicationTO.getSchemeName());
                            List headlst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", schemeMap);
                            if (headlst != null && headlst.size() > 0) {
                                applicationMap = (HashMap) headlst.get(0);
                            }
                            if (CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getNetAmt()).doubleValue() > 0) {                               
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("BANKING_REP_PAY_HEAD"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_BRANCH, mdsBankAdvReceiptEntryTO.getBranchCode());//BRANCH_ID);
                                txMap.put(TransferTrans.PARTICULARS, "Installment Amt :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsBankAdvReceiptEntryTO.getChittalNo() + "_" + mdsBankAdvReceiptEntryTO.getSubNo() + ":BANK ADVANCE");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                if (map.containsKey("POSTAGE_AMT_FOR_INT") || map.containsKey("RENEW_POSTAGE_AMT")) {
                                    if (map.containsKey("POSTAGE_AMT_FOR_INT")) {
                                        if (postageAmt > 0.0) {
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, (CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getNetAmt()).doubleValue() - postageAmt) / schemeCount);
                                        }
                                    }
                                    if (map.containsKey("RENEW_POSTAGE_AMT")) {
                                        if (renewPostageAmt > 0.0) {
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, (CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getNetAmt()).doubleValue() - renewPostageAmt) / schemeCount);
                                        }
                                    }
                                } else {
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, (CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getNetAmt()).doubleValue()) / schemeCount);
                                }
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setTransDt(currDt);
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));
                                if (map.containsKey("POSTAGE_AMT_FOR_INT") || map.containsKey("RENEW_POSTAGE_AMT")) {
                                    if (postageAmt > 0.0) {
                                        txMap.put(TransferTrans.CR_AC_HD, postageAcHd);
                                        txMap.put("AUTHORIZEREMARKS", "MDS_POSTAGE");
                                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.PARTICULARS, "Installment Amt :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsBankAdvReceiptEntryTO.getChittalNo() + "_" + mdsBankAdvReceiptEntryTO.getSubNo() + ":BANK ADVANCE");
                                        txMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        txMap.put("TRANS_MOD_TYPE", "MDS");
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, postageAmt/schemeCount);
                                        transferTo.setInitiatedBranch(BRANCH_ID);
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    }
                                    if (renewPostageAmt > 0.0) {
                                        txMap.put(TransferTrans.CR_AC_HD, postageAcHd);
                                        txMap.put("AUTHORIZEREMARKS", "MDS_POSTAGE");
                                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.PARTICULARS, "Installment Amt :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsBankAdvReceiptEntryTO.getChittalNo() + "_" + mdsBankAdvReceiptEntryTO.getSubNo() + ":BANK ADVANCE");
                                        txMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        txMap.put("TRANS_MOD_TYPE", "MDS");
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, renewPostageAmt/schemeCount);
                                        transferTo.setInitiatedBranch(BRANCH_ID);
                                    }
                                    transferTo.setGlTransActNum(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    TxTransferTO.add(transferTo);
                                }                              
                            }
                             if (!penalTransHappened) {
                                if (CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getPenalAmtPayable()).doubleValue() > 0) {
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
                                    txMap.put("TRANS_MOD_TYPE", "MDS");
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getPenalAmtPayable()/schemeCount).doubleValue());
                                    transferTo.setTransId("-");
                                    transferTo.setBatchId("-");
                                    transferTo.setTransDt(currDt);
                                    transferTo.setInitiatedBranch(BRANCH_ID);
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                    transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));
                                    transactionTO.setChequeNo("SERVICE_TAX");
                                    transferTo.setGlTransActNum(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    TxTransferTO.add(transferTo);
                                    //    System.out.println("transferTo List 2 : " + transferTo);
                                }
                                //Notice Charge Amount Transaction Started
                                if (CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getNoticeAmt()).doubleValue() > 0) {
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
                                        for (int t = 0; t < chargeList.size(); t++) {
                                            double chargeAmount = 0.0;
                                            String chargeType = "";
                                            whereMap = (HashMap) chargeList.get(t);
                                            chargeAmount = CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
                                            Rounding rod = new Rounding();
                                            chargeAmount = (double) rod.getNearest((long) (chargeAmount * 100), 100) / 100;
                                            chargeType = CommonUtil.convertObjToStr(whereMap.get("CHARGE_TYPE"));
                                            if (chargeAmount > 0) {
                                                transferTo = new TxTransferTO();
                                                txMap = new HashMap();
                                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("NOTICE_CHARGES_HEAD"));
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put(TransferTrans.CR_BRANCH, mdsBankAdvReceiptEntryTO.getBranchCode());//BRANCH_ID);
                                                txMap.put(TransferTrans.PARTICULARS, chargeType + applicationMap.get(" MP_MDS_CODE") + "-" + mdsBankAdvReceiptEntryTO.getChittalNo() + "_" + mdsBankAdvReceiptEntryTO.getSubNo());
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                                transferTo = transactionDAO.addTransferCreditLocal(txMap, chargeAmount/schemeCount);
                                                transferTo.setTransId("-");
                                                transferTo.setBatchId("-");
                                                transferTo.setTransDt(currDt);
                                                transferTo.setInitiatedBranch(BRANCH_ID);
                                                transferTo.setSingleTransId(generateSingleTransId);
                                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                                transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));
                                                transactionTO.setChequeNo("SERVICE_TAX");
                                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));
                                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                TxTransferTO.add(transferTo);
                                                //  System.out.println("Notice Charge transferTo List  : " + i + " " + transferTo);
                                            }
                                        }
                                    }
                                }
                                //Case Expense Amount Transaction
                                if (CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getArbitrationAmt()).doubleValue() > 0) {
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
                                        for (int t = 0; t < chargeList.size(); t++) {
                                            double chargeAmount = 0.0;
                                            String chargeType = "";
                                            whereMap = (HashMap) chargeList.get(t);
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
                                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                                transferTo = transactionDAO.addTransferCreditLocal(txMap, chargeAmount/schemeCount);
                                                transferTo.setTransId("-");
                                                transferTo.setBatchId("-");
                                                transferTo.setTransDt(currDt);
                                                transferTo.setInitiatedBranch(BRANCH_ID);
                                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                                transferTo.setSingleTransId(generateSingleTransId);
                                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                                transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));
                                                transactionTO.setChequeNo("SERVICE_TAX");
                                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));
                                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                TxTransferTO.add(transferTo);
                                                //  System.out.println("Arbitration transferTo List  : " + i + " " + transferTo);
                                            }
                                        }
                                    }
                                }
                               if (i == (allApplnDetails.size() - 1)) {
                                    penalTransHappened = true;
                                }
                            }
                             if (bonusreversal) {
                            System.out.println("Final Amount Transaction Started");
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            //Added By Suresh
                            if (map.containsKey("MDS_CLOSURE")) {
                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("SUNDRY_RECEIPT_HEAD"));
                            } else {
                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("BONUS_PAYABLE_HEAD"));
                            }
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, mdsBankAdvReceiptEntryTO.getBranchCode());//BRANCH_ID);
                            txMap.put(TransferTrans.PARTICULARS, "Bonus Reversal :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsBankAdvReceiptEntryTO.getChittalNo() + "_" + mdsBankAdvReceiptEntryTO.getSubNo() + ":BANK ADVANCE");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
//                            System.out.println("txMap5 : " + txMap + "bankBonusAmt :" + CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getBonusAmtPayable()).doubleValue());
//                            System.out.println("bankBonusReverseAmt" + bankBonusReverseAmt);
                            // transferTo =  transactionDAO.addTransferCreditLocal(txMap, bankBonusReverseAmt);
                            txMap.put("TRANS_MOD_TYPE", "MDS");
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getBonusAmtPayable()/schemeCount).doubleValue());
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt(currDt);
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            TxTransferTO.add(transferTo);
                        }                            
                        }

                        HashMap applnMap = new HashMap();
                        transferDAO = new TransferDAO();
                        map.put("MODE", map.get("COMMAND"));
                        map.put("COMMAND", map.get("MODE"));
                        map.put("TxTransferTO", TxTransferTO);
                        transMap = transferDAO.execute(map, false);
                        mdsBankAdvReceiptEntryTO.setNetTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                      //  System.out.println("transactionDAO map : " + map);
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
                        sqlMap.executeUpdate("updateLinkBatchIdTransfer", linkBatchMap);
//                        commonTransactionCashandTransferBankAdv(map, mdsBankAdvReceiptEntryTO, debitMap,applicationMap);
                    } else if (transactionTO.getTransType().equals("CASH")) {
                        if (map.containsKey("MEMBER_RECEIPT_SINGLE_ID") && map.get("MEMBER_RECEIPT_SINGLE_ID") != null) {
                            generateSingleTransId = CommonUtil.convertObjToStr(map.get("MEMBER_RECEIPT_SINGLE_ID"));
                        } else {
                            generateSingleTransId = generateLinkID();
                        }
                        String cash_unique_id = "";
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
                        cashTransMap.put("SCREEN_NAME", "MDS Receipts");
                        cashTransMap.put("TRANS_ALL_ID", CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                        cashTransMap.put("TRANS_MOD_TYPE", "MDS");
                        CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                        double finalReceivingAmt = CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getNetAmt()).doubleValue();
                        cashTransMap.put("ACC_HEAD",applicationMap.get("BANKING_REP_PAY_HEAD"));
                        cashTransMap.put("PARTICULARS","Installment Amt ");
                        cashTransMap.put("AMOUNT",String.valueOf(finalReceivingAmt));
                        cashTransMap.put(CommonConstants.SCREEN,(String) map.get(CommonConstants.SCREEN));
                        cashTransMap.put("GDS_TRANS_FOR","INSTALLMENT");
                        
                        //ArrayList applncashList = commonApplicationList(cashTransMap);
                        double transactionAmount = finalReceivingAmt;
                        ArrayList applncashList = commonApplicationListForCashTrans(map,allApplnDetails,mdsBankAdvReceiptEntryTO,cash_unique_id,transactionAmount,cashTransMap);//nithya---- 
                        cashTransMap.put("DAILYDEPOSITTRANSTO",applncashList);
                        HashMap cashMap = cashTransactionDAO.execute(cashTransMap, false);
                        mdsBankAdvReceiptEntryTO.setNetTransId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                      //  System.out.println("cashMap :" + cashMap);
                        String command = CommonUtil.convertObjToStr(mdsReceiptEntryTO.getCommand());
                        transactionDAO.setBatchId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                        transactionDAO.setBatchDate(currDt);
                        map.put("MODE", "INSERT");
                        map.put("COMMAND", map.get("MODE"));
                        transactionDAO.execute(map);
                        //System.out.println("afeter insatall pay" + (mdsBankAdvReceiptEntryTO.getNetAmt()).doubleValue());;
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
                        //System.out.println("Trans ID 1 :: " + cashMap.get("TRANS_ID"));
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
                        if(!penalTransHappened){
                            if(CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getPenalAmtPayable()).doubleValue()>0){
                                cashTransMap.put("ACC_HEAD",applicationMap.get("PENAL_INTEREST_HEAD"));
                                cashTransMap.put("PARTICULARS","Penal ");
                                cashTransMap.put("AMOUNT",mdsBankAdvReceiptEntryTO.getPenalAmtPayable());
                                cashTransMap.put("TRANS_MOD_TYPE", "MDS");
                                cashTransMap.put(CommonConstants.SCREEN,(String) map.get(CommonConstants.SCREEN));
                                cashTransMap.put("GDS_TRANS_FOR","PENAL");
                                //applncashList = commonApplicationList(cashTransMap);
                                applncashList = commonApplicationListForCashTrans(map,allApplnDetails,mdsBankAdvReceiptEntryTO,cash_unique_id,mdsBankAdvReceiptEntryTO.getPenalAmtPayable(),cashTransMap);//nithya---- 
                                cashTransMap.put("DAILYDEPOSITTRANSTO", applncashList);
                                cashTransMap.put("DAILYDEPOSITTRANSTO",applncashList);
                                cashTransMap.put("SCREEN_NAME","MDS Receipts");
                                cashTransMap.put("SINGLE_TRANS_ID", generateSingleTransId);
                                cashTransMap.put("TRANS_ALL_ID",CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                cashMap = cashTransactionDAO.execute(cashTransMap,false);
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
                               // System.out.println("Trans ID 2 :: " + cashMap.get("TRANS_ID"));
                                linkBatchMap.put("TRANS_DT", currDt);
                                linkBatchMap.put("INITIATED_BRANCH", _branchCode);
                                sqlMap.executeUpdate("updateLinkBatchIdCash", linkBatchMap);
                            }
                            //Notice Charge Amount Transaction Started
                            if (CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getNoticeAmt()).doubleValue() > 0) {
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

                                //System.out.println("Notice Charge Started !!!");
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
                                            cashTransMap.put("SINGLE_TRANS_ID", generateSingleTransId);
                                            cashTransMap.put("AMOUNT", String.valueOf(chargeAmount));
                                            cashTransMap.put("MP_MDS_CODE", applicationMap.get("MP_MDS_CODE"));
                                            cashTransMap.put("TRANS_MOD_TYPE", "MDS");
                                            cashTransMap.put(CommonConstants.SCREEN, (String) map.get(CommonConstants.SCREEN));
                                            cashTransMap.put("GDS_TRANS_FOR","CHARGES");
                                            // ArrayList lstNoticedata=commonApplicationList(cashTransMap);
                                            ArrayList lstNoticedata = commonApplicationListForCashTrans(map, allApplnDetails, mdsBankAdvReceiptEntryTO, cash_unique_id, chargeAmount,cashTransMap);
                                            if (lstNoticedata.size() > 0) {
                                                applncashList.add(lstNoticedata.get(0));
                                            }
                                        }
                                    }
                                    cashTransMap.put("DAILYDEPOSITTRANSTO", applncashList);
                                    cashTransMap.put("SCREEN_NAME", "MDS Receipts");
                                    cashTransMap.put("TRANS_ALL_ID", CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
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
                                   // System.out.println("Trans ID 3 :: " + cashMap.get("TRANS_ID"));
                                    linkBatchMap.put("TRANS_DT", currDt);
                                    linkBatchMap.put("INITIATED_BRANCH", _branchCode);
                                    sqlMap.executeUpdate("updateLinkBatchIdCash", linkBatchMap);
                                }
                            }
                            //Case Expense Amount Transaction
                            if (CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getArbitrationAmt()).doubleValue() > 0) {
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
                                            }else if (chargeType.equals("MISCELLANEOUS CHARGES")) {
                                                chargeType = "MISCELLANEOUS_CHARGES";
                                            }
                                            else if (chargeType.equals("OTHER CHARGES")) {
                                                chargeType = "OTHER_CHARGES";
                                            }
                                            else if (chargeType.equals("LEGAL CHARGES")) {
                                                chargeType = "LEGAL_CHARGES";
                                            }
                                            else if (chargeType.equals("INSURANCE CHARGES")) {
                                                chargeType = "INSURANCE_CHARGES";
                                            }
//                                            System.out.println("###### chargeType Head : " + chargeType);
//                                            System.out.println("###### chargeAmount : " + chargeAmount);
//                                            System.out.println("###### chargeAmount Head2222: " + applicationMap.get(chargeType));
//                                            cashTransMap.put("ACC_HEAD",applicationMap.get("CASE_EXPENSE_HEAD"));
                                            cashTransMap.put("ACC_HEAD", applicationMap.get(chargeType));
                                            cashTransMap.put("GDS_TRANS_FOR","ARB_CHARGES");
                                            cashTransMap.put("PARTICULARS", chargeType);
                                            cashTransMap.put("SINGLE_TRANS_ID", generateSingleTransId);
                                            cashTransMap.put("AMOUNT", String.valueOf(chargeAmount));
                                            cashTransMap.put("MP_MDS_CODE", applicationMap.get("MP_MDS_CODE"));
                                            cashTransMap.put("TRANS_MOD_TYPE", "MDS");
                                            cashTransMap.put(CommonConstants.SCREEN,(String) map.get(CommonConstants.SCREEN));
                                            //ArrayList lstdata=commonApplicationList(cashTransMap);
                                            ArrayList lstdata=commonApplicationListForCashTrans(map,allApplnDetails,mdsBankAdvReceiptEntryTO,cash_unique_id,chargeAmount,cashTransMap);
                                            if(lstdata.size() > 0){
                                            applncashList.add(lstdata.get(0));
                                            }
                                        }
                                    }
                                    cashTransMap.put("DAILYDEPOSITTRANSTO", applncashList);
                                    cashTransMap.put("SINGLE_TRANS_ID", generateSingleTransId);
                                    cashTransMap.put("SCREEN_NAME", "MDS Receipts");
                                    cashTransMap.put("TRANS_ALL_ID", CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
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
                        if (mdsBankAdvReceiptEntryTO.getBonusAmtPayable() != null && CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getBonusAmtPayable()).doubleValue() > 0.0) {
                            if (bonusreversal) {
                                //Added By Suresh
                                if (map.containsKey("MDS_CLOSURE")) {
                                    cashTransMap.put("ACC_HEAD", applicationMap.get("SUNDRY_RECEIPT_HEAD"));
                                } else {
                                    cashTransMap.put("ACC_HEAD", applicationMap.get("BONUS_PAYABLE_HEAD"));
                                }
                                cashTransMap.put("PARTICULARS", "Bonus");
                                cashTransMap.put("AMOUNT", CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getBonusAmtPayable()).doubleValue());
                               // System.out.println("####### cashTransMap : " + cashTransMap);
                                cashTransMap.put("TRANS_MOD_TYPE", "MDS");
                                cashTransMap.put(CommonConstants.SCREEN,(String) map.get(CommonConstants.SCREEN));
                                cashTransMap.put("GDS_TRANS_FOR","BONUS_REVERSAL");
                                applncashList = commonApplicationListForCashTrans(map,allApplnDetails,mdsBankAdvReceiptEntryTO,cash_unique_id,CommonUtil.convertObjToDouble(mdsBankAdvReceiptEntryTO.getBonusAmtPayable()).doubleValue(),cashTransMap);
                                cashTransMap.put("DAILYDEPOSITTRANSTO", applncashList);
                                cashTransMap.put("SINGLE_TRANS_ID", generateSingleTransId);
                                cashTransMap.put("SCREEN_NAME", "MDS Receipts");
                                cashTransMap.put("TRANS_ALL_ID", CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                                cashMap = cashTransactionDAO.execute(cashTransMap, false);
                                mdsBankAdvReceiptEntryTO.setBonusTransId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
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
//                        commonTransactionCashandTransferBankAdv(map, mdsBankAdvReceiptEntryTO, debitMap,applicationMap);
                    }
                    map.put("BANK_ADVANCE_NET_TRANS_ID", mdsBankAdvReceiptEntryTO.getNetTransId());
                    map.put("BANK_ADVANCE_BONUS_TRANS_ID", mdsBankAdvReceiptEntryTO.getBonusTransId());
                    map.put("BANK_ADVANCE_PENAL_TRANS_ID", mdsBankAdvReceiptEntryTO.getPenalTransId());
                    map.put("BANK_ADVANCE_NOTICE_TRANS_ID", mdsBankAdvReceiptEntryTO.getNoticeId());
                    map.put("BANK_ADVANCE_ARBITRATION_TRANS_ID", mdsBankAdvReceiptEntryTO.getArbitrationId());
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

    private void setTransDetails(GDSClosedReciptTO mdsReceiptEntryTO) throws Exception {
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
        System.out.println("@@@ transIdMap:" + transIdMap + " / @@@ batchId:" + batchId);
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
            if (cashList != null && cashList.size() > 0) {
                if ((mapKey.equals("NET") || mapKey.equals("BONUS")) && returnMap.containsKey(mapKey + "_CASH_DATA")) {
                    ArrayList oldTransDetails = (ArrayList) returnMap.get(mapKey + "_CASH_DATA");
                    cashList.addAll(oldTransDetails);
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
    
    private ArrayList commonApplicationList(HashMap applnMap)throws Exception{
        System.out.println("applnMap :"+applnMap);
        ArrayList cashList = new ArrayList();
        if (mdsReceiptEntryTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
            CashTransactionTO objCashTO = new CashTransactionTO();
            objCashTO.setTransId("");
            objCashTO.setProdType("GL");
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
            objCashTO.setParticulars(applnMap.get("PARTICULARS") + ":" + applnMap.get("MP_MDS_CODE") + "-" + mdsReceiptEntryTO.getChittalNo() + "_" + mdsReceiptEntryTO.getSubNo());
//            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            objCashTO.setCommand(mdsReceiptEntryTO.getCommand());
            objCashTO.setTransAllId(CommonUtil.convertObjToStr(applnMap.get("TRANS_ALL_ID")));
            objCashTO.setAcHdId(CommonUtil.convertObjToStr(applnMap.get("ACC_HEAD")));
            objCashTO.setInpAmount(new Double(CommonUtil.convertObjToDouble(applnMap.get("AMOUNT")).doubleValue()));
            objCashTO.setAmount(new Double(CommonUtil.convertObjToDouble(applnMap.get("AMOUNT")).doubleValue()));
            objCashTO.setTransModType(CommonUtil.convertObjToStr(applnMap.get("TRANS_MOD_TYPE")));
            objCashTO.setSingleTransId(generateSingleTransId);
            objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(mdsReceiptEntryTO.getChittalNo()));
            objCashTO.setIbrHierarchy(CommonUtil.convertObjToStr(ibrHierarchy++));
            if (applnMap.containsKey(CommonConstants.SCREEN) && applnMap.get(CommonConstants.SCREEN) != null) {
                objCashTO.setScreenName((String) applnMap.get(CommonConstants.SCREEN));
            }
            System.out.println("objCashTO 1st one:" + objCashTO);
            cashList.add(objCashTO);
            objCashTO = null;
        }
        return cashList;
    }
    
    private CashTransactionTO createCashTransactionTO(HashMap applnMap) throws Exception{ //Case Expense Transaction
        CashTransactionTO objCashTO = new CashTransactionTO();
        if (mdsReceiptEntryTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
            objCashTO.setTransId("");
            objCashTO.setProdType("GL");
            objCashTO.setTransType(CommonConstants.CREDIT);
            objCashTO.setInitTransId(CommonUtil.convertObjToStr(applnMap.get("USER_ID")));
            objCashTO.setBranchId(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
            objCashTO.setStatusBy(CommonUtil.convertObjToStr(applnMap.get("USER_ID")));
            objCashTO.setStatusDt(mdsReceiptEntryTO.getStatusDt());
            objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
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
            objCashTO.setIbrHierarchy(CommonUtil.convertObjToStr(ibrHierarchy++));
            objCashTO.setSingleTransId(generateSingleTransId);           
            objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(applnMap.get("GL_TRANS_ACT_NUM")));
             if (applnMap.containsKey(CommonConstants.SCREEN) && applnMap.get(CommonConstants.SCREEN) != null) {
                objCashTO.setScreenName((String) applnMap.get(CommonConstants.SCREEN));
            }
            System.out.println("objCashTO 1st one:" + objCashTO);
        }
        return objCashTO;
    }

    public void commonTransactionCashandTransfer(HashMap map, GDSClosedReciptTO mdsCurrentReceiptEntryTO, HashMap debitMap, HashMap applicationMap, int schemeCount,List allApplnDetails) throws Exception {
        String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        System.out.println("mdsCurrentReceiptEntryTO####" + mdsCurrentReceiptEntryTO + map);
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


        for (int i = 0; i < allApplnDetails.size(); i++) {
            GDSApplicationTO objGDSApplicationTO = (GDSApplicationTO) allApplnDetails.get(i);
            HashMap schemeMap = new HashMap();
            schemeMap.put("SCHEME_NAME", objGDSApplicationTO.getSchemeName());
            List headlst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", schemeMap);
            if (headlst != null && headlst.size() > 0) {
                applicationMap = (HashMap) headlst.get(0);
            }
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
                    txMap.put(TransferTrans.PARTICULARS, "Bonus : " + map.get("FROM_PARTICULARS_TRANSALL"));
                } else {
                    txMap.put(TransferTrans.PARTICULARS, "Bonus :" + applicationMap.get("MP_MDS_CODE") + "-" + objGDSApplicationTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
                }
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");//           
                txMap.put("TRANS_MOD_TYPE", "MDS");
                transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getBonusAmtPayable()/schemeCount).doubleValue());
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransType(CommonConstants.DEBIT);
                transferTo.setProdType(TransactionFactory.GL);
                transferTo.setTransDt(currDt);
                transferTo.setBranchId(mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);
                transferTo.setInitiatedBranch(mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
                transactionTO.setChequeNo("SERVICE_TAX");
                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));
                transferTo.setSingleTransId(generateSingleTransId);
                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                TxTransferTO.add(transferTo);               
            }
//       
            if (CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getDiscountAmt()).doubleValue() > 0) {
                System.out.println("Discount Started");
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("DISCOUNT_HEAD"));
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.DR_BRANCH, mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);

                if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {                    
                    txMap.put(TransferTrans.PARTICULARS, "Discount : " + map.get("FROM_PARTICULARS_TRANSALL"));
                } else {
                    txMap.put(TransferTrans.PARTICULARS, "Discount :" + applicationMap.get("MP_MDS_CODE") + "-" + objGDSApplicationTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
                }
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                txMap.put("TRANS_MOD_TYPE", "MDS");
                transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getDiscountAmt()/schemeCount).doubleValue());
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransType(CommonConstants.DEBIT);
                transferTo.setProdType(TransactionFactory.GL);
                transferTo.setTransDt(currDt);
                transferTo.setBranchId(mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);
                transferTo.setInitiatedBranch(BRANCH_ID);
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
                transactionTO.setChequeNo("SERVICE_TAX");
                transferTo.setSingleTransId(generateSingleTransId);
                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));
                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                TxTransferTO.add(transferTo);                
            }
            double receivingGLAmt = CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getDiscountAmt()).doubleValue()
                    + CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getBonusAmtPayable()).doubleValue();
            
            if (receivingGLAmt > 0) {
                System.out.println("GL Final Transaction Started");
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");               
                if (map.containsKey("MDS_CLOSURE")) {
                    txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("MDS_RECEIVABLE_HEAD"));
                } else {
                    txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
                }
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.CR_BRANCH, mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);

                if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {                    
                    txMap.put(TransferTrans.PARTICULARS, map.get("FROM_PARTICULARS_TRANSALL"));
                } else {
                    txMap.put(TransferTrans.PARTICULARS, applicationMap.get("MP_MDS_CODE") + "-" + objGDSApplicationTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
                }
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                txMap.put("TRANS_MOD_TYPE", "MDS");
                transferTo = transactionDAO.addTransferCreditLocal(txMap, receivingGLAmt/schemeCount);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransType(CommonConstants.CREDIT);
                transferTo.setProdType(TransactionFactory.GL);
                transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                if (map.containsKey("MDS_CLOSURE")) {
                    transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("MDS_RECEIVABLE_HEAD")));
                } else {
                    transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("RECEIPT_HEAD")));
                }
                transferTo.setTransDt(currDt);
                transferTo.setBranchId(mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);
                transferTo.setInitiatedBranch(mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
                transactionTO.setChequeNo("SERVICE_TAX");
                transferTo.setSingleTransId(generateSingleTransId);
                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));
                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                TxTransferTO.add(transferTo);
            }
        }
            HashMap applnMap = new HashMap();
            map.put("MODE", map.get("COMMAND"));
            map.put("COMMAND", map.get("MODE"));
            map.put("TxTransferTO", TxTransferTO);            
            HashMap transMap = transferDAO.execute(map, false);
            
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
           
            sqlMap.executeUpdate("updateLinkBatchIdTransferMDS", linkBatchMap);
            linkBatchMap = null;
            transMap = null;
    }
    
    
    public void commonTransactionCashandTransfer1(HashMap map, GDSClosedReciptTO mdsCurrentReceiptEntryTO, HashMap debitMap, HashMap applicationMap) throws Exception {
        String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        System.out.println("mdsCurrentReceiptEntryTO####" + mdsCurrentReceiptEntryTO + map);
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
    //            System.out.println("map.get=0000====" + map.get("FROM_PARTICULARS_TRANSALL"));
                txMap.put(TransferTrans.PARTICULARS, "Bonus : " + map.get("FROM_PARTICULARS_TRANSALL"));
            } else {
                txMap.put(TransferTrans.PARTICULARS, "Bonus :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsCurrentReceiptEntryTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
            }
            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
//            System.out.println("txMap : " + txMap+"serviceAmt :"+mdsCurrentReceiptEntryTO.getBonusAmtPayable());
            txMap.put("TRANS_MOD_TYPE", "MDS");
            transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getBonusAmtPayable()).doubleValue());
            transferTo.setTransId("-");
            transferTo.setBatchId("-");
            transferTo.setTransType(CommonConstants.DEBIT);
            transferTo.setProdType(TransactionFactory.GL);
            transferTo.setTransDt(currDt);
            transferTo.setBranchId(mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);
            transferTo.setInitiatedBranch(mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);
            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
            transactionTO.setChequeNo("SERVICE_TAX");
            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
            transferTo.setSingleTransId(generateSingleTransId);
            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
            TxTransferTO.add(transferTo);
          //  System.out.println("transferTo List 6 : " + transferTo);
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
            //    System.out.println("map.get=eeeee====" + map.get("FROM_PARTICULARS_TRANSALL"));
                txMap.put(TransferTrans.PARTICULARS, "Discount : " + map.get("FROM_PARTICULARS_TRANSALL"));
            } else {
                txMap.put(TransferTrans.PARTICULARS, "Discount :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsCurrentReceiptEntryTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
            }
            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
//            System.out.println("txMap : " + txMap+"serviceAmt :"+mdsCurrentReceiptEntryTO.getDiscountAmt());
            txMap.put("TRANS_MOD_TYPE", "MDS");
            transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getDiscountAmt()).doubleValue());
            transferTo.setTransId("-");
            transferTo.setBatchId("-");
            transferTo.setTransType(CommonConstants.DEBIT);
            transferTo.setProdType(TransactionFactory.GL);
            transferTo.setTransDt(currDt);
            transferTo.setBranchId(mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);
            transferTo.setInitiatedBranch(BRANCH_ID);
            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
            transactionTO.setChequeNo("SERVICE_TAX");
            transferTo.setSingleTransId(generateSingleTransId);
            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
            TxTransferTO.add(transferTo);
           // System.out.println("transferTo List 7 : " + transferTo);
        }
        double receivingGLAmt = CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getDiscountAmt()).doubleValue()
                + CommonUtil.convertObjToDouble(mdsCurrentReceiptEntryTO.getBonusAmtPayable()).doubleValue();
        //System.out.println("transferTo List 7 : " + TxTransferTO + "receivingGLAmt : " + receivingGLAmt);
        if (receivingGLAmt > 0) {
            System.out.println("GL Final Transaction Started");
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
            txMap.put(TransferTrans.CR_BRANCH, mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);

            if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
          //      System.out.println("map.get=ddddd====" + map.get("FROM_PARTICULARS_TRANSALL"));
                txMap.put(TransferTrans.PARTICULARS, map.get("FROM_PARTICULARS_TRANSALL"));
            } else {
                txMap.put(TransferTrans.PARTICULARS, applicationMap.get("MP_MDS_CODE") + "-" + mdsCurrentReceiptEntryTO.getChittalNo() + "_" + mdsCurrentReceiptEntryTO.getSubNo());
            }
            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
//            System.out.println("txMap : " + txMap+"serviceAmt :"+receivingGLAmt);
            txMap.put("TRANS_MOD_TYPE", "MDS");
            transferTo = transactionDAO.addTransferCreditLocal(txMap, receivingGLAmt);
            transferTo.setTransId("-");
            transferTo.setBatchId("-");
            transferTo.setTransType(CommonConstants.CREDIT);
            transferTo.setProdType(TransactionFactory.GL);
            transferTo.setTransAllId(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
            if (map.containsKey("MDS_CLOSURE")) {
                transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("MDS_RECEIVABLE_HEAD")));
            } else {
                transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("RECEIPT_HEAD")));
            }
            transferTo.setTransDt(currDt);
            transferTo.setBranchId(mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);
            transferTo.setInitiatedBranch(mdsCurrentReceiptEntryTO.getBranchCode());//BRANCH_ID);
            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
            transactionTO.setChequeNo("SERVICE_TAX");
            transferTo.setSingleTransId(generateSingleTransId);
            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(mdsCurrentReceiptEntryTO.getChittalNo()));
            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
            TxTransferTO.add(transferTo);
//            System.out.println("transferTo List 8 : " + transferTo);
            //System.out.println("transferTo List 8 : " + TxTransferTO);
            HashMap applnMap = new HashMap();
            map.put("MODE", map.get("COMMAND"));
            map.put("COMMAND", map.get("MODE"));
            map.put("TxTransferTO", TxTransferTO);
            //System.out.println("transferDAO List Last : " + map);
            HashMap transMap = transferDAO.execute(map, false);
            //System.out.println("transferDAO List transMap : " + transMap);
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
            // linkBatchMap.put("INITIATED_BRANCH",_branchCode);
            sqlMap.executeUpdate("updateLinkBatchIdTransferMDS", linkBatchMap);
            linkBatchMap = null;
            transMap = null;
        }
    }


    public void commonTransactionCashandTransferBankAdv(HashMap map, MDSClosedReciptTO mdsBankAdvReceiptEntryTO, HashMap debitMap, HashMap applicationMap) throws Exception {
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
//            System.out.println("txMap : " + txMap+"serviceAmt :"+futureAdvbonusAmt);
            txMap.put("TRANS_MOD_TYPE", "MDS");
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
            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));
            transactionTO.setChequeNo("SERVICE_TAX");
            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));
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
//            System.out.println("txMap : " + txMap+"serviceAmt :"+receivingGLAmt);
            txMap.put("TRANS_MOD_TYPE", "MDS");
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
            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));
            transactionTO.setChequeNo("SERVICE_TAX");
            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(mdsBankAdvReceiptEntryTO.getChittalNo()));
            TxTransferTO.add(transferTo);

//            System.out.println("transferTo List 8 : " + TxTransferTO);
            HashMap applnMap = new HashMap();
            map.put("MODE", map.get("COMMAND"));
            map.put("COMMAND", map.get("MODE"));
            map.put("TxTransferTO", TxTransferTO);
            map.put("TRANS_MOD_TYPE_MMBS", CommonConstants.MMBS);
//            System.out.println("transferDAO List Last : " + map);
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
            mdsReceiptEntryTO.setStatusDt(currDt);
            System.out.println("mdsReceiptEntryTO :" + mdsReceiptEntryTO);
            sqlMap.executeUpdate("deleteReceiptEntryTO", mdsReceiptEntryTO);
        }
    }

    private void authorize(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        System.out.println("AuthorizeMap :" + map);
        HashMap authorizeMap = new HashMap();
        HashMap authMap = new HashMap();
        HashMap instMap = new HashMap();
        SmsConfigDAO smsDAO = new SmsConfigDAO();
        authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
        authMap = (HashMap) map.get("AUTHORIZEMAP");
        String authorizeStatus = CommonUtil.convertObjToStr(authorizeMap.get("AUTHORIZESTATUS"));
        String transId = CommonUtil.convertObjToStr(authorizeMap.get("NET_TRANS_ID"));
        System.out.println("authorizeee Statusss" + authorizeStatus);
        if (map.containsKey("TransactionTO")) {
            HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
            TransactionTO transactionTO = new TransactionTO();
            LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
            if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
            }
            transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
            HashMap serviceAuthMap = new HashMap();
            if (transactionTO.getTransType().equals("TRANSFER") || transactionTO.getTransType().equals("CASH")) {
                authorizeMap.put(CommonConstants.TRANS_ID, authorizeMap.get("NET_TRANS_ID"));
                authorizeMap.put("TRANS_DT", currDt);
                authorizeMap.put("INITIATED_BRANCH", _branchCode);
                List lst = (List) sqlMap.executeQueryForList("getSelectParticularTransList", authorizeMap);
                HashMap transactionMap = new HashMap();
                if (lst != null && lst.size() > 0) {
                    transactionMap = (HashMap) lst.get(0);
                }
                if (transactionMap != null && transactionMap.get("NET_TRANS_ID") != null && !transactionMap.get("NET_TRANS_ID").equals("")) {
                    System.out.println("TRANSFER NET_TRANS_ID :" + transactionMap.get("NET_TRANS_ID"));
                    String linkBatchId = CommonUtil.convertObjToStr(transactionMap.get("NET_TRANS_ID"));
                    HashMap cashAuthMap = new HashMap();
                    cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                    cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
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
                    System.out.println("#$#$ Net Amount transList:" + transList);
                    for (int i = 0; i < transList.size(); i++) {
                        linkBatchId = CommonUtil.convertObjToStr(transList.get(i));
                        serviceAuthMap.put("TRANS_ID", linkBatchId);
                        System.out.println(" cashAuthMap :" + cashAuthMap + "linkBatchId :" + linkBatchId + "authorizeStatus :" + authorizeStatus);
                        if (transactionTO.getTransType().equals("TRANSFER")) {
                        TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
                        } 
                    }
                    System.out.println("transactionTO.getTransType() :" + transactionTO.getTransType());
                    cashAuthMap = null;
                }
                if (transactionTO.getTransType().equals("CASH")) {
                    if (transactionMap != null && transactionMap.get("NET_TRANS_ID") != null && !transactionMap.get("NET_TRANS_ID").equals("")) {
                        //Changed By Suresh
                        CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                        ArrayList arrList = new ArrayList();
                        authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
                        HashMap singleAuthorizeMap = new HashMap();
                        singleAuthorizeMap.put("STATUS", authorizeStatus);
                        singleAuthorizeMap.put("TRANS_ID", transactionMap.get("NET_TRANS_ID"));
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
                        System.out.println("before entering DAO map :" + map);
                        cashTransactionDAO.execute(map, false);
                        cashTransactionDAO = null;
                        dataMap = null;
                    }
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
                        map.put("SCREEN_NAME", "MDS Receipts");
                        HashMap dataMap = new HashMap();
                        dataMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                        dataMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                        dataMap.put("DAILY", "DAILY");
                        map.put(CommonConstants.AUTHORIZEMAP, dataMap);
                        System.out.println("before entering DAO map :" + map);
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
                        map.put("SCREEN_NAME", "MDS Receipts");
                        HashMap dataMap = new HashMap();
                        dataMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                        dataMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                        dataMap.put("DAILY", "DAILY");
                        map.put(CommonConstants.AUTHORIZEMAP, dataMap);
                        cashTransactionDAO.execute(map, false);
                        cashTransactionDAO = null;
                        dataMap = null;
                    }
                    if (transactionMap != null && transactionMap.get("PENAL_TRANS_ID") != null && !transactionMap.get("PENAL_TRANS_ID").equals("")) {
                        //Changed By Suresh
                        CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                        ArrayList arrList = new ArrayList();
                        authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
                        HashMap singleAuthorizeMap = new HashMap();
                        singleAuthorizeMap.put("STATUS", authorizeStatus);
                        singleAuthorizeMap.put("TRANS_ID", transactionMap.get("PENAL_TRANS_ID"));
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
                        System.out.println("before entering DAO map :" + map);
                        cashTransactionDAO.execute(map, false);
                        cashTransactionDAO = null;
                        dataMap = null;
                    }
                }
                //                if (transactionTO.getTransType().equals("TRANSFER")){
                if (transactionMap != null && transactionMap.get("BONUS_TRANS_ID") != null && !transactionMap.get("BONUS_TRANS_ID").equals("")) {
                    System.out.println("TRANSFER BONUS_TRANS_ID :" + transactionMap.get("BONUS_TRANS_ID"));
                    serviceAuthMap = new HashMap();
                    String linkBatchId = CommonUtil.convertObjToStr(transactionMap.get("BONUS_TRANS_ID"));
                    HashMap cashAuthMap = new HashMap();
                    //cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                    cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
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
                    System.out.println("#$#$ Bonus transList:" + transList);
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
                    cashAuthMap.put(CommonConstants.BRANCH_ID, initBrId);// map.get(CommonConstants.BRANCH_ID));
                    if (initBrId != null && !initBrId.equals("")) {
                        for (int i = 0; i < transList.size(); i++) {
                            linkBatchId = CommonUtil.convertObjToStr(transList.get(i));
                            serviceAuthMap.put("TRANS_ID", linkBatchId);
                            System.out.println(" cashAuthMap :" + cashAuthMap + "linkBatchId :" + linkBatchId + "authorizeStatus :" + authorizeStatus);
                            TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
                        }
                    }
                    System.out.println("transactionTO.getTransType() :" + transactionTO.getTransType());
                    cashAuthMap = null;
                }
                if (transactionMap != null && transactionMap.get("DISCOUNT_TRANS_ID") != null && !transactionMap.get("DISCOUNT_TRANS_ID").equals("")) {
                    System.out.println("TRANSFER DISCOUNT_TRANS_ID :" + transactionMap.get("DISCOUNT_TRANS_ID"));
                    //                        serviceAuthMap = new HashMap();
                    serviceAuthMap.put("AUTHORIZE_STATUS", authorizeStatus);
                    serviceAuthMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                    serviceAuthMap.put("TRANS_ID", transactionMap.get("DISCOUNT_TRANS_ID"));
                    String linkBatchId = CommonUtil.convertObjToStr(transactionMap.get("DISCOUNT_TRANS_ID"));
                    HashMap cashAuthMap = new HashMap();
                    cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                    cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                    transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                    System.out.println(" cashAuthMap :" + cashAuthMap + "linkBatchId :" + linkBatchId + "authorizeStatus :" + authorizeStatus);
                    TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
                    System.out.println("transactionTO.getTransType() :" + transactionTO.getTransType());
                    cashAuthMap = null;
                }

                if (transactionMap != null && transactionMap.get("PENAL_TRANS_ID") != null && !transactionMap.get("PENAL_TRANS_ID").equals("")) {
                    System.out.println("TRANSFER PENAL_TRANS_ID :" + transactionMap.get("PENAL_TRANS_ID"));
                    serviceAuthMap = new HashMap();
                    serviceAuthMap.put("AUTHORIZE_STATUS", authorizeStatus);
                    serviceAuthMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                    serviceAuthMap.put("TRANS_ID", transactionMap.get("PENAL_TRANS_ID"));
                    String linkBatchId = CommonUtil.convertObjToStr(transactionMap.get("PENAL_TRANS_ID"));
                    HashMap cashAuthMap = new HashMap();
                    cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                    cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                    transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                    System.out.println(" cashAuthMap :" + cashAuthMap + "linkBatchId :" + linkBatchId + "authorizeStatus :" + authorizeStatus);
                    if (transactionTO.getTransType().equals("TRANSFER")) {
                    TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
                    }
                    System.out.println("transactionTO.getTransType() :" + transactionTO.getTransType());
                    cashAuthMap = null;
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
                    System.out.println("#### auth Map : " + authMap);
                    transactionaApplnMap.put("TRANS_ID", transactionMap.get("NET_TRANS_ID"));
                    System.out.println("############" + transactionaApplnMap);
                    List allApplnDetails = sqlMap.executeQueryForList("getAllApplcationDetailsForGDS", map);     
                    for (int i = 0; i < allApplnDetails.size(); i++) {
                        GDSApplicationTO objGDSApplicationTO = (GDSApplicationTO) allApplnDetails.get(i);
                        transactionaApplnMap.put("SCHEME_NAME", objGDSApplicationTO.getSchemeName());
                        sqlMap.executeUpdate("updateMDSShadowCreditCrAmtMap", transactionaApplnMap);
                        sqlMap.executeUpdate("updateMDSShadowCreditDrAmtMap", transactionaApplnMap);
                        sqlMap.executeUpdate("updateMDSAvailBalanceMap", transactionaApplnMap);
                        sqlMap.executeUpdate("updateMDSClearBalanceMap", transactionaApplnMap);
                        sqlMap.executeUpdate("updateMDSTotalBalanceMap", transactionaApplnMap);
                    }
                    
                        if (isSplitMDSTransaction != null && isSplitMDSTransaction.equals("Y")) {
                        List lsts = null;
                        if (map != null && map.containsKey("WHOLE_SPLIT_MAP") && map.get("WHOLE_SPLIT_MAP") != null) {
                            HashMap maps = (HashMap) map.get("WHOLE_SPLIT_MAP");
                            if(maps.containsKey("MDSReceiptEntryTO") && maps.get("MDSReceiptEntryTO") != null){
                            lsts =  (List) maps.get("MDSReceiptEntryTO");
                            }
                        }
                    if(lsts != null && lsts.size() > 0){    
                    for (int i = 0; i < lsts.size(); i++) {
                    MDSClosedReciptTO MDSTo = (MDSClosedReciptTO)lsts.get(i);    
                    HashMap mdsTransMap = new HashMap();
                    mdsTransMap.put("SCHEME_NAME", mdsReceiptEntryTO.getSchemeName());
                    mdsTransMap.put("CHITTAL_NO", mdsReceiptEntryTO.getChittalNo());
                    mdsTransMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getSubNo()));
                    mdsTransMap.put("NO_OF_INST",CommonUtil.convertObjToInt(1));
                    mdsTransMap.put("INST_AMT", MDSTo.getInstAmt());
                    mdsTransMap.put("PENAL_AMT", MDSTo.getPenalAmtPayable());
                    mdsTransMap.put("BONUS_AMT", MDSTo.getBonusAmtPayable());
                    mdsTransMap.put("DISCOUNT_AMT", MDSTo.getDiscountAmt());
                    mdsTransMap.put("MDS_INTEREST", mdsReceiptEntryTO.getMdsInterset());
                    double noticeAmount = CommonUtil.convertObjToDouble(MDSTo.getNoticeAmt());//9535
                    double arbitrationAmount = CommonUtil.convertObjToDouble(MDSTo.getArbitrationAmt());//9535
                    double setvideTaxAmount = CommonUtil.convertObjToDouble(MDSTo.getServiceTaxAmt());//9535
                    mdsTransMap.put("NOTICE_AMT", CommonUtil.convertObjToDouble(MDSTo.getNoticeAmt()));
                    mdsTransMap.put("ARBITRATION_AMT", CommonUtil.convertObjToDouble(MDSTo.getArbitrationAmt()));
                    mdsTransMap.put("SERVICE_TAX_AMT", CommonUtil.convertObjToDouble(MDSTo.getServiceTaxAmt()));
                    mdsTransMap.put("NARRATION", CommonUtil.convertObjToStr(MDSTo.getNarration()));
                    mdsReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(MDSTo.getNetAmt()) - noticeAmount -arbitrationAmount - setvideTaxAmount);
                    
                    mdsTransMap.put("NET_AMT", CommonUtil.convertObjToDouble(MDSTo.getNetAmt()));

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
                    if (!authorizeStatus.equals("REJECTED")) {
                        sqlMap.executeUpdate("updateMDSTransDetailsEachRec", mdsTransMap);
                    }
                    }
                    }
                }else{
                        int schemeCount = allApplnDetails.size();
                        String isBonusTrans = "Y";
                        String commonSchemeName = "";
                        for (int i = 0; i < allApplnDetails.size(); i++) {
                            GDSApplicationTO objGDSApplicationTO = (GDSApplicationTO) allApplnDetails.get(i);
                            if(i == 0){
                                commonSchemeName = objGDSApplicationTO.getSchemeName();
                                HashMap checkMap = new HashMap();
                                checkMap.put("SCHEME_NAME",commonSchemeName);
                                List list = sqlMap.executeQueryForList("getifBonusTransactionRequired", checkMap);
                                if(list != null && list.size() > 0){
                                    checkMap = (HashMap)list.get(0);
                                    if(checkMap.containsKey("IS_BONUS_TRANSFER") && checkMap.get("IS_BONUS_TRANSFER") != null && !"".equalsIgnoreCase(CommonUtil.convertObjToStr(checkMap.get("IS_BONUS_TRANSFER")))){
                                        isBonusTrans = CommonUtil.convertObjToStr(checkMap.get("IS_BONUS_TRANSFER"));
                                    }
                                }
                            }
                            HashMap mdsTransMap = new HashMap();
                            mdsTransMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getSubNo()));
                            mdsTransMap.put("NO_OF_INST", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getNoOfInstPay()));
                            mdsTransMap.put("INST_AMT", mdsReceiptEntryTO.getInstAmt() / schemeCount);
                            mdsTransMap.put("PENAL_AMT", mdsReceiptEntryTO.getPenalAmtPayable() / schemeCount);
                            if (isBonusTrans.equalsIgnoreCase("N")) { // Modified by nithya on 16-01-2020 for KD-1280
//                                mdsTransMap.put("BONUS_AMT",0);
//                                mdsTransMap.put("DISCOUNT_AMT",0);
                                  mdsTransMap.put("BONUS_AMT",new Double(0.0));
                                  mdsTransMap.put("DISCOUNT_AMT",new Double(0.0));
                            } else {
                                mdsTransMap.put("BONUS_AMT", mdsReceiptEntryTO.getBonusAmtPayable() / schemeCount);
                                mdsTransMap.put("DISCOUNT_AMT", mdsReceiptEntryTO.getDiscountAmt() / schemeCount);
                            }
                            mdsTransMap.put("MDS_INTEREST", mdsReceiptEntryTO.getMdsInterset() / schemeCount);
                            mdsTransMap.put("NOTICE_AMT", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNoticeAmt() / schemeCount));
                            mdsTransMap.put("ARBITRATION_AMT", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getArbitrationAmt() / schemeCount));
                            mdsTransMap.put("SERVICE_TAX_AMT", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getServiceTaxAmt() / schemeCount));
                            mdsTransMap.put("NARRATION", CommonUtil.convertObjToStr(mdsReceiptEntryTO.getNarration()));
                            double noticeAmount = CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNoticeAmt());//9535
                            System.out.println("setNetAmtsetNetAmt" + mdsReceiptEntryTO.getNetAmt());
                            mdsReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNetAmt()) - noticeAmount);
                            System.out.println("noticeAmountnoticeAmount" + noticeAmount + "  " + mdsReceiptEntryTO.getNetAmt());
                            mdsTransMap.put("NET_AMT", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNetAmt() / schemeCount));

                            mdsTransMap.put("STATUS", CommonConstants.STATUS_CREATED);
                            mdsTransMap.put("STATUS_DT", setProperDtFormat(currDt));
                            mdsTransMap.put("STATUS_BY", map.get("USER_ID"));
                            mdsTransMap.put("AUTHORIZE_STATUS", authorizeStatus);
                            mdsTransMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                            mdsTransMap.put("AUTHORIZE_DT", setProperDtFormat(currDt));
                            mdsTransMap.put("TRANS_DT", setProperDtFormat(currDt));
                            mdsTransMap.put("GDS_NO", mdsReceiptEntryTO.getGds_no());
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
                            mdsTransMap.put("SCHEME_NAME", objGDSApplicationTO.getSchemeName());
                            mdsTransMap.put("CHITTAL_NO", objGDSApplicationTO.getChittalNo());
                            if (transactionMap != null && transactionMap.containsKey("NET_TRANS_ID") && transactionMap.get("NET_TRANS_ID") != null && !transactionMap.get("NET_TRANS_ID").equals("")) {
                            if (!authorizeStatus.equals("REJECTED")) {
                                sqlMap.executeUpdate("updateGDSTransDetailsEachRec", mdsTransMap);
                                }
                            }
                        }
                    }
                }
                serviceAuthMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                serviceAuthMap.put("AUTHORIZE_DT", currDt);
                serviceAuthMap.put("TRANS_DT", currDt);
                serviceAuthMap.put("INITIATED_BRANCH", _branchCode);
                System.out.println("############ serviceAuthMap " + serviceAuthMap);
                sqlMap.executeUpdate("updateAuthorizeTransaction", serviceAuthMap);
                serviceAuthMap = null;
                HashMap authorizeReceiptMap = new HashMap();
                authorizeReceiptMap.put("TRANS_DT", currDt);
                authorizeReceiptMap.put("AUTHORIZE_STATUS", authorizeStatus);
                authorizeReceiptMap.put("AUTHORIZE_BY", authMap.get("USER_ID"));
//                authorizeReceiptMap.put("NET_TRANS_ID",transactionTO.getBatchId());
                authorizeReceiptMap.put("NET_TRANS_ID", authMap.get("NET_TRANS_ID"));
                authorizeReceiptMap.put(CommonConstants.BRANCH_ID, _branchCode);
                sqlMap.executeUpdate("updateAuthorizeReceiptEntry", authorizeReceiptMap);
                if(map.containsKey("SERVICE_TAX_AUTH")){
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
                whereMap.put("INSTALLMENT_NO", CommonUtil.convertObjToInt(instNo));
                whereMap.put("CHITTAL_NO", mdsReceiptEntryTO.getChittalNo());
                whereMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getSubNo()));
                whereMap.put("AUTH_STATUS", authorizeStatus);
                if (authorizeStatus.equals("REJECTED")) {
                    whereMap.put("REPAID", "N");
                } else {
                    whereMap.put("REPAID", "Y");
                }
                double number = CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNoOfInstPay()).doubleValue();
                System.out.println("number.." + number);
//                List getBankAd = sqlMap.executeQueryForList("getPendingBankAdv", whereMap);
//                if (getBankAd != null && getBankAd.size() > 0) {
//                    int countLength = getBankAd.size();
//                    if (number >= countLength) {
//                        sqlMap.executeUpdate("updateMDSBankAdvanceRepaidStatus", whereMap);
//                    } else {
//                        int k = 0;
//                        for (int i = 0; i < number; i++) {
//                            HashMap aMap = new HashMap();
//                            aMap = (HashMap) getBankAd.get(i);
//                            whereMap.put("BANK_ADV_ID", aMap.get("BANK_ADV_ID").toString());
//                            sqlMap.executeUpdate("updateMDSBankAdvanceRepaidStatusSelected", whereMap);
//                        }
//
//
//
//
//
//                    }
//
//
//
//                }
                
                List getBankAd = sqlMap.executeQueryForList("getGDSPendingBankAdv", whereMap);
                if (getBankAd != null && getBankAd.size() > 0) {
                    int countLength = getBankAd.size();
                    if (number >= countLength) {
                        sqlMap.executeUpdate("updateGDSBankAdvanceRepaidStatus", whereMap);
                    } else {
                        int k = 0;
                        for (int i = 0; i < number; i++) {
                            HashMap aMap = new HashMap();
                            aMap = (HashMap) getBankAd.get(i);
                            whereMap.put("BANK_ADV_ID", aMap.get("BANK_ADV_ID").toString());
                            sqlMap.executeUpdate("updateGDSBankAdvanceRepaidStatusSelected", whereMap);
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
                }
                authorizeMap = null;
                authMap = null;
                map = null;
            }
            //Added by sreekrishnan for sms alert..
            if (authorizeStatus.equals(CommonConstants.STATUS_AUTHORIZED)) {
                HashMap smsAlertMap = new HashMap();
                smsAlertMap.put("SCHEME_NAME", mdsReceiptEntryTO.getSchemeName());
                smsAlertMap.put("CHITTAL_NO", mdsReceiptEntryTO.getChittalNo());
                System.out.println("smsAlertMap%#%#%#%#%#%  " + smsAlertMap);
                List MdsSmsList = sqlMap.executeQueryForList("getMDsDetailsForSMS", smsAlertMap);
                if (MdsSmsList != null && MdsSmsList.size() > 0) {
                    HashMap MdsSmsMap = (HashMap) MdsSmsList.get(0);
                    System.out.println("MdsSmsMap%#%#%#^#^#^#^" + MdsSmsMap);
                    if (MdsSmsMap != null && !MdsSmsMap.equals("")) {
                        MdsSmsMap.put(CommonConstants.TRANS_ID, transId);
                        MdsSmsMap.put("TRANS_DT", currDt);
                        List MdsTransList = sqlMap.executeQueryForList("getMdsTransDetailsForSms", MdsSmsMap);
                        if (MdsTransList != null && MdsTransList.size() > 0) {
                            HashMap MdsTransMap = (HashMap) MdsTransList.get(0);
                            System.out.println("MdsTransMap %#%#%#^#^#^#^" + MdsTransMap);
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

    public HashMap execute(HashMap map) throws Exception {
        return execute(map, true);
    }

    public HashMap execute(HashMap map, boolean isTransaction) throws Exception {
        //    public HashMap execute(HashMap map)  throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        /*
         * To Verify The Account Head data...
         */
        isSplitMDSTransaction = "";
        generateSingleTransId = generateLinkID();
        System.out.println("Map in Receipt Entry DAO : " + map);
        if (map.containsKey("ACCT_HD")) {
            ServerUtil.verifyAccountHead(map);
        }
        if(map.containsKey("IS_SPLIT_MDS_TRANSACTION") && map.get("IS_SPLIT_MDS_TRANSACTION")!=null){
            isSplitMDSTransaction = CommonUtil.convertObjToStr(map.get("IS_SPLIT_MDS_TRANSACTION"));
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
        if (map.containsKey("mdsReceiptEntryTO")) {
            mdsReceiptEntryTO = new GDSClosedReciptTO();
            mdsReceiptEntryTO = (GDSClosedReciptTO) map.get("mdsReceiptEntryTO");
            final String command = mdsReceiptEntryTO.getCommand();
            try {
                if (isTransaction) {
                    sqlMap.startTransaction();
                }
                objLogTO.setStatus(command);
                returnMap = new HashMap();
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertData(map, objLogDAO, objLogTO);
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData(map, objLogDAO, objLogTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData(map, objLogDAO, objLogTO);
                } else if (map.containsKey("AUTHORIZEMAP")) {
                    authorize(map, objLogDAO, objLogTO);
                } else {
                    throw new NoCommandException();
                }
                if (isTransaction) {
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

     private ArrayList commonApplicationListForCashTrans(HashMap map, List allApplnDetails,GDSClosedReciptTO mdsCurrentReceiptEntryTO, String cash_unique_id,double transactionAmount,HashMap cashTransMap) throws Exception {
            System.out.println("applnMap :" + map);
            System.out.println("transactionAmount :: "+transactionAmount);
            ArrayList cashList = new ArrayList();
            if (allApplnDetails != null && allApplnDetails.size() > 0) {
                int schemeCount = allApplnDetails.size();
                transactionAmount = transactionAmount/schemeCount;
                HashMap applnMap = new HashMap();
                for (int i = 0; i < schemeCount; i++) {
                    GDSApplicationTO objGDSApplicationTO = (GDSApplicationTO) allApplnDetails.get(i);
                    HashMap schemeMap = new HashMap();
                    schemeMap.put("SCHEME_NAME", objGDSApplicationTO.getSchemeName());
                    List headlst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", schemeMap);
                    if (headlst != null && headlst.size() > 0) {
                        applnMap = (HashMap) headlst.get(0);
                    }
                    applnMap.put("ACCT_FROM_BRANCH", mdsCurrentReceiptEntryTO.getBranchCode());
                    applnMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                    applnMap.put("MDS_GL_DETAILS", "MDS_GL_DETAILS");
                    applnMap.put("MDS_PENAL_DETAILS", "MDS_PENAL_DETAILS");
                    applnMap.put("MDS_NOTICE_DETAILS", "MDS_NOTICE_DETAILS");
                    applnMap.put("MDS_ARBITRATION_DETAILS", "MDS_ARBITRATION_DETAILS");
                    applnMap.put("LINK_BATCH_ID", mdsCurrentReceiptEntryTO.getChittalNo());
                    applnMap.put("RECEIPT_HEAD", applnMap.get("RECEIPT_HEAD"));
                    applnMap.put("MDS_RECEIVABLE_HEAD", applnMap.get("MDS_RECEIVABLE_HEAD"));
                    applnMap.put("PENAL_INTEREST_HEAD", applnMap.get("PENAL_INTEREST_HEAD"));
                    applnMap.put("NOTICE_CHARGES_HEAD", applnMap.get("NOTICE_CHARGES_HEAD"));
                    applnMap.put("CASE_EXPENSE_HEAD", applnMap.get("CASE_EXPENSE_HEAD"));
                    applnMap.put("SCHEME_NAME", mdsCurrentReceiptEntryTO.getSchemeName());
                    applnMap.put("USER_ID", CommonUtil.convertObjToStr(map.get("USER_ID")));
                    applnMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                    applnMap.put("AUTHORIZE_REMARKS", cash_unique_id);
                    applnMap.put("TRANS_ALL_ID", CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")));
                    applnMap.put("TRANS_MOD_TYPE", "MDS");
                    CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                    if (map.containsKey("MDS_CLOSURE")) {
                        applnMap.put("ACC_HEAD", applnMap.get("MDS_RECEIVABLE_HEAD"));
                    } else {
                        applnMap.put("ACC_HEAD", applnMap.get("RECEIPT_HEAD"));
                    }
                    if (map.containsKey("FROM_PARTICULARS_TRANSALL")) {
                        applnMap.put(TransferTrans.PARTICULARS, map.get("FROM_PARTICULARS_TRANSALL"));
                    } else {
                        applnMap.put("PARTICULARS", "Installment Amt ");
                    }                    
                    applnMap.put("AMOUNT", String.valueOf(transactionAmount));
                    applnMap.put("MP_MDS_CODE", applnMap.get("MP_MDS_CODE"));
                    applnMap.put("TRANS_MOD_TYPE", "MDS");
                    applnMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                    if(CommonUtil.convertObjToStr(cashTransMap.get("GDS_TRANS_FOR")).equalsIgnoreCase("INSTALLMENT")){
                        cashTransMap.put("ACC_HEAD",applnMap.get("BANKING_REP_PAY_HEAD"));
                    }
                    if(CommonUtil.convertObjToStr(cashTransMap.get("GDS_TRANS_FOR")).equalsIgnoreCase("PENAL")){
                        cashTransMap.put("ACC_HEAD",applnMap.get("PENAL_INTEREST_HEAD"));
                    }
                    if(CommonUtil.convertObjToStr(cashTransMap.get("GDS_TRANS_FOR")).equalsIgnoreCase("CHARGES")){
                        cashTransMap.put("ACC_HEAD",applnMap.get("NOTICE_CHARGES_HEAD"));
                    }
                    if(CommonUtil.convertObjToStr(cashTransMap.get("GDS_TRANS_FOR")).equalsIgnoreCase("ARB_CHARGES")){
                        cashTransMap.put("ACC_HEAD",applnMap.get(cashTransMap.get("PARTICULARS")));
                    }
                    if(CommonUtil.convertObjToStr(cashTransMap.get("GDS_TRANS_FOR")).equalsIgnoreCase("BONUS_REVERSAL")){
                       if(map.containsKey("MDS_CLOSURE")){
                           cashTransMap.put("ACC_HEAD",applnMap.get("SUNDRY_RECEIPT_HEAD"));
                       }else{
                           cashTransMap.put("ACC_HEAD",applnMap.get("BONUS_PAYABLE_HEAD"));
                       }
                    }
                    if(CommonUtil.convertObjToStr(cashTransMap.get("GDS_TRANS_FOR")).equalsIgnoreCase("INSTALLMENT_NORMAL")){
                       if (map.containsKey("MDS_CLOSURE")) {
                            cashTransMap.put("ACC_HEAD", applnMap.get("MDS_RECEIVABLE_HEAD"));
                        } else {
                            cashTransMap.put("ACC_HEAD", applnMap.get("RECEIPT_HEAD"));
                        }
                    }
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
                        objCashTO.setParticulars(cashTransMap.get("PARTICULARS") + ":" + applnMap.get("MP_MDS_CODE") + "-" + objGDSApplicationTO.getChittalNo() + "_" + mdsReceiptEntryTO.getSubNo());
                        objCashTO.setInitChannType(CommonConstants.CASHIER);
                        objCashTO.setCommand(mdsReceiptEntryTO.getCommand());
                        objCashTO.setTransAllId(CommonUtil.convertObjToStr(applnMap.get("TRANS_ALL_ID")));
                        objCashTO.setAcHdId(CommonUtil.convertObjToStr(cashTransMap.get("ACC_HEAD")));
                        objCashTO.setInpAmount(new Double(CommonUtil.convertObjToDouble(applnMap.get("AMOUNT")).doubleValue()));
                        objCashTO.setAmount(new Double(CommonUtil.convertObjToDouble(applnMap.get("AMOUNT")).doubleValue()));
                        objCashTO.setTransModType(CommonUtil.convertObjToStr(applnMap.get("TRANS_MOD_TYPE")));
                        objCashTO.setSingleTransId(generateSingleTransId);
                        objCashTO.setGlTransActNum(objGDSApplicationTO.getChittalNo());
                        objCashTO.setIbrHierarchy(CommonUtil.convertObjToStr(ibrHierarchy++));
                        if (applnMap.containsKey("SCREEN_NAME") && applnMap.get("SCREEN_NAME") != null) {
                            objCashTO.setScreenName(CommonUtil.convertObjToStr(applnMap.get("SCREEN_NAME")));
                        }
                        System.out.println("objCashTO 1st one:" + objCashTO);
                        cashList.add(objCashTO);
                        objCashTO = null;
                    }
                }
            }
            return cashList;
        }
    
    private void destroyObjects() {
        mdsReceiptEntryTO = null;
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
    //added by chithra for service Tax
    private void insertServiceTaxDetails(ServiceTaxDetailsTO objserviceTaxDetailsTO) {
        try {
            objserviceTaxDetailsTO.setServiceTaxDet_Id(getServiceTaxNo());
            objserviceTaxDetailsTO.setParticulars("MDS Closed Receipt Entry");
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
            GDSClosedReciptDAO dao = new GDSClosedReciptDAO();
            HashMap inputMap = new HashMap();
            //inputMap.put("ACCT_HD", "CCS_D");

            System.out.println(sqlMap.executeQueryForList("OperativeAcctProduct.getSelectAcctHeadTOList", null));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
