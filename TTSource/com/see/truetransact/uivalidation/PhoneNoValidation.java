/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * PhoneNoValidation.java
 */

package com.see.truetransact.uivalidation;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.commonutil.CommonUtil;
import java.text.DecimalFormat;
/**
 *
 * @author Admin
 */
public class PhoneNoValidation extends UIComponentValidation {
    private String errorMessage;
    private int _nums = 0;
    private int _decimals=0;
    
    public PhoneNoValidation() {
        
    }
    
    
        /** Checks whether the email entered is in proper format.If not set the
     * proper error message.
     * @return
     */    
    public boolean validate() {
        boolean isPhoneNo = true;
        CTextField textField = (CTextField) getComponent();
        ValidationRB validationResourceBundle = new ValidationRB();
        String phoneNo = CommonUtil.convertObjToStr(textField.getText());
        if (phoneNo.length() == 10 || phoneNo.length() == 11) {


            isPhoneNo = validateNumeric(textField);
            if (!isPhoneNo) {
                setErrorMessage(validationResourceBundle.getString("phoneno_warning"));
                isPhoneNo=false;
//                return false;

            }
        } else {
            setErrorMessage(validationResourceBundle.getString("phoneno_warning"));
             textField.setText("");
             isPhoneNo=false;
        }
        return isPhoneNo;
    }
    
    
      /**
     * @return
     */
    public boolean validateNumeric(CTextField textField) {
        boolean returnValue = true;
//        CTextField textField = (CTextField)getComponent();
        String content = textField.getText();
        if (content.length() != 0) {
            try {
                String textValue = textField.getText();
              
                    returnValue = validString(textValue);
                    if (!returnValue) {
                        textField.setText("");
                        returnValue = false;
                    }
//                } 
//                else
//                    textField.setText( getFormattedText(textValue) );
            } catch (NumberFormatException e) {
                ValidationRB validationResourceBundle = new ValidationRB();
                setErrorMessage(validationResourceBundle.getString("numeric_warning"));
                textField.setText("");
//                validationResourceBundle = null;
                returnValue = false;
            }
        }
        return returnValue;
    }
    
      
    private boolean validString(String str) {
        String strFind = "";
        boolean result = true;
        char arr[] = null;
        char prevChar=0, currChar;
        int ascii;
        for (int i=0, j=str.length();i<j;i++) {
            currChar = str.charAt(i);
            ascii = (int) currChar;
            if (!(ascii >= 48 && ascii <= 57)) {
                ValidationRB validationResourceBundle = new ValidationRB();
                setErrorMessage(validationResourceBundle.getString("numeric_warning"));
//                validationResourceBundle = null;
                result = false;
                break;
            }
        }
        return result;
    }
    
//    public String getFormattedText(String textValue){
//        if( 0 != textValue.length() ) {
//            char lastChar = textValue.charAt(textValue.length()-1);
//            
//            if (!Character.isDigit(lastChar)) {
//                textValue = textValue.substring(0, textValue.length()-1);
//                textValue = String.valueOf
//                    (
//                        Double.parseDouble(textValue) * Double.parseDouble
//                        (CurrencyValidation.SHORTCUT_NUMS.get(String.valueOf(lastChar)).toString())
//                     );
//            }
//            
//            DecimalFormat numberFormat = new DecimalFormat();
//            StringBuffer strB = new StringBuffer("##############");
//            if (_decimals > 0) {
//                strB.append(".");
//                
//                for (int i=0; i < _decimals; i++) {
//                    strB.append("#");
//                }
//            }
//            numberFormat.applyPattern(strB.toString());
//            
//            return numberFormat.format(Double.parseDouble(textValue) );
//        }
//        return textValue;
//    }
    
     public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
}
