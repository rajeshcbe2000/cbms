/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * StandingInstructionMRB.java
 */
package com.see.truetransact.ui.supporting.standinginstruction;
import java.util.ListResourceBundle;
public class StandingInstructionMRB extends ListResourceBundle {
    public StandingInstructionMRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"dtdEndDtSI", "Enter the End Date"},
        {"dtdSuspendDt", "Enter the End Date"},
        {"txtAmountCSI", "Enter the Credit Amount!!!"},
        {"txtParticularsCSI", "ParticularsCSI should not be empty!!!"},
        {"cboMoRSI", "Select the desired mode of transmission "},
        {"txtGraceDaysSI", "Enter the no of Grace days up to which the SI is to be tried if it fails on appointed date"},
        {"rdoRettCommSI_Yes", "Whether Remittance Commission Is to be collected"},
        {"txtCommChargesSI", "Enter the SI Charges Amount"},
        {"cboSIType", "Select the relevant Type of Standing Instruction!!!"},
        {"rdoCommSI_Yes", "Whether SI Commission Is to be collected"},
        {"txtParticularsDSI", "ParticularsDSI should not be empty!!!"},
        {"txtAmountDSI", "Enter the Debit Amount"},
        {"dtdStartDtSI", "Enter the Start Date"},
        {"txtSINo", "SINo should not be empty!!!"},
        {"txtAccNoCSI", "Enter the Acc Number"},
        {"cboProdIDCSI", "Select the relevant Product Id"},
        {"txtRettCommChargesSI", "Enter the Remittance Charges Amount"},
        {"cboProdIDDSI", "Select the relevant Product Id"},
        {"txtAccNoDSI", "Enter the Acc Number"},
        {"txtMinBalSI", "Enter the Minimum Balance Amount"},
        {"txtBeneficiarySI", "Enter the Beneficiary Name"},
        {"txtMultiplieSI", "Enter the Multiples of Amount"},
        {"cboWeekDay", "Select the WeekDay on Which the StandingInstruction is to be Executed"},
        {"cboWeek", "Select the Specific Week in a month on which the StandingInstruction is to be Exececuted"},
        {"cboSpecificDate", "Select the Specific Date in a month on which the StandingInstruction is to be Executed"},
        {"rdoHolidayExecution_Yes", "Whether StandingInstruction is to be Executed on Holiday"},
        {"rdoSIAutoPosting_Yes","Whether StandingInstruction is to be Automatically Posted"},
        {"txtForwardCount", "Enter the Number of times StandingInstruction Scheduled can be forwarded"},
        {"cboExecConfig", "Select the execution to be configuration for the StandingInstruction"},
        {"txtAcceptanceCharges", "Enter the Acceptance Charges"},
        {"txtFailureCharges", "Enter the Failure Charges"},
        {"txtExecutionCharges","Enter the Execution Charges"},
        {"cboFrequencySI", "Enter the Desired Mode of Frequency"},
        {"txtMinBalSI", "Enter the Minimum Balance Amount"},
        {"cboProductTypeCSI", "Product Type Should be Selected!!!"},
        {"cboProductTypeDSI", "Product Type Should be Selected!!!"},
        {"cboExecutionDay", "Execution Day Should be Selected!!!"},
        {"txtAccHeadValueCSI", "Enter the AccountHead"},
        {"txtAccHeadValueDSI", "Enter the AccountHead"},
        {"NoSiCreditMsg", "Credit SI Is Empty"},
        {"NoSiDebitMsg", "Debit SI Is Empty"},
        
    };
    
}
