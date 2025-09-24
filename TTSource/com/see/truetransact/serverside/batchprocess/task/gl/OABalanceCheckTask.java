/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * OABalanceCheckTask.java
 *
 * Created on March 10, 2005, 10:40 AM
 */
package com.see.truetransact.serverside.batchprocess.task.gl;

import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverutil.ServerUtil;
import com.ibatis.db.sqlmap.SqlMap;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
//
///**
// *
// * @author  Sunil (152691)
// */
public class OABalanceCheckTask extends Task {

    private static SqlMap sqlMap = null;
    private String branch = null;
    HashMap finalMap = new HashMap();
    private String TODAY_DT = "TODAY_DT";
    private String AC_HD_ID = "AC_HD_ID";
    private String CLOSE_BAL = "CLOSE_BAL";
    private String OPN_BAL = "OPN_BAL";
    private String TRANS_AMT = "TRANS_AMT";
    private String BALANCE_TYPE = "BALANCE_TYPE";
    private String BALANCE = "BALANCE";
    ArrayList finalList = new ArrayList();
    private Date currDt = null;
    private String dayEndType;
    private List branchList;
    private HashMap taskMap;
    private String taskLable;

    /**
     * Creates a new instance of OABalanceCheckTask
     */
    public OABalanceCheckTask(TaskHeader header) throws Exception {
        setHeader(header);
        branch = header.getBranchID();
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        currDt = ServerUtil.getCurrentDate(branch);
        taskMap = header.getTaskParam();
        if (taskMap != null && taskMap.containsKey("DAY_END_TYPE")) {
            dayEndType = CommonUtil.convertObjToStr(taskMap.get("DAY_END_TYPE"));
        }
        if (CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
            if (taskMap.containsKey("BRANCH_LST")) {
                branchList = (List) taskMap.get("BRANCH_LST");
                System.out.println("branchList*****" + branchList);
            } else {
                HashMap tempMap = new HashMap();
                tempMap.put("NEXT_DATE", currDt);
                branchList = (List) sqlMap.executeQueryForList("getAllBranchesFromDayEnd", tempMap);
                tempMap = null;
            }
        } else {
            HashMap tempMap = new HashMap();
            tempMap.put(CommonConstants.BRANCH_ID, getHeader().getBranchID());
            branchList = new ArrayList();
            branchList.add(tempMap);
            tempMap = null;
        }
        if (taskMap != null && taskMap.containsKey("OA_BAL_CHK_TASK_LABLE")) {
            taskLable = CommonUtil.convertObjToStr(taskMap.get("OA_BAL_CHK_TASK_LABLE"));
        }
    }
//    
//    public TaskStatus executeTask() throws Exception {
//        TaskStatus status = new TaskStatus();
//        status.setStatus(BatchConstants.STARTED);
//        HashMap paramMap = new HashMap();
//        paramMap.put(BRANCH_CODE, branch);
//        if(sqlMap.executeQueryForList("getOABalCheck", paramMap).size() > 0 ) {
//            status.setStatus(BatchConstants.ERROR);
//        } else {
//            status.setStatus(BatchConstants.COMPLETED);
//        }
//        System.out.println (status.getStatus());
//        paramMap = null ;
//        return status;
//    }
//    
//  
//    public static void main(String args[]){
//        try{
//            TaskHeader header = new TaskHeader();
//            header.setTaskClass("OABalanceCheckTask");
//            HashMap paramMap = new HashMap();
//            header.setBranchID("BRAN");
//            header.setTaskParam(paramMap);
//            OABalanceCheckTask ft = new OABalanceCheckTask(header);
//            System.out.println (ft.executeTask());
//        }catch(Exception E){
//            E.printStackTrace();
//        }
//    }
//}

//package com.see.truetransact.serverside.batchprocess.task.gl;
//import com.see.truetransact.commonutil.CommonConstants;
//import com.see.truetransact.commonutil.CommonUtil ;
//import com.see.truetransact.commonutil.DateUtil ;
//import com.see.truetransact.clientutil.ClientUtil ;
//import com.see.truetransact.commonutil.TTException ;
//import java.util.HashMap;
//import java.util.List;
//import java.util.ArrayList;
//import com.see.truetransact.serverside.batchprocess.task.Task;
//
//
    /**
     *
     * @author Sunil (152691)
     */
//public class OABalanceCheckTask{
//    HashMap finalMap = new HashMap();
//    private String BRANCH_CODE = "BRANCH_CODE" ;
//    private String TODAY_DT = "TODAY_DT" ;
//    private String AC_HD_ID = "AC_HD_ID" ;
//    private String CLOSE_BAL = "CLOSE_BAL" ;
//    private String OPN_BAL = "OPN_BAL" ;
//    private String TRANS_AMT = "TRANS_AMT" ;
//    private String BALANCE_TYPE = "BALANCE_TYPE";
//    private String BALANCE = "BALANCE";
//
//    ArrayList finalList = new ArrayList();
    /**
     * Creates a new instance of BalanceCheck
     */
//    public OABalanceCheckTask() throws Exception {
//    }
    private void checkAndAddKeys(List outputList) {
        HashMap tempMap = null;
        double amt = 0;
        Double objAmt = null;
        for (int i = 0, j = outputList.size(); i < j; i++) {
            tempMap = (HashMap) outputList.get(i);
            objAmt = CommonUtil.convertObjToDouble(tempMap.get(CommonConstants.DEBIT));
            amt = (-1) * objAmt.doubleValue();
            objAmt = CommonUtil.convertObjToDouble(tempMap.get(CommonConstants.CREDIT));
            amt = amt + objAmt.doubleValue();

            if (finalMap.containsKey(tempMap.get("AC_HD_ID"))) {
                amt = amt
                        + CommonUtil.convertObjToDouble(finalMap.get(tempMap.get("AC_HD_ID"))).doubleValue();
            }
            finalMap.put(tempMap.get("AC_HD_ID"), new Double(amt));
            tempMap = null;
            objAmt = null;
            amt = 0;
        }
    }

    private void addToFinalList(List outputList) {
        HashMap tempMap = null;
        for (int i = 0, j = outputList.size(); i < j; i++) {
            tempMap = (HashMap) outputList.get(i);
            finalMap.put(tempMap.get("AC_HD_ID"), tempMap.get("BALANCE"));

            tempMap = null;
        }
        System.out.println("finalMap      ----" + finalMap);
    }

    private void checkWithGL() throws Exception {
        HashMap tempMap = null;
        double glAmt = 0;
        double acHdAmt = 0;
        HashMap paramMap = new HashMap();
        paramMap.put(CommonConstants.BRANCH_ID, branch);
        paramMap.put("TODAY_DT", currDt);
        List outputList = sqlMap.executeQueryForList("selectTotalGlDayEnd", paramMap);
        if (outputList != null && outputList.size() > 0) {
            System.out.println("outputList : " + outputList);
            for (int i = 0, j = outputList.size(); i < j; i++) {
                tempMap = (HashMap) outputList.get(i);
                System.out.println("tempMap : " + tempMap);
                System.out.println("finalMap : " + finalMap);
                //             System.out.println("diff"+finalMap-empMap);
                if (finalMap.containsKey(tempMap.get("AC_HD_ID"))) {
                    glAmt = CommonUtil.convertObjToDouble(tempMap.get("CLOSE_BAL")).doubleValue();
                    acHdAmt = CommonUtil.convertObjToDouble(finalMap.get(tempMap.get("AC_HD_ID"))).doubleValue();
                    System.out.println("glAmt : " + glAmt);
                    System.out.println("acHdAmt : " + acHdAmt);
                    tempMap.put("TRANS_AMT", new Double(acHdAmt - glAmt));
                    if (glAmt != acHdAmt) {
                        finalList.add(tempMap);
                    }
                }
            }
        }
        finalMap = new HashMap();
    }

    public TaskStatus executeTask() throws Exception {
        TaskStatus status = new TaskStatus();
        status.setStatus(BatchConstants.STARTED);
        /**
         * What this method does : Executes 5 queries. The output of each query
         * is added as a key value pair in a HashMap. Key is each AC_HD_ID,
         * value is Amount. Each time the query executes it checks if the key
         * exists in the finalMap If key exists, amt is updated. Dr amounts are
         * multiplied by -1 and added to existing amt. Cr amounts are added as
         * is. The last query totals the amount for each account head against
         * opening and closing balance in GL_Abstract. Hence the pre requesite
         * is that the GL_Abstartct insert task SHOULD be executed before this
         * code is run.
         */
        HashMap paramMap = new HashMap();
        //paramMap.put("BRANCH_CODE", "BRAN");
        paramMap.put("TODAY_DT", currDt);

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
                    sqlMap.startTransaction();
                    HashMap errorMap = new HashMap();
                    errorMap.put("ERROR_DATE", currDt);
                    errorMap.put("TASK_NAME", taskLable);
                    errorMap.put("BRANCH_ID", branch);
                    sqlMap.executeUpdate("deleteError_showing", errorMap);
                    sqlMap.commitTransaction();
                    paramMap.put(CommonConstants.BRANCH_ID, branch);
                    try {

//        List outputList = ClientUtil.executeQuery("getTransferTransDayEnd", paramMap);
//        checkAndAddKeys(outputList) ;
//        
//        outputList = ClientUtil.executeQuery("getCashTransDayEnd", paramMap);
//        checkAndAddKeys(outputList) ;
//        
//        outputList = ClientUtil.executeQuery("getCSHANDTransDayEnd", paramMap);
//        checkAndAddKeys(outputList) ;

                        List outputList = ServerUtil.executeQuery("getAccBalancesDayEnd", paramMap);
                        addToFinalList(outputList);
                        checkWithGL();

                        outputList = ServerUtil.executeQuery("getInwardClgDayEnd", paramMap);
                        checkAndAddKeys(outputList);

                        outputList = ServerUtil.executeQuery("getOutwardClgDayEnd", paramMap);
                        checkAndAddKeys(outputList);

                        System.out.println(finalMap);
                        outputList = ServerUtil.executeQuery("selectTotalGlDayEnd", paramMap);
                        HashMap tempMap = null;
                        double amt = 0;
                        double tempAmt = 0;
                        for (int i = 0, j = outputList.size(); i < j; i++) {
                            tempMap = (HashMap) outputList.get(i);
                            amt = 0;
                            tempAmt = 0;
                            tempMap.put(TRANS_AMT, "0");
                            if (finalMap.containsKey(tempMap.get(AC_HD_ID))) {
                                tempMap.put(TRANS_AMT, finalMap.get(tempMap.get(AC_HD_ID)));
                                tempAmt = +CommonUtil.convertObjToDouble(finalMap.get(tempMap.get(AC_HD_ID))).doubleValue();

                                amt =
                                        CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue()
                                        - (CommonUtil.convertObjToDouble(tempMap.get(OPN_BAL)).doubleValue());

                                if ((tempMap.get(BALANCE_TYPE).equals(CommonConstants.CREDIT) && tempAmt > 0)) {
                                    amt += ((-1) * tempAmt);
                                } else {
                                    amt += tempAmt;
                                }
                                //            } else {
                                //                amt = 
                                //                    CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue()    
                                //                    - CommonUtil.convertObjToDouble(tempMap.get(OPN_BAL)).doubleValue();
                            }

                            if (amt != 0) {
                                finalList.add(tempMap);
                            }
                        }
                    } catch (Exception e) {
//                    sqlMap.rollbackTransaction();
//                    e.printStackTrace();
//                    throw new TransRollbackException(e);
                        sqlMap.rollbackTransaction();
//                              status.setStatus(BatchConstants.ERROR) ;
//                              errorMap = new HashMap();
//                              errorMap.put("ERROR_DATE",currDt);
//                              errorMap.put("TASK_NAME", taskLable);
//                              errorMap.put("ERROR_MSG",e.getMessage());
//                              errorMap.put("BRANCH_ID", branch);
//                              errorMap.put("ACT_NUM","GL");
//                              sqlMap.startTransaction();
//                              sqlMap.executeUpdate("insertError_showing", errorMap);
//                              sqlMap.commitTransaction();
//                              System.out.println("Error thrown for Operative A/c No " + dataMap.get("ACT_NUM"));
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
                                            errorMap.put("ERROR_DATE", currDt);
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
                                    errorMap.put("ERROR_DATE", currDt);
                                    errorMap.put("TASK_NAME", taskLable);
                                    errorMap.put("ERROR_MSG", errMsg);
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
                            errorMap.put("ERROR_DATE", currDt);
                            errorMap.put("TASK_NAME", taskLable);
                            errorMap.put("ERROR_MSG", errMsg);
                            errorMap.put("ERROR_CLASS", errClassName);
                            errorMap.put("ACT_NUM", "GL");
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
                    }
                }
            }
        }


        if (finalList.size() > 0) {
            status.setStatus(BatchConstants.ERROR);
        } else {
            status.setStatus(BatchConstants.COMPLETED);
        }

        return status;
//        System.out.println("error list : " + finalList);

//        StringBuffer out = CommonUtil.createHTML(finalList, true, true);
//        if (out.toString().length() == 0 ) {
//            out = CommonUtil.createHTMLNoData();
//        }
//        return out.toString() ;
    }

    public static void main(String args[]) {
        try {
//            OABalanceCheckTask ft = new OABalanceCheckTask() ;
//            System.out.println (ft.executeTask());
        } catch (Exception E) {
            E.printStackTrace();
        }
    }
}
