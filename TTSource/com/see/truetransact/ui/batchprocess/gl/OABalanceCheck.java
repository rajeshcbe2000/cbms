

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * OABalanceCheck.java
 *
 * Created on MAR 1, 2005, 1:45 AM
 */

package com.see.truetransact.ui.batchprocess.gl;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil ;
import com.see.truetransact.commonutil.DateUtil ;
import com.see.truetransact.clientutil.ClientUtil ;
import com.see.truetransact.commonutil.TTException ;
import com.see.truetransact.ui.TrueTransactMain ;
import com.see.truetransact.clientutil.exceptionhashmap.ExceptionHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author  Sunil (152691)
 */
public class OABalanceCheck{
    HashMap finalMap = new HashMap();
    private String BRANCH_CODE = "BRANCH_CODE" ;
    private String TODAY_DT = "TODAY_DT" ;
    private String AC_HD_ID = "AC_HD_ID" ;
    private String CLOSE_BAL = "CLOSE_BAL" ;
    private String OPN_BAL = "OPN_BAL" ;
    private String TRANS_AMT = "TRANS_AMT" ;
    private String BALANCE_TYPE = "BALANCE_TYPE";
    private String BALANCE = "BALANCE";

    ArrayList finalList = new ArrayList();
    private Date currDt = null;
    /** Creates a new instance of BalanceCheck */
    public OABalanceCheck() throws Exception {
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
    
    private void addToFinalList(List outputList){
        HashMap tempMap = null ;
        for(int i = 0, j = outputList.size() ; i < j ; i++){
            tempMap = (HashMap)outputList.get(i);
            finalMap.put(tempMap.get(AC_HD_ID), tempMap.get(BALANCE));
            tempMap = null ;
        }        
    }
    
    private void checkWithGL() throws Exception {
        HashMap tempMap = null ;
        double glAmt = 0 ;
        double acHdAmt = 0 ;	
		currDt = ClientUtil.getCurrentDate();
        HashMap paramMap = new HashMap();
        paramMap.put(BRANCH_CODE, TrueTransactMain.BRANCH_ID);
        paramMap.put(TODAY_DT, currDt.clone());
        List outputList = ClientUtil.executeQuery("selectTotalGlDayEnd", paramMap);
        System.out.println("outputList : " + outputList);
        for(int i = 0, j = outputList.size() ; i < j ; i++){
            tempMap = (HashMap)outputList.get(i);
            System.out.println("tempMap : " + tempMap);
            System.out.println("finalMap : " + finalMap);
            if(finalMap.containsKey(tempMap.get(AC_HD_ID))){
                glAmt = CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue();
                acHdAmt = CommonUtil.convertObjToDouble(finalMap.get(tempMap.get(AC_HD_ID))).doubleValue();
                System.out.println("glAmt : " + glAmt);
                System.out.println("acHdAmt : " + acHdAmt);
                tempMap.put(TRANS_AMT, new Double(acHdAmt - glAmt)) ;
                if(glAmt!=acHdAmt)
                    finalList.add(tempMap) ;
            }
        }
        finalMap = new HashMap();
    }
    
    public String executeTask() throws Exception{
        /** What this method does :
         * Executes 5 queries. The output of each query is added as a key value pair
         * in a HashMap. Key is each AC_HD_ID, value is Amount. 
         * Each time the query executes it checks if the key exists in the finalMap
         * If key exists, amt is updated. Dr amounts are multiplied by -1 and added to existing amt.
         * Cr amounts are added as is.
         * The last query totals the amount for each account head against opening and
         * closing balance in GL_Abstract. Hence the pre requesite is that the GL_Abstartct insert task
         * SHOULD be executed before this code is run.
         */
        currDt = ClientUtil.getCurrentDate();
        HashMap paramMap = new HashMap();
        //paramMap.put("BRANCH_CODE", "BRAN");
        paramMap.put(BRANCH_CODE, TrueTransactMain.BRANCH_ID);
        paramMap.put(TODAY_DT, currDt.clone());
        

//        List outputList = ClientUtil.executeQuery("getTransferTransDayEnd", paramMap);
//        checkAndAddKeys(outputList) ;
//        
//        outputList = ClientUtil.executeQuery("getCashTransDayEnd", paramMap);
//        checkAndAddKeys(outputList) ;
//        
//        outputList = ClientUtil.executeQuery("getCSHANDTransDayEnd", paramMap);
//        checkAndAddKeys(outputList) ;

        List outputList = ClientUtil.executeQuery("getAccBalancesDayEnd", paramMap);
        addToFinalList(outputList);
        checkWithGL();
        
//        outputList = ClientUtil.executeQuery("getInwardClgDayEnd", paramMap);
//        checkAndAddKeys(outputList) ;
//        
//        outputList = ClientUtil.executeQuery("getOutwardClgDayEnd", paramMap);
//        checkAndAddKeys(outputList) ;
        
        System.out.println(finalMap);
        outputList = ClientUtil.executeQuery("selectTotalGlDayEnd", paramMap);
        HashMap tempMap = null ;
        double amt = 0 ;
        double tempAmt = 0 ;
        for(int i = 0, j = outputList.size() ; i < j ; i++){
            tempMap = (HashMap)outputList.get(i);
            amt = 0 ;
            tempAmt = 0 ;
            tempMap.put(TRANS_AMT, "0");
            if(finalMap.containsKey(tempMap.get(AC_HD_ID))){
                tempMap.put(TRANS_AMT, finalMap.get(tempMap.get(AC_HD_ID))) ;
                tempAmt = + CommonUtil.convertObjToDouble(finalMap.get(tempMap.get(AC_HD_ID))).doubleValue();
                
                amt = 
                    CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue()    
                    - (CommonUtil.convertObjToDouble(tempMap.get(OPN_BAL)).doubleValue()) ;
                
                if((tempMap.get(BALANCE_TYPE).equals(CommonConstants.CREDIT) && tempAmt > 0))
                    amt+= ((-1) * tempAmt) ;
                else
                    amt+= tempAmt ;
//            } else {
//                amt = 
//                    CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue()    
//                    - CommonUtil.convertObjToDouble(tempMap.get(OPN_BAL)).doubleValue();
            }
            
            if(amt != 0)
                finalList.add(tempMap) ;
        }
        
        System.out.println("error list : " + finalList);
        StringBuffer out = CommonUtil.createHTML(finalList, true, true);
       
        if (out.toString().length() == 0 ) {
            out = CommonUtil.createHTMLNoData();
        }
        return out.toString() ;
    }
    
    public String executeTask(String lable) throws Exception{
        /** What this method does :
         * Executes 5 queries. The output of each query is added as a key value pair
         * in a HashMap. Key is each AC_HD_ID, value is Amount.
         * Each time the query executes it checks if the key exists in the finalMap
         * If key exists, amt is updated. Dr amounts are multiplied by -1 and added to existing amt.
         * Cr amounts are added as is.
         * The last query totals the amount for each account head against opening and
         * closing balance in GL_Abstract. Hence the pre requesite is that the GL_Abstartct insert task
         * SHOULD be executed before this code is run.
         */
        
        HashMap paramMap = new HashMap();
        //paramMap.put("BRANCH_CODE", "BRAN");
        paramMap.put(BRANCH_CODE, TrueTransactMain.BRANCH_ID);
        paramMap.put(TODAY_DT, currDt.clone());
        
        
        //        List outputList = ClientUtil.executeQuery("getTransferTransDayEnd", paramMap);
        //        checkAndAddKeys(outputList) ;
        //
        //        outputList = ClientUtil.executeQuery("getCashTransDayEnd", paramMap);
        //        checkAndAddKeys(outputList) ;
        //
        //        outputList = ClientUtil.executeQuery("getCSHANDTransDayEnd", paramMap);
        //        checkAndAddKeys(outputList) ;
        
        List outputList = ClientUtil.executeQuery("getAccBalancesDayEnd", paramMap);
        addToFinalList(outputList);
        checkWithGL();
        
        //        outputList = ClientUtil.executeQuery("getInwardClgDayEnd", paramMap);
        //        checkAndAddKeys(outputList) ;
        //
        //        outputList = ClientUtil.executeQuery("getOutwardClgDayEnd", paramMap);
        //        checkAndAddKeys(outputList) ;
        
        System.out.println(finalMap);
        outputList = ClientUtil.executeQuery("selectTotalGlDayEnd", paramMap);
        HashMap tempMap = null ;
        double amt = 0 ;
        double tempAmt = 0 ;
        for(int i = 0, j = outputList.size() ; i < j ; i++){
            tempMap = (HashMap)outputList.get(i);
            amt = 0 ;
            tempAmt = 0 ;
            tempMap.put(TRANS_AMT, "0");
            if(finalMap.containsKey(tempMap.get(AC_HD_ID))){
                tempMap.put(TRANS_AMT, finalMap.get(tempMap.get(AC_HD_ID))) ;
                tempAmt = + CommonUtil.convertObjToDouble(finalMap.get(tempMap.get(AC_HD_ID))).doubleValue();
                
                amt =
                CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue()
                - (CommonUtil.convertObjToDouble(tempMap.get(OPN_BAL)).doubleValue()) ;
                
                if((tempMap.get(BALANCE_TYPE).equals(CommonConstants.CREDIT) && tempAmt > 0))
                    amt+= ((-1) * tempAmt) ;
                else
                    amt+= tempAmt ;
                //            } else {
                //                amt =
                //                    CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue()
                //                    - CommonUtil.convertObjToDouble(tempMap.get(OPN_BAL)).doubleValue();
            }
            
            if(amt != 0)
                finalList.add(tempMap) ;
        }
        System.out.println("error list : " + finalList);
        StringBuffer out = CommonUtil.createHTML(finalList, true, true);
        HashMap dataMap = new HashMap();
        dataMap.put("ERROR_DATE", currDt);
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
     public String executeTask(String lable,HashMap isPrev)throws Exception{
        /** What this method does :
         * Executes 5 queries. The output of each query is added as a key value pair
         * in a HashMap. Key is each AC_HD_ID, value is Amount.
         * Each time the query executes it checks if the key exists in the finalMap
         * If key exists, amt is updated. Dr amounts are multiplied by -1 and added to existing amt.
         * Cr amounts are added as is.
         * The last query totals the amount for each account head against opening and
         * closing balance in GL_Abstract. Hence the pre requesite is that the GL_Abstartct insert task
         * SHOULD be executed before this code is run.
         */
        
        HashMap paramMap = new HashMap();
        //paramMap.put("BRANCH_CODE", "BRAN");
        paramMap.put(BRANCH_CODE, TrueTransactMain.BRANCH_ID);
        paramMap.put(TODAY_DT, currDt);
        
        
        //        List outputList = ClientUtil.executeQuery("getTransferTransDayEnd", paramMap);
        //        checkAndAddKeys(outputList) ;
        //
        //        outputList = ClientUtil.executeQuery("getCashTransDayEnd", paramMap);
        //        checkAndAddKeys(outputList) ;
        //
        //        outputList = ClientUtil.executeQuery("getCSHANDTransDayEnd", paramMap);
        //        checkAndAddKeys(outputList) ;
        
        List outputList = ClientUtil.executeQuery("getAccBalancesDayEnd", paramMap);
        addToFinalList(outputList);
        checkWithGL();
        
        //        outputList = ClientUtil.executeQuery("getInwardClgDayEnd", paramMap);
        //        checkAndAddKeys(outputList) ;
        //
        //        outputList = ClientUtil.executeQuery("getOutwardClgDayEnd", paramMap);
        //        checkAndAddKeys(outputList) ;
        
        System.out.println(finalMap);
        outputList = ClientUtil.executeQuery("selectTotalGlDayEnd", paramMap);
        HashMap tempMap = null ;
        double amt = 0 ;
        double tempAmt = 0 ;
        for(int i = 0, j = outputList.size() ; i < j ; i++){
            tempMap = (HashMap)outputList.get(i);
            amt = 0 ;
            tempAmt = 0 ;
            tempMap.put(TRANS_AMT, "0");
            if(finalMap.containsKey(tempMap.get(AC_HD_ID))){
                tempMap.put(TRANS_AMT, finalMap.get(tempMap.get(AC_HD_ID))) ;
                tempAmt = + CommonUtil.convertObjToDouble(finalMap.get(tempMap.get(AC_HD_ID))).doubleValue();
                
                amt =
                CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue()
                - (CommonUtil.convertObjToDouble(tempMap.get(OPN_BAL)).doubleValue()) ;
                
                if((tempMap.get(BALANCE_TYPE).equals(CommonConstants.CREDIT) && tempAmt > 0))
                    amt+= ((-1) * tempAmt) ;
                else
                    amt+= tempAmt ;
                //            } else {
                //                amt =
                //                    CommonUtil.convertObjToDouble(tempMap.get(CLOSE_BAL)).doubleValue()
                //                    - CommonUtil.convertObjToDouble(tempMap.get(OPN_BAL)).doubleValue();
            }
            
            if(amt != 0)
                finalList.add(tempMap) ;
        }
        System.out.println("error list : " + finalList);
        StringBuffer out = CommonUtil.createHTML(finalList, true, true);
        HashMap dataMap = new HashMap();
        dataMap.put("ERROR_DATE", currDt);
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
    public static void main(String args[]){
        try{
            OABalanceCheck ft = new OABalanceCheck() ;
            System.out.println (ft.executeTask());
        }catch(Exception E){
            E.printStackTrace();
        }
    }
}
