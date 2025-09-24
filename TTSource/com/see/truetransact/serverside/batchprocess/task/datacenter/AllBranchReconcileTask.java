/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AllBranchReconcileTask.java
 *
 * Created on February 28, 2005, 12:23 PM
 */
package com.see.truetransact.serverside.batchprocess.task.datacenter;

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
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author 152691 This class checks if GL_opening_bal + cr -dr = GL_closing_bal
 * Is to be called as a part of the Day End batch process
 */
public class AllBranchReconcileTask extends Task {

    private static SqlMap sqlMap = null;
    private String BRANCH_CODE = "BRANCH_CODE";
    private String TODAY_DT = "TODAY_DT";
    private String AC_HD_ID = "AC_HD_ID";
    private String CLOSE_BAL = "CLOSE_BAL";
    private String OPN_BAL = "OPN_BAL";
    private String TRANS_AMT = "TRANS_AMT";
    private String BALANCE_TYPE = "BALANCE_TYPE";
    private String branch = null;
    private String process = null;
    HashMap finalMap = new HashMap();
    private String dayEndType;
    private HashMap taskParamMap;
    private Date currDt;
    private List branchList;

    /**
     * Creates a new instance of BalanceCheckTask
     */
    public AllBranchReconcileTask(TaskHeader header) throws Exception {
        setHeader(header);
        process = header.getProcessType();
        branch = header.getBranchID();
        initializeTaskObj(header.getTaskParam());
    }

    private void initializeTaskObj(HashMap dataMap) throws Exception {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        currDt = ServerUtil.getCurrentDate(branch);
        taskParamMap = dataMap;
        if (taskParamMap != null && taskParamMap.containsKey("DAY_END_TYPE")) {
            dayEndType = CommonUtil.convertObjToStr(taskParamMap.get("DAY_END_TYPE"));
        }
        if (CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
            HashMap tempMap = new HashMap();
            tempMap.put("NEXT_DATE", currDt);
            branchList = (List) sqlMap.executeQueryForList("getAllBranchesFromDayEnd", tempMap);
            tempMap = null;
        } else {
            HashMap tempMap = new HashMap();
            tempMap.put(CommonConstants.BRANCH_ID, getHeader().getBranchID());
            branchList = new ArrayList();
            branchList.add(tempMap);
            tempMap = null;
        }
    }

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
        HashMap paramMap = new HashMap();
        paramMap.put(TODAY_DT, currDt);

        ArrayList finalList = new ArrayList();
        if (branchList != null && branchList.size() > 0) {
            for (int b = 0; b < branchList.size(); b++) {
                HashMap branchMap = (HashMap) branchList.get(b);
                branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                paramMap.put(BRANCH_CODE, branch);
                //        System.out.println("ServerUtil.getCurrentDate(super._branchCode) : " + ServerUtil.getCurrentDate(super._branchCode));

                List outputList = sqlMap.executeQueryForList("getTransferTransDayEnd", paramMap);
                checkAndAddKeys(outputList);

                outputList = sqlMap.executeQueryForList("getCashTransDayEnd", paramMap);
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
                for (int i = 0, j = outputList.size(); i < j; i++) {
                    tempMap = (HashMap) outputList.get(i);
                    System.out.println("tempMap : " + tempMap);
                    amt = 0;
                    tempAmt = 0;
                    tempMap.put(TRANS_AMT, "0");
                    System.out.println("finalMap : " + finalMap);
                    if (finalMap.containsKey(tempMap.get(AC_HD_ID))) {
                        tempMap.put(TRANS_AMT, finalMap.get(tempMap.get(AC_HD_ID)));
                        tempAmt = CommonUtil.convertObjToDouble(finalMap.get(tempMap.get(AC_HD_ID))).doubleValue();

                        amt = CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue()
                                - (CommonUtil.convertObjToDouble(tempMap.get(OPN_BAL)).doubleValue());

                        if ((tempMap.get(BALANCE_TYPE).equals(CommonConstants.CREDIT) && tempAmt > 0)) {
                            amt += ((-1) * tempAmt);
                        } else {
                            amt += tempAmt;
                        }

                    } else {
                        amt =
                                CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue()
                                - CommonUtil.convertObjToDouble(tempMap.get(OPN_BAL)).doubleValue();
                    }

                    if (amt != 0) {
                        finalList.add(tempMap);
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
    }

    public static void main(String arg[]) {
        try {
            TaskHeader header = new TaskHeader();
            header.setTaskClass("AllBranchReconcileTask");
            HashMap paramMap = new HashMap();

            //header.setProcessType(CommonConstants.DAY_BEGIN);
            header.setProcessType(CommonConstants.DAY_END);

            header.setBranchID("ABC50002");
            header.setTaskParam(paramMap);
            AllBranchReconcileTask tsk = new AllBranchReconcileTask(header);
            TaskStatus status = tsk.executeTask();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
