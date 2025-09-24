/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * TransferTrans.java
 *
 * Created on December 29, 2004, 2:25 PM
 */
package com.see.truetransact.serverside.common.transaction;

import java.util.HashMap;
import java.util.Date;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.serverside.transaction.transfer.*;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.ibatis.db.sqlmap.SqlMap;
import java.util.ArrayList;
import com.see.truetransact.transferobject.common.transaction.FailureTxTransferTO;

/**
 *
 * @author 152691
 */
public class TransferTrans {

    private String linkBatchId; //Value will be set in TransactionDAO
    private String initiatedBranch;
    private String transMode;
    public static final String CR_AC_HD = "CR_AC_HD";
    public static final String DR_AC_HD = "DR_AC_HD";
    public static final String CR_PROD_ID = "CR_PROD_ID";
    public static final String DR_PROD_ID = "DR_PROD_ID";
    public static final String CR_ACT_NUM = "CR_ACT_NUM";
    public static final String DR_ACT_NUM = "DR_ACT_NUM";
    public static final String DR_PROD_TYPE = "DR_PROD_TYPE";
    public static final String CR_PROD_TYPE = "CR_PROD_TYPE";
    public static final String CURRENCY = "BASE_CURRENCY";
    public static final String DR_BRANCH = "DR_BRAN";
    public static final String CR_BRANCH = "CR_BRAN";
    public static final String INITIATED_BRANCH = "INITIATED_BRANCH";
    public static String user = CommonConstants.TTSYSTEM;
    public static final String PARTICULARS = "PARTICULARS";
    private final String VOUCHER = "VOUCHER";
    public static final String INITIATOR = "INIT";
    public static final String DR_INSTRUMENT_1 = "DR_INSTRUMENT_1";
    public static final String DR_INSTRUMENT_2 = "DR_INSTRUMENT_2";
    public static final String DR_INST_TYPE = "DR_INST_TYPE";
    public static final String DR_INST_DATE = "DR_INST_DATE";
    public static final String NARRATION = "NARRATION";
    public static final String AUTHORIZE_STATUS_2 = "AUTHORIZE_STATUS_2";
    public static String initiatorType = CommonConstants.CASHIER;
    public String particulars = "";
    private static SqlMap sqlMap = null;
    private String loanDebitInt = "";
    private String commandMode = CommonConstants.TOSTATUS_INSERT;
    private boolean forProcCharge = false;
    private boolean forLodgementICC = false;
    private boolean forDebitInt = false;
    private Object oldAmount = null;
    private HashMap allAmountMap = new HashMap();
    private String actclosingmincheck;
    private String breakLoanHierachy = "N";
    private HashMap depPenalMap = new HashMap();
    private String debitLoanType = null;

    /**
     * Creates a new instance of Transaction
     */
    public TransferTrans() {
    }

    public void doDebitCredit(ArrayList batchList, String branchCode, boolean isAutoAuthorize, String commandMode) throws Exception {
        System.out.println("InsidedoDebitCredit" + batchList);
        System.out.println("InsidedoDebitCredit" + commandMode);
        setCommandMode(commandMode);
        doDebitCredit(batchList, branchCode, isAutoAuthorize);
    }

    public void doDebitCredit(ArrayList batchList, String branchCode, boolean isAutoAuthorize) throws Exception {
        TransferDAO transferDAO = new TransferDAO();
        HashMap data = new HashMap();
        if(batchList.contains("SHARE_SCREEN")){
            data.put("SHARE_SCREEN", "SHARE_SCREEN");
            batchList.remove("SHARE_SCREEN");
        }
        if(batchList.contains("DEPOSIT_TO_LOAN")){
            data.put("DEPOSIT_TO_LOAN", "DEPOSIT_TO_LOAN");
            batchList.remove("DEPOSIT_TO_LOAN");
        }
        
        if(batchList.contains("GOLD_LOAN_RENEWAL")){ // Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
            data.put("GOLD_LOAN_RENEWAL", "GOLD_LOAN_RENEWAL");
            batchList.remove("GOLD_LOAN_RENEWAL");
        }
        
        data.put("TxTransferTO", batchList);
        data.put("COMMAND", getCommandMode());
        data.put("OLDAMOUNT", getOldAmount());
        data.put("LINK_BATCH_ID", getLinkBatchId());
        data.put("INITIATED_BRANCH", getInitiatedBranch());
        data.put(CommonConstants.BRANCH_ID, branchCode);
        data.put(CommonConstants.USER_ID, user);
        data.put(CommonConstants.MODULE, "Transaction");
        data.put(CommonConstants.SCREEN, "");
        if (breakLoanHierachy != null && breakLoanHierachy.equals("Y")) {
            data.put(CommonConstants.BREAK_LOAN_HIERARCHY, "Y");
        } else {
            data.put(CommonConstants.BREAK_LOAN_HIERARCHY, "N");
        }

        if (getAllAmountMap() != null && getAllAmountMap().size() > 0) {
            data.put("ALL_AMOUNT", getAllAmountMap());
            if (getAllAmountMap().containsKey("STRING_REBATE_INTEREST")) {
                data.put("REBATE_INTEREST", "Y");
            }
            if (getAllAmountMap().containsKey("LOAN_FROM_CHARGESUI")) {
                data.put("LOAN_FROM_CHARGESUI", "");
            }
            if (getAllAmountMap().containsKey("CORP_LOAN_MAP")) {
                data.put("CORP_LOAN_MAP", getAllAmountMap().get("CORP_LOAN_MAP"));
            }
        }
        if (loanDebitInt != null && loanDebitInt.length() > 0) {
            data.put("LOAN_PARTICULARS", loanDebitInt);
        }
        if(debitLoanType != null &&  debitLoanType.length() > 0){
              data.put("DEBIT_LOAN_TYPE", debitLoanType);
        }
        transferDAO.setForLoanDebitInt(isForDebitInt());
     // System.out.println("doDebitCreditdata" + data);
        if (isAutoAuthorize) {
            HashMap authorizeMap = new HashMap();
            authorizeMap.put("BATCH_ID", null);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
            data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
        }
        if (isForProcCharge()) {
            transferDAO.setForProcCharge(isForProcCharge());
        }
        if (isForLodgementICC()) {
            transferDAO.setForProcCharge(isForLodgementICC());
        }
        if (getDepPenalMap() != null && getDepPenalMap().size() > 0) {
            data.put("DEPOSIT_PENAL_MONTH", getDepPenalMap().get("DEPOSIT_PENAL_MONTH"));
            data.put("DEPOSIT_PENAL_AMT", getDepPenalMap().get("DEPOSIT_PENAL_AMT"));
        }
        
        
        transferDAO.execute(data, false);
        forDebitInt = false;
        setAllAmountMap(new HashMap());
        setDepPenalMap(new HashMap());
    }

    //This method is to be called in Authorize method.
    public void doTransferAuthorize(ArrayList batchList, HashMap paramMap) throws Exception {
        TransferDAO transferDAO = new TransferDAO();
//BRANCH_ID, USER_ID, need to come in param Map
        paramMap.put(CommonConstants.MODULE, "Transaction");
        paramMap.put(CommonConstants.SCREEN, "");
        HashMap authorizeMap = new HashMap();
        authorizeMap.put("BATCH_ID", paramMap.get("BATCH_ID"));
        authorizeMap.put(CommonConstants.AUTHORIZESTATUS, paramMap.get(CommonConstants.AUTHORIZESTATUS));
        authorizeMap.put(CommonConstants.AUTHORIZEDATA, batchList);
        if (paramMap.containsKey("REMARKS")) {
            authorizeMap.put("REMARKS", paramMap.get("REMARKS"));
        } else {
            if (paramMap.containsKey("LINK_BATCH_ID")) {
                paramMap.remove("LINK_BATCH_ID"); // Changed for deposit closing authorize time no need for link batch id.... 
            }
        }
        if (paramMap.containsKey(CommonConstants.BRANCH_ID)) {
            authorizeMap.put(CommonConstants.BRANCH_ID, paramMap.get(CommonConstants.BRANCH_ID));
        }
        paramMap.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
        if (loanDebitInt != null && loanDebitInt.length() > 0) {
            paramMap.put("LOAN_PARTICULARS", loanDebitInt);
        }
        System.out.println("###### doTransferAuthorize paramMap : " + paramMap);
        System.out.println("transferTrans###" + batchList);
        transferDAO.execute(paramMap, false);
        loanDebitInt = null;
    }

    public void doDebitCredit(ArrayList batchList, String branchCode) throws Exception {
        doDebitCredit(batchList, branchCode, true);
    }

    public TxTransferTO getDebitTransferTO(HashMap resultMap, double amount) {
        //System.out.println("########DebitTransferTO" + resultMap);
        FailureTxTransferTO objTxTransferTO = new FailureTxTransferTO();
        try {
            objTxTransferTO.setAcHdId(CommonUtil.convertObjToStr(resultMap.get(DR_AC_HD)));
            objTxTransferTO.setProdId(CommonUtil.convertObjToStr(resultMap.get(DR_PROD_ID)));
            objTxTransferTO.setActNum(CommonUtil.convertObjToStr(resultMap.get(DR_ACT_NUM)));
            objTxTransferTO.setInpAmount(CommonUtil.convertObjToDouble(String.valueOf(amount)));
            objTxTransferTO.setInpCurr(CommonUtil.convertObjToStr(resultMap.get(CURRENCY)));

            objTxTransferTO.setAmount(CommonUtil.convertObjToDouble(String.valueOf(amount)));
            objTxTransferTO.setTransType(CommonConstants.DEBIT);
            if (resultMap.containsKey("LINK_BATCH_ID")) {
                objTxTransferTO.setLinkBatchId((String) resultMap.get("LINK_BATCH_ID"));
            }
            if (resultMap.containsKey("NARRATION")) {
                objTxTransferTO.setNarration(CommonUtil.convertObjToStr(resultMap.get("NARRATION")));
            }
            if (resultMap.containsKey("DR_INSTRUMENT_2")) {
                objTxTransferTO.setInstrumentNo2(CommonUtil.convertObjToStr(resultMap.get("DR_INSTRUMENT_2")));
            }
            if (resultMap.containsKey("generateSingleTransId")) {
                objTxTransferTO.setSingleTransId(CommonUtil.convertObjToStr(resultMap.get("generateSingleTransId")));
            }
          if (resultMap.containsKey("DR_INSTRUMENT_1")) {
            objTxTransferTO.setInstrumentNo1(CommonUtil.convertObjToStr(resultMap.get(DR_INSTRUMENT_1)));
          }
        //generate the Instrument nos below
//            if(objTxTransferTO.getInstType()!=null && !CommonUtil.convertObjToStr(objTxTransferTO.getInstType()).equals("")){
            if (resultMap.containsKey(DR_INST_TYPE) && CommonUtil.convertObjToStr(resultMap.get(DR_INST_TYPE)).length() > 0) {
                objTxTransferTO.setInstType(CommonUtil.convertObjToStr(resultMap.get(DR_INST_TYPE)));
                objTxTransferTO.setInstrumentNo1(CommonUtil.convertObjToStr(resultMap.get(DR_INSTRUMENT_1)));
                objTxTransferTO.setInstrumentNo2(CommonUtil.convertObjToStr(resultMap.get(DR_INSTRUMENT_2)));
            } else {
                objTxTransferTO.setInstType(VOUCHER);
            }
             if (resultMap.containsKey(DR_INST_TYPE) && CommonUtil.convertObjToStr(resultMap.get(DR_INST_TYPE)).equals("AB_CHEQUE")) {
                 objTxTransferTO.setInstType("");
             }
            if (resultMap.get(DR_INST_DATE) != null) {
                objTxTransferTO.setInstDt((Date) resultMap.get(DR_INST_DATE));
            } else {
                objTxTransferTO.setInstDt(ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(resultMap.get(DR_BRANCH))));
            }

            objTxTransferTO.setInitChannType(null);
            objTxTransferTO.setParticulars(null);

            objTxTransferTO.setProdType((String) resultMap.get(DR_PROD_TYPE));

            if (resultMap.containsKey(CommonConstants.USER_ID)) {
                user = (String) resultMap.get(CommonConstants.USER_ID);
            }

            if (resultMap.containsKey(INITIATOR)) {
                initiatorType = (String) resultMap.get(INITIATOR);
            }

            if (resultMap.containsKey(PARTICULARS)) {
                particulars = "To " + (String) resultMap.get(PARTICULARS);
            }

            if (resultMap.containsKey(AUTHORIZE_STATUS_2)) {
                objTxTransferTO.setAuthorizeStatus_2((String) resultMap.get(AUTHORIZE_STATUS_2));
            }

            if (resultMap.containsKey("AUTHORIZEREMARKS")) {
                objTxTransferTO.setAuthorizeRemarks((String) resultMap.get("AUTHORIZEREMARKS"));
            }

            if (resultMap.containsKey("HIERARCHY")) {
                objTxTransferTO.setHierarchyLevel((String) resultMap.get("HIERARCHY"));
            }
            if(resultMap.containsKey("GL_TRANS_ACT_NUM")){
                objTxTransferTO.setGlTransActNum((String) resultMap.get("GL_TRANS_ACT_NUM"));
            }

            objTxTransferTO.setBranchId(CommonUtil.convertObjToStr(resultMap.get(DR_BRANCH)));
            objTxTransferTO.setStatusDt(ServerUtil.getCurrentDate(objTxTransferTO.getBranchId()));
            objTxTransferTO.setTransDt(ServerUtil.getCurrentDate(objTxTransferTO.getBranchId()));
            //System.out.println("getBranchId : "+objTxTransferTO.getBranchId());
            //System.out.println("getInitiatedBranch : "+objTxTransferTO.getInitiatedBranch());
            if (resultMap.containsKey("DR_INSTRUMENT_2") && resultMap.get("DR_INSTRUMENT_2") != null && 
                CommonUtil.convertObjToStr(resultMap.get("DR_INSTRUMENT_2")).length()>0 && 
                CommonUtil.convertObjToStr(resultMap.get("DR_INSTRUMENT_2")).equals("Deposit Closure")) {
                System.out.println("getInitiatedBranch : "+resultMap.get(INITIATED_BRANCH));
                objTxTransferTO.setTransDt(ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(resultMap.get(INITIATED_BRANCH))));
            }
            if (resultMap.containsKey("DR_INSTRUMENT_2") && resultMap.get("DR_INSTRUMENT_2") != null && 
                CommonUtil.convertObjToStr(resultMap.get("DR_INSTRUMENT_2")).length()>0 && 
                CommonUtil.convertObjToStr(resultMap.get("DR_INSTRUMENT_2")).equals("Deposit Account Renewal")) {
                System.out.println("getInitiatedBranch : "+resultMap.get(INITIATED_BRANCH));
                objTxTransferTO.setTransDt(ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(resultMap.get(INITIATED_BRANCH))));
            }
            
            if (resultMap.containsKey("TERM_DEPOSIT_INTER_BRANCH_TRANS") && resultMap.get("TERM_DEPOSIT_INTER_BRANCH_TRANS") != null && 
                CommonUtil.convertObjToStr(resultMap.get("TERM_DEPOSIT_INTER_BRANCH_TRANS")).length()>0 && 
                CommonUtil.convertObjToStr(resultMap.get("TERM_DEPOSIT_INTER_BRANCH_TRANS")).equals("TERM_DEPOSIT_INTER_BRANCH_TRANS")) {
                System.out.println("getInitiatedBranch : "+resultMap.get(INITIATED_BRANCH));
                objTxTransferTO.setTransDt(ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(resultMap.get(INITIATED_BRANCH))));
            }
            
            // Added by nithya on 01-03-2019 for KD 196 - 0014990: InterBranch Transaction Issues.
            if (resultMap.containsKey("WAIVE_OFF_INTER_BRANCH_TRANS") && resultMap.get("WAIVE_OFF_INTER_BRANCH_TRANS") != null && 
                CommonUtil.convertObjToStr(resultMap.get("WAIVE_OFF_INTER_BRANCH_TRANS")).length()>0 && 
                CommonUtil.convertObjToStr(resultMap.get("WAIVE_OFF_INTER_BRANCH_TRANS")).equals("WAIVE_OFF_INTER_BRANCH_TRANS")) {
                //System.out.println("getInitiatedBranch : "+resultMap.get(INITIATED_BRANCH));
                objTxTransferTO.setTransDt(ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(resultMap.get(INITIATED_BRANCH))));
            }
            // End of KD 196
            
            // Interbranch gold loan closing issue reported after testing of jira KD-196 
            if (resultMap.containsKey("ACCOUNT_CLOSING_INTER_BRANCH_TRANS") && resultMap.get("ACCOUNT_CLOSING_INTER_BRANCH_TRANS") != null
                    && CommonUtil.convertObjToStr(resultMap.get("ACCOUNT_CLOSING_INTER_BRANCH_TRANS")).length() > 0
                    && CommonUtil.convertObjToStr(resultMap.get("ACCOUNT_CLOSING_INTER_BRANCH_TRANS")).equals("ACCOUNT_CLOSING_INTER_BRANCH_TRANS")) {
                objTxTransferTO.setTransDt(ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(resultMap.get(INITIATED_BRANCH))));
            }

            // Added by nithya on 10-10-2019 for KD-653
            if (resultMap.containsKey("BILLS_INTER_BRANCH_TRANS") && resultMap.get("BILLS_INTER_BRANCH_TRANS") != null
                    && CommonUtil.convertObjToStr(resultMap.get("BILLS_INTER_BRANCH_TRANS")).length() > 0
                    && CommonUtil.convertObjToStr(resultMap.get("BILLS_INTER_BRANCH_TRANS")).equals("BILLS_INTER_BRANCH_TRANS")) {
                objTxTransferTO.setTransDt(ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(resultMap.get(INITIATED_BRANCH))));
            }


            if (resultMap.containsKey("ATMTRANS_BRANCH_ID") && resultMap.get("ATMTRANS_BRANCH_ID") != null
                    && CommonUtil.convertObjToStr(resultMap.get("ATMTRANS_BRANCH_ID")).length() > 0) {
                objTxTransferTO.setTransDt(ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(resultMap.get("ATMTRANS_BRANCH_ID"))));
            }

            
            if (resultMap.containsKey("FROM_RESERVE_SCREEN") && resultMap.get("FROM_RESERVE_SCREEN") != null
                    && CommonUtil.convertObjToStr(resultMap.get("FROM_RESERVE_SCREEN")).length() > 0
                    && CommonUtil.convertObjToStr(resultMap.get("FROM_RESERVE_SCREEN")).equals("FROM_RESERVE_SCREEN")) {
                objTxTransferTO.setTransDt((Date) resultMap.get("RESERVE_TRANS_DATE"));
            }

            if (resultMap.containsKey("SALARY_PROCESS_RD_INTER_BRANCH_TRANS") && resultMap.get("SALARY_PROCESS_RD_INTER_BRANCH_TRANS") != null && 
                CommonUtil.convertObjToStr(resultMap.get("SALARY_PROCESS_RD_INTER_BRANCH_TRANS")).length()>0 && 
                CommonUtil.convertObjToStr(resultMap.get("SALARY_PROCESS_RD_INTER_BRANCH_TRANS")).equals("SALARY_PROCESS_RD_INTER_BRANCH_TRANS")) {                
                objTxTransferTO.setTransDt(ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(resultMap.get(INITIATED_BRANCH))));
            }
            
            //For KD-3434 - Gold loan payment charge
            if (resultMap.containsKey("GOLD_LOAN_INTER_BRANCH_TRANS") && resultMap.get("GOLD_LOAN_INTER_BRANCH_TRANS") != null && 
                CommonUtil.convertObjToStr(resultMap.get("GOLD_LOAN_INTER_BRANCH_TRANS")).length()>0 && 
                CommonUtil.convertObjToStr(resultMap.get("GOLD_LOAN_INTER_BRANCH_TRANS")).equals("GOLD_LOAN_INTER_BRANCH_TRANS")) {                
                objTxTransferTO.setTransDt(ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(resultMap.get(INITIATED_BRANCH))));
            }
            
            objTxTransferTO.setTransModType(CommonUtil.convertObjToStr(resultMap.get("TRANS_MOD_TYPE")));
            if (resultMap.containsKey("SCREEN_NAME")) {
                objTxTransferTO.setScreenName(CommonUtil.convertObjToStr(resultMap.get("SCREEN_NAME")));
            }
            objTxTransferTO = setTransCommonParam(objTxTransferTO);
            if (resultMap != null && resultMap.containsKey("DEBIT_LOAN_TYPE")) {
                debitLoanType = CommonUtil.convertObjToStr(resultMap.get("DEBIT_LOAN_TYPE"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objTxTransferTO;
    }

    private FailureTxTransferTO setTransCommonParam(FailureTxTransferTO objTxTransferTO) throws Exception {
   //     System.out.println("&&&&& FailureTxTransferTO objTxTransferTO " + objTxTransferTO);

        objTxTransferTO.setBatchId(null);
        objTxTransferTO.setTransId(null);
        objTxTransferTO.setInitTransId(user);
        objTxTransferTO.setInitChannType(initiatorType);
        objTxTransferTO.setParticulars(particulars);
        objTxTransferTO.setInitiatedBranch(getInitiatedBranch());
        objTxTransferTO.setStatus(CommonConstants.STATUS_CREATED);
        objTxTransferTO.setStatusBy(user);
        objTxTransferTO.setTransModType(objTxTransferTO.getTransModType());

        if (!CommonUtil.convertObjToStr(getTransMode()).equals("")) {
            objTxTransferTO.setTransMode(getTransMode());
        } else {
            objTxTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
        }
        objTxTransferTO.setScreenName(objTxTransferTO.getScreenName());
        objTxTransferTO.setAuthorizeRemarks(objTxTransferTO.getAuthorizeRemarks());
        // If AcHdId is not passed... retrive from the database based on Product Type and Prod ID
        if ((objTxTransferTO.getAcHdId() == null || objTxTransferTO.getAcHdId().trim().equals(""))
                && objTxTransferTO.getProdId() != null && !objTxTransferTO.getProdId().trim().equals("") && !objTxTransferTO.getProdType().equals("GL")) {
            ServiceLocator locate = ServiceLocator.getInstance();
            sqlMap = (SqlMap) locate.getDAOSqlMap();
            String qry = "getAccountHead" + objTxTransferTO.getProdType();
            objTxTransferTO.setAcHdId((String) sqlMap.executeQueryForObject(qry, objTxTransferTO.getProdId()));
        }
        particulars = "";
      //  System.out.println("##### setTransCommonParam objTxTransferTO" + objTxTransferTO);
        return objTxTransferTO;
    }

    public TxTransferTO getCreditTransferTO(HashMap resultMap, double amount) {
    //    System.out.println("########CreditTransferTO" + resultMap);
        FailureTxTransferTO objTxTransferTO = new FailureTxTransferTO();
        try {
            objTxTransferTO.setAcHdId(CommonUtil.convertObjToStr(resultMap.get(CR_AC_HD)));
            objTxTransferTO.setActNum(CommonUtil.convertObjToStr(resultMap.get(CR_ACT_NUM)));
            objTxTransferTO.setInpAmount(CommonUtil.convertObjToDouble(String.valueOf(amount)));
            objTxTransferTO.setInpCurr(CommonUtil.convertObjToStr(resultMap.get(CURRENCY)));
            objTxTransferTO.setAmount(CommonUtil.convertObjToDouble(String.valueOf(amount)));
            objTxTransferTO.setTransType(CommonConstants.CREDIT);
            objTxTransferTO.setBranchId(CommonUtil.convertObjToStr(resultMap.get(CR_BRANCH)));
            objTxTransferTO.setStatusDt(ServerUtil.getCurrentDate(objTxTransferTO.getBranchId()));
			objTxTransferTO.setTransDt(ServerUtil.getCurrentDate(objTxTransferTO.getBranchId()));
            if (resultMap.containsKey("DR_INSTRUMENT_2") && resultMap.get("DR_INSTRUMENT_2") != null && 
                CommonUtil.convertObjToStr(resultMap.get("DR_INSTRUMENT_2")).length()>0 && 
                CommonUtil.convertObjToStr(resultMap.get("DR_INSTRUMENT_2")).equals("Deposit Closure")) {
                System.out.println("getInitiatedBranch : "+resultMap.get(INITIATED_BRANCH));
                objTxTransferTO.setTransDt(ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(resultMap.get(INITIATED_BRANCH))));
            }
            if (resultMap.containsKey("DR_INSTRUMENT_2") && resultMap.get("DR_INSTRUMENT_2") != null && 
                CommonUtil.convertObjToStr(resultMap.get("DR_INSTRUMENT_2")).length()>0 && 
                CommonUtil.convertObjToStr(resultMap.get("DR_INSTRUMENT_2")).equals("Deposit Account Renewal")) {
                System.out.println("getInitiatedBranch : "+resultMap.get(INITIATED_BRANCH));
                objTxTransferTO.setTransDt(ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(resultMap.get(INITIATED_BRANCH))));
            }
            // Added by nithya for 0009241: interbranch related issues (different date issue) [ share ]
            if (resultMap.containsKey("SHARE_INTER_BRANCH_TRANSACTION") && resultMap.get("SHARE_INTER_BRANCH_TRANSACTION") != null && 
                CommonUtil.convertObjToStr(resultMap.get("SHARE_INTER_BRANCH_TRANSACTION")).length()>0 && 
                CommonUtil.convertObjToStr(resultMap.get("SHARE_INTER_BRANCH_TRANSACTION")).equals("SHARE_INTER_BRANCH_TRANSACTION")) {
                System.out.println("getInitiatedBranch : "+resultMap.get(INITIATED_BRANCH));
                objTxTransferTO.setTransDt(ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(resultMap.get(INITIATED_BRANCH))));
            }
            // Added by nithya for 0009241: interbranch related issues (different date issue) [ loans payment ]
            if (resultMap.containsKey("TERM_LOANS_INTER_BRANCH_TRANS") && resultMap.get("TERM_LOANS_INTER_BRANCH_TRANS") != null && 
                CommonUtil.convertObjToStr(resultMap.get("TERM_LOANS_INTER_BRANCH_TRANS")).length()>0 && 
                CommonUtil.convertObjToStr(resultMap.get("TERM_LOANS_INTER_BRANCH_TRANS")).equals("TERM_LOANS_INTER_BRANCH_TRANS")) {                
                objTxTransferTO.setTransDt(ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(resultMap.get(INITIATED_BRANCH))));
            }
            
            // Added by nithya on 03-10-2018 for KD 270 - Gold Loan Renewal - Now it is not possible to do the interbranch transaction Credit(Savings Bank of Other Branch)
            if (resultMap.containsKey("GOLD_LOAN_INTER_BRANCH_TRANS") && resultMap.get("GOLD_LOAN_INTER_BRANCH_TRANS") != null && 
                CommonUtil.convertObjToStr(resultMap.get("GOLD_LOAN_INTER_BRANCH_TRANS")).length()>0 && 
                CommonUtil.convertObjToStr(resultMap.get("GOLD_LOAN_INTER_BRANCH_TRANS")).equals("GOLD_LOAN_INTER_BRANCH_TRANS")) {                
                objTxTransferTO.setTransDt(ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(resultMap.get(INITIATED_BRANCH))));
            }
            // End KD 270
            
            // Added by nithya on 01-03-2019 for KD 196 - 0014990: InterBranch Transaction Issues.
            if (resultMap.containsKey("WAIVE_OFF_INTER_BRANCH_TRANS") && resultMap.get("WAIVE_OFF_INTER_BRANCH_TRANS") != null && 
                CommonUtil.convertObjToStr(resultMap.get("WAIVE_OFF_INTER_BRANCH_TRANS")).length()>0 && 
                CommonUtil.convertObjToStr(resultMap.get("WAIVE_OFF_INTER_BRANCH_TRANS")).equals("WAIVE_OFF_INTER_BRANCH_TRANS")) {
                //System.out.println("getInitiatedBranch : "+resultMap.get(INITIATED_BRANCH));
                objTxTransferTO.setTransDt(ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(resultMap.get(INITIATED_BRANCH))));
            }
            // End
            
             // Added by nithya on 10-10-2019 for KD-653
             if (resultMap.containsKey("BILLS_INTER_BRANCH_TRANS") && resultMap.get("BILLS_INTER_BRANCH_TRANS") != null && 
                CommonUtil.convertObjToStr(resultMap.get("BILLS_INTER_BRANCH_TRANS")).length()>0 && 
                CommonUtil.convertObjToStr(resultMap.get("BILLS_INTER_BRANCH_TRANS")).equals("BILLS_INTER_BRANCH_TRANS")) {
                //System.out.println("getInitiatedBranch : "+resultMap.get(INITIATED_BRANCH));
                objTxTransferTO.setTransDt(ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(resultMap.get(INITIATED_BRANCH))));
            }
            
            if (resultMap.containsKey("ATMTRANS_BRANCH_ID") && resultMap.get("ATMTRANS_BRANCH_ID") != null && 
                CommonUtil.convertObjToStr(resultMap.get("ATMTRANS_BRANCH_ID")).length()>0) {
                System.out.println("ATMTRANS_BRANCH_ID : "+resultMap.get("ATMTRANS_BRANCH_ID"));
                objTxTransferTO.setTransDt(ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(resultMap.get("ATMTRANS_BRANCH_ID"))));
            }
            
            //Added by nithya on 18-11-2021 for KD-3124
            if (resultMap.containsKey("FROM_RESERVE_SCREEN") && resultMap.get("FROM_RESERVE_SCREEN") != null
                    && CommonUtil.convertObjToStr(resultMap.get("FROM_RESERVE_SCREEN")).length() > 0
                    && CommonUtil.convertObjToStr(resultMap.get("FROM_RESERVE_SCREEN")).equals("FROM_RESERVE_SCREEN")) {
                objTxTransferTO.setTransDt((Date) resultMap.get("RESERVE_TRANS_DATE"));
            }
            
           if (resultMap.containsKey("SALARY_PROCESS_RD_INTER_BRANCH_TRANS") && resultMap.get("SALARY_PROCESS_RD_INTER_BRANCH_TRANS") != null && 
                CommonUtil.convertObjToStr(resultMap.get("SALARY_PROCESS_RD_INTER_BRANCH_TRANS")).length()>0 && 
                CommonUtil.convertObjToStr(resultMap.get("SALARY_PROCESS_RD_INTER_BRANCH_TRANS")).equals("SALARY_PROCESS_RD_INTER_BRANCH_TRANS")) {                
                objTxTransferTO.setTransDt(ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(resultMap.get(INITIATED_BRANCH))));
            }
            
            objTxTransferTO.setProdType((String) resultMap.get(CR_PROD_TYPE));
            objTxTransferTO.setProdId((String) resultMap.get(CR_PROD_ID));
            if (resultMap.containsKey("LINK_BATCH_ID")) {
                objTxTransferTO.setLinkBatchId((String) resultMap.get("LINK_BATCH_ID"));
            }
            if (resultMap.containsKey("NARRATION")) {
                objTxTransferTO.setNarration(CommonUtil.convertObjToStr(resultMap.get("NARRATION")));
            }
            if (resultMap.containsKey("generateSingleTransId")) {
                objTxTransferTO.setSingleTransId(CommonUtil.convertObjToStr(resultMap.get("generateSingleTransId")));
            }
            if (resultMap.containsKey(DR_INSTRUMENT_1)) {
                objTxTransferTO.setInstrumentNo1((String) resultMap.get(DR_INSTRUMENT_1));
            }

            if (resultMap.containsKey(DR_INSTRUMENT_2)) {
                objTxTransferTO.setInstrumentNo2((String) resultMap.get(DR_INSTRUMENT_2));
            }

            if (resultMap.containsKey(AUTHORIZE_STATUS_2)) {
                objTxTransferTO.setAuthorizeStatus_2((String) resultMap.get(AUTHORIZE_STATUS_2));
            }

            if (resultMap.containsKey(PARTICULARS)) {
                particulars = "By " + (String) resultMap.get(PARTICULARS);
            }

            if (resultMap.containsKey("AUTHORIZEREMARKS")) {
                objTxTransferTO.setAuthorizeRemarks((String) resultMap.get("AUTHORIZEREMARKS"));
            }

            if (resultMap.containsKey(CommonConstants.USER_ID)) {
                user = (String) resultMap.get(CommonConstants.USER_ID);
            }

            if (resultMap.containsKey("HIERARCHY")) {
                objTxTransferTO.setHierarchyLevel((String) resultMap.get("HIERARCHY"));
            }
            if(resultMap.containsKey("GL_TRANS_ACT_NUM")){
                objTxTransferTO.setGlTransActNum((String) resultMap.get("GL_TRANS_ACT_NUM"));
            }
             if (resultMap.containsKey("SCREEN_NAME")) {
                objTxTransferTO.setScreenName(CommonUtil.convertObjToStr(resultMap.get("SCREEN_NAME")));
            }
            objTxTransferTO = setTransCommonParam(objTxTransferTO);
            if (resultMap.containsKey("USER")) {
                objTxTransferTO.setInitTransId(CommonUtil.convertObjToStr(resultMap.get("USER")));
                objTxTransferTO.setStatusBy(CommonUtil.convertObjToStr(resultMap.get("USER")));
            }
            if (resultMap.containsKey(DR_INST_TYPE)) {
                objTxTransferTO.setInstType((String) resultMap.get(DR_INST_TYPE));
            }
            objTxTransferTO.setTransModType(CommonUtil.convertObjToStr(resultMap.get("TRANS_MOD_TYPE")));
            // Added by nithya on 27-01-2017 for 0005736: LOANS_DISBURSEMENT TABLE ,DATA INSERTION ISSUE
            if (resultMap != null && resultMap.containsKey("DEBIT_GOLDLOAN_TYPE") && resultMap.get("DEBIT_GOLDLOAN_TYPE") != null) {
                debitLoanType = CommonUtil.convertObjToStr(resultMap.get("DEBIT_GOLDLOAN_TYPE"));
            }
            // End
           
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objTxTransferTO;
    }

    /**
     * Getter for property linkBatchId.
     *
     * @return Value of property linkBatchId.
     */
    public java.lang.String getLinkBatchId() {
        return linkBatchId;
    }

    /**
     * Setter for property linkBatchId.
     *
     * @param linkBatchId New value of property linkBatchId.
     */
    public void setLinkBatchId(java.lang.String linkBatchId) {
        this.linkBatchId = linkBatchId;
    }

    /**
     * Getter for property commandMode.
     *
     * @return Value of property commandMode.
     */
    private java.lang.String getCommandMode() {
        return commandMode;
    }

    /**
     * Setter for property commandMode.
     *
     * @param commandMode New value of property commandMode.
     */
    private void setCommandMode(java.lang.String commandMode) {
        this.commandMode = commandMode;
    }

    /**
     * Getter for property initiatedBranch.
     *
     * @return Value of property initiatedBranch.
     */
    public java.lang.String getInitiatedBranch() {
        return initiatedBranch;
    }

    /**
     * Setter for property initiatedBranch.
     *
     * @param initiatedBranch New value of property initiatedBranch.
     */
    public void setInitiatedBranch(java.lang.String initiatedBranch) {
        this.initiatedBranch = initiatedBranch;
    }

    /**
     * Getter for property transMode.
     *
     * @return Value of property transMode.
     */
    public java.lang.String getTransMode() {
        return transMode;
    }

    /**
     * Setter for property transMode.
     *
     * @param transMode New value of property transMode.
     */
    public void setTransMode(java.lang.String transMode) {
        this.transMode = transMode;
    }

    /**
     * Getter for property forProcCharge.
     *
     * @return Value of property forProcCharge.
     */
    public boolean isForProcCharge() {
        return forProcCharge;
    }

    /**
     * Setter for property forProcCharge.
     *
     * @param forProcCharge New value of property forProcCharge.
     */
    public void setForProcCharge(boolean forProcCharge) {
        this.forProcCharge = forProcCharge;
    }

    /**
     * Getter for property forDebitInt.
     *
     * @return Value of property forDebitInt.
     */
    public boolean isForDebitInt() {
        return forDebitInt;
    }

    /**
     * Setter for property forDebitInt.
     *
     * @param forDebitInt New value of property forDebitInt.
     */
    public void setForDebitInt(boolean forDebitInt) {
        this.forDebitInt = forDebitInt;
    }

    /**
     * Getter for property oldAmount.
     *
     * @return Value of property oldAmount.
     */
    public java.lang.Object getOldAmount() {
        return oldAmount;
    }

    /**
     * Setter for property oldAmount.
     *
     * @param oldAmount New value of property oldAmount.
     */
    public void setOldAmount(java.lang.Object oldAmount) {
        this.oldAmount = oldAmount;
    }

    /**
     * Getter for property loanDebitInt.
     *
     * @return Value of property loanDebitInt.
     */
    public java.lang.String getLoanDebitInt() {
        return loanDebitInt;
    }

    /**
     * Setter for property loanDebitInt.
     *
     * @param loanDebitInt New value of property loanDebitInt.
     */
    public void setLoanDebitInt(java.lang.String loanDebitInt) {
        this.loanDebitInt = loanDebitInt;
    }

    /**
     * Getter for property allAmountMap.
     *
     * @return Value of property allAmountMap.
     */
    public java.util.HashMap getAllAmountMap() {
        return allAmountMap;
    }

    /**
     * Setter for property allAmountMap.
     *
     * @param allAmountMap New value of property allAmountMap.
     */
    public void setAllAmountMap(java.util.HashMap allAmountMap) {
        this.allAmountMap = allAmountMap;
    }

    /**
     * Getter for property forLodgementICC.
     *
     * @return Value of property forLodgementICC.
     */
    public boolean isForLodgementICC() {
        return forLodgementICC;
    }

    /**
     * Setter for property forLodgementICC.
     *
     * @param forLodgementICC New value of property forLodgementICC.
     */
    public void setForLodgementICC(boolean forLodgementICC) {
        this.forLodgementICC = forLodgementICC;
    }

    /**
     * Getter for property actclosingmincheck.
     *
     * @return Value of property actclosingmincheck.
     */
    public java.lang.String getActclosingmincheck() {
        return actclosingmincheck;
    }

    /**
     * Setter for property actclosingmincheck.
     *
     * @param actclosingmincheck New value of property actclosingmincheck.
     */
    public void setActclosingmincheck(java.lang.String actclosingmincheck) {
        this.actclosingmincheck = actclosingmincheck;
    }

    /**
     * Getter for property breakLoanHierachy.
     *
     * @return Value of property breakLoanHierachy.
     */
    public java.lang.String getBreakLoanHierachy() {
        return breakLoanHierachy;
    }

    /**
     * Setter for property breakLoanHierachy.
     *
     * @param breakLoanHierachy New value of property breakLoanHierachy.
     */
    public void setBreakLoanHierachy(java.lang.String breakLoanHierachy) {
        this.breakLoanHierachy = breakLoanHierachy;
    }

    public HashMap getDepPenalMap() {
        return depPenalMap;
    }

    public void setDepPenalMap(HashMap depPenalMap) {
        this.depPenalMap = depPenalMap;
    }

    public String getDebitLoanType() {
        return debitLoanType;
    }

    public void setDebitLoanType(String debitLoanType) {
        this.debitLoanType = debitLoanType;
    }
    
}
