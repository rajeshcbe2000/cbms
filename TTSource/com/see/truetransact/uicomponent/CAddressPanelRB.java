/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * AddressPanelRB.java
 *
 * Created on September 2, 2003, 1:12 PM
 */

package com.see.truetransact.uicomponent;

/**
 *
 * @author  Pranav
 */
public class CAddressPanelRB extends java.util.ListResourceBundle {
    
    /** Creates a new instance of AddressPanelRB */
    public CAddressPanelRB() {
    }
    
    protected Object[][] getContents() {
        
        return contents;
    }

    static final String[][] contents = {
        {"lblStreet","Street"},
        {"lblArea","Area"},
        {"lblCountry","Country"},
        {"lblState","State"},
        {"lblCity","City"},
        {"lblPincode","Pincode"}
    };
}
