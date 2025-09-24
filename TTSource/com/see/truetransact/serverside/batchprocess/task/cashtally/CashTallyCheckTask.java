/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * CashTallyCheckTask.java
 *
 * Created on February 28, 2005, 12:23 PM
 */
package com.see.truetransact.serverside.batchprocess.task.cashtally;

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
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
/**
 *
 * @author 152691 This class checks if cash transaction from Vault and teller
 * tally Is to be called as a part of the Day End batch process
 */
public class CashTallyCheckTask extends Task {

    private static SqlMap sqlMap = null;
    private String branch = null;
    private String process = null;
    HashMap vaultMap = new HashMap();
    private Date currDt = null;
    /**
     * Creates a new instance of CashTallyCheckTask
     */
    public CashTallyCheckTask(TaskHeader header) throws Exception {
        setHeader(header);
        process = header.getProcessType();
        branch = header.getBranchID();
        initializeTaskObj(header.getTaskParam());
    }

    private void initializeTaskObj(HashMap dataMap) throws Exception {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();

    }

    public TaskStatus executeTask() throws Exception {
        /**
         * Formaula to be employed Vault sum(payment) + Cash sum(receipts) -
         * Cash sum(payments) = Vault sum(receipt)
         *
         */
        currDt = ServerUtil.getCurrentDate(super._branchCode);
        TaskStatus status = new TaskStatus();
        status.setStatus(BatchConstants.STARTED);
        System.out.println(status.getStatus());
        HashMap dataMap = new HashMap();
        HashMap paramMap = new HashMap();
        paramMap.put("BRANCH_CODE", branch);
        paramMap.put("TODAY_DT", currDt.clone());

        ArrayList finalList = new ArrayList();
        HashMap tempMap = null;
        HashMap hashData = null;
        List outputList = null;
        List lst = null;
        List lstData = null;
        HashMap data = null;
        List crDrList = null;
        double amt = 0;
        double glcurbal = 0;
        double openingCount = 0;

        lst = sqlMap.executeQueryForList("getCashMovementDetails", paramMap);
        //        System.out.println("@@@@@lst"+lst);
        if (lst != null) {
            if (lst.size() > 0) {
                for (int a = 0, b = lst.size(); a < b; a++) {

                    String receivID = "";
                    data = (HashMap) lst.get(a);
                    paramMap.put("USER_ID", data.get("RECEIVED_CASHIER_ID"));
                    //                    System.out.println("@@@@@paramMap****"+paramMap);
                    outputList = sqlMap.executeQueryForList("getVaultCheckTally", paramMap);
                    crDrList = sqlMap.executeQueryForList("getcrDrCheckTally", paramMap);
                    //                    System.out.println("@@@@@outputList"+outputList);
                    for (int i = 0, j = outputList.size(); i < j; i++) {

                        tempMap = (HashMap) outputList.get(i);
                        System.out.println("tempMap****^^*" + tempMap);
                        amt = amt + CommonUtil.convertObjToDouble(tempMap.get("DENOMINATION_TOTAL")).doubleValue();



//                            receivID = CommonUtil.convertObjToStr(vaultMap.get("RECEIVED_CASHIER_ID"));
//                            if(vaultMap.containsKey("RECEIVED_CASHIER_ID") && vaultMap.containsValue(receivID))
//                                amt = amt + CommonUtil.convertObjToDouble(vaultMap.get("DENOMINATION_TOTAL")).doubleValue();


                        //                        System.out.println("%%%%vaultMap"+vaultMap);
                    }

                    if (crDrList != null && crDrList.size() > 0) {
                        double cramt = 0.0;
                        double dramt = 0.0;

                        for (int i = 0; i < crDrList.size(); i++) {
                            HashMap crdrmap = (HashMap) crDrList.get(i);
                            if (crdrmap.get("TRANS_TYPE").equals("CREDIT")) {
                                cramt = cramt + CommonUtil.convertObjToDouble(crdrmap.get("INP_AMOUNT")).doubleValue();

                            }
                            if (crdrmap.get("TRANS_TYPE").equals("DEBIT")) {
                                dramt = dramt + CommonUtil.convertObjToDouble(crdrmap.get("INP_AMOUNT")).doubleValue();
                            }

                        }
                        amt = amt + (cramt - dramt);
                    }
                    System.out.println("%%%%amt" + amt);
                    System.out.println("%%%%data" + data);
                    if (amt == 0) {
                    } else {
                        vaultMap.put("RECEIVED_CASHIER_ID", data.get("RECEIVED_CASHIER_ID"));
                        vaultMap.put("DENOMINATION_TOTAL", new Double(amt));
                    }

                }
                //                System.out.println("vaultMap"+vaultMap);
                if (vaultMap.size() > 0) {
                    status.setStatus(BatchConstants.ERROR);
                    System.out.println("Completion Status : " + status.getStatus());
                } else {
                    lst = sqlMap.executeQueryForList("getDenoValueCount", paramMap);
                    lstData = sqlMap.executeQueryForList("getCurbalGl", paramMap);
                    dataMap = (HashMap) lst.get(0);
                    if ((dataMap.get("OPENING_COUNT") != null)) {
                        if (lst.size() > 0 && lstData.size() > 0) {

                            openingCount = CommonUtil.convertObjToDouble(dataMap.get("OPENING_COUNT")).doubleValue();
                            System.out.println("openingCount" + openingCount);
                            hashData = (HashMap) lstData.get(0);
                            glcurbal = CommonUtil.convertObjToDouble(hashData.get("CUR_BAL")).doubleValue();
                            System.out.println("glcurbal" + glcurbal);

                            if (openingCount == glcurbal && vaultMap.size() <= 0) {
                                status.setStatus(BatchConstants.COMPLETED);
                            } else {
                                status.setStatus(BatchConstants.ERROR);
                            }
                        } else {
                            status.setStatus(BatchConstants.COMPLETED);//STATUS COMPLETED
                        }
                    } else {
                        status.setStatus(BatchConstants.COMPLETED);//STATUS COMPLETED
                    }

                }
                /*outputList = sqlMap.executeQueryForList("getVaultCashTallyAmount", paramMap);
                 for(int i = 0, j = outputList.size() ; i < j ; i++){
                 amt = 0 ;
                 tempMap = (HashMap)outputList.get(i);
                 amt = CommonUtil.convertObjToDouble(tempMap.get("VAULT_RECEIPT")).doubleValue()
                 + CommonUtil.convertObjToDouble(tempMap.get("CASH_BOX_BALANCE")).doubleValue()
                 - CommonUtil.convertObjToDouble(tempMap.get("VAULT_PAYMENT")).doubleValue() ;
       
                 vaultMap.put(tempMap.get("RECEIVED_CASHIER_ID"), new Double(amt)) ;
                 }
                 outputList = sqlMap.executeQueryForList("getCashMovementTallyAmount", paramMap);
       
                 for(int k = 0, l = outputList.size() ; k < l ; k++){
                 amt =0 ;
                 tempMap = (HashMap)outputList.get(k);
                 amt =
                 CommonUtil.convertObjToDouble(tempMap.get("CREDIT")).doubleValue()
                 - CommonUtil.convertObjToDouble(tempMap.get("DEBIT")).doubleValue() ;
       
                 if(vaultMap.containsKey(tempMap.get("INIT_TRANS_ID")))
                 amt = amt + CommonUtil.convertObjToDouble(vaultMap.get(tempMap.get("INIT_TRANS_ID"))).doubleValue() ;
       
                 vaultMap.put(tempMap.get("INIT_TRANS_ID"), new Double(amt)) ;
                 }
       
                 Iterator vaultKeys = vaultMap.keySet().iterator();
                 String key = null ;
                 while (vaultKeys.hasNext()){
                 key = (String)vaultKeys.next();
                 if(CommonUtil.convertObjToDouble(vaultMap.get(key)).doubleValue() == 0)
                 vaultMap.remove(key);
                 }
       
                 if(vaultMap.size() > 0 )
                 status.setStatus(BatchConstants.ERROR) ;
                 else
                 status.setStatus(BatchConstants.COMPLETED);
       
                 System.out.println("Completion Status : " + status.getStatus());
                 return status;*/

            } else {
                status.setStatus(BatchConstants.COMPLETED);
            }
        }
        System.out.println("Completion Status : " + status.getStatus());
        return status;
    }

    public static void main(String arg[]) {
        try {
            TaskHeader header = new TaskHeader();
            header.setTaskClass("CashTallyCheckTask");
            HashMap paramMap = new HashMap();
            header.setProcessType(CommonConstants.DAY_END);
            header.setBranchID(CommonConstants.BRANCH_ID);
            header.setTaskParam(paramMap);
            CashTallyCheckTask tsk = new CashTallyCheckTask(header);
            TaskStatus status = tsk.executeTask();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
