/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriSubSidyRB.java
 *
 * Created on April 28, 2009, 4:13 PM
 */

package com.see.truetransact.ui.termloan.agrisubsidydetails;
import java.util.ListResourceBundle;
/**
 *
 * @author  Administrator
 */
public class AgriSubSidyRB extends ListResourceBundle {
    
    /** Creates a new instance of AgriSubLimitRB */
    public AgriSubSidyRB() {
    }
    
    protected Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        
        
        {"lblSolicitor_Name","Solicitor Name"},
        {"lblValuerName","Valuer Name"},
        {"lblDeveloper_Name","Developer Name"},
        {"lblProject_Name","Project Name"},
        {"lblReferalCode","Referal Code"},
        {"lblDisbursement_mode","Disbursement Mode"},
        {"lblSource_Code","Source Code"}
        
    };
}
