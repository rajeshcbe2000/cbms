/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * PasswordRules.java
 *
 * Created on February 15, 2005, 11:25 AM
 */

package com.see.truetransact.ui.common.passwordrules;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.transferobject.sysadmin.config.ConfigPasswordTO;
import java.util.List;
/**
 *
 * @author  152715
 */
public class PasswordRules {
    private ConfigPasswordTO objConfigPwdTO = null;
    // variables declaration
    private int minLength;
    private int maxLength;
    private int specialChar;
    private int number;
    private int pwdHistoryCount;
    private int upperCase;
    private int pwdLength;
    private String password = "";
    private StringBuffer strBuf = null;
    private StringBuffer errorMsg = null;
    //    private int lastPwds;
    private static int countDigit;
    private static int countUpperCase;
    private static int countSplChar;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final PasswordRulesRB resourceBundle = new PasswordRulesRB();
    
    /** Creates a new instance of PasswordRules */
    public PasswordRules() {
        getParameters();
        initializeData();
    }
    /**
     * To get the values from Parameters Table
     */
    private void getParameters() {
        try {
            List list = (List) ClientUtil.executeQuery("getSelectConfigPasswordTO",null);
            if(list != null && list.size()>0) {
                objConfigPwdTO = (ConfigPasswordTO)list.get(0);
                
            }
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public ConfigPasswordTO getPasswordConfig() {
        getParameters();
        return objConfigPwdTO;
    }
    
    private void initializeData() {
        if (objConfigPwdTO != null) {
            this.maxLength = convertStrToInt(CommonUtil.convertObjToStr(objConfigPwdTO.getMaxLength()));
            this.minLength = convertStrToInt(CommonUtil.convertObjToStr(objConfigPwdTO.getMinLength()));
            this.number = convertStrToInt(CommonUtil.convertObjToStr(objConfigPwdTO.getNumberChars()));
            this.specialChar = convertStrToInt(CommonUtil.convertObjToStr(objConfigPwdTO.getSpecialChars()));
            this.upperCase = convertStrToInt(CommonUtil.convertObjToStr(objConfigPwdTO.getUppercaseChars()));
            this.pwdHistoryCount = convertStrToInt(CommonUtil.convertObjToStr(objConfigPwdTO.getShouldNotLastpwd()));
        }
    }
    /**
     * To concatenate error Msg
     */
    private void addErrorMsg() {
        if (errorMsg == null)
            errorMsg = new StringBuffer();
        errorMsg.append(CommonUtil.convertObjToStr(strBuf)).append("\n");
        
    }
    /**
     * validates the password
     */
    public boolean validatePassword(String pwd) {
        boolean valid = false;
        this.password = pwd;
        findPasswordLength();
        if (this.pwdLength >0) {
            if (passwordRange()) {
                getMsgForRange();
                addErrorMsg();
                nullStrBuf();
                valid = true;
            }
            countDigitUpperCaseSplChar();
            if (containsNumber()) {
                getMsgForNumber();
                addErrorMsg();
                nullStrBuf();
                valid = true;
            }
            if (containsSplChar()) {
                getMsgForSplChar();
                addErrorMsg();
                nullStrBuf();
                valid = true;
            }
            if (containsUpperCase()) {
                getMsgForUpCase();
                addErrorMsg();
                nullStrBuf();
                valid = true;
            }
            
        
        }
        
        
        if (errorMsg != null && errorMsg.length()>0) {
            displayAlert(CommonUtil.convertObjToStr(errorMsg));
            
        }
        errorMsg = null;
        
        return valid;
    }
    /**
     * To display Alert for warnings
     */
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    /**
     * return String from resourceBundle
     */
    private String getStringRB(String rbKey) {
        return CommonUtil.convertObjToStr(resourceBundle.getString(rbKey));
    }
    // Alert Msg for Special Character
    public void getMsgForSplChar() {
        appendStr(getStringRB("Contain"));
        if(objConfigPwdTO != null)
            appendStr(CommonUtil.convertObjToStr(objConfigPwdTO.getSpecialChars()));
        appendStr(getStringRB("SplChar"));
    }
    // Alert Msg for Numeric values
    public void getMsgForNumber() {
        appendStr(getStringRB("Contain"));
        if(objConfigPwdTO != null)
            appendStr(CommonUtil.convertObjToStr(objConfigPwdTO.getNumberChars()));
        appendStr(getStringRB("Numeric"));
    }
    // Alert Msg for Upper Case
    public void getMsgForUpCase() {
        appendStr(getStringRB("Contain"));
        if(objConfigPwdTO != null)
            appendStr(CommonUtil.convertObjToStr(objConfigPwdTO.getUppercaseChars()));
        appendStr(getStringRB("UpperCase"));
    }
    // get Msg if pwd doesnt lies between the range
    public void getMsgForRange() {
        appendStr(getStringRB("Length"));
        if(objConfigPwdTO != null)
            appendStr(CommonUtil.convertObjToStr(objConfigPwdTO.getMinLength()));
        appendStr(getStringRB("TO"));
        if(objConfigPwdTO != null)
            appendStr(CommonUtil.convertObjToStr(objConfigPwdTO.getMaxLength()));
    }
    // appends warning messages
    private void appendStr(String str) {
        if(strBuf == null)
            strBuf = new StringBuffer();
        strBuf.append(str);
        
    }
    private void nullStrBuf() {
        strBuf = null;
    }
    // finds the length of the password
    public void findPasswordLength() {
        this.pwdLength = ((String)CommonUtil.convertObjToStr(this.password)).length();
    }
    /**
     * Parses String as Int
     */
    public int convertStrToInt(String str) {
        int temp = 0;
        if (str.length()>0 && !str.equals("") && str != null) {
            temp = Integer.parseInt(str);
        }
        return temp;
    }
    // checks whether pwd between the range
    public boolean passwordRange() {
        boolean range = true;
        if (this.minLength == 0 || this.maxLength == 0 || 
                this.pwdLength >= this.minLength && this.pwdLength <= this.maxLength) {
            range = false;
        }
        return range;
    }
    // checks whether pwd contains numeric value
    public boolean containsNumber() {
        boolean flag = true;
        if (this.number <= this.countDigit || this.number == 0) {
            flag = false;
        }
        return flag;
    }
    // checks whether pwd contains special character
    public boolean containsSplChar() {
        boolean flag = true;
        if (this.specialChar <= this.countSplChar || this.specialChar == 0) {
            flag = false;
        }
        return flag;
    }
    // checks whether pwd contains upper case
    public boolean containsUpperCase() {
        boolean flag = true;
        if (this.upperCase <= this.countUpperCase || this.upperCase  == 0) {
            flag = false;
        }
        return flag;
    }
    /**
     * To Count No of Numeric, UpperCase and Special Character in the String password
     */
    public void countDigitUpperCaseSplChar() {
        if (this.pwdLength > 0) {
            for (int i=0;i<this.pwdLength;i++) {
                char val = this.password.charAt(i);
                int ascii = (int)val;
                
                if(Character.isDigit(val)) {
                    countDigit++;
                } else if (Character.isUpperCase(val)) {
                    countUpperCase++;
                } else if ((ascii >= 33 && ascii <= 47)
                || (ascii >= 58 && ascii <= 64)
                || (ascii >= 91 && ascii <= 96)
                || (ascii >= 123 && ascii <= 127)) {
                    countSplChar++;
                }
            }
        }
    }
    
    /**
     * Getter for property pwdHistoryCount.
     * @return Value of property pwdHistoryCount.
     */
    public int getPwdHistoryCount() {
        return pwdHistoryCount;
    }
    
    /**
     * Setter for property pwdHistoryCount.
     * @param pwdHistoryCount New value of property pwdHistoryCount.
     */
    public void setPwdHistoryCount(int pwdHistoryCount) {
        this.pwdHistoryCount = pwdHistoryCount;
    }
    
    //    public static void main(String arg[]) {
    //        System.out.println("Password Rules Invoked...");
    //        PasswordRules pwd = new PasswordRules();
    //        pwd.validatePassword("phr1A");
    //    }
}
