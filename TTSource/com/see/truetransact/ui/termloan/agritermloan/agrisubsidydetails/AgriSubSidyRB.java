/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriSubSidyRB.java
 *
 * Created on April 28, 2009, 4:13 PM
 */

package com.see.truetransact.ui.termloan.agritermloan.agrisubsidydetails;
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
        {"lblPropertyType","Property type"},
        {"lblValutionDt","Valuation Dt"},
        {"lblValutionAmt","Valuation Amt"},
        {"lblValuatedBy","Valuated By"},
        {"lblName","Name"},
        {"lblDesignation","Designation"},
        {"lblValutionRemarks","Valuation Remarks"},
        {"lblTypeOfSubSidy","Subsidy Type"},
        {"lblDepositProdId","Deposit Prodid"},
        {"lblDepositNo","Deposit No"},
        {"lblSubSidyDate","Subsidy Dt"},
        {"lblRecivedFrom","Received From"},
        
        {"lblSubSidyAmt","Subsidy Amt"},
        {"lblAmtAdjusted","Amt Adjusted"},
        {"lblAmtRefunded","Amt Refunded"},
        {"lblRefundDate","Refund Date"},
        {"lblOutStandingAmt","Outstanding Amt"},
        {"btnSubSidyNew","New"},
        {"btnSubsidySave","Save"},
        {"btnSubsidyDelete","Delete"},
        
        {"btnValutiontNew","New"},
        {"btnValutionSave","Save"},
        {"btnValutionDelete","Delete"},
        {"colum0","Slno"},
        {"colum1","Subsidy amt"},
        {"colum2","Start dt"},
        {"colum3","End dt"},
        {"colum4","Inspection type"},
        {"colum5","Inspection date"},
        {"colum6","Inspection details"},
        {"colum7","Inspect by"},
         {"colum8","Slno"}        
    };
}
