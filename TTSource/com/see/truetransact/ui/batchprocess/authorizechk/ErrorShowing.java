/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ErrorShowing.java
 *
 * Created on August 28, 2009, 4:53 PM
 */

//package com.see.truetransact.ui.batchprocess.authorizechk;
//
///**
// *
// * @author  Administrator
// */
//public class ErrorShowing {
//    
//    /** Creates a new instance of ErrorShowing */
//    public ErrorShowing() {
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
/*
 * AuthorizationCheck.java
 *
 * Created on FEB 25, 2005, 11:45 AM
 */

package com.see.truetransact.ui.batchprocess.authorizechk;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil ;
import com.see.truetransact.clientutil.ClientUtil ;
import com.see.truetransact.commonutil.TTException ;
import java.util.HashMap;
import java.util.List;
import java.util.Set ;
import java.util.Iterator ;
import com.see.truetransact.commonutil.DateUtil ;
import com.see.truetransact.ui.TrueTransactMain ;
import java.util.Date;
/**
 *
 * @author  Sathiya
 */
public class ErrorShowing{
    private Date currDt = null;
    
    /** Creates a new instance of AuthorizationCheck */
    public ErrorShowing() throws Exception {
    }
    
    public String executeTask() throws Exception {
        currDt = ClientUtil.getCurrentDate();
        HashMap paramMap = new HashMap();
        paramMap.put("ERROR_DATE", currDt.clone());
        paramMap.put("TASK_NAME", "ReverseFlexiTaskonMaturity");
        List output = ClientUtil.executeQuery("getSelectError_showing", paramMap) ;
        paramMap = null ;
        StringBuffer out = CommonUtil.createHTML(output) ;
        if (out.toString().length() == 0 ) {
            out = CommonUtil.createHTMLNoData();
        }
        return out.toString() ;
    }
     public String executeTask(String lable) throws Exception {
        currDt = ClientUtil.getCurrentDate();
        HashMap paramMap = new HashMap();
        paramMap.put("ERROR_DATE", currDt.clone());
        paramMap.put("TASK_NAME", lable);
        List output = ClientUtil.executeQuery("getSelectError_showing", paramMap) ;
        paramMap = null ;
        StringBuffer out = CommonUtil.createHTML(output) ;
          HashMap dataMap = new HashMap();
        dataMap.put("ERROR_DATE", currDt.clone());
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
		currDt = ClientUtil.getCurrentDate();
        paramMap.put("ERROR_DATE", currDt.clone());
        paramMap.put("TASK_NAME", lable);
        List output = ClientUtil.executeQuery("getSelectError_showing", paramMap) ;
        paramMap = null ;
        StringBuffer out = CommonUtil.createHTML(output) ;
          HashMap dataMap = new HashMap();
        dataMap.put("ERROR_DATE", currDt.clone());
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
            TrueTransactMain.BRANCH_ID ="ABC50001";
            ErrorShowing ft = new ErrorShowing();
            System.out.println(ft.executeTask());
        }catch(Exception E){
            E.printStackTrace();
        }
    }
}
