/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * BalanceCheckTask.java
 *
 * Created on February 28, 2005, 12:23 PM
 */
package com.see.truetransact.serverside.batchprocess.task.gl;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author 152691 This class checks if GL_opening_bal + cr -dr = GL_closing_bal
 * Is to be called as a part of the Day End batch process
 */
public class BalanceCheckTask extends Task {

    private static SqlMap sqlMap = null;
    private String BRANCH_CODE = "BRANCH_CODE";
    private String TODAY_DT = "TODAY_DT";
    private String AC_HD_ID = "AC_HD_ID";
    private String CLOSE_BAL = "CLOSE_BAL";
    private String OPN_BAL = "OPN_BAL";
    private String TRANS_AMT = "TRANS_AMT";
    private String BALANCE_TYPE = "BALANCE_TYPE";
    private String BALANCE = "BALANCE";
    private String branch = null;
    private String process = null;
    HashMap finalMap = new HashMap();
    ArrayList finalList = new ArrayList();
    private Date currDt = null;
    private String dayEndType;
    private List branchList;
    private HashMap paramMap;
    private String taskLable;

    /**
     * Creates a new instance of BalanceCheckTask
     */
    public BalanceCheckTask(TaskHeader header) throws Exception {
        setHeader(header);
        process = header.getProcessType();
        branch = header.getBranchID();
        initializeTaskObj(header.getTaskParam());
    }

    private void initializeTaskObj(HashMap dataMap) throws Exception {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        currDt = ServerUtil.getCurrentDate(super._branchCode);
        paramMap = getHeader().getTaskParam();
        if (paramMap != null && paramMap.containsKey("DAY_END_TYPE")) {
            dayEndType = CommonUtil.convertObjToStr(paramMap.get("DAY_END_TYPE"));
        }
        if (CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
            if (paramMap.containsKey("BRANCH_LST")) {
                branchList = (List) paramMap.get("BRANCH_LST");
                System.out.println("branchList*****" + branchList);
            } else {
                HashMap tempMap = new HashMap();
                tempMap.put("NEXT_DATE", currDt);
                tempMap.put("BRANCH_DAY_END_STATUS", "COMPLETED");
                tempMap.put("DC_DAY_END_STATUS", "COMPLETED");
//            branchList=(List)sqlMap.executeQueryForList("getAllBranchesFromDayEnd",tempMap);
                branchList = (List) sqlMap.executeQueryForList("getSelectBranchCompletedLst", tempMap);
                tempMap = null;
            }
        } else {
            HashMap tempMap = new HashMap();
            tempMap.put(CommonConstants.BRANCH_ID, getHeader().getBranchID());
            branchList = new ArrayList();
            branchList.add(tempMap);
            tempMap = null;
        }
        if (paramMap != null && paramMap.containsKey("BAL_CHK_TASK_LABLE")) {
            taskLable = CommonUtil.convertObjToStr(paramMap.get("BAL_CHK_TASK_LABLE"));
        }
    }

    private void checkAndAddKeys(List outputList) {
        HashMap tempMap = null;
        double amt = 0;
        Double objAmt = null;
        if (outputList != null && outputList.size() > 0) {
            for (int i = 0, j = outputList.size(); i < j; i++) {
                tempMap = (HashMap) outputList.get(i);
                objAmt = CommonUtil.convertObjToDouble(tempMap.get(CommonConstants.DEBIT));
                amt = (-1) * objAmt.doubleValue();
                objAmt = CommonUtil.convertObjToDouble(tempMap.get(CommonConstants.CREDIT));
                amt = amt + objAmt.doubleValue();

                if (finalMap.containsKey(tempMap.get(AC_HD_ID))) {
                    amt = amt
                            + CommonUtil.convertObjToDouble(finalMap.get(tempMap.get(AC_HD_ID))).doubleValue();
                }

                finalMap.put(tempMap.get(AC_HD_ID), new Double(amt));
                tempMap = null;
                objAmt = null;
                amt = 0;
            }
        }
    }

    private void addToFinalList(List outputList) {
        HashMap tempMap = null;
        for (int i = 0, j = outputList.size(); i < j; i++) {
            tempMap = (HashMap) outputList.get(i);
            finalMap.put(tempMap.get(AC_HD_ID), tempMap.get(BALANCE));
            tempMap = null;
        }
    }

    private void checkWithGL() throws Exception {
        HashMap tempMap = null;
        double glAmt = 0;
        double acHdAmt = 0;
        HashMap paramMap = new HashMap();
        paramMap.put(BRANCH_CODE, branch);
        paramMap.put(TODAY_DT, ServerUtil.getCurrentDate(super._branchCode));
        List outputList = sqlMap.executeQueryForList("selectTotalGlDayEnd", paramMap);
        System.out.println("outputList : " + outputList);
        for (int i = 0, j = outputList.size(); i < j; i++) {
            tempMap = (HashMap) outputList.get(i);
            System.out.println("tempMap : " + tempMap);
            System.out.println("finalMap : " + finalMap);
            if (finalMap.containsKey(tempMap.get(AC_HD_ID))) {
                glAmt = CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue();
                acHdAmt = CommonUtil.convertObjToDouble(finalMap.get(tempMap.get(AC_HD_ID))).doubleValue();
                System.out.println("glAmt : " + glAmt);
                System.out.println("acHdAmt : " + acHdAmt);
                tempMap.put(TRANS_AMT, new Double(acHdAmt - glAmt));
                if (glAmt != acHdAmt) {
                    finalList.add(tempMap);
                }
            }
        }
        finalMap = new HashMap();
    }

    public TaskStatus executeTask() throws Exception {
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
        TaskStatus status = new TaskStatus();
        status.setStatus(BatchConstants.STARTED);
        paramMap.put(TODAY_DT, currDt);
        System.out.println("ServerUtil.getCurrentDate(super._branchCode) : " + currDt);
        finalList = new ArrayList();

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
                    try {
                        paramMap.put(CommonConstants.BRANCH_ID, branch);

                        List outputList = sqlMap.executeQueryForList("getTransferTransDayEnd", paramMap);
                        checkAndAddKeys(outputList);

                        outputList = sqlMap.executeQueryForList("getCashTransDayEnd", paramMap);
                        checkAndAddKeys(outputList);

                        outputList = sqlMap.executeQueryForList("getCSHANDTransDayEnd", paramMap);
                        checkAndAddKeys(outputList);

                        outputList = sqlMap.executeQueryForList("getInwardClgDayEnd", paramMap);
                        checkAndAddKeys(outputList);

                        outputList = sqlMap.executeQueryForList("getOutwardClgDayEnd", paramMap);
                        checkAndAddKeys(outputList);

                        outputList = sqlMap.executeQueryForList("selectTotalGlDayEnd", paramMap);
                        System.out.println("outputList : " + outputList);
                        HashMap tempMap = null;
                        double amt = 0;
                        double tempAmt = 0;
                        System.out.println("finalList=null;" + finalList);
                        for (int i = 0, j = outputList.size(); i < j; i++) {
                            tempMap = (HashMap) outputList.get(i);
                            System.out.println("tempMap : " + tempMap);
                            amt = 0;
                            tempAmt = 0;
                            tempMap.put(TRANS_AMT, "0");
                            System.out.println("finalMap : " + finalMap);
                            if (finalMap.containsKey(tempMap.get(AC_HD_ID))) {
                                System.out.println("insid Checking " + tempMap.get(AC_HD_ID));
                                tempMap.put(TRANS_AMT, finalMap.get(tempMap.get(AC_HD_ID)));
                                tempAmt = CommonUtil.convertObjToDouble(finalMap.get(tempMap.get(AC_HD_ID))).doubleValue();

                                if (CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue() > 0 && CommonUtil.convertObjToDouble(tempMap.get(OPN_BAL)).doubleValue() > 0
                                        && CommonUtil.convertObjToStr(tempMap.get("BALANCE_TYPE")).equals(CommonUtil.convertObjToStr(tempMap.get("OPENBALANCE_TYPE")))) {

                                    System.out.print("both balancetype is equal and balance>0");
                                    amt = CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue()
                                            - (CommonUtil.convertObjToDouble(tempMap.get(OPN_BAL)).doubleValue());
                                    amt = roundOff((long) (amt * 1000));
                                    amt = amt / 100.0;
                                    System.out.println("amt###########" + amt);
                                    System.out.println("tempAmt|||||**********" + tempAmt);


                                    if ((tempMap.get(BALANCE_TYPE).equals(CommonConstants.CREDIT) && tempAmt > 0)) {
                                        amt += ((-1) * tempAmt);
                                    } else if (tempMap.get(BALANCE_TYPE).equals(CommonConstants.CREDIT) && amt < 0) {
                                        amt = amt * -1;
                                        amt += tempAmt;
                                    } else {
                                        amt += tempAmt;
                                    }
                                } else if ((CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue() == 0 || CommonUtil.convertObjToDouble(tempMap.get(OPN_BAL)).doubleValue() == 0)
                                        && (CommonUtil.convertObjToStr(tempMap.get("BALANCE_TYPE")).equals("") || CommonUtil.convertObjToStr(tempMap.get("OPENBALANCE_TYPE")).equals(""))) {
                                    amt = CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue()
                                            - (CommonUtil.convertObjToDouble(tempMap.get(OPN_BAL)).doubleValue());
                                    amt = roundOff((long) (amt * 1000));
                                    amt = amt / 100.0;
                                    System.out.println("amt###########" + amt);
                                    System.out.println("tempAmt|||||**********" + tempAmt);
                                    System.out.print("different and amount 0");

                                    if ((tempMap.get(BALANCE_TYPE).equals(CommonConstants.CREDIT) && tempAmt > 0)) {
                                        amt += ((-1) * tempAmt);
                                    } else if (tempMap.get(BALANCE_TYPE).equals(CommonConstants.CREDIT) && amt < 0) {
                                        amt = amt * -1;
                                        amt += tempAmt;
                                    } else {
                                        amt += tempAmt;
                                    }
                                } else if (!CommonUtil.convertObjToStr(tempMap.get("BALANCE_TYPE")).equals(CommonUtil.convertObjToStr(tempMap.get("OPENBALANCE_TYPE")))
                                        && CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue() > 0 && CommonUtil.convertObjToDouble(tempMap.get(OPN_BAL)).doubleValue() > 0) {
                                    System.out.print("both balancetype not  equal ");
                                    if (tempAmt < 0) {
                                        tempAmt = tempAmt * -1;
                                    }
                                    amt = CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue()
                                            + (CommonUtil.convertObjToDouble(tempMap.get(OPN_BAL)).doubleValue());
                                    amt = roundOff((long) (amt * 1000));
                                    amt = amt / 100.0;

                                    amt = amt - tempAmt;
                                } else if (CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue() == 0 && CommonUtil.convertObjToDouble(tempMap.get(OPN_BAL)).doubleValue() == 0
                                        && CommonUtil.convertObjToStr(tempMap.get("BALANCE_TYPE")).equals(CommonUtil.convertObjToStr(tempMap.get("OPENBALANCE_TYPE")))) {
                                    amt = CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue()
                                            - (CommonUtil.convertObjToDouble(tempMap.get(OPN_BAL)).doubleValue());
                                    amt = tempAmt - amt;
                                } else if (CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue() == 0 && CommonUtil.convertObjToDouble(tempMap.get(OPN_BAL)).doubleValue() == 0
                                        && !CommonUtil.convertObjToStr(tempMap.get("BALANCE_TYPE")).equals(CommonUtil.convertObjToStr(tempMap.get("OPENBALANCE_TYPE")))) {
                                    amt = CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue()
                                            - (CommonUtil.convertObjToDouble(tempMap.get(OPN_BAL)).doubleValue());
                                    amt = tempAmt - amt;
                                } else {
                                    amt = 0.0;
                                }


                                System.out.println("amt###########" + amt);
                                System.out.println("tempAmt|||||**********" + tempAmt);
                                //            } else {
                                //                amt =
                                //                    CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue()
                                //                    - CommonUtil.convertObjToDouble(tempMap.get(OPN_BAL)).doubleValue();
                            }
                            System.out.println("amt###########" + amt);
                            if (amt < 0) {
                                amt = amt * -1;
                            }
                            if (Math.abs(amt) > .1) {
                                finalList.add(tempMap);
                            }
                            //            if(amt!=0.0)
                            //                finalList.add(tempMap) ;
                            //            }
                        }
                    } catch (Exception e) {
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
                                            errorMap.put("ACT_NUM", "bal chk");
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
                                    errorMap.put("ACT_NUM", "bal chk");
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
                            errorMap.put("ACT_NUM", "bal chk");
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
        System.out.println("#### Final List : " + finalList);
        if (finalList.size() > 0) {
            status.setStatus(BatchConstants.ERROR);
        } else {
            status.setStatus(BatchConstants.COMPLETED);
        }

        return status;
    }

    private long roundOff(long amt) {
        long amount = amt / 10;
        int lastDigit = (int) amt % 10;
        if (lastDigit > 5) {
            amount++;
        }
        return amount;
    }

    public static void main(String arg[]) {
        try {
            TaskHeader header = new TaskHeader();
            header.setTaskClass("BalanceCheckTask");
            HashMap paramMap = new HashMap();

            //header.setProcessType(CommonConstants.DAY_BEGIN);
            header.setProcessType(CommonConstants.DAY_END);

            header.setBranchID("ABC50002");
            header.setTaskParam(paramMap);
            BalanceCheckTask tsk = new BalanceCheckTask(header);
            TaskStatus status = tsk.executeTask();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
