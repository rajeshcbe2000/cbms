/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * UnclearedScheduleCheck.java
 *
 * Created on March 10, 2005, 10:40 AM
 */

package com.see.truetransact.ui.batchprocess.gl;

import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil ;
import com.see.truetransact.commonutil.DateUtil ;
import com.see.truetransact.commonutil.TTException ;
import com.see.truetransact.clientutil.ClientUtil ;
import java.util.HashMap;
import java.util.List;
import com.see.truetransact.ui.TrueTransactMain ;
/**
 *
 * @author  Sunil (152691)
 */
public class UnclearedScheduleCheck{
    private String branch = null ;      
    
    /** Creates a new instance of UnclearedScheduleCheck */
    public UnclearedScheduleCheck() throws Exception {
    }
    
    public String executeTask() throws Exception {
        HashMap paramMap = new HashMap();
        paramMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        paramMap.put("TODAY_DT",ClientUtil.getCurrentDate()); 
        List output = ClientUtil.executeQuery("getUnclearedClrgSchedule", paramMap) ;
        paramMap = null ;
        StringBuffer out = CommonUtil.createHTML(output) ;
        if (out.toString().length() == 0 ) {
            out = CommonUtil.createHTMLNoData();
        }
        return out.toString() ;
    }
    
    public String executeTask(String lable) throws Exception {
        HashMap paramMap = new HashMap();
        paramMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        paramMap.put("TODAY_DT",ClientUtil.getCurrentDate()); 
        List output = ClientUtil.executeQuery("getUnclearedClrgSchedule", paramMap) ;
        paramMap = null ;
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
     public String executeTask(String lable,HashMap isPrev) throws Exception {
             HashMap paramMap = new HashMap();
        paramMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        paramMap.put("TODAY_DT",ClientUtil.getCurrentDate()); 
        List output = ClientUtil.executeQuery("getUnclearedClrgSchedule", paramMap) ;
        paramMap = null ;
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
  
    public static void main(String args[]){
        try{
            UnclearedScheduleCheck ft = new UnclearedScheduleCheck();
            System.out.println (ft.executeTask());
        }catch(Exception E){
            E.printStackTrace();
        }
    }
}
