/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * NPATask.java
 *
 * Created on July 9, 2004, 4:40 PM
 */
package com.see.truetransact.serverside.batchprocess.task.loan;

import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.DateUtil;
import com.ibatis.db.sqlmap.SqlMap;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Sunil (152691)
 * @creation date 31 March 2005
 */
public class NPATask extends Task {

    private static SqlMap sqlMap = null;
    private String branch = null;
    private Date currDt = null;
    private String dayEndType;
    private List branchList;
    private HashMap paramMap;
    private String taskLable;

    /**
     * Creates a new instance of NPATask
     */
    public NPATask(TaskHeader header) throws Exception {
        setHeader(header);
        branch = header.getBranchID();
        currDt = ServerUtil.getCurrentDate(super._branchCode);
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        paramMap = header.getTaskParam();
        if (paramMap != null && paramMap.containsKey("DAY_END_TYPE")) {
            dayEndType = CommonUtil.convertObjToStr(paramMap.get("DAY_END_TYPE"));
        }
        if (CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
            HashMap tempMap = new HashMap();
            if (paramMap.containsKey("BRANCH_LST")) {
                branchList = (List) paramMap.get("BRANCH_LST");
                System.out.println("branchList*****" + branchList);
            } else {
                tempMap.put("NEXT_DATE", currDt);
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
        if (paramMap != null && paramMap.containsKey("NPA_TASK_LABLE")) {
            taskLable = CommonUtil.convertObjToStr(paramMap.get("NPA_TASK_LABLE"));
        }
    }

    public TaskStatus executeTask() throws Exception {
        TaskStatus status = new TaskStatus();
        status.setStatus(BatchConstants.STARTED);
        HashMap paramMap = new HashMap();

//        HashMap hash;
        boolean runStatus = true;

        paramMap.put("TODAY_DT", currDt);
        //Check and change standard to sub standard here
//        try{

//            hash=new HashMap();
        if (branchList != null && branchList.size() > 0) {
            for (int b = 0; b < branchList.size(); b++) {
                HashMap branchMap = (HashMap) branchList.get(b);
                branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                currDt = ServerUtil.getCurrentDate(branch);
                HashMap compMap = new HashMap();
                compMap.put("TASK_NAME", taskLable);
                compMap.put("DAYEND_DT", currDt);
                compMap.put("BRANCH_ID", branch);
                List compLst = (List) sqlMap.executeQueryForList("getSelectTaskLst", compMap);
                compMap = null;
                String compStatus = "";
                if (compLst != null && compLst.size() > 0) {
                    compMap = (HashMap) compLst.get(0);
                    compStatus = CommonUtil.convertObjToStr(compMap.get("TASK_STATUS"));
                    compMap = null;
                }
                if (compLst.size() <= 0 || compStatus.equals("ERROR")) {
                    try {
                        sqlMap.startTransaction();
                        HashMap errorMap = new HashMap();
                        errorMap.put("ERROR_DATE", currDt);
                        errorMap.put("TASK_NAME", taskLable);
                        errorMap.put("BRANCH_ID", branch);
                        sqlMap.executeUpdate("deleteError_showing", errorMap);
                        sqlMap.commitTransaction();
                        paramMap = new HashMap();
                        paramMap.put(CommonConstants.BRANCH_ID, branch);
                        paramMap.put("BRANCH_ID", branch);
                        paramMap.put("TODAY_DT", currDt);
                        List lst = sqlMap.executeQueryForList("getLoansProduct", paramMap);
                        HashMap productMap = new HashMap();
                        if (lst != null && lst.size() > 0) {
                            for (int product = 0; product < lst.size(); product++) {
                                productMap = (HashMap) lst.get(product);
                                Date curr_dt = (Date) currDt.clone();
                                paramMap.put("PROD_ID", productMap.get("PROD_ID"));
                                String behaves_like = CommonUtil.convertObjToStr(productMap.get("BEHAVES_LIKE"));
                                List actLst = null;
                                if (behaves_like != null && behaves_like.equals("OD")) {
                                    actLst = sqlMap.executeQueryForList("getAllLoanRecordOD", paramMap);
                                } else {
                                    actLst = sqlMap.executeQueryForList("getAllLoanRecord", paramMap);
                                }
                                for (int s = 0; s < actLst.size(); s++) {
                                    HashMap hashMap = (HashMap) actLst.get(s);
                                    String behaveLike = CommonUtil.convertObjToStr(hashMap.get("BEHAVES_LIKE"));
                                    Date instalDt = new Date();
                                    paramMap.put("ACT_NUM", hashMap.get("ACCT_NUM"));
                                    hashMap.put("ACT_NUM", hashMap.get("ACCT_NUM"));
                                    paramMap.putAll(hashMap);
                                    hashMap.put(CommonConstants.BRANCH_ID, paramMap.get(CommonConstants.BRANCH_ID));
                                    instalDt = getInstallmentDate(paramMap);
                                    decalreAssetStatus(instalDt, paramMap, hashMap, curr_dt, behaves_like);
                                    System.out.println("instalDt###" + instalDt);
                                    instalDt = new Date();
                                    if (hashMap.containsKey("LAST_INT_CALC_DT") && hashMap.get("LAST_INT_CALC_DT") != null) {
                                        instalDt = (Date) hashMap.get("LAST_INT_CALC_DT");
                                        System.out.println("instalDt###" + instalDt);

                                        decalreAssetStatus(instalDt, paramMap, hashMap, curr_dt, behaves_like);
                                    }
                                }
                            }
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
                            }
                        }
                    } catch (Exception error) {
//                            sqlMap.rollbackTransaction();
//                            runStatus = false;
//                            System.out.println("Could not update doubtful 3 to loss");
//                            error.printStackTrace();
                        sqlMap.rollbackTransaction();
                        status.setStatus(BatchConstants.ERROR);
                        sqlMap.startTransaction();
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
                        sqlMap.commitTransaction();

//                        HashMap errorMap = new HashMap();
//                        errorMap.put("ERROR_DATE",currDt);
//                        errorMap.put("TASK_NAME", taskLable);
//                        errorMap.put("ERROR_MSG",error.getMessage());
//                        System.out.println("Error ERROR_MSG " + error.getMessage());
//                        errorMap.put("ACT_NUM",paramMap.get("ACT_NUM"));
//                        errorMap.put("BRANCH_ID", branch);
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
                        if (error instanceof TTException) {
                            System.out.println("#$#$ if TTException part..." + error);
                            tte = (TTException) error;
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
                                    System.out.println("#$#$ if not TTException part..." + error);
                                    errMsg = error.getMessage();
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
                            System.out.println("#$#$ if not TTException part..." + error);
                            errMsg = error.getMessage();
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

                        error.printStackTrace();
                    }
                }
            }
        }
//        }
//        catch(Exception error){
//            sqlMap.rollbackTransaction();
//            runStatus = false;
//            System.out.println("Could not update doubtful 3 to loss");
//            error.printStackTrace();
//        }
        /**
         * *************************************88
         */
        /*  sqlMap.executeUpdate("NPA_std_to_substdtest", paramMap);
           
         for(int i=0;i<lst.size();i++){
         hash=(HashMap)lst.get(i);
         hash.put("CURR_STATUS","SUB_STANDARD_ASSETS");
         hash.put("TO_DATE",paramMap.get("TODAY_DT"));
         hash.put("STATUS_BY","TTSYSTEM");
         hash.put("AUTHORIZE_STATUS","AUTHORIZED");
         sqlMap.executeUpdate("INSERT_NPA_HISTORY",hash);
         }
         sqlMap.commitTransaction();
         }catch(Exception error){
         sqlMap.rollbackTransaction();
         runStatus = false;
         System.out.println("Could not update std to sub std");
         error.printStackTrace();
         }
           
         //Check and change sub standard to doubt here
         try{
         sqlMap.startTransaction();
           
         List lst=sqlMap.executeQueryForList("NPA_SELECT_SUBSTD_TO_DOUBT_1",paramMap);
         sqlMap.executeUpdate("NPA_substd_to_doubt_1", paramMap);
         hash=new HashMap();
         for(int i=0;i<lst.size();i++){
         hash=(HashMap)lst.get(i);
         hash.put("CURR_STATUS","DOUBTFUL_ASSETS_1");
         hash.put("TO_DATE",paramMap.get("TODAY_DT"));
         hash.put("STATUS_BY","TTSYSTEM");
         hash.put("AUTHORIZE_STATUS","AUTHORIZED");
         sqlMap.executeUpdate("INSERT_NPA_HISTORY",hash);
         }
         sqlMap.commitTransaction();
         }catch(Exception error){
         sqlMap.rollbackTransaction();
         runStatus = false;
         System.out.println("Could not update sub std to doubtful 1");
         error.printStackTrace();
         }
           
         try{
         sqlMap.startTransaction();
           
         List lst=sqlMap.executeQueryForList("NPA_SELECT_SUBSTD_TO_DOUBT_2",paramMap);
         sqlMap.executeUpdate("NPA_substd_to_doubt_2", paramMap);
         hash=new HashMap();
         for(int i=0;i<lst.size();i++){
         hash=(HashMap)lst.get(i);
         hash.put("CURR_STATUS","DOUBTFUL_ASSETS_2");
         hash.put("TO_DATE",paramMap.get("TODAY_DT"));
         hash.put("STATUS_BY","TTSYSTEM");
         hash.put("AUTHORIZE_STATUS","AUTHORIZED");
         sqlMap.executeUpdate("INSERT_NPA_HISTORY",hash);
         }
         sqlMap.commitTransaction();
         }catch(Exception error){
         sqlMap.rollbackTransaction();
         runStatus = false;
         System.out.println("Could not update doubtful 1 to doubtful 2");
         error.printStackTrace();
         }
           
         try{
         sqlMap.startTransaction();
         List lst=sqlMap.executeQueryForList("NPA_SELECT_SUBSTD_TO_DOUBT_3",paramMap);
         sqlMap.executeUpdate("NPA_substd_to_doubt_3", paramMap);
         hash=new HashMap();
         for(int i=0;i<lst.size();i++){
         hash=(HashMap)lst.get(i);
         hash.put("CURR_STATUS","DOUBTFUL_ASSETS_3");
         hash.put("TO_DATE",paramMap.get("TODAY_DT"));
         hash.put("STATUS_BY","TTSYSTEM");
         hash.put("AUTHORIZE_STATUS","AUTHORIZED");
         sqlMap.executeUpdate("INSERT_NPA_HISTORY",hash);
         }
         sqlMap.commitTransaction();
         }catch(Exception error){
         sqlMap.rollbackTransaction();
         runStatus = false;
         System.out.println("Could not update doubtful 2 to doubtful 3");
         error.printStackTrace();
         }
           
         //Check and change doubt to loss here
         try{
         sqlMap.startTransaction();
         List lst=sqlMap.executeQueryForList("NPA_SELECT_DOUBT_3_TO_LOSS",paramMap);
         sqlMap.executeUpdate("NPA_doubt_3_to_loss", paramMap);
         hash=new HashMap();
         for(int i=0;i<lst.size();i++){
         hash=(HashMap)lst.get(i);
         hash.put("CURR_STATUS","LOSS_ASSETS");
         hash.put("TO_DATE",paramMap.get("TODAY_DT"));
         hash.put("STATUS_BY","TTSYSTEM");
         hash.put("AUTHORIZE_STATUS","AUTHORIZED");
         sqlMap.executeUpdate("INSERT_NPA_HISTORY",hash);
         }
         sqlMap.commitTransaction();
         }catch(Exception error){
         sqlMap.rollbackTransaction();
         runStatus = false;
         System.out.println("Could not update doubtful 3 to loss");
         error.printStackTrace();
         } */
        if (!runStatus) {
            status.setStatus(BatchConstants.ERROR);
        } else {
            status.setStatus(BatchConstants.COMPLETED);
        }
        System.out.println(status.getStatus());
        paramMap = null;
        return status;
    }

    public void decalreAssetStatus(Date instalDt, HashMap paramMap, HashMap hashMap, Date curr_dt, String behaves_like) throws Exception {
        String ASSET_STATUS = null;
        if (instalDt != null) {
            paramMap.put("INSTALLMENT_DT", instalDt);
            String asset_status = CommonUtil.convertObjToStr(hashMap.get("ASSET_STATUS"));
            System.out.println(asset_status.equals("STANDARD_ASSETS") + "parammap#####" + paramMap);
            int period = CommonUtil.convertObjToInt(hashMap.get("PERIOD_TRANS_SUBSTANDARD"));
            System.out.println("datediff#####" + DateUtil.dateDiff(DateUtil.addDays(instalDt, period), curr_dt));
            if (period > 0) {
                if (DateUtil.dateDiff(DateUtil.addDays(instalDt, period), curr_dt) > 0 && asset_status.equals("STANDARD_ASSETS")) {
                    ASSET_STATUS = "SUB_STANDARD_ASSETS";
                    HashMap assetMap = new HashMap();
                    if (behaves_like != null && behaves_like.equals("OD")) {
                        assetMap.put("PRODUCT_TYPE", "AD");
                    } else {
                        assetMap.put("PRODUCT_TYPE", "TL");
                    }
                    assetMap.put("PRODUCT_ID", hashMap.get("PROD_ID"));
                    assetMap.put("ACT_TO", hashMap.get("ACCT_NUM"));
                    assetMap.put("ACT_FROM", hashMap.get("ACCT_NUM"));
                    assetMap.put("DATE_FROM", hashMap.get("LAST_INT_CALC_DT"));
                    assetMap.put("DATE_TO", curr_dt);
                    assetMap.put("CHARGESUI", "CHARGESUI");
                    System.out.println("assetMap#####" + assetMap);
                    if (hashMap.containsKey("CALENDAR_FREQ") && hashMap.get("CALENDAR_FREQ") != null && hashMap.get("CALENDAR_FREQ").equals("Y")) {
                        getHeader().setTaskParam(assetMap);
                        InterestCalculationTask intcalTask = new InterestCalculationTask(getHeader());
                        intcalTask.executeTask();
                    }
                }
            }

            period = CommonUtil.convertObjToInt(hashMap.get("PERIOD_TRANS_DOUBTFUL"));
            if (period > 0) {
                if (DateUtil.dateDiff(DateUtil.addDays(instalDt, period), curr_dt) > 0 && asset_status.equals("SUB_STANDARD_ASSETS")) {
                    ASSET_STATUS = "DOUBTFUL_ASSETS_1";
                }
            }

            period = CommonUtil.convertObjToInt(hashMap.get("PERIOD_TRANS_DOUBTFUL_2"));
            if (period > 0) {
                if (DateUtil.dateDiff(DateUtil.addDays(instalDt, period), curr_dt) > 0 && asset_status.equals("DOUBTFUL_ASSETS_1")) {
                    ASSET_STATUS = "DOUBTFUL_ASSETS_2";
                }
            }

            period = CommonUtil.convertObjToInt(hashMap.get("PERIOD_TRANS_DOUBTFUL_3"));
            if (period > 0) {
                if (DateUtil.dateDiff(DateUtil.addDays(instalDt, period), curr_dt) > 0 && asset_status.equals("DOUBTFUL_ASSETS_2")) {
                    ASSET_STATUS = "DOUBTFUL_ASSETS_3";
                }
            }
            period = CommonUtil.convertObjToInt(hashMap.get("PERIOD_TRANS_LOSS"));
            if (period > 0) {
                if (DateUtil.dateDiff(DateUtil.addDays(instalDt, period), curr_dt) > 0 && asset_status.equals("DOUBTFUL_ASSETS_3")) {
                    ASSET_STATUS = "LOSS_ASSETS";
                }
            }
            hashMap.put("CURR_STATUS", ASSET_STATUS);
            hashMap.put("TO_DATE", paramMap.get("TODAY_DT"));
            hashMap.put("TODAY_DT", paramMap.get("TODAY_DT"));
            hashMap.put("AUTHORIZE_STATUS", "AUTHORIZED");
            hashMap.put("ACT_NUM", hashMap.get("ACCT_NUM"));
            sqlMap.startTransaction();

            System.out.println("finalmap####" + hashMap);
            if (ASSET_STATUS != null) {
                sqlMap.executeUpdate("INSERT_NPA_HISTORY", hashMap);
                sqlMap.executeUpdate("NPA_std_to_substdtest", hashMap);

            }
            sqlMap.commitTransaction();
        }
    }

    public Date getInstallmentDate(HashMap paramMap) throws Exception {
        HashMap allInstallmentMap = new HashMap();
        Date inst_dt = null;
        System.out.println("paramMap###" + paramMap);
        String behaveLike = CommonUtil.convertObjToStr(paramMap.get("BEHAVES_LIKE"));
        if (behaveLike != null && behaveLike != "OD") {
            List paidAmt = sqlMap.executeQueryForList("getPaidPrinciple", paramMap);
            allInstallmentMap = (HashMap) paidAmt.get(0);
            double totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
            paidAmt = sqlMap.executeQueryForList("getIntDetails", paramMap);
            if (paidAmt != null && paidAmt.size() > 0) {
                allInstallmentMap = (HashMap) paidAmt.get(0);
            }
            double totexcessamt = CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
            totPrinciple += totexcessamt;
            //            if(totPrinciple >0){
            List allLst = sqlMap.executeQueryForList("getAllLoanInstallment", paramMap);
            inst_dt = null;
            for (int i = 0; i < allLst.size(); i++) {
                allInstallmentMap = (HashMap) allLst.get(i);
                double instalAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                if (instalAmt <= totPrinciple) {
                    totPrinciple -= instalAmt;
                    inst_dt = new Date();
                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                } else {
                    inst_dt = new Date();
                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                    totPrinciple += CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                    break;
                }

            }

            //            }
        } else {

            String asset_status = CommonUtil.convertObjToStr(paramMap.get("ASSET_STATUS"));
            Date curr_dt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("TODAY_DT")));
            Date previus_dt = null;
            List transDetails = sqlMap.executeQueryForList("getFirstTranDetails", paramMap);
            System.out.println("getFirstTranDetails####" + transDetails);
            if (asset_status.equals("STANDARD_ASSETS")) {
                if (paramMap.get("PERIOD_TRANS_SUBSTANDARD") != null) {
                    previus_dt = DateUtil.addDays(curr_dt, -CommonUtil.convertObjToInt(paramMap.get("PERIOD_TRANS_SUBSTANDARD")));
                } else {
                    previus_dt = DateUtil.addDays(curr_dt, 0);
                }
            }

            if (asset_status.equals("SUB_STANDARD_ASSETS")) {
                if (paramMap.get("PERIOD_TRANS_DOUBTFUL") != null) {
                    previus_dt = DateUtil.addDays(curr_dt, -CommonUtil.convertObjToInt(paramMap.get("PERIOD_TRANS_DOUBTFUL")));
                } else {
                    previus_dt = DateUtil.addDays(curr_dt, 0);
                }
            }

            if (asset_status.equals("DOUBTFUL_ASSETS")) {
                if (paramMap.get("PERIOD_TRANS_DOUBTFUL_1") != null) {
                    previus_dt = DateUtil.addDays(curr_dt, -CommonUtil.convertObjToInt(paramMap.get("PERIOD_TRANS_DOUBTFUL_1")));
                } else {
                    previus_dt = DateUtil.addDays(curr_dt, 0);
                }
            }
            if (asset_status.equals("DOUBTFUL_ASSETS_1")) {
                if (paramMap.get("PERIOD_TRANS_DOUBTFUL_2") != null) {
                    previus_dt = DateUtil.addDays(curr_dt, -CommonUtil.convertObjToInt(paramMap.get("PERIOD_TRANS_DOUBTFUL_2")));
                } else {
                    previus_dt = DateUtil.addDays(curr_dt, 0);
                }
            }

            if (asset_status.equals("DOUBTFUL_ASSETS_2")) {
                if (paramMap.get("PERIOD_TRANS_DOUBTFUL_3") != null) {
                    previus_dt = DateUtil.addDays(curr_dt, -CommonUtil.convertObjToInt(paramMap.get("PERIOD_TRANS_DOUBTFUL_3")));
                } else {
                    previus_dt = DateUtil.addDays(curr_dt, 0);
                }
            }

            if (asset_status.equals("DOUBTFUL_ASSETS_3")) {
                if (paramMap.get("PERIOD_TRANS_LOSS") != null) {
                    previus_dt = DateUtil.addDays(curr_dt, -CommonUtil.convertObjToInt(paramMap.get("PERIOD_TRANS_LOSS")));
                } else {
                    previus_dt = DateUtil.addDays(curr_dt, 0);
                }
            }

            Date transdt = null;
            if (transDetails != null && transDetails.size() > 0) {
                HashMap transDetailsMap = (HashMap) transDetails.get(0);
                transdt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(transDetailsMap.get("TRANS_DT")));
            }
            System.out.println("previusdate" + previus_dt + "transdt" + transdt);
            System.out.println("DateUtil.dateDiff(previus_dt,transdt)<0" + DateUtil.dateDiff(previus_dt, transdt));
            if (transdt != null && DateUtil.dateDiff(previus_dt, transdt) < 0) {

                //                         if(asset_status.equals("STANDARD_ASSETS"))
                //                         {
                GregorianCalendar firstdaymonth = new GregorianCalendar(1, previus_dt.getMonth() + 1, previus_dt.getYear() + 1900);
                int noOfDays = firstdaymonth.getActualMaximum(firstdaymonth.DAY_OF_MONTH);
                GregorianCalendar lastdaymonth = new GregorianCalendar(previus_dt.getYear() + 1900, previus_dt.getMonth() + 1, noOfDays);
                paramMap.put("FROM_DT", firstdaymonth.getTime());
                paramMap.put("TO_DATE", lastdaymonth.getTime());
                System.out.println("paramMap###" + paramMap);
                List gettotCredit = sqlMap.executeQueryForList("getTotTranAmt", paramMap);
                System.out.println("getTotTranAmt####" + transDetails);
                if (gettotCredit != null && gettotCredit.size() > 0) {
                    GregorianCalendar debitIntMonth = new GregorianCalendar(previus_dt.getYear() + 1900, previus_dt.getMonth(), 1);
                    noOfDays = debitIntMonth.getActualMaximum(debitIntMonth.DAY_OF_MONTH);
                    GregorianCalendar debitlastdaymonth = new GregorianCalendar(previus_dt.getYear() + 1900, previus_dt.getMonth(), noOfDays);
                    paramMap.put("FROM_DT", debitIntMonth.getTime());
                    paramMap.put("TO_DATE", debitlastdaymonth.getTime());
                    System.out.println("getTotTranAmt####2paramMap" + paramMap);
                    List lst = sqlMap.executeQueryForList("getDebitTranAmt", paramMap);
                    if (lst != null && lst.size() > 0) {
                        HashMap hash = (HashMap) lst.get(0);
                        HashMap totCredit = (HashMap) gettotCredit.get(0);
                        double ibal = CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
                        double totCreditamt = CommonUtil.convertObjToDouble(totCredit.get("AMOUNT")).doubleValue();
                        if (totCreditamt >= ibal) {
                            inst_dt = null;
                        } else {
                            inst_dt = (Date) currDt.clone();
                        }
                    } else {
                        inst_dt = null;
                    }
                }

                //
            } else {
                inst_dt = null;
            }


        }

        return inst_dt;
    }

    public static void main(String args[]) {
        try {
            TaskHeader header = new TaskHeader();
            header.setTaskClass("NPATask");
            HashMap paramMap = new HashMap();
            header.setBranchID("BRAN");
            header.setTaskParam(paramMap);
            NPATask ft = new NPATask(header);
            System.out.println(ft.executeTask());
        } catch (Exception E) {
            E.printStackTrace();
        }
    }
}
