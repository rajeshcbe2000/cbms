/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ConfirmThanxRule.java
 *
 * Created on March 31, 2005, 12:38 PM
 */

package com.see.truetransact.businessrule.transaction;


import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.exceptionconstants.transaction.TransactionConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;

import java.util.HashMap;
import java.util.List;
import com.ibatis.db.sqlmap.SqlMap;

/**
 *
 * @author  152721
 */
public class ConfirmThanxRule extends ValidationRule{
    private SqlMap sqlMap = null;
    final String YES = "Y";
    
    /** Creates a new instance of ConfirmThanxRule */
    public ConfirmThanxRule() throws Exception {
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    
    /*
     * To Validate the Condition depending on the Value Passed in the HashMap from the DAO.
     */
    public void validate(HashMap inputMap) throws Exception {
        getTarget(inputMap);
    }
    
    private void getTarget(HashMap inputMap) throws Exception{
        List list = (List)sqlMap.executeQueryForList("getConfirmThanxData", inputMap);
        
        if(list!= null && list.size() > 0){
            HashMap confimMap = (HashMap)list.get(0);
            System.out.println("confimMap: " +confimMap);
            
            if(!CommonUtil.convertObjToStr(confimMap.get("CONFIRM_THANKS")).equalsIgnoreCase(YES)){
                throw new ValidationRuleException(TransactionConstants.THNX_NOT_RECEIVED);
                
            }else{
                System.out.println("No Error in ConfirmThanxRule...");
            }
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        HashMap inputMap = new HashMap();
        inputMap.put("ACCOUNTNO", "D0001143_1");//__ OA060898, LA00000000001063
        try{
            ConfirmThanxRule cRule = new ConfirmThanxRule();
            cRule.validate(inputMap);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Done");
        
        
    }
    
}
