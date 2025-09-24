/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * StandingInstructionTask.java
 *
 * Created on July 23, 2004, 10:55 AM
 */
package com.see.truetransact.serverside.batchprocess.task.supporting.standinginstruction;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Date;
import java.util.GregorianCalendar;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import java.util.GregorianCalendar;
import java.util.Calendar;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.transaction.transfer.*;
import com.see.truetransact.transferobject.batchprocess.supporting.standinginstruction.StandingInstructionTaskTO;
import com.see.truetransact.transferobject.supporting.standinginstruction.StandingInstructionTO;
import com.see.truetransact.transferobject.supporting.standinginstruction.StandingInstructionCreditTO;
import com.see.truetransact.transferobject.supporting.standinginstruction.StandingInstructionDebitTO;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.remittance.RemittanceIssueDAO;
import com.see.truetransact.transferobject.remittance.RemittanceIssueTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.common.viewall.SelectAllDAO;

/**
 *
 * @author shanmuga MODIFIED : Sunil, 17 Mar 2005 Changes done : a. Removed
 * sqlMapBatch
 *
 */
public class StandingInstructionTask extends Task {

    private static SqlMap sqlMap = null;
    private TransferDAO transferDAO = new TransferDAO();
    private final String AC_HD_ID = "AC_HD_ID";
    private final String ACT_NUM = "ACT_NUM";
    private final String BRANCH_CODE = "BRANCH_CODE";
    private final String COMMAND = "COMMAND";
    private final String BASE_CURRENCY = "BASE_CURRENCY";
    private final String EXEC_DT = "EXEC_DT";
    private final String FREQUENCY = "FREQUENCY";
    private final String GRACE_DAYS = "GRACE_DAYS";
    private final String INSTALLMENT = "INSTALLMENT";
    private final String MODULE = "Supporting";
    private final String NEXT_INSTALL = "NEXT_INSTALL";
    private final String PROD_ID = "PROD_ID";
    private final String SCREEN = "StandingInstruction";
    private final String SI_ID = "SI_ID";
    private final String SI_START_DT = "SI_START_DT";
    private final String SI_END_DT = "SI_END_DT";
    private String branchID = "";
    private String userID = "";
    private String prodType = "";
    private Date checkThisCDate = null;
    private boolean isVariable = false;
    private double debitAmt = 0;
    private boolean chkPrevDayHol = false;
    // private boolean depValidate = true;
    private Date prevDt = null;
    private Date curDate = null;
    private double installment = 0;
    HashMap batch = new HashMap();
    private String taskLable;
    private String dayEndType;
    private List branchList;
    private HashMap taskMap;
    LinkedHashMap transactionDetailsMap = new LinkedHashMap();
    TransactionTO transactionTODebit = new TransactionTO();
    private RemittanceIssueDAO remittanceIssueDAO;
    private RemittanceIssueTO remittanceIssueTO;

    /**
     * Creates a new instance of StandingInstructionTask
     */
    public StandingInstructionTask(TaskHeader header) throws Exception {
        setHeader(header);
        userID = header.getUserID();
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        curDate = ServerUtil.getCurrentDate(header.getBranchID());
        taskMap = header.getTaskParam();
        if (taskMap != null && taskMap.containsKey("DAY_END_TYPE")) {
            dayEndType = CommonUtil.convertObjToStr(taskMap.get("DAY_END_TYPE"));
        }
        if (CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
            if (taskMap.containsKey("BRANCH_LST")) {
                branchList = (List) taskMap.get("BRANCH_LST");
                System.out.println("branchList*****" + branchList);
            } else {
//                HashMap tempMap = new HashMap();
//                tempMap.put("NEXT_DATE", curDate);
//                branchList=(List)sqlMap.executeQueryForList("getAllBranchesFromDayEndComp",tempMap);
//                tempMap = null;
                branchList = null;
            }
        } else {
            HashMap tempMap = new HashMap();
            tempMap.put(CommonConstants.BRANCH_ID, getHeader().getBranchID());
            branchList = new ArrayList();
            branchList.add(tempMap);
            tempMap = null;
        }
        if (taskMap != null && taskMap.containsKey("CHK_EXEC_STD_TASK_LABLE")) {
            taskLable = CommonUtil.convertObjToStr(taskMap.get("CHK_EXEC_STD_TASK_LABLE"));
        }
    }

    private TxTransferTO getDebitTransferTO(StandingInstructionDebitTO standingInstructionDebitTO, StandingInstructionTO siTO, String drBranchCode) {
        final TxTransferTO objTxTransferTO = new TxTransferTO();
        try {
            objTxTransferTO.setBatchId(null);

            HashMap prodID = new HashMap();
            HashMap actNum = new HashMap();
            prodID.put(PROD_ID, standingInstructionDebitTO.getProdId());

            if (!standingInstructionDebitTO.getProdType().equals("GL")) {
                HashMap acHeadMap = (HashMap) sqlMap.executeQueryForObject("getProdID_OP_AC_PRODUCT" + standingInstructionDebitTO.getProdType(), prodID);
                actNum.put(ACT_NUM, standingInstructionDebitTO.getAcctNo());
                // The following line by Rajesh
//                HashMap branchidMap = (HashMap)sqlMap.executeQueryForObject("getBranchCode_ACT_MASTER" + standingInstructionDebitTO.getProdType(), actNum);
                objTxTransferTO.setAcHdId(CommonUtil.convertObjToStr(acHeadMap.get(AC_HD_ID)));
                objTxTransferTO.setProdId(standingInstructionDebitTO.getProdId());
                objTxTransferTO.setActNum(standingInstructionDebitTO.getAcctNo());
                objTxTransferTO.setBranchId(drBranchCode);  // This line added & commented the following line by Rajesh
//                objTxTransferTO.setBranchId(CommonUtil.convertObjToStr(branchidMap.get(BRANCH_CODE)));
                objTxTransferTO.setInpCurr(CommonUtil.convertObjToStr(acHeadMap.get(BASE_CURRENCY)));
                acHeadMap = null;
                // The following line by Rajesh
//                branchidMap = null;
            } else {
                objTxTransferTO.setAcHdId(standingInstructionDebitTO.getAcHdId());
                objTxTransferTO.setBranchId(branchID);
                objTxTransferTO.setInpCurr("INR");
            }
            objTxTransferTO.setProdType(standingInstructionDebitTO.getProdType());
            objTxTransferTO.setTransModType(standingInstructionDebitTO.getProdType());
            if (isVariable) {
                objTxTransferTO.setInpAmount(new Double(CommonUtil.convertObjToDouble(String.valueOf(debitAmt)).doubleValue()
                        + CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue()
                        + CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue()));
                objTxTransferTO.setAmount(new Double(CommonUtil.convertObjToDouble(String.valueOf(debitAmt)).doubleValue()
                        + CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue()
                        + CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue()));
                System.out.println("######objTxTransfer1233333" + objTxTransferTO.getInpAmount() + "@@@####" + objTxTransferTO.getAmount());
            } else {
                objTxTransferTO.setInpAmount(new Double(CommonUtil.convertObjToDouble(standingInstructionDebitTO.getAmount()).doubleValue()
                        + CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue()
                        + CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue()));
//                objTxTransferTO.setAmount(standingInstructionDebitTO.getAmount());
                objTxTransferTO.setAmount(new Double(CommonUtil.convertObjToDouble(standingInstructionDebitTO.getAmount()).doubleValue()
                        + CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue()
                        + CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue()));
            }
            objTxTransferTO.setTransType(CommonConstants.DEBIT);
            objTxTransferTO.setTransDt(curDate);
            objTxTransferTO.setInstType("VOUCHER");
            objTxTransferTO.setInstrumentNo1("1");
            objTxTransferTO.setInstrumentNo2("2");
            objTxTransferTO.setInstDt(curDate);
            objTxTransferTO.setInitChannType("CASHIER");
            objTxTransferTO.setParticulars("To-SI-" + standingInstructionDebitTO.getParticulars() + " " + siTO.getSiId());
            objTxTransferTO.setTransMode("TRANSFER");
            objTxTransferTO.setStatus(CommonConstants.STATUS_CREATED);
            if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                objTxTransferTO.setStatusBy(CommonConstants.TTSYSTEM);
                objTxTransferTO.setInitTransId(CommonConstants.TTSYSTEM);
            } else {
                objTxTransferTO.setStatusBy(String.valueOf("SITTSYSTEM"));
                objTxTransferTO.setInitTransId(String.valueOf("SITTSYSTEM"));
            }
            objTxTransferTO.setStatusDt(curDate);
            objTxTransferTO.setInitiatedBranch(super._branchCode);
            if (standingInstructionDebitTO.getProdType().equalsIgnoreCase("AD")) {// Added by nithya on 20-01-2017 for 	0005687: While RD Standing instruction from Advance, Advance Ledger not updating the Debit amount
                objTxTransferTO.setAuthorizeRemarks("DP");
            }
            installment += standingInstructionDebitTO.getAmount().doubleValue();
            System.out.println("######objTxTransferTODDDDD" + objTxTransferTO.getInitiatedBranch());


            prodID = null;
            actNum = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objTxTransferTO;
    }

    private TxTransferTO getCreditTransferTO(StandingInstructionCreditTO standingInstructionCreditTO, StandingInstructionTO siTO, String crBranchCode) {
        final TxTransferTO objTxTransferTO = new TxTransferTO();
        try {
            objTxTransferTO.setBatchId("");
            objTxTransferTO.setTransId("");
            HashMap prodID = new HashMap();
            HashMap actNum = new HashMap();
            String prod = standingInstructionCreditTO.getProdType();
            //            System.out.println("######getCreditTransferTO prodType"+prod);

            if (!standingInstructionCreditTO.getProdType().equals("GL")) {
                prodID.put(PROD_ID, standingInstructionCreditTO.getProdId());
                //            System.out.println("######getCreditTransferTO prodID"+prodID);
                HashMap acHeadMap = (HashMap) sqlMap.executeQueryForObject("getProdID_OP_AC_PRODUCT" + standingInstructionCreditTO.getProdType(), prodID);
                //            System.out.println("######getCreditTransferTO acHeadMap"+acHeadMap);
                String act = "";
                act = standingInstructionCreditTO.getAcctNo();
                if (standingInstructionCreditTO.getProdType().equals("TL")) {
                    act = standingInstructionCreditTO.getAcctNo();
                    if (act.lastIndexOf("_") != -1) {
                        act = act.substring(0, act.lastIndexOf("_"));
                    }
                    //                System.out.println("####act inside"+act);
                }
                //            System.out.println("####act outside"+act);
                actNum.put(ACT_NUM, act);
                //            System.out.println("######getCreditTransferTO actNum"+actNum);
                // The following line by Rajesh
//                HashMap branchidMap = (HashMap)sqlMap.executeQueryForObject("getBranchCode_ACT_MASTER" + standingInstructionCreditTO.getProdType(), actNum);
                //            System.out.println("######getCreditTransferTO branchidMap"+branchidMap);
                //            System.out.println("######getCreditTransferTO branchidMap actNum"+actNum);
                objTxTransferTO.setAcHdId(CommonUtil.convertObjToStr(acHeadMap.get(AC_HD_ID)));
                objTxTransferTO.setProdId(standingInstructionCreditTO.getProdId());
                objTxTransferTO.setActNum(standingInstructionCreditTO.getAcctNo());
                objTxTransferTO.setBranchId(crBranchCode);  // This line added & commented the following line by Rajesh
//                objTxTransferTO.setBranchId(CommonUtil.convertObjToStr(branchidMap.get(BRANCH_CODE)));
                objTxTransferTO.setInpCurr(CommonUtil.convertObjToStr(acHeadMap.get(BASE_CURRENCY)));
                acHeadMap = null;
                // The following line by Rajesh
//                branchidMap = null;
            } else {
                objTxTransferTO.setAcHdId(standingInstructionCreditTO.getAcHdId());
                objTxTransferTO.setBranchId(branchID);
                objTxTransferTO.setInpCurr("INR");
            }
            objTxTransferTO.setProdType(standingInstructionCreditTO.getProdType());
            objTxTransferTO.setTransModType(standingInstructionCreditTO.getProdType());
            if (isVariable) {
                objTxTransferTO.setInpAmount(CommonUtil.convertObjToDouble(String.valueOf(debitAmt)));
                objTxTransferTO.setAmount(CommonUtil.convertObjToDouble(String.valueOf(debitAmt)));
                System.out.println("######objTxTransfer1233333" + objTxTransferTO.getInpAmount() + "@@@####" + objTxTransferTO.getAmount());
                isVariable = false;
            } else {
                objTxTransferTO.setInpAmount(standingInstructionCreditTO.getAmount());
                objTxTransferTO.setAmount(standingInstructionCreditTO.getAmount());
            }
            objTxTransferTO.setTransType(CommonConstants.CREDIT);
            objTxTransferTO.setTransDt(curDate);
            objTxTransferTO.setInstType(null);
            objTxTransferTO.setInstrumentNo1(null);
            objTxTransferTO.setInstrumentNo2(null);
            objTxTransferTO.setInstDt(null);
            objTxTransferTO.setInitChannType("CASHIER");
            objTxTransferTO.setParticulars("By-SI-" + standingInstructionCreditTO.getParticulars() + " " + siTO.getSiId());
            objTxTransferTO.setTransMode("TRANSFER");
            objTxTransferTO.setStatus(CommonConstants.STATUS_CREATED);
            if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                objTxTransferTO.setStatusBy(CommonConstants.TTSYSTEM);
                objTxTransferTO.setInitTransId(CommonConstants.TTSYSTEM);
            } else {
                objTxTransferTO.setStatusBy(String.valueOf("SITTSYSTEM"));
                objTxTransferTO.setInitTransId(String.valueOf("SITTSYSTEM"));
            }
            objTxTransferTO.setStatusDt(curDate);
            objTxTransferTO.setInitiatedBranch(super._branchCode);
            objTxTransferTO.setBatchId("");//changed by jithin for loan splitup according to hierarchy
            System.out.println("######objTxTransferTO" + objTxTransferTO.getInitiatedBranch());

            prodID = null;
            actNum = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objTxTransferTO;
    }

    private TxTransferTO getDebitSiChargesTransferTO(StandingInstructionDebitTO standingInstructionDebitTO, StandingInstructionTO siTO, String drBranchCode) {
        final TxTransferTO objTxTransferTO = new TxTransferTO();
        try {
            objTxTransferTO.setBatchId(null);

            HashMap prodID = new HashMap();
            HashMap actNum = new HashMap();
            prodID.put(PROD_ID, standingInstructionDebitTO.getProdId());

            if (!standingInstructionDebitTO.getProdType().equals("GL")) {
                HashMap acHeadMap = (HashMap) sqlMap.executeQueryForObject("getProdID_OP_AC_PRODUCT" + standingInstructionDebitTO.getProdType(), prodID);
                actNum.put(ACT_NUM, standingInstructionDebitTO.getAcctNo());
                // The following line by Rajesh
//                HashMap branchidMap = (HashMap)sqlMap.executeQueryForObject("getBranchCode_ACT_MASTER" + standingInstructionDebitTO.getProdType(), actNum);
                objTxTransferTO.setAcHdId(CommonUtil.convertObjToStr(acHeadMap.get(AC_HD_ID)));
                objTxTransferTO.setProdId(standingInstructionDebitTO.getProdId());
                objTxTransferTO.setActNum(standingInstructionDebitTO.getAcctNo());
                objTxTransferTO.setBranchId(drBranchCode);  // This line added & commented the following line by Rajesh
//                objTxTransferTO.setBranchId(CommonUtil.convertObjToStr(branchidMap.get(BRANCH_CODE)));
                objTxTransferTO.setInpCurr(CommonUtil.convertObjToStr(acHeadMap.get(BASE_CURRENCY)));
                acHeadMap = null;
                // The following line by Rajesh
//                branchidMap = null;
            } else {
                objTxTransferTO.setAcHdId(standingInstructionDebitTO.getAcHdId());
                objTxTransferTO.setBranchId(branchID);
                objTxTransferTO.setInpCurr("INR");
            }
            objTxTransferTO.setProdType(standingInstructionDebitTO.getProdType());
            objTxTransferTO.setTransModType(standingInstructionDebitTO.getProdType());
//            if(isVariable){
//                objTxTransferTO.setInpAmount(CommonUtil.convertObjToDouble(String.valueOf(debitAmt)));
//                objTxTransferTO.setAmount(CommonUtil.convertObjToDouble(String.valueOf(debitAmt)));
//                System.out.println("######objTxTransfer1233333"+objTxTransferTO.getInpAmount()+"@@@####"+objTxTransferTO.getAmount());
//            }else{
            objTxTransferTO.setInpAmount(new Double(CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue()
                    + CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue()));
//                objTxTransferTO.setAmount(standingInstructionDebitTO.getAmount());
            objTxTransferTO.setAmount(new Double(CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue()
                    + CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue()));
//            }
            objTxTransferTO.setTransType(CommonConstants.DEBIT);
            objTxTransferTO.setTransDt(curDate);
            objTxTransferTO.setInstType("VOUCHER");
            objTxTransferTO.setInstrumentNo1("1");
            objTxTransferTO.setInstrumentNo2("2");
            objTxTransferTO.setInstDt(curDate);
            objTxTransferTO.setInitChannType("CASHIER");
            objTxTransferTO.setParticulars("To-SI-SI Charges " + siTO.getSiId());
            objTxTransferTO.setTransMode("TRANSFER");
            objTxTransferTO.setStatus(CommonConstants.STATUS_CREATED);
            if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                objTxTransferTO.setStatusBy(CommonConstants.TTSYSTEM);
                objTxTransferTO.setInitTransId(CommonConstants.TTSYSTEM);
            } else {
                objTxTransferTO.setStatusBy(String.valueOf("SITTSYSTEM"));
                objTxTransferTO.setInitTransId(String.valueOf("SITTSYSTEM"));
            }
            objTxTransferTO.setStatusDt(curDate);
            objTxTransferTO.setInitiatedBranch(super._branchCode);
            installment += standingInstructionDebitTO.getAmount().doubleValue();
            System.out.println("######FailureCharges" + objTxTransferTO);


            prodID = null;
            actNum = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objTxTransferTO;
    }

    private TxTransferTO getSiChargeCreditTransferTO(StandingInstructionCreditTO standingInstructionCreditTO, StandingInstructionTO siTO, String crBranchCode) {
        final TxTransferTO objSiChargeTxTransferTO = new TxTransferTO();
        try {
            objSiChargeTxTransferTO.setBatchId("");
            objSiChargeTxTransferTO.setTransId("");
            HashMap prodID = new HashMap();
            HashMap actNum = new HashMap();
//            HashMap acHeadMap = new HashMap();
//            prodID.put(PROD_ID, standingInstructionCreditTO.getProdId());    
//                List lst = (List)sqlMap.executeQueryForList("getSelectSIChargesHeadTO", null);
            HashMap acHeadMap = (HashMap) sqlMap.executeQueryForObject("getServiceTaxSiCharges", null);
//                if(lst != null && lst.size()>0){
//                    acHeadMap = (HashMap)lst.get(0);
//                }
            System.out.println("#####dd#acHeadMap" + acHeadMap);
            System.out.println("######acHeadMap" + (CommonUtil.convertObjToStr(acHeadMap.get("SI_COM_HD"))));
            objSiChargeTxTransferTO.setAcHdId(CommonUtil.convertObjToStr(acHeadMap.get("siComHd")));
            objSiChargeTxTransferTO.setBranchId(branchID);
            objSiChargeTxTransferTO.setInpCurr("INR");
            acHeadMap = null;
            objSiChargeTxTransferTO.setProdType("GL");
            objSiChargeTxTransferTO.setTransModType("GL");
            if (isVariable) {
                objSiChargeTxTransferTO.setInpAmount(siTO.getSiCharges());
                objSiChargeTxTransferTO.setAmount(siTO.getSiCharges());
                System.out.println("######objTxTransfer1233333" + objSiChargeTxTransferTO.getInpAmount() + "@@@####" + objSiChargeTxTransferTO.getAmount());
                isVariable = false;
            } else {
                objSiChargeTxTransferTO.setInpAmount(siTO.getSiCharges());
                objSiChargeTxTransferTO.setAmount(siTO.getSiCharges());
            }
            objSiChargeTxTransferTO.setTransType(CommonConstants.CREDIT);
            objSiChargeTxTransferTO.setTransDt(curDate);
            objSiChargeTxTransferTO.setInstType(null);
            objSiChargeTxTransferTO.setInstrumentNo1(null);
            objSiChargeTxTransferTO.setInstrumentNo2(null);
            objSiChargeTxTransferTO.setInstDt(null);
            objSiChargeTxTransferTO.setInitChannType("CASHIER");
            objSiChargeTxTransferTO.setParticulars("By-SI-SI Charges " + siTO.getSiId());
            objSiChargeTxTransferTO.setTransMode("TRANSFER");
            objSiChargeTxTransferTO.setStatus(CommonConstants.STATUS_CREATED);
            if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                objSiChargeTxTransferTO.setStatusBy(CommonConstants.TTSYSTEM);
                objSiChargeTxTransferTO.setInitTransId(CommonConstants.TTSYSTEM);
            } else {
                objSiChargeTxTransferTO.setStatusBy(String.valueOf("SITTSYSTEM"));
                objSiChargeTxTransferTO.setInitTransId(String.valueOf("SITTSYSTEM"));
            }
            objSiChargeTxTransferTO.setStatusDt(curDate);
            objSiChargeTxTransferTO.setInitiatedBranch(super._branchCode);
            objSiChargeTxTransferTO.setBatchId(_branchCode);
            System.out.println("######objSiChargeTxTransferTO" + objSiChargeTxTransferTO.getInitiatedBranch());

            prodID = null;
            actNum = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objSiChargeTxTransferTO;
    }

    private TxTransferTO getSiChargeSTCreditTransferTO(StandingInstructionCreditTO standingInstructionCreditTO, StandingInstructionTO siTO, String crBranchCode) {
        final TxTransferTO objSiChargeSTTxTransferTO = new TxTransferTO();
        try {
            objSiChargeSTTxTransferTO.setBatchId("");
            objSiChargeSTTxTransferTO.setTransId("");
            HashMap prodID = new HashMap();
            HashMap actNum = new HashMap();
//            prodID.put(PROD_ID, standingInstructionCreditTO.getProdId());    
//                HashMap acHeadMap = (HashMap)sqlMap.executeQueryForList("getServiceTaxSiCharges", prodID);
//            HashMap acHeadMap = new HashMap();
//            prodID.put(PROD_ID, standingInstructionCreditTO.getProdId());    
//                List lst = (List)sqlMap.executeQueryForList("getSelectSIChargesHeadTO", null);
//                if(lst != null && lst.size()>0){
//                    acHeadMap = (HashMap)lst.get(0);
//                }
            HashMap acHeadMap = (HashMap) sqlMap.executeQueryForObject("getServiceTaxSiCharges", null);
            System.out.println("######acHeadMap" + (CommonUtil.convertObjToStr(acHeadMap.get("SERVICE_TAX_HD"))));
            objSiChargeSTTxTransferTO.setAcHdId(CommonUtil.convertObjToStr(acHeadMap.get("serviceTaxHd")));
            System.out.println("######objSiChargeSTTxTransferTO" + objSiChargeSTTxTransferTO);
            objSiChargeSTTxTransferTO.setBranchId(branchID);
            objSiChargeSTTxTransferTO.setInpCurr("INR");
            acHeadMap = null;
            objSiChargeSTTxTransferTO.setProdType("GL");
            objSiChargeSTTxTransferTO.setTransModType("GL");
            if (isVariable) {
                objSiChargeSTTxTransferTO.setInpAmount(siTO.getExecCharge());
                objSiChargeSTTxTransferTO.setAmount(siTO.getExecCharge());
                System.out.println("######objTxTransfer1233333" + objSiChargeSTTxTransferTO.getInpAmount() + "@@@####" + objSiChargeSTTxTransferTO.getAmount());
                isVariable = false;
            } else {
                objSiChargeSTTxTransferTO.setInpAmount(siTO.getExecCharge());
                objSiChargeSTTxTransferTO.setAmount(siTO.getExecCharge());
            }
            objSiChargeSTTxTransferTO.setTransType(CommonConstants.CREDIT);
            objSiChargeSTTxTransferTO.setTransDt(curDate);
            objSiChargeSTTxTransferTO.setInstType(null);
            objSiChargeSTTxTransferTO.setInstrumentNo1(null);
            objSiChargeSTTxTransferTO.setInstrumentNo2(null);
            objSiChargeSTTxTransferTO.setInstDt(null);
            objSiChargeSTTxTransferTO.setInitChannType("CASHIER");
            objSiChargeSTTxTransferTO.setParticulars("By-SI-SI Charges ST " + siTO.getSiId());
            objSiChargeSTTxTransferTO.setTransMode("TRANSFER");
            objSiChargeSTTxTransferTO.setStatus(CommonConstants.STATUS_CREATED);
            if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                objSiChargeSTTxTransferTO.setStatusBy(CommonConstants.TTSYSTEM);
                objSiChargeSTTxTransferTO.setInitTransId(CommonConstants.TTSYSTEM);
            } else {
                objSiChargeSTTxTransferTO.setStatusBy(String.valueOf("SITTSYSTEM"));
                objSiChargeSTTxTransferTO.setInitTransId(String.valueOf("SITTSYSTEM"));
            }
            objSiChargeSTTxTransferTO.setStatusDt(curDate);
            objSiChargeSTTxTransferTO.setInitiatedBranch(super._branchCode);
            objSiChargeSTTxTransferTO.setBatchId(_branchCode);
            System.out.println("######objSiChargeSTTxTransferTO" + objSiChargeSTTxTransferTO.getInitiatedBranch());

            prodID = null;
            actNum = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objSiChargeSTTxTransferTO;
    }

    private TxTransferTO getDebitFailureChargesTransferTO(StandingInstructionDebitTO standingInstructionDebitTO, StandingInstructionTO siTO, String drBranchCode) {
        final TxTransferTO objTxTransferTO = new TxTransferTO();
        try {
            objTxTransferTO.setBatchId(null);

            HashMap prodID = new HashMap();
            HashMap actNum = new HashMap();
            prodID.put(PROD_ID, standingInstructionDebitTO.getProdId());

            if (!standingInstructionDebitTO.getProdType().equals("GL")) {
                HashMap acHeadMap = (HashMap) sqlMap.executeQueryForObject("getProdID_OP_AC_PRODUCT" + standingInstructionDebitTO.getProdType(), prodID);
                actNum.put(ACT_NUM, standingInstructionDebitTO.getAcctNo());
                // The following line by Rajesh
//                HashMap branchidMap = (HashMap)sqlMap.executeQueryForObject("getBranchCode_ACT_MASTER" + standingInstructionDebitTO.getProdType(), actNum);
                objTxTransferTO.setAcHdId(CommonUtil.convertObjToStr(acHeadMap.get(AC_HD_ID)));
                objTxTransferTO.setProdId(standingInstructionDebitTO.getProdId());
                objTxTransferTO.setActNum(standingInstructionDebitTO.getAcctNo());
                objTxTransferTO.setBranchId(drBranchCode);  // This line added & commented the following line by Rajesh
//                objTxTransferTO.setBranchId(CommonUtil.convertObjToStr(branchidMap.get(BRANCH_CODE)));
                objTxTransferTO.setInpCurr(CommonUtil.convertObjToStr(acHeadMap.get(BASE_CURRENCY)));
                acHeadMap = null;
                // The following line by Rajesh
//                branchidMap = null;
            } else {
                objTxTransferTO.setAcHdId(standingInstructionDebitTO.getAcHdId());
                objTxTransferTO.setBranchId(branchID);
                objTxTransferTO.setInpCurr("INR");
            }
            objTxTransferTO.setProdType(standingInstructionDebitTO.getProdType());
            objTxTransferTO.setTransModType(standingInstructionDebitTO.getProdType());
//            if(isVariable){
//                objTxTransferTO.setInpAmount(CommonUtil.convertObjToDouble(String.valueOf(debitAmt)));
//                objTxTransferTO.setAmount(CommonUtil.convertObjToDouble(String.valueOf(debitAmt)));
//                System.out.println("######objTxTransfer1233333"+objTxTransferTO.getInpAmount()+"@@@####"+objTxTransferTO.getAmount());
//            }else{
            objTxTransferTO.setInpAmount(new Double(CommonUtil.convertObjToDouble(siTO.getFailureCharge()).doubleValue()
                    + CommonUtil.convertObjToDouble(siTO.getFailureServiceTax()).doubleValue()));
//                objTxTransferTO.setAmount(standingInstructionDebitTO.getAmount());
            objTxTransferTO.setAmount(new Double(CommonUtil.convertObjToDouble(siTO.getFailureCharge()).doubleValue()
                    + CommonUtil.convertObjToDouble(siTO.getFailureServiceTax()).doubleValue()));
//            }
            objTxTransferTO.setTransType(CommonConstants.DEBIT);
            objTxTransferTO.setTransDt(curDate);
            objTxTransferTO.setInstType("VOUCHER");
            objTxTransferTO.setInstrumentNo1("1");
            objTxTransferTO.setInstrumentNo2("2");
            objTxTransferTO.setInstDt(curDate);
            objTxTransferTO.setInitChannType("CASHIER");
            objTxTransferTO.setParticulars("To-SI-Failure Charges " + siTO.getSiId());
            objTxTransferTO.setTransMode("TRANSFER");
            objTxTransferTO.setStatus(CommonConstants.STATUS_CREATED);
            if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                objTxTransferTO.setStatusBy(CommonConstants.TTSYSTEM);
                objTxTransferTO.setInitTransId(CommonConstants.TTSYSTEM);
            } else {
                objTxTransferTO.setStatusBy(String.valueOf("SITTSYSTEM"));
                objTxTransferTO.setInitTransId(String.valueOf("SITTSYSTEM"));
            }
            objTxTransferTO.setStatusDt(curDate);
            objTxTransferTO.setInitiatedBranch(super._branchCode);
            installment += standingInstructionDebitTO.getAmount().doubleValue();
            System.out.println("######FailureCharges" + objTxTransferTO);


            prodID = null;
            actNum = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objTxTransferTO;
    }

    private TxTransferTO getSiFailChargeCreditTransferTO(StandingInstructionCreditTO standingInstructionCreditTO, StandingInstructionTO siTO, String crBranchCode) {
        final TxTransferTO objSiChargeTxTransferTO = new TxTransferTO();
        try {
            objSiChargeTxTransferTO.setBatchId("");
            objSiChargeTxTransferTO.setTransId("");
            HashMap prodID = new HashMap();
            HashMap actNum = new HashMap();
//            HashMap acHeadMap = new HashMap();
//            prodID.put(PROD_ID, standingInstructionCreditTO.getProdId());    
//                List lst = (List)sqlMap.executeQueryForList("getSelectSIChargesHeadTO", null);
            HashMap acHeadMap = (HashMap) sqlMap.executeQueryForObject("getServiceTaxSiCharges", null);
//                if(lst != null && lst.size()>0){
//                    acHeadMap = (HashMap)lst.get(0);
//                }
            System.out.println("#####dd#acHeadMap" + acHeadMap);
//                System.out.println("######acHeadMap"+(CommonUtil.convertObjToStr(acHeadMap.get("SI_COM_HD"))));    
            objSiChargeTxTransferTO.setAcHdId(CommonUtil.convertObjToStr(acHeadMap.get("failChargeHd")));
            objSiChargeTxTransferTO.setBranchId(branchID);
            objSiChargeTxTransferTO.setInpCurr("INR");
            acHeadMap = null;
            objSiChargeTxTransferTO.setProdType("GL");
            objSiChargeTxTransferTO.setTransModType("GL");
//            if(isVariable){
//                objSiChargeTxTransferTO.setInpAmount(siTO.getFailureCharge());
//                objSiChargeTxTransferTO.setAmount(siTO.getFailureCharge());
//                System.out.println("######objTxTransfer1233333"+objSiChargeTxTransferTO.getInpAmount()+"@@@####"+objSiChargeTxTransferTO.getAmount());
//                isVariable = false;
//            }else{
            objSiChargeTxTransferTO.setInpAmount(siTO.getFailureCharge());
            objSiChargeTxTransferTO.setAmount(siTO.getFailureCharge());
//            }
            objSiChargeTxTransferTO.setTransType(CommonConstants.CREDIT);
            objSiChargeTxTransferTO.setTransDt(curDate);
            objSiChargeTxTransferTO.setInstType(null);
            objSiChargeTxTransferTO.setInstrumentNo1(null);
            objSiChargeTxTransferTO.setInstrumentNo2(null);
            objSiChargeTxTransferTO.setInstDt(null);
            objSiChargeTxTransferTO.setInitChannType("CASHIER");
            objSiChargeTxTransferTO.setParticulars("By-SI-Failure Charges " + siTO.getSiId());
            objSiChargeTxTransferTO.setTransMode("TRANSFER");
            objSiChargeTxTransferTO.setStatus(CommonConstants.STATUS_CREATED);
            if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                objSiChargeTxTransferTO.setStatusBy(CommonConstants.TTSYSTEM);
                objSiChargeTxTransferTO.setInitTransId(CommonConstants.TTSYSTEM);
            } else {
                objSiChargeTxTransferTO.setStatusBy(String.valueOf("SITTSYSTEM"));
                objSiChargeTxTransferTO.setInitTransId(String.valueOf("SITTSYSTEM"));
            }
            objSiChargeTxTransferTO.setStatusDt(curDate);
            objSiChargeTxTransferTO.setInitiatedBranch(super._branchCode);
            objSiChargeTxTransferTO.setBatchId(_branchCode);
            System.out.println("######objSiChargeTxTransferTO" + objSiChargeTxTransferTO.getInitiatedBranch());

            prodID = null;
            actNum = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objSiChargeTxTransferTO;
    }

    private TxTransferTO getSiFailChargeSTCreditTransferTO(StandingInstructionCreditTO standingInstructionCreditTO, StandingInstructionTO siTO, String crBranchCode) {
        final TxTransferTO objSiChargeSTTxTransferTO = new TxTransferTO();
        try {
            objSiChargeSTTxTransferTO.setBatchId("");
            objSiChargeSTTxTransferTO.setTransId("");
            HashMap prodID = new HashMap();
            HashMap actNum = new HashMap();
//            prodID.put(PROD_ID, standingInstructionCreditTO.getProdId());    
//                HashMap acHeadMap = (HashMap)sqlMap.executeQueryForList("getServiceTaxSiCharges", prodID);
//            HashMap acHeadMap = new HashMap();
//            prodID.put(PROD_ID, standingInstructionCreditTO.getProdId());    
//                List lst = (List)sqlMap.executeQueryForList("getSelectSIChargesHeadTO", null);
//                if(lst != null && lst.size()>0){
//                    acHeadMap = (HashMap)lst.get(0);
//                }
            HashMap acHeadMap = (HashMap) sqlMap.executeQueryForObject("getServiceTaxSiCharges", null);
            System.out.println("######acHeadMap" + (CommonUtil.convertObjToStr(acHeadMap.get("SERVICE_TAX_HD"))));
            objSiChargeSTTxTransferTO.setAcHdId(CommonUtil.convertObjToStr(acHeadMap.get("serviceTaxHd")));
            System.out.println("######objSiChargeSTTxTransferTO" + objSiChargeSTTxTransferTO);
            objSiChargeSTTxTransferTO.setBranchId(branchID);
            objSiChargeSTTxTransferTO.setInpCurr("INR");
            acHeadMap = null;
            objSiChargeSTTxTransferTO.setProdType("GL");
            objSiChargeSTTxTransferTO.setTransModType("GL");
//            if(isVariable){
//                objSiChargeSTTxTransferTO.setInpAmount(CommonUtil.convertObjToDouble(String.valueOf(debitAmt)));
//                objSiChargeSTTxTransferTO.setAmount(CommonUtil.convertObjToDouble(String.valueOf(debitAmt)));
//                System.out.println("######objTxTransfer1233333"+objSiChargeSTTxTransferTO.getInpAmount()+"@@@####"+objSiChargeSTTxTransferTO.getAmount());
//                isVariable = false;
//            }else{
            objSiChargeSTTxTransferTO.setInpAmount(siTO.getFailureServiceTax());
            objSiChargeSTTxTransferTO.setAmount(siTO.getFailureServiceTax());
//            }
            objSiChargeSTTxTransferTO.setTransType(CommonConstants.CREDIT);
            objSiChargeSTTxTransferTO.setTransDt(curDate);
            objSiChargeSTTxTransferTO.setInstType(null);
            objSiChargeSTTxTransferTO.setInstrumentNo1(null);
            objSiChargeSTTxTransferTO.setInstrumentNo2(null);
            objSiChargeSTTxTransferTO.setInstDt(null);
            objSiChargeSTTxTransferTO.setInitChannType("CASHIER");
            objSiChargeSTTxTransferTO.setParticulars("By-SI-Failure Charges ST " + siTO.getSiId());
            objSiChargeSTTxTransferTO.setTransMode("TRANSFER");
            objSiChargeSTTxTransferTO.setStatus(CommonConstants.STATUS_CREATED);
            if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                objSiChargeSTTxTransferTO.setStatusBy(CommonConstants.TTSYSTEM);
                objSiChargeSTTxTransferTO.setInitTransId(CommonConstants.TTSYSTEM);
            } else {
                objSiChargeSTTxTransferTO.setStatusBy(String.valueOf("SITTSYSTEM"));
                objSiChargeSTTxTransferTO.setInitTransId(String.valueOf("SITTSYSTEM"));
            }
            objSiChargeSTTxTransferTO.setStatusDt(curDate);
            objSiChargeSTTxTransferTO.setInitiatedBranch(super._branchCode);
            objSiChargeSTTxTransferTO.setBatchId(_branchCode);
            System.out.println("######objSiChargeSTTxTransferTO" + objSiChargeSTTxTransferTO.getInitiatedBranch());

            prodID = null;
            actNum = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objSiChargeSTTxTransferTO;
    }

    private StandingInstructionTaskTO getStandingInstructionTaskTO(StandingInstructionTO siTO, boolean status) {
        final StandingInstructionTaskTO objStandingInstructionTaskTO = new StandingInstructionTaskTO();
        try {
            objStandingInstructionTaskTO.setExecDt(curDate);
            //double install = CommonUtil.convertObjToDouble(mapSITask.get(INSTALLMENT)).doubleValue()+1;
            //objStandingInstructionTaskTO.setInstallment(CommonUtil.convertObjToDouble(String.valueOf(install)));
            objStandingInstructionTaskTO.setInstallment(new Double(0.0));
            if (batch.containsKey(CommonConstants.TRANS_ID)) {
                objStandingInstructionTaskTO.setBatchID(CommonUtil.convertObjToStr(batch.get(CommonConstants.TRANS_ID)));
            }
            //            objStandingInstructionTaskTO.setInstallment(new Double(installment));
            objStandingInstructionTaskTO.setSiId(CommonUtil.convertObjToStr(siTO.getSiId()));
            if (status) {
                objStandingInstructionTaskTO.setStatus(CommonConstants.SUCCESS);
            } else {
                objStandingInstructionTaskTO.setStatus(CommonConstants.FAILURE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objStandingInstructionTaskTO;
    }

    public TaskStatus executeTask() throws Exception {
        TaskStatus status = new TaskStatus();

        //        try{
        status.setStatus(BatchConstants.STARTED);
        status.setExecutionCount(0);
        //List list = sqlMap.executeQueryForList("StandingInstruction_BatchProcess", "");
        String yesNo = CommonUtil.convertObjToStr(ServerUtil.getCbmsParameterMap().get("EXECUTE_SI_DAY_BEGIN"));//basing on this SI will execute and added by Kannan AR ref. KDSA-679
        if(yesNo.length()>0 && yesNo.equals("Y")){
        if (branchList != null && branchList.size() > 0) {
            for (int b = 0; b < branchList.size(); b++) {
                HashMap branchMap = (HashMap) branchList.get(b);
                String branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                curDate = ServerUtil.getCurrentDate(branch);

                //newly added
                HashMap compMap = new HashMap();
                List compLst = null;
                compMap.put("TASK_NAME", taskLable);
                compMap.put("DAYEND_DT", curDate);
                compMap.put("BRANCH_ID", branch);
                //                if(process.equals(CommonConstants.DAY_BEGIN)){
                compLst = (List) sqlMap.executeQueryForList("getSelectTaskLstDayBegin", compMap);
                //                }else{
                //                    compLst = (List)sqlMap.executeQueryForList("getSelectTaskLst", compMap);
                //                }
                compMap = null;
                String compStatus = "";
                if (compLst != null && compLst.size() > 0) {
                    compMap = (HashMap) compLst.get(0);
                    compStatus = CommonUtil.convertObjToStr(compMap.get("TASK_STATUS"));
                    compMap = null;
                }
                if (compLst.size() <= 0 || compStatus.equals("ERROR")) {
                    sqlMap.startTransaction();
                    HashMap errorMap = new HashMap();
                    errorMap.put("ERROR_DATE", curDate);
                    errorMap.put("TASK_NAME", taskLable);
                    errorMap.put("BRANCH_ID", branch);
                    sqlMap.executeUpdate("deleteError_showing", errorMap);
                    sqlMap.commitTransaction();
                    try {
                        HashMap paramMap = new HashMap();
                        do {
                            paramMap.put("TODAY_DT", curDate);
                            if (chkPrevDayHol) {
                                paramMap.put("TODAY_DT", prevDt);
                            }
                            paramMap.put("SI_TYPE", "FIXED");
                            //                paramMap.put("EXEC_CONFIG", CommonConstants.DAY_BEGIN);
                            //                System.out.println("DDDDDDDDDDDDDDDDDDDD"+getHeader().getProcessType());
                            if (getHeader().getProcessType().equals(CommonConstants.DAY_BEGIN)) {
                                paramMap.put("EXEC_CONFIG", CommonConstants.DAY_BEGIN);
                            } else {
                                paramMap.put("EXEC_CONFIG", CommonConstants.DAY_END);
                            }

                            //run failure batch first
                            paramMap.put("TRANS_STATUS", "FAILURE");
                            paramMap.put("BRANCH_CODE", branch);
                            //                System.out.println("ParamMap ##### " +paramMap);
                            runBatch(paramMap, status);
                            //                System.out.println("First ParamMap runBatch ##### FAILURE executeTask paramMap :" +paramMap);

                            //run new batch here
                            paramMap.put("TRANS_STATUS", "SUCCESS");
                            runBatch(paramMap, status);
                            if (prevDt == null) {
                                prevDt = DateUtil.addDaysProperFormat(curDate, -1);
                            } else {
                                prevDt = DateUtil.addDaysProperFormat(prevDt, -1);
                            }
                            chkPrevDayHoliday(prevDt);
                        } while (chkPrevDayHol);
                    } catch (Exception e) {
//                    e.printStackTrace();
//                    status.setStatus(BatchConstants.REJECTED);
//                    throw new TransRollbackException(e);
                        sqlMap.rollbackTransaction();
                        String errMsg = "";
                        TTException tte = null;
                        HashMap exceptionMap = null;
                        HashMap excMap = null;
                        String strExc = null;
                        String errClassName = "";
                        if (e instanceof TTException) {
                            System.out.println("#$#$ if TTException part..." + e);
                            tte = (TTException) e;
                            if (tte != null) {
                                exceptionMap = tte.getExceptionHashMap();
                                System.out.println("#$#$ if TTException part exceptionMap ..." + exceptionMap);
                                if (exceptionMap != null) {
                                    ArrayList list = (ArrayList) exceptionMap.get(CommonConstants.EXCEPTION_LIST);
                                    errClassName = CommonUtil.convertObjToStr(exceptionMap.get(CommonConstants.CONSTANT_CLASS));
                                    System.out.println("#$#$ if TTException part EXCEPTION_LIST ..." + list);
                                    if (list != null && list.size() > 0) {
                                        for (int i = 0; i < list.size(); i++) {
                                            if (list.get(i) instanceof HashMap) {
                                                excMap = (HashMap) list.get(i);
                                                System.out.println("#$#$ if TTException part excMap ..." + excMap);
                                                strExc = CommonUtil.convertObjToStr(excMap.get(CommonConstants.ERR_MESSAGE))
                                                        + " (" + CommonUtil.convertObjToStr(excMap.get(CommonConstants.ERR_DATA)) + ")";
                                            } else {
                                                strExc = (String) list.get(i);
                                                System.out.println("#$#$ if TTException part strExc ..." + strExc);
                                            }
                                            errorMap = new HashMap();
                                            errorMap.put("ERROR_DATE", curDate);
                                            errorMap.put("TASK_NAME", taskLable);
                                            errorMap.put("ERROR_MSG", strExc);
                                            errorMap.put("ERROR_CLASS", errClassName);
                                            errorMap.put("ACT_NUM", "GL");
                                            errorMap.put("BRANCH_ID", branch);
                                            sqlMap.startTransaction();
                                            sqlMap.executeUpdate("insertError_showing", errorMap);
                                            sqlMap.commitTransaction();
                                            errorMap = null;
                                        }
                                    }
                                } else {
                                    System.out.println("#$#$ if not TTException part..." + e);
                                    errMsg = e.getMessage();
                                    errorMap = new HashMap();
                                    errorMap.put("ERROR_DATE", curDate);
                                    errorMap.put("TASK_NAME", taskLable);
                                    errorMap.put("ERROR_MSG", errMsg);
                                    errorMap.put("ERROR_CLASS", errClassName);
                                    errorMap.put("ACT_NUM", "minor to major");
                                    errorMap.put("BRANCH_ID", branch);
                                    sqlMap.startTransaction();
                                    sqlMap.executeUpdate("insertError_showing", errorMap);
                                    sqlMap.commitTransaction();
                                    errorMap = null;
                                }
                            }
                        } else {
                            System.out.println("#$#$ if not TTException part..." + e);
                            errMsg = e.getMessage();
                            errorMap = new HashMap();
                            errorMap.put("ERROR_DATE", curDate);
                            errorMap.put("TASK_NAME", taskLable);
                            errorMap.put("ERROR_MSG", errMsg);
                            errorMap.put("ERROR_CLASS", errClassName);
                            errorMap.put("ACT_NUM", "minor to major");
                            errorMap.put("BRANCH_ID", branch);
                            sqlMap.startTransaction();
                            sqlMap.executeUpdate("insertError_showing", errorMap);
                            sqlMap.commitTransaction();
                            errorMap = null;
                        }
                        status.setStatus(BatchConstants.ERROR);
                        //                        System.out.println("Error thrown for Operative A/c No " + dataMap.get("ACT_NUM"));
                        //                e.printStackTrace();
                        tte = null;
                        exceptionMap = null;
                        excMap = null;
                        e.printStackTrace();
                    }
                }
                if (!compStatus.equals("COMPLETED")) {
                    if (status.getStatus() != BatchConstants.ERROR) {
                        sqlMap.startTransaction();
                        if (compStatus.equals("ERROR")) {
                            HashMap statusMap = new HashMap();
                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                            statusMap.put("BRANCH_CODE", branch);
                            statusMap.put("TASK_NAME", taskLable);
                            statusMap.put("TASK_STATUS", "COMPLETED");
                            statusMap.put("USER_ID", getHeader().getUserID());
                            statusMap.put("DAYEND_DT", curDate);
                            //                                if(process.equals(CommonConstants.DAY_BEGIN)){
                            sqlMap.executeUpdate("updateTskStatusDayBegin", statusMap);
                            //                                }else{
                            //                                    sqlMap.executeUpdate("updateTskStatus", statusMap);
                            //                                }
                            statusMap = null;
                        } else {
                            HashMap statusMap = new HashMap();
                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                            statusMap.put("BRANCH_CODE", branch);
                            statusMap.put("TASK_NAME", taskLable);
                            statusMap.put("TASK_STATUS", "COMPLETED");
                            statusMap.put("USER_ID", getHeader().getUserID());
                            statusMap.put("DAYEND_DT", curDate);
                            //                                if(process.equals(CommonConstants.DAY_BEGIN)){
                            sqlMap.executeUpdate("InsertDayEndStatusDayBegin", statusMap);
                            //                                }else{
                            //                                    sqlMap.executeUpdate("InsertDayEndStatus", statusMap);
                            //                                }
                            statusMap = null;
                        }
                        sqlMap.commitTransaction();
                    } else {
                        if (compStatus.equals("ERROR")) {
                            HashMap statusMap = new HashMap();
                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                            statusMap.put("BRANCH_CODE", branch);
                            statusMap.put("TASK_NAME", taskLable);
                            statusMap.put("TASK_STATUS", "ERROR");
                            statusMap.put("USER_ID", getHeader().getUserID());
                            statusMap.put("DAYEND_DT", curDate);
                            //                            if(process.equals(CommonConstants.DAY_BEGIN)){
                            sqlMap.executeUpdate("updateTskStatusDayBegin", statusMap);
                            //                            }else{
                            //                                sqlMap.executeUpdate("updateTskStatus", statusMap);
                            //                            }
                            statusMap = null;
                        } else {
                            //                            isError = true;
                            HashMap statusMap = new HashMap();
                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                            statusMap.put("BRANCH_CODE", branch);
                            statusMap.put("TASK_NAME", taskLable);
                            statusMap.put("TASK_STATUS", "ERROR");
                            statusMap.put("USER_ID", getHeader().getUserID());
                            statusMap.put("DAYEND_DT", curDate);
                            //                            if(process.equals(CommonConstants.DAY_BEGIN)){
                            sqlMap.executeUpdate("InsertDayEndStatusDayBegin", statusMap);
                            //                            }else{
                            //                                sqlMap.executeUpdate("InsertDayEndStatus", statusMap);
                            //                            }
                            statusMap = null;

                        }
                    }
                }
                //            System.out.println("runBatch ParamMap ##### SUCCESS executeTask paramMap :" +paramMap);
            }
        }
       }
        status.setStatus(BatchConstants.COMPLETED);


        return status;
    }

    private void runBatch(HashMap paramMap, TaskStatus status) throws Exception {
        StandingInstructionTO siTO;
        ArrayList batchList;
//        ArrayList FailureBatchList;
        boolean transStatus;
        HashMap errorMap;
        Date nxtDate;
        Date startDt;
        Date lstDt;
        String nextDate;
        String lastDate;
        String fwdDt;
        String execDtIfHoliday = "";
        double minBal = 0;
        double multiples = 0;
        boolean depValidate = true;
        System.out.println("###### runBatch paramMap" + paramMap);
        System.out.println("###### runBatch errorMap" + status);
        List list = sqlMap.executeQueryForList("standingBatchRun", paramMap);
        System.out.println("inside ####### runBatch List :" + list);
        List listVar = sqlMap.executeQueryForList("standingBatchRunVariable", paramMap);
        System.out.println("inside ####### runBatch List :" + list);
        status.setActualCount(list.size());
        if (list != null && list.size() > 0) {                                                //IF STANDING INSTRUCTION IS FIXED
            for (int i = list.size() - 1, k = 1; i >= 0; --i, ++k) {
                transStatus = true;
                List drlist = null;
                List crlst = null;
                String freq = "";
                String week = "";
                String weekDay = "";
                siTO = (StandingInstructionTO) list.get(i);
                System.out.println("sito : " + siTO);
                fwdDt = CommonUtil.convertObjToStr(siTO.getForwardRunDt());
                execDtIfHoliday = CommonUtil.convertObjToStr(siTO.getChangeHolidayExec());
                System.out.println("execDtIfHoliday#########:" + execDtIfHoliday);
                branchID = CommonUtil.convertObjToStr(siTO.getBranchCode());
                lstDt = siTO.getLastRunDt();
                lastDate = CommonUtil.convertObjToStr(siTO.getLastRunDt());
                nextDate = CommonUtil.convertObjToStr(siTO.getNextRunDt());
                nxtDate = siTO.getNextRunDt();
                startDt = siTO.getSiStartDt();
                System.out.println("startDate$$$####" + startDt);
                batchList = new ArrayList();
                freq = siTO.getFrequency();
                week = siTO.getWeek();
                weekDay = siTO.getWeekDay();
                HashMap siId = new HashMap();
                siId.put("SAL_REC_MODULE", CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE));
                siId.put("SI_ID", siTO.getSiId());
                siId.put("ASONDATE",curDate.clone()); // Added by nithya on 03-03-2017 for 5763 [ RBI ]
                List creditList = sqlMap.executeQueryForList("getSelectStandingInstructionCreditTO", siId);
                List debitList = sqlMap.executeQueryForList("getSelectStandingInstructionDebitTO", siId);
                System.out.println("inside for loop ####### runBatch creditList :" + creditList);
                System.out.println("inside for loop ####### runBatch debitList :" + debitList);
                if (debitList != null && debitList.size() > 0) {
                    for (int x = 0, y = debitList.size(); x < y; x++) {
                        HashMap where = new HashMap();
                        where.put("PROD_TYPE", ((StandingInstructionCreditTO) creditList.get(0)).getProdType());
                        where.put("ACT_NUM", ((StandingInstructionDebitTO) debitList.get(x)).getAcctNo());
                        if (!(CommonUtil.convertObjToStr(where.get("PROD_TYPE")).equals("RM"))) {
                            StandingInstructionDebitTO standingInstructionDebitTO = (StandingInstructionDebitTO) debitList.get(x);
                            HashMap branchidMap = new HashMap();
                            if (!standingInstructionDebitTO.getProdType().equals("GL")) {
                                branchidMap = (HashMap) sqlMap.executeQueryForObject("getBranchCode_ACT_MASTER"
                                        + standingInstructionDebitTO.getProdType(), where);
                            }
                            if (branchList != null && branchList.size() > 0) {
                                for (int b = 0; b < branchList.size(); b++) {
                                    HashMap branchMap = (HashMap) branchList.get(b);
                                    String branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                                    if (standingInstructionDebitTO.getProdType().equals("GL")) {
                                        branchidMap.put("BRANCH_CODE", branchMap.get(CommonConstants.BRANCH_ID));
                                    }
                                    if (CommonUtil.convertObjToStr(branchidMap.get(BRANCH_CODE)).equals(branch)) {
                                        batchList.add(getDebitTransferTO(standingInstructionDebitTO, (StandingInstructionTO) siTO, branch));
                                    }
                                    branchMap = null;
                                }
                            }
                            branchidMap = null;
                        } else {
                        }
                    }
                }
                TransferTrans trans = new TransferTrans();
                if(creditList!=null && creditList.size()>0){
                    for (int x = 0 ,z = creditList.size();x < z ; x++){
                            HashMap where  =new HashMap();
                            where.put("ACT_NUM", ((StandingInstructionCreditTO)creditList.get(x)).getAcctNo());
                            where.put("PROD_ID",((StandingInstructionCreditTO)creditList.get(x)).getProdId());
                            where.put("PROD_TYPE",((StandingInstructionCreditTO)creditList.get(x)).getProdType());
                            prodType = CommonUtil.convertObjToStr(where.get("PROD_TYPE"));
                            if(!(where.get("PROD_TYPE").equals("RM"))){
                                String mapName = "getDetailsSIAcNumCredit"+((StandingInstructionCreditTO) creditList.get(x)).getProdType();
                                StandingInstructionCreditTO standingInstructionCreditTO = (StandingInstructionCreditTO) creditList.get(x);
                                HashMap branchidMap = new HashMap();
                                if(!standingInstructionCreditTO.getProdType().equals("GL"))
                                    branchidMap = (HashMap)sqlMap.executeQueryForObject("getBranchCode_ACT_MASTER" + 
                                        standingInstructionCreditTO.getProdType(), where);
                                    if (branchList!=null && branchList.size()>0) {
                                        for (int b=0;b<branchList.size();b++) {
                                            HashMap branchMap = (HashMap) branchList.get(b);
                                            String branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                                            if(standingInstructionCreditTO.getProdType().equals("GL"))
                                                branchidMap.put("BRANCH_CODE",branchMap.get(CommonConstants.BRANCH_ID));
                                            if(CommonUtil.convertObjToStr(branchidMap.get(BRANCH_CODE)).equals(branch)) {
                                                batchList.add(getCreditTransferTO(standingInstructionCreditTO,(StandingInstructionTO) siTO, branch));
                                                if(CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue() > 0){
                                                    batchList.add(getSiChargeCreditTransferTO(standingInstructionCreditTO,(StandingInstructionTO) siTO, branch));
                                                }
                                                if(CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue() > 0){
                                                    batchList.add(getSiChargeSTCreditTransferTO(standingInstructionCreditTO,(StandingInstructionTO) siTO, branch));
                                                }
                                            }
                                            branchMap = null;
                                        }
                                    }
                                    branchidMap = null;
                              //  if((where.get("PROD_TYPE").equals("TD")) && (where.get("PROD_ID").equals("RD"))){//changed by jiv
                                     if((where.get("PROD_TYPE").equals("TD")) ){
                                         String behaves="";
                                         String inst_beyond_maturity="";
                                         List behavesList = sqlMap.executeQueryForList("getSiBeyondMaturity", where);
                                         if (behavesList.size() > 0) {
                                             HashMap behMap = (HashMap) behavesList.get(0);
                                             behaves = behMap.get("BEHAVES_LIKE").toString();
                                             inst_beyond_maturity=behMap.get("INST_BEYOND_MATURITY_DATE").toString();//INST_BEYOND_MATURITY_DATE
                                             if (behaves.equals("RECURRING")) {
                                                 String actNum = CommonUtil.convertObjToStr(where.get("ACT_NUM"));
                                                 actNum = actNum.substring(0, actNum.lastIndexOf("_"));
                                                 where.put("ACT_NUM", actNum);
                                                 List depVal = sqlMap.executeQueryForList("getDepValidateData", where);
                                                 HashMap where1 = new HashMap();
                                                 if ((depVal != null) && (depVal.size() > 0)) {
                                                     where1 = (HashMap) depVal.get(0);
                                                     String actStatus = CommonUtil.convertObjToStr(where1.get("ACCT_STATUS"));
                                                     Date matDate = (Date) where1.get("MATURITY_DT");
                                                     Date todayDate = (Date) paramMap.get("TODAY_DT");
                                                     int total=CommonUtil.convertObjToInt(where1.get("TOTAL_INSTALLMENTS"));
                                                     int paid=CommonUtil.convertObjToInt(where1.get("TOTAL_INSTALL_PAID"));
                                                     System.out.println("+++actStatus" + actStatus + "%%%%%matDate" + matDate);
                                                     System.out.println("************" + DateUtil.dateDiff(matDate, todayDate));
                                                     System.out.println("inst_beyond_maturity"+inst_beyond_maturity);
                                                  
                                                     if(inst_beyond_maturity.equals("N")){
                                                     if ((actStatus.equalsIgnoreCase("CLOSED"))
                                                             || (DateUtil.dateDiff(matDate, todayDate) > 0)) {
                                                         depValidate = false;
                                                     }else
                                                         depValidate = true;
                                                     }else if(inst_beyond_maturity.equals("Y")){
                                                     if ((actStatus.equalsIgnoreCase("CLOSED"))
                                                             || ((total-paid)==0)) {
                                                         depValidate = false;
                                                     }
                          /*added by sreekrishnan*/  else
                          /*added by sreekrishnan*/  depValidate = true;  
                                                     }
                                                    
                                                 }
                                             }
                                         }
                                }
                            //Added By Suresh Set All_Amount Map
                            if (where.get("PROD_TYPE").equals("TL") || where.get("PROD_TYPE").equals("AD")) {
                                HashMap loanMap = new HashMap();
                                loanMap = interestCalculationTLAD(CommonUtil.convertObjToStr(where.get("ACT_NUM")),
                                        CommonUtil.convertObjToStr(where.get("PROD_ID")));
                                List waiveList = sqlMap.executeQueryForList("getwaiveoffDetails", where);
                                if (waiveList != null && waiveList.size() > 0) {
                                    HashMap waiveMap = (HashMap) waiveList.get(0);
                                    loanMap.put("PENAL_WAIVE_OFF", waiveMap.get("PENAL_WAIVER"));
                                }
                                trans.setAllAmountMap(loanMap);
                            }
                        } else {
                        }
                    }
                }

                if (depValidate) {
                    try {
                        sqlMap.startTransaction();
                        if (!prodType.equalsIgnoreCase("RM")) {
                            //                            TransferTrans trans = new TransferTrans();

                            trans.setInitiatedBranch(branchID);
                            trans.setLoanDebitInt("DP");
                            if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                                trans.doDebitCredit(batchList, _branchCode);
                            } else {
                                trans.doDebitCredit(batchList, _branchCode, false);
                            }
                            trans.setLoanDebitInt("");

                        } else {
                            if (debitList != null && debitList.size() > 0) {
                                for (int x = 0, y = debitList.size(); x < y; x++) {
                                    HashMap where = new HashMap();
                                    where.put("PROD_TYPE", ((StandingInstructionCreditTO) creditList.get(x)).getProdType());
                                    if ((where.get("PROD_TYPE").equals("RM"))) {
                                        StandingInstructionDebitTO standingInstructionDebitTO = (StandingInstructionDebitTO) debitList.get(x);
                                        final HashMap accountNameMap = new HashMap();
                                        String custName = "";
                                        accountNameMap.put("ACC_NUM", standingInstructionDebitTO.getAcctNo());
                                        final List resultList = sqlMap.executeQueryForList("getAccountNumberName" + standingInstructionDebitTO.getProdType(), accountNameMap);
                                        if (resultList != null) {
                                            if (resultList.size() != 0) {
                                                final HashMap resultMap = (HashMap) resultList.get(0);
                                                if (resultMap != null) {
                                                    custName = resultMap.get("CUSTOMER_NAME").toString();
                                                }
                                            }
                                        }
                                        Date todayDt = (Date) curDate.clone();
                                        System.out.println("inside Rm");
                                        HashMap dtatMap = new HashMap();
                                        LinkedHashMap notDelMap = new LinkedHashMap();
                                        LinkedHashMap notDelRemMap = new LinkedHashMap();
                                        dtatMap.put("MODE", "INSERT");
                                        TransactionTO transfer = new TransactionTO();
                                        transfer.setTransType("TRANSFER");
                                        transfer.setTransAmt(new Double(CommonUtil.convertObjToDouble(standingInstructionDebitTO.getAmount()).doubleValue()
                                                + CommonUtil.convertObjToDouble(siTO.getRemitCharges()).doubleValue()
                                                + CommonUtil.convertObjToDouble(siTO.getAcceptanceCharge()).doubleValue()
                                                + CommonUtil.convertObjToDouble(siTO.getServiceTax()).doubleValue()));
                                        transfer.setProductId(CommonUtil.convertObjToStr(standingInstructionDebitTO.getProdId()));
                                        transfer.setProductType(CommonUtil.convertObjToStr(standingInstructionDebitTO.getProdType()));
                                        transfer.setDebitAcctNo(CommonUtil.convertObjToStr(standingInstructionDebitTO.getAcctNo()));
                                        transfer.setApplName(CommonUtil.convertObjToStr(custName));  // Added by Rajesh
                                        transfer.setInstType("VOUCHER");
                                        transfer.setChequeDt(todayDt);
                                        LinkedHashMap transMap = new LinkedHashMap();
                                        LinkedHashMap remMap = new LinkedHashMap();
                                        transfer.setCommand("INSERT");
                                        notDelMap.put(String.valueOf(1), transfer);//"NOT_DELETED_TRANS_TOs"
                                        transMap.put("NOT_DELETED_TRANS_TOs", notDelMap);
                                        dtatMap.put("TransactionTO", transMap);
                                        dtatMap.put(CommonConstants.BRANCH_ID, branchID);
                                        dtatMap.put("OPERATION_MODE", "ISSUE");
                                        dtatMap.put("AUTHORIZEMAP", null);
                                        RemittanceIssueTO remt = new RemittanceIssueTO();
                                        remt.setDraweeBranchCode(branchID);
                                        remt.setAmount(standingInstructionDebitTO.getAmount());
                                        remt.setCategory("GENERAL_CATEGORY");
                                        remt.setRemarks(siTO.getSiId());

                                        // The following block added by Rajesh
                                        HashMap behavesMap = new HashMap();
                                        behavesMap.put("BEHAVES_LIKE", "PO");
                                        List lstRemit = sqlMap.executeQueryForList("selectRemitProductId", behavesMap);
                                        if (lstRemit != null && lstRemit.size() > 0) {
                                            behavesMap = (HashMap) lstRemit.get(0);
                                            remt.setProdId(CommonUtil.convertObjToStr(behavesMap.get("PROD_ID")));
                                        }
                                        HashMap draweeMap = new HashMap();
                                        if (behavesMap.get("PAY_ISSUE_BRANCH").equals("ISSU_BRANCH")) {
                                            lstRemit = sqlMap.executeQueryForList("getSelectBankTOList", null);
                                            if (lstRemit != null && lstRemit.size() > 0) {
                                                draweeMap = (HashMap) lstRemit.get(0);
                                            }
                                        }
                                        remt.setFavouring(((StandingInstructionCreditTO) creditList.get(x)).getAcctNo());
                                        remt.setDraweeBank(CommonUtil.convertObjToStr(draweeMap.get("BANK_CODE")));
                                        remt.setBranchId(branchID);
                                        remt.setCity("560");
                                        remt.setInstrumentNo1("PO");
                                        remt.setCommand("INSERT");
                                        remt.setExchange(CommonUtil.convertObjToDouble(siTO.getRemitCharges()));
                                        remt.setPostage(CommonUtil.convertObjToDouble(siTO.getAcceptanceCharge()));
                                        remt.setOtherCharges(CommonUtil.convertObjToDouble(siTO.getServiceTax()));
                                        remt.setTotalAmt(new Double(CommonUtil.convertObjToDouble(standingInstructionDebitTO.getAmount()).doubleValue()
                                                + CommonUtil.convertObjToDouble(siTO.getRemitCharges()).doubleValue()
                                                + CommonUtil.convertObjToDouble(siTO.getAcceptanceCharge()).doubleValue()
                                                + CommonUtil.convertObjToDouble(siTO.getServiceTax()).doubleValue()));
                                        System.out.println("remt%%%%" + remt);
                                        remt.setStatusDt(todayDt);
                                        remt.setStatusBy("TTSYSTEM");
                                        notDelRemMap.put(String.valueOf(1), remt);
                                        remMap.put("NOT_DELETED_ISSUE_TOs", notDelRemMap);
                                        dtatMap.put("RemittanceIssueTO", remMap);
                                        System.out.println("dtatMap$$$$$$$$$$$$$$$" + dtatMap);
                                        RemittanceIssueDAO remDao = new RemittanceIssueDAO();
                                        HashMap resulMap = new HashMap();
                                        remDao.setFromotherDAo(false);
                                        resulMap = remDao.execute(dtatMap);
                                        System.out.println("resulMap$$$$$$$$$$$$$$$" + resulMap);
                                        behavesMap = null;
                                        draweeMap = null;
                                        lstRemit = null;
                                    }
                                }
                            }

                            //si charges
                            batchList = new ArrayList();
                            if (debitList != null && debitList.size() > 0) {
                                for (int x = 0, y = debitList.size(); x < y; x++) {
                                    HashMap where = new HashMap();
                                    where.put("PROD_TYPE", ((StandingInstructionDebitTO) debitList.get(x)).getProdType());
                                    where.put("ACT_NUM", ((StandingInstructionDebitTO) debitList.get(x)).getAcctNo());
                                    if (!(where.get("PROD_TYPE").equals("RM"))) {
                                        StandingInstructionDebitTO standingInstructionDebitTO = (StandingInstructionDebitTO) debitList.get(x);
                                        if (!standingInstructionDebitTO.getProdType().equals("GL")) {
                                            HashMap branchidMap = (HashMap) sqlMap.executeQueryForObject("getBranchCode_ACT_MASTER"
                                                    + standingInstructionDebitTO.getProdType(), where);
                                            if (branchList != null && branchList.size() > 0) {
                                                for (int b = 0; b < branchList.size(); b++) {
                                                    HashMap branchMap = (HashMap) branchList.get(b);
                                                    String branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                                                    if (CommonUtil.convertObjToStr(branchidMap.get(BRANCH_CODE)).equals(branch)) {
                                                        batchList.add(getDebitSiChargesTransferTO(standingInstructionDebitTO, (StandingInstructionTO) siTO, branch));
                                                    }
                                                    branchMap = null;
                                                }
                                            }
                                            branchidMap = null;
                                        }
                                    } else {
                                    }
                                }
                            }
                            if (creditList != null && creditList.size() > 0) {
                                for (int x = 0, z = creditList.size(); x < z; x++) {
                                    HashMap where = new HashMap();
                                    where.put("ACT_NUM", ((StandingInstructionCreditTO) creditList.get(x)).getAcctNo());
                                    where.put("PROD_ID", ((StandingInstructionCreditTO) creditList.get(x)).getProdId());
                                    where.put("PROD_TYPE", ((StandingInstructionCreditTO) creditList.get(x)).getProdType());
                                    prodType = CommonUtil.convertObjToStr(where.get("PROD_TYPE"));
                                    StandingInstructionCreditTO standingInstructionCreditTO = (StandingInstructionCreditTO) creditList.get(x);
                                    if (branchList != null && branchList.size() > 0) {
                                        for (int b = 0; b < branchList.size(); b++) {
                                            HashMap branchMap = (HashMap) branchList.get(b);
                                            String branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                                            if (CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue() > 0) {
                                                batchList.add(getSiChargeCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                            }
                                            if (CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue() > 0) {
                                                batchList.add(getSiChargeSTCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                            }
                                            branchMap = null;
                                        }
                                    }
                                }
                            }

                            //   trans = new TransferTrans();  ///commented by jithin for splitting loan according to hierarchy
                            trans.setInitiatedBranch(branchID);
                            trans.setLoanDebitInt("DP");


                            if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                                trans.doDebitCredit(batchList, _branchCode);
                            } else {
                                trans.doDebitCredit(batchList, _branchCode, false);
                            }
                            trans.setLoanDebitInt("");
                        }
                        sqlMap.executeUpdate("insertStandingInstructionTaskTO", getStandingInstructionTaskTO(siTO, true));
                        siTO.setForwardRunDt(null);
                        System.out.println("siTO :@###@@ " + siTO.toString());
                        int weekCnt = 0;
                        int calWeek = 0;
                        if (week != null) {
                            if (week.equalsIgnoreCase("FIRSTWEEK")) {
                                weekCnt = 1;
                            } else if (week.equalsIgnoreCase("SECONDWEEK")) {
                                weekCnt = 2;
                            } else if (week.equalsIgnoreCase("THIRDWEEK")) {
                                weekCnt = 3;
                            } else if (week.equalsIgnoreCase("FOURTHWEEK")) {
                                weekCnt = 4;
                            } else {
                                weekCnt = 5;
                            }
                        }
                        if ((!nextDate.equals("")) && (!lastDate.equals(""))) {                       // if StandingInstruction is already run
                            if (freq != null) {
                                //if frequency is mentioned
                                siTO.setLastRunDt(curDate);
                                if (fwdDt.equals("")) {
                                    siTO.setNextRunDt((DateUtil.addDaysProperFormat(siTO.getNextRunDt(), CommonUtil.convertObjToInt(siTO.getFrequency()))));
                                    Date nxtCalDt = nextCaldate(startDt, siTO.getNextRunDt());
                                    System.out.println("!!!nxtCalDt####" + nxtCalDt);
                                    siTO.setNextRunDt(nxtCalDt);
                                }
                                System.out.println("!!!11" + siTO.getNextRunDt());
                            } else {
                                // if week and weeday given

                                if ((weekDay != null) && (week == null)) {
                                    // if only weekday like monday,tue.... is mentioned
                                    siTO.setLastRunDt(curDate);
                                    if (fwdDt.equals("")) {
                                        siTO.setNextRunDt(DateUtil.addDaysProperFormat(siTO.getNextRunDt(), 7));

                                    }
                                    System.out.println("!!!11" + siTO.getNextRunDt());
                                } else {
                                    // if only weekday like monday,tue.... and week lik firstweek,secondweek is mentioned

                                    //                                Date nxtDt = new Date(siTO.getLastRunDt().getYear(),siTO.getLastRunDt().getMonth(),siTO.getLastRunDt().getDate());
                                    Date nxtDt = siTO.getLastRunDt();
                                    Calendar c1 = new GregorianCalendar(nxtDt.getYear() + 1900, nxtDt.getMonth(), nxtDt.getDate());
                                    System.out.println("### Date : " + c1.getTime());
                                    int weeks = (c1.getActualMaximum(c1.DATE) / 7) > 4 ? 5 : 4;
                                    c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, weeks);
                                    System.out.println("### Date : " + c1.getTime());
                                    HashMap dateMap = new HashMap();
                                    dateMap.put("TODAY_DT", c1.getTime());
                                    System.out.println("### dateMap : " + dateMap);
                                    List lst = sqlMap.executeQueryForList("getWeekCnt", dateMap);
                                    dateMap = null;
                                    if (lst != null && lst.size() > 0) {
                                        dateMap = (HashMap) lst.get(0);
                                        calWeek = CommonUtil.convertObjToInt(dateMap.get("CAL_DT"));
                                        System.out.println("### dateMap : " + dateMap);
                                    }
                                    System.out.println("### dateMap : " + calWeek);
                                    System.out.println("### dateMap : " + week);
                                    if (calWeek != weekCnt) {
                                        if (calWeek == weekCnt + 1) {
                                            c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
                                        } else {
                                            c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
                                        }
                                        System.out.println("### c1****** : " + c1.getTime());
                                    }
                                    if (fwdDt.equals("")) {
                                        Date nextRundt = (Date) nxtDt.clone();
                                        siTO.setNextRunDt(returnDateFormat(c1, nxtDt));

                                    }// Calculating nextRunDt c1.getTime() based on week and weekDay
                                    System.out.println("!!!11" + siTO.getNextRunDt());
                                    siTO.setLastRunDt(curDate);
                                }
                            }
                        } else {                                                                          // if StandingInstruction is running for the First time
                            if (freq != null) {
                                //if frequency is mentioned
                                siTO.setNextRunDt((DateUtil.addDaysProperFormat(startDt, CommonUtil.convertObjToInt(siTO.getFrequency()))));
                                Date nxtCalDt = nextCaldate(startDt, siTO.getNextRunDt());
                                System.out.println("!!!nxtCalDt2" + nxtCalDt);
                                siTO.setNextRunDt(nxtCalDt);
                                siTO.setLastRunDt(curDate);
                                System.out.println("!!!1222" + siTO.getNextRunDt());
                            } else {
                                // if week and weeday given
                                if ((weekDay != null) && (week == null)) {
                                    // if only weekday like monday,tue.... is mentioned
                                    siTO.setNextRunDt(DateUtil.addDaysProperFormat(startDt, 7));
                                    siTO.setLastRunDt(curDate);
                                } else {
                                    // if only weekday like monday,tue.... and week lik firstweek,secondweek is mentioned
                                    //                                Date nxtDt = new Date(siTO.getSiStartDt().getYear(),siTO.getSiStartDt().getMonth(),siTO.getSiStartDt().getDate());
                                    Date nxtDt = siTO.getSiStartDt();
                                    System.out.println("### nxtDt : " + nxtDt);
                                    Calendar c1 = new GregorianCalendar(nxtDt.getYear() + 1900, nxtDt.getMonth(), nxtDt.getDate());
                                    System.out.println("###c1 : " + c1.getTime());
                                    int weeks = (c1.getActualMaximum(c1.DATE) / 7) > 4 ? 5 : 4;
                                    c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, weeks);
                                    System.out.println("###c1 : " + c1);
                                    System.out.println("### Date : " + c1.getTime());
                                    HashMap dateMap = new HashMap();
                                    //                                dateMap.put("TODAY_DT", c1.getTime());comment abi
                                    dateMap.put("TODAY_DT", c1.getTime());
                                    System.out.println("### dateMap : " + dateMap);
                                    List lst = sqlMap.executeQueryForList("getWeekCnt", dateMap);
                                    dateMap = null;
                                    if (lst != null && lst.size() > 0) {
                                        dateMap = (HashMap) lst.get(0);
                                        calWeek = CommonUtil.convertObjToInt(dateMap.get("CAL_DT"));
                                        System.out.println("### dateMap : " + dateMap);
                                    }
                                    System.out.println("### dateMap : " + calWeek);
                                    System.out.println("### dateMap : " + weekCnt);
                                    if (calWeek != weekCnt) {
                                        if (calWeek == weekCnt + 1) {
                                            c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
                                        } else {
                                            c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
                                        }
                                        System.out.println("### c1****** : " + c1.getTime());
                                    }
                                    Date newDate = (Date) nxtDt.clone();
                                    newDate.setYear(c1.getTime().getYear());
                                    siTO.setNextRunDt(c1.getTime());                                        // Calculated nextRunDt c1.getTime() based on week and weekDay
                                    siTO.setLastRunDt(curDate);
                                    System.out.println("!!!1222" + siTO.getNextRunDt());
                                }
                            }
                        }
                        siTO.setCount(new Double(0));
                        System.out.println("siTO : " + siTO.toString());
                        Date nextdt = (Date) siTO.getNextRunDt();
                        System.out.println("nextrundate####" + nextdt);
                        holiydaychecking((Date) siTO.getNextRunDt(), execDtIfHoliday);                              // Holiday Checking for nxtRunDt
                        if (DateUtil.dateDiff(checkThisCDate, siTO.getNextRunDt()) == 0) {
                            siTO.setExecDtHoliday(null);
                        } else {
                            siTO.setExecDtHoliday(checkThisCDate);
                        }
                        sqlMap.executeUpdate("updateStandingInstruction", siTO);
                        sqlMap.commitTransaction();
                    } catch (Exception transError) {
                        System.out.println("insert failed, first attempt");
                        transStatus = false;
                        sqlMap.rollbackTransaction();
                        System.out.println("Error in transaction 1 ....");
                        transError.printStackTrace();
                    }

                    if (transStatus == false) {                                                  // if SI failed
                        try {
                            batchList = new ArrayList();
                            if (debitList != null && debitList.size() > 0) {
                                for (int x = 0, y = debitList.size(); x < y; x++) {
                                    HashMap where = new HashMap();
                                    where.put("PROD_TYPE", ((StandingInstructionCreditTO) debitList.get(x)).getProdType());
                                    where.put("ACT_NUM", ((StandingInstructionDebitTO) debitList.get(x)).getAcctNo());
                                    if (!(where.get("PROD_TYPE").equals("RM"))) {
                                        StandingInstructionDebitTO standingInstructionDebitTO = (StandingInstructionDebitTO) debitList.get(x);
                                        if (!standingInstructionDebitTO.getProdType().equals("GL")) {
                                            HashMap branchidMap = (HashMap) sqlMap.executeQueryForObject("getBranchCode_ACT_MASTER"
                                                    + standingInstructionDebitTO.getProdType(), where);
                                            if (branchList != null && branchList.size() > 0) {
                                                for (int b = 0; b < branchList.size(); b++) {
                                                    HashMap branchMap = (HashMap) branchList.get(b);
                                                    String branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                                                    if (CommonUtil.convertObjToStr(branchidMap.get(BRANCH_CODE)).equals(branch)) {
                                                        batchList.add(getDebitFailureChargesTransferTO(standingInstructionDebitTO, (StandingInstructionTO) siTO, branch));
                                                    }
                                                    branchMap = null;
                                                }
                                            }
                                            branchidMap = null;
                                        }
                                    } else {
                                    }
                                }
                            }
                            if (creditList != null && creditList.size() > 0) {
                                for (int x = 0, z = creditList.size(); x < z; x++) {
                                    HashMap where = new HashMap();
                                    where.put("ACT_NUM", ((StandingInstructionCreditTO) creditList.get(x)).getAcctNo());
                                    where.put("PROD_ID", ((StandingInstructionCreditTO) creditList.get(x)).getProdId());
                                    where.put("PROD_TYPE", ((StandingInstructionCreditTO) creditList.get(x)).getProdType());
                                    prodType = CommonUtil.convertObjToStr(where.get("PROD_TYPE"));
                                    String mapName = "getDetailsSIAcNumCredit" + ((StandingInstructionCreditTO) creditList.get(x)).getProdType();
                                    StandingInstructionCreditTO standingInstructionCreditTO = (StandingInstructionCreditTO) creditList.get(x);
                                    if (branchList != null && branchList.size() > 0) {
                                        for (int b = 0; b < branchList.size(); b++) {
                                            HashMap branchMap = (HashMap) branchList.get(b);
                                            String branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                                            if (CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue() > 0) {
                                                batchList.add(getSiFailChargeCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                            }
                                            if (CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue() > 0) {
                                                batchList.add(getSiFailChargeSTCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                            }
                                            branchMap = null;
                                        }
                                    }
                                }
                            }

                            sqlMap.startTransaction();
                            trans = new TransferTrans();
                            trans.setInitiatedBranch(branchID);
                            trans.setLoanDebitInt("DP");
                            if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                                trans.doDebitCredit(batchList, _branchCode);
                            } else {
                                trans.doDebitCredit(batchList, _branchCode, false);
                            }
                            trans.setLoanDebitInt("");
                            sqlMap.commitTransaction();
                        } catch (Exception transFailureEx) {
                            System.out.println("insert failed. Complete abort. Could not log");
                            sqlMap.rollbackTransaction();
                            System.out.println("Error in transaction 2 ....");
                            transFailureEx.printStackTrace();
                        } finally {
                            sqlMap.startTransaction();
                            Date startDate;
                            double count = siTO.getCount().doubleValue();
                            double fwdCount = siTO.getCarriedForwardCount().doubleValue();
                            sqlMap.executeUpdate("insertStandingInstructionTaskTO", getStandingInstructionTaskTO(siTO, false));
                            //                        String weekDay = "";
                            week = siTO.getWeek();
                            int weekCnt = 0;
                            int calWeek = 0;
                            if (week != null) {
                                if (week.equalsIgnoreCase("FIRSTWEEK")) {
                                    weekCnt = 1;
                                } else if (week.equalsIgnoreCase("SECONDWEEK")) {
                                    weekCnt = 2;
                                } else if (week.equalsIgnoreCase("THIRDWEEK")) {
                                    weekCnt = 3;
                                } else if (week.equalsIgnoreCase("FOURTHWEEK")) {
                                    weekCnt = 4;
                                } else {
                                    weekCnt = 5;
                                }
                            }
                            if ((count == 0 || count <= fwdCount) && (fwdCount > 0)) {        // if CarriedFwd count is specified
                                double updatedCnt = 0;
                                HashMap ctn = new HashMap();
                                count = count + 1;
                                ctn.put("SI_NO", siTO.getSiId());
                                ctn.put("COUNT", new Double(count));
                                sqlMap.executeUpdate("updateCount", ctn);                   // updating count value
                                List cntlist = sqlMap.executeQueryForList("getCount", ctn);
                                System.out.println("cntlist%%%%%5" + cntlist);
                                ctn = null;
                                if (cntlist.size() > 0 && cntlist != null) {
                                    ctn = (HashMap) cntlist.get(0);
                                    updatedCnt = CommonUtil.convertObjToDouble(ctn.get("COUNT")).doubleValue();
                                    System.out.println("updatedCnt%%%" + updatedCnt);
                                }
                                if (fwdCount >= updatedCnt) {
                                    if ((lastDate.equals("")) && (nextDate.equals(""))) {      // if Standing Instruction is running for the first time
                                        if (freq != null) {
                                            //if frequency is mentioned
                                            siTO.setNextRunDt(DateUtil.addDaysProperFormat(startDt, CommonUtil.convertObjToInt(siTO.getFrequency())));
                                            Date nxtCalDt = nextCaldate(startDt, siTO.getNextRunDt());
                                            System.out.println("nxtCalDt%%%" + nxtCalDt);
                                            siTO.setNextRunDt(nxtCalDt);
                                            siTO.setLastRunDt(curDate);
                                        } else {
                                            //if week and weekday is mentioned
                                            if ((weekDay != null) && (week == null)) {
                                                // if only weekday like monday,tue.... is mentioned
                                                siTO.setNextRunDt(DateUtil.addDaysProperFormat(startDt, 7));

                                                siTO.setLastRunDt(curDate);
                                            } else {
                                                // if only weekday like monday,tue.... and week lik firstweek,secondweek is mentioned
                                                Date nxtDt = new Date(siTO.getSiStartDt().getYear(), siTO.getSiStartDt().getMonth(), siTO.getSiStartDt().getDate());
                                                System.out.println("### nxtDt : " + nxtDt);
                                                Calendar c1 = new GregorianCalendar(nxtDt.getYear() + 1900, nxtDt.getMonth(), nxtDt.getDate());
                                                System.out.println("###c1 : " + c1.getTime());
                                                int weeks = (c1.getActualMaximum(c1.DATE) / 7) > 4 ? 5 : 4;
                                                c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, weeks);
                                                System.out.println("###c1 : " + c1);
                                                System.out.println("### Date : " + c1.getTime());
                                                HashMap dateMap = new HashMap();
                                                dateMap.put("TODAY_DT", c1.getTime());
                                                System.out.println("### dateMap : " + dateMap);
                                                List lst = sqlMap.executeQueryForList("getWeekCnt", dateMap);
                                                dateMap = null;
                                                if (lst != null && lst.size() > 0) {
                                                    dateMap = (HashMap) lst.get(0);
                                                    calWeek = CommonUtil.convertObjToInt(dateMap.get("CAL_DT"));
                                                    System.out.println("### dateMap : " + dateMap);
                                                }
                                                System.out.println("### dateMap : " + calWeek);
                                                System.out.println("### dateMap : " + week);
                                                if (calWeek != weekCnt) {
                                                    if (calWeek == weekCnt + 1) {
                                                        c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
                                                    } else {
                                                        c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
                                                    }
                                                    System.out.println("### c1****** : " + c1.getTime());
                                                }
                                                siTO.setNextRunDt(c1.getTime());
                                                //                                            siTO.setNextRunDt(DateUtil.addDaysProperFormat(siTO.getSiStartDt(), CommonUtil.convertObjToInt(siTO.getFrequency())));
                                                siTO.setLastRunDt(curDate);
                                            }
                                        }
                                    } else {                                                   // if Standing Instruction has run already
                                        if (freq != null) {
                                            //if frequency is mentioned
                                            siTO.setLastRunDt(curDate);
                                            if (fwdDt.equals("")) {
                                                siTO.setNextRunDt(DateUtil.addDaysProperFormat(nxtDate, CommonUtil.convertObjToInt(siTO.getFrequency())));
                                                Date nxtCalDt = nextCaldate(startDt, siTO.getNextRunDt());
                                                System.out.println("nxtCalDt$$$%%%" + nxtCalDt);
                                                siTO.setNextRunDt(nxtCalDt);
                                            }
                                        } else {
                                            //if week and weekday is mentioned
                                            if ((weekDay != null) && (week == null)) {
                                                // if only weekday like monday,tue.... is mentioned
                                                siTO.setLastRunDt(curDate);
                                                if (fwdDt.equals("")) {
                                                    siTO.setNextRunDt(DateUtil.addDaysProperFormat(nxtDate, 7));
                                                }
                                            } else {
                                                // if only weekday like monday,tue.... and week lik firstweek,secondweek is mentioned
                                                Date nxtDt = new Date(siTO.getNextRunDt().getYear(), siTO.getNextRunDt().getMonth(), siTO.getNextRunDt().getDate());
                                                System.out.println("### nxtDt : " + nxtDt);
                                                Calendar c1 = new GregorianCalendar(nxtDt.getYear() + 1900, nxtDt.getMonth(), nxtDt.getDate());
                                                System.out.println("###c1 : " + c1.getTime());
                                                int weeks = (c1.getActualMaximum(c1.DATE) / 7) > 4 ? 5 : 4;
                                                c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, weeks);
                                                System.out.println("###c1 : " + c1);
                                                System.out.println("### Date : " + c1.getTime());
                                                HashMap dateMap = new HashMap();
                                                dateMap.put("TODAY_DT", c1.getTime());
                                                System.out.println("### dateMap : " + dateMap);
                                                List lst = sqlMap.executeQueryForList("getWeekCnt", dateMap);
                                                dateMap = null;
                                                if (lst != null && lst.size() > 0) {
                                                    dateMap = (HashMap) lst.get(0);
                                                    calWeek = CommonUtil.convertObjToInt(dateMap.get("CAL_DT"));
                                                    System.out.println("### dateMap : " + dateMap);
                                                }
                                                System.out.println("### dateMap : " + calWeek);
                                                System.out.println("### dateMap : " + week);
                                                if (calWeek != weekCnt) {
                                                    if (calWeek == weekCnt + 1) {
                                                        c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
                                                    } else {
                                                        c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
                                                    }
                                                    System.out.println("### c1****** : " + c1.getTime());
                                                }
                                                siTO.setLastRunDt(curDate);
                                                if (fwdDt.equals("")) {
                                                    siTO.setNextRunDt(c1.getTime());
                                                }
                                            }
                                        }
                                    }
                                    siTO.setExecDtHoliday(null);
                                    siTO.setForwardRunDt(DateUtil.addDaysProperFormat(curDate, 1));
                                    execDtIfHoliday = "NEXTDAY";
                                    holiydaychecking(siTO.getForwardRunDt(), execDtIfHoliday);               // Holiday Checking for fwdRunDt
                                    if (DateUtil.dateDiff(checkThisCDate, siTO.getForwardRunDt()) == 0) {
                                        //do nothing
                                    } else {
                                        siTO.setForwardRunDt(checkThisCDate);
                                    }
                                } else {                                                                      // if count has gone above the specified Carried FwdCount
                                    ctn.put("SI_NO", siTO.getSiId());
                                    ctn.put("COUNT", new Double(0));
                                    sqlMap.executeUpdate("updateCount", ctn);
                                    siTO.setForwardRunDt(null);
                                    siTO.setLastRunDt(curDate);
                                    String execDtHol = siTO.getChangeHolidayExec();
                                    holiydaychecking(siTO.getNextRunDt(), execDtHol);
                                    if (DateUtil.dateDiff(checkThisCDate, siTO.getNextRunDt()) == 0) {
                                        siTO.setExecDtHoliday(null);
                                    } else {
                                        siTO.setExecDtHoliday(checkThisCDate);
                                    }
                                }
                            } else {
                                // if carried count is 0
                                String execDtHol = siTO.getChangeHolidayExec();
                                if ((lastDate.equals("")) && (nextDate.equals(""))) {                         // if Standing Instruction is running for the first time
                                    if (freq != null) {
                                        //if frequency is mentioned
                                        siTO.setNextRunDt(DateUtil.addDaysProperFormat(startDt, CommonUtil.convertObjToInt(siTO.getFrequency())));
                                        Date nxtCalDt = nextCaldate(startDt, siTO.getNextRunDt());
                                        System.out.println("nxtCalDt###%%%" + nxtCalDt);
                                        siTO.setNextRunDt(nxtCalDt);
                                        siTO.setLastRunDt(curDate);
                                        siTO.setCount(new Double(0));
                                        System.out.println("siTO : " + siTO.toString());
                                        holiydaychecking(siTO.getNextRunDt(), execDtIfHoliday);
                                        if (DateUtil.dateDiff(checkThisCDate, siTO.getNextRunDt()) == 0) {
                                            siTO.setExecDtHoliday(null);
                                        } else {
                                            siTO.setExecDtHoliday(checkThisCDate);
                                        }
                                    } else {
                                        // if week and weekday is mentioned
                                        if ((weekDay != null) && (week == null)) {
                                            // if only weekday like monday,tue.... is mentioned
                                            siTO.setNextRunDt(DateUtil.addDaysProperFormat(startDt, 7));
                                            siTO.setLastRunDt(curDate);
                                            siTO.setCount(new Double(0));
                                            System.out.println("siTO : " + siTO.toString());
                                            holiydaychecking(siTO.getNextRunDt(), execDtIfHoliday);
                                            if (DateUtil.dateDiff(checkThisCDate, siTO.getNextRunDt()) == 0) {
                                                siTO.setExecDtHoliday(null);
                                            } else {
                                                siTO.setExecDtHoliday(checkThisCDate);
                                            }
                                        } else {
                                            // if only weekday like monday,tue.... and week lik firstweek,secondweek is mentioned
                                            Date nxtDt = new Date(siTO.getSiStartDt().getYear(), siTO.getSiStartDt().getMonth(), siTO.getSiStartDt().getDate());
                                            System.out.println("### nxtDt : " + nxtDt);
                                            Calendar c1 = new GregorianCalendar(nxtDt.getYear() + 1900, nxtDt.getMonth(), nxtDt.getDate());
                                            System.out.println("###c1 : " + c1.getTime());
                                            int weeks = (c1.getActualMaximum(c1.DATE) / 7) > 4 ? 5 : 4;
                                            c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, weeks);
                                            System.out.println("###c1 : " + c1);
                                            System.out.println("### Date : " + c1.getTime());
                                            HashMap dateMap = new HashMap();
                                            dateMap.put("TODAY_DT", c1.getTime());
                                            System.out.println("### dateMap : " + dateMap);
                                            List lst = sqlMap.executeQueryForList("getWeekCnt", dateMap);
                                            dateMap = null;
                                            if (lst != null && lst.size() > 0) {
                                                dateMap = (HashMap) lst.get(0);
                                                calWeek = CommonUtil.convertObjToInt(dateMap.get("CAL_DT"));
                                                System.out.println("### dateMap : " + dateMap);
                                            }
                                            System.out.println("### dateMap : " + calWeek);
                                            System.out.println("### dateMap : " + week);
                                            if (calWeek != weekCnt) {
                                                if (calWeek == weekCnt + 1) {
                                                    c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
                                                } else {
                                                    c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
                                                }
                                                System.out.println("### c1****** : " + c1.getTime());
                                            }
                                            siTO.setNextRunDt(c1.getTime());
                                            siTO.setLastRunDt(curDate);
                                            System.out.println("siTO : " + siTO.toString());
                                            holiydaychecking(siTO.getNextRunDt(), execDtHol);
                                            if (DateUtil.dateDiff(checkThisCDate, siTO.getNextRunDt()) == 0) {
                                                siTO.setExecDtHoliday(null);
                                            } else {
                                                siTO.setExecDtHoliday(checkThisCDate);
                                            }
                                        }
                                    }
                                } else {                                                               // if Standing Instruction has run already
                                    if (freq != null) {
                                        //if frequency is mentioned
                                        siTO.setLastRunDt(curDate);
                                        siTO.setNextRunDt(DateUtil.addDaysProperFormat(nxtDate, CommonUtil.convertObjToInt(siTO.getFrequency())));
                                        Date nxtCalDt = nextCaldate(startDt, siTO.getNextRunDt());
                                        System.out.println("nxtCalDt###%%%" + nxtCalDt);
                                        siTO.setNextRunDt(nxtCalDt);
                                        siTO.setCount(new Double(0));
                                        System.out.println("siTO : " + siTO.toString());
                                        holiydaychecking(siTO.getNextRunDt(), execDtIfHoliday);
                                        if (DateUtil.dateDiff(checkThisCDate, siTO.getNextRunDt()) == 0) {
                                            siTO.setExecDtHoliday(null);
                                        } else {
                                            siTO.setExecDtHoliday(checkThisCDate);
                                        }
                                    } else {
                                        // if week and weekday is mentioned
                                        if ((weekDay != null) && (week == null)) {
                                            // if only weekday like monday,tue.... is mentioned
                                            siTO.setLastRunDt(curDate);
                                            System.out.println("nxtDate****%%" + nxtDate);
                                            siTO.setNextRunDt(DateUtil.addDaysProperFormat(nxtDate, 7));
                                            System.out.println("siTO.getNextRunDt()##%%%" + siTO.getNextRunDt());
                                            siTO.setCount(new Double(0));
                                            System.out.println("siTO : " + siTO.toString());
                                            holiydaychecking(siTO.getNextRunDt(), execDtIfHoliday);
                                            if (DateUtil.dateDiff(checkThisCDate, siTO.getNextRunDt()) == 0) {
                                                siTO.setExecDtHoliday(null);
                                            } else {
                                                siTO.setExecDtHoliday(checkThisCDate);
                                            }
                                        } else {
                                            // if only weekday like monday,tue.... and week lik firstweek,secondweek is mentioned
                                            //                                        Date nxtDt = new Date(siTO.getNextRunDt().getYear(),siTO.getNextRunDt().getMonth(),siTO.getNextRunDt().getDate());abi
                                            Date nxtDt = siTO.getNextRunDt();
                                            System.out.println("### nxtDt : " + nxtDt);
                                            Calendar c1 = new GregorianCalendar(nxtDt.getYear() + 1900, nxtDt.getMonth(), nxtDt.getDate());
                                            System.out.println("###c1 : " + c1.getTime());
                                            int weeks = (c1.getActualMaximum(c1.DATE) / 7) > 4 ? 5 : 4;
                                            c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, weeks);
                                            System.out.println("###c1 : " + c1);
                                            System.out.println("### Date : " + c1.getTime());
                                            HashMap dateMap = new HashMap();
                                            dateMap.put("TODAY_DT", c1.getTime());
                                            System.out.println("### dateMap : " + dateMap);
                                            List lst = sqlMap.executeQueryForList("getWeekCnt", dateMap);
                                            dateMap = null;
                                            if (lst != null && lst.size() > 0) {
                                                dateMap = (HashMap) lst.get(0);
                                                calWeek = CommonUtil.convertObjToInt(dateMap.get("CAL_DT"));
//                                                System.out.println("### dateMap : "+dateMap);
                                            }
//                                            System.out.println("### dateMap : "+calWeek);
//                                            System.out.println("### dateMap : "+week);
                                            if (calWeek != weekCnt) {
                                                if (calWeek == weekCnt + 1) {
                                                    c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
                                                } else {
                                                    c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
                                                }
//                                                System.out.println("### c1****** : "+c1.getTime());
                                            }
                                            siTO.setNextRunDt(c1.getTime());
                                            siTO.setLastRunDt(curDate);
                                            holiydaychecking(siTO.getNextRunDt(), execDtHol);                            // Holiday Cheking
                                            if (DateUtil.dateDiff(checkThisCDate, siTO.getNextRunDt()) == 0) {
                                                siTO.setExecDtHoliday(null);
                                            } else {
                                                siTO.setExecDtHoliday(checkThisCDate);
                                            }
                                        }
                                    }
                                }
                            }
                            errorMap = new HashMap();
                            errorMap.put("STATUS", "ERROR");
                            errorMap.put("SI_NO", siTO.getSiId());
                            errorMap.put("FWD_RUN_DT", siTO.getForwardRunDt());
                            errorMap.put("NXT_RUN_DT", siTO.getNextRunDt());
                            errorMap.put("LST_RUN_DT", siTO.getLastRunDt());
                            errorMap.put("EXE_HOL_DT", siTO.getExecDtHoliday());
                            System.out.println("==============>" + errorMap);
                            sqlMap.executeUpdate("updateStandingInstructionFailure", errorMap);
                            errorMap = null;
                            sqlMap.commitTransaction();
                        }
                    }
                    if (transStatus == true) {
                        status.setExecutionCount(k);
                    }

                    batchList = null;

                }
            }
        }

        if (listVar != null && listVar.size() > 0) {                                              //IF STANDING INSTRUCTION IS VARIABLE
            for (int i = listVar.size() - 1, k = 1; i >= 0; --i, ++k) {
                isVariable = true;
                transStatus = true;
                List drlist = null;
                List crlst = null;
                String freq = "";
                siTO = (StandingInstructionTO) listVar.get(i);
                System.out.println("sito : " + siTO);
                freq = siTO.getFrequency();

                if (freq != null) {                                                            // IF FREQUENCY IS MENTIONED
                    fwdDt = CommonUtil.convertObjToStr(siTO.getForwardRunDt());
                    execDtIfHoliday = CommonUtil.convertObjToStr(siTO.getChangeHolidayExec());
                    System.out.println("execDtIfHoliday#########:" + execDtIfHoliday);
                    branchID = CommonUtil.convertObjToStr(siTO.getBranchCode());
                    lstDt = siTO.getLastRunDt();
                    lastDate = CommonUtil.convertObjToStr(siTO.getLastRunDt());
                    siTO.setLastRunDt(curDate);
                    nextDate = CommonUtil.convertObjToStr(siTO.getNextRunDt());
                    nxtDate = siTO.getNextRunDt();
                    startDt = siTO.getSiStartDt();
                    minBal = siTO.getMinBalance().doubleValue();
                    multiples = siTO.getMultiplesOf().doubleValue();
                    String debitActNo = "";
                    double clearBal = 0;
                    debitAmt = 0;
                    batchList = new ArrayList();
                    HashMap siId = new HashMap();
                    siId.put("SAL_REC_MODULE", CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE));
                    siId.put("SI_ID", siTO.getSiId());
                    siId.put("ASONDATE",curDate.clone()); // Added by nithya on 03-03-2017 for 5763 [ RBI ]
                    List creditList = sqlMap.executeQueryForList("getSelectStandingInstructionCreditTO", siId);
                    List debitList = sqlMap.executeQueryForList("getSelectStandingInstructionDebitTO", siId);
                    System.out.println("inside for loop ####### runBatch creditList :" + creditList);
                    System.out.println("inside for loop ####### runBatch debitList :" + debitList);


                    //Added By Suresh
                    boolean loanFlag = false;
                    HashMap loanMap = new HashMap();
                    HashMap depMap = new HashMap();
                    if (creditList != null && creditList.size() > 0) {                                                // Credit List
                        for (int x = 0, z = creditList.size(); x < z; x++) {
                            HashMap where = new HashMap();
                            where.put("ACT_NUM", ((StandingInstructionCreditTO) creditList.get(x)).getAcctNo());
                            where.put("PROD_ID", ((StandingInstructionCreditTO) creditList.get(x)).getProdId());
                            where.put("PROD_TYPE", ((StandingInstructionCreditTO) creditList.get(x)).getProdType());
                            prodType = CommonUtil.convertObjToStr(where.get("PROD_TYPE"));
                            //Added By Suresh Set All_Amount
                            if (where.get("PROD_TYPE").equals("TL") || where.get("PROD_TYPE").equals("AD")) {
                                loanMap = calcLoanPayments(CommonUtil.convertObjToStr(where.get("ACT_NUM")),
                                        CommonUtil.convertObjToStr(where.get("PROD_ID")), CommonUtil.convertObjToStr(where.get("PROD_TYPE")));
                                if (loanMap != null) {
                                    debitAmt = CommonUtil.convertObjToDouble(loanMap.get("TOTAL_DEMAND")).doubleValue();
                                }
                                loanFlag = true;
                            }
                            if (where.get("PROD_TYPE").equals("TD")) {
                                try {
                                    depMap = calcTermDeposits(CommonUtil.convertObjToStr(where.get("ACT_NUM")),
                                            CommonUtil.convertObjToStr(where.get("PROD_ID")), CommonUtil.convertObjToStr(where.get("PROD_TYPE")));
                                    if (depMap != null) {
                                        debitAmt = CommonUtil.convertObjToDouble(depMap.get("TOTAL_DEMAND")).doubleValue();
                                    }
                                    loanFlag = true;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    // Debit List
                    if (debitList != null && debitList.size() > 0) {
                        for (int x = 0, y = debitList.size(); x < y; x++) {
                            HashMap where = new HashMap();
                            String mapName = "getDetailsSIAcNumDebit" + ((StandingInstructionDebitTO) debitList.get(x)).getProdType();
                            where.put("PROD_TYPE", ((StandingInstructionCreditTO) creditList.get(x)).getProdType());
                            where.put("ACT_NUM", ((StandingInstructionDebitTO) debitList.get(x)).getAcctNo());
                            where.put("PROD_ID", ((StandingInstructionDebitTO) debitList.get(x)).getAcHdId());
                            where.put("AMOUNT", ((StandingInstructionDebitTO) debitList.get(x)).getAmount());
                            debitActNo = CommonUtil.convertObjToStr(where.get("ACT_NUM"));
                            String debitMapName = "";
                            if (!loanFlag) {
                                if (where.get("PROD_TYPE").equals("OA")) {
                                    debitMapName = "getTransActDetails";
                                } else if (where.get("PROD_TYPE").equals("SA")) {
                                    debitMapName = "getTransActDetailsSA";
                                }
                                List clearBalLst = sqlMap.executeQueryForList(debitMapName, debitActNo);
                                if (clearBalLst != null && clearBalLst.size() > 0) {
                                    HashMap clearBalMap = new HashMap();
                                    clearBalMap = (HashMap) clearBalLst.get(0);
                                    //                                System.out.println("!!!!!!!!!!!!clearBalMap"+clearBalMap);
                                    clearBal = CommonUtil.convertObjToDouble(clearBalMap.get("clearBalance")).doubleValue();
                                    System.out.println("!!!!!!!!!!!!clearBal!!!!!!!!!!" + clearBal + "@@@@minbal" + minBal);
                                    double amt = clearBal - minBal;
                                    System.out.println("!!!!!!!!!!!!amt!!!!!!!!!!" + amt);
                                    debitAmt = ((int) (amt / multiples)) * multiples;
                                    System.out.println("!!!!!!!!!!!!!debitAmt!!!!!!!!!" + debitAmt);
                                }
                            }
                            StandingInstructionDebitTO standingInstructionDebitTO = (StandingInstructionDebitTO) debitList.get(x);
                            if (debitAmt > 0) {
                                if (!(where.get("PROD_TYPE").equals("RM"))) {
                                    if (!standingInstructionDebitTO.getProdType().equals("GL")) {
                                        HashMap branchidMap = (HashMap) sqlMap.executeQueryForObject("getBranchCode_ACT_MASTER"
                                                + standingInstructionDebitTO.getProdType(), where);
                                        if (branchList != null && branchList.size() > 0) {
                                            for (int b = 0; b < branchList.size(); b++) {
                                                HashMap branchMap = (HashMap) branchList.get(b);
                                                String branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                                                if (CommonUtil.convertObjToStr(branchidMap.get(BRANCH_CODE)).equals(branch)) {
                                                    batchList.add(getDebitTransferTO(standingInstructionDebitTO, (StandingInstructionTO) siTO, branch));
                                                }
                                                branchMap = null;
                                            }
                                        }
                                        branchidMap = null;
                                    }
                                } else {
                                }
                            }
                        }
                    }
                    // Credit List
                    TransferTrans trans = new TransferTrans();
                    if (creditList != null && creditList.size() > 0) {
                        for (int x = 0, z = creditList.size(); x < z; x++) {
                            HashMap where = new HashMap();
                            where.put("ACT_NUM", ((StandingInstructionCreditTO) creditList.get(x)).getAcctNo());
                            where.put("PROD_ID", ((StandingInstructionCreditTO) creditList.get(x)).getProdId());
                            where.put("PROD_TYPE", ((StandingInstructionCreditTO) creditList.get(x)).getProdType());
                            prodType = CommonUtil.convertObjToStr(where.get("PROD_TYPE"));
                            String mapName = "getDetailsSIAcNumCredit" + ((StandingInstructionCreditTO) creditList.get(x)).getProdType();
                            if (debitAmt > 0) {
                                if (!(where.get("PROD_TYPE").equals("RM"))) {
                                    StandingInstructionCreditTO standingInstructionCreditTO = (StandingInstructionCreditTO) creditList.get(x);
                                    if (!standingInstructionCreditTO.getProdType().equals("GL")) {
                                        HashMap branchidMap = (HashMap) sqlMap.executeQueryForObject("getBranchCode_ACT_MASTER"
                                                + standingInstructionCreditTO.getProdType(), where);
                                        if (branchList != null && branchList.size() > 0) {
                                            for (int b = 0; b < branchList.size(); b++) {
                                                HashMap branchMap = (HashMap) branchList.get(b);
                                                String branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                                                if (CommonUtil.convertObjToStr(branchidMap.get(BRANCH_CODE)).equals(branch)) {
                                                    batchList.add(getCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                                    if (CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue() > 0) {
                                                        batchList.add(getSiChargeCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                                    }
                                                    if (CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue() > 0) {
                                                        batchList.add(getSiChargeSTCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                                    }
                                                }
                                                branchMap = null;
                                            }
                                        }
                                        branchidMap = null;
                                    }
//                            batchList.add(getCreditTransferTO((StandingInstructionCreditTO) creditList.get(x),(StandingInstructionTO) siTO));
                                    if ((where.get("PROD_TYPE").equals("TD"))) {
                                        String behaves = "";
                                        String inst_beyond_maturity = "";
                                        List behavesList = sqlMap.executeQueryForList("getSiBeyondMaturity", where);
                                        if (behavesList.size() > 0) {
                                            HashMap behMap = (HashMap) behavesList.get(0);
                                            behaves = behMap.get("BEHAVES_LIKE").toString();
                                            inst_beyond_maturity = behMap.get("INST_BEYOND_MATURITY_DATE").toString();//INST_BEYOND_MATURITY_DATE
                                            if (behaves.equals("RECURRING")) {
                                                String actNum = CommonUtil.convertObjToStr(where.get("ACT_NUM"));
                                                actNum = actNum.substring(0, actNum.lastIndexOf("_"));
                                                where.put("ACT_NUM", actNum);
                                                List depVal = sqlMap.executeQueryForList("getDepValidateData", where);
                                                HashMap where1 = new HashMap();
                                                if ((depVal != null) && (depVal.size() > 0)) {
                                                    where1 = (HashMap) depVal.get(0);
                                                    String actStatus = CommonUtil.convertObjToStr(where1.get("ACCT_STATUS"));
                                                    Date matDate = (Date) where1.get("MATURITY_DT");
                                                    Date todayDate = (Date) paramMap.get("TODAY_DT");
                                                    int total = CommonUtil.convertObjToInt(where1.get("TOTAL_INSTALLMENTS"));
                                                    int paid = CommonUtil.convertObjToInt(where1.get("TOTAL_INSTALL_PAID"));
                                                    System.out.println("+++actStatus" + actStatus + "%%%%%matDate" + matDate);
                                                    System.out.println("************" + DateUtil.dateDiff(matDate, todayDate));
                                                    System.out.println("inst_beyond_maturity" + inst_beyond_maturity);

                                                    if (inst_beyond_maturity.equals("N")) {
                                                        if ((actStatus.equalsIgnoreCase("CLOSED"))
                                                                || (DateUtil.dateDiff(matDate, todayDate) > 0)) {
                                                            depValidate = false;
                                                        }
                                                    } else if (inst_beyond_maturity.equals("Y")) {
                                                        if ((actStatus.equalsIgnoreCase("CLOSED"))
                                                                || ((total - paid) == 0)) {
                                                            depValidate = false;
                                                        }
                                                    }

                                                }
                                            }
                                        }
                                    }
                                    //Added By Suresh Set All_Amount Map
                                    if (where.get("PROD_TYPE").equals("TL") || where.get("PROD_TYPE").equals("AD")) {
                                        if (loanMap != null) {
                                            trans.setAllAmountMap(loanMap);
                                        }
                                    }
                                    if (depValidate && where.get("PROD_TYPE").equals("TD")) {
                                        if (depMap != null) {
                                            trans.setDepPenalMap(depMap);
                                        }
                                    }
                                } else {
                                }
                            }
                        }
                    }
                    if (depValidate) {
                        System.out.println("batchList    " + batchList);
                        try {
                            System.out.println("trying insert.............");
                            sqlMap.startTransaction();
                            if (!prodType.equalsIgnoreCase("RM")) {
                                if (debitAmt > 0) {
                                    //                                    trans = new TransferTrans();
                                    trans.setInitiatedBranch(branchID);
                                    if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                                        trans.doDebitCredit(batchList, _branchCode);
                                    } else {
                                        trans.doDebitCredit(batchList, _branchCode, false);
                                    }
                                } else {
                                    transStatus = false;
                                }
                            } else {
                                if (debitList != null && debitList.size() > 0) {
                                    for (int x = 0, y = debitList.size(); x < y; x++) {
                                        HashMap where = new HashMap();
                                        where.put("PROD_TYPE", ((StandingInstructionCreditTO) creditList.get(x)).getProdType());
                                        if ((where.get("PROD_TYPE").equals("RM"))) {
                                            StandingInstructionDebitTO standingInstructionDebitTO = (StandingInstructionDebitTO) debitList.get(x);
                                            final HashMap accountNameMap = new HashMap();
                                            String custName = "";
                                            accountNameMap.put("ACC_NUM", standingInstructionDebitTO.getAcctNo());
                                            final List resultList = sqlMap.executeQueryForList("getAccountNumberName" + standingInstructionDebitTO.getProdType(), accountNameMap);
                                            if (resultList != null) {
                                                if (resultList.size() != 0) {
                                                    final HashMap resultMap = (HashMap) resultList.get(0);
                                                    if (resultMap != null) {
                                                        custName = resultMap.get("CUSTOMER_NAME").toString();
                                                    }
                                                }
                                            }
                                            Date todayDt = (Date) curDate.clone();
                                            System.out.println("inside Rm");
                                            HashMap dtatMap = new HashMap();
                                            LinkedHashMap notDelMap = new LinkedHashMap();
                                            LinkedHashMap notDelRemMap = new LinkedHashMap();
                                            dtatMap.put("MODE", "INSERT");
                                            TransactionTO transfer = new TransactionTO();
                                            transfer.setTransType("TRANSFER");
                                            transfer.setTransAmt(new Double(CommonUtil.convertObjToDouble(String.valueOf(debitAmt)).doubleValue()
                                                    + CommonUtil.convertObjToDouble(siTO.getRemitCharges()).doubleValue()
                                                    + CommonUtil.convertObjToDouble(siTO.getAcceptanceCharge()).doubleValue()
                                                    + CommonUtil.convertObjToDouble(siTO.getServiceTax()).doubleValue()));
                                            transfer.setProductId(CommonUtil.convertObjToStr(standingInstructionDebitTO.getProdId()));
                                            transfer.setProductType(CommonUtil.convertObjToStr(standingInstructionDebitTO.getProdType()));
                                            transfer.setDebitAcctNo(CommonUtil.convertObjToStr(standingInstructionDebitTO.getAcctNo()));
                                            transfer.setApplName(CommonUtil.convertObjToStr(custName));  // Added by Rajesh
                                            transfer.setInstType("VOUCHER");
                                            transfer.setChequeDt(todayDt);
                                            LinkedHashMap transMap = new LinkedHashMap();
                                            LinkedHashMap remMap = new LinkedHashMap();
                                            transfer.setCommand("INSERT");
                                            notDelMap.put(String.valueOf(1), transfer);//"NOT_DELETED_TRANS_TOs"
                                            transMap.put("NOT_DELETED_TRANS_TOs", notDelMap);
                                            dtatMap.put("TransactionTO", transMap);
                                            dtatMap.put(CommonConstants.BRANCH_ID, branchID);
                                            dtatMap.put("OPERATION_MODE", "ISSUE");
                                            dtatMap.put("AUTHORIZEMAP", null);
                                            RemittanceIssueTO remt = new RemittanceIssueTO();
                                            remt.setDraweeBranchCode(branchID);
                                            remt.setAmount(CommonUtil.convertObjToDouble(String.valueOf(debitAmt)));
                                            remt.setCategory("GENERAL_CATEGORY");
                                            remt.setRemarks(siTO.getSiId());

                                            // The following block added by Rajesh
                                            HashMap behavesMap = new HashMap();
                                            behavesMap.put("BEHAVES_LIKE", "PO");
                                            List lstRemit = sqlMap.executeQueryForList("selectRemitProductId", behavesMap);
                                            if (lstRemit != null && lstRemit.size() > 0) {
                                                behavesMap = (HashMap) lstRemit.get(0);
                                                remt.setProdId(CommonUtil.convertObjToStr(behavesMap.get("PROD_ID")));
                                            }
                                            HashMap draweeMap = new HashMap();
                                            if (behavesMap.get("PAY_ISSUE_BRANCH").equals("ISSU_BRANCH")) {
                                                lstRemit = sqlMap.executeQueryForList("getSelectBankTOList", null);
                                                if (lstRemit != null && lstRemit.size() > 0) {
                                                    draweeMap = (HashMap) lstRemit.get(0);
                                                }
                                            }

                                            remt.setFavouring(((StandingInstructionCreditTO) creditList.get(x)).getAcctNo());
                                            remt.setDraweeBank(CommonUtil.convertObjToStr(draweeMap.get("BANK_CODE")));
                                            remt.setBranchId(branchID);
                                            remt.setCity("560");
                                            remt.setInstrumentNo1("PO");
                                            remt.setCommand("INSERT");
                                            remt.setExchange(CommonUtil.convertObjToDouble(siTO.getRemitCharges()));
                                            remt.setPostage(CommonUtil.convertObjToDouble(siTO.getAcceptanceCharge()));
                                            remt.setOtherCharges(CommonUtil.convertObjToDouble(siTO.getServiceTax()));
                                            remt.setTotalAmt(new Double(CommonUtil.convertObjToDouble(String.valueOf(debitAmt)).doubleValue()
                                                    + CommonUtil.convertObjToDouble(siTO.getRemitCharges()).doubleValue()
                                                    + CommonUtil.convertObjToDouble(siTO.getAcceptanceCharge()).doubleValue()
                                                    + CommonUtil.convertObjToDouble(siTO.getServiceTax()).doubleValue()));
                                            System.out.println("remt%%%%" + remt);
                                            remt.setStatusDt(todayDt);
                                            remt.setStatusBy("TTSYSTEM");
                                            notDelRemMap.put(String.valueOf(1), remt);
                                            remMap.put("NOT_DELETED_ISSUE_TOs", notDelRemMap);
                                            dtatMap.put("RemittanceIssueTO", remMap);
                                            System.out.println("dtatMap$$$$$$$$$$$$$$$" + dtatMap);
                                            RemittanceIssueDAO remDao = new RemittanceIssueDAO();
                                            HashMap resulMap = new HashMap();
                                            remDao.setFromotherDAo(false);
                                            resulMap = remDao.execute(dtatMap);
                                            System.out.println("resulMap$$$$$$$$$$$$$$$" + resulMap);
                                            behavesMap = null;
                                            draweeMap = null;
                                            lstRemit = null;
                                        }
                                    }
                                }

                                //si charges
                                batchList = new ArrayList();
                                if (debitList != null && debitList.size() > 0) {
                                    for (int x = 0, y = debitList.size(); x < y; x++) {
                                        HashMap where = new HashMap();
                                        where.put("PROD_TYPE", ((StandingInstructionDebitTO) debitList.get(x)).getProdType());
                                        where.put("ACT_NUM", ((StandingInstructionDebitTO) debitList.get(x)).getAcctNo());
                                        if (!(where.get("PROD_TYPE").equals("RM"))) {
                                            StandingInstructionDebitTO standingInstructionDebitTO = (StandingInstructionDebitTO) debitList.get(x);
                                            if (!standingInstructionDebitTO.getProdType().equals("GL")) {
                                                HashMap branchidMap = (HashMap) sqlMap.executeQueryForObject("getBranchCode_ACT_MASTER"
                                                        + standingInstructionDebitTO.getProdType(), where);
                                                if (branchList != null && branchList.size() > 0) {
                                                    for (int b = 0; b < branchList.size(); b++) {
                                                        HashMap branchMap = (HashMap) branchList.get(b);
                                                        String branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                                                        if (CommonUtil.convertObjToStr(branchidMap.get(BRANCH_CODE)).equals(branch)) {
                                                            batchList.add(getDebitSiChargesTransferTO(standingInstructionDebitTO, (StandingInstructionTO) siTO, branch));
                                                        }
                                                        branchMap = null;
                                                    }
                                                }
                                                branchidMap = null;
                                            }
                                        } else {
                                        }
                                    }
                                }
                                if (creditList != null && creditList.size() > 0) {
                                    for (int x = 0, z = creditList.size(); x < z; x++) {
                                        HashMap where = new HashMap();
                                        where.put("ACT_NUM", ((StandingInstructionCreditTO) creditList.get(x)).getAcctNo());
                                        where.put("PROD_ID", ((StandingInstructionCreditTO) creditList.get(x)).getProdId());
                                        where.put("PROD_TYPE", ((StandingInstructionCreditTO) creditList.get(x)).getProdType());
                                        prodType = CommonUtil.convertObjToStr(where.get("PROD_TYPE"));
                                        StandingInstructionCreditTO standingInstructionCreditTO = (StandingInstructionCreditTO) creditList.get(x);
                                        if (branchList != null && branchList.size() > 0) {
                                            for (int b = 0; b < branchList.size(); b++) {
                                                HashMap branchMap = (HashMap) branchList.get(b);
                                                String branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                                                if (CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue() > 0) {
                                                    batchList.add(getSiChargeCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                                }
                                                if (CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue() > 0) {
                                                    batchList.add(getSiChargeSTCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                                }
                                                branchMap = null;
                                            }
                                        }
                                    }
                                }
                                trans = new TransferTrans();
                                trans.setInitiatedBranch(branchID);
                                trans.setLoanDebitInt("DP");
                                if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                                    trans.doDebitCredit(batchList, _branchCode);
                                } else {
                                    trans.doDebitCredit(batchList, _branchCode, false);
                                }
                                trans.setLoanDebitInt("");
                            }
                            sqlMap.executeUpdate("insertStandingInstructionTaskTO", getStandingInstructionTaskTO(siTO, true));
                            siTO.setForwardRunDt(null);
                            if ((!nextDate.equals("")) && (!lastDate.equals(""))) {                                       // if Standing Instruction has already run
                                siTO.setLastRunDt(curDate);
                                if (fwdDt.equals("")) {
                                    siTO.setNextRunDt((DateUtil.addDaysProperFormat(siTO.getNextRunDt(), CommonUtil.convertObjToInt(siTO.getFrequency()))));
                                    Date nxtCalDt = nextCaldate(startDt, siTO.getNextRunDt());
                                    System.out.println("!!!nxtCalDt####" + nxtCalDt);
                                    siTO.setNextRunDt(nxtCalDt);
                                }
                                System.out.println("!!!11" + siTO.getNextRunDt());
                            } else {                                                                                      // if Standing Instruction is running for the first time
                                siTO.setNextRunDt((DateUtil.addDaysProperFormat(startDt, CommonUtil.convertObjToInt(siTO.getFrequency()))));
                                Date nxtCalDt = nextCaldate(startDt, siTO.getNextRunDt());
                                System.out.println("!!!nxtCalDt####" + nxtCalDt);
                                siTO.setNextRunDt(nxtCalDt);
                                siTO.setLastRunDt(curDate);
                                System.out.println("!!!1222" + siTO.getNextRunDt());
                            }
                            siTO.setCount(new Double(0));
                            System.out.println("siTO : " + siTO.toString());
                            holiydaychecking(siTO.getNextRunDt(), execDtIfHoliday);
                            if (DateUtil.dateDiff(checkThisCDate, siTO.getNextRunDt()) == 0) {
                                siTO.setExecDtHoliday(null);
                            } else {
                                siTO.setExecDtHoliday(checkThisCDate);
                            }
                            sqlMap.executeUpdate("updateStandingInstruction", siTO);
                            sqlMap.commitTransaction();
                        } catch (Exception transError) {
                            System.out.println("insert failed, first attempt");
                            transStatus = false;
                            sqlMap.rollbackTransaction();
                            System.out.println("Error in transaction 1 ....");
                            transError.printStackTrace();
                        }

                        if (transStatus == false) {                                                   //if Standing Instruction failed
                            try {
                                batchList = new ArrayList();
                                if (debitList != null && debitList.size() > 0) {
                                    for (int x = 0, y = debitList.size(); x < y; x++) {
                                        HashMap where = new HashMap();
                                        where.put("PROD_TYPE", ((StandingInstructionCreditTO) creditList.get(x)).getProdType());
                                        where.put("ACT_NUM", ((StandingInstructionDebitTO) debitList.get(x)).getAcctNo());
                                        if (!(where.get("PROD_TYPE").equals("RM"))) {
                                            StandingInstructionDebitTO standingInstructionDebitTO = (StandingInstructionDebitTO) debitList.get(x);
                                            if (!standingInstructionDebitTO.getProdType().equals("GL")) {
                                                HashMap branchidMap = (HashMap) sqlMap.executeQueryForObject("getBranchCode_ACT_MASTER"
                                                        + standingInstructionDebitTO.getProdType(), where);
                                                if (branchList != null && branchList.size() > 0) {
                                                    for (int b = 0; b < branchList.size(); b++) {
                                                        HashMap branchMap = (HashMap) branchList.get(b);
                                                        String branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                                                        if (CommonUtil.convertObjToStr(branchidMap.get(BRANCH_CODE)).equals(branch)) {
                                                            batchList.add(getDebitFailureChargesTransferTO(standingInstructionDebitTO, (StandingInstructionTO) siTO, branch));
                                                        }
                                                        branchMap = null;
                                                    }
                                                }
                                                branchidMap = null;
                                            }
                                        } else {
                                        }
                                    }
                                }
                                if (creditList != null && creditList.size() > 0) {
                                    for (int x = 0, z = creditList.size(); x < z; x++) {
                                        HashMap where = new HashMap();
                                        where.put("ACT_NUM", ((StandingInstructionCreditTO) creditList.get(x)).getAcctNo());
                                        where.put("PROD_ID", ((StandingInstructionCreditTO) creditList.get(x)).getProdId());
                                        where.put("PROD_TYPE", ((StandingInstructionCreditTO) creditList.get(x)).getProdType());
                                        prodType = CommonUtil.convertObjToStr(where.get("PROD_TYPE"));
                                        String mapName = "getDetailsSIAcNumCredit" + ((StandingInstructionCreditTO) creditList.get(x)).getProdType();
                                        StandingInstructionCreditTO standingInstructionCreditTO = (StandingInstructionCreditTO) creditList.get(x);
                                        if (branchList != null && branchList.size() > 0) {
                                            for (int b = 0; b < branchList.size(); b++) {
                                                HashMap branchMap = (HashMap) branchList.get(b);
                                                String branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                                                if (CommonUtil.convertObjToDouble(siTO.getSiCharges()).doubleValue() > 0) {
                                                    batchList.add(getSiFailChargeCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                                }
                                                if (CommonUtil.convertObjToDouble(siTO.getExecCharge()).doubleValue() > 0) {
                                                    batchList.add(getSiFailChargeSTCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                                }
                                                branchMap = null;
                                            }
                                        }
                                    }
                                }
                                sqlMap.startTransaction();
                                trans = new TransferTrans();
                                trans.setInitiatedBranch(branchID);
                                trans.setLoanDebitInt("DP");
                                if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                                    trans.doDebitCredit(batchList, _branchCode);
                                } else {
                                    trans.doDebitCredit(batchList, _branchCode, false);
                                }
                                trans.setLoanDebitInt("");
                                sqlMap.commitTransaction();
                            } catch (Exception transFailureEx) {
                                System.out.println("insert failed. Complete abort. Could not log");
                                sqlMap.rollbackTransaction();
                                System.out.println("Error in transaction 2 ....");
                                transFailureEx.printStackTrace();
                            } finally {
                                sqlMap.startTransaction();
                                Date startDate;
                                double count = siTO.getCount().doubleValue();
                                double fwdCount = siTO.getCarriedForwardCount().doubleValue();
                                sqlMap.executeUpdate("insertStandingInstructionTaskTO", getStandingInstructionTaskTO(siTO, false));

                                if ((count == 0 || count <= fwdCount) && (fwdCount > 0)) {
                                    double updatedCnt = 0;
                                    HashMap ctn = new HashMap();
                                    count = count + 1;
                                    ctn.put("SI_NO", siTO.getSiId());
                                    ctn.put("COUNT", new Double(count));
                                    sqlMap.executeUpdate("updateCount", ctn);
                                    List cntlist = sqlMap.executeQueryForList("getCount", ctn);
                                    System.out.println("cntlist%%%%%5" + cntlist);
                                    ctn = null;
                                    if (cntlist.size() > 0 && cntlist != null) {
                                        ctn = (HashMap) cntlist.get(0);
                                        updatedCnt = CommonUtil.convertObjToDouble(ctn.get("COUNT")).doubleValue();
//                                        System.out.println("updatedCnt%%%"+updatedCnt);
                                    }
                                    if (fwdCount >= updatedCnt) {
                                        if ((lastDate.equals("")) && (nextDate.equals(""))) {                 // if Standing Instruction running for the first time
                                            siTO.setNextRunDt(DateUtil.addDaysProperFormat(startDt, CommonUtil.convertObjToInt(siTO.getFrequency())));
                                            Date nxtCalDt = nextCaldate(startDt, siTO.getNextRunDt());
//                                            System.out.println("!!!nxtCalDt####"+nxtCalDt);
                                            siTO.setNextRunDt(nxtCalDt);
                                            siTO.setLastRunDt(curDate);
                                        } else {                                                               // if Standing Instruction has already run
                                            siTO.setLastRunDt(curDate);
                                            if (fwdDt.equals("")) {
                                                siTO.setNextRunDt(DateUtil.addDaysProperFormat(nxtDate, CommonUtil.convertObjToInt(siTO.getFrequency())));
                                                Date nxtCalDt = nextCaldate(startDt, siTO.getNextRunDt());
//                                                System.out.println("!!!nxtCalDt####"+nxtCalDt);
                                                siTO.setNextRunDt(nxtCalDt);
                                            }
                                        }
                                        siTO.setExecDtHoliday(null);
                                        siTO.setForwardRunDt(DateUtil.addDaysProperFormat(curDate, 1));
                                        execDtIfHoliday = "NEXTDAY";
                                        holiydaychecking(siTO.getForwardRunDt(), execDtIfHoliday);          // Holiday checking
                                        if (DateUtil.dateDiff(checkThisCDate, siTO.getForwardRunDt()) == 0) {
                                            //do nothing
                                        } else {
                                            siTO.setForwardRunDt(checkThisCDate);
                                        }
                                    } else {                                                                  // if Carried Fwd Count is beyound the specefied count
                                        ctn.put("SI_NO", siTO.getSiId());
                                        ctn.put("COUNT", new Double(0));
                                        sqlMap.executeUpdate("updateCount", ctn);
                                        siTO.setForwardRunDt(null);
                                        siTO.setLastRunDt(curDate);
                                        String execDtHol = siTO.getChangeHolidayExec();
                                        holiydaychecking(siTO.getNextRunDt(), execDtHol);
                                        if (DateUtil.dateDiff(checkThisCDate, siTO.getNextRunDt()) == 0) {
                                            siTO.setExecDtHoliday(null);
                                        } else {
                                            siTO.setExecDtHoliday(checkThisCDate);
                                        }
                                    }
                                } else {
                                    // if carried count is 0
                                    String execDtHol = siTO.getChangeHolidayExec();
                                    if ((lastDate.equals("")) && (nextDate.equals(""))) {                             // Standing Instruction running for the first time
                                        siTO.setNextRunDt(DateUtil.addDaysProperFormat(startDt, CommonUtil.convertObjToInt(siTO.getFrequency())));
                                        Date nxtCalDt = nextCaldate(startDt, siTO.getNextRunDt());
//                                        System.out.println("!!!nxtCalDt####"+nxtCalDt);
                                        siTO.setNextRunDt(nxtCalDt);
                                        siTO.setLastRunDt(curDate);
//                                        System.out.println("siTO : " + siTO.toString());
                                        holiydaychecking(siTO.getNextRunDt(), execDtHol);
                                        if (DateUtil.dateDiff(checkThisCDate, siTO.getNextRunDt()) == 0) {
                                            siTO.setExecDtHoliday(null);
                                        } else {
                                            siTO.setExecDtHoliday(checkThisCDate);
                                        }
                                    } else {                                                                           // Standing Instruction has already run
                                        siTO.setLastRunDt(curDate);
                                        siTO.setNextRunDt(DateUtil.addDaysProperFormat(nxtDate, CommonUtil.convertObjToInt(siTO.getFrequency())));
                                        Date nxtCalDt = nextCaldate(startDt, siTO.getNextRunDt());
//                                        System.out.println("!!!nxtCalDt####"+nxtCalDt);
                                        siTO.setNextRunDt(nxtCalDt);
//                                        System.out.println("siTO : " + siTO.toString());
                                        holiydaychecking(siTO.getNextRunDt(), execDtHol);
                                        if (DateUtil.dateDiff(checkThisCDate, siTO.getNextRunDt()) == 0) {
                                            siTO.setExecDtHoliday(null);
                                        } else {
                                            siTO.setExecDtHoliday(checkThisCDate);
                                        }
                                    }
                                }
                                errorMap = new HashMap();
                                errorMap.put("STATUS", "ERROR");
                                errorMap.put("SI_NO", siTO.getSiId());
                                errorMap.put("FWD_RUN_DT", siTO.getForwardRunDt());
                                errorMap.put("NXT_RUN_DT", siTO.getNextRunDt());
                                errorMap.put("LST_RUN_DT", siTO.getLastRunDt());
                                errorMap.put("EXE_HOL_DT", siTO.getExecDtHoliday());
                                System.out.println("==============>" + errorMap);
                                sqlMap.executeUpdate("updateStandingInstructionFailure", errorMap);
                                errorMap = null;
                                sqlMap.commitTransaction();
                            }
                        }
                        if (transStatus == true) {
                            status.setExecutionCount(k);
                        }

                        batchList = null;
                    }
                } else {                                                                        //IF WEEK, WEEKDAY MENTIONED
                    fwdDt = CommonUtil.convertObjToStr(siTO.getForwardRunDt());
                    execDtIfHoliday = CommonUtil.convertObjToStr(siTO.getChangeHolidayExec());
                    System.out.println("execDtIfHoliday#########:" + execDtIfHoliday);
                    branchID = CommonUtil.convertObjToStr(siTO.getBranchCode());
                    lstDt = siTO.getLastRunDt();
                    lastDate = CommonUtil.convertObjToStr(siTO.getLastRunDt());
                    siTO.setLastRunDt(curDate);
                    nextDate = CommonUtil.convertObjToStr(siTO.getNextRunDt());
                    nxtDate = siTO.getNextRunDt();
                    startDt = siTO.getSiStartDt();
                    minBal = siTO.getMinBalance().doubleValue();
                    multiples = siTO.getMultiplesOf().doubleValue();
                    String debitActNo = "";
                    double clearBal = 0;

                    batchList = new ArrayList();
                    HashMap siId = new HashMap();
                    siId.put("SAL_REC_MODULE", CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE));
                    siId.put("SI_ID", siTO.getSiId());
                    siId.put("ASONDATE",curDate.clone()); // Added by nithya on 03-03-2017 for 5763 [ RBI ]
                    List creditList = sqlMap.executeQueryForList("getSelectStandingInstructionCreditTO", siId);
                    List debitList = sqlMap.executeQueryForList("getSelectStandingInstructionDebitTO", siId);
                    System.out.println("inside for loop ####### runBatch creditList :" + creditList);
                    System.out.println("inside for loop ####### runBatch debitList :" + debitList);


                    //Added By Suresh
                    boolean loanFlag = false;
                    HashMap loanMap = new HashMap();
                    HashMap depMap = new HashMap();
                    if (creditList != null && creditList.size() > 0) {                                                // Credit List
                        for (int x = 0, z = creditList.size(); x < z; x++) {
                            HashMap where = new HashMap();
                            where.put("ACT_NUM", ((StandingInstructionCreditTO) creditList.get(x)).getAcctNo());
                            where.put("PROD_ID", ((StandingInstructionCreditTO) creditList.get(x)).getProdId());
                            where.put("PROD_TYPE", ((StandingInstructionCreditTO) creditList.get(x)).getProdType());
                            prodType = CommonUtil.convertObjToStr(where.get("PROD_TYPE"));
                            //Added By Suresh Set All_Amount
                            if (where.get("PROD_TYPE").equals("TL") || where.get("PROD_TYPE").equals("AD")) {
                                loanMap = calcLoanPayments(CommonUtil.convertObjToStr(where.get("ACT_NUM")),
                                        CommonUtil.convertObjToStr(where.get("PROD_ID")), CommonUtil.convertObjToStr(where.get("PROD_TYPE")));
                                if (loanMap != null) {
                                    debitAmt = CommonUtil.convertObjToDouble(loanMap.get("TOTAL_DEMAND")).doubleValue();
                                }
                                loanFlag = true;
                            }
                            if (where.get("PROD_TYPE").equals("TD")) {
                                depMap = calcTermDeposits(CommonUtil.convertObjToStr(where.get("ACT_NUM")),
                                        CommonUtil.convertObjToStr(where.get("PROD_ID")), CommonUtil.convertObjToStr(where.get("PROD_TYPE")));
                                if (depMap != null) {
                                    debitAmt = CommonUtil.convertObjToDouble(depMap.get("TOTAL_DEMAND")).doubleValue();
                                }
                                loanFlag = true;
                            }
                        }
                    }

                    if (debitList != null && debitList.size() > 0) {                                          // Debit list
                        for (int x = 0, y = debitList.size(); x < y; x++) {
                            HashMap where = new HashMap();
                            String mapName = "getDetailsSIAcNumDebit" + ((StandingInstructionDebitTO) debitList.get(x)).getProdType();
                            where.put("ACT_NUM", ((StandingInstructionDebitTO) debitList.get(x)).getAcctNo());
                            where.put("PROD_ID", ((StandingInstructionDebitTO) debitList.get(x)).getAcHdId());
                            where.put("AMOUNT", ((StandingInstructionDebitTO) debitList.get(x)).getAmount());
                            debitActNo = CommonUtil.convertObjToStr(where.get("ACT_NUM"));
                            String debitMapName = "";
                            if (!loanFlag) {
                                if (where.get("PROD_TYPE").equals("OA")) {
                                    debitMapName = "getTransActDetails";
                                } else if (where.get("PROD_TYPE").equals("SA")) {
                                    debitMapName = "getTransActDetailsSA";
                                }
                                List clearBalLst = sqlMap.executeQueryForList(debitMapName, debitActNo);
                                if (clearBalLst != null && clearBalLst.size() > 0) {
                                    HashMap clearBalMap = new HashMap();
                                    clearBalMap = (HashMap) clearBalLst.get(0);
                                    System.out.println("!!!!!!!!!!!!clearBalMap" + clearBalMap);
                                    clearBal = CommonUtil.convertObjToDouble(clearBalMap.get("clearBalance")).doubleValue();
                                    System.out.println("!!!!!!!!!!!!clearBal!!!!!!!!!!" + clearBal + "@@@@minbal" + minBal);
                                    double amt = clearBal - minBal;
                                    System.out.println("!!!!!!!!!!!!amt!!!!!!!!!!" + amt);
                                    debitAmt = ((int) (amt / multiples)) * multiples;
                                    System.out.println("!!!!!!!!!!!!!debitAmt!!!!!!!!!" + debitAmt);
                                }
                            }
                            if (!((StandingInstructionDebitTO) debitList.get(x)).getProdType().equals("GL")) {
                                where = null;
                            }
                            StandingInstructionDebitTO standingInstructionDebitTO = (StandingInstructionDebitTO) debitList.get(x);
                            if (!standingInstructionDebitTO.getProdType().equals("GL")) {
                                HashMap branchidMap = (HashMap) sqlMap.executeQueryForObject("getBranchCode_ACT_MASTER"
                                        + standingInstructionDebitTO.getProdType(), where);
                                if (branchList != null && branchList.size() > 0) {
                                    for (int b = 0; b < branchList.size(); b++) {
                                        HashMap branchMap = (HashMap) branchList.get(b);
                                        String branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                                        if (CommonUtil.convertObjToStr(branchidMap.get(BRANCH_CODE)).equals(branch)) {
                                            batchList.add(getDebitTransferTO(standingInstructionDebitTO, (StandingInstructionTO) siTO, branch));
                                        }
                                        branchMap = null;
                                    }
                                }
                                branchidMap = null;
                            }
                        }
                    }
                    TransferTrans trans = new TransferTrans();
                    if (creditList != null && creditList.size() > 0) {                                    // Credit List
                        for (int x = 0, z = creditList.size(); x < z; x++) {

                            HashMap where = new HashMap();
                            where.put("ACT_NUM", ((StandingInstructionCreditTO) creditList.get(x)).getAcctNo());
                            where.put("PROD_ID", ((StandingInstructionCreditTO) creditList.get(x)).getProdId());
                            where.put("PROD_TYPE", ((StandingInstructionCreditTO) creditList.get(x)).getProdType());
                            String mapName = "getDetailsSIAcNumCredit" + ((StandingInstructionCreditTO) creditList.get(x)).getProdType();
                            StandingInstructionCreditTO standingInstructionCreditTO = (StandingInstructionCreditTO) creditList.get(x);
                            if (!standingInstructionCreditTO.getProdType().equals("GL")) {
                                HashMap branchidMap = (HashMap) sqlMap.executeQueryForObject("getBranchCode_ACT_MASTER"
                                        + standingInstructionCreditTO.getProdType(), where);
                                if (branchList != null && branchList.size() > 0) {
                                    for (int b = 0; b < branchList.size(); b++) {
                                        HashMap branchMap = (HashMap) branchList.get(b);
                                        String branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                                        if (CommonUtil.convertObjToStr(branchidMap.get(BRANCH_CODE)).equals(branch)) {
                                            batchList.add(getCreditTransferTO(standingInstructionCreditTO, (StandingInstructionTO) siTO, branch));
                                        }
                                        branchMap = null;
                                    }
                                }
                                branchidMap = null;
                            }
                            if ((where.get("PROD_TYPE").equals("TD"))) {
                                String behaves = "";
                                String inst_beyond_maturity = "";
                                List behavesList = sqlMap.executeQueryForList("getSiBeyondMaturity", where);
                                if (behavesList.size() > 0) {
                                    HashMap behMap = (HashMap) behavesList.get(0);
                                    behaves = behMap.get("BEHAVES_LIKE").toString();
                                    inst_beyond_maturity = behMap.get("INST_BEYOND_MATURITY_DATE").toString();//INST_BEYOND_MATURITY_DATE
                                    if (behaves.equals("RECURRING")) {
                                        String actNum = CommonUtil.convertObjToStr(where.get("ACT_NUM"));
                                        actNum = actNum.substring(0, actNum.lastIndexOf("_"));
                                        where.put("ACT_NUM", actNum);
                                        List depVal = sqlMap.executeQueryForList("getDepValidateData", where);
                                        HashMap where1 = new HashMap();
                                        if ((depVal != null) && (depVal.size() > 0)) {
                                            where1 = (HashMap) depVal.get(0);
                                            String actStatus = CommonUtil.convertObjToStr(where1.get("ACCT_STATUS"));
                                            Date matDate = (Date) where1.get("MATURITY_DT");
                                            Date todayDate = (Date) paramMap.get("TODAY_DT");
                                            int total = CommonUtil.convertObjToInt(where1.get("TOTAL_INSTALLMENTS"));
                                            int paid = CommonUtil.convertObjToInt(where1.get("TOTAL_INSTALL_PAID"));
                                            System.out.println("+++actStatus" + actStatus + "%%%%%matDate" + matDate);
                                            System.out.println("************" + DateUtil.dateDiff(matDate, todayDate));
                                            System.out.println("inst_beyond_maturity" + inst_beyond_maturity);

                                            if (inst_beyond_maturity.equals("N")) {
                                                if ((actStatus.equalsIgnoreCase("CLOSED"))
                                                        || (DateUtil.dateDiff(matDate, todayDate) > 0)) {
                                                    depValidate = false;
                                                }
                                            } else if (inst_beyond_maturity.equals("Y")) {
                                                if ((actStatus.equalsIgnoreCase("CLOSED"))
                                                        || ((total - paid) == 0)) {
                                                    depValidate = false;
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                            //Added By Suresh Set All_Amount Map
                            if (where.get("PROD_TYPE").equals("TL") || where.get("PROD_TYPE").equals("AD")) {
                                if (loanMap != null) {
                                    trans.setAllAmountMap(loanMap);
                                }
                            }
                            if (depValidate && where.get("PROD_TYPE").equals("TD")) {
                                if (depMap != null) {
                                    trans.setDepPenalMap(depMap);
                                }
                            }
                        }
                    }
                    if (depValidate) {
                        System.out.println("batchList    " + batchList);

                        try {
                            System.out.println("trying insert.............");
                            sqlMap.startTransaction();
//                            TransferTrans trans = new TransferTrans();
                            trans.setInitiatedBranch(branchID);
                            if (siTO.getAutomaticPosting().equalsIgnoreCase("Y")) {
                                trans.doDebitCredit(batchList, _branchCode);
                            } else {
                                trans.doDebitCredit(batchList, _branchCode, false);
                            }
                            sqlMap.executeUpdate("insertStandingInstructionTaskTO", getStandingInstructionTaskTO(siTO, true));
                            siTO.setForwardRunDt(null);
                            String week = "";
                            String weekDay = "";
                            week = siTO.getWeek();
                            weekDay = siTO.getWeekDay();
                            int weekCnt = 0;
                            int calWeek = 0;
                            if (week != null) {
                                if (week.equalsIgnoreCase("FIRSTWEEK")) {
                                    weekCnt = 1;
                                } else if (week.equalsIgnoreCase("SECONDWEEK")) {
                                    weekCnt = 2;
                                } else if (week.equalsIgnoreCase("THIRDWEEK")) {
                                    weekCnt = 3;
                                } else if (week.equalsIgnoreCase("FOURTHWEEK")) {
                                    weekCnt = 4;
                                } else {
                                    weekCnt = 5;
                                }
                            }
                            if ((!nextDate.equals("")) && (!lastDate.equals(""))) {           // if Standing Instruction has already run
                                if ((weekDay != null) && (week == null)) {
                                    // for only weekday specified
                                    // if only weekday like monday,tue.... is mentioned
                                    siTO.setLastRunDt(curDate);
                                    if (fwdDt.equals("")) {
                                        siTO.setNextRunDt(DateUtil.addDaysProperFormat(siTO.getNextRunDt(), 7));
                                    }
//                                System.out.println("!!!11"+siTO.getNextRunDt());
                                } else {
                                    // if weekday and week is mentioned
                                    //                                Date nxtDt = new Date(siTO.getLastRunDt().getYear(),siTO.getLastRunDt().getMonth(),siTO.getLastRunDt().getDate());abi
                                    Date nxtDt = siTO.getLastRunDt();
                                    Calendar c1 = new GregorianCalendar(nxtDt.getYear() + 1900, nxtDt.getMonth(), nxtDt.getDate());
//                                System.out.println("### Date : "+c1.getTime());
                                    int weeks = (c1.getActualMaximum(c1.DATE) / 7) > 4 ? 5 : 4;
                                    c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, weeks);
//                                System.out.println("### Date : "+c1.getTime());
                                    HashMap dateMap = new HashMap();
                                    dateMap.put("TODAY_DT", c1.getTime());
//                                System.out.println("### dateMap : "+dateMap);
                                    List lst = sqlMap.executeQueryForList("getWeekCnt", dateMap);
                                    dateMap = null;
                                    if (lst != null && lst.size() > 0) {
                                        dateMap = (HashMap) lst.get(0);
                                        calWeek = CommonUtil.convertObjToInt(dateMap.get("CAL_DT"));
//                                    System.out.println("### dateMap : "+dateMap);
                                    }
//                                System.out.println("### dateMap : "+calWeek);
//                                System.out.println("### dateMap : "+week);
                                    if (calWeek != weekCnt) {
                                        if (calWeek == weekCnt + 1) {
                                            c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
                                        } else {
                                            c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
                                        }
//                                    System.out.println("### c1****** : "+c1.getTime());
                                    }
                                    if (fwdDt.equals("")) {
                                        siTO.setNextRunDt(c1.getTime());
                                    }
                                    siTO.setLastRunDt(curDate);
//                                System.out.println("!!!11"+siTO.getNextRunDt());
                                }
                            } else {                                                                  // if Standing Instruction is running for the first time
                                if ((weekDay != null) && (week == null)) {
                                    // for only weekday specified
                                    // if only weekday like monday,tue.... is mentioned
                                    siTO.setNextRunDt(DateUtil.addDaysProperFormat(startDt, 7));
                                    siTO.setLastRunDt(curDate);
//                                System.out.println("!!!1222"+siTO.getNextRunDt());
                                } else {
                                    // if week and weekday is mentioned
                                    //                                Date nxtDt = new Date(siTO.getSiStartDt().getYear(),siTO.getSiStartDt().getMonth(),siTO.getSiStartDt().getDate());abi
                                    Date nxtDt = siTO.getSiStartDt();
//                                System.out.println("### nxtDt : "+nxtDt);
                                    Calendar c1 = new GregorianCalendar(nxtDt.getYear() + 1900, nxtDt.getMonth(), nxtDt.getDate());
//                                System.out.println("###c1 : "+c1.getTime());
                                    int weeks = (c1.getActualMaximum(c1.DATE) / 7) > 4 ? 5 : 4;
                                    c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, weeks);
//                                System.out.println("###c1 : "+c1);
//                                System.out.println("### Date : "+c1.getTime());
                                    HashMap dateMap = new HashMap();
                                    dateMap.put("TODAY_DT", c1.getTime());
//                                System.out.println("### dateMap : "+dateMap);
                                    List lst = sqlMap.executeQueryForList("getWeekCnt", dateMap);
                                    dateMap = null;
                                    if (lst != null && lst.size() > 0) {
                                        dateMap = (HashMap) lst.get(0);
                                        calWeek = CommonUtil.convertObjToInt(dateMap.get("CAL_DT"));
//                                    System.out.println("### dateMap : "+dateMap);
                                    }
//                                System.out.println("### dateMap : "+calWeek);
//                                System.out.println("### dateMap : "+week);
                                    if (calWeek != weekCnt) {
                                        if (calWeek == weekCnt + 1) {
                                            c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
                                        } else {
                                            c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
                                        }
//                                    System.out.println("### c1****** : "+c1.getTime());
                                    }
                                    siTO.setNextRunDt(c1.getTime());
                                    siTO.setLastRunDt(curDate);
//                                System.out.println("!!!1222"+siTO.getNextRunDt());
                                }
                            }
                            siTO.setCount(new Double(0));
//                        System.out.println("siTO : " + siTO.toString());
                            holiydaychecking(siTO.getNextRunDt(), execDtIfHoliday);
                            if (DateUtil.dateDiff(checkThisCDate, siTO.getNextRunDt()) == 0) {
                                siTO.setExecDtHoliday(null);
                            } else {
                                siTO.setExecDtHoliday(checkThisCDate);
                            }
                            sqlMap.executeUpdate("updateStandingInstruction", siTO);
                            sqlMap.commitTransaction();
                        } catch (Exception transError) {
                            System.out.println("insert failed, first attempt");
                            transStatus = false;
                            sqlMap.rollbackTransaction();
                            System.out.println("Error in transaction 1 ....");
                            transError.printStackTrace();
                        }

                        if (transStatus == false) {                                               // If SI failed
                            try {
                                Date startDate;
                                double count = siTO.getCount().doubleValue();
                                double fwdCount = siTO.getCarriedForwardCount().doubleValue();
//                            System.out.println("count%%%"+count);
//                            System.out.println("trying insert second time");
                                sqlMap.startTransaction();
                                sqlMap.executeUpdate("insertStandingInstructionTaskTO", getStandingInstructionTaskTO(siTO, false));
                                String week = "";
                                String weekDay = "";
                                week = siTO.getWeek();
                                weekDay = siTO.getWeekDay();
                                int weekCnt = 0;
                                int calWeek = 0;
                                if (week != null) {
                                    if (week.equalsIgnoreCase("FIRSTWEEK")) {
                                        weekCnt = 1;
                                    } else if (week.equalsIgnoreCase("SECONDWEEK")) {
                                        weekCnt = 2;
                                    } else if (week.equalsIgnoreCase("THIRDWEEK")) {
                                        weekCnt = 3;
                                    } else if (week.equalsIgnoreCase("FOURTHWEEK")) {
                                        weekCnt = 4;
                                    } else {
                                        weekCnt = 5;
                                    }
                                }
                                if ((count == 0 || count <= fwdCount) && (fwdCount > 0)) {
                                    double updatedCnt = 0;
                                    HashMap ctn = new HashMap();
                                    count = count + 1;
                                    ctn.put("SI_NO", siTO.getSiId());
                                    ctn.put("COUNT", new Double(count));
                                    sqlMap.executeUpdate("updateCount", ctn);
                                    List cntlist = sqlMap.executeQueryForList("getCount", ctn);
//                                System.out.println("cntlist%%%%%5"+cntlist);
                                    ctn = null;
                                    if (cntlist.size() > 0 && cntlist != null) {
                                        ctn = (HashMap) cntlist.get(0);
                                        updatedCnt = CommonUtil.convertObjToDouble(ctn.get("COUNT")).doubleValue();
//                                    System.out.println("updatedCnt%%%"+updatedCnt);
                                    }
                                    if (fwdCount >= updatedCnt) {
                                        if ((lastDate.equals("")) && (nextDate.equals(""))) {                 // SI running for the first time
                                            if ((weekDay != null) && (week == null)) {
                                                // for only weekday specified
                                                // if only weekday like monday,tue.... is mentioned
                                                siTO.setNextRunDt(DateUtil.addDaysProperFormat(startDt, 7));
                                                siTO.setLastRunDt(curDate);
                                            } else {
                                                // if only week, weekday is mentioned
                                                //                                            Date nxtDt = new Date(siTO.getSiStartDt().getYear(),siTO.getSiStartDt().getMonth(),siTO.getSiStartDt().getDate());abi
                                                Date nxtDt = siTO.getSiStartDt();
//                                            System.out.println("### nxtDt : "+nxtDt);
                                                Calendar c1 = new GregorianCalendar(nxtDt.getYear() + 1900, nxtDt.getMonth(), nxtDt.getDate());
//                                            System.out.println("###c1 : "+c1.getTime());
                                                int weeks = (c1.getActualMaximum(c1.DATE) / 7) > 4 ? 5 : 4;
                                                c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, weeks);
//                                            System.out.println("###c1 : "+c1);
//                                            System.out.println("### Date : "+c1.getTime());
                                                HashMap dateMap = new HashMap();
                                                dateMap.put("TODAY_DT", c1.getTime());
//                                            System.out.println("### dateMap : "+dateMap);
                                                List lst = sqlMap.executeQueryForList("getWeekCnt", dateMap);
                                                dateMap = null;
                                                if (lst != null && lst.size() > 0) {
                                                    dateMap = (HashMap) lst.get(0);
                                                    calWeek = CommonUtil.convertObjToInt(dateMap.get("CAL_DT"));
//                                                System.out.println("### dateMap : "+dateMap);
                                                }
//                                            System.out.println("### dateMap : "+calWeek);
//                                            System.out.println("### dateMap : "+week);
                                                if (calWeek != weekCnt) {
                                                    if (calWeek == weekCnt + 1) {
                                                        c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
                                                    } else {
                                                        c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
                                                    }
//                                                System.out.println("### c1****** : "+c1.getTime());
                                                }
                                                siTO.setNextRunDt(c1.getTime());
                                                siTO.setLastRunDt(curDate);
                                            }
                                        } else {                                                               // SI has already run
                                            if ((weekDay != null) && (week == null)) {
                                                // for only weekday specified
                                                // if only weekday like monday,tue.... is mentioned
                                                siTO.setLastRunDt(curDate);
                                                if (fwdDt.equals("")) {
                                                    siTO.setNextRunDt(DateUtil.addDaysProperFormat(nxtDate, 7));
                                                }

                                            } else {
                                                // if only week,weekday like monday,tue.... is mentioned
                                                //                                            Date nxtDt = new Date(siTO.getNextRunDt().getYear(),siTO.getNextRunDt().getMonth(),siTO.getNextRunDt().getDate());abi
                                                Date nxtDt = siTO.getSiStartDt();
//                                            System.out.println("### nxtDt : "+nxtDt);
                                                Calendar c1 = new GregorianCalendar(nxtDt.getYear() + 1900, nxtDt.getMonth(), nxtDt.getDate());
//                                            System.out.println("###c1 : "+c1.getTime());
                                                int weeks = (c1.getActualMaximum(c1.DATE) / 7) > 4 ? 5 : 4;
                                                c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, weeks);
//                                            System.out.println("###c1 : "+c1);
//                                            System.out.println("### Date : "+c1.getTime());
                                                HashMap dateMap = new HashMap();
                                                dateMap.put("TODAY_DT", c1.getTime());
//                                            System.out.println("### dateMap : "+dateMap);
                                                List lst = sqlMap.executeQueryForList("getWeekCnt", dateMap);
                                                dateMap = null;
                                                if (lst != null && lst.size() > 0) {
                                                    dateMap = (HashMap) lst.get(0);
                                                    calWeek = CommonUtil.convertObjToInt(dateMap.get("CAL_DT"));
//                                                System.out.println("### dateMap : "+dateMap);
                                                }
//                                            System.out.println("### dateMap : "+calWeek);
//                                            System.out.println("### dateMap : "+week);
                                                if (calWeek != weekCnt) {
                                                    if (calWeek == weekCnt + 1) {
                                                        c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
                                                    } else {
                                                        c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
                                                    }
//                                                System.out.println("### c1****** : "+c1.getTime());
                                                }
                                                siTO.setLastRunDt(curDate);
                                                if (fwdDt.equals("")) {
                                                    siTO.setNextRunDt(c1.getTime());
                                                }
                                            }
                                        }
                                        siTO.setExecDtHoliday(null);
                                        siTO.setForwardRunDt(DateUtil.addDaysProperFormat(curDate, 1));
                                        execDtIfHoliday = "NEXTDAY";
                                        holiydaychecking(siTO.getForwardRunDt(), execDtIfHoliday);
                                        if (DateUtil.dateDiff(checkThisCDate, siTO.getForwardRunDt()) == 0) {
                                            //do nothing
                                        } else {
                                            siTO.setForwardRunDt(checkThisCDate);
                                        }
                                    } else {
                                        ctn.put("SI_NO", siTO.getSiId());
                                        ctn.put("COUNT", new Double(0));
                                        sqlMap.executeUpdate("updateCount", ctn);
                                        siTO.setForwardRunDt(null);
                                        siTO.setLastRunDt(curDate);
                                        String execDtHol = siTO.getChangeHolidayExec();
                                        holiydaychecking(nxtDate, execDtHol);
                                        if (DateUtil.dateDiff(checkThisCDate, nxtDate) == 0) {
                                            siTO.setExecDtHoliday(null);
                                        } else {
                                            siTO.setExecDtHoliday(checkThisCDate);
                                        }
                                    }
                                } else {
                                    // if carried count is 0
                                    String execDtHol = siTO.getChangeHolidayExec();
                                    if ((lastDate.equals("")) && (nextDate.equals(""))) {                 // if SI running for the first time
                                        if ((weekDay != null) && (week == null)) {
                                            // for only weekday specified
                                            // if only weekday like monday,tue.... is mentioned
                                            siTO.setNextRunDt(DateUtil.addDaysProperFormat(startDt, 7));
                                            siTO.setLastRunDt(curDate);
                                            siTO.setCount(new Double(0));
//                                        System.out.println("siTO : " + siTO.toString());
                                            holiydaychecking(siTO.getNextRunDt(), execDtIfHoliday);
                                            if (DateUtil.dateDiff(checkThisCDate, siTO.getNextRunDt()) == 0) {
                                                siTO.setExecDtHoliday(null);
                                            } else {
                                                siTO.setExecDtHoliday(checkThisCDate);
                                            }
                                        } else {
                                            // if only week, weekday is mentioned
                                            //                                        Date nxtDt = new Date(siTO.getSiStartDt().getYear(),siTO.getSiStartDt().getMonth(),siTO.getSiStartDt().getDate());abi
                                            Date nxtDt = siTO.getSiStartDt();
//                                        System.out.println("### nxtDt : "+nxtDt);
                                            Calendar c1 = new GregorianCalendar(nxtDt.getYear() + 1900, nxtDt.getMonth(), nxtDt.getDate());
//                                        System.out.println("###c1 : "+c1.getTime());
                                            int weeks = (c1.getActualMaximum(c1.DATE) / 7) > 4 ? 5 : 4;
                                            c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, weeks);
//                                        System.out.println("###c1 : "+c1);
//                                        System.out.println("### Date : "+c1.getTime());
                                            HashMap dateMap = new HashMap();
                                            dateMap.put("TODAY_DT", c1.getTime());
//                                        System.out.println("### dateMap : "+dateMap);
                                            List lst = sqlMap.executeQueryForList("getWeekCnt", dateMap);
                                            dateMap = null;
                                            if (lst != null && lst.size() > 0) {
                                                dateMap = (HashMap) lst.get(0);
                                                calWeek = CommonUtil.convertObjToInt(dateMap.get("CAL_DT"));
//                                            System.out.println("### dateMap : "+dateMap);
                                            }
//                                        System.out.println("### dateMap : "+calWeek);
//                                        System.out.println("### dateMap : "+week);
                                            if (calWeek != weekCnt) {
                                                if (calWeek == weekCnt + 1) {
                                                    c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
                                                } else {
                                                    c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
                                                }
//                                            System.out.println("### c1****** : "+c1.getTime());
                                            }
                                            siTO.setNextRunDt(c1.getTime());
                                            siTO.setLastRunDt(curDate);
//                                        System.out.println("siTO : " + siTO.toString());
                                            holiydaychecking(siTO.getNextRunDt(), execDtHol);
                                            if (DateUtil.dateDiff(checkThisCDate, siTO.getNextRunDt()) == 0) {
                                                siTO.setExecDtHoliday(null);
                                            } else {
                                                siTO.setExecDtHoliday(checkThisCDate);
                                            }
                                        }
                                    } else {                                                              // if SI has already run
                                        if ((weekDay != null) && (week == null)) {
                                            // for only weekday specified
                                            // if only weekday like monday,tue.... is mentioned
                                            siTO.setLastRunDt(curDate);
//                                        System.out.println("nxtDate****%%"+nxtDate);
                                            siTO.setNextRunDt(DateUtil.addDaysProperFormat(nxtDate, 7));
//                                        System.out.println("siTO.getNextRunDt()##%%%"+siTO.getNextRunDt());
                                            siTO.setCount(new Double(0));
//                                        System.out.println("siTO : " + siTO.toString());
                                            holiydaychecking(siTO.getNextRunDt(), execDtIfHoliday);
                                            if (DateUtil.dateDiff(checkThisCDate, siTO.getNextRunDt()) == 0) {
                                                siTO.setExecDtHoliday(null);
                                            } else {
                                                siTO.setExecDtHoliday(checkThisCDate);
                                            }
                                        } else {
                                            // if only week, weekday is mentioned
                                            //                                        Date nxtDt = new Date(siTO.getNextRunDt().getYear(),siTO.getNextRunDt().getMonth(),siTO.getNextRunDt().getDate());
                                            Date nxtDt = siTO.getSiStartDt();
//                                        System.out.println("### nxtDt : "+nxtDt);
                                            Calendar c1 = new GregorianCalendar(nxtDt.getYear() + 1900, nxtDt.getMonth(), nxtDt.getDate());
//                                        System.out.println("###c1 : "+c1.getTime());
                                            int weeks = (c1.getActualMaximum(c1.DATE) / 7) > 4 ? 5 : 4;
                                            c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, weeks);
//                                        System.out.println("###c1 : "+c1);
//                                        System.out.println("### Date : "+c1.getTime());
                                            HashMap dateMap = new HashMap();
                                            dateMap.put("TODAY_DT", c1.getTime());
//                                        System.out.println("### dateMap : "+dateMap);
                                            List lst = sqlMap.executeQueryForList("getWeekCnt", dateMap);
                                            dateMap = null;
                                            if (lst != null && lst.size() > 0) {
                                                dateMap = (HashMap) lst.get(0);
                                                calWeek = CommonUtil.convertObjToInt(dateMap.get("CAL_DT"));
//                                            System.out.println("### dateMap : "+dateMap);
                                            }
//                                        System.out.println("### dateMap : "+calWeek);
//                                        System.out.println("### dateMap : "+week);
                                            if (calWeek != weekCnt) {
                                                if (calWeek == weekCnt + 1) {
                                                    c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
                                                } else {
                                                    c1.add(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
                                                }
//                                            System.out.println("### c1****** : "+c1.getTime());
                                            }
                                            siTO.setNextRunDt(c1.getTime());
                                            siTO.setLastRunDt(curDate);
                                            holiydaychecking(siTO.getNextRunDt(), execDtHol);
                                            if (DateUtil.dateDiff(checkThisCDate, siTO.getNextRunDt()) == 0) {
                                                siTO.setExecDtHoliday(null);
                                            } else {
                                                siTO.setExecDtHoliday(checkThisCDate);
                                            }
                                        }
                                    }
                                }
                                errorMap = new HashMap();
                                errorMap.put("STATUS", "ERROR");
                                errorMap.put("SI_NO", siTO.getSiId());
                                errorMap.put("FWD_RUN_DT", siTO.getForwardRunDt());
                                errorMap.put("NXT_RUN_DT", siTO.getNextRunDt());
                                errorMap.put("LST_RUN_DT", siTO.getLastRunDt());
                                errorMap.put("EXE_HOL_DT", siTO.getExecDtHoliday());
                                System.out.println("==============>" + errorMap);
                                sqlMap.executeUpdate("updateStandingInstructionFailure", errorMap);
                                errorMap = null;
                                sqlMap.commitTransaction();
                            } catch (Exception transFailureEx) {
                                System.out.println("insert failed. Complete abort. Could not log");
                                sqlMap.rollbackTransaction();
                                System.out.println("Error in transaction 2 ....");
                                transFailureEx.printStackTrace();
                            }
                        }
                        if (transStatus == true) {
                            status.setExecutionCount(k);
                        }

                        batchList = null;
                    }
                }
            }
        }
    }

    Date returnDateFormat(Calendar c, Date dt) {
        Date cal_date = (Date) dt.clone();
        cal_date.setDate(c.getTime().getDate());
        cal_date.setMonth(c.getTime().getMonth());
        cal_date.setYear(c.getTime().getYear());
        return cal_date;
    }

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) curDate.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
    //added by jithin

    public HashMap calcTermDeposits(String actNum, String prodId, String prodType) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap whereMap = new HashMap();
        String behavesLike = "";
        String roundReq = "";
        String round ="";
        System.out.println("###### actNum" + actNum);
        if (actNum.indexOf("_") != -1) {
            actNum = actNum.substring(0, actNum.indexOf("_"));
        }
        whereMap.put("ACT_NUM", actNum);
        List behavesLikeList = sqlMap.executeQueryForList("getBehavesLikeForDepositNo", whereMap);
        if (behavesLikeList != null && behavesLikeList.size() > 0) {
            whereMap = (HashMap) behavesLikeList.get(0);
            behavesLike = CommonUtil.convertObjToStr(whereMap.get("BEHAVES_LIKE"));
            if (!behavesLike.equals("") && behavesLike != null) {
                System.out.println("###### behavesLike" + behavesLike + "###### actNum" + actNum);

                if (behavesLike.equals("RECURRING")) {            //Recurring Deposit
                    HashMap accountMap = new HashMap();
                    HashMap lastMap = new HashMap();
                    HashMap rdDataMap = new HashMap();
                    rdDataMap.put("DEPOSIT_NO", actNum);
                    accountMap.put("DEPOSIT_NO", actNum);
                    accountMap.put("BRANCH_ID", _branchCode);
                    List lst = sqlMap.executeQueryForList("getProductIdForDeposits", accountMap);
                    if (lst != null && lst.size() > 0) {
                        accountMap = (HashMap) lst.get(0);
                        Date currDate = (Date) curDate.clone();
                        //                        Date currDate = (Date) currDt.clone();
                        String insBeyondMaturityDat = "";
                        List recurringLst = sqlMap.executeQueryForList("getRecurringDepositDetails", accountMap);
                        if (recurringLst != null && recurringLst.size() > 0) {
                            HashMap recurringMap = new HashMap();
                            recurringMap = (HashMap) recurringLst.get(0);
                            insBeyondMaturityDat = CommonUtil.convertObjToStr(recurringMap.get("INST_BEYOND_MATURITY_DATE"));
                        }
                        
                        HashMap mapDelayAmtRound = new HashMap();
                        mapDelayAmtRound.put("PROD_ID", accountMap.get("PROD_ID"));
                        List lstDelayAmtRound = sqlMap.executeQueryForList("getSelDelayAmtRoundOff", mapDelayAmtRound);
                        mapDelayAmtRound = new HashMap();
                        if (lstDelayAmtRound.size() > 0 && lstDelayAmtRound != null) {
                            mapDelayAmtRound = (HashMap) lstDelayAmtRound.get(0);                        
                            roundReq = CommonUtil.convertObjToStr(mapDelayAmtRound.get("PENAL_ROUNDOFF_REQ"));
                            round = CommonUtil.convertObjToStr(mapDelayAmtRound.get("PENAL_ROUNDOFF"));
                        }            
                        long totalDelay = 0;
                        long actualDelay = 0;
                        double delayAmt = 0.0;
                        double tot_Inst_paid = 0.0;
                        double depAmt = CommonUtil.convertObjToDouble(accountMap.get("DEPOSIT_AMT")).doubleValue();
                        Date matDt = new Date();
                        matDt.setTime(currDate.getTime());
                        Date depDt = new Date();
                        depDt.setTime(currDate.getTime());
                        System.out.println("&&&&&&&&&&&& CurrentDate11111" + currDate);
                        lastMap.put("DEPOSIT_NO", actNum);
                        lst = sqlMap.executeQueryForList("getInterestDeptIntTable", lastMap);
                        if (lst != null && lst.size() > 0) {
                            lastMap = (HashMap) lst.get(0);
                            System.out.println("###### lastMap--->" + lastMap);
                            rdDataMap.put("DEPOSIT_AMT", lastMap.get("DEPOSIT_AMT"));
                            rdDataMap.put("MATURITY_DT", lastMap.get("MATURITY_DT"));
                            tot_Inst_paid = CommonUtil.convertObjToDouble(lastMap.get("TOTAL_INSTALL_PAID")).doubleValue();
                            HashMap prematureDateMap = new HashMap();
                            double monthPeriod = 0.0;
                            Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lastMap.get("MATURITY_DT")));
                            System.out.println("############# MATURITY_DT" + matDate);
                            System.out.println("############# CurrentDate" + currDate);
                            if (matDate.getDate() > 0) {
                                matDt.setDate(matDate.getDate());
                                matDt.setMonth(matDate.getMonth());
                                matDt.setYear(matDate.getYear());
                            }
                            Date depDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lastMap.get("DEPOSIT_DT")));
                            if (depDate.getDate() > 0) {
                                depDt.setDate(depDate.getDate());
                                depDt.setMonth(depDate.getMonth());
                                depDt.setYear(depDate.getYear());
                            }
                            System.out.println("############# MATURITY_DT" + matDate);
                            System.out.println("############# CurrentDate" + currDate);
                            if (DateUtil.dateDiff((Date) matDt, (Date) currDate) > 0) {
                                matDt = setProperDtFormat(matDt);
                                prematureDateMap.put("TO_DATE", matDt);
                                prematureDateMap.put("FROM_DATE", lastMap.get("DEPOSIT_DT"));
                                lst = sqlMap.executeQueryForList("periodRunMap", prematureDateMap);
                                if (lst != null && lst.size() > 0) {
                                    prematureDateMap = (HashMap) lst.get(0);
                                    System.out.println("############# prematureDateMap" + prematureDateMap);
                                    monthPeriod = CommonUtil.convertObjToDouble(prematureDateMap.get("NO_OF_MONTHS")).doubleValue();
                                    actualDelay = (long) monthPeriod - (long) tot_Inst_paid;
                                    System.out.println("############# actualDelay" + actualDelay + "############# monthPeriod" + monthPeriod + "############# tot_Inst_paid" + tot_Inst_paid);
                                }
                                lst = null;
                            } else {
                                int dep = depDt.getMonth();
                                int curr = currDate.getMonth() + 1;
                                int depYear = depDt.getYear() + 1900;
                                int currYear = currDate.getYear() + 1900;
                                if (depYear == currYear) {
                                    monthPeriod = curr - dep;
                                    actualDelay = (long) monthPeriod - (long) tot_Inst_paid;
                                } else {
                                    int diffYear = currYear - depYear;
                                    monthPeriod = (diffYear * 12 - dep) + curr;
                                    actualDelay = (long) monthPeriod - (long) tot_Inst_paid;
                                }
                                System.out.println("############# else actualDelay" + actualDelay + "############# monthPeriod" + monthPeriod + "############# tot_Inst_paid" + tot_Inst_paid);
                            }
                        }
                        lst = null;

                        if ((DateUtil.dateDiff((Date) matDt, (Date) currDate) > 0) && !insBeyondMaturityDat.equals("") && insBeyondMaturityDat.equals("N")) {
                            dataMap = new HashMap();
                            return dataMap;
                        }
                        System.out.println("#############%%%%% MATURITY_DT" + matDt);
                        System.out.println("############# %%%% CurrentDate" + currDate);
                        //delayed installment calculation...
                        if (DateUtil.dateDiff((Date) matDt, (Date) currDate) < 0 || insBeyondMaturityDat.equals("Y")) {
                            depAmt = CommonUtil.convertObjToDouble(accountMap.get("DEPOSIT_AMT")).doubleValue();
                            double chargeAmt = depAmt / 100;
                            HashMap delayMap = new HashMap();
                            delayMap.put("PROD_ID", accountMap.get("PROD_ID"));
                            delayMap.put("DEPOSIT_AMT", accountMap.get("DEPOSIT_AMT"));
                            lst = sqlMap.executeQueryForList("getSelectDelayedRate", delayMap);
                            if (lst != null && lst.size() > 0) {
                                delayMap = (HashMap) lst.get(0);
                                delayAmt = CommonUtil.convertObjToDouble(delayMap.get("PENAL_INT")).doubleValue();
                                delayAmt = delayAmt * chargeAmt;
                                System.out.println("######recurring delayAmt : " + delayAmt);
                            }
                            lst = null;
                            HashMap depRecMap = new HashMap();
//                            depRecMap.put("DEPOSIT_NO", actNum + "_1");
//                            List lstRec = sqlMap.executeQueryForList("getDepTransactionRecurring", depRecMap);
//                            if (lstRec != null && lstRec.size() > 0) {//how many unCompleted quarter is there upto (one by one) we have to calculate simple interest formula....
//                                for (int i = 0; i < lstRec.size(); i++) {//when the customer is paid from that dt to today dt....
//                                    depRecMap = (HashMap) lstRec.get(i);
//                                    Date transDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("TRANS_DT")));
//                                    Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("DUE_DATE")));
//                                    int transMonth = transDt.getMonth() + 1;
//                                    int dueMonth = dueDate.getMonth() + 1;
//                                    int dueYear = dueDate.getYear() + 1900;
//                                    int transYear = transDt.getYear() + 1900;
//                                    int delayedInstallment;// = transMonth - dueMonth;
//                                    if (dueYear == transYear) {
//                                        delayedInstallment = transMonth - dueMonth;
//                                    } else {
//                                        int diffYear = transYear - dueYear;
//                                        delayedInstallment = (diffYear * 12 - dueMonth) + transMonth;
//                                    }
//                                    if (delayedInstallment < 0) {
//                                        delayedInstallment = 0;
//                                    }
//                                    //totalDelay = totalDelay + delayedInstallment;
//                                }
//                            }                            
                            System.out.println("#### totalDelay1111111--->" + totalDelay);
                            
                            //lstRec = null;
                            HashMap paramMap = new HashMap();
                            depRecMap = new HashMap();
                            depRecMap.put("DEPOSIT_NO", actNum + "_1");
                            depRecMap.put("DEPOSIT_DT", lastMap.get("DEPOSIT_DT"));
                            depRecMap.put("CURR_DT", currDate);
                            depRecMap.put("SL_NO", new Double(tot_Inst_paid));
                            depRecMap.put("ACC_NUM",  actNum);
                            depRecMap.put(CommonConstants.BRANCH_ID, _branchCode);
                            List list;
                            List paramList = sqlMap.executeQueryForList("getSelectParameterForRdPendingCalc", depRecMap); 
                            if (paramList != null && paramList.size() > 0) {
                                paramMap = (HashMap) paramList.get(0);
                            }
                            ///if(paramMap.containsKey("INCLUDE_FULL_MONTH") && !paramMap.get("INCLUDE_FULL_MONTH").equals("") && paramMap.get("INCLUDE_FULL_MONTH").equals("Y")) {
                            //    lstRec =  sqlMap.executeQueryForList("getDepTransRecurrWithFullMonth", depRecMap);        
                           // }else{
                            //    lstRec = sqlMap.executeQueryForList("getDepTransRecurr", depRecMap);
                            //}
//                            if (lstRec != null && lstRec.size() > 0) {//how many unCompleted quarter is there upto (one by one) we have to calculate simple interest formula....
//                                for (int i = 0; i < lstRec.size(); i++) {//when the customer is paid from that dt to today dt....
//                                    depRecMap = (HashMap) lstRec.get(i);
//                                    System.out.println("depRecMap^$^$^$^"+depRecMap+"iiiiiiiiiiiiii"+i);
//                                    Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("DUE_DATE")));
//                                    int transMonth = currDate.getMonth() + 1;
//                                    int dueMonth = dueDate.getMonth() + 1;
//                                    int dueYear = dueDate.getYear() + 1900;
//                                    int transYear = currDate.getYear() + 1900;
//                                    int delayedInstallment;// = transMonth - dueMonth;
//                                    System.out.println("dueYear^$^$^$^"+dueYear);
//                                    System.out.println("transYear^$^$^$^"+transYear);
//                                    if (dueYear == transYear) {
//                                        delayedInstallment = transMonth - dueMonth;
//                                    } else {
//                                        int diffYear = transYear - dueYear;
//                                        delayedInstallment = (diffYear * 12 - dueMonth) + transMonth;
//                                        System.out.println("delayedInstallment^$^$^$^"+delayedInstallment);
//                                    }
//                                    if (delayedInstallment < 0) {
//                                        delayedInstallment = 0;
//                                    }
//                                    System.out.println("delayedInstallment2222222222^$^$^$^"+delayedInstallment);
//                                   // totalDelay = totalDelay + delayedInstallment;
//                                }
//                          
                            List lstRec = null;
                            String instalPendng = "";
                            if(paramMap.containsKey("INCLUDE_FULL_MONTH") && !paramMap.get("INCLUDE_FULL_MONTH").equals("") && paramMap.get("INCLUDE_FULL_MONTH").equals("Y")) {
                                list = sqlMap.executeQueryForList("getSelectInstalmentPendingForTransDetailsUiWithMonthEnd", depRecMap);  
                            }else{
                                list = sqlMap.executeQueryForList("getSelectInstalmentPendingForTransDetailsUi", depRecMap);
                            }
                            if (list != null && list.size() > 0) {
                                paramMap = (HashMap) list.get(0);
                                instalPendng = CommonUtil.convertObjToStr(paramMap.get("PENDING"));
                                totalDelay = (long) CommonUtil.convertObjToInt(instalPendng);
                                //  actualDelay1=actualDelay;
                                System.out.println("actualDelay iv in cash>>>" + totalDelay);
                            }
                            lstRec = null;
                            System.out.println("#### totalDelay--->" + totalDelay);
                            System.out.println("#### delayAmt--->" + delayAmt);
                            //delayAmt = delayAmt * totalDelay;
                            //delayAmt = (double) delayAmt * 100), 100) / 100;
                            //Added by sreekrishnan
                            double cummInst = 0.0;
                            double penal = 0.0;
                            if (totalDelay>0) {
                                cummInst = totalDelay * (totalDelay + 1) / 2;
                                penal = cummInst * delayAmt ;
                            } 
                            System.out.println("#### penal--->111111" + penal);
                            if (roundReq.equals("Y")) {
                                if (round.equals("NEAREST_VALUE")) {
                                    penal = (double) getNearest((long) (penal * 100), 100) / 100;
                                 } else if (round.equals("LOWER_VALUE")) {
                                    penal = (double) roundOffLower((long) (penal * 100), 100) / 100;
                                } else if (round.equals("HIGHER_VALUE")) {
                                    penal = (double) higher((long) (penal * 100), 100) / 100;
                                } else {
                                    penal = new Double(penal);
                                }
                            }
                            System.out.println("#### cummInst--->" + cummInst);
                            System.out.println("#### penal--->" + penal*100);
                            double oldPenalAmt = CommonUtil.convertObjToDouble(accountMap.get("DELAYED_AMOUNT")).doubleValue();
                            long oldPenalMonth = CommonUtil.convertObjToLong(accountMap.get("DELAYED_MONTH"));
                            double balanceAmt = 0.0;
                            //balanceAmt = penal;
                            if (oldPenalAmt > 0) {
                                balanceAmt = penal - oldPenalAmt;
                                totalDelay = totalDelay - oldPenalMonth;
                            } else {
                                balanceAmt = penal;
                            }
                            System.out.println("actualDelay^#%^#^^#"+actualDelay);
                            double principal = 0.0;
                            if(actualDelay>0){
                                principal = actualDelay * CommonUtil.convertObjToDouble(rdDataMap.get("DEPOSIT_AMT")).doubleValue();
                            }else{
                                principal = CommonUtil.convertObjToDouble(rdDataMap.get("DEPOSIT_AMT")).doubleValue();
                            }
                            double totalDemand = principal + balanceAmt;
                            rdDataMap.put("DELAYED_MONTH", accountMap.get("DELAYED_MONTH"));
                            rdDataMap.put("DEPOSIT_PENAL_AMT", String.valueOf(balanceAmt));
                            rdDataMap.put("PRINCIPAL", String.valueOf(principal));
                            rdDataMap.put("TOTAL_DEMAND", String.valueOf(totalDemand));
                            rdDataMap.put("DEPOSIT_PENAL_MONTH", String.valueOf(totalDelay));
                            System.out.println("#### balanceAmt--->" + balanceAmt + "##### totalDelay" + totalDelay);
                        }
                    }
                    System.out.println("#### rdDataMap--->" + rdDataMap);
                    dataMap.put("PRINCIPAL", rdDataMap.get("PRINCIPAL"));
                    dataMap.put("DEPOSIT_PENAL_AMT", rdDataMap.get("DEPOSIT_PENAL_AMT"));
                    dataMap.put("TOTAL_DEMAND", rdDataMap.get("TOTAL_DEMAND"));
                    dataMap.put("DEPOSIT_PENAL_MONTH", rdDataMap.get("DEPOSIT_PENAL_MONTH"));
                    dataMap.put("INTEREST", new Double(0));
                    dataMap.put("CHARGES", new Double(0));
                    if (CommonUtil.convertObjToDouble(rdDataMap.get("TOTAL_DEMAND")).doubleValue() <= 0.0) {
                        dataMap = null;
                    }
                    System.out.println("######## dataMap" + dataMap);
                }
            }
        }
        return dataMap;
    }

        public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
        if ((mod <= (roundingFactor / 2)) || (mod <= (roundingFactorOdd / 2))) {
            return lower(number, roundingFactor);
        } else {
            return higher(number, roundingFactor);
        }
    }

    public long lower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    public long higher(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        if (mod == 0) {
            return number;
        }
        return (number - mod) + roundingFactor;
    }

    public long roundOffLower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }
    
    //Added By Suresh
    public HashMap calcLoanPayments(String actNum, String prodId, String prodType) throws Exception {
        HashMap asAndWhenMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put("ACT_NUM", actNum);
        asAndWhenMap = interestCalculationTLAD(actNum, prodId);
        System.out.println("@#@ asAndWhenMap is >>>>" + asAndWhenMap);
        System.out.println("transDetail" + actNum + _branchCode);
        HashMap insertPenal = new HashMap();
        List chargeList = null;
        Date inst_dt = null;
        HashMap loanInstall = new HashMap();
        loanInstall.put("ACT_NUM", actNum);
        loanInstall.put("BRANCH_CODE", _branchCode);
        double instalAmt = 0.0;
        double paidAmount = 0.0;
        if (prodType != null && prodType.equals("TL")) {      //Only TL
            HashMap allInstallmentMap = null;
            List paidAmt = sqlMap.executeQueryForList("getPaidPrinciple", loanInstall);
            allInstallmentMap = (HashMap) paidAmt.get(0);
            System.out.println("!!!!asAndWhenMap:" + asAndWhenMap);
            double totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
            paidAmount = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
            if (asAndWhenMap == null || (asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && (asAndWhenMap.get("AS_CUSTOMER_COMES") != null) && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("N"))) {
                paidAmt = sqlMap.executeQueryForList("getIntDetails", loanInstall);
                if (paidAmt != null && paidAmt.size() > 0) {
                    allInstallmentMap = (HashMap) paidAmt.get(0);
                }
                double totExcessAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
                totPrinciple += totExcessAmt;
            }
            List lst = sqlMap.executeQueryForList("getAllLoanInstallment", loanInstall);
            for (int i = 0; i < lst.size(); i++) {
                allInstallmentMap = (HashMap) lst.get(i);
                instalAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                if (instalAmt <= totPrinciple) {
                    totPrinciple -= instalAmt;
                    inst_dt = new Date();
                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                } else {
                    inst_dt = new Date();
                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                    totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue() - totPrinciple;
                    break;
                }
            }
            Date addDt = (Date) curDate.clone();
            Date instDt = DateUtil.addDays(inst_dt, 1);
            addDt.setDate(instDt.getDate());
            addDt.setMonth(instDt.getMonth());
            addDt.setYear(instDt.getYear());
            loanInstall.put("FROM_DATE", addDt);//DateUtil.addDays(inst_dt,1));
            loanInstall.put("TO_DATE", curDate);
            System.out.println("!! getTotalamount#####" + loanInstall);
            List lst1 = null;
            if (inst_dt != null && (totPrinciple > 0)) {
                lst1 = sqlMap.executeQueryForList("getTotalAmountOverDue", loanInstall);
                System.out.println("listsize####" + lst1);
            }
            double principle = 0;
            if (lst1 != null && lst1.size() > 0) {
                HashMap map = (HashMap) lst1.get(0);
                principle = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue();
            }
            totPrinciple += principle;
            insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(totPrinciple));
            insertPenal.put("INSTALL_DT", inst_dt);
            if (asAndWhenMap.containsKey("MORATORIUM_INT_FOR_EMI")) {
                insertPenal.put("MORATORIUM_INT_FOR_EMI", asAndWhenMap.get("MORATORIUM_INT_FOR_EMI"));
            }
            if (asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && (asAndWhenMap.get("AS_CUSTOMER_COMES") != null) && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")) {
                double interest = CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
                double penal = CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                List facilitylst = sqlMap.executeQueryForList("LoneFacilityDetailAD", loanInstall);
                if (facilitylst != null && facilitylst.size() > 0) {
                    HashMap hash = (HashMap) facilitylst.get(0);
                    hash.put("ACT_NUM", loanInstall.get("ACT_NUM"));
                    if (asAndWhenMap.containsKey("PREMATURE")) {
                        insertPenal.put("PREMATURE", asAndWhenMap.get("PREMATURE"));
                    }
                    if (asAndWhenMap.containsKey("PREMATURE") && asAndWhenMap.containsKey("PREMATURE_INT_CALC_AMT")
                            && CommonUtil.convertObjToStr(asAndWhenMap.get("PREMATURE_INT_CALC_AMT")).equals("LOANSANCTIONAMT")) {
                        hash.put("FROM_DT", hash.get("ACCT_OPEN_DT"));
                    } else {
                        hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                        hash.put("FROM_DT", DateUtil.addDays(((Date) hash.get("FROM_DT")), 2));
                    }
                    hash.put("TO_DATE", curDate);
                    if (!(asAndWhenMap != null && asAndWhenMap.containsKey("INSTALL_TYPE") && asAndWhenMap.get("INSTALL_TYPE") != null && asAndWhenMap.get("INSTALL_TYPE").equals("EMI"))) {
                        facilitylst = sqlMap.executeQueryForList("getPaidPrinciple", hash);
                    } else {
                        facilitylst = null;
                        if (asAndWhenMap.containsKey("PRINCIPAL_DUE") && asAndWhenMap.get("PRINCIPAL_DUE") != null) {
                            insertPenal.put("CURR_MONTH_PRINCEPLE", asAndWhenMap.get("PRINCIPAL_DUE"));
                        }
                    }
                    if (facilitylst != null && facilitylst.size() > 0) {
                        hash = (HashMap) facilitylst.get(0);
                        interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                        penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();

                        insertPenal.put("PAID_INTEREST", hash.get("INTEREST"));
                    }
                }
                System.out.println("####interest:" + interest);
                if (interest > 0) {
                    insertPenal.put("CURR_MONTH_INT", new Double(interest));
                } else {
                    insertPenal.put("CURR_MONTH_INT", new Double(0));
                }
                if (penal > 0) {
                    insertPenal.put("PENAL_INT", new Double(penal));
                } else {
                    insertPenal.put("PENAL_INT", new Double(0));
                }
                insertPenal.put("INTEREST", asAndWhenMap.get("INTEREST"));
                insertPenal.put("LOAN_CLOSING_PENAL_INT", asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));

                insertPenal.put("LAST_INT_CALC_DT", asAndWhenMap.get("LAST_INT_CALC_DT"));
                insertPenal.put("ROI", asAndWhenMap.get("ROI"));
                chargeList = sqlMap.executeQueryForList("getChargeDetails", loanInstall);
            } else {
                List getIntDetails = sqlMap.executeQueryForList("getIntDetails", loanInstall);
                HashMap hash = null;
                if (getIntDetails != null) {
                    for (int i = 0; i < getIntDetails.size(); i++) {
                        hash = (HashMap) getIntDetails.get(i);
                        String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
                        double pBal = CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue();
                        double iBal = CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
                        double pibal = CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
                        double excess = CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT")).doubleValue();
                        pBal -= excess;
                        if (pBal < totPrinciple) {
                            insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(pBal));
                        }
                        if (trn_mode.equals("C*")) {
                            insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                            insertPenal.put("PRINCIPLE_BAL", new Double(pBal));
                            insertPenal.put("EBAL", hash.get("EBAL"));
                            break;
                        } else {
                            if (!trn_mode.equals("DP")) {
                                insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                            }
                            insertPenal.put("EBAL", hash.get("EBAL"));
                            insertPenal.put("PRINCIPLE_BAL", new Double(pBal));
                        }
                        System.out.println("int principel detailsINSIDE LOAN##" + insertPenal);
                    }
                }
                getIntDetails = sqlMap.executeQueryForList("getPenalIntDetails", loanInstall);
                hash = (HashMap) getIntDetails.get(0);
                insertPenal.put("PENAL_INT", hash.get("PIBAL"));
            }
        }

        if (prodType != null && prodType.equals("AD")) // Only  AD
        {
            if (asAndWhenMap != null && asAndWhenMap.size() > 0) {
                if (asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")) {
                    List facilitylst = sqlMap.executeQueryForList("LoneFacilityDetailAD", loanInstall);
                    double interest = CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
                    double penal = CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                    if (facilitylst != null && facilitylst.size() > 0) {
                        HashMap hash = (HashMap) facilitylst.get(0);
                        hash.put("ACT_NUM", loanInstall.get("ACT_NUM"));
                        hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                        hash.put("FROM_DT", DateUtil.addDays(((Date) hash.get("FROM_DT")), 2));
                        hash.put("TO_DATE", DateUtil.addDaysProperFormat(curDate, -1));
                        facilitylst = sqlMap.executeQueryForList("getPaidPrincipleAD", hash);
                        if (facilitylst != null && facilitylst.size() > 0) {
                            hash = (HashMap) facilitylst.get(0);
                            interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                            penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                        }
                    }
                    if (interest > 0) {
                        insertPenal.put("CURR_MONTH_INT", new Double(interest));
                    } else {
                        insertPenal.put("CURR_MONTH_INT", new Double(0));
                    }
                    if (penal > 0) {
                        insertPenal.put("PENAL_INT", new Double(penal));
                    } else {
                        insertPenal.put("PENAL_INT", new Double(0));
                    }
                    insertPenal.put("INTEREST", asAndWhenMap.get("INTEREST"));
                    insertPenal.put("LOAN_CLOSING_PENAL_INT", asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
                    chargeList = sqlMap.executeQueryForList("getChargeDetails", loanInstall);
                } else {
                    if (prodType != null && prodType.equals("AD")) {
                        List getIntDetails = sqlMap.executeQueryForList("getIntDetailsAD", loanInstall);
                        HashMap hash = null;

                        for (int i = 0; i < getIntDetails.size(); i++) {
                            hash = (HashMap) getIntDetails.get(i);
                            String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
                            double iBal = CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
                            double pibal = CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
                            double excess = CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT")).doubleValue();
                            if (trn_mode.equals("C*")) {
                                insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                                insertPenal.put("PRINCIPLE_BAL", new Double(CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue() - excess));
                                insertPenal.put("EBAL", hash.get("EBAL"));
                                break;
                            } else {
                                insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                                insertPenal.put("PRINCIPLE_BAL", new Double(CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue() - excess));
                                insertPenal.put("EBAL", hash.get("EBAL"));
                            }
                            System.out.println("int principel detailsINSIDE OD" + insertPenal);
                        }
                        getIntDetails = sqlMap.executeQueryForList("getPenalIntDetailsAD", loanInstall);
                        if (getIntDetails.size() > 0) {
                            hash = (HashMap) getIntDetails.get(0);
                            insertPenal.put("PENAL_INT", hash.get("PIBAL"));
                        }
                        insertPenal.remove("PRINCIPLE_BAL");

                    }
                }
            }
        }
        //Added By Suresh  (Current Dt > To Date AND PBAL >0 in ADV_TRANS_DETAILS, Add Principle_Balance)
        if (prodType != null && prodType.equals("AD")) {
            double pBalance = 0.0;
            Date expDt = null;
            List expDtList = sqlMap.executeQueryForList("getLoanExpDate", loanInstall);
            if (expDtList != null && expDtList.size() > 0) {
                whereMap = new HashMap();
                whereMap = (HashMap) expDtList.get(0);
                pBalance = CommonUtil.convertObjToDouble(whereMap.get("PBAL")).doubleValue();
                expDt = (Date) whereMap.get("TO_DT");
                long diffDayPending = DateUtil.dateDiff(expDt, curDate);
                System.out.println("############# Insert PBalance" + pBalance + "######diffDayPending :" + diffDayPending);
                if (diffDayPending > 0 && pBalance > 0) {
                    insertPenal.put("PRINCIPLE_BAL", new Double(pBalance));
                }
            }
        }
        System.out.println("####### insertPenal : " + insertPenal);

        //Charges
        double chargeAmt = 0.0;
        whereMap = new HashMap();
        whereMap.put("ACT_NUM", actNum);
        chargeAmt = getChargeAmount(whereMap, prodType);
        double totalDemand = 0.0;
        double principalAmount = 0.0;
        if (insertPenal.containsKey("CURR_MONTH_PRINCEPLE")) {
            principalAmount = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_PRINCEPLE")).doubleValue();     // Principal Amount
            totalDemand = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue()
                    + CommonUtil.convertObjToDouble(insertPenal.get("PENAL_INT")).doubleValue() + chargeAmt;
        } else {
            principalAmount = CommonUtil.convertObjToDouble(insertPenal.get("PRINCIPLE_BAL")).doubleValue();     // Principal Amount
            totalDemand = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue()
                    + CommonUtil.convertObjToDouble(insertPenal.get("PENAL_INT")).doubleValue() + chargeAmt;
        }
        if (inst_dt != null && prodType.equals("TL")) {
            if (DateUtil.dateDiff(curDate, inst_dt) <= 0) {
                asAndWhenMap.put("CURR_MONTH_PRINCEPLE", String.valueOf(principalAmount));
            } else {
                asAndWhenMap.put("CURR_MONTH_PRINCEPLE", "0");
                principalAmount = 0.0;
            }
        }
        if (prodType.equals("AD")) {
            if (principalAmount > 0) {
                asAndWhenMap.put("CURR_MONTH_PRINCEPLE", String.valueOf(principalAmount));
            } else {
                asAndWhenMap.put("CURR_MONTH_PRINCEPLE", "0");
            }
        }
        totalDemand += principalAmount;
        asAndWhenMap.put("CURR_MONTH_INT", insertPenal.get("CURR_MONTH_INT"));
        asAndWhenMap.put("PENAL_INT", insertPenal.get("PENAL_INT"));
        asAndWhenMap.put("TOTAL_DEMAND", new Double(totalDemand));
        if (totalDemand <= 0) {
            asAndWhenMap = null;
        }
        System.out.println("####### Single Row asAndWhenMap : " + asAndWhenMap);
        return asAndWhenMap;
    }

    private HashMap interestCalculationTLAD(String accountNo, String prodID) {
        HashMap map = new HashMap();
        HashMap insertPenal = new HashMap();
        HashMap hash = null;
        try {
            map.put("ACT_NUM", accountNo);
            map.put("PROD_ID", prodID);
            List lst = sqlMap.executeQueryForList("IntCalculationDetail", map);
            if (lst != null && lst.size() > 0) {
                hash = (HashMap) lst.get(0);
                insertPenal.put("AS_CUSTOMER_COMES", hash.get("AS_CUSTOMER_COMES"));
                if (hash.get("AS_CUSTOMER_COMES") != null && hash.get("AS_CUSTOMER_COMES").equals("N")) {
                    hash = new HashMap();
                    return hash;
                }
                map.put("BRANCH_ID", branchID);
                map.put(CommonConstants.BRANCH_ID, branchID);
                map.putAll(hash);
                map.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
                map.put("CURR_DATE", curDate);
                System.out.println("map before intereest###" + map);
                TaskHeader header = new TaskHeader();
                header.setBranchID(branchID);
                InterestCalculationTask interestcalTask = new InterestCalculationTask(header);
                hash = interestcalTask.interestCalcTermLoanAD(map);
                if (hash == null) {
                    hash = new HashMap();
                } else if (hash != null && hash.size() > 0) {
                    double interest = CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                    double penal = CommonUtil.convertObjToDouble(hash.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                    hash.put("ACT_NUM", accountNo);
                    hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                    hash.put("FROM_DT", DateUtil.addDays(((Date) hash.get("FROM_DT")), 2));
                    hash.put("TO_DATE", map.get("CURR_DATE"));
                    List facilitylst = sqlMap.executeQueryForList("getPaidPrinciple", hash);
                    if (facilitylst != null && facilitylst.size() > 0) {
                        hash = (HashMap) facilitylst.get(0);
                        interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                        penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                    }
                    if (interest > 0) {
                        insertPenal.put("CURR_MONTH_INT", new Double(interest));
                    } else {
                        insertPenal.put("CURR_MONTH_INT", new Double(0));
                    }
                    if (penal > 0) {
                        insertPenal.put("PENAL_INT", new Double(penal));
                    } else {
                        insertPenal.put("PENAL_INT", new Double(0));
                    }
                    List chargeList = sqlMap.executeQueryForList("getChargeDetails", map);
                    if (chargeList != null && chargeList.size() > 0) {
                        for (int i = 0; i < chargeList.size(); i++) {
                            HashMap chargeMap = (HashMap) chargeList.get(i);
                            double chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                            if (chargeMap.get("CHARGE_TYPE").equals("POSTAGE CHARGES") && chargeAmt > 0) {
                                insertPenal.put("POSTAGE CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("MISCELLANEOUS CHARGES") && chargeAmt > 0) {
                                insertPenal.put("MISCELLANEOUS CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("LEGAL CHARGES") && chargeAmt > 0) {
                                insertPenal.put("LEGAL CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("INSURANCE CHARGES") && chargeAmt > 0) {
                                insertPenal.put("INSURANCE CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("EXECUTION DECREE CHARGES") && chargeAmt > 0) {
                                insertPenal.put("EXECUTION DECREE CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("ARBITRARY CHARGES") && chargeAmt > 0) {
                                insertPenal.put("ARBITRARY CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            chargeMap = null;
                        }
                    }
                    chargeList = null;
                }
                interestcalTask = null;
                System.out.println("hashinterestoutput###" + insertPenal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        map = null;
        hash = null;

        return insertPenal;
    }

    private double getChargeAmount(HashMap whereMap, String prodType) {   //Charges
        double chargeAmount = 0.0;
        try {
            List chargeList = null;
            chargeList = sqlMap.executeQueryForList("getChargeDetails", whereMap);
            if (chargeList != null && chargeList.size() > 0) {
                for (int i = 0; i < chargeList.size(); i++) {
                    whereMap = (HashMap) chargeList.get(i);
                    chargeAmount += CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
                }
            }
            chargeList = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chargeAmount;
    }

    void chkPrevDayHoliday(Date prevDt) {
        try {
            HashMap dataMap = new HashMap();
            dataMap.put("NEXT_DATE",DateUtil.addDays(prevDt,0));
            dataMap.put("BRANCH_CODE", getHeader().getBranchID());
            List Holiday = sqlMap.executeQueryForList("checkHolidayDateOD", dataMap);
            List weeklyOf = sqlMap.executeQueryForList("checkWeeklyOffOD", dataMap);
            boolean isHoliday = Holiday.size() > 0 ? true : false;
            boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
            if (isHoliday || isWeekOff) {
                //                List siLst = sqlMap.executeQueryForList("checkPendingSI", dataMap);
                //                if(siLst != null && siLst.size() > 0)
                chkPrevDayHol = true;
                //                else
                //                    chkPrevDayHol = false;
            } else {
                chkPrevDayHol = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void holiydaychecking(Date lstintCr, String execDt) {
        try {
            HashMap MonthEnd = new HashMap(); //traansferto holidaychecking method;
            boolean checkHoliday = true;
//            System.out.println("lstDate  firstholiday"+lstintCr);
            Date lstDate = (Date) curDate.clone();
//           System.out.println("lstDate  firstholiday"+lstDate);
            lstDate.setMonth(lstintCr.getMonth());
            lstDate.setYear(lstintCr.getYear());
            lstDate.setDate(lstintCr.getDate());
//           System.out.println("lstDate  secodeholiday"+lstDate);
            MonthEnd.put("NEXT_DATE", DateUtil.addDays(lstDate,0));//lstintCr abi
            MonthEnd.put("BRANCH_CODE", getHeader().getBranchID());
//            System.out.println("holidaychecking#####"+MonthEnd);
            while (checkHoliday) {
                boolean tholiday = false;
//                System.out.println("enterytothecheckholiday"+checkHoliday);
                List Holiday = sqlMap.executeQueryForList("checkHolidayDateOD", MonthEnd);
                List weeklyOf = sqlMap.executeQueryForList("checkWeeklyOffOD", MonthEnd);
                boolean isHoliday = Holiday.size() > 0 ? true : false;
                boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
                if (isHoliday || isWeekOff) {
                    MonthEnd = dateMinusPlus(MonthEnd, execDt);
//                    System.out.println("MonthEnd^^^^^^"+MonthEnd);
                    checkHoliday = true;
                } else {
                    checkHoliday = false;
                    checkThisCDate = (Date) MonthEnd.get("NEXT_DATE");
                    //                    doNoHoliday(CommonUtil.convertObjToStr(MonthEnd.get("NEXT_DATE")));;

                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HashMap dateMinusPlus(HashMap dateMap, String execDt) {
        String day = CommonUtil.convertObjToStr(dateMap.get("NEXT_DATE"));

        Date lastDay = (Date) dateMap.get("NEXT_DATE");
        int days = lastDay.getDate();
        if (execDt.equalsIgnoreCase("PREVIOUSDAY")) {

            lastDay = DateUtil.addDaysProperFormat(lastDay, -1);
        } else if (execDt.equalsIgnoreCase("NEXTDAY")) {
            lastDay = DateUtil.addDaysProperFormat(lastDay, 1);
            //            days++;
        }
        //            lastDay.setDate(days);
        dateMap.put("NEXT_DATE", lastDay);
        dateMap.put("BRANCH_CODE", getHeader().getBranchID());
        return dateMap;
    }

    private boolean doNoHoliday(String nonHoliday) {
        Date IsDt = DateUtil.getDateMMDDYYYY(nonHoliday);
        Date checkThisCDate = (Date) curDate.clone();
        checkThisCDate.setDate(IsDt.getDate());
        checkThisCDate.setMonth(IsDt.getMonth());
        checkThisCDate.setYear(IsDt.getYear());
//        checkThisCDate=DateUtil.getDateMMDDYYYY(nonHoliday);
//        System.out.println("nonHoliday"+nonHoliday);
//        System.out.println("checkThisCDate"+checkThisCDate);
        return false;
    }

    public Date nextCaldate(Date siDt, Date nxtRunDt) {
//        System.out.println("nxtRunDt&&&&&&&&&&&&&&"+nxtRunDt);
//        System.out.println("siDt&&&&&&&&&&&&&&"+siDt);
        //        nxtRunDt = DateUtil.addDaysProperFormat(nxtRunDt, CommonUtil.convertObjToInt(nxtMap.get("FREQ")));
//        System.out.println("After nxtRunDt&&&&&&&&&&&&&&"+nxtRunDt);
        Calendar dpnxtCalender = new GregorianCalendar(nxtRunDt.getYear() + 1900, nxtRunDt.getMonth(), nxtRunDt.getDate());
//        System.out.println("dpnxtCalender&&&&&&&&&&&&&&"+dpnxtCalender.getTime());
        int lstDayofmonth = dpnxtCalender.getActualMaximum(dpnxtCalender.DAY_OF_MONTH);
//        System.out.println("lstDayofmonth"+lstDayofmonth);
        //        Calendar dpDtCalender=new GregorianCalendar(siDt.getYear()+1900,siDt.getMonth()+1,siDt.getDate());
        int siDay = siDt.getDate();
//        System.out.println("siDay$$$$$$$$$$$"+siDay);
        if (lstDayofmonth > siDay) {
            nxtRunDt.setDate(siDay);
            return nxtRunDt;
        } else {
            nxtRunDt.setDate(lstDayofmonth);
            return nxtRunDt;
        }
    }

    public static void main(String arg[]) {
        try {
            TaskHeader header = new TaskHeader();
            header.setTaskClass("StandingInstructionTask");
            StandingInstructionTask tsk = new StandingInstructionTask(header);
            TaskStatus status = tsk.executeTask();
            System.out.println("StandingInstructionTask Status: " + status.getStatus());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
