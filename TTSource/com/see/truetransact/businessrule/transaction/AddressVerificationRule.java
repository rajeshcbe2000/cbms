/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AddressVerificationRule.java
 *
 * Created on March 31, 2005, 2:47 PM
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
public class AddressVerificationRule extends ValidationRule{
    private SqlMap sqlMap = null;
    final String YES = "Y";
    private String ErrorMessage = "";
    
    /** Creates a new instance of AddressVerificationRule */
    public AddressVerificationRule() throws Exception{
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap)locate.getDAOSqlMap();
    }
    
    /*
     * To Validate the Condition depending on the Value Passed in the HashMap from the DAO.
     */
    public void validate(HashMap inputMap) throws Exception {
        getTarget(inputMap);
    }
    
    private void getTarget(HashMap inputMap) throws Exception{
        List list = (List)sqlMap.executeQueryForList("getAddressVerificationData", inputMap);
        if(list!= null && list.size() > 0){
            HashMap addrMap = (HashMap)list.get(0);
            System.out.println("addrMap: " +addrMap);
            
            if(!CommonUtil.convertObjToStr(addrMap.get("ADDR_VERIFIED")).equalsIgnoreCase(YES)){
//                setErrorMessage(TransactionConstants.ADDR_NOT_VERIFIED);
//                final String[] options = {"Ok"};
//                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
//                final int option = javax.swing.JOptionPane.showOptionDialog(null, "Address of the Customer not Verified yet.",
//                com.see.truetransact.commonutil.CommonConstants.WARNINGTITLE,
//                javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE,
//                null, options, options[0]);
//                javax.swing.JOptionPane.showMessageDialog(null,"Address of the Customer not Verified yet.");
                throw new ValidationRuleException(TransactionConstants.ADDR_NOT_VERIFIED);   // Commented by Rajesh.
                
            }else{
                System.out.println("No Error in AddressVerificationRule");
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
            AddressVerificationRule aRule = new AddressVerificationRule();
            aRule.validate(inputMap);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Done");
    }
    
    /**
     * Getter for property ErrorMessage.
     * @return Value of property ErrorMessage.
     */
    public java.lang.String getErrorMessage() {
        return ErrorMessage;
    }
    
    /**
     * Setter for property ErrorMessage.
     * @param ErrorMessage New value of property ErrorMessage.
     */
    public void setErrorMessage(java.lang.String ErrorMessage) {
        this.ErrorMessage = ErrorMessage;
    }
    
}
