/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * OAInoperativeBalanceCheck.java
 *
 * Created on March 11, 2005, 10:40 AM
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
import java.util.Date;
/**
 *
 * @author  Sunil (152691)
 */
public class OAInoperativeBalanceCheck{
    private String branch = null ;      
    private Date currDt = null;
    /** Creates a new instance of OAInoperativeBalanceCheck */
    public OAInoperativeBalanceCheck() throws Exception {
    }
    
    public String executeTask() throws Exception {
        HashMap paramMap = new HashMap();
        HashMap tempMap = null ;
        paramMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        List outputList = ClientUtil.executeQuery("getOAInoperativeBalCheck", paramMap) ;
        for(int i = 0, j = outputList.size(); i < j; i++){
            tempMap = (HashMap)outputList.get(i) ;
            if(tempMap.get("GL_SUM")!= null && 
                !CommonUtil.convertObjToStr(tempMap.get("GL_SUM")).equals("")){
                    if(CommonUtil.convertObjToDouble(tempMap.get("GL_SUM")).doubleValue()
                        == CommonUtil.convertObjToDouble(tempMap.get("ACT_SUM")).doubleValue())
                    outputList.remove(i) ;
            }
        }
        tempMap = null ;
        paramMap = null ;
        StringBuffer out = CommonUtil.createHTML(outputList) ;
        if (out.toString().length() == 0 ) {
            out = CommonUtil.createHTMLNoData();
        }
        return out.toString() ;
    }
    
     public String executeTask(String lable) throws Exception {
        HashMap paramMap = new HashMap();
        HashMap tempMap = null ;
        paramMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        List outputList = ClientUtil.executeQuery("getOAInoperativeBalCheck", paramMap) ;
        for(int i = 0, j = outputList.size(); i < j; i++){
            tempMap = (HashMap)outputList.get(i) ;
            if(tempMap.get("GL_SUM")!= null && 
                !CommonUtil.convertObjToStr(tempMap.get("GL_SUM")).equals("")){
                    if(CommonUtil.convertObjToDouble(tempMap.get("GL_SUM")).doubleValue()
                        == CommonUtil.convertObjToDouble(tempMap.get("ACT_SUM")).doubleValue())
                    outputList.remove(i) ;
            }
        }
        tempMap = null ;
        paramMap = null ;
        StringBuffer out = CommonUtil.createHTML(outputList) ;
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
        HashMap paramMap = new HashMap();
        HashMap tempMap = null ;
        paramMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        List outputList = ClientUtil.executeQuery("getOAInoperativeBalCheck", paramMap) ;
        for(int i = 0, j = outputList.size(); i < j; i++){
            tempMap = (HashMap)outputList.get(i) ;
            if(tempMap.get("GL_SUM")!= null && 
                !CommonUtil.convertObjToStr(tempMap.get("GL_SUM")).equals("")){
                    if(CommonUtil.convertObjToDouble(tempMap.get("GL_SUM")).doubleValue()
                        == CommonUtil.convertObjToDouble(tempMap.get("ACT_SUM")).doubleValue())
                    outputList.remove(i) ;
            }
        }
        tempMap = null ;
        paramMap = null ;
        StringBuffer out = CommonUtil.createHTML(outputList) ;
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
            OAInoperativeBalanceCheck ft = new OAInoperativeBalanceCheck();
            System.out.println (ft.executeTask());
        }catch(Exception E){
            E.printStackTrace();
        }
    }
}
