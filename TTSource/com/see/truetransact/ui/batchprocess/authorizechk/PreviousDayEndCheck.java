/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * PreviousDayEndCheck.java
 *
 * Created on June 22, 2009, 1:56 PM
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
import java.util.Date;
import com.see.truetransact.commonutil.DateUtil ;
import com.see.truetransact.ui.TrueTransactMain ;
/**
 *
 * @author  Administrator
 */
public class PreviousDayEndCheck {
     private List branchList;
     private Date currDt;
    /** Creates a new instance of PreviousDayEndCheck */
    public PreviousDayEndCheck() {
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
       HashMap whereMap = new HashMap();
//       List prevLst = ClientUtil.executeQuery("getPrevDayEndDate", whereMap);
//       if(prevLst!=null && prevLst.size()>0){
//           whereMap = (HashMap) prevLst.get(0);
//           whereMap.put("DAYEND_DT", whereMap.get("PREV_DT"));
//       }
        whereMap.put("DAYEND_DT", ClientUtil.getCurrentDate());
        List output = ClientUtil.executeQuery("getDayEndStatusForDC", whereMap);
//        List output = ClientUtil.executeQuery("getSelectError_showing", paramMap) ;
        whereMap = null ;
        StringBuffer out = CommonUtil.createHTML(output) ;
          HashMap dataMap = new HashMap();
        dataMap.put("ERROR_DATE", ClientUtil.getCurrentDate());
        dataMap.put("TASK_NAME", lable);
        List outputlst = ClientUtil.executeQuery("getSelectError_showing", dataMap) ;
        dataMap = null;
        if(outputlst!=null && outputlst.size() > 0){
            String strData = "a";
            out.append(CommonUtil.createHTML(outputlst,true,true,strData));
        }else{
            out.append(CommonUtil.createHTML(outputlst,true,true));
        }
        if (out.toString().length() == 0 ) {
            out = CommonUtil.createHTMLNoData();
        }
        return out.toString() ;
    }
     public String executeTask(String lable,HashMap isPrev) throws Exception{
        HashMap dateMap = new HashMap();
        StringBuffer out = null;
          if (isPrev.containsKey("BRANCH_LST")){
                branchList = (List)isPrev.get("BRANCH_LST");
              if (branchList!=null && branchList.size()>0) {
//                for (int b=0;b<branchList.size();b++) {
                    dateMap = (HashMap)branchList.get(0); 
                     HashMap whereMap = new HashMap();
                       whereMap.put("BRANCH_CODE", CommonUtil.convertObjToStr(dateMap.get("BRANCH_CODE")));
                       List brnLst = (List) ClientUtil.executeQuery("getApplDateHashMap", whereMap);
                       whereMap = null;
                       if(brnLst!=null && brnLst.size() > 0){  
                            whereMap = (HashMap)brnLst.get(0);
                            currDt = (Date) whereMap.get("CURR_APPL_DT");
                       }
        whereMap = new HashMap();
        whereMap.put("DAYEND_DT", currDt);
        List output = ClientUtil.executeQuery("getDayEndStatusForDC", whereMap);
        whereMap = null ;
        out = CommonUtil.createHTML(output) ;
          HashMap dataMap = new HashMap();
        dataMap.put("ERROR_DATE", currDt);
        dataMap.put("TASK_NAME", lable);
        List outputlst = ClientUtil.executeQuery("getSelectError_showing", dataMap) ;
        dataMap = null;
        if(outputlst!=null && outputlst.size() > 0){
            String strData = "a";
            out.append(CommonUtil.createHTML(outputlst,true,true,strData));
        }else{
            out.append(CommonUtil.createHTML(outputlst,true,true));
        }
        if (out.toString().length() == 0 ) {
            out = CommonUtil.createHTMLNoData();
        }
     
        
              }
     }
        
        return out.toString() ;
     }
    public static void main(String args[]){
        try{
            TrueTransactMain.BRANCH_ID ="ABC50001";
            PreviousDayEndCheck ft = new PreviousDayEndCheck();
            System.out.println(ft.executeTask());
        }catch(Exception E){
            E.printStackTrace();
        }
    }
    
}
