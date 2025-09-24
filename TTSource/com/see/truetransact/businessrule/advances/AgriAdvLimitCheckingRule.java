/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriAdvLimitCheckingRule.java
 *
 * Created on May 5, 2005, 12:54 PM
 */

package com.see.truetransact.businessrule.advances;

import java.util.HashMap;
import java.util.List;

import com.ibatis.db.sqlmap.SqlMap;

import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.exceptionconstants.advances.AdvancesConstants;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
/**
 *
 * @author  152713
 */
public class AgriAdvLimitCheckingRule extends ValidationRule{
    private SqlMap sqlMap = null;
    /** Creates a new instance of LimitCheckingRule */
    public AgriAdvLimitCheckingRule() throws Exception {
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    
    public void validate(HashMap inputMap) throws Exception {
        getTarget(inputMap);
    }
    
    /**
     * Method To get the Data from the Database to be Compared
     * The inputMap is passed from the DAO.
     */
    private void getTarget(HashMap inputMap) throws Exception{
        System.out.println("###$$ LimitCheckingRule : inputMap : "+inputMap);
        double amount =  CommonUtil.convertObjToDouble(inputMap.get("AMOUNT")).doubleValue();
        if (amount < 0)
            amount = -amount;
        
        /*
         * The Mapped Statement "getTLBalance" is used to get the values of 
         * the Available, Clear, Shadow, Limit balances.
         */
        List list = (List) sqlMap.executeQueryForList("getATLBalance", inputMap);
        List calDrawAmtlist = (List) sqlMap.executeQueryForList("getTotCalculatedDrawingAmountAAD", inputMap);
        HashMap resultMap = (HashMap)list.get(0);
        double subLimit= CommonUtil.convertObjToDouble(sqlMap.executeQueryForObject("getAgriSubLimitATL", inputMap)).doubleValue();
        double availableBalance = CommonUtil.convertObjToDouble(resultMap.get("AVAILABLE_BALANCE")).doubleValue();
        double clearBalance = CommonUtil.convertObjToDouble(resultMap.get("CLEAR_BALANCE")).doubleValue();
        double shadowCredit = CommonUtil.convertObjToDouble(resultMap.get("SHADOW_CREDIT")).doubleValue();
        double shadowDebit = CommonUtil.convertObjToDouble(resultMap.get("SHADOW_DEBIT")).doubleValue();
        double limit = CommonUtil.convertObjToDouble(resultMap.get("LIMIT")).doubleValue();
        double limitAmt = availableBalance;
        double calDrawingAmt = 0.0;
        
        String status = CommonUtil.convertObjToStr(resultMap.get("STATUS"));
        
        list = null;
        resultMap = null;
        
        if (calDrawAmtlist.size() > 0){
            resultMap = (HashMap)calDrawAmtlist.get(0);;
            calDrawingAmt = CommonUtil.convertObjToDouble(resultMap.get("LIMIT")).doubleValue();
            
            calDrawAmtlist = null;
            resultMap = null;
        }
        
        if(CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
            /* If the Limit amount in Facility details is less than 
             *   the Total calculated drawing amount then the 'amount' should be less than 
             *   the facility detail's limit amount 
             * else
             *   'amount' should be less than the Total calculated drawing amount
             */
            
            if (limit < calDrawingAmt){
                limitAmt = calDrawingAmt + availableBalance;
            } 
//            else
//            {
//                limitAmt = limit + availableBalance;
//            }
            if (inputMap.get(TransactionDAOConstants.PROD_TYPE).equals("AAD")) {
                limitAmt=availableBalance;
            } else {
                if(limit<availableBalance)
                    limitAmt=limit;
                else
                    limitAmt=availableBalance;
            }
            if (limitAmt < amount) {
                System.out.println("limitAmt : amount : " + limitAmt + ":" + amount);
                throw new ValidationRuleException(AdvancesConstants.ADVANCES_EXCEED_LIMIT);
            }
            if(subLimit<amount && clearBalance<amount)
                 throw new ValidationRuleException(AdvancesConstants.EXCEED_SUB_LIMIT);
            
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
