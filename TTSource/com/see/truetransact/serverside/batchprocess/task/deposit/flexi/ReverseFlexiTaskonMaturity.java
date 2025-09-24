/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ReverseFlexiTaskonMaturity.java
 *
 * Created on March 8, 2005, 4:38 PM
 */
package com.see.truetransact.serverside.batchprocess.task.deposit.flexi;

import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;

import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.ibatis.db.sqlmap.SqlMap;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.InterestCalc;
import com.see.truetransact.commonutil.LocaleConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.serverside.tds.tdscalc.TdsCalc;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.batchprocess.interest.InterestBatchTO;
import com.see.truetransact.serverside.deposit.closing.DepositClosingDAO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.transferobject.deposit.DepSubNoAccInfoTO;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;

/**
 *
 * @author Sathiya
 */
public class ReverseFlexiTaskonMaturity extends Task {

    private static SqlMap sqlMap = null;
    private String BRANCH_ID = "BRAN";
    private String ACT_NUM = "";
    private String USER_ID = "";
    private String SCREEN = "TD";
    private InterestBatchTO interestBatchTO = null;
    private String dayEndType;
    private List branchList;
    private HashMap taskMap;
    private Date currDt;
    private boolean isError = false;
    private String taskLable;
    private String process = null;

    /**
     * Creates a new instance of ReverseFlexiTask
     */
    public ReverseFlexiTaskonMaturity(TaskHeader header) throws Exception {
        setHeader(header);
        process = header.getProcessType();
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        BRANCH_ID = CommonUtil.convertObjToStr(header.getBranchID());
        USER_ID = CommonUtil.convertObjToStr(header.getUserID());
        currDt = ServerUtil.getCurrentDate(BRANCH_ID);
        taskMap = header.getTaskParam();
        if (taskMap != null && taskMap.containsKey("DAY_END_TYPE")) {
            dayEndType = CommonUtil.convertObjToStr(taskMap.get("DAY_END_TYPE"));
        }
        if (CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
            HashMap tempMap = new HashMap();
            tempMap.put("NEXT_DATE", currDt);
            if (process.equals(CommonConstants.DAY_BEGIN)) {
                if (taskMap.containsKey("BRANCH_LST")) {
                    branchList = (List) taskMap.get("BRANCH_LST");
                    System.out.println("branchList*****" + branchList);
                } else {
                    branchList = (List) sqlMap.executeQueryForList("getAllBranchesFromDayEndComp", tempMap);
                }
            } else {
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
        if (taskMap != null && taskMap.containsKey("REV_FLX_MAT_TASK_LABLE")) {
            taskLable = CommonUtil.convertObjToStr(taskMap.get("REV_FLX_MAT_TASK_LABLE"));
        }
        if (taskMap != null && taskMap.containsKey("CHK_TD_REV_FLX_TASK_LABLE")) {
            taskLable = CommonUtil.convertObjToStr(taskMap.get("CHK_TD_REV_FLX_TASK_LABLE"));
        }
    }

    //__ To be Called for the Batch Process...
//    public TaskStatus executeTask() throws Exception {
//        TaskStatus status = new TaskStatus();
//        
//        //__ Common Method Call..
//        implementTask();
//        if(status.getStatus()!=BatchConstants.ERROR);
//        status.setStatus(BatchConstants.COMPLETED);
//        return status;
//    }
//    //__
//    public void reverseFlexi(String actNum) throws Exception {
//        ACT_NUM = actNum;//__ Common Method Call..
//        implementTask();
//    }
    //__ Common method to Transfer the amount from Flexi_deposit to Available Balance...
    public TaskStatus executeTask() throws Exception {
        TaskStatus status = new TaskStatus();
        HashMap dataMap = new HashMap();
        dataMap.put("MATURITY_DT", currDt);
        //__ if the Act_num is present...
        //        dataMap.put("ACT_NUM", ACT_NUM);
        if (branchList != null && branchList.size() > 0) {
            for (int b = 0; b < branchList.size(); b++) {
                HashMap branchMap = (HashMap) branchList.get(b);
                BRANCH_ID = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                currDt = ServerUtil.getCurrentDate(BRANCH_ID);
                HashMap compMap = new HashMap();
                compMap.put("TASK_NAME", taskLable);
                compMap.put("DAYEND_DT", currDt);
                compMap.put("BRANCH_ID", BRANCH_ID);
                List compLst = (List) sqlMap.executeQueryForList("getSelectTaskLstDayBegin", compMap);
                compMap = null;
                String compStatus = "";
                if (compLst != null && compLst.size() > 0) {
                    compMap = (HashMap) compLst.get(0);
                    compStatus = CommonUtil.convertObjToStr(compMap.get("TASK_STATUS"));
                    compMap = null;
                }
                if (compLst.size() <= 0 || compStatus.equals("ERROR")) {

                    int count = holidayProvision((Date) currDt.clone(), BRANCH_ID, USER_ID);
                    Date holidayDate = null;
                    //                System.out.println("count Normal : " + count);//__ To Know the Mode...
                    if (count > 0) {
                        holidayDate = (Date) currDt.clone();
                        holidayDate = DateUtil.addDays(holidayDate, -count);
                        if (holidayDate.getDate() > 0) {
                            holidayDate.setDate(holidayDate.getDate());
                            holidayDate.setMonth(holidayDate.getMonth());
                            holidayDate.setYear(holidayDate.getYear());
                        }
                        //                    System.out.println("holidayDate Normal : " + holidayDate);//__ To Know the Mode...
                        dataMap.put("HOLIDAY_MATURITY_DT", holidayDate);
                        //                    System.out.println("count Normal : " + count);//__ To Know the Mode...
                    } else {
                        dataMap.put("HOLIDAY_MATURITY_DT", currDt);
                    }
                    dataMap.put(CommonConstants.BRANCH_ID, BRANCH_ID);

                    sqlMap.startTransaction();
                    HashMap errorMap = new HashMap();
                    errorMap.put("ERROR_DATE", currDt);
                    errorMap.put("TASK_NAME", taskLable);
                    errorMap.put("BRANCH_ID", BRANCH_ID);
                    sqlMap.executeUpdate("deleteError_showing", errorMap);
                    sqlMap.commitTransaction();

                    ArrayList acctList = (ArrayList) getDepositAmt(dataMap);
                    //                System.out.println("acctList: " + acctList.size());
                    for (int i = 0; i < acctList.size(); i++) {//__ If the List Contains some Data...
                        dataMap = (HashMap) acctList.get(i);
                        System.out.println("dataMap Normal : " + dataMap);//__ To Know the Mode...
                        try {
                            sqlMap.startTransaction();
                            status.setStatus(BatchConstants.STARTED);
                            HashMap depositHeadMap = new HashMap();
                            depositHeadMap.put("PROD_ID", dataMap.get("FLEXI_PROD"));
                            depositHeadMap = (HashMap) sqlMap.executeQueryForObject("getDepositClosingHeads", depositHeadMap);
                            //                        System.out.println("depositHeadMap Normal : " + depositHeadMap);
                            HashMap txMap = new HashMap();
                            ArrayList transferList = new ArrayList();
                            TransferTrans transferTrans = new TransferTrans();
                            TxTransferTO transferTo = new TxTransferTO();
                            //                        double flexiAmt = CommonUtil.convertObjToDouble(dataMap.get("FLEXI_DEPOSIT_AMT")).doubleValue();
                            Date depositDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dataMap.get("DEPOSIT_DT")));
                            Date maturityDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dataMap.get("MATURITY_DT")));
                            double depositAmt = CommonUtil.convertObjToDouble(dataMap.get("TOTAL_BALANCE")).doubleValue();
                            double depAmt = CommonUtil.convertObjToDouble(dataMap.get("TOTAL_BALANCE")).doubleValue();
                            double credit = CommonUtil.convertObjToDouble(dataMap.get("TOTAL_INT_CREDIT")).doubleValue();
                            double drawn = CommonUtil.convertObjToDouble(dataMap.get("TOTAL_INT_DRAWN")).doubleValue();
                            double totIntAmt = CommonUtil.convertObjToDouble(dataMap.get("TOT_INT_AMT")).doubleValue();
                            double rateOfInt = CommonUtil.convertObjToDouble(dataMap.get("RATE_OF_INT")).doubleValue();
                            double balance = totIntAmt - credit;
                            double payable = credit - drawn;
                            if (holidayDate != null && holidayDate.getDate() > 0 && DateUtil.dateDiff(maturityDt, holidayDate) > 0) {
                                double holidayIntAmt = depositAmt * rateOfInt * count / 36500;
                                //                            System.out.println("holidayIntAmt Normal : " + holidayIntAmt);
                                balance = balance + holidayIntAmt;
                                balance = (double) getNearest((long) ((long) balance * 100), 100) / 100;
                                //                            System.out.println("balance Normal : " + balance);
                            }
                            //__ To Update the Avail Balance and Flexi Deposit Amount
                            HashMap inputMap = new HashMap();
                            double minBal2 = CommonUtil.convertObjToDouble(dataMap.get("MIN_BAL")).doubleValue();
                            double availBalance = CommonUtil.convertObjToDouble(dataMap.get("AVAILABLE_BALANCE")).doubleValue();
                            double totalDepBal = CommonUtil.convertObjToDouble(dataMap.get("TOTAL_BALANCE")).doubleValue();
                            double deptAmt = 0.0;
                            //                        HashMap alreadyReducedMap = new HashMap();
                            //                        alreadyReducedMap.put("FLEXI_ACT_NUM",dataMap.get("FLEXI_ACT_NUM"));
                            //                        List lstAlready = sqlMap.executeQueryForList("getSelectAlreadyReduced",alreadyReducedMap);
                            //                        if(lstAlready!=null && lstAlready.size()>0){
                            //                            alreadyReducedMap = (HashMap)lstAlready.get(0);
                            //                            double flexiAmt = CommonUtil.convertObjToDouble(alreadyReducedMap.get("TOTAL_BALANCE")).doubleValue();
                            //                            if(flexiAmt == totalDepBal){
                            //                                deptAmt = depAmt;
                            //                                inputMap.put("FLEXI_DEOSIT_AMT",new Double(deptAmt * -1));
                            //                                System.out.println("deptAmt inside if Normal : " + deptAmt);
                            //                            }else{
                            ////                                flexiAmt = flexiAmt - totalDepBal;
                            //                                deptAmt = depAmt - minBal2;
                            //                                inputMap.put("FLEXI_DEOSIT_AMT",new Double(deptAmt * -1));
                            //                                System.out.println("deptAmt inside else Normal : " + deptAmt);
                            //                            }
                            //                            System.out.println("deptAmt if Normal : " + deptAmt);
                            //                        }else{
                            //                            deptAmt = depAmt - minBal2;
                            //                            inputMap.put("FLEXI_DEOSIT_AMT",new Double(deptAmt * -1));
                            //                            System.out.println("deptAmt else Normal : " + deptAmt);
                            //                        }
                            //                        if(flexiAmt == totalDepBal){
                            //                            deptAmt = depAmt - minBal2;
                            //                            inputMap.put("FLEXI_DEOSIT_AMT",new Double(deptAmt * -1));
                            //                            System.out.println("deptAmt inside if Normal : " + deptAmt);
                            //                        }else{
                            deptAmt = depAmt;
                            inputMap.put("FLEXI_DEOSIT_AMT", new Double(deptAmt * -1));
                            System.out.println("deptAmt else Normal : " + deptAmt);
                            //                        }
                            System.out.println("deptAmt Normal : " + deptAmt);
                            inputMap.put("DEOSIT_AMT", new Double(depAmt * -1));
                            inputMap.put("ACT_NUM", dataMap.get("FLEXI_ACT_NUM"));
                            updateAvailBalance(inputMap);
                            HashMap operativeHeadMap = new HashMap();
                            operativeHeadMap.put("ACT_NUM", dataMap.get("FLEXI_ACT_NUM"));
                            HashMap operativeAcHeads = (HashMap) sqlMap.executeQueryForObject("getAccNoProdIdDet", operativeHeadMap);
                            //                        System.out.println("operativeAcHeads: " + operativeAcHeads);
                            transferTrans.setInitiatedBranch(BRANCH_ID);
                            if (balance > 0) {
                                txMap = new HashMap();
                                transferList = new ArrayList();
                                txMap.put(TransferTrans.DR_AC_HD, (String) depositHeadMap.get("INT_PROV_ACHD"));//debiting to int paid a/c head
                                txMap.put(TransferTrans.PARTICULARS, " " + dataMap.get("DEPOSITNO") + "_1");
                                txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);

                                txMap.put(TransferTrans.CR_AC_HD, (String) depositHeadMap.get("INT_PAY")); // credited to interest payable account head......
                                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);

                                transferTo = transferTrans.getDebitTransferTO(txMap, Math.abs(balance));
                                transferList.add(transferTo);
                                transferTo = transferTrans.getCreditTransferTO(txMap, Math.abs(balance));
                                transferList.add(transferTo);
                                transferTo.setAuthorizeRemarks("REVERSE_FLEXI");
                                transferTrans.doDebitCredit(transferList, BRANCH_ID);

                                txMap = new HashMap();
                                transferList = new ArrayList();
                                txMap.put(TransferTrans.DR_AC_HD, (String) depositHeadMap.get("INT_PAY"));//debiting int payable a/c head
                                txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, " value " + dataMap.get("FLEXI_ACT_NUM"));

                                txMap.put(TransferTrans.CR_AC_HD, (String) operativeAcHeads.get("AC_HD_ID"));
                                txMap.put(TransferTrans.CR_PROD_ID, dataMap.get("PROD_ID"));
                                txMap.put(TransferTrans.CR_ACT_NUM, dataMap.get("FLEXI_ACT_NUM"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                txMap.put(TransferTrans.CURRENCY, "INR");

                                transferTo = transferTrans.getDebitTransferTO(txMap, Math.abs(balance + payable));
                                transferList.add(transferTo);
                                txMap.put(TransferTrans.PARTICULARS, " " + dataMap.get("DEPOSITNO") + "_1");
                                transferTo = transferTrans.getCreditTransferTO(txMap, Math.abs(balance + payable));
                                transferList.add(transferTo);
                                transferTo.setAuthorizeRemarks("REVERSE_FLEXI");
                                transferTrans.doDebitCredit(transferList, BRANCH_ID);
                            }
                            if (depositAmt > 0) {
                                txMap = new HashMap();
                                transferList = new ArrayList();
                                txMap.put(TransferTrans.DR_AC_HD, (String) depositHeadMap.get("FIXED_DEPOSIT_ACHD"));
                                txMap.put(TransferTrans.DR_PROD_ID, dataMap.get("FLEXI_PROD"));
                                txMap.put(TransferTrans.DR_ACT_NUM, dataMap.get("DEPOSITNO") + "_1");
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);

                                txMap.put(TransferTrans.CR_AC_HD, (String) operativeAcHeads.get("AC_HD_ID"));
                                txMap.put(TransferTrans.CR_PROD_ID, dataMap.get("PROD_ID"));
                                txMap.put(TransferTrans.CR_ACT_NUM, dataMap.get("FLEXI_ACT_NUM"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                txMap.put(TransferTrans.PARTICULARS, "On Maturity Closing Flexi Deposit " + " " + dataMap.get("DEPOSITNO") + "_1");

                                //                            System.out.println("****** txMap 1st depositAmt Normal : "+txMap);
                                transferTo = transferTrans.getDebitTransferTO(txMap, Math.abs(depositAmt));
                                transferList.add(transferTo);
                                txMap.put(TransferTrans.PARTICULARS, " " + dataMap.get("FLEXI_ACT_NUM"));
                                transferTo = transferTrans.getCreditTransferTO(txMap, Math.abs(depositAmt));
                                transferList.add(transferTo);
                                transferTo.setAuthorizeRemarks("REVERSE_FLEXI");
                                transferTrans.doDebitCredit(transferList, BRANCH_ID);
                            }
                            HashMap tdsCalcMap = new HashMap();
                            tdsCalcMap.put("CUST_ID", dataMap.get("CUST_ID"));
                            tdsCalcMap.put("PROD_ID", dataMap.get("FLEXI_PROD"));
                            tdsCalcMap.put("DEPOSIT_NO", dataMap.get("DEPOSITNO"));
                            tdsCalcMap.put("RATE_OF_INT", dataMap.get("RATE_OF_INT"));
                            tdsCalcMap.put("INT_PAY", depositHeadMap.get("INT_PAY"));
                            tdsCalcMap.put("INT_PROV_ACHD", depositHeadMap.get("INT_PROV_ACHD"));
                            //                        System.out.println("******tdsCalcMap Normal : "+tdsCalcMap);
                            if (balance != 0) {
                                updatingDepositInterestTable(tdsCalcMap, balance, depositAmt, totIntAmt, currDt, payable, maturityDt, depositDt);
                            }
                            //                        System.out.println("******tdsCalcMap calcuateTDS : "+tdsCalcMap);
                            calcuateTDS(tdsCalcMap, balance, depositAmt, totIntAmt, currDt);                //calcualting TDS...
                            HashMap depositMap = new HashMap();
                            depositMap.put("DEPOSIT_NO", dataMap.get("DEPOSITNO"));
                            depositMap.put("DEPOSIT_DT", dataMap.get("DEPOSIT_DT"));
                            depositMap.put("TOTAL_INT_CREDIT", new Double(balance + payable));
                            depositMap.put("TOTAL_INT_DRAWN", new Double(balance + payable));
                            depositMap.put("CURR_RATE_OF_INT", dataMap.get("RATE_OF_INT"));
                            depositMap.put("INTEREST_AMT", new Double(balance + payable));
                            depositMap.put("DEPOSIT_STATUS", CommonConstants.CLOSED);
                            depositMap.put("ACCT_STATUS", CommonConstants.CLOSED);
                            depositMap.put("CLOSE_DT", currDt);
                            //                        System.out.println("******depositMap : "+depositMap);
                            sqlMap.executeUpdate("updateFlexiClosedDetails", depositMap);
                            sqlMap.executeUpdate("updateFlexiClosedSubDetails", depositMap);
                            DepSubNoAccInfoTO depSubNoAccInfoTO = new DepSubNoAccInfoTO();
                            HashMap oldMap = new HashMap();
                            oldMap.put("DEPOSIT NO", dataMap.get("DEPOSITNO"));
                            List lst = (List) sqlMap.executeQueryForList("getSelectDepSubNoAccInfoTO", oldMap);
                            if (lst != null && lst.size() > 0) {
                                oldMap = (HashMap) lst.get(0);
                                depSubNoAccInfoTO.setDepositNo(CommonUtil.convertObjToStr(oldMap.get("DEPOSIT_NO")));
                                depSubNoAccInfoTO.setDepositSubNo(CommonUtil.convertObjToInt(oldMap.get("DEPOSIT_SUB_NO")));
                                depSubNoAccInfoTO.setDepositDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oldMap.get("DEPOSIT_DT"))));
                                depSubNoAccInfoTO.setDepositPeriodDd(CommonUtil.convertObjToDouble(oldMap.get("DEPOSIT_PERIOD_DD")));
                                depSubNoAccInfoTO.setDepositPeriodMm(CommonUtil.convertObjToDouble(oldMap.get("DEPOSIT_PERIOD_MM")));
                                depSubNoAccInfoTO.setDepositPeriodYy(CommonUtil.convertObjToDouble(oldMap.get("DEPOSIT_PERIOD_YY")));
                                depSubNoAccInfoTO.setDepositAmt(CommonUtil.convertObjToDouble(oldMap.get("DEPOSIT_AMT")));
                                depSubNoAccInfoTO.setIntpayMode(CommonUtil.convertObjToStr(oldMap.get("INTPAY_MODE")));
                                depSubNoAccInfoTO.setIntpayFreq(CommonUtil.convertObjToDouble(oldMap.get("INTPAY_FREQ")));
                                depSubNoAccInfoTO.setMaturityDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oldMap.get("MATURITY_DT"))));
                                depSubNoAccInfoTO.setRateOfInt(CommonUtil.convertObjToDouble(oldMap.get("RATE_OF_INT")));
                                depSubNoAccInfoTO.setMaturityAmt(CommonUtil.convertObjToDouble(oldMap.get("MATURITY_AMT")));
                                depSubNoAccInfoTO.setTotIntAmt(CommonUtil.convertObjToDouble(oldMap.get("TOT_INT_AMT")));
                                depSubNoAccInfoTO.setPeriodicIntAmt(CommonUtil.convertObjToDouble(oldMap.get("PERIODIC_INT_AMT")));
                                depSubNoAccInfoTO.setStatus(CommonUtil.convertObjToStr(oldMap.get("STATUS")));
                                depSubNoAccInfoTO.setClearBalance(new Double(0.0));
                                depSubNoAccInfoTO.setAvailableBalance(new Double(0.0));
                                depSubNoAccInfoTO.setCreateBy(CommonUtil.convertObjToStr(oldMap.get("CREATE_BY")));
                                depSubNoAccInfoTO.setCloseDt(currDt);
                                depSubNoAccInfoTO.setCloseBy(CommonUtil.convertObjToStr(CommonConstants.TTSYSTEM));
                                depSubNoAccInfoTO.setAuthorizeStatus(CommonUtil.convertObjToStr(oldMap.get("AUTHORIZE_STATUS")));
                                depSubNoAccInfoTO.setAuthorizeDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oldMap.get("AUTHORIZE_DT"))));
                                depSubNoAccInfoTO.setAuthorizeBy(CommonUtil.convertObjToStr(oldMap.get("AUTHORIZE_BY")));
                                depSubNoAccInfoTO.setAcctStatus(CommonConstants.CLOSED);
                                depSubNoAccInfoTO.setLastIntApplDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oldMap.get("LAST_INT_APPL_DT"))));
                                depSubNoAccInfoTO.setTotalIntCredit(CommonUtil.convertObjToDouble(oldMap.get("TOTAL_INT_CREDIT")));
                                depSubNoAccInfoTO.setTotalIntDrawn(CommonUtil.convertObjToDouble(oldMap.get("TOTAL_INT_DRAWN")));
                                depSubNoAccInfoTO.setTotalBalance(new Double(0.0));
                                depSubNoAccInfoTO.setInstallType(CommonUtil.convertObjToStr(oldMap.get("INSTALL_TYPE")));
                                depSubNoAccInfoTO.setPaymentDay((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr("")));
                                depSubNoAccInfoTO.setCalender_day(new Double(0));
                                depSubNoAccInfoTO.setFlexi_status("Y");
                                depSubNoAccInfoTO.setRenewedDt(getProperDateFormat(oldMap.get("RENEWED_DT")));
                                sqlMap.executeUpdate("insertDepSubNoAccInfoTOSameNo", depSubNoAccInfoTO);
                                HashMap intMap = new HashMap();
                                intMap.put("DEPOSIT_NO", oldMap.get("DEPOSIT_NO"));
                                lst = (List) sqlMap.executeQueryForList("getSelectDepSubNoIntDetails", intMap);
                                if (lst != null && lst.size() > 0) {
                                    intMap = (HashMap) lst.get(0);
                                    intMap.put("ACT_NUM", intMap.get("DEPOSIT_NO"));
                                    intMap.put("ACCT_STATUS", CommonConstants.CLOSED);
                                    intMap.put("CURR_RATE_OF_INT", CommonUtil.convertObjToDouble(intMap.get("CURR_RATE_OF_INT")).doubleValue());
                                    intMap.put("SB_INT_AMT", CommonUtil.convertObjToDouble(intMap.get("SB_INT_AMT")).doubleValue());
                                    //intMap.put("SB_PERIOD_RUN", String.valueOf(CommonUtil.convertObjToDouble(intMap.get("SB_PERIOD_RUN")).doubleValue()));
                                    intMap.put("SB_PERIOD_RUN", CommonUtil.convertObjToDouble(intMap.get("SB_PERIOD_RUN")).doubleValue());
                                    intMap.put("BAL_INT_AMT", CommonUtil.convertObjToDouble(CommonUtil.convertObjToDouble(intMap.get("INTEREST_AMT")).doubleValue()));
                                    intMap.put("INT", String.valueOf(CommonUtil.convertObjToDouble(intMap.get("PENAL_RATE")).doubleValue()));
                                    sqlMap.executeUpdate("updateSbInterestAmountSameNo", intMap);//sameno table itwill store.
                                    intMap = null;
                                }
                            }
                            lst = null;
                            HashMap renewalMap = new HashMap();
                            renewalMap.put("DEPOSIT_NO", dataMap.get("DEPOSITNO"));
                            lst = sqlMap.executeQueryForList("getSelectMaxSLNo", renewalMap);
                            if (lst != null && lst.size() > 0) {
                                renewalMap = (HashMap) lst.get(0);
                                double maxNo = CommonUtil.convertObjToDouble(renewalMap.get("MAX_NO")).doubleValue();
                                renewalMap.put("DEPOSIT_NO", dataMap.get("DEPOSITNO"));
                                renewalMap.put("COUNT", new Double(maxNo + 1));
                                sqlMap.executeUpdate("updateMaxSLNo", renewalMap);
                            } else {
                                renewalMap.put("DEPOSIT_NO", dataMap.get("DEPOSITNO"));
                                renewalMap.put("COUNT", new Double(1));
                                sqlMap.executeUpdate("updateMaxSLNo", renewalMap);
                            }
                            HashMap flexiAmtMap = new HashMap();
                            flexiAmtMap.put("ACT_NUM", dataMap.get("FLEXI_ACT_NUM"));
                            List lstFlexi = sqlMap.executeQueryForList("getSelectFlexiAmtDetails", flexiAmtMap);
                            if (lstFlexi != null && lstFlexi.size() > 0) {
                                flexiAmtMap = (HashMap) lstFlexi.get(0);
                                double flexiAmt = CommonUtil.convertObjToDouble(flexiAmtMap.get("FLEXI_DEPOSIT_AMT")).doubleValue();
                                if (flexiAmt == 0) {
                                    sqlMap.executeUpdate("updateFlexiAmtDetails", flexiAmtMap);
                                }
                            }
                            flexiAmtMap = null;
                            lstFlexi = null;
                            lst = null;
                            renewalMap = null;
                            depSubNoAccInfoTO = null;
                            oldMap = null;
                            inputMap = null;
                            txMap = null;
                            transferList = null;
                            transferTrans = null;
                            transferTo = null;
                            depositHeadMap = null;
                            operativeHeadMap = null;
                            depositMap = null;
                            tdsCalcMap = null;
                            lst = null;
                            sqlMap.commitTransaction();
                        } catch (Exception e) {
                            //                        sqlMap.rollbackTransaction();
                            //                        status.setStatus(BatchConstants.ERROR) ;
                            //                        errorMap = new HashMap();
                            //                        errorMap.put("ERROR_DATE",currDt);
                            //                        errorMap.put("TASK_NAME", "ReverseFlexiTaskonMaturity");
                            //                        errorMap.put("ERROR_MSG",e.getMessage());
                            //                        errorMap.put("ACT_NUM",dataMap.get("FLEXI_ACT_NUM"));
                            //                        errorMap.put("BRANCH_ID", BRANCH_ID);
                            //                        sqlMap.startTransaction();
                            //                        sqlMap.executeUpdate("insertError_showing", errorMap);
                            //                        sqlMap.commitTransaction();
                            //                        System.out.println("Error thrown for Depsoit No " + dataMap.get("DEPOSITNO"));
                            //                        e.printStackTrace();
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
                                            for (int a = 0; a < list.size(); a++) {
                                                if (list.get(a) instanceof HashMap) {
                                                    excMap = (HashMap) list.get(a);
                                                    System.out.println("#$#$ if TTException part excMap ..." + excMap);
                                                    strExc = CommonUtil.convertObjToStr(excMap.get(CommonConstants.ERR_MESSAGE))
                                                            + " (" + CommonUtil.convertObjToStr(excMap.get(CommonConstants.ERR_DATA)) + ")";
                                                } else {
                                                    strExc = (String) list.get(i);
                                                    System.out.println("#$#$ if TTException part strExc ..." + strExc);
                                                }
                                                errorMap = new HashMap();
                                                errorMap.put("ERROR_DATE", currDt);
                                                errorMap.put("TASK_NAME", taskLable);
                                                errorMap.put("ERROR_MSG", strExc);
                                                errorMap.put("ERROR_CLASS", errClassName);
                                                errorMap.put("ACT_NUM", dataMap.get("FLEXI_ACT_NUM"));
                                                errorMap.put("BRANCH_ID", BRANCH_ID);
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
                                        errorMap.put("ERROR_DATE", currDt);
                                        errorMap.put("TASK_NAME", taskLable);
                                        errorMap.put("ERROR_MSG", errMsg);
                                        errorMap.put("ERROR_CLASS", errClassName);
                                        System.out.println("@@@@ggggggg" + dataMap.get("FLEXI_ACT_NUM"));
                                        errorMap.put("ACT_NUM", dataMap.get("FLEXI_ACT_NUM"));
                                        errorMap.put("BRANCH_ID", BRANCH_ID);
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
                                errorMap.put("ERROR_DATE", currDt);
                                errorMap.put("TASK_NAME", taskLable);
                                errorMap.put("ERROR_MSG", errMsg);
                                errorMap.put("ERROR_CLASS", errClassName);
                                errorMap.put("ACT_NUM", dataMap.get("FLEXI_ACT_NUM"));
                                errorMap.put("BRANCH_ID", BRANCH_ID);
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
                        dataMap = null;
                    }
                    acctList = null;

                }
                if (!compStatus.equals("COMPLETED")) {
                    if (status.getStatus() != BatchConstants.ERROR) {
                        sqlMap.startTransaction();
                        if (compStatus.equals("ERROR")) {
                            HashMap statusMap = new HashMap();
                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                            statusMap.put("BRANCH_CODE", BRANCH_ID);
                            statusMap.put("TASK_NAME", taskLable);
                            statusMap.put("TASK_STATUS", "COMPLETED");
                            statusMap.put("USER_ID", getHeader().getUserID());
                            statusMap.put("DAYEND_DT", currDt);
                            sqlMap.executeUpdate("updateTskStatusDayBegin", statusMap);
                            statusMap = null;
                        } else {
                            HashMap statusMap = new HashMap();
                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                            statusMap.put("BRANCH_CODE", BRANCH_ID);
                            statusMap.put("TASK_NAME", taskLable);
                            statusMap.put("TASK_STATUS", "COMPLETED");
                            statusMap.put("USER_ID", getHeader().getUserID());
                            statusMap.put("DAYEND_DT", currDt);
                            sqlMap.executeUpdate("InsertDayEndStatusDayBegin", statusMap);
                            statusMap = null;
                        }
                        sqlMap.commitTransaction();
                    } else {
                        if (compStatus.equals("ERROR")) {
                            HashMap statusMap = new HashMap();
                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                            statusMap.put("BRANCH_CODE", BRANCH_ID);
                            statusMap.put("TASK_NAME", taskLable);
                            statusMap.put("TASK_STATUS", "ERROR");
                            statusMap.put("USER_ID", getHeader().getUserID());
                            statusMap.put("DAYEND_DT", currDt);
                            sqlMap.executeUpdate("updateTskStatusDayBegin", statusMap);
                            statusMap = null;
                        } else {
                            isError = true;
                            HashMap statusMap = new HashMap();
                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                            statusMap.put("BRANCH_CODE", BRANCH_ID);
                            statusMap.put("TASK_NAME", taskLable);
                            statusMap.put("TASK_STATUS", "ERROR");
                            statusMap.put("USER_ID", getHeader().getUserID());
                            statusMap.put("DAYEND_DT", currDt);
                            sqlMap.executeUpdate("InsertDayEndStatusDayBegin", statusMap);
                            statusMap = null;

                        }
                    }
                }

            }
        }
        if (status.getStatus() != BatchConstants.ERROR) {
            status.setStatus(BatchConstants.COMPLETED);
        }
        dataMap = null;
        return status;
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

    private void updatingDepositInterestTable(HashMap interestMap, double balance, double depositAmt,
            double totIntAmt, Date currDt, double payable, Date maturityDt, Date depositDt) throws Exception {
        System.out.println("interestMap :" + interestMap + "balance :" + balance + "depositAmt :" + depositAmt + "totIntAmt :" + totIntAmt);
        interestBatchTO = new InterestBatchTO();
        Date matDt = (Date) currDt.clone();
        if (maturityDt != null) {
            matDt.setDate(maturityDt.getDate());
            matDt.setMonth(maturityDt.getMonth());
            matDt.setYear(maturityDt.getYear());
        }
        Date depDt = (Date) currDt.clone();
        if (depositDt != null) {
            depDt.setDate(depositDt.getDate());
            depDt.setMonth(depositDt.getMonth());
            depDt.setYear(depositDt.getYear());
        }
        interestBatchTO.setIntDt(depDt);
        interestBatchTO.setApplDt(matDt);
        interestBatchTO.setActNum(interestMap.get("DEPOSIT_NO") + "_1");
        interestBatchTO.setProductId(CommonUtil.convertObjToStr(interestMap.get("PROD_ID")));
        interestBatchTO.setPrincipleAmt(new Double(depositAmt));
        interestBatchTO.setCustId(CommonUtil.convertObjToStr(interestMap.get("CUST_ID")));
        interestBatchTO.setIntRate(CommonUtil.convertObjToDouble(interestMap.get("RATE_OF_INT")));
        HashMap prodMap = new HashMap();
        prodMap.put("PROD_ID", interestMap.get("PROD_ID"));
        List resList = (List) sqlMap.executeQueryForList("getBehavesLike", prodMap);
        prodMap.put("BEHAVES_LIKE", ((HashMap) resList.get(0)).get("BEHAVES_LIKE"));
        if (prodMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
            interestBatchTO.setIntType("COMPOUND");
        } else if (prodMap.get("BEHAVES_LIKE").equals("FIXED")) {
            interestBatchTO.setIntType("SIMPLE");
        }
        interestBatchTO.setAcHdId(CommonUtil.convertObjToStr(interestMap.get("INT_PROV_ACHD")));
        interestBatchTO.setProductType("TD");
        interestBatchTO.setTrnDt(currDt);
        HashMap depMap = new HashMap();
        if (balance != 0) {
            if (balance > 0) {
                interestBatchTO.setDrCr("CREDIT");
                interestBatchTO.setTransLogId("Payable");
                interestBatchTO.setIntAmt(new Double(balance));
                interestBatchTO.setTot_int_amt(new Double(balance));
                sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
            }
            if (totIntAmt > 0) {
                interestBatchTO.setDrCr("DEBIT");
                interestBatchTO.setAcHdId(CommonUtil.convertObjToStr(interestMap.get("INT_PAY")));
                interestBatchTO.setTransLogId("Payable");
                interestBatchTO.setIntAmt(new Double(balance + payable));
                interestBatchTO.setTot_int_amt(new Double(0.0));
                sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
            }
        }
        interestMap = null;
        balance = 0;
        depositAmt = 0;
        totIntAmt = 0;
        interestBatchTO = null;
    }

    private HashMap calcuateTDS(HashMap tdsCalcMap, double balance, double depositAmt, double totIntAmt, Date currDt) throws Exception {
        System.out.println("####### doDepositClose tdsCalcMap " + tdsCalcMap);
        double intTrfAmt = 0.0;
        String Prod_type = "TD";
        interestBatchTO = new InterestBatchTO();
        TdsCalc tdsCalculator = new TdsCalc(super._branchCode);
        String CustId = CommonUtil.convertObjToStr(tdsCalcMap.get("CUST_ID"));
        String prod_id = CommonUtil.convertObjToStr(tdsCalcMap.get("PROD_ID"));
        String accnum = CommonUtil.convertObjToStr(tdsCalcMap.get("DEPOSIT_NO"));
        intTrfAmt = balance;
        HashMap tdsMap = new HashMap();
        HashMap closeMap = new HashMap();
        closeMap.put("DEPOSIT_NO", tdsCalcMap.get("DEPOSIT_NO"));
        closeMap.put("RATE_OF_INT", tdsCalcMap.get("RATE_OF_INT"));
        closeMap.put("CUSTID", tdsCalcMap.get("CUST_ID"));
        tdsMap.put("INT_DATE", currDt);
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

    private int holidayProvision(Date matDt, String BRANCH_ID, String USER_ID) throws Exception {
        int count = 0;
        HashMap weeklyOff = new HashMap();
        HashMap holidayMap = new HashMap();
        weeklyOff.put("NEXT_DATE", DateUtil.addDays(matDt, -1));
        weeklyOff.put("BRANCH_CODE", BRANCH_ID);
        weeklyOff.put("CURR_DATE", matDt);
        List lst = sqlMap.executeQueryForList("checkHolidayProvisionTD", weeklyOff);
        if (lst != null && lst.size() > 0) {
            for (int j = 0; j < lst.size(); j++) {
                count = count + 1;
            }
            holidayMap.put("NEXT_DATE", matDt);
            holidayMap.put("BRANCH_CODE", BRANCH_ID);
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
        return count;
    }
    //__ To be Called from the Code...

    private List getDepositAmt(HashMap dataMap) throws Exception {
        List list = sqlMap.executeQueryForList("ReverseFlexi.getDepositData", dataMap);
        return list;
    }

    //__ To Update the Avail Balance and Flexi Deposit Amount
    private void updateAvailBalance(HashMap dataMap) throws Exception {
        sqlMap.executeUpdate("Flexi.updateFlexiBalance", dataMap);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            TaskHeader header = new TaskHeader();

            header.setBranchID("Bran");
            header.setUserID(CommonConstants.TTSYSTEM);
            header.setIpAddr("172.19.147.86");
            header.setUserID("sysadmin");

            ReverseFlexiTask tsk = new ReverseFlexiTask(header);
            //            TaskStatus Status = tsk.executeTask();
            //            System.out.println("Status: " + Status);

//            tsk.reverseFlexi("OA060897");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
    
}
