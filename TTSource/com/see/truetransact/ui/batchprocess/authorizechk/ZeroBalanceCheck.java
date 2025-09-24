/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ZeroBalanceCheck.java
 *
 * Created on MAR 1, 2005, 1:45 AM
 */

package com.see.truetransact.ui.batchprocess.authorizechk;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil ;
import com.see.truetransact.commonutil.DateUtil ;
import com.see.truetransact.clientutil.ClientUtil ;
import com.see.truetransact.commonutil.TTException ;
import java.util.HashMap;
import java.util.List;
import java.util.Set ;
import java.util.Iterator ;
import com.see.truetransact.ui.TrueTransactMain ;
import java.util.Date;
/**
 *
 * @author  Sunil (152691)
 */
public class ZeroBalanceCheck{
    private Date currDt = null;
    /** Creates a new instance of ZeroBalanceCheck */
    public ZeroBalanceCheck() throws Exception {
    }
    
   
    public String executeTask() throws Exception{
        HashMap map = new HashMap();
        currDt = ClientUtil.getCurrentDate();
        map.put("TODAY_DT", currDt.clone());
        map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        List unAuthList = ClientUtil.executeQuery("getZeroBalanceData", map) ;
        StringBuffer out = CommonUtil.createHTML(unAuthList, true, false);
        unAuthList = ClientUtil.executeQuery("getZeroBalanceDepositData", map) ;
        out.append(CommonUtil.createHTML(unAuthList, false, true));
        map = null ;
        if (out.toString().length() == 0 ) {
            out = CommonUtil.createHTMLNoData();
        }
        return out.toString() ;
    }
     public String executeTask(String lable) throws Exception{
        HashMap map = new HashMap();
        currDt = ClientUtil.getCurrentDate();
        map.put("TODAY_DT", currDt.clone());
        map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        List unAuthList = ClientUtil.executeQuery("getZeroBalanceData", map) ;
        StringBuffer out = CommonUtil.createHTML(unAuthList, true, false);
        unAuthList = ClientUtil.executeQuery("getZeroBalanceDepositData", map) ;
        out.append(CommonUtil.createHTML(unAuthList, false, true));
        map = null ;
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
     public String executeTask(String lable,HashMap isPrev) throws Exception {
           HashMap map = new HashMap();
           currDt = ClientUtil.getCurrentDate();
        map.put("TODAY_DT", currDt.clone());
        map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        List unAuthList = ClientUtil.executeQuery("getZeroBalanceData", map) ;
        StringBuffer out = CommonUtil.createHTML(unAuthList, true, false);
        unAuthList = ClientUtil.executeQuery("getZeroBalanceDepositData", map) ;
        out.append(CommonUtil.createHTML(unAuthList, false, true));
        map = null ;
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
       
    public static void main(String args[]){
        try{
            ZeroBalanceCheck ft = new ZeroBalanceCheck();
            System.out.println (ft.executeTask());
            //ft.executeTask();
        }catch(Exception E){
            E.printStackTrace();
        }
    }
}
