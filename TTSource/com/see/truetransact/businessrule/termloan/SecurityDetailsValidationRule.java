/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * SecurityDetailsValidationRule.java
 *
 * Created on May 20, 2005, 12:32 PM
 */

package com.see.truetransact.businessrule.termloan;

import java.util.HashMap;
import java.util.List;

import com.ibatis.db.sqlmap.SqlMap;

import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.exceptionconstants.termloan.TermLoanConstants;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.commonutil.interestcalc.Rounding;

/**
 *
 * @author  152713
 */
public class SecurityDetailsValidationRule extends ValidationRule{
    private SqlMap sqlMap = null;
    private final String BEHAVES_LIKE = "BEHAVES_LIKE";
    private final String CC = "CC";
    private final String ELIGIBLE_LOAN_AMOUNT = "ELIGIBLE_LOAN_AMOUNT";
    private final String LOANS_AGAINST_DEPOSITS = "LOANS_AGAINST_DEPOSITS";
    private final String OD = "OD";
    
    /** Creates a new instance of SecurityDetailsValidationRule */
    public SecurityDetailsValidationRule() throws Exception{
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    
    public void validate(HashMap inputMap) throws Exception {
        super._branchCode = CommonUtil.convertObjToStr(inputMap.get(CommonConstants.BRANCH_ID));
        
        getTarget(inputMap);
    }
    /**
     * Method To get the Data from the Database to be Compared
     * The inputMap is passed from the DAO.
     */
    private void getTarget(HashMap inputMap) throws Exception{
        String strBehavesLike = CommonUtil.convertObjToStr(inputMap.get(BEHAVES_LIKE));
        int limitAmount = CommonUtil.convertObjToInt(inputMap.get("LIMIT"));
        int eligible_Loan_Amt = 0;
        double marginrate=0;
        if (strBehavesLike.length() <= 0){
            throw new ValidationRuleException(TermLoanConstants.NOT_VALID_PROD_ID);
        }
        if (!strBehavesLike.equals(LOANS_AGAINST_DEPOSITS)){
            /*
             * The Mapped Statement "getLoanSecurity_Eligible_Loan_Amt" is used
             * to get the eligible loan amount
             * using LOANS_SECURITY_DETAILS
             */
            List list=null;
            if(inputMap.containsKey("PROD_TYPE") && inputMap.get("PROD_TYPE") !=null && inputMap.get("PROD_TYPE").equals("ATL"))
                list = (List) sqlMap.executeQueryForList("getAgriLoanSecurity_Eligible_Loan_Amt", inputMap);
            else
                list = (List) sqlMap.executeQueryForList("getLoanSecurity_Eligible_Loan_Amt", inputMap);
            if (list.size() > 0){
                HashMap resultMap = (HashMap)list.get(0);
                
//                eligible_Loan_Amt = CommonUtil.convertObjToInt(resultMap.get(ELIGIBLE_LOAN_AMOUNT));
                eligible_Loan_Amt = limitAmount;
                
                resultMap = null;
            }else{
                throw new ValidationRuleException(TermLoanConstants.INSUFFICIENT_SECURITY_DETAILS);
            }
            list = null;
        }else if (strBehavesLike.equals(LOANS_AGAINST_DEPOSITS)){
            /*
             * The Mapped Statement "getDepositLienAmount" is used to get the deposit Lien amount
             *  using DEPOSIT_LIEN
             */
            List list = (List) sqlMap.executeQueryForList("getDepositLienAmount", inputMap);
            if (list.size() > 0){
                HashMap resultMap = (HashMap)list.get(0);
                
                eligible_Loan_Amt = CommonUtil.convertObjToInt(resultMap.get(ELIGIBLE_LOAN_AMOUNT));
                
                resultMap = new HashMap();
             list = (List) sqlMap.executeQueryForList("TermLoan.getProdHead", inputMap);
            if (list.size() > 0){
                 resultMap = (HashMap)list.get(0);
                 marginrate=CommonUtil.convertObjToDouble(resultMap.get("DEP_ELIGIBLE_LOAN_AMT")).doubleValue();
                 String roundOff=CommonUtil.convertObjToStr(resultMap.get("DEPOSIT_ROUNDOFF"));
                 marginrate=marginrate/100;
                 System.out.println("marginrate  ###"+marginrate+"  "+limitAmount);
                 double totalLien=limitAmount/marginrate;
                 totalLien=roundOffDepositLien(totalLien,roundOff);
                   System.out.println("totalLien  ###"+totalLien);
                   if(totalLien>eligible_Loan_Amt)
                       throw new ValidationRuleException(TermLoanConstants.INSUFFICIENT_SECURITY_DETAILS);
            }
             
            }else{
                throw new ValidationRuleException(TermLoanConstants.INSUFFICIENT_SECURITY_DETAILS);
            }
            list = null;
        }
        
        if (!(eligible_Loan_Amt >= limitAmount && eligible_Loan_Amt > 0)){
            throw new ValidationRuleException(TermLoanConstants.INSUFFICIENT_SECURITY_DETAILS);
        }
    }
        /*
         *loan against deposit product level parameter eligible marging round off value
         * everthing chceking purpose*/
    
     private double roundOffDepositLien(double lienAmt,String roundOff){
//        roundOff=CommonUtil.convertObjToStr(resultMap.get("DEPOSIT_ROUNDOFF"));
        long roundOffValue=0;
//        long finalLien=0;
        double finalLien=0.0;
        long longLien=(long)lienAmt;
//            if(roundOff.length()>0){
//                if(roundOff .equals("NEAREST_TENS")){
//                    roundOffValue=10;
//            }
//                else   if(roundOff .equals("NEAREST_HUNDREDS")){
//                     roundOffValue=100;
//            }
//                else  if(roundOff .equals("NEAREST_VALUE")){
                     roundOffValue=1;
//            } else  if(roundOff .equals("NO_ROUND_OFF")){
//                   return finalLien;
//            }
//        }
//         long lienAmt=(long)(enterAmt/eligibleMargin);
         Rounding rd=new Rounding();
         finalLien= rd.getNearestHigher(lienAmt,roundOffValue); //longLien
         return finalLien;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
