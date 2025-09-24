/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * CashTallyCheck.java
 *
 * Created on MAR 1, 2005, 1:45 AM
 */

package com.see.truetransact.ui.batchprocess.cashtally;

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
public class CashTallyCheck{
    HashMap vaultMap = new HashMap();
    private Date currDt = null;
    /** Creates a new instance of CashTallyCheck */
    public CashTallyCheck() throws Exception {
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
        
//        outputList = ClientUtil.executeQuery("getVaultCashTallyAmount", paramMap);
//        for(int i = 0, j = outputList.size() ; i < j ; i++){
//            amt = 0 ;
//            tempMap = (HashMap)outputList.get(i);
//            amt = CommonUtil.convertObjToDouble(tempMap.get("VAULT_RECEIPT")).doubleValue()
//            + CommonUtil.convertObjToDouble(tempMap.get("CASH_BOX_BALANCE")).doubleValue()
//            - CommonUtil.convertObjToDouble(tempMap.get("VAULT_PAYMENT")).doubleValue() ;
//            
//            vaultMap.put(tempMap.get("RECEIVED_CASHIER_ID"), new Double(amt)) ;
//        }
//        outputList = ClientUtil.executeQuery("getCashMovementTallyAmount", paramMap);
//        
//        for(int k = 0, l = outputList.size() ; k < l ; k++){
//            amt =0 ;
//            tempMap = (HashMap)outputList.get(k);
//            amt =
//                CommonUtil.convertObjToDouble(tempMap.get("CREDIT")).doubleValue()
//                - CommonUtil.convertObjToDouble(tempMap.get("DEBIT")).doubleValue() ;
//            
//            if(vaultMap.containsKey(tempMap.get("INIT_TRANS_ID")))
//                amt = amt + CommonUtil.convertObjToDouble(vaultMap.get(tempMap.get("INIT_TRANS_ID"))).doubleValue() ;
//                
//            vaultMap.put(tempMap.get("INIT_TRANS_ID"), new Double(amt)) ;
//        }
//        
//    Iterator vaultKeys = vaultMap.keySet().iterator();
//    String key = null ;
//    while (vaultKeys.hasNext()){
//        key = (String)vaultKeys.next();
//        if(CommonUtil.convertObjToDouble(vaultMap.get(key)).doubleValue() == 0)
//            vaultMap.remove(key);
//    }
        lst = ClientUtil.executeQuery("getCashMovementDetails", paramMap);
//        System.out.println("@@@@@lst"+lst);
        if(lst != null)
            if(lst.size() > 0){
                for(int a = 0, b = lst.size(); a < b; a++){
                    amt = 0;
                    data = (HashMap) lst.get(a);
                    paramMap.put("USER_ID", data.get("RECEIVED_CASHIER_ID"));
//                    System.out.println("@@@@@paramMap****"+paramMap);
                    
                    crDrList=ClientUtil.executeQuery("getcrDrCheckTally", paramMap);                  
                    outputList = ClientUtil.executeQuery("getVaultCheckTally", paramMap);
                  
                
                   
//                    System.out.println("@@@@@outputList"+outputList);
                    for(int i = 0, j = outputList.size() ; i < j ; i++){
                        
                        String receivID = "";
                        tempMap = (HashMap)outputList.get(i);
//                        System.out.println("tempMap****^^*"+tempMap);
                        amt = amt+CommonUtil.convertObjToDouble(tempMap.get("DENOMINATION_TOTAL")).doubleValue();
//                        if(amt==0){}
//                        else{
//                            receivID = CommonUtil.convertObjToStr(data.get("RECEIVED_CASHIER_ID"));
//                            if(vaultMap.containsKey("RECEIVED_CASHIER_ID") && vaultMap.containsValue(receivID))
//                                amt = amt + CommonUtil.convertObjToDouble(vaultMap.get("DENOMINATION_TOTAL")).doubleValue();
//                            vaultMap.put("RECEIVED_CASHIER_ID", data.get("RECEIVED_CASHIER_ID"));
//                            vaultMap.put("DENOMINATION_TOTAL", new Double(amt));
//                        }
//                        System.out.println("%%%%vaultMap"+vaultMap);
                    }
                    if(crDrList!=null && crDrList.size()>0){
                        double cramt=0.0;
                        double dramt=0.0;
                        
                        for(int i=0;i<crDrList.size();i++){
                            HashMap crdrmap=(HashMap)crDrList.get(i);
                            if(crdrmap.get("TRANS_TYPE").equals("CREDIT")){
                                cramt=cramt+CommonUtil.convertObjToDouble(crdrmap.get("INP_AMOUNT")).doubleValue();
                                
                            }
                             if(crdrmap.get("TRANS_TYPE").equals("DEBIT")){
                                 dramt=dramt+CommonUtil.convertObjToDouble(crdrmap.get("INP_AMOUNT")).doubleValue();
                             }
                            
                        }
                        amt=amt+(cramt-dramt);
                    }
                      if(amt==0){}else{
                            vaultMap.put("RECEIVED_CASHIER_ID", data.get("RECEIVED_CASHIER_ID"));
                            vaultMap.put("DENOMINATION_TOTAL", new Double(amt));
                    }

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
                else{
                    lst = ClientUtil.executeQuery("getDenoValueCount", paramMap);
                    lstData = ClientUtil.executeQuery("getCurbalGl", paramMap);
                    if(lst != null)
                    if(lst.size()>0 && lstData.size()>0){
                        dataMap = (HashMap)lst.get(0);
                        openingCount = CommonUtil.convertObjToDouble(dataMap.get("OPENING_COUNT")).doubleValue();
                        System.out.println("openingCount"+openingCount);
                        hashData = (HashMap)lstData.get(0);
                        glcurbal = CommonUtil.convertObjToDouble(hashData.get("CUR_BAL")).doubleValue();
                        System.out.println("glcurbal"+glcurbal);
                        if((openingCount == glcurbal) && (vaultMap.size()<=0)){
                            //STATUS COMPLETED
                        }
                        else{
                            vaultMap.put("OPENING COUNT", new Double(openingCount));
                            vaultMap.put("GL CURRENT BALANCE", new Double(glcurbal));
                            if(vaultMap.size() != 0)
                                finalList.add(vaultMap);
                        }
                    }else{
                        //STATUS COMPLETED
                    }
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
        
//        outputList = ClientUtil.executeQuery("getVaultCashTallyAmount", paramMap);
//        for(int i = 0, j = outputList.size() ; i < j ; i++){
//            amt = 0 ;
//            tempMap = (HashMap)outputList.get(i);
//            amt = CommonUtil.convertObjToDouble(tempMap.get("VAULT_RECEIPT")).doubleValue()
//            + CommonUtil.convertObjToDouble(tempMap.get("CASH_BOX_BALANCE")).doubleValue()
//            - CommonUtil.convertObjToDouble(tempMap.get("VAULT_PAYMENT")).doubleValue() ;
//            
//            vaultMap.put(tempMap.get("RECEIVED_CASHIER_ID"), new Double(amt)) ;
//        }
//        outputList = ClientUtil.executeQuery("getCashMovementTallyAmount", paramMap);
//        
//        for(int k = 0, l = outputList.size() ; k < l ; k++){
//            amt =0 ;
//            tempMap = (HashMap)outputList.get(k);
//            amt =
//                CommonUtil.convertObjToDouble(tempMap.get("CREDIT")).doubleValue()
//                - CommonUtil.convertObjToDouble(tempMap.get("DEBIT")).doubleValue() ;
//            
//            if(vaultMap.containsKey(tempMap.get("INIT_TRANS_ID")))
//                amt = amt + CommonUtil.convertObjToDouble(vaultMap.get(tempMap.get("INIT_TRANS_ID"))).doubleValue() ;
//                
//            vaultMap.put(tempMap.get("INIT_TRANS_ID"), new Double(amt)) ;
//        }
//        
//    Iterator vaultKeys = vaultMap.keySet().iterator();
//    String key = null ;
//    while (vaultKeys.hasNext()){
//        key = (String)vaultKeys.next();
//        if(CommonUtil.convertObjToDouble(vaultMap.get(key)).doubleValue() == 0)
//            vaultMap.remove(key);
//    }
        lst = ClientUtil.executeQuery("getCashMovementDetails", paramMap);
//        System.out.println("@@@@@lst"+lst);
        if(lst != null)
            if(lst.size() > 0){
                for(int a = 0, b = lst.size(); a < b; a++){
                    amt = 0;
                    data = (HashMap) lst.get(a);
                    paramMap.put("USER_ID", data.get("RECEIVED_CASHIER_ID"));
//                    System.out.println("@@@@@paramMap****"+paramMap);
                    outputList = ClientUtil.executeQuery("getVaultCheckTally", paramMap);
                     crDrList=ClientUtil.executeQuery("getcrDrCheckTally", paramMap);              
//                    System.out.println("@@@@@outputList"+outputList);
                    for(int i = 0, j = outputList.size() ; i < j ; i++){
                        
                        String receivID = "";
                        tempMap = (HashMap)outputList.get(i);
//                        System.out.println("tempMap****^^*"+tempMap);
                        amt = amt+CommonUtil.convertObjToDouble(tempMap.get("DENOMINATION_TOTAL")).doubleValue();
//                        if(amt==0){}
//                        else{
//                            receivID = CommonUtil.convertObjToStr(data.get("RECEIVED_CASHIER_ID"));
//                            if(vaultMap.containsKey("RECEIVED_CASHIER_ID") && vaultMap.containsValue(receivID))
//                                amt = amt + CommonUtil.convertObjToDouble(vaultMap.get("DENOMINATION_TOTAL")).doubleValue();
//                            vaultMap.put("RECEIVED_CASHIER_ID", data.get("RECEIVED_CASHIER_ID"));
//                            vaultMap.put("DENOMINATION_TOTAL", new Double(amt));
//                        }
//                        System.out.println("%%%%vaultMap"+vaultMap);
                    }
                     
                     
                       if(crDrList!=null && crDrList.size()>0){
                        double cramt=0.0;
                        double dramt=0.0;
                        
                        for(int i=0;i<crDrList.size();i++){
                            HashMap crdrmap=(HashMap)crDrList.get(i);
                            if(crdrmap.get("TRANS_TYPE").equals("CREDIT")){
                                cramt=cramt+CommonUtil.convertObjToDouble(crdrmap.get("INP_AMOUNT")).doubleValue();
                                
                            }
                             if(crdrmap.get("TRANS_TYPE").equals("DEBIT")){
                                 dramt=dramt+CommonUtil.convertObjToDouble(crdrmap.get("INP_AMOUNT")).doubleValue();
                             }
                            
                        }
                        amt=amt+(cramt-dramt);
                    }
                     if(amt==0){}else{
                          vaultMap.put("RECEIVED_CASHIER_ID", data.get("RECEIVED_CASHIER_ID"));
                            vaultMap.put("DENOMINATION_TOTAL", new Double(amt));
                    }

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
                else{
                    lst = ClientUtil.executeQuery("getDenoValueCount", paramMap);
                    lstData = ClientUtil.executeQuery("getCurbalGl", paramMap);
                    if(lst != null)
                    if(lst.size()>0 && lstData.size()>0){
                        dataMap = (HashMap)lst.get(0);
                        openingCount = CommonUtil.convertObjToDouble(dataMap.get("OPENING_COUNT")).doubleValue();
                        System.out.println("openingCount"+openingCount);
                        hashData = (HashMap)lstData.get(0);
                        glcurbal = CommonUtil.convertObjToDouble(hashData.get("CUR_BAL")).doubleValue();
                        System.out.println("glcurbal"+glcurbal);
                        if((openingCount == glcurbal) && (vaultMap.size()<=0)){
                            //STATUS COMPLETED
                        }
                        else{
                            vaultMap.put("OPENING COUNT", new Double(openingCount));
                            vaultMap.put("GL CURRENT BALANCE", new Double(glcurbal));
                            if(vaultMap.size() != 0)
                                finalList.add(vaultMap);
                        }
                    }else{
                        //STATUS COMPLETED
                    }
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
        List crDrList=null;
        double amt = 0;
        double glcurbal = 0;
        double openingCount = 0;
        
//        outputList = ClientUtil.executeQuery("getVaultCashTallyAmount", paramMap);
//        for(int i = 0, j = outputList.size() ; i < j ; i++){
//            amt = 0 ;
//            tempMap = (HashMap)outputList.get(i);
//            amt = CommonUtil.convertObjToDouble(tempMap.get("VAULT_RECEIPT")).doubleValue()
//            + CommonUtil.convertObjToDouble(tempMap.get("CASH_BOX_BALANCE")).doubleValue()
//            - CommonUtil.convertObjToDouble(tempMap.get("VAULT_PAYMENT")).doubleValue() ;
//            
//            vaultMap.put(tempMap.get("RECEIVED_CASHIER_ID"), new Double(amt)) ;
//        }
//        outputList = ClientUtil.executeQuery("getCashMovementTallyAmount", paramMap);
//        
//        for(int k = 0, l = outputList.size() ; k < l ; k++){
//            amt =0 ;
//            tempMap = (HashMap)outputList.get(k);
//            amt =
//                CommonUtil.convertObjToDouble(tempMap.get("CREDIT")).doubleValue()
//                - CommonUtil.convertObjToDouble(tempMap.get("DEBIT")).doubleValue() ;
//            
//            if(vaultMap.containsKey(tempMap.get("INIT_TRANS_ID")))
//                amt = amt + CommonUtil.convertObjToDouble(vaultMap.get(tempMap.get("INIT_TRANS_ID"))).doubleValue() ;
//                
//            vaultMap.put(tempMap.get("INIT_TRANS_ID"), new Double(amt)) ;
//        }
//        
//    Iterator vaultKeys = vaultMap.keySet().iterator();
//    String key = null ;
//    while (vaultKeys.hasNext()){
//        key = (String)vaultKeys.next();
//        if(CommonUtil.convertObjToDouble(vaultMap.get(key)).doubleValue() == 0)
//            vaultMap.remove(key);
//    }
        lst = ClientUtil.executeQuery("getCashMovementDetails", paramMap);
//        System.out.println("@@@@@lst"+lst);
        if(lst != null)
            if(lst.size() > 0){
                for(int a = 0, b = lst.size(); a < b; a++){
                    amt = 0;
                    data = (HashMap) lst.get(a);
                    paramMap.put("USER_ID", data.get("RECEIVED_CASHIER_ID"));
//                    System.out.println("@@@@@paramMap****"+paramMap);
                    outputList = ClientUtil.executeQuery("getVaultCheckTally", paramMap);
                    crDrList=ClientUtil.executeQuery("getcrDrCheckTally", paramMap); 
//                    System.out.println("@@@@@outputList"+outputList);
                    for(int i = 0, j = outputList.size() ; i < j ; i++){
                        
                        String receivID = "";
                        tempMap = (HashMap)outputList.get(i);
//                        System.out.println("tempMap****^^*"+tempMap);
                        amt = amt+CommonUtil.convertObjToDouble(tempMap.get("DENOMINATION_TOTAL")).doubleValue();
//                        if(amt==0){}
//                        else{
//                            receivID = CommonUtil.convertObjToStr(data.get("RECEIVED_CASHIER_ID"));
//                            if(vaultMap.containsKey("RECEIVED_CASHIER_ID") && vaultMap.containsValue(receivID))
//                                amt = amt + CommonUtil.convertObjToDouble(vaultMap.get("DENOMINATION_TOTAL")).doubleValue();
//                            vaultMap.put("RECEIVED_CASHIER_ID", data.get("RECEIVED_CASHIER_ID"));
//                            vaultMap.put("DENOMINATION_TOTAL", new Double(amt));
//                        }
//                        System.out.println("%%%%vaultMap"+vaultMap);
                    }
                      if(crDrList!=null && crDrList.size()>0){
                        double cramt=0.0;
                        double dramt=0.0;
                        
                        for(int i=0;i<crDrList.size();i++){
                            HashMap crdrmap=(HashMap)crDrList.get(i);
                            if(crdrmap.get("TRANS_TYPE").equals("CREDIT")){
                                cramt=cramt+CommonUtil.convertObjToDouble(crdrmap.get("INP_AMOUNT")).doubleValue();
                                
                            }
                             if(crdrmap.get("TRANS_TYPE").equals("DEBIT")){
                                 dramt=dramt+CommonUtil.convertObjToDouble(crdrmap.get("INP_AMOUNT")).doubleValue();
                             }
                            
                        }
                        amt=amt+(cramt-dramt);
                    }
                    if(amt==0){}else{
                            vaultMap.put("RECEIVED_CASHIER_ID", data.get("RECEIVED_CASHIER_ID"));
                            vaultMap.put("DENOMINATION_TOTAL", new Double(amt));
                    }

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
                else{
                    lst = ClientUtil.executeQuery("getDenoValueCount", paramMap);
                    lstData = ClientUtil.executeQuery("getCurbalGl", paramMap);
                    if(lst != null)
                    if(lst.size()>0 && lstData.size()>0){
                        dataMap = (HashMap)lst.get(0);
                        openingCount = CommonUtil.convertObjToDouble(dataMap.get("OPENING_COUNT")).doubleValue();
                        System.out.println("openingCount"+openingCount);
                        hashData = (HashMap)lstData.get(0);
                        glcurbal = CommonUtil.convertObjToDouble(hashData.get("CUR_BAL")).doubleValue();
                        System.out.println("glcurbal"+glcurbal);
                        if((openingCount == glcurbal) && (vaultMap.size()<=0)){
                            //STATUS COMPLETED
                        }
                        else{
                            vaultMap.put("OPENING COUNT", new Double(openingCount));
                            vaultMap.put("GL CURRENT BALANCE", new Double(glcurbal));
                            if(vaultMap.size() != 0)
                                finalList.add(vaultMap);
                        }
                    }else{
                        //STATUS COMPLETED
                    }
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
            CashTallyCheck ft = new CashTallyCheck();
            System.out.println (ft.executeTask());
        }catch(Exception E){
            E.printStackTrace();
        }
    }
}
