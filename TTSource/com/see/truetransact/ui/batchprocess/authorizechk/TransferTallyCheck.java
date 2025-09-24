/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TransferTallyCheck.java
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
/**
 *
 * @author  Sunil (152691)
 */
public class TransferTallyCheck{
   
    /** Creates a new instance of TransferTallyCheck */
    public TransferTallyCheck() throws Exception {
    }
    
    
    public String executeTask() throws Exception{
        HashMap map = new HashMap();
        map.put("TODAY_DT", ClientUtil.getCurrentDate());
        map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        List unAuthList = ClientUtil.executeQuery("getNonTallyTransfers", map) ;
        map = null ;
        StringBuffer out = CommonUtil.createHTML(unAuthList);
        if (out.toString().length() == 0 ) {
            out = CommonUtil.createHTMLNoData();
        }
        return out.toString() ;
    }
    
    public String executeTask(String lable) throws Exception{
        HashMap map = new HashMap();
        map.put("TODAY_DT", ClientUtil.getCurrentDate());
        map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        List unAuthList = ClientUtil.executeQuery("getNonTallyTransfers", map) ;
        map = null ;
        StringBuffer out = CommonUtil.createHTML(unAuthList);
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
     public String executeTask(String lable,HashMap isPrev){
          HashMap map = new HashMap();
        map.put("TODAY_DT", ClientUtil.getCurrentDate());
        map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        List unAuthList = ClientUtil.executeQuery("getNonTallyTransfers", map) ;
        map = null ;
        StringBuffer out = CommonUtil.createHTML(unAuthList);
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
            TransferTallyCheck ft = new TransferTallyCheck();
            System.out.println(ft.executeTask());
        }catch(Exception E){
            E.printStackTrace();
        }
    }
}
