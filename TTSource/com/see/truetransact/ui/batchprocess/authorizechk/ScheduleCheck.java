/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ScheduleCheck.java
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
 * @author  Sunil (152691)
 */
public class ScheduleCheck{
    private Date currDt = null;
    private Date prevDt = null;
    
    /** Creates a new instance of AuthorizationCheck */
    public ScheduleCheck() throws Exception {
    }
    
    public String executeTask() throws Exception {
        StringBuffer strBuff = new StringBuffer();
        currDt = ClientUtil.getCurrentDate();
        HashMap paramMap = new HashMap();
        paramMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        paramMap.put("TODAY_DT", currDt.clone());
        prevDt = DateUtil.addDaysProperFormat(currDt, -1);
        paramMap.put("PREV_DT", prevDt);
        List tableList = ClientUtil.executeQuery("scheduleTable", paramMap) ;
        boolean header = true;
        boolean footer = true;
        int len = 15;
        if (tableList.size() < len) len = tableList.size();
        System.out.println("rish..."+len);
        for(int i = 0, j = tableList.size(); i < len; i++){
            if(i == 0){
                header = true;
                footer = false;
            }
            if(i == j){
                header = false ;
                footer = true ;
            }
            System.out.println("rish........."+(HashMap)tableList.get(i));
            strBuff.append(getIssueData((HashMap)tableList.get(i), header, footer)) ;
            System.out.println("rissssssssssss");
        }
        return strBuff.toString();
    }
     public String executeDataTask() throws Exception {
        StringBuffer strBuff = new StringBuffer();
        currDt = ClientUtil.getCurrentDate();
        HashMap paramMap = new HashMap();
        paramMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        paramMap.put("TODAY_DT", currDt.clone());
        prevDt = DateUtil.addDaysProperFormat(currDt, -1);
        paramMap.put("PREV_DT", prevDt);
        List tableList = ClientUtil.executeQuery("getIssueTransactionTable", paramMap) ;
        boolean header = true;
        boolean footer = true;
        int len = 15;
        if (tableList.size() < len) len = tableList.size();
        System.out.println("rish..."+len);
        for(int i = 0, j = tableList.size(); i < len; i++){
            if(i == 0){
                header = true;
                footer = false;
            }
            if(i == j){
                header = false ;
                footer = true ;
            }
            System.out.println("rish........."+(HashMap)tableList.get(i));
            strBuff.append(getListData((HashMap)tableList.get(i), header, footer)) ;
            System.out.println("rissssssssssss");
        }
        return strBuff.toString();
    }
    public String executeTask(String lable) throws Exception {
        StringBuffer strBuff = new StringBuffer();
        HashMap paramMap = new HashMap();
		currDt = ClientUtil.getCurrentDate();
        paramMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        paramMap.put("TODAY_DT", currDt.clone());
        List tableList = ClientUtil.executeQuery("checkSchedule", paramMap) ;
        boolean header = true;
        boolean footer = true;
        int len = 15;
        if (tableList.size() < len) len = tableList.size();
        
        for(int i = 0, j = tableList.size(); i < len; i++){
            if(i == 0){
                header = true;
                footer = false;
            }
            if(i == j){
                header = false ;
                footer = true ;
            }
            
           strBuff.append(getIssueData((HashMap)tableList.get(i), header, footer)) ;
        }
          HashMap dataMap = new HashMap();
        dataMap.put("ERROR_DATE", currDt.clone());
        dataMap.put("TASK_NAME", lable);
        List output = ClientUtil.executeQuery("getSelectError_showing", dataMap) ;
        dataMap = null;
        if(output!=null && output.size() > 0){
            String strData = "a";
            strBuff.append(CommonUtil.createHTML(output,true,true,strData));
        }else{
            strBuff.append(CommonUtil.createHTML(output,true,true));
        }
        if (strBuff.toString().length() == 0 ) {
            strBuff = CommonUtil.createHTMLNoData();
        }
        return strBuff.toString();
    }
     public String executeTask(String lable,HashMap isPrev) throws Exception {
        StringBuffer strBuff = new StringBuffer();
		currDt = ClientUtil.getCurrentDate();
        HashMap paramMap = new HashMap();
        paramMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        paramMap.put("TODAY_DT", currDt.clone());
        List tableList = ClientUtil.executeQuery("checkSchedule", paramMap) ;
        boolean header = true;
        boolean footer = true;
        int len = 15;
        if (tableList.size() < len) len = tableList.size();
        
        for(int i = 0, j = tableList.size(); i < len; i++){
            if(i == 0){
                header = true;
                footer = false;
            }
            if(i == j){
                header = false ;
                footer = true ;
            }
            
            strBuff.append(getIssueData((HashMap)tableList.get(i), header, footer)) ;
        }
//        HashMap dataMap = new HashMap();
//        dataMap.put("ERROR_DATE", currDt.clone());
//        dataMap.put("TASK_NAME", lable);
//        List output = ClientUtil.executeQuery("getSelectError_showing", dataMap) ;
//        dataMap = null;
//        if(output!=null && output.size() > 0){
//            String strData = "a";
//            strBuff.append(CommonUtil.createHTML(output,true,true,strData));
//        }else{
//            strBuff.append(CommonUtil.createHTML(output,true,true));
//        }
//        if (strBuff.toString().length() == 0 ) {
//            strBuff = CommonUtil.createHTMLNoData();
//        }
        return strBuff.toString();
    }
//    private StringBuffer getUnAuthorizedData(HashMap map, boolean header, boolean footer) throws Exception{
//        map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
//        List unAuthList = ClientUtil.executeQuery("getNonAuthorizedRecords", map) ;
//        System.out.println("unAuthList:" + unAuthList);
//        StringBuffer out = CommonUtil.createHTML(unAuthList, header, footer);
//        return out ;
//    }
    private StringBuffer getIssueData(HashMap map, boolean header, boolean footer) throws Exception {
        //map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);

        map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        map.put("TODAY_DT", currDt.clone());
        prevDt = DateUtil.addDaysProperFormat(currDt, -1);
        map.put("PREV_DT", prevDt);
        System.out.println("rissssssssssssssddfg" + map);
        List unAuthList = ClientUtil.executeQuery("checkScheduleData", map);
        System.out.println("unAuthList:" + unAuthList);
        StringBuffer out = CommonUtil.createHTML(unAuthList, header, footer);
        return out;
    }
     private StringBuffer getListData(HashMap map, boolean header, boolean footer) throws Exception {
        //  map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);

        map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        map.put("TODAY_DT", currDt.clone());
        prevDt = DateUtil.addDaysProperFormat(currDt, -1);
        map.put("PREV_DT", prevDt);
        System.out.println("rissssssssssssssddfg" + map);
        List unAuthList = ClientUtil.executeQuery("getTransactionData", map);
        System.out.println("unAuthList:" + unAuthList);
        StringBuffer out = CommonUtil.createHTML(unAuthList, header, footer);
        return out;
    }
    public static void main(String args[]){
        try{
            TrueTransactMain.BRANCH_ID ="ABC50001";
            ScheduleCheck ft = new ScheduleCheck();
            System.out.println(ft.executeTask());
        }catch(Exception E){
            E.printStackTrace();
        }
    }
}
