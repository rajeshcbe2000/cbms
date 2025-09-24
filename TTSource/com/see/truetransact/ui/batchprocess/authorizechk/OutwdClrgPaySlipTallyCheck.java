/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * OutwdClrgPaySlipTallyCheck.java
 *
 * Created on MAR 1, 2005, 1:45 AM
 */

package com.see.truetransact.ui.batchprocess.authorizechk;
import com.see.truetransact.commonutil.DateUtil ;
import com.see.truetransact.commonutil.CommonUtil ;
import com.see.truetransact.clientutil.ClientUtil ;
import java.util.HashMap;
import java.util.List;
import com.see.truetransact.ui.TrueTransactMain ;
import java.util.Date;
/**
 *
 * @author  Sunil (152691)
 */
public class OutwdClrgPaySlipTallyCheck{
   	private Date currDt = null;
    /** Creates a new instance of OutwdClrgPaySlipTallyCheck */
    public OutwdClrgPaySlipTallyCheck() throws Exception {
    }
    
    
    public String executeTask() throws Exception{
        currDt = ClientUtil.getCurrentDate();
        HashMap map = new HashMap();
        map.put("TODAY_DT", currDt.clone());
        map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        List unAuthList = ClientUtil.executeQuery("getNonTallyOutClr_PS", map) ;
        map = null ;
        StringBuffer out = CommonUtil.createHTML(unAuthList) ;
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
        List unAuthList = ClientUtil.executeQuery("getNonTallyOutClr_PS", map) ;
        map = null ;
        StringBuffer out = CommonUtil.createHTML(unAuthList) ;
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
        List unAuthList = ClientUtil.executeQuery("getNonTallyOutClr_PS", map) ;
        map = null ;
        StringBuffer out = CommonUtil.createHTML(unAuthList) ;
          HashMap dataMap = new HashMap();
        dataMap.put("ERROR_DATE", ClientUtil.getCurrentDate());
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
            OutwdClrgPaySlipTallyCheck ft = new OutwdClrgPaySlipTallyCheck();
            System.out.println(ft.executeTask());
        }catch(Exception E){
            E.printStackTrace();
        }
    }
}
