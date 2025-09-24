/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AccountMaintenanceRule.java
 *
 * Created on July 2, 2004, 12:05 PM
 */

package com.see.truetransact.businessrule.generalledger;

import java.util.HashMap;
import java.util.List;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.exceptionconstants.generalledger.GeneralLedgerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverutil.ServerConstants;

/**
 *
 * @author  rahul
 * AccountMaintenanceRule Checks for the following:
 * a) Does the given AccountHead Already Exists?
 * b) If yes, Does its Parameters alredy defined?
 */
public class AccountMaintenanceRule extends ValidationRule{
    private SqlMap sqlMap = null;
    
    /** Creates a new instance of AccountMaintenanceRule */
    public AccountMaintenanceRule() throws Exception {
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    
    /* 
     * To Validate the Condition depending on the Value Passed in the HashMap from the DAO.
     */
    public void validate(HashMap inputMap) throws Exception {
        getTarget(inputMap);
    }
    
    /*
     * Method To get the Data from the Database to be Compared...
     * inputMap passed from the DAO...
     */
    private void getTarget(HashMap inputMap) throws Exception{
        boolean check;
        
        /* The Mapped Statement "accMaintenance.getAcHead" and "accMaintenance.getAcHeadParam"  are
         * in AccountMaintenanceMap; and are used to Know if the
         * AccountHead is Vaild one or not.
         */
        List accountHeadList = (List) sqlMap.executeQueryForList("accMaintenance.getAcHead", inputMap);
        
        // If the AccountHead is a valid one, chech if its parameters already exist or not. 
        if( accountHeadList.size() > 0 ){
            List accountHeadParamList = (List) sqlMap.executeQueryForList("accMaintenance.getAcHeadParam", inputMap);
            if( !(accountHeadParamList.size() > 0) ){
                System.out.println("Parameters does not exist...");
                throw new ValidationRuleException(GeneralLedgerConstants.ACCOUNTHEADPARAM);
            }else{
                System.out.println("Account Head Along with the Parameters, exits.");
            }
        }else {
            System.out.println("Account Head does not exist...");
            throw new ValidationRuleException(GeneralLedgerConstants.ACCOUNTHEAD);
        }
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        HashMap inputMap = new HashMap();
        inputMap.put("ACCT_HD", "SBIBK" );
        
        AccountMaintenanceRule aMRule = new AccountMaintenanceRule();
        aMRule.validate(inputMap);
    }
    
}
