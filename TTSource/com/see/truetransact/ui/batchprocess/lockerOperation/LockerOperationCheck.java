/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LockerOperationCheck.java
 *
 * Created on MAR 1, 2005, 1:45 AM
 */

package com.see.truetransact.ui.batchprocess.lockerOperation;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil ;
import com.see.truetransact.commonutil.DateUtil ;
import com.see.truetransact.clientutil.ClientUtil ;
import com.see.truetransact.commonutil.TTException ;
import com.see.truetransact.ui.TrueTransactMain ;
import java.util.*;


/**
 *
 * @author  Sunil (152691)
 */
public class LockerOperationCheck{
    LinkedHashMap vaultMap = new LinkedHashMap();
    private Date currDt = null;
    /** Creates a new instance of CashTallyCheck */
    public LockerOperationCheck() throws Exception {
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
        
        
        lst = ClientUtil.executeQuery("getLockerOp", paramMap);
        
        if(lst != null)
            if(lst.size() > 0){
                for(int a = 0, b = lst.size(); a < b; a++){
                    data=(HashMap)lst.get(a);
                    vaultMap.put("OPERATION_ID", data.get("OPERATION_ID"));
                    vaultMap.put("LOCKER_NUM", data.get("LOCKER_NUM"));
                    vaultMap.put("OPERATION_DT", data.get("OPERATION_DT"));
                    vaultMap.put("CUST_ID", data.get("CUST_ID")); 
                    vaultMap.put("NAME", data.get("NAME")); 
                    if(vaultMap.size() != 0){
                        finalList.add(vaultMap);
                        
                    }
                    vaultMap = new LinkedHashMap();
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
        lst = ClientUtil.executeQuery("getLockerOp", paramMap);
        //        System.out.println("@@@@@lst"+lst);
        if(lst != null)
            if(lst.size() > 0){
                for(int a = 0, b = lst.size(); a < b; a++){
                    data = (HashMap) lst.get(a);
                    vaultMap.put("OPERATION_ID", data.get("OPERATION_ID"));
                    vaultMap.put("LOCKER_NUM", data.get("LOCKER_NUM"));
                    vaultMap.put("OPERATION_DT", data.get("OPERATION_DT"));
                    vaultMap.put("CUST_ID", data.get("CUST_ID")); 
                    vaultMap.put("NAME", data.get("NAME")); 
                    if(vaultMap.size() != 0){
                        finalList.add(vaultMap);
                        
                    }
                    vaultMap = new LinkedHashMap();
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
        dataMap1.put("ERROR_DATE", currDt);
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
        
        
        lst = ClientUtil.executeQuery("getLockerOp", paramMap);
        //        System.out.println("@@@@@lst"+lst);
        if(lst != null)
            if(lst.size() > 0){
                for(int a = 0, b = lst.size(); a < b; a++){
                    data = (HashMap) lst.get(a);
                    vaultMap.put("OPERATION_ID", data.get("OPERATION_ID"));
                    vaultMap.put("LOCKER_NUM", data.get("LOCKER_NUM"));
                    vaultMap.put("OPERATION_DT", data.get("OPERATION_DT"));
                    vaultMap.put("CUST_ID", data.get("CUST_ID")); 
                    vaultMap.put("NAME", data.get("NAME")); 
                    if(vaultMap.size() != 0){
                        finalList.add(vaultMap);
                        //                          System.out.println("%%%%finalList"+finalList);
                    }
                    vaultMap = new LinkedHashMap();
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
            LockerOperationCheck ft = new LockerOperationCheck();
            System.out.println(ft.executeTask());
        }catch(Exception E){
            E.printStackTrace();
        }
    }
}
