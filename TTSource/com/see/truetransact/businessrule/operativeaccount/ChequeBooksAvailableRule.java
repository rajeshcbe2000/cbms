/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ChequeBooksAvailableRule.java
 *
 * Created on August 30, 2004, 2:07 PM
 */

package com.see.truetransact.businessrule.operativeaccount;

import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.exceptionconstants.operativeaccount.OperativeAccountConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;

import java.util.HashMap;
import java.util.List;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonUtil;
/**
 *
 * @author  rahul
 * ChequeBooksAvailableRule Checks for the following:
 * a) If the No. of Cheque books Ordered are Available or not...
 */
public class ChequeBooksAvailableRule extends ValidationRule{
    private SqlMap sqlMap = null;
    
    /** Creates a new instance of ChequeBooksAvailableRule */
    public ChequeBooksAvailableRule() throws Exception{
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    
    /** To Validate the Condition depending on the Value Passed in the HashMap from the DAO.*/
    public void validate(HashMap inputMap) throws Exception {
        getTarget(inputMap);
    }
    
    private void getTarget(HashMap inputMap) throws Exception{
        boolean check;
        
        /* 
         * The Mapped Statement "getChequeBookAvailablelRule" is in ChequeBookMap; and is
         * used to Know if the No. of Cheque Books Ordered are Available or not...
         */
        List booksAvailableLevel = (List) sqlMap.executeQueryForList("getChequeBookAvailablelRule", inputMap);
        
        if(booksAvailableLevel!=null){
            if(booksAvailableLevel.size() > 0){
                System.out.println("booksAvailableLevel size: "+ booksAvailableLevel.size());
                throw new ValidationRuleException(OperativeAccountConstants.BOOKS_AVAIL_LEVEL);
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
        inputMap.put("ITEM_SUB_TYPE", "CURRENT_ACCT_CHEQUES");
        inputMap.put("LEAVES_PER_BOOK", "100");
        inputMap.put("BRANCH_ID", "Bran");
        inputMap.put("CHEQUE_BOOKS", CommonUtil.convertObjToInt("1"));
        
        ChequeBooksAvailableRule cRule = new ChequeBooksAvailableRule();
        cRule.validate(inputMap);
    }
    
}
