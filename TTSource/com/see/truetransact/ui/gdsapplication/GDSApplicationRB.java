/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemittanceProductRB.java
 *
 * Created on Fri Jun 04 12:09:38 GMT+05:30 2004
 */

package com.see.truetransact.ui.gdsapplication;

import com.see.truetransact.ui.mdsapplication.*;
import java.util.ListResourceBundle;

public class GDSApplicationRB extends ListResourceBundle {
    public GDSApplicationRB(){
    }
    
    public Object[][] getContents() {
        return contents; 
    }
    
    static final String[][] contents = {

        {"lblSchemeName", "GDS Group Name"},
        {"lblDivisionNo", "Division No"},
        {"lblChittalNo", "Chittal No"},
        {"lblSubNo", "Sub No"},
        {"lblChitStartDt", "Chit start Date"},
        {"lblChitEndDt", "Chit End Date"},
        {"lblInstAmt", "Installment Amount"},
        {"lblApplnNo", "Application No"},
        {"lblApplnDate", "Application Date"},
        {"lblThalayal", "Thalayal"},
        {"lblCoChittal", "Co-Chittal"},
        {"lblMunnal", "Munnal"},
        {"lblMembershipNo", "Member No"},
        {"lblMembershipType", "Member Type"},
        {"lblMembershipName", "Member Name"},
        {"lblHouseStNo", "House/Street No"},
        {"lblCity", "City"},
        {"lblPin", "Pin"},
        {"lblArea", "Area"},
        {"lblState", "State"},
        {"lblStandingInstn", "Standing Instruction"},
        {"lblNominationDetails", "Nomination Details"},
        {"lblSalaryRecovery", "Salary Recovery"},
        {"lblRemarks", "Remarks"}
    };
    
}
