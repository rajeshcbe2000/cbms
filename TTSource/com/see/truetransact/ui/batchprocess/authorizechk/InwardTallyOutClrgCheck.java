/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * InwardTallyOutClrgCheck.java
 *
 * Created on MAR 1, 2005, 1:45 AM
 */

package com.see.truetransact.ui.batchprocess.authorizechk;
import com.see.truetransact.commonutil.DateUtil ;
import com.see.truetransact.commonutil.CommonUtil ;
import com.see.truetransact.clientutil.ClientUtil ;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import com.see.truetransact.ui.TrueTransactMain ;
import java.util.Date;
/**
 *
 * @author  Sunil (152691)
 */
public class InwardTallyOutClrgCheck{
    private Date currDt = null;
    /** Creates a new instance of InwardTallyOutClrgCheck */
    public InwardTallyOutClrgCheck() throws Exception {
    }
    
    
    public String executeTask() throws Exception{
        currDt = ClientUtil.getCurrentDate();
        HashMap paramMap = new HashMap();
        paramMap.put("TODAY_DT", currDt.clone());
        paramMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        System.out.println("paramMap : " + paramMap);
        ArrayList output = new ArrayList();
        List inwardTally = ClientUtil.executeQuery("getInwardTally", paramMap);
        List clearingTally = ClientUtil.executeQuery("getClearingTally", paramMap);
        HashMap inwardMap = null ;
        HashMap clearingMap = null ;
        boolean found = false ;
        for(int inwardCount= 0,inwardTotalCount= inwardTally.size(); inwardCount< inwardTotalCount;inwardCount++){
            inwardMap = (HashMap)inwardTally.get(inwardCount) ;
            for(int clearingCount= 0,clearingTotalCount= clearingTally.size(); clearingCount< clearingTotalCount;clearingCount++){
                
                clearingMap = (HashMap)clearingTally.get(clearingCount) ;
                //System.out.println("clearingMap " + inwardCount + " = " + clearingMap);
                if(clearingMap.get("SCHEDULE_NO").equals(inwardMap.get("SCHEDULE_NO")) &&
                clearingMap.get("CLEARING_TYPE").equals(inwardMap.get("CLEARING_TYPE"))){
                    if(clearingMap.get("PHY_AMT").equals(inwardMap.get("PHY_AMT")) &&
                    clearingMap.get("PHY_COUNT").equals(inwardMap.get("PHY_COUNT"))){
                        clearingMap.put("FOUND", "true");
                        inwardMap.put("FOUND", "true");
                        break ;
                    }
                }
                else{
                    if(clearingMap.get("FOUND")== null)
                        clearingMap.put("FOUND", "false");
                    else if(!clearingMap.get("FOUND").equals("true"))
                        clearingMap.put("FOUND", "false");
                }
                
            }
            if(inwardMap.get("FOUND")== null)
                inwardMap.put("FOUND", "false");
            else if(!inwardMap.get("FOUND").equals("true"))
                inwardMap.put("FOUND", "false");
        }
        
        for(int inwardCount= 0,inwardTotalCount= inwardTally.size(); inwardCount< inwardTotalCount;inwardCount++){
            if(((HashMap)inwardTally.get(inwardCount)).containsKey("FOUND"))
                if(((HashMap)inwardTally.get(inwardCount)).get("FOUND").equals("false")){
                    ((HashMap)inwardTally.get(inwardCount)).remove("FOUND");
                    output.add(inwardTally.get(inwardCount));
                }
        }
        
        for(int clearingCount= 0,clearingTotalCount= clearingTally.size(); clearingCount< clearingTotalCount;clearingCount++){
            if(((HashMap)clearingTally.get(clearingCount)).containsKey("FOUND"))
                if(((HashMap)clearingTally.get(clearingCount)).get("FOUND").equals("false")){
                    found = false ;
                    for(int i =0, j = output.size();i < j; i++){
                        if(((HashMap)clearingTally.get(clearingCount)).get("SCHEDULE_NO").equals(
                        ((HashMap)output.get(i)).get("SCHEDULE_NO")) &&
                        ((HashMap)clearingTally.get(clearingCount)).get("CLEARING_TYPE").equals(
                        ((HashMap)output.get(i)).get("CLEARING_TYPE"))){
                            found = true ;
                            break ;
                        }
                    }
                } else {
                    found = true;
                }
            if(found == false){
                ((HashMap)clearingTally.get(clearingCount)).remove("FOUND");
                output.add(clearingTally.get(clearingCount));
            }
        }
        
        
        paramMap = null ;
        StringBuffer out = CommonUtil.createHTML(output) ;
        if (out.toString().length() == 0 ) {
            out = CommonUtil.createHTMLNoData();
        }
        return out.toString() ;
    }
     public String executeTask(String lable) throws Exception{
        HashMap paramMap = new HashMap();
		currDt = ClientUtil.getCurrentDate();
        paramMap.put("TODAY_DT", currDt.clone());
        paramMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        System.out.println("paramMap : " + paramMap);
        ArrayList output = new ArrayList();
        List inwardTally = ClientUtil.executeQuery("getInwardTally", paramMap);
        List clearingTally = ClientUtil.executeQuery("getClearingTally", paramMap);
        HashMap inwardMap = null ;
        HashMap clearingMap = null ;
        boolean found = false ;
        for(int inwardCount= 0,inwardTotalCount= inwardTally.size(); inwardCount< inwardTotalCount;inwardCount++){
            inwardMap = (HashMap)inwardTally.get(inwardCount) ;
            for(int clearingCount= 0,clearingTotalCount= clearingTally.size(); clearingCount< clearingTotalCount;clearingCount++){
                
                clearingMap = (HashMap)clearingTally.get(clearingCount) ;
                //System.out.println("clearingMap " + inwardCount + " = " + clearingMap);
                if(clearingMap.get("SCHEDULE_NO").equals(inwardMap.get("SCHEDULE_NO")) &&
                clearingMap.get("CLEARING_TYPE").equals(inwardMap.get("CLEARING_TYPE"))){
                    if(clearingMap.get("PHY_AMT").equals(inwardMap.get("PHY_AMT")) &&
                    clearingMap.get("PHY_COUNT").equals(inwardMap.get("PHY_COUNT"))){
                        clearingMap.put("FOUND", "true");
                        inwardMap.put("FOUND", "true");
                        break ;
                    }
                }
                else{
                    if(clearingMap.get("FOUND")== null)
                        clearingMap.put("FOUND", "false");
                    else if(!clearingMap.get("FOUND").equals("true"))
                        clearingMap.put("FOUND", "false");
                }
                
            }
            if(inwardMap.get("FOUND")== null)
                inwardMap.put("FOUND", "false");
            else if(!inwardMap.get("FOUND").equals("true"))
                inwardMap.put("FOUND", "false");
        }
        
        for(int inwardCount= 0,inwardTotalCount= inwardTally.size(); inwardCount< inwardTotalCount;inwardCount++){
            if(((HashMap)inwardTally.get(inwardCount)).containsKey("FOUND"))
                if(((HashMap)inwardTally.get(inwardCount)).get("FOUND").equals("false")){
                    ((HashMap)inwardTally.get(inwardCount)).remove("FOUND");
                    output.add(inwardTally.get(inwardCount));
                }
        }
        
        for(int clearingCount= 0,clearingTotalCount= clearingTally.size(); clearingCount< clearingTotalCount;clearingCount++){
            if(((HashMap)clearingTally.get(clearingCount)).containsKey("FOUND"))
                if(((HashMap)clearingTally.get(clearingCount)).get("FOUND").equals("false")){
                    found = false ;
                    for(int i =0, j = output.size();i < j; i++){
                        if(((HashMap)clearingTally.get(clearingCount)).get("SCHEDULE_NO").equals(
                        ((HashMap)output.get(i)).get("SCHEDULE_NO")) &&
                        ((HashMap)clearingTally.get(clearingCount)).get("CLEARING_TYPE").equals(
                        ((HashMap)output.get(i)).get("CLEARING_TYPE"))){
                            found = true ;
                            break ;
                        }
                    }
                } else {
                    found = true;
                }
            if(found == false){
                ((HashMap)clearingTally.get(clearingCount)).remove("FOUND");
                output.add(clearingTally.get(clearingCount));
            }
        }
        
        
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
      public String executeTask(String lable,HashMap isPrev){
            HashMap paramMap = new HashMap();
		currDt = ClientUtil.getCurrentDate();
        paramMap.put("TODAY_DT", currDt.clone());
        paramMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        System.out.println("paramMap : " + paramMap);
        ArrayList output = new ArrayList();
        List inwardTally = ClientUtil.executeQuery("getInwardTally", paramMap);
        List clearingTally = ClientUtil.executeQuery("getClearingTally", paramMap);
        HashMap inwardMap = null ;
        HashMap clearingMap = null ;
        boolean found = false ;
        for(int inwardCount= 0,inwardTotalCount= inwardTally.size(); inwardCount< inwardTotalCount;inwardCount++){
            inwardMap = (HashMap)inwardTally.get(inwardCount) ;
            for(int clearingCount= 0,clearingTotalCount= clearingTally.size(); clearingCount< clearingTotalCount;clearingCount++){
                
                clearingMap = (HashMap)clearingTally.get(clearingCount) ;
                //System.out.println("clearingMap " + inwardCount + " = " + clearingMap);
                if(clearingMap.get("SCHEDULE_NO").equals(inwardMap.get("SCHEDULE_NO")) &&
                clearingMap.get("CLEARING_TYPE").equals(inwardMap.get("CLEARING_TYPE"))){
                    if(clearingMap.get("PHY_AMT").equals(inwardMap.get("PHY_AMT")) &&
                    clearingMap.get("PHY_COUNT").equals(inwardMap.get("PHY_COUNT"))){
                        clearingMap.put("FOUND", "true");
                        inwardMap.put("FOUND", "true");
                        break ;
                    }
                }
                else{
                    if(clearingMap.get("FOUND")== null)
                        clearingMap.put("FOUND", "false");
                    else if(!clearingMap.get("FOUND").equals("true"))
                        clearingMap.put("FOUND", "false");
                }
                
            }
            if(inwardMap.get("FOUND")== null)
                inwardMap.put("FOUND", "false");
            else if(!inwardMap.get("FOUND").equals("true"))
                inwardMap.put("FOUND", "false");
        }
        
        for(int inwardCount= 0,inwardTotalCount= inwardTally.size(); inwardCount< inwardTotalCount;inwardCount++){
            if(((HashMap)inwardTally.get(inwardCount)).containsKey("FOUND"))
                if(((HashMap)inwardTally.get(inwardCount)).get("FOUND").equals("false")){
                    ((HashMap)inwardTally.get(inwardCount)).remove("FOUND");
                    output.add(inwardTally.get(inwardCount));
                }
        }
        
        for(int clearingCount= 0,clearingTotalCount= clearingTally.size(); clearingCount< clearingTotalCount;clearingCount++){
            if(((HashMap)clearingTally.get(clearingCount)).containsKey("FOUND"))
                if(((HashMap)clearingTally.get(clearingCount)).get("FOUND").equals("false")){
                    found = false ;
                    for(int i =0, j = output.size();i < j; i++){
                        if(((HashMap)clearingTally.get(clearingCount)).get("SCHEDULE_NO").equals(
                        ((HashMap)output.get(i)).get("SCHEDULE_NO")) &&
                        ((HashMap)clearingTally.get(clearingCount)).get("CLEARING_TYPE").equals(
                        ((HashMap)output.get(i)).get("CLEARING_TYPE"))){
                            found = true ;
                            break ;
                        }
                    }
                } else {
                    found = true;
                }
            if(found == false){
                ((HashMap)clearingTally.get(clearingCount)).remove("FOUND");
                output.add(clearingTally.get(clearingCount));
            }
        }
        
        
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
            InwardTallyOutClrgCheck ft = new InwardTallyOutClrgCheck();
            System.out.println(ft.executeTask());
        }catch(Exception E){
            E.printStackTrace();
        }
    }
}
