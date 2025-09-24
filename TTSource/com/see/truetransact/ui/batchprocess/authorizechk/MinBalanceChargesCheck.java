/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * MinBalanceChargesCheck.java
 *
 * Created on July 6, 2009, 6:49 PM
 */

package com.see.truetransact.ui.batchprocess.authorizechk;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil ;
import com.see.truetransact.clientutil.ClientUtil ;
import com.see.truetransact.commonutil.TTException ;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set ;
import java.util.Iterator ;
import com.see.truetransact.commonutil.DateUtil ;
import com.see.truetransact.ui.TrueTransactMain ;
/**
 *
 * @author  Administrator
 */
//public class MinBalanceChargesCheck {
//    
//    /** Creates a new instance of MinBalanceChargesCheck */
//    public MinBalanceChargesCheck() {
//    }
//    
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) {
//        // TODO code application logic here
//    }
//    
//}
public class MinBalanceChargesCheck {
    
    /** Creates a new instance of DepositIntCheck */
    public MinBalanceChargesCheck() {
    }
    
     public String executeTask() throws Exception {
//        HashMap paramMap = new HashMap();
//        paramMap.put("ERROR_DATE", ClientUtil.getCurrentDate());
//        paramMap.put("TASK_NAME", "Check for previous Day's Day-End");
        HashMap whereMap = new HashMap();
        whereMap.put("DAYEND_DT", ClientUtil.getCurrentDate());
        List output = ClientUtil.executeQuery("getDayEndStatusForDC", whereMap);
//        List output = ClientUtil.executeQuery("getSelectError_showing", paramMap) ;
//        if(output!=null && output.size()>0){
//            LinkedHashMap outputMap = new LinkedHashMap();
//            outputMap = (LinkedHashMap)output.get(0);
////            outputMap.put("BRANCH_ID", branchFromList);
//            outputMap.put("TASK_NAME", "Branch DayEnd not completed...");
//            outputMap.put("TASK_STATUS", "ERROR");
//            output.add(outputMap);
//        }
        whereMap = null ;
        StringBuffer out = CommonUtil.createHTML(output) ;
        if (out.toString().length() == 0 ) {
            out = CommonUtil.createHTMLNoData();
        }
        return out.toString() ;
    }
     public String executeTask(String lable) throws Exception {
//       HashMap whereMap = new HashMap();
//        whereMap.put("DAYEND_DT", ClientUtil.getCurrentDate());
//        List output = ClientUtil.executeQuery("getDayEndStatusForDC", whereMap);
//        List output = ClientUtil.executeQuery("getSelectError_showing", paramMap) ;
//        whereMap = null ;
//        StringBuffer out = CommonUtil.createHTML(output) ;
          HashMap dataMap = new HashMap();
        dataMap.put("ERROR_DATE", ClientUtil.getCurrentDate());
        dataMap.put("TASK_NAME", lable);
        List outputlst = ClientUtil.executeQuery("getSelectError_showing", dataMap) ;
        dataMap = null;
        StringBuffer out = null;
        if(outputlst!=null && outputlst.size() > 0){
            String strData = "a";
//            out.append(CommonUtil.createHTML(outputlst,true,true,strData));
            out = CommonUtil.createHTML(outputlst,true,true,strData);
        }else{
            out = CommonUtil.createHTML(outputlst,true,true);
        }
        if (out.toString().length() == 0 ) {
            out = CommonUtil.createHTMLNoData();
        }
        return out.toString() ;
    }
      public String executeTask(String lable,HashMap isPrev) throws Exception {
            HashMap dataMap = new HashMap();
        dataMap.put("ERROR_DATE", ClientUtil.getCurrentDate());
        dataMap.put("TASK_NAME", lable);
        List outputlst = ClientUtil.executeQuery("getSelectError_showing", dataMap) ;
        dataMap = null;
        StringBuffer out = null;
        if(outputlst!=null && outputlst.size() > 0){
            String strData = "a";
//            out.append(CommonUtil.createHTML(outputlst,true,true,strData));
            out = CommonUtil.createHTML(outputlst,true,true,strData);
        }else{
            out = CommonUtil.createHTML(outputlst,true,true);
        }
        if (out.toString().length() == 0 ) {
            out = CommonUtil.createHTMLNoData();
        }
        return out.toString() ;
      }
    public static void main(String args[]){
        try{
            TrueTransactMain.BRANCH_ID ="ABC50001";
            MinBalanceChargesCheck ft = new MinBalanceChargesCheck();
            System.out.println(ft.executeTask());
        }catch(Exception E){
            E.printStackTrace();
        }
    }
    
}
