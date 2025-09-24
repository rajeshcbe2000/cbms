/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * MinBalanceChargesTask.java
 *
 * Created on September 8, 2004, 12:53 PM
 */
package com.see.truetransact.serverside.charges;

import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverside.sysadmin.servicetax.ServiceTaxMaintenanceGroupDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;

import com.see.truetransact.transferobject.batchprocess.charges.ChargesBatchTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;

import com.see.truetransact.serverside.transaction.transfer.*;
import com.see.truetransact.commonutil.TTException;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;

import com.ibatis.db.sqlmap.SqlMap;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author rahul
 */
public class MinBalanceChargesTask extends Charge {

    private TransferDAO transferDAO = new TransferDAO();
    private ChargesBatchTO objChargesBatchTO;
    private final String acctHead = "AC_HD_ID";
    private final String prodId = "PROD_ID";
    private final String acctNum = "ACT_NUM";
    private final String currency = "BASE_CURRENCY";
    private final String branch = "BRAN";
    private final String user = "ADMIN";
    private final String chrgType = "NONMNT_MIN_CHRG";
    private final String chrgAmt = "MIN_ACT_BAL";
    private int transCount = 0;
    private String chargesMin = null;

    /**
     * Creates a new instance of MinBalanceChargesTask
     */
    public MinBalanceChargesTask(TaskHeader header) throws Exception {
        super(header);
    }

    private HashMap createChargeCalMap(HashMap resultMap, double charges, int transCount) {
        double calculatedCharge = 0;
        HashMap chargeCalculator = new HashMap();
        chargeCalculator.put("ACT_NUM", resultMap.get(acctNum));
        chargeCalculator.put("CHRG_TYPE", chrgType);
        chargeCalculator.put("PRODUCT_TYPE", productType);
        chargeCalculator.put("PRODUCT_ID", resultMap.get(prodId)); //
        chargeCalculator.put("AC_HD_ID", resultMap.get(acctHead));
        chargeCalculator.put("ACT_AMT", null); //Since the charge is not based on slabs.
        chargeCalculator.put("ACT_COUNT", String.valueOf(transCount)); //Since it is not based on number of transactions
        chargeCalculator.put(CommonConstants.CHRG_AMT, String.valueOf(charges));
        chargeCalculator.put("LOG_ID", null);
        return chargeCalculator;
    }

    private double getCharges(HashMap dataMap, double chrgAmt) throws Exception {
        dataMap.put("TODAY_DT", currDt);
        List inwardList = sqlMap.executeQueryForList("OperativeAccount.CountInwardClearingTrans", dataMap);
        List cashList = sqlMap.executeQueryForList("OperativeAccount.CountCashTrans", dataMap);
        List transferList = sqlMap.executeQueryForList("OperativeAccount.CountTransferTrans", dataMap);

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

        this.transCount = CommonUtil.convertObjToInt(inwardMap.get("ROWS"))
                + CommonUtil.convertObjToInt(cashMap.get("ROWS"))
                + CommonUtil.convertObjToInt(transferMap.get("ROWS"));

        double charges = chrgAmt * transCount;

        //Destroy objects
        inwardMap = null;
        cashMap = null;
        transferMap = null;

        return charges;
    }

    public TaskStatus executeTask() throws Exception {
        TaskStatus status = new TaskStatus();
        HashMap resultMap;
        ArrayList batchList;
        double actualCharges = 0;

        status.setStatus(BatchConstants.STARTED);
        //        HashMap param = new HashMap();
        //        param.put("PROD_ID","CAGen");
        //        param.put("ACT_FROM","OA001017");
        //        param.put("ACT_TO","OA001019");
        //
        //        System.out.println("param: " + param);

        productType = "OA";
        if (branchList != null && branchList.size() > 0) {
            for (int b = 0; b < branchList.size(); b++) {
                HashMap branchMap = (HashMap) branchList.get(b);
                branchId = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                currDt = ServerUtil.getCurrentDate(branchId);
                if (fromChargesFolio != null && fromChargesFolio.length() > 0) {
                    chargesMin = fromChargesFolio;
                } else {
                    chargesMin = "";
                }
                HashMap compMap = new HashMap();
                String compStatus = "";
                List compLst = null;
                if (chargesMin.equals("")) {
                    compMap.put("TASK_NAME", taskLableMin);
                    compMap.put("DAYEND_DT", currDt);
                    compMap.put("BRANCH_ID", branchId);
                    compLst = (List) sqlMap.executeQueryForList("getSelectTaskLst", compMap);
                    compMap = null;
                    if (compLst != null && compLst.size() > 0) {
                        compMap = (HashMap) compLst.get(0);
                        compStatus = CommonUtil.convertObjToStr(compMap.get("TASK_STATUS"));
                        compMap = null;
                    }
                } else {
                    compMap.put("TASK_NAME", "A");
                    compMap.put("DAYEND_DT", currDt);
                    compMap.put("BRANCH_ID", branchId);
                    compLst = (List) sqlMap.executeQueryForList("getSelectTaskLst", compMap);
                    compMap = null;
                    compStatus = "ERROR";
                }
//        if (!CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL"))
                if (compLst.size() <= 0 || compStatus.equals("ERROR")) {
                    HashMap errorMap = new HashMap();
                    if (chargesMin.equals("")) {
                        sqlMap.startTransaction();
                        errorMap.put("ERROR_DATE", currDt);
                        errorMap.put("TASK_NAME", taskLableMin);
                        errorMap.put("BRANCH_ID", branchId);
                        sqlMap.executeUpdate("deleteError_showing", errorMap);
                        sqlMap.commitTransaction();
                    }
                    param.put(CommonConstants.BRANCH_ID, branchId);
                    //        else
                    param.put("NEXT_DATE", currDt);
                    HashMap dataMap = new HashMap();
                    List resultList = sqlMap.executeQueryForList("OperativeAccount.MinBalanceChargesBatch", param);
                    if (resultList.size() > 0) {
                        for (int i = 0, j = resultList.size(); i < j; i++) {
                            try {
                                boolean blnValidateError = false;
                                Exception validationException = null;
                                sqlMap.startTransaction();

                                actualCharges = 0;
                                this.transCount = 0;

                                batchList = new ArrayList();
                                resultMap = (HashMap) resultList.get(i);
//                        super._branchCode = CommonUtil.convertObjToStr(resultMap.get(CommonConstants.BRANCH_ID));
//                        super.branchId = CommonUtil.convertObjToStr(resultMap.get(CommonConstants.BRANCH_ID));
                                System.out.println(" ==============> resultMap " + resultMap);
                                String TRANSTYPE = CommonUtil.convertObjToStr(resultMap.get("AMT_NONMAIN_MINBAL_PD"));

                                actualCharges = CommonUtil.convertObjToDouble(resultMap.get(chrgAmt)).doubleValue();

                                /**
                                 * To Get the Data Depending on the AccountNo.
                                 * and Charges Type...
                                 */
                                dataMap = new HashMap();
                                dataMap.put("ACT_NUM", CommonUtil.convertObjToStr(resultMap.get(acctNum)));
                                dataMap.put("CHRG_TYPE", chrgType);

                                /**
                                 * If the frequency of the Charges to be levied
                                 * is Per-Day or per-Month or Per-Transaction
                                 */
                                System.out.println("TRANSTYPE: " + TRANSTYPE);

                                if (TRANSTYPE.equalsIgnoreCase("PER DAY")) {
                                    dataMap.put("FORMAT", "MM/DD/YYYY");
                                } else if (TRANSTYPE.equalsIgnoreCase("PER MONTH")) {
                                    dataMap.put("FORMAT", "MM/YYYY");
                                } else if (TRANSTYPE.equalsIgnoreCase("PER TRANSACTION")) {
                                    dataMap.put("FORMAT", "MM/DD/YYYY");
                                    actualCharges = getCharges(dataMap, CommonUtil.convertObjToDouble(resultMap.get(chrgAmt)).doubleValue());
                                }



                                /**
                                 * To Know if the Charges are already applied
                                 * for the day
                                 */
                                System.out.println("++++++++++++ dataMap = " + dataMap);
                                dataMap.put("TODAY_DT", currDt);
                                List countList = sqlMap.executeQueryForList("OperativeAccount.CountActCharges", dataMap);
                                if (countList.size() > 0) {
                                    HashMap countMap = new HashMap();
                                    countMap = (HashMap) countList.get(0);
                                    int count = CommonUtil.convertObjToInt(countMap.get("COUNT"));
                                    countMap = null;
                                    /**
                                     * If the Count is Zero, apply the
                                     * Charges... else, Skip the process
                                     */
                                    if (count > 0) {
                                        System.out.println("The Charges Are Already applied");
                                    } else {
                                        if (actualCharges > 0) {
                                            System.out.println("Apply the charges....");

                                            HashMap chargeCalculator = createChargeCalMap(resultMap, actualCharges, this.transCount);
                                            ChargesCalculation chrgCalc = new ChargesCalculation();
                                            chrgCalc.insertCharge(chargeCalculator);
                                            chrgCalc = null;

                                            HashMap populateTransfer = getPopulateTransfer(resultMap);
                                            System.out.println("populateTransfer: " + populateTransfer);
                                            populateTransfer.put("", "");

//                                    try{
                                            updateAccountHeads(populateTransfer, actualCharges);
                                            ServiceTaxMaintenanceGroupDAO serTaxChg = new ServiceTaxMaintenanceGroupDAO();
                                            HashMap stMap = new HashMap();
                                            resultMap.put("CHARGE_TYPE", "NONMAIN_MINBAL");
                                            resultMap.put("CAL_AMT", new Double(actualCharges));
                                            resultMap.put("CALCULATION_TYPE", "AFTER_AUTH");
                                            resultMap.put("ACT_NUM", CommonUtil.convertObjToStr(resultMap.get(acctNum)));
                                            resultMap.put("ST_CAL", "ST_CAL");
                                            resultMap.put(TransferTrans.PARTICULARS, "Non Minimum Balance Maintenance  chg");
                                            resultMap.put(CommonConstants.BRANCH_ID, branchId);
                                            resultMap.put("BRANCH_ID", branchId);
                                            resultMap.put("PROD_TYPE", "OA");
                                            resultMap.put("PROD_ID", prodId);
                                            serTaxChg.execute(resultMap);

//                                    }catch(Exception validationError){
//                                        System.out.println("Error in asasasasasas");
//                                        validationError.printStackTrace();
//                                        sqlMap.rollbackTransaction();
//                                        blnValidateError = true;
//                                        validationException = validationError ;
//
//                                    }
                                            if (blnValidateError) {
                                                sqlMap.startTransaction();
                                                transferLog(validationException, (String) resultMap.get(acctHead), (String) resultMap.get(prodId), 0, (String) resultMap.get(acctNum), 0, actualCharges);
                                            }
                                        }
                                    }
                                }
                                sqlMap.commitTransaction();
                            } catch (Exception e) {
                                if (chargesMin.equals("")) {
                                    System.out.println("Exception part in MinBalanceChargesTask... " + e);
                                    sqlMap.rollbackTransaction();
                                    sqlMap.startTransaction();
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
                                                            strExc = (String) list.get(a);
                                                            System.out.println("#$#$ if TTException part strExc ..." + strExc);
                                                        }
                                                        errorMap = new HashMap();
                                                        errorMap.put("ERROR_DATE", currDt);
                                                        errorMap.put("TASK_NAME", taskLableMin);
                                                        errorMap.put("ERROR_MSG", strExc);
                                                        errorMap.put("ERROR_CLASS", errClassName);
                                                        errorMap.put("ACT_NUM", dataMap.get("ACT_NUM"));
                                                        errorMap.put("BRANCH_ID", branchId);
                                                        sqlMap.executeUpdate("insertError_showing", errorMap);
                                                        errorMap = null;
                                                    }
                                                }
                                            }
                                        }
                                        status.setStatus(BatchConstants.ERROR);
                                    } else {
                                        System.out.println("#$#$ if not TTException part..." + e);
                                        errMsg = e.getMessage();
                                        errorMap = new HashMap();
                                        errorMap.put("ERROR_DATE", currDt);
                                        errorMap.put("TASK_NAME", taskLableMin);
                                        errorMap.put("ERROR_MSG", errMsg);
                                        errorMap.put("ERROR_CLASS", errClassName);
                                        errorMap.put("ACT_NUM", dataMap.get("ACT_NUM"));
                                        errorMap.put("BRANCH_ID", branchId);
                                        sqlMap.executeUpdate("insertError_showing", errorMap);
                                        errorMap = null;
                                        status.setStatus(BatchConstants.ERROR);
                                    }

                                    tte = null;
                                    exceptionMap = null;
                                    excMap = null;
                                    e.printStackTrace();
                                    sqlMap.commitTransaction();
                                } else {
                                    sqlMap.rollbackTransaction();
                                    e.printStackTrace();
                                }
                            }
                        }
                        if (chargesMin.equals("")) {
                            if (status.getStatus() != BatchConstants.ERROR) {
                                sqlMap.startTransaction();
                                if (compStatus.equals("ERROR")) {
                                    HashMap statusMap = new HashMap();
                                    //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                                    statusMap.put("BRANCH_CODE", branchId);
                                    statusMap.put("TASK_NAME", taskLableMin);
                                    statusMap.put("TASK_STATUS", "COMPLETED");
                                    statusMap.put("USER_ID", userID);
                                    statusMap.put("DAYEND_DT", currDt);
                                    sqlMap.executeUpdate("updateTskStatus", statusMap);
                                    statusMap = null;
                                } else {
                                    HashMap statusMap = new HashMap();
                                    //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                                    statusMap.put("BRANCH_CODE", branchId);
                                    statusMap.put("TASK_NAME", taskLableMin);
                                    statusMap.put("TASK_STATUS", "COMPLETED");
                                    statusMap.put("USER_ID", userID);
                                    statusMap.put("DAYEND_DT", currDt);
                                    sqlMap.executeUpdate("InsertDayEndStatus", statusMap);
                                    statusMap = null;
                                }
                                sqlMap.commitTransaction();
                            } else {
                                sqlMap.startTransaction();
                                if (compStatus.equals("ERROR")) {
                                    HashMap statusMap = new HashMap();
                                    //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                                    statusMap.put("BRANCH_CODE", branchId);
                                    statusMap.put("TASK_NAME", taskLableMin);
                                    statusMap.put("TASK_STATUS", "ERROR");
                                    statusMap.put("USER_ID", userID);
                                    statusMap.put("DAYEND_DT", currDt);
                                    sqlMap.executeUpdate("updateTskStatus", statusMap);
                                    statusMap = null;
                                } else {
//                            isError = true;
                                    HashMap statusMap = new HashMap();
                                    //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                                    statusMap.put("BRANCH_CODE", branchId);
                                    statusMap.put("TASK_NAME", taskLableMin);
                                    statusMap.put("TASK_STATUS", "ERROR");
                                    statusMap.put("USER_ID", userID);
                                    statusMap.put("DAYEND_DT", currDt);
                                    sqlMap.executeUpdate("InsertDayEndStatus", statusMap);
                                    statusMap = null;

                                }
                                sqlMap.commitTransaction();
                            }
                        }
//                status.setStatus(BatchConstants.COMPLETED);
                    } else {
                        if (chargesMin.equals("")) {
                            if (status.getStatus() != BatchConstants.ERROR) {
                                sqlMap.startTransaction();
                                if (!compStatus.equals("")) {
                                    HashMap statusMap = new HashMap();
                                    //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                                    statusMap.put("BRANCH_CODE", branchId);
                                    statusMap.put("TASK_NAME", taskLableMin);
                                    statusMap.put("TASK_STATUS", "COMPLETED");
                                    statusMap.put("USER_ID", userID);
                                    statusMap.put("DAYEND_DT", currDt);
                                    sqlMap.executeUpdate("updateTskStatus", statusMap);
                                    statusMap = null;
                                } else {
                                    HashMap statusMap = new HashMap();
                                    //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                                    statusMap.put("BRANCH_CODE", branchId);
                                    statusMap.put("TASK_NAME", taskLableMin);
                                    statusMap.put("TASK_STATUS", "COMPLETED");
                                    statusMap.put("USER_ID", userID);
                                    statusMap.put("DAYEND_DT", currDt);
                                    sqlMap.executeUpdate("InsertDayEndStatus", statusMap);
                                    statusMap = null;
                                }
                                sqlMap.commitTransaction();
                            }
                        }
//                status.setStatus(BatchConstants.NO_DATA);
                        status.setStatus(BatchConstants.COMPLETED);
                    }
                }
            }
        }
        if (status.getStatus() != BatchConstants.ERROR) {
            status.setStatus(BatchConstants.COMPLETED);
        }
        System.out.println("!!!!status" + status);
        return status;
    }

    private HashMap getPopulateTransfer(HashMap inputMap) {
        HashMap resultMap = new HashMap();
        resultMap.put("PRODUCT_ID", inputMap.get(prodId));
        resultMap.put("ACT_NUM", inputMap.get(acctNum));
        resultMap.put("BRANCH_CODE", branchId);
        resultMap.put("AC_HD_ID", inputMap.get(acctHead));
        resultMap.put("CR_AC_HD_ID", inputMap.get(chrgType));
        resultMap.put("CR_PROD_TYPE", "GL");
        resultMap.put("DR_PROD_TYPE", "OA");
        resultMap.put("PARTICULARS", "Min Bal Charges for A/c : " + inputMap.get(acctNum));
        //System.out.println("=========> HashMap resultMap = " + resultMap);
        return resultMap;

    }

    public static void main(String arg[]) {
        try {
            TaskHeader header = new TaskHeader();
            header.setTaskClass("MinBalanceChargesTask");
            header.setTransactionType("MIN_BALANCE");
            header.setProductType("OA");

            HashMap param = new HashMap();
            param.put("PROD_ID", "CAGen");
            param.put("ACT_FROM", "OA001036");
            param.put("ACT_TO", "OA001100");

            header.setTaskParam(param);

            MinBalanceChargesTask tsk = new MinBalanceChargesTask(header);
            TaskStatus status = tsk.executeTask();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
