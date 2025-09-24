/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ExcessTransChrgesTask.java
 *
 * Created on September 10, 2004, 2:34 PM
 */
package com.see.truetransact.serverside.charges;

import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.BatchConstants;

import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.servicelocator.ServiceLocator;

import com.see.truetransact.transferobject.batchprocess.charges.ChargesBatchTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;

import com.see.truetransact.serverside.transaction.transfer.*;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;

import com.ibatis.db.sqlmap.SqlMap;

import java.util.GregorianCalendar;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author rahul
 */
public class ExcessTransChrgesTask extends Charge {

    private TransferDAO transferDAO = new TransferDAO();
    private ChargesBatchTO objChargesBatchTO;
    private final String acctHead = "AC_HD_ID";
    private final String prodId = "PROD_ID";
    private final String acctNum = "ACT_NUM";
    private final String currency = "BASE_CURRENCY";
    private final String branch = "BRAN";
    private final String user = CommonConstants.TTSYSTEM;
    private final String chrgType = "EXCESS_WITHDRAWLS_TRANS";
    private final String chrgAmt = "EXCESS_WITHD_CHRG";
    private final String xsChgAcctHead = "EXCESS_FREE_WITHD";
    private String strCurrrentdate = "";
    private final int OPENDAY = 13;
    private final int SPECIFICDATE = 14;

    /**
     * Creates a new instance of ExcessTransChrgesTask
     */
    public ExcessTransChrgesTask(TaskHeader taskHeader) throws Exception {
        super(taskHeader);
        System.out.println("taskHeader:" + taskHeader);
    }

    private String fixSFormat(int freqType, HashMap resultMap) {
        String sformat = "";
        if ((freqType != OPENDAY) && (freqType != SPECIFICDATE)) {
            System.out.println("First of Specific Month");
            /**
             * The Cycle date Starts from the First of the Specified Month...
             */
            StringBuffer strDate = new StringBuffer();
            strDate.append(freqType + "/");

            Date currentDate = currDt;
            strDate.append(String.valueOf(currentDate.getDate()) + "/");
            strDate.append(String.valueOf(currentDate.getYear() + 1900));

            /**
             * if the Month is less than 10, append 0 to it...
             */
            if (freqType < 10) {
                sformat = String.valueOf(currentDate.getYear() + 1900) + "/" + "0" + String.valueOf(freqType);
            } else {
                sformat = String.valueOf(currentDate.getYear() + 1900) + "/" + String.valueOf(freqType);
            }

            this.strCurrrentdate = strDate.toString();
        } else if (freqType == SPECIFICDATE) {
            /**
             * The Cycle date Starts from the Specific date...
             */
            //System.out.println("Specific Date");
            Date currentDate = (Date) resultMap.get("FREE_WITHDRAWAL_FROM");
            this.strCurrrentdate = DateUtil.getStringDate(currentDate);

            if (currentDate.getMonth() < 10) {
                sformat = String.valueOf(currentDate.getYear() + 1900) + "/" + "0" + String.valueOf(currentDate.getMonth());
            } else {
                sformat = String.valueOf(currentDate.getYear() + 1900) + "/" + String.valueOf(currentDate.getMonth());
            }
        } else if (freqType == OPENDAY) {
            /**
             * The Cycle date Starts from the Account Opening date...
             */
            System.out.println("Account Opening Date");

            Date currentDate = (Date) resultMap.get("CREATE_DT");
            this.strCurrrentdate = DateUtil.getStringDate(currentDate);

            if (currentDate.getMonth() < 10) {
                sformat = String.valueOf(currentDate.getYear() + 1900) + "/" + "0" + String.valueOf(currentDate.getMonth());
            } else {
                sformat = String.valueOf(currentDate.getYear() + 1900) + "/" + String.valueOf(currentDate.getMonth());
            }
        }
        //sformat ="2004/02" ; //To test ONLY
        return sformat;
    }

    private String fixEFormat(int freqPeriod) {
        Date workingDate = DateUtil.getDateMMDDYYYY(this.strCurrrentdate);
        System.out.println("Working date  :" + workingDate);

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(workingDate);
        calendar.add(calendar.MONTH, (freqPeriod / 30));

        Date endDate = calendar.getTime();
        int newMonth = endDate.getMonth() + 1;
        int newYear = endDate.getYear() + 1900;

        String eformat = "";
        /**
         * if the Month is less than 10, append 0 to it...
         */
        if (newMonth < 10) {
            eformat = newYear + "/" + "0" + newMonth;
        } else {
            eformat = newYear + "/" + newMonth;
        }
        //eformat = "2005/02"; //To test ONLY
        return eformat;
    }

    private int getTransCount(HashMap dataMap) throws Exception {
        List inwardList = sqlMap.executeQueryForList("OperativeAccount.CountInwardClearingWithdrawls", dataMap);
        List cashList = sqlMap.executeQueryForList("OperativeAccount.CountCashWithdrawls", dataMap);
        List transferList = sqlMap.executeQueryForList("OperativeAccount.CountTransferWithdrawls", dataMap);

        HashMap inwardMap = new HashMap();
        HashMap cashMap = new HashMap();
        HashMap transferMap = new HashMap();

        if (inwardList.size() > 0) {
            inwardMap = (HashMap) inwardList.get(0);
        }
        if (cashList.size() > 0) {
            cashMap = (HashMap) cashList.get(0);
        }
        if (transferList.size() > 0) {
            transferMap = (HashMap) transferList.get(0);
        }

        int transSum = CommonUtil.convertObjToInt(inwardMap.get("ROWS"))
                + CommonUtil.convertObjToInt(cashMap.get("ROWS"))
                + CommonUtil.convertObjToInt(transferMap.get("ROWS"));

        inwardMap = null;
        cashMap = null;
        transferMap = null;

        return transSum;
    }

    private HashMap getPopulateTransfer(HashMap inputMap) {
        HashMap resultMap = new HashMap();
        resultMap.put("PRODUCT_ID", inputMap.get(prodId));
        resultMap.put("ACT_NUM", inputMap.get(acctNum));
        resultMap.put("BRANCH_CODE", _branchCode);
        resultMap.put("AC_HD_ID", inputMap.get(acctHead));
        resultMap.put("CR_AC_HD_ID", inputMap.get(xsChgAcctHead));
        resultMap.put("CR_PROD_TYPE", "GL");
        resultMap.put("DR_PROD_TYPE", "OA");
        //System.out.println("=========> HashMap resultMap = " + resultMap);
        return resultMap;

    }

    public TaskStatus executeTask() throws Exception {
        TaskStatus status = new TaskStatus();
        HashMap resultMap;
        ArrayList batchList;


        status.setStatus(BatchConstants.STARTED);
        System.out.println("task Started ....");

        HashMap whereMap = new HashMap();
        if (!CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
            whereMap.put(CommonConstants.BRANCH_ID, branchId);
        } else {
            whereMap.put("NEXT_DATE", currDt);
        }
        final List resultList = sqlMap.executeQueryForList("OperativeAccount.ExcessTransChargesBatch", whereMap);
        /*
         *resultList has a HashMap which contains accNo, ProdId ....
         **/
        runFailureBatch(); //Finish all the prev failure cases first. Runs at each batch operation.

        if (resultList.size() > 0) {
            for (int i = 0; i < resultList.size(); i++) {
                try {
                    boolean blnValidateError = false;
                    Exception validationException = null;
                    sqlMap.startTransaction();
                    batchList = new ArrayList();
                    resultMap = (HashMap) resultList.get(i);
                    super._branchCode = CommonUtil.convertObjToStr(resultMap.get(CommonConstants.BRANCH_ID));
                    super.branchId = CommonUtil.convertObjToStr(resultMap.get(CommonConstants.BRANCH_ID));
                    System.out.println("========> Running charges for ACCT No ======> " + resultMap.get(acctNum));
                    int freqType = CommonUtil.convertObjToInt(resultMap.get("FREE_WITHDRAWAL_TYPE"));
                    /**
                     * To Get the Data Depending on the AccountNo. and Charges Type...
                     */
                    HashMap dataMap = new HashMap();
                    dataMap.put("ACT_NUM", CommonUtil.convertObjToStr(resultMap.get(acctNum)));
                    dataMap.put("CHRG_TYPE", chrgType);
                    dataMap.put("FORMAT", "MM/YYYY");

                    double charges = 0;

                    int freqPeriod = CommonUtil.convertObjToInt(resultMap.get("FREE_WITHDRAWALS_PD"));

                    String sformat = fixSFormat(freqType, resultMap);
                    String eformat = fixEFormat(freqPeriod);

                    dataMap.put("S_DATE", sformat);
                    dataMap.put("E_DATE", eformat);

                    dataMap.put("USER", user);
                    System.out.println("dataMap = " + dataMap);

                    int transSum = getTransCount(dataMap);

                    /* ==========> Code refactoring complete till here <======= */

                    /**
                     * Actual transaction to be charged is the Total No. minus
                     * free Withdrawls (Debit transactions) for the specific
                     * period...
                     */
                    int actualTrans = transSum - CommonUtil.convertObjToInt(resultMap.get("FREE_WITHDRAWALS"));
                    //System.out.println("==========> transSum  = " + transSum);

                    //System.out.println("==========> Actual Trans = " + actualTrans);
                    if (actualTrans > 0) {
                        charges = Double.parseDouble(CommonUtil.convertObjToStr(resultMap.get(chrgAmt))) * actualTrans;
                        //System.out.println("==========> resultMap.get(chrgAmt) = " + resultMap.get(chrgAmt));
                        //System.out.println("==========> charges = " + charges);
                        /**
                         * To Know if the Charges are already applied for the
                         * day
                         */
                        dataMap.put("TODAY_DT", currDt);
                        final List countList = sqlMap.executeQueryForList("OperativeAccount.CountActCharges", dataMap);
                        //System.out.println("==========> countList = " + countList);
                        if (countList.size() > 0) {
                            HashMap countMap = new HashMap();
                            countMap = (HashMap) countList.get(0);
                            int count = CommonUtil.convertObjToInt(countMap.get("COUNT"));
                            countMap = null;
                            /**
                             * If the Count is Zero, apply the Charges... else,
                             * Skip the process
                             */
                            if (count > 0) {
                                System.out.println("The Charges Are Already applied");
                            } else {
                                System.out.println("Apply the Charges");

                                HashMap chargeCalculator = createChargeCalMap(resultMap, actualTrans, charges);

                                ChargesCalculation chrgCalc = new ChargesCalculation();
                                chrgCalc.insertCharge(chargeCalculator);
                                chrgCalc = null;

                                HashMap populateTransfer = getPopulateTransfer(resultMap);

                                //Check here, transaction block
                                try {
                                    updateAccountHeads(populateTransfer, charges);
                                } catch (Exception validationError) {
                                    validationError.printStackTrace();
                                    sqlMap.rollbackTransaction();
                                    blnValidateError = true;
                                    validationException = validationError;

                                }
                                if (blnValidateError) {
                                    sqlMap.startTransaction();
                                    transferLog(validationException, (String) resultMap.get(acctHead), (String) resultMap.get(prodId), 0, (String) resultMap.get(acctNum), 0, charges);
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
        }

        status.setStatus(BatchConstants.COMPLETED);
        System.out.println("Completed successfully");


        return status;
    }

    private HashMap createChargeCalMap(HashMap resultMap, int actualTrans, double charges) {
        double calculatedCharge = 0;
        HashMap chargeCalculator = new HashMap();
        chargeCalculator.put("ACT_NUM", resultMap.get(acctNum));
        chargeCalculator.put("CHRG_TYPE", chrgType);
        chargeCalculator.put("PRODUCT_TYPE", "OA");
        chargeCalculator.put("PRODUCT_ID", resultMap.get(prodId)); //
        chargeCalculator.put("AC_HD_ID", resultMap.get(acctHead));
        chargeCalculator.put("ACT_AMT", null); //Since the charge is not based on slabs.
        chargeCalculator.put("ACT_COUNT", String.valueOf(actualTrans));
        chargeCalculator.put(CommonConstants.CHRG_AMT, String.valueOf(charges));
        chargeCalculator.put("LOG_ID", null);
        return chargeCalculator;
    }

    public static void main(String arg[]) {
        try {
            TaskHeader header = new TaskHeader();
            header.setProductType("OA");
            header.setTransactionType("EXCESSTRANSCHARGE");
            header.setTaskClass("ExcessTransChrgesTask");
            ExcessTransChrgesTask tsk = new ExcessTransChrgesTask(header);
            TaskStatus status = tsk.executeTask();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
