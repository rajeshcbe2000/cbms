/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ConfigurationMRB.java
 * 
 * Created on Fri Feb 11 14:16:32 IST 2005
 */

package com.see.truetransact.ui.sysadmin.config;

import java.util.ListResourceBundle;

public class ConfigurationMRB extends ListResourceBundle {
    public ConfigurationMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtNo", "No should not be empty!!!"},
        {"txtMaxLength", "MaxLength should not be empty!!!"},
        {"txtSplChar", "SplChar should not be empty!!!"},
        {"txtAttempts", "Attempts should not be empty!!!"},
        {"chkPwdNeverExpire", "PwdNeverExpire should be selected!!!"},
        {"chkAccLocked", "AccLocked should be selected!!!"},
        {"chkCantChangePwd", "CantChangePwd should be selected!!!"},
        {"chkFirstLogin", "FirstLogin should be selected!!!"},
        {"txtMinLength", "MinLength should not be empty!!!"},
        {"txtDays", "Days should not be empty!!!"},
        {"txtPwds", "Pwds should not be empty!!!"},
        {"txtUpperCase", "UpperCase should not be empty!!!"},
        {"txtMinorAge", "Enter the Age"},
      
        {"txtCashActHead",  "Select an Cash AccountHead"},
        {"txtIBRActHead", "Select an IBR AccountHead"},
        {"txtSIChargesHead", "Select an SIChargesHead"},
        {"txtRemitChargesHead", "Select an RemitChargesHead"},
        {"txtAcceptChargesHead", "Select an AcceptChargesHead"},
        {"txtExecChargesHead", "Select an ExecChargesHead"},
        {"txtFailChargesHead", "Select an FailChargesHead"},        
        {"txtSalarySuspense","Select an SalarySuspense"},
        {"txtAppSuspenseAcHd","Select an AppSuspenseAcHd"},
        {"txtSeniorCitizenAge", "SeniorCitizenAge should not be empty!!!"},
        {"txtRetirementAge","RetirementAge should not be empty!!!"},
        {"txtGahanPeriod","GahanPeriod should not be empty!!!"},
        {"txtPendingTxnAllowedDays","Pending TxnAllowedDays should not be empty!!!"},
        {"txtRTGS_GL", "RTGS_GL should not be empty!!!"},
        {"txtAmcAlertTime","AmcAlertTime should not be empty!!!"},
        {"tdtToDate","ToDate should not be empty!!!"},
        {"tdtFromDate", "FromDate should not be empty!!!"},
        {"txtPanDetails","PanDetails should not be empty!!!"}
   };
}
