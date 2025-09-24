/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriLoanLimitCheckingRule.java
 *
 * Created on March 5, 2005, 6:19 PM
 */

package com.see.truetransact.businessrule.termloan.agritermloan;

import java.util.HashMap;
import java.util.List;
import java.util.Date;
import com.ibatis.db.sqlmap.SqlMap;

import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.exceptionconstants.termloan.TermLoanConstants;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
/**
 *
 * @author  152713
 */
public class AgriLoanLimitCheckingRule extends ValidationRule{
    private SqlMap sqlMap = null;
    /** Creates a new instance of LoanLimitCheckingRule */
    public AgriLoanLimitCheckingRule() throws Exception{
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    /**
     * To Validate the Condition depending on the Value Passed in the HashMap from the DAO
     */
    public void validate(HashMap inputMap) throws Exception {
        super._branchCode = CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.BRANCH_CODE));
        
        getTarget(inputMap);
    }
    
    /**
     * Method To get the Data from the Database to be Compared
     * The inputMap is passed from the DAO.
     */
    private void getTarget(HashMap inputMap) throws Exception{
        System.out.println("loansLimitCheckingRule####"+inputMap);
        double amount =  CommonUtil.convertObjToDouble(inputMap.get("AMOUNT")).doubleValue();
        if (amount < 0)
            amount = -amount;
        
        /*
         * The Mapped Statement "getTLBalance" is used to get the values of
         * the Available, Clear, Shadow, Limit balances.
         */
        List list = (List) sqlMap.executeQueryForList("getATLBalance", inputMap);
        HashMap resultMap = (HashMap)list.get(0);
        double subLimit= CommonUtil.convertObjToDouble(sqlMap.executeQueryForObject("getAgriSubLimitATL", inputMap)).doubleValue();
        double availableBalance = CommonUtil.convertObjToDouble(resultMap.get("AVAILABLE_BALANCE")).doubleValue();
        double clearBalance = CommonUtil.convertObjToDouble(resultMap.get("CLEAR_BALANCE")).doubleValue();
        double shadowCredit = CommonUtil.convertObjToDouble(resultMap.get("SHADOW_CREDIT")).doubleValue();
        double shadowDebit = CommonUtil.convertObjToDouble(resultMap.get("SHADOW_DEBIT")).doubleValue();
        double loanBalancePrincipal = CommonUtil.convertObjToDouble(resultMap.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();
        String status = CommonUtil.convertObjToStr(resultMap.get("STATUS"));
        String multipleDisbursementAllowed = CommonUtil.convertObjToStr(resultMap.get("MULTIPLE_DISBURSEMENT"));
        Date repaymentDate = (Date) resultMap.get("REPAYMENT_DATE");
        Date loan_expiry_dt = (Date) resultMap.get("EXPIRY_DT");
        Date loan_Start_Dt=(Date)resultMap.get("LOAN_START_DT");
        Date curr_dt=DateUtil.getDateWithoutMinitues(ServerUtil.getCurrentDate(_branchCode));
        int NO_MORATORIUM=CommonUtil.convertObjToInt(resultMap.get("NO_MORATORIUM"));
        list = null;
        resultMap = null;
        double totDisbursementAmt=0.0;
        loan_Start_Dt=DateUtil.getDateWithoutMinitues(loan_Start_Dt);
        if(CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.DEBIT) && DateUtil.dateDiff(loan_Start_Dt,curr_dt)<0) {
            throw new ValidationRuleException(TermLoanConstants.FUTURE_DATE);
        }
        
        if (inputMap.get(TransactionDAOConstants.TO_STATUS).equals(CommonConstants.STATUS_CREATED) && CommonUtil.convertObjToStr(inputMap.get("DEBIT_LOAN_TYPE")).equals("DP")){
            List debitChkList = sqlMap.executeQueryForList("getDisbursementDetailsATL", inputMap);
            HashMap debitChkMap = (HashMap) debitChkList.get(0);
            totDisbursementAmt=CommonUtil.convertObjToDouble(debitChkMap.get("TOTAMT")).doubleValue();
            int noDisbursements = CommonUtil.convertObjToInt(debitChkMap.get("NO_DISBURSEMENT"));
            System.out.println("#### noDisbursements : " + noDisbursements);
            if (multipleDisbursementAllowed.equals("N") && noDisbursements > 0){
                throw new ValidationRuleException(TermLoanConstants.MULTIPLE_DISBURSEMENT);
            }
            
            debitChkList = null;
            debitChkMap = null;
        }
        
        if(CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.DEBIT) &&  CommonUtil.convertObjToStr(inputMap.get("DEBIT_LOAN_TYPE")).equals("DP")) {
            if(clearBalance>0){
                if(clearBalance<amount){
                    throw new ValidationRuleException(TermLoanConstants.EXCEED_LIMIT);
                }
                
            }else if (availableBalance < amount) {
                throw new ValidationRuleException(TermLoanConstants.EXCEED_LIMIT);
            } else if(subLimit !=0 && (totDisbursementAmt+amount>subLimit)){
                //wont exceed subLimit
                throw new ValidationRuleException(TermLoanConstants.EXCEED_SUB_LIMIT);
                
            }
            
            
            
            //            if (inputMap.get(TransactionDAOConstants.TO_STATUS).equals(CommonConstants.STATUS_CREATED) && CommonUtil.convertObjToStr(inputMap.get("DEBIT_LOAN_TYPE")).equals("DP")){
            //                List debitChkList = sqlMap.executeQueryForList("getDisbursementDetailsATL", inputMap);
            //                HashMap debitChkMap = (HashMap) debitChkList.get(0);
            //                double totDisbursementAmt=CommonUtil.convertObjToDouble(debitChkMap.get("TOTAMT")).doubleValue();
            //                int noDisbursements = CommonUtil.convertObjToInt(debitChkMap.get("NO_DISBURSEMENT"));
            //                System.out.println("#### noDisbursements : " + noDisbursements);
            //                if (multipleDisbursementAllowed.equals("N") && noDisbursements > 0){
            //                    throw new ValidationRuleException(TermLoanConstants.MULTIPLE_DISBURSEMENT);
            //                }
            //
            //                debitChkList = null;
            //                debitChkMap = null;
            //            }
            
            //            if (NO_MORATORIUM >0 && repaymentDate.compareTo(ServerUtil.getCurrentDate(super._branchCode)) <= 0 ||loanBalancePrincipal<0){
            //                loanBalancePrincipal=-1*loanBalancePrincipal;
            //                if(amount>loanBalancePrincipal)
            //                    throw new ValidationRuleException(TermLoanConstants.DISBURSEMENT_DATE_EXPIRED);
            //            }
            //            else
            if(CommonUtil.convertObjToStr(inputMap.get("DEBIT_LOAN_TYPE")).equals("DP"))
                //                if(repaymentDate.compareTo(ServerUtil.getCurrentDate(super._branchCode)) < 0){loan_expiry_dt
                if(loan_expiry_dt.compareTo(ServerUtil.getCurrentDate(super._branchCode)) < 0){
                    throw new ValidationRuleException(TermLoanConstants.DISBURSEMENT_DATE_EXPIRED);
                }
            
        }else if(CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.CREDIT)) {
            
        }
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
