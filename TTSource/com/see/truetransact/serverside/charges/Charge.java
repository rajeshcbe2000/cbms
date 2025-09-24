/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * Charges.java
 *
 * Created on January 19, 2005, 2:44 PM
 */
package com.see.truetransact.serverside.charges;

import com.ibatis.db.sqlmap.SqlMap;
import org.apache.log4j.Logger;

import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.LocaleConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.common.transaction.TransferTransLog;
import com.see.truetransact.transferobject.common.transaction.FailureTxTransferTO;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;

/**
 *
 * @author Balachandar
 */
public abstract class Charge extends Task {

    protected SqlMap sqlMap = null;
    protected final Logger log = Logger.getLogger(Charge.class);
    private ChargesCalculation chargeCalc = new ChargesCalculation();
    protected String chargeType;
    protected String productType;
    protected String branchId;
    protected String userID;
    protected HashMap param;
    protected ArrayList batchList = null;
    protected Date currDt = null;
    protected String dayEndType;
    protected List branchList;
    protected String taskLableFolio;
    protected String taskLableMin;
    protected String fromChargesFolio;

    /**
     * Creates a new instance of Transaction
     */
    public Charge(TaskHeader taskHeader) throws ServiceLocatorException {
        this.chargeType = taskHeader.getTaskClass();
        this.productType = taskHeader.getProductType();
        this.param = taskHeader.getTaskParam();
        this.branchId = taskHeader.getBranchID();
        this.userID = taskHeader.getUserID();
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        _branchCode = taskHeader.getBranchID();

        currDt = ServerUtil.getCurrentDate(branchId);
        if (param != null && param.containsKey("DAY_END_TYPE")) {
            dayEndType = CommonUtil.convertObjToStr(param.get("DAY_END_TYPE"));
        }
        if (CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
            HashMap tempMap = new HashMap();
            if (param.containsKey("BRANCH_LST")) {
                branchList = (List) param.get("BRANCH_LST");
                System.out.println("branchList*****" + branchList);
            } else {
                tempMap.put("NEXT_DATE", currDt);
                try {
                    branchList = (List) sqlMap.executeQueryForList("getAllBranchesFromDayEnd", tempMap);
                } catch (Exception e) {
                    System.out.println("#$#$# Error in getAllBranchesFromDayEnd map in Charge Task...");
                }
            }
            tempMap = null;
        } else {
            HashMap tempMap = new HashMap();
            tempMap.put(CommonConstants.BRANCH_ID, _branchCode);
            branchList = new ArrayList();
            branchList.add(tempMap);
            tempMap = null;
        }
        if (param != null && param.containsKey("FOLIO_TASK_LABLE")) {
            taskLableFolio = CommonUtil.convertObjToStr(param.get("FOLIO_TASK_LABLE"));
        }
        if (param != null && param.containsKey("MIN_BAL_TASK_LABLE")) {
            taskLableMin = CommonUtil.convertObjToStr(param.get("MIN_BAL_TASK_LABLE"));
        }
        if (param != null && param.containsKey("CHARGES_PROCESS")) {
            fromChargesFolio = CommonUtil.convertObjToStr(param.get("CHARGES_PROCESS"));
        }

    }

    public void print(String str) {
        System.out.println(str);
        str = null;
    }

    public void runFailureBatch() throws Exception {
        ArrayList batchList = null;
        TransferTrans trans = new TransferTrans();
        //Get distinct log id here.
        //with logid get that batch, return the failure TO
        //add TO to batch
        //get branchId from the TO and pass it.
        //call doDebitCredit with that batch's TO's
        //Update the batch's log id to null
        List failureBatch = sqlMap.executeQueryForList("getChargesFailureLogIdList", chargeType);
        int failureBatchSize = failureBatch.size();
        for (int count = 0; count < failureBatchSize; count++) {
            batchList = new ArrayList();
            List failureList = sqlMap.executeQueryForList("getChargesFailureList", (String) failureBatch.get(count));
            for (int i = 0, failSize = failureList.size(); i < failSize; i++) {
                batchList.add((FailureTxTransferTO) failureList.get(i));
                branchId = ((FailureTxTransferTO) failureList.get(i)).getBranchId();
                trans.doDebitCredit(batchList, branchId);
            }
            sqlMap.executeUpdate("updateActChargesLogID", CommonUtil.convertObjToStr(failureBatch.get(count)));
        }
        trans = null;
    }

    public void transferLog(Exception transErr, String chargeAcHd, String productId, double availableBalance, String actId, double folioCount, double chargeAmt) throws Exception {
        if (getBatchList() != null) {

            HashMap errorMap = new HashMap();
            errorMap.put(TransferTransLog.BATCH_TYPE, chargeType);
            //Retrieve the error msg from the Exception and log it
            errorMap.put(TransferTransLog.FAILURE_REMARK, transErr.toString());
            TransferTransLog trLog = new TransferTransLog(sqlMap);
            Date dt = new Date();
            String strDt = String.valueOf(dt.getTime());

            if (chargeAmt > 0) {
                insertCharge(chargeAcHd, productId, availableBalance, actId, folioCount, strDt, chargeAmt);
            } else {
                // calculating & inserting into ACT_Charges
                calculateAndInsertCharge(chargeAcHd, productId, availableBalance, actId, folioCount, strDt, null);
            }

            trLog.writeErrorBatch(getBatchList(), errorMap, strDt);
            trLog = null;
        }
    }

    public void updateAccountHeads(HashMap actMaster, double charges) throws Exception {
        HashMap map = new HashMap();
        map.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(actMaster.get("PRODUCT_ID")));
        map.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(actMaster.get("ACT_NUM")));
        map.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(actMaster.get("BRANCH_CODE")));
        map.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(actMaster.get("BRANCH_CODE")));
        map.put(TransferTrans.CURRENCY, LocaleConstants.DEFAULT_CURRENCY);
        map.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(actMaster.get("DR_AC_HD_ID")));
        map.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(actMaster.get("CR_AC_HD_ID")));
        map.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
//        map.put(TransferTrans.DR_PROD_TYPE, super.getHeader().getProductType());
        map.put(TransferTrans.DR_PROD_TYPE, productType);
        map.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(TransferTrans.PARTICULARS));

        TransferTrans trans = new TransferTrans();
        trans.setInitiatedBranch(CommonUtil.convertObjToStr(actMaster.get("BRANCH_CODE")));
        trans.setLinkBatchId(CommonUtil.convertObjToStr(actMaster.get("ACT_NUM")));
        batchList = new ArrayList();
        batchList.add(trans.getDebitTransferTO(map, charges));
        batchList.add(trans.getCreditTransferTO(map, charges));
        setBatchList(batchList); //returns the list so that it may be logged if failure

        trans.doDebitCredit(batchList, CommonUtil.convertObjToStr(actMaster.get("BRANCH_CODE")));
        batchList = null;
    }

    private HashMap getChargeMap(String chargeAcHd, String productId, double chargeOn, String actId, double actCount, String logId, HashMap foliochargeAmt) throws Exception {
        HashMap chargeCalculator = new HashMap();
        chargeCalculator.put("ACT_NUM", actId);
        String chargeType1 = chargeType;
//        chargeType=CommonUtil.convertObjToStr(foliochargeAmt.get("CHARGE_TYPE"));
        chargeCalculator.put("CHRG_TYPE", chargeType);
//        chargeType=chargeType1;
        chargeCalculator.put("PRODUCT_TYPE", productType);
        chargeCalculator.put("PRODUCT_ID", productId);
        chargeCalculator.put("AC_HD_ID", chargeAcHd);
        chargeCalculator.put("ACT_AMT", String.valueOf(chargeOn)); // available balance or charge based on
        chargeCalculator.put("ACT_COUNT", String.valueOf(actCount));
        chargeCalculator.put("LOG_ID", logId);
        chargeCalculator.put("TODAY_DT", currDt);
        chargeCalculator.put(CommonConstants.BRANCH_ID, this.branchId);
        chargeCalculator.putAll(foliochargeAmt);
        return chargeCalculator;
    }

    public void insertCharge(String chargeAcHd, String productId, double chargeOn, String actId, double actCount, String logId, double charge) throws Exception {
        HashMap chargeCalculator = getChargeMap(chargeAcHd, productId, chargeOn, actId, actCount, logId, null);
        chargeCalculator.put(CommonConstants.CHRG_AMT, String.valueOf(charge));
        try {
            chargeCalc.insertCharge(chargeCalculator);
        } catch (TTException tt) {
            System.out.println(tt + " for A/c no " + actId);
        }
    }

    public double calculateAndInsertCharge(String chargeAcHd, String productId, double chargeOn, String actId, double actCount, String logId, HashMap foliochargeAmt) throws Exception {
        double calculatedCharge = 0;
        HashMap chargeCalculator = getChargeMap(chargeAcHd, productId, chargeOn, actId, actCount, logId, foliochargeAmt);
        System.out.println("chargecalculator###" + chargeCalculator);
        try {
            calculatedCharge = chargeCalc.calculateAndInsertCharge(chargeCalculator);
        } catch (TTException tt) {
            System.out.println(tt + " for A/c no " + actId);
        }
        return (calculatedCharge);
    }

    public static void main(String args[]) {
        try {
//            Charge ft = new Charge(null);
//            ft.executeTask();
        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    /**
     * Getter for property batchList.
     *
     * @return Value of property batchList.
     */
    public ArrayList getBatchList() {
        return batchList;
    }

    /**
     * Setter for property batchList.
     *
     * @param batchList New value of property batchList.
     */
    public void setBatchList(ArrayList batchList) {
        this.batchList = batchList;
    }
}
