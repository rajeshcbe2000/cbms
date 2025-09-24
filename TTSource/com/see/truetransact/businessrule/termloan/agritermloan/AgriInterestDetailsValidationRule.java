/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriInterestDetailsValidationRule.java
 *
 * Created on May 20, 2005, 12:31 PM
 */

package com.see.truetransact.businessrule.termloan.agritermloan;

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

/**
 *
 * @author  152713
 */
public class AgriInterestDetailsValidationRule extends ValidationRule{
    private SqlMap sqlMap = null;
    
    private final String ACT = "ACT";
    private final String AMOUNT = "AMOUNT";
    private final String BEHAVES_LIKE = "BEHAVES_LIKE";
    private final String CATEGORY = "CATEGORY";
    private final String CATEGORY_ID = "CATEGORY_ID";
    private final String CC = "CC";
    private final String FROM_DATE = "FROM_DATE";
    private final String INT_GET_FROM = "INT_GET_FROM";
    private final String LIMIT = "LIMIT";
    private final String LOANS_AGAINST_DEPOSITS = "LOANS_AGAINST_DEPOSITS";
    private final String NO_RECORDS = "NO_RECORDS";
    private final String OD = "OD";
    private final String PROD = "PROD";
    private final String TO_DATE = "TO_DATE";
    
    /** Creates a new instance of InterestDetailsValidationRule */
    public AgriInterestDetailsValidationRule() throws Exception{
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
        String strIntGetFrom = CommonUtil.convertObjToStr(inputMap.get(INT_GET_FROM));
        if (strBehavesLike.length() <= 0){
            throw new ValidationRuleException(TermLoanConstants.NOT_VALID_PROD_ID);
        }
        if (strIntGetFrom.length() <= 0){
            throw new ValidationRuleException(TermLoanConstants.NO_INTEREST_DETAILS);
        }else if (strIntGetFrom.equals(ACT)){
            List list=null;
            if(inputMap.containsKey("PROD_TYPE") && inputMap.get("PROD_TYPE").equals("ATL"))
                list = (List) sqlMap.executeQueryForList("getAgriAcctInterestDetailsCount", inputMap);
            else
                list = (List) sqlMap.executeQueryForList("getAcctInterestDetailsCount", inputMap);
            if (list.size() > 0){
                HashMap retrieveMap = (HashMap) list.get(0);
                if (CommonUtil.convertObjToInt(retrieveMap.get(NO_RECORDS)) <= 0){
                    throw new ValidationRuleException(TermLoanConstants.NO_INTEREST_DETAILS);
                }
            }else{
                throw new ValidationRuleException(TermLoanConstants.NO_INTEREST_DETAILS);
            }
        }else if (strIntGetFrom.equals(PROD)){
            // To get the From Dt, To Dt and Limit of the particular Loan
            List list = (List) sqlMap.executeQueryForList("getIntDetailsWhereConditions", inputMap);            
            if (list.size() > 0){
                HashMap retrieveMap = (HashMap) list.get(0);
                retrieveMap.put(CATEGORY_ID, retrieveMap.get(CATEGORY));
                retrieveMap.put(AMOUNT, retrieveMap.get(LIMIT));
                // Add code for making interest periodwise : Added by nithya on 30-10-2017 for 7867
                List intCalcMethodLst = (List) sqlMap.executeQueryForList("checkIfIntCalcPeriodSlabWiseOrNot", retrieveMap);
                if (intCalcMethodLst != null && intCalcMethodLst.size() > 0) {
                    HashMap intCalcMethodMap = (HashMap) intCalcMethodLst.get(0);
                    if (intCalcMethodMap.containsKey("IS_INT_PERIOD_SLAB_CALC") && intCalcMethodMap.get("IS_INT_PERIOD_SLAB_CALC") != null && !"".equals(intCalcMethodMap.get("IS_INT_PERIOD_SLAB_CALC"))) {
                        if (CommonUtil.convertObjToStr(intCalcMethodMap.get("IS_INT_PERIOD_SLAB_CALC")).equalsIgnoreCase("Y")) {
                            retrieveMap.put("PERIOD_WISE_INT_RATE", "PERIOD_WISE_INT_RATE");
                            retrieveMap.put("INT_UP_TO_DATE", ServerUtil.getCurrentDate(_branchCode));
                        } else {
                            retrieveMap.put("AMOUNT_WISE_INT_RATE", "AMOUNT_WISE_INT_RATE");
                        }
                    } else {
                        retrieveMap.put("AMOUNT_WISE_INT_RATE", "AMOUNT_WISE_INT_RATE");
                    }
                } else {
                    retrieveMap.put("AMOUNT_WISE_INT_RATE", "AMOUNT_WISE_INT_RATE");
                }
                List interestList = (List) sqlMap.executeQueryForList("getSelectProductTermLoanInterestMap", retrieveMap);
                if (interestList.size() <= 0) {
                    throw new ValidationRuleException(TermLoanConstants.NO_INTEREST_DETAILS);
                }
            }else{
                throw new ValidationRuleException(TermLoanConstants.NO_INTEREST_DETAILS);
            }
            
        }else{
            throw new ValidationRuleException(TermLoanConstants.NO_INTEREST_DETAILS);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
