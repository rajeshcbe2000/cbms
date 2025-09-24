/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * EmailValidation.java
 *
 * Created on July 18, 2003, 3:29 PM
 */

package com.see.truetransact.uivalidation;

import com.see.truetransact.uicomponent.CTextField;

/** To Check whether the email entered is in proper format. This will not
 * check whether the given email exists or not
 * @author karthik
 */
public class EmailValidation extends UIComponentValidation{
    
   private String errorMessage;
  
   /** Creates a new instance of EmailValidation */
    public EmailValidation() {
    }
    
    /** Checks whether the email entered is in proper format.If not set the
     * proper error message.
     * @return
     */    
    public boolean validate() {
        CTextField textField = (CTextField)getComponent();
        String mailID = textField.getText();
        if (mailID.length() != 0) {
            if( mailID.indexOf("@") < 0 ||
            mailID.indexOf(".") < 0 ||
            mailID.length() <= 5  ||
            mailID.indexOf("@") == 0 ||
            mailID.indexOf(".") == 0 ||
            (mailID.indexOf("@") !=  mailID.lastIndexOf("@")) ||
            (mailID.indexOf("@")) == (mailID.length()-1) ||
            (mailID.indexOf(".")) == (mailID.length()-1) ||
            (mailID.indexOf("@")+1) == (mailID.lastIndexOf(".")) ||
            (mailID.indexOf(".")+1) == (mailID.indexOf("@")) ||
            !validEmail(mailID.substring(mailID.lastIndexOf("."))) || /* check in the end is it number*/
            !validEmail(mailID.replaceAll("@", "").replaceAll(".", "")) /* check for full email id is number. */
            ) {
                ValidationRB validationResourceBundle = new ValidationRB();
                setErrorMessage(validationResourceBundle.getString("email_warning"));
                return false;
            }
        }
        return true;
    }
    
    private boolean validEmail(String mailID) {
        boolean result;
        try {
            // if all/end is number it is not a valid email id
            Double.valueOf(mailID);
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
    
}
