/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * SalaryStructureMRB.java
 * 
 * Created on Wed Jun 02 10:45:38 GMT+05:30 2004
 */

package com.see.truetransact.ui.common;

import java.util.ListResourceBundle;

public class SalaryStructureMRB extends ListResourceBundle {
    public SalaryStructureMRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"cboSalaryStructureProdId", "Grade should be a proper value!!!"},
        {"lblSalaryStructureFromDateValue", "From date should be proper value!!!"},
        {"lblSalaryStructureToDateValue", "To date should be proper value !!!"},
        {"txtSalaryStructureStartingAmtValue","Starting scale amoumt should be proper value!!!"},
        {"txtSalaryStructureEndingAmtValue", "Ending scale amount should be a proper value!!!"},
        {"txtSalaryStructureAmtValue", "Increments should be a proper value!!!"},
        {"txtSalaryStructureIncYearValue", "No of increments should be a proper value!!!"},
        {"txtSalaryStructureTotNoIncValue", "Total no of stagnation should be a proper value!!!"},
        {"txtSalaryStructureStagnationAmtValue", "Stagnation increments amont should be a proper value!!!"},
        {"txtSalaryStructureNoOfStagnationValue", "No of stagnation should be a proper value!!!"},
        {"txtSalaryStructureStagnationOnceInValue", "Once in should be a proper value!!!"},
        {"cboSalaryStructureStagnationOnceIn", "Once in should be a proper value!!!"},
        
        {"cboCCAllowanceCityType", "City type should be a proper value!!!"},
        {"cboCCAllowance", "Designation should be a proper value!!!"},
        {"tdtCCAllowanceFromDateValue", "From date should be a proper value!!!"},
        {"tdtCCAllowanceToDateValue", "To date should be a proper value!!!"},
        {"txtCCAllowanceStartingAmtValue", "No of increments should be a proper value!!!"},
        {"txtCCAllowanceEndingAmtValue", "No of increments should be a proper value!!!"},
        {"txtFromAmount", "From Amount should be a proper value!!!"},
        {"txtToAmount", "To Amount should be a proper value!!!"},
        
        {"cboHRAllowanceCityType", "City type should be a proper value!!!"},
        {"cboHRAllowanceDesignation", "Designation should be a proper value!!!"},
        {"tdtHRAllowanceFromDateValue", "From date should be a proper value!!!"},
        {"tdtHRAllowanceToDateValue", "To date should be a proper value!!!"},
        {"txtHRAllowanceStartingAmtValue", "No of increments should be a proper value!!!"},
        {"txtHRAllowanceEndingAmtValue", "No of increments should be a proper value!!!"},
        
        {"cboDADesignationValue", "Designation should be a proper value!!!"},
        {"tdtDAFromDateValue", "From date should be a proper value!!!"},
        {"tdtDAToDateValue", "To date should be a proper value!!!"},
        {"txtDANoOfPointsPerSlabValue", "No of Points Per Slab should be a proper value!!!"},
        {"txtDAIndexValue", "Index should be a proper value!!!"},
        {"txtDAPercentagePerSlabValue", "Percentage Per Slab should be a proper value!!!"},
        {"txtTotalNoofSlabValue", "Total No of Slab should be a proper value!!!"},
        {"txtDATotalDAPercentageValue", "Total Percentage should be a proper value!!!"},

        {"cboTravellingAllowance", "Designation should be a proper value!!!"},
        {"tdtTAFromDateValue", "From date should be a proper value!!!"},
        {"tdtTAToDateValue", "To date should be a proper value!!!"},
//        {"chkFixedConveyance", "No of increments should be a proper value!!!"},
//        {"chkPetrolAllowance", "No of increments should be a proper value!!!"},
        {"txtBasicAmtUptoValue", "Basic amount upto should be a proper value!!!"},
        {"txtConveyancePerMonthValue", "Conveyance permonth should be a proper value!!!"},
        {"txtBasicAmtBeyondValue", "Basic amount beyond should be a proper value!!!"},
        {"txtConveyanceAmtValue", "Conveyance amount should be a proper value!!!"},
        {"txtNooflitresValue", "No of litres should be a proper value!!!"},
        {"txtPricePerlitreValue", "No of price per litre should be a proper value!!!"},
        {"txtTotalConveyanceAmtValue", "Total conveyance amount should be a proper value!!!"},

        {"cboMAidDesg", "Designation should be a proper value!!!"},
        {"tdtMAidFromDateValue", "From date should be a proper value!!!"},
        {"tdtMAidToDateValue", "To date should be a proper value!!!"},
        {"txtMAidAmtValue", "Medical allowace amount should be a proper value!!!"},
        
        {"cboOADesignationValue", "Designation should be a proper value!!!"},
        {"tdtOAFromDateValue", "From date should be a proper value!!!"},
        {"tdtOAToDateValue", "To date should be a proper value!!!"},
        {"cboOAllowanceTypeValue", "Allowance type should be proper value!!!"},
        {"cboOAParameterBasedOnValue", "Parameter based should be proper value!!!"},
        {"cboOASubParameterValue", "Subparameter should be proper value!!!"},
        {"chkOAFixedValue", "Fixed should be proper value!!!"},
        {"chkOAPercentageValue", "Percentage should be proper value!!!"},
        {"txtOAFixedAmtValue", "Fixed amount should be proper value!!!"},
        {"txtOAMaximumOfValue", "Maximum of amount should be proper value!!!"},
        {"txtOAPercentageValue", "Percentage should be proper value!!!"}
        
    };
}
