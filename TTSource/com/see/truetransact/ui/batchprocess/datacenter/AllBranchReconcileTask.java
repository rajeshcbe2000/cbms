/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AllBranchReconcileTask.java
 *
 * Created on February 28, 2005, 12:23 PM
 */

package com.see.truetransact.ui.batchprocess.datacenter;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil ;
import com.see.truetransact.commonutil.DateUtil ;
import com.see.truetransact.clientutil.ClientUtil ;
import com.see.truetransact.commonutil.TTException ;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Iterator;
import java.util.Date;
import com.see.truetransact.ui.TrueTransactMain;

/**
 *
 * @author  152691
 * This class checks if GL_opening_bal + cr -dr = GL_closing_bal
 * Is to be called as a part of the Day End batch process
 */
public class AllBranchReconcileTask{
    private String BRANCH_CODE = "BRANCH_CODE" ;
    private String TODAY_DT = "TODAY_DT" ;
    private String AC_HD_ID = "AC_HD_ID" ;
    private String CLOSE_BAL = "CLOSE_BAL" ;
    private String OPN_BAL = "OPN_BAL" ;
    private String TRANS_AMT = "TRANS_AMT" ;
    private String BALANCE_TYPE = "BALANCE_TYPE";
    
    private String branch = null ;
    private String process = null ;
    HashMap finalMap = new HashMap();
    private String dayEndType;
    private HashMap taskParamMap;
    private Date currDt;
    private List branchList;
    
    /** Creates a new instance of BalanceCheckTask */
    public AllBranchReconcileTask() throws Exception {
        currDt = ClientUtil.getCurrentDate();
        HashMap tempMap = new HashMap();
        tempMap.put("NEXT_DATE", currDt);
        branchList=(List)ClientUtil.executeQuery("getAllBranchesFromDayEnd",tempMap);
        tempMap = null;
    }
    
    private void checkAndAddKeys(List outputList){
        HashMap tempMap = null ;
        double amt = 0 ;
        Double objAmt = null ;
        for(int i = 0, j = outputList.size() ; i < j ; i++){
            tempMap = (HashMap)outputList.get(i);
            objAmt = CommonUtil.convertObjToDouble(tempMap.get(CommonConstants.DEBIT)) ;
            amt = (-1) * objAmt.doubleValue();
            objAmt = CommonUtil.convertObjToDouble(tempMap.get(CommonConstants.CREDIT)) ;
            amt = amt + objAmt.doubleValue();
            
            if(finalMap.containsKey(tempMap.get(AC_HD_ID))){
                amt = amt 
                    + CommonUtil.convertObjToDouble(finalMap.get(tempMap.get(AC_HD_ID))).doubleValue() ;
            }
            
            finalMap.put(tempMap.get(AC_HD_ID), new Double(amt));
            tempMap = null ;
            objAmt = null ;
            amt = 0 ;
        }
    }
    
    public String executeTask() throws Exception {
        /** What this method does :
         * Executes 5 queries. The output of each query is added as a key value pair
         * in a HashMap. Key is each AC_HD_ID, value is Amount. 
         * Each time the query executes it checks if the key exists in the finalMap
         * If key exists, amt is updated. Dr amounts are multiplied by -1 and added to existing amt.
         * Cr amounts are added as is.
         * The last query totals the amount for each account head against opening and
         * closing balance in GL_Abstract. Hence the pre requesite is that 
         * the GL_Abstartct insert task SHOULD be executed before this code is run.
         */
        HashMap paramMap = new HashMap();
        paramMap.put(TODAY_DT, currDt);

        StringBuffer out = new StringBuffer("");
        
        ArrayList finalList = new ArrayList();
        String oldBranch="";
        if (branchList!=null && branchList.size()>0) {
            for (int b=0;b<branchList.size();b++) {
                HashMap branchMap = (HashMap) branchList.get(b);
                branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                paramMap.put(BRANCH_CODE, branch);
        //        System.out.println("ServerUtil.getCurrentDate(super._branchCode) : " + ServerUtil.getCurrentDate(super._branchCode));

                List outputList = ClientUtil.executeQuery("getTransferTransDayEnd", paramMap);
                checkAndAddKeys(outputList) ;

                outputList = ClientUtil.executeQuery("getCashTransDayEnd", paramMap);
                checkAndAddKeys(outputList) ;

                outputList = ClientUtil.executeQuery("getInwardClgDayEnd", paramMap);
                checkAndAddKeys(outputList) ;

                outputList = ClientUtil.executeQuery("getOutwardClgDayEnd", paramMap);
                checkAndAddKeys(outputList) ;

                outputList = ClientUtil.executeQuery("selectTotalGlDayEnd", paramMap);
                System.out.println("outputList : " + outputList);
                HashMap tempMap = null ;
                double amt = 0 ;
                double tempAmt = 0 ;
                for(int i = 0, j = outputList.size() ; i < j ; i++){
                    tempMap = (HashMap)outputList.get(i);
                    System.out.println("tempMap : " + tempMap);
                    amt = 0 ;
                    tempAmt = 0 ;
                    tempMap.put(TRANS_AMT, "0");
                    System.out.println("finalMap : " + finalMap);
                    if(finalMap.containsKey(tempMap.get(AC_HD_ID))){
                        tempMap.put(TRANS_AMT, finalMap.get(tempMap.get(AC_HD_ID))) ;
                        tempAmt = CommonUtil.convertObjToDouble(finalMap.get(tempMap.get(AC_HD_ID))).doubleValue() ;

                        amt = CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue()    
                            - (CommonUtil.convertObjToDouble(tempMap.get(OPN_BAL)).doubleValue()) ;

                        if((tempMap.get(BALANCE_TYPE).equals(CommonConstants.CREDIT) && tempAmt > 0))
                            amt+= ((-1) * tempAmt) ;
                        else
                            amt+= tempAmt ;

                    } else {
                        amt = 
                            CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue()    
                            - CommonUtil.convertObjToDouble(tempMap.get(OPN_BAL)).doubleValue();
                    }

                    if(amt != 0) {
                        if (!oldBranch.equals(branch)) {
                            if (oldBranch.length()>0) {
                                out.append(CommonUtil.createHTML(finalList, true, false));
                                finalList = new ArrayList();
                            }
                            HashMap branchHeadMap = new HashMap();
                            branchHeadMap.put("BRANCH_ID", branch);
                            ArrayList tempList = new ArrayList();
                            tempList.add(branchHeadMap);
                            out.append(CommonUtil.createHTML(tempList, true, false));
                        }
                        finalList.add(tempMap) ;
                        oldBranch = new String(branch);
                    }
                }
            }
        }
        
        out.append(CommonUtil.createHTML(finalList, true, true));
        if (out.toString().length() == 0 ) {
            out = CommonUtil.createHTMLNoData();
        }
        return out.toString() ;
    }
    public String executeTask(String lable) throws Exception {
        /** What this method does :
         * Executes 5 queries. The output of each query is added as a key value pair
         * in a HashMap. Key is each AC_HD_ID, value is Amount. 
         * Each time the query executes it checks if the key exists in the finalMap
         * If key exists, amt is updated. Dr amounts are multiplied by -1 and added to existing amt.
         * Cr amounts are added as is.
         * The last query totals the amount for each account head against opening and
         * closing balance in GL_Abstract. Hence the pre requesite is that 
         * the GL_Abstartct insert task SHOULD be executed before this code is run.
         */
        HashMap paramMap = new HashMap();
        paramMap.put(TODAY_DT, currDt);

        StringBuffer out = new StringBuffer("");
        
        ArrayList finalList = new ArrayList();
        String oldBranch="";
        if (branchList!=null && branchList.size()>0) {
            for (int b=0;b<branchList.size();b++) {
                HashMap branchMap = (HashMap) branchList.get(b);
                branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                paramMap.put(BRANCH_CODE, branch);
        //        System.out.println("ServerUtil.getCurrentDate(super._branchCode) : " + ServerUtil.getCurrentDate(super._branchCode));

                List outputList = ClientUtil.executeQuery("getTransferTransDayEnd", paramMap);
                checkAndAddKeys(outputList) ;

                outputList = ClientUtil.executeQuery("getCashTransDayEnd", paramMap);
                checkAndAddKeys(outputList) ;

                outputList = ClientUtil.executeQuery("getInwardClgDayEnd", paramMap);
                checkAndAddKeys(outputList) ;

                outputList = ClientUtil.executeQuery("getOutwardClgDayEnd", paramMap);
                checkAndAddKeys(outputList) ;

                outputList = ClientUtil.executeQuery("selectTotalGlDayEnd", paramMap);
                System.out.println("outputList : " + outputList);
                HashMap tempMap = null ;
                double amt = 0 ;
                double tempAmt = 0 ;
                for(int i = 0, j = outputList.size() ; i < j ; i++){
                    tempMap = (HashMap)outputList.get(i);
                    System.out.println("tempMap : " + tempMap);
                    amt = 0 ;
                    tempAmt = 0 ;
                    tempMap.put(TRANS_AMT, "0");
                    System.out.println("finalMap : " + finalMap);
                    if(finalMap.containsKey(tempMap.get(AC_HD_ID))){
                        tempMap.put(TRANS_AMT, finalMap.get(tempMap.get(AC_HD_ID))) ;
                        tempAmt = CommonUtil.convertObjToDouble(finalMap.get(tempMap.get(AC_HD_ID))).doubleValue() ;

                        amt = CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue()    
                            - (CommonUtil.convertObjToDouble(tempMap.get(OPN_BAL)).doubleValue()) ;

                        if((tempMap.get(BALANCE_TYPE).equals(CommonConstants.CREDIT) && tempAmt > 0))
                            amt+= ((-1) * tempAmt) ;
                        else
                            amt+= tempAmt ;

                    } else {
                        amt = 
                            CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue()    
                            - CommonUtil.convertObjToDouble(tempMap.get(OPN_BAL)).doubleValue();
                    }

                    if(amt != 0) {
                        if (!oldBranch.equals(branch)) {
                            if (oldBranch.length()>0) {
                                out.append(CommonUtil.createHTML(finalList, true, false));
                                finalList = new ArrayList();
                            }
                            HashMap branchHeadMap = new HashMap();
                            branchHeadMap.put("BRANCH_ID", branch);
                            ArrayList tempList = new ArrayList();
                            tempList.add(branchHeadMap);
                            out.append(CommonUtil.createHTML(tempList, true, false));
                        }
                        finalList.add(tempMap) ;
                        oldBranch = new String(branch);
                    }
                }
            }
        }
        
        out.append(CommonUtil.createHTML(finalList, true, true));
          HashMap dataMap = new HashMap();
        dataMap.put("ERROR_DATE", currDt.clone());
        dataMap.put("TASK_NAME", lable);
        List output = ClientUtil.executeQuery("getSelectError_showing", dataMap) ;
        dataMap = null;
        if(output!=null && output.size() > 0){
            String strData = "a";
            out.append(CommonUtil.createHTML(output,true,true,strData));
        }else{
            out.append(CommonUtil.createHTML(output,true,true));
        }
        if (out.toString().length() == 0 ) {
            out = CommonUtil.createHTMLNoData();
        }
        return out.toString() ;
    }
    
     public String executeTask(String lable,HashMap isPrev)throws Exception {
        /** What this method does :
         * Executes 5 queries. The output of each query is added as a key value pair
         * in a HashMap. Key is each AC_HD_ID, value is Amount. 
         * Each time the query executes it checks if the key exists in the finalMap
         * If key exists, amt is updated. Dr amounts are multiplied by -1 and added to existing amt.
         * Cr amounts are added as is.
         * The last query totals the amount for each account head against opening and
         * closing balance in GL_Abstract. Hence the pre requesite is that 
         * the GL_Abstartct insert task SHOULD be executed before this code is run.
         */
        HashMap paramMap = new HashMap();
        paramMap.put(TODAY_DT, currDt);

        StringBuffer out = new StringBuffer("");
        
        ArrayList finalList = new ArrayList();
        String oldBranch="";
        if (branchList!=null && branchList.size()>0) {
            for (int b=0;b<branchList.size();b++) {
                HashMap branchMap = (HashMap) branchList.get(b);
                branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                paramMap.put(BRANCH_CODE, branch);
        //        System.out.println("ServerUtil.getCurrentDate(super._branchCode) : " + ServerUtil.getCurrentDate(super._branchCode));

                List outputList = ClientUtil.executeQuery("getTransferTransDayEnd", paramMap);
                checkAndAddKeys(outputList) ;

                outputList = ClientUtil.executeQuery("getCashTransDayEnd", paramMap);
                checkAndAddKeys(outputList) ;

                outputList = ClientUtil.executeQuery("getInwardClgDayEnd", paramMap);
                checkAndAddKeys(outputList) ;

                outputList = ClientUtil.executeQuery("getOutwardClgDayEnd", paramMap);
                checkAndAddKeys(outputList) ;

                outputList = ClientUtil.executeQuery("selectTotalGlDayEnd", paramMap);
                System.out.println("outputList : " + outputList);
                HashMap tempMap = null ;
                double amt = 0 ;
                double tempAmt = 0 ;
                for(int i = 0, j = outputList.size() ; i < j ; i++){
                    tempMap = (HashMap)outputList.get(i);
                    System.out.println("tempMap : " + tempMap);
                    amt = 0 ;
                    tempAmt = 0 ;
                    tempMap.put(TRANS_AMT, "0");
                    System.out.println("finalMap : " + finalMap);
                    if(finalMap.containsKey(tempMap.get(AC_HD_ID))){
                        tempMap.put(TRANS_AMT, finalMap.get(tempMap.get(AC_HD_ID))) ;
                        tempAmt = CommonUtil.convertObjToDouble(finalMap.get(tempMap.get(AC_HD_ID))).doubleValue() ;

                        amt = CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue()    
                            - (CommonUtil.convertObjToDouble(tempMap.get(OPN_BAL)).doubleValue()) ;

                        if((tempMap.get(BALANCE_TYPE).equals(CommonConstants.CREDIT) && tempAmt > 0))
                            amt+= ((-1) * tempAmt) ;
                        else
                            amt+= tempAmt ;

                    } else {
                        amt = 
                            CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue()    
                            - CommonUtil.convertObjToDouble(tempMap.get(OPN_BAL)).doubleValue();
                    }

                    if(amt != 0) {
                        if (!oldBranch.equals(branch)) {
                            if (oldBranch.length()>0) {
                                out.append(CommonUtil.createHTML(finalList, true, false));
                                finalList = new ArrayList();
                            }
                            HashMap branchHeadMap = new HashMap();
                            branchHeadMap.put("BRANCH_ID", branch);
                            ArrayList tempList = new ArrayList();
                            tempList.add(branchHeadMap);
                            out.append(CommonUtil.createHTML(tempList, true, false));
                        }
                        finalList.add(tempMap) ;
                        oldBranch = new String(branch);
                    }
                }
            }
        }
        
        out.append(CommonUtil.createHTML(finalList, true, true));
          HashMap dataMap = new HashMap();
        dataMap.put("ERROR_DATE", currDt.clone());
        dataMap.put("TASK_NAME", lable);
        List output = ClientUtil.executeQuery("getSelectError_showing", dataMap) ;
        dataMap = null;
        if(output!=null && output.size() > 0){
            String strData = "a";
            out.append(CommonUtil.createHTML(output,true,true,strData));
        }else{
            out.append(CommonUtil.createHTML(output,true,true));
        }
        if (out.toString().length() == 0 ) {
            out = CommonUtil.createHTMLNoData();
        }
        return out.toString() ;
     }
    
    
    public static void main(String arg[]) {
        try {
            AllBranchReconcileTask tsk = new AllBranchReconcileTask();
            System.out.println (tsk.executeTask());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
