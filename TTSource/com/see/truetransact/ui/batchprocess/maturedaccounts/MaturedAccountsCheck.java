/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * MaturedAccountsCheck.java
 *
 * Created on MAR 1, 2005, 1:45 AM
 */

package com.see.truetransact.ui.batchprocess.maturedaccounts;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil ;
import com.see.truetransact.commonutil.DateUtil ;
import com.see.truetransact.clientutil.ClientUtil ;
import com.see.truetransact.commonutil.TTException ;
import com.see.truetransact.ui.TrueTransactMain ;
import java.util.HashMap;
import java.util.Iterator ;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author  Sunil (152691)
 */
public class MaturedAccountsCheck{
    HashMap vaultMap = new HashMap();
    private Date currDt = null;
    /** Creates a new instance of CashTallyCheck */
    public MaturedAccountsCheck() throws Exception {
    }
    
    
    
    public String executeTask() throws Exception {
        /** Formaula to be employed
         * Vault sum(payment) + Cash sum(receipts) - Cash sum(payments) = Vault sum(receipt)
         *
         */
		currDt = ClientUtil.getCurrentDate();
        HashMap dataMap = new HashMap();
        HashMap paramMap = new HashMap();
        //paramMap.put("BRANCH_CODE", "BRAN");
        paramMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        paramMap.put("TODAY_DT", currDt.clone());
        System.out.println("===>>>===> paramMap in CTC UI : " + paramMap );
        ArrayList finalList = new ArrayList();
        HashMap tempMap = null ;
        HashMap hashData = null ;
        List outputList = null ;
        List vaultCashList=null;
        List CashBoxList=null;
        List cashmoveList=null;
        List crDrList=null;
        
        List lst = null;
        List lstData = null;
        HashMap data = null;
        
        double amt = 0;
        double glcurbal = 0;
        double openingCount = 0;
        
        
        lst = ClientUtil.executeQuery("getMaturedAccounts", paramMap);
        
        if(lst != null)
            if(lst.size() > 0){
                for(int a = 0, b = lst.size(); a < b; a++){
                    data=(HashMap)lst.get(a);
                    vaultMap.put("DEPOSIT_NO", data.get("DEPOSIT_NO"));
                    vaultMap.put("DEPOSIT_DT", data.get("DEPOSIT_DT"));
                    vaultMap.put("DEPOSIT_AMT", data.get("DEPOSIT_AMT"));
                    vaultMap.put("MATURITY_DT", data.get("MATURITY_DT"));
                    vaultMap.put("INT_PAY_ACC_NO", data.get("INT_PAY_ACC_NO"));
                    if(vaultMap.size() != 0){
                        finalList.add(vaultMap);
                        
                    }
                    vaultMap = new HashMap();
                }
                
                System.out.println("vaultMap"+vaultMap);
                if(vaultMap.size()>0){
                    //STATUS ERROR
                }
                
            }else{
                //STATUS COMPLETED
            }
        
        StringBuffer out = CommonUtil.createHTML(finalList, true, true);
        
        return out.toString();
    }
    
    public String executeTask(String lable) throws Exception {
        /** Formaula to be employed
         * Vault sum(payment) + Cash sum(receipts) - Cash sum(payments) = Vault sum(receipt)
         *
         */
		currDt = ClientUtil.getCurrentDate();
        HashMap dataMap = new HashMap();
        HashMap paramMap = new HashMap();
        //paramMap.put("BRANCH_CODE", "BRAN");
        paramMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        paramMap.put("TODAY_DT", currDt.clone());
        System.out.println("===>>>===> paramMap in CTC UI : " + paramMap );
        ArrayList finalList = new ArrayList();
        HashMap tempMap = null ;
        HashMap hashData = null ;
        List outputList = null ;
        List lst = null;
        List lstData = null;
        HashMap data = null;
        List  crDrList=null;
        double amt = 0;
        double glcurbal = 0;
        double openingCount = 0;
        lst = ClientUtil.executeQuery("getMaturedAccounts", paramMap);
        //        System.out.println("@@@@@lst"+lst);
        if(lst != null)
            if(lst.size() > 0){
                for(int a = 0, b = lst.size(); a < b; a++){
                    data = (HashMap) lst.get(a);
                    vaultMap.put("DEPOSIT_NO", data.get("DEPOSIT_NO"));
                    vaultMap.put("DEPOSIT_DT", data.get("DEPOSIT_DT"));
                    vaultMap.put("DEPOSIT_AMT", data.get("DEPOSIT_AMT"));
                    vaultMap.put("MATURITY_DT", data.get("MATURITY_DT"));
                    vaultMap.put("INT_PAY_ACC_NO", data.get("INT_PAY_ACC_NO"));
                    if(vaultMap.size() != 0){
                        finalList.add(vaultMap);
                        
                    }
                    vaultMap = new HashMap();
                }
                
                System.out.println("vaultMap"+vaultMap);
                if(vaultMap.size()>0){
                    //STATUS ERROR
                }
                
            }else{
                //STATUS COMPLETED
            }
        
        StringBuffer out = CommonUtil.createHTML(finalList, true, true);
        HashMap dataMap1 = new HashMap();
        dataMap1.put("ERROR_DATE", currDt.clone());
        dataMap1.put("TASK_NAME", lable);
        List output = ClientUtil.executeQuery("getSelectError_showing", dataMap1) ;
        dataMap1 = null;
        if(output!=null && output.size() > 0){
            String strData = "a";
            out.append(CommonUtil.createHTML(output,true,true,strData));
        }else{
            out.append(CommonUtil.createHTML(output,true,true));
        }
        
        return out.toString();
    }
    public String executeTask(String lable,HashMap isPrev) throws Exception {
        currDt = ClientUtil.getCurrentDate();
        HashMap dataMap = new HashMap();
        HashMap paramMap = new HashMap();
        //paramMap.put("BRANCH_CODE", "BRAN");
        paramMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        paramMap.put("TODAY_DT", currDt.clone());
        System.out.println("===>>>===> paramMap in CTC UI : " + paramMap );
        ArrayList finalList = new ArrayList();
        HashMap tempMap = null ;
        HashMap hashData = null ;
        List outputList = null ;
        List lst = null;
        List lstData = null;
        HashMap data = null;
        List crDrList=null;
        double amt = 0;
        double glcurbal = 0;
        double openingCount = 0;
        
        
        lst = ClientUtil.executeQuery("getMaturedAccounts", paramMap);
        //        System.out.println("@@@@@lst"+lst);
        if(lst != null)
            if(lst.size() > 0){
                for(int a = 0, b = lst.size(); a < b; a++){
                    data = (HashMap) lst.get(a);
                    vaultMap.put("DEPOSIT_NO", data.get("DEPOSIT_NO"));
                    vaultMap.put("DEPOSIT_DT", data.get("DEPOSIT_DT"));
                    vaultMap.put("DEPOSIT_AMT", data.get("DEPOSIT_AMT"));
                    vaultMap.put("MATURITY_DT", data.get("MATURITY_DT"));
                    vaultMap.put("INT_PAY_ACC_NO", data.get("INT_PAY_ACC_NO"));
                    if(vaultMap.size() != 0){
                        finalList.add(vaultMap);
                        //                          System.out.println("%%%%finalList"+finalList);
                    }
                    vaultMap = new HashMap();
                }
                
                System.out.println("vaultMap"+vaultMap);
                if(vaultMap.size()>0){
                    //STATUS ERROR
                }
                
            }else{
                //STATUS COMPLETED
            }
        
        StringBuffer out = CommonUtil.createHTML(finalList, true, true);
        HashMap dataMap1 = new HashMap();
        dataMap1.put("ERROR_DATE", currDt.clone());
        dataMap1.put("TASK_NAME", lable);
        List output = ClientUtil.executeQuery("getSelectError_showing", dataMap1) ;
        dataMap1 = null;
        if(output!=null && output.size() > 0){
            String strData = "a";
            out.append(CommonUtil.createHTML(output,true,true,strData));
        }else{
            out.append(CommonUtil.createHTML(output,true,true));
        }
        
        return out.toString();
    }
    
    public static void main(String args[]){
        try{
            MaturedAccountsCheck ft = new MaturedAccountsCheck();
            System.out.println(ft.executeTask());
        }catch(Exception E){
            E.printStackTrace();
        }
    }
}
