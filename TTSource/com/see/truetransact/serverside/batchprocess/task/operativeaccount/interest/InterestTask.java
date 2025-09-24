/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * IntPayableTask.java
 *
 * Created on October 27, 2004, 11:17 AM
 * Author : Sunil
 */
package com.see.truetransact.serverside.batchprocess.task.operativeaccount.interest;

import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
//import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.LocaleConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.interestcalc.*;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.exceptionconstants.termloan.TermLoanConstants;
import com.see.truetransact.transferobject.batchprocess.interest.*;
//import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.transaction.transfer.*;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.common.interest.InterestTaskRunner;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.Calendar;
import com.see.truetransact.serverside.batchprocess.task.operativeaccount.DailyBalanceUpdateTask;
import java.text.SimpleDateFormat;
import java.util.*;
import org.omg.PortableInterceptor.LOCATION_FORWARD;
//import com.see.truetransact.commonutil.interestcalc.InterestCalculationConstants;
//import com.see.truetransact.commonutil.interestcalc.Rounding;

/**
 *
 * @author rahul
 */
public class InterestTask extends Task {

    private static SqlMap sqlMap = null;
    private TransferDAO transferDAO = new TransferDAO();
    private String branch;
    private String userID;
    private int taskSelected = 0;
    private final int BATCHPROCESS = 1;
    private final int OBCODE = 2;
    private final int INSERTDATA = 3;
    private final String SCREEN = "INTEREST";
    private final String MODULE = "OPERATIVE";
    private final  String PENALTY="PENALTY";
    private final  String ROI="ROI";
    private final  String INTEREST="INTEREST";
    private HashMap paramMap;
    private TaskStatus status = null;
    Date checkThisCDate = new Date();
    Date lstintCr = null;
    private String dayEndType;
    private String taskLable;
    private String actBranch;
    private Date currDt;
    private List branchList;
    private boolean isError = false;
    private String runningProdId = "";
    private String runningActNum = "";
    private double totInterestAmt = 0.0;
    private ArrayList batchList = new ArrayList();
    private TransferTrans transferTransobj = new TransferTrans();
    private boolean isBatchExecuted = false;
    private String prodType = "";
    private ArrayList prodList = null;
    private Date curDate = null;
    private String db_Driver_name="";
    private String ROUNDING_NEAREST = "NEAREST_VALUE";
    private String NEAREST_TENS = "NEAREST_TENS";
    private String NEAREST_HUNDREDS = "NEAREST_HUNDREDS";
    private String interestRate = "";
    private HashMap interestsMap;
    private List interestList;
    private String user="";
    Date chekdateLast =null;
    Date ch_to_Date = null;
	 HashMap periodMap;
    /**
     * Creates a new instance of IntPayableTask
     */
    public InterestTask(TaskHeader header) throws Exception {
        System.out.println("header : " + header);
        setHeader(header);
        db_Driver_name = CommonUtil.convertObjToStr(header.getDB_DRIVER_NAME());
        branch = header.getBranchID();
        userID = header.getUserID();    //Added By Suresh
        curDate = ServerUtil.getCurrentDate(branch);
        initializeTaskObj(header.getTaskParam());
        prodType = getHeader().getProductType();
         periodMap=new HashMap();
    }

    //__ used by both the Constructors...
    private void initializeTaskObj(HashMap dataMap) throws Exception {
        System.out.println("@###" + dataMap);
        paramMap = dataMap;
        paramMap.put("TODAY_DT", ServerUtil.getCurrentDate(branch));
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        if (paramMap != null && paramMap.containsKey("DAY_END_TYPE")) {
            dayEndType = CommonUtil.convertObjToStr(paramMap.get("DAY_END_TYPE"));
        }
        currDt = ServerUtil.getCurrentDate(branch);
        if (paramMap != null && paramMap.containsKey("INT_TASK_LABLE")) {
            taskLable = CommonUtil.convertObjToStr(paramMap.get("INT_TASK_LABLE"));
        }
        if (CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
            if (paramMap.containsKey("BRANCH_LST")) {
                branchList = (List) paramMap.get("BRANCH_LST");
                System.out.println("branchList*****" + branchList);
            } else {
                branchList = null;
            }
        }
    }

    private HashMap getAccountHead(HashMap paramMap, HashMap resultMap) throws Exception {
        String accHead = null;
        removeExistingTransParams(resultMap);
        //IF Payable
        //Get Credit Int Ac Head from OP_AC_ACCOUNT_HD_PARAM
        //IF Receivable
        //Get Debit Int Ac Head from OP_AC_ACCOUNT_HD_PARAM
        if (getHeader().getTransactionType().equals(CommonConstants.PAYABLE)) {
            resultMap.put(TransferTrans.DR_AC_HD, (String) paramMap.get("CREDIT_INT"));
            resultMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            resultMap.put(TransferTrans.DR_BRANCH, actBranch);
            if (!resultMap.containsKey(TransferTrans.PARTICULARS)) {
                resultMap.put(TransferTrans.PARTICULARS, "Interest upto " + CommonUtil.convertObjToStr(resultMap.get("END")) + " For A/c No : " + paramMap.get("ACT_NUM"));
            }
            //            resultMap.put(TransferTrans.PARTICULARS, "Interest upto "+CommonUtil.convertObjToStr(lstintCr));
            resultMap.put(TransferTrans.CR_ACT_NUM, (String) paramMap.get("ACT_NUM"));
            resultMap.put(TransferTrans.CR_PROD_ID, (String) paramMap.get("PROD_ID"));
            resultMap.put(TransferTrans.CR_PROD_TYPE, prodType);//getHeader().getProductType());
            resultMap.put(TransferTrans.CR_BRANCH, actBranch);
            if (paramMap.containsKey("SCREEN_NAME") && paramMap.get("SCREEN_NAME") != null && CommonUtil.convertObjToStr(paramMap.get("SCREEN_NAME")).equalsIgnoreCase("Account Closing")) {
                resultMap.put("SCREEN_NAME", paramMap.get("SCREEN_NAME"));
                //Commented for KD-3392 [ Since the tag OA_INT_APP is not needed
//                if(prodType.equals("OA")){ //Added by nithya on 04-04-2022 for KD-3381
//                    resultMap.put(TransferTrans.DR_INSTRUMENT_2, "OA_INT_APP");  
//                }
            }
            resultMap.put(CommonConstants.USER_ID,CommonUtil.convertObjToStr(getHeader().getUserID()));//User id set by Kannan AR JIRA: KDSA-220
        }        
        return resultMap;
    }

    // Added by nithya for SB OD
    private HashMap getAccountHeadForSbOD(HashMap paramMap, HashMap resultMap) throws Exception {
          //resultMap.put(TransferTrans.DR_AC_HD, (String) paramMap.get("CREDIT_INT"));
        resultMap.put(TransferTrans.DR_ACT_NUM,(String) paramMap.get("ACT_NUM"));
        resultMap.put(TransferTrans.DR_PROD_ID, (String) paramMap.get("PROD_ID"));
        resultMap.put(TransferTrans.DR_PROD_TYPE, prodType);
        resultMap.put(TransferTrans.DR_BRANCH,actBranch);
        resultMap.put(TransferTrans.DR_AC_HD,paramMap.get(InterestTaskRunner.AC_HD_ID));// Added by nithya on 03-04-2018 for 9677 
        if (!resultMap.containsKey(TransferTrans.PARTICULARS)) {
                resultMap.put(TransferTrans.PARTICULARS, "Interest upto " + CommonUtil.convertObjToStr(resultMap.get("END")) + " For A/c No : " + paramMap.get("ACT_NUM"));
        }
        resultMap.put(TransferTrans.CR_AC_HD,(String) paramMap.get("DEBIT_INT"));
        resultMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.GL);
        resultMap.put(TransferTrans.CR_BRANCH,actBranch);
        
        return resultMap;
    }
    // End
    
    private HashMap getAccountHeadForReverseTrans(HashMap paramMap, HashMap resultMap) throws Exception {
        String accHead = null;
        removeExistingTransParams(resultMap);
        //IF Payable
        //Get Credit Int Ac Head from OP_AC_ACCOUNT_HD_PARAM
        //IF Receivable
        //Get Debit Int Ac Head from OP_AC_ACCOUNT_HD_PARAM
        if (getHeader().getTransactionType().equals(CommonConstants.PAYABLE)) {
            resultMap.put(TransferTrans.DR_ACT_NUM, (String) paramMap.get("ACT_NUM"));
            resultMap.put(TransferTrans.DR_PROD_ID, (String) paramMap.get("PROD_ID"));
            resultMap.put(TransferTrans.DR_PROD_TYPE, prodType);//getHeader().getProductType());
            resultMap.put(TransferTrans.DR_BRANCH, actBranch);
            resultMap.put(TransferTrans.CR_AC_HD, (String) paramMap.get("CREDIT_INT"));
            resultMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            resultMap.put(TransferTrans.CR_BRANCH, actBranch);
            if (!resultMap.containsKey(TransferTrans.PARTICULARS)) {
                resultMap.put(TransferTrans.PARTICULARS, "Interest upto " + CommonUtil.convertObjToStr(resultMap.get("END")));
            }
        }
        return resultMap;
    }

    private void removeExistingTransParams(HashMap resultMap) {
        resultMap.remove(TransferTrans.DR_ACT_NUM);
        resultMap.remove(TransferTrans.DR_PROD_ID);
        resultMap.remove(TransferTrans.DR_PROD_TYPE);
        resultMap.remove(TransferTrans.DR_BRANCH);
        resultMap.remove(TransferTrans.DR_AC_HD);
        //        resultMap.remove(TransferTrans.PARTICULARS);
        resultMap.remove(TransferTrans.CR_ACT_NUM);
        resultMap.remove(TransferTrans.CR_PROD_ID);
        resultMap.remove(TransferTrans.CR_PROD_TYPE);
        resultMap.remove(TransferTrans.CR_BRANCH);
        resultMap.remove(TransferTrans.CR_AC_HD);
    }

    private HashMap getAccountHeadForTax(HashMap paramMap, HashMap resultMap) throws Exception {
        String accHead = null;
        //IF Payable
        //Get Credit Int Ac Head from OP_AC_ACCOUNT_HD_PARAM
        //IF Receivable
        //Get Debit Int Ac Head from OP_AC_ACCOUNT_HD_PARAM
        if (getHeader().getTransactionType().equals(CommonConstants.PAYABLE)) {
            resultMap.put(TransferTrans.DR_PROD_TYPE, prodType);//getHeader().getProductType());
            resultMap.put(TransferTrans.DR_PROD_ID, (String) paramMap.get("PROD_ID"));
            resultMap.put(TransferTrans.DR_ACT_NUM, (String) paramMap.get("ACT_NUM"));
            resultMap.put(TransferTrans.DR_BRANCH, branch);
            resultMap.put(TransferTrans.PARTICULARS, "Tax deductions");
            resultMap.put(TransferTrans.CR_AC_HD, (String) resultMap.get("TAX_HEAD"));
            resultMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            resultMap.put(TransferTrans.CR_BRANCH, branch);
        }
        return resultMap;
    }

    private HashMap getInterestAmount(HashMap resultMap) throws Exception {
        double amount = 0.0;
        List amtLst = null;
        int lstSize = 0;
        int add_months = 0;
        Double minBal;

   //     String productId = CommonUtil.convertObjToStr(resultMap.get("PROD_ID"));
        //        if(resultMap.containsKey("TOT_ACT_SIZE") && resultMap.get("TOT_ACT_SIZE") !=null)
      //  HashMap periodMap = (HashMap) sqlMap.executeQueryForObject("getInterestCalcPeriod", productId);

        
        periodMap.put("PROD_ID", resultMap.get("PROD_ID"));
        periodMap.put("ACT_NUM", resultMap.get("ACT_NUM"));
        minBal = CommonUtil.convertObjToDouble(periodMap.get("MIN_BAL_FOR_INT_CALC"));
        periodMap.put("MIN_BAL_FOR_INT_CALC", minBal);
        //        HashMap createdDtMap = (HashMap)sqlMap.executeQueryForObject("Transfer.getActOpeningDate", resultMap.get("ACT_NUM"));
        //        Date createdDt;
        //        if (createdDtMap != null)
        //            if (createdDtMap.get("CREATEDATE")!=null)
        //                createdDt = (Date)createdDtMap.get("CREATEDATE");
        Date startDate = (Date) resultMap.get("START");
        Date endDate = lstintCr;
        if (periodMap.containsKey("PRODUCT_FREQ_INT_PAY") && CommonUtil.convertObjToInt(periodMap.get("PRODUCT_FREQ_INT_PAY")) == 1) {
            periodMap.put("START", DateUtil.addDays((Date) resultMap.get("START"), 1));
            periodMap.put("TODAY_DT", lstintCr);
            resultMap.put("DAILY_MAP", periodMap);
            //System.out.println("### @@@@@ result Map in getInterestAmount for Daily product = " + resultMap);
            return resultMap;
        }
        if (paramMap.containsKey("ADD_MONTHS")) {
            endDate = DateUtil.addDays(endDate, -30);
        }
        if (DateUtil.dateDiff(startDate, endDate) > 0) {
            periodMap.put("START", resultMap.get("START"));
            periodMap.put("TODAY_DT", lstintCr);
            if (paramMap.containsKey("ADD_MONTHS")) {
                add_months = CommonUtil.convertObjToInt(paramMap.get("ADD_MONTHS"));
            }
            periodMap.put("ADD_MONTHS", new Integer(add_months));
            //        System.out.println("super._branchCode : " + super._branchCode);
            //  System.out.println("periodMap in getInterestAmount = " + periodMap);
            periodMap.put(CommonConstants.DB_DRIVER_NAME,db_Driver_name);
            amtLst = sqlMap.executeQueryForList("getDailyBalance", periodMap);
            lstSize = amtLst.size();
            //System.out.println("Amount List:" + amtLst);
            if (amtLst != null && amtLst.size() > 0) {
                amount = CommonUtil.convertObjToDouble(amtLst.get(0)).doubleValue();
            }
      //      System.out.println("daily balance amoun t is..." + amount);
            //commented by rishad 20/08/2015
//            for (int i = 0; i < lstSize; i++) {
//                amount += CommonUtil.convertObjToDouble(amtLst.get(i)).doubleValue();
//            }
        }
        resultMap.put("AMOUNT", new Double(amount));
        //System.out.println("result Map in getInterestAmount = " + resultMap);
    //    periodMap = null;
        return resultMap;
    }

    // runBatchForSBOD nithya :: new method for SBOD interest processing
    private InterestBatchTO getSbOdCreditTransactDetails(HashMap resultMap, HashMap paramMap) throws Exception {
        InterestBatchTO objInterestBatchTO = null;
        objInterestBatchTO = new InterestBatchTO();
        objInterestBatchTO.setActNum(CommonUtil.convertObjToStr(resultMap.get("ACT_NUM"))); 
        objInterestBatchTO.setAcHdId(CommonUtil.convertObjToStr(paramMap.get("CREDIT_INT")));       
        objInterestBatchTO.setIntType(InterestCalculationConstants.SIMPLE_INTEREST);       
        objInterestBatchTO.setProductId(CommonUtil.convertObjToStr(paramMap.get("PRODUCT_ID")));
        objInterestBatchTO.setProductType(CommonUtil.convertObjToStr(paramMap.get("PRODUCT_TYPE")));
        objInterestBatchTO.setTransLogId(null);
        objInterestBatchTO.setCustId(CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
        objInterestBatchTO.setIsTdsApplied("N");
        objInterestBatchTO.setIntDt(getProperFormatDate(resultMap.get("START")));
        objInterestBatchTO.setApplDt(getProperFormatDate(resultMap.get("END") ));  //Changed TODAY_DT to END by nithya on 17-05-2018 for 0010266: SB/OD Interest Processing- The Table act param detail, last Cr int Date and last dr int date updating is wrong.     
        objInterestBatchTO.setBranch_code(CommonUtil.convertObjToStr(paramMap.get("BRANCH_CODE")));
        objInterestBatchTO.setUser_id(userID);
        return objInterestBatchTO;
    }
    
    private InterestBatchTO getSbOdDebitTransactDetails(HashMap resultMap, HashMap paramMap) throws Exception {
        InterestBatchTO objInterestBatchTO = null;
        objInterestBatchTO = new InterestBatchTO();
        objInterestBatchTO.setActNum(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));  // Changed to paramMap for 0009842: SB/OD Interest Processing- The Table act param detail, last Cr int Date and last dr int date not updating.  
        objInterestBatchTO.setAcHdId(CommonUtil.convertObjToStr(paramMap.get("DEBIT_INT")));
        objInterestBatchTO.setIntType(InterestCalculationConstants.SIMPLE_INTEREST);       
        objInterestBatchTO.setProductId(CommonUtil.convertObjToStr(paramMap.get("PRODUCT_ID")));
        objInterestBatchTO.setProductType(CommonUtil.convertObjToStr(paramMap.get("PRODUCT_TYPE")));
        objInterestBatchTO.setTransLogId(null);
        objInterestBatchTO.setCustId(CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
        objInterestBatchTO.setIsTdsApplied("N");
        objInterestBatchTO.setIntDt(getProperFormatDate(resultMap.get("START")));
        objInterestBatchTO.setApplDt(getProperFormatDate(resultMap.get("END") ));   //Changed TODAY_DT to END by nithya on 17-05-2018 for 0010266: SB/OD Interest Processing- The Table act param detail, last Cr int Date and last dr int date updating is wrong.       
        objInterestBatchTO.setBranch_code(CommonUtil.convertObjToStr(paramMap.get("BRANCH_CODE")));
        objInterestBatchTO.setUser_id(userID);
        return objInterestBatchTO;
    }
    
    private void runBatchForSBOD(HashMap resultMap, HashMap paramMap, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        // call the function for sbod which returns credit & debit interest        
        double sbODDebitInt = 0;
        double sbODCreditInt = 0;
        actBranch = CommonUtil.convertObjToStr(resultMap.get(CommonConstants.BRANCH_ID));
        InterestBatchTO objInterestBatchTO = null;
        HashMap headerParamMap = getHeader().getTaskParam();
//        System.out.println("headerParamMap :: " + headerParamMap +"\nparamMap :: " + paramMap +"\n resultMap :: " + resultMap);
//        System.out.println("SBOD set");
//        System.out.println("START DATE :: " + paramMap.get("DATE_FROM"));
//        System.out.println("END DATE :: " + resultMap.get("END"));
//        System.out.println(" ACT_NUM :: " + resultMap.get("ACT_NUM"));        
        HashMap SBODIntParamMap = new HashMap();
        SBODIntParamMap.put("ACT_NUM",resultMap.get("ACT_NUM"));
        SBODIntParamMap.put("START_DATE",paramMap.get("DATE_FROM"));
        SBODIntParamMap.put("END_DATE",resultMap.get("END"));
        List sbOdIntList = sqlMap.executeQueryForList("getSBODDailyInterest", SBODIntParamMap);
        if(sbOdIntList != null && sbOdIntList.size() > 0){
            HashMap sbOdIntMap = (HashMap)sbOdIntList.get(0);
            if(sbOdIntMap.containsKey("CREDITINT")){
                sbODCreditInt = CommonUtil.convertObjToDouble(sbOdIntMap.get("CREDITINT"));
            }
            if(sbOdIntMap.containsKey("DEBITINT")){
                sbODDebitInt = CommonUtil.convertObjToDouble(sbOdIntMap.get("DEBITINT")); 
            }
        }   
        //System.out.println("debit int :: " + sbODDebitInt +"\n" + "credit int :: " + sbODCreditInt);
        if (sbODCreditInt > 0) { // payable
            // trial insert
            objInterestBatchTO = getSbOdCreditTransactDetails(resultMap, paramMap);
            objInterestBatchTO.setIntAmt(sbODCreditInt);
            //System.out.println("inside runBatchForSBOD :: objInterestBatchTO :: " + objInterestBatchTO);
            //objInterestBatchTO = getSbOdCreditTransactDetails(getHeader(),resultMap);
            if (!(paramMap.containsKey("TRIAL") && paramMap.get("TRIAL").equals("Y"))) {
                transferTransobj = new TransferTrans();
                transferTransobj.setInitiatedBranch(CommonUtil.convertObjToStr(resultMap.get(CommonConstants.BRANCH_ID)));
                //if (batchList == null) {
                    batchList = new ArrayList();
                //}
                resultMap = prepareMap(paramMap, resultMap);
                //System.out.println("resultmap......" + resultMap);
                resultMap.put("DR_INSTRUMENT_1","SB INTEREST");
                resultMap.put("LINK_BATCH_ID",objInterestBatchTO.getActNum()); // 17-06-2019 for KD 533
               resultMap.put("TRANS_MOD_TYPE",TransactionFactory.GL);// Added by nithya on 26-03-2018 for 0009579: SB Account Closure --> The Interest transfer Part TRans_mod_type is null. So Print not getting
                batchList.add(transferTransobj.getDebitTransferTO(resultMap, sbODCreditInt));
                resultMap.put("TRANS_MOD_TYPE",TransactionFactory.OPERATIVE);// Added by nithya on 26-03-2018 for 0009579: SB Account Closure --> The Interest transfer Part TRans_mod_type is null. So Print not getting
                batchList.add(transferTransobj.getCreditTransferTO(resultMap, sbODCreditInt));
                //System.out.println("inside runBatchForSBOD :: batchList :: " + batchList + "\n transferTransobj :: " + transferTransobj);
                if (batchList != null && batchList.size() > 0) {
                    transferTransobj.doDebitCredit(batchList, CommonUtil.convertObjToStr(resultMap.get(CommonConstants.BRANCH_ID)), true);
                }  
                sqlMap.executeUpdate("insertInterestBatchTO", objInterestBatchTO);
            }
            if((paramMap.containsKey("TRIAL") && paramMap.get("TRIAL").equals("Y"))){
                //System.out.println("SB OD trial credit process");
                HashMap finMap = new HashMap();
                finMap.put("ACCOUNT_NO", paramMap.get("ACT_NUM"));
                finMap.put("TO_DATE", getProperFormatDate(paramMap.get("TODAY_DT")));
                sqlMap.executeUpdate("deleteAccountIntTrialTO", finMap);
                objInterestBatchTO.setBranch_code(CommonUtil.convertObjToStr(paramMap.get("BRANCH_CODE")));
                objInterestBatchTO.setUser_id(userID);
                // for avoiding Nullpointer Exception 
                objInterestBatchTO.setIntPostingPossible("Y");// Added by nithya on 19-11-2019 for KD-729
                sqlMap.executeUpdate("insertInterestTrialBatchTO", objInterestBatchTO);
            }
        }
        if (sbODDebitInt > 0) { // receievable debit
            // trial insert
            //System.out.println("inside runBatchForSBOD sbODDebitInt :: " + sbODDebitInt);           
            objInterestBatchTO = getSbOdDebitTransactDetails(resultMap, paramMap);
            objInterestBatchTO.setIntAmt(sbODDebitInt);
            //System.out.println("inside runBatchForSBOD :: objInterestBatchTO :: " + objInterestBatchTO);
            //objInterestBatchTO = getSbOdCreditTransactDetails(getHeader(),resultMap);
            if (!(paramMap.containsKey("TRIAL") && paramMap.get("TRIAL").equals("Y"))) {
                transferTransobj = new TransferTrans();
                transferTransobj.setInitiatedBranch(CommonUtil.convertObjToStr(resultMap.get(CommonConstants.BRANCH_ID)));
                //System.out.println("batchList before :: " + batchList);
                //if (batchList == null) {
                    batchList = new ArrayList();
                //}
                resultMap = getAccountHeadForSbOD(paramMap, resultMap);
                resultMap.put(TransferTrans.CURRENCY, LocaleConstants.DEFAULT_CURRENCY);
                //resultMap.remove("ACT_NUM");
                //resultMap.remove("PROD_ID");
                //System.out.println("resultmap......" + resultMap);
                resultMap.put("DR_INSTRUMENT_1","OD INTEREST");
                resultMap.put("LINK_BATCH_ID",objInterestBatchTO.getActNum());// 17-06-2019 for KD 533
                resultMap.put("TRANS_MOD_TYPE",TransactionFactory.OPERATIVE);// Added by nithya on 26-03-2018 for 0009579: SB Account Closure --> The Interest transfer Part TRans_mod_type is null. So Print not getting
                batchList.add(transferTransobj.getDebitTransferTO(resultMap, sbODDebitInt));
                resultMap.put("TRANS_MOD_TYPE",TransactionFactory.GL);// Added by nithya on 26-03-2018 for 0009579: SB Account Closure --> The Interest transfer Part TRans_mod_type is null. So Print not getting
                batchList.add(transferTransobj.getCreditTransferTO(resultMap, sbODDebitInt));
                //System.out.println("inside runBatchForSBOD :: batchList :: " + batchList + "\n transferTransobj :: " + transferTransobj);
                if (batchList != null && batchList.size() > 0) {
                    transferTransobj.doDebitCredit(batchList, CommonUtil.convertObjToStr(resultMap.get(CommonConstants.BRANCH_ID)), true);
                } 
                sqlMap.executeUpdate("insertInterestBatchTO", objInterestBatchTO);
            }
            if((paramMap.containsKey("TRIAL") && paramMap.get("TRIAL").equals("Y"))){
                //System.out.println("SB OD trial debit process");
                HashMap finMap = new HashMap();
                finMap.put("ACCOUNT_NO", paramMap.get("ACT_NUM"));
                finMap.put("TO_DATE", getProperFormatDate(paramMap.get("TODAY_DT")));
                sqlMap.executeUpdate("deleteAccountIntTrialTO", finMap);
                objInterestBatchTO.setBranch_code(CommonUtil.convertObjToStr(paramMap.get("BRANCH_CODE")));
                objInterestBatchTO.setUser_id(userID);
                // for avoiding Nullpointer Exception 
                objInterestBatchTO.setIntPostingPossible("Y");// Added by nithya on 19-11-2019 for KD-729
                sqlMap.executeUpdate("insertInterestTrialBatchTO", objInterestBatchTO);
            }
            
        }
        
        if (!(paramMap.containsKey("TRIAL") && paramMap.get("TRIAL").equals("Y"))) { 
            if(objInterestBatchTO != null){               
             sqlMap.executeUpdate("updateAcctPaymentApplDtAndTemp", objInterestBatchTO); 
             sqlMap.executeUpdate("updateAcctLastReceiptDt", objInterestBatchTO); 
            } 
        }
        //return objInterestBatchTO;
    }
    
    // End    
    
    //__ Method to Calculate the Interest...
    private InterestBatchTO runBatch(HashMap resultMap, HashMap paramMap, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        InterestBatchTO objInterestBatchTO = null;
        InterestTaskRunner interestTaskRun = new InterestTaskRunner();
        TransferTrans transferTrans = new TransferTrans();
       // System.out.println("Old REsulsMap: " + resultMap);
        actBranch = CommonUtil.convertObjToStr(resultMap.get(CommonConstants.BRANCH_ID));
        String valueDateMode = "NORMAL";
        if (resultMap.containsKey("DAILY_MAP")) {
            resultMap.remove("DAILY_MAP");
        }
        if (!resultMap.containsKey("VALUE_DT_MODE")) {
            resultMap = getInterestAmount(resultMap);
        } else if (resultMap.containsKey("VALUE_DT_MODE")) {
            valueDateMode = CommonUtil.convertObjToStr(resultMap.get("VALUE_DT_MODE"));
            double prodAmount = CommonUtil.convertObjToDouble(resultMap.get("PROD_AMOUNT")).doubleValue();
            if (valueDateMode.equals("REVERSAL")) {
                if (prodAmount > 0) {
                    valueDateMode = "REVERSAL_DEBIT";
                } else {
                    valueDateMode = "TRANS_CREDIT";
                    prodAmount = -1 * prodAmount;
                }
            }
            resultMap.put("AMOUNT", new Double(prodAmount));
        } else {
            return null;
        }
        System.out.println("New REsulsMap: " + resultMap+"valueDateMode: " + valueDateMode+"@@@@@@ paramMap: " + paramMap);
        //        objInterestBatchTO.setBranchCode(CommonUtil.convertObjToStr(resultMap.get("BRANCH_ID")));
        if ((resultMap.containsKey("AMOUNT")) || resultMap.containsKey("DAILY_MAP")) {
            if (!resultMap.containsKey("DAILY_MAP")) {
                objInterestBatchTO = interestTaskRun.interestAmount(getHeader(), resultMap);                
            } else if (resultMap.containsKey("DAILY_MAP")) {
                //KD-3439
                if(paramMap.containsKey("SCREEN_NAME") && paramMap.get("SCREEN_NAME") != null){
                    resultMap.put("FROM_SCREEN", paramMap.get("SCREEN_NAME"));
                }
                objInterestBatchTO = getInterestForOperativeDailyProductType(resultMap);
                //                return objInterestBatchTO;
            }
            objInterestBatchTO.setIntDt((Date) resultMap.get("START"));
            objInterestBatchTO.setApplDt((Date) resultMap.get("END"));
            //            objInterestBatchTO.setApplDt(lstintCr);
            if (objInterestBatchTO.getIntAmt().doubleValue() < CommonUtil.convertObjToDouble(paramMap.get("MIN_CR_INT_AMT")).doubleValue()) {
                objInterestBatchTO.setIntAmt(new Double(0));
            }
            //System.out.println("objInterestBatchTO: " + objInterestBatchTO);
            double interestAmt = CommonUtil.convertObjToDouble(objInterestBatchTO.getIntAmt());
            //debit interest recived  account credit multiple operative account in SB interest calculation time (single debit voucher)
            totInterestAmt += interestAmt;
            //System.out.println("interestAmt@@@@@" + interestAmt);
            //__ In Case of BatchProcess, DO the following...
            if ((taskSelected == BATCHPROCESS || taskSelected == INSERTDATA)) {//&& interestAmt > 0 ) {
                /**
                 * To Enter the Data in the Transaction Table...
                 */
                if (batchList == null) {
                    batchList = new ArrayList();
                }
                ArrayList taxList = new ArrayList();
                transferTransobj = new TransferTrans();
                HashMap tax = new HashMap();
                HashMap taxMap = new HashMap();
                tax = (HashMap) resultMap.clone();
                taxMap = (HashMap) resultMap.clone();
                //System.out.println("taxMap@@@@: " + tax);
                if (valueDateMode.equals("NORMAL") || valueDateMode.equals("TRANS_CREDIT")) {
                    if (valueDateMode.equals("TRANS_CREDIT")) {
                        resultMap.put(TransferTrans.PARTICULARS, "Transaction for Short Credited Interest ("
                                + CommonUtil.convertObjToStr(resultMap.get("START")) + " - "
                                + CommonUtil.convertObjToStr(resultMap.get("END")) + ")");
                    }
                    resultMap = prepareMap(paramMap, resultMap);
                } else if (valueDateMode.equals("REVERSAL_DEBIT")) {
                    resultMap.put(TransferTrans.PARTICULARS, "Reversal for Excess Credited Interest ("
                            + CommonUtil.convertObjToStr(resultMap.get("START")) + " - "
                            + CommonUtil.convertObjToStr(resultMap.get("END")) + ")");
                    resultMap = prepareMapForReverseTrans(paramMap, resultMap);
                    objInterestBatchTO.setIntAmt(new Double(-1 * objInterestBatchTO.getIntAmt().doubleValue()));
                    objInterestBatchTO.setPrincipleAmt(new Double(-1 * objInterestBatchTO.getPrincipleAmt().doubleValue()));
                }
                transferTransobj.setInitiatedBranch(actBranch);
                //commented by rishad 04/2016(mm/yyyy) last transaction linkbatch id updating  for whole transaction
                //transferTransobj.setLinkBatchId(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
               // resultMap.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                if (!(paramMap.containsKey("TRIAL") && paramMap.get("TRIAL").equals("Y"))) {
                    if (!resultMap.containsKey("TOT_ACT_SIZE") && (interestAmt > 0 || totInterestAmt > 0)) {
                        if (taskSelected == BATCHPROCESS && totInterestAmt > 0) {
                            if(resultMap.get("DR_PROD_TYPE").equals(TransactionFactory.GL)){
                                resultMap.put("TRANS_MOD_TYPE",TransactionFactory.GL);
                            }
                            if(resultMap.containsKey("generateSingleTransId")){
                                resultMap.put("generateSingleTransId", resultMap.get("generateSingleTransId"));
                            }
                            batchList.add(transferTransobj.getDebitTransferTO(resultMap, totInterestAmt));
                        } else if (interestAmt > 0) {
                        if(resultMap.get("DR_PROD_TYPE").equals(TransactionFactory.GL)){
                            resultMap.put("TRANS_MOD_TYPE",TransactionFactory.GL);
                        }
                        if(resultMap.containsKey("generateSingleTransId")){
                        	resultMap.put("generateSingleTransId", resultMap.get("generateSingleTransId"));
                        }                          
                        
                       //Added for KD-3392 
                       if(resultMap.containsKey("SCREEN_NAME") && resultMap.get("SCREEN_NAME") != null && CommonUtil.convertObjToStr(resultMap.get("SCREEN_NAME")).equalsIgnoreCase("Account Closing")){
                           resultMap.put("LINK_BATCH_ID",objInterestBatchTO.getActNum());
                        }
                        
                            batchList.add(transferTransobj.getDebitTransferTO(resultMap, interestAmt));
                        }
                    }
                }
		if (!(paramMap.containsKey("TRIAL") && paramMap.get("TRIAL").equals("Y"))) {
               // transferTransobj.setLinkBatchId(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
             //   resultMap.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                if (interestAmt > 0) {
                		if(resultMap.get("CR_PROD_TYPE").equals(TransactionFactory.OPERATIVE))
                    {
                         resultMap.put("TRANS_MOD_TYPE",TransactionFactory.OPERATIVE);
                    }
                    if(resultMap.containsKey("generateSingleTransId")){
                                resultMap.put("generateSingleTransId", resultMap.get("generateSingleTransId"));
                    }                   
                    if(resultMap.containsKey("SCREEN_NAME") && resultMap.get("SCREEN_NAME") != null && CommonUtil.convertObjToStr(resultMap.get("SCREEN_NAME")).equalsIgnoreCase("Account Closing")){
                        resultMap.put("LINK_BATCH_ID",objInterestBatchTO.getActNum());
                        resultMap.put("SCREEN_NAME", resultMap.get("SCREEN_NAME"));
                    }
                    batchList.add(transferTransobj.getCreditTransferTO(resultMap, interestAmt));
                }
                //            if(taskSelected == BATCHPROCESS)
     //  System.out.println("totInterestAmt#####" + totInterestAmt + "final resultMap##### " + resultMap);
                if (!(resultMap.containsKey("TOT_ACT_SIZE") && resultMap.get("TOT_ACT_SIZE") != null)) {
                 // System.out.println("batchList#####" + batchList);
                    if (batchList != null && batchList.size() > 0) {
                        transferTransobj.doDebitCredit(batchList, actBranch, true);
                    }
                 //   System.out.println("transaction part executed " + paramMap);
                }
                //            else
                //                transferTrans.doDebitCredit(batchList, branch, false) ;
                String taxAplicable = CommonUtil.convertObjToStr(paramMap.get("TAX_APPLICABLE"));
                if (taxAplicable.equals("Y") && !valueDateMode.equals("REVERSAL_DEBIT")) {
                    tax.put("TAX_HEAD", paramMap.get("TAX_HEAD"));
                    //System.out.println("taxMap@@@@insideif: " + tax);
                    double intAmt = CommonUtil.convertObjToDouble(paramMap.get("NRO_TAX_AMT")).doubleValue();
                    if (intAmt > 0 && interestAmt > 0) {
                        Rounding rd = new Rounding();
                        double taxAmts = (interestAmt * intAmt) / 100;
                        long taxAmt = (long) (taxAmts * 100);
                        taxAmt = rd.getNearest(taxAmt, 100) / 100;
                        //System.out.println("###taxAmt: " + taxAmt);
                        if (taxAmt > 0) {
                            tax = taxMap(paramMap, tax);
                            //System.out.println("###taxMapAfterTaxMAp: " + tax);
                            transferTrans.setInitiatedBranch(branch);
                            transferTrans.setLinkBatchId(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                            taxList.add(transferTrans.getDebitTransferTO(tax, taxAmt));
                            taxList.add(transferTrans.getCreditTransferTO(tax, taxAmt));
                            //System.out.println("###taxList: " + taxList);
                            transferTrans.doDebitCredit(taxList, branch, true);
                            taxMap.put("APPL_DT", lstintCr);
                            taxMap.put("INT_AMT", String.valueOf(interestAmt));
                            taxMap.put("INT_RATE", objInterestBatchTO.getIntRate());
                            taxMap.put("TAX_AMT", String.valueOf(taxAmt));
                            taxMap.put("TAX_APPLICABLE", String.valueOf(intAmt));
                            taxMap.put("BRANCH_CODE", branch);
                            //System.out.println("insertNROTaxAmt:taxMap " + taxMap);
                            sqlMap.executeUpdate("insertNROTaxAmt", taxMap);
                        }
                    }
                }
                //__ To insert the data(Act interest) in the ACT_INTEREST...
                if (!resultMap.containsKey("DAILY_MAP") && !(paramMap.containsKey("TRIAL") && paramMap.get("TRIAL").equals("Y"))) {
                    sqlMap.executeUpdate("insertInterestBatchTO", objInterestBatchTO);
                }
                //commented by rishad 14/08/2015 for avoiding unwanted exception and time consuming
//                objLogTO.setData(objInterestBatchTO.toString());
//                objLogTO.setPrimaryKey(objInterestBatchTO.getKeyData());
//                objLogDAO.addToLog(objLogTO);
            }
             //   System.out.println("interestAmt runBatch : "+interestAmt);
            //Added By Suresh       //Transaction Or Trial Both time Interest Details are insert into Trial Table.
            if ((paramMap.containsKey("TRIAL") && paramMap.get("TRIAL").equals("Y"))&&(!resultMap.containsKey("DAILY_MAP")) && interestAmt > 0 ) {
                //Added By Suresh  Based On Product Level Min_Int_Amount to be Check.
                HashMap finMap = new HashMap();
                finMap.put("ACCOUNT_NO", paramMap.get("ACT_NUM"));
                finMap.put("TO_DATE", getProperFormatDate(paramMap.get("TODAY_DT")));
                sqlMap.executeUpdate("deleteAccountIntTrialTO", finMap);
                objInterestBatchTO.setBranch_code(CommonUtil.convertObjToStr(paramMap.get("BRANCH_CODE")));
                objInterestBatchTO.setUser_id(userID);
                 // for avoiding Nullpointer Exception 
                objInterestBatchTO.setIntPostingPossible("Y");// Added by nithya on 19-11-2019 for KD-729
                sqlMap.executeUpdate("insertInterestTrialBatchTO", objInterestBatchTO);
            }
            resultMap.remove(TransferTrans.PARTICULARS);
            if (!(paramMap.containsKey("TRIAL") && paramMap.get("TRIAL").equals("Y"))) {
                if ((taskSelected == BATCHPROCESS || taskSelected == INSERTDATA) && valueDateMode.equals("NORMAL")) {
//                    
//                    sqlMap.executeUpdate("updateAcctPaymentTEMPApplDt", objInterestBatchTO);
//                    sqlMap.executeUpdate("updateAcctPaymentApplDt", objInterestBatchTO);
                     sqlMap.executeUpdate("updateAcctPaymentApplDtAndTemp", objInterestBatchTO);
                }
            }
        } else {
            if (CommonUtil.convertObjToDouble(resultMap.get("AMOUNT")).doubleValue() == 0) {
                if (taskSelected == BATCHPROCESS && getHeader().getTransactionType().equalsIgnoreCase(CommonConstants.PAYABLE)) {
                    if (!(resultMap.containsKey("TOT_ACT_SIZE") && resultMap.get("TOT_ACT_SIZE") != null)) {
                        resultMap = prepareMap(paramMap, resultMap);
                        transferTransobj.setInitiatedBranch(actBranch);
                      //  System.out.println(totInterestAmt + "resultMap   #####" + resultMap);
                        if (totInterestAmt > 0.0) {
                            batchList.add(transferTransobj.getDebitTransferTO(resultMap, totInterestAmt));
                            //System.out.println("batchList   #####" + batchList);
                            isBatchExecuted = false;
                            transferTransobj.doDebitCredit(batchList, actBranch, true);
                        }
                    }
                }
            }
        }
        }
        if (taskSelected == OBCODE && valueDateMode.equals("REVERSAL_DEBIT")) {
            objInterestBatchTO.setIntAmt(new Double(-1 * objInterestBatchTO.getIntAmt().doubleValue()));
        }
        return objInterestBatchTO;
    }

    // For Daily Product : To calculate the Interest and put the data in the InterestBatchTO Object...
    public InterestBatchTO getInterestForOperativeDailyProductType(HashMap resultMap) throws Exception {
     //   System.out.println("Resultmap in setRateOfInterest : " + resultMap);
        HashMap map = new HashMap();
        map.put(InterestTaskRunner.PRODUCT_ID, resultMap.get(InterestTaskRunner.PRODUCT_ID));
        map.put(InterestTaskRunner.PRODUCT_TYPE, prodType);// getHeader().getProductType());
        map.put(InterestTaskRunner.CATEGORY_ID, resultMap.get(InterestTaskRunner.CATEGORY_ID));
        map.put(InterestTaskRunner.PRINCIPLE_AMT, CommonUtil.convertObjToDouble("1"));

        HashMap dailyMap = (HashMap) resultMap.get("DAILY_MAP");
        map.put("START", dailyMap.get("START"));
        map.put("TODAY_DT", DateUtil.addDays((Date) dailyMap.get("TODAY_DT"), 1));
        map.put("ACT_NUM", dailyMap.get("ACT_NUM"));
        Double roi = new Double(0);
        if (resultMap.containsKey("INT_TYPE")) {
            map.put("INT_TYPE", resultMap.get("INT_TYPE"));
        }
        //        System.out.println("#$#$# map  : " + map );
        List dateList = (List) sqlMap.executeQueryForList("getDatesForOperativeInterest", map);
        //        System.out.println("##### getInterestForOperativeDailyProductType dateList :"+dateList);
        Double prodAmt = new Double(0);
        HashMap dataMap = null;
        double intAmt = 0;
        double totalIntAmt = 0;
        Date start = (Date) dailyMap.get("START");
        Date transDt = null;
        InterestBatchTO objInterestBatchTO = null;
        if (dateList != null && dateList.size() > 0) {
            for (int i = 0; i < dateList.size(); i++) {
                transDt = (Date) dateList.get(i);
                //                map.put("START_DT", transDt);
                //                System.out.println("##### transDt:"+transDt);
                map.put(InterestTaskRunner.DEPOSIT_DT, start);
                // To get the interest
                dataMap = (HashMap) sqlMap.executeQueryForObject("icm.getInterestRates", map);
                //                System.out.println("##### setRateOfInterst dataMap :"+dataMap);
                if (dataMap != null) {
                    roi = CommonUtil.convertObjToDouble(((HashMap) dataMap).get(InterestTaskRunner.ROI));
                    if (resultMap.containsValue(InterestTaskRunner.PENAL_INTEREST)) {
                        roi = CommonUtil.convertObjToDouble(((HashMap) dataMap).get(InterestTaskRunner.PENAL_INTEREST));
                    }
                    if (resultMap.containsValue(InterestTaskRunner.ADDL_INTEREST)) {
                        roi = CommonUtil.convertObjToDouble(((HashMap) dataMap).get(InterestTaskRunner.ADDITIONAL_INTEREST));
                    }
                }
                //                System.out.println("#####setRateOfInterst ROI"+roi);
                dataMap = (HashMap) resultMap.get("DAILY_MAP");
                map.put("ROI", roi);
                map.put("TODAY_DT", DateUtil.addDays(transDt, -1));
                //                System.out.println("##### map:"+map);
                map.put(CommonConstants.DB_DRIVER_NAME,db_Driver_name);
                dataMap = (HashMap) sqlMap.executeQueryForObject("getInterestAmountForDailyProduct", map);
                //                System.out.println("#$$# DateUtil.dateDiff(start, transDt):"+DateUtil.dateDiff(start, transDt));
                //                intAmt = prodAmt.doubleValue()*(roi.doubleValue()/100) * DateUtil.dateDiff(start, transDt)/365;
                if (dataMap != null && dataMap.size() > 0) {
                    intAmt = CommonUtil.convertObjToDouble(dataMap.get("INTEREST_AMT")).doubleValue();
                }
                //System.out.println("############## resultMap : " + resultMap);
                //System.out.println("########### Actual Closing Int Payable Amount : " + intAmt);
                //Added By Suresh
                if (resultMap.containsKey("INT_PAYABLE_AMOUNT") && (CommonUtil.convertObjToDouble(resultMap.get("INT_PAYABLE_AMOUNT")).doubleValue() > 0)) {
                    intAmt = CommonUtil.convertObjToDouble(resultMap.get("INT_PAYABLE_AMOUNT")).doubleValue();
                //    System.out.println("########### Changed Closing Int Payable Amount : " + intAmt);
                }
                totalIntAmt += intAmt;
                
                //KD-3439
                if (resultMap.containsKey("FROM_SCREEN") && resultMap.get("FROM_SCREEN") != null && resultMap.get("FROM_SCREEN").equals("Account Closing")) {
                    if (resultMap.containsKey("INT_PAYABLE_AMOUNT") && resultMap.get("INT_PAYABLE_AMOUNT") != null && (CommonUtil.convertObjToDouble(resultMap.get("INT_PAYABLE_AMOUNT")).doubleValue() > 0)) {
                        totalIntAmt = CommonUtil.convertObjToDouble(resultMap.get("INT_PAYABLE_AMOUNT")).doubleValue();
                        System.out.println("########### totalIntAmt checking for SB Closing : " + totalIntAmt);
                    }
                }

               // System.out.println("totalIntAmt : " + totalIntAmt + " intAmt : "+intAmt);
                //                System.out.println("#$#$ Interest calculated map for Daily Product type : " + dataMap);

                objInterestBatchTO = new InterestBatchTO();
                objInterestBatchTO.setActNum(CommonUtil.convertObjToStr(resultMap.get("ACT_NUM")));
                if (getHeader().getTransactionType().equals(CommonConstants.PAYABLE)) {
                    objInterestBatchTO.setAcHdId(CommonUtil.convertObjToStr(paramMap.get("DEBIT_INT")));
                } else {
                    objInterestBatchTO.setAcHdId(CommonUtil.convertObjToStr(paramMap.get("CREDIT_INT")));
                }
             //   System.out.println("objInterestBatchTO 1 : " + objInterestBatchTO + " intAmt : "+intAmt);
                objInterestBatchTO.setIntRate(roi);
                objInterestBatchTO.setIntType(InterestCalculationConstants.SIMPLE_INTEREST);
                objInterestBatchTO.setPrincipleAmt(CommonUtil.convertObjToDouble(dataMap.get("PRINCIPAL_AMT")));
                objInterestBatchTO.setProductId(CommonUtil.convertObjToStr(paramMap.get("PRODUCT_ID")));
                objInterestBatchTO.setProductType(CommonUtil.convertObjToStr(paramMap.get("PRODUCT_TYPE")));
                objInterestBatchTO.setTransLogId(null);
            //    System.out.println("objInterestBatchTO 2 : " + objInterestBatchTO + " intAmt : "+intAmt);
                objInterestBatchTO.setCustId(CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
                objInterestBatchTO.setIsTdsApplied("N");
                objInterestBatchTO.setIntDt(getProperFormatDate(map.get("START")));
                objInterestBatchTO.setApplDt(getProperFormatDate(map.get("TODAY_DT")));
              //  System.out.println("objInterestBatchTO 3 : " + objInterestBatchTO + " intAmt : "+intAmt);
                if (intAmt > 0) {
                    objInterestBatchTO.setIntAmt(new Double(intAmt));
                } else {
                    objInterestBatchTO.setIntAmt(new Double(0));
                }
                //Added By Suresh       //Transaction Or Trial Both time Interest Details are insert into Trial Table.
                if ((paramMap.containsKey("TRIAL") && paramMap.get("TRIAL").equals("Y"))&&intAmt > 0 && intAmt >= CommonUtil.convertObjToDouble(paramMap.get("MIN_CR_INT_AMT")).doubleValue()) {
                    //Added By Suresh  Based On Product Level Min_Int_Amount to be Check.
                    HashMap finMap = new HashMap();
                    finMap.put("ACCOUNT_NO", paramMap.get("ACT_NUM"));
                    finMap.put("TO_DATE", getProperFormatDate(map.get("TODAY_DT")));
                    sqlMap.executeUpdate("deleteAccountIntTrialTO", finMap);
                    objInterestBatchTO.setBranch_code(CommonUtil.convertObjToStr(paramMap.get("BRANCH_CODE")));
                    objInterestBatchTO.setUser_id(userID);
                   // for avoiding Nullpointer Exception 
                    objInterestBatchTO.setIntPostingPossible("Y");// Added by nithya on 19-11-2019 for KD-729
                    sqlMap.executeUpdate("insertInterestTrialBatchTO", objInterestBatchTO);
                }
                if (!(paramMap.containsKey("TRIAL") && paramMap.get("TRIAL").equals("Y"))) {
                   sqlMap.executeUpdate("insertInterestBatchTO", objInterestBatchTO); 
                }
                /*if (paramMap.containsKey("TRIAL") && paramMap.get("TRIAL").equals("Y") ) {//Added by kannan refer rajesh sir
                    if (intAmt > 0 && intAmt>=CommonUtil.convertObjToDouble(paramMap.get("MIN_CR_INT_AMT")).doubleValue()) {
                        //Added By Suresh  Based On Product Level Min_Int_Amount to be Check.
                        HashMap finMap = new HashMap();
                        finMap.put("ACCOUNT_NO", paramMap.get("ACT_NUM"));
                        sqlMap.executeUpdate("deleteAccountIntTrialTO", finMap);
                        objInterestBatchTO.setBranch_code(CommonUtil.convertObjToStr(paramMap.get("BRANCH_CODE")));
                        objInterestBatchTO.setUser_id(CommonUtil.convertObjToStr(paramMap.get("USER_ID")));
                        sqlMap.executeUpdate("insertInterestTrialBatchTO", objInterestBatchTO);
                    }
                } else {
                    sqlMap.executeUpdate("insertInterestBatchTO", objInterestBatchTO);
                }*/
                start = (Date) currDt.clone();
                start.setDate(transDt.getDate());
                start.setMonth(transDt.getMonth());
                start.setYear(transDt.getYear());
                map.put("START", start);
                //                System.out.println("##### start:"+start);
            }
        }

        //System.out.println("#$#$ totalIntAmt " + totalIntAmt);
        objInterestBatchTO.setIntAmt(new Double(totalIntAmt));
        return objInterestBatchTO;
    }

    private HashMap prepareMap(HashMap paramMap, HashMap resultMap) throws Exception {
        resultMap = getAccountHead(paramMap, resultMap);
        resultMap.put(TransferTrans.CURRENCY, LocaleConstants.DEFAULT_CURRENCY);

        //Remove keys which were over written with TransferTrans's keys
        resultMap.remove("ACT_NUM");
        resultMap.remove("PROD_ID");

        return resultMap;
    }

    private HashMap prepareMapForReverseTrans(HashMap paramMap, HashMap resultMap) throws Exception {
        resultMap = getAccountHeadForReverseTrans(paramMap, resultMap);
        resultMap.put(TransferTrans.CURRENCY, LocaleConstants.DEFAULT_CURRENCY);

        //Remove keys which were over written with TransferTrans's keys
        resultMap.remove("ACT_NUM");
        resultMap.remove("PROD_ID");

        return resultMap;
    }

    private HashMap taxMap(HashMap paramMap, HashMap resultMap) throws Exception {
        resultMap = getAccountHeadForTax(paramMap, resultMap);
        //System.out.println("resultMapInsideTAxMAp" + resultMap);
        resultMap.put(TransferTrans.CURRENCY, LocaleConstants.DEFAULT_CURRENCY);

        //Remove keys which were over written with TransferTrans's keys
        resultMap.remove("ACT_NUM");
        resultMap.remove("PROD_ID");

        return resultMap;
    }
    //__ To get the Data regarding the product...

    private ArrayList getProductList(HashMap nonBatch) throws Exception {
        ArrayList productList = new ArrayList();
        if (prodType.equals("AD")) {
            if (nonBatch.containsKey(InterestTaskRunner.PRODUCT_ID)) {
                productList = (ArrayList) sqlMap.executeQueryForList("icm.getProductsAD", nonBatch);
            } else {
                productList = (ArrayList) sqlMap.executeQueryForList("icm.getProductsAD", null);
            }
        } else {
            if (nonBatch.containsKey(InterestTaskRunner.PRODUCT_ID)) {
                productList = (ArrayList) sqlMap.executeQueryForList("icm.getProducts", nonBatch);
            } else {
                productList = (ArrayList) sqlMap.executeQueryForList("icm.getProducts", null);
            }
        }
        return productList;
    }

    //__ To get the Data regarding the Account Number...
    private ArrayList getAccountList(HashMap nonBatch) throws Exception {
        ArrayList accountList = new ArrayList();

        //To get FromActNo & ToActNo if present in paramMap
        if (paramMap.containsKey("ACT_FROM")) {
            nonBatch.put("ACT_FROM", paramMap.get("ACT_FROM"));
        }
        if (paramMap.containsKey("ACT_TO")) {
            nonBatch.put("ACT_TO", paramMap.get("ACT_TO"));
        }
        //        if (!CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL"))
        nonBatch.put(CommonConstants.BRANCH_ID, branch);
        //        else
        nonBatch.put("NEXT_DATE",(Date) currDt.clone());
        Date dt=(Date) currDt.clone();
        //          if (!CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL"))
        //            nonBatch.put(CommonConstants.BRANCH_ID, branch);
        //        else
        //            nonBatch.put("NEXT_DATE", currDt);
      //  System.out.println("non Batch = " + nonBatch);

        if (prodType.equals("AD")) {
            accountList = (ArrayList) sqlMap.executeQueryForList("icm.getAccountsAD", nonBatch);
        } else {
            //For each product, get the account no
            // If running for a single Ac/No, take Ac/No from Map
            if (nonBatch.containsKey(CommonConstants.ACT_NUM)) {
                accountList.add(nonBatch);
            } else {
                accountList = (ArrayList) sqlMap.executeQueryForList("icm.getAccounts", nonBatch);
            }
        }
        return accountList;
    }

    //__ Common Method Call...
    private HashMap implementTask(HashMap nonBatch, boolean isExceptionCatch) throws Exception {
        InterestBatchTO objInterestBatchTO = null;
        HashMap interestMap = new HashMap();
        ArrayList accountList = new ArrayList();
        batchList = new ArrayList();
     //  System.out.println("!!!!! Inside implementTask...");
        // Log DAO
        LogDAO objLogDAO = new LogDAO();
        totInterestAmt = 0;
        isBatchExecuted = true;
        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId(getHeader().getUserID());
        objLogTO.setBranchId(getHeader().getBranchID());
        objLogTO.setIpAddr(getHeader().getIpAddr());
        objLogTO.setModule(MODULE);
        objLogTO.setScreen(SCREEN);
        //System.out.println("!!!!! Logged...");
        //__ Get All the Acount Nos...
     //   System.out.println("!!!!! nonBatch : " + nonBatch);
      // System.out.println("nonbatch..........."+nonBatch);
        if (taskSelected == OBCODE) {
            accountList = (ArrayList) nonBatch.get(CommonConstants.ACT_NUM);

        } else if (taskSelected == INSERTDATA) {
            accountList = (ArrayList) nonBatch.get(CommonConstants.ACT_NUM);

        } else {
            accountList = getAccountList(nonBatch);
        }
      //  System.out.println("!!!!! accountList iNTtASK : "+accountList);
          String productId = CommonUtil.convertObjToStr(nonBatch.get("PROD_ID"));
        //        if(resultMap.containsKey("TOT_ACT_SIZE") && resultMap.get("TOT_ACT_SIZE") !=null)
        periodMap = (HashMap) sqlMap.executeQueryForObject("getInterestCalcPeriod", productId);
        HashMap resultMap = new HashMap();
        //System.out.println("nithya accountList :: " + accountList);
        if (accountList != null && accountList.size() > 0) {
            int accountListSize = accountList.size();
            for (int acc = 0; acc < accountListSize; acc++) {
                try {
                    //                sqlMap.startTransaction();
		//	System.out.println("!!!!! taskSelected iNTtASK : "+taskSelected);
                    if (taskSelected == OBCODE || taskSelected == INSERTDATA) {
                        paramMap.put("ACT_NUM", accountList.get(acc));

                    } else {
                        paramMap.put("ACT_NUM", ((HashMap) accountList.get(acc)).get("ACT_NUM")); //Interest is calculated for this Ac No
                    }
				    ArrayList resultList = new ArrayList();
                    if (getHeader().getTransactionType().equalsIgnoreCase(CommonConstants.PAYABLE)) {
                        resultList = (ArrayList) sqlMap.executeQueryForList("OperativeAccount.InterestPayable", paramMap);

                    } else {
                        resultList = (ArrayList) sqlMap.executeQueryForList("OperativeAccount.InterestReceivable", paramMap);
                    }

                    //System.out.println("resultList: " + resultList);

                    if (resultList.size() > 0) {
                        for (int i = 0, size = resultList.size(); i < size; i++) {
                            //run the interest calculator for each account.
                            resultMap = (HashMap) resultList.get(i);
                            if (accountListSize != 1 && (accountListSize - 1) != acc) {
                                resultMap.put("TOT_ACT_SIZE", new Integer(accountListSize));
                                //System.out.println("tooacctSize" + acc);
                            } else {
                                System.out.println("tooacctSize else");
                            }

                            if (getHeader().getTransactionType().equalsIgnoreCase(CommonConstants.PAYABLE)) {
                                resultMap.put("INT_TYPE", "C");
                            } else {
                                resultMap.put("INT_TYPE", "D");
                            }
                            if (paramMap.containsKey("DATE_TO") && paramMap.get("DATE_TO") != null) {
                                resultMap.put("END", paramMap.get("DATE_TO"));
                            } else {
                                resultMap.put("END", (Date) lstintCr.clone());
                            }
                            //System.out.println("BATCHPROCESS" + BATCHPROCESS + "resultMap resultMap" + resultMap);
                            if (DateUtil.dateDiff((Date) resultMap.get("START"), (Date) resultMap.get("END")) > 0) {
                                if (taskSelected != OBCODE && taskSelected != INSERTDATA) {
                                    sqlMap.startTransaction();
                                }
                                resultMap.put("BRANCH_ID", nonBatch.get("BRANCH"));
                                //commented by rishad 28/08/2015
                                //                        System.out.println("resultMaprunbatch"+resultMap);
                                // Takes list of value date transactions for the current period.
//                                List valueDateList = sqlMap.executeQueryForList("getValueDateForIntCalc", resultMap);
//                                if (valueDateList != null && valueDateList.size() > 0) {
//                                    String productId = (String) resultMap.get("PROD_ID");
//                                    HashMap periodMap = (HashMap) sqlMap.executeQueryForObject("getInterestCalcPeriod", productId);
//                                    objInterestBatchTO = doValueDate(valueDateList, periodMap, resultMap, objLogDAO, objLogTO);
//                                    periodMap = null;
//                                } else {
//                                    //Added BY Suresh
//                                //    System.out.println("############### nonBatch : " + nonBatch);
//                                    if (nonBatch.containsKey("INT_PAYABLE_AMOUNT")) {
//                                        resultMap.put("INT_PAYABLE_AMOUNT", nonBatch.get("INT_PAYABLE_AMOUNT"));
//                                    }
//                                    //                                transferTransobj = new TransferTrans();
//                                    //                                transferTransobj.setInitiatedBranch(actBranch);
//                                    if(nonBatch.containsKey("generateSingleTransId")&&nonBatch.get("generateSingleTransId")!=null)
//                                    {
//                                        resultMap.put("generateSingleTransId",nonBatch.get("generateSingleTransId"));
//                                    }
//                                    objInterestBatchTO = runBatch(resultMap, paramMap, objLogDAO, objLogTO);
//                                }
                       //end comment
                                //added by rishad
                                 if (nonBatch.containsKey("INT_PAYABLE_AMOUNT")) {
                                        resultMap.put("INT_PAYABLE_AMOUNT", nonBatch.get("INT_PAYABLE_AMOUNT"));
                                    }
                                    //                                transferTransobj = new TransferTrans();
                                    //                                transferTransobj.setInitiatedBranch(actBranch);
                                    if(nonBatch.containsKey("generateSingleTransId")&&nonBatch.get("generateSingleTransId")!=null)
                                    {
                                        resultMap.put("generateSingleTransId",nonBatch.get("generateSingleTransId"));
                                    }
                                    // Code for SB OD  : isTODSetForProduct nithya
                                    HashMap todCheckmap = new HashMap();
                                    todCheckmap.put("PROD_ID",CommonUtil.convertObjToStr(nonBatch.get("PROD_ID")));
                                    List todList = sqlMap.executeQueryForList("isTODSetForProduct", todCheckmap);
                                    if(todList != null && todList.size() > 0){
                                        HashMap todMap = (HashMap)todList.get(0);
                                        if(todMap.containsKey("TEMP_OD_ALLOWED")){
                                            if(CommonUtil.convertObjToStr(todMap.get("TEMP_OD_ALLOWED")).equalsIgnoreCase("Y")){
                                                runBatchForSBOD(resultMap, paramMap, objLogDAO, objLogTO);                                                                                                                                          
                                            }else{
                                               System.out.println("SBOD not set : normal SB processing"); 
                                               objInterestBatchTO = runBatch(resultMap, paramMap, objLogDAO, objLogTO); 
                                            }
                                                
                                        }else{
                                            System.out.println("SBOD not set : normal SB processing"); 
                                            objInterestBatchTO = runBatch(resultMap, paramMap, objLogDAO, objLogTO); 
                                        }
                                    }else{
                                        objInterestBatchTO = runBatch(resultMap, paramMap, objLogDAO, objLogTO); 
                                    }
                                    // End
                                   // objInterestBatchTO = runBatch(resultMap, paramMap, objLogDAO, objLogTO);
                                  //end
                                if (objInterestBatchTO != null) {
                                    interestMap.put(CommonUtil.convertObjToStr(objInterestBatchTO.getActNum()), CommonUtil.convertObjToStr(objInterestBatchTO.getIntAmt()));
                                } else {
                                    interestMap.put(paramMap.get("ACT_NUM"), "0.0");
                                }
                                if (taskSelected != OBCODE && taskSelected != INSERTDATA) {
                                    System.out.println("transaction completed");
                                    sqlMap.commitTransaction();
                                }
                            } else {
								if (!(paramMap.containsKey("TRIAL") && paramMap.get("TRIAL").equals("Y"))) {
                                if (taskSelected == BATCHPROCESS && getHeader().getTransactionType().equalsIgnoreCase(CommonConstants.PAYABLE)) {
                                    if (accountListSize != 1 && (accountListSize - 1) == acc && isBatchExecuted) {
                                        resultMap = prepareMap(paramMap, resultMap);
                                        transferTransobj.setInitiatedBranch(actBranch);
                                    //    System.out.println(totInterestAmt + "resultMap   #####" + resultMap);
                                        if (totInterestAmt > 0.0) {
                                            batchList.add(transferTransobj.getDebitTransferTO(resultMap, totInterestAmt));
                                    //       System.out.println("batchList end#####" + batchList);
                                            transferTransobj.setInitiatedBranch(actBranch);
                                            if (batchList != null && batchList.size() > 0) {
                                                transferTransobj.doDebitCredit(batchList, actBranch, true);
												}
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        interestMap.put(paramMap.get("ACT_NUM"), "0.0");
                        //System.out.println("implementTask " + paramMap.get("ACT_NUM"));

                    }

                    //                if(objInterestBatchTO != null && taskSelected != OBCODE){
                    //                    if(getHeader().getTransactionType().equalsIgnoreCase(CommonConstants.PAYABLE)){
                    //                        //__ Update the LAST_INT_APPLDT_CR in OP_AC_INTPAY_PARAM
                    //                        sqlMap.executeUpdate("updatePaymentApplDt", objInterestBatchTO);
                    //
                    //                    }else{
                    //                        //__ Update the LAST_INT_APPLDT_CR in OP_AC_INTRECV_PARAM
                    //                        sqlMap.executeUpdate("updateRecievableApplDt", objInterestBatchTO);
                    //                    }
                    //
                    //
                    //                }
                    //                sqlMap.commitTransaction();
                } catch (Exception e) {
                    //                sqlMap.rollbackTransaction();
                    if (taskSelected != OBCODE && taskSelected != INSERTDATA) {
                        e.printStackTrace();
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
                                            HashMap errorMap = new HashMap();
                                            errorMap.put("ERROR_DATE", currDt);
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
                                    System.out.println("#$#$ if not TTException part..." + e);
                                    errMsg = e.getMessage();
                                    HashMap errorMap = new HashMap();
                                    errorMap.put("ERROR_DATE", currDt);
                                    errorMap.put("TASK_NAME", taskLable);
                                    errorMap.put("ERROR_MSG", errMsg);
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
                            System.out.println("#$#$ if not TTException part..." + e);
                            errMsg = e.getMessage();
                            HashMap errorMap = new HashMap();
                            errorMap.put("ERROR_DATE", currDt);
                            errorMap.put("TASK_NAME", taskLable);
                            errorMap.put("ERROR_MSG", errMsg);
                            errorMap.put("ERROR_CLASS", errClassName);
                            errorMap.put("ACT_NUM", paramMap.get("ACT_NUM"));
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
                        //                    batchList =null;
                    }
                    e.printStackTrace();

                    if (!isExceptionCatch) {
                        //                    throw new TransRollbackException(e);
                        throw new TTException(e);
                    } else {
                        status.setStatus(BatchConstants.ERROR);
                    }
                }
            }
            if ((paramMap.containsKey("TRIAL") && paramMap.get("TRIAL").equals("Y"))) {
                System.out.println("##################### TRIAL PROCESS ONLY COMPLETED ############");
            }
        }
        if (taskSelected != OBCODE && taskSelected != INSERTDATA) {
            sqlMap.startTransaction();
        }
        HashMap proMap = new HashMap();
        proMap.put("LAST_APPL_DT", resultMap.get("END"));
        proMap.put(CommonConstants.BRANCH_ID, branch);
        proMap.put("PROD_ID", paramMap.get("PROD_ID"));
        proMap.put("PROD_TYPE", paramMap.get("PRODUCT_TYPE"));
        //System.out.println("------proMap------------" + proMap);
        if (!(paramMap.containsKey("TRIAL") && paramMap.get("TRIAL").equals("Y"))) {
            //System.out.println("nithya paramMap :: " + paramMap +" \n taskSelected :: " + taskSelected); // To be commented
            if (!paramMap.containsKey("ACT_TO") && taskSelected == BATCHPROCESS) {
                sqlMap.executeUpdate("updateDepositApplicationApplDT", proMap);
                proMap.put("LAST_PROV_DT", resultMap.get("END"));
                sqlMap.executeUpdate("updateDepositProvisionApplDT", proMap);// Added by nithya on 03-10-2018 for KD 233 RBI ECS - SB OD Interest application - Needed to update the Deposit Provision Table properly.
            }
        }
        if (taskSelected != OBCODE && taskSelected != INSERTDATA) {
            sqlMap.commitTransaction();
        }
        return interestMap;
    }

    private InterestBatchTO doValueDate(List valueDateList, HashMap periodMap, HashMap resultMap, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
       // System.out.println("#$#$# valueDateList : " + valueDateList + "  resultMap : " + resultMap);
        InterestBatchTO objInterestBatchTO = null;
        double finalIntAmt = 0;
        //        Date lstintCr = (Date) CurDate.clone();
        resultMap.put("AMOUNT", new Double(0));
        Date startDt = (Date) resultMap.get("START");
        Date endDt = (Date) resultMap.get("END");
        periodMap.put("AMOUNT", new Double(0));
        periodMap.put("PROD_ID", resultMap.get("PROD_ID"));
        periodMap.put("ACT_NUM", resultMap.get("ACT_NUM"));
        HashMap valueDateMap = (HashMap) valueDateList.get(0);
        Date valueDt = (Date) valueDateMap.get("VALUE_DT");
        Date transDt = null;
        String transType = CommonUtil.convertObjToStr(valueDateMap.get("TRANS_TYPE"));
        HashMap dailyProductHashMap = new HashMap();
        List dailyProductLst = new ArrayList();
        double addAmount = 0;

        Date START = (Date) startDt.clone();
        Date END = (Date) START.clone();
        //System.out.println("#$#$# START : " + START + "  valueDt : " + valueDt);
        int FREQ_A = CommonUtil.convertObjToInt(resultMap.get("FREQ_A"));
        int FREQ = CommonUtil.convertObjToInt(resultMap.get("FREQ"));
        int startDate = CommonUtil.convertObjToInt(periodMap.get("STARTDAY_PROD_CALC"));
        int endDate = CommonUtil.convertObjToInt(periodMap.get("ENDDAY_PROD_CALC"));
        // To start from the period which is from the starting value date.
        while (DateUtil.dateDiff(valueDt, START) > 0) {
            START = DateUtil.addDays(START, -1 * FREQ_A);
        }
        Date START_DT = null;
        // From the starting value date period to end period or current date.
        while (DateUtil.dateDiff(END, endDt) > 0) {
            if (resultMap.containsKey(InterestTaskRunner.PROD_ID)) {
                runningProdId = CommonUtil.convertObjToStr(resultMap.get(InterestTaskRunner.PROD_ID));
                runningActNum = CommonUtil.convertObjToStr(resultMap.get("ACT_NUM"));
            } else {
                resultMap.put(InterestTaskRunner.PROD_ID, runningProdId);
                resultMap.put("ACT_NUM", runningActNum);
            }
            resultMap.put(InterestTaskRunner.PRINCIPLE_AMT, new Double(0));
            //System.out.println("#$#$# resultMap : " + resultMap);
            double valueDtProdAmt = 0;
            // To take one quarterly/half yearly/yearly based on the product level frequency.
            END = DateUtil.addDays(START, FREQ_A);
            //System.out.println("#$#$# START : " + START + "   END : " + END);
            Date newValueDt = null;
            List lst = null;
            HashMap amtHash = null;
            Date chkDate = null;
            double dayEndAmount = 0;
            double nextAmount = 0;
            START_DT = DateUtil.addDays((Date) START.clone(), 1);
            Date END_DT = null;
            //            System.out.println("#$#$# valueDateNewList before while loop : "+valueDateNewList);
            Date currDate = (Date) END.clone();
            boolean valueDtOver = false;
            while (DateUtil.dateDiff(START_DT, END) > 0) {
                END_DT = DateUtil.addDays(START_DT, FREQ);
                periodMap.put("START", START_DT);
                periodMap.put("TODAY_DT", END_DT);
                if ((!paramMap.containsKey("ADD_MONTHS")) && DateUtil.dateDiff(endDt, END_DT) > 0) {
                   // System.out.println("#$#$# process is over for this a/c ...");
                    valueDtOver = true;
                    break;
                }
                // For a/c closing interest should be calculated upto previous month
                // So the following if condition checks the current period end date is equal to END dt.
                if (START_DT.getMonth() == endDt.getMonth()
                        && START_DT.getYear() == endDt.getYear()) {
                    if (paramMap.containsKey("ADD_MONTHS")) {
                        START_DT = (Date) END_DT.clone();
                    //    System.out.println("#$#$# this is last month for a/c closing...");
                        valueDtOver = true;
                        break;
                    }
                }
                //System.out.println("#$#$# periodMap in performValueDate 2 : " + periodMap);
                // To take daily balance for a particular month.
                lst = sqlMap.executeQueryForList("getDailyBalanceForValueDateNew", periodMap);
                //System.out.println("#$#$# old dailyProductLst in performValueDate 2 : " + lst);
                if (lst.size() > 0) {
                    for (int v = 0; v < lst.size(); v++) {
                        amtHash = (HashMap) lst.get(v);
                        chkDate = (Date) amtHash.get("DAY_END_DT");
                        dayEndAmount = CommonUtil.convertObjToDouble(amtHash.get("AMT")).doubleValue();
                        for (int j = 0; j < valueDateList.size(); j++) {
                            valueDateMap = (HashMap) valueDateList.get(j);
                            transDt = (Date) valueDateMap.get("TRANS_DT");
                            valueDt = (Date) valueDateMap.get("VALUE_DT");
                            transType = CommonUtil.convertObjToStr(valueDateMap.get("TRANS_TYPE"));
                            nextAmount = CommonUtil.convertObjToDouble(valueDateMap.get("AMOUNT")).doubleValue();
                            if (transType.equals("DEBIT")) {
                                nextAmount = -1 * nextAmount;
                            }
                            // If dayend date equals valueDt the transaction amount will be added.
                            if (DateUtil.dateDiff(valueDt, chkDate) == 0) {
                                addAmount = addAmount + nextAmount;
                                //System.out.println("#$#$# valueDt=chkDate : addAmount : " + addAmount + "   nextAmount : " + nextAmount);
                            }
                            // If dayend date equals transDt the transaction amount will be deducted.
                            // Because the transDt onwards act balance affected. So, no need to add again.
                            if (DateUtil.dateDiff(transDt, chkDate) == 0) {
                                nextAmount = -1 * nextAmount;
                                addAmount = addAmount + nextAmount;
                                //System.out.println("#$#$# transDt=chkDate : addAmount : " + addAmount + "   nextAmount : " + nextAmount);
                            }
                        }
                        //System.out.println("#$#$# outside for loop : dayEndAmount : " + dayEndAmount + "   addAmount : " + addAmount);
                        dayEndAmount += addAmount;
                        amtHash.put("AMT", new Double(dayEndAmount));
                    }
                    dailyProductLst = new ArrayList();
                    //System.out.println("#$#$# startDate : " + startDate + "   endDate : " + endDate);
                    for (int v = 0; v < lst.size(); v++) {
                        //                        System.out.println("#$#$# lst.size() : "+lst.size()+"   v : "+v);
                        amtHash = (HashMap) lst.get(v);
                        chkDate = (Date) amtHash.get("DAY_END_DT");
                        if (chkDate.getDate() >= startDate && chkDate.getDate() <= endDate) // To check whether the date between 10 to 31 or not
                        {
                            dailyProductLst.add(amtHash.get("AMT"));
                        } else {
                            lst.remove(v);          // If the date not between 10 to 31, then removes from the list.
                            v = v - 1;
                        }
                    }
                    //System.out.println("#$#$# new dailyProductLst in performValueDate 2 : " + lst);
                    //System.out.println("#$#$# new dailyProductLst in performValueDate 3 : " + dailyProductLst);

                    dailyProductHashMap.put((START_DT.getMonth() + 1) + "-" + (START_DT.getYear() + 1900), dailyProductLst);
                    //System.out.println("#$#$# dailyProductLst in performValueDate 2 : " + dailyProductHashMap);
                    double[] sortList = new double[dailyProductLst.size()];

                    for (int v = 0; v < dailyProductLst.size(); v++) {
                        sortList[v] = CommonUtil.convertObjToDouble(dailyProductLst.get(v)).doubleValue();
                    }
                    java.util.Arrays.sort(sortList);
                    valueDtProdAmt += sortList[0];      // Takes the minimum balance as product & makes product sum.
                    //System.out.println("#$#$# final amtLst : " + sortList[0]);
                    sortList = null;
                }
                //System.out.println("#$#$# final valueDtProdAmt : " + valueDtProdAmt);
                START_DT = (Date) END_DT.clone();
            }

            //System.out.println("##@@## lstintCr = " + lstintCr + "##@@## currDate = " + currDate);
            String valueDtTransMode = "";
            if (DateUtil.dateDiff(currDate, endDt) != 0 && !valueDtOver) {  // No need to take old balance for the current period
                int add_months = 0;
                double amount = 0;
                int lstSize = 0;
                periodMap.put("START", START);
                periodMap.put("TODAY_DT", END);

                //                if (paramMap.containsKey("ADD_MONTHS"))
                //                    add_months = CommonUtil.convertObjToInt(paramMap.get("ADD_MONTHS"));
                periodMap.put("ADD_MONTHS", new Integer(0));
                //System.out.println("#$@## periodMap for old product amount = " + periodMap);
                 periodMap.put(CommonConstants.DB_DRIVER_NAME,db_Driver_name);
                List amtLst = sqlMap.executeQueryForList("getDailyBalance", periodMap);
                // lstSize = amtLst.size();
                //System.out.println("Amount List:" + amtLst);
                if (amtLst != null && amtLst.size() > 0) {
                    amount = CommonUtil.convertObjToDouble(amtLst.get(0)).doubleValue();
                }
              //  System.out.println("daily balance amoun t is..." + amount);
                //commented by rishad 20/08/2015
//                for (int ai = 0; ai < lstSize; ai++) {
//                    amount += CommonUtil.convertObjToDouble(amtLst.get(ai)).doubleValue();
//                }
//                //System.out.println("#### oldProdAmt = " + amount);
                resultMap.put("PROD_AMOUNT", new Double(amount - valueDtProdAmt));
                valueDtTransMode = "REVERSAL";
                amtLst = null;
            }
            // To perform usual operations if valueDtTransMode is NORMAL.
            if (!valueDtTransMode.equals("REVERSAL")) {
                valueDtTransMode = "NORMAL";
                resultMap.put("PROD_AMOUNT", new Double(valueDtProdAmt));
            }
            resultMap.put("VALUE_DT_MODE", valueDtTransMode);
            resultMap.put("START", START);
            resultMap.put("END", END);
            //System.out.println("#$#$# final resultMap  : " + resultMap);
            objInterestBatchTO = runBatch(resultMap, paramMap, objLogDAO, objLogTO);
            START = (Date) END.clone();
            if (objInterestBatchTO != null) {
                finalIntAmt += objInterestBatchTO.getIntAmt().doubleValue();
            }
        }
        for (int j = 0; j < valueDateList.size(); j++) {
            valueDateMap = (HashMap) valueDateList.get(j);
            valueDateMap.put("INITIATED_BRANCH", branch);
            sqlMap.executeUpdate("updateTransValueDateRemarks", valueDateMap);
        }
        valueDateMap = null;
        dailyProductLst = null;
        if (objInterestBatchTO == null) {
            objInterestBatchTO = new InterestBatchTO();
        }
        objInterestBatchTO.setActNum(CommonUtil.convertObjToStr(resultMap.get("ACT_NUM")));
        objInterestBatchTO.setIntAmt(new Double(finalIntAmt));
        return objInterestBatchTO;
    }

    //__ To be Called for the Batch Process...
    public TaskStatus executeTask() throws Exception {
        status = new TaskStatus();
        prodList = new ArrayList();
        String compStatus = "";
        //        try{
        HashMap nonBatch = new HashMap();
        //__ The selected task is being performed as a Batch Process...
        taskSelected = BATCHPROCESS;
        boolean intflg = false;
        if (prodType != null && prodType.length() > 0) {
            prodList.add(prodType);
        } else {
            prodList.add("OA");
            prodList.add("AD");
        }
        for (int prodNo = 0; prodNo < prodList.size(); prodNo++) {
            prodType = CommonUtil.convertObjToStr(prodList.get(prodNo));
            //__ get ProductData...
            if (getHeader().getTaskParam().containsKey(CommonConstants.PRODUCT_ID)) {
                nonBatch.put(InterestTaskRunner.PRODUCT_ID, getHeader().getTaskParam().get(CommonConstants.PRODUCT_ID));
                intflg = true;
            }
            //__ getAccount Data
            if (getHeader().getTaskParam().containsKey(CommonConstants.ACT_NUM)) {
                nonBatch.put(CommonConstants.ACT_NUM, getHeader().getTaskParam().get(CommonConstants.ACT_NUM));
                intflg = true;
            }

            status.setStatus(BatchConstants.STARTED);
            //        sqlMap.startTransaction();

            ArrayList productList = getProductList(nonBatch);
            int prodListSize = productList.size();

            for (int prod = 0; prod < prodListSize; prod++) {
                //Store all product level parameters
                paramMap.put(InterestTaskRunner.AC_HD_ID, ((HashMap) productList.get(prod)).get("AC_HD_ID")); //Ac Head
                paramMap.put(InterestTaskRunner.DEBIT_INT, ((HashMap) productList.get(prod)).get("DEBIT_INT")); //Debit Int Head
                paramMap.put(InterestTaskRunner.CREDIT_INT, ((HashMap) productList.get(prod)).get("CREDIT_INT")); //Credit Int Head
                paramMap.put(InterestTaskRunner.PRODUCT_ID, ((HashMap) productList.get(prod)).get("PROD_ID")); //Product Id
                paramMap.put("MIN_CR_INT_AMT", ((HashMap) productList.get(prod)).get("MIN_CR_INT_AMT")); //Minimum amount for Crediting Interest
                paramMap.put("TAX_APPLICABLE", ((HashMap) productList.get(prod)).get("TAX_INT_APPLICABLE"));
                paramMap.put("NRO_TAX_AMT", ((HashMap) productList.get(prod)).get("NRO_TAX_AMT"));
                paramMap.put("TAX_HEAD", ((HashMap) productList.get(prod)).get("TAX_HEAD"));

                if (getHeader().getTransactionType().equalsIgnoreCase(CommonConstants.PAYABLE)) {
                    paramMap.put(InterestTaskRunner.LAST_APPL_DT, ((HashMap) productList.get(prod)).get("LAST_APPL_DT_CR")); //Last Interest Applied Dt Payale

                } else {
                    paramMap.put(InterestTaskRunner.LAST_APPL_DT, ((HashMap) productList.get(prod)).get("LAST_APPL_DT_DR")); //Last Interest Applied Dt Recievable
                }

                paramMap.put(InterestTaskRunner.APPL_NEW_DT, null); //Updated date based on FREQ_A from InterestTaskRunner
                paramMap.put(InterestTaskRunner.PRODUCT_TYPE, prodType);// getHeader().getProductType()) ; //Product Type; for Transaction update
                paramMap.put("CR_INT_APPL_FRQ",((HashMap) productList.get(prod)).get("CR_INT_APPL_FREQ"));  // Added by kannan taking credit int appl freq from product level refer rajesh sir               
                if (CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
                    HashMap tempMap = new HashMap();
                    tempMap.put("NEXT_DATE", currDt);
                    tempMap.put("BRANCH_DAY_END_STATUS", "COMPLETED");
                    tempMap.put("DC_DAY_END_STATUS", "COMPLETED");
                    //                branchList=(List)sqlMap.executeQueryForList("getAllBranchesFromDayEnd",tempMap);
                    branchList = (List) sqlMap.executeQueryForList("getSelectBranchCompletedLst", tempMap);
                    tempMap = null;
                } else {
                    HashMap tempMap = new HashMap();
                    tempMap.put(CommonConstants.BRANCH_ID, getHeader().getBranchID());
                    branchList = new ArrayList();
                    branchList.add(tempMap);
                    tempMap = null;
                }
                if (branchList != null && branchList.size() > 0) {
                    for (int b = 0; b < branchList.size(); b++) {
                        HashMap branchMap = (HashMap) branchList.get(b);
                        branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                        currDt = ServerUtil.getCurrentDate(branch);
                        //                    if (taskLable!=null && taskLable.length()>0) {  //Only from DayEnd
                        HashMap compMap = new HashMap();

                        List compLst = null;
                        if (!paramMap.containsKey("CHARGES_PROCESS")) {
                            compMap.put("TASK_NAME", taskLable);
                            compMap.put("DAYEND_DT", currDt);
                            compMap.put("BRANCH_ID", branch);
                            compLst = (List) sqlMap.executeQueryForList("getSelectTaskLst", compMap);
                            compMap = null;
                            if (compLst != null && compLst.size() > 0) {
                                compMap = (HashMap) compLst.get(0);
                                compStatus = CommonUtil.convertObjToStr(compMap.get("TASK_STATUS"));
                                compMap = null;
                            }
                        } else {
                            compStatus = "ERROR";
                            compMap.put("TASK_NAME", "A");
                            compMap.put("DAYEND_DT", currDt);
                            compMap.put("BRANCH_ID", branch);
                            compLst = (List) sqlMap.executeQueryForList("getSelectTaskLst", compMap);
                            compMap = null;
                        }
                        if (compLst.size() <= 0 || compStatus.equals("ERROR")) {
                            try {
                                HashMap errorMap = new HashMap();
                                if (!paramMap.containsKey("CHARGES_PROCESS")) {
                                    sqlMap.startTransaction();
                                    errorMap.put("ERROR_DATE", currDt);
                                    errorMap.put("TASK_NAME", taskLable);
                                    errorMap.put("BRANCH_ID", branch);
                                    sqlMap.executeUpdate("deleteError_showing", errorMap);
                                    sqlMap.commitTransaction();
                                }
                                //                                    sqlMap.startTransaction();
                                nonBatch.put(InterestTaskRunner.PRODUCT_ID, ((HashMap) productList.get(prod)).get("PROD_ID")); //Product Id)

                                //                lstintCr=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get(InterestTaskRunner.LAST_APPL_DT))) ;
                                paramMap.put(CommonConstants.BRANCH_ID, branch);
                                //                                        lstintCr=(Date)paramMap.get(InterestTaskRunner.LAST_APPL_DT);
                                paramMap.put("PROD_TYPE", paramMap.get(InterestTaskRunner.PRODUCT_TYPE));
                                List ntlst = sqlMap.executeQueryForList("lastIntApplnDt", paramMap);
                                if (ntlst != null && ntlst.size() > 0) {
                                    HashMap dtMap = (HashMap) ntlst.get(0);
                                    lstintCr = (Date) dtMap.get(InterestTaskRunner.LAST_APPL_DT);
                                     nonBatch.put("inputDt",lstintCr);
                            ///      System.out.println("LSTINCRDATE"+lstintCr);
                                    //commented by rishad 18/08/2015 
                                   // lstintCr = (Date) currDt.clone();
                                    int fre = CommonUtil.convertObjToInt(((HashMap) productList.get(prod)).get("CR_INT_APPL_FREQ"));

                                    //                lstintCr= DateUtil.addDays(lstintCr, fre);
                                    Date passDate = new Date(2011 - 1900, 3 - 1, 31); //For taking end of the month date
                                    lstintCr = DateUtil.nextCalcDate(passDate, lstintCr, fre);
                                    //System.out.println("LST AFTER PASING"+lstintCr); 
                                    Date dayB = new Date();
                                    dayB = ServerUtil.getCurrentDate(branch);
                            //       System.out.println("date....."+dayB);
                                    //                                        dayB = new Date(dayB.getYear(), dayB.getMonth(), dayB.getDate());
                                   // System.out.println("lstintCr ===========" + lstintCr);
                                  // System.out.println("dayB ===========" + dayB);
                                    //                                    sqlMap.startTransaction();
                                    //                                    if(intflg==false) {
                                    HashMap resultMap = new HashMap();
                                  System.out.println("paramMap: " + paramMap);
                                    if (paramMap.containsKey("DATE_TO")) {
                                        resultMap.put("END", paramMap.get("DATE_TO"));
                                        nonBatch.put("END", paramMap.get("DATE_TO"));
                                    } else {
                                        resultMap.put("END", (Date) currDt.clone());
                                        nonBatch.put("END", paramMap.get("DATE_TO"));
                                    }
                                    //Here if condition is checking, In DEPOSIT_PROVISION TABLE  LAST_PROV_DT+FREQUENCY(In Product Level Yearly/Half Yearly..) is Less than Day_End_Date(Appl Date)
                                    if (DateUtil.dateDiff(lstintCr, dayB) > 0) {
                                        if (b == 0) {
                                            runDailyBalance();
                                        }
                                        if (prodType.equals("AD")) {
                                            nonBatch.put("BRANCH_CODE", branch);
                                            if (taskSelected == BATCHPROCESS) {
                                                if (DateUtil.dateDiff(currDt, lstintCr) > 0) {
                                                    continue;
                                                }
                                            }
                                            ArrayList totAcctList = getAccountList(nonBatch);//
                                            intPayableAD(resultMap, totAcctList, nonBatch);
                                        } else {
                                            implementTask(nonBatch, true);
                                        }
                                    } else {
                                        //Added by kannan refer rajesh sir  Interest calculate upto todate & not consider about holiday
                                        if (prodType.equals("OA") && paramMap.containsKey("DATE_TO") && paramMap.get("DATE_TO") != null) {
                                            Date toDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("DATE_TO")));
                                            HashMap newMap = new HashMap();
                                            //starting of sb calc
                                            List lst = (List) sqlMap.executeQueryForList("getLastFinYear", newMap);
                                            newMap = (HashMap) lst.get(0);
                                            Date finYrDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(newMap.get("LAST_FINANCIAL_YEAR_END")));
                                            if (paramMap.containsKey("CR_INT_APPL_FRQ")) {
                                                finYrDt = DateUtil.addDays(finYrDt, CommonUtil.convertObjToInt(paramMap.get("CR_INT_APPL_FRQ")));
                                             //   System.out.println("***********Financial Year Date : " + finYrDt);
                                                if (DateUtil.dateDiff(toDate, finYrDt) == 0) {
                                                    checkThisCDate = finYrDt;
                                                    dayB = toDate;
                                                }
                                            }
                                        } else {
                                            holiydaychecking(lstintCr);
                                           // System.out.println("checkThisCDate--------" + checkThisCDate);
                                          //  System.out.println("datediff###" + DateUtil.dateDiff(checkThisCDate, dayB));
                                            checkThisCDate = new Date(checkThisCDate.getYear(), checkThisCDate.getMonth(), checkThisCDate.getDate());
                                        }
                                        if (DateUtil.dateDiff(checkThisCDate, dayB) >= 0) {
                                            if (b == 0) {
                                                runDailyBalance();
                                            }
                                            if (prodType.equals("AD")) {
                                                nonBatch.put("BRANCH_CODE", branch);
                                                if (taskSelected == BATCHPROCESS) {
                                                    if (DateUtil.dateDiff(currDt, lstintCr) < 0) {
                                                        continue;
                                                    }
                                                }
                                                ArrayList totAcctList = getAccountList(nonBatch);
                                                intPayableAD(resultMap, totAcctList, nonBatch);
                                            } else {
                                                implementTask(nonBatch, true);
                                            }
                                        }
                                    }
                                    //                                    }else{
                                    //                                        lstintCr=ServerUtil.getCurrentDate(branch);
                                    //                                        implementTask(nonBatch, true);
                                    //                                    }
                                    //                                    sqlMap.commitTransaction();
                                } else {
                                    System.out.println("No Records In DepositProvision Table for this Product " + paramMap.get("PROD_ID"));
                                }
                                status.setStatus(BatchConstants.COMPLETED);
                            } catch (Exception e) {
                                //                                sqlMap.rollbackTransaction();
                                //                                status.setStatus(BatchConstants.ERROR) ;
                                //                                e.printStackTrace();
                                if (!paramMap.containsKey("CHARGES_PROCESS")) {
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
                                                        HashMap errorMap = new HashMap();
                                                        errorMap.put("ERROR_DATE", currDt);
                                                        errorMap.put("TASK_NAME", taskLable);
                                                        errorMap.put("ERROR_MSG", strExc);
                                                        errorMap.put("ERROR_CLASS", errClassName);
                                                        errorMap.put("ACT_NUM", paramMap.get("ACT_NUM"));
                                                        errorMap.put("BRANCH_ID", actBranch);
                                                        sqlMap.startTransaction();
                                                        sqlMap.executeUpdate("insertError_showing", errorMap);
                                                        sqlMap.commitTransaction();
                                                        errorMap = null;
                                                    }
                                                }
                                            } else {
                                                System.out.println("#$#$ if not TTException part..." + e);
                                                errMsg = e.getMessage();
                                                HashMap errorMap = new HashMap();
                                                errorMap.put("ERROR_DATE", currDt);
                                                errorMap.put("TASK_NAME", taskLable);
                                                errorMap.put("ERROR_MSG", errMsg);
                                                errorMap.put("ERROR_CLASS", errClassName);
                                                errorMap.put("ACT_NUM", paramMap.get("ACT_NUM"));
                                                errorMap.put("BRANCH_ID", actBranch);
                                                sqlMap.startTransaction();
                                                sqlMap.executeUpdate("insertError_showing", errorMap);
                                                sqlMap.commitTransaction();
                                                errorMap = null;
                                            }
                                        }
                                    } else {
                                        System.out.println("#$#$ if not TTException part..." + e);
                                        errMsg = e.getMessage();
                                        HashMap errorMap = new HashMap();
                                        errorMap.put("ERROR_DATE", currDt);
                                        errorMap.put("TASK_NAME", taskLable);
                                        errorMap.put("ERROR_MSG", errMsg);
                                        errorMap.put("ERROR_CLASS", errClassName);
                                        errorMap.put("ACT_NUM", paramMap.get("ACT_NUM"));
                                        errorMap.put("BRANCH_ID", actBranch);
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
                                } else {
                                    sqlMap.rollbackTransaction();
                                    e.printStackTrace();
                                    //                                e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!paramMap.containsKey("CHARGES_PROCESS")) {
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
                        statusMap.put("DAYEND_DT", currDt);
                        sqlMap.executeUpdate("updateTskStatus", statusMap);
                        statusMap = null;
                    } else {
                        HashMap statusMap = new HashMap();
                        //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                        statusMap.put("BRANCH_CODE", branch);
                        statusMap.put("TASK_NAME", taskLable);
                        statusMap.put("TASK_STATUS", "COMPLETED");
                        statusMap.put("USER_ID", getHeader().getUserID());
                        statusMap.put("DAYEND_DT", currDt);
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
                        statusMap.put("DAYEND_DT", currDt);
                        sqlMap.executeUpdate("updateTskStatus", statusMap);
                        statusMap = null;
                    } else {
                        //                            isError = true;
                        HashMap statusMap = new HashMap();
                        //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                        statusMap.put("BRANCH_CODE", branch);
                        statusMap.put("TASK_NAME", taskLable);
                        statusMap.put("TASK_STATUS", "ERROR");
                        statusMap.put("USER_ID", getHeader().getUserID());
                        statusMap.put("DAYEND_DT", currDt);
                        sqlMap.executeUpdate("InsertDayEndStatus", statusMap);
                        statusMap = null;

                    }
                    //                            }
                    //                        }
                    //                    }
                    //                    }
                    //                    else {    //From Charges screen
                    //                        try{
                    //                            nonBatch.put(InterestTaskRunner.PRODUCT_ID, ((HashMap)productList.get(prod)).get("PROD_ID")) ; //Product Id)
                    //
                    //                            //                lstintCr=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get(InterestTaskRunner.LAST_APPL_DT))) ;
                    //                            paramMap.put(CommonConstants.BRANCH_ID, branch);
                    //                            //                                        lstintCr=(Date)paramMap.get(InterestTaskRunner.LAST_APPL_DT);
                    //                            List ntlst=sqlMap.executeQueryForList("lastIntApplnDt",paramMap);
                    //                            if (ntlst!=null && ntlst.size()>0) {
                    //                                HashMap dtMap=(HashMap)ntlst.get(0);
                    //                                lstintCr=(Date)dtMap.get(InterestTaskRunner.LAST_APPL_DT);
                    //                                int fre=CommonUtil.convertObjToInt(((HashMap)productList.get(prod)).get("CR_INT_APPL_FREQ"));
                    //
                    //                                //                lstintCr= DateUtil.addDays(lstintCr, fre);
                    //                                lstintCr=DateUtil.addDays(lstintCr,fre);
                    //                                Date dayB=new Date();
                    //                                dayB= ServerUtil.getCurrentDate(branch);
                    //                                //                                        dayB = new Date(dayB.getYear(), dayB.getMonth(), dayB.getDate());
                    //                                System.out.println("lstintCr ==========="+lstintCr);
                    //                                System.out.println("dayB ==========="+dayB);
                    //                                if(intflg==false) {
                    //                                    if(DateUtil.dateDiff(lstintCr,dayB)>0){
                    //                                        runDailyBalance();
                    //                                        implementTask(nonBatch, true);
                    //                                    } else {
                    //                                        holiydaychecking(lstintCr);
                    //                                        System.out.println("checkThisCDate--------"+checkThisCDate);
                    //                                        System.out.println("datediff###"+DateUtil.dateDiff(checkThisCDate,dayB));
                    //                                        checkThisCDate = new Date(checkThisCDate.getYear(), checkThisCDate.getMonth(), checkThisCDate.getDate());
                    //                                        if(DateUtil.dateDiff(checkThisCDate,dayB)>=0){
                    //                                            runDailyBalance();
                    //                                            implementTask(nonBatch, true);
                    //                                        }
                    //                                    }
                    //                                }else{
                    //                                    lstintCr=ServerUtil.getCurrentDate(branch);
                    //                                    implementTask(nonBatch, true);
                    //                                }
                    //                            } else {
                    //                                System.out.println("No Records In DepositProvision Table for this Product "+paramMap.get("PROD_ID"));
                    //                            }
                    //
                    //                        }catch(Exception e){
                    //                            sqlMap.rollbackTransaction();
                    //                            status.setStatus(BatchConstants.ERROR) ;
                    //
                    //                            e.printStackTrace();
                    //                        }
                    //                    }
                }
            }

        }

        //        sqlMap.commitTransaction();
        //System.out.println("status.getStatus() : " + status.getStatus());
        if (status.getStatus() != BatchConstants.ERROR) {
            status.setStatus(BatchConstants.COMPLETED);
        }
        //        if(isError){
        //            status.setStatus(BatchConstants.ERROR);
        //            isError = false;
        //        }
        System.out.println("status.getStatus() : " + status.getStatus());
        paramMap = null;
        taskSelected = 0;
        return status;
    }

    /**
     * payable interest calculation calculating on credit balance in advances
     */
    private HashMap intPayableAD(HashMap resultMap, ArrayList totAcctList, HashMap nonBatch) throws Exception {
        resultMap.put("END", lstintCr);
       // System.out.println("resultMap        :" + resultMap +"totAcctList --->"+totAcctList);
        HashMap returnMap = new HashMap();
        if (totAcctList != null && totAcctList.size() > 0) {
            for (int i = 0; i < totAcctList.size(); i++) {
                resultMap.putAll((HashMap) totAcctList.get(i));
                paramMap.putAll((HashMap) totAcctList.get(i));
                if (taskSelected == BATCHPROCESS) {
                    if (resultMap.get("START")!=null && (DateUtil.dateDiff((Date) resultMap.get("START"), lstintCr) < 0)) {
                        continue;
                    }


                }
 				paramMap.put(InterestTaskRunner.PRODUCT_ID, nonBatch.get(InterestTaskRunner.PRODUCT_ID));
                resultMap.put(InterestTaskRunner.PRODUCT_ID, nonBatch.get(InterestTaskRunner.PRODUCT_ID));
                InterestBatchTO objInterestBatchTO = null;
                InterestTaskRunner interestTaskRun = new InterestTaskRunner();
                //                resultMap.put("START",resultMap)
               // System.out.println("paramMap###" + paramMap + "totAcctList    " + totAcctList + "Old REsulsMap: " + resultMap);
                Date to_dt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("DATE_TO")));
                actBranch = CommonUtil.convertObjToStr(resultMap.get("BRANCH_ID"));
                paramMap.put("DR_BRANCH", actBranch);
                paramMap.put("CR_BRANCH", actBranch);
 				double interestAmt = 0;
                getHeader().setProductType(prodType);
                objInterestBatchTO = new InterestBatchTO();//interestTaskRun.getInterestAmountCommon(getHeader(), resultMap);
                objInterestBatchTO.setIntDt((Date) resultMap.get("START"));
                objInterestBatchTO.setApplDt((Date) resultMap.get("END"));
                objInterestBatchTO.setProductType("AD");
                objInterestBatchTO.setBranch_code(_branchCode);
                objInterestBatchTO.setProductType(prodType);
                objInterestBatchTO.setProductId(CommonUtil.convertObjToStr(nonBatch.get(InterestTaskRunner.PRODUCT_ID)));
                HashMap productAmountMap = interestTaskRun.getProductAmtForIntCalc(resultMap, prodType);
	            Double productAmount = CommonUtil.convertObjToDouble(productAmountMap.get("PRODUCT_AMT"));
	            objInterestBatchTO.setPrincipleAmt(productAmount);
                //System.out.println("objInterestBatchTO: " + objInterestBatchTO);
              //  double interestAmt = objInterestBatchTO.getIntAmt().doubleValue();
              //  double minCreditIntAmt = CommonUtil.convertObjToDouble(paramMap.get("MIN_CR_INT_AMT")).doubleValue();
             //   if (minCreditIntAmt > interestAmt) {
              //      interestAmt = 0;
              //  }
             //     System.out.println("interestAmt 999999--->"+interestAmt);
              //  returnMap.put("ADVANCES_CREDIT_INT", new Double(interestAmt));
                //__ In Case of BatchProcess, DO the following...
                if (taskSelected == BATCHPROCESS /*&& interestAmt > 0*/) {  //|| taskSelected == INSERTDATA

                     if (CommonUtil.convertObjToStr(objInterestBatchTO.getProductType()).equals("AD") /*&& interestAmt > 0*/) {
                       // insertActInterstUsingDayEndBalance(objInterestBatchTO);
                          paramMap.put("PROD_ID", paramMap.get("PRODUCT_ID"));
                          paramMap.put("CURR_DATE", paramMap.get("DATE_TO"));
                          List lst = sqlMap.executeQueryForList("getLoansProduct", paramMap);
                          if (lst != null && lst.size() > 0) {
                                HashMap productMap = (HashMap) lst.get(0);
                                paramMap.putAll(productMap);
                          }
                            paramMap.put("BATCH_PROCESS", "BATCH_PROCESS");
                  //          System.out.println("CHARGESUI####7777#@@@@@@" + paramMap);
                            List lstAppdt = sqlMap.executeQueryForList("GETLASTCALCDT", paramMap);
                            if (lstAppdt != null && lstAppdt.size() > 0) {
                                HashMap lastAppDt = (HashMap) lstAppdt.get(0);
                                paramMap.putAll(lastAppDt);
                               
                            }
                        HashMap dMap=interestCalcTermLoanAD(paramMap,resultMap);
                        interestAmt=CommonUtil.convertObjToDouble(dMap.get("INTEREST"));
                       // objInterestBatchTO.setI
                         returnMap.put("ADVANCES_CREDIT_INT", new Double(interestAmt));
                         objInterestBatchTO.setIntAmt(interestAmt);
                    }
                    //  System.out.println("interestAmt 88888--->"+interestAmt);
                      
                    ///////
                    /**
                     * To Enter the Data in the Transaction Table...
                     */
                    ArrayList batchList = new ArrayList();
                    TransferTrans transferTrans = new TransferTrans();
                    paramMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(resultMap.get(TransferTrans.PARTICULARS))
                            + " (" + CommonUtil.convertObjToStr(resultMap.get("START")) + " - "
                            + CommonUtil.convertObjToStr(resultMap.get("END")) + ")");
                    if(paramMap.containsKey("CHARGESUI")){
                          paramMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(resultMap.get(TransferTrans.PARTICULARS))
                            + " (" + CommonUtil.convertObjToStr(resultMap.get("START")) + " - "
                            + CommonUtil.convertObjToStr(paramMap.get("DATE_TO")) + ")");
                         
                    }
                    resultMap = prepareMap(paramMap, resultMap);
                    //System.out.println("resultMap@@@@     " + resultMap);
                    if(paramMap.containsKey("CHARGESUI")){
                        
                      resultMap.put(TransferTrans.DR_INSTRUMENT_2, "CHARGESUI");  
                    }
                    objInterestBatchTO.setIntAmt(new Double(objInterestBatchTO.getIntAmt().doubleValue()));
                    objInterestBatchTO.setPrincipleAmt(new Double(objInterestBatchTO.getPrincipleAmt().doubleValue()));
                    transferTrans.setInitiatedBranch(branch);
                    transferTrans.setLinkBatchId(CommonUtil.convertObjToStr(resultMap.get("CR_ACT_NUM")));
                    if (CommonUtil.convertObjToStr(paramMap.get("PROD_TYPE")).equals("AD")) {
                        resultMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                    } else {
                        resultMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.LOANS);
                        resultMap.put("TRANS_MOD_TYPE", TransactionFactory.LOANS);
                    }
                    batchList.add(transferTrans.getDebitTransferTO(resultMap, interestAmt));
                    batchList.add(transferTrans.getCreditTransferTO(resultMap, interestAmt));
                    transferTrans.setForDebitInt(true);  // To avoid error message "Insufficient Balance" even
                    // if the clearbalance is not enough for debiting interest
                    transferTrans.doDebitCredit(batchList, branch, true);
                    sqlMap.executeUpdate("updateAdvancesAppDate", resultMap);
                    //__ To insert the data(Act interest) in the ACT_INTEREST...
                    if (CommonUtil.convertObjToStr(objInterestBatchTO.getProductType()).equals("AD") && interestAmt > 0) {
                       // insertActInterstUsingDayEndBalance(objInterestBatchTO);
                          paramMap.put("PROD_ID", paramMap.get("PRODUCT_ID"));
                            paramMap.put("CURR_DATE", paramMap.get("DATE_TO"));
                          List lst = sqlMap.executeQueryForList("getLoansProduct", paramMap);
                            if (lst != null && lst.size() > 0) {
                                HashMap productMap = (HashMap) lst.get(0);
                                paramMap.putAll(productMap);
                            }
                            paramMap.put("BATCH_PROCESS", "BATCH_PROCESS");
                        //    System.out.println("CHARGESUI####7777#@@@@@@" + paramMap);
                            List lstAppdt = sqlMap.executeQueryForList("GETLASTCALCDT", paramMap);
                            if (lstAppdt != null && lstAppdt.size() > 0) {
                                HashMap lastAppDt = (HashMap) lstAppdt.get(0);
                                paramMap.putAll(lastAppDt);
                               
                            }
                       
                      //  interestCalcTermLoanAD(paramMap);
                        
                    } else {
                        sqlMap.executeUpdate("insertInterestBatchTO", objInterestBatchTO);
                    }

                } else if (taskSelected == BATCHPROCESS) {

                    resultMap.put("CR_ACT_NUM", resultMap.get("ACT_NUM"));

                    sqlMap.executeUpdate("updateAdvancesAppDate", resultMap);
                    if (CommonUtil.convertObjToStr(objInterestBatchTO.getProductType()).equals("AD") && interestAmt > 0) {
                        //insertActInterstUsingDayEndBalance(objInterestBatchTO);
                         paramMap.put("PROD_ID", paramMap.get("PRODUCT_ID"));
                            paramMap.put("CURR_DATE", paramMap.get("DATE_TO"));
                          List lst = sqlMap.executeQueryForList("getLoansProduct", paramMap);
                            if (lst != null && lst.size() > 0) {
                                HashMap productMap = (HashMap) lst.get(0);
                                paramMap.putAll(productMap);
                            }
                            paramMap.put("BATCH_PROCESS", "BATCH_PROCESS");
                     //       System.out.println("CHARGESUI####7777#@@@@@@" + paramMap);
                            List lstAppdt = sqlMap.executeQueryForList("GETLASTCALCDT", paramMap);
                            if (lstAppdt != null && lstAppdt.size() > 0) {
                                HashMap lastAppDt = (HashMap) lstAppdt.get(0);
                                paramMap.putAll(lastAppDt);
                               
                            }
                        
                       // interestCalcTermLoanAD(paramMap);
                    } else {
                        sqlMap.executeUpdate("insertInterestBatchTO", objInterestBatchTO);
                    }
                }
            }
            //////
            /////
            HashMap proMap = new HashMap();
            proMap.put("LAST_APPL_DT", resultMap.get("END"));
            proMap.put(CommonConstants.BRANCH_ID, branch);
            proMap.put("PROD_ID", paramMap.get("PROD_ID"));
            proMap.put("PROD_TYPE", prodType);
            //System.out.println("------proMap------------" + proMap);
         //   if (!paramMap.containsKey("ACT_TO") && taskSelected == BATCHPROCESS) {
                sqlMap.executeUpdate("updateDepositApplicationApplDT", proMap);
                if (!paramMap.containsKey("ACT_TO") && taskSelected == BATCHPROCESS) {
                sqlMap.executeUpdate("updateAdvancesAppDateBasingOnProdId", proMap);
                }

           // }
        }
        return returnMap;
    }
/*
    //insert act interest even break up period
    private void insertActInterstUsingDayEndBalance(InterestBatchTO objInterestBatchTO) throws Exception {//bbb
        HashMap singleRecord = new HashMap();
        HashMap hashDailyMap = new HashMap();
        Date dayEndDt = null, firstDate = null;
        String firstDt = null;
        double intRate = 0;
        double intAmount = 0;
        Object obj = objInterestBatchTO;
        Date chekdateLast = null;
        InterestBatchTO tempobjInterestBatchTO = null;
        singleRecord.put("START_DATE", objInterestBatchTO.getIntDt());
        singleRecord.put("CURR_DATE", lstintCr);//objInterestBatchTO.getApplDt());
        singleRecord.put("ACT_NUM", objInterestBatchTO.getActNum());
        singleRecord.put(CommonConstants.DB_DRIVER_NAME, db_Driver_name);
        System.out.println("objInterestBatchTO" + objInterestBatchTO + "singleRecord" + singleRecord);
        List dailyBalOD = sqlMap.executeQueryForList("dayEndBalanceForIntCalcAD", singleRecord);

        if (dailyBalOD != null && dailyBalOD.size() > 0) {
            intRate = CommonUtil.convertObjToDouble(objInterestBatchTO.getIntRate()).doubleValue();
            for (int i = 0; i < dailyBalOD.size(); i++) {
                chekdateLast = objInterestBatchTO.getApplDt();
                //                        System.out.println("#$#$"+dailyBalOD);
                hashDailyMap = (HashMap) dailyBalOD.get(i);
                dayEndDt = DateUtil.getDateWithoutMinitues(CommonUtil.convertObjToStr(hashDailyMap.get("DAY_END_DT")));
                Date nextDt = DateUtil.getDateWithoutMinitues(CommonUtil.convertObjToStr(hashDailyMap.get("NEXT_DT")));
                double curr_amt = 0;
                double next_amt = 0;
                curr_amt = CommonUtil.convertObjToDouble(hashDailyMap.get("AMT")).doubleValue();
                next_amt = CommonUtil.convertObjToDouble(hashDailyMap.get("NEXT_AMT")).doubleValue();
                //System.out.println("curr_amt" + curr_amt + "next_amt" + next_amt + "firstDt" + firstDt);
                //Babu added daily calculation-
                if (curr_amt == next_amt && firstDt == null) {
                      firstDt = CommonUtil.convertObjToStr(hashDailyMap.get("DAY_END_DT"));
                      firstDate = DateUtil.getDateMMDDYYYY(firstDt);
                      if (curr_amt < 0 && CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("COMPOUND")) {
                        firstDt = null;
                      }
                                    System.out.println("inside the firstdate##" + firstDt);

                 }
                //End bbb                
                if (curr_amt > 0) {
                    if (firstDt == null) {//curr_amt==next_amt &&
                        firstDt = CommonUtil.convertObjToStr(hashDailyMap.get("DAY_END_DT"));
                        firstDate = DateUtil.getDateMMDDYYYY(firstDt);


                        //                    if(curr_amt<=0)
                        //                        firstDt=null;
                        System.out.println("inside the firstdate##" + firstDt);
                    }
                }
                if ((curr_amt > 0 && curr_amt != next_amt) || i + 1 == dailyBalOD.size()) {
                    //                                hashDailyMap.putAll(productRecord);
                    if (firstDt != null && firstDt.length() > 0) {
                        hashDailyMap.put("START_DT", firstDate);

                        //                        if (DateUtil.dateDiff(dayEndDt,nextDt)==0)
                        //                            hashDailyMap.put("END_DT",hashDailyMap.get("NEXT_DT"));
                        //                        else{

                        hashDailyMap.put("END_DT", DateUtil.addDaysProperFormat(nextDt, 0));
                        if (i + 1 == dailyBalOD.size()) {
                            hashDailyMap.put("END_DT", DateUtil.addDaysProperFormat(objInterestBatchTO.getApplDt(), 0));
                        }
                        hashDailyMap.put("NO_ROUND_OFF_INT", "NO_ROUND_OFF_INT");
                        //                        }

                    } else {
                        hashDailyMap.put("START_DT", hashDailyMap.get("DAY_END_DT"));
                        if ((chekdateLast != null && DateUtil.dateDiff(dayEndDt, chekdateLast) == 0)
                                || DateUtil.dateDiff(dayEndDt, nextDt) == 0)//(dayEndDt.equals(chekdateLast))
                        {
                            hashDailyMap.put("END_DT", hashDailyMap.get("NEXT_DT"));
                        } else {
                            hashDailyMap.put("END_DT", DateUtil.addDaysProperFormat(nextDt, -1));
                        }
                        hashDailyMap.put("NO_ROUND_OFF_INT", "NO_ROUND_OFF_INT");
                    }



                    //System.out.println("hashdailymap####" + hashDailyMap);
                    tempobjInterestBatchTO = (InterestBatchTO) createNewTO(objInterestBatchTO);

                    tempobjInterestBatchTO.setApplDt(getProperFormatDate(hashDailyMap.get("END_DT")));
                    tempobjInterestBatchTO.setIntDt(getProperFormatDate(hashDailyMap.get("START_DT")));
                    //                    tempobjInterestBatchTO.setIntAmt(new Double(intAmount));
                    long noOfDays = DateUtil.dateDiff((Date) hashDailyMap.get("START_DT"), (Date) hashDailyMap.get("END_DT"));
                    //System.out.println("noOfDays####" + noOfDays + "curr_amt" + curr_amt + "intRate" + intRate);
                    intAmount = (double) (curr_amt * intRate * noOfDays + 1) / 36500;//(noOfDays+1
                    tempobjInterestBatchTO.setIntAmt(new Double(intAmount));
                    tempobjInterestBatchTO.setPrincipleAmt(new Double(curr_amt));
                    sqlMap.executeUpdate("insertInterestBatchTO", tempobjInterestBatchTO);
                    firstDt = null;
                } else {
                    if (firstDt != null && firstDt.length() > 0) {
                        hashDailyMap.put("START_DT", firstDate);
                        hashDailyMap.put("END_DT", nextDt);
                        //System.out.println("hashdailymap####else" + hashDailyMap);
                        //System.out.println("curr_amt" + curr_amt + "next_amt" + next_amt + "firstDt" + firstDt);
                        tempobjInterestBatchTO = (InterestBatchTO) createNewTO(objInterestBatchTO);
                        tempobjInterestBatchTO.setApplDt(getProperFormatDate(hashDailyMap.get("END_DT")));
                        tempobjInterestBatchTO.setIntDt(getProperFormatDate(hashDailyMap.get("START_DT")));
                        long noOfDays = DateUtil.dateDiff((Date) hashDailyMap.get("START_DT"), (Date) hashDailyMap.get("END_DT"));
                        //System.out.println("noOfDays####" + noOfDays + "curr_amt" + curr_amt + "intRate" + intRate);
                        intAmount = (double) (curr_amt * intRate * noOfDays + 1) / 36500;//(noOfDays+1
                        tempobjInterestBatchTO.setIntAmt(new Double(intAmount));
                        tempobjInterestBatchTO.setPrincipleAmt(new Double(curr_amt));
                        sqlMap.executeUpdate("insertInterestBatchTO", tempobjInterestBatchTO);
                        firstDt = null;
                    }
                }

            }
        }
    }*/
        private List getInterestDetails(HashMap whereMap, HashMap passDt) throws Exception {
        List list = null;
        String intToGetFrom = CommonUtil.convertObjToStr(whereMap.get("INT_GET_FROM"));
            if (intToGetFrom.equals("PROD")) {
                whereMap.put(CommonConstants.CREDIT, CommonConstants.CREDIT);
                // Add code for making interest periodwise : Added by nithya on 30-10-2017 for 7867
                List intCalcMethodLst = (List) sqlMap.executeQueryForList("checkIfIntCalcPeriodSlabWiseOrNot", whereMap);
                if (intCalcMethodLst != null && intCalcMethodLst.size() > 0) {
                    HashMap intCalcMethodMap = (HashMap) intCalcMethodLst.get(0);
                    if (intCalcMethodMap.containsKey("IS_INT_PERIOD_SLAB_CALC") && intCalcMethodMap.get("IS_INT_PERIOD_SLAB_CALC") != null && !"".equals(intCalcMethodMap.get("IS_INT_PERIOD_SLAB_CALC"))) {
                        if (CommonUtil.convertObjToStr(intCalcMethodMap.get("IS_INT_PERIOD_SLAB_CALC")).equalsIgnoreCase("Y")) {
                            whereMap.put("PERIOD_WISE_INT_RATE", "PERIOD_WISE_INT_RATE");
                            whereMap.put("INT_UP_TO_DATE", currDt.clone());
                        } else {
                            whereMap.put("AMOUNT_WISE_INT_RATE", "AMOUNT_WISE_INT_RATE");
                        }
                    } else {
                        whereMap.put("AMOUNT_WISE_INT_RATE", "AMOUNT_WISE_INT_RATE");
                    }
                } else {
                    whereMap.put("AMOUNT_WISE_INT_RATE", "AMOUNT_WISE_INT_RATE");
                }
                list = (List) sqlMap.executeQueryForList("getSelectProductTermLoanInterestMap", whereMap); //change deposit date to from date
                //      System.out.println("prodListrepaymentcalculator" + list);
            } else if (intToGetFrom.equals("ACT")) {
            list = (List) sqlMap.executeQueryForList("getSelectAccountTermLoanInterestMap", whereMap);
      //      System.out.println("actListrepaymentcalculator" + list);
        }
       /* if (list != null && list.size() > 0 && (passDt.containsKey("DEPOSIT_PREMATURE_CLOSER") || prematureLTD)) {
            for (int i = 0; i < list.size(); i++) {
                HashMap depositList = (HashMap) list.get(i);
                System.out.println("depositList ###" + depositList);
                depositList.put("INTEREST", passDt.get("DEPOSIT INT"));
                list.set(i, depositList);
            }
            System.out.println("list####" + list);
        }*/


        if (list == null || list.size() == 0) {
            throw new ValidationRuleException(TermLoanConstants.NO_INTEREST_DETAILS);
        }
        return list;
    }

    
    public HashMap interestCalcTermLoanAD(HashMap passDate,HashMap resultMap) throws Exception {
        
        String user=null;
       
        Double ROI = new Double(0);
        HashMap actListMap = new HashMap();
        HashMap depositMap = new HashMap();
        if (status != null) {
            status.setStatus(BatchConstants.STARTED);
        }
        Date curr_dt = null;
        Date actClosingDt = null;
        String oneMonthInterest = null;
        HashMap insertRecord = new HashMap();
        HashMap lastAppDt = new HashMap();
        String sourceScreen = "";
        Date MatDate=null;
   //     System.out.println("pass  ...Date1111####" + passDate);
        String behavelike = CommonUtil.convertObjToStr(passDate.get("BEHAVES_LIKE"));
      /*  if(passDate.containsKey("CALC_ON_MATURITY")){
        if(passDate.get("CALC_ON_MATURITY") != null && passDate.get("ACT_NUM")!= null && passDate.containsKey("ACT_NUM") && passDate.get("CALC_ON_MATURITY").equals("Y") && passDate.containsKey("CALC_ON_MATURITY")){
            depositMap.put("ACCT_NUM", CommonUtil.convertObjToStr(passDate.get("ACT_NUM")));
            List cumDepositlst = sqlMap.executeQueryForList("getExpiryDateForDepositLoan", depositMap);
            if(cumDepositlst != null && cumDepositlst.size() > 0){
                HashMap Depositlst = (HashMap)cumDepositlst.get(0);
                if(Depositlst != null && Depositlst.containsKey("MATURITY_DT")){
	                passDate.put("MATURITY_DATE", Depositlst.get("MATURITY_DT"));
	                interestsMap.put("MATURITY_DATE", Depositlst.get("MATURITY_DT"));
	                MatDate=(Date)Depositlst.get("MATURITY_DT");
                }
            }
            System.out.println("cumDepositlst" + cumDepositlst+" map "+passDate);

            System.out.println("passDate.get(CALC_ON_MATURITY)"+passDate.get("CALC_ON_MATURITY"));
            interestsMap.put("CALC_ON_MATURITY", passDate.get("CALC_ON_MATURITY"));
        }} */
        if (passDate.containsKey("USER_ID")) {
            user = CommonUtil.convertObjToStr(passDate.get("USER_ID"));
        }
      //  System.out.println("In user "+user);
        if (!(passDate.containsKey("PENAL_APP_PRINCIPAL") || passDate.containsKey("PENAL_APP_INTEREST"))) {
            List lstAppdt = sqlMap.executeQueryForList("GETLASTCALCDT", passDate);
            if (lstAppdt != null && lstAppdt.size() > 0) {
                lastAppDt = (HashMap) lstAppdt.get(0);
                passDate.putAll(lastAppDt);
            }
        }
        //        passDate.put("INTEREST_CALCULTION","INTEREST_CALCULTION");//for standard asset only
        chekdateLast = new Date();
         chekdateLast = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(passDate.get("DATE_TO")));
         ch_to_Date = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(passDate.get("DATE_TO")));
      //   System.out.println("chekdateLast------>"+chekdateLast);
//                chekdateLast= holiydaychecking(whereMap);
            if (DateUtil.dateDiff(chekdateLast, currDt) < 0) {
                chekdateLast = currDt;
            }
        //    System.out.println("chekdateLast-2222----->"+chekdateLast);
        if (passDate.containsKey("CURR_DATE") && passDate.get("CURR_DATE") != null) {
            //            String currdt=CommonUtil.convertObjToStr(passDate.get("CURR_DATE"));
            //            curr_dt=DateUtil.getDateMMDDYYYY(currdt);
            //            System.out.println("currdate### before"+curr_dt);
            curr_dt = (Date) passDate.get("CURR_DATE");
         //   System.out.println("currdate### 22222after" + curr_dt);
            //            if(passDate.containsKey("LOAN_ACCOUNT_CLOSING"))
            //            chekdateLast=DateUtil.addDaysProperFormat(curr_dt,-1);
            //            else
            if (passDate.containsKey("LOAN_ACCOUNT_CLOSING") && passDate.get("LOAN_ACCOUNT_CLOSING") != null) {
                chekdateLast = (Date) DateUtil.addDaysProperFormat(curr_dt, -1);
         //       System.out.println("checkdatelast66666####" + chekdateLast);
                //                passDate.remove("INTEREST_CALCULTION");
            }
            //            if(passDate.containsKey("CHARGESUI"))
            //                chekdateLast =curr_dt;
        }

        List allRecords = null;

        HashMap penalInterest = new HashMap();

        if (CommonUtil.convertObjToStr(passDate.get("BEHAVES_LIKE")).equals("OD")) {
            penalInterest = new HashMap();
            penalInterest.putAll(passDate);
            allRecords = sqlMap.executeQueryForList("getAllLoanRecordOD", passDate);
        } else {
            penalInterest = new HashMap();
            allRecords = sqlMap.executeQueryForList("getAllLoanRecord", passDate);

            penalInterest.putAll(passDate);
        }
       //  System.out.println("allRecords INJ 8888-----> "+allRecords);
        HashMap productRecord = new HashMap();
        if (allRecords != null && allRecords.size() > 0) {
            for (int j = 0; j < allRecords.size(); j++) {
                try {


                  //  do {
                        if (!(passDate.containsKey("LOAN_ACCOUNT_CLOSING") && passDate.get("LOAN_ACCOUNT_CLOSING") != null)) {
                            sqlMap.startTransaction();
                        }
                        
                        HashMap returnMap = new HashMap();
                        HashMap singleRecord = new HashMap();
                        HashMap penalInterestMap = new HashMap();
                        HashMap cumDepositMap = null;
                        String intGetFrom = null;
                        productRecord = new HashMap();
                        productRecord = (HashMap) allRecords.get(j);
                       // System.out.println("productRecord Record 44444 ### : " + productRecord);
						if (passDate.containsKey("PRODUCT_RECORD_PENAL") && passDate.get("PRODUCT_RECORD_PENAL") != null) {
                        	productRecord.put(PENALTY, PENALTY);
                    	}
                        intGetFrom = CommonUtil.convertObjToStr(productRecord.get("INT_GET_FROM"));
//                    //OTS interest calculation
//                    if(productRecord.containsKey("OTS") && CommonUtil.convertObjToStr(productRecord.get("OTS")).equals("Y")){
//                        returnMap= calculateOTSInterstDetails(productRecord);
//                        return returnMap;
//                  //  }
                      //  System.out.println("productRecord innnnnnn: "+productRecord);
	                    singleRecord.put("ACT_NUM", productRecord.get("ACCT_NUM"));
	                    singleRecord.put("LAST_INT_CALC_DT", productRecord.get("LAST_INT_CALC_DT"));
	                    if (!(passDate.containsKey("PRODUCT_RECORD") && passDate.get("PRODUCT_RECORD") != null)) {
	                 //       System.out.println("singleRecord : "+singleRecord);
	                        sqlMap.executeUpdate("deleteLoanInterestTMP", singleRecord);
	                    }

                        //for prematurecloser 30 days interest
                   /*     if (passDate.containsKey("PREMATURE_ONEMONTH_INT")) {
                            int prematurePeriod = CommonUtil.convertObjToDouble(productRecord.get("PREMATURE_FREQ")).intValue();
//                        if(DateUtil.dateDiff((Date)productRecord.get("ACCT_OPEN_DT"),curr_dt)<=prematurePeriod) {
                            Date actOpenDt = (Date) productRecord.get("ACCT_OPEN_DT");
                            int addMethod = CommonUtil.convertObjToInt(periodStringMap.get(CommonUtil.convertObjToStr(productRecord.get("PREMATURE_PERIOD"))));
                            Date addedDate = DateUtil.addDays(actOpenDt, prematurePeriod, addMethod, false);
                            System.out.println("!!!!### curr_dt:" + curr_dt + " / addedDate:" + addedDate);
                            if (DateUtil.dateDiff(curr_dt, addedDate) >= 1) {
//                            if(productRecord.containsKey("LAST_INT_CALC_DT") && productRecord.get("LAST_INT_CALC_DT") !=null) {
                                int premaPeriod = (int) prematurePeriod;
//                                chekdateLast=DateUtil.addDays((Date) productRecord.get("LAST_INT_CALC_DT"),premaPeriod,true);
                                oneMonthInterest = CommonUtil.convertObjToStr(productRecord.get("PREMATURE_INT_CALC_AMT"));
//                                System.out.println("oneMonthInterest###"+oneMonthInterest+"chekdateLast"+chekdateLast);
                                System.out.println("oneMonthInterest###" + oneMonthInterest + " / curr_dt" + curr_dt);
                                interestList = getInterestDetails(productRecord, passDate);

                                if (oneMonthInterest.equals("LOANSANCTIONAMT")) {
                                    return (oneMonthInterstCalculationforSanctionAmt(productRecord, curr_dt));
                                } else if (oneMonthInterest.equals("LOANOUTSTANDINGAMT")) {
                                    return (oneMonthInterstCalculationforOutstandingAmt(productRecord, curr_dt));
                                }
//                            }
                            }

                        } */

                        //for return asset status roi
                        insertRecord.put("ASSET_STATUS", productRecord.get("ASSET_STATUS"));
                        interestList = getInterestDetails(productRecord, passDate);
                        if (interestList != null && interestList.size() > 0) {
                            HashMap map = (HashMap) interestList.get(0);
                       //     System.out.println("map   mm :" + map);
                            insertRecord.put("ROI", map.get("INTEREST"));
                        }
                    //      System.out.println("insertRecord INJ 7777-----> "+insertRecord);
  /*
                        if (productRecord.containsKey("SECRETARIAT_INT") && CommonUtil.convertObjToStr(productRecord.get("SECRETARIAT_INT")).equals("Y")) {
                            return emiCalculaterforKerla(productRecord, interestList);

                        }
                        //FOR EMI INTEREST CALCULATION
                        if (productRecord != null && productRecord.containsKey("INSTALL_TYPE") && productRecord.get("INSTALL_TYPE") != null
                                && // ((productRecord.get("INSTALL_TYPE").equals("UNIFORM_PRINCIPLE_EMI") && productRecord.get("EMI_IN_SIMPLEINTREST").equals("Y")) ) && (!productRecord.get("INSTALL_TYPE").equals("LUMP_SUM"))
                                (!(productRecord.get("INSTALL_TYPE").equals("UNIFORM_PRINCIPLE_EMI"))) && (!productRecord.get("INSTALL_TYPE").equals("LUMP_SUM"))
                                && productRecord.containsKey("AS_CUSTOMER_COMES") && productRecord.get("AS_CUSTOMER_COMES").equals("Y") && (!productRecord.get("INSTALL_TYPE").equals("EYI"))) {
                            productRecord.put("NO_OF_INSTALLMENT", passDate.get("NO_OF_INSTALLMENT"));
                            if (passDate.containsKey("LOAN_EMI_CLOSE") && passDate.get("LOAN_EMI_CLOSE") != null) {
                                productRecord.put("LOAN_EMI_CLOSE", passDate.get("LOAN_EMI_CLOSE"));
                            }
                            productRecord.put("NO_OF_INSTALLMENT", passDate.get("NO_OF_INSTALLMENT"));
                            return emiCalculater(productRecord, interestList, passDate);
                        }

                        //END EMI INTEREST CALCULATION


                        List cumDepositlst = sqlMap.executeQueryForList("getDepositBehavesforLoan", productRecord);
                        if (cumDepositlst != null && cumDepositlst.size() > 0) {
                            cumDepositMap = (HashMap) cumDepositlst.get(0);
                            if (passDate.containsKey("SOURCE_SCREEN") && passDate.get("SOURCE_SCREEN") != null) {
                                sourceScreen = CommonUtil.convertObjToStr(passDate.get("SOURCE_SCREEN"));
                            } else {
                                sourceScreen = "";
                            }

                            // kerla co oeprative bank theny want to collect till deposit matruiry date or upto date  for that purpose added condition
                            if (sourceScreen.equals("LOAN_CLOSING")) {
                                if (productRecord.containsKey("INT_UPTO_MAT_DT") && CommonUtil.convertObjToStr(productRecord.get("INT_UPTO_MAT_DT")).equals("Y")) {
                                    if (cumDepositMap.containsKey("MATURITY_DT") && cumDepositMap.get("MATURITY_DT") != null) {
                                        if (DateUtil.dateDiff((Date) cumDepositMap.get("MATURITY_DT"), curr_dt) > 0) {
                                            chekdateLast = (Date) DateUtil.addDaysProperFormat((Date) cumDepositMap.get("MATURITY_DT"), 0);
                                            if(behavelike!=null && behavelike.equals("LOANS_AGAINST_DEPOSITS")){
                                                chekdateLast = (Date) DateUtil.addDaysProperFormat((Date) cumDepositMap.get("MATURITY_DT"), -1);
                                            }
                                            productRecord.put("END_DT", chekdateLast);
                                        }
                                        System.out.println("Till Maturity date ######" + chekdateLast);
                                    }

                                }//kerla  validation end
                            }
                            // kerla co oeprative bank theny want to collect till deposit matruiry date or upto date  for that purpose added condition
                            if (sourceScreen.equals("DEPOSIT_CLOSING")) {
                                if (productRecord.containsKey("INT_UPTO_DEPOSIT_MAT_DT") && CommonUtil.convertObjToStr(productRecord.get("INT_UPTO_DEPOSIT_MAT_DT")).equals("Y")) {
                                    if (cumDepositMap.containsKey("MATURITY_DT") && cumDepositMap.get("MATURITY_DT") != null) {
                                        if (DateUtil.dateDiff((Date) cumDepositMap.get("MATURITY_DT"), curr_dt) > 0) {
                                            chekdateLast = (Date) DateUtil.addDaysProperFormat((Date) cumDepositMap.get("MATURITY_DT"), 0);
                                             if(behavelike!=null && behavelike.equals("LOANS_AGAINST_DEPOSITS")){
                                                chekdateLast = (Date) DateUtil.addDaysProperFormat((Date) cumDepositMap.get("MATURITY_DT"), -1);
                                            }
                                            productRecord.put("END_DT", chekdateLast);
                                        }
                                        System.out.println("Till deposit Maturity date ######" + chekdateLast);
                                    }

                                }//kerla  validation end
                            }

//edited by Nidhin
                            String deposit_behaves = CommonUtil.convertObjToStr(cumDepositMap.get("BEHAVES_LIKE"));
                            if (deposit_behaves.equals("CUMMULATIVE") || deposit_behaves.equals("RECURRING")) {
                                productRecord.put("PRINICIPAL", cumDepositMap.get("LOAN_BALANCE_PRINCIPAL"));
                                if(interestsMap.containsKey("MATURITY_DATE") && interestsMap.get("MATURITY_DATE") != null && interestsMap.containsKey("CALC_ON_MATURITY") && interestsMap.get("CALC_ON_MATURITY") != null)
                                if(interestsMap.get("CALC_ON_MATURITY").equals("Y")){
                                    productRecord.put("MATURITY_DATE", interestsMap.get("MATURITY_DATE"));
                                }    
                                returnMap = cumalitiveDepositAsAnWhen(productRecord, passDate);
                                returnMap.putAll(lastAppDt);
                                return returnMap;
                            }
                        }

                        if (passDate.containsKey("DEPOSIT_PREMATURE_CLOSER")) {
                            if (productRecord.containsKey("AS_CUSTOMER_COMES")
                                    && CommonUtil.convertObjToStr(productRecord.get("AS_CUSTOMER_COMES")).equals("Y")) {
                                prematureLTD = true;
                                passDate.remove("DEPOSIT_PREMATURE_CLOSER");
                            }
                        }
                        if (passDate.containsKey("DEPOSIT_PREMATURE_CLOSER") && prematureLTD == false) {

                            returnMap = prematureDepositClosingInt(productRecord, passDate);
                            return returnMap;
                        }  */
                        if (productRecord.containsKey("LAST_INT_CALC_DT") && productRecord.get("LAST_INT_CALC_DT") != null && DateUtil.dateDiff((Date) productRecord.get("LAST_INT_CALC_DT"), chekdateLast) == 0) {
                            continue;
                        }
                        //for penal
                        if (CommonUtil.convertObjToStr(productRecord.get("PENAL_APPL")).equals("Y") && (!CommonUtil.convertObjToStr(passDate.get("BEHAVES_LIKE")).equals("OD"))) {
                            penalInterest.putAll(productRecord);
                           // penalInterestMap = interestCalculationTL(penalInterest);
                            if (passDate.containsKey("FORPENAL_INTEREST_PURPOSE") && passDate.get("FORPENAL_INTEREST_PURPOSE") != null) {
                          //      System.out.println("penalinterest@@@@@@@" + penalInterestMap);
                                return penalInterestMap;
                            }

                            //                    System.out.println("totpenalint$######"+totpenalint);
                        }
                      //  System.out.print("singleRecordforinterestproduct" + productRecord);
                        singleRecord.put("ACT_NUM", productRecord.get("ACCT_NUM"));
                        if (passDate.containsKey("LOAN_ACCOUNT_CLOSING")) {
							singleRecord.put("CURR_DT", currDt);
                            singleRecord.put("BRANCH_CODE", branch);
                            singleRecord.put("USER_ID", user);
                            //                    List lst=sqlMap.executeQueryForList("selectLoanInterestTMP",singleRecord);
                            //                    if( lst!=null && lst.size()>0)
                            //sqlMap.executeUpdate("deleteLoanInterestTMP", singleRecord);
                        }
                        // The following block added by Rajesh because after going inside interestCalculationTL() chekdateLast making -1
                        if (passDate.containsKey("LOAN_ACCOUNT_CLOSING") && passDate.get("LOAN_ACCOUNT_CLOSING") != null) {
                            chekdateLast = (Date) DateUtil.addDaysProperFormat(curr_dt, -1);
                       //     System.out.println("checkdatelast####" + chekdateLast);
                        /*    if (sourceScreen.equals("LOAN_CLOSING")) {
                                if (productRecord.containsKey("INT_UPTO_MAT_DT") && CommonUtil.convertObjToStr(productRecord.get("INT_UPTO_MAT_DT")).equals("Y")) {
                                    if (cumDepositMap.containsKey("MATURITY_DT") && cumDepositMap.get("MATURITY_DT") != null) {
                                        if (DateUtil.dateDiff((Date) cumDepositMap.get("MATURITY_DT"), curr_dt) > 0) {
                                            chekdateLast = (Date) DateUtil.addDaysProperFormat((Date) cumDepositMap.get("MATURITY_DT"), 0);
                                            if(behavelike!=null && behavelike.equals("LOANS_AGAINST_DEPOSITS")){
                                                chekdateLast = (Date) DateUtil.addDaysProperFormat((Date) cumDepositMap.get("MATURITY_DT"), -1);
                                            }
                                            productRecord.put("END_DT", chekdateLast);
                                        }
                                        System.out.println("Till Maturitydate last ######" + chekdateLast);
                                    }

                                }//kerla  validation end
                            } 
                            if (sourceScreen.equals("DEPOSIT_CLOSING")) {
                                if (productRecord.containsKey("INT_UPTO_DEPOSIT_MAT_DT") && CommonUtil.convertObjToStr(productRecord.get("INT_UPTO_DEPOSIT_MAT_DT")).equals("Y")) {
                                    if (cumDepositMap.containsKey("MATURITY_DT") && cumDepositMap.get("MATURITY_DT") != null) {
                                        if (DateUtil.dateDiff((Date) cumDepositMap.get("MATURITY_DT"), curr_dt) > 0) {
                                            chekdateLast = (Date) DateUtil.addDaysProperFormat((Date) cumDepositMap.get("MATURITY_DT"), 0);
                                            if(behavelike!=null && behavelike.equals("LOANS_AGAINST_DEPOSITS")){
                                                chekdateLast = (Date) DateUtil.addDaysProperFormat((Date) cumDepositMap.get("MATURITY_DT"), -1);
                                            }
                                            productRecord.put("END_DT", chekdateLast);
                                        }
                                        System.out.println("Till Maturitydate last ######" + chekdateLast);
                                    }

                                }//kerla  validation end
                            }*/
                        }
                        Date tempDt = (Date) currDt.clone();
                        tempDt.setDate(chekdateLast.getDate());
                        tempDt.setMonth(chekdateLast.getMonth());
                        tempDt.setYear(chekdateLast.getYear());
                        singleRecord.put("CURR_DATE", DateUtil.addDays(tempDt, 0));//chekdateLast
                  //      System.out.println("singleRecord@@@" + singleRecord);
                        //                else
                        //                    singleRecord.put("CURR_DATE",passDate.get("CURR_DATE"));

                        Date acOpDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productRecord.get("ACCT_OPEN_DT")));
                        Date stDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productRecord.get("LAST_INT_CALC_DT")));
                        //               if(!passDate.containsKey("LOAN_ACCOUNT_CLOSING"))
                        // The following if block commented because it's calculating retrospective interest also & one day interest more also
//                    if(acOpDt!=null && DateUtil.dateDiff(acOpDt,stDt)==0){
//                        tempDt=(Date)currDt.clone();
//                        tempDt.setDate(stDt.getDate());
//                        tempDt.setMonth(stDt.getMonth());
//                        tempDt.setYear(stDt.getYear());
//                        singleRecord.put("START_DATE",tempDt);//stDt
//                    }
//                    else{
                        stDt = DateUtil.addDaysProperFormat(stDt, 1);
//                        tempDt = (Date) currDt.clone();
//                        tempDt.setDate(stDt.getDate());
//                        tempDt.setMonth(stDt.getMonth());
//                        tempDt.setYear(stDt.getYear());
                        tempDt=CommonUtil.getProperDate(currDt, stDt);
                        singleRecord.put("START_DATE", tempDt);//stDt
//                    }
                       /* if (prematureLTD) {//<--this is for as an only passDate.containsKey("DEPOSIT_PREMATURE_CLOSER")
                            tempDt = (Date) currDt.clone();
                            tempDt.setDate(acOpDt.getDate());
                            tempDt.setMonth(acOpDt.getMonth());
                            tempDt.setYear(acOpDt.getYear());
                            singleRecord.put("START_DATE", tempDt);//acOpDt
                        }*/
                        //                 Date acOpDt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productRecord.get("ACCT_OPEN_DT")));

                        //                stDt=DateUtil.addDaysProperFormat(stDt,1);
                        //                singleRecord.put("START_DATE",stDt);
                        //                singleRecord.put("START_DATE",productRecord.get("LAST_INT_CALC_DT"));
                        interestList = null;
                        //                if(singleRecord!=null)//  only for testing purpose
                        //                return singleRecord;
                        // The following line commented because not going inside RetrasPectiveInterestCalculation() method
//                    interestList= getInterestDetails(productRecord,passDate);
                    //    System.out.println("singleRecord333" + singleRecord);

//                    // The following block added to avoid unnecessary Retrospective int calculation by Rajesh
//                    Date last_int_calc_dt = (Date) productRecord.get("LAST_INT_CALC_DT");
//                    tempDt=(Date)currDt.clone();
//                    tempDt.setDate(last_int_calc_dt.getDate());
//                    tempDt.setMonth(last_int_calc_dt.getMonth());
//                    tempDt.setYear(last_int_calc_dt.getYear());
//                    productRecord.put("LAST_INT_CALC_DT", DateUtil.addDays(tempDt,1));
//                    // End (further one line added below RetrasPectiveInterestCalculation call

                        //retraspective
//                    double finalRetraspectiveAmt=RetrasPectiveInterestCalculation(productRecord,passDate,singleRecord);
                        double finalRetraspectiveAmt = 0;

//                    productRecord.put("LAST_INT_CALC_DT", last_int_calc_dt);

                        interestList = getInterestDetails(productRecord, passDate);
                     //   System.out.println("singleRecord444" + singleRecord);
                        singleRecord.put("START_DATE", paramMap.get("START"));
                        List dailyBalOD = null;

                        if (CommonUtil.convertObjToStr(passDate.get("BEHAVES_LIKE")).equals("OD")) {
                            //Condition added for old qury
//                            System.out.println("ACC OP DATE--->"+resultMap.get("DEPOSIT_DT") +"STTT DATE--->"+resultMap.get("START") +
//                                    "ENDD DATE -->"+resultMap.get("END"));
                             Date accOpenDate=getProperFormatDate(resultMap.get("DEPOSIT_DT"));
                             Date start=getProperFormatDate(resultMap.get("START"));
                             Date end=getProperFormatDate(resultMap.get("END"));
                           //  System.out.println("start--->"+start +"end--->"+end +"accOpenDate-->"+accOpenDate);
                             HashMap dataMap=new HashMap();
                             dataMap.put("ACT_NUM",productRecord.get("ACCT_NUM"));
                             dataMap.put("FROM_DT",start);
                             dataMap.put("PROD_ID",passDate.get("PROD_ID"));
                             List fromDateExits= ServerUtil.executeQuery("fromDateExists", dataMap);
                             int countDate=0;
                             if(fromDateExits!=null && fromDateExits.size()>0){
                                 HashMap dateExits = (HashMap)fromDateExits.get(0);
                                 if(dateExits!=null && dateExits.containsKey("COUNT")){
                                     countDate=CommonUtil.convertObjToInt(dateExits.get("COUNT"));
                                 }
                             }
                            //  System.out.println("date checkkkiiii--->"+start.compareTo(accOpenDate) * accOpenDate.compareTo(end));
                              // System.out.println("countDate-5555-->"+countDate +"  JJJJ--->"+productRecord.get("AS_CUSTOMER_COMES"));
                           if ( (productRecord.containsKey("AS_CUSTOMER_COMES")
                                    && CommonUtil.convertObjToStr(productRecord.get("AS_CUSTOMER_COMES")).equals("Y")) ||
                                   (start.compareTo(accOpenDate) * accOpenDate.compareTo(end) > 0) ||
                                  (productRecord.containsKey("AS_CUSTOMER_COMES")
                                    && CommonUtil.convertObjToStr(productRecord.get("AS_CUSTOMER_COMES")).equals("N") && countDate>0) ) {
                               dailyBalOD = sqlMap.executeQueryForList("dayEndBalanceForIntCalcAD", singleRecord);
                           }
                           else{
                               dailyBalOD = sqlMap.executeQueryForList("dayEndBalanceForIntCalcADNew", singleRecord);
                           }
                        } 
                      /*  else if (CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("SIMPLE") && CommonUtil.convertObjToStr(productRecord.get("LOAN_BALANCE")).equals("N")
                                && CommonUtil.convertObjToStr(productRecord.get("SUBSIDY_RECEIVED_DT")).equals("N")) {
                            dailyBalOD = sqlMap.executeQueryForList("dayEndBalanceForSubsidyIntCalc_simple_TL", singleRecord);
                        } else if (CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("COMPOUND") && CommonUtil.convertObjToStr(productRecord.get("LOAN_BALANCE")).equals("N")
                                && CommonUtil.convertObjToStr(productRecord.get("SUBSIDY_RECEIVED_DT")).equals("N")) {
                            dailyBalOD = sqlMap.executeQueryForList("dayEndBalanceForSubsidyIntCalcTL", singleRecord);
                        } else if (CommonUtil.convertObjToStr(productRecord.get("INTEREST_TYPE")).equals("FLAT_RATE") && CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("SIMPLE") || (oneMonthInterest != null && oneMonthInterest.equals("LOANSANCTIONAMT"))) {
                            dailyBalOD = sqlMap.executeQueryForList("dayEndBalanceForIntCalcFlat_TL", singleRecord);
                        } else if (CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("COMPOUND")) {
                            dailyBalOD = sqlMap.executeQueryForList("dayEndBalanceForIntCalcTL", singleRecord);
                        } else if (CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("SIMPLE")) {
                            dailyBalOD = sqlMap.executeQueryForList("dayEndBalanceForIntCalc_simple_TL", singleRecord);
                        } */
                        //                    System.out.println("DAILYbALANCEOD&&&&&&^^"+dailyBalOD);
                        //                dailyBalOD=null; testing penal
                        double totInterest = 0;
                        HashMap hashDailyMap = new HashMap();
                        Date firstDate = new Date();
                        String firstDt = null;
                        Date dayEndDt = null;
                        if (dailyBalOD != null && dailyBalOD.size() > 0) {
                            for (int i = 0; i < dailyBalOD.size(); i++) {
                                //                        System.out.println("#$#$"+dailyBalOD);
                                hashDailyMap = (HashMap) dailyBalOD.get(i);
                       //         System.out.println("hashDailyMapbbbbbbb=========" + hashDailyMap);
								//String dayEnddt =CommonUtil.convertObjToStr(hashDailyMap.get("DAY_END_DT"));
                                String newstr = CommonUtil.convertObjToStr(hashDailyMap.get("DAY_END_DT"));
                                SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
								//SimpleDateFormat format = new SimpleDateFormat("EE MMM dd hh:mm:ss z yyyy");
                                Calendar c = Calendar.getInstance();
                                c.setTime(format1.parse(newstr));

                               //  dayEndDt = DateUtil.getDateWithoutMinitues(CommonUtil.convertObjToStr(hashDailyMap.get("DAY_END_DT")));
                               // dayEndDt=CommonUtil.getProperDate(currDt,DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hashDailyMap.get("DAY_END_DT"))));
                                dayEndDt = DateUtil.getDateWithoutMinitues(c.getTime());
                      //          System.out.println("dayEndDt ----------- :"+dayEndDt);
                                Date nextDt = DateUtil.getDateWithoutMinitues(CommonUtil.convertObjToStr(hashDailyMap.get("NEXT_DT")));
                                double curr_amt = 0;
                                double next_amt = 0;
                        //        System.out.println("DateUtil.dateDiff(dayEndDt,chekdateLast)====" + DateUtil.dateDiff(dayEndDt, chekdateLast) + "chekdateLast" + chekdateLast + "dayEndDt" + dayEndDt + "passDate.containsKey()" + passDate.containsKey("CHARGESUI"));
                                if (passDate.containsKey("CHARGESUI") && DateUtil.dateDiff(dayEndDt, chekdateLast) < 0) {
                                    break;
                                }

                                //                    if(CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("COMPOUND")){
                                curr_amt = CommonUtil.convertObjToDouble(hashDailyMap.get("AMT")).doubleValue();
                                next_amt = CommonUtil.convertObjToDouble(hashDailyMap.get("NEXT_AMT")).doubleValue();
                                //                    }else{
                                //                        curr_amt=CommonUtil.convertObjToDouble(hashDailyMap.get("PRINCIPAL")).doubleValue();
                                //                        next_amt=CommonUtil.convertObjToDouble(hashDailyMap.get("NEXT_PRINCIPAL")).doubleValue();
                                //                    }
                                //                            System.out.println("productrecord####"+productRecord+"curramt&&&&$$"+curr_amt+"nextamt###"+next_amt);
                                
//                                if (curr_amt == next_amt && firstDt == null) {
                                    firstDt = CommonUtil.convertObjToStr(hashDailyMap.get("DAY_END_DT"));
                                    firstDate = DateUtil.getDateMMDDYYYY(firstDt);
                            //        System.out.println("inside the firstDt22222##--------" + firstDt +"curr_amt--->"+curr_amt);
                                    //commented by chithra for mantis: 10368: Advances Credit Interest Processing is not working
                                    
//                                    if (curr_amt > 0 && CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("COMPOUND")) {//bbbb  < to >
//                                        firstDt = null;
//                                        System.out.println("inside the 6666666firstDt22222##--------" + firstDt +"curr_amt--->"+curr_amt);
//                                    }
//                                    System.out.println("inside the firstdate##--------" + firstDt);
//
//                                }
//                                if ((CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("COMPOUND") && curr_amt > 0//bbbb < to >
//                                        && curr_amt != next_amt) || (CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("SIMPLE") && curr_amt != next_amt)
//                                        || i + 1 == dailyBalOD.size()) {// || (passDate.containsKey("CHARGESUI")&& DateUtil.dateDiff(nextDt,chekdateLast) >=0 && curr_amt<0)) {
                                if ((CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("COMPOUND") && curr_amt > 0)||
                                        (CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("SIMPLE") && curr_amt != next_amt)) {
                                    hashDailyMap.putAll(productRecord);
                          //          System.out.println("firstDt 00000---->"+firstDt);
                                    if (firstDt != null && firstDt.length() > 0) {
                                        hashDailyMap.put("START_DT", firstDate);
                                        //                            hashDailyMap.put("END_DT",hashDailyMap.get("DAY_END_DT"));

                                        //                                if (i+1==dailyBalOD.size())   //now onlycommented becase last date calculation done by out of for loop
                                        //                                    hashDailyMap.put("END_DT",hashDailyMap.get("NEXT_DT"));
                                        //                                else
//System.out.println("fDateUtil.dateDiff(dayEndDt, nextDt)---->"+DateUtil.dateDiff(dayEndDt, nextDt));
                                        if (DateUtil.dateDiff(dayEndDt, nextDt) == 0) {
                                            hashDailyMap.put("END_DT", hashDailyMap.get("NEXT_DT"));
                                        } else {

                                            hashDailyMap.put("END_DT", DateUtil.addDaysProperFormat(nextDt, -1));
                                            if (i + 1 == dailyBalOD.size() && passDate.containsKey("LOAN_ACCOUNT_CLOSING")) {
                                                hashDailyMap.put("END_DT", DateUtil.addDaysProperFormat(curr_dt, -1));
                                                if ((productRecord.containsKey("INT_UPTO_DEPOSIT_MAT_DT") && CommonUtil.convertObjToStr(productRecord.get("INT_UPTO_DEPOSIT_MAT_DT")).equals("N"))
                                                        || (productRecord.containsKey("INT_UPTO_MAT_DT") && CommonUtil.convertObjToStr(productRecord.get("INT_UPTO_MAT_DT")).equals("N"))) {
                                                    hashDailyMap.put("END_DT", DateUtil.addDaysProperFormat(curr_dt, -1));
                                                }
                                                if (sourceScreen.equals("LOAN_CLOSING")) {    //////////////////------------------------------
                                                    if (productRecord.containsKey("INT_UPTO_MAT_DT") && CommonUtil.convertObjToStr(productRecord.get("INT_UPTO_MAT_DT")).equals("Y")) {
                                                        if (DateUtil.dateDiff((Date) cumDepositMap.get("MATURITY_DT"), curr_dt) > 0)
                                                        	hashDailyMap.put("END_DT", DateUtil.addDaysProperFormat(chekdateLast, 0));
                                                        else
                                                          	hashDailyMap.put("END_DT", DateUtil.addDaysProperFormat(curr_dt, -1));   
                                                        
                                                    }
                                                } else if (sourceScreen.equals("DEPOSIT_CLOSING")) {
                                                    if (productRecord.containsKey("INT_UPTO_DEPOSIT_MAT_DT") && CommonUtil.convertObjToStr(productRecord.get("INT_UPTO_DEPOSIT_MAT_DT")).equals("Y")) {
                                                        hashDailyMap.put("END_DT", DateUtil.addDaysProperFormat(chekdateLast, 0));
                                                    }
                                                }
                                            }
                                            //                                    hashDailyMap.put("NO_ROUND_OFF_INT","NO_ROUND_OFF_INT");


                                            hashDailyMap.put("NO_ROUND_OFF_INT", "NO_ROUND_OFF_INT");
                                        }

                                    } else {
                                        hashDailyMap.put("START_DT", hashDailyMap.get("DAY_END_DT"));
                                        if ((chekdateLast != null && DateUtil.dateDiff(dayEndDt, chekdateLast) == 0)
                                                || DateUtil.dateDiff(dayEndDt, nextDt) == 0)//(dayEndDt.equals(chekdateLast))
                                        {
                                            hashDailyMap.put("END_DT", hashDailyMap.get("NEXT_DT"));
                                        } else {
                                            hashDailyMap.put("END_DT", DateUtil.addDaysProperFormat(nextDt, -1));
                                        }
                                        hashDailyMap.put("NO_ROUND_OFF_INT", "NO_ROUND_OFF_INT");
                                    }
                                    if (i + 1 == dailyBalOD.size() && passDate.containsKey("LOAN_ACCOUNT_CLOSING")) {
                                        hashDailyMap.put("END_DT", DateUtil.addDaysProperFormat(curr_dt, -1));
                                        if (sourceScreen.equals("LOAN_CLOSING")) {    //////////////////------------------------------
                                            if (productRecord.containsKey("INT_UPTO_MAT_DT") && CommonUtil.convertObjToStr(productRecord.get("INT_UPTO_MAT_DT")).equals("Y")) {
                                                hashDailyMap.put("END_DT", DateUtil.addDaysProperFormat(chekdateLast, 0));
                                                if(behavelike!=null && behavelike.equals("LOANS_AGAINST_DEPOSITS")){
                                                	chekdateLast = (Date) DateUtil.addDaysProperFormat(chekdateLast, -1);
                                            	}
                                            }
                                        } else if (sourceScreen.equals("DEPOSIT_CLOSING")) {
                                            if (productRecord.containsKey("INT_UPTO_DEPOSIT_MAT_DT") && CommonUtil.convertObjToStr(productRecord.get("INT_UPTO_DEPOSIT_MAT_DT")).equals("Y")) {
                                                hashDailyMap.put("END_DT", DateUtil.addDaysProperFormat(chekdateLast, 0));
                                            }
                                        }
                                    }
                                  //  System.out.println("hashdailymap##77777##" + hashDailyMap);//bbbb   <0 and - remove else
                                    if ((CommonUtil.convertObjToDouble(hashDailyMap.get("AMT")).doubleValue() > 0 && CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("COMPOUND"))
                                            || (CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("SIMPLE")
                                            && CommonUtil.convertObjToDouble(hashDailyMap.get("AMT")).doubleValue() > 0)) {  //PRINCIPAL
                                  //     System.out.println("HashDailyMap##444444444444###" + hashDailyMap.get("AMT"));
                                        if (CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("SIMPLE")) {
                                            hashDailyMap.put("PRINCIPAL_BAL", hashDailyMap.get("AMT"));
                                        } else {
                                            hashDailyMap.put("PRINCIPAL_BAL", new Double(CommonUtil.convertObjToDouble(hashDailyMap.get("AMT")).doubleValue()));
                                        }
                                        hashDailyMap.put("PROD_ID", passDate.get("PROD_ID"));
                                      //  System.out.println("HashDailyMap##66666###" + hashDailyMap);
                                        //                                Date nextdt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hashDailyMap.get("NEXT_DT")));
                                        //                                if(! (DateUtil.dateDiff(nextdt,chekdateLast)==0)){
                                        hashDailyMap.put("NO_ROUND_OFF_INT", "NO_ROUND_OFF_INT");
                                        //                                }
                                        HashMap resultInt = new HashMap();
                                        //                                    if(cumDepositMap !=null && cumDepositMap.containsKey("BEHAVES_LIKE") && (cumDepositMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")|| cumDepositMap.get("BEHAVES_LIKE").equals("RECURRING"))) {
                                        //                                        resultInt = cumalitiveDepositAsAnWhen(productRecord,passDate,hashDailyMap);
                                        //                                    }else
                                        if (hashDailyMap.containsKey("INTEREST_TYPE") && hashDailyMap.get("INTEREST_TYPE").equals("FLOATING_RATE")) {
                                            HashMap interestMap = new HashMap();
                                            Date nextroidt = null;
                                            boolean dayendOver = false;
                                            interestMap.put("FROM_DATE", productRecord.get("FROM_DATE"));   //hashDailyMap.get("START_DT"));
                                            interestMap.put("TO_DATE", productRecord.get("TO_DATE"));   //hashDailyMap.get("END_DT"));
                                            interestMap.put("PROD_ID", passDate.get("PROD_ID"));
                                            interestMap.put("CATEGORY_ID", productRecord.get("CATEGORY_ID"));
                                            interestMap.put("AMOUNT", productRecord.get("AMOUNT"));
                                            interestMap.put("DAYENDSTDT", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hashDailyMap.get("START_DT"))));
                                            interestMap.put("DAYENDEDDT", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hashDailyMap.get("END_DT"))));
                                            interestMap.put("ACCT_NUM", hashDailyMap.get("ACCT_NUM"));
                                            Date start_dt = (Date) hashDailyMap.get("START_DT");
                                            Date dayEndBalanceDt = (Date) hashDailyMap.get("END_DT");
                                            List resultInterstRate = null;
                                            if (intGetFrom.equals("PROD")) {
                                                resultInterstRate = sqlMap.executeQueryForList("getSelectProductTermLoanFlotingInterestMap", interestMap);
                                            } else {
                                                resultInterstRate = (List) sqlMap.executeQueryForList("getSelectAccountTermLoanFlotingInterestMap", interestMap);
                                            }
                                            //                                            resultInterstRate=sqlMap.executeQueryForList("getSelectProductTermLoanFlotingInterestMap",interestMap);
  //System.out.println("resultInterstRate INJ 7777-----> "+resultInterstRate);
                                            if (resultInterstRate == null || resultInterstRate.isEmpty()) {
                                                if (intGetFrom.equals("PROD")) {
                                                    resultInterstRate = sqlMap.executeQueryForList("getSelectProductDayEndTermLoanInterestMap", interestMap);
                                                } else {
                                                    resultInterstRate = (List) sqlMap.executeQueryForList("getSelectAccountTermLoanDayEndInterestMap", interestMap);
                                                }
                                            }
                                            if (resultInterstRate != null && resultInterstRate.size() > 0) {
                                                for (int k = 0; k < resultInterstRate.size(); k++) {
                                                    HashMap interestResultMap = (HashMap) resultInterstRate.get(k);
                                                    Date roifrom_dt = (Date) interestResultMap.get("FROM_DT");
                                                    Date roito_dt = (Date) interestResultMap.get("TO_DT");
                                                    if (DateUtil.dateDiff(start_dt, roifrom_dt) > 0) {
                                                        if (intGetFrom.equals("PROD")) {
                                                            resultInterstRate = sqlMap.executeQueryForList("getSelectProductDayEndTermLoanInterestMap", interestMap);
                                                        } else {
                                                            resultInterstRate = (List) sqlMap.executeQueryForList("getSelectAccountTermLoanDayEndInterestMap", interestMap);
                                                        }
                                        //                System.out.println("resultInterstRate!!777777!!!!" + resultInterstRate);
                                                        if (resultInterstRate != null && resultInterstRate.size() > 0) {
                                                            interestResultMap = (HashMap) resultInterstRate.get(0);
                                                            roifrom_dt = (Date) interestResultMap.get("FROM_DT");
                                                            roito_dt = (Date) interestResultMap.get("TO_DT");
                                                        } else {
                                                            return null;
                                                        }
                                                    }
                                                    if (DateUtil.dateDiff(start_dt, roifrom_dt) <= 0 && (roito_dt == null || DateUtil.dateDiff(roito_dt, (Date) hashDailyMap.get("END_DT")) <= 0)) {
                                                        interestList.clear();
                                                        interestList.add(resultInterstRate.get(k));
                                                    //    System.out.println("hashDailyMap INJ 55555555-----> "+hashDailyMap);
                                                        resultInt = calculateInterest(hashDailyMap);
                                                        if (resultInt != null) {
                                                            hashDailyMap.put(InterestCalculationConstants.INTEREST, resultInt.get(InterestCalculationConstants.INTEREST));
                                                            if (passDate.containsKey("LOAN_ACCOUNT_CLOSING")) {
                                                                insertLoanInteresttmp(hashDailyMap);
                                                            } else {
                                                                insertLoanInterest(hashDailyMap);
                                                            }
                                                        }
                                                    } else {
                                                        do {
                                                            if (roito_dt != null && DateUtil.dateDiff(dayEndBalanceDt, roito_dt) <= 0) {
                                                                hashDailyMap.put("END_DT", roito_dt);
                                                            } else {
                                                                hashDailyMap.put("END_DT", dayEndBalanceDt);
                                                                dayendOver = true;
                                                            }
                                                            if (resultInterstRate != null && resultInterstRate.size() > 0) {
                                                                //interestList.add(resultInterstRate.get(0));
                                                                interestList.clear();
                                                                interestList.add(resultInterstRate.get(k));
                                                            }
                                                     //       System.out.println("interestList======"+interestList +" resultInterstRate--->"+resultInterstRate +" hashDailyMap---->"+hashDailyMap);
                                                            resultInt = calculateInterest(hashDailyMap);
                                                           // System.out.println("resultInt4444======"+resultInt);
                                                            if (resultInt != null) {
                                                                hashDailyMap.put(InterestCalculationConstants.INTEREST, resultInt.get(InterestCalculationConstants.INTEREST));
                                                                if (passDate.containsKey("LOAN_ACCOUNT_CLOSING")) {
                                                                    insertLoanInteresttmp(hashDailyMap);
                                                                } else {
                                                                    insertLoanInterest(hashDailyMap);
                                                                }
                                                            }
                                                            if (roito_dt != null) {
                                                                roifrom_dt = (Date) DateUtil.addDays(roito_dt, 1);
                                                            }
                                                            hashDailyMap.put("START_DT", roifrom_dt);
                                                            hashDailyMap.put("END_DT", dayEndBalanceDt);
                                                            interestMap.put("DAYENDSTDT", hashDailyMap.get("START_DT"));
                                                            interestMap.put("DAYENDEDDT", hashDailyMap.get("END_DT"));
                                                            if (intGetFrom.equals("PROD")) {
                                                                resultInterstRate = sqlMap.executeQueryForList("getSelectProductDayEndTermLoanInterestMap", interestMap);
                                                            } else {
                                                                resultInterstRate = sqlMap.executeQueryForList("getSelectAccountTermLoanDayEndInterestMap", interestMap);
                                                            }
                                                            if (resultInterstRate != null && resultInterstRate.size() > 0) {
                                                                interestResultMap = (HashMap) resultInterstRate.get(0);
                                                                roifrom_dt = (Date) interestResultMap.get("FROM_DT");
                                                                if (interestResultMap.containsKey("TO_DT") && interestResultMap.get("TO_DT") != null) {
                                                                    roito_dt = (Date) interestResultMap.get("TO_DT");
                                                                } else {
                                                                    roito_dt = null;
                                                                }
                                                            } else {
                                                                roito_dt = null;
                                                            }
                                                            hashDailyMap.put("START_DT", roifrom_dt);
                                                        } while (DateUtil.dateDiff(roifrom_dt, dayEndBalanceDt) > 0 && dayendOver == false);
                                                        //                                                           Date nextroidt=(Date)DateUtil.addDays(roifrom_dt,1);
                                                        //                                                           if(DateUtil.dateDiff(nextroidt,dayEndBalanceDt)>0){
                                                        //                                                           hashDailyMap.put("START_DT",nextroidt);
                                                        //                                                           hashDailyMap.put("END_DT",dayEndBalanceDt);
                                                        //                                                            resultInt= calculateInterest(hashDailyMap);
                                                        //                                                }
                                                        //                                                    interestList
                                                        dayendOver = false;
                                                    }



                                                }
                                            }

                                        } else {
                                      //       System.out.println("hashDailyMap INJ 3666-----> "+hashDailyMap);
                                            resultInt = calculateInterest(hashDailyMap);
                                            hashDailyMap.put(InterestCalculationConstants.INTEREST, resultInt.get(InterestCalculationConstants.INTEREST));
                                            if (passDate.containsKey("LOAN_ACCOUNT_CLOSING")) {
                                                insertLoanInteresttmp(hashDailyMap);
                                            } else {
                                                insertLoanInterest(hashDailyMap);
                                            }
                                        }
                                        totInterest += CommonUtil.convertObjToDouble(resultInt.get(InterestCalculationConstants.INTEREST)).doubleValue();
                                        ROI = CommonUtil.convertObjToDouble(resultInt.get("ROI"));
                                        firstDt = null;
                                        //                             if(CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("COMPOUND") && next_amt>0){
                                        //                                firstDt=CommonUtil.convertObjToStr(hashDailyMap.get("NEXT_DT"));
                                        //                            }

                                    }
                                }
                                //                    if(CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("COMPOUND") && next_amt>0){
                                //                        firstDt=CommonUtil.convertObjToStr(hashDailyMap.get("NEXT_DT"));
                                //                        firstDate=DateUtil.getDateMMDDYYYY(firstDt);
                                //                        System.out.println("nextamt###"+next_amt);
                                //                    }

                            }
                        }
                      //  System.out.print(chekdateLast + "totInterest55555----" + totInterest);
                        Date dayenddt = null;
                        if (hashDailyMap.containsKey("END_DT") && hashDailyMap.get("END_DT") != null)//CHANGE DAY_END_DT
                        {
                            dayenddt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hashDailyMap.get("END_DT")));
                        }
                      //  System.out.println("lastdayend date 666666 :" + dayenddt);
                        //                 if( passDate.containsKey("BATCH_PROCESS") && DateUtil.dateDiff(chekdateLast, curr_dt)<0 || passDate.containsKey("CHARGESUI")){// ||
                        //                 (CommonUtil.convertObjToDouble(hashDailyMap.get("AMT")).doubleValue() == CommonUtil.convertObjToDouble(hashDailyMap.("NEXT_AMT")).doubleValue())){
                        if (dayenddt != null) {
                            if (DateUtil.dateDiff(chekdateLast, dayenddt) < 0) {//|| passDate.containsKey("CHARGESUI")
                     //           System.out.println("datediff##### 66666ttt" + DateUtil.dateDiff(chekdateLast, curr_dt));
                                hashDailyMap.putAll(productRecord);
                                if (firstDt != null) {
                                    hashDailyMap.put("START_DT", firstDate);
                                } else {
                                    hashDailyMap.put("START_DT", DateUtil.addDaysProperFormat(dayEndDt, 1));
                                }
                                //                    if(passDate.containsKey("CHARGESUI"))
                                //                         hashDailyMap.put("END_DT",passDate.get("DATE_TO"));
                                //                    else
                                hashDailyMap.put("END_DT", chekdateLast);
                                if ((CommonUtil.convertObjToDouble(hashDailyMap.get("AMT")).doubleValue() < 0 && CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("COMPOUND"))
                                        || (CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("SIMPLE") && CommonUtil.convertObjToDouble(hashDailyMap.get("AMT")).doubleValue() > 0) && 1 != dailyBalOD.size()) {
                                    if (CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("SIMPLE")) {
                                        hashDailyMap.put("PRINCIPAL_BAL", new Double(CommonUtil.convertObjToDouble(hashDailyMap.get("AMT")).doubleValue()));
                                    } else {
                                        hashDailyMap.put("PRINCIPAL_BAL", new Double(-CommonUtil.convertObjToDouble(hashDailyMap.get("AMT")).doubleValue()));
                                    }
                                    //                        hashDailyMap.put("PRINCIPAL_BAL",hashDailyMap.get("AMT"));
                                    hashDailyMap.put("PROD_ID", passDate.get("PROD_ID"));
                                    HashMap resultInt = null;
                                    //                                if(cumDepositMap !=null && cumDepositMap.containsKey("BEHAVES_LIKE") && (cumDepositMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")|| cumDepositMap.get("BEHAVES_LIKE").equals("RECURRING"))) {
                                    //                                          resultInt = cumalitiveDepositAsAnWhen(productRecord,passDate,hashDailyMap);
                                    //                                }else
                                    //floting rate of interest
                                    if (hashDailyMap.containsKey("INTEREST_TYPE") && hashDailyMap.get("INTEREST_TYPE").equals("FLOATING_RATE")) {
                                        HashMap interestMap = new HashMap();
                                        Date nextroidt = null;
                                        boolean dayendOver = false;
                                        interestMap.put("FROM_DATE", productRecord.get("FROM_DATE"));   //hashDailyMap.get("START_DT"));
                                        interestMap.put("TO_DATE", productRecord.get("TO_DATE"));   //hashDailyMap.get("END_DT"));
                                        interestMap.put("PROD_ID", passDate.get("PROD_ID"));
                                        interestMap.put("CATEGORY_ID", productRecord.get("CATEGORY_ID"));
                                        interestMap.put("AMOUNT", productRecord.get("AMOUNT"));
                                        interestMap.put("DAYENDSTDT", hashDailyMap.get("START_DT"));
                                        interestMap.put("DAYENDEDDT", hashDailyMap.get("END_DT"));
                                        interestMap.put("ACCT_NUM", hashDailyMap.get("ACCT_NUM"));
                                        Date start_dt = (Date) hashDailyMap.get("START_DT");
                                        Date dayEndBalanceDt = (Date) hashDailyMap.get("END_DT");
                                        List resultInterstRate = null;
                                        if (intGetFrom.equals("PROD")) {
                                            resultInterstRate = sqlMap.executeQueryForList("getSelectProductTermLoanFlotingInterestMap", interestMap);
                                        } else {
                                            resultInterstRate = (List) sqlMap.executeQueryForList("getSelectAccountTermLoanFlotingInterestMap", interestMap);
                                        }


                                        if (resultInterstRate == null || resultInterstRate.isEmpty()) {
                                            if (intGetFrom.equals("PROD")) {
                                                resultInterstRate = sqlMap.executeQueryForList("getSelectProductDayEndTermLoanInterestMap", interestMap);
                                            } else {
                                                resultInterstRate = sqlMap.executeQueryForList("getSelectAccountTermLoanDayEndInterestMap", interestMap);
                                            }
                                        }

                                        if (resultInterstRate != null && resultInterstRate.size() > 0) {
                                            for (int k = 0; k < resultInterstRate.size(); k++) {
                                                HashMap interestResultMap = (HashMap) resultInterstRate.get(k);
                                                Date roifrom_dt = (Date) interestResultMap.get("FROM_DT");
                                                Date roito_dt = (Date) interestResultMap.get("TO_DT");
                                                if (DateUtil.dateDiff(start_dt, roifrom_dt) > 0) {
                                                    resultInterstRate = sqlMap.executeQueryForList("getSelectProductDayEndTermLoanInterestMap", interestMap);
                                                    if (resultInterstRate != null && resultInterstRate.size() > 0) {
                                                        interestResultMap = (HashMap) resultInterstRate.get(0);
                                                        roifrom_dt = (Date) interestResultMap.get("FROM_DT");
                                                        roito_dt = (Date) interestResultMap.get("TO_DT");
                                                    }
                                                    if (DateUtil.dateDiff(start_dt, roifrom_dt) <= 0 && (roito_dt == null || DateUtil.dateDiff(roito_dt, (Date) hashDailyMap.get("END_DT")) <= 0)) {
                                                        interestList.clear();
                                                        interestList.add(resultInterstRate.get(k));
                                                        resultInt = calculateInterest(hashDailyMap);
                                                        if (resultInt != null) {
                                                         //    System.out.println("77777777======"+resultInt);
                                                            hashDailyMap.put(InterestCalculationConstants.INTEREST, resultInt.get(InterestCalculationConstants.INTEREST));
                                                            if (passDate.containsKey("LOAN_ACCOUNT_CLOSING")) {
                                                                insertLoanInteresttmp(hashDailyMap);
                                                            } else {
                                                                insertLoanInterest(hashDailyMap);
                                                            }
                                                        }
                                                    } else {
                                                        do {
                                                         //    System.out.println("77777roito_dt777======"+roito_dt);
                                                            if (roito_dt != null && DateUtil.dateDiff(dayEndBalanceDt, roito_dt) <= 0) {
                                                                hashDailyMap.put("END_DT", roito_dt);
                                                            } else {
                                                                hashDailyMap.put("END_DT", dayEndBalanceDt);
                                                                dayendOver = true;
                                                            }
                                                            if (resultInterstRate != null && resultInterstRate.size() > 0) {
                                                                //                                                            interestList=(List)resultInterstRate.get(0);
                                                                interestList.clear();
                                                                interestList.add(resultInterstRate.get(k));
                                                            }
                                                            resultInt = calculateInterest(hashDailyMap);
                                                    //          System.out.println("resultInt 888======"+resultInt);
                                                            if (resultInt != null) {
                                                                hashDailyMap.put(InterestCalculationConstants.INTEREST, resultInt.get(InterestCalculationConstants.INTEREST));
                                                                if (passDate.containsKey("LOAN_ACCOUNT_CLOSING")) {
                                                                    insertLoanInteresttmp(hashDailyMap);
                                                                } else {
                                                                    insertLoanInterest(hashDailyMap);
                                                                }
                                                            }
                                                            if (roito_dt != null) {
                                                                roifrom_dt = (Date) DateUtil.addDays(roito_dt, 1);
                                                            }
                                                            hashDailyMap.put("START_DT", roifrom_dt);
                                                            hashDailyMap.put("END_DT", dayEndBalanceDt);
                                                            interestMap.put("DAYENDSTDT", hashDailyMap.get("START_DT"));
                                                            interestMap.put("DAYENDEDDT", hashDailyMap.get("END_DT"));
                                                            if (intGetFrom.equals("PROD")) {
                                                                resultInterstRate = sqlMap.executeQueryForList("getSelectProductDayEndTermLoanInterestMap", interestMap);
                                                            } else {
                                                                resultInterstRate = sqlMap.executeQueryForList("getSelectAccountTermLoanDayEndInterestMap", interestMap);
                                                            }
                                                            if (resultInterstRate != null && resultInterstRate.size() > 0) {
                                                                interestResultMap = (HashMap) resultInterstRate.get(0);
                                                                roifrom_dt = (Date) interestResultMap.get("FROM_DT");
                                                                if (interestResultMap.containsKey("TO_DT") && interestResultMap.get("TO_DT") != null) {
                                                                    roito_dt = (Date) interestResultMap.get("TO_DT");
                                                                }
                                                            } else {
                                                                roito_dt = null;
                                                            }
                                                            hashDailyMap.put("START_DT", roifrom_dt);
                                                        } while (DateUtil.dateDiff(roifrom_dt, dayEndBalanceDt) > 0 && dayendOver == false);
                                                        //                                                           Date nextroidt=(Date)DateUtil.addDays(roifrom_dt,1);
                                                        //                                                           if(DateUtil.dateDiff(nextroidt,dayEndBalanceDt)>0){
                                                        //                                                           hashDailyMap.put("START_DT",nextroidt);
                                                        //                                                           hashDailyMap.put("END_DT",dayEndBalanceDt);
                                                        //                                                            resultInt= calculateInterest(hashDailyMap);
                                                        //                                                }
                                                        //                                                    interestList
                                                    }

                                                }

                                            }
                                        }
                                    } else {
                                        resultInt = calculateInterest(hashDailyMap);
                                        hashDailyMap.put(InterestCalculationConstants.INTEREST, resultInt.get(InterestCalculationConstants.INTEREST));
                                        //                             hashDailyMap.put("DAY_END_DT",hashDailyMap.get("START_DT")); //for insert interestdetails only
                                        hashDailyMap.put("NEXT_DT", chekdateLast);  //for insert interestdetails only
                                        if (passDate.containsKey("LOAN_ACCOUNT_CLOSING")) {
                                            insertLoanInteresttmp(hashDailyMap);
                                        } else {
                                            insertLoanInterest(hashDailyMap);
                                        }
                                    }
                                    if (resultInt != null) {
                                        totInterest += CommonUtil.convertObjToDouble(resultInt.get(InterestCalculationConstants.INTEREST)).doubleValue();
                                        ROI = CommonUtil.convertObjToDouble(resultInt.get("ROI"));
                                    }
                                }
                            }
                        }

                        //                if(totInterest >0){
                        HashMap roundeMap = new HashMap();
                        if (finalRetraspectiveAmt != 0) {
                            totInterest -= finalRetraspectiveAmt;
                        }
                        //checking purpose
                        //                    totInterest*=(-1);
                  //      System.out.println("totInterest :"+totInterest);
                        if(paramMap!=null && paramMap.containsKey("OD_CHARGESUI_INTAMT")){
                            totInterest = CommonUtil.convertObjToDouble(paramMap.get("OD_CHARGESUI_INTAMT"));
                        }
                        roundeMap.put("TOT_INTEREST", new Double(totInterest));
                        roundeMap.put("PROD_ID", passDate.get("PROD_ID"));
                        totInterest = roundOffLoanInterest(roundeMap);
                      //    System.out.println("totInterest 888777788======"+totInterest);
                        //                try{
                        //                    sqlMap.startTransaction();
                        //                    insertRecord=new HashMap();

                        //OTS interest calculation
                   /*     if (productRecord.containsKey("OTS") && CommonUtil.convertObjToStr(productRecord.get("OTS")).equals("Y")) {
                            returnMap = calculateOTSInterstDetails(productRecord);//returnMap
                            insertRecord.putAll(returnMap);
//                        totInterest=0;//need not calculate interest
                        }*/
                        if (ROI.doubleValue() > 0) {
                            insertRecord.put("ROI", ROI);
                        }
                        insertRecord.put("ACT_NUM", singleRecord.get("ACT_NUM"));
                        insertRecord.put("PROD_ID", passDate.get("PROD_ID"));
                        insertRecord.put("FROM_DATE", singleRecord.get("START_DATE"));
                        insertRecord.put("TO_DATE", singleRecord.get("CURR_DATE"));
                        insertRecord.put("INTEREST", new Double(totInterest));
                        //                                    sqlMap.executeUpdate("insertMonthInterestOD", insertRecord);
                        insertRecord.put("ACCOUNTNO", insertRecord.get("ACT_NUM"));
                        insertRecord.put("AMOUNT", insertRecord.get("INTEREST"));
                        insertRecord.put("ASSET_STATUS", productRecord.get("ASSET_STATUS"));
                        double penal = 0;
                        if (productRecord.containsKey("OTS") && CommonUtil.convertObjToStr(productRecord.get("OTS")).equals("Y")) {
                        } else if (penalInterestMap != null && penalInterestMap.size() > 0) {
                            insertRecord.put("LOAN_CLOSING_PENAL_INT", penalInterestMap.get("TOT_PENAL_INT"));
                            penal = CommonUtil.convertObjToDouble(penalInterestMap.get("TOT_PENAL_INT")).doubleValue();
                        }
                        if (passDate.containsKey("BATCH_PROCESS")) {
                            insertRecord.put("LAST_CALC_DT", chekdateLast);
                        } else {
                            insertRecord.put("LAST_CALC_DT", singleRecord.get("CURR_DATE"));
                        }
                        insertRecord.put("BEHAVES_LIKE", passDate.get("BEHAVES_LIKE"));
                        if (!passDate.containsKey("LOAN_ACCOUNT_CLOSING")) {
                            if (passDate.containsKey("BATCH_PROCESS") && productRecord.get("ASSET_STATUS") != null && productRecord.get("ASSET_STATUS").equals("STANDARD_ASSETS")) {
                                insertRecord.put("END_DT", hashDailyMap.get("END_DT"));
//                            if(productRecord.containsKey("INTEREST_DUE_KEPT_RECEIVABLE") && CommonUtil.convertObjToStr(productRecord.get("INTEREST_DUE_KEPT_RECEIVABLE")).equals("N")){
//                                doTransactionBaseAVBalanceOD(insertRecord,productRecord);
//                            }
//                            else if(productRecord.containsKey("INTEREST_DUE_KEPT_RECEIVABLE") && CommonUtil.convertObjToStr(productRecord.get("INTEREST_DUE_KEPT_RECEIVABLE")).equals("Y")){
                              //  doTransactionOD(insertRecord, productRecord);
                          //      System.out.println("IF PARTTT ");
//                            }
                            } else {
                       //         System.out.println("ELSE PARTTT ");
                              //  npaInterest(insertRecord);//non standard asset keep in seperate
                            }
                        }
                     //   System.out.println("execution successfull777777y");
                       /* if (prematureLTD/* || passDate.containsKey("NORMAL_CLOSER")) {
                            HashMap where = new HashMap();
                            where.put("ACT_NUM", singleRecord.get("ACT_NUM"));
                            where.put("FROM_DT", singleRecord.get("START_DATE"));
                            where.put("TO_DATE", currDt);//singleRecord.get("CURR_DATE"));
                            HashMap paidMap = (HashMap) sqlMap.executeQueryForObject("getPaidPrinciple", where);
                            double paidInt = CommonUtil.convertObjToDouble(paidMap.get("INTEREST")).doubleValue();
                            double paidPenalInt = CommonUtil.convertObjToDouble(paidMap.get("PENAL")).doubleValue();
                            totInterest = totInterest - paidInt;
                            paidPenalInt = penal - paidPenalInt;
                            insertRecord.put("INTEREST", new Double(totInterest));//INTEREST
                            insertRecord.put("LOAN_CLOSING_PENAL_INT", new Double(paidPenalInt));

                        } */
                        if (!(passDate.containsKey("LOAN_ACCOUNT_CLOSING") && passDate.get("LOAN_ACCOUNT_CLOSING") != null)) {
                            sqlMap.commitTransaction();
                        }
                        penalInterestMap = new HashMap();

                      //  System.out.println("insert Record  666666###" + insertRecord);
                   // }// while (calculatePenalInterestAdvancesCalenderFreq(productRecord, passDate, actListMap));
                } catch (Exception e) {
                    sqlMap.rollbackTransaction();
//                        status.setStatus(BatchConstants.ERROR) ;
//                        HashMap errorMap = new HashMap();
//                        errorMap.put("ERROR_DATE",currDt);
//                        errorMap.put("TASK_NAME", taskLable);
//                        errorMap.put("ERROR_MSG",e.getMessage());
//                        errorMap.put("ACT_NUM",productRecord.get("ACCT_NUM"));
//                        errorMap.put("BRANCH_ID", productRecord.get("BRANCH_ID"));
//                        sqlMap.startTransaction();
//                        sqlMap.executeUpdate("insertError_showing", errorMap);
//                        sqlMap.commitTransaction();
                    String errMsg = "";
                    TTException tte = null;
                    HashMap exceptionMap = null;
                    HashMap excMap = null;
                    String strExc = null;
                    String errClassName = "";
                    e.printStackTrace();
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
                                        HashMap errorMap = new HashMap();
                                        errorMap.put("ERROR_DATE", currDt);
                                        errorMap.put("TASK_NAME", taskLable);
                                        errorMap.put("ERROR_MSG", strExc);
                                        errorMap.put("ERROR_CLASS", errClassName);
                                        errorMap.put("ACT_NUM", productRecord.get("ACCT_NUM"));
                                        errorMap.put("BRANCH_ID", productRecord.get("BRANCH_ID"));
                                        sqlMap.startTransaction();
                                        sqlMap.executeUpdate("insertError_showing", errorMap);
                                        sqlMap.commitTransaction();
                                        errorMap = null;
                                    }
                                }
                            } else {
                                System.out.println("#$#$ if not TTException part..." + e);
                                errMsg = e.getMessage();
                                HashMap errorMap = new HashMap();
                                errorMap.put("ERROR_DATE", currDt);
                                errorMap.put("TASK_NAME", taskLable);
                                errorMap.put("ERROR_MSG", errMsg);
                                errorMap.put("ERROR_CLASS", errClassName);
                                errorMap.put("ACT_NUM", productRecord.get("ACCT_NUM"));
                                errorMap.put("BRANCH_ID", productRecord.get("BRANCH_ID"));
                                sqlMap.startTransaction();
                                sqlMap.executeUpdate("insertError_showing", errorMap);
                                sqlMap.commitTransaction();
                                errorMap = null;
                            }
                        }
                    } else {
                        System.out.println("#$#$ if not TTException part..." + e);
                        errMsg = e.getMessage();
                        HashMap errorMap = new HashMap();
                        errorMap.put("ERROR_DATE", currDt);
                        errorMap.put("TASK_NAME", taskLable);
                        errorMap.put("ERROR_MSG", errMsg);
                        errorMap.put("ERROR_CLASS", errClassName);
                        errorMap.put("ACT_NUM", productRecord.get("ACCT_NUM"));
                        errorMap.put("BRANCH_ID", productRecord.get("BRANCH_ID"));
                        sqlMap.startTransaction();
                        sqlMap.executeUpdate("insertError_showing", errorMap);
                        sqlMap.commitTransaction();
                        errorMap = null;
                    }
                    if (status != null) {
                        status.setStatus(BatchConstants.ERROR);
                    }
                    //                        System.out.println("Error thrown for Operative A/c No " + dataMap.get("ACT_NUM"));
                    //                e.printStackTrace();
                    tte = null;
                    exceptionMap = null;
                    excMap = null;
                    e.printStackTrace();
                }
            }
        }
        insertRecord.putAll(lastAppDt);

        insertRecord.put("LAST_INT_CALC_DT", passDate.get("LAST_INT_CALC_DT"));
        insertRecord.put("INTEREST_WAIVER", productRecord.get("INTEREST_WAIVER"));
        insertRecord.put("PENAL_WAIVER", productRecord.get("PENAL_WAIVER"));
        //added b y rishad  18/03/2014
        insertRecord.put("PRINCIPAL_WAIVER", productRecord.get("PRINCIPAL_WAIVER"));
        insertRecord.put("NOTICE_WAIVER", productRecord.get("NOTICE_WAIVER"));
        insertRecord.put("EP_COST_WAIVER", productRecord.get("EP_COST_WAIVER"));
        insertRecord.put("POSTAGE_WAIVER", productRecord.get("POSTAGE_WAIVER"));
        insertRecord.put("ADVERTISE_WAIVER", productRecord.get("ADVERTISE_WAIVER"));
        insertRecord.put("LEGAL_WAIVER", productRecord.get("LEGAL_WAIVER"));
        insertRecord.put("INSURANCE_WAIVER", productRecord.get("INSURANCE_WAIVER"));
        insertRecord.put("MISCELLANEOUS_WAIVER", productRecord.get("MISCELLANEOUS_WAIVER"));
        insertRecord.put("DECREE_WAIVER", productRecord.get("DECREE_WAIVER"));
        insertRecord.put("ARBITRARY_WAIVER ", productRecord.get("ARBITRARY_WAIVER "));
        insertRecord.put("ARC_WAIVER", productRecord.get("ARC_WAIVER"));
        
        if(insertRecord.containsKey("LOAN_CLOSING_PENAL_INT") && CommonUtil.convertObjToDouble(insertRecord.get("LOAN_CLOSING_PENAL_INT")).doubleValue()==0.0){
            //insertRecord .put("REBATE_INTEREST",new Double(rebateInterestCalculation(productRecord)));
        } else{
            insertRecord .put("REBATE_INTEREST",new Double(0));
        }

        System.out.println("#$#$# Final return map444444444444444444444444 : " + insertRecord);
        passDate = new HashMap();
        lastAppDt = new HashMap();

        return insertRecord;
    }
    
        private double roundOffLoanInterest(HashMap prodLevelWhereMap) throws Exception {
        Rounding rd = new Rounding();
        HashMap prodLevelValues = getProductLevelValues(CommonUtil.convertObjToStr(prodLevelWhereMap.get("PROD_ID")));
        String roundingType = null;
        double interest = 0;
        interest = CommonUtil.convertObjToDouble(prodLevelWhereMap.get("TOT_INTEREST")).doubleValue();
        if (interest == 0) {
            interest = CommonUtil.convertObjToDouble(prodLevelWhereMap.get("TOT_PENAL_INT")).doubleValue();
        }
        if (prodLevelValues.containsKey("DEBIT_INT_ROUNDOFF") && prodLevelValues.get("DEBIT_INT_ROUNDOFF") != null) {
            roundingType = (CommonUtil.convertObjToStr(prodLevelValues.get("DEBIT_INT_ROUNDOFF")));
        } else {
            roundingType = ROUNDING_NEAREST;//InterestCalculationConstants.ROUNDING_HIGHER;
        }
 //       System.out.println(interest + "prodLevelValues#####" + prodLevelValues);
        String floatPrecisionStr = "2";
        if (roundingType != null && roundingType.length() > 0
                && floatPrecisionStr != null && floatPrecisionStr.length() > 0) {
            int floatPrecision = Integer.parseInt(floatPrecisionStr);
            //            interest= interest*Math.pow(10, floatPrecision+1);
            double number = interest;
          //  System.out.println("!@!@ INSIDE IF ROUNDING..." + roundingType);
            //The following block changed for Polpully Bank
//            if(roundingType.equals(ROUNDING_NEAREST))
//                number =rd.getNearestHigher(number,1);
//            else if (roundingType.equals(NEAREST_TENS))
//                number =rd.getNearestHigher(number,10);
//            else if (roundingType.equals(NEAREST_HUNDREDS))
//                number =rd.getNearestHigher(number,100);
            if (roundingType.equals(ROUNDING_NEAREST)) {
                number = (long) (number + 0.5); //rd.getNearest(number,1);
            } else if (roundingType.equals(NEAREST_TENS)) {
                number = rd.getNearest(number, 10);
            } else if (roundingType.equals(NEAREST_HUNDREDS)) {
                number = rd.getNearest(number, 100);
            }
            interest = number;///Math.pow(10,floatPrecision+1);
        }
        rd = null;
    //    System.out.println("interest@@@####" + interest);
        return interest;
    }


        HashMap calculateInterest(HashMap map) throws Exception {
     //   System.out.println("calcualteinterest ###" + map);
        HashMap hash = null;
        if (map.containsKey(PENALTY)) {
            hash = setInterestCalculationBeanValues(map, PENALTY);
        } else {
            hash = setInterestCalculationBeanValues(map, ROI);
        }
        LoanCalculateInterest interest = new LoanCalculateInterest();
        Thread.sleep((long) Math.random());
        if(interestsMap != null){
        hash.put("INTEREST_MAP",interestsMap);
         //   System.out.println("HHHHHASH"+hash);
        }
        HashMap getIntAmt = interest.getInterest(hash);
        return getIntAmt;
    }

     private HashMap setInterestCalculationBeanValues(HashMap prodLevelWhereMap, String interestType) throws Exception {
        HashMap map = new HashMap();
        HashMap repayMap = new HashMap();
        interestRate = new String();
        HashMap prodLevelValues = getProductLevelValues(CommonUtil.convertObjToStr(prodLevelWhereMap.get("PROD_ID")));
        InterestCalculationBean interestBean = new InterestCalculationBean();
        interestBean.setCompoundingPeriod("365");
        interestBean.setPrincipalAmt(CommonUtil.convertObjToStr(prodLevelWhereMap.get("PRINCIPAL_BAL")));
        //        if(CommonUtil.convertObjToStr(prodLevelWhereMap.get("PROD_ID")).equals("OD"))
        //            interestBean.setCompoundingType("COMPOUND");
        //        else
        interestBean.setCompoundingType("REPAYMENT");
        //        interestBean.setInterestType(CommonUtil.convertObjToStr(prodLevelWhereMap.get("INTEREST")));//COMPUND OR SIMPLE
        interestBean.setInterestType("COMPOUND");
        interestBean.setIsDuration_ddmmyy(false);
        interestBean.setDuration_FromDate(CommonUtil.convertObjToStr(prodLevelWhereMap.get("START_DT")));
        interestBean.setDuration_ToDate(CommonUtil.convertObjToStr(prodLevelWhereMap.get("END_DT")));
        interestBean.setFloatPrecision("2");
        if (prodLevelValues.containsKey("YEAR_OPTION")) {
            interestBean.setYearOption(CommonUtil.convertObjToStr(prodLevelValues.get("YEAR_OPTION")));
        } else {
            interestBean.setYearOption(InterestCalculationConstants.YEAR_OPTION_ACTUAL);
        }
        if (prodLevelValues.containsKey("MONTH_OPTION")) {
            interestBean.setMonthOption(CommonUtil.convertObjToStr(prodLevelValues.get("MONTH_OPTION")));
        } else {
            interestBean.setMonthOption(InterestCalculationConstants.MONTH_OPTION_ACTUAL);
        }
        if (prodLevelValues.containsKey("ROUNDING_TYPE")) {
            interestBean.setRoundingType(CommonUtil.convertObjToStr(prodLevelValues.get("ROUNDING_TYPE")));
        } else {
            interestBean.setRoundingType(InterestCalculationConstants.ROUNDING_HIGHER);
        }
        //Mantis 8358 - Babu
        if (prodLevelValues.containsKey("DEBIT_INT_ROUNDOFF")) {
            if (prodLevelValues.get("DEBIT_INT_ROUNDOFF") != null) {
                String intRd = CommonUtil.convertObjToStr(prodLevelValues.get("DEBIT_INT_ROUNDOFF"));
                if (intRd != null && intRd.equals(ROUNDING_NEAREST)) {
                    interestBean.setRoundingType(InterestCalculationConstants.ROUNDING_NEAREST);
                }
                if (intRd != null && intRd.equals(NEAREST_TENS)) {
                    interestBean.setRoundingType(InterestCalculationConstants.ROUNDING_VALUE_10_RUPEES);
                }
                if (intRd != null && intRd.equals(NEAREST_HUNDREDS)) {
                    interestBean.setRoundingType(InterestCalculationConstants.ROUNDING_VALUE_100_RUPEES);
                }
            }
        }
        if (prodLevelValues.containsKey("ROUNDING_FACTOR")) {
            interestBean.setRoundingFactor(CommonUtil.convertObjToStr(prodLevelValues.get("ROUNDING_FACTOR")));
        } else {
            interestBean.setRoundingFactor(InterestCalculationConstants.ROUNDING_VALUE_1_RUPEE);
        }
        if (prodLevelWhereMap.containsKey("NO_ROUND_OFF_INT")) {
            interestBean.setNoLoanRoundeInt("NO_ROUND_OFF_INT");
        }
        repayMap.put("FROM_DATE", (Date)prodLevelWhereMap.get("START_DT"));
        repayMap.put("TO_DATE", (Date) prodLevelWhereMap.get("END_DT"));
        ArrayList variousInterestList = null;
        //        if (interestType.equals(ROI) || interestType.equals(PENALTY)){
        //System.out.println("interestList---------------->"+interestList+"interestType-->"+interestType);
        variousInterestList = getInterestList(interestList, interestType);
        //        }
        //        else
        if (interestType.equals(PENALTY)) {
            //            variousInterestList = getPenulInterestList(DateUtil.getStringDate((Date)prodLevelWhereMap.get("START_DT")), DateUtil.getStringDate((Date)prodLevelWhereMap.get("END_DT")));
        }

        interestBean.setRateOfInterest(CommonUtil.convertObjToStr(((HashMap) variousInterestList.get(0)).get(INTEREST)));
        //for insert interest detais
        interestRate = CommonUtil.convertObjToStr(((HashMap) variousInterestList.get(0)).get(INTEREST));
        repayMap.put("VARIOUS_INTEREST_RATE", variousInterestList);
        repayMap.put("REPAYMENT_TYPE", "REPAYMENT");
        map.put(CommonConstants.DATA, interestBean);
        //        map.put("INTEREST_TYPE_C_S",prodLevelWhereMap.get("INTEREST"));
        map.put("REPAYMENT_DETAILS", repayMap);
     //   System.out.println("interest Bean  all map#####" + map);
        repayMap = null;
        interestBean = null;
        return map;
    }

            
       private HashMap getProductLevelValues(String strProdID) throws Exception {
        HashMap transactionMap = new HashMap();
        HashMap retrieve = new HashMap();
        transactionMap.put("PROD_ID", strProdID);
        List resultList = (List) sqlMap.executeQueryForList("getCompFreqRoundOff_LoanProd", transactionMap);
        if (resultList.size() > 0) {
            // If Product Account Head exist in Database
            retrieve = (HashMap) resultList.get(0);
        }
        transactionMap = null;
        resultList = null;
        return retrieve;
    }
       
       
    private ArrayList getInterestList(List list, String interestType) throws Exception {
        HashMap temp;
        HashMap interestMap;
        HashMap matToDate;
    //    System.out.println("getInterestList"+list+"interestType"+interestType);
        ArrayList various_interestList = new ArrayList();
        for (int i = list.size() - 1, j = 0; i >= 0; --i, ++j) {
            temp = (HashMap) list.get(j);
            //line added by nidhin
            
            if(temp.containsKey("TO_DT") && temp.get("TO_DT") != null){
                interestsMap.put("MAT_DATE",temp.get("TO_DT"));
            }
          //  System.out.println("TODATEEEE"+interestsMap);
            interestMap = new HashMap();
            if (interestType.equals(ROI)) {
                interestMap.put(INTEREST, temp.get(INTEREST));
            } else if (interestType.equals(PENALTY)) {
                interestMap.put(INTEREST, temp.get("PENAL_INTEREST"));
            }
            interestMap.put("FROM_DATE", DateUtil.getStringDate((Date) temp.get("FROM_DT")));
            various_interestList.add(interestMap);
            interestMap = null;
            temp = null;
        }
        temp = null;
      //  System.out.println("varius interest list###3" + various_interestList);
        return various_interestList;
    }

        
     private void insertLoanInteresttmp(HashMap interestMap) throws Exception {
     //   System.out.println("insertLoanInteresttmp####" + interestMap);
        HashMap loanIntMap = new HashMap();
        long noofdays = 0;
        //         List lst=sqlMap.executeQueryForList("selectLoanInterestTMP",interestMap);
        //         if( lst!=null && lst.size()>0)
        //             sqlMap.executeUpdate("deleteLoanInterestTMP",loanIntMap);
        double amt = CommonUtil.convertObjToDouble(interestMap.get("AMT")).doubleValue();
        if (interestMap.get("INTEREST") != null
                && CommonUtil.convertObjToDouble(interestMap.get("INTEREST")).doubleValue() > 0) {   //This condition added by Rajesh
            noofdays = DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(interestMap.get("START_DT"))),
                    DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(interestMap.get("END_DT"))));
         //   System.out.println("noof days####" + noofdays);
            Date dayenddt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(interestMap.get("START_DT")));
            Date nextdt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(interestMap.get("END_DT")));

            if (CommonUtil.convertObjToStr(interestMap.get("VALUEDATE")).equals("VALUEDATE_DR")) {

                loanIntMap.put("REMARKS", "1");
                loanIntMap.put("VALIDATE_TYPE", "VALUEDATE_DR");
            } else if (CommonUtil.convertObjToStr(interestMap.get("VALUEDATE")).equals("VALUEDATE_CR")) {
                loanIntMap.put("REMARKS", "2");
                loanIntMap.put("VALIDATE_TYPE", "VALUEDATE_CR");
            } else if (CommonUtil.convertObjToStr(interestMap.get("PENALTY")).equals("PENALTY")) {
                loanIntMap.put("REMARKS", "3");
                loanIntMap.put("VALIDATE_TYPE", "PENALTY");
            } else {
                loanIntMap.put("REMARKS", "0");
                loanIntMap.put("VALIDATE_TYPE", "ROI");
            }
            noofdays += 1;
//            if(noofdays==0)
//                noofdays+=1;
//            else
//                noofdays+=2;
        //    System.out.println("noof after  days####" + noofdays);
            if (amt < 0) {
                amt *= -1;
            }
            loanIntMap.put("ACT_NUM", interestMap.get("ACT_NUM"));
            loanIntMap.put("FROM_DT", dayenddt);
            loanIntMap.put("TO_DATE", nextdt);
            loanIntMap.put("AMT", new Double(amt));
            loanIntMap.put("NO_OF_DAYS", CommonUtil.convertObjToInt(noofdays));
            loanIntMap.put("TOT_PRODUCT", new Double(amt * noofdays));
            loanIntMap.put("INT_AMT", interestMap.get("INTEREST"));
            loanIntMap.put("PROD_ID", interestMap.get("PROD_ID"));
            loanIntMap.put("CUST_ID", interestMap.get("CUST_ID"));
            loanIntMap.put("INT_RATE", CommonUtil.convertObjToDouble(interestRate));
			loanIntMap.put("CURR_DT", curDate); // Added by Rajesh
            loanIntMap.put("BRANCH_CODE", branch);
            loanIntMap.put("USER_ID", user);
//            VALIDATE_TYPE
        //    System.out.println("loanIntMap#####@@@@" + loanIntMap);


            sqlMap.executeUpdate("insertLoanInterestTMPPROCEDURE", loanIntMap);
            //sqlMap.executeUpdate("insertLoanInterestTMP",loanIntMap); // COMMENT BY ABI WE MADE AURONOMOUS TRANSACTION THROUGH THE ORACLE PROCEDURE
        }
    }

    private void insertLoanInterest(HashMap interestMap) throws Exception {
      //  System.out.println("insertLoanInterest##6666##" + interestMap);
       // System.out.println("insertLoanInteresttmp##7777##" + interestMap);
        HashMap loanIntMap = new HashMap();
        long noofdays = 0;
        double amt = CommonUtil.convertObjToDouble(interestMap.get("AMT")).doubleValue();
        noofdays = DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(interestMap.get("START_DT"))),
                DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(interestMap.get("END_DT"))));
      //  System.out.println("noof after  days####" + noofdays);
        //        if(noofdays==0)
        //            noofdays=1;
        //        else
        noofdays += 1;
      //  System.out.println("noof after  days####" + noofdays);
        if (amt < 0) {
            amt *= -1;
        }
        String stringDayend = CommonUtil.convertObjToStr(interestMap.get("START_DT"));
        Date dayenddt = DateUtil.getDateMMDDYYYY(stringDayend);
        String enddt = CommonUtil.convertObjToStr(interestMap.get("END_DT"));
        Date nextdt = DateUtil.getDateMMDDYYYY(enddt);

        if (CommonUtil.convertObjToStr(interestMap.get("VALUEDATE")).equals("VALUEDATE_DR")) {

            loanIntMap.put("REMARKS", "1");
            loanIntMap.put("VALIDATE_TYPE", "VALUEDATE_DR");
        } else if (CommonUtil.convertObjToStr(interestMap.get("VALUEDATE")).equals("VALUEDATE_CR")) {
            loanIntMap.put("REMARKS", "2");
            loanIntMap.put("VALIDATE_TYPE", "VALUEDATE_CR");
        } else if (CommonUtil.convertObjToStr(interestMap.get("PENALTY")).equals("PENALTY")) {
            loanIntMap.put("REMARKS", "3");
            loanIntMap.put("VALIDATE_TYPE", "PENALTY");
        } else {
            loanIntMap.put("REMARKS", "0");
            loanIntMap.put("VALIDATE_TYPE", "ROI");
        }
        if (CommonUtil.convertObjToStr(interestMap.get("ACCT_NUM")).length() > 0) {
            loanIntMap.put("ACT_NUM", interestMap.get("ACCT_NUM"));
        } else {
            loanIntMap.put("ACT_NUM", interestMap.get("ACT_NUM"));
        }
        loanIntMap.put("ACT_NUM", interestMap.get("ACT_NUM"));
        loanIntMap.put("FROM_DT",CommonUtil.getProperDate(currDt, dayenddt));
        loanIntMap.put("TO_DATE",CommonUtil.getProperDate(currDt, nextdt));
        loanIntMap.put("AMT", new Double(amt));
        loanIntMap.put("NO_OF_DAYS", new Long(noofdays));
        loanIntMap.put("TOT_PRODUCT", new Double(amt * noofdays));
        loanIntMap.put("INT_AMT", interestMap.get("INTEREST"));
        loanIntMap.put("PROD_ID", interestMap.get("PROD_ID"));
        loanIntMap.put("CUST_ID", interestMap.get("CUST_ID"));
        loanIntMap.put("INT_RATE", CommonUtil.convertObjToDouble(interestRate));
     //   System.out.println("loanIntMap#####@@4444@@" + loanIntMap);
        sqlMap.executeUpdate("insertLoanInterest", loanIntMap);
    }


    private InterestBatchTO createNewTO(InterestBatchTO obj) throws Exception {
        InterestBatchTO tempObj = new InterestBatchTO();
        tempObj.setAcHdId(obj.getAcHdId());
        tempObj.setActNum(obj.getActNum());
        tempObj.setApplDt(obj.getApplDt());
        tempObj.setCustId(obj.getCustId());
        tempObj.setDrCr(obj.getDrCr());
        tempObj.setIntAmt(obj.getIntAmt());
        tempObj.setIntDt(obj.getIntDt());
        tempObj.setIntRate(obj.getIntRate());
        tempObj.setIntType(obj.getIntType());
        tempObj.setIsTdsApplied(obj.getIsTdsApplied());
        tempObj.setLastTdsApplDt(obj.getLastTdsApplDt());
        tempObj.setLastTdsRecivedFrom(obj.getLastTdsRecivedFrom());
        tempObj.setPrincipleAmt(obj.getPrincipleAmt());
        tempObj.setProductId(obj.getProductId());
        tempObj.setProductType(obj.getProductType());
        tempObj.setTaxAmt(obj.getTaxAmt());
        tempObj.setTdsAmt(obj.getTdsAmt());
        tempObj.setTdsDeductedFromAll(obj.getTdsDeductedFromAll());
        tempObj.setTot_int_amt(obj.getTot_int_amt());
        tempObj.setTotalTdsAmt(obj.getTotalTdsAmt());
        tempObj.setTransLogId(obj.getTransLogId());
        tempObj.setTrnDt(obj.getTrnDt());
        return tempObj;
    }

    private void runDailyBalance() throws Exception {
        TaskHeader tskHeader = new TaskHeader();
        tskHeader.setBranchID(branch);
        tskHeader.setUserID(getHeader().getUserID());
        tskHeader.setIpAddr(getHeader().getIpAddr());
        //                        tskHeader.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkDailyBalance.getText())));
        for (int i = 0; i < prodList.size(); i++) {
            tskHeader.setProductType(CommonUtil.convertObjToStr(prodList.get(i)));
            tskHeader.setTaskParam(getHeader().getTaskParam());
            DailyBalanceUpdateTask daily = new DailyBalanceUpdateTask(tskHeader);
            daily.executeTask();
        }
        tskHeader = null;
    }

    //__ To be called from the OB, Calculates the Interest and inserts the data into the DataBase...
    public void insertData() throws Exception {
        //__ The selected task is being called from OB of Some Code...
        HashMap resultMap = new HashMap();
        taskSelected = INSERTDATA;
        final String prodId = CommonUtil.convertObjToStr(paramMap.get(CommonConstants.PRODUCT_ID));
        paramMap.put(InterestTaskRunner.PRODUCT_ID, prodId);

        ArrayList productList = getProductList(paramMap);
        setProdIdData(productList);
     //   System.out.println("paramMap2: " + paramMap);
        lstintCr = ServerUtil.getCurrentDate(branch);
        if (prodType.equals("AD")) {
            taskSelected = OBCODE;
            paramMap.put("BRANCH_CODE", branch);
            if (paramMap.get("ACT_NUM") instanceof ArrayList) {
                ArrayList singleList = (ArrayList) paramMap.get("ACT_NUM");
                resultMap.put("ACT_NUM", CommonUtil.convertObjToStr(singleList.get(0)));
                paramMap.put("ACT_NUM", CommonUtil.convertObjToStr(singleList.get(0)));
            }
            ArrayList totAcctList = getAccountList(paramMap);
            resultMap.put("START", paramMap.get("LAST_APPL_DT"));
            resultMap.put("END", DateUtil.addDays((Date) paramMap.get("TODAY_DT"), -1));
            resultMap.put("PROD_ID", paramMap.get("PROD_ID"));
            if (paramMap.get("ACT_NUM") instanceof ArrayList) {
                ArrayList singleList = (ArrayList) paramMap.get("ACT_NUM");
                resultMap.put("ACT_NUM", CommonUtil.convertObjToStr(singleList.get(0)));
            }
            resultMap.put("PROD_ID", paramMap.get("PROD_ID"));

            intPayableAD(resultMap, totAcctList, paramMap);
        } else {
            implementTask(paramMap, false);
        }
        taskSelected = 0;
    }

    //__ To be called from the OB to get the Interest...
    public HashMap getInterestData() throws Exception {
        //__ The selected task is being called from OB of Some Code...
        taskSelected = OBCODE;
        HashMap dataMap = new HashMap();
        HashMap resultMap = new HashMap();
        final String prodId = CommonUtil.convertObjToStr(paramMap.get(CommonConstants.PRODUCT_ID));
        paramMap.put(InterestTaskRunner.PRODUCT_ID, prodId);

        ArrayList productList = getProductList(paramMap);
      //  System.out.println("!!!!!!!! productList.size() : " + productList.size());
        if (productList.size() != 0 && productList != null) { //added by Rajesh
            setProdIdData(productList);
        //    System.out.println("!!!!!!!! paramMap : " + paramMap);
            lstintCr = ServerUtil.getCurrentDate(branch);
            if (prodType.equals("AD")) {
                paramMap.put("BRANCH_CODE", branch);
                if (paramMap.get("ACT_NUM") instanceof ArrayList) {
                    ArrayList singleList = (ArrayList) paramMap.get("ACT_NUM");
                    resultMap.put("ACT_NUM", CommonUtil.convertObjToStr(singleList.get(0)));
                    paramMap.put("ACT_NUM", CommonUtil.convertObjToStr(singleList.get(0)));
                }
                ArrayList totAcctList = getAccountList(paramMap);
                resultMap.put("START", paramMap.get("LAST_APPL_DT"));
                resultMap.put("END", DateUtil.addDays((Date) paramMap.get("TODAY_DT"), -1));
                resultMap.put("PROD_ID", paramMap.get("PROD_ID"));
                if (paramMap.get("ACT_NUM") instanceof ArrayList) {
                    ArrayList singleList = (ArrayList) paramMap.get("ACT_NUM");
                    resultMap.put("ACT_NUM", CommonUtil.convertObjToStr(singleList.get(0)));
                }
                resultMap.put("PROD_ID", paramMap.get("PROD_ID"));

                dataMap = intPayableAD(resultMap, totAcctList, paramMap);
                //                dataMap = implementTask(paramMap, false);
            } else {
                dataMap = implementTask(paramMap, false);
            }
            taskSelected = 0;
            return dataMap;
        }
        dataMap.put(paramMap.get("ACT_NUM"), "0.0");
        return dataMap;
    }

    public long higher(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        if (mod == 0) {
            return number;
        }
        return (number - mod) + roundingFactor;
    }

    public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
        if ((mod < (roundingFactor / 2)) || (mod < (roundingFactorOdd / 2))) {
            return lower(number, roundingFactor);
        } else {
            return higher(number, roundingFactor);
        }
    }

    public long lower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    //__ to set the Data in the Param Map regarding the Product Data...
    private void setProdIdData(ArrayList productList) throws Exception {
        paramMap.put(InterestTaskRunner.AC_HD_ID, ((HashMap) productList.get(0)).get("AC_HD_ID")); //Ac Head
        paramMap.put(InterestTaskRunner.DEBIT_INT, ((HashMap) productList.get(0)).get("DEBIT_INT")); //Debit Int Head
        paramMap.put(InterestTaskRunner.CREDIT_INT, ((HashMap) productList.get(0)).get("CREDIT_INT")); //Credit Int Head
        paramMap.put(InterestTaskRunner.PRODUCT_ID, ((HashMap) productList.get(0)).get("PROD_ID")); //Product Id
        paramMap.put("MIN_CR_INT_AMT", ((HashMap) productList.get(0)).get("MIN_CR_INT_AMT")); //Minimum amount for Crediting Interest

        if (getHeader().getTransactionType().equalsIgnoreCase(CommonConstants.PAYABLE)) {
            paramMap.put(InterestTaskRunner.LAST_APPL_DT, ((HashMap) productList.get(0)).get("LAST_APPL_DT_CR")); //Last Interest Applied Dt Payale
            paramMap.put("TAX_APPLICABLE", ((HashMap) productList.get(0)).get("TAX_INT_APPLICABLE"));
            paramMap.put("NRO_TAX_AMT", ((HashMap) productList.get(0)).get("NRO_TAX_AMT"));
            paramMap.put("TAX_HEAD", ((HashMap) productList.get(0)).get("TAX_HEAD"));

        } else {
            paramMap.put(InterestTaskRunner.LAST_APPL_DT, ((HashMap) productList.get(0)).get("LAST_APPL_DT_DR")); //Last Interest Applied Dt Recievable
        }

        paramMap.put(InterestTaskRunner.APPL_NEW_DT, null); //Updated date based on FREQ_A from InterestTaskRunner
        paramMap.put(InterestTaskRunner.PRODUCT_TYPE, prodType);//getHeader().getProductType()) ; //Product Type; for Transaction update
    }

    public Date getProperFormatDate(Object obj) {
        Date currDt = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDt = (Date) curDate.clone();
            currDt.setDate(tempDt.getDate());
            currDt.setMonth(tempDt.getMonth());
            currDt.setYear(tempDt.getYear());
        }
        return currDt;
    }

    public static void main(String arg[]) {
        try {
            TaskHeader header = new TaskHeader();
            header.setTaskClass("InterestTask");
            //            header.setTransactionType(CommonConstants.PAYABLE);
            header.setTransactionType(CommonConstants.RECEIVABLE);
            header.setProductType(TransactionFactory.OPERATIVE);

            header.setBranchID("Bran");
            header.setIpAddr("172.19.147.86");
            header.setUserID("Psupro");

            ArrayList acctList = new ArrayList();
            acctList.add("OA060922");//, OA060910
            //            acctList.add("OA060887");

            HashMap taskParam = new HashMap();
            taskParam.put(CommonConstants.PRODUCT_ID, "SBGEN");
            //            taskParam.put(CommonConstants.ACT_NUM, "OA060892");
            taskParam.put(CommonConstants.ACT_NUM, acctList);
            taskParam.put(CommonConstants.BRANCH, "Bran");
            //            taskParam.put("DATE_FROM", DateUtil.getDate(1,10,2003));
            //            taskParam.put("DATE_TO", DateUtil.getDate(31,03,2004));

            header.setTaskParam(taskParam);

            InterestTask tsk = new InterestTask(header);
            //            TaskStatus status = tsk.executeTask();
            HashMap dataList = tsk.getInterestData();
         //   System.out.println("dataList: " + dataList);

            //            tsk.insertData();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void holiydaychecking(Date lstintCr) {
        try {
            HashMap MonthEnd = new HashMap(); //traansferto holidaychecking method;
            boolean checkHoliday = true;
            MonthEnd.put("NEXT_DATE", lstintCr);
            MonthEnd.put("BRANCH_CODE", getHeader().getBranchID());
            //                try{
            //            sqlMap.startTransaction();
            lstintCr = lstintCr;
            while (checkHoliday) {
                boolean tholiday = false;
       //         System.out.println("enterytothecheckholiday" + checkHoliday);
                List Holiday = sqlMap.executeQueryForList("checkHolidayDateOD", MonthEnd);
                List weeklyOf = sqlMap.executeQueryForList("checkWeeklyOffOD", MonthEnd);
                boolean isHoliday = Holiday.size() > 0 ? true : false;
                boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
                if (isHoliday || isWeekOff) {
                    MonthEnd = dateMinus(MonthEnd);
                    checkHoliday = true;
                } else {
                    checkHoliday = doNoHoliday(CommonUtil.convertObjToStr(MonthEnd.get("NEXT_DATE")));;

                }
                //                if(! isWeekOff)
                //                    if(isHoliday) {
                //                        MonthEnd = dateMinus(MonthEnd);
                //                        checkHoliday=true;
                //                    }
                //                    else  {
                //                        checkHoliday=doNoHoliday(CommonUtil.convertObjToStr(MonthEnd.get("NEXT_DATE")));;
                //                    }
                //                if(!isHoliday)
                //                    if(isWeekOff) {
                //                        HashMap week=(HashMap)weeklyOf.get(0);
                //                        int datenum=Lday.getDay();
                //                        int ofyes=CommonUtil.convertObjToInt(week.get("WEEKLY_OFF1"));
                //                        if(datenum==ofyes){
                //                            MonthEnd = dateMinus(MonthEnd);
                //                            checkHoliday=true;
                //                        }
                //                    }
                //                    else {
                //                        checkHoliday=doNoHoliday(CommonUtil.convertObjToStr(MonthEnd.get("NEXT_DATE")));;
                //                    }

            }
            //            sqlMap.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HashMap dateMinus(HashMap dateMap) {
        String day = CommonUtil.convertObjToStr(dateMap.get("NEXT_DATE"));
        Date lastDay = (Date) dateMap.get("NEXT_DATE");
        int days = lastDay.getDate();
        days--;
        lastDay.setDate(days);
        dateMap.put("NEXT_DATE", lastDay);
        dateMap.put("BRANCH_CODE", getHeader().getBranchID());
        return dateMap;
    }

    private boolean doNoHoliday(String nonHoliday) {
        checkThisCDate = DateUtil.getDateMMDDYYYY(nonHoliday);
     //   System.out.println("nonHoliday" + nonHoliday);
        return false;
    }
}
