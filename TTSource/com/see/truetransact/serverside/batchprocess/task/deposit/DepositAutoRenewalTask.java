/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DepositAutoRenewalTask.java
 *
 * Created on February 28, 2005, 12:23 PM
 * Author : 152691, Sunil
 *
 * Desc : This code renews the deposits whose product is configured as AUTO RENEW
 *        Runs as a batch process. It closes the matured deposit and creates a new
 *        deposit using the matured amt. The period and all other params like Nominee,
 *        POA, Auth Sig remains the same as the original deposit.
 *
 *        ROI and Maturity Amt are recalculated based on Deposit Amt
 *
 *        Invokes various DOA calls. Tranaction handling not done in this code.
 *        It can be implemented as required
 */
//Code TODO as on 16 Mar 2005
//  a. Get ROI on date of renewal based on SYSDATE and new DEPOSIT_AMT
//  b. Calculate maturity Amt based on above ROI and DEPOSIT_AMT
//  c. Line Nos 106, 111, 113
package com.see.truetransact.serverside.batchprocess.task.deposit;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.InterestCalc;
import com.see.truetransact.serverutil.ServerUtil;
import java.util.GregorianCalendar;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import com.see.truetransact.transferobject.deposit.AccInfoTO;
import com.see.truetransact.serverside.deposit.TermDepositDAO;
import com.see.truetransact.transferobject.deposit.DepSubNoAccInfoTO;
import com.see.truetransact.transferobject.deposit.TransferInTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.deposit.JointAccntTO;
import com.see.truetransact.transferobject.common.nominee.NomineeTO;
import com.see.truetransact.transferobject.common.authorizedsignatory.AuthorizedSignatoryInstructionTO;
import com.see.truetransact.transferobject.common.authorizedsignatory.AuthorizedSignatoryTO;
import com.see.truetransact.transferobject.common.powerofattorney.PowerAttorneyTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.transferobject.batchprocess.interest.InterestBatchTO;
import com.see.truetransact.serverside.tds.tdscalc.TdsCalc;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import java.text.DecimalFormat;
import java.util.Date;

/**
 *
 * @author 152691 This class renews deposits on maturity date if auto renewal
 * flag is true
 */
public class DepositAutoRenewalTask extends Task {

    private static SqlMap sqlMap = null;
    private String branch = null;
    private String USER_ID = null;
    private String CUST_ID = null;
    private String prodId = null;
    private String category = null;
    LinkedHashMap depSubNoTOMap;
    private Date currDt = null;
    private String renewInt = null;
    private InterestBatchTO interestBatchTO;
    private TdsCalc tdsCalc;
    private String dayEndType;
    private List branchList;
    private boolean isError = false;
    HashMap finalMap = new HashMap();
    private String taskLable;
    private String generateSingleTransId = "";
    private String productBehavesLike = "";
    public final String YES_STR = "Y";
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private LinkedHashMap transactionDetailsTO;
    public final String NO_STR = "N";
    private String productInterestType = "";
    private final String FLD_FOR_DB_YES_NO = "DBYesOrNo";
    private int yearTobeAdded = 1900;
    AccInfoTO objAccInfoTO;
    LinkedHashMap jntAcctTOMap;
    public LinkedHashMap jntAcctAll;
    HashMap jntAcctSingleRec;
    private final String YES_FULL_STR = "Yes";
    private final String NO_FULL_STR = "No";

    /**
     * Creates a new instance of DepositAutoRenewalTask
     */
    public DepositAutoRenewalTask(TaskHeader header) throws Exception {
        setHeader(header);
        branch = header.getBranchID();
        USER_ID = CommonUtil.convertObjToStr(header.getUserID());
        initializeTaskObj(header.getTaskParam());
    }

    private void initializeTaskObj(HashMap dataMap) throws Exception {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        currDt = ServerUtil.getCurrentDate(_branchCode);

        if (dataMap != null && dataMap.containsKey("DAY_END_TYPE")) {
            dayEndType = CommonUtil.convertObjToStr(dataMap.get("DAY_END_TYPE"));
        }
        if (CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {

            HashMap tempMap = new HashMap();
            if (dataMap.containsKey("BRANCH_LST")) {
                branchList = (List) dataMap.get("BRANCH_LST");
                System.out.println("branchList*****" + branchList);
            } else {
                tempMap.put("NEXT_DATE", currDt.clone());
                branchList = (List) sqlMap.executeQueryForList("getAllBranchesFromDayEnd", tempMap);
            }
            tempMap = null;
        } else {
            HashMap tempMap = new HashMap();
            tempMap.put(CommonConstants.BRANCH_ID, getHeader().getBranchID());
            branchList = new ArrayList();
            branchList.add(tempMap);
            tempMap = null;
        }
        if (dataMap != null && dataMap.containsKey("DEP_AUTO_REN_TASK_LABLE")) {
            taskLable = CommonUtil.convertObjToStr(dataMap.get("DEP_AUTO_REN_TASK_LABLE"));
        }
    }

    public LinkedHashMap setDepSubNoAccInfoData(List depSubNoAll) throws Exception {
        System.out.println("###### setDepSubNoAccInfoData depSubNoAll" + depSubNoAll);
        HashMap depSubNoAccInfoSingleRec;
        LinkedHashMap depSubNoTOMap = new LinkedHashMap();
        DepSubNoAccInfoTO objDepSubNoAccInfoTO;
        int depSubNoSize = depSubNoAll.size();
        for (int i = 0; i < depSubNoSize; i++) {
            depSubNoAccInfoSingleRec = (HashMap) depSubNoAll.get(i);
            objDepSubNoAccInfoTO = new DepSubNoAccInfoTO();
            HashMap txMap = new HashMap();
            ArrayList transferList = new ArrayList();
            TransferTrans transferTrans = new TransferTrans();
            TxTransferTO transferTo = new TxTransferTO();
            HashMap depositHeadMap = new HashMap();
            depositHeadMap.put("PROD_ID", prodId);
            depositHeadMap = (HashMap) sqlMap.executeQueryForObject("getDepositClosingHeads", depositHeadMap);
            System.out.println("depositHeadMap: " + depositHeadMap);
            Date depositDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get("MATURITY_DT")));
            double depositAmt = CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get("DEPOSIT_AMT")).doubleValue();
            double totIntAmt = CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get("TOT_INT_AMT")).doubleValue();
            double totIntCredit = CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get("TOTAL_INT_CREDIT")).doubleValue();
            double totIntDrawn = CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get("TOTAL_INT_DRAWN")).doubleValue();
            double balanceInt = totIntAmt - totIntCredit;
            double payableAmt = totIntCredit - totIntDrawn;
            System.out.println("renewInt" + renewInt + "totIntAmt :" + totIntAmt + "totIntCredit :" + totIntCredit + "totIntDrawn :" + totIntDrawn + "balanceInt :" + balanceInt + "payableAmt :" + payableAmt);
            if (renewInt != null && renewInt.equals("Y") && balanceInt > 0) {
                txMap = new HashMap();
                transferList = new ArrayList();
                transferTrans.setInitiatedBranch(branch);
                transferTrans.setLinkBatchId(depSubNoAccInfoSingleRec.get("DEPOSIT_NO") + "_1");
                txMap.put(TransferTrans.DR_AC_HD, (String) depositHeadMap.get("INT_PROV_ACHD"));//debiting to int paid a/c head
                txMap.put(TransferTrans.PARTICULARS, " " + depSubNoAccInfoSingleRec.get("DEPOSIT_NO") + "_1");
                txMap.put(TransferTrans.DR_BRANCH, branch);
                txMap.put(TransferTrans.CURRENCY, "INR");
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);

                txMap.put(TransferTrans.CR_AC_HD, (String) depositHeadMap.get("INT_PAY")); // credited to interest payable account head......
                txMap.put(TransferTrans.CR_BRANCH, branch);
                txMap.put(TransferTrans.CURRENCY, "INR");
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);

                transferTo = transferTrans.getDebitTransferTO(txMap, Math.abs(balanceInt));
                transferList.add(transferTo);
                transferTo = transferTrans.getCreditTransferTO(txMap, Math.abs(balanceInt));
                transferList.add(transferTo);
                transferTrans.doDebitCredit(transferList, branch);
                System.out.println("******Y 1st transaction : " + txMap + "balanceInt :" + balanceInt);

                txMap = new HashMap();
                transferList = new ArrayList();
                txMap.put(TransferTrans.DR_AC_HD, (String) depositHeadMap.get("INT_PAY"));//debiting int payable a/c head
                txMap.put(TransferTrans.DR_BRANCH, branch);
                txMap.put(TransferTrans.CURRENCY, "INR");
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.PARTICULARS, " " + depSubNoAccInfoSingleRec.get("DEPOSIT_NO") + "_1");

                txMap.put(TransferTrans.CR_AC_HD, (String) depositHeadMap.get("FIXED_DEPOSIT_ACHD"));
                txMap.put(TransferTrans.CR_PROD_ID, prodId);
                txMap.put(TransferTrans.CR_ACT_NUM, depSubNoAccInfoSingleRec.get("DEPOSIT_NO") + "_1");
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                txMap.put(TransferTrans.CR_BRANCH, branch);
                txMap.put(TransferTrans.CURRENCY, "INR");
                System.out.println("******Y 2nd transaction : " + txMap + "balanceInt :" + (balanceInt + payableAmt));

                transferTo = transferTrans.getDebitTransferTO(txMap, Math.abs(balanceInt + payableAmt));
                transferList.add(transferTo);
                txMap.put(TransferTrans.PARTICULARS, " " + depSubNoAccInfoSingleRec.get("DEPOSIT_NO") + "_1");
                transferTo = transferTrans.getCreditTransferTO(txMap, Math.abs(balanceInt + payableAmt));
                transferList.add(transferTo);
                transferTrans.doDebitCredit(transferList, branch);
            } else if (renewInt != null && renewInt.equals("N")
                    && (balanceInt + payableAmt) > 0 && depSubNoAccInfoSingleRec.get("INTPAY_MODE").equals("TRANSFER")) {
                HashMap operativeHeadMap = new HashMap();
                txMap = new HashMap();
                transferList = new ArrayList();
                operativeHeadMap.put("ACT_NUM", depSubNoAccInfoSingleRec.get("INT_PAY_ACC_NO"));
                HashMap operativeAcHeads = (HashMap) sqlMap.executeQueryForObject("getAccNoProdIdDet", operativeHeadMap);
                System.out.println("operativeAcHeads: " + operativeAcHeads);
                transferTrans.setInitiatedBranch(branch);
                transferTrans.setLinkBatchId(depSubNoAccInfoSingleRec.get("DEPOSIT_NO") + "_1");
                txMap.put(TransferTrans.DR_AC_HD, (String) depositHeadMap.get("INT_PROV_ACHD"));//debiting to int paid a/c head
                txMap.put(TransferTrans.PARTICULARS, " " + depSubNoAccInfoSingleRec.get("DEPOSIT_NO") + "_1");
                txMap.put(TransferTrans.DR_BRANCH, branch);
                txMap.put(TransferTrans.CURRENCY, "INR");
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);

                txMap.put(TransferTrans.CR_AC_HD, (String) depositHeadMap.get("INT_PAY")); // credited to interest payable account head......
                txMap.put(TransferTrans.CR_BRANCH, branch);
                txMap.put(TransferTrans.CURRENCY, "INR");
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);

                transferTo = transferTrans.getDebitTransferTO(txMap, Math.abs(balanceInt));
                transferList.add(transferTo);
                transferTo = transferTrans.getCreditTransferTO(txMap, Math.abs(balanceInt));
                transferList.add(transferTo);
                transferTrans.doDebitCredit(transferList, branch);
                System.out.println("******N 1st transaction : " + txMap + "balanceInt :" + balanceInt);

                txMap = new HashMap();
                transferList = new ArrayList();
                txMap.put(TransferTrans.DR_AC_HD, (String) depositHeadMap.get("INT_PAY"));//debiting int payable a/c head
                txMap.put(TransferTrans.DR_BRANCH, branch);
                txMap.put(TransferTrans.CURRENCY, "INR");
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.PARTICULARS, " " + depSubNoAccInfoSingleRec.get("DEPOSIT_NO") + "_1");

                txMap.put(TransferTrans.CR_AC_HD, (String) operativeAcHeads.get("AC_HD_ID"));
                txMap.put(TransferTrans.CR_PROD_ID, depSubNoAccInfoSingleRec.get("INT_PAY_PROD_ID"));
                txMap.put(TransferTrans.CR_ACT_NUM, depSubNoAccInfoSingleRec.get("INT_PAY_ACC_NO"));
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                txMap.put(TransferTrans.CR_BRANCH, branch);
                txMap.put(TransferTrans.PARTICULARS, "Pending Interest from AutoRenewal" + depSubNoAccInfoSingleRec.get("DEPOSIT_NO") + "_1");
                System.out.println("******N 2nd transaction : " + txMap + "balanceInt :" + (balanceInt + payableAmt));
                transferTo = transferTrans.getDebitTransferTO(txMap, Math.abs(balanceInt + payableAmt));
                transferList.add(transferTo);
                txMap.put(TransferTrans.PARTICULARS, " " + depSubNoAccInfoSingleRec.get("DEPOSIT_NO") + "_1");
                transferTo = transferTrans.getCreditTransferTO(txMap, Math.abs(balanceInt + payableAmt));
                transferList.add(transferTo);
                transferTrans.doDebitCredit(transferList, branch);
                operativeHeadMap = null;

            }
            HashMap tdsCalcMap = new HashMap();
            tdsCalcMap.put("CUST_ID", CUST_ID);
            tdsCalcMap.put("PROD_ID", prodId);
            tdsCalcMap.put("DEPOSIT_NO", depSubNoAccInfoSingleRec.get("DEPOSIT_NO"));
            tdsCalcMap.put("RATE_OF_INT", depSubNoAccInfoSingleRec.get("RATE_OF_INT"));
            tdsCalcMap.put("INT_PAY", depositHeadMap.get("INT_PAY"));
            tdsCalcMap.put("INT_PROV_ACHD", depositHeadMap.get("INT_PROV_ACHD"));
            System.out.println("******tdsCalcMap : " + tdsCalcMap);
            if (balanceInt != 0) {
                updatingDepositInterestTable(tdsCalcMap, depositAmt, totIntAmt, currDt, balanceInt, payableAmt);
            }

            objDepSubNoAccInfoTO.setDepositNo(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get("DEPOSIT_NO")));
            objDepSubNoAccInfoTO.setDepositSubNo(CommonUtil.convertObjToInt(depSubNoAccInfoSingleRec.get("DEPOSIT_SUB_NO")));
            objDepSubNoAccInfoTO.setDepositDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get("DEPOSIT_DT"))));
            objDepSubNoAccInfoTO.setDepositPeriodDd(CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get("DEPOSIT_PERIOD_DD")));
            objDepSubNoAccInfoTO.setDepositPeriodMm(CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get("DEPOSIT_PERIOD_MM")));
            objDepSubNoAccInfoTO.setDepositPeriodYy(CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get("DEPOSIT_PERIOD_YY")));
            objDepSubNoAccInfoTO.setDepositAmt(CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get("DEPOSIT_AMT")));
            objDepSubNoAccInfoTO.setIntpayMode(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get("INTPAY_MODE")));
            objDepSubNoAccInfoTO.setIntpayFreq(CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get("INTPAY_FREQ")));
            objDepSubNoAccInfoTO.setMaturityDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get("MATURITY_DT"))));
            objDepSubNoAccInfoTO.setRateOfInt(CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get("RATE_OF_INT")));
            objDepSubNoAccInfoTO.setMaturityAmt(CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get("MATURITY_AMT")));
            objDepSubNoAccInfoTO.setTotIntAmt(CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get("TOT_INT_AMT")));
            objDepSubNoAccInfoTO.setPeriodicIntAmt(CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get("PERIODIC_INT_AMT")));
            objDepSubNoAccInfoTO.setStatus(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get("STATUS")));
            objDepSubNoAccInfoTO.setCloseDt((Date) currDt.clone());
            objDepSubNoAccInfoTO.setCloseBy(CommonUtil.convertObjToStr(CommonConstants.TTSYSTEM));
            objDepSubNoAccInfoTO.setAuthorizeStatus(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get("AUTHORIZE_STATUS")));
            objDepSubNoAccInfoTO.setAuthorizeDt((Date) currDt.clone());
            objDepSubNoAccInfoTO.setAuthorizeBy(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get("AUTHORIZE_BY")));
            objDepSubNoAccInfoTO.setAcctStatus("CLOSED");
            objDepSubNoAccInfoTO.setLastIntApplDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get("LAST_INT_APPL_DT"))));
            objDepSubNoAccInfoTO.setTotalIntCredit(new Double(totIntAmt));
            objDepSubNoAccInfoTO.setTotalIntDrawn(new Double(totIntAmt));
            objDepSubNoAccInfoTO.setTotalBalance(new Double(0.0));
            objDepSubNoAccInfoTO.setClearBalance(new Double(0.0));
            objDepSubNoAccInfoTO.setAvailableBalance(new Double(0.0));
            objDepSubNoAccInfoTO.setInstallType(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get("INSTALL_TYPE")));
            objDepSubNoAccInfoTO.setPaymentDay((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr("")));
            objDepSubNoAccInfoTO.setIntPayProdType(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get("INT_PAY_PROD_TYPE")));
            objDepSubNoAccInfoTO.setIntPayProdId(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get("INT_PAY_PROD_ID")));
            objDepSubNoAccInfoTO.setIntPayAcNo(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get("INT_PAY_ACC_NO")));
            objDepSubNoAccInfoTO.setCalender_day(new Double(0));
            objDepSubNoAccInfoTO.setFlexi_status("Y");
            objDepSubNoAccInfoTO.setRenewedDt(getProperDateFormat(depSubNoAccInfoSingleRec.get("RENEWED_DT")));//31-05-2019           
            sqlMap.executeUpdate("insertDepSubNoAccInfoTOSameNo", objDepSubNoAccInfoTO);
            HashMap intMap = new HashMap();
            intMap.put("DEPOSIT_NO", depSubNoAccInfoSingleRec.get("DEPOSIT_NO"));
            List lst = (List) sqlMap.executeQueryForList("getSelectDepSubNoIntDetails", intMap);
            if (lst != null && lst.size() > 0) {
                intMap = (HashMap) lst.get(0);
                intMap.put("ACT_NUM", intMap.get("DEPOSIT_NO"));
                double currRate = CommonUtil.convertObjToDouble(intMap.get("CURR_RATE_OF_INT")).doubleValue();
                if (currRate == 0) {
                    currRate = 0.0;
                }
                double penalRate = CommonUtil.convertObjToDouble(intMap.get("PENAL_RATE")).doubleValue();
                if (penalRate == 0) {
                    penalRate = 0.0;
                }
                double sbInt = CommonUtil.convertObjToDouble(intMap.get("SB_INT_AMT")).doubleValue();
                if (sbInt == 0) {
                    sbInt = 0.0;
                }
                double sbPeriod = CommonUtil.convertObjToDouble(intMap.get("SB_PERIOD_RUN")).doubleValue();
                if (sbPeriod == 0) {
                    sbPeriod = 0.0;
                }
                double interestAmt = CommonUtil.convertObjToDouble(intMap.get("INTEREST_AMT")).doubleValue();
                if (interestAmt == 0) {
                    interestAmt = 0.0;
                }
                intMap.put("ACCT_STATUS", "CLOSED");
                intMap.put("CURR_RATE_OF_INT", currRate);
                intMap.put("SB_INT_AMT", CommonUtil.convertObjToDouble(sbInt));
                //intMap.put("SB_PERIOD_RUN", String.valueOf(sbPeriod));
                intMap.put("SB_PERIOD_RUN", CommonUtil.convertObjToDouble(sbPeriod));
                intMap.put("BAL_INT_AMT", CommonUtil.convertObjToDouble(interestAmt));
                intMap.put("INT", CommonUtil.convertObjToDouble(penalRate));
                sqlMap.executeUpdate("updateSbInterestAmountSameNo", intMap);//sameno table itwill store.
                intMap.put("RENEWED_DT", currDt.clone());
                sqlMap.executeUpdate("updateSbInterestAmountSameNo", intMap);//sameno table itwill store.
                intMap = null;
            }
            objDepSubNoAccInfoTO = new DepSubNoAccInfoTO();

            objDepSubNoAccInfoTO.setDepositNo(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get("DEPOSIT_NO")));
            objDepSubNoAccInfoTO.setDepositSubNo(CommonUtil.convertObjToInt(depSubNoAccInfoSingleRec.get("DEPOSIT_SUB_NO")));
            objDepSubNoAccInfoTO.setCommand(CommonConstants.STATUS_CREATED);
            objDepSubNoAccInfoTO.setDepositPeriodDd(CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get("DEPOSIT_PERIOD_DD")));
            objDepSubNoAccInfoTO.setDepositPeriodMm(CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get("DEPOSIT_PERIOD_MM")));
            objDepSubNoAccInfoTO.setDepositPeriodYy(CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get("DEPOSIT_PERIOD_YY")));
            objDepSubNoAccInfoTO.setIntpayFreq(CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get("INTPAY_FREQ")));
            objDepSubNoAccInfoTO.setIntpayMode(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get("INT_PAY_MODE")));
            objDepSubNoAccInfoTO.setInstallType(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get("INSTALL_TYPE")));
            objDepSubNoAccInfoTO.setPaymentType(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get("PAYMENT_TYPE")));
            objDepSubNoAccInfoTO.setPaymentDay((Date) depSubNoAccInfoSingleRec.get("PAYMENT_DAY"));
            objDepSubNoAccInfoTO.setLastTransDt((Date) depSubNoAccInfoSingleRec.get("LAST_TRANS_DT"));
            objDepSubNoAccInfoTO.setAcctStatus("NEW");
            objDepSubNoAccInfoTO.setTotalIntCredit(new Double(0.0));
            objDepSubNoAccInfoTO.setTotalIntDrawn(new Double(0.0));
            objDepSubNoAccInfoTO.setLastTransDt((Date) currDt.clone());
            objDepSubNoAccInfoTO.setPeriodicIntAmt(new Double(0.0));
            objDepSubNoAccInfoTO.setStatus(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get("STATUS")));
            objDepSubNoAccInfoTO.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
            objDepSubNoAccInfoTO.setAuthorizeDt((Date) currDt.clone());
            objDepSubNoAccInfoTO.setAuthorizeBy(CommonConstants.TTSYSTEM);
            objDepSubNoAccInfoTO.setIntpayMode(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get("INTPAY_MODE")));

            HashMap productMap = new HashMap();
            HashMap prodMap = new HashMap();
            prodMap.put("PROD_ID", prodId);
            int period = 0;
            List resList = (List) sqlMap.executeQueryForList("getBehavesLike", prodMap);
            productMap.put("BEHAVES_LIKE", ((HashMap) resList.get(0)).get("BEHAVES_LIKE"));
            int duration_DD = CommonUtil.convertObjToInt(depSubNoAccInfoSingleRec.get("DEPOSIT_PERIOD_DD"));
            int duration_MM = CommonUtil.convertObjToInt(depSubNoAccInfoSingleRec.get("DEPOSIT_PERIOD_MM"));
            int duration_YY = CommonUtil.convertObjToInt(depSubNoAccInfoSingleRec.get("DEPOSIT_PERIOD_YY"));
            period = duration_DD + (duration_MM * 30) + (duration_YY * 365);
            objDepSubNoAccInfoTO.setDepositDt(depositDt);
            Date maturityDt = DateUtil.addDays(depositDt, period);
            objDepSubNoAccInfoTO.setMaturityDt(maturityDt);
            productMap.put("PRODUCT_TYPE", "TD");
            productMap.put("PROD_ID", prodId);
            productMap.put("AMOUNT", CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get("DEPOSIT_AMT")));
            productMap.put("CUSTID", CUST_ID);
            productMap.put("DEPOSITNO", depSubNoAccInfoSingleRec.get("DEPOSIT_NO"));
            productMap.put("DEPOSIT_DT", depSubNoAccInfoSingleRec.get("DEPOSIT_DT"));
            productMap.put("MATURITY_DT", currDt.clone());
            productMap.put("PERIOD", period);
            productMap.put("CATEGORY_ID", category);
            HashMap inputMap = getRateOfInt(productMap);
            System.out.println("productMap 3rd : " + productMap);
            System.out.println("productMap : " + productMap);
            double amount = 0.0;
            double rateOfInt = CommonUtil.convertObjToDouble(inputMap.get("ROI")).doubleValue();
            objDepSubNoAccInfoTO.setRateOfInt(new Double(rateOfInt));
            if (rateOfInt > 0 && productMap.get("BEHAVES_LIKE").equals("FIXED")) {
                if (renewInt != null && renewInt.equals("Y")) {
                    depositAmt = depositAmt + balanceInt + payableAmt;
                    amount = depositAmt * period * rateOfInt / 36500;
                    System.out.println("amount : " + amount + "period : " + period + "rateOfInt :" + rateOfInt + "depositAmt :" + depositAmt);
                    amount = (double) getNearest((long) ((long) amount * 100), 100) / 100;
                    objDepSubNoAccInfoTO.setDepositAmt(new Double(depositAmt));
                    objDepSubNoAccInfoTO.setMaturityAmt(new Double(depositAmt));
                } else {
                    amount = depositAmt * period * rateOfInt / 36500;
                    System.out.println("amount : " + amount + "period : " + period + "rateOfInt :" + rateOfInt + "depositAmt :" + depositAmt);
                    amount = (double) getNearest((long) ((long) amount * 100), 100) / 100;
                    objDepSubNoAccInfoTO.setDepositAmt(new Double(depositAmt));
                    objDepSubNoAccInfoTO.setMaturityAmt(new Double(depositAmt));
                }
            } else if (rateOfInt > 0 && productMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                double maturityAmt = CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get("MATURITY_AMT")).doubleValue();
                objDepSubNoAccInfoTO.setDepositAmt(new Double(maturityAmt));
                duration_YY = CommonUtil.convertObjToInt(depSubNoAccInfoSingleRec.get("DEPOSIT_PERIOD_YY"));
                duration_MM = CommonUtil.convertObjToInt(depSubNoAccInfoSingleRec.get("DEPOSIT_PERIOD_MM"));
                period = duration_MM + (duration_YY * 12);
                System.out.println("maturityAmt : " + maturityAmt + "period : " + period + "rateOfInt :" + rateOfInt + "depositAmt :" + depositAmt);
                amount = maturityAmt * (Math.pow((1 + (rateOfInt / 100) / 4.0), period / 12 * 4.0));
                System.out.println("amount : " + amount + "period : " + period + "rateOfInt :" + rateOfInt + "depositAmt :" + depositAmt);

                duration_DD = CommonUtil.convertObjToInt(depSubNoAccInfoSingleRec.get("DEPOSIT_PERIOD_DD"));//INCASE ANY DAYS IS THERE
                amount = amount + (amount * duration_DD * rateOfInt / 36500);
                amount = (double) getNearest((long) ((long) amount * 100), 100) / 100;
                System.out.println("amount : " + amount + "period : " + period + "rateOfInt :" + rateOfInt + "depositAmt :" + depositAmt);
                objDepSubNoAccInfoTO.setMaturityAmt(new Double(amount));
                amount = amount - maturityAmt;
            }
            objDepSubNoAccInfoTO.setTotIntAmt(new Double(amount));
            objDepSubNoAccInfoTO.setIntPayProdType(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get("INT_PAY_PROD_TYPE")));
            objDepSubNoAccInfoTO.setIntPayProdId(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get("INT_PAY_PROD_ID")));
            objDepSubNoAccInfoTO.setIntPayAcNo(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get("INT_PAY_ACC_NO")));
            depSubNoTOMap.put(String.valueOf(i), objDepSubNoAccInfoTO);
            //calcuateTDS(tdsCalcMap, depositAmt, totIntAmt, currDt.clone());                //calcualting TDS...
            objDepSubNoAccInfoTO = null;
            depSubNoAccInfoSingleRec = null;
            productMap = null;
            inputMap = null;
            tdsCalcMap = null;
            prodMap = null;
            depositHeadMap = null;
            transferList = null;
            txMap = null;
            transferTrans = null;
            transferTo = null;
        }
        return depSubNoTOMap;
    }

    private HashMap getRateOfInt(HashMap inputMap) throws Exception {
        List list = (List) sqlMap.executeQueryForList("icm.getInterestRates", inputMap);
        System.out.println("list : " + list);
        if (list != null && list.size() > 0) {
            inputMap.putAll((HashMap) list.get(0));
        } else {
            inputMap.put("ROI", "0.0");
            inputMap.put("PENAL_INT", "0.0");
        }
        return inputMap;
    }

    private void updatingDepositInterestTable(HashMap interestMap, double depositAmt, double totIntAmt, Date currDt, double balanceInt, double payableAmt) throws Exception {
        System.out.println("interestMap :" + interestMap + "totIntAmt :" + totIntAmt + "depositAmt :" + depositAmt + "balanceInt :" + balanceInt + "payableAmt " + payableAmt);
        interestBatchTO = new InterestBatchTO();
        HashMap prodMap = new HashMap();
        prodMap.put("PROD_ID", prodId);
        List resList = (List) sqlMap.executeQueryForList("getBehavesLike", prodMap);
        prodMap.put("BEHAVES_LIKE", ((HashMap) resList.get(0)).get("BEHAVES_LIKE"));
        if (prodMap.get("BEHAVES_LIKE").equals("FIXED")) {
            interestBatchTO.setIntType("SIMPLE");
        } else if (prodMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
            interestBatchTO.setIntType("COMPOUND");
        }
        interestBatchTO.setApplDt((Date) currDt.clone());
        interestBatchTO.setActNum(interestMap.get("DEPOSIT_NO") + "_1");
        interestBatchTO.setProductId(CommonUtil.convertObjToStr(interestMap.get("PROD_ID")));
        interestBatchTO.setPrincipleAmt(new Double(depositAmt));
        interestBatchTO.setCustId(CommonUtil.convertObjToStr(interestMap.get("CUST_ID")));
        interestBatchTO.setIntRate(CommonUtil.convertObjToDouble(interestMap.get("RATE_OF_INT")));
        interestBatchTO.setIntDt((Date) currDt.clone());
        interestBatchTO.setAcHdId(CommonUtil.convertObjToStr(interestMap.get("INT_PROV_ACHD")));
        interestBatchTO.setProductType("TD");
        interestBatchTO.setTrnDt((Date) currDt.clone());
        HashMap renewalCountMap = new HashMap();
        renewalCountMap.put("ACT_NUM", interestMap.get("DEPOSIT_NO"));
        List lstCount = sqlMap.executeQueryForList("getSelectRenewalCount", renewalCountMap);
        if (lstCount != null && lstCount.size() > 0) {
            renewalCountMap = (HashMap) lstCount.get(0);
            interestBatchTO.setSlNo(CommonUtil.convertObjToDouble(renewalCountMap.get("RENEWAL_COUNT")));
        }
        HashMap depMap = new HashMap();
        if (balanceInt != 0 || payableAmt != 0) {
            if (balanceInt > 0) {
                interestBatchTO.setDrCr("CREDIT");
                interestBatchTO.setTransLogId("Payable");
                interestBatchTO.setAcHdId(CommonUtil.convertObjToStr(interestMap.get("INT_PROV_ACHD")));
                interestBatchTO.setIntAmt(new Double(balanceInt));
                interestBatchTO.setTot_int_amt(new Double(balanceInt));
                sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
            }
            if ((payableAmt + balanceInt) > 0) {
                interestBatchTO.setDrCr("DEBIT");
                interestBatchTO.setAcHdId(CommonUtil.convertObjToStr(interestMap.get("INT_PAY")));
                interestBatchTO.setTransLogId("Payable");
                interestBatchTO.setIntAmt(new Double(balanceInt + payableAmt));
                interestBatchTO.setTot_int_amt(new Double(0.0));
                sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
            }
        }
        interestMap = null;
        depositAmt = 0;
        totIntAmt = 0;
        interestBatchTO = null;
    }

    public LinkedHashMap setJointAcctData(List jntAcctAll) {
        HashMap singleRecordJntAcct;
        LinkedHashMap jntAcctTOMap = new LinkedHashMap();
        JointAccntTO objJointAccntTO;
        int jntAcctSize = jntAcctAll.size();
        for (int i = 0; i < jntAcctSize; i++) {
            singleRecordJntAcct = (HashMap) jntAcctAll.get(i);
            objJointAccntTO = new JointAccntTO();
            objJointAccntTO.setCustId(CommonUtil.convertObjToStr(singleRecordJntAcct.get("CUST_ID")));
            objJointAccntTO.setDepositNo(CommonUtil.convertObjToStr(singleRecordJntAcct.get("DEPOSIT_NO")));
            objJointAccntTO.setStatus(CommonUtil.convertObjToStr(singleRecordJntAcct.get("STATUS")));
            objJointAccntTO.setCommand(CommonConstants.STATUS_CREATED);
            jntAcctTOMap.put(String.valueOf(i), objJointAccntTO);
            objJointAccntTO = null;
            singleRecordJntAcct = null;
        }
        return jntAcctTOMap;
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

    public ArrayList setNominee(List lst) {
        ArrayList data = new ArrayList();
        NomineeTO nominee;
        for (int i = 0, j = lst.size(); i < j; i++) {
            nominee = (NomineeTO) lst.get(i);
            nominee.setStatusDt((Date) currDt.clone());
            data.add(nominee);
        }
        return data;
    }

    public HashMap setPoa(List poaList) {
        int poaLen = poaList.size();
        HashMap poaMap = new HashMap();
        if (poaLen > 0) {
            PowerAttorneyTO objPOATO;
            for (int j = 0; j < poaLen; j++) {
                objPOATO = (PowerAttorneyTO) poaList.get(j);
                objPOATO.setCommand(CommonConstants.TOSTATUS_INSERT);
                objPOATO.setStatusBy(CommonConstants.TTSYSTEM);
                objPOATO.setStatusDt((Date) currDt.clone());
                objPOATO.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
                poaMap.put(String.valueOf(j), objPOATO);
            }
        }
        return poaMap;
    }

    public AccInfoTO setAccInfoData(HashMap map) throws Exception {
        AccInfoTO objAccInfoTO = new AccInfoTO();
        objAccInfoTO.setDepositNo(CommonUtil.convertObjToStr(map.get("DEPOSIT_NO")));
        objAccInfoTO.setAuthorizedSignatory(CommonUtil.convertObjToStr(map.get("AUTHORIZED_SIGNATORY")));
        objAccInfoTO.setCommAddress(CommonUtil.convertObjToStr(map.get("COMM_ADDRESS")));
        HashMap prodMap = new HashMap();
        prodMap.put("PROD_ID", prodId);
        List resList = (List) sqlMap.executeQueryForList("getBehavesLike", prodMap);
        prodMap.put("BEHAVES_LIKE", ((HashMap) resList.get(0)).get("BEHAVES_LIKE"));
        if (prodMap.get("BEHAVES_LIKE").equals("FIXED")) {
            objAccInfoTO.setCommand(CommonConstants.TOSTATUS_UPDATE);
        } else if (prodMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
            objAccInfoTO.setCommand(CommonConstants.TOSTATUS_INSERT);
        }
        objAccInfoTO.setCustId(CommonUtil.convertObjToStr(map.get("CUST_ID")));
        objAccInfoTO.setRenewalCount(new Double(CommonUtil.convertObjToDouble(map.get("RENEWAL_COUNT")).doubleValue() + 1));
        objAccInfoTO.setFifteenhDeclare(CommonUtil.convertObjToStr(map.get("FIFTEENH_DECLARE")));
        objAccInfoTO.setNomineeDetails(CommonUtil.convertObjToStr(map.get("NOMINEE_DETAILS")));
        objAccInfoTO.setOpeningMode("Renewal");
        objAccInfoTO.setRenewalFromDeposit(CommonUtil.convertObjToStr(map.get("DEPOSIT_NO")));
        objAccInfoTO.setPoa(CommonUtil.convertObjToStr(map.get("POA")));
        objAccInfoTO.setPanNumber(CommonUtil.convertObjToStr(map.get("PAN_NUMBER")));
        objAccInfoTO.setAgentId(CommonUtil.convertObjToStr(map.get("AGENT_ID")));
        objAccInfoTO.setProdId(CommonUtil.convertObjToStr(map.get("PROD_ID")));
        objAccInfoTO.setRemarks(CommonUtil.convertObjToStr(map.get("REMARKS")));
        objAccInfoTO.setSettlementMode(CommonUtil.convertObjToStr(map.get("SETTLEMENT_MODE")));
        objAccInfoTO.setConstitution(CommonUtil.convertObjToStr(map.get("CONSTITUTION")));
        objAccInfoTO.setCategory(CommonUtil.convertObjToStr(map.get("CATEGORY")));
        objAccInfoTO.setBranchId(CommonUtil.convertObjToStr(map.get("BRANCH_ID")));
        objAccInfoTO.setCreatedBy(CommonUtil.convertObjToStr(map.get("CREATED_BY")));
        objAccInfoTO.setStatusBy(CommonUtil.convertObjToStr(map.get("STATUS_BY")));
        objAccInfoTO.setStatus(CommonUtil.convertObjToStr(map.get(CommonConstants.TOSTATUS_INSERT)));
        objAccInfoTO.setTaxDeductions(CommonUtil.convertObjToStr(map.get("TAX_DEDUCTIONS")));
        objAccInfoTO.setStatusDt((Date) currDt.clone());
        objAccInfoTO.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        objAccInfoTO.setAuthorizedDt((Date) currDt.clone());
        objAccInfoTO.setAuthorizedBy(CommonConstants.TTSYSTEM);
        objAccInfoTO.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("INITIATED_BRANCH")));
        objAccInfoTO.setDeathClaim(CommonUtil.convertObjToStr(map.get("DEATH_CLAIM")));
        objAccInfoTO.setAutoRenewal(CommonUtil.convertObjToStr(map.get("AUTO_RENEWAL")));
        objAccInfoTO.setRenewWithInt(CommonUtil.convertObjToStr(map.get("RENEW_WITH_INT")));
        objAccInfoTO.setMatAlertRep(CommonUtil.convertObjToStr(map.get("MAT_ALERT_REPORT")));
        objAccInfoTO.setMember(CommonUtil.convertObjToStr(map.get("MEMBER")));
//        System.out.println("###### setAccInfoData map"+map);
        return objAccInfoTO;
    }

    public LinkedHashMap setAuthorizedSignatory(List authSignList) {
        LinkedHashMap authSignMap = new LinkedHashMap();
        int authLen = authSignList.size();
        if (authLen > 0) {
            AuthorizedSignatoryTO objAuthSign;
            for (int j = 0; j < authLen; j++) {
                objAuthSign = (AuthorizedSignatoryTO) authSignList.get(j);
                objAuthSign.setCommand(CommonConstants.TOSTATUS_INSERT);
                objAuthSign.setStatusBy(CommonConstants.TTSYSTEM);
                objAuthSign.setStatusDt((Date) currDt.clone());
                objAuthSign.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);

                authSignMap.put(String.valueOf(j), objAuthSign);
            }
        }
        return authSignMap;
    }

    private LinkedHashMap setAuthorizedSignatoryInstruction(List autInstList) {
        int authInsLen = autInstList.size();
        LinkedHashMap authInsMap = new LinkedHashMap();
        if (authInsLen > 0) {
            AuthorizedSignatoryInstructionTO objAuthInst;
            for (int j = 0; j < authInsLen; j++) {
                objAuthInst = (AuthorizedSignatoryInstructionTO) autInstList.get(j);
                objAuthInst.setCommand(CommonConstants.TOSTATUS_INSERT);
                objAuthInst.setStatusBy(CommonConstants.TTSYSTEM);
                objAuthInst.setStatusDt((Date) currDt.clone());
                authInsMap.put(String.valueOf(j), objAuthInst);
            }
        }
        return authInsMap;
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

    public String getProductBehaveLike(String param) {
        final HashMap whereMap = new HashMap();
        whereMap.put("PROD_ID", param);
        final List resultList = ServerUtil.executeQuery("getProductBehavesLike", whereMap);
        HashMap resultProductBehavesLike = (HashMap) resultList.get(0);
        productBehavesLike = CommonUtil.convertObjToStr(resultProductBehavesLike.get("BEHAVES_LIKE"));
        productInterestType = CommonUtil.convertObjToStr(resultProductBehavesLike.get("INT_TYPE"));
        // setBehavesLike(productBehavesLike);
        return productBehavesLike;
    }

    private String calculateRenewalMatDate(String depMatDate, int pYear, int pMonth, int pDays) {
        java.util.Date depDate = DateUtil.getDateWithoutMinitues(depMatDate);
        //system.out.println("####calculateMatDate : " + depDate);
        if (depDate != null) {
            GregorianCalendar cal = new GregorianCalendar((depDate.getYear() + yearTobeAdded), depDate.getMonth(), depDate.getDate());
            if (pYear > 0) {
                cal.add(GregorianCalendar.YEAR, pYear);
            } else {
                cal.add(GregorianCalendar.YEAR, 0);
            }
            if (pMonth > 0) {
                cal.add(GregorianCalendar.MONTH, pMonth);
            } else {
                cal.add(GregorianCalendar.MONTH, 0);
            }
            if (pDays > 0) {
                double txtBoxPeriod = CommonUtil.convertObjToDouble(pDays).doubleValue();
                String totMonths = String.valueOf(txtBoxPeriod / 365);
                long totyears = new Long(totMonths.substring(0, totMonths.indexOf("."))).longValue();
                double leftOverMth = new Double(totMonths.substring(totMonths.indexOf("."))).doubleValue();
                java.text.DecimalFormat df = new java.text.DecimalFormat("#####");
                leftOverMth = new Double(df.format(leftOverMth * 365)).doubleValue();
                if (totyears >= 1) {
                    cal.add(GregorianCalendar.YEAR, (int) totyears);
                    cal.add(GregorianCalendar.DAY_OF_MONTH, (int) leftOverMth);
                } else {
                    cal.add(GregorianCalendar.DAY_OF_MONTH, pDays);
                }
            } else {
                cal.add(GregorianCalendar.DAY_OF_MONTH, 0);
            }
            //  observable.setRenewaltdtMaturityDate(DateUtil.getStringDate(cal.getTime()));
            ///tdtRenewalMaturityDate.setDateValue(observable.getRenewaltdtMaturityDate());
            return DateUtil.getStringDate(cal.getTime());
        }
        return null;
    }

    public double setRenewalRateOfInterset(String prodiD, String category, double renewedDepAmt, String depdate, String renMatDate) {
        double retInt = -1;
        long period = 0;
        HashMap whereMap = new HashMap();
        whereMap.put("PRODUCT_TYPE", "TD");
        String sourceProdId = prodiD;
        String prodId = prodiD;

        whereMap.put("CATEGORY_ID", category);
        whereMap.put("PROD_ID", prodiD);
        whereMap.put("AMOUNT", renewedDepAmt);
        whereMap.put("PRODUCT_TYPE", "TD");
        whereMap.put("DEPOSIT_DT", depdate);
        Date startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depdate));
        Date endDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(renMatDate));
        whereMap.put("DEPOSIT_DT", startDt);
        if (startDt != null && endDt != null) {
            period = DateUtil.dateDiff(startDt, endDt);
        }
        whereMap.put("PERIOD", period);
        List dataList = (List) ServerUtil.executeQuery("icm.getInterestRates", whereMap);
        HashMap roiHash = new HashMap();
        if (dataList != null && dataList.size() > 0) {
            roiHash = (HashMap) dataList.get(0);
            retInt = CommonUtil.convertObjToDouble(roiHash.get("ROI")).doubleValue();
        } else {
            retInt = 0;
        }
        return retInt;
    }

    public HashMap setRenewalAmountsAccROI(HashMap detailsHash, HashMap param) {
        System.out.println("########setAmountsAccROI111 : " + detailsHash + "param-->" + param);
        HashMap amtDetHash = new HashMap();
        long period = 0;
        long cummPeriod = 0;
        long cummMonth = 0;
        double totalAmt = 0.0;
        if (param == null) {
            if (detailsHash.get("BEHAVES_LIKE").equals("FIXED")) {
                if (detailsHash.containsKey("INTEREST_TYPE") && detailsHash.get("INTEREST_TYPE") != null && detailsHash.get("INTEREST_TYPE").equals("MONTHLY")) {
                    period = CommonUtil.convertObjToInt(detailsHash.get("PERIOD_MONTHS")) * 30;
                    period = period + CommonUtil.convertObjToInt(detailsHash.get("PERIOD_YEARS")) * 360;
                } else {
                    Date startDt = null;
                    Date endDt = null;
                    if (detailsHash.containsKey("DEPOSIT_DT") && detailsHash.containsKey("MATURITY_DT")) {
                        startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(detailsHash.get("DEPOSIT_DT")));
                        endDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(detailsHash.get("MATURITY_DT")));
                    } else {
                        startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(detailsHash.get("DEPOSIT_DT")));
                        endDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(detailsHash.get("MATURITY_DT")));
                    }
                    period = DateUtil.dateDiff(startDt, endDt);
                    int count = 0;
                    while (DateUtil.dateDiff(startDt, endDt) > 0) {
                        int month = startDt.getMonth();
                        int startYear = startDt.getYear() + 1900;
                        if (month == 1 && startYear % 4 == 0) {
                            count++;
                        }
                        startDt = DateUtil.addDays(startDt, 30);
                    }
                    period -= count;
                }
            } else if (detailsHash.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                if (detailsHash.containsKey("PERIOD_DAYS") && detailsHash.get("PERIOD_DAYS") != null) {
                    period = period + CommonUtil.convertObjToInt(detailsHash.get("PERIOD_DAYS"));
                    cummPeriod = period;
                }
                if (detailsHash.containsKey("PERIOD_MONTHS") && detailsHash.get("PERIOD_MONTHS") != null) {
                    period = period + CommonUtil.convertObjToInt(detailsHash.get("PERIOD_MONTHS")) * 30;
                    cummMonth = CommonUtil.convertObjToInt(detailsHash.get("PERIOD_MONTHS"));
                }
                if (detailsHash.containsKey("PERIOD_YEARS") && detailsHash.get("PERIOD_YEARS") != null) {
                    period = period + CommonUtil.convertObjToInt(detailsHash.get("PERIOD_YEARS")) * 360;
                }
                long fullPeriod = 0;
                fullPeriod = period;
                double simpleAmt = 0.0;
                double completeAmt = 0.0;
                cummPeriod = cummPeriod % 30;
                cummMonth = cummMonth % 3;
                if (cummMonth == 0) {
                    //system.out.println("******** cummPeriod == 0: "+cummPeriod);
                }
                if (cummPeriod > 0 || cummMonth > 0) {
                    cummMonth = cummMonth * 30;
                    cummPeriod = cummPeriod + cummMonth;
                    //system.out.println("******** cummPeriod != 0: "+cummPeriod);
                }
                if (fullPeriod > 0) {
                    period = fullPeriod - cummPeriod;
                    detailsHash.put("BEHAVES_LIKE", "CUMMULATIVE");
                    detailsHash.put("PEROID", String.valueOf(period));
                    detailsHash.put("CATEGORY_ID", CommonUtil.convertObjToStr(detailsHash.get("CATEGORY_ID")));
                    detailsHash.put("PROD_ID", CommonUtil.convertObjToStr(detailsHash.get("PROD_ID")));
                    List list = (List) ServerUtil.executeQuery("getDepProdIntPay", detailsHash);
                    if (list != null && list.size() > 0) {
                        detailsHash.putAll((HashMap) list.get(0));
                        InterestCalc interestCalc = new InterestCalc();
                        amtDetHash = interestCalc.calcAmtDetails(detailsHash);
                        completeAmt = CommonUtil.convertObjToDouble(amtDetHash.get("INTEREST")).doubleValue();
                    }
                    detailsHash.put("AMOUNT", amtDetHash.get("AMOUNT"));
                    detailsHash.remove("PEROID");
                    int yearPer = CommonUtil.convertObjToInt(detailsHash.get("PERIOD_YEARS"));
                    yearPer = yearPer * 12;
                    int monthPer = CommonUtil.convertObjToInt(detailsHash.get("PERIOD_MONTHS"));
                    monthPer = (monthPer + yearPer) / 3;
                    int totMonth = monthPer * 3;
                    int tot = 0;
                    Date endDt = null;
                }
                if (cummPeriod > 0) {
                    detailsHash.put("BEHAVES_LIKE", "FIXED");
                    detailsHash.put("PEROID", String.valueOf(cummPeriod));
                    detailsHash.put("CATEGORY_ID", detailsHash.get("CATEGORY_ID"));
                    detailsHash.put("PROD_ID", detailsHash.get("PROD_ID"));
                    List list = (List) ServerUtil.executeQuery("getDepProdIntPay", detailsHash);
                    if (list != null && list.size() > 0) {
                        detailsHash.putAll((HashMap) list.get(0));
                        InterestCalc interestCalc = new InterestCalc();
                        detailsHash.put("INTEREST_TYPE", "YEARLY");
                        detailsHash.put("CALC_OPENING_MODE", "CALC_OPENING_MODE");
                        amtDetHash = interestCalc.calcAmtDetails(detailsHash);
                        simpleAmt = CommonUtil.convertObjToDouble(amtDetHash.get("INTEREST")).doubleValue();
                    }
                }
                detailsHash.put("BEHAVES_LIKE", "CUMMULATIVE");
                double interest = simpleAmt + completeAmt;
                amtDetHash.put("INTEREST", new Double(interest));
                //system.out.println("******** : "+detailsHash);
            } else {
                detailsHash.put("PEROID", String.valueOf(period));
            }
            //system.out.println(" set Amount ROI rate of interest param"+detailsHash);
        }
        if (!detailsHash.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
            //--- If the param is NUll then put category and Prod ,else put all the param value to the detailHash
            detailsHash.put("PEROID", String.valueOf(period));
            if (param == null) {
                detailsHash.put("CATEGORY_ID", CommonUtil.convertObjToStr(detailsHash.get("CATEGORY_ID")));
                detailsHash.put("PROD_ID", CommonUtil.convertObjToStr(detailsHash.get("PROD_ID")));
            } else {
                detailsHash.putAll(param);
            }
            HashMap discountedMap = new HashMap();
            discountedMap.put("PROD_ID", CommonUtil.convertObjToStr(detailsHash.get("PROD_ID")));
            List lstDiscounted = ServerUtil.executeQuery("getDepProdDetails", discountedMap);
            if (lstDiscounted != null && lstDiscounted.size() > 0) {
                discountedMap = (HashMap) lstDiscounted.get(0);
                detailsHash.put("DISCOUNTED_RATE", discountedMap.get("DISCOUNTED_RATE"));
            }
            List list = (List) ServerUtil.executeQuery("getDepProdIntPay", detailsHash);
            if (list != null && list.size() > 0) {
                detailsHash.putAll((HashMap) list.get(0));
                InterestCalc interestCalc = new InterestCalc();
                amtDetHash = interestCalc.calcAmtDetails(detailsHash);
            }
        }
        return amtDetHash;
    }

    public AccInfoTO setAccInfoData(HashMap dataMap, String categ) {
        objAccInfoTO = new AccInfoTO();
        try {
            /* Sets the Authroized signatory to "Y" if it is checked
             else it assigns "N" */
            objAccInfoTO.setAuthorizedSignatory(CommonUtil.convertObjToStr(dataMap.get("AUTHORIZED_SIGNATORY")));
            objAccInfoTO.setCommAddress(CommonUtil.convertObjToStr(dataMap.get("COMM_ADDRESS")));
            objAccInfoTO.setCommand("RENEW");
            objAccInfoTO.setCustId(CommonUtil.convertObjToStr(dataMap.get("CUST_ID")));
            objAccInfoTO.setDepositNo(CommonUtil.convertObjToStr(dataMap.get("DEPOSIT_NO")));
            objAccInfoTO.setRenewalFromDeposit(CommonUtil.convertObjToStr(dataMap.get("DEPOSIT_NO")));
            objAccInfoTO.setOpeningMode("Renewal");
            objAccInfoTO.setDepositNo("Renewal");//CommonUtil.convertObjToStr(dataMap.get("Deposit No")));
            int renCount = CommonUtil.convertObjToInt(dataMap.get("RENEWAL_COUNT"));
            objAccInfoTO.setRenewalCount(CommonUtil.convertObjToDouble((renCount + 1)));
            objAccInfoTO.setFifteenhDeclare(CommonUtil.convertObjToStr(dataMap.get("FIFTEENH_DECLARE")));
            objAccInfoTO.setNomineeDetails(CommonUtil.convertObjToStr(dataMap.get("NOMINEE_DETAILS")));
            objAccInfoTO.setOpeningMode("Renewal");
            objAccInfoTO.setTransOut(CommonUtil.convertObjToStr(dataMap.get("TRANS_OUT")));
            objAccInfoTO.setDeathClaim(CommonUtil.convertObjToStr(dataMap.get("DEATH_CLAIM")));
            objAccInfoTO.setAutoRenewal(CommonUtil.convertObjToStr(dataMap.get("AUTO_RENEWAL")));
            objAccInfoTO.setRenewWithInt(CommonUtil.convertObjToStr(dataMap.get("RENEW_WITH_INT")));//bbbb
            objAccInfoTO.setMatAlertRep(CommonUtil.convertObjToStr(dataMap.get("MAT_ALERT_REPORT")));
            objAccInfoTO.setStandingInstruct(CommonUtil.convertObjToStr(dataMap.get("STANDING_INSTRUCT")));
            objAccInfoTO.setPanNumber(CommonUtil.convertObjToStr(dataMap.get("PAN_NUMBER")));
            objAccInfoTO.setPoa(CommonUtil.convertObjToStr(dataMap.get("POA")));
            objAccInfoTO.setAgentId(CommonUtil.convertObjToStr(dataMap.get("AGENT_ID")));
            objAccInfoTO.setProdId(CommonUtil.convertObjToStr(dataMap.get("PROD_ID")));
            objAccInfoTO.setRemarks(CommonUtil.convertObjToStr(dataMap.get("REMARKS")));
            objAccInfoTO.setMdsGroup(CommonUtil.convertObjToStr(dataMap.get("MDS_GROUP")));
            objAccInfoTO.setMdsRemarks(CommonUtil.convertObjToStr(dataMap.get("MDS_REMARKS")));
            objAccInfoTO.setSettlementMode(CommonUtil.convertObjToStr("0"));
            objAccInfoTO.setConstitution(CommonUtil.convertObjToStr(dataMap.get("CONSTITUTION")));
            objAccInfoTO.setAddressType(CommonUtil.convertObjToStr(dataMap.get("ADDR_TYPE")));
            objAccInfoTO.setCategory(CommonUtil.convertObjToStr(categ));
            //objAccInfoTO.setCustType("");
            objAccInfoTO.setBranchId(branch);
            objAccInfoTO.setCreatedBy(CommonConstants.TTSYSTEM);
            objAccInfoTO.setCreatedDt((Date) currDt.clone());
            objAccInfoTO.setStatusBy(CommonConstants.TTSYSTEM);
            objAccInfoTO.setStatus(CommonConstants.STATUS_CREATED);
            objAccInfoTO.setStatusDt((Date) currDt.clone());
            objAccInfoTO.setInitiatedBranch(branch);
            objAccInfoTO.setPrintingNo(CommonUtil.convertObjToInt(dataMap.get("PRINTING_NO")));
            objAccInfoTO.setReferenceNo(CommonUtil.convertObjToStr(dataMap.get("REFERENCE_NO")));
            objAccInfoTO.setMember(CommonUtil.convertObjToStr(dataMap.get("MEMBER")));
            objAccInfoTO.setTaxDeductions(CommonUtil.convertObjToStr(dataMap.get("TAX_DEDUCTIONS")));//"TAX_DEDUCTIONS");
            objAccInfoTO.setAccZeroBalYN(CommonUtil.convertObjToStr(dataMap.get("ACC_ZERO_BAL_YN")));
        } catch (Exception e) {
            //  parseException.logException(e, true);
        }
        return objAccInfoTO;
    }

    private HashMap insertJntAcctSingleRec(HashMap custMapData, String dbYesOrNo) {
        jntAcctSingleRec = new HashMap();
        jntAcctSingleRec.put("CUST_ID", CommonUtil.convertObjToStr(custMapData.get("CUST_ID")));
        jntAcctSingleRec.put(FLD_FOR_DB_YES_NO, dbYesOrNo);
        jntAcctSingleRec.put("STATUS", "CREATED");
        return jntAcctSingleRec;
    }

    public HashMap setJointAccntData(HashMap dataMap) {
        HashMap singleRecordJntAcct;
        jntAcctTOMap = new LinkedHashMap();
        try {
            JointAccntTO objJointAccntTO;
            HashMap hash = new HashMap();
            hash.put("CUST_ID", CommonUtil.convertObjToStr(dataMap.get("CUST_ID")));
            // jntAcctAll = objJointAcctHolderManipulation.populateJointAccntTable(hash, jntAcctAll, tblJointAccnt);
            if (jntAcctAll == null) { //--- If jointAcctAll Hashmap is null, initialize it.
                jntAcctAll = new LinkedHashMap();
            }
            HashMap custMapData;
            List custListData = ServerUtil.executeQuery("getSelectAccInfoTblDisplay", dataMap);
            custMapData = (HashMap) custListData.get(0);
            String keyCustId = CommonUtil.convertObjToStr(custMapData.get("CUST_ID"));
            /* If there is No Customer Id, insert the all the data with dbYesOrNo having "No" value
             * else, insert the all the data with dbYesOrNo having "Yes" value */
            if (jntAcctAll.get(keyCustId) == null) {
                custMapData = insertJntAcctSingleRec(custMapData, NO_FULL_STR);
            } else {
                custMapData = insertJntAcctSingleRec(custMapData, YES_FULL_STR);
            }
            jntAcctAll.put(keyCustId, custMapData);
            System.out.println("jntAcctAll%$%#%#%" + jntAcctAll);
            custListData = null;
            custMapData = null;
            int jntAcctSize = jntAcctAll.size();
            for (int i = 0; i < jntAcctSize; i++) {
                singleRecordJntAcct = (HashMap) jntAcctAll.get(CommonUtil.convertObjToStr(jntAcctAll.keySet().toArray()[i]));
                objJointAccntTO = new JointAccntTO();
                objJointAccntTO.setCustId(CommonUtil.convertObjToStr(singleRecordJntAcct.get("CUST_ID")));
                objJointAccntTO.setDepositNo(CommonUtil.convertObjToStr(dataMap.get("DEPOSIT_NO")));
                objJointAccntTO.setStatus(CommonUtil.convertObjToStr(singleRecordJntAcct.get("STATUS")));
                objJointAccntTO.setCommand("RENEW");
                jntAcctTOMap.put(String.valueOf(i), objJointAccntTO);
                System.out.println("jntAcctTOMap%$%#%#%" + jntAcctTOMap);
                objJointAccntTO = null;
                singleRecordJntAcct = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jntAcctTOMap;
    }

    public HashMap setDepSubNoAccInfoData(HashMap dataMap, HashMap renewalMap, String prodBevaves) {
        HashMap depSubNoAccInfoSingleRec;
        depSubNoTOMap = new LinkedHashMap();
        System.out.println("renewalMap-------->" + renewalMap);
        try {
            DepSubNoAccInfoTO objDepSubNoAccInfoTO;
            //  int depSubNoSize = depSubNoAll.size();
            //  for (int i = 0; i < renewalMap.size(); i++) {
            // depSubNoAccInfoSingleRec = (HashMap) depSubNoAll.get(String.valueOf(i));
            objDepSubNoAccInfoTO = new DepSubNoAccInfoTO();
            objDepSubNoAccInfoTO.setCommand("RENEW");
            objDepSubNoAccInfoTO.setDepositAmt(CommonUtil.convertObjToDouble(dataMap.get("DEPOSIT_AMT")));
            objDepSubNoAccInfoTO.setDepositDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dataMap.get("OLD_MATURITY_DATE"))));
            objDepSubNoAccInfoTO.setDepositNo("Renewal");
            objDepSubNoAccInfoTO.setDepositPeriodDd(CommonUtil.convertObjToDouble(dataMap.get("DEPOSIT_PERIOD_DD")));
            objDepSubNoAccInfoTO.setDepositPeriodMm(CommonUtil.convertObjToDouble(dataMap.get("DEPOSIT_PERIOD_MM")));
            objDepSubNoAccInfoTO.setDepositPeriodYy(CommonUtil.convertObjToDouble(dataMap.get("DEPOSIT_PERIOD_YY")));
            //mm  
            //objDepSubNoAccInfoTO.setDepositPeriodWk("");
            objDepSubNoAccInfoTO.setDepositSubNo(CommonUtil.convertObjToInt("1"));
            objDepSubNoAccInfoTO.setIntpayFreq(CommonUtil.convertObjToDouble(renewalMap.get("RENEWAL_INT_FREQ")));
            objDepSubNoAccInfoTO.setIntpayMode(CommonUtil.convertObjToStr(renewalMap.get("INTPAY_MODE")));
            objDepSubNoAccInfoTO.setInstallType(CommonUtil.convertObjToStr(""));
            objDepSubNoAccInfoTO.setPaymentType(CommonUtil.convertObjToStr(""));
            objDepSubNoAccInfoTO.setPaymentDay(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_MATURITY_DT"))));
            System.out.println("prodBevaves->" + prodBevaves);
            if (prodBevaves != null && prodBevaves.equals("FIXED")) {
                // if(!whereMap.containsKey("INT_WITHDRAWING")){
                objDepSubNoAccInfoTO.setMaturityAmt(CommonUtil.convertObjToDouble(dataMap.get("DEPOSIT_AMT")));
                objDepSubNoAccInfoTO.setMaturityDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_MATURITY_DT"))));
                // if (CdataMap.get("RENEW_WITH_INT")) {

                objDepSubNoAccInfoTO.setTotIntAmt(CommonUtil.convertObjToDouble(renewalMap.get("RENEWAL_TOT_INTAMT")));
                // } else {
                objDepSubNoAccInfoTO.setTotIntAmt(CommonUtil.convertObjToDouble(renewalMap.get("RENEWAL_TOT_INTAMT")));
                //   }
                //  }
            } else {
                objDepSubNoAccInfoTO.setMaturityAmt(CommonUtil.convertObjToDouble(renewalMap.get("RENEWAL_MATURITY_AMT")));
                objDepSubNoAccInfoTO.setMaturityDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_MATURITY_DT"))));
                objDepSubNoAccInfoTO.setTotIntAmt(CommonUtil.convertObjToDouble(renewalMap.get("RENEWAL_TOT_INTAMT")));
            }
            objDepSubNoAccInfoTO.setPeriodicIntAmt(CommonUtil.convertObjToDouble(""));
            objDepSubNoAccInfoTO.setRateOfInt(CommonUtil.convertObjToDouble(renewalMap.get("RENEWAL_RATE_OF_INT")));
            objDepSubNoAccInfoTO.setSubstatusDt((Date) currDt.clone());
            objDepSubNoAccInfoTO.setStatus(CommonUtil.convertObjToStr(CommonConstants.STATUS_CREATED));

            objDepSubNoAccInfoTO.setIntPayProdId(CommonUtil.convertObjToStr(renewalMap.get("INT_PAY_PROD_ID")));
            objDepSubNoAccInfoTO.setIntPayProdType(CommonUtil.convertObjToStr(renewalMap.get("INT_PAY_PROD_TYPE")));
            objDepSubNoAccInfoTO.setIntPayAcNo(CommonUtil.convertObjToStr(renewalMap.get("INT_PAY_ACC_NO")));
            // if (depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_CALENDER_FREQ) != null && depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_CALENDER_FREQ).equals("Y")) {
            //      objDepSubNoAccInfoTO.setCalender_freq(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_CALENDER_FREQ)));
            //      objDepSubNoAccInfoTO.setCalender_date(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_CALENDER_DATE))));
            //      objDepSubNoAccInfoTO.setCalender_day(CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_CALENDER_DAY)));
            // } else {
            objDepSubNoAccInfoTO.setCalender_freq("N");
            objDepSubNoAccInfoTO.setCalender_date(DateUtil.getDateMMDDYYYY(""));
            objDepSubNoAccInfoTO.setCalender_day(CommonUtil.convertObjToDouble(""));
            //  }
            if ("RENEW".equals("RENEW")) {
                objDepSubNoAccInfoTO.setFlexi_status("NR");
            } else {
                objDepSubNoAccInfoTO.setFlexi_status("N");
            }
            objDepSubNoAccInfoTO.setSalaryRecovery("N");

            String productId = CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PRODID"));
            objDepSubNoAccInfoTO.setPostageAmt(CommonUtil.convertObjToDouble(""));
            objDepSubNoAccInfoTO.setRenewPostageAmt(CommonUtil.convertObjToDouble(""));
            HashMap recurringMap = new HashMap();
            recurringMap.put("PROD_ID", productId);
            List lst = ServerUtil.executeQuery("getBehavesLikeForDeposit", recurringMap);
            if (lst.size() > 0) {
                recurringMap = (HashMap) lst.get(0);
                if (recurringMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                    double period = CommonUtil.convertObjToInt(objDepSubNoAccInfoTO.getDepositPeriodYy());
                    double periodMm = 0.0;
                    if (period >= 1) {
                        period = period * 12.0;
                        periodMm = CommonUtil.convertObjToInt(objDepSubNoAccInfoTO.getDepositPeriodMm());
                        period = period + periodMm;
                    } else {
                        period = CommonUtil.convertObjToInt(objDepSubNoAccInfoTO.getDepositPeriodMm());
                    }
                    objDepSubNoAccInfoTO.setTotalInstallments(new Double(period));
                }

            }
            depSubNoTOMap.put("0", objDepSubNoAccInfoTO);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return depSubNoTOMap;
    }

    public TaskStatus executeTask() throws Exception {
        String SCREEN = "TD";
        String TERM_DEPOSIT_FOR_DAO = "TERMDEPOSIT";
        String DEP_SUB_NO_FOR_DAO = "DepSubNoAccInfoTO";
        String JOINT_ACCNT_FOR_DAO = "JointAccntTO";
        String AUTH_SIGN_DAO = "AuthorizedSignatoryTO";
        String AUTH_SIGN_INST_DAO = "AuthorizedSignatoryInstructionTO";
        //        Date currDt = currDt;
        HashMap data = new HashMap();
        HashMap paramMap = new HashMap();
        TaskStatus status = new TaskStatus();
        status.setStatus(BatchConstants.STARTED);

        sqlMap.startTransaction();
        sqlMap.executeUpdate("updateDepositShadowCreditDebit", paramMap);//Added By Suresh 16-Jul-2019   Refered By Rajesh/Sathiya
        sqlMap.commitTransaction();

        if (branchList != null && branchList.size() > 0) {
            for (int b = 0; b < branchList.size(); b++) {
                HashMap branchMap = (HashMap) branchList.get(b);
                branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                currDt = ServerUtil.getCurrentDate(branch);
                HashMap compMap = new HashMap();
                compMap.put("TASK_NAME", taskLable);
                compMap.put("DAYEND_DT", (Date) currDt.clone());
                compMap.put("BRANCH_ID", branch);
                List compLst = (List) sqlMap.executeQueryForList("getSelectTaskLst", compMap);
                compMap = null;
                String compStatus = "";
                if (compLst != null && compLst.size() > 0) {
                    compMap = (HashMap) compLst.get(0);
                    compStatus = CommonUtil.convertObjToStr(compMap.get("TASK_STATUS"));
                    compMap = null;
                }

                paramMap.put("BRANCH_CODE", branch);
                paramMap.put("TODAY_DT", currDt.clone());

                data.put("UI_PRODUCT_TYPE", SCREEN);
                //        System.out.println(status.getStatus());
                AccInfoTO accInfoTo = null;
                TermDepositDAO objDepositDao = new TermDepositDAO();
                int count = holidayProvision(currDt, branch, USER_ID);
                System.out.println("count : " + count);//__ To Know the Mode...
                if (count > 0) {
                    Date holidayDate = (Date) currDt.clone();
                    holidayDate = DateUtil.addDays(holidayDate, -count);
                    //            if(holidayDate.getDate()>0){
                    //                holidayDate.setDate(holidayDate.getDate());
                    //                holidayDate.setMonth(holidayDate.getMonth());
                    //                holidayDate.setYear(holidayDate.getYear());
                    //            }
                    System.out.println("holidayDate : " + holidayDate);//__ To Know the Mode...
                    paramMap.put("HOLIDAY_MATURITY_DT", holidayDate);
                } else {
                    paramMap.put("HOLIDAY_MATURITY_DT", currDt.clone());
                }

                List renDepositList = sqlMap.executeQueryForList("getAutoRenewableDeposits", paramMap);
                paramMap.remove("BRANCH_CODE");
                paramMap.remove("TODAY_DT");
                System.out.println("renDepositList : " + renDepositList.size());
                if (compLst.size() <= 0 || compStatus.equals("ERROR")) {
                    sqlMap.startTransaction();
                    HashMap errorMap = new HashMap();
                    errorMap.put("ERROR_DATE", currDt.clone());
                    errorMap.put("TASK_NAME", taskLable);
                    errorMap.put("BRANCH_ID", branch);
                    sqlMap.executeUpdate("deleteError_showing", errorMap);
                    sqlMap.commitTransaction();
                    DepSubNoAccInfoTO objDepSubNoAccInfoTO = null;
                    for (int i = 0; i < renDepositList.size(); i++) {
                        try {
                            //  sqlMap.startTransaction();
                            generateSingleTransId = generateLinkID();
//                            HashMap errorMap = new HashMap();
                            //    accInfoTo = (AccInfoTO) renDepositList.get(i);
                            //    paramMap.put("DEPOSIT NO", accInfoTo.getDepositNo());
                            //    paramMap.put("UI_PRODUCT_TYPE", SCREEN);
                            //   System.out.println("##### executeTask : " + paramMap);
                            //  data = objDepositDao.executeQuery(paramMap);
                            //////////////////////////////
                            HashMap renewalMap = new HashMap();
                            HashMap dataMap = (HashMap) renDepositList.get(i);
                            String accNo = CommonUtil.convertObjToStr(dataMap.get("DEPOSIT_NO"));
                            String depAmt = CommonUtil.convertObjToStr(dataMap.get("DEPOSIT_AMT"));
                            String prodid = CommonUtil.convertObjToStr(dataMap.get("PROD_ID"));
                            String prodBevaves = getProductBehaveLike(prodid);
                            if (prodBevaves != null && prodBevaves.equals("FIXED")) {
                                if (dataMap.containsKey("INTPAY_MODE") && dataMap.get("INTPAY_MODE") != null && !dataMap.get("INTPAY_MODE").equals("")
                                        && dataMap.get("INTPAY_MODE").equals("CASH") && dataMap.containsKey("RENEW_WITH_INT") && dataMap.get("RENEW_WITH_INT") != null && !dataMap.get("RENEW_WITH_INT").equals("") && dataMap.get("RENEW_WITH_INT").equals("N")) {
                                    System.out.println("Renewal without interest account : " + accNo);
                                    continue;
                                }
                            }
                            String categ = "";
                            categ = CommonUtil.convertObjToStr(dataMap.get("CATEGORY"));
                            String currCategory = CommonUtil.convertObjToStr(dataMap.get("CATEGORY"));

                            String opMode = CommonUtil.convertObjToStr(dataMap.get("OPENING_MODE"));
                            String MatDate = CommonUtil.convertObjToStr(dataMap.get("MATURITY_DT"));

                            HashMap data1 = new HashMap();
                            HashMap dMap = new HashMap();
                            dMap.put("DEPOSIT NO", accNo);
                            List list = (List) ServerUtil.executeQuery("getSelectDepSubNoAccInfoTO", dMap);
                            HashMap calcDepSubNo;
                            String oldDepDate = "";
                            double renIntAmt = 0;
                            double renewedDepAmt = 0, matAmt = 0, balIntAmt = 0;
                            String renInt = "";
                            System.out.println("accNo : " + accNo + " i Value : " + i);
                            for (int k = 0; k < list.size(); k++) {
                                calcDepSubNo = (HashMap) list.get(k);
                                double totAmt = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOT_INT_AMT")).doubleValue();
                                double drAmt = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_DRAWN")).doubleValue();
                                double crAmt = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_CREDIT")).doubleValue();
                                oldDepDate = CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_DT"));
                                // Added by nithya on 22-04-2021 from KD-2787
                                if (calcDepSubNo.containsKey("LAST_INT_APPL_DT") && calcDepSubNo.get("LAST_INT_APPL_DT") != null) {
                                    oldDepDate = CommonUtil.convertObjToStr(calcDepSubNo.get("LAST_INT_APPL_DT"));
                                }
                                // End
                                balIntAmt = totAmt - drAmt;
                                double balAmt = crAmt - drAmt;
                                balIntAmt = (double) getNearest((long) (balIntAmt * 100), 100) / 100;
                                matAmt = CommonUtil.convertObjToDouble(calcDepSubNo.get("MATURITY_AMT")).doubleValue();
                                renewedDepAmt = 0;
                                renInt = CommonUtil.convertObjToStr(dataMap.get("RENEW_WITH_INT"));
                                if (prodBevaves != null && prodBevaves.equals("FIXED")) {
                                    if (!renInt.equals("") && renInt.equals("Y")) {
                                        renewedDepAmt = matAmt + balIntAmt;
                                    } else {
                                        renewedDepAmt = matAmt;//-balIntAmt;
                                    }
                                } else {
                                    renewedDepAmt = matAmt;
                                }

                                System.out.println("accNo : " + accNo + " k Value : " + k);
                                //ren int calc
                                double maturityAmt = 0.0;
                                double depositAmt = 0;
                                double interestAmt = 0;
                                int pYeras = 0, pMonths = 0, pDays = 0;
                                pYeras = CommonUtil.convertObjToInt(dataMap.get("DEPOSIT_PERIOD_YY"));
                                pMonths = CommonUtil.convertObjToInt(dataMap.get("DEPOSIT_PERIOD_MM"));
                                pDays = CommonUtil.convertObjToInt(dataMap.get("DEPOSIT_PERIOD_DD"));
                                HashMap detailsHash = new HashMap();
                                //txtRenewalRateOfInterest.setText(String.valueOf(txtRenewalRateOfInterest.getText()));
                                String depMatDate = DateUtil.getStringDate((Date) calcDepSubNo.get("MATURITY_DT"));
                                detailsHash.put("AMOUNT", renewedDepAmt);
                                detailsHash.put("DEPOSIT_DT", depMatDate);
                                detailsHash.put("PERIOD_DAYS", pDays);
                                detailsHash.put("PERIOD_MONTHS", pMonths);
                                detailsHash.put("PERIOD_YEARS", pYeras);
                                String RenewMatDate = calculateRenewalMatDate(depMatDate,
                                        pYeras, pMonths, pDays);
                                detailsHash.put("MATURITY_DT", RenewMatDate);
                                double ROI = setRenewalRateOfInterset(prodid, categ, renewedDepAmt,
                                        depMatDate, RenewMatDate);
                                detailsHash.put("ROI", ROI);
                                detailsHash.put("DISCOUNTED_RATE", "");
                                detailsHash.put("BEHAVES_LIKE", prodBevaves);
                                detailsHash.put("CATEGORY_ID", categ);
                                detailsHash.put("PROD_ID", prodid);
                                detailsHash.put("BEHAVES_LIKE", prodBevaves);
                                detailsHash.put("BEHAVES_LIKE", prodBevaves);
                                detailsHash.put("DEPOSIT_DT", DateUtil.getDateMMDDYYYY(depMatDate));
                                detailsHash.put("MATURITY_DT", DateUtil.getDateMMDDYYYY(RenewMatDate));
                                detailsHash = setRenewalAmountsAccROI(detailsHash, null);
                                System.out.println("detailsHash IN MM---->" + detailsHash);
                                double renMatAmount = CommonUtil.convertObjToDouble(detailsHash.get("AMOUNT"));
                                renMatAmount = (double) getNearest((long) (renMatAmount * 100), 100) / 100;
                                renIntAmt = CommonUtil.convertObjToDouble(detailsHash.get("INTEREST"));
                                renIntAmt = (double) getNearest((long) (renIntAmt * 100), 100) / 100;
                                renewalMap.put("RENEWAL_TOT_INTAMT", CommonUtil.convertObjToStr(renIntAmt));
                                renewalMap.put("RENEWAL_DEPOSIT_AMT", CommonUtil.convertObjToStr(renewedDepAmt));
                                renewalMap.put("BALANCE_INT_AMT", CommonUtil.convertObjToStr(balIntAmt));
                                renewalMap.put("RENEWAL_MATURITY_AMT", CommonUtil.convertObjToStr(renMatAmount));//detailsHash.get("AMOUNT")));
                                renewalMap.put("RENEWAL_MATURITY_DT", RenewMatDate);
                                renewalMap.put("RENEWAL_RATE_OF_INT", CommonUtil.convertObjToStr(ROI));
                                renewalMap.put("RENEWAL_INT_FREQ", CommonUtil.convertObjToStr(calcDepSubNo.get("INTPAY_FREQ")));
                                renewalMap.put("INT_PAY_PROD_TYPE", CommonUtil.convertObjToStr(calcDepSubNo.get("INT_PAY_PROD_TYPE")));
                                renewalMap.put("INT_PAY_PROD_ID", CommonUtil.convertObjToStr(calcDepSubNo.get("INT_PAY_PROD_ID")));
                                renewalMap.put("INT_PAY_ACC_NO", CommonUtil.convertObjToStr(calcDepSubNo.get("INT_PAY_ACC_NO")));
                                renewalMap.put("INTPAY_MODE", CommonUtil.convertObjToStr(calcDepSubNo.get("INTPAY_MODE")));
                                renewalMap.put("RENEWAL_PAY_MODE", CommonUtil.convertObjToStr(calcDepSubNo.get("INTPAY_MODE")));
                                String mode = CommonUtil.convertObjToStr(calcDepSubNo.get("INTPAY_MODE"));
                                if (mode != null && mode.equals("TRANSFER")) {
                                    renewalMap.put("RENEWAL_PAY_PRODTYPE", CommonUtil.convertObjToStr(calcDepSubNo.get("INT_PAY_PROD_TYPE")));
                                    renewalMap.put("RENEWAL_PAY_PRODID", CommonUtil.convertObjToStr(calcDepSubNo.get("INT_PAY_PROD_ID")));
                                    renewalMap.put("RENEWAL_PAY_ACCNO", CommonUtil.convertObjToStr(calcDepSubNo.get("INT_PAY_ACC_NO")));
                                }
                                //end
                            }
                            objAccInfoTO = setAccInfoData(dataMap, categ);
                            objAccInfoTO.setCommand("RENEW");
                            data1.put("TERMDEPOSIT", objAccInfoTO);
                            //For joint acc
                            //Below line commented by sreekrishnan as per discussed with jithesh
                     /*if (objAccInfoTO.getConstitution().equals("JOINT_ACCOUNT")) {
                             data1.put("JointAccntTO", setJointAccntData(dataMap));
                             jntAcctTOMap = new LinkedHashMap() ;
                             }*/
                            //--- puts the data if the NomineeDetails is checked
                            if (objAccInfoTO.getNomineeDetails().equals(YES_STR)) {
                                HashMap nomineeMap = new HashMap();
                                nomineeMap.put("DEPOSIT_NO", CommonUtil.convertObjToStr(dataMap.get("DEPOSIT_NO")));
                                List lst1 = (List) ServerUtil.executeQuery("getSelectRenewalNomineeTOTD", nomineeMap);
                                if (lst1 != null && lst1.size() > 0) {
                                    data1.put("AccountNomineeTO", lst1);
                                }
                                // data.put("AccountNomineeTO", objNomineeOB.getNomimeeList());
                                data1.put("AccountNomineeDeleteTO", new ArrayList());
                            }
                            //end
                            renewalMap.put("RENEWAL_DEP_ADDING", "NO");
                            renewalMap.put("RENEW_POSTAGE_AMT", "");
                            renewalMap.put("RENEWAL_INT_PAYABLE", "Y");
                            renewalMap.put("RENEWAL_DEPOSIT_YEARS", CommonUtil.convertObjToStr(dataMap.get("DEPOSIT_PERIOD_YY")));
                            renewalMap.put("OLD_DEPOSIT_DATE", oldDepDate);
                            renewalMap.put("FLD_DEP_RENEWAL_SUB_PRINTINGNO", "");
                            renewalMap.put("RENEWAL_INT_PAYABLE", "Y");
                            renewalMap.put("RENEWAL_CATEGORY", CommonUtil.convertObjToStr(categ));
                            renewalMap.put("RENEWAL_DEPOSIT_DT", CommonUtil.convertObjToStr(dataMap.get("MATURITY_DT")));
                            renewalMap.put("DEPOSIT_NO", CommonUtil.convertObjToStr(dataMap.get("DEPOSIT_NO")));
                            renewalMap.put("RENEWAL_NOTICE", "N");
                            renewalMap.put("SB_PERIOD_RUN", "");//////////////
                            renewalMap.put("AUTO_RENEWAL", "Y");
                            int siFreq = CommonUtil.convertObjToInt(renewalMap.get("RENEWAL_INT_FREQ"));
                            String freq = "";
                            if (siFreq == 30) {
                                freq = CommonUtil.convertObjToStr("Monthly");
                            } else if (siFreq == 90) {
                                freq = CommonUtil.convertObjToStr("Quaterly");
                            } else if (siFreq == 180) {
                                freq = CommonUtil.convertObjToStr("Half Yearly");
                            } else if (siFreq == 360) {
                                freq = CommonUtil.convertObjToStr("Yearly");
                            } else if (siFreq == 60) {
                                freq = CommonUtil.convertObjToStr("2 Months");
                            } else if (siFreq == 120) {
                                freq = CommonUtil.convertObjToStr("4 Months");
                            } else if (siFreq == 150) {
                                freq = CommonUtil.convertObjToStr("5 Months");
                            } else if (siFreq == 210) {
                                freq = CommonUtil.convertObjToStr("7 Months");
                            } else if (siFreq == 240) {
                                freq = CommonUtil.convertObjToStr("8 Months");
                            } else if (siFreq == 270) {
                                freq = CommonUtil.convertObjToStr("9 Months");
                            } else if (siFreq == 300) {
                                freq = CommonUtil.convertObjToStr("10 Months");
                            } else if (siFreq == 330) {
                                freq = CommonUtil.convertObjToStr("11 Months");
                            } else if (siFreq == 0) {
                                freq = "Date of Maturity";
                            }
                            ///periodic int clc
                            double perIntAmt = 0, yr = 0;
                            int pYeras = 0, pMonths = 0, pDays = 0;
                            pYeras = CommonUtil.convertObjToInt(dataMap.get("DEPOSIT_PERIOD_YY"));
                            pMonths = CommonUtil.convertObjToInt(dataMap.get("DEPOSIT_PERIOD_MM"));
                            pDays = CommonUtil.convertObjToInt(dataMap.get("DEPOSIT_PERIOD_DD"));
                            if (pYeras > 0) {
                                yr = CommonUtil.convertObjToDouble(pYeras);
                            }

                            if (pMonths > 0) {
                                yr = yr + (CommonUtil.convertObjToDouble(pMonths) / 12);
                            }

                            if (pDays > 0) {
                                yr = yr + (CommonUtil.convertObjToDouble(pDays) / 365);
                            }
                            double totalIntAmtPerYear = (CommonUtil.convertObjToDouble(renIntAmt) / yr);
                            if (siFreq == 180) {
                                perIntAmt = totalIntAmtPerYear / 2;
                            } else if (siFreq == 30) {
                                perIntAmt = totalIntAmtPerYear / 12;
                                double depositAmt = CommonUtil.convertObjToDouble(renewedDepAmt);

                                //--- Calculation for Period as No.Of Days
                                int YrsToDay = 0;
                                int MonToDay = 0;
                                int daysEntered = 0;
                                int periodInDays = 0;
                                if (pYeras > 0) {
                                    YrsToDay = (CommonUtil.convertObjToInt(pYeras)) * 365;
                                }
                                if (pMonths > 0) {
                                    MonToDay = (CommonUtil.convertObjToInt(pMonths) * 30);
                                }
                                if (pDays > 0) {
                                    daysEntered = (CommonUtil.convertObjToInt(pDays));
                                }
                                periodInDays = (YrsToDay + MonToDay + daysEntered);
                            }
                            if (siFreq == 360) {
                                perIntAmt = totalIntAmtPerYear;
                            } else if (siFreq == 90) {
                                perIntAmt = totalIntAmtPerYear / 4;
                            } else if (siFreq == 0) {
                                perIntAmt = 0;
                            } else if (siFreq == 60) {
                                perIntAmt = totalIntAmtPerYear / 6;
                            } else if (siFreq == 120) {
                                perIntAmt = totalIntAmtPerYear / 3;
                            } else if (siFreq == 150) {
                                perIntAmt = totalIntAmtPerYear / 2.4;
                            } else if (siFreq == 210) {
                                perIntAmt = totalIntAmtPerYear / 1.7;
                            } else if (siFreq == 240) {
                                perIntAmt = totalIntAmtPerYear / 1.5;
                            } else if (siFreq == 270) {
                                perIntAmt = totalIntAmtPerYear / 1.33;
                            } else if (siFreq == 300) {
                                perIntAmt = totalIntAmtPerYear / 1.2;
                            } else if (siFreq == 330) {
                                perIntAmt = totalIntAmtPerYear / 1.09;
                            }
                            try {
                                String depInt = "N";
                                HashMap dataMap1 = new HashMap();
                                dataMap1.put("PROD_ID", CommonUtil.convertObjToStr(dataMap.get("Product Id")));
                                List periodList = ServerUtil.executeQuery("getIntRoundAtIntApplication", dataMap1);
                                if (periodList != null && periodList.size() > 0) {
                                    HashMap periodMap = (HashMap) periodList.get(0);
                                    if (periodMap.containsKey("INT_ROUND_AT_INTAPPL")) {
                                        depInt = "Y";
                                    }
                                }
                                if (depInt != null && depInt.equals("N")) {
                                    perIntAmt = (double) getNearest((long) (perIntAmt * 100), 100) / 100;
                                } else {
                                    DecimalFormat df = new DecimalFormat("#.##");
                                    perIntAmt = CommonUtil.convertObjToDouble(df.format(perIntAmt));
                                }
                                //system.out.println("#### cboInterestPaymentFreqActionPerformed " + perIntAmt);
                            } catch (Exception e) {
                                System.out.println("Exxxxx-->" + e);
                            }
                            //end
                            renewalMap.put("RENEWAL_PAY_FREQ", CommonUtil.convertObjToStr(freq));
                            renewalMap.put("RENEWAL_NOTICE", "N");
                            renewalMap.put("RENEWAL_DEPOSIT_DAYS", CommonUtil.convertObjToStr(dataMap.get("DEPOSIT_PERIOD_DD")));
                            if (renInt.equals("N") || renInt.equals("") || renInt == null) {
                                renewalMap.put("RENEWAL_INT_WITHDRAWING", "YES");
                                renewalMap.put("WITHDRAWING_INT_AMT", CommonUtil.convertObjToStr(renewalMap.get("BALANCE_INT_AMT")));
                                String withDType = CommonUtil.convertObjToStr(dataMap.get("INTPAY_MODE"));
                                renewalMap.put("RENEWAL_INT_TRANS_MODE", withDType);
                                if (withDType.equals("TRANSFER")) {
                                    renewalMap.put("RENEWAL_INT_TRANS_ACCNO", CommonUtil.convertObjToStr(dataMap.get("INT_PAY_ACC_NO")));
                                    renewalMap.put("RENEWAL_INT_TRANS_PRODID", CommonUtil.convertObjToStr(dataMap.get("INT_PAY_PROD_ID")));
                                    renewalMap.put("RENEWAL_INT_TRANS_PRODTYPE", CommonUtil.convertObjToStr(dataMap.get("INT_PAY_PROD_TYPE")));

                                } else {
                                    renewalMap.put("RENEWAL_INT_TOKEN_NO", "");
                                }
                                renewalMap.put("AUTO_RENEWAL_INT_TRANS_MODE", "AUTO_RENEWAL_INT_TRANS_MODE");
                                renewalMap.put("RENEWAL_INT_WITHDRAWING", "YES");
                                renewalMap.put("RENEWAL_INT_WITHDRAWING", "YES");

                            } else {
                                renewalMap.put("RENEWAL_INT_WITHDRAWING", "NO");///////////////////
                            }
                            renewalMap.put("PREV_INT_AMT", "0");
                            renewalMap.put("RENEWAL_DEPOSIT_MONTHS", CommonUtil.convertObjToStr(dataMap.get("DEPOSIT_PERIOD_MM")));
                            renewalMap.put("USER_ID", CommonConstants.TTSYSTEM);
                            renewalMap.put("RENEWAL_PRODID", CommonUtil.convertObjToStr(dataMap.get("PROD_ID")));
                            renewalMap.put("AUTO_RENEW_WITH", CommonUtil.convertObjToStr(dataMap.get("RENEW_WITH_INT")));//babuu
                            renewalMap.put("RENEWAL_CALENDER_FREQ_DAY", "");
                            renewalMap.put("RENEWAL_DEP_WITHDRAWING", "NO");
                            renewalMap.put("SB_INT_AMT", "0");
                            renewalMap.put("RENEWAL_CALENDER_FREQ", "N");

                            renewalMap.put("RENEWAL_PERIODIC_INT", CommonUtil.convertObjToStr(perIntAmt));
                            renewalMap.put("BRANCH_CODE", branch);
                            renewalMap.put("INITIATED_BRANCH", branch);
                            renewalMap.put("OLD_MATURITY_DATE", CommonUtil.convertObjToStr(dataMap.get("MATURITY_DT")));
                            renewalMap.put("PENDING_AMT_RATE", "");
                            data1.put("RENEWALMAP", renewalMap);
                            if (transactionDetailsTO == null) {
                                transactionDetailsTO = new LinkedHashMap();
                            }
                            data1.put(CommonConstants.MODULE, "Time Deposits");
                            data1.put(CommonConstants.SCREEN, "Deposit Accounts");
                            data1.put(CommonConstants.SELECTED_BRANCH_ID, branch);
                            data1.put("BRANCH_CODE", branch);
                            data1.put("INITIATED_BRANCH", branch);
                            data1.put("DepSubNoAccInfoTO", setDepSubNoAccInfoData(dataMap, renewalMap, prodBevaves));
                            transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
                            data1.put("TransactionTO", transactionDetailsTO);
                            data1.put("SAME_DEPOSIT_NO", "");
                            data1.put("UI_PRODUCT_TYPE", "TD");
                            data1.put("MODE", CommonConstants.TOSTATUS_INSERT);
                            HashMap intMap = new HashMap();
                            intMap.put("TRANS_MODE", "");
                            intMap.put("INT", "INT");

                            intMap.put("SB_INT_AMT", String.valueOf("0"));
                            intMap.put("SB_PERIOD_RUN", String.valueOf("0"));
//                intMap.put("BAL_INT_AMT",String.valueOf(getBalanceInterestAmountValue()));

                            intMap.put("ACT_NUM", CommonUtil.convertObjToStr(dataMap.get("DEPOSIT_NO")));
                            intMap.put("ROI", CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_RATE_OF_INT")));
                            intMap.put("CUST_ID", CommonUtil.convertObjToStr(dataMap.get("CUST_ID")));
                            if (prodBevaves != null && prodBevaves.equals("FIXED")) {
                                intMap.put("DEPOSIT_AMT", CommonUtil.convertObjToStr(matAmt));
                                intMap.put("BAL_INT_AMT", CommonUtil.convertObjToStr(balIntAmt));
                            } else {
                                intMap.put("DEPOSIT_AMT", CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEPOSIT_AMT")));
                                intMap.put("BAL_INT_AMT", CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_TOT_INTAMT")));
                            }
                            data1.put("INTMAP", intMap);
                            intMap = null;
                            data1.put("MULTI_REN_SINGLE_TRANS_ID", generateSingleTransId);
                            TermDepositDAO daoDep = new TermDepositDAO();
                            daoDep.execute(data1);

                            HashMap authMap = new HashMap();
                            HashMap renMap = (HashMap) data1.get("RENEWALMAP");
                            authMap.put("USER_ID", CommonUtil.convertObjToStr(renMap.get("USER_ID")));
                            authMap.put("BRANCH_CODE", CommonUtil.convertObjToStr(_branchCode));
                            authMap.put("UI_PRODUCT_TYPE", "TD");
                            HashMap authorizeMap = new HashMap();
                            authorizeMap.put("USER_ID", CommonUtil.convertObjToStr(renMap.get("USER_ID")));
                            authorizeMap.put("AUTHORIZESTATUS", "AUTHORIZED");
                            authorizeMap.put("BRANCH_CODE", CommonUtil.convertObjToStr(_branchCode));
                            ArrayList arrList = new ArrayList();
                            HashMap authDataMap = new HashMap();
                            authDataMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(renMap.get("USER_ID")));
                            String depNo = CommonUtil.convertObjToStr(renMap.get("DEPOSIT_NO"));
                            if (depNo != null && depNo.contains("_")) {
                                int index = depNo.indexOf("_");
                                depNo = depNo.substring(0, index);
                            }
                            authDataMap.put("DEPOSIT NO", depNo);
                            arrList.add(authDataMap);
                            authorizeMap.put("AUTHORIZEDATA", arrList);
                            authMap.put("AUTHORIZEMAP", authorizeMap);
                            TermDepositDAO daoDep1 = new TermDepositDAO();
                            daoDep1.execute(authMap);
                            /*     if (((List) data.get("AcctInfoDetails")).size() > 0) {
                             prodId = accInfoTo.getProdId();
                             category = accInfoTo.getCategory();
                             renewInt = accInfoTo.getRenewWithInt();
                             if (renewInt != null) {
                             System.out.println("renewInt : " + renewInt);
                             CUST_ID = accInfoTo.getCustId();
                             //Modify DEPOSIT_ACINFO Table Data
                             data.put(TERM_DEPOSIT_FOR_DAO, setAccInfoData((HashMap) ((List) data.get("AcctInfoDetails")).get(0)));

                             data.put(DEP_SUB_NO_FOR_DAO, setDepSubNoAccInfoData((List) data.get("DepSubNoAcctInfoDetails")));//Modify DEPOSIT_SUB_ACINFO Table Data

                             if (accInfoTo.getConstitution() != null && accInfoTo.getConstitution().equals("JOINT_ACCOUNT"))//Modify JointAcctDetails
                             {
                             if (data.containsKey("JointAcctDetails") && ((List) data.get("JointAcctDetails")).size() > 0) {
                             data.put(JOINT_ACCNT_FOR_DAO, setJointAcctData((List) data.get("JointAcctDetails")));
                             }
                             }

                             if (accInfoTo.getPoa() != null && accInfoTo.getPoa().equals("Y"))//POA Details
                             {
                             if (data.containsKey("PowerAttorneyTO")) {
                             if (((List) data.get("PowerAttorneyTO")).size() > 0) {
                             data.put("PoATO", setPoa((List) data.get("PowerAttorneyTO")));
                             }
                             }
                             }

                             if (accInfoTo.getNomineeDetails() != null && accInfoTo.getNomineeDetails().equals("Y"))// Nominee Details
                             {
                             if (data.containsKey("AccountNomineeList") && ((List) data.get("AccountNomineeList")).size() > 0) {
                             data.put("AccountNomineeTO", setNominee((List) data.get("AccountNomineeList")));
                             } else {
                             ArrayList dataList = new ArrayList();
                             NomineeTO nominee = new NomineeTO();
                             dataList.add(nominee);
                             data.put("AccountNomineeTO", dataList);
                             }
                             }

                             if (accInfoTo.getAuthorizedSignatory() != null && accInfoTo.getAuthorizedSignatory().equals("Y")) {//Authorization Details
                             if (data.containsKey("AuthorizedSignatoryTO") && ((List) data.get("AuthorizedSignatoryTO")).size() > 0) {
                             data.put(AUTH_SIGN_DAO, setAuthorizedSignatory((List) data.get("AuthorizedSignatoryTO")));
                             data.put(AUTH_SIGN_INST_DAO, setAuthorizedSignatoryInstruction((List) data.get("AuthorizedSignatoryInstructionTO")));
                             System.out.println("##### setAuthorizedSignatory data : " + data);
                             }
                             }
                             data.put("UI_PRODUCT_TYPE", SCREEN);
                             data.put(CommonConstants.BRANCH_ID, branch);
                             data.put(CommonConstants.SELECTED_BRANCH_ID, branch);
                             //                    data.put("SAME_DEPOSIT_NO_AUTO_RENEWAL","");
                             System.out.println("##### OUT OF IF LOOP DATA: " + data);
                             //if no error in closing, open a new account here
                             // closing is called in setDepSubNoAccInfoData()
                             HashMap depositMap = objDepositDao.execute(data, false); //False indicates not to start a new transaction in called DAO
                             System.out.println("#####depositMap : " + depositMap);
                             HashMap prodMap = new HashMap();
                             prodMap.put("PROD_ID", prodId);
                             List resList = (List) sqlMap.executeQueryForList("getBehavesLike", prodMap);
                             prodMap.put("BEHAVES_LIKE", ((HashMap) resList.get(0)).get("BEHAVES_LIKE"));
                             if (prodMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                             HashMap txMap = new HashMap();
                             ArrayList transferList = new ArrayList();
                             TransferTrans transferTrans = new TransferTrans();
                             TxTransferTO transferTo = new TxTransferTO();
                             HashMap depositHeadMap = new HashMap();
                             depositHeadMap.put("PROD_ID", prodId);
                             depositHeadMap = (HashMap) sqlMap.executeQueryForObject("getDepositClosingHeads", depositHeadMap);
                             System.out.println("depositHeadMap: " + depositHeadMap);
                             HashMap amtMap = new HashMap();
                             amtMap.put("DEPOSIT_NO", accInfoTo.getDepositNo());
                             List lst = sqlMap.executeQueryForList("getRenewalNewDetails", amtMap);
                             if (lst != null && lst.size() > 0) {
                             amtMap = (HashMap) lst.get(0);
                             double depositAmt = CommonUtil.convertObjToDouble(amtMap.get("MATURITY_AMT")).doubleValue();
                             txMap = new HashMap();
                             transferList = new ArrayList();
                             transferTrans.setInitiatedBranch(branch);
                             transferTrans.setLinkBatchId(accInfoTo.getDepositNo() + "_1");
                             txMap.put(TransferTrans.DR_AC_HD, (String) depositHeadMap.get("FIXED_DEPOSIT_ACHD"));
                             txMap.put(TransferTrans.DR_PROD_ID, prodId);
                             txMap.put(TransferTrans.DR_ACT_NUM, accInfoTo.getDepositNo() + "_1");
                             txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                             txMap.put(TransferTrans.DR_BRANCH, branch);
                             txMap.put(TransferTrans.CURRENCY, "INR");
                             txMap.put(TransferTrans.PARTICULARS, " " + depositMap.get("DEPOSIT NO") + "_1");

                             txMap.put(TransferTrans.CR_AC_HD, (String) depositHeadMap.get("FIXED_DEPOSIT_ACHD"));
                             txMap.put(TransferTrans.CR_PROD_ID, prodId);
                             txMap.put(TransferTrans.CR_ACT_NUM, depositMap.get("DEPOSIT NO") + "_1");
                             txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                             txMap.put(TransferTrans.CR_BRANCH, branch);
                             txMap.put(TransferTrans.CURRENCY, "INR");
                             System.out.println("******Y 2nd transaction : " + txMap + "depositAmt :" + (depositAmt));

                             transferTo = transferTrans.getDebitTransferTO(txMap, Math.abs(depositAmt));
                             transferList.add(transferTo);
                             txMap.put(TransferTrans.PARTICULARS, " " + accInfoTo.getDepositNo() + "_1");
                             transferTo = transferTrans.getCreditTransferTO(txMap, Math.abs(depositAmt));
                             transferList.add(transferTo);
                             transferTrans.doDebitCredit(transferList, branch);
                             amtMap.put("DEPOSIT NO", accInfoTo.getDepositNo());
                             amtMap.put("ACCT_STATUS", "CLOSED");
                             amtMap.put("DEPOSIT_STATUS", "CLOSED");
                             sqlMap.executeUpdate("updateRejectionStatusAcinfo", amtMap);
                             sqlMap.executeUpdate("updateRejectionStatusSubAcinfo", amtMap);
                             amtMap = null;
                             }
                             txMap = null;
                             transferList = null;
                             transferList = null;
                             transferTrans = null;
                             transferTo = null;
                             depositHeadMap = null;
                             }
                             if (prodMap.get("BEHAVES_LIKE").equals("FIXED")) {
                             HashMap updateMap = new HashMap();
                             updateMap.put("DEPOSIT_NO", accInfoTo.getDepositNo());
                             updateMap.put("TOTAL_INT_CREDIT", new Double(0.0));
                             updateMap.put("TOTAL_INT_DRAWN", new Double(0.0));
                             updateMap.put("TOTAL_INT_DEBIT", new Double(0.0));
                             sqlMap.executeUpdate("updateLastIntApplDtNull", updateMap);
                             updateMap = null;
                             }
                             HashMap renewalMap = new HashMap();
                             renewalMap.put("DEPOSIT_NO", accInfoTo.getDepositNo());
                             List lst = sqlMap.executeQueryForList("getSelectMaxSLNo", renewalMap);
                             if (lst != null && lst.size() > 0) {
                             renewalMap = (HashMap) lst.get(0);
                             double maxNo = CommonUtil.convertObjToDouble(renewalMap.get("MAX_NO")).doubleValue();
                             renewalMap.put("DEPOSIT_NO", accInfoTo.getDepositNo());
                             renewalMap.put("COUNT", new Double(maxNo + 1));
                             sqlMap.executeUpdate("updateMaxSLNo", renewalMap);
                             } else {
                             renewalMap.put("DEPOSIT_NO", accInfoTo.getDepositNo());
                             renewalMap.put("COUNT", new Double(1));
                             sqlMap.executeUpdate("updateMaxSLNo", renewalMap);
                             }
                             renewalMap = null;
                             }
                             }*/

                            //  sqlMap.commitTransaction();
                            //  }
                        } catch (Exception err) {
                            //  sqlMap.rollbackTransaction();
//                            status.setStatus(BatchConstants.ERROR) ;
//                            System.out.println("Error thrown for Depsoit No " + accInfoTo.getDepositNo());
                            err.printStackTrace();
//                            sqlMap.rollbackTransaction();
//                        status.setStatus(BatchConstants.ERROR) ;
//                        errorMap = new HashMap();
//                        errorMap.put("ERROR_DATE",currDt.clone());
//                        errorMap.put("TASK_NAME", "FlexiTask");
//                        errorMap.put("ERROR_MSG",err.getMessage());
//                        errorMap.put("ACT_NUM",paramMap.get("DEPOSIT NO"));
//                        errorMap.put("BRANCH_ID", branch);
//                        sqlMap.startTransaction();
//                        sqlMap.executeUpdate("insertError_showing", errorMap);
//                        sqlMap.commitTransaction();
//                        System.out.println("Error thrown for Operative A/c No " + dataMap.get("ACT_NUM"));
//                             if (taskSelected != OBCODE) {
                            String errMsg = "";
                            TTException tte = null;
                            HashMap exceptionMap = null;
                            HashMap excMap = null;
                            String strExc = null;
                            String errClassName = "";
                            if (err instanceof TTException) {
                                System.out.println("#$#$ if TTException part..." + err);
                                tte = (TTException) err;
                                if (tte != null) {
                                    exceptionMap = tte.getExceptionHashMap();
                                    System.out.println("#$#$ if TTException part exceptionMap ..." + exceptionMap);
                                    if (exceptionMap != null) {
                                        ArrayList list = (ArrayList) exceptionMap.get(CommonConstants.EXCEPTION_LIST);
                                        errClassName = CommonUtil.convertObjToStr(exceptionMap.get(CommonConstants.CONSTANT_CLASS));
                                        System.out.println("#$#$ if TTException part EXCEPTION_LIST ..." + list);
                                        if (list != null && list.size() > 0) {
                                            for (int a = 0; a < list.size(); a++) {
                                                if (list.get(a) instanceof HashMap) {
                                                    excMap = (HashMap) list.get(a);
                                                    System.out.println("#$#$ if TTException part excMap ..." + excMap);
                                                    strExc = CommonUtil.convertObjToStr(excMap.get(CommonConstants.ERR_MESSAGE))
                                                            + " (" + CommonUtil.convertObjToStr(excMap.get(CommonConstants.ERR_DATA)) + ")";
                                                } else {
                                                    strExc = (String) list.get(a);
                                                    System.out.println("#$#$ if TTException part strExc ..." + strExc);
                                                }
                                                errorMap = new HashMap();
                                                errorMap.put("ERROR_DATE", currDt.clone());
                                                errorMap.put("TASK_NAME", taskLable);
                                                errorMap.put("ERROR_MSG", strExc);
                                                errorMap.put("ERROR_CLASS", errClassName);
                                                errorMap.put("ACT_NUM", paramMap.get("ACT_NUM"));
                                                errorMap.put("BRANCH_ID", branch);
                                                sqlMap.startTransaction();
                                                sqlMap.executeUpdate("insertError_showing", errorMap);
                                                sqlMap.commitTransaction();
                                                errorMap = null;
                                            }
                                        }
                                    } else {
                                        System.out.println("#$#$ if not TTException part..." + err);
                                        errMsg = err.getMessage();
                                        errorMap = new HashMap();
                                        errorMap.put("ERROR_DATE", currDt.clone());
                                        errorMap.put("TASK_NAME", taskLable);
                                        errorMap.put("ERROR_MSG", errMsg);
                                        errorMap.put("ERROR_CLASS", errClassName);
                                        errorMap.put("ACT_NUM", paramMap.get("DEPOSIT NO"));
                                        errorMap.put("BRANCH_ID", branch);
                                        sqlMap.startTransaction();
                                        sqlMap.executeUpdate("insertError_showing", errorMap);
                                        sqlMap.commitTransaction();
                                        errorMap = null;
                                    }
                                }
                            } else {
                                System.out.println("#$#$ if not TTException part..." + err);
                                errMsg = err.getMessage();
                                errorMap = new HashMap();
                                errorMap.put("ERROR_DATE", currDt.clone());
                                errorMap.put("TASK_NAME", taskLable);
                                errorMap.put("ERROR_MSG", errMsg);
                                errorMap.put("ERROR_CLASS", errClassName);
                                errorMap.put("ACT_NUM", paramMap.get("DEPOSIT NO"));
                                errorMap.put("BRANCH_ID", branch);
                                System.out.println("#$#$ if not TTException part... errorMap:" + errorMap);
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
//                }
                            err.printStackTrace();
                        }
                    }



                }
//                 }catch(Exception err){
////                            sqlMap.rollbackTransaction();
////                            status.setStatus(BatchConstants.ERROR) ;
////                            System.out.println("Error thrown for Depsoit No " + accInfoTo.getDepositNo());
////                            err.printStackTrace();
//                           sqlMap.rollbackTransaction();
//                        status.setStatus(BatchConstants.ERROR) ;
//                        sqlMap.startTransaction();
//                        if(compStatus.equals("ERROR")){
//                            HashMap statusMap = new HashMap();
//                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
//                            statusMap.put("BRANCH_CODE", branch);
//                            statusMap.put("TASK_NAME", taskLable);
//                            statusMap.put("TASK_STATUS", "ERROR");
//                            statusMap.put("USER_ID", getHeader().getUserID());
//                            statusMap.put("DAYEND_DT", currDt.clone());
//                            sqlMap.executeUpdate("updateTskStatus", statusMap);
//                            statusMap = null;
//                        }else{
//                            isError = true;
//                            HashMap statusMap = new HashMap();
//                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
//                            statusMap.put("BRANCH_CODE", branch);
//                            statusMap.put("TASK_NAME", taskLable);
//                            statusMap.put("TASK_STATUS", "ERROR");
//                            statusMap.put("USER_ID", getHeader().getUserID());
//                            statusMap.put("DAYEND_DT", currDt.clone());
//                            sqlMap.executeUpdate("InsertDayEndStatus", statusMap);
//                            statusMap = null;
//                            
//                        }
//                        HashMap errorMap = new HashMap();
//                        errorMap.put("ERROR_DATE",currDt.clone());
//                        errorMap.put("TASK_NAME", taskLable);
//                        errorMap.put("ERROR_MSG",err.getMessage());
//                        System.out.println("Error ERROR_MSG " + err.getMessage());
//                        errorMap.put("ACT_NUM","asd");
//                        errorMap.put("BRANCH_ID", branch);
//                        System.out.println("errorMap" + errorMap);
//                        //                        sqlMap.startTransaction();
//                        sqlMap.executeUpdate("insertError_showing", errorMap);
//                        //                        sqlMap.commitTransaction();
//                        //                            System.out.println("Error thrown for Depsoit No " + dataMap.get("DEPOSITNO"));
//                        //                             sqlMap.rollbackTransaction();
//                        sqlMap.commitTransaction();
//                        
//                        err.printStackTrace();
//                        }
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
                            statusMap.put("DAYEND_DT", currDt.clone());
                            sqlMap.executeUpdate("updateTskStatus", statusMap);
                            statusMap = null;
                        } else {
                            HashMap statusMap = new HashMap();
                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                            statusMap.put("BRANCH_CODE", branch);
                            statusMap.put("TASK_NAME", taskLable);
                            statusMap.put("TASK_STATUS", "COMPLETED");
                            statusMap.put("USER_ID", getHeader().getUserID());
                            statusMap.put("DAYEND_DT", currDt.clone());
                            sqlMap.executeUpdate("InsertDayEndStatus", statusMap);
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
                            statusMap.put("DAYEND_DT", currDt.clone());
                            sqlMap.executeUpdate("updateTskStatus", statusMap);
                            statusMap = null;
                        } else {
                            isError = true;
                            HashMap statusMap = new HashMap();
                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                            statusMap.put("BRANCH_CODE", branch);
                            statusMap.put("TASK_NAME", taskLable);
                            statusMap.put("TASK_STATUS", "ERROR");
                            statusMap.put("USER_ID", getHeader().getUserID());
                            statusMap.put("DAYEND_DT", currDt.clone());
                            sqlMap.executeUpdate("InsertDayEndStatus", statusMap);
                            statusMap = null;

                        }
                    }
                }
            }
        }
        data = null;
        paramMap = null;
        if (status.getStatus() != BatchConstants.ERROR) {
            status.setStatus(BatchConstants.COMPLETED);
        }
//        if(isError){
//                status.setStatus(BatchConstants.ERROR);
//                isError = false;
//            }
        System.out.println("Completion Status : " + status.getStatus());
        return status;
    }

    private int holidayProvision(Date matDt, String BRANCH_ID, String USER_ID) throws Exception {
        int count = 0;
        try {
            HashMap weeklyOff = new HashMap();
            HashMap holidayMap = new HashMap();
            weeklyOff.put("NEXT_DATE", DateUtil.addDaysProperFormat(matDt, -4));
            //  weeklyOff.put("NEXT_DATE", DateUtil.addDays(matDt, -4));
            weeklyOff.put(CommonConstants.BRANCH_ID, BRANCH_ID);
            weeklyOff.put("CURR_DATE", matDt);
            List lst = sqlMap.executeQueryForList("checkHolidayProvisionTD", weeklyOff);
            if (lst != null && lst.size() > 0) {
                for (int j = 0; j < lst.size(); j++) {
                    count = count + 1;
                }
                holidayMap.put("NEXT_DATE", matDt);
                holidayMap.put(CommonConstants.BRANCH_ID, BRANCH_ID);
                sqlMap.executeQueryForList("checkWeeklyOffOD", holidayMap);
                if (lst != null && lst.size() > 0) {
                    count = count + 1;
                }
            } else {
                lst = sqlMap.executeQueryForList("checkWeeklyOffOD", weeklyOff);
                if (lst != null && lst.size() > 0) {
                    count = count + 1;
                }
                lst = sqlMap.executeQueryForList("checkHolidayProvisionTD", weeklyOff);
                if (lst != null && lst.size() > 0) {
                    for (int j = 0; j < lst.size(); j++) {
                        count = count + 1;
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    private HashMap calcuateTDS(HashMap tdsCalcMap, double depositAmt, double totIntAmt, Date currDt) throws Exception {
        System.out.println("####### doDepositClose tdsCalcMap " + tdsCalcMap);
        double intTrfAmt = 0.0;
        String Prod_type = "TD";
        interestBatchTO = new InterestBatchTO();
        TdsCalc tdsCalculator = new TdsCalc(branch);
        String CustId = CommonUtil.convertObjToStr(tdsCalcMap.get("CUST_ID"));
        String prod_id = CommonUtil.convertObjToStr(tdsCalcMap.get("PROD_ID"));
        String accnum = CommonUtil.convertObjToStr(tdsCalcMap.get("DEPOSIT_NO"));
        //        intTrfAmt = CommonUtil.convertObjToDouble(tdsCalcMap.get("TDS_AMOUNT")).doubleValue();
        HashMap tdsMap = new HashMap();
        HashMap closeMap = new HashMap();
        closeMap.put("DEPOSIT_NO", tdsCalcMap.get("DEPOSIT_NO"));
        closeMap.put("RATE_OF_INT", tdsCalcMap.get("RATE_OF_INT"));
        closeMap.put("CUSTID", tdsCalcMap.get("CUST_ID"));
        tdsMap.put("INT_DATE", currDt.clone());
        //        tdsMap.put("INT_DATE", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(ServerUtil.getCurrentDate(_branchCode))));
        tdsMap.put("CUSTID", tdsCalcMap.get("CUST_ID"));

        List exceptionList = (List) sqlMap.executeQueryForList("getTDSExceptionData", tdsMap);
        if (exceptionList == null || exceptionList.size() <= 0) {
            tdsMap = new HashMap();
            tdsMap = tdsCalculator.tdsCalcforInt(CustId, intTrfAmt, accnum, Prod_type, prod_id, closeMap);
            System.out.println("####### doDepositClose tdsMap " + tdsMap);
            if (tdsMap != null) {
                interestBatchTO.setIsTdsApplied("Y");
                interestBatchTO.setTdsAmt(CommonUtil.convertObjToDouble(tdsMap.get("TDSDRAMT")));
            }
        }
        return tdsMap;
    }
    
    public Date getProperDateFormat(Object obj) {
        Date currDate1 = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDate1 = (Date) currDt.clone();
            currDate1.setDate(tempDt.getDate());
            currDate1.setMonth(tempDt.getMonth());
            currDate1.setYear(tempDt.getYear());
        }
        return currDate1;
    }

    public static void main(String arg[]) {
        try {
            TaskHeader header = new TaskHeader();
            header.setTaskClass("DepositAutoRenewalTask");
            HashMap paramMap = new HashMap();
            header.setBranchID(CommonConstants.BRANCH_ID);
            header.setUserID("sysadmin");
            header.setTaskParam(paramMap);
            DepositAutoRenewalTask tsk = new DepositAutoRenewalTask(header);
            TaskStatus status = tsk.executeTask();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
