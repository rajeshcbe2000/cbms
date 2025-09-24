/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ConfigurationRB.java
 *
 * Created on Fri Feb 11 16:02:57 IST 2005
 */
package com.see.truetransact.ui.sysadmin.config;

import java.util.ListResourceBundle;

public class ConfigurationRB extends ListResourceBundle {

    public ConfigurationRB() {
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblLastPwd", "Should Not Be Any Of The Last"},
        {"btnClose", ""},
        {"lblPasswordExpire", "Password Never Expire"},
        {"chkCantChangePwd", ""},
        {"lblAtleast", "Atleast"},
        {"lblMsg", ""},
        {"lblMinLength", "Minimum Length"},
        {"lblPwds", "Passwords"},
        {"lblSpace2", " Status :"},
        {"btnSave", ""},
        {"lblFirstLogin", "Change Password On First Login"},
        {"chkAccLocked", ""},
        {"lblMaxLength", "Maximum Length"},
        {"lblStatus", "                      "},
        {"lblAttempts", "Attempts"},
        {"lblSpace1", "     "},
        {"lblUserCantChangePwd", "User Cannot Change Password"},
        {"lblPwdExpiry", "Password Expiry"},
        {"lblUpCase", "Upper Case"},
        {"lblSplChar1", "Special Character"},
        {"lblNo", "Number"},
        {"lblDays", "Days"},
        {"lblLowAtleast", "Atleast"},
        {"chkPwdNeverExpire", ""},
        {"btnCancel", ""},
        {"lblAtleastUp", "Atleast"},
        {"chkFirstLogin", ""},
        {"lblAfter", "After"},
        {"lblUserAccLocked", "User Account Locked"},
        {"btnEdit", ""},
        {"btnPrint", ""},
        {"PwdMinLength", "Password cannot be less than 6 characters."},
        {"PwdChar", "Password should contain atleast one alphabet."},
        {"PwdNumChar", "Password should contain atleast one number."},
        {"AUTHWARNING", "There is no Record To be Authorized/Rejected..."},
        {"lblMinorAge", "Minor Age"},
        {"lblRetireAge", "Retirement Age"},
        {"lblLastFinancialYearEnd", "LastFinancialYearEnd..."},
        {"lblSeniorCitizenAge", "SeniorCitizenAge Age"},
        {"lblCashActHead", "Cash Act.Head"},
        {"lblIBRActHead", "IBR Act.Head"},
        {"lblSIChargesHead", "SI Commission Head"},
        {"lblRemitChargesHead", "Remittance Charges Head"},
        {"lblAcceptChargesHead", "Postage Charges Head"},
        {"lblExecChargesHead", "Execution Charges Head"},
        {"lblFailChargesHead", "Failure Charges Head"},
        {"lblServiceTaxHead", "SI/Failure Service Tax Head"},
        {"lblServiceTax", "SI/Failure Service Tax"},
        {"lblDayEndType", "Day End Type"},
        {"rdoDayEndType_BranchLevel", "Branch Level"},
        {"rdoDayEndType_BankLevel", "Bank Level"},
        {"lblHolidayIB_Trans1", "Allow Interbranch"},
        {"lblHolidayIB_Trans2", "Transactions on Holiday"},
        {"rdoIB_OnHoliday_Yes", "Yes"},
        {"rdoIB_OnHoliday_No", "No"},
        {"btnSalarySuspense", ""},
        {"lblYearEndProcessDate", "YearEndProcessDate"},
        {"lblFromDate", "AMC FromDate"},
        {"btnRTGS_GL", ""},
        {"btnSuspenseAcHd", "SuspenseAcHd"},
        {"lblToDate", "AMC ToDate"},
        {"lblAmcAlertTime", "AMC AlertTime"},
        {"lblServicePeriod", "ServicePeriod"},
        {"lblGahanPeriod", "GahanPeriod"},
        {"lblSuspenseAcHead", "SuspenseAcHead"},
        {"lblSalarySuspense", "SalarySuspense"},
        {"lblRTGSGL", "RTGSGL"},
        {"lblPanDetails", "PanDetails"},
        {"lblPendingTxnAllowedDays", "PendingTxnAllowedDays"},
        {"lblEffectiveFrom", "EffectiveFrom"},
        {"lblPenalIntFromReports", "PenalIntFromReports"},
        {"lblAllowTokenNo", "AllowTokenNo"},
        {"lblAllowServiceTax", "AllowServiceTax"},
        {"lblAllowMultiShare", "AllowMultiShare"},
        {"lblAllowDenomination", "AllowDenomination"},
        {"lblAllowCashierAuthorization", "AllowCashierAuthorization"},
        {"rdoAllowAuthorizationYes", "Yes"},
        {"rdoAllowAuthorizationNo", "No"},
        {"rdoCashierAuthorizationYes", "Yes"},
        {"rdoCashierAuthorizationNo", "No"},
        {"rdoDenominationYes", "Yes"},
        {"rdoDenominationNo", "No"},
        {"rdoMultiShareYes", "Yes"},
        {"rdoMultiShareNo", "No"},
        {"rdoServiceTaxYes", "Yes"},
        {"rdoServiceTaxNo", "No"},
        {"rdoTokenNoAllowYes", "Yes"},
        {"rdoTokenNoAllowNo", "No"},
        {"txtPanDetails", "PanDetails"},
        {"tdtEffectiveFrom", "EffectiveFrom"},
        {"txtGahanPeriod", "GahanPeriod"},
        {"txtServicePeriod", "ServicePeriod"},
        {"txtSeniorCitizenAge", "SeniorCitizenAge"},
        {"txtAmcAlertTime", "AmcAlertTime"},
        {"tdtToDate", "ToDate"},
        {"tdtFromDate", "FromDate"},
        {"txtAppSuspenseAcHd", "txtAppSuspenseAcHd"},
        {"txtSalarySuspense", "txtSalarySuspense"},
        {"txtRTGS_GL", "txtRTGS_GL"},
        {"tdtYearEndProcessDate", "tdtYearEndProcessDate"},
        {"tdtLastFinancialYearEnd", "tdtLastFinancialYearEnd"},
        {"rdoExcludePenalIntFromReportsYes", "Yes"},
        {"rdoExcludePenalIntFromReportsNo", "No"},
        {"btnCashActHead", ""},
        {"btnCashActHead", ""},
        {"btnCashActHead", ""},
        {"btnCashActHead", ""},
        {"btnServiceTaxHead", ""},
        {"btnIBRActHead", ""},
        {"txtPendingTxnAllowedDays", "PendingTxnAllowedDays"},
        {"btnSIChargesHead", ""},
        {"btnRemitChargesHead", ""},
        {"btnAcceptChargesHead", ""},
        {"btnExecChargesHead", ""},
        {"btnlFailChargesHead", ""},
        {"PwdRules", "Minimum Length Should be Greater Than Or Equal to Sum No Of Spl Character, Upper Case And Number !"},
        {"MaxWarning", "Maximum Length Should be Greater than Minimum Length !"},
        {"lblPanAmount", "Cash Limit For PAN Details"},
        {"lblLogOutTime", "Auto Logout Time(minutes)"}

    };

}
