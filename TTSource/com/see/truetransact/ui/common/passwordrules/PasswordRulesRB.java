/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * PasswordRulesRB.java
 *
 * Created on February 16, 2005, 10:47 AM
 */

package com.see.truetransact.ui.common.passwordrules;

import java.util.ListResourceBundle;
/**
 *
 * @author  152715
 */
public class PasswordRulesRB extends ListResourceBundle {
    
    /** Creates a new instance of PasswordRulesRB */
    public PasswordRulesRB() {
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"Contain", "Password Should Contain Atleast "},
        {"SplChar", " Special Characters "},
        {"UpperCase", " Upper Case "},
        {"Numeric", " Numeric Values "},
        {"Length", "Length Of The Password Should Be Between "},
        {"TO", " To "}
        
    };
    
}
