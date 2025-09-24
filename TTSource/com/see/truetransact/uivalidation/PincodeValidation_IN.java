/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * PincodeValidation_IN.java
 *
 * Created on July 19, 2003, 12:22 PM
 */

package com.see.truetransact.uivalidation;

import java.awt.event.KeyEvent;
import com.see.truetransact.uicomponent.CTextField;

/** To check whether the Pincode entered for the Country INDIA is in valid format or
 * not. Basically, the pincode should be of 6 characters in length & should not
 * start with 0.
 * @author karthik
 */
public class PincodeValidation_IN extends UIComponentValidation{
    
   private String errorMessage;
    /** Creates a new instance of Pincode_IN */
    public PincodeValidation_IN() {
    }
    
    /** To validate whether the given text is of valid Pincode Format or not. If not set the
     * proper error message.
     * @return
     */    
    public boolean validate() {
        CTextField textField = (CTextField)getComponent();
        String pincode = textField.getText();
        if( 0 != pincode.length() )
        {
            if( pincode.charAt(0) == '0' || 6 != pincode.length() )
            {
                ValidationRB validationResourceBundle = new ValidationRB();
                setErrorMessage(validationResourceBundle.getString("pincode_warning"));
                textField.setText("");
                return false;
            }
        }
        return true;
    }
    
    /** To restrict the user to enter only numbers
     * @param keyEvent
     * @return
     */    
    public boolean validateEvent(java.awt.event.KeyEvent keyEvent){
        char c = keyEvent.getKeyChar();
         if (!(Character.isDigit(c) ||
         (c == KeyEvent.VK_BACK_SPACE) ||
         (c == KeyEvent.VK_DELETE))) {
             keyEvent.consume();
         }
        return true;
    }
    
    public String getErrorMessage() {
        return this.errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    
}
