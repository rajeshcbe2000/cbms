/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * IntroducerVerificationRule.java
 *
 * Created on March 31, 2005, 3:19 PM
 */

package com.see.truetransact.businessrule.transaction;

import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.exceptionconstants.transaction.TransactionConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.serverutil.ServerUtil;

import java.util.HashMap;
import java.util.List;
import com.ibatis.db.sqlmap.SqlMap;

/**
 *
 * @author  152721
 */
public class IntroducerVerificationRule extends ValidationRule{
    private SqlMap sqlMap = null;
    final String YES = "Y";
    
    /** Creates a new instance of IntroducerVerificationRule */
    public IntroducerVerificationRule() throws Exception{
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap)locate.getDAOSqlMap();
    }
    
    /*
     * To Validate the Condition depending on the Value Passed in the HashMap from the DAO.
     */
    public void validate(HashMap inputMap) throws Exception {
        super._branchCode = CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.BRANCH_CODE));

        getTarget(inputMap);
    }
    
    private void getTarget(HashMap inputMap) throws Exception{
        inputMap.put("CURRENT_DT", ServerUtil.getCurrentDate(super._branchCode));
        
        List list = (List)sqlMap.executeQueryForList("getIntroducerVerificationData", inputMap);
        if(list!= null && list.size() > 0){
            HashMap introMap = (HashMap)list.get(0);
            System.out.println("introMap: " +introMap);
            
            if(!CommonUtil.convertObjToStr(introMap.get("CUST_ID")).equalsIgnoreCase("")){
                throw new ValidationRuleException(TransactionConstants.INTRO_NOT_VERIFIED);
                
            }else{
                System.out.println("No Error in IntroducerVerificationRule");
            }
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        HashMap inputMap = new HashMap();
        inputMap.put("ACCOUNTNO", "LA00000000001063");//__ OA060898, LA00000000001063, D0001143_1
        try{
            IntroducerVerificationRule iRule = new IntroducerVerificationRule();
            iRule.validate(inputMap);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Done");
    }
}
