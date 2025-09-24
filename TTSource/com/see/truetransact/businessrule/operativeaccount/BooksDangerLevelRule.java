/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * BooksDangerLevelRule.java
 *
 * Created on August 30, 2004, 12:33 PM
 */

package com.see.truetransact.businessrule.operativeaccount;


import java.util.HashMap;
import java.util.List;

import com.ibatis.db.sqlmap.SqlMap;

import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.exceptionconstants.operativeaccount.OperativeAccountConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
/**
 *
 * @author  rahul
 *
 * BooksDangerLevelRule Checks for the following:
 * a) If the Cheque books level is less than the Danger Level or not...
 */
public class BooksDangerLevelRule extends ValidationRule{
    private SqlMap sqlMap = null;
    
    /** Creates a new instance of BooksDangerLevelRule */
    public BooksDangerLevelRule() throws Exception{
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    
    /** To Validate the Condition depending on the Value Passed in the HashMap from the DAO.*/
    public void validate(HashMap inputMap) throws Exception {
        getTarget(inputMap);
    }
    
    /* 
      * Method To get the Data from the Database to be Compared...
      * inputMap passed from the DAO.
      */
    
    private void getTarget(HashMap inputMap) throws Exception{
        boolean check;
        
        /* 
         * The Mapped Statement "getBooksDangerLevelRule" is in ChequeBookMap; and is
         * used to Know if the Available Books are less than the Danger level or not...
         */
        List booksDangerLevel = (List) sqlMap.executeQueryForList("getBooksDangerLevelRule", inputMap);
        
        if(booksDangerLevel!=null){
            if(booksDangerLevel.size() > 0){
//                final String[] options = {"Ok"};
//                System.out.println("booksDangerLevel size: "+ booksDangerLevel.size());
//                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
//                final int option = javax.swing.JOptionPane.showOptionDialog(null, "Address of the Customer not Verified yet.",
//                com.see.truetransact.commonutil.CommonConstants.WARNINGTITLE,
//                javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE,
//                null, options, options[0]);
//                javax.swing.JOptionPane.showMessageDialog(null, "Available Cheque Books are less than the Danger Level");
                throw new ValidationRuleException(OperativeAccountConstants.BOOKS_DANGER_LEVEL);   // Commented by Rajesh.
            }
        }
//        else{
//            throw new ValidationRuleException(TransactionConstants.DRAFT_NOT_ISSUED);
//        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        HashMap inputMap = new HashMap();
        inputMap.put("ITEM_ID", "II001005" );
        inputMap.put("ITEM_SUB_TYPE", "CURRENT_ACCT_CHEQUES");
        inputMap.put("LEAVES_PER_BOOK", "100");
        inputMap.put("BRANCH_ID", "Bran");
        
        BooksDangerLevelRule bRule = new BooksDangerLevelRule();
        bRule.validate(inputMap);
    }
    
}
