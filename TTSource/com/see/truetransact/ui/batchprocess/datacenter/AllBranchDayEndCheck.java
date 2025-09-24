/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AllBranchDayEndCheck.java
 *
 * Created on MAR 1, 2005, 1:45 AM
 */

package com.see.truetransact.ui.batchprocess.datacenter;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil ;
import com.see.truetransact.commonutil.DateUtil ;
import com.see.truetransact.clientutil.ClientUtil ;
import com.see.truetransact.serverutil.ServerUtil ;
import com.see.truetransact.commonutil.TTException ;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Iterator;
import java.util.Date;
import com.see.truetransact.ui.TrueTransactMain;
/**
 *
 * @author  Bala
 */
public class AllBranchDayEndCheck{
    private Date currDt;
    private List branchList;
    private StringBuffer presentTasks = new StringBuffer();
    
    /** Creates a new instance of ZeroBalanceCheck */
    public AllBranchDayEndCheck() throws Exception {
        currDt = ClientUtil.getCurrentDate();
        HashMap tempMap = new HashMap();
        tempMap.put("NEXT_DATE", currDt);
        branchList=(List)ClientUtil.executeQuery("getAllBranchesFromDayEndChk",tempMap);
        tempMap = null;        
    }
    
   
    public String executeTask() throws Exception{
		currDt = ClientUtil.getCurrentDate();
        HashMap passMap = new HashMap();
        passMap.put("CURR_DT", currDt);
        ArrayList outputList = new ArrayList();
        List lst = ClientUtil.executeQuery("checkAllBranchDayEndStartedOrNot", passMap);
        LinkedHashMap dayEndBranchMap = null;
        if (lst!=null && lst.size()>0) {
            dayEndBranchMap = (LinkedHashMap)lst.get(0);
            System.out.println("#$#$# dayEndBranchList : "+lst);
        } else
            System.out.println("#$#$# dayEndBranchList is null");

            String branchFromList = "";
            String dateList = "";
            if (branchList!=null && branchList.size()>0) {
                for (int b=0;b<branchList.size();b++) {
                    HashMap branchMap = (HashMap) branchList.get(b);
                    branchFromList = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                    dateList = CommonUtil.convertObjToStr(branchMap.get("CURR_APPL_DT"));
                    int existCount=0;
                    if (lst!=null && lst.size()>0)
                    for (int i=0; i<lst.size(); i++) {
                        dayEndBranchMap = (LinkedHashMap) lst.get(i);
                        String dayEndBranch = CommonUtil.convertObjToStr(dayEndBranchMap.get("BRANCH_ID"));
                        int cnt = CommonUtil.convertObjToInt(dayEndBranchMap.get("CNT"));
                        if (branchFromList.equals(dayEndBranch) && cnt>0) {
                            existCount++;
                            HashMap mapData = new HashMap();
                            mapData.put("DAYEND_DT", currDt.clone());
                            mapData.put("BRANCH_ID", branchFromList);
                            List lstData = ((List)ClientUtil.executeQuery("getSelectBranchCompletedLst", mapData));
                            mapData = null;
                            if(lstData != null && lstData.size() > 0){
                            for(int j=0;j<lstData.size();j++){
                                mapData = (HashMap)lstData.get(j);
//                                LinkedHashMap outputMap = new LinkedHashMap();
//                                outputMap.put("BRANCH_ID", branchFromList);
//                                outputMap.put("TASK_NAME", "DayEnd not completed...");
//                                outputMap.put("TASK_STATUS", "ERROR");
//                                outputList.add(outputMap);
                            }
                            }else{
                                LinkedHashMap outputMap = new LinkedHashMap();
                                outputMap.put("BRANCH_ID", branchFromList);
                                 outputMap.put("APPL_DATE", dateList);
                                outputMap.put("TASK_NAME", "Branch DayEnd not completed...");
                                outputMap.put("TASK_STATUS", "ERROR");
                                outputList.add(outputMap);
                            }
                        }
                    }
//                    HashMap mapData = new HashMap();
//                    mapData.put("DAYEND_DT", currDt.clone());
//                    mapData.put("BRANCH_ID", branchFromList);
//                    List lstData = ((List)ClientUtil.executeQuery("getSelectBranchCompletedLst", mapData));
//                    mapData = null;
//                    if(lstData != null && lstData.size() > 0){
//                        for(int i=0;i<lstData.size();i++){
//                            mapData = (HashMap)lstData.get(i);
//                            LinkedHashMap outputMap = new LinkedHashMap();
//                            outputMap.put("BRANCH_ID", branchFromList);
//                            outputMap.put("TASK_NAME", "DayEnd not completed...");
//                            outputMap.put("TASK_STATUS", "ERROR");
//                            outputList.add(outputMap);
//                            
//                        }
//                    }else 
                        if (existCount==0) {
                        LinkedHashMap outputMap = new LinkedHashMap();
                        outputMap.put("BRANCH_ID", branchFromList);
                        outputMap.put("APPL_DATE", dateList);
                        outputMap.put("TASK_NAME", "DayEnd not started...");
                        outputMap.put("TASK_STATUS", "ERROR");
                        outputList.add(outputMap);
                    }
//                        else{
//                        //do nothing
//                    }
                    
                }
            }
        lst = ClientUtil.executeQuery("checkAllBranchDayEnd", passMap);
        outputList.addAll(lst);
        System.out.println("#$#$# outputList : "+outputList);
        StringBuffer out = CommonUtil.createHTML(outputList, true, false);
        if (out.toString().length() == 0 ) {
            out = CommonUtil.createHTMLNoData();
        }
        return out.toString() ;
//        HashMap map = new HashMap();
//        List branchList = ClientUtil.executeQuery("checkAllBranchDayEnd", map) ;
//        StringBuffer out = CommonUtil.createHTML(branchList, true, false);
//        map = null ;
//        if (out.toString().length() == 0 ) {
//            out = CommonUtil.createHTMLNoData();
//        }
//        return out.toString() ;
       
    }
    public String executeTask(String lable) throws Exception{
        HashMap passMap = new HashMap();
		currDt = ClientUtil.getCurrentDate();
        passMap.put("CURR_DT", currDt);
        ArrayList outputList = new ArrayList();
        List lst = ClientUtil.executeQuery("checkAllBranchDayEndStartedOrNot", passMap);
        LinkedHashMap dayEndBranchMap = null;
        if (lst!=null && lst.size()>0) {
            dayEndBranchMap = (LinkedHashMap)lst.get(0);
            System.out.println("#$#$# dayEndBranchList : "+lst);
        } else
            System.out.println("#$#$# dayEndBranchList is null");

            String branchFromList = "";
            String dateList = "";
            if (branchList!=null && branchList.size()>0) {
                for (int b=0;b<branchList.size();b++) {
                    HashMap branchMap = (HashMap) branchList.get(b);
                    branchFromList = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                    dateList = CommonUtil.convertObjToStr(branchMap.get("CURR_APPL_DT"));
                    int existCount=0;
                    if (lst!=null && lst.size()>0)
                    for (int i=0; i<lst.size(); i++) {
                        dayEndBranchMap = (LinkedHashMap) lst.get(i);
                        String dayEndBranch = CommonUtil.convertObjToStr(dayEndBranchMap.get("BRANCH_ID"));
                        int cnt = CommonUtil.convertObjToInt(dayEndBranchMap.get("CNT"));
                        if (branchFromList.equals(dayEndBranch) && cnt>0) {
                            existCount++;
                            HashMap mapData = new HashMap();
                            mapData.put("DAYEND_DT", currDt.clone());
                            mapData.put("BRANCH_ID", branchFromList);
                            List lstData = ((List)ClientUtil.executeQuery("getSelectBranchCompletedLst", mapData));
                            mapData = null;
                            if(lstData != null && lstData.size() > 0){
                            for(int j=0;j<lstData.size();j++){
                                mapData = (HashMap)lstData.get(j);
//                                LinkedHashMap outputMap = new LinkedHashMap();
//                                outputMap.put("BRANCH_ID", branchFromList);
//                                outputMap.put("TASK_NAME", "DayEnd not completed...");
//                                outputMap.put("TASK_STATUS", "ERROR");
//                                outputList.add(outputMap);
                            }
                            }else{
                                LinkedHashMap outputMap = new LinkedHashMap();
                                outputMap.put("BRANCH_ID", branchFromList);
                                outputMap.put("APPL_DATE", dateList);
                                outputMap.put("TASK_NAME", "Branch DayEnd not completed...");
                                outputMap.put("TASK_STATUS", "ERROR");
                                outputList.add(outputMap);
                            }
                        }
                    }
//                    HashMap mapData = new HashMap();
//                    mapData.put("DAYEND_DT", currDt.clone());
//                    mapData.put("BRANCH_ID", branchFromList);
//                    List lstData = ((List)ClientUtil.executeQuery("getSelectBranchCompletedLst", mapData));
//                    mapData = null;
//                    if(lstData != null && lstData.size() > 0){
//                        for(int i=0;i<lstData.size();i++){
//                            mapData = (HashMap)lstData.get(i);
//                            LinkedHashMap outputMap = new LinkedHashMap();
//                            outputMap.put("BRANCH_ID", branchFromList);
//                            outputMap.put("TASK_NAME", "DayEnd not completed...");
//                            outputMap.put("TASK_STATUS", "ERROR");
//                            outputList.add(outputMap);
//                            
//                        }
//                    }else 
                        if (existCount==0) {
                        LinkedHashMap outputMap = new LinkedHashMap();
                        outputMap.put("BRANCH_ID", branchFromList);
                        outputMap.put("APPL_DATE", dateList);
                        outputMap.put("TASK_NAME", "DayEnd not started...");
                        outputMap.put("TASK_STATUS", "ERROR");
                        outputList.add(outputMap);
                    }
//                        else{
//                        //do nothing
//                    }
                    
                }
            }
        lst = ClientUtil.executeQuery("checkAllBranchDayEnd", passMap);
        outputList.addAll(lst);
        System.out.println("#$#$# outputList : "+outputList);
        StringBuffer out = CommonUtil.createHTML(outputList, true, false);
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
//        HashMap map = new HashMap();
//        List branchList = ClientUtil.executeQuery("checkAllBranchDayEnd", map) ;
//        StringBuffer out = CommonUtil.createHTML(branchList, true, false);
//        map = null ;
//        if (out.toString().length() == 0 ) {
//            out = CommonUtil.createHTMLNoData();
//        }
//        return out.toString() ;
       
    }
    
     public String executeTask(String lable,HashMap isPrev) throws Exception{
         HashMap dateMap = new HashMap();
		currDt = ClientUtil.getCurrentDate();
          if (isPrev.containsKey("BRANCH_LST")){
                branchList = (List)isPrev.get("BRANCH_LST");
              if (branchList!=null && branchList.size()>0) {
                for (int b=0;b<branchList.size();b++) {
                    dateMap = (HashMap)branchList.get(b);
//                    bran = CommonUtil.convertObjToStr(dateMap.get("BRANCH_CODE"));
                    presentTasks.append("'"+CommonUtil.convertObjToStr(dateMap.get("BRANCH_CODE"))+"'");
                    if((branchList.size() > 1) && (branchList.size()-1 != b))
                        presentTasks.append(",");
                       HashMap whereMap = new HashMap();
                       whereMap.put("BRANCH_CODE", CommonUtil.convertObjToStr(dateMap.get("BRANCH_CODE")));
                       List brnLst = (List) ClientUtil.executeQuery("getApplDateHashMap", whereMap);
                       whereMap = null;
                       if(brnLst!=null && brnLst.size() > 0){  
                            whereMap = (HashMap)brnLst.get(0);
                            currDt = (Date) whereMap.get("CURR_APPL_DT");
                       }
//                    currDt = ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(dateMap.get("BRANCH_CODE")));
//                    currDt = (Date)dateMap.get("CUR_BRAN_DT");
                }
            }
          }
        HashMap passMap = new HashMap();
        passMap.put("CURR_DT", currDt);
        passMap.put("BRANCH_ID", presentTasks);
        ArrayList outputList = new ArrayList();
        List lst = ClientUtil.executeQuery("checkAllBranchDayEndStartedOrNot", passMap);
        LinkedHashMap dayEndBranchMap = null;
        if (lst!=null && lst.size()>0) {
            dayEndBranchMap = (LinkedHashMap)lst.get(0);
            System.out.println("#$#$# dayEndBranchList : "+lst);
        } else
            System.out.println("#$#$# dayEndBranchList is null");

            String branchFromList = "";
            String dateList = "";
            if (branchList!=null && branchList.size()>0) {
                for (int b=0;b<branchList.size();b++) {
                    HashMap branchMap = (HashMap) branchList.get(b);
                    branchFromList = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                    dateList = CommonUtil.convertObjToStr(branchMap.get("CUR_BRAN_DT"));
                    int existCount=0;
                    if (lst!=null && lst.size()>0)
                    for (int i=0; i<lst.size(); i++) {
                        dayEndBranchMap = (LinkedHashMap) lst.get(i);
                        String dayEndBranch = CommonUtil.convertObjToStr(dayEndBranchMap.get("BRANCH_ID"));
                        int cnt = CommonUtil.convertObjToInt(dayEndBranchMap.get("CNT"));
                        if (branchFromList.equals(dayEndBranch) && cnt>0) {
                            existCount++;
                            HashMap mapData = new HashMap();
                            mapData.put("DAYEND_DT", currDt);
                            mapData.put("BRANCH_ID", branchFromList);
                            List lstData = ((List)ClientUtil.executeQuery("getSelectBranchCompletedLst", mapData));
                            mapData = null;
                            if(lstData != null && lstData.size() > 0){
                            for(int j=0;j<lstData.size();j++){
                                mapData = (HashMap)lstData.get(j);
//                                LinkedHashMap outputMap = new LinkedHashMap();
//                                outputMap.put("BRANCH_ID", branchFromList);
//                                outputMap.put("TASK_NAME", "DayEnd not completed...");
//                                outputMap.put("TASK_STATUS", "ERROR");
//                                outputList.add(outputMap);
                            }
                            }else{
                                LinkedHashMap outputMap = new LinkedHashMap();
                                outputMap.put("BRANCH_ID", branchFromList);
                                outputMap.put("APPL_DATE", dateList);
                                outputMap.put("TASK_NAME", "Branch DayEnd not completed...");
                                outputMap.put("TASK_STATUS", "ERROR");
                                outputList.add(outputMap);
                            }
                        }
                    }
//                    HashMap mapData = new HashMap();
//                    mapData.put("DAYEND_DT", currDt.clone());
//                    mapData.put("BRANCH_ID", branchFromList);
//                    List lstData = ((List)ClientUtil.executeQuery("getSelectBranchCompletedLst", mapData));
//                    mapData = null;
//                    if(lstData != null && lstData.size() > 0){
//                        for(int i=0;i<lstData.size();i++){
//                            mapData = (HashMap)lstData.get(i);
//                            LinkedHashMap outputMap = new LinkedHashMap();
//                            outputMap.put("BRANCH_ID", branchFromList);
//                            outputMap.put("TASK_NAME", "DayEnd not completed...");
//                            outputMap.put("TASK_STATUS", "ERROR");
//                            outputList.add(outputMap);
//                            
//                        }
//                    }else 
                        if (existCount==0) {
                        LinkedHashMap outputMap = new LinkedHashMap();
                        outputMap.put("BRANCH_ID", branchFromList);
                        outputMap.put("APPL_DATE", dateList);
                        outputMap.put("TASK_NAME", "DayEnd not started...");
                        outputMap.put("TASK_STATUS", "ERROR");
                        outputList.add(outputMap);
                    }
//                        else{
//                        //do nothing
//                    }
                    
                }
            }
        lst = ClientUtil.executeQuery("checkAllBranchDayEnd", passMap);
        outputList.addAll(lst);
        System.out.println("#$#$# outputList : "+outputList);
        StringBuffer out = CommonUtil.createHTML(outputList, true, false);
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
//        HashMap map = new HashMap();
//        List branchList = ClientUtil.executeQuery("checkAllBranchDayEnd", map) ;
//        StringBuffer out = CommonUtil.createHTML(branchList, true, false);
//        map = null ;
//        if (out.toString().length() == 0 ) {
//            out = CommonUtil.createHTMLNoData();
//        }
//        return out.toString() ;
       
    }
       
    public static void main(String args[]){
        try{
            AllBranchDayEndCheck ft = new AllBranchDayEndCheck();
            System.out.println (ft.executeTask());
            //ft.executeTask();
        }catch(Exception E){
            E.printStackTrace();
        }
    }
}
