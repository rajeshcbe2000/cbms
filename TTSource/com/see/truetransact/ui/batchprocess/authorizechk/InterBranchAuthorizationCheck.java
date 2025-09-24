/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * InterBranchAuthorizationCheck.java
 *
 * Created on AUG 23, 2005, 11:45 AM
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
 * @author  Sunil (152691)
 */
public class InterBranchAuthorizationCheck{
    
    private Date currDt = null;
    /** Creates a new instance of AuthorizationCheck */
    public InterBranchAuthorizationCheck() throws Exception {
    }
    
    public String executeTask() throws Exception {
        StringBuffer strBuff = new StringBuffer();
        StringBuffer out = new StringBuffer();
        HashMap paramMap = new HashMap();
		currDt = ClientUtil.getCurrentDate();
        paramMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        paramMap.put("TODAY_DT", currDt.clone());
//        List tableList = ClientUtil.executeQuery("getCASH_TRANSNonAuthRecInterBranch", paramMap) ;
//        strBuff.append(CommonUtil.createHTML(tableList,true,false));
//        List TrnList = ClientUtil.executeQuery("getTRANSFER_TRANSNonAuthRecInterBranch", paramMap) ;
//        strBuff.append(CommonUtil.createHTML(TrnList,false,false));
//        List InwardList = ClientUtil.executeQuery("getINWARD_CLEARINGNonAuthRecInterBranch", paramMap) ;
//        strBuff.append(CommonUtil.createHTML(InwardList,false,false));
//        List OutList = ClientUtil.executeQuery("getOutward_CLEARINGNonAuthRecInterBranch", paramMap) ;
//        strBuff.append(CommonUtil.createHTML(OutList,false,true));
        List tableList = ClientUtil.executeQuery("getNonAuthRecInterBranch", paramMap) ;
        strBuff.append(CommonUtil.createHTML(tableList,true,true));
        if(strBuff.length()==0){
            strBuff = CommonUtil.createHTMLNoData();
        }
        boolean header = true;
        boolean footer = true;
        int len = 15;
        if (tableList.size() < len)
            len = tableList.size();
        
        for(int i = 0, j = tableList.size(); i < len; i++){
            if(i == 0){
                header = true;
                footer = false;
            }
            if(i == j){
                header = false ;
                footer = true ;
            }
            //
            
            //             strBuff.append(getIntrUnAuthorizedData((HashMap)tableList.get(i), header, footer)) ;
        }
        //            out.append(CommonUtil.createHTML(tableList, header, footer));
        //            if (out.toString().length() == 0 ) {
        //                out = CommonUtil.createHTMLNoData();
        //            }
        return strBuff.toString();
    }
     public String executeTask(String lable) throws Exception {
        StringBuffer strBuff = new StringBuffer();
        StringBuffer out = new StringBuffer();
        HashMap paramMap = new HashMap();
		currDt = ClientUtil.getCurrentDate();
        paramMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        paramMap.put("TODAY_DT", currDt.clone());
//        List tableList = ClientUtil.executeQuery("getCASH_TRANSNonAuthRecInterBranch", paramMap) ;
//        strBuff.append(CommonUtil.createHTML(tableList,true,false));
//        List TrnList = ClientUtil.executeQuery("getTRANSFER_TRANSNonAuthRecInterBranch", paramMap) ;
//        strBuff.append(CommonUtil.createHTML(TrnList,false,false));
//        List InwardList = ClientUtil.executeQuery("getINWARD_CLEARINGNonAuthRecInterBranch", paramMap) ;
//        strBuff.append(CommonUtil.createHTML(InwardList,false,false));
//        List OutList = ClientUtil.executeQuery("getOutward_CLEARINGNonAuthRecInterBranch", paramMap) ;
//        strBuff.append(CommonUtil.createHTML(OutList,false,true));
        List tableList = ClientUtil.executeQuery("getNonAuthRecInterBranch", paramMap) ;
        strBuff.append(CommonUtil.createHTML(tableList,true,true));
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
        if(strBuff.length()==0){
            strBuff = CommonUtil.createHTMLNoData();
        }
        boolean header = true;
        boolean footer = true;
        int len = 15;
        if (tableList.size() < len)
            len = tableList.size();
        
        for(int i = 0, j = tableList.size(); i < len; i++){
            if(i == 0){
                header = true;
                footer = false;
            }
            if(i == j){
                header = false ;
                footer = true ;
            }
            //
            
            //             strBuff.append(getIntrUnAuthorizedData((HashMap)tableList.get(i), header, footer)) ;
        }
        //            out.append(CommonUtil.createHTML(tableList, header, footer));
        //            if (out.toString().length() == 0 ) {
        //                out = CommonUtil.createHTMLNoData();
        //            }
        return strBuff.toString();
    }
      public String executeTask(String lable,HashMap isPrev){
           StringBuffer strBuff = new StringBuffer();
        StringBuffer out = new StringBuffer();
        HashMap paramMap = new HashMap();
		currDt = ClientUtil.getCurrentDate();
        paramMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        paramMap.put("TODAY_DT", currDt.clone());
//        List tableList = ClientUtil.executeQuery("getCASH_TRANSNonAuthRecInterBranch", paramMap) ;
//        strBuff.append(CommonUtil.createHTML(tableList,true,false));
//        List TrnList = ClientUtil.executeQuery("getTRANSFER_TRANSNonAuthRecInterBranch", paramMap) ;
//        strBuff.append(CommonUtil.createHTML(TrnList,false,false));
//        List InwardList = ClientUtil.executeQuery("getINWARD_CLEARINGNonAuthRecInterBranch", paramMap) ;
//        strBuff.append(CommonUtil.createHTML(InwardList,false,false));
//        List OutList = ClientUtil.executeQuery("getOutward_CLEARINGNonAuthRecInterBranch", paramMap) ;
//        strBuff.append(CommonUtil.createHTML(OutList,false,true));
        List tableList = ClientUtil.executeQuery("getNonAuthRecInterBranch", paramMap) ;
        strBuff.append(CommonUtil.createHTML(tableList,true,true));
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
        if(strBuff.length()==0){
            strBuff = CommonUtil.createHTMLNoData();
        }
        boolean header = true;
        boolean footer = true;
        int len = 15;
        if (tableList.size() < len)
            len = tableList.size();
        
        for(int i = 0, j = tableList.size(); i < len; i++){
            if(i == 0){
                header = true;
                footer = false;
            }
            if(i == j){
                header = false ;
                footer = true ;
            }
            //
            
            //             strBuff.append(getIntrUnAuthorizedData((HashMap)tableList.get(i), header, footer)) ;
        }
        //            out.append(CommonUtil.createHTML(tableList, header, footer));
        //            if (out.toString().length() == 0 ) {
        //                out = CommonUtil.createHTMLNoData();
        //            }
        return strBuff.toString();
      }
    //    private StringBuffer getIntrUnAuthorizedData(HashMap map, boolean header, boolean footer) throws Exception{
    //
    //        HashMap tableMap= new HashMap();
    //        tableMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
    ////        tableMap.put("TABLE_NAME", map.get("TABLE_NAME")  );
    //        tableMap.put("TODAY_DT", currDt);
    //        List unAuthList = ClientUtil.executeQuery("getNonAuthRecInterBranch", tableMap) ;
    //        System.out.println("unAuthList:" + unAuthList);
    //        StringBuffer out = CommonUtil.createHTML(unAuthList, header, footer);
    //        if (out.toString().length() == 0 ) {
    //            out = CommonUtil.createHTMLNoData();
    //        }
    //        return out ;
    //    }
    public static void main(String args[]){
        try{
            TrueTransactMain.BRANCH_ID ="ABC50001";
            InterBranchAuthorizationCheck ft = new InterBranchAuthorizationCheck();
            System.out.println(ft.executeTask());
        }catch(Exception E){
            E.printStackTrace();
        }
    }
}
