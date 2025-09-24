/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * InOperativeChargesTask.java
 *
 * Created on August 18, 2004, 10:26 AM
 */
package com.see.truetransact.serverside.charges;

import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.ibatis.db.sqlmap.SqlMap;

import com.see.truetransact.transferobject.batchprocess.charges.ChargesBatchTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.transaction.transfer.*;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

/**
 *
 * @author rahul
 */
public class InOperativeChargesTask extends Charge {

    private TransferDAO transferDAO = new TransferDAO();
    private ChargesBatchTO objChargesBatchTO;
    private final String acctHead = "AC_HD_ID";
    private final String prodId = "PROD_ID";
    private final String acctNum = "ACT_NUM";
    private final String inoperCharge = "INOP_AC_CHRG";
    private final String amount = "INOPERATIVE_AC_CHARGES";
    private final String currency = "BASE_CURRENCY";
    private final String inOperCharges = "InOperative Account Charges";
    private final String branch = "BRAN";
    private final String user = "ADMIN";
    private final String chrgType = "IN_OPERATIVE";

    /**
     * Creates a new instance of InOperativeChargesTask
     */
    public InOperativeChargesTask(TaskHeader header) throws Exception {
        super(header);
    }

    private HashMap createChargeCalMap(HashMap resultMap) {
        double calculatedCharge = 0;
        HashMap chargeCalculator = new HashMap();
        chargeCalculator.put("ACT_NUM", resultMap.get(acctNum));
        chargeCalculator.put("CHRG_TYPE", chrgType);
        chargeCalculator.put("PRODUCT_TYPE", "OA");
        chargeCalculator.put("PRODUCT_ID", resultMap.get(prodId)); //TODO VERIFY
        chargeCalculator.put("AC_HD_ID", CommonUtil.convertObjToStr(resultMap.get(inoperCharge)));
        chargeCalculator.put("ACT_AMT", null); //Since the charge is not based on slabs.
        chargeCalculator.put("ACT_COUNT", null); //Since this is not based on transacation count
        chargeCalculator.put(CommonConstants.CHRG_AMT, CommonUtil.convertObjToStr(resultMap.get(amount)));
        chargeCalculator.put("LOG_ID", null);
        return chargeCalculator;
    }

    private HashMap getPopulateTransfer(HashMap inputMap) {
        HashMap resultMap = new HashMap();
        resultMap.put("PRODUCT_ID", inputMap.get(prodId));
        resultMap.put("ACT_NUM", inputMap.get(acctNum));
        resultMap.put("BRANCH_CODE", branch);
        resultMap.put("AC_HD_ID", inputMap.get(acctHead));
        resultMap.put("CR_AC_HD_ID", inputMap.get(inoperCharge));
        resultMap.put("CR_PROD_TYPE", "GL");
        resultMap.put("DR_PROD_TYPE", "OA");
        System.out.println("=========> HashMap resultMap = " + resultMap);
        return resultMap;

    }

    public TaskStatus executeTask() throws Exception {
        TaskStatus status = new TaskStatus();
        HashMap resultMap;
        ArrayList batchList;

        param = new HashMap();
        param.put("TODAY_DT", ServerUtil.getCurrentDate(super._branchCode));
//        param.put("PROD_ID","CAGen");
//        param.put("ACT_FROM","OA001001");
//        param.put("ACT_TO","OA001005");
//        param.put("DATE_FROM",ServerUtil.getCurrentDate(_branchCode));
//        param.put("DATE_TO","");

        status.setStatus(BatchConstants.STARTED);

        List resultList = sqlMap.executeQueryForList("OperativeAccount.InOperativeChargesBatchDynamic", param);
        System.out.println("resultList.size() : " + resultList.size());
        if (resultList.size() > 0) {
            for (int i = 0; i < resultList.size(); i++) {
                try {
                    boolean blnValidateError = false;
                    Exception validationException = null;
                    sqlMap.startTransaction();
                    resultMap = (HashMap) resultList.get(i);

                    HashMap populateTransfer = getPopulateTransfer(resultMap);

                    double charges = CommonUtil.convertObjToDouble(resultMap.get(amount)).doubleValue();

                    HashMap chargeCalculator = createChargeCalMap(resultMap);

                    ChargesCalculation chrgCalc = new ChargesCalculation();
                    chrgCalc.insertCharge(chargeCalculator);
                    chrgCalc = null;
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
                        System.out.println("==========> resultMap ===> " + resultMap);
                        transferLog(validationException, (String) resultMap.get(acctHead), (String) resultMap.get(prodId), 0, (String) resultMap.get(acctNum), 0, charges);
                    }
                    sqlMap.commitTransaction();
                } catch (Exception e) {
                    sqlMap.rollbackTransaction();
                    e.printStackTrace();
                    throw new TransRollbackException(e);
                }

            }
        }
        System.out.println("Done ... ");
        status.setStatus(BatchConstants.COMPLETED);
        return status;
    }

    public static void main(String arg[]) {
        try {
            TaskHeader header = new TaskHeader();
            header.setTaskClass("InOperativeChargesTask");
            header.setTransactionType("IN_OPERATIVE");
            header.setProductType("OA");
            InOperativeChargesTask tsk = new InOperativeChargesTask(header);
            TaskStatus status = tsk.executeTask();
            System.out.println("InOperativeChargesTask Status: " + status.getStatus());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
