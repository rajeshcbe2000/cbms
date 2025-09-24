/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * WithdrawlSlipRule.java
 *
 * Created on March 14, 2005, 11:11 AM
 */

package com.see.truetransact.businessrule.transaction;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.ibatis.db.sqlmap.SqlMap;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;

import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.exceptionconstants.transaction.TransactionConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;


import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;

/**
 *
 * @author  152721
 */
public class WithdrawlSlipRule extends ValidationRule{
    private SqlMap sqlMap = null;
    private final String YES = "Y";
    
    /** Creates a new instance of WithdrawlSlipRule */
    public WithdrawlSlipRule() throws Exception {
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    
    public void validate(HashMap inputMap) throws Exception {
        getTarget(inputMap);
    }
    
    private void getTarget(HashMap inputMap) throws Exception{
        double amount =  CommonUtil.convertObjToDouble(inputMap.get("AMOUNT")).doubleValue();
        if(amount < 0 )
            amount = -1 * amount;
        
        ArrayList withDrawalList = new ArrayList();
        withDrawalList = (ArrayList)getWithDrawalSlipData(inputMap);
        
        if(withDrawalList.size() > 0){
            HashMap withDrawalMap = (HashMap)withDrawalList.get(0);
            System.out.println("withDrawalMap: " + withDrawalMap);
            //__ Check if the Withdrawal Slip is allowed or not...
            if(CommonUtil.convertObjToStr(withDrawalMap.get("SLIP_ALLOWED")).equalsIgnoreCase(YES)){
                
                //__ Check if the Amount allowed is Less than the specified Amount...
                if(amount >
                CommonUtil.convertObjToDouble(withDrawalMap.get("AMOUNT_ALLOWED")).doubleValue()){
                    //__ Throw Exception...
                    throw new ValidationRuleException(TransactionConstants.AMOUNT_EXCEEDS);
                }
            }else{
                //__ Throw Exception...
                throw new ValidationRuleException(TransactionConstants.WITHDRAWAL_SLIP_NOT_ALLOWED);
            }
        }
    }
    
    //__ To get the data from the Operative Account Product table(s)...
    private List getWithDrawalSlipData(HashMap inputMap) throws Exception{
        String prodType = CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.PROD_TYPE));
        List list = sqlMap.executeQueryForList("getWithdrawalSlipData"+prodType ,inputMap);
        return list;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        HashMap inputMap = new HashMap();
        inputMap.put("AMOUNT",String.valueOf(6000));
        inputMap.put(TransactionDAOConstants.PROD_TYPE, "OA");
        inputMap.put("ACCOUNTNO", "OA001002");
        
        try{
            WithdrawlSlipRule wsRule = new WithdrawlSlipRule();
            wsRule.getTarget(inputMap);
            System.out.println("Done");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
