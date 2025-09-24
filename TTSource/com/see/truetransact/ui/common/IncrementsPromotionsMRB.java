/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * DeductionMRB.java
 * 
 * Created on Wed Jun 02 10:45:38 GMT+05:30 2004
 */

package com.see.truetransact.ui.common;

import java.util.ListResourceBundle;

public class IncrementsPromotionsMRB extends ListResourceBundle {
    public IncrementsPromotionsMRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {    
        {"tdtIncrementEffectiveDateValue","Effective date should not be empty!!!"},
        {"tdtIncrementCreatedDateValue","Created date should not be empty!!!"},
        {"txtEmployeeGrade","Increment type should not be empty!!!"},
        {"txtIncrementAmount","Increment Amount!!!"},
        {"txtNewBasic","New Basic after calculation"},
        {"txtIncrementNo","Increment No of the Employee!!"},
        {"tdtIncrementDate","last increment date!!!"},
        {"txtPromotionEmployeeId","Employee id should not be empty!!!"},
        {"txtPromotionEmployeeName","Employee name should not be null!!"},
        {"tdtPromotionEffectiveDateValue","Effective date should not be empty!!!"},
        {"tdtPromotionCreatedDateValue","Created date should not be empty!!!"},
        {"txtPromotionBasicPayValue","Basicpay should not be empty!!!"},
        {"cboPromotionDesigValue","Promotion designation should not be empty!!!"}
    };
}
