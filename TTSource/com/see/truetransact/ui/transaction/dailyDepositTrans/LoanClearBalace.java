/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LoanClearBalace.java
 */

package com.see.truetransact.ui.transaction.dailyDepositTrans;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.common.viewall.SelectAllDAO;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author BABU
 */
public class LoanClearBalace {
        private Date currentDate;
           private String actBranch;
        public LoanClearBalace()
                {
                      currentDate = ClientUtil.getCurrentDate();
                      actBranch = ProxyParameters.BRANCH_ID;
                }  
     public HashMap interestCalculationTLAD1(Object accountNo, Object prod_id,String prodType) throws Exception {
       
        HashMap dataMap=new HashMap();
        HashMap hash=new HashMap();
        try{
            hash.put("ACT_NUM",accountNo);
            hash.put("PRODUCT_TYPE",prodType);
            hash.put("PROD_ID",prod_id);
            //            hash.put("TRANS_DT", interestUptoDt);
            hash.put("TRANS_DT", currentDate);
            hash.put("INITIATED_BRANCH", actBranch);
            String mapNameForCalcInt = "IntCalculationDetail";
            if (prodType.equals("AD")) {
                mapNameForCalcInt = "IntCalculationDetailAD";
            }
            List lst=ClientUtil.executeQuery(mapNameForCalcInt, hash);
            System.out.println(accountNo+","+prod_id+","+"LIST   1>>>>>>"+lst);
            if(lst !=null && lst.size()>0){
                hash=(HashMap)lst.get(0);
                java.util.Iterator iterator = hash.keySet().iterator();
                while (iterator.hasNext()) {
                    String value = "";
                    String key = iterator.next().toString();
                    if(hash.get(key)!=null)
                     value = hash.get(key).toString();
                    System.out.println(key + " " + value);
                }
                if(hash.get("AS_CUSTOMER_COMES")!=null  && hash.get("AS_CUSTOMER_COMES").equals("N")){
                    hash=new HashMap();
                    return hash;
                }
                hash.put("ACT_NUM",accountNo);
                hash.put("PRODUCT_TYPE",prodType);
                hash.put("PROD_ID",prod_id);
                //                hash.put("TRANS_DT", intUptoDt);
                hash.put("TRANS_DT", currentDate);
                hash.put("INITIATED_BRANCH", actBranch);
                hash.put("ACT_NUM",accountNo);
                hash.put("BRANCH_ID", actBranch);
                hash.put("BRANCH_CODE", actBranch);
                hash.put("LOAN_ACCOUNT_CLOSING","LOAN_ACCOUNT_CLOSING");
                //                hash.put("CURR_DATE", interestUptoDt);
                hash.put("CURR_DATE", currentDate);
                dataMap.put(CommonConstants.MAP_WHERE, hash);
                System.out.println("map before actBranch###"+actBranch);
                System.out.println("map before intereest###"+dataMap);
                hash = new SelectAllDAO().executeQuery(dataMap);
                if(hash==null) {
                    hash=new HashMap();
                }
                if (hash.containsKey("DATA") && hash.get("DATA")!=null) {
                    hash.putAll((HashMap)((List)hash.get("DATA")).get(0));
                }
                hash.putAll((HashMap)dataMap.get(CommonConstants.MAP_WHERE));
                System.out.println("hashinterestoutput###"+hash);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return hash;
    } 
     public double calcLoanPayments(String actNum,String prodId, String prodType) throws Exception {
         double totalDemand = 0.0;
          prodId=actNum.substring(3, 6);
        HashMap dataMap=new HashMap();
        HashMap whereMap=new HashMap();
        whereMap.put("ACT_NUM",actNum);
        int firstDay=0;
        Date inst_dt=null;
        HashMap asAndWhenMap = interestCalculationTLAD1(actNum, prodId, prodType);
        System.out.println("@#@ asAndWhenMap is >>>>"+asAndWhenMap);
       // System.out.println("transDetail"+actNum+_branchCode);
        HashMap insertPenal=new HashMap();
        List chargeList=null;
        HashMap loanInstall = new HashMap();
        loanInstall.put("ACT_NUM",actNum);
        loanInstall.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
        if(prodType !=null && prodType.equals("TL")) {      //Only TL
            HashMap allInstallmentMap=null;
            List paidAmt=ClientUtil.executeQuery("getPaidPrinciple", loanInstall);
            allInstallmentMap=(HashMap)paidAmt.get(0);
            System.out.println("!!!!asAndWhenMap:"+asAndWhenMap);
            double totPrinciple=CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
            if(asAndWhenMap ==null || (asAndWhenMap !=null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && (asAndWhenMap.get("AS_CUSTOMER_COMES")!=null) && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("N"))){
                paidAmt=ClientUtil.executeQuery("getIntDetails", loanInstall);
                if(paidAmt !=null && paidAmt.size()>0)
                    allInstallmentMap=(HashMap)paidAmt.get(0);
                double totExcessAmt=CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
                totPrinciple+=totExcessAmt;
            }
            List lst=ClientUtil.executeQuery("getAllLoanInstallment", loanInstall);
            //                    Date inst_dt=null;
            for(int i=0;i<lst.size();i++) {
                allInstallmentMap=(HashMap)lst.get(i);
                double instalAmt=CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                if(instalAmt<=totPrinciple) {
                    totPrinciple-=instalAmt;
                    inst_dt=new Date();
                    String in_date=CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                    inst_dt=DateUtil.getDateMMDDYYYY(in_date);
                }
                else{
                    inst_dt=new Date();
                    String in_date=CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                    inst_dt=DateUtil.getDateMMDDYYYY(in_date);
                    totPrinciple=CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue()-totPrinciple;
                    break;
                }
            }
            Date addDt=(Date)currentDate.clone();
            Date instDt=DateUtil.addDays(inst_dt,1);
            addDt.setDate(instDt.getDate());
            addDt.setMonth(instDt.getMonth());
            addDt.setYear(instDt.getYear());
            loanInstall.put("FROM_DATE",addDt);//DateUtil.addDays(inst_dt,1));
            //                    loanInstall.put("TO_DATE",interestUptoDt);
            loanInstall.put("TO_DATE",currentDate);
            System.out.println("!! getTotalamount#####"+loanInstall);
            List lst1=null;
            if(inst_dt !=null &&(totPrinciple>0)) {
                lst1=ClientUtil.executeQuery("getTotalAmountOverDue",loanInstall);
                System.out.println("listsize####"+lst1);
            }
            double principle=0;
            if(lst1 !=null && lst1.size()>0){
                HashMap map=(HashMap)lst1.get(0);
                principle=CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue();
            }
            totPrinciple+=principle;
            insertPenal.put("CURR_MONTH_PRINCEPLE",new Double(totPrinciple));
            insertPenal.put("INSTALL_DT",inst_dt);
            if (asAndWhenMap.containsKey("MORATORIUM_INT_FOR_EMI")) {
                insertPenal.put("MORATORIUM_INT_FOR_EMI",asAndWhenMap.get("MORATORIUM_INT_FOR_EMI"));
            }
            if(asAndWhenMap !=null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && (asAndWhenMap.get("AS_CUSTOMER_COMES")!=null) && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")){
                double interest=CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
                double penal =CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                List facilitylst=ClientUtil.executeQuery("LoneFacilityDetailAD",loanInstall);
                if(facilitylst!=null && facilitylst.size()>0){
                    HashMap hash=(HashMap)facilitylst.get(0);
                    hash.put("ACT_NUM",loanInstall.get("ACT_NUM"));
                    if (asAndWhenMap.containsKey("PREMATURE")) {
                        insertPenal.put("PREMATURE",asAndWhenMap.get("PREMATURE"));
                    }
                    if (asAndWhenMap.containsKey("PREMATURE") && asAndWhenMap.containsKey("PREMATURE_INT_CALC_AMT") &&
                    CommonUtil.convertObjToStr(asAndWhenMap.get("PREMATURE_INT_CALC_AMT")).equals("LOANSANCTIONAMT")) {
                        hash.put("FROM_DT",hash.get("ACCT_OPEN_DT"));
                    } else {
                        hash.put("FROM_DT",hash.get("LAST_INT_CALC_DT"));
                        hash.put("FROM_DT",DateUtil.addDays(((Date)hash.get("FROM_DT")),2));
                    }
                    //                            hash.put("TO_DATE", interestUptoDt.clone());
                    hash.put("TO_DATE", currentDate.clone());
                    if(!(asAndWhenMap !=null && asAndWhenMap.containsKey("INSTALL_TYPE") && asAndWhenMap.get("INSTALL_TYPE")!=null && asAndWhenMap.get("INSTALL_TYPE").equals( "EMI")))
                        facilitylst=ClientUtil.executeQuery("getPaidPrinciple",hash);
                    else{
                        facilitylst=null;
                        if( asAndWhenMap.containsKey("PRINCIPAL_DUE") && asAndWhenMap.get("PRINCIPAL_DUE")!=null){
                            insertPenal.put("CURR_MONTH_PRINCEPLE",asAndWhenMap.get("PRINCIPAL_DUE"));
                        }
                    }
                    if(facilitylst!=null && facilitylst.size()>0){
                        hash=(HashMap)facilitylst.get(0);
                        interest-=CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                        penal-=CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                        
                        insertPenal.put("PAID_INTEREST",hash.get("INTEREST"));
                    }
                }
                System.out.println("####interest:"+interest);
                if(interest>0)
                    insertPenal.put("CURR_MONTH_INT",new Double(interest));
                else
                    insertPenal.put("CURR_MONTH_INT",new Double(0));
                if(penal>0)
                    insertPenal.put("PENAL_INT",new Double(penal));
                else
                    insertPenal.put("PENAL_INT",new Double(0));
                insertPenal.put("INTEREST",asAndWhenMap.get("INTEREST"));
                insertPenal.put("LOAN_CLOSING_PENAL_INT",asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
                
                insertPenal.put("LAST_INT_CALC_DT",asAndWhenMap.get("LAST_INT_CALC_DT"));
                insertPenal.put("ROI",asAndWhenMap.get("ROI"));
                chargeList=ClientUtil.executeQuery("getChargeDetails",loanInstall);
            }else{
                List getIntDetails=ClientUtil.executeQuery("getIntDetails", loanInstall);
                HashMap hash=null;
                if(getIntDetails!=null)
                    for(int i=0;i<getIntDetails.size();i++){
                        hash=(HashMap)getIntDetails.get(i);
                        String trn_mode=CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
                        double pBal=CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue();
                        double iBal=CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
                        double pibal= CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
                        double excess= CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT")).doubleValue();
                        pBal-=excess;
                        if(pBal<totPrinciple)
                            insertPenal.put("CURR_MONTH_PRINCEPLE",new Double(pBal));
                        if(trn_mode.equals("C*")){
                            insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                            insertPenal.put("PRINCIPLE_BAL",new Double(pBal));
                            insertPenal.put("EBAL",hash.get("EBAL"));
                            break;
                        }
                        else {
                            if(!trn_mode.equals("DP"))
                                insertPenal.put("CURR_MONTH_INT",String.valueOf(iBal + pibal));
                            insertPenal.put("EBAL",hash.get("EBAL"));
                            insertPenal.put("PRINCIPLE_BAL",new Double(pBal));
                        }
                        System.out.println("int principel detailsINSIDE LOAN##"+insertPenal);
                    }
                getIntDetails=ClientUtil.executeQuery("getPenalIntDetails", loanInstall);
                hash=(HashMap)getIntDetails.get(0);
                insertPenal.put("PENAL_INT",hash.get("PIBAL"));
            }
        }
        
        if(prodType !=null && prodType.equals("AD"))        // Only  AD
            if(asAndWhenMap !=null && asAndWhenMap.size()>0)
                if(asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")){
                    List facilitylst=ClientUtil.executeQuery("LoneFacilityDetailAD",loanInstall);
                    double interest=CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
                    double penal =CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                    if(facilitylst!=null && facilitylst.size()>0){
                        HashMap hash=(HashMap)facilitylst.get(0);
                        hash.put("ACT_NUM",loanInstall.get("ACT_NUM"));
                        hash.put("FROM_DT",hash.get("LAST_INT_CALC_DT"));
                        hash.put("FROM_DT",DateUtil.addDays(((Date)hash.get("FROM_DT")),2));
                        //                                hash.put("TO_DATE",DateUtil.addDaysProperFormat(interestUptoDt,-1));
                        hash.put("TO_DATE",DateUtil.addDaysProperFormat(currentDate,-1));
                        facilitylst=ClientUtil.executeQuery("getPaidPrincipleAD",hash);
                        if(facilitylst!=null && facilitylst.size()>0){
                            hash=(HashMap)facilitylst.get(0);
                            interest-=CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                            penal-=CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                        }
                    }
                    if(interest>0)
                        insertPenal.put("CURR_MONTH_INT",new Double(interest));
                    else
                        insertPenal.put("CURR_MONTH_INT",new Double(0));
                    if(penal>0)
                        insertPenal.put("PENAL_INT",new Double(penal));
                    else
                        insertPenal.put("PENAL_INT",new Double(0));
                    insertPenal.put("INTEREST",asAndWhenMap.get("INTEREST"));
                    insertPenal.put("LOAN_CLOSING_PENAL_INT",asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
                    chargeList=ClientUtil.executeQuery("getChargeDetails",loanInstall);
                }else{
                    if(prodType !=null && prodType.equals("AD")) {
                        List getIntDetails=ClientUtil.executeQuery("getIntDetailsAD", loanInstall);
                        HashMap hash=null;
                        
                        for(int i=0;i<getIntDetails.size();i++){
                            hash=(HashMap)getIntDetails.get(i);
                            String trn_mode=CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
                            double iBal=CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
                            double pibal= CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
                            double excess= CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT")).doubleValue();
                            if(trn_mode.equals("C*")){
                                insertPenal.put("CURR_MONTH_INT",String.valueOf(iBal + pibal));
                                insertPenal.put("PRINCIPLE_BAL",new Double(CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue()-excess));
                                insertPenal.put("EBAL",hash.get("EBAL"));
                                break;
                            }else{
                                insertPenal.put("CURR_MONTH_INT",String.valueOf(iBal + pibal));
                                insertPenal.put("PRINCIPLE_BAL",new Double(CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue()-excess));
                                insertPenal.put("EBAL",hash.get("EBAL"));
                            }
                            System.out.println("int principel detailsINSIDE OD"+insertPenal);
                        }
                        getIntDetails=ClientUtil.executeQuery("getPenalIntDetailsAD", loanInstall);
                        if(getIntDetails.size()>0){
                            hash=(HashMap)getIntDetails.get(0);
                            insertPenal.put("PENAL_INT",hash.get("PIBAL"));
                        }
                        insertPenal.remove("PRINCIPLE_BAL");
                        
                    }
                }
        //Added By Suresh  (Current Dt > To Date AND PBAL >0 in ADV_TRANS_DETAILS, Add Principle_Balance)
        if(prodType !=null && prodType.equals("AD")){
            double pBalance=0.0;
            Date expDt = null;
            List expDtList=ClientUtil.executeQuery("getLoanExpDate", loanInstall);
            if(expDtList!=null && expDtList.size()>0){
                whereMap = new HashMap();
                whereMap=(HashMap) expDtList.get(0);
                pBalance = CommonUtil.convertObjToDouble(whereMap.get("PBAL")).doubleValue();
                expDt = (Date)whereMap.get("TO_DT");
                //                        long diffDayPending = DateUtil.dateDiff(expDt,intUptoDt);
                long diffDayPending = DateUtil.dateDiff(expDt, currentDate);
                System.out.println("############# Insert PBalance"+pBalance+"######diffDayPending :"+diffDayPending);
                if(diffDayPending>0 && pBalance>0){
                    insertPenal.put("PRINCIPLE_BAL",new Double(pBalance));
                }
            }
        }
        System.out.println("####### insertPenal : "+insertPenal);
        //Charges
        double chargeAmt =0.0;
        whereMap =new HashMap();
        whereMap.put("ACT_NUM",actNum);
        chargeAmt = getChargeAmount(whereMap, prodType);
       /* if(chargeAmt>0){
            dataMap.put("CHARGES", String.valueOf(chargeAmt));
        }else{
            dataMap.put("CHARGES", "0");
        }*/
        System.out.println("####### Single Row insertPenal : "+insertPenal);
        
        double principalAmount = 0.0;
        if(insertPenal.containsKey("CURR_MONTH_PRINCEPLE")){
            principalAmount = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_PRINCEPLE")).doubleValue();     // Principal Amount
            totalDemand = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue() +
            CommonUtil.convertObjToDouble(insertPenal.get("PENAL_INT")).doubleValue()+chargeAmt;
        }else{
            principalAmount = CommonUtil.convertObjToDouble(insertPenal.get("PRINCIPLE_BAL")).doubleValue();     // Principal Amount
            totalDemand = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue() +
            CommonUtil.convertObjToDouble(insertPenal.get("PENAL_INT")).doubleValue()+chargeAmt;
        }
        
        
        if (inst_dt!=null && prodType.equals("TL")){
            //                    if(DateUtil.dateDiff(intUptoDt, inst_dt)<=0 ){
            if(DateUtil.dateDiff(currentDate, inst_dt)<=0 ){
               // dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
            }else{
               // dataMap.put("PRINCIPAL", "0");
                principalAmount =0.0;
            }
        }
      /*  if (prodType.equals("AD")){
            if(principalAmount>0){
                dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
            }else{
                dataMap.put("PRINCIPAL", "0");
            }
        }*/
        totalDemand+=principalAmount;
      //  dataMap.put("INTEREST", insertPenal.get("CURR_MONTH_INT"));
      //  dataMap.put("PENAL", insertPenal.get("PENAL_INT"));
       // dataMap.put("TOTAL_DEMAND", new Double(totalDemand));
       // if(totalDemand<=0){
        //    dataMap = null;
       // }
        //            }else{
        //                System.out.println("### Not Allow : "+checkDate);
        //                dataMap = null;
        //            }
        //        }
        
        
        System.out.println("####### Single Row DataMap : "+dataMap);
        return totalDemand;
    }
       private double getChargeAmount(HashMap whereMap, String prodType){   //Charges
        double chargeAmount =0.0;
        try{
            List chargeList = null;
            String actNo="";
            HashMap recoverChrgMap = new HashMap();
            actNo = CommonUtil.convertObjToStr(whereMap.get("ACT_NUM"));
            chargeList = ClientUtil.executeQuery("getChargeDetails", whereMap);
            if(chargeList!=null && chargeList.size()>0){
                for(int i=0;i<chargeList.size();i++){
                    whereMap=(HashMap) chargeList.get(i);
                    chargeAmount+=CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
                    
                    
                }
            }
            chargeList = null;
            recoverChrgMap = null;
        }catch(Exception e){
            e.printStackTrace();
        }
        return chargeAmount;
    }
}
