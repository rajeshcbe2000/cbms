/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DepositMaturingCheck.java
 *
 * Created on May 23, 2012, 04:52 PM
 * Author : Rajesh
 * Desc : The class is used to check for Matured Deposits on run date
 *        Is always run as a Batch, in DAY END process
 *        header should contain User id and Branch Id
 *
 *Map Used : DepositMap
 *
 *
 */

package com.see.truetransact.ui.batchprocess.authorizechk;

import com.see.truetransact.clientutil.ClientUtil ;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.ui.TrueTransactMain ;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import com.see.truetransact.commonutil.DateUtil;

/**
 *
 * @author  Sunil
 */
public class DepositMaturingCheck {
    
    private String branch ;
    private String dayEndType;
    private HashMap taskMap;
    private Date currDt;
    
    /** Creates a new instance of DepositMaturingCheckTask */
    public DepositMaturingCheck() throws Exception {
        branch = TrueTransactMain.BRANCH_ID;
        currDt = ClientUtil.getCurrentDate();
    }
    
    public String executeTask(String lable) throws Exception {
    	HashMap whereMap = new HashMap();
        StringBuffer out = new StringBuffer("");
        try{

            whereMap.put(CommonConstants.BRANCH_ID, branch);
            whereMap.put("TODAY_DT", currDt);
            ArrayList accountList = (ArrayList)ClientUtil.executeQuery("getLTDMaturedDepositAccounts", whereMap);
            if (accountList!=null && accountList.size()>0) {
                System.out.println("#$#$ accountList:" + accountList);
                out = CommonUtil.createHTML(accountList, true, true);
                accountList.clear();
            } else {
                out = CommonUtil.createHTMLNoData();
            }
            accountList = null;
            whereMap.clear();
            whereMap = null;
        }catch(Exception e){
            e.printStackTrace();
        }
        return out.toString();

    }
    
    public static void main(String arg[]) {
        try {
            TrueTransactMain.BRANCH_ID ="0001";
            DepositMaturingCheck ft = new DepositMaturingCheck();
            System.out.println(ft.executeTask("test"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
