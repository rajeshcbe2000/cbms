/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * PartialWithdrawalCheckingRule.java
 *
 * Created on June 23, 2004, 3:25 PM
 */

package com.see.truetransact.businessrule.deposit.closing;

import java.util.HashMap;
import java.util.List;
import java.util.Date;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.commonutil.exceptionconstants.deposit.closing.DepositClosingConstants;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.commonutil.DateUtil;

/**
 *
 * @author  Pinky
 */

public class PartialWithdrawalCheckingRule extends ValidationRule{
    private SqlMap sqlMap = null;   
    
    /** Creates a new instance of DateCheckingRule */
    public PartialWithdrawalCheckingRule() throws Exception{        
        final ServiceLocator locate = ServiceLocator.getInstance();
        //sqlMap = (SqlMap) locate.getDAOSqlMap();
    }    
    /**
     * To Validate the Condition depending on the Value Passed in the HashMap from the DAO...
     */
    public void validate(HashMap inputMap) throws Exception {
        getTarget(inputMap);
    }    
    /*
     * Method To get the Data from the Database to be Compared...
     * inputMap passed from the DAO...
     */
    private void getTarget(HashMap inputMap) throws Exception{           
        double maxPWNo = ((java.math.BigDecimal)inputMap.get("MAX_NOPW")).doubleValue();
        double maxPWAmt = ((java.math.BigDecimal)inputMap.get("MAX_WDAMT")).doubleValue();
        double maxPWAmtYear = ((java.math.BigDecimal)inputMap.get("MAX_WDAMT_YEAR")).doubleValue();
        double amtPW = ((Double)inputMap.get("WDAMT")).doubleValue();
        double noPW = ((Integer)inputMap.get("NOPW")).doubleValue();
        double yearAmtPW = ((Double)inputMap.get("YEARWDAMT")).doubleValue();
        
        System.out.println("PartialWithdrawal"+noPW);
        System.out.println(maxPWNo);
        
        if(noPW>maxPWNo)                
            throw new ValidationRuleException(DepositClosingConstants.NOPWVALUE);       
        if(amtPW>maxPWAmt)            
            throw new ValidationRuleException(DepositClosingConstants.MAXPWAMOUNT);        
        if(yearAmtPW>maxPWAmtYear)                   
            throw new ValidationRuleException(DepositClosingConstants.YEARPWAMOUNT);                          
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        try{
        HashMap inputMap = new HashMap();
        inputMap.put("MAX_NOPW",new java.math.BigDecimal(5));        
        inputMap.put("MAX_WDAMT", new java.math.BigDecimal(800));        
        inputMap.put("MAX_WDAMT_YEAR", new java.math.BigDecimal(50));        
        inputMap.put("NOPW",new java.math.BigDecimal(6));        
        inputMap.put("WDAMT", new java.math.BigDecimal(400));        
        inputMap.put("YEARWDAMT", new java.math.BigDecimal(500));        
        PartialWithdrawalCheckingRule dRule = new PartialWithdrawalCheckingRule();
        dRule.validate(inputMap);
        }catch(Throwable t){
            t.printStackTrace();
        }
        //System.out.println("no exception");
    }    
}

