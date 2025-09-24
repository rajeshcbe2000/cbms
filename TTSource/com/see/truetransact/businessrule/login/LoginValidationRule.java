/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LoginValidationRule.java
 *
 * Created on March 11, 2005, 12:05 PM
 */

package com.see.truetransact.businessrule.login;

import java.util.HashMap;
import java.util.List;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.exceptionconstants.login.LoginConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverutil.ServerUtil;
import java.util.Date;


/**
 *
 * @author  JK
 * LoginValidationRule Checks for the following:
 * a) If the User Id and Password exists
 * b) If yes, -- Is it Blocked user
 * c) -- Is it Temporarily suspended user
 * d) -- Is it Expired Account
 */
public class LoginValidationRule extends ValidationRule{
    private SqlMap sqlMap = null;
    
    /** Creates a new instance of AccountMaintenanceRule */
    public LoginValidationRule() throws Exception {
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    
    /* 
     * To Validate the Condition depending on the Value Passed in the HashMap from the DAO.
     */
    public void validate(HashMap inputMap) throws Exception {
        super._branchCode = CommonUtil.convertObjToStr(inputMap.get("BRANCHCODE"));
        getTarget(inputMap);
    }
    
    /*
     * Method To get the Data from the Database to be Compared...
     * inputMap passed from the DAO...
     */
    private void getTarget(HashMap inputMap) throws Exception{
        boolean check;
        inputMap.put("CURR_DATE", ServerUtil.getCurrentDate(_branchCode));
        /* The Mapped Statement "loginValidation.getUserIdDetails" is
         * in LoginMap; and is used to Know if the
         * UserId, Pwd and BranchCode are Vaild or not.
         */
        List getUserDetailsList = (List) sqlMap.executeQueryForList("loginValidation.getUserIdDetails", inputMap);
        
        List getUserTerminalList = (List) sqlMap.executeQueryForList("loginValidation.getUserTerminalDetails", inputMap);
        if( getUserTerminalList != null && getUserTerminalList.size() > 0 ){
            HashMap getUserTerminalHash = (HashMap) getUserTerminalList.get(0);
            getUserTerminalHash.putAll(inputMap);
            List getCountOfTerminalList = (List) sqlMap.executeQueryForList("loginValidation.getCountOfTerminal", getUserTerminalHash);
            HashMap getCountOfTerminalHash = (HashMap) getCountOfTerminalList.get(0);
            int countTerminalUserData = CommonUtil.convertObjToInt(getCountOfTerminalHash.get("COUNT"));
            if(countTerminalUserData==0){
                System.out.println("Not a valid Terminal User ....");
                throw new ValidationRuleException(LoginConstants.INVALID_TERMINAL_USER);
            }
        }
        //--- If the UserId is a valid one, check for the other parameters 
        if( getUserDetailsList != null && getUserDetailsList.size() > 0 ){
            HashMap getUserDetailsHash = (HashMap) getUserDetailsList.get(0);
            System.out.println("getUserDetailsHash:" + getUserDetailsHash);
            //--- If the user exists and the HashMap vlues are not null, continue  else show alert messsage
            if(getUserDetailsHash!= null){
                /* --- Checks for DAYEND Completed or not
                 *     If DAYEND Completed the system should not allow to 
                 *     login who are not having access to DAYBEGIN
                 */
                HashMap dataHash = new HashMap();
                dataHash.put("GROUP_ID", CommonUtil.convertObjToStr(getUserDetailsHash.get("USER_GROUP")));
                dataHash.put("SCREEN_ID", "SCR01041");
                List lst = ServerUtil.executeQuery("getCountOfScreen", dataHash);
                int cnt = CommonUtil.convertObjToInt(((HashMap)lst.get(0)).get("COUNT"));
                if (cnt==0) {
                    dataHash = new HashMap();
                    dataHash.put("BRANCH_CODE", CommonUtil.convertObjToStr(getUserDetailsHash.get("BRANCH_CODE")));
                    lst = ServerUtil.executeQuery("chkTransactionAllowed", dataHash);
                    System.out.println("#### lst : "+lst);
                    if(lst.size()>0) {
                        String strStatus = "";
                        strStatus = CommonUtil.convertObjToStr(lst.get(0));
                        if (strStatus.equalsIgnoreCase("COMPLETED")) {
                            System.out.println("Continue After DayEnd ....");
                            throw new ValidationRuleException(LoginConstants.CONTINUE_AFTER_DAY_BEGIN);
                        }
                    }
                }
                dataHash = null;
                lst = null;
                //--- If the Status is Deleted show alert, else continue
                if(!CommonUtil.convertObjToStr(getUserDetailsHash.get(CommonConstants.STATUS)).equals(CommonConstants.STATUS_DELETED)){
                    //--- If it is not Expired user Continue, else alert the user
                    if(isTodayBetween((Date)getUserDetailsHash.get("ACTIVATION_DATE"),(Date)getUserDetailsHash.get("EXPIRY_DATE")) == true){
                        //--- If the user is authorized, then continue, else alert the user
                        if(CommonUtil.convertObjToStr(getUserDetailsHash.get("AUTHORIZED_STATUS")).equals("AUTHORIZED")){
                            //--- If the useris not suspended user, then continue, else alert the user
                            
                            if((CommonUtil.convertObjToStr(getUserDetailsHash.get("SUSPEND_USER")).equals("Y")) || 
                                (getUserDetailsHash.get("SUSPEND_FROM_DT")!= null && 
                                getUserDetailsHash.get("SUSPEND_TO_DT") != null && 
                                isTodayBetweenOrB4(
                                    (Date)getUserDetailsHash.get("SUSPEND_FROM_DT"), 
                                    (Date)getUserDetailsHash.get("SUSPEND_TO_DT")) == true)) {
                                System.out.println("Suspended User ....");
                                throw new ValidationRuleException(LoginConstants.SUSPENDED_USER);
                            } else {
                                System.out.println("VALID USER:");
                            }

                        } else {
                            System.out.println("Not an Authorized User ....");
                            throw new ValidationRuleException(LoginConstants.UNAUTHORIZED_USER);
                        }
                        
                    } else {
                        System.out.println("Expired User ....");
                        throw new ValidationRuleException(LoginConstants.EXPIRED_USER);
                    }
                    
                } else {
                    System.out.println("Deleted User Status ...");
                    throw new ValidationRuleException(LoginConstants.BLOCKED_USER);
                }
            } else {
                System.out.println("UserId, Password, BranchCode does not match...");
                throw new ValidationRuleException(LoginConstants.INVALID_USER);
            }
        } else {
            System.out.println("UserId, Password, BranchCode does not match...");
            throw new ValidationRuleException(LoginConstants.INVALID_USER);
        }
        
    }
    
    /** returns True or False depending
     *  on whether today is between the From date
     *  and To date or before the FromDate
     */
    private boolean isTodayBetweenOrB4(Date dtFrom, Date dtTo){
        boolean retDate = false;
        Date TodaysDt = ServerUtil.getCurrentDate(super._branchCode);
        //--- If Today is Between the "From date" and "To Date" or B4 the "From Date", then return true.
        if((TodaysDt.after(dtFrom) && TodaysDt.before((dtTo))) || (TodaysDt.before((dtFrom))) || TodaysDt.before((dtTo))) { 
            retDate = true;
        } 
        return retDate;
    }
    
    /** returns True or False depending
     *  on whether today is between the From date
     *  and To date
     */
    private boolean isTodayBetween(Date dtFrom, Date dtTo){
        boolean retDate = false;
        Date TodaysDt = ServerUtil.getCurrentDate(super._branchCode);
        //--- If Today is Between the "From date" and "To Date" , then return true.
        if(((TodaysDt.after(dtFrom) || TodaysDt.compareTo(dtFrom) == 0) && (TodaysDt.before((dtTo))) || TodaysDt.compareTo(dtTo) == 0)) { 
            retDate = true;
        } 
        return retDate;
    }    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        HashMap inputMap = new HashMap();
        inputMap.put("USERID", "admin" );
        inputMap.put("PWD", "0zQAkXnI4zvhc8CvCC4BaA==" );
        inputMap.put("BRANCHCODE", "Bran" );
        LoginValidationRule lnRule = new LoginValidationRule();
        lnRule.validate(inputMap);
        System.out.println("valid");
    }
    
}
