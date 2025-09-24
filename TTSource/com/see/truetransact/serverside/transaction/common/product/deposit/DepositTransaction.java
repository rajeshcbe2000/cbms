/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositTransaction.java
 *
 * Created on October 1, 2004, 3:19 PM
 */
package com.see.truetransact.serverside.transaction.common.product.deposit;

import java.util.HashMap;

//import com.ibatis.db.sqlmap.SqlMap;

//import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
//import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
// For the Business Rules...
import com.see.truetransact.businessrule.RuleContext;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.businessrule.transaction.DeathMarkedRule;
//import com.see.truetransact.businessrule.transaction.AddressVerificationRule;
//import com.see.truetransact.businessrule.transaction.ConfirmThanxRule;


// For Maintaining Logs...
//import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

//import org.apache.log4j.Logger;

import com.see.truetransact.serverside.transaction.common.Transaction;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;

// For the Business Rules...
//import com.see.truetransact.businessrule.RuleContext;
import com.see.truetransact.businessrule.RuleEngine;
import com.see.truetransact.businessrule.transaction.AccountCheckingRule;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import java.util.ArrayList;
import com.see.truetransact.commonutil.TTException;

import java.util.List;
import java.util.Date;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.serverside.transaction.common.product.interbranch.InterBranchTransaction;
import java.util.*;

/**
 *
 * @author Pinky
 */
public class DepositTransaction extends Transaction {

    public DepositTransaction() throws ServiceLocatorException {
        super();
    }

    /**
     * Updating Clear & Total Balance
     */
    public void performOtherBalanceAdd(HashMap addShadow) throws Exception {
        printLog("performOtherBalanceAdd :" + addShadow);

        String act_num = (String) addShadow.get(TransactionDAOConstants.ACCT_NO);
        if (act_num != null && !act_num.equalsIgnoreCase("")) {
            System.out.println("condition false");
            if (!addShadow.containsKey(TransactionDAOConstants.UNCLEAR_AMT)) {
                addShadow.put(TransactionDAOConstants.UNCLEAR_AMT, new Double(0));
            }
            // Updates other Balances  (Clear balance and Total Balance)
            sqlMap.executeUpdate("updateOtherBalancesTD", addShadow);
            printLog("performOtherBalanceAdd:after:" + addShadow);
        }
        addShadow = null;
    }

    /**
     * Updating Clear & Total Balance
     */
    public void performOtherBalanceMinus(HashMap addShadow) throws Exception {
        printLog("performOtherBalanceMinus:before:" + addShadow);

        String act_num = (String) addShadow.get(TransactionDAOConstants.ACCT_NO);
        if (act_num != null && !act_num.equalsIgnoreCase("")) {

            if (addShadow.containsKey(TransactionDAOConstants.UNCLEAR_AMT)) {
                addShadow.put(TransactionDAOConstants.UNCLEAR_AMT,
                        new Double(-1 * Double.parseDouble(addShadow.get(TransactionDAOConstants.UNCLEAR_AMT).toString())));
            }

            if (!addShadow.containsKey(TransactionDAOConstants.UNCLEAR_AMT)) {
                addShadow.put(TransactionDAOConstants.UNCLEAR_AMT, new Double(0));
            }


            addShadow.put(TransactionDAOConstants.AMT,
                    new Double(-1 * Double.parseDouble(addShadow.get(TransactionDAOConstants.AMT).toString())));
            // Updates other Balances  (Clear balance and Total Balance)
            sqlMap.executeUpdate("updateOtherBalancesTD", addShadow);
            printLog("performOtherBalanceMinus:after:" + addShadow);
        }
        addShadow = null;
    }

    /**
     * Updating Shadow debit or shadow credit based on the parameter
     */
    public void performShadowAdd(HashMap addShadow) throws Exception {
        printLog("performShadowAdd:before addShadow:" + addShadow);
        String act_num = (String) addShadow.get(TransactionDAOConstants.ACCT_NO);
        if (act_num != null && !act_num.equalsIgnoreCase("")) {

            if (((String) addShadow.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
                addShadow.put(TransactionDAOConstants.AMT,
                        new Double(-1 * Double.parseDouble(addShadow.get(TransactionDAOConstants.AMT).toString())));
                if (addShadow.containsValue("SAME_DEPOSIT_NO")) {
                    addShadow.put("ACCT_STATUS", "CLOSED");
                }
                sqlMap.executeUpdate("updateShadowDebitTD", addShadow);
            } else {
                if (addShadow.containsValue("SAME_DEPOSIT_NO")) {
                    addShadow.put("ACCT_STATUS", "NEW");
                }
                sqlMap.executeUpdate("updateShadowCreditTD", addShadow);
            }
            printLog("performShadowAdd:after addShadow :" + addShadow);

        }
        addShadow = null;
    }

    /**
     * Updating Shadow debit or shadow credit based on the parameter
     */
    public void performShadowMinus(HashMap minusMap) throws Exception {
        printLog("performShadowMinus:before minusMap :" + minusMap);

        String act_num = (String) minusMap.get(TransactionDAOConstants.ACCT_NO);
        if (act_num != null && !act_num.equalsIgnoreCase("")) {

            minusMap.put(TransactionDAOConstants.AMT,
                    new Double(-1 * Double.parseDouble(minusMap.get(TransactionDAOConstants.AMT).toString())));
            if (((String) minusMap.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
                if (minusMap.containsValue("SAME_DEPOSIT_NO")) {
                    minusMap.put("ACCT_STATUS", "CLOSED");
                }
                sqlMap.executeUpdate("updateShadowDebitTD", minusMap);
            } else {
                if (minusMap.containsValue("SAME_DEPOSIT_NO")) {
                    minusMap.put("ACCT_STATUS", "NEW");
                }
                sqlMap.executeUpdate("updateShadowCreditTD", minusMap);
            }
            printLog("performShadowMinus:after minusMap:" + minusMap);
        }
        minusMap = null;
    }

    /**
     * Updating Available Balance
     */
    public void updateAvailableBalance(HashMap updateMap) throws Exception {
        printLog("updateAvailableBalance:" + updateMap);

        String act_num = (String) updateMap.get(TransactionDAOConstants.ACCT_NO);
        if (act_num != null && !act_num.equalsIgnoreCase("")) {

            if (!updateMap.containsKey(TransactionDAOConstants.UNCLEAR_AMT)) {
                updateMap.put(TransactionDAOConstants.UNCLEAR_AMT, new Double(0));
            }

            System.out.println("deposit_No : " + updateMap.get("ACCOUNTNO"));
            String deposit_No = CommonUtil.convertObjToStr(updateMap.get("ACCOUNTNO"));
            if (deposit_No.lastIndexOf("_") != -1) {
                deposit_No = deposit_No.substring(0, deposit_No.lastIndexOf("_"));
            }
            System.out.println("depositNo " + deposit_No);
            HashMap recurringMap = new HashMap();
            recurringMap.put("DEPOSIT_NO", deposit_No);
            if (updateMap.containsValue("DEBIT") && updateMap.containsValue("SAME_DEPOSIT_NO")) {
                updateMap.put("ACCT_STATUS", "CLOSED");
            } else if (updateMap.containsValue("CREDIT") && updateMap.containsValue("SAME_DEPOSIT_NO")) {
                updateMap.put("ACCT_STATUS", "NEW");
            }
            sqlMap.executeUpdate("updateAvailBalanceTD", updateMap);
            System.out.println("###recurringMap :" + recurringMap);
            double totalInstallment = 0.0;
            double installmentPaid = 0.0;
            double installPaid = 0.0;
            double depAmt = 0.0;
            double currAmt = 0.0;
            double totalBalance = 0.0,adtAmt=0.0;
            String behavesLike = "";
            String branchCode = "",prod_ID ="";
            Date matDate = null;
            int dep_freq =0;
            HashMap recurMap = new HashMap();
            if (!updateMap.containsKey("DEPOSIT_CLOSING")) {
                List list = sqlMap.executeQueryForList("getDepositAmountForRecurring", recurringMap);
                if (list != null && list.size() > 0) {
                    recurMap = (HashMap) list.get(0);
                    totalInstallment = CommonUtil.convertObjToDouble(recurMap.get("TOTAL_INSTALLMENTS")).doubleValue();
                    installmentPaid = CommonUtil.convertObjToDouble(recurMap.get("TOTAL_INSTALL_PAID")).doubleValue();
                    totalBalance = CommonUtil.convertObjToDouble(recurMap.get("TOTAL_BALANCE")).doubleValue();
                    matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(recurMap.get("MATURITY_DT")));
                    branchCode = CommonUtil.convertObjToStr(recurMap.get("BRANCH_CODE"));
                    if(recurMap.containsKey("ADT_AMT")){
                    	adtAmt=CommonUtil.convertObjToDouble(recurMap.get("ADT_AMT"));
                    }
                    depAmt = CommonUtil.convertObjToDouble(recurMap.get("DEPOSIT_AMT")).doubleValue();
                    //                System.out.println("####installment insert :" +totalInstallment);                    
                    //                System.out.println("####installment insert :" +installmentPaid);                    
                    System.out.println("###recurMap :" + recurMap);
                    HashMap prodMap = new HashMap();
                    prodMap.put("PROD_ID", recurMap.get("PROD_ID"));
                    System.out.println("####prodMap :" + prodMap);
                     prod_ID = CommonUtil.convertObjToStr(recurMap.get("PROD_ID"));
                    list = sqlMap.executeQueryForList("getBehavesLikeForDeposit", prodMap);
                    if (list != null && list.size() > 0) {
                        System.out.println("####prodMap :" + list);
                        prodMap = (HashMap) list.get(0);
                        behavesLike = CommonUtil.convertObjToStr(prodMap.get("BEHAVES_LIKE"));
//                        HashMap accHeadMap = new HashMap();
//                        accHeadMap.put("DEPOSIT_NO",deposit_No);
//                        accHeadMap.put("INSTALL_TYPE", prodMap.get("ACCT_HEAD"));
//                        sqlMap.executeUpdate("updateAccountHeadDeposits", accHeadMap);
                        if (prodMap.get("BEHAVES_LIKE").equals("RECURRING")){
                            if(!(updateMap.get("INSTRUMENT1")!=null && !updateMap.get("INSTRUMENT1").equals("") && updateMap.get("INSTRUMENT1").equals("INTEREST_AMT"))){
                                System.out.println("####hereeeee %#%@%@%@%@%");
                                prod_ID = CommonUtil.convertObjToStr(recurMap.get("PROD_ID"));
                                currAmt = CommonUtil.convertObjToDouble(updateMap.get("AMOUNT")).doubleValue();
                                depAmt = CommonUtil.convertObjToDouble(recurMap.get("DEPOSIT_AMT")).doubleValue();
                                System.out.println("####currAmt :" + currAmt);
                                System.out.println("####depAmt :" + depAmt);
                                installPaid = currAmt / depAmt;
                                System.out.println("####installment :" + installPaid);
                                recurMap.put("INSTALLMENT_PAID", new Double(installPaid));
                                recurMap.put("TOTAL_INSTALLMENTS", new Double(installPaid));
                                recurMap.put("DEPOSIT_NO", recurringMap.get("DEPOSIT_NO"));
                                sqlMap.executeUpdate("updateInstallmentPaid", recurMap);
                            }
                        }
                    }
                }
                String paidInstall = String.valueOf(installmentPaid);
                boolean transInFlag = false;
                if (recurMap.containsKey("OPENING_MODE") && recurMap.get("OPENING_MODE").equals("TransferIn") && totalBalance == 0) {
                    transInFlag = true;
                    HashMap transferInMap = new HashMap();
                    transferInMap.put("DEPOSIT_NO", deposit_No);
                    List lst = sqlMap.executeQueryForList("getSelectOriginalAcNo", transferInMap);
                    if (lst != null && lst.size() > 0) {
                        transferInMap = (HashMap) lst.get(0);
                        String originalAcNo = CommonUtil.convertObjToStr(transferInMap.get("ORIGINAL_AC_NUMBER"));
                        transferInMap = new HashMap();
                        transferInMap.put("ORIGINAL_AC_NUMBER", originalAcNo + "_1");
                        lst = sqlMap.executeQueryForList("selectAllRecordsFromDepRec", transferInMap);
                        if (lst != null && lst.size() > 0) {
                            for (int i = 0; i < lst.size(); i++) {
                                transferInMap = (HashMap) lst.get(i);
                                HashMap transInMap = new HashMap();
                                transInMap.put("TODAY_DT", transferInMap.get("TRANS_DT"));
                                transInMap.put("ACCOUNTNO", updateMap.get("ACCOUNTNO"));
                                transInMap.put("DELAYED_COUNT",new Double(0) );
                                transInMap.put("AMOUNT",CommonUtil.convertObjToDouble(transferInMap.get("AMOUNT")));
                                transInMap.put("DUE_DATE", transferInMap.get("DUE_DATE"));
                                transInMap.put("SL_NO", CommonUtil.convertObjToDouble(transferInMap.get("SL_NO")));
                                transInMap.put("ADT_AMT",new Double(0));
                                System.out.println("###transInMap :" + transInMap);
                                sqlMap.executeUpdate("insertRecurringDeposit", transInMap);
                            }
                        }
                    }
                    transferInMap = null;
                }
                if (recurMap.containsKey("OPENING_MODE") && !recurMap.get("OPENING_MODE").equals("TransferIn") && isRecurringDeposit(act_num) && paidInstall.equals("0.0")) {
                    System.out.println("###recurMap paidInstall :" + recurMap);
                    HashMap recurrMap = new HashMap();
                    int num = 0;
                    //                String strDepDate = CommonUtil.convertObjToStr(recurMap.get("DEPOSIT_DT"));
                    //                Date depDate = DateUtil.getDateMMDDYYYY(strDepDate);
                    Date depDate = (Date) recurMap.get("DEPOSIT_DT");
                    Date fromDt = (Date) recurMap.get("DEPOSIT_DT");
                   //Added by Chithra
					 if (behavesLike.equals("RECURRING")) {
                        prod_ID = CommonUtil.convertObjToStr(recurMap.get("PROD_ID"));
                        if (prod_ID != null && prod_ID.length() > 0) {
                            HashMap chkMap = new HashMap();
                            chkMap.put("PID", prod_ID);
                            List chklist = sqlMap.executeQueryForList("getDailyDepositFrequency", chkMap);
                            if (chklist != null && chklist.size() > 0) {
                                HashMap sing = (HashMap) chklist.get(0);
                                if (sing != null && sing.containsKey("DEPOSIT_FREQ")) {
                                    dep_freq = CommonUtil.convertObjToInt(sing.get("DEPOSIT_FREQ"));
                                }
                            }
                        }
                     }
                    //Added by nithya on 23-12-2021 for KD-3134   
                    long instCount = 0;                     
                    List instCountList = sqlMap.executeQueryForList("getRecurringDepositInstCount", updateMap);  
                    if(instCountList != null && instCountList.size() > 0){
                        HashMap countMap = (HashMap)instCountList.get(0);
                        if(countMap.containsKey("INST_COUNT") && countMap.get("INST_COUNT") != null){
                          instCount = CommonUtil.convertObjToLong(countMap.get("INST_COUNT"));
                        }
                    }
                    if (CommonConstants.SAL_REC_MODULE != null && !CommonConstants.SAL_REC_MODULE.equals("")
                            && CommonConstants.SAL_REC_MODULE.equalsIgnoreCase("Y") && instCount > 0) {                        
                        double instPaid = CommonUtil.convertObjToDouble(recurMap.get("INSTALLMENT_PAID"));                        
                        for(int i=0; i<instPaid; i++){
                            HashMap instUpdateMap = new HashMap();
                            instUpdateMap.put("SL_NO", CommonUtil.convertObjToStr(i+1));
                            instUpdateMap.put("TRANS_DT", updateMap.get("TODAY_DT"));
                            instUpdateMap.put("AMOUNT", new Double(depAmt));
                            instUpdateMap.put("COUNT", new Double(0));
                            instUpdateMap.put("ACCOUNTNO", updateMap.get("ACCOUNTNO"));
                            System.out.println("####instUpdateMap :" + instUpdateMap);
                            sqlMap.executeUpdate("updateDepositTransDate", instUpdateMap);
                        }
                    }else{                    
                    for (int i = 0; i < totalInstallment; i++) {
                        num = i + 1;
                        recurrMap.put("SL_NO", new Double(num));
                        //                        System.out.println("####num :" +num); 
                        recurrMap.put("DUE_DATE", depDate);
                        recurrMap.put("TODAY_DT", null);
                        recurrMap.put("DELAYED_COUNT", new Double(0));
                        recurrMap.put("AMOUNT", new Double(0));
                        recurrMap.put("ACCOUNTNO", updateMap.get("ACCOUNTNO"));
                        recurrMap.put("ADT_AMT",new Double(0));
                        //                        System.out.println("####depDate before insert :" +depDate);
                        //                        System.out.println("####recurrMap before insert :" +recurrMap);                    
                        sqlMap.executeUpdate("insertRecurringDeposit", recurrMap);
                       //Added By Chithra
                        if(behavesLike.equals("RECURRING") && dep_freq==7){
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(depDate);
                            cal.add(Calendar.DATE, 7); 
                            depDate = cal.getTime();
                            depDate = CommonUtil.getProperDate(ServerUtil.getCurrentDate(branchCode), depDate);
                         } else if (behavesLike.equals("RECURRING") && dep_freq == 365) { // Added by nithya on 13-09-2017 for 0007656: Need yearly installment remittance option in rd type deposit
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(depDate);
                            cal.add(Calendar.YEAR, 1);
                            depDate = cal.getTime();
                            depDate = CommonUtil.getProperDate(ServerUtil.getCurrentDate(branchCode), depDate);
                        }
                        else{
                           depDate = DateUtil.addDays(depDate, fromDt, 30); 
                        }
                       if (num == 1) {
                            recurrMap.put("TRANS_DT", updateMap.get("TODAY_DT"));
                            recurrMap.put("AMOUNT", updateMap.get("AMOUNT"));
                            recurrMap.put("ACCOUNTNO", updateMap.get("ACCOUNTNO"));
                            recurrMap.put("DELAYED_COUNT", new Double(0));
                            sqlMap.executeUpdate("updateRecurringDeposit", recurrMap);
                        }
                    }
                }  
                    recurrMap = null;
                } else if (transInFlag == false) {
                    installmentPaid = installmentPaid + 1;
                    System.out.println("installment else :" + updateMap + "installPaid :" + installPaid + "installmentPaid :" + installmentPaid);
                    HashMap elseMap = new HashMap();
                    for (int i = 0; i < installPaid; i++) {
                        elseMap.put("ACCOUNTNO", updateMap.get("ACCOUNTNO"));
                        List lst = sqlMap.executeQueryForList("getSelectDepositTransDt", elseMap);
                        if (lst != null && lst.size() > 0) {
                            elseMap = (HashMap) lst.get(0);
                            System.out.println("####elseMap :" + elseMap);
                            double slNo = CommonUtil.convertObjToDouble(elseMap.get("SL_NO")).doubleValue();
                            //                        double slNo = CommonUtil.convertObjToDouble(elseMap.get("MIN(SL_NO)")).doubleValue();
                            System.out.println("####dueMap elseMap:" + elseMap);
                            //                        Date dueDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(elseMap.get("DUE_DATE")));
                            //                        Date today = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(updateMap.get("TODAY_DT")));
                            Date dueDt = (Date) elseMap.get("DUE_DATE");
                            Date today = (Date) updateMap.get("TODAY_DT");
                            System.out.println("today:" + today + "dueDt:" + dueDt);
                            int dueMonth = dueDt.getMonth() + 1;
                            int transMonth = today.getMonth() + 1;
                            int dueYear = dueDt.getYear() + 1900;
                            int transYear = today.getYear() + 1900;
                            System.out.println("currYr :" + dueYear + "transYr :" + transYear + "dueMth :" + dueMonth + "transMth :" + transMonth);
                            double delayeInstallment = 0;
                            if (dueYear == transYear) {
                                delayeInstallment = transMonth - dueMonth;
                            } else {
                                double diffYear = transYear - dueYear;
                                delayeInstallment = (diffYear * 12 - dueMonth) + transMonth;
                                System.out.println("diffYear :" + diffYear + "delayeInstallment :" + delayeInstallment + "totMon :" + delayeInstallment);
                            }
                            System.out.println("slNo else :" + slNo);
                            //                        elseMap.put("SL_NO",new Double(slNo));
                            elseMap.put("SL_NO", CommonUtil.convertObjToStr((installmentPaid)));
                            elseMap.put("TRANS_DT", updateMap.get("TODAY_DT"));
                            elseMap.put("AMOUNT", new Double(depAmt));
                            elseMap.put("COUNT", new Double(0));
                            elseMap.put("ACCOUNTNO", updateMap.get("ACCOUNTNO"));
                            System.out.println("####elseMap :" + elseMap);
                            sqlMap.executeUpdate("updateDepositTransDate", elseMap);
                            installmentPaid = installmentPaid + 1;
                            delayeInstallment = 0.0;
                            System.out.println("####installment after update else :" + slNo);
                        }
                    }
                    //Added BY Suresh
                    if (behavesLike.equals("RECURRING")) {
                        Date currDt = (Date) ServerUtil.getCurrentDate(branchCode);
                        Date matDt = (Date) ServerUtil.getCurrentDate(branchCode);
//                        Date matDt = ClientUtil.getCurrentDate();
//                        Date currDt = ClientUtil.getCurrentDate();
                        if (matDate.getDate() > 0) {
                            matDt.setDate(matDate.getDate());
                            matDt.setMonth(matDate.getMonth());
                            matDt.setYear(matDate.getYear());
                        }
//                        System.out.println("##### matDt"+matDt+" #####currDt"+currDt);
//                        System.out.println("##### DateUtil.dateDiff((Date)matDt,(Date)currDt) "+DateUtil.dateDiff((Date)matDt,(Date)currDt));
                        if (DateUtil.dateDiff((Date) matDt, (Date) currDt) > 0) {
                            String specialRd = "N";
                            HashMap depRecMap = new HashMap();
                            depRecMap.put("ACCOUNTNO", deposit_No + "_1");
                            List lstRec = sqlMap.executeQueryForList("checkFullInsPaidRD", depRecMap);
                            if (lstRec != null && lstRec.size() > 0) {
                                List recurringLst = sqlMap.executeQueryForList("getRecurringDepositDetails", recurMap);
                                if (recurringLst != null && recurringLst.size() > 0) {
                                    int addMaturityDays = 0;
                                    recurringMap = new HashMap();
                                    recurringMap = (HashMap) recurringLst.get(0);
                                    if(recurringMap.containsKey("SPECIAL_RD") && recurringMap.get("SPECIAL_RD") != null){
                                        specialRd = CommonUtil.convertObjToStr(recurringMap.get("SPECIAL_RD"));
                                    }
                                    if(!specialRd.equals("Y")){
                                    addMaturityDays = CommonUtil.convertObjToInt(recurringMap.get("MATURITY_DT_LASTINSTALL"));
                                    currDt = DateUtil.addDays(currDt, addMaturityDays);
                                    recurringMap.put("MATURITY_DT", currDt);
                                    recurringMap.put("DEPOSIT_NO", deposit_No);
                                    sqlMap.executeUpdate("updateMaturityDate", recurringMap);
                                    }
                                }
                            }
                        }
                    }
                    //added by rishad 28/08/2016
                     if (behavesLike.equals("THRIFT") || behavesLike.equals("BENEVOLENT")) {
                        int num = 0;
                        HashMap whereMap = new HashMap();
                        whereMap.put("ACCOUNTNO", deposit_No + "_1");
                        List lstRecsl = sqlMap.executeQueryForList("getDepositRecuringSlNo", whereMap);
                        if (lstRecsl != null && lstRecsl.size() > 0) {
                            HashMap resultMap = new HashMap();
                            resultMap = (HashMap) lstRecsl.get(0);
                            num = CommonUtil.convertObjToInt(resultMap.get("SL_NO"));
                        }
                        currAmt = CommonUtil.convertObjToDouble(updateMap.get("AMOUNT")).doubleValue();
                        // depAmt = CommonUtil.convertObjToDouble(recurMap.get("DEPOSIT_AMT")).doubleValue();
                        System.out.println("####currAmt :" + currAmt);
                        //  System.out.println("####depAmt :" + depAmt);
                        double depAdt = depAmt + adtAmt;
                        installPaid = currAmt / depAdt;
                        System.out.println("instalent paid" + installPaid);
                        //Added by nithya on 16-04-2018 for 	0009935: Thrift/Benevolent Interest Processing.. After interest application no need to insert the data in Deposit Recurring Table.
                        if(behavesLike.equals("THRIFT") && updateMap.containsKey("INSTRUMENT1") && updateMap.get("INSTRUMENT1") != null && (CommonUtil.convertObjToStr(updateMap.get("INSTRUMENT1")).trim()).equalsIgnoreCase("THRIFT INTEREST PAID".trim())){
                            installPaid = 0;
                        }
                        // System.out.println("behaveslike : " + behavesLike +"::installPaid ::" + installPaid);
                        // End of 0009935
                        for (int i = 0; i < installPaid; i++) {
                            num = num + 1;
                            HashMap transInMap = new HashMap();
                            transInMap.put("TODAY_DT", updateMap.get("TODAY_DT"));
                            transInMap.put("ACCOUNTNO", deposit_No + "_1");
                            transInMap.put("DELAYED_COUNT", new Double(0));
                            transInMap.put("AMOUNT", depAmt);
                            transInMap.put("DUE_DATE", updateMap.get("TODAY_DT"));
                            transInMap.put("SL_NO", new Double(num));
                            transInMap.put("ADT_AMT", adtAmt);
                            System.out.println("###transInMap :" + transInMap);
                            sqlMap.executeUpdate("insertRecurringDeposit", transInMap);
                        }
                    }
//                      if (behavesLike.equals("THRIFT") || behavesLike.equals("BENEVOLENT")) {
//                        HashMap depRecMap = new HashMap();
//                        depRecMap.put("ACCOUNTNO", deposit_No + "_1");
//                        List lstRec = sqlMap.executeQueryForList("getdepositInstNo", depRecMap);
//                        if (lstRec != null && lstRec.size() > 0) {
//                            HashMap valMap = (HashMap) lstRec.get(0);
//                            double instNo = 0;
//                            Date inst_date = null;
//                            if (valMap != null) {
//                                instNo = CommonUtil.convertObjToInt(valMap.get("DEP_INST_NO"));
//                                inst_date =  setProperDtFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(valMap.get("INT_CALC_UPTO_DT"))),branchCode);
//
//                                double amt = instNo * depAmt;
//                                double addAounmt = instNo * adtAmt;
//                                HashMap transInMap = new HashMap();
//                                transInMap.put("TODAY_DT", inst_date);
//                                transInMap.put("ACCOUNTNO", deposit_No + "_1");
//                                transInMap.put("DELAYED_COUNT", new Double(0));
//                                transInMap.put("AMOUNT", amt);
//                                transInMap.put("DUE_DATE", inst_date);
//                                transInMap.put("SL_NO", new Double(0));
//                                transInMap.put("ADT_AMT", addAounmt);
//                                System.out.println("###transInMap :" + transInMap);
//                                sqlMap.executeUpdate("insertRecurringDeposit", transInMap);
//
//                            }
//                        }
//                        
//                     
//                    }
                    elseMap = null;
                }
            }
            if (isDailyDeposit(act_num)) {
                updateMap.put("AGENT_ID", updateMap.get("PARTICULARS"));
                updateMap.remove("PARTICULARS");
                List lst = (List) sqlMap.executeQueryForList("getAmountDailyDeposit", updateMap);
                double amount = 0.0;
                HashMap hash = new HashMap();
                if (lst != null && lst.size() > 0) {
                    hash = (HashMap) lst.get(0);
                    if (hash.size() > 0) {
                        amount = CommonUtil.convertObjToLong(hash.get("TOTAL_AMOUNT"));
                    }
                }
                double amt = CommonUtil.convertObjToLong(updateMap.get("AMOUNT"));
                double totAmount = (amt + amount);
                updateMap.put("TOTAL_AMOUNT", new Double(totAmount));
                sqlMap.executeUpdate("insertDailyDeposit", updateMap);
                lst = null;
                hash = null;
            }
        }
        updateMap = null;
    }

    private boolean isRecurringDeposit(String actNum) throws Exception {
        boolean recurring = false;
        int cnt = CommonUtil.convertObjToInt(sqlMap.executeQueryForObject("getRecurringDeposit", actNum));
        if (cnt > 0) {
            recurring = true;
        }
        return recurring;
    }

    private boolean isDailyDeposit(String actNum) throws Exception {
        boolean daily = false;
        int cnt = CommonUtil.convertObjToInt(sqlMap.executeQueryForObject("getDailyDeposit", actNum));
        if (cnt > 0) {
            daily = true;
        }
        return daily;
    }

    public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
        if ((mod < (roundingFactor / 2)) || (mod < (roundingFactorOdd / 2))) {
            return lower(number, roundingFactor);
        } else {
            return higher(number, roundingFactor);
        }
    }

    public long lower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    public long higher(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        if (mod == 0) {
            return number;
        }
        return (number - mod) + roundingFactor;
    }

    public long roundOffLower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    public void validateRules(HashMap validateMap, boolean isException) throws Exception {
        // Validation only for Debit
        String act_num = (String) validateMap.get(TransactionDAOConstants.ACCT_NO);

        validateMap.put(TransactionDAOConstants.PROD_TYPE, TransactionFactory.DEPOSITS);

        RuleEngine engine = new RuleEngine();
        RuleContext context = new RuleContext();

        if (act_num != null && !act_num.equalsIgnoreCase("") && !isException) {
             //added by Rishad For Account Validate 10/12/2019
             context.addRule(new AccountCheckingRule());
            if (!((String) validateMap.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
                context.addRule(new DeathMarkedRule());
            }
//            context.addRule(new AddressVerificationRule());
//            context.addRule(new ConfirmThanxRule());
        }

        if (((String) validateMap.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {

            //Double amount = (Double)validateMap.put(TransactionDAOConstants.AMT,new Double(0.0));
            if (act_num != null && !act_num.equalsIgnoreCase("") && !isException) {
                //   context.addRule(new LimitCheckingRule());
            }
            //context = addRuleToContext(validateMap,context);

            //To apply business rule
            if (validateMap.get(TransactionDAOConstants.INSTRUMENT_TYPE) != null) {
                if (((String) validateMap.get(TransactionDAOConstants.INSTRUMENT_TYPE)).equalsIgnoreCase(TransactionDAOConstants.DD)) {
                    /// context.addRule(new DraftInstrumentRule());
                    // context.addRule(new DateCheckingRule());
                } else if (((String) validateMap.get(TransactionDAOConstants.INSTRUMENT_TYPE)).equalsIgnoreCase(TransactionDAOConstants.CHEQUE)) {
                    // context.addRule(new ChequeInstrumentRule());
                    // context.addRule(new DateCheckingRule());
                }
            }
            //validateMap.put(TransactionDAOConstants.AMT,amount);
        }

        ArrayList list = (ArrayList) engine.validateAll(context, validateMap);

        if (list != null) {
            HashMap exception = new HashMap();
            exception.put(CommonConstants.EXCEPTION_LIST, list);
            exception.put(CommonConstants.CONSTANT_CLASS, TransactionDAOConstants.TRANS_RULE_MAP);
            System.out.println("Exception List : " + list);
            throw new TTException(exception);
        }

        context = null;
        engine = null;
    }

    public void updateGL(String acctHead, Double amount, LogTO objLogTO, HashMap ruleMap) throws Exception {
//        String transOwner = objLogTO.getBranchId();
//        String acctOwner = objLogTO.getBranchId();

//        if (ruleMap != null && !CommonUtil.convertObjToStr(ruleMap.get(TransactionDAOConstants.ACCT_NO)).equals("")) {
//            // get the branch for the a/c and override acctOwner variable
//            String acctNo = CommonUtil.convertObjToStr(ruleMap.get(TransactionDAOConstants.ACCT_NO));
//            if (acctNo.lastIndexOf("_")!=-1)
//                acctNo = acctNo.substring(0,acctNo.lastIndexOf("_"));
//            acctOwner = CommonUtil.convertObjToStr(sqlMap.executeQueryForObject("getBranchTD", acctNo));
//        }
        String transOwner = CommonUtil.convertObjToStr(ruleMap.get(TransactionDAOConstants.INITIATED_BRANCH));
        String acctOwner = CommonUtil.convertObjToStr(ruleMap.get(TransactionDAOConstants.BRANCH_CODE));

        if (ruleMap.containsKey("IS_INTER_BRANCH_TRANS")
                && new Boolean(CommonUtil.convertObjToStr(ruleMap.get("IS_INTER_BRANCH_TRANS"))).booleanValue()) {
            transOwner = "";
        } else if (transOwner.length() == 0) {
            throw new TTException("Initiated Brach Should not null ");
        }
//        System.out.println("#$#$#$ transOwner : "+transOwner+"  /   acctOwner : "+acctOwner);
        /**
         * Inter Branch Transaction
         */
        if (!transOwner.equals(acctOwner)) {
//            String transOwner = objLogTO.getBranchId();
//            String acctOwner = ServerConstants.HO;

            InterBranchTransaction objInterBranch = new InterBranchTransaction(transOwner, acctOwner);
            objInterBranch.doInterBranchTransaction(acctHead, amount, objLogTO, ruleMap);
            ruleMap.put("INTER_BRANCH_TRANS", new Boolean(true));

//        if (!transOwner.equalsIgnoreCase(acctOwner)) {
//            InterBranchTransaction objInterBranch = new InterBranchTransaction(transOwner, acctOwner);
//            objInterBranch.doInterBranchTransaction (acctHead, amount, objLogTO, ruleMap);
//            ruleMap.put("INTER_BRANCH_TRANS",new Boolean(true)); // This line added by Rajesh
//        } 
        } else {
            super.updateGL(acctHead, amount, objLogTO, ruleMap);
        }
    }
     private Date setProperDtFormat(Date dt,String branchCode) {
        Date tempDt =  (Date) ServerUtil.getCurrentDate(branchCode);
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
}
