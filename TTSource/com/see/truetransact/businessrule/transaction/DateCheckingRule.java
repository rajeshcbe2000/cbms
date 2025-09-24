/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DateCheckingRule.java
 *
 * Created on February 27, 2004, 2:56 PM
 */

package com.see.truetransact.businessrule.transaction;

import java.util.HashMap;
import java.util.Date;

//import java.util.List;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.servicelocator.ServiceLocator;
//import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.exceptionconstants.transaction.TransactionConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.serverutil.ServerUtil;
/**
 *
 * @author  rahul, bala
 *
 * DateCheckingRule checks for the Following:
 * a) Given Date Should not be a future Date.
 * b) Given Date Should not nbe more than 6 months old.
 */
public class DateCheckingRule  extends ValidationRule{
    private SqlMap sqlMap = null;
    final long CONVERT = 2592000000L; //(1000L * 60 * 60 * 24) * 30;
    
    
    /** Creates a new instance of DateCheckingRule */
    public DateCheckingRule() throws Exception{
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    
    /**
     * To Validate the Condition depending on the Value Passed in the HashMap from the DAO...
     */
    public void validate(HashMap inputMap) throws Exception {
        System.out.println("datacheckingvalidation####"+inputMap);
        super._branchCode = CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.BRANCH_CODE));
        
        getTarget(inputMap);
    }
    
    /*
     * Method To get the Data from the Database to be Compared...
     * inputMap passed from the DAO...
     */
    private void getTarget(HashMap inputMap) throws Exception{
        Date instDt = (Date) inputMap.get("DATE");
//        System.out.println("#### DateCheckingRule getTarget() - inputMap : "+inputMap);
        // Checking with Account Opening Date.
        if (inputMap.containsKey("CREATEDATE")) {
            Date openingDate = (Date) inputMap.get("CREATEDATE");        
            if(instDt.before(openingDate)){
                throw new ValidationRuleException(TransactionConstants.DATE_LT_ACCT_OPEN);
            }
        }
        
        double months = 0;
//        long diff = (new Date()).getTime() - instDt.getTime();
        /* 
         * Convert the Dates into time and then get the difference of 
         * the Dates. 
         */
//        System.out.println("#### DateCheckingRule getTarget() - super._branchCode : "+super._branchCode);
        if (instDt != null) {
//            long diff = ServerUtil.getCurrentDate(super._branchCode).getTime() - instDt.getTime();
//            if (diff>=0){
                HashMap instMap = new HashMap();
                Date dueDate = (Date) inputMap.get("DATE");
                Date currDt = ServerUtil.getCurrentDate(super._branchCode);
                currDt.setDate(dueDate.getDate());
                currDt.setMonth(dueDate.getMonth());
                currDt.setYear(dueDate.getYear());
                instMap.put("INST_DATE",currDt);
                instMap.put("CURR_DATE",ServerUtil.getCurrentDate(super._branchCode));
                System.out.println("instMap : "+ instMap);
                java.util.List lst = sqlMap.executeQueryForList("getInstrumentMonthDiff",instMap);
                if(lst!=null && lst.size()>0){
                    instMap = (HashMap)lst.get(0);
                    System.out.println("instMap : "+ instMap);
                    months = CommonUtil.convertObjToDouble(instMap.get("MONTH_DIFF")).doubleValue();
                    System.out.println("Months: "+ months);
                }
                if(months>=0){
                /*
                 * divide the Difference by (millis per day * 30) to get the No of Months.
                 */
//                months = diff/CONVERT;    
                System.out.println("Months: "+ months);
                if (months > 3){
                    throw new ValidationRuleException(TransactionConstants.DATE_EXPIRED);
                }
            } else{              // if the date is in Future
                throw new ValidationRuleException(TransactionConstants.DATE_FUTURE);
            }            
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        HashMap inputMap = new HashMap();
        //inputMap.put("DATE", DateUtil.getDateMMDDYYYY("7/09/2004"));
        
        DateCheckingRule dRule = new DateCheckingRule();
        dRule.validate(inputMap);
    }
    
}
