/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ValidationRB.java
 *
 * Created on July 18, 2003, 12:47 PM
 */

package com.see.truetransact.uivalidation;

import java.util.ListResourceBundle;
/**
 *
 * @author  karthik
 */
public class ValidationRB extends ListResourceBundle{
    
    /**Warning : Integer_seperator "." should be set with an escape charactor as "\\." 
     * Works for patterns 1,000,000.22 & 1.000.000,22 
     * For display_optionpane give the value as "1" to display & other values for not to
     * display
     */
    static final String[][] contents = {
              {"language_code", "en"},
              {"country_code", "IN"},
              {"integer_seperator", ","},
              {"decimal_seperator", "."},
              {"display_optionpane", "1"},
              {"email_warning", "Enter a valid Email!"},
              {"pincode_warning", "Enter a valid 6 digit Pincode!"},
              {"currency_warning", "Enter a valid Currency!"},
              {"string_warning", "Enter a valid Data."},
              {"numeric_warning", "Enter a valid Number!"},
              {"phoneno_warning", "Enter a valid Phone Number!"}
            
      };
    /** Creates a new instance of ValidationResourceBundle */
      public ValidationRB() {
    }
    
    /** To get all the contents
     * @return To return contents
     */    
    public Object[][] getContents() {
        return contents;
    }
    
}
