/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * FolioCharge.java
 *
 * Created on July 9, 2004, 4:40 PM
 */
package com.see.truetransact.serverside.charges;

import com.see.truetransact.commonutil.*;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;
import com.see.truetransact.serverutil.ServerUtil;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import com.see.truetransact.serverside.sysadmin.servicetax.ServiceTaxMaintenanceGroupDAO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import java.util.*;

/**
 *
 * @author Sunil (152691)
 *
 */
public class FolioChargesTask extends Charge {

    private String productType;
    private String folioChargeApplicable = null; // Y/N
    private String toChargeOn = null; // Both/Manual/System
    private String toChargeOnType = null; // Both/Credit/Debit
    private String toCollectFolioChg = null;
    private double noOfEntriesPerFolio = 0; //numeric
    private String folioAcHead = null;
    private String actAcHead = null;
    private double ratePerFolio = 0;
    private String uptoBalance = null;
    private String paramQuery;
    private String accountQuery;
    private InterestCalculationTask loanIntCalTask = null;
    private ArrayList batchList = null;
    private boolean isError = false;
    private String chargesFolio = null;
    private String folioChargeType="";
    private String folioRestrictionFrq="";
    private int foliorestrictionPeriod=0;
    private int yearTobeAdded = 1900;
//    private String taskLable;

    /**
     * Creates a new instance of FolioChargesTask
     */
    public FolioChargesTask(TaskHeader taskHeader) throws Exception {
        super(taskHeader);
        productType = taskHeader.getProductType();
        loanIntCalTask = new InterestCalculationTask(taskHeader);
        System.out.println(" productType : " + productType);
    }

    public TaskStatus executeTask() throws Exception {
        TaskStatus status = new TaskStatus();

        status.setStatus(BatchConstants.STARTED);
        System.out.println("Task Started for .... " + productType);
        List paramsList = null;
        //from day end
        if (!(productType != null)) {
            ArrayList lst = (ArrayList) sqlMap.executeQueryForList("getoperativeProduct", "");
            lst.addAll(sqlMap.executeQueryForList("Transfer.getCreditProductAD", ""));
            System.out.println("arrayList#####" + lst);
            for (int i = 0; i < lst.size(); i++) {
                HashMap dayEndMap = (HashMap) lst.get(i);
                if (dayEndMap.containsKey("BEHAVES_LIKE") && CommonUtil.convertObjToStr(dayEndMap.get("BEHAVES_LIKE")).equals("OD")) {
                    productType = "AD";
                    dayEndMap.put("PROD_ID", dayEndMap.get("PRODID"));
                } else {
                    productType = "OA";
                }
                dayEndMap.put("PROD_TYPE", productType);
                dayEndMap.put("CHARGE_TYPE", "FolioChargesTask");
                if (branchList != null && branchList.size() > 0) {
                    for (int b = 0; b < branchList.size(); b++) {
                        HashMap branchMap = (HashMap) branchList.get(b);
                        branchId = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                        currDt = ServerUtil.getCurrentDate(branchId);
                        HashMap compMap = new HashMap();
                        String compStatus = "";
                        List compLst = null;
                        if (fromChargesFolio != null && fromChargesFolio.length() > 0) {
                            chargesFolio = fromChargesFolio;
                        } else {
                            chargesFolio = "";
                        }
                        if (chargesFolio.equals("")) {
                            compMap.put("TASK_NAME", taskLableFolio);
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
                            compMap.put("TASK_NAME", taskLableFolio);
                            compMap.put("DAYEND_DT", currDt);
                            compMap.put("BRANCH_ID", branchId);
                            compLst = (List) sqlMap.executeQueryForList("getSelectTaskLst", compMap);
                            compMap = null;
                            compStatus = "ERROR";
                        }
                        if (compLst.size() <= 0 || compStatus.equals("ERROR")) {
                            try {
                                HashMap errorMap = new HashMap();
                                if (chargesFolio.equals("")) {
                                    sqlMap.startTransaction();
                                    errorMap.put("ERROR_DATE", currDt);
                                    errorMap.put("TASK_NAME", taskLableFolio);
                                    errorMap.put("BRANCH_ID", branchId);
                                    sqlMap.executeUpdate("deleteError_showing", errorMap);
                                    sqlMap.commitTransaction();
                                }
                                dayEndMap.put("BRANCH_ID", branchId);
                                System.out.println("dayEndmap####" + dayEndMap);
                                List dateList = sqlMap.executeQueryForList("getSelectCommonFolioCharges", dayEndMap);
                                System.out.println("dateList$######" + dateList);
                                if (dateList != null && dateList.size() > 0) {
                                    HashMap folioMap = (HashMap) dateList.get(0);
                                    folioMap.put("BEHAVES_LIKE", dayEndMap.get("BEHAVES_LIKE"));
                                    Date fromdt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(folioMap.get("LAST_CHARG_CALC_DT")));
                                    Date toDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(folioMap.get("NEXT_CHARG_CALC_DT")));
                                    fromdt = DateUtil.addDays(fromdt, 1);
                                    //                toDt=DateUtil.addDays(toDt, 0);
                                    //                System.out.println("fromdt--------->"+fromdt+"             "+"toDt------------------>"+toDt);

                                    //for wont run one more time perday
                                    if (DateUtil.dateDiff(fromdt, currDt) > 0) {
                                        if (productType.equals("OA")) {
                                            this.paramQuery = "Charges.getFolioParamsOA";
                                            this.accountQuery = "Charges.getAllAccountDataOA";
                                        } else {
                                            this.paramQuery = "Charges.getFolioParamsTL_AD";
                                            this.accountQuery = "Charges.getAllAccountDataTL_AD";
                                        }

                                        //                super.param.put("BEHAVES_LIKE", productType);

                                        paramsList = (List) sqlMap.executeQueryForList(paramQuery, folioMap);
                                        System.out.println("paramlist###" + paramsList);
                                        int paramsListSize = paramsList.size();
                                        for (int outer = 0; outer < paramsListSize; outer++) {
                                            HashMap paramMap = (HashMap) paramsList.get(outer);
                                            getParams(paramMap);
                                            if (folioChargeApplicable.toUpperCase().equals("Y")) {
                                                //__ To Pass the data to the Method...
                                                HashMap dataMap = new HashMap();
                                                dataMap.put("PROD_ID", paramMap.get("PROD_ID"));
                                                //                        if(super.param.containsKey("ACT_FROM") && super.param.get("ACT_FROM")!=null)
                                                //                            dataMap.put("ACT_FROM",param.get("ACT_FROM"));
                                                //                        if(super.param.containsKey("ACT_TO") && super.param.get("ACT_TO")!=null)
                                                //                            dataMap.put("ACT_TO",super.param.get("ACT_TO"));

                                                if (folioMap.containsKey("LAST_CHARG_CALC_DT")) {
                                                    Date LAST_CHARG_CALC_DT = (Date) folioMap.get("LAST_CHARG_CALC_DT");
                                                    LAST_CHARG_CALC_DT = DateUtil.addDays(LAST_CHARG_CALC_DT, 1);
                                                    dataMap.put("DATE_FROM", LAST_CHARG_CALC_DT);
                                                    dataMap.put("LAST_CHARG_CALC_DT", LAST_CHARG_CALC_DT);

                                                }

                                                if (folioMap.containsKey("NEXT_CHARG_CALC_DT")) {
                                                    Date NEXT_CHARG_CALC_DT = (Date) folioMap.get("NEXT_CHARG_CALC_DT");
                                                    NEXT_CHARG_CALC_DT = DateUtil.addDays(NEXT_CHARG_CALC_DT, 1);
                                                    dataMap.put("DATE_TO", NEXT_CHARG_CALC_DT);
                                                    dataMap.put("NEXT_CHARG_CALC_DT", NEXT_CHARG_CALC_DT);
                                                }
                                                dataMap.put("CURR_DT", dataMap.get("DATE_TO"));
                                                dataMap.put("CURR_DATE", currDt);
                                                //                runBatch((String)paramMap.get("PROD_ID"));
                                                //                        dataMap.put("DATE_TO",toDt);
                                                dataMap.put("BRANCH_CODE", branchId);
                                                System.out.println("dataMap####" + dataMap);
                                                Date validationDt = loanIntCalTask.holiydaychecking(dataMap);
                                                System.out.println("validationdate###0" + validationDt);
                                                if (DateUtil.dateDiff(validationDt, currDt) >= 0) {
                                                    dataMap.put("FOLIO_CHRG_APPLFREQ", paramMap.get("FOLIO_CHRG_APPLFREQ"));
                                                    dataMap.put("BRANCH_ID", branchId);
                                                    System.out.println("datamap######" + dataMap);
                                                    runBatch(dataMap);
                                                }
                                            }
                                        }
                                    }
                                }
                                if (chargesFolio.equals("")) {
                                    if (status.getStatus() != BatchConstants.ERROR) {
                                        sqlMap.startTransaction();
                                        if (compStatus.equals("ERROR")) {
                                            HashMap statusMap = new HashMap();
                                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                                            statusMap.put("BRANCH_CODE", branchId);
                                            statusMap.put("TASK_NAME", taskLableFolio);
                                            statusMap.put("TASK_STATUS", "COMPLETED");
                                            statusMap.put("USER_ID", userID);
                                            statusMap.put("DAYEND_DT", currDt);
                                            sqlMap.executeUpdate("updateTskStatus", statusMap);
                                            statusMap = null;
                                        } else {
                                            HashMap statusMap = new HashMap();
                                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                                            statusMap.put("BRANCH_CODE", branchId);
                                            statusMap.put("TASK_NAME", taskLableFolio);
                                            statusMap.put("TASK_STATUS", "COMPLETED");
                                            statusMap.put("USER_ID", userID);
                                            statusMap.put("DAYEND_DT", currDt);
                                            sqlMap.executeUpdate("InsertDayEndStatus", statusMap);
                                            statusMap = null;
                                        }
                                        sqlMap.commitTransaction();
                                    }
                                }
                            } catch (Exception e) {
                                if (chargesFolio.equals("")) {
                                    sqlMap.rollbackTransaction();
//                        status.setStatus(BatchConstants.ERROR) ;
                                    sqlMap.startTransaction();
                                    if (compStatus.equals("ERROR")) {
                                        HashMap statusMap = new HashMap();
                                        //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                                        statusMap.put("BRANCH_CODE", branchId);
                                        statusMap.put("TASK_NAME", taskLableFolio);
                                        statusMap.put("TASK_STATUS", "ERROR");
                                        statusMap.put("USER_ID", userID);
                                        statusMap.put("DAYEND_DT", currDt);
                                        sqlMap.executeUpdate("updateTskStatus", statusMap);
                                        statusMap = null;
                                    } else {
                                        isError = true;
                                        HashMap statusMap = new HashMap();
                                        //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                                        statusMap.put("BRANCH_CODE", branchId);
                                        statusMap.put("TASK_NAME", taskLableFolio);
                                        statusMap.put("TASK_STATUS", "ERROR");
                                        statusMap.put("USER_ID", userID);
                                        statusMap.put("DAYEND_DT", currDt);
                                        sqlMap.executeUpdate("InsertDayEndStatus", statusMap);
                                        statusMap = null;

                                    }
                                    sqlMap.commitTransaction();
//                        HashMap errorMap = new HashMap();
//                        errorMap.put("ERROR_DATE",currDt);
//                        errorMap.put("TASK_NAME", taskLableFolio);
//                        errorMap.put("ERROR_MSG",e.getMessage());
//                        System.out.println("Error ERROR_MSG " + e.getMessage());
//                        errorMap.put("ACT_NUM","asd");
//                        errorMap.put("BRANCH_ID", branchId);
//                        System.out.println("errorMap" + errorMap);
//                        //                        sqlMap.startTransaction();
//                        sqlMap.executeUpdate("insertError_showing", errorMap);
//                        //                        sqlMap.commitTransaction();
//                        //                            System.out.println("Error thrown for Depsoit No " + dataMap.get("DEPOSITNO"));
//                        //                             sqlMap.rollbackTransaction();
//                        sqlMap.commitTransaction();
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
                                                        HashMap errorMap = new HashMap();
                                                        errorMap.put("ERROR_DATE", currDt);
                                                        errorMap.put("TASK_NAME", taskLableFolio);
                                                        errorMap.put("ERROR_MSG", strExc);
                                                        errorMap.put("ERROR_CLASS", errClassName);
                                                        errorMap.put("ACT_NUM", "ACT_NUM");
                                                        errorMap.put("BRANCH_ID", branchId);
                                                        sqlMap.startTransaction();
                                                        sqlMap.executeUpdate("insertError_showing", errorMap);
                                                        sqlMap.commitTransaction();
                                                        errorMap = null;
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        System.out.println("#$#$ if not TTException part..." + e);
                                        errMsg = e.getMessage();
                                        HashMap errorMap = new HashMap();
                                        errorMap.put("ERROR_DATE", currDt);
                                        errorMap.put("TASK_NAME", taskLableFolio);
                                        errorMap.put("ERROR_MSG", errMsg);
                                        errorMap.put("ERROR_CLASS", errClassName);
                                        errorMap.put("ACT_NUM", "ACT_NUM");
                                        errorMap.put("BRANCH_ID", branchId);
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
                                    //                            continue;
                                } else {
                                    sqlMap.rollbackTransaction();
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
            lst = new ArrayList();
        } /* ===================================== FROM  CHARGESUI ===================================== */ else {
            //   from  chargesui
            if (productType.equals("OA")) {
                this.paramQuery = "Charges.getFolioParamsOA";
                this.accountQuery = "Charges.getAllAccountDataOA";
            } else {
                this.paramQuery = "Charges.getFolioParamsTL_AD";
                this.accountQuery = "Charges.getAllAccountDataTL_AD";
            }

            super.param.put("BEHAVES_LIKE", productType);


            paramsList = (List) sqlMap.executeQueryForList(paramQuery, super.param);
            System.out.println("paramlist###" + paramsList);
            int paramsListSize = paramsList.size();
            for (int outer = 0; outer < paramsListSize; outer++) {
                HashMap paramMap = (HashMap) paramsList.get(outer);
                getParams(paramMap);
                if (folioChargeApplicable.toUpperCase().equals("Y")) {
                    //__ To Pass the data to the Method...
                    HashMap dataMap = new HashMap();
                    dataMap.put("PROD_ID", paramMap.get("PROD_ID"));
                    if (super.param.containsKey("ACT_FROM") && super.param.get("ACT_FROM") != null) {
                        dataMap.put("ACT_FROM", param.get("ACT_FROM"));
                    }
                    if (super.param.containsKey("ACT_TO") && super.param.get("ACT_TO") != null) {
                        dataMap.put("ACT_TO", super.param.get("ACT_TO"));
                    }

                    if (super.param.containsKey("DATE_FROM")) {
                        dataMap.put("DATE_FROM", (Date) super.param.get("DATE_FROM"));
                    }

                    if (super.param.containsKey("DATE_TO")) {
                        dataMap.put("DATE_TO", (Date) super.param.get("DATE_TO"));
                    }

                    HashMap dayEndMap = new HashMap();
                    dayEndMap.put("PROD_TYPE", productType);
                    dayEndMap.put("CHARGE_TYPE", "FolioChargesTask");
                    dayEndMap.put("BRANCH_ID", branchId);
                    dayEndMap.put("PROD_ID", CommonUtil.convertObjToStr(paramMap.get("PROD_ID")));
                    List dateList = sqlMap.executeQueryForList("getSelectCommonFolioCharges", dayEndMap);
                    dayEndMap = new HashMap();
                    if (dateList != null && dateList.size() > 0) {
                        dayEndMap = (HashMap) dateList.get(0);
                        dateList = null;
                        System.out.println("TO_DATE-------------->before " + dataMap.get("TO_DATE"));
                        Date LAST_CHARG_CALC_DT = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dayEndMap.get("LAST_CHARG_CALC_DT")));
                        Date NEXT_CHARG_CALC_DT = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dayEndMap.get("NEXT_CHARG_CALC_DT")));
                        dataMap.put("LAST_CHARG_CALC_DT", LAST_CHARG_CALC_DT);
                        dataMap.put("NEXT_CHARG_CALC_DT", NEXT_CHARG_CALC_DT);
                        if (dataMap.get("DATE_TO") == null) {
                            //                             Date NEXT_CHARG_CALC_DT=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dayEndMap.get("NEXT_CHARG_CALC_DT")));
                            dataMap.put("DATE_TO", NEXT_CHARG_CALC_DT);
                        }
                    }
                    dataMap.put("CURR_DT", dataMap.get("DATE_TO"));
                    System.out.println("TO_DATE-------------->after " + dataMap.get("TO_DATE"));

                    //                runBatch((String)paramMap.get("PROD_ID"));
                    dataMap.put("CURR_DATE", currDt);
                    dataMap.put("BRANCH_CODE", branchId);
                    Date validationDt = loanIntCalTask.holiydaychecking(dataMap);
                    //                    validationDt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dataMap.get("CURR_DATE")));
                    System.out.println("validationdate###0" + validationDt);
                    if (DateUtil.dateDiff(validationDt, currDt) >= 0 || (dataMap.get("ACT_TO") != null && dataMap.get("ACT_FROM") != null)) {
                        dataMap.put("FOLIO_CHRG_APPLFREQ", paramMap.get("FOLIO_CHRG_APPLFREQ"));
                        dataMap.put("BRANCH_ID", branchId);
                        System.out.println("datamap######" + dataMap);
                        runBatch(dataMap);
                    }
                }
            }
        }

        //        CommonUtil.write("Job Completed");
        if (status.getStatus() != BatchConstants.ERROR) {
            status.setStatus(BatchConstants.COMPLETED);
        }
        System.out.println("Task Completed for .... " + productType);

        return status;
    }

    //    private void runBatch(String productId) throws Exception{
    private void runBatch(HashMap dataMap) throws Exception {
//        System.out.println("insiderunbatch####" + dataMap);
        int accListSize = 0;
        double folioCount = 0;
        HashMap foliochargeAmt = null;
        int remainFolio = 0;
        String actId = null;
        String branchCode = null;
        double availableBalance = 0;
        boolean blnValidateError = false;
        Exception validationException = null;
        Date lstCalDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dataMap.get("LAST_CHARG_CALC_DT")));
        Date nxtDt = DateUtil.addDays(lstCalDt, CommonUtil.convertObjToInt(dataMap.get("FOLIO_CHRG_APPLFREQ")));
//        System.out.println("lstCalDt-----" + lstCalDt + "   " + "nxtDt----->" + nxtDt);
        dataMap.put("NEXT_CHARG_CALC_DT", nxtDt);
        String productId = CommonUtil.convertObjToStr(dataMap.get("PROD_ID"));
        //        List accList = getAllAccounts(productId);
        Date create_limit_dt = (Date) currDt.clone();
        if (create_limit_dt != null) {
            GregorianCalendar cal = new GregorianCalendar((create_limit_dt.getYear() + yearTobeAdded), create_limit_dt.getMonth(), create_limit_dt.getDate());
            int negativevalue = foliorestrictionPeriod * -1;
            if (folioRestrictionFrq.equalsIgnoreCase("MONTHS")) {
                cal.add(GregorianCalendar.MONTH, negativevalue);
            } else if (folioRestrictionFrq.equalsIgnoreCase("DAYES")) {
                cal.add(GregorianCalendar.DAY_OF_MONTH, negativevalue);
            }
            dataMap.put("CREATE_LIMIT_DT", cal.getTime());
        }
        List accList = getAllAccounts(dataMap);
        lstCalDt = DateUtil.addDays(lstCalDt, 1);
        dataMap.put("LAST_CHARG_CALC_DT", lstCalDt);

        // TODO call unexecuted (from ACT_CHARGES which contains LogId not null data)
        // then update the Log Id with...
        // sqlMap.executeUpdate("updateActChargesLogID", strDt);
        if (folioChargeType != null && folioChargeType.equalsIgnoreCase("FIXED")) {
            try {
                sqlMap.startTransaction();
                accListSize = accList.size();
                batchList = new ArrayList();
                double totalCharge = 0.0;
                TransferTrans trans = new TransferTrans();
                   foliochargeAmt = new HashMap();
                foliochargeAmt.put("PER_FOLIO_AMT", ratePerFolio);
                foliochargeAmt.put("CHCK_UPTO_BALANCE", uptoBalance);
                foliochargeAmt.put("AVAILABLE_BALANCE", availableBalance);
                foliochargeAmt.put("PRODUCT_TYPE", productType);
                for (int i = 0; i < accListSize; i++) {
                    HashMap stMap=null;
                    int transCount = 0;
                    double charges = 0;
                    availableBalance = 0;
                    HashMap actMaster = (HashMap) accList.get(i);
                    actMaster.put("DR_AC_HD_ID", actAcHead);
                    actMaster.put("PRODUCT_ID", productId);
                    actId = (String) ((HashMap) accList.get(i)).get("ACT_NUM");
                    branchCode = (String) ((HashMap) accList.get(i)).get("BRANCH_CODE");
                    availableBalance = CommonUtil.convertObjToDouble(actMaster.get("AVAILABLE_BALANCE")).doubleValue();
                    dataMap.put("ACT_NUM", ((HashMap) accList.get(i)).get("ACT_NUM"));
                    System.out.println("getHeader().getTaskClass()##" + getClass().getName());
                    dataMap.put("TASK_NAME", "FolioChargesTask");;//getClass().getName());
                    // noOfEntriesPerFolio variab le in case of fixed using as minimum entries of transaction
                    if (noOfEntriesPerFolio > 0) {
                        transCount = getIndividualFolioAccounts(dataMap);
                        foliochargeAmt.put("REMAIN_FOLIO", new Integer(transCount));
                        if (transCount < noOfEntriesPerFolio) {
                            continue;
                        }
                    }
                    //ratePerFolio Is fixed Amount In case Of Folio Charge Fixed Type
                    double remainAmt = 0;
                    if (ratePerFolio > availableBalance) {
                        if (uptoBalance.equals("UPTO BALANCE")) {
                            remainAmt = availableBalance - ratePerFolio;
                            if (remainAmt < 0) {
                                foliochargeAmt.put("REMAIN_AMT", new Double(remainAmt));
                            }
                       charges=availableBalance;
                        }
                        else {
                            remainAmt = ratePerFolio;
                            charges = 0;
                            foliochargeAmt.put("REMAIN_AMT", new Double(remainAmt));
                        }
                    } else {
                        charges = ratePerFolio;
                        foliochargeAmt.put("REMAIN_AMT", new Double(remainAmt));
                    }
     
                    if (charges > 0) {
                        totalCharge+=charges;
                        HashMap map = new HashMap();
                        map.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(actMaster.get("PRODUCT_ID")));
                        map.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(actMaster.get("ACT_NUM")));
                        map.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(actMaster.get("BRANCH_CODE")));
                        map.put(TransferTrans.CURRENCY, LocaleConstants.DEFAULT_CURRENCY);
                        map.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(actMaster.get("DR_AC_HD_ID")));
                        map.put(TransferTrans.DR_PROD_TYPE, productType);
                        map.put(TransferTrans.PARTICULARS, "Folio Charge");
                        map.put("LINK_BATCH_ID",CommonUtil.convertObjToStr(actMaster.get("ACT_NUM")));
                        map.put("SCREEN_NAME","CHARGES");
                        map.put("GL_TRANS_ACT_NUM",CommonUtil.convertObjToStr(actMaster.get("ACT_NUM")));
                        trans.setInitiatedBranch(CommonUtil.convertObjToStr(actMaster.get("BRANCH_CODE")));
                        batchList.add(trans.getDebitTransferTO(map, charges));
                        stMap = new HashMap();
                        stMap = getChargeMap(folioAcHead, productId, availableBalance, actId, 0, null, foliochargeAmt); // foliochargeAmt hashmap want for know the folio amt 
                        stMap.put(CommonConstants.CHRG_AMT, String.valueOf(charges));
                        sqlMap.executeUpdate("Charges.insertCalcCharges", stMap);
                        sqlMap.executeUpdate("updateLastFolioChgRunDate" + productType, dataMap);
                        stMap = null;
                    }
                }
                if (totalCharge > 0) {
                    HashMap map = new HashMap();
                    System.out.println("rishad..."+_branchCode);
                    map.put(TransferTrans.CR_BRANCH,_branchCode );
                    map.put(TransferTrans.CURRENCY, LocaleConstants.DEFAULT_CURRENCY);
                    map.put(TransferTrans.CR_AC_HD,folioAcHead);
                    map.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
//        map.put(TransferTrans.DR_PROD_TYPE, super.getHeader().getProductType());
                    map.put(TransferTrans.DR_PROD_TYPE, productType);
                    map.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(TransferTrans.PARTICULARS));
                    batchList.add(trans.getCreditTransferTO(map, totalCharge));
                    setBatchList(batchList); //returns the list so that it may be logged if failure
                    trans.setInitiatedBranch(_branchCode);
                    trans.doDebitCredit(batchList,_branchCode);
                    batchList = null;
                }
                if (dataMap.get("ACT_FROM") == null && dataMap.get("ACT_TO") == null) {
            updateSeperateBranch(dataMap);
        }
                sqlMap.commitTransaction();
            } catch (Exception transErr) {
                transErr.printStackTrace();
                sqlMap.rollbackTransaction();
            }
        } else {
        runFailureBatch();

        accListSize = accList.size();
        for (int i = 0; i < accListSize; i++) {
            blnValidateError = false;
            validationException = null;
            sqlMap.startTransaction();
            try {
                int transCount = 0;
                double charges = 0;
                availableBalance = 0;
                foliochargeAmt = new HashMap();
                HashMap actMaster = (HashMap) accList.get(i);

                actMaster.put("CR_AC_HD_ID", folioAcHead);
                actMaster.put("DR_AC_HD_ID", actAcHead);
                actMaster.put("PRODUCT_ID", productId);
                actId = (String) ((HashMap) accList.get(i)).get("ACT_NUM");
                branchCode = (String) ((HashMap) accList.get(i)).get("BRANCH_CODE");
                availableBalance = CommonUtil.convertObjToDouble(actMaster.get("AVAILABLE_BALANCE")).doubleValue();
                //                transCount = getAccountTransCount(actId, productId);

                dataMap.put("ACT_NUM", ((HashMap) accList.get(i)).get("ACT_NUM"));
                System.out.println("getHeader().getTaskClass()##" + getClass().getName());
                dataMap.put("TASK_NAME", "FolioChargesTask");;//getClass().getName());
//                dataMap.put("CHARGE_TYPE","FOLIOCHG");
                transCount = getAccountTransCount(dataMap);
                List folioList = null;
                HashMap folioMap = new HashMap();
                double totAmt = 0;
                int count = 0;
                //nagtive balance check
//                folioList=sqlMap.executeQueryForList("Charges.checkNagetiveBalance",dataMap);
//                if(folioList !=null && folioList.size()>0) {
//                    folioMap=(HashMap)folioList.get(0);
//                    count=CommonUtil.convertObjToInt(folioMap.get("COUNTER"));
//                }
                folioList = null;
                folioMap = new HashMap();
                //free folio
//                if(productType.equals("AD"))
//                    folioList=sqlMap.executeQueryForList("Charges.sumAmtAD",dataMap);
//                else
//                    folioList=sqlMap.executeQueryForList("Charges.sumAmtOA",dataMap);
//                if(folioList !=null && folioList.size()>0){
//                    folioMap=(HashMap)folioList.get(0);
//                    totAmt=CommonUtil.convertObjToDouble(folioMap.get("AMT")).doubleValue();
//                }
                folioList = null;
                folioMap = new HashMap();
                //for previous folio
                folioList = sqlMap.executeQueryForList("Charges.getPreviousFolio", dataMap);
                System.out.println("previousfoliolist###" + folioList);
                int preFolio = 0;
                if (folioList != null && folioList.size() > 0) {
                    folioMap = (HashMap) folioList.get(0);
                    preFolio = CommonUtil.convertObjToInt(folioMap.get("REMAIN_FOLIO"));
                    System.out.println("preFolio##" + preFolio);
                }
                //end prefolio
                folioList = null;
                folioMap = new HashMap();
                //NO OF FREE FOLIO
                int NoFree = 0;
                if (totAmt > 0) {
                    folioList = sqlMap.executeQueryForList("Charges.getFreeFolio", dataMap);
                    if (folioList != null && folioList.size() > 0) {
                        folioMap = (HashMap) folioList.get(0);
                        NoFree = CommonUtil.convertObjToInt(folioMap.get("NO_FREE_FOLIO"));
                    }
                    System.out.println("nofree####" + NoFree);

                }
                transCount += preFolio;
                System.out.println("transCount------->" + transCount);
                transCount -= NoFree;
                System.out.println("transCount------->" + transCount);
                if (noOfEntriesPerFolio > 0) {
//                    folioCount = transCount / noOfEntriesPerFolio ;
                    remainFolio = (int) transCount % (int) noOfEntriesPerFolio;
                }
                System.out.println("beforefolioCount: " + folioCount);
                System.out.println("beforeremainFolio: " + remainFolio);
                if (remainFolio > 0) {
                    transCount = transCount - remainFolio;

                }
                System.out.println("transCount------->" + transCount);
                if (transCount > 0) {
                    folioCount = transCount / noOfEntriesPerFolio;
                }
                System.out.println("transCount------->" + transCount);
                System.out.println("beforefolioCount: " + folioCount);
                System.out.println("beforeremainFolio: " + remainFolio);

                if (folioCount < 0) {
                    folioCount = 0;
                }
                foliochargeAmt.put("REMAIN_FOLIO", new Integer(transCount));
                foliochargeAmt.put("PER_FOLIO_AMT", new Double(ratePerFolio));
                foliochargeAmt.put("CHCK_UPTO_BALANCE", uptoBalance);
                foliochargeAmt.put("AVAILABLE_BALANCE", actMaster.get("AVAILABLE_BALANCE"));
                foliochargeAmt.put("PRODUCT_TYPE", productType);
//                dataMap.put("CHARGE_TYPE","FOLIOCHG");


                if (folioCount > 0) {
                    charges = folioCount * ratePerFolio;
                }
//                    charges= calculateAndInsertCharge(folioAcHead, productId, availableBalance, actId, folioCount, null,foliochargeAmt) ; // foliochargeAmt hashmap want for knowthe folio amt 10-jan-2008

                if (charges >= 0) {
                    try {

                        ServiceTaxMaintenanceGroupDAO serTaxChg = new ServiceTaxMaintenanceGroupDAO();
                        HashMap resultMap = new HashMap();
                        resultMap.put("CHARGE_TYPE", "FOLIOCHG");
                        resultMap.put("CAL_AMT", new Double(charges));
                        resultMap.put("ACT_NUM", CommonUtil.convertObjToStr(actMaster.get("ACT_NUM")));
                        resultMap.put("ST_CAL", "ST_CAL");
                        resultMap.put(TransferTrans.PARTICULARS, "Folio Charges ServiceTax");
                        System.out.println("branchCode--------->" + branchCode);
                        resultMap.put(CommonConstants.BRANCH_ID, branchCode);
                        resultMap.put("BRANCH_ID", branchCode);
                        resultMap.put("PROD_TYPE", productType);
                        resultMap.put("PROD_ID", CommonUtil.convertObjToStr(actMaster.get("PRODUCT_ID")));
                        HashMap stMap = serTaxChg.execute(resultMap);
                        double st = CommonUtil.convertObjToDouble(stMap.get("SERVICE_TAX")).doubleValue();
                        st = st + charges;
                        double abal = CommonUtil.convertObjToDouble(actMaster.get("AVAILABLE_BALANCE")).doubleValue();

                        if (charges > 0 && abal >= st) {
                            resultMap.put("CALCULATION_TYPE", "AFTER_AUTH");
                            updateAccountHeads(actMaster, charges);
                            foliochargeAmt.put("REMAIN_FOLIO", new Integer(remainFolio));
                            serTaxChg.execute(resultMap);
                        } else {
                            foliochargeAmt.put("REMAIN_FOLIO", new Integer(transCount));
                        }
                        stMap = new HashMap();
                        stMap = getChargeMap(folioAcHead, productId, availableBalance, actId, folioCount, null, foliochargeAmt); // foliochargeAmt hashmap want for knowthe folio amt 10-jan-2008
                        System.out.println("insertChargeData" + stMap);
                        stMap.put(CommonConstants.CHRG_AMT, String.valueOf(charges));

                        sqlMap.executeUpdate("Charges.insertCalcCharges", stMap);
                        sqlMap.executeUpdate("updateLastFolioChgRunDate" + productType, dataMap);

                        stMap = null;

                    } catch (Exception validationError) {
                        validationError.printStackTrace();
                        sqlMap.rollbackTransaction();
                        blnValidateError = true;
                        validationException = validationError;

                    }
                    if (blnValidateError) {
                        sqlMap.startTransaction();
                        transferLog(validationException, folioAcHead, productId, availableBalance, actId, folioCount, 0);
                    }
                }
                if (dataMap.get("ACT_FROM") == null && dataMap.get("ACT_TO") == null) {
                updateSeperateBranch(dataMap);
                 }
                sqlMap.commitTransaction();
                
            } catch (Exception transErr) {
                transErr.printStackTrace();
                sqlMap.rollbackTransaction();
            }
            folioCount = 0;
        }
        }
    }

    private void updateSeperateBranch(HashMap updateBranchMap) throws Exception {
        System.out.println("updateBranchMap------------------------->" + updateBranchMap);
        int folioFrequency = CommonUtil.convertObjToInt(updateBranchMap.get("FOLIO_CHRG_APPLFREQ"));
        System.out.println("folioFrequency----------------->" + folioFrequency);
        Date lastChargeCalcDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(updateBranchMap.get("NEXT_CHARG_CALC_DT")));
        System.out.println("lastChargeCalcDt----------------->" + lastChargeCalcDt);
        Date nextChargeCalcDt = DateUtil.addDays(lastChargeCalcDt, folioFrequency);
        HashMap updateMap = new HashMap();
        updateMap.put("LAST_CHARG_CALC_DT", lastChargeCalcDt);
        updateMap.put("NEXT_CHARG_CALC_DT", nextChargeCalcDt);
        updateMap.put("BRANCH_ID", super.branchId);
        updateMap.put("PROD_ID", updateBranchMap.get("PROD_ID"));
        System.out.println("updatefoliocharge#####" + updateMap);
        sqlMap.executeUpdate("updateFolioCharges", updateMap);
    }
    //    private int getAccountTransCount(String actId, String productId) throws Exception{

    private int getAccountTransCount(HashMap dataMap) throws Exception {
        int transCountCash = 0;
        int transCountTransfer = 0;
        int transCountOutward = 0;
        int transCountInward = 0;
        //        String productId = CommonUtil.convertObjToStr(dataMap.get("PROD_ID"));
        //        String actId = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));

        //        transCountCash = getIndividualFolioAccountsCash(actId, productId);
        //        transCountTransfer = getIndividualFolioAccountsTransfer(actId, productId);

        transCountCash = getIndividualFolioAccountsCash(dataMap);
        transCountTransfer = getIndividualFolioAccountsTransfer(dataMap);
        if (toChargeOnType != null && (!toChargeOnType.toUpperCase().equals("DEBIT"))) {   //Debit
            //            transCountOutward = getIndividualFolioAccountsOutward(actId, productId);
            transCountOutward = getIndividualFolioAccountsOutward(dataMap);
        }

        if (toChargeOnType != null && (!toChargeOnType.toUpperCase().equals("CREDIT"))) { //Credit
            //            transCountInward = getIndividualFolioAccountsInward(actId, productId);
            transCountInward = getIndividualFolioAccountsInward(dataMap);
        }
        return transCountCash + transCountTransfer + transCountOutward + transCountInward;//+ 50; //testing
    }

    private void getParams(HashMap paramsMap) throws Exception {
        folioChargeApplicable = (String) paramsMap.get("FOLIO_CHG_APPLICABLE");
        toChargeOn = (String) paramsMap.get("TO_CHARGE_ON");
        toChargeOnType = (String) paramsMap.get("TO_CHARGE_ON_TYPE");
//        toCollectFolioChg = (String)paramsMap.get("TO_COLLECT_FOLIO_CHG") ;
        noOfEntriesPerFolio = CommonUtil.convertObjToDouble(paramsMap.get("NO_OFENTRIESPER_FOLIO")).doubleValue();
        folioAcHead = (String) paramsMap.get("FOLIO");
        actAcHead = (String) paramsMap.get("AC_HD_ID");
        ratePerFolio = CommonUtil.convertObjToDouble(paramsMap.get("RATE_PER_FOLIO")).doubleValue();
        uptoBalance = CommonUtil.convertObjToStr(paramsMap.get("TO_COLLECT_FOLIO_CHG"));
        folioRestrictionFrq =CommonUtil.convertObjToStr(paramsMap.get("FOLIO_RESTRICTION_FRQ"));
        foliorestrictionPeriod=CommonUtil.convertObjToInt(paramsMap.get("FOLIO_RESTRICTION_PERIOD"));
        folioChargeType=CommonUtil.convertObjToStr(paramsMap.get("FOLIO_CHARGE_TYPE"));
    }

    //    private List getAllAccounts(String productId) throws Exception{
    private List getAllAccounts(HashMap productId) throws Exception {
        return (List) sqlMap.executeQueryForList(accountQuery, productId);
    }

    //    private int getIndividualFolioAccountsCash(String actNum, String prodId) throws Exception{
    private int getIndividualFolioAccountsCash(HashMap inputMap) throws Exception {
        //        HashMap inputMap = new HashMap();
        //        inputMap.put("ACT_NUM", actNum);
        //        inputMap.put("PROD_ID", prodId);
        inputMap.put("TO_CHARGE_ON_TYPE", null);

        if (toChargeOnType != null && (!toChargeOnType.toUpperCase().equals("BOTH"))) {
            inputMap.put("TO_CHARGE_ON_TYPE", toChargeOnType); // Credit/Debit/Both
        }
        return Integer.parseInt((String) sqlMap.executeQueryForObject("Charges.getIndiFolioAccountsCash", inputMap));
    }

    //    private int getIndividualFolioAccountsTransfer(String actNum, String prodId) throws Exception{
    private int getIndividualFolioAccountsTransfer(HashMap inputMap) throws Exception {
        //        HashMap inputMap = new HashMap();
        //        inputMap.put("ACT_NUM", actNum);
        //        inputMap.put("PROD_ID", prodId);
        inputMap.put("TO_CHARGE_ON", null);
        inputMap.put("TO_CHARGE_ON_TYPE", null);
        inputMap.put("SYSTEM", "");

        //Below code added to handle TL / AD data.
        if (toChargeOn != null && toChargeOn.toUpperCase().equals("S")) {
            toChargeOn = "SYSTEM";
        } else if (toChargeOn != null && toChargeOn.toUpperCase().equals("M")) {
            toChargeOn = "MANUAL";
        } else if (toChargeOn != null && toChargeOn.toUpperCase().equals("B")) {
            toChargeOn = "BOTH";
        }
        //end of code added to handle TL / AD data.

        if (toChargeOn != null && !toChargeOn.toUpperCase().equals("BOTH")) {// System/Manual/Both
            inputMap.put("TO_CHARGE_ON", toChargeOn);
            inputMap.put("SYSTEM", "TTSYSTEM");
        }
//        else if(toChargeOn !=null && (toChargeOn.toUpperCase().equals("SYSTEM")||toChargeOn.toUpperCase().equals("MANUAL")))

        if (toChargeOnType != null && (!toChargeOnType.toUpperCase().equals("BOTH"))) {
            inputMap.put("TO_CHARGE_ON_TYPE", toChargeOnType); // Credit/Debit/Both
        }
        System.out.println("transferonly####" + inputMap);
        return Integer.parseInt((String) sqlMap.executeQueryForObject("Charges.getIndiFolioAccountsTransfer", inputMap));
    }

    //    private int getIndividualFolioAccountsOutward(String actNum, String prodId) throws Exception{
    private int getIndividualFolioAccountsOutward(HashMap inputMap) throws Exception {
        //        HashMap inputMap = new HashMap();
        //        inputMap.put("ACT_NUM", actNum);
        //        inputMap.put("PROD_ID", prodId);

        return Integer.parseInt((String) sqlMap.executeQueryForObject("Charges.getIndiFolioAccountsOutward", inputMap));
    }

    //    private int getIndividualFolioAccountsInward(String actNum, String prodId) throws Exception{
    private int getIndividualFolioAccountsInward(HashMap inputMap) throws Exception {
        //        HashMap inputMap = new HashMap();
        //        inputMap.put("ACT_NUM", actNum);
        //        inputMap.put("PROD_ID", prodId);



        return Integer.parseInt((String) sqlMap.executeQueryForObject("Charges.getIndiFolioAccountsInward", inputMap));
    }
     
	private int getIndividualFolioAccounts(HashMap inputMap) throws Exception {
        inputMap.put("TO_CHARGE_ON_TYPE", null);
        if (toChargeOnType != null && (!toChargeOnType.toUpperCase().equals("BOTH"))) {
            inputMap.put("TO_CHARGE_ON_TYPE", toChargeOnType); // Credit/Debit/Both
        }
        return Integer.parseInt((String) sqlMap.executeQueryForObject("Charges.getIndividualFolioAccounts", inputMap));
    }
    public static void main(String args[]) {
        try {
            TaskHeader header = new TaskHeader();
            header.setProductType("AD");
            header.setTransactionType("FOLIOCHG");
            header.setTaskClass("FolioChargesTask");
            HashMap param = new HashMap();
            //            param.put("PROD_ID","CAGen");
            //            param.put("ACT_FROM","OA060862");
            //            param.put("ACT_TO","OA060869");
            ////            param.put("DATE_FROM","");
            ////            param.put("DATE_TO","");
            //
            header.setTaskParam(param);

            FolioChargesTask ft = new FolioChargesTask(header);
            ft.executeTask();
        } catch (Exception E) {
            E.printStackTrace();
        }
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
}
