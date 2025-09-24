/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * DefaultValidation.java
 *
 * Created on July 18, 2003, 3:29 PM
 */

package com.see.truetransact.uivalidation;

import com.see.truetransact.uicomponent.CTextField;
/** To Check whether the email entered is in proper format. This will not
 * check whether the given email exists or not
 * @author karthik, Bala
 */
public class DefaultValidation extends UIComponentValidation{
    private boolean allowNum = false;
    private boolean allowAll = false;
    private String errorMessage;
    
    /** Creates a new instance of EmailValidation */
    public DefaultValidation() {
    }
    
    /** Checks whether the entered string is valid or not.If not set the
     * proper error message.
     * @return
     */
    public boolean validate() {
        String strText = new String();
        CTextField textField = null;
        if(this.getComponent() instanceof CTextField){
            textField = (CTextField)getComponent();
            allowNum = textField.isAllowNumber();
            allowAll = textField.isAllowAll();
            strText = textField.getText();
        }
        
        if (strText.length() > 0 && !allowAll && textField != null && textField.isEditable()) {
            String invalid = validString(strText);
            if( strText.indexOf(".") == 0 || invalid.length() > 0) {
                ValidationRB validationResourceBundle = new ValidationRB();
                setErrorMessage(validationResourceBundle.getString("string_warning") + " Check for " + invalid);
                textField.setText("");
                return false;
            }
        }
        return true;
    }
    
    private String validString(String str) {
        String strFind = "";
        String result = "";
        char arr[] = null;
        char prevChar=0, currChar;
        int ascii;
        /* Checking for spaces
         */
        if (str.indexOf("  ") >= 0) {
            result = "More Spaces";
        } else if (!allowNum && !alphNumeric(str)) {
            result = " all are numbers.";
        } else {
            for (int i=0, j=str.length();i<j;i++) {
                currChar = str.charAt(i);
                ascii = (int) currChar;
                //System.out.println("ascii---->" + ascii + "  " + (char) ascii);
                /* Special Character Range
                 */
                if ((ascii >= 32 && ascii <= 47)
                || (ascii >= 58 && ascii <= 64)
                || (ascii >= 91 && ascii <= 96)
                || (ascii >= 123 && ascii <= 127)) {
                    /* checking for continues differe special chars
                     */
                    if (prevChar != 0 && prevChar != ' ' && currChar != ' ') {
                        result = String.valueOf(prevChar) + String.valueOf(currChar);
                        //System.out.println("invalid data1");
                        break;
                    } else {
                        arr = new char[]{currChar, currChar};
                        strFind = strFind.valueOf(arr);
                        prevChar = currChar;
                        /* Checking for same special chars
                         */
                        if (str.indexOf(strFind) >= 0) {
                            result = strFind;
                            //System.out.println("invalid data2");
                            break;
                        }
                    }
                } else { // Resetting previous character
                    prevChar = 0;
                }
            }
        }
        return result;
    }
    
    private boolean alphNumeric(String inputTxt) {
        boolean result;
        try {
            // if all/end is number it is not a valid email id
            Double.valueOf(inputTxt);
            result = false;
        } catch (NumberFormatException ex) {
            result = true;
        }
        return result;
    }
    
    public String getErrorMessage() {
        return this.errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public static void main(String strarr[]) {
        System.out.println("Done");
    }
}
