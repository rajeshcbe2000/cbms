/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * DeductionRB.java
 *
 * Created on Wed Jun 02 10:35:02 GMT+05:30 2004
 */

package com.see.truetransact.ui.common;

import java.util.ListResourceBundle;

public class IncrementsPromotionsRB extends ListResourceBundle {
    public IncrementsPromotionsRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"lblIncrementSLNO","SL No"},
        {"lblIncrementEmpId","Employee ID"},
        {"lblIncrementEmpName","Employee Name"},
        {"lblBasicSalary","Basic"},
        {"lblIncrementDesignation","Designation"},
        {"lblIncrementDate","Last increment date"},
        {"lblIncrementEffectiveDate","Effective date"},
        {"lblIncrementCreatedDate","Created date"},
        {"lblEmployeeGrade","Employees stage"},
        {"lblIncrementAmount","Increment Amount"},
        {"lblNewBasic","New Basic"},
        {"lblIncrementNo","Increment No"},
        {"lblPromotionSLNO","SL No"},
        {"lblPromotionEmployeeId","Employee ID"},
        {"lblPromotionEmployeeName","Employee name"},
        {"lblPromotionDesignation","Last Designation"},
        {"lblPromotionEmpBranch","Employee branch"},
        {"lblPromotionLastDesg","Last Grade"},
        {"lblPromotionEffectiveDate","Effective date"},
        {"lblPromotionCreatedDate","Created date"},
        {"lblPromotionBasicPay","Present basic pay"},
        {"lblPromotionStatus","Promotion Grade"}
    };
    
}
