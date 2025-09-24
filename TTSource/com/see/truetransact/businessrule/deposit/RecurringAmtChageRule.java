/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * RecurringAmtChageRule.java
 *
 * Created on March 14, 2005, 12:03 PM
 */

package com.see.truetransact.businessrule.deposit;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import com.ibatis.db.sqlmap.SqlMap;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.exceptionconstants.deposit.DepositConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 *
 * @author  152721
 */
public class RecurringAmtChageRule extends ValidationRule{
    private SqlMap sqlMap = null;
    private final String DECREASE = "DECREASE";
    private final String INCREASE = "INCREASE";
    
    
    /** Creates a new instance of RecurringAmtChageRule */
    public RecurringAmtChageRule() throws Exception {
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    
    public void validate(HashMap inputMap) throws Exception {
        super._branchCode = CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.BRANCH_CODE));
        getTarget(inputMap);
    }
    
    private void getTarget(HashMap inputMap) throws Exception{
        ArrayList recurringList = new ArrayList();
        recurringList = (ArrayList)getRecurringDepositData(inputMap);
        
        //__ To Check if the Data for the Particular Account No Exists or not...
        if(recurringList.size() > 0){
            HashMap resultMap = (HashMap)recurringList.get(0);
            
            //__ Check if the Time to Chang the Installment Amount has Come or not... 
            if(DateUtil.dateDiff(ServerUtil.getCurrentDate(super._branchCode), (Date)resultMap.get("DDATE")) > 0){
                
                final String recVal = CommonUtil.convertObjToStr(resultMap.get("CHANGE_VALUE"));
                //__ if the Change type is DECREASE
                if(recVal.equalsIgnoreCase(DECREASE)){
                    if(CommonUtil.convertObjToDouble(resultMap.get("AMOUNT")).doubleValue() <
                    CommonUtil.convertObjToDouble(inputMap.get("AMOUNT")).doubleValue()){
                        //__ Throw Exception...
                        throw new ValidationRuleException(DepositConstants.DECREASE_DEPOSIT_AMT);
                    }
                }
                //__ if the Change type is INCREASE
                else if(recVal.equalsIgnoreCase(INCREASE)){
                    if(CommonUtil.convertObjToDouble(resultMap.get("AMOUNT")).doubleValue() >
                    CommonUtil.convertObjToDouble(inputMap.get("AMOUNT")).doubleValue()){
                        //__ Throw Exception...
                        throw new ValidationRuleException(DepositConstants.INCREASE_DEPOSIT_AMT);
                    }
                }
            }else{
                //__ Throw Exception...
                throw new ValidationRuleException(DepositConstants.FREQ_NOT_ATTAINED);
            }
        }
    }
    
    //__ To get the data from the Deposit Data table(s)...
    private List getRecurringDepositData(HashMap inputMap) throws Exception{
        List list = sqlMap.executeQueryForList("getRecurringAmtChageData",inputMap);
        return list;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        HashMap inputMap = new HashMap();
        inputMap.put("DEPOSIT_NO","D0001115_1");
        inputMap.put("AMOUNT","5000");
        
        try{
            RecurringAmtChageRule rRule = new RecurringAmtChageRule();
            rRule.getTarget(inputMap);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
