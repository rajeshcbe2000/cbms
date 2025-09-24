/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSStandingInstructionDAO.java
 * 
 * Created on Tue Oct 11 13:18:08 IST 2011
 */
package com.see.truetransact.serverside.gdsapplication.gdsStandingInstruction;

import com.see.truetransact.serverside.mdsapplication.*;
import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.sms.SmsConfigDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.gdsapplication.GDSApplicationTO;
import com.see.truetransact.transferobject.gdsapplication.gdsreceiptentry.GDSReceiptEntryTO;
import com.see.truetransact.transferobject.mdsapplication.mdsmastermaintenance.MDSDepositTypeTO;
import com.see.truetransact.transferobject.mdsapplication.mdsreceiptentry.MDSReceiptEntryTO;
import com.see.truetransact.transferobject.product.groupmdsdeposit.GroupMDSDepositTO;
import java.util.*;

/**
 * MDSStandingInstruction DAO.
 *
 */
public class GDSStandingInstructionDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private TransactionDAO transactionDAO = null;
    TransferDAO transferDAO = new TransferDAO();
    public List standingLst = null;
    private Date currDt = null;
    private GDSReceiptEntryTO mdsReceiptEntryTO = null;
    private String schemeName = "";
    private Map returnMap = null;
    private List returnMapList = null;
    private List returnSingleMapList = null;
    private String generateSingleTransId = null;

    ;
    /** Creates a new instance of OperativeAcctProductDAO */
    public GDSStandingInstructionDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public static void main(String str[]) {
        try {
            GDSStandingInstructionDAO dao = new GDSStandingInstructionDAO();
            HashMap inputMap = new HashMap();
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

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        generateSingleTransId = generateLinkID();
        currDt = ServerUtil.getCurrentDate(_branchCode);
        System.out.println("Map in GDSStandingInstruction DAO : " + map);
        if (map.containsKey("GDS_STANDING_INSTRUCTION")) {
            try {
                sqlMap.startTransaction();
                returnMapList = new ArrayList();
                returnSingleMapList = new ArrayList();
                returnMap = new HashMap();
                standingLst = (List) map.get("GDS_STANDING_INSTRUCTION");
                System.out.println("@##$#$% standingLst #### :" + standingLst);
                map.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                executeTransactionPart(map);
                if (returnMapList != null && returnMapList.size() > 0) {
                    returnMap.put("TRANSACTION_DETAILS", returnMapList);
                    returnMap.put("TRANSACTION_DETAILS_SI", returnSingleMapList);
                }
                sqlMap.commitTransaction();
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw e;
            }
            destroyObjects();
        }
        if (map.containsKey("PENDING_LIST")) {
            returnMap = new HashMap();
            List pendingAuthlst = sqlMap.executeQueryForList("checkPendingForAuthorization", map);
            if (pendingAuthlst != null && pendingAuthlst.size() > 0) {
                returnMap.put("PENDING_LIST", pendingAuthlst);
            }
        }
        System.out.println("@#$@@$@@@$ returnMap : " + returnMap);
        return (HashMap) returnMap;
    }

    private void executeTransactionPart(HashMap map) throws Exception {
        try {
            if (standingLst != null && standingLst.size() > 0) {
                String isBonusTrans = "Y";
                schemeName = CommonUtil.convertObjToStr(map.get("SCHEME_NAME"));
                String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
                SmsConfigDAO smsDAO = new SmsConfigDAO();
                HashMap unMarkleanMap = new HashMap();
                HashMap groupMap = new HashMap();
                groupMap.put("GROUP_NO", map.get("GROUP_NO"));
                List commonSchemeDetailsLst = sqlMap.executeQueryForList("getDetailsForOneSchemeInGroup", groupMap);
                HashMap commonSchemeMap = (HashMap) commonSchemeDetailsLst.get(0);
                String commonScheme = CommonUtil.convertObjToStr(commonSchemeMap.get("SCHEME_NAME"));
                HashMap checkMap = new HashMap();
                checkMap.put("SCHEME_NAME", commonScheme);
                List bonusTransLst = sqlMap.executeQueryForList("getifBonusTransactionRequired", checkMap);
                if (bonusTransLst != null && bonusTransLst.size() > 0) {
                    checkMap = (HashMap) bonusTransLst.get(0);
                    if (checkMap.containsKey("IS_BONUS_TRANSFER") && checkMap.get("IS_BONUS_TRANSFER") != null && !"".equalsIgnoreCase(CommonUtil.convertObjToStr(checkMap.get("IS_BONUS_TRANSFER")))) {
                        isBonusTrans = CommonUtil.convertObjToStr(checkMap.get("IS_BONUS_TRANSFER"));
                    }
                }
                
                  //Fetch group name
                String gdsGroupName = "";
                if (map.containsKey("GROUP_NO") && map.get("GROUP_NO") != null) {
                    HashMap groupNameMap = new HashMap();
                    groupNameMap.put("GROUP_NO", CommonUtil.convertObjToStr(map.get("GROUP_NO")));
                    List list = (List) sqlMap.executeQueryForList("getAllSelectGroupMDSDepositTO", groupNameMap);
                    GroupMDSDepositTO objGroupMDSDepositTO = (GroupMDSDepositTO) list.get(0);
                    if (objGroupMDSDepositTO.getGroupName().indexOf(10) != -1) {
                        gdsGroupName = objGroupMDSDepositTO.getGroupName().substring(0, 10);
                    } else {
                        gdsGroupName = objGroupMDSDepositTO.getGroupName();
                    }
                }
                //End
                
                
                HashMap GDSBonusMap = new HashMap();
                GDSBonusMap = (HashMap) map.get("GDS_BONUS_MAP");
                for (int i = 0; i < standingLst.size(); i++) {
                    HashMap dataMap = new HashMap();
                    double instalAmt = 0.0;
                    double bonusAmt = 0.0;
                    double discountAmt = 0.0;
                    double penalAmt = 0.0;
                    double netAmt = 0.0;
                    double bonusNew = 0.0;
                    String chittalNo = "";
                    String subNo = "";
                    String prodType = "";
                    String prodId = "";
                    String debitAccNo = "";
                    double forfeitBonusAmt = 0.0;
                    int noOFinstToPay = 0;
                    int paidInst = 0;
                    HashMap leanFlagMap = new HashMap();
                    boolean leanFlag = false;
                    String transId = "";
                    dataMap = (HashMap) standingLst.get(i);
                    int schemeCount = 0;
                    HashMap applicationMap = new HashMap();
                    applicationMap.put("GDS_NO", dataMap.get("CHITTAL_NO"));
                    List allApplnDetails = sqlMap.executeQueryForList("getAllApplcationDetailsForGDS", applicationMap);
                    if (allApplnDetails != null && allApplnDetails.size() > 0) {
                        schemeCount = allApplnDetails.size();
                        instalAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("INST_AMT")).replaceAll(",", "")).doubleValue();
                        bonusAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("BONUS")).replaceAll(",", "")).doubleValue();
                        bonusNew = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("BONUS_NEW")).replaceAll(",", ""));
                        discountAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("DISCOUNT")).replaceAll(",", "")).doubleValue();
                        penalAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("PENAL")).replaceAll(",", "")).doubleValue();
                        netAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("NET_AMOUNT")).replaceAll(",", "")).doubleValue();
                        chittalNo = CommonUtil.convertObjToStr(dataMap.get("CHITTAL_NO"));
                        subNo = CommonUtil.convertObjToStr(dataMap.get("SUB_NO"));
                        prodType = CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE"));
                        prodId = CommonUtil.convertObjToStr(dataMap.get("PROD_ID"));
                        debitAccNo = CommonUtil.convertObjToStr(dataMap.get("DR_ACT_NO"));
                        if (dataMap.containsKey("FORFEIT_BONUS") && dataMap.get("FORFEIT_BONUS") != null) {
                            forfeitBonusAmt = CommonUtil.convertObjToDouble(dataMap.get("FORFEIT_BONUS"));
                        }
                        // Debit transaction starts
                        ArrayList transferList = new ArrayList();
                        TransferTrans transferTrans = new TransferTrans();
                        TxTransferTO transferTo = new TxTransferTO();
                        ArrayList TxTransferTO = new ArrayList();
                        TransactionTO transactionTO = new TransactionTO();
                        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                        transactionDAO.setInitiatedBranch(_branchCode);
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        HashMap txMap = new HashMap();
                        HashMap transactionListMap = new HashMap();
                        HashMap transMap = new HashMap();
                        transactionTO.setProductType(prodType);
                        transactionTO.setDebitAcctNo(debitAccNo);
                        transactionTO.setProductId(prodId);
                        HashMap debitMap = new HashMap();
                        List lst;
                        if (!prodType.equals("") && (prodType.equals("OA") || prodType.equals("SA") || prodType.equals("AD") || prodType.equals("AB") || prodType.equals("TD"))) {
                            debitMap.put("PROD_ID", prodId);
                            lst = sqlMap.executeQueryForList("getAccountHeadProd" + prodType, debitMap);
                            if (lst != null && lst.size() > 0) {
                                debitMap = (HashMap) lst.get(0);
                            }
                            HashMap debitBranchCodeMap = new HashMap();
                            debitBranchCodeMap.put("ACT_NUM", debitAccNo);
                            List lstdebitBranchCode = sqlMap.executeQueryForList("getSelectInterBranchCode", debitBranchCodeMap);
                            if (lstdebitBranchCode != null && lstdebitBranchCode.size() > 0) {
                                debitBranchCodeMap = (HashMap) lstdebitBranchCode.get(0);
                                transferTo.setBranchId(CommonUtil.convertObjToStr(debitBranchCodeMap.get("BRANCH_CODE")));
                            }
                        }
                        LinkedHashMap instMap = new LinkedHashMap();
                        List instMapLst = new ArrayList();                       
                        int noOfInstPay = CommonUtil.convertObjToInt(dataMap.get("NO_OF_INST_PAY"));
                        int noOfInstPaid = CommonUtil.convertObjToInt(dataMap.get("PAID_INST"));
                        for (int t = 1; t <= noOfInstPay; t++) {
                            System.out.println("t..." + (noOfInstPaid + t));
                            instMap.put(String.valueOf(noOfInstPaid + t), noOfInstPaid + t);
                            instMapLst.add(noOfInstPaid + t);
                        }
                        LinkedHashMap chitalBonusMap = (LinkedHashMap) GDSBonusMap.get(dataMap.get("CHITTAL_NO"));
                        LinkedHashMap finalChitalBonusMap = new LinkedHashMap();
                        System.out.println("chitalBonusMap :: " + chitalBonusMap);
                        if (instMapLst != null && instMapLst.size() > 0) {
                            for (int inst = 0; inst < instMapLst.size(); inst++) {
                                if (chitalBonusMap.get(instMapLst.get(inst)) != null) {
                                    finalChitalBonusMap.put(String.valueOf(instMapLst.get(inst)), chitalBonusMap.get(instMapLst.get(inst)));
                                } else {
                                    finalChitalBonusMap.put(String.valueOf(instMapLst.get(inst)), 0.0);
                                }
                            }
                        }
                        System.out.println("finalChitalBonusMap :: " + finalChitalBonusMap);
                        double bonusN = 0.0;
                        for (int h = 0; h < instMapLst.size(); h++) {
                            System.out.println("h :: " + instMapLst.get(h));
                            bonusN = CommonUtil.convertObjToDouble(finalChitalBonusMap.get(instMapLst.get(h)));
                        }
                        System.out.println("bonusN ::" + bonusN);
                        double totalDebitAmt = 0.0;
                        List advProcessInstList = new ArrayList();
                        for (int count = 0; count < allApplnDetails.size(); count++) { 
                            advProcessInstList = new ArrayList();
                            double bankAdvInsAmt = 0.0;
                            double bankBonusAmt = 0.0;
                            double instalmentPayAmt = 0.0;
                            double futureAdvbonusAmt = 0.0;
                            mdsReceiptEntryTO = new GDSReceiptEntryTO();
                            GDSApplicationTO objGDSApplicationTO = (GDSApplicationTO) allApplnDetails.get(count);
                            HashMap schemeMap = new HashMap();
                            schemeMap.put("SCHEME_NAME", objGDSApplicationTO.getSchemeName());
                            List headlst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", schemeMap);
                            if (headlst != null && headlst.size() > 0) {
                                applicationMap = (HashMap) headlst.get(0);
                            }
                            mdsReceiptEntryTO.setSchemeName(objGDSApplicationTO.getSchemeName());
                            mdsReceiptEntryTO.setChittalNo(objGDSApplicationTO.getChittalNo());
                            mdsReceiptEntryTO.setSubNo(CommonUtil.convertObjToDouble(dataMap.get("SUB_NO")));
                            mdsReceiptEntryTO.setMemberName(CommonUtil.convertObjToStr(dataMap.get("MEMBER_NAME")));
                            mdsReceiptEntryTO.setDivisionNo(CommonUtil.convertObjToDouble(dataMap.get("DIVISION_NO")));
                            mdsReceiptEntryTO.setChitStartDt(getProperDateFormat(CommonUtil.convertObjToStr(dataMap.get("CHIT_START_DT"))));
                            mdsReceiptEntryTO.setChitEndDt(getProperDateFormat(CommonUtil.convertObjToStr(dataMap.get("INSTALLMENT_DATE"))));
                            mdsReceiptEntryTO.setPaidDate(getProperDateFormat(CommonUtil.convertObjToStr(dataMap.get("PAID_DATE"))));
                            mdsReceiptEntryTO.setNoOfInst(CommonUtil.convertObjToInt(dataMap.get("NO_OF_INSTALLMENTS")));
                            mdsReceiptEntryTO.setCurrInst(CommonUtil.convertObjToDouble(dataMap.get("CURR_INST")));
                            mdsReceiptEntryTO.setInstAmt(CommonUtil.convertObjToDouble(dataMap.get("INST_AMT")));
                            mdsReceiptEntryTO.setPendingInst(CommonUtil.convertObjToDouble(dataMap.get("PENDING_INST")));
                            mdsReceiptEntryTO.setTotalInstDue(CommonUtil.convertObjToDouble(dataMap.get("PENDING_DUE_AMT")));
                            mdsReceiptEntryTO.setPrizedMember(CommonUtil.convertObjToStr(dataMap.get("PRIZED_MEMBER")));
                            mdsReceiptEntryTO.setNoticeAmt(CommonUtil.convertObjToDouble("0"));
                            mdsReceiptEntryTO.setArbitrationAmt(CommonUtil.convertObjToDouble("0"));
                            mdsReceiptEntryTO.setNoOfInstPay(CommonUtil.convertObjToInt(dataMap.get("NO_OF_INST_PAY")));
                            mdsReceiptEntryTO.setInstAmtPayable(CommonUtil.convertObjToDouble(dataMap.get("INST_AMT_PAYABLE")));
                            mdsReceiptEntryTO.setPenalAmtPayable(CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("PENAL")).replaceAll(",", "")));

                            HashMap bankAdvMap = new HashMap();//Nidhin
                            int instNo = CommonUtil.convertObjToInt(mdsReceiptEntryTO.getCurrInst()) - CommonUtil.convertObjToInt(mdsReceiptEntryTO.getPendingInst());
                            bankAdvMap.put("SCHEME_NAME", CommonUtil.convertObjToStr(mdsReceiptEntryTO.getSchemeName()));
                            bankAdvMap.put("CHITTAL_NO", CommonUtil.convertObjToStr(mdsReceiptEntryTO.getChittalNo()));
                            bankAdvMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getSubNo()));
                            bankAdvMap.put("INSTALLMENT_NO", CommonUtil.convertObjToInt(instNo));
                            bankAdvMap.put("TO_INSTALLMENT", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getCurrInst()));
                            List bankAdvLst = sqlMap.executeQueryForList("getSelectBankAdvanceDetailsData", bankAdvMap);
                            Iterator addressIterator1 = finalChitalBonusMap.keySet().iterator();
                            LinkedHashMap normalInstMap = new LinkedHashMap();
                            if (bankAdvLst != null && bankAdvLst.size() > 0) {
                                LinkedHashMap bankMap = new LinkedHashMap();
                                bankAdvMap = new LinkedHashMap();
                                for (int b = 0; b < bankAdvLst.size(); b++) {
                                    bankMap = (LinkedHashMap) bankAdvLst.get(b);
                                    bankAdvMap.put(String.valueOf(bankMap.get("INSTALLMENT_NO")), bankMap);
                                }
                                System.out.println("bankAdvMap :: " + bankAdvMap);
                                String key1 = "";
                                for (int f = 0; f < finalChitalBonusMap.size(); f++) {
                                    key1 = (String) addressIterator1.next();
                                    System.out.println("###### key1 ###### : " + key1);
                                    if (bankAdvMap.containsKey(key1)) {
                                        HashMap installMap = new HashMap();
                                        bankAdvInsAmt += CommonUtil.convertObjToDouble(((HashMap) bankAdvMap.get(key1)).get("INST_AMT")).doubleValue();
                                        bankBonusAmt += CommonUtil.convertObjToDouble(((HashMap) bankAdvMap.get(key1)).get("BONUS_AMT")).doubleValue();
                                        advProcessInstList.add(key1);
                                    } else {
                                        HashMap installMap = new HashMap();
                                        futureAdvbonusAmt += CommonUtil.convertObjToDouble(finalChitalBonusMap.get(key1)).doubleValue();//     
                                        normalInstMap.put(key1, finalChitalBonusMap.get(key1));
                                    }
                                }
                                bankAdvInsAmt = bankAdvInsAmt - bankBonusAmt;
                                System.out.println("bankAdvInsAmt :: " + bankAdvInsAmt);
                                System.out.println("bankBonusAmt :: " + bankBonusAmt);
                                System.out.println("instalmentPayAmt :: " + instalmentPayAmt);
                                System.out.println("futureAdvbonusAmt :: " + futureAdvbonusAmt);
                            } else {
                                String key1 = "";
                                for (int f = 0; f < finalChitalBonusMap.size(); f++) {
                                    key1 = (String) addressIterator1.next();
                                    //System.out.println("###### key1 ###### : " + key1);
                                    HashMap installMap = new HashMap();
                                    futureAdvbonusAmt += CommonUtil.convertObjToDouble(finalChitalBonusMap.get(key1)).doubleValue();//     
                                    normalInstMap.put(key1, finalChitalBonusMap.get(key1));
                                }
                            }
                            //System.out.println("normalInstMap :: " + normalInstMap);
                            Iterator bonusKeyIterator = normalInstMap.keySet().iterator();
                            String bonusKey = "";
                            double bonusPayable = 0.0;
                            double normalInstallmentAmt = 0.0;
                            for (int f = 0; f < normalInstMap.size(); f++) {
                                bonusKey = (String) bonusKeyIterator.next();
                                bonusPayable = bonusPayable + CommonUtil.convertObjToDouble(normalInstMap.get(bonusKey));
                                normalInstallmentAmt += mdsReceiptEntryTO.getInstAmt();
                            }
                            mdsReceiptEntryTO.setBonusAmtPayable(bonusPayable);
                            mdsReceiptEntryTO.setForfeitBonusAmtPayable(forfeitBonusAmt);
                            mdsReceiptEntryTO.setBankAdvanceAmt(bankAdvInsAmt);
                            mdsReceiptEntryTO.setDiscountAmt(CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("DISCOUNT")).replaceAll(",", "")));
                            mdsReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("NET_AMOUNT")).replaceAll(",", "")));
                            mdsReceiptEntryTO.setPaidInst(CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("PAID_INST")).replaceAll(",", "")));
                            mdsReceiptEntryTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
                            mdsReceiptEntryTO.setStatus(CommonConstants.STATUS_CREATED);
                            mdsReceiptEntryTO.setStatusDt((Date) currDt.clone());
                            mdsReceiptEntryTO.setAuthorizeStatus(CommonUtil.convertObjToStr(CommonConstants.STATUS_AUTHORIZED));
                            mdsReceiptEntryTO.setAuthorizeBy((String) map.get(CommonConstants.USER_ID));
                            mdsReceiptEntryTO.setAuthorizeDt((Date) currDt.clone());
                            mdsReceiptEntryTO.setBranchCode(_branchCode);
                            mdsReceiptEntryTO.setNarration(CommonUtil.convertObjToStr(dataMap.get("NARRATION")));
                            mdsReceiptEntryTO.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("INITIATED_BRANCH")));
                            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                            transactionDAO.setInitiatedBranch(_branchCode);
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            if (penalAmt > 0.0) {
                                //System.out.println("penal Started");
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_AC_HD, (String) applicationMap.get("PENAL_INTEREST_HEAD"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                txMap.put(TransferTrans.PARTICULARS, "Penal " + applicationMap.get("MP_MDS_CODE") + "-" + objGDSApplicationTO.getChittalNo() + "_" + subNo);
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                txMap.put("generateSingleTransId", generateSingleTransId);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, penalAmt);
                                transferTo.setInstrumentNo1("SI");
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setTransDt(currDt);
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));
                                transactionTO.setChequeNo("SERVICE_TAX");
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                transferTo.setInstrumentNo2("PENAL_AMT");// Added by nithya on 31-10-2019 for KD-680
                                TxTransferTO.add(transferTo);
                                totalDebitAmt += penalAmt;
                            }
                            //System.out.println("totalDebitAmt penal :: " + totalDebitAmt);
                            if (bankAdvInsAmt > 0) {
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("BANKING_REP_PAY_HEAD"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                txMap.put(TransferTrans.PARTICULARS, "Installment Amt " + applicationMap.get("MP_MDS_CODE") + "-" + objGDSApplicationTO.getChittalNo() + "_" + subNo + "  From : " + debitAccNo);
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                txMap.put("generateSingleTransId", generateSingleTransId);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, bankAdvInsAmt);
                                transferTo.setInstrumentNo1("SI");
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setTransDt(currDt);
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                transferTo.setInstrumentNo2("INSTALMENT_AMT");// Added by nithya on 31-10-2019 for KD-680
                                TxTransferTO.add(transferTo);
                                totalDebitAmt += bankAdvInsAmt;
                            }
                            
                            
                            if (forfeitBonusAmt > 0.0) {
                                System.out.println("Forfeit transaction Started");
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_AC_HD, (String) applicationMap.get("MDS_FORFEITED_ACCT_HEAD"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                txMap.put(TransferTrans.PARTICULARS, "Bonus Reversal :" + applicationMap.get("MP_MDS_CODE") + "-" + chittalNo + "_" + subNo + ":BANK ADVANCE");
                                //     System.out.println("txMap2 : " + txMap + "penalAmt :" + penalAmt);                        
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                txMap.put("generateSingleTransId", generateSingleTransId);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, forfeitBonusAmt);
                                if (map.containsKey("FROM_MDS_RECOVERY_SCREEN")) {
                                    transferTo.setRec_mode("RP");
                                }
                                transferTo.setInstrumentNo1("SI");
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setTransDt(currDt);
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(chittalNo));
                                transactionTO.setChequeNo("SERVICE_TAX");
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                transferTo.setInstrumentNo2("BONUS_REVERSAL");
                                TxTransferTO.add(transferTo);
                                totalDebitAmt += forfeitBonusAmt;
                            }
                            
                            
                            
                            //System.out.println("totalDebitAmt bankAdvInsAmt :: " + totalDebitAmt);
                            //System.out.println("normalInstallmentAmt :: " + normalInstallmentAmt);
                            //System.out.println("mdsReceiptEntryTO.getBonusAmtPayable() :: " + mdsReceiptEntryTO.getBonusAmtPayable());
                            double finalReceivingAmt = normalInstallmentAmt - mdsReceiptEntryTO.getBonusAmtPayable();
                            if (finalReceivingAmt > 0) {
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                txMap.put(TransferTrans.PARTICULARS, "Installment Amt " + applicationMap.get("MP_MDS_CODE") + "-" + objGDSApplicationTO.getChittalNo() + "_" + subNo + "  From : " + debitAccNo);
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                txMap.put("generateSingleTransId", generateSingleTransId);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, finalReceivingAmt);
                                transferTo.setInstrumentNo1("SI");
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setTransDt(currDt);
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                transferTo.setInstrumentNo2("INSTALMENT_AMT");// Added by nithya on 31-10-2019 for KD-680
                                TxTransferTO.add(transferTo);
                                totalDebitAmt += finalReceivingAmt;
                            }
                            double bonous_Amt_trans = CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getBonusAmtPayable());
                        }
                        //System.out.println("totalDebitAmt..... " + totalDebitAmt);
                        if (netAmt > 0.0) {
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL") && transactionTO.getProductId().equals("")) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, "Amount Trans to " + schemeName + "-" + chittalNo + "_" + subNo+"-"+gdsGroupName);
                            } else if (!transactionTO.getProductType().equals("") && (prodType.equals("OA") || prodType.equals("SA") || prodType.equals("AD") || prodType.equals("AB") || prodType.equals("TD"))) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HEAD"));
                                txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                                txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(transactionTO.getProductType()));
                                txMap.put(TransferTrans.PARTICULARS, "Amount Trans to " + schemeName + "-" + chittalNo + "_" + subNo+"-"+gdsGroupName);
                            }
                            txMap.put(TransferTrans.DR_BRANCH, transferTo.getBranchId());
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
                            //System.out.println("txMap1 : " + txMap + "netAmt :" + netAmt);
                            if (transactionTO.getProductType().equals("AD")) {
                                txMap.put("AUTHORIZEREMARKS", "DP");
                            }
                            txMap.put("generateSingleTransId", generateSingleTransId);
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, totalDebitAmt);
                            transferTo.setInstrumentNo1("SI");
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt(currDt);
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(chittalNo));
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            TxTransferTO.add(transferTo);
                        }
                        HashMap applnMap = new HashMap();
                        transferDAO = new TransferDAO();
                        map.put("MODE", map.get("COMMAND"));
                        map.put("COMMAND", map.get("MODE"));
                        map.put("TxTransferTO", TxTransferTO);
                        try {
                            transMap = transferDAO.execute(map, false); // Comment to be removed
                        } catch (Exception e) {
                            sqlMap.rollbackTransaction();
                            e.printStackTrace();
                        }
                        transId = CommonUtil.convertObjToStr(transMap.get("TRANS_ID"));
                        mdsReceiptEntryTO.setNetTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                        mdsReceiptEntryTO.setPenalTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                        mdsReceiptEntryTO.setSingleTransId(generateSingleTransId);
                        transactionDAO.setBatchId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
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
                        authorizeTransaction(transMap, map);
                        if (bonusNew > 0 || bonusNew > 0.0) {
                            bonusNew = bonusNew;
                        }
                        double bonusForTransfer = mdsReceiptEntryTO.getBonusAmtPayable();
                        if (isBonusTrans.equalsIgnoreCase("Y")) {
                            commonTransactionCashandTransfer(map, debitMap, applicationMap, bonusForTransfer, discountAmt, chittalNo, subNo, allApplnDetails);
                        }
                        //System.out.println("####### mdsReceiptEntryTO : " + mdsReceiptEntryTO);
                        paidInst = CommonUtil.convertObjToInt(mdsReceiptEntryTO.getPaidInst());
                        noOFinstToPay = CommonUtil.convertObjToInt(mdsReceiptEntryTO.getNoOfInstPay());
                        int totalNoOfInstPay = paidInst + noOFinstToPay;
                        if (totalNoOfInstPay == CommonUtil.convertObjToInt(mdsReceiptEntryTO.getNoOfInst())) {
                            unMarkleanMap.put("CHITTAL_NO", CommonUtil.convertObjToStr(mdsReceiptEntryTO.getChittalNo()));
                            unMarkleanMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getSubNo()));
                            List depositTypeList = sqlMap.executeQueryForList("getSelectMDSDepositTypeTO", unMarkleanMap);
                            if (depositTypeList != null && depositTypeList.size() > 0) {
                                String depositNo = CommonUtil.convertObjToStr(((MDSDepositTypeTO) depositTypeList.get(0)).getDepositNo());
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
                                if (leanFlag) {
                                    HashMap availClearMap = new HashMap();
                                    double clearBal = 0;
                                    double availBal = 0;
                                    List clrAvailBalList = sqlMap.executeQueryForList("getClearAvailbalance", unMarkleanMap);
                                    if (clrAvailBalList != null && clrAvailBalList.size() > 0) {
                                        availClearMap = (HashMap) clrAvailBalList.get(0);
                                        if (availClearMap.containsKey("CLEAR_BALANCE") && availClearMap.get("CLEAR_BALANCE") != null && availClearMap.containsKey("AVAILABLE_BALANCE") && availClearMap.get("AVAILABLE_BALANCE") != null) {
                                            clearBal = CommonUtil.convertObjToDouble(availClearMap.get("CLEAR_BALANCE"));
                                            availBal = CommonUtil.convertObjToDouble(availClearMap.get("AVAILABLE_BALANCE"));
                                        }
                                    }
                                    unMarkleanMap.put("LIEN_ACCT_NO", chittalNo);
                                    List lienAmountList = sqlMap.executeQueryForList("selectLienAmountInDepositLien", unMarkleanMap);
                                    if (lienAmountList != null && lienAmountList.size() > 0) {
                                        double lienAmount = 0;
                                        HashMap lienAmountMap = (HashMap) lienAmountList.get(0);
                                        if (lienAmountMap.containsKey("LIENAMOUNT") && lienAmountMap.get("LIENAMOUNT") != null) {
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
                        if (transMap != null && transMap.containsKey("TRANS_ID") && transMap.get("TRANS_ID") != null) {
                            for (int k = 0; k < allApplnDetails.size(); k++) {
                                GDSApplicationTO objGDSApplicationTO = (GDSApplicationTO) allApplnDetails.get(k);
                                mdsReceiptEntryTO.setGds_no(CommonUtil.convertObjToStr(dataMap.get("CHITTAL_NO")));
                                mdsReceiptEntryTO.setChittalNo(objGDSApplicationTO.getChittalNo());
                                mdsReceiptEntryTO.setSchemeName(objGDSApplicationTO.getSchemeName());
                                if (isBonusTrans.equalsIgnoreCase("N")) {
                                    mdsReceiptEntryTO.setBonusAmtPayable(0.0);
                                    mdsReceiptEntryTO.setDiscountAmt(0.0);
                                }
                                if(mdsReceiptEntryTO.getForfeitBonusAmtPayable() > 0){
                                    mdsReceiptEntryTO.setForfeitBonusTransId(mdsReceiptEntryTO.getNetTransId());
                                }
                                sqlMap.executeUpdate("insertGDSReceiptEntryTO", mdsReceiptEntryTO);
                                HashMap transactionaApplnMap = new HashMap();
                                transactionaApplnMap.put("TOTAL_BALANCE", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getInstAmt()));
                                transactionaApplnMap.put("CLEAR_BALANCE", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getInstAmt()));
                                transactionaApplnMap.put("AVAILABLE_BALANCE", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getInstAmt()));
                                transactionaApplnMap.put("SHADOW_CREDIT", mdsReceiptEntryTO.getInstAmt());
                                transactionaApplnMap.put("SHADOW_DEBIT", mdsReceiptEntryTO.getInstAmt());
                                transactionaApplnMap.put("SCHEME_NAME", mdsReceiptEntryTO.getSchemeName());
                                transactionaApplnMap.put("BRANCH_CODE", _branchCode);
                                transactionaApplnMap.put("TRANS_ID", mdsReceiptEntryTO.getNetTransId());
                                sqlMap.executeUpdate("updateMDSAvailBalanceMap", transactionaApplnMap);
                                sqlMap.executeUpdate("updateMDSClearBalanceMap", transactionaApplnMap);
                                sqlMap.executeUpdate("updateMDSTotalBalanceMap", transactionaApplnMap);
                                HashMap mdsTransMap = new HashMap();
                                mdsTransMap.put("SCHEME_NAME", mdsReceiptEntryTO.getSchemeName());
                                mdsTransMap.put("CHITTAL_NO", mdsReceiptEntryTO.getChittalNo());
                                mdsTransMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getSubNo()));
                                mdsTransMap.put("NO_OF_INST", mdsReceiptEntryTO.getNoOfInstPay());
                                mdsTransMap.put("INST_AMT", mdsReceiptEntryTO.getInstAmt());
                                mdsTransMap.put("PENAL_AMT", mdsReceiptEntryTO.getPenalAmtPayable());
                                if (isBonusTrans.equalsIgnoreCase("N")) {// Modified by nithya on 16-01-2020 for KD-1280
//                                    mdsTransMap.put("BONUS_AMT", 0);
//                                    mdsTransMap.put("DISCOUNT_AMT", 0);
                                      mdsTransMap.put("BONUS_AMT", new Double(0.0));
                                      mdsTransMap.put("DISCOUNT_AMT", new Double(0.0));                                    
                                } else {
                                    mdsTransMap.put("BONUS_AMT", mdsReceiptEntryTO.getBonusAmtPayable());
                                    mdsTransMap.put("DISCOUNT_AMT", mdsReceiptEntryTO.getDiscountAmt());
                                }
                                mdsTransMap.put("MDS_INTEREST", mdsReceiptEntryTO.getMdsInterset());
                                mdsTransMap.put("NET_AMT", mdsReceiptEntryTO.getNetAmt());
                                //mdsTransMap.put("SUBSCRIPTION_AMT", mdsReceiptEntryTO.getInstAmtPayable()); // 01-11-2019
                                mdsTransMap.put("STATUS", CommonConstants.STATUS_CREATED);
                                mdsTransMap.put("STATUS_DT", currDt);
                                mdsTransMap.put("STATUS_BY", map.get("USER_ID"));
                                mdsTransMap.put("AUTHORIZE_STATUS", CommonUtil.convertObjToStr(CommonConstants.STATUS_AUTHORIZED));
                                mdsTransMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                                mdsTransMap.put("AUTHORIZE_DT", currDt);
                                mdsTransMap.put("TRANS_DT", currDt);
                                mdsTransMap.put("NET_TRANS_ID", mdsReceiptEntryTO.getNetTransId());
                                mdsTransMap.put("NARRATION", mdsReceiptEntryTO.getNarration());
                                mdsTransMap.put("GDS_NO", mdsReceiptEntryTO.getGds_no());
                                mdsTransMap.put("FORFEITED_AMT",mdsReceiptEntryTO.getForfeitBonusAmtPayable()); // 2093
                                mdsTransMap.put("BANK_ADV_AMTCR",mdsReceiptEntryTO.getBankAdvanceAmt());
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
                                sqlMap.executeUpdate("updateGDSTransDetailsEachRec", mdsTransMap);
                            }
                            if (advProcessInstList != null && advProcessInstList.size() > 0) {
                                //System.out.println(" advProcessInstList ::  " + advProcessInstList);
                                for (int l = 0; l < advProcessInstList.size(); l++) {
                                    HashMap whereMap = new HashMap();
                                    whereMap.put("INSTALLMENT_NO", CommonUtil.convertObjToInt(advProcessInstList.get(l)));
                                    whereMap.put("CHITTAL_NO", CommonUtil.convertObjToStr(dataMap.get("CHITTAL_NO")));
                                    whereMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getSubNo()));
                                    whereMap.put("AUTH_STATUS", "AUTHORIZED");
                                    whereMap.put("GDS_NO", CommonUtil.convertObjToStr(dataMap.get("CHITTAL_NO")));
                                    whereMap.put("REPAID", "Y");
                                    whereMap.put("REPAID_DT", currDt.clone());
                                    sqlMap.executeUpdate("updateGDSBankAdvanceRepaidStatus", whereMap);
                                }
                            }
                        }

                        if (!transMap.get("SINGLE_TRANS_ID").equals("")) {
                            returnSingleMapList.add(transMap.get("SINGLE_TRANS_ID"));
                        }
                        if (!transMap.get("TRANS_ID").equals("")) {
                            returnMapList.add(transMap.get("TRANS_ID"));
                        }
                        if (returnMapList != null && returnMapList.size() > 0) {
                            HashMap smsAlertMap = new HashMap();
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
                                        MdsTransMap.put(CommonConstants.BRANCH_ID, BRANCH_ID);
                                        smsDAO.MdsReceiptsSmsConfiguration(MdsTransMap);
                                        MdsTransList = null;
                                    }
                                }
                                MdsSmsList = null;
                            }
                        }
                        HashMap lastInstMap = new HashMap();
                        lastInstMap.put("SCHEME_NAME", mdsReceiptEntryTO.getSchemeName());
                        lastInstMap.put("CHITTAL_NO", mdsReceiptEntryTO.getChittalNo());
                        List LastInstList = sqlMap.executeQueryForList("getChittalLastInstDetails", lastInstMap);
                        if (LastInstList != null && LastInstList.size() > 0) {
                            lastInstMap.put("LAST_INST_DT", currDt.clone());
                            sqlMap.executeUpdate("updateMasterMaintenceCloseDt", lastInstMap);
                        }
                    }
                }
                List closureList = (List) sqlMap.executeQueryForList("checkSchemeClosureDetails", map);
                if (closureList != null && closureList.size() > 0) {
                    double totalSchemeAmount = 0.0;
                    double totalAmtReceived = 0.0;
                    double totalAmtPaid = 0.0;
                    HashMap whereMap = new HashMap();
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
                                    hmap.put("SUBNO",CommonUtil.convertObjToInt("1"));
                                    hmap.put("SHADOWLIEN", new Double(0.0));
                                    sqlMap.executeUpdate("updateSubAcInfoAvlBal", hmap);
                                    sqlMap.executeUpdate("updateUnlienForMDS", hmap);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    public Date getProperDateFormat(Object obj) {
        Date curDt = null;
        if (obj!=null && obj.toString().length()>0) {
            Date tempDt= DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            curDt=(Date)currDt.clone();
            curDt.setDate(tempDt.getDate());
            curDt.setMonth(tempDt.getMonth());
            curDt.setYear(tempDt.getYear());
        }
        return curDt;
    }
    
    private String setNarrationToSplitTransaction(int i, int paidInstallments, String strDt, String endDt) {
        String narration = "";
        ArrayList narrationList = new ArrayList();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM/yyyy");
        int paidInst = paidInstallments + 1;
        paidInstallments += i;
        paidInst += i;
        narration = "Inst#" + (paidInst);
        Date dt1 = DateUtil.addDays(DateUtil.getDateMMDDYYYY(strDt), 30 * paidInstallments);
        Date dt = DateUtil.addDays(DateUtil.getDateMMDDYYYY(endDt), 30 * (1));
        narration += " " + sdf.format(dt1);
        //narrationList.add(narration);
        return narration;
    }

    public void authorizeTransaction(HashMap transMap, HashMap map) throws Exception {
        try {
            if (transMap != null && transMap.get("TRANS_ID") != null && !transMap.get("TRANS_ID").equals("")) {
                System.out.println("TRANSFER TRANS_ID :" + transMap.get("TRANS_ID"));
                String authorizeStatus = CommonUtil.convertObjToStr(CommonConstants.STATUS_AUTHORIZED);
                String linkBatchId = CommonUtil.convertObjToStr(transMap.get("TRANS_ID"));
                HashMap cashAuthMap = new HashMap();
                cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                cashAuthMap.put("MDS_STANDING_AD", "MDS_STANDING_AD");
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
                cashAuthMap = null;
                transMap = null;
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    public void commonTransactionCashandTransfer(HashMap map, HashMap debitMap, HashMap applicationMap, double bonusAmt, double discountAmt, String chittalNo, String subNo, List allApplnDetails) throws Exception {
        String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        TransferTrans transferTrans = new TransferTrans();
        TxTransferTO transferTo = new TxTransferTO();
        ArrayList TxTransferTO = new ArrayList();
        TransactionTO transactionTO = new TransactionTO();
        HashMap txMap = new HashMap();
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
            // Bonus transaction Start
            if (bonusAmt > 0) {
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("BONUS_RECEIVABLE_HEAD"));
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                txMap.put(TransferTrans.PARTICULARS, "Bonus " + applicationMap.get("MP_MDS_CODE") + "-" + objGDSApplicationTO.getChittalNo() + "_" + subNo);
                txMap.put("TRANS_MOD_TYPE", "MDS");
                transferTo = transactionDAO.addTransferCreditLocal(txMap, bonusAmt);
                transferTo.setInstrumentNo1("SI");
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransType(CommonConstants.DEBIT);
                transferTo.setProdType(TransactionFactory.GL);
                transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("BONUS_RECEIVABLE_HEAD")));
                transferTo.setTransDt(currDt);
                transferTo.setBranchId(BRANCH_ID);
                transferTo.setInitiatedBranch(BRANCH_ID);
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));
                transactionTO.setChequeNo("SERVICE_TAX");
                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                TxTransferTO.add(transferTo);
            }
            // Bonus transaction ends
            // Discount transaction start
            if (discountAmt > 0) {
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("DISCOUNT_HEAD"));
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                txMap.put(TransferTrans.PARTICULARS, "Discount " + applicationMap.get("MP_MDS_CODE") + "-" + chittalNo + "_" + subNo);
                txMap.put("TRANS_MOD_TYPE", "MDS");
                transferTo = transactionDAO.addTransferCreditLocal(txMap, discountAmt);
                transferTo.setInstrumentNo1("SI");
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransType(CommonConstants.DEBIT);
                transferTo.setProdType(TransactionFactory.GL);
                transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("DISCOUNT_HEAD")));
                transferTo.setTransDt(currDt);
                transferTo.setBranchId(BRANCH_ID);
                transferTo.setInitiatedBranch(BRANCH_ID);
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));
                transactionTO.setChequeNo("SERVICE_TAX");
                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                TxTransferTO.add(transferTo);
            }
            // Discount transaction ends
            double receivingGLAmt = discountAmt + bonusAmt;
            if (receivingGLAmt > 0) {
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                txMap.put(TransferTrans.PARTICULARS, "Installment Amt " + applicationMap.get("MP_MDS_CODE") + "-" + objGDSApplicationTO.getChittalNo() + "_" + subNo);                
                txMap.put("TRANS_MOD_TYPE", "MDS");
                transferTo = transactionDAO.addTransferCreditLocal(txMap, receivingGLAmt);
                transferTo.setInstrumentNo1("SI");
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransType(CommonConstants.CREDIT);
                transferTo.setProdType(TransactionFactory.GL);
                transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("RECEIPT_HEAD")));
                transferTo.setTransDt(currDt);
                transferTo.setBranchId(BRANCH_ID);
                transferTo.setInitiatedBranch(BRANCH_ID);
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));
                transactionTO.setChequeNo("SERVICE_TAX");
                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                TxTransferTO.add(transferTo);
            }
        }
        map.put("MODE", map.get("COMMAND"));
        map.put("COMMAND", map.get("MODE"));
        map.put("TxTransferTO", TxTransferTO);        
        HashMap transMap = transferDAO.execute(map, false);
        mdsReceiptEntryTO.setBonusTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
        mdsReceiptEntryTO.setDiscountTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
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
        authorizeTransaction(transMap, map);
        linkBatchMap = null;
        transMap = null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
//        currDt = ServerUtil.getCurrentDate(_branchCode);
        return null;
    }

    private void destroyObjects() {
        standingLst = null;
    }
    
     private void executeTransactionPart_old(HashMap map) throws Exception {
        try {
            if (standingLst != null && standingLst.size() > 0) {
                String isBonusTrans = "Y";
                schemeName = CommonUtil.convertObjToStr(map.get("SCHEME_NAME"));
                String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
                SmsConfigDAO smsDAO = new SmsConfigDAO();
                HashMap unMarkleanMap = new HashMap();
                HashMap groupMap = new HashMap();
                groupMap.put("GROUP_NO", map.get("GROUP_NO"));
                List commonSchemeDetailsLst = sqlMap.executeQueryForList("getDetailsForOneSchemeInGroup", groupMap);
                HashMap commonSchemeMap = (HashMap) commonSchemeDetailsLst.get(0);
                String commonScheme = CommonUtil.convertObjToStr(commonSchemeMap.get("SCHEME_NAME"));
                HashMap checkMap = new HashMap();
                checkMap.put("SCHEME_NAME", commonScheme);
                List bonusTransLst = sqlMap.executeQueryForList("getifBonusTransactionRequired", checkMap);
                if (bonusTransLst != null && bonusTransLst.size() > 0) {
                    checkMap = (HashMap) bonusTransLst.get(0);
                    if (checkMap.containsKey("IS_BONUS_TRANSFER") && checkMap.get("IS_BONUS_TRANSFER") != null && !"".equalsIgnoreCase(CommonUtil.convertObjToStr(checkMap.get("IS_BONUS_TRANSFER")))) {
                        isBonusTrans = CommonUtil.convertObjToStr(checkMap.get("IS_BONUS_TRANSFER"));
                    }
                }               
                for (int i = 0; i < standingLst.size(); i++) {  
                    HashMap dataMap = new HashMap();
                    double instalAmt = 0.0;
                    double bonusAmt = 0.0;
                    double discountAmt = 0.0;
                    double penalAmt = 0.0;
                    double netAmt = 0.0;
                    double bonusNew = 0.0;
                    String chittalNo = "";
                    String subNo = "";
                    String prodType = "";
                    String prodId = "";
                    String debitAccNo = "";
                    int noOFinstToPay = 0;
                    int paidInst = 0;
                    HashMap leanFlagMap = new HashMap();
                    boolean leanFlag = false;
                    String transId = "";
                    dataMap = (HashMap) standingLst.get(i);
                    int schemeCount = 0;
                    HashMap applicationMap = new HashMap();
                    applicationMap.put("GDS_NO", dataMap.get("CHITTAL_NO"));
                    List allApplnDetails = sqlMap.executeQueryForList("getAllApplcationDetailsForGDS", applicationMap);
                    if (allApplnDetails != null && allApplnDetails.size() > 0) {
                        schemeCount = allApplnDetails.size();
                        instalAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("INST_AMT")).replaceAll(",", "")).doubleValue();
                        bonusAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("BONUS")).replaceAll(",", "")).doubleValue();
                        bonusNew = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("BONUS_NEW")).replaceAll(",", ""));
                        discountAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("DISCOUNT")).replaceAll(",", "")).doubleValue();
                        penalAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("PENAL")).replaceAll(",", "")).doubleValue();
                        netAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("NET_AMOUNT")).replaceAll(",", "")).doubleValue();
                        chittalNo = CommonUtil.convertObjToStr(dataMap.get("CHITTAL_NO"));
                        subNo = CommonUtil.convertObjToStr(dataMap.get("SUB_NO"));
                        prodType = CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE"));
                        prodId = CommonUtil.convertObjToStr(dataMap.get("PROD_ID"));
                        debitAccNo = CommonUtil.convertObjToStr(dataMap.get("DR_ACT_NO"));
                        // Debit transaction starts
                        ArrayList transferList = new ArrayList();
                        TransferTrans transferTrans = new TransferTrans();
                        TxTransferTO transferTo = new TxTransferTO();
                        ArrayList TxTransferTO = new ArrayList();
                        TransactionTO transactionTO = new TransactionTO();
                        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                        transactionDAO.setInitiatedBranch(_branchCode);
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        HashMap txMap = new HashMap();
                        HashMap transactionListMap = new HashMap();
                        HashMap transMap = new HashMap();
                        transactionTO.setProductType(prodType);
                        transactionTO.setDebitAcctNo(debitAccNo);
                        transactionTO.setProductId(prodId);
                        HashMap debitMap = new HashMap();
                        List lst;
                        if (!prodType.equals("") && (prodType.equals("OA") || prodType.equals("SA") || prodType.equals("AD") || prodType.equals("AB") || prodType.equals("TD"))) {
                            debitMap.put("PROD_ID", prodId);
                            lst = sqlMap.executeQueryForList("getAccountHeadProd" + prodType, debitMap);
                            if (lst != null && lst.size() > 0) {
                                debitMap = (HashMap) lst.get(0);
                            }
                            HashMap debitBranchCodeMap = new HashMap();
                            debitBranchCodeMap.put("ACT_NUM", debitAccNo);
                            List lstdebitBranchCode = sqlMap.executeQueryForList("getSelectInterBranchCode", debitBranchCodeMap);
                            if (lstdebitBranchCode != null && lstdebitBranchCode.size() > 0) {
                                debitBranchCodeMap = (HashMap) lstdebitBranchCode.get(0);
                                transferTo.setBranchId(CommonUtil.convertObjToStr(debitBranchCodeMap.get("BRANCH_CODE")));
                            }
                        }
                        if (netAmt > 0.0) {
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL") && transactionTO.getProductId().equals("")) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, "Amount Trans to " + schemeName + "-" + chittalNo + "_" + subNo);
                            } else if (!transactionTO.getProductType().equals("") && (prodType.equals("OA") || prodType.equals("SA") || prodType.equals("AD") || prodType.equals("AB") || prodType.equals("TD"))) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HEAD"));
                                txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                                txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(transactionTO.getProductType()));
                                txMap.put(TransferTrans.PARTICULARS, "Amount Trans to " + schemeName + "-" + chittalNo + "_" + subNo);
                            }
                            txMap.put(TransferTrans.DR_BRANCH, transferTo.getBranchId());
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
                            System.out.println("txMap1 : " + txMap + "netAmt :" + netAmt);
                            if (transactionTO.getProductType().equals("AD")) {
                                txMap.put("AUTHORIZEREMARKS", "DP");
                            }
                            txMap.put("generateSingleTransId", generateSingleTransId);
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, netAmt * schemeCount);
                            transferTo.setInstrumentNo1("SI");
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt(currDt);
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(chittalNo));
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            TxTransferTO.add(transferTo);
                        }
                        // Debit transaction ends
                        for (int count = 0; count < allApplnDetails.size(); count++) {
                            mdsReceiptEntryTO = new GDSReceiptEntryTO();
                            GDSApplicationTO objGDSApplicationTO = (GDSApplicationTO) allApplnDetails.get(count);
                            HashMap schemeMap = new HashMap();
                            schemeMap.put("SCHEME_NAME", objGDSApplicationTO.getSchemeName());
                            List headlst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", schemeMap);
                            if (headlst != null && headlst.size() > 0) {
                                applicationMap = (HashMap) headlst.get(0);
                            }
                            mdsReceiptEntryTO.setSchemeName(objGDSApplicationTO.getSchemeName());
                            mdsReceiptEntryTO.setChittalNo(objGDSApplicationTO.getChittalNo());
                            mdsReceiptEntryTO.setSubNo(CommonUtil.convertObjToDouble(dataMap.get("SUB_NO")));
                            mdsReceiptEntryTO.setMemberName(CommonUtil.convertObjToStr(dataMap.get("MEMBER_NAME")));
                            mdsReceiptEntryTO.setDivisionNo(CommonUtil.convertObjToDouble(dataMap.get("DIVISION_NO")));
                            mdsReceiptEntryTO.setChitStartDt(getProperDateFormat(CommonUtil.convertObjToStr(dataMap.get("CHIT_START_DT"))));
                            mdsReceiptEntryTO.setChitEndDt(getProperDateFormat(CommonUtil.convertObjToStr(dataMap.get("INSTALLMENT_DATE"))));
                            mdsReceiptEntryTO.setPaidDate(getProperDateFormat(CommonUtil.convertObjToStr(dataMap.get("PAID_DATE"))));
                            mdsReceiptEntryTO.setNoOfInst(CommonUtil.convertObjToInt(dataMap.get("NO_OF_INSTALLMENTS")));
                            mdsReceiptEntryTO.setCurrInst(CommonUtil.convertObjToDouble(dataMap.get("CURR_INST")));
                            mdsReceiptEntryTO.setInstAmt(CommonUtil.convertObjToDouble(dataMap.get("INST_AMT")));
                            mdsReceiptEntryTO.setPendingInst(CommonUtil.convertObjToDouble(dataMap.get("PENDING_INST")));
                            mdsReceiptEntryTO.setTotalInstDue(CommonUtil.convertObjToDouble(dataMap.get("PENDING_DUE_AMT")));
                            mdsReceiptEntryTO.setPrizedMember(CommonUtil.convertObjToStr(dataMap.get("PRIZED_MEMBER")));
                            mdsReceiptEntryTO.setNoticeAmt(CommonUtil.convertObjToDouble("0"));
                            mdsReceiptEntryTO.setArbitrationAmt(CommonUtil.convertObjToDouble("0"));
                            mdsReceiptEntryTO.setNoOfInstPay(CommonUtil.convertObjToInt(dataMap.get("NO_OF_INST_PAY")));
                            mdsReceiptEntryTO.setInstAmtPayable(CommonUtil.convertObjToDouble(dataMap.get("INST_AMT_PAYABLE")));
                            mdsReceiptEntryTO.setPenalAmtPayable(CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("PENAL")).replaceAll(",", "")));

                            HashMap bankAdvMap = new HashMap();//Nidhin
                            int instNo = CommonUtil.convertObjToInt(mdsReceiptEntryTO.getCurrInst()) - CommonUtil.convertObjToInt(mdsReceiptEntryTO.getPendingInst());
                            bankAdvMap.put("SCHEME_NAME", CommonUtil.convertObjToStr(mdsReceiptEntryTO.getSchemeName()));
                            bankAdvMap.put("CHITTAL_NO", CommonUtil.convertObjToStr(mdsReceiptEntryTO.getChittalNo()));
                            bankAdvMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getSubNo()));
                            bankAdvMap.put("INSTALLMENT_NO", CommonUtil.convertObjToInt(instNo));
                            bankAdvMap.put("TO_INSTALLMENT", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getCurrInst()));
                            List bankAdvanceLst = sqlMap.executeQueryForList("getSelectBankAdvanceBonusData", bankAdvMap);
                            if (bankAdvanceLst != null && bankAdvanceLst.size() > 0) {
                                HashMap bonusMap = (HashMap) bankAdvanceLst.get(0);
                                if (bonusMap.containsKey("BONUS_AMOUNT") && bonusMap.get("BONUS_AMOUNT") != null) {
                                    double bankBonusAmt = CommonUtil.convertObjToDouble(bonusMap.get("BONUS_AMOUNT"));
                                    bonusAmt = bonusAmt - bankBonusAmt;
                                }
                            }
                            if ((bonusNew > 0 || bonusNew > 0.0) && bonusNew > bonusAmt) {//Jeffin
                                mdsReceiptEntryTO.setBonusAmtPayable(bonusNew);
                            } else {
                                if (dataMap.containsKey("BONUS") && dataMap.get("BONUS") != null) {
                                    mdsReceiptEntryTO.setBonusAmtPayable(CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("BONUS")).replaceAll(",", "")));
                                } else {
                                    mdsReceiptEntryTO.setBonusAmtPayable(CommonUtil.convertObjToDouble("0"));
                                }
                            }
                            mdsReceiptEntryTO.setDiscountAmt(CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("DISCOUNT")).replaceAll(",", "")));
                            mdsReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("NET_AMOUNT")).replaceAll(",", "")));
                            mdsReceiptEntryTO.setPaidInst(CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("PAID_INST")).replaceAll(",", "")));
                            mdsReceiptEntryTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
                            mdsReceiptEntryTO.setStatus(CommonConstants.STATUS_CREATED);
                            mdsReceiptEntryTO.setStatusDt((Date) currDt.clone());
                            mdsReceiptEntryTO.setAuthorizeStatus(CommonUtil.convertObjToStr(CommonConstants.STATUS_AUTHORIZED));
                            mdsReceiptEntryTO.setAuthorizeBy((String) map.get(CommonConstants.USER_ID));
                            mdsReceiptEntryTO.setAuthorizeDt((Date) currDt.clone());
                            mdsReceiptEntryTO.setBranchCode(_branchCode);
                            mdsReceiptEntryTO.setNarration(CommonUtil.convertObjToStr(dataMap.get("NARRATION")));
                            mdsReceiptEntryTO.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("INITIATED_BRANCH")));
                            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                            transactionDAO.setInitiatedBranch(_branchCode);
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            // Penal transaction starts
                            if (penalAmt > 0.0) {
                                System.out.println("penal Started");
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_AC_HD, (String) applicationMap.get("PENAL_INTEREST_HEAD"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                txMap.put(TransferTrans.PARTICULARS, "Penal " + applicationMap.get("MP_MDS_CODE") + "-" + objGDSApplicationTO.getChittalNo() + "_" + subNo);
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                txMap.put("generateSingleTransId", generateSingleTransId);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, penalAmt);
                                transferTo.setInstrumentNo1("SI");
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setTransDt(currDt);
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));
                                transactionTO.setChequeNo("SERVICE_TAX");
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                            }
                            // Penal Transaction Ends
                            // credit transaction : amount to mds = netamount - penal amount :: starts
                            double finalReceivingAmt = netAmt - penalAmt;
                            if (finalReceivingAmt > 0) {
                                transferTo = new TxTransferTO();
                                txMap = new HashMap();
                                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                txMap.put(TransferTrans.PARTICULARS, "Installment Amt " + applicationMap.get("MP_MDS_CODE") + "-" + objGDSApplicationTO.getChittalNo() + "_" + subNo + "  From : " + debitAccNo);
                                txMap.put("TRANS_MOD_TYPE", "MDS");
                                txMap.put("generateSingleTransId", generateSingleTransId);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, finalReceivingAmt);
                                transferTo.setInstrumentNo1("SI");
                                transferTo.setTransId("-");
                                transferTo.setBatchId("-");
                                transferTo.setTransDt(currDt);
                                transferTo.setInitiatedBranch(BRANCH_ID);
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
                                transferTo.setGlTransActNum(CommonUtil.convertObjToStr(objGDSApplicationTO.getChittalNo()));
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                TxTransferTO.add(transferTo);
                            }
                            // credit transaction : amount to mds = netamount - penal amount :: ends
                            // Bank advance - need to check
                            double bonous_Amt_trans = CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getBonusAmtPayable());
                            if (bankAdvanceLst != null && bankAdvanceLst.size() > 0) {
                                HashMap bonusMap = (HashMap) bankAdvanceLst.get(0);
                                if (bonusMap.containsKey("BONUS_AMOUNT") && bonusMap.get("BONUS_AMOUNT") != null) {
                                    bonous_Amt_trans = bonusNew;
                                }
                            }
                        }
                        HashMap applnMap = new HashMap();
                        transferDAO = new TransferDAO();
                        map.put("MODE", map.get("COMMAND"));
                        map.put("COMMAND", map.get("MODE"));
                        map.put("TxTransferTO", TxTransferTO);
                        try {
                            transMap = transferDAO.execute(map, false); // Comment to be removed
                        } catch (Exception e) {
                            sqlMap.rollbackTransaction();
                            e.printStackTrace();
                            //returnMap.put(chittalNo, e);
                            //continue;
                        }
                        transId = CommonUtil.convertObjToStr(transMap.get("TRANS_ID"));
                        mdsReceiptEntryTO.setNetTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                        mdsReceiptEntryTO.setPenalTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                        mdsReceiptEntryTO.setSingleTransId(generateSingleTransId);
                        transactionDAO.setBatchId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
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
                        authorizeTransaction(transMap, map); 
                        if (bonusNew > 0 || bonusNew > 0.0) {
                            bonusNew = bonusNew;
                        }                      
                        double bonusForTransfer = mdsReceiptEntryTO.getBonusAmtPayable();
                        if(isBonusTrans.equalsIgnoreCase("Y"))
                        commonTransactionCashandTransfer(map, debitMap, applicationMap, bonusForTransfer, discountAmt, chittalNo, subNo, allApplnDetails);
                        System.out.println("####### mdsReceiptEntryTO : " + mdsReceiptEntryTO);
                        paidInst = CommonUtil.convertObjToInt(mdsReceiptEntryTO.getPaidInst());
                        noOFinstToPay = CommonUtil.convertObjToInt(mdsReceiptEntryTO.getNoOfInstPay());
                        int totalNoOfInstPay = paidInst + noOFinstToPay;
                        if (totalNoOfInstPay == CommonUtil.convertObjToInt(mdsReceiptEntryTO.getNoOfInst())) {                           
                            unMarkleanMap.put("CHITTAL_NO", CommonUtil.convertObjToStr(mdsReceiptEntryTO.getChittalNo()));
                            unMarkleanMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getSubNo()));
                            List depositTypeList = sqlMap.executeQueryForList("getSelectMDSDepositTypeTO", unMarkleanMap);
                            if (depositTypeList != null && depositTypeList.size() > 0) {
                                String depositNo = CommonUtil.convertObjToStr(((MDSDepositTypeTO) depositTypeList.get(0)).getDepositNo());
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
                                if (leanFlag) {
                                    HashMap availClearMap = new HashMap();
                                    double clearBal = 0;
                                    double availBal = 0;
                                    List clrAvailBalList = sqlMap.executeQueryForList("getClearAvailbalance", unMarkleanMap);
                                    if (clrAvailBalList != null && clrAvailBalList.size() > 0) {
                                        availClearMap = (HashMap) clrAvailBalList.get(0);
                                        if (availClearMap.containsKey("CLEAR_BALANCE") && availClearMap.get("CLEAR_BALANCE") != null && availClearMap.containsKey("AVAILABLE_BALANCE") && availClearMap.get("AVAILABLE_BALANCE") != null) {
                                            clearBal = CommonUtil.convertObjToDouble(availClearMap.get("CLEAR_BALANCE"));
                                            availBal = CommonUtil.convertObjToDouble(availClearMap.get("AVAILABLE_BALANCE"));
                                        }
                                    }
                                    unMarkleanMap.put("LIEN_ACCT_NO", chittalNo);
                                    List lienAmountList = sqlMap.executeQueryForList("selectLienAmountInDepositLien", unMarkleanMap);
                                    if (lienAmountList != null && lienAmountList.size() > 0) {
                                        double lienAmount = 0;
                                        HashMap lienAmountMap = (HashMap) lienAmountList.get(0);
                                        if (lienAmountMap.containsKey("LIENAMOUNT") && lienAmountMap.get("LIENAMOUNT") != null) {
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

                        if (map.containsKey("MDS_SPLIT_TRANSACTION") && map.get("MDS_SPLIT_TRANSACTION") != null && map.get("MDS_SPLIT_TRANSACTION").equals("Y")) {
                            List splitList = null;
                            List splitTransInstList = new ArrayList();
                            List bonusAmountList = new ArrayList();
                            List penalList = new ArrayList();
                            List narrationList = new ArrayList();
                            HashMap splitMap = new HashMap();
                            int curInst = 0;                           
                            if (map.containsKey("GDS_STANDING_INSTRUCTION_SPLIT_LIST") && map.get("GDS_STANDING_INSTRUCTION_SPLIT_LIST") != null) {
                                splitList = (List) map.get("GDS_STANDING_INSTRUCTION_SPLIT_LIST");                                                         
                                splitMap = (HashMap) splitList.get(i);
                                if (splitMap.containsKey("INST_AMT_LIST") && splitMap.get("INST_AMT_LIST") != null) {
                                    splitTransInstList = (List) splitMap.get("INST_AMT_LIST");
                                }
                                if (splitMap.containsKey("BONUS_AMT_LIST") && splitMap.get("BONUS_AMT_LIST") != null) {
                                    bonusAmountList = (List) splitMap.get("BONUS_AMT_LIST");
                                }
                                if (splitMap.containsKey("NARRATION_LIST") && splitMap.get("NARRATION_LIST") != null) {
                                    narrationList = (List) splitMap.get("NARRATION_LIST");
                                }
                                if (splitMap.containsKey("PENAL_AMT_LIST") && splitMap.get("PENAL_AMT_LIST") != null) {
                                    penalList = (List) splitMap.get("PENAL_AMT_LIST");
                                }
                                if (splitMap.containsKey("INSTALL_NO") && splitMap.get("INSTALL_NO") != null) {
                                    curInst = CommonUtil.convertObjToInt(splitMap.get("INSTALL_NO"));
                                }
                                for (int k = 0; k < splitTransInstList.size(); k++) {
                                    mdsReceiptEntryTO.setBonusAmtPayable(CommonUtil.convertObjToDouble(bonusAmountList.get(k)));
                                    mdsReceiptEntryTO.setCurrInst(new Double(curInst));
                                    mdsReceiptEntryTO.setNarration(setNarrationToSplitTransaction(k,
                                            CommonUtil.convertObjToInt(splitMap.get("INSTALL_NO")),
                                            CommonUtil.convertObjToStr(mdsReceiptEntryTO.getChitStartDt()),
                                            CommonUtil.convertObjToStr(mdsReceiptEntryTO.getChitEndDt())));
                                    mdsReceiptEntryTO.setNoOfInstPay(1);
                                    mdsReceiptEntryTO.setPaidInst(new Double(curInst));
                                    mdsReceiptEntryTO.setPenalAmtPayable(CommonUtil.convertObjToDouble(penalList.get(k)));
                                    mdsReceiptEntryTO.setInstAmtPayable(CommonUtil.convertObjToDouble(splitTransInstList.get(k)));
                                    mdsReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(penalList.get(k)) + CommonUtil.convertObjToDouble(splitTransInstList.get(k)));
                                    sqlMap.executeUpdate("insertReceiptEntryTO", mdsReceiptEntryTO);
                                    curInst++;
                                  
                                    HashMap transactionaApplnMap = new HashMap();
                                    transactionaApplnMap.put("TOTAL_BALANCE", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getInstAmt()));
                                    transactionaApplnMap.put("CLEAR_BALANCE", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getInstAmt()));
                                    transactionaApplnMap.put("AVAILABLE_BALANCE", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getInstAmt()));
                                    transactionaApplnMap.put("SHADOW_CREDIT", mdsReceiptEntryTO.getInstAmt());
                                    transactionaApplnMap.put("SHADOW_DEBIT", mdsReceiptEntryTO.getInstAmt());
                                    transactionaApplnMap.put("SCHEME_NAME", mdsReceiptEntryTO.getSchemeName());
                                    transactionaApplnMap.put("BRANCH_CODE", _branchCode);
                                    transactionaApplnMap.put("TRANS_ID", mdsReceiptEntryTO.getNetTransId());                                   
                                    sqlMap.executeUpdate("updateMDSAvailBalanceMap", transactionaApplnMap);
                                    sqlMap.executeUpdate("updateMDSClearBalanceMap", transactionaApplnMap);
                                    sqlMap.executeUpdate("updateMDSTotalBalanceMap", transactionaApplnMap);

                                    HashMap mdsTransMap = new HashMap();
                                    mdsTransMap.put("SCHEME_NAME", mdsReceiptEntryTO.getSchemeName());
                                    mdsTransMap.put("CHITTAL_NO", mdsReceiptEntryTO.getChittalNo());
                                    mdsTransMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getSubNo()));
                                    mdsTransMap.put("NO_OF_INST", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getNoOfInstPay()));
                                    mdsTransMap.put("INST_AMT", mdsReceiptEntryTO.getInstAmt());
                                    mdsTransMap.put("PENAL_AMT", mdsReceiptEntryTO.getPenalAmtPayable());
                                    mdsTransMap.put("BONUS_AMT", mdsReceiptEntryTO.getBonusAmtPayable());
                                    mdsTransMap.put("DISCOUNT_AMT", mdsReceiptEntryTO.getDiscountAmt());
                                    mdsTransMap.put("MDS_INTEREST", mdsReceiptEntryTO.getMdsInterset());
                                    mdsTransMap.put("NET_AMT", mdsReceiptEntryTO.getNetAmt());
                                    mdsTransMap.put("STATUS", CommonConstants.STATUS_CREATED);
                                    mdsTransMap.put("STATUS_DT", currDt);
                                    mdsTransMap.put("STATUS_BY", map.get("USER_ID"));
                                    mdsTransMap.put("AUTHORIZE_STATUS", CommonUtil.convertObjToStr(CommonConstants.STATUS_AUTHORIZED));
                                    mdsTransMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                                    mdsTransMap.put("AUTHORIZE_DT", currDt);
                                    mdsTransMap.put("TRANS_DT", currDt);
                                    mdsTransMap.put("NET_TRANS_ID", mdsReceiptEntryTO.getNetTransId());
                                    mdsTransMap.put("NARRATION", mdsReceiptEntryTO.getNarration());
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
                                    sqlMap.executeUpdate("updateMDSTransDetailsEachRec", mdsTransMap);
                                }                               
                            }
                        } else {
                            // Inserting to mds_receipt_entry and mds_trans_details starts                            
                            if (transMap != null && transMap.containsKey("TRANS_ID") && transMap.get("TRANS_ID") != null) {                                
                                for (int k = 0; k < allApplnDetails.size(); k++) {
                                    GDSApplicationTO objGDSApplicationTO = (GDSApplicationTO) allApplnDetails.get(k);
                                    mdsReceiptEntryTO.setGds_no(CommonUtil.convertObjToStr(dataMap.get("CHITTAL_NO")));
                                    mdsReceiptEntryTO.setChittalNo(objGDSApplicationTO.getChittalNo());
                                    mdsReceiptEntryTO.setSchemeName(objGDSApplicationTO.getSchemeName());
                                    if (isBonusTrans.equalsIgnoreCase("N")) {
                                        mdsReceiptEntryTO.setBonusAmtPayable(0.0);
                                        mdsReceiptEntryTO.setDiscountAmt(0.0);
                                    }
                                    sqlMap.executeUpdate("insertGDSReceiptEntryTO", mdsReceiptEntryTO);
                                    HashMap transactionaApplnMap = new HashMap();
                                    transactionaApplnMap.put("TOTAL_BALANCE", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getInstAmt()));
                                    transactionaApplnMap.put("CLEAR_BALANCE", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getInstAmt()));
                                    transactionaApplnMap.put("AVAILABLE_BALANCE", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getInstAmt()));
                                    transactionaApplnMap.put("SHADOW_CREDIT", mdsReceiptEntryTO.getInstAmt());
                                    transactionaApplnMap.put("SHADOW_DEBIT", mdsReceiptEntryTO.getInstAmt());
                                    transactionaApplnMap.put("SCHEME_NAME", mdsReceiptEntryTO.getSchemeName());
                                    transactionaApplnMap.put("BRANCH_CODE", _branchCode);
                                    transactionaApplnMap.put("TRANS_ID", mdsReceiptEntryTO.getNetTransId());
                                    sqlMap.executeUpdate("updateMDSAvailBalanceMap", transactionaApplnMap);
                                    sqlMap.executeUpdate("updateMDSClearBalanceMap", transactionaApplnMap);
                                    sqlMap.executeUpdate("updateMDSTotalBalanceMap", transactionaApplnMap);
                                    HashMap mdsTransMap = new HashMap();
                                    mdsTransMap.put("SCHEME_NAME", mdsReceiptEntryTO.getSchemeName());
                                    mdsTransMap.put("CHITTAL_NO", mdsReceiptEntryTO.getChittalNo());
                                    mdsTransMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getSubNo()));
                                    mdsTransMap.put("NO_OF_INST", mdsReceiptEntryTO.getNoOfInstPay());
                                    mdsTransMap.put("INST_AMT", mdsReceiptEntryTO.getInstAmt());
                                    mdsTransMap.put("PENAL_AMT", mdsReceiptEntryTO.getPenalAmtPayable());                                    
                                    if (isBonusTrans.equalsIgnoreCase("N")) { // Modified by nithya on 16-01-2020 for KD-1280
//                                        mdsTransMap.put("BONUS_AMT", 0);
//                                        mdsTransMap.put("DISCOUNT_AMT", 0);
                                          mdsTransMap.put("BONUS_AMT", new Double(0.0));
                                          mdsTransMap.put("DISCOUNT_AMT", new Double(0.0));     
                                    }else{
                                        mdsTransMap.put("BONUS_AMT", mdsReceiptEntryTO.getBonusAmtPayable());
                                        mdsTransMap.put("DISCOUNT_AMT", mdsReceiptEntryTO.getDiscountAmt());
                                    }
                                    mdsTransMap.put("MDS_INTEREST", mdsReceiptEntryTO.getMdsInterset());
                                    mdsTransMap.put("NET_AMT", mdsReceiptEntryTO.getNetAmt());
                                    //mdsTransMap.put("SUBSCRIPTION_AMT", mdsReceiptEntryTO.getInstAmtPayable()); // 01-11-2019
                                    mdsTransMap.put("STATUS", CommonConstants.STATUS_CREATED);
                                    mdsTransMap.put("STATUS_DT", currDt);
                                    mdsTransMap.put("STATUS_BY", map.get("USER_ID"));
                                    mdsTransMap.put("AUTHORIZE_STATUS", CommonUtil.convertObjToStr(CommonConstants.STATUS_AUTHORIZED));
                                    mdsTransMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                                    mdsTransMap.put("AUTHORIZE_DT", currDt);
                                    mdsTransMap.put("TRANS_DT", currDt);
                                    mdsTransMap.put("NET_TRANS_ID", mdsReceiptEntryTO.getNetTransId());
                                    mdsTransMap.put("NARRATION", mdsReceiptEntryTO.getNarration());
                                    mdsTransMap.put("GDS_NO",mdsReceiptEntryTO.getGds_no());
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
                                    sqlMap.executeUpdate("updateGDSTransDetailsEachRec", mdsTransMap);
                                }
                            }
                            // Inserting to mds_receipt_entry and mds_trans_details starts End                                        
                        }
                        if (!transMap.get("SINGLE_TRANS_ID").equals("")) {
                            returnSingleMapList.add(transMap.get("SINGLE_TRANS_ID"));
                        }
                        if (!transMap.get("TRANS_ID").equals("")) {
                            returnMapList.add(transMap.get("TRANS_ID"));
                        }
                        if (returnMapList != null && returnMapList.size() > 0) {
                            HashMap smsAlertMap = new HashMap();
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
                                        MdsTransMap.put(CommonConstants.BRANCH_ID, BRANCH_ID);
                                        smsDAO.MdsReceiptsSmsConfiguration(MdsTransMap);
                                        MdsTransList = null;
                                    }
                                }
                                MdsSmsList = null;
                            }
                        }                      
                        HashMap lastInstMap = new HashMap();
                        lastInstMap.put("SCHEME_NAME", mdsReceiptEntryTO.getSchemeName());
                        lastInstMap.put("CHITTAL_NO", mdsReceiptEntryTO.getChittalNo());
                        List LastInstList = sqlMap.executeQueryForList("getChittalLastInstDetails", lastInstMap);
                        if (LastInstList != null && LastInstList.size() > 0) {
                            lastInstMap.put("LAST_INST_DT", currDt.clone());
                            sqlMap.executeUpdate("updateMasterMaintenceCloseDt", lastInstMap);
                        }
                    }
                }
                //Added By Suresh
                List closureList = (List) sqlMap.executeQueryForList("checkSchemeClosureDetails", map);
                if (closureList != null && closureList.size() > 0) {
                    double totalSchemeAmount = 0.0;
                    double totalAmtReceived = 0.0;
                    double totalAmtPaid = 0.0;
                    HashMap whereMap = new HashMap();
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
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }
     
}