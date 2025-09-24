/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * UserCheck.java
 *
 * Created on Mar 26, 2005, 4:40 PM
 */

package com.see.truetransact.ui.batchprocess.authorizechk;

import java.util.HashMap;
import java.util.List;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import java.util.Date;
/**
 *
 * @author  Bala
 */
public class UserCheck {
    /** Creates a new instance of AuthorizationCheck */
    public UserCheck() throws Exception {
    }
    private Date currDt = null;
    public String executeTask() throws Exception{
        HashMap map = new HashMap();
        currDt = ClientUtil.getCurrentDate();
        map.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
        map.put("USER_ID", ProxyParameters.USER_ID);
        List lst = ClientUtil.executeQuery("getUserLogoutStatus", map) ;
        StringBuffer out = CommonUtil.createHTML(lst, true, true);
        if (out.toString().length() == 0 ) {
            out = CommonUtil.createHTMLNoData();
        }
        return out.toString() ;
    }
    public String executeTask(String lable) throws Exception{
        HashMap map = new HashMap();
        currDt = ClientUtil.getCurrentDate();
        map.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
        map.put("USER_ID", ProxyParameters.USER_ID);
        List lst = ClientUtil.executeQuery("getUserLogoutStatus", map) ;
        StringBuffer out = CommonUtil.createHTML(lst, true, true);
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
      public String executeTask(String lable,HashMap isPrev){
           HashMap map = new HashMap();
           currDt = ClientUtil.getCurrentDate();
        map.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
        map.put("USER_ID", ProxyParameters.USER_ID);
        List lst = ClientUtil.executeQuery("getUserLogoutStatus", map) ;
        StringBuffer out = CommonUtil.createHTML(lst, true, true);
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
            UserCheck ft = new UserCheck();
            System.out.println (ft.executeTask());
        }catch(Exception E){
            E.printStackTrace();
        }
    }
}